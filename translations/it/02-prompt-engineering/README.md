# Modulo 02: Ingegneria dei Prompt con GPT-5.2

## Sommario

- [Video Dimostrativo](../../../02-prompt-engineering)
- [Cosa Imparerai](../../../02-prompt-engineering)
- [Prerequisiti](../../../02-prompt-engineering)
- [Comprendere l'Ingegneria dei Prompt](../../../02-prompt-engineering)
- [Fondamenti di Ingegneria dei Prompt](../../../02-prompt-engineering)
  - [Prompt Zero-Shot](../../../02-prompt-engineering)
  - [Prompt Few-Shot](../../../02-prompt-engineering)
  - [Catena di Pensiero](../../../02-prompt-engineering)
  - [Prompt Basati sul Ruolo](../../../02-prompt-engineering)
  - [Template per Prompt](../../../02-prompt-engineering)
- [Modelli Avanzati](../../../02-prompt-engineering)
- [Esegui l'Applicazione](../../../02-prompt-engineering)
- [Screenshot dell'Applicazione](../../../02-prompt-engineering)
- [Esplorando i Modelli](../../../02-prompt-engineering)
  - [Bassa vs Alta Motivazione](../../../02-prompt-engineering)
  - [Esecuzione di Compiti (Introduzioni agli Strumenti)](../../../02-prompt-engineering)
  - [Codice Auto-Riflessivo](../../../02-prompt-engineering)
  - [Analisi Strutturata](../../../02-prompt-engineering)
  - [Chat Multi-Turno](../../../02-prompt-engineering)
  - [Ragionamento Passo-Passo](../../../02-prompt-engineering)
  - [Output Vincolato](../../../02-prompt-engineering)
- [Cosa Stai Veramente Imparando](../../../02-prompt-engineering)
- [Prossimi Passi](../../../02-prompt-engineering)

## Video Dimostrativo

Guarda questa sessione live che spiega come iniziare con questo modulo:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering con LangChain4j - Sessione Live" width="800"/></a>

## Cosa Imparerai

Il diagramma seguente offre una panoramica degli argomenti chiave e delle competenze che svilupperai in questo modulo — dalle tecniche di raffinamento dei prompt al workflow passo-passo che seguirai.

<img src="../../../translated_images/it/what-youll-learn.c68269ac048503b2.webp" alt="Cosa Imparerai" width="800"/>

Nei moduli precedenti, hai esplorato interazioni base di LangChain4j con i modelli GitHub e visto come la memoria abilita l’IA conversazionale con Azure OpenAI. Ora ci concentreremo su come porre domande — i prompt stessi — utilizzando GPT-5.2 di Azure OpenAI. Il modo in cui strutturi i tuoi prompt influisce notevolmente sulla qualità delle risposte ottenute. Iniziamo con una revisione delle tecniche fondamentali di prompting, poi passeremo a otto modelli avanzati che sfruttano appieno le capacità di GPT-5.2.

Useremo GPT-5.2 perché introduce il controllo del ragionamento - puoi indicare al modello quanto riflettere prima di rispondere. Questo rende più evidenti le diverse strategie di prompting e aiuta a capire quando usare ciascun approccio. Beneficeremo anche di limiti di frequenza meno restrittivi su GPT-5.2 rispetto ai modelli GitHub.

## Prerequisiti

- Modulo 01 completato (risorse Azure OpenAI distribuite)
- File `.env` nella directory principale con credenziali Azure (creato da `azd up` nel Modulo 01)

> **Nota:** Se non hai completato il Modulo 01, segui prima le istruzioni di distribuzione lì.

## Comprendere l'Ingegneria dei Prompt

Alla base, l’ingegneria dei prompt è la differenza tra istruzioni vaghe e precise, come illustra il confronto qui sotto.

<img src="../../../translated_images/it/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Cos'è l'Ingegneria dei Prompt?" width="800"/>

L’ingegneria dei prompt consiste nel progettare un testo di input che ottenga costantemente i risultati desiderati. Non si tratta solo di fare domande - ma di strutturare le richieste affinché il modello capisca esattamente cosa vuoi e come fornire la risposta.

Pensalo come dare istruzioni a un collega. "Correggi l’errore" è vago. "Correggi la eccezione null pointer in UserService.java alla linea 45 aggiungendo un controllo null" è specifico. I modelli linguistici funzionano allo stesso modo - specificità e struttura sono importanti.

Il diagramma seguente mostra come LangChain4j si inserisce in questo contesto — collegando i tuoi modelli di prompt al modello attraverso i costrutti SystemMessage e UserMessage.

<img src="../../../translated_images/it/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Come si integra LangChain4j" width="800"/>

LangChain4j fornisce l’infrastruttura — connessioni ai modelli, memoria e tipi di messaggi — mentre i modelli di prompt sono semplicemente testi accuratamente strutturati che invii attraverso questa infrastruttura. I principali costrutti sono `SystemMessage` (che imposta il comportamento e il ruolo dell’IA) e `UserMessage` (che contiene la tua richiesta vera e propria).

## Fondamenti di Ingegneria dei Prompt

Le cinque tecniche principali mostrate qui sotto costituiscono la base per un’ingegneria dei prompt efficace. Ognuna affronta un aspetto diverso della comunicazione con i modelli linguistici.

<img src="../../../translated_images/it/five-patterns-overview.160f35045ffd2a94.webp" alt="Panoramica di Cinque Modelli di Ingegneria dei Prompt" width="800"/>

Prima di immergerci nei modelli avanzati di questo modulo, rivediamo cinque tecniche fondamentali di prompting. Questi sono i mattoni che ogni ingegnere di prompt dovrebbe conoscere. Se hai già seguito il [modulo Quick Start](../00-quick-start/README.md#2-prompt-patterns), li hai già visti in azione — ecco il quadro concettuale alla base.

### Prompt Zero-Shot

L’approccio più semplice: dare al modello un’istruzione diretta senza esempi. Il modello si affida interamente al proprio addestramento per comprendere ed eseguire il compito. Funziona bene per richieste semplici dove il comportamento atteso è chiaro.

<img src="../../../translated_images/it/zero-shot-prompting.7abc24228be84e6c.webp" alt="Prompt Zero-Shot" width="800"/>

*Istruzione diretta senza esempi — il modello deduce il compito dall’istruzione stessa*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Risposta: "Positivo"
```

**Quando usarlo:** Classificazioni semplici, domande dirette, traduzioni o qualsiasi compito che il modello può gestire senza ulteriori indicazioni.

### Prompt Few-Shot

Fornisci esempi che dimostrano il modello da seguire. Il modello apprende il formato input-output atteso dai tuoi esempi e lo applica ai nuovi input. Questo migliora notevolmente la coerenza per compiti dove il formato o comportamento desiderato non è ovvio.

<img src="../../../translated_images/it/few-shot-prompting.9d9eace1da88989a.webp" alt="Prompt Few-Shot" width="800"/>

*Apprendimento dagli esempi — il modello identifica il modello e lo applica a nuovi input*

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

**Quando usarlo:** Classificazioni personalizzate, formattazioni coerenti, compiti specifici di dominio, o quando i risultati zero-shot sono incoerenti.

### Catena di Pensiero

Chiedi al modello di mostrare il ragionamento passo-passo. Invece di saltare direttamente alla risposta, il modello scompone il problema e lavora esplicitamente su ogni parte. Questo migliora l’accuratezza in matematica, logica e ragionamenti multi-fase.

<img src="../../../translated_images/it/chain-of-thought.5cff6630e2657e2a.webp" alt="Prompt Catena di Pensiero" width="800"/>

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

**Quando usarlo:** Problemi matematici, puzzle logici, debug o qualsiasi compito dove mostrare il processo di ragionamento migliora accuratezza e fiducia.

### Prompt Basati sul Ruolo

Imposta una persona o ruolo per l’IA prima di porre la domanda. Questo fornisce un contesto che modella il tono, la profondità e il focus della risposta. Un "architetto software" dà consigli diversi rispetto a un "sviluppatore junior" o a un "auditor di sicurezza".

<img src="../../../translated_images/it/role-based-prompting.a806e1a73de6e3a4.webp" alt="Prompt Basati sul Ruolo" width="800"/>

*Impostare contesto e persona — stessa domanda, risposte diverse a seconda del ruolo assegnato*

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

**Quando usarlo:** Revisioni di codice, tutoraggio, analisi specifiche di dominio, o quando servono risposte personalizzate a un livello di esperienza o prospettiva precisa.

### Template per Prompt

Crea prompt riutilizzabili con segnaposto variabili. Invece di scrivere un nuovo prompt ogni volta, definisci un template una volta e inserisci valori diversi. La classe `PromptTemplate` di LangChain4j rende facile questo con la sintassi `{{variabile}}`.

<img src="../../../translated_images/it/prompt-templates.14bfc37d45f1a933.webp" alt="Template per Prompt" width="800"/>

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

**Quando usarlo:** Query ripetute con input diversi, elaborazioni batch, costruzione di workflow AI riutilizzabili, o qualsiasi scenario in cui la struttura del prompt resta la stessa ma cambiano i dati.

---

Questi cinque fondamenti ti danno una solida cassetta degli attrezzi per la maggior parte dei compiti di prompting. Il resto di questo modulo si basa su di essi con **otto modelli avanzati** che sfruttano il controllo del ragionamento, l’auto-valutazione e le capacità di output strutturato di GPT-5.2.

## Modelli Avanzati

Con i fondamenti coperti, passiamo agli otto modelli avanzati che rendono questo modulo unico. Non tutti i problemi richiedono lo stesso approccio. Alcune domande richiedono risposte rapide, altre un ragionamento profondo. Alcuni hanno bisogno di ragionamenti visibili, altri solo dei risultati. Ciascun modello qui sotto è ottimizzato per uno scenario diverso — e il controllo del ragionamento di GPT-5.2 rende queste differenze ancora più marcate.

<img src="../../../translated_images/it/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Otto Modelli di Prompting" width="800"/>

*Panoramica degli otto modelli di ingegneria dei prompt e i loro casi d’uso*

GPT-5.2 aggiunge un’altra dimensione a questi modelli: *il controllo del ragionamento*. Il cursore qui sotto mostra come puoi regolare lo sforzo di pensiero del modello — da risposte rapide e dirette ad analisi profonde e dettagliate.

<img src="../../../translated_images/it/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Controllo del Ragionamento con GPT-5.2" width="800"/>

*Il controllo del ragionamento di GPT-5.2 ti permette di specificare quanto il modello deve riflettere — da risposte veloci e dirette a esplorazioni approfondite*

**Bassa Motivazione (Veloce & Mirato)** - Per domande semplici dove vuoi risposte rapide e dirette. Il modello fa un ragionamento minimo - massimo 2 passaggi. Usalo per calcoli, ricerche o domande dirette.

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
> - "Qual è la differenza tra modelli di prompting a bassa e alta motivazione?"
> - "Come aiutano i tag XML nei prompt a strutturare la risposta dell’IA?"
> - "Quando dovrei usare modelli di auto-riflessione rispetto a istruzioni dirette?"

**Alta Motivazione (Profondo & Dettagliato)** - Per problemi complessi dove vuoi un’analisi completa. Il modello esplora a fondo e mostra un ragionamento dettagliato. Usalo per progettazioni di sistema, decisioni di architettura o ricerche complesse.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Esecuzione di Compiti (Progresso Passo-Passo)** - Per flussi di lavoro multi-step. Il modello fornisce un piano iniziale, narra ogni passaggio mentre lavora, poi fornisce un riepilogo. Usalo per migrazioni, implementazioni o qualsiasi processo multi-step.

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

Il prompting Catena di Pensiero chiede esplicitamente al modello di mostrare il ragionamento, migliorando l’accuratezza per compiti complessi. La scomposizione passo-passo aiuta sia gli umani che l’IA a comprendere la logica.

> **🤖 Prova con la Chat di [GitHub Copilot](https://github.com/features/copilot):** Chiedi su questo modello:
> - "Come adatterei il modello di esecuzione compiti per operazioni di lunga durata?"
> - "Quali sono le best practice per strutturare le introduzioni degli strumenti nelle applicazioni di produzione?"
> - "Come posso catturare e mostrare aggiornamenti di progresso intermedi in un’interfaccia utente?"

Il diagramma qui sotto illustra questo flusso Plan → Execute → Summarize.

<img src="../../../translated_images/it/task-execution-pattern.9da3967750ab5c1e.webp" alt="Modello di Esecuzione dei Compiti" width="800"/>

*Flusso Plan → Execute → Summarize per compiti multi-step*

**Codice Auto-Riflessivo** - Per generare codice di qualità produttiva. Il modello genera codice seguendo standard di produzione con una gestione errori adeguata. Usalo per costruire nuove funzionalità o servizi.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

Il diagramma seguente mostra questo ciclo di miglioramento iterativo — genera, valuta, identifica debolezze e affina fino a quando il codice soddisfa gli standard produttivi.

<img src="../../../translated_images/it/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciclo di Auto-Riflessione" width="800"/>

*Ciclo di miglioramento iterativo - genera, valuta, identifica problemi, migliora, ripeti*

**Analisi Strutturata** - Per valutazioni coerenti. Il modello revisiona il codice usando un quadro fisso (correttezza, pratiche, performance, sicurezza, manutenibilità). Usalo per review di codice o valutazioni di qualità.

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

> **🤖 Prova con la Chat di [GitHub Copilot](https://github.com/features/copilot):** Chiedi sull’analisi strutturata:
> - "Come posso personalizzare il quadro di analisi per diversi tipi di revisioni di codice?"
> - "Qual è il modo migliore per analizzare e agire su output strutturati programmaticamente?"
> - "Come garantire livelli di severità coerenti tra diverse sessioni di revisione?"

Il diagramma seguente mostra come questo quadro strutturato organizza una revisione di codice in categorie coerenti con livelli di severità.

<img src="../../../translated_images/it/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Modello di Analisi Strutturata" width="800"/>

*Quadro per revisioni di codice coerenti con livelli di severità*

**Chat Multi-Turno** - Per conversazioni che necessitano contesto. Il modello ricorda i messaggi precedenti e costruisce su di essi. Usalo per sessioni di aiuto interattive o Q&A complessi.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

Il diagramma seguente visualizza come il contesto della conversazione si accumula a ogni turno e come si relaziona al limite di token del modello.

<img src="../../../translated_images/it/context-memory.dff30ad9fa78832a.webp" alt="Memoria del Contesto" width="800"/>

*Come il contesto di conversazione si accumula su più turni fino a raggiungere il limite di token*
**Step-by-Step Reasoning** - Per problemi che richiedono una logica visibile. Il modello mostra un ragionamento esplicito per ogni passaggio. Usalo per problemi di matematica, puzzle logici o quando hai bisogno di comprendere il processo di pensiero.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

Il diagramma sottostante illustra come il modello suddivide i problemi in passaggi logici espliciti e numerati.

<img src="../../../translated_images/it/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Schema Passo-Passo" width="800"/>

*Scomporre i problemi in passaggi logici espliciti*

**Constrained Output** - Per risposte con requisiti di formato specifici. Il modello segue rigorosamente le regole di formato e lunghezza. Usalo per riassunti o quando hai bisogno di una struttura di output precisa.

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

Il seguente diagramma mostra come i vincoli guidano il modello a produrre un output che aderisce rigorosamente al tuo formato e ai requisiti di lunghezza.

<img src="../../../translated_images/it/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Schema Output Vincolato" width="800"/>

*Applicare requisiti specifici di formato, lunghezza e struttura*

## Esegui l'Applicazione

**Verifica il deployment:**

Assicurati che il file `.env` esista nella directory principale con le credenziali Azure (create durante il Modulo 01). Esegui questo dal modulo (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Avvia l'applicazione:**

> **Nota:** Se hai già avviato tutte le applicazioni usando `./start-all.sh` dalla directory principale (come descritto nel Modulo 01), questo modulo è già in esecuzione sulla porta 8083. Puoi saltare i comandi di avvio qui sotto e andare direttamente a http://localhost:8083.

**Opzione 1: Uso del Spring Boot Dashboard (Consigliato per utenti VS Code)**

Il contenitore di sviluppo include l'estensione Spring Boot Dashboard, che fornisce un'interfaccia visiva per gestire tutte le applicazioni Spring Boot. La trovi nella Activity Bar sul lato sinistro di VS Code (cerca l'icona di Spring Boot).

Dal Spring Boot Dashboard puoi:
- Vedere tutte le applicazioni Spring Boot disponibili nel workspace
- Avviare/fermare le applicazioni con un solo clic
- Visualizzare i log delle applicazioni in tempo reale
- Monitorare lo stato delle applicazioni

Basta cliccare il pulsante play accanto a "prompt-engineering" per avviare questo modulo, o avviare tutti i moduli contemporaneamente.

<img src="../../../translated_images/it/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*Il Spring Boot Dashboard in VS Code — avvia, ferma e monitora tutti i moduli da un unico posto*

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

Entrambi gli script caricano automaticamente le variabili d'ambiente dal file `.env` nella directory principale e costruiranno i JAR se non esistono.

> **Nota:** Se preferisci compilare tutti i moduli manualmente prima di avviare:
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

Apri http://localhost:8083 nel browser.

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

Ecco l'interfaccia principale del modulo di prompt engineering, dove puoi sperimentare tutti e otto i pattern fianco a fianco.

<img src="../../../translated_images/it/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*La dashboard principale che mostra tutti gli 8 pattern di prompt engineering con le loro caratteristiche e casi d'uso*

## Esplorazione dei Pattern

L'interfaccia web ti permette di sperimentare diverse strategie di prompting. Ogni pattern risolve diversi problemi — provali per capire quando ciascun approccio funziona meglio.

> **Nota: Streaming vs Non-Streaming** — Ogni pagina di pattern offre due pulsanti: **🔴 Stream Response (Live)** e una opzione **Non-streaming**. Lo streaming usa Server-Sent Events (SSE) per mostrare i token in tempo reale mentre il modello li genera, così vedi subito i progressi. L'opzione non-streaming aspetta l'intera risposta prima di visualizzarla. Per prompt che avviano ragionamenti complessi (es. High Eagerness, Self-Reflecting Code), la chiamata non-streaming può richiedere molto tempo — a volte minuti — senza alcun feedback visibile. **Usa lo streaming quando sperimenti prompt complessi** così puoi vedere il modello lavorare ed evitare l'impressione che la richiesta sia andata in timeout.
>
> **Nota: Requisito Browser** — La funzione streaming usa la Fetch Streams API (`response.body.getReader()`) che richiede un browser completo (Chrome, Edge, Firefox, Safari). Non funziona nel Simple Browser integrato in VS Code, poiché la sua webview non supporta la ReadableStream API. Se usi il Simple Browser, i pulsanti non-streaming funzioneranno normalmente — solo i pulsanti streaming sono interessati. Apri `http://localhost:8083` in un browser esterno per l'esperienza completa.

### Low vs High Eagerness

Fai una domanda semplice come "Qual è il 15% di 200?" usando Low Eagerness. Otterrai una risposta istantanea e diretta. Ora chiedi qualcosa di complesso come "Progetta una strategia di caching per una API ad alto traffico" usando High Eagerness. Clicca **🔴 Stream Response (Live)** e guarda apparire il ragionamento dettagliato del modello token per token. Stesso modello, stessa struttura di domanda — ma il prompt indica quanta riflessione deve fare.

### Esecuzione di Compiti (Tool Preambles)

I workflow multi-step beneficiano di pianificazione anticipata e narrazione dei progressi. Il modello delinea cosa farà, narra ogni passo e poi riassume i risultati.

### Codice Auto-Riflettente

Prova "Crea un servizio di validazione email". Invece di generare solo il codice e fermarsi, il modello genera, valuta secondo criteri di qualità, identifica debolezze e migliora. Lo vedrai iterare fino a quando il codice soddisfa gli standard di produzione.

### Analisi Strutturata

Le revisioni di codice necessitano di framework di valutazione coerenti. Il modello analizza il codice usando categorie fisse (correttezza, pratiche, prestazioni, sicurezza) con livelli di gravità.

### Chat Multi-Turno

Chiedi "Cos'è Spring Boot?" e subito dopo "Fammi vedere un esempio". Il modello ricorda la tua prima domanda e ti dà un esempio specifico di Spring Boot. Senza memoria, la seconda domanda sarebbe troppo vaga.

### Step-by-Step Reasoning

Scegli un problema di matematica e provalo sia con Step-by-Step Reasoning che con Low Eagerness. Low Eagerness ti dà solo la risposta — veloce ma opaca. Step-by-step ti mostra ogni calcolo e decisione.

### Constrained Output

Quando hai bisogno di formati specifici o conteggi di parole, questo pattern impone un'adesione rigorosa. Prova a generare un riassunto con esattamente 100 parole in formato elenco puntato.

## Cosa Stai Davvero Imparando

**Lo Sforzo di Ragionamento Cambia Tutto**

GPT-5.2 ti permette di controllare lo sforzo computazionale tramite i prompt. Poco sforzo significa risposte rapide con esplorazione minima. Molto sforzo significa che il modello impiega tempo per riflettere profondamente. Stai imparando ad abbinare lo sforzo alla complessità del compito — non perdere tempo con domande semplici, ma non affrettare decisioni complesse.

**La Struttura Guida il Comportamento**

Hai notato i tag XML nei prompt? Non sono decorativi. I modelli seguono istruzioni strutturate in modo più affidabile rispetto al testo libero. Quando hai bisogno di processi multi-step o logica complessa, la struttura aiuta il modello a tenere traccia di dove si trova e cosa viene dopo. Il diagramma sotto scompone un prompt ben strutturato, mostrando come tag come `<system>`, `<instructions>`, `<context>`, `<user-input>`, e `<constraints>` organizzano le tue istruzioni in sezioni chiare.

<img src="../../../translated_images/it/prompt-structure.a77763d63f4e2f89.webp" alt="Struttura del Prompt" width="800"/>

*Anatomia di un prompt ben strutturato con sezioni chiare e organizzazione in stile XML*

**Qualità tramite Auto-Valutazione**

I pattern che usano l'auto-riflessione funzionano rendendo espliciti i criteri di qualità. Invece di sperare che il modello "faccia bene", gli dici esattamente cosa significa "bene": logica corretta, gestione degli errori, prestazioni, sicurezza. Il modello può quindi valutare la propria uscita e migliorare. Questo trasforma la generazione di codice da una lotteria a un processo.

**Il Contesto è Finito**

Le conversazioni multi-turno funzionano includendo la storia dei messaggi con ogni richiesta. Ma c'è un limite — ogni modello ha un conteggio massimo di token. Man mano che le conversazioni crescono, avrai bisogno di strategie per mantenere contesto rilevante senza superare quel limite. Questo modulo ti mostra come funziona la memoria; più avanti imparerai quando riassumere, quando dimenticare e quando recuperare.

## Passi Successivi

**Prossimo Modulo:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigazione:** [← Precedente: Modulo 01 - Introduzione](../01-introduction/README.md) | [Torna al Principale](../README.md) | [Avanti: Modulo 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Sebbene ci impegniamo per l’accuratezza, si prega di considerare che le traduzioni automatiche possono contenere errori o imprecisioni. Il documento originale nella sua lingua natìa deve essere considerato la fonte autorevole. Per informazioni critiche si raccomanda la traduzione professionale umana. Non siamo responsabili per eventuali incomprensioni o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->