# Module 05: Model Context Protocol (MCP)

## Inhoudsopgave

- [Wat je zult leren](../../../05-mcp)
- [Wat is MCP?](../../../05-mcp)
- [Hoe MCP werkt](../../../05-mcp)
- [De Agentic Module](../../../05-mcp)
- [De voorbeelden uitvoeren](../../../05-mcp)
  - [Vereisten](../../../05-mcp)
- [Snel aan de slag](../../../05-mcp)
  - [Bestandsbewerkingen (Stdio)](../../../05-mcp)
  - [Supervisor Agent](../../../05-mcp)
    - [De demo uitvoeren](../../../05-mcp)
    - [Hoe de Supervisor werkt](../../../05-mcp)
    - [Hoe FileAgent MCP-tools runtime ontdekt](../../../05-mcp)
    - [Reactiestrategieën](../../../05-mcp)
    - [De uitvoer begrijpen](../../../05-mcp)
    - [Uitleg van Agentic Module functies](../../../05-mcp)
- [Belangrijke concepten](../../../05-mcp)
- [Gefeliciteerd!](../../../05-mcp)
  - [Wat is de volgende stap?](../../../05-mcp)

## Wat je zult leren

Je hebt al gespreks-AI gebouwd, prompts onder de knie gekregen, reacties aan documenten gekoppeld en agenten gecreëerd met tools. Maar al die tools waren maatwerk voor jouw specifieke toepassing. Wat als je je AI toegang kon geven tot een gestandaardiseerd ecosysteem van tools die door iedereen gemaakt en gedeeld kunnen worden? In deze module leer je precies dat doen met het Model Context Protocol (MCP) en LangChain4j’s agentic module. We laten eerst een eenvoudige MCP-bestandlezer zien en tonen daarna hoe deze gemakkelijk integreert in geavanceerde agentic workflows met het Supervisor Agent-patroon.

## Wat is MCP?

Het Model Context Protocol (MCP) biedt precies dat — een standaardmanier voor AI-toepassingen om externe tools te ontdekken en te gebruiken. In plaats van maatwerkintegraties voor elke gegevensbron of service te schrijven, verbind je met MCP-servers die hun mogelijkheden in een consistent formaat aanbieden. Je AI-agent kan die tools dan automatisch ontdekken en gebruiken.

Het onderstaande diagram toont het verschil — zonder MCP vereist elke integratie maatwerkaansluitingen; met MCP verbindt één protocol je app met elke tool:

<img src="../../../translated_images/nl/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Voor MCP: Complexe punt-naar-punt integraties. Na MCP: Eén protocol, eindeloze mogelijkheden.*

MCP lost een fundamenteel probleem in AI-ontwikkeling op: elke integratie is maatwerk. Wil je GitHub benaderen? Maatwerkcode. Wil je bestanden lezen? Maatwerkcode. Wil je een database queryen? Maatwerkcode. En geen van deze integraties werkt met andere AI-applicaties.

MCP standaardiseert dit. Een MCP-server biedt tools aan met duidelijke beschrijvingen en schema’s. Elke MCP-client kan verbinden, beschikbare tools ontdekken en ze gebruiken. Een keer bouwen, overal gebruiken.

Het onderstaande diagram illustreert deze architectuur — een enkele MCP-client (jouw AI-toepassing) maakt verbinding met meerdere MCP-servers, die elk hun eigen set tools aanbieden via het standaardprotocol:

<img src="../../../translated_images/nl/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol architectuur – gestandaardiseerde toolontdekking en uitvoering*

## Hoe MCP werkt

Onder de motorkap gebruikt MCP een gelaagde architectuur. Je Java-applicatie (de MCP-client) ontdekt beschikbare tools, verzendt JSON-RPC-aanvragen via een transportlaag (Stdio of HTTP), en de MCP-server voert bewerkingen uit en retourneert resultaten. Het volgende diagram verdeelt elke laag van dit protocol:

<img src="../../../translated_images/nl/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Hoe MCP onder de motorkap werkt — clients ontdekken tools, wisselen JSON-RPC-berichten uit, en voeren bewerkingen uit via een transportlaag.*

**Server-client architectuur**

MCP gebruikt een client-server model. Servers bieden tools aan — bestanden lezen, databases queryen, API’s aanroepen. Clients (jouw AI-toepassing) verbinden met servers en gebruiken hun tools.

Om MCP met LangChain4j te gebruiken, voeg deze Maven-afhankelijkheid toe:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Toolontdekking**

Wanneer je client connect met een MCP-server, vraagt deze "Welke tools heb je?" De server reageert met een lijst van beschikbare tools, elk met beschrijvingen en parameterschema’s. Je AI-agent kan dan beslissen welke tools te gebruiken op basis van gebruikersverzoeken. Het onderstaande diagram toont deze handdruk — de client verstuurt een `tools/list`-verzoek en de server geeft zijn beschikbare tools terug met beschrijvingen en parameterschema’s:

<img src="../../../translated_images/nl/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*De AI ontdekt beschikbare tools bij het opstarten — weet nu welke mogelijkheden er zijn en kan beslissen welke te gebruiken.*

**Transportmechanismen**

MCP ondersteunt verschillende transportmechanismen. De twee opties zijn Stdio (voor lokale subprocess-communicatie) en Streamable HTTP (voor externe servers). Deze module demonstreert het Stdio-transport:

<img src="../../../translated_images/nl/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP transportmechanismen: HTTP voor externe servers, Stdio voor lokale processen*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Voor lokale processen. Je applicatie start een server als subprocess en communiceert via standaardinvoer/-uitvoer. Handig voor toegang tot het bestandssysteem of commandoregeltools.

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

De `@modelcontextprotocol/server-filesystem`-server biedt de volgende tools, allemaal gesandboxed op de door jou gespecificeerde mappen:

| Tool | Beschrijving |
|------|--------------|
| `read_file` | Lees de inhoud van één enkel bestand |
| `read_multiple_files` | Lees meerdere bestanden in één oproep |
| `write_file` | Maak een bestand aan of overschrijf een bestand |
| `edit_file` | Voer gerichte zoek-en-vervang-bewerkingen uit |
| `list_directory` | Lijst bestanden en mappen op een pad |
| `search_files` | Zoek recursief bestanden die aan een patroon voldoen |
| `get_file_info` | Verkrijg bestandsmetadata (grootte, tijdstempels, permissies) |
| `create_directory` | Maak een map aan (inclusief oudermappen) |
| `move_file` | Verplaats of hernoem een bestand of map |

Het volgende diagram toont hoe Stdio-transport werkt tijdens runtime — je Java-applicatie start de MCP-server als child-proces en communiceert via stdin/stdout-pijpen, zonder netwerk of HTTP:

<img src="../../../translated_images/nl/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio-transport in actie — je applicatie start de MCP-server als child-proces en communiceert via stdin/stdout-pijpen.*

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) en vraag:
> - "Hoe werkt Stdio-transport en wanneer moet ik het gebruiken in plaats van HTTP?"
> - "Hoe beheert LangChain4j de levenscyclus van gegenereerde MCP-serverprocessen?"
> - "Wat zijn de veiligheidsimplicaties van AI toegang geven tot het bestandssysteem?"

## De Agentic Module

Hoewel MCP gestandaardiseerde tools biedt, biedt LangChain4j's **agentic module** een declaratieve manier om agenten te bouwen die deze tools orkestreren. De `@Agent`-annotatie en `AgenticServices` laten je agentgedrag definiëren via interfaces, in plaats van imperatieve code.

In deze module verken je het **Supervisor Agent**-patroon — een geavanceerde agentic AI-benadering waarbij een “supervisor” agent dynamisch beslist welke subagenten worden uitgevoerd, gebaseerd op gebruikersverzoeken. We combineren beide concepten door één van onze subagenten MCP-gestuurde bestandsaccess-mogelijkheden te geven.

Om de agentic module te gebruiken, voeg volgende Maven-afhankelijkheid toe:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Opmerking:** De `langchain4j-agentic` module gebruikt een aparte versie-eigenschap (`langchain4j.mcp.version`) omdat die op een ander schema uitgebracht wordt dan de kernbibliotheken van LangChain4j.

> **⚠️ Experimenteel:** De `langchain4j-agentic` module is **experimenteel** en kan wijzigen. De stabiele manier om AI-assistenten te bouwen blijft `langchain4j-core` met aangepaste tools (Module 04).

## De voorbeelden uitvoeren

### Vereisten

- Module [Module 04 - Tools](../04-tools/README.md) afgerond (deze module bouwt voort op het concept van aangepaste tools en vergelijkt die met MCP-tools)
- `.env`-bestand in de rootmap met Azure-gegevens (gemaakt door `azd up` in Module 01)
- Java 21+, Maven 3.9+
- Node.js 16+ en npm (voor MCP-servers)

> **Opmerking:** Als je de omgevingsvariabelen nog niet hebt ingesteld, zie [Module 01 - Introductie](../01-introduction/README.md) voor deployinstructies (`azd up` maakt automatisch het `.env` bestand aan), of kopieer `.env.example` naar `.env` in de rootmap en vul je waarden in.

## Snel aan de slag

**Met VS Code:** Rechtsklik eenvoudig op een demo-bestand in de Explorer en selecteer **"Run Java"**, of gebruik de launch-configuraties in het Run and Debug-paneel (zorg eerst dat je `.env` bestand met Azure-gegevens is geconfigureerd).

**Met Maven:** Je kunt ook vanuit de opdrachtregel draaien met onderstaande voorbeelden.

### Bestandsbewerkingen (Stdio)

Dit demonstreert tools gebaseerd op lokale subprocesses.

**✅ Geen vereisten nodig** - de MCP-server wordt automatisch gestart.

**Gebruik de start scripts (aanbevolen):**

De start scripts laden automatisch de omgevingsvariabelen uit het root `.env` bestand:

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

**Met VS Code:** Rechtsklik op `StdioTransportDemo.java` en selecteer **"Run Java"** (zorg dat je `.env` bestand geconfigureerd is).

De applicatie start automatisch een filesystem MCP-server en leest een lokaal bestand. Let op hoe het beheer van het subprocess voor je geregeld is.

**Verwachte uitvoer:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor Agent

Het **Supervisor Agent-patroon** is een **flexibele** vorm van agentic AI. Een Supervisor gebruikt een LLM om autonoom te beslissen welke agenten moeten worden aangestuurd op basis van de gebruikersaanvraag. In het volgende voorbeeld combineren we MCP-gestuurde bestandsaccess met een LLM-agent om een gecontroleerde workflow “bestand lezen → rapport maken” te creëren.

In de demo leest `FileAgent` een bestand via MCP-bestandssysteemtools, en `ReportAgent` genereert een gestructureerd rapport met een executive summary (1 zin), 3 kernpunten en aanbevelingen. De Supervisor orkestreert deze flow automatisch:

<img src="../../../translated_images/nl/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*De Supervisor gebruikt zijn LLM om te bepalen welke agenten worden aangestuurd en in welke volgorde — geen hardcoded routing nodig.*

Zo ziet de concrete workflow eruit in onze bestands-naar-rapport pijplijn:

<img src="../../../translated_images/nl/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent leest het bestand via MCP-tools, daarna transformeert ReportAgent de ruwe inhoud naar een gestructureerd rapport.*

Het volgende sequentiediagram volgt de volledige Supervisor-orkestratie — van het starten van de MCP-server, via de autonome agentselectie van de Supervisor, naar tool-aanroepen over stdio en het eindrapport:

<img src="../../../translated_images/nl/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*De Supervisor roept autonoom FileAgent aan (die MCP-server via stdio oproept om het bestand te lezen), daarna ReportAgent om een gestructureerd rapport te genereren — elke agent bewaart zijn output in de gedeelde Agentic Scope.*

Elke agent slaat zijn output op in de **Agentic Scope** (gedeeld geheugen), zodat downstream-agenten eerdere resultaten kunnen gebruiken. Dit toont hoe MCP-tools naadloos in agentic workflows integreren — de Supervisor hoeft niet te weten *hoe* bestanden worden gelezen, alleen dat `FileAgent` dat kan.

#### De demo uitvoeren

De start scripts laden automatisch de omgevingsvariabelen uit het root `.env` bestand:

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

**Met VS Code:** Rechtsklik op `SupervisorAgentDemo.java` en selecteer **"Run Java"** (zorg dat je `.env` bestand geconfigureerd is).

#### Hoe de Supervisor werkt

Voordat je agenten bouwt, moet je de MCP-transport koppelen aan een client en deze wikkelen als een `ToolProvider`. Zo worden de MCP-servertools beschikbaar voor je agenten:

```java
// Maak een MCP-client van de transportlaag
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Wikkel de client in als een ToolProvider — dit verbindt MCP-tools met LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Nu kun je `mcpToolProvider` injecteren in elke agent die MCP-tools nodig heeft:

```java
// Stap 1: FileAgent leest bestanden met MCP-tools
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Heeft MCP-tools voor bestandsbewerkingen
        .build();

// Stap 2: ReportAgent genereert gestructureerde rapporten
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor coördineert de workflow van bestand → rapport
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Retourneer het definitieve rapport
        .build();

// De Supervisor bepaalt welke agents worden aangeroepen op basis van het verzoek
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Hoe FileAgent MCP-tools runtime ontdekt

Je vraagt je misschien af: **hoe weet `FileAgent` hoe het npm filesystemtools moet gebruiken?** Het antwoord is: dat doet hij niet — de **LLM** ontdekt dit tijdens runtime via toolschema’s.

De interface van `FileAgent` is slechts een **promptdefinitie**. Het heeft geen harde kennis van `read_file`, `list_directory`, of andere MCP-tools. Zo verloopt het eind-tot-eind proces:
1. **Server start:** `StdioMcpTransport` start het `@modelcontextprotocol/server-filesystem` npm-pakket als een kindproces  
2. **Tooldetectie:** De `McpClient` stuurt een `tools/list` JSON-RPC-verzoek naar de server, die reageert met toolnamen, beschrijvingen en parameterschema's (bijv. `read_file` — *"Lees de volledige inhoud van een bestand"* — `{ path: string }`)  
3. **Schema-injectie:** `McpToolProvider` wikkelt deze ontdekte schema's in en maakt ze beschikbaar voor LangChain4j  
4. **LLM beslist:** Wanneer `FileAgent.readFile(path)` wordt aangeroepen, stuurt LangChain4j het systeembericht, gebruikersbericht, **en de lijst met toolschema's** naar de LLM. De LLM leest de toolbeschrijvingen en genereert een toolaanroep (bijv. `read_file(path="/some/file.txt")`)  
5. **Uitvoering:** LangChain4j onderschept de toolaanroep, leidt deze via de MCP-client terug naar het Node.js-subproces, ontvangt het resultaat en geeft dit terug aan de LLM  

Dit is hetzelfde [Tool Discovery](../../../05-mcp) mechanisme dat hierboven is beschreven, maar dan toegepast op de agentworkflow. De `@SystemMessage` en `@UserMessage` annotaties sturen het gedrag van de LLM, terwijl de geïnjecteerde `ToolProvider` het **vermogen** geeft — de LLM brengt het tweetal realtime samen.  

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) en vraag:  
> - "Hoe weet deze agent welke MCP-tool hij moet aanroepen?"  
> - "Wat gebeurt er als ik de ToolProvider uit de agent builder verwijder?"  
> - "Hoe worden toolschema's doorgegeven aan de LLM?"  

#### Responsstrategieën  

Wanneer je een `SupervisorAgent` configureert, geef je op hoe deze het definitieve antwoord aan de gebruiker formuleert nadat de sub-agenten hun taken hebben voltooid. Het onderstaande diagram toont de drie beschikbare strategieën — LAST geeft direct de output terug van de laatste agent, SUMMARY synthetiseert alle outputs via een LLM, en SCORED kiest degene met de hoogste score ten opzichte van het oorspronkelijke verzoek:  

<img src="../../../translated_images/nl/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>  

*Drie strategieën voor hoe de Supervisor zijn definitieve respons formuleert — kies op basis van of je output van de laatste agent, een samengevat antwoord, of de best scorende optie wilt.*  

De beschikbare strategieën zijn:  

| Strategie | Beschrijving |  
|----------|-------------|  
| **LAST** | De supervisor retourneert de output van de laatst aangeroepen sub-agent of tool. Dit is nuttig wanneer de laatste agent in de workflow specifiek is ontworpen om het volledige, definitieve antwoord te produceren (bijv. een "Summary Agent" in een onderzoeksworkflow). |  
| **SUMMARY** | De supervisor gebruikt haar eigen interne Language Model (LLM) om een samenvatting te synthetiseren van de gehele interactie en alle output van sub-agenten, en retourneert deze samenvatting als het definitieve antwoord. Dit biedt een helder, geaggregeerd antwoord aan de gebruiker. |  
| **SCORED** | Het systeem gebruikt een interne LLM om zowel de LAST-respons als de SUMMARY van de interactie te scoren tegenover het oorspronkelijke gebruikersverzoek, en retourneert de output met de hoogste score. |  

Zie [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) voor de volledige implementatie.  

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) en vraag:  
> - "Hoe bepaalt de Supervisor welke agenten worden aangeroepen?"  
> - "Wat is het verschil tussen Supervisor en Sequential workflowpatronen?"  
> - "Hoe kan ik het planningsgedrag van de Supervisor aanpassen?"  

#### Het begrijpen van de output  

Als je de demo uitvoert, zie je een gestructureerde walkthrough van hoe de Supervisor meerdere agenten orkestreert. Dit is wat elk onderdeel betekent:  

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**De kop** introduceert het workflowconcept: een gefocuste pijplijn van het lezen van een bestand tot het genereren van een rapport.  

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
  
**Workflowdiagram** toont de gegevensstroom tussen agenten. Elke agent heeft een specifieke rol:  
- **FileAgent** leest bestanden met MCP-tools en slaat de ruwe inhoud op in `fileContent`  
- **ReportAgent** gebruikt die inhoud en maakt een gestructureerd rapport in `report`  

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Gebruikersverzoek** toont de taak. De Supervisor analyseert dit en besluit FileAgent → ReportAgent aan te roepen.  

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
  
**Supervisor Orkestratie** toont de 2-staps flow in actie:  
1. **FileAgent** leest het bestand via MCP en slaat de inhoud op  
2. **ReportAgent** ontvangt de inhoud en genereert een gestructureerd rapport  

De Supervisor nam deze beslissingen **autonoom** op basis van het gebruikersverzoek.  

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
  
#### Toelichting op Agentic Module-functies  

Het voorbeeld demonstreert diverse geavanceerde functies van de agentic module. Laten we Agentic Scope en Agent Listeners wat nader bekijken.  

**Agentic Scope** toont het gedeelde geheugen waar agenten hun resultaten opslaan met `@Agent(outputKey="...")`. Dit maakt het mogelijk om:  
- Latere agenten toegang te geven tot de output van eerdere agenten  
- De Supervisor een eindantwoord te laten synthetiseren  
- Voor jou om te inspecteren wat elke agent produceerde  

Het onderstaande diagram toont hoe Agentic Scope werkt als gedeeld geheugen in de file-to-report workflow — FileAgent schrijft zijn output onder de sleutel `fileContent`, ReportAgent leest die en schrijft zijn eigen output onder `report`:  

<img src="../../../translated_images/nl/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>  

*Agentic Scope werkt als gedeeld geheugen — FileAgent schrijft `fileContent`, ReportAgent leest het en schrijft `report`, en jouw code leest het eindresultaat.*  

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Ruwe bestandsgegevens van FileAgent
String report = scope.readState("report");            // Gestructureerd rapport van ReportAgent
```
  
**Agent Listeners** maken het mogelijk om agentuitvoering te monitoren en debuggen. De stapsgewijze output die je in de demo ziet, komt van een AgentListener die zich koppelt aan elke agentaanroep:  
- **beforeAgentInvocation** — Wordt aangeroepen wanneer de Supervisor een agent selecteert, zodat je kunt zien welke agent is gekozen en waarom  
- **afterAgentInvocation** — Wordt aangeroepen wanneer een agent klaar is en toont het resultaat  
- **inheritedBySubagents** — Wanneer waar, monitort de listener alle agenten in de hiërarchie  

Het volgende diagram toont de volledige levenscyclus van de Agent Listener, inclusief hoe `onError` fouten afhandelt tijdens agentuitvoering:  

<img src="../../../translated_images/nl/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>  

*Agent Listeners koppelen zich aan de uitvoeringslevenscyclus — monitor wanneer agenten starten, afronden of fouten tegenkomen.*  

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
        return true; // Propageren naar alle sub-agenten
    }
};
```
  
Naast het Supervisor-patroon biedt de `langchain4j-agentic` module verschillende krachtige workflowpatronen. Het onderstaande diagram toont ze allemaal — van eenvoudige sequentiële pijplijnen tot human-in-the-loop goedkeuringsworkflows:  

<img src="../../../translated_images/nl/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>  

*Vijf workflowpatronen voor het orkestreren van agenten — van eenvoudige sequentiële pijplijnen tot human-in-the-loop goedkeuringsworkflows.*  

| Patroon | Beschrijving | Gebruik |  
|---------|-------------|---------|  
| **Sequential** | Voer agenten achtereenvolgens uit, output stroomt door naar de volgende | Pijplijnen: onderzoek → analyse → rapport |  
| **Parallel** | Voer agenten gelijktijdig uit | Onafhankelijke taken: weer + nieuws + aandelen |  
| **Loop** | Itereer totdat een conditie is voldaan | Kwaliteitsscore: verfijnen totdat score ≥ 0,8 |  
| **Conditional** | Routeer op basis van voorwaarden | Classificeer → routeer naar specialistagent |  
| **Human-in-the-Loop** | Voeg menselijke checkpoints toe | Goedkeuringsworkflows, content review |  

## Kernconcepten  

Nu je MCP en de agentic module in actie hebt verkend, laten we samenvatten wanneer je welke aanpak het beste kunt gebruiken.  

Een groot voordeel van MCP is het groeiende ecosysteem. Het onderstaande diagram toont hoe een universeel protocol jouw AI-applicatie verbindt met een grote verscheidenheid aan MCP-servers — van bestandssysteem- en database-access tot GitHub, e-mail, webscraping en meer:  

<img src="../../../translated_images/nl/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>  

*MCP creëert een universeel protocolecosysteem — elke MCP-compatibele server werkt met elke MCP-compatibele client, waardoor toolsharing tussen applicaties mogelijk wordt.*  

**MCP** is ideaal wanneer je bestaande toolecosystemen wilt benutten, tools wilt bouwen die door meerdere applicaties gedeeld kunnen worden, derde-partijdiensten wilt integreren met standaardprotocollen, of toolimplementaties wil wisselen zonder code te wijzigen.  

**De Agentic Module** werkt het best als je declaratieve agentdefinities wilt met `@Agent`-annotaties, workfloworkestratie nodig hebt (sequentieel, loop, parallel), de voorkeur geeft aan interface-gebaseerd agentontwerp boven imperatieve code, of meerdere agenten combineert die output delen via `outputKey`.  

**Het Supervisor Agent-patroon** blinkt uit wanneer de workflow vooraf niet voorspelbaar is en je wilt dat de LLM beslist, wanneer je meerdere gespecialiseerde agenten hebt die dynamische orkestratie nodig hebben, bij het bouwen van conversatiesystemen die naar verschillende capaciteiten routeren, of wanneer je het meest flexibele, adaptieve agentgedrag wilt.  

Om je te helpen kiezen tussen de aangepaste `@Tool`-methoden uit Module 04 en MCP-tools uit deze module, benadrukt de volgende vergelijking de belangrijkste afwegingen — aangepaste tools bieden nauwe koppeling en volledige typesafety voor app-specifieke logica, terwijl MCP-tools gestandaardiseerde, herbruikbare integraties bieden:  

<img src="../../../translated_images/nl/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>  

*Wanneer gebruik je aangepaste @Tool-methoden vs MCP-tools — aangepaste tools voor app-specifieke logica met volledige typesafety, MCP-tools voor gestandaardiseerde integraties die tussen applicaties werken.*  

## Gefeliciteerd!  

Je hebt alle vijf modules van de LangChain4j for Beginners cursus doorlopen! Hier zie je de volledige leerreis die je hebt afgelegd — van basischat tot MCP-aangedreven agentic systemen:  

<img src="../../../translated_images/nl/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>  

*Je leerreis door alle vijf modules — van basischat tot MCP-aangedreven agentic systemen.*  

Je hebt de LangChain4j for Beginners cursus afgerond. Je hebt geleerd:  

- Hoe je conversatie-AI met geheugen opbouwt (Module 01)  
- Prompt engineering patronen voor verschillende taken (Module 02)  
- Hoe je antwoorden grondt in je documenten met RAG (Module 03)  
- Het maken van basis AI-agenten (assistenten) met aangepaste tools (Module 04)  
- Het integreren van gestandaardiseerde tools met de LangChain4j MCP en Agentic modules (Module 05)  

### Wat nu?  

Na het doorlopen van de modules kun je de [Testing Guide](../docs/TESTING.md) verkennen om LangChain4j testconcepten in actie te zien.  

**Officiële bronnen:**  
- [LangChain4j Documentatie](https://docs.langchain4j.dev/) - Uitgebreide gidsen en API-referentie  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Broncode en voorbeelden  
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - Stapsgewijze tutorials voor diverse use cases  

Bedankt voor het afronden van deze cursus!  

---  

**Navigatie:** [← Vorige: Module 04 - Tools](../04-tools/README.md) | [Terug naar Hoofdmenu](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI-vertalingsdienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u zich ervan bewust te zijn dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal wordt beschouwd als de gezaghebbende bron. Voor cruciale informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor enige misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->