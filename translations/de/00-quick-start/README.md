# Modul 00: Schnellstart

## Inhaltsverzeichnis

- [Einführung](../../../00-quick-start)
- [Was ist LangChain4j?](../../../00-quick-start)
- [LangChain4j Abhängigkeiten](../../../00-quick-start)
- [Voraussetzungen](../../../00-quick-start)
- [Einrichtung](../../../00-quick-start)
  - [1. Holen Sie sich Ihren GitHub-Token](../../../00-quick-start)
  - [2. Setzen Sie Ihren Token](../../../00-quick-start)
- [Führen Sie die Beispiele aus](../../../00-quick-start)
  - [1. Basic Chat](../../../00-quick-start)
  - [2. Prompt-Muster](../../../00-quick-start)
  - [3. Funktionsaufruf](../../../00-quick-start)
  - [4. Dokumenten Q&A (Easy RAG)](../../../00-quick-start)
  - [5. Verantwortungsbewusste KI](../../../00-quick-start)
- [Was jedes Beispiel zeigt](../../../00-quick-start)
- [Nächste Schritte](../../../00-quick-start)
- [Fehlerbehebung](../../../00-quick-start)

## Einführung

Dieser Schnellstart soll Ihnen helfen, LangChain4j so schnell wie möglich zum Laufen zu bringen. Er behandelt die absoluten Grundlagen des Aufbaus von KI-Anwendungen mit LangChain4j und GitHub Models. In den nächsten Modulen wechseln Sie zu Azure OpenAI und GPT-5.2 und gehen tiefer auf jedes Konzept ein.

## Was ist LangChain4j?

LangChain4j ist eine Java-Bibliothek, die das Erstellen von KI-gestützten Anwendungen vereinfacht. Anstatt mit HTTP-Clients und JSON-Parsing zu arbeiten, verwenden Sie reine Java-APIs.

Die „Chain“ in LangChain bezieht sich darauf, mehrere Komponenten zu verketten – Sie können zum Beispiel eine Eingabeaufforderung mit einem Modell und einem Parser verbinden oder mehrere KI-Aufrufe aneinanderreihen, bei denen eine Ausgabe als Eingabe für den nächsten Schritt dient. Dieser Schnellstart konzentriert sich auf die Grundlagen, bevor komplexere Ketten erkundet werden.

<img src="../../../translated_images/de/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Verkettungskonzept" width="800"/>

*Verkettung von Komponenten in LangChain4j – Bausteine verbinden sich, um leistungsstarke KI-Workflows zu erstellen*

Wir verwenden drei Kernkomponenten:

**ChatModel** – Die Schnittstelle für KI-Modell-Interaktionen. Rufen Sie `model.chat("prompt")` auf und erhalten Sie eine Antwort als String. Wir verwenden `OpenAiOfficialChatModel`, das mit OpenAI-kompatiblen Endpunkten wie GitHub Models funktioniert.

**AiServices** – Erstellt typsichere KI-Service-Schnittstellen. Definieren Sie Methoden, annotieren Sie sie mit `@Tool` und LangChain4j übernimmt die Orchestrierung. Die KI ruft Ihre Java-Methoden automatisch auf, wenn nötig.

**MessageWindowChatMemory** – Hält den Gesprächsverlauf aufrecht. Ohne dies ist jede Anfrage unabhängig. Damit erinnert sich die KI an vorherige Nachrichten und behält den Kontext über mehrere Runden.

<img src="../../../translated_images/de/architecture.eedc993a1c576839.webp" alt="LangChain4j Architektur" width="800"/>

*LangChain4j Architektur – Kernkomponenten arbeiten zusammen, um Ihre KI-Anwendungen anzutreiben*

## LangChain4j Abhängigkeiten

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

Das `langchain4j-open-ai-official`-Modul stellt die Klasse `OpenAiOfficialChatModel` bereit, die mit OpenAI-kompatiblen APIs kommuniziert. GitHub Models verwendet dasselbe API-Format, daher ist kein spezieller Adapter erforderlich – Sie müssen nur die Basis-URL auf `https://models.github.ai/inference` setzen.

Das `langchain4j-easy-rag`-Modul bietet automatisches Dokumenten-Splitting, Einbettung und Recherche, sodass Sie RAG-Anwendungen bauen können, ohne jeden Schritt manuell konfigurieren zu müssen.

## Voraussetzungen

**Verwenden Sie den Dev Container?** Java und Maven sind bereits installiert. Sie benötigen nur einen persönlichen GitHub Access Token.

**Lokale Entwicklung:**
- Java 21+, Maven 3.9+
- Persönlicher GitHub Access Token (Anleitung unten)

> **Hinweis:** Dieses Modul verwendet `gpt-4.1-nano` von GitHub Models. Ändern Sie den Modellnamen im Code nicht – er ist konfiguriert für die verfügbaren GitHub-Modelle.

## Einrichtung

### 1. Holen Sie sich Ihren GitHub-Token

1. Gehen Sie zu [GitHub Einstellungen → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Klicken Sie auf „Generate new token“
3. Geben Sie einen beschreibenden Namen ein (z. B. „LangChain4j Demo“)
4. Legen Sie ein Ablaufdatum fest (7 Tage empfohlen)
5. Unter „Account permissions“ suchen Sie „Models“ und setzen es auf „Read-only“
6. Klicken Sie auf „Generate token“
7. Kopieren und speichern Sie Ihren Token – Sie sehen ihn nicht wieder

### 2. Setzen Sie Ihren Token

**Option 1: Verwendung von VS Code (Empfohlen)**

Wenn Sie VS Code verwenden, fügen Sie Ihren Token zur `.env`-Datei im Projektstammverzeichnis hinzu:

Falls die `.env`-Datei nicht existiert, kopieren Sie `.env.example` nach `.env` oder erstellen Sie eine neue `.env`-Datei im Projektstammverzeichnis.

**Beispiel `.env`-Datei:**
```bash
# In /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Danach können Sie einfach mit der rechten Maustaste auf eine beliebige Demo-Datei (z. B. `BasicChatDemo.java`) im Explorer klicken und **„Run Java“** auswählen oder die Startkonfigurationen aus dem Ausführen- und Debug-Panel verwenden.

**Option 2: Verwendung des Terminals**

Setzen Sie den Token als Umgebungsvariable:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Führen Sie die Beispiele aus

**Verwendung von VS Code:** Klicken Sie einfach mit der rechten Maustaste auf eine beliebige Demo-Datei im Explorer und wählen Sie **„Run Java“** oder verwenden Sie die Startkonfigurationen im Ausführen- und Debug-Panel (stellen Sie sicher, dass Sie Ihren Token zuerst in die `.env`-Datei eingetragen haben).

**Verwendung von Maven:** Alternativ können Sie die Beispiele auch über die Kommandozeile ausführen:

### 1. Basic Chat

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

Zeigt zero-shot, few-shot, Chain-of-thought und rollenbasierte Aufforderungen.

### 3. Funktionsaufruf

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

Die KI ruft Ihre Java-Methoden automatisch auf, wenn notwendig.

### 4. Dokumenten Q&A (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Stellen Sie Fragen zu Ihren Dokumenten mit Easy RAG, inklusive automatischer Einbettung und Recherche.

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

Beginnen Sie hier, um LangChain4j in seiner einfachsten Form zu sehen. Sie erstellen ein `OpenAiOfficialChatModel`, senden eine Eingabeaufforderung mit `.chat()` und erhalten eine Antwort. Dies demonstriert die Grundlage: wie man Modelle mit benutzerdefinierten Endpunkten und API-Schlüsseln initialisiert. Sobald Sie dieses Muster verstanden haben, baut alles Weitere darauf auf.

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
> - „Wie wechsle ich in diesem Code von GitHub Models zu Azure OpenAI?“
> - „Welche weiteren Parameter kann ich in OpenAiOfficialChatModel.builder() konfigurieren?“
> - „Wie füge ich Streaming-Antworten hinzu, anstatt auf die vollständige Antwort zu warten?“

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Jetzt, da Sie wissen, wie Sie mit einem Modell sprechen, sehen wir uns an, was Sie ihm sagen. Diese Demo verwendet dieselbe Modellkonfiguration, zeigt aber fünf verschiedene Prompt-Muster. Probieren Sie Zero-Shot-Prompts für direkte Anweisungen, Few-Shot-Prompts, die aus Beispielen lernen, Chain-of-Thought-Prompts, die den Denkprozess offenlegen, und rollenbasierte Prompts, die den Kontext setzen. Sie sehen, wie dasselbe Modell dramatisch unterschiedliche Ergebnisse liefert, je nachdem, wie Sie Ihre Anfrage formulieren.

Die Demo zeigt auch Prompt-Templates, ein kraftvolles Werkzeug, um wiederverwendbare Prompts mit Variablen zu erstellen.
Das folgende Beispiel zeigt ein Prompt unter Verwendung der LangChain4j `PromptTemplate`, um Variablen auszufüllen. Die KI antwortet basierend auf dem angegebenen Reiseziel und der Aktivität.

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
> - „Was ist der Unterschied zwischen Zero-Shot und Few-Shot Prompting, und wann verwende ich welches?“
> - „Wie beeinflusst der Temperaturparameter die Antworten des Modells?“
> - „Welche Techniken gibt es, um Prompt Injection Angriffe in der Produktion zu verhindern?“
> - „Wie erstelle ich wiederverwendbare PromptTemplate-Objekte für gängige Muster?“

**Tool Integration** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Hier wird LangChain4j mächtig. Sie verwenden `AiServices`, um einen KI-Assistenten zu erstellen, der Ihre Java-Methoden aufrufen kann. Annotieren Sie Methoden einfach mit `@Tool("Beschreibung")` und LangChain4j übernimmt den Rest – die KI entscheidet automatisch, welches Tool wann genutzt wird, basierend auf der Nutzeranfrage. Das demonstriert Funktionsaufrufe, eine Schlüsseltechnik zum Aufbau von KI, die handeln kann, nicht nur Fragen beantwortet.

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
> - „Was passiert, wenn ein Tool eine Ausnahme wirft – wie sollte ich Fehler behandeln?“
> - „Wie würde ich eine echte API anstelle dieses Taschenrechner-Beispiels integrieren?“

**Dokumenten Q&A (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Hier sehen Sie RAG (retrieval-augmented generation) mit LangChain4js "Easy RAG" Ansatz. Dokumente werden geladen, automatisch aufgeteilt und in einem In-Memory-Speicher eingebettet, dann liefert ein Content-Retriever relevante Ausschnitte zur Anfragezeit an die KI. Die KI antwortet basierend auf Ihren Dokumenten, nicht auf ihrem allgemeinen Wissen.

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
> - „Wie verhindert RAG KI-Halluzinationen im Vergleich zur Nutzung der Trainingsdaten des Modells?“
> - „Was ist der Unterschied zwischen diesem einfachen Ansatz und einer benutzerdefinierten RAG-Pipeline?“
> - „Wie würde ich das skalieren, um mehrere Dokumente oder größere Wissensbasen zu handhaben?“

**Verantwortungsbewusste KI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Bauen Sie KI-Sicherheit mit einem mehrschichtigen Ansatz. Diese Demo zeigt zwei Schutzebenen, die zusammenarbeiten:

**Teil 1: LangChain4j Input Guardrails** – Blockieren gefährlicher Prompts, bevor sie das LLM erreichen. Erstellen Sie eigene Guardrails, die auf verbotene Schlüsselwörter oder Muster prüfen. Diese laufen in Ihrem Code, sind also schnell und kostenlos.

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

**Teil 2: Provider Safety Filters** – GitHub Models verfügt über integrierte Filter, die Dinge abfangen, die Ihre Guardrails eventuell übersehen. Sie sehen harte Blockaden (HTTP 400 Fehler) für schwere Verstöße und sanfte Ablehnungen, wenn die KI höflich ablehnt.

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) und fragen Sie:
> - „Was ist InputGuardrail und wie erstelle ich meine eigenen?“
> - „Was ist der Unterschied zwischen einer harten Blockade und einer sanften Ablehnung?“
> - „Warum verwendet man Guardrails und Provider-Filter zusammen?“

## Nächste Schritte

**Nächstes Modul:** [01-introduction - Erste Schritte mit LangChain4j](../01-introduction/README.md)

---

**Navigation:** [← Zurück zur Hauptseite](../README.md) | [Weiter: Modul 01 - Einführung →](../01-introduction/README.md)

---

## Fehlerbehebung

### Erster Maven-Build

**Problem**: Erster `mvn clean compile` oder `mvn package` dauert lange (10-15 Minuten)

**Ursache**: Maven muss alle Projektabhängigkeiten (Spring Boot, LangChain4j-Bibliotheken, Azure SDKs usw.) beim ersten Build herunterladen.

**Lösung**: Das ist normales Verhalten. Nachfolgende Builds sind deutlich schneller, da die Abhängigkeiten lokal zwischengespeichert werden. Die Downloadzeit hängt von Ihrer Netzwerkgeschwindigkeit ab.

### PowerShell Maven-Befehls-Syntax

**Problem**: Maven-Befehle schlagen fehl mit dem Fehler `Unknown lifecycle phase ".mainClass=..."`
**Ursache**: PowerShell interpretiert `=` als Zuweisungsoperator, wodurch die Maven-Eigenschaftssyntax unterbrochen wird

**Lösung**: Verwenden Sie den Stop-Parsing-Operator `--%` vor dem Maven-Befehl:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Der Operator `--%` weist PowerShell an, alle verbleibenden Argumente wörtlich an Maven weiterzugeben, ohne sie zu interpretieren.

### Emoji-Anzeige in Windows PowerShell

**Problem**: KI-Antworten zeigen unlesbare Zeichen (z.B. `????` oder `â??`) anstelle von Emojis in PowerShell

**Ursache**: Die Standardcodierung von PowerShell unterstützt keine UTF-8 Emojis

**Lösung**: Führen Sie diesen Befehl vor dem Ausführen von Java-Anwendungen aus:
```cmd
chcp 65001
```

Dies erzwingt die UTF-8-Codierung im Terminal. Alternativ können Sie Windows Terminal verwenden, das eine bessere Unicode-Unterstützung bietet.

### Debugging von API-Aufrufen

**Problem**: Authentifizierungsfehler, Ratenbegrenzungen oder unerwartete Antworten vom KI-Modell

**Lösung**: Die Beispiele enthalten `.logRequests(true)` und `.logResponses(true)`, um API-Aufrufe in der Konsole anzuzeigen. Dies hilft, Authentifizierungsfehler, Ratenbegrenzungen oder unerwartete Antworten zu diagnostizieren. Entfernen Sie diese Flags in der Produktion, um die Protokollierung zu reduzieren.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir um Genauigkeit bemüht sind, kann diese automatisierte Übersetzung Fehler oder Ungenauigkeiten enthalten. Das Originaldokument in seiner Originalsprache ist als maßgebliche Quelle anzusehen. Für kritische Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Nutzung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->