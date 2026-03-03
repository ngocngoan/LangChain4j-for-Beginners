# Module 01: Getting Started with LangChain4j

## Table of Contents

- [Video Walkthrough](../../../01-introduction)
- [What You'll Learn](../../../01-introduction)
- [Prerequisites](../../../01-introduction)
- [Understanding the Core Problem](../../../01-introduction)
- [Understanding Tokens](../../../01-introduction)
- [How Memory Works](../../../01-introduction)
- [How This Uses LangChain4j](../../../01-introduction)
- [Deploy Azure OpenAI Infrastructure](../../../01-introduction)
- [Run the Application Locally](../../../01-introduction)
- [Using the Application](../../../01-introduction)
  - [Stateless Chat (Left Panel)](../../../01-introduction)
  - [Stateful Chat (Right Panel)](../../../01-introduction)
- [Next Steps](../../../01-introduction)

## Video Walkthrough

Watch dis live session wey dey explain how to start to use dis module:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## What You'll Learn

Inside di quick start, you use GitHub Models to send prompts, call tools, build one RAG pipeline, and test guardrails. Dem demos show wetin fit be done — now we go switch to Azure OpenAI and GPT-5.2 and start to build production-style applications. Dis module focus on conversational AI wey dey remember context and keep state — di concepts wey dem quick start demos use for back ground but no explain.

We go use Azure OpenAI GPT-5.2 for dis guide because e strong for reasoning and make di behavior of different patterns clear. When you add memory, you go clearly sabi di difference. Dis one go make am easy to understand wetin each component dey bring come your application.

You go build one application wey go show both patterns:

**Stateless Chat** - Each request na separate one. Di model no get memory of previous messages. Na di pattern wey you use for quick start.

**Stateful Conversation** - Each request get conversation history. Di model dey keep context through many turns. Na wetin production applications need.

## Prerequisites

- Azure subscription wey get Azure OpenAI access
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Note:** Java, Maven, Azure CLI and Azure Developer CLI (azd) don already install for di devcontainer wey dem provide.

> **Note:** Dis module dey use GPT-5.2 for Azure OpenAI. The deployment dey configured automatically by `azd up` - no change di model name inside di code.

## Understanding the Core Problem

Language models no get state. Each API call na separate one. If you talk “My name is John” then ask “What's my name?”, di model no go know say you don already introduce yourself. E dey treat all requests like say na di first conversation wey you dey do.

E good for simple Q&A but e no work for real applications. Customer service bots need to remember wetin you talk. Personal assistants need context. Any multi-turn conversation need memory.

Di picture below show di two ways — for left side, na stateless call wey forget your name; for right side, na stateful call wey use ChatMemory wey remember am.

<img src="../../../translated_images/pcm/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Di difference between stateless (independent calls) and stateful (context-aware) conversations*

## Understanding Tokens

Before you enter conversation matter, e good to sabi tokens - na di basic text units wey language models dey process:

<img src="../../../translated_images/pcm/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Example how text dey break into tokens - "I love AI!" become 4 separate processing units*

Tokens na how AI models dey measure and process text. Words, punctuation, and space fit be tokens. Your model get limit on how many tokens e fit process at once (400,000 for GPT-5.2, with up to 272,000 input tokens and 128,000 output tokens). If you sabi tokens, e go help you manage conversation length and cost.

## How Memory Works

Chat memory solve di stateless wahala by keeping conversation history. Before dem send your request to di model, di framework go first put di relevant previous messages front front. When you ask "What's my name?", di system go actually send all di conversation history. This one make di model see say you already talk "My name is John."

LangChain4j get memory implementations wey dey handle dis one automatically. You go choose how many messages to keep and di framework go arrange di context window. Di picture below show how MessageWindowChatMemory dey keep sliding window of recent messages.

<img src="../../../translated_images/pcm/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory dey keep sliding window of recent messages, e dey automatically drop old ones*

## How This Uses LangChain4j

Dis module dey build on top quick start by adding Spring Boot and conversation memory. Na so di pieces dey connect:

**Dependencies** - Add two LangChain4j libraries:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**Chat Model** - Configure Azure OpenAI as one Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

The builder dey read credentials from environment variables wey `azd up` set. If you set `baseUrl` to your Azure endpoint, e make OpenAI client fit work with Azure OpenAI.

**Conversation Memory** - Track chat history with MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Create memory with `withMaxMessages(10)` to keep last 10 messages. Add user and AI messages with typed wrappers: `UserMessage.from(text)` and `AiMessage.from(text)`. Get history `memory.messages()` then send am go di model. Di service store separate memory per conversation ID so multiple users fit chat at the same time.

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) and ask:
> - "How MessageWindowChatMemory dey decide which messages to drop when window full?"
> - "Fit I do custom memory storage with database instead of in-memory?"
> - "How I fit add summarization to compress old conversation history?"

Di stateless chat endpoint no use memory - just `chatModel.chat(prompt)` like for quick start. Di stateful endpoint dey add messages go memory, gather history and join di context for each request. Same model config, different patterns.

## Deploy Azure OpenAI Infrastructure

**Bash:**
```bash
cd 01-introduction
azd up  # Choose subscription and place (eastus2 na di best)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Choose subscription and where you dey (eastus2 na beta)
```

> **Note:** If you see timeout error (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), no wahala, just run `azd up` again. Azure resources fit still dey provision in background, so retrying go help deployment finish when resources finish.

This one go:
1. Deploy Azure OpenAI resource with GPT-5.2 and text-embedding-3-small models
2. Automatically create `.env` file for project root with credentials
3. Set all necessary environment variables

**If deployment get wahala?** Check [Infrastructure README](infra/README.md) for troubleshooting tips including subdomain name conflicts, manual Azure Portal deployment steps, and model config guide.

**Confirm deployment complete:**

**Bash:**
```bash
cat ../.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Suppose show AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

> **Note:** `azd up` command dey automatically generate `.env` file. If you want update am later, fit manually edit `.env` or regenerate by running:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## Run the Application Locally

**Confirm deployment:**

Make sure `.env` file dey root folder wit Azure credentials. Run this command inside module folder (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Suppoz go show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Shiud show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start di apps:**

**Option 1: Use Spring Boot Dashboard (Best for VS Code users)**

Di devcontainer get Spring Boot Dashboard extension, wey get GUI interface to control all Spring Boot apps. You fit find am for di Activity Bar for left side of VS Code (look for Spring Boot icon).

From di Spring Boot Dashboard you fit:
- See all Spring Boot apps for di workspace
- Start/stop apps with one click
- View app logs as e dey happen
- Check app status

Just click di play button beside "introduction" to start dis module, or start all modules at once.

<img src="../../../translated_images/pcm/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard inside VS Code — start, stop, and monitor all modules for one place*

**Option 2: Use shell scripts**

Start all web apps (modules 01-04):

**Bash:**
```bash
cd ..  # From root directory
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # From root directory
.\start-all.ps1
```

Or start only dis module:

**Bash:**
```bash
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Both scripts go load environment variables from root `.env` file and go build the JARs if dem no dey.

> **Note:** If you want build all modules manually before starting:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Open http://localhost:8080 for your browser.

**To stop:**

**Bash:**
```bash
./stop.sh  # Dis module only
# Or
cd .. && ./stop-all.sh  # All di modules
```

**PowerShell:**
```powershell
.\stop.ps1  # Dis module only
# Or
cd ..; .\stop-all.ps1  # All di modules
```

## Using the Application

Di application get web interface with two chat types side-by-side.

<img src="../../../translated_images/pcm/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard wey show Simple Chat (stateless) and Conversational Chat (stateful) options*

### Stateless Chat (Left Panel)

Try am first. Tell "My name is John" then quickly ask "What's my name?" Di model no go remember because each message na separate one. Dis one show di core problem with basic language model integration - no conversation context.

<img src="../../../translated_images/pcm/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI no remember your name from your last message*

### Stateful Chat (Right Panel)

Now try di same thing here. Tell "My name is John" then ask "What's my name?" This time e remember. Di difference na MessageWindowChatMemory - e dey keep conversation history and add am with every request. Na so production conversational AI dey work.

<img src="../../../translated_images/pcm/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI remember your name from earlier conversation*

Both sides use di same GPT-5.2 model. Di only difference na memory. Dis one make clear how memory fit improve your app and why e important for real use.

## Next Steps

**Next Module:** [02-prompt-engineering - Prompt Engineering with GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigation:** [← Previous: Module 00 - Quick Start](../00-quick-start/README.md) | [Back to Main](../README.md) | [Next: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis document na di one wey AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator) translate. Even though we dey try make e correct, abeg make you sabi say automated translations fit get some errors or mistake. Di original document for dia own language na di main correct source. If na important info, e better make pro human translator do am. We no gree take any blame if person misunderstand or misinterpret tins because of dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->