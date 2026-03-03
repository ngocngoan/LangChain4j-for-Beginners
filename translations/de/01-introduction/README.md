# Modul 01: Einstieg mit LangChain4j

## Inhaltsverzeichnis

- [Video-Durchgang](../../../01-introduction)
- [Was Sie lernen werden](../../../01-introduction)
- [Voraussetzungen](../../../01-introduction)
- [Das Kernproblem verstehen](../../../01-introduction)
- [Tokens verstehen](../../../01-introduction)
- [Wie Speicher funktioniert](../../../01-introduction)
- [Wie dies LangChain4j verwendet](../../../01-introduction)
- [Azure OpenAI-Infrastruktur bereitstellen](../../../01-introduction)
- [Anwendung lokal ausführen](../../../01-introduction)
- [Die Anwendung verwenden](../../../01-introduction)
  - [Zustandsloser Chat (linkes Panel)](../../../01-introduction)
  - [Zustandsbehafteter Chat (rechtes Panel)](../../../01-introduction)
- [Nächste Schritte](../../../01-introduction)

## Video-Durchgang

Sehen Sie sich diese Live-Session an, die erklärt, wie Sie mit diesem Modul starten:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Einstieg mit LangChain4j - Live-Session" width="800"/></a>

## Was Sie lernen werden

Im Quickstart haben Sie GitHub-Modelle verwendet, um Prompts zu senden, Werkzeuge aufzurufen, eine RAG-Pipeline zu bauen und Guardrails zu testen. Diese Demos zeigten, was möglich ist – jetzt wechseln wir zu Azure OpenAI und GPT-5.2 und beginnen damit, produktionsähnliche Anwendungen zu entwickeln. Dieses Modul konzentriert sich auf konversationelle KI, die Kontext behält und Zustände verwaltet – die Konzepte, die in den Quickstart-Demos hinter den Kulissen genutzt, aber nicht erklärt wurden.

Wir verwenden während dieses Leitfadens Azure OpenAIs GPT-5.2, da seine fortgeschrittenen Fähigkeiten im logischen Denken das Verhalten der verschiedenen Muster deutlicher machen. Wenn Sie Speicher hinzufügen, sehen Sie den Unterschied klar. Das erleichtert das Verständnis dessen, was jede Komponente Ihrer Anwendung bringt.

Sie werden eine Anwendung bauen, die beide Muster demonstriert:

**Zustandsloser Chat** - Jede Anfrage ist unabhängig. Das Modell hat kein Gedächtnis für vorherige Nachrichten. Dies ist das Muster, das Sie im Quickstart verwendet haben.

**Zustandsbehaftete Konversation** - Jede Anfrage beinhaltet die Gesprächshistorie. Das Modell behält den Kontext über mehrere Interaktionen hinweg. Dies ist das, was produktive Anwendungen benötigen.

## Voraussetzungen

- Azure-Abonnement mit Azure OpenAI-Zugriff
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Hinweis:** Java, Maven, Azure CLI und Azure Developer CLI (azd) sind im bereitgestellten Devcontainer vorinstalliert.

> **Hinweis:** Dieses Modul verwendet GPT-5.2 auf Azure OpenAI. Die Bereitstellung wird automatisch über `azd up` konfiguriert – ändern Sie den Modellnamen im Code nicht.

## Das Kernproblem verstehen

Sprachmodelle sind zustandslos. Jeder API-Aufruf ist unabhängig. Wenn Sie „Mein Name ist John“ senden und dann fragen „Wie heißt mein Name?“, hat das Modell keine Ahnung, dass Sie sich gerade vorgestellt haben. Es behandelt jede Anfrage, als wäre es das erste Gespräch, das Sie je geführt haben.

Das ist für einfache Q&A okay, aber für echte Anwendungen unbrauchbar. Kundendienstbots müssen sich merken, was Sie ihnen gesagt haben. Persönliche Assistenten brauchen Kontext. Jede mehrstufige Konversation erfordert Speicher.

Die folgende Grafik stellt die beiden Ansätze gegenüber – links ein zustandsloser Aufruf, der Ihren Namen vergisst; rechts ein zustandsbehafteter Aufruf, unterstützt von ChatMemory, der ihn sich merkt.

<img src="../../../translated_images/de/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Zustandslose vs. Zustandsbehaftete Konversationen" width="800"/>

*Der Unterschied zwischen zustandslosen (unabhängigen Anfragen) und zustandsbehafteten (kontextbewussten) Konversationen*

## Tokens verstehen

Bevor wir in Konversationen eintauchen, ist es wichtig, Tokens zu verstehen – die Grundeinheiten des Textes, die Sprachmodelle verarbeiten:

<img src="../../../translated_images/de/token-explanation.c39760d8ec650181.webp" alt="Token-Erklärung" width="800"/>

*Beispiel, wie Text in Tokens zerlegt wird – „I love AI!“ wird zu 4 separaten Verarbeitungseinheiten*

Tokens sind die Maßeinheiten, mit denen KI-Modelle Text messen und verarbeiten. Wörter, Satzzeichen und sogar Leerzeichen können Tokens sein. Ihr Modell hat ein Limit, wie viele Tokens es auf einmal verarbeiten kann (400.000 bei GPT-5.2, mit bis zu 272.000 Eingabe-Tokens und 128.000 Ausgabe-Tokens). Tokens zu verstehen hilft Ihnen, Gesprächslänge und Kosten zu verwalten.

## Wie Speicher funktioniert

Chat-Speicher löst das Zustandslosigkeitsproblem, indem er die Gesprächshistorie erhält. Bevor Ihre Anfrage an das Modell gesendet wird, fügt das Framework relevante vorherige Nachrichten hinzu. Wenn Sie fragen „Wie heißt mein Name?“, sendet das System tatsächlich die gesamte Gesprächshistorie, sodass das Modell sieht, dass Sie zuvor sagten „Mein Name ist John.“

LangChain4j bietet Speicherimplementierungen, die dies automatisch übernehmen. Sie wählen aus, wie viele Nachrichten behalten werden, und das Framework verwaltet das Kontextfenster. Die folgende Grafik zeigt, wie MessageWindowChatMemory ein gleitendes Fenster der letzten Nachrichten erhält.

<img src="../../../translated_images/de/memory-window.bbe67f597eadabb3.webp" alt="Speicherfenster-Konzept" width="800"/>

*MessageWindowChatMemory erhält ein gleitendes Fenster mit neuesten Nachrichten und entfernt automatisch alte*

## Wie dies LangChain4j verwendet

Dieses Modul erweitert den Quickstart durch die Integration von Spring Boot und das Hinzufügen von Gesprächsspeicher. So fügen sich die Teile zusammen:

**Abhängigkeiten** – Fügen Sie zwei LangChain4j-Bibliotheken hinzu:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**Chat-Modell** – Konfigurieren Sie Azure OpenAI als Spring-Bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

Der Builder liest Zugangsdaten aus Umgebungsvariablen, die von `azd up` gesetzt wurden. Das Setzen von `baseUrl` auf Ihren Azure-Endpunkt macht den OpenAI-Client kompatibel mit Azure OpenAI.

**Gesprächsspeicher** – Verfolgen Sie den Chat-Verlauf mit MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Erstellen Sie Speicher mit `withMaxMessages(10)`, um die letzten 10 Nachrichten zu behalten. Fügen Sie Benutzer- und KI-Nachrichten mit typisierten Wrappern hinzu: `UserMessage.from(text)` und `AiMessage.from(text)`. Holen Sie den Verlauf mit `memory.messages()` ab und senden Sie ihn ans Modell. Der Service speichert separate Speicherinstanzen pro Gesprächs-ID, sodass mehrere Nutzer gleichzeitig chatten können.

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat aus:** Öffnen Sie [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) und fragen Sie:
> - „Wie entscheidet MessageWindowChatMemory, welche Nachrichten fallen gelassen werden, wenn das Fenster voll ist?“
> - „Kann ich kundenspezifischen Speicher mittels Datenbank statt im Arbeitsspeicher implementieren?“
> - „Wie würde ich eine Zusammenfassung hinzufügen, um alte Gesprächshistorie zu komprimieren?“

Der zustandslose Chat-Endpunkt überspringt den Speicher komplett – einfach `chatModel.chat(prompt)` wie im Quickstart. Der zustandsbehaftete Endpunkt fügt Nachrichten zum Speicher hinzu, ruft den Verlauf ab und inkludiert diesen Kontext bei jeder Anfrage. Gleiche Modellkonfiguration, unterschiedliche Muster.

## Azure OpenAI-Infrastruktur bereitstellen

**Bash:**
```bash
cd 01-introduction
azd up  # Wählen Sie das Abonnement und den Standort aus (empfohlen wird eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Wählen Sie das Abonnement und den Standort (eastus2 empfohlen)
```

> **Hinweis:** Wenn Sie einen Timeout-Fehler (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`) erhalten, führen Sie einfach `azd up` erneut aus. Azure-Ressourcen sind möglicherweise noch in Bereitstellung, und ein erneuter Versuch erlaubt der Bereitstellung, abzuschließen, sobald die Ressourcen einen Endzustand erreichen.

Dies führt aus:
1. Bereitstellung der Azure OpenAI-Ressource mit GPT-5.2 und text-embedding-3-small Modellen
2. Automatische Erstellung einer `.env`-Datei im Projektstamm mit Zugangsdaten
3. Einrichtung aller erforderlichen Umgebungsvariablen

**Legen Sie bei Bereitstellungsproblemen los?** Lesen Sie die [Infrastructure README](infra/README.md) für detaillierte Fehlerbehebung, einschließlich Konflikten bei Subdomain-Namen, manuellen Azure Portal-Bereitstellungsschritten und Modellkonfigurationshinweisen.

**Bestätigen Sie, dass die Bereitstellung erfolgreich war:**

**Bash:**
```bash
cat ../.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY usw. anzeigen.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY usw. anzeigen.
```

> **Hinweis:** Der Befehl `azd up` erzeugt automatisch die `.env`-Datei. Wenn Sie diese später aktualisieren wollen, können Sie entweder die `.env`-Datei manuell bearbeiten oder sie durch Ausführen der folgenden Befehle neu generieren:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```

> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## Anwendung lokal ausführen

**Bereitstellung überprüfen:**

Stellen Sie sicher, dass die `.env`-Datei mit Azure-Zugangsdaten im Stammverzeichnis vorhanden ist. Führen Sie dies im Modulverzeichnis (`01-introduction/`) aus:

**Bash:**
```bash
cat ../.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```

**Starten Sie die Anwendungen:**

**Option 1: Spring Boot Dashboard verwenden (empfohlen für VS Code Nutzer)**

Der Devcontainer enthält die Spring Boot Dashboard-Erweiterung, die eine visuelle Oberfläche bietet, um alle Spring Boot-Anwendungen zu verwalten. Sie finden sie in der Aktivitätsleiste links in VS Code (Suchen Sie das Spring Boot-Symbol).

Vom Spring Boot Dashboard aus können Sie:
- Alle verfügbaren Spring Boot-Anwendungen im Workspace sehen
- Anwendungen mit einem Klick starten/stoppen
- Anwendungsprotokolle in Echtzeit anzeigen
- Anwendungsstatus überwachen

Starten Sie dieses Modul einfach mit dem Play-Button neben „introduction“ oder starten Sie alle Module gleichzeitig.

<img src="../../../translated_images/de/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Das Spring Boot Dashboard in VS Code – starten, stoppen und überwachen Sie alle Module an einem Ort*

**Option 2: Shell-Skripte verwenden**

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
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Beide Skripte laden automatisch Umgebungsvariablen aus der Root-`.env`-Datei und bauen die JARs, falls diese nicht existieren.

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

Öffnen Sie http://localhost:8080 im Browser.

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

## Die Anwendung verwenden

Die Anwendung bietet eine Weboberfläche mit zwei Chat-Implementierungen nebeneinander.

<img src="../../../translated_images/de/home-screen.121a03206ab910c0.webp" alt="Startseite der Anwendung" width="800"/>

*Dashboard zeigt die Optionen Einfacher Chat (zustandslos) und Konversationeller Chat (zustandsbehaftet)*

### Zustandsloser Chat (linkes Panel)

Probieren Sie dies zuerst aus. Fragen Sie „Mein Name ist John“ und fragen Sie dann sofort „Wie heißt mein Name?“ Das Modell wird sich nicht erinnern, weil jede Nachricht unabhängig ist. Das zeigt das Kernproblem der einfachen Sprachmodell-Integration – kein Gesprächskontext.

<img src="../../../translated_images/de/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo zustandsloser Chat" width="800"/>

*KI erinnert sich nicht an Ihren Namen aus der vorherigen Nachricht*

### Zustandsbehafteter Chat (rechtes Panel)

Probieren Sie jetzt dieselbe Folge hier aus. Fragen Sie „Mein Name ist John“ und dann „Wie heißt mein Name?“ Diesmal erinnert es sich. Der Unterschied ist MessageWindowChatMemory – dieser erhält die Gesprächshistorie und inkludiert sie bei jeder Anfrage. So funktioniert produktive konversationelle KI.

<img src="../../../translated_images/de/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo zustandsbehafteter Chat" width="800"/>

*KI erinnert sich an Ihren Namen aus dem früheren Gespräch*

Beide Panels verwenden dasselbe GPT-5.2 Modell. Der einzige Unterschied ist der Speicher. Das macht klar, welchen Mehrwert Speicher für Ihre Anwendung bringt und warum er für reale Anwendungsfälle essenziell ist.

## Nächste Schritte

**Nächstes Modul:** [02-prompt-engineering – Prompt Engineering mit GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigation:** [← Vorheriges: Modul 00 – Quick Start](../00-quick-start/README.md) | [Zurück zum Hauptmenü](../README.md) | [Nächstes: Modul 02 – Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mithilfe des KI-Übersetzungsdienstes [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, weisen wir darauf hin, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Ursprungssprache ist als maßgebliche Quelle zu betrachten. Für wichtige Informationen wird eine professionelle, menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die durch die Nutzung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->