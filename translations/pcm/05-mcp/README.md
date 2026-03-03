# Module 05: Model Context Protocol (MCP)

## Table of Contents

- [Wetin You Go Learn](../../../05-mcp)
- [Wetin be MCP?](../../../05-mcp)
- [How MCP Dey Work](../../../05-mcp)
- [The Agentic Module](../../../05-mcp)
- [How to Run di Examples](../../../05-mcp)
  - [Wetin You Need First](../../../05-mcp)
- [Quick Start](../../../05-mcp)
  - [File Operations (Stdio)](../../../05-mcp)
  - [Supervisor Agent](../../../05-mcp)
    - [How to Run di Demo](../../../05-mcp)
    - [How Supervisor Dey Work](../../../05-mcp)
    - [Response Strategies](../../../05-mcp)
    - [How to Understand di Output](../../../05-mcp)
    - [Explanation of Agentic Module Features](../../../05-mcp)
- [Key Concepts](../../../05-mcp)
- [Congratulations!](../../../05-mcp)
  - [Wetin Next?](../../../05-mcp)

## Wetin You Go Learn

You don build conversational AI, master prompts, ground your responses for documents, and create agents wit tools dem. But all dis tools na custom-build only for your own application. Wetin if you fit give your AI access to one standardized ecosystem of tools wey anybody fit create and share? For dis module, you go learn how to do am wit Model Context Protocol (MCP) and LangChain4j's agentic module. We go first show simple MCP file reader then show how e fit enter advanced agentic workflows using di Supervisor Agent pattern.

## Wetin be MCP?

Model Context Protocol (MCP) na exactly wetin e be - na standard way wey AI applications fit find and use external tools. Instead make you dey write custom integration for each data source or service, you go connect to MCP servers wey dey show their capabilities for one consistent way. Your AI agent fit then find and use these tools automatically.

Di diagram wey dey below show di difference — without MCP, every integration need custom point-to-point wiring; wit MCP, one protocol dey connect your app to any tool:

<img src="../../../translated_images/pcm/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Before MCP: Complex point-to-point integrations. After MCP: One protocol, endless possibilities.*

MCP solve one fundamental wahala for AI development: every integration na custom. You want access GitHub? Custom code. You want read files? Custom code. You want query database? Custom code. And none of these integrations dey work wit other AI applications.

MCP standardize all dis tins. MCP server dey expose tools wit clear descriptions and schemas. Any MCP client fit connect, find available tools, and use dem. Build once, use everywhere.

Di diagram wey dey below show di architecture — one MCP client (na your AI application) dey connect to many MCP servers, each server dey expose their own set of tools through standard protocol:

<img src="../../../translated_images/pcm/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol architecture - standardized tool discovery and execution*

## How MCP Dey Work

Under di hood, MCP dey use layered architecture. Your Java application (the MCP client) dey find available tools, dey send JSON-RPC requests through transport layer (Stdio or HTTP), then MCP server dey run the operations and return results. Di diagram wey follow dey breakdown each layer of dis protocol:

<img src="../../../translated_images/pcm/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*How MCP dey work under di hood — clients dey find tools, dey exchange JSON-RPC messages, then dey run operations through transport layer.*

**Server-Client Architecture**

MCP dey use client-server model. Servers dey provide tools - dem fit dey read files, query database, call APIs. Clients (your AI application) dey connect to servers and use their tools.

To use MCP wit LangChain4j, add dis Maven dependency:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Tool Discovery**

When your client connect to MCP server, e go ask "Wetin tools you get?" The server go respond wit list of available tools, each get description and parameter schemas. Your AI agent fit then decide which tools to use based on user request. Di diagram wey dey below show dis handshake — di client send `tools/list` request and the server go return dia available tools wit description and parameter schemas:

<img src="../../../translated_images/pcm/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*The AI dey discover available tools for startup — e don sabi wetin capabilities dey, fit decide which one to use.*

**Transport Mechanisms**

MCP support different transport mechanisms. Two options be Stdio (for local subprocess communication) and Streamable HTTP (for remote servers). Dis module dey demonstrate Stdio transport:

<img src="../../../translated_images/pcm/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP transport mechanisms: HTTP for remote servers, Stdio for local processes*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

For local processes. Your application dey spawn one server as subprocess and dem dey communicate through standard input/output. Dis one good for filesystem access or command-line tools.

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

Di `@modelcontextprotocol/server-filesystem` server dey expose dis tools, all na inside sandbox for directories wey you specify:

| Tool | Description |
|------|-------------|
| `read_file` | Read contents of single file |
| `read_multiple_files` | Read multiple files for one call |
| `write_file` | Create or overwrite file |
| `edit_file` | Make targeted find-and-replace edits |
| `list_directory` | List files and directories for one path |
| `search_files` | Recursively search files wey match pattern |
| `get_file_info` | Get file metadata (size, timestamps, permissions) |
| `create_directory` | Create directory (including parent directories) |
| `move_file` | Move or rename file or directory |

Di diagram wey follow show how Stdio transport dey work for runtime — your Java application dey spawn MCP server as child process and dem dey communicate through stdin/stdout pipes, no network or HTTP involved:

<img src="../../../translated_images/pcm/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio transport dey action — your application dey spawn MCP server as child process and dem dey communicate through stdin/stdout pipes.*

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) and ask:
> - "How Stdio transport dey work and when I suppose use am vs HTTP?"
> - "How LangChain4j dey manage lifecycle of spawned MCP server processes?"
> - "Wetin be security implication if AI get access to file system?"

## The Agentic Module

Even if MCP provide standardized tools, LangChain4j's **agentic module** dey give declarative way to build agents wey dey orchestrate tools dem. Di `@Agent` annotation and `AgenticServices` dey let you define agent behavior through interfaces instead of imperative code.

For dis module, you go explore **Supervisor Agent** pattern — na advanced agentic AI way wey "supervisor" agent dey dynamically decide which sub-agents to call based on user request. We go combine both tin by giving one sub-agent MCP-powered file access capabilities.

To use agentic module, add dis Maven dependency:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Note:** Di `langchain4j-agentic` module dey use separate version property (`langchain4j.mcp.version`) because e dey release for different schedule than core LangChain4j libraries.

> **⚠️ Experimental:** Di `langchain4j-agentic` module still dey **experimental** and fit change anytime. Di stable way to build AI assistants still be `langchain4j-core` wit custom tools (Module 04).

## How to Run di Examples

### Wetin You Need First

- You for don finish [Module 04 - Tools](../04-tools/README.md) (dis module dey build on custom tool concepts and compare dem wit MCP tools)
- `.env` file for root directory wit Azure credentials (wey `azd up` create for Module 01)
- Java 21+, Maven 3.9+
- Node.js 16+ and npm (for MCP servers)

> **Note:** If you never set up your environment variables yet, check [Module 01 - Introduction](../01-introduction/README.md) for deployment instructions (`azd up` go create `.env` file automatically), or copy `.env.example` to `.env` for root directory and fill your values.

## Quick Start

**If you dey Use VS Code:** Just right-click on any demo file for Explorer and choose **"Run Java"**, or use launch configurations wey dey Run and Debug panel (make sure `.env` file get your Azure credentials first).

**If you dey Use Maven:** You fit run from command line wit di examples wey follow.

### File Operations (Stdio)

Dis one dey show local subprocess-based tools.

**✅ No prerequisites needed** - MCP server go spawn automatically.

**Using Start Scripts (Recommended):**

Start scripts dey automatically load environment variables from root `.env` file:

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

**Using VS Code:** Right-click on `StdioTransportDemo.java` and select **"Run Java"** (make sure `.env` file get your Azure credentials).

Application go spawn filesystem MCP server automatically and read local file. Notice how subprocess management dey handled for you.

**Expected output:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor Agent

**Supervisor Agent pattern** na **flexible** kind agentic AI. Supervisor dey use LLM to autonomously decide which agents to call based on user request. For di next example, we go combine MCP-powered file access with LLM agent to create supervised file read → report workflow.

For di demo, `FileAgent` go read file using MCP filesystem tools, and `ReportAgent` go create structured report wit executive summary (1 sentence), 3 key points, and recommendation dem. Supervisor go automatically control dis flow:

<img src="../../../translated_images/pcm/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Supervisor dey use dia LLM to decide which agents to call and wetin order — no need hardcoded routing.*

Dis na wetin di concrete workflow go look like for our file-to-report pipeline:

<img src="../../../translated_images/pcm/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent go read file via MCP tools, then ReportAgent go convert raw content into structured report.*

Each agent go store output inside **Agentic Scope** (shared memory), so downstream agents fit access previous results. Dis one show how MCP tools fit enter agentic workflows smoothly — Supervisor no need sabi *how* files dem dey read, e just need know say `FileAgent` fit do am.

#### How to Run di Demo

Start scripts dey automatically load environment variables from root `.env` file:

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

**Using VS Code:** Right-click on `SupervisorAgentDemo.java` and select **"Run Java"** (make sure `.env` file get your Azure credentials).

#### How Supervisor Dey Work

Before you start to build agents, you go connect MCP transport to client and wrap am as `ToolProvider`. Na so MCP server tools go dey available to your agents:

```java
// Make one MCP client from di transport
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Wrap di client as one ToolProvider — dis one dey connect MCP tools join LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Now you fit inject `mcpToolProvider` inside any agent wey need MCP tools:

```java
// Step 1: FileAgent dey read files wit MCP tools
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Get MCP tools for file wahala dem
        .build();

// Step 2: ReportAgent dey make structured reports
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor dey run the file → report work process
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Return the final report
        .build();

// The Supervisor na him dey decide which agents to call based on the request
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Response Strategies

When you dey configure `SupervisorAgent`, you go specify how e go form final answer for user after sub-agents don finish work. Di diagram wey dey below show three strategies wey available — LAST go directly return output from last agent, SUMMARY go synthesize all outputs wit LLM, and SCORED go pick output wey score pass for original request:

<img src="../../../translated_images/pcm/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Three strategies for how Supervisor dey form final response — choose based on whether you want last agent output, synthesized summary, or best-scoring option.*

Di strategies wey dey available be:

| Strategy | Description |
|----------|-------------|
| **LAST** | Supervisor go return output from last sub-agent or tool wey dem call. E good when last agent in workflow dey specially design to produce complete final answer (e.g., "Summary Agent" for research pipeline). |
| **SUMMARY** | Supervisor go use im own internal Language Model (LLM) to synthesize summary of full interaction and all sub-agent outputs, then return dat summary as final response. E provide clean aggregated answer to user. |
| **SCORED** | System go use internal LLM to score both LAST response and SUMMARY of interaction against original user request, then return output wey get higher score. |
See [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) for di full implementation.

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) and ask:
> - "How di Supervisor decide which agents to invoke?"
> - "Wetin be di difference between Supervisor and Sequential workflow patterns?"
> - "How I fit customize di Supervisor's planning behavior?"

#### Understanding di Output

When you run di demo, you go see how Supervisor dey arrange multiple agents one by one. Na wetin each section mean be dis:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Di header** dey introduce workflow concept: na focused pipeline from file reading reach report generation.

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

**Workflow Diagram** dey show how data dey flow between agents. Each agent get im role:
- **FileAgent** dey read files using MCP tools and dey store raw content inside `fileContent`
- **ReportAgent** dey use dat content and dey produce structured report inside `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**User Request** dey show di task. Di Supervisor go parse am and decide to invoke FileAgent → ReportAgent.

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

**Supervisor Orchestration** show di 2-step flow for action:
1. **FileAgent** go read di file via MCP and save di content
2. **ReportAgent** go receive di content and generate structured report

Supervisor make all dis decisions **on e own** based on wetin user request.

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

Di example dey show plenty advanced features of di agentic module. Make we look Agentic Scope and Agent Listeners well well.

**Agentic Scope** na di shared memory where agents store their results using `@Agent(outputKey="...")`. Dis one dey allow:
- Later agents to fit access earlier agents' outputs
- Di Supervisor to fit put together di final response
- You to check wetin each agent produce

Di diagram below dey show how Agentic Scope dey work as shared memory for di file-to-report workflow — FileAgent dey write output under di key `fileContent`, ReportAgent dey read am then dey write im own output under `report`:

<img src="../../../translated_images/pcm/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope dey act as shared memory — FileAgent dey write `fileContent`, ReportAgent go read am then write `report`, and your code go read di final result.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Raw file data from FileAgent
String report = scope.readState("report");            // Structured report from ReportAgent
```

**Agent Listeners** dey enable monitoring and debugging of agent execution. Di step-by-step output you dey see for demo come from an AgentListener wey dey hook into each agent invocation:
- **beforeAgentInvocation** - E dey call when Supervisor select agent, make you fit see which agent dem choose and why
- **afterAgentInvocation** - E dey call when agent finish, dey show im result
- **inheritedBySubagents** - When true, di listener go monitor all agents wey dey inside hierarchy

Di diagram below dey show full Agent Listener lifecycle, including how `onError` dey handle failures during agent execution:

<img src="../../../translated_images/pcm/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners dey hook into di execution lifecycle — dem dey monitor when agents start, finish, or when errors show.*

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
        return true; // Carry go all di sub-agents dem
    }
};
```

Beyond di Supervisor pattern, di `langchain4j-agentic` module get several strong workflow patterns. Di diagram below dey show all five — from simple sequential pipelines reach human-in-the-loop approval workflows:

<img src="../../../translated_images/pcm/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Five workflow patterns for managing agents — from simple sequential pipelines to human-in-the-loop approval workflows.*

| Pattern | Description | Use Case |
|---------|-------------|----------|
| **Sequential** | Execute agents one after another, output dey flow to next | Pipelines: research → analyze → report |
| **Parallel** | Run agents at the same time | Tasks wey dey independent: weather + news + stocks |
| **Loop** | Repeat till condition met | Quality scoring: refine till score ≥ 0.8 |
| **Conditional** | Route based on conditions | Classify → send go specialist agent |
| **Human-in-the-Loop** | Add human checkpoints | Approval workflows, content review |

## Key Concepts

Now wey you don explore MCP and agentic module for action, make we summarize wen to use each approach.

One big advantage of MCP na im get growing ecosystem. Di diagram below dey show how one universal protocol dey connect your AI application to many MCP servers — from filesystem and database access to GitHub, email, web scraping, and more:

<img src="../../../translated_images/pcm/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP dey build universal protocol ecosystem — any MCP-compatible server fit work with any MCP-compatible client, which enable tool sharing across applications.*

**MCP** best when you wan use existing tool ecosystems, build tools wey multiple applications fit share, integrate third-party services through standard protocols, or swap tool implementations without changing code.

**The Agentic Module** best when you want declarative agent definitions with `@Agent` annotations, need workflow orchestration (sequential, loop, parallel), prefer interface-based agent design more than imperative code, or you dey combine multiple agents wey dey share outputs through `outputKey`.

**The Supervisor Agent pattern** dey shine when workflow no clear beforehand and you want LLM to decide, when you get multiple specialized agents wey need dynamic arrangement, when you dey build conversational systems wey fit route to different abilities, or when you want most flexible, adaptive agent behavior.

To help you decide between custom `@Tool` methods from Module 04 and MCP tools from dis module, di comparison below point out main trade-offs — custom tools give tight coupling and full type safety for app-specific logic, while MCP tools provide standardized, reusable integrations:

<img src="../../../translated_images/pcm/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*When to use custom @Tool methods vs MCP tools — custom tools for app-specific logic with full type safety, MCP tools for standardized integrations wey fit work across applications.*

## Congratulations!

You don pass through all five modules of LangChain4j for Beginners course! This na how your full learning journey look — from basic chat reach MCP-powered agentic systems:

<img src="../../../translated_images/pcm/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Your learning journey through all five modules — from basic chat to MCP-powered agentic systems.*

You don complete LangChain4j for Beginners course. You don learn:

- How to build conversational AI with memory (Module 01)
- Prompt engineering patterns for different tasks (Module 02)
- Grounding responses inside your documents with RAG (Module 03)
- Creating basic AI agents (assistants) with custom tools (Module 04)
- Integrating standardized tools with LangChain4j MCP and Agentic modules (Module 05)

### Wetin dey Next?

After you finish di modules, explore [Testing Guide](../docs/TESTING.md) to see LangChain4j testing concepts for action.

**Official Resources:**
- [LangChain4j Documentation](https://docs.langchain4j.dev/) - Complete guides and API reference
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Source code and examples
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - Step-by-step tutorials for different use cases

Thank you for completing dis course!

---

**Navigation:** [← Previous: Module 04 - Tools](../04-tools/README.md) | [Back to Main](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis document don translate wit AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). Even as we dey try make am correct, make you sabi say automated translation fit get some mistake or wrong tori inside. Di original document wey e dey for im own language na im you suppose trust pass. For important info, make person wey sabi translate am well make e do am. We no go take responsibility if person no understand well or if e misinterpret dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->