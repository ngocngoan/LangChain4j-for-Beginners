# Modul 00: Schnellstart

## Inhaltsverzeichnis

- [Einführung](../../../00-quick-start)
- [Was ist LangChain4j?](../../../00-quick-start)
- [LangChain4j-Abhängigkeiten](../../../00-quick-start)
- [Voraussetzungen](../../../00-quick-start)
- [Einrichtung](../../../00-quick-start)
  - [1. Holen Sie sich Ihr GitHub-Token](../../../00-quick-start)
  - [2. Legen Sie Ihr Token fest](../../../00-quick-start)
- [Führen Sie die Beispiele aus](../../../00-quick-start)
  - [1. Basis-Chat](../../../00-quick-start)
  - [2. Prompt-Muster](../../../00-quick-start)
  - [3. Funktionsaufruf](../../../00-quick-start)
  - [4. Dokument Q&A (Easy RAG)](../../../00-quick-start)
  - [5. Verantwortungsbewusste KI](../../../00-quick-start)
- [Was jedes Beispiel zeigt](../../../00-quick-start)
- [Nächste Schritte](../../../00-quick-start)
- [Fehlerbehebung](../../../00-quick-start)

## Einführung

Dieser Schnellstart soll Sie so schnell wie möglich mit LangChain4j zum Laufen bringen. Er behandelt die absoluten Grundlagen zum Erstellen von KI-Anwendungen mit LangChain4j und GitHub-Modellen. In den nächsten Modulen verwenden Sie Azure OpenAI mit LangChain4j, um fortgeschrittenere Anwendungen zu entwickeln.

## Was ist LangChain4j?

LangChain4j ist eine Java-Bibliothek, die den Aufbau KI-gestützter Anwendungen vereinfacht. Anstatt sich mit HTTP-Clients und JSON-Parsing zu beschäftigen, arbeiten Sie mit sauberen Java-APIs.

Die „Kette“ in LangChain bezieht sich darauf, mehrere Komponenten zu verketten – Sie könnten einen Prompt an ein Modell und dann an einen Parser ketten oder mehrere KI-Aufrufe hintereinander schalten, wobei eine Ausgabe in den nächsten Eingang übergeht. Dieser Schnellstart konzentriert sich auf die Grundlagen, bevor komplexere Ketten erkundet werden.

<img src="../../../translated_images/de/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Verkettungskonzept" width="800"/>

*Verkettung von Komponenten in LangChain4j – Bausteine verbinden sich, um leistungsfähige KI-Workflows zu erstellen*

Wir verwenden drei Kernkomponenten:

**ChatModel** – Die Schnittstelle für KI-Modellinteraktionen. Rufen Sie `model.chat("prompt")` auf und erhalten Sie eine Antwort als String. Wir verwenden `OpenAiOfficialChatModel`, das mit OpenAI-kompatiblen Endpunkten wie GitHub-Modellen funktioniert.

**AiServices** – Erstellt typsichere KI-Service-Schnittstellen. Definieren Sie Methoden, annotieren Sie sie mit `@Tool` und LangChain4j übernimmt die Orchestrierung. Die KI ruft Ihre Java-Methoden bei Bedarf automatisch auf.

**MessageWindowChatMemory** – Pflegt die Gesprächshistorie. Ohne diese ist jede Anfrage unabhängig. Mit ihr erinnert sich die KI an vorherige Nachrichten und hält über mehrere Runden Kontext.

<img src="../../../translated_images/de/architecture.eedc993a1c576839.webp" alt="LangChain4j Architektur" width="800"/>

*LangChain4j-Architektur – Kernkomponenten arbeiten zusammen, um Ihre KI-Anwendungen zu betreiben*

## LangChain4j-Abhängigkeiten

Dieser Schnellstart verwendet drei Maven-Abhängigkeiten in der [`pom.xml`](../../../00-quick-start/pom.xml):

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

Das Modul `langchain4j-open-ai-official` stellt die Klasse `OpenAiOfficialChatModel` bereit, die sich mit OpenAI-kompatiblen APIs verbindet. GitHub Modelle verwenden denselben API-Standard, sodass kein spezieller Adapter erforderlich ist – geben Sie einfach die Basis-URL `https://models.github.ai/inference` an.

Das Modul `langchain4j-easy-rag` bietet automatische Dokumentaufteilung, Einbettung und Abruf, damit Sie RAG-Anwendungen bauen können, ohne jeden Schritt manuell konfigurieren zu müssen.

## Voraussetzungen

**Verwenden Sie den Dev Container?** Java und Maven sind bereits installiert. Sie benötigen nur ein GitHub Personal Access Token.

**Lokale Entwicklung:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (Anleitung unten)

> **Hinweis:** Dieses Modul verwendet `gpt-4.1-nano` von GitHub Modellen. Ändern Sie den Modellnamen im Code nicht – er ist so konfiguriert, dass er mit GitHubs verfügbaren Modellen funktioniert.

## Einrichtung

### 1. Holen Sie sich Ihr GitHub-Token

1. Gehen Sie zu [GitHub Einstellungen → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Klicken Sie auf „Generate new token“
3. Geben Sie einen beschreibenden Namen ein (z.B. „LangChain4j Demo“)
4. Legen Sie das Ablaufdatum fest (7 Tage empfohlen)
5. Unter „Account permissions“ suchen Sie „Models“ und setzen es auf „Read-only“
6. Klicken Sie auf „Generate token“
7. Kopieren Sie Ihr Token und speichern Sie es – Sie sehen es nicht noch einmal

### 2. Legen Sie Ihr Token fest

**Option 1: Verwendung von VS Code (empfohlen)**

Wenn Sie VS Code verwenden, fügen Sie Ihr Token der `.env`-Datei im Projektstamm hinzu:

Falls die `.env`-Datei nicht existiert, kopieren Sie `.env.example` nach `.env` oder erstellen Sie eine neue `.env`-Datei im Projektstamm.

**Beispiel `.env`-Datei:**
```bash
# In /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Dann können Sie einfach im Explorer mit der rechten Maustaste auf eine Demo-Datei (z.B. `BasicChatDemo.java`) klicken und **„Run Java“** auswählen oder die Launch-Konfigurationen im Run & Debug-Panel verwenden.

**Option 2: Verwendung des Terminals**

Setzen Sie das Token als Umgebungsvariable:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Führen Sie die Beispiele aus

**Mit VS Code:** Klicken Sie einfach mit der rechten Maustaste auf eine Demo-Datei im Explorer und wählen Sie **„Run Java“**, oder verwenden Sie die Launch-Konfigurationen im Run & Debug-Panel (stellen Sie sicher, dass Sie Ihr Token zuerst der `.env`-Datei hinzugefügt haben).

**Mit Maven:** Alternativ können Sie es auch über die Befehlszeile ausführen:

### 1. Basis-Chat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Prompt-Muster

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Zeigt Zero-Shot, Few-Shot, Chain-of-Thought und Rollenbasiertes Prompting.

### 3. Funktionsaufruf

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

Die KI ruft automatisch Ihre Java-Methoden auf, wenn nötig.

### 4. Dokument Q&A (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Stellen Sie Fragen zu Ihren Dokumenten mit Easy RAG mit automatischer Einbettung und Abruf.

### 5. Verantwortungsbewusste KI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Sehen Sie, wie KI-Sicherheitsfilter schädliche Inhalte blockieren.

## Was jedes Beispiel zeigt

**Basic Chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Beginnen Sie hier, um LangChain4j in seiner einfachsten Form kennenzulernen. Sie erstellen ein `OpenAiOfficialChatModel`, senden einen Prompt mit `.chat()` und erhalten eine Antwort zurück. Das zeigt die Grundlage: wie man Modelle mit benutzerdefinierten Endpunkten und API-Schlüsseln initialisiert. Wenn Sie dieses Muster verstanden haben, baut alles andere darauf auf.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) und fragen Sie:
> - „Wie würde ich in diesem Code von GitHub Models zu Azure OpenAI wechseln?“
> - „Welche anderen Parameter kann ich in OpenAiOfficialChatModel.builder() konfigurieren?“
> - „Wie füge ich Streaming-Antworten hinzu, anstatt auf die vollständige Antwort zu warten?“

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Nun, da Sie wissen, wie man mit einem Modell spricht, schauen wir uns an, was Sie ihm sagen. Diese Demo verwendet dieselbe Modellkonfiguration, zeigt aber fünf verschiedene Prompt-Muster. Probieren Sie Zero-Shot-Prompts für direkte Anweisungen, Few-Shot-Prompts, die aus Beispielen lernen, Chain-of-Thought-Prompts, die die Denkprozesse offenlegen, und rollenbasierte Prompts, die den Kontext festlegen. Sie sehen, wie dasselbe Modell dramatisch unterschiedliche Ergebnisse liefert, je nachdem, wie Sie Ihre Anfrage formulieren.

Die Demo zeigt auch Prompt-Templates, die eine leistungsstarke Möglichkeit bieten, wiederverwendbare Prompts mit Variablen zu erstellen.
Das folgende Beispiel zeigt einen Prompt mit dem LangChain4j `PromptTemplate`, der Variablen ausfüllt. Die KI antwortet basierend auf dem angegebenen Ziel und der Aktivität.

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) und fragen Sie:
> - „Was ist der Unterschied zwischen Zero-Shot- und Few-Shot-Prompting, und wann sollte ich welches verwenden?“
> - „Wie beeinflusst der Temperatur-Parameter die Antworten des Modells?“
> - „Welche Techniken gibt es, um Prompt Injection Angriffe in der Produktion zu verhindern?“
> - „Wie kann ich wiederverwendbare PromptTemplate-Objekte für häufige Muster erstellen?“

**Tool Integration** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Hier wird LangChain4j mächtig. Sie verwenden `AiServices`, um einen KI-Assistenten zu erstellen, der Ihre Java-Methoden aufrufen kann. Annotieren Sie Methoden einfach mit `@Tool("Beschreibung")` und LangChain4j erledigt den Rest – die KI entscheidet automatisch, wann jedes Tool aufgrund der Benutzeranfrage verwendet wird. Dies zeigt Funktionsaufrufe, eine Schlüsseltechnik, um KI zu bauen, die Aktionen ausführen kann, nicht nur Fragen beantwortet.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) und fragen Sie:
> - „Wie funktioniert die @Tool-Annotation und was macht LangChain4j damit hinter den Kulissen?“
> - „Kann die KI mehrere Tools nacheinander aufrufen, um komplexe Probleme zu lösen?“
> - „Was passiert, wenn ein Tool eine Ausnahme wirft – wie soll ich Fehler behandeln?“
> - „Wie würde ich eine echte API anstelle dieses Taschenrechner-Beispiels integrieren?“

**Dokument Q&A (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Hier sehen Sie RAG (retrieval-augmented generation) mit LangChain4js „Easy RAG“-Ansatz. Dokumente werden geladen, automatisch aufgeteilt und in einen In-Memory-Speicher eingebettet, dann liefert ein Inhalts-Abrufer relevante Ausschnitte zur Laufzeit an die KI. Die KI antwortet basierend auf Ihren Dokumenten, nicht auf ihrem allgemeinen Wissen.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) und fragen Sie:
> - „Wie verhindert RAG KI-Halluzinationen im Vergleich zur Verwendung der Trainingsdaten des Modells?“
> - „Was ist der Unterschied zwischen diesem einfachen Ansatz und einer benutzerdefinierten RAG-Pipeline?“
> - „Wie würde ich das skalieren, um mehrere Dokumente oder größere Wissensdatenbanken zu verarbeiten?“

**Verantwortungsbewusste KI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Bauen Sie KI-Sicherheit mit Verteidigung in der Tiefe auf. Diese Demo zeigt zwei Schutzebenen, die zusammenarbeiten:

**Teil 1: LangChain4j Input Guardrails** – Blockieren gefährlicher Prompts, bevor sie das LLM erreichen. Erstellen Sie benutzerdefinierte Guardrails, die nach verbotenen Schlüsselwörtern oder Mustern suchen. Diese laufen im Code, sind also schnell und kostenlos.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**Teil 2: Anbieter-Sicherheitsfilter** – GitHub Modelle verfügen über eingebaute Filter, die das abfangen, was Ihre Guardrails womöglich übersehen. Sie sehen harte Blockierungen (HTTP 400 Fehler) bei schweren Verstößen und weiche Ablehnungen, bei denen die KI höflich verweigert.

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) und fragen Sie:
> - „Was ist InputGuardrail und wie erstelle ich meinen eigenen?“
> - „Was ist der Unterschied zwischen einer harten Blockierung und einer weichen Ablehnung?“
> - „Warum beide, Guardrails und Anbieterfilter, zusammen verwenden?“

## Nächste Schritte

**Nächstes Modul:** [01-introduction – Erste Schritte mit LangChain4j und gpt-5 auf Azure](../01-introduction/README.md)

---

**Navigation:** [← Zurück zum Hauptmenü](../README.md) | [Weiter: Modul 01 – Einführung →](../01-introduction/README.md)

---

## Fehlerbehebung

### Erster Maven-Build

**Problem:** Der erste `mvn clean compile` oder `mvn package` dauert sehr lange (10–15 Minuten)

**Ursache:** Maven muss beim ersten Build alle Projektabhängigkeiten (Spring Boot, LangChain4j-Bibliotheken, Azure SDKs etc.) herunterladen.

**Lösung:** Dies ist normales Verhalten. Nachfolgende Builds sind viel schneller, da Abhängigkeiten lokal zwischengespeichert werden. Die Downloadzeit hängt von Ihrer Netzwerkgeschwindigkeit ab.

### PowerShell Maven-Befehlssyntax

**Problem:** Maven-Befehle schlagen fehl mit dem Fehler `Unknown lifecycle phase ".mainClass=..."`
**Ursache**: PowerShell interpretiert `=` als Zuweisungsoperator für Variablen, wodurch die Maven-Property-Syntax unterbrochen wird

**Lösung**: Verwenden Sie den Stop-Parsing-Operator `--%` vor dem Maven-Befehl:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Der Operator `--%` weist PowerShell an, alle verbleibenden Argumente wortwörtlich an Maven weiterzugeben, ohne sie zu interpretieren.

### Windows PowerShell Emoji-Anzeige

**Problem**: KI-Antworten zeigen Müllzeichen (z. B. `????` oder `â??`) statt Emojis in PowerShell an

**Ursache**: Die Standardcodierung von PowerShell unterstützt keine UTF-8-Emojis

**Lösung**: Führen Sie diesen Befehl vor dem Ausführen von Java-Anwendungen aus:
```cmd
chcp 65001
```

Dies erzwingt die UTF-8-Codierung im Terminal. Alternativ können Sie Windows Terminal verwenden, das eine bessere Unicode-Unterstützung bietet.

### API-Aufrufe debuggen

**Problem**: Authentifizierungsfehler, Ratenbegrenzungen oder unerwartete Antworten vom KI-Modell

**Lösung**: Die Beispiele enthalten `.logRequests(true)` und `.logResponses(true)`, um API-Aufrufe in der Konsole anzuzeigen. Dies hilft beim Beheben von Authentifizierungsfehlern, Ratenbegrenzungen oder unerwarteten Antworten. Entfernen Sie diese Flags in der Produktionsumgebung, um die Protokollierung zu reduzieren.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir auf Genauigkeit achten, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Ursprache ist als maßgebliche Quelle zu betrachten. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Nutzung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->