# LangChain4j Sõnastik

## Sisukord

- [Põhikontseptsioonid](../../../docs)
- [LangChain4j komponendid](../../../docs)
- [AI/ML kontseptsioonid](../../../docs)
- [Guardrails](../../../docs)
- [Prompt Engineering](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agentid ja Tööriistad](../../../docs)
- [Agentic Moodul](../../../docs)
- [Mudeli Konteksti Protokoll (MCP)](../../../docs)
- [Azure Teenused](../../../docs)
- [Testimine ja Arendus](../../../docs)

Kiire viide kursuse jooksul kasutatud terminitele ja kontseptsioonidele.

## Põhikontseptsioonid

**AI Agent** - Süsteem, mis kasutab tehisintellekti autonoomseks mõtlemiseks ja tegutsemiseks. [Moodul 04](../04-tools/README.md)

**Chain** - Operatsioonide jada, kus väljund läheb järgmisse sammu.

**Chunking** - Dokumentide jagamine väiksemateks osadeks. Tüüpiline: 300-500 tokenit koos kattuvusega. [Moodul 03](../03-rag/README.md)

**Context Window** - Maksimaalne tokenite arv, mida mudel suudab töödelda. GPT-5.2: 400K tokenit (kuni 272K sisend, 128K väljund).

**Embeddings** - Teksti tähendust esindavad numbrilised vektorid. [Moodul 03](../03-rag/README.md)

**Function Calling** - Mudel genereerib struktureeritud päringuid väliste funktsioonide kutsumiseks. [Moodul 04](../04-tools/README.md)

**Hallucination** - Kui mudelid genereerivad vale, aga usutavat infot.

**Prompt** - Tekstisisend keelemudelile. [Moodul 02](../02-prompt-engineering/README.md)

**Semantic Search** - Otsing tähenduse järgi, kasutades embeddings'e, mitte märksõnu. [Moodul 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: mäluta. Stateful: hoiab vestluse ajalugu. [Moodul 01](../01-introduction/README.md)

**Tokens** - Teksti põhiüksused, mida mudelid töötlevad. Mõjutab kulusid ja piiranguid. [Moodul 01](../01-introduction/README.md)

**Tool Chaining** - Tööriistade järjestikune täitmine, kus väljund juhib järgmist kutset. [Moodul 04](../04-tools/README.md)

## LangChain4j komponendid

**AiServices** - Loob tüübiturvalisi tehisintellekti teenuste liideseid.

**OpenAiOfficialChatModel** - Ühtne klient OpenAI ja Azure OpenAI mudelitele.

**OpenAiOfficialEmbeddingModel** - Loob embeddings'e OpenAI ametliku kliendi abil (toetab nii OpenAI kui Azure OpenAI).

**ChatModel** - Keelemudelite põhiliides.

**ChatMemory** - Säilitab vestluse ajaloo.

**ContentRetriever** - Leiab RAG jaoks asjakohaseid dokumentide tükke.

**DocumentSplitter** - Jagab dokumendid osadeks.

**EmbeddingModel** - Konkreetsete võtmesõnade teisendamine numbrilisteks vektoriteks.

**EmbeddingStore** - Salvestab ja hangib embeddings'e.

**MessageWindowChatMemory** - Hoiab liikuva akna hiljutiste sõnumite jaoks.

**PromptTemplate** - Loob taaskasutatavaid prompt-malle koos `{{variable}}` kohatäiteks.

**TextSegment** - Tekstitükk metainfoga. Kasutatakse RAG koosseisus.

**ToolExecutionRequest** - Tööriista täitmise taotluse esitlus.

**UserMessage / AiMessage / SystemMessage** - Vestlusakna sõnumitüübid.

## AI/ML kontseptsioonid

**Few-Shot Learning** - Selgitavate näidete esitamine promptides. [Moodul 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - Suurekeelne mudel, mida on treenitud tohutul hulgal tekstidel.

**Reasoning Effort** - GPT-5.2 parameeter, mis juhib mõtlemise sügavust. [Moodul 02](../02-prompt-engineering/README.md)

**Temperature** - Juhtib väljundi juhuslikkust. Madal=deterministlik, kõrge=loov.

**Vector Database** - Spetsiaalne andmebaas embeddings'ite jaoks. [Moodul 03](../03-rag/README.md)

**Zero-Shot Learning** - Ülesannete täitmine ilma näideteta. [Moodul 02](../02-prompt-engineering/README.md)

## Guardrails - [Moodul 00](../00-quick-start/README.md)

**Defense in Depth** - Mitmetasandiline turvalahendus, mis kombineerib rakenduse tasandi piire ja pakkuja turvafiltreid.

**Hard Block** - Pakkuja viskab HTTP 400 vea tõsiste sisurikete korral.

**InputGuardrail** - LangChain4j liides kasutaja sisendi valideerimiseks enne LLM-i jõudmist. Säästab kulusid ja viivitusi, blokeerides kahjulikud promptid varakult.

**InputGuardrailResult** - Tagastustüüp guardrali valideerimisel: `success()` või `fatal("põhjus")`.

**OutputGuardrail** - Liides AI-väljundi valideerimiseks enne kasutajale tagastamist.

**Provider Safety Filters** - AI pakkujate (nt GitHub Models) sisseehitatud sisufiltrid, mis tabavad rikkumisi API tasemel.

**Soft Refusal** - Mudel keeldub viisakalt vastamast, ilma vea viskamata.

## Prompt Engineering - [Moodul 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Samm-sammuline põhjendamine parema täpsuse saavutamiseks.

**Constrained Output** - Spetsiifilise vormingu või struktuuri nõudmine.

**High Eagerness** - GPT-5.2 muster põhjalikuks põhjendamiseks.

**Low Eagerness** - GPT-5.2 muster kiireteks vastusteks.

**Multi-Turn Conversation** - Konteksti hoidmine mitme vahetuse jooksul.

**Role-Based Prompting** - Mudeli isikupära seadmine süsteemisõnumite kaudu.

**Self-Reflection** - Mudel hindab ja parandab oma väljundit.

**Structured Analysis** - Fikseeritud hindamisraamistik.

**Task Execution Pattern** - Planeeri → Täida → Kokkuvõtte.

## RAG (Retrieval-Augmented Generation) - [Moodul 03](../03-rag/README.md)

**Document Processing Pipeline** - Lae → tükelda → embedding → salvestus.

**In-Memory Embedding Store** - Mitte-püsiv salvestus testimiseks.

**RAG** - Kombineerib otsingu ja generatsiooni vastuste täpsustamiseks.

**Similarity Score** - Semantilise sarnasuse mõõt (0-1).

**Source Reference** - Metainfo otsitud sisu kohta.

## Agentid ja Tööriistad - [Moodul 04](../04-tools/README.md)

**@Tool Annotation** - Märgib Java meetodid AI-ga kutsutavateks tööriistadeks.

**ReAct Pattern** - Mõtle → Tegutse → Vaata → Korda.

**Session Management** - Eraldab erinevate kasutajate kontekstid.

**Tool** - Funktsioon, mida AI agent saab kutsuda.

**Tool Description** - Tööriista eesmärgi ja parameetrite dokumentatsioon.

## Agentic Moodul - [Moodul 05](../05-mcp/README.md)

**@Agent Annotation** - Märgib liidesed AI agentideks koos deklaratiivse käitumise määratlusega.

**Agent Listener** - Konks agentide täitmise jälgimiseks meetodite `beforeAgentInvocation()` ja `afterAgentInvocation()` kaudu.

**Agentic Scope** - Jagatud mälu, kus agentide väljundid talletatakse ja mida järgmised agentid tarbivad.

**AgenticServices** - Agentide loomiseks mõeldud tehas kasutades `agentBuilder()` ja `supervisorBuilder()`.

**Conditional Workflow** - Tingimuslik marsruutimine erinevate spetsialist-agentide juurde.

**Human-in-the-Loop** - Töötlemismuster, mis lisab inimkontrollpunktid kinnituseks või sisukontrolliks.

**langchain4j-agentic** - Maven sõltuvus deklaratiivseks agentide ehitamiseks (eksperimentaalne).

**Loop Workflow** - Agentide täitmine kordub, kuni tingimus on täidetud (nt kvaliteediskoor ≥ 0.8).

**outputKey** - Agendi annotatsiooni parameeter, mis määrab, kuhu tulemused Agentic Scope'is salvestatakse.

**Parallel Workflow** - Mitme agendi samaaegne käivitamine iseseisvate ülesannete jaoks.

**Response Strategy** - Kuidas juhendaja koostab lõpliku vastuse: LAST, SUMMARY või SCORED.

**Sequential Workflow** - Agentide järjekordne täitmine, kus väljund voolab järgmisesse sammu.

**Supervisor Agent Pattern** - Täiustatud agentide muster, kus juhendaja LLM otsustab dünaamiliselt, milliseid sub-agente kutsuda.

## Mudeli Konteksti Protokoll (MCP) - [Moodul 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven sõltuvus MCP integreerimiseks LangChain4j'sse.

**MCP** - Mudeli konteksti protokoll: standard AI rakenduste ühendamiseks väliste tööriistadega. Tee kord ja kasuta kõikjal.

**MCP Client** - Rakendus, mis ühendub MCP serveritega tööriistade leidmiseks ja kasutamiseks.

**MCP Server** - Teenus, mis eksponeerib tööriistu MCP kaudu koos selgete kirjelduste ja parameetrite skeemidega.

**McpToolProvider** - LangChain4j komponent, mis pakib MCP tööriistad AI teenuste ja agentide kasutamiseks.

**McpTransport** - Liides MCP side jaoks. Rakendused: Stdio ja HTTP.

**Stdio Transport** - Kohalik protsessi transport läbi stdin/stdout. Kasulik failisüsteemi juurdepääsuks või käsurea tööriistadele.

**StdioMcpTransport** - LangChain4j rakendus, mis käivitab MCP serveri alamprotsessina.

**Tool Discovery** - Klient küsib serverilt saadaval olevate tööriistade kirjeldusi ja skeeme.

## Azure Teenused - [Moodul 01](../01-introduction/README.md)

**Azure AI Search** - Pilvepõhine otsing koos vektorite võimetega. [Moodul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Azure ressursside juurutamine.

**Azure OpenAI** - Microsofti ettevõtete tehisintellekti teenus.

**Bicep** - Azure infrastruktuuri koodikeel. [Infrastruktuuri juhend](../01-introduction/infra/README.md)

**Deployment Name** - Nimi mudeli juurutamiseks Azure'is.

**GPT-5.2** - Viimane OpenAI mudel mõtlemise juhtimisega. [Moodul 02](../02-prompt-engineering/README.md)

## Testimine ja Arendus - [Testimise juhend](TESTING.md)

**Dev Container** - Konteinerpõhine arenduskeskkond. [Seadistus](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Tasuta AI mudelite mänguväljak. [Moodul 00](../00-quick-start/README.md)

**In-Memory Testimine** - Testimine mälu baasil salvestusega.

**Integration Testing** - Testimine päris infrastruktuuriga.

**Maven** - Java ehitustööriist.

**Mockito** - Java testimise raamistik.

**Spring Boot** - Java rakendusraamistik. [Moodul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:  
See dokument on tõlgitud kasutades tehisintellekti tõlketeenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi püüame täpsust, tuleb arvestada, et automatiseeritud tõlkes võivad esineda vead või ebatäpsused. Originaaldokument oma emakeeles tuleks pidada autoriteetseks allikaks. Kriitilise informatsiooni puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta selle tõlkega seotud arusaamatuste ega valesti tõlgendamise eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->