# LangChain4j Szójegyzék

## Tartalomjegyzék

- [Alapfogalmak](../../../docs)
- [LangChain4j komponensek](../../../docs)
- [AI/ML fogalmak](../../../docs)
- [Guardrails](../../../docs)
- [Prompt mérnökség](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Ügynökök és eszközök](../../../docs)
- [Agentikus modul](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Azure szolgáltatások](../../../docs)
- [Tesztelés és fejlesztés](../../../docs)

Gyors hivatkozás a kurzus során használt kifejezésekhez és fogalmakhoz.

## Alapfogalmak

**AI ügynök** - Olyan rendszer, amely AI-t használ önálló gondolkodásra és cselekvésre. [Module 04](../04-tools/README.md)

**Lánc** - Műveletsorozat, ahol az eredmény a következő lépés bemenete.

**Darabolás** - Dokumentumok kisebb részekre bontása. Tipikus: 300-500 token átfedéssel. [Module 03](../03-rag/README.md)

**Kontextus ablak** - Maximális token szám, amelyet egy modell feldolgozni tud. GPT-5.2: 400K token (legfeljebb 272K bemenet, 128K kimenet).

**Beágyazások** - Numerikus vektorok, amelyek a szöveg jelentését reprezentálják. [Module 03](../03-rag/README.md)

**Funkcióhívás** - A modell strukturált kéréseket generál külső funkciók meghívására. [Module 04](../04-tools/README.md)

**Hallucináció** - Amikor a modellek helytelen, de hihető információt generálnak.

**Prompt** - Szöveges bemenet a nyelvi modellnek. [Module 02](../02-prompt-engineering/README.md)

**Szemantikus keresés** - Keresés a jelentés alapján, beágyazásokat használva, nem kulcsszavakat. [Module 03](../03-rag/README.md)

**Állapotmentes vs állapotőr** - Állapotmentes: nincs memóriája. Állapotőr: megőrzi a beszélgetés előzményeit. [Module 01](../01-introduction/README.md)

**Tokenek** - Alapvető szövegegységek, amelyeket a modellek feldolgoznak. Hatással vannak a költségekre és korlátokra. [Module 01](../01-introduction/README.md)

**Eszköz láncolás** - Eszközök egymás utáni végrehajtása, ahol az eredmény befolyásolja a következő hívást. [Module 04](../04-tools/README.md)

## LangChain4j komponensek

**AiServices** - Típusbiztos AI szolgáltatás interfészek létrehozása.

**OpenAiOfficialChatModel** - Egyesített kliens az OpenAI és Azure OpenAI modellekhez.

**OpenAiOfficialEmbeddingModel** - Beágyazások létrehozása az OpenAI hivatalos kliensével (támogatja mindkettőt: OpenAI és Azure OpenAI).

**ChatModel** - Nyelvi modellek fő interfésze.

**ChatMemory** - Megőrzi a beszélgetés előzményeit.

**ContentRetriever** - Megtalálja a releváns dokumentumdarabokat RAG-hez.

**DocumentSplitter** - Dokumentumokat tördel darabokra.

**EmbeddingModel** - Szöveget numerikus vektorokká alakít.

**EmbeddingStore** - Beágyazások tárolása és lekérése.

**MessageWindowChatMemory** - Az utolsó üzenetek mozgóablakát kezeli.

**PromptTemplate** - Újrafelhasználható promtok létrehozása `{{variable}}` helyőrzőkkel.

**TextSegment** - Szöveg darab metaadatokkal. RAG-ben használatos.

**ToolExecutionRequest** - Az eszköz végrehajtási kérésének reprezentációja.

**UserMessage / AiMessage / SystemMessage** - Beszélgetési üzenettípusok.

## AI/ML fogalmak

**Few-Shot Learning** - Példák biztosítása promptokban. [Module 02](../02-prompt-engineering/README.md)

**Nagy nyelvi modell (LLM)** - Nagy mennyiségű szöveg alapján tanított AI modellek.

**Gondolkodási erőfeszítés** - GPT-5.2 paraméter, ami befolyásolja a gondolkodás mélységét. [Module 02](../02-prompt-engineering/README.md)

**Hőmérséklet** - A kimenet véletlenszerűségét szabályozza. Alacsony=determinista, magas=kreatív.

**Vektoradatbázis** - Beágyazásokra specializált adatbázis. [Module 03](../03-rag/README.md)

**Zero-Shot Learning** - Feladat végrehajtása példa nélkül. [Module 02](../02-prompt-engineering/README.md)

## Guardrails - [Module 00](../00-quick-start/README.md)

**Mélységi védelem** - Többrétegű biztonsági megközelítés, amely az alkalmazásszintű guardrail-eket kombinálja a szolgáltató biztonsági szűrőivel.

**Kemény blokkolás** - A szolgáltató HTTP 400 hibát dob súlyos tartalmi szabálysértés esetén.

**InputGuardrail** - LangChain4j interfész a felhasználói bemenet ellenőrzésére, mielőtt az LLM-hez jutna. Költséget és késleltetést spórol, mert korán blokkolja a káros promptokat.

**InputGuardrailResult** - Guardrail ellenőrzés visszatérési típusa: `success()` vagy `fatal("ok")`.

**OutputGuardrail** - Interfész AI válaszok ellenőrzésére, mielőtt visszajutnak a felhasználókhoz.

**Szolgáltató biztonsági szűrők** - AI szolgáltatók beépített tartalomszűrői (pl. GitHub modellek), amelyek API szinten észlelik a szabálysértéseket.

**Lágy elutasítás** - A modell udvariasan megtagadja a választ anélkül, hogy hibát dobna.

## Prompt mérnökség - [Module 02](../02-prompt-engineering/README.md)

**Gondolatmenet-lánc** - Lépésenkénti érvelés a jobb pontosságért.

**Korlátozott kimenet** - Meghatározott formátum vagy szerkezet kikényszerítése.

**Magas igyekezet** - GPT-5.2 minta a részletes érveléshez.

**Alacsony igyekezet** - GPT-5.2 minta a gyors válaszokhoz.

**Többkörös beszélgetés** - Kontextus fenntartása az üzenetváltások között.

**Szerepalapú promptolás** - Modell személyiség beállítása rendszerüzenetekkel.

**Önreflexió** - A modell értékeli és fejleszti a saját kimenetét.

**Strukturált elemzés** - Rögzített értékelési keretrendszer.

**Feladatvégrehajtási minta** - Tervezés → Végrehajtás → Összefoglalás.

## RAG (Retrieval-Augmented Generation) - [Module 03](../03-rag/README.md)

**Dokumentumfeldolgozó csővezeték** - Betöltés → darabolás → beágyazás → tárolás.

**Memóriabeli beágyazás tároló** - Nem tartós tároló teszteléshez.

**RAG** - Kombinálja a keresést és generálást a válaszok megalapozásához.

**Hasonlósági pontszám** - (0-1) számszerűsíti a szemantikus hasonlóságot.

**Forrás referencia** - Lekért tartalom metaadatai.

## Ügynökök és eszközök - [Module 04](../04-tools/README.md)

**@Tool annotáció** - Java metódusokat jelöl AI által hívható eszközökként.

**ReAct minta** - Érvelés → cselekvés → megfigyelés → ismétlés.

**Munkamenet kezelés** - Különböző felhasználók külön kontextusai.

**Eszköz** - Funkció, amit AI ügynök meghívhat.

**Eszköz leírás** - Dokumentáció az eszköz céljáról és paramétereiről.

## Agentikus modul - [Module 05](../05-mcp/README.md)

**@Agent annotáció** - Interfészeket jelöl AI ügynökökként deklaratív viselkedésdefinícióval.

**Agent Listener** - Hook az ügynök végrehajtásának megfigyelésére `beforeAgentInvocation()` és `afterAgentInvocation()` segítségével.

**Agentikus hatókör** - Megosztott memória, ahol az ügynökök az `outputKey` segítségével tárolják az eredményeket a következő ügynökök számára.

**AgenticServices** - Ügynökök létrehozó gyára `agentBuilder()` és `supervisorBuilder()` használatával.

**Feltételes munkafolyamat** - Feltételek alapján irányított út különböző szakértő ügynökökhöz.

**Ember a hurokban** - Munkafolyamat minta, amely emberi ellenőrzőpontokat ad jóváhagyás vagy tartalmi ellenőrzés céljából.

**langchain4j-agentic** - Maven függőség deklaratív ügynöképítéshez (kísérleti).

**Ciklus munkafolyamat** - Ügynök végrehajtás ismétlése, amíg a feltétel teljesül (pl. minőségi pontszám ≥ 0.8).

**outputKey** - Ügynök annotáció paraméter, mely megadja, hol tárolódnak az eredmények az Agentikus hatókörben.

**Párhuzamos munkafolyamat** - Több ügynök párhuzamos futtatása független feladatokra.

**Válaszstratégia** - Hogyan fogalmazza meg a felügyelő a végső választ: LEGUTOLSÓ, ÖSSZEFOGLALÓ vagy ÉRTÉKELT.

**Szekvenciális munkafolyamat** - Ügynökök sorrendben végrehajtva, ahol az eredmény továbbáramlik a következő lépéshez.

**Felügyelő ügynök minta** - Fejlett ügynökös minta, ahol a felügyelő LLM dinamikusan dönti el, mely al-ügynököket hívja meg.

## Model Context Protocol (MCP) - [Module 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven függőség MCP integrációhoz LangChain4j-ben.

**MCP** - Model Context Protocol: szabvány AI alkalmazások csatlakoztatására külső eszközökhöz. Egyszer építsd fel, bárhol használd.

**MCP kliens** - Alkalmazás, amely MCP szerverekhez csatlakozik, hogy eszközöket fedezzen fel és használjon.

**MCP szerver** - Szolgáltatás, amely MCP-n keresztül eszközöket tesz elérhetővé egyértelmű leírásokkal és paraméter sémákkal.

**McpToolProvider** - LangChain4j komponens, amely becsomagolja az MCP eszközöket AI szolgáltatások és ügynökök használatára.

**McpTransport** - MCP kommunikációs interfész. Megvalósítások között van Stdio és HTTP.

**Stdio transport** - Helyi folyamat szállítás stdin/stdout-on keresztül. Hasznos fájlrendszer eléréséhez vagy parancssori eszközökhöz.

**StdioMcpTransport** - LangChain4j megvalósítás, amely MCP szervert indít alfolyamatként.

**Eszköz felfedezés** - A kliens lekérdezi a szervert az elérhető eszközök leírásaival és sémáival.

## Azure szolgáltatások - [Module 01](../01-introduction/README.md)

**Azure AI Search** - Felhő alapú keresés vektor támogatással. [Module 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Azure erőforrások telepítése.

**Azure OpenAI** - Microsoft vállalati AI szolgáltatása.

**Bicep** - Azure infrastruktúra-kód nyelv. [Infrastrukturális útmutató](../01-introduction/infra/README.md)

**Telepítés neve** - Modell telepítésének neve Azure-ban.

**GPT-5.2** - Legújabb OpenAI modell érvelési vezérléssel. [Module 02](../02-prompt-engineering/README.md)

## Tesztelés és fejlesztés - [Tesztelési útmutató](TESTING.md)

**Dev Container** - Konténerizált fejlesztői környezet. [Konfiguráció](../../../.devcontainer/devcontainer.json)

**GitHub modellek** - Ingyenes AI modell játszótér. [Module 00](../00-quick-start/README.md)

**Memóriabeli tesztelés** - Tesztelés memóriabeli tárolóval.

**Integrációs tesztelés** - Tesztelés valódi infrastruktúrával.

**Maven** - Java build automatizációs eszköz.

**Mockito** - Java mocking könyvtár.

**Spring Boot** - Java alkalmazáskeretrendszer. [Module 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Jogi nyilatkozat**:
Ez a dokumentum az [Co-op Translator](https://github.com/Azure/co-op-translator) mesterséges intelligencia alapú fordító szolgáltatásával készült. Bár az pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az anyanyelvén tekintendő hiteles forrásnak. Kritikus információk esetén professzionális emberi fordítást javaslunk. Nem vállalunk felelősséget az e fordítás használatából eredő félreértésekért vagy félreértelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->