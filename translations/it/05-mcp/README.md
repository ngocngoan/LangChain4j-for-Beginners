# Modulo 05: Model Context Protocol (MCP)

## Indice

- [Cosa Imparerai](../../../05-mcp)
- [Cos'è MCP?](../../../05-mcp)
- [Come Funziona MCP](../../../05-mcp)
- [Il Modulo Agentico](../../../05-mcp)
- [Esecuzione degli Esempi](../../../05-mcp)
  - [Prerequisiti](../../../05-mcp)
- [Avvio Rapido](../../../05-mcp)
  - [Operazioni sui File (Stdio)](../../../05-mcp)
  - [Agente Supervisore](../../../05-mcp)
    - [Esecuzione della Demo](../../../05-mcp)
    - [Come Funziona il Supervisore](../../../05-mcp)
    - [Come FileAgent Scopre gli Strumenti MCP a Runtime](../../../05-mcp)
    - [Strategie di Risposta](../../../05-mcp)
    - [Comprendere l'Output](../../../05-mcp)
    - [Spiegazione delle Funzionalità del Modulo Agentico](../../../05-mcp)
- [Concetti Chiave](../../../05-mcp)
- [Congratulazioni!](../../../05-mcp)
  - [Cosa Fare Dopo?](../../../05-mcp)

## Cosa Imparerai

Hai costruito AI conversazionale, padroneggiato i prompt, ancorato risposte nei documenti e creato agenti con strumenti. Ma tutti quegli strumenti erano creati su misura per la tua specifica applicazione. E se potessi dare alla tua AI accesso a un ecosistema standardizzato di strumenti che chiunque può creare e condividere? In questo modulo, imparerai proprio questo con il Model Context Protocol (MCP) e il modulo agentico di LangChain4j. Mostriamo prima un semplice lettore di file MCP e poi come si integra facilmente in workflow agentici avanzati utilizzando il pattern Supervisor Agent.

## Cos'è MCP?

Il Model Context Protocol (MCP) fornisce esattamente questo: un modo standard per le applicazioni AI di scoprire e usare strumenti esterni. Invece di scrivere integrazioni personalizzate per ogni sorgente dati o servizio, ti connetti a server MCP che espongono le loro funzionalità in un formato coerente. Il tuo agente AI può quindi scoprire e usare automaticamente questi strumenti.

Il diagramma sottostante mostra la differenza — senza MCP, ogni integrazione richiede collegamenti punto a punto personalizzati; con MCP, un unico protocollo collega la tua app a qualsiasi strumento:

<img src="../../../translated_images/it/mcp-comparison.9129a881ecf10ff5.webp" alt="Confronto MCP" width="800"/>

*Prima di MCP: integrazioni punto a punto complesse. Dopo MCP: un protocollo, infinite possibilità.*

MCP risolve un problema fondamentale nello sviluppo AI: ogni integrazione è personalizzata. Vuoi accedere a GitHub? Codice personalizzato. Vuoi leggere file? Codice personalizzato. Vuoi interrogare un database? Codice personalizzato. E nessuna di queste integrazioni funziona con altre applicazioni AI.

MCP standardizza tutto ciò. Un server MCP espone strumenti con descrizioni chiare e schemi. Qualsiasi client MCP può connettersi, scoprire gli strumenti disponibili e usarli. Si costruisce una volta, si usa ovunque.

Il diagramma sottostante illustra questa architettura — un singolo client MCP (la tua applicazione AI) si connette a più server MCP, ognuno che espone il proprio set di strumenti tramite il protocollo standard:

<img src="../../../translated_images/it/mcp-architecture.b3156d787a4ceac9.webp" alt="Architettura MCP" width="800"/>

*Architettura del Model Context Protocol - scoperta e esecuzione strumenti standardizzata*

## Come Funziona MCP

Sotto il cofano, MCP utilizza un'architettura a strati. La tua applicazione Java (il client MCP) scopre gli strumenti disponibili, invia richieste JSON-RPC attraverso un livello di trasporto (Stdio o HTTP), e il server MCP esegue le operazioni e restituisce i risultati. Il diagramma seguente scompone ogni livello di questo protocollo:

<img src="../../../translated_images/it/mcp-protocol-detail.01204e056f45308b.webp" alt="Dettaglio Protocollo MCP" width="800"/>

*Come funziona MCP sotto il cofano — i client scoprono gli strumenti, scambiano messaggi JSON-RPC, ed eseguono operazioni tramite un livello di trasporto.*

**Architettura Server-Client**

MCP utilizza un modello client-server. I server forniscono strumenti - lettura file, interrogazione database, chiamate API. I client (la tua applicazione AI) si connettono ai server e usano i loro strumenti.

Per usare MCP con LangChain4j, aggiungi questa dipendenza Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Scoperta degli Strumenti**

Quando il tuo client si connette a un server MCP, chiede "Quali strumenti hai?" Il server risponde con una lista di strumenti disponibili, ciascuno con descrizioni e schemi dei parametri. Il tuo agente AI può quindi decidere quali strumenti usare in base alle richieste dell'utente. Il diagramma sottostante mostra questo handshake — il client invia una richiesta `tools/list` e il server restituisce i suoi strumenti disponibili con descrizioni e schemi:

<img src="../../../translated_images/it/tool-discovery.07760a8a301a7832.webp" alt="Scoperta Strumenti MCP" width="800"/>

*L'AI scopre gli strumenti disponibili all'avvio — ora conosce quali capacità sono disponibili e può decidere quali utilizzare.*

**Meccanismi di Trasporto**

MCP supporta diversi meccanismi di trasporto. Le due opzioni sono Stdio (per comunicazione con processi locali) e Streamable HTTP (per server remoti). Questo modulo dimostra il trasporto Stdio:

<img src="../../../translated_images/it/transport-mechanisms.2791ba7ee93cf020.webp" alt="Meccanismi di Trasporto" width="800"/>

*Meccanismi di trasporto MCP: HTTP per server remoti, Stdio per processi locali*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Per processi locali. La tua applicazione avvia un server come processo figlio e comunica tramite input/output standard. Utile per accesso al filesystem o strumenti da riga di comando.

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

Il server `@modelcontextprotocol/server-filesystem` espone i seguenti strumenti, tutti sandboxati nelle directory che specifichi:

| Strumento | Descrizione |
|------|-------------|
| `read_file` | Legge il contenuto di un singolo file |
| `read_multiple_files` | Legge più file in una sola chiamata |
| `write_file` | Crea o sovrascrive un file |
| `edit_file` | Effettua modifiche mirate find-and-replace |
| `list_directory` | Elenca file e directory in un percorso |
| `search_files` | Cerca ricorsivamente file che corrispondono a un pattern |
| `get_file_info` | Ottiene metadati del file (dimensioni, timestamp, permessi) |
| `create_directory` | Crea una directory (incluso genitori) |
| `move_file` | Sposta o rinomina un file o directory |

Il diagramma seguente mostra come funziona il trasporto Stdio a runtime — la tua applicazione Java avvia il server MCP come processo figlio e comunicano tramite pipe stdin/stdout, senza rete o HTTP coinvolti:

<img src="../../../translated_images/it/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Flusso Trasporto Stdio" width="800"/>

*Trasporto Stdio in azione — la tua applicazione avvia il server MCP come processo figlio e comunica tramite pipe stdin/stdout.*

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) e chiedi:
> - "Come funziona il trasporto Stdio e quando dovrei usarlo rispetto a HTTP?"
> - "Come gestisce LangChain4j il ciclo di vita dei processi server MCP avviati?"
> - "Quali sono le implicazioni di sicurezza nell'autorizzare l'AI ad accedere al file system?"

## Il Modulo Agentico

Mentre MCP fornisce strumenti standardizzati, il modulo **agentico** di LangChain4j offre un modo dichiarativo per costruire agenti che orchestrano quegli strumenti. L'annotazione `@Agent` e `AgenticServices` ti permettono di definire il comportamento degli agenti tramite interfacce piuttosto che codice imperativo.

In questo modulo, esplorerai il pattern **Supervisor Agent** — un approccio avanzato di AI agentica dove un agente "supervisore" decide dinamicamente quali sotto-agenti invocare in base alle richieste dell'utente. Combineremo entrambi i concetti dando a uno dei nostri sotto-agenti capacità di accesso file potenziate da MCP.

Per usare il modulo agentico, aggiungi questa dipendenza Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Nota:** Il modulo `langchain4j-agentic` utilizza una proprietà di versione separata (`langchain4j.mcp.version`) perché viene rilasciato con una tempistica diversa rispetto alle librerie core LangChain4j.

> **⚠️ Sperimentale:** Il modulo `langchain4j-agentic` è **sperimentale** e soggetto a modifiche. Il modo stabile per costruire assistenti AI rimane `langchain4j-core` con strumenti personalizzati (Modulo 04).

## Esecuzione degli Esempi

### Prerequisiti

- Completato [Modulo 04 - Tools](../04-tools/README.md) (questo modulo si basa sui concetti di strumenti personalizzati e li confronta con quelli MCP)
- File `.env` nella directory root con credenziali Azure (creato da `azd up` nel Modulo 01)
- Java 21+, Maven 3.9+
- Node.js 16+ e npm (per i server MCP)

> **Nota:** Se non hai ancora configurato le variabili d'ambiente, consulta [Modulo 01 - Introduzione](../01-introduction/README.md) per le istruzioni di deployment (`azd up` crea automaticamente il file `.env`), oppure copia `.env.example` in `.env` nella directory root e inserisci i tuoi valori.

## Avvio Rapido

**Usando VS Code:** Basta cliccare con il tasto destro su un file demo nell'Explorer e selezionare **"Esegui Java"**, oppure usa le configurazioni di avvio dal pannello Run and Debug (assicurati prima che il tuo file `.env` sia configurato con le credenziali Azure).

**Usando Maven:** In alternativa, puoi eseguire dalla linea di comando con gli esempi seguenti.

### Operazioni sui File (Stdio)

Questo dimostra strumenti basati su processi locali.

**✅ Nessun prerequisito necessario** - il server MCP viene avviato automaticamente.

**Usando gli Script di Avvio (Consigliato):**

Gli script di avvio caricano automaticamente le variabili d'ambiente dal file `.env` nella directory root:

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

**Usando VS Code:** Clicca con il tasto destro su `StdioTransportDemo.java` e seleziona **"Run Java"** (assicurati che il file `.env` sia configurato).

L'applicazione avvia automaticamente un server MCP filesystem e legge un file locale. Nota come la gestione del processo secondario è automatica.

**Output atteso:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agente Supervisore

Il **pattern Supervisor Agent** è una forma **flessibile** di AI agentica. Un Supervisore usa un LLM per decidere autonomamente quali agenti invocare in base alla richiesta dell'utente. Nel prossimo esempio, combiniamo l'accesso a file potenziato MCP con un agente LLM per creare un flusso di lettura file → report supervisionato.

Nella demo, `FileAgent` legge un file usando gli strumenti filesystem MCP, e `ReportAgent` genera un report strutturato con un sommario esecutivo (1 frase), 3 punti chiave e raccomandazioni. Il Supervisore orchestra questo flusso automaticamente:

<img src="../../../translated_images/it/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Pattern Agente Supervisore" width="800"/>

*Il Supervisore usa il suo LLM per decidere quali agenti invocare e in che ordine — nessun routing hardcoded necessario.*

Ecco come appare il workflow concreto della nostra pipeline da file a report:

<img src="../../../translated_images/it/file-report-workflow.649bb7a896800de9.webp" alt="Workflow File a Report" width="800"/>

*FileAgent legge il file tramite strumenti MCP, poi ReportAgent trasforma il contenuto grezzo in un report strutturato.*

Il diagramma di sequenza seguente traccia l'intera orchestrazione del Supervisore — dall'avvio del server MCP, alla selezione autonoma degli agenti da parte del Supervisore, alle chiamate agli strumenti via stdio e infine al report:

<img src="../../../translated_images/it/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Diagramma Sequenza Agente Supervisore" width="800"/>

*Il Supervisore invoca autonomamente FileAgent (che chiama il server MCP via stdio per leggere il file), poi invoca ReportAgent per generare un report strutturato — ogni agente salva il proprio output nella Agentic Scope condivisa.*

Ogni agente salva il proprio output nella **Agentic Scope** (memoria condivisa), permettendo agli agenti a valle di accedere ai risultati precedenti. Questo dimostra come gli strumenti MCP si integrino senza soluzione di continuità nei workflow agentici — il Supervisore non deve sapere *come* i file vengono letti, solo che `FileAgent` può farlo.

#### Esecuzione della Demo

Gli script di avvio caricano automaticamente le variabili d'ambiente dal file `.env` nella directory root:

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

**Usando VS Code:** Clicca con il tasto destro su `SupervisorAgentDemo.java` e seleziona **"Run Java"** (assicurati che il file `.env` sia configurato).

#### Come Funziona il Supervisore

Prima di costruire gli agenti, devi collegare il trasporto MCP a un client e avvolgerlo come `ToolProvider`. Ecco come gli strumenti del server MCP diventano disponibili agli agenti:

```java
// Crea un client MCP dal trasporto
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Avvolgi il client come un ToolProvider — questo collega gli strumenti MCP in LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Ora puoi iniettare `mcpToolProvider` in qualsiasi agente che necessita di strumenti MCP:

```java
// Passo 1: FileAgent legge i file usando gli strumenti MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Ha strumenti MCP per le operazioni sui file
        .build();

// Passo 2: ReportAgent genera report strutturati
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Il Supervisore coordina il flusso di lavoro file → report
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Restituisce il report finale
        .build();

// Il Supervisore decide quali agenti invocare in base alla richiesta
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Come FileAgent Scopre gli Strumenti MCP a Runtime

Potresti chiederti: **come fa `FileAgent` a sapere come usare gli strumenti filesystem npm?** La risposta è che non lo sa — è l'**LLM** che lo capisce a runtime attraverso gli schemi degli strumenti.

L'interfaccia `FileAgent` è solo una **definizione di prompt**. Non ha conoscenza hardcoded di `read_file`, `list_directory`, o di altri strumenti MCP. Ecco cosa succede end-to-end:
1. **Server avviato:** `StdioMcpTransport` lancia il pacchetto npm `@modelcontextprotocol/server-filesystem` come processo figlio  
2. **Scoperta degli strumenti:** Il `McpClient` invia una richiesta JSON-RPC `tools/list` al server, che risponde con nomi degli strumenti, descrizioni e schemi dei parametri (ad esempio, `read_file` — *"Leggi il contenuto completo di un file"* — `{ path: string }`)  
3. **Iniezione degli schemi:** `McpToolProvider` incapsula questi schemi scoperti e li rende disponibili a LangChain4j  
4. **Decisione LLM:** Quando viene chiamato `FileAgent.readFile(path)`, LangChain4j invia il messaggio di sistema, il messaggio utente **e la lista degli schemi degli strumenti** al LLM. Il LLM legge le descrizioni degli strumenti e genera una chiamata allo strumento (ad esempio, `read_file(path="/some/file.txt")`)  
5. **Esecuzione:** LangChain4j intercetta la chiamata allo strumento, la instrada attraverso il client MCP al subprocess Node.js, ottiene il risultato e lo alimenta di nuovo al LLM  

Questo è lo stesso meccanismo di [Tool Discovery](../../../05-mcp) descritto sopra, ma applicato specificamente al flusso di lavoro dell'agente. Le annotazioni `@SystemMessage` e `@UserMessage` guidano il comportamento del LLM, mentre il `ToolProvider` iniettato gli offre le **capacità** — il LLM fa da ponte tra i due a runtime.

> **🤖 Prova con la chat [GitHub Copilot](https://github.com/features/copilot):** Apri [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) e chiedi:  
> - "Come fa questo agente a sapere quale strumento MCP chiamare?"  
> - "Cosa succederebbe se rimuovessi il ToolProvider dal costruttore dell'agente?"  
> - "Come vengono passati gli schemi degli strumenti al LLM?"  

#### Strategie di Risposta

Quando configuri un `SupervisorAgent`, indichi come deve formulare la risposta finale all'utente dopo che i sotto-agenti hanno completato i loro compiti. Lo schema sottostante mostra le tre strategie disponibili — LAST restituisce direttamente l'output finale dell'agente, SUMMARY sintetizza tutti gli output tramite un LLM, e SCORED sceglie quello con punteggio più alto rispetto alla richiesta originale:

<img src="../../../translated_images/it/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Tre strategie per come il Supervisor formula la risposta finale — scegli in base a se vuoi l'output dell'ultimo agente, un riepilogo sintetizzato o l'opzione con il punteggio migliore.*

Le strategie disponibili sono:

| Strategia | Descrizione |
|----------|-------------|
| **LAST** | Il supervisore restituisce l'output dell'ultimo sotto-agente o strumento chiamato. Utile quando l'agente finale nel flusso di lavoro è specificamente progettato per produrre la risposta completa finale (ad esempio, un "Agente Riassuntore" in una pipeline di ricerca). |
| **SUMMARY** | Il supervisore usa il proprio modello linguistico interno (LLM) per sintetizzare un riepilogo dell'intera interazione e di tutti gli output dei sotto-agenti, quindi restituisce quel riepilogo come risposta finale. Questo fornisce una risposta pulita e aggregata all'utente. |
| **SCORED** | Il sistema usa un LLM interno per valutare sia la risposta LAST sia il SUMMARY dell'interazione rispetto alla richiesta utente originale, restituendo l'output con il punteggio più alto. |

Vedi [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) per l'implementazione completa.

> **🤖 Prova con la chat [GitHub Copilot](https://github.com/features/copilot):** Apri [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) e chiedi:  
> - "Come decide il Supervisor quali agenti chiamare?"  
> - "Qual è la differenza tra i modelli di workflow Supervisor e Sequential?"  
> - "Come posso personalizzare il comportamento di pianificazione del Supervisor?"  

#### Comprendere l'Output

Quando esegui la demo, vedrai una guida strutturata su come il Supervisor orchestra più agenti. Ecco cosa significa ogni sezione:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**L'intestazione** introduce il concetto di workflow: una pipeline focalizzata dalla lettura file alla generazione del report.

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
  
**Diagramma di Workflow** mostra il flusso dati tra agenti. Ogni agente ha un ruolo specifico:  
- **FileAgent** legge file usando strumenti MCP e conserva il contenuto grezzo in `fileContent`  
- **ReportAgent** consuma quel contenuto e produce un report strutturato in `report`  

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Richiesta Utente** mostra il compito. Il Supervisor la analizza e decide di invocare FileAgent → ReportAgent.

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
  
**Orchestrazione del Supervisor** mostra il flusso in 2 passi:  
1. **FileAgent** legge il file tramite MCP e conserva il contenuto  
2. **ReportAgent** riceve il contenuto e genera un report strutturato  

Il Supervisor ha preso queste decisioni **autonomamente** basandosi sulla richiesta dell'utente.

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
  
#### Spiegazione delle Funzionalità del Modulo Agentic

L'esempio dimostra diverse funzionalità avanzate del modulo agentic. Diamo uno sguardo più da vicino a Agentic Scope e Agent Listeners.

**Agentic Scope** mostra la memoria condivisa dove gli agenti hanno memorizzato i loro risultati usando `@Agent(outputKey="...")`. Questo permette di:  
- Far accedere agli agenti successivi gli output degli agenti precedenti  
- Permettere al Supervisor di sintetizzare una risposta finale  
- Consentirti di ispezionare cosa ha prodotto ogni agente  

Il diagramma sottostante mostra come Agentic Scope funzioni come memoria condivisa nel workflow da file a report — FileAgent scrive il suo output sotto la chiave `fileContent`, ReportAgent legge quello e scrive il proprio output sotto `report`:

<img src="../../../translated_images/it/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope funge da memoria condivisa — FileAgent scrive `fileContent`, ReportAgent lo legge e scrive `report`, e il tuo codice legge il risultato finale.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Dati grezzi del file da FileAgent
String report = scope.readState("report");            // Rapporto strutturato da ReportAgent
```
  
**Agent Listeners** permettono di monitorare e fare debug dell'esecuzione degli agenti. L'output passo-passo che vedi nella demo proviene da un AgentListener che si aggancia a ogni invocazione di agente:  
- **beforeAgentInvocation** - Chiamato quando il Supervisor seleziona un agente, ti mostra quale agente è stato scelto e perché  
- **afterAgentInvocation** - Chiamato quando un agente termina, mostrando il suo risultato  
- **inheritedBySubagents** - Quando true, il listener monitora tutti gli agenti nella gerarchia  

Il diagramma seguente mostra il ciclo di vita completo di Agent Listener, incluso come `onError` gestisce i fallimenti durante l'esecuzione agenti:

<img src="../../../translated_images/it/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Gli Agent Listeners si agganciano al ciclo di vita di esecuzione — monitorano quando gli agenti iniziano, terminano o incontrano errori.*

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
        return true; // Propagare a tutti i sotto-agenti
    }
};
```
  
Oltre al pattern Supervisor, il modulo `langchain4j-agentic` fornisce diversi potenti modelli di workflow. Il diagramma sottostante mostra tutti e cinque — da pipeline sequenziali semplici a workflow di approvazione con intervento umano:

<img src="../../../translated_images/it/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Cinque modelli di workflow per orchestrare agenti — da pipeline sequenziali semplici a workflow di approvazione con intervento umano.*

| Modello | Descrizione | Caso d'Uso |
|---------|-------------|------------|
| **Sequential** | Esegue gli agenti in ordine, l'output scorre al successivo | Pipeline: ricerca → analisi → report |
| **Parallel** | Esegue agenti simultaneamente | Task indipendenti: meteo + notizie + azioni |
| **Loop** | Itera finché condizione è soddisfatta | Valutazione qualità: raffina finché score ≥ 0.8 |
| **Conditional** | Instrada basandosi su condizioni | Classifica → instrada ad agente specialista |
| **Human-in-the-Loop** | Aggiunge checkpoint umani | Workflow di approvazione, revisione contenuti |

## Concetti Chiave

Ora che hai esplorato MCP e il modulo agentic in azione, riassumiamo quando usare ogni approccio.

Uno dei maggiori vantaggi di MCP è il suo ecosistema in crescita. Il diagramma seguente mostra come un singolo protocollo universale colleghi la tua applicazione AI a una vasta gamma di server MCP — dall'accesso a filesystem e database a GitHub, email, web scraping e altro:

<img src="../../../translated_images/it/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP crea un ecosistema con protocollo universale — qualsiasi server compatibile MCP funziona con qualsiasi client MCP, permettendo la condivisione di strumenti tra applicazioni.*

**MCP** è ideal quando vuoi sfruttare ecosistemi di strumenti esistenti, creare strumenti che più applicazioni possono condividere, integrare servizi di terze parti con protocolli standard o sostituire implementazioni di strumenti senza cambiare codice.

**Il Modulo Agentic** è più indicato quando vuoi definizioni di agenti dichiarative con annotazioni `@Agent`, necessiti orchestrazione di workflow (sequenziale, loop, parallelo), preferisci progettare agenti basati su interfacce anziché codice imperativo, o stai combinando più agenti che condividono output tramite `outputKey`.

**Il pattern Supervisor Agent** brilla quando il workflow non è prevedibile in anticipo e vuoi che sia il LLM a decidere, quando hai più agenti specializzati che necessitano orchestrazione dinamica, quando costruisci sistemi conversazionali che instradano a diverse capacità, o quando vuoi il comportamento agente più flessibile e adattivo.

Per aiutarti a decidere tra i metodi custom `@Tool` del Modulo 04 e gli strumenti MCP di questo modulo, la seguente comparazione evidenzia i principali compromessi — gli strumenti custom forniscono accoppiamento stretto e completa sicurezza di tipo per logiche specifiche all'app, mentre gli strumenti MCP offrono integrazioni standardizzate e riutilizzabili:

<img src="../../../translated_images/it/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Quando usare metodi custom @Tool vs strumenti MCP — strumenti custom per logiche specifiche con sicurezza di tipo completa, strumenti MCP per integrazioni standardizzate funzionanti tra applicazioni.*

## Congratulazioni!

Hai completato tutti e cinque i moduli del corso LangChain4j for Beginners! Ecco uno sguardo al percorso completo di apprendimento che hai completato — dalla chat di base fino ai sistemi agentic alimentati MCP:

<img src="../../../translated_images/it/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Il tuo percorso di apprendimento attraverso tutti e cinque i moduli — dalla chat di base ai sistemi agentic alimentati MCP.*

Hai completato il corso LangChain4j for Beginners. Hai imparato:

- Come costruire AI conversazionali con memoria (Modulo 01)  
- Pattern di prompt engineering per compiti diversi (Modulo 02)  
- Come fondare risposte sui tuoi documenti con RAG (Modulo 03)  
- Come creare agenti AI base (assistenti) con strumenti custom (Modulo 04)  
- Come integrare strumenti standardizzati con i moduli LangChain4j MCP e Agentic (Modulo 05)  

### Cosa Fare Dopo?

Dopo aver completato i moduli, esplora la [Guida ai Test](../docs/TESTING.md) per vedere i concetti di testing LangChain4j in azione.

**Risorse Ufficiali:**  
- [Documentazione LangChain4j](https://docs.langchain4j.dev/) - Guide complete e riferimento API  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Codice sorgente ed esempi  
- [Tutorial LangChain4j](https://docs.langchain4j.dev/tutorials/) - Tutorial passo-passo per vari casi d'uso  

Grazie per aver completato questo corso!

---

**Navigazione:** [← Precedente: Modulo 04 - Strumenti](../04-tools/README.md) | [Torna alla Home](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Nota di avviso**:
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Pur impegnandoci per garantire accuratezza, si prega di notare che le traduzioni automatiche possono contenere errori o imprecisioni. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche, si consiglia una traduzione professionale effettuata da un umano. Non ci assumiamo alcuna responsabilità per eventuali incomprensioni o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->