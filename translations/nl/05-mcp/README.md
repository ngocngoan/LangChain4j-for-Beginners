# Module 05: Model Context Protocol (MCP)

## Inhoudsopgave

- [Wat je zult leren](../../../05-mcp)
- [Wat is MCP?](../../../05-mcp)
- [Hoe MCP werkt](../../../05-mcp)
- [De Agentische Module](../../../05-mcp)
- [De voorbeelden draaien](../../../05-mcp)
  - [Vereisten](../../../05-mcp)
- [Snel aan de slag](../../../05-mcp)
  - [Bestandsbewerkingen (Stdio)](../../../05-mcp)
  - [Supervisor Agent](../../../05-mcp)
    - [De demo draaien](../../../05-mcp)
    - [Hoe de Supervisor werkt](../../../05-mcp)
    - [Reactiestrategieën](../../../05-mcp)
    - [De output begrijpen](../../../05-mcp)
    - [Uitleg over Agentische Module functies](../../../05-mcp)
- [Belangrijke concepten](../../../05-mcp)
- [Gefeliciteerd!](../../../05-mcp)
  - [Wat nu?](../../../05-mcp)

## Wat je zult leren

Je hebt al conversatie-AI gebouwd, prompts beheerst, antwoorden in documenten gegrondvest en agenten met tools gemaakt. Maar al die tools waren op maat gemaakt voor je specifieke toepassing. Wat als je je AI toegang kunt geven tot een gestandaardiseerd ecosysteem van tools die iedereen kan creëren en delen? In deze module leer je precies dat doen met het Model Context Protocol (MCP) en de agentische module van LangChain4j. We tonen eerst een eenvoudige MCP-bestandslezer en laten vervolgens zien hoe die gemakkelijk integreert in geavanceerde agentische workflows met behulp van het Supervisor Agent-patroon.

## Wat is MCP?

Het Model Context Protocol (MCP) biedt precies dat - een standaardmanier voor AI-toepassingen om externe tools te ontdekken en te gebruiken. In plaats van voor elke gegevensbron of dienst aangepaste integraties te schrijven, maak je verbinding met MCP-servers die hun mogelijkheden in een consistent formaat onthullen. Je AI-agent kan dan automatisch deze tools ontdekken en gebruiken.

<img src="../../../translated_images/nl/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Vergelijking" width="800"/>

*Voor MCP: complexe punt-tot-punt integraties. Na MCP: één protocol, eindeloze mogelijkheden.*

MCP lost een fundamenteel probleem in AI-ontwikkeling op: elke integratie is maatwerk. Wil je toegang tot GitHub? Maatwerkcode. Wil je bestanden lezen? Maatwerkcode. Wil je een database bevragen? Maatwerkcode. En geen van deze integraties werkt met andere AI-toepassingen.

MCP standaardiseert dit. Een MCP-server biedt tools aan met heldere beschrijvingen en schema’s. Elke MCP-client kan verbinding maken, beschikbare tools ontdekken en gebruiken. Een keer bouwen, overal gebruiken.

<img src="../../../translated_images/nl/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architectuur" width="800"/>

*Model Context Protocol architectuur - gestandaardiseerde toolontdekking en uitvoering*

## Hoe MCP werkt

<img src="../../../translated_images/nl/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Details" width="800"/>

*Hoe MCP onder de motorkap werkt — clients ontdekken tools, wisselen JSON-RPC berichten uit en voeren bewerkingen uit via een transportlaag.*

**Server-Client Architectuur**

MCP gebruikt een client-server model. Servers bieden tools - bestanden lezen, databases bevragen, API’s aanroepen. Clients (jouw AI-toepassing) maken verbinding met servers en gebruiken hun tools.

Om MCP te gebruiken met LangChain4j, voeg je deze Maven-afhankelijkheid toe:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Tool Ontdekking**

Wanneer jouw client verbinding maakt met een MCP-server, vraagt het: "Welke tools heb je?" De server reageert met een lijst van beschikbare tools, elk met beschrijvingen en parameterschema’s. Je AI-agent kan dan bepalen welke tools te gebruiken op basis van gebruikersverzoeken.

<img src="../../../translated_images/nl/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Ontdekking" width="800"/>

*De AI ontdekt beschikbare tools bij opstart — weet nu welke mogelijkheden er zijn en kan beslissen welke te gebruiken.*

**Transport Mechanismen**

MCP ondersteunt verschillende transportmechanismen. Deze module demonstreert de Stdio-transport voor lokale processen:

<img src="../../../translated_images/nl/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanismen" width="800"/>

*MCP transportmechanismen: HTTP voor remote servers, Stdio voor lokale processen*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Voor lokale processen. Je applicatie start een server als subprocess en communiceert via standaard input/output. Handig voor toegang tot het bestandssysteem of command line tools.

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

<img src="../../../translated_images/nl/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Stroom" width="800"/>

*Stdio transport in actie — je applicatie start de MCP server als subproces en communiceert via stdin/stdout pipes.*

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) en vraag:
> - "Hoe werkt Stdio transport en wanneer moet ik het gebruiken versus HTTP?"
> - "Hoe beheert LangChain4j de levenscyclus van opgestarte MCP serverprocessen?"
> - "Wat zijn de beveiligingsimplicaties van AI toegang geven tot het bestandssysteem?"

## De Agentische Module

Hoewel MCP gestandaardiseerde tools biedt, biedt de **agentische module** van LangChain4j een declaratieve manier om agenten te bouwen die deze tools orkestreren. De annotatie `@Agent` en `AgenticServices` laten je agentgedrag definiëren via interfaces in plaats van imperatieve code.

In deze module verken je het **Supervisor Agent**-patroon — een geavanceerde agentische AI-benadering waarbij een "supervisor" agent dynamisch bepaalt welke subagenten worden aangeroepen op basis van gebruikersverzoeken. We combineren beide concepten door één van onze subagenten MCP-aangedreven bestands toegang te geven.

Om de agentische module te gebruiken, voeg deze Maven-afhankelijkheid toe:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **⚠️ Experimenteel:** De `langchain4j-agentic` module is **experimenteel** en kan veranderen. De stabiele manier om AI-assistenten te bouwen blijft `langchain4j-core` met aangepaste tools (Module 04).

## De voorbeelden draaien

### Vereisten

- Java 21+, Maven 3.9+
- Node.js 16+ en npm (voor MCP servers)
- Omgevingsvariabelen geconfigureerd in `.env` bestand (vanaf root directory):
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (zelfde als Modules 01-04)

> **Opmerking:** Als je je omgevingsvariabelen nog niet hebt ingesteld, zie [Module 00 - Quick Start](../00-quick-start/README.md) voor instructies, of kopieer `.env.example` naar `.env` in de root directory en vul je waarden in.

## Snel aan de slag

**Met VS Code:** Klik met de rechtermuisknop op een demo bestand in de Explorer en kies **"Run Java"**, of gebruik de launch-configuraties uit het Run en Debug paneel (zorg dat je token eerst is toegevoegd aan het `.env` bestand).

**Met Maven:** Je kunt ook vanaf de commandoregel draaien met de voorbeelden hieronder.

### Bestandsbewerkingen (Stdio)

Dit demonstreert lokaal op subprocesses gebaseerde tools.

**✅ Geen vereisten nodig** - de MCP server wordt automatisch gestart.

**Met de start scripts (Aanbevolen):**

De start scripts laden automatisch omgevingsvariabelen uit het root `.env` bestand:

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

**Met VS Code:** Klik met de rechtermuisknop op `StdioTransportDemo.java` en kies **"Run Java"** (zorg dat je `.env` bestand goed is ingesteld).

De applicatie start automatisch een filesystem MCP server en leest een lokaal bestand. Let op hoe het beheer van het subprocess voor jou wordt afgehandeld.

**Verwachte output:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor Agent

Het **Supervisor Agent-patroon** is een **flexibele** agentische AI-vorm. Een Supervisor gebruikt een LLM om autonoom te bepalen welke agenten op te roepen op basis van de gebruikersvraag. In het volgende voorbeeld combineren we MCP-aangedreven bestands toegang met een LLM-agent om een gecontroleerde file read → report workflow te maken.

In de demo leest `FileAgent` een bestand via MCP filesystem tools, en genereert `ReportAgent` een gestructureerd rapport met een executive samenvatting (1 zin), 3 kernpunten en aanbevelingen. De Supervisor orkestreert deze stroom automatisch:

<img src="../../../translated_images/nl/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Patroon" width="800"/>

*De Supervisor gebruikt zijn LLM om te beslissen welke agenten aan te roepen en in welke volgorde — geen hardcoded routing nodig.*

Zo ziet de concrete workflow eruit voor onze file-to-report pijplijn:

<img src="../../../translated_images/nl/file-report-workflow.649bb7a896800de9.webp" alt="Bestand naar Rapport Workflow" width="800"/>

*FileAgent leest het bestand via MCP tools, daarna zet ReportAgent de ruwe inhoud om in een gestructureerd rapport.*

Elke agent slaat zijn output op in de **Agentic Scope** (gedeeld geheugen), waardoor downstream agenten eerdere resultaten kunnen gebruiken. Dit toont aan hoe MCP tools naadloos integreren in agentische workflows — de Supervisor hoeft niet te weten *hoe* bestanden worden gelezen, alleen dat `FileAgent` dat kan.

#### De demo draaien

De start scripts laden automatisch omgevingsvariabelen uit het root `.env` bestand:

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

**Met VS Code:** Klik met de rechtermuisknop op `SupervisorAgentDemo.java` en kies **"Run Java"** (zorg dat je `.env` bestand goed is ingesteld).

#### Hoe de Supervisor werkt

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

// Supervisor orkestreert de bestands- → rapportworkflow
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Retourneer het definitieve rapport
        .build();

// De Supervisor bepaalt welke agenten te gebruiken op basis van het verzoek
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Reactiestrategieën

Wanneer je een `SupervisorAgent` configureert, geef je op hoe deze het definitieve antwoord aan de gebruiker formuleert nadat de subagenten hun taken hebben afgerond.

<img src="../../../translated_images/nl/response-strategies.3d0cea19d096bdf9.webp" alt="Reactiestrategieën" width="800"/>

*Drie strategieën hoe de Supervisor zijn definitieve antwoord formuleert — kies op basis van of je de output van de laatste agent wilt, een gesynthetiseerde samenvatting, of de best scorende optie.*

De beschikbare strategieën zijn:

| Strategie | Beschrijving |
|----------|-------------|
| **LAST** | De supervisor geeft de output terug van de laatst aangeroepen subagent of tool. Dit is nuttig wanneer de laatste agent in de workflow specifiek ontworpen is om het complete, definitieve antwoord te produceren (bijv. een "Summary Agent" in een onderzoeksworkflow). |
| **SUMMARY** | De supervisor gebruikt zijn eigen interne Language Model (LLM) om een samenvatting te synthetiseren van de hele interactie en alle subagent outputs, en geeft die samenvatting als het definitieve antwoord terug. Dit levert een overzichtelijk, geaggregeerd antwoord aan de gebruiker. |
| **SCORED** | Het systeem gebruikt een intern LLM om zowel de LAST response als de SUMMARY van de interactie te scoren ten opzichte van het originele gebruikersverzoek, en geeft de output terug die de hoogste score krijgt. |

Zie [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) voor de volledige implementatie.

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) en vraag:
> - "Hoe bepaalt de Supervisor welke agenten worden aangeroepen?"
> - "Wat is het verschil tussen Supervisor- en Sequential workflowpatronen?"
> - "Hoe kan ik het planningsgedrag van de Supervisor aanpassen?"

#### De output begrijpen

Wanneer je de demo draait, zie je een gestructureerde doorloop van hoe de Supervisor meerdere agenten orkestreert. Dit betekent elk onderdeel:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**De koptekst** introduceert het workflow concept: een gerichte pijplijn van bestandslezing tot rapportgeneratie.

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

**Workflow Diagram** toont de gegevensstroom tussen agenten. Elke agent heeft een specifieke rol:
- **FileAgent** leest bestanden met MCP tools en slaat ruwe inhoud op in `fileContent`
- **ReportAgent** gebruikt die inhoud en produceert een gestructureerd rapport in `report`

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

**Supervisor Orkestratie** toont de 2-staps stroom in actie:
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

#### Uitleg over Agentische Module functies

Het voorbeeld laat verschillende geavanceerde functies van de agentische module zien. Laten we Agentic Scope en Agent Listeners nader bekijken.

**Agentic Scope** toont het gedeelde geheugen waar agenten hun resultaten opslaan met `@Agent(outputKey="...")`. Dit maakt het mogelijk dat:
- Latere agenten outputs van eerdere agenten kunnen lezen
- De Supervisor een finales antwoord kan synthetiseren
- Je kunt bekijken wat elke agent produceerde

<img src="../../../translated_images/nl/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Gedeeld Geheugen" width="800"/>

*Agentic Scope fungeert als gedeeld geheugen — FileAgent schrijft `fileContent`, ReportAgent leest dat en schrijft `report`, en jouw code leest het eindresultaat.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Ruwe bestandsgegevens van FileAgent
String report = scope.readState("report");            // Gestructureerd rapport van ReportAgent
```

**Agent Listeners** maken monitoring en debuggen van agentuitvoering mogelijk. De stapsgewijze output die je in de demo ziet komt van een AgentListener die zich aan elk agent aanroepen koppelt:
- **beforeAgentInvocation** - Wordt aangeroepen wanneer de Supervisor een agent selecteert, zodat je kunt zien welke agent is gekozen en waarom
- **afterAgentInvocation** - Wordt aangeroepen wanneer een agent klaar is, met de getoonde resultaat
- **inheritedBySubagents** - Wanneer true, monitort de listener alle agents in de hiërarchie

<img src="../../../translated_images/nl/agent-listeners.784bfc403c80ea13.webp" alt="Levenscyclus van Agent Listeners" width="800"/>

*Agent Listeners haken in op de uitvoeringslevenscyclus — monitoren wanneer agents starten, voltooien of fouten tegenkomen.*

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
        return true; // Verspreid naar alle subagenten
    }
};
```

Naast het Supervisor-patroon biedt de `langchain4j-agentic` module verschillende krachtige workflowpatronen en functies:

<img src="../../../translated_images/nl/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patronen" width="800"/>

*Vijf workflowpatronen voor het orkestreren van agents — van eenvoudige sequentiële pijplijnen tot goedkeuringsworkflows met menselijke tussenkomst.*

| Patroon | Beschrijving | Gebruik |
|---------|--------------|---------|
| **Sequentieel** | Voer agents op volgorde uit, uitvoer stroomt door naar de volgende | Pijplijnen: onderzoek → analyse → rapport |
| **Parallel** | Draai agents gelijktijdig | Onafhankelijke taken: weer + nieuws + aandelen |
| **Lus** | Itereer totdat aan voorwaarde is voldaan | Kwaliteitsscores: verfijnen totdat score ≥ 0,8 |
| **Conditioneel** | Routeren op basis van voorwaarden | Classificeren → doorsturen naar specialist |
| **Mens-in-de-Lus** | Voeg menselijke controles toe | Goedkeuringsworkflows, inhoudsbeoordeling |

## Belangrijke Concepten

Nu je MCP en de agentic module in actie hebt verkend, laten we samenvatten wanneer je welke aanpak gebruikt.

<img src="../../../translated_images/nl/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosysteem" width="800"/>

*MCP creëert een universeel protocolecosysteem — elke MCP-compatibele server werkt samen met elke MCP-compatibele client, waardoor tools gedeeld kunnen worden tussen applicaties.*

**MCP** is ideaal wanneer je bestaande tooleti ecosystems wilt benutten, tools wilt bouwen die door meerdere applicaties gedeeld kunnen worden, derde partijen wilt integreren via standaardprotocollen, of toolimplementaties wilt wisselen zonder code te wijzigen.

**De Agentic Module** werkt het beste als je declaratieve agentdefinities met `@Agent` annotaties wilt, workfloworkestratie nodig hebt (sequentieel, lus, parallel), een interface-gebaseerd agentontwerp prefereert boven imperative code, of meerdere agents wilt combineren die uitvoer delen via `outputKey`.

**Het Supervisor Agent patroon** blinkt uit als de workflow van tevoren niet voorspelbaar is en je het LLM wilt laten beslissen, als je meerdere gespecialiseerde agents hebt die dynamisch moeten worden georkestreerd, bij het bouwen van conversatiesystemen die naar verschillende capaciteiten routeren, of als je het meest flexibele, adaptieve agentgedrag wilt.

<img src="../../../translated_images/nl/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Aangepaste Tools vs MCP Tools" width="800"/>

*Wanneer aangepaste @Tool-methoden te gebruiken vs MCP tools — aangepaste tools voor app-specifieke logica met volledige typesafety, MCP tools voor gestandaardiseerde integraties die over apps heen werken.*

## Gefeliciteerd!

<img src="../../../translated_images/nl/course-completion.48cd201f60ac7570.webp" alt="Voltooiing Cursus" width="800"/>

*Je leertraject door alle vijf modules — van basischat tot MCP-gestuurde agentic systemen.*

Je hebt de LangChain4j voor beginners cursus voltooid. Je hebt geleerd:

- Hoe je conversatie-AI bouwt met geheugen (Module 01)
- Prompt engineering patronen voor verschillende taken (Module 02)
- Antwoorden in je documenten verankeren met RAG (Module 03)
- Basis AI agents maken (assistenten) met aangepaste tools (Module 04)
- Gestandaardiseerde tools integreren met de LangChain4j MCP en Agentic modules (Module 05)

### Wat Nu?

Na het voltooien van de modules, verken de [Testing Guide](../docs/TESTING.md) om LangChain4j testconcepten in praktijk te zien.

**Officiële bronnen:**
- [LangChain4j Documentatie](https://docs.langchain4j.dev/) - Uitgebreide gidsen en API-referentie
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Broncode en voorbeelden
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - Stapsgewijze tutorials voor diverse use cases

Dank je wel voor het volgen van deze cursus!

---

**Navigatie:** [← Vorige: Module 04 - Tools](../04-tools/README.md) | [Terug naar Hoofd](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI vertaaldienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, kan het voorkomen dat geautomatiseerde vertalingen fouten of onnauwkeurigheden bevatten. Het originele document in de oorspronkelijke taal dient als de gezaghebbende bron te worden beschouwd. Voor cruciale informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor misverstanden of verkeerde interpretaties voortvloeiend uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->