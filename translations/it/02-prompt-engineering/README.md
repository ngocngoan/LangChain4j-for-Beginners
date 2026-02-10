# Modulo 02: Prompt Engineering con GPT-5.2

## Indice

- [Cosa Imparerai](../../../02-prompt-engineering)
- [Prerequisiti](../../../02-prompt-engineering)
- [Comprendere il Prompt Engineering](../../../02-prompt-engineering)
- [Come Usa LangChain4j](../../../02-prompt-engineering)
- [I Pattern Fondamentali](../../../02-prompt-engineering)
- [Uso delle Risorse Azure Esistenti](../../../02-prompt-engineering)
- [Screenshot dell'Applicazione](../../../02-prompt-engineering)
- [Esplorazione dei Pattern](../../../02-prompt-engineering)
  - [Bassa vs Alta Voglia](../../../02-prompt-engineering)
  - [Esecuzione di Compiti (Preludi degli Strumenti)](../../../02-prompt-engineering)
  - [Codice Auto-Riflettente](../../../02-prompt-engineering)
  - [Analisi Strutturata](../../../02-prompt-engineering)
  - [Chat Multi-Turno](../../../02-prompt-engineering)
  - [Ragionamento Passo dopo Passo](../../../02-prompt-engineering)
  - [Output Vincolato](../../../02-prompt-engineering)
- [Cosa Stai Davvero Imparando](../../../02-prompt-engineering)
- [Prossimi Passi](../../../02-prompt-engineering)

## Cosa Imparerai

Nel modulo precedente hai visto come la memoria abiliti l'AI conversazionale e hai usato i Modelli GitHub per interazioni di base. Ora ci concentreremo su come porre domande - i prompt stessi - usando GPT-5.2 di Azure OpenAI. Il modo in cui strutturi i tuoi prompt influenza drasticamente la qualità delle risposte che ottieni.

Useremo GPT-5.2 perché introduce il controllo del ragionamento - puoi dire al modello quanto deve riflettere prima di rispondere. Questo rende più evidenti le diverse strategie di prompting e ti aiuta a capire quando usare ogni approccio. Beneficeremo anche dei limiti di frequenza meno restrittivi di Azure per GPT-5.2 rispetto ai Modelli GitHub.

## Prerequisiti

- Modulo 01 completato (risorse Azure OpenAI distribuite)
- File `.env` nella directory radice con credenziali Azure (creato da `azd up` nel Modulo 01)

> **Nota:** Se non hai completato il Modulo 01, segui prima le istruzioni di distribuzione lì.

## Comprendere il Prompt Engineering

Il prompt engineering riguarda il progettare il testo di input che ottiene costantemente i risultati di cui hai bisogno. Non si tratta solo di fare domande - ma di strutturare richieste in modo che il modello capisca esattamente cosa vuoi e come fornirlo.

Pensalo come dare istruzioni a un collega. "Risolvere il bug" è vago. "Risolvere la eccezione null pointer in UserService.java alla riga 45 aggiungendo un controllo null" è specifico. I modelli linguistici funzionano allo stesso modo - specificità e struttura contano.

## Come Usa LangChain4j

Questo modulo dimostra pattern avanzati di prompting usando la stessa base LangChain4j dei moduli precedenti, con un focus sulla struttura del prompt e il controllo del ragionamento.

<img src="../../../translated_images/it/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*Come LangChain4j collega i tuoi prompt a Azure OpenAI GPT-5.2*

**Dipendenze** - Il Modulo 02 usa le seguenti dipendenze langchain4j definite in `pom.xml`:
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**Configurazione OpenAiOfficialChatModel** - [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Il modello chat è configurato manualmente come bean Spring usando il client Official OpenAI, che supporta gli endpoint Azure OpenAI. La differenza chiave rispetto al Modulo 01 è come strutturiamo i prompt inviati a `chatModel.chat()`, non la configurazione del modello in sé.

**Messaggi di Sistema e Utente** - [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j separa i tipi di messaggi per chiarezza. `SystemMessage` imposta il comportamento e il contesto dell'AI (come "Sei un revisore di codice"), mentre `UserMessage` contiene la richiesta effettiva. Questa separazione ti permette di mantenere un comportamento AI coerente tra diverse richieste utente.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/it/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage fornisce il contesto persistente mentre UserMessages contengono richieste individuali*

**MessageWindowChatMemory per Multi-Turno** - Per il pattern di conversazione multi-turno, riutilizziamo `MessageWindowChatMemory` dal Modulo 01. Ogni sessione ha la propria istanza di memoria memorizzata in una `Map<String, ChatMemory>`, permettendo più conversazioni concorrenti senza mescolare contesti.

**Template di Prompt** - Il vero focus qui è il prompt engineering, non nuove API LangChain4j. Ogni pattern (bassa voglia, alta voglia, esecuzione compiti, ecc.) usa lo stesso metodo `chatModel.chat(prompt)` ma con stringhe di prompt strutturate con cura. I tag XML, le istruzioni e il formato fanno tutti parte del testo del prompt, non delle funzionalità LangChain4j.

**Controllo del Ragionamento** - Lo sforzo di ragionamento di GPT-5.2 è controllato attraverso istruzioni nel prompt come "massimo 2 passi di ragionamento" o "esplora a fondo". Sono tecniche di prompt engineering, non configurazioni LangChain4j. La libreria semplicemente invia i tuoi prompt al modello.

Il punto chiave: LangChain4j fornisce l'infrastruttura (connessione modello via [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), memoria, gestione messaggi via [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), mentre questo modulo ti insegna come costruire prompt efficaci all’interno di questa infrastruttura.

## I Pattern Fondamentali

Non tutti i problemi richiedono lo stesso approccio. Alcune domande hanno bisogno di risposte rapide, altre di riflessioni profonde. Alcune richiedono ragionamento visibile, altre solo risultati. Questo modulo copre otto pattern di prompting - ciascuno ottimizzato per diversi scenari. Sperimenterai tutti per capire quando funziona meglio ogni approccio.

<img src="../../../translated_images/it/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Panoramica degli otto pattern di prompt engineering e i loro casi d’uso*

<img src="../../../translated_images/it/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Bassa voglia (veloce, diretto) vs Alta voglia (approfondito, esplorativo) approcci di ragionamento*

**Bassa Voglia (Veloce e Mirato)** - Per domande semplici dove vuoi risposte rapide e dirette. Il modello fa un ragionamento minimo - massimo 2 passi. Usalo per calcoli, ricerche o domande dirette.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Esplora con GitHub Copilot:** Apri [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) e chiedi:
> - "Qual è la differenza tra i pattern di prompt a bassa e alta voglia?"
> - "Come aiutano i tag XML nei prompt a strutturare la risposta dell'AI?"
> - "Quando dovrei usare pattern di auto-riflessione rispetto a istruzioni dirette?"

**Alta Voglia (Profondo e Approfondito)** - Per problemi complessi dove vuoi un'analisi completa. Il modello esplora a fondo e mostra ragionamenti dettagliati. Usalo per progettazione di sistemi, decisioni architetturali o ricerche complesse.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Esecuzione di Compiti (Progresso Passo dopo Passo)** - Per flussi di lavoro in più fasi. Il modello fornisce un piano anticipato, narra ogni passo mentre lavora, poi dà un riepilogo. Usalo per migrazioni, implementazioni o processi multi-step.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Il prompting Chain-of-Thought chiede esplicitamente al modello di mostrare il processo di ragionamento, migliorando l’accuratezza per compiti complessi. La scomposizione passo dopo passo aiuta sia gli umani che l’AI a comprendere la logica.

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Chiedi di questo pattern:
> - "Come adattare il pattern di esecuzione compiti per operazioni a lungo termine?"
> - "Quali sono le best practice per strutturare i preludi degli strumenti in applicazioni di produzione?"
> - "Come catturare e mostrare aggiornamenti intermedi di progresso in un’interfaccia utente?"

<img src="../../../translated_images/it/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Flusso di lavoro Pianifica → Esegui → Riassumi per compiti multi-step*

**Codice Auto-Riflettente** - Per generare codice di qualità produzione. Il modello genera codice, lo verifica rispetto a criteri di qualità e lo migliora iterativamente. Usalo quando crei nuove funzionalità o servizi.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/it/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Ciclo di miglioramento iterativo - genera, valuta, identifica problemi, migliora, ripeti*

**Analisi Strutturata** - Per valutazioni coerenti. Il modello revisa il codice usando un framework fisso (correttezza, pratiche, performance, sicurezza). Usalo per code review o assessment qualità.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Chiedi dell’analisi strutturata:
> - "Come personalizzare il framework di analisi per diversi tipi di code review?"
> - "Qual è il modo migliore per analizzare e agire su output strutturati programmaticamente?"
> - "Come garantire livelli di gravità coerenti in diverse sessioni di revisione?"

<img src="../../../translated_images/it/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Framework a quattro categorie per revisioni codice coerenti con livelli di gravità*

**Chat Multi-Turno** - Per conversazioni che necessitano di contesto. Il modello ricorda i messaggi precedenti e costruisce su di essi. Usalo per sessioni di aiuto interattivo o Q&A complessi.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/it/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Come il contesto della conversazione si accumula su più turni fino al limite di token*

**Ragionamento Passo dopo Passo** - Per problemi che richiedono una logica visibile. Il modello mostra il ragionamento esplicito per ogni passo. Usalo per problemi matematici, puzzle logici o quando vuoi capire il processo mentale.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/it/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Scomposizione dei problemi in passi logici espliciti*

**Output Vincolato** - Per risposte con requisiti di formato specifici. Il modello rispetta rigorosamente le regole di formato e lunghezza. Usalo per riepiloghi o quando vuoi una struttura di output precisa.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/it/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Applicazione di requisiti specifici di formato, lunghezza e struttura*

## Uso delle Risorse Azure Esistenti

**Verifica distribuzione:**

Assicurati che il file `.env` esista nella directory radice con le credenziali Azure (creato durante il Modulo 01):
```bash
cat ../.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Avvia l’applicazione:**

> **Nota:** Se hai già avviato tutte le applicazioni usando `./start-all.sh` dal Modulo 01, questo modulo è già in esecuzione sulla porta 8083. Puoi saltare i comandi di avvio qui sotto e andare direttamente su http://localhost:8083.

**Opzione 1: Usare Spring Boot Dashboard (Consigliato per utenti VS Code)**

Il container di sviluppo include l’estensione Spring Boot Dashboard, che fornisce un’interfaccia visiva per gestire tutte le app Spring Boot. La trovi nella Activity Bar a sinistra in VS Code (icona Spring Boot).

Dal Spring Boot Dashboard puoi:
- Vedere tutte le applicazioni Spring Boot disponibili nel workspace
- Avviare/fermare applicazioni con un click
- Visualizzare i log delle applicazioni in tempo reale
- Monitorare lo stato delle applicazioni

Clicca semplicemente il pulsante play accanto a "prompt-engineering" per avviare questo modulo, oppure avvia tutti i moduli insieme.

<img src="../../../translated_images/it/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opzione 2: Usare script shell**

Avvia tutte le web app (moduli 01-04):

**Bash:**
```bash
cd ..  # Dalla directory radice
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Dalla directory principale
.\start-all.ps1
```

Oppure avvia solo questo modulo:

**Bash:**
```bash
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

Entrambi gli script caricano automaticamente le variabili ambiente dal file `.env` radice e compileranno i JAR se non esistono.

> **Nota:** Se preferisci compilare manualmente tutti i moduli prima di avviare:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Apri http://localhost:8083 nel tuo browser.

**Per fermare:**

**Bash:**
```bash
./stop.sh  # Solo questo modulo
# O
cd .. && ./stop-all.sh  # Tutti i moduli
```

**PowerShell:**
```powershell
.\stop.ps1  # Solo questo modulo
# O
cd ..; .\stop-all.ps1  # Tutti i moduli
```

## Screenshot dell'Applicazione

<img src="../../../translated_images/it/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*La dashboard principale mostra tutti gli 8 pattern di prompt engineering con le loro caratteristiche e casi d’uso*

## Esplorazione dei Pattern

L’interfaccia web ti permette di sperimentare diverse strategie di prompting. Ogni pattern risolve problemi diversi - provali per vedere quando ogni approccio funziona meglio.

### Bassa vs Alta Voglia

Fai una domanda semplice come "Qual è il 15% di 200?" usando Bassa Voglia. Otterrai una risposta rapida e diretta. Ora chiedi qualcosa di complesso come "Progetta una strategia di caching per un’API ad alto traffico" usando Alta Voglia. Osserva come il modello rallenta e fornisce ragionamenti dettagliati. Stesso modello, stessa struttura di domanda - ma il prompt gli dice quanto deve riflettere.
<img src="../../../translated_images/it/low-eagerness-demo.898894591fb23aa0.webp" alt="Dimostrazione Bassa Prontezza" width="800"/>

*Calcolo rapido con ragionamento minimo*

<img src="../../../translated_images/it/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Dimostrazione Alta Prontezza" width="800"/>

*Strategia di caching completa (2,8MB)*

### Esecuzione del Compito (Preludi degli Strumenti)

I flussi di lavoro multi-step beneficiano di una pianificazione anticipata e di una narrazione del progresso. Il modello delinea cosa farà, narra ogni passaggio, poi riassume i risultati.

<img src="../../../translated_images/it/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Dimostrazione Esecuzione Compito" width="800"/>

*Creazione di un endpoint REST con narrazione passo passo (3,9MB)*

### Codice Auto-Riflettente

Prova "Crea un servizio di validazione email". Invece di generare solo il codice e fermarsi, il modello genera, valuta secondo criteri di qualità, identifica le debolezze e migliora. Vedrai iterare fino a quando il codice soddisfa gli standard di produzione.

<img src="../../../translated_images/it/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Dimostrazione Codice Auto-Riflettente" width="800"/>

*Servizio completo di validazione email (5,2MB)*

### Analisi Strutturata

Le revisioni del codice richiedono quadri valutativi coerenti. Il modello analizza il codice usando categorie fisse (correttezza, pratiche, prestazioni, sicurezza) con livelli di gravità.

<img src="../../../translated_images/it/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Dimostrazione Analisi Strutturata" width="800"/>

*Revisione del codice basata su framework*

### Chat Multi-Turno

Chiedi "Cos'è Spring Boot?" e poi subito dopo "Fammi vedere un esempio". Il modello ricorda la tua prima domanda e ti fornisce un esempio specifico di Spring Boot. Senza memoria, la seconda domanda sarebbe troppo vaga.

<img src="../../../translated_images/it/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Dimostrazione Chat Multi-Turno" width="800"/>

*Conservazione del contesto tra le domande*

### Ragionamento Passo per Passo

Scegli un problema matematico e provalo sia con il Ragionamento Passo per Passo sia con la Bassa Prontezza. La bassa prontezza ti dà solo la risposta - veloce ma opaca. Il passo per passo ti mostra ogni calcolo e decisione.

<img src="../../../translated_images/it/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Dimostrazione Ragionamento Passo per Passo" width="800"/>

*Problema matematico con passaggi espliciti*

### Output Vincolato

Quando hai bisogno di formati specifici o conteggi di parole, questo schema impone un rigoroso rispetto. Prova a generare un sommario con esattamente 100 parole in formato elenco puntato.

<img src="../../../translated_images/it/constrained-output-demo.567cc45b75da1633.webp" alt="Dimostrazione Output Vincolato" width="800"/>

*Sommario di machine learning con controllo del formato*

## Cosa Stai Davvero Imparando

**Lo Sforzo di Ragionamento Cambia Tutto**

GPT-5.2 ti permette di controllare lo sforzo computazionale tramite i tuoi prompt. Poco sforzo significa risposte veloci con esplorazione minima. Alto sforzo significa che il modello si prende tempo per riflettere a fondo. Stai imparando a modulare lo sforzo in base alla complessità del compito - non sprecare tempo su domande semplici, ma non affrettare decisioni complesse.

**La Struttura Guida il Comportamento**

Hai notato i tag XML nei prompt? Non sono decorativi. I modelli seguono istruzioni strutturate più affidabilmente rispetto al testo libero. Quando hai bisogno di processi multi-step o logica complessa, la struttura aiuta il modello a tenere traccia di dove si trova e cosa deve fare dopo.

<img src="../../../translated_images/it/prompt-structure.a77763d63f4e2f89.webp" alt="Struttura del Prompt" width="800"/>

*Anatomia di un prompt ben strutturato con sezioni chiare e organizzazione in stile XML*

**Qualità attraverso l'Auto-Valutazione**

Gli schemi auto-riflettenti funzionano rendendo espliciti i criteri di qualità. Invece di sperare che il modello "faccia bene", gli dici esattamente cosa significa "bene": logica corretta, gestione degli errori, prestazioni, sicurezza. Il modello può quindi valutare il proprio output e migliorarlo. Questo trasforma la generazione di codice da una lotteria a un processo.

**Il Contesto è Finito**

Le conversazioni multi-turno funzionano includendo la cronologia dei messaggi ad ogni richiesta. Ma c'è un limite - ogni modello ha un conteggio massimo di token. Man mano che le conversazioni crescono, dovrai adottare strategie per mantenere il contesto rilevante senza superare quel limite. Questo modulo ti mostra come funziona la memoria; più avanti imparerai quando riassumere, quando dimenticare e quando recuperare.

## Prossimi Passi

**Modulo Successivo:** [03-rag - RAG (Generazione Aumentata dal Recupero)](../03-rag/README.md)

---

**Navigazione:** [← Precedente: Modulo 01 - Introduzione](../01-introduction/README.md) | [Torna al Principale](../README.md) | [Successivo: Modulo 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Dichiarazione di non responsabilità**:
Questo documento è stato tradotto utilizzando il servizio di traduzione AI [Co-op Translator](https://github.com/Azure/co-op-translator). Pur impegnandoci per garantire l’accuratezza, si prega di considerare che le traduzioni automatiche possono contenere errori o imprecisioni. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda la traduzione professionale umana. Non ci assumiamo alcuna responsabilità per eventuali malintesi o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->