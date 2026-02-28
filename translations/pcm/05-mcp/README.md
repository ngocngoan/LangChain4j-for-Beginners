# Module 05: Model Context Protocol (MCP)

## Table of Contents

- [Wet you go learn](../../../05-mcp)
- [Wetin be MCP?](../../../05-mcp)
- [How MCP dey work](../../../05-mcp)
- [Di Agentic Module](../../../05-mcp)
- [How to Run Di Examples](../../../05-mcp)
  - [Wet you need first](../../../05-mcp)
- [Quick Start](../../../05-mcp)
  - [File Operations (Stdio)](../../../05-mcp)
  - [Supervisor Agent](../../../05-mcp)
    - [How to Run Di Demo](../../../05-mcp)
    - [How di Supervisor dey work](../../../05-mcp)
    - [Response Strategies](../../../05-mcp)
    - [How to Understand di Output](../../../05-mcp)
    - [Explanation of di Agentic Module Features](../../../05-mcp)
- [Key Concepts](../../../05-mcp)
- [Congrats!](../../../05-mcp)
  - [Wetin you go do next?](../../../05-mcp)

## Wet you go learn

You don build conversational AI, sabi prompts well, fit base responses for documents, and create agents wey get tools. But na custom tools you use build am for your application only. Wetin if you fit give your AI access to one kain standard ecosystem of tools wey anybody fit create and share? For this module, you go learn how to do just that with Model Context Protocol (MCP) plus LangChain4j agentic module. We go first show simple MCP file reader then show how e easy to join am inside advanced agentic workflows with Supervisor Agent pattern.

## Wetin be MCP?

Model Context Protocol (MCP) na exactly dat – e be standard way for AI applications to find and use outside tools. Instead make you write custom integration for every data source or service, you go connect to MCP servers wey show their abilities for one kain consistent format. Your AI agent fit then discover and use those tools automatically.

<img src="../../../translated_images/pcm/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Before MCP: Complex point-to-point integrations. After MCP: One protocol, endless possibilities.*

MCP solve one fundamental wahala for AI development: every integration na custom. You want access GitHub? Custom code. You want read files? Custom code. You want query database? Custom code. And none of these integration fit work with other AI applications.

MCP standardize am. MCP server dey expose tools with clear description and schemas. Any MCP client fit connect, find available tools, and use dem. Build once, use everywhere.

<img src="../../../translated_images/pcm/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol architecture - standardized tool discovery and execution*

## How MCP dey work

<img src="../../../translated_images/pcm/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*How MCP dey work under di hood — clients find tools, exchange JSON-RPC messages, and run operations through one transport layer.*

**Server-Client Architecture**

MCP dey use client-server model. Servers dey give tools - reading files, querying databases, calling APIs. Clients (na your AI application) go connect to servers and use their tools.

To use MCP with LangChain4j, add this Maven dependency:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Tool Discovery**

When your client connect MCP server, e go ask "Wetin tools you get?" Server go answer with list of available tools, each get description and parameter schemas. Your AI agent fit decide which tools to use based on user requests.

<img src="../../../translated_images/pcm/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI find available tools at startup — e don sabi wetin tools dey there and fit choose which one to use.*

**Transport Mechanisms**

MCP support different transport mechanism dem. This module show Stdio transport for local processes:

<img src="../../../translated_images/pcm/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP transport mechanisms: HTTP for remote servers, Stdio for local processes*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Na for local processes. Your app go spawn server as subprocess and communicate through standard input/output. Good for filesystem access or command-line tools.

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

<img src="../../../translated_images/pcm/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio transport dey waka — your app go spawn MCP server as child process and communicate through stdin/stdout pipes.*

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) and ask:
> - "How Stdio transport dey work and when I suppose use am instead of HTTP?"
> - "How LangChain4j dey manage lifecycle of spawned MCP server processes?"
> - "Wetin be the security risk for giving AI access to file system?"

## The Agentic Module

Even though MCP dey provide standardized tools, LangChain4j's **agentic module** dey provide one kind declarative way to build agents wey go control those tools. The `@Agent` annotation and `AgenticServices` make you fit define agent behavior with interfaces instead of imperative code.

For this module, you go explore **Supervisor Agent** pattern — advanced agentic AI way wey "supervisor" agent dey dynamically decide which sub-agents to call based on user requests. We go join both ideas by giving one of our sub-agents MCP-powered file access powers.

To use agentic module, add this Maven dependency:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **⚠️ Experimental:** The `langchain4j-agentic` module still **experimental** and fit change. The stable way to build AI assistant still na `langchain4j-core` with custom tools (Module 04).

## How to Run Di Examples

### Wet you need first

- Java 21+, Maven 3.9+
- Node.js 16+ and npm (for MCP servers)
- Environment variables dem set inside `.env` file (from root directory):
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (like Modules 01-04)

> **Note:** If you never set your environment variables yet, check [Module 00 - Quick Start](../00-quick-start/README.md) for how-to, or copy `.env.example` go `.env` for root and put your values.

## Quick Start

**If you dey use VS Code:** Just right-click demo file for Explorer and choose **"Run Java"**, or use launch configurations pick from Run and Debug panel (make sure you don add your token to `.env` file first).

**If you dey use Maven:** You fit also run from command line with examples below.

### File Operations (Stdio)

This one show local subprocess-based tools.

**✅ No prerequisite needed** - MCP server go spawn automatically.

**Using the Start Scripts (Recommended):**

Start scripts go automatically add environment variables from root `.env`:

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

**Using VS Code:** Right click `StdioTransportDemo.java` and select **"Run Java"** (make sure `.env` file well configured).

App go spawn filesystem MCP server automatically and read local file. See how subprocess management dey handled for you.

**Expected output:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor Agent

The **Supervisor Agent pattern** na **flexible** form of agentic AI. Supervisor dey use LLM to decide anyhow which agents dem go call based on user request. For next example, we combine MCP-powered file access with LLM agent to create supervised file read → report workflow.

For demo, `FileAgent` go read file with MCP filesystem tools, and `ReportAgent` go make structured report with executive summary (1 sentence), 3 main points, and recommendations. Supervisor go arrange this flow automatic:

<img src="../../../translated_images/pcm/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Supervisor dey use im LLM to decide which agents to call and in which order — no hardcoded routing needed.*

Dis na how the pipeline file-to-report flow go look like:

<img src="../../../translated_images/pcm/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent read file via MCP tools, then ReportAgent turn raw content to structured report.*

Every agent go store their output for **Agentic Scope** (shared memory), make downstream agents fit read previous result. Dis show how MCP tools fit joint properly inside agentic workflows — Supervisor no need sabi *how* files dey read, just know say `FileAgent` fit do am.

#### How to Run Di Demo

Start scripts go automatically load environment variables from root `.env`:

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

**Using VS Code:** Right click `SupervisorAgentDemo.java` and choose **"Run Java"** (make sure `.env` file configured).

#### How di Supervisor dey work

```java
// Step 1: FileAgent dey read files wit MCP tools
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Get MCP tools for file operations
        .build();

// Step 2: ReportAgent dey generate structured reports
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor dey manage the file → report workflow
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Return the final report
        .build();

// The Supervisor go decide which agents to call based on di request
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Response Strategies

When you set `SupervisorAgent`, you go talk how e go formulate final answer for user after sub-agents finish their work.

<img src="../../../translated_images/pcm/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Three ways how Supervisor fit take formulate final response — choose based on if you want last agent output, summary, or best scored option.*

Strategies wey dey:

| Strategy | Description |
|----------|-------------|
| **LAST** | Supervisor go return output from last sub-agent or tool wey e call. Use am if last agent for workflow na the one wey dem design to produce full final answer (like "Summary Agent" for research pipeline). |
| **SUMMARY** | Supervisor go use im own Language Model (LLM) to combine summary of all interaction and sub-agent outputs, then give that summary as final response. E make answer dey clean and combined for user. |
| **SCORED** | System go use internal LLM to score LAST response and SUMMARY of interaction against original user request, then return the one wey get higher score. |

See [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) for full implementation.

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) and ask:
> - "How Supervisor dey decide which agents to call?"
> - "What's di difference between Supervisor and Sequential workflow patterns?"
> - "How I fit customize Supervisor's planning behavior?"

#### How to Understand di Output

When you run the demo, you go see structured walkthrough how Supervisor dey arrange many agents. This na the meaning of every section:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**The header** dey introduce workflow concept: focused pipeline from file reading to report making.

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

**Workflow Diagram** dey show how data flow between agents. Every agent get specific role:
- **FileAgent** dey read files with MCP tools and store raw content for `fileContent`
- **ReportAgent** go use dat content and produce structured report for `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**User Request** dey show the task. Supervisor go parse am and decide to call FileAgent → ReportAgent.

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

**Supervisor Orchestration** dey show 2-step flow for action:
1. **FileAgent** read file via MCP and store content
2. **ReportAgent** receive content and make structured report

Supervisor na im decide all this **on im own** based on user request.

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

#### Explanation of di Agentic Module Features

This example dey show some advanced features of agentic module. Make we check Agentic Scope and Agent Listeners.

**Agentic Scope** na shared memory where agents store their results using `@Agent(outputKey="...")`. E allow:
- Later agents to access results from earlier agents
- Supervisor to create final summarized response
- You fit check wetin each agent produce

<img src="../../../translated_images/pcm/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope na like shared memory — FileAgent dey write `fileContent`, ReportAgent dey read am and write `report`, your code go read final output.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Raw file data from FileAgent
String report = scope.readState("report");            // Structured report from ReportAgent
```

**Agent Listeners** make monitoring and debugging agent execution easy. Step-by-step output you dey see for demo na from AgentListener wey dey hook into every agent call:

- **beforeAgentInvocation** - Dem go call am when Supervisor select agent, e dey let you see which agent dem choose and why
- **afterAgentInvocation** - Dem go call am when agent don finish, e dey show di result
- **inheritedBySubagents** - If true, di listener go monitor all agents for di hierarchy

<img src="../../../translated_images/pcm/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners dey hook join di execution lifecycle — dem dey monitor when agents start, finish, or when dem get error.*

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
        return true; // Make e spread go all sub-agents dem
    }
};
```
  
Beyond di Supervisor pattern, di `langchain4j-agentic` module get plenty strong workflow patterns and features:

<img src="../../../translated_images/pcm/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Five workflow patterns for how to manage agents — from simple order waka pipelines go reach human-in-the-loop approval workflows.*

| Pattern | Description | Use Case |
|---------|-------------|----------|
| **Sequential** | Make agents run one after the other, output go follow enter the next | Pipelines: research → analyze → report |
| **Parallel** | Make agents run at the same time | Independent tasks: weather + news + stocks |
| **Loop** | Run again and again till condition satisfy | Quality scoring: keep improve till score reach ≥ 0.8 |
| **Conditional** | Make decision based on condition | Classify → send to specialist agent |
| **Human-in-the-Loop** | Add human checkpoints | Approval workflows, content review |

## Key Concepts

Now wey you don explore MCP and di agentic module for use, make we summarize when you go use each approach.

<img src="../../../translated_images/pcm/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP dey create universal protocol ecosystem — any MCP-compatible server fit work with any MCP-compatible client, make e easy to share tools across different apps.*

**MCP** good when you want use existing tool ecosystem, build tools wey plenty apps fit share, join third-party services with standard protocols, or change tool implementation without to change code.

**The Agentic Module** dey best when you want declarative agent definitions with `@Agent` annotations, need workflow orchestration (sequential, loop, parallel), prefer interface-based agent design pass imperative code, or you dey join many agents wey dey share output through `outputKey`.

**The Supervisor Agent pattern** dey shine when workflow no too predictable before hand and you want make LLM decide, when you get many specialized agents wey need dynamic orchestration, when you dey build conversational systems wey route to different capabilities, or when you want flexible and adaptive agent behavior.

<img src="../../../translated_images/pcm/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*When to use custom @Tool methods vs MCP tools — custom tools good for app-specific logic with full type safety, MCP tools good for standard integrations wey fit work across apps.*

## Congratulations!

<img src="../../../translated_images/pcm/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*You don complete learning journey through all five modules — from basic chat go MCP-powered agentic systems.*

You don finish di LangChain4j for Beginners course. You don sabi:

- How to build conversational AI wey get memory (Module 01)
- Prompt engineering patterns for different tasks (Module 02)
- How to ground responses inside your documents with RAG (Module 03)
- How to create basic AI agents (assistants) with custom tools (Module 04)
- How to join standardized tools with LangChain4j MCP and Agentic modules (Module 05)

### Wetin Next?

After you don finish all modules, try check the [Testing Guide](../docs/TESTING.md) to see LangChain4j testing concepts for action.

**Official Resources:**  
- [LangChain4j Documentation](https://docs.langchain4j.dev/) - Complete guides and API reference  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Source code and examples  
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - Step-by-step tutorials for many kinds use cases  

Thank you for finishing this course!

---

**Navigation:** [← Previous: Module 04 - Tools](../04-tools/README.md) | [Back to Main](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Warning**:
Dis document don translate wit AI translation service wey dem dey call [Co-op Translator](https://github.com/Azure/co-op-translator). Even though we dey try make am correct, abeg remember say automated translation fit get some mistakes or no too correct. Di original document wey e dey for inside dia own language na di real source. If na serious info, e good make human professional translate am. We no go responsible for any misunderstanding or wrong sense wey fit come from using dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->