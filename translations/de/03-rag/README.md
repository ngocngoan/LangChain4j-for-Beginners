# Modul 03: RAG (Retrieval-Augmented Generation)

## Inhaltsverzeichnis

- [Video Walkthrough](../../../03-rag)
- [Was Sie lernen werden](../../../03-rag)
- [Voraussetzungen](../../../03-rag)
- [Verständnis von RAG](../../../03-rag)
  - [Welchen RAG-Ansatz verwendet dieses Tutorial?](../../../03-rag)
- [Wie es funktioniert](../../../03-rag)
  - [Dokumentenverarbeitung](../../../03-rag)
  - [Erstellung von Embeddings](../../../03-rag)
  - [Semantische Suche](../../../03-rag)
  - [Antwortgenerierung](../../../03-rag)
- [Anwendung ausführen](../../../03-rag)
- [Anwendung verwenden](../../../03-rag)
  - [Dokument hochladen](../../../03-rag)
  - [Fragen stellen](../../../03-rag)
  - [Quellen überprüfen](../../../03-rag)
  - [Experimente mit Fragen](../../../03-rag)
- [Wichtige Konzepte](../../../03-rag)
  - [Chunking-Strategie](../../../03-rag)
  - [Ähnlichkeitsscores](../../../03-rag)
  - [In-Memory-Speicherung](../../../03-rag)
  - [Verwaltung des Kontextfensters](../../../03-rag)
- [Wann RAG wichtig ist](../../../03-rag)
- [Nächste Schritte](../../../03-rag)

## Video Walkthrough

Sehen Sie sich diese Live-Session an, die erklärt, wie Sie mit diesem Modul starten:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG mit LangChain4j - Live Session" width="800"/></a>

## Was Sie lernen werden

In den vorherigen Modulen haben Sie gelernt, wie Sie Gespräche mit KI führen und Ihre Prompts effektiv strukturieren. Aber es gibt eine grundlegende Einschränkung: Sprachmodelle kennen nur das, was sie während des Trainings gelernt haben. Sie können keine Fragen zu den Unternehmensrichtlinien, Ihrer Projektdokumentation oder Informationen beantworten, zu denen sie nicht trainiert wurden.

RAG (Retrieval-Augmented Generation) löst dieses Problem. Anstatt dem Modell Ihre Informationen beizubringen (was teuer und unpraktisch ist), geben Sie ihm die Fähigkeit, in Ihren Dokumenten zu suchen. Wenn jemand eine Frage stellt, findet das System relevante Informationen und fügt diese in den Prompt ein. Das Modell antwortet dann auf Grundlage dieses abgerufenen Kontexts.

Stellen Sie sich RAG vor wie eine Referenzbibliothek für das Modell. Wenn Sie eine Frage stellen, erledigt das System:

1. **Benutzeranfrage** – Sie stellen eine Frage  
2. **Embedding** – Wandelt Ihre Frage in einen Vektor um  
3. **Vektorsuche** – Findet ähnliche Dokumentenabschnitte  
4. **Kontextaufbau** – Fügt relevante Abschnitte zum Prompt hinzu  
5. **Antwort** – Das LLM generiert eine Antwort basierend auf dem Kontext  

Dies verankert die Antworten des Modells in Ihren tatsächlichen Daten, anstatt sich auf sein Trainingswissen zu verlassen oder Antworten zu erfinden.

## Voraussetzungen

- Abgeschlossenes [Modul 00 - Quick Start](../00-quick-start/README.md) (für das Easy RAG-Beispiel, das später in diesem Modul genannt wird)  
- Abgeschlossenes [Modul 01 - Einführung](../01-introduction/README.md) (Azure OpenAI Ressourcen bereitgestellt, einschließlich des `text-embedding-3-small` Embedding-Modells)  
- `.env`-Datei im Stammverzeichnis mit Azure-Anmeldeinformationen (erstellt durch `azd up` in Modul 01)  

> **Hinweis:** Wenn Sie Modul 01 nicht abgeschlossen haben, folgen Sie zunächst dort den Bereitstellungsanweisungen. Der Befehl `azd up` stellt sowohl das GPT-Chat-Modell als auch das Embedding-Modell bereit, das in diesem Modul verwendet wird.

## Verständnis von RAG

Die folgende Grafik illustriert das Kernkonzept: Anstatt sich nur auf die Trainingsdaten des Modells zu verlassen, erhält RAG eine Referenzbibliothek Ihrer Dokumente, die vor der Antwortgenerierung konsultiert wird.

<img src="../../../translated_images/de/what-is-rag.1f9005d44b07f2d8.webp" alt="Was ist RAG" width="800"/>

*Dieses Diagramm zeigt den Unterschied zwischen einem Standard-LLM (das auf Trainingsdaten schätzt) und einem RAG-verbesserten LLM (das zuerst Ihre Dokumente konsultiert).*

So verbinden sich die Teile von Anfang bis Ende. Die Frage eines Benutzers durchläuft vier Phasen – Embedding, Vektorsuche, Kontextaufbau und Antwortgenerierung – die jeweils auf der vorherigen aufbauen:

<img src="../../../translated_images/de/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architektur" width="800"/>

*Dieses Diagramm zeigt die End-to-End RAG-Pipeline – eine Benutzeranfrage durchläuft Embedding, Vektorsuche, Kontextaufbau und Antwortgenerierung.*

Der Rest dieses Moduls führt Sie durch jede Phase im Detail, mit Code, den Sie ausführen und anpassen können.

### Welchen RAG-Ansatz verwendet dieses Tutorial?

LangChain4j bietet drei Möglichkeiten, RAG zu implementieren, jeweils mit unterschiedlicher Abstraktionsebene. Das Diagramm unten vergleicht sie nebeneinander:

<img src="../../../translated_images/de/rag-approaches.5b97fdcc626f1447.webp" alt="Drei RAG-Ansätze in LangChain4j" width="800"/>

*Dieses Diagramm vergleicht die drei LangChain4j RAG-Ansätze – Easy, Native und Advanced – zeigt deren Hauptkomponenten und wann man sie einsetzt.*

| Ansatz | Was er tut | Kompromiss |
|---|---|---|
| **Easy RAG** | Verkabelt alles automatisch über `AiServices` und `ContentRetriever`. Sie annotieren ein Interface, hängen einen Retriever an, und LangChain4j kümmert sich hinter den Kulissen um Embedding, Suche und Prompt-Erstellung. | Minimaler Code, aber Sie sehen nicht, was in jedem Schritt passiert. |
| **Native RAG** | Sie rufen das Embedding-Modell auf, durchsuchen den Speicher, bauen den Prompt zusammen und generieren die Antwort – Schritt für Schritt explizit. | Mehr Code, aber jede Phase ist sichtbar und änderbar. |
| **Advanced RAG** | Verwendet das `RetrievalAugmentor` Framework mit einsteckbaren Query-Transformern, Routern, Neu-Rankern und Content-Injectoren für produktionsreife Pipelines. | Maximale Flexibilität, aber deutlich komplexer. |

**Dieses Tutorial verwendet den Native-Ansatz.** Jeder Schritt der RAG-Pipeline – das Einbetten der Anfrage, das Durchsuchen des Vektorspeichers, das Zusammenstellen des Kontexts und das Generieren der Antwort – ist explizit in [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) beschrieben. Dies ist bewusst so: als Lernressource ist es wichtiger, dass Sie jede Phase sehen und verstehen, als dass der Code minimal gehalten wird. Sobald Sie sich mit dem Zusammenspiel der Teile sicher fühlen, können Sie für schnelle Prototypen zu Easy RAG wechseln oder für Produktivsysteme Advanced RAG nutzen.

> **💡 Easy RAG schon gesehen?** Das [Quick Start-Modul](../00-quick-start/README.md) enthält ein Beispiel für Document Q&A ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)), das Easy RAG nutzt – LangChain4j übernimmt automatisch Embedding, Suche und Prompt-Erstellung. Dieses Modul geht einen Schritt weiter und öffnet diese Pipeline, sodass Sie jede Phase selbst sehen und steuern können.

Das folgende Diagramm zeigt die Easy RAG-Pipeline aus dem Quick Start-Beispiel. Beachten Sie, wie `AiServices` und `EmbeddingStoreContentRetriever` alle Komplexität verbergen – Sie laden ein Dokument, hängen einen Retriever an und erhalten Antworten. Der Native-Ansatz in diesem Modul öffnet jeden dieser versteckten Schritte:

<img src="../../../translated_images/de/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Dieses Diagramm zeigt die Easy RAG-Pipeline aus `SimpleReaderDemo.java`. Vergleichen Sie dies mit dem Native-Ansatz in diesem Modul: Easy RAG verbirgt Embedding, Retrieval und Prompt-Erstellung hinter `AiServices` und `ContentRetriever` – Sie laden ein Dokument, hängen einen Retriever an und erhalten Antworten. Der Native-Ansatz in diesem Modul öffnet diese Pipeline, sodass Sie jede Phase (einbetten, suchen, Kontext zusammenstellen, generieren) selbst aufrufen und volle Transparenz und Kontrolle haben.*

## Wie es funktioniert

Die RAG-Pipeline in diesem Modul gliedert sich in vier Phasen, die nacheinander ablaufen, sobald ein Benutzer eine Frage stellt. Zuerst wird ein hochgeladenes Dokument **geparst und in Chunks zerlegt** – handhabbare Stücke. Diese Chunks werden dann in **Vektor-Embeddings** umgewandelt und gespeichert, damit sie mathematisch verglichen werden können. Wenn eine Anfrage eintrifft, führt das System eine **semantische Suche** durch, um die relevantesten Chunks zu finden, und übergibt diese schließlich als Kontext an das LLM für die **Antwortgenerierung**. Die folgenden Abschnitte erläutern jede Phase mit tatsächlichem Code und Diagrammen. Sehen wir uns den ersten Schritt genauer an.

### Dokumentenverarbeitung

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Wenn Sie ein Dokument hochladen, wird es geparst (PDF oder reiner Text), mit Metadaten wie Dateiname versehen und anschließend in Chunks zerlegt – kleinere Abschnitte, die gut in das Kontextfenster des Modells passen. Diese Chunks überlappen leicht, damit an den Grenzen kein Kontext verloren geht.

```java
// Analysiere die hochgeladene Datei und verpacke sie in ein LangChain4j-Dokument
Document document = Document.from(content, metadata);

// Teile es in 300-Token-Stücke mit 30-Token-Überlappung auf
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Das folgende Diagramm zeigt, wie das visuell funktioniert. Beachten Sie, wie jeder Chunk einige Tokens mit den Nachbarn teilt – die 30-Token-Überlappung stellt sicher, dass kein wichtiger Kontext durch die Lappen geht:

<img src="../../../translated_images/de/document-chunking.a5df1dd1383431ed.webp" alt="Dokumenten-Chucking" width="800"/>

*Dieses Diagramm zeigt ein Dokument, aufgeteilt in 300-Token-Chunks mit 30-Token-Überlappung, um Kontext an den Chunk-Grenzen zu bewahren.*

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) und fragen Sie:  
> - „Wie zerlegt LangChain4j Dokumente in Chunks und warum ist Überlappung wichtig?“  
> - „Was ist die optimale Chunk-Größe für verschiedene Dokumenttypen und warum?“  
> - „Wie gehe ich mit mehrsprachigen Dokumenten oder spezieller Formatierung um?“

### Erstellung von Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Jeder Chunk wird in eine numerische Darstellung umgewandelt, die als Embedding bezeichnet wird – im Grunde ein Bedeutungs-zu-Zahlen-Wandler. Das Embedding-Modell ist nicht „intelligent“ wie ein Chat-Modell; es kann keine Anweisungen befolgen, nicht vernunftbasiert antworten oder Fragen beantworten. Es ordnet Text lediglich in einem mathematischen Raum an, in dem ähnliche Bedeutungen nah beieinander liegen – „Auto“ nahe „Automobil“, „Rückgaberichtlinie“ nahe „Geld zurück“. Man kann sich ein Chat-Modell als eine Person vorstellen, mit der man sprechen kann; ein Embedding-Modell ist ein hochleistungsfähiges Ablagesystem.

Das folgende Diagramm visualisiert dieses Konzept – Text geht rein, numerische Vektoren kommen raus, und ähnliche Bedeutungen erzeugen benachbarte Vektoren:

<img src="../../../translated_images/de/embedding-model-concept.90760790c336a705.webp" alt="Embedding-Modell Konzept" width="800"/>

*Dieses Diagramm zeigt, wie ein Embedding-Modell Text in numerische Vektoren umwandelt und ähnliche Bedeutungen – wie „Auto“ und „Automobil“ – im Vektorraum nahe beieinander platziert.*

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
  
Das Klassendiagramm unten zeigt die zwei getrennten Abläufe in einer RAG-Pipeline und die LangChain4j-Klassen, die sie implementieren. Der **Ingestion-Flow** (läuft einmal beim Upload) zerlegt das Dokument, bettet die Chunks ein und speichert sie via `.addAll()`. Der **Query-Flow** (läuft bei jeder Benutzeranfrage) bettet die Frage ein, durchsucht den Speicher via `.search()` und übergibt den passenden Kontext an das Chat-Modell. Beide Flows treffen sich an der gemeinsamen Schnittstelle `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/de/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Klassen" width="800"/>

*Dieses Diagramm zeigt die zwei Abläufe in einer RAG-Pipeline – Ingestion und Query – und wie sie über einen gemeinsamen EmbeddingStore verbunden sind.*

Sobald Embeddings gespeichert sind, clustern sich ähnliche Inhalte automatisch im Vektorraum. Die folgende Visualisierung zeigt, wie Dokumente zu verwandten Themen als nahe beieinander liegende Punkte erscheinen, was die semantische Suche ermöglicht:

<img src="../../../translated_images/de/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektor-Embeddings Raum" width="800"/>

*Diese Visualisierung zeigt, wie verwandte Dokumente im 3D-Vektorraum clustern, mit Themen wie technische Dokumentation, Geschäftsregeln und FAQs, die jeweils eigene Gruppen bilden.*

Wenn ein Benutzer eine Suche startet, folgen vier Schritte: Dokumente einmal einbetten, die Anfrage bei jeder Suche einbetten, den Anfragevektor mit allen gespeicherten Vektoren per Kosinusähnlichkeit vergleichen und die top-K höchstbewerteten Chunks zurückgeben. Das folgende Diagramm zeigt jeden Schritt und die beteiligten LangChain4j-Klassen:

<img src="../../../translated_images/de/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding-Suchschritte" width="800"/>

*Dieses Diagramm zeigt den vierstufigen Embedding-Suchprozess: Dokumente einbetten, Anfrage einbetten, Vektoren per Kosinusähnlichkeit vergleichen und die Top-K-Ergebnisse zurückgeben.*

### Semantische Suche

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Wenn Sie eine Frage stellen, wird auch Ihre Frage zu einem Embedding. Das System vergleicht das Embedding Ihrer Frage mit allen Embeddings der Dokumenten-Chunks. Es findet die Chunks mit den ähnlichsten Bedeutungen – nicht nur passende Schlüsselwörter, sondern tatsächliche semantische Ähnlichkeit.

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
  
Das folgende Diagramm stellt semantische Suche einer traditionellen Keyword-Suche gegenüber. Eine Keyword-Suche nach „Fahrzeug“ übersieht einen Chunk über „Autos und Lastwagen“, aber die semantische Suche versteht, dass sie dasselbe meinen, und gibt ihn als hoch bewertetes Ergebnis zurück:

<img src="../../../translated_images/de/semantic-search.6b790f21c86b849d.webp" alt="Semantische Suche" width="800"/>

*Dieses Diagramm vergleicht die schlüsselwortbasierte Suche mit der semantischen Suche und zeigt, wie die semantische Suche konzeptionell verwandte Inhalte abruft, auch wenn die exakten Keywords unterschiedlich sind.*
Unter der Haube wird Ähnlichkeit mit Kosinusähnlichkeit gemessen – im Wesentlichen wird gefragt: "Zeigen diese beiden Pfeile in dieselbe Richtung?" Zwei Abschnitte können völlig unterschiedliche Wörter verwenden, aber wenn sie dasselbe bedeuten, zeigen ihre Vektoren in dieselbe Richtung und erreichen einen Wert nahe 1,0:

<img src="../../../translated_images/de/cosine-similarity.9baeaf3fc3336abb.webp" alt="Kosinusähnlichkeit" width="800"/>

*Dieses Diagramm veranschaulicht die Kosinusähnlichkeit als den Winkel zwischen Einbettungsvektoren – besser ausgerichtete Vektoren erreichen Werte näher an 1,0, was auf eine höhere semantische Ähnlichkeit hinweist.*

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) und fragen Sie:
> - "Wie funktioniert die Ähnlichkeitssuche mit Einbettungen und was bestimmt den Wert?"
> - "Welchen Ähnlichkeitsschwellenwert sollte ich verwenden und wie wirkt sich das auf die Ergebnisse aus?"
> - "Wie gehe ich mit Fällen um, in denen keine relevanten Dokumente gefunden werden?"

### Antwortgenerierung

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Die relevantesten Abschnitte werden zu einem strukturierten Prompt zusammengefügt, der explizite Anweisungen, den abgerufenen Kontext und die Frage des Benutzers enthält. Das Modell liest diese spezifischen Abschnitte und antwortet basierend auf diesen Informationen – es kann nur das verwenden, was ihm gerade vorliegt, was Halluzinationen verhindert.

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

Das folgende Diagramm zeigt diese Zusammenstellung in Aktion – die höchstbewerteten Abschnitte aus dem Suchschritt werden in die Prompt-Vorlage eingefügt, und das `OpenAiOfficialChatModel` erzeugt eine fundierte Antwort:

<img src="../../../translated_images/de/context-assembly.7e6dd60c31f95978.webp" alt="Kontextzusammenstellung" width="800"/>

*Dieses Diagramm zeigt, wie die höchstbewerteten Abschnitte in einen strukturierten Prompt zusammengefügt werden, wodurch das Modell eine fundierte Antwort aus Ihren Daten generieren kann.*

## Anwendung starten

**Bereitstellung prüfen:**

Stellen Sie sicher, dass die `.env`-Datei mit Azure-Anmeldedaten im Stammverzeichnis vorhanden ist (wurde im Modul 01 erstellt). Führen Sie dies aus dem Modulverzeichnis (`03-rag/`) aus:

**Bash:**
```bash
cat ../.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```

**Starten Sie die Anwendung:**

> **Hinweis:** Wenn Sie alle Anwendungen bereits mit `./start-all.sh` aus dem Stammverzeichnis gestartet haben (wie in Modul 01 beschrieben), läuft dieses Modul bereits auf Port 8081. Sie können die Startbefehle unten überspringen und direkt zu http://localhost:8081 gehen.

**Option 1: Verwendung des Spring Boot Dashboards (Empfohlen für VS Code Benutzer)**

Der Dev-Container enthält die Erweiterung Spring Boot Dashboard, die eine visuelle Oberfläche zur Verwaltung aller Spring Boot Anwendungen bietet. Sie finden sie in der Aktivitätsleiste auf der linken Seite von VS Code (Achten Sie auf das Spring Boot Symbol).

Im Spring Boot Dashboard können Sie:
- Alle verfügbaren Spring Boot Anwendungen im Workspace sehen
- Anwendungen mit einem Klick starten/stoppen
- Echtzeit-Logs der Anwendungen einsehen
- Den Anwendungsstatus überwachen

Klicken Sie einfach auf den Play-Button neben „rag“, um dieses Modul zu starten, oder starten Sie alle Module auf einmal.

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

Beide Skripte laden automatisch die Umgebungsvariablen aus der `.env`-Datei im Stammverzeichnis und erzeugen die JARs, falls sie noch nicht existieren.

> **Hinweis:** Wenn Sie alle Module lieber manuell bauen möchten, bevor Sie starten:
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

Öffnen Sie http://localhost:8081 in Ihrem Browser.

**Zum Stoppen:**

**Bash:**
```bash
./stop.sh  # Nur dieses Modul
# Oder
cd .. && ./stop-all.sh  # Alle Module
```

**PowerShell:**
```powershell
.\stop.ps1  # Nur dieses Modul
# Oder
cd ..; .\stop-all.ps1  # Alle Module
```

## Anwendung benutzen

Die Anwendung bietet eine Weboberfläche zum Hochladen von Dokumenten und zur Fragestellung.

<a href="images/rag-homepage.png"><img src="../../../translated_images/de/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG-Anwendungsoberfläche" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Dieser Screenshot zeigt die RAG-Anwendungsoberfläche, in der Sie Dokumente hochladen und Fragen stellen können.*

### Dokument hochladen

Beginnen Sie mit dem Hochladen eines Dokuments – am besten eignen sich TXT-Dateien für Tests. In diesem Verzeichnis liegt eine Datei `sample-document.txt`, die Informationen über LangChain4j-Funktionen, RAG-Implementierung und Best Practices enthält – perfekt zum Testen des Systems.

Das System verarbeitet Ihr Dokument, teilt es in Abschnitte auf und erstellt Einbettungen für jeden Abschnitt. Dies geschieht automatisch beim Hochladen.

### Fragen stellen

Stellen Sie jetzt spezifische Fragen zum Dokumentinhalt. Versuchen Sie etwas Faktisches, das klar im Dokument steht. Das System sucht nach relevanten Abschnitten, fügt diese in den Prompt ein und generiert eine Antwort.

### Quellen prüfen

Beachten Sie, dass jede Antwort Quellenverweise mit Ähnlichkeitspunkten enthält. Diese Werte (0 bis 1) zeigen, wie relevant jeder Abschnitt für Ihre Frage war. Höhere Werte bedeuten bessere Übereinstimmungen. So können Sie die Antwort mit dem Quellmaterial abgleichen.

<a href="images/rag-query-results.png"><img src="../../../translated_images/de/rag-query-results.6d69fcec5397f355.webp" alt="RAG-Abfrageergebnisse" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Dieser Screenshot zeigt Abfrageergebnisse mit generierter Antwort, Quellenhinweisen und Relevanzwerten für jeden abgerufenen Abschnitt.*

### Experimentieren mit Fragen

Probieren Sie verschiedene Fragetypen aus:
- Konkrete Fakten: „Was ist das Hauptthema?“
- Vergleiche: „Was ist der Unterschied zwischen X und Y?“
- Zusammenfassungen: „Fassen Sie die wichtigsten Punkte zu Z zusammen“

Beobachten Sie, wie sich die Relevanzwerte ändern, je nachdem, wie gut Ihre Frage mit dem Dokumentinhalt übereinstimmt.

## Wichtige Konzepte

### Abschnittsstrategie

Dokumente werden in 300-Token-Abschnitte mit 30 Token Überlappung aufgeteilt. Dieses Gleichgewicht stellt sicher, dass jeder Abschnitt genügend Kontext enthält, um aussagekräftig zu sein, aber klein genug bleibt, um mehrere Abschnitte in einem Prompt unterzubringen.

### Ähnlichkeitswerte

Jeder abgerufene Abschnitt hat einen Ähnlichkeitswert zwischen 0 und 1, der zeigt, wie eng er zur Frage des Benutzers passt. Das folgende Diagramm visualisiert die Wertbereiche und wie das System sie zur Filterung der Ergebnisse nutzt:

<img src="../../../translated_images/de/similarity-scores.b0716aa911abf7f0.webp" alt="Ähnlichkeitswerte" width="800"/>

*Dieses Diagramm zeigt Wertbereiche von 0 bis 1 mit einem Mindestschwellenwert von 0,5, der irrelevante Abschnitte herausfiltert.*

Die Werte reichen von 0 bis 1:
- 0,7–1,0: Hoch relevant, exakte Übereinstimmung
- 0,5–0,7: Relevant, guter Kontext
- Unter 0,5: Herausgefiltert, zu unterschiedlich

Das System ruft nur Abschnitte oberhalb des Mindestschwellenwerts ab, um Qualität sicherzustellen.

Einbettungen funktionieren gut, wenn Bedeutungen klar gruppiert sind, aber sie haben Schwachstellen. Das folgende Diagramm zeigt häufige Fehlerfälle – zu große Abschnitte produzieren unscharfe Vektoren, zu kleine Abschnitte fehlen Kontext, mehrdeutige Begriffe zeigen auf mehrere Cluster, und exakte Nachschlagen (IDs, Teilenummern) funktionieren mit Einbettungen überhaupt nicht:

<img src="../../../translated_images/de/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Fehlermodi bei Einbettungen" width="800"/>

*Dieses Diagramm zeigt gängige Fehlerfälle bei Einbettungen: zu große Abschnitte, zu kleine Abschnitte, mehrdeutige Begriffe, die auf mehrere Cluster zeigen, und exakte Nachschlageoperationen wie IDs.*

### In-Memory-Speicherung

Dieses Modul verwendet der Einfachheit halber eine In-Memory-Speicherung. Beim Neustart der Anwendung gehen hochgeladene Dokumente verloren. Produktionssysteme verwenden persistente Vektor-Datenbanken wie Qdrant oder Azure AI Search.

### Kontextfenstermanagement

Jedes Modell hat ein maximales Kontextfenster. Sie können nicht jeden Abschnitt eines großen Dokuments einbeziehen. Das System ruft die N relevantesten Abschnitte (Standard 5) ab, um innerhalb der Grenzen zu bleiben und dennoch genügend Kontext für genaue Antworten zu bieten.

## Wann RAG wichtig ist

RAG ist nicht immer der richtige Ansatz. Die folgende Entscheidungshilfe hilft Ihnen zu bestimmen, wann RAG Mehrwert bietet und wann einfachere Ansätze – wie Inhalte direkt im Prompt einzubinden oder sich auf das Modellwissen zu verlassen – ausreichen:

<img src="../../../translated_images/de/when-to-use-rag.1016223f6fea26bc.webp" alt="Wann RAG verwenden" width="800"/>

*Dieses Diagramm zeigt eine Entscheidungshilfe, wann RAG Mehrwert bietet und wann einfachere Ansätze ausreichend sind.*

## Nächste Schritte

**Nächstes Modul:** [04-tools - KI-Agenten mit Tools](../04-tools/README.md)

---

**Navigation:** [← Vorheriges: Modul 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Zurück zum Hauptverzeichnis](../README.md) | [Nächstes: Modul 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, können automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten. Das Originaldokument in seiner Ursprungssprache gilt als maßgebliche Quelle. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Verwendung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->