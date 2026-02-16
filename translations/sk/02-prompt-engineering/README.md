# Modul 02: Prompt Engineering s GPT-5.2

## Obsah

- [Čo sa naučíte](../../../02-prompt-engineering)
- [Predpoklady](../../../02-prompt-engineering)
- [Pochopenie Prompt Engineering](../../../02-prompt-engineering)
- [Základy Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Pokročilé vzory](../../../02-prompt-engineering)
- [Použitie existujúcich Azure zdrojov](../../../02-prompt-engineering)
- [Snímky obrazovky aplikácie](../../../02-prompt-engineering)
- [Preskúmanie vzorov](../../../02-prompt-engineering)
  - [Nízka verzus vysoká angažovanosť](../../../02-prompt-engineering)
  - [Vykonávanie úloh (úvody k nástrojom)](../../../02-prompt-engineering)
  - [Sebareflexívny kód](../../../02-prompt-engineering)
  - [Štruktúrovaná analýza](../../../02-prompt-engineering)
  - [Viackolové rozhovory](../../../02-prompt-engineering)
  - [Postupné uvažovanie](../../../02-prompt-engineering)
  - [Obmedzený výstup](../../../02-prompt-engineering)
- [Čo sa naozaj učíte](../../../02-prompt-engineering)
- [Ďalšie kroky](../../../02-prompt-engineering)

## Čo sa naučíte

<img src="../../../translated_images/sk/what-youll-learn.c68269ac048503b2.webp" alt="Čo sa naučíte" width="800"/>

V predchádzajúcom module ste videli, ako pamäť umožňuje konverzačné AI a používali ste GitHub Models pre základné interakcie. Teraz sa zameriame na to, ako klásť otázky — samotné promptovanie — pomocou Azure OpenAI a GPT-5.2. Spôsob, akým štruktúrujete svoje prompty, výrazne ovplyvňuje kvalitu odpovedí, ktoré dostanete. Začíname prehľadom základných techník promptovania, následne prejdeme k ôsmim pokročilým vzorom, ktoré naplno využívajú schopnosti GPT-5.2.

Použijeme GPT-5.2, pretože zavádza riadenie uvažovania – môžete modelu povedať, koľko premýšľania má vykonať pred odpoveďou. To robí rôzne stratégie promptovania zjavnejšími a pomáha vám pochopiť, kedy použiť ktorý prístup. Tiež využijeme menej obmedzení frekvencie pre GPT-5.2 v Azure v porovnaní s GitHub Models.

## Predpoklady

- Dokončený Modul 01 (nasadené Azure OpenAI zdroje)
- Súbor `.env` v koreňovom adresári so prihlasovacími údajmi Azure (vytvorený príkazom `azd up` v Module 01)

> **Poznámka:** Ak ste neukončili Modul 01, najprv postupujte podľa tam uvedených pokynov na nasadenie.

## Pochopenie Prompt Engineering

<img src="../../../translated_images/sk/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Čo je Prompt Engineering?" width="800"/>

Prompt engineering sa zaoberá navrhovaním vstupného textu, ktorý vám konzistentne prinesie požadované výsledky. Nejde len o kladenie otázok – ide o štruktúrovanie požiadaviek tak, aby model presne pochopil, čo chcete a ako to má doručiť.

Predstavte si to ako dávanie inštrukcií kolegovi. „Oprav chybu“ je nešpecifické. „Oprav výnimku null pointer v UserService.java na riadku 45 pridaním kontroly na null“ je konkrétne. Jazykové modely fungujú rovnako – špecificita a štruktúra majú význam.

<img src="../../../translated_images/sk/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Ako zapadá LangChain4j" width="800"/>

LangChain4j poskytuje infraštruktúru — pripojenia k modelu, pamäť a typy správ — zatiaľ čo promptové vzory sú iba starostlivo štruktúrovaný text, ktorý posielate cez túto infraštruktúru. Kľúčové stavebné kamene sú `SystemMessage` (ktorá nastavuje správanie a rolu AI) a `UserMessage` (ktorá nesie vašu skutočnú požiadavku).

## Základy Prompt Engineering

<img src="../../../translated_images/sk/five-patterns-overview.160f35045ffd2a94.webp" alt="Prehľad piatich vzorov promptovania" width="800"/>

Predtým, ako sa pustíme do pokročilých vzorov v tomto module, pozrime sa na päť základných techník promptovania. Sú to stavebné kamene, ktoré by mal poznať každý prompt engineer. Ak ste už prešli [rýchly štart modulom](../00-quick-start/README.md#2-prompt-patterns), videli ste ich v praxi — tu je konceptuálny rámec za nimi.

### Zero-Shot Prompting

Najjednoduchší prístup: dajte modelu priamu inštrukciu bez príkladov. Model sa spolieha úplne na svoje trénovanie, aby pochopil a vykonal úlohu. Funguje to dobre pri priamych požiadavkách, kde je očakávané správanie zjavné.

<img src="../../../translated_images/sk/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Priama inštrukcia bez príkladov — model vyvodí úlohu len z inštrukcie*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odpoveď: "Pozitívne"
```

**Kedy použiť:** Jednoduché klasifikácie, priame otázky, preklady alebo akúkoľvek úlohu, ktorú môže model zvládnuť bez ďalších pokynov.

### Few-Shot Prompting

Poskytnite príklady, ktoré demonštrujú vzor, podľa ktorého má model postupovať. Model sa naučí očakávaný formát vstupu a výstupu z vašich príkladov a aplikuje ho na nové vstupy. Výrazne to zlepšuje konzistenciu pri úlohách, kde nie je očakávaný formát alebo správanie zjavné.

<img src="../../../translated_images/sk/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Učenie sa z príkladov — model identifikuje vzor a použije ho na nové vstupy*

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

**Kedy použiť:** Prispôsobené klasifikácie, konzistentné formátovanie, doménovo špecifické úlohy, alebo keď sú výsledky zero-shot nekonzistentné.

### Chain of Thought

Požiadajte model, aby ukázal svoje uvažovanie krok za krokom. Namiesto okamžitej odpovede model rozkladá problém a pracuje s každou jeho časťou explicitne. Zlepšuje to presnosť pre matematické, logické a viacstupňové uvažovacie úlohy.

<img src="../../../translated_images/sk/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Postupné uvažovanie — rozklad komplexných problémov na explicitné logické kroky*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model ukazuje: 15 - 8 = 7, potom 7 + 12 = 19 jabĺk
```

**Kedy použiť:** Matematické problémy, logické hádanky, ladenie chýb alebo akúkoľvek úlohu, kde zobrazenie procesu uvažovania zvyšuje presnosť a dôveru.

### Role-Based Prompting

Nastavte AI personu alebo rolu pred položením otázky. Poskytuje to kontext, ktorý formuje tón, hĺbku a zameranie odpovede. „Softvérový architekt“ dáva iné rady než „junior developer“ alebo „bezpečnostný audítor“.

<img src="../../../translated_images/sk/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Nastavenie kontextu a persony — tá istá otázka dostane odlišnú odpoveď podľa priradenej role*

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

**Kedy použiť:** Kódové revízie, doučovanie, doménovo špecifická analýza alebo ak potrebujete odpovede prispôsobené úrovni odbornosti či perspektíve.

### Prompt Templates

Vytvorte opakovane použiteľné prompty s premennými zástupcami. Namiesto písania nového promptu zakaždým definujte šablónu raz a doplňujte rozličné hodnoty. Trieda `PromptTemplate` v LangChain4j to uľahčuje pomocou syntaxe `{{variable}}`.

<img src="../../../translated_images/sk/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Opakovane použiteľné prompty s premennými zástupcami — jedna šablóna, mnoho použitia*

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

**Kedy použiť:** Opakované dotazy s rôznymi vstupmi, hromadné spracovanie, tvorba opakovane použiteľných AI pracovných tokov alebo v scenároch, kde sa štruktúra promptu nemení, ale údaje áno.

---

Tieto päť základov vám poskytujú pevný nástrojový súbor pre väčšinu promptovacích úloh. Zvyšok tohto modulu na nich staví s **ošmi pokročilými vzormi**, ktoré využívajú riadenie uvažovania GPT-5.2, sebehodnotenie a štruktúrované výstupy.

## Pokročilé vzory

Po zvládnutí základov prejdeme k ôsmim pokročilým vzorom, ktoré robia tento modul jedinečným. Nie všetky problémy vyžadujú rovnaký prístup. Niektoré otázky vyžadujú rýchle odpovede, iné hlboké premýšľanie. Niektoré vyžadujú viditeľné uvažovanie, iné len výsledky. Každý z nasledujúcich vzorov je optimalizovaný pre iný scenár — a riadenie uvažovania GPT-5.2 zvýrazňuje tieto rozdiely ešte viac.

<img src="../../../translated_images/sk/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osem vzorov promptovania" width="800"/>

*Prehľad ôsmich vzorov prompt engineering a ich využitie*

<img src="../../../translated_images/sk/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Riadenie uvažovania s GPT-5.2" width="800"/>

*Riadenie uvažovania GPT-5.2 umožňuje špecifikovať, koľko má model premýšľať — od rýchlych priame odpovede po hlboké skúmanie*

<img src="../../../translated_images/sk/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Porovnanie úsilí uvažovania" width="800"/>

*Nízka angažovanosť (rýchle, priame) vs vysoká angažovanosť (dôkladné, preskúmavacie) prístupy k uvažovaniu*

**Nízka angažovanosť (Rýchlo a zamerane)** – Pre jednoduché otázky, kde chcete rýchle, priame odpovede. Model minimalizuje uvažovanie – max. 2 kroky. Použite pre výpočty, vyhľadávanie alebo priame otázky.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Preskúmajte s GitHub Copilot:** Otvorte [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) a opýtajte sa:
> - „Aký je rozdiel medzi nízkou a vysokou angažovanosťou v promptovacích vzoroch?“
> - „Ako pomáhajú XML tagy v promptoch štruktúrovať odpoveď AI?“
> - „Kedy mám použiť sebareflexné vzory vs priame inštrukcie?“

**Vysoká angažovanosť (Hlboké a dôkladné)** – Pre komplexné problémy, kde chcete komplexnú analýzu. Model dôkladne skúma a ukazuje detailné uvažovanie. Použite pre návrhy systémov, architektonické rozhodnutia alebo zložité výskumné úlohy.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Vykonávanie úloh (postupný postup)** – Pre viacstupňové pracovné toky. Model poskytuje plán dopredu, rozpráva každý krok počas práce a potom zhrnie výsledok. Použite pre migrácie, implementácie alebo akýkoľvek viacstupňový proces.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought promptovanie explicitne žiada model, aby ukázal svoj proces uvažovania, čím sa zlepšuje presnosť pri zložitých úlohách. Postupný rozklad pomáha ľuďom aj AI pochopiť logiku.

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Opýtajte sa na tento vzor:
> - „Ako by som prispôsobil vzor vykonávania úloh pre dlhodobé operácie?“
> - „Aké sú najlepšie praktiky pre štruktúrovanie úvodov k nástrojom v produkčných aplikáciách?“
> - „Ako zachytiť a zobraziť priebežné aktualizácie pokroku v používateľskom rozhraní?“

<img src="../../../translated_images/sk/task-execution-pattern.9da3967750ab5c1e.webp" alt="Vzor vykonávania úloh" width="800"/>

*Plánuj → Vykonaj → Zhrň pracovný postup pre viacstupňové úlohy*

**Sebareflexívny kód** – Pre generovanie kódu kvality produkcie. Model generuje kód, kontroluje ho podľa kritérií kvality a iteratívne ho zlepšuje. Použite pri tvorbe nových funkcií alebo služieb.

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

<img src="../../../translated_images/sk/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cyklus sebareflexie" width="800"/>

*Iteratívna slučka zlepšovania – generuj, vyhodnoť, identifikuj problémy, zlepšuj, opakuj*

**Štruktúrovaná analýza** – Pre konzistentné hodnotenie. Model prehodnocuje kód pomocou pevného rámca (správnosť, praktiky, výkon, bezpečnosť). Použite pre kódové revízie alebo hodnotenia kvality.

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
> - „Ako môžem prispôsobiť rámec analýzy pre rôzne typy kódových revízií?“
> - „Aký je najlepší spôsob, ako programovo spracovať a reagovať na štruktúrovaný výstup?“
> - „Ako zabezpečiť konzistentnú úroveň závažnosti naprieč rôznymi revíznymi reláciami?“

<img src="../../../translated_images/sk/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Vzor štruktúrovanej analýzy" width="800"/>

*Štvorkategóriový rámec pre konzistentné kódové revízie so závažnostnými úrovňami*

**Viackolové rozhovory** – Pre konverzácie vyžadujúce kontext. Model si pamätá predchádzajúce správy a nadväzuje na ne. Použite pre interaktívne pomocné relácie alebo zložité Q&A.

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

*Ako sa kontext konverzácie kumuluje počas viacerých kôl až do dosiahnutia limitu tokenov*

**Postupné uvažovanie** – Pre problémy, ktoré vyžadujú viditeľnú logiku. Model ukazuje explicitné uvažovanie pre každý krok. Použite pri matematických problémoch, logických hádankách alebo ak potrebujete pochopiť proces myslenia.

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

*Rozklad problémov na explicitné logické kroky*

**Obmedzený výstup** – Pre odpovede so špecifickými požiadavkami na formát. Model prísne dodržiava pravidlá o formáte a dĺžke. Použite pre zhrnutia alebo ak potrebujete presnú štruktúru výstupu.

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

Skontrolujte, či súbor `.env` existuje v koreňovom adresári s prihlasovacími údajmi Azure (vytvorený počas Modulu 01):
```bash
cat ../.env  # Malo by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spustenie aplikácie:**

> **Poznámka:** Ak ste už spustili všetky aplikácie pomocou `./start-all.sh` z Modulu 01, tento modul už beží na porte 8083. Môžete preskočiť nižšie uvedené príkazy na spustenie a prejsť priamo na http://localhost:8083.

**Možnosť 1: Použitie Spring Boot Dashboard (Odporúčané pre používateľov VS Code)**

Vývojové kontajner zahŕňa rozšírenie Spring Boot Dashboard, ktoré poskytuje vizuálne rozhranie na správu všetkých Spring Boot aplikácií. Nájdete ho v paneli Aktivít na ľavej strane VS Code (ikonka Spring Boot).
Z panela Spring Boot Dashboard môžete:
- Vidieť všetky dostupné Spring Boot aplikácie v pracovnom priestore
- Spustiť/zastaviť aplikácie jedným kliknutím
- Pozerať si logy aplikácie v reálnom čase
- Monitorovať stav aplikácie

Jednoducho kliknite na tlačidlo spustenia vedľa "prompt-engineering" pre spustenie tohto modulu alebo spustite všetky moduly naraz.

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

Oba skripty automaticky načítajú premenné prostredia zo súboru `.env` v koreňovom adresári a zostavia JAR súbory, ak neexistujú.

> **Poznámka:** Ak si prajete zostaviť všetky moduly manuálne pred spustením:
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
./stop.sh  # Tento modul iba
# Alebo
cd .. && ./stop-all.sh  # Všetky moduly
```

**PowerShell:**
```powershell
.\stop.ps1  # Iba tento modul
# Alebo
cd ..; .\stop-all.ps1  # Všetky moduly
```

## Snímky obrazovky aplikácie

<img src="../../../translated_images/sk/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Hlavný panel zobrazujúci všetkých 8 vzorov prompt engineering s ich charakteristikami a prípadmi použitia*

## Preskúmanie vzorov

Webové rozhranie vám umožňuje experimentovať s rôznymi stratégiami promptovania. Každý vzor rieši iné problémy – vyskúšajte ich, aby ste videli, kedy ktorý prístup vyniká.

### Nízka vs vysoká ochota (Eagerness)

Opýtajte sa jednoduchú otázku, napríklad "Čo je 15 % z 200?" s nízkou ochotou. Dostanete okamžitú, priamu odpoveď. Teraz opýtajte sa niečo komplexné, napríklad "Navrhnite stratégiu cachovania pre API s vysokou návštevnosťou" s vysokou ochotou. Sledujte, ako model spomalí a poskytne podrobné odôvodnenie. Rovnaký model, rovnaká štruktúra otázky – ale prompt mu hovorí, koľko má rozmýšľať.

<img src="../../../translated_images/sk/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Rýchly výpočet s minimálnym rozumením*

<img src="../../../translated_images/sk/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Komplexná stratégia cachovania (2.8MB)*

### Vykonávanie úloh (Predslovné nástroje)

Viacstupňové pracovné postupy profitujú z vopred plánovania a komentovania priebehu. Model popisuje, čo urobí, komentuje každý krok a potom zhrnie výsledky.

<img src="../../../translated_images/sk/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Vytvorenie REST endpointu s komentovaním krok za krokom (3.9MB)*

### Sebareflexívny kód

Vyskúšajte "Vytvoriť službu validácie emailov". Namiesto toho, aby model len generoval kód a skončil, generuje, hodnotí podľa kvalitatívnych kritérií, identifikuje slabiny a zlepšuje. Uvidíte, ako iteruje, až kým kód nespĺňa produkčné štandardy.

<img src="../../../translated_images/sk/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Kompletná služba validácie emailov (5.2MB)*

### Štruktúrovaná analýza

Code review vyžaduje konzistentné hodnotiace kritériá. Model analyzuje kód podľa pevných kategórií (správnosť, postupy, výkon, bezpečnosť) s rôznymi úrovňami závažnosti.

<img src="../../../translated_images/sk/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Code review na báze rámca*

### Viackolové rozhovory (Multi-Turn Chat)

Opýtajte sa "Čo je Spring Boot?" a potom hneď pokračujte "Ukáž mi príklad". Model si pamätá vašu prvú otázku a dáva vám príklad ohľadom Spring Boot špecificky. Bez pamäti by bola druhá otázka príliš nejasná.

<img src="../../../translated_images/sk/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Zachovanie kontextu cez viaceré otázky*

### Krok za krokom rozumovanie

Vyberte si matematickú úlohu a vyskúšajte ju s krokom za krokom rozumovaním a s nízkou ochotou. Nízka ochota vám len rýchlo dá odpoveď – rýchlo, ale nejasne. Krok za krokom vás prevedie každým výpočtom a rozhodnutím.

<img src="../../../translated_images/sk/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matematický problém s explicitnými krokmi*

### Obmedzený výstup

Keď potrebujete špecifické formáty alebo počet slov, tento vzor vynucuje prísne dodržiavanie. Vyskúšajte vygenerovať zhrnutie s presne 100 slovami vo forme odrážok.

<img src="../../../translated_images/sk/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Zhrnutie strojového učenia s kontrolou formátu*

## Čo sa naozaj učíte

**Rozumové úsilie mení všetko**

GPT-5.2 vám umožňuje kontrolovať výpočtové úsilie cez vaše prompty. Nízke úsilie znamená rýchle odpovede s minimálnym skúmaním. Vysoké úsilie znamená, že model si vezme čas na hlboké premýšľanie. Učíte sa prispôsobiť úsilie zložitosti úlohy – nestrácajte čas na jednoduché otázky, ale ani neponáhľajte komplexné rozhodnutia.

**Štruktúra vedie správanie**

Všimli ste si XML značky v promptoch? Nie sú ozdobné. Modely spoľahlivejšie dodržiavajú štruktúrované inštrukcie než voľný text. Keď potrebujete viacstupňové procesy alebo zložitú logiku, štruktúra pomáha modelu sledovať, kde sa nachádza a čo príde ďalej.

<img src="../../../translated_images/sk/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatómia dobre štruktúrovaného promptu s jasnými časťami a XML-štýlom organizácie*

**Kvalita cez sebaohodnotenie**

Vzor sebazrkadlenia pracuje tak, že robí kvalitatívne kritériá explicitné. Namiesto toho, aby ste dúfali, že model "urobí to správne", presne mu poviete, čo "správne" znamená: správna logika, správa chýb, výkon, bezpečnosť. Model potom môže hodnotiť svoj vlastný výstup a zlepšovať sa. Toto premieňa generovanie kódu z lotérie na proces.

**Kontext je obmedzený**

Viackolové rozhovory fungujú tak, že každá požiadavka obsahuje históriu správ. Ale je tu limit – každý model má maximálny počet tokenov. Ako rozhovory rastú, budete potrebovať stratégie, ako udržať relevantný kontext bez prekročenia limitu. Tento modul ukazuje, ako funguje pamäť; neskôr sa naučíte, kedy sumarizovať, kedy zabudnúť a kedy vyhľadávať.

## Ďalšie kroky

**Ďalší modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigácia:** [← Predchádzajúci: Modul 01 - Úvod](../01-introduction/README.md) | [Späť na hlavné](../README.md) | [Ďalší: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zrieknutie sa zodpovednosti**:
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, berte prosím na vedomie, že automatizované preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho rodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre dôležité informácie sa odporúča profesionálny preklad od človeka. Nie sme zodpovední za akékoľvek nedorozumenia alebo nesprávne interpretácie vzniknuté použitím tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->