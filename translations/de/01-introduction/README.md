# Modul 01: Einstieg mit LangChain4j

## Inhaltsverzeichnis

- [Video-Anleitung](../../../01-introduction)
- [Was Sie lernen werden](../../../01-introduction)
- [Voraussetzungen](../../../01-introduction)
- [Das Kernproblem verstehen](../../../01-introduction)
- [Tokens verstehen](../../../01-introduction)
- [Wie Speicher funktioniert](../../../01-introduction)
- [Wie dies LangChain4j verwendet](../../../01-introduction)
- [Azure OpenAI Infrastruktur bereitstellen](../../../01-introduction)
- [Die Anwendung lokal ausführen](../../../01-introduction)
- [Die Anwendung verwenden](../../../01-introduction)
  - [Zustandsloser Chat (linkes Panel)](../../../01-introduction)
  - [Zustandsbehafteter Chat (rechtes Panel)](../../../01-introduction)
- [Nächste Schritte](../../../01-introduction)

## Video-Anleitung

Sehen Sie sich diese Live-Sitzung an, die erklärt, wie Sie mit diesem Modul starten: [Einstieg mit LangChain4j - Live-Sitzung](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Was Sie lernen werden

Wenn Sie den Schnellstart abgeschlossen haben, haben Sie gesehen, wie man Prompts sendet und Antworten erhält. Das ist die Grundlage, aber echte Anwendungen benötigen mehr. Dieses Modul zeigt Ihnen, wie Sie konversationale KI bauen, die Kontext speichert und den Zustand beibehält – der Unterschied zwischen einer einmaligen Demo und einer produktionsreifen Anwendung.

Wir verwenden im gesamten Leitfaden Azure OpenAI GPT-5.2, weil seine fortgeschrittenen Argumentationsfähigkeiten das Verhalten unterschiedlicher Muster deutlicher machen. Wenn Sie Speicher hinzufügen, sehen Sie den Unterschied klar. Das erleichtert zu verstehen, was jede Komponente zur Anwendung beiträgt.

Sie bauen eine Anwendung, die beide Muster demonstriert:

**Zustandsloser Chat** – Jede Anfrage ist unabhängig. Das Modell erinnert sich nicht an vorherige Nachrichten. Dies ist das Muster, das Sie im Schnellstart verwendet haben.

**Zustandsbehaftete Konversation** – Jede Anfrage enthält die Gesprächshistorie. Das Modell behält den Kontext über mehrere Runden hinweg bei. Das erfordern produktive Anwendungen.

## Voraussetzungen

- Azure-Abonnement mit Zugriff auf Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Hinweis:** Java, Maven, Azure CLI und Azure Developer CLI (azd) sind im bereitgestellten Devcontainer vorinstalliert.

> **Hinweis:** Dieses Modul verwendet GPT-5.2 auf Azure OpenAI. Die Bereitstellung wird automatisch über `azd up` konfiguriert – ändern Sie den Modellnamen im Code nicht.

## Das Kernproblem verstehen

Sprachmodelle sind zustandslos. Jeder API-Aufruf ist unabhängig. Wenn Sie „Mein Name ist John“ senden und dann fragen „Wie heiße ich?“, weiß das Modell nicht, dass Sie sich gerade vorgestellt haben. Es behandelt jede Anfrage, als wäre es das erste Gespräch, das Sie je geführt haben.

Das ist für einfache Q&A in Ordnung, aber für echte Anwendungen nutzlos. Kundenservice-Bots müssen sich merken, was Sie gesagt haben. Persönliche Assistenten benötigen Kontext. Jedes mehrstufige Gespräch erfordert Speicher.

<img src="../../../translated_images/de/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Zustandslose vs. Zustandsbehaftete Konversationen" width="800"/>

*Der Unterschied zwischen zustandslosen (unabhängigen Aufrufen) und zustandsbehafteten (kontextbewussten) Konversationen*

## Tokens verstehen

Bevor wir in Konversationen eintauchen, ist es wichtig, Tokens zu verstehen – die Grundeinheiten des Texts, die Sprachmodelle verarbeiten:

<img src="../../../translated_images/de/token-explanation.c39760d8ec650181.webp" alt="Token-Erklärung" width="800"/>

*Beispiel, wie Text in Tokens zerlegt wird – „I love AI!“ wird zu 4 separaten Verarbeitungseinheiten*

Tokens sind das Maß, mit dem KI-Modelle Text messen und verarbeiten. Wörter, Satzzeichen und sogar Leerzeichen können Tokens sein. Ihr Modell hat ein Limit, wie viele Tokens es gleichzeitig verarbeiten kann (400.000 für GPT-5.2, mit bis zu 272.000 Eingabe-Tokens und 128.000 Ausgabe-Tokens). Tokens zu verstehen hilft Ihnen, Gesprächslänge und Kosten zu steuern.

## Wie Speicher funktioniert

Chat-Speicher löst das Zustandslosigkeitsproblem, indem er die Gesprächshistorie speichert. Bevor Ihre Anfrage ans Modell geschickt wird, fügt das Framework relevante vorherige Nachrichten hinzu. Wenn Sie „Wie heiße ich?“ fragen, sendet das System tatsächlich die gesamte Gesprächshistorie mit, sodass das Modell sieht, dass Sie zuvor „Mein Name ist John“ gesagt haben.

LangChain4j stellt Speicherimplementierungen bereit, die das automatisch handhaben. Sie wählen aus, wie viele Nachrichten behalten werden sollen, und das Framework verwaltet das Kontextfenster.

<img src="../../../translated_images/de/memory-window.bbe67f597eadabb3.webp" alt="Konzept des Speicherfensters" width="800"/>

*MessageWindowChatMemory verwaltet ein gleitendes Fenster der letzten Nachrichten und entfernt automatisch alte Nachrichten*

## Wie dies LangChain4j verwendet

Dieses Modul erweitert den Schnellstart durch Integration von Spring Boot und Hinzufügen von Gesprächsspeicher. So fügen sich die Komponenten zusammen:

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

**Chat-Modell** – Konfigurieren Sie Azure OpenAI als Spring Bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Der Builder liest Anmeldedaten aus Umgebungsvariablen, die von `azd up` gesetzt wurden. Das Setzen von `baseUrl` auf Ihren Azure-Endpunkt lässt den OpenAI-Client mit Azure OpenAI funktionieren.

**Gesprächsspeicher** – Verfolgen Sie den Chat-Verlauf mit MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Erstellen Sie Speicher mit `withMaxMessages(10)`, um die letzten 10 Nachrichten zu behalten. Fügen Sie Nutzer- und KI-Nachrichten mit getypten Wrappern hinzu: `UserMessage.from(text)` und `AiMessage.from(text)`. Greifen Sie mit `memory.messages()` auf die Historie zu und senden Sie sie ans Modell. Der Service speichert separate Speicher-Instanzen pro Konversations-ID, sodass mehrere Nutzer gleichzeitig chatten können.

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) und fragen Sie:
> - "Wie entscheidet MessageWindowChatMemory, welche Nachrichten fallen gelassen werden, wenn das Fenster voll ist?"
> - "Kann ich eigene Speicherimplementierungen mit einer Datenbank anstelle von In-Memory erstellen?"
> - "Wie würde ich eine Zusammenfassung hinzufügen, um alte Gesprächshistorien zu komprimieren?"

Der zustandslose Chat-Endpunkt überspringt den Speicher komplett – einfach `chatModel.chat(prompt)` wie im Schnellstart. Der zustandsbehaftete Endpunkt fügt Nachrichten zum Speicher hinzu, ruft die Historie ab und schickt diesen Kontext bei jeder Anfrage mit. Gleiche Modellkonfiguration, unterschiedliche Muster.

## Azure OpenAI Infrastruktur bereitstellen

**Bash:**
```bash
cd 01-introduction
azd up  # Wählen Sie Abonnement und Standort (eastus2 empfohlen)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Abonnement und Standort auswählen (eastus2 empfohlen)
```

> **Hinweis:** Wenn ein Timeout-Fehler auftritt (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), führen Sie einfach `azd up` erneut aus. Azure-Ressourcen werden möglicherweise noch im Hintergrund bereitgestellt, und ein erneuter Versuch ermöglicht die Fertigstellung, sobald die Ressourcen den Endzustand erreichen.

Dies wird:
1. Azure OpenAI-Ressource mit GPT-5.2 und text-embedding-3-small Modellen bereitstellen
2. Automatisch eine `.env`-Datei im Projektroot mit Zugangsdaten generieren
3. Alle erforderlichen Umgebungsvariablen konfigurieren

**Probleme bei der Bereitstellung?** Siehe die [Infrastructure README](infra/README.md) für detaillierte Fehlerbehebung bei Subdomain-Konflikten, manuelle Azure-Portal-Bereitstellung und Modellkonfigurationshinweise.

**Überprüfen Sie, ob die Bereitstellung erfolgreich war:**

**Bash:**
```bash
cat ../.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, usw. anzeigen.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY usw. anzeigen.
```

> **Hinweis:** Der Befehl `azd up` generiert die `.env`-Datei automatisch. Wenn Sie diese später aktualisieren müssen, können Sie entweder die `.env` manuell bearbeiten oder sie mit folgendem Befehl neu generieren:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## Die Anwendung lokal ausführen

**Bereitstellung verifizieren:**

Stellen Sie sicher, dass die `.env`-Datei im Root-Verzeichnis mit den Azure-Zugangsdaten vorhanden ist:

**Bash:**
```bash
cat ../.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```

**Starten Sie die Anwendungen:**

**Option 1: Nutzung des Spring Boot Dashboards (empfohlen für VS Code Nutzer)**

Der Devcontainer enthält die Spring Boot Dashboard-Erweiterung, die eine visuelle Oberfläche zur Verwaltung aller Spring Boot Anwendungen bietet. Sie finden es in der Aktivitätsleiste links in VS Code (Suchen Sie das Spring Boot Icon).

Im Spring Boot Dashboard können Sie:
- Alle verfügbaren Spring Boot Anwendungen im Workspace sehen
- Anwendungen mit einem Klick starten/stoppen
- Anwendungs-Logs live ansehen
- Anwendungsstatus überwachen

Klicken Sie einfach auf den Play-Button neben „introduction“, um dieses Modul zu starten, oder starten Sie alle Module gleichzeitig.

<img src="../../../translated_images/de/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Option 2: Nutzung von Shell-Skripten**

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

Beide Skripte laden automatisch Umgebungsvariablen aus der Root `.env`-Datei und bauen die JARs, falls diese nicht existieren.

> **Hinweis:** Wenn Sie alle Module manuell vor dem Start bauen möchten:
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

Öffnen Sie http://localhost:8080 in Ihrem Browser.

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

<img src="../../../translated_images/de/home-screen.121a03206ab910c0.webp" alt="Startbildschirm der Anwendung" width="800"/>

*Dashboard mit der Auswahl zwischen Simple Chat (zustandslos) und Conversational Chat (zustandsbehaftet)*

### Zustandsloser Chat (linkes Panel)

Probieren Sie das zuerst. Fragen Sie „Mein Name ist John“ und dann direkt „Wie heiße ich?“ Das Modell wird sich nicht erinnern, da jede Nachricht unabhängig ist. Dies demonstriert das Kernproblem bei der Basisintegration von Sprachmodellen – kein Gesprächskontext.

<img src="../../../translated_images/de/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo Zustandsloser Chat" width="800"/>

*Die KI erinnert sich nicht an Ihren Namen aus der vorherigen Nachricht*

### Zustandsbehafteter Chat (rechtes Panel)

Versuchen Sie hier die gleiche Abfolge. Fragen Sie „Mein Name ist John“ und dann „Wie heiße ich?“ Dieses Mal erinnert es sich. Der Unterschied ist MessageWindowChatMemory – es verwaltet die Gesprächshistorie und fügt sie jeder Anfrage hinzu. So arbeiten produktive konversationale KIs.

<img src="../../../translated_images/de/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo Zustandsbehafteter Chat" width="800"/>

*Die KI erinnert sich an Ihren Namen vom Gesprächsanfang*

Beide Panels nutzen das gleiche GPT-5.2 Modell. Der einzige Unterschied ist der Speicher. Das macht klar, was Speicher Ihrer Anwendung bringt und warum er für reale Anwendungsfälle unerlässlich ist.

## Nächste Schritte

**Nächstes Modul:** [02-prompt-engineering - Prompt Engineering mit GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigation:** [← Vorheriges: Modul 00 - Schnellstart](../00-quick-start/README.md) | [Zurück zur Übersicht](../README.md) | [Weiter: Modul 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mithilfe des KI-Übersetzungsdienstes [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir auf Genauigkeit achten, möchten wir darauf hinweisen, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Ursprungssprache gilt als maßgebliche Quelle. Für kritische Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Nutzung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->