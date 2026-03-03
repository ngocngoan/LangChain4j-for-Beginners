# Modul 02: Prompt Engineering s GPT-5.2

## Obsah

- [Video Prehľad](../../../02-prompt-engineering)
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
- [Spustenie aplikácie](../../../02-prompt-engineering)
- [Screenshoty aplikácie](../../../02-prompt-engineering)
- [Preskúmanie vzorov](../../../02-prompt-engineering)
  - [Nízka vs vysoka ochota](../../../02-prompt-engineering)
  - [Vykonávanie úloh (predslovy nástrojov)](../../../02-prompt-engineering)
  - [Seba-reflektujúci kód](../../../02-prompt-engineering)
  - [Štruktúrovaná analýza](../../../02-prompt-engineering)
  - [Viackolové konverzácie](../../../02-prompt-engineering)
  - [Dôvodovanie krok za krokom](../../../02-prompt-engineering)
  - [Obmedzený výstup](../../../02-prompt-engineering)
- [Čo sa naozaj učíte](../../../02-prompt-engineering)
- [Ďalšie kroky](../../../02-prompt-engineering)

## Video Prehľad

Pozrite si túto živú reláciu, ktorá vysvetľuje, ako začať s týmto modulom:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Čo sa naučíte

Nasledujúca schéma poskytuje prehľad kľúčových tém a zručností, ktoré v tomto module získate — od techník zdokonaľovania promptov až po postupný pracovný tok, ktorý budete nasledovať.

<img src="../../../translated_images/sk/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

V predchádzajúcich moduloch ste preskúmali základné interakcie LangChain4j s GitHub modelmi a videli ste, ako pamäť umožňuje konverzačné AI s Azure OpenAI. Teraz sa zameriame na to, ako kladiete otázky — samotné promptovanie — pomocou GPT-5.2 od Azure OpenAI. Spôsob, akým štruktúrujete svoje prompty, dramaticky ovplyvňuje kvalitu odpovedí, ktoré dostávate. Začíname prehľadom základných techník promptovania, potom prejdeme na osem pokročilých vzorov, ktoré plne využívajú schopnosti GPT-5.2.

Použijeme GPT-5.2, pretože zavádza kontrolu razonovania - môžete modelu povedať, koľko má premýšľať pred odpoveďou. To robí rôzne stratégie promptovania zreteľnejšie a pomáha pochopiť, kedy použiť ktorý prístup. Tiež využijeme výhodu Azure, ktorý má menej obmedzení rýchlosti pre GPT-5.2 v porovnaní s GitHub modelmi.

## Predpoklady

- Dokončený Modul 01 (nasadené zdroje Azure OpenAI)
- Súbor `.env` v koreňovom adresári s Azure povereniami (vytvorený príkazom `azd up` v Module 01)

> **Poznámka:** Ak ste ešte neukončili Modul 01, najskôr postupujte podľa tamojších inštrukcií nasadenia.

## Pochopenie Prompt Engineering

V jadre je prompt engineering rozdiel medzi nejasnými a presnými inštrukciami, ako ukazuje nasledujúce porovnanie.

<img src="../../../translated_images/sk/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Prompt engineering je o navrhovaní vstupného textu, ktorý vám konzistentne prináša požadované výsledky. Nie je to len o kladení otázok - ide o štruktúrovanie požiadaviek tak, aby model presne pochopil, čo chcete a ako to má doručiť.

Predstavte si to ako dávanie inštrukcií kolegovi. "Oprav chybu" je nejasné. "Oprav výnimku null pointer v súbore UserService.java na riadku 45 pridaním kontroly na null" je špecifické. Jazykové modely fungujú rovnako - špecifikácia a štruktúra sú dôležité.

Nasledujúca schéma ukazuje, ako sa LangChain4j tohto procesu zúčastňuje — prepája vaše vzory promptov s modelom cez stavebné bloky SystemMessage a UserMessage.

<img src="../../../translated_images/sk/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j poskytuje infraštruktúru — pripojenia k modelu, pamäť a typy správ — zatiaľ čo vzory promptov sú len starostlivo štruktúrovaný text, ktorý cez túto infraštruktúru posielate. Kľúčové stavebné bloky sú `SystemMessage` (ktorý nastavuje správanie a rolu AI) a `UserMessage` (ktorý nesie vašu skutočnú požiadavku).

## Základy Prompt Engineering

Päť základných techník zobrazených nižšie tvorí základ efektívneho prompt engineeringu. Každá rieši iný aspekt toho, ako komunikujete s jazykovými modelmi.

<img src="../../../translated_images/sk/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Predtým, než sa ponoríme do pokročilých vzorov v tomto module, prejdime si päť základných techník promptovania. Sú to stavebné kamene, ktoré by mal poznať každý prompt inžinier. Ak ste už prešli cez [Quick Start modul](../00-quick-start/README.md#2-prompt-patterns), videli ste ich v akcii — tu je konceptuálny rámec za nimi.

### Zero-Shot Prompting

Najjednoduchší prístup: dajte modelu priamu inštrukciu bez príkladov. Model sa spolieha výhradne na svoje tréningové dáta, aby pochopil a vykonal úlohu. Funguje to dobre pri jednoznačných požiadavkách, kde je očakávané správanie zrejmé.

<img src="../../../translated_images/sk/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Priama inštrukcia bez príkladov — model vyvodzuje úlohu z inštrukcie samotnej*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odpoveď: "Pozitívne"
```

**Kedy použiť:** Jednoduché klasifikácie, priame otázky, preklady alebo akúkoľvek úlohu, ktorú model zvládne bez ďalších pokynov.

### Few-Shot Prompting

Poskytnite príklady, ktoré demonštrujú vzor, ktorý chcete, aby model nasledoval. Model sa naučí očakávaný formát vstupu a výstupu z vašich príkladov a aplikuje ho na nové vstupy. Toto výrazne zlepšuje konzistenciu pri úlohách, kde požadovaný formát alebo správanie nie je zrejmé.

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

**Kedy použiť:** Vlastné klasifikácie, konzistentné formátovanie, špecifické doménové úlohy alebo ak sú výsledky zero-shot nejednoznačné.

### Chain of Thought

Požiadajte model, aby ukázal svoje zdôvodnenie krok za krokom. Namiesto okamžitej odpovede model rozkladá problém a explicitne rieši každú časť. To zlepšuje presnosť pri matematike, logike a viacstupňových razonovacích úlohách.

<img src="../../../translated_images/sk/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Dôvodovanie krok za krokom — rozkladanie komplexných problémov na explicitné logické kroky*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model ukazuje: 15 - 8 = 7, potom 7 + 12 = 19 jabĺk
```

**Kedy použiť:** Matematické problémy, logické hádanky, ladenie chýb alebo akákoľvek úloha, kde zobrazenie procesu razonovania zvyšuje presnosť a dôveru.

### Role-Based Prompting

Nastavte AI personu alebo rolu pred položením otázky. To poskytuje kontext, ktorý formuje tón, hĺbku a fokus odpovede. "Softvérový architekt" dáva iné rady než "junior vývojár" alebo "auditor bezpečnosti".

<img src="../../../translated_images/sk/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Nastavenie kontextu a persony — rovnaká otázka dostane odlišnú odpoveď podľa pridanej role*

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

**Kedy použiť:** Kontroly kódu, doučovanie, špecifická doménová analýza alebo keď potrebujete odpovede prispôsobené určitej úrovni odbornosti či perspektíve.

### Prompt Templates

Vytvorte znovupoužiteľné prompty s premennými zástupcami. Namiesto písania nového promptu zakaždým, definujte šablónu raz a vyplňujte rôzne hodnoty. Trieda `PromptTemplate` v LangChain4j to uľahčuje pomocou syntaxe `{{variable}}`.

<img src="../../../translated_images/sk/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Znovupoužiteľné prompty s premennými zástupcami — jedna šablóna, mnoho využití*

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

**Kedy použiť:** Opakované dopyty s rôznymi vstupmi, dávkové spracovanie, budovanie znovupoužiteľných AI pracovných tokov alebo akýkoľvek scenár, kde sa štruktúra promptu nemení, ale údaje áno.

---

Tieto päť základov vám poskytuje pevný nástrojový súbor pre väčšinu promptovacích úloh. Zvyšok tohto modulu na nich stavia s **osem pokročilými vzormi**, ktoré využívajú razonovaciu kontrolu, sebahodnotenie a schopnosti štruktúrovaného výstupu GPT-5.2.

## Pokročilé vzory

Keďže základné techniky sú pokryté, poďme k ôsmim pokročilým vzorom, ktoré robia tento modul jedinečným. Nie všetky problémy vyžadujú rovnaký prístup. Niektoré otázky potrebujú rýchle odpovede, iné hlboké premýšľanie. Niektoré potrebujú viditeľné razonovanie, iné len výsledky. Každý z nižšie uvedených vzorov je optimalizovaný pre inú situáciu — a razonovacia kontrola GPT-5.2 robí rozdiely ešte výraznejšími.

<img src="../../../translated_images/sk/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Prehľad ôsmich vzorov prompt engineeringu a ich použitia*

GPT-5.2 pridáva ďalšiu dimenziu k týmto vzorom: *kontrolu razonovania*. Posuvník nižšie ukazuje, ako môžete upraviť rozsah premýšľania modelu — od rýchlych, priamych odpovedí po hlbokú, dôkladnú analýzu.

<img src="../../../translated_images/sk/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Razonovacia kontrola GPT-5.2 vám umožňuje špecifikovať, koľko má model rozmýšľať — od rýchlych priamych odpovedí po hlboké skúmanie*

**Nízka ochota (rýchle a zamerané)** - Pre jednoduché otázky, kde chcete rýchle, priame odpovede. Model vykoná minimálne razonovanie - maximálne 2 kroky. Použite to na výpočty, vyhľadávania alebo jednoznačné otázky.

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
> - "Aký je rozdiel medzi nízkou a vysokou ochotou pri promptovacích vzoroch?"
> - "Ako XML značky v promptoch pomáhajú štruktúrovať odpoveď AI?"
> - "Kedy mám použiť vzory so sebareflexiou oproti priamej inštrukcii?"

**Vysoká ochota (hlboké a dôkladné)** - Pre komplexné problémy, kde chcete komplexnú analýzu. Model dôkladne skúma a ukazuje podrobné zdôvodnenia. Použite to na návrhy systémov, rozhodnutia o architektúre alebo komplexný výskum.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Vykonávanie úloh (postupný pokrok)** - Pre viacstupňové pracovné toky. Model poskytne plán vopred, komentuje každý krok pri práci a potom dá zhrnutie. Použite to pri migráciách, implementáciách alebo akomkoľvek viacstupňovom procese.

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

Promptovanie Chain-of-Thought explicitne žiada model, aby ukázal proces razonovania, čím zlepšuje presnosť pri zložitých úlohách. Postupné rozdelenie pomáha ľuďom aj AI pochopiť logiku.

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Spýtajte sa na tento vzor:
> - "Ako by som upravil vzor vykonávania úloh pre dlhodobé operácie?"
> - "Aké sú najlepšie praktiky pre štrukturovanie predslovov nástrojov v produkčných aplikáciách?"
> - "Ako zachytiť a zobrazovať priebežné aktualizácie pokroku v UI?"

Schéma nižšie ilustruje pracovný tok Plán → Vykonaj → Zhrň.

<img src="../../../translated_images/sk/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Pracovný tok Plán → Vykonaj → Zhrň pre viacstupňové úlohy*

**Seba-reflektujúci kód** - Pre generovanie produkčne kvalitného kódu. Model generuje kód v súlade s produkčnými štandardmi s riadnym spracovaním chýb. Použite to pri budovaní nových funkcií alebo služieb.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

Schéma nižšie ukazuje tento iteratívny cyklus zlepšovania — generuj, vyhodnoť, identifikuj nedostatky a dolad až kým kód spĺňa produkčné štandardy.

<img src="../../../translated_images/sk/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iteratívny cyklus zlepšovania - generovať, hodnotiť, identifikovať problémy, zlepšovať, opakovať*

**Štruktúrovaná analýza** - Pre konzistentné hodnotenie. Model hodnotí kód pomocou pevne stanovenej štruktúry (správnosť, postupy, výkon, bezpečnosť, udržiavateľnosť). Použite to na kontrolu kódu alebo hodnotenie kvality.

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
> - "Ako prispôsobiť analytický rámec pre rôzne typy kontrol kódu?"
> - "Aký je najlepší spôsob, ako programovo spracovať a reagovať na štruktúrovaný výstup?"
> - "Ako zabezpečiť konzistentné úrovne závažnosti naprieč rôznymi kontrolnými sedeními?"

Nasledujúca schéma ukazuje, ako táto štruktúrovaná rámcová analýza organizuje kontrolu kódu do konzistentných kategórií s úrovňami závažnosti.

<img src="../../../translated_images/sk/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Rámec pre konzistentné kontroly kódu s úrovňami závažnosti*

**Viackolové konverzácie** - Pre rozhovory, ktoré potrebujú kontext. Model si pamätá predchádzajúce správy a buduje na nich. Použite to na interaktívnu pomoc alebo zložité otázky a odpovede.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

Schéma nižšie vizualizuje, ako sa konverzačný kontext s každým kolom kumuluje a ako súvisí s limitom tokenov modelu.

<img src="../../../translated_images/sk/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Ako sa konverzačný kontext kumuluje cez viaceré kolá až do dosiahnutia limitu tokenov*
**Krok za krokom vysvetlenie** - Pre problémy vyžadujúce viditeľnú logiku. Model ukazuje explicitné zdôvodnenie pre každý krok. Použite to pri matematických úlohách, logických hádankách alebo keď potrebujete pochopiť myšlienkový proces.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

Nižšie uvedený diagram zobrazuje, ako model rozdeľuje problémy na explicitné, číslované logické kroky.

<img src="../../../translated_images/sk/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Rozkladanie problémov na explicitné logické kroky*

**Obmedzený výstup** - Pre odpovede s konkrétnymi požiadavkami na formát. Model striktne dodržiava pravidlá formátu a dĺžky. Použite to na súhrny alebo keď potrebujete presnú štruktúru výstupu.

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

Nasledujúci diagram ukazuje, ako obmedzenia usmerňujú model vytvoriť výstup, ktorý striktne dodržiava vaše požiadavky na formát a dĺžku.

<img src="../../../translated_images/sk/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Nútenie dodržiavania špecifických požiadaviek na formát, dĺžku a štruktúru*

## Spustenie aplikácie

**Overenie nasadenia:**

Uistite sa, že súbor `.env` existuje v koreňovom adresári s údajmi Azure (vytvorený počas Modulu 01). Spustite to z adresára modulu (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # Malo by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Malo by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spustenie aplikácie:**

> **Poznámka:** Ak ste už spustili všetky aplikácie pomocou `./start-all.sh` z koreňového adresára (ako je popísané v Module 01), tento modul už beží na porte 8083. Môžete vynechať príkazy na spustenie nižšie a ísť priamo na http://localhost:8083.

**Možnosť 1: Použitie Spring Boot Dashboard (Odporúčané pre používateľov VS Code)**

Vývojové prostredie obsahuje rozšírenie Spring Boot Dashboard, ktoré poskytuje vizuálne rozhranie na správu všetkých Spring Boot aplikácií. Nájdete ho na Paneli aktivít na ľavej strane VS Code (hľadajte ikonu Spring Boot).

Zo Spring Boot Dashboard môžete:
- Vidieť všetky dostupné Spring Boot aplikácie v pracovnom priestore
- Jedným kliknutím spustiť/zastaviť aplikácie
- Sledovať logy aplikácie v reálnom čase
- Monitorovať stav aplikácie

Jednoducho kliknite na tlačidlo prehrávania vedľa "prompt-engineering" pre spustenie tohto modulu, alebo spustite všetky moduly naraz.

<img src="../../../translated_images/sk/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard vo VS Code — spustite, zastavte a sledujte všetky moduly z jedného miesta*

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

Oba skripty automaticky načítajú premenné prostredia z koreňového `.env` súboru a zostavia JAR súbory, ak neexistujú.

> **Poznámka:** Ak chcete radšej zostaviť všetky moduly manuálne pred spustením:
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

Otvorte http://localhost:8083 vo svojom prehliadači.

**Na zastavenie:**

**Bash:**
```bash
./stop.sh  # Iba tento modul
# Alebo
cd .. && ./stop-all.sh  # Všetky moduly
```

**PowerShell:**
```powershell
.\stop.ps1  # Tento modul iba
# Alebo
cd ..; .\stop-all.ps1  # Všetky moduly
```

## Screenshoty aplikácie

Tu je hlavné rozhranie modulu prompt engineering, kde môžete experimentovať so všetkými ôsmimi vzormi vedľa seba.

<img src="../../../translated_images/sk/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Hlavný dashboard zobrazujúci všetkých 8 vzorov prompt engineering s ich charakteristikami a prípadmi použitia*

## Preskúmanie vzorov

Webové rozhranie vám umožňuje experimentovať s rôznymi stratégiami promptovania. Každý vzor rieši odlišné problémy – vyskúšajte ich a zistite, kedy ktorý prístup vynikne.

> **Poznámka: Streamovanie vs Nestreamovanie** — Každá stránka s vzorom ponúka dva tlačidlá: **🔴 Stream Response (Live)** a možnosť **Nestreamovanie**. Streamovanie používa Server-Sent Events (SSE) na zobrazenie tokenov v reálnom čase, keď ich model generuje, takže vidíte postup okamžite. Nestreamovanie čaká na celú odpoveď, až potom ju zobrazí. Pre prompt-y vyžadujúce hlboké uvažovanie (napr. High Eagerness, Self-Reflecting Code) môže nestreamovanie trvať veľmi dlho — niekedy minúty — bez viditeľnej spätnej väzby. **Používajte streamovanie pri experimentoch s komplexnými promptami**, aby ste videli model pracovať a predišli dojmu, že požiadavka vypršala.
>
> **Poznámka: Požiadavka na prehliadač** — Funkcia streamovania používa Fetch Streams API (`response.body.getReader()`), ktoré vyžaduje plnohodnotný prehliadač (Chrome, Edge, Firefox, Safari). Nepracuje vo VS Code zabudovanom Simple Browser, pretože jeho webview nepodporuje ReadableStream API. Ak používate Simple Browser, tlačidlá pre nestreamovanie budú fungovať normálne — postihnuté sú len tlačidlá pre streamovanie. Pre úplný zážitok otvorte `http://localhost:8083` v externom prehliadači.

### Nízka vs vysoká snaha (Eagerness)

Opýtajte sa jednoduchú otázku, napríklad „Čo je 15 % zo 200?“ pomocou Nízkej snahe. Dostanete okamžitú, priamu odpoveď. Teraz sa opýtajte niečo zložité, napríklad „Navrhnite caching stratégiu pre vysokotrafikovú API“ pomocou Vysokej snahy. Kliknite na **🔴 Stream Response (Live)** a sledujte, ako sa token po tokene objavuje podrobné zdôvodnenie modelu. Rovnaký model, rovnaká štruktúra otázky – ale prompt mu hovorí, koľko premýšľania má urobiť.

### Vykonávanie úloh (předslovné nástroje)

Workflows s viacerými krokmi profitujú z vopred plánovania a priebežného rozprávania. Model načrtne, čo urobí, popíše každý krok, potom zhrnie výsledky.

### Samo-reflektujúci kód

Vyskúšajte „Vytvorte službu na validáciu emailov“. Namiesto toho, aby model len vygeneroval kód a zastavil sa, generuje, hodnotí podľa kvalitatívnych kritérií, identifikuje slabé miesta a zlepšuje ho. Uvidíte, ako iteruje, kým kód nespĺňa produkčné štandardy.

### Štruktúrovaná analýza

Kontroly kódu vyžadujú konzistentné hodnotiace rámce. Model analyzuje kód s použitím pevných kategórií (správnosť, praktiky, výkon, bezpečnosť) a úrovní závažnosti.

### Viackrokový chat

Opýtajte sa „Čo je Spring Boot?“ a potom hneď pokračujte otázkou „Ukáž mi príklad“. Model si pamätá vašu prvú otázku a poskytne vám konkrétny príklad Spring Boot. Bez pamäti by druhá otázka bola príliš všeobecná.

### Krok za krokom zdôvodnenie

Vyberte matematický problém a vyskúšajte ho s Krok za krokom zdôvodnením aj Nízku snahu. Nízka snaha vám len rýchlo dá odpoveď, no bez vysvetlenia. Krok za krokom vám ukáže každý výpočet a rozhodnutie.

### Obmedzený výstup

Keď potrebujete konkrétne formáty alebo počet slov, tento vzor vynucuje prísne dodržiavanie pravidiel. Vyskúšajte generovanie súhrnu presne so 100 slovami v odrážkach.

## Čo sa naozaj učíte

**Snahou o zdôvodnenie sa mení všetko**

GPT-5.2 vám umožňuje kontrolovať výpočtové úsilie cez vaše prompt-y. Nízke úsilie znamená rýchle odpovede s minimálnym skúmaním. Vysoké úsilie znamená, že model si dá čas na dôkladné premýšľanie. Učíte sa prispôsobiť úsilie zložitosti úlohy – nestrácajte čas na jednoduché otázky, ale ani neponáhľajte zložité rozhodnutia.

**Štruktúra usmerňuje správanie**

Všimli ste si XML tagy v promptoch? Nie sú len dekoratívne. Modely dôveryhodnejšie nasledujú štruktúrované inštrukcie než voľný text. Keď potrebujete viacstupňové procesy alebo komplexnú logiku, štruktúra pomáha modelu sledovať, kde je a čo nasleduje. Nižšie uvedený diagram rozkladá dobre štruktúrovaný prompt, ukazujúc ako tagy `<system>`, `<instructions>`, `<context>`, `<user-input>` a `<constraints>` organizujú vaše inštrukcie do prehľadných sekcií.

<img src="../../../translated_images/sk/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatómia dobre štruktúrovaného promptu s jasnými sekciami a XML štýlom organizácie*

**Kvalita cez samo-hodnotenie**

Samo-reflektujúce vzory fungujú tak, že explicitne uvedú kvalitatívne kritériá. Namiesto dúfania, že model „to urobí správne“, mu presne poviete, čo znamená „správne“: správna logika, spracovanie chýb, výkon, bezpečnosť. Model potom dokáže ohodnotiť vlastný výstup a zlepšiť ho. To premieňa generovanie kódu z lotérie na proces.

**Kontext je konečný**

Viacstupňové rozhovory fungujú tak, že so žiadosťou sa zahrnie história správ. Ale je tu limit – každý model má maximálny počet tokenov. Ako konverzácie rastú, budete potrebovať stratégie na udržanie relevantného kontextu bez prekročenia limitu. Tento modul vám ukazuje, ako funguje pamäť; neskôr sa naučíte, kedy zhrnúť, kedy zabudnúť a kedy získať späť.

## Ďalšie kroky

**Ďalší modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigácia:** [← Predchádzajúci: Modul 01 - Úvod](../01-introduction/README.md) | [Späť na hlavný](../README.md) | [Ďalší: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Upozornenie**:  
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, uvedomte si, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho natívnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nezodpovedáme za akékoľvek nedorozumenia alebo zle interpretácie vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->