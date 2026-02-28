# Modul 05: Model Context Protocol (MCP)

## Inhaltsverzeichnis

- [Was Sie lernen werden](../../../05-mcp)
- [Was ist MCP?](../../../05-mcp)
- [Wie MCP funktioniert](../../../05-mcp)
- [Das agentische Modul](../../../05-mcp)
- [Ausführen der Beispiele](../../../05-mcp)
  - [Voraussetzungen](../../../05-mcp)
- [Schnellstart](../../../05-mcp)
  - [Dateioperationen (Stdio)](../../../05-mcp)
  - [Supervisor Agent](../../../05-mcp)
    - [Demo ausführen](../../../05-mcp)
    - [Wie der Supervisor funktioniert](../../../05-mcp)
    - [Antwortstrategien](../../../05-mcp)
    - [Das Ausgabeverstehen](../../../05-mcp)
    - [Erklärung der Funktionen des agentischen Moduls](../../../05-mcp)
- [Kernkonzepte](../../../05-mcp)
- [Herzlichen Glückwunsch!](../../../05-mcp)
  - [Wie geht es weiter?](../../../05-mcp)

## Was Sie lernen werden

Sie haben konversationale KI gebaut, Prompts gemeistert, Antworten auf Dokumente abgestimmt und Agenten mit Tools erstellt. Aber all diese Tools wurden speziell für Ihre Anwendung entwickelt. Was wäre, wenn Sie Ihrer KI Zugang zu einem standardisierten Ökosystem von Tools geben könnten, das jeder erstellen und teilen kann? In diesem Modul lernen Sie genau das mit dem Model Context Protocol (MCP) und dem agentischen Modul von LangChain4j. Wir zeigen zuerst einen einfachen MCP-Dateileser und dann, wie er sich mühelos in fortgeschrittene agentische Workflows mittels des Supervisor Agent-Musters integriert.

## Was ist MCP?

Das Model Context Protocol (MCP) bietet genau das – eine standardisierte Möglichkeit für KI-Anwendungen, externe Tools zu entdecken und zu nutzen. Statt für jede Datenquelle oder jeden Dienst benutzerdefinierte Integrationen zu schreiben, verbinden Sie sich mit MCP-Servern, die ihre Fähigkeiten in einem einheitlichen Format bereitstellen. Ihr KI-Agent kann dann diese Tools automatisch entdecken und verwenden.

<img src="../../../translated_images/de/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Vergleich" width="800"/>

*Vor MCP: Komplexe Punkt-zu-Punkt-Integrationen. Nach MCP: Ein Protokoll, unendliche Möglichkeiten.*

MCP löst ein grundlegendes Problem der KI-Entwicklung: Jede Integration ist maßgeschneidert. Sie wollen GitHub nutzen? Custom Code. Sie wollen Dateien lesen? Custom Code. Sie wollen eine Datenbank abfragen? Custom Code. Und keine dieser Integrationen funktioniert mit anderen KI-Anwendungen.

MCP standardisiert dies. Ein MCP-Server bietet Tools mit klaren Beschreibungen und Schemata an. Jeder MCP-Client kann sich verbinden, verfügbare Tools entdecken und verwenden. Einmal erstellen, überall nutzen.

<img src="../../../translated_images/de/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architektur" width="800"/>

*Model Context Protocol-Architektur – standardisierte Tool-Erkennung und Ausführung*

## Wie MCP funktioniert

<img src="../../../translated_images/de/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protokoll Details" width="800"/>

*Wie MCP unter der Haube funktioniert — Clients entdecken Tools, tauschen JSON-RPC-Nachrichten aus und führen Operationen über eine Transportschicht aus.*

**Server-Client-Architektur**

MCP verwendet ein Client-Server-Modell. Server stellen Tools bereit – Dateien lesen, Datenbanken abfragen, APIs aufrufen. Clients (Ihre KI-Anwendung) verbinden sich mit Servern und nutzen deren Tools.

Um MCP mit LangChain4j zu verwenden, fügen Sie diese Maven-Abhängigkeit hinzu:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```
  
**Tool-Erkennung**

Wenn sich Ihr Client mit einem MCP-Server verbindet, fragt er „Welche Tools hast du?“ Der Server antwortet mit einer Liste verfügbarer Tools, jeweils mit Beschreibungen und Parametern-Schemata. Ihr KI-Agent kann dann anhand der Benutzeranfragen entscheiden, welche Tools er verwenden möchte.

<img src="../../../translated_images/de/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool-Erkennung" width="800"/>

*Die KI entdeckt verfügbare Tools beim Start – sie weiß nun, welche Fähigkeiten verfügbar sind und kann entscheiden, welche sie nutzt.*

**Transportmechanismen**

MCP unterstützt verschiedene Transportmechanismen. Dieses Modul zeigt den Stdio-Transport für lokale Prozesse:

<img src="../../../translated_images/de/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transportmechanismen" width="800"/>

*MCP Transportmechanismen: HTTP für entfernte Server, Stdio für lokale Prozesse*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Für lokale Prozesse. Ihre Anwendung startet einen Server als Unterprozess und kommuniziert über Standard-Ein-/-Ausgabe. Nützlich für Dateisystemzugriffe oder Kommandozeilentools.

```java
McpTransport stdioTransport = new StdioMcpTransport.Builder()
    .command(List.of(
        npmCmd, "exec",
        "@modelcontextprotocol/server-filesystem@2025.12.18",
        resourcesDir
    ))
    .logEvents(false)
    .build();
```
  
<img src="../../../translated_images/de/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Ablauf" width="800"/>

*Stdio Transport in Aktion – Ihre Anwendung startet den MCP-Server als Kindprozess und kommuniziert via stdin/stdout Pipes.*

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) und fragen Sie:
> - „Wie funktioniert der Stdio-Transport und wann sollte ich ihn gegenüber HTTP verwenden?“
> - „Wie verwaltet LangChain4j den Lebenszyklus gestarteter MCP-Serverprozesse?“
> - „Welche Sicherheitsimplikationen hat es, der KI Zugriff auf das Dateisystem zu geben?“

## Das agentische Modul

Während MCP standardisierte Tools bietet, liefert LangChain4j’s **agentisches Modul** eine deklarative Möglichkeit, Agenten zu bauen, die diese Tools orchestrieren. Die `@Agent`-Annotation und `AgenticServices` erlauben es Ihnen, das Agentenverhalten über Schnittstellen statt imperativen Code zu definieren.

In diesem Modul erkunden Sie das **Supervisor Agent**-Muster – einen fortgeschrittenen agentischen KI-Ansatz, bei dem ein „Supervisor“-Agent dynamisch entscheidet, welche Sub-Agenten basierend auf Benutzeranfragen aufgerufen werden. Wir kombinieren diese Konzepte, indem wir einem unserer Sub-Agenten MCP-gestützte Dateizugriffsfähigkeiten geben.

Um das agentische Modul zu verwenden, fügen Sie diese Maven-Abhängigkeit hinzu:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
  
> **⚠️ Experimentell:** Das Modul `langchain4j-agentic` ist **experimentell** und noch im Wandel. Der stabile Weg, KI-Assistenten zu bauen, bleibt `langchain4j-core` mit benutzerdefinierten Tools (Modul 04).

## Ausführen der Beispiele

### Voraussetzungen

- Java 21+, Maven 3.9+
- Node.js 16+ und npm (für MCP-Server)
- Umgebungsvariablen in der `.env`-Datei konfiguriert (vom Root-Verzeichnis):
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (wie bei den Modulen 01-04)

> **Hinweis:** Wenn Sie Ihre Umgebungsvariablen noch nicht gesetzt haben, siehe [Modul 00 - Schnellstart](../00-quick-start/README.md) für Anleitungen, oder kopieren Sie `.env.example` zu `.env` im Root-Verzeichnis und füllen Sie Ihre Werte aus.

## Schnellstart

**Mit VS Code:** Klicken Sie einfach mit der rechten Maustaste auf eine Demo-Datei im Explorer und wählen Sie **"Run Java"**, oder verwenden Sie die Startkonfigurationen im Run & Debug Panel (stellen Sie sicher, dass Sie zuvor Ihren Token in der `.env`-Datei hinzugefügt haben).

**Mit Maven:** Alternativ können Sie die Beispiele auch über die Kommandozeile ausführen.

### Dateioperationen (Stdio)

Dies demonstriert lokal unterprozessbasierte Tools.

**✅ Keine Voraussetzungen erforderlich** – der MCP-Server wird automatisch gestartet.

**Start-Skripte (empfohlen):**

Die Start-Skripte laden automatisch Umgebungsvariablen aus der Root-`.env` Datei:

**Bash:**  
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```
  
**PowerShell:**  
```powershell
cd 05-mcp
.\start-stdio.ps1
```
  
**Mit VS Code:** Rechtsklicken Sie auf `StdioTransportDemo.java` und wählen **„Run Java“** (stellen Sie sicher, dass Ihre `.env`-Datei konfiguriert ist).

Die Anwendung startet automatisch einen Dateisystem-MCP-Server und liest eine lokale Datei. Beachten Sie, wie die Verwaltung des Unterprozesses für Sie übernommen wird.

**Erwartete Ausgabe:**  
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```
  
### Supervisor Agent

Das **Supervisor Agent-Muster** ist eine **flexible** Form agentischer KI. Ein Supervisor nutzt ein LLM, um autonom zu entscheiden, welche Agenten basierend auf der Benutzeranfrage aufgerufen werden. Im nächsten Beispiel kombinieren wir MCP-gestützten Dateizugriff mit einem LLM-Agenten, um einen überwachten Workflow „Datei lesen → Bericht erstellen“ zu realisieren.

Im Demo liest `FileAgent` eine Datei über MCP-Dateisystemtools, und `ReportAgent` erstellt einen strukturierten Bericht mit einer Executive Summary (1 Satz), 3 Schlüsselpunkten und Empfehlungen. Der Supervisor orchestriert diesen Ablauf automatisch:

<img src="../../../translated_images/de/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Muster" width="800"/>

*Der Supervisor nutzt sein LLM, um zu entscheiden, welche Agenten in welcher Reihenfolge aufgerufen werden – keine fest kodierte Steuerung nötig.*

So sieht der konkrete Workflow unserer Datei-zu-Bericht-Pipeline aus:

<img src="../../../translated_images/de/file-report-workflow.649bb7a896800de9.webp" alt="Datei zu Bericht Workflow" width="800"/>

*FileAgent liest die Datei via MCP-Tools, dann verwandelt ReportAgent den Rohinhalt in einen strukturierten Bericht.*

Jeder Agent speichert seine Ausgabe im **Agentic Scope** (gemeinsamer Speicher), sodass nachfolgende Agenten auf frühere Ergebnisse zugreifen können. Dies zeigt, wie MCP-Tools nahtlos in agentische Workflows integrieren – der Supervisor muss nicht wissen, *wie* Dateien gelesen werden, nur dass `FileAgent` es kann.

#### Demo ausführen

Die Start-Skripte laden automatisch Umgebungsvariablen aus der Root-`.env` Datei:

**Bash:**  
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```
  
**PowerShell:**  
```powershell
cd 05-mcp
.\start-supervisor.ps1
```
  
**Mit VS Code:** Rechtsklicken Sie auf `SupervisorAgentDemo.java` und wählen **„Run Java“** (stellen Sie sicher, dass Ihre `.env`-Datei konfiguriert ist).

#### Wie der Supervisor funktioniert

```java
// Schritt 1: FileAgent liest Dateien mit MCP-Tools
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Hat MCP-Tools für Dateioperationen
        .build();

// Schritt 2: ReportAgent erstellt strukturierte Berichte
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor steuert den Datei → Bericht-Workflow
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Gibt den finalen Bericht zurück
        .build();

// Der Supervisor entscheidet, welche Agenten basierend auf der Anfrage aufgerufen werden sollen
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```
  
#### Antwortstrategien

Wenn Sie einen `SupervisorAgent` konfigurieren, legen Sie fest, wie er seine endgültige Antwort an den Benutzer formuliert, nachdem die Sub-Agenten ihre Aufgaben abgeschlossen haben.

<img src="../../../translated_images/de/response-strategies.3d0cea19d096bdf9.webp" alt="Antwortstrategien" width="800"/>

*Drei Strategien, wie der Supervisor seine finale Antwort formuliert – wählen Sie, ob Sie die Ausgabe des letzten Agenten, eine synthetisierte Zusammenfassung oder die bestbewertete Option wünschen.*

Verfügbare Strategien:

| Strategie | Beschreibung |
|----------|-------------|
| **LAST** | Der Supervisor liefert die Ausgabe des zuletzt aufgerufenen Sub-Agenten oder Tools zurück. Dies ist nützlich, wenn der letzte Agent im Workflow speziell darauf ausgelegt ist, die vollständige, finale Antwort zu liefern (z. B. ein „Summary Agent“ in einer Forschungspipeline). |
| **SUMMARY** | Der Supervisor nutzt sein internes Sprachmodell (LLM), um eine Zusammenfassung der gesamten Interaktion und aller Sub-Agent-Ausgaben zu synthetisieren und gibt diese Zusammenfassung als finale Antwort zurück. Das bietet dem Benutzer eine klare, aggregierte Antwort. |
| **SCORED** | Das System verwendet ein internes LLM, um sowohl die LAST-Antwort als auch die SUMMARY der Interaktion gegen die ursprüngliche Benutzeranfrage zu bewerten und gibt die Ausgabe mit der höheren Bewertung zurück. |

Siehe [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) für die vollständige Implementierung.

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) und fragen Sie:
> - „Wie entscheidet der Supervisor, welche Agenten er aufruft?“
> - „Was ist der Unterschied zwischen Supervisor- und sequentiellen Workflow-Mustern?“
> - „Wie kann ich das Planungsverhalten des Supervisors anpassen?“

#### Das Ausgabeverstehen

Wenn Sie die Demo ausführen, sehen Sie eine strukturierte Schritt-für-Schritt-Darstellung, wie der Supervisor mehrere Agenten orchestriert. Dies bedeutet jeder Abschnitt:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Die Überschrift** stellt das Workflow-Konzept vor: eine fokussierte Pipeline vom Dateilesen zur Berichtserstellung.

```
--- WORKFLOW ---------------------------------------------------------
  ┌─────────────┐      ┌──────────────┐
  │  FileAgent  │ ───▶ │ ReportAgent  │
  │ (MCP tools) │      │  (pure LLM)  │
  └─────────────┘      └──────────────┘
   outputKey:           outputKey:
   'fileContent'        'report'

--- AVAILABLE AGENTS -------------------------------------------------
  [FILE]   FileAgent   - Reads files via MCP → stores in 'fileContent'
  [REPORT] ReportAgent - Generates structured report → stores in 'report'
```
  
**Workflow-Diagramm** zeigt den Datenfluss zwischen den Agenten. Jeder Agent hat eine spezifische Rolle:
- **FileAgent** liest Dateien über MCP-Tools und speichert den Rohinhalt in `fileContent`
- **ReportAgent** verarbeitet diesen Inhalt und erzeugt einen strukturierten Bericht in `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Benutzeranfrage** zeigt die Aufgabe. Der Supervisor analysiert diese und entscheidet, FileAgent → ReportAgent aufzurufen.

```
--- SUPERVISOR ORCHESTRATION -----------------------------------------
  The Supervisor decides which agents to invoke and passes data between them...

  +-- STEP 1: Supervisor chose -> FileAgent (reading file via MCP)
  |
  |   Input: .../file.txt
  |
  |   Result: LangChain4j is an open-source, provider-agnostic Java framework for building LLM...
  +-- [OK] FileAgent (reading file via MCP) completed

  +-- STEP 2: Supervisor chose -> ReportAgent (generating structured report)
  |
  |   Input: LangChain4j is an open-source, provider-agnostic Java framew...
  |
  |   Result: Executive Summary...
  +-- [OK] ReportAgent (generating structured report) completed
```
  
**Supervisor-Orchestrierung** zeigt den 2-Schritte-Fluss in Aktion:
1. **FileAgent** liest die Datei via MCP und speichert den Inhalt
2. **ReportAgent** erhält den Inhalt und erstellt einen strukturierten Bericht

Der Supervisor traf diese Entscheidungen **autonom** basierend auf der Benutzeranfrage.

```
--- FINAL RESPONSE ---------------------------------------------------
Executive Summary
...

Key Points
...

Recommendations
...

--- AGENTIC SCOPE (Data Flow) ----------------------------------------
  Each agent stores its output for downstream agents to consume:
  * fileContent: LangChain4j is an open-source, provider-agnostic Java framework...
  * report: Executive Summary...
```
  
#### Erklärung der Funktionen des agentischen Moduls

Das Beispiel demonstriert mehrere fortgeschrittene Features des agentischen Moduls. Schauen wir uns Agentic Scope und Agent Listener genauer an.

**Agentic Scope** zeigt den gemeinsamen Speicher, in dem Agenten ihre Ergebnisse mit `@Agent(outputKey="...")` ablegen. Dies ermöglicht:
- Nachfolgende Agenten können auf die Ausgaben vorheriger Agenten zugreifen
- Der Supervisor kann eine finale Antwort synthetisieren
- Sie können prüfen, was jeder Agent erzeugt hat

<img src="../../../translated_images/de/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Gemeinsamer Speicher" width="800"/>

*Agentic Scope fungiert als gemeinsamer Speicher – FileAgent schreibt `fileContent`, ReportAgent liest diesen und schreibt `report`, und Ihr Code liest das Endergebnis.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Rohdateidaten vom FileAgent
String report = scope.readState("report");            // Strukturierter Bericht vom ReportAgent
```
  
**Agent Listener** ermöglichen Überwachung und Debugging der Agentenausführung. Die schrittweise Ausgabe, die Sie in der Demo sehen, stammt von einem AgentListener, der sich in jeden Agentenaufruf einklinkt:
- **beforeAgentInvocation** - Wird aufgerufen, wenn der Supervisor einen Agent auswählt, damit Sie sehen können, welcher Agent gewählt wurde und warum
- **afterAgentInvocation** - Wird aufgerufen, wenn ein Agent abgeschlossen ist und zeigt dessen Ergebnis
- **inheritedBySubagents** - Wenn true, überwacht der Listener alle Agenten in der Hierarchie

<img src="../../../translated_images/de/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listener greifen in den Ausführungslebenszyklus ein — überwachen, wann Agenten starten, abschließen oder auf Fehler stoßen.*

```java
AgentListener monitor = new AgentListener() {
    private int step = 0;
    
    @Override
    public void beforeAgentInvocation(AgentRequest request) {
        step++;
        System.out.println("  +-- STEP " + step + ": " + request.agentName());
    }
    
    @Override
    public void afterAgentInvocation(AgentResponse response) {
        System.out.println("  +-- [OK] " + response.agentName() + " completed");
    }
    
    @Override
    public boolean inheritedBySubagents() {
        return true; // An alle Unteragenten weiterleiten
    }
};
```

Neben dem Supervisor-Pattern bietet das `langchain4j-agentic` Modul mehrere leistungsstarke Workflow-Patterns und Funktionen:

<img src="../../../translated_images/de/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Fünf Workflow-Patterns zur Orchestrierung von Agenten — von einfachen sequentiellen Pipelines bis hin zu Genehmigungs-Workflows mit menschlicher Interaktion.*

| Pattern | Beschreibung | Anwendungsfall |
|---------|--------------|----------------|
| **Sequential** | Führe Agenten nacheinander aus, Ausgabe fließt zum nächsten | Pipelines: recherchieren → analysieren → berichten |
| **Parallel** | Führe Agenten gleichzeitig aus | Unabhängige Aufgaben: Wetter + Nachrichten + Aktien |
| **Loop** | Iterieren, bis Bedingung erfüllt ist | Qualitätsbewertung: verfeinern, bis Score ≥ 0,8 |
| **Conditional** | Route basierend auf Bedingungen | Klassifizieren → an Spezialagent weiterleiten |
| **Human-in-the-Loop** | Füge menschliche Kontrollpunkte hinzu | Genehmigungs-Workflows, Inhaltsprüfung |

## Schlüsselkonzepte

Nachdem Sie MCP und das agentic-Modul in Aktion erkundet haben, fassen wir zusammen, wann Sie welchen Ansatz verwenden sollten.

<img src="../../../translated_images/de/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP schafft ein universelles Protokoll-Ökosystem — jeder MCP-kompatible Server funktioniert mit jedem MCP-kompatiblen Client, was Tools über Anwendungen hinweg teilbar macht.*

**MCP** eignet sich ideal, wenn Sie bestehende Tool-Ökosysteme nutzen, Tools entwickeln möchten, die mehrere Anwendungen teilen können, Drittanbieterdienste mit Standardprotokollen integrieren oder Tool-Implementierungen austauschen wollen, ohne Code zu ändern.

**Das Agentic-Modul** ist optimal, wenn Sie deklarative Agent-Definitionen mit `@Agent` Annotationen wollen, Workflow-Orchestrierung (sequentiell, Schleife, parallel) benötigen, eine Schnittstellen-basierte Agent-Entwicklung gegenüber imperativem Code bevorzugen oder mehrere Agenten kombinieren, die Ausgaben via `outputKey` teilen.

**Das Supervisor Agent Pattern** zeigt seine Stärke, wenn der Workflow nicht vorab vorhersehbar ist und Sie möchten, dass das LLM entscheidet, wenn Sie mehrere spezialisierte Agenten mit dynamischer Orchestrierung haben, wenn Sie Konversationssysteme bauen, die unterschiedliche Fähigkeiten ansteuern, oder wenn Sie das flexibelste, adaptive Agenten-Verhalten wünschen.

<img src="../../../translated_images/de/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Wann verwenden Sie benutzerdefinierte @Tool-Methoden vs. MCP-Tools — benutzerdefinierte Tools für app-spezifische Logik mit voller Typsicherheit, MCP-Tools für standardisierte Integrationen, die über Anwendungen hinweg funktionieren.*

## Herzlichen Glückwunsch!

<img src="../../../translated_images/de/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Ihre Lernreise durch alle fünf Module — vom einfachen Chat bis zu MCP-gestützten agentic Systemen.*

Sie haben den LangChain4j für Anfänger-Kurs abgeschlossen. Sie haben gelernt:

- Wie man konversationelle KI mit Gedächtnis aufbaut (Modul 01)
- Prompt-Engineering-Patterns für verschiedene Aufgaben (Modul 02)
- Antworten in Ihren Dokumenten mit RAG verankern (Modul 03)
- Einfache KI-Agenten (Assistenten) mit benutzerdefinierten Tools erstellen (Modul 04)
- Standardisierte Tools mit den LangChain4j MCP- und Agentic-Modulen integrieren (Modul 05)

### Wie geht es weiter?

Nach Abschluss der Module erkunden Sie den [Testing Guide](../docs/TESTING.md), um LangChain4j-Testkonzepte in der Praxis zu sehen.

**Offizielle Ressourcen:**
- [LangChain4j Dokumentation](https://docs.langchain4j.dev/) - Umfassende Anleitungen und API-Referenz
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Quellcode und Beispiele
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - Schritt-für-Schritt-Tutorials für verschiedene Anwendungsfälle

Vielen Dank für den Abschluss dieses Kurses!

---

**Navigation:** [← Zurück: Modul 04 - Tools](../04-tools/README.md) | [Zurück zur Übersicht](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, kann es bei automatisierten Übersetzungen zu Fehlern oder Ungenauigkeiten kommen. Das Originaldokument in seiner ursprünglichen Sprache ist als maßgebliche Quelle zu betrachten. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die durch die Nutzung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->