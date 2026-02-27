# Modul 05: Model Context Protocol (MCP)

## Innholdsfortegnelse

- [Hva du vil lære](../../../05-mcp)
- [Hva er MCP?](../../../05-mcp)
- [Hvordan MCP fungerer](../../../05-mcp)
- [Den Agentiske Modulen](../../../05-mcp)
- [Kjøre eksemplene](../../../05-mcp)
  - [Forutsetninger](../../../05-mcp)
- [Rask start](../../../05-mcp)
  - [Filoperasjoner (Stdio)](../../../05-mcp)
  - [Veilederagent](../../../05-mcp)
    - [Kjøre demoen](../../../05-mcp)
    - [Hvordan veilederen fungerer](../../../05-mcp)
    - [Forstå outputten](../../../05-mcp)
    - [Forklaring av Agentiske modul-funksjoner](../../../05-mcp)
- [Nøkkelbegreper](../../../05-mcp)
- [Gratulerer!](../../../05-mcp)
  - [Hva nå?](../../../05-mcp)

## Hva du vil lære

Du har bygget konverserende AI, mestret prompts, forankret svar i dokumenter og opprettet agenter med verktøy. Men alle disse verktøyene var skreddersydd for din spesifikke applikasjon. Hva om du kunne gi AI-en din tilgang til et standardisert økosystem av verktøy som hvem som helst kan lage og dele? I denne modulen vil du lære akkurat det med Model Context Protocol (MCP) og LangChain4j sin agentiske modul. Vi viser først en enkel MCP fil-leser og deretter hvordan den lett integreres i avanserte agentiske arbeidsflyter ved bruk av Veilederagent-mønsteret.

## Hva er MCP?

Model Context Protocol (MCP) tilbyr akkurat det – en standard måte for AI-applikasjoner å oppdage og bruke eksterne verktøy på. I stedet for å skrive tilpassede integrasjoner for hver datakilde eller tjeneste, kobler du til MCP-servere som eksponerer sine kapabiliteter i et konsistent format. Din AI-agent kan da automatisk oppdage og bruke disse verktøyene.

<img src="../../../translated_images/no/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Før MCP: Komplekse punkt-til-punkt-integrasjoner. Etter MCP: Ett protokoll, uendelige muligheter.*

MCP løser et grunnleggende problem i AI-utvikling: hver integrasjon er tilpasset. Vil du få tilgang til GitHub? Tilpasset kode. Vil du lese filer? Tilpasset kode. Vil du spørre en database? Tilpasset kode. Og ingen av disse integrasjonene fungerer med andre AI-applikasjoner.

MCP standardiserer dette. En MCP-server eksponerer verktøy med klare beskrivelser og skjemaer. Enhver MCP-klient kan koble til, oppdage tilgjengelige verktøy og bruke dem. Bygg én gang, bruk overalt.

<img src="../../../translated_images/no/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol arkitektur - standardisert verktøysoppdagelse og utførelse*

## Hvordan MCP fungerer

<img src="../../../translated_images/no/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Hvordan MCP fungerer under panseret — klienter oppdager verktøy, utveksler JSON-RPC meldinger, og utfører operasjoner gjennom et transportlag.*

**Server-klient-arkitektur**

MCP bruker en klient-server-modell. Servere tilbyr verktøy – leser filer, spørrer databaser, kaller APIer. Klienter (din AI-applikasjon) kobler til servere og bruker verktøyene deres.

For å bruke MCP med LangChain4j, legg til dette Maven-avhengighetsforholdet:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Verktøysoppdagelse**

Når klienten din kobler til en MCP-server, spør den "Hvilke verktøy har du?" Serveren svarer med en liste over tilgjengelige verktøy, hver med beskrivelser og parameter-skjemaer. Din AI-agent kan så avgjøre hvilke verktøy den skal bruke basert på brukerforespørsler.

<img src="../../../translated_images/no/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI-en oppdager tilgjengelige verktøy ved oppstart — den vet nå hvilke kapasiteter som er tilgjengelige og kan bestemme hvilke den skal bruke.*

**Transportmekanismer**

MCP støtter ulike transportmekanismer. Denne modulen demonstrerer Stdio-transport for lokale prosesser:

<img src="../../../translated_images/no/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP transportmekanismer: HTTP for fjernservere, Stdio for lokale prosesser*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

For lokale prosesser. Applikasjonen din starter en server som en underprosess og kommuniserer gjennom standard input/output. Nyttig for tilgang til filsystem eller kommandolinjeverktøy.

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

<img src="../../../translated_images/no/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio-transport i praksis — applikasjonen din starter MCP-serveren som en barneprosess og kommuniserer gjennom stdin/stdout rør.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) og spør:
> - "Hvordan fungerer Stdio-transport og når bør jeg bruke den kontra HTTP?"
> - "Hvordan håndterer LangChain4j livssyklusen til spawnede MCP-serverprosesser?"
> - "Hva er sikkerhetsimplikasjonene av å gi AI tilgang til filsystemet?"

## Den Agentiske Modulen

Mens MCP tilbyr standardiserte verktøy, gir LangChain4j sin **agentiske modul** en deklarativ måte å bygge agenter som orkestrerer disse verktøyene på. `@Agent`-annotasjonen og `AgenticServices` lar deg definere agentadferd gjennom grensesnitt i stedet for imperativ kode.

I denne modulen utforsker du **Veilederagent**-mønsteret — en avansert agentisk AI-tilnærming der en "veileder" agent dynamisk avgjør hvilke under-agenter som skal kalles basert på brukerforespørsler. Vi kombinerer begge konsepter ved å gi en av våre under-agenter MCP-drevne filtilgangskapasiteter.

For å bruke den agentiske modulen, legg til dette Maven-avhengighetsforholdet:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **⚠️ Eksperimentell:** `langchain4j-agentic` modulen er **eksperimentell** og kan endres. Den stabile måten å bygge AI-assistenter på er fortsatt `langchain4j-core` med tilpassede verktøy (Modul 04).

## Kjøre eksemplene

### Forutsetninger

- Java 21+, Maven 3.9+
- Node.js 16+ og npm (for MCP-servere)
- Miljøvariabler konfigurert i `.env`-fil (fra rotkatalogen):
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (som i Moduler 01-04)

> **Merk:** Hvis du ikke har satt opp miljøvariablene dine enda, se [Modul 00 - Rask Start](../00-quick-start/README.md) for instruksjoner, eller kopier `.env.example` til `.env` i rotkatalogen og fyll inn dine verdier.

## Rask start

**Bruker du VS Code:** Høyreklikk enkelt på en demo-fil i Utforskeren og velg **"Run Java"**, eller bruk startkonfigurasjonene i Run and Debug-panelet (sørg for at tokenet ditt er lagt inn i `.env`-filen først).

**Bruker du Maven:** Alternativt kan du kjøre via kommandolinjen med eksemplene nedenfor.

### Filoperasjoner (Stdio)

Dette demonstrerer verktøy basert på lokale underprosesser.

**✅ Ingen forutsetninger trengs** – MCP-serveren startes automatisk.

**Bruke startskriptene (anbefalt):**

Startskriptene laster automatisk miljøvariabler fra `.env` i rotkatalogen:

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

**Bruker du VS Code:** Høyreklikk på `StdioTransportDemo.java` og velg **"Run Java"** (sørg for at `.env`-filen er konfigurert).

Applikasjonen starter en MCP-server for filsystemet automatisk og leser en lokal fil. Legg merke til hvordan underprosesshåndteringen styres for deg.

**Forventet output:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Veilederagent

**Veilederagent-mønsteret** er en **fleksibel** form for agentisk AI. En Veileder bruker en LLM til autonomt å avgjøre hvilke agenter som skal kalles basert på brukerens forespørsel. I neste eksempel kombinerer vi MCP-drevet filtilgang med en LLM-agent for å skape en overvåket les fil → rapport workflow.

I demoen leser `FileAgent` en fil med MCP filsystemverktøy, og `ReportAgent` genererer en strukturert rapport med en lederoppsummering (1 setning), 3 hovedpunkter og anbefalinger. Veilederen orkestrerer denne flyten automatisk:

<img src="../../../translated_images/no/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Veilederen bruker sin LLM til å avgjøre hvilke agenter som skal kalles og i hvilken rekkefølge — ingen hardkodet ruting nødvendig.*

Slik ser den konkrete arbeidsflyten ut for vår fil-til-rapport-pipeline:

<img src="../../../translated_images/no/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent leser filen via MCP-verktøy, så transformerer ReportAgent råinnholdet til en strukturert rapport.*

Hver agent lagrer sitt output i **Agentic Scope** (delt minne), som lar etterfølgende agenter hente tidligere resultater. Dette demonstrerer hvordan MCP-verktøy sømløst integreres i agentiske arbeidsflyter — Veilederen trenger ikke vite *hvordan* filer leses, bare at `FileAgent` kan gjøre det.

#### Kjøre demoen

Startskriptene laster automatisk miljøvariabler fra rotens `.env`-fil:

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

**Bruker du VS Code:** Høyreklikk på `SupervisorAgentDemo.java` og velg **"Run Java"** (sørg for at `.env`-filen er konfigurert).

#### Hvordan veilederen fungerer

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

// Veileder orkestrerer fil → rapport arbeidsflyt
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Returner den endelige rapporten
        .build();

// Veilederen bestemmer hvilke agenter som skal kalles basert på forespørselen
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Responsstrategier

Når du konfigurerer en `SupervisorAgent`, spesifiserer du hvordan den skal formulere sitt endelige svar til brukeren etter at under-agentene har fullført oppgavene sine.

<img src="../../../translated_images/no/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Tre strategier for hvordan Veilederen formulerer sitt endelige svar — velg basert på om du vil ha siste agents output, en syntetisert oppsummering, eller den best rangerte utgaven.*

De tilgjengelige strategiene er:

| Strategi | Beskrivelse |
|----------|-------------|
| **LAST** | Veilederen returnerer outputen fra den siste under-agenten eller verktøyet som ble kalt. Dette er nyttig når siste agent i arbeidsflyten er spesifikt designet for å produsere det komplette, endelige svaret (f.eks. en "Oppsummeringsagent" i en forskningspipeline). |
| **SUMMARY** | Veilederen bruker sin egen interne språkmodell (LLM) til å syntetisere en oppsummering av hele interaksjonen og alle under-agenters output, og returnerer denne oppsummeringen som det endelige svaret. Dette gir et rent, samlet svar til brukeren. |
| **SCORED** | Systemet bruker en intern LLM til å score både LAST-responsen og SUMMARY av interaksjonen opp mot den originale brukerforespørselen, og returnerer det outputet som får høyest poengsum. |

Se [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) for komplett implementasjon.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) og spør:
> - "Hvordan bestemmer Veilederen hvilke agenter den skal kalle?"
> - "Hva er forskjellen på Veileder- og Sekvensielt arbeidsmønster?"
> - "Hvordan kan jeg tilpasse Veilederens planleggingsadferd?"

#### Forstå outputten

Når du kjører demoen, vil du se en strukturert gjennomgang av hvordan Veilederen orkestrerer flere agenter. Her er hva hver seksjon betyr:

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

**Arbeidsflytdiagrammet** viser dataflyten mellom agentene. Hver agent har en spesifikk rolle:
- **FileAgent** leser filer ved hjelp av MCP-verktøy og lagrer råinnhold i `fileContent`
- **ReportAgent** bruker dette innholdet og produserer en strukturert rapport i `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Brukerforespørsel** viser oppgaven. Veilederen parser dette og bestemmer at den skal kalle FileAgent → ReportAgent.

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

**Veilederorkestrering** viser 2-trinns flyten i aksjon:
1. **FileAgent** leser filen via MCP og lagrer innholdet
2. **ReportAgent** mottar innholdet og genererer en strukturert rapport

Veilederen tok disse beslutningene **autonomt** basert på brukerens forespørsel.

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

#### Forklaring av Agentiske modul-funksjoner

Eksemplet demonstrerer flere avanserte funksjoner av den agentiske modulen. La oss se nærmere på Agentic Scope og Agent Listeners.

**Agentic Scope** viser det delte minnet der agenter lagret sine resultater ved bruk av `@Agent(outputKey="...")`. Dette gjør at:
- Senere agenter kan få tilgang til tidligere agenters output
- Veilederen kan syntetisere et slutt-svar
- Du kan inspisere hva hver agent produserte

<img src="../../../translated_images/no/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope fungerer som delt minne — FileAgent skriver `fileContent`, ReportAgent leser det og skriver `report`, og koden din leser det endelige resultatet.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Rå fildata fra FileAgent
String report = scope.readState("report");            // Strukturert rapport fra ReportAgent
```

**Agent Listeners** gjør det mulig å overvåke og feilsøke agentkjøringen. Det steg-for-steg-outputen du ser i demoen kommer fra en AgentListener som hooker inn i hver agentkall:
- **beforeAgentInvocation** - Kalles når Supervisor velger en agent, slik at du kan se hvilken agent som ble valgt og hvorfor  
- **afterAgentInvocation** - Kalles når en agent fullfører, og viser resultatet  
- **inheritedBySubagents** - Når sann, overvåker lytteren alle agenter i hierarkiet  

<img src="../../../translated_images/no/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent-lyttere hooker seg inn i utførelseslivssyklusen — overvåker når agenter starter, fullfører, eller møter feil.*

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
        return true; // Propager til alle under-agenter
    }
};
```
  
Utover Supervisor-mønsteret tilbyr `langchain4j-agentic` modulen flere kraftige arbeidsflytmønstre og funksjoner:

<img src="../../../translated_images/no/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Fem arbeidsflytmønstre for orkestrering av agenter — fra enkle sekvensielle rørledninger til godkjenningsflyter med menneskelig innblanding.*

| Mønster | Beskrivelse | Brukstilfelle |
|---------|-------------|---------------|
| **Sekvensiell** | Utfør agenter i rekkefølge, output flyter til neste | Rørledninger: forskning → analyse → rapport |
| **Parallell** | Kjør agenter samtidig | Uavhengige oppgaver: vær + nyheter + aksjer |
| **Løkke** | Iterer til betingelse er oppfylt | Kvalitetsscore: forbedre til score ≥ 0.8 |
| **Betinget** | Rute basert på betingelser | Klassifiser → rute til spesialistagent |
| **Human-in-the-Loop** | Legg til menneskelige sjekkpunkter | Godkjenningsflyter, innholdsvurdering |

## Nøkkelkonsepter

Nå som du har utforsket MCP og agentic-modulen i praksis, la oss oppsummere når du bør bruke hver tilnærming.

<img src="../../../translated_images/no/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP skaper et universelt protokollekosystem — enhver MCP-kompatibel server fungerer med enhver MCP-kompatibel klient, og gjør verktøydeling på tvers av applikasjoner mulig.*

**MCP** er ideelt når du ønsker å utnytte eksisterende verktøyøkosystemer, bygge verktøy som flere applikasjoner kan dele, integrere tredjepartstjenester med standardprotokoller, eller bytte ut verktøyimplementasjoner uten å endre kode.

**Agentic-modulen** fungerer best når du ønsker deklarative agentdefinisjoner med `@Agent` annotasjoner, trenger arbeidsflyt-orkestrering (sekvensiell, løkke, parallell), foretrekker grensesnittbasert agentdesign framfor imperativ kode, eller kombinerer flere agenter som deler output via `outputKey`.

**Supervisor Agent-mønsteret** skinner når arbeidsflyten ikke er forutsigbar på forhånd og du vil at LLM skal avgjøre, når du har flere spesialiserte agenter som trenger dynamisk orkestrering, ved bygging av samtalesystemer som ruter til ulike kapasiteter, eller når du ønsker mest mulig fleksibel og adaptiv agentatferd.

<img src="../../../translated_images/no/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Når man skal bruke egendefinerte @Tool-metoder kontra MCP-verktøy — egendefinerte verktøy for app-spesifikk logikk med full typesikkerhet, MCP-verktøy for standardiserte integrasjoner som fungerer på tvers av applikasjoner.*

## Gratulerer!

<img src="../../../translated_images/no/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Din læringsreise gjennom alle fem modulene — fra grunnleggende chat til MCP-drevne agentic systemer.*

Du har fullført LangChain4j for Nybegynnere-kurset. Du har lært:

- Hvordan bygge samtale-AI med minne (Modul 01)  
- Mønstre for prompt-engineering for forskjellige oppgaver (Modul 02)  
- Å forankre svar i dokumentene dine med RAG (Modul 03)  
- Lage grunnleggende AI-agenter (assistenter) med egendefinerte verktøy (Modul 04)  
- Integrere standardiserte verktøy med LangChain4j MCP og Agentic moduler (Modul 05)  

### Hva nå?

Etter å ha fullført modulene, utforsk [Testing Guide](../docs/TESTING.md) for å se LangChain4j testkonsepter i praksis.

**Offisielle ressurser:**  
- [LangChain4j Dokumentasjon](https://docs.langchain4j.dev/) - Omfattende guider og API-referanse  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Kildekode og eksempler  
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - Trinnvise veiledninger for ulike brukstilfeller  

Takk for at du fullførte dette kurset!

---

**Navigasjon:** [← Forrige: Modul 04 - Verktøy](../04-tools/README.md) | [Tilbake til Hovedside](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettingstjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vennligst vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det opprinnelige dokumentet på dets originale språk skal betraktes som den autoritative kilden. For kritisk informasjon anbefales profesjonell human oversettelse. Vi er ikke ansvarlige for misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->