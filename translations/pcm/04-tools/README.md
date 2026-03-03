# Module 04: AI Agents wit Tools

## Table of Contents

- [Wetyn You Go Learn](../../../04-tools)
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

## Wetyn You Go Learn

So far, you don learn how to gist wit AI, structure prompts well well, and ground responses for your documents. But still one gbege dey: language models fit generate text only. Dem no fit check weather, do calculation, query database, or interact wit outside systems.

Tools go change this one. If you give model access to functions wey e fit call, you go turn am from text generator to agent wey fit take action. The model dey decide when e need tool, which tool to use, and wetin parameters to pass. Your code go run the function and return result. The model go join that result inside e response.

## Prerequisites

- Don finish [Module 01 - Introduction](../01-introduction/README.md) (Azure OpenAI resources don deploy)
- Don finish pipo modules wey dem recommend (this module dey talk about [RAG concepts from Module 03](../03-rag/README.md) for Tools vs RAG comparison)
- `.env` file for root directory wit Azure credentials (wey `azd up` create for Module 01)

> **Note:** If you never finish Module 01, make you follow deployment instructions wey dey there first.

## Understanding AI Agents wit Tools

> **📝 Note:** The word "agents" for this module mean AI assistants wey dey advanced wit tool-calling capabilities. E different from **Agentic AI** patterns (autonomous agents wey get planning, memory, and multi-step reasoning) wey we go yarn for [Module 05: MCP](../05-mcp/README.md).

Without tools, language model fit only produce text from e training data. Ask am about current weather, e go just guess. Give am tools, e fit call weather API, do calculation, or query database — then mix those real results inside e response.

<img src="../../../translated_images/pcm/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*Without tools the model fit only guess — wit tools e fit call APIs, run calculation, and bring real-time data.*

AI agent wit tools dey follow **Reasoning and Acting (ReAct)** pattern. The model no dey just respond — e dey reason wetin e need, e dey act by calling tool, e dey observe result, then e go decide to act again or give final answer:

1. **Reason** — Agent go analyze question and find wetin info e need
2. **Act** — Agent go choose correct tool, generate right parameters, then call am
3. **Observe** — Agent go receive tool output and check result
4. **Repeat or Respond** — If still need more data, agent go do am again, if no, e go make natural language answer

<img src="../../../translated_images/pcm/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*The ReAct cycle — agent dey reason wetin to do, dey act by calling tool, dey observe result, dey loop till e fit give final answer.*

This one dey happen automatically. You go define tools and their descriptions. The model go manage decision-making about when and how to use am.

## How Tool Calling Dey Work

### Tool Definitions

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

You go specify functions wit clear descriptions and parameter details. The model dey see these descriptions inside system prompt and e go understand wetin each tool de do.

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

Diagram below go break down every annotation and show how every part dey help AI understand when to call tool and wetin argument to pass:

<img src="../../../translated_images/pcm/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*Anatomy of tool definition — @Tool dey tell AI when to use am, @P dey describe every parameter, and @AiService dey wire everything start-up time.*

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) and ask:
> - "How I fit integrate real weather API like OpenWeatherMap instead of mock data?"
> - "Wetyn dey make good tool description wey go help AI use am correct?"
> - "How I go handle API errors and rate limits for tool implementation?"

### Decision Making

When person ask "Wetin be weather for Seattle?", model no go just randomly choose tool. E go compare wetin user want with every tool description wey e get. E go score each one for relevance, then pick best match. E go still generate structured function call wit correct parameters — like for this case, e go set `location` to `"Seattle"`.

If no tool match wetin user request, model go answer based on wetin e sabi. If multiple tools match, e go pick the most specific one.

<img src="../../../translated_images/pcm/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*Model dey evaluate every tool against wetin user want and pick best match — dat na why e important to write clear and specific tool descriptions.*

### Execution

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot go auto-wire `@AiService` interface wit all registered tools, and LangChain4j go run tool calls automatically. For backstage, full tool call na six steps — from user question to natural language answer:

<img src="../../../translated_images/pcm/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*The full flow — user ask question, model select tool, LangChain4j run am, model mix result inside response.*

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) and ask:
> - "How ReAct pattern dey work and why e dey effective for AI agents?"
> - "How agent dey decide which tool to use and wetin order na?"
> - "Wetin happen if tool execution fail - how I go fit handle errors well?"

### Response Generation

The model go collect weather data and put am well for natural language response for the user.

### Architecture: Spring Boot Auto-Wiring

This module dey use LangChain4j Spring Boot integration with `@AiService` interfaces wey dey declarative. For startup Spring Boot go find every `@Component` wey get `@Tool` methods, your `ChatModel` bean, and `ChatMemoryProvider` — then e go wire dem all together as single `Assistant` interface wit zero manual work.

<img src="../../../translated_images/pcm/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*@AiService interface dey tie ChatModel, tool components, and memory provider together — Spring Boot dey handle wiring automatically.*

Main benefits of this approach:

- **Spring Boot auto-wiring** — ChatModel and tools automatically join
- **@MemoryId pattern** — Automatic session-based memory management
- **Single instance** — Assistant create once and e dey reuse for better speed
- **Type-safe execution** — Java methods dey call directly wit type conversion
- **Multi-turn orchestration** — Dey handle tool chaining automatically
- **Zero boilerplate** — No need manual `AiServices.builder()` calls or memory HashMap

Other ways (manual `AiServices.builder()`) need more code and no get Spring Boot integration benefits.

## Tool Chaining

**Tool Chaining** — Real power for tool-based agents show when one question need multiple tools. Ask "Wetin be weather for Seattle in Fahrenheit?" and agent go chain two tools: first e go call `getCurrentWeather` to get temperature for Celsius, then e go use that value give `celsiusToFahrenheit` make e convert am — all this na one chat turn.

<img src="../../../translated_images/pcm/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*Tool chaining for work — agent call getCurrentWeather first, then use Celsius result pass to celsiusToFahrenheit, then deliver combined answer.*

**Graceful Failures** — Ask for weather for city wey no dey mock data. Tool go return error message, AI go talk say e no fit help instead of crash. Tools dey fail safe. Diagram below show two approaches — wit good error handling, agent go catch the exception and respond well, without am, the whole app go crash:

<img src="../../../translated_images/pcm/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*When tool fail, agent go catch error and respond wit correct explanation instead of crash.*

This one happen for one conversation turn. Agent go manage multiple tool calls alone.

## Run the Application

**Confirm deployment:**

Make sure `.env` file dey root directory wit Azure credentials (wey Module 01 create). Run this from module folder (`04-tools/`):

**Bash:**
```bash
cat ../.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start the application:**

> **Note:** If you don start all applications wit `./start-all.sh` from root directory (like Module 01 talk), this module don dey for port 8084. You fit skip start commands below and waka go http://localhost:8084 directly.

**Option 1: Using Spring Boot Dashboard (Na Better method for VS Code users)**

Dev container get Spring Boot Dashboard extension, wey get visual interface to manage all Spring Boot apps. You fit find am for Activity Bar for left side of VS Code (look for Spring Boot icon).

From Spring Boot Dashboard, you fit:
- See all Spring Boot apps wey dey workspace
- Start/stop apps wit one click
- View app logs for real-time
- Check app status

Just click play button near "tools" to start this module, or start all modules at once.

Dis na how Spring Boot Dashboard dey look for VS Code:

<img src="../../../translated_images/pcm/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard for VS Code — fit start, stop, and monitor all modules from one place*

**Option 2: Using shell scripts**

Start all web apps (modules 01-04):

**Bash:**
```bash
cd ..  # From root directory
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # From di root directory
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

Both scripts go automatically load environment variables from root `.env` file and go build the JARs if dem no dey.

> **Note:** If you wan build all modules manually before start:
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
cd ..; .\stop-all.ps1  # All di modules
```

## Using the Application

Application get web interface wey you fit use interact wit AI agent wey get access to weather and temperature conversion tools. This be how the interface dey look — e get quick-start examples and chat panel wey you fit send requests inside:
<a href="images/tools-homepage.png"><img src="../../../translated_images/pcm/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI Agent Tools interface - quick examples and chat interface for to interact wit tools*

### Try Simple Tool Usage

Start wit simple request: "Convert 100 degrees Fahrenheit to Celsius". Di agent sabi say e need di temperature conversion tool, e call am wit di correct parameters, den e return di result. Notice how e feel natural - you no tell am which tool to use or how to call am.

### Test Tool Chaining

Now try sometin more complex: "Wetyn be di weather for Seattle and convert am to Fahrenheit?" Watch di agent dey work am step by step. E first get di weather (wey return Celsius), e sabi say e need to convert to Fahrenheit, e call di conversion tool, den e join all di results for one response.

### See Conversation Flow

Di chat interface dey keep conversation history, so you fit get multi-turn interactions. You fit see all di previous queries and responses, e easy to track di talk and understand how di agent dey build context over many exchanges.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/pcm/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Multi-turn conversation wey dey show simple conversions, weather lookups, and tool chaining*

### Experiment with Different Requests

Try different combinations:
- Weather lookups: "Wetin be di weather for Tokyo?"
- Temperature conversions: "Wetin be 25°C for Kelvin?"
- Combined queries: "Check di weather for Paris and tell me if e dey above 20°C"

Notice how di agent dey understand natural language and map am to correct tool calls.

## Key Concepts

### ReAct Pattern (Reasoning and Acting)

Di agent dey alternate between reasoning (deciding wetin to do) and acting (using tools). Dis pattern dey enable autonomous problem-solving no be just respond to instruction.

### Tool Descriptions Matter

How your tool descriptions be dey directly affect how well di agent dey use dem. Clear, specific descriptions dey help di model understand when and how to call each tool.

### Session Management

Di `@MemoryId` annotation dey enable automatic session-based memory management. Each session ID get im own `ChatMemory` instance wey di `ChatMemoryProvider` bean dey manage, so many users fit interact wit di agent at the same time without their conversations mixing. Di diagram below show how many users dey routed to different memory stores based on their session IDs:

<img src="../../../translated_images/pcm/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Each session ID dey map to isolated conversation history — users no dey see each other's messages.*

### Error Handling

Tools fit fail — APIs timeout, parameters fit no valid, external services fit go down. Production agents need error handling so model fit explain problems or try alternatives instead of make di whole app crash. When tool throw exception, LangChain4j dey catch am and give di error message back to model, wey fit explain di problem in natural language.

## Available Tools

Di diagram below show di wide ecosystem of tools wey you fit build. Dis module dey demonstrate weather and temperature tools, but di same `@Tool` pattern fit work for any Java method — from database queries to payment processing.

<img src="../../../translated_images/pcm/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Any Java method annotated wit @Tool go available to di AI — di pattern extend to databases, APIs, email, file operations, and more.*

## When to Use Tool-Based Agents

No every request need tools. Di decision depend on whether AI need interact wit external systems or fit answer from im own knowledge. Di guide below summarize when tools get value and when dem no necessary:

<img src="../../../translated_images/pcm/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*Quick decision guide — tools dey for real-time data, calculations, and actions; general knowledge and creative tasks no really need dem.*

## Tools vs RAG

Modules 03 and 04 dey extend wetin AI fit do, but for different ways. RAG dey give model access to **knowledge** by retrieving documents. Tools dey give model di ability to do **actions** by calling functions. Di diagram below compare di two approaches side by side — from how each workflow dey operate to their trade-offs:

<img src="../../../translated_images/pcm/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG dey retrieve information from static documents — Tools dey execute actions and fetch dynamic, real-time data. Plenty production systems dey combine both.*

For real life, many production systems dey use both approaches: RAG to ground answers for your documentation, and Tools to fetch live data or do operations.

## Next Steps

**Next Module:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigation:** [← Previous: Module 03 - RAG](../03-rag/README.md) | [Back to Main](../README.md) | [Next: Module 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:  
Dis document na AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator) wey translate am. Even though we dey try make am correct, abeg sabi say automated translation fit get some mistakes or no too correct. The original document wey dey im own language na the correct one. If na serious matter, make person wey sabi do professional human translation help. We no go responsible for any wahala or mistake wey fit happen because of this translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->