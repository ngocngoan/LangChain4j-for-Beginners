# LangChain4j Kamusi

## Jedwali la Yaliyomo

- [Misingi ya Misingi](../../../docs)
- [Sehemu za LangChain4j](../../../docs)
- [Misingi ya AI/ML](../../../docs)
- [Mlinzi](../../../docs)
- [Uhandisi wa Prompt](../../../docs)
- [RAG (Uzalishaji Ulioboreshwa na Urejeshaji)](../../../docs)
- [Wakala na Zana](../../../docs)
- [Kipengele cha Wakili](../../../docs)
- [Itifaki ya Muktadha wa Mfano (MCP)](../../../docs)
- [Huduma za Azure](../../../docs)
- [Upimaji na Maendeleo](../../../docs)

Marejeleo ya haraka kwa maneno na dhana zinazotumika katika kozi nzima.

## Misingi ya Msingi

**Wakala wa AI** - Mfumo unaotumia AI kufikiri na kutenda kwa uhuru. [Somo 04](../04-tools/README.md)

**Mnyororo** - Mfululizo wa shughuli ambapo matokeo huingia kwenye hatua inayofuata.

**Ugawaji Vipande** - Kugawanya nyaraka katika vipande vidogo. Kawaida: tokeni 300-500 zenye mkusanyiko. [Somo 03](../03-rag/README.md)

**Dirisha la Muktadha** - Tokeni nyingi zaidi ambazo mfano unaweza kusindika. GPT-5.2: tokeni 400K (hadi 272K ingizo, 128K matokeo).

**Embeddings** - Vektors za nambari zinazoonyesha maana ya maandishi. [Somo 03](../03-rag/README.md)

**Kuita Kazi** - Mfano hutengeneza maombi yaliyopangwa kwa kufungua vitengeza vya nje. [Somo 04](../04-tools/README.md)

**Halusinasheni** - Wakati mifano hutengeneza habari zisizo sahihi lakini zinaonekana kuwa halali.

**Prompt** - Ingizo la maandishi kwa mfano wa lugha. [Somo 02](../02-prompt-engineering/README.md)

**Utafutaji wa Semantiki** - Kutafuta kwa maana kwa kutumia embeddings, sio maneno muhimu. [Somo 03](../03-rag/README.md)

**Kiasi cha Hali vs Isiyo na Hali** - Isiyo na hali: hakuna kumbukumbu. Na hali: huhifadhi historia ya mazungumzo. [Somo 01](../01-introduction/README.md)

**Tokeni** - Vitengo vya msingi vya maandishi vinavyochakatwa na mifano. Huathiri gharama na vizingiti. [Somo 01](../01-introduction/README.md)

**Mnyororo wa Zana** - Utekelezaji wa zana mfululizo ambapo matokeo huonyesha mwito unaofuata. [Somo 04](../04-tools/README.md)

## Sehemu za LangChain4j

**AiServices** - Huunda interfaces salama za huduma za AI.

**OpenAiOfficialChatModel** - Mteja wa umoja kwa miundo ya OpenAI na Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Huunda embeddings kwa kutumia mteja rasmi wa OpenAI (huunga mkono OpenAI na Azure OpenAI).

**ChatModel** - Kiolesura kikuu kwa mifano ya lugha.

**ChatMemory** - Huhifadhi historia ya mazungumzo.

**ContentRetriever** - Hupata vipande vya nyaraka vinavyofaa kwa RAG.

**DocumentSplitter** - Hugawanya nyaraka katika vipande.

**EmbeddingModel** - Hubadilisha maandishi kuwa vektors za nambari.

**EmbeddingStore** - Huhifadhi na kurejesha embeddings.

**MessageWindowChatMemory** - Huhifadhi dirisha linalosogezwa la ujumbe wa karibuni.

**PromptTemplate** - Huunda prompts zinazoweza kutumika tena zenye mabano ya `{{variable}}`.

**TextSegment** - Kipande cha maandishi chenye metadata. Kinatumika katika RAG.

**ToolExecutionRequest** - Huonyesha ombi la utekelezaji wa zana.

**UserMessage / AiMessage / SystemMessage** - Aina za ujumbe wa mazungumzo.

## Misingi ya AI/ML

**Few-Shot Learning** - Kutoa mifano katika prompts. [Somo 02](../02-prompt-engineering/README.md)

**Mfano Mkubwa wa Lugha (LLM)** - Mifano ya AI iliyofunzwa kwa data nyingi za maandishi.

**Juhudi za Kuhesabu** - Parameta ya GPT-5.2 inayodhibiti undani wa fikra. [Somo 02](../02-prompt-engineering/README.md)

**Joto** - Hudhibiti mwelekeo wa matokeo. Chini=msingi, juu=ubunifu.

**Hifadhidata ya Vektors** - Hifadhidata maalum kwa embeddings. [Somo 03](../03-rag/README.md)

**Zero-Shot Learning** - Kutekeleza majukumu bila mifano. [Somo 02](../02-prompt-engineering/README.md)

## Mlinzi - [Somo 00](../00-quick-start/README.md)

**Ulinzi kwa Ngazi Nyingi** - Mbinu ya usalama yenye tabaka nyingi ikijumuisha mlinzi wa kiwango cha programu na vichujio vya usalama vya mtoa huduma.

**Kuzuia Kuwezeshwa** - Mtoa huduma hutupa kosa la HTTP 400 kwa ukiukaji makubwa wa maudhui.

**InputGuardrail** - Kiolesura cha LangChain4j kwa kuthibitisha ingizo la mtumiaji kabla halijafika kwa LLM. Huhifadhi gharama na kuchelewesha kwa kuzuia prompts hatarishi mapema.

**InputGuardrailResult** - Aina ya kurudisha kwa uthibitishaji wa mlinzi: `success()` au `fatal("reason")`.

**OutputGuardrail** - Kiolesura kwa kuthibitisha majibu ya AI kabla ya kurudisha kwa watumiaji.

**Provider Safety Filters** - Vichujio vya maudhui vilivyojengwa kutoka kwa watoa huduma za AI (mfano, GitHub Models) vinavyoshika ukiukaji katika kiwango cha API.

**Kukataa Kwa Heshima** - Mfano hukataa kwa heshima kujibu bila kutoa kosa.

## Uhandisi wa Prompt - [Somo 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Fikra kwa hatua kwa usahihi bora.

**Matokeo Yaliyolazimishwa** - Kulazimisha muundo au mfumo maalum.

**Shauku Zaidi** - Mfano wa GPT-5.2 kwa fikra za kina.

**Shauku Ndogo** - Mfano wa GPT-5.2 kwa majibu ya haraka.

**Mazungumzo ya Mizunguko Mingi** - Kuhifadhi muktadha kati ya kubadilishana.

**Prompting kwa Kulingana na Nafasi** - Kuweka tabia ya mfano kupitia ujumbe wa mfumo.

**Utambuzi wa Nafsi** - Mfano hupima na kuboresha matokeo yake.

**Uchambuzi Uliofungwa** - Mfumo wa tathmini uliowekwa.

**Mfumo wa Utekelezaji wa Kazi** - Panga → Tekeleza → Fupisha.

## RAG (Uzalishaji Ulioboreshwa na Urejeshaji) - [Somo 03](../03-rag/README.md)

**Mchakato wa Usindikaji wa Nyaraka** - Pakua → gawanya → ingiza → hifadhi.

**Hifadhi ya Embedding Isiyo ya Kudumu** - Hifadhi isiyoendelezwa kwa upimaji.

**RAG** - Inachanganya urejeshaji na uzalishaji kuweka majibu kwenye muktadha.

**Alama ya Ufanano** - Kipimo (0-1) cha ufanano wa semantiki.

**Marejeo ya Chanzo** - Metadata kuhusu maudhui yaliyopatikana.

## Wakala na Zana - [Somo 04](../04-tools/README.md)

**Maelezo ya @Tool** - Inaonyesha mbinu za Java kama zana zinazoweza kuitwa na AI.

**Mfumo wa ReAct** - Fikiri → Tenda → Angalia → Rudia.

**Usimamizi wa Kikao** - Muktadha tofauti kwa watumiaji mbalimbali.

**Zana** - Kazi ambayo wakala wa AI anaweza kuita.

**Maelezo ya Zana** - Nyaraka ya kusudi la zana na vigezo.

## Kipengele cha Wakili - [Somo 05](../05-mcp/README.md)

**Maelezo ya @Agent** - Inaonyesha interfaces kama wakala wa AI yenye kigezo cha tabia kilichosemwa.

**Msimamizi wa Wakala** - Kiungo cha kufuatilia utekelezaji wa wakala kupitia `beforeAgentInvocation()` na `afterAgentInvocation()`.

**Eneo la Agentic** - Kumbukumbu inayoshirikiwa ambapo mawakala huhifadhi matokeo kwa kutumia `outputKey` kwa mawakala wa hatua inayofuata.

**AgenticServices** - Kiwanda cha kuunda mawakala kwa kutumia `agentBuilder()` na `supervisorBuilder()`.

**Kazi ya Masharti** - Njia kulingana na masharti kwa mawakala maalum tofauti.

**Human-in-the-Loop** - Mfano wa mchakato unaoingiza binadamu kuangalia au kutoa idhini.

**langchain4j-agentic** - Tegemezi la Maven kwa uundaji wa wakala kwa njia ya kidokezo (jaribio).

**mzunguko wa kazi** - Rudia utekelezaji wa wakala mpaka hali itimizwe (mfano, alama ya ubora ≥ 0.8).

**outputKey** - Kigezo cha maelezo ya wakala kinachoeleza mahali matokeo huhifadhiwa katika Eneo la Agentic.

**Mchakato wa Kazi kwa Nguvu Moja** - Endesha mawakala wengi kwa wakati mmoja kwa majukumu huru.

**Mikakati ya Majibu** - Jinsi msimamizi anavyoandaa jibu la mwisho: LAST, SUMMARY, au SCORED.

**Mchakato wa Kazi Mfululizo** - Tekeleza mawakala kwa mpangilio ambapo matokeo huingia kwenye hatua inayofuata.

**Mfano wa Wakili wa Msimamizi** - Mfano wa kipekee wa wakala ambapo msimamizi wa LLM huamua kwa nguvu ni mawakala gani ndogo wataiwe.

## Itifaki ya Muktadha wa Mfano (MCP) - [Somo 05](../05-mcp/README.md)

**langchain4j-mcp** - Tegemezi la Maven kwa muunganiko wa MCP katika LangChain4j.

**MCP** - Itifaki ya Muktadha wa Mfano: kiwango cha kuunganisha programu za AI na zana za nje. Jenga mara moja, tumia kila mahali.

**Mteja wa MCP** - Programu inayounganisha na seva za MCP kugundua na kutumia zana.

**Server ya MCP** - Huduma inayotoa zana kupitia MCP zenye maelezo wazi na michoro ya vigezo.

**McpToolProvider** - Sehemu ya LangChain4j inayojifunga zana za MCP kwa ajili ya matumizi katika huduma za AI na mawakala.

**McpTransport** - Kiolesura cha mawasiliano ya MCP. Matumizi ni pamoja na Stdio na HTTP.

**Usafirishaji wa Stdio** - Usafirishaji wa mchakato wa ndani kupitia stdin/stdout. Hufaa kwa upatikanaji wa mfumo wa faili au zana za mstari wa amri.

**StdioMcpTransport** - Utekelezaji wa LangChain4j unaounda seva ya MCP kama mchakato msaidizi.

**Ugunduzi wa Zana** - Mteja huuliza seva kuhusu zana zinazopatikana zenye maelezo na michoro.

## Huduma za Azure - [Somo 01](../01-introduction/README.md)

**Azure AI Search** - Utafutaji wa wingu yenye uwezo wa vektors. [Somo 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Hupeleka rasilimali za Azure.

**Azure OpenAI** - Huduma ya AI ya kampuni ya Microsoft.

**Bicep** - Lugha ya Azure ya miundombinu kama msimbo. [Mwongozo wa Miundombinu](../01-introduction/infra/README.md)

**Jina la Utekelezaji** - Jina la utekelezaji wa mfano katika Azure.

**GPT-5.2** - Mfano wa hivi karibuni wa OpenAI wenye udhibiti wa fikra. [Somo 02](../02-prompt-engineering/README.md)

## Upimaji na Maendeleo - [Mwongozo wa Upimaji](TESTING.md)

**Dev Container** - Mazingira ya maendeleo yaliyopakiwa ndani ya kontena. [Mpangilio](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Uwanja wa majaribio wa mfano wa AI bure. [Somo 00](../00-quick-start/README.md)

**Upimaji wa Kumbukumbu Ndani** - Upimaji kwa kutumia hifadhi ya ndani.

**Upimaji wa Muunganiko** - Upimaji kwa kutumia miundombinu halisi.

**Maven** - Kifaa cha ujenzi cha programu ya Java.

**Mockito** - Mfumo wa kuiga kwa Java.

**Spring Boot** - Mfumo wa programu ya Java. [Somo 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tangazo la Majadiliano**:
Nyaraka hii imefasiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kwa usahihi, tafadhali fahamu kwamba tafsiri za moja kwa moja zinaweza kuwa na makosa au upungufu wa usahihi. Nyaraka ya asili katika lugha yake halisi inapaswa kuzingatiwa kama chanzo halali. Kwa habari muhimu, tafsiri ya kitaalamu ya binadamu inapendekezwa. Hatubeba dhamana zozote kwa kutoelewana au tafsiri zisizo sahihi zinazosababishwa na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->