# Modul 05: Model Context Protocol (MCP)

## Inhaltsverzeichnis

- [Was Sie lernen werden](../../../05-mcp)
- [Was ist MCP?](../../../05-mcp)
- [Wie MCP funktioniert](../../../05-mcp)
- [Das agentische Modul](../../../05-mcp)
- [Die Beispiele ausführen](../../../05-mcp)
  - [Voraussetzungen](../../../05-mcp)
- [Schnellstart](../../../05-mcp)
  - [Dateioperationen (Stdio)](../../../05-mcp)
  - [Supervisor-Agent](../../../05-mcp)
    - [Demo ausführen](../../../05-mcp)
    - [Wie der Supervisor funktioniert](../../../05-mcp)
    - [Wie FileAgent MCP-Tools zur Laufzeit entdeckt](../../../05-mcp)
    - [Antwortstrategien](../../../05-mcp)
    - [Das Ergebnis verstehen](../../../05-mcp)
    - [Erklärung der Features des agentischen Moduls](../../../05-mcp)
- [Wichtige Konzepte](../../../05-mcp)
- [Glückwunsch!](../../../05-mcp)
  - [Wie geht es weiter?](../../../05-mcp)

## Was Sie lernen werden

Sie haben konversationelle KI gebaut, Beherrschung von Prompts erlangt, Antworten in Dokumenten verankert und Agenten mit Tools erstellt. Aber all diese Tools wurden speziell für Ihre Anwendung entwickelt. Was wäre, wenn Sie Ihrer KI Zugriff auf ein standardisiertes Ökosystem von Tools geben könnten, die jeder erstellen und teilen kann? In diesem Modul lernen Sie genau das mit dem Model Context Protocol (MCP) und dem agentischen Modul von LangChain4j. Wir zeigen zunächst einen einfachen MCP-Dateileser und danach, wie er mühelos in komplexe agentische Workflows mittels des Supervisor-Agent-Musters integriert wird.

## Was ist MCP?

Das Model Context Protocol (MCP) bietet genau das – eine standardisierte Methode für KI-Anwendungen, externe Tools zu entdecken und zu nutzen. Anstatt für jede Datenquelle oder jeden Dienst individuelle Integrationen zu schreiben, verbinden Sie sich mit MCP-Servern, die ihre Fähigkeiten in einem einheitlichen Format bereitstellen. Ihr KI-Agent kann diese Tools dann automatisch entdecken und verwenden.

Das folgende Diagramm zeigt den Unterschied — ohne MCP erfordert jede Integration punktuelle, maßgeschneiderte Verkabelung; mit MCP verbindet ein einziges Protokoll Ihre App mit jedem Tool:

<img src="../../../translated_images/de/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Vor MCP: Komplexe Punkt-zu-Punkt-Integrationen. Nach MCP: Ein Protokoll, unendliche Möglichkeiten.*

MCP löst ein fundamentales Problem in der KI-Entwicklung: Jede Integration ist individuell. Möchten Sie auf GitHub zugreifen? Individueller Code. Möchten Sie Dateien lesen? Individueller Code. Möchten Sie eine Datenbank abfragen? Individueller Code. Und keine dieser Integrationen funktioniert mit anderen KI-Anwendungen.

MCP standardisiert das. Ein MCP-Server stellt Tools mit klaren Beschreibungen und Schemata bereit. Jeder MCP-Client kann sich verbinden, verfügbare Tools entdecken und verwenden. Einmal bauen, überall nutzen.

Das folgende Diagramm illustriert diese Architektur – ein einzelner MCP-Client (Ihre KI-Anwendung) verbindet sich mit mehreren MCP-Servern, die jeweils ihre eigenen Tools über das Standardprotokoll bereitstellen:

<img src="../../../translated_images/de/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol-Architektur – standardisierte Tool-Erkennung und -Ausführung*

## Wie MCP funktioniert

Im Hintergrund verwendet MCP eine geschichtete Architektur. Ihre Java-Anwendung (der MCP-Client) entdeckt verfügbare Tools, sendet JSON-RPC-Anfragen über eine Transportschicht (Stdio oder HTTP), und der MCP-Server führt die Operationen aus und liefert Ergebnisse zurück. Das folgende Diagramm zeigt jede Schicht dieses Protokolls im Detail:

<img src="../../../translated_images/de/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Wie MCP im Hintergrund funktioniert — Clients entdecken Tools, tauschen JSON-RPC-Nachrichten aus und führen Operationen über eine Transportschicht aus.*

**Server-Client-Architektur**

MCP verwendet ein Client-Server-Modell. Server stellen Tools bereit – Dateien lesen, Datenbanken abfragen, APIs aufrufen. Clients (Ihre KI-Anwendung) verbinden sich zu den Servern und verwenden deren Tools.

Um MCP mit LangChain4j zu verwenden, fügen Sie diese Maven-Abhängigkeit hinzu:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Tool-Erkennung**

Wenn sich Ihr Client mit einem MCP-Server verbindet, fragt er: „Welche Tools hast du?“ Der Server antwortet mit einer Liste verfügbarer Tools, jeweils mit Beschreibungen und Parameterschemata. Ihr KI-Agent kann dann entscheiden, welche Tools basierend auf Benutzeranfragen verwendet werden sollen. Das folgende Diagramm zeigt diesen Handshake — der Client sendet eine `tools/list`-Anfrage und der Server liefert seine verfügbaren Tools mit Beschreibungen und Parameterschemata zurück:

<img src="../../../translated_images/de/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*Die KI entdeckt verfügbare Tools beim Start — jetzt weiß sie, welche Fähigkeiten vorhanden sind, und kann entscheiden, welche sie nutzt.*

**Transportmechanismen**

MCP unterstützt verschiedene Transportmechanismen. Die beiden Optionen sind Stdio (für lokale Subprozess-Kommunikation) und Streamable HTTP (für Remote-Server). Dieses Modul demonstriert den Stdio-Transport:

<img src="../../../translated_images/de/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP-Transportmechanismen: HTTP für Remote-Server, Stdio für lokale Prozesse*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Für lokale Prozesse. Ihre Anwendung startet einen Server als Subprozess und kommuniziert über Standard-Ein-/Ausgabe. Nützlich für Dateisystemzugriff oder Kommandozeilentools.

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

Der `@modelcontextprotocol/server-filesystem`-Server stellt die folgenden Tools bereit, alle auf die von Ihnen angegebenen Verzeichnisse begrenzt:

| Tool | Beschreibung |
|------|--------------|
| `read_file` | Inhalt einer einzelnen Datei lesen |
| `read_multiple_files` | Mehrere Dateien in einem Aufruf lesen |
| `write_file` | Datei erstellen oder überschreiben |
| `edit_file` | Gezielt Finden-und-Ersetzen-Bearbeitungen durchführen |
| `list_directory` | Dateien und Verzeichnisse an einem Pfad auflisten |
| `search_files` | Rekursiv nach Dateien suchen, die einem Muster entsprechen |
| `get_file_info` | Dateimetadaten abrufen (Größe, Zeitstempel, Berechtigungen) |
| `create_directory` | Verzeichnis erstellen (inklusive Elternverzeichnisse) |
| `move_file` | Datei oder Verzeichnis verschieben oder umbenennen |

Das folgende Diagramm zeigt, wie Stdio-Transport zur Laufzeit funktioniert — Ihre Java-Anwendung startet den MCP-Server als Kindprozess und beide kommunizieren über stdin/stdout-Pipes, ohne Netzwerk oder HTTP:

<img src="../../../translated_images/de/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio-Transport in Aktion — Ihre Anwendung startet den MCP-Server als Kindprozess und kommuniziert über stdin/stdout-Pipes.*

> **🤖 Probieren Sie es mit dem [GitHub Copilot](https://github.com/features/copilot) Chat aus:** Öffnen Sie [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) und fragen Sie:
> - "Wie funktioniert Stdio-Transport und wann sollte ich ihn im Vergleich zu HTTP verwenden?"
> - "Wie verwaltet LangChain4j den Lebenszyklus von gestarteten MCP-Serverprozessen?"
> - "Welche Sicherheitsimplikationen hat es, der KI Zugriff auf das Dateisystem zu geben?"

## Das agentische Modul

Während MCP standardisierte Tools bereitstellt, bietet das **agentische Modul** von LangChain4j eine deklarative Möglichkeit, Agenten zu bauen, die diese Tools orchestrieren. Die Annotation `@Agent` und `AgenticServices` ermöglichen es Ihnen, das Verhalten von Agenten durch Schnittstellen statt durch imperativen Code zu definieren.

In diesem Modul erkunden Sie das **Supervisor-Agent**-Muster — einen fortgeschrittenen agentischen KI-Ansatz, bei dem ein „Supervisor“-Agent dynamisch entscheidet, welche Unteragenten basierend auf Benutzeranfragen aufgerufen werden. Wir kombinieren beide Konzepte und geben einem unserer Unteragenten MCP-basierte Dateizugriffsfähigkeiten.

Um das agentische Modul zu verwenden, fügen Sie diese Maven-Abhängigkeit hinzu:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Hinweis:** Das `langchain4j-agentic` Modul verwendet eine eigene Versions-Eigenschaft (`langchain4j.mcp.version`), da es nach einem anderen Zeitplan als die Kernbibliotheken von LangChain4j veröffentlicht wird.

> **⚠️ Experimentell:** Das `langchain4j-agentic` Modul ist **experimentell** und Änderungen unterworfen. Der stabile Weg, KI-Assistenten zu bauen, bleibt weiterhin `langchain4j-core` mit benutzerdefinierten Tools (Modul 04).

## Die Beispiele ausführen

### Voraussetzungen

- Abgeschlossenes [Modul 04 - Tools](../04-tools/README.md) (dieses Modul baut auf den Konzepten benutzerdefinierter Tools auf und vergleicht sie mit MCP-Tools)
- `.env`-Datei im Stammverzeichnis mit Azure-Zugangsdaten (erstellt durch `azd up` im Modul 01)
- Java 21+, Maven 3.9+
- Node.js 16+ und npm (für MCP-Server)

> **Hinweis:** Falls Sie Ihre Umgebungsvariablen noch nicht eingerichtet haben, sehen Sie in [Modul 01 - Einführung](../01-introduction/README.md) die Bereitstellungsanleitung (`azd up` erstellt die `.env`-Datei automatisch), oder kopieren Sie `.env.example` nach `.env` im Stammverzeichnis und füllen Sie Ihre Werte ein.

## Schnellstart

**Mit VS Code:** Klicken Sie einfach mit der rechten Maustaste auf eine beliebige Demo-Datei im Explorer und wählen Sie **„Run Java“** oder verwenden Sie die Startkonfigurationen im Ausführen- und Debuggen-Panel (stellen Sie sicher, dass Ihre `.env`-Datei mit Azure-Zugangsdaten konfiguriert ist).

**Mit Maven:** Alternativ können Sie die Beispiele auch von der Kommandozeile aus ausführen.

### Dateioperationen (Stdio)

Dies demonstriert lokal subprocess-basierte Tools.

**✅ Keine Voraussetzungen nötig** – der MCP-Server wird automatisch gestartet.

**Mit den Start-Skripten (empfohlen):**

Die Start-Skripte laden automatisch die Umgebungsvariablen aus der `.env`-Datei im Stammverzeichnis:

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

**Mit VS Code:** Rechtsklicken Sie auf `StdioTransportDemo.java` und wählen Sie **„Run Java“** (stellen Sie sicher, dass Ihre `.env`-Datei konfiguriert ist).

Die Anwendung startet automatisch einen MCP-Server für das Dateisystem und liest eine lokale Datei. Beachten Sie, wie die Verwaltung des Subprozesses für Sie übernommen wird.

**Erwartete Ausgabe:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor-Agent

Das **Supervisor-Agent-Muster** ist eine **flexible** Form von agentischer KI. Ein Supervisor verwendet ein LLM, um autonom zu entscheiden, welche Agenten basierend auf der Benutzeranfrage aufgerufen werden. Im nächsten Beispiel kombinieren wir MCP-basierten Dateizugriff mit einem LLM-Agenten, um einen überwachten Datei-lesen → Bericht-Erstellungs-Workflow zu erzeugen.

Im Demo liest `FileAgent` eine Datei mit MCP-Dateisystemtools, und `ReportAgent` erstellt einen strukturierten Bericht mit einer Zusammenfassung (1 Satz), 3 Schlüsselpunkten und Empfehlungen. Der Supervisor orchestriert diesen Ablauf automatisch:

<img src="../../../translated_images/de/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Der Supervisor verwendet sein LLM, um zu entscheiden, welche Agenten in welcher Reihenfolge aufgerufen werden — keine harte Kodierung der Weiterleitung nötig.*

So sieht der konkrete Workflow für unsere Datei-zu-Bericht-Pipeline aus:

<img src="../../../translated_images/de/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent liest die Datei über MCP-Tools, dann wandelt ReportAgent den Rohinhalt in einen strukturierten Bericht um.*

Das folgende Sequenzdiagramm verfolgt die vollständige Supervisor-Orchestrierung — vom Starten des MCP-Servers, über die autonome Agentenauswahl des Supervisors, bis zu den Tool-Aufrufen über stdio und dem finalen Bericht:

<img src="../../../translated_images/de/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*Der Supervisor ruft autonom FileAgent auf (der den MCP-Server über stdio kontaktiert, um die Datei zu lesen), dann ruft er ReportAgent auf, um einen strukturierten Bericht zu generieren — jeder Agent speichert seine Ausgabe im gemeinsamen Agentic Scope.*

Jeder Agent speichert seine Ausgabe im **Agentic Scope** (gemeinsamer Speicher), sodass nachgelagerte Agenten auf vorherige Ergebnisse zugreifen können. Das zeigt, wie MCP-Tools nahtlos in agentische Workflows integriert werden — der Supervisor muss nicht wissen, *wie* Dateien gelesen werden, nur, dass `FileAgent` es kann.

#### Demo ausführen

Die Start-Skripte laden automatisch die Umgebungsvariablen aus der `.env`-Datei im Stammverzeichnis:

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

**Mit VS Code:** Rechtsklicken Sie auf `SupervisorAgentDemo.java` und wählen Sie **„Run Java“** (stellen Sie sicher, dass Ihre `.env`-Datei konfiguriert ist).

#### Wie der Supervisor funktioniert

Bevor Sie Agenten bauen, müssen Sie den MCP-Transport mit einem Client verbinden und ihn als `ToolProvider` verpacken. So werden die Tools des MCP-Servers für Ihre Agenten verfügbar:

```java
// Erstellen Sie einen MCP-Client aus dem Transport
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Wickeln Sie den Client als ToolProvider ein — dies verbindet MCP-Tools mit LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Jetzt können Sie `mcpToolProvider` in jedem Agenten injizieren, der MCP-Tools benötigt:

```java
// Schritt 1: FileAgent liest Dateien mit MCP-Tools
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Verfügt über MCP-Tools für Dateioperationen
        .build();

// Schritt 2: ReportAgent erstellt strukturierte Berichte
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor steuert den Datei → Bericht Ablauf
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Gibt den abschließenden Bericht zurück
        .build();

// Der Supervisor entscheidet, welche Agenten basierend auf der Anfrage aufgerufen werden
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Wie FileAgent MCP-Tools zur Laufzeit entdeckt

Sie fragen sich vielleicht: **Wie weiß `FileAgent`, wie es die npm-Dateisystemtools verwenden soll?** Die Antwort ist: Es weiß es nicht — die **LLM** findet es zur Laufzeit anhand von Tool-Schemata heraus.

Die `FileAgent`-Schnittstelle ist lediglich eine **Prompt-Definition**. Sie hat kein vordefiniertes Wissen über `read_file`, `list_directory` oder andere MCP-Tools. So läuft der End-to-End-Prozess ab:
1. **Server startet:** `StdioMcpTransport` startet das npm-Paket `@modelcontextprotocol/server-filesystem` als Kindprozess  
2. **Tool-Erkennung:** Der `McpClient` sendet eine `tools/list` JSON-RPC-Anfrage an den Server, der mit Toolnamen, Beschreibungen und Parameterschemata antwortet (z. B. `read_file` — *"Liest den kompletten Inhalt einer Datei"* — `{ path: string }`)  
3. **Schema-Injektion:** `McpToolProvider` umschließt diese entdeckten Schemata und macht sie LangChain4j verfügbar  
4. **LLM entscheidet:** Wenn `FileAgent.readFile(path)` aufgerufen wird, sendet LangChain4j die Systemnachricht, Benutzernachricht **und die Liste der Tool-Schemata** an das LLM. Das LLM liest die Tool-Beschreibungen und generiert einen Tool-Aufruf (z. B. `read_file(path="/some/file.txt")`)  
5. **Ausführung:** LangChain4j fängt den Tool-Aufruf ab, leitet ihn über den MCP-Client zurück an den Node.js-Subprozess, erhält das Ergebnis und übergibt es zurück an das LLM  

Dies ist derselbe [Tool Discovery](../../../05-mcp) Mechanismus wie oben beschrieben, aber speziell auf den Agenten-Workflow angewendet. Die `@SystemMessage` und `@UserMessage` Annotationen steuern das Verhalten des LLM, während der injizierte `ToolProvider` ihm die **Fähigkeiten** gibt — das LLM verbindet die beiden zur Laufzeit.

> **🤖 Probiere es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffne [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) und frage:  
> - "Wie weiß dieser Agent, welches MCP-Tool aufgerufen werden soll?"  
> - "Was passiert, wenn ich den ToolProvider aus dem Agent Builder entferne?"  
> - "Wie werden Tool-Schemata an das LLM übergeben?"  

#### Antwortstrategien

Wenn du einen `SupervisorAgent` konfigurierst, gibst du an, wie er seine endgültige Antwort an den Nutzer formulieren soll, nachdem die Unteragenten ihre Aufgaben abgeschlossen haben. Das folgende Diagramm zeigt die drei verfügbaren Strategien — LAST gibt die finale Ausgabe des letzten Agenten direkt zurück, SUMMARY fasst alle Ausgaben durch ein LLM zusammen, und SCORED wählt die Antwort mit der höheren Bewertung im Vergleich zur ursprünglichen Anfrage aus:

<img src="../../../translated_images/de/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Drei Strategien, wie der Supervisor seine endgültige Antwort formuliert — wähle basierend darauf, ob du die Ausgabe des letzten Agenten, eine zusammengefasste Synthese oder die beste bewertete Option willst.*

Die verfügbaren Strategien sind:

| Strategie | Beschreibung |
|----------|-------------|
| **LAST** | Der Supervisor gibt die Ausgabe des zuletzt aufgerufenen Unteragenten oder Tools zurück. Dies ist nützlich, wenn der letzte Agent im Workflow speziell darauf ausgelegt ist, die vollständige, finale Antwort zu liefern (z. B. ein „Summary Agent“ in einer Forschungspipeline). |
| **SUMMARY** | Der Supervisor verwendet sein eigenes internes Sprachmodell (LLM), um eine Zusammenfassung der gesamten Interaktion und aller Unteragenten-Ausgaben zu erstellen und gibt diese Zusammenfassung als endgültige Antwort zurück. Dies bietet dem Nutzer eine klare, aggregierte Antwort. |
| **SCORED** | Das System nutzt ein internes LLM, um sowohl die LAST-Antwort als auch die SUMMARY der Interaktion anhand der ursprünglichen Nutzeranfrage zu bewerten und gibt die Ausgabe zurück, die die höhere Bewertung erhält. |

Siehe [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) für die vollständige Implementierung.

> **🤖 Probiere es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffne [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) und frage:  
> - "Wie entscheidet der Supervisor, welche Agenten aufgerufen werden?"  
> - "Was ist der Unterschied zwischen Supervisor und sequenziellen Workflow-Mustern?"  
> - "Wie kann ich das Planungsverhalten des Supervisors anpassen?"  

#### Das Ergebnis verstehen

Wenn du die Demo ausführst, siehst du eine strukturierte Schritt-für-Schritt-Erklärung, wie der Supervisor mehrere Agenten orchestriert. Hier die Bedeutung der einzelnen Abschnitte:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Die Überschrift** führt in das Workflow-Konzept ein: eine fokussierte Pipeline vom Datei-Lesen bis zur Berichtserstellung.

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
  
**Workflow-Diagramm** zeigt den Datenfluss zwischen Agenten. Jeder Agent hat eine spezifische Rolle:  
- **FileAgent** liest Dateien mittels MCP-Tools und speichert den rohen Inhalt in `fileContent`  
- **ReportAgent** verarbeitet diesen Inhalt und erstellt einen strukturierten Bericht in `report`  

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Nutzeranfrage** zeigt die Aufgabe. Der Supervisor analysiert diese und entscheidet, FileAgent → ReportAgent aufzurufen.

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
1. **FileAgent** liest die Datei über MCP und speichert den Inhalt  
2. **ReportAgent** erhält den Inhalt und generiert den strukturierten Bericht  

Der Supervisor traf diese Entscheidungen **autonom** basierend auf der Nutzeranfrage.

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
  
#### Erklärung Agenten-Modul-Funktionen

Das Beispiel zeigt mehrere fortgeschrittene Funktionen des agentischen Moduls. Ein genauerer Blick auf Agentic Scope und Agent Listeners:

**Agentic Scope** zeigt den geteilten Speicher, in dem Agenten ihre Ergebnisse mittels `@Agent(outputKey="...")` speichern. Dies ermöglicht:  
- Späteren Agenten den Zugriff auf Ergebnisse früherer Agenten  
- Dem Supervisor die Synthese einer finalen Antwort  
- Dir die Inspektion dessen, was jeder Agent produziert hat  

Das folgende Diagramm zeigt, wie Agentic Scope als geteilter Speicher im Datei-zu-Bericht-Workflow funktioniert — FileAgent schreibt seine Ausgabe unter dem Schlüssel `fileContent`, ReportAgent liest diese und speichert seine eigene Ausgabe unter `report`:  

<img src="../../../translated_images/de/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope fungiert als Shared Memory — FileAgent schreibt `fileContent`, ReportAgent liest es und schreibt `report`, dein Code liest das Endergebnis.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Rohdateidaten von FileAgent
String report = scope.readState("report");            // Strukturierter Bericht von ReportAgent
```
  
**Agent Listeners** ermöglichen das Überwachen und Debuggen der Agentenausführung. Die Schritt-für-Schritt-Ausgabe, die du in der Demo siehst, stammt von einem AgentListener, der sich in jeden Agentenaufruf einklinkt:  
- **beforeAgentInvocation** – Wird aufgerufen, wenn der Supervisor einen Agenten auswählt, sodass du sehen kannst, welcher Agent warum ausgewählt wurde  
- **afterAgentInvocation** – Wird aufgerufen, wenn ein Agent abgeschlossen ist, um sein Ergebnis anzuzeigen  
- **inheritedBySubagents** – Wenn true, überwacht der Listener alle Agenten in der Hierarchie  

Das folgende Diagramm zeigt den vollständigen Lebenszyklus eines Agent Listener, einschließlich wie `onError` Fehler während der Agentenausführung behandelt:

<img src="../../../translated_images/de/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners sind in den Ausführungslebenszyklus eingebunden — Überwache, wann Agenten starten, abschließen oder Fehler auftauchen.*

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
  
Über das Supervisor-Muster hinaus bietet das `langchain4j-agentic` Modul mehrere leistungsfähige Workflow-Muster. Das folgende Diagramm zeigt alle fünf — von einfachen sequentiellen Pipelines bis hin zu Human-in-the-Loop-Freigabeworkflows:

<img src="../../../translated_images/de/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Fünf Workflow-Muster zur Orchestrierung von Agenten — von einfachen sequentiellen Pipelines bis hin zu Human-in-the-Loop-Freigabeworkflows.*

| Muster | Beschreibung | Anwendungsfall |
|---------|-------------|----------|
| **Sequenziell** | Agenten werden nacheinander ausgeführt, Ausgabe fließt an den nächsten | Pipelines: recherchieren → analysieren → berichten |
| **Parallel** | Agenten werden gleichzeitig ausgeführt | Unabhängige Aufgaben: Wetter + Nachrichten + Aktien |
| **Schleife** | Iteriere bis Bedingung erfüllt ist | Qualitätsbewertung: verfeinern bis Score ≥ 0,8 |
| **Bedingt** | Verzweigung basierend auf Bedingungen | Klassifizieren → zum Spezialisten-Agenten routen |
| **Human-in-the-Loop** | Menschliche Kontrollpunkte hinzufügen | Freigabeworkflows, Inhaltskontrolle |

## Schlüsselkonzepte

Nachdem du MCP und das agentische Modul in Aktion erlebt hast, fassen wir zusammen, wann du welche Methode verwenden solltest.

Einer der größten Vorteile von MCP ist sein wachsendes Ökosystem. Das folgende Diagramm zeigt, wie ein einziges universelles Protokoll deine KI-Anwendung mit einer Vielzahl von MCP-Servern verbindet — von Dateisystem- und Datenbankzugriff über GitHub, E-Mail, Web-Scraping und mehr:

<img src="../../../translated_images/de/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP schafft ein universelles Protokoll-Ökosystem — jeder MCP-kompatible Server funktioniert mit jedem MCP-kompatiblen Client und ermöglicht Tool-Sharing über Anwendungen hinweg.*

**MCP** eignet sich ideal, wenn du bestehende Tool-Ökosysteme nutzen, Tools bauen möchtest, die von mehreren Anwendungen geteilt werden können, Drittanbieter-Services mit standardisierten Protokollen integrieren oder Tool-Implementierungen austauschen willst, ohne Code zu ändern.

**Das agentische Modul** ist am besten, wenn du deklarative Agenten-Definitionen mit `@Agent` Annotationen möchtest, Workflow-Orchestrierung (sequentiell, Schleife, parallel) brauchst, eine Interface-basierte Agentengestaltung gegenüber imperativem Code bevorzugst oder mehrere Agenten kombinierst, die Ausgaben über `outputKey` teilen.

**Das Supervisor-Agent-Muster** ist stark, wenn der Workflow vorher nicht vorhersehbar ist und das LLM entscheiden soll, wenn du mehrere spezialisierte Agenten hast, die dynamisch orchestriert werden müssen, bei konversationellen Systemen, die auf unterschiedliche Fähigkeiten routen, oder wenn du das flexibelste, adaptivste Agentenverhalten möchtest.

Um dir bei der Entscheidung zwischen benutzerdefinierten `@Tool`-Methoden aus Modul 04 und MCP-Tools aus diesem Modul zu helfen, zeigt der folgende Vergleich die wichtigsten Abwägungen — benutzerdefinierte Tools bieten enge Kopplung und volle Typensicherheit für anwendungsspezifische Logik, MCP-Tools bieten standardisierte, wiederverwendbare Integrationen:

<img src="../../../translated_images/de/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Wann benutzerdefinierte @Tool-Methoden oder MCP-Tools genutzt werden sollten — benutzerdefinierte Tools für anwendungsspezifische Logik mit voller Typensicherheit, MCP-Tools für standardisierte Integrationen, die anwendungsübergreifend funktionieren.*

## Herzlichen Glückwunsch!

Du hast alle fünf Module des LangChain4j für Anfänger-Kurses durchlaufen! Hier ist eine Übersicht über den gesamten Lernweg, den du abgeschlossen hast — vom einfachen Chat bis hin zu MCP-betriebenen agentischen Systemen:

<img src="../../../translated_images/de/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Dein Lernweg durch alle fünf Module — vom einfachen Chat bis zu MCP-betriebenen agentischen Systemen.*

Du hast den LangChain4j für Anfänger-Kurs abgeschlossen. Du hast gelernt:

- Wie man konversationelle KI mit Gedächtnis baut (Modul 01)  
- Prompt-Engineering-Muster für verschiedene Aufgaben (Modul 02)  
- Antworten auf deine Dokumente mit RAG verankern (Modul 03)  
- Grundlegende KI-Agenten (Assistenten) mit benutzerdefinierten Tools erstellen (Modul 04)  
- Standardisierte Tools mit den LangChain4j MCP- und Agentic-Modulen integrieren (Modul 05)  

### Was kommt als Nächstes?

Nach Abschluss der Module erkunde den [Testing Guide](../docs/TESTING.md), um Konzepte zum Testen in LangChain4j in Aktion zu sehen.

**Offizielle Ressourcen:**  
- [LangChain4j Dokumentation](https://docs.langchain4j.dev/) – umfassende Anleitungen und API-Referenz  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) – Quellcode und Beispiele  
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) – Schritt-für-Schritt-Anleitungen für verschiedene Anwendungsfälle  

Danke, dass du diesen Kurs abgeschlossen hast!

---

**Navigation:** [← Vorheriges: Modul 04 - Tools](../04-tools/README.md) | [Zurück zur Hauptseite](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir auf Genauigkeit achten, kann es bei automatischen Übersetzungen zu Fehlern oder Ungenauigkeiten kommen. Das Originaldokument in seiner Ursprungssprache ist als maßgebliche Quelle zu betrachten. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Nutzung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->