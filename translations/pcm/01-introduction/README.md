# Module 01: Getting Started wit LangChain4j

## Table of Contents

- [Video Walkthrough](../../../01-introduction)
- [Wetn You Go Learn](../../../01-introduction)
- [Wetn You Must Get Before](../../../01-introduction)
- [Understanding Di Core Problem](../../../01-introduction)
- [Understanding Tokens](../../../01-introduction)
- [How Memory Dey Work](../../../01-introduction)
- [How Dis One Use LangChain4j](../../../01-introduction)
- [Deploy Azure OpenAI Infrastructure](../../../01-introduction)
- [Run Di Application Locally](../../../01-introduction)
- [Using Di Application](../../../01-introduction)
  - [Stateless Chat (Left Panel)](../../../01-introduction)
  - [Stateful Chat (Right Panel)](../../../01-introduction)
- [Wetn Next](../../../01-introduction)

## Video Walkthrough

Watch dis live session wey explain how to start wit dis module:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Wetn You Go Learn

If you don finish di quick start, you don see how to send prompts and get responses. Na dat be di foundation, but real applications need more. Dis module go teach you how to build conversational AI wey dey remember context and maintain state - na di difference between one-off demo and production-ready application.

We go use Azure OpenAI's GPT-5.2 through dis guide because e get advanced reasoning skills wey make behavior of different patterns clear. When you add memory, you go see di difference well well. Dis one make am easy to understand wetin each part dey bring into your application.

You go build one application wey go show both patterns:

**Stateless Chat** - Each request independent. Di model no get memory of previous messages. Na di pattern wey you use for quick start.

**Stateful Conversation** - Each request get conversation history. Di model dey maintain context across multiple turns. Na wetin production applications need.

## Wetn You Must Get Before

- Azure subscription with Azure OpenAI access
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Note:** Java, Maven, Azure CLI and Azure Developer CLI (azd) don already dey inside di devcontainer wey dem provide.

> **Note:** Dis module dey use GPT-5.2 on Azure OpenAI. Di deployment dey automatically set with `azd up` - no change di model name for di code.

## Understanding Di Core Problem

Language models no get memory. Each API call dey independent. If you talk "My name is John" then ask "Wetn be my name?", di model no sabi say you just talk yourself. E dey treat every request like na di first time una dey talk.

Dis one okay for simple Q&A but e no work for real applications. Customer service bots need to remember wetin you talk before. Personal assistants need context. Any multi-turn conversation need memory.

<img src="../../../translated_images/pcm/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Di difference between stateless (independent calls) and stateful (context-aware) conversations*

## Understanding Tokens

Before you start talk, e good make you understand tokens - na di basic units of text wey language models dey process:

<img src="../../../translated_images/pcm/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Example of how text dey break into tokens - "I love AI!" turn to 4 separate processing units*

Tokens na how AI models dey measure and process text. Words, punctuation, and even spaces fit be tokens. Your model get limit on how many tokens e fit process at once (400,000 for GPT-5.2, wit up to 272,000 input tokens and 128,000 output tokens). Understanding tokens go help you manage conversation length and costs.

## How Memory Dey Work

Chat memory dey solve di stateless problem by maintaining conversation history. Before you send your request to di model, di framework dey put relevant previous messages front front. When you ask "Wetn be my name?", di system dey send all di conversation history, so di model fit see say you talk "My name is John" before.

LangChain4j get memory implementations wey dey handle dis automatically. You fit choose how many messages you wan keep and di framework dey manage di context window.

<img src="../../../translated_images/pcm/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory dey maintain one sliding window of recent messages, e dey automatically drop old ones*

## How Dis One Use LangChain4j

Dis module extend di quick start by adding Spring Boot and conversation memory. See how di pieces join:

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

Di builder dey read credentials from environment variables wey `azd up` set. Setting `baseUrl` to your Azure endpoint make OpenAI client work well with Azure OpenAI.

**Conversation Memory** - Track chat history wit MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Create memory wit `withMaxMessages(10)` to keep last 10 messages. Add user and AI messages wit typed wrappers: `UserMessage.from(text)` and `AiMessage.from(text)`. Retrieve history wit `memory.messages()` then send am to di model. Di service dey keep separate memory instances for each conversation ID, to make multiple users fit chat at di same time.

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) and ask:
> - "How MessageWindowChatMemory dey decide which messages to drop when di window full?"
> - "Fit I implement custom memory storage using database instead in-memory?"
> - "How I go take add summarization to compress old conversation history?"

Di stateless chat endpoint no even use memory - just `chatModel.chat(prompt)` like quick start. Di stateful endpoint go add messages to memory, retrieve history, and include dat context wit each request. Same model config, different patterns.

## Deploy Azure OpenAI Infrastructure

**Bash:**
```bash
cd 01-introduction
azd up  # Choose subscription and place (eastus2 na di one wey dem recommend)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Choo subscribe and place (eastus2 na beta)
```

> **Note:** If you see timeout error (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), just run `azd up` again. Azure resources fit still dey provision for background, retrying go allow di deployment finish when resources reach terminal state.

Dis one go:
1. Deploy Azure OpenAI resource wit GPT-5.2 and text-embedding-3-small models
2. Automatically generate `.env` file for project root wit credentials
3. Set up all environment variables wey you need

**If deployment get wahala?** Check [Infrastructure README](infra/README.md) for better troubleshooting, like subdomain name conflicts, manual Azure Portal deployment steps, and model configuration tips.

**Make sure deployment succeed:**

**Bash:**
```bash
cat ../.env  # Suppose show AZURE_OPENAI_ENDPOINT, API_KEY, and di oda tins.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Suppose show AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

> **Note:** `azd up` command dey generate `.env` file automatically. If you want update am later, you fit edit `.env` file manually or regenerate am by running:
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

## Run Di Application Locally

**Make sure deployment dey ok:**

Check say `.env` file dey root directory wit Azure credentials:

**Bash:**
```bash
cat ../.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start di applications:**

**Option 1: Use Spring Boot Dashboard (Good for VS Code users)**

Di dev container get Spring Boot Dashboard extension wey provide visual interface to manage all Spring Boot applications. You fit find am for Activity Bar for left side of VS Code (look for Spring Boot icon).

For Spring Boot Dashboard, you fit:
- See all Spring Boot applications wey dey workspace
- Start/stop applications wit one click
- View application logs immediately
- Monitor application status

Just click play button beside "introduction" to start dis module, or start all modules together.

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
cd ..  # From di root folder
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

Both scripts dey load environment variables from root `.env` file automatically and go build JARs if dem no exist.

> **Note:** If you like build all modules manually before you start:
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
cd ..; .\stop-all.ps1  # All modules dem
```

## Using Di Application

Di application get web interface wit two chat versions side by side.

<img src="../../../translated_images/pcm/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard wey show Simple Chat (stateless) and Conversational Chat (stateful) options*

### Stateless Chat (Left Panel)

Try dis one first. Ask "My name is John" then immediately ask "Wetn be my name?" Di model no go remember because each message stand alone. Dis one show di core problem with basic language model integration - no conversation context.

<img src="../../../translated_images/pcm/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI no dey remember your name from previous message*

### Stateful Chat (Right Panel)

Now try di same thing here. Ask "My name is John" then "Wetn be my name?" This time e remember. Di difference na MessageWindowChatMemory - e dey keep conversation history and include am with each request. Na so production conversational AI dey work.

<img src="../../../translated_images/pcm/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI dey remember your name from earlier for di conversation*

Both panels dey use same GPT-5.2 model. Di only difference na memory. Dis one make am clear wetin memory dey bring to your application and why e important for real use.

## Wetn Next

**Next Module:** [02-prompt-engineering - Prompt Engineering wit GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigation:** [← Previous: Module 00 - Quick Start](../00-quick-start/README.md) | [Back to Main](../README.md) | [Next: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Warning**:  
Dis document na wetin AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator) translate. Even though we dey try make am correct, abeg make you know say machine fit get some mistakes or wrong parts. The original document wey dem write for im own language na the correct one. If na serious matter, better make person wey sabi human translator do am. We no go take responsibility if anybody miss understand or misinterpret anything from this translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->