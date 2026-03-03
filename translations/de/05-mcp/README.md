# Modul 05: Model Context Protocol (MCP)

## Inhaltsverzeichnis

- [Was Sie lernen werden](../../../05-mcp)
- [Was ist MCP?](../../../05-mcp)
- [Wie MCP funktioniert](../../../05-mcp)
- [Das Agentic-Modul](../../../05-mcp)
- [Die Beispiele ausführen](../../../05-mcp)
  - [Voraussetzungen](../../../05-mcp)
- [Schnellstart](../../../05-mcp)
  - [Dateioperationen (Stdio)](../../../05-mcp)
  - [Supervisor-Agent](../../../05-mcp)
    - [Demo ausführen](../../../05-mcp)
    - [Wie der Supervisor funktioniert](../../../05-mcp)
    - [Antwortstrategien](../../../05-mcp)
    - [Verstehen der Ausgabe](../../../05-mcp)
    - [Erklärung der Agentic-Modul-Funktionen](../../../05-mcp)
- [Schlüsselkonzepte](../../../05-mcp)
- [Herzlichen Glückwunsch!](../../../05-mcp)
  - [Was kommt als Nächstes?](../../../05-mcp)

## Was Sie lernen werden

Sie haben konversationelle KI gebaut, mit Prompts experimentiert, Antworten auf Dokumente bezogen und Agenten mit Werkzeugen erstellt. Aber all diese Werkzeuge wurden speziell für Ihre Anwendung maßgeschneidert. Was wäre, wenn Sie Ihrer KI Zugriff auf ein standardisiertes Ökosystem von Werkzeugen geben könnten, die jeder erstellen und teilen kann? In diesem Modul lernen Sie genau das mit dem Model Context Protocol (MCP) und LangChain4j's agentischem Modul. Zuerst zeigen wir einen einfachen MCP-Dateileser und anschließend, wie er sich mühelos in fortgeschrittene agentische Workflows mit dem Supervisor-Agent-Muster integrieren lässt.

## Was ist MCP?

Das Model Context Protocol (MCP) bietet genau das – eine standardisierte Methode für KI-Anwendungen, externe Werkzeuge zu entdecken und zu verwenden. Anstatt für jede Datenquelle oder jeden Dienst individuelle Integrationen zu schreiben, verbinden Sie sich mit MCP-Servern, die ihre Fähigkeiten in einem einheitlichen Format bereitstellen. Ihr KI-Agent kann diese Werkzeuge dann automatisch entdecken und nutzen.

Das folgende Diagramm zeigt den Unterschied — ohne MCP erfordert jede Integration eine individuelle Punkt-zu-Punkt-Verkabelung; mit MCP verbindet ein einziges Protokoll Ihre App mit jedem Werkzeug:

<img src="../../../translated_images/de/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Vergleich" width="800"/>

*Vor MCP: Komplexe Punkt-zu-Punkt-Integrationen. Nach MCP: Ein Protokoll, unendliche Möglichkeiten.*

MCP löst ein grundlegendes Problem in der KI-Entwicklung: Jede Integration ist individuell. Möchten Sie GitHub nutzen? Eigenes Coding. Dateien lesen? Eigenes Coding. Datenbank abfragen? Eigenes Coding. Und keine dieser Integrationen funktioniert mit anderen KI-Anwendungen.

MCP standardisiert das. Ein MCP-Server stellt Werkzeuge mit klaren Beschreibungen und Schemata bereit. Jeder MCP-Client kann sich verbinden, verfügbare Werkzeuge entdecken und sie nutzen. Einmal bauen, überall verwenden.

Das folgende Diagramm veranschaulicht diese Architektur — ein einzelner MCP-Client (Ihre KI-Anwendung) verbindet sich mit mehreren MCP-Servern, die jeweils ihre eigenen Werkzeugsätze über das Standardprotokoll bereitstellen:

<img src="../../../translated_images/de/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architektur" width="800"/>

*Model Context Protocol-Architektur – standardisierte Werkzeugentdeckung und -ausführung*

## Wie MCP funktioniert

Im Hintergrund verwendet MCP eine geschichtete Architektur. Ihre Java-Anwendung (der MCP-Client) entdeckt verfügbare Werkzeuge, sendet JSON-RPC-Anfragen über eine Transportschicht (Stdio oder HTTP) und der MCP-Server führt Operationen aus und liefert Ergebnisse zurück. Das folgende Diagramm zerlegt jede Schicht dieses Protokolls:

<img src="../../../translated_images/de/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protokolldetails" width="800"/>

*Wie MCP im Hintergrund funktioniert — Clients entdecken Werkzeuge, tauschen JSON-RPC-Nachrichten aus und führen Operationen über eine Transportschicht aus.*

**Server-Client-Architektur**

MCP verwendet ein Client-Server-Modell. Server bieten Werkzeuge an – Datei lesen, Datenbanken abfragen, APIs aufrufen. Clients (Ihre KI-Anwendung) verbinden sich mit den Servern und nutzen deren Werkzeuge.

Um MCP mit LangChain4j zu verwenden, fügen Sie diese Maven-Abhängigkeit hinzu:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Werkzeugentdeckung**

Wenn Ihr Client sich mit einem MCP-Server verbindet, fragt er: „Welche Werkzeuge hast du?“ Der Server antwortet mit einer Liste verfügbarer Werkzeuge, jeweils mit Beschreibungen und Parameterschemata. Ihr KI-Agent kann dann anhand der Benutzeranfragen entscheiden, welche Werkzeuge er nutzen möchte. Das folgende Diagramm zeigt diesen Handshake — der Client sendet eine `tools/list`-Anfrage und der Server antwortet mit seinen verfügbaren Werkzeugen inklusive Beschreibungen und Parameterschemata:

<img src="../../../translated_images/de/tool-discovery.07760a8a301a7832.webp" alt="MCP Werkzeugentdeckung" width="800"/>

*Die KI entdeckt die verfügbaren Werkzeuge beim Start — sie weiß jetzt, welche Fähigkeiten vorhanden sind und kann entscheiden, welche sie verwenden möchte.*

**Transportmechanismen**

MCP unterstützt verschiedene Transportmechanismen. Die zwei Optionen sind Stdio (für lokale Subprozess-Kommunikation) und Streamable HTTP (für Remote-Server). Dieses Modul demonstriert den Stdio-Transport:

<img src="../../../translated_images/de/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transportmechanismen" width="800"/>

*MCP-Transportmechanismen: HTTP für Remote-Server, Stdio für lokale Prozesse*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Für lokale Prozesse. Ihre Anwendung startet einen Server als Subprozess und kommuniziert über Standard-Ein-/Ausgabe. Nützlich für Zugriff auf Dateisystem oder Kommandozeilen-Tools.

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

Der Server `@modelcontextprotocol/server-filesystem` stellt folgende Werkzeuge bereit, alle sandboxed auf die von Ihnen angegebenen Verzeichnisse:

| Werkzeuge | Beschreibung |
|------|-------------|
| `read_file` | Den Inhalt einer einzelnen Datei lesen |
| `read_multiple_files` | Mehrere Dateien in einem Aufruf lesen |
| `write_file` | Eine Datei erstellen oder überschreiben |
| `edit_file` | Gezielt Suchen-und-Ersetzen-Bearbeitungen durchführen |
| `list_directory` | Dateien und Verzeichnisse an einem Pfad auflisten |
| `search_files` | Rekursiv nach Dateien suchen, die einem Muster entsprechen |
| `get_file_info` | Dateimetadaten abrufen (Größe, Zeitstempel, Berechtigungen) |
| `create_directory` | Ein Verzeichnis erstellen (inklusive übergeordneter Verzeichnisse) |
| `move_file` | Eine Datei oder ein Verzeichnis verschieben oder umbenennen |

Das folgende Diagramm zeigt, wie Stdio-Transport zur Laufzeit funktioniert — Ihre Java-Anwendung startet den MCP-Server als Kindprozess und sie kommunizieren über stdin/stdout-Pipes, ohne Netzwerk oder HTTP:

<img src="../../../translated_images/de/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transportablauf" width="800"/>

*Stdio-Transport in Aktion — Ihre Anwendung startet den MCP-Server als Kindprozess und kommuniziert über stdin/stdout-Pipes.*

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat aus:** Öffnen Sie [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) und fragen Sie:
> - „Wie funktioniert der Stdio-Transport und wann sollte ich ihn statt HTTP verwenden?“
> - „Wie verwaltet LangChain4j den Lebenszyklus gestarteter MCP-Serverprozesse?“
> - „Welche Sicherheitsaspekte gibt es, wenn KI Zugriff auf das Dateisystem erhält?“

## Das Agentic-Modul

Während MCP standardisierte Werkzeuge bereitstellt, bietet das **agentische Modul** von LangChain4j eine deklarative Möglichkeit, Agenten zu erstellen, die diese Werkzeuge orchestrieren. Die Annotation `@Agent` und `AgenticServices` erlauben es Ihnen, das Verhalten eines Agenten durch Schnittstellen anstelle von imperativem Code zu definieren.

In diesem Modul erkunden Sie das **Supervisor-Agent**-Muster — einen fortgeschrittenen agentischen KI-Ansatz, bei dem ein „Supervisor“ Agent dynamisch entscheidet, welche Unteragenten basierend auf Benutzeranfragen aufgerufen werden. Wir kombinieren beide Konzepte, indem wir einem unserer Unteragenten MCP-gestützte Dateizugriffsfähigkeiten geben.

Um das agentische Modul zu verwenden, fügen Sie diese Maven-Abhängigkeit hinzu:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **Hinweis:** Das Modul `langchain4j-agentic` verwendet eine separate Versionsnummer (`langchain4j.mcp.version`), da es zu einem anderen Zeitpunkt als die Kernbibliotheken von LangChain4j veröffentlicht wird.

> **⚠️ Experimentell:** Das Modul `langchain4j-agentic` ist **experimentell** und kann sich ändern. Die stabile Methode zum Erstellen von KI-Assistenten bleibt `langchain4j-core` mit benutzerdefinierten Werkzeugen (Modul 04).

## Die Beispiele ausführen

### Voraussetzungen

- Abgeschlossenes [Modul 04 - Werkzeuge](../04-tools/README.md) (dieses Modul baut auf Konzepten für benutzerdefinierte Werkzeuge auf und vergleicht sie mit MCP-Werkzeugen)
- `.env`-Datei im Stammverzeichnis mit Azure-Zugangsdaten (erstellt durch `azd up` in Modul 01)
- Java 21+, Maven 3.9+
- Node.js 16+ und npm (für MCP-Server)

> **Hinweis:** Falls Sie Ihre Umgebungsvariablen noch nicht eingerichtet haben, siehe [Modul 01 - Einführung](../01-introduction/README.md) für Bereitstellungsanleitungen (`azd up` erstellt die `.env`-Datei automatisch) oder kopieren Sie `.env.example` als `.env` ins Stammverzeichnis und füllen Sie Ihre Werte aus.

## Schnellstart

**Verwendung mit VS Code:** Einfach mit der rechten Maustaste auf eine Demo-Datei im Explorer klicken und **„Run Java“** wählen, oder die Startkonfigurationen im Run & Debug-Fenster verwenden (stellen Sie zuerst sicher, dass Ihre `.env`-Datei mit Azure-Zugangsdaten konfiguriert ist).

**Verwendung mit Maven:** Alternativ können Sie die Beispiele mit den untenstehenden Befehlen in der Kommandozeile ausführen.

### Dateioperationen (Stdio)

Dies demonstriert lokal subprocess-basierte Werkzeuge.

**✅ Keine Voraussetzungen erforderlich** – der MCP-Server wird automatisch gestartet.

**Verwendung der Start-Skripte (empfohlen):**

Die Start-Skripte laden automatisch Umgebungsvariablen aus der `.env`-Datei im Stammverzeichnis:

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

**Verwendung mit VS Code:** Rechtsklick auf `StdioTransportDemo.java` und **„Run Java“** wählen (stellen Sie sicher, dass Ihre `.env`-Datei konfiguriert ist).

Die Anwendung startet automatisch einen MCP-Server für das Dateisystem und liest eine lokale Datei. Beachten Sie, wie die Subprozessverwaltung für Sie übernommen wird.

**Erwartete Ausgabe:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor-Agent

Das **Supervisor-Agent-Muster** ist eine **flexible** Form von agentischer KI. Ein Supervisor verwendet ein LLM, um autonom zu entscheiden, welche Agenten basierend auf der Benutzeranfrage aufgerufen werden. Im nächsten Beispiel kombinieren wir MCP-gestützten Dateizugriff mit einem LLM-Agenten, um einen überwachten Workflow Datei lesen → Bericht erzeugen zu erstellen.

In der Demo liest `FileAgent` eine Datei mit MCP-Dateisystem-Werkzeugen, und `ReportAgent` generiert einen strukturierten Bericht mit einem Executive Summary (1 Satz), 3 Schlüsselpunkten und Empfehlungen. Der Supervisor orchestriert diesen Ablauf automatisch:

<img src="../../../translated_images/de/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Muster" width="800"/>

*Der Supervisor verwendet sein LLM, um zu entscheiden, welche Agenten aufgerufen werden und in welcher Reihenfolge — keine fest kodierte Steuerung nötig.*

So sieht der konkrete Workflow für unsere Pipeline von Datei zu Bericht aus:

<img src="../../../translated_images/de/file-report-workflow.649bb7a896800de9.webp" alt="Datei zu Bericht Workflow" width="800"/>

*FileAgent liest die Datei über MCP-Werkzeuge, dann verwandelt ReportAgent den rohen Inhalt in einen strukturierten Bericht.*

Jeder Agent speichert seine Ausgabe im **Agentic Scope** (gemeinsamer Speicher), sodass nachgelagerte Agenten auf frühere Ergebnisse zugreifen können. Dies zeigt, wie MCP-Werkzeuge nahtlos in agentische Workflows integriert werden — der Supervisor muss nicht wissen, *wie* Dateien gelesen werden, nur, dass `FileAgent` es kann.

#### Demo ausführen

Die Start-Skripte laden automatisch Umgebungsvariablen aus der `.env`-Datei im Stammverzeichnis:

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

**Verwendung mit VS Code:** Rechtsklick auf `SupervisorAgentDemo.java` und **„Run Java“** wählen (stellen Sie sicher, dass Ihre `.env`-Datei konfiguriert ist).

#### Wie der Supervisor funktioniert

Bevor Sie Agenten bauen, müssen Sie den MCP-Transport an einen Client anschließen und ihn als `ToolProvider` einhüllen. So werden die Werkzeuge des MCP-Servers für Ihre Agenten verfügbar:

```java
// Erstelle einen MCP-Client aus dem Transport
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Verpacke den Client als ToolProvider — dies verbindet MCP-Werkzeuge mit LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Nun können Sie `mcpToolProvider` in jeden Agenten injizieren, der MCP-Werkzeuge benötigt:

```java
// Schritt 1: FileAgent liest Dateien mit MCP-Tools
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Hat MCP-Tools für Dateioperationen
        .build();

// Schritt 2: ReportAgent erzeugt strukturierte Berichte
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor steuert den Datei → Bericht Arbeitsablauf
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Gibt den endgültigen Bericht zurück
        .build();

// Der Supervisor entscheidet, welche Agenten basierend auf der Anfrage aufgerufen werden sollen
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Antwortstrategien

Wenn Sie einen `SupervisorAgent` konfigurieren, geben Sie an, wie die finale Antwort an den Benutzer formuliert werden soll, nachdem die Unteragenten ihre Aufgaben erledigt haben. Das folgende Diagramm zeigt die drei verfügbaren Strategien — LAST gibt die Ausgabe des letzten Agenten direkt zurück, SUMMARY fasst alle Ausgaben über ein LLM zusammen, und SCORED wählt jene Ausgabe aus, die gegen die ursprüngliche Anfrage besser bewertet wird:

<img src="../../../translated_images/de/response-strategies.3d0cea19d096bdf9.webp" alt="Antwortstrategien" width="800"/>

*Drei Strategien, wie der Supervisor seine finale Antwort formuliert — wählen Sie je nach Wunsch die Ausgabe des letzten Agenten, eine synthetisierte Zusammenfassung oder die am besten bewertete Option.*

Verfügbare Strategien sind:

| Strategie | Beschreibung |
|----------|-------------|
| **LAST** | Der Supervisor gibt die Ausgabe des letzten aufgerufenen Unteragenten oder Werkzeugs zurück. Dies ist nützlich, wenn der finale Agent im Workflow speziell dafür konzipiert ist, die komplette, abschließende Antwort zu liefern (z.B. ein "Summary Agent" in einer Forschungs-Pipeline). |
| **SUMMARY** | Der Supervisor verwendet sein internes Sprachmodell (LLM), um eine Zusammenfassung der gesamten Interaktion und aller Unteragenten-Ausgaben zu erstellen, und gibt diese Zusammenfassung als finale Antwort zurück. Dies liefert eine klare, aggregierte Antwort für den Benutzer. |
| **SCORED** | Das System bewertet mit einem internen LLM sowohl die LAST-Antwort als auch die SUMMARY der Interaktion in Bezug auf die ursprüngliche Benutzeranfrage und gibt jene Ausgabe zurück, die die höhere Bewertung erhält. |
Siehe [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) für die vollständige Implementierung.

> **🤖 Probieren Sie Chat mit [GitHub Copilot](https://github.com/features/copilot) aus:** Öffnen Sie [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) und fragen Sie:
> - "Wie entscheidet der Supervisor, welche Agenten aufgerufen werden?"
> - "Was ist der Unterschied zwischen Supervisor- und Sequential-Workflow-Mustern?"
> - "Wie kann ich das Planungsverhalten des Supervisors anpassen?"

#### Verständnis der Ausgabe

Wenn Sie die Demo ausführen, sehen Sie eine strukturierte Schritt-für-Schritt-Darstellung, wie der Supervisor mehrere Agenten orchestriert. Hier ist, was jeder Abschnitt bedeutet:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Der Header** führt in das Workflow-Konzept ein: eine fokussierte Pipeline vom Dateilesen bis zur Berichtserstellung.

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
- **FileAgent** liest Dateien mit MCP-Tools und speichert den rohen Inhalt in `fileContent`
- **ReportAgent** verarbeitet diesen Inhalt und erzeugt einen strukturierten Bericht in `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Benutzeranfrage** zeigt die Aufgabe. Der Supervisor analysiert diese und entscheidet, FileAgent → ReportAgent auszuführen.

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

**Supervisor-Orchestrierung** zeigt den 2-schrittigen Ablauf in Aktion:
1. **FileAgent** liest die Datei über MCP und speichert den Inhalt
2. **ReportAgent** erhält den Inhalt und erzeugt einen strukturierten Bericht

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

#### Erklärung der Funktionen des Agentic-Moduls

Das Beispiel zeigt mehrere fortgeschrittene Funktionen des Agentic-Moduls. Sehen wir uns Agentic Scope und Agent Listeners genauer an.

**Agentic Scope** zeigt den gemeinsamen Speicher, in dem Agenten ihre Ergebnisse mit `@Agent(outputKey="...")` speichern. Dies ermöglicht:
- Dass spätere Agenten auf die Ausgaben früherer Agenten zugreifen können
- Dem Supervisor, eine endgültige Antwort zu aggregieren
- Ihnen, zu überprüfen, was jeder Agent produziert hat

Das folgende Diagramm zeigt, wie Agentic Scope als gemeinsamer Speicher im Datei-zu-Bericht-Workflow funktioniert — FileAgent schreibt seine Ausgabe unter dem Schlüssel `fileContent`, ReportAgent liest diese und schreibt seine eigene Ausgabe unter `report`:

<img src="../../../translated_images/de/agentic-scope.95ef488b6c1d02ef.webp" alt="Gemeinsamer Speicher des Agentic Scope" width="800"/>

*Agentic Scope fungiert als gemeinsamer Speicher — FileAgent schreibt `fileContent`, ReportAgent liest es und schreibt `report`, und Ihr Code liest das Endergebnis.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Rohdateidaten vom FileAgent
String report = scope.readState("report");            // Strukturierter Bericht vom ReportAgent
```

**Agent Listeners** ermöglichen Überwachung und Debugging der Agentenausführung. Die schrittweise Ausgabe, die Sie in der Demo sehen, stammt von einem AgentListener, der sich in jeden Agentenaufruf einklinkt:
- **beforeAgentInvocation** - Wird aufgerufen, wenn der Supervisor einen Agenten auswählt, damit Sie sehen, welcher Agent ausgewählt wurde und warum
- **afterAgentInvocation** - Wird aufgerufen, wenn ein Agent seine Arbeit beendet hat und zeigt dessen Ergebnis
- **inheritedBySubagents** - Wenn true, überwacht der Listener alle Agenten in der Hierarchie

Das folgende Diagramm zeigt den vollständigen Lebenszyklus eines Agent Listeners, einschließlich wie `onError` Fehler während der Agentenausführung behandelt:

<img src="../../../translated_images/de/agent-listeners.784bfc403c80ea13.webp" alt="Lebenszyklus von Agent Listeners" width="800"/>

*Agent Listeners greifen in den Ausführungslebenszyklus ein — überwachen, wann Agenten starten, abschließen oder Fehler auftreten.*

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

Neben dem Supervisor-Muster bietet das `langchain4j-agentic` Modul mehrere mächtige Workflow-Muster. Das folgende Diagramm zeigt alle fünf — von einfachen sequentiellen Pipelines bis hin zu menschlichen Genehmigungs-Workflows:

<img src="../../../translated_images/de/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow-Muster" width="800"/>

*Fünf Workflow-Muster zur Orchestrierung von Agenten — von einfachen sequentiellen Pipelines bis hin zu menschlichen Genehmigungs-Workflows.*

| Muster | Beschreibung | Anwendungsfall |
|---------|-------------|----------|
| **Sequential** | Agenten nacheinander ausführen, Ausgabe fließt zum nächsten | Pipelines: recherchieren → analysieren → berichten |
| **Parallel** | Agenten gleichzeitig ausführen | Unabhängige Aufgaben: Wetter + Nachrichten + Aktien |
| **Loop** | Iterieren, bis Bedingung erfüllt ist | Qualitätsbewertung: verfeinern bis Score ≥ 0,8 |
| **Conditional** | Routing basierend auf Bedingungen | Klassifizierung → an Spezialagent weiterleiten |
| **Human-in-the-Loop** | Menschliche Kontrollpunkte hinzufügen | Genehmigungsworkflows, Inhaltsprüfung |

## Schlüsselkonzepte

Nachdem Sie MCP und das Agentic-Modul in Aktion kennengelernt haben, fassen wir zusammen, wann Sie welche Methode verwenden sollten.

Ein großer Vorteil von MCP ist sein wachsendes Ökosystem. Das folgende Diagramm zeigt, wie ein einzelnes universelles Protokoll Ihre KI-Anwendung mit einer großen Vielfalt von MCP-Servern verbindet — von Dateisystem- und Datenbankzugriff über GitHub, E-Mail, Web-Scraping und mehr:

<img src="../../../translated_images/de/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP-Ökosystem" width="800"/>

*MCP schafft ein Ökosystem mit universellem Protokoll — jeder MCP-kompatible Server funktioniert mit jedem MCP-kompatiblen Client und ermöglicht so Tool-Sharing zwischen Anwendungen.*

**MCP** ist ideal, wenn Sie bestehende Tool-Ökosysteme nutzen wollen, Tools bauen, die mehrere Anwendungen teilen können, Drittdienste mit Standardprotokollen integrieren möchten oder Tool-Implementierungen austauschen möchten, ohne Code zu ändern.

**Das Agentic-Modul** eignet sich am besten, wenn Sie deklarative Agenten-Definitionen mit `@Agent` Annotationen wünschen, Workflow-Orchestrierung (sequentiell, Schleife, parallel) benötigen, eine designorientierte Agentenentwicklung per Interface anstelle von imperativem Code bevorzugen oder mehrere Agenten kombinieren, die Ausgaben über `outputKey` teilen.

**Das Supervisor-Agent-Muster** ist hervorragend geeignet, wenn der Workflow nicht vorhersagbar ist und Sie möchten, dass das LLM entscheidet, wenn Sie mehrere spezialisierte Agenten haben, die dynamisch orchestriert werden müssen, beim Aufbau von konversationellen Systemen mit verschiedenen Fähigkeiten oder wenn Sie das flexibelste, anpassungsfähigste Agentenverhalten wollen.

Zur Entscheidung zwischen den benutzerdefinierten `@Tool`-Methoden aus Modul 04 und MCP-Tools aus diesem Modul zeigt der folgende Vergleich die wichtigsten Vor- und Nachteile auf — benutzerdefinierte Werkzeuge bieten enge Kopplung und volle Typsicherheit für anwendungsspezifische Logik, MCP-Tools standardisierte, wiederverwendbare Integrationen:

<img src="../../../translated_images/de/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Benutzerdefinierte Tools vs MCP-Tools" width="800"/>

*Wann Sie benutzerdefinierte @Tool-Methoden vs MCP-Tools verwenden sollten — benutzerdefinierte Tools für anwendungsspezifische Logik mit voller Typsicherheit, MCP-Tools für standardisierte Integrationen, die über Anwendungen hinweg funktionieren.*

## Herzlichen Glückwunsch!

Sie haben alle fünf Module des LangChain4j for Beginners-Kurses erfolgreich durchlaufen! Hier ist ein Überblick über Ihre gesamte Lernreise — von einfachem Chat bis hin zu MCP-gestützten agentischen Systemen:

<img src="../../../translated_images/de/course-completion.48cd201f60ac7570.webp" alt="Kursabschluss" width="800"/>

*Ihre Lernreise durch alle fünf Module — vom Basic Chat bis zu MCP-gestützten agentischen Systemen.*

Sie haben den LangChain4j for Beginners-Kurs abgeschlossen und gelernt:

- Wie man konversationales KI mit Speicher aufbaut (Modul 01)
- Prompt Engineerings Muster für verschiedene Aufgaben (Modul 02)
- Antworten mit eigenen Dokumenten durch RAG verankern (Modul 03)
- Basis-KI-Agenten (Assistenten) mit benutzerdefinierten Tools erstellen (Modul 04)
- Standardisierte Tools mit LangChain4j MCP- und Agentic-Modulen integrieren (Modul 05)

### Wie geht es weiter?

Nach Abschluss der Module sehen Sie sich den [Testing Guide](../docs/TESTING.md) an, um LangChain4j-Testkonzepte in Aktion zu erleben.

**Offizielle Ressourcen:**
- [LangChain4j Dokumentation](https://docs.langchain4j.dev/) – Umfassende Anleitungen und API-Referenz
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) – Quellcode und Beispiele
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) – Schritt-für-Schritt-Anleitungen für verschiedene Einsatzszenarien

Vielen Dank, dass Sie diesen Kurs abgeschlossen haben!

---

**Navigation:** [← Zurück: Modul 04 - Tools](../04-tools/README.md) | [Zurück zur Übersicht](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir auf Genauigkeit achten, können automatische Übersetzungen Fehler oder Ungenauigkeiten enthalten. Das Originaldokument in der Originalsprache gilt als maßgebliche Quelle. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die sich aus der Nutzung dieser Übersetzung ergeben.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->