# LangChain4j Glossar

## Inhaltsverzeichnis

- [Kernkonzepte](../../../docs)
- [LangChain4j-Komponenten](../../../docs)
- [AI/ML-Konzepte](../../../docs)
- [Sicherheitsmaßnahmen](../../../docs)
- [Prompt Engineering](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agenten und Werkzeuge](../../../docs)
- [Agentisches Modul](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Azure-Dienste](../../../docs)
- [Testen und Entwicklung](../../../docs)

Kurze Referenz für Begriffe und Konzepte, die im gesamten Kurs verwendet werden.

## Kernkonzepte

**AI Agent** – System, das KI nutzt, um autonom zu denken und zu handeln. [Modul 04](../04-tools/README.md)

**Chain** – Verkettung von Operationen, bei der die Ausgabe in den nächsten Schritt fließt.

**Chunking** – Aufteilung von Dokumenten in kleinere Stücke. Typisch: 300-500 Tokens mit Überlappung. [Modul 03](../03-rag/README.md)

**Kontextfenster** – Maximale Anzahl an Tokens, die ein Modell verarbeiten kann. GPT-5.2: 400K Tokens.

**Embeddings** – Numerische Vektoren, die die Bedeutung von Text darstellen. [Modul 03](../03-rag/README.md)

**Funktionsaufruf** – Modell erzeugt strukturierte Anfragen, um externe Funktionen aufzurufen. [Modul 04](../04-tools/README.md)

**Halluzination** – Wenn Modelle inkorrekte, aber plausible Informationen generieren.

**Prompt** – Texteingabe an ein Sprachmodell. [Modul 02](../02-prompt-engineering/README.md)

**Semantische Suche** – Suche basierend auf Bedeutung durch Embeddings, nicht Schlüsselwörter. [Modul 03](../03-rag/README.md)

**Zustandsorientiert vs. Zustandslos** – Zustandslos: kein Gedächtnis. Zustandsorientiert: pflegt Gesprächshistorie. [Modul 01](../01-introduction/README.md)

**Tokens** – Grundlegende Text-Einheiten, die Modelle verarbeiten. Beeinflussen Kosten und Limits. [Modul 01](../01-introduction/README.md)

**Werkzeug-Verkettung** – Sequenzielle Ausführung von Werkzeugen, wobei die Ausgabe den nächsten Aufruf informiert. [Modul 04](../04-tools/README.md)

## LangChain4j-Komponenten

**AiServices** – Erzeugt typsichere KI-Dienste-Schnittstellen.

**OpenAiOfficialChatModel** – Vereinheitlichter Client für OpenAI- und Azure OpenAI-Modelle.

**OpenAiOfficialEmbeddingModel** – Erzeugt Embeddings mit dem offiziellen OpenAI-Client (unterstützt sowohl OpenAI als auch Azure OpenAI).

**ChatModel** – Kernschnittstelle für Sprachmodelle.

**ChatMemory** – Pflegt Gesprächshistorie.

**ContentRetriever** – Findet relevante Dokumentausschnitte für RAG.

**DocumentSplitter** – Teilt Dokumente in Fragmente.

**EmbeddingModel** – Wandelt Text in numerische Vektoren um.

**EmbeddingStore** – Speichert und ruft Embeddings ab.

**MessageWindowChatMemory** – Pflegt ein gleitendes Fenster aktueller Nachrichten.

**PromptTemplate** – Erstellt wiederverwendbare Prompts mit `{{variable}}` Platzhaltern.

**TextSegment** – Textstück mit Metadaten. Wird in RAG verwendet.

**ToolExecutionRequest** – Repräsentiert Werkzeugausführungsanforderung.

**UserMessage / AiMessage / SystemMessage** – Nachrichtentypen im Gespräch.

## AI/ML-Konzepte

**Few-Shot Learning** – Beispiele in Prompts bereitstellen. [Modul 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** – KI-Modelle, trainiert auf großen Textmengen.

**Reasoning Effort** – GPT-5.2 Parameter zur Steuerung der Denk-Tiefe. [Modul 02](../02-prompt-engineering/README.md)

**Temperatur** – Steuert Zufälligkeit der Ausgabe. Niedrig=deterministisch, hoch=kreativ.

**Vector Database** – Spezialisierte Datenbank für Embeddings. [Modul 03](../03-rag/README.md)

**Zero-Shot Learning** – Aufgaben ohne Beispiele ausführen. [Modul 02](../02-prompt-engineering/README.md)

## Sicherheitsmaßnahmen - [Modul 00](../00-quick-start/README.md)

**Defense in Depth** – Mehrschichtiger Sicherheitsansatz, der Anwendungsschutzmaßnahmen mit den Sicherheitsfiltern des Anbieters kombiniert.

**Hard Block** – Anbieter wirft HTTP 400 Fehler bei schwerwiegenden Inhaltsverletzungen.

**InputGuardrail** – LangChain4j-Schnittstelle zur Validierung der Benutzereingabe, bevor sie das LLM erreicht. Spart Kosten und Latenz durch frühzeitiges Blockieren schädlicher Prompts.

**InputGuardrailResult** – Rückgabetyp für Guardrail-Validierung: `success()` oder `fatal("Grund")`.

**OutputGuardrail** – Schnittstelle zur Validierung von KI-Antworten vor der Rückgabe an den Nutzer.

**Provider Safety Filters** – Eingebaute Inhaltsfilter von KI-Anbietern (z.B. GitHub-Modelle), die Verstöße auf API-Ebene abfangen.

**Soft Refusal** – Modell lehnt höflich ab zu antworten, ohne einen Fehler zu werfen.

## Prompt Engineering - [Modul 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** – Schrittweises Denken für bessere Genauigkeit.

**Begrenzte Ausgabe** – Durchsetzen eines bestimmten Formats oder einer Struktur.

**Hohe Eifer** – GPT-5.2 Muster für gründliches Nachdenken.

**Niedrige Eifer** – GPT-5.2 Muster für schnelle Antworten.

**Multi-Turn Conversation** – Kontext über mehrere Austausche aufrechterhalten.

**Rollenbasiertes Prompting** – Modell-Persona über Systemnachrichten festlegen.

**Selbstreflexion** – Modell bewertet und verbessert seine Ausgabe.

**Strukturierte Analyse** – Fester Bewertungsrahmen.

**Aufgaben-Ausführungsmuster** – Planen → Ausführen → Zusammenfassen.

## RAG (Retrieval-Augmented Generation) - [Modul 03](../03-rag/README.md)

**Dokumenten-Verarbeitungspipeline** – Laden → fragmentieren → einbetten → speichern.

**In-Memory Embedding Store** – Nicht persistenter Speicher für Tests.

**RAG** – Kombination von Abruf und Generierung zur Absicherung von Antworten.

**Ähnlichkeitsscore** – Maß (0-1) der semantischen Ähnlichkeit.

**Quellenreferenz** – Metadaten über abgerufene Inhalte.

## Agenten und Werkzeuge - [Modul 04](../04-tools/README.md)

**@Tool Annotation** – Markiert Java-Methoden als KI-aufgerufene Werkzeuge.

**ReAct-Muster** – Denken → Handeln → Beobachten → Wiederholen.

**Sitzungsmanagement** – Separate Kontexte für verschiedene Nutzer.

**Werkzeug** – Funktion, die ein KI-Agent aufrufen kann.

**Werkzeugbeschreibung** – Dokumentation des Werkzeugzwecks und der Parameter.

## Agentisches Modul - [Modul 05](../05-mcp/README.md)

**@Agent Annotation** – Markiert Schnittstellen als KI-Agenten mit deklarativer Verhaltensdefinition.

**Agent Listener** – Hook zur Überwachung der Agentenausführung über `beforeAgentInvocation()` und `afterAgentInvocation()`.

**Agentic Scope** – Gemeinsamer Speicher, in dem Agenten Ausgaben mit `outputKey` speichern, damit nachgelagerte Agenten sie nutzen können.

**AgenticServices** – Fabrik zur Erstellung von Agenten mittels `agentBuilder()` und `supervisorBuilder()`.

**Bedingter Ablauf** – Routing basierend auf Bedingungen zu verschiedenen Spezialagenten.

**Human-in-the-Loop** – Ablaufmuster mit menschlichen Kontrollpunkten für Freigabe oder Inhaltsprüfung.

**langchain4j-agentic** – Maven-Abhängigkeit für deklaratives Agenten-Building (experimentell).

**Loop Workflow** – Agentenausführung iterieren, bis eine Bedingung erfüllt ist (z.B. Qualitätswert ≥ 0,8).

**outputKey** – Agenten-Annotation-Parameter zum Speichern der Ergebnisse im Agentic Scope.

**Paralleler Workflow** – Mehrere Agenten simultan für unabhängige Aufgaben ausführen.

**Antwortstrategie** – Wie der Supervisor die finale Antwort formuliert: LAST, SUMMARY oder SCORED.

**Sequenzieller Workflow** – Agenten in Reihenfolge ausführen, wobei die Ausgabe zum nächsten Schritt fließt.

**Supervisor Agent Pattern** – Fortgeschrittenes agentisches Muster, bei dem ein Supervisor-LLM dynamisch entscheidet, welche Sub-Agenten aufgerufen werden.

## Model Context Protocol (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** – Maven-Abhängigkeit für MCP-Integration in LangChain4j.

**MCP** – Model Context Protocol: Standard zur Verbindung von KI-Anwendungen mit externen Werkzeugen. Einmal bauen, überall nutzen.

**MCP Client** – Anwendung, die sich mit MCP-Servern verbindet, um Werkzeuge zu entdecken und zu nutzen.

**MCP Server** – Dienst, der Werkzeuge über MCP mit klaren Beschreibungen und Parameterschemata bereitstellt.

**McpToolProvider** – LangChain4j-Komponente, die MCP-Werkzeuge für AI-Dienste und Agenten kapselt.

**McpTransport** – Schnittstelle für MCP-Kommunikation. Implementierungen umfassen Stdio und HTTP.

**Stdio Transport** – Lokaler Prozess-Transport über stdin/stdout. Nützlich für Dateisystemzugriff oder Kommandozeilenwerkzeuge.

**StdioMcpTransport** – LangChain4j-Implementierung, die MCP-Server als Unterprozess startet.

**Werkzeug-Erkennung** – Client fragt Server nach verfügbaren Werkzeugen mit Beschreibungen und Schemata ab.

## Azure-Dienste - [Modul 01](../01-introduction/README.md)

**Azure AI Search** – Cloud-Suche mit Vektorfunktionalität. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** – Stellt Azure-Ressourcen bereit.

**Azure OpenAI** – Microsofts Unternehmens-KI-Dienst.

**Bicep** – Azure Infrastruktur-als-Code Sprache. [Infrastruktur-Anleitung](../01-introduction/infra/README.md)

**Bereitstellungsname** – Name für Modell-Bereitstellung in Azure.

**GPT-5.2** – Aktuellstes OpenAI-Modell mit Steuerung des Denkprozesses. [Modul 02](../02-prompt-engineering/README.md)

## Testen und Entwicklung - [Testanleitung](TESTING.md)

**Dev Container** – Containerisierte Entwicklungsumgebung. [Konfiguration](../../../.devcontainer/devcontainer.json)

**GitHub-Modelle** – Kostenloser KI-Modellspielplatz. [Modul 00](../00-quick-start/README.md)

**In-Memory Testing** – Tests mit In-Memory-Speicher.

**Integrationstests** – Tests mit echter Infrastruktur.

**Maven** – Java-Build-Automatisierungstool.

**Mockito** – Java-Mocking-Framework.

**Spring Boot** – Java-Anwendungsframework. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mithilfe des KI-Übersetzungsdienstes [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir auf Genauigkeit achten, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Ursprache gilt als maßgebliche Quelle. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Nutzung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->