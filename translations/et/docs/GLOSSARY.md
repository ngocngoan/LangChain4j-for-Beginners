# LangChain4j Glosaar

## Sisukord

- [Põhikontseptsioonid](../../../docs)
- [LangChain4j komponendid](../../../docs)
- [AI/ML kontseptsioonid](../../../docs)
- [Turvaelemendid](../../../docs)
- [Promptide inseneritöö](../../../docs)
- [RAG (Otsimist toetav genereerimine)](../../../docs)
- [Agentid ja tööriistad](../../../docs)
- [Agentne moodul](../../../docs)
- [Mudelikonteksti protokoll (MCP)](../../../docs)
- [Azure teenused](../../../docs)
- [Testimine ja arendamine](../../../docs)

Kiirviide kogu kursuse vältel kasutatud terminitele ja mõistetele.

## Põhikontseptsioonid

**AI Agent** – Süsteem, mis kasutab tehisintellekti autonoomseks mõtlemiseks ja tegutsemiseks. [Moodul 04](../04-tools/README.md)

**Chain** – Tegemiste jada, kus väljund suunatakse järgmisele etapile.

**Chunking** – Dokumentide tükeldamine väiksemateks osadeks. Tavaliselt 300-500 tokenit koos kattuvusega. [Moodul 03](../03-rag/README.md)

**Context Window** – Maksimaalne tokenite arv, mida mudel suudab töödelda. GPT-5.2: 400 000 tokenit.

**Embeddings** – Teksti tähendust numbriliste vektorite kujul representatsioon. [Moodul 03](../03-rag/README.md)

**Function Calling** – Mudel genereerib struktureeritud päringuid välist funktsioonide kutsumiseks. [Moodul 04](../04-tools/README.md)

**Hallucination** – Kui mudel genereerib valesid, kuid usutavaid andmeid.

**Prompt** – Tekstisisend keelemudelile. [Moodul 02](../02-prompt-engineering/README.md)

**Semantic Search** – Otsing tähenduse järgi, kasutades embeddings’e asemel märksõnu. [Moodul 03](../03-rag/README.md)

**Stateful vs Stateless** – Stateless: ei hoia mälu. Stateful: säilitab vestluse ajalugu. [Moodul 01](../01-introduction/README.md)

**Tokens** – Teksti põhiühikud, mida mudel töödeldakse. Mõjutab kulusid ja piiranguid. [Moodul 01](../01-introduction/README.md)

**Tool Chaining** – Tööriistade järjestikune kasutus, kus väljund mõjutab järgmist kutsumist. [Moodul 04](../04-tools/README.md)

## LangChain4j komponendid

**AiServices** – Loob tüübiturvalised AI teenuse liidesed.

**OpenAiOfficialChatModel** – Ühtne klient OpenAI ja Azure OpenAI mudelitele.

**OpenAiOfficialEmbeddingModel** – Loob embeddings’e kasutades OpenAI ametlikku klienti (toetab nii OpenAI kui Azure OpenAI).

**ChatModel** – Keelemudelite põhiliides.

**ChatMemory** – Hooldab vestluse ajalugu.

**ContentRetriever** – Leiab RAG jaoks asjakohased dokumendi tükid.

**DocumentSplitter** – Jagab dokumendid osadeks.

**EmbeddingModel** – Muudab teksti numbrilisteks vektoriteks.

**EmbeddingStore** – Salvestab ja otsib embeddings’e.

**MessageWindowChatMemory** – Hoiab liugakna viimastest sõnumitest.

**PromptTemplate** – Loob taaskasutatavaid päringuid koos `{{variable}}` kohatäitega.

**TextSegment** – Tekstitükk metainfo ja kaasnevaga. Kasutatakse RAG-s.

**ToolExecutionRequest** – Esindab tööriista täitmise päringut.

**UserMessage / AiMessage / SystemMessage** – Vestluse sõnumitüübid.

## AI/ML kontseptsioonid

**Few-Shot Learning** – Näidete andmine päringutes. [Moodul 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** – Suured tehisintellekti mudelid, mis on treenitud suurel hulgal tekstidel.

**Reasoning Effort** – GPT-5.2 parameeter, mis juhib mõtlemise sügavust. [Moodul 02](../02-prompt-engineering/README.md)

**Temperature** – Kontrollib väljundi juhuslikkust. Madal=deterministlik, kõrge=loominguline.

**Vector Database** – Spetsiaalne andmebaas embeddings’ite jaoks. [Moodul 03](../03-rag/README.md)

**Zero-Shot Learning** – Ülesannete sooritamine ilma näideteta. [Moodul 02](../02-prompt-engineering/README.md)

## Turvaelemendid - [Moodul 00](../00-quick-start/README.md)

**Defense in Depth** – Mitmekihiline turvalahendus, mis kombineerib rakenduse turvaelemendid pakkuja ohutussõeladega.

**Hard Block** – Pakkuja viskab HTTP 400 veateate raske sisurikkumise puhul.

**InputGuardrail** – LangChain4j liides kasutaja sisendi valideerimiseks enne LLM-i läbimist. Säästab kulusid ja latentsust, blokeerides kahjulikud päringud juba varakult.

**InputGuardrailResult** – Tagastustüüp turvavalideerimise tulemusel: `success()` või `fatal("põhjus")`.

**OutputGuardrail** – Liides AI vastuste valideerimiseks enne kasutajale tagastamist.

**Provider Safety Filters** – Sisseehitatud sisufiltrid AI pakkujatelt (nt GitHub Models), mis püüavad rikkumisi API tasandil.

**Soft Refusal** – Mudel keeldub viisakalt vastamast, ilma veateadet viskamata.

## Promptide inseneritöö - [Moodul 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** – Samm-sammuline põhjendus paremaks täpsuseks.

**Constrained Output** – Konkreetses formaadis või struktuuris väljundi nõudmine.

**High Eagerness** – GPT-5.2 muster põhjalikuks mõtlemiseks.

**Low Eagerness** – GPT-5.2 muster kiireteks vastusteks.

**Multi-Turn Conversation** – Konteksti hoidmine vahetuste vahel.

**Role-Based Prompting** – Mudeli persona seadmine süsteemsete sõnumite kaudu.

**Self-Reflection** – Mudel hindab ja täiustab oma väljundit.

**Structured Analysis** – Fikseeritud hindamismetoodika.

**Task Execution Pattern** – Plaan → Täida → Kokkuvõtte tee.

## RAG (Otsimist toetav genereerimine) - [Moodul 03](../03-rag/README.md)

**Document Processing Pipeline** – Laadi → tükelda → tekita embeddings’e → salvesta.

**In-Memory Embedding Store** – Mittepüsiv hoiustamisviis testimiseks.

**RAG** – Ühendab otsingu ja genereerimise, et maandada vastuseid.

**Similarity Score** – Semantilise sarnasuse mõõt (0-1).

**Source Reference** – Lähteinfo metaandmed leitud sisust.

## Agentid ja tööriistad - [Moodul 04](../04-tools/README.md)

**@Tool Annotation** – Märgistab Java meetodid AI kaudu kutsutavateks tööriistadeks.

**ReAct Pattern** – Mõtle → Tegutse → Hinda → Korda.

**Session Management** – Eri kasutajate konteksti eraldamine.

**Tool** – Funktsioon, mida AI agent suudab kutsuda.

**Tool Description** – Dokumentatsioon tööriista eesmärgi ja parameetrite kohta.

## Agentne moodul - [Moodul 05](../05-mcp/README.md)

**@Agent Annotation** – Märgistab liidesed AI agentideks deklaratiivse käitumise definitsiooniga.

**Agent Listener** – Nõelag toimingu jälgimiseks `beforeAgentInvocation()` ja `afterAgentInvocation()` kaudu.

**Agentic Scope** – Jagatud mälu, kus agentide väljundid salvestatakse `outputKey` kaudu järgmiste agentide tarbeks.

**AgenticServices** – Tehasekomponent agentide loomiseks kasutades `agentBuilder()` ja `supervisorBuilder()`.

**Conditional Workflow** – Voog, mis tingimuste põhjal suunab eri spetsialistagentide poole.

**Human-in-the-Loop** – Töövoo muster, mis lisab inimlikke kontrolle kinnitamiseks või sisu ülevaatuseks.

**langchain4j-agentic** – Maven'i sõltuvus deklaratiivseks agentide ehitamiseks (katsetusjärgus).

**Loop Workflow** – Agentide korduv käivitamine, kuni täidetud tingimus (nt kvaliteediskoor ≥ 0.8).

**outputKey** – Agent annotatsiooni parameeter, mis määrab, kuhu tulemused Agentic Scope’i salvestatakse.

**Parallel Workflow** – Mitme agendi samaaegne käivitamine sõltumatute ülesannete täitmiseks.

**Response Strategy** – Kuidas juhendaja koostab lõpliku vastuse: LAST, SUMMARY või SCORED.

**Sequential Workflow** – Agendid täidetakse järjestikku, väljund liigub järgmisele etapile.

**Supervisor Agent Pattern** – Täiustatud agentne muster, kus juhendaja LLM otsustab dünaamiliselt, milliseid alamagente kutsuda.

## Mudelikonteksti protokoll (MCP) - [Moodul 05](../05-mcp/README.md)

**langchain4j-mcp** – Maven'i sõltuvus MCP integratsiooniks LangChain4j's.

**MCP** – Mudelikonteksti protokoll: standard AI rakenduste ühendamiseks välistingimuste tööriistadega. Ehita korra, kasuta kõikjal.

**MCP Client** – Rakendus, mis ühendub MCP serveritega, tööriistade leidmiseks ja kasutamiseks.

**MCP Server** – Teenus, mis pakub tööriistu MCP kaudu selgete kirjelduste ja parameetri skeemidega.

**McpToolProvider** – LangChain4j komponent, mis teeb MCP tööriistad AI teenustes ja agentides kasutatavaks.

**McpTransport** – Liides MCP kommunikatsiooniks. Rakendused sisaldavad Stdio ja HTTP.

**Stdio Transport** – Kohalik protsessi transport stdin/stdout kaudu. Kasulik failisüsteemi ligipääsuks või käsureatööriistade jaoks.

**StdioMcpTransport** – LangChain4j rakendus, mis käivitab MCP serveri alamprotsessina.

**Tool Discovery** – Klient küsib serverilt saadavalolevate tööriistade kohta koos kirjelduste ja skeemidega.

## Azure teenused - [Moodul 01](../01-introduction/README.md)

**Azure AI Search** – Pilvepõhine otsing vektorvõimete jaotusega. [Moodul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** – Azure ressursside juurutamine.

**Azure OpenAI** – Microsofti ettevõtte AI teenus.

**Bicep** – Azure infrastruktuuri-koodi keel. [Infrastruktuuri juhend](../01-introduction/infra/README.md)

**Deployment Name** – Mudeli juurutamise nimi Azure’is.

**GPT-5.2** – Uusim OpenAI mudel mõtlemise juhtimisega. [Moodul 02](../02-prompt-engineering/README.md)

## Testimine ja arendamine - [Testimise juhend](TESTING.md)

**Dev Container** – Konteineripõhine arenduskeskkond. [Seadistus](../../../.devcontainer/devcontainer.json)

**GitHub Models** – Tasuta AI mudelite katseala. [Moodul 00](../00-quick-start/README.md)

**In-Memory Testing** – Testimine mälus hoiustamisega.

**Integration Testing** – Testimine päris infrastruktuuriga.

**Maven** – Java ehituse automatiseerimise tööriist.

**Mockito** – Java moki raamistiku.

**Spring Boot** – Java rakenduste raamistik. [Moodul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:
See dokument on tõlgitud kasutades tehisintellektil põhinevat tõlketeenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi me püüame tagada tõelevastavust, tuleb arvestada, et automaatsed tõlked võivad sisaldada vigu või ebatäpsusi. Algne dokument selle emakeeles tuleks pidada autoriteetseks allikaks. Tähtsa info puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta selle tõlke kasutamisest tulenevate arusaamatuste või moonutuste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->