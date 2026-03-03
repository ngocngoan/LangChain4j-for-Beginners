# Modul 04: KI-Agenten mit Werkzeugen

## Inhaltsverzeichnis

- [Was Sie lernen werden](../../../04-tools)
- [Voraussetzungen](../../../04-tools)
- [Verstehen von KI-Agenten mit Werkzeugen](../../../04-tools)
- [Wie Werkzeugaufrufe funktionieren](../../../04-tools)
  - [Werkzeugdefinitionen](../../../04-tools)
  - [Entscheidungsfindung](../../../04-tools)
  - [Ausführung](../../../04-tools)
  - [Antwortgenerierung](../../../04-tools)
  - [Architektur: Spring Boot Auto-Wiring](../../../04-tools)
- [Werkzeugverkettung](../../../04-tools)
- [Anwendung ausführen](../../../04-tools)
- [Anwendung verwenden](../../../04-tools)
  - [Einfache Werkzeugnutzung ausprobieren](../../../04-tools)
  - [Werkzeugverkettung testen](../../../04-tools)
  - [Konversationsfluss ansehen](../../../04-tools)
  - [Mit verschiedenen Anfragen experimentieren](../../../04-tools)
- [Schlüsselkonzepte](../../../04-tools)
  - [ReAct-Muster (Reasoning and Acting)](../../../04-tools)
  - [Werkzeugbeschreibungen sind wichtig](../../../04-tools)
  - [Sitzungsverwaltung](../../../04-tools)
  - [Fehlerbehandlung](../../../04-tools)
- [Verfügbare Werkzeuge](../../../04-tools)
- [Wann Werkzeug-basierte Agenten einsetzen](../../../04-tools)
- [Werkzeuge vs RAG](../../../04-tools)
- [Nächste Schritte](../../../04-tools)

## Was Sie lernen werden

Bis jetzt haben Sie gelernt, wie man Gespräche mit KI führt, Eingabeaufforderungen effektiv strukturieren und Antworten anhand Ihrer Dokumente fundieren kann. Aber es gibt eine grundlegende Einschränkung: Sprachmodelle können nur Text generieren. Sie können das Wetter nicht prüfen, keine Berechnungen durchführen, keine Datenbanken abfragen und nicht mit externen Systemen interagieren.

Werkzeuge verändern das. Indem man dem Modell Zugriff auf Funktionen gibt, die es aufrufen kann, verwandelt man es von einem reinen Textgenerator in einen Agenten, der handeln kann. Das Modell entscheidet, wann es ein Werkzeug benötigt, welches Werkzeug es benutzt und welche Parameter es übergibt. Ihr Code führt die Funktion aus und liefert das Ergebnis zurück. Das Modell integriert dieses Ergebnis in seine Antwort.

## Voraussetzungen

- Abgeschlossenes [Modul 01 - Einführung](../01-introduction/README.md) (Azure OpenAI Ressourcen bereitgestellt)
- Empfehlenswert: Abgeschlossene vorherige Module (in diesem Modul wird auf [RAG-Konzepte aus Modul 03](../03-rag/README.md) im Vergleich Werkzeuge vs RAG verwiesen)
- `.env`-Datei im Stammverzeichnis mit Azure-Zugangsdaten (erstellt durch `azd up` in Modul 01)

> **Hinweis:** Falls Sie Modul 01 noch nicht abgeschlossen haben, folgen Sie zuerst dort den Bereitstellungsanweisungen.

## Verstehen von KI-Agenten mit Werkzeugen

> **📝 Hinweis:** Der Begriff „Agenten“ in diesem Modul bezieht sich auf KI-Assistenten mit erweiterten Werkzeug-Aufruf-Fähigkeiten. Dies unterscheidet sich von den **Agentic AI**-Mustern (autonome Agenten mit Planung, Gedächtnis und mehrstufigem Schlussfolgern), die wir in [Modul 05: MCP](../05-mcp/README.md) behandeln werden.

Ohne Werkzeuge kann ein Sprachmodell nur Text aus seinen Trainingsdaten generieren. Fragen Sie es nach dem aktuellen Wetter, muss es raten. Geben Sie ihm Werkzeuge, kann es eine Wetter-API aufrufen, Berechnungen durchführen oder eine Datenbank abfragen – und diese realen Ergebnisse in seine Antwort einfließen lassen.

<img src="../../../translated_images/de/what-are-tools.724e468fc4de64da.webp" alt="Ohne Werkzeuge vs Mit Werkzeugen" width="800"/>

*Ohne Werkzeuge kann das Modell nur raten — mit Werkzeugen kann es APIs aufrufen, Berechnungen ausführen und Echtzeitdaten liefern.*

Ein KI-Agent mit Werkzeugen folgt einem **Reasoning and Acting (ReAct)**-Muster. Das Modell reagiert nicht nur — es überlegt, was es braucht, handelt, indem es ein Werkzeug aufruft, beobachtet das Ergebnis und entscheidet dann, ob es erneut handeln oder die finale Antwort geben soll:

1. **Überlegen** — Der Agent analysiert die Frage des Nutzers und ermittelt, welche Informationen er benötigt  
2. **Handeln** — Der Agent wählt das passende Werkzeug aus, erzeugt die korrekten Parameter und ruft es auf  
3. **Beobachten** — Der Agent erhält die Ausgabe des Werkzeugs und bewertet das Ergebnis  
4. **Wiederholen oder Antworten** — Wenn weitere Daten nötig sind, wird der Zyklus wiederholt; andernfalls wird eine Antwort in natürlicher Sprache formuliert  

<img src="../../../translated_images/de/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct-Muster" width="800"/>

*Der ReAct-Zyklus — der Agent überlegt, was zu tun ist, handelt durch Werkzeugaufruf, beobachtet das Ergebnis und wiederholt, bis er die finale Antwort geben kann.*

Dies geschieht automatisch. Sie definieren die Werkzeuge und deren Beschreibungen. Das Modell übernimmt die Entscheidungsfindung, wann und wie sie verwendet werden.

## Wie Werkzeugaufrufe funktionieren

### Werkzeugdefinitionen

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Sie definieren Funktionen mit klaren Beschreibungen und Parameterspezifikationen. Das Modell sieht diese Beschreibungen in seinem Systemprompt und versteht, was jedes Werkzeug tut.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Ihre Wettersuchlogik
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Der Assistent wird automatisch von Spring Boot verbunden mit:
// - ChatModel Bean
// - Alle @Tool-Methoden aus @Component-Klassen
// - ChatMemoryProvider für das Sitzungsmanagement
```

Das untenstehende Diagramm erklärt jede Annotation und zeigt, wie jede einzelne Komponente der KI hilft zu verstehen, wann das Werkzeug aufzurufen ist und welche Argumente übergeben werden:

<img src="../../../translated_images/de/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomie von Werkzeugdefinitionen" width="800"/>

*Anatomie einer Werkzeugdefinition — @Tool sagt der KI, wann sie es verwenden soll, @P beschreibt jeden Parameter und @AiService verbindet alles beim Start.*

> **🤖 Probieren Sie den [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) und fragen Sie:
> - „Wie würde ich eine echte Wetter-API wie OpenWeatherMap statt Mock-Daten integrieren?“
> - „Was macht eine gute Werkzeugbeschreibung aus, die der KI hilft, es korrekt zu nutzen?“
> - „Wie behandle ich API-Fehler und Rate Limits in der Werkzeugimplementierung?“

### Entscheidungsfindung

Wenn ein Nutzer fragt: „Wie ist das Wetter in Seattle?“, wählt das Modell nicht zufällig ein Werkzeug aus. Es vergleicht die Absicht des Nutzers mit jeder verfügbaren Werkzeugbeschreibung, bewertet jede auf Relevanz und wählt die passendste aus. Anschließend generiert es einen strukturierten Funktionsaufruf mit den richtigen Parametern — in diesem Fall wird `location` auf `"Seattle"` gesetzt.

Wenn kein Werkzeug zur Anfrage passt, beantwortet das Modell die Frage aus seinem eigenen Wissen. Wenn mehrere Werkzeuge passen, wählt es das spezifischste.

<img src="../../../translated_images/de/decision-making.409cd562e5cecc49.webp" alt="Wie die KI entscheidet, welches Werkzeug verwendet wird" width="800"/>

*Das Modell bewertet jedes verfügbare Werkzeug anhand der Nutzerabsicht und wählt die beste Übereinstimmung — deshalb sind klare, spezifische Werkzeugbeschreibungen wichtig.*

### Ausführung

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot verdrahtet automatisch die deklarative `@AiService`-Schnittstelle mit allen registrierten Werkzeugen, und LangChain4j führt Werkzeugaufrufe automatisch aus. Im Hintergrund durchläuft ein kompletter Werkzeugaufruf sechs Stufen — von der natürlichen Spracheingabe des Nutzers bis zurück zu einer Antwort in natürlicher Sprache:

<img src="../../../translated_images/de/tool-calling-flow.8601941b0ca041e6.webp" alt="Ablauf eines Werkzeugaufrufs" width="800"/>

*Der End-to-End-Ablauf — der Nutzer stellt eine Frage, das Modell wählt ein Werkzeug aus, LangChain4j führt es aus, und das Modell integriert das Ergebnis in eine natürliche Antwort.*

Wenn Sie das [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) aus Modul 00 ausgeführt haben, kennen Sie dieses Muster bereits — die `Calculator`-Werkzeuge wurden genauso aufgerufen. Das untenstehende Sequenzdiagramm zeigt genau, was hinter den Kulissen bei dieser Demo passierte:

<img src="../../../translated_images/de/tool-calling-sequence.94802f406ca26278.webp" alt="Sequenzdiagramm zum Werkzeugaufruf" width="800"/>

*Die Werkzeugaufruf-Schleife aus dem Quick Start-Demo — `AiServices` sendet Ihre Nachricht und Werkzeug-Schemata an das LLM, das LLM antwortet mit einem Funktionsaufruf wie `add(42, 58)`, LangChain4j führt die `Calculator`-Methode lokal aus und liefert das Ergebnis für die finale Antwort zurück.*

> **🤖 Probieren Sie den [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) und fragen Sie:
> - „Wie funktioniert das ReAct-Muster und warum ist es für KI-Agenten effektiv?“
> - „Wie entscheidet der Agent, welches Werkzeug zu verwenden ist und in welcher Reihenfolge?“
> - „Was passiert, wenn ein Werkzeugsaufruf fehlschlägt – wie sollte ich Fehler robust behandeln?“

### Antwortgenerierung

Das Modell erhält die Wetterdaten und formatiert sie in eine Antwort in natürlicher Sprache für den Nutzer.

### Architektur: Spring Boot Auto-Wiring

Dieses Modul verwendet die Spring Boot-Integration von LangChain4j mit deklarativen `@AiService`-Schnittstellen. Beim Start entdeckt Spring Boot jede `@Component`, die `@Tool`-Methoden enthält, Ihre `ChatModel`-Bean und den `ChatMemoryProvider` – und verdrahtet alles zu einer einzigen `Assistant`-Schnittstelle ohne Boilerplate.

<img src="../../../translated_images/de/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architektur" width="800"/>

*Die @AiService-Schnittstelle verbindet ChatModel, Werkzeug-Komponenten und Memory Provider — Spring Boot übernimmt das gesamte Verdrahten automatisch.*

Hier der vollständige Anfrage-Lebenszyklus als Sequenzdiagramm — von der HTTP-Anfrage über Controller, Service und auto-verdrahteten Proxy bis zum Werkzeugaufruf und zurück:

<img src="../../../translated_images/de/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Spring Boot Werkzeugaufruf-Sequenz" width="800"/>

*Der komplette Spring Boot-Anfragelebenszyklus — HTTP-Anfrage fließt durch Controller und Service zum auto-verdrahteten Assistant-Proxy, der LLM- und Werkzeugaufrufe automatisch orchestriert.*

Wichtige Vorteile dieses Ansatzes:

- **Spring Boot Auto-Wiring** — ChatModel und Werkzeuge werden automatisch injiziert  
- **@MemoryId-Muster** — Automatisches sessionsbasiertes Speichermanagement  
- **Einzelne Instanz** — Assistant wird einmal erstellt und für bessere Performance wiederverwendet  
- **Typensichere Ausführung** — Java-Methoden werden direkt mit Typkonvertierung aufgerufen  
- **Mehrstufige Orchestrierung** — Handhabt Werkzeugverkettung automatisch  
- **Kein Boilerplate** — Keine manuellen `AiServices.builder()`-Aufrufe oder speicherbasierte HashMaps  

Alternative Ansätze (manuelle `AiServices.builder()`-Aufrufe) erfordern mehr Code und verzichten auf die Integrationsvorteile von Spring Boot.

## Werkzeugverkettung

**Werkzeugverkettung** — Die wahre Stärke von Werkzeug-basierten Agenten zeigt sich, wenn eine einzelne Frage mehrere Werkzeuge erfordert. Fragen Sie: „Wie ist das Wetter in Seattle in Fahrenheit?“ und der Agent verkettet automatisch zwei Werkzeuge: zuerst ruft er `getCurrentWeather` auf, um die Temperatur in Celsius zu erhalten, dann übergibt er diesen Wert an `celsiusToFahrenheit` zur Umrechnung — alles in einer einzigen Gesprächsrunde.

<img src="../../../translated_images/de/tool-chaining-example.538203e73d09dd82.webp" alt="Beispiel Werkzeugverkettung" width="800"/>

*Werkzeugverkettung in Aktion — der Agent ruft zuerst getCurrentWeather auf, leitet das Celsius-Ergebnis an celsiusToFahrenheit weiter und liefert eine kombinierte Antwort.*

**Fehlerfreundlichkeit** — Fragen Sie nach dem Wetter in einer Stadt, die nicht in den Mock-Daten enthalten ist. Das Werkzeug liefert eine Fehlermeldung, und die KI erklärt, dass sie nicht helfen kann, anstatt abzustürzen. Werkzeuge scheitern sicher. Das Diagramm unten vergleicht die beiden Ansätze — mit ordentlicher Fehlerbehandlung fängt der Agent die Ausnahme ab und reagiert hilfreich, ohne sie führt ein Fehler zum Absturz der gesamten Anwendung:

<img src="../../../translated_images/de/error-handling-flow.9a330ffc8ee0475c.webp" alt="Fehlerbehandlungsablauf" width="800"/>

*Wenn ein Werkzeug fehlschlägt, fängt der Agent den Fehler ab und antwortet mit einer hilfreichen Erklärung anstatt abzustürzen.*

Dies geschieht in einer einzelnen Gesprächsrunde. Der Agent orchestriert mehrere Werkzeugaufrufe autonom.

## Anwendung ausführen

**Bereitstellung überprüfen:**

Stellen Sie sicher, dass die `.env`-Datei im Stammverzeichnis mit Azure-Zugangsdaten existiert (erstellt während Modul 01). Führen Sie dies aus dem Modulverzeichnis (`04-tools/`) aus:

**Bash:**
```bash
cat ../.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Soll AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```

**Starten der Anwendung:**

> **Hinweis:** Falls Sie alle Anwendungen bereits mit `./start-all.sh` aus dem Stammverzeichnis gestartet haben (wie in Modul 01 beschrieben), läuft dieses Modul bereits auf Port 8084. Sie können die Startbefehle unten überspringen und direkt zu http://localhost:8084 gehen.

**Option 1: Verwendung des Spring Boot Dashboards (empfohlen für VS Code Benutzer)**

Der Dev-Container enthält die Spring Boot Dashboard-Erweiterung, die eine visuelle Schnittstelle bereitstellt, um alle Spring Boot-Anwendungen zu verwalten. Sie finden es in der Aktivitätsleiste links in VS Code (suchen Sie das Spring Boot-Symbol).

Über das Spring Boot Dashboard können Sie:
- Alle verfügbaren Spring Boot-Anwendungen im Workspace sehen  
- Anwendungen mit einem Klick starten/stoppen  
- Anwendungsprotokolle in Echtzeit ansehen  
- Den Anwendungsstatus überwachen  

Klicken Sie einfach auf den Play-Button neben „tools“, um dieses Modul zu starten, oder starten Sie alle Module gleichzeitig.

So sieht das Spring Boot Dashboard in VS Code aus:

<img src="../../../translated_images/de/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Das Spring Boot Dashboard in VS Code — starten, stoppen und überwachen Sie alle Module an einem Ort*

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

Oder starte nur dieses Modul:

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

Beide Skripte laden automatisch Umgebungsvariablen aus der root `.env` Datei und bauen die JARs, falls diese noch nicht existieren.

> **Hinweis:** Wenn du alle Module lieber manuell bauen möchtest, bevor du startest:
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

Öffne http://localhost:8084 in deinem Browser.

**Um zu stoppen:**

**Bash:**
```bash
./stop.sh  # Nur dieses Modul
# Oder
cd .. && ./stop-all.sh  # Alle Module
```

**PowerShell:**
```powershell
.\stop.ps1  # Dieses Modul nur
# Oder
cd ..; .\stop-all.ps1  # Alle Module
```

## Verwendung der Anwendung

Die Anwendung bietet eine Weboberfläche, über die du mit einem KI-Agenten interagieren kannst, der Zugriff auf Wetter- und Temperaturumrechnungstools hat. So sieht die Oberfläche aus — sie enthält Schnellstartbeispiele und ein Chat-Panel für das Senden von Anfragen:

<a href="images/tools-homepage.png"><img src="../../../translated_images/de/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Die AI Agent Tools Schnittstelle – Schnellstartbeispiele und Chatoberfläche zur Interaktion mit Tools*

### Einfachen Tool-Einsatz ausprobieren

Beginne mit einer einfachen Anfrage: „Konvertiere 100 Grad Fahrenheit in Celsius“. Der Agent erkennt, dass er das Temperaturumrechnungstool benötigt, ruft es mit den passenden Parametern auf und liefert das Ergebnis zurück. Beachte, wie natürlich das wirkt – du hast nicht angegeben, welches Tool verwendet oder wie es aufgerufen werden soll.

### Tool-Verkettung testen

Versuche nun etwas Komplexeres: „Wie ist das Wetter in Seattle und konvertiere es in Fahrenheit?“ Beobachte, wie der Agent dies schrittweise verarbeitet. Er holt zuerst die Wetterdaten (die in Celsius zurückgegeben werden), erkennt, dass er in Fahrenheit umrechnen muss, ruft das Umrechnungstool auf und kombiniert beide Ergebnisse zu einer Antwort.

### Gesprächsverlauf ansehen

Die Chatoberfläche speichert den Gesprächsverlauf, sodass du mehrstufige Interaktionen führen kannst. Du kannst alle vorherigen Anfragen und Antworten sehen, was es einfach macht, den Verlauf nachzuvollziehen und zu verstehen, wie der Agent im Laufe mehrerer Schritte Kontext aufbaut.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/de/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Mehrstufiges Gespräch mit einfachen Umrechnungen, Wetterabfragen und Tool-Verkettungen*

### Mit verschiedenen Anfragen experimentieren

Probiere verschiedene Kombinationen aus:
- Wetterabfragen: „Wie ist das Wetter in Tokio?“
- Temperaturumrechnungen: „Wie viel sind 25°C in Kelvin?“
- Kombinierte Anfragen: „Überprüfe das Wetter in Paris und sage mir, ob es über 20°C liegt“

Beachte, wie der Agent natürliche Sprache interpretiert und entsprechend passende Tool-Aufrufe erstellt.

## Wichtige Konzepte

### ReAct-Muster (Schlussfolgern und Handeln)

Der Agent wechselt zwischen dem Schlussfolgern (Entscheiden, was zu tun ist) und dem Handeln (Tools verwenden). Dieses Muster ermöglicht autonome Problemlösung anstelle bloßer Ausführung von Anweisungen.

### Tool-Beschreibungen sind wichtig

Die Qualität der Tool-Beschreibungen beeinflusst direkt, wie gut der Agent sie nutzt. Klare, spezifische Beschreibungen helfen dem Modell zu verstehen, wann und wie jedes Tool aufgerufen werden soll.

### Sitzungsverwaltung

Die `@MemoryId`-Annotation ermöglicht automatische sitzungsbasierte Speicherverwaltung. Jede Sitzungs-ID bekommt eine eigene `ChatMemory`-Instanz, verwaltet vom `ChatMemoryProvider`-Bean, sodass mehrere Nutzer gleichzeitig mit dem Agenten interagieren können, ohne dass sich ihre Gespräche vermischen. Das folgende Diagramm zeigt, wie mehrere Nutzer basierend auf ihren Sitzungs-IDs zu isolierten Speicherbereichen geleitet werden:

<img src="../../../translated_images/de/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Jede Sitzungs-ID führt zu einem isolierten Gesprächsverlauf – Nutzer sehen niemals die Nachrichten der anderen.*

### Fehlerbehandlung

Tools können ausfallen – APIs können Zeitüberschreitungen haben, Parameter ungültig sein, externe Dienste ausfallen. Produktionsagenten benötigen Fehlerbehandlung, damit das Modell Probleme erklären oder Alternativen versuchen kann, anstatt die ganze Anwendung abstürzen zu lassen. Wenn ein Tool eine Ausnahme wirft, fängt LangChain4j diese ab und gibt die Fehlermeldung an das Modell zurück, das dann das Problem in natürlicher Sprache erläutert.

## Verfügbare Tools

Das folgende Diagramm zeigt das breite Ökosystem von Tools, die du bauen kannst. Dieses Modul demonstriert Wetter- und Temperaturtools, aber das gleiche `@Tool`-Muster funktioniert für jede Java-Methode — von Datenbankabfragen bis zur Zahlungsabwicklung.

<img src="../../../translated_images/de/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Jede mit @Tool annotierte Java-Methode wird für die KI verfügbar — das Muster erstreckt sich auf Datenbanken, APIs, E-Mail, Dateioperationen und mehr.*

## Wann Tool-basierte Agenten verwenden

Nicht jede Anfrage benötigt Tools. Die Entscheidung hängt davon ab, ob die KI mit externen Systemen interagieren muss oder aus ihrem eigenen Wissen antworten kann. Die folgende Übersicht zeigt, wann Tools Mehrwert bieten und wann sie unnötig sind:

<img src="../../../translated_images/de/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*Eine schnelle Entscheidungsübersicht – Tools sind für Echtzeitdaten, Berechnungen und Aktionen; allgemeines Wissen und kreative Aufgaben benötigen sie nicht.*

## Tools vs RAG

Module 03 und 04 erweitern beide die Fähigkeiten der KI, aber auf grundlegend unterschiedliche Weise. RAG gibt dem Modell Zugang zu **Wissen** durch Abruf von Dokumenten. Tools geben dem Modell die Fähigkeit, **Aktionen** durch Funktionsaufrufe auszuführen. Das folgende Diagramm vergleicht diese beiden Ansätze nebeneinander – vom Ablauf der Workflows bis zu deren Vor- und Nachteilen:

<img src="../../../translated_images/de/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG ruft Informationen aus statischen Dokumenten ab – Tools führen Aktionen aus und holen dynamische, Echtzeitdaten. Viele Produktionssysteme kombinieren beides.*

In der Praxis kombinieren viele Produktionssysteme beide Ansätze: RAG, um Antworten in deiner Dokumentation zu verankern, und Tools, um Live-Daten zu holen oder Operationen durchzuführen.

## Nächste Schritte

**Nächstes Modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigation:** [← Vorheriges: Modul 03 - RAG](../03-rag/README.md) | [Zurück zur Übersicht](../README.md) | [Nächstes: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mithilfe des KI-Übersetzungsdienstes [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir auf Genauigkeit achten, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Ursprungssprache gilt als maßgebliche Quelle. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die durch die Nutzung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->