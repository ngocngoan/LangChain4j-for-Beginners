# Modulo 02: Ingegneria dei Prompt con GPT-5.2

## Indice

- [Video Dimostrativo](../../../02-prompt-engineering)
- [Cosa Imparerai](../../../02-prompt-engineering)
- [Prerequisiti](../../../02-prompt-engineering)
- [Comprendere l'Ingegneria dei Prompt](../../../02-prompt-engineering)
- [Fondamenti di Ingegneria dei Prompt](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Modelli di Prompt](../../../02-prompt-engineering)
- [Pattern Avanzati](../../../02-prompt-engineering)
- [Utilizzo delle Risorse Azure Esistenti](../../../02-prompt-engineering)
- [Screenshot dell'Applicazione](../../../02-prompt-engineering)
- [Esplorazione dei Pattern](../../../02-prompt-engineering)
  - [Bassa vs Alta Prontezza](../../../02-prompt-engineering)
  - [Esecuzione del Compito (Preludi per Strumenti)](../../../02-prompt-engineering)
  - [Codice Auto-Riflettente](../../../02-prompt-engineering)
  - [Analisi Strutturata](../../../02-prompt-engineering)
  - [Chat Multi-Turno](../../../02-prompt-engineering)
  - [Ragionamento Passo per Passo](../../../02-prompt-engineering)
  - [Output Vincolato](../../../02-prompt-engineering)
- [Cosa Stai Veramente Imparando](../../../02-prompt-engineering)
- [Passi Successivi](../../../02-prompt-engineering)

## Video Dimostrativo

Guarda questa sessione live che spiega come iniziare con questo modulo:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Cosa Imparerai

<img src="../../../translated_images/it/what-youll-learn.c68269ac048503b2.webp" alt="Cosa Imparerai" width="800"/>

Nel modulo precedente, hai visto come la memoria abilita l'IA conversazionale e utilizzato i Modelli GitHub per interazioni di base. Ora ci concentreremo su come porre le domande — i prompt stessi — usando GPT-5.2 di Azure OpenAI. Il modo in cui strutturi i prompt influenza drasticamente la qualità delle risposte che ottieni. Iniziamo con una revisione delle tecniche fondamentali di prompting, poi passiamo a otto pattern avanzati che sfruttano appieno le capacità di GPT-5.2.

Useremo GPT-5.2 perché introduce il controllo del ragionamento - puoi dire al modello quanto deve pensare prima di rispondere. Questo rende più evidenti le diverse strategie di prompting e ti aiuta a capire quando usare ciascun approccio. Beneficeremo anche dei limiti di frequenza meno restrittivi di Azure per GPT-5.2 rispetto ai Modelli GitHub.

## Prerequisiti

- Completato il Modulo 01 (risorse Azure OpenAI distribuite)
- File `.env` nella directory radice con credenziali Azure (creato da `azd up` nel Modulo 01)

> **Nota:** Se non hai completato il Modulo 01, segui prima le istruzioni di distribuzione lì indicate.

## Comprendere l'Ingegneria dei Prompt

<img src="../../../translated_images/it/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Cos'è l'Ingegneria dei Prompt?" width="800"/>

L'ingegneria dei prompt consiste nel progettare testo di input che ti dia costantemente i risultati necessari. Non si tratta solo di fare domande - si tratta di strutturare le richieste in modo che il modello capisca esattamente cosa vuoi e come fornirtelo.

Pensalo come dare istruzioni a un collega. "Correggi il bug" è vago. "Correggi l'eccezione null pointer in UserService.java alla linea 45 aggiungendo un controllo null" è specifico. I modelli linguistici funzionano allo stesso modo - specificità e struttura sono importanti.

<img src="../../../translated_images/it/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Come LangChain4j si Inserisce" width="800"/>

LangChain4j fornisce l'infrastruttura — connessioni ai modelli, memoria e tipi di messaggi — mentre i pattern di prompt sono solo testo accuratamente strutturato che invii attraverso quell'infrastruttura. I componenti chiave sono `SystemMessage` (che imposta il comportamento e il ruolo dell'IA) e `UserMessage` (che contiene la tua richiesta vera e propria).

## Fondamenti di Ingegneria dei Prompt

<img src="../../../translated_images/it/five-patterns-overview.160f35045ffd2a94.webp" alt="Panoramica dei Cinque Pattern di Prompt Engineering" width="800"/>

Prima di immergerci nei pattern avanzati di questo modulo, rivediamo cinque tecniche fondamentali di prompting. Sono i mattoni fondamentali che ogni ingegnere di prompt dovrebbe conoscere. Se hai già lavorato attraverso il [modulo Quick Start](../00-quick-start/README.md#2-prompt-patterns), li hai visti in azione — ecco il quadro concettuale dietro di essi.

### Zero-Shot Prompting

L'approccio più semplice: dare al modello un'istruzione diretta senza esempi. Il modello si basa interamente sul proprio addestramento per comprendere ed eseguire il compito. Questo funziona bene per richieste dirette dove il comportamento atteso è ovvio.

<img src="../../../translated_images/it/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Istruzione diretta senza esempi — il modello deduce il compito solo dall'istruzione*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Risposta: "Positivo"
```

**Quando usarlo:** Classificazioni semplici, domande dirette, traduzioni, o qualsiasi compito che il modello può gestire senza ulteriori indicazioni.

### Few-Shot Prompting

Fornisci esempi che mostrano il modello il pattern che vuoi che segua. Il modello apprende il formato input-output atteso dai tuoi esempi e lo applica a nuovi input. Questo migliora drasticamente la coerenza per compiti dove il formato o comportamento desiderato non è ovvio.

<img src="../../../translated_images/it/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Apprendimento dagli esempi — il modello identifica il pattern e lo applica a nuovi input*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**Quando usarlo:** Classificazioni personalizzate, formattazione coerente, compiti specifici di dominio o quando i risultati zero-shot sono incoerenti.

### Chain of Thought

Chiedi al modello di mostrare il proprio ragionamento passo-passo. Invece di saltare direttamente alla risposta, il modello scompone il problema e lavora su ogni parte esplicitamente. Questo migliora l'accuratezza su matematica, logica e compiti di ragionamento multi-passaggio.

<img src="../../../translated_images/it/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Ragionamento passo-passo — scomporre problemi complessi in passi logici espliciti*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Il modello mostra: 15 - 8 = 7, poi 7 + 12 = 19 mele
```

**Quando usarlo:** Problemi matematici, enigmi logici, debugging, o qualsiasi compito dove mostrare il processo di ragionamento migliora accuratezza e fiducia.

### Role-Based Prompting

Imposta una persona o un ruolo per l'IA prima di porre la domanda. Questo fornisce contesto che modella il tono, la profondità e il focus della risposta. Un "architetto software" dà consigli diversi rispetto a un "sviluppatore junior" o un "auditor di sicurezza".

<img src="../../../translated_images/it/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Impostare contesto e persona — la stessa domanda ottiene risposte diverse a seconda del ruolo assegnato*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**Quando usarlo:** Revisioni del codice, tutoring, analisi specifiche di dominio, o quando hai bisogno di risposte personalizzate per un determinato livello di competenza o prospettiva.

### Modelli di Prompt

Crea prompt riutilizzabili con segnaposto variabili. Invece di scrivere un nuovo prompt ogni volta, definisci un modello una volta e inserisci valori diversi. La classe `PromptTemplate` di LangChain4j facilita questo con la sintassi `{{variable}}`.

<img src="../../../translated_images/it/prompt-templates.14bfc37d45f1a933.webp" alt="Modelli di Prompt" width="800"/>

*Prompt riutilizzabili con segnaposto variabili — un modello, molti usi*

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

**Quando usarlo:** Query ripetute con input diversi, elaborazione batch, costruzione di workflow IA riutilizzabili, o qualsiasi scenario dove la struttura del prompt rimane la stessa ma i dati cambiano.

---

Questi cinque fondamenti ti danno un solido kit per la maggior parte dei compiti di prompting. Il resto di questo modulo si basa su di essi con **otto pattern avanzati** che sfruttano il controllo del ragionamento di GPT-5.2, l'auto-valutazione e le capacità di output strutturato.

## Pattern Avanzati

Con i fondamenti coperti, passiamo agli otto pattern avanzati che rendono unico questo modulo. Non tutti i problemi richiedono lo stesso approccio. Alcune domande richiedono risposte rapide, altre un pensiero profondo. Alcune bisogno di ragionamento visibile, altre solo di risultati. Ogni pattern qui sotto è ottimizzato per uno scenario diverso — e il controllo del ragionamento di GPT-5.2 rende le differenze ancora più evidenti.

<img src="../../../translated_images/it/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Otto Pattern di Prompt Engineering" width="800"/>

*Panoramica degli otto pattern di prompt engineering e i loro casi d'uso*

<img src="../../../translated_images/it/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Controllo del Ragionamento con GPT-5.2" width="800"/>

*Il controllo del ragionamento di GPT-5.2 ti permette di specificare quanto deve pensare il modello — da risposte veloci e dirette a esplorazioni approfondite*

**Bassa Prontezza (Veloce & Focalizzato)** - Per domande semplici in cui vuoi risposte rapide e dirette. Il modello fa un ragionamento minimo - massimo 2 passi. Usalo per calcoli, ricerche, o domande dirette.

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Esplora con GitHub Copilot:** Apri [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) e chiedi:
> - "Qual è la differenza tra i pattern di prompting a bassa e alta prontezza?"
> - "Come aiutano i tag XML nei prompt a strutturare la risposta dell'IA?"
> - "Quando dovrei usare i pattern di auto-riflessione rispetto all'istruzione diretta?"

**Alta Prontezza (Profondo & Completo)** - Per problemi complessi in cui vuoi un'analisi completa. Il modello esplora a fondo e mostra un ragionamento dettagliato. Usalo per design di sistemi, decisioni architetturali o ricerche complesse.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Esecuzione del Compito (Progresso Passo-Passo)** - Per workflow multi-step. Il modello fornisce un piano iniziale, narra ogni passo mentre lavora, poi dà un riepilogo. Usalo per migrazioni, implementazioni o qualsiasi processo multi-step.

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

Il prompting Chain-of-Thought chiede esplicitamente al modello di mostrare il processo di ragionamento, migliorando l'accuratezza per compiti complessi. La suddivisione passo-passo aiuta sia gli umani che l'IA a capire la logica.

> **🤖 Prova con la Chat di [GitHub Copilot](https://github.com/features/copilot):** Chiedi di questo pattern:
> - "Come adattare il pattern di esecuzione del compito per operazioni di lunga durata?"
> - "Quali sono le best practice per strutturare i preludi strumenti nelle applicazioni di produzione?"
> - "Come posso catturare e mostrare aggiornamenti di progresso intermedi in un'interfaccia utente?"

<img src="../../../translated_images/it/task-execution-pattern.9da3967750ab5c1e.webp" alt="Pattern di Esecuzione del Compito" width="800"/>

*Flusso Plan → Esegui → Riepiloga per compiti multi-step*

**Codice Auto-Riflettente** - Per generare codice di qualità produzione. Il modello genera codice seguendo standard di produzione con gestione degli errori adeguata. Usalo quando costruisci nuove funzionalità o servizi.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/it/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciclo di Auto-Riflessione" width="800"/>

*Ciclo iterativo di miglioramento - genera, valuta, individua problemi, migliora, ripeti*

**Analisi Strutturata** - Per valutazioni coerenti. Il modello rivede il codice usando un framework fisso (correttezza, pratiche, performance, sicurezza, manutenibilità). Usalo per revisioni di codice o valutazioni di qualità.

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Prova con la Chat di [GitHub Copilot](https://github.com/features/copilot):** Chiedi dell'analisi strutturata:
> - "Come personalizzare il framework di analisi per diversi tipi di revisioni del codice?"
> - "Qual è il modo migliore per analizzare e agire su output strutturati programmaticamente?"
> - "Come assicurare coerenza nei livelli di gravità tra diverse sessioni di revisione?"

<img src="../../../translated_images/it/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Pattern di Analisi Strutturata" width="800"/>

*Framework per revisioni di codice coerenti con livelli di gravità*

**Chat Multi-Turno** - Per conversazioni che richiedono contesto. Il modello ricorda i messaggi precedenti e costruisce su di essi. Usalo per sessioni di aiuto interattive o Q&A complessi.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/it/context-memory.dff30ad9fa78832a.webp" alt="Memoria del Contesto" width="800"/>

*Come il contesto della conversazione si accumula su più turni fino a raggiungere il limite di token*

**Ragionamento Passo-passo** - Per problemi che richiedono logica visibile. Il modello mostra ragionamenti espliciti per ogni passo. Usalo per problemi matematici, enigmi logici o quando hai bisogno di capire il processo di pensiero.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/it/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Pattern Passo-passo" width="800"/>

*Scomporre problemi in passi logici espliciti*

**Output Vincolato** - Per risposte con requisiti di formato specifici. Il modello segue rigorosamente regole di formato e lunghezza. Usalo per riepiloghi o quando ti serve una struttura di output precisa.

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

<img src="../../../translated_images/it/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Pattern di Output Vincolato" width="800"/>

*Applicazione di requisiti specifici di formato, lunghezza e struttura*

## Utilizzo delle Risorse Azure Esistenti

**Verifica distribuzione:**

Assicurati che il file `.env` esista nella directory radice con credenziali Azure (creato durante il Modulo 01):
```bash
cat ../.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Avvia l'applicazione:**

> **Nota:** Se hai già avviato tutte le applicazioni usando `./start-all.sh` dal Modulo 01, questo modulo è già in esecuzione sulla porta 8083. Puoi saltare i comandi di avvio qui sotto e andare direttamente a http://localhost:8083.
**Opzione 1: Utilizzo di Spring Boot Dashboard (Consigliato per gli utenti VS Code)**

Il dev container include l'estensione Spring Boot Dashboard, che fornisce un'interfaccia visiva per gestire tutte le applicazioni Spring Boot. Puoi trovarla nella Activity Bar sul lato sinistro di VS Code (cerca l'icona di Spring Boot).

Da Spring Boot Dashboard, puoi:
- Vedere tutte le applicazioni Spring Boot disponibili nell'area di lavoro
- Avviare/interrompere le applicazioni con un solo clic
- Visualizzare i log delle applicazioni in tempo reale
- Monitorare lo stato delle applicazioni

Clicca semplicemente sul pulsante play accanto a "prompt-engineering" per avviare questo modulo, oppure avvia tutti i moduli contemporaneamente.

<img src="../../../translated_images/it/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opzione 2: Utilizzo di script shell**

Avvia tutte le applicazioni web (moduli 01-04):

**Bash:**
```bash
cd ..  # Dalla directory root
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Dalla directory root
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

Entrambi gli script caricano automaticamente le variabili d'ambiente dal file `.env` nella root e compileranno i JAR se non esistono.

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

*La dashboard principale che mostra tutti e 8 i pattern di ingegneria dei prompt con le loro caratteristiche e casi d'uso*

## Esplorare i Pattern

L'interfaccia web ti permette di sperimentare diverse strategie di prompting. Ogni pattern risolve problemi diversi - provali per vedere quando ogni approccio brilla.

> **Nota: Streaming vs Non-Streaming** — Ogni pagina del pattern offre due pulsanti: **🔴 Stream Response (Live)** e un'opzione **Non-streaming**. Lo streaming utilizza Server-Sent Events (SSE) per visualizzare i token in tempo reale mentre il modello li genera, così vedi il progresso immediatamente. L'opzione non-streaming aspetta l'intera risposta prima di mostrarla. Per prompt che innescano ragionamenti profondi (es. High Eagerness, Self-Reflecting Code), la chiamata non-streaming può richiedere molto tempo — a volte minuti — senza feedback visibile. **Usa lo streaming quando sperimenti prompt complessi** per vedere il modello all'opera ed evitare l'impressione che la richiesta sia scaduta.
>
> **Nota: Requisiti del Browser** — La funzione streaming usa la Fetch Streams API (`response.body.getReader()`) che richiede un browser completo (Chrome, Edge, Firefox, Safari). Non funziona nel Simple Browser integrato di VS Code, perché la sua webview non supporta la ReadableStream API. Se usi il Simple Browser, i pulsanti non-streaming funzioneranno normalmente — solo quelli streaming sono interessati. Apri `http://localhost:8083` in un browser esterno per un'esperienza completa.

### Low vs High Eagerness

Fai una domanda semplice come "Qual è il 15% di 200?" usando Low Eagerness. Otterrai una risposta immediata e diretta. Ora chiedi qualcosa di complesso come "Progetta una strategia di caching per un'API ad alto traffico" usando High Eagerness. Clicca su **🔴 Stream Response (Live)** e guarda il ragionamento dettagliato del modello apparire token per token. Stesso modello, stessa struttura della domanda - ma il prompt indica quanto ragionamento fare.

### Esecuzione di Task (Tool Preambles)

I workflow multi-step beneficiano di una pianificazione anticipata e di una narrazione dei progressi. Il modello delinea cosa farà, narra ogni passo e poi riassume i risultati.

### Self-Reflecting Code

Prova "Crea un servizio di validazione email". Invece di generare solo il codice e fermarsi, il modello genera, valuta secondo criteri di qualità, identifica debolezze e migliora. Vedrai iterare fino a quando il codice soddisfa gli standard di produzione.

### Analisi Strutturata

Le revisioni del codice necessitano di framework di valutazione coerenti. Il modello analizza il codice usando categorie fisse (correttezza, pratiche, prestazioni, sicurezza) con livelli di severità.

### Chat Multi-Turno

Chiedi "Cos'è Spring Boot?" e poi subito dopo "Fammi vedere un esempio". Il modello ricorda la tua prima domanda e ti fornisce un esempio specifico di Spring Boot. Senza memoria, quella seconda domanda sarebbe troppo vaga.

### Ragionamento Passo-Passo

Scegli un problema matematico e provalo sia con Ragionamento Passo-Passo che con Low Eagerness. Low eagerness ti dà solo la risposta - veloce ma opaca. Il passo-passo ti mostra ogni calcolo e decisione.

### Output Vincolato

Quando ti servono formati specifici o conteggi di parole, questo pattern impone un'aderenza rigorosa. Prova a generare un riassunto con esattamente 100 parole in formato elenco puntato.

## Cosa Stai Davvero Imparando

**Lo Sforzo di Ragionamento Cambia Tutto**

GPT-5.2 ti permette di controllare lo sforzo computazionale attraverso i tuoi prompt. Basso sforzo significa risposte rapide con esplorazione minima. Alto sforzo significa che il modello prende tempo per pensare profondamente. Stai imparando a far corrispondere lo sforzo alla complessità del compito - non sprecare tempo su domande semplici, ma non affrettare nemmeno le decisioni complesse.

**La Struttura Guida il Comportamento**

Hai notato i tag XML nei prompt? Non sono decorativi. I modelli seguono istruzioni strutturate in modo più affidabile rispetto al testo libero. Quando servono processi multi-step o logica complessa, la struttura aiuta il modello a capire dove si trova e cosa deve fare dopo.

<img src="../../../translated_images/it/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomia di un prompt ben strutturato con sezioni chiare e organizzazione in stile XML*

**Qualità Attraverso l'Auto-Valutazione**

I pattern self-reflecting funzionano rendendo espliciti i criteri di qualità. Invece di sperare che il modello "faccia giusto", gli dici esattamente cosa significa "giusto": logica corretta, gestione degli errori, prestazioni, sicurezza. Il modello può quindi valutare il proprio output e migliorare. Questo trasforma la generazione di codice da una lotteria a un processo.

**Il Contesto è Finito**

Le conversazioni multi-turn funzionano includendo la cronologia dei messaggi ad ogni richiesta. Ma c'è un limite - ogni modello ha un massimo di token. Man mano che le conversazioni crescono, serviranno strategie per mantenere il contesto rilevante senza superare quel limite. Questo modulo ti mostra come funziona la memoria; più avanti imparerai quando riassumere, quando dimenticare e quando recuperare.

## Prossimi Passi

**Prossimo Modulo:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigazione:** [← Precedente: Modulo 01 - Introduzione](../01-introduction/README.md) | [Torna al Principale](../README.md) | [Successivo: Modulo 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avvertenza**:  
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Sebbene ci impegniamo per garantire l’accuratezza, si prega di notare che le traduzioni automatiche possono contenere errori o imprecisioni. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale effettuata da un umano. Non ci assumiamo alcuna responsabilità per eventuali malintesi o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->