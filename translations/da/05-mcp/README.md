# Modul 05: Model Context Protocol (MCP)

## Indholdsfortegnelse

- [Hvad du vil lære](../../../05-mcp)
- [Hvad er MCP?](../../../05-mcp)
- [Hvordan MCP virker](../../../05-mcp)
- [Agent-modulet](../../../05-mcp)
- [Køre eksemplerne](../../../05-mcp)
  - [Forudsætninger](../../../05-mcp)
- [Kom hurtigt i gang](../../../05-mcp)
  - [Filoperationer (Stdio)](../../../05-mcp)
  - [Supervisor Agent](../../../05-mcp)
    - [Køre demoen](../../../05-mcp)
    - [Hvordan Supervisor virker](../../../05-mcp)
    - [Responsstrategier](../../../05-mcp)
    - [Forstå outputtet](../../../05-mcp)
    - [Forklaring af funktioner i Agent-modulet](../../../05-mcp)
- [Nøglebegreber](../../../05-mcp)
- [Tillykke!](../../../05-mcp)
  - [Hvad er næste skridt?](../../../05-mcp)

## Hvad du vil lære

Du har bygget samtale-AI, mestret prompts, forankret svar i dokumenter og skabt agenter med værktøjer. Men alle disse værktøjer var skræddersyede til din specifikke applikation. Hvad nu hvis du kunne give din AI adgang til et standardiseret økosystem af værktøjer, som alle kan oprette og dele? I dette modul lærer du præcis det med Model Context Protocol (MCP) og LangChain4j's agent-modul. Vi viser først en simpel MCP fillæser og demonstrerer derefter, hvordan den let integreres i avancerede agentarbejdsgange ved brug af Supervisor Agent-mønsteret.

## Hvad er MCP?

Model Context Protocol (MCP) tilbyder netop det — en standard måde for AI-applikationer at opdage og bruge eksterne værktøjer. I stedet for at skrive tilpassede integrationer for hver datakilde eller service, forbinder du til MCP-servere, der udstiller deres funktioner i et konsistent format. Din AI-agent kan derefter automatisk opdage og bruge disse værktøjer.

Diagrammet nedenfor viser forskellen — uden MCP kræver hver integration speciallavet punkt-til-punkt-forbindelser; med MCP forbinder et enkelt protokol din app til ethvert værktøj:

<img src="../../../translated_images/da/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Sammenligning" width="800"/>

*Før MCP: Komplekse punkt-til-punkt-integrationer. Efter MCP: Én protokol, uendelige muligheder.*

MCP løser et grundlæggende problem i AI-udvikling: hver integration er speciallavet. Vil du tilgå GitHub? Specialkodning. Vil du læse filer? Specialkodning. Vil du spørge en database? Specialkodning. Og ingen af disse integrationer virker med andre AI-applikationer.

MCP standardiserer dette. En MCP-server udstiller værktøjer med klare beskrivelser og skemaer. Enhver MCP-klient kan forbinde, opdage tilgængelige værktøjer og bruge dem. Byg én gang, brug overalt.

Diagrammet nedenfor illustrerer denne arkitektur — en enkelt MCP-klient (din AI-applikation) forbinder til flere MCP-servere, som hver udstiller deres eget sæt værktøjer via den standardiserede protokol:

<img src="../../../translated_images/da/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Arkitektur" width="800"/>

*Model Context Protocol arkitektur - standardiseret værktøjsopdagelse og udførelse*

## Hvordan MCP virker

Under overfladen bruger MCP en lagdelt arkitektur. Din Java-applikation (MCP-klienten) opdager tilgængelige værktøjer, sender JSON-RPC forespørgsler gennem et transportlag (Stdio eller HTTP), og MCP-serveren udfører operationer og returnerer resultater. Følgende diagram nedbryder hvert lag i denne protokol:

<img src="../../../translated_images/da/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protokol Detaljer" width="800"/>

*Hvordan MCP virker under overfladen — klienter opdager værktøjer, udveksler JSON-RPC-beskeder og udfører operationer gennem et transportlag.*

**Server-klient arkitektur**

MCP bruger et klient-server model. Servere leverer værktøjer — læse filer, forespørge databaser, kalde API’er. Klienter (din AI-applikation) forbinder til servere og bruger deres værktøjer.

For at bruge MCP med LangChain4j, tilføj denne Maven-afhængighed:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Værktøjsopdagelse**

Når din klient forbinder til en MCP-server, spørger den "Hvilke værktøjer har du?" Serveren svarer med en liste over tilgængelige værktøjer, hver med beskrivelser og parameter-skemaer. Din AI-agent kan så beslutte, hvilke værktøjer den vil bruge baseret på brugerens forespørgsler. Diagrammet nedenfor viser dette håndtryk — klienten sender en `tools/list` forespørgsel og serveren returnerer sine tilgængelige værktøjer med beskrivelser og parametrene:

<img src="../../../translated_images/da/tool-discovery.07760a8a301a7832.webp" alt="MCP Værktøjsopdagelse" width="800"/>

*AI opdager tilgængelige værktøjer ved opstart — det ved nu, hvilke funktioner der findes, og kan beslutte hvilke den vil bruge.*

**Transportmekanismer**

MCP understøtter forskellige transportmekanismer. De to muligheder er Stdio (til lokal subprocess-kommunikation) og Streamable HTTP (til fjernservere). Dette modul demonstrerer Stdio-transport:

<img src="../../../translated_images/da/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mekanismer" width="800"/>

*MCP transportmekanismer: HTTP til fjernservere, Stdio til lokale processer*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Til lokale processer. Din applikation starter en server som en subprocess og kommunikerer via standard input/output. Brugbart til filsystemadgang eller kommandolinjeværktøjer.

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

`@modelcontextprotocol/server-filesystem` serveren udstiller følgende værktøjer, alle sandboxed til de mapper, du angiver:

| Værktøj | Beskrivelse |
|------|-------------|
| `read_file` | Læs indholdet af en enkelt fil |
| `read_multiple_files` | Læs flere filer i ét kald |
| `write_file` | Opret eller overskriv en fil |
| `edit_file` | Foretag målrettede find-og-erstat redigeringer |
| `list_directory` | List filer og mapper på en sti |
| `search_files` | Søg rekursivt efter filer, der matcher et mønster |
| `get_file_info` | Hent filmetadata (størrelse, tidspunkter, tilladelser) |
| `create_directory` | Opret en mappe (inklusive forældre-mapper) |
| `move_file` | Flyt eller omdøb en fil eller mappe |

Følgende diagram viser, hvordan Stdio-transport virker ved kørsel — din Java-applikation starter MCP-serveren som en underordnet proces og de kommunikerer via stdin/stdout rør, uden netværk eller HTTP involveret:

<img src="../../../translated_images/da/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio transport i aktion — din applikation starter MCP-serveren som en underordnet proces og kommunikerer via stdin/stdout rør.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åben [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) og spørg:
> - "Hvordan virker Stdio transport, og hvornår skal jeg bruge den i stedet for HTTP?"
> - "Hvordan håndterer LangChain4j livscyklussen for oprettede MCP-serverprocesser?"
> - "Hvad er sikkerhedsmæssige konsekvenser ved at give AI adgang til filsystemet?"

## Agent-modulet

Mens MCP tilbyder standardiserede værktøjer, giver LangChain4j's **agent-modul** en deklarativ måde at bygge agenter, der orkestrerer disse værktøjer. `@Agent` annoteringen og `AgenticServices` lader dig definere agentadfærd via interfaces frem for imperativ kode.

I dette modul udforsker du **Supervisor Agent**-mønsteret — en avanceret agentbaseret AI tilgang, hvor en "supervisor" agent dynamisk beslutter, hvilke sub-agenter der skal påkaldes baseret på brugerens forespørgsel. Vi kombinerer begge koncepter ved at give en af vores sub-agenter MCP-drevne filadgangs-muligheder.

For at bruge agentmodulet, tilføj denne Maven-afhængighed:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Bemærk:** `langchain4j-agentic` modulet bruger en separat versionsproperty (`langchain4j.mcp.version`), da det udgives på en anden tidsplan end de øvrige LangChain4j kernebiblioteker.

> **⚠️ Eksperimentel:** `langchain4j-agentic` modulet er **eksperimentelt** og kan ændres. Den stabile måde at bygge AI-assistenter på er fortsat `langchain4j-core` med specialværktøjer (Modul 04).

## Køre eksemplerne

### Forudsætninger

- Fuldført [Modul 04 - Værktøjer](../04-tools/README.md) (dette modul bygger videre på koncepterne for specialværktøjer og sammenligner dem med MCP-værktøjer)
- `.env` fil i rodmappen med Azure-legitimationsoplysninger (oprettet af `azd up` i Modul 01)
- Java 21+, Maven 3.9+
- Node.js 16+ og npm (til MCP-servere)

> **Bemærk:** Hvis du endnu ikke har sat miljøvariabler op, se [Modul 01 - Introduktion](../01-introduction/README.md) for implementeringsinstruktioner (`azd up` opretter `.env`-filen automatisk), eller kopier `.env.example` til `.env` i rodmappen og indtast dine værdier.

## Kom hurtigt i gang

**Brug VS Code:** Højreklik blot på en hvilken som helst demofil i Explorer og vælg **"Kør Java"**, eller brug launch-konfigurationerne fra Run and Debug-panelet (sørg først for, at din `.env` fil er konfigureret med Azure-legitimationsoplysninger).

**Brug Maven:** Alternativt kan du køre fra kommandolinjen med nedenstående eksempler.

### Filoperationer (Stdio)

Dette demonstrerer værktøjer baseret på lokale subprocesses.

**✅ Ingen forudsætninger nødvendige** — MCP-serveren oprettes automatisk.

**Brug startskrifterne (Anbefalet):**

Startskrifterne indlæser miljøvariabler automatisk fra rodens `.env`-fil:

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

**Brug VS Code:** Højreklik på `StdioTransportDemo.java` og vælg **"Kør Java"** (sørg for at din `.env` fil er konfigureret).

Applikationen starter automatisk en MCP-filserverserver og læser en lokal fil. Bemærk, hvordan subprocess-styringen håndteres for dig.

**Forventet output:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor Agent

**Supervisor Agent-mønsteret** er en **fleksibel** form for agentbaseret AI. En Supervisor bruger en LLM til autonomt at beslutte, hvilke agenter der skal påkaldes baseret på brugerens anmodning. I det næste eksempel kombinerer vi MCP-drevet filadgang med en LLM-agent for at skabe en overvåget fil-læs → rapportarbejdsgang.

I demoen læser `FileAgent` en fil vha. MCP filsystemværktøjer, og `ReportAgent` genererer en struktureret rapport med et ledelsesresumé (1 sætning), 3 nøglepunkter og anbefalinger. Supervisor styrer denne arbejdsgang automatisk:

<img src="../../../translated_images/da/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Mønster" width="800"/>

*Supervisor bruger sin LLM til at beslutte, hvilke agenter der skal påkaldes og i hvilken rækkefølge — ingen hårdkodet routing nødvendig.*

Sådan ser den konkrete arbejdsgang ud for vores fil-til-rapport pipeline:

<img src="../../../translated_images/da/file-report-workflow.649bb7a896800de9.webp" alt="Fil til Rapport Arbejdsgang" width="800"/>

*FileAgent læser filen via MCP-værktøjer, hvorefter ReportAgent omdanner det rå indhold til en struktureret rapport.*

Hver agent gemmer sit output i **Agentic Scope** (delt hukommelse), hvilket giver nedstrøms agenter adgang til tidligere resultater. Dette demonstrerer, hvordan MCP-værktøjer integreres fejlfrit i agentarbejdsgange — Supervisor behøver ikke vide *hvordan* filer læses, kun at `FileAgent` kan gøre det.

#### Køre demoen

Startskrifterne indlæser miljøvariabler automatisk fra rodens `.env`-fil:

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

**Brug VS Code:** Højreklik på `SupervisorAgentDemo.java` og vælg **"Kør Java"** (sørg for at `.env` filen er konfigureret).

#### Hvordan Supervisor virker

Før du bygger agenter, skal du forbinde MCP-transporten til en klient og pakke den som en `ToolProvider`. Sådan bliver MCP-serverens værktøjer tilgængelige for dine agenter:

```java
// Opret en MCP-klient fra transporten
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Indpak klienten som en ToolProvider — dette forbinder MCP-værktøjer til LangChain4j
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

// Supervisor beslutter, hvilke agenter der skal kaldes baseret på anmodningen
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Responsstrategier

Når du konfigurerer en `SupervisorAgent`, angiver du, hvordan den skal formulere sit endelige svar til brugeren, efter sub-agenterne har afsluttet deres opgaver. Diagrammet nedenfor viser de tre tilgængelige strategier — LAST returnerer den sidste agents output direkte, SUMMARY syntetiserer alle outputs via en LLM, og SCORED vælger den, der scorer højest ift. den oprindelige forespørgsel:

<img src="../../../translated_images/da/response-strategies.3d0cea19d096bdf9.webp" alt="Responsstrategier" width="800"/>

*Tre strategier for, hvordan Supervisor formulerer sit endelige svar — vælg baseret på, om du ønsker sidste agents output, et syntetiseret sammendrag eller den bedst scorende mulighed.*

De tilgængelige strategier er:

| Strategi | Beskrivelse |
|----------|-------------|
| **LAST** | Supervisor returnerer outputtet fra den sidste sub-agent eller værktøj, der blev kaldt. Dette er nyttigt, når den sidste agent i arbejdsgangen er specifikt designet til at producere det komplette endelige svar (f.eks. en "Summary Agent" i en forskningspipeline). |
| **SUMMARY** | Supervisor bruger sin interne sprogmodel (LLM) til at syntetisere et sammendrag af hele interaktionen og alle sub-agent outputs, og returnerer derefter dette som det endelige svar. Dette giver et klart, aggregeret svar til brugeren. |
| **SCORED** | Systemet bruger en intern LLM til at score både LAST-responsen og SUMMARY af interaktionen ift. den oprindelige brugerforespørgsel, og returnerer det output, der får højest score. |
Se [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) for den komplette implementering.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) og spørg:
> - "Hvordan beslutter Supervisor, hvilke agenter der skal påkaldes?"
> - "Hvad er forskellen mellem Supervisor- og Sekventielle workflow-mønstre?"
> - "Hvordan kan jeg tilpasse Supervisors planlægningsadfærd?"

#### Forståelse af output

Når du kører demoen, vil du se en struktureret gennemgang af, hvordan Supervisor orkestrerer flere agenter. Her er, hvad hver sektion betyder:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Overskriften** introducerer workflow-konceptet: en fokuseret pipeline fra fil-læsning til rapportgenerering.

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

**Workflow-diagram** viser dataflowet mellem agenter. Hver agent har en bestemt rolle:
- **FileAgent** læser filer ved hjælp af MCP-værktøjer og gemmer rå indhold i `fileContent`
- **ReportAgent** bruger dette indhold og producerer en struktureret rapport i `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Brugerforespørgsel** viser opgaven. Supervisor parser dette og beslutter at påkalde FileAgent → ReportAgent.

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

**Supervisor-orchestrering** viser 2-trinsflowet i aktion:
1. **FileAgent** læser filen via MCP og gemmer indholdet
2. **ReportAgent** modtager indholdet og genererer en struktureret rapport

Supervisor tog disse beslutninger **autonomt** baseret på brugerens forespørgsel.

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

#### Forklaring af Agentic Modules funktioner

Eksemplet demonstrerer flere avancerede funktioner i agentic-modulet. Lad os se nærmere på Agentic Scope og Agent Listeners.

**Agentic Scope** viser det delte hukommelsesområde, hvor agenter lagrede deres resultater ved hjælp af `@Agent(outputKey="...")`. Dette giver mulighed for:
- Senere agenter kan få adgang til tidligere agenters output
- Supervisor kan syntetisere et endeligt svar
- Du kan inspicere, hvad hver agent producerede

Diagrammet nedenfor viser, hvordan Agentic Scope fungerer som delt hukommelse i file-til-rapport workflowen — FileAgent skriver sit output under nøgleordet `fileContent`, ReportAgent læser det og skriver sit eget output under `report`:

<img src="../../../translated_images/da/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope fungerer som delt hukommelse — FileAgent skriver `fileContent`, ReportAgent læser det og skriver `report`, og din kode læser det endelige resultat.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Rå fildata fra FileAgent
String report = scope.readState("report");            // Struktureret rapport fra ReportAgent
```

**Agent Listeners** muliggør overvågning og fejlfinding af agentkørsel. Outputtet, du ser trin-for-trin i demoen, kommer fra en AgentListener, der kobler sig på hver agent-påkravelse:
- **beforeAgentInvocation** - Kaldes, når Supervisor vælger en agent, så du kan se, hvilken agent der blev valgt og hvorfor
- **afterAgentInvocation** - Kaldes, når en agent er færdig, og viser dens resultat
- **inheritedBySubagents** - Når sand, overvåger listener alle agenter i hierarkiet

Følgende diagram viser den fulde Agent Listener livscyklus, inklusiv hvordan `onError` håndterer fejl under agentkørsel:

<img src="../../../translated_images/da/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners kobler sig på udførelseslivscyklussen — overvåg, når agenter starter, færdiggør eller støder på fejl.*

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
        return true; // Udbred til alle underagenter
    }
};
```

Ud over Supervisor-mønstret tilbyder `langchain4j-agentic` modulet flere kraftfulde workflow-mønstre. Diagrammet nedenfor viser alle fem — fra simple sekventielle pipelines til godkendelses-workflows med menneskelig involvering:

<img src="../../../translated_images/da/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Fem workflow-mønstre til orkestrering af agenter — fra simple sekventielle pipelines til godkendelses-workflows med menneskelig involvering.*

| Mønster | Beskrivelse | Brug |
|---------|-------------|----------|
| **Sekventiel** | Udfør agenter i rækkefølge, output flyder til næste | Pipelines: research → analyse → rapport |
| **Parallel** | Kør agenter samtidigt | Uafhængige opgaver: vejr + nyheder + aktier |
| **Loop** | Iterer indtil betingelse opfyldt | Kvalitetsscore: forbedr indtil score ≥ 0,8 |
| **Betinget** | Routing baseret på betingelser | Klassificer → send til specialistagent |
| **Human-in-the-Loop** | Tilføj menneskelige checkpoints | Godkendelses-workflows, indholdsrevision |

## Nøglebegreber

Nu hvor du har udforsket MCP og agentic-modulet i praksis, lad os opsummere, hvornår hver tilgang skal bruges.

En af MCP’s største styrker er det voksende økosystem. Diagrammet nedenfor viser, hvordan en universel protokol forbinder din AI-applikation med en bred vifte af MCP-servere — fra filsystem- og databaseadgang til GitHub, email, webscraping og mere:

<img src="../../../translated_images/da/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP skaber et universelt protokol-økosystem — enhver MCP-kompatibel server virker med enhver MCP-kompatibel klient, hvilket muliggør deling af værktøjer på tværs af applikationer.*

**MCP** er ideelt, når du vil udnytte eksisterende værktøjs-økosystemer, bygge værktøjer, som flere applikationer kan dele, integrere tredjepartstjenester med standardprotokoller eller udskifte værktøjsimplementeringer uden at ændre kode.

**Agentic-modulet** fungerer bedst, når du ønsker deklarative agentdefinitioner med `@Agent` annotationer, har brug for workflow-orchestrering (sekventiel, loop, parallel), foretrækker interface-baseret agentdesign fremfor imperativ kode, eller kombinerer flere agenter, der deler output via `outputKey`.

**Supervisor Agent-mønstret** er nyttigt, når workflowen ikke er forudsigelig og du vil lade LLM’en beslutte, når du har flere specialiserede agenter, der behøver dynamisk orkestrering, når du bygger konversationssystemer, der ruter til forskellige funktioner, eller når du ønsker den mest fleksible, adaptive agentadfærd.

For at hjælpe dig med at vælge mellem de tilpassede `@Tool` metoder fra modul 04 og MCP-værktøjer fra dette modul fremhæver følgende sammenligning de vigtigste afvejninger — tilpassede værktøjer giver tæt kobling og fuld typesikkerhed for app-specifik logik, mens MCP-værktøjer tilbyder standardiserede, genanvendelige integrationer:

<img src="../../../translated_images/da/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Hvornår du skal bruge tilpassede @Tool-metoder vs MCP-værktøjer — tilpassede værktøjer til app-specifik logik med fuld typesikkerhed, MCP-værktøjer til standardiserede integrationer der virker på tværs af applikationer.*

## Tillykke!

Du har gennemført alle fem moduler af LangChain4j for Beginners-kurset! Her er et overblik over hele læringsrejsen, du har gennemført — fra grundlæggende chat til MCP-drevne agentic-systemer:

<img src="../../../translated_images/da/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Din læringsrejse gennem alle fem moduler — fra grundlæggende chat til MCP-drevne agentic-systemer.*

Du har gennemført LangChain4j for Beginners-kurset. Du har lært:

- Hvordan man bygger konversations-AI med hukommelse (modul 01)
- Prompt engineering mønstre til forskellige opgaver (modul 02)
- At fundere svar i dine dokumenter med RAG (modul 03)
- At skabe grundlæggende AI-agenter (assistenter) med tilpassede værktøjer (modul 04)
- At integrere standardiserede værktøjer med LangChain4j MCP og Agentic modulerne (modul 05)

### Hvad nu?

Efter at have gennemført modulerne, udforsk [Testing Guide](../docs/TESTING.md) for at se LangChain4j testkoncepter i praksis.

**Officielle ressourcer:**
- [LangChain4j Dokumentation](https://docs.langchain4j.dev/) - Omfattende guides og API-reference
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Kildekode og eksempler
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - Trin-for-trin tutorials til forskellige brugstilfælde

Tak fordi du gennemførte dette kursus!

---

**Navigation:** [← Forrige: Modul 04 - Værktøjer](../04-tools/README.md) | [Tilbage til hoved](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, skal du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det oprindelige dokument på dets oprindelige sprog bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for misforståelser eller fejltolkninger, der opstår som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->