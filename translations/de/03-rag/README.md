# Modul 03: RAG (Retrieval-Augmented Generation)

## Inhaltsverzeichnis

- [Video-Durchgang](../../../03-rag)
- [Was Sie lernen werden](../../../03-rag)
- [Voraussetzungen](../../../03-rag)
- [RAG verstehen](../../../03-rag)
  - [Welchen RAG-Ansatz verwendet dieses Tutorial?](../../../03-rag)
- [Wie es funktioniert](../../../03-rag)
  - [Dokumentenverarbeitung](../../../03-rag)
  - [Erstellung von Embeddings](../../../03-rag)
  - [Semantische Suche](../../../03-rag)
  - [Antwortgenerierung](../../../03-rag)
- [Anwendung ausführen](../../../03-rag)
- [Verwendung der Anwendung](../../../03-rag)
  - [Ein Dokument hochladen](../../../03-rag)
  - [Fragen stellen](../../../03-rag)
  - [Quellenreferenzen prüfen](../../../03-rag)
  - [Mit Fragen experimentieren](../../../03-rag)
- [Schlüsselkonzepte](../../../03-rag)
  - [Chunking-Strategie](../../../03-rag)
  - [Ähnlichkeitspunkte](../../../03-rag)
  - [In-Memory-Speicherung](../../../03-rag)
  - [Verwaltung des Kontextfensters](../../../03-rag)
- [Wann RAG wichtig ist](../../../03-rag)
- [Nächste Schritte](../../../03-rag)

## Video-Durchgang

Sehen Sie sich diese Live-Session an, die erklärt, wie Sie mit diesem Modul starten: [RAG mit LangChain4j - Live Session](https://www.youtube.com/watch?v=_olq75ZH_eY)

## Was Sie lernen werden

In den vorherigen Modulen haben Sie gelernt, wie man Gespräche mit KI führt und Ihre Eingabeaufforderungen effektiv strukturiert. Aber es gibt eine grundlegende Einschränkung: Sprachmodelle wissen nur, was sie während des Trainings gelernt haben. Sie können keine Fragen zu den Richtlinien Ihres Unternehmens, zu Ihren Projektdokumentationen oder zu Informationen beantworten, auf die sie nicht trainiert wurden.

RAG (Retrieval-Augmented Generation) löst dieses Problem. Anstatt zu versuchen, dem Modell Ihre Informationen beizubringen (was teuer und unpraktisch ist), geben Sie ihm die Möglichkeit, Ihre Dokumente zu durchsuchen. Wenn jemand eine Frage stellt, findet das System relevante Informationen und fügt diese in die Eingabeaufforderung ein. Das Modell antwortet dann basierend auf diesem abgerufenen Kontext.

Betrachten Sie RAG als eine Referenzbibliothek für das Modell. Wenn Sie eine Frage stellen, führt das System aus:

1. **Nutzeranfrage** – Sie stellen eine Frage  
2. **Embedding** – Wandelt Ihre Frage in einen Vektor um  
3. **Vektorsuche** – Findet ähnliche Dokumentenabschnitte  
4. **Kontextzusammenstellung** – Fügt relevante Abschnitte zur Eingabeaufforderung hinzu  
5. **Antwort** – Das LLM generiert eine Antwort basierend auf dem Kontext

Dies verankert die Antworten des Modells in Ihren tatsächlichen Daten, anstatt sich nur auf das Trainingswissen zu verlassen oder Antworten zu erfinden.

## Voraussetzungen

- Abgeschlossenes [Modul 00 – Quick Start](../00-quick-start/README.md) (für das oben referenzierte Easy RAG-Beispiel)  
- Abgeschlossenes [Modul 01 – Introduction](../01-introduction/README.md) (Azure OpenAI Ressourcen bereitgestellt, einschließlich des `text-embedding-3-small` Embedding-Modells)  
- `.env`-Datei im Stammverzeichnis mit Azure-Anmeldeinformationen (erstellt durch `azd up` in Modul 01)

> **Hinweis:** Wenn Sie Modul 01 nicht abgeschlossen haben, folgen Sie zunächst den dortigen Bereitstellungsanweisungen. Der Befehl `azd up` stellt sowohl das GPT-Chatmodell als auch das in diesem Modul verwendete Embedding-Modell bereit.

## RAG verstehen

Das folgende Diagramm veranschaulicht das Kernkonzept: Anstatt sich nur auf die Trainingsdaten des Modells zu verlassen, erhält RAG eine Referenzbibliothek Ihrer Dokumente, die es vor jeder Antwortgenerierung konsultiert.

<img src="../../../translated_images/de/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*Dieses Diagramm zeigt den Unterschied zwischen einem Standard-LLM (das aus Trainingsdaten schätzt) und einem RAG-verbesserten LLM (das zuerst Ihre Dokumente konsultiert).*

So sind die Elemente end-to-end verbunden. Die Nutzerfrage durchläuft vier Stufen – Einbettung, Vektorsuche, Kontextzusammenstellung und Antwortgenerierung – die jeweils auf der vorherigen aufbauen:

<img src="../../../translated_images/de/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*Dieses Diagramm zeigt die End-to-End RAG-Pipeline – eine Nutzeranfrage fließt durch Einbettung, Vektorsuche, Kontextzusammenstellung und Antwortgenerierung.*

Der Rest dieses Moduls beschreibt jede Stufe im Detail mit ausführbarem Code und Diagrammen.

### Welchen RAG-Ansatz verwendet dieses Tutorial?

LangChain4j bietet drei Möglichkeiten, RAG zu implementieren, jede mit einem anderen Abstraktionsniveau. Das folgende Diagramm vergleicht sie nebeneinander:

<img src="../../../translated_images/de/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*Dieses Diagramm vergleicht die drei LangChain4j RAG-Ansätze – Easy, Native und Advanced – zeigt ihre Hauptbestandteile und wann man sie verwendet.*

| Ansatz | Was es tut | Kompromiss |
|---|---|---|
| **Easy RAG** | Verkabelt alles automatisch über `AiServices` und `ContentRetriever`. Sie annotieren ein Interface, hängen einen Retriever an, und LangChain4j übernimmt Einbettung, Suche und Prompt-Zusammenstellung im Hintergrund. | Minimaler Code, aber Sie sehen nicht, was in jedem Schritt geschieht. |
| **Native RAG** | Sie rufen das Embedding-Modell auf, durchsuchen den Store, bauen selbst den Prompt und generieren die Antwort – ein expliziter Schritt nach dem anderen. | Mehr Code, aber jede Stufe ist sichtbar und änderbar. |
| **Advanced RAG** | Verwendet das `RetrievalAugmentor` Framework mit Plug-in-Abfrage-Transformatoren, Routern, Re-Rankern und Inhalt-Injektoren für produktionsreife Pipelines. | Maximale Flexibilität, aber deutlich mehr Komplexität. |

**Dieses Tutorial verwendet den Native-Ansatz.** Jeder Schritt der RAG-Pipeline – Einbettung der Anfrage, Suche im Vektorspeicher, Zusammenstellung des Kontexts und Generierung der Antwort – wird explizit in [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) dargestellt. Das ist bewusst so: als Lernressource ist es wichtiger, dass Sie jede Stufe sehen und verstehen, als dass der Code minimiert wird. Sobald Sie verstehen, wie die einzelnen Teile zusammenpassen, können Sie zu Easy RAG für schnelle Prototypen oder zu Advanced RAG für produktive Systeme wechseln.

> **💡 Schon Easy RAG in Aktion gesehen?** Das [Quick Start Modul](../00-quick-start/README.md) enthält ein Dokumenten-Q&A Beispiel ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)), das den Easy RAG-Ansatz verwendet – LangChain4j übernimmt Einbettung, Suche und Prompt-Zusammenstellung automatisch. Dieses Modul geht den nächsten Schritt und öffnet diese Pipeline, sodass Sie jede Stufe selbst sehen und steuern können.

<img src="../../../translated_images/de/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Dieses Diagramm zeigt die Easy RAG-Pipeline aus `SimpleReaderDemo.java`. Vergleichen Sie dies mit dem Native-Ansatz in diesem Modul: Easy RAG verbirgt Einbettung, Abruf und Prompt-Zusammenstellung hinter `AiServices` und `ContentRetriever` – Sie laden ein Dokument, hängen einen Retriever an und erhalten Antworten. Der Native-Ansatz in diesem Modul öffnet diese Pipeline, damit Sie jede Stufe (einbetten, suchen, Kontext zusammenstellen, generieren) selbst aufrufen können und somit volle Sichtbarkeit und Kontrolle haben.*

## Wie es funktioniert

Die RAG-Pipeline in diesem Modul teilt sich in vier Stufen, die jedes Mal nacheinander ausgeführt werden, wenn ein Nutzer eine Frage stellt. Zuerst wird ein hochgeladenes Dokument **geparst und in Chunks aufgeteilt**. Diese Chunks werden dann in **Vektor-Embeddings** umgewandelt und gespeichert, sodass sie mathematisch verglichen werden können. Wenn eine Anfrage eintrifft, führt das System eine **semantische Suche** durch, um die relevantesten Chunks zu finden, und gibt diese schließlich als Kontext an das LLM für die **Antwortgenerierung** weiter. Die folgenden Abschnitte führen durch jede Stufe mit dem tatsächlichen Code und Diagrammen. Schauen wir uns den ersten Schritt an.

### Dokumentenverarbeitung

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Wenn Sie ein Dokument hochladen, parst das System es (PDF oder Klartext), fügt Metadaten wie Dateiname hinzu und teilt es dann in Chunks – kleinere Abschnitte, die bequem in das Kontextfenster des Modells passen. Diese Chunks überlappen sich leicht, damit am Rand kein Kontext verloren geht.

```java
// Analysiere die hochgeladene Datei und verpacke sie in ein LangChain4j-Dokument
Document document = Document.from(content, metadata);

// In 300-Token-Abschnitte mit 30-Token-Überlappung aufteilen
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Das folgende Diagramm zeigt, wie das visuell funktioniert. Achten Sie darauf, wie sich jeder Chunk mit seinen Nachbarn Tokens teilt – die 30-Token-Überlappung sorgt dafür, dass kein wichtiger Kontext zwischen den Abschnitten verloren geht:

<img src="../../../translated_images/de/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*Dieses Diagramm zeigt ein Dokument, das in 300-Token-Chunks mit 30-Token-Überlappung aufgeteilt wird, um den Kontext an den Chunk-Grenzen zu bewahren.*

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) und fragen Sie:  
> - "Wie teilt LangChain4j Dokumente in Chunks und warum ist Überlappung wichtig?"  
> - "Wie groß ist die optimale Chunk-Größe für verschiedene Dokumenttypen und warum?"  
> - "Wie handle ich Dokumente in mehreren Sprachen oder mit spezieller Formatierung?"

### Erstellung von Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Jeder Chunk wird in eine numerische Darstellung umgewandelt, die als Embedding bezeichnet wird – im Grunde ein Bedeutungs-zu-Zahlen-Konverter. Das Embedding-Modell ist nicht „intelligent“ wie ein Chatmodell; es kann keine Anweisungen befolgen, nicht logisch schlussfolgern oder Fragen beantworten. Was es kann, ist Text in einen mathematischen Raum abzubilden, in dem ähnliche Bedeutungen nahe beieinander liegen – „Auto“ nahe bei „Automobil“, „Rückgaberecht“ nahe bei „Geld zurück“. Man kann sich ein Chatmodell als eine Person vorstellen, mit der man spricht; ein Embedding-Modell ist ein erstklassiges Ablagesystem.

<img src="../../../translated_images/de/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*Dieses Diagramm zeigt, wie ein Embedding-Modell Text in numerische Vektoren umwandelt, vergleichbare Bedeutungen – wie „Auto“ und „Automobil“ – nahe zusammen im Vektorraum platziert.*

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
  
Das Klassendiagramm unten zeigt die zwei separaten Abläufe in einer RAG-Pipeline und die LangChain4j-Klassen, die diese implementieren. Der **Ingestion-Flow** (läuft einmal bei Upload) teilt das Dokument, embedded die Chunks und speichert sie über `.addAll()`. Der **Query-Flow** (läuft jedes Mal bei einer Nutzeranfrage) embedded die Frage, sucht im Store über `.search()` und übergibt den passenden Kontext an das Chatmodell. Beide Flows treffen sich an der gemeinsamen Schnittstelle `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/de/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*Dieses Diagramm zeigt die zwei Flows in einer RAG-Pipeline – Ingestion und Query – und wie sie über ein gemeinsames EmbeddingStore verbunden sind.*

Sobald Embeddings gespeichert sind, gruppieren sich ähnliche Inhalte natürlich nah im Vektorraum. Die Visualisierung unten zeigt, wie Dokumente zu verwandten Themen als nahe beieinander liegende Punkte enden, was semantische Suche überhaupt erst ermöglicht:

<img src="../../../translated_images/de/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*Diese Visualisierung zeigt, wie verwandte Dokumente im 3D-Vektorraum gruppiert sind, mit Themen wie Technische Dokumente, Geschäftsvorschriften und FAQs, die jeweils eigene Cluster bilden.*

Wenn ein Nutzer sucht, durchläuft das System vier Schritte: die Dokumente werden einmal embedded, die Anfrage bei jeder Suche embedded, der Anfragvektor wird mit allen gespeicherten Vektoren mittels Kosinusähnlichkeit verglichen, und die Top-K höchstbewerteten Chunks werden zurückgegeben. Das Diagramm unten erläutert jeden Schritt und die beteiligten LangChain4j-Klassen:

<img src="../../../translated_images/de/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*Dieses Diagramm zeigt den vierstufigen Einbettungssuchprozess: Dokumente einbetten, Anfrage einbetten, Vektorvergleich mit Kosinusähnlichkeit, und Rückgabe der Top-K-Ergebnisse.*

### Semantische Suche

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Wenn Sie eine Frage stellen, wird auch Ihre Frage in ein Embedding umgewandelt. Das System vergleicht das Embedding Ihrer Frage mit allen Embeddings der Dokumentenabschnitte. Es findet die Chunks mit der ähnlichsten Bedeutung – nicht nur anhand von Stichwörtern, sondern tatsächlich semantisch ähnlich.

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
  
Das folgende Diagramm stellt die semantische Suche der herkömmlichen Stichwortsuche gegenüber. Eine Stichwortsuche nach „Fahrzeug“ verpasst einen Abschnitt über „Autos und Lastwagen“, aber die semantische Suche versteht, dass sie dasselbe meinen und gibt diesen Abschnitt als hoch bewertetes Ergebnis zurück:

<img src="../../../translated_images/de/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*Dieses Diagramm vergleicht stichwortbasierte Suche mit semantischer Suche und zeigt, wie semantische Suche konzeptuell verwandte Inhalte auch bei abweichenden Schlüsselwörtern abruft.*

Im Hintergrund wird Ähnlichkeit mit Kosinusähnlichkeit gemessen – im Grunde wird gefragt: „zeigen diese beiden Pfeile in dieselbe Richtung?“ Zwei Chunks können komplett unterschiedliche Wörter verwenden, aber wenn sie dieselbe Bedeutung haben, zeigen ihre Vektoren in dieselbe Richtung und erzielen eine Bewertung nahe 1,0:

<img src="../../../translated_images/de/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*Dieses Diagramm zeigt Kosinusähnlichkeit als den Winkel zwischen Einbettungsvektoren – stärker ausgerichtete Vektoren erzielen Werte nahe 1,0, was auf höhere semantische Ähnlichkeit hinweist.*
> **🤖 Probieren Sie den Chat mit [GitHub Copilot](https://github.com/features/copilot) aus:** Öffnen Sie [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) und fragen Sie:
> - "Wie funktioniert die Ähnlichkeitssuche mit Embeddings und was bestimmt den Wert?"
> - "Welchen Ähnlichkeitsschwellenwert sollte ich verwenden und wie beeinflusst er die Ergebnisse?"
> - "Wie gehe ich mit Fällen um, in denen keine relevanten Dokumente gefunden wurden?"

### Antwortgenerierung

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Die relevantesten Abschnitte werden zu einem strukturierten Prompt zusammengefügt, der explizite Anweisungen, den abgerufenen Kontext und die Frage des Benutzers enthält. Das Modell liest diese spezifischen Abschnitte und antwortet basierend auf diesen Informationen – es kann nur das verwenden, was vor ihm liegt, was Halluzinationen verhindert.

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

Das folgende Diagramm zeigt diese Zusammenstellung in Aktion — die am besten bewerteten Abschnitte aus dem Suchschritt werden in die Prompt-Vorlage eingefügt, und das `OpenAiOfficialChatModel` erzeugt eine fundierte Antwort:

<img src="../../../translated_images/de/context-assembly.7e6dd60c31f95978.webp" alt="Kontextzusammenstellung" width="800"/>

*Dieses Diagramm zeigt, wie die am besten bewerteten Abschnitte zu einem strukturierten Prompt zusammengefügt werden, sodass das Modell eine fundierte Antwort aus Ihren Daten generieren kann.*

## Anwendung starten

**Bereitstellung prüfen:**

Stellen Sie sicher, dass die `.env`-Datei im Stammverzeichnis mit Azure-Anmeldedaten vorhanden ist (wurde während Modul 01 erstellt):

**Bash:**
```bash
cat ../.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```

**Starten Sie die Anwendung:**

> **Hinweis:** Wenn Sie bereits alle Anwendungen mit `./start-all.sh` aus Modul 01 gestartet haben, läuft dieses Modul bereits auf Port 8081. Sie können die Startbefehle unten überspringen und direkt zu http://localhost:8081 gehen.

**Option 1: Verwenden des Spring Boot Dashboards (Empfohlen für VS Code Benutzer)**

Der Dev-Container enthält die Spring Boot Dashboard-Erweiterung, die eine visuelle Oberfläche zur Verwaltung aller Spring Boot-Anwendungen bietet. Sie finden sie in der Aktivitätsleiste links in VS Code (Suchen Sie nach dem Spring Boot-Symbol).

Im Spring Boot Dashboard können Sie:
- Alle verfügbaren Spring Boot-Anwendungen im Arbeitsbereich sehen
- Anwendungen mit einem Klick starten/beenden
- Anwendungprotokolle in Echtzeit anzeigen
- Anwendungsstatus überwachen

Klicken Sie einfach auf die Wiedergabetaste neben "rag", um dieses Modul zu starten, oder starten Sie alle Module gleichzeitig.

<img src="../../../translated_images/de/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Dieser Screenshot zeigt das Spring Boot Dashboard in VS Code, wo Sie Anwendungen visuell starten, stoppen und überwachen können.*

**Option 2: Verwendung von Shell-Skripten**

Starten Sie alle Webanwendungen (Module 01-04):

**Bash:**
```bash
cd ..  # Vom Stammverzeichnis
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Vom Stammverzeichnis
.\start-all.ps1
```

Oder starten Sie nur dieses Modul:

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

Beide Skripte laden automatisch Umgebungsvariablen aus der `.env`-Datei im Stammverzeichnis und erstellen die JARs, falls diese nicht existieren.

> **Hinweis:** Wenn Sie alle Module manuell vor dem Start erstellen möchten:
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

Öffnen Sie http://localhost:8081 in Ihrem Browser.

**Zum Stoppen:**

**Bash:**
```bash
./stop.sh  # Dieses Modul nur
# Oder
cd .. && ./stop-all.sh  # Alle Module
```

**PowerShell:**
```powershell
.\stop.ps1  # Dieses Modul nur
# Oder
cd ..; .\stop-all.ps1  # Alle Module
```

## Verwendung der Anwendung

Die Anwendung bietet eine Weboberfläche zum Hochladen von Dokumenten und zum Stellen von Fragen.

<a href="images/rag-homepage.png"><img src="../../../translated_images/de/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Anwendungsschnittstelle" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Dieser Screenshot zeigt die RAG-Anwendungsschnittstelle, in der Sie Dokumente hochladen und Fragen stellen.*

### Dokument hochladen

Beginnen Sie mit dem Hochladen eines Dokuments – TXT-Dateien funktionieren am besten zum Testen. Eine `sample-document.txt` ist in diesem Verzeichnis enthalten, die Informationen zu LangChain4j-Funktionen, RAG-Implementierung und Best Practices enthält – perfekt zum Testen des Systems.

Das System verarbeitet Ihr Dokument, zerlegt es in Abschnitte und erstellt für jeden Abschnitt Embeddings. Dies geschieht automatisch beim Hochladen.

### Fragen stellen

Stellen Sie nun spezifische Fragen zum Dokumentinhalt. Versuchen Sie etwas Faktisches, das im Dokument klar angegeben ist. Das System sucht nach relevanten Abschnitten, fügt diese in den Prompt ein und generiert eine Antwort.

### Quellenangaben prüfen

Beachten Sie, dass jede Antwort Quellenangaben mit Ähnlichkeitsscores enthält. Diese Scores (0 bis 1) zeigen, wie relevant jeder Abschnitt für Ihre Frage war. Höhere Werte bedeuten bessere Übereinstimmungen. Dies ermöglicht Ihnen, die Antwort mit dem Quellmaterial zu überprüfen.

<a href="images/rag-query-results.png"><img src="../../../translated_images/de/rag-query-results.6d69fcec5397f355.webp" alt="RAG Abfrageergebnisse" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Dieser Screenshot zeigt Suchergebnisse mit der generierten Antwort, Quellenangaben und Relevanzscores für jeden abgerufenen Abschnitt.*

### Mit Fragen experimentieren

Probieren Sie verschiedene Fragetypen aus:
- Spezifische Fakten: "Was ist das Hauptthema?"
- Vergleiche: "Was ist der Unterschied zwischen X und Y?"
- Zusammenfassungen: "Fasse die wichtigsten Punkte zu Z zusammen"

Beobachten Sie, wie sich die Relevanzscores ändern, abhängig davon, wie gut Ihre Frage zum Dokumentinhalt passt.

## Schlüsselkonzepte

### Chunking-Strategie

Dokumente werden in Abschnitte mit je 300 Tokens und 30 Tokens Überlappung aufgeteilt. Diese Balance sorgt dafür, dass jeder Abschnitt genug Kontext für Sinnhaftigkeit hat, aber klein genug bleibt, um mehrere Abschnitte in einem Prompt zu verwenden.

### Ähnlichkeitsscores

Jeder abgerufene Abschnitt wird mit einem Ähnlichkeitsscore zwischen 0 und 1 versehen, der angibt, wie nah er der Frage des Benutzers entspricht. Das folgende Diagramm visualisiert die Score-Bereiche und wie das System sie verwendet, um Ergebnisse zu filtern:

<img src="../../../translated_images/de/similarity-scores.b0716aa911abf7f0.webp" alt="Ähnlichkeitsscores" width="800"/>

*Dieses Diagramm zeigt Score-Bereiche von 0 bis 1 mit einem Mindestschwellenwert von 0,5, der irrelevante Abschnitte herausfiltert.*

Scores reichen von 0 bis 1:
- 0,7-1,0: Hoch relevant, exakte Übereinstimmung
- 0,5-0,7: Relevant, guter Kontext
- Unter 0,5: Gefiltert, zu unähnlich

Das System ruft nur Abschnitte oberhalb des Mindestschwellenwerts ab, um Qualität zu gewährleisten.

Embeddings funktionieren gut, wenn Bedeutungen klar gruppiert sind, haben jedoch Schwachstellen. Das folgende Diagramm zeigt typische Fehlermodi — zu große Abschnitte erzeugen unscharfe Vektoren, zu kleine Abschnitte haben wenig Kontext, mehrdeutige Begriffe verweisen auf mehrere Cluster, und exakte Suchanfragen (IDs, Teilenummern) funktionieren mit Embeddings gar nicht:

<img src="../../../translated_images/de/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Fehlermodi von Embeddings" width="800"/>

*Dieses Diagramm zeigt typische Fehlermodi bei Embeddings: zu große Abschnitte, zu kleine Abschnitte, mehrdeutige Begriffe, die auf mehrere Cluster verweisen, und exakte Suchabfragen wie IDs.*

### In-Memory-Speicherung

Dieses Modul verwendet der Einfachheit halber eine In-Memory-Speicherung. Beim Neustart der Anwendung gehen hochgeladene Dokumente verloren. Produktionssysteme verwenden persistente Vektordatenbanken wie Qdrant oder Azure AI Search.

### Verwaltung des Kontextfensters

Jedes Modell hat ein maximales Kontextfenster. Sie können nicht jeden Abschnitt eines großen Dokuments einfügen. Das System ruft die N relevantesten Abschnitte (Standard 5) ab, um innerhalb der Grenzen zu bleiben und gleichzeitig genug Kontext für genaue Antworten bereitzustellen.

## Wann RAG wichtig ist

RAG ist nicht immer der richtige Ansatz. Die folgende Entscheidungshilfe hilft Ihnen zu bestimmen, wann RAG Mehrwert bietet und wann einfachere Ansätze – wie Inhalte direkt im Prompt einzufügen oder auf das eingebaute Wissen des Modells zu vertrauen – ausreichen:

<img src="../../../translated_images/de/when-to-use-rag.1016223f6fea26bc.webp" alt="Wann RAG verwenden" width="800"/>

*Dieses Diagramm zeigt eine Entscheidungshilfe, wann RAG Mehrwert bietet und wann einfachere Ansätze ausreichen.*

**Verwenden Sie RAG, wenn:**
- Sie Fragen zu proprietären Dokumenten beantworten
- Informationen sich häufig ändern (Richtlinien, Preise, Spezifikationen)
- Genauigkeit eine Quellenangabe erfordert
- Inhalte zu groß sind, um in einen einzigen Prompt zu passen
- Sie überprüfbare, fundierte Antworten benötigen

**Verwenden Sie RAG nicht, wenn:**
- Fragen auf allgemeinem Wissen basieren, das das Modell bereits hat
- Echtzeitdaten benötigt werden (RAG arbeitet mit hochgeladenen Dokumenten)
- Inhalte klein genug sind, um direkt im Prompt enthalten zu sein

## Nächste Schritte

**Nächstes Modul:** [04-tools - KI-Agenten mit Werkzeugen](../04-tools/README.md)

---

**Navigation:** [← Zurück: Modul 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Zurück zur Übersicht](../README.md) | [Weiter: Modul 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mithilfe des KI-Übersetzungsdienstes [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir bemüht sind, Genauigkeit zu gewährleisten, können automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten. Das Originaldokument in seiner Ursprungssprache gilt als maßgebliche Quelle. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Nutzung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->