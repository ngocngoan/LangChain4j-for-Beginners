# Modul 01: Einstieg mit LangChain4j

## Inhaltsverzeichnis

- [Was Sie lernen werden](../../../01-introduction)
- [Voraussetzungen](../../../01-introduction)
- [Das Kernproblem verstehen](../../../01-introduction)
- [Tokens verstehen](../../../01-introduction)
- [Wie Speicher funktioniert](../../../01-introduction)
- [Wie dies LangChain4j nutzt](../../../01-introduction)
- [Azure OpenAI Infrastruktur bereitstellen](../../../01-introduction)
- [Die Anwendung lokal ausführen](../../../01-introduction)
- [Die Anwendung nutzen](../../../01-introduction)
  - [Zustandsloser Chat (linkes Panel)](../../../01-introduction)
  - [Zustandsbehafteter Chat (rechtes Panel)](../../../01-introduction)
- [Nächste Schritte](../../../01-introduction)

## Was Sie lernen werden

Wenn Sie den Schnellstart abgeschlossen haben, haben Sie gesehen, wie man Prompts sendet und Antworten erhält. Das ist die Grundlage, aber echte Anwendungen benötigen mehr. Dieses Modul zeigt Ihnen, wie Sie konversationelle KI bauen, die den Kontext behält und den Zustand verwaltet – der Unterschied zwischen einer einmaligen Demo und einer produktionsreifen Anwendung.

Wir verwenden im gesamten Leitfaden Azure OpenAI's GPT-5.2, da seine erweiterten Denkfähigkeiten das Verhalten der verschiedenen Muster deutlicher machen. Wenn Sie Speicher hinzufügen, sehen Sie den Unterschied klar. Das erleichtert das Verständnis, was jede Komponente zu Ihrer Anwendung beiträgt.

Sie werden eine Anwendung bauen, die beide Muster demonstriert:

**Zustandsloser Chat** – Jede Anfrage ist eigenständig. Das Modell erinnert sich nicht an vorherige Nachrichten. Dies ist das Muster, das Sie im Schnellstart verwendet haben.

**Zustandsbehaftete Konversation** – Jede Anfrage beinhaltet den Gesprächsverlauf. Das Modell behält den Kontext über mehrere Runden hinweg. Dies ist das, was Produktionsanwendungen benötigen.

## Voraussetzungen

- Azure-Abonnement mit Azure OpenAI Zugriff
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Hinweis:** Java, Maven, Azure CLI und Azure Developer CLI (azd) sind im bereitgestellten Devcontainer vorinstalliert.

> **Hinweis:** Dieses Modul verwendet GPT-5.2 auf Azure OpenAI. Die Bereitstellung wird automatisch über `azd up` konfiguriert – ändern Sie den Modellnamen im Code nicht.

## Das Kernproblem verstehen

Sprachmodelle sind zustandslos. Jeder API-Aufruf ist unabhängig. Wenn Sie "Mein Name ist John" senden und dann fragen "Wie heiße ich?" weiß das Modell nicht, dass Sie sich gerade vorgestellt haben. Es behandelt jede Anfrage, als ob es das erste Gespräch wäre, das Sie jemals geführt haben.

Das ist für einfache Fragen und Antworten in Ordnung, aber für echte Anwendungen nutzlos. Kundenservice-Bots müssen sich erinnern, was Sie ihnen erzählt haben. Persönliche Assistenten brauchen Kontext. Jede mehrschrittige Konversation benötigt Speicher.

<img src="../../../translated_images/de/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Zustandslose vs. zustandsbehaftete Gespräche" width="800"/>

*Der Unterschied zwischen zustandslosen (unabhängigen Aufrufen) und zustandsbehafteten (kontextbewussten) Gesprächen*

## Tokens verstehen

Bevor wir uns Konversationen widmen, ist es wichtig, Tokens zu verstehen – die Grundeinheiten des Textes, die Sprachmodelle verarbeiten:

<img src="../../../translated_images/de/token-explanation.c39760d8ec650181.webp" alt="Token-Erklärung" width="800"/>

*Beispiel, wie Text in Tokens zerlegt wird – „I love AI!“ wird zu 4 einzelnen Verarbeitungseinheiten*

Tokens sind das Maß, mit dem KI-Modelle Text verarbeiten. Wörter, Satzzeichen und sogar Leerzeichen können Tokens sein. Ihr Modell hat eine Grenze dafür, wie viele Tokens es auf einmal verarbeiten kann (400.000 für GPT-5.2, mit bis zu 272.000 Eingabe- und 128.000 Ausgabe-Tokens). Das Verständnis von Tokens hilft Ihnen, die Länge und Kosten von Konversationen zu managen.

## Wie Speicher funktioniert

Chat-Speicher löst das zustandslose Problem, indem er den Gesprächsverlauf erhält. Bevor eine Anfrage an das Modell gesendet wird, fügt das Framework relevante vorherige Nachrichten hinzu. Wenn Sie fragen "Wie heiße ich?", schickt das System tatsächlich den gesamten Gesprächsverlauf, sodass das Modell sehen kann, dass Sie zuvor sagten "Mein Name ist John."

LangChain4j stellt Speicher-Implementierungen bereit, die dies automatisch handhaben. Sie wählen, wie viele Nachrichten gespeichert werden, und das Framework verwaltet das Kontextfenster.

<img src="../../../translated_images/de/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory verwaltet ein gleitendes Fenster der letzten Nachrichten und entfernt automatisch alte*

## Wie dies LangChain4j nutzt

Dieses Modul erweitert den Schnellstart durch Integration von Spring Boot und Hinzufügen von Gesprächsspeicher. So fügen sich die Teile zusammen:

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
  
Der Builder liest Zugangsdaten aus Umgebungsvariablen, die von `azd up` gesetzt werden. Das Setzen von `baseUrl` auf Ihren Azure-Endpunkt erlaubt dem OpenAI-Client, mit Azure OpenAI zu arbeiten.

**Gesprächsspeicher** – Verfolgen Sie den Chatverlauf mit MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```
  
Erstellen Sie Speicher mit `withMaxMessages(10)`, um die letzten 10 Nachrichten zu behalten. Fügen Sie Benutzer- und AI-Nachrichten mit typisierten Wrappern hinzu: `UserMessage.from(text)` und `AiMessage.from(text)`. Rufen Sie den Verlauf mit `memory.messages()` ab und senden Sie ihn an das Modell. Der Service speichert separate Speichervarianten pro Konversations-ID, sodass mehrere Benutzer gleichzeitig chatten können.

> **🤖 Probieren Sie es mit [GitHub Copilot](https://github.com/features/copilot) Chat aus:** Öffnen Sie [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) und fragen Sie:
> - "Wie entscheidet MessageWindowChatMemory, welche Nachrichten entfernt werden, wenn das Fenster voll ist?"
> - "Kann ich eigenen Speicher unter Verwendung einer Datenbank anstelle von In-Memory implementieren?"
> - "Wie würde ich eine Zusammenfassung hinzufügen, um alten Gesprächsverlauf zu komprimieren?"

Der zustandslose Chat-Endpunkt überspringt Speicher vollständig – einfach `chatModel.chat(prompt)` wie im Schnellstart. Der zustandsbehaftete Endpunkt fügt Nachrichten dem Speicher hinzu, ruft den Verlauf ab und inkludiert diesen Kontext bei jeder Anfrage. Gleiche Modellkonfiguration, unterschiedliche Muster.

## Azure OpenAI Infrastruktur bereitstellen

**Bash:**  
```bash
cd 01-introduction
azd up  # Abonnement und Standort auswählen (eastus2 empfohlen)
```
  
**PowerShell:**  
```powershell
cd 01-introduction
azd up  # Abonnement und Standort auswählen (eastus2 empfohlen)
```
  
> **Hinweis:** Wenn Sie einen Timeout-Fehler erhalten (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), führen Sie einfach `azd up` erneut aus. Azure-Ressourcen können noch im Hintergrund bereitgestellt werden und eine Wiederholung erlaubt, die Bereitstellung abzuschließen, sobald die Ressourcen einen terminalen Zustand erreichen.

Dies wird:  
1. Azure OpenAI-Ressource mit GPT-5.2 und text-embedding-3-small Modellen bereitstellen  
2. Automatisch eine `.env` Datei im Projektstamm mit Zugangsdaten generieren  
3. Alle benötigten Umgebungsvariablen konfigurieren

**Probleme bei der Bereitstellung?** Siehe die [Infrastructure README](infra/README.md) für detaillierte Fehlersuche, einschließlich Konflikte von Subdomainnamen, manuelle Azure-Portal-Bereitstellungsschritte und Hinweise zur Modellkonfiguration.

**Verifizieren Sie die erfolgreiche Bereitstellung:**

**Bash:**  
```bash
cat ../.env  # Soll AZURE_OPENAI_ENDPOINT, API_KEY usw. anzeigen.
```
  
**PowerShell:**  
```powershell
Get-Content ..\.env  # Sollte AZURE_OPENAI_ENDPOINT, API_KEY usw. anzeigen.
```
  
> **Hinweis:** Der Befehl `azd up` erstellt automatisch die `.env` Datei. Wenn Sie diese später aktualisieren müssen, können Sie entweder die `.env` Datei manuell bearbeiten oder sie durch Ausführen folgendes Befehls neu generieren:  
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
  

## Die Anwendung lokal ausführen

**Verifizieren Sie die Bereitstellung:**  

Stellen Sie sicher, dass die `.env` Datei im Stammverzeichnis mit Azure-Zugangsdaten existiert:

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

Der Devcontainer beinhaltet die Erweiterung Spring Boot Dashboard, welche eine visuelle Oberfläche zur Verwaltung aller Spring Boot Anwendungen bietet. Sie finden sie in der Aktivitätsleiste links in VS Code (suchen Sie nach dem Spring Boot Symbol).

Im Spring Boot Dashboard können Sie:  
- Alle verfügbaren Spring Boot Anwendungen im Workspace sehen  
- Anwendungen mit einem Klick starten/stoppen  
- Anwendungsprotokolle in Echtzeit ansehen  
- Den Status der Anwendung überwachen  

Klicken Sie einfach auf den Play-Button neben „introduction“, um dieses Modul zu starten, oder starten Sie alle Module auf einmal.

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
  

Beide Skripte laden automatisch Umgebungsvariablen aus der `.env` Datei im Stammverzeichnis und bauen die JAR-Dateien, falls sie noch nicht existieren.

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
  

## Die Anwendung nutzen

Die Anwendung bietet eine Weboberfläche mit zwei Chat-Implementierungen nebeneinander.

<img src="../../../translated_images/de/home-screen.121a03206ab910c0.webp" alt="Startbildschirm der Anwendung" width="800"/>

*Dashboard zeigt sowohl Simple Chat (zustandslos) als auch Conversational Chat (zustandsbehaftet) Optionen*

### Zustandsloser Chat (linkes Panel)

Probieren Sie dies zuerst. Fragen Sie "Mein Name ist John" und dann sofort "Wie heiße ich?" Das Modell wird sich nicht erinnern, da jede Nachricht unabhängig ist. Das demonstriert das Kernproblem der einfachen Sprachmodell-Integration – kein Gesprächskontext.

<img src="../../../translated_images/de/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo zustandsloser Chat" width="800"/>

*KI erinnert sich nicht an Ihren Namen aus der vorherigen Nachricht*

### Zustandsbehafteter Chat (rechtes Panel)

Probieren Sie hier dieselbe Abfolge. Fragen Sie "Mein Name ist John" und dann "Wie heiße ich?" Dieses Mal erinnert es sich. Der Unterschied ist MessageWindowChatMemory – es hält den Gesprächsverlauf und fügt ihn jeder Anfrage hinzu. So funktioniert konversationelle KI in der Produktion.

<img src="../../../translated_images/de/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo zustandsbehafteter Chat" width="800"/>

*KI erinnert sich an Ihren Namen aus einem früheren Gesprächsteil*

Beide Panels verwenden dasselbe GPT-5.2 Modell. Der einzige Unterschied ist der Speicher. Das macht deutlich, was Speicher Ihrer Anwendung bringt und warum er für echte Anwendungsfälle essentiell ist.

## Nächste Schritte

**Nächstes Modul:** [02-prompt-engineering - Prompt Engineering mit GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigation:** [← Vorheriges: Modul 00 - Schnellstart](../00-quick-start/README.md) | [Zurück zum Hauptverzeichnis](../README.md) | [Nächstes: Modul 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, können automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten. Das ursprüngliche Dokument in der Originalsprache gilt als maßgebliche Quelle. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Verantwortung für Missverständnisse oder Fehlinterpretationen, die aus der Nutzung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->