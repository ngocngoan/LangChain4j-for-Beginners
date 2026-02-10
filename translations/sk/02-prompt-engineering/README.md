# Modul 02: Inžinierstvo promptov s GPT-5.2

## Obsah

- [Čo sa naučíte](../../../02-prompt-engineering)
- [Predpoklady](../../../02-prompt-engineering)
- [Pochopenie inžinierstva promptov](../../../02-prompt-engineering)
- [Ako to používa LangChain4j](../../../02-prompt-engineering)
- [Základné vzory](../../../02-prompt-engineering)
- [Použitie existujúcich Azure zdrojov](../../../02-prompt-engineering)
- [Snímky obrazovky aplikácie](../../../02-prompt-engineering)
- [Preskúmanie vzorov](../../../02-prompt-engineering)
  - [Nízka vs vysoká ochota](../../../02-prompt-engineering)
  - [Vykonávanie úloh (nástrojové preambuly)](../../../02-prompt-engineering)
  - [Sebareflektujúci kód](../../../02-prompt-engineering)
  - [Štruktúrovaná analýza](../../../02-prompt-engineering)
  - [Viackolový chat](../../../02-prompt-engineering)
  - [Postupné uvažovanie](../../../02-prompt-engineering)
  - [Obmedzený výstup](../../../02-prompt-engineering)
- [Čo sa skutočne učíte](../../../02-prompt-engineering)
- [Ďalšie kroky](../../../02-prompt-engineering)

## Čo sa naučíte

V predchádzajúcom module ste videli, ako pamäť umožňuje konverzačné AI a používali ste GitHub Models pre základné interakcie. Teraz sa zameriame na to, ako klásť otázky - teda na samotné promptovanie - s využitím GPT-5.2 od Azure OpenAI. Spôsob, akým štruktúrujete prompt, dramaticky ovplyvňuje kvalitu odpovedí, ktoré dostanete.

Použijeme GPT-5.2, pretože prináša kontrolu uvažovania - môžete modelu povedať, koľko má premýšľať pred odpoveďou. To robí rôzne stratégie promptovania zreteľnejšími a pomáha vám pochopiť, kedy použiť ktorý prístup. Zároveň využijeme menšie limity pre GPT-5.2 na Azure oproti GitHub Models.

## Predpoklady

- Dokončený Modul 01 (nasadené Azure OpenAI zdroje)
- Súbor `.env` v koreňovom adresári so Azure povereniami (vytvorený pomocou `azd up` v Module 01)

> **Poznámka:** Ak Modul 01 nie je dokončený, najprv postupujte podľa jeho návodu na nasadenie.

## Pochopenie inžinierstva promptov

Inžinierstvo promptov znamená navrhnúť vstupný text tak, aby vám dôsledne prinášal požadované výsledky. Nie je to len o kladení otázok - ide o štruktúrovanie požiadaviek tak, aby model presne pochopil, čo chcete a ako to má dodať.

Predstavte si to ako dávate zadanie kolegovi. „Oprav chybu“ je nejasné. „Oprav výnimku null pointer v UserService.java na riadku 45 pridaním kontroly null“ je konkrétne. Jazykové modely fungujú rovnako - dôležitá je špecifickosť a štruktúra.

## Ako to používa LangChain4j

Tento modul demonštruje pokročilé vzory promptovania používajúce rovnaký základ LangChain4j z predchádzajúcich modulov, s dôrazom na štruktúru promptov a kontrolu uvažovania.

<img src="../../../translated_images/sk/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*Ako LangChain4j prepája vaše promptové požiadavky s Azure OpenAI GPT-5.2*

**Závislosti** – Modul 02 používa nasledujúce závislosti langchain4j definované v `pom.xml`:
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**Konfigurácia OpenAiOfficialChatModel** – [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Chat model je manuálne nakonfigurovaný ako Spring bean využívajúci oficiálneho OpenAI klienta, ktorý podporuje Azure OpenAI endpointy. Kľúčový rozdiel oproti Modulu 01 je v tom, ako štruktúrujeme prompty odosielané do `chatModel.chat()`, nie samotné nastavenie modelu.

**Systémové a používateľské správy** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j rozdeľuje typy správ pre prehľadnosť. `SystemMessage` nastavuje správanie a kontext AI (napr. „Ste recenzent kódu“), zatiaľ čo `UserMessage` obsahuje reálnu požiadavku. Toto rozdelenie umožňuje udržiavať konzistentné správanie AI naprieč rôznymi používateľskými dopytmi.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/sk/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage poskytuje trvalý kontext, zatiaľ čo UserMessages obsahujú individuálne požiadavky*

**MessageWindowChatMemory pre viackolové rozhovory** – Pre vzor viackolovej konverzácie znovu používame `MessageWindowChatMemory` z Modulu 01. Každá relácia má vlastnú inštanciu pamäte uloženú v `Map<String, ChatMemory>`, čo umožňuje paralelné vedenie viacerých rozhovorov bez zamiešania kontextu.

**Šablóny promptov** – Skutočným zameraním je inžinierstvo promptov, nie nové API LangChain4j. Každý vzor (nízka ochota, vysoká ochota, vykonávanie úloh atď.) používa tú istú metódu `chatModel.chat(prompt)`, ale s dôkladne štruktúrovanými promptmi. XML značky, inštrukcie a formátovanie sú súčasťou textu promptu, nie vlastnosťami LangChain4j.

**Kontrola uvažovania** – Uvažovanie GPT-5.2 je riadené cez inštrukcie v promptoch, ako napr. „maximálne 2 kroky uvažovania“ alebo „preskúmaj dôkladne“. Toto sú techniky inžinierstva promptov, nie konfigurácie LangChain4j. Knižnica len doručuje vaše prompty modelu.

Hlavný záver: LangChain4j poskytuje infraštruktúru (pripojenie modelu cez [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), pamäť, spracovanie správ cez [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), zatiaľ čo tento modul vás učí, ako efektívne vytvárať prompty v rámci tejto infraštruktúry.

## Základné vzory

Nie všetky problémy musia byť riešené rovnakým prístupom. Niektoré otázky potrebujú rýchle odpovede, iné hlboké uvažovanie. Niektoré potrebujú viditeľné uvažovanie, iné len výsledky. Tento modul pokrýva osem vzorov promptovania – každý optimalizovaný pre rôzne scenáre. Vyskúšate ich všetky, aby ste sa naučili, kedy ktorý prístup najlepšie funguje.

<img src="../../../translated_images/sk/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Prehľad ôsmich vzorov inžinierstva promptov a ich použitia*

<img src="../../../translated_images/sk/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Nízka ochota (rýchle, priame) vs vysoká ochota (dôkladné, skúmajúce) prístupy uvažovania*

**Nízka ochota (rýchle & zamerané)** – Pre jednoduché otázky, kde chcete rýchle a priame odpovede. Model robí minimálne uvažovanie – maximálne 2 kroky. Použite to na výpočty, vyhľadávanie alebo priame otázky.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Preskúmajte s GitHub Copilot:** Otvorte [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) a opýtajte sa:
> - „Aký je rozdiel medzi nízkou a vysokou ochotou pri promptovaní?“
> - „Ako XML značky v promptoch pomáhajú štruktúrovať odpoveď AI?“
> - „Kedy použiť vzory sebareflexie a kedy priame inštrukcie?“

**Vysoká ochota (hlboké & dôkladné)** – Pre zložité problémy, kde chcete komplexnú analýzu. Model dôkladne skúma a ukazuje detailné uvažovanie. Použite to na návrh systémov, architektonické rozhodnutia alebo komplexný výskum.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Vykonávanie úloh (postupný pokrok)** – Pre viackrokové postupy. Model poskytuje plán vopred, rozpráva každý krok počas práce a potom dáva zhrnutie. Použite to na migrácie, implementácie alebo akýkoľvek viackrokový proces.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought promptovanie explicitne žiada model, aby ukázal svoj proces uvažovania, čím zlepšuje presnosť pri zložitých úlohách. Postupný rozklad pomáha ľuďom aj AI pochopiť logiku.

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Opýtajte sa na tento vzor:
> - „Ako by som prispôsobil vzor vykonávania úloh pre dlhodobé operácie?“
> - „Aké sú najlepšie praktiky pre štruktúrovanie nástrojových preambul v produkčných aplikáciách?“
> - „Ako môžem zachytiť a zobrazovať priebežné aktualizácie stavu v UI?“

<img src="../../../translated_images/sk/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Plán → Vykonanie → Zhrnutie pracovného toku pre viackrokové úlohy*

**Sebareflektujúci kód** – Na generovanie produkčného kódu. Model generuje kód, kontroluje ho podľa kvalitatívnych kritérií a iteratívne ho zlepšuje. Použite to pri budovaní nových funkcií alebo služieb.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sk/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iteračný cyklus zlepšovania – generuj, hodnoti, identifikuj problémy, vylepši, opakuj*

**Štruktúrovaná analýza** – Na konzistentné hodnotenie. Model prehodnocuje kód podľa pevne daného rámca (správnosť, postupy, výkon, bezpečnosť). Použite to na revízie kódu alebo hodnotenia kvality.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Opýtajte sa na štruktúrovanú analýzu:
> - „Ako môžem prispôsobiť rámec analýzy pre rôzne typy revízií kódu?“
> - „Aký je najlepší spôsob na spracovanie a programatickú reakciu na štruktúrovaný výstup?“
> - „Ako zabezpečiť konzistentnú úroveň závažnosti naprieč rôznymi recenziami?“

<img src="../../../translated_images/sk/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Rámec so štyrmi kategóriami pre konzistentné revízie kódu so závažnostnými úrovňami*

**Viackolový chat** – Pre rozhovory vyžadujúce kontext. Model si pamätá predchádzajúce správy a stavia na nich. Použite to na interaktívnu pomoc alebo zložité otázky a odpovede.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/sk/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Ako sa kontext rozhovoru hromadí cez viaceré kolá až do limitu tokenov*

**Postupné uvažovanie** – Pre problémy vyžadujúce viditeľnú logiku. Model ukazuje explicitné uvažovanie pri každom kroku. Použite to na matematické problémy, logické hádanky alebo keď potrebujete pochopiť proces myslenia.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sk/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Rozklad problémov na explicitné logické kroky*

**Obmedzený výstup** – Pre odpovede s konkrétnymi formátovými požiadavkami. Model striktne dodržiava pravidlá formátu a dĺžky. Použite to pre zhrnutia alebo keď potrebujete presnú štruktúru výstupu.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sk/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Vynucovanie presných požiadaviek na formát, dĺžku a štruktúru*

## Použitie existujúcich Azure zdrojov

**Overenie nasadenia:**

Uistite sa, že súbor `.env` existuje v koreňovom adresári so Azure povereniami (vytvorený počas Modulu 01):
```bash
cat ../.env  # Malo by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spustenie aplikácie:**

> **Poznámka:** Ak ste už spustili všetky aplikácie pomocou `./start-all.sh` z Modulu 01, tento modul už beží na porte 8083. Môžete preskočiť spúšťacie príkazy nižšie a prejsť priamo na http://localhost:8083.

**Možnosť 1: Použitie Spring Boot Dashboard (odporúčané pre používateľov VS Code)**

Dev kontajner obsahuje rozšírenie Spring Boot Dashboard, ktoré poskytuje vizuálne rozhranie na správu všetkých Spring Boot aplikácií. Nájdete ho v Activity Bar na ľavej strane VS Code (ikonka Spring Boot).

Zo Spring Boot Dashboard môžete:
- Zobraziť všetky dostupné Spring Boot aplikácie v pracovnom priestore
- Jedným kliknutím spúšťať/zastavovať aplikácie
- Prezerať logy aplikácií v reálnom čase
- Monitorovať stav aplikácií

Jednoducho kliknite na tlačidlo spustiť vedľa „prompt-engineering“ pre spustenie tohto modulu alebo spustite všetky moduly naraz.

<img src="../../../translated_images/sk/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Možnosť 2: Použitie shell skriptov**

Spustite všetky webové aplikácie (moduly 01-04):

**Bash:**
```bash
cd ..  # Z koreňového adresára
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Z koreňového adresára
.\start-all.ps1
```

Alebo spustite len tento modul:

**Bash:**
```bash
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

Oba skripty automaticky načítajú premenné prostredia zo súboru `.env` v koreňovom adresári a postavia JARy, ak ešte neexistujú.

> **Poznámka:** Ak radšej staviate moduly manuálne pred spustením:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Otvorte http://localhost:8083 vo vašom prehliadači.

**Na zastavenie:**

**Bash:**
```bash
./stop.sh  # Len tento modul
# Alebo
cd .. && ./stop-all.sh  # Všetky moduly
```

**PowerShell:**
```powershell
.\stop.ps1  # Tento modul iba
# Alebo
cd ..; .\stop-all.ps1  # Všetky moduly
```

## Snímky obrazovky aplikácie

<img src="../../../translated_images/sk/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Hlavný dashboard zobrazujúci všetky 8 vzorov inžinierstva promptov s ich charakteristikami a použitím*

## Preskúmanie vzorov

Webové rozhranie vám umožňuje experimentovať s rôznymi stratégiami promptovania. Každý vzor rieši iné problémy - vyskúšajte ich a uvidíte, kedy ktorý prístup najlepšie funguje.

### Nízka vs vysoká ochota

Opýtajte sa jednoduchú otázku ako „Čo je 15 % zo 200?“ s nízkou ochotou. Dostanete okamžitú, priamu odpoveď. Teraz sa opýtajte niečo zložitejšie ako „Navrhnite caching stratégiu pre API s vysokou návštevnosťou“ s vysokou ochotou. Sledujte, ako model spomaľuje a poskytuje detailné uvažovanie. Rovnaký model, rovnaká štruktúra otázky – ale prompt povie, koľko má model premýšľať.
<img src="../../../translated_images/sk/low-eagerness-demo.898894591fb23aa0.webp" alt="Demo nízkej pripravenosti" width="800"/>

*Rýchly výpočet s minimálnym uvažovaním*

<img src="../../../translated_images/sk/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Demo vysokej pripravenosti" width="800"/>

*Komplexná stratégia cache (2,8 MB)*

### Vykonávanie úloh (Úvodné nastavenie nástrojov)

Viacstupňové pracovné postupy profitujú z vopred plánovania a komentovania priebehu. Model načrtne, čo urobí, komentuje každý krok a potom zhrnie výsledky.

<img src="../../../translated_images/sk/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Demo vykonávania úloh" width="800"/>

*Vytvorenie REST endpointu s komentovaním krok za krokom (3,9 MB)*

### Kód so sebakritikou

Skúste "Vytvoriť službu na overenie e-mailu". Namiesto generovania kódu a zastavenia model generuje, hodnotí podľa kritérií kvality, identifikuje slabé miesta a vylepšuje. Uvidíte, ako iteruje, až kým kód nespĺňa produkčné štandardy.

<img src="../../../translated_images/sk/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Demo kódu so sebakritikou" width="800"/>

*Kompletná služba overenia e-mailu (5,2 MB)*

### Štruktúrovaná analýza

Kontroly kódu potrebujú konzistentné hodnotiace rámce. Model analyzuje kód pomocou pevných kategórií (správnosť, postupy, výkon, bezpečnosť) s rôznymi úrovňami závažnosti.

<img src="../../../translated_images/sk/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Demo štruktúrovanej analýzy" width="800"/>

*Kontrola kódu založená na rámci*

### Viackolové rozhovory

Opýtajte sa „Čo je Spring Boot?“ a hneď potom „Ukáž mi príklad“. Model si pamätá vašu prvú otázku a dá vám špecifický príklad Spring Boot. Bez pamäti by druhá otázka bola príliš nejasná.

<img src="../../../translated_images/sk/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Demo viackolového rozhovoru" width="800"/>

*Zachovanie kontextu medzi otázkami*

### Zdôvodnenie krok za krokom

Vyberte matematický problém a skúste ho s metódou krok za krokom aj s nízkou pripravenosťou. Nízka pripravenosť vám dá iba odpoveď – rýchlu, ale nejasnú. Krok za krokom vám ukáže každý výpočet a rozhodnutie.

<img src="../../../translated_images/sk/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Demo zdôvodnenia krok za krokom" width="800"/>

*Matematický problém s explicitnými krokmi*

### Obmedzený výstup

Keď potrebujete špecifické formáty alebo počet slov, tento vzor zabezpečuje prísne dodržiavanie. Skúste vygenerovať súhrn s presne 100 slovami v bodoch.

<img src="../../../translated_images/sk/constrained-output-demo.567cc45b75da1633.webp" alt="Demo obmedzeného výstupu" width="800"/>

*Zhrnutie strojového učenia s kontrolou formátu*

## Čo sa naozaj učíte

**Úsilie o rozumovanie mení všetko**

GPT-5.2 vám umožňuje riadiť výpočtové úsilie prostredníctvom vašich promptov. Nízke úsilie znamená rýchle odpovede s minimálnym preskúmaním. Vysoké úsilie znamená, že model si dá čas na hlboké premýšľanie. Učíte sa prispôsobiť úsilie zložitosti úlohy – nestrácajte čas na jednoduché otázky, ale ani neponáhľajte zložité rozhodnutia.

**Štruktúra riadi správanie**

Všimli ste si XML tagy v promptoch? Nie sú len na ozdobu. Modely spoľahlivejšie nasledujú štruktúrované inštrukcie než voľný text. Ak potrebujete viacstupňové procesy alebo zložitú logiku, štruktúra pomáha modelu sledovať, kde sa nachádza a čo príde ďalej.

<img src="../../../translated_images/sk/prompt-structure.a77763d63f4e2f89.webp" alt="Štruktúra promptu" width="800"/>

*Anatómia dobre štruktúrovaného promptu s jasnými sekciami a organizáciou v štýle XML*

**Kvalita cez sebahodnotenie**

Vzory so sebakritikou fungujú tak, že robia kritériá kvality explicitnými. Namiesto toho, aby ste dúfali, že model "to spraví správne", presne mu poviete, čo "správne" znamená: správna logika, spracovanie chýb, výkon, bezpečnosť. Model potom dokáže sám vyhodnotiť svoj výstup a vylepšiť ho. To z generovania kódu robí proces, nie lotériu.

**Kontext je obmedzený**

Viackolové rozhovory fungujú tak, že ku každému požiadavku pripoja históriu správ. Ale existuje limit – každý model má maximálny počet tokenov. Ako rozhovory rastú, budete potrebovať stratégie, ako udržať relevantný kontext bez dosiahnutia limitu. Tento modul vám ukáže, ako pamäť funguje; neskôr sa naučíte, kedy zhrnúť, kedy zabudnúť a kedy vyvolať.

## Ďalšie kroky

**Ďalší modul:** [03-rag - RAG (Generovanie s doplnením vyhľadávania)](../03-rag/README.md)

---

**Navigácia:** [← Predchádzajúci: Modul 01 - Úvod](../01-introduction/README.md) | [Späť na hlavnú stránku](../README.md) | [Ďalší: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Upozornenie**:  
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, uvedomte si, že automatizované preklady môžu obsahovať chyby alebo nepresnosti. Originálny dokument v pôvodnom jazyku by mal byť považovaný za záväzný zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za akékoľvek nedorozumenia alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->