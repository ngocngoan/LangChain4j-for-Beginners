# LangChain4j Talahulugan

## Talaan ng Nilalaman

- [Pangunahing Konsepto](../../../docs)
- [Mga Komponent ng LangChain4j](../../../docs)
- [Mga Konsepto ng AI/ML](../../../docs)
- [Guardrails](../../../docs)
- [Prompt Engineering](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Mga Ahente at Mga Kasangkapan](../../../docs)
- [Agentic Module](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Mga Serbisyo sa Azure](../../../docs)
- [Pagsubok at Pagpapaunlad](../../../docs)

Mabilisang sanggunian para sa mga terminolohiya at konseptong ginamit sa buong kurso.

## Pangunahing Konsepto

**AI Agent** - Sistema na gumagamit ng AI upang mag-isip at kumilos nang awtonomo. [Module 04](../04-tools/README.md)

**Chain** - Sunod-sunod na mga operasyon kung saan ang output ay ipinasok sa susunod na hakbang.

**Chunking** - Paghahati ng mga dokumento sa mas maliliit na bahagi. Karaniwan: 300-500 token na may overlap. [Module 03](../03-rag/README.md)

**Context Window** - Pinakamaximum na bilang ng token na kayang iproseso ng modelo. GPT-5.2: 400K token.

**Embeddings** - Numerikal na mga vector na kumakatawan sa kahulugan ng teksto. [Module 03](../03-rag/README.md)

**Function Calling** - Ang modelo ay bumubuo ng istrakturadong kahilingan para tawagan ang mga external na function. [Module 04](../04-tools/README.md)

**Hallucination** - Kapag ang mga modelo ay bumubuo ng maling ngunit kapani-paniwalang impormasyon.

**Prompt** - Tekstong input para sa isang language model. [Module 02](../02-prompt-engineering/README.md)

**Semantic Search** - Paghahanap batay sa kahulugan gamit ang embeddings, hindi mga keyword. [Module 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: walang memorya. Stateful: nagtatago ng kasaysayan ng pag-uusap. [Module 01](../01-introduction/README.md)

**Tokens** - Pangunahing yunit ng teksto na pinoproseso ng mga modelo. Nakakaapekto sa gastos at limitasyon. [Module 01](../01-introduction/README.md)

**Tool Chaining** - Sunud-sunod na pagpatupad ng mga kasangkapan kung saan ang output ay nagsisilbing input sa susunod na tawag. [Module 04](../04-tools/README.md)

## Mga Komponent ng LangChain4j

**AiServices** - Lumilikha ng type-safe na mga interface para sa AI services.

**OpenAiOfficialChatModel** - Pinagsamang client para sa OpenAI at Azure OpenAI models.

**OpenAiOfficialEmbeddingModel** - Lumilikha ng embeddings gamit ang OpenAI Official client (sumusuporta sa parehong OpenAI at Azure OpenAI).

**ChatModel** - Pangunahing interface para sa mga language model.

**ChatMemory** - Nangangalaga ng kasaysayan ng pag-uusap.

**ContentRetriever** - Naghahanap ng mga kaugnay na piraso ng dokumento para sa RAG.

**DocumentSplitter** - Naghahati ng mga dokumento sa mga chunks.

**EmbeddingModel** - Nagko-convert ng teksto sa numerikal na mga vector.

**EmbeddingStore** - Nag-iimbak at kumukuha ng mga embeddings.

**MessageWindowChatMemory** - Nangangalaga ng sliding window ng mga kamakailang mensahe.

**PromptTemplate** - Lumilikha ng mga reusable na prompt na may `{{variable}}` placeholders.

**TextSegment** - Piraso ng teksto na may metadata. Ginagamit sa RAG.

**ToolExecutionRequest** - Kumakatawan sa kahilingan para sa pagpatupad ng kasangkapan.

**UserMessage / AiMessage / SystemMessage** - Mga uri ng mensahe sa pag-uusap.

## Mga Konsepto ng AI/ML

**Few-Shot Learning** - Pagbibigay ng mga halimbawa sa mga prompt. [Module 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - AI models na sinanay sa malawak na datos ng teksto.

**Reasoning Effort** - Parameter ng GPT-5.2 na kumokontrol sa lalim ng pagiisip. [Module 02](../02-prompt-engineering/README.md)

**Temperature** - Kumokontrol sa randomness ng output. Mababa = deterministic, mataas = malikhain.

**Vector Database** - Espesyal na database para sa embeddings. [Module 03](../03-rag/README.md)

**Zero-Shot Learning** - Pagsasagawa ng mga gawain nang walang mga halimbawa. [Module 02](../02-prompt-engineering/README.md)

## Guardrails - [Module 00](../00-quick-start/README.md)

**Defense in Depth** - Multi-layer na seguridad na pinagsasama ang guardrails sa antas ng aplikasyon at mga provider safety filters.

**Hard Block** - Nagbibigay ang provider ng HTTP 400 error para sa matinding paglabag sa nilalaman.

**InputGuardrail** - Interface ng LangChain4j para sa pag-validate ng input ng user bago ito makarating sa LLM. Nakakatipid ng gastos at latency sa pamamagitan ng pag-block ng mga mapanganib na prompt nang maaga.

**InputGuardrailResult** - Uri ng return para sa pag-validate ng guardrail: `success()` o `fatal("reason")`.

**OutputGuardrail** - Interface para sa pag-validate ng mga tugon ng AI bago ibalik sa mga gumagamit.

**Provider Safety Filters** - Built-in na mga filter ng nilalaman mula sa mga AI provider (hal., GitHub Models) na humuhuli ng paglabag sa antas ng API.

**Soft Refusal** - Magalang na pagtanggi ng modelo na sagutin nang hindi nagbato ng error.

## Prompt Engineering - [Module 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Hakbang-hakbang na pagiisip para sa mas mahusay na katumpakan.

**Constrained Output** - Pagpapatupad ng partikular na format o istraktura.

**High Eagerness** - Pattern ng GPT-5.2 para sa masusing reasoning.

**Low Eagerness** - Pattern ng GPT-5.2 para sa mabilis na sagot.

**Multi-Turn Conversation** - Pangangalaga ng konteksto sa mga palitan.

**Role-Based Prompting** - Pagtatakda ng persona ng modelo sa pamamagitan ng mga system messages.

**Self-Reflection** - Pagsusuri at pagpapabuti ng output ng modelo.

**Structured Analysis** - Nakapirming framework ng pagsusuri.

**Task Execution Pattern** - Plano → Isakatuparan → Buod.

## RAG (Retrieval-Augmented Generation) - [Module 03](../03-rag/README.md)

**Document Processing Pipeline** - Load → chunk → embed → store.

**In-Memory Embedding Store** - Hindi permanenteng imbakan para sa pagsubok.

**RAG** - Pinaghalong retrieval at generation upang maging grounded ang mga tugon.

**Similarity Score** - Sukat (0-1) ng semantic similarity.

**Source Reference** - Metadata tungkol sa nakuha na nilalaman.

## Mga Ahente at Mga Kasangkapan - [Module 04](../04-tools/README.md)

**@Tool Annotation** - Nagmamarka ng mga Java method bilang mga tool na maaaring tawagin ng AI.

**ReAct Pattern** - Reason → Act → Observe → Repeat.

**Session Management** - Hiwa-hiwalay na mga konteksto para sa iba't ibang user.

**Tool** - Function na maaaring tawagin ng AI agent.

**Tool Description** - Dokumentasyon ng layunin at mga parameter ng tool.

## Agentic Module - [Module 05](../05-mcp/README.md)

**@Agent Annotation** - Nagmamarka ng mga interface bilang AI agent na may deklaratibong depinisyon ng pag-uugali.

**Agent Listener** - Hook para sa pagmamanman ng pagpatupad ng ahente gamit ang `beforeAgentInvocation()` at `afterAgentInvocation()`.

**Agentic Scope** - Shared memory kung saan nag-iimbak ang mga ahente ng mga output gamit ang `outputKey` para magamit ng downstream agents.

**AgenticServices** - Pabrika para sa paggawa ng mga ahente gamit ang `agentBuilder()` at `supervisorBuilder()`.

**Conditional Workflow** - Ruta batay sa mga kundisyon patungo sa iba't ibang espesyalistang ahente.

**Human-in-the-Loop** - Pattern ng workflow na nagdaragdag ng mga checkpoint ng tao para sa pag-apruba o pagrebisa ng nilalaman.

**langchain4j-agentic** - Maven dependency para sa deklaratibong paggawa ng ahente (eksperimento).

**Loop Workflow** - Paulit-ulit na pagpapatupad ng agent hanggang matugunan ang isang kondisyon (hal., quality score ≥ 0.8).

**outputKey** - Parameter ng annotation ng ahente na nagsasaad kung saan iniimbak ang mga resulta sa Agentic Scope.

**Parallel Workflow** - Sabayang pagpapatakbo ng maraming agent para sa magkakahiwalay na gawain.

**Response Strategy** - Paraan ng tagapamahala sa pagbuo ng huling sagot: LAST, SUMMARY, o SCORED.

**Sequential Workflow** - Sunud-sunod na pagpapatupad ng mga ahente kung saan ang output ay dumadaloy sa susunod na hakbang.

**Supervisor Agent Pattern** - Advanced na pattern ng agentic kung saan ang supervisor LLM ay dinamiko na nagpapasya kung aling sub-agent ang tatawagin.

## Model Context Protocol (MCP) - [Module 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven dependency para sa integrasyon ng MCP sa LangChain4j.

**MCP** - Model Context Protocol: pamantayan para sa pagkonekta ng AI apps sa mga external na kasangkapan. Gumawa nang isang beses, gamitin saanman.

**MCP Client** - Aplikasyon na kumokonekta sa MCP servers upang tuklasin at gamitin ang mga kasangkapan.

**MCP Server** - Serbisyo na naglalantad ng mga kasangkapan sa pamamagitan ng MCP na may malinaw na paglalarawan at parameter schemas.

**McpToolProvider** - Komponent ng LangChain4j na bumabalot sa MCP tools para gamitin sa AI services at mga ahente.

**McpTransport** - Interface para sa komunikasyon ng MCP. Kasama sa mga implementasyon ang Stdio at HTTP.

**Stdio Transport** - Lokal na transport na proseso gamit ang stdin/stdout. Kapaki-pakinabang para sa access sa filesystem o mga command-line tool.

**StdioMcpTransport** - Implementasyon ng LangChain4j na nagpapalabas ng MCP server bilang subprocess.

**Tool Discovery** - Ang client ay nagtatanong sa server para sa mga magagamit na tool na may mga paglalarawan at schemas.

## Mga Serbisyo sa Azure - [Module 01](../01-introduction/README.md)

**Azure AI Search** - Cloud search na may vector na kakayahan. [Module 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Nagde-deploy ng mga Azure resources.

**Azure OpenAI** - Enterprise AI service ng Microsoft.

**Bicep** - Wika para sa infrastructure-as-code sa Azure. [Infrastructure Guide](../01-introduction/infra/README.md)

**Deployment Name** - Pangalan para sa deployment ng modelo sa Azure.

**GPT-5.2** - Pinakabagong model ng OpenAI na may kontrol sa reasoning. [Module 02](../02-prompt-engineering/README.md)

## Pagsubok at Pagpapaunlad - [Testing Guide](TESTING.md)

**Dev Container** - Containerized na kapaligiran sa pagpapaunlad. [Configuration](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Libreng AI model playground. [Module 00](../00-quick-start/README.md)

**In-Memory Testing** - Pagsubok gamit ang in-memory na imbakan.

**Integration Testing** - Pagsubok gamit ang tunay na imprastruktura.

**Maven** - Java build automation tool.

**Mockito** - Java mocking framework.

**Spring Boot** - Java application framework. [Module 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Paunawa**:  
Ang dokumentong ito ay isinalin gamit ang AI na serbisyo sa pagsasalin na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagaman nagsusumikap kami para sa katumpakan, pakatandaan na ang automatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o di-tumpak na impormasyon. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na opisyal na sanggunian. Para sa mahahalagang impormasyon, ipinapayo ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang hindi pagkakaintindihan o maling interpretasyon na nagmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->