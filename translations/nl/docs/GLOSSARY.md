# LangChain4j Woordenlijst

## Inhoudsopgave

- [Kernconcepten](../../../docs)
- [LangChain4j Componenten](../../../docs)
- [AI/ML Concepten](../../../docs)
- [Guardrails](../../../docs)
- [Prompt Engineering](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agenten en Tools](../../../docs)
- [Agentic Module](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Azure Diensten](../../../docs)
- [Testen en Ontwikkeling](../../../docs)

Snelle referentie voor termen en concepten die door de cursus heen worden gebruikt.

## Kernconcepten

**AI Agent** - Systeem dat AI gebruikt om autonoom te redeneren en te handelen. [Module 04](../04-tools/README.md)

**Chain** - Reeks bewerkingen waarbij output de volgende stap voedt.

**Chunking** - Documenten opdelen in kleinere stukken. Typisch: 300-500 tokens met overlap. [Module 03](../03-rag/README.md)

**Context Window** - Maximale tokens die een model kan verwerken. GPT-5.2: 400K tokens (tot 272K input, 128K output).

**Embeddings** - Numerieke vectoren die tekstbetekenis representeren. [Module 03](../03-rag/README.md)

**Function Calling** - Model genereert gestructureerde verzoeken om externe functies aan te roepen. [Module 04](../04-tools/README.md)

**Hallucinatie** - Wanneer modellen incorrecte maar plausibele informatie genereren.

**Prompt** - Tekstinput voor een taalmodel. [Module 02](../02-prompt-engineering/README.md)

**Semantisch Zoeken** - Zoeken op betekenis met behulp van embeddings, niet sleutelwoorden. [Module 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: geen geheugen. Stateful: onderhoudt gespreksgeschiedenis. [Module 01](../01-introduction/README.md)

**Tokens** - Basis tekstunits die modellen verwerken. Beïnvloeden kosten en limieten. [Module 01](../01-introduction/README.md)

**Tool Chaining** - Opeenvolgende uitvoering van tools waarbij output de volgende aanroep informeert. [Module 04](../04-tools/README.md)

## LangChain4j Componenten

**AiServices** - Maakt type-veilige AI service interfaces.

**OpenAiOfficialChatModel** - Geünificeerde client voor OpenAI en Azure OpenAI modellen.

**OpenAiOfficialEmbeddingModel** - Maakt embeddings met OpenAI Official client (ondersteunt OpenAI en Azure OpenAI).

**ChatModel** - Kerninterface voor taalmodellen.

**ChatMemory** - Onderhoudt gespreksgeschiedenis.

**ContentRetriever** - Vindt relevante documentstukken voor RAG.

**DocumentSplitter** - Splitst documenten in stukken.

**EmbeddingModel** - Zet tekst om in numerieke vectoren.

**EmbeddingStore** - Slaat embeddings op en haalt ze op.

**MessageWindowChatMemory** - Onderhoudt schuivend venster van recente berichten.

**PromptTemplate** - Maakt herbruikbare prompts met `{{variable}}` placeholders.

**TextSegment** - Tekststuk met metadata. Gebruikt in RAG.

**ToolExecutionRequest** - Vertegenwoordigt verzoek tot uitvoering van een tool.

**UserMessage / AiMessage / SystemMessage** - Gespreksberichttypen.

## AI/ML Concepten

**Few-Shot Learning** - Voorbeelden geven in prompts. [Module 02](../02-prompt-engineering/README.md)

**Groot Taalmodel (LLM)** - AI modellen getraind op enorme hoeveelheden tekstdata.

**Redeneerinspanning** - GPT-5.2 parameter die diepgang van denken aanstuurt. [Module 02](../02-prompt-engineering/README.md)

**Temperatuur** - Stuurt de willekeurigheid van output. Laag=deterministisch, hoog=creatief.

**Vector Database** - Gespecialiseerde database voor embeddings. [Module 03](../03-rag/README.md)

**Zero-Shot Learning** - Taken uitvoeren zonder voorbeelden. [Module 02](../02-prompt-engineering/README.md)

## Guardrails - [Module 00](../00-quick-start/README.md)

**Defense in Depth** - Meerlaagse beveiligingsaanpak die guardrails op applicatieniveau combineert met provider veiligheidsfilters.

**Hard Block** - Provider gooit HTTP 400 fout bij ernstige inhoudsovertredingen.

**InputGuardrail** - LangChain4j interface om gebruikersinput te valideren voordat het LLM bereikt. Bespaart kosten en latency door schadelijke prompts vroeg te blokkeren.

**InputGuardrailResult** - Retourtype voor guardrail validatie: `success()` of `fatal("reden")`.

**OutputGuardrail** - Interface om AI-antwoorden te valideren voordat ze aan gebruikers teruggegeven worden.

**Provider Safety Filters** - Ingebouwde inhoudsfilters van AI providers (bijv. GitHub Models) die overtredingen afvangen op API niveau.

**Soft Refusal** - Model weigert beleefd te antwoorden zonder foutmelding.

## Prompt Engineering - [Module 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Stapsgewijze redenatie voor betere nauwkeurigheid.

**Beperkte Output** - Het afdwingen van specifiek formaat of structuur.

**Hoge Eifories** - GPT-5.2 patroon voor grondige redenering.

**Lage Eifories** - GPT-5.2 patroon voor snelle antwoorden.

**Multi-Turn Conversatie** - Context behouden over meerdere uitwisselingen.

**Rolgebaseerde Prompting** - Instellen van modelpersona via system berichten.

**Zelfreflectie** - Model evalueert en verbetert zijn output.

**Gestructureerde Analyse** - Vaste evaluatiekader.

**Taakuitvoeringspatroon** - Plannen → Uitvoeren → Samenvatten.

## RAG (Retrieval-Augmented Generation) - [Module 03](../03-rag/README.md)

**Documentverwerkingspipeline** - Laden → opdelen → embedden → opslaan.

**In-Memory Embedding Store** - Niet-persistent opslag voor testen.

**RAG** - Combineert ophalen met generatie om antwoorden te funderen.

**Gelijkenisscore** - Maat (0-1) van semantische gelijkenis.

**Bronvermelding** - Metadata over opgehaalde inhoud.

## Agenten en Tools - [Module 04](../04-tools/README.md)

**@Tool Annotatie** - Markeert Java methoden als AI-oproepbare tools.

**ReAct Patroon** - Redeneer → Handel → Observeer → Herhaal.

**Sessiebeheer** - Gescheiden contexten voor verschillende gebruikers.

**Tool** - Functie die een AI agent kan aanroepen.

**Tool Beschrijving** - Documentatie van tool doel en parameters.

## Agentic Module - [Module 05](../05-mcp/README.md)

**@Agent Annotatie** - Markeert interfaces als AI agenten met declaratieve gedragsdefinitie.

**Agent Listener** - Hook voor monitoring van agentuitvoering via `beforeAgentInvocation()` en `afterAgentInvocation()`.

**Agentic Scope** - Gedeeld geheugen waar agenten output opslaan met `outputKey` voor andere agenten.

**AgenticServices** - Factory voor creëren van agenten via `agentBuilder()` en `supervisorBuilder()`.

**Conditionele Workflow** - Route op basis van condities naar verschillende specialistagenten.

**Human-in-the-Loop** - Workflowpatroon met menselijke checkpoints voor goedkeuring of inhoudscontrole.

**langchain4j-agentic** - Maven dependency voor declaratief agentbouw (experimenteel).

**Loop Workflow** - Itereer agentuitvoering tot aan een conditie voldaan is (bijv. kwaliteitscore ≥ 0.8).

**outputKey** - Agent annotatieparameter die aangeeft waar resultaten in Agentic Scope worden opgeslagen.

**Parallel Workflow** - Meerdere agenten tegelijk uitvoeren voor onafhankelijke taken.

**Responsstrategie** - Hoe supervisor het eindantwoord formuleert: LAST, SUMMARY, of SCORED.

**Sequentiële Workflow** - Agenten in volgorde uitvoeren waarbij output naar volgende stap stroomt.

**Supervisor Agent Patroon** - Geavanceerd agentic patroon waar een supervisor LLM dynamisch beslist welke sub-agenten aan te roepen.

## Model Context Protocol (MCP) - [Module 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven dependency voor MCP integratie in LangChain4j.

**MCP** - Model Context Protocol: standaard voor koppeling van AI-apps aan externe tools. Eénmaal bouwen, overal gebruiken.

**MCP Client** - Applicatie die verbinding maakt met MCP servers om tools te ontdekken en te gebruiken.

**MCP Server** - Dienst die tools via MCP aanbiedt met duidelijke beschrijvingen en parameterschema's.

**McpToolProvider** - LangChain4j component die MCP tools wrappet voor gebruik in AI services en agenten.

**McpTransport** - Interface voor MCP communicatie. Implementaties omvatten Stdio en HTTP.

**Stdio Transport** - Lokale procestransport via stdin/stdout. Handig voor bestandsysteemtoegang of commandoregeltools.

**StdioMcpTransport** - LangChain4j implementatie die MCP server als subprocess start.

**Tool Discovery** - Client vraagt server om beschikbare tools met beschrijvingen en schema's.

## Azure Diensten - [Module 01](../01-introduction/README.md)

**Azure AI Search** - Cloud-zoekdienst met vectormogelijkheden. [Module 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Zet Azure resources uit.

**Azure OpenAI** - Microsoft's enterprise AI-dienst.

**Bicep** - Azure infrastructuur-als-code taal. [Infrastructure Guide](../01-introduction/infra/README.md)

**Deployment Naam** - Naam voor modeldeployment in Azure.

**GPT-5.2** - Laatste OpenAI model met redeneersturing. [Module 02](../02-prompt-engineering/README.md)

## Testen en Ontwikkeling - [Testing Guide](TESTING.md)

**Dev Container** - Containerized ontwikkelomgeving. [Configuratie](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Gratis AI model playground. [Module 00](../00-quick-start/README.md)

**In-Memory Testing** - Testen met in-memory opslag.

**Integratietesten** - Testen met echte infrastructuur.

**Maven** - Java build automatiseringstool.

**Mockito** - Java mocking framework.

**Spring Boot** - Java applicatiekader. [Module 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI vertaaldienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal wordt beschouwd als de gezaghebbende bron. Voor cruciale informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->