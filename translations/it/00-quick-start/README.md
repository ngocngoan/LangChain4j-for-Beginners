# Modulo 00: Avvio Rapido

## Indice

- [Introduzione](../../../00-quick-start)
- [Cos'è LangChain4j?](../../../00-quick-start)
- [Dipendenze di LangChain4j](../../../00-quick-start)
- [Prerequisiti](../../../00-quick-start)
- [Configurazione](../../../00-quick-start)
  - [1. Ottieni il tuo Token GitHub](../../../00-quick-start)
  - [2. Imposta il tuo Token](../../../00-quick-start)
- [Esegui gli Esempi](../../../00-quick-start)
  - [1. Chat Base](../../../00-quick-start)
  - [2. Schemi di Prompt](../../../00-quick-start)
  - [3. Chiamata di Funzioni](../../../00-quick-start)
  - [4. Domande & Risposte su Documenti (RAG)](../../../00-quick-start)
  - [5. Intelligenza Artificiale Responsabile](../../../00-quick-start)
- [Cosa Mostra Ogni Esempio](../../../00-quick-start)
- [Prossimi Passi](../../../00-quick-start)
- [Risoluzione dei Problemi](../../../00-quick-start)

## Introduzione

Questo avvio rapido è pensato per farti iniziare a usare LangChain4j il più velocemente possibile. Copre le basi assolute della costruzione di applicazioni AI con LangChain4j e GitHub Models. Nei moduli successivi utilizzerai Azure OpenAI con LangChain4j per costruire applicazioni più avanzate.

## Cos'è LangChain4j?

LangChain4j è una libreria Java che semplifica la creazione di applicazioni potenziate dall'intelligenza artificiale. Invece di gestire client HTTP e parsing JSON, lavori con API Java pulite.

La "catena" in LangChain si riferisce a concatene di più componenti - potresti concatenare un prompt a un modello a un parser, o concatenare più chiamate AI dove un output viene usato come input per la chiamata successiva. Questo avvio rapido si concentra sui fondamenti prima di esplorare catene più complesse.

<img src="../../../translated_images/it/langchain-concept.ad1fe6cf063515e1.webp" alt="Concetto di concatenamento di LangChain4j" width="800"/>

*Concatenamento di componenti in LangChain4j - i blocchi costitutivi si collegano per creare potenti flussi di lavoro AI*

Useremo tre componenti principali:

**ChatLanguageModel** - L'interfaccia per le interazioni con i modelli AI. Chiama `model.chat("prompt")` e ottieni una stringa di risposta. Usiamo `OpenAiOfficialChatModel` che funziona con endpoint compatibili OpenAI come GitHub Models.

**AiServices** - Crea interfacce di servizio AI ad tipo sicuro. Definisci metodi, annotali con `@Tool`, e LangChain4j gestisce l'orchestrazione. L'AI chiama automaticamente i tuoi metodi Java quando necessario.

**MessageWindowChatMemory** - Mantiene la cronologia della conversazione. Senza questo, ogni richiesta è indipendente. Con esso, l'AI ricorda i messaggi precedenti e mantiene il contesto su più turni.

<img src="../../../translated_images/it/architecture.eedc993a1c576839.webp" alt="Architettura di LangChain4j" width="800"/>

*Architettura di LangChain4j - componenti core che lavorano insieme per potenziare le tue applicazioni AI*

## Dipendenze di LangChain4j

Questo avvio rapido usa due dipendenze Maven nel [`pom.xml`](../../../00-quick-start/pom.xml):

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
```

Il modulo `langchain4j-open-ai-official` fornisce la classe `OpenAiOfficialChatModel` che si connette alle API compatibili OpenAI. GitHub Models usa lo stesso formato API, quindi non serve un adattatore speciale - basta puntare la URL base a `https://models.github.ai/inference`.

## Prerequisiti

**Usi il Contenitore di Sviluppo?** Java e Maven sono già installati. Ti serve solo un Token di Accesso Personale GitHub.

**Sviluppo Locale:**
- Java 21+, Maven 3.9+
- Token di Accesso Personale GitHub (istruzioni sotto)

> **Nota:** Questo modulo usa `gpt-4.1-nano` di GitHub Models. Non modificare il nome del modello nel codice - è configurato per funzionare con i modelli disponibili su GitHub.

## Configurazione

### 1. Ottieni il tuo Token GitHub

1. Vai su [Impostazioni GitHub → Token di Accesso Personale](https://github.com/settings/personal-access-tokens)
2. Clicca su "Genera nuovo token"
3. Imposta un nome descrittivo (es. "Demo LangChain4j")
4. Imposta la scadenza (7 giorni consigliati)
5. In "Permessi account", trova "Models" e imposta su "Sola lettura"
6. Clicca su "Genera token"
7. Copia e salva il token - non lo vedrai più

### 2. Imposta il tuo Token

**Opzione 1: Usare VS Code (Consigliato)**

Se usi VS Code, aggiungi il token nel file `.env` nella radice del progetto:

Se il file `.env` non esiste, copia `.env.example` in `.env` oppure crea un nuovo file `.env` nella radice del progetto.

**Esempio di file `.env`:**
```bash
# In /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Poi puoi semplicemente cliccare col destro su qualsiasi file demo (es. `BasicChatDemo.java`) nell’Explorer e selezionare **"Run Java"** oppure usare le configurazioni di avvio dal pannello Run and Debug.

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

## Esegui gli Esempi

**Usando VS Code:** Clicca col destro su qualsiasi file demo nell’Explorer e seleziona **"Run Java"**, o usa le configurazioni di lancio dal pannello Run and Debug (assicurati di aver prima aggiunto il token al file `.env`).

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

### 2. Schemi di Prompt

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Mostra zero-shot, few-shot, chain-of-thought, e prompting basato su ruoli.

### 3. Chiamata di Funzioni

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

L’AI chiama automaticamente i tuoi metodi Java quando serve.

### 4. Domande & Risposte su Documenti (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Fai domande su contenuti in `document.txt`.

### 5. Intelligenza Artificiale Responsabile

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Vedi come i filtri di sicurezza AI bloccano contenuti nocivi.

## Cosa Mostra Ogni Esempio

**Chat Base** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Inizia qui per vedere LangChain4j nella sua forma più semplice. Creerai un `OpenAiOfficialChatModel`, invierai un prompt con `.chat()`, e riceverai una risposta. Dimostra le basi: come inizializzare modelli con endpoint personalizzati e chiavi API. Una volta compreso questo schema, tutto il resto si costruisce sopra.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) e chiedi:
> - "Come passo da GitHub Models a Azure OpenAI in questo codice?"
> - "Quali altri parametri posso configurare in OpenAiOfficialChatModel.builder()?"
> - "Come aggiungo risposte in streaming invece di aspettare la risposta completa?"

**Ingegneria del Prompt** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Ora che sai come parlare con un modello, esploriamo cosa dici. Questa demo usa la stessa configurazione modello ma mostra cinque modelli di prompting diversi. Prova prompt zero-shot per istruzioni dirette, few-shot che apprendono da esempi, chain-of-thought che rivelano ragionamenti, e prompt basati sui ruoli che impostano il contesto. Vedrai come lo stesso modello dà risultati molto diversi in base a come formuli la richiesta.

La demo mostra anche template di prompt, un modo potente per creare prompt riutilizzabili con variabili.
L’esempio sotto mostra un prompt che usa il `PromptTemplate` di LangChain4j per riempire variabili. L’AI risponde in base alla destinazione e all’attività fornita.

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
> - "Qual è la differenza tra zero-shot e few-shot prompting, e quando usarli?"
> - "Come influenza il parametro temperatura le risposte del modello?"
> - "Quali tecniche ci sono per prevenire attacchi di prompt injection in produzione?"
> - "Come posso creare oggetti PromptTemplate riutilizzabili per schemi comuni?"

**Integrazione di Strumenti** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Qui LangChain4j diventa potente. Userai `AiServices` per creare un assistente AI che può chiamare i tuoi metodi Java. Basta annotare i metodi con `@Tool("descrizione")` e LangChain4j gestisce tutto - l’AI decide automaticamente quando usare ogni strumento in base a cosa chiede l’utente. Dimostra la chiamata di funzioni, una tecnica chiave per costruire AI che possa agire, non solo rispondere.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) e chiedi:
> - "Come funziona l’annotazione @Tool e cosa fa LangChain4j dietro le quinte?"
> - "L’AI può chiamare più strumenti in sequenza per risolvere problemi complessi?"
> - "Cosa succede se uno strumento genera un’eccezione - come gestisco gli errori?"
> - "Come integrerei una vera API invece di questo esempio con la calcolatrice?"

**Domande & Risposte su Documenti (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Qui vedi le basi di RAG (retrieval-augmented generation). Invece di affidarsi solo ai dati di addestramento del modello, carichi contenuti da [`document.txt`](../../../00-quick-start/document.txt) e li includi nel prompt. L’AI risponde basandosi sul documento, non sulla conoscenza generale. È il primo passo per costruire sistemi che lavorano con i tuoi dati.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Nota:** Questo approccio semplice carica l’intero documento nel prompt. Per file grandi (>10KB), supererai i limiti di contesto. Il Modulo 03 copre chunking e ricerca vettoriale per sistemi RAG in produzione.

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) e chiedi:
> - "Come RAG previene le allucinazioni AI rispetto ai dati di training del modello?"
> - "Qual è la differenza tra questo approccio semplice e l’uso di embedding vettoriali per il recupero?"
> - "Come scalerei questo per gestire più documenti o basi di conoscenza più grandi?"
> - "Quali sono le best practice per strutturare il prompt affinché l’AI usi solo il contesto fornito?"

**Intelligenza Artificiale Responsabile** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Costruisci sicurezza AI con difesa in profondità. Questa demo mostra due livelli di protezione che lavorano insieme:

**Parte 1: LangChain4j Input Guardrails** - Blocca prompt pericolosi prima che arrivino al LLM. Crea guardrails personalizzate che controllano parole chiave o schemi proibiti. Questi girano nel tuo codice, quindi sono veloci e gratuiti.

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

**Parte 2: Filtri di Sicurezza del Provider** - GitHub Models ha filtri integrati che catturano ciò che le tue guardrails potrebbero perdere. Vedrai blocchi rigidi (errori HTTP 400) per violazioni gravi e rifiuti soft dove l’AI declina educatamente.

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) e chiedi:
> - "Cos’è InputGuardrail e come ne creo uno mio?"
> - "Qual è la differenza tra un blocco rigido e un rifiuto soft?"
> - "Perché usare insieme guardrails e filtri del provider?"

## Prossimi Passi

**Prossimo Modulo:** [01-introduction - Iniziare con LangChain4j e gpt-5 su Azure](../01-introduction/README.md)

---

**Navigazione:** [← Torna al Principale](../README.md) | [Avanti: Modulo 01 - Introduzione →](../01-introduction/README.md)

---

## Risoluzione dei Problemi

### Primo Build Maven

**Problema**: Il primo `mvn clean compile` o `mvn package` impiega molto tempo (10-15 minuti)

**Causa**: Maven deve scaricare tutte le dipendenze di progetto (Spring Boot, librerie LangChain4j, SDK Azure, ecc.) al primo build.

**Soluzione**: Questo è comportamento normale. I build successivi saranno molto più veloci perché le dipendenze sono memorizzate localmente. Il tempo di download dipende dalla velocità della tua rete.
### Sintassi del comando Maven in PowerShell

**Problema**: I comandi Maven falliscono con errore `Unknown lifecycle phase ".mainClass=..."`

**Causa**: PowerShell interpreta `=` come operatore di assegnazione di variabile, interrompendo la sintassi delle proprietà di Maven

**Soluzione**: Usa l'operatore di stop-parsing `--%` prima del comando Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

L'operatore `--%` dice a PowerShell di passare tutti gli argomenti rimanenti letteralmente a Maven senza interpretarli.

### Visualizzazione delle emoji in Windows PowerShell

**Problema**: Le risposte AI mostrano caratteri illeggibili (es. `????` o `â??`) invece delle emoji in PowerShell

**Causa**: La codifica predefinita di PowerShell non supporta le emoji UTF-8

**Soluzione**: Esegui questo comando prima di avviare le applicazioni Java:
```cmd
chcp 65001
```

Questo forza la codifica UTF-8 nel terminale. In alternativa, usa Windows Terminal che ha un supporto migliore per Unicode.

### Debug delle chiamate API

**Problema**: Errori di autenticazione, limiti di velocità o risposte inaspettate dal modello AI

**Soluzione**: Gli esempi includono `.logRequests(true)` e `.logResponses(true)` per mostrare le chiamate API nella console. Questo aiuta a risolvere errori di autenticazione, limiti di velocità o risposte inaspettate. Rimuovi questi flag in produzione per ridurre il rumore nei log.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avvertenza**:  
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Pur impegnandoci per l’accuratezza, si prega di notare che le traduzioni automatiche possono contenere errori o imprecisioni. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale effettuata da un umano. Non siamo responsabili per eventuali fraintendimenti o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->