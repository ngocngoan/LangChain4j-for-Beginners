# Modul 02: Prompt Engineering mit GPT-5.2

## Inhaltsverzeichnis

- [Video-Durchgang](../../../02-prompt-engineering)
- [Was Sie lernen werden](../../../02-prompt-engineering)
- [Voraussetzungen](../../../02-prompt-engineering)
- [Verständnis von Prompt Engineering](../../../02-prompt-engineering)
- [Grundlagen des Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Rollenbasiertes Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Fortgeschrittene Muster](../../../02-prompt-engineering)
- [Verwendung bestehender Azure-Ressourcen](../../../02-prompt-engineering)
- [Screenshots der Anwendung](../../../02-prompt-engineering)
- [Erkundung der Muster](../../../02-prompt-engineering)
  - [Niedrige vs hohe Eiferbereitschaft](../../../02-prompt-engineering)
  - [Aufgabenausführung (Tool-Preambles)](../../../02-prompt-engineering)
  - [Selbstreflektierender Code](../../../02-prompt-engineering)
  - [Strukturierte Analyse](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Schritt-für-Schritt-Argumentation](../../../02-prompt-engineering)
  - [Eingeschränkte Ausgabe](../../../02-prompt-engineering)
- [Was Sie wirklich lernen](../../../02-prompt-engineering)
- [Nächste Schritte](../../../02-prompt-engineering)

## Video-Durchgang

Sehen Sie sich diese Live-Session an, die erklärt, wie Sie mit diesem Modul starten:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering mit LangChain4j - Live-Session" width="800"/></a>

## Was Sie lernen werden

<img src="../../../translated_images/de/what-youll-learn.c68269ac048503b2.webp" alt="Was Sie lernen werden" width="800"/>

Im vorherigen Modul haben Sie gesehen, wie Gedächtnis konversationelle KI ermöglicht und wie GitHub-Modelle für grundlegende Interaktionen genutzt werden. Jetzt konzentrieren wir uns darauf, wie Sie Fragen stellen — also die Prompts selbst — unter Verwendung von Azure OpenAI's GPT-5.2. Die Art, wie Sie Ihre Prompts strukturieren, beeinflusst die Qualität der Antworten dramatisch. Wir beginnen mit einer Übersicht der grundlegenden Prompting-Techniken und wechseln dann zu acht fortgeschrittenen Mustern, die die Fähigkeiten von GPT-5.2 voll ausnutzen.

Wir verwenden GPT-5.2, weil es eine Steuerung der Argumentation einführt – Sie können dem Modell sagen, wie viel Denken es vor der Antwort tun soll. Das macht unterschiedliche Prompting-Strategien klarer und hilft Ihnen zu verstehen, wann welche Methode anzuwenden ist. Außerdem profitieren wir von Azures geringeren Rate Limits für GPT-5.2 im Vergleich zu GitHub-Modellen.

## Voraussetzungen

- Abgeschlossenes Modul 01 (Azure OpenAI-Ressourcen bereitgestellt)
- `.env`-Datei im Stammverzeichnis mit Azure-Zugangsdaten (erstellt durch `azd up` in Modul 01)

> **Hinweis:** Wenn Sie Modul 01 noch nicht abgeschlossen haben, folgen Sie zuerst dort den Bereitstellungsanweisungen.

## Verständnis von Prompt Engineering

<img src="../../../translated_images/de/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Was ist Prompt Engineering?" width="800"/>

Prompt Engineering bedeutet, Eingabetexte so zu gestalten, dass Sie konsequent die benötigten Ergebnisse erhalten. Es geht nicht nur darum, Fragen zu stellen - sondern darum, Anfragen so zu strukturieren, dass das Modell genau versteht, was Sie wollen und wie es geliefert werden soll.

Stellen Sie sich vor, Sie geben einem Kollegen Anweisungen. „Behebe den Fehler“ ist unklar. „Behebe die Nullzeiger-Ausnahme in UserService.java Zeile 45, indem du eine Null-Prüfung hinzufügst“ ist spezifisch. Sprachmodelle funktionieren genauso – Spezifität und Struktur sind entscheidend.

<img src="../../../translated_images/de/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Wie LangChain4j passt" width="800"/>

LangChain4j stellt die Infrastruktur bereit — Modellverbindungen, Gedächtnis und Nachrichtentypen — während Prompt-Muster einfach sorgfältig strukturierte Texte sind, die Sie über diese Infrastruktur schicken. Die wichtigsten Bausteine sind `SystemMessage` (setzt das Verhalten und die Rolle der KI) und `UserMessage` (trägt Ihre eigentliche Anfrage).

## Grundlagen des Prompt Engineering

<img src="../../../translated_images/de/five-patterns-overview.160f35045ffd2a94.webp" alt="Übersicht über fünf Prompt Engineering Muster" width="800"/>

Bevor wir in die fortgeschrittenen Muster dieses Moduls eintauchen, schauen wir uns fünf grundlegende Prompting-Techniken an. Diese sind die Bausteine, die jeder Prompt Engineer kennen sollte. Wenn Sie bereits das [Quick Start Modul](../00-quick-start/README.md#2-prompt-patterns) durchgearbeitet haben, haben Sie diese in Aktion gesehen — hier ist der konzeptionelle Rahmen dahinter.

### Zero-Shot Prompting

Der einfachste Ansatz: Geben Sie dem Modell eine direkte Anweisung ohne Beispiele. Das Modell verlässt sich vollständig auf sein Training, um die Aufgabe zu verstehen und auszuführen. Das funktioniert gut bei einfachen Anfragen, bei denen das erwartete Verhalten offensichtlich ist.

<img src="../../../translated_images/de/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direkte Anweisung ohne Beispiele — das Modell schließt die Aufgabe allein aus der Anweisung*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Antwort: "Positiv"
```

**Wann anwenden:** Einfache Klassifizierungen, direkte Fragen, Übersetzungen oder jede Aufgabe, die das Modell ohne zusätzliche Anleitung bewältigen kann.

### Few-Shot Prompting

Geben Sie Beispiele an, die das Muster demonstrieren, dem das Modell folgen soll. Das Modell lernt das erwartete Eingabe-Ausgabe-Format aus Ihren Beispielen und wendet es auf neue Eingaben an. Das verbessert die Konsistenz erheblich bei Aufgaben, bei denen Format oder Verhalten nicht offensichtlich sind.

<img src="../../../translated_images/de/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Lernen aus Beispielen — das Modell erkennt das Muster und überträgt es auf neue Eingaben*

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

**Wann anwenden:** Maßgeschneiderte Klassifizierungen, konsistente Formatierung, domänenspezifische Aufgaben oder wenn Zero-Shot-Ergebnisse inkonsistent sind.

### Chain of Thought

Bitten Sie das Modell, seine Überlegungen Schritt für Schritt zu zeigen. Anstatt sofort eine Antwort zu geben, zerlegt das Modell das Problem und arbeitet jeden Teil explizit durch. Das verbessert die Genauigkeit bei mathematischen, logischen und mehrstufigen Argumentationsaufgaben.

<img src="../../../translated_images/de/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Schrittweise Argumentation — komplexe Probleme in explizite logische Schritte zerlegen*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Das Modell zeigt: 15 - 8 = 7, dann 7 + 12 = 19 Äpfel
```

**Wann anwenden:** Mathematische Probleme, Logikrätsel, Debugging oder jede Aufgabe, bei der das Zeigen des Denkprozesses Genauigkeit und Vertrauen verbessert.

### Rollenbasiertes Prompting

Geben Sie der KI vor der Frage eine Persona oder Rolle vor. Dies liefert Kontext, der Ton, Tiefe und Fokus der Antwort formt. Ein „Software-Architekt“ gibt andere Ratschläge als ein „Junior-Entwickler“ oder ein „Sicherheitsauditor“.

<img src="../../../translated_images/de/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rollenbasiertes Prompting" width="800"/>

*Kontext und Persona setzen — dieselbe Frage erhält unterschiedliche Antworten je nach zugewiesener Rolle*

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

**Wann anwenden:** Code-Reviews, Nachhilfe, domänenspezifische Analysen oder wenn Sie Antworten benötigen, die auf einem bestimmten Fachwissen oder Standpunkt basieren.

### Prompt Templates

Erstellen Sie wiederverwendbare Prompts mit variablen Platzhaltern. Statt jedes Mal einen neuen Prompt zu schreiben, definieren Sie eine Vorlage einmal und füllen unterschiedliche Werte ein. Die `PromptTemplate`-Klasse von LangChain4j erleichtert dies mit der Syntax `{{variable}}`.

<img src="../../../translated_images/de/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

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

**Wann anwenden:** Wiederholte Abfragen mit unterschiedlichen Eingaben, Batch-Verarbeitung, Aufbau wiederverwendbarer KI-Workflows oder jedes Szenario, wo die Prompt-Struktur gleich bleibt, aber die Daten sich ändern.

---

Diese fünf Grundlagen geben Ihnen ein solides Werkzeugset für die meisten Prompting-Aufgaben. Der Rest dieses Moduls baut darauf auf mit **acht fortgeschrittenen Mustern**, die GPT-5.2s Steuerung der Argumentation, Selbstbewertung und strukturierte Ausgabe-Fähigkeiten nutzen.

## Fortgeschrittene Muster

Nachdem die Grundlagen abgedeckt sind, wechseln wir zu den acht fortgeschrittenen Mustern, die dieses Modul einzigartig machen. Nicht alle Probleme brauchen dieselbe Herangehensweise. Manche Fragen benötigen schnelle Antworten, andere tiefgehendes Nachdenken. Manche brauchen sichtbare Argumentation, andere nur Ergebnisse. Jedes Muster unten ist auf ein anderes Szenario optimiert — und GPT-5.2s Steuerung der Argumentation macht die Unterschiede noch deutlicher.

<img src="../../../translated_images/de/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Acht Prompting-Muster" width="800"/>

*Übersicht der acht Prompt Engineering Muster und ihre Anwendungsfälle*

<img src="../../../translated_images/de/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Steuerung der Argumentation mit GPT-5.2" width="800"/>

*GPT-5.2s Steuerung der Argumentation ermöglicht anzugeben, wie viel Denken das Modell tun soll - von schnellen direkten Antworten bis zu tiefgehender Exploration*

**Niedrige Eiferbereitschaft (Schnell & Fokussiert)** – Für einfache Fragen, bei denen schnelle, direkte Antworten gewünscht sind. Das Modell führt minimale Argumentation durch – maximal 2 Schritte. Nutzen Sie dies für Berechnungen, Nachschlagevorgänge oder einfache Fragen.

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
> - „Was ist der Unterschied zwischen niedriger und hoher Eiferbereitschaft bei Prompting-Mustern?“
> - „Wie helfen die XML-Tags in Prompts, die Antwort der KI zu strukturieren?“
> - „Wann soll ich Selbstreflexionsmuster und wann direkte Anweisungen verwenden?“

**Hohe Eiferbereitschaft (Tief & Gründlich)** – Für komplexe Probleme, bei denen Sie umfassende Analysen wünschen. Das Modell erkundet gründlich und zeigt detaillierte Argumentationen. Nutzen Sie dies für Systemdesign, Architekturentscheidungen oder komplexe Recherchen.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Aufgabenausführung (Schritt-für-Schritt Fortschritt)** – Für mehrstufige Arbeitsabläufe. Das Modell liefert einen Plan vorab, erzählt jeden Schritt während der Arbeit und gibt anschließend eine Zusammenfassung. Verwenden Sie dies für Migrationen, Implementierungen oder beliebige mehrstufige Prozesse.

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

Chain-of-Thought Prompting bittet das Modell ausdrücklich, seinen Denkprozess zu zeigen, was die Genauigkeit bei komplexen Aufgaben verbessert. Die Schritt-für-Schritt-Aufschlüsselung hilft sowohl Menschen als auch KI, die Logik zu verstehen.

> **🤖 Versuchen Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Fragen Sie zu diesem Muster:
> - „Wie würde ich das Task-Execution-Muster für langandauernde Operationen anpassen?“
> - „Was sind Best Practices für das Strukturieren von Tool-Preambles in Produktionsanwendungen?“
> - „Wie kann ich Zwischenfortschritts-Updates erfassen und in einer UI anzeigen?“

<img src="../../../translated_images/de/task-execution-pattern.9da3967750ab5c1e.webp" alt="Aufgabenausführung-Muster" width="800"/>

*Planen → Ausführen → Zusammenfassen Workflow für mehrstufige Aufgaben*

**Selbstreflektierender Code** – Zum Generieren von produktionsreifem Code. Das Modell erzeugt Code gemäß Produktionsstandards mit ordentlichem Fehlerhandling. Verwenden Sie dies beim Erstellen neuer Features oder Dienste.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/de/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Selbstreflexionszyklus" width="800"/>

*Iterative Verbesserungsschleife – generieren, bewerten, Probleme identifizieren, verbessern, wiederholen*

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

> **🤖 Versuchen Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Fragen Sie zur strukturierten Analyse:
> - „Wie kann ich den Analyse-Rahmen für verschiedene Arten von Code-Reviews anpassen?“
> - „Wie kann ich strukturierte Ausgaben programmgesteuert parsen und darauf reagieren?“
> - „Wie stelle ich konsistente Schweregrade über verschiedene Review-Sitzungen sicher?“

<img src="../../../translated_images/de/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Strukturiertes Analyse-Muster" width="800"/>

*Rahmenwerk für konsistente Code-Reviews mit Schweregraden*

**Multi-Turn Chat** – Für Gespräche, die Kontext benötigen. Das Modell erinnert sich an vorherige Nachrichten und baut darauf auf. Nutzen Sie dies für interaktive Hilfesitzungen oder komplexe Q&A.

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

*Wie sich der Gesprächskontext über mehrere Runden hinweg anhäuft, bis das Token-Limit erreicht ist*

**Schritt-für-Schritt-Argumentation** – Für Probleme, die sichtbare Logik verlangen. Das Modell zeigt explizite Argumentationen für jeden Schritt. Verwenden Sie dies für mathematische Probleme, Logikrätsel oder wenn Sie den Denkprozess verstehen müssen.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/de/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Schritt-für-Schritt Muster" width="800"/>

*Zerlegen von Problemen in explizite logische Schritte*

**Eingeschränkte Ausgabe** – Für Antworten mit spezifischen Formatvorgaben. Das Modell hält sich strikt an Format- und Längenregeln. Nutzen Sie dies für Zusammenfassungen oder wenn Sie eine präzise Ausgabestruktur benötigen.

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

*Durchsetzung von spezifischen Format-, Längen- und Strukturvorgaben*

## Verwendung bestehender Azure-Ressourcen

**Bereitstellung überprüfen:**

Stellen Sie sicher, dass die `.env`-Datei im Stammverzeichnis mit Azure-Zugangsdaten vorhanden ist (während Modul 01 erstellt):
```bash
cat ../.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```

**Starten der Anwendung:**

> **Hinweis:** Wenn Sie bereits alle Anwendungen mit `./start-all.sh` aus Modul 01 gestartet haben, läuft dieses Modul bereits auf Port 8083. Sie können die untenstehenden Startbefehle überspringen und direkt zu http://localhost:8083 gehen.
**Option 1: Verwendung des Spring Boot Dashboards (Empfohlen für VS Code-Nutzer)**

Der Dev-Container enthält die Spring Boot Dashboard-Erweiterung, die eine visuelle Oberfläche zur Verwaltung aller Spring Boot-Anwendungen bereitstellt. Sie finden sie in der Aktivitätsleiste auf der linken Seite von VS Code (suchen Sie nach dem Spring Boot-Symbol).

Über das Spring Boot Dashboard können Sie:
- Alle verfügbaren Spring Boot-Anwendungen im Workspace sehen
- Anwendungen mit einem einzigen Klick starten/stoppen
- Anwendungsprotokolle in Echtzeit ansehen
- Den Anwendungsstatus überwachen

Klicken Sie einfach auf die Wiedergabetaste neben „prompt-engineering“, um dieses Modul zu starten, oder starten Sie alle Module auf einmal.

<img src="../../../translated_images/de/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Option 2: Verwendung von Shell-Skripten**

Alle Webanwendungen starten (Module 01-04):

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
  
Oder nur dieses Modul starten:

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
  
Beide Skripte laden automatisch Umgebungsvariablen aus der `.env`-Datei im Root-Verzeichnis und bauen die JARs, falls sie nicht existieren.

> **Hinweis:** Wenn Sie bevorzugen, alle Module manuell vor dem Start zu bauen:
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

*Das Haupt-Dashboard zeigt alle 8 Prompt-Engineering-Muster mit ihren Merkmalen und Anwendungsfällen*

## Die Muster erkunden

Die Weboberfläche ermöglicht es, mit verschiedenen Prompting-Strategien zu experimentieren. Jedes Muster löst unterschiedliche Probleme – probieren Sie sie aus, um zu sehen, wann welche Herangehensweise glänzt.

> **Hinweis: Streaming vs. Nicht-Streaming** — Jede Musterseite bietet zwei Buttons: **🔴 Stream Response (Live)** und eine **Nicht-Streaming**-Option. Streaming verwendet Server-Sent Events (SSE), um Tokens in Echtzeit anzuzeigen, während das Modell sie generiert, sodass Sie den Fortschritt sofort sehen. Die Nicht-Streaming-Option wartet auf die gesamte Antwort, bevor sie angezeigt wird. Bei Prompts, die tiefes Nachdenken auslösen (z. B. High Eagerness, Self-Reflecting Code), kann der Nicht-Streaming-Aufruf sehr lange dauern – manchmal Minuten – ohne sichtbares Feedback. **Verwenden Sie Streaming beim Experimentieren mit komplexen Prompts**, damit Sie das Modell arbeiten sehen und den Eindruck vermeiden, dass die Anfrage abgelaufen ist.
>
> **Hinweis: Browser-Anforderung** — Die Streaming-Funktion nutzt die Fetch Streams API (`response.body.getReader()`), die einen vollwertigen Browser (Chrome, Edge, Firefox, Safari) benötigt. Sie funktioniert **nicht** im integrierten Simple Browser von VS Code, da dessen Webview die ReadableStream API nicht unterstützt. Wenn Sie den Simple Browser verwenden, funktionieren die Nicht-Streaming-Buttons weiterhin normal – nur die Streaming-Buttons sind betroffen. Öffnen Sie `http://localhost:8083` in einem externen Browser für das volle Erlebnis.

### Niedrige vs. Hohe Eifrigkeit

Stellen Sie eine einfache Frage wie „Was sind 15 % von 200?“ mit niedriger Eifrigkeit. Sie erhalten eine sofortige, direkte Antwort. Stellen Sie nun eine komplexe Frage wie „Entwerfen Sie eine Cache-Strategie für eine stark frequentierte API“ mit hoher Eifrigkeit. Klicken Sie auf **🔴 Stream Response (Live)** und beobachten Sie, wie das Modell seine detaillierte Überlegung Token für Token anzeigt. Dasselbe Modell, dieselbe Frage-Struktur – aber der Prompt gibt vor, wie viel Denkarbeit geleistet wird.

### Ausführung von Aufgaben (Tool-Preambles)

Mehrstufige Workflows profitieren von einer Planung im Voraus und einer Fortschrittserzählung. Das Modell skizziert, was es tun wird, beschreibt jeden Schritt und fasst dann die Ergebnisse zusammen.

### Selbstreflektierender Code

Probieren Sie „Erstelle einen E-Mail-Validierungsdienst“. Anstatt nur Code zu generieren und zu stoppen, generiert das Modell, bewertet diesen anhand von Qualitätskriterien, identifiziert Schwächen und verbessert ihn. Sie sehen, wie es iteriert, bis der Code den Produktionsstandards entspricht.

### Strukturierte Analyse

Code-Reviews benötigen konsistente Bewertungsrahmen. Das Modell analysiert Code mit festen Kategorien (Korrektheit, Praktiken, Performance, Sicherheit) und Schweregraden.

### Multi-Turn Chat

Fragen Sie „Was ist Spring Boot?“ und fragen Sie dann sofort nach „Zeig mir ein Beispiel“. Das Modell erinnert sich an Ihre erste Frage und liefert Ihnen spezifisch ein Spring Boot-Beispiel. Ohne Gedächtnis wäre diese zweite Frage zu vage.

### Schritt-für-Schritt Reasoning

Wählen Sie ein mathematisches Problem und versuchen Sie es mit Schritt-für-Schritt Reasoning und niedriger Eifrigkeit. Niedrige Eifrigkeit gibt Ihnen nur die Antwort – schnell, aber undurchsichtig. Schritt-für-Schritt zeigt jede Berechnung und Entscheidung.

### Eingeschränkte Ausgabe

Wenn Sie bestimmte Formate oder Wortzahlen benötigen, erzwingt dieses Muster strikte Einhaltung. Versuchen Sie, eine Zusammenfassung mit genau 100 Worten im Bullet-Point-Format zu generieren.

## Was Sie wirklich lernen

**Denkanstrengung verändert alles**

GPT-5.2 ermöglicht es Ihnen, den Rechenaufwand über Ihre Prompts zu steuern. Niedriger Aufwand bedeutet schnelle Antworten mit minimaler Erkundung. Hoher Aufwand bedeutet, dass das Modell Zeit zum tiefen Nachdenken nimmt. Sie lernen, Aufwand an die Komplexität der Aufgabe anzupassen – verschwenden Sie keine Zeit mit einfachen Fragen, aber überstürzen Sie auch keine komplexen Entscheidungen.

**Struktur steuert Verhalten**

Haben Sie die XML-Tags in den Prompts bemerkt? Sie sind keine Dekoration. Modelle folgen strukturierten Anweisungen zuverlässiger als freiformigem Text. Wenn Sie mehrstufige Prozesse oder komplexe Logik benötigen, hilft Struktur dem Modell zu verfolgen, wo es ist und was als Nächstes kommt.

<img src="../../../translated_images/de/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Struktur" width="800"/>

*Anatomie eines gut strukturierten Prompts mit klaren Abschnitten und XML-ähnlicher Organisation*

**Qualität durch Selbstbewertung**

Die selbstreflektierenden Muster funktionieren, indem Qualitätskriterien explizit gemacht werden. Anstatt zu hoffen, dass das Modell „es richtig macht“, sagen Sie ihm genau, was „richtig“ bedeutet: korrekte Logik, Fehlerbehandlung, Leistung, Sicherheit. Das Modell kann dann seine eigene Ausgabe bewerten und verbessern. Das verwandelt die Code-Generierung von einer Lotterie in einen Prozess.

**Kontext ist endlich**

Mehrrundengespräche funktionieren, weil die Nachrichtenhistorie bei jeder Anfrage mitgesendet wird. Aber es gibt ein Limit – jedes Modell hat eine maximale Tokenanzahl. Wenn Gespräche wachsen, benötigen Sie Strategien, um relevanten Kontext zu behalten, ohne dieses Limit zu überschreiten. Dieses Modul zeigt Ihnen, wie Gedächtnis funktioniert; später lernen Sie, wann man zusammenfasst, wann man vergisst und wann man abruft.

## Nächste Schritte

**Nächstes Modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Vorheriges: Modul 01 - Einführung](../01-introduction/README.md) | [Zurück zur Hauptseite](../README.md) | [Weiter: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mithilfe des KI-Übersetzungsdienstes [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, können automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten. Das Originaldokument in seiner Ursprungssprache ist als maßgebliche Quelle anzusehen. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Nutzung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->