# LangChain4j Ordliste

## Indholdsfortegnelse

- [Kernebegreber](../../../docs)
- [LangChain4j Komponenter](../../../docs)
- [AI/ML Begreber](../../../docs)
- [Guardrails](../../../docs)
- [Prompt Engineering](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agenter og Værktøjer](../../../docs)
- [Agentmodul](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Azure Tjenester](../../../docs)
- [Test og Udvikling](../../../docs)

Hurtig reference for termer og begreber brugt gennem hele kurset.

## Kernebegreber

**AI Agent** - System, der bruger AI til at ræsonnere og handle autonomt. [Module 04](../04-tools/README.md)

**Kæde** - Sekvens af operationer, hvor output fodres ind i næste trin.

**Chunking** - Opdeling af dokumenter i mindre stykker. Typisk: 300-500 tokens med overlap. [Module 03](../03-rag/README.md)

**Kontekstvindue** - Maksimalt antal tokens en model kan behandle. GPT-5.2: 400K tokens.

**Embeddings** - Numeriske vektorer, der repræsenterer teksts mening. [Module 03](../03-rag/README.md)

**Funktionsopkald** - Modellens generering af strukturerede forespørgsler for at kalde eksterne funktioner. [Module 04](../04-tools/README.md)

**Hallucination** - Når modeller genererer ukorrekte men plausible oplysninger.

**Prompt** - Tekstinput til en sprogmodel. [Module 02](../02-prompt-engineering/README.md)

**Semantisk søgning** - Søgning efter mening ved brug af embeddings, ikke nøgleord. [Module 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: ingen hukommelse. Stateful: bevarer samtalehistorik. [Module 01](../01-introduction/README.md)

**Tokens** - Grundlæggende tekstenheder, som modeller behandler. Påvirker omkostninger og grænser. [Module 01](../01-introduction/README.md)

**Kæde af værktøjer** - Sekventiel værktøjsudførelse, hvor output informerer næste kald. [Module 04](../04-tools/README.md)

## LangChain4j Komponenter

**AiServices** - Opretter typesikre AI-servicegrænseflader.

**OpenAiOfficialChatModel** - Enhedlig klient til OpenAI og Azure OpenAI modeller.

**OpenAiOfficialEmbeddingModel** - Opretter embeddings via OpenAI Official klient (understøtter både OpenAI og Azure OpenAI).

**ChatModel** - Kernegrænseflade til sprogmodeller.

**ChatMemory** - Vedligeholder samtalehistorik.

**ContentRetriever** - Finder relevante dokumentstykker til RAG.

**DocumentSplitter** - Opdeler dokumenter i bidder.

**EmbeddingModel** - Konverterer tekst til numeriske vektorer.

**EmbeddingStore** - Gemmer og henter embeddings.

**MessageWindowChatMemory** - Vedligeholder et glidende vindue af nylige beskeder.

**PromptTemplate** - Opretter genanvendelige prompts med `{{variable}}` pladsholdere.

**TextSegment** - Tekststykke med metadata. Bruges i RAG.

**ToolExecutionRequest** - Repræsenterer anmodning om værktøjsudførelse.

**UserMessage / AiMessage / SystemMessage** - Samtaletypemeldinger.

## AI/ML Begreber

**Few-Shot Learning** - Afgivelse af eksempler i prompts. [Module 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - AI-modeller trænet på store mængder tekstdata.

**Reasoning Effort** - GPT-5.2 parameter, der styrer tænkedybde. [Module 02](../02-prompt-engineering/README.md)

**Temperatur** - Kontrollerer output randomness. Lav=determeret, høj=kreativ.

**Vektordatabase** - Specialiseret database til embeddings. [Module 03](../03-rag/README.md)

**Zero-Shot Learning** - Udførelse af opgaver uden eksempler. [Module 02](../02-prompt-engineering/README.md)

## Guardrails - [Module 00](../00-quick-start/README.md)

**Defense in Depth** - Flerlags sikkerhedstilgang, der kombinerer applikationsniveau guardrails med leverandørsikkerhedsfiltre.

**Hard Block** - Leverandør kaster HTTP 400 fejl ved alvorlige indholdsbrud.

**InputGuardrail** - LangChain4j interface til validering af brugerinput inden det når LLM. Spar omkostninger og latency ved at blokere skadelige prompts tidligt.

**InputGuardrailResult** - Return type for guardrail validering: `success()` eller `fatal("reason")`.

**OutputGuardrail** - Interface til validering af AI-svar før de returneres til brugere.

**Provider Safety Filters** - Indbyggede indholdsfiltre fra AI-leverandører (f.eks. GitHub Models), der fanger brud på API-niveau.

**Soft Refusal** - Model afslår høfligt at svare uden at kaste fejl.

## Prompt Engineering - [Module 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Trin-for-trin ræsonnering for bedre nøjagtighed.

**Constrained Output** - Håndhævelse af specifikt format eller struktur.

**High Eagerness** - GPT-5.2 mønster for grundig ræsonnering.

**Low Eagerness** - GPT-5.2 mønster for hurtige svar.

**Multi-Turn Conversation** - Bevarelse af kontekst gennem udvekslinger.

**Role-Based Prompting** - Indstilling af modelperson via systemmeddelelser.

**Self-Reflection** - Model evaluerer og forbedrer sit output.

**Structured Analysis** - Fast evalueringsramme.

**Task Execution Pattern** - Plan → Udfør → Opsummer.

## RAG (Retrieval-Augmented Generation) - [Module 03](../03-rag/README.md)

**Dokumentbehandlingspipeline** - Indlæs → del → indlejre → gem.

**In-Memory Embedding Store** - Ikke-permanent lager til test.

**RAG** - Kombinerer genfinding med generering for at fundere svar.

**Lighedsscore** - Mål (0-1) for semantisk lighed.

**Kildereference** - Metadata om hentet indhold.

## Agenter og Værktøjer - [Module 04](../04-tools/README.md)

**@Tool Annotation** - Marker Java-metoder som AI-kaldbare værktøjer.

**ReAct Pattern** - Ræsonner → Handl → Observer → Gentag.

**Session Management** - Adskilte kontekster for forskellige brugere.

**Værktøj** - Funktion, som en AI-agent kan kalde.

**Tool Beskrivelse** - Dokumentation af værktøjets formål og parametre.

## Agentmodul - [Module 05](../05-mcp/README.md)

**@Agent Annotation** - Marker grænseflader som AI-agenter med deklarativ adfærdsdefinering.

**Agent Listener** - Hook til overvågning af agentudførelse via `beforeAgentInvocation()` og `afterAgentInvocation()`.

**Agentic Scope** - Delt hukommelse hvor agenter gemmer output ved brug af `outputKey` til nedstrøms agenter.

**AgenticServices** - Fabrik til at skabe agenter via `agentBuilder()` og `supervisorBuilder()`.

**Conditional Workflow** - Rute baseret på betingelser til forskellige specialistagenter.

**Human-in-the-Loop** - Arbejdsgangsmønster med menneskelige checkpoints til godkendelse eller indholdsrevision.

**langchain4j-agentic** - Maven-afhængighed til deklarativ agentbygning (eksperimentel).

**Loop Workflow** - Gentag agentudførelse indtil betingelse opfyldes (f.eks. kvalitets-score ≥ 0.8).

**outputKey** - Agent annotation parameter, der specificerer hvor resultater gemmes i Agentic Scope.

**Parallel Workflow** - Kør flere agenter samtidigt for uafhængige opgaver.

**Response Strategy** - Hvordan supervisor formulerer endeligt svar: LAST, SUMMARY, eller SCORED.

**Sequential Workflow** - Udfør agenter i rækkefølge, hvor output flyder til næste trin.

**Supervisor Agent Pattern** - Avanceret agentmønster hvor en supervisor LLM dynamisk beslutter hvilke sub-agenter der skal tilkaldes.

## Model Context Protocol (MCP) - [Module 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven-afhængighed til MCP-integration i LangChain4j.

**MCP** - Model Context Protocol: standard for tilslutning af AI-apps til eksterne værktøjer. Byg én gang, brug overalt.

**MCP Client** - Applikation, der forbinder til MCP-servere for at opdage og bruge værktøjer.

**MCP Server** - Service, der eksponerer værktøjer via MCP med klare beskrivelser og parameterskemaer.

**McpToolProvider** - LangChain4j komponent, der omslutter MCP-værktøjer til brug i AI-services og agenter.

**McpTransport** - Interface til MCP-kommunikation. Implementeringer inkluderer Stdio og HTTP.

**Stdio Transport** - Lokal proces-transport via stdin/stdout. Nyttig for filsystemadgang eller kommandolinjeværktøjer.

**StdioMcpTransport** - LangChain4j-implementering, der starter MCP-server som subprocess.

**Tool Discovery** - Client forespørger server efter tilgængelige værktøjer med beskrivelser og skemaer.

## Azure Tjenester - [Module 01](../01-introduction/README.md)

**Azure AI Search** - Cloud-søgning med vektorfunktioner. [Module 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Udruller Azure-ressourcer.

**Azure OpenAI** - Microsofts enterprise AI-tjeneste.

**Bicep** - Azure infrastruktur-som-kode sprog. [Infrastructure Guide](../01-introduction/infra/README.md)

**Deploymentsnavn** - Navn til modeludrulning i Azure.

**GPT-5.2** - Seneste OpenAI-model med ræsonneringskontrol. [Module 02](../02-prompt-engineering/README.md)

## Test og Udvikling - [Testing Guide](TESTING.md)

**Dev Container** - Containeriseret udviklingsmiljø. [Konfiguration](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Gratis AI-model legeplads. [Module 00](../00-quick-start/README.md)

**In-Memory Testing** - Test med in-memory lager.

**Integration Testing** - Test med rigtig infrastruktur.

**Maven** - Java build-automatiseringsværktøj.

**Mockito** - Java mocking framework.

**Spring Boot** - Java applikationsframework. [Module 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, skal du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det oprindelige dokument på dets modersmål bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for eventuelle misforståelser eller fejltolkninger, der opstår som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->