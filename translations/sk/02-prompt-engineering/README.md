# Modul 02: Inžinierstvo promptov s GPT-5.2

## Obsah

- [Video prehliadka](../../../02-prompt-engineering)
- [Čo sa naučíte](../../../02-prompt-engineering)
- [Predpoklady](../../../02-prompt-engineering)
- [Pochopenie inžinierstva promptov](../../../02-prompt-engineering)
- [Základy inžinierstva promptov](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Pokročilé vzory](../../../02-prompt-engineering)
- [Použitie existujúcich zdrojov Azure](../../../02-prompt-engineering)
- [Snímky obrazovky aplikácie](../../../02-prompt-engineering)
- [Preskúmanie vzorov](../../../02-prompt-engineering)
  - [Nízka vs vysoká ochota](../../../02-prompt-engineering)
  - [Vykonávanie úloh (preambuly nástrojov)](../../../02-prompt-engineering)
  - [Seba-reflektujúci kód](../../../02-prompt-engineering)
  - [Štruktúrovaná analýza](../../../02-prompt-engineering)
  - [Viackolový chat](../../../02-prompt-engineering)
  - [Rozumovanie krok za krokom](../../../02-prompt-engineering)
  - [Obmedzený výstup](../../../02-prompt-engineering)
- [Čo sa naozaj učíte](../../../02-prompt-engineering)
- [Ďalšie kroky](../../../02-prompt-engineering)

## Video prehliadka

Pozrite si túto živú reláciu, ktorá vysvetľuje, ako začať s týmto modulom:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Čo sa naučíte

<img src="../../../translated_images/sk/what-youll-learn.c68269ac048503b2.webp" alt="Čo sa naučíte" width="800"/>

V predchádzajúcom module ste videli, ako pamäť umožňuje konverzačné AI a používali ste GitHub Models pre základné interakcie. Teraz sa zameriame na to, ako klásť otázky — samotné promptovanie — pomocou GPT-5.2 z Azure OpenAI. Spôsob, akým štruktúrujete svoje prompty, dramaticky ovplyvňuje kvalitu odpovedí, ktoré dostanete. Začneme prehľadom základných techník promptovania a následne prejdeme k ôsmim pokročilým vzorom, ktoré plne využívajú schopnosti GPT-5.2.

Použijeme GPT-5.2, pretože zavádza kontrolu rozumovania – môžete modelu povedať, koľko má premýšľať pred odpoveďou. To robí rôzne promptingové stratégie výraznejšími a pomáha vám pochopiť, kedy použiť ktorý prístup. Tiež využijeme výhodu nižších limitov rýchlosti Azure pre GPT-5.2 v porovnaní s GitHub Models.

## Predpoklady

- Dokončený Modul 01 (nasadené zdroje Azure OpenAI)
- Súbor `.env` v koreňovom adresári s Azure povereniami (vytvorený pomocou `azd up` v Module 01)

> **Poznámka:** Ak ste neabsolvovali Modul 01, najprv postupujte podľa návodov na jeho nasadenie.

## Pochopenie inžinierstva promptov

<img src="../../../translated_images/sk/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Čo je inžinierstvo promptov?" width="800"/>

Inžinierstvo promptov je o navrhovaní vstupného textu, ktorý vám konzistentne prinesie požadované výsledky. Nie je to len o kladení otázok – je to o štruktúrovaní požiadaviek tak, aby model presne pochopil, čo chcete a ako to dodať.

Predstavte si, že dávate inštrukcie kolegovi. „Oprav chybu“ je nejasné. „Oprav výnimku null pointer v UserService.java na riadku 45 pridaním kontroly null“ je konkrétne. Jazykové modely fungujú rovnako – dôležitá je špecificita a štruktúra.

<img src="../../../translated_images/sk/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Ako zapadá LangChain4j" width="800"/>

LangChain4j poskytuje infraštruktúru — pripojenia k modelom, pamäť a typy správ — zatiaľ čo promptingové vzory sú len starostlivo štruktúrovaný text, ktorý touto infraštruktúrou posielate. Kľúčovými stavebnými blokmi sú `SystemMessage` (ktorá nastavuje správanie a rolu AI) a `UserMessage` (ktorá nesie vašu skutočnú požiadavku).

## Základy inžinierstva promptov

<img src="../../../translated_images/sk/five-patterns-overview.160f35045ffd2a94.webp" alt="Prehľad piatich vzorov inžinierstva promptov" width="800"/>

Predtým, než sa pustíme do pokročilých vzorov v tomto module, poďme si zopakovať päť základných techník promptovania. Sú to stavebné bloky, ktoré by mal poznať každý prompt inžinier. Ak ste už absolvovali [Quick Start modul](../00-quick-start/README.md#2-prompt-patterns), videli ste ich v akcii — toto je koncepčný rámec za nimi.

### Zero-Shot Prompting

Najjednoduchší prístup: dajte modelu priamo inštrukciu bez príkladov. Model sa spolieha výhradne na svoje trénovanie na pochopenie a vykonanie úlohy. Funguje to dobre pri priamych požiadavkách, kde je očakávané správanie zrejmé.

<img src="../../../translated_images/sk/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Priama inštrukcia bez príkladov — model odvodzuje úlohu iba z inštrukcie*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odpoveď: "Pozitívne"
```

**Kedy použiť:** Jednoduché klasifikácie, priame otázky, preklady alebo akákoľvek úloha, ktorú model dokáže zvládnuť bez dodatočných rád.

### Few-Shot Prompting

Poskytnite príklady, ktoré demonštrujú vzor, ktorý chcete, aby model nasledoval. Model sa naučí očakávaný formát vstup-výstup z vašich príkladov a aplikuje ho na nové vstupy. Toto výrazne zlepšuje konzistenciu pri úlohách, kde požadovaný formát alebo správanie nie je zjavné.

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

**Kedy použiť:** Vlastné klasifikácie, konzistentná formátovanie, doménovo špecifické úlohy alebo ak sú výsledky zero-shot nekonzistentné.

### Chain of Thought

Požiadajte model, aby ukázal svoje rozumovanie krok za krokom. Namiesto okamžitej odpovede model rozkladá problém a prechádza každou časťou explicitne. To zlepšuje presnosť pri matematike, logike a viacstupňovom rozumovaní.

<img src="../../../translated_images/sk/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Rozumovanie krok za krokom — rozklad komplexných problémov na explicitné logické kroky*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model ukazuje: 15 - 8 = 7, potom 7 + 12 = 19 jabĺk
```

**Kedy použiť:** Matematické problémy, logické hádanky, ladenie alebo akákoľvek úloha, kde zobrazenie rozumovacieho procesu zvyšuje presnosť a dôveru.

### Role-Based Prompting

Nastavte personu alebo rolu AI pred položením otázky. Toto poskytuje kontext, ktorý formuje tón, hĺbku a zameranie odpovede. „Softvérový architekt“ poskytne iné rady než „junior vývojár“ alebo „auditor bezpečnosti“.

<img src="../../../translated_images/sk/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Nastavenie kontextu a persony — tá istá otázka dostane odlišnú odpoveď podľa pridelenej roly*

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

**Kedy použiť:** Kódové revízie, doučovanie, doménovo špecifická analýza alebo ak potrebujete odpovede prispôsobené úrovni odbornosti alebo perspektíve.

### Prompt Templates

Vytvárajte opakovane použiteľné prompty s premennými zástupcami. Namiesto písania nového promptu zakaždým definujte šablónu raz a vyplňte rôzne hodnoty. Trieda `PromptTemplate` v LangChain4j to uľahčuje pomocou syntaxe `{{variable}}`.

<img src="../../../translated_images/sk/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Opakovane použiteľné prompty s premennými zástupcami — jedna šablóna, mnoho použití*

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

**Kedy použiť:** Opakované dopyty s rôznymi vstupmi, dávkové spracovanie, budovanie opakovane použiteľných AI pracovných postupov alebo akýkoľvek scenár, kde štruktúra promptu zostáva rovnaká, ale dáta sa menia.

---

Tieto päť základov vám poskytuje pevný arzenál pre väčšinu promptingových úloh. Zvyšok tohto modulu na nich stavia s **ošmi pokročilými vzormi**, ktoré využívajú kontrolu rozumovania GPT-5.2, sebahodnotenie a schopnosti štruktúrovaného výstupu.

## Pokročilé vzory

Po zvládnutí základov sa presuňme k ôsmim pokročilým vzorom, ktoré robia tento modul jedinečným. Nie každý problém vyžaduje rovnaký prístup. Niektoré otázky potrebujú rýchle odpovede, iné hlboké premýšľanie. Niektoré vyžadujú viditeľné rozumovanie, iné iba výsledky. Každý vzor nižšie je optimalizovaný pre iný scenár — a kontrola rozumovania GPT-5.2 robí tieto rozdiely ešte výraznejšími.

<img src="../../../translated_images/sk/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osem promptingových vzorov" width="800"/>

*Prehľad ôsmich vzorov inžinierstva promptov a ich použitia*

<img src="../../../translated_images/sk/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontrola rozumovania s GPT-5.2" width="800"/>

*Kontrola rozumovania GPT-5.2 umožňuje určiť, koľko má model premýšľať — od rýchlych priamych odpovedí po hlboké skúmanie*

**Nízka ochota (rýchle a zamerané)** - Pre jednoduché otázky, kde chcete rýchle, priame odpovede. Model vykoná minimálne rozumovanie - maximálne 2 kroky. Použite to pri výpočtoch, vyhľadávaniach alebo priamych otázkach.

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
> - „Aký je rozdiel medzi nízkou a vysokou ochotou v promptingových vzoroch?“
> - „Ako XML značky v promptoch pomáhajú štruktúrovať odpoveď AI?“
> - „Kedy použiť vzory sebareflexie vs priame inštrukcie?“

**Vysoká ochota (hlboké a dôkladné)** - Pre komplexné problémy, kde chcete komplexnú analýzu. Model dôkladne skúma a ukazuje podrobné rozumovanie. Používajte to pri návrhoch systémov, rozhodnutiach o architektúre alebo komplexnom výskume.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Vykonávanie úloh (postup krok za krokom)** - Pre viacstupňové pracovné postupy. Model poskytuje plán dopredu, rozpráva o každom kroku počas práce a potom dáva zhrnutie. Použite to pri migráciách, implementáciách alebo akomkoľvek viacstupňovom procese.

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

Chain-of-Thought prompting explicitne žiada model, aby ukázal svoj rozumovací proces, čím zlepšuje presnosť pri komplexných úlohách. Rozklad krok za krokom pomáha ľuďom aj AI pochopiť logiku.

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Opýtajte sa na tento vzor:
> - „Ako by som adaptoval vzor vykonávania úloh pre dlhodobé operácie?“
> - „Aké sú najlepšie praktiky pre štruktúrovanie preambulov nástrojov v produkčných aplikáciách?“
> - „Ako môžem zachytiť a zobrazovať priebežné aktualizácie pokroku v UI?“

<img src="../../../translated_images/sk/task-execution-pattern.9da3967750ab5c1e.webp" alt="Vzor vykonávania úloh" width="800"/>

*Plán → Vykonaj → Zhrni pracovný postup pre viacstupňové úlohy*

**Seba-reflektujúci kód** - Pre generovanie kódu produkčnej kvality. Model generuje kód podľa produkčných štandardov s riadnym spracovaním chýb. Používajte to pri budovaní nových funkcií alebo služieb.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sk/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cyklus sebareflexie" width="800"/>

*Iteratívna slučka zlepšenia - generuj, hodnoti, identifikuj problémy, zlepši, opakuj*

**Štruktúrovaná analýza** - Pre konzistentné hodnotenie. Model recenzuje kód pomocou pevného rámca (správnosť, postupy, výkon, bezpečnosť, udržiavateľnosť). Používajte to na revízie kódu alebo hodnotenia kvality.

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
> - „Ako môžem prispôsobiť hodnotiaci rámec pre rôzne typy revízií kódu?“
> - „Aký je najlepší spôsob, ako programaticky spracovať a využiť štruktúrovaný výstup?“
> - „Ako zabezpečiť konzistentnú závažnosť naprieč rôznymi hodnotiacimi stretnutiami?“

<img src="../../../translated_images/sk/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Vzor štruktúrovanej analýzy" width="800"/>

*Rámec pre konzistentné revízie kódu so závažnostnými úrovňami*

**Viackolový chat** - Pre konverzácie, ktoré potrebujú kontext. Model si pamätá predchádzajúce správy a stavia na nich. Používajte to pre interaktívne pomocné relácie alebo zložité otázky a odpovede.

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

*Ako sa konverzačný kontext hromadí cez viaceré kolá až do dosiahnutia limitu tokenov*

**Rozumovanie krok za krokom** - Pre problémy vyžadujúce viditeľnú logiku. Model ukazuje explicitné rozumovanie pre každý krok. Používajte to pre matematické problémy, logické hádanky alebo ak potrebujete pochopiť proces myslenia.

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

*Rozklad problémov do explicitných logických krokov*

**Obmedzený výstup** - Pre odpovede so špecifickými požiadavkami na formát. Model prísne dodržiava pravidlá formátu a dĺžky. Používajte to pre zhrnutia alebo keď potrebujete presnú štruktúru výstupu.

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

## Použitie existujúcich zdrojov Azure

**Overenie nasadenia:**

Uistite sa, že súbor `.env` existuje v koreňovom adresári s povereniami Azure (vytvorený počas Modulu 01):
```bash
cat ../.env  # Malo by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spustenie aplikácie:**

> **Poznámka:** Ak ste už spustili všetky aplikácie pomocou `./start-all.sh` z Modulu 01, tento modul už beží na porte 8083. Môžete preskočiť spúšťacie príkazy nižšie a prejsť rovno na http://localhost:8083.
**Možnosť 1: Použitie Spring Boot Dashboard (odporúčané pre používateľov VS Code)**

Dev kontajner obsahuje rozšírenie Spring Boot Dashboard, ktoré poskytuje vizuálne rozhranie na správu všetkých Spring Boot aplikácií. Nájdete ho v Activity Bar na ľavej strane VS Code (hľadajte ikonu Spring Boot).

Zo Spring Boot Dashboard môžete:
- Vidieť všetky dostupné Spring Boot aplikácie v pracovnom priestore
- Spustiť/zastaviť aplikácie jedným kliknutím
- Zobraziť logy aplikácií v reálnom čase
- Monitorovať stav aplikácií

Jednoducho kliknite na tlačidlo prehrávania vedľa "prompt-engineering" na spustenie tohto modulu, alebo spustite všetky moduly naraz.

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

Oba skripty automaticky načítavajú premenné prostredia z koreňového súboru `.env` a zostavia JARy, ak ešte neexistujú.

> **Poznámka:** Ak preferujete manuálne zostaviť všetky moduly pred spustením:
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

Otvorte vo vašom prehliadači http://localhost:8083.

**Na zastavenie:**

**Bash:**
```bash
./stop.sh  # Iba tento modul
# Alebo
cd .. && ./stop-all.sh  # Všetky moduly
```

**PowerShell:**
```powershell
.\stop.ps1  # Iba tento modul
# Alebo
cd ..; .\stop-all.ps1  # Všetky moduly
```

## Screenshoty aplikácie

<img src="../../../translated_images/sk/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Hlavný dashboard zobrazujúci všetkých 8 vzorov prompt inžinierstva s ich charakteristikami a prípadmi použitia*

## Preskúmanie vzorov

Webové rozhranie vám umožňuje experimentovať s rôznymi stratégiami promptovania. Každý vzor rieši iné problémy – vyskúšajte ich, aby ste videli, kedy ktorých prístup vynikne.

> **Poznámka: Streaming vs Non-Streaming** — Každá stránka vzoru ponúka dve tlačidlá: **🔴 Stream Response (Live)** a možnosť **Non-streaming**. Streaming využíva Server-Sent Events (SSE) na zobrazovanie tokenov v reálnom čase, ako ich model generuje, takže pokrok vidíte okamžite. Non-streaming možnosť čaká na celú odpoveď pred jej zobrazením. Pre prompty, ktoré vyžadujú hlboké uvažovanie (napr. High Eagerness, Self-Reflecting Code), volanie non-streaming môže trvať veľmi dlho — niekedy minúty — bez viditeľnej spätnej väzby. **Používajte streaming pri experimentoch s komplexnými promptmi**, aby ste videli, ako model pracuje, a vyhli sa dojmu, že požiadavka zlyhala.
>
> **Poznámka: Požiadavky prehliadača** — Funkcia streamingu používa Fetch Streams API (`response.body.getReader()`), ktoré vyžaduje plnohodnotný prehliadač (Chrome, Edge, Firefox, Safari). Nepracuje vo vstavanom Simple Browser v VS Code, pretože jeho webview nepodporuje ReadableStream API. Ak používate Simple Browser, tlačidlá non-streaming budú normálne fungovať – ovplyvnené sú len tlačidlá streamingu. Pre kompletný zážitok otvorte `http://localhost:8083` v externom prehliadači.

### Nízka vs Vysoká Dychtivosť

Opýtajte sa jednoduchú otázku ako „Čo je 15 % z 200?“ s nízkou dychtivosťou. Dostanete okamžitú, priamu odpoveď. Teraz skúste niečo zložitejšie ako „Navrhnite stratégiu cachovania pre vysoko zaťažené API“ s vysokou dychtivosťou. Kliknite na **🔴 Stream Response (Live)** a sledujte, ako sa podrobné uvažovanie modelu objavuje token po tokene. Rovnaký model, rovnaká štruktúra otázky – ale prompt mu hovorí, koľko rozmýšľať.

### Vykonávanie úloh (Predslovy nástrojov)

Viacstupňové pracovné postupy profitujú z plánovania dopredu a komentovania pokroku. Model naznačí, čo urobí, popisuje každý krok a potom zhrnie výsledky.

### Sebareflektujúci kód

Vyskúšajte „Vytvor službu na overovanie emailov“. Namiesto generovania kódu a zastavenia model generuje, hodnotí podľa kvalitatívnych kritérií, identifikuje slabiny a vylepšuje. Uvidíte, ako iteruje, kým kód nespĺňa produkčné štandardy.

### Štruktúrovaná analýza

Kontroly kódu vyžadujú konzistentné hodnotiace rámce. Model analyzuje kód pomocou pevných kategórií (správnosť, praktiky, výkon, bezpečnosť) s úrovňami závažnosti.

### Viackolová konverzácia

Opýtajte sa „Čo je Spring Boot?“ a hneď potom sa pýtajte „Ukáž mi príklad“. Model si pamätá vašu prvú otázku a predloží vám špecifický príklad Spring Boot. Bez pamäti by druhá otázka bola príliš nejasná.

### Krok za krokom uvažovanie

Vyberte si matematický problém a vyskúšajte ho s krokovým uvažovaním aj s nízkou dychtivosťou. Nízka dychtivosť vám poskytne odpoveď – rýchlo ale nejasne. Krok za krokom vám ukáže každý výpočet a rozhodnutie.

### Obmedzený výstup

Keď potrebujete konkrétne formáty alebo počet slov, tento vzor striktne vyžaduje dodržiavanie. Skúste vygenerovať zhrnutie s presne 100 slovami v bodoch.

## Čo sa naozaj učíte

**Úsilie o uvažovanie mení všetko**

GPT-5.2 vám umožňuje riadiť výpočtové úsilie cez svoje promptovania. Nízke úsilie znamená rýchle odpovede s minimálnym preskúmaním. Vysoké úsilie znamená, že model si dá čas na hlboké uvažovanie. Učíte sa prispôsobiť úsilie komplexnosti úlohy – nestrácajte čas na jednoduché otázky, ale ani príliš neponáhľajte komplexné rozhodnutia.

**Štruktúra riadi správanie**

Všimli ste si XML tagy v promptoch? Nie sú len dekoráciou. Modely dodržiavajú štruktúrované inštrukcie spoľahlivejšie než voľný text. Keď potrebujete viacstupňové procesy alebo komplexnú logiku, štruktúra pomáha modelu sledovať, kde sa nachádza a čo príde ďalej.

<img src="../../../translated_images/sk/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatómia dobre štruktúrovaného promptu s jasnými sekciami a organizáciou v štýle XML*

**Kvalita cez seba-hodnotenie**

Sebareflektujúce vzory fungujú tak, že explicitne stanovujú kritériá kvality. Namiesto nádeje, že model to „urobí správne“, presne mu hovoríte, čo znamená „správne“: korektná logika, spracovanie chýb, výkon, bezpečnosť. Model potom môže vyhodnotiť svoj vlastný výstup a zlepšiť ho. To transformuje generovanie kódu z lotérie na proces.

**Kontext je konečný**

Viackolové konverzácie fungujú tak, že ku každej požiadavke pridávajú históriu správ. Ale existuje limit – každý model má maximálny počet tokenov. Ako konverzácie rastú, budete potrebovať stratégie, ako si udržať relevantný kontext bez prekročenia limitu. Tento modul vám ukáže, ako pamäť funguje; neskôr sa naučíte, kedy zhrnúť, kedy zabudnúť a kedy vyhľadať.

## Ďalšie kroky

**Ďalší modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigácia:** [← Predchádzajúci: Modul 01 - Úvod](../01-introduction/README.md) | [Späť na hlavnú](../README.md) | [Ďalší: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vyhlásenie o zodpovednosti**:  
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Hoci sa snažíme o presnosť, majte prosím na pamäti, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Originálny dokument v jeho pôvodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre dôležité informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za žiadne nedorozumenia alebo nesprávne interpretácie vzniknuté použitím tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->