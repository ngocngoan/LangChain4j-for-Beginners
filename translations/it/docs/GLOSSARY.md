# Glossario LangChain4j

## Indice

- [Concetti Fondamentali](../../../docs)
- [Componenti LangChain4j](../../../docs)
- [Concetti AI/ML](../../../docs)
- [Guardrails](../../../docs)
- [Prompt Engineering](../../../docs)
- [RAG (Generazione Arricchita da Recupero)](../../../docs)
- [Agenti e Strumenti](../../../docs)
- [Modulo Agentico](../../../docs)
- [Protocollo di Contesto Modello (MCP)](../../../docs)
- [Servizi Azure](../../../docs)
- [Testing e Sviluppo](../../../docs)

Riferimento rapido per termini e concetti usati durante il corso.

## Concetti Fondamentali

**Agente AI** - Sistema che utilizza AI per ragionare e agire autonomamente. [Modulo 04](../04-tools/README.md)

**Catena** - Sequenza di operazioni in cui l'output alimenta il passo successivo.

**Suddivisione (Chunking)** - Suddividere documenti in pezzi più piccoli. Tipico: 300-500 token con sovrapposizione. [Modulo 03](../03-rag/README.md)

**Finestra di Contesto** - Massimo numero di token che un modello può processare. GPT-5.2: 400K token (fino a 272K input, 128K output).

**Embedding** - Vettori numerici che rappresentano il significato del testo. [Modulo 03](../03-rag/README.md)

**Chiamata di Funzione** - Il modello genera richieste strutturate per chiamare funzioni esterne. [Modulo 04](../04-tools/README.md)

**Allucinazione** - Quando i modelli generano informazioni errate ma plausibili.

**Prompt** - Testo di input per un modello di linguaggio. [Modulo 02](../02-prompt-engineering/README.md)

**Ricerca Semantica** - Ricerca basata sul significato usando embedding, non parole chiave. [Modulo 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: nessuna memoria. Stateful: mantiene la cronologia della conversazione. [Modulo 01](../01-introduction/README.md)

**Token** - Unità base di testo che i modelli processano. Influisce su costi e limiti. [Modulo 01](../01-introduction/README.md)

**Catena di Strumenti** - Esecuzione sequenziale di strumenti dove l'output informa la chiamata successiva. [Modulo 04](../04-tools/README.md)

## Componenti LangChain4j

**AiServices** - Crea interfacce di servizi AI type-safe.

**OpenAiOfficialChatModel** - Client unificato per modelli OpenAI e Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Crea embedding usando il client ufficiale OpenAI (supporta sia OpenAI che Azure OpenAI).

**ChatModel** - Interfaccia core per modelli di linguaggio.

**ChatMemory** - Mantiene la cronologia della conversazione.

**ContentRetriever** - Trova i chunk di documento rilevanti per RAG.

**DocumentSplitter** - Suddivide documenti in chunk.

**EmbeddingModel** - Converte testo in vettori numerici.

**EmbeddingStore** - Memorizza e recupera embedding.

**MessageWindowChatMemory** - Mantiene una finestra mobile dei messaggi recenti.

**PromptTemplate** - Crea prompt riutilizzabili con segnaposti `{{variable}}`.

**TextSegment** - Chunk di testo con metadati. Usato in RAG.

**ToolExecutionRequest** - Rappresenta una richiesta di esecuzione di uno strumento.

**UserMessage / AiMessage / SystemMessage** - Tipi di messaggi di conversazione.

## Concetti AI/ML

**Few-Shot Learning** - Fornire esempi all'interno dei prompt. [Modulo 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - Modelli AI addestrati su grandi quantità di testo.

**Sforzo di Ragionamento** - Parametro GPT-5.2 che controlla la profondità del pensiero. [Modulo 02](../02-prompt-engineering/README.md)

**Temperatura** - Controlla la casualità in output. Basso=deterministico, alto=creativo.

**Database Vettoriale** - Database specializzato per embedding. [Modulo 03](../03-rag/README.md)

**Zero-Shot Learning** - Eseguire compiti senza esempi. [Modulo 02](../02-prompt-engineering/README.md)

## Guardrails - [Modulo 00](../00-quick-start/README.md)

**Difesa in Profondità** - Approccio di sicurezza a più livelli che combina guardrails a livello applicativo con filtri di sicurezza del provider.

**Blocco Rigido** - Il provider lancia un errore HTTP 400 per gravi violazioni di contenuto.

**InputGuardrail** - Interfaccia LangChain4j per validare l'input dell'utente prima che raggiunga l'LLM. Risparmia costi e latenza bloccando prompt dannosi in anticipo.

**InputGuardrailResult** - Tipo di ritorno per la validazione guardrail: `success()` o `fatal("reason")`.

**OutputGuardrail** - Interfaccia per validare le risposte AI prima di restituirle agli utenti.

**Filtri di Sicurezza del Provider** - Filtri di contenuto integrati dai provider AI (es. GitHub Models) che intercettano violazioni a livello API.

**Rifiuto Soft** - Il modello declina educatamente di rispondere senza generare errore.

## Prompt Engineering - [Modulo 02](../02-prompt-engineering/README.md)

**Catena di Pensiero (Chain-of-Thought)** - Ragionamento passo-passo per maggiore accuratezza.

**Output Vincolato (Constrained Output)** - Applicare formato o struttura specifici.

**Alta Prontezza (High Eagerness)** - Pattern GPT-5.2 per ragionamento approfondito.

**Bassa Prontezza (Low Eagerness)** - Pattern GPT-5.2 per risposte rapide.

**Conversazione Multi-Turno** - Mantenere il contesto attraverso gli scambi.

**Prompting Basato sul Ruolo** - Definire la persona del modello tramite messaggi di sistema.

**Auto-Riflessione** - Il modello valuta e migliora il proprio output.

**Analisi Strutturata** - Quadro di valutazione fisso.

**Pattern di Esecuzione del Compito** - Pianifica → Esegui → Riassumi.

## RAG (Generazione Arricchita da Recupero) - [Modulo 03](../03-rag/README.md)

**Pipeline di Elaborazione Documento** - Carica → suddividi → incorpora → archivia.

**Archiviazione Embedding In-Memory** - Memorizzazione non persistente per test.

**RAG** - Combina recupero con generazione per ancorare le risposte.

**Punteggio di Similarità** - Misura (0-1) di somiglianza semantica.

**Riferimento alla Fonte** - Metadati sul contenuto recuperato.

## Agenti e Strumenti - [Modulo 04](../04-tools/README.md)

**Annotazione @Tool** - Marca metodi Java come strumenti richiamabili dall'AI.

**Pattern ReAct** - Ragiona → Agisci → Osserva → Ripeti.

**Gestione Sessione** - Contesti separati per utenti differenti.

**Strumento (Tool)** - Funzione che un agente AI può chiamare.

**Descrizione Strumento** - Documentazione dello scopo e dei parametri dello strumento.

## Modulo Agentico - [Modulo 05](../05-mcp/README.md)

**Annotazione @Agent** - Marca interfacce come agenti AI con definizione comportamentale dichiarativa.

**Agent Listener** - Hook per monitorare l’esecuzione agente tramite `beforeAgentInvocation()` e `afterAgentInvocation()`.

**Ambito Agentico (Agentic Scope)** - Memoria condivisa dove gli agenti memorizzano output utilizzando `outputKey` per agenti a valle.

**AgenticServices** - Factory per creare agenti usando `agentBuilder()` e `supervisorBuilder()`.

**Workflow Condizionale** - Routing basato su condizioni verso agenti specialisti diversi.

**Human-in-the-Loop** - Pattern di workflow che aggiunge checkpoint umani per approvazione o revisione contenuti.

**langchain4j-agentic** - Dipendenza Maven per costruzione agenti dichiarativa (sperimentale).

**Workflow a Ciclo (Loop Workflow)** - Iterare l’esecuzione agente finché una condizione è soddisfatta (es. punteggio qualità ≥ 0.8).

**outputKey** - Parametro annotazione agente che specifica dove vengono archiviati i risultati nell’Ambito Agentico.

**Workflow Parallelo** - Esegue più agenti simultaneamente per compiti indipendenti.

**Strategia di Risposta** - Come il supervisore formula la risposta finale: LAST, SUMMARY, o SCORED.

**Workflow Sequenziale** - Esegue agenti in ordine dove l’output fluisce al passo successivo.

**Pattern Agent Supervisore** - Pattern avanzato agentico dove un LLM supervisore decide dinamicamente quali sub-agenti invocare.

## Protocollo di Contesto Modello (MCP) - [Modulo 05](../05-mcp/README.md)

**langchain4j-mcp** - Dipendenza Maven per l’integrazione MCP in LangChain4j.

**MCP** - Model Context Protocol: standard per collegare app AI a strumenti esterni. Scrivi una volta, usa ovunque.

**Client MCP** - Applicazione che si connette a server MCP per scoprire e usare strumenti.

**Server MCP** - Servizio che espone strumenti via MCP con descrizioni chiare e schemi parametri.

**McpToolProvider** - Componente LangChain4j che incapsula strumenti MCP per uso in servizi AI e agenti.

**McpTransport** - Interfaccia per comunicazione MCP. Implementazioni includono Stdio e HTTP.

**Trasporto Stdio** - Trasporto processo locale tramite stdin/stdout. Utile per accesso filesystem o strumenti da riga di comando.

**StdioMcpTransport** - Implementazione LangChain4j che avvia server MCP come processo secondario.

**Scoperta Strumenti** - Client interroga server per strumenti disponibili con descrizioni e schemi.

## Servizi Azure - [Modulo 01](../01-introduction/README.md)

**Azure AI Search** - Ricerca cloud con funzionalità vettoriali. [Modulo 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Deploy di risorse Azure.

**Azure OpenAI** - Servizio AI enterprise di Microsoft.

**Bicep** - Linguaggio infrastructure-as-code per Azure. [Guida Infrastructure](../01-introduction/infra/README.md)

**Nome di Deploy** - Nome per il deployment del modello in Azure.

**GPT-5.2** - Ultimo modello OpenAI con controllo del ragionamento. [Modulo 02](../02-prompt-engineering/README.md)

## Testing e Sviluppo - [Guida Testing](TESTING.md)

**Dev Container** - Ambiente di sviluppo containerizzato. [Configurazione](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Playground AI gratuito. [Modulo 00](../00-quick-start/README.md)

**Testing In-Memory** - Testing con archiviazione in memoria.

**Testing di Integrazione** - Testing con infrastruttura reale.

**Maven** - Strumento Java per automazione build.

**Mockito** - Framework Java per mocking.

**Spring Boot** - Framework applicativo Java. [Modulo 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avvertenza**:
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Pur impegnandoci per garantire accuratezza, si prega di notare che le traduzioni automatiche possono contenere errori o inesattezze. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale effettuata da un umano. Non ci assumiamo alcuna responsabilità per malintesi o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->