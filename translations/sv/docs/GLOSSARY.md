# LangChain4j Ordlista

## Innehållsförteckning

- [Kärnkoncept](../../../docs)
- [LangChain4j-komponenter](../../../docs)
- [AI/ML-koncept](../../../docs)
- [Säkerhetsmekanismer](../../../docs)
- [Prompt Engineering](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agenter och Verktyg](../../../docs)
- [Agentmodul](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Azure-tjänster](../../../docs)
- [Testning och Utveckling](../../../docs)

Snabb referens för termer och koncept som används i hela kursen.

## Kärnkoncept

**AI Agent** - System som använder AI för att resonera och agera autonomt. [Modul 04](../04-tools/README.md)

**Chain** - Sekvens av operationer där utdata mata in i nästa steg.

**Chunking** - Bryter dokument i mindre delar. Typiskt: 300-500 token med överlappning. [Modul 03](../03-rag/README.md)

**Context Window** - Max antal token en modell kan bearbeta. GPT-5.2: 400K token (upp till 272K input, 128K output).

**Embeddings** - Numeriska vektorer som representerar textens betydelse. [Modul 03](../03-rag/README.md)

**Function Calling** - Modell genererar strukturerade förfrågningar för att anropa externa funktioner. [Modul 04](../04-tools/README.md)

**Hallucination** - När modeller genererar felaktig men trovärdig information.

**Prompt** - Textinmatning till en språkmodell. [Modul 02](../02-prompt-engineering/README.md)

**Semantic Search** - Sökning baserad på betydelse med embeddings, inte nyckelord. [Modul 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: inget minne. Stateful: behåller konversationshistorik. [Modul 01](../01-introduction/README.md)

**Tokens** - Grundläggande textenheter som modeller bearbetar. Påverkar kostnader och begränsningar. [Modul 01](../01-introduction/README.md)

**Tool Chaining** - Sekventiell verktygsexekvering där utdata informerar nästa anrop. [Modul 04](../04-tools/README.md)

## LangChain4j-komponenter

**AiServices** - Skapar typer-baserade AI-servicelgränssnitt.

**OpenAiOfficialChatModel** - Enhetlig klient för OpenAI och Azure OpenAI-modeller.

**OpenAiOfficialEmbeddingModel** - Skapar embeddings med OpenAI Official-klienten (stödjer både OpenAI och Azure OpenAI).

**ChatModel** - Kärngränssnitt för språkmodeller.

**ChatMemory** - Behåller konversationshistorik.

**ContentRetriever** - Hittar relevanta dokumentbitar för RAG.

**DocumentSplitter** - Delar upp dokument i bitar.

**EmbeddingModel** - Omvandlar text till numeriska vektorer.

**EmbeddingStore** - Lagrar och hämtar embeddings.

**MessageWindowChatMemory** - Behåller ett glidande fönster av senaste meddelanden.

**PromptTemplate** - Skapar återanvändbara prompts med `{{variable}}`-platshållare.

**TextSegment** - Textbit med metadata. Används i RAG.

**ToolExecutionRequest** - Representerar verktygsexekveringsförfrågan.

**UserMessage / AiMessage / SystemMessage** - Typer av konversationsmeddelanden.

## AI/ML-koncept

**Few-Shot Learning** - Tillhandahåller exempel i prompts. [Modul 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - AI-modeller tränade på stora textmängder.

**Reasoning Effort** - GPT-5.2-parameter som styr hurtighet i tänkande. [Modul 02](../02-prompt-engineering/README.md)

**Temperature** - Styr outputens slumpmässighet. Lågt=deterministiskt, högt=kreativt.

**Vector Database** - Specialiserad databas för embeddings. [Modul 03](../03-rag/README.md)

**Zero-Shot Learning** - Utför uppgifter utan exempel. [Modul 02](../02-prompt-engineering/README.md)

## Säkerhetsmekanismer - [Modul 00](../00-quick-start/README.md)

**Defense in Depth** - Flerlagers säkerhetsstrategi som kombinerar applikationsnivå-säkerhetsmekanismer med leverantörers säkerhetsfilter.

**Hard Block** - Leverantör kastar HTTP 400-fel vid allvarliga innehållsöverträdelse.

**InputGuardrail** - LangChain4j-gränssnitt för validering av användarinmatning innan det når LLM. Sparar kostnad och latens genom att blockera skadliga prompts tidigt.

**InputGuardrailResult** - Returtyp för validering av säkerhetsmekanismer: `success()` eller `fatal("reason")`.

**OutputGuardrail** - Gränssnitt för validering av AI-svar innan de returneras till användare.

**Provider Safety Filters** - Inbyggda innehållsfilter från AI-leverantörer (t.ex. GitHub Models) som fångar överträdelser på API-nivå.

**Soft Refusal** - Modell artigt avböjer att svara utan att kasta fel.

## Prompt Engineering - [Modul 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Stegvis resonemang för bättre noggrannhet.

**Constrained Output** - Tvingar specifikt format eller struktur.

**High Eagerness** - GPT-5.2-mönster för grundligt resonemang.

**Low Eagerness** - GPT-5.2-mönster för snabba svar.

**Multi-Turn Conversation** - Behålla kontext över utbyten.

**Role-Based Prompting** - Sätta modellpersona via systemmeddelanden.

**Self-Reflection** - Modell utvärderar och förbättrar sin output.

**Structured Analysis** - Fast utvärderingsramverk.

**Task Execution Pattern** - Planera → Exekvera → Sammanfatta.

## RAG (Retrieval-Augmented Generation) - [Modul 03](../03-rag/README.md)

**Document Processing Pipeline** - Ladda → dela upp → embedda → lagra.

**In-Memory Embedding Store** - Icke-persistent lagring för testning.

**RAG** - Kombinerar hämtning med generering för att grundlägga svar.

**Similarity Score** - Mått (0-1) på semantisk likhet.

**Source Reference** - Metadata om hämtat innehåll.

## Agenter och Verktyg - [Modul 04](../04-tools/README.md)

**@Tool Annotation** - Markerar Java-metoder som AI-anropbara verktyg.

**ReAct Pattern** - Resonera → Agera → Observera → Upprepa.

**Session Management** - Separata kontexter för olika användare.

**Tool** - Funktion som en AI-agent kan anropa.

**Tool Description** - Dokumentation av verktygets syfte och parametrar.

## Agentmodul - [Modul 05](../05-mcp/README.md)

**@Agent Annotation** - Markerar gränssnitt som AI-agenter med deklarativ beteendedefinition.

**Agent Listener** - Hook för att övervaka agentexekvering via `beforeAgentInvocation()` och `afterAgentInvocation()`.

**Agentic Scope** - Delat minne där agenter lagrar utdata med `outputKey` för att andra agenter ska kunna använda.

**AgenticServices** - Fabrik för att skapa agenter med `agentBuilder()` och `supervisorBuilder()`.

**Conditional Workflow** - Rutt baserat på villkor till olika specialagenters.

**Human-in-the-Loop** - Arbetsflödesmönster som lägger till mänskliga kontrollpunkter för godkännande eller innehållsgranskning.

**langchain4j-agentic** - Maven-beroende för deklarativ agentbyggnad (experimentell).

**Loop Workflow** - Iterera agentexekvering tills ett villkor uppfylls (t.ex. kvalitetspoäng ≥ 0.8).

**outputKey** - Agentannoteringsparameter som anger var resultat lagras i Agentic Scope.

**Parallel Workflow** - Kör flera agenter samtidigt för oberoende uppgifter.

**Response Strategy** - Hur supervisor formulerar slutgiltigt svar: LAST, SUMMARY eller SCORED.

**Sequential Workflow** - Exekvera agenter i ordning där utdata flödar till nästa steg.

**Supervisor Agent Pattern** - Avancerat agentmönster där en supervisor LLM dynamiskt väljer vilka underagenter som ska anropas.

## Model Context Protocol (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven-beroende för MCP-integration i LangChain4j.

**MCP** - Model Context Protocol: standard för att koppla AI-appar till externa verktyg. Bygg en gång, använd överallt.

**MCP Client** - Applikation som ansluter till MCP-servrar för att upptäcka och använda verktyg.

**MCP Server** - Tjänst som exponerar verktyg via MCP med tydliga beskrivningar och parameterscheman.

**McpToolProvider** - LangChain4j-komponent som omsluter MCP-verktyg för användning i AI-tjänster och agenter.

**McpTransport** - Gränssnitt för MCP-kommunikation. Implementationer inkluderar Stdio och HTTP.

**Stdio Transport** - Lokal processtransport via stdin/stdout. Användbar för åtkomst till filsystem eller kommandoradsverktyg.

**StdioMcpTransport** - LangChain4j-implementation som startar MCP-server som subprocess.

**Tool Discovery** - Klient frågar servern om tillgängliga verktyg med beskrivningar och scheman.

## Azure-tjänster - [Modul 01](../01-introduction/README.md)

**Azure AI Search** - Molnsökning med vektorbaserade funktioner. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Distribuerar Azure-resurser.

**Azure OpenAI** - Microsofts företags-AI-tjänst.

**Bicep** - Azure infrastruktur-som-kod språk. [Infrastrukturguide](../01-introduction/infra/README.md)

**Deployment Name** - Namn för modellutplacering i Azure.

**GPT-5.2** - Senaste OpenAI-modellen med kontroll över resonemang. [Modul 02](../02-prompt-engineering/README.md)

## Testning och Utveckling - [Testguide](TESTING.md)

**Dev Container** - Containeriserad utvecklingsmiljö. [Konfiguration](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Gratis AI-modelllekplats. [Modul 00](../00-quick-start/README.md)

**In-Memory Testing** - Testning med lagring i minnet.

**Integration Testing** - Testning med riktig infrastruktur.

**Maven** - Java-byggautomatiseringsverktyg.

**Mockito** - Java-ramverk för mockning.

**Spring Boot** - Java applikationsramverk. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, bör du vara medveten om att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess ursprungliga språk bör betraktas som den auktoritativa källan. För viktig information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för eventuella missförstånd eller feltolkningar som uppstår vid användning av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->