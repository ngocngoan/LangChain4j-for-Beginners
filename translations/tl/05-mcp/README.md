# Module 05: Model Context Protocol (MCP)

## Table of Contents

- [Video Walkthrough](../../../05-mcp)
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
    - [How FileAgent Discovers MCP Tools at Runtime](../../../05-mcp)
    - [Response Strategies](../../../05-mcp)
    - [Understanding the Output](../../../05-mcp)
    - [Explanation of Agentic Module Features](../../../05-mcp)
- [Key Concepts](../../../05-mcp)
- [Congratulations!](../../../05-mcp)
  - [What's Next?](../../../05-mcp)

## Video Walkthrough

Panoorin ang live session na ito na nagpapaliwanag kung paano magsimula sa module na ito:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="AI Agents with Tools and MCP - Live Session" width="800"/></a>

## What You'll Learn

Nakabuo ka na ng conversational AI, natutunan ang mga prompt, nilagyan ng batayan ang mga sagot mula sa mga dokumento, at gumawa ng mga agent na may mga tool. Ngunit lahat ng mga tool na iyon ay custom-built para sa iyong partikular na aplikasyon. Paano kung mabibigyan mo ang iyong AI ng access sa isang standardized na ecosystem ng mga tool na maaaring malikha at maibahagi ng sinuman? Sa module na ito, matututunan mo kung paano gawin iyon gamit ang Model Context Protocol (MCP) at agentic module ng LangChain4j. Unang ipapakita namin ang isang simpleng MCP file reader at pagkatapos ay ipapakita kung paano ito madaling nae-integrate sa mga advanced na agentic workflows gamit ang Supervisor Agent pattern.

## What is MCP?

Ang Model Context Protocol (MCP) ay nagbibigay ng eksaktong iyon - isang standard na paraan para sa mga AI application na matuklasan at gamitin ang mga external na tool. Sa halip na magsulat ng custom integration para sa bawat data source o serbisyo, kumokonekta ka sa mga MCP server na nagpapakita ng kanilang mga kakayahan sa isang consistent na format. Maaari nang matuklasan at gamitin ng iyong AI agent ang mga tool na ito nang awtomatiko.

Ipinapakita ng diagram sa ibaba ang kaibahan — kung walang MCP, bawat integration ay nangangailangan ng custom point-to-point na wiring; gamit ang MCP, isang protocol lang ang nagkokonekta sa app mo sa anumang tool:

<img src="../../../translated_images/tl/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Bago ang MCP: Kumplikadong point-to-point na integrations. Pagkatapos ng MCP: Isang protocol, walang katapusang posibilidad.*

Nilulutas ng MCP ang isang pangunahing problema sa pagbuo ng AI: bawat integration ay custom. Gusto mong i-access ang GitHub? Custom code. Gusto mong magbasa ng mga file? Custom code. Gusto mong mag-query ng database? Custom code. At wala sa mga integration na ito ang gumagana sa ibang AI application.

Sine-standardize ito ng MCP. Nag-eexpose ang isang MCP server ng mga tool na may malinaw na paglalarawan at mga schema. Anumang MCP client ay pwedeng kumonekta, matuklasan ang mga magagamit na tool, at gamitin ang mga ito. Build once, use everywhere.

Ipinapakita ng diagram sa ibaba ang arkitekturang ito — isang MCP client (ang iyong AI application) ay kumokonekta sa maraming MCP server, na ang bawat isa ay nag-eexpose ng sariling hanay ng mga tool sa pamamagitan ng standard protocol:

<img src="../../../translated_images/tl/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol na arkitektura - standardized na pagtuklas at pag-execute ng mga tool*

## How MCP Works

Sa ilalim, gumagamit ang MCP ng isang layered architecture. Natutuklasan ng iyong Java application (ang MCP client) ang mga available na tool, nagpapadala ng mga JSON-RPC request sa pamamagitan ng transport layer (Stdio o HTTP), at ang MCP server ang nagpapatupad ng mga operasyon at nagbabalik ng resulta. Ang sumusunod na diagram ay nagpapakita ng bawat layer ng protocol na ito:

<img src="../../../translated_images/tl/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Paano gumagana ang MCP sa ilalim — natutuklasan ng mga client ang mga tool, nagpapalitan ng mga JSON-RPC na mensahe, at nagpapatupad ng mga operasyon sa pamamagitan ng transport layer.*

**Server-Client Architecture**

Gumagamit ang MCP ng client-server model. Nagbibigay ang mga server ng mga tool - pagbabasa ng mga file, pag-query ng database, pagtawag ng mga API. Kumokonekta ang mga client (ang iyong AI application) sa mga server at ginagamit ang kanilang mga tool.

Para magamit ang MCP sa LangChain4j, idagdag ang Maven dependency na ito:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Tool Discovery**

Kapag kumokonekta ang iyong client sa isang MCP server, nagtatanong ito ng "Anong mga tool ang meron kayo?" Sumasagot ang server ng listahan ng mga available na tool, bawat isa ay may mga paglalarawan at parameter schema. Maaari nang magpasya ang iyong AI agent kung alin sa mga tool ang gagamitin base sa hinihingi ng user. Ipinapakita ng diagram sa ibaba ang handshake na ito — nagpapadala ang client ng `tools/list` request at ibinabalik ng server ang mga magagamit nitong tool na may paglalarawan at parameter schema:

<img src="../../../translated_images/tl/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*Natutuklasan ng AI ang mga magagamit na tool sa pagsisimula — alam na nito ang mga kakayahan na available at maaaring pumili kung alin ang gagamitin.*

**Transport Mechanisms**

Sinusuportahan ng MCP ang iba't ibang mekanismo ng transport. Ang dalawang opsyon ay Stdio (para sa lokal na subprocess communication) at Streamable HTTP (para sa remote server). Ipinapakita ng module na ito ang Stdio transport:

<img src="../../../translated_images/tl/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*Mekanismo ng pag-transport ng MCP: HTTP para sa remote server, Stdio para sa lokal na proseso*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Para sa mga lokal na proseso. Ang iyong aplikasyon ay nagsisimula ng server bilang subprocess at nakikipag-usap sa pamamagitan ng standard input/output. Magagamit ito para sa filesystem access o mga command-line tool.

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

Ipinapakita ng server na `@modelcontextprotocol/server-filesystem` ang mga sumusunod na tool, lahat ay naka-sandbox sa mga direktoyo na iyong itinalaga:

| Tool | Description |
|------|-------------|
| `read_file` | Basahin ang nilalaman ng isang file |
| `read_multiple_files` | Basahin ang maraming file sa iisang tawag |
| `write_file` | Gumawa o i-overwrite ang isang file |
| `edit_file` | Gumawa ng target na find-and-replace na edit |
| `list_directory` | Ilan ang mga file at direktoryo sa isang path |
| `search_files` | Maghanap ng files nang recursive na tugma sa pattern |
| `get_file_info` | Kumuha ng metadata ng file (laki, timestamps, permissions) |
| `create_directory` | Gumawa ng direktoryo (kasama ang mga parent direktoryo) |
| `move_file` | Ilipat o palitan ang pangalan ng file o direktoryo |

Ipinapakita ng sumusunod na diagram kung paano gumagana ang Stdio transport sa runtime — ang iyong Java application ay nagsisimula ng MCP server bilang child process at nag-uusap sila sa pamamagitan ng stdin/stdout pipes, walang paggamit ng network o HTTP:

<img src="../../../translated_images/tl/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio transport sa aksyon — ang iyong aplikasyon ay nagsisimula ng MCP server bilang child process at nakikipag-ugnayan sa pamamagitan ng stdin/stdout pipes.*

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) at itanong:
> - "Paano gumagana ang Stdio transport at kailan dapat ko itong gamitin kumpara sa HTTP?"
> - "Paano pinamamahalaan ng LangChain4j ang lifecycle ng mga spawned MCP server process?"
> - "Ano ang mga security na implikasyon ng pagbibigay akses ng AI sa file system?"

## The Agentic Module

Habang nagbibigay ang MCP ng standardized na mga tool, nagbibigay naman ang LangChain4j's **agentic module** ng isang declarative na paraan para bumuo ng mga agent na nag-o-orchestrate ng mga tool na iyon. Ang `@Agent` annotation at `AgenticServices` ay nagpapahintulot sa iyo na magdeklara ng pag-uugali ng agent sa pamamagitan ng mga interface imbes na imperative na code.

Sa module na ito, sisiyasatin mo ang **Supervisor Agent** pattern — isang advanced na agentic AI approach kung saan ang isang "supervisor" agent ay dinamiko na nagpapasya kung alin sa mga sub-agent ang tatawagin base sa hinihingi ng user. Pagsasamahin natin ang dalawang konsepto na ito sa pagbibigay sa isa sa ating mga sub-agent ng mga MCP-powered na kakayahan sa pagbasa ng file.

Para magamit ang agentic module, idagdag ang Maven dependency na ito:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Tandaan:** Ang `langchain4j-agentic` module ay gumagamit ng hiwalay na version property (`langchain4j.mcp.version`) dahil inilalabas ito sa ibang iskedyul kumpara sa core LangChain4j libraries.

> **⚠️ Experimental:** Ang `langchain4j-agentic` module ay **eksperimental** at maaaring magbago. Ang matatag na paraan ng paggawa ng AI assistant ay nananatiling `langchain4j-core` na may custom tool (Module 04).

## Running the Examples

### Prerequisites

- Nakumpleto ang [Module 04 - Tools](../04-tools/README.md) (ang module na ito ay nakabase sa mga konsepto ng custom tool at ihinahambing ang mga ito sa mga tool ng MCP)
- `.env` file sa root directory na may Azure credentials (ginawa ng `azd up` sa Module 01)
- Java 21+, Maven 3.9+
- Node.js 16+ at npm (para sa mga MCP server)

> **Tandaan:** Kung hindi mo pa naitatakda ang iyong environment variables, tingnan ang [Module 01 - Introduction](../01-introduction/README.md) para sa mga tagubilin sa deployment (`azd up` awtomatikong lumilikha ng `.env` file), o kopyahin ang `.env.example` papuntang `.env` sa root directory at punan ang iyong mga values.

## Quick Start

**Gamit ang VS Code:** I-right click lang ang kahit anong demo file sa Explorer at piliin ang **"Run Java"**, o gamitin ang launch configurations mula sa Run and Debug panel (siguraduhing naka-configure ang iyong `.env` file na may Azure credentials muna).

**Gamit ang Maven:** Bilang alternatibo, maaari kang magpatakbo mula sa command line gamit ang mga halimbawa sa ibaba.

### File Operations (Stdio)

Ipinapakita nito ang mga local subprocess-based na tool.

**✅ Walang kinakailangang prerequisites** - ang MCP server ay awtomatikong nagsisimula.

**Gamit ang Start Scripts (Inirerekomenda):**

Awtomatikong niloload ng mga start script ang mga environment variable mula sa root `.env` file:

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

**Gamit ang VS Code:** I-right click ang `StdioTransportDemo.java` at piliin ang **"Run Java"** (siguraduhing naka-configure ang iyong `.env` file).

Ang application ay awtomatikong nag-spawn ng MCP server para sa filesystem at nagbabasa ng lokal na file. Pansinin kung paano pinangangasiwaan para sa iyo ang subprocess management.

**Inaasahang output:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor Agent

Ang **Supervisor Agent pattern** ay isang **flexible** na anyo ng agentic AI. Isang Supervisor ang gumagamit ng LLM upang autonomously na magpasya kung alin sa mga agent ang tatawagin batay sa hiling ng user. Sa susunod na halimbawa, pinagsama natin ang MCP-powered na file access sa LLM agent para gumawa ng supervised na file read → report workflow.

Sa demo, nagbabasa ang `FileAgent` ng file gamit ang MCP filesystem tools, at gumagawa ang `ReportAgent` ng structured report na may executive summary (1 pangungusap), 3 pangunahing punto, at mga rekomendasyon. Inaayos ng Supervisor ang daloy na ito nang awtomatiko:

<img src="../../../translated_images/tl/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Ginagamit ng Supervisor ang LLM nito para magpasya kung alin sa mga agent ang tatawagin at sa anong pagkakasunod-sunod — walang kailangang hardcoded routing.*

Ganito ang magiging konkretong daloy para sa ating file-to-report pipeline:

<img src="../../../translated_images/tl/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*Binabasa ng FileAgent ang file sa pamamagitan ng MCP tools, pagkatapos ay tinatransform ng ReportAgent ang raw na nilalaman sa isang structured report.*

Ipinapakita ng sumusunod na sequence diagram ang buong Supervisor orchestration — mula sa pag-spawn ng MCP server, sa autonomous na pagpili ng agent ng Supervisor, hanggang sa mga tawag sa tool sa pamamagitan ng stdio at ang panghuling ulat:

<img src="../../../translated_images/tl/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*Autonomously na tinatawag ng Supervisor ang FileAgent (na tumatawag sa MCP server gamit ang stdio para basahin ang file), pagkatapos ay tinatawag ang ReportAgent para gumawa ng structured report — bawat agent ay nag-iimbak ng output sa shared Agentic Scope.*

Bawat agent ay nag-iimbak ng output nito sa **Agentic Scope** (shared memory), na nagpapahintulot sa mga downstream na agent na ma-access ang mga naunang resulta. Ipinapakita nito kung paano seamless na nakakapag-integrate ang mga MCP tool sa agentic workflow — hindi kailangang malaman ng Supervisor *kung paano* binabasa ang mga file, sapat na na kayang gawin ito ng `FileAgent`.

#### Running the Demo

Awtomatikong niloload ng mga start script ang mga environment variable mula sa root `.env` file:

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

**Gamit ang VS Code:** I-right click ang `SupervisorAgentDemo.java` at piliin ang **"Run Java"** (siguraduhing naka-configure ang iyong `.env` file).

#### How the Supervisor Works

Bago gumawa ng mga agent, kailangan mong ikonekta ang MCP transport sa isang client at i-wrap ito bilang `ToolProvider`. Ganito nagiging available sa iyong mga agent ang mga tool ng MCP server:

```java
// Gumawa ng MCP client mula sa transport
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Balutin ang client bilang isang ToolProvider — pinagsasama nito ang mga MCP tool sa LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Ngayon, maaari mong i-inject ang `mcpToolProvider` sa anumang agent na nangangailangan ng MCP tools:

```java
// Hakbang 1: Binabasa ng FileAgent ang mga file gamit ang mga MCP tool
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Mayroong MCP tool para sa mga operasyon ng file
        .build();

// Hakbang 2: Nagtatala ng structured na mga ulat ang ReportAgent
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Pinangangasiwaan ng Supervisor ang daloy ng file → ulat
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Ibalik ang huling ulat
        .build();

// Pinipili ng Supervisor kung aling mga ahente ang tatawagin base sa kahilingan
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### How FileAgent Discovers MCP Tools at Runtime

Maaaring nagtatanong ka: **paano nalalaman ng `FileAgent` kung paano gagamitin ang npm filesystem tools?** Ang sagot ay hindi nito kailangang malaman — ang **LLM** mismo ang naghahanap sa runtime gamit ang mga tool schema.
Ang `FileAgent` interface ay isang **prompt definition** lamang. Wala itong hardcoded na kaalaman tungkol sa `read_file`, `list_directory`, o anumang iba pang MCP tool. Ganito ang nangyayari mula simula hanggang dulo:

1. **Nag-spawn ang Server:** Pinapalabas ng `StdioMcpTransport` ang `@modelcontextprotocol/server-filesystem` npm package bilang isang child process
2. **Pagtuklas ng Tool:** Nagpapadala ang `McpClient` ng `tools/list` JSON-RPC request sa server, na sumasagot ng mga pangalan ng tool, deskripsyon, at parameter schemas (halimbawa, `read_file` — *"Basahin ang buong nilalaman ng isang file"* — `{ path: string }`)
3. **Schema injection:** Binabalot ng `McpToolProvider` ang mga natuklasang schemas na ito at ginagawa silang available sa LangChain4j
4. **Desisyon ng LLM:** Kapag tinawag ang `FileAgent.readFile(path)`, nagpapadala ang LangChain4j ng system message, user message, **at ang listahan ng tool schemas** sa LLM. Binabasa ng LLM ang mga paglalarawan ng tool at bumubuo ng tawag sa tool (halimbawa, `read_file(path="/some/file.txt")`)
5. **Pagpapatupad:** Ina-intercept ng LangChain4j ang tawag sa tool, ipinapasa ito sa MCP client pabalik sa Node.js subprocess, kinukuha ang resulta, at ibinabalik ito sa LLM

Ito ay pareho ng [Tool Discovery](../../../05-mcp) na mekanismo na inilarawan sa itaas, ngunit inilalapat nang partikular sa agent workflow. Ginagabayan ng mga @SystemMessage at @UserMessage na anotasyon ang kilos ng LLM, habang ang injected `ToolProvider` ang nagbibigay nito ng **kakayahan** — pinagdurugtong ng LLM ang dalawa sa runtime.

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) at itanong:
> - "Paano malalaman ng ahenteng ito kung anong MCP tool ang tatawagin?"
> - "Ano ang mangyayari kung aalisin ko ang ToolProvider mula sa agent builder?"
> - "Paano naipapasa ang mga schema ng tool sa LLM?"

#### Mga Estratehiya sa Pagsagot

Kapag nag-configure ka ng `SupervisorAgent`, tinutukoy mo kung paano nito gagawin ang pinal na sagot sa user matapos makumpleto ng mga sub-agent ang kanilang mga gawain. Ipinapakita sa diagram sa ibaba ang tatlong magagamit na estratehiya — ang LAST ay direktang ibinabalik ang huling output ng ahente, ang SUMMARY ay nagsasama-sama ng lahat ng output gamit ang LLM, at ang SCORED ay pinipili ang mas mataas ang marka laban sa orihinal na kahilingan:

<img src="../../../translated_images/tl/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Tatlong estratehiya kung paano ginagawa ng Supervisor ang huling sagot — piliin batay kung gusto mo ang huling output ng ahente, isang pinagsama-samang buod, o ang pinakamataas na score.*

Ang mga magagamit na estratehiya ay:

| Estratehiya | Deskripsyon |
|-------------|-------------|
| **LAST**    | Ibinabalik ng supervisor ang output ng huling sub-agent o tool na tinawag. Ito ay kapaki-pakinabang kapag ang huling ahente sa workflow ay nilikha talaga upang magbigay ng kumpleto at pinal na sagot (hal., isang "Summary Agent" sa research pipeline). |
| **SUMMARY** | Ginagamit ng supervisor ang sarili nitong internal na Language Model (LLM) upang buuin ang isang buod ng buong interaksyon at lahat ng output ng sub-agent, at saka ibinabalik ang buod bilang pinal na sagot. Nagbibigay ito ng malinis at pinag-isang sagot sa user. |
| **SCORED**  | Ginagamit ng sistema ang internal na LLM upang bigyan ng score ang parehong LAST na sagot at ang SUMMARY ng interaksyon laban sa orihinal na kahilingan ng user, at ibinabalik ang output na may mas mataas na iskor. |

Tingnan ang [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) para sa kumpletong implementasyon.

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) at itanong:
> - "Paano nagde-decide ang Supervisor kung aling mga ahente ang tatawagin?"
> - "Ano ang pagkakaiba ng Supervisor at Sequential workflow patterns?"
> - "Paano ko mae-customize ang pagpaplano ng gawi ng Supervisor?"

#### Pag-unawa sa Output

Kapag pinatakbo mo ang demo, makikita mo ang isang nakaayos na walkthrough kung paano inoorganisa ng Supervisor ang marami pang ahente. Ganito ang ibig sabihin ng bawat seksyon:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Ang header** ay nagpapakilala sa konsepto ng workflow: isang nakatutok na pipeline mula sa pagbasa ng file hanggang sa pagbuo ng ulat.

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

**Workflow Diagram** nagpapakita ng daloy ng datos sa pagitan ng mga ahente. May partikular na tungkulin ang bawat ahente:
- **FileAgent** ang nagbabasa ng mga file gamit ang MCP tools at iniimbak ang raw na nilalaman sa `fileContent`
- **ReportAgent** ang kumonsumo sa nilalaman na iyon at gumawa ng istrukturadong ulat sa `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**User Request** nagpapakita ng gawain. Tinutukoy ng Supervisor ito at nagpasiya na tawagin ang FileAgent → ReportAgent.

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

**Supervisor Orchestration** nagpapakita ng 2-hakbang na daloy sa aksyon:
1. **FileAgent** bumabasa ng file sa pamamagitan ng MCP at iniimbak ang nilalaman
2. **ReportAgent** tumatanggap ng nilalaman at gumagawa ng istrukturadong ulat

Ginawa ng Supervisor ang mga desisyong ito **nangangasiwa nang sarili** batay sa kahilingan ng user.

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

Ipinapakita ng halimbawa ang ilang advanced na tampok ng agentic module. Tingnan natin nang mas malaliman ang Agentic Scope at Agent Listeners.

**Agentic Scope** ay nagpapakita ng shared memory kung saan iniimbak ng mga ahente ang kanilang mga resulta gamit ang `@Agent(outputKey="...")`. Pinapahintulutan nito:
- Ma-access ng mga susunod na ahente ang mga output ng naunang ahente
- Magsama-sama ng pinal na sagot ng Supervisor
- Masuri mo kung ano ang ginawa ng bawat ahente

Ipinapakita ng diagram sa ibaba kung paano gumagana ang Agentic Scope bilang shared memory sa file-to-report workflow — isinusulat ng FileAgent ang output nito sa susi na `fileContent`, binabasa ng ReportAgent iyon at sinusulat ang sariling output sa ilalim ng `report`:

<img src="../../../translated_images/tl/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Ang Agentic Scope ay nagsisilbing shared memory — sinusulat ng FileAgent ang `fileContent`, binabasa ito ng ReportAgent at sinusulat ang `report`, at binabasa ng iyong code ang pinal na resulta.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Hilaw na datos ng file mula sa FileAgent
String report = scope.readState("report");            // Istrakturadong ulat mula sa ReportAgent
```

**Agent Listeners** ay nagbibigay-daan sa pagmamanman at pag-debug ng pagpapatupad ng ahente. Ang sunud-sunod na output na nakikita mo sa demo ay nagmumula sa isang AgentListener na nakakabit sa bawat pagtawag ng ahente:
- **beforeAgentInvocation** - Tinatawag kapag pinili ng Supervisor ang isang ahente, upang makita mo kung aling ahente ang pinili at bakit
- **afterAgentInvocation** - Tinatawag kapag natapos ang isang ahente, na nagpapakita ng resulta nito
- **inheritedBySubagents** - Kapag true, minomonitor ng listener ang lahat ng ahente sa hierarchy

Ipinapakita ng sumusunod na diagram ang buong lifecycle ng Agent Listener, kabilang kung paano hinahandle ng `onError` ang mga pagkakamali habang nagpapatakbo ang ahente:

<img src="../../../translated_images/tl/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Ang Agent Listeners ay nakakabit sa lifecycle ng pagpapatakbo — minomonitor kung kailan nagsisimula, natatapos, o nagkakaroon ng error ang mga ahente.*

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
        return true; // Ipasa sa lahat ng sub-agents
    }
};
```

Bukod sa Supervisor pattern, nag-aalok ang `langchain4j-agentic` module ng maraming makapangyarihang workflow patterns. Ipinapakita ng diagram sa ibaba ang lahat ng lima — mula sa simpleng sunud-sunod na pipeline hanggang sa human-in-the-loop na approval workflows:

<img src="../../../translated_images/tl/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Limang workflow patterns para sa pag-oorganisa ng mga ahente — mula sa simpleng sunud-sunod na pipeline hanggang sa human-in-the-loop na approval workflows.*

| Pattern | Deskripsyon | Use Case |
|---------|-------------|----------|
| **Sequential** | Isakatuparan ang mga ahente nang ayon sa pagkakasunod, ang output ang dumadaloy sa susunod | Pipelines: research → analyze → report |
| **Parallel** | Patakbuhin ang mga ahente nang sabay-sabay | Independiyenteng mga gawain: panahon + balita + stocks |
| **Loop** | Ulitin hanggang matugunan ang kondisyon | Quality scoring: pinuhin hanggang ang score ay ≥ 0.8 |
| **Conditional** | Ruta batay sa mga kondisyon | Iklasipika → ruta sa espesyalistang ahente |
| **Human-in-the-Loop** | Magdagdag ng human checkpoints | Mga approval workflow, pagsusuri ng nilalaman |

## Mga Pangunahing Konsepto

Ngayon na na-explore mo na ang MCP at ang agentic module sa aksyon, buuin natin kung kailan gagamitin ang bawat paraan.

Isa sa pinakamalaking bentahe ng MCP ay ang lumalaking ecosystem nito. Ipinapakita ng diagram sa ibaba kung paano kumokonekta ang isang unibersal na protocol ng iyong AI application sa iba't ibang MCP servers — mula sa access sa filesystem at database hanggang sa GitHub, email, web scraping, at iba pa:

<img src="../../../translated_images/tl/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*Lumilikha ang MCP ng ecosystem ng unibersal na protocol — anumang MCP-compatible server ay gumagana sa anumang MCP-compatible client, nagpapahintulot sa pagbabahagi ng mga tool sa iba't ibang aplikasyon.*

**Ang MCP** ay perpekto kapag nais mong gamitin ang mga umiiral na ecosystem ng tool, gumawa ng mga tool na maaaring gamitin ng maraming application, isama ang mga third-party na serbisyo gamit ang mga standard na protocol, o palitan ang implementasyon ng tool nang hindi binabago ang code.

**Ang Agentic Module** ang pinakamainam kapag nais mo ng deklaratibong mga agent definition gamit ang `@Agent` annotations, kailangan ng workflow orchestration (sequential, loop, parallel), mas gusto ang interface-based agent design kaysa sa imperative code, o pinagsasama ang maraming ahente na nagbabahagi ng outputs gamit ang `outputKey`.

**Namumukod-tangi ang Supervisor Agent pattern** kapag hindi predictable ang workflow nang maaga at gusto mong ang LLM ang magdesisyon, kapag maraming specialized agent na kailangan ng dynamic na orchestration, kapag gumagawa ng conversational system na nagruruta sa iba't ibang kakayahan, o kapag gusto mo ng pinaka-flexible at adaptive na kilos ng ahente.

Upang matulungan kang magdesisyon sa pagitan ng custom `@Tool` methods mula sa Module 04 at MCP tools mula sa module na ito, ipinamumungkahi sa sumusunod na paghahambing ang mga pangunahing trade-off — ang custom tools ay nagbibigay ng mahigpit na ugnayan at kumpletong type safety para sa logic ng app, habang ang MCP tools ay nag-aalok ng standardized, reusable na integrasyon:

<img src="../../../translated_images/tl/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Kailan gagamit ng custom @Tool methods laban MCP tools — custom tools para sa logic na app-specific na may kumpletong type safety, MCP tools para sa standardized na mga integrasyon na gumagana sa iba't ibang aplikasyon.*

## Congratulations!

Natapos mo na ang lahat ng limang module ng LangChain4j para sa mga Baguhan! Narito ang buong paglalakbay sa pagkatuto na iyong tinahak — mula sa basic chat hanggang sa MCP-powered na agentic systems:

<img src="../../../translated_images/tl/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Ang paglalakbay mo sa pagkatuto sa lahat ng limang modules — mula sa basic chat hanggang sa MCP-powered na agentic systems.*

Natapos mo na ang LangChain4j para sa Baguhan na kurso. Natutunan mo:

- Paano gumawa ng conversational AI na may memorya (Module 01)
- Mga pattern sa prompt engineering para sa iba't ibang gawain (Module 02)
- Pagbatayan ang mga sagot sa iyong mga dokumento gamit ang RAG (Module 03)
- Paglikha ng mga basic AI agents (assistants) gamit ang custom tools (Module 04)
- Pagsasama ng standardized tools gamit ang LangChain4j MCP at Agentic modules (Module 05)

### Ano ang Susunod?

Pagkatapos matapos ang mga module, tuklasin ang [Testing Guide](../docs/TESTING.md) para makita ang mga konsepto ng pagsusuri sa LangChain4j na ginagamit.

**Opisyal na Mga Mapagkukunan:**
- [LangChain4j Documentation](https://docs.langchain4j.dev/) - Komprehensibong mga gabay at API reference
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Source code at mga halimbawa
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - Mga step-by-step tutorial para sa iba't ibang gamit

Salamat sa pagtapos ng kursong ito!

---

**Navigasyon:** [← Nakaraan: Module 04 - Tools](../04-tools/README.md) | [Pabalik sa Pangunahing Pahina](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Paunawa**:
Ang dokumentong ito ay isinalin gamit ang AI translation service na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagama't nagsusumikap kami para sa katumpakan, pakatandaan na maaaring may mga kamalian o di-tumpak na bahagi ang mga awtomatikong pagsasalin. Ang orihinal na dokumento sa kanyang orihinal na wika ang dapat ituring na opisyal na sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na maaaring magmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->