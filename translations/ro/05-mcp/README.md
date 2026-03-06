# Modulul 05: Protocolul Contextului Modelului (MCP)

## Cuprins

- [Ce vei învăța](../../../05-mcp)
- [Ce este MCP?](../../../05-mcp)
- [Cum funcționează MCP](../../../05-mcp)
- [Modulul Agentic](../../../05-mcp)
- [Rularea Exemplelor](../../../05-mcp)
  - [Precondiții](../../../05-mcp)
- [Pornire Rapidă](../../../05-mcp)
  - [Operațiuni cu Fisiere (Stdio)](../../../05-mcp)
  - [Agentul Supraveghetor](../../../05-mcp)
    - [Rularea Demo-ului](../../../05-mcp)
    - [Cum funcționează Supraveghetorul](../../../05-mcp)
    - [Cum descoperă FileAgent uneltele MCP la rulare](../../../05-mcp)
    - [Strategii de Răspuns](../../../05-mcp)
    - [Înțelegerea Rezultatului](../../../05-mcp)
    - [Explicația Funcționalităților Modulului Agentic](../../../05-mcp)
- [Concepte Cheie](../../../05-mcp)
- [Felicitări!](../../../05-mcp)
  - [Ce urmează?](../../../05-mcp)

## Ce vei învăța

Ai construit o AI conversațională, ai stăpânit prompturile, ai ancorat răspunsurile în documente și ai creat agenți cu unelte. Dar toate acele unelte au fost construite personalizat pentru aplicația ta specifică. Ce-ai zice dacă ai putea oferi AI-ului tău acces la un ecosistem standardizat de unelte pe care oricine le poate crea și partaja? În acest modul, vei învăța cum să faci exact asta cu Protocolul Contextului Modelului (MCP) și modulul agentic LangChain4j. Mai întâi prezentăm un cititor simplu de fișiere MCP, apoi arătăm cum se integrează ușor în fluxuri agentice avansate folosind modelul Agent Supraveghetor.

## Ce este MCP?

Protocolul Contextului Modelului (MCP) oferă exact asta – o metodă standard pentru aplicațiile AI de a descoperi și folosi unelte externe. În loc să scrii integrări personalizate pentru fiecare sursă de date sau serviciu, te conectezi la servere MCP care expun capacitățile lor într-un format consecvent. Agentul tău AI poate apoi să descopere și să utilizeze aceste unelte automat.

Diagrama de mai jos arată diferența — fără MCP, fiecare integrare necesită legături punct-la-punct personalizate; cu MCP, un singur protocol conectează aplicația ta cu orice unealtă:

<img src="../../../translated_images/ro/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Înainte de MCP: Integrări complexe punct-la-punct. După MCP: Un protocol, posibilități infinite.*

MCP rezolvă o problemă fundamentală în dezvoltarea AI: fiecare integrare este personalizată. Vrei să accesezi GitHub? Cod personalizat. Vrei să citești fișiere? Cod personalizat. Vrei să interoghezi o bază de date? Cod personalizat. Și niciuna dintre aceste integrări nu funcționează cu alte aplicații AI.

MCP standardizează asta. Un server MCP expune unelte cu descrieri clare și scheme. Orice client MCP se poate conecta, descoperi uneltele disponibile și le poate folosi. Construiești o dată, folosești peste tot.

Diagrama de mai jos ilustrează această arhitectură — un singur client MCP (aplicația ta AI) se conectează la mai mulți serveri MCP, fiecare expunând propriul set de unelte prin protocolul standard:

<img src="../../../translated_images/ro/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Arhitectura Protocolului Contextului Model - descoperire și execuție standardizată a uneltelor*

## Cum funcționează MCP

Sub capotă, MCP folosește o arhitectură stratificată. Aplicația ta Java (clientul MCP) descoperă uneltele disponibile, trimite cereri JSON-RPC printr-un strat de transport (Stdio sau HTTP), iar serverul MCP execută operațiuni și returnează rezultate. Diagrama de mai jos detaliază fiecare strat al acestui protocol:

<img src="../../../translated_images/ro/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Cum funcționează MCP sub capotă — clienții descoperă uneltele, schimbă mesaje JSON-RPC și execută operațiuni printr-un strat de transport.*

**Arhitectură Server-Client**

MCP folosește un model client-server. Serverele oferă unelte - citirea fișierelor, interogarea bazelor de date, apelarea API-urilor. Clienții (aplicația ta AI) se conectează la servere și folosesc uneltele lor.

Pentru a folosi MCP cu LangChain4j, adaugă această dependență Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Descoperirea Uneltelor**

Când clientul tău se conectează la un server MCP, întreabă "Ce unelte ai?" Serverul răspunde cu o listă de unelte disponibile, fiecare cu descrieri și scheme de parametri. Agentul tău AI poate apoi să decidă ce unelte să folosească bazat pe cererile utilizatorului. Diagrama de mai jos arată această strângere de mână — clientul trimite o cerere `tools/list` iar serverul returnează uneltele disponibile cu descrieri și scheme de parametri:

<img src="../../../translated_images/ro/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI-ul descoperă uneltele disponibile la pornire — acum știe ce capacități sunt disponibile și poate decide care să folosească.*

**Mecanisme de Transport**

MCP suportă diferite mecanisme de transport. Cele două opțiuni sunt Stdio (pentru comunicare locală cu subprocese) și HTTP Streamabil (pentru servere la distanță). Acest modul demonstrează transportul Stdio:

<img src="../../../translated_images/ro/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*Mecanismele de transport MCP: HTTP pentru servere remote, Stdio pentru procese locale*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Pentru procese locale. Aplicația ta pornește un server ca subprocess și comunică prin intrare/ieșire standard. Util pentru acces la sistemul de fișiere sau unelte de linie de comandă.

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

Serverul `@modelcontextprotocol/server-filesystem` expune următoarele unelte, toate sandboxate în directoarele specificate:

| Unealtă | Descriere |
|---------|-----------|
| `read_file` | Citește conținutul unui singur fișier |
| `read_multiple_files` | Citește mai multe fișiere într-un singur apel |
| `write_file` | Creează sau suprascrie un fișier |
| `edit_file` | Face editări țintite de tip găsește-și înlocuiește |
| `list_directory` | Listează fișiere și directoare la o cale dată |
| `search_files` | Caută recursiv fișiere care se potrivesc după un șablon |
| `get_file_info` | Obține metadatele fișierului (dimensiune, timpi, permisiuni) |
| `create_directory` | Creează un director (incluzând directoare părinte) |
| `move_file` | Mută sau redenumește un fișier sau director |

Diagrama de mai jos arată cum funcționează transportul Stdio în timp rulării — aplicația ta Java pornește serverul MCP ca proces copil și comunică prin pipe-uri stdin/stdout, fără rețea sau HTTP implicat:

<img src="../../../translated_images/ro/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Transportul Stdio în acțiune — aplicația ta pornește serverul MCP ca proces copil și comunică prin pipe-uri stdin/stdout.*

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschide [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) și întreabă:
> - "Cum funcționează transportul Stdio și când ar trebui să-l folosesc față de HTTP?"
> - "Cum gestionează LangChain4j ciclul de viață al proceselor server MCP pornite?"
> - "Care sunt implicațiile de securitate ale oferirii AI-ului acces la sistemul de fișiere?"

## Modulul Agentic

Deși MCP oferă unelte standardizate, modulul agentic LangChain4j oferă o modalitate declarativă de a crea agenți care orchestrează aceste unelte. Anotarea `@Agent` și `AgenticServices` îți permit să definești comportamentul agentului prin interfețe, nu prin cod imperativ.

În acest modul, vei explora patternul **Agent Supraveghetor** — o abordare agentică avansată în care un agent „supraveghetor” decide dinamic ce sub-agenți să invoce în baza cererilor utilizatorului. Combinăm ambele concepte oferind unuia dintre sub-agenți capacități de acces la fișiere alimentate de MCP.

Pentru a folosi modulul agentic, adaugă această dependență Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Notă:** Modulul `langchain4j-agentic` folosește o proprietate de versiune separată (`langchain4j.mcp.version`) pentru că este lansat după un program diferit față de bibliotecile de bază LangChain4j.

> **⚠️ Experimental:** Modulul `langchain4j-agentic` este **experimental** și poate suferi modificări. Modulul stabil pentru a crea asistenți AI rămâne `langchain4j-core` cu unelte personalizate (Modul 04).

## Rularea Exemplelor

### Precondiții

- Parcurgerea [Modulului 04 - Unelte](../04-tools/README.md) (acest modul se bazează pe conceptele de unelte personalizate și le compară cu uneltele MCP)
- Fișier `.env` în directorul rădăcină cu credențiale Azure (creat de `azd up` în Modulul 01)
- Java 21+, Maven 3.9+
- Node.js 16+ și npm (pentru serverele MCP)

> **Notă:** Dacă nu ți-ai setat încă variabilele de mediu, vezi [Modulul 01 - Introducere](../01-introduction/README.md) pentru instrucțiuni de deploy (`azd up` creează automat fișierul `.env`), sau copiază `.env.example` în `.env` în directorul rădăcină și completează valorile.

## Pornire Rapidă

**Folosește VS Code:** Dă click dreapta pe orice fișier demo în Explorer și alege **"Run Java"**, sau folosește configurațiile de rulare din panoul Run and Debug (asigură-te că fișierul `.env` este configurat cu credențiale Azure).

**Folosește Maven:** Alternativ, poți rula din linia de comandă cu exemplele de mai jos.

### Operațiuni cu Fișiere (Stdio)

Acest demo arată unelte locale bazate pe subprocese.

**✅ Nu sunt necesare precondiții** — serverul MCP este pornit automat.

**Folosind scripturile de pornire (Recomandat):**

Scripturile de pornire încarcă automat variabilele de mediu din fișierul `.env` din rădăcină:

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

Aplicația pornește automat un server MCP de sistem de fișiere și citește un fișier local. Observă cum gestionarea procesului copil este realizată pentru tine.

**Output așteptat:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agentul Supraveghetor

Patternul **Agent Supraveghetor** este o formă **flexibilă** de AI agentic. Un Supraveghetor folosește un LLM pentru a decide autonom ce agenți să invoce bazat pe cererea utilizatorului. În exemplul următor, combinăm accesul la fișiere alimentat de MCP cu un agent LLM pentru a crea un flux de lucru supravegheat de citire fișier → raport.

În demo, `FileAgent` citește un fișier folosind uneltele MCP de sistem de fișiere, iar `ReportAgent` generează un raport structurat cu un rezumat executiv (1 propoziție), 3 puncte cheie și recomandări. Supraveghetorul orchestrează acest flux automat:

<img src="../../../translated_images/ro/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Supraveghetorul folosește LLM-ul să decidă ce agenți să invoce și în ce ordine — fără rutare codificată.*

Iată cum arată fluxul concret pentru pipeline-ul nostru citire fișier → raport:

<img src="../../../translated_images/ro/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent citește fișierul prin uneltele MCP, apoi ReportAgent transformă conținutul brut într-un raport structurat.*

Următorul diagramă de secvență trasează întreaga orchestrare a Supraveghetorului — de la pornirea serverului MCP, până la selecția autonomă a agenților de către Supraveghetor, la apelurile uneltelor prin stdio și raportul final:

<img src="../../../translated_images/ro/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*Supraveghetorul invocă autonom FileAgent (care apelează serverul MCP prin stdio pentru a citi fișierul), apoi invocă ReportAgent pentru a genera un raport structurat — fiecare agent stochează output-ul în spațiul comun Agentic Scope.*

Fiecare agent stochează rezultatul său în **Agentic Scope** (memorie partajată), permițând agenților din aval să acceseze rezultate anterioare. Aceasta demonstrează cum uneltele MCP se integrează perfect în fluxurile agentice — Supraveghetorul nu trebuie să știe *cum* se citesc fișierele, ci doar că `FileAgent` poate face asta.

#### Rularea Demo-ului

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

#### Cum funcționează Supraveghetorul

Înainte să construiești agenții, trebuie să conectezi transportul MCP la un client și să-l înfășori ca `ToolProvider`. Astfel uneltele serverului MCP devin disponibile agenților tăi:

```java
// Creează un client MCP din transport
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Împachetează clientul ca un ToolProvider — acesta face legătura între uneltele MCP și LangChain4j
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

// Supervisorul orchestrează fluxul de lucru fișier → raport
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Returnează raportul final
        .build();

// Supervisorul decide ce agenți să invoce în funcție de cerere
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Cum descoperă FileAgent uneltele MCP la rulare

Te întrebi poate: **cum știe `FileAgent` să folosească uneltele npm de sistem de fișiere?** Răspunsul este că nu știe — **LLM-ul** îl identifică dinamic la rulare prin schemele uneltelor.

Interfața `FileAgent` este doar o **definiție de prompt**. Nu are cunoștințe codificate despre `read_file`, `list_directory` sau alte unelte MCP. Iată ce se întâmplă end-to-end:
1. **Pornirea serverului:** `StdioMcpTransport` lansează pachetul npm `@modelcontextprotocol/server-filesystem` ca un proces copil  
2. **Descoperirea uneltelor:** `McpClient` trimite o cerere JSON-RPC `tools/list` către server, care răspunde cu numele uneltelor, descrieri și schemele parametrilor (de exemplu, `read_file` — *"Citește conținutul complet al unui fișier"* — `{ path: string }`)  
3. **Injecția schemelor:** `McpToolProvider` înfășoară aceste scheme descoperite și le face disponibile pentru LangChain4j  
4. **Decizia LLM:** Când se apelează `FileAgent.readFile(path)`, LangChain4j trimite mesajul sistem, mesajul utilizator și **lista schemelor uneltelor** către LLM. LLM citește descrierile uneltelor și generează un apel al uneltei (de exemplu, `read_file(path="/some/file.txt")`)  
5. **Executarea:** LangChain4j interceptează apelul uneltei, îl redirecționează prin clientul MCP înapoi la subprocessul Node.js, primește rezultatul și îl transmite înapoi către LLM  

Acesta este același mecanism [Tool Discovery](../../../05-mcp) descris mai sus, dar aplicat specific fluxului agentului. Anotațiile `@SystemMessage` și `@UserMessage` ghidează comportamentul LLM, în timp ce `ToolProvider` injectat îi oferă **capabilitățile** — LLM face legătura între cele două în timpul execuției.

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschide [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) și întreabă:  
> - "Cum știe acest agent ce unealtă MCP să apeleze?"  
> - "Ce s-ar întâmpla dacă aș elimina ToolProvider din constructorul agentului?"  
> - "Cum sunt transmise schemele uneltelor către LLM?"  

#### Strategii de răspuns

Când configurezi un `SupervisorAgent`, specifici cum ar trebui să formuleze răspunsul final către utilizator după ce sub-agentele și-au îndeplinit sarcinile. Diagrama următoare arată cele trei strategii disponibile — LAST returnează direct rezultatul ultimului agent, SUMMARY sintetizează toate rezultatele printr-un LLM, iar SCORED alege răspunsul cu cel mai mare scor față de cererea inițială:

<img src="../../../translated_images/ro/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Cele trei strategii prin care Supervisor formulează răspunsul final — alege în funcție de dacă dorești rezultatul ultimului agent, un sumar sintetizat sau opțiunea cu cel mai bun scor.*

Strategiile disponibile sunt:

| Strategie | Descriere |
|----------|-------------|
| **LAST** | Supervisorul returnează rezultatul ultimului sub-agent sau unealtă apelată. Este util când agentul final din flux este special conceput să producă răspunsul complet, final (de exemplu, un "Agent de sumarizare" într-un flux de cercetare). |
| **SUMMARY** | Supervisorul folosește propriul său model lingvistic intern (LLM) pentru a sintetiza un sumar al întregii interacțiuni și al tuturor rezultatelor sub-agente, apoi returnează acel sumar ca răspuns final. Oferă un răspuns agregat și curat pentru utilizator. |
| **SCORED** | Sistemul folosește un LLM intern pentru a evalua atât răspunsul LAST cât și SUMMARY în raport cu cererea inițială a utilizatorului, returnând răspunsul care primește scorul mai mare. |

Vezi [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) pentru implementarea completă.

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschide [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) și întreabă:  
> - "Cum decide Supervisor ce agenți să invoce?"  
> - "Care este diferența dintre pattern-urile Supervisor și Sequential?"  
> - "Cum pot personaliza comportamentul de planificare al Supervisorului?"  

#### Înțelegerea rezultatului

Când rulezi demo-ul, vei vedea o prezentare structurată a modului în care Supervisor orchestrează mai mulți agenți. Iată ce înseamnă fiecare secțiune:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Antetul** introduce conceptul de workflow: un pipeline focalizat de la citirea fișierului la generarea raportului.

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
  
**Diagrama workflow-ului** arată fluxul de date între agenți. Fiecare agent are un rol specific:  
- **FileAgent** citește fișiere folosind unelte MCP și stochează conținutul brut în `fileContent`  
- **ReportAgent** consumă acel conținut și produce un raport structurat în `report`  

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Cererea utilizatorului** arată sarcina. Supervisorul o analizează și decide să invoce FileAgent → ReportAgent.

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
  
**Orchestrarea Supervisorului** arată în practică fluxul în 2 pași:  
1. **FileAgent** citește fișierul prin MCP și stochează conținutul  
2. **ReportAgent** primește conținutul și generează raportul structurat  

Supervisorul a luat aceste decizii **autonom** bazându-se pe cererea utilizatorului.

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
  
#### Explicație privind funcționalitățile modulului agentic

Exemplul demonstrează mai multe funcții avansate ale modulului agentic. Haideți să analizăm mai îndeaproape Agentic Scope și Agent Listeners.

**Agentic Scope** arată memoria partajată unde agenții și-au stocat rezultatele folosind `@Agent(outputKey="...")`. Aceasta permite:  
- agenților următori să acceseze rezultatele agenților anteriori  
- Supervisorului să sintetizeze un răspuns final  
- ție să inspectezi ce a produs fiecare agent  

Diagrama de mai jos arată cum funcționează Agentic Scope ca memorie partajată în fluxul file-to-report — FileAgent scrie ieșirea sub cheia `fileContent`, ReportAgent citește aceasta și scrie propria ieșire sub `report`:

<img src="../../../translated_images/ro/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope acționează ca memorie partajată — FileAgent scrie `fileContent`, ReportAgent îl citește și scrie `report`, iar codul tău citește rezultatul final.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Date brute din fișier de la FileAgent
String report = scope.readState("report");            // Raport structurat de la ReportAgent
```
  
**Agent Listeners** permit monitorizarea și depanarea execuției agenților. Rezultatul pas cu pas pe care îl vezi în demo provine de la un AgentListener care se atașează la fiecare invocare de agent:  
- **beforeAgentInvocation** – Apelat când Supervisor selectează un agent, permițându-ți să vezi ce agent a fost ales și de ce  
- **afterAgentInvocation** – Apelat când un agent termină, arătând rezultatul său  
- **inheritedBySubagents** – Dacă este true, listenerul monitorizează toți agenții din ierarhie  

Diagrama următoare arată întreg ciclul de viață al Agent Listener, inclusiv modul în care `onError` gestionează erorile în timpul execuției agentului:

<img src="../../../translated_images/ro/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners se atașează ciclului de viață al execuției — monitorizează când agenții pornesc, termină sau întâmpină erori.*

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
        return true; // Propagă către toți sub-agentele
    }
};
```
  
Dincolo de pattern-ul Supervisor, modulul `langchain4j-agentic` oferă mai multe pattern-uri puternice pentru workflow. Diagrama următoare arată toate cele cinci — de la pipeline-uri simple secvențiale până la workflow-uri de aprobare cu implicare umană:

<img src="../../../translated_images/ro/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Cinque pattern-uri de workflow pentru orchestrarea agenților — de la pipeline-uri simple secvențiale până la workflow-uri cu implicare umană.*

| Pattern | Descriere | Caz de utilizare |
|---------|-------------|----------|
| **Sequential** | Execută agenții în ordine, ieșirea curge către următorul | Pipeline-uri: cercetare → analiză → raport |
| **Parallel** | Rulează agenți simultan | Sarcini independente: vreme + știri + bursă |
| **Loop** | Repetă până la condiția îndeplinită | Evaluare calitate: rafinează până scor ≥ 0.8 |
| **Conditional** | Direcționează în funcție de condiții | Clasifică → direcționează către agent specialist |
| **Human-in-the-Loop** | Adaugă puncte de control umane | Workflow-uri de aprobare, revizuire conținut |

## Concepte cheie

Acum că ai explorat MCP și modulul agentic în acțiune, să rezumăm când să folosești fiecare abordare.

Unul dintre cele mai mari avantaje MCP este ecosistemul său în creștere. Diagrama de mai jos arată cum un protocol universal single conectează aplicația ta AI la o varietate largă de servere MCP — de la acces la filesystem și baze de date până la GitHub, email, web scraping și altele:

<img src="../../../translated_images/ro/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP creează un ecosistem de protocol universal — orice server compatibil MCP funcționează cu orice client compatibil MCP, permițând partajarea uneltelor între aplicații.*

**MCP** este ideal când dorești să valorifici ecosisteme de unelte existente, să construiești unelte pe care mai multe aplicații să le poată partaja, să integrezi servicii terțe cu protocoale standard sau să înlocuiești implementări de unelte fără să schimbi codul.

**Modulul Agentic** funcționează cel mai bine când dorești definiții declarative ale agenților cu anotații `@Agent`, ai nevoie de orchestrarea workflow-urilor (secvențial, loop, paralel), preferi designul agenților bazat pe interfață în loc de cod imperativ sau combini mai mulți agenți care partajează ieșiri prin `outputKey`.

**Pattern-ul Supervisor Agent** strălucește când fluxul de lucru nu este predictibil în avans și dorești ca LLM să decidă, când ai mulți agenți specializați ce necesită orchestrare dinamică, când construiești sisteme conversaționale care direcționează către capabilități diferite sau când vrei cel mai flexibil și adaptiv comportament al agentului.

Pentru a te ajuta să decizi între metodele personalizate `@Tool` din Modulul 04 și uneltele MCP din acest modul, comparația următoare evidențiază compromisurile cheie — uneltele personalizate oferă cuplare strânsă și siguranță totală a tipurilor pentru logica specifică aplicației, în timp ce uneltele MCP oferă integrări standardizate, reutilizabile:

<img src="../../../translated_images/ro/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Când să folosești metode personalizate @Tool vs uneltele MCP — uneltele personalizate pentru logica aplicației cu siguranță totală a tipurilor, uneltele MCP pentru integrări standardizate care funcționează peste aplicații.*

## Felicitări!

Ai parcurs toate cele cinci module ale cursului LangChain4j for Beginners! Iată o privire de ansamblu asupra întregii călătorii de învățare — de la chat-ul de bază până la sisteme agentice alimentate de MCP:

<img src="../../../translated_images/ro/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Drumul tău de învățare prin toate cele cinci module — de la chat de bază la sisteme agentice cu MCP.*

Ai finalizat cursul LangChain4j for Beginners. Ai învățat:

- Cum să construiești AI conversațional cu memorie (Modul 01)  
- Modele de inginerie a prompturilor pentru diverse sarcini (Modul 02)  
- Cum să fundamentezi răspunsurile pe documentele tale cu RAG (Modul 03)  
- Crearea agenților AI de bază (asistenți) cu unelte personalizate (Modul 04)  
- Integrarea uneltelor standardizate cu LangChain4j MCP și modulele Agentic (Modul 05)  

### Ce urmează?

După ce ai terminat modulele, explorează [Testing Guide](../docs/TESTING.md) pentru a vedea conceptele de testare LangChain4j în acțiune.

**Resurse oficiale:**  
- [Documentația LangChain4j](https://docs.langchain4j.dev/) – Ghiduri complete și referință API  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) – Cod sursă și exemple  
- [Tutoriale LangChain4j](https://docs.langchain4j.dev/tutorials/) – Tutoriale pas cu pas pentru diverse cazuri de utilizare  

Mulțumim că ai parcurs acest curs!

---

**Navigare:** [← Anterior: Modul 04 - Tool-uri](../04-tools/README.md) | [Înapoi la Principal](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:  
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să țineți cont că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa nativă trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm responsabilitatea pentru eventuale neînțelegeri sau interpretări greșite care ar putea apărea din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->