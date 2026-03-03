# LangChain4j Glossar

## Inhaltsverzeichnis

- [Kernkonzepte](../../../docs)
- [LangChain4j-Komponenten](../../../docs)
- [KI/ML-Konzepte](../../../docs)
- [Guardrails](../../../docs)
- [Prompt-Engineering](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agenten und Tools](../../../docs)
- [Agentisches Modul](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Azure-Dienste](../../../docs)
- [Testen und Entwicklung](../../../docs)

Schnelle Referenz für Begriffe und Konzepte, die im gesamten Kurs verwendet werden.

## Kernkonzepte

**KI-Agent** – System, das KI verwendet, um autonom zu denken und zu handeln. [Modul 04](../04-tools/README.md)

**Chain** – Abfolge von Operationen, bei der die Ausgabe in den nächsten Schritt eingeht.

**Chunking** – Aufteilen von Dokumenten in kleinere Stücke. Typisch: 300–500 Tokens mit Überlappung. [Modul 03](../03-rag/README.md)

**Kontextfenster** – Maximale Tokens, die ein Modell verarbeiten kann. GPT-5.2: 400K Tokens (bis zu 272K Eingabe, 128K Ausgabe).

**Embeddings** – Numerische Vektoren, die die Bedeutung von Text repräsentieren. [Modul 03](../03-rag/README.md)

**Funktionsaufruf** – Modell erzeugt strukturierte Anfragen, um externe Funktionen aufzurufen. [Modul 04](../04-tools/README.md)

**Halluzination** – Wenn Modelle falsche, aber plausible Informationen generieren.

**Prompt** – Texteingabe an ein Sprachmodell. [Modul 02](../02-prompt-engineering/README.md)

**Semantische Suche** – Suche nach Bedeutung mittels Embeddings, nicht Schlüsselwörtern. [Modul 03](../03-rag/README.md)

**Zustandsbehaftet vs. zustandslos** – Zustandslos: kein Gedächtnis. Zustandsbehaftet: speichert Gesprächshistorie. [Modul 01](../01-introduction/README.md)

**Tokens** – Grundlegende Textbausteine, die Modelle verarbeiten. Beeinflussen Kosten und Limits. [Modul 01](../01-introduction/README.md)

**Tool Chaining** – Sequenzielle Ausführung von Tools, wobei Ausgabe den nächsten Aufruf informiert. [Modul 04](../04-tools/README.md)

## LangChain4j-Komponenten

**AiServices** – Erstellt typsichere KI-Service-Interfaces.

**OpenAiOfficialChatModel** – Einheitlicher Client für OpenAI- und Azure OpenAI-Modelle.

**OpenAiOfficialEmbeddingModel** – Erzeugt Embeddings mit OpenAI Official Client (unterstützt OpenAI und Azure OpenAI).

**ChatModel** – Kerninterface für Sprachmodelle.

**ChatMemory** – Speichert Gesprächshistorie.

**ContentRetriever** – Findet relevante Dokumentabschnitte für RAG.

**DocumentSplitter** – Teilt Dokumente in Abschnitte auf.

**EmbeddingModel** – Wandelt Text in numerische Vektoren um.

**EmbeddingStore** – Speichert und ruft Embeddings ab.

**MessageWindowChatMemory** – Pflegt ein gleitendes Fenster der letzten Nachrichten.

**PromptTemplate** – Erstellt wiederverwendbare Prompts mit `{{variable}}`-Platzhaltern.

**TextSegment** – Textabschnitt mit Metadaten. Verwendet in RAG.

**ToolExecutionRequest** – Repräsentiert eine Tool-Ausführungsanfrage.

**UserMessage / AiMessage / SystemMessage** – Nachrichtentypen im Gespräch.

## KI/ML-Konzepte

**Few-Shot Learning** – Beispiele in Prompts bereitstellen. [Modul 02](../02-prompt-engineering/README.md)

**Großes Sprachmodell (LLM)** – KI-Modelle, die auf umfangreichen Textdaten trainiert sind.

**Reasoning Effort** – GPT-5.2-Parameter zur Steuerung der Denktiefe. [Modul 02](../02-prompt-engineering/README.md)

**Temperatur** – Steuert Zufälligkeit der Ausgabe. Niedrig=deterministisch, hoch=kreativ.

**Vektordatenbank** – Spezialisierte Datenbank für Embeddings. [Modul 03](../03-rag/README.md)

**Zero-Shot Learning** – Aufgaben ohne Beispiele ausführen. [Modul 02](../02-prompt-engineering/README.md)

## Guardrails - [Modul 00](../00-quick-start/README.md)

**Defense in Depth** – Mehrschichtiger Sicherheitsansatz, der Anwendungsebene-Guardrails mit Sicherheitfiltern des Anbieters kombiniert.

**Hard Block** – Anbieter wirft HTTP-400-Fehler bei schweren Inhaltsverstößen.

**InputGuardrail** – LangChain4j-Interface zur Validierung von Benutzereingaben vor dem Erreichen des LLM. Spart Kosten und Latenz durch frühes Sperren schädlicher Prompts.

**InputGuardrailResult** – Rückgabetyp für Guardrail-Validierung: `success()` oder `fatal("reason")`.

**OutputGuardrail** – Interface zur Validierung von KI-Antworten vor der Rückgabe an Nutzer.

**Sicherheitsfilter des Anbieters** – Eingebaute Inhaltsfilter von KI-Anbietern (z.B. GitHub Models), die Verstöße auf API-Ebene erfassen.

**Soft Refusal** – Modell lehnt höflich ab, ohne Fehler zu werfen.

## Prompt-Engineering - [Modul 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** – Schrittweises Denken für bessere Genauigkeit.

**Eingeschränkte Ausgabe** – Erzwingung eines bestimmten Formats oder einer Struktur.

**Hohe Nachdrücklichkeit** – GPT-5.2-Muster für gründliches Denken.

**Niedrige Nachdrücklichkeit** – GPT-5.2-Muster für schnelle Antworten.

**Multi-Turn-Konversation** – Kontext über mehrere Austausch hinweg aufrechterhalten.

**Rollenspezifisches Prompting** – Modellpersona über Systemnachrichten festlegen.

**Selbstreflexion** – Modell bewertet und verbessert seine Ausgabe.

**Strukturierte Analyse** – Festes Bewertungsframework.

**Task Execution Pattern** – Planen → Ausführen → Zusammenfassen.

## RAG (Retrieval-Augmented Generation) - [Modul 03](../03-rag/README.md)

**Dokumentenverarbeitungspipeline** – Laden → Aufteilen → Einbetten → Speichern.

**In-Memory Embedding Store** – Nicht persistenter Speicher zum Testen.

**RAG** – Kombination aus Abruf und Generierung zur Fundierung von Antworten.

**Ähnlichkeitsscore** – Maß (0–1) für semantische Ähnlichkeit.

**Quellenverweis** – Metadaten zu abgerufenen Inhalten.

## Agenten und Tools - [Modul 04](../04-tools/README.md)

**@Tool Annotation** – Kennzeichnet Java-Methoden als KI-aufrufbare Tools.

**ReAct Pattern** – Reason → Act → Observe → Repeat.

**Sitzungsverwaltung** – Separate Kontexte für verschiedene Nutzer.

**Tool** – Funktion, die ein KI-Agent aufrufen kann.

**Toolbeschreibung** – Dokumentation von Zweck und Parametern eines Tools.

## Agentisches Modul - [Modul 05](../05-mcp/README.md)

**@Agent Annotation** – Kennzeichnet Interfaces als KI-Agenten mit deklarativer Verhaltensdefinition.

**Agent Listener** – Hook zur Überwachung der Agentenausführung via `beforeAgentInvocation()` und `afterAgentInvocation()`.

**Agentic Scope** – Gemeinsamer Speicher, in dem Agenten Ausgaben mit `outputKey` speichern, damit nachfolgende Agenten sie konsumieren können.

**AgenticServices** – Fabrik zur Erstellung von Agenten mittels `agentBuilder()` und `supervisorBuilder()`.

**Conditional Workflow** – Route basierend auf Bedingungen zu verschiedenen Spezialagenten.

**Human-in-the-Loop** – Workflow-Muster mit menschlichen Kontrollpunkten zur Genehmigung oder Inhaltsprüfung.

**langchain4j-agentic** – Maven-Abhängigkeit für deklarativen Agentenbau (experimentell).

**Loop Workflow** – Wiederholte Agentenausführung, bis eine Bedingung erfüllt ist (z.B. Qualitätswert ≥ 0,8).

**outputKey** – Agenten-Annotierungsparameter, der angibt, wo Ergebnisse im Agentic Scope gespeichert werden.

**Parallel Workflow** – Gleichzeitiges Ausführen mehrerer Agenten für unabhängige Aufgaben.

**Response Strategy** – Wie der Supervisor die finale Antwort formuliert: LAST, SUMMARY oder SCORED.

**Sequential Workflow** – Agenten in Reihenfolge ausführen, bei der Ausgabe zum nächsten Schritt fließt.

**Supervisor Agent Pattern** – Fortgeschrittenes agentisches Muster, bei dem ein Supervisor-LLM dynamisch entscheidet, welche Unteragenten aufgerufen werden.

## Model Context Protocol (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** – Maven-Abhängigkeit für MCP-Integration in LangChain4j.

**MCP** – Model Context Protocol: Standard für die Verbindung von KI-Apps mit externen Tools. Einmal bauen, überall verwenden.

**MCP Client** – Anwendung, die sich mit MCP-Servern verbindet, um Tools zu entdecken und zu nutzen.

**MCP Server** – Dienst, der Tools über MCP mit klaren Beschreibungen und Parameterschemata bereitstellt.

**McpToolProvider** – LangChain4j-Komponente, die MCP-Tools für KI-Dienste und Agenten einbindet.

**McpTransport** – Interface für MCP-Kommunikation. Implementierungen umfassen Stdio und HTTP.

**Stdio Transport** – Lokaler Prozess-Transport via stdin/stdout. Nützlich für Dateisystemzugriff oder Kommandozeilentools.

**StdioMcpTransport** – LangChain4j-Implementation, die MCP-Server als Nebenprozess startet.

**Tool Discovery** – Client fragt Server nach verfügbaren Tools mit Beschreibungen und Schemata ab.

## Azure-Dienste - [Modul 01](../01-introduction/README.md)

**Azure AI Search** – Cloud-Suche mit Vektorfähigkeiten. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** – Stellt Azure-Ressourcen bereit.

**Azure OpenAI** – Microsofts Unternehmens-KI-Dienst.

**Bicep** – Azure-Infrastructure-as-Code-Sprache. [Infrastructure Guide](../01-introduction/infra/README.md)

**Deployment-Name** – Name für Modellausrollung in Azure.

**GPT-5.2** – Neuestes OpenAI-Modell mit Steuerung der Denkprozesse. [Modul 02](../02-prompt-engineering/README.md)

## Testen und Entwicklung - [Testing Guide](TESTING.md)

**Dev Container** – Containerisierte Entwicklungsumgebung. [Konfiguration](../../../.devcontainer/devcontainer.json)

**GitHub Models** – Kostenlose KI-Modellspielwiese. [Modul 00](../00-quick-start/README.md)

**In-Memory Testing** – Testen mit In-Memory-Speicher.

**Integration Testing** – Testen mit realer Infrastruktur.

**Maven** – Java-Build-Automatisierungswerkzeug.

**Mockito** – Java-Mocking-Framework.

**Spring Boot** – Java-Anwendungsframework. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, kann es bei automatischen Übersetzungen zu Fehlern oder Ungenauigkeiten kommen. Das Originaldokument in seiner Ursprungssprache ist als maßgebliche Quelle anzusehen. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Verwendung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->