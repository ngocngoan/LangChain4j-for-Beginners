# Modulo 03: RAG (Retrieval-Augmented Generation)

## Indice

- [Cosa Imparerai](../../../03-rag)
- [Comprendere RAG](../../../03-rag)
- [Prerequisiti](../../../03-rag)
- [Come Funziona](../../../03-rag)
  - [Elaborazione del Documento](../../../03-rag)
  - [Creazione di Embedding](../../../03-rag)
  - [Ricerca Semantica](../../../03-rag)
  - [Generazione delle Risposte](../../../03-rag)
- [Esecuzione dell'Applicazione](../../../03-rag)
- [Utilizzo dell'Applicazione](../../../03-rag)
  - [Carica un Documento](../../../03-rag)
  - [Fai Domande](../../../03-rag)
  - [Controlla le Fonti](../../../03-rag)
  - [Sperimenta con le Domande](../../../03-rag)
- [Concetti Chiave](../../../03-rag)
  - [Strategia di Suddivisione](../../../03-rag)
  - [Punteggi di Similarità](../../../03-rag)
  - [Archiviazione in Memoria](../../../03-rag)
  - [Gestione della Finestra di Contesto](../../../03-rag)
- [Quando RAG è Importante](../../../03-rag)
- [Passi Successivi](../../../03-rag)

## Cosa Imparerai

Nei moduli precedenti, hai imparato come avere conversazioni con l’IA e strutturare efficacemente i tuoi prompt. Ma c'è una limitazione fondamentale: i modelli di linguaggio conoscono solo ciò che hanno appreso durante l’addestramento. Non possono rispondere a domande sulle politiche della tua azienda, sulla documentazione del tuo progetto, o su qualsiasi informazione su cui non sono stati addestrati.

RAG (Retrieval-Augmented Generation) risolve questo problema. Invece di cercare di insegnare al modello le tue informazioni (cosa costosa e poco pratica), gli dai la capacità di cercare nei tuoi documenti. Quando qualcuno fa una domanda, il sistema trova informazioni rilevanti e le include nel prompt. Il modello quindi risponde basandosi su quel contesto recuperato.

Pensa a RAG come a fornire al modello una biblioteca di riferimento. Quando fai una domanda, il sistema:

1. **Query dell’Utente** – Fai una domanda  
2. **Embedding** – Converte la tua domanda in un vettore  
3. **Ricerca Vettoriale** – Trova i frammenti di documento simili  
4. **Assemblaggio del Contesto** – Aggiunge i frammenti rilevanti al prompt  
5. **Risposta** – LLM genera una risposta basata sul contesto  

Questo fa sì che le risposte del modello siano ancorate ai tuoi dati reali invece di basarsi solo sulle conoscenze d’addestramento o inventare risposte.

## Comprendere RAG

Il diagramma qui sotto illustra il concetto fondamentale: invece di affidarsi solo ai dati di addestramento del modello, RAG gli fornisce una biblioteca di riferimento dei tuoi documenti da consultare prima di generare ogni risposta.

<img src="../../../translated_images/it/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

Ecco come i vari componenti si collegano end-to-end. La domanda di un utente attraversa quattro fasi — embedding, ricerca vettoriale, assemblaggio del contesto e generazione della risposta — ciascuna costruendo sulla precedente:

<img src="../../../translated_images/it/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

Il resto di questo modulo spiega ogni fase nel dettaglio, con codice che puoi eseguire e modificare.

## Prerequisiti

- Completamento del Modulo 01 (risorse Azure OpenAI distribuite)  
- File `.env` nella directory principale con credenziali Azure (creato da `azd up` nel Modulo 01)  

> **Nota:** Se non hai completato il Modulo 01, segui prima le istruzioni di distribuzione lì.

## Come Funziona

### Elaborazione del Documento

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Quando carichi un documento, il sistema lo analizza (PDF o testo semplice), allega i metadata come il nome del file, e poi lo suddivide in chunk — pezzi più piccoli che si adattano comodamente nella finestra di contesto del modello. Questi chunk si sovrappongono leggermente per non perdere il contesto ai bordi.

```java
// Analizza il file caricato e avvolgilo in un Documento LangChain4j
Document document = Document.from(content, metadata);

// Suddividi in blocchi di 300 token con sovrapposizione di 30 token
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Il diagramma qui sotto mostra come funziona visivamente. Nota come ogni chunk condivide alcuni token con i suoi vicini — la sovrapposizione di 30 token garantisce che nessun contesto importante venga perso:

<img src="../../../translated_images/it/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) e chiedi:  
> - "Come fa LangChain4j a dividere i documenti in chunk e perché la sovrapposizione è importante?"  
> - "Qual è la dimensione ottimale dei chunk per diversi tipi di documento e perché?"  
> - "Come gestisco documenti in più lingue o con formattazioni speciali?"

### Creazione di Embedding

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Ogni chunk viene convertito in una rappresentazione numerica chiamata embedding - essenzialmente un'impronta matematica che cattura il significato del testo. Testi simili producono embedding simili.

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
  
Il diagramma delle classi qui sotto mostra come questi componenti LangChain4j si collegano. `OpenAiOfficialEmbeddingModel` converte il testo in vettori, `InMemoryEmbeddingStore` conserva i vettori insieme ai dati originali `TextSegment`, e `EmbeddingSearchRequest` controlla i parametri di recupero come `maxResults` e `minScore`:

<img src="../../../translated_images/it/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

Una volta che gli embedding sono memorizzati, i contenuti simili si raggruppano naturalmente nello spazio vettoriale. La visualizzazione qui sotto mostra come documenti su argomenti correlati finiscono come punti vicini, rendendo possibile la ricerca semantica:

<img src="../../../translated_images/it/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

### Ricerca Semantica

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Quando fai una domanda, essa diventa anch’essa un embedding. Il sistema confronta l’embedding della tua domanda con tutti gli embedding dei chunk di documento. Trova i chunk con significati più simili - non solo corrispondenza di parole chiave, ma vera similarità semantica.

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
  
Il diagramma qui sotto contrappone la ricerca semantica alla tradizionale ricerca per parole chiave. Una ricerca per parola chiave "veicolo" perde un chunk che parla di "auto e camion," ma la ricerca semantica comprende che significano la stessa cosa e lo restituisce come corrispondenza ad alto punteggio:

<img src="../../../translated_images/it/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

> **🤖 Prova con [GitHub Copilot](https://github.com/features/copilot) Chat:** Apri [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) e chiedi:  
> - "Come funziona la ricerca per similarità con gli embedding e cosa determina il punteggio?"  
> - "Quale soglia di similarità dovrei utilizzare e come influisce sui risultati?"  
> - "Come gestisco i casi in cui non si trovano documenti rilevanti?"

### Generazione delle Risposte

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

I chunk più rilevanti sono assemblati in un prompt strutturato che include istruzioni esplicite, il contesto recuperato e la domanda dell’utente. Il modello legge quei specifici chunk e risponde basandosi su quell’informazione — può usare solo ciò che ha davanti, evitando allucinazioni.

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
  
Il diagramma qui sotto mostra questo assemblaggio in azione — i chunk con il punteggio più alto dalla fase di ricerca vengono inseriti nel template del prompt, e `OpenAiOfficialChatModel` genera una risposta ancorata:

<img src="../../../translated_images/it/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

## Esecuzione dell'Applicazione

**Verifica la distribuzione:**

Assicurati che il file `.env` esista nella directory principale con le credenziali Azure (creato durante il Modulo 01):  
```bash
cat ../.env  # Dovrebbe mostrare AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Avvia l'applicazione:**

> **Nota:** Se hai già avviato tutte le applicazioni usando `./start-all.sh` nel Modulo 01, questo modulo è già in esecuzione sulla porta 8081. Puoi saltare i comandi di avvio sotto e andare direttamente su http://localhost:8081.

**Opzione 1: Usare Spring Boot Dashboard (Consigliato per utenti VS Code)**

Il container di sviluppo include l’estensione Spring Boot Dashboard, che fornisce un’interfaccia visiva per gestire tutte le applicazioni Spring Boot. La trovi nella barra laterale sinistra di VS Code (icona Spring Boot).

Dal Spring Boot Dashboard puoi:  
- Vedere tutte le applicazioni Spring Boot disponibili nell’ambiente di lavoro  
- Avviare/fermare le applicazioni con un solo clic  
- Visualizzare i log in tempo reale  
- Monitorare lo stato delle applicazioni  

Basta cliccare il pulsante play accanto a "rag" per avviare questo modulo, oppure avviare tutti i moduli contemporaneamente.

<img src="../../../translated_images/it/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

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
cd 03-rag
./start.sh
```
  
**PowerShell:**  
```powershell
cd 03-rag
.\start.ps1
```
  
Entrambi gli script caricano automaticamente le variabili ambiente dal file `.env` principale e costruiranno i JAR se non esistono.

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
  
Apri http://localhost:8081 nel browser.

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
  

## Utilizzo dell'Applicazione

L’applicazione offre un’interfaccia web per caricare documenti e fare domande.

<a href="images/rag-homepage.png"><img src="../../../translated_images/it/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*L’interfaccia dell’applicazione RAG - carica documenti e fai domande*

### Carica un Documento

Inizia caricando un documento - i file TXT sono i migliori per testare. Un `sample-document.txt` è fornito in questa directory, contiene informazioni sulle caratteristiche di LangChain4j, l’implementazione RAG e best practice - perfetto per testare il sistema.

Il sistema processa il tuo documento, lo divide in chunk e crea embedding per ogni chunk. Questo avviene automaticamente quando carichi.

### Fai Domande

Ora poni domande specifiche sul contenuto del documento. Prova con qualcosa di fattuale chiaramente indicato nel documento. Il sistema cerca i chunk rilevanti, li include nel prompt e genera una risposta.

### Controlla le Fonti

Nota che ogni risposta include riferimenti alle fonti con punteggi di similarità. Questi punteggi (da 0 a 1) mostrano quanto ciascun chunk era rilevante per la tua domanda. Punteggi più alti significano corrispondenze migliori. Questo ti permette di verificare la risposta con il materiale originale.

<a href="images/rag-query-results.png"><img src="../../../translated_images/it/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Risultati della query che mostrano risposta con riferimenti alla fonte e punteggi di rilevanza*

### Sperimenta con le Domande

Prova diversi tipi di domande:  
- Fatti specifici: "Qual è il tema principale?"  
- Confronti: "Qual è la differenza tra X e Y?"  
- Riassunti: "Riassumi i punti chiave su Z"  

Osserva come cambiano i punteggi di rilevanza in base a quanto bene la tua domanda corrisponde al contenuto del documento.

## Concetti Chiave

### Strategia di Suddivisione

I documenti vengono suddivisi in chunk da 300 token con 30 token di sovrapposizione. Questo equilibrio garantisce che ogni chunk abbia abbastanza contesto per essere significativo restando abbastanza piccolo da includere più chunk in un prompt.

### Punteggi di Similarità

Ogni chunk recuperato viene fornito con un punteggio di similarità tra 0 e 1 che indica quanto bene corrisponde alla domanda dell’utente. Il diagramma qui sotto visualizza le gamme di punteggio e come il sistema le usa per filtrare i risultati:

<img src="../../../translated_images/it/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

I punteggi vanno da 0 a 1:  
- 0.7-1.0: Altamente rilevante, corrispondenza esatta  
- 0.5-0.7: Rilevante, buon contesto  
- Sotto 0.5: Filtrato, troppo dissimile  

Il sistema recupera solo chunk sopra la soglia minima per assicurare qualità.

### Archiviazione in Memoria

Questo modulo usa l’archiviazione in memoria per semplicità. Quando riavvii l’applicazione, i documenti caricati vengono persi. I sistemi di produzione usano database vettoriali persistenti come Qdrant o Azure AI Search.

### Gestione della Finestra di Contesto

Ogni modello ha una finestra di contesto massima. Non puoi includere tutti i chunk di un documento grande. Il sistema recupera i N chunk più rilevanti (default 5) per rimanere dentro i limiti fornendo abbastanza contesto per risposte accurate.

## Quando RAG è Importante

RAG non è sempre l’approccio giusto. La guida decisionale qui sotto ti aiuta a determinare quando RAG aggiunge valore rispetto a quando approcci più semplici — come includere contenuti direttamente nel prompt o affidarsi alla conoscenza incorporata nel modello — sono sufficienti:

<img src="../../../translated_images/it/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

**Usa RAG quando:**
- Rispondere a domande su documenti proprietari
- Le informazioni cambiano frequentemente (politiche, prezzi, specifiche)
- La precisione richiede l'attribuzione della fonte
- Il contenuto è troppo grande per adattarsi a un unico prompt
- Hai bisogno di risposte verificabili e fondate

**Non usare RAG quando:**
- Le domande richiedono conoscenze generali che il modello possiede già
- Sono necessari dati in tempo reale (RAG funziona su documenti caricati)
- Il contenuto è abbastanza piccolo da includere direttamente nei prompt

## Passi Successivi

**Modulo Successivo:** [04-tools - Agenti AI con Strumenti](../04-tools/README.md)

---

**Navigazione:** [← Precedente: Modulo 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Torna al Principale](../README.md) | [Successivo: Modulo 04 - Strumenti →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avvertenza**:  
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Pur impegnandoci per garantire l’accuratezza, si prega di tenere presente che le traduzioni automatiche possono contenere errori o imprecisioni. Il documento originale nella sua lingua madre deve essere considerato la fonte autorevole. Per informazioni critiche si raccomanda la traduzione professionale a cura di un esperto umano. Non ci assumiamo alcuna responsabilità per incomprensioni o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->