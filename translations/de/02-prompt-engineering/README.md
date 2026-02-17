# Modul 02: Prompt Engineering mit GPT-5.2

## Inhaltsverzeichnis

- [Was Sie lernen werden](../../../02-prompt-engineering)
- [Voraussetzungen](../../../02-prompt-engineering)
- [Verständnis von Prompt Engineering](../../../02-prompt-engineering)
- [Grundlagen des Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Rollenbasiertes Prompting](../../../02-prompt-engineering)
  - [Prompt-Vorlagen](../../../02-prompt-engineering)
- [Fortgeschrittene Muster](../../../02-prompt-engineering)
- [Verwendung bestehender Azure-Ressourcen](../../../02-prompt-engineering)
- [Anwendungs-Screenshots](../../../02-prompt-engineering)
- [Erkundung der Muster](../../../02-prompt-engineering)
  - [Niedrige vs hohe Eiferbereitschaft](../../../02-prompt-engineering)
  - [Aufgabenausführung (Tool-Preambles)](../../../02-prompt-engineering)
  - [Selbstreflektierender Code](../../../02-prompt-engineering)
  - [Strukturierte Analyse](../../../02-prompt-engineering)
  - [Mehrstufiger Chat](../../../02-prompt-engineering)
  - [Schritt-für-Schritt-Argumentation](../../../02-prompt-engineering)
  - [Eingeschränkte Ausgabe](../../../02-prompt-engineering)
- [Was Sie wirklich lernen](../../../02-prompt-engineering)
- [Nächste Schritte](../../../02-prompt-engineering)

## Was Sie lernen werden

<img src="../../../translated_images/de/what-youll-learn.c68269ac048503b2.webp" alt="Was Sie lernen werden" width="800"/>

Im vorherigen Modul haben Sie gesehen, wie Speicher konversationelle KI ermöglicht und GitHub-Modelle für grundlegende Interaktionen verwendet wurden. Jetzt konzentrieren wir uns darauf, wie Sie Fragen stellen – also die Prompts selbst – mit Azure OpenAI’s GPT-5.2. Die Art und Weise, wie Sie Ihre Prompts strukturieren, beeinflusst maßgeblich die Qualität der Antworten. Wir beginnen mit einer Übersicht der grundlegenden Prompting-Techniken und gehen dann zu acht fortgeschrittenen Mustern über, die die Fähigkeiten von GPT-5.2 voll ausnutzen.

Wir verwenden GPT-5.2, weil es eine Steuerung des Denkprozesses einführt – Sie können dem Modell sagen, wie viel Nachdenken vor der Antwort erfolgen soll. Das macht unterschiedliche Prompting-Strategien deutlicher und hilft Ihnen zu verstehen, wann welche Methode sinnvoll ist. Außerdem profitieren wir von den geringeren Rate Limits für GPT-5.2 gegenüber den GitHub-Modellen in Azure.

## Voraussetzungen

- Modul 01 abgeschlossen (Azure OpenAI-Ressourcen bereitgestellt)
- `.env`-Datei im Stammverzeichnis mit Azure-Anmeldeinformationen (erstellt durch `azd up` in Modul 01)

> **Hinweis:** Wenn Sie Modul 01 nicht abgeschlossen haben, folgen Sie dort zunächst den Bereitstellungsanweisungen.

## Verständnis von Prompt Engineering

<img src="../../../translated_images/de/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Was ist Prompt Engineering?" width="800"/>

Prompt Engineering bedeutet, Eingabetext so zu gestalten, dass Sie konsequent die gewünschten Ergebnisse erhalten. Es geht nicht nur ums Fragenstellen – es geht darum, Anfragen so zu strukturieren, dass das Modell genau versteht, was Sie wollen und wie es geliefert werden soll.

Stellen Sie sich vor, Sie geben einem Kollegen Anweisungen. „Behebe den Fehler“ ist vage. „Behebe die Nullpointer-Exception in UserService.java Zeile 45, indem du eine Nullprüfung hinzufügst“ ist spezifisch. Sprachmodelle funktionieren genauso – Spezifität und Struktur sind entscheidend.

<img src="../../../translated_images/de/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Wie LangChain4j passt" width="800"/>

LangChain4j bietet die Infrastruktur — Modellverbindungen, Speicher und Nachrichtentypen — während Prompt-Muster einfach sorgfältig strukturierte Texte sind, die Sie durch diese Infrastruktur senden. Die wichtigsten Bausteine sind `SystemMessage` (legt das Verhalten und die Rolle der KI fest) und `UserMessage` (enthält Ihre eigentliche Anfrage).

## Grundlagen des Prompt Engineering

<img src="../../../translated_images/de/five-patterns-overview.160f35045ffd2a94.webp" alt="Übersicht der fünf Prompt Engineering Muster" width="800"/>

Bevor wir zu den fortgeschrittenen Mustern in diesem Modul kommen, sehen wir uns fünf grundlegende Prompting-Techniken an. Diese sind die Bausteine, die jeder Prompt-Ingenieur kennen sollte. Wenn Sie bereits das [Quick Start Modul](../00-quick-start/README.md#2-prompt-patterns) bearbeitet haben, kennen Sie diese schon aus der Praxis – hier ist der konzeptionelle Rahmen dahinter.

### Zero-Shot Prompting

Der einfachste Ansatz: Geben Sie dem Modell eine direkte Anweisung ohne Beispiele. Das Modell verlässt sich vollständig auf sein Training, um die Aufgabe zu verstehen und auszuführen. Dies funktioniert gut bei einfachen Anfragen, bei denen das erwartete Verhalten offensichtlich ist.

<img src="../../../translated_images/de/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direkte Anweisung ohne Beispiele – das Modell leitet die Aufgabe allein aus der Anweisung ab*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Antwort: "Positiv"
```

**Wann verwenden:** Einfache Klassifizierungen, direkte Fragen, Übersetzungen oder jede Aufgabe, die das Modell ohne zusätzliche Anleitung bewältigen kann.

### Few-Shot Prompting

Geben Sie Beispiele, die das Muster zeigen, dem das Modell folgen soll. Das Modell lernt aus Ihren Beispielen das erwartete Eingabe-Ausgabe-Format und wendet es auf neue Eingaben an. Das verbessert die Konsistenz bei Aufgaben erheblich, bei denen das gewünschte Format oder Verhalten nicht offensichtlich ist.

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

**Wann verwenden:** Anpassbare Klassifizierungen, gleichmäßige Formatierung, domänenspezifische Aufgaben oder wenn Zero-Shot-Ergebnisse uneinheitlich sind.

### Chain of Thought

Bitten Sie das Modell, seine Überlegungen Schritt für Schritt darzulegen. Statt direkt zur Antwort zu springen, zerlegt das Modell das Problem und arbeitet jeden Teil explizit durch. Das verbessert die Genauigkeit bei Mathematik-, Logik- und mehrstufigen Denkaufgaben.

<img src="../../../translated_images/de/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Schritt-für-Schritt-Argumentation – komplexe Probleme in explizite logische Schritte zerlegen*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Das Modell zeigt: 15 - 8 = 7, dann 7 + 12 = 19 Äpfel
```

**Wann verwenden:** Mathematische Probleme, Logikrätsel, Debugging oder alle Aufgaben, bei denen das Darlegen des Denkprozesses die Genauigkeit und das Vertrauen verbessert.

### Rollenbasiertes Prompting

Legen Sie eine Persona oder Rolle für die KI fest, bevor Sie Ihre Frage stellen. Dies bietet Kontext, der Ton, Tiefe und Fokus der Antwort prägt. Ein „Softwarearchitekt“ gibt andere Ratschläge als ein „Junior-Entwickler“ oder ein „Sicherheitsauditor“.

<img src="../../../translated_images/de/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rollenbasiertes Prompting" width="800"/>

*Kontext und Persona festlegen – dieselbe Frage erhält je nach zugewiesener Rolle unterschiedliche Antworten*

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

**Wann verwenden:** Code-Reviews, Nachhilfe, domänenspezifische Analysen oder wenn Sie Antworten brauchen, die auf einem bestimmten Fachniveau oder einer Perspektive basieren.

### Prompt-Vorlagen

Erstellen Sie wiederverwendbare Prompts mit variable Platzhaltern. Anstatt jedes Mal einen neuen Prompt zu schreiben, definieren Sie eine Vorlage und füllen verschiedene Werte ein. Die `PromptTemplate`-Klasse von LangChain4j macht das mit der `{{variable}}`-Syntax einfach.

<img src="../../../translated_images/de/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Wiederverwendbare Prompts mit variablen Platzhaltern – eine Vorlage, viele Verwendungen*

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

**Wann verwenden:** Wiederholte Abfragen mit unterschiedlichen Eingaben, Stapelverarbeitung, Aufbau wiederverwendbarer KI-Workflows oder jedes Szenario, bei dem die Prompt-Struktur gleich bleibt, sich aber die Daten ändern.

---

Diese fünf Grundlagen geben Ihnen eine solide Werkzeugkiste für die meisten Prompting-Aufgaben. Der Rest dieses Moduls baut darauf auf mit **acht fortgeschrittenen Mustern**, die GPT-5.2s Steuerung des Denkprozesses, Selbstbewertung und strukturierte Ausgabe nutzen.

## Fortgeschrittene Muster

Mit den Grundlagen abgedeckt, kommen wir zu den acht fortgeschrittenen Mustern, die dieses Modul einzigartig machen. Nicht alle Probleme erfordern den gleichen Ansatz. Manche Fragen brauchen schnelle Antworten, andere tiefes Nachdenken. Manche brauchen sichtbare Begründungen, andere nur Ergebnisse. Jedes Muster unten ist für ein anderes Szenario optimiert – und GPT-5.2s Denkprozesssteuerung macht die Unterschiede noch deutlicher.

<img src="../../../translated_images/de/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Acht Prompting-Muster" width="800"/>

*Übersicht über die acht Prompt Engineering Muster und ihre Anwendungsfälle*

<img src="../../../translated_images/de/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Denkprozesssteuerung mit GPT-5.2" width="800"/>

*Die Denkprozesssteuerung von GPT-5.2 erlaubt es, festzulegen, wie viel Nachdenken das Modell tun soll – von schnellen direkten Antworten bis zu tiefgehender Erkundung*

<img src="../../../translated_images/de/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Vergleich des Denkaufwands" width="800"/>

*Niedrige Eiferbereitschaft (schnell, direkt) vs. hohe Eiferbereitschaft (gründlich, explorativ) Denkansätze*

**Niedrige Eiferbereitschaft (schnell & fokussiert)** – Für einfache Fragen, bei denen Sie schnelle, direkte Antworten wünschen. Das Modell macht minimalen Denkaufwand – maximal 2 Schritte. Verwenden Sie dies für Berechnungen, Nachschlagen oder einfache Fragen.

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

> 💡 **Erkunden mit GitHub Copilot:** Öffnen Sie [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) und fragen Sie:
> - „Was ist der Unterschied zwischen den prompting Mustern niedrige und hohe Eiferbereitschaft?“
> - „Wie helfen die XML-Tags in Prompts, die Antwort der KI zu strukturieren?“
> - „Wann sollte ich Selbstreflexionsmuster gegenüber direkter Anweisung verwenden?“

**Hohe Eiferbereitschaft (tief & gründlich)** – Für komplexe Probleme, bei denen Sie eine umfassende Analyse wünschen. Das Modell erkundet gründlich und zeigt ausführliche Begründungen. Verwenden Sie dies für Systemdesign, Architekturentscheidungen oder komplexe Forschung.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Aufgabenausführung (Schritt-für-Schritt-Fortschritt)** – Für mehrstufige Abläufe. Das Modell bietet einen Plan vorab, erzählt jeden Schritt beim Arbeiten und gibt dann eine Zusammenfassung. Verwenden Sie dies für Migrationen, Implementierungen oder jeden mehrstufigen Prozess.

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
> - „Was sind Best Practices für die Strukturierung von Tool-Preambles in Produktionsanwendungen?“
> - „Wie kann ich Zwischenfortschrittsaktualisierungen in einer UI erfassen und anzeigen?“

<img src="../../../translated_images/de/task-execution-pattern.9da3967750ab5c1e.webp" alt="Aufgabenausführungsmuster" width="800"/>

*Plan → Ausführen → Zusammenfassen Arbeitsablauf für mehrstufige Aufgaben*

**Selbstreflektierender Code** – Für die Erzeugung von Produktionsqualität-Code. Das Modell erzeugt Code, der Produktionsstandards mit korrektem Fehlermanagement folgt. Verwenden Sie dies beim Aufbau neuer Features oder Dienste.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/de/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Selbstreflexionszyklus" width="800"/>

*Iterative Verbesserungsschleife - generieren, bewerten, Probleme identifizieren, verbessern, wiederholen*

**Strukturierte Analyse** – Für konsistente Bewertungen. Das Modell überprüft Code anhand eines festen Rahmens (Korrektheit, Praktiken, Leistung, Sicherheit, Wartbarkeit). Verwenden Sie dies für Code-Reviews oder Qualitätsbewertungen.

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
> - „Wie kann ich das Analyse-Framework für verschiedene Arten von Code-Reviews anpassen?“
> - „Was ist der beste Weg, strukturierte Ausgaben programmatisch zu parsen und zu verwenden?“
> - „Wie stelle ich konsistente Schweregrade über verschiedene Review-Sitzungen sicher?“

<img src="../../../translated_images/de/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Strukturiertes Analyse-Muster" width="800"/>

*Rahmen für konsistente Code-Reviews mit Schweregraden*

**Mehrstufiger Chat** – Für Konversationen, die Kontext benötigen. Das Modell merkt sich vorherige Nachrichten und baut darauf auf. Verwenden Sie dies für interaktive Hilfesitzungen oder komplexe Q&A.

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

*Wie sich Kontext über mehrere Gesprächsrunden bis zur Token-Grenze ansammelt*

**Schritt-für-Schritt-Argumentation** – Für Probleme, die sichtbare Logik erfordern. Das Modell zeigt explizites Denken für jeden Schritt. Verwenden Sie dies für Mathematikprobleme, Logikrätsel oder wenn Sie den Denkprozess verstehen müssen.

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

*Komplexe Probleme in explizite logische Schritte zerlegen*

**Eingeschränkte Ausgabe** – Für Antworten mit spezifischen Formatvorgaben. Das Modell hält sich streng an Format- und Längenregeln. Verwenden Sie dies für Zusammenfassungen oder wenn präzise Ausgabestrukturen benötigt werden.

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

<img src="../../../translated_images/de/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Eingeschränktes Ausgabe-Muster" width="800"/>

*Durchsetzung spezifischer Format-, Längen- und Strukturvorgaben*

## Verwendung bestehender Azure-Ressourcen

**Überprüfen der Bereitstellung:**

Stellen Sie sicher, dass die `.env`-Datei im Stammverzeichnis mit Azure-Anmeldeinformationen existiert (erstellt während Modul 01):
```bash
cat ../.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```

**Starten der Anwendung:**

> **Hinweis:** Wenn Sie alle Anwendungen bereits mit `./start-all.sh` aus Modul 01 gestartet haben, läuft dieses Modul bereits auf Port 8083. Sie können die untenstehenden Startbefehle überspringen und direkt zu http://localhost:8083 gehen.

**Option 1: Verwendung des Spring Boot Dashboards (Empfohlen für VS Code-Nutzer)**

Der Dev-Container enthält die Erweiterung Spring Boot Dashboard, die eine visuelle Oberfläche zum Verwalten aller Spring Boot-Anwendungen bietet. Sie finden sie in der Aktivitätsleiste auf der linken Seite von VS Code (achten Sie auf das Spring Boot-Symbol).
Vom Spring Boot Dashboard aus können Sie:
- Alle verfügbaren Spring Boot-Anwendungen im Arbeitsbereich sehen
- Anwendungen mit einem einzigen Klick starten/stoppen
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

Beide Skripte laden automatisch Umgebungsvariablen aus der Root-`.env`-Datei und erstellen die JARs, falls sie noch nicht existieren.

> **Hinweis:** Wenn Sie alle Module manuell vor dem Starten bauen möchten:
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

## Anwendungsscreenshots

<img src="../../../translated_images/de/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Das Hauptdashboard zeigt alle 8 Prompt-Engineering-Muster mit ihren Eigenschaften und Anwendungsfällen*

## Erkundung der Muster

Die Weboberfläche ermöglicht es Ihnen, mit verschiedenen Prompting-Strategien zu experimentieren. Jedes Muster löst andere Probleme – probieren Sie sie aus, um zu sehen, wann welcher Ansatz glänzt.

### Niedrige vs. hohe Bereitschaft

Stellen Sie eine einfache Frage wie „Was sind 15 % von 200?“ mit niedriger Bereitschaft. Sie erhalten eine sofortige, direkte Antwort. Stellen Sie nun eine komplexe Frage wie „Entwerfen Sie eine Caching-Strategie für eine stark frequentierte API“ mit hoher Bereitschaft. Beobachten Sie, wie das Modell langsamer wird und ausführliche Begründungen liefert. Gleiches Modell, gleiche Frageform – aber die Eingabeaufforderung sagt ihm, wie viel es nachdenken soll.

<img src="../../../translated_images/de/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Schnelle Berechnung mit minimaler Begründung*

<img src="../../../translated_images/de/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Umfassende Caching-Strategie (2,8 MB)*

### Aufgaben-Ausführung (Tool-Preambles)

Mehrschrittige Workflows profitieren von einer Vorausplanung und einer Fortschrittserzählung. Das Modell skizziert, was es tun wird, beschreibt jeden Schritt und fasst dann die Ergebnisse zusammen.

<img src="../../../translated_images/de/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Erstellung eines REST-Endpunkts mit Schritt-für-Schritt-Erzählung (3,9 MB)*

### Selbstreflektierender Code

Probieren Sie „Erstelle einen E-Mail-Validierungsdienst“. Anstatt nur Code zu generieren und zu stoppen, erzeugt das Modell, bewertet anhand von Qualitätskriterien, identifiziert Schwächen und verbessert. Sie sehen, wie es iteriert, bis der Code Produktionsstandards erfüllt.

<img src="../../../translated_images/de/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Vollständiger E-Mail-Validierungsdienst (5,2 MB)*

### Strukturierte Analyse

Code-Reviews benötigen konsistente Bewertungsrahmen. Das Modell analysiert Code mit festen Kategorien (Korrektheit, Praktiken, Leistung, Sicherheit) mit Schweregraden.

<img src="../../../translated_images/de/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Framework-basierte Code-Review*

### Mehrstufiger Chat

Fragen Sie „Was ist Spring Boot?“ und folgen Sie sofort mit „Zeig mir ein Beispiel“ nach. Das Modell erinnert sich an Ihre erste Frage und gibt Ihnen ein spezifisches Spring Boot-Beispiel. Ohne Gedächtnis wäre die zweite Frage zu vage.

<img src="../../../translated_images/de/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Kontextbewahrung über mehrere Fragen*

### Schritt-für-Schritt-Begründung

Wählen Sie ein Mathematikproblem und probieren Sie es mit Schritt-für-Schritt-Begründung und niedriger Bereitschaft aus. Niedrige Bereitschaft liefert nur die Antwort – schnell, aber undurchsichtig. Schritt-für-Schritt zeigt Ihnen jede Berechnung und Entscheidung.

<img src="../../../translated_images/de/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Mathematikproblem mit expliziten Schritten*

### Eingeschränkte Ausgabe

Wenn Sie bestimmte Formate oder Wortanzahlen benötigen, erzwingt dieses Muster strikte Einhaltung. Versuchen Sie, eine Zusammenfassung mit genau 100 Wörtern im Listenformat zu erzeugen.

<img src="../../../translated_images/de/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Maschinelles Lernen Zusammenfassung mit Formatsteuerung*

## Was Sie wirklich lernen

**Denken verändert alles**

GPT-5.2 ermöglicht es Ihnen, den Rechenaufwand über Ihre Eingabeaufforderungen zu steuern. Wenig Aufwand bedeutet schnelle Antworten mit minimaler Exploration. Hoher Aufwand bedeutet, dass das Modell sich Zeit nimmt, um tief nachzudenken. Sie lernen, den Aufwand an die Komplexität der Aufgabe anzupassen – verschwenden Sie keine Zeit mit einfachen Fragen, aber hetzen Sie komplexe Entscheidungen auch nicht.

**Struktur leitet das Verhalten**

Achten Sie auf die XML-Tags in den Eingabeaufforderungen? Sie sind nicht dekorativ. Modelle folgen strukturierten Anweisungen zuverlässiger als Freitext. Wenn Sie mehrstufige Prozesse oder komplexe Logik benötigen, hilft Struktur dem Modell, zu verfolgen, wo es sich befindet und was als Nächstes kommt.

<img src="../../../translated_images/de/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomie einer gut strukturierten Eingabeaufforderung mit klaren Abschnitten und XML-artiger Organisation*

**Qualität durch Selbstevaluation**

Die selbstreflektierenden Muster funktionieren, indem Qualitätskriterien explizit gemacht werden. Anstatt zu hoffen, dass das Modell „es richtig macht“, sagen Sie ihm genau, was „richtig“ bedeutet: korrekte Logik, Fehlerbehandlung, Leistung, Sicherheit. Das Modell kann dann seine eigene Ausgabe bewerten und verbessern. Das verwandelt die Codegenerierung von einer Lotterie in einen Prozess.

**Kontext ist begrenzt**

Mehrstufige Gespräche funktionieren, indem sie die Nachrichtenhistorie mit jeder Anfrage einbinden. Aber es gibt ein Limit – jedes Modell hat eine maximale Token-Anzahl. Wenn Gespräche wachsen, brauchen Sie Strategien, um relevanten Kontext zu behalten, ohne das Limit zu überschreiten. Dieses Modul zeigt Ihnen, wie Memory funktioniert; später lernen Sie, wann Sie Zusammenfassen, wann Vergessen und wann Abrufen sollten.

## Nächste Schritte

**Nächstes Modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Vorheriges: Modul 01 - Einführung](../01-introduction/README.md) | [Zurück zur Hauptseite](../README.md) | [Nächstes: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Hinweis**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, kann es bei automatischen Übersetzungen zu Fehlern oder Ungenauigkeiten kommen. Das Originaldokument in seiner Ausgangssprache gilt als maßgebliche Quelle. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die durch die Nutzung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->