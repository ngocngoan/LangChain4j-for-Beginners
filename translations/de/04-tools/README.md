# Modul 04: KI-Agenten mit Tools

## Inhaltsverzeichnis

- [Was Sie lernen werden](../../../04-tools)
- [Voraussetzungen](../../../04-tools)
- [Verständnis von KI-Agenten mit Tools](../../../04-tools)
- [Wie Tool-Aufrufe funktionieren](../../../04-tools)
  - [Tool-Definitionen](../../../04-tools)
  - [Entscheidungsfindung](../../../04-tools)
  - [Ausführung](../../../04-tools)
  - [Antwortgenerierung](../../../04-tools)
  - [Architektur: Spring Boot Auto-Wiring](../../../04-tools)
- [Tool-Ketten](../../../04-tools)
- [Anwendung starten](../../../04-tools)
- [Anwendung verwenden](../../../04-tools)
  - [Einfache Tool-Nutzung ausprobieren](../../../04-tools)
  - [Tool-Ketten testen](../../../04-tools)
  - [Gesprächsfluss ansehen](../../../04-tools)
  - [Mit verschiedenen Anfragen experimentieren](../../../04-tools)
- [Wichtige Konzepte](../../../04-tools)
  - [ReAct-Muster (Reasoning and Acting)](../../../04-tools)
  - [Tool-Beschreibungen sind wichtig](../../../04-tools)
  - [Sitzungsverwaltung](../../../04-tools)
  - [Fehlerbehandlung](../../../04-tools)
- [Verfügbare Tools](../../../04-tools)
- [Wann man toolbasierte Agenten verwendet](../../../04-tools)
- [Tools vs RAG](../../../04-tools)
- [Nächste Schritte](../../../04-tools)

## Was Sie lernen werden

Bisher haben Sie gelernt, wie man Gespräche mit KI führt, Eingabeaufforderungen effektiv strukturiert und Antworten in Ihren Dokumenten verankert. Es gibt aber noch eine grundlegende Einschränkung: Sprachmodelle können nur Text generieren. Sie können nicht das Wetter prüfen, Berechnungen durchführen, Datenbanken abfragen oder mit externen Systemen interagieren.

Tools ändern das. Indem Sie dem Modell Zugang zu Funktionen geben, die es aufrufen kann, verwandeln Sie es von einem Textgenerator in einen Agenten, der Aktionen ausführen kann. Das Modell entscheidet, wann es ein Tool braucht, welches Tool es verwendet und welche Parameter es übergibt. Ihr Code führt die Funktion aus und gibt das Ergebnis zurück. Das Modell integriert dieses Ergebnis in seine Antwort.

## Voraussetzungen

- Abgeschlossenes [Modul 01 - Einführung](../01-introduction/README.md) (Azure OpenAI-Ressourcen bereitgestellt)
- Frühere Module abgeschlossen empfohlen (dieses Modul verweist auf [RAG-Konzepte aus Modul 03](../03-rag/README.md) im Vergleich Tools vs RAG)
- `.env`-Datei im Stammverzeichnis mit Azure-Zugangsdaten (erstellt durch `azd up` in Modul 01)

> **Hinweis:** Wenn Sie Modul 01 noch nicht abgeschlossen haben, folgen Sie zuerst den dortigen Bereitstellungsanweisungen.

## Verständnis von KI-Agenten mit Tools

> **📝 Hinweis:** Der Begriff „Agenten“ in diesem Modul bezieht sich auf KI-Assistenten, die mit Tool-Aufruf-Fähigkeiten erweitert sind. Dies unterscheidet sich von den **Agentic AI**-Mustern (autonome Agenten mit Planung, Gedächtnis und mehrstufigem Denken), die wir in [Modul 05: MCP](../05-mcp/README.md) behandeln werden.

Ohne Tools kann ein Sprachmodell nur Text basierend auf seinem Trainingsdatensatz generieren. Fragt man nach dem aktuellen Wetter, muss es raten. Gibt man ihm Tools, kann es eine Wetter-API aufrufen, Berechnungen durchführen oder eine Datenbank abfragen – und diese realen Resultate dann in seine Antwort einfließen lassen.

<img src="../../../translated_images/de/what-are-tools.724e468fc4de64da.webp" alt="Ohne Tools vs Mit Tools" width="800"/>

*Ohne Tools kann das Modell nur raten – mit Tools kann es APIs aufrufen, Berechnungen ausführen und Echtzeitdaten zurückgeben.*

Ein KI-Agent mit Tools folgt einem **Reasoning and Acting (ReAct)**-Muster. Das Modell antwortet nicht nur – es überlegt, was es braucht, handelt durch Aufruf eines Tools, beobachtet das Ergebnis und entscheidet dann, ob es erneut handeln oder die finale Antwort liefern soll:

1. **Reason (Überlegen)** — Der Agent analysiert die Frage des Benutzers und ermittelt, welche Informationen er benötigt
2. **Act (Handeln)** — Der Agent wählt das richtige Tool, erzeugt die korrekten Parameter und ruft es auf
3. **Observe (Beobachten)** — Der Agent erhält die Ausgabe des Tools und bewertet das Ergebnis
4. **Repeat or Respond (Wiederholen oder Antworten)** — Wenn mehr Daten benötigt werden, läuft der Agent erneut; sonst erstellt er eine natürlichsprachliche Antwort

<img src="../../../translated_images/de/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct-Muster" width="800"/>

*Der ReAct-Zyklus — der Agent überlegt, was zu tun ist, handelt durch Tool-Aufruf, beobachtet das Ergebnis und wiederholt dies, bis er die finale Antwort liefern kann.*

Dies geschieht automatisch. Sie definieren die Tools und deren Beschreibungen. Das Modell übernimmt die Entscheidungsfindung darüber, wann und wie diese zu nutzen sind.

## Wie Tool-Aufrufe funktionieren

### Tool-Definitionen

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Sie definieren Funktionen mit klaren Beschreibungen und Parameterspezifikationen. Das Modell sieht diese Beschreibungen in seinem System-Prompt und versteht, was jedes Tool macht.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Ihre Wetterabfrage-Logik
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Assistent wird automatisch von Spring Boot verbunden mit:
// - ChatModel Bean
// - Alle @Tool-Methoden von @Component-Klassen
// - ChatMemoryProvider für Sitzungsverwaltung
```

Das folgende Diagramm erklärt jede Annotation und zeigt, wie jedes Element der KI hilft zu verstehen, wann sie das Tool aufrufen und welche Argumente sie übergeben soll:

<img src="../../../translated_images/de/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomie von Tool-Definitionen" width="800"/>

*Anatomie einer Tool-Definition – @Tool sagt der KI, wann sie es verwenden soll, @P beschreibt jeweils einen Parameter, und @AiService verbindet alles beim Start.*

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) und fragen Sie:
> - „Wie integriere ich eine echte Wetter-API wie OpenWeatherMap statt Mock-Daten?“
> - „Was macht eine gute Tool-Beschreibung aus, die der KI hilft, es richtig zu verwenden?“
> - „Wie gehe ich in der Tool-Implementierung mit API-Fehlern und Ratenbegrenzungen um?“

### Entscheidungsfindung

Wenn ein Nutzer fragt „Wie ist das Wetter in Seattle?“, wählt das Modell kein Tool willkürlich aus. Es vergleicht die Absicht des Nutzers mit jeder verfügbaren Tool-Beschreibung, bewertet deren Relevanz und wählt die beste Übereinstimmung aus. Anschließend generiert es einen strukturierten Funktionsaufruf mit den korrekten Parametern – hier wird `location` auf `"Seattle"` gesetzt.

Wenn kein Tool zur Anfrage passt, greift das Modell auf sein eigenes Wissen zurück. Wenn mehrere Tools passen, wählt es das spezifischste aus.

<img src="../../../translated_images/de/decision-making.409cd562e5cecc49.webp" alt="Wie die KI entscheidet, welches Tool verwendet wird" width="800"/>

*Das Modell bewertet jedes verfügbare Tool im Bezug auf die Nutzerabsicht und wählt das beste aus – deshalb sind klare, spezifische Tool-Beschreibungen wichtig.*

### Ausführung

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot verdrahtet die deklarative `@AiService`-Schnittstelle automatisch mit allen registrierten Tools, und LangChain4j führt Tool-Aufrufe automatisch aus. Im Hintergrund durchläuft ein kompletter Tool-Aufruf sechs Stufen – von der natürlichsprachlichen Frage des Nutzers bis zur natürlichsprachlichen Antwort:

<img src="../../../translated_images/de/tool-calling-flow.8601941b0ca041e6.webp" alt="Flow des Tool-Aufrufs" width="800"/>

*Der durchgehende Ablauf – der Benutzer stellt eine Frage, das Modell wählt ein Tool aus, LangChain4j führt es aus, und das Modell arbeitet das Ergebnis in eine natürliche Antwort ein.*

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) und fragen Sie:
> - „Wie funktioniert das ReAct-Muster und warum ist es effektiv für KI-Agenten?“
> - „Wie entscheidet der Agent, welches Tool in welcher Reihenfolge verwendet wird?“
> - „Was passiert, wenn ein Tool-Aufruf fehlschlägt – wie handhabe ich Fehler robust?“

### Antwortgenerierung

Das Modell erhält die Wetterdaten und formatiert sie zu einer natürlichsprachlichen Antwort für den Benutzer.

### Architektur: Spring Boot Auto-Wiring

Dieses Modul nutzt LangChain4js Integration in Spring Boot mit deklarativen `@AiService`-Schnittstellen. Beim Start entdeckt Spring Boot alle `@Component`s mit `@Tool`-Methoden, Ihre `ChatModel`-Bean und den `ChatMemoryProvider` – und verdrahtet sie alle zu einer einzigen `Assistant`-Schnittstelle ohne Boilerplate.

<img src="../../../translated_images/de/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architektur" width="800"/>

*Die @AiService-Schnittstelle vereint ChatModel, Tool-Komponenten und Memory Provider – Spring Boot übernimmt die gesamte Verdrahtung automatisch.*

Wesentliche Vorteile dieses Ansatzes:

- **Spring Boot Auto-Wiring** — ChatModel und Tools werden automatisch injiziert
- **@MemoryId-Muster** — Automatische Sitzungsbasierte Speicherverwaltung
- **Eine einzige Instanz** — Assistant wird einmal erzeugt und für bessere Performance wiederverwendet
- **Typ-sichere Ausführung** — Java-Methoden werden direkt mit Typkonvertierung aufgerufen
- **Multi-Turn-Orchestrierung** — Handhabt Tool-Ketten automatisch
- **Kein Boilerplate** — Keine manuellen `AiServices.builder()`-Aufrufe oder Memory-HashMaps

Alternative Ansätze (manueller `AiServices.builder()`) erfordern mehr Code und verzichten auf die Vorteile der Spring Boot-Integration.

## Tool-Ketten

**Tool-Ketten** — Die wahre Stärke von toolbasierten Agenten zeigt sich, wenn eine einzelne Frage mehrere Tools erfordert. Fragt man „Wie ist das Wetter in Seattle in Fahrenheit?“, verbindet der Agent automatisch zwei Tools: Zuerst ruft er `getCurrentWeather` auf, um die Temperatur in Celsius zu erhalten, danach übergibt er diesen Wert an `celsiusToFahrenheit` zur Umrechnung – alles in einer einzigen Gesprächsrunde.

<img src="../../../translated_images/de/tool-chaining-example.538203e73d09dd82.webp" alt="Beispiel Tool-Ketten" width="800"/>

*Tool-Ketten in Aktion – der Agent ruft zuerst getCurrentWeather auf, gibt dann das Celsius-Ergebnis an celsiusToFahrenheit weiter und liefert eine kombinierte Antwort.*

**Sanfte Fehlerbehandlung** — Fragt man nach dem Wetter in einer Stadt, die nicht in den Mock-Daten vorkommt, gibt das Tool eine Fehlermeldung zurück, und die KI erklärt, dass sie nicht helfen kann, anstatt abzustürzen. Tools fallen sicher aus. Das folgende Diagramm zeigt den Unterschied der beiden Ansätze – mit korrekter Fehlerbehandlung fängt der Agent die Ausnahme und antwortet hilfreich, ohne Ausnahme stürzt die gesamte Anwendung ab:

<img src="../../../translated_images/de/error-handling-flow.9a330ffc8ee0475c.webp" alt="Fehlerbehandlungs-Fluss" width="800"/>

*Wenn ein Tool fehlschlägt, fängt der Agent den Fehler ab und gibt eine hilfreiche Erklärung, anstatt abzustürzen.*

Dies passiert in einer einzigen Gesprächsrunde. Der Agent orchestriert mehrere Tool-Aufrufe autonom.

## Anwendung starten

**Bereitstellung überprüfen:**

Stellen Sie sicher, dass die `.env`-Datei mit Azure-Zugangsdaten im Stammverzeichnis vorhanden ist (erstellt während Modul 01). Führen Sie dies aus dem Modulverzeichnis (`04-tools/`) aus:

**Bash:**
```bash
cat ../.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```

**Anwendung starten:**

> **Hinweis:** Wenn Sie alle Anwendungen bereits mit `./start-all.sh` aus dem Stammverzeichnis gestartet haben (wie in Modul 01 beschrieben), läuft dieses Modul bereits auf Port 8084. Sie können die Startbefehle unten überspringen und direkt zu http://localhost:8084 gehen.

**Option 1: Verwendung des Spring Boot Dashboards (Empfohlen für VS Code Nutzer)**

Der Dev-Container enthält die Spring Boot Dashboard-Erweiterung, die eine visuelle Oberfläche zur Verwaltung aller Spring Boot Anwendungen bietet. Sie finden sie in der Aktivitätsleiste links in VS Code (Suchen Sie nach dem Spring Boot-Symbol).

Im Spring Boot Dashboard können Sie:
- Alle verfügbaren Spring Boot Anwendungen im Workspace sehen
- Anwendungen mit einem Klick starten/stoppen
- Anwendungsprotokolle in Echtzeit ansehen
- Anwendungsstatus überwachen

Klicken Sie einfach auf den Play-Button neben „tools“, um dieses Modul zu starten, oder starten Sie alle Module gleichzeitig.

So sieht das Spring Boot Dashboard in VS Code aus:

<img src="../../../translated_images/de/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Das Spring Boot Dashboard in VS Code – starten, stoppen und überwachen Sie alle Module an einem Ort*

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
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

Beide Skripte laden automatisch Umgebungsvariablen aus der Datei `.env` im Stammverzeichnis und bauen die JARs, falls diese noch nicht existieren.

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

Öffnen Sie http://localhost:8084 in Ihrem Browser.

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

## Anwendung verwenden

Die Anwendung bietet eine Web-Oberfläche, über die Sie mit einem KI-Agenten interagieren können, der Zugriff auf Wetter- und Temperaturumrechnungstools hat. So sieht die Benutzeroberfläche aus — mit Schnellstart-Beispielen und einem Chat-Panel zum Senden von Anfragen:
<a href="images/tools-homepage.png"><img src="../../../translated_images/de/tools-homepage.4b4cd8b2717f9621.webp" alt="AI-Agenten-Werkzeugoberfläche" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Die AI-Agenten-Werkzeugoberfläche – schnelle Beispiele und Chat-Oberfläche zur Interaktion mit Werkzeugen*

### Einfache Werkzeugverwendung ausprobieren

Beginnen Sie mit einer einfachen Anfrage: „Wandle 100 Grad Fahrenheit in Celsius um“. Der Agent erkennt, dass er das Temperatur-Umwandlungswerkzeug benötigt, ruft es mit den richtigen Parametern auf und gibt das Ergebnis zurück. Beachten Sie, wie natürlich sich das anfühlt – Sie haben nicht angegeben, welches Werkzeug verwendet werden soll oder wie es aufgerufen wird.

### Werkzeugverkettung testen

Probieren Sie nun etwas Komplexeres: „Wie ist das Wetter in Seattle und wandle es in Fahrenheit um?“ Beobachten Sie, wie der Agent dies in Schritten abarbeitet. Zuerst holt er das Wetter (das in Celsius zurückgegeben wird), erkennt, dass eine Umwandlung in Fahrenheit erforderlich ist, ruft das Umwandlungswerkzeug auf und kombiniert beide Ergebnisse zu einer Antwort.

### Verlauf des Gesprächs ansehen

Die Chat-Oberfläche speichert den Gesprächsverlauf, sodass Sie mehrstufige Interaktionen führen können. Sie sehen alle vorherigen Anfragen und Antworten, was es einfach macht, das Gespräch nachzuverfolgen und zu verstehen, wie der Agent Kontext über mehrere Austausche aufbaut.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/de/tools-conversation-demo.89f2ce9676080f59.webp" alt="Gespräch mit mehreren Werkzeugaufrufen" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Mehrstufiges Gespräch mit einfachen Umwandlungen, Wetterabfragen und Werkzeugverkettung*

### Verschiedene Anfragen ausprobieren

Testen Sie verschiedene Kombinationen:
- Wetterabfragen: „Wie ist das Wetter in Tokio?“
- Temperaturumwandlungen: „Wie viel sind 25 °C in Kelvin?“
- Kombinierte Abfragen: „Überprüfe das Wetter in Paris und sag mir, ob es über 20 °C ist“

Beachten Sie, wie der Agent natürliche Sprache interpretiert und auf passende Werkzeugaufrufe abbildet.

## Wichtige Konzepte

### ReAct-Muster (Reasoning and Acting)

Der Agent wechselt zwischen dem Nachdenken (entscheiden, was zu tun ist) und dem Handeln (Werkzeuge verwenden). Dieses Muster ermöglicht autonomes Problemlösen statt nur auf Anweisungen zu reagieren.

### Werkzeugbeschreibungen sind wichtig

Die Qualität Ihrer Werkzeugbeschreibungen beeinflusst direkt, wie gut der Agent sie nutzt. Klare, spezifische Beschreibungen helfen dem Modell zu verstehen, wann und wie jedes Werkzeug aufgerufen wird.

### Sitzungsverwaltung

Die `@MemoryId`-Annotation ermöglicht eine automatische speicherbasierte Sitzungsverwaltung. Jede Sitzungs-ID erhält eine eigene `ChatMemory`-Instanz, die vom `ChatMemoryProvider`-Bean verwaltet wird, so dass mehrere Benutzer gleichzeitig mit dem Agenten interagieren können, ohne dass sich deren Gespräche vermischen. Das folgende Diagramm zeigt, wie mehreren Benutzern basierend auf ihren Sitzungs-IDs isolierte Speicher zugeordnet werden:

<img src="../../../translated_images/de/session-management.91ad819c6c89c400.webp" alt="Sitzungsverwaltung mit @MemoryId" width="800"/>

*Jede Sitzungs-ID wird einem isolierten Gesprächsverlauf zugeordnet – Benutzer sehen niemals die Nachrichten der anderen.*

### Fehlerbehandlung

Werkzeuge können fehlschlagen — APIs können zeitüberschreiten, Parameter sind möglicherweise ungültig, externe Dienste fallen aus. Produktive Agenten benötigen Fehlerbehandlung, damit das Modell Probleme erklären oder Alternativen versuchen kann, anstatt dass die gesamte Anwendung abstürzt. Wenn ein Werkzeug eine Ausnahme auslöst, fängt LangChain4j diese ab und gibt die Fehlermeldung an das Modell weiter, das dann das Problem in natürlicher Sprache erklären kann.

## Verfügbare Werkzeuge

Das folgende Diagramm zeigt das breite Ökosystem an Werkzeugen, die Sie bauen können. Dieses Modul demonstriert Wetter- und Temperaturwerkzeuge, aber das gleiche `@Tool`-Muster funktioniert für jede Java-Methode – von Datenbankabfragen bis Zahlungsabwicklung.

<img src="../../../translated_images/de/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Werkzeug-Ökosystem" width="800"/>

*Jede mit @Tool annotierte Java-Methode wird dem KI-Modell zugänglich – das Muster erstreckt sich auf Datenbanken, APIs, E-Mail, Dateioperationen und mehr.*

## Wann man Werkzeug-basierte Agenten verwendet

Nicht jede Anfrage benötigt Werkzeuge. Die Entscheidung hängt davon ab, ob die KI mit externen Systemen interagieren muss oder aus eigenem Wissen antworten kann. Die folgende Übersicht fasst zusammen, wann Werkzeuge Mehrwert bieten und wann sie unnötig sind:

<img src="../../../translated_images/de/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Wann man Werkzeuge verwendet" width="800"/>

*Eine schnelle Entscheidungsübersicht – Werkzeuge sind für Echtzeitdaten, Berechnungen und Aktionen; allgemeines Wissen und kreative Aufgaben benötigen sie nicht.*

## Werkzeuge vs. RAG

Die Module 03 und 04 erweitern beide die Fähigkeiten der KI, aber auf grundsätzlich unterschiedliche Weise. RAG gibt dem Modell Zugriff auf **Wissen**, indem Dokumente abgerufen werden. Werkzeuge geben dem Modell die Fähigkeit, **Aktionen** durch Funktionsaufrufe auszuführen. Das folgende Diagramm vergleicht diese zwei Ansätze nebeneinander – von Abläufen bis zu den jeweiligen Vor- und Nachteilen:

<img src="../../../translated_images/de/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Vergleich Werkzeuge vs RAG" width="800"/>

*RAG ruft Informationen aus statischen Dokumenten ab – Werkzeuge führen Aktionen aus und holen dynamische, aktuelle Daten. Viele produktive Systeme kombinieren beides.*

In der Praxis kombinieren viele produktive Systeme beide Ansätze: RAG zum Fundieren der Antworten in der Dokumentation und Werkzeuge zum Abrufen von Live-Daten oder Ausführen von Operationen.

## Nächste Schritte

**Nächstes Modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigation:** [← Vorheriges: Modul 03 - RAG](../03-rag/README.md) | [Zurück zur Übersicht](../README.md) | [Nächstes: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Originalsprache ist als maßgebliche Quelle zu betrachten. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Nutzung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->