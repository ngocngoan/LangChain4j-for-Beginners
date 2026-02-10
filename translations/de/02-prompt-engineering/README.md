# Modul 02: Prompt Engineering mit GPT-5.2

## Inhaltsverzeichnis

- [Was Sie lernen werden](../../../02-prompt-engineering)
- [Voraussetzungen](../../../02-prompt-engineering)
- [Verständnis von Prompt Engineering](../../../02-prompt-engineering)
- [Wie dies LangChain4j nutzt](../../../02-prompt-engineering)
- [Die Kernmuster](../../../02-prompt-engineering)
- [Verwendung bestehender Azure-Ressourcen](../../../02-prompt-engineering)
- [Anwendungs-Screenshots](../../../02-prompt-engineering)
- [Erkunden der Muster](../../../02-prompt-engineering)
  - [Niedrige vs hohe Einsatzbereitschaft](../../../02-prompt-engineering)
  - [Aufgabenausführung (Werkzeug-Einleitungen)](../../../02-prompt-engineering)
  - [Selbstreflektierender Code](../../../02-prompt-engineering)
  - [Strukturierte Analyse](../../../02-prompt-engineering)
  - [Mehrstufiger Chat](../../../02-prompt-engineering)
  - [Schritt-für-Schritt-Denken](../../../02-prompt-engineering)
  - [Eingeschränkte Ausgabe](../../../02-prompt-engineering)
- [Was Sie wirklich lernen](../../../02-prompt-engineering)
- [Nächste Schritte](../../../02-prompt-engineering)

## Was Sie lernen werden

Im vorherigen Modul haben Sie gesehen, wie Speicher konversationale KI ermöglicht und GitHub-Modelle für grundlegende Interaktionen verwendet wurden. Jetzt konzentrieren wir uns darauf, wie Sie Fragen stellen – die Prompts selbst – unter Verwendung von Azure OpenAI’s GPT-5.2. Die Art und Weise, wie Sie Ihre Prompts strukturieren, beeinflusst dramatisch die Qualität der Antworten, die Sie erhalten.

Wir verwenden GPT-5.2, weil es eine Steuerung des Denkprozesses einführt – Sie können dem Modell sagen, wie viel Denkarbeit es vor der Antwort leisten soll. Das macht verschiedene Prompting-Strategien deutlicher und hilft Ihnen zu verstehen, wann Sie welche Methode anwenden sollten. Außerdem profitieren wir von den geringeren Ratenlimits von Azure für GPT-5.2 gegenüber den GitHub-Modellen.

## Voraussetzungen

- Abgeschlossenes Modul 01 (Azure OpenAI-Ressourcen bereitgestellt)
- `.env`-Datei im Stammverzeichnis mit Azure-Zugangsdaten (erstellt durch `azd up` im Modul 01)

> **Hinweis:** Falls Sie Modul 01 noch nicht abgeschlossen haben, folgen Sie dort zuerst den Bereitstellungsanweisungen.

## Verständnis von Prompt Engineering

Prompt Engineering bedeutet, Eingabetexte so zu gestalten, dass Sie konstant die gewünschten Ergebnisse erhalten. Es geht nicht nur darum, Fragen zu stellen – sondern darum, Anfragen so zu strukturieren, dass das Modell genau versteht, was Sie wollen und wie es das liefern soll.

Man kann es sich wie Anweisungen an einen Kollegen vorstellen. „Behebe den Fehler“ ist vage. „Behebe die Nullpointer-Exception in UserService.java Zeile 45 durch Hinzufügen einer Null-Prüfung“ ist konkret. Sprachmodelle funktionieren genauso – Spezifizität und Struktur sind entscheidend.

## Wie dies LangChain4j nutzt

Dieses Modul demonstriert fortgeschrittene Prompting-Muster auf Basis derselben LangChain4j-Grundlage wie die vorherigen Module, mit Schwerpunkt auf Prompt-Struktur und Kontrolle über das Denkverhalten.

<img src="../../../translated_images/de/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*Wie LangChain4j Ihre Prompts mit Azure OpenAI GPT-5.2 verbindet*

**Abhängigkeiten** – Modul 02 verwendet die folgenden langchain4j-Abhängigkeiten, definiert in `pom.xml`:  
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
  
**OpenAiOfficialChatModel-Konfiguration** – [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Das Chatmodell ist manuell als Spring-Bean mit dem offiziellen OpenAI-Client konfiguriert, der Azure OpenAI-Endpunkte unterstützt. Der entscheidende Unterschied zu Modul 01 liegt darin, wie wir die an `chatModel.chat()` gesendeten Prompts strukturieren, nicht in der Modellausrüstung selbst.

**System- und Nutzer-Nachrichten** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j trennt Nachrichtentypen zur besseren Übersicht. `SystemMessage` legt das Verhalten und den Kontext der KI fest („Du bist ein Code-Reviewer“), während `UserMessage` die eigentliche Anfrage enthält. Diese Trennung erlaubt es, das KI-Verhalten über verschiedene Nutzeranfragen hinweg konsistent zu halten.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```
  
<img src="../../../translated_images/de/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage liefert persistenten Kontext, während UserMessages individuelle Anfragen enthalten*

**MessageWindowChatMemory für Mehrfachrunden** – Für das Mehrfachrunden-Gesprächsmuster verwenden wir `MessageWindowChatMemory` aus Modul 01 erneut. Jede Sitzung erhält seine eigene Speicherinstanz, gespeichert in einer `Map<String, ChatMemory>`, was mehrere parallele Konversationen ohne Kontextvermischung ermöglicht.

**Prompt-Vorlagen** – Der eigentliche Fokus liegt hier auf der Prompt-Gestaltung, nicht auf neuen LangChain4j-APIs. Jedes Muster (niedrige Einsatzbereitschaft, hohe Einsatzbereitschaft, Aufgabenausführung etc.) nutzt dieselbe Methode `chatModel.chat(prompt)`, jedoch mit sorgfältig strukturierten Prompt-Strings. Die XML-Tags, Anweisungen und Formatierungen sind alle Teil des Prompt-Texts, nicht LangChain4j-Funktionen.

**Steuerung des Denkprozesses** – Die Denkanstrengung von GPT-5.2 wird über Prompt-Anweisungen wie „maximal 2 Denkschritte“ oder „gründlich untersuchen“ gesteuert. Das sind Techniken des Prompt Engineering, keine LangChain4j-Konfigurationen. Die Bibliothek liefert lediglich Ihre Prompts an das Modell.

Die wichtigste Erkenntnis: LangChain4j stellt die Infrastruktur bereit (Modell-Verbindung via [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), Speicher, Nachrichten-Handling via [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), während dieses Modul Ihnen zeigt, wie Sie innerhalb dieser Infrastruktur effektive Prompts erstellen.

## Die Kernmuster

Nicht alle Probleme benötigen den gleichen Ansatz. Manche Fragen verlangen schnelle Antworten, andere tiefes Nachdenken. Manche benötigen sichtbares Nachdenken, andere nur Ergebnisse. Dieses Modul behandelt acht Prompting-Muster – jeweils optimiert für unterschiedliche Szenarien. Sie werden alle ausprobieren, um zu lernen, wann welcher Ansatz am besten funktioniert.

<img src="../../../translated_images/de/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Übersicht der acht Prompt Engineering-Muster und deren Anwendungsfälle*

<img src="../../../translated_images/de/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Niedrige Einsatzbereitschaft (schnell, direkt) vs. hohe Einsatzbereitschaft (gründlich, explorativ)*

**Niedrige Einsatzbereitschaft (Schnell & Fokussiert)** – Für einfache Fragen, bei denen Sie schnelle, direkte Antworten möchten. Das Modell denkt minimal – maximal 2 Schritte. Verwenden Sie dies für Berechnungen, Nachschlagen oder einfache Fragen.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```
  
> 💡 **Entdecken mit GitHub Copilot:** Öffnen Sie [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) und fragen Sie:
> - „Was ist der Unterschied zwischen niedriger und hoher Einsatzbereitschaft bei Prompt-Mustern?“
> - „Wie helfen die XML-Tags in Prompts, die Antwort der KI zu strukturieren?“
> - „Wann sollte ich Selbstreflexion verwenden und wann direkte Anweisungen?“

**Hohe Einsatzbereitschaft (Tief & Gründlich)** – Für komplexe Probleme, bei denen Sie umfassende Analysen möchten. Das Modell erforscht gründlich und zeigt detailliertes Nachdenken. Nutzen Sie dies für Systemdesign, Architekturentscheidungen oder komplexe Forschung.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**Aufgabenausführung (Schritt-für-Schritt-Fortschritt)** – Für mehrstufige Arbeitsabläufe. Das Modell liefert einen Plan vorab, beschreibt jeden Schritt während der Arbeit und gibt abschließend eine Zusammenfassung. Verwenden Sie das bei Migrationen, Implementierungen oder jeglichen mehrstufigen Prozessen.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```
  
Chain-of-Thought-Prompting fordert das Modell explizit auf, seinen Denkprozess zu zeigen, was die Genauigkeit bei komplexen Aufgaben verbessert. Die schrittweise Aufgliederung hilft sowohl Menschen als auch KI, die Logik zu verstehen.

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Fragen Sie zu diesem Muster:
> - „Wie würde ich das Aufgabenausführungs-Muster für lang laufende Operationen anpassen?“
> - „Was sind beste Praktiken für die Strukturierung von Werkzeug-Einleitungen in Produktionsanwendungen?“
> - „Wie kann ich Zwischenfortschritte in einer UI erfassen und anzeigen?“

<img src="../../../translated_images/de/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Plan → Ausführen → Zusammenfassen im Arbeitsablauf für mehrstufige Aufgaben*

**Selbstreflektierender Code** – Zur Erzeugung von Code in Produktionsqualität. Das Modell generiert Code, prüft ihn anhand von Qualitätskriterien und verbessert ihn iterativ. Nutzen Sie das, wenn Sie neue Features oder Services entwickeln.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/de/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iterative Verbesserungs-Schleife – generieren, bewerten, Probleme erkennen, verbessern, wiederholen*

**Strukturierte Analyse** – Für konsistente Bewertungen. Das Modell überprüft Code mit einem festen Rahmen (Korrektheit, Praktiken, Leistung, Sicherheit). Verwenden Sie dies für Code-Reviews oder Qualitätsprüfungen.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```
  
> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Fragen Sie zur strukturierten Analyse:
> - „Wie kann ich den Analyse-Rahmen für unterschiedliche Arten von Code-Reviews anpassen?“
> - „Wie analysiert und verarbeitet man strukturierte Ausgaben programmatisch am besten?“
> - „Wie gewährleiste ich konsistente Schweregrade über verschiedene Review-Sitzungen hinweg?“

<img src="../../../translated_images/de/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Vierkategorie-Rahmen für konsistente Code-Reviews mit Schweregradstufen*

**Mehrstufiger Chat** – Für Gespräche, die Kontext benötigen. Das Modell merkt sich vorherige Nachrichten und baut darauf auf. Nutzen Sie dies für interaktive Hilfesitzungen oder komplexe Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
<img src="../../../translated_images/de/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Wie sich der Gesprächskontext über mehrere Runden ansammelt, bis zur Token-Grenze*

**Schritt-für-Schritt-Denken** – Für Probleme, die sichtbare Logik brauchen. Das Modell zeigt explizites Nachdenken für jeden Schritt. Verwenden Sie dies für Matheaufgaben, Logikrätsel oder wenn Sie den Denkprozess nachvollziehen möchten.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/de/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Probleme in explizite logische Schritte zerlegen*

**Eingeschränkte Ausgabe** – Für Antworten mit spezifischen Formatvorgaben. Das Modell hält sich strikt an Format- und Längenregeln. Verwenden Sie dies für Zusammenfassungen oder wenn Sie eine präzise Ausgabestruktur benötigen.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/de/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Spezifische Anforderungen an Format, Länge und Struktur durchsetzen*

## Verwendung bestehender Azure-Ressourcen

**Überprüfen der Bereitstellung:**

Stellen Sie sicher, dass die `.env`-Datei im Stammverzeichnis mit Azure-Zugangsdaten vorliegt (wurde im Modul 01 erstellt):  
```bash
cat ../.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```
  
**Starten der Anwendung:**

> **Hinweis:** Falls Sie alle Anwendungen bereits mit `./start-all.sh` aus Modul 01 gestartet haben, läuft dieses Modul bereits auf Port 8083. Sie können die Startbefehle unten überspringen und direkt http://localhost:8083 aufrufen.

**Option 1: Verwendung des Spring Boot Dashboards (Empfohlen für VS Code-Nutzer)**

Der Dev-Container enthält die Spring Boot Dashboard-Erweiterung, die eine visuelle Oberfläche zur Verwaltung aller Spring Boot-Anwendungen bietet. Sie finden sie in der Aktivitätsleiste links in VS Code (Suchen Sie nach dem Spring Boot-Symbol).

Vom Spring Boot Dashboard aus können Sie:
- Alle verfügbaren Spring Boot-Anwendungen im Workspace sehen
- Anwendungen mit einem Klick starten/stoppen
- Anwendungsprotokolle in Echtzeit ansehen
- Den Anwendungsstatus überwachen

Klicken Sie einfach auf den Play-Button neben „prompt-engineering“, um dieses Modul zu starten, oder starten Sie alle Module gleichzeitig.

<img src="../../../translated_images/de/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Option 2: Nutzung von Shell-Skripten**

Starten Sie alle Webanwendungen (Module 01–04):

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
cd 02-prompt-engineering
./start.sh
```
  
**PowerShell:**  
```powershell
cd 02-prompt-engineering
.\start.ps1
```
  
Beide Skripte laden automatisch Umgebungsvariablen aus der `.env`-Datei im Stammverzeichnis und bauen die JARs, falls sie nicht vorhanden sind.

> **Hinweis:** Wenn Sie alle Module vor dem Start manuell bauen möchten:
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
  
Öffnen Sie http://localhost:8083 in Ihrem Browser.

**Zum Beenden:**

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
  
## Anwendungs-Screenshots

<img src="../../../translated_images/de/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Das Haupt-Dashboard zeigt alle 8 Prompt Engineering-Muster mit ihren Merkmalen und Anwendungsfällen*

## Erkunden der Muster

Die Weboberfläche ermöglicht es Ihnen, mit verschiedenen Prompting-Strategien zu experimentieren. Jedes Muster löst unterschiedliche Probleme – probieren Sie sie aus, um zu sehen, wann welcher Ansatz am besten funktioniert.

### Niedrige vs hohe Einsatzbereitschaft

Stellen Sie eine einfache Frage wie „Was sind 15 % von 200?“ mit niedriger Einsatzbereitschaft. Sie erhalten eine sofortige, direkte Antwort. Stellen Sie nun eine komplexe Frage wie „Entwerfe eine Caching-Strategie für eine stark frequentierte API“ mit hoher Einsatzbereitschaft. Beobachten Sie, wie das Modell langsamer wird und detaillierte Begründungen liefert. Dasselbe Modell, dieselbe Fragestellung – aber der Prompt bestimmt, wie viel Denkarbeit es leisten soll.
<img src="../../../translated_images/de/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Schnelle Berechnung mit minimalem Nachdenken*

<img src="../../../translated_images/de/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Umfassende Caching-Strategie (2,8 MB)*

### Aufgaben-Ausführung (Tool-Preambles)

Mehrstufige Workflows profitieren von Planung im Voraus und Fortschritts-Erzählung. Das Modell skizziert, was es tun wird, erzählt jeden Schritt und fasst dann die Ergebnisse zusammen.

<img src="../../../translated_images/de/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Erstellen eines REST-Endpunkts mit schrittweiser Erzählung (3,9 MB)*

### Selbstreflektierender Code

Versuchen Sie „Erstelle einen E-Mail-Validierungsdienst“. Statt nur Code zu generieren und zu stoppen, erzeugt das Modell Code, bewertet ihn anhand von Qualitätskriterien, erkennt Schwächen und verbessert ihn. Sie sehen, wie es iteriert, bis der Code Produktionsstandards erfüllt.

<img src="../../../translated_images/de/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Vollständiger E-Mail-Validierungsdienst (5,2 MB)*

### Strukturierte Analyse

Code-Reviews benötigen konsistente Bewertungsrahmen. Das Modell analysiert Code anhand fester Kategorien (Korrektheit, Praktiken, Leistung, Sicherheit) mit Schweregraden.

<img src="../../../translated_images/de/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Rahmenbasierte Code-Überprüfung*

### Mehrstufiger Chat

Fragen Sie „Was ist Spring Boot?“ und unmittelbar danach „Zeig mir ein Beispiel“. Das Modell erinnert sich an Ihre erste Frage und gibt Ihnen ein spezifisches Spring Boot-Beispiel. Ohne Gedächtnis wäre die zweite Frage zu ungenau.

<img src="../../../translated_images/de/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Kontextbewahrung über Fragen hinweg*

### Schritt-für-Schritt-Nachdenken

Wählen Sie eine Mathematikaufgabe und probieren Sie sie mit Schritt-für-Schritt-Nachdenken und mit geringer Bereitschaft aus. Geringe Bereitschaft gibt nur die Antwort – schnell, aber undurchsichtig. Schritt-für-Schritt zeigt jede Berechnung und Entscheidung.

<img src="../../../translated_images/de/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matheaufgabe mit expliziten Schritten*

### Eingeschränkte Ausgabe

Wenn Sie bestimmte Formate oder Wortzahlen benötigen, erzwingt dieses Muster strikte Einhaltung. Versuchen Sie, eine Zusammenfassung mit genau 100 Wörtern im Aufzählungsformat zu erzeugen.

<img src="../../../translated_images/de/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Machine-Learning-Zusammenfassung mit Formatkontrolle*

## Was Sie Wirklich Lernen

**Nachdenkaufwand ändert alles**

GPT-5.2 ermöglicht es Ihnen, den Rechenaufwand über Ihre Eingabeaufforderungen zu steuern. Geringer Aufwand bedeutet schnelle Antworten mit minimaler Erkundung. Hoher Aufwand bedeutet, dass das Modell sich Zeit nimmt, um tief nachzudenken. Sie lernen, den Aufwand an die Komplexität der Aufgabe anzupassen – verschwenden Sie keine Zeit bei einfachen Fragen, aber hetzen Sie auch keine komplexen Entscheidungen.

**Struktur lenkt das Verhalten**

Haben Sie die XML-Tags in den Prompts bemerkt? Sie sind nicht dekorativ. Modelle folgen strukturierten Anweisungen zuverlässiger als Freitext. Wenn Sie mehrstufige Prozesse oder komplexe Logik brauchen, hilft Struktur dem Modell, zu verfolgen, wo es sich befindet und was als Nächstes kommt.

<img src="../../../translated_images/de/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomie eines gut strukturierten Prompts mit klaren Abschnitten und XML-artiger Organisation*

**Qualität durch Selbstbewertung**

Die selbstreflektierenden Muster funktionieren, indem Qualitätskriterien explizit gemacht werden. Anstatt zu hoffen, dass das Modell „es richtig macht“, sagen Sie ihm genau, was „richtig“ bedeutet: korrekte Logik, Fehlerbehandlung, Leistung, Sicherheit. Das Modell kann dann seine eigene Ausgabe bewerten und verbessern. Das verwandelt Codegenerierung von einer Lotterie in einen Prozess.

**Kontext ist begrenzt**

Mehrstufige Gespräche funktionieren, indem der Nachrichtenverlauf bei jeder Anfrage mitgesendet wird. Aber es gibt eine Grenze – jedes Modell hat eine maximale Tokenanzahl. Wenn Gespräche wachsen, brauchen Sie Strategien, um relevanten Kontext zu behalten, ohne dieses Limit zu erreichen. Dieses Modul zeigt, wie Gedächtnis funktioniert; später lernen Sie, wann man zusammenfasst, wann man vergisst und wann man abruft.

## Nächste Schritte

**Nächstes Modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Vorheriges: Modul 01 - Einführung](../01-introduction/README.md) | [Zurück zum Hauptmenü](../README.md) | [Nächstes: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mithilfe des KI-Übersetzungsdienstes [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir eine hohe Genauigkeit anstreben, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Ursprungsprache ist als maßgebliche Quelle zu betrachten. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Nutzung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->