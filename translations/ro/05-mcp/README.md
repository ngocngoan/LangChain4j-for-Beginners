# Modul 05: Protocolul Contextului Modelului (MCP)

## Cuprins

- [Ce Vei Învăța](../../../05-mcp)
- [Ce este MCP?](../../../05-mcp)
- [Cum Funcționează MCP](../../../05-mcp)
- [Modulul Agentic](../../../05-mcp)
- [Rularea Exemplului](../../../05-mcp)
  - [Prerechizite](../../../05-mcp)
- [Pornire Rapidă](../../../05-mcp)
  - [Operațiuni cu Fișiere (Stdio)](../../../05-mcp)
  - [Agent Supervisor](../../../05-mcp)
    - [Rularea Demonstrației](../../../05-mcp)
    - [Cum Funcționează Supervisorul](../../../05-mcp)
    - [Strategii de Răspuns](../../../05-mcp)
    - [Înțelegerea Rezultatului](../../../05-mcp)
    - [Explicația Funcționalităților Modulului Agentic](../../../05-mcp)
- [Concepte Cheie](../../../05-mcp)
- [Felicitări!](../../../05-mcp)
  - [Ce Urmează?](../../../05-mcp)

## Ce Vei Învăța

Ai construit AI conversațional, ai stăpânit prompturile, ai fundamentat răspunsurile în documente și ai creat agenți cu unelte. Dar toate acele unelte au fost construite personalizat pentru aplicația ta specifică. Dar dacă ai putea să-i oferi AI-ului tău acces la un ecosistem standardizat de unelte pe care oricine îl poate crea și împărtăși? În acest modul, vei învăța exact asta folosind Protocolul Contextului Modelului (MCP) și modulul agentic al LangChain4j. Mai întâi prezentăm un cititor simplu de fișiere MCP și apoi arătăm cum se integrează ușor în fluxuri de lucru agentice avansate folosind tiparul Agent Supervisor.

## Ce este MCP?

Protocolul Contextului Modelului (MCP) oferă exact acest lucru — o modalitate standardizată prin care aplicațiile AI pot descoperi și folosi unelte externe. În loc să scrii integrări personalizate pentru fiecare sursă de date sau serviciu, te conectezi la servere MCP care expun capabilitățile lor într-un format consistent. Agentul tău AI poate apoi să descopere și să folosească aceste unelte automat.

Diagrama de mai jos arată diferența — fără MCP, fiecare integrare necesită cablare punct-la-punct personalizată; cu MCP, un singur protocol conectează aplicația ta la orice unealtă:

<img src="../../../translated_images/ro/mcp-comparison.9129a881ecf10ff5.webp" alt="Comparatie MCP" width="800"/>

*Înainte de MCP: Integrări complexe punct-la-punct. După MCP: Un protocol, posibilități nesfârșite.*

MCP rezolvă o problemă fundamentală în dezvoltarea AI: fiecare integrare este personalizată. Vrei să accesezi GitHub? Cod personalizat. Vrei să citești fișiere? Cod personalizat. Vrei să interoghezi o bază de date? Cod personalizat. Și niciuna dintre aceste integrări nu funcționează cu alte aplicații AI.

MCP standardizează acest lucru. Un server MCP expune unelte cu descrieri clare și scheme. Orice client MCP poate să se conecteze, să descopere uneltele disponibile și să le folosească. Construiți o dată, folosiți oriunde.

Diagrama de mai jos ilustrează această arhitectură — un singur client MCP (aplicația ta AI) se conectează la mai mulți serveri MCP, fiecare expunând propriul set de unelte prin protocolul standard:

<img src="../../../translated_images/ro/mcp-architecture.b3156d787a4ceac9.webp" alt="Arhitectura MCP" width="800"/>

*Arhitectura Protocolului Contextului Modelului - descoperirea și execuția standardizată a uneltelor*

## Cum Funcționează MCP

La nivel intern, MCP folosește o arhitectură stratificată. Aplicația ta Java (client MCP) descoperă uneltele disponibile, trimite cereri JSON-RPC printr-un strat de transport (Stdio sau HTTP), iar serverul MCP execută operațiunile și returnează rezultatele. Diagrama următoare descompune fiecare strat al acestui protocol:

<img src="../../../translated_images/ro/mcp-protocol-detail.01204e056f45308b.webp" alt="Detaliu Protocol MCP" width="800"/>

*Cum funcționează MCP la nivel intern — clienții descoperă unelte, schimbă mesaje JSON-RPC și execută operațiuni printr-un strat de transport.*

**Arhitectura Client-Server**

MCP folosește un model client-server. Serverele pun la dispoziție unelte — citire fișiere, interogare baze de date, apelare API-uri. Clienții (aplicația ta AI) se conectează la servere și folosesc uneltele lor.

Pentru a folosi MCP cu LangChain4j, adaugă această dependență Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Descoperirea Uneltelor**

Când clientul tău se conectează la un server MCP, întreabă "Ce unelte ai?" Serverul răspunde cu o listă de unelte disponibile, fiecare având descrieri și scheme de parametri. Agentul tău AI poate decide apoi ce unelte să folosească în funcție de cererile utilizatorului. Diagrama de mai jos arată acest schimb — clientul trimite o cerere `tools/list` iar serverul returnează uneltele disponibile cu descrierile și schemele parametrilor:

<img src="../../../translated_images/ro/tool-discovery.07760a8a301a7832.webp" alt="Descoperirea Uneltelor MCP" width="800"/>

*AI-ul descoperă uneltele disponibile la pornire — acum știe ce capabilități sunt disponibile și poate decide pe care să le folosească.*

**Mecanisme de Transport**

MCP suportă diferite mecanisme de transport. Cele două opțiuni sunt Stdio (pentru comunicare locală între procese) și HTTP Streamabil (pentru servere la distanță). Acest modul demonstrează transportul Stdio:

<img src="../../../translated_images/ro/transport-mechanisms.2791ba7ee93cf020.webp" alt="Mecanisme de Transport" width="800"/>

*Mecanisme de transport MCP: HTTP pentru servere la distanță, Stdio pentru procese locale*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Pentru procese locale. Aplicația ta pornește un server ca subproces și comunică prin intrare/ieșire standard. Util pentru acces la sistemul de fișiere sau unelte linie de comandă.

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

Serverul `@modelcontextprotocol/server-filesystem` expune următoarele unelte, toate sandboxate în directoarele specificate de tine:

| Unealtă | Descriere |
|------|-------------|
| `read_file` | Citește conținutul unui singur fișier |
| `read_multiple_files` | Citește mai multe fișiere într-un singur apel |
| `write_file` | Creează sau suprascrie un fișier |
| `edit_file` | Efectuează editări țintite find-and-replace |
| `list_directory` | Listează fișiere și directoare într-o cale |
| `search_files` | Caută recursiv fișiere care corespund unui tipar |
| `get_file_info` | Obține metadate fișier (dimensiune, timestamp-uri, permisiuni) |
| `create_directory` | Creează un director (inclusiv directoare părinte) |
| `move_file` | Mută sau redenumește un fișier sau director |

Diagrama următoare arată cum funcționează transportul Stdio în timp de execuție — aplicația ta Java pornește serverul MCP ca proces copil și comunică prin pipe-uri stdin/stdout, fără rețea sau HTTP implicat:

<img src="../../../translated_images/ro/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Flux Transport Stdio" width="800"/>

*Transport Stdio în acțiune — aplicația ta pornește serverul MCP ca proces copil și comunică prin pipe-uri stdin/stdout.*

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschide [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) și întreabă:
> - "Cum funcționează transportul Stdio și când ar trebui să-l folosesc față de HTTP?"
> - "Cum gestionează LangChain4j ciclul de viață al proceselor server MCP lansate?"
> - "Care sunt implicațiile de securitate când oferi AI-ului acces la sistemul de fișiere?"

## Modulul Agentic

În timp ce MCP oferă unelte standardizate, **modulul agentic** al LangChain4j oferă o manieră declarativă de a construi agenți care orchestrează acele unelte. Anotarea `@Agent` și `AgenticServices` îți permit să definești comportamentul agentului prin interfețe, nu prin cod imperativ.

În acest modul, vei explora tiparul **Agent Supervisor** — o abordare AI agentică avansată unde un agent „supervisor” decide dinamic care sub-agenti să fie invocați în baza cererilor utilizatorului. Vom combina ambele concepte oferind unuia dintre sub-agenti capabilități de acces la fișiere alimentate de MCP.

Pentru a folosi modulul agentic, adaugă această dependență Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Notă:** Modulul `langchain4j-agentic` folosește o proprietate de versiune separată (`langchain4j.mcp.version`) deoarece este lansat pe un calendar diferit față de bibliotecile core LangChain4j.

> **⚠️ Experimental:** Modulul `langchain4j-agentic` este **experimental** și poate suferi modificări. Modalitatea stabilă de a construi asistenți AI rămâne `langchain4j-core` cu unelte personalizate (Modul 04).

## Rularea Exemplului

### Prerechizite

- Finalizat [Modul 04 - Unelte](../04-tools/README.md) (acest modul construiește pe conceptele uneltelor personalizate și le compară cu cele MCP)
- Fișier `.env` în directorul rădăcină cu credențiale Azure (creat prin `azd up` în Modul 01)
- Java 21+, Maven 3.9+
- Node.js 16+ și npm (pentru serverele MCP)

> **Notă:** Dacă încă nu ți-ai configurat variabilele de mediu, vezi [Modul 01 - Introducere](../01-introduction/README.md) pentru instrucțiuni de deploy (`azd up` creează automat fișierul `.env`), sau copiază `.env.example` în `.env` în directorul rădăcină și completează valorile.

## Pornire Rapidă

**Folosește VS Code:** Click dreapta pe orice fișier demo în Explorer și selectează **"Run Java"**, sau folosește configurările de lansare din panoul Run and Debug (asigură-te că fișierul `.env` este configurat cu credențiale Azure înainte).

**Folosește Maven:** Alternativ, poți rula din linia de comandă cu exemplele de mai jos.

### Operațiuni cu Fișiere (Stdio)

Acest exemplu demonstrează unelte locale bazate pe subprocese.

**✅ Nu sunt necesare prerechizite** — serverul MCP este pornit automat.

**Folosește scripturile de pornire (Recomandat):**

Scripturile de pornire încarcă automat variabilele de mediu din fișierul `.env` rădăcină:

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

**Folosește VS Code:** Click dreapta pe `StdioTransportDemo.java` și selectează **"Run Java"** (asigură-te că fișierul `.env` este configurat).

Aplicația pornește automat un server MCP pentru sistemul de fișiere și citește un fișier local. Observă cum gestionarea subproceselor este tratată pentru tine.

**Rezultat așteptat:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agent Supervisor

Tiparul **Agent Supervisor** este o formă **flexibilă** de AI agentică. Un Supervisor folosește un LLM pentru a decide autonom care agenți să fie invocați în funcție de cererea utilizatorului. În exemplul următor, combinăm accesul la fișiere alimentat de MCP cu un agent LLM pentru a crea un flux supravegheat citire fișier → raport.

În demo, `FileAgent` citește un fișier folosind uneltele MCP pentru sistemul de fișiere, iar `ReportAgent` generează un raport structurat cu un rezumat executiv (1 propoziție), 3 puncte cheie și recomandări. Supervisorul orchestrează acest flux automat:

<img src="../../../translated_images/ro/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Tipar Agent Supervisor" width="800"/>

*Supervisorul folosește LLM-ul său pentru a decide care agenți să fie invocați și în ce ordine — fără rutare hardcodata.*

Iată cum arată concret fluxul de lucru pentru canalul nostru de la fișier la raport:

<img src="../../../translated_images/ro/file-report-workflow.649bb7a896800de9.webp" alt="Flux de lucru Fișier La Raport" width="800"/>

*FileAgent citește fișierul prin uneltele MCP, apoi ReportAgent transformă conținutul brut într-un raport structurat.*

Fiecare agent stochează rezultatul în **Agentic Scope** (memorie partajată), permițând agenților următori să acceseze rezultatele anterioare. Acest lucru demonstrează cum uneltele MCP se integrează perfect în fluxurile agentice — Supervisorul nu trebuie să știe *cum* se citesc fișierele, doar că `FileAgent` poate face asta.

#### Rularea Demonstrației

Scripturile de pornire încarcă automat variabilele de mediu din fișierul `.env` rădăcină:

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

**Folosește VS Code:** Click dreapta pe `SupervisorAgentDemo.java` și selectează **"Run Java"** (asigură-te că `.env` este configurat).

#### Cum Funcționează Supervisorul

Înainte de a construi agenți, trebuie să conectezi transportul MCP la un client și să-l învelești ca un `ToolProvider`. Iată cum uneltele serverului MCP devin disponibile pentru agenții tăi:

```java
// Creează un client MCP din transport
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Împachetează clientul ca un ToolProvider — asta face legătura între instrumentele MCP și LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Acum poți injecta `mcpToolProvider` în orice agent care are nevoie de unelte MCP:

```java
// Pasul 1: FileAgent citește fișiere folosind instrumentele MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Are instrumente MCP pentru operațiuni cu fișiere
        .build();

// Pasul 2: ReportAgent generează rapoarte structurate
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supraveghetorul orchestrează fluxul de lucru fișier → raport
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Returnează raportul final
        .build();

// Supraveghetorul decide ce agenți să invoce în funcție de cerere
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Strategii de Răspuns

Când configurezi un `SupervisorAgent`, specifici cum ar trebui să formuleze răspunsul final către utilizator după ce sub-agenții și-au terminat sarcinile. Diagrama de mai jos arată cele trei strategii disponibile — LAST returnează direct ieșirea ultimului agent, SUMMARY sintetizează toate ieșirile printr-un LLM, iar SCORED alege ieșirea care are scor mai bun față de cererea inițială:

<img src="../../../translated_images/ro/response-strategies.3d0cea19d096bdf9.webp" alt="Strategii de Răspuns" width="800"/>

*Trei strategii pentru modul în care Supervisorul formulează răspunsul final — alege în funcție de dorința de a avea ieșirea ultimului agent, un rezumat sintetizat sau cea mai bine punctată opțiune.*

Strategiile disponibile sunt:

| Strategie | Descriere |
|----------|-------------|
| **LAST** | Supervisorul returnează ieșirea ultimului sub-agent sau unealtă apelată. Este util atunci când agentul final din flux este special conceput să producă răspunsul complet, final (exemplu: "Agent Rezumat" într-un pipeline de cercetare). |
| **SUMMARY** | Supervisorul folosește propriul model lingvistic (LLM) intern pentru a sintetiza un rezumat al întregii interacțiuni și a tuturor ieșirilor sub-agentului, apoi returnează acest rezumat ca răspuns final. Oferă un răspuns curat, agregat utilizatorului. |
| **SCORED** | Sistemul folosește un LLM intern pentru a puncta atât răspunsul LAST cât și rezumatul SUMMARY al interacțiunii față de cererea inițială a utilizatorului, returnând ieșirea care primește scorul mai mare. |
Vezi [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) pentru implementarea completă.

> **🤖 Încearcă cu Chat-ul [GitHub Copilot](https://github.com/features/copilot):** Deschide [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) și întreabă:
> - „Cum decide Supervisor ce agenți să invoce?”
> - „Care este diferența dintre modelele de flux de lucru Supervisor și Sequential?”
> - „Cum pot personaliza comportamentul de planificare al Supervisorului?”

#### Înțelegerea rezultatului

Când rulezi demo-ul, vei vedea un parcurs structurat despre cum Supervisor orchestrează mai mulți agenți. Iată ce înseamnă fiecare secțiune:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Antetul** introduce conceptul de flux de lucru: o conductă focalizată de la citirea fișierelor până la generarea raportului.

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

**Diagrama fluxului de lucru** arată fluxul de date între agenți. Fiecare agent are un rol specific:
- **FileAgent** citește fișierele folosind uneltele MCP și stochează conținutul brut în `fileContent`
- **ReportAgent** consumă acel conținut și produce un raport structurat în `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Cererea utilizatorului** arată sarcina. Supervisor o analizează și decide să invoce FileAgent → ReportAgent.

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

**Orchestrarea Supervisorului** arată fluxul în 2 pași în acțiune:
1. **FileAgent** citește fișierul prin MCP și stochează conținutul
2. **ReportAgent** primește conținutul și generează un raport structurat

Supervisorul a luat aceste decizii **autonom** bazat pe cererea utilizatorului.

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

#### Explicația caracteristicilor modulului Agentic

Exemplul demonstrează mai multe caracteristici avansate ale modulului agentic. Să aruncăm o privire mai atentă asupra Agentic Scope și Agent Listeners.

**Agentic Scope** arată memoria partajată unde agenții și-au stocat rezultatele folosind `@Agent(outputKey="...")`. Aceasta permite:
- Agenților următori să acceseze ieșirile agenților anteriori
- Supervisorului să sintetizeze un răspuns final
- Ție să inspectezi ce a produs fiecare agent

Diagrama de mai jos arată cum Agentic Scope funcționează ca memorie partajată în fluxul de la fișier la raport — FileAgent scrie ieșirea sa sub cheia `fileContent`, ReportAgent citește aceasta și scrie propria ieșire sub `report`:

<img src="../../../translated_images/ro/agentic-scope.95ef488b6c1d02ef.webp" alt="Memorie Partajată Agentic Scope" width="800"/>

*Agentic Scope acționează ca memorie partajată — FileAgent scrie `fileContent`, ReportAgent îl citește și scrie `report`, iar codul tău citește rezultatul final.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Date brute din fișier de la FileAgent
String report = scope.readState("report");            // Raport structurat de la ReportAgent
```

**Agent Listeners** permit monitorizarea și depanarea execuției agenților. Output-ul pas cu pas pe care îl vezi în demo vine de la un AgentListener care se conectează la fiecare invocare de agent:
- **beforeAgentInvocation** - Apelat când Supervisor selectează un agent, permițându-ți să vezi care agent a fost ales și de ce
- **afterAgentInvocation** - Apelat când un agent finalizează, arătând rezultatul său
- **inheritedBySubagents** - Când este adevărat, listenerul monitorizează toți agenții din ierarhie

Diagrama următoare arată ciclul complet de viață al Agent Listener, inclusiv cum `onError` gestionează eșecurile în execuția agentului:

<img src="../../../translated_images/ro/agent-listeners.784bfc403c80ea13.webp" alt="Ciclul de Viață Agent Listeners" width="800"/>

*Agent Listeners se conectează la ciclul de viață al execuției — monitorizează când agenții pornesc, finalizează sau întâmpină erori.*

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
        return true; // Propagă către toți sub-agenti
    }
};
```

Dincolo de modelul Supervisor, modulul `langchain4j-agentic` oferă mai multe modele puternice de flux de lucru. Diagrama de mai jos arată toate cele cinci — de la conducte simple secvențiale la fluxuri de aprobare cu implicare umană:

<img src="../../../translated_images/ro/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Modele Fluxuri de Lucru Agenți" width="800"/>

*Cinco modele de flux de lucru pentru orchestrarea agenților — de la conducte secvențiale simple la fluxuri de aprobare cu implicare umană.*

| Model | Descriere | Caz de folosință |
|---------|-------------|----------|
| **Sequential** | Execută agenții în ordine, ieșirea curge către următorul | Conducte: cercetare → analiză → raport |
| **Parallel** | Rulează agenții simultan | Sarcini independente: vreme + știri + bursă |
| **Loop** | Iterează până se îndeplinește o condiție | Scor de calitate: rafinează până când scor ≥ 0.8 |
| **Conditional** | Direcționează pe bază de condiții | Clasicare → direcționare către agent specialist |
| **Human-in-the-Loop** | Adaugă puncte de control umane | Fluxuri de aprobare, revizuire conținut |

## Concepute cheie

Acum că ai explorat MCP și modulul agentic în acțiune, să rezumăm când să folosești fiecare abordare.

Unul dintre cele mai mari avantaje ale MCP este ecosistemul său în creștere. Diagrama de mai jos arată cum un singur protocol universal conectează aplicația ta AI la o varietate largă de servere MCP — de la acces la sistemul de fișiere și baze de date până la GitHub, email, web scraping și altele:

<img src="../../../translated_images/ro/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="Ecosistem MCP" width="800"/>

*MCP creează un ecosistem de protocol universal — orice server compatibil MCP funcționează cu orice client compatibil MCP, permițând partajarea uneltelor între aplicații.*

**MCP** este ideal când dorești să valorifici ecosisteme existente de unelte, să construiești unelte pe care mai multe aplicații să le poată partaja, să integrezi servicii terțe cu protocoale standard sau să schimbi implementările uneltelor fără să modifici codul.

**Modulul Agentic** funcționează cel mai bine când dorești definiții declarative de agenți cu adnotări `@Agent`, ai nevoie de orchestrarea fluxurilor de lucru (secuențial, buclă, paralel), preferi un design bazat pe interfață în loc de cod imperativ, sau combini mai mulți agenți care partajează ieșiri prin `outputKey`.

**Modelul Supervisor Agent** strălucește când fluxul de lucru nu este predictibil în avans și dorești ca LLM să decidă, când ai mai mulți agenți specializați care necesită orchestrare dinamică, când construiești sisteme conversaționale care direcționează către diferite capabilități, sau când vrei cel mai flexibil și adaptiv comportament al agentului.

Pentru a te ajuta să decizi între metodele personalizate `@Tool` din Modulul 04 și uneltele MCP din acest modul, compararea următoare evidențiază compromisurile cheie — uneltele personalizate oferă cuplare strânsă și siguranță completă a tipurilor pentru logica specifică aplicației, în timp ce uneltele MCP oferă integrări standardizate și reutilizabile:

<img src="../../../translated_images/ro/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Unelte personalizate vs unelte MCP" width="800"/>

*Când să folosești metode personalizate @Tool versus unelte MCP — unelte personalizate pentru logica specifică aplicației cu siguranță completă de tip, unelte MCP pentru integrări standardizate care funcționează peste aplicații.*

## Felicitări!

Ai parcurs toate cele cinci module ale cursului LangChain4j pentru începători! Iată o privire asupra întregului parcurs de învățare pe care l-ai completat — de la chat de bază până la sisteme agentice bazate pe MCP:

<img src="../../../translated_images/ro/course-completion.48cd201f60ac7570.webp" alt="Finalizarea cursului" width="800"/>

*Parcursul tău de învățare prin toate cele cinci module — de la chat de bază până la sisteme agentice puternice bazate pe MCP.*

Ai finalizat cursul LangChain4j pentru începători. Ai învățat:

- Cum să construiești AI conversațional cu memorie (Modulul 01)
- Modele de inginerie a prompturilor pentru diferite sarcini (Modulul 02)
- Ancorarea răspunsurilor în documentele tale cu RAG (Modulul 03)
- Crearea de agenți AI de bază (asistenți) cu unelte personalizate (Modulul 04)
- Integrarea uneltelor standardizate cu modulele LangChain4j MCP și Agentic (Modulul 05)

### Ce urmează?

După ce ai terminat modulele, explorează [Ghidul de Testare](../docs/TESTING.md) pentru a vedea conceptele de testare LangChain4j în acțiune.

**Resurse oficiale:**
- [Documentația LangChain4j](https://docs.langchain4j.dev/) - Ghiduri cuprinzătoare și referință API
- [LangChain4j pe GitHub](https://github.com/langchain4j/langchain4j) - Cod sursă și exemple
- [Tutoriale LangChain4j](https://docs.langchain4j.dev/tutorials/) - Tutoriale pas cu pas pentru diverse cazuri de utilizare

Mulțumim că ai finalizat acest curs!

---

**Navigare:** [← Anterior: Modul 04 - Unelte](../04-tools/README.md) | [Înapoi la Principal](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:  
Acest document a fost tradus utilizând serviciul de traducere automată AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original, în limba sa nativă, trebuie considerat sursa autoritară. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm răspunderea pentru eventualele neînțelegeri sau interpretări greșite rezultate din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->