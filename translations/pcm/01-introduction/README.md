# Module 01: Getting Started wit LangChain4j

## Table of Contents

- [Wetn You Go Learn](../../../01-introduction)
- [Wetin You Need Beforehand](../../../01-introduction)
- [Understanding Di Main Wahala](../../../01-introduction)
- [Understanding Tokens](../../../01-introduction)
- [How Memory Dey Work](../../../01-introduction)
- [How Dis One Take Use LangChain4j](../../../01-introduction)
- [Deploy Azure OpenAI Infrastructure](../../../01-introduction)
- [Run Di Application for Your Computer](../../../01-introduction)
- [How to Use Di Application](../../../01-introduction)
  - [Stateless Chat (Left Panel)](../../../01-introduction)
  - [Stateful Chat (Right Panel)](../../../01-introduction)
- [Next Steps](../../../01-introduction)

## Wetn You Go Learn

If you don complete di quick start, you don see how to send prompts and get responses. Na dat be di foundation, but real applications need more. Dis module go teach you how to build conversational AI wey go remember context and maintain state - di difference between one-time demo and beta wey e ready for production.

We go use Azure OpenAI GPT-5.2 all through dis guide because e get better reasoning powers wey dey make how different patterns dey behave clear pass. When you add memory, you go clear see di difference. Dis one dey make am easy to understand wetin each part dey bring to your application.

You go build one application wey go show both patterns:

**Stateless Chat** - Each request na separate one. Di model no get memory of the message before. Na di pattern wey you use for di quick start be dis.

**Stateful Conversation** - Each request carry di conversation history. Di model go maintain context for many turns. Na wetin production applications need.

## Wetin You Need Beforehand

- Azure subscription with Azure OpenAI access
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Note:** Java, Maven, Azure CLI and Azure Developer CLI (azd) dey pre-installed for di devcontainer wey dem provide.

> **Note:** Dis module dey use GPT-5.2 on top Azure OpenAI. Di deployment dey setup automatically through `azd up` - no change di model name for di code.

## Understanding Di Main Wahala

Language models no get memory. Each API call na separate one. If you send "My name is John" then ask "Wetin be my name?", di model no sabi say you don talk before. E go treat every request like na di first time you dey talk.

E fit work for simple Q&A but e no good for real applications. Customer service bots suppose remember wetin you talk before. Personal assistants need context. Any multi-turn conversation need memory.

<img src="../../../translated_images/pcm/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Di difference between stateless (independent calls) and stateful (context-aware) conversations*

## Understanding Tokens

Before you start any conversations, e good make you understand tokens - na di small small parts of text wey language models dey process:

<img src="../../../translated_images/pcm/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Example of how text dey split into tokens - "I love AI!" dey become 4 separate units for processing*

Tokens na how AI models dey measure and process text. Words, punctuation, even spaces fit be tokens. Your model get limit of how many tokens e fit process at once (400,000 for GPT-5.2, with up to 272,000 input tokens and 128,000 output tokens). If you understand tokens, e go help you control how long conversations go be and how cost be.

## How Memory Dey Work

Chat memory fix di stateless problem by maintaining conversation history. Before you send request go di model, di framework go add di important previous messages front. When you ask "Wetin be my name?", di system go send di whole conversation history, make di model fit see say you talk "My name is John" before.

LangChain4j provide memory ways wey dey do dis automatically. You fit choose how many messages to keep and di framework go manage di context window.

<img src="../../../translated_images/pcm/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory dey keep recent messages dey slide, e dey automatically drop old ones*

## How Dis One Take Use LangChain4j

Dis module extend di quick start by adding Spring Boot and adding conversation memory. Na so di parts take join:

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

**Chat Model** - Setup Azure OpenAI as Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Di builder go read credentials from environment variables wey `azd up` set. If you set `baseUrl` to your Azure endpoint, di OpenAI client go work with Azure OpenAI.

**Conversation Memory** - Track chat history with MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Make memory with `withMaxMessages(10)` to keep last 10 messages. Add user and AI messages with typed wrappers: `UserMessage.from(text)` and `AiMessage.from(text)`. Fit collect history with `memory.messages()` then send am to di model. Di service dey keep separate memory for each conversation ID, so multiple users fit chat at di same time.

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ask:
> - "How MessageWindowChatMemory take decide which messages to drop when e window full?"
> - "I fit implement my own custom memory storage using database instead of in-memory?"
> - "How I go fit add summarization to compress old conversation history?"

Di stateless chat endpoint no use memory at all - na just `chatModel.chat(prompt)` like quick start. Di stateful endpoint add messages to memory, collect history, and put am together with each request. Same model config, different patterns.

## Deploy Azure OpenAI Infrastructure

**Bash:**
```bash
cd 01-introduction
azd up  # Chos subscription and location (eastus2 na wetin dey recommended)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Choose subscription and location (eastus2 dey recommended)
```

> **Note:** If you get timeout error (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), just run `azd up` again. Sometimes Azure resources still dey provision, retrying help make deployment finish when e ready.

Dis one go:
1. Deploy Azure OpenAI resource with GPT-5.2 and text-embedding-3-small models 
2. Automatically create `.env` file for project root with credentials
3. Setup all necessary environment variables

**Get any deployment wahala?** See di [Infrastructure README](infra/README.md) for detailed troubleshooting like subdomain conflicts, manual Azure Portal deployment steps, and model config tips.

**Check if deployment don work:**

**Bash:**
```bash
cat ../.env  # E for show AZURE_OPENAI_ENDPOINT, API_KEY, en.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

> **Note:** `azd up` command dey generate `.env` file automatically. If later you want change am, you fit either edit `.env` file manually or regenerate am by running:
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

## Run Di Application for Your Computer

**Check deployment:**

Make sure `.env` file dey root directory with Azure credentials:

**Bash:**
```bash
cat ../.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start di applications:**

**Option 1: Use Spring Boot Dashboard (Best for VS Code users)**

Di dev container get Spring Boot Dashboard extension, wey dey give visual interface to manage all Spring Boot applications. You fit find am for Activity Bar for left side of VS Code (look for di Spring Boot icon).

From Spring Boot Dashboard, you fit:
- See all Spring Boot apps wey dey workspace
- Start/stop apps with one click
- View app logs live
- Monitor app status

Just click di play button beside "introduction" to start dis module, or start all modules at once.

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

Both scripts go automatically load environment variables from root `.env` and go build JARs if dem no dey.

> **Note:** If you want build all modules yourself before you start:
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
cd ..; .\stop-all.ps1  # All modules
```

## How to Use Di Application

Di application get web interface with two chat ways side by side.

<img src="../../../translated_images/pcm/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard wey show both Simple Chat (stateless) and Conversational Chat (stateful) options*

### Stateless Chat (Left Panel)

Try am first. Ask "My name is John" then quickly ask "Wetin be my name?" Di model no go remember because each message na separate one. Dis one show di main wahala wey basic language model get - no conversation context.

<img src="../../../translated_images/pcm/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI no go remember your name from previous message*

### Stateful Chat (Right Panel)

Now try di same thing here. Ask "My name is John" then "Wetin be my name?" This time e go remember. Di difference na MessageWindowChatMemory - e keep conversation history and add am with each request. Na so production conversational AI dey work.

<img src="../../../translated_images/pcm/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI remember your name from earlier conversation*

Both panels dey use same GPT-5.2 model. Di only difference na memory. E clear wetin memory dey bring to your application and why e important for real use.

## Next Steps

**Next Module:** [02-prompt-engineering - Prompt Engineering wit GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigation:** [← Previous: Module 00 - Quick Start](../00-quick-start/README.md) | [Back to Main](../README.md) | [Next: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis document don translate wit AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). Even though we dey try make am correct, abeg sabi say automated translation fit get some mistakes or wrong tins. Di original document wey dem write for im original language na di correct one. For important information, e better make human professional translate am. We no go responsible for any misunderstanding or wrong meaning wey fit happen because of dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->