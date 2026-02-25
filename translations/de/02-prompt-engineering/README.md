# Modul 02: Prompt-Engineering mit GPT-5.2

## Inhaltsverzeichnis

- [Was Sie lernen werden](../../../02-prompt-engineering)
- [Voraussetzungen](../../../02-prompt-engineering)
- [Verständnis von Prompt Engineering](../../../02-prompt-engineering)
- [Grundlagen des Prompt Engineerings](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Rollenspezifisches Prompting](../../../02-prompt-engineering)
  - [Prompt-Vorlagen](../../../02-prompt-engineering)
- [Fortgeschrittene Muster](../../../02-prompt-engineering)
- [Verwendung vorhandener Azure-Ressourcen](../../../02-prompt-engineering)
- [Anwendungs-Screenshots](../../../02-prompt-engineering)
- [Erkundung der Muster](../../../02-prompt-engineering)
  - [Niedrige vs. hohe Eile](../../../02-prompt-engineering)
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

Im vorherigen Modul haben Sie gesehen, wie Gedächtnis konversationelle KI ermöglicht und GitHub-Modelle für grundlegende Interaktionen verwendet wurden. Jetzt konzentrieren wir uns darauf, wie Sie Fragen stellen — also die Prompts selbst — mit Azure OpenAI's GPT-5.2. Die Art, wie Sie Ihre Prompts strukturieren, beeinflusst dramatisch die Qualität der Antworten, die Sie erhalten. Wir beginnen mit einer Übersicht über die grundlegenden Prompt-Techniken, gefolgt von acht fortgeschrittenen Mustern, die die Fähigkeiten von GPT-5.2 voll ausschöpfen.

Wir verwenden GPT-5.2, weil es eine Steuerung der Argumentation einführt - Sie können dem Modell sagen, wie viel Nachdenken es vor der Antwort tun soll. Das macht verschiedene Prompt-Strategien deutlicher und hilft Ihnen zu verstehen, wann Sie welche Methode verwenden sollten. Außerdem profitieren wir von Azures geringeren Ratebegrenzungen für GPT-5.2 im Vergleich zu GitHub-Modellen.

## Voraussetzungen

- Abgeschlossenes Modul 01 (Azure OpenAI-Ressourcen bereitgestellt)
- `.env`-Datei im Stammverzeichnis mit Azure-Zugangsdaten (erstellt durch `azd up` in Modul 01)

> **Hinweis:** Wenn Sie Modul 01 noch nicht abgeschlossen haben, folgen Sie zuerst den dortigen Bereitstellungsanweisungen.

## Verständnis von Prompt Engineering

<img src="../../../translated_images/de/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Was ist Prompt Engineering?" width="800"/>

Prompt Engineering bedeutet, Eingabetext so zu gestalten, dass Sie konsistent die Ergebnisse erhalten, die Sie benötigen. Es geht nicht nur darum, Fragen zu stellen – es geht darum, Anfragen so zu strukturieren, dass das Modell genau versteht, was Sie wollen und wie es geliefert werden soll.

Denken Sie daran wie an eine Anweisung an einen Kollegen. „Behebe den Fehler“ ist ungenau. „Behebe die Nullzeiger-Ausnahme in UserService.java, Zeile 45, durch Hinzufügen einer Nullprüfung“ ist spezifisch. Sprachmodelle funktionieren genauso – Spezifität und Struktur sind entscheidend.

<img src="../../../translated_images/de/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Wie LangChain4j passt" width="800"/>

LangChain4j stellt die Infrastruktur bereit — Modellverbindungen, Speicher und Nachrichtenarten — während Prompt-Muster einfach sorgfältig strukturierte Texte sind, die Sie durch diese Infrastruktur senden. Die wichtigsten Bausteine sind `SystemMessage` (die Verhalten und Rolle der KI festlegt) und `UserMessage` (die Ihre eigentliche Anfrage trägt).

## Grundlagen des Prompt Engineerings

<img src="../../../translated_images/de/five-patterns-overview.160f35045ffd2a94.webp" alt="Übersicht der fünf Prompt-Engineering-Muster" width="800"/>

Bevor wir uns den fortgeschrittenen Mustern dieses Moduls zuwenden, sehen wir uns fünf grundlegende Prompting-Techniken an. Diese sind die Bausteine, die jeder Prompt Engineer kennen sollte. Wenn Sie bereits das [Quick Start Modul](../00-quick-start/README.md#2-prompt-patterns) durchgearbeitet haben, kennen Sie diese Ansätze bereits — hier ist der dahinterstehende konzeptionelle Rahmen.

### Zero-Shot Prompting

Der einfachste Ansatz: dem Modell eine direkte Anweisung ohne Beispiele geben. Das Modell verlässt sich vollständig auf sein Training, um die Aufgabe zu verstehen und auszuführen. Das funktioniert gut bei einfachen Aufgaben, bei denen das erwartete Verhalten offensichtlich ist.

<img src="../../../translated_images/de/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direkte Anweisung ohne Beispiele — das Modell erschließt die Aufgabe allein aus der Anweisung*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Antwort: "Positiv"
```
  
**Wann verwenden:** Einfache Klassifikationen, direkte Fragen, Übersetzungen oder jede Aufgabe, die das Modell ohne zusätzliche Anleitung bewältigen kann.

### Few-Shot Prompting

Beispiele bereitstellen, die das Muster demonstrieren, dem das Modell folgen soll. Das Modell lernt das erwartete Eingabe-Ausgabe-Format von Ihren Beispielen und wendet es auf neue Eingaben an. Dies verbessert die Konsistenz drastisch bei Aufgaben, bei denen das gewünschte Format oder Verhalten nicht offensichtlich ist.

<img src="../../../translated_images/de/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Lernen aus Beispielen — das Modell erkennt das Muster und wendet es auf neue Eingaben an*

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
  
**Wann verwenden:** Benutzerdefinierte Klassifikationen, konsistente Formatierungen, domänenspezifische Aufgaben oder wenn Zero-Shot-Ergebnisse inkonsistent sind.

### Chain of Thought

Das Modell auffordern, seine Argumentation Schritt für Schritt zu zeigen. Statt direkt zur Antwort zu springen, zerlegt das Modell das Problem und arbeitet jeden Teil explizit durch. Das verbessert die Genauigkeit bei Mathematik, Logik und mehrstufigen Denkaufgaben.

<img src="../../../translated_images/de/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Schritt-für-Schritt-Argumentation — komplexe Probleme in explizite logische Schritte zerlegen*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Das Modell zeigt: 15 - 8 = 7, dann 7 + 12 = 19 Äpfel
```
  
**Wann verwenden:** Matheaufgaben, Logikrätsel, Debugging oder jede Aufgabe, bei der das Zeigen des Denkprozesses Genauigkeit und Vertrauen erhöht.

### Rollenspezifisches Prompting

Weisen Sie der KI vor der Frage eine Persona oder Rolle zu. Das liefert Kontext, der Ton, Tiefe und Fokus der Antwort prägt. Ein „Softwarearchitekt“ gibt andere Ratschläge als ein „Juniorentwickler“ oder ein „Sicherheitsexperte“.

<img src="../../../translated_images/de/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rollenspezifisches Prompting" width="800"/>

*Kontext- und Rollenvergabe — dieselbe Frage erhält je nach zugewiesener Rolle eine andere Antwort*

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
  
**Wann verwenden:** Code-Reviews, Schulungen, domänenspezifische Analysen oder wenn Sie Antworten benötigen, die an ein bestimmtes Fachwissen oder eine Perspektive angepasst sind.

### Prompt-Vorlagen

Erstellen Sie wiederverwendbare Prompts mit variablen Platzhaltern. Statt jedes Mal einen neuen Prompt zu schreiben, definieren Sie einmal eine Vorlage und füllen unterschiedliche Werte ein. LangChain4j’s `PromptTemplate`-Klasse macht dies mit der `{{variable}}`-Syntax einfach.

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
  
**Wann verwenden:** Wiederholte Abfragen mit unterschiedlichen Eingaben, Stapelverarbeitung, Aufbau wiederverwendbarer KI-Workflows oder jede Situation, in der sich die Prompt-Struktur nicht ändert, wohl aber die Daten.

---

Diese fünf Grundlagen geben Ihnen ein solides Werkzeugset für die meisten Prompting-Aufgaben. Der Rest dieses Moduls baut darauf mit **acht fortgeschrittenen Mustern** auf, die GPT-5.2’s Steuerung von Argumentation, Selbstevaluation und strukturierter Ausgabe nutzen.

## Fortgeschrittene Muster

Nachdem die Grundlagen behandelt sind, geht es nun zu den acht fortgeschrittenen Mustern, die dieses Modul einzigartig machen. Nicht alle Probleme benötigen den gleichen Ansatz. Manche Fragen verlangen schnelle Antworten, andere tiefes Nachdenken. Manche erfordern sichtbare Argumentation, andere nur Ergebnisse. Jedes untenstehende Muster ist für ein anderes Szenario optimiert — und GPT-5.2’s Steuerung der Argumentation lässt die Unterschiede noch deutlicher hervortreten.

<img src="../../../translated_images/de/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Acht Prompt-Engineering-Muster" width="800"/>

*Übersicht über die acht Prompt-Engineering-Muster und ihre Anwendungsfälle*

<img src="../../../translated_images/de/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Steuerung der Argumentation mit GPT-5.2" width="800"/>

*GPT-5.2’s Steuerung der Argumentation erlaubt anzugeben, wie viel Nachdenken das Modell leisten soll — von schnellen direkten Antworten bis zu tiefer Exploration*

**Niedrige Eile (Schnell & Fokussiert)** – Für einfache Fragen, bei denen Sie schnelle, direkte Antworten wollen. Das Modell denkt minimal nach - maximal 2 Schritte. Verwenden Sie dies für Berechnungen, Nachschlagen oder klare Fragen.

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
  
> 💡 **Mit GitHub Copilot erkunden:** Öffnen Sie [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) und fragen Sie:  
> - "Was ist der Unterschied zwischen niedrigen und hohen Eile-Prompting-Mustern?"  
> - "Wie helfen XML-Tags in Prompts dabei, die Antwort der KI zu strukturieren?"  
> - "Wann sollte ich Selbstreflexionsmuster gegenüber direkter Anweisung verwenden?"

**Hohe Eile (Tief & Gründlich)** – Für komplexe Probleme, bei denen Sie umfassende Analysen wollen. Das Modell arbeitet gründlich und zeigt detaillierte Argumentationen. Nutzen Sie dies für Systemdesign, Architekturentscheidungen oder komplexe Forschung.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**Aufgabenausführung (Progress Schritt für Schritt)** – Für mehrstufige Workflows. Das Modell liefert einen Plan im Voraus, erläutert jeden Schritt beim Arbeiten und gibt dann eine Zusammenfassung. Verwenden Sie dies bei Migrationen, Implementierungen oder jedem mehrstufigen Prozess.

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
  
Chain-of-Thought-Prompting fordert das Modell ausdrücklich auf, seinen Argumentationsprozess zu zeigen, was die Genauigkeit bei komplexen Aufgaben verbessert. Die Schritt-für-Schritt-Aufschlüsselung hilft sowohl Menschen als auch KI, die Logik zu verstehen.

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Fragen Sie zu diesem Muster:  
> - "Wie würde ich das Aufgabenausführungs-Muster für langlaufende Operationen anpassen?"  
> - "Was sind Best Practices zum Strukturieren von Tool-Preambles in produktiven Anwendungen?"  
> - "Wie kann ich Zwischenfortschrittsanzeigen in einer UI erfassen und anzeigen?"

<img src="../../../translated_images/de/task-execution-pattern.9da3967750ab5c1e.webp" alt="Aufgabenausführungs-Muster" width="800"/>

*Planen → Ausführen → Zusammenfassen Workflow für mehrstufige Aufgaben*

**Selbstreflektierender Code** – Für die Generierung von produktionsreifem Code. Das Modell schreibt Code nach Produktionsstandards mit ordentlicher Fehlerbehandlung. Verwenden Sie dies beim Aufbau neuer Features oder Dienste.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/de/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Selbstreflexionszyklus" width="800"/>

*Iterativer Verbesserungszyklus – generieren, bewerten, Probleme identifizieren, verbessern, wiederholen*

**Strukturierte Analyse** – Für konsistente Bewertungen. Das Modell überprüft Code anhand eines festen Rahmens (Korrektheit, Praktiken, Leistung, Sicherheit, Wartbarkeit). Verwenden Sie dies bei Code-Reviews oder Qualitätsbewertungen.

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
> - "Wie kann ich den Analyse-Rahmen für verschiedene Arten von Code-Reviews anpassen?"  
> - "Wie analysiere und verwende ich strukturierte Ausgaben programmgesteuert am besten?"  
> - "Wie stelle ich konsistente Schweregrade über verschiedene Review-Sitzungen sicher?"

<img src="../../../translated_images/de/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Strukturiertes Analyse-Muster" width="800"/>

*Rahmenwerk für konsistente Code-Reviews mit Schweregraden*

**Mehrstufiger Chat** – Für Gespräche, die Kontext benötigen. Das Modell erinnert sich an frühere Nachrichten und baut darauf auf. Verwenden Sie dies für interaktive Hilfesitzungen oder komplexe Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
<img src="../../../translated_images/de/context-memory.dff30ad9fa78832a.webp" alt="Kontext-Gedächtnis" width="800"/>

*Wie sich der Gesprächskontext über mehrere Runden ansammelt, bis das Token-Limit erreicht ist*

**Schritt-für-Schritt-Argumentation** – Für Probleme, die sichtbare Logik erfordern. Das Modell zeigt explizite Argumentationen für jeden Schritt. Verwenden Sie dies für Matheaufgaben, Logikrätsel oder wenn Sie den Denkprozess verstehen möchten.

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

**Eingeschränkte Ausgabe** – Für Antworten mit spezifischen Format-Anforderungen. Das Modell befolgt streng Format- und Längenregeln. Verwenden Sie dies für Zusammenfassungen oder wenn Sie eine präzise Ausgabe-Struktur benötigen.

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

## Verwendung vorhandener Azure-Ressourcen

**Bereitstellung überprüfen:**

Stellen Sie sicher, dass die `.env`-Datei mit den Azure-Zugangsdaten im Stammverzeichnis vorhanden ist (wurde in Modul 01 erstellt):
```bash
cat ../.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```
  
**Starten Sie die Anwendung:**

> **Hinweis:** Wenn Sie bereits alle Anwendungen mit `./start-all.sh` aus Modul 01 gestartet haben, läuft dieses Modul bereits auf Port 8083. Sie können die Startbefehle unten überspringen und direkt zu http://localhost:8083 wechseln.

**Option 1: Verwendung des Spring Boot Dashboards (Empfohlen für VS Code-Nutzer)**

Der Dev-Container enthält die Spring Boot Dashboard-Erweiterung, die eine visuelle Oberfläche zum Verwalten aller Spring Boot-Anwendungen bietet. Sie finden sie in der Aktivitätsleiste links in VS Code (suchen Sie nach dem Spring Boot-Symbol).

Über das Spring Boot Dashboard können Sie:  
- Alle verfügbaren Spring Boot-Anwendungen im Workspace sehen  
- Anwendungen mit einem Klick starten/stoppen  
- Anwendungsprotokolle in Echtzeit ansehen  
- Status der Anwendungen überwachen
Klicken Sie einfach auf die Wiedergabetaste neben „prompt-engineering“, um dieses Modul zu starten, oder starten Sie alle Module gleichzeitig.

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

Beide Skripte laden automatisch Umgebungsvariablen aus der `.env`-Datei im Root-Verzeichnis und erstellen die JARs, falls diese noch nicht existieren.

> **Hinweis:** Wenn Sie alle Module lieber manuell bauen möchten, bevor Sie sie starten:
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

*Das Haupt-Dashboard zeigt alle 8 Prompt-Engineering-Muster mit ihren Merkmalen und Anwendungsfällen*

## Die Muster erkunden

Die Weboberfläche ermöglicht es Ihnen, mit verschiedenen Prompting-Strategien zu experimentieren. Jedes Muster löst unterschiedliche Probleme – probieren Sie sie aus, um zu sehen, wann welche Methode am besten funktioniert.

> **Hinweis: Streaming vs. Non-Streaming** — Auf jeder Mustervorlage gibt es zwei Schaltflächen: **🔴 Stream Response (Live)** und eine **Nicht-Streaming**-Option. Streaming nutzt Server-Sent Events (SSE), um Token in Echtzeit anzuzeigen, während das Modell sie generiert, sodass Sie den Fortschritt sofort sehen. Die Nicht-Streaming-Option wartet, bis die ganze Antwort vorliegt, bevor sie angezeigt wird. Bei Prompts, die tiefgehendes Nachdenken erfordern (z.B. Hohe Eifer, Selbstreflektierender Code), kann der nicht-streamende Aufruf sehr lange dauern – manchmal mehrere Minuten – ohne sichtbares Feedback. **Verwenden Sie Streaming beim Experimentieren mit komplexen Prompts**, damit Sie die Arbeit des Modells sehen und nicht den Eindruck bekommen, die Anfrage sei zeitlich abgelaufen.
>
> **Hinweis: Browser-Voraussetzung** — Die Streaming-Funktion nutzt die Fetch Streams API (`response.body.getReader()`), die einen vollwertigen Browser (Chrome, Edge, Firefox, Safari) benötigt. Sie funktioniert **nicht** im eingebauten Simple Browser von VS Code, da dessen Webview die ReadableStream API nicht unterstützt. Wenn Sie den Simple Browser verwenden, funktionieren die Nicht-Streaming-Schaltflächen weiterhin normal – nur die Streaming-Schaltflächen sind betroffen. Öffnen Sie `http://localhost:8083` in einem externen Browser für das volle Erlebnis.

### Niedriger vs. hoher Eifer

Stellen Sie eine einfache Frage wie „Was sind 15 % von 200?“ mit niedrigem Eifer. Sie erhalten eine sofortige, direkte Antwort. Stellen Sie nun eine komplexe Frage wie „Entwerfen Sie eine Caching-Strategie für eine stark frequentierte API“ mit hohem Eifer. Klicken Sie auf **🔴 Stream Response (Live)** und beobachten Sie, wie die detaillierte Argumentation des Modells Token für Token erscheint. Dasselbe Modell, gleiche Fragenstruktur – aber der Prompt steuert, wie viel Denkaufwand investiert wird.

### Aufgaben-Ausführung (Tool-Preambles)

Mehrstufige Workflows profitieren von einer Vorausplanung und einer Fortschrittserzählung. Das Modell skizziert, was es tun wird, erläutert jeden Schritt und fasst die Ergebnisse zusammen.

### Selbstreflektierender Code

Probieren Sie „Erstelle einen E-Mail-Validierungsdienst“. Anstatt nur Code zu generieren und aufzuhören, generiert das Modell, bewertet anhand von Qualitätskriterien, identifiziert Schwächen und verbessert sich. Sie sehen es iterieren, bis der Code den Produktionsstandards entspricht.

### Strukturierte Analyse

Code-Reviews benötigen konsistente Bewertungsrahmen. Das Modell analysiert Code anhand fester Kategorien (Korrektheit, Praktiken, Leistung, Sicherheit) mit Schweregraden.

### Mehrstufiger Chat

Fragen Sie „Was ist Spring Boot?“ und folgen Sie dann direkt mit „Zeig mir ein Beispiel“. Das Modell erinnert sich an Ihre erste Frage und gibt Ihnen ein Spring Boot-Beispiel speziell dazu. Ohne Gedächtnis wäre die zweite Frage zu vage.

### Schritt-für-Schritt-Überlegung

Wählen Sie eine Mathematikaufgabe und versuchen Sie es sowohl mit Schritt-für-Schritt-Überlegung als auch mit niedrigem Eifer. Niedriger Eifer liefert nur die schnelle Antwort – schnell, aber undurchsichtig. Schritt-für-Schritt zeigt jede Berechnung und Entscheidung.

### Eingeschränkte Ausgabe

Wenn Sie bestimmte Formate oder Wortzahlen benötigen, erzwingt dieses Muster strikte Einhaltung. Versuchen Sie, eine Zusammenfassung mit genau 100 Wörtern in Stichpunktform zu erstellen.

## Was Sie wirklich lernen

**Denkaufwand ändert alles**

GPT-5.2 erlaubt es Ihnen, den Rechenaufwand über Ihre Prompts zu steuern. Geringer Aufwand bedeutet schnelle Antworten mit minimaler Recherche. Hoher Aufwand bedeutet, dass das Modell sich Zeit zum tiefen Nachdenken nimmt. Sie lernen, den Aufwand an die Komplexität der Aufgabe anzupassen – verschwenden Sie keine Zeit mit einfachen Fragen, aber überstürzen Sie keine komplexen Entscheidungen.

**Struktur steuert das Verhalten**

Fallen Ihnen die XML-Tags in den Prompts auf? Diese sind nicht dekorativ. Modelle folgen strukturierten Anweisungen zuverlässiger als freiem Text. Wenn Sie mehrstufige Prozesse oder komplexe Logik benötigen, hilft Struktur, dass das Modell weiß, wo es ist und was als Nächstes kommt.

<img src="../../../translated_images/de/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomie eines gut strukturierten Prompts mit klaren Abschnitten und XML-ähnlicher Organisation*

**Qualität durch Selbstevaluation**

Die selbstreflektierenden Muster funktionieren, indem Qualitätskriterien explizit gemacht werden. Anstatt zu hoffen, dass das Modell „richtig macht“, sagen Sie ihm genau, was „richtig“ bedeutet: korrekte Logik, Fehlerbehandlung, Leistung, Sicherheit. Das Modell kann dann seine eigene Ausgabe bewerten und verbessern. Das verwandelt Code-Generierung vom Glücksspiel in einen strukturierten Prozess.

**Kontext ist begrenzt**

Mehrstufige Konversationen funktionieren, indem bei jeder Anfrage der Nachrichtenverlauf übermittelt wird. Doch es gibt ein Limit – jedes Modell hat eine maximale Token-Anzahl. Wenn Gespräche wachsen, brauchen Sie Strategien, um relevanten Kontext zu behalten, ohne diese Grenze zu überschreiten. Dieses Modul zeigt Ihnen, wie das Gedächtnis funktioniert; später lernen Sie, wann Sie zusammenfassen, wann Sie vergessen und wann Sie abrufen sollten.

## Nächste Schritte

**Nächstes Modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Vorheriges: Modul 01 - Einführung](../01-introduction/README.md) | [Zurück zum Hauptmenü](../README.md) | [Weiter: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, bitten wir zu beachten, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Ursprungssprache gilt als maßgebliche Quelle. Bei wichtigen Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die durch die Nutzung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->