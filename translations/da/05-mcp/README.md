# Modul 05: Model Context Protocol (MCP)

## Indholdsfortegnelse

- [Hvad du vil lære](../../../05-mcp)
- [Hvad er MCP?](../../../05-mcp)
- [Hvordan MCP fungerer](../../../05-mcp)
- [Agentmodulet](../../../05-mcp)
- [Køre eksemplerne](../../../05-mcp)
  - [Forudsætninger](../../../05-mcp)
- [Hurtig start](../../../05-mcp)
  - [Filoperationer (Stdio)](../../../05-mcp)
  - [Supervisor-agent](../../../05-mcp)
    - [Køre demoen](../../../05-mcp)
    - [Hvordan Supervisor fungerer](../../../05-mcp)
    - [Responsstrategier](../../../05-mcp)
    - [Forstå outputtet](../../../05-mcp)
    - [Forklaring af agentmodulets funktioner](../../../05-mcp)
- [Nøglebegreber](../../../05-mcp)
- [Tillykke!](../../../05-mcp)
  - [Hvad nu?](../../../05-mcp)

## Hvad du vil lære

Du har bygget konversationel AI, mestret prompts, forankret svar i dokumenter og skabt agenter med værktøjer. Men alle disse værktøjer var specialbyggede til din specifikke applikation. Hvad nu hvis du kunne give din AI adgang til et standardiseret økosystem af værktøjer, som alle kan skabe og dele? I dette modul lærer du netop det med Model Context Protocol (MCP) og LangChain4j’s agentmodul. Vi viser først en simpel MCP-filoplæser og viser derefter, hvordan den let integreres i avancerede agentbaserede workflows ved hjælp af Supervisor Agent-mønstret.

## Hvad er MCP?

Model Context Protocol (MCP) tilbyder netop det – en standard måde for AI-applikationer at opdage og bruge eksterne værktøjer. I stedet for at skrive tilpassede integrationer for hver datakilde eller service, forbinder du til MCP-servere, der eksponerer deres funktioner i et ensartet format. Din AI-agent kan så automatisk opdage og bruge disse værktøjer.

<img src="../../../translated_images/da/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Før MCP: Komplekse punkt-til-punkt integrationer. Efter MCP: Ét protokol, uendelige muligheder.*

MCP løser et grundlæggende problem i AI-udvikling: hver integration er specialbygget. Vil du tilgå GitHub? Egne koder. Vil du læse filer? Egne koder. Vil du forespørge en database? Egne koder. Og ingen af disse integrationer virker sammen med andre AI-applikationer.

MCP standardiserer dette. En MCP-server eksponerer værktøjer med klare beskrivelser og skemaer. Enhver MCP-klient kan forbinde, opdage tilgængelige værktøjer og bruge dem. Byg én gang, brug overalt.

<img src="../../../translated_images/da/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol arkitektur – standardiseret værktøjsopdagelse og udførelse*

## Hvordan MCP fungerer

<img src="../../../translated_images/da/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Hvordan MCP fungerer under motorhjelmen — klienter opdager værktøjer, udveksler JSON-RPC beskeder og udfører operationer gennem et transportlag.*

**Server-klient Arkitektur**

MCP bruger en klient-server model. Servere leverer værktøjer – læser filer, spørger databaser, kalder API’er. Klienter (din AI-applikation) forbinder til servere og bruger deres værktøjer.

For at bruge MCP med LangChain4j, tilføj denne Maven-afhængighed:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Værktøjsopdagelse**

Når din klient forbinder til en MCP-server, spørger den: "Hvilke værktøjer har du?" Serveren svarer med en liste over tilgængelige værktøjer, hver med beskrivelser og parameterskemaer. Din AI-agent kan herefter beslutte, hvilke værktøjer der skal bruges baseret på brugerens forespørgsler.

<img src="../../../translated_images/da/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI’en opdager tilgængelige værktøjer ved opstart — den ved nu, hvilke kapabiliteter der er tilgængelige, og kan vælge hvilke der skal bruges.*

**Transportmekanismer**

MCP understøtter forskellige transportmekanismer. Dette modul demonstrerer Stdio-transporten til lokale processer:

<img src="../../../translated_images/da/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP transportmekanismer: HTTP til fjernservere, Stdio til lokale processer*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Til lokale processer. Din applikation starter en server som en underproces og kommunikerer gennem standard input/output. Nyttigt til adgang til filsystem eller kommandolinjeværktøjer.

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

<img src="../../../translated_images/da/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio transport i aktion — din applikation starter MCP-serveren som en børneproces og kommunikerer via stdin/stdout-rør.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) og spørg:
> - "Hvordan fungerer Stdio-transporten, og hvornår bør jeg bruge den vs HTTP?"
> - "Hvordan håndterer LangChain4j livscyklussen for startede MCP-serverprocesser?"
> - "Hvad er sikkerhedsimplikationerne ved at give AI adgang til filsystemet?"

## Agentmodulet

Mens MCP giver standardiserede værktøjer, giver LangChain4j’s **agentmodul** en deklarativ måde at bygge agenter, der orkestrerer disse værktøjer. `@Agent`-annotationen og `AgenticServices` lader dig definere agentadfærd gennem interfaces i stedet for imperativ kode.

I dette modul udforsker du **Supervisor Agent**-mønstret — en avanceret agent-baseret AI-tilgang, hvor en "supervisor"-agent dynamisk beslutter, hvilke under-agenter der skal påkaldes baseret på brugerforespørgsler. Vi kombinerer begge koncepter ved at give en af vores under-agenter MCP-drevne filadgangsfunktioner.

For at bruge agentmodulet, tilføj denne Maven-afhængighed:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **⚠️ Eksperimentel:** `langchain4j-agentic` modulet er **eksperimentelt** og kan ændres. Den stabile måde at bygge AI-assistenter på er stadig `langchain4j-core` med tilpassede værktøjer (Modul 04).

## Køre eksemplerne

### Forudsætninger

- Java 21+, Maven 3.9+
- Node.js 16+ og npm (til MCP-servere)
- Miljøvariabler konfigureret i `.env` fil (fra rodmappen):
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (samme som Moduler 01-04)

> **Note:** Hvis du ikke har sat dine miljøvariabler op endnu, se [Modul 00 - Hurtig Start](../00-quick-start/README.md) for instruktioner eller kopier `.env.example` til `.env` i rodmappen og udfyld dine værdier.

## Hurtig start

**Bruger du VS Code:** Højreklik blot på en demo-fil i Explorer og vælg **"Run Java"**, eller brug launch-konfigurationerne fra Run and Debug-panelet (sørg først for at have tilføjet din token til `.env` filen).

**Bruger du Maven:** Alternativt kan du køre fra kommandolinjen med eksemplerne nedenfor.

### Filoperationer (Stdio)

Dette demonstrerer lokale subprocess-baserede værktøjer.

**✅ Ingen forudsætninger nødvendige** - MCP-serveren startes automatisk.

**Brug start-scripts (anbefalet):**

Start-scripts loader automatisk miljøvariabler fra rodens `.env` fil:

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

**Bruger du VS Code:** Højreklik på `StdioTransportDemo.java` og vælg **"Run Java"** (sørg for at `.env` filen er konfigureret).

Applikationen starter en filsystem MCP-server automatisk og læser en lokal fil. Bemærk hvordan underprocesstyringen håndteres for dig.

**Forventet output:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor-agent

**Supervisor Agent-mønstret** er en **fleksibel** form for agent-basret AI. En Supervisor bruger en LLM til selvstændigt at beslutte, hvilke agenter der skal påkaldes baseret på brugerens forespørgsel. I næste eksempel kombinerer vi MCP-drevet filadgang med en LLM-agent for at skabe en overvåget læsning af filer → rapporteringsworkflow.

I demoen læser `FileAgent` en fil via MCP-filsystemværktøjer, og `ReportAgent` genererer en struktureret rapport med en ledelsessammendrag (1 sætning), 3 nøglepunkter og anbefalinger. Superviseren orkestrerer denne flow automatisk:

<img src="../../../translated_images/da/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Supervisor bruger sin LLM til at beslutte, hvilke agenter der skal påkaldes og i hvilken rækkefølge — intet hårdkodet routing nødvendigt.*

Sådan ser det konkrete workflow ud for vores fil-til-rapport pipeline:

<img src="../../../translated_images/da/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent læser filen via MCP-værktøjer, derefter omdanner ReportAgent det rå indhold til en struktureret rapport.*

Hver agent gemmer sit output i **Agentic Scope** (delt hukommelse), så efterfølgende agenter kan tilgå tidligere resultater. Dette viser, hvordan MCP-værktøjer integreres problemfrit i agentiske workflows — Superviseren behøver ikke at kende *hvordan* filer læses, kun at `FileAgent` kan gøre det.

#### Køre demoen

Start-scripts loader automatisk miljøvariabler fra rodens `.env` fil:

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

**Bruger du VS Code:** Højreklik på `SupervisorAgentDemo.java` og vælg **"Run Java"** (sørg for at `.env` filen er konfigureret).

#### Hvordan Supervisor fungerer

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

// Supervisor orkestrerer fil → rapport arbejdsflowet
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Returner den endelige rapport
        .build();

// Supervisor beslutter, hvilke agenter der skal påkaldes baseret på anmodningen
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Responsstrategier

Når du konfigurerer en `SupervisorAgent`, specificerer du, hvordan den skal formulere sit endelige svar til brugeren efter, at under-agenterne har afsluttet deres opgaver.

<img src="../../../translated_images/da/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Tre strategier for, hvordan Superviseren formulerer sit endelige svar — vælg baseret på, om du vil have sidste agents output, en syntetiseret opsummering eller den bedst scorende mulighed.*

De tilgængelige strategier er:

| Strategi | Beskrivelse |
|----------|-------------|
| **LAST** | Superviseren returnerer output fra den sidste kaldte sub-agent eller værktøj. Dette er nyttigt, når den sidste agent i workflowet er specifikt designet til at producere det komplette, endelige svar (f.eks. en "Summary Agent" i en research pipeline). |
| **SUMMARY** | Superviseren bruger sin egen interne sprogmodel (LLM) til at syntetisere et sammendrag af hele interaktionen og alle sub-agent outputs og returnerer dette som det endelige svar. Det giver et rent, samlet svar til brugeren. |
| **SCORED** | Systemet bruger en intern LLM til at score både LAST-svaret og SAMMENDRAGET af interaktionen i forhold til den oprindelige brugerforespørgsel og returnerer det output, der modtager den højeste score. |

Se [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) for komplet implementation.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) og spørg:
> - "Hvordan beslutter Superviseren, hvilke agenter der skal påkaldes?"
> - "Hvad er forskellen på Supervisor og Sequential workflow-mønstre?"
> - "Hvordan kan jeg tilpasse Supervisorens planlægningsadfærd?"

#### Forstå outputtet

Når du kører demoen, vil du se en struktureret gennemgang af, hvordan Superviseren orkestrerer flere agenter. Her er hvad hver sektion betyder:

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

**Workflow-diagram** viser dataflowet mellem agenterne. Hver agent har en specifik rolle:
- **FileAgent** læser filer ved hjælp af MCP-værktøjer og gemmer råindhold i `fileContent`
- **ReportAgent** bruger dette indhold og producerer en struktureret rapport i `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Brugerforespørgsel** viser opgaven. Superviseren parses denne og beslutter at påkalde FileAgent → ReportAgent.

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

**Supervisor Orkestrering** viser 2-trins-flowet i aktion:
1. **FileAgent** læser filen via MCP og gemmer indholdet
2. **ReportAgent** modtager indholdet og genererer en struktureret rapport

Superviseren tog disse beslutninger **autonomt** baseret på brugerens forespørgsel.

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

#### Forklaring af agentmodulets funktioner

Eksemplet demonstrerer flere avancerede funktioner i agentmodulet. Lad os kigge nærmere på Agentic Scope og Agent Listeners.

**Agentic Scope** viser den delte hukommelse, hvor agenter gemte deres resultater ved brug af `@Agent(outputKey="...")`. Dette tillader:
- Senere agenter at tilgå tidligere agenters output
- Superviseren at syntetisere et endeligt svar
- Dig at inspicere, hvad hver agent producerede

<img src="../../../translated_images/da/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope fungerer som delt hukommelse — FileAgent skriver `fileContent`, ReportAgent læser den og skriver `report`, og din kode læser det endelige resultat.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Rå fildata fra FileAgent
String report = scope.readState("report");            // Struktureret rapport fra ReportAgent
```

**Agent Listeners** muliggør overvågning og debugging af agentudførelse. Det trin-for-trin output, du ser i demoen, kommer fra en AgentListener, der kobler sig på hver agentkald:
- **beforeAgentInvocation** - Kaldt når Supervisor vælger en agent, så du kan se, hvilken agent der blev valgt, og hvorfor
- **afterAgentInvocation** - Kaldt når en agent er færdig, der viser dens resultat
- **inheritedBySubagents** - Når sand, overvåger lytteren alle agenter i hierarkiet

<img src="../../../translated_images/da/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent-lyttere kobler sig på eksekveringslivscyklussen — overvåger når agenter starter, afslutter eller støder på fejl.*

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

Udover Supervisor-mønsteret tilbyder `langchain4j-agentic` modulet flere kraftfulde workflow-mønstre og funktioner:

<img src="../../../translated_images/da/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Fem workflow-mønstre til orkestrering af agenter — fra simple sekventielle pipelines til human-in-the-loop godkendelses-workflows.*

| Mønster | Beskrivelse | Anvendelsestilfælde |
|---------|-------------|----------|
| **Sekventiel** | Udfør agenter i rækkefølge, output flyder til næste | Pipelines: forskning → analyse → rapport |
| **Parallelt** | Kør agenter samtidigt | Uafhængige opgaver: vejr + nyheder + aktier |
| **Løkke** | Iterer indtil betingelse er opfyldt | Kvalitetsscorering: forfin indtil score ≥ 0.8 |
| **Betinget** | Rute baseret på betingelser | Klassificer → rute til specialistagent |
| **Human-in-the-Loop** | Tilføj menneskelige kontrolpunkter | Godkendelses-workflows, indholdsrevision |

## Centrale Begreber

Nu hvor du har udforsket MCP og agentic modulet i praksis, lad os opsummere hvornår man skal bruge hver tilgang.

<img src="../../../translated_images/da/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP skaber et universelt protokol-økosystem — enhver MCP-kompatibel server fungerer med enhver MCP-kompatibel klient, hvilket muliggør værktøjsdeling på tværs af applikationer.*

**MCP** er ideel, når du vil udnytte eksisterende værktøjsøkosystemer, bygge værktøjer som flere applikationer kan dele, integrere tredjepartstjenester med standardprotokoller, eller udskifte værktøjsimplementeringer uden at ændre kode.

**Agentic Modulet** fungerer bedst, når du ønsker deklarative agentdefinitioner med `@Agent` annotationer, har brug for workflow-orkestrering (sekventiel, løkke, parallelt), foretrækker interface-baseret agentdesign fremfor imperativ kode, eller kombinerer flere agenter der deler output via `outputKey`.

**Supervisor Agent-mønsteret** skinner, når workflow ikke er forudsigeligt på forhånd, og du ønsker at LLM skal beslutte, når du har flere specialiserede agenter der kræver dynamisk orkestrering, når du bygger konversationssystemer der ruter til forskellige funktioner, eller når du ønsker den mest fleksible og adaptive agentadfærd.

<img src="../../../translated_images/da/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Hvornår du skal bruge brugerdefinerede @Tool metoder vs MCP værktøjer — brugerdefinerede værktøjer til app-specifik logik med fuld typesikkerhed, MCP værktøjer til standardiserede integrationer der fungerer på tværs af applikationer.*

## Tillykke!

<img src="../../../translated_images/da/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Din læringsrejse gennem alle fem moduler — fra grundlæggende chat til MCP-drevne agentic systemer.*

Du har gennemført LangChain4j for Beginners kurset. Du har lært:

- Hvordan man bygger konversations-AI med hukommelse (Modul 01)
- Prompt engineering mønstre til forskellige opgaver (Modul 02)
- At forankre svar i dine dokumenter med RAG (Modul 03)
- At skabe grundlæggende AI-agenter (assistenter) med brugerdefinerede værktøjer (Modul 04)
- At integrere standardiserede værktøjer med LangChain4j MCP og Agentic modulerne (Modul 05)

### Hvad nu?

Efter at have gennemført modulerne, kan du udforske [Testing Guide](../docs/TESTING.md) for at se LangChain4j testkonceptet i praksis.

**Officielle ressourcer:**
- [LangChain4j Dokumentation](https://docs.langchain4j.dev/) - Omfattende guider og API-reference
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Kildekode og eksempler
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - Step-for-step tutorials til forskellige brugssager

Tak fordi du gennemførte kurset!

---

**Navigation:** [← Forrige: Modul 04 - Værktøjer](../04-tools/README.md) | [Tilbage til Forsiden](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:  
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, skal du være opmærksom på, at automatiske oversættelser kan indeholde fejl eller unøjagtigheder. Det oprindelige dokument på dets modersmål bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for misforståelser eller fejltolkninger, der opstår som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->