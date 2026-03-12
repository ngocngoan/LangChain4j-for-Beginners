# Modu 05: Model Context Protocol (MCP)

## Innholdsfortegnelse

- [Video Gjennomgang](../../../05-mcp)
- [Hva Du Vil Lære](../../../05-mcp)
- [Hva er MCP?](../../../05-mcp)
- [Hvordan MCP Fungerer](../../../05-mcp)
- [Den Agentiske Modulen](../../../05-mcp)
- [Kjøre Eksemplene](../../../05-mcp)
  - [Forutsetninger](../../../05-mcp)
- [Rask Start](../../../05-mcp)
  - [Filoperasjoner (Stdio)](../../../05-mcp)
  - [Supervisor Agent](../../../05-mcp)
    - [Kjøre Demoen](../../../05-mcp)
    - [Hvordan Supervisor Fungerer](../../../05-mcp)
    - [Hvordan FileAgent Oppdager MCP Verktøy ved Kjøretid](../../../05-mcp)
    - [Responsstrategier](../../../05-mcp)
    - [Forstå Utdataene](../../../05-mcp)
    - [Forklaring av Agentiske Modulfunksjoner](../../../05-mcp)
- [Nøkkelbegreper](../../../05-mcp)
- [Gratulerer!](../../../05-mcp)
  - [Hva Nå?](../../../05-mcp)

## Video Gjennomgang

Se denne live-sesjonen som forklarer hvordan du kommer i gang med denne modulen:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="AI Agents with Tools and MCP - Live Session" width="800"/></a>

## Hva Du Vil Lære

Du har bygget samtale-AI, mestret prompts, forankret svar i dokumenter, og laget agenter med verktøy. Men alle de verktøyene var spesialbygget for din spesifikke applikasjon. Hva om du kunne gi AI-en din tilgang til et standardisert økosystem av verktøy som alle kan lage og dele? I denne modulen lærer du hvordan du gjør nettopp det med Model Context Protocol (MCP) og LangChain4js agentiske modul. Vi viser først en enkel MCP fil-leser og deretter hvordan den lett integreres i avanserte agentiske arbeidsflyter ved bruk av Supervisor Agent mønsteret.

## Hva er MCP?

Model Context Protocol (MCP) gir nettopp dette – en standard måte for AI-applikasjoner å oppdage og bruke eksterne verktøy. I stedet for å skrive spesialtilpassede integrasjoner for hver datakilde eller tjeneste, kobler du til MCP-servere som eksponerer sine muligheter i et konsistent format. Din AI-agent kan så automatisk oppdage og benytte disse verktøyene.

Diagrammet under viser forskjellen – uten MCP krever hver integrasjon spesialtilpasset punkt-til-punkt kobling; med MCP kobler én protokoll appen din til hvilket som helst verktøy:

<img src="../../../translated_images/no/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Før MCP: Komplekse punkt-til-punkt-integrasjoner. Etter MCP: Én protokoll, endeløse muligheter.*

MCP løser et grunnleggende problem i AI-utvikling: hver integrasjon er spesiallaget. Vil du ha tilgang til GitHub? Spesialkode. Vil du lese filer? Spesialkode. Vil du spørre en database? Spesialkode. Og ingen av disse integrasjonene fungerer med andre AI-applikasjoner.

MCP standardiserer dette. En MCP-server eksponerer verktøy med klare beskrivelser og skjemaer. Hvilken som helst MCP-klient kan koble til, oppdage tilgjengelige verktøy, og bruke dem. Bygg én gang, bruk overalt.

Diagrammet under illustrerer denne arkitekturen – en enkel MCP-klient (din AI-applikasjon) kobler til flere MCP-servere, hver som eksponerer sitt eget sett av verktøy via standard protokoll:

<img src="../../../translated_images/no/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol arkitektur – standardisert verktøyoppdagelse og kjøring*

## Hvordan MCP Fungerer

Under panseret bruker MCP en lagdelt arkitektur. Din Java-applikasjon (MCP-klienten) oppdager verktøy, sender JSON-RPC-forespørsler gjennom et transportlag (Stdio eller HTTP), og MCP-serveren utfører operasjoner og returnerer resultater. Følgende diagram forklarer hvert lag i denne protokollen:

<img src="../../../translated_images/no/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Hvordan MCP fungerer under panseret — klienter oppdager verktøy, utveksler JSON-RPC meldinger, og utfører operasjoner gjennom et transportlag.*

**Server-Klient Arkitektur**

MCP bruker et klient-server modell. Servere tilbyr verktøy – lese filer, spørre databaser, kalle APIer. Klienter (din AI-applikasjon) kobler seg til servere og bruker verktøyene.

For å bruke MCP med LangChain4j, legg til denne Maven-avhengigheten:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Verktøyoppdagelse**

Når klienten din kobler til en MCP-server, spør den "Hvilke verktøy har dere?" Serveren svarer med en liste over tilgjengelige verktøy, hver med beskrivelser og parameter-skjemaer. Din AI-agent kan så avgjøre hvilke verktøy den vil bruke basert på brukerens forespørsler. Diagrammet under viser dette håndtrykket – klienten sender en `tools/list` forespørsel og serveren returnerer sine tilgjengelige verktøy med beskrivelser og parameter-skjemaer:

<img src="../../../translated_images/no/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI-en oppdager tilgjengelige verktøy ved oppstart — den vet nå hvilke muligheter som finnes og kan avgjøre hvilke som skal brukes.*

**Transportmekanismer**

MCP støtter forskjellige transportmekanismer. De to alternativene er Stdio (for lokal prosesskommunikasjon) og Streamable HTTP (for fjerne servere). Denne modulen demonstrerer Stdio-transport:

<img src="../../../translated_images/no/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP transportmekanismer: HTTP for fjerne servere, Stdio for lokale prosesser*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

For lokale prosesser. Applikasjonen din starter en server som en underprosess og kommuniserer via standard input/output. Nyttig for tilgang til filsystem eller kommandolinjeverktøy.

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

`@modelcontextprotocol/server-filesystem` serveren eksponerer følgende verktøy, alle sandboxet til de katalogene du angir:

| Verktøy | Beskrivelse |
|---------|-------------|
| `read_file` | Les innholdet i en enkelt fil |
| `read_multiple_files` | Les flere filer i én kall |
| `write_file` | Opprett eller overskriv en fil |
| `edit_file` | Gjør målrettede søk-og-erstatt-endringer |
| `list_directory` | List filer og kataloger på en sti |
| `search_files` | Rekursivt søk etter filer som matcher et mønster |
| `get_file_info` | Hent filmetadata (størrelse, tidsstempler, rettigheter) |
| `create_directory` | Opprett en katalog (inkludert nødvendige overordnede kataloger) |
| `move_file` | Flytt eller gi nytt navn til en fil eller katalog |

Diagrammet under viser hvordan Stdio-trafikk fungerer ved kjøretid — din Java-applikasjon starter MCP-serveren som en underprosess og de kommuniserer via stdin/stdout rør, uten nettverk eller HTTP involvert:

<img src="../../../translated_images/no/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio-transport i praksis — applikasjonen starter MCP-server som underprosess og kommuniserer via stdin/stdout rør.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) og spør:
> - "Hvordan fungerer Stdio transport og når bør jeg bruke det vs HTTP?"
> - "Hvordan håndterer LangChain4j livssyklusen til spawnede MCP serverprosesser?"
> - "Hva er sikkerhetsimplikasjonene ved å gi AI tilgang til filsystemet?"

## Den Agentiske Modulen

Mens MCP gir standardiserte verktøy, tilbyr LangChain4js **agentiske modul** en deklarativ måte å bygge agenter som orkestrerer disse verktøyene. `@Agent` annotasjonen og `AgenticServices` lar deg definere agentatferd gjennom grensesnitt snarere enn imperativ kode.

I denne modulen utforsker du **Supervisor Agent** mønsteret – en avansert agentisk AI-tilnærming hvor en "veileder" agent dynamisk avgjør hvilke under-agenter som skal kalles basert på brukerens forespørsel. Vi kombinerer begge konsepter ved å gi en av våre under-agenter MCP-drevne filtilgangsmuligheter.

For å bruke den agentiske modulen, legg til denne Maven-avhengigheten:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Merk:** `langchain4j-agentic` modulen bruker en separat versjonsegenskap (`langchain4j.mcp.version`) fordi den slippes på et annet skjema enn kjernebibliotekene til LangChain4j.

> **⚠️ Eksperimentell:** `langchain4j-agentic` modulen er **eksperimentell** og kan endres. Den stabile måten å bygge AI-assistenter på er fortsatt `langchain4j-core` med spesialtilpassede verktøy (Modul 04).

## Kjøre Eksemplene

### Forutsetninger

- Fullført [Modul 04 - Verktøy](../04-tools/README.md) (denne modulen bygger på konseptet med spesiallagde verktøy og sammenligner dem med MCP-verktøy)
- `.env` fil i rotkatalogen med Azure legitimasjon (laget av `azd up` i Modul 01)
- Java 21+, Maven 3.9+
- Node.js 16+ og npm (for MCP-servere)

> **Merk:** Hvis du ikke har satt opp miljøvariablene dine enda, se [Modul 01 - Introduksjon](../01-introduction/README.md) for deployeringsinstruksjoner (`azd up` lager `.env` filen automatisk), eller kopier `.env.example` til `.env` i rotkatalog og fyll inn dine verdier.

## Rask Start

**Bruke VS Code:** Høyreklikk bare på en vilkårlig demofil i Explorer og velg **"Run Java"**, eller bruk launch-konfigurasjonene fra Run and Debug panel (sørg for at `.env` filen din er konfigurert med Azure legitimasjon først).

**Bruke Maven:** Alternativt kan du kjøre fra kommandolinje med eksemplene under.

### Filoperasjoner (Stdio)

Dette demonstrerer verktøy basert på lokale underprosesser.

**✅ Ingen forutsetninger trengs** – MCP-serveren startes automatisk.

**Bruke Startskriptene (Anbefalt):**

Startskriptene laster automatisk miljøvariabler fra rotens `.env` fil:

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

**Bruke VS Code:** Høyreklikk på `StdioTransportDemo.java` og velg **"Run Java"** (sørg for at `.env` filen er konfigurert).

Applikasjonen starter en MCP-server for filsystem automatisk og leser en lokal fil. Legg merke til hvordan underprosessstyringen håndteres for deg.

**Forventet utdata:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor Agent

**Supervisor Agent mønsteret** er en **fleksibel** form for agentisk AI. En Supervisor bruker en LLM til autonomt å avgjøre hvilke agenter som skal kalles basert på brukerens forespørsel. I det neste eksempelet kombinerer vi MCP-drevet filtilgang med en LLM-agent for å skape en overvåket fil-les → rapport arbeidsflyt.

I demoen leser `FileAgent` en fil med MCP filsystemverktøy, og `ReportAgent` lager en strukturert rapport med et lederresymé (1 setning), 3 hovedpunkter og anbefalinger. Supervisor orkestrerer denne flyten automatisk:

<img src="../../../translated_images/no/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Supervisor bruker sin LLM til å avgjøre hvilke agenter som skal kalles og i hvilken rekkefølge – ingen hardkodet ruting nødvendig.*

Slik ser den konkrete arbeidsflyten ut for vår fil-til-rapport pipeline:

<img src="../../../translated_images/no/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent leser filen via MCP-verktøyene, så transformerer ReportAgent det rå innholdet til en strukturert rapport.*

Følge-diagrammet under sporer hele Supervisor-orkestreringen – fra spawning av MCP-serveren, gjennom Supervisors autonome agentvalg, til verktøykall over stdio og den endelige rapporten:

<img src="../../../translated_images/no/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*Supervisor kaller autonomt FileAgent (som kaller MCP-serveren over stdio for å lese filen), så kaller ReportAgent for å generere en strukturert rapport – hver agent lagrer sin output i delt Agentic Scope.*

Hver agent lagrer sin output i **Agentic Scope** (delt minne), som tillater agenter nedstrøms å få tilgang til tidligere resultater. Dette demonstrerer hvordan MCP-verktøy integreres sømløst i agentiske arbeidsflyter – Supervisor trenger ikke vite *hvordan* filer leses, bare at `FileAgent` kan gjøre det.

#### Kjøre Demoen

Startskriptene laster automatisk miljøvariabler fra rotens `.env` fil:

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

**Bruke VS Code:** Høyreklikk på `SupervisorAgentDemo.java` og velg **"Run Java"** (sørg for at `.env` filen er konfigurert).

#### Hvordan Supervisor Fungerer

Før du bygger agenter, må du koble MCP-transporten til en klient og pakke det som en `ToolProvider`. Slik blir MCP-serverens verktøy tilgjengelig for agentene dine:

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

// Supervisor orkestrerer arbeidsflyten fil → rapport
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Returner den endelige rapporten
        .build();

// Supervisor avgjør hvilke agenter som skal kalles basert på forespørselen
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Hvordan FileAgent Oppdager MCP Verktøy ved Kjøretid

Du lurer kanskje på: **hvordan vet `FileAgent` hvordan man bruker npm filsystemverktøyene?** Svaret er at det gjør den ikke – **LLMen** finner ut av det ved kjøretid gjennom verktøyskjemaer.
`FileAgent`-grensesnittet er bare en **promptdefinisjon**. Det har ingen innebygd kunnskap om `read_file`, `list_directory` eller andre MCP-verktøy. Slik skjer det fra start til slutt:

1. **Server starter:** `StdioMcpTransport` lanserer `@modelcontextprotocol/server-filesystem` npm-pakken som en underordnet prosess
2. **Verktøydiscovery:** `McpClient` sender en `tools/list` JSON-RPC-forespørsel til serveren, som svarer med verktøynavn, beskrivelser og parameterskjemaer (f.eks. `read_file` — *"Les hele innholdet i en fil"* — `{ path: string }`)
3. **Skjema-innsprøytning:** `McpToolProvider` pakker inn disse oppdagede skjemaene og gjør dem tilgjengelige for LangChain4j
4. **LLM bestemmer:** Når `FileAgent.readFile(path)` kalles, sender LangChain4j systemmeldingen, brukermeldingen, **og listen over verktøyskjemaer** til LLM. LLM leser verktøybeskrivelsene og genererer et verktøykall (f.eks. `read_file(path="/some/file.txt")`)
5. **Utførelse:** LangChain4j avskjærer verktøykallet, ruter det gjennom MCP-klienten tilbake til Node.js-underprosessen, henter resultatet og sender det tilbake til LLM

Dette er den samme [Tool Discovery](../../../05-mcp) mekanismen beskrevet ovenfor, men brukt spesielt for agent-arbeidsflyten. `@SystemMessage` og `@UserMessage`-annotasjonene styrer LLMs oppførsel, mens den injiserte `ToolProvider` gir den **evnene** — LLM brolegger de to i kjøretid.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) og spør:
> - "Hvordan vet denne agenten hvilket MCP-verktøy den skal kalle?"
> - "Hva ville skje hvis jeg fjernet ToolProvider fra agentbyggeren?"
> - "Hvordan blir verktøyskjemaene sendt til LLM?"

#### Responsstrategier

Når du konfigurerer en `SupervisorAgent`, angir du hvordan den skal formulere sitt endelige svar til brukeren etter at underagentene har fullført sine oppgaver. Diagrammet under viser de tre tilgjengelige strategiene — LAST returnerer det siste agentens output direkte, SUMMARY syntetiserer alle outputs via en LLM, og SCORED velger det som scorer høyest mot den opprinnelige forespørselen:

<img src="../../../translated_images/no/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Tre strategier for hvordan Supervisor formulerer sitt endelige svar — velg basert på om du vil ha siste agents output, en oppsummert syntese, eller det best rangerte alternativet.*

De tilgjengelige strategiene er:

| Strategi | Beskrivelse |
|----------|-------------|
| **LAST** | Supervisor returnerer output fra den siste sub-agenten eller verktøyet som ble kalt. Dette er nyttig når den siste agenten i arbeidsflyten er spesielt designet for å produsere det komplette, endelige svaret (f.eks. en "Summary Agent" i en forskningspipeline). |
| **SUMMARY** | Supervisor bruker sin egen interne språklige modell (LLM) for å syntetisere en oppsummering av hele interaksjonen og alle underagentens outputs, og returnerer så denne oppsummeringen som det endelige svaret. Dette gir et rent, aggregert svar til brukeren. |
| **SCORED** | Systemet bruker en intern LLM for å score både LAST-responsen og SUMMARY av interaksjonen mot den opprinnelige brukerforespørselen, og returnerer det outputet som får høyest poengsum. |

Se [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) for komplett implementasjon.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) og spør:
> - "Hvordan avgjør Supervisor hvilke agenter å kalle?"
> - "Hva er forskjellen på Supervisor og Sequential arbeidsflytmønstre?"
> - "Hvordan kan jeg tilpasse Supervisors planleggingsatferd?"

#### Forstå outputen

Når du kjører demoen, vil du se en strukturert gjennomgang av hvordan Supervisor orkestrerer flere agenter. Her er hva hver seksjon betyr:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Overskriften** introduserer arbeidsflytkonseptet: en fokusert pipeline fra fillesing til rapportgenerering.

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
- **ReportAgent** bruker dette innholdet og lager en strukturert rapport i `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Brukerforespørsel** viser oppgaven. Supervisor tolker denne og bestemmer å kalle FileAgent → ReportAgent.

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

**Supervisor Orkestrering** viser 2-trinnsflyten i praksis:
1. **FileAgent** leser filen via MCP og lagrer innholdet
2. **ReportAgent** mottar innholdet og genererer en strukturert rapport

Supervisor tok disse beslutningene **selvstendig** basert på brukerens forespørsel.

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

#### Forklaring av Agentic Module-funksjoner

Eksemplet demonstrerer flere avanserte funksjoner i agentic-modulen. La oss se nærmere på Agentic Scope og Agent Listeners.

**Agentic Scope** viser delt minne hvor agenter lagrer sine resultater med `@Agent(outputKey="...")`. Dette muliggjør:
- At senere agenter kan få tilgang til tidligere agenters output
- Supervisor kan syntetisere et endelig svar
- Du kan inspisere hva hver agent produserte

Diagrammet under viser hvordan Agentic Scope fungerer som delt minne i fil-til-rapport-arbeidsflyten — FileAgent skriver sin output under nøkkelen `fileContent`, ReportAgent leser den og skriver sin egen output under `report`:

<img src="../../../translated_images/no/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope fungerer som delt minne — FileAgent skriver `fileContent`, ReportAgent leser og skriver `report`, og koden din leser sluttresultatet.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Rå fildata fra FileAgent
String report = scope.readState("report");            // Strukturert rapport fra ReportAgent
```

**Agent Listeners** muliggjør overvåkning og debugging av agentkjøring. Steg-for-steg output du ser i demoen kommer fra en AgentListener som kobler seg på hver agentkall:
- **beforeAgentInvocation** - Kalles når Supervisor velger en agent, slik at du ser hvilken agent som ble valgt og hvorfor
- **afterAgentInvocation** - Kalles når en agent fullfører, og viser resultatet
- **inheritedBySubagents** - Når sann, overvåker listeneren alle agenter i hierarkiet

Diagrammet under viser hele livssyklusen for Agent Listener, inkludert hvordan `onError` håndterer feil under agentkjøring:

<img src="../../../translated_images/no/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners kobler seg på kjøringssyklusen — overvåk når agenter starter, fullfører eller får feil.*

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
        return true; // Spre til alle underagenter
    }
};
```

Utover Supervisor-mønsteret tilbyr `langchain4j-agentic` flere kraftfulle arbeidsflytmønstre. Diagrammet under viser alle fem — fra enkle sekvensielle pipelines til godkjenningsarbeidsflyt med menneskelig interaksjon:

<img src="../../../translated_images/no/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Fem arbeidsflytmønstre for å orkestrere agenter — fra enkle sekvensielle pipelines til godkjenningsarbeidsflyt med menneskelig interaksjon.*

| Mønster | Beskrivelse | Brukstilfelle |
|---------|-------------|---------------|
| **Sequential** | Utfør agenter etter hverandre, output flyter til neste | Pipelines: forskning → analyse → rapport |
| **Parallel** | Kjør agenter samtidig | Uavhengige oppgaver: vær + nyheter + aksjer |
| **Loop** | Iterer til betingelse er oppfylt | Kvalitetsscore: forbedre til score ≥ 0.8 |
| **Conditional** | Rute basert på betingelser | Klassifiser → rute til spesialistagent |
| **Human-in-the-Loop** | Legg til menneskelige kontrollpunkter | Godkjenningsprosesser, innholdsgranskning |

## Nøkkelkonsepter

Nå som du har utforsket MCP og agentic-modulen i praksis, la oss oppsummere når du bør bruke hver tilnærming.

En av MCPs største fordeler er det voksende økosystemet. Diagrammet under viser hvordan en enkelt universell protokoll kobler AI-applikasjonen din til en bred variasjon av MCP-servere — fra filsystem- og database-tilgang til GitHub, e-post, nettskraping med mer:

<img src="../../../translated_images/no/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP skaper et universelt protokollekosystem — enhver MCP-kompatibel server fungerer med hvilken som helst MCP-kompatibel klient, noe som muliggjør verktøydeling mellom applikasjoner.*

**MCP** er ideelt når du vil dra nytte av eksisterende verktøysøkosystemer, bygge verktøy som flere applikasjoner kan dele, integrere tredjepartstjenester med standard protokoller, eller bytte ut verktøysimplementasjoner uten å endre kode.

**Agentic-modulen** fungerer best når du ønsker deklarative agentdefinisjoner med `@Agent`-annotasjoner, trenger arbeidsflytorkestrering (sekvensiell, loop, parallell), foretrekker grensesnittbasert agentdesign fremfor imperativ kode, eller kombinerer flere agenter som deler output via `outputKey`.

**Supervisor Agent-mønsteret** passer utmerket når arbeidsflyten ikke er forutsigbar på forhånd og du vil at LLM skal avgjøre, når du har flere spesialiserte agenter som trenger dynamisk orkestrering, når du bygger samtalesystemer som ruter til ulike kapasiteter, eller når du ønsker mest mulig fleksibel, adaptiv agentadferd.

For å hjelpe deg å velge mellom egendefinerte `@Tool`-metoder fra modul 04 og MCP-verktøy fra denne modulen, fremhever følgende sammenligning nøkkelavveiningene — egendefinerte verktøy gir tett kobling og full typesikkerhet for app-spesifikk logikk, mens MCP-verktøy tilbyr standardiserte, gjenbrukbare integrasjoner:

<img src="../../../translated_images/no/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Når du bør bruke egendefinerte @Tool-metoder vs MCP-verktøy — egendefinerte verktøy for app-spesifikk logikk med full typesikkerhet, MCP-verktøy for standardiserte integrasjoner som fungerer på tvers av applikasjoner.*

## Gratulerer!

Du har fullført alle fem modulene i LangChain4j for nybegynnere-kurset! Her er et overblikk over hele læringsreisen du har gjennomført — fra grunnleggende chat til MCP-drevne agentiske systemer:

<img src="../../../translated_images/no/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Din læringsreise gjennom alle fem moduler — fra grunnleggende chat til MCP-drevne agentiske systemer.*

Du har lært:

- Hvordan bygge samtale-AI med minne (Modul 01)
- Prompt-teknikker for ulike oppgaver (Modul 02)
- Å forankre svar i dokumenter med RAG (Modul 03)
- Å lage grunnleggende AI-agenter (assistenter) med egendefinerte verktøy (Modul 04)
- Å integrere standardiserte verktøy med LangChain4j MCP og Agentic-moduler (Modul 05)

### Hva nå?

Etter å ha fullført modulene, utforsk [Testing Guide](../docs/TESTING.md) for å se LangChain4j testkonsepter i praksis.

**Offisielle ressurser:**
- [LangChain4j Dokumentasjon](https://docs.langchain4j.dev/) - Omfattende guider og API-referanse
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Kildekode og eksempler
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - Steg-for-steg tutorials for ulike brukstilfeller

Takk for at du fullførte dette kurset!

---

**Navigasjon:** [← Forrige: Modul 04 - Verktøy](../04-tools/README.md) | [Tilbake til Hoved](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vennligst vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det opprinnelige dokumentet på dets opprinnelige språk bør betraktes som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår fra bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->