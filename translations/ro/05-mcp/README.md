# Module 05: Protocolul Contextului Modelului (MCP)

## Cuprins

- [Ce Vei Învața](../../../05-mcp)
- [Ce este MCP?](../../../05-mcp)
- [Cum Funcționează MCP](../../../05-mcp)
- [Modulul Agentic](../../../05-mcp)
- [Rularea Exemplelor](../../../05-mcp)
  - [Prerechizite](../../../05-mcp)
- [Început Rapid](../../../05-mcp)
  - [Operațiuni cu Fișiere (Stdio)](../../../05-mcp)
  - [Agent Supraveghetor](../../../05-mcp)
    - [Rularea Demo-ului](../../../05-mcp)
    - [Cum Funcționează Supraveghetorul](../../../05-mcp)
    - [Strategii de Răspuns](../../../05-mcp)
    - [Înțelegerea Rezultatelor](../../../05-mcp)
    - [Explicația Funcționalităților Modulului Agentic](../../../05-mcp)
- [Concepte Cheie](../../../05-mcp)
- [Felicitări!](../../../05-mcp)
  - [Ce Urmează?](../../../05-mcp)

## Ce Vei Învața

Ai construit un AI conversațional, ai stăpânit prompturile, ai ancorat răspunsuri în documente și ai creat agenți cu unelte. Dar toate acele unelte au fost construite personalizat pentru aplicația ta specifică. Ce-ar fi dacă ai putea oferi AI-ului tău acces la un ecosistem standardizat de unelte pe care oricine le poate crea și împărtăși? În acest modul, vei învăța cum să faci exact asta cu Protocolul Contextului Modelului (MCP) și modulul agentic LangChain4j. Mai întâi prezentăm un cititor simplu de fișiere MCP și apoi arătăm cum se integrează ușor în fluxuri agentice avansate utilizând modelul Agent Supraveghetor.

## Ce este MCP?

Protocolul Contextului Modelului (MCP) oferă exact asta - o metodă standard pentru aplicațiile AI de a descoperi și folosi unelte externe. În loc să scrii integrări personalizate pentru fiecare sursă de date sau serviciu, te conectezi la servere MCP care expun capabilitățile lor într-un format consistent. Agentul tău AI poate astfel descoperi și folosi aceste unelte automat.

<img src="../../../translated_images/ro/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Înainte de MCP: Integrări punct-la-punct complexe. După MCP: Un singur protocol, posibilități infinite.*

MCP rezolvă o problemă fundamentală în dezvoltarea AI: fiecare integrare este personalizată. Vrei acces la GitHub? Cod personalizat. Vrei să citești fișiere? Cod personalizat. Vrei să interoghezi o bază de date? Cod personalizat. Și niciuna dintre aceste integrări nu funcționează cu alte aplicații AI.

MCP standardizează asta. Un server MCP expune unelte cu descrieri clare și scheme de parametri. Orice client MCP se poate conecta, descoperi uneltele disponibile și le poate folosi. Creezi o dată, folosești oriunde.

<img src="../../../translated_images/ro/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Arhitectura Protocolului Contextului Modelului - descoperire și execuție standardizată a uneltelor*

## Cum Funcționează MCP

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

Când clientul tău se conectează la un server MCP, întreabă „Ce unelte aveți?” Serverul răspunde cu o listă de unelte disponibile, fiecare cu descrieri și scheme de parametri. Agentul tău AI poate decide apoi ce unelte să folosească pe baza solicitărilor utilizatorului.

<img src="../../../translated_images/ro/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI-ul descoperă uneltele disponibile la pornire — acum știe ce capabilități sunt disponibile și poate decide care să le folosească.*

**Mecanisme de Transport**

MCP suportă diferite mecanisme de transport. Acest modul demonstrează transportul Stdio pentru procese locale:

<img src="../../../translated_images/ro/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*Mecanismele de transport MCP: HTTP pentru servere la distanță, Stdio pentru procese locale*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Pentru procese locale. Aplicația ta pornește un server ca sub-proces și comunică prin intrarea/ieșirea standard. Util pentru accesul la sistemul de fișiere sau unelte de linie de comandă.

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

<img src="../../../translated_images/ro/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Transportul Stdio în acțiune — aplicația ta pornește serverul MCP ca proces copil și comunică prin pipe-urile stdin/stdout.*

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschide [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) și întreabă:
> - "Cum funcționează transportul Stdio și când ar trebui folosit în loc de HTTP?"
> - "Cum gestionează LangChain4j ciclul de viață al proceselor server MCP lansate?"
> - "Care sunt implicațiile de securitate ale acordării accesului AI-ului la sistemul de fișiere?"

## Modulul Agentic

În timp ce MCP oferă unelte standardizate, modulul **agentic** LangChain4j oferă o metodă declarativă de a construi agenți care orchestrează acele unelte. Anotarea `@Agent` și `AgenticServices` îți permit să definești comportamentul agentului prin interfețe, nu cod imperativ.

În acest modul, vei explora modelul **Agent Supraveghetor** — o abordare avansată agentică AI unde un agent „supraveghetor” decide dinamic ce sub-agenti să invoce pe baza cererilor utilizatorului. Vom combina ambele concepte oferind unuia dintre sub-agenti capacități MCP pentru accesul la fișiere.

Pentru a folosi modulul agentic, adaugă această dependență Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **⚠️ Experimental:** Modulul `langchain4j-agentic` este **experimental** și poate suferi modificări. Modalitatea stabilă de a construi asistenți AI rămâne `langchain4j-core` cu unelte personalizate (Modulul 04).

## Rularea Exemplelor

### Prerechizite

- Java 21+, Maven 3.9+
- Node.js 16+ și npm (pentru serverele MCP)
- Variabile de mediu configurate în fișierul `.env` (din directorul rădăcină):
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (la fel ca în Modulele 01-04)

> **Notă:** Dacă nu ți-ai configurat încă variabilele de mediu, vezi [Modulul 00 - Început Rapid](../00-quick-start/README.md) pentru instrucțiuni sau copiază `.env.example` în `.env` în directorul rădăcină și completează valorile tale.

## Început Rapid

**Folosind VS Code:** Pur și simplu fă clic dreapta pe orice fișier demo în Explorer și selectează **"Run Java"**, sau folosește configurațiile de lansare din panoul Run and Debug (asigură-te că ai adăugat mai întâi tokenul în fișierul `.env`).

**Folosind Maven:** Alternativ, poți rula din linia de comandă cu exemplele de mai jos.

### Operațiuni cu Fișiere (Stdio)

Acest exemplu demonstrează unelte locale bazate pe subprocese.

**✅ Nu sunt necesare prerechizite** - serverul MCP este pornit automat.

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

**Folosind VS Code:** Fă clic dreapta pe `StdioTransportDemo.java` și selectează **"Run Java"** (asigură-te că fișierul `.env` este configurat).

Aplicația pornește automat un server MCP pe sistemul de fișiere și citește un fișier local. Observă cum este gestionat pentru tine managementul subprocesului.

**Rezultat așteptat:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agent Supraveghetor

Modelul **Agent Supraveghetor** este o formă **flexibilă** de AI agentică. Un Supraveghetor folosește un LLM pentru a decide autonom ce agenți să invoce pe baza solicitării utilizatorului. În următorul exemplu, combinăm accesul la fișiere MCP cu un agent LLM pentru a crea un flux controlat de citire fișier → raport.

În demo, `FileAgent` citește un fișier folosind unelte MCP din sistemul de fișiere, iar `ReportAgent` generează un raport structurat cu un rezumat executiv (o propoziție), 3 puncte cheie și recomandări. Supraveghetorul orchestrează acest flux automat:

<img src="../../../translated_images/ro/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Supraveghetorul folosește LLM-ul său pentru a decide ce agenți să invoce și în ce ordine — fără rutare hardcodate.*

Iată cum arată concret fluxul pentru pipeline-ul nostru fișier-raport:

<img src="../../../translated_images/ro/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent citește fișierul prin unelte MCP, apoi ReportAgent transformă conținutul brut într-un raport structurat.*

Fiecare agent își stochează output-ul în **Agentic Scope** (memoria partajată), permițând agenților următori să acceseze rezultatele anterioare. Aceasta demonstrează cum uneltele MCP se integrează perfect în fluxurile agentice — Supraveghetorul nu trebuie să știe *cum* sunt citite fișierele, ci doar că `FileAgent` poate face asta.

#### Rularea Demo-ului

Scripturile de pornire încarcă automat variabilele de mediu din fișierul `.env` din rădăcină:

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

**Folosind VS Code:** Fă clic dreapta pe `SupervisorAgentDemo.java` și selectează **"Run Java"** (asigură-te că fișierul `.env` este configurat).

#### Cum Funcționează Supraveghetorul

```java
// Pasul 1: FileAgent citește fișiere utilizând instrumentele MCP
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

// Supervisorul decide ce agenți să invoce în funcție de solicitare
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Strategii de Răspuns

Când configurezi un `SupervisorAgent`, specifici cum ar trebui să formuleze răspunsul final către utilizator după ce sub-agenții și-au terminat sarcinile.

<img src="../../../translated_images/ro/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Trei strategii pentru cum formulează Supraveghetorul răspunsul final — alege în funcție de dacă vrei output-ul ultimului agent, un rezumat sintetizat sau opțiunea cu cel mai bun scor.*

Strategiile disponibile sunt:

| Strategie | Descriere |
|----------|-------------|
| **LAST** | Supraveghetorul returnează output-ul ultimului sub-agent sau unealtă apelată. Este util când agentul final din flux este proiectat special să producă răspunsul complet, final (de ex., un „Agent Rezumat” într-un pipeline de cercetare). |
| **SUMMARY** | Supraveghetorul folosește propriul model de limbaj (LLM) intern pentru a sintetiza un rezumat a întregii interacțiuni și a tuturor output-urilor sub-agenti, apoi returnează rezumatul ca răspuns final. Aceasta oferă un răspuns curat, agregat către utilizator. |
| **SCORED** | Sistemul folosește un LLM intern pentru a evalua atât răspunsul LAST, cât și SUMMARY al interacțiunii în raport cu cererea inițială a utilizatorului, returnând output-ul care primește cel mai mare scor. |

Vezi [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) pentru implementarea completă.

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschide [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) și întreabă:
> - "Cum decide Supraveghetorul ce agenți să invoce?"
> - "Care este diferența dintre modelele de flux de lucru Supraveghetor și Secvențial?"
> - "Cum pot personaliza comportamentul de planificare al Supraveghetorului?"

#### Înțelegerea Rezultatelor

Când rulezi demo-ul, vei vedea o prezentare structurată despre cum orchestrează Supraveghetorul mai mulți agenți. Iată ce reprezintă fiecare secțiune:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Antetul** introduce conceptul de flux de lucru: un pipeline focusat de la citirea fișierelor la generarea raportului.

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

**Diagrama Fluxului de Lucru** arată fluxul de date între agenți. Fiecare agent are un rol specific:
- **FileAgent** citește fișiere folosind unelte MCP și stochează conținutul brut în `fileContent`
- **ReportAgent** consumă acest conținut și produce un raport structurat în `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Solicitarea Utilizatorului** arată sarcina. Supraveghetorul o analizează și decide să invoce FileAgent → ReportAgent.

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

**Orchestrarea Supraveghetorului** arată fluxul în 2 pași în acțiune:
1. **FileAgent** citește fișierul prin MCP și stochează conținutul
2. **ReportAgent** primește conținutul și generează raportul structurat

Supraveghetorul a luat aceste decizii **autonom** bazat pe cererea utilizatorului.

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

#### Explicația Funcționalităților Modulului Agentic

Exemplul demonstrează mai multe funcționalități avansate ale modulului agentic. Să aruncăm o privire mai atentă asupra Agentic Scope și Agent Listeners.

**Agentic Scope** arată memoria partajată unde agenții și-au stocat rezultatele folosind `@Agent(outputKey="...")`. Aceasta permite:
- Agenților ulterioare să acceseze output-urile agenților anteriori
- Supraveghetorului să sintetizeze un răspuns final
- Ție să inspectezi ce a produs fiecare agent

<img src="../../../translated_images/ro/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope acționează ca memorie partajată — FileAgent scrie `fileContent`, ReportAgent îl citește și scrie `report`, iar codul tău citește rezultatul final.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Date brute din fișier de la FileAgent
String report = scope.readState("report");            // Raport structurat de la ReportAgent
```

**Agent Listeners** permit monitorizarea și depanarea execuției agenților. Output-ul pas-cu-pas pe care îl vezi în demo provine de la un AgentListener care se conectează la fiecare invocare a agentului:
- **beforeAgentInvocation** - Apelat când Supervisorul selectează un agent, permițându-vă să vedeți care agent a fost ales și de ce  
- **afterAgentInvocation** - Apelat când un agent termină, afișând rezultatul său  
- **inheritedBySubagents** - Când este adevărat, ascultătorul monitorizează toți agenții din ierarhie  

<img src="../../../translated_images/ro/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners se conectează în ciclul de viață al execuției — monitorizează când agenții pornesc, se termină sau întâmpină erori.*

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
  
Dincolo de modelul Supervisor, modulul `langchain4j-agentic` oferă mai multe modele puternice de fluxuri de lucru și funcționalități:

<img src="../../../translated_images/ro/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Cinsprezece modele de flux de lucru pentru orchestrarea agenților — de la pipeline-uri secvențiale simple la fluxuri de aprobare cu implicarea umană.*

| Model | Descriere | Caz de utilizare |
|---------|-------------|----------|
| **Sequential** | Execută agenții în ordine, ieșirea curge către următorul | Pipeline-uri: cercetare → analiză → raport |
| **Parallel** | Rulează agenții simultan | Sarcini independente: vreme + știri + acțiuni |
| **Loop** | Repetă până când se îndeplinește condiția | Scorare calitate: rafinează până scor ≥ 0.8 |
| **Conditional** | Direcționează pe baza condițiilor | Clasifică → trimite către agent specialist |
| **Human-in-the-Loop** | Adaugă puncte de control umane | Fluxuri de aprobare, revizuire conținut |

## Concepe cheie

Acum că ai explorat MCP și modulul agentic în acțiune, să rezumăm când să folosești fiecare abordare.

<img src="../../../translated_images/ro/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP creează un ecosistem universal de protocoale — orice server compatibil MCP funcționează cu orice client compatibil MCP, permițând partajarea de unelte între aplicații.*

**MCP** este ideal când vrei să valorifici ecosisteme de unelte existente, să construiești unelte pe care mai multe aplicații să le poată folosi, să integrezi servicii terțe cu protocoale standard, sau să schimbi implementările uneltelor fără a modifica codul.

**Modulul Agentic** funcționează cel mai bine când vrei definiții declarative de agenți cu adnotări `@Agent`, ai nevoie de orchestrare de fluxuri de lucru (secvențial, buclă, paralel), preferi design bazat pe interfețe în loc de cod imperativ, sau combini mai mulți agenți care împart ieșiri prin `outputKey`.

**Modelul Supervisor Agent** strălucește când fluxul de lucru nu este previzibil dinainte și vrei ca LLM să decidă, când ai mai mulți agenți specializați ce necesită orchestrare dinamică, când construiești sisteme conversaționale ce direcționează către capacități diferite, sau când dorești cel mai flexibil și adaptiv comportament al agentului.

<img src="../../../translated_images/ro/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Când să folosești metode personalizate @Tool versus unelte MCP — unelte personalizate pentru logica specifică aplicației cu siguranță completă a tipurilor, unelte MCP pentru integrări standardizate ce funcționează în aplicații diverse.*

## Felicitări!

<img src="../../../translated_images/ro/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Drumul tău de învățare prin cele cinci module — de la chat de bază la sisteme agentic alimentate de MCP.*

Ai finalizat cursul LangChain4j pentru Începători. Ai învățat:

- Cum să construiești AI conversațional cu memorie (Modulul 01)  
- Modele de inginerie a prompturilor pentru diferite sarcini (Modulul 02)  
- Ancorarea răspunsurilor în documente folosind RAG (Modulul 03)  
- Crearea de agenți AI de bază (asistenți) cu unelte personalizate (Modulul 04)  
- Integrarea uneltelor standardizate cu modulele LangChain4j MCP și Agentic (Modulul 05)  

### Ce urmează?

După finalizarea modulelor, explorează [Ghidul de Testare](../docs/TESTING.md) pentru a vedea conceptele de testare LangChain4j în acțiune.

**Resurse Oficiale:**  
- [Documentația LangChain4j](https://docs.langchain4j.dev/) - Ghiduri cuprinzătoare și referință API  
- [LangChain4j pe GitHub](https://github.com/langchain4j/langchain4j) - Cod sursă și exemple  
- [Tutoriale LangChain4j](https://docs.langchain4j.dev/tutorials/) - Tutoriale pas cu pas pentru diverse cazuri de utilizare  

Mulțumim că ai finalizat acest curs!

---

**Navigare:** [← Anterior: Modul 04 - Unelte](../04-tools/README.md) | [Înapoi la Principal](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:
Acest document a fost tradus utilizând serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși depunem eforturi pentru a asigura acuratețea, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa nativă trebuie considerat sursa oficială. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm responsabilitatea pentru eventualele neînțelegeri sau interpretări greșite rezultate din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->