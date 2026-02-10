# LangChain4j Slovník pojmů

## Obsah

- [Základní pojmy](../../../docs)
- [Komponenty LangChain4j](../../../docs)
- [Pojmy AI/ML](../../../docs)
- [Ochranná opatření](../../../docs)
- [Tvorba promptů](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agentové a nástroje](../../../docs)
- [Agentní modul](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Služby Azure](../../../docs)
- [Testování a vývoj](../../../docs)

Rychlá reference pojmů a konceptů používaných v kurzu.

## Základní pojmy

**AI Agent** - Systém, který používá AI k autonomnímu uvažování a jednání. [Modul 04](../04-tools/README.md)

**Řetězec (Chain)** - Sekvence operací, kde výstup slouží jako vstup do dalšího kroku.

**Dělení na části (Chunking)** - Rozdělování dokumentů na menší kusy. Typicky 300-500 tokenů s překryvem. [Modul 03](../03-rag/README.md)

**Kontextové okno** - Maximální počet tokenů, které model může zpracovat. GPT-5.2: 400 tisíc tokenů.

**Vkládání (Embeddings)** - Číselné vektory reprezentující význam textu. [Modul 03](../03-rag/README.md)

**Volání funkcí** - Model generuje strukturované požadavky na volání externích funkcí. [Modul 04](../04-tools/README.md)

**Halucinace (Hallucination)** - Když modely generují nesprávné, ale uvěřitelné informace.

**Prompt** - Textový vstup do jazykového modelu. [Modul 02](../02-prompt-engineering/README.md)

**Sémantické vyhledávání** - Vyhledávání podle významu pomocí embeddingů, nikoliv klíčových slov. [Modul 03](../03-rag/README.md)

**Stavový vs bezstavový (Stateful vs Stateless)** - Stateless: bez paměti. Stateful: uchovává historii konverzace. [Modul 01](../01-introduction/README.md)

**Tokeny** - Základní jednotky textu, které modely zpracovávají. Ovlivňují náklady a limity. [Modul 01](../01-introduction/README.md)

**Řetězení nástrojů (Tool Chaining)** - Sekvenční vykonávání nástrojů, kde výstup ovlivňuje další volání. [Modul 04](../04-tools/README.md)

## Komponenty LangChain4j

**AiServices** - Vytváří typu bezpečné AI servisní rozhraní.

**OpenAiOfficialChatModel** - Unifikovaný klient pro modely OpenAI a Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Vytváří embeddingy pomocí oficiálního klienta OpenAI (podporuje OpenAI i Azure OpenAI).

**ChatModel** - Základní rozhraní pro jazykové modely.

**ChatMemory** - Udržuje historii konverzace.

**ContentRetriever** - Najde relevantní části dokumentu pro RAG.

**DocumentSplitter** - Rozdělí dokumenty na části.

**EmbeddingModel** - Převádí text na číselné vektory.

**EmbeddingStore** - Ukládá a načítá embeddingy.

**MessageWindowChatMemory** - Udržuje posuvné okno nedávných zpráv.

**PromptTemplate** - Vytváří znovupoužitelné prompty s proměnnými `{{variable}}`.

**TextSegment** - Část textu s metadaty. Používá se v RAG.

**ToolExecutionRequest** - Reprezentuje požadavek na vykonání nástroje.

**UserMessage / AiMessage / SystemMessage** - Typy zpráv v konverzaci.

## Pojmy AI/ML

**Few-Shot Learning** - Poskytování příkladů v promptech. [Modul 02](../02-prompt-engineering/README.md)

**Velký jazykový model (LLM)** - AI modely trénované na rozsáhlých textových datech.

**Rozvaha (Reasoning Effort)** - Parametr GPT-5.2 ovlivňující hloubku uvažování. [Modul 02](../02-prompt-engineering/README.md)

**Teplota (Temperature)** - Řídí náhodnost výstupu. Nízká=deterministický, vysoká=tvůrčí.

**Vektorová databáze** - Specializovaná databáze pro embeddingy. [Modul 03](../03-rag/README.md)

**Zero-Shot Learning** - Provádění úloh bez příkladů. [Modul 02](../02-prompt-engineering/README.md)

## Ochranná opatření - [Modul 00](../00-quick-start/README.md)

**Obrana do hloubky (Defense in Depth)** - Vícevrstvý bezpečnostní přístup kombinující aplikační guardrails s filtry poskytovatele.

**Hard Block** - Poskytovatel vrací HTTP 400 chybu při závažných porušeních obsahu.

**InputGuardrail** - Rozhraní LangChain4j pro validaci vstupu uživatele před jeho předáním LLM. Šetří náklady a latenci blokováním škodlivých promptů již na začátku.

**InputGuardrailResult** - Návratový typ validace guardrail: `success()` nebo `fatal("důvod")`.

**OutputGuardrail** - Rozhraní pro validaci odpovědí AI před jejich zasláním uživatelům.

**Filtry bezpečnosti poskytovatele** - Vložené filtry obsahu od AI poskytovatelů (např. GitHub Models), které detekují porušení na úrovni API.

**Měkký odmítnutí (Soft Refusal)** - Model zdvořile odmítne odpovědět bez vyhození chyby.

## Tvorba promptů - [Modul 02](../02-prompt-engineering/README.md)

**Řetězec myšlení (Chain-of-Thought)** - Krok za krokem uvažování pro lepší přesnost.

**Omezený výstup (Constrained Output)** - Vynucení specifického formátu nebo struktury.

**Vysoká ochota (High Eagerness)** - Vzor GPT-5.2 pro důkladné uvažování.

**Nízká ochota (Low Eagerness)** - Vzor GPT-5.2 pro rychlé odpovědi.

**Vícekolová konverzace (Multi-Turn Conversation)** - Udržování kontextu přes výměny.

**Role-based prompting** - Nastavení persony modelu přes systémové zprávy.

**Sebereflexe (Self-Reflection)** - Model hodnotí a zlepšuje svůj výstup.

**Strukturovaná analýza (Structured Analysis)** - Pevný vyhodnocovací rámec.

**Vzor vykonávání úkolu (Task Execution Pattern)** - Plánuj → Proveď → Shrni.

## RAG (Retrieval-Augmented Generation) - [Modul 03](../03-rag/README.md)

**Proces zpracování dokumentu** - Načti → rozděl → vlož → ulož.

**In-memory embedding store** - Nepersistentní úložiště pro testování.

**RAG** - Kombinuje vyhledávání s generováním pro podložené odpovědi.

**Podobnostní skóre (Similarity Score)** - Míra (0-1) sémantické podobnosti.

**Zdrojová reference (Source Reference)** - Metadata o nalezeném obsahu.

## Agentové a nástroje - [Modul 04](../04-tools/README.md)

**@Tool Anotace** - Označuje metody v Javě jako AI-volatelné nástroje.

**ReAct Vzor** - Uvažuj → Jednej → Pozoruj → Opakuj.

**Správa relací** - Oddělené kontexty pro různé uživatele.

**Nástroj (Tool)** - Funkce, kterou může AI agent zavolat.

**Popis nástroje** - Dokumentace účelu a parametrů nástroje.

## Agentní modul - [Modul 05](../05-mcp/README.md)

**@Agent Anotace** - Označuje rozhraní jako AI agenty s deklarativní definicí chování.

**Agent Listener** - Háček pro sledování vykonání agenta přes `beforeAgentInvocation()` a `afterAgentInvocation()`.

**Agentic Scope** - Sdílená paměť, kde agenti ukládají výstupy pomocí `outputKey` pro další agenty.

**AgenticServices** - Továrna na vytváření agentů pomocí `agentBuilder()` a `supervisorBuilder()`.

**Podmíněný workflow** - Směrování na různé specialisty podle podmínek.

**Člověk v procesu (Human-in-the-Loop)** - Vzorec workflow přidávající kontroly člověka pro schválení nebo revizi obsahu.

**langchain4j-agentic** - Maven závislost pro deklarativní tvorbu agentů (experimentální).

**Loop Workflow** - Opakované vykonávání agenta dokud není splněna podmínka (např. skóre kvality ≥ 0,8).

**outputKey** - Parametr anotace agenta udávající, kde jsou výsledky ukládány v Agentic Scope.

**Paralelní workflow** - Současné spuštění více agentů pro nezávislé úkoly.

**Strategie odpovědi (Response Strategy)** - Jak supervizor formuluje konečnou odpověď: LAST, SUMMARY, nebo SCORED.

**Sekvenční workflow** - Vykonávání agentů v pořadí, kde výstup přechází do dalšího kroku.

**Supervizní agentní vzor (Supervisor Agent Pattern)** - Pokročilý agentní vzor, kde supervizní LLM dynamicky rozhoduje, které pod-agenty vyvolat.

## Model Context Protocol (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven závislost pro integraci MCP v LangChain4j.

**MCP** - Model Context Protocol: standard pro připojení AI aplikací k externím nástrojům. Napiš jednou, používej všude.

**MCP Client** - Aplikace připojující se k MCP serverům za účelem objevování a používání nástrojů.

**MCP Server** - Služba zpřístupňující nástroje přes MCP s jasnými popisy a schématy parametrů.

**McpToolProvider** - Komponenta LangChain4j, která zabalí MCP nástroje pro použití v AI službách a agentech.

**McpTransport** - Rozhraní pro komunikaci MCP. Implementace zahrnují Stdio a HTTP.

**Stdio Transport** - Lokální transport procesu přes stdin/stdout. Užitečný pro přístup k souborovému systému nebo příkazovým nástrojům.

**StdioMcpTransport** - Implementace LangChain4j spouštějící MCP server jako podproces.

**Objevování nástrojů (Tool Discovery)** - Klient dotazuje server na dostupné nástroje s popisy a schématy.

## Služby Azure - [Modul 01](../01-introduction/README.md)

**Azure AI Search** - Cloudové vyhledávání s vektorovými schopnostmi. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Nasazuje Azure zdroje.

**Azure OpenAI** - Podniková AI služba Microsoftu.

**Bicep** - Jazyk pro infrastrukturu jako kód v Azure. [Průvodce infrastrukturou](../01-introduction/infra/README.md)

**Název nasazení** - Název pro nasazení modelu v Azure.

**GPT-5.2** - Nejnovější model OpenAI s řízením uvažování. [Modul 02](../02-prompt-engineering/README.md)

## Testování a vývoj - [Testing Guide](TESTING.md)

**Dev Container** - Kontejnery prostředí pro vývoj. [Konfigurace](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Bezplatná AI modelová aréna. [Modul 00](../00-quick-start/README.md)

**Testování v paměti (In-Memory Testing)** - Testování s úložištěm v paměti.

**Integrační testování** - Testování s reálnou infrastrukturou.

**Maven** - Nástroj pro automatizaci sestavení Java.

**Mockito** - Framework pro mocking v Javě.

**Spring Boot** - Java aplikační framework. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože se snažíme o přesnost, berte prosím na vědomí, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho mateřském jazyce by měl být považován za autoritativní zdroj. Pro důležité informace doporučujeme využít profesionální lidský překlad. Nejsme odpovědní za jakákoli nedorozumění nebo mylné interpretace vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->