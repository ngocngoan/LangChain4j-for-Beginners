# Glossario LangChain4j

## Indice

- [Concetti Core](../../../docs)
- [Componenti LangChain4j](../../../docs)
- [Concetti AI/ML](../../../docs)
- [Guardrails](../../../docs)
- [Prompt Engineering](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agenti e Strumenti](../../../docs)
- [Modulo Agentic](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Servizi Azure](../../../docs)
- [Testing e Sviluppo](../../../docs)

Riferimento rapido per termini e concetti utilizzati durante il corso.

## Concetti Core

**AI Agent** - Sistema che usa AI per ragionare e agire autonomamente. [Modulo 04](../04-tools/README.md)

**Chain** - Sequenza di operazioni dove l’output alimenta il passo successivo.

**Chunking** - Suddividere documenti in pezzi più piccoli. Tipico: 300-500 token con sovrapposizione. [Modulo 03](../03-rag/README.md)

**Context Window** - Numero massimo di token che un modello può processare. GPT-5.2: 400K token.

**Embeddings** - Vettori numerici che rappresentano il significato del testo. [Modulo 03](../03-rag/README.md)

**Function Calling** - Il modello genera richieste strutturate per chiamare funzioni esterne. [Modulo 04](../04-tools/README.md)

**Hallucination** - Quando i modelli generano informazioni errate ma plausibili.

**Prompt** - Input testuale per un modello linguistico. [Modulo 02](../02-prompt-engineering/README.md)

**Semantic Search** - Ricerca per significato usando embeddings, non parole chiave. [Modulo 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: senza memoria. Stateful: mantiene la cronologia della conversazione. [Modulo 01](../01-introduction/README.md)

**Tokens** - Unità testuali base che i modelli processano. Influiscono su costi e limiti. [Modulo 01](../01-introduction/README.md)

**Tool Chaining** - Esecuzione sequenziale di strumenti dove l’output informa la chiamata successiva. [Modulo 04](../04-tools/README.md)

## Componenti LangChain4j

**AiServices** - Crea interfacce di servizio AI type-safe.

**OpenAiOfficialChatModel** - Client unificato per modelli OpenAI e Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Crea embeddings usando il client OpenAI Official (supporta sia OpenAI che Azure OpenAI).

**ChatModel** - Interfaccia core per modelli linguistici.

**ChatMemory** - Mantiene la cronologia della conversazione.

**ContentRetriever** - Trova chunk di documenti rilevanti per RAG.

**DocumentSplitter** - Suddivide documenti in chunk.

**EmbeddingModel** - Converte testo in vettori numerici.

**EmbeddingStore** - Memorizza e recupera embeddings.

**MessageWindowChatMemory** - Mantiene una finestra mobile dei messaggi recenti.

**PromptTemplate** - Crea prompt riutilizzabili con segnaposto `{{variable}}`.

**TextSegment** - Chunk di testo con metadata. Usato in RAG.

**ToolExecutionRequest** - Rappresenta una richiesta di esecuzione di uno strumento.

**UserMessage / AiMessage / SystemMessage** - Tipi di messaggi in conversazione.

## Concetti AI/ML

**Few-Shot Learning** - Fornire esempi nei prompt. [Modulo 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - Modelli AI addestrati su grandi quantità di testo.

**Reasoning Effort** - Parametro GPT-5.2 che controlla la profondità del ragionamento. [Modulo 02](../02-prompt-engineering/README.md)

**Temperature** - Controlla l’imprevedibilità dell’output. Bassa=deterministico, alta=creativo.

**Vector Database** - Database specializzato per embeddings. [Modulo 03](../03-rag/README.md)

**Zero-Shot Learning** - Eseguire compiti senza esempi. [Modulo 02](../02-prompt-engineering/README.md)

## Guardrails - [Modulo 00](../00-quick-start/README.md)

**Defense in Depth** - Approccio di sicurezza multilivello che combina guardrails a livello applicativo con filtri di sicurezza del provider.

**Hard Block** - Il provider genera errore HTTP 400 per violazioni gravi dei contenuti.

**InputGuardrail** - Interfaccia LangChain4j per validare l’input utente prima di raggiungere l’LLM. Risparmia costi e latenza bloccando prompt dannosi in anticipo.

**InputGuardrailResult** - Tipo di ritorno per la validazione guardrail: `success()` o `fatal("reason")`.

**OutputGuardrail** - Interfaccia per validare le risposte AI prima di restituirle agli utenti.

**Provider Safety Filters** - Filtri contenuti integrati dai provider AI (es. GitHub Models) che intercettano violazioni a livello API.

**Soft Refusal** - Il modello rifiuta educatamente di rispondere senza generare errore.

## Prompt Engineering - [Modulo 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Ragionamento passo dopo passo per maggiore accuratezza.

**Constrained Output** - Forzare un formato o struttura specifica.

**High Eagerness** - Pattern GPT-5.2 per ragionamenti approfonditi.

**Low Eagerness** - Pattern GPT-5.2 per risposte rapide.

**Multi-Turn Conversation** - Mantenere il contesto attraverso più scambi.

**Role-Based Prompting** - Impostare la persona del modello tramite messaggi di sistema.

**Self-Reflection** - Il modello valuta e migliora il proprio output.

**Structured Analysis** - Quadro di valutazione fisso.

**Task Execution Pattern** - Pianifica → Esegui → Riassumi.

## RAG (Retrieval-Augmented Generation) - [Modulo 03](../03-rag/README.md)

**Document Processing Pipeline** - Carica → suddividi → incorpora → memorizza.

**In-Memory Embedding Store** - Memoria non persistente per testing.

**RAG** - Combina ricerca con generazione per fondare le risposte.

**Similarity Score** - Misura (0-1) di similitudine semantica.

**Source Reference** - Metadata sul contenuto recuperato.

## Agenti e Strumenti - [Modulo 04](../04-tools/README.md)

**@Tool Annotation** - Marca metodi Java come strumenti chiamabili dall’AI.

**ReAct Pattern** - Ragiona → Agisci → Osserva → Ripeti.

**Session Management** - Contesti separati per utenti diversi.

**Tool** - Funzione che un agent AI può chiamare.

**Tool Description** - Documentazione di scopo e parametri dello strumento.

## Modulo Agentic - [Modulo 05](../05-mcp/README.md)

**@Agent Annotation** - Marca interfacce come agent AI con definizione comportamentale dichiarativa.

**Agent Listener** - Hook per monitorare l’esecuzione agente via `beforeAgentInvocation()` e `afterAgentInvocation()`.

**Agentic Scope** - Memoria condivisa dove gli agenti memorizzano output usando `outputKey` per agenti downstream.

**AgenticServices** - Factory per creare agenti usando `agentBuilder()` e `supervisorBuilder()`.

**Conditional Workflow** - Instradamento basato su condizioni verso agenti specialisti diversi.

**Human-in-the-Loop** - Pattern di workflow con checkpoint umani per approvazione o revisione contenuti.

**langchain4j-agentic** - Dipendenza Maven per costruzione agente dichiarativa (sperimentale).

**Loop Workflow** - Itera esecuzione agente finché una condizione è soddisfatta (es. punteggio qualità ≥ 0.8).

**outputKey** - Parametro annotazione agente che specifica dove immagazzinare i risultati in Agentic Scope.

**Parallel Workflow** - Esegue più agenti simultaneamente per compiti indipendenti.

**Response Strategy** - Come il supervisore formula la risposta finale: LAST, SUMMARY, o SCORED.

**Sequential Workflow** - Esegue agenti in ordine dove l’output scorre nel passo successivo.

**Supervisor Agent Pattern** - Pattern agentic avanzato in cui un supervisore LLM decide dinamicamente quali sotto-agenti invocare.

## Model Context Protocol (MCP) - [Modulo 05](../05-mcp/README.md)

**langchain4j-mcp** - Dipendenza Maven per integrazione MCP in LangChain4j.

**MCP** - Model Context Protocol: standard per collegare app AI a strumenti esterni. Costruisci una volta, usa ovunque.

**MCP Client** - Applicazione che si connette a server MCP per scoprire e usare strumenti.

**MCP Server** - Servizio che espone strumenti via MCP con descrizioni chiare e schemi parametri.

**McpToolProvider** - Componente LangChain4j che incapsula strumenti MCP per uso in servizi AI e agenti.

**McpTransport** - Interfaccia per comunicazione MCP. Implementazioni includono Stdio e HTTP.

**Stdio Transport** - Trasporto locale via stdin/stdout. Utile per accesso filesystem o strumenti da linea di comando.

**StdioMcpTransport** - Implementazione LangChain4j che genera server MCP come sotto-processo.

**Tool Discovery** - Il client interroga il server per strumenti disponibili con descrizioni e schemi.

## Servizi Azure - [Modulo 01](../01-introduction/README.md)

**Azure AI Search** - Ricerca cloud con capacità vettoriali. [Modulo 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Deploy di risorse Azure.

**Azure OpenAI** - Servizio AI enterprise di Microsoft.

**Bicep** - Linguaggio Azure infrastructure-as-code. [Guida infrastruttura](../01-introduction/infra/README.md)

**Deployment Name** - Nome per il deployment del modello in Azure.

**GPT-5.2** - Ultimo modello OpenAI con controllo del ragionamento. [Modulo 02](../02-prompt-engineering/README.md)

## Testing e Sviluppo - [Guida al Testing](TESTING.md)

**Dev Container** - Ambiente di sviluppo containerizzato. [Configurazione](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Playground AI modello gratuito. [Modulo 00](../00-quick-start/README.md)

**In-Memory Testing** - Testing con memorizzazione in memoria.

**Integration Testing** - Testing con infrastruttura reale.

**Maven** - Tool di automazione build Java.

**Mockito** - Framework Java per mocking.

**Spring Boot** - Framework applicativo Java. [Modulo 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Pur facendo del nostro meglio per garantire l’accuratezza, si prega di notare che le traduzioni automatiche possono contenere errori o inesattezze. Il documento originale nella sua lingua originale deve essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale effettuata da un traduttore umano. Non siamo responsabili per eventuali malintesi o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->