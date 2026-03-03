# Modul 02: Prompt Engineering mit GPT-5.2

## Inhaltsverzeichnis

- [Video Walkthrough](../../../02-prompt-engineering)
- [Was Sie lernen werden](../../../02-prompt-engineering)
- [Voraussetzungen](../../../02-prompt-engineering)
- [Verständnis von Prompt Engineering](../../../02-prompt-engineering)
- [Grundlagen des Prompt Engineerings](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Rollenbasiertes Prompting](../../../02-prompt-engineering)
  - [Prompt-Vorlagen](../../../02-prompt-engineering)
- [Fortgeschrittene Muster](../../../02-prompt-engineering)
- [Anwendung starten](../../../02-prompt-engineering)
- [Screenshots der Anwendung](../../../02-prompt-engineering)
- [Erkundung der Muster](../../../02-prompt-engineering)
  - [Niedrige vs. hohe Bereitschaft](../../../02-prompt-engineering)
  - [Aufgabenausführung (Werkzeug-Preambles)](../../../02-prompt-engineering)
  - [Selbstreflektierender Code](../../../02-prompt-engineering)
  - [Strukturierte Analyse](../../../02-prompt-engineering)
  - [Mehrstufiger Chat](../../../02-prompt-engineering)
  - [Schritt-für-Schritt-Argumentation](../../../02-prompt-engineering)
  - [Eingeschränkte Ausgabe](../../../02-prompt-engineering)
- [Was Sie wirklich lernen](../../../02-prompt-engineering)
- [Nächste Schritte](../../../02-prompt-engineering)

## Video Walkthrough

Sehen Sie sich diese Live-Session an, die erklärt, wie Sie mit diesem Modul starten:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering mit LangChain4j - Live Session" width="800"/></a>

## Was Sie lernen werden

Das folgende Diagramm gibt einen Überblick über die wichtigsten Themen und Fähigkeiten, die Sie in diesem Modul entwickeln werden — von Techniken zur Verfeinerung von Prompts bis zum schrittweisen Workflow, dem Sie folgen.

<img src="../../../translated_images/de/what-youll-learn.c68269ac048503b2.webp" alt="Was Sie lernen werden" width="800"/>

In den vorherigen Modulen haben Sie grundlegende Interaktionen mit LangChain4j und GitHub-Modellen erforscht und gesehen, wie Speicher konversationelle KI mit Azure OpenAI ermöglicht. Jetzt konzentrieren wir uns darauf, wie Sie Fragen stellen — also die Prompts selbst — mit Azure OpenAI's GPT-5.2. Die Art und Weise, wie Sie Ihre Prompts strukturieren, beeinflusst dramatisch die Qualität der Antworten, die Sie erhalten. Wir beginnen mit einer Übersicht der grundlegenden Prompting-Techniken und gehen dann zu acht fortgeschrittenen Mustern über, die die Möglichkeiten von GPT-5.2 voll ausschöpfen.

Wir verwenden GPT-5.2, weil es eine Steuerung des Denkens einführt – Sie können dem Modell sagen, wie viel Nachdenken vor der Antwort erfolgen soll. Das macht verschiedene Prompting-Strategien deutlicher sichtbar und hilft Ihnen zu verstehen, welche Ansätze wann zu verwenden sind. Außerdem profitieren wir von Azure's geringeren Ratenbegrenzungen für GPT-5.2 im Vergleich zu GitHub-Modellen.

## Voraussetzungen

- Abgeschlossenes Modul 01 (Azure OpenAI-Ressourcen bereitgestellt)
- `.env` Datei im Hauptverzeichnis mit Azure-Anmeldedaten (erstellt durch `azd up` im Modul 01)

> **Hinweis:** Falls Sie Modul 01 noch nicht abgeschlossen haben, folgen Sie bitte zuerst den dortigen Bereitstellungsanweisungen.

## Verständnis von Prompt Engineering

Im Kern ist Prompt Engineering der Unterschied zwischen vagen und präzisen Anweisungen, wie der folgende Vergleich zeigt.

<img src="../../../translated_images/de/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Was ist Prompt Engineering?" width="800"/>

Prompt Engineering bedeutet, Texteingaben so zu gestalten, dass sie konsistent die gewünschten Ergebnisse liefern. Es geht nicht nur ums Fragenstellen – sondern darum, Anfragen so zu strukturieren, dass das Modell genau versteht, was Sie wollen und wie es geliefert werden soll.

Denken Sie daran wie an eine Anweisung an einen Kollegen. „Behebe den Fehler“ ist vage. „Behebe die NullPointerException in UserService.java Zeile 45 durch Hinzufügen einer Nullprüfung“ ist spezifisch. Sprachmodelle funktionieren genauso – Spezifität und Struktur zählen.

Das folgende Diagramm zeigt, wie LangChain4j in dieses Bild passt — es verbindet Ihre Prompt-Muster mit dem Modell über die Bausteine `SystemMessage` und `UserMessage`.

<img src="../../../translated_images/de/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Wie LangChain4j passt" width="800"/>

LangChain4j stellt die Infrastruktur bereit — Modellverbindungen, Speicher und Nachrichtentypen — während Prompt-Muster einfach sorgfältig strukturierte Texte sind, die Sie durch diese Infrastruktur senden. Die Schlüsselbausteine sind `SystemMessage` (setzt das Verhalten und die Rolle der KI) und `UserMessage` (enthält Ihre eigentliche Anfrage).

## Grundlagen des Prompt Engineerings

Die fünf Kerntechniken, die unten gezeigt sind, bilden die Grundlage für effektives Prompt Engineering. Jede behandelt einen anderen Aspekt, wie Sie mit Sprachmodellen kommunizieren.

<img src="../../../translated_images/de/five-patterns-overview.160f35045ffd2a94.webp" alt="Übersicht der fünf Muster des Prompt Engineerings" width="800"/>

Bevor wir in diesem Modul zu den fortgeschrittenen Mustern übergehen, sehen wir uns fünf grundlegende Prompting-Techniken an. Diese sind die Bausteine, die jeder Prompt-Ingenieur kennen sollte. Wenn Sie bereits das [Quick Start Modul](../00-quick-start/README.md#2-prompt-patterns) durchgearbeitet haben, kennen Sie diese bereits in der Praxis — hier ist das konzeptuelle Grundgerüst dahinter.

### Zero-Shot Prompting

Der einfachste Ansatz: Geben Sie dem Modell eine direkte Anweisung ohne Beispiele. Das Modell verlässt sich vollständig auf sein Training, um die Aufgabe zu verstehen und auszuführen. Das funktioniert gut bei einfachen Anforderungen, wo das erwartete Verhalten offensichtlich ist.

<img src="../../../translated_images/de/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direkte Anweisung ohne Beispiele — das Modell leitet die Aufgabe allein aus der Anweisung ab*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Antwort: "Positiv"
```

**Wann verwenden:** Für einfache Klassifikationen, direkte Fragen, Übersetzungen oder Aufgaben, die das Modell ohne zusätzliche Anleitung bewältigen kann.

### Few-Shot Prompting

Geben Sie Beispiele an, die das Muster zeigen, dem das Modell folgen soll. Das Modell lernt von Ihren Beispielen das erwartete Eingabe-Ausgabe-Format und wendet es auf neue Eingaben an. Das verbessert die Konsistenz bei Aufgaben, wo Format oder Verhalten nicht offensichtlich sind.

<img src="../../../translated_images/de/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Lernen anhand von Beispielen — das Modell erkennt das Muster und wendet es auf neue Eingaben an*

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

**Wann verwenden:** Für kundenspezifische Klassifikationen, konsistente Formatierung, domänenspezifische Aufgaben oder wenn Zero-Shot-Ergebnisse inkonsistent sind.

### Chain of Thought

Bitten Sie das Modell, seine Argumentation Schritt für Schritt zu zeigen. Statt direkt zur Antwort zu springen, zerlegt das Modell das Problem und arbeitet jeden Teil explizit durch. Das verbessert die Genauigkeit bei Mathematik, Logik und mehrstufigen Denkaufgaben.

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

**Wann verwenden:** Für Mathematikaufgaben, Logikrätsel, Debugging oder Aufgaben, bei denen das Zeigen des Denkprozesses Genauigkeit und Vertrauen verbessert.

### Rollenbasiertes Prompting

Weisen Sie der KI vor der Fragestellung eine Persona oder Rolle zu. Das liefert Kontext, der Ton, Tiefe und Fokus der Antwort prägt. Ein „Softwarearchitekt“ gibt andere Ratschläge als ein „Junior-Entwickler“ oder ein „Sicherheitsauditor“.

<img src="../../../translated_images/de/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rollenbasiertes Prompting" width="800"/>

*Kontext und Persona setzen — dieselbe Frage erhält je nach zugewiesener Rolle unterschiedliche Antworten*

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

**Wann verwenden:** Für Code-Reviews, Nachhilfe, domänenspezifische Analysen oder wenn Sie Antworten benötigen, die an ein bestimmtes Fachwissen oder eine Perspektive angepasst sind.

### Prompt-Vorlagen

Erstellen Sie wiederverwendbare Prompts mit variablen Platzhaltern. Statt jedes Mal einen neuen Prompt zu schreiben, definieren Sie eine Vorlage einmal und füllen verschiedene Werte ein. Die `PromptTemplate` Klasse von LangChain4j macht das mit `{{variable}}` Syntax einfach.

<img src="../../../translated_images/de/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt-Vorlagen" width="800"/>

*Wiederverwendbare Prompts mit variablen Platzhaltern — eine Vorlage, viele Verwendungen*

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

**Wann verwenden:** Für wiederholte Anfragen mit unterschiedlichen Eingaben, Stapelverarbeitung, Aufbau wiederverwendbarer KI-Workflows oder beliebige Szenarien, bei denen die Prompt-Struktur gleich bleibt, aber die Daten sich ändern.

---

Diese fünf Grundlagen geben Ihnen ein solides Werkzeugset für die meisten Prompting-Aufgaben. Der Rest dieses Moduls baut darauf mit **acht fortgeschrittenen Mustern** auf, die GPT-5.2s Steuerung von Denkprozessen, Selbstbewertung und strukturierte Ausgabe-Fähigkeiten ausnutzen.

## Fortgeschrittene Muster

Mit den Grundlagen abgedeckt, wenden wir uns den acht fortgeschrittenen Mustern zu, die dieses Modul einzigartig machen. Nicht alle Probleme brauchen den gleichen Ansatz. Manche Fragen verlangen schnelle Antworten, andere tiefgründiges Nachdenken. Manche brauchen sichtbare Argumentation, andere nur Ergebnisse. Jedes Muster unten ist auf ein anderes Szenario optimiert — und GPT-5.2s Steuerung des Denkens macht die Unterschiede noch deutlicher.

<img src="../../../translated_images/de/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Acht Muster für Prompt Engineering" width="800"/>

*Überblick über die acht Muster des Prompt Engineerings und deren Anwendungsfälle*

GPT-5.2 fügt den Mustern eine weitere Dimension hinzu: *Steuerung des Denkens*. Der Schieberegler unten zeigt, wie Sie den Denkaufwand des Modells anpassen können — von schnellen, direkten Antworten bis hin zu tiefgründiger, gründlicher Analyse.

<img src="../../../translated_images/de/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Steuerung des Denkens mit GPT-5.2" width="800"/>

*Die Steuerung des Denkens bei GPT-5.2 lässt Sie festlegen, wie viel das Modell nachdenken soll — von schnellen direkten Antworten bis zu tiefgründiger Erforschung*

**Niedrige Bereitschaft (Schnell & Fokussiert)** - Für einfache Fragen, bei denen Sie schnelle, direkte Antworten wollen. Das Modell denkt minimal — maximal 2 Schritte. Nutzen Sie das für Berechnungen, Nachschlagen oder einfache Fragen.

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
> - „Was ist der Unterschied zwischen niedriger und hoher Bereitschaft bei Prompt-Mustern?“
> - „Wie helfen die XML-Tags in Prompts, die Antwort der KI zu strukturieren?“
> - „Wann sollte ich Selbstreflexionsmuster vs. direkte Anweisungen verwenden?“

**Hohe Bereitschaft (Tief & Gründlich)** - Für komplexe Probleme, bei denen Sie umfassende Analysen wollen. Das Modell erforscht gründlich und zeigt detaillierte Argumentationen. Nutzen Sie das für Systemdesign, Architekturentscheidungen oder komplexe Recherche.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Aufgabenausführung (Schrittweiser Fortschritt)** - Für mehrschrittige Arbeitsabläufe. Das Modell liefert einen Plan vorab, berichtet jeden Schritt beim Arbeiten und gibt dann eine Zusammenfassung. Verwenden Sie dies für Migrationen, Implementierungen oder jegliche mehrstufigen Prozesse.

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

Chain-of-Thought Prompting fordert das Modell explizit auf, seinen Denkprozess zu zeigen, was die Genauigkeit bei komplexen Aufgaben verbessert. Die schrittweise Zerlegung hilft sowohl Menschen als auch KI, die Logik zu verstehen.

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Fragen Sie zu diesem Muster:
> - „Wie passe ich das Aufgabenausführungsmuster für langlaufende Operationen an?“
> - „Was sind bewährte Methoden für die Strukturierung von Werkzeug-Preambles in Produktionsanwendungen?“
> - „Wie erfasse und zeige ich Zwischenfortschritte in einer Benutzeroberfläche an?“

Das folgende Diagramm illustriert diesen Plan → Ausführen → Zusammenfassen Workflow.

<img src="../../../translated_images/de/task-execution-pattern.9da3967750ab5c1e.webp" alt="Muster für Aufgabenausführung" width="800"/>

*Plan → Ausführen → Zusammenfassen Arbeitsablauf für mehrstufige Aufgaben*

**Selbstreflektierender Code** - Für die Erzeugung von Produktionscode. Das Modell generiert Code nach Produktionsstandards mit ordentlicher Fehlerbehandlung. Nutzen Sie das, wenn Sie neue Features oder Services entwickeln.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

Das folgende Diagramm zeigt diese iterative Verbesserungsschleife — generieren, bewerten, Schwachstellen erkennen, verfeinern bis der Code Produktionsstandards erfüllt.

<img src="../../../translated_images/de/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Selbstreflexionszyklus" width="800"/>

*Iterative Verbesserungsschleife - generieren, bewerten, Probleme identifizieren, verbessern, wiederholen*

**Strukturierte Analyse** - Für konsistente Bewertungen. Das Modell prüft Code anhand eines festen Rahmens (Korrektheit, Praktiken, Leistung, Sicherheit, Wartbarkeit). Nutzen Sie das für Code-Reviews oder Qualitätsbewertungen.

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
> - „Wie kann ich strukturierte Ausgaben programmatisch parsen und verwenden?“
> - „Wie stelle ich konsistente Schweregrade über verschiedene Review-Sitzungen sicher?“

Das folgende Diagramm zeigt, wie dieser strukturierte Rahmen ein Code-Review in konsistente Kategorien mit Schweregraden unterteilt.

<img src="../../../translated_images/de/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Muster für strukturierte Analyse" width="800"/>

*Rahmenwerk für konsistente Code-Reviews mit Schweregraden*

**Mehrstufiger Chat** - Für Konversationen, die Kontext brauchen. Das Modell erinnert sich an vorherige Nachrichten und baut darauf auf. Nutzen Sie das für interaktive Hilfesitzungen oder komplexe Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

Das folgende Diagramm visualisiert, wie Kontext in der Unterhaltung mit jedem Schritt wächst und wie das im Verhältnis zum Token-Limit des Modells steht.

<img src="../../../translated_images/de/context-memory.dff30ad9fa78832a.webp" alt="Kontext-Speicher" width="800"/>

*Wie sich der Gesprächskontext über mehrere Schritte ansammelt, bis das Token-Limit erreicht wird*
**Schritt-für-Schritt-Begründung** – Für Probleme, die sichtbare Logik erfordern. Das Modell zeigt eine explizite Begründung für jeden Schritt. Verwenden Sie dies für Mathematikaufgaben, Logikrätsel oder wenn Sie den Denkprozess nachvollziehen müssen.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```
  
Das untenstehende Diagramm veranschaulicht, wie das Modell Probleme in explizite, nummerierte logische Schritte unterteilt.

<img src="../../../translated_images/de/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Schritt-für-Schritt-Muster" width="800"/>

*Probleme in explizite logische Schritte zerlegen*

**Eingeschränkte Ausgabe** – Für Antworten mit spezifischen Formatvorgaben. Das Modell hält sich strikt an Format- und Längenregeln. Verwenden Sie dies für Zusammenfassungen oder wenn Sie eine präzise Ausgabe benötigen.

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
  
Das folgende Diagramm zeigt, wie Einschränkungen das Modell anleiten, eine Ausgabe zu erzeugen, die streng den Format- und Längenanforderungen entspricht.

<img src="../../../translated_images/de/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Muster für eingeschränkte Ausgabe" width="800"/>

*Durchsetzung spezifischer Format-, Längen- und Strukturvorgaben*

## Anwendung starten

**Bereitstellung überprüfen:**

Stellen Sie sicher, dass die `.env`-Datei im Stammverzeichnis mit Azure-Anmeldedaten existiert (wurde während Modul 01 erstellt). Führen Sie dies aus dem Modulverzeichnis (`02-prompt-engineering/`) aus:

**Bash:**  
```bash
cat ../.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```
  
**PowerShell:**  
```powershell
Get-Content ..\.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```
  
**Starten der Anwendung:**

> **Hinweis:** Wenn Sie bereits alle Anwendungen mit `./start-all.sh` aus dem Stammverzeichnis gestartet haben (wie in Modul 01 beschrieben), läuft dieses Modul bereits auf Port 8083. Sie können die Startbefehle unten überspringen und direkt zu http://localhost:8083 gehen.

**Option 1: Verwendung des Spring Boot Dashboards (empfohlen für VS Code-Nutzer)**

Der Dev-Container enthält die Erweiterung Spring Boot Dashboard, die eine visuelle Oberfläche zur Verwaltung aller Spring Boot-Anwendungen bietet. Sie finden sie in der Aktivitätsleiste links in VS Code (suchen Sie nach dem Spring Boot Symbol).

Vom Spring Boot Dashboard aus können Sie:
- Alle verfügbaren Spring Boot-Anwendungen im Workspace sehen
- Anwendungen mit einem Klick starten/stoppen
- Anwendungsprotokolle in Echtzeit ansehen
- Anwendungsstatus überwachen

Klicken Sie einfach auf die Wiedergabetaste neben „prompt-engineering“, um dieses Modul zu starten, oder starten Sie alle Module auf einmal.

<img src="../../../translated_images/de/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*Das Spring Boot Dashboard in VS Code — Starte, stoppe und überwache alle Module an einem Ort*

**Option 2: Verwendung von Shell-Skripten**

Alle Webanwendungen starten (Module 01-04):

**Bash:**  
```bash
cd ..  # Aus dem Stammverzeichnis
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
  
Beide Skripte laden automatisch Umgebungsvariablen aus der `.env`-Datei im Stammverzeichnis und bauen die JARs, falls diese noch nicht existieren.

> **Hinweis:** Wenn Sie alle Module manuell vor dem Start bauen möchten:
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

Hier sehen Sie die Hauptoberfläche des Prompt Engineering Moduls, wo Sie alle acht Muster nebeneinander ausprobieren können.

<img src="../../../translated_images/de/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Startseite" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Das Haupt-Dashboard zeigt alle 8 Prompt-Engineering-Muster mit ihren Eigenschaften und Anwendungsfällen*

## Die Muster erkunden

Die Weboberfläche ermöglicht es Ihnen, verschiedene Prompting-Strategien auszuprobieren. Jedes Muster löst unterschiedliche Probleme – probieren Sie es aus, um zu sehen, wann welche Methode glänzt.

> **Hinweis: Streaming vs. Nicht-Streaming** — Jede Muster-Seite bietet zwei Buttons: **🔴 Stream Response (Live)** und eine **Nicht-Streaming**-Option. Streaming verwendet Server-Sent Events (SSE), um Tokens in Echtzeit anzuzeigen, während das Modell sie generiert, sodass Sie den Fortschritt sofort sehen. Die Nicht-Streaming-Option wartet, bis die gesamte Antwort fertig ist, bevor sie angezeigt wird. Bei Prompts, die tiefes Nachdenken erfordern (z. B. hohe Eifrigkeit, selbstreflektierender Code), kann der Nicht-Streaming-Aufruf sehr lange dauern – manchmal Minuten – ohne sichtbares Feedback. **Verwenden Sie Streaming beim Experimentieren mit komplexen Prompts**, damit Sie sehen, wie das Modell arbeitet, und nicht den Eindruck bekommen, die Anfrage sei abgelaufen.
>
> **Hinweis: Browser-Anforderung** — Die Streaming-Funktion nutzt die Fetch Streams API (`response.body.getReader()`), die einen vollständigen Browser (Chrome, Edge, Firefox, Safari) erfordert. Sie funktioniert **nicht** im integrierten Simple Browser von VS Code, da dessen Webview die ReadableStream API nicht unterstützt. Im Simple Browser funktionieren die Nicht-Streaming Buttons weiterhin normal – nur die Streaming Buttons sind betroffen. Öffnen Sie `http://localhost:8083` in einem externen Browser für das volle Erlebnis.

### Niedrige vs. hohe Eifrigkeit

Stellen Sie eine einfache Frage wie „Was sind 15 % von 200?“ mit niedriger Eifrigkeit. Sie erhalten eine sofortige, direkte Antwort. Stellen Sie nun eine komplexe Frage wie „Entwerfe eine Caching-Strategie für eine stark frequentierte API“ mit hoher Eifrigkeit. Klicken Sie auf **🔴 Stream Response (Live)** und beobachten Sie, wie die detaillierte Begründung des Modells Token für Token erscheint. Dasselbe Modell, dieselbe Frage – nur der Prompt steuert, wie intensiv nachgedacht wird.

### Aufgaben-Ausführung (Tool-Preambels)

Mehrstufige Arbeitsabläufe profitieren von einer vorherigen Planung und Fortschrittskommentierung. Das Modell skizziert, was es tun wird, erläutert jeden Schritt und fasst die Ergebnisse zusammen.

### Selbstreflektierender Code

Probieren Sie „Erstelle einen E-Mail-Validierungsdienst“. Anstatt nur Code zu generieren und zu stoppen, erzeugt das Modell, bewertet ihn anhand von Qualitätskriterien, erkennt Schwächen und verbessert den Code. Sie sehen, wie es iteriert, bis der Code den Produktionsstandards entspricht.

### Strukturierte Analyse

Code Reviews brauchen konsistente Bewertungsrahmen. Das Modell analysiert den Code anhand fester Kategorien (Korrektheit, Praktiken, Leistung, Sicherheit) mit Schweregraden.

### Multi-Turn Chat

Fragen Sie „Was ist Spring Boot?“ und folgen Sie direkt mit „Zeig mir ein Beispiel“. Das Modell erinnert sich an Ihre erste Frage und gibt Ihnen ein spezielles Spring Boot-Beispiel. Ohne Gedächtnis wäre die zweite Frage zu vage.

### Schritt-für-Schritt-Begründung

Wählen Sie ein Mathematikproblem und probieren Sie es mit Schritt-für-Schritt-Begründung und niedriger Eifrigkeit. Niedrige Eifrigkeit gibt nur die Antwort – schnell, aber undurchsichtig. Schritt-für-Schritt zeigt jeden Rechenschritt und jede Entscheidung.

### Eingeschränkte Ausgabe

Wenn Sie bestimmte Formate oder Wortzahlen benötigen, erzwingt dieses Muster eine strikte Einhaltung. Versuchen Sie, eine Zusammenfassung mit exakt 100 Wörtern im Stichpunktformat zu generieren.

## Was Sie wirklich lernen

**Der Denkaufwand verändert alles**

GPT-5.2 lässt Sie den Rechenaufwand über Ihre Prompts steuern. Niedriger Aufwand bedeutet schnelle Antworten mit minimaler Exploration. Hoher Aufwand bedeutet, das Modell nimmt sich Zeit zum tiefen Nachdenken. Sie lernen, den Aufwand an die Komplexität der Aufgabe anzupassen – verschwenden Sie keine Zeit bei einfachen Fragen, hetzen Sie aber auch komplexe Entscheidungen nicht durch.

**Struktur steuert Verhalten**

Fallen Ihnen die XML-Tags in den Prompts auf? Sie sind keine Dekoration. Modelle folgen strukturierten Anweisungen zuverlässiger als freien Text. Wenn Sie mehrschrittige Prozesse oder komplexe Logik benötigen, hilft Struktur dem Modell, zu verfolgen, wo es ist und was als Nächstes kommt. Das folgende Diagramm zerlegt einen gut strukturierten Prompt und zeigt, wie Tags wie `<system>`, `<instructions>`, `<context>`, `<user-input>` und `<constraints>` Ihre Anweisungen in eindeutige Abschnitte gliedern.

<img src="../../../translated_images/de/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt-Struktur" width="800"/>

*Aufbau eines gut strukturierten Prompts mit klaren Abschnitten und XML-artiger Organisation*

**Qualität durch Selbstevaluation**

Die selbstreflektierenden Muster arbeiten, indem Qualitätskriterien explizit gemacht werden. Anstatt zu hoffen, dass das Modell „richtig macht“, sagen Sie ihm genau, was „richtig“ bedeutet: korrekte Logik, Fehlerbehandlung, Leistung, Sicherheit. Das Modell kann dann seine eigene Ausgabe bewerten und verbessern. So wird das Code-Generieren vom Glücksspiel zu einem Prozess.

**Kontext ist begrenzt**

Mehrstufige Gespräche arbeiten, indem sie den Nachrichtenverlauf mit jeder Anfrage einbinden. Aber es gibt ein Limit – jedes Modell hat eine maximale Token-Anzahl. Wenn Gespräche wachsen, brauchen Sie Strategien, um relevanten Kontext zu bewahren, ohne diese Grenze zu überschreiten. Dieses Modul zeigt Ihnen, wie Gedächtnis funktioniert; später lernen Sie, wann Sie zusammenfassen, vergessen und abrufen sollten.

## Nächste Schritte

**Nächstes Modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Zurück: Modul 01 - Einführung](../01-introduction/README.md) | [Zurück zum Hauptmenü](../README.md) | [Weiter: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, kann es bei automatisierten Übersetzungen zu Fehlern oder Ungenauigkeiten kommen. Das ursprüngliche Dokument in seiner Originalsprache gilt als maßgebliche Quelle. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Nutzung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->