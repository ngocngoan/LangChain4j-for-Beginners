# Module 01: Getting Started wit LangChain4j

## Table of Contents

- [Video Walkthrough](../../../01-introduction)
- [Wetin You Go Learn](../../../01-introduction)
- [Wetins You Need Before](../../../01-introduction)
- [Understanding Di Core Wahala](../../../01-introduction)
- [Understanding Tokens](../../../01-introduction)
- [How Memory Dey Work](../../../01-introduction)
- [How Dis One Dey Use LangChain4j](../../../01-introduction)
- [Deploy Azure OpenAI Infrastructure](../../../01-introduction)
- [Run Di Application for Local](../../../01-introduction)
- [How to Use Di Application](../../../01-introduction)
  - [Stateless Chat (Left Panel)](../../../01-introduction)
  - [Stateful Chat (Right Panel)](../../../01-introduction)
- [Next Steps](../../../01-introduction)

## Video Walkthrough

Watch dis live session wey dey explain how to start dis module: [Getting Started with LangChain4j - Live Session](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Wetin You Go Learn

If you don finish di quick start, you don see how to send prompts and get responses. Na dat be di foundation, but real aplikasi dem need more. Dis module go teach you how to build conversational AI wey dey remember context and dey maintain state - di kain difference wey dey between one-off demo and one wey production-ready.

We go use Azure OpenAI GPT-5.2 for dis guide because e get advanced reasoning skill wey go make you fit see how different patterns dem dey behave well well. When you add memory, you go clear see di difference. E go make am easy to understand wetin each part dey bring come your application.

You go build one application wey go show both patterns:

**Stateless Chat** - Every request na im stand-alone. Di model no get memory about previous messages. Na di pattern wey you use for quick start.

**Stateful Conversation** - Every request dey carry conversation history. Di model dey maintain context for many turns. Na wetin production applications need be dis one.

## Wetins You Need Before

- Azure subscription wey get Azure OpenAI access
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Note:** Java, Maven, Azure CLI and Azure Developer CLI (azd) dey pre-installed for di devcontainer wey dem provide.

> **Note:** Dis module dey use GPT-5.2 on top Azure OpenAI. Di deployment na wetin `azd up` go do automatically - no go touch di model name for code.

## Understanding Di Core Wahala

Language models no get state. Every API call na different one. If you talk "My name is John" then you ask "Wetin be my name?", di model no go sabi say you just introduce yourself. E dey treat every request as if na di first time una dey yarn.

Dis one good for simple Q&A but no work for real applications. Customer service bots need remember wetin you talk before. Personal assistants need context. Any multi-turn conversation need memory.

<img src="../../../translated_images/pcm/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Di difference between stateless (independent calls) and stateful (context-aware) conversations*

## Understanding Tokens

Before you enter conversation matter, e good make you sabi tokens - na di basic units of text wey language models dey process:

<img src="../../../translated_images/pcm/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Example of how text dey break into tokens - "I love AI!" break into 4 separate processing units*

Tokens na how AI models dey measure and process text. Words, punctuation, even spaces fit be tokens. Your model get limit how many tokens e fit process once (400,000 for GPT-5.2, weh fit carry up to 272,000 input tokens and 128,000 output tokens). To sabi tokens go help you manage conversation length and cost.

## How Memory Dey Work

Chat memory dey solve di stateless wahala by maintaining conversation history. Before you send your request to model, di framework go put the relevant previous messages front. When you ask "Wetin be my name?", di system actually dey send the entire conversation history, so di model fit see say you don talk "My name is John" before.

LangChain4j provide memory implementations wey dey handle dis one automatically. You go choose how many messages you wan keep and di framework go manage di context window.

<img src="../../../translated_images/pcm/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory dey maintain sliding window of recent messages, e dey drop old ones automatically*

## How Dis One Dey Use LangChain4j

Dis module dey extend quick start by to join Spring Boot and add conversation memory. See how pieces dey fit:

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

**Chat Model** - Configure Azure OpenAI as Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder dey read credentials from environment variables wey `azd up` set. Setting `baseUrl` to your Azure endpoint go make OpenAI client work with Azure OpenAI.

**Conversation Memory** - Track chat history wit MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Create memory wit `withMaxMessages(10)` to keep last 10 messages. Add user and AI messages wit typed wrappers: `UserMessage.from(text)` and `AiMessage.from(text)`. Retrieve history wit `memory.messages()` and send am to model. Di service dey store separate memory instances per conversation ID, so multiple users fit chat at di same time.

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) and ask:
> - "How MessageWindowChatMemory dey decide which messages to drop when di window full?"
> - "Fit I implement custom memory storage wit database instead of in-memory?"
> - "How I go add summarization to compress old conversation history?"

Di stateless chat endpoint no use memory at all - na just `chatModel.chat(prompt)` like quick start. Di stateful endpoint add messages to memory, retrieve history, and include dat context with every request. Same model config, but different patterns.

## Deploy Azure OpenAI Infrastructure

**Bash:**
```bash
cd 01-introduction
azd up  # Choose subscription and place (eastus2 na the best)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Choose subscription and place (eastus2 dey recommended)
```

> **Note:** If you see timeout error (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), just run `azd up` again. Azure resources fit still dey provision for background, and retrying go allow deployment to finish once resources reach terminal state.

Dis one go:
1. Deploy Azure OpenAI resource wit GPT-5.2 and text-embedding-3-small models
2. Automatically generate `.env` file for project root wit credentials
3. Setup all di environment variables wey dem need

**Get deployment wahala?** See [Infrastructure README](infra/README.md) for detailed troubleshooting including subdomain name conflicts, manual Azure Portal deployment steps, and model configuration advice.

**Make sure deployment succeed:**

**Bash:**
```bash
cat ../.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, and oda tins.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, and other tins.
```

> **Note:** `azd up` command go generate `.env` file automatically. If na later you wan update am, you fit either edit `.env` file manually or regenerate by running:
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

## Run Di Application for Local

**Check deployment:**

Make sure `.env` file dey for root directory wit Azure credentials:

**Bash:**
```bash
cat ../.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start di applications:**

**Option 1: Use Spring Boot Dashboard (Recommended for VS Code users)**

Di dev container get Spring Boot Dashboard extension, wey dey provide you visual interface to manage all Spring Boot applications. You fit find am for Activity Bar for left side of VS Code (look for Spring Boot icon).

For Spring Boot Dashboard, you fit:
- See all available Spring Boot applications for workspace
- Start/stop applications wit single click
- View application logs for real-time
- Monitor application status

Just click the play button next to "introduction" to start dis module, or start all modules at once.

<img src="../../../translated_images/pcm/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Option 2: Use shell scripts**

Start all web applications (modules 01-04):

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

Or start just dis module:

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

Both scripts go auto load environment variables from root `.env` file and go build di JARs if dem no dey.

> **Note:** If you prefer to build all modules manually before you start:
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
cd .. && ./stop-all.sh  # All modules
```

**PowerShell:**
```powershell
.\stop.ps1  # Dis module only
# Or
cd ..; .\stop-all.ps1  # All modules
```

## How to Use Di Application

Di application dey provide web interface wit two chat implementations side-by-side.

<img src="../../../translated_images/pcm/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard wey show both Simple Chat (stateless) and Conversational Chat (stateful) options*

### Stateless Chat (Left Panel)

Try this one first. Ask "My name is John" then quickly ask "Wetin be my name?" Di model no go remember because every message na stand-alone. Dis one dey show di main wahala for basic language model integration - no conversation context.

<img src="../../../translated_images/pcm/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI no remember your name from dia previous message*

### Stateful Chat (Right Panel)

Now try di same sequence here. Ask "My name is John" then "Wetin be my name?" Dis time e go remember. Di difference na MessageWindowChatMemory - e dey keep conversation history and add am to every request. Na so production conversational AI dey work.

<img src="../../../translated_images/pcm/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI remember your name from earlier for conversation*

Both panels dey use the same GPT-5.2 model. Di only difference na memory. Dis one make am clear why memory dey important for your application and why e necessary for real use cases.

## Next Steps

**Next Module:** [02-prompt-engineering - Prompt Engineering wit GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigation:** [← Previous: Module 00 - Quick Start](../00-quick-start/README.md) | [Back to Main](../README.md) | [Next: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis document na translashun we AI translashun service [Co-op Translator](https://github.com/Azure/co-op-translator) help do. Even though we try make am correct, abeg make you sabi say automated translashun fit get some mistake or wrong waka. Di original document wey e be for im own language na di kasala-free source. If na important tin be dat, e betta make professional pesin we sabi do translashun handle am. We no go dey responsible if any wahala or wrong understanding show because of dis translashun.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->