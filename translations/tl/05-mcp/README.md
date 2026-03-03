# Module 05: Model Context Protocol (MCP)

## Table of Contents

- [What You'll Learn](../../../05-mcp)
- [What is MCP?](../../../05-mcp)
- [How MCP Works](../../../05-mcp)
- [The Agentic Module](../../../05-mcp)
- [Running the Examples](../../../05-mcp)
  - [Prerequisites](../../../05-mcp)
- [Quick Start](../../../05-mcp)
  - [File Operations (Stdio)](../../../05-mcp)
  - [Supervisor Agent](../../../05-mcp)
    - [Running the Demo](../../../05-mcp)
    - [How the Supervisor Works](../../../05-mcp)
    - [Response Strategies](../../../05-mcp)
    - [Understanding the Output](../../../05-mcp)
    - [Explanation of Agentic Module Features](../../../05-mcp)
- [Key Concepts](../../../05-mcp)
- [Congratulations!](../../../05-mcp)
  - [What's Next?](../../../05-mcp)

## What You'll Learn

Nakabuo ka na ng conversational AI, napagmaster ang prompts, na-ground ang mga sagot sa mga dokumento, at nakalikha ng mga agent na may mga tools. Pero ang mga tools na iyon ay custom-built para sa iyong partikular na aplikasyon. Paano kaya kung maibibigay mo sa iyong AI ang access sa isang standardized ecosystem ng mga tools na maaaring likhain at ibahagi ng sinuman? Sa module na ito, matututuhan mo kung paano gawin iyon gamit ang Model Context Protocol (MCP) at ang agentic module ng LangChain4j. Ipapakita muna namin ang isang simpleng MCP file reader at pagkatapos ay ipapakita kung paano ito madaling mai-integrate sa advanced agentic workflows gamit ang Supervisor Agent pattern.

## What is MCP?

Ang Model Context Protocol (MCP) ay nagbibigay ng eksaktong iyon - isang pamantayan para sa mga AI application upang matuklasan at magamit ang mga external na tools. Sa halip na magsulat ng custom integrations para sa bawat data source o serbisyo, kumokonekta ka sa mga MCP server na nagpapakita ng kanilang kakayahan sa isang pare-parehong format. Ang iyong AI agent ay maaari nang awtomatikong matuklasan at magamit ang mga tools na ito.

Ipinapakita ng diagram sa ibaba ang pagkakaiba — kung walang MCP, bawat integration ay nangangailangan ng custom point-to-point wiring; gamit ang MCP, isang protocol lang ang nagkokonekta sa iyong app sa anumang tool:

<img src="../../../translated_images/tl/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Bago ang MCP: Masalimuot na point-to-point integrations. Pagkatapos ng MCP: Isang protocol, walang hanggang posibilidad.*

Nilulutas ng MCP ang isang pangunahing problema sa pag-develop ng AI: bawat integration ay custom. Gusto mo bang mag-access ng GitHub? Custom code. Gusto mong magbasa ng mga file? Custom code. Gusto mong mag-query ng database? Custom code. At wala sa mga integration na ito ang gumagana sa ibang AI application.

Pinapantay ng MCP ito. Isang MCP server ang nagpapakita ng mga tools na may malinaw na mga deskripsyon at schema. Maaaring kumonekta ang anumang MCP client, matuklasan ang mga available na tools, at magamit ito. Isang beses lang itayo, gamitin saan man.

Ipinapakita ng diagram sa ibaba ang arkitektura nito — isang MCP client (iyong AI application) ang kumokonekta sa maraming MCP servers, bawat isa ay nagpapakita ng sarili nilang set ng mga tools sa pamamagitan ng standard protocol:

<img src="../../../translated_images/tl/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Arkitektura ng Model Context Protocol - standardized tool discovery at execution*

## How MCP Works

Sa ilalim, gumagamit ang MCP ng layered architecture. Ang iyong Java application (ang MCP client) ay natutuklasan ang mga available na tools, nagse-send ng JSON-RPC requests sa pamamagitan ng transport layer (Stdio o HTTP), at ang MCP server ang nag-eexecute ng mga operasyon at nagbabalik ng resulta. Pinapaliwanag ng sumusunod na diagram bawat layer ng protocol na ito:

<img src="../../../translated_images/tl/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Paano gumagana ang MCP sa ilalim — natutuklasan ng mga client ang tools, nagpapalitan ng JSON-RPC messages, at nagpapatupad ng mga operasyon sa pamamagitan ng transport layer.*

**Server-Client Architecture**

Gumagamit ang MCP ng client-server model. Nagbibigay ng tools ang mga servers - pagbabasa ng files, pag-query ng mga databases, pagtawag sa mga APIs. Kumokonekta ang mga clients (iyong AI application) sa mga server at ginagamit ang kanilang mga tools.

Para gamitin ang MCP kasama ang LangChain4j, idagdag ang Maven dependency na ito:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Tool Discovery**

Kapag kumonekta ang iyong client sa isang MCP server, tinatanong nito, "Anong mga tools mayroon kayo?" Sumagot ang server ng listahan ng mga available na tools, bawat isa ay may kasamang deskripsyon at schema ng mga parameter. Ang iyong AI agent ay maaaring magpasya kung anong mga tools ang gagamitin base sa kahilingan ng user. Ipinapakita ng diagram sa ibaba ang handshake na ito — nagpapadala ang client ng `tools/list` na request at bumabalik ang server ng listahan ng mga available na tools nito kasama ang mga deskripsyon at schema ng mga parameter:

<img src="../../../translated_images/tl/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*Natutuklasan ng AI ang mga available na tools sa pagsisimula — alam na nito kung ano ang mga kakayahan at maaaring magpasya kung alin ang gagamitin.*

**Transport Mechanisms**

Sinusuportahan ng MCP ang iba't ibang mekanismo ng transportasyon. Ang dalawang opsyon ay Stdio (para sa lokal na subprocess communication) at Streamable HTTP (para sa mga remote server). Ipinapakita sa module na ito ang Stdio transport:

<img src="../../../translated_images/tl/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*Mga mekanismo ng transport ng MCP: HTTP para sa remote server, Stdio para sa lokal na proseso*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Para sa mga lokal na proseso. Ang iyong aplikasyon ay nagsisimula ng server bilang subprocess at nakikipagkomunika sa pamamagitan ng standard input/output. Kapaki-pakinabang para sa filesystem access o command-line tools.

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

Ang `@modelcontextprotocol/server-filesystem` server ay nagpapakita ng mga sumusunod na tools, lahat ay sandboxed sa mga directory na iyong itinatakda:

| Tool | Deskripsyon |
|------|-------------|
| `read_file` | Basahin ang nilalaman ng isang file |
| `read_multiple_files` | Basahin ang maramihang mga file sa isang tawag |
| `write_file` | Gumawa o i-overwrite ang isang file |
| `edit_file` | Gawin ang mga targeted na find-and-replace na pagbabago |
| `list_directory` | Ilahad ang mga file at directory sa isang path |
| `search_files` | Rekursibong maghanap ng mga file na tumutugma sa pattern |
| `get_file_info` | Kunin ang metadata ng file (laki, timestamps, permissions) |
| `create_directory` | Gumawa ng directory (kasama ang parent directories) |
| `move_file` | Ilipat o palitan ang pangalan ng isang file o directory |

Ipinapakita ng sumusunod na diagram kung paano gumagana ang Stdio transport sa runtime — ang iyong Java application ay nagsisimula ng MCP server bilang child process at nakikipag-ugnayan ito sa pamamagitan ng stdin/stdout pipes, walang network o HTTP na kasangkot:

<img src="../../../translated_images/tl/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio transport sa aksyon — ang application mo ay nagsisimula ng MCP server bilang child process at nakikipag-ugnayan sa pamamagitan ng stdin/stdout pipes.*

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) at itanong:
> - "Paano gumagana ang Stdio transport at kailan ko ito gagamitin kumpara sa HTTP?"
> - "Paano pinamamahalaan ng LangChain4j ang lifecycle ng mga spawned MCP server processes?"
> - "Ano ang mga security implications ng pagbibigay ng AI access sa file system?"

## The Agentic Module

Habang nagbibigay ang MCP ng standardized tools, nagbibigay ang **agentic module** ng LangChain4j ng deklaratibong paraan upang bumuo ng mga agent na nag-o-orchestrate ng mga tools na iyon. Ang `@Agent` annotation at `AgenticServices` ay nagpapahintulot sa iyo na tukuyin ang ugali ng agent sa pamamagitan ng mga interface sa halip na imperative code.

Sa module na ito, susuriin mo ang **Supervisor Agent** pattern — isang advanced na agentic AI na paraan kung saan ang "supervisor" agent ay dynamic na nagpapasya kung anong mga sub-agent ang tatawagin base sa kahilingan ng user. Pagsasamahin namin ang parehong konsepto sa pagbibigay sa isa sa aming sub-agent ng MCP-powered file access na kakayahan.

Para gamitin ang agentic module, idagdag ang Maven dependency na ito:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Tandaan:** Gumagamit ang `langchain4j-agentic` module ng hiwalay na version property (`langchain4j.mcp.version`) dahil inilalabas ito sa ibang iskedyul kumpara sa core LangChain4j libraries.

> **⚠️ Experimental:** Ang `langchain4j-agentic` module ay **eksperimentyal** at maaaring magbago. Ang matatag na paraan upang bumuo ng AI assistants ay nananatiling `langchain4j-core` na may custom tools (Module 04).

## Running the Examples

### Prerequisites

- Nakumpleto ang [Module 04 - Tools](../04-tools/README.md) (ang module na ito ay nakabatay sa konsepto ng custom tools at inihahambing ito sa MCP tools)
- `.env` file sa root directory na may Azure credentials (nalikha ng `azd up` sa Module 01)
- Java 21+, Maven 3.9+
- Node.js 16+ at npm (para sa MCP servers)

> **Tandaan:** Kung hindi mo pa naisasetup ang iyong environment variables, tingnan ang [Module 01 - Introduction](../01-introduction/README.md) para sa mga tagubilin sa deployment (`azd up` ang awtomatikong lumilikha ng `.env` file), o kopyahin ang `.env.example` papuntang `.env` sa root directory at punan ang iyong mga halaga.

## Quick Start

**Gamit ang VS Code:** I-right-click lang ang anumang demo file sa Explorer at piliin ang **"Run Java"**, o gamitin ang launch configurations mula sa Run and Debug panel (siguraduhin munang na-configure ang iyong `.env` file na may Azure credentials).

**Gamit ang Maven:** Bilang alternatibo, maaari kang magpatakbo mula sa command line gamit ang mga halimbawa sa ibaba.

### File Operations (Stdio)

Ipinapakita nito ang mga lokal na subprocess-based na tools.

**✅ Walang kailangan na prerequisites** - ang MCP server ay awtomatikong pinapagana.

**Gamit ang Start Scripts (Inirerekomenda):**

Awtomatikong niloload ng start scripts ang environment variables mula sa root `.env` file:

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

**Gamit ang VS Code:** I-right-click ang `StdioTransportDemo.java` at piliin ang **"Run Java"** (siguraduhing nakaayos ang iyong `.env` file).

Awtomatikong pinapagana ng application ang filesystem MCP server at nagbabasa ng isang lokal na file. Pansinin kung paano pinamamahalaan ang subprocess para sa iyo.

**Inaasahang output:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor Agent

Ang **Supervisor Agent pattern** ay isang **flexible** na anyo ng agentic AI. Gumagamit ang isang Supervisor ng LLM upang autonomously magpasya kung aling mga agent ang tatawagin base sa kahilingan ng user. Sa susunod na halimbawa, pinagsasama namin ang MCP-powered file access sa isang LLM agent upang lumikha ng supervised file read → report workflow.

Sa demo, nagbabasa ang `FileAgent` ng isang file gamit ang mga MCP filesystem tools, at gumagawa ang `ReportAgent` ng isang structured na report na may executive summary (1 pangungusap), 3 mahahalagang punto, at mga rekomendasyon. Ino-orchestrate ng Supervisor ang daloy na ito nang awtomatiko:

<img src="../../../translated_images/tl/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Gumagamit ang Supervisor ng sarili nitong LLM upang magpasya kung aling mga agent ang tatawagin at sa anong pagkakasunod-sunod — hindi kailangan ng hardcoded routing.*

Ganito ang eksaktong workflow para sa aming file-to-report pipeline:

<img src="../../../translated_images/tl/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*Binabasa ng FileAgent ang file gamit ang MCP tools, pagkatapos ay ginagawang structured report ng ReportAgent ang raw na nilalaman.*

Itinatago ng bawat agent ang output nito sa **Agentic Scope** (shared memory), na nagpapahintulot sa mga downstream na agent na ma-access ang mga naunang resulta. Ipinapakita nito kung paano seamless na nag-iintegrate ang MCP tools sa agentic workflows — hindi kailangang malaman ng Supervisor kung *paano* binabasa ang mga file, ang kailangan lang malaman ay magagawa ito ng `FileAgent`.

#### Running the Demo

Awtomatikong niloload ng start scripts ang environment variables mula sa root `.env` file:

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

**Gamit ang VS Code:** I-right-click ang `SupervisorAgentDemo.java` at piliin ang **"Run Java"** (siguraduhing nakaayos ang iyong `.env` file).

#### How the Supervisor Works

Bago bumuo ng mga agent, kailangan mong kumonekta ang MCP transport sa isang client at i-wrap ito bilang `ToolProvider`. Ganito nagiging available sa mga agent ang mga tools ng MCP server:

```java
// Gumawa ng MCP client mula sa transport
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Balutin ang client bilang isang ToolProvider — ito ay nag-uugnay ng MCP tools sa LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Ngayon, maaari mong i-inject ang `mcpToolProvider` sa anumang agent na nangangailangan ng MCP tools:

```java
// Hakbang 1: Binabasa ng FileAgent ang mga file gamit ang mga MCP tool
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // May mga MCP tool para sa mga operasyon ng file
        .build();

// Hakbang 2: Gumagawa ang ReportAgent ng mga istrakturadong ulat
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Inaayos ng Supervisor ang daloy ng file → ulat
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Ibalik ang pangwakas na ulat
        .build();

// Pinipili ng Supervisor kung aling mga ahente ang tatawagin ayon sa kahilingan
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Response Strategies

Kapag kino-configure mo ang `SupervisorAgent`, tinutukoy mo kung paano ito magfo-formulate ng huling sagot sa user pagkatapos magawa ng mga sub-agent ang kanilang mga gawain. Ipinapakita ng diagram sa ibaba ang tatlong available na strategies — ang LAST ay direktang ibinabalik ang huling output ng agent, ang SUMMARY ay nagsasynthesise ng lahat ng output gamit ang isang LLM, at ang SCORED ay pinipili ang mas mataas ang score sa orihinal na kahilingan:

<img src="../../../translated_images/tl/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Tatlong strategy kung paano ginagawa ng Supervisor ang huling sagot — pumili base sa gusto mong output: huling agent output, synthesized summary, o ang pinakamatagumpay na pagpipilian.*

Ang mga available na strategy ay:

| Strategy | Deskripsyon |
|----------|-------------|
| **LAST** | Ibinabalik ng supervisor ang output ng huling sub-agent o tool na tinawag. Kapaki-pakinabang ito kapag ang huling agent sa workflow ay partikular na dinisenyo para magbigay ng kompletong, panghuling sagot (halimbawa, isang "Summary Agent" sa isang research pipeline). |
| **SUMMARY** | Ginagamit ng supervisor ang sarili nitong internal Language Model (LLM) upang gumawa ng synthesized summary ng buong interaksyon at lahat ng sub-agent outputs, pagkatapos ay ibinabalik ang summary na iyon bilang panghuling sagot. Nagbibigay ito ng malinis, pinag-isang sagot sa user. |
| **SCORED** | Ginagamit ng sistema ang internal LLM upang i-score ang parehong LAST response at ang SUMMARY ng interaksyon laban sa orihinal na kahilingan ng user, at ibinabalik ang output na may mas mataas na score. |
Tingnan ang [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) para sa kumpletong implementasyon.

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) at itanong:
> - "Paano pinipili ng Supervisor kung aling mga ahente ang tatawagin?"
> - "Ano ang pinagkaiba ng mga pattern ng Supervisor at Sequential workflow?"
> - "Paano ko mae-customize ang planning behavior ng Supervisor?"

#### Pag-unawa sa Output

Kapag pinatakbo mo ang demo, makikita mo ang isang istrukturadong walkthrough kung paano inoorganisa ng Supervisor ang maraming ahente. Ganito ang kahulugan ng bawat bahagi:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Ang header** ay nagpapakilala ng konsepto ng workflow: isang nakatutok na pipeline mula sa pagbasa ng file hanggang sa paggawa ng ulat.

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

**Workflow Diagram** ay nagpapakita ng daloy ng datos sa pagitan ng mga ahente. Bawat ahente ay may espesipikong papel:
- **FileAgent** ang nagbabasa ng mga file gamit ang mga MCP tools at iniimbak ang raw content sa `fileContent`
- **ReportAgent** ang kumokonsumo ng content na iyon at gumagawa ng istrakturadong ulat sa `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**User Request** ay nagpapakita ng gawain. Pinoproseso ito ng Supervisor at nagpasya na tawagin ang FileAgent → ReportAgent.

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

**Supervisor Orchestration** ay nagpapakita ng 2-hakbang na daloy sa aksyon:
1. **FileAgent** ang nagbabasa ng file sa pamamagitan ng MCP at iniimbak ang content
2. **ReportAgent** ay tumatanggap ng content at gumagawa ng istrakturadong ulat

Ginawa ng Supervisor ang mga desisyong ito **nang mag-isa** batay sa kahilingan ng user.

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

#### Paliwanag ng Mga Tampok ng Agentic Module

Ipinapakita ng halimbawa ang ilang advanced na tampok ng agentic module. Tingnan natin nang mas malapitan ang Agentic Scope at Agent Listeners.

**Agentic Scope** ay nagpapakita ng shared memory kung saan iniimbak ng mga ahente ang kanilang mga resulta gamit ang `@Agent(outputKey="...")`. Pinapayagan nito na:
- Makakuha ang mga sumunod na ahente ng outputs ng mga naunang ahente
- Ang Supervisor ay makabuo ng panghuling tugon
- Mabansagan mo kung ano ang nilikha ng bawat ahente

Ipinapakita ng diagram sa ibaba kung paano gumagana ang Agentic Scope bilang shared memory sa file-to-report workflow — sinusulat ng FileAgent ang output nito sa ilalim ng susi na `fileContent`, binabasa ito ng ReportAgent at sinusulat ang sarili nitong output sa ilalim ng `report`:

<img src="../../../translated_images/tl/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Gumagana ang Agentic Scope bilang shared memory — sinusulat ng FileAgent ang `fileContent`, binabasa ito ng ReportAgent at sinusulat ang `report`, at binabasa ng iyong code ang panghuling resulta.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Hilaw na datos ng file mula sa FileAgent
String report = scope.readState("report");            // Istrakturadong ulat mula sa ReportAgent
```

**Agent Listeners** ay nagpapahintulot ng pagmamanman at pag-debug ng pagpapatupad ng ahente. Ang hakbang-hakbang na output na nakikita mo sa demo ay nagmumula sa isang AgentListener na nakakabit sa bawat pagtawag ng ahente:
- **beforeAgentInvocation** - Tinatawag kapag pinili ng Supervisor ang isang ahente, para makita mo kung aling ahente ang pinili at bakit
- **afterAgentInvocation** - Tinatawag kapag natapos ang isang ahente, na nagpapakita ng resulta nito
- **inheritedBySubagents** - Kapag true, minomonitor ng listener ang lahat ng ahente sa hierarchy

Ipinapakita ng sumusunod na diagram ang buong lifecycle ng Agent Listener, kabilang kung paano hinahandle ng `onError` ang mga pagkakamali habang nagpapatakbo ang ahente:

<img src="../../../translated_images/tl/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Nakakabit ang Agent Listeners sa lifecycle ng pagpapatupad — mino-monitor kung kailan nagsisimula, natatapos, o nakakaranas ng error ang mga ahente.*

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
        return true; // Ipasa sa lahat ng sub-ahente
    }
};
```

Bukod sa Supervisor pattern, nagbibigay ang `langchain4j-agentic` module ng ilang makapangyarihang mga workflow pattern. Ipinapakita ng diagram sa ibaba ang lahat ng lima — mula sa mga simpleng sequential pipelines hanggang sa human-in-the-loop approval workflows:

<img src="../../../translated_images/tl/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Limang workflow pattern para sa pag-oorganisa ng mga ahente — mula sa simpleng sequential pipelines hanggang sa human-in-the-loop approval workflows.*

| Pattern | Paglalarawan | Gamit |
|---------|--------------|-------|
| **Sequential** | Patakbuhin ang mga ahente nang sunod-sunod, ang output ay dumadaloy sa susunod | Pipeline: pananaliksik → pagsuri → ulat |
| **Parallel** | Patakbuhin ang mga ahente nang sabay-sabay | Mga independenteng gawain: panahon + balita + stocks |
| **Loop** | Ulitin hanggang matugunan ang kondisyon | Pag-score ng kalidad: ayusin hanggang score ≥ 0.8 |
| **Conditional** | I-route ayon sa mga kondisyon | Klasekasyon → i-route sa espesyalistang ahente |
| **Human-in-the-Loop** | Magdagdag ng mga human checkpoint | Approval workflows, pagsusuri ng nilalaman |

## Mga Pangunahing Konsepto

Ngayon na nalaman mo ang MCP at ang agentic module sa aksyon, balikan natin kung kailan gagamitin ang bawat isa.

Isa sa pinakamalaking kalamangan ng MCP ay ang lumalaking ecosystem nito. Ipinapakita ng diagram sa ibaba kung paano nakakonekta ang isang unibersal na protocol ang iyong AI application sa maraming MCP servers — mula sa filesystem at database access hanggang sa GitHub, email, web scraping, at iba pa:

<img src="../../../translated_images/tl/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*Lumilikha ang MCP ng unibersal na ecosystem ng protocol — anumang MCP-compatible na server ay gumagana sa anumang MCP-compatible na client, nagpapahintulot ng sharing ng tools sa iba't ibang aplikasyon.*

**Ang MCP** ay perpekto kapag nais mong gamitin ang umiiral na ecosystem ng tools, gumawa ng mga tool na maaaring paggamitan ng maraming aplikasyon, isama ang third-party na serbisyo gamit ang standard na mga protocol, o palitan ang implementasyon ng tool nang hindi binabago ang code.

**Ang Agentic Module** ay pinakamahusay kapag gusto mong magkaroon ng deklaratibong mga ahente gamit ang `@Agent` annotations, kailangan ng workflow orchestration (sequential, loop, parallel), mas gusto ang interface-based na disenyo ng ahente kaysa imperative na code, o pinagsasama ang maraming ahente na nagbabahagi ng outputs gamit ang `outputKey`.

**Ang Supervisor Agent pattern** ay namumukod-tangi kapag hindi maagang natutukoy ang workflow at gusto mong ang LLM ang magdesisyon, kapag may maraming espesyalisadong ahente na nangangailangan ng dynamic na orchestration, sa pagbuo ng mga conversational system na nagro-route sa iba't ibang kakayahan, o kapag gusto mo ng pinaka-flexible at adaptive na behavior ng ahente.

Para matulungan kang pumili sa pagitan ng custom na `@Tool` methods mula sa Module 04 at MCP tools mula sa module na ito, ipinapakita ng sumusunod na paghahambing ang mga pangunahing trade-off — nagbibigay ang custom tools ng mahigpit na coupling at full type safety para sa app-specific na logic, habang nag-aalok ang MCP tools ng standardized, reusable na integrations:

<img src="../../../translated_images/tl/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Kailan gagamit ng custom @Tool methods laban MCP tools — custom tools para sa app-specific logic na may full type safety, MCP tools para sa standardized integrations na gumagana sa iba't ibang aplikasyon.*

## Congratulations!

Natapos mo na ang lahat ng limang modules ng LangChain4j para sa Beginners na kurso! Narito ang buong learning journey na natapos mo — mula sa basic chat hanggang sa MCP-powered agentic systems:

<img src="../../../translated_images/tl/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Ang iyong learning journey sa lahat ng limang modules — mula sa basic chat hanggang MCP-powered agentic systems.*

Natapos mo ang LangChain4j para sa Beginners na kurso. Natutunan mo:

- Paano bumuo ng conversational AI na may memory (Module 01)
- Mga pattern ng prompt engineering para sa iba't ibang gawain (Module 02)
- Pagsuporta ng mga sagot sa iyong mga dokumento gamit ang RAG (Module 03)
- Paglikha ng mga basic AI agents (assistants) gamit ang custom tools (Module 04)
- Pagsasama ng standardized tools gamit ang LangChain4j MCP at Agentic modules (Module 05)

### Ano ang Susunod?

Pagkatapos matapos ang mga module, tuklasin ang [Testing Guide](../docs/TESTING.md) para makita ang mga konsepto ng LangChain4j testing sa aksyon.

**Opisyal na Mga Sanggunian:**
- [LangChain4j Documentation](https://docs.langchain4j.dev/) - Komprehensibong mga gabay at API reference
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Source code at mga halimbawa
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - Mga hakbang-hakbang na tutorial para sa iba't ibang kaso ng paggamit

Maraming salamat sa pagtapos ng kurso na ito!

---

**Navigation:** [← Nakaraan: Module 04 - Tools](../04-tools/README.md) | [Bumalik sa Pangunahing Pahina](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Paalala**:  
Ang dokumentong ito ay isinalin gamit ang serbisyong AI na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagama't nagsusumikap kami para sa katumpakan, pakatandaan na ang awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o hindi pagkakatugma. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na pangunahing sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasaling-tao. Hindi kami mananagot sa anumang hindi pagkakaintindihan o maling interpretasyon na nagmumula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->