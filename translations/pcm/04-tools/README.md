# Module 04: AI Agents wit Tools

## Table of Contents

- [Video Walkthrough](../../../04-tools)
- [Wetín You Go Learn](../../../04-tools)
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

## Video Walkthrough

Watch dis live session wey explain how to start wit dis module:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="AI Agents with Tools and MCP - Live Session" width="800"/></a>

## Wetín You Go Learn

So far, you don learn how to yarn wit AI, how to arrange prompts well well, and how to base responses for your documents. But one gbege still dey: language models fit only make text. Dem no fit check weather, do maths, ask databases, or dey interact wit outside systems.

Tools change dis. By giving the model access to functions wey e fit call, you go turn am from just text generator to agent wey fit take action. The model dey decide when e need tool, which tool to use, and which parameters to pass. Your code go run the function and return the result. The model go add that result to e response.

## Prerequisites

- Don finish [Module 01 - Introduction](../01-introduction/README.md) (Azure OpenAI resources done deploy)
- Don finish previous modules wey dem recommend (dis module dey talk about [RAG concepts from Module 03](../03-rag/README.md) for the Tools vs RAG comparison)
- `.env` file for root directory wit Azure credentials (na `azd up` for Module 01 create am)

> **Note:** If you never finish Module 01, start from there first with the deployment instructions.

## Understanding AI Agents wit Tools

> **📝 Note:** The word "agents" for dis module mean AI assistants wey dem add tool-calling abilities to. Dis different from **Agentic AI** patterns (autonomous agents wit planning, memory, and multi-step reasoning) wey we go cover for [Module 05: MCP](../05-mcp/README.md).

Without tools, language model fit only generate text from wetin e learn. If you ask am about weather now, e go just guess. But if you give am tools, e fit call weather API, do maths, or ask database — then join real result inside e response.

<img src="../../../translated_images/pcm/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*Without tools the model go just guess — wit tools e fit call APIs, do maths, and bring real-time data.*

AI agent wit tools dey follow **Reasoning and Acting (ReAct)** pattern. The model no just answer — e dey think wetin e need, call tool, check the result, then decide if e go act again or give answer:

1. **Reason** — Agent dey check user question and find wetin e need
2. **Act** — Agent pick correct tool, set parameters, and call am
3. **Observe** — Agent see the tool output and judge am
4. **Repeat or Respond** — If e still need data, e go start again; if no, e go give normal language answer

<img src="../../../translated_images/pcm/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*ReAct cycle — agent dey reason, act by calling tool, observe result, and continue till e fit give final answer.*

Dis dey happen automatically. You go define tools and their description. The model go dey handle when and how to use dem.

## How Tool Calling Dey Work

### Tool Definitions

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

You go define functions wit clear description and parameters. The model go see these description inside system prompt and understand wetin each tool dey do.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // How you dey find weather
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Assistant na Spring Boot dey automatically connect am with:
// - ChatModel bean
// - All @Tool methods from @Component classes
// - ChatMemoryProvider for session management
```

The diagram below show every annotation and how each part dey help AI know when to call tool and which arguments to pass:

<img src="../../../translated_images/pcm/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*Anatomy of tool definition — @Tool tell AI when to use am, @P describe each parameter, and @AiService join everything together for startup.*

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) and ask:
> - "How I go join real weather API like OpenWeatherMap instead of mock data?"
> - "Wetin make beta tool description wey go help AI use am well?"
> - "How I go handle API wahala and rate limits for tool implementations?"

### Decision Making

If user ask "Wetyn the weather dey like for Seattle?", the model no just pick tool anyhow. E go compare user intent to all tool description wey e get, score all for relevance, then pick correct one. E go generate function call with right parameters — here e set `location` to `"Seattle"`.

If no tool fit user request, model go reply with wetin e know. If tool plenty wey fit, e go choose the most correct one.

<img src="../../../translated_images/pcm/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*Model dey check all available tools against user intent and pick correct one — na why clear, specific tool description dey important.*

### Execution

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot auto-wires the `@AiService` interfaces for all tools registered, LangChain4j go run tool calls automatically. Under the hood, tool call go run six stages — from user natural language question reach natural language answer:

<img src="../../../translated_images/pcm/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*End-to-end flow — user ask, model select tool, LangChain4j run am, model use result join response.*

If you run [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) for Module 00 before, you don see dis pattern — Calculator tool calls dey the same. Sequence diagram below show wetin happen under the hood that time:

<img src="../../../translated_images/pcm/tool-calling-sequence.94802f406ca26278.webp" alt="Tool Calling Sequence Diagram" width="800"/>

*Tool-calling loop from Quick Start demo — `AiServices` send your message and tool schema to LLM, LLM come reply wit function call like `add(42, 58)`, LangChain4j run Calculator method for local, then return result for final answer.*

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) and ask:
> - "How ReAct pattern dey work and why e dey effective for AI agents?"
> - "How agent dey decide which tool to use and wetin order?"
> - "If tool run fail, how I fit handle errors strong strong?"

### Response Generation

Model go receive weather data then format am as natural language response for user.

### Architecture: Spring Boot Auto-Wiring

Dis module dey use LangChain4j Spring Boot integration wit declarative `@AiService` interfaces. For startup, Spring Boot go find every `@Component` wey get `@Tool` methods, plus your `ChatModel` bean and `ChatMemoryProvider` — then wire dem all into one `Assistant` interface wit no extra code.

<img src="../../../translated_images/pcm/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*@AiService interface dey join ChatModel, tool components, and memory provider — Spring Boot dey handle all wiring automatically.*

This na full request lifecycle as sequence diagram — from HTTP request through controller, service, and auto-wired proxy, go all the way to tool execution and back:

<img src="../../../translated_images/pcm/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Spring Boot Tool Calling Sequence" width="800"/>

*Complete Spring Boot request lifecycle — HTTP request flow reach controller, service, auto-wired Assistant proxy, and e manage LLM and tool calls automatically.*

Big benefits of dis approach:

- **Spring Boot auto-wiring** — ChatModel and tools dey inject automatically
- **@MemoryId pattern** — Automatic session-based memory management
- **Single instance** — Assistant create once, dem dey reuse am for better speed
- **Type-safe execution** — Java methods dey call direct wit type conversion
- **Multi-turn orchestration** — E dey handle tool chaining automatically
- **Zero boilerplate** — No manual `AiServices.builder()` or memory HashMap

Other ways (manual `AiServices.builder()`) need more code and no get Spring Boot integration benefits.

## Tool Chaining

**Tool Chaining** — Real power of tool-based agents show when one question need many tools. If you ask "Wetyn weather dey like for Seattle in Fahrenheit?" agent go chain two tools automatic: first call `getCurrentWeather` get temp for Celsius, then pass am to `celsiusToFahrenheit` to convert — all na one conversation turn.

<img src="../../../translated_images/pcm/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*Tool chaining in action — agent call getCurrentWeather first, then use Celsius result for celsiusToFahrenheit, then give combined answer.*

**Graceful Failures** — If you ask weather for city wey no dey mock data, tool go return error message, AI go explain say e no fit help instead make e crash. Tools dey fail safely. Diagram below compare the two style — wit correct error handling, agent go catch the error and answer well; if no, whole app go crash:

<img src="../../../translated_images/pcm/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*If tool fail, agent go catch error and give helpful explanation instead of crash.*

Na one conversation turn dis one. Agent dey manage many tool calls by itself.

## Run the Application

**Check deployment:**

Make sure `.env` file dey root directory with Azure credentials (na Module 01 create am). Run dis from module folder (`04-tools/`):

**Bash:**
```bash
cat ../.env  # En go show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # En suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start the application:**

> **Note:** If you don start all apps wit `./start-all.sh` from root directory (like Module 01 talk), dis module dey run for port 8084 already. You fit skip the start commands here and go straight to http://localhost:8084.

**Option 1: Using Spring Boot Dashboard (Recommended for VS Code users)**

Dev container get Spring Boot Dashboard extension wey give GUI to manage all Spring Boot apps. You fit find am for Activity Bar left side of VS Code (look for the Spring Boot icon).

From Spring Boot Dashboard, you fit:
- See all Spring Boot apps wey dey workspace
- Start/stop apps with one click
- See app logs inside real-time
- Monitor app status
Just click di play button wey dey next to "tools" to start dis module, or start all modules at once.

Na so di Spring Boot Dashboard be for VS Code:

<img src="../../../translated_images/pcm/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Di Spring Boot Dashboard for VS Code — start, stop, and monitor all modules from one place*

**Option 2: Using shell scripts**

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
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

Both scripts dey automatically load environment variables from di root `.env` file and go build di JARs if dem no dey.

> **Note:** If you prefer make you build all modules manually before you start:
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

## How to Use Di Application

Di application dey provide web interface wey you fit use to interact with AI agent wey get access to weather and temperature conversion tools. Na so di interface be — e get quick-start examples and chat panel for sending requests:

<a href="images/tools-homepage.png"><img src="../../../translated_images/pcm/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Di AI Agent Tools interface - quick examples and chat interface for interacting with tools*

### Try Simple Tool Usage

Start with simple request: "Convert 100 degrees Fahrenheit to Celsius". Di agent go sabi say e need di temperature conversion tool, go call am with correct parameters, then return di result. You go notice say dis kain thing feel natural - you no talk which tool make e use or how to call am.

### Test Tool Chaining

Now try sometin wey get small complication: "What's the weather in Seattle and convert am to Fahrenheit?" Watch as di agent go work am step by step. E first get di weather (wey dey return Celsius), then e go sabi say e need make e convert to Fahrenheit, go call di conversion tool, then join both results into one response.

### See Conversation Flow

Di chat interface dey keep conversation history, e make you fit do multi-turn interactions. You fit see all previous questions and answers, e easy to track di conversation and understand how di agent dey build context as you dey yarn many times.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/pcm/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Multi-turn conversation wey dey show simple conversions, weather lookups, and tool chaining*

### Experiment with Different Requests

Try different combinations:
- Weather lookups: "What's the weather in Tokyo?"
- Temperature conversions: "What is 25°C in Kelvin?"
- Combined queries: "Check di weather for Paris and tell me if e dey above 20°C"

You go see how di agent dey interpret natural language and map am to correct tool calls.

## Key Concepts

### ReAct Pattern (Reasoning and Acting)

Di agent dey change between reasoning (to decide wetin im go do) and acting (to use tools). Dis pattern dey allow autonomous problem-solving instead of just obey instructions.

### Tool Descriptions Matter

How you describe your tools dey affect how well di agent fit use dem. Clear and specific descriptions dey help di model understand when and how to call each tool.

### Session Management

Di `@MemoryId` annotation dey enable automatic session-based memory management. Each session ID get im own `ChatMemory` instance wey di `ChatMemoryProvider` bean dey manage, so many users fit interact with di agent at di same time without their conversations mixing. Di diagram below show how many users dey routed to separate memory stores based on their session IDs:

<img src="../../../translated_images/pcm/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Each session ID maps to isolated conversation history — users no fit see each other's messages.*

### Error Handling

Tools fit fail — APIs fit timeout, parameters fit be invalid, external services fit crash. Production agents need error handling so di model fit explain problems or try other ways instead of crash di whole app. When tool throw exception, LangChain4j go catch am and pass di error message back to di model, so e fit explain di problem in natural language.

## Available Tools

Di diagram below show di broad ecosystem of tools you fit build. Dis module show weather and temperature tools, but di same `@Tool` pattern fit work for any Java method — from database queries to payment processing.

<img src="../../../translated_images/pcm/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Any Java method wey annotated with @Tool fit become available to di AI — di pattern fit extend to databases, APIs, email, file operations, and more.*

## When to Use Tool-Based Agents

No all requests need tools. Di decision na if di AI need interact with external systems or e fit answer from im own knowledge. Di guide below summarize when tools dey add value and when dem no necessary:

<img src="../../../translated_images/pcm/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*A quick decision guide — tools dey for real-time data, calculations, and actions; general knowledge and creative tasks no need dem.*

## Tools vs RAG

Modules 03 and 04 both extend wetin di AI fit do, but for different ways. RAG dey give di model access to **knowledge** by retrieving documents. Tools dey give di model ability to perform **actions** by calling functions. Di diagram below compare both approaches side by side — from how dem workflow dey operate to di trade-offs wey dey:

<img src="../../../translated_images/pcm/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG dey retrieve info from static documents — Tools dey execute actions and fetch dynamic, real-time data. Many production systems dey combine both.*

For practice, many production systems dey use both ways together: RAG to ground answers for your documentation, and Tools to fetch live data or perform actions.

## Next Steps

**Next Module:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigation:** [← Previous: Module 03 - RAG](../03-rag/README.md) | [Back to Main](../README.md) | [Next: Module 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis document don been translate usin AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). Even though we dey try make am correct, abeg remember say automated translations fit get errors or mistakes. Di original document wey dem write for im own language na di correct one. For important matter, better make person wey sabi human translation do am. We no dey responsible for any wahala or wrong understanding wey fit come from dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->