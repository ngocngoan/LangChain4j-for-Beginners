# Modul 00: Schnellstart

## Inhaltsverzeichnis

- [Einführung](../../../00-quick-start)
- [Was ist LangChain4j?](../../../00-quick-start)
- [LangChain4j Abhängigkeiten](../../../00-quick-start)
- [Voraussetzungen](../../../00-quick-start)
- [Setup](../../../00-quick-start)
  - [1. Holen Sie sich Ihr GitHub-Token](../../../00-quick-start)
  - [2. Setzen Sie Ihr Token](../../../00-quick-start)
- [Führen Sie die Beispiele aus](../../../00-quick-start)
  - [1. Basis-Chat](../../../00-quick-start)
  - [2. Prompt-Muster](../../../00-quick-start)
  - [3. Funktionsaufruf](../../../00-quick-start)
  - [4. Dokument Q&A (RAG)](../../../00-quick-start)
  - [5. Verantwortliche KI](../../../00-quick-start)
- [Was jedes Beispiel zeigt](../../../00-quick-start)
- [Nächste Schritte](../../../00-quick-start)
- [Fehlerbehebung](../../../00-quick-start)

## Einführung

Dieser Schnellstart soll Ihnen helfen, LangChain4j so schnell wie möglich zum Laufen zu bringen. Er behandelt die absoluten Grundlagen zum Erstellen von KI-Anwendungen mit LangChain4j und GitHub Models. In den nächsten Modulen verwenden Sie Azure OpenAI mit LangChain4j, um fortgeschrittenere Anwendungen zu bauen.

## Was ist LangChain4j?

LangChain4j ist eine Java-Bibliothek, die das Bauen von KI-gestützten Anwendungen vereinfacht. Anstatt sich mit HTTP-Clients und JSON-Parsing zu beschäftigen, arbeiten Sie mit sauberen Java-APIs.

Das "Chain" in LangChain bezieht sich darauf, mehrere Komponenten aneinanderzureihen – Sie können eine Eingabeaufforderung an ein Modell an einen Parser reihen oder mehrere KI-Aufrufe verknüpfen, bei denen eine Ausgabe in die nächste Eingabe fließt. Dieser Schnellstart konzentriert sich auf die Grundlagen, bevor komplexere Ketten erkundet werden.

<img src="../../../translated_images/de/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Kettenbildung von Komponenten in LangChain4j – Bausteine verbinden sich zu leistungsfähigen KI-Workflows*

Wir verwenden drei Kernkomponenten:

**ChatLanguageModel** – Die Schnittstelle für KI-Modell-Interaktionen. Rufen Sie `model.chat("prompt")` auf und erhalten Sie eine Antwortzeichenkette. Wir verwenden `OpenAiOfficialChatModel`, das mit OpenAI-kompatiblen Endpunkten wie GitHub Models funktioniert.

**AiServices** – Erstellt typsichere KI-Service-Schnittstellen. Definieren Sie Methoden, annotieren Sie sie mit `@Tool`, und LangChain4j übernimmt die Orchestrierung. Die KI ruft Ihre Java-Methoden automatisch auf, wenn nötig.

**MessageWindowChatMemory** – Pflegt den Gesprächsverlauf. Ohne diesen ist jede Anfrage unabhängig. Mit ihm merkt sich die KI vorherige Nachrichten und behält den Kontext über mehrere Runden.

<img src="../../../translated_images/de/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j-Architektur – Kernkomponenten arbeiten zusammen, um Ihre KI-Anwendungen mit Leistung zu versorgen*

## LangChain4j Abhängigkeiten

Dieser Schnellstart verwendet zwei Maven-Abhängigkeiten in der [`pom.xml`](../../../00-quick-start/pom.xml):

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
```
  
Das Modul `langchain4j-open-ai-official` stellt die Klasse `OpenAiOfficialChatModel` bereit, die sich mit OpenAI-kompatiblen APIs verbindet. GitHub Models verwendet dasselbe API-Format, daher wird kein spezieller Adapter benötigt – zeigen Sie einfach die Basis-URL auf `https://models.github.ai/inference`.

## Voraussetzungen

**Verwenden Sie den Dev Container?** Java und Maven sind bereits installiert. Sie benötigen nur ein GitHub Personal Access Token.

**Lokale Entwicklung:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (Anleitung unten)

> **Hinweis:** Dieses Modul nutzt `gpt-4.1-nano` von GitHub Models. Ändern Sie den Modellnamen im Code nicht – es ist für die verfügbaren GitHub-Modelle konfiguriert.

## Setup

### 1. Holen Sie sich Ihr GitHub-Token

1. Gehen Sie zu [GitHub Einstellungen → Personal Access Tokens](https://github.com/settings/personal-access-tokens)  
2. Klicken Sie auf „Generate new token“  
3. Geben Sie einen beschreibenden Namen ein (z. B. „LangChain4j Demo“)  
4. Legen Sie eine Ablaufzeit fest (7 Tage empfohlen)  
5. Unter „Account permissions“ finden Sie „Models“ und setzen es auf „Read-only“  
6. Klicken Sie auf „Generate token“  
7. Kopieren und sichern Sie Ihr Token – Sie sehen es danach nicht wieder  

### 2. Setzen Sie Ihr Token

**Option 1: Nutzung von VS Code (empfohlen)**

Wenn Sie VS Code verwenden, fügen Sie Ihr Token der `.env`-Datei im Projektstamm hinzu:

Wenn die `.env`-Datei nicht existiert, kopieren Sie `.env.example` zu `.env` oder erstellen Sie eine neue `.env`-Datei im Projektstamm.

**Beispiel `.env`-Datei:**  
```bash
# In /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```
  
Dann können Sie einfach per Rechtsklick auf eine Demo-Datei (z. B. `BasicChatDemo.java`) im Explorer **"Run Java"** auswählen oder die Startkonfigurationen im Run and Debug-Panel benutzen.

**Option 2: Nutzung über Terminal**

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

**Mit VS Code:** Einfach per Rechtsklick auf eine Demo-Datei im Explorer **"Run Java"** wählen oder die Startkonfigurationen im Run and Debug-Panel benutzen (stellen Sie sicher, dass Sie vorher Ihr Token zur `.env`-Datei hinzugefügt haben).

**Mit Maven:** Alternativ können Sie die Beispiele über die Kommandozeile starten:

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
  
Zeigt Zero-Shot, Few-Shot, Chain-of-Thought und rollenbasierte Prompts.

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

### 4. Dokument Q&A (RAG)

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
Stellen Sie Fragen zum Inhalt in `document.txt`.

### 5. Verantwortliche KI

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

**Basis-Chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Starten Sie hier, um LangChain4j in seiner einfachsten Form zu sehen. Sie erstellen ein `OpenAiOfficialChatModel`, senden eine Eingabeaufforderung mit `.chat()` und erhalten eine Antwort zurück. Dies zeigt die Grundlage: wie man Modelle mit benutzerdefinierten Endpunkten und API-Schlüsseln initialisiert. Sobald Sie dieses Muster verstanden haben, baut alles Weitere darauf auf.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```
  
> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) und fragen Sie:  
> - "Wie wechsle ich in diesem Code von GitHub Models zu Azure OpenAI?"  
> - "Welche weiteren Parameter kann ich in OpenAiOfficialChatModel.builder() konfigurieren?"  
> - "Wie füge ich Streaming-Antworten hinzu, anstatt auf die komplette Antwort zu warten?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Jetzt, da Sie wissen, wie Sie mit einem Modell sprechen, schauen wir, was Sie ihm sagen. Dieses Demo verwendet dieselbe Modellausrichtung, zeigt aber fünf verschiedene Prompt-Muster. Probieren Sie Zero-Shot-Prompts für direkte Anweisungen, Few-Shot-Prompts, die aus Beispielen lernen, Chain-of-Thought-Prompts, die Denkprozesse offenbaren, und rollenbasierte Prompts, die Kontext setzen. Sie werden sehen, wie dasselbe Modell je nach Formulierung Ihrer Anfrage dramatisch unterschiedliche Ergebnisse liefert.

Die Demo zeigt auch Prompt-Vorlagen, eine kraftvolle Methode, um wiederverwendbare Prompts mit Variablen zu erstellen. Das folgende Beispiel zeigt einen Prompt mit der LangChain4j `PromptTemplate`, um Variablen zu füllen. Die KI antwortet basierend auf dem angegebenen Zielort und der Aktivität.

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
> - "Was ist der Unterschied zwischen Zero-Shot und Few-Shot Prompting, und wann sollte ich welches verwenden?"  
> - "Wie beeinflusst der Temperatur-Parameter die Antworten des Modells?"  
> - "Welche Techniken gibt es, um Prompt-Injection-Angriffe in der Produktion zu verhindern?"  
> - "Wie erstelle ich wiederverwendbare PromptTemplate-Objekte für gängige Muster?"

**Tool-Integration** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Hier wird LangChain4j mächtig. Sie verwenden `AiServices`, um einen KI-Assistenten zu erstellen, der Ihre Java-Methoden aufrufen kann. Annotieren Sie Methoden einfach mit `@Tool("Beschreibung")` und LangChain4j übernimmt den Rest – die KI entscheidet automatisch, wann sie welches Tool verwendet, basierend auf den Nutzeranfragen. Dies demonstriert Funktionsaufrufe, eine Schlüsseltechnik zum Bauen von KI, die Aktionen ausführen kann, nicht nur Fragen beantwortet.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```
  
> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) und fragen Sie:  
> - "Wie funktioniert die @Tool-Annotation und was macht LangChain4j damit hinter den Kulissen?"  
> - "Kann die KI mehrere Tools hintereinander aufrufen, um komplexe Probleme zu lösen?"  
> - "Was passiert, wenn ein Tool eine Ausnahme wirft – wie sollte ich Fehler behandeln?"  
> - "Wie integriere ich eine echte API statt dieses Rechner-Beispiels?"

**Dokument Q&A (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Hier sehen Sie die Grundlage von RAG (retrieval-augmented generation). Anstatt sich nur auf Trainingsdaten des Modells zu verlassen, laden Sie Inhalte aus [`document.txt`](../../../00-quick-start/document.txt) und beziehen diese in den Prompt ein. Die KI antwortet basierend auf Ihrem Dokument, nicht auf allgemeinem Wissen. Dies ist der erste Schritt, um Systeme zu bauen, die mit Ihren eigenen Daten arbeiten können.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```
  
> **Hinweis:** Dieser einfache Ansatz lädt das gesamte Dokument in den Prompt. Bei großen Dateien (>10 KB) überschreiten Sie die Kontextgrenzen. Modul 03 behandelt Chunking und Vektorsuche für produktive RAG-Systeme.

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) und fragen Sie:  
> - "Wie verhindert RAG KI-Halluzinationen im Vergleich zur Nutzung der Trainingsdaten des Modells?"  
> - "Was ist der Unterschied zwischen diesem einfachen Ansatz und der Nutzung von Vektor-Embeddings für Retrieval?"  
> - "Wie skaliere ich das, um mehrere Dokumente oder größere Wissensdatenbanken zu handhaben?"  
> - "Was sind Best Practices zur Strukturierung des Prompts, damit die KI nur den bereitgestellten Kontext nutzt?"

**Verantwortliche KI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Bauen Sie KI-Sicherheit mit Verteidigungsprinzipien in der Tiefe. Diese Demo zeigt zwei Schutzebenen, die zusammenarbeiten:

**Teil 1: LangChain4j Input Guardrails** – Blockieren gefährlicher Prompts, bevor sie das LLM erreichen. Erstellen Sie individuelle Schutzschienen, die nach verbotenen Schlüsselwörtern oder Mustern suchen. Diese laufen in Ihrem Code, sind schnell und kostenlos.

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
  
**Teil 2: Provider Sicherheitsfilter** – GitHub Models hat eingebaute Filter, die das abfangen, was Ihre Guardrails eventuell übersehen. Sie werden harte Sperren (HTTP 400 Fehler) bei schweren Verstößen und sanfte Ablehnungen sehen, bei denen die KI höflich ablehnt.

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) und fragen Sie:  
> - "Was ist InputGuardrail und wie erstelle ich meine eigenen?"  
> - "Was ist der Unterschied zwischen einer harten Sperre und einer sanften Ablehnung?"  
> - "Warum sollte man Guardrails und Provider-Filter zusammen verwenden?"

## Nächste Schritte

**Nächstes Modul:** [01-introduction – Einstieg mit LangChain4j und gpt-5 auf Azure](../01-introduction/README.md)

---

**Navigation:** [← Zurück zur Übersicht](../README.md) | [Weiter: Modul 01 – Einführung →](../01-introduction/README.md)

---

## Fehlerbehebung

### Erster Maven-Build

**Problem:** Erster `mvn clean compile` oder `mvn package` dauert lange (10-15 Minuten)

**Ursache:** Maven lädt beim ersten Build alle Projektabhängigkeiten (Spring Boot, LangChain4j-Bibliotheken, Azure SDKs usw.) herunter.

**Lösung:** Das ist normales Verhalten. Nachfolgende Builds sind viel schneller, da die Abhängigkeiten lokal zwischengespeichert sind. Die Downloaddauer hängt von Ihrer Netzwerkgeschwindigkeit ab.
### PowerShell Maven-Befehlssyntax

**Problem**: Maven-Befehle schlagen mit dem Fehler `Unknown lifecycle phase ".mainClass=..."` fehl

**Ursache**: PowerShell interpretiert `=` als Variablenzuweisungsoperator, wodurch die Maven-Eigenschaftssyntax unterbrochen wird

**Lösung**: Verwenden Sie den Stop-Parsen-Operator `--%` vor dem Maven-Befehl:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Der `--%` Operator weist PowerShell an, alle verbleibenden Argumente wörtlich an Maven weiterzugeben, ohne sie zu interpretieren.

### Windows PowerShell Emoji-Anzeige

**Problem**: KI-Antworten zeigen in PowerShell Müllzeichen (z.B. `????` oder `â??`) statt Emojis an

**Ursache**: Die Standardkodierung von PowerShell unterstützt keine UTF-8-Emojis

**Lösung**: Führen Sie diesen Befehl vor der Ausführung von Java-Anwendungen aus:
```cmd
chcp 65001
```

Dies erzwingt UTF-8-Kodierung im Terminal. Alternativ verwenden Sie Windows Terminal, das eine bessere Unicode-Unterstützung bietet.

### Fehlerbehebung bei API-Aufrufen

**Problem**: Authentifizierungsfehler, Ratenbegrenzungen oder unerwartete Antworten des KI-Modells

**Lösung**: Die Beispiele enthalten `.logRequests(true)` und `.logResponses(true)`, um API-Aufrufe in der Konsole anzuzeigen. Dies hilft bei der Fehlerbehebung von Authentifizierungsfehlern, Ratenbegrenzungen oder unerwarteten Antworten. Entfernen Sie diese Flags in der Produktion, um Log-Lärm zu reduzieren.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir um Genauigkeit bemüht sind, können automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten. Das Originaldokument in seiner Ursprungsprache gilt als maßgebliche Quelle. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die durch die Nutzung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->