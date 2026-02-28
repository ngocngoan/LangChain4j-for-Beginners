# Modul 02: Prompt-Engineering mit GPT-5.2

## Inhaltsverzeichnis

- [Video-Durchgang](../../../02-prompt-engineering)
- [Was Sie lernen werden](../../../02-prompt-engineering)
- [Voraussetzungen](../../../02-prompt-engineering)
- [Verständnis von Prompt-Engineering](../../../02-prompt-engineering)
- [Grundlagen des Prompt-Engineerings](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Rollenbasiertes Prompting](../../../02-prompt-engineering)
  - [Prompt-Vorlagen](../../../02-prompt-engineering)
- [Fortgeschrittene Muster](../../../02-prompt-engineering)
- [Verwendung bestehender Azure-Ressourcen](../../../02-prompt-engineering)
- [Anwendungs-Screenshots](../../../02-prompt-engineering)
- [Muster erkunden](../../../02-prompt-engineering)
  - [Niedrige vs. hohe Eiferbereitschaft](../../../02-prompt-engineering)
  - [Aufgabenausführung (Tool-Preambles)](../../../02-prompt-engineering)
  - [Selbstreflektierender Code](../../../02-prompt-engineering)
  - [Strukturierte Analyse](../../../02-prompt-engineering)
  - [Mehrstufiger Chat](../../../02-prompt-engineering)
  - [Schritt-für-Schritt-Logik](../../../02-prompt-engineering)
  - [Eingeschränkte Ausgabe](../../../02-prompt-engineering)
- [Was Sie wirklich lernen](../../../02-prompt-engineering)
- [Nächste Schritte](../../../02-prompt-engineering)

## Video-Durchgang

Sehen Sie sich diese Live-Sitzung an, die erklärt, wie Sie mit diesem Modul beginnen: [Prompt Engineering with LangChain4j - Live Session](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## Was Sie lernen werden

<img src="../../../translated_images/de/what-youll-learn.c68269ac048503b2.webp" alt="Was Sie lernen werden" width="800"/>

Im vorherigen Modul haben Sie gesehen, wie Speicher Konversations-KI ermöglicht und GitHub-Modelle für grundlegende Interaktionen genutzt. Jetzt konzentrieren wir uns darauf, wie Sie Fragen stellen – die Prompts selbst – mit Azure OpenAI's GPT-5.2. Die Art und Weise, wie Sie Ihre Prompts strukturieren, beeinflusst dramatisch die Qualität der erhaltenen Antworten. Wir beginnen mit einer Überprüfung der grundlegenden Prompting-Techniken und steigen dann in acht fortgeschrittene Muster ein, die die Fähigkeiten von GPT-5.2 voll nutzen.

Wir verwenden GPT-5.2, weil es eine Steuerung des Denkprozesses einführt – Sie können dem Modell mitteilen, wie viel es vor der Antwort nachdenken soll. Dies macht verschiedene Prompting-Strategien deutlicher und hilft Ihnen zu verstehen, wann Sie welche Methode einsetzen sollten. Außerdem profitieren wir von den geringeren Rate-Limits für GPT-5.2 in Azure im Vergleich zu GitHub-Modellen.

## Voraussetzungen

- Abgeschlossenes Modul 01 (Azure OpenAI-Ressourcen bereitgestellt)
- `.env` Datei im Stammverzeichnis mit Azure-Zugangsdaten (erstellt durch `azd up` in Modul 01)

> **Hinweis:** Wenn Sie Modul 01 noch nicht abgeschlossen haben, folgen Sie zuerst den dortigen Bereitstellungsanweisungen.

## Verständnis von Prompt-Engineering

<img src="../../../translated_images/de/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Was ist Prompt-Engineering?" width="800"/>

Prompt-Engineering bedeutet, Eingabetext so zu gestalten, dass Sie konsequent die gewünschten Ergebnisse erhalten. Es geht nicht nur darum, Fragen zu stellen – es geht darum, Anfragen so zu strukturieren, dass das Modell genau versteht, was Sie wollen und wie es geliefert werden soll.

Denken Sie daran wie eine Anweisung an einen Kollegen. „Behebe den Fehler“ ist ungenau. „Behebe die Nullzeiger-Ausnahme in UserService.java Zeile 45, indem du eine Nullprüfung hinzufügst“ ist spezifisch. Sprachmodelle funktionieren genauso – Spezifität und Struktur sind entscheidend.

<img src="../../../translated_images/de/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Wie LangChain4j passt" width="800"/>

LangChain4j stellt die Infrastruktur bereit — Modellverbindungen, Speicher und Nachrichtentypen — während Prompt-Muster nur sorgfältig strukturierter Text sind, der durch diese Infrastruktur gesendet wird. Die wichtigsten Bausteine sind `SystemMessage` (welches das Verhalten und die Rolle der KI festlegt) und `UserMessage` (welches Ihre tatsächliche Anfrage trägt).

## Grundlagen des Prompt-Engineerings

<img src="../../../translated_images/de/five-patterns-overview.160f35045ffd2a94.webp" alt="Übersicht fünf Prompt-Engineering Muster" width="800"/>

Bevor wir uns den fortgeschrittenen Mustern in diesem Modul widmen, lassen Sie uns fünf grundlegende Prompting-Techniken durchgehen. Diese sind die Bausteine, die jeder Prompt-Ingenieur kennen sollte. Wenn Sie bereits das [Quick Start Modul](../00-quick-start/README.md#2-prompt-patterns) bearbeitet haben, kennen Sie diese bereits in der Praxis — hier sehen Sie den konzeptuellen Rahmen dahinter.

### Zero-Shot Prompting

Der einfachste Ansatz: Geben Sie dem Modell eine direkte Anweisung ohne Beispiele. Das Modell verlässt sich vollständig auf sein Training, um die Aufgabe zu verstehen und auszuführen. Das funktioniert gut für einfache Anfragen, bei denen das erwartete Verhalten offensichtlich ist.

<img src="../../../translated_images/de/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direkte Anweisung ohne Beispiele — das Modell leitet die Aufgabe allein aus der Anweisung ab*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Antwort: "Positiv"
```

**Wann verwenden:** Einfache Klassifikationen, direkte Fragen, Übersetzungen oder jede Aufgabe, die das Modell ohne weitere Anleitung bewältigen kann.

### Few-Shot Prompting

Geben Sie Beispiele an, die das Muster demonstrieren, dem das Modell folgen soll. Das Modell lernt das erwartete Eingabe-Ausgabe-Format aus Ihren Beispielen und wendet es auf neue Eingaben an. Dies erhöht die Konsistenz dramatisch bei Aufgaben, bei denen das gewünschte Format oder Verhalten nicht offensichtlich ist.

<img src="../../../translated_images/de/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Lernen anhand von Beispielen — das Modell erkennt das Muster und überträgt es auf neue Eingaben*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**Wann verwenden:** Benutzerdefinierte Klassifikationen, konsistente Formatierung, domänenspezifische Aufgaben oder wenn Zero-Shot-Ergebnisse inkonsistent sind.

### Chain of Thought

Fordern Sie das Modell auf, seine Überlegungen Schritt für Schritt zu zeigen. Anstatt direkt eine Antwort zu geben, zerlegt das Modell das Problem und arbeitet jeden Teil explizit durch. Dies verbessert die Genauigkeit bei mathematischen, logischen und mehrstufigen Denkaufgaben.

<img src="../../../translated_images/de/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Schritt-für-Schritt-Logik — komplexe Probleme in explizite logische Schritte zerlegen*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Das Modell zeigt: 15 - 8 = 7, dann 7 + 12 = 19 Äpfel
```

**Wann verwenden:** Mathematische Probleme, Logikrätsel, Debugging oder jede Aufgabe, bei der das Zeigen des Denkprozesses Genauigkeit und Vertrauen verbessert.

### Rollenbasiertes Prompting

Legen Sie vor Ihrer Frage eine Persona oder Rolle für die KI fest. Dadurch erhält die Antwort Kontext, der Ton, Tiefe und Fokus beeinflusst. Ein „Software-Architekt“ gibt andere Ratschläge als ein „Junior-Entwickler“ oder ein „Sicherheitsauditor“.

<img src="../../../translated_images/de/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rollenbasiertes Prompting" width="800"/>

*Kontext und Persona festlegen — dieselbe Frage erhält unterschiedliche Antworten, abhängig von der zugewiesenen Rolle*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**Wann verwenden:** Code-Reviews, Nachhilfe, domänenspezifische Analysen oder wenn Sie Antworten benötigen, die auf einem bestimmten Fachwissen oder einer Perspektive basieren.

### Prompt-Vorlagen

Erstellen Sie wiederverwendbare Prompts mit variablen Platzhaltern. Statt jedes Mal einen neuen Prompt zu schreiben, definieren Sie eine Vorlage einmal und füllen verschiedene Werte ein. Die `PromptTemplate`-Klasse von LangChain4j macht das mit der `{{variable}}`-Syntax einfach.

<img src="../../../translated_images/de/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt-Vorlagen" width="800"/>

*Wiederverwendbare Prompts mit variablen Platzhaltern — eine Vorlage, viele Anwendungen*

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

**Wann verwenden:** Wiederholte Abfragen mit unterschiedlichen Eingaben, Batch-Verarbeitung, Aufbau wiederverwendbarer KI-Workflows oder jede Situation, in der sich die Prompt-Struktur nicht ändert, aber die Daten.

---

Diese fünf Grundlagen geben Ihnen ein solides Werkzeugset für die meisten Prompting-Aufgaben. Der Rest dieses Moduls baut darauf auf mit **acht fortgeschrittenen Mustern**, die GPT-5.2's Steuerung des Denkprozesses, Selbstbewertung und strukturierte Ausgabe nutzen.

## Fortgeschrittene Muster

Nachdem die Grundlagen geklärt sind, gehen wir zu den acht fortgeschrittenen Mustern über, die dieses Modul einzigartig machen. Nicht alle Probleme benötigen denselben Ansatz. Manche Fragen erfordern schnelle Antworten, andere tiefes Nachdenken. Manche wollen sichtbare Überlegungen, andere nur Ergebnisse. Jedes Muster unten ist für ein anderes Szenario optimiert – und GPT-5.2's Steuerung des Denkprozesses macht die Unterschiede noch deutlicher.

<img src="../../../translated_images/de/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Acht Prompt-Muster" width="800"/>

*Übersicht der acht Prompt-Engineering-Muster und ihrer Anwendungsfälle*

<img src="../../../translated_images/de/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Steuerung des Denkprozesses mit GPT-5.2" width="800"/>

*Die Steuerung des Denkprozesses in GPT-5.2 erlaubt es, anzugeben, wie viel das Modell nachdenken soll – von schnellen direkten Antworten bis zu tiefgehender Erkundung*

**Niedrige Eiferbereitschaft (Schnell & Fokussiert)** – Für einfache Fragen, bei denen Sie schnelle, direkte Antworten möchten. Das Modell denkt minimal nach – maximal 2 Schritte. Nutzen Sie dies für Berechnungen, Nachschlagen oder einfache Fragen.

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Entdecken mit GitHub Copilot:** Öffnen Sie [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) und fragen Sie:
> - „Was ist der Unterschied zwischen niedriger und hoher Eiferbereitschaft bei Prompt-Mustern?“
> - „Wie helfen die XML-Tags in Prompts, die Antwort der KI zu strukturieren?“
> - „Wann sollte ich Selbstreflexions-Muster versus direkte Anweisung verwenden?“

**Hohe Eiferbereitschaft (Tief & Gründlich)** – Für komplexe Probleme, bei denen Sie eine umfassende Analyse wünschen. Das Modell erforscht gründlich und zeigt ausführliche Überlegungen. Nutzen Sie dies für Systemdesign, Architekturentscheidungen oder komplexe Forschung.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Aufgabenausführung (Fortschritt Schritt für Schritt)** – Für mehrstufige Abläufe. Das Modell stellt einen Plan vor, erzählt jeden Schritt beim Arbeiten und gibt dann eine Zusammenfassung. Nutzen Sie dies bei Migrationen, Implementierungen oder jedem mehrstufigen Prozess.

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought Prompting fordert das Modell explizit auf, seinen Denkprozess zu zeigen, was die Genauigkeit bei komplexen Aufgaben verbessert. Die schrittweise Aufschlüsselung hilft sowohl Menschen als auch KI, die Logik zu verstehen.

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Fragen Sie zu diesem Muster:
> - „Wie würde ich das Aufgabenausführungsmuster für lang laufende Operationen anpassen?“
> - „Was sind bewährte Vorgehensweisen für die Strukturierung von Tool-Preambles in Produktionsanwendungen?“
> - „Wie kann ich Zwischenfortschritte in einer UI erfassen und anzeigen?“

<img src="../../../translated_images/de/task-execution-pattern.9da3967750ab5c1e.webp" alt="Muster Aufgabenausführung" width="800"/>

*Plan → Ausführen → Zusammenfassen Arbeitsablauf für mehrstufige Aufgaben*

**Selbstreflektierender Code** – Für die Generierung von Produktionstauglichem Code. Das Modell erzeugt Code gemäß Produktionsstandards mit korrektem Fehlerhandling. Nutzen Sie dies beim Aufbau neuer Funktionen oder Dienste.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/de/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Selbstreflexionszyklus" width="800"/>

*Iterativer Verbesserungszyklus - generieren, bewerten, Probleme identifizieren, verbessern, wiederholen*

**Strukturierte Analyse** – Für konsistente Bewertungen. Das Modell überprüft Code mit einem festen Rahmenwerk (Korrektheit, Praktiken, Performance, Sicherheit, Wartbarkeit). Nutzen Sie dies für Code-Reviews oder Qualitätsbewertungen.

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Fragen Sie zur strukturierten Analyse:
> - „Wie kann ich das Analyse-Rahmenwerk für verschiedene Arten von Code-Reviews anpassen?“
> - „Was ist der beste Weg, strukturierte Ausgaben programmatisch zu parsen und darauf zu reagieren?“
> - „Wie stelle ich konsistente Schweregrade über verschiedene Review-Sitzungen hinweg sicher?“

<img src="../../../translated_images/de/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Muster der strukturierten Analyse" width="800"/>

*Rahmenwerk für konsistente Code-Reviews mit Schweregrad-Stufen*

**Mehrstufiger Chat** – Für Gespräche, die Kontext benötigen. Das Modell merkt sich vorherige Nachrichten und baut darauf auf. Nutzen Sie dies für interaktive Hilfesitzungen oder komplexes Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/de/context-memory.dff30ad9fa78832a.webp" alt="Kontext-Speicher" width="800"/>

*Wie sich der Gesprächskontext über mehrere Runden ansammelt, bis das Token-Limit erreicht ist*

**Schritt-für-Schritt-Logik** – Für Probleme, die sichtbare Logik erfordern. Das Modell zeigt explizite Überlegungen für jeden Schritt. Nutzen Sie dies bei Mathematikaufgaben, Logikrätseln oder wenn Sie den Denkprozess verstehen müssen.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/de/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Schritt-für-Schritt-Muster" width="800"/>

*Probleme in explizite logische Schritte zerlegen*

**Eingeschränkte Ausgabe** – Für Antworten mit spezifischen Formatvorgaben. Das Modell folgt strikt Format- und Längenregeln. Nutzen Sie dies für Zusammenfassungen oder wenn Sie eine genaue Ausgabe benötigen.

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

<img src="../../../translated_images/de/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Muster Einschränkung der Ausgabe" width="800"/>

*Durchsetzung spezifischer Format-, Längen- und Strukturvorgaben*

## Verwendung bestehender Azure-Ressourcen

**Überprüfung der Bereitstellung:**

Stellen Sie sicher, dass die `.env` Datei im Stammverzeichnis mit Azure-Zugangsdaten existiert (erstellt während Modul 01):
```bash
cat ../.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```

**Starten der Anwendung:**

> **Hinweis:** Wenn Sie bereits alle Anwendungen mit `./start-all.sh` aus Modul 01 gestartet haben, läuft dieses Modul bereits auf Port 8083. Sie können die Startbefehle unten überspringen und direkt zu http://localhost:8083 gehen.

**Option 1: Verwendung des Spring Boot Dashboards (empfohlen für VS Code Nutzer)**
Der Dev-Container enthält die Spring Boot Dashboard-Erweiterung, die eine visuelle Oberfläche zur Verwaltung aller Spring Boot-Anwendungen bietet. Sie finden sie in der Aktivitätsleiste links in VS Code (achten Sie auf das Spring Boot-Symbol).

Vom Spring Boot Dashboard aus können Sie:
- Alle verfügbaren Spring Boot-Anwendungen im Arbeitsbereich sehen
- Anwendungen mit einem Klick starten/stoppen
- Anwendungsprotokolle in Echtzeit anzeigen
- Anwendungsstatus überwachen

Klicken Sie einfach auf die Wiedergabetaste neben „prompt-engineering“, um dieses Modul zu starten, oder starten Sie alle Module auf einmal.

<img src="../../../translated_images/de/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

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
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

Beide Skripte laden automatisch Umgebungsvariablen aus der `.env`-Datei im Root-Verzeichnis und bauen die JARs, falls diese noch nicht existieren.

> **Hinweis:** Wenn Sie alle Module manuell bauen möchten, bevor Sie starten:
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

**Zum Stoppen:**

**Bash:**
```bash
./stop.sh  # Dieses Modul nur
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

*Das Hauptdashboard zeigt alle 8 Prompt-Engineering-Muster mit ihren Merkmalen und Anwendungsfällen*

## Die Muster erkunden

Die Weboberfläche ermöglicht es Ihnen, mit verschiedenen Prompting-Strategien zu experimentieren. Jedes Muster löst unterschiedliche Probleme – probieren Sie sie aus, um zu sehen, wann welcher Ansatz am besten funktioniert.

> **Hinweis: Streaming vs. Nicht-Streaming** — Jede Mustervorlage bietet zwei Buttons: **🔴 Stream Response (Live)** und eine **Nicht-Streaming**-Option. Streaming verwendet Server-Sent Events (SSE), um Tokens in Echtzeit während der Modellerstellung anzuzeigen, sodass Sie den Fortschritt sofort sehen. Die Nicht-Streaming-Option wartet auf die gesamte Antwort, bevor sie angezeigt wird. Bei Prompts, die tiefgehendes Nachdenken auslösen (z. B. High Eagerness, Self-Reflecting Code), kann der Nicht-Streaming-Aufruf sehr lange dauern – manchmal Minuten – ohne sichtbares Feedback. **Verwenden Sie Streaming beim Experimentieren mit komplexen Prompts**, damit Sie sehen, wie das Modell arbeitet, und nicht den Eindruck bekommen, die Anfrage sei abgelaufen.
>
> **Hinweis: Browser-Anforderung** — Das Streaming-Feature nutzt die Fetch Streams API (`response.body.getReader()`), die einen vollständigen Browser (Chrome, Edge, Firefox, Safari) benötigt. Es funktioniert **nicht** im integrierten Simple Browser von VS Code, da dessen Webview die ReadableStream-API nicht unterstützt. Im Simple Browser funktionieren die Nicht-Streaming-Buttons normal – nur die Streaming-Buttons sind betroffen. Öffnen Sie `http://localhost:8083` in einem externen Browser für das volle Erlebnis.

### Geringe vs Hohe Eagerness (Bereitschaft)

Stellen Sie eine einfache Frage wie „Was sind 15 % von 200?“ mit niedriger Eagerness. Sie erhalten eine sofortige, direkte Antwort. Stellen Sie nun etwas Komplexes wie „Entwerfe eine Caching-Strategie für eine viel frequentierte API“ mit hoher Eagerness. Klicken Sie auf **🔴 Stream Response (Live)** und beobachten Sie, wie die detaillierten Überlegungen des Modells Token für Token erscheinen. Dasselbe Modell, dieselbe Frage – aber der Prompt gibt vor, wie intensiv das Nachdenken sein soll.

### Aufgaben-Ausführung (Tool-Preambles)

Mehrstufige Workflows profitieren von Vorausplanung und Fortschrittserzählung. Das Modell skizziert, was es tun wird, erläutert jeden Schritt und fasst anschließend die Ergebnisse zusammen.

### Selbstreflektierender Code

Probieren Sie „Erstelle einen E-Mail-Validierungsdienst“. Anstatt einfach nur Code zu generieren und zu stoppen, erzeugt das Modell Code, bewertet ihn anhand von Qualitätskriterien, erkennt Schwächen und verbessert ihn. Sie sehen, wie es so lange iteriert, bis der Code Produktionsstandards erfüllt.

### Strukturierte Analyse

Code Reviews benötigen konsistente Bewertungsrahmen. Das Modell analysiert den Code anhand fester Kategorien (Korrektheit, Praktiken, Leistung, Sicherheit) mit Schweregraden.

### Mehrstufiger Chat

Fragen Sie „Was ist Spring Boot?“ und folgen Sie sofort mit „Zeig mir ein Beispiel“. Das Modell erinnert sich an Ihre erste Frage und gibt Ihnen ein spezielles Spring Boot-Beispiel. Ohne Gedächtnis wäre die zweite Frage zu vage.

### Schritt-für-Schritt-Denken

Wählen Sie ein Matheproblem und probieren Sie es mit Schritt-für-Schritt-Denken und niedriger Eagerness. Niedrige Eagerness liefert nur schnell die Antwort – aber undurchsichtig. Schritt-für-Schritt zeigt alle Berechnungen und Entscheidungen.

### Eingeschränkte Ausgabe

Wenn Sie spezifische Formate oder Wortzahlen benötigen, erzwingt dieses Muster strikte Einhaltung. Versuchen Sie, eine Zusammenfassung mit genau 100 Wörtern im Bullet-Point-Format zu generieren.

## Was Sie wirklich lernen

**Denkanstrengung ändert alles**

GPT-5.2 erlaubt es Ihnen, den Rechenaufwand durch Ihre Prompts zu steuern. Niedriger Aufwand bedeutet schnelle Antworten mit minimaler Erkundung. Hoher Aufwand bedeutet, dass das Modell Zeit für tiefes Nachdenken nimmt. Sie lernen, Aufwand der Komplexität der Aufgabe anzupassen – verschwenden Sie keine Zeit mit simplen Fragen, aber überstürzen Sie komplexe Entscheidungen nicht.

**Struktur steuert Verhalten**

Ist Ihnen das XML-Tagging in den Prompts aufgefallen? Es ist keine Dekoration. Modelle folgen strukturierten Anweisungen verlässlicher als Freitext. Für mehrstufige Prozesse oder komplexe Logik hilft Struktur dem Modell, den Überblick zu behalten, wo es ist und was als Nächstes kommt.

<img src="../../../translated_images/de/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Aufbau eines gut strukturierten Prompts mit klaren Abschnitten und XML-ähnlicher Organisation*

**Qualität durch Selbstevaluation**

Die selbstreflektierenden Muster funktionieren, indem sie Qualitätskriterien explizit machen. Anstatt zu hoffen, dass das Modell „es richtig macht“, sagen Sie ihm genau, was „richtig“ bedeutet: korrekte Logik, Fehlerbehandlung, Leistung, Sicherheit. Das Modell kann dann seine eigene Ausgabe bewerten und verbessern. Das macht Code-Generierung zu einem Prozess statt zu einer Lotterie.

**Kontext ist begrenzt**

Mehrstufige Gespräche funktionieren, indem sie den Nachrichtenverlauf mit jeder Anfrage mitschicken. Aber es gibt eine Grenze – jedes Modell hat eine maximale Tokenanzahl. Mit wachsendem Gespräch brauchen Sie Strategien, um relevanten Kontext zu bewahren, ohne diese Grenze zu überschreiten. Dieses Modul zeigt, wie Gedächtnis funktioniert; später lernen Sie, wann Sie zusammenfassen, wann Sie vergessen und wann Sie abrufen.

## Nächste Schritte

**Nächstes Modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Zurück: Modul 01 - Einführung](../01-introduction/README.md) | [Zurück zum Hauptmenü](../README.md) | [Weiter: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir um Genauigkeit bemüht sind, bitten wir zu beachten, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Ursprungssprache gilt als maßgebliche Quelle. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Verwendung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->