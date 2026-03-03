# Modulo 05: Protocollo del Contesto del Modello (MCP)

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
    - [Strategie di Risposta](../../../05-mcp)
    - [Comprensione dell'Uscita](../../../05-mcp)
    - [Spiegazione delle Funzionalità del Modulo Agentico](../../../05-mcp)
- [Concetti Chiave](../../../05-mcp)
- [Congratulazioni!](../../../05-mcp)
  - [Cosa Fare Dopo?](../../../05-mcp)

## Cosa Imparerai

Hai costruito AI conversazionale, padroneggiato i prompt, ancorato risposte in documenti e creato agenti con tool. Ma tutti quei tool erano personalizzati per la tua applicazione specifica. E se potessi dare alla tua AI accesso a un ecosistema standardizzato di tool che chiunque può creare e condividere? In questo modulo, apprenderai come fare proprio questo con il Model Context Protocol (MCP) e il modulo agentico di LangChain4j. Mostriamo prima un semplice lettore di file MCP e poi come si integra facilmente in workflow agentici avanzati usando il pattern Supervisor Agent.

## Cos'è MCP?

Il Model Context Protocol (MCP) offre esattamente questo: un modo standard per le applicazioni AI di scoprire e utilizzare tool esterni. Invece di scrivere integrazioni personalizzate per ogni sorgente dati o servizio, ti connetti a server MCP che espongono le loro capacità in un formato coerente. Il tuo agente AI può così scoprire e usare automaticamente questi tool.

Il diagramma qui sotto mostra la differenza — senza MCP, ogni integrazione richiede cablaggi punto-punto personalizzati; con MCP, un solo protocollo collega la tua app a qualsiasi tool:

<img src="../../../translated_images/it/mcp-comparison.9129a881ecf10ff5.webp" alt="Confronto MCP" width="800"/>

*Prima di MCP: integrazioni punto-punto complesse. Dopo MCP: un protocollo, infinite possibilità.*

MCP risolve un problema fondamentale nello sviluppo AI: ogni integrazione è personalizzata. Vuoi accedere a GitHub? Codice personalizzato. Vuoi leggere file? Codice personalizzato. Vuoi interrogare un database? Codice personalizzato. E nessuna di queste integrazioni funziona con altre applicazioni AI.

MCP standardizza tutto questo. Un server MCP espone tool con descrizioni e schemi chiari. Qualsiasi client MCP può connettersi, scoprire i tool disponibili e usarli. Costruisci una volta, usa ovunque.

Il diagramma sotto illustra questa architettura — un singolo client MCP (la tua applicazione AI) si connette a molteplici server MCP, ciascuno che espone il proprio set di tool attraverso il protocollo standard:

<img src="../../../translated_images/it/mcp-architecture.b3156d787a4ceac9.webp" alt="Architettura MCP" width="800"/>

*Architettura del Model Context Protocol - scoperta e esecuzione standardizzate dei tool*

## Come Funziona MCP

Dietro le quinte, MCP usa un’architettura a strati. La tua applicazione Java (il client MCP) scopre i tool disponibili, invia richieste JSON-RPC attraverso un livello di trasporto (Stdio o HTTP), e il server MCP esegue le operazioni e ritorna i risultati. Il diagramma seguente scompone ogni strato di questo protocollo:

<img src="../../../translated_images/it/mcp-protocol-detail.01204e056f45308b.webp" alt="Dettaglio Protocollo MCP" width="800"/>

*Come funziona MCP nel dettaglio — i client scoprono i tool, scambiano messaggi JSON-RPC, ed eseguono operazioni tramite un livello di trasporto.*

**Architettura Server-Client**

MCP usa un modello client-server. I server forniscono tool - leggere file, interrogare database, chiamare API. I client (la tua applicazione AI) si connettono ai server e usano i loro tool.

Per usare MCP con LangChain4j, aggiungi questa dipendenza Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Scoperta dei Tool**

Quando il tuo client si connette a un server MCP, chiede "Quali tool avete?" Il server risponde con una lista di tool disponibili, ciascuno con descrizioni e schemi dei parametri. Il tuo agente AI può quindi decidere quali tool usare in base alle richieste dell’utente. Il diagramma qui sotto mostra questo handshake — il client invia una richiesta `tools/list` e il server ritorna i tool disponibili con descrizioni e schemi dei parametri:

<img src="../../../translated_images/it/tool-discovery.07760a8a301a7832.webp" alt="Scoperta Tool MCP" width="800"/>

*L'AI scopre i tool disponibili all’avvio — ora sa quali capacità sono presenti e può decidere quali usare.*

**Meccanismi di Trasporto**

MCP supporta diversi meccanismi di trasporto. Le due opzioni sono Stdio (per comunicazione con processi locali) e HTTP streamabile (per server remoti). Questo modulo dimostra il trasporto Stdio:

<img src="../../../translated_images/it/transport-mechanisms.2791ba7ee93cf020.webp" alt="Meccanismi di Trasporto" width="800"/>

*Meccanismi di trasporto MCP: HTTP per server remoti, Stdio per processi locali*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Per processi locali. La tua applicazione avvia un server come sottoprocesso e comunica tramite input/output standard. Utile per accesso filesystem o strumenti da linea di comando.

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

Il server `@modelcontextprotocol/server-filesystem` espone i seguenti tool, tutti confinati nelle directory che specifichi:

| Tool | Descrizione |
|------|-------------|
| `read_file` | Legge il contenuto di un singolo file |
| `read_multiple_files` | Legge più file in una singola chiamata |
| `write_file` | Crea o sovrascrive un file |
| `edit_file` | Esegue modifiche mirate di ricerca e sostituzione |
| `list_directory` | Elenca file e directory in un percorso |
| `search_files` | Cerca ricorsivamente file che corrispondono a un pattern |
| `get_file_info` | Ottiene metadati file (dimensione, timestamp, permessi) |
| `create_directory` | Crea una directory (inclusi eventuali genitori) |
| `move_file` | Sposta o rinomina un file o una directory |

Il diagramma seguente mostra come funziona il trasporto Stdio a runtime — la tua applicazione Java avvia il server MCP come processo figlio e comunicano tramite pipe stdin/stdout, senza rete o HTTP:

<img src="../../../translated_images/it/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Flusso Trasporto Stdio" width="800"/>

*Trasporto Stdio in azione — la tua applicazione avvia il server MCP come processo figlio e comunica tramite pipe stdin/stdout.*

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) e chiedi:
> - "Come funziona il trasporto Stdio e quando dovrei usarlo invece di HTTP?"
> - "Come gestisce LangChain4j il ciclo di vita dei processi server MCP avviati?"
> - "Quali implicazioni di sicurezza ci sono nel dare all’AI accesso al file system?"

## Il Modulo Agentico

Mentre MCP fornisce tool standardizzati, il modulo agentico di LangChain4j offre un modo dichiarativo per costruire agenti che orchestrano quei tool. L’annotazione `@Agent` e `AgenticServices` ti permettono di definire il comportamento dell’agente tramite interfacce invece di codice imperativo.

In questo modulo, esplorerai il pattern **Supervisor Agent** — un approccio agentico avanzato dove un agente "supervisore" decide dinamicamente quali sub-agenti invocare in base alle richieste dell’utente. Combineremo entrambi i concetti fornendo a uno dei nostri sub-agenti capacità di accesso file potenziate da MCP.

Per usare il modulo agentico, aggiungi questa dipendenza Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Nota:** Il modulo `langchain4j-agentic` usa una proprietà di versione separata (`langchain4j.mcp.version`) perché viene rilasciato con una tempistica diversa rispetto alle librerie core di LangChain4j.

> **⚠️ Sperimentale:** Il modulo `langchain4j-agentic` è **sperimentale** e soggetto a cambiamenti. Il modo stabile per costruire assistenti AI rimane `langchain4j-core` con tool personalizzati (Modulo 04).

## Esecuzione degli Esempi

### Prerequisiti

- Completato [Modulo 04 - Tools](../04-tools/README.md) (questo modulo si basa sui concetti dei tool personalizzati e li confronta con i tool MCP)
- File `.env` nella directory principale con le credenziali Azure (creato da `azd up` nel Modulo 01)
- Java 21+, Maven 3.9+
- Node.js 16+ e npm (per i server MCP)

> **Nota:** Se non hai ancora configurato le variabili d’ambiente, consulta [Modulo 01 - Introduzione](../01-introduction/README.md) per le istruzioni di deployment (`azd up` crea automaticamente il file `.env`), oppure copia `.env.example` in `.env` nella directory principale e inserisci i tuoi valori.

## Avvio Rapido

**Usando VS Code:** Basta cliccare con il tasto destro su qualsiasi file demo nell’Explorer e selezionare **"Run Java"**, oppure usare le configurazioni di avvio dal pannello Run and Debug (assicurati prima che il file `.env` sia configurato con le credenziali Azure).

**Usando Maven:** In alternativa, puoi eseguire dalla linea di comando con gli esempi seguenti.

### Operazioni sui File (Stdio)

Dimostra tool basati su sottoprocesso locale.

**✅ Nessun prerequisito necessario** - il server MCP viene avviato automaticamente.

**Usando gli Script di Avvio (Consigliato):**

Gli script di avvio caricano automaticamente le variabili d’ambiente dal file `.env` nella root:

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

**Usando VS Code:** Clicca con il tasto destro su `StdioTransportDemo.java` e seleziona **"Run Java"** (assicurati che il tuo file `.env` sia configurato).

L’applicazione avvia automaticamente un server MCP per il filesystem e legge un file locale. Nota come la gestione del sottoprocesso è automatica.

**Output previsto:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agente Supervisore

Il **pattern Supervisor Agent** è una forma **flessibile** di AI agentica. Un Supervisore usa un LLM per decidere autonomamente quali agenti invocare in base alla richiesta utente. Nel prossimo esempio combiniamo l’accesso file potenziato da MCP con un agente LLM per creare un flusso supervisato di lettura file → report.

Nella demo, `FileAgent` legge un file usando i tool filesystem MCP, e `ReportAgent` genera un report strutturato con un sommario esecutivo (1 frase), 3 punti chiave e raccomandazioni. Il Supervisore orchestra questo flusso automaticamente:

<img src="../../../translated_images/it/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Pattern Agente Supervisore" width="800"/>

*Il Supervisore usa il suo LLM per decidere quali agenti invocare e in quale ordine — nessun routing hardcoded necessario.*

Ecco come appare il workflow concreto per la nostra pipeline file → report:

<img src="../../../translated_images/it/file-report-workflow.649bb7a896800de9.webp" alt="Workflow File a Report" width="800"/>

*FileAgent legge il file tramite tool MCP, poi ReportAgent trasforma il contenuto grezzo in un report strutturato.*

Ogni agente memorizza i propri output nell’**Agentic Scope** (memoria condivisa), permettendo agli agenti successivi di accedere ai risultati precedenti. Questo dimostra come i tool MCP si integrano perfettamente nei workflow agentici — il Supervisore non deve sapere *come* i file siano letti, solo che `FileAgent` può farlo.

#### Esecuzione Demo

Gli script di avvio caricano automaticamente le variabili d’ambiente dal file `.env` nella root:

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

**Usando VS Code:** Clicca con il tasto destro su `SupervisorAgentDemo.java` e seleziona **"Run Java"** (assicurati che il tuo file `.env` sia configurato).

#### Come Funziona il Supervisore

Prima di costruire agenti, devi connettere il trasporto MCP a un client e avvolgerlo come `ToolProvider`. Ecco come i tool del server MCP diventano disponibili ai tuoi agenti:

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

Ora puoi iniettare `mcpToolProvider` in qualsiasi agente che necessita dei tool MCP:

```java
// Passo 1: FileAgent legge i file usando gli strumenti MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Ha strumenti MCP per le operazioni sui file
        .build();

// Passo 2: ReportAgent genera rapporti strutturati
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Il Supervisore orchestra il flusso di lavoro file → rapporto
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Restituisce il rapporto finale
        .build();

// Il Supervisore decide quali agenti invocare in base alla richiesta
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Strategie di Risposta

Quando configuri un `SupervisorAgent`, specifichi come deve formulare la risposta finale all’utente dopo che i sub-agenti hanno completato i loro compiti. Il diagramma sotto mostra le tre strategie disponibili — LAST restituisce direttamente l’output dell’ultimo agente, SUMMARY sintetizza tutti gli output tramite un LLM, e SCORED sceglie l’output che ottiene il punteggio più alto rispetto alla richiesta originale:

<img src="../../../translated_images/it/response-strategies.3d0cea19d096bdf9.webp" alt="Strategie di Risposta" width="800"/>

*Tre strategie per come il Supervisore formula la risposta finale — scegli in base al fatto se vuoi l’output dell’ultimo agente, un sommario sintetizzato, o l’opzione con il punteggio migliore.*

Le strategie disponibili sono:

| Strategia | Descrizione |
|----------|-------------|
| **LAST** | Il supervisore restituisce l’output dell’ultimo sub-agente o tool chiamato. Utile quando l’ultimo agente nel workflow è specificamente progettato per produrre la risposta completa e finale (es. un “Agente Sommario” in una pipeline di ricerca). |
| **SUMMARY** | Il supervisore usa il proprio modello linguistico interno (LLM) per sintetizzare un sommario dell’intera interazione e di tutti gli output dei sub-agenti, quindi restituisce quel sommario come risposta finale. Fornisce una risposta aggregata e pulita all’utente. |
| **SCORED** | Il sistema usa un LLM interno per valutare sia la risposta LAST che il sommario SUMMARY dell’interazione rispetto alla richiesta originale dell’utente, restituendo l’output con il punteggio più alto. |
Vedi [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) per l'implementazione completa.

> **🤖 Prova con la chat di [GitHub Copilot](https://github.com/features/copilot):** Apri [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) e chiedi:
> - "Come decide il Supervisor quali agenti invocare?"
> - "Qual è la differenza tra i pattern di workflow Supervisor e Sequenziale?"
> - "Come posso personalizzare il comportamento di pianificazione del Supervisor?"

#### Comprensione dell'Output

Quando esegui la demo, vedrai un percorso strutturato su come il Supervisor orchestra più agenti. Ecco cosa significa ogni sezione:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**L'intestazione** introduce il concetto di workflow: una pipeline focalizzata dalla lettura del file alla generazione del report.

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

**Diagramma del Workflow** mostra il flusso di dati tra gli agenti. Ogni agente ha un ruolo specifico:
- **FileAgent** legge i file usando gli strumenti MCP e memorizza il contenuto grezzo in `fileContent`
- **ReportAgent** utilizza quel contenuto e produce un report strutturato in `report`

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

**Orchestrazione del Supervisor** mostra in azione il flusso a 2 passaggi:
1. **FileAgent** legge il file tramite MCP e memorizza il contenuto
2. **ReportAgent** riceve il contenuto e genera un report strutturato

Il Supervisor ha preso queste decisioni **in modo autonomo** in base alla richiesta dell'utente.

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

L'esempio dimostra diverse funzionalità avanzate del modulo agentic. Vediamo più da vicino Agentic Scope e Agent Listeners.

**Agentic Scope** mostra la memoria condivisa dove gli agenti hanno memorizzato i loro risultati usando `@Agent(outputKey="...")`. Questo permette:
- Agenti successivi di accedere agli output degli agenti precedenti
- Al Supervisor di sintetizzare una risposta finale
- A te di ispezionare ciò che ciascun agente ha prodotto

Il diagramma sotto mostra come Agentic Scope funzioni come memoria condivisa nel workflow da file a report — FileAgent scrive il suo output sotto la chiave `fileContent`, ReportAgent lo legge e scrive il suo output sotto `report`:

<img src="../../../translated_images/it/agentic-scope.95ef488b6c1d02ef.webp" alt="Memoria Condivisa Agentic Scope" width="800"/>

*Agentic Scope agisce come memoria condivisa — FileAgent scrive `fileContent`, ReportAgent lo legge e scrive `report`, e il tuo codice legge il risultato finale.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Dati grezzi del file da FileAgent
String report = scope.readState("report");            // Rapporto strutturato da ReportAgent
```

**Agent Listeners** permettono di monitorare e fare debug dell’esecuzione degli agenti. L’output passo-passo che vedi nella demo proviene da un AgentListener che si aggancia a ogni invocazione di agente:
- **beforeAgentInvocation** - Chiamato quando il Supervisor seleziona un agente, permettendoti di vedere quale agente è stato scelto e perché
- **afterAgentInvocation** - Chiamato quando un agente termina, mostrando il suo risultato
- **inheritedBySubagents** - Quando true, il listener monitora tutti gli agenti nella gerarchia

Il diagramma seguente mostra l’intero ciclo di vita di un Agent Listener, inclusa la gestione degli errori con `onError` durante l’esecuzione dell’agente:

<img src="../../../translated_images/it/agent-listeners.784bfc403c80ea13.webp" alt="Ciclo di Vita Agent Listeners" width="800"/>

*Agent Listeners si agganciano al ciclo di vita dell’esecuzione — monitorano quando gli agenti iniziano, terminano o incontrano errori.*

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

Oltre al pattern Supervisor, il modulo `langchain4j-agentic` fornisce diversi potenti pattern di workflow. Il diagramma sotto mostra tutti e cinque — da pipeline sequenziali semplici a workflow di approvazione con intervento umano:

<img src="../../../translated_images/it/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Pattern di Workflow Agenti" width="800"/>

*Cinque pattern di workflow per orchestrare agenti — da pipeline sequenziali semplici a workflow di approvazione con intervento umano.*

| Pattern | Descrizione | Caso d’uso |
|---------|-------------|------------|
| **Sequenziale** | Esegue gli agenti in ordine, l’output fluisce al successivo | Pipeline: ricerca → analisi → report |
| **Parallelo** | Esegue agenti simultaneamente | Attività indipendenti: meteo + notizie + azioni |
| **Loop** | Itèra finché la condizione è soddisfatta | Valutazione qualità: affina finché score ≥ 0.8 |
| **Condizionale** | Instrada in base a condizioni | Classifica → inoltra all’agente specialista |
| **Human-in-the-Loop** | Aggiunge checkpoint umani | Workflow di approvazione, revisione contenuti |

## Concetti Chiave

Ora che hai esplorato MCP e il modulo agentic in azione, riassumiamo quando usare ciascun approccio.

Uno dei maggiori vantaggi di MCP è il suo ecosistema in crescita. Il diagramma sottostante mostra come un singolo protocollo universale connette la tua applicazione AI a una varietà di server MCP — dall’accesso a filesystem e database a GitHub, email, web scraping e altro:

<img src="../../../translated_images/it/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="Ecosistema MCP" width="800"/>

*MCP crea un ecosistema di protocolli universali — qualsiasi server compatibile MCP funziona con qualsiasi client compatibile MCP, permettendo la condivisione di strumenti tra applicazioni.*

**MCP** è ideale quando vuoi sfruttare ecosistemi di strumenti esistenti, costruire strumenti che più applicazioni possono condividere, integrare servizi terzi con protocolli standard o sostituire implementazioni di strumenti senza cambiare codice.

**Il Modulo Agentic** funziona meglio quando vuoi definizioni di agenti dichiarative con annotazioni `@Agent`, necessiti orchestrazione di workflow (sequenziale, loop, parallelo), preferisci il design degli agenti basato su interfacce rispetto al codice imperativo, o stai combinando più agenti che condividono output tramite `outputKey`.

**Il pattern Supervisor Agent** brilla quando il workflow non è prevedibile in anticipo e vuoi che sia il LLM a decidere, quando hai più agenti specializzati che necessitano orchestrazione dinamica, quando costruisci sistemi conversazionali che instradano a diverse capacità, o quando vuoi il comportamento agente più flessibile e adattivo.

Per aiutarti a scegliere tra i metodi personalizzati `@Tool` del Modulo 04 e gli strumenti MCP di questo modulo, la seguente comparazione evidenzia i principali compromessi — gli strumenti personalizzati ti offrono accoppiamento stretto e piena sicurezza di tipo per la logica specifica dell’app, mentre gli strumenti MCP offrono integrazioni standardizzate e riutilizzabili:

<img src="../../../translated_images/it/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Strumenti Personalizzati vs Strumenti MCP" width="800"/>

*Quando usare metodi custom @Tool vs strumenti MCP — strumenti custom per logica specifica dell’app con piena sicurezza di tipo, strumenti MCP per integrazioni standardizzate e funzionanti tra applicazioni.*

## Congratulazioni!

Hai completato tutti e cinque i moduli del corso LangChain4j per Principianti! Ecco uno sguardo al percorso di apprendimento completo che hai completato — dal chat di base fino ai sistemi agentic potenziati da MCP:

<img src="../../../translated_images/it/course-completion.48cd201f60ac7570.webp" alt="Completamento del Corso" width="800"/>

*Il tuo percorso di apprendimento attraverso tutti e cinque i moduli — dalla chat di base ai sistemi agentic potenziati da MCP.*

Hai completato il corso LangChain4j per Principianti. Hai imparato:

- Come costruire AI conversazionale con memoria (Modulo 01)
- Pattern di prompt engineering per compiti diversi (Modulo 02)
- Fondare risposte sui tuoi documenti con RAG (Modulo 03)
- Creare agenti AI base (assistenti) con strumenti personalizzati (Modulo 04)
- Integrare strumenti standardizzati con i moduli LangChain4j MCP e Agentic (Modulo 05)

### Cosa Fare Ora?

Dopo aver completato i moduli, esplora la [Guida al Testing](../docs/TESTING.md) per vedere i concetti di testing di LangChain4j in azione.

**Risorse Ufficiali:**
- [Documentazione LangChain4j](https://docs.langchain4j.dev/) - Guide complete e riferimento API
- [LangChain4j su GitHub](https://github.com/langchain4j/langchain4j) - Codice sorgente ed esempi
- [Tutorial LangChain4j](https://docs.langchain4j.dev/tutorials/) - Tutorial passo-passo per vari casi d’uso

Grazie per aver completato questo corso!

---

**Navigazione:** [← Precedente: Modulo 04 - Tools](../04-tools/README.md) | [Torna al Principale](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Pur impegnandoci per garantire l’accuratezza, si prega di notare che le traduzioni automatiche possono contenere errori o inesattezze. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale effettuata da un essere umano. Non siamo responsabili per eventuali incomprensioni o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->