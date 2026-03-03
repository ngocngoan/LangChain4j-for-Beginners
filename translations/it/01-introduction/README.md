# Modulo 01: Iniziare con LangChain4j

## Indice

- [Video Dimostrativo](../../../01-introduction)
- [Cosa Imparerai](../../../01-introduction)
- [Prerequisiti](../../../01-introduction)
- [Comprendere il Problema Principale](../../../01-introduction)
- [Comprendere i Token](../../../01-introduction)
- [Come Funziona la Memoria](../../../01-introduction)
- [Come Viene Usato LangChain4j](../../../01-introduction)
- [Distribuire l'Infrastruttura Azure OpenAI](../../../01-introduction)
- [Eseguire l'Applicazione Localmente](../../../01-introduction)
- [Usare l'Applicazione](../../../01-introduction)
  - [Chat Senza Stato (Pannello Sinistro)](../../../01-introduction)
  - [Chat Con Stato (Pannello Destro)](../../../01-introduction)
- [Prossimi Passi](../../../01-introduction)

## Video Dimostrativo

Guarda questa sessione live che spiega come iniziare con questo modulo:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Cosa Imparerai

Nel quick start, hai usato i Modelli GitHub per inviare prompt, chiamare strumenti, costruire una pipeline RAG e testare le protezioni. Quelle demo hanno mostrato cosa è possibile — ora passiamo ad Azure OpenAI e GPT-5.2 e iniziamo a costruire applicazioni in stile produzione. Questo modulo si concentra sull'AI conversazionale che ricorda il contesto e mantiene lo stato — i concetti che quelle demo del quick start usavano dietro le quinte ma non spiegavano.

Useremo GPT-5.2 di Azure OpenAI in tutta la guida perché le sue capacità avanzate di ragionamento rendono il comportamento dei diversi pattern più evidente. Quando aggiungi la memoria, vedrai chiaramente la differenza. Questo rende più facile capire cosa ogni componente apporta alla tua applicazione.

Costruirai un'applicazione che dimostra entrambi i pattern:

**Chat Senza Stato** - Ogni richiesta è indipendente. Il modello non ha memoria dei messaggi precedenti. Questo è il pattern usato nel quick start.

**Conversazione Con Stato** - Ogni richiesta include la cronologia della conversazione. Il modello mantiene il contesto su più turni. Questo è ciò che le applicazioni di produzione richiedono.

## Prerequisiti

- Sottoscrizione Azure con accesso ad Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Nota:** Java, Maven, Azure CLI e Azure Developer CLI (azd) sono preinstallati nel devcontainer fornito.

> **Nota:** Questo modulo usa GPT-5.2 su Azure OpenAI. La distribuzione è configurata automaticamente tramite `azd up` - non modificare il nome del modello nel codice.

## Comprendere il Problema Principale

I modelli di linguaggio sono senza stato. Ogni chiamata API è indipendente. Se invii "Mi chiamo John" e poi chiedi "Come mi chiamo?", il modello non ha idea che tu ti sia appena presentato. Tratta ogni richiesta come se fosse la prima conversazione che hai mai avuto.

Questo va bene per semplici Q&A ma è inutile per applicazioni reali. I bot del servizio clienti devono ricordare cosa hai detto loro. Gli assistenti personali hanno bisogno di contesto. Qualsiasi conversazione multi-turno richiede memoria.

Il diagramma seguente mette a confronto i due approcci — a sinistra, una chiamata senza stato che dimentica il tuo nome; a destra, una chiamata con stato supportata da ChatMemory che lo ricorda.

<img src="../../../translated_images/it/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*La differenza tra conversazioni senza stato (chiamate indipendenti) e con stato (consapevoli del contesto)*

## Comprendere i Token

Prima di immergersi nelle conversazioni, è importante comprendere i token - le unità di base del testo che i modelli di linguaggio elaborano:

<img src="../../../translated_images/it/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Esempio di come il testo viene suddiviso in token - "I love AI!" diventa 4 unità di elaborazione separate*

I token sono il modo in cui i modelli AI misurano ed elaborano il testo. Parole, punteggiatura e persino spazi possono essere token. Il tuo modello ha un limite sul numero di token che può elaborare in una volta (400.000 per GPT-5.2, con fino a 272.000 token in input e 128.000 in output). Comprendere i token ti aiuta a gestire la lunghezza della conversazione e i costi.

## Come Funziona la Memoria

La memoria nella chat risolve il problema dello stato mantenendo la cronologia della conversazione. Prima di inviare la tua richiesta al modello, il framework antepone i messaggi precedenti rilevanti. Quando chiedi "Come mi chiamo?", il sistema in realtà invia l'intera cronologia della conversazione, permettendo al modello di vedere che hai detto "Mi chiamo John" in precedenza.

LangChain4j fornisce implementazioni di memoria che gestiscono questo automaticamente. Scegli quanti messaggi conservare e il framework gestisce la finestra di contesto. Il diagramma sotto mostra come MessageWindowChatMemory mantiene una finestra scorrevole dei messaggi recenti.

<img src="../../../translated_images/it/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory mantiene una finestra scorrevole dei messaggi recenti, eliminandone automaticamente i più vecchi*

## Come Viene Usato LangChain4j

Questo modulo estende il quick start integrando Spring Boot e aggiungendo la memoria nella conversazione. Ecco come si incastrano i pezzi:

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

Il builder legge le credenziali dalle variabili d'ambiente impostate da `azd up`. Impostare `baseUrl` al tuo endpoint Azure fa funzionare il client OpenAI con Azure OpenAI.

**Memoria della Conversazione** - Traccia la cronologia chat con MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Crea la memoria con `withMaxMessages(10)` per tenere gli ultimi 10 messaggi. Aggiungi messaggi utente e AI con wrapper tipizzati: `UserMessage.from(text)` e `AiMessage.from(text)`. Recupera la cronologia con `memory.messages()` e inviala al modello. Il servizio memorizza istanze di memoria separate per ID conversazione, consentendo a più utenti di chattare contemporaneamente.

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) e chiedi:
> - "Come decide MessageWindowChatMemory quali messaggi eliminare quando la finestra è piena?"
> - "Posso implementare una memoria personalizzata usando un database invece che in memoria?"
> - "Come potrei aggiungere una sintesi per comprimere la vecchia cronologia della conversazione?"

L'endpoint chat senza stato salta completamente la memoria - usa soltanto `chatModel.chat(prompt)` come nel quick start. L'endpoint con stato aggiunge messaggi alla memoria, recupera la cronologia e include quel contesto ad ogni richiesta. Stessa configurazione del modello, pattern differenti.

## Distribuire l'Infrastruttura Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Seleziona sottoscrizione e posizione (eastus2 consigliato)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Seleziona l'abbonamento e la posizione (consigliato eastus2)
```

> **Nota:** Se riscontri un errore di timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), esegui semplicemente di nuovo `azd up`. Le risorse Azure potrebbero essere ancora in provisioning in background, e riprovare consente alla distribuzione di completarsi una volta che le risorse raggiungono uno stato terminale.

Questo farà:
1. Distribuire la risorsa Azure OpenAI con modelli GPT-5.2 e text-embedding-3-small
2. Generare automaticamente il file `.env` nella root del progetto con le credenziali
3. Impostare tutte le variabili di ambiente necessarie

**Problemi nella distribuzione?** Consulta il [README dell'Infrastruttura](infra/README.md) per una risoluzione dettagliata dei problemi inclusi conflitti di nomi di sottodomini, passaggi manuali di distribuzione tramite Azure Portal e linee guida sulla configurazione del modello.

**Verifica che la distribuzione sia riuscita:**

**Bash:**
```bash
cat ../.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, ecc.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, ecc.
```

> **Nota:** Il comando `azd up` genera automaticamente il file `.env`. Se devi aggiornarlo dopo, puoi modificare direttamente il file `.env` oppure rigenerarlo eseguendo:
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

Assicurati che il file `.env` esista nella directory root con le credenziali Azure. Esegui questo dalla directory del modulo (`01-introduction/`):

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

Il dev container include l'estensione Spring Boot Dashboard, che fornisce un'interfaccia visiva per gestire tutte le applicazioni Spring Boot. La trovi nella Activity Bar sul lato sinistro di VS Code (cerca l'icona di Spring Boot).

Dal Spring Boot Dashboard puoi:
- Vedere tutte le applicazioni Spring Boot disponibili nell'area di lavoro
- Avviare/arrestare applicazioni con un clic
- Visualizzare i log delle applicazioni in tempo reale
- Monitorare lo stato delle applicazioni

Clicca semplicemente il pulsante play accanto a "introduction" per avviare questo modulo, oppure avvia tutti i moduli insieme.

<img src="../../../translated_images/it/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Il Spring Boot Dashboard in VS Code — avvia, arresta e monitora tutti i moduli da un unico posto*

**Opzione 2: Usare script shell**

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
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Entrambi gli script caricano automaticamente le variabili d'ambiente dal file `.env` della root e compileranno i JAR se non esistono.

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

Apri http://localhost:8080 nel tuo browser.

**Per fermare:**

**Bash:**
```bash
./stop.sh  # Solo questo modulo
# Oppure
cd .. && ./stop-all.sh  # Tutti i moduli
```

**PowerShell:**
```powershell
.\stop.ps1  # Solo questo modulo
# O
cd ..; .\stop-all.ps1  # Tutti i moduli
```

## Usare l'Applicazione

L'applicazione fornisce un'interfaccia web con due implementazioni di chat affiancate.

<img src="../../../translated_images/it/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard che mostra entrambe le opzioni Simple Chat (senza stato) e Conversational Chat (con stato)*

### Chat Senza Stato (Pannello Sinistro)

Prova prima questa. Chiedi "Mi chiamo John" e poi subito dopo chiedi "Come mi chiamo?" Il modello non ricorderà perché ogni messaggio è indipendente. Questo dimostra il problema principale con l'integrazione base dei modelli di linguaggio - nessun contesto di conversazione.

<img src="../../../translated_images/it/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*L'AI non ricorda il tuo nome dal messaggio precedente*

### Chat Con Stato (Pannello Destro)

Ora prova la stessa sequenza qui. Chiedi "Mi chiamo John" e poi "Come mi chiamo?" Questa volta lo ricorda. La differenza è MessageWindowChatMemory - mantiene la cronologia della conversazione e la include ad ogni richiesta. Così funziona l'AI conversazionale in produzione.

<img src="../../../translated_images/it/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*L'AI ricorda il tuo nome da prima nella conversazione*

Entrambi i pannelli usano lo stesso modello GPT-5.2. L'unica differenza è la memoria. Questo rende chiaro cosa la memoria apporta alla tua applicazione e perché è essenziale per casi d'uso reali.

## Prossimi Passi

**Modulo Successivo:** [02-prompt-engineering - Prompt Engineering con GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigazione:** [← Precedente: Modulo 00 - Quick Start](../00-quick-start/README.md) | [Indietro al Principale](../README.md) | [Successivo: Modulo 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avvertenza**:  
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Pur facendo del nostro meglio per garantire l’accuratezza, si prega di considerare che le traduzioni automatiche possono contenere errori o inesattezze. Il documento originale nella sua lingua natale deve essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale effettuata da un traduttore umano. Non siamo responsabili per eventuali malintesi o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->