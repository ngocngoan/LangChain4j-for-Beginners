# Modulo 02: Ingegneria dei Prompt con GPT-5.2

## Sommario

- [Cosa Imparerai](../../../02-prompt-engineering)
- [Prerequisiti](../../../02-prompt-engineering)
- [Comprendere l'Ingegneria dei Prompt](../../../02-prompt-engineering)
- [Fondamenti di Ingegneria dei Prompt](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Schemi Avanzati](../../../02-prompt-engineering)
- [Utilizzo delle Risorse Azure Esistenti](../../../02-prompt-engineering)
- [Screenshot dell'Applicazione](../../../02-prompt-engineering)
- [Esplorare gli Schemi](../../../02-prompt-engineering)
  - [Bassa vs Alta Propensione](../../../02-prompt-engineering)
  - [Esecuzione del Compito (Preliminari degli Strumenti)](../../../02-prompt-engineering)
  - [Codice Auto-Riflessivo](../../../02-prompt-engineering)
  - [Analisi Strutturata](../../../02-prompt-engineering)
  - [Chat a Turni Multipli](../../../02-prompt-engineering)
  - [Ragionamento Passo-Passo](../../../02-prompt-engineering)
  - [Output Vincolato](../../../02-prompt-engineering)
- [Cosa Stai Davvero Imparando](../../../02-prompt-engineering)
- [Passi Successivi](../../../02-prompt-engineering)

## Cosa Imparerai

<img src="../../../translated_images/it/what-youll-learn.c68269ac048503b2.webp" alt="Cosa Imparerai" width="800"/>

Nel modulo precedente, hai visto come la memoria abilita l'AI conversazionale e hai utilizzato i Modelli GitHub per interazioni di base. Ora ci concentreremo su come formulare domande — i prompt stessi — utilizzando GPT-5.2 di Azure OpenAI. Il modo in cui strutturi i tuoi prompt influisce drasticamente sulla qualità delle risposte che ottieni. Iniziamo con una revisione delle tecniche fondamentali di prompting, per poi passare a otto schemi avanzati che sfruttano appieno le capacità di GPT-5.2.

Useremo GPT-5.2 perché introduce il controllo del ragionamento - puoi dire al modello quanto pensare prima di rispondere. Questo rende più evidenti le diverse strategie di prompting e ti aiuta a capire quando usare ogni approccio. Beneficeremo inoltre dei limiti di velocità inferiori su Azure rispetto ai Modelli GitHub per GPT-5.2.

## Prerequisiti

- Modulo 01 completato (risorse Azure OpenAI distribuite)
- File `.env` nella directory principale con credenziali Azure (creato da `azd up` nel Modulo 01)

> **Nota:** Se non hai completato il Modulo 01, segui prima le istruzioni di distribuzione indicate lì.

## Comprendere l'Ingegneria dei Prompt

<img src="../../../translated_images/it/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Cos'è l'Ingegneria dei Prompt?" width="800"/>

L'ingegneria dei prompt riguarda la progettazione del testo di input che ti consente di ottenere costantemente i risultati di cui hai bisogno. Non si tratta solo di fare domande - si tratta di strutturare le richieste in modo che il modello comprenda esattamente cosa vuoi e come fornirlo.

Pensalo come dare istruzioni a un collega. "Correggi il bug" è vago. "Correggi l'eccezione null pointer in UserService.java alla riga 45 aggiungendo un controllo null" è specifico. I modelli linguistici funzionano allo stesso modo - specificità e struttura contano.

<img src="../../../translated_images/it/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Come si Inserisce LangChain4j" width="800"/>

LangChain4j fornisce l'infrastruttura — connessioni ai modelli, memoria, e tipi di messaggi — mentre i pattern di prompt sono semplicemente testi strutturati con cura che invii tramite quell'infrastruttura. I blocchi costitutivi chiave sono `SystemMessage` (che imposta il comportamento e il ruolo dell'AI) e `UserMessage` (che porta la tua richiesta effettiva).

## Fondamenti di Ingegneria dei Prompt

<img src="../../../translated_images/it/five-patterns-overview.160f35045ffd2a94.webp" alt="Panoramica dei Cinque Pattern di Ingegneria dei Prompt" width="800"/>

Prima di addentrarci negli schemi avanzati di questo modulo, rivediamo cinque tecniche fondamentali di prompting. Questi sono i mattoni di base che ogni ingegnere di prompt dovrebbe conoscere. Se hai già lavorato con il [modulo Quick Start](../00-quick-start/README.md#2-prompt-patterns), li hai visti in azione — ecco il quadro concettuale alla base.

### Zero-Shot Prompting

L'approccio più semplice: dare al modello un'istruzione diretta senza esempi. Il modello si basa interamente sul suo training per capire ed eseguire il compito. Funziona bene per richieste semplici dove il comportamento atteso è ovvio.

<img src="../../../translated_images/it/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Istruzione diretta senza esempi — il modello deduce il compito solo dall'istruzione*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Risposta: "Positivo"
```

**Quando usarlo:** Classificazioni semplici, domande dirette, traduzioni o qualsiasi compito che il modello può gestire senza guida aggiuntiva.

### Few-Shot Prompting

Fornire esempi che dimostrano il modello di comportamento che vuoi che il modello segua. Il modello apprende il formato input-output atteso dai tuoi esempi e lo applica a nuovi input. Questo migliora drasticamente la coerenza per compiti dove il formato o comportamento desiderato non è ovvio.

<img src="../../../translated_images/it/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Apprendere dagli esempi — il modello identifica il modello e lo applica a nuovi input*

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

**Quando usarlo:** Classificazioni personalizzate, formattazioni coerenti, compiti specifici di dominio, o quando i risultati zero-shot sono inconsistenti.

### Chain of Thought

Chiedere al modello di mostrare il proprio ragionamento passo passo. Invece di saltare direttamente alla risposta, il modello scompone il problema e lavora esplicitamente su ogni parte. Questo migliora la precisione in matematica, logica e ragionamenti multi-step.

<img src="../../../translated_images/it/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Ragionamento passo-passo — scomporre problemi complessi in passaggi logici espliciti*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Il modello mostra: 15 - 8 = 7, poi 7 + 12 = 19 mele
```

**Quando usarlo:** Problemi di matematica, puzzle logici, debugging, o qualsiasi compito dove mostrare il processo di ragionamento migliora precisione e affidabilità.

### Role-Based Prompting

Imposta una persona o ruolo per l'AI prima di porre la tua domanda. Questo fornisce un contesto che modella il tono, la profondità e il focus della risposta. Un "architetto software" offre consigli diversi rispetto a un "sviluppatore junior" o un "auditor di sicurezza".

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

**Quando usarlo:** Revisioni del codice, tutoraggio, analisi di dominio specifico, o quando necessiti risposte tarate su uno specifico livello di esperienza o prospettiva.

### Prompt Templates

Crea prompt riutilizzabili con segnaposto variabili. Invece di scrivere un nuovo prompt ogni volta, definisci un template una volta e compila valori diversi. La classe `PromptTemplate` di LangChain4j rende semplice questo con la sintassi `{{variable}}`.

<img src="../../../translated_images/it/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompt riutilizzabili con segnaposto variabili — un template, molti usi*

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

**Quando usarlo:** Query ripetute con input diversi, elaborazioni batch, costruzione di workflow AI riutilizzabili, o qualsiasi scenario dove la struttura del prompt rimane la stessa ma variano i dati.

---

Questi cinque fondamenti ti forniscono un solido set di strumenti per la maggior parte dei compiti di prompting. Il resto di questo modulo costruisce su di essi con **otto schemi avanzati** che sfruttano il controllo del ragionamento, l'auto-valutazione e l'output strutturato di GPT-5.2.

## Schemi Avanzati

Con i fondamenti coperti, passiamo agli otto schemi avanzati che rendono questo modulo unico. Non tutti i problemi richiedono lo stesso approccio. Alcune domande necessitano risposte rapide, altre riflessioni profonde. Alcune vogliono ragionamenti visibili, altre solo risultati. Ogni schema qui sotto è ottimizzato per uno scenario diverso — e il controllo del ragionamento di GPT-5.2 rende le differenze ancora più evidenti.

<img src="../../../translated_images/it/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Otto Pattern di Prompting" width="800"/>

*Panoramica degli otto pattern di ingegneria dei prompt e i loro casi d'uso*

<img src="../../../translated_images/it/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Controllo del Ragionamento con GPT-5.2" width="800"/>

*Il controllo del ragionamento in GPT-5.2 permette di indicare quanto il modello deve ragionare — da risposte rapide dirette a esplorazioni approfondite*

<img src="../../../translated_images/it/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Confronto Sforzo di Ragionamento" width="800"/>

*Propensione Bassa (veloce, diretto) vs Propensione Alta (approfondito, esplorativo)*

**Propensione Bassa (Veloce & Focalizzato)** - Per domande semplici dove vuoi risposte rapide e dirette. Il modello ragiona minimo - massimo 2 passaggi. Usalo per calcoli, ricerche o domande dirette.

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
> - "Qual è la differenza tra i pattern di prompting a bassa e alta propensione?"
> - "Come aiutano i tag XML nei prompt a strutturare la risposta dell'AI?"
> - "Quando dovrei usare pattern di auto-riflessione rispetto all'istruzione diretta?"

**Propensione Alta (Profondo & Approfondito)** - Per problemi complessi dove vuoi un'analisi completa. Il modello esplora approfonditamente e mostra un ragionamento dettagliato. Usalo per progettazione di sistemi, decisioni architetturali o ricerche complesse.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Esecuzione del Compito (Progresso Passo-Passo)** - Per workflow multi-step. Il modello fornisce un piano anticipato, narra ogni passo mentre lavora, poi fornisce un riepilogo. Usalo per migrazioni, implementazioni o qualsiasi processo multi-step.

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

Il prompting Chain-of-Thought chiede esplicitamente al modello di mostrare il processo di ragionamento, migliorando la precisione per compiti complessi. La scomposizione passo-passo aiuta sia umani che AI a comprendere la logica.

> **🤖 Prova con la Chat di [GitHub Copilot](https://github.com/features/copilot):** Chiedi di questo pattern:
> - "Come adatterei il pattern di esecuzione del compito per operazioni di lunga durata?"
> - "Quali sono le best practice per strutturare i preliminari degli strumenti in applicazioni di produzione?"
> - "Come posso catturare e mostrare aggiornamenti intermedi di progresso in un'interfaccia utente?"

<img src="../../../translated_images/it/task-execution-pattern.9da3967750ab5c1e.webp" alt="Schema di Esecuzione del Compito" width="800"/>

*Flusso Plan → Execute → Summarize per compiti multi-step*

**Codice Auto-Riflettente** - Per generare codice di qualità produttiva. Il modello genera codice seguendo standard produttivi con gestione degli errori appropriata. Usalo quando costruisci nuove funzionalità o servizi.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/it/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciclo di Auto-Riflessione" width="800"/>

*Ciclo iterativo di miglioramento - genera, valuta, identifica problemi, migliora, ripeti*

**Analisi Strutturata** - Per valutazioni coerenti. Il modello esamina il codice usando un framework fisso (correttezza, pratiche, prestazioni, sicurezza, manutenibilità). Usalo per revisioni del codice o valutazioni di qualità.

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
> - "Come posso personalizzare il framework di analisi per tipi diversi di revisioni del codice?"
> - "Qual è il modo migliore per parsare e agire sull'output strutturato programmaticamente?"
> - "Come garantisco livelli di gravità coerenti tra sessioni di revisione differenti?"

<img src="../../../translated_images/it/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Schema di Analisi Strutturata" width="800"/>

*Framework per revisioni del codice coerenti con livelli di gravità*

**Chat a Turni Multipli** - Per conversazioni che necessitano di contesto. Il modello ricorda i messaggi precedenti e costruisce su di essi. Usalo per sessioni di aiuto interattive o Q&A complesse.

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

*Come il contesto della conversazione si accumula su più turni fino al limite di token*

**Ragionamento Passo-Passo** - Per problemi che richiedono logica visibile. Il modello mostra ragionamenti espliciti per ogni passo. Usalo per problemi matematici, puzzle logici, o quando vuoi comprendere il processo mentale.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/it/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Schema Passo-Passo" width="800"/>

*Scomporre problemi in passaggi logici espliciti*

**Output Vincolato** - Per risposte che richiedono formati specifici. Il modello segue rigorosamente regole di formato e lunghezza. Usalo per riassunti o quando necessiti di una struttura precisa nell'output.

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

<img src="../../../translated_images/it/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Schema di Output Vincolato" width="800"/>

*Applicare requisiti specifici di formato, lunghezza e struttura*

## Utilizzo delle Risorse Azure Esistenti

**Verifica la distribuzione:**

Assicurati che il file `.env` esista nella directory principale con le credenziali Azure (create durante il Modulo 01):
```bash
cat ../.env  # Deve mostrare AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Avvia l'applicazione:**

> **Nota:** Se hai già avviato tutte le applicazioni usando `./start-all.sh` dal Modulo 01, questo modulo è già in esecuzione sulla porta 8083. Puoi saltare i comandi di avvio qui sotto e andare direttamente su http://localhost:8083.

**Opzione 1: Usare la Spring Boot Dashboard (Raccomandato per utenti VS Code)**

Il contenitore di sviluppo include l'estensione Spring Boot Dashboard, che fornisce un'interfaccia visiva per gestire tutte le applicazioni Spring Boot. Puoi trovarla nella barra attività a sinistra di VS Code (cerca l'icona di Spring Boot).
Dal Spring Boot Dashboard, puoi:
- Vedere tutte le applicazioni Spring Boot disponibili nell'area di lavoro
- Avviare/fermare le applicazioni con un solo clic
- Visualizzare i log delle applicazioni in tempo reale
- Monitorare lo stato delle applicazioni

Clicca semplicemente il pulsante play accanto a "prompt-engineering" per avviare questo modulo, oppure avvia tutti i moduli insieme.

<img src="../../../translated_images/it/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opzione 2: Uso di script shell**

Avvia tutte le applicazioni web (moduli 01-04):

**Bash:**
```bash
cd ..  # Dalla directory principale
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Dalla directory radice
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

*Il dashboard principale che mostra tutti gli 8 pattern di prompt engineering con le loro caratteristiche e casi d'uso*

## Esplorazione dei Pattern

L'interfaccia web ti permette di sperimentare diverse strategie di prompting. Ogni pattern risolve problemi differenti - provali per vedere quando ogni approccio funziona meglio.

### Eagerness Bassa vs Alta

Fai una domanda semplice come "Qual è il 15% di 200?" usando Eagerness Bassa. Riceverai una risposta istantanea e diretta. Ora chiedi qualcosa di complesso come "Progetta una strategia di caching per un'API ad alto traffico" usando Eagerness Alta. Osserva come il modello rallenta e fornisce un ragionamento dettagliato. Stesso modello, stessa struttura della domanda - ma il prompt indica quanto deve riflettere.

<img src="../../../translated_images/it/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Calcolo rapido con ragionamento minimo*

<img src="../../../translated_images/it/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Strategia di caching completa (2.8MB)*

### Esecuzione di Compiti (Preamboli Strumento)

I workflow multi-step traggono vantaggio dalla pianificazione anticipata e dalla narrazione del progresso. Il modello delinea cosa farà, narra ogni passaggio, poi riassume i risultati.

<img src="../../../translated_images/it/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Creazione di un endpoint REST con narrazione passo-passo (3.9MB)*

### Codice Auto-Riflettente

Prova "Crea un servizio di validazione email". Invece di generare solo codice e fermarsi, il modello genera, valuta rispetto a criteri di qualità, individua debolezze e migliora. Vedrai che itera finché il codice non soddisfa gli standard di produzione.

<img src="../../../translated_images/it/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Servizio completo di validazione email (5.2MB)*

### Analisi Strutturata

Le revisioni del codice necessitano di framework di valutazione coerenti. Il modello analizza il codice usando categorie fisse (correttezza, pratiche, prestazioni, sicurezza) con livelli di severità.

<img src="../../../translated_images/it/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Revisione del codice basata su framework*

### Chat Multi-Turno

Chiedi "Cos'è Spring Boot?" e subito dopo "Mostrami un esempio". Il modello ricorda la tua prima domanda e ti fornisce un esempio specifico di Spring Boot. Senza memoria, quella seconda domanda sarebbe troppo vaga.

<img src="../../../translated_images/it/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Conservazione del contesto tra le domande*

### Ragionamento Passo-Passo

Scegli un problema matematico e provalo sia con il ragionamento passo-passo che con Eagerness Bassa. Eagerness bassa ti dà solo la risposta - veloce ma opaca. Il ragionamento passo-passo mostra ogni calcolo e decisione.

<img src="../../../translated_images/it/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Problema matematico con passaggi espliciti*

### Uscita Vincolata

Quando hai bisogno di formati specifici o di un conteggio esatto di parole, questo pattern impone un'aderenza rigorosa. Prova a generare un riassunto con esattamente 100 parole in formato elenco puntato.

<img src="../../../translated_images/it/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Riassunto di machine learning con controllo del formato*

## Cosa Stai Davvero Imparando

**Lo Sforzo di Ragionamento Cambia Tutto**

GPT-5.2 ti permette di controllare lo sforzo computazionale attraverso i tuoi prompt. Poco sforzo significa risposte rapide con esplorazione minima. Molto sforzo significa che il modello prende tempo per riflettere a fondo. Stai imparando a far corrispondere lo sforzo alla complessità del compito - non sprecare tempo su domande semplici, ma non affrettare neanche decisioni complesse.

**La Struttura Guida il Comportamento**

Hai notato i tag XML nei prompt? Non sono decorativi. I modelli seguono istruzioni strutturate più affidabilmente rispetto a testo libero. Quando hai bisogno di processi multi-step o logica complessa, la struttura aiuta il modello a capire dove si trova e cosa deve fare dopo.

<img src="../../../translated_images/it/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomia di un prompt ben strutturato con sezioni chiare e organizzazione in stile XML*

**Qualità Attraverso l'Auto-Valutazione**

I pattern auto-riflettenti funzionano rendendo espliciti i criteri di qualità. Invece di sperare che il modello "faccia bene", gli dici esattamente cosa significa "bene": logica corretta, gestione degli errori, prestazioni, sicurezza. Il modello può così valutare la propria uscita e migliorare. Questo trasforma la generazione di codice da una lotteria in un processo.

**Il Contesto È Finito**

Le conversazioni multi-turno funzionano includendo la storia dei messaggi in ogni richiesta. Ma c'è un limite - ogni modello ha un conteggio massimo di token. Man mano che le conversazioni crescono, avrai bisogno di strategie per mantenere il contesto rilevante senza superare quel limite. Questo modulo mostra come funziona la memoria; più avanti imparerai quando riassumere, quando dimenticare e quando recuperare.

## Prossimi Passi

**Modulo Successivo:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigazione:** [← Precedente: Modulo 01 - Introduzione](../01-introduction/README.md) | [Torna al Principale](../README.md) | [Successivo: Modulo 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:  
Questo documento è stato tradotto utilizzando il servizio di traduzione AI [Co-op Translator](https://github.com/Azure/co-op-translator). Pur impegnandoci per garantire l’accuratezza, si prega di notare che le traduzioni automatiche possono contenere errori o inesattezze. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale umana. Non ci assumiamo alcuna responsabilità per eventuali malintesi o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->