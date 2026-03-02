# LangChain4j Talahulugan

## Talaan ng Nilalaman

- [Pangunahing Konsepto](../../../docs)
- [Mga Komponent ng LangChain4j](../../../docs)
- [Mga Konsepto sa AI/ML](../../../docs)
- [Mga Guardrails](../../../docs)
- [Prompt Engineering](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Mga Ahente at Mga Kasangkapan](../../../docs)
- [Agentic Module](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Mga Serbisyo ng Azure](../../../docs)
- [Pagsubok at Pag-unlad](../../../docs)

Mabilisang sanggunian para sa mga termino at konsepto na ginagamit sa buong kurso.

## Pangunahing Konsepto

**AI Agent** - Sistemang gumagamit ng AI para mag-isip at kumilos nang awtonomo. [Module 04](../04-tools/README.md)

**Chain** - Sunud-sunod na operasyon kung saan ang output ay nagsisilbing input sa susunod na hakbang.

**Chunking** - Paghiwa-hiwalay ng mga dokumento sa maliliit na bahagi. Karaniwang: 300-500 tokens na may overlap. [Module 03](../03-rag/README.md)

**Context Window** - Pinakamalaking bilang ng tokens na kayang i-proseso ng modelo. GPT-5.2: 400K tokens (hanggang 272K input, 128K output).

**Embeddings** - Numerikal na vector na kumakatawan sa kahulugan ng teksto. [Module 03](../03-rag/README.md)

**Function Calling** - Lumilikha ang modelo ng nakaestrukturang kahilingan para tumawag ng panlabas na mga function. [Module 04](../04-tools/README.md)

**Hallucination** - Kapag ang mga modelo ay nagbuo ng maling ngunit mukhang kapani-paniwalang impormasyon.

**Prompt** - Tekstong ipinasok sa isang language model. [Module 02](../02-prompt-engineering/README.md)

**Semantic Search** - Paghahanap batay sa kahulugan gamit ang embeddings, hindi paksa. [Module 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: walang memorya. Stateful: nagpapanatili ng kasaysayan ng usapan. [Module 01](../01-introduction/README.md)

**Tokens** - Pangunahing yunit ng teksto na pinoproseso ng mga modelo. Nakakaapekto sa gastos at limitasyon. [Module 01](../01-introduction/README.md)

**Tool Chaining** - Sunud-sunod na paggamit ng mga kasangkapan kung saan ang output ay ginagamit sa susunod na tawag. [Module 04](../04-tools/README.md)

## Mga Komponent ng LangChain4j

**AiServices** - Lumilikha ng type-safe na mga interface ng serbisyo ng AI.

**OpenAiOfficialChatModel** - Pinagsamang kliyente para sa OpenAI at Azure OpenAI models.

**OpenAiOfficialEmbeddingModel** - Lumilikha ng embeddings gamit ang OpenAI Official client (sumusuporta sa OpenAI at Azure OpenAI).

**ChatModel** - Pangunahing interface para sa mga language model.

**ChatMemory** - Nangangalaga ng kasaysayan ng usapan.

**ContentRetriever** - Nakakahanap ng mga kaugnay na bahagi ng dokumento para sa RAG.

**DocumentSplitter** - Naghahati ng mga dokumento sa maliliit na bahagi.

**EmbeddingModel** - Nagko-convert ng teksto sa numerikal na mga vector.

**EmbeddingStore** - Nag-iimbak at kumukuha ng mga embeddings.

**MessageWindowChatMemory** - Nangangalaga ng sliding window ng mga kamakailang mensahe.

**PromptTemplate** - Lumilikha ng reusable prompts gamit ang `{{variable}}` placeholders.

**TextSegment** - Piraso ng teksto na may metadata. Ginagamit sa RAG.

**ToolExecutionRequest** - Kinakatawan ang kahilingan para sa pagpapatupad ng kasangkapan.

**UserMessage / AiMessage / SystemMessage** - Mga uri ng mensahe sa usapan.

## Mga Konsepto sa AI/ML

**Few-Shot Learning** - Pagbibigay ng mga halimbawa sa mga prompt. [Module 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - AI models na sinanay gamit ang napakalaking dami ng teksto.

**Reasoning Effort** - Parameter ng GPT-5.2 na kumokontrol sa lalim ng pag-iisip. [Module 02](../02-prompt-engineering/README.md)

**Temperature** - Kumokontrol sa pagiging random ng output. Mababang value=deterministic, mataas=malikhain.

**Vector Database** - Espesyal na database para sa mga embeddings. [Module 03](../03-rag/README.md)

**Zero-Shot Learning** - Pagsasagawa ng mga gawain nang walang mga halimbawa. [Module 02](../02-prompt-engineering/README.md)

## Mga Guardrails - [Module 00](../00-quick-start/README.md)

**Defense in Depth** - Multi-layer na diskarte sa seguridad na pinagsasama ang application-level guardrails at provider safety filters.

**Hard Block** - Nagbibigay ang provider ng HTTP 400 error para sa matinding paglabag sa nilalaman.

**InputGuardrail** - LangChain4j interface para sa pag-validate ng input ng user bago makarating sa LLM. Nakakatipid sa gastos at latency sa pamamagitan ng maagang pagharang ng mapanganib na prompt.

**InputGuardrailResult** - Uri ng return para sa validation ng guardrail: `success()` o `fatal("reason")`.

**OutputGuardrail** - Interface para sa pag-validate ng tugon ng AI bago ibalik sa mga user.

**Provider Safety Filters** - Mga built-in na filter sa nilalaman mula sa AI providers (hal. GitHub Models) na naghuhuli ng paglabag sa API level.

**Soft Refusal** - Masinop na pagtanggi ng modelo na sumagot nang hindi nagbabato ng error.

## Prompt Engineering - [Module 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Hakbang-hakbang na pangangatwiran para sa mas mahusay na katumpakan.

**Constrained Output** - Pagpapatupad ng tiyak na format o estruktura.

**High Eagerness** - Pattern ng GPT-5.2 para sa masusing pangangatwiran.

**Low Eagerness** - Pattern ng GPT-5.2 para sa mabilisang sagot.

**Multi-Turn Conversation** - Pagpapanatili ng konteksto sa mga palitan.

**Role-Based Prompting** - Pagtatakda ng personalidad ng modelo sa pamamagitan ng mga system messages.

**Self-Reflection** - Pagsusuri at pagpapabuti ng output ng modelo.

**Structured Analysis** - Nakapirming balangkas ng pagsusuri.

**Task Execution Pattern** - Plano → Isagawa → Buodin.

## RAG (Retrieval-Augmented Generation) - [Module 03](../03-rag/README.md)

**Document Processing Pipeline** - Load → chunk → embed → store.

**In-Memory Embedding Store** - Hindi permanenteng imbakan para sa pagsubok.

**RAG** - Pinagsasama ang retrieval at generation para sa mas matibay na tugon.

**Similarity Score** - Sukatan (0-1) ng semantikong pagkakatulad.

**Source Reference** - Metadata tungkol sa nakuhang nilalaman.

## Mga Ahente at Mga Kasangkapan - [Module 04](../04-tools/README.md)

**@Tool Annotation** - Nagmamarka ng Java methods bilang AI-callable tools.

**ReAct Pattern** - Mag-isip → Kumilos → Obserbahan → Ulitin.

**Session Management** - Ihiwalay na mga konteksto para sa iba't ibang user.

**Tool** - Function na maaaring tawagin ng isang AI agent.

**Tool Description** - Dokumentasyon ng layunin at mga parameter ng kasangkapan.

## Agentic Module - [Module 05](../05-mcp/README.md)

**@Agent Annotation** - Nagmamarka ng mga interface bilang AI agents na may deklaratibong pagdedepina ng asal.

**Agent Listener** - Hook para sa pagmamanman ng execution ng ahente gamit ang `beforeAgentInvocation()` at `afterAgentInvocation()`.

**Agentic Scope** - Pinagsamang memorya kung saan iniimbak ng mga ahente ang mga output gamit ang `outputKey` para magamit ng mga sumusunod na ahente.

**AgenticServices** - Pabrika para sa paggawa ng mga ahente gamit ang `agentBuilder()` at `supervisorBuilder()`.

**Conditional Workflow** - Ruta base sa mga kundisyon papunta sa iba't ibang mga espesyalistang ahente.

**Human-in-the-Loop** - Workflow pattern na naglalagay ng mga checkpoint para sa tao para sa pag-apruba o pagsusuri ng nilalaman.

**langchain4j-agentic** - Maven dependency para sa deklaratibong pagbuo ng ahente (eksperimento).

**Loop Workflow** - Paulit-ulit na pagpapatakbo ng ahente hanggang matugunan ang isang kundisyon (hal. quality score ≥ 0.8).

**outputKey** - Parameter ng annotation ng ahente na nagsasaad kung saan iniimbak ang mga resulta sa Agentic Scope.

**Parallel Workflow** - Pagpapatakbo ng maramihang mga ahente nang sabay-sabay para sa mga independiyenteng gawain.

**Response Strategy** - Paraan ng supervisor sa pagbuo ng panghuling sagot: LAST, SUMMARY, o SCORED.

**Sequential Workflow** - Sunud-sunod na pagpapatakbo ng ahente kung saan ang output ay dumadaloy sa susunod na hakbang.

**Supervisor Agent Pattern** - Advanced na pattern ng ahente kung saan ang isang supervisor LLM ang dinamiko na nagdedesisyon kung alin sa mga sub-agents ang tatawagin.

## Model Context Protocol (MCP) - [Module 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven dependency para sa integrasyon ng MCP sa LangChain4j.

**MCP** - Model Context Protocol: pamantayan para sa pagkonekta ng mga AI app sa panlabas na mga kasangkapan. Gawin isang beses, gamitin saanman.

**MCP Client** - Aplikasyon na kumokonekta sa MCP servers para matuklasan at magamit ang mga kasangkapan.

**MCP Server** - Serbisyong nagpapakita ng mga kasangkapan sa pamamagitan ng MCP na may malinaw na mga paglalarawan at schema ng mga parameter.

**McpToolProvider** - Komponent ng LangChain4j na kumukulayet ng MCP tools para gamitin sa AI services at mga ahente.

**McpTransport** - Interface para sa komunikasyon ng MCP. Kasama ang mga implementasyon ng Stdio at HTTP.

**Stdio Transport** - Lokal na proseso na transport gamit ang stdin/stdout. Kapaki-pakinabang sa pag-access ng filesystem o mga command-line tools.

**StdioMcpTransport** - Implementasyon ng LangChain4j na nagpapatakbo ng MCP server bilang subprocess.

**Tool Discovery** - Kliyente na nagtatanong sa server para sa mga available na kasangkapan na may mga paglalarawan at schema.

## Mga Serbisyo ng Azure - [Module 01](../01-introduction/README.md)

**Azure AI Search** - Cloud search na may vector capabilities. [Module 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Nagde-deploy ng mga Azure resources.

**Azure OpenAI** - Serbisyo ng AI ng Microsoft para sa negosyo.

**Bicep** - Wika para sa Azure infrastructure-as-code. [Infrastructure Guide](../01-introduction/infra/README.md)

**Deployment Name** - Pangalan para sa deployment ng modelo sa Azure.

**GPT-5.2** - Pinakabagong modelo ng OpenAI na may kontrol sa pangangatwiran. [Module 02](../02-prompt-engineering/README.md)

## Pagsubok at Pag-unlad - [Testing Guide](TESTING.md)

**Dev Container** - Containerized na kapaligiran para sa pag-develop. [Configuration](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Libreng playground para sa AI models. [Module 00](../00-quick-start/README.md)

**In-Memory Testing** - Pagsubok gamit ang in-memory na imbakan.

**Integration Testing** - Pagsubok gamit ang totoong imprastraktura.

**Maven** - Tool para sa Java build automation.

**Mockito** - Java mocking framework.

**Spring Boot** - Java application framework. [Module 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Pagsasabi ng Pananagutan**:  
Ang dokumentong ito ay isinalin gamit ang serbisyo ng AI na pagsasalin [Co-op Translator](https://github.com/Azure/co-op-translator). Bagama't nagsusumikap kaming maging tumpak, pakatandaan na ang mga awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o di-katumpakan. Ang orihinal na dokumento sa kanyang orihinal na wika ang dapat ituring na opisyal na sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasaling pantao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na maaaring magmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->