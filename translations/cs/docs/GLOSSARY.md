# LangChain4j Slovník pojmů

## Obsah

- [Jádrové pojmy](../../../docs)
- [Komponenty LangChain4j](../../../docs)
- [AI/ML pojmy](../../../docs)
- [Ochranné mechanismy](../../../docs)
- [Řízení příkazů (prompt engineering)](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agenti a nástroje](../../../docs)
- [Agentický modul](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Azure služby](../../../docs)
- [Testování a vývoj](../../../docs)

Rychlá reference pojmů a konceptů používaných v celém kurzu.

## Jádrové pojmy

**AI Agent** - Systém používající AI k autonomnímu uvažování a jednání. [Modul 04](../04-tools/README.md)

**Řetězec (Chain)** - Sekvence operací, kde výstup je vstupem do dalšího kroku.

**Chunking** - Rozdělování dokumentů na menší části. Typicky 300-500 tokenů s překrytím. [Modul 03](../03-rag/README.md)

**Context Window** - Maximální počet tokenů, které model dokáže zpracovat. GPT-5.2: 400K tokenů (až 272K vstup, 128K výstup).

**Embeddings** - Číselné vektory představující význam textu. [Modul 03](../03-rag/README.md)

**Volání funkce (Function Calling)** - Model generuje strukturované požadavky k vyvolání externích funkcí. [Modul 04](../04-tools/README.md)

**Halucinace** - Když modely generují nesprávné, ale věrohodné informace.

**Prompt** - Textový vstup do jazykového modelu. [Modul 02](../02-prompt-engineering/README.md)

**Sémantické vyhledávání** - Vyhledávání podle významu pomocí embeddings, nikoli klíčových slov. [Modul 03](../03-rag/README.md)

**Stavový vs Bezustavový (Stateful vs Stateless)** - Bezustavový: bez paměti. Stavový: uchovává historii konverzace. [Modul 01](../01-introduction/README.md)

**Tokeny** - Základní textové jednotky zpracovávané modely. Ovlivňují náklady a limity. [Modul 01](../01-introduction/README.md)

**Řetězení nástrojů (Tool Chaining)** - Sekvenční spouštění nástrojů, kde výstup informuje další volání. [Modul 04](../04-tools/README.md)

## Komponenty LangChain4j

**AiServices** - Vytváří typově bezpečné rozhraní AI služeb.

**OpenAiOfficialChatModel** - Jednotný klient pro modely OpenAI a Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Vytváří embeddings pomocí klienta OpenAI Official (podporuje OpenAI i Azure OpenAI).

**ChatModel** - Jádrové rozhraní pro jazykové modely.

**ChatMemory** - Uchovává historii konverzace.

**ContentRetriever** - Vyhledává relevantní části dokumentů pro RAG.

**DocumentSplitter** - Rozděluje dokumenty na kousky.

**EmbeddingModel** - Převádí text na číselné vektory.

**EmbeddingStore** - Ukládá a načítá embeddings.

**MessageWindowChatMemory** - Uchovává posuvné okno nedávných zpráv.

**PromptTemplate** - Vytváří znovupoužitelné prompty s `{{variable}}` zástupci.

**TextSegment** - Textový segment s metadaty. Používá se v RAG.

**ToolExecutionRequest** - Reprezentuje požadavek na spuštění nástroje.

**UserMessage / AiMessage / SystemMessage** - Typy zpráv v konverzaci.

## AI/ML pojmy

**Učení s několika příklady (Few-Shot Learning)** - Zadávání příkladů v promptech. [Modul 02](../02-prompt-engineering/README.md)

**Velký jazykový model (LLM)** - AI modely trénované na obrovských datových sadách textu.

**Úroveň úvahy (Reasoning Effort)** - Parametr GPT-5.2 ovládající hloubku uvažování. [Modul 02](../02-prompt-engineering/README.md)

**Teplota (Temperature)** - Řídí náhodnost výstupu. Nízká=deterministické, vysoká=tvůrčí.

**Vektorová databáze** - Specializovaná databáze pro embeddings. [Modul 03](../03-rag/README.md)

**Učení bez příkladů (Zero-Shot Learning)** - Provádění úkolů bez příkladů. [Modul 02](../02-prompt-engineering/README.md)

## Ochranné mechanismy - [Modul 00](../00-quick-start/README.md)

**Defense in Depth** - Vícevrstvá bezpečnostní metoda kombinující aplikační ochranné mechanismy s filtry poskytovatelů.

**Hard Block** - Poskytovatel vrací HTTP 400 chybu při závažném porušení obsahu.

**InputGuardrail** - Rozhraní LangChain4j pro ověřování vstupů uživatele před dosažením LLM. Šetří náklady a zpoždění tím, že brání škodlivým promptům včas.

**InputGuardrailResult** - Návratový typ pro ověření vstupního guardrailu: `success()` nebo `fatal("důvod")`.

**OutputGuardrail** - Rozhraní pro ověřování AI odpovědí před jejich vrácením uživatelům.

**Provider Safety Filters** - Vestavěné obsahové filtry AI poskytovatelů (např. GitHub Models), které zachytí porušení na úrovni API.

**Soft Refusal** - Model zdvořile odmítne odpovědět bez vyvolání chyby.

## Řízení příkazů (prompt engineering) - [Modul 02](../02-prompt-engineering/README.md)

**Řetězení myšlenek (Chain-of-Thought)** - Krok za krokem uvažování pro lepší přesnost.

**Omezený výstup (Constrained Output)** - Vynucení specifického formátu nebo struktury.

**Vysoká snaha (High Eagerness)** - Vzor GPT-5.2 pro důkladné uvažování.

**Nízká snaha (Low Eagerness)** - Vzor GPT-5.2 pro rychlé odpovědi.

**Vícekolová konverzace (Multi-Turn Conversation)** - Uchovávání kontextu napříč výměnami.

**Role-Based Prompting** - Nastavení persony modelu pomocí systémových zpráv.

**Sebe-reflexe (Self-Reflection)** - Model posuzuje a zlepšuje svůj výstup.

**Strukturovaná analýza (Structured Analysis)** - Pevný hodnotící rámec.

**Vzor vykonávání úkolu (Task Execution Pattern)** - Plánuj → Vykonej → Shrň.

## RAG (Retrieval-Augmented Generation) - [Modul 03](../03-rag/README.md)

**Pipeline zpracování dokumentů** - Načíst → rozdělit → vložit do embedů → uložit.

**In-memory embedding store** - Nepersistentní úložiště pro testování.

**RAG** - Kombinuje vyhledávání s generováním pro zakotvení odpovědí.

**Skóre podobnosti (Similarity Score)** - Míra (0-1) sémantické podobnosti.

**Zdrojová reference (Source Reference)** - Metadata o vyhledaném obsahu.

## Agenti a nástroje - [Modul 04](../04-tools/README.md)

**@Tool anotace** - Označuje metody v Javě jako nástroje volatelné AI.

**ReAct pattern** - Uvažuj → jednej → pozoruj → opakuj.

**Správa relací (Session Management)** - Oddělené kontexty pro různé uživatele.

**Nástroj (Tool)** - Funkce, kterou může AI agent volat.

**Popis nástroje (Tool Description)** - Dokumentace účelu a parametrů nástroje.

## Agentický modul - [Modul 05](../05-mcp/README.md)

**@Agent anotace** - Označuje rozhraní jako AI agenty s deklarativní definicí chování.

**Agent Listener** - Háček pro sledování běhu agentů přes `beforeAgentInvocation()` a `afterAgentInvocation()`.

**Agentický rozsah (Agentic Scope)** - Sdílená paměť, kam agenti ukládají výstupy s klíčem `outputKey` pro konzumaci dalšími agenty.

**AgenticServices** - Továrna pro tvorbu agentů pomocí `agentBuilder()` a `supervisorBuilder()`.

**Podmíněný workflow (Conditional Workflow)** - Směrování na různé specialisty podle podmínek.

**Human-in-the-Loop** - Vzorec workflow přidávající lidské kontrolní body pro schválení nebo revizi obsahu.

**langchain4j-agentic** - Maven závislost pro deklarativní tvorbu agentů (experimentální).

**Smyčkový workflow (Loop Workflow)** - Iterace opakovaného běhu agenta do splnění podmínky (např. skóre kvality ≥ 0.8).

**outputKey** - Parametr anotace agenta určující, kam se ukládají výsledky v Agentic Scope.

**Paralelní workflow (Parallel Workflow)** - Současný běh více agentů na nezávislých úkolech.

**Strategie odpovědí (Response Strategy)** - Jak dozor formulujefinální odpověď: POSLEDNÍ, SOUHRN nebo OHODNOCENÁ.

**Sekvenční workflow (Sequential Workflow)** - Postupné spouštění agentů, kde výstup plyne do dalšího kroku.

**Vzorec dozorčího agenta (Supervisor Agent Pattern)** - Pokročilý agentický vzorec, kde dozorčí LLM dynamicky rozhoduje o volání podagentů.

## Model Context Protocol (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven závislost pro integraci MCP v LangChain4j.

**MCP** - Model Context Protocol: standard pro propojení AI aplikací s externími nástroji. Postav jednou, použij všude.

**MCP Client** - Aplikace, která se připojuje k MCP serverům pro objevování a používání nástrojů.

**MCP Server** - Služba vystavující nástroje přes MCP s popisy a schématy parametrů.

**McpToolProvider** - Komponenta LangChain4j, která obaluje MCP nástroje pro použití v AI službách a agentech.

**McpTransport** - Rozhraní pro MCP komunikaci. Implementace zahrnují Stdio a HTTP.

**Stdio Transport** - Lokální procesní transport přes stdin/stdout. Vhodné pro přístup k souborům nebo příkazové nástroje.

**StdioMcpTransport** - Implementace LangChain4j, která spouští MCP server jako podproces.

**Objevování nástrojů (Tool Discovery)** - Klient dotazuje server na dostupné nástroje s popisy a schématy.

## Azure služby - [Modul 01](../01-introduction/README.md)

**Azure AI Search** - Cloudové vyhledávání s vektorovou podporou. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Nástroj pro nasazení Azure zdrojů.

**Azure OpenAI** - Podniková AI služba Microsoftu.

**Bicep** - Jazyk pro infrastrukturu jako kód v Azure. [Příručka infrastruktury](../01-introduction/infra/README.md)

**Název nasazení** - Název nasazení modelu v Azure.

**GPT-5.2** - Nejnovější model OpenAI s řízením uvažování. [Modul 02](../02-prompt-engineering/README.md)

## Testování a vývoj - [Testovací příručka](TESTING.md)

**Dev Container** - Kontejnerizované vývojové prostředí. [Konfigurace](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Volné AI modelové hřiště. [Modul 00](../00-quick-start/README.md)

**Testování v paměti (In-Memory Testing)** - Testování s úložištěm v paměti.

**Integrační testování (Integration Testing)** - Testování s reálnou infrastrukturou.

**Maven** - Nástroj pro automatizaci sestavení v Javě.

**Mockito** - Java framework pro mockování.

**Spring Boot** - Java aplikační rámec. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). I když usilujeme o přesnost, mějte prosím na paměti, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho mateřském jazyce by měl být považován za autoritativní zdroj. Pro kritické informace se doporučuje profesionální lidský překlad. Nejsme odpovědní za žádná nedorozumění nebo nesprávné výklady vzniklé z užití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->