# Modul 02: Inžinierstvo promptov s GPT-5.2

## Obsah

- [Čo sa naučíte](../../../02-prompt-engineering)
- [Predpoklady](../../../02-prompt-engineering)
- [Pochopenie inžinierstva promptov](../../../02-prompt-engineering)
- [Základy inžinierstva promptov](../../../02-prompt-engineering)
  - [Zero-Shot promptovanie](../../../02-prompt-engineering)
  - [Few-Shot promptovanie](../../../02-prompt-engineering)
  - [Reťazec myšlienok](../../../02-prompt-engineering)
  - [Promptovanie založené na roli](../../../02-prompt-engineering)
  - [Šablóny promptov](../../../02-prompt-engineering)
- [Pokročilé vzory](../../../02-prompt-engineering)
- [Použitie existujúcich Azure zdrojov](../../../02-prompt-engineering)
- [Snímky obrazovky aplikácie](../../../02-prompt-engineering)
- [Preskúmanie vzorov](../../../02-prompt-engineering)
  - [Nízka vs Vysoká ochota](../../../02-prompt-engineering)
  - [Vykonávanie úloh (preambuly nástrojov)](../../../02-prompt-engineering)
  - [Kód so sebazrkadlením](../../../02-prompt-engineering)
  - [Štruktúrovaná analýza](../../../02-prompt-engineering)
  - [Viackolová konverzácia](../../../02-prompt-engineering)
  - [Krok za krokom uvažovanie](../../../02-prompt-engineering)
  - [Obmedzený výstup](../../../02-prompt-engineering)
- [Čo sa skutočne naučíte](../../../02-prompt-engineering)
- [Ďalšie kroky](../../../02-prompt-engineering)

## Čo sa naučíte

<img src="../../../translated_images/sk/what-youll-learn.c68269ac048503b2.webp" alt="Čo sa naučíte" width="800"/>

V predchádzajúcom module ste videli, ako pamäť umožňuje konverzačné AI a používali ste GitHub Modely na základné interakcie. Teraz sa zameriame na to, ako klásť otázky — samotné prompty — pomocou Azure OpenAI GPT-5.2. Spôsob, akým štruktúrujete svoje prompty, dramaticky ovplyvňuje kvalitu odpovedí, ktoré dostanete. Začneme prehľadom základných techník promptovania a potom prejdeme k ôsmim pokročilým vzorom, ktoré plne využívajú schopnosti GPT-5.2.

Použijeme GPT-5.2, pretože zavádza riadenie uvažovania - môžete modelu povedať, koľko premýšľania má vykonať pred odpoveďou. To robí rôzne stratégie promptovania jasnejšie a pomáha vám pochopiť, kedy použiť ktorý prístup. Tiež budeme mať úžitok z menších obmedzení rýchlosti na GPT-5.2 oproti GitHub Modelom v Azure.

## Predpoklady

- Dokončený Modul 01 (nasadené Azure OpenAI zdroje)
- Súbor `.env` v koreňovom adresári so Azure povereniami (vytvorený príkazom `azd up` v Module 01)

> **Poznámka:** Ak ste Modul 01 ešte nedokončili, najskôr postupujte podľa pokynov na nasadenie tam.

## Pochopenie inžinierstva promptov

<img src="../../../translated_images/sk/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Čo je inžinierstvo promptov?" width="800"/>

Inžinierstvo promptov je o navrhovaní vstupného textu, ktorý vám konzistentne prináša požadované výsledky. Nejde len o kladenie otázok — ide o štruktúrovanie požiadaviek tak, aby model presne rozumel, čo chcete a ako to doručiť.

Predstavte si to ako dávanie inštrukcií kolegovi. „Oprav chybu“ je nejasné. „Oprav null pointer exception v UserService.java na riadku 45 pridaním kontroly na null“ je konkrétne. Jazykové modely fungujú rovnako — dôležitá je špecifickosť a štruktúra.

<img src="../../../translated_images/sk/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Ako LangChain4j zapadá" width="800"/>

LangChain4j poskytuje infraštruktúru — prepojenia modelov, pamäť a typy správ — zatiaľ čo vzory promptov sú len starostlivo štruktúrovaný text, ktorý posielate cez túto infraštruktúru. Kľúčovými stavebnými prvkami sú `SystemMessage` (nastavuje správanie a rolu AI) a `UserMessage` (ktorý nesie vašu aktuálnu požiadavku).

## Základy inžinierstva promptov

<img src="../../../translated_images/sk/five-patterns-overview.160f35045ffd2a94.webp" alt="Prehľad piatich vzorov inžinierstva promptov" width="800"/>

Predtým, než sa pustíme do pokročilých vzorov v tomto module, poďme si zopakovať päť základných techník promptovania. Sú to stavebné bloky, ktoré by mal poznať každý inžinier promptov. Ak ste už pracovali na [rýchlom štarte](../00-quick-start/README.md#2-prompt-patterns), už ste ich videli v akcii — tu je koncepčný rámec za nimi.

### Zero-Shot promptovanie

Najjednoduchší prístup: dajte modelu priamu inštrukciu bez príkladov. Model sa spolieha výlučne na svoje tréningové dáta, aby pochopil a vykonal úlohu. Funguje to dobre pri jednoduchých žiadostiach, kde je očakávané správanie zjavné.

<img src="../../../translated_images/sk/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot promptovanie" width="800"/>

*Priama inštrukcia bez príkladov — model vyvodzuje úlohu iba z inštrukcie*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odpoveď: "Pozitívne"
```

**Kedy použiť:** Jednoduché klasifikácie, priame otázky, preklady alebo akúkoľvek úlohu, ktorú model zvládne bez ďalšieho usmernenia.

### Few-Shot promptovanie

Poskytnite príklady, ktoré demonštrujú vzor, ktorý chcete, aby model nasledoval. Model sa naučí očakávaný vstupno-výstupný formát z vašich príkladov a aplikuje ho na nové vstupy. Toto výrazne zlepšuje konzistenciu pri úlohách, kde požadovaný formát alebo správanie nie sú zrejmé.

<img src="../../../translated_images/sk/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot promptovanie" width="800"/>

*Učenie sa z príkladov — model rozpoznáva vzor a aplikuje ho na nové vstupy*

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

### Reťazec myšlienok

Požiadajte model, aby ukázal svoj proces uvažovania krok za krokom. Namiesto toho, aby skočil rovno na odpoveď, model rozloží problém a explicitne prejde každou časťou. Toto zlepšuje presnosť v matematike, logike a úlohách s viacstupňovým uvažovaním.

<img src="../../../translated_images/sk/chain-of-thought.5cff6630e2657e2a.webp" alt="Reťazec myšlienok promptovanie" width="800"/>

*Krok za krokom uvažovanie — rozkladanie komplexných problémov na explicitné logické kroky*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model ukazuje: 15 - 8 = 7, potom 7 + 12 = 19 jabĺk
```

**Kedy použiť:** Matematické úlohy, logické hádanky, ladenie chýb, alebo akákoľvek úloha, kde zobrazenie procesu uvažovania zlepšuje presnosť a dôveru.

### Promptovanie založené na roli

Nastavte AI personu alebo rolu pred položením otázky. Toto poskytuje kontext, ktorý formuje tón, hĺbku a zameranie odpovede. „Softvérový architekt“ dáva iné rady než „junior vývojár“ alebo „audit bezpečnosti“.

<img src="../../../translated_images/sk/role-based-prompting.a806e1a73de6e3a4.webp" alt="Promptovanie založené na roli" width="800"/>

*Nastavenie kontextu a persony — tá istá otázka dostane inú odpoveď v závislosti od priradenej roly*

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

**Kedy použiť:** Kontroly kódu, doučovanie, doménovo špecifické analýzy alebo keď potrebujete odpovede prispôsobené špecializácii alebo pohľadu.

### Šablóny promptov

Vytvorte znovu použiteľné prompty s premennými zástupcami. Namiesto písania nového promptu zakaždým definujte šablónu raz a do nej dosadzujte rôzne hodnoty. Trieda `PromptTemplate` od LangChain4j to uľahčuje syntaxou `{{variable}}`.

<img src="../../../translated_images/sk/prompt-templates.14bfc37d45f1a933.webp" alt="Šablóny promptov" width="800"/>

*Znovu použiteľné prompty s premennými zástupcami — jedna šablóna, mnoho použitia*

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

**Kedy použiť:** Opakované dotazy s rôznymi vstupmi, dávkové spracovanie, tvorba znovu použitých AI workflowov alebo v akýchkoľvek scenároch, kde sa štruktúra promptu nemení, ale dáta áno.

---

Tieto päť základov vám dáva pevný nástrojový súbor pre väčšinu úloh promptovania. Zvyšok tohto modulu na nich stavia pomocou **osem pokročilých vzorov**, ktoré využívajú riadenie uvažovania GPT-5.2, seba hodnotenie a schopnosti štruktúrovaného výstupu.

## Pokročilé vzory

Po tom, čo máme pokryté základy, poďme k ôsmim pokročilým vzorom, ktoré robia tento modul výnimočným. Nie všetky problémy vyžadujú rovnaký prístup. Niektoré otázky potrebujú rýchle odpovede, iné hlboké premýšľanie. Niektoré potrebujú viditeľné uvažovanie, iné len výsledky. Každý vzor nižšie je optimalizovaný pre inú situáciu — a riadenie uvažovania GPT-5.2 robí rozdiely ešte výraznejšími.

<img src="../../../translated_images/sk/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osem vzorov promptovania" width="800"/>

*Prehľad ôsmich vzorov inžinierstva promptov a ich použitia*

<img src="../../../translated_images/sk/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Riadenie uvažovania s GPT-5.2" width="800"/>

*Riadenie uvažovania GPT-5.2 vám umožňuje určiť, koľko premýšľania má model urobiť — od rýchlych priamych odpovedí po hlbokú exploráciu*

<img src="../../../translated_images/sk/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Porovnanie úsilia pri uvažovaní" width="800"/>

*Nízka ochota (rýchle, priame) vs Vysoká ochota (dôkladné, prieskumné) prístupy k uvažovaniu*

**Nízka ochota (rýchle a zamerané)** - Pre jednoduché otázky, kde chcete rýchle, priame odpovede. Model robí minimálne uvažovanie - maximálne 2 kroky. Použite to na výpočty, vyhľadávanie alebo jednoduché otázky.

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

> 💡 **Preskúmajte s GitHub Copilot:** Otvorte [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) a spýtajte sa:
> - "Aký je rozdiel medzi promptovými vzormi s nízkou a vysokou ochotou?"
> - "Ako pomáhajú XML tagy v promptoch štruktúrovať odpoveď AI?"
> - "Kedy použiť vzory so sebazrkadlením vs priamu inštrukciu?"

**Vysoká ochota (hlboké a dôkladné)** - Pre zložité problémy, kde chcete komplexnú analýzu. Model dôkladne preskúma a ukáže detailné uvažovanie. Použite to pre systémový dizajn, architektonické rozhodnutia alebo komplexný výskum.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Vykonávanie úloh (postup krok za krokom)** - Pre workflowy s viacerými krokmi. Model poskytne plán vopred, popisuje každý krok, ktorý vykonáva, a nakoniec dá sumarizáciu. Použite to pre migrácie, implementácie alebo akýkoľvek viacstupňový proces.

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

Chain-of-Thought promptovanie výslovne žiada model, aby ukázal svoj proces uvažovania, čím zlepšuje presnosť pri zložitých úlohách. Postupné rozdelenie pomáha pochopiť logiku ľuďom aj AI.

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Spýtajte sa na tento vzor:
> - "Ako by som prispôsobil vzor vykonávania úloh pre dlhé operácie?"
> - "Aké sú najlepšie praktiky pre štruktúru preambúl nástrojov v produkčných aplikáciách?"
> - "Ako zachytiť a zobraziť medzistav progresu v používateľskom rozhraní?"

<img src="../../../translated_images/sk/task-execution-pattern.9da3967750ab5c1e.webp" alt="Vzor vykonávania úloh" width="800"/>

*Plán → Vykonanie → Sumarizácia workflow pre multi-krokové úlohy*

**Kód so sebazrkadlením** - Pre generovanie kódu produkčnej kvality. Model generuje kód podľa výrobnych štandardov s riadnym spracovaním chýb. Použite to pri tvorbe nových funkcií alebo služieb.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sk/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cyklus sebazrkadlenia" width="800"/>

*Iteratívna slučka zlepšovania - generovať, vyhodnotiť, identifikovať problémy, zlepšiť, opakovať*

**Štruktúrovaná analýza** - Pre konzistentné hodnotenie. Model kontroluje kód pomocou pevného rámca (správnosť, prax, výkon, bezpečnosť, udržateľnosť). Použite to pre code review alebo hodnotenia kvality.

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

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Spýtajte sa na štruktúrovanú analýzu:
> - "Ako prispôsobiť rámec analýzy pre rôzne typy code reviews?"
> - "Aký je najlepší spôsob programatického spracovania a použitia štruktúrovaného výstupu?"
> - "Ako zabezpečiť konzistentné úrovne závažnosti naprieč rôznymi kontrolnými reláciami?"

<img src="../../../translated_images/sk/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Vzor štruktúrovanej analýzy" width="800"/>

*Rámec pre konzistentné kontroly kódu s úrovňami závažnosti*

**Viackolová konverzácia** - Pre rozhovory, ktoré potrebujú kontext. Model si pamätá predchádzajúce správy a nadväzuje na ne. Použite to pre interaktívne pomocné sedenia alebo komplexné Q&A.

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

*Ako sa konverzačný kontext kumuluje počas viacerých kôl až do dosiahnutia limitu tokenov*

**Krok za krokom uvažovanie** - Pre problémy vyžadujúce viditeľnú logiku. Model ukazuje explicitný dôvod pre každý krok. Použite to pre matematické problémy, logické hádanky, alebo keď potrebujete pochopiť proces uvažovania.

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

**Obmedzený výstup** - Pre odpovede s konkrétnymi požiadavkami na formát. Model striktne dodržiava pravidlá formátu a dĺžky. Použite to na zhrnutia alebo keď potrebujete presnú štruktúru výstupu.

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

*Vynucovanie špecifických požiadaviek na formát, dĺžku a štruktúru*

## Použitie existujúcich Azure zdrojov

**Overenie nasadenia:**

Skontrolujte, či súbor `.env` existuje v koreňovom adresári so Azure povereniami (vytvorený počas Modulu 01):
```bash
cat ../.env  # Mala by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spustenie aplikácie:**

> **Poznámka:** Ak ste už spustili všetky aplikácie pomocou `./start-all.sh` z Modulu 01, tento modul už beží na porte 8083. Môžete vynechať spúšťacie príkazy nižšie a ísť priamo na http://localhost:8083.

**Možnosť 1: Použitie Spring Boot Dashboard (odporúčané pre používateľov VS Code)**

Dev container obsahuje rozšírenie Spring Boot Dashboard, ktoré poskytuje vizuálne rozhranie na správu všetkých Spring Boot aplikácií. Nájdete ho v Activity Bar na ľavej strane VS Code (hľadajte ikonu Spring Boot).
Zo Spring Boot Dashboard môžete:
- Vidieť všetky dostupné Spring Boot aplikácie v pracovnom priestore
- Spustiť/zastaviť aplikácie jedným kliknutím
- Zobraziť logy aplikácie v reálnom čase
- Monitorovať stav aplikácie

Jednoducho kliknite na tlačidlo prehrávania vedľa "prompt-engineering" pre spustenie tohto modulu, alebo spustite všetky moduly naraz.

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

Obidva skripty automaticky načítajú premenné prostredia z koreňového súboru `.env` a zostavia JAR-y, ak neexistujú.

> **Poznámka:** Ak chcete pred spustením manuálne zostaviť všetky moduly:
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

Otvorte http://localhost:8083 vo vašom prehliadači.

**Na zastavenie:**

**Bash:**
```bash
./stop.sh  # Tento modul iba
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

*Hlavný dashboard zobrazujúci všetky 8 vzorov prompt engineering s ich charakteristikami a prípadmi použitia*

## Preskúmanie vzorov

Webové rozhranie vám umožňuje experimentovať s rôznymi stratégiami promptovania. Každý vzor rieši iné problémy — vyskúšajte ich, aby ste videli, kedy ktorý prístup vyniká.

### Nízka vs vysoká ochota

Opýtajte sa jednoduchú otázku ako "Čo je 15% zo 200?" pomocou Nízkej ochoty. Dostanete okamžitú, priamu odpoveď. Teraz sa spýtajte niečo zložité ako "Navrhnite stratégiu cacheovania pre vysoko zaťažené API" pomocou Vysokej ochoty. Sledujte, ako model spomalí a poskytne detailné zdôvodnenie. Rovnaký model, rovnaká štruktúra otázky – ale prompt mu hovorí, koľko má myslieť.

<img src="../../../translated_images/sk/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Rýchly výpočet s minimálnym zdôvodnením*

<img src="../../../translated_images/sk/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Komplexná stratégia cacheovania (2,8MB)*

### Vykonávanie úloh (Predslovné nástroje)

Viackrokové pracovné postupy profitujú z vopred plánovania a komentovania priebehu. Model načrtne, čo urobí, komentuje každý krok a potom zhrnie výsledky.

<img src="../../../translated_images/sk/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Vytvorenie REST endpointu s komentovaním krok po kroku (3,9MB)*

### Sebareflexívny kód

Skúste "Vytvorte službu na overenie e-mailu". Namiesto generovania kódu a zastavenia, model generuje, hodnotí kvalitu, identifikuje slabiny a zlepšuje. Uvidíte, ako iteruje, kým kód nespĺňa produkčné štandardy.

<img src="../../../translated_images/sk/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Kompletná služba na overenie e-mailu (5,2MB)*

### Štruktúrovaná analýza

Code review vyžaduje konzistentné hodnotiace rámce. Model analyzuje kód pomocou pevných kategórií (správnosť, praktiky, výkon, bezpečnosť) so závažnostnými úrovňami.

<img src="../../../translated_images/sk/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Code review založené na frameworku*

### Viackolový chat

Opýtajte sa "Čo je Spring Boot?" a potom hneď pokračujte "Ukáž mi príklad". Model si pamätá vašu prvú otázku a poskytne vám konkrétny príklad Spring Boot. Bez pamäti by bola tá druhá otázka príliš nejasná.

<img src="../../../translated_images/sk/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Zachovanie kontextu naprieč otázkami*

### Zdôvodňovanie krok za krokom

Vyberte si matematickú úlohu a vyskúšajte ju s krokovým zdôvodňovaním a nízkou ochotou. Nízka ochota vám len dá odpoveď – rýchlo, ale nejasne. Krokové zdôvodňovanie ukazuje každý výpočet a rozhodnutie.

<img src="../../../translated_images/sk/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matematický problém s explicitnými krokmi*

### Obmedzený výstup

Keď potrebujete špecifické formáty alebo počet slov, tento vzor vynucuje prísne dodržiavanie. Skúste vygenerovať zhrnutie s presne 100 slovami v bodoch.

<img src="../../../translated_images/sk/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Strojové učenie s kontrolou formátu*

## Čo sa naozaj učíte

**Úsilie o zdôvodnenie mení všetko**

GPT-5.2 vám umožňuje ovládať výpočtové úsilie cez vaše promptovanie. Nízke úsilie znamená rýchle odpovede s minimálnym skúmaním. Vysoké úsilie znamená, že model si dá na čas a hlboko rozmýšľa. Učíte sa prispôsobiť úsilie komplexnosti úlohy – nestrácajte čas s jednoduchými otázkami, ale ani neponáhľajte zložité rozhodnutia.

**Štruktúra vedie správanie**

Vidíte XML značky v promptoch? Nie sú dekoráciou. Modely dôslednejšie sledujú štruktúrované pokyny než voľný text. Keď potrebujete viackrokové procesy alebo zložitú logiku, štruktúra pomáha modelu sledovať, kde sa nachádza a čo má nasledovať.

<img src="../../../translated_images/sk/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatómia dobre štruktúrovaného promptu s jasnými sekciami a organizáciou v štýle XML*

**Kvalita cez sebevaluáciu**

Sebareflexívne vzory fungujú tak, že robia kritériá kvality explicitnými. Namiesto dúfania, že model „urobí to správne“, presne mu povedzte, čo znamená „správne“: správna logika, spracovanie chýb, výkon, bezpečnosť. Model potom dokáže sám vyhodnotiť svoj výstup a zlepšiť sa. To premieňa generovanie kódu z lotérie na proces.

**Kontext je konečný**

Viackolové konverzácie fungujú tak, že pri každej požiadavke pridávajú históriu správ. Ale existuje limit – každý model má maximálny počet tokenov. Ako sa konverzácie zväčšujú, budete potrebovať stratégie na udržanie relevantného kontextu bez prekročenia limitu. Tento modul ukazuje, ako pamäť funguje; neskôr sa naučíte, kedy zhrnúť, kedy zabudnúť a kedy vyhľadať.

## Ďalšie kroky

**Ďalší modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigácia:** [← Predchádzajúci: Modul 01 - Úvod](../01-introduction/README.md) | [Späť na hlavný](../README.md) | [Ďalší: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vylúčenie zodpovednosti**:
Tento dokument bol preložený pomocou automatizovanej prekladateľskej služby AI [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, berte prosím na vedomie, že automatizované preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho rodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za akékoľvek nepochopenia alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->