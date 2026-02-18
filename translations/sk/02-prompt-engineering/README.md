# Modul 02: Prompt inžinierstvo s GPT-5.2

## Obsah

- [Čo sa naučíte](../../../02-prompt-engineering)
- [Predpoklady](../../../02-prompt-engineering)
- [Pochopenie prompt inžinierstva](../../../02-prompt-engineering)
- [Základy prompt inžinierstva](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Pokročilé vzory](../../../02-prompt-engineering)
- [Použitie existujúcich Azure zdrojov](../../../02-prompt-engineering)
- [Snímky obrazovky aplikácie](../../../02-prompt-engineering)
- [Preskúmanie vzorov](../../../02-prompt-engineering)
  - [Nízka vs vysoká ochota](../../../02-prompt-engineering)
  - [Vykonávanie úloh (preambuly nástrojov)](../../../02-prompt-engineering)
  - [Sebareflexívny kód](../../../02-prompt-engineering)
  - [Štruktúrovaná analýza](../../../02-prompt-engineering)
  - [Viackolový chat](../../../02-prompt-engineering)
  - [Krok za krokom uvažovanie](../../../02-prompt-engineering)
  - [Obmedzený výstup](../../../02-prompt-engineering)
- [Čo sa naozaj učíte](../../../02-prompt-engineering)
- [Ďalšie kroky](../../../02-prompt-engineering)

## Čo sa naučíte

<img src="../../../translated_images/sk/what-youll-learn.c68269ac048503b2.webp" alt="Čo sa naučíte" width="800"/>

V predchádzajúcom module ste videli, ako pamäť umožňuje konverzačné AI a použili ste GitHub Modely na základné interakcie. Teraz sa zameriame na to, ako kladiete otázky — samotné prompty — pomocou Azure OpenAI GPT-5.2. Spôsob, akým štruktúrujete svoje prompty, dramaticky ovplyvňuje kvalitu odpovedí, ktoré dostanete. Začneme prehľadom základných techník promptovania a potom prejdeme k ôsmim pokročilým vzorom, ktoré plne využívajú schopnosti GPT-5.2.

Použijeme GPT-5.2, pretože zavádza kontrolu uvažovania - môžete modelu povedať, koľko má premýšľať pred odpoveďou. To robí rôzne stratégie promptovania zreteľnejšími a pomáha vám pochopiť, kedy použiť ktorý prístup. Tiež využijeme menej limitov rýchlosti na GPT-5.2 v Azure v porovnaní s GitHub modelmi.

## Predpoklady

- Dokončený Modul 01 (nasadené Azure OpenAI zdroje)
- Súbor `.env` v koreňovom adresári s Azure povereniami (vytvorený príkazom `azd up` v Module 01)

> **Poznámka:** Ak ste neabsolvovali Modul 01, najskôr postupujte podľa tam uvedených pokynov na nasadenie.

## Pochopenie prompt inžinierstva

<img src="../../../translated_images/sk/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Čo je prompt inžinierstvo?" width="800"/>

Prompt inžinierstvo spočíva v navrhovaní vstupného textu, ktorý vám konzistentne prinesie požadované výsledky. Nejde len o kladenie otázok – ide o štruktúrovanie požiadaviek tak, aby model presne pochopil, čo chcete a ako to doručiť.

Predstavte si to ako dávanie pokynov kolegovi. „Oprav chybu“ je nejasné. „Oprav výnimku null pointer v UserService.java na riadku 45 pridaním kontroly na null“ je konkrétne. Jazykové modely fungujú rovnako – dôležitá je špecifickosť a štruktúra.

<img src="../../../translated_images/sk/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Ako LangChain4j zapadá" width="800"/>

LangChain4j poskytuje infraštruktúru — pripojenia k modelom, pamäť a typy správ — zatiaľ čo vzory promptov sú len starostlivo štruktúrovaný text, ktorý cez túto infraštruktúru posielate. Kľúčové stavebné kamene sú `SystemMessage` (nastavuje správanie a rolu AI) a `UserMessage` (nesie vašu skutočnú požiadavku).

## Základy prompt inžinierstva

<img src="../../../translated_images/sk/five-patterns-overview.160f35045ffd2a94.webp" alt="Prehľad piatich vzorov prompt inžinierstva" width="800"/>

Predtým, než sa ponoríme do pokročilých vzorov v tomto module, pozrime sa na päť základných techník promptovania. Sú to stavebné kamene, ktoré by mal poznať každý prompt inžinier. Ak ste už prešli [modulom Rýchly začiatok](../00-quick-start/README.md#2-prompt-patterns), videli ste ich v akcii — tu je konceptuálny rámec za nimi.

### Zero-Shot Prompting

Najjednoduchší prístup: dajte modelu priamy pokyn bez ukážok. Model sa spolieha výlučne na svoje trénovanie, aby pochopil a vykonal úlohu. Funguje to dobre pre jednoduché požiadavky, kde je očakávané správanie zjavné.

<img src="../../../translated_images/sk/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Priamy pokyn bez ukážok — model vysvetľuje úlohu len z pokynu*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odpoveď: "Pozitívne"
```

**Kedy použiť:** Jednoduché klasifikácie, priame otázky, preklady alebo akákoľvek úloha, ktorú model zvládne bez ďalších pokynov.

### Few-Shot Prompting

Poskytnite príklady, ktoré ukazujú vzor, ktorý chcete, aby model nasledoval. Model sa naučí očakávaný formát vstupu a výstupu podľa vašich príkladov a použije ho na nové vstupy. Toto dramaticky zlepšuje konzistentnosť pre úlohy, kde požadovaný formát alebo správanie nie je zjavné.

<img src="../../../translated_images/sk/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Učenie sa z príkladov — model identifikuje vzor a aplikuje ho na nové vstupy*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**Kedy použiť:** Vlastné klasifikácie, konzistentné formátovanie, doménovo špecifické úlohy alebo keď sú výsledky zero-shot nekonzistentné.

### Chain of Thought

Požiadajte model, aby ukázal svoje uvažovanie krok za krokom. Namiesto toho, aby preskočil priamo na odpoveď, model rozloží problém a explicitne pracuje s jednotlivými časťami. Zlepšuje to presnosť u matematických, logických a viacstupňových uvažovacích úlohach.

<img src="../../../translated_images/sk/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Krok za krokom uvažovanie — rozklad komplexných problémov na explicitné logické kroky*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model ukazuje: 15 - 8 = 7, potom 7 + 12 = 19 jabĺk
```

**Kedy použiť:** Matematické problémy, logické hádanky, ladenie alebo akákoľvek úloha, kde zobrazenie procesu uvažovania zvyšuje presnosť a dôveru.

### Role-Based Prompting

Nastavte osobnosť alebo rolu pre AI pred položením otázky. Poskytuje kontext, ktorý formuje tón, hĺbku a zameranie odpovede. „Softvérový architekt“ dáva iné rady ako „junior vývojár“ alebo „bezpečnostný auditor“.

<img src="../../../translated_images/sk/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Nastavenie kontextu a osobnosti — rovnaká otázka dostane odlišnú odpoveď podľa priradenej roly*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**Kedy použiť:** Prehliadky kódu, doučovanie, doménovo špecifická analýza alebo keď potrebujete odpovede prispôsobené konkrétnej úrovni odbornosti alebo perspektíve.

### Prompt Templates

Vytvorte znovupoužiteľné prompty s premennými zástupcami. Namiesto písania nového promptu zakaždým definujte šablónu raz a vyplňte rôzne hodnoty. Trieda `PromptTemplate` z LangChain4j to uľahčuje pomocou syntaxe `{{variable}}`.

<img src="../../../translated_images/sk/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Znovupoužiteľné prompty s premennými zástupcami — jedna šablóna, mnoho použití*

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

**Kedy použiť:** Opakované dopyty s rôznymi vstupmi, dávkové spracovanie, tvorba znovupoužiteľných AI workflow, alebo situácie, kde sa štruktúra promptu nemení, len dáta.

---

Tieto päť základných techník vám poskytuje pevný nástrojový set pre väčšinu promptovacích úloh. Zvyšok tohto modulu na nich stavia s **osem pokročilými vzormi**, ktoré využívajú kontrolu uvažovania GPT-5.2, seba-hodnotenie a schopnosti štruktúrovaného výstupu.

## Pokročilé vzory

Po pokrytí základov sa presuňme k ôsmim pokročilým vzorom, ktoré robia tento modul jedinečným. Nie všetky problémy vyžadujú rovnaký prístup. Niektoré otázky potrebujú rýchle odpovede, iné hlboké premýšľanie. Niektoré vyžadujú viditeľné uvažovanie, iné len výsledky. Každý vzor nižšie je optimalizovaný pre inú situáciu — a kontrola uvažovania GPT-5.2 robí rozdiely ešte výraznejšími.

<img src="../../../translated_images/sk/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osem vzorov promptovania" width="800"/>

*Prehľad ôsmich vzorov prompt inžinierstva a ich použitia*

<img src="../../../translated_images/sk/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontrola uvažovania s GPT-5.2" width="800"/>

*Kontrola uvažovania GPT-5.2 vám umožňuje špecifikovať, koľko má model premýšľať — od rýchlych priamych odpovedí po hlboké preskúmanie*

**Nízka ochota (rýchle a zamerané)** - Pre jednoduché otázky, kde chcete rýchle priame odpovede. Model robí minimálne uvažovanie – max. 2 kroky. Použite pre výpočty, vyhľadávania alebo jednoduché otázky.

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Preskúmajte s GitHub Copilot:** Otvorte [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) a opýtajte sa:
> - „Aký je rozdiel medzi vzormi nízkej a vysokej ochoty promptovania?“
> - „Ako XML tagy v promptoch pomáhajú štruktúrovať odpoveď AI?“
> - „Kedy by som mal použiť sebareflexívne vzory vs. priame inštrukcie?“

**Vysoká ochota (hlboké a dôkladné)** - Pre komplexné problémy, kde chcete komplexnú analýzu. Model dôkladne skúma a ukazuje detailné uvažovanie. Použite pre návrh systémov, rozhodnutia o architektúre alebo komplexný výskum.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Vykonávanie úloh (postup krok za krokom)** - Pre viacstupňové workflow. Model poskytuje plán vopred, popisuje každý krok pri práci a potom dáva zhrnutie. Použite pre migrácie, implementácie alebo akýkoľvek viacstupňový proces.

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

Promptovanie Chain-of-Thought explicitne žiada model, aby ukázal proces uvažovania, čo zlepšuje presnosť pri zložitých úlohách. Postupné rozkladanie pomáha ľuďom aj AI pochopiť logiku.

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Opýtajte sa na tento vzor:
> - „Ako by som prispôsobil vzor vykonávania úloh pre dlhodobé operácie?“
> - „Aké sú najlepšie praktiky pre štruktúrovanie preambúl nástrojov v produkčných aplikáciách?“
> - „Ako môžem zachytiť a zobraziť priebežné aktualizácie postupu v UI?“

<img src="../../../translated_images/sk/task-execution-pattern.9da3967750ab5c1e.webp" alt="Vzor vykonávania úloh" width="800"/>

*Plán → Vykonaj → Zhrň workflow pre viacstupňové úlohy*

**Sebareflexívny kód** - Pre generovanie produkčne kvalitného kódu. Model generuje kód podľa produkčných štandardov s riadnym spracovaním chýb. Použite pri tvorbe nových funkcií alebo služieb.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sk/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cyklus sebareflexie" width="800"/>

*Iteračný cyklus zlepšovania – generovať, hodnotiť, identifikovať problémy, zlepšiť, opakovať*

**Štruktúrovaná analýza** - Pre konzistentné hodnotenie. Model preskúma kód podľa pevne stanovenej metodiky (správnosť, praktiky, výkon, bezpečnosť, udržiavateľnosť). Použite pri prehliadkach kódu alebo hodnotení kvality.

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Opýtajte sa na štruktúrovanú analýzu:
> - „Ako môžem prispôsobiť rámec analýzy pre rôzne typy prehliadok kódu?“
> - „Aký je najlepší spôsob, ako programovo spracovať a reagovať na štruktúrovaný výstup?“
> - „Ako zabezpečiť konzistentnú úroveň závažnosti v rôznych hodnotiacich sedeniach?“

<img src="../../../translated_images/sk/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Vzor štruktúrovanej analýzy" width="800"/>

*Rámec pre konzistentné prehliadky kódu so závažnostnými úrovňami*

**Viackolový chat** - Pre konverzácie, ktoré potrebujú kontext. Model si pamätá predchádzajúce správy a nadväzuje na ne. Použite pre interaktívne poradenské relácie alebo komplexné Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/sk/context-memory.dff30ad9fa78832a.webp" alt="Pamäť kontextu" width="800"/>

*Ako sa kontext konverzácie kumuluje cez viacero kôl až do limitu tokenov*

**Krok za krokom uvažovanie** - Pre problémy vyžadujúce viditeľnú logiku. Model ukazuje explicitné uvažovanie pre každý krok. Použite pri matematických problémoch, logických hádankách alebo keď potrebujete pochopiť proces uvažovania.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sk/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Vzor krok za krokom" width="800"/>

*Rozkladanie problémov na explicitné logické kroky*

**Obmedzený výstup** - Pre odpovede so špecifickými požiadavkami na formát. Model striktne dodržiava pravidlá formátu a dĺžky. Použite pre súhrny alebo keď potrebujete presnú štruktúru výstupu.

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

<img src="../../../translated_images/sk/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Vzor obmedzeného výstupu" width="800"/>

*Uplatnenie špecifických požiadaviek na formát, dĺžku a štruktúru*

## Použitie existujúcich Azure zdrojov

**Overenie nasadenia:**

Uistite sa, že súbor `.env` existuje v koreňovom adresári s Azure povereniami (vytvorené počas Modulu 01):
```bash
cat ../.env  # Mala by ukázať AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spustenie aplikácie:**

> **Poznámka:** Ak ste už spustili všetky aplikácie pomocou `./start-all.sh` z Modulu 01, tento modul už beží na porte 8083. Môžete preskočiť nasledujúce príkazy na spustenie a ísť priamo na http://localhost:8083.

**Možnosť 1: Použitie Spring Boot Dashboard (odporúčané pre používateľov VS Code)**

Vývojársky kontajner obsahuje rozšírenie Spring Boot Dashboard, ktoré poskytuje vizuálne rozhranie na správu všetkých aplikácií Spring Boot. Nájdete ho v Activity Bare na ľavej strane VS Code (hľadajte ikonu Spring Boot).

Zo Spring Boot Dashboard môžete:
- Vidieť všetky dostupné Spring Boot aplikácie v pracovnom priestore
- Spúšťať/zastavovať aplikácie jedným kliknutím
- Zobraziť logy aplikácie v reálnom čase
- Monitorovať stav aplikácie
Jednoducho kliknite na tlačidlo prehrávania vedľa „prompt-engineering“ pre spustenie tohto modulu alebo spustite naraz všetky moduly.

<img src="../../../translated_images/sk/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Možnosť 2: Použitie shell skriptov**

Spustite všetky webové aplikácie (moduly 01-04):

**Bash:**
```bash
cd ..  # Zo základného adresára
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

Oba skripty automaticky načítajú premenné prostredia zo základného súboru `.env` a zostavia JAR súbory, ak ešte neexistujú.

> **Poznámka:** Ak uprednostňujete manuálnu kompiláciu všetkých modulov pred spustením:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Otvorte v prehliadači http://localhost:8083.

**Na zastavenie:**

**Bash:**
```bash
./stop.sh  # Iba tento modul
# Alebo
cd .. && ./stop-all.sh  # Všetky moduly
```

**PowerShell:**
```powershell
.\stop.ps1  # Len tento modul
# Alebo
cd ..; .\stop-all.ps1  # Všetky moduly
```

## Screenshoty aplikácie

<img src="../../../translated_images/sk/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Hlavný dashboard zobrazujúci všetkých 8 vzorov prompt engineering s ich charakteristikami a prípadmi použitia*

## Preskúmanie vzorov

Webové rozhranie vám umožňuje experimentovať s rôznymi stratégiami promptov. Každý vzor rieši iné problémy – vyskúšajte ich, aby ste videli, kedy ktorý prístup vyniká.

### Nízka verzus vysoká ochota

Opýtajte sa jednoduchú otázku ako „Čo je 15 % z 200?“ použitím nízkej ochoty. Dostanete okamžitú, priamu odpoveď. Teraz položte niečo zložité ako „Navrhnite stratégiu cachovania pre API s vysokou návštevnosťou“ použitím vysokej ochoty. Sledujte, ako sa model spomalí a poskytne detailné zdôvodnenie. Rovnaký model, rovnaká štruktúra otázky – ale prompt mu povie, koľko premýšľania má použiť.

<img src="../../../translated_images/sk/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Rýchly výpočet s minimálnym zdôvodnením*

<img src="../../../translated_images/sk/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Komplexná stratégia cachovania (2,8 MB)*

### Vykonávanie úloh (Úvodné texty nástrojov)

Viacstupňové pracovné postupy využívajú plánovanie a rozprávanie priebehu vopred. Model načrtne, čo urobí, komentuje každý krok a potom zhrnie výsledky.

<img src="../../../translated_images/sk/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Vytvorenie REST endpointu so slovným vyjadrením každého kroku (3,9 MB)*

### Sebareflexívny kód

Skúste „Vytvorte službu na overovanie emailov“. Namiesto toho, aby len generoval kód a zastavil sa, model generuje, hodnotí podľa kritérií kvality, identifikuje slabiny a zlepšuje. Uvidíte, ako iteruje, až kým kód nespĺňa výrobný štandard.

<img src="../../../translated_images/sk/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Kompletná služba overovania emailov (5,2 MB)*

### Štruktúrovaná analýza

Kontroly kódu potrebujú konzistentné hodnotiace rámce. Model analyzuje kód podľa pevných kategórií (správnosť, praktiky, výkon, bezpečnosť) s úrovňami závažnosti.

<img src="../../../translated_images/sk/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Kontrola kódu založená na rámci*

### Viackolový chat

Opýtajte sa „Čo je Spring Boot?“ a hneď pokračujte otázkou „Ukáž mi príklad“. Model si pamätá vašu prvú otázku a priamo vám dá konkrétny príklad Spring Boot. Bez pamäte by druhá otázka bola príliš nejasná.

<img src="../../../translated_images/sk/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Uchovanie kontextu medzi otázkami*

### Krok za krokom zdôvodnenie

Vyberte si matematický problém a skúste ho vyriešiť pomocou Krok za krokom zdôvodnenia a Nízkej ochoty. Nízka ochota vám dá iba odpoveď – rýchlu, ale nepriehľadnú. Krok za krokom vám ukáže každý výpočet a rozhodnutie.

<img src="../../../translated_images/sk/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matematický problém s explicitnými krokmi*

### Obmedzený výstup

Keď potrebujete špecifické formáty alebo počet slov, tento vzor vynucuje prísne dodržiavanie. Vyskúšajte vygenerovať zhrnutie s presne 100 slovami v odrážkovom formáte.

<img src="../../../translated_images/sk/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Zhrnutie strojového učenia s kontrolou formátu*

## Čo sa naozaj učíte

**Úsilie o zdôvodnenie mení všetko**

GPT-5.2 vám umožňuje ovládať výpočtové úsilie prostredníctvom vašich promptov. Nízke úsilie znamená rýchle odpovede s minimálnym skúmaním. Vysoké úsilie znamená, že model si dá čas na dôkladné zamyslenie. Učíte sa prispôsobiť úsilie zložitosti úlohy – nestrácajte čas na jednoduché otázky, ale ani neponáhľajte pri zložitých rozhodnutiach.

**Štruktúra usmerňuje správanie**

Vidíte XML tagy v promptoch? Nie sú dekoráciou. Modely spoľahlivejšie sledujú štruktúrované inštrukcie než voľný text. Keď potrebujete viacstupňové procesy alebo komplexnú logiku, štruktúra pomáha modelu sledovať, kde sa nachádza a čo nasleduje.

<img src="../../../translated_images/sk/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatómia dobre štruktúrovaného promptu s jasnými sekciami a XML-štýlovou organizáciou*

**Kvalita cez seba hodnotenie**

Sebareflexívne vzory fungujú tak, že explicitne uvádzajú kritériá kvality. Namiesto toho, aby ste dúfali, že model „spraví to správne“, poviete mu presne, čo znamená „správne“: správna logika, spracovanie chýb, výkon, bezpečnosť. Model potom dokáže vyhodnotiť svoj vlastný výstup a zlepšiť sa. To mení generovanie kódu z lotérie na proces.

**Kontext je konečný**

Viackolové rozhovory fungujú tak, že ku každému požiadavku pridajú históriu správ. Ale je tu limit – každý model má maximálny počet tokenov. Ako konverzácie rastú, budete potrebovať stratégie, ako uchovať relevantný kontext bez prekročenia limitu. Tento modul vám ukáže, ako pamäť funguje; neskôr sa naučíte, kedy zhrnúť, kedy zabudnúť a kedy načítať.

## Ďalšie kroky

**Ďalší modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigácia:** [← Predchádzajúce: Modul 01 - Úvod](../01-introduction/README.md) | [Späť na hlavnú](../README.md) | [Ďalší: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vylúčenie zodpovednosti**:
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, prosím, majte na pamäti, že automatizované preklady môžu obsahovať chyby alebo nepresnosti. Originálny dokument v jeho pôvodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny preklad od človeka. Nezodpovedáme za žiadne nedorozumenia alebo nesprávne výklady vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->