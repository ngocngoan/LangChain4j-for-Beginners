# Modulo 04: Agenti AI con Strumenti

## Indice

- [Video Walkthrough](../../../04-tools)
- [Cosa Imparerai](../../../04-tools)
- [Prerequisiti](../../../04-tools)
- [Comprendere gli Agenti AI con Strumenti](../../../04-tools)
- [Come Funziona la Chiamata agli Strumenti](../../../04-tools)
  - [Definizioni degli Strumenti](../../../04-tools)
  - [Processo Decisionale](../../../04-tools)
  - [Esecuzione](../../../04-tools)
  - [Generazione della Risposta](../../../04-tools)
  - [Architettura: Auto-Iniezione di Spring Boot](../../../04-tools)
- [Catena di Strumenti](../../../04-tools)
- [Avvia l'Applicazione](../../../04-tools)
- [Uso dell'Applicazione](../../../04-tools)
  - [Prova l'Uso Semplice dello Strumento](../../../04-tools)
  - [Test della Catena di Strumenti](../../../04-tools)
  - [Guarda il Flusso della Conversazione](../../../04-tools)
  - [Sperimenta con Diverse Richieste](../../../04-tools)
- [Concetti Chiave](../../../04-tools)
  - [Pattern ReAct (Ragionamento e Azione)](../../../04-tools)
  - [Le Descrizioni degli Strumenti Contano](../../../04-tools)
  - [Gestione della Sessione](../../../04-tools)
  - [Gestione degli Errori](../../../04-tools)
- [Strumenti Disponibili](../../../04-tools)
- [Quando Usare Agenti Basati su Strumenti](../../../04-tools)
- [Strumenti vs RAG](../../../04-tools)
- [Prossimi Passi](../../../04-tools)

## Video Walkthrough

Guarda questa sessione live che spiega come iniziare con questo modulo:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="Agenti AI con Strumenti e MCP - Sessione Live" width="800"/></a>

## Cosa Imparerai

Finora, hai imparato come conversare con l'AI, strutturare efficacemente i prompt e ancorare le risposte ai tuoi documenti. Ma esiste ancora un limite fondamentale: i modelli linguistici possono solo generare testo. Non possono controllare il meteo, effettuare calcoli, interrogare database o interagire con sistemi esterni.

Gli strumenti cambiano questo. Dando al modello accesso a funzioni che può chiamare, lo trasformi da generatore di testo a un agente che può compiere azioni. Il modello decide quando ha bisogno di uno strumento, quale strumento usare e quali parametri passare. Il tuo codice esegue la funzione e restituisce il risultato. Il modello incorpora quel risultato nella sua risposta.

## Prerequisiti

- Completato il [Modulo 01 - Introduzione](../01-introduction/README.md) (risorse Azure OpenAI distribuite)
- Completati i moduli precedenti consigliati (questo modulo fa riferimento ai [concetti RAG del Modulo 03](../03-rag/README.md) nel confronto Strumenti vs RAG)
- File `.env` nella directory radice con le credenziali Azure (creato con `azd up` nel Modulo 01)

> **Nota:** Se non hai completato il Modulo 01, segui prima le istruzioni di distribuzione lì.

## Comprendere gli Agenti AI con Strumenti

> **📝 Nota:** Il termine "agenti" in questo modulo si riferisce ad assistenti AI potenziati con capacità di chiamata di strumenti. Questo è diverso dai pattern di **Agentic AI** (agenti autonomi con pianificazione, memoria e ragionamento multi-step) che tratteremo in [Modulo 05: MCP](../05-mcp/README.md).

Senza strumenti, un modello linguistico può solo generare testo dai dati di addestramento. Chiedigli il meteo attuale e deve indovinare. Dagli strumenti, può chiamare un'API meteo, effettuare calcoli o interrogare un database — poi inserire quei risultati reali nella risposta.

<img src="../../../translated_images/it/what-are-tools.724e468fc4de64da.webp" alt="Senza Strumenti vs Con Strumenti" width="800"/>

*Senza strumenti il modello può solo indovinare — con gli strumenti può chiamare API, eseguire calcoli e restituire dati in tempo reale.*

Un agente AI con strumenti segue un pattern **Reasoning and Acting (ReAct)**. Il modello non si limita a rispondere — pensa a ciò di cui ha bisogno, agisce chiamando uno strumento, osserva il risultato e decide se agire ancora o fornire la risposta finale:

1. **Ragiona** — L'agente analizza la domanda dell'utente e determina quali informazioni servono
2. **Agisci** — L'agente seleziona lo strumento giusto, genera i parametri corretti e lo chiama
3. **Osserva** — L'agente riceve l'output dello strumento e valuta il risultato
4. **Ripeti o Rispondi** — Se servono altri dati, l'agente ripete; altrimenti compone una risposta in linguaggio naturale

<img src="../../../translated_images/it/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Pattern ReAct" width="800"/>

*Il ciclo ReAct — l'agente ragiona su cosa fare, agisce chiamando uno strumento, osserva il risultato e ripete fino a fornire la risposta finale.*

Questo accade in modo automatico. Definisci gli strumenti e le loro descrizioni. Il modello gestisce le decisioni su quando e come usarli.

## Come Funziona la Chiamata agli Strumenti

### Definizioni degli Strumenti

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Definisci funzioni con descrizioni chiare e specifiche dei parametri. Il modello vede queste descrizioni nel prompt di sistema e capisce cosa fa ogni strumento.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // La tua logica di ricerca meteo
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Assistant è automaticamente collegato da Spring Boot con:
// - Bean ChatModel
// - Tutti i metodi @Tool delle classi @Component
// - ChatMemoryProvider per la gestione della sessione
```

Il diagramma seguente analizza ogni annotazione e mostra come ogni pezzo aiuta l'AI a capire quando chiamare lo strumento e quali argomenti passare:

<img src="../../../translated_images/it/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomia delle Definizioni degli Strumenti" width="800"/>

*Anatomia di una definizione di strumento — @Tool indica all'AI quando usarlo, @P descrive ogni parametro, e @AiService collega tutto all'avvio.*

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) e chiedi:
> - "Come potrei integrare una vera API meteo come OpenWeatherMap invece dei dati di esempio?"
> - "Cosa rende una buona descrizione dello strumento che aiuta l'AI ad usarlo correttamente?"
> - "Come gestisco errori API e limiti di chiamate nelle implementazioni degli strumenti?"

### Processo Decisionale

Quando un utente chiede "Che tempo fa a Seattle?", il modello non sceglie uno strumento a caso. Confronta l'intento dell'utente con ogni descrizione degli strumenti a cui ha accesso, valuta la rilevanza di ciascuno e seleziona la corrispondenza migliore. Genera quindi una chiamata di funzione strutturata con i parametri corretti — in questo caso impostando `location` a `"Seattle"`.

Se nessuno strumento corrisponde alla richiesta dell'utente, il modello risponde con le sue conoscenze interne. Se più strumenti corrispondono, sceglie quello più specifico.

<img src="../../../translated_images/it/decision-making.409cd562e5cecc49.webp" alt="Come l'AI Decide Quale Strumento Usare" width="800"/>

*Il modello valuta ogni strumento disponibile rispetto all'intento dell'utente e seleziona la corrispondenza migliore — perciò scrivere descrizioni chiare e specifiche è importante.*

### Esecuzione

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot collega automaticamente l'interfaccia dichiarativa `@AiService` con tutti gli strumenti registrati, e LangChain4j esegue le chiamate agli strumenti automaticamente. Dietro le quinte, una chiamata completa ad uno strumento scorre attraverso sei fasi — dalla domanda in linguaggio naturale dell'utente fino alla risposta in linguaggio naturale:

<img src="../../../translated_images/it/tool-calling-flow.8601941b0ca041e6.webp" alt="Flusso di Chiamata agli Strumenti" width="800"/>

*Il flusso end-to-end — l'utente fa una domanda, il modello seleziona uno strumento, LangChain4j lo esegue, e il modello intreccia il risultato in una risposta naturale.*

Se hai eseguito il [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) nel Modulo 00, hai già visto questo pattern in azione — anche gli strumenti `Calculator` venivano chiamati nello stesso modo. Il diagramma di sequenza seguente mostra esattamente cosa è successo sotto il cofano durante quella demo:

<img src="../../../translated_images/it/tool-calling-sequence.94802f406ca26278.webp" alt="Diagramma di Sequenza per la Chiamata agli Strumenti" width="800"/>

*Il ciclo di chiamata agli strumenti dalla demo Quick Start — `AiServices` manda il tuo messaggio e gli schemi degli strumenti al LLM, il LLM risponde con una chiamata di funzione come `add(42, 58)`, LangChain4j esegue il metodo `Calculator` localmente, e restituisce il risultato per la risposta finale.*

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) e chiedi:
> - "Come funziona il pattern ReAct e perché è efficace per gli agenti AI?"
> - "Come decide l'agente quale strumento usare e in che ordine?"
> - "Cosa succede se l'esecuzione di uno strumento fallisce - come gestire gli errori in modo robusto?"

### Generazione della Risposta

Il modello riceve i dati meteorologici e li formatta in una risposta in linguaggio naturale per l'utente.

### Architettura: Auto-Iniezione di Spring Boot

Questo modulo usa l'integrazione LangChain4j con Spring Boot tramite interfacce dichiarative `@AiService`. All'avvio Spring Boot scopre ogni `@Component` che contiene metodi annotati `@Tool`, il tuo bean `ChatModel` e il `ChatMemoryProvider` — quindi li collega tutti in una singola interfaccia `Assistant` senza necessità di codice boilerplate.

<img src="../../../translated_images/it/spring-boot-wiring.151321795988b04e.webp" alt="Architettura Auto-Iniezione di Spring Boot" width="800"/>

*L'interfaccia @AiService unisce il ChatModel, i componenti degli strumenti e il provider di memoria — Spring Boot gestisce automaticamente tutta l'iniezione.*

Ecco il ciclo di vita completo della richiesta come diagramma di sequenza — dalla richiesta HTTP attraverso controller, servizio e proxy auto-iniettato, fino all'esecuzione dello strumento e ritorno:

<img src="../../../translated_images/it/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Sequenza di Chiamata degli Strumenti con Spring Boot" width="800"/>

*Il ciclo di vita completo di una richiesta in Spring Boot — la richiesta HTTP passa attraverso controller e servizio fino al proxy Assistant auto-iniettato, che orchestra automaticamente la chiamata al LLM e agli strumenti.*

Principali vantaggi di questo approccio:

- **Auto-iniezione di Spring Boot** — ChatModel e strumenti iniettati automaticamente
- **Pattern @MemoryId** — Gestione automatica della memoria basata sulla sessione
- **Istanza singola** — Assistant creato una volta e riutilizzato per migliori prestazioni
- **Esecuzione type-safe** — Metodi Java chiamati direttamente con conversione tipi
- **Orchestrazione multi-turno** — Gestisce la catena di strumenti automaticamente
- **Zero boilerplate** — Nessuna chiamata manuale `AiServices.builder()` o hashmap per la memoria

Approcci alternativi (manuale `AiServices.builder()`) richiedono più codice e non beneficiano dell'integrazione con Spring Boot.

## Catena di Strumenti

**Catena di Strumenti** — La vera potenza degli agenti basati su strumenti emerge quando una singola domanda richiede più strumenti. Chiedi "Che tempo fa a Seattle in Fahrenheit?" e l'agente collega automaticamente due strumenti: prima chiama `getCurrentWeather` per ottenere la temperatura in Celsius, poi passa quel valore a `celsiusToFahrenheit` per la conversione — tutto in un unico turno di conversazione.

<img src="../../../translated_images/it/tool-chaining-example.538203e73d09dd82.webp" alt="Esempio di Catena di Strumenti" width="800"/>

*Catena di strumenti in azione — l'agente chiama prima getCurrentWeather, poi passa il risultato in Celsius a celsiusToFahrenheit e fornisce una risposta combinata.*

**Gestione Elegante degli Errori** — Chiedi il meteo in una città che non è nei dati di esempio. Lo strumento restituisce un messaggio di errore, e l'AI spiega che non può aiutare invece di bloccarsi. Gli strumenti falliscono in modo sicuro. Il diagramma sotto confronta i due approcci — con una corretta gestione degli errori, l'agente cattura l'eccezione e risponde in maniera utile, mentre senza di essa l'intera applicazione va in crash:

<img src="../../../translated_images/it/error-handling-flow.9a330ffc8ee0475c.webp" alt="Flusso di Gestione degli Errori" width="800"/>

*Quando uno strumento fallisce, l'agente cattura l'errore e risponde con una spiegazione utile invece di andare in crash.*

Questo accade in un singolo turno di conversazione. L'agente orchestra più chiamate agli strumenti autonomamente.

## Avvia l'Applicazione

**Verifica la distribuzione:**

Assicurati che il file `.env` esista nella directory radice con le credenziali Azure (creato durante il Modulo 01). Esegui questo dal directory del modulo (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Avvia l'applicazione:**

> **Nota:** Se hai già avviato tutte le applicazioni usando `./start-all.sh` dalla directory radice (come descritto nel Modulo 01), questo modulo è già in esecuzione sulla porta 8084. Puoi saltare i comandi di avvio qui sotto e andare direttamente su http://localhost:8084.

**Opzione 1: Uso del Spring Boot Dashboard (Raccomandato per utenti VS Code)**

Il container di sviluppo include l'estensione Spring Boot Dashboard, che fornisce un'interfaccia visiva per gestire tutte le applicazioni Spring Boot. Puoi trovarlo nella Activity Bar sulla sinistra di VS Code (cerca l'icona di Spring Boot).

Dal Spring Boot Dashboard, puoi:
- Vedere tutte le applicazioni Spring Boot disponibili nello spazio di lavoro
- Avviare/fermare applicazioni con un solo clic
- Visualizzare in tempo reale i log delle applicazioni
- Monitorare lo stato dell'applicazione
Basta cliccare sul pulsante play accanto a "tools" per avviare questo modulo, oppure avviare tutti i moduli insieme.

Ecco come appare la Spring Boot Dashboard in VS Code:

<img src="../../../translated_images/it/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*La Spring Boot Dashboard in VS Code — avvia, ferma e monitora tutti i moduli da un unico posto*

**Opzione 2: Utilizzo di script shell**

Avvia tutte le applicazioni web (moduli 01-04):

**Bash:**
```bash
cd ..  # Dalla directory radice
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
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

Entrambi gli script caricano automaticamente le variabili d’ambiente dal file `.env` nella radice e compileranno i JAR se non esistono.

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

Apri http://localhost:8084 nel tuo browser.

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

## Uso dell’Applicazione

L’applicazione fornisce un’interfaccia web dove puoi interagire con un agente AI che ha accesso a strumenti meteo e di conversione della temperatura. Ecco come appare l’interfaccia — include esempi rapidi e un pannello chat per inviare richieste:

<a href="images/tools-homepage.png"><img src="../../../translated_images/it/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*L’interfaccia AI Agent Tools - esempi rapidi e interfaccia chat per interagire con gli strumenti*

### Prova un Uso Semplice degli Strumenti

Inizia con una richiesta semplice: "Converti 100 gradi Fahrenheit in Celsius". L’agente riconosce che serve lo strumento di conversione della temperatura, lo richiama con i parametri corretti e restituisce il risultato. Nota quanto sembra naturale — non hai specificato quale strumento usare o come chiamarlo.

### Prova la Catena di Strumenti

Ora prova qualcosa di più complesso: "Com'è il meteo a Seattle e converti in Fahrenheit?" Guarda l’agente lavorare a questo per passi. Prima ottiene il meteo (che ritorna in Celsius), riconosce che deve convertire in Fahrenheit, chiama lo strumento di conversione, e combina entrambi i risultati in una singola risposta.

### Vedi il Flusso della Conversazione

L’interfaccia chat conserva la cronologia delle conversazioni, permettendoti di avere interazioni multi-turno. Puoi vedere tutte le query e risposte precedenti, rendendo facile tracciare la conversazione e capire come l’agente costruisce il contesto attraverso più scambi.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/it/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Conversazione multi-turno che mostra conversioni semplici, ricerche meteo e concatenamento di strumenti*

### Sperimenta con Richieste Diverse

Prova varie combinazioni:
- Ricerche meteo: "Com'è il meteo a Tokyo?"
- Conversioni di temperatura: "Quanto sono 25°C in Kelvin?"
- Query combinate: "Controlla il meteo a Parigi e dimmi se è sopra i 20°C"

Nota come l’agente interpreta il linguaggio naturale e lo mappa a chiamate appropriate agli strumenti.

## Concetti Chiave

### Pattern ReAct (Reasoning and Acting)

L’agente alterna tra ragionamento (decidere cosa fare) e azione (usare gli strumenti). Questo pattern permette la risoluzione autonoma dei problemi invece di rispondere solo a istruzioni.

### Le Descrizioni degli Strumenti Contano

La qualità delle descrizioni degli strumenti influisce direttamente sull’efficacia dell’agente nell’utilizzarli. Descrizioni chiare e specifiche aiutano il modello a capire quando e come chiamare ciascuno strumento.

### Gestione della Sessione

L’annotazione `@MemoryId` abilita la gestione automatica della memoria basata sulla sessione. Ogni ID di sessione ottiene la propria istanza di `ChatMemory` gestita dal bean `ChatMemoryProvider`, così più utenti possono interagire con l’agente simultaneamente senza che le conversazioni si mescolino. Il diagramma seguente mostra come più utenti vengono indirizzati a memorie isolate basate sui loro ID di sessione:

<img src="../../../translated_images/it/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Ogni ID di sessione mappa a una storia di conversazione isolata — gli utenti non vedono mai i messaggi degli altri.*

### Gestione degli Errori

Gli strumenti possono fallire — le API scadono, i parametri potrebbero essere invalidi, i servizi esterni si interrompono. Gli agenti in produzione necessitano della gestione degli errori così il modello può spiegare i problemi o tentare alternative anziché far crashare l’intera applicazione. Quando uno strumento lancia un’eccezione, LangChain4j la cattura e restituisce il messaggio di errore al modello, che può quindi spiegare il problema in linguaggio naturale.

## Strumenti Disponibili

Il diagramma sottostante mostra il vasto ecosistema di strumenti che puoi costruire. Questo modulo dimostra strumenti per il meteo e la temperatura, ma lo stesso pattern `@Tool` funziona per qualsiasi metodo Java — dalle query al database all’elaborazione dei pagamenti.

<img src="../../../translated_images/it/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Qualsiasi metodo Java annotato con @Tool diventa disponibile per l’AI — il pattern si estende a database, API, email, operazioni su file e altro.*

## Quando Usare Agenti Basati su Strumenti

Non ogni richiesta necessita di strumenti. La decisione dipende dal fatto che l’AI debba interagire con sistemi esterni o possa rispondere dalla propria conoscenza. La guida seguente riassume quando gli strumenti aggiungono valore e quando non sono necessari:

<img src="../../../translated_images/it/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*Una guida veloce per decidere — gli strumenti servono per dati in tempo reale, calcoli e azioni; conoscenza generale e compiti creativi non ne hanno bisogno.*

## Strumenti vs RAG

I moduli 03 e 04 estendono entrambi le capacità dell’AI, ma in modi fondamentalmente diversi. RAG dà al modello accesso alla **conoscenza** recuperando documenti. Gli strumenti danno al modello la capacità di compiere **azioni** chiamando funzioni. Il diagramma sottostante confronta questi due approcci fianco a fianco — da come funziona ciascun flusso di lavoro ai compromessi tra di essi:

<img src="../../../translated_images/it/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG recupera informazioni da documenti statici — gli strumenti eseguono azioni e recuperano dati dinamici e in tempo reale. Molti sistemi di produzione combinano entrambi.*

In pratica, molti sistemi di produzione combinano entrambi gli approcci: RAG per ancorare le risposte nella documentazione, e Strumenti per recuperare dati live o eseguire operazioni.

## Passi Successivi

**Prossimo Modulo:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigazione:** [← Precedente: Modulo 03 - RAG](../03-rag/README.md) | [Torna al Principale](../README.md) | [Successivo: Modulo 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:  
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica AI [Co-op Translator](https://github.com/Azure/co-op-translator). Pur impegnandoci per garantire accuratezza, si prega di considerare che le traduzioni automatiche possono contenere errori o inesattezze. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche, si consiglia una traduzione professionale effettuata da un esperto umano. Non siamo responsabili per eventuali incomprensioni o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->