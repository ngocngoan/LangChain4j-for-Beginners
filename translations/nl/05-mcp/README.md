# Module 05: Model Context Protocol (MCP)

## Inhoudsopgave

- [Wat Je Zal Leren](../../../05-mcp)
- [Wat is MCP?](../../../05-mcp)
- [Hoe MCP Werkt](../../../05-mcp)
- [De Agentic Module](../../../05-mcp)
- [De Voorbeelden Uitvoeren](../../../05-mcp)
  - [Vereisten](../../../05-mcp)
- [Snelle Start](../../../05-mcp)
  - [Bestandsbewerkingen (Stdio)](../../../05-mcp)
  - [Supervisor Agent](../../../05-mcp)
    - [De Demo Uitvoeren](../../../05-mcp)
    - [Hoe de Supervisor Werkt](../../../05-mcp)
    - [Responsstrategieën](../../../05-mcp)
    - [De Output Begrijpen](../../../05-mcp)
    - [Uitleg van Agentic Module Functies](../../../05-mcp)
- [Kernconcepten](../../../05-mcp)
- [Gefeliciteerd!](../../../05-mcp)
  - [Wat Nu?](../../../05-mcp)

## Wat Je Zal Leren

Je hebt conversatie-AI gebouwd, hebt prompts beheerst, antwoorden gebaseerd op documenten gegeven en agents met tools gemaakt. Maar al die tools waren specifiek gebouwd voor jouw toepassing. Wat als je je AI toegang kon geven tot een gestandaardiseerd ecosysteem van tools die iedereen kan maken en delen? In deze module leer je precies dat met het Model Context Protocol (MCP) en LangChain4j's agentic module. We tonen eerst een eenvoudige MCP-bestandlezer en laten vervolgens zien hoe die gemakkelijk integreert in geavanceerde agentic workflows met behulp van het Supervisor Agent patroon.

## Wat is MCP?

Het Model Context Protocol (MCP) biedt precies dat - een standaard manier voor AI-toepassingen om externe tools te ontdekken en te gebruiken. In plaats van aangepaste integraties te schrijven voor elke datasource of service, verbind je met MCP-servers die hun functionaliteiten in een consistent formaat beschikbaar stellen. Je AI-agent kan dan deze tools automatisch ontdekken en gebruiken.

De onderstaande afbeelding toont het verschil — zonder MCP vereist elke integratie maatwerkpunt-naar-punt aansluitingen; met MCP verbindt één protocol jouw app met elke tool:

<img src="../../../translated_images/nl/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Voor MCP: complexe punt-naar-punt integraties. Na MCP: één protocol, eindeloze mogelijkheden.*

MCP lost een fundamenteel probleem in AI-ontwikkeling op: elke integratie is maatwerk. Wil je toegang tot GitHub? Maatwerk code. Wil je bestanden lezen? Maatwerk code. Wil je een database bevragen? Maatwerk code. En geen van deze integraties werkt met andere AI-toepassingen.

MCP standaardiseert dit. Een MCP-server stelt tools beschikbaar met duidelijke beschrijvingen en schemas. Elke MCP-client kan verbinden, beschikbare tools ontdekken en gebruiken. Eén keer bouwen, overal gebruiken.

De afbeelding hieronder illustreert deze architectuur — één MCP-client (jouw AI-applicatie) verbindt met meerdere MCP-servers, elk met hun eigen set tools via het standaardprotocol:

<img src="../../../translated_images/nl/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol architectuur - gestandaardiseerde tooldiscovery en uitvoering*

## Hoe MCP Werkt

Onder de motorkap gebruikt MCP een gelaagde architectuur. Je Java-applicatie (de MCP-client) ontdekt beschikbare tools, verzendt JSON-RPC verzoeken via een transportlaag (Stdio of HTTP), en de MCP-server voert operaties uit en retourneert resultaten. Het volgende diagram verdeelt elke laag van dit protocol:

<img src="../../../translated_images/nl/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Hoe MCP werkt onder de motorkap — clients ontdekken tools, wisselen JSON-RPC berichten uit, en voeren operaties uit via een transportlaag.*

**Server-Client Architectuur**

MCP gebruikt een client-server model. Servers bieden tools - bestanden lezen, databases bevragen, API's aanroepen. Clients (jouw AI-applicatie) verbinden met servers en gebruiken hun tools.

Om MCP met LangChain4j te gebruiken, voeg deze Maven dependency toe:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Tool Discovery**

Wanneer je client verbindt met een MCP-server, vraagt het "Welke tools heb je?" De server reageert met een lijst van beschikbare tools, elk met beschrijvingen en parameterschema's. Je AI-agent kan dan beslissen welke tools te gebruiken op basis van gebruikersverzoeken. Het diagram hieronder toont deze handdruk — de client stuurt een `tools/list` verzoek en de server retourneert de beschikbare tools met beschrijvingen en parameterschema's:

<img src="../../../translated_images/nl/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*De AI ontdekt beschikbare tools bij opstart — het weet nu welke functionaliteiten beschikbaar zijn en kan beslissen welke te gebruiken.*

**Transport Mechanismen**

MCP ondersteunt verschillende transportmechanismen. De twee opties zijn Stdio (voor lokale subprocess communicatie) en Streamable HTTP (voor remote servers). Deze module demonstreert de Stdio transport:

<img src="../../../translated_images/nl/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP transportmechanismen: HTTP voor remote servers, Stdio voor lokale processen*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Voor lokale processen. Je applicatie start een server als subprocess op en communiceert via standaard input/output. Handig voor toegang tot bestandssystemen of command line tools.

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

De `@modelcontextprotocol/server-filesystem` server stelt de volgende tools beschikbaar, allemaal geïsoleerd tot de mappen die je opgeeft:

| Tool | Beschrijving |
|------|--------------|
| `read_file` | Lees de inhoud van een enkel bestand |
| `read_multiple_files` | Lees meerdere bestanden in één oproep |
| `write_file` | Maak een bestand aan of overschrijf het |
| `edit_file` | Voer gerichte zoek-en-vervangbewerkingen uit |
| `list_directory` | Lijst bestanden en mappen op een pad |
| `search_files` | Zoek recursief naar bestanden die aan een patroon voldoen |
| `get_file_info` | Verkrijg metadata van een bestand (grootte, tijden, permissies) |
| `create_directory` | Maak een map aan (inclusief bovenliggende mappen) |
| `move_file` | Verplaats of hernoem een bestand of map |

Het onderstaande diagram toont hoe Stdio transport tijdens runtime werkt — je Java-applicatie start de MCP-server als child process en ze communiceren via stdin/stdout pijpen, zonder netwerk of HTTP:

<img src="../../../translated_images/nl/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio transport in actie — jouw applicatie start de MCP-server als child process en communiceert via stdin/stdout pijpen.*

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) en vraag:
> - "Hoe werkt Stdio transport en wanneer moet ik het gebruiken versus HTTP?"
> - "Hoe beheert LangChain4j de levenscyclus van opgestarte MCP serverprocessen?"
> - "Wat zijn de beveiligingsimplicaties van het geven van AI-toegang tot het bestandssysteem?"

## De Agentic Module

Terwijl MCP gestandaardiseerde tools biedt, levert LangChain4j's **agentic module** een declaratieve manier om agents te bouwen die deze tools orkestreren. De `@Agent` annotatie en `AgenticServices` laten je gedrag van agents definiëren via interfaces in plaats van imperatieve code.

In deze module onderzoek je het **Supervisor Agent** patroon — een geavanceerde agentic AI-benadering waarbij een "supervisor"-agent dynamisch beslist welke sub-agents aan te roepen zijn op basis van gebruikersverzoeken. We combineren beide concepten door een van onze sub-agents MCP-gestuurde bestandsaccess te geven.

Om de agentic module te gebruiken, voeg deze Maven dependency toe:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Opmerking:** De `langchain4j-agentic` module gebruikt een aparte versie-eigenschap (`langchain4j.mcp.version`) omdat deze op een ander schema wordt uitgebracht dan de kern LangChain4j bibliotheken.

> **⚠️ Experimenteel:** De `langchain4j-agentic` module is **experimenteel** en kan veranderen. De stabiele manier om AI-assistenten te bouwen blijft `langchain4j-core` met aangepaste tools (Module 04).

## De Voorbeelden Uitvoeren

### Vereisten

- Voltooide [Module 04 - Tools](../04-tools/README.md) (deze module bouwt voort op concepten van aangepaste tools en vergelijkt deze met MCP tools)
- `.env` bestand in de hoofdmap met Azure-credentials (gemaakt door `azd up` in Module 01)
- Java 21+, Maven 3.9+
- Node.js 16+ en npm (voor MCP servers)

> **Opmerking:** Als je je omgevingsvariabelen nog niet hebt ingesteld, zie [Module 01 - Introductie](../01-introduction/README.md) voor implementatie-instructies (`azd up` maakt het `.env` bestand automatisch aan), of kopieer `.env.example` naar `.env` in de hoofdmap en vul je waarden in.

## Snelle Start

**Met VS Code:** Klik met de rechtermuisknop op een demobestand in de Explorer en selecteer **"Run Java"**, of gebruik de launch-configuraties vanuit het Run and Debug paneel (zorg dat je `.env` bestand eerst is geconfigureerd met Azure-credentials).

**Met Maven:** Je kunt ook vanaf de commandoregel uitvoeren met onderstaande voorbeelden.

### Bestandsbewerkingen (Stdio)

Dit demonstreert tools gebaseerd op lokale subprocessen.

**✅ Geen vereisten nodig** - de MCP-server wordt automatisch gestart.

**Met de Start-scripts (Aanbevolen):**

De start-scripts laden automatisch omgevingsvariabelen uit het root `.env` bestand:

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

**Met VS Code:** Klik met de rechtermuisknop op `StdioTransportDemo.java` en selecteer **"Run Java"** (zorg dat je `.env` bestand is geconfigureerd).

De applicatie start automatisch een filesystem MCP-server op en leest een lokaal bestand. Merk op hoe het beheer van het subprocess voor jou wordt geregeld.

**Verwachte output:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor Agent

Het **Supervisor Agent patroon** is een **flexibele** vorm van agentic AI. Een Supervisor gebruikt een LLM om autonoom te beslissen welke agents te activeren zijn gebaseerd op het verzoek van de gebruiker. In het volgende voorbeeld combineren we MCP-gestuurde bestandsaccess met een LLM-agent om een gecoördineerde file lezen → rapport workflow te maken.

In de demo leest `FileAgent` een bestand met MCP filesystem tools, en `ReportAgent` genereert een gestructureerd rapport met een executive summary (1 zin), 3 kernpunten, en aanbevelingen. De Supervisor coördineert deze flow automatisch:

<img src="../../../translated_images/nl/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*De Supervisor gebruikt zijn LLM om te beslissen welke agents te activeren en in welke volgorde — geen vaste routering nodig.*

Zo ziet de concrete workflow eruit voor onze bestands-naar-rapport pijplijn:

<img src="../../../translated_images/nl/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent leest het bestand via MCP-tools, daarna transformeert ReportAgent de ruwe inhoud in een gestructureerd rapport.*

Elke agent slaat zijn output op in de **Agentic Scope** (gedeeld geheugen), waardoor volgende agents toegang hebben tot eerdere resultaten. Dit laat zien hoe MCP tools naadloos integreren in agentic workflows — de Supervisor hoeft niet te weten *hoe* bestanden worden gelezen, alleen dat `FileAgent` dat kan.

#### De Demo Uitvoeren

De start-scripts laden automatisch omgevingsvariabelen uit het root `.env` bestand:

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

**Met VS Code:** Klik met de rechtermuisknop op `SupervisorAgentDemo.java` en selecteer **"Run Java"** (zorg dat je `.env` bestand is geconfigureerd).

#### Hoe de Supervisor Werkt

Voordat je agents bouwt, moet je het MCP-transport verbinden met een client en deze verpakken als een `ToolProvider`. Zo worden de MCP-server tools beschikbaar voor je agents:

```java
// Maak een MCP-client van de transportlaag
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Wikkel de client in als een ToolProvider — dit brengt MCP-tools naar LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Nu kun je `mcpToolProvider` injecteren in elke agent die MCP tools nodig heeft:

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

// Supervisor coördineert de bestand → rapport workflow
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Retourneer het eindrapport
        .build();

// De Supervisor bepaalt welke agenten worden aangeroepen op basis van het verzoek
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Responsstrategieën

Wanneer je een `SupervisorAgent` configureert, specificeer je hoe hij zijn definitieve antwoord aan de gebruiker formuleert nadat de sub-agents hun taken hebben voltooid. Het diagram hieronder toont de drie beschikbare strategieën — LAST retourneert direct de output van de laatste agent, SUMMARY synthetiseert alle outputs via een LLM, en SCORED kiest degene met de hoogste score ten opzichte van het originele verzoek:

<img src="../../../translated_images/nl/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Drie strategieën voor hoe de Supervisor zijn eindantwoord formuleert — kies op basis van of je de output van de laatste agent, een gesynthetiseerde samenvatting of de best scorende optie wilt.*

De beschikbare strategieën zijn:

| Strategie | Beschrijving |
|----------|--------------|
| **LAST** | De supervisor retourneert de output van de laatste aangeroepen sub-agent of tool. Dit is nuttig wanneer de laatste agent in de workflow specifiek ontworpen is om het volledige, definitieve antwoord te produceren (bijvoorbeeld een "Summary Agent" in een onderzoeksworkflow). |
| **SUMMARY** | De supervisor gebruikt zijn eigen interne Language Model (LLM) om een samenvatting te maken van de gehele interactie en alle outputs van sub-agents, en retourneert die samenvatting als het finale antwoord. Dit levert een schone, geaggregeerde reactie aan de gebruiker. |
| **SCORED** | Het systeem gebruikt een interne LLM om zowel de LAST-respons als de SUMMARY van de interactie te beoordelen ten opzichte van het oorspronkelijke gebruikersverzoek, en retourneert de output met de hoogste score. |
Zie [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) voor de volledige implementatie.

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) en vraag:
> - "Hoe besluit de Supervisor welke agents te activeren?"
> - "Wat is het verschil tussen Supervisor en Sequential workflow patronen?"
> - "Hoe kan ik het planningsgedrag van de Supervisor aanpassen?"

#### Begrijpen van de Output

Wanneer je de demo draait, zie je een gestructureerde walkthrough van hoe de Supervisor meerdere agents orkestreert. Dit betekent elk onderdeel:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**De header** introduceert het workflowconcept: een gerichte pijplijn van het lezen van bestanden tot rapportgeneratie.

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

**Workflow Diagram** toont de datastroom tussen agents. Elke agent heeft een specifieke rol:
- **FileAgent** leest bestanden met MCP-tools en slaat onbewerkte inhoud op in `fileContent`
- **ReportAgent** gebruikt die inhoud en produceert een gestructureerd rapport in `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Gebruikersverzoek** toont de taak. De Supervisor analyseert dit en besluit FileAgent → ReportAgent te activeren.

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

**Supervisor Orkestratie** toont de 2-stappen flow in actie:
1. **FileAgent** leest het bestand via MCP en slaat de inhoud op
2. **ReportAgent** ontvangt de inhoud en maakt een gestructureerd rapport

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

#### Uitleg van Agentic Module Functies

Het voorbeeld demonstreert verschillende geavanceerde functies van het agentic module. Laten we Agentic Scope en Agent Listeners nader bekijken.

**Agentic Scope** toont het gedeelde geheugen waar agents hun resultaten opslaan via `@Agent(outputKey="...")`. Dit maakt het mogelijk:
- Latere agents toegang te geven tot outputs van vroegere agents
- De Supervisor een definitief antwoord te laten samenstellen
- Jou om te zien wat elke agent produceerde

Het onderstaande diagram toont hoe Agentic Scope fungeert als gedeeld geheugen in de file-to-report workflow — FileAgent schrijft zijn output onder de sleutel `fileContent`, ReportAgent leest dat en schrijft zijn eigen output onder `report`:

<img src="../../../translated_images/nl/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope fungeert als gedeeld geheugen — FileAgent schrijft `fileContent`, ReportAgent leest dit en schrijft `report`, en jouw code leest het eindresultaat.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Ruwe bestandsgegevens van FileAgent
String report = scope.readState("report");            // Gestructureerd rapport van ReportAgent
```

**Agent Listeners** maken monitoring en debugging van agent-uitvoering mogelijk. De stap-voor-stap output die je in de demo ziet komt van een AgentListener die zich vasthaakt aan elke agent-activatie:
- **beforeAgentInvocation** - Wordt aangeroepen wanneer de Supervisor een agent selecteert, zodat je ziet welke agent gekozen is en waarom
- **afterAgentInvocation** - Wordt aangeroepen wanneer een agent klaar is, en toont het resultaat
- **inheritedBySubagents** - Als waar, monitort de listener alle agents in de hiërarchie

Het volgende diagram toont de volledige levenscyclus van Agent Listener, inclusief hoe `onError` fouten tijdens agent-uitvoering afhandelt:

<img src="../../../translated_images/nl/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners haken zich in op de uitvoeringslevenscyclus — monitoren wanneer agents starten, voltooien, of fouten tegenkomen.*

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

Naast het Supervisor patroon biedt het `langchain4j-agentic` module meerdere krachtige workflow patronen. Onderstaand diagram toont alle vijf — van eenvoudige sequentiële pijplijnen tot goedkeuringsworkflows met human-in-the-loop:

<img src="../../../translated_images/nl/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Vijf workflow patronen voor het orkestreren van agents — van eenvoudige sequentiële pijplijnen tot human-in-the-loop goedkeuringsworkflows.*

| Patroon | Beschrijving | Gebruik |
|---------|--------------|---------|
| **Sequential** | Voer agents na elkaar uit, output gaat door naar de volgende | Pijplijnen: onderzoek → analyse → rapport |
| **Parallel** | Laat agents gelijktijdig lopen | Onafhankelijke taken: weer + nieuws + aandelen |
| **Loop** | Herhaal tot conditie bereikt | Kwaliteitsscore: verfijn tot score ≥ 0.8 |
| **Conditional** | Routeer op basis van condities | Classificeer → stuur naar specialist |
| **Human-in-the-Loop** | Voeg menselijke controlepunten toe | Goedkeuringsworkflows, content review |

## Kernbegrippen

Nu je MCP en het agentic module in actie hebt gezien, vatten we samen wanneer je welk model gebruikt.

Een van MCP’s grootste voordelen is het groeiende ecosysteem. Het onderstaande diagram toont hoe één universeel protocol je AI-app verbindt met een grote verscheidenheid aan MCP-servers — van bestandssysteem en database toegang tot GitHub, e-mail, web scraping, en meer:

<img src="../../../translated_images/nl/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP creëert een universeel protocol ecosysteem — elke MCP-compatibele server werkt met elke MCP-compatibele client, wat tooldeling over applicaties mogelijk maakt.*

**MCP** is ideaal als je bestaande tool-ecosystemen wilt benutten, tools wilt bouwen die meerdere applicaties delen, derde partij services wilt integreren met standaardprotocollen, of toolimplementaties wilt wisselen zonder code te veranderen.

**Het Agentic Module** werkt het beste als je declaratieve agentdefinities met `@Agent` annotaties wilt, workfloworkestratie (sequentieel, loop, parallel) nodig hebt, voorkeur geeft aan interface-gebaseerde agentontwerpen boven imperatieve code, of meerdere agents combineert die output delen via `outputKey`.

**Het Supervisor Agent patroon** is krachtig als de workflow niet van tevoren voorspelbaar is en je wilt dat de LLM beslist, als je meerdere gespecialiseerde agents hebt die dynamisch gecoördineerd moeten worden, als je conversatiesystemen bouwt die naar verschillende capaciteiten doorverwijzen, of als je het meest flexibele, adaptieve agentgedrag wilt.

Om je te helpen kiezen tussen de custom `@Tool` methoden uit Module 04 en MCP-tools uit deze module, toont de volgende vergelijking de belangrijkste afwegingen — custom tools bieden strakke koppeling en volledige typesafety voor app-specifieke logica, terwijl MCP-tools gestandaardiseerde, herbruikbare integraties bieden:

<img src="../../../translated_images/nl/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Wanneer gebruik je custom @Tool methoden vs MCP tools — custom tools voor app-specifieke logica met volledige typesafety, MCP tools voor gestandaardiseerde integraties die app-overschrijdend werken.*

## Gefeliciteerd!

Je hebt alle vijf modules van de LangChain4j voor Beginners cursus doorlopen! Hier zie je de volledige leerlijn die je hebt afgerond — van basale chat tot MCP-aangedreven agentic systemen:

<img src="../../../translated_images/nl/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Je leertraject door alle vijf modules — van basischat tot MCP-aangedreven agentic systemen.*

Je hebt de LangChain4j voor Beginners cursus voltooid. Je hebt geleerd:

- Hoe je conversatie-AI bouwt met geheugen (Module 01)
- Prompt engineering patronen voor verschillende taken (Module 02)
- Antwoorden onderbouwen met je documenten via RAG (Module 03)
- Basis AI-agents (assistenten) maken met custom tools (Module 04)
- Gestandaardiseerde tools integreren met de LangChain4j MCP en Agentic modules (Module 05)

### Wat Nu?

Na het afronden van de modules, verken de [Testing Guide](../docs/TESTING.md) om LangChain4j testconcepten in de praktijk te zien.

**Officiële bronnen:**
- [LangChain4j Documentatie](https://docs.langchain4j.dev/) - Uitgebreide handleidingen en API referentie
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Broncode en voorbeelden
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - Stapsgewijze tutorials voor diverse use cases

Bedankt voor het volgen van deze cursus!

---

**Navigatie:** [← Vorige: Module 04 - Tools](../04-tools/README.md) | [Terug naar hoofdmenu](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI-vertalingsdienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het oorspronkelijke document in de originele taal moet worden beschouwd als de gezaghebbende bron. Voor kritieke informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties voortvloeiend uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->