# LangChain4j Woordenlijst

## Inhoudsopgave

- [Kernbegrippen](../../../docs)
- [LangChain4j-componenten](../../../docs)
- [AI/ML-begrippen](../../../docs)
- [Guardrails](../../../docs)
- [Prompt Engineering](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agenten en Tools](../../../docs)
- [Agentic Module](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Azure Services](../../../docs)
- [Testen en Ontwikkeling](../../../docs)

Snelle referentie voor termen en begrippen die door de cursus worden gebruikt.

## Kernbegrippen

**AI-agent** - Systeem dat AI gebruikt om autonoom te redeneren en te handelen. [Module 04](../04-tools/README.md)

**Keten** - Reeks operaties waarbij output naar de volgende stap gaat.

**Chunking** - Documenten opsplitsen in kleinere stukken. Typisch: 300-500 tokens met overlap. [Module 03](../03-rag/README.md)

**Contextvenster** - Maximale tokens die een model kan verwerken. GPT-5.2: 400K tokens.

**Embeddings** - Numerieke vectoren die tekstbetekenis representeren. [Module 03](../03-rag/README.md)

**Function Calling** - Model genereert gestructureerde verzoeken om externe functies aan te roepen. [Module 04](../04-tools/README.md)

**Hallucinatie** - Wanneer modellen onjuiste maar plausibele informatie genereren.

**Prompt** - Tekstinvoer voor een taalmodel. [Module 02](../02-prompt-engineering/README.md)

**Semantisch zoeken** - Zoeken op betekenis met embeddings, niet zoekwoorden. [Module 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: zonder geheugen. Stateful: onderhoudt gespreksgeschiedenis. [Module 01](../01-introduction/README.md)

**Tokens** - Basis tekst-eenheden die modellen verwerken. Beïnvloedt kosten en limieten. [Module 01](../01-introduction/README.md)

**Tool Chaining** - Opeenvolgende tool-uitvoering waarbij output de volgende oproep beïnvloedt. [Module 04](../04-tools/README.md)

## LangChain4j-componenten

**AiServices** - Maakt type-veilige AI-service interfaces.

**OpenAiOfficialChatModel** - Geünificeerde client voor OpenAI en Azure OpenAI modellen.

**OpenAiOfficialEmbeddingModel** - Maakt embeddings met OpenAI Official client (ondersteunt OpenAI en Azure OpenAI).

**ChatModel** - Kerninterface voor taalmodellen.

**ChatMemory** - Onderhoudt gespreksgeschiedenis.

**ContentRetriever** - Vindt relevante documentstukken voor RAG.

**DocumentSplitter** - Splitst documenten in stukken.

**EmbeddingModel** - Zet tekst om in numerieke vectoren.

**EmbeddingStore** - Slaat embeddings op en haalt ze op.

**MessageWindowChatMemory** - Onderhoudt schuifvenster van recente berichten.

**PromptTemplate** - Maakt herbruikbare prompts met `{{variable}}` placeholders.

**TextSegment** - Tekststuk met metadata. Gebruikt in RAG.

**ToolExecutionRequest** - Vertegenwoordigt tool-uitvoeringsverzoek.

**UserMessage / AiMessage / SystemMessage** - Soorten gesprekberichten.

## AI/ML-begrippen

**Few-Shot Learning** - Voorbeelden geven in prompts. [Module 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - AI-modellen getraind op enorme tekstdata.

**Reasoning Effort** - GPT-5.2 parameter die de denkdiepte aanstuurt. [Module 02](../02-prompt-engineering/README.md)

**Temperature** - Regelt willekeurigheid in output. Laag=deterministisch, hoog=creatief.

**Vector Database** - Gespecialiseerde database voor embeddings. [Module 03](../03-rag/README.md)

**Zero-Shot Learning** - Taken uitvoeren zonder voorbeelden. [Module 02](../02-prompt-engineering/README.md)

## Guardrails - [Module 00](../00-quick-start/README.md)

**Defense in Depth** - Meerlaagse beveiligingsaanpak die applicatie-level guardrails met provider veiligheidfilters combineert.

**Hard Block** - Provider gooit HTTP 400 fout bij ernstige content overtredingen.

**InputGuardrail** - LangChain4j interface om gebruikersinput te valideren voordat het de LLM bereikt. Bespaart kosten en latency door schadelijke prompts vroeg te blokkeren.

**InputGuardrailResult** - Returntype voor guardrail validatie: `success()` of `fatal("reden")`.

**OutputGuardrail** - Interface om AI-antwoorden te valideren voordat ze aan gebruikers worden teruggegeven.

**Provider Safety Filters** - Ingebouwde contentfilters van AI-providers (bv. GitHub Models) die overtredingen op API-niveau detecteren.

**Soft Refusal** - Model weigert beleefd te antwoorden zonder foutmelding.

## Prompt Engineering - [Module 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Stapsgewijze redenering voor betere nauwkeurigheid.

**Constrained Output** - Vastgestelde specifieke format of structuur afdwingen.

**High Eagerness** - GPT-5.2 patroon voor grondige redenering.

**Low Eagerness** - GPT-5.2 patroon voor snelle antwoorden.

**Multi-Turn Conversation** - Context over meerdere uitwisselingen behouden.

**Role-Based Prompting** - Modelpersoon instellen via system messages.

**Self-Reflection** - Model evalueert en verbetert eigen output.

**Structured Analysis** - Vaste evaluatiekader.

**Task Execution Pattern** - Plan → Uitvoeren → Samenvatten.

## RAG (Retrieval-Augmented Generation) - [Module 03](../03-rag/README.md)

**Document Processing Pipeline** - Laden → opdelen → embedden → opslaan.

**In-Memory Embedding Store** - Niet-persistent opslag voor testen.

**RAG** - Combineert ophalen met generatie voor gefundeerde antwoorden.

**Similarity Score** - Maat (0-1) voor semantische gelijkenis.

**Source Reference** - Metadata over opgehaalde content.

## Agenten en Tools - [Module 04](../04-tools/README.md)

**@Tool Annotatie** - Markeert Java-methoden als AI-aanroepbare tools.

**ReAct Patroon** - Redeneren → Handelen → Observeren → Herhalen.

**Session Management** - Gescheiden contexten voor verschillende gebruikers.

**Tool** - Functie die een AI-agent kan aanroepen.

**Tool Description** - Documentatie van tooldoel en parameters.

## Agentic Module - [Module 05](../05-mcp/README.md)

**@Agent Annotatie** - Markeert interfaces als AI-agenten met declaratieve gedragsdefinitie.

**Agent Listener** - Hook om agent-uitvoering te monitoren via `beforeAgentInvocation()` en `afterAgentInvocation()`.

**Agentic Scope** - Gedeeld geheugen waar agenten uitvoer opslaan met `outputKey` voor downstream agenten om te gebruiken.

**AgenticServices** - Factory om agenten te maken via `agentBuilder()` en `supervisorBuilder()`.

**Conditional Workflow** - Route afhankelijk van condities naar verschillende specialist-agenten.

**Human-in-the-Loop** - Workflow patroon met menselijke checkpoints voor goedkeuring of contentreview.

**langchain4j-agentic** - Maven-dependency voor declaratief agent bouwen (experimenteel).

**Loop Workflow** - Iteratieve agent-uitvoering totdat conditie is voldaan (bv. kwaliteitsscore ≥ 0.8).

**outputKey** - Agentannotatieparameter die specificeert waar resultaten in Agentic Scope worden opgeslagen.

**Parallel Workflow** - Meerdere agenten gelijktijdig uitvoeren voor onafhankelijke taken.

**Response Strategy** - Hoe supervisor het eindantwoord formuleert: LAST, SUMMARY, of SCORED.

**Sequential Workflow** - Agenten in volgorde uitvoeren waarbij output naar de volgende stap gaat.

**Supervisor Agent Pattern** - Geavanceerd agentic patroon waarbij een supervisor LLM dynamisch bepaalt welke sub-agenten worden opgeroepen.

## Model Context Protocol (MCP) - [Module 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven-dependency voor MCP-integratie in LangChain4j.

**MCP** - Model Context Protocol: standaard om AI-apps te verbinden met externe tools. Eenmalig bouwen, overal gebruiken.

**MCP Client** - Applicatie die verbindt met MCP-servers om tools te ontdekken en te gebruiken.

**MCP Server** - Service die tools aanbiedt via MCP met duidelijke omschrijvingen en parameterschema’s.

**McpToolProvider** - LangChain4j-component die MCP-tools verpakt voor gebruik in AI-services en agenten.

**McpTransport** - Interface voor MCP-communicatie. Implementaties zijn o.a. Stdio en HTTP.

**Stdio Transport** - Lokale procestransport via stdin/stdout. Handig voor toegang tot bestandssysteem of command-line tools.

**StdioMcpTransport** - LangChain4j-implementatie die MCP-server als subprocess start.

**Tool Discovery** - Client vraagt server om beschikbare tools met omschrijvingen en schema’s.

## Azure Services - [Module 01](../01-introduction/README.md)

**Azure AI Search** - Cloud-zoekdienst met vectormogelijkheden. [Module 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Zet Azure-resources uit.

**Azure OpenAI** - Microsofts enterprise AI-dienst.

**Bicep** - Azure infrastructuur-als-code taal. [Infrastructure Guide](../01-introduction/infra/README.md)

**Deployment Name** - Naam voor modeluitrol in Azure.

**GPT-5.2** - Laatste OpenAI-model met redeneringscontrole. [Module 02](../02-prompt-engineering/README.md)

## Testen en Ontwikkeling - [Testing Guide](TESTING.md)

**Dev Container** - Gecontaineriseerde ontwikkelomgeving. [Configuratie](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Gratis AI model speelveld. [Module 00](../00-quick-start/README.md)

**In-Memory Testing** - Testen met in-memory opslag.

**Integration Testing** - Testen met echte infrastructuur.

**Maven** - Java build-automatiseringstool.

**Mockito** - Java mocking framework.

**Spring Boot** - Java applicatieframework. [Module 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:  
Dit document is vertaald met behulp van de AI-vertalingsdienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat automatische vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het oorspronkelijke document in de oorspronkelijke taal geldt als de gezaghebbende bron. Voor belangrijke informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties die voortkomen uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->