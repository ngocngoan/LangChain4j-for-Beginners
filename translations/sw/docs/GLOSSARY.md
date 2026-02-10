# Kamusi ya LangChain4j

## Jedwali la Maudhui

- [Madhumuni ya Msingi](../../../docs)
- [Vipengele vya LangChain4j](../../../docs)
- [Madhumuni ya AI/ML](../../../docs)
- [Misingi ya Usalama](../../../docs)
- [Uhandisi wa Maagizo](../../../docs)
- [RAG (Uundaji Unaosaidiwa na Utafutaji)](../../../docs)
- [Wakala na Vifaa](../../../docs)
- [Moduli ya Wakala](../../../docs)
- [Itifaki ya Muktadha wa Mfano (MCP)](../../../docs)
- [Huduma za Azure](../../../docs)
- [Upimaji na Maendelezaji](../../../docs)

Marejeo ya haraka kwa maneno na dhana zinazotumika katika kozi yote.

## Madhumuni ya Msingi

**AI Agent** - Mfumo unaotumia AI kufikiria na kuchukua hatua kwa uhuru. [Moduli 04](../04-tools/README.md)

**Chain** - Mfululizo wa shughuli ambapo matokeo yanatumika kwenye hatua inayofuata.

**Chunking** - Kugawanya nyaraka katika vipande vidogo vidogo. Kawaida: tokeni 300-500 zenye mchanganyiko. [Moduli 03](../03-rag/README.md)

**Context Window** - Tokeni kubwa zaidi ambazo mfano unaweza kusindika. GPT-5.2: tokeni 400K.

**Embeddings** - Vektori za nambari zinazowakilisha maana ya maandishi. [Moduli 03](../03-rag/README.md)

**Function Calling** - Mfano hutengeneza maombi ya muundo wa kupiga simu za kazi za nje. [Moduli 04](../04-tools/README.md)

**Hallucination** - Agizo linalotengeneza taarifa isiyo sahihi lakini inaonekana kuwa ya kweli.

**Prompt** - Ingizo la maandishi kwa mfano wa lugha. [Moduli 02](../02-prompt-engineering/README.md)

**Semantic Search** - Utafutaji kwa maana kwa kutumia embeddings, si maneno muhimu. [Moduli 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: haina kumbukumbu. Stateful: huendeleza rekodi ya mazungumzo. [Moduli 01](../01-introduction/README.md)

**Tokens** - Vitengo vya msingi vya maandishi ambavyo mifano husindika. Hupata athari kwa gharama na mipaka. [Moduli 01](../01-introduction/README.md)

**Tool Chaining** - Kutekeleza zana kwa mfuatano ambapo matokeo huathiri simu inayofuata. [Moduli 04](../04-tools/README.md)

## Vipengele vya LangChain4j

**AiServices** - Hutengeneza interface za huduma za AI zenye usalama wa aina.

**OpenAiOfficialChatModel** - Mteja muhtasari kwa mifano ya OpenAI na Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Hutengeneza embeddings kwa kutumia mteja rasmi wa OpenAI (huunga mkono OpenAI na Azure OpenAI).

**ChatModel** - Interface kuu kwa mifano ya lugha.

**ChatMemory** - Huhifadhi rekodi ya mazungumzo.

**ContentRetriever** - Hutafuta vipande muhimu vya nyaraka kwa RAG.

**DocumentSplitter** - Hugawanya nyaraka katika vipande.

**EmbeddingModel** - Hubadilisha maandishi kuwa vektori za nambari.

**EmbeddingStore** - Huhifadhi na hurudisha embeddings.

**MessageWindowChatMemory** - Huhifadhi dirisha la msogeo la ujumbe za hivi karibuni.

**PromptTemplate** - Hutengeneza maagizo yanayoweza kutumika tena yenye alama za `{{variable}}`.

**TextSegment** - Kipande cha maandishi chenye metadata. Kinatumika katika RAG.

**ToolExecutionRequest** - Huonyesha ombi la kutekeleza zana.

**UserMessage / AiMessage / SystemMessage** - Aina za ujumbe wa mazungumzo.

## Madhumuni ya AI/ML

**Few-Shot Learning** - Kuwasilisha mifano katika maagizo. [Moduli 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - Mifano ya AI iliyofunzwa kwa data kubwa ya maandishi.

**Reasoning Effort** - Kigezo cha GPT-5.2 kinachodhibiti kina cha kufikiri. [Moduli 02](../02-prompt-engineering/README.md)

**Temperature** - Hudhibiti randomness ya matokeo. Chini=hakika, juu=ubunifu.

**Vector Database** - Hifadhidata maalum kwa embeddings. [Moduli 03](../03-rag/README.md)

**Zero-Shot Learning** - Kutekeleza kazi bila mifano. [Moduli 02](../02-prompt-engineering/README.md)

## Misingi ya Usalama - [Moduli 00](../00-quick-start/README.md)

**Defense in Depth** - Njia ya usalama ya safu nyingi inayochanganya misingi ya ngazi ya programu na vichujio vya usalama vya mtoa huduma.

**Hard Block** - Mtoa huduma anatuma kosa la HTTP 400 kwa ukiukaji mkali wa maudhui.

**InputGuardrail** - Interface ya LangChain4j ya kuthibitisha ingizo la mtumiaji kabla halijafika kwa LLM. Hutoa akiba ya gharama na ucheleweshaji kwa kuziba maagizo hatari mapema.

**InputGuardrailResult** - Aina ya kurudi kwa uthibitishaji wa guardrail: `success()` au `fatal("reason")`.

**OutputGuardrail** - Interface ya kuthibitisha majibu ya AI kabla hayarudishwi kwa watumiaji.

**Provider Safety Filters** - Vichujio vya maudhui vilivyojengwa na watoa AI (mfano, GitHub Models) ambavyo hukamata ukiukaji ngazi ya API.

**Soft Refusal** - Mfano hupinga kwa heshima kujibu bila kutoa kosa.

## Uhandisi wa Maagizo - [Moduli 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Ufikiri hatua kwa hatua kwa usahihi bora.

**Constrained Output** - Kulazimisha muundo au fomu mahususi.

**High Eagerness** - Mchoro wa GPT-5.2 kwa ufikiri wa kina.

**Low Eagerness** - Mchoro wa GPT-5.2 kwa majibu ya haraka.

**Multi-Turn Conversation** - Kuhifadhi muktadha kati ya mazungumzo mbalimbali.

**Role-Based Prompting** - Kuweka mhusika wa mfano kupitia ujumbe za mfumo.

**Self-Reflection** - Mfano huthamini na kuboresha matokeo yake.

**Structured Analysis** - Mfumo imara wa tathmini.

**Task Execution Pattern** - Panga → Tekeleza → Kamilisha.

## RAG (Uundaji Unaosaidiwa na Utafutaji) - [Moduli 03](../03-rag/README.md)

**Document Processing Pipeline** - Pakua → gawanya → fanya embed → hifadhi.

**In-Memory Embedding Store** - Hifadhi isiyo ya kudumu kwa majaribio.

**RAG** - Inachanganya utafutaji na uundaji ili kuimarisha majibu.

**Similarity Score** - Kipimo (0-1) cha ufananifu wa maana.

**Source Reference** - Metadata kuhusu maudhui yaliyopatikana.

## Wakala na Vifaa - [Moduli 04](../04-tools/README.md)

**@Tool Annotation** - Inaonyesha mbinu za Java kama zana zinazoweza kupigiwa simu na AI.

**ReAct Pattern** - Fikiria → Fanya → Angalia → Rudia.

**Session Management** - Muktadha tofauti kwa watumiaji tofauti.

**Tool** - Kazi ambayo wakala wa AI anaweza kuita.

**Tool Description** - Nyaraka za madhumuni na parameta za zana.

## Moduli ya Wakala - [Moduli 05](../05-mcp/README.md)

**@Agent Annotation** - Inaonyesha interface kama mawakala wa AI wenye ufafanuzi wa tabia waelekezi.

**Agent Listener** - Hook ya kufuatilia utekelezaji wa wakala kupitia `beforeAgentInvocation()` na `afterAgentInvocation()`.

**Agentic Scope** - Kumbukumbu shirikishi ambapo mawakala huhifadhi matokeo kwa kutumia `outputKey` kwa mawakala wa baadaye kuyatumia.

**AgenticServices** - Kiwanda cha kuunda mawakala kwa kutumia `agentBuilder()` na `supervisorBuilder()`.

**Conditional Workflow** - Njia kwa kuzingatia masharti kwenda kwa mawakala maalum tofauti.

**Human-in-the-Loop** - Mchoro wa mchakato unaoongeza hatua za kibinadamu kwa idhini au ukaguzi wa maudhui.

**langchain4j-agentic** - Uridhi wa Maven wa ujenzi wa wakala waelekezi (jaribio).

**Loop Workflow** - Rudia utekelezaji wa wakala hadi sharti lifanikike (mfano, alama ya ubora ≥ 0.8).

**outputKey** - Parameta ya maelezo ya wakala inayobainisha mahali pa kuhifadhi matokeo ndani ya Agentic Scope.

**Parallel Workflow** - Endesha mawakala wengi kwa wakati mmoja kwa kazi zisizohusiana.

**Response Strategy** - Jinsi msimamizi anavyotengeneza jibu la mwisho: LAST, SUMMARY, au SCORED.

**Sequential Workflow** - Tekeleza mawakala kwa mpangilio ambapo matokeo huenda kwenye hatua inayofuata.

**Supervisor Agent Pattern** - Mchoro wa hali ya juu wa wakala waelekezi ambapo LLM msimamizi hufanya maamuzi ya kutoana ni mawakala upi wa kidogo watekelezwe.

## Itifaki ya Muktadha wa Mfano (MCP) - [Moduli 05](../05-mcp/README.md)

**langchain4j-mcp** - Uridhi wa Maven kwa ujumuishaji MCP katika LangChain4j.

**MCP** - Itifaki ya Muktadha wa Mfano: kiwango cha kuunganisha programu za AI na zana za nje. Jenga mara moja, tumia kila mahali.

**MCP Client** - Programu inayounganisha na seva za MCP ili kugundua na kutumia zana.

**MCP Server** - Huduma inayotoa zana kupitia MCP kwa maelezo wazi na muundo wa parameta.

**McpToolProvider** - Kipengele cha LangChain4j kinachozunguka zana za MCP kwa matumizi katika huduma na mawakala wa AI.

**McpTransport** - Interface ya mawasiliano ya MCP. Kutekelezwa ni pamoja na Stdio na HTTP.

**Stdio Transport** - Usafirishaji wa mchakato wa ndani kupitia stdin/stdout. Inafaa kwa ufikiaji wa mfumo wa faili au zana za mstari wa amri.

**StdioMcpTransport** - Utekelezaji wa LangChain4j unaochoma seva ya MCP kama mchakato mdogo.

**Tool Discovery** - Mteja huuliza seva kwa zana zinazopatikana zenye maelezo na miundo.

## Huduma za Azure - [Moduli 01](../01-introduction/README.md)

**Azure AI Search** - Utafutaji wa wingu unaoendeshwa na vector. [Moduli 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Hupakia rasilimali za Azure.

**Azure OpenAI** - Huduma ya AI ya kampuni ya Microsoft.

**Bicep** - Lugha ya Azure ya miundombinu kama msimbo. [Mwongozo wa Miundombinu](../01-introduction/infra/README.md)

**Deployment Name** - Jina la utekelezaji wa mfano katika Azure.

**GPT-5.2** - Mfano mpya wa OpenAI wenye usimamizi wa fikra. [Moduli 02](../02-prompt-engineering/README.md)

## Upimaji na Maendelezaji - [Mwongozo wa Upimaji](TESTING.md)

**Dev Container** - Mazingira ya maendeleo yaliyowekwa kwenye container. [Marekebisho](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Eneo la bure la majaribio ya mfano wa AI. [Moduli 00](../00-quick-start/README.md)

**In-Memory Testing** - Upimaji kwa hifadhi ya ndani.

**Integration Testing** - Upimaji kwa miundombinu halisi.

**Maven** - Zana ya kujenga programu ya Java.

**Mockito** - Mfumo wa kuigiza wa Java.

**Spring Boot** - Mfumo wa programu wa Java. [Moduli 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tangazo la Hukumu**:
Nyaraka hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Wakati tunajitahidi kwa usahihi, tafadhali fahamu kuwa tafsiri za moja kwa moja zinaweza kuwa na makosa au usahihi mdogo. Nyaraka ya asili katika lugha yake ya asili inapaswa kuzingatiwa kama chanzo cha kuthibitishwa. Kwa taarifa muhimu, tafsiri ya kitaalamu inayofanywa na binadamu inapendekezwa. Hatubeba jukumu lolote kwa kutoelewana au tafsiri potofu zinazotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->