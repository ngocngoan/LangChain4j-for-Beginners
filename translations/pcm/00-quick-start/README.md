# Module 00: Quick Start

## Table of Contents

- [Introduction](../../../00-quick-start)
- [Wetìn be LangChain4j?](../../../00-quick-start)
- [LangChain4j Dependencies](../../../00-quick-start)
- [Prerequisites](../../../00-quick-start)
- [Setup](../../../00-quick-start)
  - [1. Get Your GitHub Token](../../../00-quick-start)
  - [2. Set Your Token](../../../00-quick-start)
- [Run the Examples](../../../00-quick-start)
  - [1. Basic Chat](../../../00-quick-start)
  - [2. Prompt Patterns](../../../00-quick-start)
  - [3. Function Calling](../../../00-quick-start)
  - [4. Document Q&A (RAG)](../../../00-quick-start)
  - [5. Responsible AI](../../../00-quick-start)
- [Wetìn Each Example Dey Show](../../../00-quick-start)
- [Next Steps](../../../00-quick-start)
- [Troubleshooting](../../../00-quick-start)

## Introduction

Dis quickstart na to help you start LangChain4j fast-fast. E go yan the basics for building AI applications with LangChain4j and GitHub Models. For the next modules, you go use Azure OpenAI with LangChain4j build more better applications.

## Wetìn be LangChain4j?

LangChain4j na Java library wey e make e easy to build AI-powered applications. Instead make you dey worry about HTTP clients and JSON parsing, you go just dey work with clean Java APIs.

The "chain" for LangChain mean say you dey join several components together—like you fit join prompt to model then model to parser, or join many AI calls where one output become next input. Dis quick start na to show you the basics before you go inside the gbas gbos chains.

<img src="../../../translated_images/pcm/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Joining components for LangChain4j - building blocks wey connect to build power AI workflows*

We go use three important components:

**ChatLanguageModel** - Na the interface for AI model interactions. You call `model.chat("prompt")` and e go give you answer. We dey use `OpenAiOfficialChatModel` wey dey work with OpenAI-compatible endpoints like GitHub Models.

**AiServices** - E dey create type-safe AI service interfaces. You define methods, put `@Tool` for them, LangChain4j go handle the rest. AI go automatically call your Java methods if e need am.

**MessageWindowChatMemory** - E dey keep conversation history. If no be so, each request go be independent. If you use am, AI go remember previous messages and keep context for many turns.

<img src="../../../translated_images/pcm/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j architecture - core parts wey dey work together to power your AI applications*

## LangChain4j Dependencies

Dis quick start dey use two Maven dependencies inside [`pom.xml`](../../../00-quick-start/pom.xml):

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

The `langchain4j-open-ai-official` module na im get `OpenAiOfficialChatModel` class wey connect to OpenAI-compatible APIs. GitHub Models use the same API format, so you no need any special adapter—just set the base URL to `https://models.github.ai/inference`.

## Prerequisites

**You dey use Dev Container?** Java and Maven don already dey installed. You only need GitHub Personal Access Token.

**For Local Development:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (instructions dey below)

> **Note:** Dis module dey use `gpt-4.1-nano` from GitHub Models. No change the model name for the code—e don already set to work with GitHub models wey dey available.

## Setup

### 1. Get Your GitHub Token

1. Go [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Click "Generate new token"
3. Put descriptive name (for example, "LangChain4j Demo")
4. Set expiration (7 days na better)
5. For "Account permissions", find "Models" make e be "Read-only"
6. Click "Generate token"
7. Copy and save your token—no go see am again

### 2. Set Your Token

**Option 1: If you dey use VS Code (Na better option)**

If you dey VS Code, add your token to `.env` file wey dey project root:

If `.env` file no dey, copy `.env.example` to `.env` or create new `.env` file for project root.

**Example `.env` file:**
```bash
# Inside /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Then you fit just right-click any demo file (like `BasicChatDemo.java`) inside Explorer and select **"Run Java"** or use the launch configurations from Run and Debug panel.

**Option 2: Using Terminal**

Set token as environment variable:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Run the Examples

**Using VS Code:** Just right-click any demo file inside Explorer and select **"Run Java"**, or use launch configurations from Run and Debug panel (just make sure say you don add your token to `.env` file first).

**Using Maven:** Or you fit run from command line:

### 1. Basic Chat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Prompt Patterns

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

E show zero-shot, few-shot, chain-of-thought, and role-based prompting.

### 3. Function Calling

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI go automatically call your Java methods when e need am.

### 4. Document Q&A (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

You fit ask question about content wey dey for `document.txt`.

### 5. Responsible AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

See how AI safety filters dey block bad content.

## Wetìn Each Example Dey Show

**Basic Chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Start for here to see LangChain4j for e simplest form. You go create `OpenAiOfficialChatModel`, send prompt with `.chat()`, then get answer back. Dis na foundation: how you take initialize models with custom endpoints and API keys. When you understand dis pattern, everything else go easy.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) and ask:
> - "How I fit switch from GitHub Models go Azure OpenAI for dis code?"
> - "Wetìn other parameters I fit configure for OpenAiOfficialChatModel.builder()?"
> - "How I fit add streaming responses instead make I wait complete response?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Now wey you sabi how to talk to model, make we look wetin you go talk. Dis demo dey use same model setup but e show five different prompting patterns. Try zero-shot prompts for direct instructions, few-shot prompts wey learn from example, chain-of-thought prompts wey show reasoning steps, and role-based prompts wey set context. You go see how same model go give different results depending on how you set your request.

The demo also dey show prompt templates, dem powerful for making reusable prompts with variables.
The example below show prompt wey dey use LangChain4j `PromptTemplate` to fill variables. AI go answer based on destination and activity wey you provide.

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) and ask:
> - "Wetìn be the difference between zero-shot and few-shot prompting, and when I go use each?"
> - "How temperature parameter dey affect model responses?"
> - "Wetin be some ways to prevent prompt injection attacks for production?"
> - "How I fit create reusable PromptTemplate objects for common patterns?"

**Tool Integration** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Dis na where LangChain4j dey powerful. You go use `AiServices` make AI assistant wey fit call your Java methods. Just put `@Tool("description")` on methods, LangChain4j go do the rest—AI go decide automatic when to use each tool depending on wetin user ask. Dis na to show function calling, one key way to make AI wey fit take action, no just answer questions.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) and ask:
> - "How @Tool annotation work and wetin LangChain4j dey do with am behind the scenes?"
> - "Fit AI call many tools one after another to solve complex wahala?"
> - "Wetin happen if tool throw exception - how I go handle errors?"
> - "How I fit add real API no be this calculator example?"

**Document Q&A (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Here you go see the base of RAG (retrieval-augmented generation). Instead to depend on model training data, you load content from [`document.txt`](../../../00-quick-start/document.txt) and put am inside prompt. AI go answer based on your document, no be general knowledge. Na first step to build system wey fit work with your own data.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Note:** Dis simple way go put all document inside prompt. If file big pass (>10KB), you fit exceed context limit. Module 03 go show chunking and vector search for production RAG systems.

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) and ask:
> - "How RAG dey prevent AI hallucinations compared to model training data?"
> - "Wetìn be the difference between dis simple way and to use vector embeddings for retrieval?"
> - "How I fit scale am to handle many documents or big knowledge bases?"
> - "Wetìn be best practice for structure prompt make AI use only the context wey you give?"

**Responsible AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Build AI safety with defense in depth. Dis demo dey show two layers of protection wey dey work together:

**Part 1: LangChain4j Input Guardrails** - E dey block dangerous prompts before dem reach the LLM. You fit create your own guardrails wey go check prohibited keywords or patterns. Dem dey run for your code, so e fast and e no get cost.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**Part 2: Provider Safety Filters** - GitHub Models get built-in filters wey go catch wetin your guardrails fit miss. You go see hard blocks (HTTP 400 errors) for serious violations and soft refusals where AI go politely decline.

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) and ask:
> - "Wetìn be InputGuardrail and how I fit create my own?"
> - "Wetìn be difference between hard block and soft refusal?"
> - "Why make I use both guardrails and provider filters together?"

## Next Steps

**Next Module:** [01-introduction - Getting Started with LangChain4j and gpt-5 on Azure](../01-introduction/README.md)

---

**Navigation:** [← Back to Main](../README.md) | [Next: Module 01 - Introduction →](../01-introduction/README.md)

---

## Troubleshooting

### First-Time Maven Build

**Issue**: First time you run `mvn clean compile` or `mvn package` e go take long time (10-15 minutes)

**Cause**: Maven need download all dependencies for project (Spring Boot, LangChain4j libraries, Azure SDKs, etc.) for first build.

**Solution**: Dis na normal thing. Later builds go quick because dependencies don dey cached for your machine. Download time fit depend on your network speed.
### PowerShell Maven Command Syntax

**Issue**: Maven commands dey fail wit error `Unknown lifecycle phase ".mainClass=..."`

**Cause**: PowerShell dey interpret `=` as if na variable assignment operator, e dey spoil Maven property syntax

**Solution**: Use di stop-parsing operator `--%` before di Maven command:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Di `--%` operator dey tell PowerShell make e pass all di remaining arguments as dem be to Maven without to interpret am.

### Windows PowerShell Emoji Display

**Issue**: AI responses dey show garbage characters (like `????` or `â??`) instead of emojis for PowerShell

**Cause**: PowerShell default encoding no fit support UTF-8 emojis

**Solution**: Run dis command before you run Java applications:
```cmd
chcp 65001
```

Dis one dey force UTF-8 encoding for di terminal. If you want, you fit use Windows Terminal wey get beta Unicode support.

### Debugging API Calls

**Issue**: Authentication errors, rate limits, or unexpected responses from di AI model

**Solution**: Di examples get `.logRequests(true)` and `.logResponses(true)` wey dey show API calls for di console. Dis one go help you troubleshoot authentication errors, rate limits, or unexpected responses. Remove these flags for production to reduce log noise.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis document na tobi AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator) wey translate am. Even though we try make am correct, abeg mek you sabi say automatic translation fit get error or wahala. The original document wey original language na di correct one. If na serious thing, e better make person wey sabi translate am for human help you. We no go take any blame if person no understand or if dem misunderstand because of this translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->