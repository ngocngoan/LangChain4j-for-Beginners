# Module 04: AI Agents wit Tools

## Table of Contents

- [Wetin You Go Learn](../../../04-tools)
- [Prerequisites](../../../04-tools)
- [Understanding AI Agents wit Tools](../../../04-tools)
- [How Tool Calling Dey Work](../../../04-tools)
  - [Tool Definitions](../../../04-tools)
  - [Decision Making](../../../04-tools)
  - [Execution](../../../04-tools)
  - [Response Generation](../../../04-tools)
  - [Architecture: Spring Boot Auto-Wiring](../../../04-tools)
- [Tool Chaining](../../../04-tools)
- [Run the Application](../../../04-tools)
- [Using the Application](../../../04-tools)
  - [Try Simple Tool Usage](../../../04-tools)
  - [Test Tool Chaining](../../../04-tools)
  - [See Conversation Flow](../../../04-tools)
  - [Experiment wit Different Requests](../../../04-tools)
- [Key Concepts](../../../04-tools)
  - [ReAct Pattern (Reasoning and Acting)](../../../04-tools)
  - [Tool Descriptions Matter](../../../04-tools)
  - [Session Management](../../../04-tools)
  - [Error Handling](../../../04-tools)
- [Available Tools](../../../04-tools)
- [When to Use Tool-Based Agents](../../../04-tools)
- [Tools vs RAG](../../../04-tools)
- [Next Steps](../../../04-tools)

## Wetin You Go Learn

So far, you don learn how to dey yan wit AI, how to structure prompts well well, and how to make responses base on your documents. But e still get one kain main limitation: language models fit only generate text. Dem no fit check weather, do calculation, query databases, or join with external systems.

Tools come change dis one. By giving the model access to functions wey e fit call, you go transform am from text generator to agent wey fit take actions. The model go decide when e need tool, which tool to use, and wetin parameters to pass. Your code go run the function and return the result. The model go join that result inside im response.

## Prerequisites

- Finish Module 01 (Azure OpenAI resources dey deployed)
- `.env` file dey root folder wit Azure credentials (wey `azd up` for Module 01 create)

> **Note:** If you never finish Module 01, abeg follow deployment instruction wey dey there first.

## Understanding AI Agents wit Tools

> **📝 Note:** The word "agents" for this module na AI assistants wey get tool-calling power. E different from the **Agentic AI** patterns (wey be autonomous agents wit planning, memory, and multi-step reasoning) wey we go talk for [Module 05: MCP](../05-mcp/README.md).

Without tools, language model fit only generate text from im training data. If you ask am for the current weather, e go just guess. But if you give am tools, e fit call weather API, do calculation, or query database — then mix the real results into im response.

<img src="../../../translated_images/pcm/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*Without tools the model fit only guess — but wit tools e fit call APIs, do calculation, and return real-time data.*

AI agent wey get tools dey follow **Reasoning and Acting (ReAct)** pattern. The model no just respond — e dey think about wetin e need, e act by calling tool, e see the result, then e decide if na to act again or to give the answer:

1. **Reason** — The agent go analyze wetin user ask and decide wetin info e need
2. **Act** — The agent go pick correct tool, generate correct parameters, and call am
3. **Observe** — The agent go receive tool output and check the result
4. **Repeat or Respond** — If e need more data, e go repeat; if no, e go give answer for normal way

<img src="../../../translated_images/pcm/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*The ReAct cycle — the agent dey reason wetin to do, e dey act by calling tool, e dey observe the result, and e dey repeat till e fit give final answer.*

This one dey happen automatic. You go define the tools and their description. The model go handle the decision making for when and how to use them.

## How Tool Calling Dey Work

### Tool Definitions

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

You go define functions wit clear description and parameter specifications. The model dey see these description inside system prompt and e go sabi wetin each tool dey do.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Your weather lookup logic
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Assistant dey automatically connect by Spring Boot wit:
// - ChatModel bean
// - All @Tool methods from @Component classes
// - ChatMemoryProvider for session management
```
  
Diagram wey dey below go breakdown every annotation and show how each one dey help AI know when to call tool and which arguments to pass:

<img src="../../../translated_images/pcm/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*Anatomy of tool definition — @Tool dey tell AI as e suppose take am, @P dey describe each parameter, and @AiService dey wire everything join for startup.*

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) and ask:  
> - "How I fit join real weather API like OpenWeatherMap replace mock data?"  
> - "Wetin make good tool description wey help AI use am correct?"  
> - "How I go handle API errors and rate limits for my tool implementation?"

### Decision Making

When user ask "Wetin be the weather for Seattle?", the model no go just anyhow pick tool. E go compare wetin user mean wit every tool description wey e get access, e go score every one based on relevance, then e go choose the best one. Then model go generate structured function call wit correct parameters — for this case, e go set `location` to `"Seattle"`.

If no tool fit user request, model go answer based on im own knowledge. If many tools fit, e go pick the most specific one.

<img src="../../../translated_images/pcm/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*The model go check every tool wey dey compare to wetin user mean and pick the best one — na why clear and specific tool descriptions dey important.*

### Execution

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot go auto-wire the declarative `@AiService` interface wit all registered tools, LangChain4j go then run tool calls automatic. For the background, full tool call go pass six stages — comot from user natural language question go come back to natural language answer:

<img src="../../../translated_images/pcm/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*The whole flow — user ask question, model pick tool, LangChain4j run am, model go mix the result for natural response.*

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) and ask:  
> - "How ReAct pattern dey work and why e good for AI agents?"  
> - "How agent go decide which tool to use and the order?"  
> - "If tool run work fail, wetin I suppose do to handle errors well?"

### Response Generation

Model go collect weather data and format am as normal language response for user.

### Architecture: Spring Boot Auto-Wiring

This module dey use LangChain4j Spring Boot integration wit declarative `@AiService` interfaces. For startup, Spring Boot go find all `@Component` wey get `@Tool` methods, your `ChatModel` bean, and the `ChatMemoryProvider` — then wire dem all inside one `Assistant` interface wit zero boilerplate.

<img src="../../../translated_images/pcm/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*The @AiService interface dey join ChatModel, tool components, and memory provider — Spring Boot dey handle all wiring automatic.*

Some beta advantage for this way:

- **Spring Boot auto-wiring** — ChatModel and tools self inject
- **@MemoryId pattern** — Automatic session-based memory management
- **Single instance** — Assistant create once and dey reuse for better performance
- **Type-safe execution** — Java methods run direct wit type conversion
- **Multi-turn orchestration** — Automatically handle tool chaining
- **Zero boilerplate** — No manual `AiServices.builder()` calls or memory HashMap

Other approaches (wey na manual `AiServices.builder()` be) need more code and no get Spring Boot integration advantage.

## Tool Chaining

**Tool Chaining** — Real power for tool-based agents go show anytime one question need multiple tools. If user ask "How be the weather for Seattle for Fahrenheit?" agent go automatically chain two tools: e first call `getCurrentWeather` to get temperature for Celsius, then e pass that one go `celsiusToFahrenheit` to convert — all for one conversation turn.

<img src="../../../translated_images/pcm/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*Tool chaining for work — agent first call getCurrentWeather, then chook the Celsius result inside celsiusToFahrenheit, then deliver combined answer.*

Dis na how e go look inside the running application — agent dey chain two tool calls inside one conversation turn:

<a href="images/tool-chaining.png"><img src="../../../translated_images/pcm/tool-chaining.3b25af01967d6f7b.webp" alt="Tool Chaining" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Real application output — agent automatic chain getCurrentWeather → celsiusToFahrenheit for one turn.*

**Graceful Failures** — If you ask weather for one city wey no dey mock data, tool go return error message, AI go explain say e no fit help, no go crash. Tools fail safe.

<img src="../../../translated_images/pcm/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*If tool fail, agent go catch the error and give helpful explanation instead of crash.*

This one happen for one conversation turn. Agent self control many tool calls.

## Run the Application

**Make sure deployment:**

Make sure `.env` file dey root folder wit Azure credentials (wey Module 01 create):  
```bash
cat ../.env  # Suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Start the application:**  

> **Note:** If you don start all applications wit `./start-all.sh` from Module 01, this module don dey run for port 8084. You fit skip start commands and waka go http://localhost:8084 directly.

**Option 1: Use Spring Boot Dashboard (Best for VS Code users)**

Dev container get Spring Boot Dashboard extension wey give you visual interface to manage all Spring Boot applications. You fit find am for Activity Bar for left side of VS Code (look for Spring Boot icon).

For Spring Boot Dashboard, you fit:  
- See all Spring Boot applications wey dey your workspace  
- Start/stop applications wit one click  
- View application logs live  
- Monitor application status  

Just click play button beside "tools" to start this module, or start all modules at once.

<img src="../../../translated_images/pcm/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

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
  
Or start just this module:

**Bash:**  
```bash
cd 04-tools
./start.sh
```
  
**PowerShell:**  
```powershell
cd 04-tools
.\start.ps1
```
  
Both scripts go automatic load environment variables from root `.env` file and go build JARs if dem no dey.

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
  
Open http://localhost:8084 for your browser.

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
  
## Using the Application

This application give web interface wey you fit interact wit AI agent wey get access to weather and temperature conversion tools.

<a href="images/tools-homepage.png"><img src="../../../translated_images/pcm/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*The AI Agent Tools interface - quick examples and chat interface to interact wit tools*

### Try Simple Tool Usage
Start with one straightforward request: "Convert 100 degrees Fahrenheit to Celsius". Di agent sabi say e need di temperature conversion tool, e call am wit di correct parameters, den e return di result. You go notice how e feel natural - you no talk say which tool to use or how to call am.

### Test Tool Chaining

Now try something wey get more levels: "Wetin be di weather for Seattle and convert am to Fahrenheit?" Watch how di agent dey work am step by step. E first collect di weather (wey return Celsius), e sabi say e need to convert am to Fahrenheit, e call di conversion tool, den e put both results join for one reply.

### See Conversation Flow

Di chat interface dey keep conversation history, make you fit talk many turns. You fit see all di questions and answers wey happen before, e easy to follow di talk and understand how di agent dey add context across many turns.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/pcm/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Multi-turn conversation wey show simple conversions, weather lookups, and tool chaining*

### Experiment with Different Requests

Try different combinations:
- Weather lookups: "Wetin be di weather for Tokyo?"
- Temperature conversions: "Wetin be 25°C for Kelvin?"
- Combined queries: "Check di weather for Paris and tell me if e pass 20°C"

You go notice how di agent dey understand natural language and dey match am correct to tool calls.

## Key Concepts

### ReAct Pattern (Reasoning and Acting)

Di agent dey alternate between reasoning (to decide wetin to do) and acting (to use tools). Dis pattern dey allow autonomous problem-solving instead of just responding to instruction.

### Tool Descriptions Matter

Better tool descriptions fit help di model understand well when and how to call each tool. Clear and spesific descriptions dey important.

### Session Management

Di `@MemoryId` annotation dey enable automatic session-based memory management. Every session ID get im own `ChatMemory` instance wey `ChatMemoryProvider` bean dey manage, so plenty users fit interact with di agent at di same time without mixing their conversations.

<img src="../../../translated_images/pcm/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Each session ID dey linked to one separate conversation history — users nor go see other people messages.*

### Error Handling

Tools fit fail — APIs fit timeout, parameters fit no correct, external services fit crash. Production agents need error handling so di model fit explain problem or try another solution instead of crashing everywhere. When tool throw exception, LangChain4j go catch am and give di error message back to di model, wey fit explain wetin happen in normal language.

## Available Tools

Di diagram below show di wide range of tools you fit build. Dis module dey show weather and temperature tools, but di same `@Tool` pattern fit work for any Java method — from database queries to payment processing.

<img src="../../../translated_images/pcm/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Any Java method wey get @Tool annotation go dey available to AI — di pattern fit extend to databases, APIs, email, file operations, and more.*

## When to Use Tool-Based Agents

<img src="../../../translated_images/pcm/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*Quick decision guide — tools good for real-time data, calculations, and actions; general knowledge and creative tasks no really need dem.*

**Use tools when:**
- Answering require real-time data (weather, stock prices, inventory)
- You need to do calculations wey pass simple math
- Access databases or APIs
- Take actions (send emails, create tickets, update records)
- Combine multiple data sources

**No use tools when:**
- Questions fit answer with general knowledge
- Response na pure conversation
- Tool latency go make di experience too slow

## Tools vs RAG

Modules 03 and 04 dem both extend wetin AI fit do, but for different ways. RAG dey give di model access to **knowledge** by fetching documents. Tools dey give di model power to take **actions** by calling functions.

<img src="../../../translated_images/pcm/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG dey fetch info from static documents — Tools dey execute actions and get dynamic, real-time data. Many production systems dey combine both.*

For practice, many production systems dey combine both: RAG for ground answers for your documents, and Tools for get live data or do operations.

## Next Steps

**Next Module:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigation:** [← Previous: Module 03 - RAG](../03-rag/README.md) | [Back to Main](../README.md) | [Next: Module 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:  
Dis document don translate wit AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). Even though we dey try make am correct, abeg sabi say automated translations fit get some mistakes or errors. Di original document for dia own language na di correct one. If na important info, good professional human translation better. We no go responsible for any misunderstanding or wrong explanation wey fit show because of dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->