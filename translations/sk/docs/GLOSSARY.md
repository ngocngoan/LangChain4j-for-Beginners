# LangChain4j Slovník pojmov

## Obsah

- [Základné pojmy](../../../docs)
- [Komponenty LangChain4j](../../../docs)
- [Pojmy z AI/ML](../../../docs)
- [Bezpečnostné opatrenia](../../../docs)
- [Návrh promptov](../../../docs)
- [RAG (Generovanie s doplnením z vyhľadávania)](../../../docs)
- [Agent a nástroje](../../../docs)
- [Agentický modul](../../../docs)
- [Protokol Kontextu Modelu (MCP)](../../../docs)
- [Azure služby](../../../docs)
- [Testovanie a vývoj](../../../docs)

Rýchla referencia pojmov a konceptov použitých v priebehu kurzu.

## Základné pojmy

**AI agent** - Systém, ktorý využíva AI na uvažovanie a autonómne konanie. [Modul 04](../04-tools/README.md)

**Reťazec** - Sekvencia operácií, kde výstup je vstupom do ďalšieho kroku.

**Delenie na časti** - Rozklad dokumentov na menšie kúsky. Typicky 300-500 tokenov s prekrytím. [Modul 03](../03-rag/README.md)

**Okno kontextu** - Maximálny počet tokenov, ktoré môže model spracovať. GPT-5.2: 400 tisíc tokenov.

**Vloženia (embeddings)** - Číselné vektory reprezentujúce význam textu. [Modul 03](../03-rag/README.md)

**Volanie funkcie** - Model generuje štruktúrované požiadavky na volanie externých funkcií. [Modul 04](../04-tools/README.md)

**Halucinácia** - Keď modely generujú nesprávne, ale pravdepodobné informácie.

**Prompt** - Textový vstup do jazykového modelu. [Modul 02](../02-prompt-engineering/README.md)

**Sémantické vyhľadávanie** - Vyhľadávanie podľa významu pomocou vložení, nie kľúčových slov. [Modul 03](../03-rag/README.md)

**Stavový vs Nestavový** - Nestavový: bez pamäte. Stavový: uchováva históriu konverzácie. [Modul 01](../01-introduction/README.md)

**Tokeny** - Základné textové jednotky spracovávané modelmi. Ovplyvňujú náklady a limity. [Modul 01](../01-introduction/README.md)

**Reťazenie nástrojov** - Sekvenčné vykonávanie nástrojov, kde výstup ovplyvňuje ďalšie volanie. [Modul 04](../04-tools/README.md)

## Komponenty LangChain4j

**AiServices** - Vytvára typovo bezpečné rozhrania AI služieb.

**OpenAiOfficialChatModel** - Jednotný klient pre OpenAI a Azure OpenAI modely.

**OpenAiOfficialEmbeddingModel** - Vytvára embeddings pomocou oficiálneho OpenAI klienta (podporuje OpenAI aj Azure OpenAI).

**ChatModel** - Jadro rozhrania pre jazykové modely.

**ChatMemory** - Uchováva históriu konverzácie.

**ContentRetriever** - Hľadá relevantné časti dokumentov pre RAG.

**DocumentSplitter** - Rozdeľuje dokumenty na časti.

**EmbeddingModel** - Konvertuje text na číselné vektory.

**EmbeddingStore** - Ukladá a načítava embeddings.

**MessageWindowChatMemory** - Uchováva posuvné okno nedávnych správ.

**PromptTemplate** - Vytvára znovupoužiteľné prompty s `{{variable}}` zástupcami.

**TextSegment** - Textová časť s metadátami. Používa sa v RAG.

**ToolExecutionRequest** - Reprezentuje požiadavku na vykonanie nástroja.

**UserMessage / AiMessage / SystemMessage** - Typy správ v konverzácii.

## Pojmy z AI/ML

**Few-Shot Learning** - Poskytovanie príkladov v promptoch. [Modul 02](../02-prompt-engineering/README.md)

**Veľký jazykový model (LLM)** - AI modely trénované na obrovskom množstve textu.

**Úsilie pri uvažovaní** - Parameter GPT-5.2 riadiaci hĺbku myslenia. [Modul 02](../02-prompt-engineering/README.md)

**Teplota** - Riadi náhodnosť výstupu. Nízka=deterministický, vysoká=kreatívny.

**Vektorová databáza** - Špecializovaná databáza pre embeddings. [Modul 03](../03-rag/README.md)

**Zero-Shot Learning** - Vykonávanie úloh bez príkladov. [Modul 02](../02-prompt-engineering/README.md)

## Bezpečnostné opatrenia - [Modul 00](../00-quick-start/README.md)

**Obrana do hĺbky** - Viacvrstvový bezpečnostný prístup kombinujúci aplikačné ochranné opatrenia s bezpečnostnými filtrami poskytovateľov.

**Tvrdý blok** - Poskytovateľ vráti HTTP 400 chybu pri vážnom porušení pravidiel obsahu.

**InputGuardrail** - Rozhranie LangChain4j na validáciu vstupu používateľa pred vstupom do LLM. Šetrí náklady a latenciu blokovaním škodlivých promptov včas.

**InputGuardrailResult** - Typ návratu overenia guardrail: `success()` alebo `fatal("dôvod")`.

**OutputGuardrail** - Rozhranie na validáciu odpovedí AI pred ich vrátením používateľom.

**Bezpečnostné filtre poskytovateľa** - Vstavané filtre obsahu od AI poskytovateľov (napr. GitHub Models) zachytávajú porušenia už na úrovni API.

**Mäkký odmietnutie** - Model slušne odmietne odpovedať bez vygenerovania chyby.

## Návrh promptov - [Modul 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Krok za krokom uvažovanie pre lepšiu presnosť.

**Obmedzený výstup** - Vynucovanie špecifického formátu alebo štruktúry.

**Vysoká horlivosť** - Vzor GPT-5.2 pre dôkladné uvažovanie.

**Nízka horlivosť** - Vzor GPT-5.2 pre rýchle odpovede.

**Viacotáčková konverzácia** - Udržiavanie kontextu cez výmeny správ.

**Promptovanie na základe rolí** - Nastavenie persony modelu cez systémové správy.

**Sebareflexia** - Model hodnotí a zlepšuje svoj výstup.

**Štruktúrovaná analýza** - Fixný hodnotiaci rámec.

**Vzor vykonávania úloh** - Plán → Vykonaj → Zhrň.

## RAG (Generovanie s doplnením z vyhľadávania) - [Modul 03](../03-rag/README.md)

**Pipeline spracovania dokumentov** - Načítať → rozdeliť → vložiť → uložiť.

**In-memory úložisko embeddings** - Nepretržité úložisko pre testovanie.

**RAG** - Kombinuje vyhľadávanie s generovaním na zakotvenie odpovedí.

**Miera podobnosti** - Miera (0-1) sémantickej podobnosti.

**Referenčný zdroj** - Metadáta o nájdenom obsahu.

## Agent a nástroje - [Modul 04](../04-tools/README.md)

**@Tool anotácia** - Označuje Java metódy ako nástroje volateľné AI.

**ReAct vzor** - Uvažuj → Konaj → Pozoruj → Opakuj.

**Správa relácie** - Oddelené kontexty pre rôznych používateľov.

**Nástroj** - Funkcia, ktorú môže AI agent zavolať.

**Popis nástroja** - Dokumentácia účelu a parametrov nástroja.

## Agentický modul - [Modul 05](../05-mcp/README.md)

**@Agent anotácia** - Označuje rozhrania ako AI agentov s deklaratívnym definovaním správania.

**Agent Listener** - Háčik na sledovanie vykonávania agenta cez `beforeAgentInvocation()` a `afterAgentInvocation()`.

**Agentický rozsah** - Zdieľaná pamäť, kde agenti ukladajú výstupy pomocou `outputKey` pre následných agentov.

**AgenticServices** - Továreň na tvorbu agentov pomocou `agentBuilder()` a `supervisorBuilder()`.

**Podmienený pracovný tok** - Smerovanie na základe podmienok k rôznym špecialistickým agentom.

**Človek v slučke** - Vzor pracovného toku s ľudskými kontrolnými bodmi pre schválenie alebo kontrolu obsahu.

**langchain4j-agentic** - Maven závislosť pre deklaratívne stavanie agentov (experimentálne).

**Cyklický pracovný tok** - Iteratívne vykonávanie agenta, kým nie je splnená podmienka (napr. skóre kvality ≥ 0,8).

**outputKey** - Parameter anotácie agenta určujúci, kde sa výsledky ukladajú v agentickom rozsahu.

**Paralelný pracovný tok** - Súbežné spustenie viacerých agentov pre nezávislé úlohy.

**Stratégia odpovede** - Ako supervízor formuluje konečnú odpoveď: POSLEDNÝ, ZHRNUTIE alebo SKÓROVANÝ.

**Sekvenčný pracovný tok** - Vykonávanie agentov v poradí, kde výstup plynie do ďalšieho kroku.

**Supervisor Agent vzor** - Pokročilý agentický vzor, kde supervízor LLM dynamicky rozhoduje, ktorých sub-agentov vyvolá.

## Protokol Kontextu Modelu (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven závislosť pre integráciu MCP v LangChain4j.

**MCP** - Model Context Protocol: štandard pre pripájanie AI aplikácií k externým nástrojom. Postav raz, používaj všade.

**MCP klient** - Aplikácia, ktorá sa pripája k MCP serverom, aby objavila a využila nástroje.

**MCP server** - Služba vystavujúca nástroje cez MCP s jasnými popismi a schémami parametrov.

**McpToolProvider** - Komponent LangChain4j, ktorý obaluje MCP nástroje pre použitie v AI službách a agentoch.

**McpTransport** - Rozhranie pre komunikáciu MCP. Implementácie zahŕňajú Stdio a HTTP.

**Stdio transport** - Lokálny procesný transport cez stdin/stdout. Užitočné pre prístup k súborovému systému alebo príkazové riadky.

**StdioMcpTransport** - Implementácia LangChain4j spúšťajúca MCP server ako podproces.

**Objavovanie nástrojov** - Klient vyhľadáva dostupné nástroje na serveri s popismi a schémami.

## Azure služby - [Modul 01](../01-introduction/README.md)

**Azure AI Search** - Cloudové vyhľadávanie s funkciou vektorov. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Nástroj na nasadzovanie Azure zdrojov.

**Azure OpenAI** - Podniková AI služba Microsoftu.

**Bicep** - Jazyk pre infraštruktúru-as-kód pre Azure. [Príručka infraštruktúry](../01-introduction/infra/README.md)

**Názov nasadenia** - Názov modelového nasadenia v Azure.

**GPT-5.2** - Najnovší OpenAI model s riadením uvažovania. [Modul 02](../02-prompt-engineering/README.md)

## Testovanie a vývoj - [Testing Guide](TESTING.md)

**Dev Container** - Kontajnerizované vývojové prostredie. [Konfigurácia](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Bezplatné AI modelové prostredie na hranie. [Modul 00](../00-quick-start/README.md)

**Testovanie v pamäti** - Testovanie s úložiskom v pamäti.

**Integračné testovanie** - Testovanie s reálnou infraštruktúrou.

**Maven** - Nástroj na automatizáciu zostavovania v Jave.

**Mockito** - Java framework na vytváranie mockov.

**Spring Boot** - Java aplikačný framework. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vylúčenie zodpovednosti**:
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, vezmite prosím na vedomie, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Originálny dokument v jeho pôvodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre dôležité informácie sa odporúča odborný ľudský preklad. Nie sme zodpovední za akékoľvek nedorozumenia alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->