# Modulo 01: Iniziare con LangChain4j

## Indice

- [Cosa Imparerai](../../../01-introduction)
- [Prerequisiti](../../../01-introduction)
- [Comprendere il Problema Principale](../../../01-introduction)
- [Comprendere i Token](../../../01-introduction)
- [Come Funziona la Memoria](../../../01-introduction)
- [Come Questo Usa LangChain4j](../../../01-introduction)
- [Distribuire l'Infrastruttura Azure OpenAI](../../../01-introduction)
- [Eseguire l'Applicazione Localmente](../../../01-introduction)
- [Utilizzare l'Applicazione](../../../01-introduction)
  - [Chat Stateless (Pannello Sinistro)](../../../01-introduction)
  - [Chat Stateful (Pannello Destro)](../../../01-introduction)
- [Passi Successivi](../../../01-introduction)

## Cosa Imparerai

Se hai completato il quick start, hai visto come inviare prompt e ricevere risposte. Questa è la base, ma le applicazioni reali hanno bisogno di più. Questo modulo ti insegna come costruire un'IA conversazionale che ricorda il contesto e mantiene lo stato - la differenza tra una demo una tantum e un'applicazione pronta per la produzione.

Utilizzeremo GPT-5.2 di Azure OpenAI in tutta la guida perché le sue capacità avanzate di ragionamento rendono più evidente il comportamento dei diversi pattern. Quando aggiungi la memoria, vedrai chiaramente la differenza. Questo rende più facile capire cosa ogni componente apporta alla tua applicazione.

Costruirai un'applicazione che dimostra entrambi i pattern:

**Chat Stateless** - Ogni richiesta è indipendente. Il modello non ha memoria dei messaggi precedenti. Questo è il pattern usato nel quick start.

**Conversazione Stateful** - Ogni richiesta include la cronologia della conversazione. Il modello mantiene il contesto attraverso più turni. Questo è ciò di cui hanno bisogno le applicazioni di produzione.

## Prerequisiti

- Sottoscrizione Azure con accesso Azure OpenAI
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Nota:** Java, Maven, Azure CLI e Azure Developer CLI (azd) sono preinstallati nel devcontainer fornito.

> **Nota:** Questo modulo utilizza GPT-5.2 su Azure OpenAI. La distribuzione è configurata automaticamente tramite `azd up` - non modificare il nome del modello nel codice.

## Comprendere il Problema Principale

I modelli linguistici sono stateless. Ogni chiamata API è indipendente. Se invii "Mi chiamo John" e poi chiedi "Come mi chiamo?", il modello non ha idea che ti sei appena presentato. Tratta ogni richiesta come se fosse la prima conversazione che hai mai avuto.

Questo va bene per semplici Q&A ma è inutile per applicazioni reali. I bot di assistenza clienti devono ricordare ciò che hai detto. Gli assistenti personali hanno bisogno di contesto. Qualsiasi conversazione a più turni richiede memoria.

<img src="../../../translated_images/it/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Conversazioni Stateless vs Stateful" width="800"/>

*La differenza tra conversazioni stateless (chiamate indipendenti) e stateful (consapevoli del contesto)*

## Comprendere i Token

Prima di entrare nelle conversazioni, è importante capire i token - le unità base di testo che i modelli linguistici elaborano:

<img src="../../../translated_images/it/token-explanation.c39760d8ec650181.webp" alt="Spiegazione dei Token" width="800"/>

*Esempio di come il testo viene suddiviso in token - "I love AI!" diventa 4 unità separate di elaborazione*

I token sono il modo in cui i modelli AI misurano e processano il testo. Parole, punteggiatura e persino spazi possono essere token. Il tuo modello ha un limite su quanti token può elaborare contemporaneamente (400.000 per GPT-5.2, con fino a 272.000 token in input e 128.000 in output). Capire i token ti aiuta a gestire la lunghezza della conversazione e i costi.

## Come Funziona la Memoria

La memoria nella chat risolve il problema dello stateless mantenendo la cronologia della conversazione. Prima di inviare la tua richiesta al modello, il framework antepone i messaggi precedenti rilevanti. Quando chiedi "Come mi chiamo?", il sistema in realtà invia tutta la cronologia della conversazione, permettendo al modello di vedere che hai detto "Mi chiamo John".

LangChain4j fornisce implementazioni di memoria che gestiscono questo automaticamente. Scegli quanti messaggi mantenere e il framework gestisce la finestra di contesto.

<img src="../../../translated_images/it/memory-window.bbe67f597eadabb3.webp" alt="Concetto di Finestra di Memoria" width="800"/>

*MessageWindowChatMemory mantiene una finestra scorrevole dei messaggi recenti, eliminando automaticamente quelli vecchi*

## Come Questo Usa LangChain4j

Questo modulo estende il quick start integrando Spring Boot e aggiungendo la memoria della conversazione. Ecco come i pezzi si incastrano:

**Dipendenze** - Aggiungi due librerie LangChain4j:

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

**Modello Chat** - Configura Azure OpenAI come bean Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

Il builder legge le credenziali dalle variabili d'ambiente impostate da `azd up`. Impostare `baseUrl` sul tuo endpoint Azure fa funzionare il client OpenAI con Azure OpenAI.

**Memoria della Conversazione** - Traccia la cronologia chat con MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Crea la memoria con `withMaxMessages(10)` per mantenere gli ultimi 10 messaggi. Aggiungi messaggi utente e AI con wrapper tipizzati: `UserMessage.from(text)` e `AiMessage.from(text)`. Recupera la cronologia con `memory.messages()` e inviala al modello. Il servizio memorizza istanze di memoria separate per ID conversazione, permettendo a più utenti di chattare simultaneamente.

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) e chiedi:
> - "Come decide MessageWindowChatMemory quali messaggi eliminare quando la finestra è piena?"
> - "Posso implementare una memorizzazione personalizzata usando un database invece della memoria in RAM?"
> - "Come potrei aggiungere un riassunto per comprimere la cronologia delle vecchie conversazioni?"

L’endpoint di chat stateless salta completamente la memoria - semplicemente `chatModel.chat(prompt)` come nel quick start. L’endpoint stateful aggiunge messaggi alla memoria, recupera la cronologia e include quel contesto ad ogni richiesta. Stessa configurazione modello, pattern diversi.

## Distribuire l'Infrastruttura Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Seleziona l'abbonamento e la posizione (consigliato eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Seleziona sottoscrizione e posizione (consigliato eastus2)
```

> **Nota:** Se incontri un errore di timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), semplicemente esegui di nuovo `azd up`. Le risorse Azure potrebbero essere ancora in provisioning in background e riprovare permette alla distribuzione di completarsi una volta che le risorse raggiungono uno stato terminale.

Questo farà:
1. Distribuire la risorsa Azure OpenAI con i modelli GPT-5.2 e text-embedding-3-small
2. Generare automaticamente il file `.env` nella root del progetto con le credenziali
3. Impostare tutte le variabili ambiente richieste

**Problemi con la distribuzione?** Consulta il [README dell'Infrastruttura](infra/README.md) per risoluzioni dettagliate, inclusi conflitti di nome del sottodominio, passaggi manuali per la distribuzione nel Portale Azure e guida alla configurazione del modello.

**Verifica che la distribuzione sia riuscita:**

**Bash:**
```bash
cat ../.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, ecc.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, ecc.
```

> **Nota:** Il comando `azd up` genera automaticamente il file `.env`. Se devi aggiornarlo in seguito, puoi modificare il file `.env` manualmente oppure rigenerarlo eseguendo:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## Eseguire l'Applicazione Localmente

**Verifica la distribuzione:**

Assicurati che il file `.env` esista nella directory root con le credenziali Azure:

**Bash:**
```bash
cat ../.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Avvia le applicazioni:**

**Opzione 1: Usare Spring Boot Dashboard (Consigliato per utenti VS Code)**

Il dev container include l’estensione Spring Boot Dashboard, che fornisce un’interfaccia visiva per gestire tutte le applicazioni Spring Boot. La puoi trovare nella Activity Bar a sinistra in VS Code (cerca l’icona Spring Boot).

Dal Spring Boot Dashboard puoi:
- Vedere tutte le applicazioni Spring Boot disponibili nell’ambiente di lavoro
- Avviare/fermare le applicazioni con un solo click
- Visualizzare i log dell’applicazione in tempo reale
- Monitorare lo stato delle applicazioni

Clicca semplicemente il pulsante play accanto a "introduction" per avviare questo modulo, oppure avvia tutti i moduli contemporaneamente.

<img src="../../../translated_images/it/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Opzione 2: Usare script shell**

Avvia tutte le applicazioni web (moduli 01-04):

**Bash:**
```bash
cd ..  # Dalla directory principale
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
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Entrambi gli script caricano automaticamente le variabili ambiente dal file `.env` in root e compileranno i JAR se non esistono.

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

Apri http://localhost:8080 nel browser.

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

## Utilizzare l'Applicazione

L’applicazione offre un’interfaccia web con due implementazioni di chat affiancate.

<img src="../../../translated_images/it/home-screen.121a03206ab910c0.webp" alt="Schermata Home Applicazione" width="800"/>

*Dashboard che mostra le opzioni Chat Semplice (stateless) e Chat Conversazionale (stateful)*

### Chat Stateless (Pannello Sinistro)

Prova prima questo. Chiedi "Mi chiamo John" e poi subito "Come mi chiamo?" Il modello non ricorderà perché ogni messaggio è indipendente. Questo dimostra il problema principale dell'integrazione base del modello linguistico - nessun contesto di conversazione.

<img src="../../../translated_images/it/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo Chat Stateless" width="800"/>

*L’IA non ricorda il tuo nome dal messaggio precedente*

### Chat Stateful (Pannello Destro)

Ora prova la stessa sequenza qui. Chiedi "Mi chiamo John" e poi "Come mi chiamo?" Questa volta lo ricorda. La differenza è MessageWindowChatMemory - mantiene la cronologia della conversazione e la include in ogni richiesta. Questo è come funziona l’IA conversazionale in produzione.

<img src="../../../translated_images/it/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo Chat Stateful" width="800"/>

*L’IA ricorda il tuo nome detto prima nella conversazione*

Entrambi i pannelli usano lo stesso modello GPT-5.2. L’unica differenza è la memoria. Questo rende chiaro cosa la memoria apporta alla tua applicazione e perché è essenziale per i casi d’uso reali.

## Passi Successivi

**Prossimo Modulo:** [02-prompt-engineering - Ingegneria del Prompt con GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigazione:** [← Precedente: Modulo 00 - Quick Start](../00-quick-start/README.md) | [Torna al Principale](../README.md) | [Successivo: Modulo 02 - Ingegneria del Prompt →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Questo documento è stato tradotto utilizzando il servizio di traduzione AI [Co-op Translator](https://github.com/Azure/co-op-translator). Pur impegnandoci per l'accuratezza, si prega di notare che le traduzioni automatiche potrebbero contenere errori o inesattezze. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche, è consigliata una traduzione professionale umana. Non siamo responsabili per eventuali malintesi o interpretazioni errate derivanti dall'uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->