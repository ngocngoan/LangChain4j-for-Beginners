# LangChain4j Ordlista

## Innehållsförteckning

- [Kärnbegrepp](../../../docs)
- [LangChain4j Komponenter](../../../docs)
- [AI/ML Begrepp](../../../docs)
- [Guardrails](../../../docs)
- [Prompt Engineering](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agenter och Verktyg](../../../docs)
- [Agentmodul](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Azure-tjänster](../../../docs)
- [Testning och Utveckling](../../../docs)

Snabb referens för termer och begrepp som används genom hela kursen.

## Kärnbegrepp

**AI Agent** - System som använder AI för att resonera och agera autonomt. [Modul 04](../04-tools/README.md)

**Chain** - Sekvens av operationer där utdata matas in i nästa steg.

**Chunking** - Delar upp dokument i mindre bitar. Typiskt: 300-500 tokens med överlappning. [Modul 03](../03-rag/README.md)

**Context Window** - Maximalt antal tokens en modell kan bearbeta. GPT-5.2: 400K tokens.

**Embeddings** - Numeriska vektorer som representerar textens betydelse. [Modul 03](../03-rag/README.md)

**Function Calling** - Modell genererar strukturerade förfrågningar för att anropa externa funktioner. [Modul 04](../04-tools/README.md)

**Hallucination** - När modeller genererar felaktig men trovärdig information.

**Prompt** - Textinmatning till ett språkmodell. [Modul 02](../02-prompt-engineering/README.md)

**Semantic Search** - Sökning baserad på betydelse med embeddings, inte nyckelord. [Modul 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: ingen minne. Stateful: bibehåller konversationshistorik. [Modul 01](../01-introduction/README.md)

**Tokens** - Grundläggande textenheter som modeller bearbetar. Påverkar kostnader och begränsningar. [Modul 01](../01-introduction/README.md)

**Tool Chaining** - Sekventiell verktygsexekvering där utdata informerar nästa anrop. [Modul 04](../04-tools/README.md)

## LangChain4j Komponenter

**AiServices** - Skapar typ-säkra AI-tjänstegränssnitt.

**OpenAiOfficialChatModel** - Enhetlig klient för OpenAI och Azure OpenAI-modeller.

**OpenAiOfficialEmbeddingModel** - Skapar embeddings med OpenAI Official-klienten (stöder både OpenAI och Azure OpenAI).

**ChatModel** - Kärninterface för språkmodeller.

**ChatMemory** - Bibehåller konversationshistorik.

**ContentRetriever** - Hittar relevanta dokumentbitar för RAG.

**DocumentSplitter** - Delar dokument i bitar.

**EmbeddingModel** - Omvandlar text till numeriska vektorer.

**EmbeddingStore** - Lagrar och hämtar embeddings.

**MessageWindowChatMemory** - Bibehåller en glidande fönster av senaste meddelanden.

**PromptTemplate** - Skapar återanvändbara prompts med `{{variable}}` platshållare.

**TextSegment** - Textbit med metadata. Används i RAG.

**ToolExecutionRequest** - Representerar verktygsexekveringsförfrågan.

**UserMessage / AiMessage / SystemMessage** - Meddelandetyper i konversation.

## AI/ML Begrepp

**Few-Shot Learning** - Ge exempel i prompts. [Modul 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - AI-modeller tränade på enorma mängder textdata.

**Reasoning Effort** - GPT-5.2 parameter som styr tankemängd. [Modul 02](../02-prompt-engineering/README.md)

**Temperature** - Styr slumpmässighet i svar. Låg=deterministisk, hög=kreativ.

**Vector Database** - Specialiserad databas för embeddings. [Modul 03](../03-rag/README.md)

**Zero-Shot Learning** - Utföra uppgifter utan exempel. [Modul 02](../02-prompt-engineering/README.md)

## Guardrails - [Modul 00](../00-quick-start/README.md)

**Defense in Depth** - Flerskikts-säkerhetsstrategi som kombinerar applikationsnivå guardrails med leverantörers säkerhetsfilter.

**Hard Block** - Leverantör kastar HTTP 400-fel vid allvarliga innehållsöverträdelser.

**InputGuardrail** - LangChain4j interface för att validera användarinmatning innan den når LLM. Sparar kostnad och väntetid genom att blockera skadliga prompts tidigt.

**InputGuardrailResult** - Returtyp för guardrail-validering: `success()` eller `fatal("reason")`.

**OutputGuardrail** - Interface för att validera AI-svar innan de returneras till användare.

**Provider Safety Filters** - Inbyggda innehållsfilter från AI-leverantörer (t.ex. GitHub Models) som fångar överträdelser på API-nivå.

**Soft Refusal** - Modell artigt avböjer att svara utan att kasta fel.

## Prompt Engineering - [Modul 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Stegvis resonemang för bättre noggrannhet.

**Constrained Output** - Tvingar specifikt format eller struktur.

**High Eagerness** - GPT-5.2 mönster för grundligt resonemang.

**Low Eagerness** - GPT-5.2 mönster för snabba svar.

**Multi-Turn Conversation** - Bibehålla kontext över utbyten.

**Role-Based Prompting** - Sätta modellens persona via systemmeddelanden.

**Self-Reflection** - Modell utvärderar och förbättrar sitt svar.

**Structured Analysis** - Fast utvärderingsramverk.

**Task Execution Pattern** - Planera → Utför → Sammanfatta.

## RAG (Retrieval-Augmented Generation) - [Modul 03](../03-rag/README.md)

**Document Processing Pipeline** - Ladda → dela → embedda → lagra.

**In-Memory Embedding Store** - Icke-persistenta lagring för testning.

**RAG** - Kombinerar hämtning med generering för att förankra svar.

**Similarity Score** - Mått (0-1) på semantisk likhet.

**Source Reference** - Metadata om hämtat innehåll.

## Agenter och Verktyg - [Modul 04](../04-tools/README.md)

**@Tool Annotation** - Markerar Java-metoder som AI-anropbara verktyg.

**ReAct Pattern** - Resonera → Agera → Observera → Upprepa.

**Session Management** - Separata kontexter för olika användare.

**Tool** - Funktion som en AI-agent kan anropa.

**Tool Description** - Dokumentation av verktygets syfte och parametrar.

## Agentmodul - [Modul 05](../05-mcp/README.md)

**@Agent Annotation** - Markerar interface som AI-agenter med deklarativ beteendedefinition.

**Agent Listener** - Hook för att övervaka agentkörning via `beforeAgentInvocation()` och `afterAgentInvocation()`.

**Agentic Scope** - Delat minne där agenter lagrar utdata med `outputKey` för att downstream-agenter ska konsumera.

**AgenticServices** - Fabrik för att skapa agenter med `agentBuilder()` och `supervisorBuilder()`.

**Conditional Workflow** - Rutt baserat på villkor till olika specialistagenter.

**Human-in-the-Loop** - Arbetsflödesmönster som lägger till mänskliga kontrollpunkter för godkännande eller innehållsgranskning.

**langchain4j-agentic** - Mavenberoende för deklarativ agentbyggnad (experimentell).

**Loop Workflow** - Iterera agentkörning tills villkor uppfylls (t.ex. kvalitetsresultat ≥ 0.8).

**outputKey** - Agent annotation parameter som specificerar var resultat lagras i Agentic Scope.

**Parallel Workflow** - Kör flera agenter samtidigt för oberoende uppgifter.

**Response Strategy** - Hur handledaren formulerar slutligt svar: LAST, SUMMARY eller SCORED.

**Sequential Workflow** - Kör agenter i ordning där utdata flödar till nästa steg.

**Supervisor Agent Pattern** - Avancerat agentmönster där en supervisor-LLM dynamiskt avgör vilka underagenter som ska anropas.

## Model Context Protocol (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** - Mavenberoende för MCP-integration i LangChain4j.

**MCP** - Model Context Protocol: standard för att koppla AI-appar till externa verktyg. Bygg en gång, använd överallt.

**MCP Client** - Applikation som kopplar till MCP-servrar för att upptäcka och använda verktyg.

**MCP Server** - Tjänst som exponerar verktyg via MCP med tydliga beskrivningar och parameterscheman.

**McpToolProvider** - LangChain4j-komponent som omsluter MCP-verktyg för användning i AI-tjänster och agenter.

**McpTransport** - Interface för MCP-kommunikation. Implementeringar inkluderar Stdio och HTTP.

**Stdio Transport** - Lokal processtransport via stdin/stdout. Användbar för filsystemåtkomst eller kommandoradsverktyg.

**StdioMcpTransport** - LangChain4j-implementation som startar MCP-server som subprocess.

**Tool Discovery** - Klient frågar server om tillgängliga verktyg med beskrivningar och scheman.

## Azure-tjänster - [Modul 01](../01-introduction/README.md)

**Azure AI Search** - Molnsökning med vektor-kapabiliteter. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Distribuerar Azure-resurser.

**Azure OpenAI** - Microsofts företags AI-tjänst.

**Bicep** - Azure infrastrukturkodspråk. [Infrastrukturguide](../01-introduction/infra/README.md)

**Deployment Name** - Namn för modellutplacering i Azure.

**GPT-5.2** - Senaste OpenAI-modellen med kontroll för resonemang. [Modul 02](../02-prompt-engineering/README.md)

## Testning och Utveckling - [Testningsguide](TESTING.md)

**Dev Container** - Containeriserad utvecklingsmiljö. [Konfiguration](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Gratis AI-modell-lekyta. [Modul 00](../00-quick-start/README.md)

**In-Memory Testing** - Testning med minneslagring.

**Integration Testing** - Testning med verklig infrastruktur.

**Maven** - Java byggautomationsverktyg.

**Mockito** - Java ramverk för mockning.

**Spring Boot** - Java applikationsramverk. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:  
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, var vänlig observera att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess modersmål bör betraktas som den auktoritativa källan. För viktig information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för några missförstånd eller feltolkningar som uppstår till följd av användningen av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->