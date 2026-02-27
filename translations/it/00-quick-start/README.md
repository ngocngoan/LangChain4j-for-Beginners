# Modulo 00: Avvio Rapido

## Indice

- [Introduzione](../../../00-quick-start)
- [Cos'è LangChain4j?](../../../00-quick-start)
- [Dipendenze di LangChain4j](../../../00-quick-start)
- [Prerequisiti](../../../00-quick-start)
- [Configurazione](../../../00-quick-start)
  - [1. Ottieni il tuo token GitHub](../../../00-quick-start)
  - [2. Imposta il tuo token](../../../00-quick-start)
- [Esegui gli Esempi](../../../00-quick-start)
  - [1. Chat Base](../../../00-quick-start)
  - [2. Pattern di Prompt](../../../00-quick-start)
  - [3. Chiamata di Funzione](../../../00-quick-start)
  - [4. Domande e Risposte su Documenti (Easy RAG)](../../../00-quick-start)
  - [5. AI Responsabile](../../../00-quick-start)
- [Cosa Mostra Ogni Esempio](../../../00-quick-start)
- [Passi Successivi](../../../00-quick-start)
- [Risoluzione dei Problemi](../../../00-quick-start)

## Introduzione

Questo avvio rapido è pensato per farti iniziare con LangChain4j il più rapidamente possibile. Copre le basi assolute per costruire applicazioni AI con LangChain4j e GitHub Models. Nei prossimi moduli userai Azure OpenAI con LangChain4j per creare applicazioni più avanzate.

## Cos'è LangChain4j?

LangChain4j è una libreria Java che semplifica la costruzione di applicazioni potenziate dall'AI. Invece di gestire client HTTP e parsing JSON, lavori con API Java pulite.

La "catena" in LangChain si riferisce al collegamento di più componenti insieme - potresti concatenare un prompt a un modello a un parser, o concatenare più chiamate AI dove un output alimenta l'input successivo. Questo avvio rapido si concentra sui fondamentali prima di esplorare catene più complesse.

<img src="../../../translated_images/it/langchain-concept.ad1fe6cf063515e1.webp" alt="Concetto di concatenazione LangChain4j" width="800"/>

*Concatenazione di componenti in LangChain4j - i blocchi di costruzione si collegano per creare potenti flussi di lavoro AI*

Useremo tre componenti base:

**ChatModel** - L'interfaccia per le interazioni con il modello AI. Chiama `model.chat("prompt")` e ottieni una stringa di risposta. Usiamo `OpenAiOfficialChatModel` che funziona con endpoint compatibili OpenAI come GitHub Models.

**AiServices** - Crea interfacce di servizi AI type-safe. Definisci metodi, annotali con `@Tool`, e LangChain4j gestisce l'orchestrazione. L'AI chiama automaticamente i tuoi metodi Java quando necessario.

**MessageWindowChatMemory** - Mantiene la cronologia della conversazione. Senza questo, ogni richiesta è indipendente. Con esso, l'AI si ricorda i messaggi precedenti e mantiene il contesto su più turni.

<img src="../../../translated_images/it/architecture.eedc993a1c576839.webp" alt="Architettura LangChain4j" width="800"/>

*Architettura LangChain4j - componenti core che lavorano insieme per potenziare le tue applicazioni AI*

## Dipendenze di LangChain4j

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

Il modulo `langchain4j-open-ai-official` fornisce la classe `OpenAiOfficialChatModel` che si connette ad API compatibili OpenAI. GitHub Models usa lo stesso formato API, quindi non serve alcun adattatore speciale - basta puntare l'URL base a `https://models.github.ai/inference`.

Il modulo `langchain4j-easy-rag` fornisce splitting automatico dei documenti, embedding e recupero così puoi costruire applicazioni RAG senza configurare manualmente ogni fase.

## Prerequisiti

**Usi il Dev Container?** Java e Maven sono già installati. Ti serve solo un Token Personale di Accesso GitHub.

**Sviluppo Locale:**
- Java 21+, Maven 3.9+
- Token Personale di Accesso GitHub (istruzioni sotto)

> **Nota:** Questo modulo usa `gpt-4.1-nano` da GitHub Models. Non modificare il nome del modello nel codice - è configurato per funzionare con i modelli disponibili su GitHub.

## Configurazione

### 1. Ottieni il tuo token GitHub

1. Vai su [Impostazioni GitHub → Token Personali di Accesso](https://github.com/settings/personal-access-tokens)
2. Clicca "Genera nuovo token"
3. Imposta un nome descrittivo (es. "Demo LangChain4j")
4. Imposta la scadenza (consigliati 7 giorni)
5. Sotto "Permessi account", trova "Models" e impostalo su "Sola lettura"
6. Clicca "Genera token"
7. Copia e salva il token - non lo vedrai più

### 2. Imposta il tuo token

**Opzione 1: Usa VS Code (consigliato)**

Se usi VS Code, aggiungi il token al file `.env` nella root del progetto:

Se il file `.env` non esiste, copia `.env.example` in `.env` o crea un nuovo file `.env` nella root del progetto.

**Esempio di file `.env`:**
```bash
# In /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Poi puoi semplicemente cliccare con il tasto destro su qualsiasi file demo (es. `BasicChatDemo.java`) nell’Esplora e selezionare **"Run Java"** oppure usare le configurazioni di lancio dal pannello Esegui e Debug.

**Opzione 2: Usa Terminale**

Imposta il token come variabile d’ambiente:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Esegui gli Esempi

**Usando VS Code:** Basta cliccare con il tasto destro su qualsiasi file demo nell’Esplora e selezionare **"Run Java"**, o usare le configurazioni di lancio dal pannello Esegui e Debug (assicurati di aver prima aggiunto il token nel file `.env`).

**Usando Maven:** In alternativa, puoi eseguire da linea di comando:

### 1. Chat Base

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Pattern di Prompt

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Mostra prompting zero-shot, few-shot, chain-of-thought, e basato su ruoli.

### 3. Chiamata di Funzione

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

L’AI chiama automaticamente i tuoi metodi Java quando necessario.

### 4. Domande e Risposte su Documenti (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Fai domande sui tuoi documenti usando Easy RAG con embedding e recupero automatici.

### 5. AI Responsabile

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Vedi come i filtri di sicurezza AI bloccano contenuti dannosi.

## Cosa Mostra Ogni Esempio

**Chat Base** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Inizia qui per vedere LangChain4j nel modo più semplice. Creerai un `OpenAiOfficialChatModel`, invierai un prompt con `.chat()`, e otterrai una risposta. Questo dimostra le basi: come inizializzare modelli con endpoint personalizzati e chiavi API. Una volta compreso questo schema, tutto il resto si basa su di esso.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Prova con la chat di [GitHub Copilot](https://github.com/features/copilot):** Apri [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) e chiedi:
> - "Come faccio a passare da GitHub Models a Azure OpenAI in questo codice?"
> - "Quali altri parametri posso configurare in OpenAiOfficialChatModel.builder()?"
> - "Come aggiungo risposte in streaming invece di attendere la risposta completa?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Ora che sai come parlare con un modello, esploriamo cosa gli dici. Questa demo usa la stessa configurazione modello ma mostra cinque pattern diversi di prompting. Prova prompt zero-shot per istruzioni dirette, few-shot che imparano da esempi, chain-of-thought che mostrano i passaggi di ragionamento, e prompt basati su ruoli che impostano il contesto. Vedrai come lo stesso modello produce risultati molto diversi a seconda di come formuli la richiesta.

La demo mostra anche template di prompt, modo potente per creare prompt riutilizzabili con variabili.
L'esempio sotto mostra un prompt che usa il `PromptTemplate` di LangChain4j per riempire variabili. L'AI risponderà in base a destinazione e attività fornite.

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

> **🤖 Prova con la chat di [GitHub Copilot](https://github.com/features/copilot):** Apri [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) e chiedi:
> - "Qual è la differenza tra zero-shot e few-shot prompting, e quando usare ciascuno?"
> - "Come influisce il parametro temperature sulle risposte del modello?"
> - "Quali sono alcune tecniche per prevenire attacchi di prompt injection in produzione?"
> - "Come posso creare oggetti PromptTemplate riutilizzabili per pattern comuni?"

**Integrazione di Tool** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Qui LangChain4j diventa potente. Userai `AiServices` per creare un assistente AI che può chiamare i tuoi metodi Java. Basta annotare i metodi con `@Tool("descrizione")` e LangChain4j gestisce il resto - l’AI decide automaticamente quando usare ogni tool in base a cosa chiede l’utente. Questo dimostra la chiamata di funzione, tecnica chiave per costruire AI che può agire, non solo rispondere.

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

> **🤖 Prova con la chat di [GitHub Copilot](https://github.com/features/copilot):** Apri [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) e chiedi:
> - "Come funziona l’annotazione @Tool e cosa fa LangChain4j dietro le quinte?"
> - "L’AI può chiamare più strumenti in sequenza per risolvere problemi complessi?"
> - "Cosa succede se un tool lancia un’eccezione - come devo gestire gli errori?"
> - "Come integrerei un’API reale invece di questo esempio calcolatore?"

**Domande e Risposte su Documenti (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Qui vedrai RAG (generazione aumentata da recupero) usando l’approccio "Easy RAG" di LangChain4j. I documenti vengono caricati, automaticamente divisi e incorporati in una memoria temporanea, poi un content retriever fornisce i pezzi rilevanti all’AI al momento della query. L’AI risponde basandosi sui tuoi documenti, non sulla sua conoscenza generale.

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

> **🤖 Prova con la chat di [GitHub Copilot](https://github.com/features/copilot):** Apri [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) e chiedi:
> - "Come RAG previene le allucinazioni AI rispetto all’uso dei dati di addestramento del modello?"
> - "Qual è la differenza tra questo approccio facile e una pipeline RAG personalizzata?"
> - "Come scalerei per gestire più documenti o basi di conoscenza più grandi?"

**AI Responsabile** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Costruisci sicurezza AI con difesa in profondità. Questa demo mostra due livelli di protezione che lavorano insieme:

**Parte 1: Input Guardrails di LangChain4j** - Blocca prompt pericolosi prima che raggiungano il LLM. Crea guardrail personalizzati che cercano parole chiave o pattern proibiti. Questi girano nel tuo codice, quindi sono veloci e gratuiti.

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

**Parte 2: Filtri di Sicurezza Provider** - GitHub Models ha filtri integrati che catturano ciò che i tuoi guardrail potrebbero mancare. Vedrai blocchi rigidi (errori HTTP 400) per violazioni gravi e rifiuti soft dove l’AI declina cortesemente.

> **🤖 Prova con la chat di [GitHub Copilot](https://github.com/features/copilot):** Apri [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) e chiedi:
> - "Cos’è InputGuardrail e come creo il mio?"
> - "Qual è la differenza tra un blocco rigido e un rifiuto soft?"
> - "Perché usare sia guardrail che filtri provider insieme?"

## Passi Successivi

**Prossimo Modulo:** [01-introduzione - Iniziare con LangChain4j e gpt-5 su Azure](../01-introduction/README.md)

---

**Navigazione:** [← Torna al principale](../README.md) | [Prossimo: Modulo 01 - Introduzione →](../01-introduction/README.md)

---

## Risoluzione dei Problemi

### Prima Compilazione Maven

**Problema**: La compilazione iniziale `mvn clean compile` o `mvn package` richiede molto tempo (10-15 minuti)

**Causa**: Maven deve scaricare tutte le dipendenze del progetto (Spring Boot, librerie LangChain4j, SDK Azure, ecc.) alla prima compilazione.

**Soluzione**: Questo è comportamento normale. Le compilazioni successive saranno molto più rapide perché le dipendenze sono cache localmente. Il tempo di download dipende dalla velocità della tua rete.

### Sintassi Comandi Maven su PowerShell

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

L'operatore `--%` indica a PowerShell di passare letteralmente tutti gli argomenti rimanenti a Maven senza interpretarli.

### Visualizzazione delle Emoji in Windows PowerShell

**Problema**: Le risposte AI mostrano caratteri non leggibili (ad esempio, `????` o `â??`) al posto delle emoji in PowerShell

**Causa**: La codifica predefinita di PowerShell non supporta le emoji UTF-8

**Soluzione**: Esegui questo comando prima di avviare applicazioni Java:
```cmd
chcp 65001
```

Questo forza la codifica UTF-8 nel terminale. In alternativa, usa Windows Terminal che ha un miglior supporto Unicode.

### Debug delle Chiamate API

**Problema**: Errori di autenticazione, limiti di frequenza o risposte inattese dal modello AI

**Soluzione**: Gli esempi includono `.logRequests(true)` e `.logResponses(true)` per mostrare le chiamate API nella console. Questo aiuta a risolvere errori di autenticazione, limiti di frequenza o risposte inattese. Rimuovi questi flag in produzione per ridurre il rumore nei log.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Dichiarazione di non responsabilità**:  
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Sebbene ci impegniamo per garantire l’accuratezza, si prega di notare che le traduzioni automatiche possono contenere errori o inesattezze. Il documento originale nella sua lingua madre deve essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale effettuata da un essere umano. Non siamo responsabili per eventuali malintesi o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->