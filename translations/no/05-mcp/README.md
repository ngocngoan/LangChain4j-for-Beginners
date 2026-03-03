# Modul 05: Model Context Protocol (MCP)

## Innholdsfortegnelse

- [Hva Du Vil Lære](../../../05-mcp)
- [Hva er MCP?](../../../05-mcp)
- [Hvordan MCP Fungerer](../../../05-mcp)
- [Agentmodulen](../../../05-mcp)
- [Kjøre Eksemplene](../../../05-mcp)
  - [Forutsetninger](../../../05-mcp)
- [Rask Start](../../../05-mcp)
  - [Filoperasjoner (Stdio)](../../../05-mcp)
  - [Tilsynsagent](../../../05-mcp)
    - [Kjøre Demoen](../../../05-mcp)
    - [Hvordan Tilsynet Fungerer](../../../05-mcp)
    - [Responsstrategier](../../../05-mcp)
    - [Forstå Utskriften](../../../05-mcp)
    - [Forklaring av Agentmodulens Funksjoner](../../../05-mcp)
- [Nøkkelkonsepter](../../../05-mcp)
- [Gratulerer!](../../../05-mcp)
  - [Hva Nå?](../../../05-mcp)

## Hva Du Vil Lære

Du har bygget samtale-AI, mestret prompts, forankret svar i dokumenter og laget agenter med verktøy. Men alle disse verktøyene var spesialbygget for din spesifikke applikasjon. Hva om du kunne gi AIen tilgang til et standardisert økosystem av verktøy som hvem som helst kan lage og dele? I denne modulen lærer du akkurat det med Model Context Protocol (MCP) og LangChain4j sin agentmodul. Vi viser først en enkel MCP-fil-leser og deretter hvordan den lett integreres i avanserte agentarbeidsflyter med Supervisor Agent-mønsteret.

## Hva er MCP?

Model Context Protocol (MCP) tilbyr nettopp det — en standard måte for AI-applikasjoner å oppdage og bruke eksterne verktøy på. I stedet for å skrive tilpassede integrasjoner for hver datakilde eller tjeneste, kobler du til MCP-servere som eksponerer sine funksjoner i et konsistent format. Din AI-agent kan deretter automatisk oppdage og bruke disse verktøyene.

Diagrammet under viser forskjellen — uten MCP krever hver integrasjon tilpasset punkt-til-punkt-kobling; med MCP kobler ett enkelt protokoll appen din til ethvert verktøy:

<img src="../../../translated_images/no/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Før MCP: Komplekse punkt-til-punkt-integrasjoner. Etter MCP: En protokoll, uendelige muligheter.*

MCP løser et grunnleggende problem innen AI-utvikling: hver integrasjon er tilpasset. Vil du få tilgang til GitHub? Tilpasset kode. Vil du lese filer? Tilpasset kode. Vil du spørre en database? Tilpasset kode. Og ingen av disse integrasjonene fungerer med andre AI-applikasjoner.

MCP standardiserer dette. En MCP-server eksponerer verktøy med klare beskrivelser og skjemaer. Enhver MCP-klient kan koble til, oppdage tilgjengelige verktøy og bruke dem. Bygg én gang, bruk overalt.

Diagrammet under illustrerer denne arkitekturen — en enkelt MCP-klient (din AI-applikasjon) kobler til flere MCP-servere, hver som eksponerer sitt eget sett med verktøy gjennom standardprotokollen:

<img src="../../../translated_images/no/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol-arkitektur - standardisert oppdagelse og utførelse av verktøy*

## Hvordan MCP Fungerer

Under panseret bruker MCP en lagdelt arkitektur. Din Java-applikasjon (MCP-klienten) oppdager tilgjengelige verktøy, sender JSON-RPC-forespørsler gjennom et transportlag (Stdio eller HTTP), og MCP-serveren utfører operasjoner og returnerer resultater. Følgende diagram bryter ned hvert lag i denne protokollen:

<img src="../../../translated_images/no/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Hvordan MCP fungerer under panseret — klienter oppdager verktøy, utveksler JSON-RPC-meldinger og utfører operasjoner gjennom et transportlag.*

**Server-Klient-arkitektur**

MCP benytter en klient-server-modell. Servere tilbyr verktøy — lese filer, spørre databaser, kalle APIer. Klienter (din AI-applikasjon) kobler til servere og bruker deres verktøy.

For å bruke MCP med LangChain4j, legg til denne Maven-avhengigheten:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Verktøyoppdagelse**

Når klienten din kobler til en MCP-server, spør den "Hvilke verktøy har dere?" Serveren svarer med en liste over tilgjengelige verktøy, hver med beskrivelser og parametreskjemaer. Din AI-agent kan så bestemme hvilke verktøy som skal brukes basert på brukerforespørsler. Diagrammet under viser denne håndtrykk-prosessen — klienten sender en `tools/list`-forespørsel og serveren returnerer sine tilgjengelige verktøy med beskrivelser og parametreskjemaer:

<img src="../../../translated_images/no/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI oppdager tilgjengelige verktøy ved oppstart — den vet nå hvilke kapasiteter som finnes og kan bestemme hvilke den skal bruke.*

**Transportmekanismer**

MCP støtter ulike transportmekanismer. De to alternativene er Stdio (for lokal underprosess-kommunikasjon) og Streamable HTTP (for eksterne servere). Denne modulen demonstrerer Stdio-transporten:

<img src="../../../translated_images/no/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP transportmekanismer: HTTP for eksterne servere, Stdio for lokale prosesser*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

For lokale prosesser. Applikasjonen din starter en server som en underprosess og kommuniserer gjennom standard input/output. Nyttig for tilgang til filsystemet eller kommandolinjeverktøy.

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

`@modelcontextprotocol/server-filesystem`-serveren eksponerer følgende verktøy, alle avgrenset til katalogene du spesifiserer:

| Verktøy | Beskrivelse |
|------|-------------|
| `read_file` | Leser innholdet i en enkelt fil |
| `read_multiple_files` | Leser flere filer i ett kall |
| `write_file` | Oppretter eller overskriver en fil |
| `edit_file` | Utfører målrettede søk-og-erstatt-endringer |
| `list_directory` | Lister filer og kataloger på en sti |
| `search_files` | Søker rekursivt etter filer som matcher et mønster |
| `get_file_info` | Henter filmetadata (størrelse, tidsstempler, tillatelser) |
| `create_directory` | Oppretter en katalog (inkludert overordnede kataloger) |
| `move_file` | Flytter eller gir nytt navn til en fil eller katalog |

Følgende diagram viser hvordan Stdio-transport fungerer under kjøring — Java-applikasjonen din starter MCP-serveren som en barneprosess og kommuniserer gjennom stdin/stdout-piper, uten nettverk eller HTTP involvert:

<img src="../../../translated_images/no/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio-transport i aksjon — applikasjonen starter MCP-serveren som en barneprosess og kommuniserer gjennom stdin/stdout-piper.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) og spør:
> - "Hvordan fungerer Stdio-transport og når bør jeg bruke det i stedet for HTTP?"
> - "Hvordan håndterer LangChain4j livssyklusen til MCP-serverprosesser som startes?"
> - "Hva er sikkerhetsimplikasjonene ved å gi AI tilgang til filsystemet?"

## Agentmodulen

Mens MCP tilbyr standardiserte verktøy, gir LangChain4j sin **agentmodul** en deklarativ måte å bygge agenter som orkestrerer disse verktøyene på. `@Agent`-annotasjonen og `AgenticServices` lar deg definere agentoppførsel gjennom grensesnitt snarere enn imperativ kode.

I denne modulen utforsker du **Supervisor Agent**-mønsteret — en avansert agentisk AI-tilnærming der en "tilsynsagent" dynamisk avgjør hvilke under-agenter den skal kalle basert på brukerforespørsler. Vi kombinerer begge konseptene ved å gi én av våre under-agenter MCP-drevne filtilgangsevner.

For å bruke agentmodulen, legg til denne Maven-avhengigheten:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Merk:** `langchain4j-agentic`-modulen bruker en egen versjonsegenskap (`langchain4j.mcp.version`) fordi den slippes på en annen tidsplan enn kjernebibliotekene til LangChain4j.

> **⚠️ Eksperimentell:** `langchain4j-agentic` er **eksperimentell** og kan endres. Den stabile måten å bygge AI-assistenter på er fortsatt `langchain4j-core` med egendefinerte verktøy (Modul 04).

## Kjøre Eksemplene

### Forutsetninger

- Fullført [Modul 04 - Verktøy](../04-tools/README.md) (denne modulen bygger på konsepter fra egendefinerte verktøy og sammenligner med MCP-verktøy)
- `.env`-fil i rotkatalog med Azure-legitimasjon (opprettet av `azd up` i Modul 01)
- Java 21+, Maven 3.9+
- Node.js 16+ og npm (for MCP-servere)

> **Merk:** Hvis du ikke har satt opp miljøvariablene dine ennå, se [Modul 01 - Introduksjon](../01-introduction/README.md) for distribusjonsinstruksjoner (`azd up` oppretter `.env`-filen automatisk), eller kopier `.env.example` til `.env` i rotkatalogen og fyll inn dine verdier.

## Rask Start

**Bruke VS Code:** Høyreklikk på en hvilken som helst demofil i Utforsker og velg **"Run Java"**, eller bruk lanseringskonfigurasjonene i Kjør og Feilsøk-panelet (pass på at `.env`-filen din er konfigurert med Azure-legitimasjon først).

**Bruke Maven:** Alternativt kan du kjøre fra kommandolinjen med eksemplene nedenfor.

### Filoperasjoner (Stdio)

Dette demonstrerer lokale verktøy basert på underprosesser.

**✅ Ingen forutsetninger kreves** - MCP-serveren startes automatisk.

**Bruke Start-skript (Anbefalt):**

Start-skriptene laster automatisk miljøvariabler fra rotens `.env`-fil:

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

**Bruke VS Code:** Høyreklikk på `StdioTransportDemo.java` og velg **"Run Java"** (sørg for at `.env`-filen din er konfigurert).

Applikasjonen starter en MCP-server for filsystem automatisk og leser en lokal fil. Legg merke til hvordan underprosesshåndteringen blir gjort for deg.

**Forventet utskrift:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Tilsynsagent

**Supervisor Agent-mønsteret** er en **fleksibel** form for agentisk AI. En Supervisor bruker en LLM for autonomt å avgjøre hvilke agenter som skal kalles basert på brukerens forespørsel. I neste eksempel kombinerer vi MCP-drevet filtilgang med en LLM-agent for å lage en overvåket fil-les → rapport-arbeidsflyt.

I demoen leser `FileAgent` en fil ved hjelp av MCPs filsystemverktøy, og `ReportAgent` genererer en strukturert rapport med et sammendrag (1 setning), 3 nøkkelpunkter og anbefalinger. Supervisor orkestrerer denne flyten automatisk:

<img src="../../../translated_images/no/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Tilsynet bruker sin LLM for å avgjøre hvilke agenter som skal kalles og i hvilken rekkefølge — ingen hardkodet ruting nødvendig.*

Slik ser den konkrete arbeidsflyten ut for vår fil-til-rapport-pipeline:

<img src="../../../translated_images/no/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent leser filen via MCP-verktøy, deretter forvandler ReportAgent råinnholdet til en strukturert rapport.*

Hver agent lagrer sitt resultat i **Agentic Scope** (delt minne), som muliggjør for påfølgende agenter å få tilgang til tidligere resultater. Dette demonstrerer hvordan MCP-verktøy integreres sømløst i agentiske arbeidsflyter — Supervisor må ikke vite *hvordan* filer leses, kun at `FileAgent` kan gjøre det.

#### Kjøre Demoen

Start-skriptene laster automatisk miljøvariabler fra rotens `.env`-fil:

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

**Bruke VS Code:** Høyreklikk på `SupervisorAgentDemo.java` og velg **"Run Java"** (sørg for at `.env`-filen er konfigurert).

#### Hvordan Tilsynet Fungerer

Før du bygger agenter, må du koble MCP-transporten til en klient og pakke den som en `ToolProvider`. Slik gjør verktøyene fra MCP-serveren tilgjengelig for agentene dine:

```java
// Opprett en MCP-klient fra transporten
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Pakk klienten som en ToolProvider — dette kobler MCP-verktøy til LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Nå kan du injisere `mcpToolProvider` i hvilken som helst agent som trenger MCP-verktøy:

```java
// Trinn 1: FileAgent leser filer ved hjelp av MCP-verktøy
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Har MCP-verktøy for filoperasjoner
        .build();

// Trinn 2: ReportAgent genererer strukturerte rapporter
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Veileder koordinerer arbeidsflyten fra fil til rapport
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Returner den endelige rapporten
        .build();

// Veilederen bestemmer hvilke agenter som skal kalles basert på forespørselen
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Responsstrategier

Når du konfigurerer en `SupervisorAgent`, angir du hvordan den skal formulere sitt endelige svar til brukeren etter at under-agentene har fullført oppgavene sine. Diagrammet under viser tre tilgjengelige strategier — LAST returnerer den siste agentens output direkte, SUMMARY syntetiserer alle outputs gjennom en LLM, og SCORED velger den som scorer høyest mot den opprinnelige forespørselen:

<img src="../../../translated_images/no/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Tre strategier for hvordan Supervisor formulerer sitt endelige svar — velg basert på om du vil ha siste agents output, en syntetisert oppsummering eller den best scorede varianten.*

De tilgjengelige strategiene er:

| Strategi | Beskrivelse |
|----------|-------------|
| **LAST** | Supervisorn returnerer output fra den siste under-agenten eller verktøyet som ble kalt. Dette er nyttig når den siste agenten i arbeidsflyten er spesielt designet for å produsere det komplette, endelige svaret (f.eks. en "Oppsummeringsagent" i en forskningspipeline). |
| **SUMMARY** | Supervisorn bruker sin egen interne språkmodell (LLM) for å oppsummere hele interaksjonen og alle under-agenters output, og returnerer denne oppsummeringen som det endelige svaret. Dette gir et ryddig, aggregert svar til brukeren. |
| **SCORED** | Systemet bruker en intern LLM for å score både LAST-svaret og SUMMARYe av interaksjonen mot den opprinnelige brukerforespørselen, og returnerer det output som får høyest poeng. |
Se [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) for fullstendig implementering.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) og spør:
> - "Hvordan bestemmer Supervisor hvilke agenter som skal kalles?"
> - "Hva er forskjellen mellom Supervisor og Sequential arbeidsflytmønstre?"
> - "Hvordan kan jeg tilpasse Supervisors planleggingsadferd?"

#### Forstå Utdata

Når du kjører demoen, vil du se en strukturert gjennomgang av hvordan Supervisor orkestrerer flere agenter. Her er hva hver seksjon betyr:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Overskriften** introduserer arbeidsflytkonseptet: en fokusert pipeline fra filopplesning til rapportgenerering.

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

**Arbeidsflytdiagram** viser dataflyten mellom agenter. Hver agent har en spesifikk rolle:
- **FileAgent** leser filer ved hjelp av MCP-verktøy og lagrer råinnhold i `fileContent`
- **ReportAgent** bruker det innholdet og produserer en strukturert rapport i `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Brukerforespørsel** viser oppgaven. Supervisor analyserer dette og bestemmer at FileAgent → ReportAgent skal kalles.

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

**Supervisor Orkestrering** viser den 2-trinns flyten i praksis:
1. **FileAgent** leser filen via MCP og lagrer innholdet
2. **ReportAgent** mottar innholdet og genererer en strukturert rapport

Supervisoren tok disse beslutningene **autonomt** basert på brukerens forespørsel.

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

#### Forklaring av Agentic-modulens funksjoner

Eksempelet demonstrerer flere avanserte funksjoner i agentic-modulen. La oss se nærmere på Agentic Scope og Agent Listeners.

**Agentic Scope** viser delt minne der agenter lagret sine resultater ved bruk av `@Agent(outputKey="...")`. Dette gjør at:
- Senere agenter kan få tilgang til tidligere agenters output
- Supervisoren kan syntetisere et endelig svar
- Du kan inspisere hva hver agent produserte

Diagrammet under viser hvordan Agentic Scope fungerer som delt minne i fil-til-rapport-arbeidsflyten — FileAgent skriver sin output under nøkkelen `fileContent`, ReportAgent leser det og skriver sin egen output under `report`:

<img src="../../../translated_images/no/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope fungerer som delt minne — FileAgent skriver `fileContent`, ReportAgent leser den og skriver `report`, og koden din leser det endelige resultatet.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Rå fildata fra FileAgent
String report = scope.readState("report");            // Strukturert rapport fra ReportAgent
```

**Agent Listeners** muliggjør overvåking og feilsøking av agentutførelse. Det trinnvise utdataet du ser i demoen kommer fra en AgentListener som kobles til hver agentinvolvering:
- **beforeAgentInvocation** - Kalles når Supervisor velger en agent, slik at du kan se hvilken agent som ble valgt og hvorfor
- **afterAgentInvocation** - Kalles når en agent fullfører, og viser resultatet
- **inheritedBySubagents** - Når sann, overvåker lytteren alle agenter i hierarkiet

Diagrammet under viser hele livssyklusen til Agent Listener, inkludert hvordan `onError` håndterer feil under agentutførelse:

<img src="../../../translated_images/no/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners kobler seg til utførelseslivssyklusen — overvåk når agenter starter, fullfører eller møter feil.*

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

Utover Supervisor-mønsteret tilbyr `langchain4j-agentic` modulen flere kraftige arbeidsflytmønstre. Diagrammet under viser alle fem — fra enkle sekvensielle pipelines til menneske-i-løkken godkjenningsarbeidsflyter:

<img src="../../../translated_images/no/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Fem arbeidsflytmønstre for orkestrering av agenter — fra enkle sekvensielle pipelines til menneske-i-løkken godkjenningsarbeidsflyter.*

| Mønster | Beskrivelse | Brukstilfelle |
|---------|-------------|---------------|
| **Sequential** | Kjør agenter i rekkefølge, output går til neste | Pipelines: forskning → analyse → rapport |
| **Parallel** | Kjør agenter samtidig | Uavhengige oppgaver: vær + nyheter + aksjer |
| **Loop** | Iterer til betingelse er oppfylt | Kvalitetsscore: forbedre til score ≥ 0.8 |
| **Conditional** | Rute basert på betingelser | Klassifiser → rute til spesialistagent |
| **Human-in-the-Loop** | Legg til menneskelige kontrollpunkter | Godkjenningsarbeidsflyter, innholdsgranskning |

## Nøkkelbegreper

Nå som du har utforsket MCP og agentic-modulen i praksis, la oss oppsummere når du bør bruke hver tilnærming.

En av MCPs største fordeler er det voksende økosystemet. Diagrammet under viser hvordan en enkelt universell protokoll kobler din AI-applikasjon til et bredt utvalg av MCP-servere — fra filsystem og database-tilgang til GitHub, e-post, webskraping med mer:

<img src="../../../translated_images/no/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP skaper et universelt protokolløkosystem — enhver MCP-kompatibel server fungerer med hvilken som helst MCP-kompatibel klient, noe som muliggjør verktøydeling på tvers av applikasjoner.*

**MCP** er ideelt når du vil utnytte eksisterende verktøyøkosystemer, bygge verktøy som flere applikasjoner kan dele, integrere tredjepartstjenester med standardprotokoller, eller bytte ut verktøymoduler uten å endre kode.

**Agentic-modulen** fungerer best når du ønsker deklarative agentdefinisjoner med `@Agent`-annotasjoner, trenger arbeidsflytorkestrering (sekvensiell, sløyfe, parallell), foretrekker grensesnittbasert agentdesign fremfor imperativ kode, eller kombinerer flere agenter som deler output via `outputKey`.

**Supervisor Agent-mønsteret** skinner når arbeidsflyten ikke er forutsigbar på forhånd og du vil at LLM skal bestemme, når du har flere spesialiserte agenter som trenger dynamisk orkestrering, når du bygger konversasjonelle systemer som ruter til ulike kapasiteter, eller når du ønsker det mest fleksible, adaptive agentadferd.

For å hjelpe deg å velge mellom tilpassede `@Tool`-metoder fra Modul 04 og MCP-verktøy fra denne modulen, viser følgende sammenligning nøkkelavveiningene — tilpassede verktøy gir tett kobling og full typesikkerhet for app-spesifikk logikk, mens MCP-verktøy tilbyr standardiserte, gjenbrukbare integrasjoner:

<img src="../../../translated_images/no/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Når du skal bruke tilpassede @Tool-metoder kontra MCP-verktøy — tilpassede verktøy for app-spesifikk logikk med full typesikkerhet, MCP-verktøy for standardiserte integrasjoner som fungerer på tvers av applikasjoner.*

## Gratulerer!

Du har fullført alle fem modulene i LangChain4j for Beginners-kurset! Her er en oversikt over hele læringsreisen du har fullført — fra enkel chat til MCP-drevne agentiske systemer:

<img src="../../../translated_images/no/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Din læringsreise gjennom alle fem moduler — fra enkel chat til MCP-drevne agentiske systemer.*

Du har fullført LangChain4j for Beginners-kurset. Du har lært:

- Hvordan bygge konversasjonell AI med minne (Modul 01)
- Prompt engineering-mønstre for ulike oppgaver (Modul 02)
- Å forankre svar i dokumenter med RAG (Modul 03)
- Å lage grunnleggende AI-agenter (assistenter) med tilpassede verktøy (Modul 04)
- Å integrere standardiserte verktøy med LangChain4j MCP- og Agentic-modulene (Modul 05)

### Hva nå?

Etter å ha fullført modulene, utforsk [Testing Guide](../docs/TESTING.md) for å se LangChain4j testkonsepter i praksis.

**Offisielle ressurser:**
- [LangChain4j Dokumentasjon](https://docs.langchain4j.dev/) - Omfattende guider og API-referanse
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Kildekode og eksempler
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - Trinnvise veiledninger for ulike brukstilfeller

Takk for at du fullførte kurset!

---

**Navigasjon:** [← Forrige: Modul 04 - Verktøy](../04-tools/README.md) | [Tilbake til Hovedside](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved bruk av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi legger vekt på nøyaktighet, vennligst vær klar over at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det originale dokumentet på originalspråket bør betraktes som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi kan ikke holdes ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->