# Modulo 02: Prompt Engineering con GPT-5.2

## Indice

- [Cosa Imparerai](../../../02-prompt-engineering)
- [Prerequisiti](../../../02-prompt-engineering)
- [Comprendere il Prompt Engineering](../../../02-prompt-engineering)
- [Fondamenti di Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Modelli Avanzati](../../../02-prompt-engineering)
- [Utilizzo delle Risorse Azure Esistenti](../../../02-prompt-engineering)
- [Screenshot dell'Applicazione](../../../02-prompt-engineering)
- [Esplorare i Modelli](../../../02-prompt-engineering)
  - [Bassa vs Alta Propensione](../../../02-prompt-engineering)
  - [Esecuzione del Compito (Prefissi per Strumenti)](../../../02-prompt-engineering)
  - [Codice Auto-Riflettente](../../../02-prompt-engineering)
  - [Analisi Strutturata](../../../02-prompt-engineering)
  - [Chat Multi-Turno](../../../02-prompt-engineering)
  - [Ragionamento Passo per Passo](../../../02-prompt-engineering)
  - [Output Vincolato](../../../02-prompt-engineering)
- [Cosa Stai Davvero Imparando](../../../02-prompt-engineering)
- [Prossimi Passi](../../../02-prompt-engineering)

## Cosa Imparerai

<img src="../../../translated_images/it/what-youll-learn.c68269ac048503b2.webp" alt="Cosa Imparerai" width="800"/>

Nel modulo precedente, hai visto come la memoria abilita l'IA conversazionale e hai utilizzato i Modelli GitHub per interazioni di base. Ora ci concentreremo su come porre domande — i prompt stessi — utilizzando GPT-5.2 di Azure OpenAI. Il modo in cui strutturi i tuoi prompt influisce significativamente sulla qualità delle risposte che ottieni. Iniziamo con una panoramica delle tecniche fondamentali di prompting, per poi passare a otto modelli avanzati che sfruttano appieno le capacità di GPT-5.2.

Useremo GPT-5.2 perché introduce il controllo del ragionamento - puoi indicare al modello quanto pensiero deve fare prima di rispondere. Ciò rende più evidenti le diverse strategie di prompting e ti aiuta a comprendere quando usare ciascun approccio. Beneficeremo inoltre di limiti di velocità meno restrittivi su Azure per GPT-5.2 rispetto ai Modelli GitHub.

## Prerequisiti

- Completato il Modulo 01 (risorse Azure OpenAI distribuite)
- File `.env` nella directory principale con le credenziali Azure (creato da `azd up` nel Modulo 01)

> **Nota:** Se non hai completato il Modulo 01, segui prima le istruzioni di distribuzione lì.

## Comprendere il Prompt Engineering

<img src="../../../translated_images/it/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Cos'è il Prompt Engineering?" width="800"/>

Il prompt engineering riguarda la progettazione di testi di input che ti permettono di ottenere costantemente i risultati di cui hai bisogno. Non si tratta solo di fare domande - ma di strutturare le richieste in modo che il modello capisca esattamente cosa vuoi e come fornirlo.

Pensalo come dare istruzioni a un collega. "Correggi il bug" è vago. "Correggi l'eccezione null pointer in UserService.java riga 45 aggiungendo un controllo null" è specifico. I modelli linguistici funzionano allo stesso modo - la specificità e la struttura sono importanti.

<img src="../../../translated_images/it/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Come si integra LangChain4j" width="800"/>

LangChain4j fornisce l'infrastruttura — connessioni ai modelli, memoria e tipi di messaggi — mentre i modelli di prompt sono semplicemente testi strutturati da inviare attraverso quell'infrastruttura. I componenti chiave sono `SystemMessage` (che imposta il comportamento e il ruolo dell'IA) e `UserMessage` (che trasporta la tua richiesta effettiva).

## Fondamenti di Prompt Engineering

<img src="../../../translated_images/it/five-patterns-overview.160f35045ffd2a94.webp" alt="Panoramica Cinque Modelli di Prompt Engineering" width="800"/>

Prima di immergerci nei modelli avanzati di questo modulo, rivediamo cinque tecniche fondamentali di prompting. Sono i mattoni su cui ogni prompt engineer dovrebbe costruire. Se hai già lavorato con il [modulo Quick Start](../00-quick-start/README.md#2-prompt-patterns), li hai visti in azione — ecco il quadro concettuale dietro di essi.

### Zero-Shot Prompting

L'approccio più semplice: dai al modello un'istruzione diretta senza esempi. Il modello si affida interamente al suo addestramento per comprendere ed eseguire il compito. Funziona bene per richieste semplici in cui il comportamento atteso è ovvio.

<img src="../../../translated_images/it/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Istruzione diretta senza esempi — il modello inferisce il compito dalla sola istruzione*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Risposta: "Positivo"
```

**Quando usarlo:** Classificazioni semplici, domande dirette, traduzioni o qualsiasi compito che il modello può gestire senza ulteriori indicazioni.

### Few-Shot Prompting

Fornisci esempi che dimostrano il modello che vuoi far seguire. Il modello apprende il formato input-output previsto dai tuoi esempi e lo applica a nuovi input. Questo migliora drasticamente la coerenza in compiti in cui il formato o il comportamento desiderati non sono evidenti.

<img src="../../../translated_images/it/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Apprendimento da esempi — il modello identifica il modello e lo applica a nuovi input*

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

Chiedi al modello di mostrare il suo ragionamento passo dopo passo. Invece di saltare subito a una risposta, il modello scompone il problema e lavora su ogni parte esplicitamente. Migliora l'accuratezza in matematica, logica e ragionamenti multi-step.

<img src="../../../translated_images/it/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Ragionamento passo passo — scomposizione di problemi complessi in passi logici espliciti*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Il modello mostra: 15 - 8 = 7, poi 7 + 12 = 19 mele
```

**Quando usarlo:** Problemi di matematica, enigmi logici, debugging o qualsiasi compito in cui mostrare il processo di ragionamento migliora accuratezza e fiducia.

### Role-Based Prompting

Imposta una persona o un ruolo per l'IA prima di porre la domanda. Questo fornisce un contesto che modella il tono, la profondità e il focus della risposta. Un "architetto software" dà consigli diversi rispetto a un "junior developer" o un "auditor di sicurezza".

<img src="../../../translated_images/it/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Impostare contesto e persona — la stessa domanda riceve risposte diverse a seconda del ruolo assegnato*

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

**Quando usarlo:** Revisioni del codice, tutoraggio, analisi specifiche di dominio, o quando servono risposte adattate a un livello di expertise o prospettiva particolare.

### Prompt Templates

Crea prompt riutilizzabili con segnaposti variabili. Invece di scrivere un prompt nuovo ogni volta, definisci un template una volta sola e riempi valori diversi. La classe `PromptTemplate` di LangChain4j rende facile questo con la sintassi `{{variable}}`.

<img src="../../../translated_images/it/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompt riutilizzabili con segnaposti variabili — un template, molti usi*

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

**Quando usarlo:** Query ripetute con input diversi, elaborazione a batch, creazione di workflow AI riutilizzabili, o qualsiasi scenario in cui la struttura del prompt resta la stessa ma i dati cambiano.

---

Questi cinque fondamenti forniscono un set solido per la maggior parte dei compiti di prompting. Il resto di questo modulo si basa su di essi con **otto modelli avanzati** che sfruttano il controllo del ragionamento di GPT-5.2, l'auto-valutazione e l'output strutturato.

## Modelli Avanzati

Con i fondamenti coperti, passiamo agli otto modelli avanzati che rendono questo modulo unico. Non tutti i problemi richiedono lo stesso approccio. Alcune domande hanno bisogno di risposte rapide, altre di pensiero profondo. Alcune di ragionamento visibile, altre solo di risultati. Ogni modello qui sotto è ottimizzato per uno scenario diverso — e il controllo del ragionamento di GPT-5.2 rende le differenze ancora più evidenti.

<img src="../../../translated_images/it/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Otto Modelli di Prompting" width="800"/>

*Panoramica degli otto modelli di prompt engineering e loro casi d’uso*

<img src="../../../translated_images/it/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Controllo del Ragionamento con GPT-5.2" width="800"/>

*Il controllo del ragionamento di GPT-5.2 ti permette di specificare quanto il modello deve ragionare — da risposte rapide e dirette a esplorazioni profonde*

**Bassa Propensione (Veloce & Mirato)** - Per domande semplici dove vuoi risposte rapide e dirette. Il modello fa un ragionamento minimo - massimo 2 passi. Usalo per calcoli, ricerche o domande lineari.

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
> - "Qual è la differenza tra i modelli di prompting a bassa e alta propensione?"
> - "Come aiutano i tag XML nei prompt a strutturare la risposta dell’IA?"
> - "Quando dovrei usare i modelli di auto-riflessione invece delle istruzioni dirette?"

**Alta Propensione (Profondo & Approfondito)** - Per problemi complessi dove vuoi un’analisi completa. Il modello esplora a fondo e mostra ragionamenti dettagliati. Usalo per progettazione di sistemi, decisioni di architettura o ricerche complesse.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Esecuzione del Compito (Progresso Passo-Passo)** - Per flussi di lavoro multipasso. Il modello fornisce un piano iniziale, narra ogni passo mentre lavora, quindi dà un riepilogo. Usalo per migrazioni, implementazioni o qualsiasi processo multipasso.

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

Il prompting Chain-of-Thought chiede esplicitamente al modello di mostrare il processo di ragionamento, migliorando l'accuratezza per compiti complessi. La scomposizione passo per passo aiuta sia gli umani che l’IA a comprendere la logica.

> **🤖 Prova con la chat [GitHub Copilot](https://github.com/features/copilot):** Chiedi di questo modello:
> - "Come potrei adattare il modello di esecuzione dei compiti per operazioni a lunga durata?"
> - "Quali sono le migliori pratiche per strutturare i prefissi degli strumenti in applicazioni di produzione?"
> - "Come posso catturare e mostrare aggiornamenti di progresso intermedi in un’interfaccia utente?"

<img src="../../../translated_images/it/task-execution-pattern.9da3967750ab5c1e.webp" alt="Modello di Esecuzione dei Compiti" width="800"/>

*Flusso di lavoro Pianifica → Esegui → Riassumi per compiti multipasso*

**Codice Auto-Riflettente** - Per generare codice di qualità da produzione. Il modello genera codice seguendo standard produttivi con una gestione degli errori adeguata. Usalo per sviluppare nuove funzionalità o servizi.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/it/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciclo di Auto-Riflessione" width="800"/>

*Ciclo iterativo di miglioramento - genera, valuta, identifica problemi, migliora, ripeti*

**Analisi Strutturata** - Per valutazioni consistenti. Il modello rivede il codice usando un framework fisso (correttezza, pratiche, performance, sicurezza, manutenibilità). Usalo per revisioni di codice o valutazioni di qualità.

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

> **🤖 Prova con la chat [GitHub Copilot](https://github.com/features/copilot):** Chiedi di analisi strutturata:
> - "Come posso personalizzare il framework di analisi per tipi diversi di review?"
> - "Qual è il modo migliore per analizzare e agire programmaticamente su output strutturati?"
> - "Come garantisco livelli di severità coerenti in diverse sessioni di revisione?"

<img src="../../../translated_images/it/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Modello di Analisi Strutturata" width="800"/>

*Framework per revisioni di codice coerenti con livelli di severità*

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

<img src="../../../translated_images/it/context-memory.dff30ad9fa78832a.webp" alt="Memoria di Contesto" width="800"/>

*Come il contesto della conversazione si accumula su più turni fino a raggiungere il limite di token*

**Ragionamento Passo per Passo** - Per problemi che richiedono logica visibile. Il modello mostra ragionamenti espliciti per ogni passo. Usalo per problemi di matematica, enigmi logici o quando serve capire il processo di pensiero.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/it/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Modello Passo per Passo" width="800"/>

*Scomposizione dei problemi in passaggi logici espliciti*

**Output Vincolato** - Per risposte con requisiti specifici di formato. Il modello segue rigorosamente regole di formato e lunghezza. Usalo per riassunti o quando serve un output dalla struttura precisa.

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

<img src="../../../translated_images/it/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Modello Output Vincolato" width="800"/>

*Applicazione rigorosa di requisiti specifici di formato, lunghezza e struttura*

## Utilizzo delle Risorse Azure Esistenti

**Verifica della distribuzione:**

Assicurati che il file `.env` sia presente nella directory principale con le credenziali Azure (creato durante il Modulo 01):
```bash
cat ../.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Avvia l’applicazione:**

> **Nota:** Se hai già avviato tutte le applicazioni usando `./start-all.sh` dal Modulo 01, questo modulo è già in esecuzione sulla porta 8083. Puoi saltare i comandi di avvio qui sotto e andare direttamente a http://localhost:8083.

**Opzione 1: Utilizzo della Spring Boot Dashboard (Consigliata per utenti VS Code)**

Il contenitore di sviluppo include l’estensione Spring Boot Dashboard, che fornisce un’interfaccia visuale per gestire tutte le applicazioni Spring Boot. La trovi nella Barra delle Attività sul lato sinistro di VS Code (cerca l’icona di Spring Boot).

Dalla Spring Boot Dashboard puoi:
- Vedere tutte le applicazioni Spring Boot disponibili nell’area di lavoro
- Avviare/arrestare le applicazioni con un solo clic
- Visualizzare i log delle applicazioni in tempo reale
- Monitorare lo stato dell’applicazione
Basta cliccare il pulsante di riproduzione accanto a "prompt-engineering" per avviare questo modulo, oppure avviare tutti i moduli contemporaneamente.

<img src="../../../translated_images/it/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opzione 2: Usare script shell**

Avvia tutte le applicazioni web (moduli 01-04):

**Bash:**
```bash
cd ..  # Dalla directory principale
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

Entrambi gli script caricano automaticamente le variabili d'ambiente dal file `.env` alla radice e compileranno i JAR se non esistono.

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

**Per arrestare:**

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

*La dashboard principale che mostra tutti e 8 i pattern di prompt engineering con le loro caratteristiche e casi d'uso*

## Esplorando i Pattern

L'interfaccia web ti permette di sperimentare con diverse strategie di prompting. Ogni pattern risolve problemi differenti - provane alcuni per vedere quando ogni approccio funziona meglio.

### Bassa vs Alta Voglia

Fai una domanda semplice come "Qual è il 15% di 200?" usando Bassa Voglia. Otterrai una risposta immediata e diretta. Ora chiedi qualcosa di complesso come "Progetta una strategia di caching per un'API ad alto traffico" usando Alta Voglia. Guarda come il modello rallenta e fornisce un ragionamento dettagliato. Stesso modello, stessa struttura della domanda - ma il prompt gli dice quanto deve riflettere.

<img src="../../../translated_images/it/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Calcolo veloce con ragionamento minimo*

<img src="../../../translated_images/it/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Strategia di caching completa (2.8MB)*

### Esecuzione di Compiti (Preamboli per Strumenti)

I flussi di lavoro multipasso beneficiano di una pianificazione anticipata e di una narrazione del progresso. Il modello descrive cosa farà, narra ogni passo, poi riassume i risultati.

<img src="../../../translated_images/it/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Creazione di un endpoint REST con narrazione passo dopo passo (3.9MB)*

### Codice Auto-Riflettente

Prova "Crea un servizio di validazione email". Invece di generare solo il codice e fermarsi, il modello genera, valuta secondo criteri di qualità, identifica punti deboli e migliora. Vedrai che itera finché il codice non soddisfa gli standard di produzione.

<img src="../../../translated_images/it/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Servizio completo di validazione email (5.2MB)*

### Analisi Strutturata

Le revisioni del codice necessitano di schemi di valutazione coerenti. Il modello analizza il codice utilizzando categorie fisse (correttezza, pratiche, prestazioni, sicurezza) con livelli di severità.

<img src="../../../translated_images/it/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Revisione del codice basata su framework*

### Chat Multi-Turno

Chiedi "Cos'è Spring Boot?" poi subito dopo "Mostrami un esempio". Il modello ricorda la tua prima domanda e ti fornisce un esempio specifico di Spring Boot. Senza memoria, la seconda domanda sarebbe troppo vaga.

<img src="../../../translated_images/it/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Preservazione del contesto attraverso le domande*

### Ragionamento Passo-Passo

Scegli un problema di matematica e provalo sia con il Ragionamento Passo-Passo sia con Bassa Voglia. La bassa voglia ti dà solo la risposta - veloce ma opaca. Il passo-passo ti mostra ogni calcolo e decisione.

<img src="../../../translated_images/it/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Problema matematico con passaggi espliciti*

### Output Vincolato

Quando hai bisogno di formati specifici o conteggi di parole, questo pattern impone un’adesione rigorosa. Prova a generare un riassunto con esattamente 100 parole in formato elenco puntato.

<img src="../../../translated_images/it/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Riassunto di machine learning con controllo di formato*

## Cosa Stai Davvero Imparando

**Lo Sforzo di Ragionamento Cambia Tutto**

GPT-5.2 ti permette di controllare lo sforzo computazionale tramite i tuoi prompt. Poco sforzo significa risposte rapide con esplorazione minima. Molto sforzo significa che il modello prende tempo per riflettere profondamente. Stai imparando a modulare lo sforzo in base alla complessità del compito - non perdere tempo su domande semplici, ma non affrettarti su decisioni complesse.

**La Struttura Guida il Comportamento**

Hai notato i tag XML nei prompt? Non sono decorativi. I modelli seguono istruzioni strutturate in modo più affidabile rispetto al testo libero. Quando serve un processo multipasso o una logica complessa, la struttura aiuta il modello a capire dove si trova e cosa viene dopo.

<img src="../../../translated_images/it/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomia di un prompt ben strutturato con sezioni chiare e organizzazione in stile XML*

**Qualità Attraverso l’Auto-Valutazione**

I pattern auto-riflettenti funzionano rendendo espliciti i criteri di qualità. Invece di sperare che il modello "faccia bene", gli dici esattamente cosa significa "bene": logica corretta, gestione degli errori, prestazioni, sicurezza. Il modello può quindi valutare il proprio output e migliorare. Questo trasforma la generazione di codice da una lotteria a un processo.

**Il Contesto È Finito**

Le conversazioni multi-turno funzionano includendo la cronologia dei messaggi a ogni richiesta. Ma c’è un limite - ogni modello ha un massimo di token. Quando le conversazioni crescono, dovrai usare strategie per mantenere il contesto rilevante senza superare il limite. Questo modulo ti mostra come funziona la memoria; più avanti imparerai quando riassumere, quando dimenticare e quando recuperare.

## Prossimi Passi

**Prossimo Modulo:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigazione:** [← Precedente: Modulo 01 - Introduzione](../01-introduction/README.md) | [Torna al Principale](../README.md) | [Successivo: Modulo 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Pur impegnandoci per garantire l’accuratezza, si prega di tenere presente che le traduzioni automatizzate possono contenere errori o imprecisioni. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda la traduzione professionale da parte di un traduttore umano. Non ci assumiamo alcuna responsabilità per eventuali malintesi o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->