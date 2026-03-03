# Modulo 00: Avvio Rapido

## Indice

- [Introduzione](../../../00-quick-start)
- [Cos'è LangChain4j?](../../../00-quick-start)
- [Dipendenze LangChain4j](../../../00-quick-start)
- [Prerequisiti](../../../00-quick-start)
- [Configurazione](../../../00-quick-start)
  - [1. Ottieni il tuo token GitHub](../../../00-quick-start)
  - [2. Imposta il tuo token](../../../00-quick-start)
- [Esegui gli esempi](../../../00-quick-start)
  - [1. Chat di base](../../../00-quick-start)
  - [2. Modelli di prompt](../../../00-quick-start)
  - [3. Chiamata di funzioni](../../../00-quick-start)
  - [4. Domande e risposte sui documenti (Easy RAG)](../../../00-quick-start)
  - [5. AI responsabile](../../../00-quick-start)
- [Cosa mostra ogni esempio](../../../00-quick-start)
- [Passi successivi](../../../00-quick-start)
- [Risoluzione dei problemi](../../../00-quick-start)

## Introduzione

Questo avvio rapido è pensato per farti partire rapidamente con LangChain4j. Copre le basi assolute per costruire applicazioni AI con LangChain4j e GitHub Models. Nei moduli successivi passerai ad Azure OpenAI e GPT-5.2 e approfondirai ogni concetto.

## Cos'è LangChain4j?

LangChain4j è una libreria Java che semplifica la costruzione di applicazioni AI. Invece di gestire client HTTP e parsing JSON, si lavora con API Java pulite.

La "catena" in LangChain si riferisce al concatenamento di più componenti - potresti concatenare un prompt a un modello a un parser, o concatenare più chiamate AI dove un output alimenta il successivo input. Questo avvio rapido si concentra sui fondamenti prima di esplorare catene più complesse.

<img src="../../../translated_images/it/langchain-concept.ad1fe6cf063515e1.webp" alt="Concetto di concatenamento LangChain4j" width="800"/>

*Componenti concatenati in LangChain4j - blocchi costitutivi che si connettono per creare potenti flussi di lavoro AI*

Utilizzeremo tre componenti principali:

**ChatModel** - Interfaccia per interazioni con il modello AI. Chiamare `model.chat("prompt")` e ottenere una stringa di risposta. Usiamo `OpenAiOfficialChatModel` che funziona con endpoint compatibili OpenAI come GitHub Models.

**AiServices** - Crea interfacce di servizi AI tipizzate. Definisci metodi, annotali con `@Tool` e LangChain4j gestisce l'orchestrazione. L'AI chiama automaticamente i tuoi metodi Java quando necessario.

**MessageWindowChatMemory** - Mantiene la cronologia della conversazione. Senza di essa, ogni richiesta è indipendente. Con essa, l'AI ricorda i messaggi precedenti e mantiene il contesto attraverso più turni.

<img src="../../../translated_images/it/architecture.eedc993a1c576839.webp" alt="Architettura LangChain4j" width="800"/>

*Architettura LangChain4j - componenti core che lavorano insieme per alimentare le tue applicazioni AI*

## Dipendenze LangChain4j

Questo avvio rapido utilizza tre dipendenze Maven nel [`pom.xml`](../../../00-quick-start/pom.xml):

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

Il modulo `langchain4j-open-ai-official` fornisce la classe `OpenAiOfficialChatModel` che si connette ad API compatibili OpenAI. GitHub Models usa lo stesso formato API, quindi non serve un adattatore speciale - basta puntare la URL base a `https://models.github.ai/inference`.

Il modulo `langchain4j-easy-rag` offre divisione automatica dei documenti, embedding e retrieval così da poter costruire applicazioni RAG senza configurare manualmente ogni passaggio.

## Prerequisiti

**Usi il Dev Container?** Java e Maven sono già installati. Ti serve solo un Personal Access Token GitHub.

**Sviluppo locale:**
- Java 21+, Maven 3.9+
- Personal Access Token GitHub (istruzioni sotto)

> **Nota:** Questo modulo usa `gpt-4.1-nano` da GitHub Models. Non modificare il nome del modello nel codice - è configurato per funzionare con i modelli disponibili su GitHub.

## Configurazione

### 1. Ottieni il tuo token GitHub

1. Vai su [Impostazioni GitHub → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Clicca "Genera nuovo token"
3. Dai un nome descrittivo (es. "Demo LangChain4j")
4. Imposta scadenza (consigliato 7 giorni)
5. Sotto "Permessi account", trova "Models" e imposta su "Sola lettura"
6. Clicca "Genera token"
7. Copia e salva il token - non sarà più visibile

### 2. Imposta il tuo token

**Opzione 1: Usare VS Code (Consigliato)**

Se usi VS Code, aggiungi il token nel file `.env` nella radice del progetto:

Se il file `.env` non esiste, copia `.env.example` in `.env` o crea un nuovo `.env` nella radice del progetto.

**Esempio file `.env`:**
```bash
# In /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Poi basta cliccare col destro su un qualsiasi file demo (es. `BasicChatDemo.java`) nell'Explorer e scegliere **"Run Java"** oppure usare le configurazioni di avvio dal pannello Run and Debug.

**Opzione 2: Usare il Terminale**

Imposta il token come variabile d’ambiente:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Esegui gli esempi

**Usando VS Code:** Basta cliccare col tasto destro un file demo nell'Explorer e scegliere **"Run Java"**, oppure usare le configurazioni dal pannello Run and Debug (assicurati prima di avere aggiunto il token nel file `.env`).

**Usando Maven:** In alternativa, esegui da linea di comando:

### 1. Chat di base

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Modelli di prompt

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Mostra prompting zero-shot, few-shot, chain-of-thought e basato su ruoli.

### 3. Chiamata di funzioni

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

L’AI chiama automaticamente i tuoi metodi Java quando necessario.

### 4. Domande e risposte sui documenti (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Fai domande sui tuoi documenti usando Easy RAG con embedding e retrieval automatici.

### 5. AI responsabile

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Vedi come i filtri di sicurezza AI bloccano contenuti dannosi.

## Cosa mostra ogni esempio

**Chat di base** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Inizia qui per vedere LangChain4j nel suo aspetto più semplice. Creerai un `OpenAiOfficialChatModel`, invierai un prompt con `.chat()`, e otterrai una risposta. Questo dimostra la base: come inizializzare modelli con endpoint e chiavi API personalizzate. Una volta compreso questo schema, tutto il resto si basa su di esso.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) e chiedi:
> - "Come passo da GitHub Models ad Azure OpenAI in questo codice?"
> - "Quali altri parametri posso configurare in OpenAiOfficialChatModel.builder()?"
> - "Come aggiungo risposte in streaming invece di aspettare la risposta completa?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Ora che sai come parlare con un modello, esploriamo cosa gli dici. Questa demo usa la stessa configurazione modello ma mostra cinque diversi modelli di prompt. Prova prompt zero-shot per istruzioni dirette, few-shot che imparano dagli esempi, chain-of-thought che mostrano i passaggi di ragionamento, e prompt basati su ruoli che impostano il contesto. Vedrai come lo stesso modello dà risultati molto diversi in base a come formuli la richiesta.

La demo mostra anche template di prompt, un modo potente per creare prompt riutilizzabili con variabili.
L’esempio sotto mostra un prompt che usa il LangChain4j `PromptTemplate` per riempire variabili. L’AI risponderà basandosi sulla destinazione e attività fornita.

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

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) e chiedi:
> - "Qual è la differenza tra zero-shot e few-shot prompting, e quando dovrei usare ciascuno?"
> - "Come influisce il parametro temperature sulle risposte del modello?"
> - "Quali tecniche ci sono per prevenire attacchi di injection nei prompt in produzione?"
> - "Come posso creare oggetti PromptTemplate riutilizzabili per modelli comuni?"

**Integrazione di strumenti** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Qui LangChain4j diventa potente. Userai `AiServices` per creare un assistente AI che chiama i tuoi metodi Java. Basta annotare metodi con `@Tool("descrizione")` e LangChain4j fa il resto - l’AI decide automaticamente quando usare ogni strumento in base a cosa chiede l’utente. Questo dimostra la chiamata di funzioni, una tecnica chiave per costruire AI che può agire, non solo rispondere a domande.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) e chiedi:
> - "Come funziona l’annotazione @Tool e cosa fa LangChain4j dietro le quinte?"
> - "L’AI può chiamare più strumenti in sequenza per risolvere problemi complessi?"
> - "Cosa succede se uno strumento genera un’eccezione - come gestisco gli errori?"
> - "Come integrazione una vera API invece di questo esempio calcolatrice?"

**Domande e risposte sui documenti (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Qui vedrai RAG (retrieval-augmented generation) usando l’approccio "Easy RAG" di LangChain4j. I documenti sono caricati, automaticamente divisi e embedded in una memoria, poi un content retriever fornisce i pezzi rilevanti all’AI al momento della richiesta. L’AI risponde basandosi sui tuoi documenti, non sulla sua conoscenza generale.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) e chiedi:
> - "Come RAG previene le allucinazioni AI rispetto all’uso dei dati di training del modello?"
> - "Qual è la differenza tra questo approccio facile e una pipeline RAG personalizzata?"
> - "Come farei a scalare questo per gestire più documenti o basi di conoscenza più grandi?"

**AI responsabile** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Costruisci sicurezza AI con difesa in profondità. Questa demo mostra due livelli di protezione che lavorano insieme:

**Parte 1: LangChain4j Input Guardrails** - Blocca prompt pericolosi prima che raggiungano il LLM. Crea guardrail personalizzati che cercano parole chiave o modelli vietati. Questi girano nel tuo codice, quindi sono veloci e gratuiti.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**Parte 2: Filtri di sicurezza del provider** - GitHub Models ha filtri integrati che intercettano quello che potrebbero perdere i tuoi guardrail. Vedrai blocchi rigidi (errori HTTP 400) per violazioni gravi e rifiuti morbidi dove l’AI declina educatamente.

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) e chiedi:
> - "Cos’è InputGuardrail e come creane uno mio?"
> - "Qual è la differenza tra blocco rigido e rifiuto morbido?"
> - "Perché usare insieme guardrail e filtri provider?"

## Passi successivi

**Modulo Successivo:** [01-introduction - Introduzione a LangChain4j](../01-introduction/README.md)

---

**Navigazione:** [← Indietro al Principale](../README.md) | [Prossimo: Modulo 01 - Introduzione →](../01-introduction/README.md)

---

## Risoluzione dei problemi

### Prima Compilazione Maven

**Problema**: `mvn clean compile` o `mvn package` iniziali richiedono molto tempo (10-15 minuti)

**Causa**: Maven deve scaricare tutte le dipendenze del progetto (Spring Boot, librerie LangChain4j, SDK Azure, ecc.) alla prima compilazione.

**Soluzione**: Comportamento normale. Le compilazioni successive saranno molto più rapide perché le dipendenze sono memorizzate in cache localmente. Il tempo di download dipende dalla velocità della tua rete.

### Sintassi comando Maven in PowerShell

**Problema**: I comandi Maven falliscono con errore `Unknown lifecycle phase ".mainClass=..."`
**Causa**: PowerShell interpreta `=` come operatore di assegnazione di variabile, interrompendo la sintassi delle proprietà Maven

**Soluzione**: Usa l'operatore di stop-parsing `--%` prima del comando Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

L'operatore `--%` indica a PowerShell di passare letteralmente tutti gli argomenti rimanenti a Maven senza interpretazione.

### Visualizzazione Emoji in Windows PowerShell

**Problema**: Le risposte AI mostrano caratteri strani (es. `????` o `â??`) invece delle emoji in PowerShell

**Causa**: La codifica predefinita di PowerShell non supporta le emoji UTF-8

**Soluzione**: Esegui questo comando prima di eseguire le applicazioni Java:
```cmd
chcp 65001
```

Questo forza la codifica UTF-8 nel terminale. In alternativa, usa Windows Terminal che ha un supporto Unicode migliore.

### Debug delle chiamate API

**Problema**: Errori di autenticazione, limiti di frequenza o risposte inaspettate dal modello AI

**Soluzione**: Gli esempi includono `.logRequests(true)` e `.logResponses(true)` per mostrare le chiamate API nella console. Questo aiuta a risolvere errori di autenticazione, limiti di frequenza o risposte inaspettate. Rimuovi queste opzioni in produzione per ridurre il rumore nei log.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avvertenza**:  
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Pur facendo del nostro meglio per garantire la precisione, si prega di notare che le traduzioni automatiche possono contenere errori o inesattezze. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale umana. Non ci assumiamo alcuna responsabilità per fraintendimenti o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->