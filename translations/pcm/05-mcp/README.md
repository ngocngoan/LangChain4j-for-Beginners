# Module 05: Model Context Protocol (MCP)

## Table of Contents

- [Wetyn You Go Learn](../../../05-mcp)
- [Wetyn Be MCP?](../../../05-mcp)
- [How MCP Dey Work](../../../05-mcp)
- [The Agentic Module](../../../05-mcp)
- [How To Run Di Examples](../../../05-mcp)
  - [Wetyn You Need Before](../../../05-mcp)
- [Quick Start](../../../05-mcp)
  - [File Operations (Stdio)](../../../05-mcp)
  - [Supervisor Agent](../../../05-mcp)
    - [How To Run Di Demo](../../../05-mcp)
    - [How Di Supervisor Dey Work](../../../05-mcp)
    - [How FileAgent Dey Find MCP Tools for Runtime](../../../05-mcp)
    - [Response Strategies](../../../05-mcp)
    - [How To Understand Di Output](../../../05-mcp)
    - [Explanation of Agentic Module Features](../../../05-mcp)
- [Key Concepts](../../../05-mcp)
- [Congrats!](../../../05-mcp)
  - [Wetyn Next?](../../../05-mcp)

## Wetyn You Go Learn

You don don build conversational AI, sabi prompts well well, make responses dey based on documents, and create agents with tools. But all di tools dem na custom-build for your own app. Wetin if you fit make your AI get access to standard ecosystem of tools wey anybody fit create and share? For this module, you go learn how to do am wit di Model Context Protocol (MCP) and LangChain4j’s agentic module. We go first show simple MCP file reader then show how e fit easily join advanced agentic workflows with di Supervisor Agent pattern.

## Wetyn Be MCP?

Di Model Context Protocol (MCP) na exactly dis – na standard way for AI apps to find and use external tools. Instead of making custom integration for each data source or service, you go just connect to MCP servers wey show their capabilities for one consistent format. Your AI agent fit then find and use these tools automatically.

Di diagram below show di difference — without MCP, every integration need custom point-to-point wiring; with MCP, one protocol connect your app to any tool:

<img src="../../../translated_images/pcm/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Before MCP: Complex point-to-point integrations. After MCP: One protocol, endless possibilities.*

MCP dey solve one big wahala for AI development: every integration na custom. You want access GitHub? Custom code. You want read files? Custom code. You want query database? Custom code. And none of these integrations fit work with other AI apps.

MCP come standardize am. MCP server dey expose tools with clear descriptions and schemas. Any MCP client fit connect, find available tools, and use dem. Build once, use everywhere.

Di diagram below show dis architecture — one MCP client (your AI app) dey connect to many MCP servers, each dey expose their own set of tools through di standard protocol:

<img src="../../../translated_images/pcm/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol architecture - standardized tool discovery and execution*

## How MCP Dey Work

For ground, MCP use layered architecture. Your Java app (di MCP client) go find available tools, send JSON-RPC requests through transport layer (Stdio or HTTP), and di MCP server de carry out di operations and send back results. Di following diagram break down each layer of dis protocol:

<img src="../../../translated_images/pcm/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*How MCP dey work for ground — clients dey find tools, dey exchange JSON-RPC messages, and dey execute operations through transport layer.*

**Server-Client Architecture**

MCP dey use client-server model. Servers dey provide tools — dem fit read files, query databases, call APIs. Clients (your AI app) connect to servers and use their tools.

To use MCP with LangChain4j, add dis Maven dependency:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Tool Discovery**

When your client connect MCP server, e go ask "Which tools you get?" Di server go reply wit list of tools wey e get, plus their descriptions and parameter schemas. Your AI agent fit select which tools to use based on wetin user ask. Di diagram below show dis handshake — client dey send `tools/list` request and di server go return available tools wit descriptions and parameters:

<img src="../../../translated_images/pcm/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*Di AI dey find tools wey e fit use at startup — e don know wetin e fit do and fit choose which tools to use.*

**Transport Mechanisms**

MCP support different transport mechanisms. Di two options na Stdio (for local subprocess communication) and Streamable HTTP (for remote servers). Dis module dey show Stdio transport:

<img src="../../../translated_images/pcm/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP transport mechanisms: HTTP for remote servers, Stdio for local processes*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

For local processes. Your app go spawn one server as subprocess and dem go communicate through standard input/output. E good for filesystem access or command-line tools.

```java
McpTransport stdioTransport = new StdioMcpTransport.Builder()
    .command(List.of(
        npmCmd, "exec",
        "@modelcontextprotocol/server-filesystem@2025.12.18",
        resourcesDir
    ))
    .logEvents(false)
    .build();
```

Di `@modelcontextprotocol/server-filesystem` server dey expose these tools, all of dem dey sandboxed to di directories wey you specify:

| Tool | Description |
|------|-------------|
| `read_file` | Read the contents of one single file |
| `read_multiple_files` | Read many files with one call |
| `write_file` | Create or overwrite a file |
| `edit_file` | Make targeted find-and-replace edits |
| `list_directory` | List files and directories wey dey one path |
| `search_files` | Recursively search for files wey match one pattern |
| `get_file_info` | Get file metadata (size, timestamps, permissions) |
| `create_directory` | Create directory (including parent directories) |
| `move_file` | Move or rename file or directory |

Di diagram below show how Stdio transport dey work for runtime — your Java app go spawn MCP server as child process and dem go communicate through stdin/stdout pipes, network or HTTP no dey involved:

<img src="../../../translated_images/pcm/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio transport for action — your app go spawn MCP server as child process and dem go communicate through stdin/stdout pipes.*

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) and ask:
> - "How Stdio transport dey work and when I suppose use am vs HTTP?"
> - "How LangChain4j dey manage lifecycle of spawned MCP server processes?"
> - "Wetin be security wahala if I give AI access to file system?"

## The Agentic Module

Even though MCP dey provide standardized tools, LangChain4j’s **agentic module** na declarative way to build agents wey go control those tools. `@Agent` annotation and `AgenticServices` dey let you define agent behavior through interfaces instead of imperative code.

For dis module, you go explore **Supervisor Agent** pattern — na advanced agentic AI approach where one "supervisor" agent go dynamically decide which sub-agents to call based on user request. We go combine both concepts by giving one of our sub-agents MCP-powered file access powers.

To use agentic module, add dis Maven dependency:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Note:** Di `langchain4j-agentic` module get separate version property (`langchain4j.mcp.version`) because e dey released for different schedule than di core LangChain4j libraries.

> **⚠️ Experimental:** Di `langchain4j-agentic` module still **experimental** and fit change. Di stable way to build AI assistants still na `langchain4j-core` wit custom tools (Module 04).

## How To Run Di Examples

### Wetyn You Need Before

- Finishi [Module 04 - Tools](../04-tools/README.md) (dis module dey build on top custom tools idea and e dey compare dem wit MCP tools)
- `.env` file for root directory wit Azure credentials (join create am with `azd up` for Module 01)
- Java 21+, Maven 3.9+
- Node.js 16+ and npm (for MCP servers)

> **Note:** If you never set up your environment variables yet, see [Module 01 - Introduction](../01-introduction/README.md) for deployment instructions (`azd up` go create `.env` file automatically), or copy `.env.example` go `.env` for root folder and fill your values.

## Quick Start

**Using VS Code:** Just right-click any demo file for Explorer and select **"Run Java"**, or use launch configurations from Run and Debug panel (make sure your `.env` file get Azure credentials first).

**Using Maven:** Or you fit run am from command line wit examples below.

### File Operations (Stdio)

Dis one dey show local subprocess-based tools.

**✅ No prerequisites needed** - di MCP server go spawn automatically.

**Using di Start Scripts (Recommended):**

Di start scripts go automatically load environment variables from root `.env` file:

**Bash:**
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-stdio.ps1
```

**Using VS Code:** Right-click `StdioTransportDemo.java` and select **"Run Java"** (make sure your `.env` file get correct config).

Di app go spawn files MCP server automatically and read one local file. See how subprocess management dey handled for you.

**Expected output:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor Agent

Di **Supervisor Agent pattern** na **flexible** way to do agentic AI. Supervisor dey use one LLM to autonomously decide which agents to call based on wetin user talk. For di next example, we go combine MCP-powered file access wit LLM agent to create supervised file read → report workflow.

For the demo, `FileAgent` go read file using MCP filesystem tools, and `ReportAgent` go create structured report wit executive summary (1 sentence), 3 key points, and recommendations. Di Supervisor go arrange dis flow automatically:

<img src="../../../translated_images/pcm/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Supervisor dey use e LLM to decide which agents to call and wetin order — no hardcoded routing needed.*

Dis na how real workflow dey be for our file-to-report pipeline:

<img src="../../../translated_images/pcm/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent dey read file using MCP tools, then ReportAgent dey change raw content come structured report.*

Di next sequence diagram follow full Supervisor orchestration — from spawn MCP server, through Supervisor’s autonomous agent selection, to tool calls over stdio and di final report:

<img src="../../../translated_images/pcm/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*Supervisor autonomously dey call FileAgent (wey dey call MCP server over stdio to read file), then dey call ReportAgent to generate structured report — all agents dey store their output for shared Agentic Scope.*

Each agent dey store output for **Agentic Scope** (shared memory), so downstream agents fit access previous results. Dis one show how MCP tools fit join agentic workflows smooth — Supervisor no need sabi *how* files dey read, e just know say `FileAgent` fit do am.

#### How To Run Di Demo

Di start scripts go automatically load environment variables from root `.env` file:

**Bash:**
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-supervisor.ps1
```

**Using VS Code:** Right-click `SupervisorAgentDemo.java` and select **"Run Java"** (make sure `.env` file get correct setup).

#### How Di Supervisor Dey Work

Before you start to build agents, you need connect MCP transport to client and wrap am as `ToolProvider`. Na so MCP server tools go fit dey available for your agents:

```java
// Make one MCP client from di transport
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Wrap di client as ToolProvider — dis one dey connect MCP tools enter LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Now you fit inject `mcpToolProvider` inside any agent wey need MCP tools:

```java
// Step 1: FileAgent dey read files with MCP tools
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Get MCP tools wey dem dey use do file operations
        .build();

// Step 2: ReportAgent dey generate structured reports
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor na im dey control the file → report workflow
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Return the final report
        .build();

// The Supervisor dey decide which agents to call based on the request
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### How FileAgent Dey Discover MCP Tools at Runtime

You fit dey ask: **how `FileAgent` sabi how to use npm filesystem tools?** Di truth be say e no sabi — na **LLM** dey find out for runtime through tool schemas.

Di `FileAgent` interface na just **prompt definition**. E no get hardcoded knowledge of `read_file`, `list_directory`, or any other MCP tool. Dis na wetin dey happen end-to-end:
1. **Server spawns:** `StdioMcpTransport` dey launch di `@modelcontextprotocol/server-filesystem` npm package as pikin process  
2. **Tool discovery:** Di `McpClient` go send `tools/list` JSON-RPC request go di server, wey go respond wit tool names, descriptions, plus parameter schemas (e.g., `read_file` — *"Read di complete contents of file"* — `{ path: string }`)  
3. **Schema injection:** `McpToolProvider` go wrap di discover schemata dem and make dem available to LangChain4j  
4. **LLM decides:** Wen `FileAgent.readFile(path)` get call, LangChain4j go send system message, user message, **and di list of tool schemas** go di LLM. Di LLM go read di tool descriptions and generate one tool call (e.g., `read_file(path="/some/file.txt")`)  
5. **Execution:** LangChain4j go catch di tool call, route am through di MCP client come back to di Node.js subprocess, collect di result, den give am back to di LLM  

Dis na di same [Tool Discovery](../../../05-mcp) mechanism wey dem don yarn before, but na dis time e apply specially for di agent workflow. Di `@SystemMessage` and `@UserMessage` annotations dey guide di LLM behavior, while di injected `ToolProvider` dey give am di **capabilities** — di LLM dey connect di two for runtime.  

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) and ask:  
> - "How dis agent sabi which MCP tool to call?"  
> - "Wetin go happen if I comot di ToolProvider from di agent builder?"  
> - "How tool schemas dey pass go LLM?"  

#### Response Strategies

Wen you configure one `SupervisorAgent`, you go specify how e suppose take form im final answer for user after di sub-agents don finish their work. Di diagram below dey show di three strategies wey dey — LAST dey return di final agent output straight, SUMMARY dey summarize all outputs wit one LLM, and SCORED go pick di one wey score pass for original request:  

<img src="../../../translated_images/pcm/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>  

*Three strategies of how Supervisor go take make e final response — choose based on whether you want di last agent output, one summarize, or di best scoring option.*  

Di strategies wey dey available na:  

| Strategy | Description |  
|----------|-------------|  
| **LAST** | Di supervisor go return di output of last sub-agent or tool wey e call. E good if di final agent for di workflow na im suppose produce di complete final answer (e.g., "Summary Agent" for research pipeline). |  
| **SUMMARY** | Di supervisor go use im own internal Language Model (LLM) summarize all di interaction and sub-agent outputs, den return dat summary as di final response. E give clean aggregated answer to user. |  
| **SCORED** | Di system go use internal LLM score both di LAST response and di SUMMARY against original user request, then return di one wey get the higher score. |  

See [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) for di full implementation.  

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) and ask:  
> - "How di Supervisor decide which agents to call?"  
> - "Wetin make Supervisor different from Sequential workflow patterns?"  
> - "How I fit customize Supervisor planning behavior?"  

#### Understanding the Output

Wen you run di demo, you go see structured walkthrough of how Supervisor dey organize multiple agents. This na wetin each section mean:  

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Di header** dey introduce di workflow concept: one focused pipeline from file reading to report generation.  

```
--- WORKFLOW ---------------------------------------------------------
  ┌─────────────┐      ┌──────────────┐
  │  FileAgent  │ ───▶ │ ReportAgent  │
  │ (MCP tools) │      │  (pure LLM)  │
  └─────────────┘      └──────────────┘
   outputKey:           outputKey:
   'fileContent'        'report'

--- AVAILABLE AGENTS -------------------------------------------------
  [FILE]   FileAgent   - Reads files via MCP → stores in 'fileContent'
  [REPORT] ReportAgent - Generates structured report → stores in 'report'
```
  
**Workflow Diagram** dey show data flow between agents. Each agent get specific role:  
- **FileAgent** dey read files using MCP tools and dem store raw content for `fileContent`  
- **ReportAgent** go consume dat content and produce structured report for `report`  

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**User Request** dey show di task. Di Supervisor dey parse am and decide to invoke FileAgent → ReportAgent.  

```
--- SUPERVISOR ORCHESTRATION -----------------------------------------
  The Supervisor decides which agents to invoke and passes data between them...

  +-- STEP 1: Supervisor chose -> FileAgent (reading file via MCP)
  |
  |   Input: .../file.txt
  |
  |   Result: LangChain4j is an open-source, provider-agnostic Java framework for building LLM...
  +-- [OK] FileAgent (reading file via MCP) completed

  +-- STEP 2: Supervisor chose -> ReportAgent (generating structured report)
  |
  |   Input: LangChain4j is an open-source, provider-agnostic Java framew...
  |
  |   Result: Executive Summary...
  +-- [OK] ReportAgent (generating structured report) completed
```
  
**Supervisor Orchestration** dey show di 2-step flow for action:  
1. **FileAgent** dey read file through MCP and store di content  
2. **ReportAgent** dey get di content then e generate structured report  

Di Supervisor make all dis decisions **by imself** based on user request.  

```
--- FINAL RESPONSE ---------------------------------------------------
Executive Summary
...

Key Points
...

Recommendations
...

--- AGENTIC SCOPE (Data Flow) ----------------------------------------
  Each agent stores its output for downstream agents to consume:
  * fileContent: LangChain4j is an open-source, provider-agnostic Java framework...
  * report: Executive Summary...
```
  
#### Explanation of Agentic Module Features

Di example dey show some advanced features of di agentic module. Make we check Agentic Scope and Agent Listeners well well.  

**Agentic Scope** dey show di shared memory wey agents dey store results using `@Agent(outputKey="...")`. Dis one allow:  
- Later agents fit access results wey earlier agents produce  
- Supervisor fit synthesize final response  
- You fit check wetin each agent produce  

Di diagram below dey show how Agentic Scope dey work as shared memory for di file-to-report workflow — FileAgent dey write output under di key `fileContent`, ReportAgent go read dat one then write im own output under `report`:  

<img src="../../../translated_images/pcm/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>  

*Agentic Scope dey act as shared memory — FileAgent dey write `fileContent`, ReportAgent dey read am and write `report`, and your code dey read di final result.*  

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Raw file data from FileAgent
String report = scope.readState("report");            // Structured report from ReportAgent
```
  
**Agent Listeners** dey enable monitoring and debugging for agent execution. Di step-by-step output wey you dey see for demo come from AgentListener wey dey hook into each agent invocation:  
- **beforeAgentInvocation** - Di one wey dey call wen Supervisor select agent, e let you see which agent dem choose and why  
- **afterAgentInvocation** - Call am wen agent finish, show im result  
- **inheritedBySubagents** - If true, di listener go dey monitor all agents for di hierarchy  

Di diagram below show full Agent Listener lifecycle, including how `onError` dey handle failures during agent execution:  

<img src="../../../translated_images/pcm/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>  

*Agent Listeners dey hook into di execution lifecycle — dem dey monitor wen agents start, finish, or encounter error.*  

```java
AgentListener monitor = new AgentListener() {
    private int step = 0;
    
    @Override
    public void beforeAgentInvocation(AgentRequest request) {
        step++;
        System.out.println("  +-- STEP " + step + ": " + request.agentName());
    }
    
    @Override
    public void afterAgentInvocation(AgentResponse response) {
        System.out.println("  +-- [OK] " + response.agentName() + " completed");
    }
    
    @Override
    public boolean inheritedBySubagents() {
        return true; // Spread am go all di small agents dem
    }
};
```
  
Apart from Supervisor pattern, di `langchain4j-agentic` module dey provide several powerful workflow patterns. Di diagram below dey show all five — from simple sequential pipelines to human-in-the-loop approval workflows:  

<img src="../../../translated_images/pcm/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>  

*Five workflow patterns for organizing agents — from simple sequential pipelines to human-in-the-loop approval workflows.*  

| Pattern | Description | Use Case |  
|---------|-------------|----------|  
| **Sequential** | Execute agents in order, output dey flow to next | Pipelines: research → analyze → report |  
| **Parallel** | Run agents at di same time | Independent tasks: weather + news + stocks |  
| **Loop** | Repeat until condition fullfill | Quality scoring: continue refine until score ≥ 0.8 |  
| **Conditional** | Route based on conditions | Classify → send to specialist agent |  
| **Human-in-the-Loop** | Put human checkpoints | Approval workflows, content review |  

## Key Concepts

Now wey you don explore MCP plus di agentic module for action, make we summarize when to use each approach.  

One big advantage for MCP na im big ecosystem. Di diagram below dey show how one universal protocol connect your AI application go wide range of MCP servers — from filesystem and database access to GitHub, email, web scraping, plus more:  

<img src="../../../translated_images/pcm/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>  

*MCP create universal protocol ecosystem — any MCP-compatible server fit work with any MCP-compatible client, e dey enable tool sharing across applications.*  

**MCP** best when you want use existing tool ecosystems, build tools wey many apps fit share, integrate third-party services wit standard protocols, or fit change tool implementations without code wahala.  

**Agentic Module** best when you want declarative agent definitions wit `@Agent` annotations, need workflow orchestration (sequential, loop, parallel), prefer interface-based agent design over imperative code, or you dey combine multiple agents wey share outputs wit `outputKey`.  

**Supervisor Agent pattern** dey shine wen workflow no dey predictable beforehand and you want LLM to decide, wen you get multiple specialized agents wey need dynamic orchestration, wen you dey build conversational systems wey route to different capabilities, or wen you want flexible, adaptive agent behavior.  

To help you decide between custom `@Tool` methods from Module 04 and MCP tools from dis module, di comparison below show di key trade-offs — custom tools dey give tight coupling plus full type safety for app-specific logic, but MCP tools dey provide standardized, reusable integrations:  

<img src="../../../translated_images/pcm/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>  

*When to use custom @Tool methods vs MCP tools — custom tools good for app-specific logic wit full type safety, MCP tools good for standardized integrations wey fit work across apps.*  

## Congratulations!

You don finish all five modules for LangChain4j for Beginners course! Here na your full learning journey — from simple chat reach MCP-powered agentic systems:  

<img src="../../../translated_images/pcm/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>  

*Your learning journey through all five modules — from basic chat to MCP-powered agentic systems.*  

You don complete LangChain4j for Beginners course. You don learn:  

- How to build conversational AI wit memory (Module 01)  
- Prompt engineering patterns for different tasks (Module 02)  
- Ground responses in your documents wit RAG (Module 03)  
- Create basic AI agents (assistants) with custom tools (Module 04)  
- Integrate standardized tools wit LangChain4j MCP and Agentic modules (Module 05)  

### Wetin Next?

After you finish di modules, explore di [Testing Guide](../docs/TESTING.md) to see LangChain4j testing concepts for action.  

**Official Resources:**  
- [LangChain4j Documentation](https://docs.langchain4j.dev/) - Complete guides and API reference  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Source code and examples  
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - Step-by-step tutorials for different cases  

Thank you for completing dis course!  

---  

**Navigation:** [← Previous: Module 04 - Tools](../04-tools/README.md) | [Back to Main](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Waning Tok**:
Dis document don translate wit AI translation service wey dem dey call [Co-op Translator](https://github.com/Azure/co-op-translator). Even tho we dey try make e correct, abeg sabi say automated translation fit get mistake or no correct well well. Di original document wey dem write for im correct language na im be di main tori. If na serious wahala, e better make real human professional translate am. We no go fit take blame if person no understand well or if e wrong because of dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->