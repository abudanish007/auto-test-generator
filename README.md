# Auto-Test Generator — IntelliJ Plugin

An IntelliJ IDEA plugin that automatically generates JUnit 5 unit tests for Java code using AI.

## What it does

Right-click any Java file in the editor → click **"Generate Tests with AI"** → a test file is instantly created in the same directory with generated unit tests covering happy paths, edge cases, and boundary conditions.

## Demo

1. Open any Java file in IntelliJ
2. Right-click inside the editor
3. Select **"Generate Tests with AI"**
4. A `*Test.java` file is created automatically

## How it works

- **GenerateTestsAction** — reads selected code or full file when triggered
- **AIService** — sends code to LLM via OpenRouter API
- **TestFileWriter** — writes the generated tests to a new file

## Project Structure

- `GenerateTestsAction.java` — Plugin action, triggered from editor right-click menu
- `AIService.java` — Handles LLM API call via OpenRouter
- `TestFileWriter.java` — Creates the test file in the project

## Setup

1. Clone the repo
2. Set your OpenRouter API key as an environment variable:
   `export OPENROUTER_API_KEY=your_key_here`
3. Open in IntelliJ IDEA
4. Run the `Run Plugin` configuration
5. In the sandbox IDE, open any Java file and right-click

## Tech Stack

- Java
- IntelliJ Platform SDK
- OpenRouter API (LLM inference)
- Gradle

## Why I built this

This plugin was built as part of the JetBrains AI Assistant Chat team internship application.
The idea came from my experience at Amazon using IntelliJ daily — writing unit tests is
repetitive and time-consuming. This plugin automates that step using AI, keeping the
developer in flow.

It was also inspired by KIRO's "hooks" system — Amazon's AI-native IDE where agents trigger
automatically on events like file saves to generate tests or documentation in the background.
This plugin brings a similar concept to IntelliJ: instead of manually prompting an AI chat,
the developer simply right-clicks and the AI does the work inline, without breaking focus.

This is the direction I believe IntelliJ's AI Assistant should grow towards — less prompting,
more ambient intelligence embedded in the workflow.plugin was built as part of the JetBrains AI Assistant Chat team internship application. The idea came from my experience at Amazon using IntelliJ daily — writing unit tests is repetitive and time-consuming. This plugin automates that step using AI, keeping the developer in flow.