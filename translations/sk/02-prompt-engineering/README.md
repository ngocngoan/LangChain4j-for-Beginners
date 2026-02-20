# Modul 02: Prompt Engineering s GPT-5.2

## Obsah

- [Čo sa naučíte](../../../02-prompt-engineering)
- [Predpoklady](../../../02-prompt-engineering)
- [Pochopenie prompt engineeringu](../../../02-prompt-engineering)
- [Základy prompt engineeringu](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Pokročilé vzory](../../../02-prompt-engineering)
- [Použitie existujúcich Azure zdrojov](../../../02-prompt-engineering)
- [Snímky obrazovky aplikácie](../../../02-prompt-engineering)
- [Preskúmanie vzorov](../../../02-prompt-engineering)
  - [Nízka vs vysoká angažovanosť](../../../02-prompt-engineering)
  - [Realizácia úloh (predslovy nástrojov)](../../../02-prompt-engineering)
  - [Samoreflektujúci kód](../../../02-prompt-engineering)
  - [Štruktúrovaná analýza](../../../02-prompt-engineering)
  - [Viackolový chat](../../../02-prompt-engineering)
  - [Postupné uvažovanie](../../../02-prompt-engineering)
  - [Obmedzený výstup](../../../02-prompt-engineering)
- [Čo sa naozaj učíte](../../../02-prompt-engineering)
- [Ďalšie kroky](../../../02-prompt-engineering)

## Čo sa naučíte

<img src="../../../translated_images/sk/what-youll-learn.c68269ac048503b2.webp" alt="Čo sa naučíte" width="800"/>

V predchádzajúcom module ste videli, ako pamäť umožňuje konverzačné AI a používali ste GitHub modely na základné interakcie. Teraz sa zameriame na to, ako kladiete otázky – samotné prompt-y – pomocou Azure OpenAI GPT-5.2. Spôsob, akým štruktúrujete prompt-y, dramaticky ovplyvňuje kvalitu odpovedí, ktoré dostanete. Začíname prehľadom základných techník promptovania, potom prejdeme k ôsmim pokročilým vzorom, ktoré plne využívajú schopnosti GPT-5.2.

Použijeme GPT-5.2, pretože zavádza kontrolu uvažovania – môžete modelu povedať, koľko uvažovania má vykonať pred odpoveďou. To robí rôzne strategie promptovania viditeľnejšími a pomáha vám pochopiť, kedy použiť každý prístup. Tiež profitujeme z menej obmedzení rýchlosti pre GPT-5.2 v porovnaní s GitHub modelmi v Azure.

## Predpoklady

- Dokončený Modul 01 (nasadené Azure OpenAI zdroje)
- Súbor `.env` v koreňovom adresári s Azure prihlasovacími údajmi (vytvorený príkazom `azd up` v Module 01)

> **Poznámka:** Ak ste nedokončili Modul 01, najprv postupujte podľa nasadenia tam.

## Pochopenie prompt engineeringu

<img src="../../../translated_images/sk/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Čo je prompt engineering?" width="800"/>

Prompt engineering je o navrhovaní vstupného textu, ktorý konzistentne prinesie požadované výsledky. Nie je to len o kladení otázok – ide o štruktúrovanie požiadaviek tak, aby model presne rozumel, čo chcete a ako to podať.

Premýšľajte o tom ako o dávaní inštrukcií kolegovi. "Oprav chybu" je nejasné. "Oprav výnimku null pointer v UserService.java na riadku 45 pridaním kontroly na null" je konkrétne. Jazykové modely fungujú rovnako – dôležitá je špecifickosť a štruktúra.

<img src="../../../translated_images/sk/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Ako zapadá LangChain4j" width="800"/>

LangChain4j poskytuje infraštruktúru – spojenia s modelom, pamäť a typy správ – zatiaľ čo vzory promptov sú len starostlivo štruktúrovaný text, ktorý cez túto infraštruktúru posielate. Kľúčové stavebné bloky sú `SystemMessage` (nastavuje správanie a rolu AI) a `UserMessage` (nesie vašu skutočnú požiadavku).

## Základy prompt engineeringu

<img src="../../../translated_images/sk/five-patterns-overview.160f35045ffd2a94.webp" alt="Prehľad piatich vzorov prompt engineeringu" width="800"/>

Predtým, než sa pustíme do pokročilých vzorov v tomto module, pozrime sa na päť základných techník promptovania. Sú to stavebné kamene, ktoré by mal poznať každý prompt inžinier. Ak ste už prešli [Rýchlym štartovacím modulom](../00-quick-start/README.md#2-prompt-patterns), videli ste ich v praxi – tu je za nimi konceptuálny rámec.

### Zero-Shot Prompting

Najjednoduchší prístup: dajte modelu priamu inštrukciu bez príkladov. Model sa spolieha úplne na svoje tréningové dáta na pochopenie a vykonanie úlohy. Funguje to dobre pre priamočiare požiadavky, kde je očakávané správanie zrejmé.

<img src="../../../translated_images/sk/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Priama inštrukcia bez príkladov – model odvádza úlohu len podľa inštrukcie*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odpoveď: „Pozitívny“
```

**Kedy použiť:** Jednoduché klasifikácie, priame otázky, preklady alebo akúkoľvek úlohu, ktorú model zvládne bez ďalších inštrukcií.

### Few-Shot Prompting

Poskytnite príklady, ktoré ukazujú vzor, ktorý chcete, aby model nasledoval. Model sa naučí očakávaný vstupno-výstupný formát z vašich príkladov a aplikuje ho na nové vstupy. To výrazne zlepšuje konzistenciu pri úlohách, kde požadovaný formát alebo správanie nie je jasné.

<img src="../../../translated_images/sk/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Učenie sa z príkladov – model identifikuje vzor a aplikuje ho na nové vstupy*

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

**Kedy použiť:** Vlastné klasifikácie, konzistentné formátovanie, doménovo špecifické úlohy alebo ak výsledky zero-shot nie sú konzistentné.

### Chain of Thought

Požiadajte model, aby zobrazil svoje uvažovanie krok za krokom. Namiesto okamžitého preskoku na odpoveď model rozloží problém a explicitne spracuje každú časť. To zlepšuje presnosť u matematických, logických a viacstupňových uvažovacích úloh.

<img src="../../../translated_images/sk/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Krok za krokom uvažovanie – rozkladanie komplexných problémov na explicitné logické kroky*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model ukazuje: 15 - 8 = 7, potom 7 + 12 = 19 jabĺk
```

**Kedy použiť:** Matematické problémy, logické hádanky, ladenie chýb alebo akákoľvek úloha, kde zobrazenie procesu uvažovania zvyšuje presnosť a dôveru.

### Role-Based Prompting

Nastavte AI personu alebo rolu pred položením otázky. Poskytuje kontext, ktorý formuje tón, hĺbku a zameranie odpovede. "Softvérový architekt" poskytne iné rady ako "junior vývojár" alebo "auditor bezpečnosti".

<img src="../../../translated_images/sk/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Nastavenie kontextu a persony – tá istá otázka dostane inú odpoveď podľa priradenej roly*

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

**Kedy použiť:** Revizie kódu, doučovanie, doménovo špecifická analýza alebo keď potrebujete odpovede prispôsobené konkrétnej úrovni odbornosti či perspektíve.

### Prompt Templates

Vytvorte znovu použiteľné prompt-y s premenlivými zástupcami. Namiesto písania nového promptu stále znova definujte šablónu a dopĺňajte rozličné hodnoty. Trieda `PromptTemplate` v LangChain4j to uľahčuje pomocou syntaxe `{{variable}}`.

<img src="../../../translated_images/sk/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Znovu použiteľné prompt-y s premenlivými zástupcami – jedna šablóna, množstvo využití*

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

**Kedy použiť:** Opakované dotazy s rôznymi vstupmi, hromadné spracovanie, budovanie znovu použiteľných AI pracovných tokov alebo scenár, kde štruktúra promptu zostáva rovnaká, len dáta sa menia.

---

Tieto päť základov vám poskytne pevný nástroj na väčšinu promptovacích úloh. Zvyšok tohto modulu na nich stavia s **osem pokročilými vzormi**, ktoré využívajú kontrolu uvažovania GPT-5.2, seba-evaluáciu a schopnosť štruktúrovaného výstupu.

## Pokročilé vzory

Po pokrytí základov prejdime k ôsmim pokročilým vzorom, ktoré robia tento modul jedinečným. Nie všetky problémy potrebujú rovnaký prístup. Niektoré otázky potrebujú rýchle odpovede, iné hlboké uvažovanie. Niektoré vyžadujú viditeľné uvažovanie, iné len výsledky. Každý nižšie uvedený vzor je optimalizovaný pre iný scenár – a kontrola uvažovania GPT-5.2 robí rozdiely ešte výraznejšími.

<img src="../../../translated_images/sk/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osem vzorov promptovania" width="800"/>

*Prehľad ôsmich vzorov prompt engineeringu a ich využitia*

<img src="../../../translated_images/sk/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontrola uvažovania s GPT-5.2" width="800"/>

*Kontrola uvažovania GPT-5.2 vám umožňuje určiť, koľko uvažovania má model vykonať – od rýchlych priamych odpovedí až po hlbokú analýzu*

**Nízka angažovanosť (rýchle a zamerané)** - Pre jednoduché otázky, kde chcete rýchle a priame odpovede. Model vykonáva minimálne uvažovanie – maximálne 2 kroky. Použite to na výpočty, vyhľadávanie alebo priame otázky.

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
> - "Aký je rozdiel medzi vzorom nízkej angažovanosti a vysokej angažovanosti?"
> - "Ako XML značky v promptoch pomáhajú štruktúrovať AI odpoveď?"
> - "Kedy by som mal použiť samoreflektujúce vzory vs priame inštrukcie?"

**Vysoká angažovanosť (hlboké a dôkladné)** - Pre komplexné problémy, kde chcete komplexnú analýzu. Model dôkladne skúma a ukazuje podrobné uvažovanie. Použite to pre návrhy systémov, rozhodnutia architektúry alebo zložité výskumy.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Realizácia úloh (postup krok za krokom)** - Pre viacstupňové pracovné postupy. Model poskytuje plán na začiatku, vysvetľuje každý krok počas práce, potom dáva zhrnutie. Použite to pre migrácie, implementácie alebo akýkoľvek viacstupňový proces.

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

Chain-of-Thought prompting explicitne žiada model, aby ukázal svoj proces uvažovania, čo zlepšuje presnosť pri komplexných úlohách. Postupné rozkladanie pomáha ľuďom aj AI pochopiť logiku.

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Spýtajte sa na tento vzor:
> - "Ako by som prispôsobil vzor realizácie úloh pre dlhodobé operácie?"
> - "Aké sú najlepšie praktiky pre štruktúrovanie predslovov nástrojov v produkčných aplikáciách?"
> - "Ako môžem zachytiť a zobraziť medzistav progresu v UI?"

<img src="../../../translated_images/sk/task-execution-pattern.9da3967750ab5c1e.webp" alt="Vzor realizácie úloh" width="800"/>

*Plán → Vykonaj → Zhrni workflow pre viacstupňové úlohy*

**Samoreflektujúci kód** - Na generovanie produkčne kvalitného kódu. Model generuje kód podľa produkčných štandardov s riadnym zaobchádzaním s chybami. Použite to pri budovaní nových funkcií alebo služieb.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sk/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cyklus samoreflexie" width="800"/>

*Iteračný cyklus zlepšovania – generuj, vyhodnocuj, identifikuj problémy, zlepšuj, opakuj*

**Štruktúrovaná analýza** - Pre konzistentné hodnotenia. Model hodnotí kód pomocou pevného rámca (správnosť, praktiky, výkon, bezpečnosť, udržiavateľnosť). Použite to na revízie kódu alebo hodnotenia kvality.

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
> - "Ako môžem prispôsobiť rámec analýzy pre rôzne typy revízií kódu?"
> - "Aký je najlepší spôsob, ako programovo spracovať a reagovať na štruktúrovaný výstup?"
> - "Ako zabezpečiť konzistentné úrovne závažnosti v rôznych revíznych sedeniach?"

<img src="../../../translated_images/sk/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Vzor štruktúrovanej analýzy" width="800"/>

*Rámec na konzistentné revízie kódu s úrovňami závažnosti*

**Viackolový chat** - Pre rozhovory, ktoré potrebujú kontext. Model si pamätá predchádzajúce správy a na ne nadväzuje. Použite to na interaktívnu pomoc alebo zložité Q&A.

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

*Ako sa konverzačný kontext hromadí počas viacerých kôl až po limit tokenov*

**Postupné uvažovanie** - Pre problémy vyžadujúce viditeľnú logiku. Model ukazuje explicitné uvažovanie pre každý krok. Použite to na matematické problémy, logické hádanky alebo keď chcete pochopiť proces uvažovania.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sk/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Vzor postupného uvažovania" width="800"/>

*Rozkladanie problémov na explicitné logické kroky*

**Obmedzený výstup** - Pre odpovede so špecifickými požiadavkami na formát. Model striktne dodržiava pravidlá formátu a dĺžky. Použite to na zhrnutia alebo keď potrebujete presnú štruktúru výstupu.

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

Uistite sa, že súbor `.env` existuje v koreňovom adresári s Azure prihlasovacími údajmi (vytvorený počas Modulu 01):
```bash
cat ../.env  # Mala by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spustenie aplikácie:**

> **Poznámka:** Ak ste už spustili všetky aplikácie príkazom `./start-all.sh` z Modulu 01, tento modul už beží na porte 8083. Môžete preskočiť príkazy na spustenie nižšie a prejsť priamo na http://localhost:8083.

**Možnosť 1: Použitie Spring Boot Dashboard (odporúčané pre používateľov VS Code)**

Dev kontajner obsahuje rozšírenie Spring Boot Dashboard, ktoré poskytuje vizuálne rozhranie na správu všetkých Spring Boot aplikácií. Nájdete ho v Aktivity Paneli na ľavej strane VS Code (hľadajte ikonku Spring Boot).

Zo Spring Boot Dashboard môžete:
- Vidieť všetky dostupné Spring Boot aplikácie v pracovnom priestore
- Jedným kliknutím spustiť/zastaviť aplikácie
- Zobraziť logy aplikácií v reálnom čase
- Monitorovať stav aplikácií
Jednoducho kliknite na tlačidlo prehrávania vedľa "prompt-engineering" na spustenie tohto modulu alebo spustite všetky moduly naraz.

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

Alebo spustite iba tento modul:

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

Oba skripty automaticky načítajú premenné prostredia zo súboru `.env` v koreňovom adresári a vytvoria JAR súbory, ak neexistujú.

> **Poznámka:** Ak chcete najprv manuálne zostaviť všetky moduly pred spustením:
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
.\stop.ps1  # Len tento modul
# Alebo
cd ..; .\stop-all.ps1  # Všetky moduly
```

## Snímky aplikácie

<img src="../../../translated_images/sk/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Hlavná obrazovka s prehľadom všetkých 8 vzorov prompt engineering s ich charakteristikami a prípadmi použitia*

## Preskúmanie vzorov

Webové rozhranie vám umožňuje experimentovať s rôznymi stratégiami promptovania. Každý vzor rieši iné problémy – vyskúšajte ich a zistite, kedy ktorý prístup vynikne.

> **Poznámka: Streamovanie vs Nestreamovanie** — Každá stránka vzoru ponúka dve tlačidlá: **🔴 Stream Response (Live)** a možnosť **Nestreamovania**. Streamovanie používa Server-Sent Events (SSE) na zobrazenie tokenov v reálnom čase, ako model generuje odpoveď, takže vidíte pokrok okamžite. Nestreamová možnosť počká na celú odpoveď pred jej zobrazením. Pri promptoch vyžadujúcich hlboké uvažovanie (napr. High Eagerness, Self-Reflecting Code) môže nestreamový hovor trvať veľmi dlho – niekedy minúty – bez žiadnej viditeľnej odozvy. **Používajte streamovanie pri experimentoch s komplexnými promptmi**, aby ste videli, ako model pracuje, a zabránili dojmu, že požiadavka vypršala.
>
> **Poznámka: Požiadavka na prehliadač** — Funkcia streamovania využíva Fetch Streams API (`response.body.getReader()`), ktoré vyžaduje plnohodnotný prehliadač (Chrome, Edge, Firefox, Safari). Nepracuje vo VS Code zabudovanom Simple Browser, pretože jeho webview nepodporuje ReadableStream API. Ak používate Simple Browser, tlačidlá nestreamovania budú fungovať normálne – ovplyvnené sú len streamovacie tlačidlá. Pre plný zážitok otvorte `http://localhost:8083` v externom prehliadači.

### Nízka vs Vysoká ochota (Eagerness)

Opýtajte sa jednoduchú otázku ako "Čo je 15 % zo 200?" pomocou Nízkej ochoty. Dostanete okamžitú, priamu odpoveď. Teraz sa opýtajte niečo zložité ako "Navrhnite stratégiu cachovania pre API s vysokou záťažou" pomocou Vysokej ochoty. Kliknite na **🔴 Stream Response (Live)** a sledujte, ako sa podrobné uvažovanie modelu objavuje token po tokene. Rovnaký model, rovnaká štruktúra otázky – ale prompt mu hovorí, koľko premýšľania má vykonať.

### Vykonávanie úloh (Náznaky na nástroje)

Viacstupňové pracovné postupy profitujú z predbežného plánovania a komentovania priebehu. Model načrtáva, čo spraví, komentuje každý krok a potom zhrnie výsledky.

### Sebareflektujúci kód

Vyskúšajte „Vytvor službu na validáciu e-mailov“. Namiesto generovania kódu a ukončenia generuje model, vyhodnocuje ho podľa kritérií kvality, identifikuje slabiny a zlepšuje ho. Uvidíte, ako iteruje, až kým kód nespĺňa produkčné štandardy.

### Štruktúrovaná analýza

Kontroly kódu potrebujú konzistentné vyhodnocovacie rámce. Model analyzuje kód podľa pevných kategórií (správnosť, praktiky, výkon, bezpečnosť) a úrovní závažnosti.

### Viackolové rozhovory

Opýtajte sa „Čo je Spring Boot?“ a hneď pokračujte s „Ukáž mi príklad“. Model si pamätá prvú otázku a poskytne konkrétny príklad Spring Boot. Bez pamäti by druhá otázka bola príliš nejasná.

### Postupné uvažovanie

Vyberte si matematický problém a vyskúšajte ho s Postupným uvažovaním a Nízku ochotou. Nízka ochota vám len rýchlo odpovie - rýchlo, ale nejasne. Postupné uvažovanie vám ukáže každý výpočet a rozhodnutie.

### Obmedzený výstup

Keď potrebujete špecifické formáty alebo počet slov, tento vzor vynucuje prísne dodržiavanie pravidiel. Skúste vygenerovať zhrnutie s presne 100 slovami v bodoch.

## Čo sa skutočne učíte

**Námaha pri uvažovaní mení všetko**

GPT-5.2 vám umožňuje ovládať výpočtovú námahu prostredníctvom vašich promptov. Nízkou námahou sa dosahujú rýchle odpovede s minimálnym prieskumom. Vysokou námahou model venuje čas hlbokému uvažovaniu. Učíte sa prispôsobiť námahu zložitosti úlohy – nestrácajte čas na jednoduché otázky, ale ani sa neponáhľajte s komplexnými rozhodnutiami.

**Štruktúra riadi správanie**

Všimli ste si XML značky v promptoch? Nie sú len dekoratívne. Modely spĺňajú usmernenia so štruktúrovanými inštrukciami spoľahlivejšie než voľný text. Keď potrebujete viacstupňové procesy alebo komplexnú logiku, štruktúra pomáha modelu sledovať, kde je a čo príde ďalej.

<img src="../../../translated_images/sk/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatómia dobre štruktúrovaného promptu s jasnými sekciami a XML-štýlovou organizáciou*

**Kvalita cez seba-hodnotenie**

Sebareflektujúce vzory fungujú tak, že robia kritériá kvality explicitnými. Namiesto nádeje, že model „to spraví správne“, mu presne poviete, čo znamená „správne“: správna logika, spracovanie chýb, výkon, bezpečnosť. Model potom dokáže vyhodnotiť vlastný výstup a zdokonaliť ho. Toto premieňa generovanie kódu z lotérie na proces.

**Kontext je konečný**

Viackolové rozhovory fungujú tak, že k žiadosti sa pripojí história správ. Ale je tu limit – každý model má maximálny počet tokenov. Ako rozhovory rastú, budete potrebovať stratégie na udržanie relevantného kontextu bez prekročenia limitu. Tento modul vám ukáže, ako pamäť funguje; neskôr sa naučíte, kedy zhrnúť, kedy zabudnúť a kedy vyhľadať.

## Ďalšie kroky

**Ďalší modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigácia:** [← Predchádzajúci: Modul 01 - Úvod](../01-introduction/README.md) | [Späť na hlavnú stránku](../README.md) | [Ďalší: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vyučenie zodpovednosti**:  
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, majte prosím na pamäti, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho rodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kľúčové informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za akékoľvek nedorozumenia alebo nesprávne výklady vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->