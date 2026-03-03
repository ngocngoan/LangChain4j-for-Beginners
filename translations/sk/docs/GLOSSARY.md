# LangChain4j Slovník

## Obsah

- [Základné pojmy](../../../docs)
- [Komponenty LangChain4j](../../../docs)
- [Pojmy AI/ML](../../../docs)
- [Bezpečnostné opatrenia](../../../docs)
- [Prompt Engineering](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agenti a nástroje](../../../docs)
- [Agentic Modul](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Azure služby](../../../docs)
- [Testovanie a vývoj](../../../docs)

Rýchla referencia pre pojmy a koncepty používané v priebehu kurzu.

## Základné pojmy

**AI Agent** - Systém, ktorý používa AI na autonómne uvažovanie a konanie. [Modul 04](../04-tools/README.md)

**Chain** - Sekvencia operácií, kde výstup slúži ako vstup do ďalšieho kroku.

**Chunking** - Rozdeľovanie dokumentov na menšie časti. Typicky 300-500 tokenov s prekrytím. [Modul 03](../03-rag/README.md)

**Context Window** - Maximálny počet tokenov, ktoré model dokáže spracovať. GPT-5.2: 400K tokenov (do 272K vstup, 128K výstup).

**Embeddings** - Číselné vektory reprezentujúce význam textu. [Modul 03](../03-rag/README.md)

**Function Calling** - Model generuje štruktúrované požiadavky na volanie externých funkcií. [Modul 04](../04-tools/README.md)

**Halucinácia** - Keď modely generujú nesprávne ale vierohodné informácie.

**Prompt** - Textový vstup do jazykového modelu. [Modul 02](../02-prompt-engineering/README.md)

**Sémantické vyhľadávanie** - Vyhľadávanie podľa významu pomocou embeddings, nie podľa kľúčových slov. [Modul 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: bez pamäti. Stateful: uchováva históriu konverzácie. [Modul 01](../01-introduction/README.md)

**Tokens** - Základné textové jednotky, ktoré model spracováva. Ovplyvňuje náklady a limity. [Modul 01](../01-introduction/README.md)

**Tool Chaining** - Sekvenčné spúšťanie nástrojov, kde výstup ovplyvňuje ďalší hovor. [Modul 04](../04-tools/README.md)

## Komponenty LangChain4j

**AiServices** - Vytvára typovo bezpečné AI servisné rozhrania.

**OpenAiOfficialChatModel** - Zjednotený klient pre OpenAI a Azure OpenAI modely.

**OpenAiOfficialEmbeddingModel** - Vytvára embeddings pomocou oficiálneho klienta OpenAI (podporuje OpenAI aj Azure OpenAI).

**ChatModel** - Hlavné rozhranie pre jazykové modely.

**ChatMemory** - Uchováva históriu konverzácie.

**ContentRetriever** - Nájde relevantné časti dokumentov pre RAG.

**DocumentSplitter** - Rozdeľuje dokumenty na časti.

**EmbeddingModel** - Premení text na číselné vektory.

**EmbeddingStore** - Ukladá a získava embeddings.

**MessageWindowChatMemory** - Udržiava kĺzavé okno nedávnych správ.

**PromptTemplate** - Vytvára opakovane použiteľné promptové šablóny s `{{variable}}` zástupcami.

**TextSegment** - Textový úsek s metadátami. Používa sa v RAG.

**ToolExecutionRequest** - Reprezentuje požiadavku na vykonanie nástroja.

**UserMessage / AiMessage / SystemMessage** - Typy správ v konverzácii.

## Pojmy AI/ML

**Few-Shot Learning** - Poskytovanie príkladov v promptoch. [Modul 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - AI modely trénované na rozsiahlych textových dátach.

**Reasoning Effort** - Parameter GPT-5.2 riadiaci hĺbku uvažovania. [Modul 02](../02-prompt-engineering/README.md)

**Temperature** - Riadi náhodnosť výstupu. Nízka=deterministické, vysoká=kreatívne.

**Vektorová databáza** - Špecializovaná databáza pre embeddings. [Modul 03](../03-rag/README.md)

**Zero-Shot Learning** - Vykonávanie úloh bez príkladov. [Modul 02](../02-prompt-engineering/README.md)

## Bezpečnostné opatrenia - [Modul 00](../00-quick-start/README.md)

**Defense in Depth** - Viacvrstvový bezpečnostný prístup kombinujúci aplikačné guardrails so bezpečnostnými filtrami poskytovateľa.

**Hard Block** - Poskytovateľ vyhodí HTTP 400 chybu pri vážnom porušení obsahu.

**InputGuardrail** - Rozhranie LangChain4j na validáciu používateľského vstupu pred dosiahnutím LLM. Šetrí náklady a latenciu tým, že blokuje škodlivé prompty skôr.

**InputGuardrailResult** - Návratový typ validácie guardrailu: `success()` alebo `fatal("dôvod")`.

**OutputGuardrail** - Rozhranie na validáciu odpovedí AI pred ich vrátením používateľom.

**Filtery bezpečnosti poskytovateľa** - Vstavané filtre obsahu od AI poskytovateľov (napr. GitHub Models), ktoré zachytávajú porušenia na úrovni API.

**Soft Refusal** - Model zdvorilo odmietne odpovedať bez vyvolania chyby.

## Prompt Engineering - [Modul 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Postupné uvažovanie pre lepšiu presnosť.

**Constrained Output** - Vynútenie konkrétneho formátu alebo štruktúry.

**High Eagerness** - Vzor GPT-5.2 pre dôkladné uvažovanie.

**Low Eagerness** - Vzor GPT-5.2 pre rýchle odpovede.

**Multi-Turn Conversation** - Udržiavanie kontextu v priebehu výmen.

**Role-Based Prompting** - Nastavenie osobnosti modelu cez systémové správy.

**Self-Reflection** - Model hodnotí a zlepšuje svoj výstup.

**Structured Analysis** - Pevný hodnotiaci rámec.

**Task Execution Pattern** - Plánuj → Vykonaj → Zhrň.

## RAG (Retrieval-Augmented Generation) - [Modul 03](../03-rag/README.md)

**Document Processing Pipeline** - Načítať → rozdeliť → embedded → uložiť.

**In-Memory Embedding Store** - Nepersistentné uloženie na testovanie.

**RAG** - Kombinuje vyhľadávanie a generovanie na zakotvenie odpovedí.

**Similarity Score** - Miera (0-1) sémantickej podobnosti.

**Source Reference** - Metadáta o získanom obsahu.

## Agenti a nástroje - [Modul 04](../04-tools/README.md)

**@Tool Anotácia** - Označuje Java metódy ako AI volateľné nástroje.

**ReAct Pattern** - Uvažuj → Konaj → Pozoruj → Opakuj.

**Správa relácie** - Oddelené kontexty pre rôznych používateľov.

**Nástroj** - Funkcia, ktorú AI agent môže vyvolať.

**Popis nástroja** - Dokumentácia účelu nástroja a parametrov.

## Agentic Modul - [Modul 05](../05-mcp/README.md)

**@Agent Anotácia** - Označuje rozhrania ako AI agentov s deklaratívnym definovaním správania.

**Agent Listener** - Háčik na monitorovanie vykonávania agenta cez `beforeAgentInvocation()` a `afterAgentInvocation()`.

**Agentic Scope** - Zdieľaná pamäť, kde agenti ukladajú výstupy pomocou `outputKey` pre použitie downstream agentmi.

**AgenticServices** - Fabrika na vytváranie agentov pomocou `agentBuilder()` a `supervisorBuilder()`.

**Podmienkový workflow** - Trasa podľa podmienok ku rôznym špecializovaným agentom.

**Human-in-the-Loop** - Vzorec workflow pridávajúci ľudské kontrolné body na schválenie alebo revíziu obsahu.

**langchain4j-agentic** - Maven závislosť pre deklaratívnu tvorbu agentov (experimentálne).

**Loop Workflow** - Iteruje vykonávanie agenta až do splnenia podmienky (napr. skóre kvality ≥ 0.8).

**outputKey** - Parameter anotácie agenta určujúci, kde sa uložia výsledky v Agentic Scope.

**Paralelný workflow** - Súčasné spúšťanie viacerých agentov pre nezávislé úlohy.

**Response Strategy** - Ako dozorný agent formuluje konečnú odpoveď: LAST, SUMMARY alebo SCORED.

**Sekvenčný workflow** - Vykonávanie agentov podľa poradia, kde výstup prúdi do ďalšieho kroku.

**Supervisor Agent Pattern** - Pokročilý agentický vzorec, kde dozorný LLM dynamicky rozhoduje, ktorých podagentov spustiť.

## Model Context Protocol (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven závislosť pre integráciu MCP v LangChain4j.

**MCP** - Model Context Protocol: štandard pre prepájanie AI aplikácií s externými nástrojmi. Vytvor raz, používaj všade.

**MCP Client** - Aplikácia pripájajúca sa k MCP serverom na objavovanie a používanie nástrojov.

**MCP Server** - Služba vystavujúca nástroje cez MCP s jasnými popismi a schémami parametrov.

**McpToolProvider** - Komponent LangChain4j, ktorý obaluje MCP nástroje na použitie v AI službách a agentoch.

**McpTransport** - Rozhranie na MCP komunikáciu. Implementácie zahŕňajú Stdio a HTTP.

**Stdio Transport** - Lokálny procesný transport cez stdin/stdout. Užitečné pre prístup k súborovému systému alebo príkazové nástroje.

**StdioMcpTransport** - Implementácia LangChain4j, ktorá spúšťa MCP server ako podproces.

**Objavovanie nástrojov** - Klient dopytuje server po dostupných nástrojoch s popismi a schémami.

## Azure služby - [Modul 01](../01-introduction/README.md)

**Azure AI Search** - Cloudové vyhľadávanie s vektorovými schopnosťami. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Nástroj na nasadenie Azure prostriedkov.

**Azure OpenAI** - Podniková AI služba Microsoftu.

**Bicep** - Azure jazyk infraštruktúry ako kódu. [Sprievodca infraštruktúrou](../01-introduction/infra/README.md)

**Názov nasadenia** - Názov pre nasadenie modelu v Azure.

**GPT-5.2** - Najnovší model OpenAI s riadením uvažovania. [Modul 02](../02-prompt-engineering/README.md)

## Testovanie a vývoj - [Testovací sprievodca](TESTING.md)

**Dev Container** - Kontajnerizované vývojové prostredie. [Konfigurácia](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Bezplatné AI modelové prostredie. [Modul 00](../00-quick-start/README.md)

**In-Memory Testing** - Testovanie s využitím pamäťového uloženia.

**Integration Testing** - Testovanie s reálnou infraštruktúrou.

**Maven** - Java nástroj na automatizáciu buildov.

**Mockito** - Java knižnica na mockovanie.

**Spring Boot** - Java aplikačný framework. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Upozornenie**:  
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Hoci sa snažíme o presnosť, majte na pamäti, že automatizované preklady môžu obsahovať chyby alebo nepresnosti. Originálny dokument v jeho pôvodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nezodpovedáme za akékoľvek nedorozumenia alebo nesprávne výklady vyplývajúce z používania tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->