# Modul 01: Einstieg in LangChain4j

## Inhaltsverzeichnis

- [Video-Durchgang](../../../01-introduction)
- [Was Sie lernen werden](../../../01-introduction)
- [Voraussetzungen](../../../01-introduction)
- [Das Kernproblem verstehen](../../../01-introduction)
- [Verstehen von Tokens](../../../01-introduction)
- [Wie Speicher funktioniert](../../../01-introduction)
- [Wie dies LangChain4j nutzt](../../../01-introduction)
- [Azure OpenAI-Infrastruktur bereitstellen](../../../01-introduction)
- [Die Anwendung lokal ausführen](../../../01-introduction)
- [Anwendung benutzen](../../../01-introduction)
  - [Zustandsloser Chat (linkes Panel)](../../../01-introduction)
  - [Zustandsbehafteter Chat (rechtes Panel)](../../../01-introduction)
- [Nächste Schritte](../../../01-introduction)

## Video-Durchgang

Sehen Sie sich diese Live-Session an, die erklärt, wie Sie mit diesem Modul beginnen:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Einstieg in LangChain4j - Live-Session" width="800"/></a>

## Was Sie lernen werden

Wenn Sie den Schnellstart abgeschlossen haben, haben Sie gesehen, wie man Prompts sendet und Antworten erhält. Das ist die Grundlage, aber echte Anwendungen benötigen mehr. Dieses Modul zeigt Ihnen, wie Sie konversationelle KI bauen, die sich an Kontext erinnert und den Zustand beibehält – der Unterschied zwischen einer einmaligen Demo und einer produktionsreifen Anwendung.

Wir verwenden im gesamten Leitfaden Azure OpenAI's GPT-5.2, weil seine erweiterten Fähigkeiten zum logischen Denken das Verhalten verschiedener Muster deutlicher machen. Wenn Sie Speicher hinzufügen, werden Sie den Unterschied klar erkennen. Das erleichtert das Verständnis, welchen Beitrag jede Komponente zu Ihrer Anwendung leistet.

Sie bauen eine Anwendung, die beide Muster demonstriert:

**Zustandsloser Chat** – Jede Anfrage ist unabhängig. Das Modell hat kein Gedächtnis an vorherige Nachrichten. Dies ist das Muster, das Sie im Schnellstart verwendet haben.

**Zustandsbehaftete Unterhaltung** – Jede Anfrage enthält den Gesprächsverlauf. Das Modell behält den Kontext über mehrere Runden hinweg. Das ist, was Produktionsanwendungen brauchen.

## Voraussetzungen

- Azure-Abonnement mit Zugriff auf Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Hinweis:** Java, Maven, Azure CLI und Azure Developer CLI (azd) sind im bereitgestellten Devcontainer vorinstalliert.

> **Hinweis:** Dieses Modul nutzt GPT-5.2 auf Azure OpenAI. Die Bereitstellung wird automatisch via `azd up` konfiguriert – ändern Sie den Modellnamen im Code nicht.

## Das Kernproblem verstehen

Sprachmodelle sind zustandslos. Jeder API-Aufruf ist unabhängig. Wenn Sie "Mein Name ist John" senden und dann fragen "Wie heiße ich?", weiß das Modell nicht, dass Sie sich gerade vorgestellt haben. Es behandelt jede Anfrage, als wäre es das erste Gespräch, das Sie jemals geführt haben.

Das reicht für einfache Fragen und Antworten, ist aber für echte Anwendungen nutzlos. Kundenservice-Bots müssen sich daran erinnern, was Sie ihnen gesagt haben. Persönliche Assistenten brauchen Kontext. Jede mehrstufige Unterhaltung erfordert Gedächtnis.

<img src="../../../translated_images/de/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Zustandslose vs Zustandsbehaftete Unterhaltungen" width="800"/>

*Der Unterschied zwischen zustandslosen (unabhängigen) und zustandsbehafteten (kontextbewussten) Unterhaltungen*

## Verstehen von Tokens

Bevor wir in Konversationen eintauchen, ist es wichtig, Tokens zu verstehen – die Grundeinheiten von Text, die Sprachmodelle verarbeiten:

<img src="../../../translated_images/de/token-explanation.c39760d8ec650181.webp" alt="Token-Erklärung" width="800"/>

*Beispiel, wie Text in Tokens aufgeteilt wird – „I love AI!“ wird zu 4 separaten Verarbeitungseinheiten*

Tokens sind die Maßeinheit, mit der KI-Modelle Text messen und verarbeiten. Wörter, Satzzeichen und sogar Leerzeichen können Tokens sein. Ihr Modell hat eine Begrenzung, wie viele Tokens es auf einmal verarbeiten kann (400.000 für GPT-5.2, mit bis zu 272.000 Eingabe-Tokens und 128.000 Ausgabe-Tokens). Das Verständnis von Tokens hilft Ihnen, die Gesprächslänge und Kosten zu steuern.

## Wie Speicher funktioniert

Chat-Speicher löst das Problem der Zustandslosigkeit, indem er den Gesprächsverlauf erhält. Bevor die Anfrage ans Modell gesendet wird, fügt das Framework relevante vorherige Nachrichten hinzu. Wenn Sie fragen „Wie heiße ich?“, sendet das System tatsächlich den gesamten Gesprächsverlauf, sodass das Modell sieht, dass Sie zuvor „Mein Name ist John“ gesagt haben.

LangChain4j stellt Speicherimplementierungen bereit, die das automatisch verwalten. Sie wählen, wie viele Nachrichten behalten werden, und das Framework verwaltet das Kontextfenster.

<img src="../../../translated_images/de/memory-window.bbe67f597eadabb3.webp" alt="Konzept des Speicherfensters" width="800"/>

*MessageWindowChatMemory verwaltet ein gleitendes Fenster der letzten Nachrichten und verwirft automatisch alte*

## Wie dies LangChain4j nutzt

Dieses Modul erweitert den Schnellstart, indem es Spring Boot integriert und Gesprächsspeicher hinzufügt. So fügen sich die Teile zusammen:

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

Der Builder liest Anmeldeinformationen aus Umgebungsvariablen, die durch `azd up` gesetzt wurden. Die Einstellung `baseUrl` auf Ihren Azure-Endpunkt sorgt dafür, dass der OpenAI-Client mit Azure OpenAI funktioniert.

**Gesprächsspeicher** – Verfolgen Sie den Chat-Verlauf mit MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Erstellen Sie Speicher mit `withMaxMessages(10)`, um die letzten 10 Nachrichten zu behalten. Fügen Sie Nutzer- und KI-Nachrichten mit typisierten Wrappern hinzu: `UserMessage.from(text)` und `AiMessage.from(text)`. Holen Sie den Verlauf mit `memory.messages()` und senden Sie ihn an das Modell. Der Service speichert separate Speicher je Gesprächs-ID, was mehreren Nutzern gleichzeitig Chatten erlaubt.

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat:** Öffnen Sie [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) und fragen Sie:
> - „Wie entscheidet MessageWindowChatMemory, welche Nachrichten entfernt werden, wenn das Fenster voll ist?“
> - „Kann ich eine eigene Speicherlösung mit einer Datenbank implementieren statt im Speicher?“
> - „Wie würde ich Zusammenfassungen hinzufügen, um alten Gesprächsverlauf zu komprimieren?“

Der zustandslose Chat-Endpunkt überspringt Speicher komplett – einfach `chatModel.chat(prompt)` wie im Schnellstart. Der zustandsbehaftete Endpunkt fügt Nachrichten zum Speicher hinzu, holt den Verlauf und fügt diesen Kontext zu jeder Anfrage hinzu. Gleiche Modell-Konfiguration, unterschiedliche Muster.

## Azure OpenAI-Infrastruktur bereitstellen

**Bash:**
```bash
cd 01-introduction
azd up  # Wählen Sie ein Abonnement und einen Standort (eastus2 empfohlen)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Wählen Sie Abonnement und Standort (empfohlen: eastus2)
```

> **Hinweis:** Wenn Sie einen Timeout-Fehler erhalten (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), führen Sie einfach `azd up` erneut aus. Azure-Ressourcen werden möglicherweise noch im Hintergrund bereitgestellt, und der erneute Versuch ermöglicht den Abschluss der Bereitstellung, sobald die Ressourcen einen Endzustand erreicht haben.

Das bewirkt:
1. Bereitstellung der Azure OpenAI-Ressource mit GPT-5.2 und text-embedding-3-small-Modellen
2. Automatische Generierung der `.env`-Datei im Projektstamm mit Zugangsdaten
3. Einrichtung aller erforderlichen Umgebungsvariablen

**Probleme bei der Bereitstellung?** Siehe die [Infrastructure README](infra/README.md) für detaillierte Problemlösungen inklusive Subdomain-Namenskonflikte, manuelle Azure Portal-Bereitstellung und Modellkonfigurationsanleitungen.

**Bereitstellung überprüfen:**

**Bash:**
```bash
cat ../.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY usw. anzeigen.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY usw. anzeigen.
```

> **Hinweis:** Der Befehl `azd up` erstellt automatisch die `.env`-Datei. Wenn Sie diese später aktualisieren müssen, können Sie die Datei manuell bearbeiten oder sie neu generieren, indem Sie Folgendes ausführen:
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

Stellen Sie sicher, dass die `.env`-Datei mit Azure-Zugangsdaten im Stammverzeichnis vorhanden ist:

**Bash:**
```bash
cat ../.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT anzeigen
```

**Starten der Anwendungen:**

**Option 1: Verwendung des Spring Boot Dashboards (Empfohlen für VS Code-Nutzer)**

Der Devcontainer enthält die Erweiterung Spring Boot Dashboard, die eine visuelle Oberfläche zur Verwaltung aller Spring Boot-Anwendungen bietet. Sie finden sie in der Aktivitätsleiste auf der linken Seite von VS Code (suchen Sie das Spring Boot-Symbol).

Vom Spring Boot Dashboard aus können Sie:
- Alle verfügbaren Spring Boot-Anwendungen im Arbeitsbereich sehen
- Anwendungen mit einem Klick starten/stoppen
- Anwendungsprotokolle in Echtzeit anzeigen
- Anwendungsstatus überwachen

Klicken Sie einfach auf den Play-Button neben „introduction“, um dieses Modul zu starten, oder starten Sie alle Module auf einmal.

<img src="../../../translated_images/de/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

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
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
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

## Anwendung benutzen

Die Anwendung stellt eine Weboberfläche mit zwei Chat-Implementierungen nebeneinander bereit.

<img src="../../../translated_images/de/home-screen.121a03206ab910c0.webp" alt="Startbildschirm der Anwendung" width="800"/>

*Dashboard zeigt sowohl Simple Chat (zustandslos) als auch Conversational Chat (zustandsbehaftet)*

### Zustandsloser Chat (linkes Panel)

Probieren Sie dies zuerst. Sagen Sie „Mein Name ist John“ und fragen Sie dann sofort „Wie heiße ich?“ Das Modell erinnert sich nicht, da jede Nachricht unabhängig ist. Dies zeigt das Kernproblem einer einfachen Sprachmodell-Integration – kein Gesprächskontext.

<img src="../../../translated_images/de/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo Zustandsloser Chat" width="800"/>

*Die KI erinnert sich nicht an Ihren Namen aus der vorherigen Nachricht*

### Zustandsbehafteter Chat (rechtes Panel)

Probieren Sie nun dieselbe Sequenz hier aus. Sagen Sie „Mein Name ist John“ und dann „Wie heiße ich?“ Diesmal erinnert es sich. Der Unterschied ist MessageWindowChatMemory – es führt Gesprächshistorie und bezieht sie in jede Anfrage mit ein. So funktioniert konversationelle KI in der Produktion.

<img src="../../../translated_images/de/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo Zustandsbehafteter Chat" width="800"/>

*Die KI erinnert sich an Ihren Namen aus dem früheren Gespräch*

Beide Panels verwenden dasselbe GPT-5.2-Modell. Der einzige Unterschied ist der Speicher. Dies macht klar, welchen Nutzen Speicher für Ihre Anwendung bringt und warum er für reale Anwendungsfälle unerlässlich ist.

## Nächste Schritte

**Nächstes Modul:** [02-prompt-engineering – Prompt Engineering mit GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigation:** [← Vorheriges: Modul 00 – Schnellstart](../00-quick-start/README.md) | [Zurück zur Übersicht](../README.md) | [Weiter: Modul 02 – Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, bitten wir zu beachten, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Ursprungssprache gilt als maßgebliche Quelle. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Verwendung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->