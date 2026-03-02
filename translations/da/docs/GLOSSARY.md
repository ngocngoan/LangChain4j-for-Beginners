# LangChain4j Ordliste

## Indholdsfortegnelse

- [Kernekoncepter](../../../docs)
- [LangChain4j Komponenter](../../../docs)
- [AI/ML Koncepter](../../../docs)
- [Guardrails](../../../docs)
- [Prompt Engineering](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agenter og Værktøjer](../../../docs)
- [Agentic Modul](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Azure Services](../../../docs)
- [Testning og Udvikling](../../../docs)

Hurtig reference for termer og koncepter brugt gennem kurset.

## Kernekoncepter

**AI Agent** - System, der bruger AI til at resonnere og handle autonomt. [Modul 04](../04-tools/README.md)

**Chain** - Sekvens af operationer hvor output føres videre til næste trin.

**Chunking** - At opdele dokumenter i mindre stykker. Typisk: 300-500 tokens med overlap. [Modul 03](../03-rag/README.md)

**Context Window** - Maksimalt antal tokens et model kan behandle. GPT-5.2: 400K tokens (op til 272K input, 128K output).

**Embeddings** - Numeriske vektorer der repræsenterer teksts betydning. [Modul 03](../03-rag/README.md)

**Function Calling** - Model genererer strukturerede forespørgsler for at kalde eksterne funktioner. [Modul 04](../04-tools/README.md)

**Hallucination** - Når modeller genererer ukorrekte men plausible oplysninger.

**Prompt** - Tekstinput til en sprogmodel. [Modul 02](../02-prompt-engineering/README.md)

**Semantic Search** - Søgning baseret på mening ved hjælp af embeddings, ikke nøgleord. [Modul 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: ingen hukommelse. Stateful: bevarer samtalehistorik. [Modul 01](../01-introduction/README.md)

**Tokens** - Grundlæggende tekstenheder modeller behandler. Påvirker omkostninger og begrænsninger. [Modul 01](../01-introduction/README.md)

**Tool Chaining** - Sekventiel udførelse af værktøjer hvor output informerer næste kald. [Modul 04](../04-tools/README.md)

## LangChain4j Komponenter

**AiServices** - Opretter type-sikre AI service interfaces.

**OpenAiOfficialChatModel** - Fælles klient til OpenAI og Azure OpenAI modeller.

**OpenAiOfficialEmbeddingModel** - Opretter embeddings ved brug af OpenAI Official klient (understøtter både OpenAI og Azure OpenAI).

**ChatModel** - Kerneinterface til sprogmodeller.

**ChatMemory** - Bevarer samtalehistorik.

**ContentRetriever** - Finder relevante dokumentstykker til RAG.

**DocumentSplitter** - Opdeler dokumenter i stykker.

**EmbeddingModel** - Omformer tekst til numeriske vektorer.

**EmbeddingStore** - Gemmer og henter embeddings.

**MessageWindowChatMemory** - Bevarer glidende vindue af nylige beskeder.

**PromptTemplate** - Opretter genanvendelige prompts med `{{variable}}` pladsholdere.

**TextSegment** - Tekststykke med metadata. Bruges i RAG.

**ToolExecutionRequest** - Repræsenterer anmodning om udførelse af værktøj.

**UserMessage / AiMessage / SystemMessage** - Samtaletypemeddelelser.

## AI/ML Koncepter

**Few-Shot Learning** - At give eksempler i prompts. [Modul 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - AI modeller trænet på store mængder tekstdata.

**Reasoning Effort** - GPT-5.2 parameter der styrer tænkedybde. [Modul 02](../02-prompt-engineering/README.md)

**Temperature** - Styrer outputtilfældighed. Lav=deterministisk, høj=kreativ.

**Vector Database** - Specialiseret database til embeddings. [Modul 03](../03-rag/README.md)

**Zero-Shot Learning** - Udfør opgaver uden eksempler. [Modul 02](../02-prompt-engineering/README.md)

## Guardrails - [Modul 00](../00-quick-start/README.md)

**Defense in Depth** - Flerlags sikkerhedstilgang der kombinerer applikationsniveau guardrails med safety-filtre fra udbyder.

**Hard Block** - Udbyder returnerer HTTP 400 fejl for alvorlige indholdsbrud.

**InputGuardrail** - LangChain4j interface til validering af brugerinput, før det når LLM. Sparrer omkostninger og latenstid ved at blokere skadelige prompts tidligt.

**InputGuardrailResult** - Returtype for guardrail validering: `success()` eller `fatal("årsag")`.

**OutputGuardrail** - Interface til validering af AI-svar før de returneres til brugere.

**Provider Safety Filters** - Indbyggede indholdsfiltre fra AI-udbydere (f.eks. GitHub Models) der fanger overtrædelser på API-niveau.

**Soft Refusal** - Model afviser høfligt at svare uden at kaste fejl.

## Prompt Engineering - [Modul 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Trinvis tankegang for bedre præcision.

**Constrained Output** - Håndhæver specifikt format eller struktur.

**High Eagerness** - GPT-5.2 mønster for grundig tænkning.

**Low Eagerness** - GPT-5.2 mønster for hurtige svar.

**Multi-Turn Conversation** - Bevarer kontekst på tværs af udvekslinger.

**Role-Based Prompting** - Indstiller modelperson via systembeskeder.

**Self-Reflection** - Model evaluerer og forbedrer sit output.

**Structured Analysis** - Fast evalueringsramme.

**Task Execution Pattern** - Planlæg → Udfør → Opsummér.

## RAG (Retrieval-Augmented Generation) - [Modul 03](../03-rag/README.md)

**Document Processing Pipeline** - Indlæs → del i stykker → embed → gem.

**In-Memory Embedding Store** - Ikke-persistent lager til test.

**RAG** - Kombinerer genfinding med generering for at underbygge svar.

**Similarity Score** - Mål (0-1) for semantisk lighed.

**Source Reference** - Metadata om hentet indhold.

## Agenter og Værktøjer - [Modul 04](../04-tools/README.md)

**@Tool Annotation** - Marker Java metoder som AI-kaldbare værktøjer.

**ReAct Pattern** - Resonner → Handl → Observer → Gentag.

**Session Management** - Separate kontekster til forskellige brugere.

**Tool** - Funktion som en AI agent kan kalde.

**Tool Description** - Dokumentation af værktøjets formål og parametre.

## Agentic Modul - [Modul 05](../05-mcp/README.md)

**@Agent Annotation** - Marker interfaces som AI agenter med deklarativ adfærdsdefinition.

**Agent Listener** - Hook til overvågning af agentudførelse via `beforeAgentInvocation()` og `afterAgentInvocation()`.

**Agentic Scope** - Delt hukommelse hvor agenter gemmer output med `outputKey` til videre forbrug af andre agenter.

**AgenticServices** - Fabrik til oprettelse af agenter via `agentBuilder()` og `supervisorBuilder()`.

**Conditional Workflow** - Rute baseret på betingelser til forskellige specialistagenter.

**Human-in-the-Loop** - Arbejdsgangsmønster der tilføjer menneskelige kontrolpunkter til godkendelse eller indholdsrevision.

**langchain4j-agentic** - Maven afhængighed for deklarativ agentopbygning (eksperimentel).

**Loop Workflow** - Iterer agentudførelse indtil en betingelse er opfyldt (f.eks. kvalitets score ≥ 0.8).

**outputKey** - Agent annotation parameter der angiver hvor resultater gemmes i Agentic Scope.

**Parallel Workflow** - Kør flere agenter samtidigt til uafhængige opgaver.

**Response Strategy** - Hvordan supervisor formulerer endeligt svar: LAST, SUMMARY, eller SCORED.

**Sequential Workflow** - Udfør agenter i rækkefølge hvor output flyder til næste trin.

**Supervisor Agent Pattern** - Avanceret agentmønster hvor en supervisor LLM dynamisk beslutter hvilke sub-agenter der skal påkaldes.

## Model Context Protocol (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven afhængighed til MCP integration i LangChain4j.

**MCP** - Model Context Protocol: standard for tilslutning af AI-apps til eksterne værktøjer. Byg én gang, brug alle steder.

**MCP Client** - Applikation der forbinder til MCP servere for at finde og bruge værktøjer.

**MCP Server** - Tjeneste der eksponerer værktøjer via MCP med klare beskrivelser og parameterskemaer.

**McpToolProvider** - LangChain4j komponent der indpakker MCP værktøjer til brug i AI services og agenter.

**McpTransport** - Interface til MCP kommunikation. Implementeringer inkluderer Stdio og HTTP.

**Stdio Transport** - Lokal proces transport via stdin/stdout. Brugbar til filsystemadgang eller kommandolinjeværktøjer.

**StdioMcpTransport** - LangChain4j implementering der starter MCP server som subprocess.

**Tool Discovery** - Klient spørger server efter tilgængelige værktøjer med beskrivelser og skemaer.

## Azure Services - [Modul 01](../01-introduction/README.md)

**Azure AI Search** - Cloud søgning med vektor kapabiliteter. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Udruller Azure ressourcer.

**Azure OpenAI** - Microsofts enterprise AI service.

**Bicep** - Azure infrastruktur-som-kode sprog. [Infrastructure Guide](../01-introduction/infra/README.md)

**Deployment Name** - Navn for modeludrulning i Azure.

**GPT-5.2** - Nyeste OpenAI model med styring af ræsonnering. [Modul 02](../02-prompt-engineering/README.md)

## Testning og Udvikling - [Testguide](TESTING.md)

**Dev Container** - Containeriseret udviklingsmiljø. [Konfiguration](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Gratis AI model legeplads. [Modul 00](../00-quick-start/README.md)

**In-Memory Testing** - Test med hukommelseslager.

**Integration Testing** - Test med rigtig infrastruktur.

**Maven** - Java build automatiseringsværktøj.

**Mockito** - Java mocking framework.

**Spring Boot** - Java applikationsramme. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:  
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, bedes du være opmærksom på, at automatiske oversættelser kan indeholde fejl eller unøjagtigheder. Det oprindelige dokument på dets modersmål skal betragtes som den autoritative kilde. For kritiske oplysninger anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for eventuelle misforståelser eller fejltolkninger som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->