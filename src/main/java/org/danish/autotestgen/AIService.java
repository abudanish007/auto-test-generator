package org.danish.autotestgen;

import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;

public class AIService {

    private static final String API_KEY = System.getenv("OPENROUTER_API_KEY");
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";

    public static String generateTests(String code) throws Exception {
        String prompt = "You are an expert Java developer. "
                + "Generate comprehensive JUnit 5 unit tests for the following Java code. "
                + "Include edge cases, happy paths, and boundary conditions. "
                + "Return ONLY the Java test class code, no explanations, no markdown.\n\n"
                + "Code to test:\n" + code;

        String requestBody = "{"
                + "\"model\": \"openrouter/auto\","
                + "\"messages\": ["
                + "  {\"role\": \"system\", \"content\": \"You are a Java testing expert. Return only valid Java code.\"},"
                + "  {\"role\": \"user\", \"content\": " + escapeJson(prompt) + "}"
                + "],"
                + "\"max_tokens\": 2000"
                + "}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .header("HTTP-Referer", "https://github.com/danish/auto-test-generator")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("API error " + response.statusCode() + ": " + response.body());
        }

        return parseContent(response.body());
    }

    private static String parseContent(String json) {
        String marker = "\"content\":";
        int idx = json.indexOf(marker);
        idx = json.indexOf(marker, idx + 1);
        if (idx == -1) {
            idx = json.indexOf("\"content\":");
        }
        if (idx == -1) throw new RuntimeException("Could not parse API response");

        String after = json.substring(idx + marker.length()).trim();
        if (after.startsWith("\"")) after = after.substring(1);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < after.length(); i++) {
            char c = after.charAt(i);
            if (c == '\\' && i + 1 < after.length()) {
                char next = after.charAt(i + 1);
                switch (next) {
                    case 'n': result.append('\n'); i++; break;
                    case 't': result.append('\t'); i++; break;
                    case '"': result.append('"'); i++; break;
                    case '\\': result.append('\\'); i++; break;
                    default: result.append(c);
                }
            } else if (c == '"') {
                break;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private static String escapeJson(String text) {
        return "\"" + text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t") + "\"";
    }
}