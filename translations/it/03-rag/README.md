# Modulo 03: RAG (Retrieval-Augmented Generation)

## Indice

- [Video Dimostrativo](../../../03-rag)
- [Cosa Imparerai](../../../03-rag)
- [Prerequisiti](../../../03-rag)
- [Comprendere RAG](../../../03-rag)
  - [Quale Approccio RAG Usa Questo Tutorial?](../../../03-rag)
- [Come Funziona](../../../03-rag)
  - [Elaborazione del Documento](../../../03-rag)
  - [Creazione di Embedding](../../../03-rag)
  - [Ricerca Semantica](../../../03-rag)
  - [Generazione delle Risposte](../../../03-rag)
- [Avvia l'Applicazione](../../../03-rag)
- [Utilizzo dell’Applicazione](../../../03-rag)
  - [Carica un Documento](../../../03-rag)
  - [Fai Domande](../../../03-rag)
  - [Verifica le Riferimenti delle Fonti](../../../03-rag)
  - [Sperimenta con le Domande](../../../03-rag)
- [Concetti Chiave](../../../03-rag)
  - [Strategia di Suddivisione](../../../03-rag)
  - [Punteggi di Similarità](../../../03-rag)
  - [Archiviazione in Memoria](../../../03-rag)
  - [Gestione della Finestra di Contesto](../../../03-rag)
- [Quando RAG Conta](../../../03-rag)
- [Passi Successivi](../../../03-rag)

## Video Dimostrativo

Guarda questa sessione dal vivo che spiega come iniziare con questo modulo: [RAG con LangChain4j - Sessione Live](https://www.youtube.com/watch?v=_olq75ZH_eY)

## Cosa Imparerai

Nei moduli precedenti, hai imparato come conversare con l’IA e strutturare efficacemente i tuoi prompt. Ma esiste una limitazione fondamentale: i modelli di linguaggio conoscono solo ciò che hanno appreso durante l’addestramento. Non possono rispondere a domande sulle politiche della tua azienda, sulla documentazione del tuo progetto o su qualsiasi informazione che non è stata incorporata in fase di addestramento.

RAG (Retrieval-Augmented Generation) risolve questo problema. Invece di cercare di insegnare al modello le tue informazioni (operazione costosa e poco pratica), gli fornisci la capacità di cercare tra i tuoi documenti. Quando qualcuno fa una domanda, il sistema trova le informazioni rilevanti e le include nel prompt. Il modello risponde quindi basandosi su quel contesto recuperato.

Pensa a RAG come a una biblioteca di riferimento per il modello. Quando fai una domanda, il sistema:

1. **Domanda Utente** - Fai una domanda  
2. **Embedding** - Converte la tua domanda in un vettore  
3. **Ricerca Vettoriale** - Trova frammenti di documento simili  
4. **Assemblaggio del Contesto** - Aggiunge i frammenti rilevanti al prompt  
5. **Risposta** - Il LLM genera una risposta basata sul contesto  

Questo ancorare le risposte del modello ai tuoi dati reali invece di basarsi solo sulla conoscenza derivante dall’addestramento o inventare risposte.

## Prerequisiti

- Completato [Modulo 00 - Quick Start](../00-quick-start/README.md) (per l’esempio Easy RAG citato sopra)  
- Completato [Modulo 01 - Introduzione](../01-introduction/README.md) (risorse Azure OpenAI distribuite, incluso il modello di embedding `text-embedding-3-small`)  
- File `.env` nella directory root con le credenziali Azure (creato da `azd up` nel Modulo 01)  

> **Nota:** Se non hai completato il Modulo 01, segui prima lì le istruzioni di distribuzione. Il comando `azd up` distribuisce sia il modello chat GPT sia il modello di embedding usato in questo modulo.

## Comprendere RAG

Il diagramma qui sotto illustra il concetto base: invece di fare affidamento solo sui dati di addestramento del modello, RAG gli fornisce una biblioteca di riferimento dei tuoi documenti da consultare prima di generare ogni risposta.

<img src="../../../translated_images/it/what-is-rag.1f9005d44b07f2d8.webp" alt="Che cos'è RAG" width="800"/>

*Questo diagramma mostra la differenza tra un LLM standard (che fa ipotesi dai dati di addestramento) e un LLM potenziato con RAG (che consulta prima i tuoi documenti).*

Ecco come si collegano i pezzi da un estremo all’altro. La domanda di un utente passa attraverso quattro fasi — embedding, ricerca vettoriale, assemblaggio del contesto e generazione della risposta — ognuna costruita sulla precedente:

<img src="../../../translated_images/it/rag-architecture.ccb53b71a6ce407f.webp" alt="Architettura RAG" width="800"/>

*Questo diagramma mostra la pipeline end-to-end di RAG — una domanda utente passa per embedding, ricerca vettoriale, assemblaggio del contesto e generazione della risposta.*

Il resto di questo modulo illustra ogni fase in dettaglio, con codice eseguibile e modificabile.

### Quale Approccio RAG Usa Questo Tutorial?

LangChain4j offre tre modi per implementare RAG, ciascuno con un diverso livello di astrazione. Il diagramma qui sotto li confronta fianco a fianco:

<img src="../../../translated_images/it/rag-approaches.5b97fdcc626f1447.webp" alt="Tre approcci RAG in LangChain4j" width="800"/>

*Questo diagramma confronta i tre approcci RAG di LangChain4j — Easy, Native e Advanced — mostrando i loro componenti chiave e quando usarli.*

| Approccio | Cosa Fa | Compromesso |
|---|---|---|
| **Easy RAG** | Collega tutto automaticamente tramite `AiServices` e `ContentRetriever`. Ancori un’interfaccia, attacchi un retriever e LangChain4j gestisce embedding, ricerca e assemblaggio prompt dietro le quinte. | Minimo codice, ma non vedi cosa accade ad ogni passo. |
| **Native RAG** | Chiami direttamente il modello embedding, cerchi nello store, costruisci il prompt e generi la risposta — un passo esplicito alla volta. | Più codice, ma ogni fase è visibile e modificabile. |
| **Advanced RAG** | Usa il framework `RetrievalAugmentor` con trasformatori di query pluggabili, router, riordinatori e iniettori di contenuto per pipeline di livello produttivo. | Massima flessibilità, ma complessità molto maggiore. |

**Questo tutorial usa l’approccio Native.** Ogni passaggio della pipeline RAG — embed della query, ricerca nello store vettoriale, assemblaggio del contesto e generazione della risposta — è scritto esplicitamente in [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Ciò è intenzionale: come risorsa didattica, è più importante che tu veda e comprenda ogni fase piuttosto che minimizzare il codice. Quando ti sentirai a tuo agio con il flusso, potrai passare a Easy RAG per prototipi rapidi o Advanced RAG per sistemi di produzione.

> **💡 Hai già visto Easy RAG in azione?** Il [modulo Quick Start](../00-quick-start/README.md) include un esempio Document Q&A ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) che usa l’approccio Easy RAG — LangChain4j gestisce automaticamente embedding, ricerca e assemblaggio prompt. Questo modulo fa il passo successivo rompendo quella pipeline così da poter vedere e controllare ogni fase personalmente.

<img src="../../../translated_images/it/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Pipeline Easy RAG - LangChain4j" width="800"/>

*Questo diagramma mostra la pipeline Easy RAG da `SimpleReaderDemo.java`. Confrontalo con l’approccio Native usato in questo modulo: Easy RAG nasconde embedding, recupero e assemblaggio prompt dietro `AiServices` e `ContentRetriever` — carichi un documento, alleghi un retriever e ricevi risposte. L’approccio Native di questo modulo rompe quella pipeline così da chiamare ogni fase (embed, ricerca, assemblaggio contesto, generazione) direttamente, offrendo completa visibilità e controllo.*

## Come Funziona

La pipeline RAG in questo modulo si suddivide in quattro fasi che si eseguono in sequenza ogni volta che un utente fa una domanda. Prima, un documento caricato viene **analizzato e suddiviso in chunk** gestibili. Quei chunk vengono poi convertiti in **embedding vettoriali** e archiviati per poterne fare confronti matematici. Quando arriva una query, il sistema esegue una **ricerca semantica** per trovare i chunk più rilevanti e infine li passa come contesto al LLM per la **generazione della risposta**. Le sezioni seguenti mostrano ogni fase con il codice effettivo e diagrammi. Vediamo il primo passo.

### Elaborazione del Documento

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Quando carichi un documento, il sistema lo analizza (PDF o testo semplice), allega metadati come il nome file e poi lo suddivide in chunk — pezzi più piccoli che si adattano comodamente alla finestra di contesto del modello. Questi chunk si sovrappongono leggermente per non perdere il contesto ai confini.

```java
// Analizza il file caricato e incapsulalo in un documento LangChain4j
Document document = Document.from(content, metadata);

// Dividi in blocchi da 300 token con una sovrapposizione di 30 token
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Il diagramma qui sotto mostra come funziona visivamente. Nota come ogni chunk condivide alcuni token con i vicini — la sovrapposizione di 30 token garantisce che nessun contesto importante cada tra le crepe:

<img src="../../../translated_images/it/document-chunking.a5df1dd1383431ed.webp" alt="Suddivisione del Documento" width="800"/>

*Questo diagramma mostra un documento diviso in chunk da 300 token con sovrapposizione di 30 token, preservando il contesto ai bordi dei chunk.*

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) e chiedi:  
> - "Come LangChain4j suddivide i documenti in chunk e perché la sovrapposizione è importante?"  
> - "Qual è la dimensione ottimale dei chunk per diversi tipi di documento e perché?"  
> - "Come gestisco documenti in più lingue o con formattazioni speciali?"

### Creazione di Embedding

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Ogni chunk viene convertito in una rappresentazione numerica chiamata embedding — essenzialmente un convertitore significato-in-numeri. Il modello embedding non è "intelligente" come un modello chat; non può seguire istruzioni, ragionare o rispondere a domande. Ciò che può fare è mappare il testo in uno spazio matematico in cui significati simili si trovano vicini — "auto" vicino a "automobile", "politica di rimborso" vicino a "restituiscimi i soldi". Pensa a un modello chat come a una persona con cui puoi parlare; un modello embedding è un sistema di archiviazione ultra efficiente.

<img src="../../../translated_images/it/embedding-model-concept.90760790c336a705.webp" alt="Concetto Modello Embedding" width="800"/>

*Questo diagramma mostra come un modello embedding converte il testo in vettori numerici, posizionando significati simili — come "auto" e "automobile" — vicini nello spazio vettoriale.*

```java
@Bean
public EmbeddingModel embeddingModel() {
    return OpenAiOfficialEmbeddingModel.builder()
        .baseUrl(azureOpenAiEndpoint)
        .apiKey(azureOpenAiKey)
        .modelName(azureEmbeddingDeploymentName)
        .build();
}

EmbeddingStore<TextSegment> embeddingStore = 
    new InMemoryEmbeddingStore<>();
```
  
Il diagramma delle classi qui sotto mostra i due flussi separati in una pipeline RAG e le classi di LangChain4j che li implementano. Il **flusso di ingestione** (eseguito una volta al momento del caricamento) suddivide il documento, crea gli embedding dei chunk e li archivia tramite `.addAll()`. Il **flusso di query** (eseguito ogni volta che un utente pone una domanda) crea l’embedding della domanda, cerca nello store tramite `.search()` e passa il contesto abbinato al modello chat. Entrambi si incontrano nell’interfaccia condivisa `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/it/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="Classi LangChain4j RAG" width="800"/>

*Questo diagramma mostra i due flussi nella pipeline RAG — ingestione e query — e come si connettono attraverso un EmbeddingStore condiviso.*

Quando gli embedding sono archiviati, contenuti simili naturalmente si raggruppano insieme nello spazio vettoriale. La visualizzazione qui sotto mostra come documenti su argomenti correlati finiscono come punti vicini, rendendo possibile la ricerca semantica:

<img src="../../../translated_images/it/vector-embeddings.2ef7bdddac79a327.webp" alt="Spazio Embedding Vettoriale" width="800"/>

*Questa visualizzazione mostra come documenti correlati si raggruppano nello spazio vettoriale 3D, con argomenti come Documentazione Tecnica, Regole Aziendali e FAQ che formano gruppi distinti.*

Quando un utente cerca, il sistema segue quattro passaggi: crea embedding dei documenti una volta, crea embedding della query a ogni ricerca, confronta il vettore della query con tutti i vettori archiviati usando la similarità coseno e restituisce i top-K chunk con i punteggi più alti. Il diagramma qui sotto spiega ogni passaggio e le classi LangChain4j coinvolte:

<img src="../../../translated_images/it/embedding-search-steps.f54c907b3c5b4332.webp" alt="Passi Ricerca Embedding" width="800"/>

*Questo diagramma mostra il processo di ricerca embedding in quattro passi: creare embedding dei documenti, embedding della query, confronto dei vettori con similarità coseno e restituzione dei migliori risultati.*

### Ricerca Semantica

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Quando fai una domanda, la tua domanda diventa anch’essa un embedding. Il sistema confronta l’embedding della tua domanda con tutti gli embedding dei chunk di documento. Trova i chunk con i significati più simili - non solo parole chiave corrispondenti, ma vera similarità semantica.

```java
Embedding queryEmbedding = embeddingModel.embed(question).content();

EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
    .queryEmbedding(queryEmbedding)
    .maxResults(5)
    .minScore(0.5)
    .build();

EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);
List<EmbeddingMatch<TextSegment>> matches = searchResult.matches();

for (EmbeddingMatch<TextSegment> match : matches) {
    String relevantText = match.embedded().text();
    double score = match.score();
}
```
  
Il diagramma qui sotto confronta la ricerca semantica con la ricerca tradizionale per parole chiave. Una ricerca per parola chiave "veicolo" perde un chunk su "auto e camion", ma la ricerca semantica capisce che significano la stessa cosa e lo restituisce come corrispondenza con alto punteggio:

<img src="../../../translated_images/it/semantic-search.6b790f21c86b849d.webp" alt="Ricerca Semantica" width="800"/>

*Questo diagramma confronta la ricerca basata su parole chiave con la ricerca semantica, mostrando come la ricerca semantica recupera contenuti concettualmente correlati anche quando le parole chiave esatte differiscono.*

Dietro le quinte, la similarità viene misurata usando la similarità coseno — in pratica si chiede "queste due frecce puntano nella stessa direzione?" Due chunk possono usare parole completamente diverse, ma se significano la stessa cosa i loro vettori puntano nella stessa direzione e ottengono un punteggio vicino a 1.0:

<img src="../../../translated_images/it/cosine-similarity.9baeaf3fc3336abb.webp" alt="Similarità Coseno" width="800"/>

*Questo diagramma illustra la similarità coseno come l’angolo tra vettori embedding — vettori più allineati ottengono punteggi più vicini a 1.0, indicando una maggiore similarità semantica.*
> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) e chiedi:
> - "Come funziona la ricerca di similarità con gli embedding e cosa determina il punteggio?"
> - "Quale soglia di similarità dovrei usare e come influenza i risultati?"
> - "Come gestisco i casi in cui non vengono trovati documenti rilevanti?"

### Generazione delle Risposte

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

I chunk più rilevanti vengono assemblati in un prompt strutturato che include istruzioni esplicite, il contesto recuperato e la domanda dell'utente. Il modello legge quei chunk specifici e risponde basandosi su quelle informazioni — può usare solo ciò che ha davanti, il che previene le allucinazioni.

```java
String context = matches.stream()
    .map(match -> match.embedded().text())
    .collect(Collectors.joining("\n\n"));

String prompt = String.format("""
    Answer the question based on the following context.
    If the answer cannot be found in the context, say so.

    Context:
    %s

    Question: %s

    Answer:""", context, request.question());

String answer = chatModel.chat(prompt);
```

Il diagramma sottostante mostra questo assemblaggio in azione — i chunk con il punteggio più alto dal passo di ricerca vengono inseriti nel template del prompt, e `OpenAiOfficialChatModel` genera una risposta fondata:

<img src="../../../translated_images/it/context-assembly.7e6dd60c31f95978.webp" alt="Assemblaggio del Contesto" width="800"/>

*Questo diagramma mostra come i chunk con punteggio più alto vengono assemblati in un prompt strutturato, permettendo al modello di generare una risposta fondata dai tuoi dati.*

## Avviare l'Applicazione

**Verifica il deployment:**

Assicurati che il file `.env` esista nella directory root con le credenziali Azure (create durante il Modulo 01):

**Bash:**
```bash
cat ../.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Avvia l'applicazione:**

> **Nota:** Se hai già avviato tutte le applicazioni usando `./start-all.sh` dal Modulo 01, questo modulo è già in esecuzione sulla porta 8081. Puoi saltare i comandi di avvio qui sotto e andare direttamente a http://localhost:8081.

**Opzione 1: Usare Spring Boot Dashboard (Consigliato per utenti VS Code)**

Il container di sviluppo include l'estensione Spring Boot Dashboard, che fornisce un'interfaccia visiva per gestire tutte le applicazioni Spring Boot. La trovi nella Activity Bar sul lato sinistro di VS Code (cerca l'icona Spring Boot).

Dal Spring Boot Dashboard puoi:
- Vedere tutte le applicazioni Spring Boot disponibili nell'area di lavoro
- Avviare/fermare le applicazioni con un solo clic
- Visualizzare i log delle applicazioni in tempo reale
- Monitorare lo stato delle applicazioni

Clicca semplicemente il pulsante play accanto a "rag" per avviare questo modulo, oppure avvia tutti i moduli contemporaneamente.

<img src="../../../translated_images/it/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Questa schermata mostra il Spring Boot Dashboard in VS Code, dove puoi avviare, fermare e monitorare le applicazioni in modo visivo.*

**Opzione 2: Usare script shell**

Avvia tutte le web app (moduli 01-04):

**Bash:**
```bash
cd ..  # Dalla directory root
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
cd 03-rag
./start.sh
```

**PowerShell:**
```powershell
cd 03-rag
.\start.ps1
```

Entrambi gli script caricano automaticamente le variabili d'ambiente dal file `.env` nella root e compileranno i JAR se non esistono.

> **Nota:** Se preferisci compilare tutti i moduli manualmente prima di avviarli:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Apri http://localhost:8081 nel tuo browser.

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

## Uso dell'Applicazione

L'applicazione fornisce un'interfaccia web per il caricamento dei documenti e la formulazione di domande.

<a href="images/rag-homepage.png"><img src="../../../translated_images/it/rag-homepage.d90eb5ce1b3caa94.webp" alt="Interfaccia Applicazione RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Questa schermata mostra l'interfaccia dell'applicazione RAG dove carichi documenti e poni domande.*

### Carica un Documento

Inizia caricando un documento – i file TXT sono i migliori per i test. È fornito un file `sample-document.txt` in questa cartella che contiene informazioni sulle funzionalità di LangChain4j, l'implementazione RAG e le best practice – perfetto per testare il sistema.

Il sistema processa il tuo documento, lo suddivide in chunk e crea embedding per ogni chunk. Questo avviene automaticamente al caricamento.

### Fai Domande

Ora poni domande specifiche sul contenuto del documento. Prova qualcosa di fattuale, chiaramente espresso nel documento. Il sistema cerca i chunk rilevanti, li include nel prompt e genera una risposta.

### Controlla i Riferimenti alla Fonte

Nota che ogni risposta include riferimenti alla fonte con i punteggi di similarità. Questi punteggi (da 0 a 1) mostrano quanto ogni chunk è rilevante rispetto alla tua domanda. Punteggi più alti indicano corrispondenze migliori. Questo ti permette di verificare la risposta rispetto al materiale di origine.

<a href="images/rag-query-results.png"><img src="../../../translated_images/it/rag-query-results.6d69fcec5397f355.webp" alt="Risultati Query RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Questa schermata mostra i risultati di una query con la risposta generata, i riferimenti alle fonti e i punteggi di rilevanza per ogni chunk recuperato.*

### Sperimenta con le Domande

Prova diversi tipi di domande:
- Fatti specifici: "Qual è l'argomento principale?"
- Confronti: "Qual è la differenza tra X e Y?"
- Riassunti: "Riepiloga i punti chiave su Z"

Guarda come cambiano i punteggi di rilevanza in base a quanto la tua domanda corrisponde al contenuto del documento.

## Concetti Chiave

### Strategia di Suddivisione in Chunk

I documenti sono divisi in chunk da 300 token con 30 token di sovrapposizione. Questo equilibrio assicura che ogni chunk abbia abbastanza contesto per essere significativo, rimanendo però abbastanza piccolo da includere più chunk in un prompt.

### Punteggi di Similarità

Ogni chunk recuperato ha un punteggio di similarità tra 0 e 1 che indica quanto è vicino alla domanda dell'utente. Il diagramma sottostante visualizza gli intervalli di punteggio e come il sistema li usa per filtrare i risultati:

<img src="../../../translated_images/it/similarity-scores.b0716aa911abf7f0.webp" alt="Punteggi di Similarità" width="800"/>

*Questo diagramma mostra gli intervalli di punteggio da 0 a 1, con una soglia minima di 0.5 che filtra i chunk irrilevanti.*

I punteggi variano da 0 a 1:
- 0.7-1.0: Altamente rilevante, corrispondenza esatta
- 0.5-0.7: Rilevante, buon contesto
- Sotto 0.5: Filtrato, troppo dissimile

Il sistema recupera solo chunk sopra la soglia minima per garantire qualità.

Gli embedding funzionano bene quando il significato forma cluster chiari, ma hanno punti ciechi. Il diagramma sottostante mostra i modi comuni di fallimento — chunk troppo grandi producono vettori confusi, chunk troppo piccoli mancano di contesto, termini ambigui puntano a più cluster, e ricerche esatte (ID, numeri di parte) non funzionano per nulla con gli embedding:

<img src="../../../translated_images/it/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Modi di Fallimento degli Embedding" width="800"/>

*Questo diagramma mostra i modi di fallimento comuni degli embedding: chunk troppo grandi, chunk troppo piccoli, termini ambigui che puntano a più cluster, e ricerche esatte come ID.*

### Memorizzazione In-Memory

Questo modulo usa la memorizzazione in memoria per semplicità. Quando riavvii l'applicazione, i documenti caricati si perdono. I sistemi in produzione usano database vettoriali persistenti come Qdrant o Azure AI Search.

### Gestione della Finestra di Contesto

Ogni modello ha una massima finestra di contesto. Non puoi includere tutti i chunk di un documento grande. Il sistema recupera i primi N chunk più rilevanti (default 5) per rimanere nei limiti offrendo comunque abbastanza contesto per risposte accurate.

## Quando RAG è Importante

RAG non è sempre l'approccio giusto. La guida decisionale sottostante ti aiuta a determinare quando RAG porta valore rispetto a quando approcci più semplici — come includere contenuto direttamente nel prompt o affidarsi alla conoscenza intrinseca del modello — sono sufficienti:

<img src="../../../translated_images/it/when-to-use-rag.1016223f6fea26bc.webp" alt="Quando Usare RAG" width="800"/>

*Questo diagramma mostra una guida decisionale per quando RAG aggiunge valore rispetto a quando approcci più semplici bastano.*

**Usa RAG quando:**
- Rispondi a domande su documenti proprietari
- L'informazione cambia frequentemente (policy, prezzi, specifiche)
- L'accuratezza richiede attribuzione della fonte
- Il contenuto è troppo grande per stare in un singolo prompt
- Hai bisogno di risposte verificabili e fondate

**Non usare RAG quando:**
- Le domande richiedono conoscenze generali che il modello ha già
- Serve dati in tempo reale (RAG funziona su documenti caricati)
- Il contenuto è abbastanza piccolo da includere direttamente nei prompt

## Passi Successivi

**Modulo Successivo:** [04-tools - AI Agents con Tools](../04-tools/README.md)

---

**Navigazione:** [← Precedente: Modulo 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Torna al Principale](../README.md) | [Successivo: Modulo 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:  
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica AI [Co-op Translator](https://github.com/Azure/co-op-translator). Pur impegnandoci per l’accuratezza, si prega di considerare che le traduzioni automatiche potrebbero contenere errori o imprecisioni. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda la traduzione professionale effettuata da un umano. Non siamo responsabili per eventuali fraintendimenti o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->