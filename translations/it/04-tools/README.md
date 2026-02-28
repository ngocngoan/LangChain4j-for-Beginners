# Modulo 04: Agenti AI con Strumenti

## Indice

- [Cosa Imparerai](../../../04-tools)
- [Prerequisiti](../../../04-tools)
- [Comprendere gli Agenti AI con Strumenti](../../../04-tools)
- [Come Funziona la Chiamata agli Strumenti](../../../04-tools)
  - [Definizioni degli Strumenti](../../../04-tools)
  - [Decision Making](../../../04-tools)
  - [Esecuzione](../../../04-tools)
  - [Generazione della Risposta](../../../04-tools)
  - [Architettura: Auto-wiring di Spring Boot](../../../04-tools)
- [Catena di Strumenti](../../../04-tools)
- [Eseguire l’Applicazione](../../../04-tools)
- [Usare l’Applicazione](../../../04-tools)
  - [Provare un Uso Semplice degli Strumenti](../../../04-tools)
  - [Testare la Catena di Strumenti](../../../04-tools)
  - [Visualizzare il Flusso della Conversazione](../../../04-tools)
  - [Sperimentare con Richieste Diverse](../../../04-tools)
- [Concetti Chiave](../../../04-tools)
  - [Pattern ReAct (Ragionare e Agire)](../../../04-tools)
  - [Le Descrizioni degli Strumenti Sono Importanti](../../../04-tools)
  - [Gestione della Sessione](../../../04-tools)
  - [Gestione degli Errori](../../../04-tools)
- [Strumenti Disponibili](../../../04-tools)
- [Quando Usare Agenti Basati su Strumenti](../../../04-tools)
- [Strumenti vs RAG](../../../04-tools)
- [Prossimi Passi](../../../04-tools)

## Cosa Imparerai

Finora, hai imparato come avere conversazioni con l’AI, strutturare efficacemente i prompt e ancorare le risposte ai tuoi documenti. Ma esiste ancora una limitazione fondamentale: i modelli linguistici possono solo generare testo. Non possono controllare il meteo, effettuare calcoli, interrogare database o interagire con sistemi esterni.

Gli strumenti cambiano questo. Dando al modello accesso a funzioni che può chiamare, lo trasformi da generatore di testo in un agente che può compiere azioni. Il modello decide quando ha bisogno di uno strumento, quale strumento usare e quali parametri passare. Il tuo codice esegue la funzione e ritorna il risultato. Il modello incorpora quel risultato nella sua risposta.

## Prerequisiti

- Aver completato il Modulo 01 (risorse Azure OpenAI distribuite)
- File `.env` nella directory principale con credenziali Azure (creato da `azd up` nel Modulo 01)

> **Nota:** Se non hai completato il Modulo 01, segui prima le istruzioni di distribuzione là presenti.

## Comprendere gli Agenti AI con Strumenti

> **📝 Nota:** Il termine "agenti" in questo modulo si riferisce ad assistenti AI potenziati con capacità di chiamata agli strumenti. Questo è diverso dai pattern **Agentic AI** (agenti autonomi con pianificazione, memoria e ragionamento multi-step) che copriremo in [Modulo 05: MCP](../05-mcp/README.md).

Senza strumenti, un modello linguistico può solo generare testo basato sui dati di training. Gli chiedi il meteo attuale e deve indovinare. Gli dai strumenti e può chiamare un’API meteo, fare calcoli o consultare un database — e poi inserire quei risultati reali nella risposta.

<img src="../../../translated_images/it/what-are-tools.724e468fc4de64da.webp" alt="Senza Strumenti vs Con Strumenti" width="800"/>

*Senza strumenti il modello può solo indovinare — con gli strumenti può chiamare API, eseguire calcoli e restituire dati in tempo reale.*

Un agente AI con strumenti segue un pattern **Reasoning and Acting (ReAct)**. Il modello non si limita a rispondere — pensa a cosa gli serve, agisce chiamando uno strumento, osserva il risultato e poi decide se agire di nuovo o fornire la risposta finale:

1. **Ragiona** — L’agente analizza la domanda dell’utente e determina quali informazioni gli servono  
2. **Agisce** — L’agente seleziona lo strumento giusto, genera i parametri corretti e lo chiama  
3. **Osserva** — L’agente riceve l’output dello strumento e valuta il risultato  
4. **Ripete o Risponde** — Se servono più dati, torna indietro; altrimenti compone una risposta in linguaggio naturale  

<img src="../../../translated_images/it/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Pattern ReAct" width="800"/>

*Il ciclo ReAct — l’agente ragiona su cosa fare, agisce chiamando uno strumento, osserva il risultato e cicla finché può fornire la risposta finale.*

Questo accade automaticamente. Definisci gli strumenti e le loro descrizioni. Il modello gestisce il processo decisionale su quando e come usarli.

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

// L'assistente è automaticamente configurato da Spring Boot con:
// - Bean ChatModel
// - Tutti i metodi @Tool dalle classi @Component
// - ChatMemoryProvider per la gestione delle sessioni
```
  
Il diagramma sottostante scompone ogni annotazione e mostra come ogni parte aiuta l’AI a capire quando chiamare lo strumento e quali argomenti passare:

<img src="../../../translated_images/it/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomia delle Definizioni degli Strumenti" width="800"/>

*Anatomia di una definizione dello strumento — @Tool indica all’AI quando usarlo, @P descrive ogni parametro e @AiService collega tutto all’avvio.*

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) e chiedi:  
> - "Come integrerei una vera API meteo come OpenWeatherMap invece di dati mock?"  
> - "Cosa rende buona una descrizione dello strumento che aiuta l’AI a usarlo correttamente?"  
> - "Come gestisco errori di API e limiti di chiamate nelle implementazioni degli strumenti?"

### Decision Making

Quando un utente chiede "Com’è il tempo a Seattle?", il modello non sceglie uno strumento a caso. Confronta l’intento dell’utente con ogni descrizione di strumento a cui ha accesso, assegna un punteggio di rilevanza e sceglie il migliore. Poi genera una chiamata di funzione strutturata con i parametri giusti — in questo caso impostando `location` a `"Seattle"`.

Se nessuno strumento corrisponde alla richiesta, il modello risponde con le sue conoscenze. Se più strumenti corrispondono, ne sceglie uno specifico.

<img src="../../../translated_images/it/decision-making.409cd562e5cecc49.webp" alt="Come l’AI Decide Quale Strumento Usare" width="800"/>

*Il modello valuta ogni strumento disponibile rispetto all’intento dell’utente e sceglie la corrispondenza migliore — ecco perché è importante scrivere descrizioni chiare e specifiche.*

### Esecuzione

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot cola automaticamente l’interfaccia dichiarativa `@AiService` con tutti gli strumenti registrati, e LangChain4j esegue le chiamate agli strumenti automaticamente. Dietro le quinte, una chiamata completa a uno strumento passa per sei fasi — dalla domanda in linguaggio naturale dell’utente fino alla risposta in linguaggio naturale:

<img src="../../../translated_images/it/tool-calling-flow.8601941b0ca041e6.webp" alt="Flusso di Chiamata agli Strumenti" width="800"/>

*Flusso end-to-end — l’utente fa una domanda, il modello seleziona uno strumento, LangChain4j lo esegue e il modello intreccia il risultato in una risposta naturale.*

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) e chiedi:  
> - "Come funziona il pattern ReAct e perché è efficace per gli agenti AI?"  
> - "Come decide l’agente quale strumento usare e in quale ordine?"  
> - "Cosa succede se fallisce l’esecuzione di uno strumento - come gestire robustamente gli errori?"

### Generazione della Risposta

Il modello riceve i dati meteo e li formatta in una risposta in linguaggio naturale per l’utente.

### Architettura: Auto-wiring di Spring Boot

Questo modulo utilizza l’integrazione Spring Boot di LangChain4j con interfacce dichiarative `@AiService`. All’avvio Spring Boot scopre ogni `@Component` che contiene metodi `@Tool`, il bean `ChatModel` e il `ChatMemoryProvider` — poi li collega tutti in una singola interfaccia `Assistant` senza codice boilerplate.

<img src="../../../translated_images/it/spring-boot-wiring.151321795988b04e.webp" alt="Architettura Auto-wiring di Spring Boot" width="800"/>

*L’interfaccia @AiService collega ChatModel, componenti degli strumenti e provider di memoria — Spring Boot gestisce automaticamente tutto il wiring.*

Vantaggi chiave di questo approccio:

- **Auto-wiring di Spring Boot** — ChatModel e strumenti iniettati automaticamente  
- **Pattern @MemoryId** — Gestione automatica della memoria basata sulla sessione  
- **Istanza singola** — Assistant creato una volta e riutilizzato per migliori prestazioni  
- **Esecuzione type-safe** — Metodi Java chiamati direttamente con conversione di tipo  
- **Orchestrazione multi-turn** — Gestisce automaticamente catene di strumenti  
- **Zero boilerplate** — Nessuna chiamata manuale `AiServices.builder()` o HashMap di memoria  

Gli approcci alternativi (con `AiServices.builder()` manuale) richiedono più codice e perdono i vantaggi dell’integrazione Spring Boot.

## Catena di Strumenti

**Catena di Strumenti** — La vera potenza degli agenti basati su strumenti emerge quando una singola domanda richiede più strumenti. Chiedi "Com’è il tempo a Seattle in Fahrenheit?" e l’agente concatena automaticamente due strumenti: prima chiama `getCurrentWeather` per ottenere la temperatura in Celsius, poi passa quel valore a `celsiusToFahrenheit` per la conversione — tutto in un unico turno di conversazione.

<img src="../../../translated_images/it/tool-chaining-example.538203e73d09dd82.webp" alt="Esempio di Catena di Strumenti" width="800"/>

*Catena di strumenti in azione — l’agente chiama prima getCurrentWeather, poi passa il risultato in Celsius a celsiusToFahrenheit, e fornisce una risposta combinata.*

Ecco com’è nell’applicazione in esecuzione — l’agente concatena due chiamate strumento in un solo turno di conversazione:

<a href="images/tool-chaining.png"><img src="../../../translated_images/it/tool-chaining.3b25af01967d6f7b.webp" alt="Catena di Strumenti" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Output reale dell’app — l’agente concatena automaticamente getCurrentWeather → celsiusToFahrenheit in un solo turno.*

**Gestione Elegante degli Errori** — Chiedi il meteo in una città non presente nei dati mock. Lo strumento restituisce un messaggio d’errore e l’AI spiega che non può aiutare invece di andare in crash. Gli strumenti falliscono in modo sicuro.

<img src="../../../translated_images/it/error-handling-flow.9a330ffc8ee0475c.webp" alt="Flusso di Gestione degli Errori" width="800"/>

*Quando uno strumento fallisce, l’agente cattura l’errore e risponde con una spiegazione di aiuto invece di andare in crash.*

Questo avviene in un solo turno di conversazione. L’agente orchestra autonomamente più chiamate a strumenti.

## Eseguire l’Applicazione

**Verifica della distribuzione:**

Assicurati che il file `.env` esista nella directory principale con le credenziali Azure (create durante il Modulo 01):  
```bash
cat ../.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Avvia l’applicazione:**

> **Nota:** Se hai già avviato tutte le applicazioni usando `./start-all.sh` dal Modulo 01, questo modulo è già in esecuzione sulla porta 8084. Puoi saltare i comandi di avvio qui sotto e andare direttamente su http://localhost:8084.

**Opzione 1: Usare Spring Boot Dashboard (Consigliato per utenti VS Code)**

Il container di sviluppo include l’estensione Spring Boot Dashboard, che fornisce un’interfaccia visiva per gestire tutte le applicazioni Spring Boot. Puoi trovarla nella Activity Bar di VS Code a sinistra (cerca l’icona Spring Boot).

Dal Spring Boot Dashboard puoi:  
- Vedere tutte le applicazioni Spring Boot disponibili nel workspace  
- Avviare/fermare applicazioni con un solo clic  
- Visualizzare i log delle applicazioni in tempo reale  
- Monitorare lo stato delle applicazioni  

Clicca semplicemente sul pulsante play accanto a "tools" per avviare questo modulo, oppure avvia tutti i moduli insieme.

<img src="../../../translated_images/it/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

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
cd 04-tools
./start.sh
```
  
**PowerShell:**  
```powershell
cd 04-tools
.\start.ps1
```
  
Entrambi gli script caricano automaticamente le variabili d’ambiente dal file `.env` principale e compileranno i JAR se non esistono.

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
  
Apri http://localhost:8084 nel browser.

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
  
## Usare l’Applicazione

L’applicazione offre un’interfaccia web dove puoi interagire con un agente AI che ha accesso a strumenti di meteo e conversione temperatura.

<a href="images/tools-homepage.png"><img src="../../../translated_images/it/tools-homepage.4b4cd8b2717f9621.webp" alt="Interfaccia Agente AI con Strumenti" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Interfaccia Agente AI con Strumenti - esempi rapidi e chat per interagire con gli strumenti*

### Provare un Uso Semplice degli Strumenti
Inizia con una richiesta semplice: "Converti 100 gradi Fahrenheit in Celsius". L'agente riconosce che ha bisogno dello strumento di conversione della temperatura, lo chiama con i parametri corretti e restituisce il risultato. Nota quanto appare naturale - non hai specificato quale strumento usare o come chiamarlo.

### Test di concatenamento degli strumenti

Ora prova qualcosa di più complesso: "Che tempo fa a Seattle e converti la temperatura in Fahrenheit?" Osserva come l'agente procede in passaggi. Prima ottiene il meteo (che restituisce i gradi Celsius), riconosce che deve convertire in Fahrenheit, chiama lo strumento di conversione e combina entrambi i risultati in una sola risposta.

### Guarda il flusso della conversazione

L'interfaccia di chat mantiene la cronologia della conversazione, permettendoti di avere interazioni multiple a turni. Puoi vedere tutte le richieste e le risposte precedenti, rendendo semplice seguire la conversazione e capire come l'agente costruisce il contesto attraverso più scambi.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/it/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversazione con più chiamate agli strumenti" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Conversazione a più turni che mostra conversioni semplici, ricerche meteo e concatenamento degli strumenti*

### Sperimenta con diverse richieste

Prova varie combinazioni:
- Ricerche meteo: "Che tempo fa a Tokyo?"
- Conversioni di temperatura: "Quanto sono 25°C in Kelvin?"
- Query combinate: "Controlla il meteo a Parigi e dimmi se è sopra i 20°C"

Nota come l'agente interpreta il linguaggio naturale e lo mappa alle chiamate corrette agli strumenti.

## Concetti chiave

### Pattern ReAct (Ragionamento e Azione)

L'agente alterna tra ragionamento (decidere cosa fare) e azione (usare strumenti). Questo pattern consente una risoluzione autonoma dei problemi invece di rispondere semplicemente alle istruzioni.

### Le descrizioni degli strumenti sono importanti

La qualità delle descrizioni degli strumenti influisce direttamente su quanto bene l'agente li utilizza. Descrizioni chiare e specifiche aiutano il modello a capire quando e come chiamare ogni strumento.

### Gestione della sessione

L'annotazione `@MemoryId` abilita la gestione automatica della memoria basata sulla sessione. Ogni ID sessione ottiene una sua istanza di `ChatMemory` gestita dal bean `ChatMemoryProvider`, così più utenti possono interagire con l'agente contemporaneamente senza che le loro conversazioni si mescolino.

<img src="../../../translated_images/it/session-management.91ad819c6c89c400.webp" alt="Gestione della sessione con @MemoryId" width="800"/>

*Ogni ID sessione corrisponde a una cronologia di conversazione isolata — gli utenti non vedono mai i messaggi degli altri.*

### Gestione degli errori

Gli strumenti possono fallire — le API possono andare in timeout, i parametri potrebbero essere invalidi, i servizi esterni possono non funzionare. Gli agenti in produzione necessitano di una gestione degli errori affinché il modello possa spiegare i problemi o tentare alternative invece di far crashare l'intera applicazione. Quando uno strumento lancia un’eccezione, LangChain4j la cattura e invia il messaggio d’errore al modello, che può così spiegare il problema in linguaggio naturale.

## Strumenti disponibili

Il diagramma qui sotto mostra l’ampio ecosistema di strumenti che puoi costruire. Questo modulo dimostra strumenti per il meteo e la temperatura, ma lo stesso pattern `@Tool` funziona per qualsiasi metodo Java — da query database a processi di pagamento.

<img src="../../../translated_images/it/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ecosistema degli strumenti" width="800"/>

*Qualsiasi metodo Java annotato con @Tool diventa disponibile per l’AI — il pattern si estende a database, API, email, operazioni su file e altro.*

## Quando usare agenti basati su strumenti

<img src="../../../translated_images/it/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Quando usare gli strumenti" width="800"/>

*Una rapida guida alle decisioni — gli strumenti servono per dati in tempo reale, calcoli e azioni; conoscenza generale e compiti creativi non ne hanno bisogno.*

**Usa gli strumenti quando:**
- La risposta richiede dati in tempo reale (meteo, prezzi azioni, inventario)
- Devi effettuare calcoli complessi rispetto alla matematica semplice
- Accedi a database o API
- Esegui azioni (invia email, crea ticket, aggiorna record)
- Combini più fonti di dati

**Non usare gli strumenti quando:**
- Le domande si rispondono con conoscenza generale
- La risposta è puramente conversazionale
- La latenza dello strumento renderebbe l’esperienza troppo lenta

## Strumenti vs RAG

I moduli 03 e 04 estendono entrambi ciò che l’AI può fare, ma in modi fondamentalmente diversi. RAG dà al modello accesso alla **conoscenza** recuperando documenti. Gli strumenti danno al modello la capacità di compiere **azioni** chiamando funzioni.

<img src="../../../translated_images/it/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Confronto Strumenti vs RAG" width="800"/>

*RAG recupera informazioni da documenti statici — Gli strumenti eseguono azioni e recuperano dati dinamici, in tempo reale. Molti sistemi di produzione combinano entrambi.*

Nella pratica, molti sistemi di produzione combinano entrambi gli approcci: RAG per ancorare le risposte alla documentazione, e Strumenti per recuperare dati live o eseguire operazioni.

## Passi successivi

**Prossimo modulo:** [05-mcp - Protocollo di contesto modello (MCP)](../05-mcp/README.md)

---

**Navigazione:** [← Precedente: Modulo 03 - RAG](../03-rag/README.md) | [Torna al principale](../README.md) | [Prossimo: Modulo 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:  
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Pur impegnandoci per garantire la precisione, si prega di notare che le traduzioni automatiche possono contenere errori o inesattezze. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche, si consiglia la traduzione professionale umana. Non siamo responsabili per eventuali malintesi o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->