# Module 00: Quick Start

## Table of Contents

- [Introduction](../../../00-quick-start)
- [Wet́in be LangChain4j?](../../../00-quick-start)
- [LangChain4j Dependencies](../../../00-quick-start)
- [Wetin You Need Before](../../../00-quick-start)
- [Setup](../../../00-quick-start)
  - [1. Collect Your GitHub Token](../../../00-quick-start)
  - [2. Set Your Token](../../../00-quick-start)
- [Run the Examples](../../../00-quick-start)
  - [1. Basic Chat](../../../00-quick-start)
  - [2. Prompt Patterns](../../../00-quick-start)
  - [3. Function Calling](../../../00-quick-start)
  - [4. Document Q&A (Easy RAG)](../../../00-quick-start)
  - [5. Responsible AI](../../../00-quick-start)
- [Wet́in Each Example Dey Show](../../../00-quick-start)
- [Next Steps](../../../00-quick-start)
- [Troubleshooting](../../../00-quick-start)

## Introduction

Dis quickstart na to help you start sharp-sharp wit LangChain4j. E dey cover di main basics to build AI apps wit LangChain4j and GitHub Models. For di next modules, you go use Azure OpenAI wit LangChain4j to build better advanced apps dem.

## Wet́in be LangChain4j?

LangChain4j na one Java library wey make am easy to build AI-powered apps. Instead to dey handle HTTP clients and JSON parsing, you go just use clean Java APIs.

Di "chain" for LangChain mean say you fit join many parts together - you fit join prompt to model to parser, or join many AI calls wey one output na di input for the next. Dis quick start na to show di basics before we go enter more complex chains.

<img src="../../../translated_images/pcm/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Joining components for LangChain4j - building blocks wey connect to create strong AI workflows*

We go use three main parts:

**ChatModel** - Na di interface for AI model interactions. You fit call `model.chat("prompt")` come get response string. We use `OpenAiOfficialChatModel` wey dey work wit OpenAI compatible endpoints like GitHub Models.

**AiServices** - E dey create type-safe AI service interfaces. You go define methods, put `@Tool` on top, and LangChain4j go handle di arranging. Di AI go automatically call your Java methods when e need am.

**MessageWindowChatMemory** - E dey keep conversation history. Without am, every request obvious to be separate. With am, AI get memory of past messages and fit keep context across many turns.

<img src="../../../translated_images/pcm/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j architecture - main parts dey work together to run your AI apps*

## LangChain4j Dependencies

Dis quick start dey use three Maven dependencies for di [`pom.xml`](../../../00-quick-start/pom.xml):

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

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

Di `langchain4j-open-ai-official` module get `OpenAiOfficialChatModel` class wey dey connect to OpenAI-compatible APIs. GitHub Models dey use same API format, so no special adapter needed - just point base URL to `https://models.github.ai/inference`.

Di `langchain4j-easy-rag` module dey provide automatic document splitting, embedding, and retrieval so you fit build RAG apps without to manually setup every step.

## Wetin You Need Before

**You dey use Dev Container?** Java and Maven don install finish. You just need GitHub Personal Access Token.

**For your local machine:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (instruction dey below)

> **Note:** Dis module dey use `gpt-4.1-nano` from GitHub Models. No change di model name for code - e set to work wit models wey GitHub get.

## Setup

### 1. Collect Your GitHub Token

1. Go [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Click "Generate new token"
3. Put name wey you fit remember (example, "LangChain4j Demo")
4. Set expiration (7 days na better)
5. For "Account permissions", find "Models" put am "Read-only"
6. Click "Generate token"
7. Copy your token and save am - you no go see am again

### 2. Set Your Token

**Option 1: Using VS Code (Better)**

If you dey use VS Code, add your token inside `.env` file for di project root:

If `.env` file no dey, copy `.env.example` go `.env` or create `.env` file yourself for project root.

**Example `.env` file:**
```bash
# Inside /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

After that, you fit just right-click any demo file (like `BasicChatDemo.java`) for Explorer and choose **"Run Java"** or use launch configurations for Run and Debug panel.

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

**Using VS Code:** Just right-click any demo file inside Explorer and select **"Run Java"**, or use launch configs from Run and Debug panel (make sure say your token dey `.env` file first).

**Using Maven:** You fit run am from command line:

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

### 4. Document Q&A (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Ask questions about your documents using Easy RAG with automatic embedding and retrieval.

### 5. Responsible AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

See how AI safety filters block harmful content.

## Wet́in Each Example Dey Show

**Basic Chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Start here to see LangChain4j for e simplest form. You go create `OpenAiOfficialChatModel`, send prompt wit `.chat()`, and get response back. Dis dey show foundation: how to initialize models wit custom endpoints and API keys. When you sabi dis pattern, everything else fit build on top.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ask:
> - "How I go switch from GitHub Models go Azure OpenAI for dis code?"
> - "Which oda parameters I fit set inside OpenAiOfficialChatModel.builder()?"
> - "How I fit add streaming responses instead of dey wait make complete response land?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Now you know how to yarn to model, make we check wetin you go yarn. Dis demo dey use same model setup but e show five different prompting patterns. Try zero-shot prompts for direct commands, few-shot prompts wey learn from examples, chain-of-thought prompts wey show reason steps, and role-based prompts wey set context. You go see how same model fit give plenty different results based on how you frame your ask.

Di demo also show prompt templates, wey be strong way to create reusable prompts wit variables.
Below example show prompt wey use LangChain4j `PromptTemplate` to fill variables. AI go answer based on di place and activity wey you give.

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

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ask:
> - "Wetin be di difference between zero-shot and few-shot prompting, and when I suppose use each?"
> - "How temperature parameter dey affect model responses?"
> - "Which kind tricks fit stop prompt injection attacks for production?"
> - "How I fit create reusable PromptTemplate objects for common patterns?"

**Tool Integration** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Dis na where LangChain4j sharp well well. You go use `AiServices` create AI assistant wey fit call your Java methods. Just put `@Tool("description")` for methods and LangChain4j go handle the rest - AI go decide wen e suppose use each tool based on wetin user ask. Dis na di function calling, one key way to build AI wey fit do actions, no be only to answer questions.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ask:
> - "How @Tool annotation dey work and wetin LangChain4j dey do wit am behind scenes?"
> - "Fit AI call many tools one after anoda to solve hard problems?"
> - "Wetin dey happen if tool throw exception - how I suppose handle errors?"
> - "How I go fit integrate real API instead of dis calculator example?"

**Document Q&A (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Here you go see RAG (retrieval-augmented generation) using LangChain4j "Easy RAG" way. Documents go load, e go split and embed automatically for in-memory store, then content retriever go supply relevant chunks to AI at query time. AI go answer based on your documents, no be general knowledge.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ask:
> - "How RAG dey stop AI hallucinations compared to di model training data?"
> - "Wetin be di difference between dis easy way and custom RAG pipeline?"
> - "How I go scale dis to handle many documents or bigger knowledge bases?"

**Responsible AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Build AI safety wit strong protection layers. Dis demo show two layers wey dey work together:

**Part 1: LangChain4j Input Guardrails** - E dey block dangerous prompts before dem reach LLM. You fit create custom guardrails dat check for forbidden keywords or patterns. Dem dey run inside your code, so e fast and no cost.

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

**Part 2: Provider Safety Filters** - GitHub Models get built-in filters wey catch wetin your guardrails fit miss. You go see hard blocks (HTTP 400 errors) for serious violation and soft refusals wey AI polite decline.

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ask:
> - "Wetin be InputGuardrail and how I fit create my own?"
> - "Wetin be di difference between hard block and soft refusal?"
> - "Why I suppose use both guardrails and provider filters together?"

## Next Steps

**Next Module:** [01-introduction - Getting Started wit LangChain4j and gpt-5 for Azure](../01-introduction/README.md)

---

**Navigation:** [← Back to Main](../README.md) | [Next: Module 01 - Introduction →](../01-introduction/README.md)

---

## Troubleshooting

### First-Time Maven Build

**Problem:** First time you run `mvn clean compile` or `mvn package` e fit take long (10-15 minutes)

**Why:** Maven dey download all project dependencies (Spring Boot, LangChain4j libraries, Azure SDKs, etc.) for di first build.

**Solution:** Dis na normal thing. Di builds after go fast well well because dependencies go save locally. Download time depend on your internet speed.

### PowerShell Maven Command Syntax

**Problem:** Maven commands dey fail wit error `Unknown lifecycle phase ".mainClass=..."`
**Cause**: PowerShell dey interpret `=` as variable assignment operator, e dey break Maven property syntax

**Solution**: Use the stop-parsing operator `--%` before the Maven command:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

The `--%` operator dey tell PowerShell make e pass all remaining arguments literally to Maven without interpretation.

### Windows PowerShell Emoji Display

**Issue**: AI responses dey show garbage characters (e.g., `????` or `â??`) instead of emojis inside PowerShell

**Cause**: PowerShell default encoding no dey support UTF-8 emojis

**Solution**: Run this command before you run Java applications:
```cmd
chcp 65001
```

This one dey force UTF-8 encoding for the terminal. You fit also use Windows Terminal wey get better Unicode support.

### Debugging API Calls

**Issue**: Authentication errors, rate limits, or unexpected responses from the AI model

**Solution**: The examples get `.logRequests(true)` and `.logResponses(true)` wey go show API calls for the console. This one dey help troubleshoot authentication errors, rate limits, or unexpected responses. Remove these flags for production to reduce log noise.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Warning**:
Dis document na wetin AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator) translate. Even though we try make e correct, make you sabi say automatic translation fit get some mistake or no too correct. Di original document for im own language na di correct one wey you suppose use. If na important information, better make human professional translate am. We no go responsible if person miss the meaning or understand am wrong because of dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->