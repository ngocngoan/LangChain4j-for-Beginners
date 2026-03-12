# Modul 05: Model Context Protocol (MCP)

## Indholdsfortegnelse

- [Video-gennemgang](../../../05-mcp)
- [Det lærer du](../../../05-mcp)
- [Hvad er MCP?](../../../05-mcp)
- [Hvordan MCP virker](../../../05-mcp)
- [Agentmodulet](../../../05-mcp)
- [Køre eksemplerne](../../../05-mcp)
  - [Forudsætninger](../../../05-mcp)
- [Hurtig start](../../../05-mcp)
  - [Filoperationer (Stdio)](../../../05-mcp)
  - [Supervisor Agent](../../../05-mcp)
    - [Køre demoen](../../../05-mcp)
    - [Hvordan Supervisor fungerer](../../../05-mcp)
    - [Hvordan FileAgent opdager MCP-værktøjer ved kørsel](../../../05-mcp)
    - [Svarstrategier](../../../05-mcp)
    - [Forstå outputtet](../../../05-mcp)
    - [Forklaring af Agentmodulets funktioner](../../../05-mcp)
- [Nøglebegreber](../../../05-mcp)
- [Tillykke!](../../../05-mcp)
  - [Hvad nu?](../../../05-mcp)

## Video-gennemgang

Se denne live-session, der forklarer, hvordan du kommer i gang med dette modul:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="AI Agents with Tools and MCP - Live Session" width="800"/></a>

## Det lærer du

Du har bygget konverserende AI, mestret prompts, forankret svar i dokumenter, og skabt agenter med værktøjer. Men alle de værktøjer var specialbygget til din specifikke applikation. Hvad hvis du kunne give din AI adgang til et standardiseret økosystem af værktøjer, som alle kan oprette og dele? I dette modul lærer du netop det med Model Context Protocol (MCP) og LangChain4j’s agentmodul. Vi viser først en simpel MCP-fil læser og demonstrerer derefter, hvordan den let integreres i avancerede agent-arbejdsgange via Supervisor Agent-mønsteret.

## Hvad er MCP?

Model Context Protocol (MCP) giver præcis det - en standard måde for AI-applikationer at opdage og bruge eksterne værktøjer på. I stedet for at skrive specialtilpassede integrationer for hver datakilde eller tjeneste, forbinder du til MCP-servere, der eksponerer deres funktionaliteter i et ensartet format. Din AI-agent kan derefter automatisk opdage og anvende disse værktøjer.

Diagrammet nedenfor viser forskellen — uden MCP kræver hver integration specialtilpasset punkt-til-punkt forbindelse; med MCP forbinder en enkelt protokol din app til ethvert værktøj:

<img src="../../../translated_images/da/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Før MCP: Komplekse punkt-til-punkt integrationer. Efter MCP: En protokol, uendelige muligheder.*

MCP løser et grundlæggende problem i AI-udvikling: hver integration er specialbygget. Vil du tilgå GitHub? Specialkode. Vil du læse filer? Specialkode. Vil du spørge en database? Specialkode. Og ingen af disse integrationer fungerer med andre AI-applikationer.

MCP standardiserer det. En MCP-server eksponerer værktøjer med klare beskrivelser og skemaer. Enhver MCP-klient kan forbinde, opdage tilgængelige værktøjer og bruge dem. Byg én gang, brug overalt.

Diagrammet nedenfor illustrerer denne arkitektur — en enkelt MCP-klient (din AI-applikation) forbinder til flere MCP-servere, der hver eksponerer deres eget sæt værktøjer gennem den standardiserede protokol:

<img src="../../../translated_images/da/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol arkitektur - standardiseret værktøjsopdagelse og udførelse*

## Hvordan MCP virker

Under motorhjelmen bruger MCP en lagdelt arkitektur. Din Java-applikation (MCP-klienten) opdager tilgængelige værktøjer, sender JSON-RPC forespørgsler gennem et transportlag (Stdio eller HTTP), og MCP-serveren udfører operationer og returnerer resultater. Følgende diagram opdeler hver lag i denne protokol:

<img src="../../../translated_images/da/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Hvordan MCP virker under motorhjelmen — klienter opdager værktøjer, udveksler JSON-RPC beskeder og udfører operationer gennem et transportlag.*

**Server-Klient Arkitektur**

MCP bruger en klient-server model. Servere leverer værktøjer - læse filer, forespørge databaser, kalde API’er. Klienter (din AI-applikation) forbinder til servere og bruger deres værktøjer.

For at bruge MCP med LangChain4j, tilføj denne Maven-afhængighed:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Værktøjsopdagelse**

Når din klient forbinder til en MCP-server, spørger den: "Hvilke værktøjer har du?" Serveren svarer med en liste over tilgængelige værktøjer, hver med beskrivelser og parameter skemaer. Din AI-agent kan så beslutte hvilke værktøjer, der skal bruges baseret på brugerens forespørgsler. Diagrammet nedenfor viser dette håndtryk — klienten sender en `tools/list` forespørgsel, og serveren returnerer sine tilgængelige værktøjer med beskrivelser og skemaer for parametre:

<img src="../../../translated_images/da/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI opdager tilgængelige værktøjer ved opstart — den ved nu, hvilke funktionaliteter der findes, og kan vælge hvilke der skal bruges.*

**Transportmekanismer**

MCP understøtter forskellige transportmekanismer. De to muligheder er Stdio (for lokal underproceskommunikation) og Streamable HTTP (for fjernservere). Dette modul demonstrerer Stdio-transporter:

<img src="../../../translated_images/da/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP transportmekanismer: HTTP for fjernservere, Stdio for lokale processer*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

For lokale processer. Din applikation starter en server som en underproces og kommunikerer gennem standard input/output. Nyttigt til filsystemadgang eller kommandolinjeværktøjer.

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

`@modelcontextprotocol/server-filesystem` serveren eksponerer følgende værktøjer, alle sandboxed til de mapper du angiver:

| Værktøj | Beskrivelse |
|---------|-------------|
| `read_file` | Læs indholdet af en enkelt fil |
| `read_multiple_files` | Læs flere filer i ét kald |
| `write_file` | Opret eller overskriv en fil |
| `edit_file` | Udfør målrettede find-og-erstatt ændringer |
| `list_directory` | List filer og mapper på en sti |
| `search_files` | Søg rekursivt efter filer, der matcher et mønster |
| `get_file_info` | Hent metadata om filer (størrelse, tidsstempler, tilladelser) |
| `create_directory` | Opret en mappe (inklusive overordnede mapper) |
| `move_file` | Flyt eller omdøb en fil eller mappe |

Følgende diagram viser, hvordan Stdio-transport virker under kørsel — din Java-applikation starter MCP-serveren som en børneproces, og de kommunikerer via stdin/stdout rør, uden noget netværk eller HTTP involveret:

<img src="../../../translated_images/da/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio-transport i aktion — din applikation starter MCP-serveren som en børneproces og kommunikerer gennem stdin/stdout-rør.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) og spørg:
> - "Hvordan fungerer Stdio transport, og hvornår skal jeg bruge det i stedet for HTTP?"
> - "Hvordan håndterer LangChain4j livscyklussen for de startede MCP-serverprocesser?"
> - "Hvad er sikkerhedsmæssige konsekvenser af at give AI adgang til filsystemet?"

## Agentmodulet

Mens MCP leverer standardiserede værktøjer, giver LangChain4j’s **agentmodul** en deklarativ måde at bygge agenter, der orkestrerer disse værktøjer på. `@Agent`-annotationen og `AgenticServices` lader dig definere agentadfærd gennem interfaces fremfor imperativ kode.

I dette modul udforsker du **Supervisor Agent**-mønsteret — en avanceret agentisk AI-tilgang, hvor en "supervisor"-agent dynamisk beslutter, hvilke under-agenter der skal kaldes baseret på brugerens forespørgsel. Vi kombinerer begge koncepter ved at give en af vores under-agenter MCP-drevne filadgangsmuligheder.

For at bruge agentmodulet, tilføj denne Maven-afhængighed:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Bemærk:** `langchain4j-agentic` modulet bruger en separat versionsproperty (`langchain4j.mcp.version`), fordi det udgives efter en anden tidsplan end LangChain4j-kernen.

> **⚠️ Eksperimentelt:** `langchain4j-agentic` modulet er **eksperimentelt** og kan ændre sig. Den stabile måde at bygge AI assistenter på er fortsat `langchain4j-core` med brugerdefinerede værktøjer (Modul 04).

## Køre eksemplerne

### Forudsætninger

- Færdiggjort [Modul 04 - Værktøjer](../04-tools/README.md) (dette modul bygger på begrebet brugerdefinerede værktøjer og sammenligner med MCP-værktøjer)
- `.env` fil i rodmappen med Azure legitimationsoplysninger (oprettet af `azd up` i Modul 01)
- Java 21+, Maven 3.9+
- Node.js 16+ og npm (til MCP-servere)

> **Bemærk:** Hvis du ikke har sat miljøvariabler op endnu, se [Modul 01 - Introduktion](../01-introduction/README.md) for deploymentsinstruktioner (`azd up` opretter `.env` filen automatisk), eller kopier `.env.example` til `.env` i rodmappen og udfyld dine værdier.

## Hurtig start

**Bruger du VS Code:** Højreklik blot på en hvilken som helst demo-fil i Explorer og vælg **"Run Java"**, eller brug launch-konfigurationerne fra Run and Debug-panelet (sørg først for at din `.env` fil er konfigureret med Azure-legitimationsoplysninger).

**Bruger du Maven:** Alternativt kan du køre fra kommandolinjen med eksemplerne nedenfor.

### Filoperationer (Stdio)

Dette demonstrerer lokale værktøjer baseret på underprocesser.

**✅ Ingen forudsætninger nødvendige** - MCP-serveren startes automatisk.

**Brug start-scripts (anbefalet):**

Start-scripts indlæser automatisk miljøvariabler fra rodmappen `.env` fil:

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

**Brug VS Code:** Højreklik på `StdioTransportDemo.java` og vælg **"Run Java"** (sørg for at din `.env` fil er konfigureret).

Applikationen starter en MCP-server til filsystemet automatisk og læser en lokal fil. Bemærk hvordan underprocesstyringen håndteres for dig.

**Forventet output:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor Agent

**Supervisor Agent-mønsteret** er en **fleksibel** form for agentisk AI. En Supervisor bruger en LLM til autonomt at beslutte, hvilke agenter der skal kaldes baseret på brugerens anmodning. I det næste eksempel kombinerer vi MCP-drevet filadgang med en LLM-agent for at skabe en overvåget læs → rapport arbejdsgang.

I demoen læser `FileAgent` en fil ved hjælp af MCP-filsystemværktøjer, og `ReportAgent` genererer en struktureret rapport med et executive summary (1 sætning), 3 nøglepunkter og anbefalinger. Supervisoren orkestrerer denne proces automatisk:

<img src="../../../translated_images/da/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Supervisor bruger sin LLM til at beslutte, hvilke agenter der skal kaldes og i hvilken rækkefølge — ingen hardkodet routing nødvendig.*

Sådan ser den konkrete arbejdsgang ud for vores pipeline fra fil til rapport:

<img src="../../../translated_images/da/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent læser filen via MCP-værktøjer, og ReportAgent omsætter det rå indhold til en struktureret rapport.*

Følgende sekvensdiagram sporer den fulde Supervisor-orkestrering — fra opstart af MCP-serveren, gennem Supervisors autonome agentvalg, til kald af værktøjer over stdio og den endelige rapport:

<img src="../../../translated_images/da/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*Supervisor kalder autonomt FileAgent (der kaldte MCP-serveren over stdio for at læse filen), kører derefter ReportAgent til at generere en struktureret rapport — hver agent gemmer sit output i det delte Agentic Scope.*

Hver agent gemmer sit output i **Agentic Scope** (delt hukommelse), hvilket tillader efterfølgende agenter at få adgang til tidligere resultater. Dette demonstrerer, hvordan MCP-værktøjer glider problemfrit ind i agentisk arbejdsgange — Supervisor behøver ikke at vide *hvordan* filer læses, kun at `FileAgent` kan gøre det.

#### Køre demoen

Start-scripts indlæser automatisk miljøvariabler fra rodmappen `.env` fil:

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

**Brug VS Code:** Højreklik på `SupervisorAgentDemo.java` og vælg **"Run Java"** (sørg for at din `.env` fil er konfigureret).

#### Hvordan Supervisor fungerer

Før du bygger agenter, skal du forbinde MCP-transporten til en klient og pakke den som en `ToolProvider`. Sådan bliver MCP-serverens værktøjer tilgængelige for dine agenter:

```java
// Opret en MCP-klient fra transporten
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Pak klienten ind som en ToolProvider — dette forbinder MCP-værktøjer til LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Nu kan du injicere `mcpToolProvider` i enhver agent, der har brug for MCP-værktøjer:

```java
// Trin 1: FileAgent læser filer ved hjælp af MCP-værktøjer
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Har MCP-værktøjer til filoperationer
        .build();

// Trin 2: ReportAgent genererer strukturerede rapporter
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor orkestrerer fil → rapport arbejdsgangen
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Returner den endelige rapport
        .build();

// Supervisor beslutter hvilke agenter der skal påkaldes baseret på anmodningen
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Hvordan FileAgent opdager MCP-værktøjer ved kørsel

Du undrer dig måske: **hvordan ved `FileAgent`, hvordan man bruger npm-filsystemværktøjerne?** Svaret er, at det gør den ikke — det er **LLM’en**, der finder ud af det ved kørsel gennem værktøjsskemaer.
`FileAgent`-grænsefladen er bare en **prompt-definition**. Den har ingen hardkodet viden om `read_file`, `list_directory` eller andre MCP-værktøjer. Her er hvad der sker fra ende til anden:

1. **Server starter:** `StdioMcpTransport` launcher `@modelcontextprotocol/server-filesystem` npm-pakken som en child process  
2. **Værktøjsopdagelse:** `McpClient` sender en `tools/list` JSON-RPC forespørgsel til serveren, som svarer med værktøjsnavne, beskrivelser og parameterskemaer (fx `read_file` — *"Read the complete contents of a file"* — `{ path: string }`)  
3. **Skema-injektion:** `McpToolProvider` indpakker disse opdagede skemaer og gør dem tilgængelige for LangChain4j  
4. **LLM beslutter:** Når `FileAgent.readFile(path)` kaldes, sender LangChain4j systembeskeden, brugerbeskeden, **og listen over værktøjsskemaer** til LLM. LLM læser værktøjsbeskrivelserne og genererer et værktøjskald (fx `read_file(path="/some/file.txt")`)  
5. **Eksekvering:** LangChain4j fanger værktøjskaldet, ruter det gennem MCP-klienten tilbage til Node.js subprocessen, modtager resultatet og sender det tilbage til LLM  

Dette er den samme [Tool Discovery](../../../05-mcp) mekanisme beskrevet ovenfor, men anvendt specifikt i agent-workflowet. `@SystemMessage` og `@UserMessage` annotationerne styrer LLM’s adfærd, mens den injicerede `ToolProvider` giver den **kapabiliteterne** — LLM forbinder de to ved runtime.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) og spørg:  
> - "Hvordan ved denne agent, hvilket MCP-værktøj den skal kalde?"  
> - "Hvad sker der, hvis jeg fjerner ToolProvider fra agent builderen?"  
> - "Hvordan bliver værktøjsskemaerne sendt til LLM?"  

#### Responsstrategier

Når du konfigurerer en `SupervisorAgent`, angiver du, hvordan den skal formulere sit endelige svar til brugeren efter under-agenterne har udført deres opgaver. Diagrammet nedenfor viser de tre tilgængelige strategier — LAST returnerer den sidste agents output direkte, SUMMARY syntetiserer alle output via en LLM, og SCORED vælger den med højeste score mod den oprindelige forespørgsel:

<img src="../../../translated_images/da/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Tre strategier for, hvordan Supervisor formulerer sit endelige svar — vælg baseret på, om du vil have sidste agents output, et syntetiseret resumé eller den bedst scorende mulighed.*

De tilgængelige strategier er:

| Strategi | Beskrivelse |
|----------|-------------|
| **LAST** | Supervisor returnerer output fra den sidste sub-agent eller det kaldte værktøj. Dette er nyttigt, når den sidste agent i workflowet er designet til specifikt at producere det komplette endelige svar (fx en "Summary Agent" i en forskningspipeline). |
| **SUMMARY** | Supervisor bruger sin egen interne Language Model (LLM) til at syntetisere et resumé af hele interaktionen og alle sub-agent output og returnerer så dette resumé som det endelige svar. Dette giver et klart, samlet svar til brugeren. |
| **SCORED** | Systemet bruger en intern LLM til at score både LAST-responsen og SUMMARY af interaktionen mod den oprindelige brugerforespørgsel og returnerer den output, der får højeste score. |

Se [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) for den komplette implementering.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) og spørg:  
> - "Hvordan beslutter Supervisor, hvilke agenter der skal kaldes?"  
> - "Hvad er forskellen mellem Supervisor og Sequential workflow-mønstrene?"  
> - "Hvordan kan jeg tilpasse Supervisors planlægningsadfærd?"  

#### Forstå outputtet

Når du kører demoen, vil du se en struktureret gennemgang af, hvordan Supervisor orkestrerer flere agenter. Her er hvad hver sektion betyder:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Headeren** introducerer workflow-konceptet: en fokuseret pipeline fra fil-læsning til rapportgenerering.

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
  
**Workflow-diagram** viser dataflowet mellem agenter. Hver agent har en specifik rolle:  
- **FileAgent** læser filer via MCP-værktøjer og gemmer rå indhold i `fileContent`  
- **ReportAgent** bruger det indhold og producerer en struktureret rapport i `report`  

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Brugerforespørgsel** viser opgaven. Supervisor parser dette og beslutter at kalde FileAgent → ReportAgent.

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
  
**Supervisor orkestrering** viser 2-trins flowet i aktion:  
1. **FileAgent** læser filen via MCP og gemmer indholdet  
2. **ReportAgent** modtager indholdet og genererer en struktureret rapport  

Supervisor traf disse beslutninger **autonomt** baseret på brugerens forespørgsel.

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
  
#### Forklaring af Agentic Module-funktioner

Eksemplet demonstrerer flere avancerede funktioner i agentic-modulet. Lad os se nærmere på Agentic Scope og Agent Listeners.

**Agentic Scope** viser den delte hukommelse, hvor agenter gemte deres resultater med `@Agent(outputKey="...")`. Dette muliggør:  
- At senere agenter kan få adgang til tidligere agenters output  
- At Supervisor kan syntetisere et endeligt svar  
- At du kan inspicere, hvad hver agent producerede  

Diagrammet nedenfor viser, hvordan Agentic Scope fungerer som delt hukommelse i fil-til-rapport workflowet — FileAgent skriver sit output under nøglen `fileContent`, ReportAgent læser det og skriver sit output under `report`:

<img src="../../../translated_images/da/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope fungerer som delt hukommelse — FileAgent skriver `fileContent`, ReportAgent læser det og skriver `report`, og din kode læser det endelige resultat.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Rå fildata fra FileAgent
String report = scope.readState("report");            // Struktureret rapport fra ReportAgent
```
  
**Agent Listeners** muliggør overvågning og fejlfinding af agent-eksekvering. Det trin-for-trin output, du ser i demoen, kommer fra en AgentListener, som kobler sig på hver agent-kald:  
- **beforeAgentInvocation** - Kaldes, når Supervisor vælger en agent, så du kan se, hvilken agent der blev valgt og hvorfor  
- **afterAgentInvocation** - Kaldes når en agent afslutter, og viser resultatet  
- **inheritedBySubagents** - Når true, overvåger listeneren alle agenter i hierarkiet  

Følgende diagram viser hele Agent Listener livscyklussen, inklusive hvordan `onError` håndterer fejl under agent-eksekvering:

<img src="../../../translated_images/da/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners kobler sig på eksekveringslivscyklussen — overvåg når agenter starter, afslutter eller får fejl.*

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
        return true; // Propager til alle underagenter
    }
};
```
  
Udover Supervisor-mønstret tilbyder `langchain4j-agentic` modulet flere kraftfulde workflow-mønstre. Diagrammet nedenfor viser alle fem — fra simple sekventielle pipelines til human-in-the-loop godkendelsesflows:

<img src="../../../translated_images/da/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Fem workflow-mønstre til orkestrering af agenter — fra simple sekventielle pipelines til human-in-the-loop godkendelsesflows.*

| Mønster | Beskrivelse | Brugstilfælde |
|---------|-------------|---------------|
| **Sequential** | Udfør agenter i rækkefølge, output flyder til næste | Pipelines: forskning → analyse → rapport |
| **Parallel** | Kør agenter samtidigt | Uafhængige opgaver: vejr + nyheder + aktier |
| **Loop** | Iterer indtil betingelse er opfyldt | Kvalitetsscore: finjuster indtil score ≥ 0.8 |
| **Conditional** | Rout baseret på betingelser | Klassificer → ruter til specialist-agent |
| **Human-in-the-Loop** | Tilføj menneskelige kontrolpunkter | Godkendelsesflows, indholdsreview |

## Centrale Begreber

Nu hvor du har udforsket MCP og agentic-modulet i praksis, lad os opsummere, hvornår hvert tiltag skal bruges.

En af MCP's største fordele er det voksende økosystem. Diagrammet nedenfor viser, hvordan en enkelt universel protokol forbinder din AI-applikation til en bred vifte af MCP-servere — fra filsystem- og databaseadgang til GitHub, email, web scraping og mere:

<img src="../../../translated_images/da/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP skaber et universelt protokol-økosystem — enhver MCP-kompatibel server virker med enhver MCP-kompatibel klient, hvilket muliggør værktøjsdeling på tværs af applikationer.*

**MCP** er ideelt, når du vil udnytte eksisterende værktøjs-økosystemer, bygge værktøjer som flere applikationer kan dele, integrere tredjepartstjenester med standardprotokoller eller skifte værktøjsimplementeringer uden kodeændring.

**Agentic Modulet** fungerer bedst, når du ønsker deklarative agent-definitioner med `@Agent` annotationer, har brug for workflow-orkestrering (sekventiel, loop, parallel), foretrækker interface-baseret agentdesign fremfor imperativ kode, eller kombinerer flere agenter med delte outputs via `outputKey`.

**Supervisor Agent-mønstret** er ideelt, når workflowet ikke kan forudsiges på forhånd og du ønsker, at LLM beslutter, når du har flere specialiserede agenter, der har brug for dynamisk orkestrering, ved opbygning af samtalesystemer, der router til forskellige kapabiliteter, eller når du ønsker den mest fleksible, adaptive agent-adfærd.

For at hjælpe dig med at vælge mellem de brugerdefinerede `@Tool` metoder fra Modul 04 og MCP værktøjer fra dette modul, fremhæver følgende sammenligning nøglekompromiser — brugerdefinerede værktøjer giver dig tæt kobling og fuld typesikkerhed til app-specifik logik, mens MCP værktøjer tilbyder standardiserede, genanvendelige integrationer:

<img src="../../../translated_images/da/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Hvornår du skal bruge brugerdefinerede @Tool metoder vs MCP værktøjer — brugerdefinerede værktøjer til app-specifik logik med fuld typesikkerhed, MCP værktøjer til standardiserede integrationer, der virker på tværs af applikationer.*

## Tillykke!

Du er kommet igennem alle fem moduler i LangChain4j for Begyndere kurset! Her er et overblik over hele læringsrejsen, du har gennemført — fra grundlæggende chat til MCP-drevne agent-systemer:

<img src="../../../translated_images/da/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Din læringsrejse gennem alle fem moduler — fra grundlæggende chat til MCP-drevne agent-systemer.*

Du har gennemført LangChain4j for Beginners kurset. Du har lært:

- Hvordan man bygger konverserende AI med hukommelse (Modul 01)  
- Prompt engineering mønstre til forskellige opgaver (Modul 02)  
- At fundere svar i dine dokumenter med RAG (Modul 03)  
- At skabe basale AI-agenter (assistenter) med brugerdefinerede værktøjer (Modul 04)  
- At integrere standardiserede værktøjer med LangChain4j MCP og Agentic modulerne (Modul 05)  

### Hvad nu?

Efter du har gennemført modulerne, kan du udforske [Testing Guide](../docs/TESTING.md) for at se LangChain4j testkoncepter i praksis.

**Officielle ressourcer:**  
- [LangChain4j Dokumentation](https://docs.langchain4j.dev/) - Omfattende guider og API reference  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Kildekode og eksempler  
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - Trin-for-trin tutorials til forskellige anvendelser  

Tak fordi du gennemførte dette kursus!

---

**Navigation:** [← Forrige: Modul 04 - Tools](../04-tools/README.md) | [Tilbage til Hoved](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:  
Dette dokument er oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, skal du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det oprindelige dokument på dets oprindelige sprog bør anses for at være den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for misforståelser eller fejltolkninger, der opstår som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->