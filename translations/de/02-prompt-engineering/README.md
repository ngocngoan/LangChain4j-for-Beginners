# Modul 02: Prompt Engineering mit GPT-5.2

## Inhaltsverzeichnis

- [Was Sie lernen werden](../../../02-prompt-engineering)
- [Voraussetzungen](../../../02-prompt-engineering)
- [Verstehen von Prompt Engineering](../../../02-prompt-engineering)
- [Grundlagen des Prompt Engineerings](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Rollenbasiertes Prompting](../../../02-prompt-engineering)
  - [Prompt-Vorlagen](../../../02-prompt-engineering)
- [Erweiterte Muster](../../../02-prompt-engineering)
- [Verwendung vorhandener Azure-Ressourcen](../../../02-prompt-engineering)
- [Anwendungs-Screenshots](../../../02-prompt-engineering)
- [Erkundung der Muster](../../../02-prompt-engineering)
  - [Niedrige vs. hohe Eifer](../../../02-prompt-engineering)
  - [Aufgabenausführung (Werkzeug-Preambles)](../../../02-prompt-engineering)
  - [Selbstreflektierender Code](../../../02-prompt-engineering)
  - [Strukturierte Analyse](../../../02-prompt-engineering)
  - [Multi-Turn-Chat](../../../02-prompt-engineering)
  - [Schritt-für-Schritt-Argumentation](../../../02-prompt-engineering)
  - [Eingeschränkte Ausgabe](../../../02-prompt-engineering)
- [Was Sie wirklich lernen](../../../02-prompt-engineering)
- [Nächste Schritte](../../../02-prompt-engineering)

## Was Sie lernen werden

<img src="../../../translated_images/de/what-youll-learn.c68269ac048503b2.webp" alt="Was Sie lernen werden" width="800"/>

Im vorherigen Modul haben Sie gesehen, wie Speicher konversationelle KI ermöglicht und GitHub-Modelle für grundlegende Interaktionen verwendet. Jetzt konzentrieren wir uns darauf, wie Sie Fragen stellen – also die Prompts selbst – mit GPT-5.2 von Azure OpenAI. Die Art und Weise, wie Sie Ihre Prompts strukturieren, beeinflusst drastisch die Qualität der Antworten, die Sie erhalten. Wir beginnen mit einer Übersicht der grundlegenden Prompting-Techniken und gehen dann zu acht erweiterten Mustern über, die die Fähigkeiten von GPT-5.2 voll ausnutzen.

Wir verwenden GPT-5.2, weil es eine Steuerung des Denkens einführt – Sie können dem Modell sagen, wie viel Nachdenken es vor der Antwort leisten soll. Das macht verschiedene Prompting-Strategien deutlicher und hilft Ihnen zu verstehen, wann Sie welche Methode einsetzen sollten. Außerdem profitieren wir von den geringeren Rate-Limits von Azure für GPT-5.2 im Vergleich zu GitHub-Modellen.

## Voraussetzungen

- Abgeschlossenes Modul 01 (Azure OpenAI-Ressourcen bereitgestellt)
- `.env`-Datei im Stammverzeichnis mit Azure-Anmeldedaten (erstellt durch `azd up` im Modul 01)

> **Hinweis:** Wenn Sie Modul 01 noch nicht abgeschlossen haben, folgen Sie dort zuerst den Bereitstellungsanweisungen.

## Verstehen von Prompt Engineering

<img src="../../../translated_images/de/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Was ist Prompt Engineering?" width="800"/>

Prompt Engineering bedeutet, Eingabetexte so zu gestalten, dass Sie zuverlässig die gewünschten Ergebnisse erhalten. Es geht nicht nur darum, Fragen zu stellen – es geht darum, Anfragen so zu strukturieren, dass das Modell genau versteht, was Sie wollen und wie es das liefern soll.

Stellen Sie sich vor, Sie geben einem Kollegen Anweisungen. „Fixe den Bug“ ist vage. „Behebe die Nullzeiger-Ausnahme in UserService.java Zeile 45 durch Hinzufügen einer Null-Prüfung“ ist spezifisch. Sprachmodelle funktionieren genauso – Spezifität und Struktur sind wichtig.

<img src="../../../translated_images/de/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Wie LangChain4j passt" width="800"/>

LangChain4j stellt die Infrastruktur bereit — Modellverbindungen, Speicher und Nachrichtentypen — während Prompt-Muster nur sorgfältig strukturierte Texte sind, die Sie durch diese Infrastruktur senden. Die zentralen Bausteine sind `SystemMessage` (die das Verhalten und die Rolle der KI festlegt) und `UserMessage` (die Ihre eigentliche Anfrage enthält).

## Grundlagen des Prompt Engineerings

<img src="../../../translated_images/de/five-patterns-overview.160f35045ffd2a94.webp" alt="Übersicht fünf Muster des Prompt Engineerings" width="800"/>

Bevor wir auf die erweiterten Muster dieses Moduls eingehen, lassen Sie uns fünf grundlegende Techniken des Promptings wiederholen. Diese sind die Bausteine, die jeder Prompt Engineer kennen sollte. Wenn Sie bereits das [Quick Start Modul](../00-quick-start/README.md#2-prompt-patterns) durchgearbeitet haben, kennen Sie diese Muster schon in der Praxis – hier ist der konzeptuelle Rahmen dahinter.

### Zero-Shot Prompting

Der einfachste Ansatz: Sie geben dem Modell eine direkte Anweisung ohne Beispiele. Das Modell verlässt sich komplett auf sein Training, um die Aufgabe zu verstehen und auszuführen. Das funktioniert gut für einfache Anfragen, bei denen das erwartete Verhalten offensichtlich ist.

<img src="../../../translated_images/de/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direkte Anweisung ohne Beispiele – das Modell schließt die Aufgabe allein aus der Anweisung*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Antwort: "Positiv"
```

**Wann verwenden:** Einfache Klassifikationen, direkte Fragen, Übersetzungen oder jede Aufgabe, die das Modell ohne zusätzliche Anleitung bewältigen kann.

### Few-Shot Prompting

Geben Sie Beispiele an, die das Muster zeigen, dem das Modell folgen soll. Das Modell lernt aus Ihren Beispielen das erwartete Eingabe-Ausgabe-Format und wendet es auf neue Eingaben an. Das verbessert die Konsistenz für Aufgaben, bei denen Format oder Verhalten nicht offensichtlich sind.

<img src="../../../translated_images/de/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Lernen aus Beispielen – das Modell erkennt das Muster und wendet es auf neue Eingaben an*

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

Bitten Sie das Modell, seine Überlegungen Schritt für Schritt zu zeigen. Anstatt direkt zu einer Antwort zu springen, zerlegt das Modell das Problem und bearbeitet jeden Teil explizit. Das verbessert die Genauigkeit bei Mathematik-, Logik- und mehrstufigen Denkaufgaben.

<img src="../../../translated_images/de/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Schritt-für-Schritt-Denken – komplexe Probleme in explizite logische Schritte zerlegen*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Das Modell zeigt: 15 - 8 = 7, dann 7 + 12 = 19 Äpfel
```

**Wann verwenden:** Mathematikaufgaben, Logikrätsel, Debugging oder jede Aufgabe, bei der die Darstellung des Denkprozesses Genauigkeit und Vertrauen verbessert.

### Rollenbasiertes Prompting

Legen Sie eine Persona oder Rolle für die KI fest, bevor Sie Ihre Frage stellen. Das bietet Kontext, der Ton, Tiefe und Fokus der Antwort prägt. Ein „Softwarearchitekt“ gibt andere Ratschläge als ein „Junior-Entwickler“ oder ein „Sicherheitsauditor“.

<img src="../../../translated_images/de/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rollenbasiertes Prompting" width="800"/>

*Kontext- und Rollenfestlegung – dieselbe Frage erhält je nach zugewiesener Rolle eine andere Antwort*

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

**Wann verwenden:** Code-Reviews, Nachhilfe, domänenspezifische Analysen oder wenn Sie Antworten benötigen, die auf einen bestimmten Erfahrungsstand oder eine Perspektive zugeschnitten sind.

### Prompt-Vorlagen

Erstellen Sie wiederverwendbare Prompts mit Platzhaltern für Variablen. Anstatt jedes Mal einen neuen Prompt zu schreiben, definieren Sie eine Vorlage einmal und füllen sie mit verschiedenen Werten. Die `PromptTemplate`-Klasse von LangChain4j macht das mit der `{{variable}}`-Syntax einfach.

<img src="../../../translated_images/de/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt-Vorlagen" width="800"/>

*Wiederverwendbare Prompts mit Variablen-Platzhaltern – eine Vorlage, viele Anwendungen*

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

**Wann verwenden:** Wiederholte Anfragen mit unterschiedlichen Eingaben, Batch-Verarbeitung, Aufbau wiederverwendbarer KI-Workflows oder jedes Szenario, in dem die Struktur des Prompts gleich bleibt, aber die Daten sich ändern.

---

Diese fünf Grundlagen geben Ihnen ein solides Werkzeugset für die meisten Prompting-Aufgaben. Der Rest dieses Moduls baut darauf auf mit **acht erweiterten Mustern**, die die Steuerung des Denkens, die Selbstbewertung und die strukturierten Ausgabe-Fähigkeiten von GPT-5.2 nutzen.

## Erweiterte Muster

Nachdem die Grundlagen abgedeckt sind, kommen wir zu den acht erweiterten Mustern, die dieses Modul einzigartig machen. Nicht alle Probleme brauchen denselben Ansatz. Manche Fragen brauchen schnelle Antworten, andere tiefgehende Überlegungen. Manche benötigen sichtbare Begründungen, andere nur Ergebnisse. Jedes Muster unten ist für ein anderes Szenario optimiert – und die Steuerung des Denkens von GPT-5.2 macht die Unterschiede noch deutlicher.

<img src="../../../translated_images/de/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Acht Muster des Prompt Engineerings" width="800"/>

*Übersicht der acht Prompt Engineering Muster und ihrer Anwendungsfälle*

<img src="../../../translated_images/de/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Steuerung des Denkens mit GPT-5.2" width="800"/>

*Die Steuerung des Denkens von GPT-5.2 erlaubt Ihnen anzugeben, wie viel Nachdenken das Modell leisten soll – von schnellen Direktantworten bis zur tiefen Exploration*

**Niedriger Eifer (Schnell & Fokussiert)** – Für einfache Fragen, bei denen Sie schnelle, direkte Antworten wollen. Das Modell denkt minimal – maximal 2 Schritte. Verwenden Sie das für Berechnungen, Nachschläge oder einfache Fragen.

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

> 💡 **Entdecken Sie mit GitHub Copilot:** Öffnen Sie [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) und fragen Sie:
> - „Was ist der Unterschied zwischen niedrigem und hohem Eifer bei Prompting-Mustern?“
> - „Wie helfen die XML-Tags in Prompts, die Antwort der KI zu strukturieren?“
> - „Wann sollte ich Selbstreflexionsmuster statt direkter Anweisungen verwenden?“

**Hoher Eifer (Tief & Gründlich)** – Für komplexe Probleme, bei denen Sie umfassende Analysen wünschen. Das Modell erkundet gründlich und zeigt detaillierte Begründungen. Verwenden Sie das für Systemdesign, Architekturentscheidungen oder komplexe Forschung.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Aufgabenausführung (Schritt-für-Schritt-Fortschritt)** – Für mehrstufige Arbeitsabläufe. Das Modell gibt einen Plan vorab, erklärt jeden Schritt während der Arbeit und liefert dann eine Zusammenfassung. Nutzen Sie das für Migrationen, Implementierungen oder beliebige mehrstufige Prozesse.

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

Chain-of-Thought-Prompting fordert das Modell ausdrücklich auf, seinen Denkprozess zu zeigen, was die Genauigkeit bei komplexen Aufgaben verbessert. Die Schritt-für-Schritt-Zerlegung hilft sowohl Menschen als auch KI, die Logik zu verstehen.

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Fragen Sie zu diesem Muster:
> - „Wie würde ich das Muster der Aufgabenausführung für langlaufende Operationen anpassen?“
> - „Was sind Best Practices zum Strukturieren von Werkzeug-Preambles in Produktionsanwendungen?“
> - „Wie kann ich Zwischenfortschrittsmeldungen in einer Benutzeroberfläche erfassen und anzeigen?“

<img src="../../../translated_images/de/task-execution-pattern.9da3967750ab5c1e.webp" alt="Muster der Aufgabenausführung" width="800"/>

*Plan → Ausführen → Zusammenfassen-Workflow für mehrstufige Aufgaben*

**Selbstreflektierender Code** – Für die Erzeugung von Produktionscode in hoher Qualität. Das Modell erzeugt Code nach Produktionsstandards mit korrektem Fehlermanagement. Verwenden Sie dies beim Aufbau neuer Features oder Services.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/de/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Selbstreflexionszyklus" width="800"/>

*Iterative Verbesserungsschleife – erzeugen, bewerten, Probleme identifizieren, verbessern, wiederholen*

**Strukturierte Analyse** – Für konsistente Bewertungen. Das Modell überprüft Code anhand eines festen Rahmens (Korrektheit, Praktiken, Leistung, Sicherheit, Wartbarkeit). Nutzen Sie dies für Code-Reviews oder Qualitätsbewertungen.

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
> - „Wie kann ich den Analyse-Rahmen für verschiedene Arten von Code-Reviews anpassen?“
> - „Wie kann ich strukturierte Ausgaben programmgesteuert parsen und verarbeiten?“
> - „Wie stelle ich konsistente Schweregrade über verschiedene Review-Sitzungen sicher?“

<img src="../../../translated_images/de/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Muster der strukturierten Analyse" width="800"/>

*Rahmenwerk für konsistente Code-Reviews mit Schweregraden*

**Multi-Turn-Chat** – Für Gespräche, die Kontext brauchen. Das Modell merkt sich vorherige Nachrichten und baut darauf auf. Nutzen Sie das für interaktive Hilfesitzungen oder komplexes Q&A.

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

*Wie sich Gesprächskontext über mehrere Runden hinweg ansammelt, bis die Token-Grenze erreicht ist*

**Schritt-für-Schritt-Argumentation** – Für Probleme, die sichtbare Logik erfordern. Das Modell zeigt explicit das Denken für jeden Schritt. Nutzen Sie das für Mathematikaufgaben, Logikrätsel oder wenn Sie den Denkprozess verstehen möchten.

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

**Eingeschränkte Ausgabe** – Für Antworten mit spezifischen Formatvorgaben. Das Modell hält strikt Format- und Längenregeln ein. Nutzen Sie dies für Zusammenfassungen oder wenn Sie eine präzise Ausgabestruktur brauchen.

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

<img src="../../../translated_images/de/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Muster der eingeschränkten Ausgabe" width="800"/>

*Durchsetzen von spezifischen Format-, Längen- und Strukturvorgaben*

## Verwendung vorhandener Azure-Ressourcen

**Bereitstellung prüfen:**

Stellen Sie sicher, dass die `.env`-Datei im Stammverzeichnis mit Azure-Anmeldedaten vorhanden ist (wurde im Modul 01 erstellt):
```bash
cat ../.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```

**Starten Sie die Anwendung:**

> **Hinweis:** Falls Sie bereits alle Anwendungen mit `./start-all.sh` aus Modul 01 gestartet haben, läuft dieses Modul schon auf Port 8083. Sie können die Startbefehle unten überspringen und direkt zu http://localhost:8083 gehen.

**Option 1: Verwendung des Spring Boot Dashboards (Empfohlen für VS Code Nutzer)**

Der Dev-Container enthält die Erweiterung Spring Boot Dashboard, die eine visuelle Oberfläche zur Verwaltung aller Spring Boot-Anwendungen bietet. Sie finden sie in der Aktivitätsleiste links in VS Code (suchen Sie nach dem Spring Boot-Symbol).

Im Spring Boot Dashboard können Sie:
- Alle verfügbaren Spring Boot-Anwendungen im Workspace sehen
- Anwendungen mit einem Klick starten/stoppen
- Anwendungsprotokolle in Echtzeit ansehen
- Den Status der Anwendungen überwachen
Klicken Sie einfach auf die Wiedergabetaste neben "prompt-engineering", um dieses Modul zu starten, oder starten Sie alle Module auf einmal.

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

Beide Skripte laden automatisch Umgebungsvariablen aus der Root-`.env`-Datei und erstellen die JARs, falls diese noch nicht existieren.

> **Hinweis:** Wenn Sie alle Module lieber manuell vor dem Start bauen möchten:
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

Öffnen Sie http://localhost:8083 in Ihrem Browser.

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

## Anwendungs-Screenshots

<img src="../../../translated_images/de/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Das Haupt-Dashboard zeigt alle 8 Prompt Engineering Muster mit ihren Eigenschaften und Anwendungsfällen*

## Erkundung der Muster

Die Web-Oberfläche ermöglicht es Ihnen, mit verschiedenen Prompting-Strategien zu experimentieren. Jedes Muster löst unterschiedliche Probleme – probieren Sie sie aus, um zu sehen, wann welcher Ansatz am besten funktioniert.

### Geringe vs. hohe Bereitschaft

Stellen Sie eine einfache Frage wie „Was sind 15 % von 200?“ mit geringer Bereitschaft. Sie erhalten eine sofortige, direkte Antwort. Stellen Sie nun eine komplexe Frage wie „Entwerfen Sie eine Caching-Strategie für eine stark frequentierte API“ mit hoher Bereitschaft. Beobachten Sie, wie das Modell langsamer wird und detaillierte Begründungen liefert. Gleiches Modell, gleiche Frage-Struktur – aber der Prompt zeigt an, wie viel Nachdenken erforderlich ist.

<img src="../../../translated_images/de/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Schnelle Berechnung mit minimaler Begründung*

<img src="../../../translated_images/de/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Umfassende Caching-Strategie (2,8MB)*

### Aufgaben-Ausführung (Tool-Preambles)

Mehrstufige Workflows profitieren von Vorausplanung und Fortschrittsbeschreibung. Das Modell skizziert, was es tun wird, erläutert jeden Schritt und fasst anschließend die Ergebnisse zusammen.

<img src="../../../translated_images/de/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Erstellung eines REST-Endpunkts mit schrittweiser Erklärung (3,9MB)*

### Selbstreflektierender Code

Versuchen Sie „Erstellen Sie einen E-Mail-Validierungsdienst“. Anstatt nur Code zu generieren und zu stoppen, erzeugt das Modell, bewertet anhand von Qualitätskriterien, identifiziert Schwachstellen und verbessert. Sie sehen, wie es iteriert, bis der Code Produktionsstandards erfüllt.

<img src="../../../translated_images/de/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Vollständiger E-Mail-Validierungsdienst (5,2MB)*

### Strukturierte Analyse

Code-Reviews benötigen konsistente Bewertungsrahmen. Das Modell analysiert Code mit festen Kategorien (Korrektheit, Praktiken, Leistung, Sicherheit) und Schweregraden.

<img src="../../../translated_images/de/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Framework-basierter Code-Review*

### Mehrstufiger Chat

Fragen Sie „Was ist Spring Boot?“ und folgen Sie direkt mit „Zeig mir ein Beispiel“. Das Modell erinnert sich an Ihre erste Frage und gibt Ihnen ein spezielles Spring Boot Beispiel. Ohne Speicher wäre die zweite Frage zu vage.

<img src="../../../translated_images/de/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Kontextbewahrung über mehrere Fragen*

### Schritt-für-Schritt-Begründung

Wählen Sie ein Matheproblem und probieren Sie es mit Schritt-für-Schritt-Begründung und geringer Bereitschaft. Geringe Bereitschaft liefert Ihnen nur die Antwort – schnell, aber undurchsichtig. Schritt-für-Schritt zeigt jede Berechnung und Entscheidung.

<img src="../../../translated_images/de/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matheaufgabe mit expliziten Schritten*

### Beschränkte Ausgabe

Wenn Sie bestimmte Formate oder Wortanzahlen benötigen, erzwingt dieses Muster strikte Einhaltung. Versuchen Sie, eine Zusammenfassung mit genau 100 Wörtern im Aufzählungsformat zu generieren.

<img src="../../../translated_images/de/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Machine Learning Zusammenfassung mit Formatkontrolle*

## Was Sie wirklich lernen

**Begründungsaufwand ändert alles**

GPT-5.2 ermöglicht es Ihnen, den Rechenaufwand über Ihre Prompts zu steuern. Wenig Aufwand bedeutet schnelle Antworten mit minimaler Erkundung. Hoher Aufwand bedeutet, dass das Modell sich Zeit nimmt, tief nachzudenken. Sie lernen, den Aufwand an die Komplexität der Aufgabe anzupassen – verschwenden Sie keine Zeit bei einfachen Fragen, aber eilen Sie auch nicht bei komplexen Entscheidungen.

**Struktur lenkt Verhalten**

Ist Ihnen der XML-Tag im Prompt aufgefallen? Sie sind nicht dekorativ. Modelle folgen strukturierten Anweisungen zuverlässiger als Freitext. Wenn Sie mehrstufige Prozesse oder komplexe Logik benötigen, hilft Struktur dem Modell, zu verfolgen, wo es gerade ist und was als Nächstes kommt.

<img src="../../../translated_images/de/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomie eines gut strukturierten Prompts mit klaren Abschnitten und XML-artiger Organisation*

**Qualität durch Selbstevaluierung**

Die selbstreflektierenden Muster funktionieren, indem Qualitätskriterien explizit gemacht werden. Anstatt zu hoffen, dass das Modell „es richtig macht“, sagen Sie ihm genau, was „richtig“ bedeutet: korrekte Logik, Fehlerbehandlung, Leistung, Sicherheit. Das Modell kann dann seine eigene Ausgabe bewerten und verbessern. Damit wird Codegenerierung vom Glücksspiel zu einem Prozess.

**Kontext ist begrenzt**

Mehrstufige Gespräche funktionieren, indem der Nachrichtenverlauf mit jeder Anfrage einbezogen wird. Aber es gibt eine Grenze – jedes Modell hat eine maximale Token-Anzahl. Mit wachsender Konversation brauchen Sie Strategien, relevanten Kontext zu bewahren, ohne diese Grenze zu überschreiten. Dieses Modul zeigt Ihnen, wie Speicher funktioniert; später lernen Sie, wann zu zusammenfassen, wann zu vergessen und wann abzurufen ist.

## Nächste Schritte

**Nächstes Modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Vorheriges: Modul 01 - Einführung](../01-introduction/README.md) | [Zurück zur Hauptseite](../README.md) | [Nächstes: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatische Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Ursprungssprache gilt als maßgebliche Quelle. Für kritische Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Nutzung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->