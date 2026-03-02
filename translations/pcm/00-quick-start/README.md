# Module 00: Quick Start

## Table of Contents

- [Introduction](../../../00-quick-start)
- [Wetɪn be LangChain4j?](../../../00-quick-start)
- [LangChain4j Dependencies](../../../00-quick-start)
- [Prerequisites](../../../00-quick-start)
- [Setup](../../../00-quick-start)
  - [1. Get Your GitHub Token](../../../00-quick-start)
  - [2. Set Your Token](../../../00-quick-start)
- [Run the Examples](../../../00-quick-start)
  - [1. Basic Chat](../../../00-quick-start)
  - [2. Prompt Patterns](../../../00-quick-start)
  - [3. Function Calling](../../../00-quick-start)
  - [4. Document Q&A (Easy RAG)](../../../00-quick-start)
  - [5. Responsible AI](../../../00-quick-start)
- [Wetɪn Each Example Dey Show](../../../00-quick-start)
- [Next Steps](../../../00-quick-start)
- [Troubleshooting](../../../00-quick-start)

## Introduction

Dis quickstart na to make you start quickly wit LangChain4j. E cover di simple simple basics of how to build AI applications wit LangChain4j and GitHub Models. For di next modules, you go change go Azure OpenAI and GPT-5.2 and learn deep pass about di concepts.

## Wetɪn be LangChain4j?

LangChain4j na Java library wey dey make e easy to build AI-powered applications. Instead make you dey use HTTP clients and dey parse JSON, you go dey work wit clean Java APIs.

Di "chain" for LangChain mean say you go join different components together - fit be say you link prompt go model go parser, or join many AI calls where output from one go enter as input go next. Dis quick start dey focus on di basics before dem waka for complex chains.

<img src="../../../translated_images/pcm/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Joining components for LangChain4j - di building blocks dey connect to make strong AI workflows*

We go use three core components:

**ChatModel** - Na interface wey you use for AI model interaction. You fit call `model.chat("prompt")` come get response string. We dey use `OpenAiOfficialChatModel` wey dey work wit OpenAI-style endpoints like GitHub Models.

**AiServices** - E dey create type-safe AI service interfaces. You define methods, put `@Tool` annotation, LangChain4j go handle di orchestration. AI go automatically call your Java methods when e need am.

**MessageWindowChatMemory** - E dey keep all di conversation history. Without am, each request be like fresh one. With am, AI dey remember previous messages and keep di context over different turns.

<img src="../../../translated_images/pcm/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j architecture - core components dey work together to power your AI applications*

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

Di `langchain4j-open-ai-official` module dey provide `OpenAiOfficialChatModel` class wey dey connect to OpenAI-style APIs. GitHub Models dey use di same API format, so no special adapter need - just point di base URL to `https://models.github.ai/inference`.

Di `langchain4j-easy-rag` module dey provide automatic document splitting, embedding, and retrieval so you fit build RAG apps without to set every step manually.

## Prerequisites

**You dey use Dev Container?** Java and Maven don already install. You only need GitHub Personal Access Token.

**Local Development:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (instructions dey below)

> **Note:** Dis module dey use `gpt-4.1-nano` from GitHub Models. No change di model name inside di code - e be configured for GitHub models dem wey dey.

## Setup

### 1. Get Your GitHub Token

1. Go [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Click "Generate new token"
3. Set name wey make sense (like "LangChain4j Demo")
4. Set expiration (7 days dey recommended)
5. For "Account permissions", find "Models" set am to "Read-only"
6. Click "Generate token"
7. Copy and save your token - you no go see am again

### 2. Set Your Token

**Option 1: Using VS Code (Better Option)**

If you dey use VS Code, put your token for `.env` file inside project root:

If `.env` no dey, copy `.env.example` go `.env` or create new `.env` file for project root.

**Example `.env` file:**
```bash
# Inside /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

You fit just right-click any demo file (for example `BasicChatDemo.java`) for Explorer and select **"Run Java"** or use launch configurations for Run and Debug panel.

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

**With VS Code:** Just right-click any demo file inside Explorer and select **"Run Java"**, or use launch configurations from Run and Debug panel (make sure say you don add token to `.env` file first).

**With Maven:** Or you fit run from command line:

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

E dey show zero-shot, few-shot, chain-of-thought, and role-based prompting.

### 3. Function Calling

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI go automatically call your Java methods when e need.

### 4. Document Q&A (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

You fit ask questions about your documents using Easy RAG wey get automatic embedding and retrieval.

### 5. Responsible AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

See as AI safety filters dey block harmful content.

## Wetɪn Each Example Dey Show

**Basic Chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Start here to see LangChain4j for e simplest form. You go create `OpenAiOfficialChatModel`, send prompt wit `.chat()`, then get response. This one na di foundation: how to start models wit custom endpoints and API keys. Once you understand dis pattern, every other thing go build on top.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) and ask:
> - "How I go waka from GitHub Models go Azure OpenAI for this code?"
> - "Wetin other parameters I fit configure inside OpenAiOfficialChatModel.builder()?"
> - "How I fit add streaming responses instead to dey wait for full response?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Now wey you sabi how to talk to model, mek we check wetin you go talk to am. Dis demo dey use di same model setup but e get five different prompt patterns. Try zero-shot prompts for direct instructions, few-shot prompts wey learn from examples, chain-of-thought prompts wey show reasoning steps, and role-based prompts wey set context. You go see how di same model fit give different results depending on how you take arrange your request.

Di demo dey also show prompt templates, wey na strong way to create reusable prompts wit variables.
Below example show prompt wit LangChain4j `PromptTemplate` to fill variables. AI go answer base on destination and activity wey you give.

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
> - "Wetin be di difference between zero-shot and few-shot prompting, and when I suppose use each?"
> - "How di temperature parameter dey affect model responses?"
> - "Wetin be some ways to stop prompt injection attacks for production?"
> - "How I fit create reusable PromptTemplate objects for common patterns?"

**Tool Integration** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Na here LangChain4j dey powerful. You go use `AiServices` take build AI assistant wey fit call your Java methods. Just annotate methods with `@Tool("description")`, LangChain4j go handle the rest - AI go decide automatically when to use tool depending on wetin user talk. Dis na function calling, one important technique to build AI wey fit do actions, no only answer questions.

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

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) and ask:
> - "How di @Tool annotation dey work and wetin LangChain4j dey do wit am for background?"
> - "Fit AI call many tools one after di other to solve complex problems?"
> - "Wetin happen if tool throw exception - how I fit handle errors?"
> - "How I go integrate real API instead of dis calculator example?"

**Document Q&A (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Here you go see RAG (retrieval-augmented generation) using LangChain4j "Easy RAG". Documents go load, e go split and embed automatically inside memory store, then content retriever go supply relevant parts to AI when you ask. AI go answer based on your documents no be general knowledge.

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

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) and ask:
> - "How RAG dey prevent AI hallucinations compared to using model training data?"
> - "Wetin be di difference between this easy way and custom RAG pipeline?"
> - "How I go scale am to handle many documents or bigger knowledge bases?"

**Responsible AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Build AI safety wit defense inside depth. Dis demo dey show two layers of protection wey dey work together:

**Part 1: LangChain4j Input Guardrails** - Block dangerous prompts before dem reach LLM. Create custom guardrails to check for forbidden keywords or patterns. Dem dey run inside your code, so dem quick and free.

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

**Part 2: Provider Safety Filters** - GitHub Models get filters wey fit catch wetin your guardrails fit miss. You go see hard blocks (HTTP 400 errors) for serious violations and soft refusals where AI go politely refuse.

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) and ask:
> - "Wetin be InputGuardrail and how I fit create my own?"
> - "Wetin be difference between hard block and soft refusal?"
> - "Why I suppose use both guardrails and provider filters together?"

## Next Steps

**Next Module:** [01-introduction - Getting Started with LangChain4j](../01-introduction/README.md)

---

**Navigation:** [← Back to Main](../README.md) | [Next: Module 01 - Introduction →](../01-introduction/README.md)

---

## Troubleshooting

### First-Time Maven Build

**Issue**: First time `mvn clean compile` or `mvn package` go take long (10-15 minutes)

**Cause**: Maven need to download all project dependencies (Spring Boot, LangChain4j libraries, Azure SDKs, etc.) for first build.

**Solution**: Dis normal. After first time, builds go fast because dependencies go dey cached locally. Download time depend on your network speed.

### PowerShell Maven Command Syntax

**Issue**: Maven commands dey fail wit error `Unknown lifecycle phase ".mainClass=..."`
**Cause**: PowerShell de interpret `=` as variable assignment operator, wey go break Maven property syntax

**Solution**: Make you use stop-parsing operator `--%` before Maven command:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

The `--%` operator dey tell PowerShell make e pass all remaining arguments for Maven without any interpretation.

### Windows PowerShell Emoji Display

**Issue**: AI response go show rubbish characters (like `????` or `â??`) instead of emojis for PowerShell

**Cause**: PowerShell default encoding no dey support UTF-8 emojis

**Solution**: Run dis command before you run Java applications:
```cmd
chcp 65001
```

Dis one go force UTF-8 encoding to dey for terminal. Another option na to use Windows Terminal wey get better Unicode support.

### Debugging API Calls

**Issue**: Authentication errors, rate limits, or unexpected responses from the AI model

**Solution**: Di examples get `.logRequests(true)` and `.logResponses(true)` wey dey show API calls for console. Dis one go help troubleshoot authentication errors, rate limits, or unexpected responses. Remove dis flags when you move to production to reduce log noise.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis dokument don translate wit AI translation service wey dey call [Co-op Translator](https://github.com/Azure/co-op-translator). Even though we dey try make am correct, abeg sabi say automated translation fit get mistakes or errors. Di original dokument wey dem write for im own language na im get final trust. If na important info, make you try use professional human translation. We no go carry any blame if person no understand or if dem use dis translation wrongly.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->