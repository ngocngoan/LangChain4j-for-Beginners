# LangChain4j Szótár

## Tartalomjegyzék

- [Alapvető Fogalmak](../../../docs)
- [LangChain4j Összetevők](../../../docs)
- [AI/ML Fogalmak](../../../docs)
- [Védőkorlátok](../../../docs)
- [Prompt Tervezés](../../../docs)
- [RAG (Keresésalapú Generálás)](../../../docs)
- [Agensek és Eszközök](../../../docs)
- [Agentikus Modul](../../../docs)
- [Modell Kontextus Protokoll (MCP)](../../../docs)
- [Azure Szolgáltatások](../../../docs)
- [Tesztelés és Fejlesztés](../../../docs)

Gyors hivatkozás a kurzus során használt kifejezésekhez és fogalmakhoz.

## Alapvető Fogalmak

**AI Agent** - Olyan rendszer, amely mesterséges intelligenciát használ az önálló gondolkodásra és cselekvésre. [Modul 04](../04-tools/README.md)

**Chain (Lánc)** - Műveletsorozat, ahol az egyik lépés kimenete a következő bemenete.

**Chunking (Darabolás)** - Dokumentumok kisebb részekre bontása. Jellemző méret: 300-500 token átfedéssel. [Modul 03](../03-rag/README.md)

**Context Window (Kontextus Ablak)** - A modell által egyszerre feldolgozható maximális tokenek száma. GPT-5.2: 400 ezer token.

**Embeddings (Beágyazások)** - Numerikus vektorok, amelyek a szöveg jelentését képviselik. [Modul 03](../03-rag/README.md)

**Function Calling (Függvényhívás)** - A modell strukturált kéréseket generál külső függvények hívására. [Modul 04](../04-tools/README.md)

**Hallucination (Hamis információ)** - Amikor a modellek helytelen, de hihető információt generálnak.

**Prompt** - Szöveges bemenet a nyelvi modellhez. [Modul 02](../02-prompt-engineering/README.md)

**Semantic Search (Szemantikus keresés)** - Keresés a jelentés alapján, beágyazásokat használva, nem kulcsszavakat. [Modul 03](../03-rag/README.md)

**Stateful vs Stateless (Állapotfüggő vagy állapotmentes)** - Állapotmentes: nincs memória. Állapotfüggő: megőrzi a beszélgetés előzményeit. [Modul 01](../01-introduction/README.md)

**Tokens (Tokenek)** - Alapvető szövegegységek, amelyeket a modellek feldolgoznak. Befolyásolják a költséget és korlátokat. [Modul 01](../01-introduction/README.md)

**Tool Chaining (Eszközláncolás)** - Eszközök egymás után futtatása, ahol a kimenet befolyásolja a következő hívást. [Modul 04](../04-tools/README.md)

## LangChain4j Összetevők

**AiServices** - Típusbiztos AI szolgáltatás interfészek létrehozása.

**OpenAiOfficialChatModel** - Egységes kliens az OpenAI és Azure OpenAI modellekhez.

**OpenAiOfficialEmbeddingModel** - Beágyazásokat készít az OpenAI Official klienssel (támogatja az OpenAI-t és az Azure OpenAI-t).

**ChatModel** - Alapvető interfész a nyelvi modellekhez.

**ChatMemory** - A beszélgetés előzményeinek megőrzése.

**ContentRetriever** - Releváns dokumentumrészek keresése RAG-hez.

**DocumentSplitter** - Dokumentumok darabolása.

**EmbeddingModel** - Szöveget numerikus vektorokká alakít.

**EmbeddingStore** - Beágyazások tárolása és lekérése.

**MessageWindowChatMemory** - Csúszó ablakos memória az utóbbi üzenetekhez.

**PromptTemplate** - Újrahasznosítható promptok létrehozása `{{variable}}` helyőrzőkkel.

**TextSegment** - Metaadatokkal ellátott szövegrész. RAG-ben használatos.

**ToolExecutionRequest** - Eszköz végrehajtási kérésének reprezentációja.

**UserMessage / AiMessage / SystemMessage** - Beszélgetési üzenettípusok.

## AI/ML Fogalmak

**Few-Shot Learning** - Példák megadása a promptokban. [Modul 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - Nagy adatállományokon tanított AI modellek.

**Reasoning Effort (Gondolkodási erőfeszítés)** - GPT-5.2 paraméter a gondolkodás mélységének szabályozására. [Modul 02](../02-prompt-engineering/README.md)

**Temperature (Hőmérséklet)** - A kimenet véletlenszerűségének szabályozása. Alacsony=detereminisztikus, magas=alkotó.

**Vector Database (Vektor adatbázis)** - Beágyazások tárolására specializált adatbázis. [Modul 03](../03-rag/README.md)

**Zero-Shot Learning** - Feladat végrehajtása példa nélkül. [Modul 02](../02-prompt-engineering/README.md)

## Védőkorlátok - [Modul 00](../00-quick-start/README.md)

**Defense in Depth (Többrétegű védelem)** - Többrétegű biztonsági megközelítés, amely ötvözi az alkalmazás szintű védeket a szolgáltatói biztonsági szűrőkkel.

**Hard Block (Kemény tiltás)** - Szolgáltató HTTP 400 hibát dob súlyos tartalmi akadályok esetén.

**InputGuardrail** - LangChain4j interfész a felhasználói bemenet érvényesítésére, mielőtt eljutna az LLM-hez. Költség- és késleltetéscsökkentés a káros promptok korai blokkolásával.

**InputGuardrailResult** - Visszatérési típus védőkorlát érvényesítéséhez: `success()` vagy `fatal("ok")`.

**OutputGuardrail** - Interfész a mesterséges intelligencia válaszainak ellenőrzésére, mielőtt visszaküldenék a felhasználónak.

**Provider Safety Filters (Szolgáltatói biztonsági szűrők)** - Beépített tartalomszűrők AI szolgáltatóktól (pl. GitHub Models), amelyek az API szinten észlelik a szabálysértéseket.

**Soft Refusal (Enyhe elutasítás)** - A modell udvariasan visszautasítja a választ anélkül, hogy hibát dobna.

## Prompt Tervezés - [Modul 02](../02-prompt-engineering/README.md)

**Chain-of-Thought (Gondolatlánc)** - Lépésenkénti érvelés a jobb pontosság érdekében.

**Constrained Output (Korlátozott kimenet)** - Speciális formátum vagy szerkezet előírása.

**High Eagerness (Magas lelkesedés)** - GPT-5.2 minta alapos érveléshez.

**Low Eagerness (Alacsony lelkesedés)** - GPT-5.2 minta gyors válaszokhoz.

**Multi-Turn Conversation (Többfordulós beszélgetés)** - Kontextus megtartása a beszélgetés során.

**Role-Based Prompting (Szerepalapú prompt)** - Modell személyiségének beállítása rendszerüzenetekkel.

**Self-Reflection (Önreflexió)** - A modell értékeli és javítja saját kimenetét.

**Structured Analysis (Strukturált elemzés)** - Rögzített értékelési keretrendszer.

**Task Execution Pattern (Feladatvégrehajtási minta)** - Tervezés → Végrehajtás → Összefoglalás.

## RAG (Keresésalapú Generálás) - [Modul 03](../03-rag/README.md)

**Document Processing Pipeline (Dokumentum feldolgozási folyamat)** - Betöltés → darabolás → beágyazás → tárolás.

**In-Memory Embedding Store (Memóriában tárolt beágyazás)** - Nem tartós tároló teszteléshez.

**RAG** - Keresést és generálást egyesítő megoldás a megalapozott válaszokért.

**Similarity Score (Hasonlósági pontszám)** - A szemantikai hasonlóság mértéke (0-1).

**Source Reference (Forrás hivatkozás)** - Lekért tartalom metaadatai.

## Agensek és Eszközök - [Modul 04](../04-tools/README.md)

**@Tool Annotation** - Java metódusokat jelöl meg AI-hívható eszközként.

**ReAct Pattern** - Érvelés → Cselekvés → Megfigyelés → Ismétlés.

**Session Management (Munkamenet-kezelés)** - Különálló kontextusok különböző felhasználók számára.

**Tool (Eszköz)** - Olyan függvény, amelyet az AI agent hívhat.

**Tool Description (Eszköz leírása)** - Az eszköz célja és paraméterei dokumentációja.

## Agentikus Modul - [Modul 05](../05-mcp/README.md)

**@Agent Annotation** - Interfészeket jelöl AI ügynökökként deklaratív viselkedés meghatározással.

**Agent Listener (Ügynök figyelő)** - Hook az ügynök végrehajtásának figyelésére `beforeAgentInvocation()` és `afterAgentInvocation()` használatával.

**Agentic Scope (Agentikus tartomány)** - Megosztott memória, ahová az ügynökök a kimeneteket tárolják `outputKey` használatával, hogy más ügynökök felhasználhassák.

**AgenticServices** - Ügynökök létrehozására szolgáló gyár `agentBuilder()` és `supervisorBuilder()` segítségével.

**Conditional Workflow (Feltételes munkafolyamat)** - Feltételek alapján vezet különböző szakértői ügynökökhöz.

**Human-in-the-Loop (Ember a folyamatban)** - Munkafolyamat minta, amely emberi jóváhagyást vagy tartalmi áttekintést ad.

**langchain4j-agentic** - Maven függőség deklaratív ügynöképítéshez (kísérleti).

**Loop Workflow (Ciklusban futó munkafolyamat)** - Ügynök végrehajtás ismétlése, amíg teljesül egy feltétel (pl. minőségi pontszám ≥ 0.8).

**outputKey** - Az ügynök annotációs paramétere, amely megadja, hová kerülnek a kimenetek az Agentic Scope-ban.

**Parallel Workflow (Párhuzamos munkafolyamat)** - Több ügynök egyidejű futtatása független feladatokra.

**Response Strategy (Válaszstratégia)** - Hogyan alakítja a supervisor a végső választ: UTOLSÓ, ÖSSZEFOGLALÓ vagy PONTSZÁMOZOTT.

**Sequential Workflow (Szekvenciális munkafolyamat)** - Ügynökök egymás utáni végrehajtása, ahol az eredmény áramlik a következő lépéshez.

**Supervisor Agent Pattern (Felügyelő ügynök minta)** - Fejlett agentikus minta, ahol egy felügyelő LLM dinamikusan dönt a rész-ügynökök meghívásáról.

## Modell Kontextus Protokoll (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven függőség az MCP integrációhoz LangChain4j-ben.

**MCP** - Modell Kontextus Protokoll: szabvány AI alkalmazások külső eszközökhöz való kapcsolódására. Egyszer kell megépíteni, bárhol használható.

**MCP Client** - Alkalmazás, amely kapcsolódik MCP szerverekhez és eszközöket fedez fel, illetve használ.

**MCP Server** - Szolgáltatás, amely eszközöket tesz elérhetővé MCP-n keresztül világos leírásokkal és paraméter sémákkal.

**McpToolProvider** - LangChain4j komponens, amely MCP eszközöket csomagol AI szolgáltatásokhoz és ügynökökhöz.

**McpTransport** - Interfész az MCP kommunikációhoz. Megvalósítások: Stdio és HTTP.

**Stdio Transport** - Helyi folyamat kommunikáció stdin/stdout-on keresztül. Hasznos fájlrendszerhez vagy parancssori eszközökhöz.

**StdioMcpTransport** - LangChain4j megvalósítás, amely MCP szervert indít alfolyamatként.

**Tool Discovery (Eszközfelfedezés)** - A kliens lekérdezi a szervert az elérhető eszközökről leírásokkal és sémákkal.

## Azure Szolgáltatások - [Modul 01](../01-introduction/README.md)

**Azure AI Search** - Felhő-alapú keresés vektoros képességekkel. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Azure erőforrások telepítése.

**Azure OpenAI** - A Microsoft vállalati AI szolgáltatása.

**Bicep** - Azure infrastruktúra-kód nyelv. [Infrastruktúra Útmutató](../01-introduction/infra/README.md)

**Deployment Name (Telepítés neve)** - Modell telepítésének neve Azure-ben.

**GPT-5.2** - Legújabb OpenAI modell érvelésvezérléssel. [Modul 02](../02-prompt-engineering/README.md)

## Tesztelés és Fejlesztés - [Tesztelési Útmutató](TESTING.md)

**Dev Container** - Konténeres fejlesztői környezet. [Konfiguráció](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Ingyenes AI modell játszótere. [Modul 00](../00-quick-start/README.md)

**In-Memory Testing (Memóriatesztelés)** - Tesztelés memóriában tárolt adatokkal.

**Integration Testing (Integrációs tesztelés)** - Tesztelés valós infrastruktúrával.

**Maven** - Java építési automatizáló eszköz.

**Mockito** - Java mocking keretrendszer.

**Spring Boot** - Java alkalmazás keretrendszer. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Jogi nyilatkozat**:
Ez a dokumentum az AI fordító szolgáltatás [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével készült. Bár igyekszünk a pontosságra, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum anyanyelvén tekintendő hivatalos forrásnak. Kritikus információk esetén professzionális emberi fordítást javaslunk. Nem vállalunk felelősséget az ebből az automatikus fordításból eredő félreértésekért vagy félreértelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->