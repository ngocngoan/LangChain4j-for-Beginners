# Modulo 02: Ingegneria dei Prompt con GPT-5.2

## Sommario

- [Video Guida](../../../02-prompt-engineering)
- [Cosa Imparerai](../../../02-prompt-engineering)
- [Prerequisiti](../../../02-prompt-engineering)
- [Comprendere l'Ingegneria dei Prompt](../../../02-prompt-engineering)
- [Fondamenti di Ingegneria dei Prompt](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Pattern Avanzati](../../../02-prompt-engineering)
- [Uso delle Risorse Azure Esistenti](../../../02-prompt-engineering)
- [Screenshot dell'Applicazione](../../../02-prompt-engineering)
- [Esplorazione dei Pattern](../../../02-prompt-engineering)
  - [Basso vs Alto Entusiasmo](../../../02-prompt-engineering)
  - [Esecuzione di Compiti (Premesse degli Strumenti)](../../../02-prompt-engineering)
  - [Codice Auto-Riflettente](../../../02-prompt-engineering)
  - [Analisi Strutturata](../../../02-prompt-engineering)
  - [Chat Multi-Turno](../../../02-prompt-engineering)
  - [Ragionamento Passo a Passo](../../../02-prompt-engineering)
  - [Output Vincolato](../../../02-prompt-engineering)
- [Cosa Stai Veramente Imparando](../../../02-prompt-engineering)
- [Prossimi Passi](../../../02-prompt-engineering)

## Video Guida

Guarda questa sessione live che spiega come iniziare con questo modulo: [Prompt Engineering con LangChain4j - Sessione Live](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## Cosa Imparerai

<img src="../../../translated_images/it/what-youll-learn.c68269ac048503b2.webp" alt="Cosa Imparerai" width="800"/>

Nel modulo precedente, hai visto come la memoria abilita l'AI conversazionale e hai usato GitHub Models per interazioni di base. Ora ci concentreremo su come fare domande — i prompt stessi — usando GPT-5.2 di Azure OpenAI. Il modo in cui strutturi i prompt influenza drasticamente la qualità delle risposte che ricevi. Iniziamo con una revisione delle tecniche fondamentali di prompting, poi passiamo a otto pattern avanzati che sfruttano appieno le capacità di GPT-5.2.

Useremo GPT-5.2 perché introduce il controllo del ragionamento - puoi indicare al modello quanto riflettere prima di rispondere. Questo rende più evidenti le diverse strategie di prompting e ti aiuta a capire quando usare ciascun approccio. Beneficeremo anche dei limiti di utilizzo più ampi di Azure per GPT-5.2 rispetto a GitHub Models.

## Prerequisiti

- Modulo 01 completato (risorse Azure OpenAI distribuite)
- File `.env` nella directory principale con credenziali Azure (creato da `azd up` nel Modulo 01)

> **Nota:** Se non hai completato il Modulo 01, segui prima le istruzioni di distribuzione lì.

## Comprendere l'Ingegneria dei Prompt

<img src="../../../translated_images/it/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Cos'è l'Ingegneria dei Prompt?" width="800"/>

L'ingegneria dei prompt riguarda la progettazione di testi di input che ottengono costantemente i risultati di cui hai bisogno. Non si tratta solo di fare domande - ma di strutturare le richieste in modo che il modello capisca esattamente cosa vuoi e come fornirlo.

Pensalo come dare istruzioni a un collega. "Correggi il bug" è vago. "Correggi l'eccezione di puntatore nullo in UserService.java riga 45 aggiungendo un controllo null" è specifico. I modelli linguistici funzionano allo stesso modo - precisione e struttura contano.

<img src="../../../translated_images/it/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Come si inserisce LangChain4j" width="800"/>

LangChain4j fornisce l'infrastruttura — connessioni ai modelli, memoria, e tipi di messaggi — mentre i pattern di prompt sono solo testi accuratamente strutturati che invii tramite quell'infrastruttura. I blocchi fondamentali sono `SystemMessage` (che imposta il comportamento e il ruolo dell'AI) e `UserMessage` (che trasporta la tua richiesta effettiva).

## Fondamenti di Ingegneria dei Prompt

<img src="../../../translated_images/it/five-patterns-overview.160f35045ffd2a94.webp" alt="Panoramica di Cinque Pattern di Ingegneria dei Prompt" width="800"/>

Prima di immergerci nei pattern avanzati di questo modulo, rivediamo cinque tecniche fondamentali di prompting. Queste sono le basi che ogni ingegnere di prompt dovrebbe conoscere. Se hai già lavorato attraverso il [modulo Quick Start](../00-quick-start/README.md#2-prompt-patterns), li hai visti in azione — ecco il quadro concettuale dietro di essi.

### Zero-Shot Prompting

L'approccio più semplice: dare al modello un'istruzione diretta senza esempi. Il modello si basa interamente sul suo addestramento per capire ed eseguire il compito. Funziona bene per richieste semplici dove il comportamento atteso è ovvio.

<img src="../../../translated_images/it/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Istruzione diretta senza esempi — il modello deduce il compito solo dall'istruzione*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Risposta: "Positivo"
```

**Quando usarlo:** Classificazioni semplici, domande dirette, traduzioni o qualsiasi compito che il modello può gestire senza guida aggiuntiva.

### Few-Shot Prompting

Fornire esempi che mostrano il modello il pattern da seguire. Il modello impara il formato input-output previsto dai tuoi esempi e lo applica a nuovi input. Migliora drasticamente la coerenza per compiti in cui il formato desiderato non è ovvio.

<img src="../../../translated_images/it/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Apprendere dagli esempi — il modello identifica il pattern e lo applica a nuovi input*

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

**Quando usarlo:** Classificazioni personalizzate, formattazioni consistenti, compiti specifici di dominio, o quando i risultati zero-shot sono incoerenti.

### Chain of Thought

Chiedere al modello di mostrare il suo ragionamento passo dopo passo. Invece di saltare direttamente a una risposta, il modello scompone il problema e lavora su ogni parte esplicitamente. Questo migliora l’accuratezza in matematica, logica e ragionamenti multi-step.

<img src="../../../translated_images/it/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Ragionamento passo-passo — scomposizione di problemi complessi in passaggi logici espliciti*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Il modello mostra: 15 - 8 = 7, poi 7 + 12 = 19 mele
```

**Quando usarlo:** Problemi di matematica, puzzle logici, debugging, o qualsiasi compito in cui mostrare il processo di ragionamento migliora accuratezza e fiducia.

### Role-Based Prompting

Impostare una persona o ruolo per l'AI prima di fare la domanda. Questo fornisce un contesto che modella il tono, la profondità e il focus della risposta. Un "architetto software" dà consigli diversi da un "junior developer" o un "auditor di sicurezza".

<img src="../../../translated_images/it/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Impostazione di contesto e persona — la stessa domanda riceve risposte diverse a seconda del ruolo assegnato*

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

**Quando usarlo:** Review del codice, tutoring, analisi specifiche di dominio, o quando servono risposte adattate a un certo livello di competenza o prospettiva.

### Prompt Templates

Creare prompt riutilizzabili con segnaposti variabili. Invece di scrivere un prompt nuovo ogni volta, definisci un template una volta e inserisci valori diversi. La classe `PromptTemplate` di LangChain4j lo rende facile con la sintassi `{{variable}}`.

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

**Quando usarlo:** Query ripetute con input diversi, elaborazioni batch, costruzione di workflow AI riutilizzabili, o qualsiasi scenario dove la struttura del prompt resta la stessa ma cambiano i dati.

---

Questi cinque fondamentali ti danno un kit solido per la maggior parte dei compiti di prompting. Il resto di questo modulo si basa su di essi con **otto pattern avanzati** che sfruttano il controllo del ragionamento, l'auto-valutazione e le capacità di output strutturato di GPT-5.2.

## Pattern Avanzati

Con i fondamentali coperti, passiamo agli otto pattern avanzati che rendono unico questo modulo. Non tutti i problemi richiedono lo stesso approccio. Alcune domande vogliono risposte rapide, altre un pensiero profondo. Alcune richiedono ragionamenti visibili, altre solo risultati. Ogni pattern sottostante è ottimizzato per uno scenario diverso — e il controllo del ragionamento di GPT-5.2 accentua ancora di più le differenze.

<img src="../../../translated_images/it/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Otto Pattern di Prompting" width="800"/>

*Panoramica degli otto pattern di ingegneria dei prompt e i loro casi d’uso*

<img src="../../../translated_images/it/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Controllo del Ragionamento con GPT-5.2" width="800"/>

*Il controllo del ragionamento di GPT-5.2 ti permette di specificare quanto il modello deve pensare — da risposte rapide e dirette a esplorazioni profonde*

**Basso Entusiasmo (Veloce & Mirato)** - Per domande semplici dove vuoi risposte rapide e dirette. Il modello fa un ragionamento minimo - massimo 2 passaggi. Usalo per calcoli, ricerche o domande semplici.

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
> - "Qual è la differenza tra pattern di basso entusiasmo e di alto entusiasmo?"
> - "Come aiutano i tag XML nei prompt a strutturare la risposta dell'AI?"
> - "Quando dovrei usare i pattern di auto-riflessione vs istruzioni dirette?"

**Alto Entusiasmo (Profondo & Completo)** - Per problemi complessi dove vuoi un'analisi completa. Il modello esplora a fondo e mostra un ragionamento dettagliato. Usalo per design di sistemi, decisioni architetturali, o ricerche complesse.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Esecuzione di Compiti (Progresso Passo Dopo Passo)** - Per workflow multi-step. Il modello fornisce un piano upfront, narra ogni passo mentre opera, poi dà un riepilogo. Usalo per migrazioni, implementazioni o qualsiasi processo multi-step.

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

Il prompting Chain-of-Thought chiede esplicitamente al modello di mostrare il processo di ragionamento, migliorando l’accuratezza per compiti complessi. La scomposizione passo-passo aiuta sia umani che AI a capire la logica.

> **🤖 Prova con la chat di [GitHub Copilot](https://github.com/features/copilot):** Chiedi di questo pattern:
> - "Come adattare il pattern di esecuzione dei compiti per operazioni di lunga durata?"
> - "Quali sono le best practice per strutturare le premesse degli strumenti nelle applicazioni di produzione?"
> - "Come posso catturare e mostrare aggiornamenti intermedi di progresso in un’interfaccia utente?"

<img src="../../../translated_images/it/task-execution-pattern.9da3967750ab5c1e.webp" alt="Pattern di Esecuzione del Compito" width="800"/>

*Flusso Plan → Execute → Summarize per compiti multi-passaggio*

**Codice Auto-Riflettente** - Per generare codice di qualità di produzione. Il modello genera codice conforme agli standard di produzione con gestione appropriata degli errori. Usalo per costruire nuove funzionalità o servizi.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/it/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciclo di Auto-Riflessione" width="800"/>

*Ciclo iterativo di miglioramento - genera, valuta, individua problemi, migliora, ripeti*

**Analisi Strutturata** - Per valutazioni consistenti. Il modello revisiona il codice usando un framework fisso (correttezza, pratiche, prestazioni, sicurezza, manutenibilità). Usalo per code review o valutazioni di qualità.

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

> **🤖 Prova con la chat di [GitHub Copilot](https://github.com/features/copilot):** Chiedi di analisi strutturata:
> - "Come personalizzare il framework di analisi per diversi tipi di code review?"
> - "Qual è il modo migliore per interpretare e agire sull'output strutturato programmaticamente?"
> - "Come garantire livelli di severità coerenti in sessioni di revisione differenti?"

<img src="../../../translated_images/it/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Pattern di Analisi Strutturata" width="800"/>

*Framework per code review coerenti con livelli di severità*

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

*Come il contesto della conversazione si accumula su più turni fino al limite di token*

**Ragionamento Passo a Passo** - Per problemi che richiedono logica visibile. Il modello mostra ragionamenti espliciti per ogni passaggio. Usalo per problemi matematici, puzzle logici, o quando serve capire il processo di pensiero.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/it/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Pattern Passo a Passo" width="800"/>

*Scomposizione dei problemi in passaggi logici espliciti*

**Output Vincolato** - Per risposte con requisiti di formato specifici. Il modello segue rigorosamente regole di formato e lunghezza. Usalo per riepiloghi o quando serve una struttura di output precisa.

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

## Uso delle Risorse Azure Esistenti

**Verifica la distribuzione:**

Assicurati che il file `.env` esista nella directory principale con le credenziali Azure (creato durante il Modulo 01):
```bash
cat ../.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Avvia l'applicazione:**

> **Nota:** Se hai già avviato tutte le applicazioni con `./start-all.sh` dal Modulo 01, questo modulo è già in esecuzione sulla porta 8083. Puoi saltare i comandi di avvio qui sotto e andare direttamente a http://localhost:8083.

**Opzione 1: Usare Spring Boot Dashboard (Consigliato per utenti VS Code)**
Il dev container include l'estensione Spring Boot Dashboard, che fornisce un'interfaccia visiva per gestire tutte le applicazioni Spring Boot. Puoi trovarla nella Barra delle Attività sul lato sinistro di VS Code (cerca l'icona di Spring Boot).

Dal Spring Boot Dashboard, puoi:
- Vedere tutte le applicazioni Spring Boot disponibili nell'area di lavoro
- Avviare/fermare le applicazioni con un solo clic
- Visualizzare i log delle applicazioni in tempo reale
- Monitorare lo stato delle applicazioni

Basta cliccare sul pulsante play accanto a "prompt-engineering" per avviare questo modulo, oppure avviare tutti i moduli contemporaneamente.

<img src="../../../translated_images/it/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opzione 2: Uso di script shell**

Avvia tutte le applicazioni web (moduli 01-04):

**Bash:**
```bash
cd ..  # Dalla directory root
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

Entrambi gli script caricano automaticamente le variabili d'ambiente dal file `.env` nella radice e compileranno i JAR se non esistono.

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
# Oppure
cd ..; .\stop-all.ps1  # Tutti i moduli
```

## Screenshot dell'Applicazione

<img src="../../../translated_images/it/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*La schermata principale del dashboard che mostra tutti gli 8 pattern di prompt engineering con le loro caratteristiche e casi d'uso*

## Esplorando i Pattern

L'interfaccia web ti permette di sperimentare diverse strategie di prompting. Ogni pattern risolve problemi differenti - provali per vedere quando ciascun approccio funziona al meglio.

> **Nota: Streaming vs Non-Streaming** — Ogni pagina del pattern offre due pulsanti: **🔴 Stream Response (Live)** e un'opzione **Non-streaming**. Lo streaming usa Server-Sent Events (SSE) per mostrare i token in tempo reale mentre il modello li genera, così vedi subito i progressi. L'opzione non-streaming aspetta la risposta completa prima di mostrarla. Per i prompt che innescano ragionamenti profondi (ad es. High Eagerness, Self-Reflecting Code), la chiamata non-streaming può impiegare molto tempo — a volte minuti — senza feedback visibile. **Usa lo streaming durante la sperimentazione con prompt complessi** così puoi vedere il modello in azione ed evitare l'impressione che la richiesta sia scaduta.
>
> **Nota: Requisiti del Browser** — La funzione di streaming utilizza la Fetch Streams API (`response.body.getReader()`) che richiede un browser completo (Chrome, Edge, Firefox, Safari). Non funziona nel Simple Browser incorporato in VS Code, poiché la sua webview non supporta la ReadableStream API. Se usi il Simple Browser, i pulsanti non-streaming funzioneranno normalmente — solo quelli di streaming sono interessati. Apri `http://localhost:8083` in un browser esterno per un'esperienza completa.

### Bassa vs Alta Eagerness

Fai una domanda semplice come "Qual è il 15% di 200?" usando Bassa Eagerness. Otterrai una risposta immediata e diretta. Ora chiedi qualcosa di complesso come "Progetta una strategia di caching per un'API ad alto traffico" usando Alta Eagerness. Clicca su **🔴 Stream Response (Live)** e guarda il ragionamento dettagliato del modello apparire token dopo token. Stesso modello, stessa struttura di domanda - ma il prompt indica quanto deve pensare.

### Esecuzione di Compiti (Tool Preambles)

I flussi di lavoro a più passaggi beneficiano di una pianificazione anticipata e della narrazione dei progressi. Il modello illustra cosa farà, narra ogni passaggio, poi riassume i risultati.

### Codice Autoriflessivo

Prova "Crea un servizio di validazione email". Invece di generare solo il codice e fermarsi, il modello genera, valuta secondo criteri di qualità, identifica debolezze e migliora. Lo vedrai iterare fino a che il codice soddisfa gli standard di produzione.

### Analisi Strutturata

Le revisioni del codice necessitano di schemi di valutazione coerenti. Il modello analizza il codice usando categorie fisse (correttezza, pratiche, prestazioni, sicurezza) con livelli di severità.

### Chat a Turni Multipli

Chiedi "Cos'è Spring Boot?" poi subito dopo "Fammi un esempio". Il modello ricorda la tua prima domanda e ti dà un esempio specifico di Spring Boot. Senza memoria, la seconda domanda sarebbe troppo vaga.

### Ragionamento Passo-Passo

Scegli un problema matematico e provalo sia con Ragionamento Passo-Passo che con Bassa Eagerness. La bassa eagerness fornisce solo la risposta - veloce ma opaca. Il ragionamento passo-passo ti mostra ogni calcolo e decisione.

### Output Vincolato

Quando ti servono formati specifici o conteggi di parole, questo pattern impone un'aderenza rigorosa. Prova a generare un riassunto con esattamente 100 parole in formato elenco puntato.

## Cosa Stai Davvero Imparando

**Lo Sforzo di Ragionamento Cambia Tutto**

GPT-5.2 ti permette di controllare lo sforzo computazionale attraverso i tuoi prompt. Basso sforzo significa risposte rapide con esplorazione minima. Alto sforzo significa che il modello prende tempo per ragionare a fondo. Stai imparando ad abbinare lo sforzo alla complessità del compito - non perdere tempo su domande semplici, ma non affrettare decisioni complesse.

**La Struttura Guida il Comportamento**

Hai notato i tag XML nei prompt? Non sono decorazioni. I modelli seguono istruzioni strutturate in modo più affidabile rispetto al testo libero. Quando servono processi a più passaggi o logiche complesse, la struttura aiuta il modello a tenere traccia di dove si trova e cosa viene dopo.

<img src="../../../translated_images/it/prompt-structure.a77763d63f4e2f89.webp" alt="Struttura del Prompt" width="800"/>

*Anatomia di un prompt ben strutturato con sezioni chiare e organizzazione in stile XML*

**Qualità Tramite Autovalutazione**

I pattern autoriflessivi funzionano esplicitando i criteri di qualità. Invece di sperare che il modello "faccia bene", dici esattamente cosa significa "bene": logica corretta, gestione degli errori, prestazioni, sicurezza. Il modello può così valutare il proprio output e migliorare. Questo trasforma la generazione di codice da una lotteria in un processo.

**Il Contesto è Finito**

Le conversazioni a turni multipli funzionano includendo la cronologia dei messaggi con ogni richiesta. Ma c'è un limite - ogni modello ha un conteggio massimo di token. Man mano che le conversazioni crescono, serviranno strategie per mantenere il contesto rilevante senza superare quel limite. Questo modulo ti mostra come funziona la memoria; più avanti imparerai quando riassumere, quando dimenticare e quando recuperare.

## Passi Successivi

**Modulo Successivo:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigazione:** [← Precedente: Modulo 01 - Introduzione](../01-introduction/README.md) | [Torna al Principale](../README.md) | [Successivo: Modulo 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avvertenza**:  
Questo documento è stato tradotto utilizzando il servizio di traduzione AI [Co-op Translator](https://github.com/Azure/co-op-translator). Sebbene ci impegniamo per garantire l’accuratezza, si prega di considerare che le traduzioni automatiche possono contenere errori o inesattezze. Il documento originale nella sua lingua natale deve essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale effettuata da un umano. Non siamo responsabili per eventuali malintesi o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->