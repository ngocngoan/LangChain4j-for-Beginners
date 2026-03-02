# Modulo 01: Introduzione a LangChain4j

## Indice

- [Video Dimostrativo](../../../01-introduction)
- [Cosa Imparerai](../../../01-introduction)
- [Prerequisiti](../../../01-introduction)
- [Comprendere il Problema Centrale](../../../01-introduction)
- [Comprendere i Token](../../../01-introduction)
- [Come Funziona la Memoria](../../../01-introduction)
- [Come Questo Usa LangChain4j](../../../01-introduction)
- [Distribuire l'Infrastruttura Azure OpenAI](../../../01-introduction)
- [Esegui l'Applicazione in Locale](../../../01-introduction)
- [Usare l'Applicazione](../../../01-introduction)
  - [Chat Senza Stato (Pannello Sinistro)](../../../01-introduction)
  - [Chat Con Stato (Pannello Destro)](../../../01-introduction)
- [Passi Successivi](../../../01-introduction)

## Video Dimostrativo

Guarda questa sessione live che spiega come iniziare con questo modulo:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Inizio con LangChain4j - Sessione Live" width="800"/></a>

## Cosa Imparerai

Se hai completato il quick start, hai visto come inviare prompt e ottenere risposte. Questa è la base, ma le applicazioni reali hanno bisogno di più. Questo modulo ti insegna come costruire un'IA conversazionale che ricorda il contesto e mantiene lo stato - la differenza tra una demo occasionale e un'applicazione pronta per la produzione.

Useremo GPT-5.2 di Azure OpenAI in tutta la guida perché le sue avanzate capacità di ragionamento rendono più evidente il comportamento dei diversi modelli. Quando aggiungi la memoria, vedrai chiaramente la differenza. Questo rende più facile capire cosa ciascun componente porta alla tua applicazione.

Costruirai un'applicazione che dimostra entrambi i modelli:

**Chat Senza Stato** - Ogni richiesta è indipendente. Il modello non ha memoria dei messaggi precedenti. Questo è il modello che hai usato nel quick start.

**Conversazione Con Stato** - Ogni richiesta include la cronologia della conversazione. Il modello mantiene il contesto su più interazioni. Questo è ciò che richiedono le applicazioni in produzione.

## Prerequisiti

- Abbonamento Azure con accesso a Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Nota:** Java, Maven, Azure CLI e Azure Developer CLI (azd) sono preinstallati nel devcontainer fornito.

> **Nota:** Questo modulo utilizza GPT-5.2 su Azure OpenAI. Il deployment viene configurato automaticamente tramite `azd up` - non modificare il nome del modello nel codice.

## Comprendere il Problema Centrale

I modelli linguistici sono senza stato. Ogni chiamata API è indipendente. Se invii "Mi chiamo John" e poi chiedi "Come mi chiamo?", il modello non ha idea che tu ti sia appena presentato. Considera ogni richiesta come se fosse la prima conversazione che hai mai avuto.

Questo va bene per Q&A semplici ma è inutile per applicazioni reali. I bot di assistenza clienti devono ricordare cosa hai detto. Gli assistenti personali hanno bisogno di contesto. Qualsiasi conversazione multi-turno richiede memoria.

<img src="../../../translated_images/it/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Conversazioni Senza Stato vs Con Stato" width="800"/>

*La differenza tra conversazioni senza stato (chiamate indipendenti) e con stato (consapevoli del contesto)*

## Comprendere i Token

Prima di entrare nelle conversazioni, è importante capire cosa sono i token - le unità base di testo che i modelli linguistici processano:

<img src="../../../translated_images/it/token-explanation.c39760d8ec650181.webp" alt="Spiegazione del Token" width="800"/>

*Esempio di come il testo viene suddiviso in token - "Amo l'IA!" diventa 4 unità separate di elaborazione*

I token sono come i modelli AI misurano e processano il testo. Parole, punteggiatura e persino spazi possono essere token. Il tuo modello ha un limite di quanti token può processare contemporaneamente (400.000 per GPT-5.2, con fino a 272.000 token in input e 128.000 in output). Comprendere i token ti aiuta a gestire la lunghezza della conversazione e i costi.

## Come Funziona la Memoria

La memoria chat risolve il problema dello stato mantenendo la cronologia della conversazione. Prima di inviare la richiesta al modello, il framework antepone i messaggi precedenti rilevanti. Quando chiedi "Come mi chiamo?", il sistema invia in realtà tutta la cronologia della conversazione, permettendo al modello di vedere che in precedenza hai detto "Mi chiamo John."

LangChain4j fornisce implementazioni di memoria che gestiscono questo automaticamente. Scegli quanti messaggi conservare e il framework gestisce la finestra di contesto.

<img src="../../../translated_images/it/memory-window.bbe67f597eadabb3.webp" alt="Concetto di Finestra di Memoria" width="800"/>

*MessageWindowChatMemory mantiene una finestra mobile degli ultimi messaggi, eliminando automaticamente quelli più vecchi*

## Come Questo Usa LangChain4j

Questo modulo estende il quick start integrando Spring Boot e aggiungendo la memoria conversazionale. Ecco come si combinano i pezzi:

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

Il builder legge le credenziali dalle variabili di ambiente impostate da `azd up`. Impostare `baseUrl` all'endpoint Azure permette al client OpenAI di funzionare con Azure OpenAI.

**Memoria Conversazione** - Traccia la cronologia della chat con MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Crea memoria con `withMaxMessages(10)` per mantenere gli ultimi 10 messaggi. Aggiungi messaggi utente e AI con wrapper tipizzati: `UserMessage.from(text)` e `AiMessage.from(text)`. Recupera la cronologia con `memory.messages()` e inviala al modello. Il servizio memorizza istanze di memoria separate per ID conversazione, permettendo a più utenti di chattare contemporaneamente.

> **🤖 Prova con la Chat di [GitHub Copilot](https://github.com/features/copilot):** Apri [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) e chiedi:
> - "Come MessageWindowChatMemory decide quali messaggi eliminare quando la finestra è piena?"
> - "Posso implementare una memoria personalizzata usando un database invece che in memoria?"
> - "Come aggiungere una sintesi per comprimere la cronologia delle vecchie conversazioni?"

L'endpoint di chat senza stato ignora completamente la memoria - solo `chatModel.chat(prompt)` come nel quick start. L'endpoint con stato aggiunge messaggi alla memoria, recupera la cronologia e include quel contesto in ogni richiesta. Stessa configurazione del modello, modelli diversi.

## Distribuire l'Infrastruttura Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Seleziona sottoscrizione e posizione (eastus2 consigliata)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Seleziona abbonamento e posizione (consigliato eastus2)
```

> **Nota:** Se incontri un errore di timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), esegui semplicemente di nuovo `azd up`. Le risorse Azure potrebbero essere ancora in fase di provisioning in background, e riprovare permette al deployment di completarsi una volta che le risorse raggiungono uno stato terminale.

Questo farà:
1. Deploy della risorsa Azure OpenAI con modelli GPT-5.2 e text-embedding-3-small
2. Generazione automatica del file `.env` nella root del progetto con le credenziali
3. Impostazione di tutte le variabili d'ambiente richieste

**Problemi con il deployment?** Consulta il [README dell'infrastruttura](infra/README.md) per dettagliata risoluzione problemi tra cui conflitti di nomi di sottodomini, passaggi manuali di deployment nel portale Azure e guida alla configurazione del modello.

**Verifica che il deployment sia riuscito:**

**Bash:**
```bash
cat ../.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, ecc.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, ecc.
```

> **Nota:** Il comando `azd up` genera automaticamente il file `.env`. Se devi aggiornarlo in seguito, puoi modificarlo manualmente o rigenerarlo eseguendo:
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

## Esegui l'Applicazione in Locale

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

Il dev container include l'estensione Spring Boot Dashboard, che offre un'interfaccia visiva per gestire tutte le applicazioni Spring Boot. La trovi nella Activity Bar sul lato sinistro di VS Code (cerca l'icona di Spring Boot).

Dal Spring Boot Dashboard puoi:
- Vedere tutte le applicazioni Spring Boot disponibili nell'ambiente di lavoro
- Avviare/fermare le applicazioni con un solo click
- Visualizzare i log delle applicazioni in tempo reale
- Monitorare lo stato dell'applicazione

Clicca il pulsante play accanto a "introduction" per avviare questo modulo, o avvia tutti i moduli insieme.

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

Entrambi gli script caricano automaticamente le variabili d'ambiente dal file `.env` della root e compileranno i JAR se non esistono.

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

## Usare l'Applicazione

L'applicazione offre un'interfaccia web con due implementazioni di chat affiancate.

<img src="../../../translated_images/it/home-screen.121a03206ab910c0.webp" alt="Schermata Home dell'Applicazione" width="800"/>

*Dashboard che mostra sia la Simple Chat (senza stato) che la Conversational Chat (con stato)*

### Chat Senza Stato (Pannello Sinistro)

Prova prima questa. Chiedi "Mi chiamo John" e subito dopo "Come mi chiamo?" Il modello non ricorderà perché ogni messaggio è indipendente. Questo dimostra il problema centrale dell'integrazione base con modelli linguistici - nessun contesto di conversazione.

<img src="../../../translated_images/it/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo Chat Senza Stato" width="800"/>

*L'IA non ricorda il tuo nome dal messaggio precedente*

### Chat Con Stato (Pannello Destro)

Ora prova la stessa sequenza qui. Chiedi "Mi chiamo John" e poi "Come mi chiamo?" Questa volta ricorda. La differenza è MessageWindowChatMemory - mantiene la cronologia della conversazione e la include in ogni richiesta. È così che funziona l'IA conversazionale in produzione.

<img src="../../../translated_images/it/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo Chat Con Stato" width="800"/>

*L'IA ricorda il tuo nome dalle precedenti conversazioni*

Entrambi i pannelli usano lo stesso modello GPT-5.2. L'unica differenza è la memoria. Questo rende chiaro cosa la memoria porta alla tua applicazione e perché è essenziale per casi d'uso reali.

## Passi Successivi

**Modulo Successivo:** [02-prompt-engineering - Prompt Engineering con GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigazione:** [← Precedente: Modulo 00 - Quick Start](../00-quick-start/README.md) | [Ritorna al Principale](../README.md) | [Successivo: Modulo 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:  
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Pur impegnandoci per garantire la precisione, si prega di tenere presente che le traduzioni automatiche possono contenere errori o imprecisioni. Il documento originale nella sua lingua originaria deve essere considerato la fonte autorevole. Per informazioni critiche si raccomanda la traduzione professionale effettuata da un umano. Non siamo responsabili per eventuali incomprensioni o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->