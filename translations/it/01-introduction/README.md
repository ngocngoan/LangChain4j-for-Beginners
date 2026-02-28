# Modulo 01: Iniziare con LangChain4j

## Indice

- [Video Guida](../../../01-introduction)
- [Cosa Imparerai](../../../01-introduction)
- [Prerequisiti](../../../01-introduction)
- [Comprendere il Problema Principale](../../../01-introduction)
- [Comprendere i Token](../../../01-introduction)
- [Come Funziona la Memoria](../../../01-introduction)
- [Come Usa LangChain4j](../../../01-introduction)
- [Distribuire l’Infrastruttura Azure OpenAI](../../../01-introduction)
- [Eseguire l’Applicazione in Locale](../../../01-introduction)
- [Usare l’Applicazione](../../../01-introduction)
  - [Chat Senza Stato (Pannello Sinistro)](../../../01-introduction)
  - [Chat Con Stato (Pannello Destro)](../../../01-introduction)
- [Prossimi Passi](../../../01-introduction)

## Video Guida

Guarda questa sessione live che spiega come iniziare con questo modulo: [Getting Started with LangChain4j - Live Session](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Cosa Imparerai

Se hai completato la guida rapida, hai visto come inviare prompt e ottenere risposte. Questa è la base, ma le applicazioni reali richiedono di più. Questo modulo ti insegna come costruire un’IA conversazionale che ricorda il contesto e mantiene lo stato - la differenza tra una demo occasionale e un’applicazione pronta per la produzione.

Useremo GPT-5.2 di Azure OpenAI in tutta questa guida perché le sue capacità avanzate di ragionamento rendono più evidenti i comportamenti dei diversi pattern. Quando aggiungi la memoria, vedrai chiaramente la differenza. Questo rende più facile capire cosa ogni componente porta alla tua applicazione.

Costruirai un’applicazione che dimostra entrambi i pattern:

**Chat Senza Stato** - Ogni richiesta è indipendente. Il modello non ricorda i messaggi precedenti. Questo è il pattern usato nella guida rapida.

**Conversazione Con Stato** - Ogni richiesta include la cronologia della conversazione. Il modello mantiene il contesto su più turni. Questo è ciò che richiedono le applicazioni di produzione.

## Prerequisiti

- Sottoscrizione Azure con accesso a Azure OpenAI
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Nota:** Java, Maven, Azure CLI e Azure Developer CLI (azd) sono preinstallati nel devcontainer fornito.

> **Nota:** Questo modulo usa GPT-5.2 su Azure OpenAI. Il deployment è configurato automaticamente tramite `azd up` - non modificare il nome del modello nel codice.

## Comprendere il Problema Principale

I modelli di linguaggio sono senza stato. Ogni chiamata API è indipendente. Se invii "Mi chiamo John" e poi chiedi "Come mi chiamo?", il modello non ha idea che ti sei appena presentato. Tratta ogni richiesta come se fosse la prima conversazione che hai mai avuto.

Questo va bene per domande e risposte semplici, ma è inutile per applicazioni reali. I bot del servizio clienti devono ricordare cosa hai detto. Gli assistenti personali hanno bisogno di contesto. Qualsiasi conversazione multi-turno richiede memoria.

<img src="../../../translated_images/it/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Conversazioni senza stato vs con stato" width="800"/>

*La differenza tra conversazioni senza stato (chiamate indipendenti) e con stato (consapevoli del contesto)*

## Comprendere i Token

Prima di immergersi nelle conversazioni, è importante capire i token - le unità base di testo che i modelli di linguaggio processano:

<img src="../../../translated_images/it/token-explanation.c39760d8ec650181.webp" alt="Spiegazione dei Token" width="800"/>

*Esempio di come il testo viene suddiviso in token - "I love AI!" diventa 4 unità separate di elaborazione*

I token sono come i modelli AI misurano ed elaborano il testo. Parole, punteggiatura e persino spazi possono essere token. Il tuo modello ha un limite di quanti token può processare contemporaneamente (400.000 per GPT-5.2, con fino a 272.000 token di input e 128.000 token di output). Capire i token ti aiuta a gestire la lunghezza delle conversazioni e i costi.

## Come Funziona la Memoria

La memoria chat risolve il problema dello stato mantenendo la cronologia della conversazione. Prima di inviare la tua richiesta al modello, il framework aggiunge prima i messaggi precedenti rilevanti. Quando chiedi "Come mi chiamo?", il sistema in realtà invia tutta la cronologia della conversazione, permettendo al modello di vedere che avevi detto "Mi chiamo John".

LangChain4j fornisce implementazioni di memoria che gestiscono questo automaticamente. Scegli quanti messaggi conservare e il framework gestisce la finestra contestuale.

<img src="../../../translated_images/it/memory-window.bbe67f597eadabb3.webp" alt="Concetto di Finestra di Memoria" width="800"/>

*MessageWindowChatMemory mantiene una finestra scorrevole degli ultimi messaggi, eliminando automaticamente i più vecchi*

## Come Usa LangChain4j

Questo modulo estende la guida rapida integrando Spring Boot e aggiungendo la memoria per la conversazione. Ecco come si collegano i pezzi:

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

Il builder legge le credenziali dalle variabili d’ambiente impostate da `azd up`. Impostare `baseUrl` al tuo endpoint Azure fa funzionare il client OpenAI con Azure OpenAI.

**Memoria Conversazione** - Traccia la cronologia chat con MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Crea la memoria con `withMaxMessages(10)` per conservare gli ultimi 10 messaggi. Aggiungi messaggi utente e AI con wrapper tipizzati: `UserMessage.from(text)` e `AiMessage.from(text)`. Recupera la cronologia con `memory.messages()` e inviala al modello. Il servizio memorizza istanze di memoria separate per ID conversazione, consentendo a più utenti di chattare contemporaneamente.

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) e chiedi:
> - "Come decide MessageWindowChatMemory quali messaggi eliminare quando la finestra è piena?"
> - "Posso implementare una memoria personalizzata usando un database invece della memoria in RAM?"
> - "Come aggiungerei il sommario per comprimere la cronologia delle conversazioni vecchie?"

L’endpoint chat senza stato salta completamente la memoria - solo `chatModel.chat(prompt)` come nella guida rapida. L’endpoint con stato aggiunge messaggi alla memoria, recupera la cronologia e include quel contesto in ogni richiesta. Stessa configurazione modello, pattern diversi.

## Distribuire l’Infrastruttura Azure OpenAI

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

> **Nota:** Se incontri un errore di timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), esegui semplicemente di nuovo `azd up`. Le risorse Azure potrebbero essere ancora in fase di provisioning in background, e riprovare consente al deployment di completarsi una volta che le risorse raggiungono uno stato finale.

Questo farà:
1. Distribuire la risorsa Azure OpenAI con i modelli GPT-5.2 e text-embedding-3-small
2. Generare automaticamente il file `.env` nella root del progetto con le credenziali
3. Impostare tutte le variabili d’ambiente richieste

**Problemi con il deployment?** Vedi il [README dell’infrastruttura](infra/README.md) per la risoluzione dettagliata, inclusi conflitti di nomi di sottodominio, passaggi manuali per il deployment da Azure Portal e indicazioni per la configurazione del modello.

**Verifica che il deployment sia riuscito:**

**Bash:**
```bash
cat ../.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, ecc.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, ecc.
```

> **Nota:** Il comando `azd up` genera automaticamente il file `.env`. Se devi aggiornarlo in seguito, puoi modificare manualmente il file `.env` oppure rigenerarlo eseguendo:
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

## Eseguire l’Applicazione in Locale

**Verifica il deployment:**

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

Il dev container include l’estensione Spring Boot Dashboard, che fornisce un’interfaccia visiva per gestire tutte le applicazioni Spring Boot. Lo trovi nella Activity Bar sul lato sinistro di VS Code (cerca l'icona Spring Boot).

Da Spring Boot Dashboard puoi:
- Visualizzare tutte le applicazioni Spring Boot disponibili nell’area di lavoro
- Avviare/fermare le applicazioni con un solo clic
- Visualizzare i log delle applicazioni in tempo reale
- Monitorare lo stato delle applicazioni

Clicca semplicemente il bottone play accanto a "introduction" per avviare questo modulo, oppure avvia tutti i moduli insieme.

<img src="../../../translated_images/it/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Opzione 2: Usare script da shell**

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
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Entrambi gli script caricano automaticamente le variabili d’ambiente dal file `.env` nella root e compileranno i JAR se non esistono.

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

Apri http://localhost:8080 nel tuo browser.

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

## Usare l’Applicazione

L’applicazione fornisce un’interfaccia web con due implementazioni di chat affiancate.

<img src="../../../translated_images/it/home-screen.121a03206ab910c0.webp" alt="Schermata Home dell’Applicazione" width="800"/>

*Dashboard che mostra sia Simple Chat (senza stato) che Conversational Chat (con stato)*

### Chat Senza Stato (Pannello Sinistro)

Prova prima questa. Chiedi "Mi chiamo John" e poi subito dopo "Come mi chiamo?" Il modello non ricorderà perché ogni messaggio è indipendente. Questo dimostra il problema principale con l’integrazione base dei modelli linguistici - nessun contesto di conversazione.

<img src="../../../translated_images/it/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo Chat Senza Stato" width="800"/>

*L’IA non ricorda il tuo nome dal messaggio precedente*

### Chat Con Stato (Pannello Destro)

Ora prova la stessa sequenza qui. Chiedi "Mi chiamo John" e poi "Come mi chiamo?" Questa volta ricorda. La differenza è MessageWindowChatMemory - mantiene la cronologia della conversazione e la include con ogni richiesta. Ecco come funziona l’IA conversazionale in produzione.

<img src="../../../translated_images/it/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo Chat Con Stato" width="800"/>

*L’IA ricorda il tuo nome precedente durante la conversazione*

Entrambi i pannelli usano lo stesso modello GPT-5.2. L’unica differenza è la memoria. Questo rende chiaro cosa la memoria porta alla tua applicazione e perché è essenziale per i casi d’uso reali.

## Prossimi Passi

**Modulo Successivo:** [02-prompt-engineering - Engineering dei Prompt con GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigazione:** [← Precedente: Modulo 00 - Guida Rapida](../00-quick-start/README.md) | [Torna al Principale](../README.md) | [Successivo: Modulo 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Pur impegnandoci per garantire precisione, si prega di considerare che le traduzioni automatiche possono contenere errori o inesattezze. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche si raccomanda la traduzione professionale effettuata da un umano. Non ci assumiamo responsabilità per eventuali incomprensioni o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->