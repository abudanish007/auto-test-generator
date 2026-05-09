# Auto-Test Generator — IntelliJ Plugin

An IntelliJ IDEA plugin that automatically generates JUnit 5 unit tests for Java code using AI. Built as part of the JetBrains AI Assistant Chat team internship application.

## What it does

Right-click any Java file in the editor → click **"Generate Tests with AI"** → a test file is instantly created in the same directory with generated unit tests covering happy paths, edge cases, and boundary conditions.

No manual prompting. No copy-pasting. The AI reads your code and writes the tests for you, directly inside your IDE.

## Demo

1. Open any Java file in IntelliJ
2. Right-click inside the editor
3. Select **"Generate Tests with AI"**
4. A `*Test.java` file is created automatically in the same directory

## How it works

The plugin is made up of three components that work together:

- **GenerateTestsAction** — listens for the right-click action in the editor. It reads either the selected code or the full file content and kicks off the test generation process on a background thread, so the IDE never freezes.
- **AIService** — takes the code and sends it to an LLM via the OpenRouter API with a carefully crafted prompt asking for JUnit 5 tests with edge cases and boundary conditions. The response is parsed and returned as a clean Java string.
- **TestFileWriter** — takes the generated test code and writes it to a new file in the same directory as the source file, named `OriginalClassTest.java`.

## Project Structure

- `GenerateTestsAction.java` — Plugin action, triggered from the editor right-click menu
- `AIService.java` — Handles the LLM API call via OpenRouter and parses the response
- `TestFileWriter.java` — Creates the test file in the same directory as the source file

## Setup

1. Clone the repo
2. Get a free API key from [OpenRouter](https://openrouter.ai)
3. Set your key as an environment variable:
   `export OPENROUTER_API_KEY=your_key_here`
4. Open the project in IntelliJ IDEA
5. Run the `Run Plugin` configuration
6. In the sandbox IDE that opens, create or open any Java file
7. Right-click inside the editor and select **"Generate Tests with AI"**

## Tech Stack

- Java
- IntelliJ Platform SDK
- OpenRouter API (LLM inference)
- Gradle

## Why I built this

During my time as a Software Development Engineer at Amazon, IntelliJ was my primary IDE. Writing unit tests was always one of the most repetitive parts of the job — especially when onboarding to a new codebase mid-sprint and needing to understand and test unfamiliar code quickly.

At Amazon I also worked with KIRO — Amazon's AI-native IDE — which had a "hooks" system where AI agents would trigger automatically on events like file saves, generating documentation or tests in the background without any manual prompting. That idea stuck with me.

This plugin is a small step in that direction for IntelliJ. Instead of switching to an AI chat window, copying code, and pasting results back, the developer simply right-clicks and the AI does the work inline. It keeps you in flow.

This is the direction I believe IntelliJ's AI Assistant should grow towards — less prompting, more ambient intelligence embedded directly in the development workflow.