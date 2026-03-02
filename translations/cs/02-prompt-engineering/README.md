# Modul 02: Prompt Engineering s GPT-5.2

## Obsah

- [Video průvodce](../../../02-prompt-engineering)
- [Co se naučíte](../../../02-prompt-engineering)
- [Předpoklady](../../../02-prompt-engineering)
- [Pochopení prompt engineeringu](../../../02-prompt-engineering)
- [Základy prompt engineeringu](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Šablony promptů](../../../02-prompt-engineering)
- [Pokročilé vzory](../../../02-prompt-engineering)
- [Použití existujících Azure zdrojů](../../../02-prompt-engineering)
- [Snímky obrazovky aplikace](../../../02-prompt-engineering)
- [Prozkoumání vzorů](../../../02-prompt-engineering)
  - [Nízká vs vysoká horlivost](../../../02-prompt-engineering)
  - [Provádění úloh (úvody k nástrojům)](../../../02-prompt-engineering)
  - [Sebe-reflexivní kód](../../../02-prompt-engineering)
  - [Strukturovaná analýza](../../../02-prompt-engineering)
  - [Vícekolový chat](../../../02-prompt-engineering)
  - [Krok za krokem uvažování](../../../02-prompt-engineering)
  - [Omezený výstup](../../../02-prompt-engineering)
- [Co opravdu získáváte](../../../02-prompt-engineering)
- [Další kroky](../../../02-prompt-engineering)

## Video průvodce

Sledujte živou relaci, která vysvětluje, jak začít s tímto modulem:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Co se naučíte

<img src="../../../translated_images/cs/what-youll-learn.c68269ac048503b2.webp" alt="Co se naučíte" width="800"/>

V předchozím modulu jste viděli, jak paměť umožňuje konverzační AI a použili GitHub Models pro základní interakce. Nyní se zaměříme na to, jak klást otázky — tedy samotné prompty — pomocí Azure OpenAI GPT-5.2. Způsob, jakým strukturalizujete své prompty, zásadně ovlivňuje kvalitu odpovědí, které získáte. Začneme přehledem základních technik promptování a poté přejdeme k osmi pokročilým vzorům, které plně využívají schopnosti GPT-5.2.

Použijeme GPT-5.2, protože zavádí kontrolu uvažování – můžete modelu říct, kolik přemýšlení má před odpovědí udělat. To činí různé strategie promptování zřetelnějšími a pomáhá pochopit, kdy kterou použít. Také využijeme nižší omezení rychlosti pro GPT-5.2 na Azure v porovnání s GitHub Models.

## Předpoklady

- Dokončený Modul 01 (nasazení Azure OpenAI zdrojů)
- `.env` soubor v kořenovém adresáři s Azure přihlašovacími údaji (vytvořeno pomocí `azd up` v Modulu 01)

> **Poznámka:** Pokud jste Modul 01 nedokončili, nejdříve postupujte podle jeho pokynů k nasazení.

## Pochopení prompt engineeringu

<img src="../../../translated_images/cs/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Co je prompt engineering?" width="800"/>

Prompt engineering spočívá v návrhu vstupního textu, který vám konzistentně přinese potřebné výsledky. Nejde jen o kladení otázek — jde o strukturování požadavků tak, aby model přesně pochopil, co chcete a jak to dodat.

Představte si to jako dávání pokynů kolegovi. "Oprav chybu" je vágní. "Oprav null pointer výjimku v UserService.java na řádku 45 přidáním kontroly null" je konkrétní. Jazykové modely fungují stejně – záleží na konkrétnosti a struktuře.

<img src="../../../translated_images/cs/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Jak LangChain4j zapadá" width="800"/>

LangChain4j poskytuje infrastrukturu — propojení s modelem, paměť a typy zpráv — zatímco promptové vzory jsou jen pečlivě strukturovaný text, který jím posíláte. Klíčovými stavebními bloky jsou `SystemMessage` (nastavuje chování a roli AI) a `UserMessage` (nese váš skutečný požadavek).

## Základy prompt engineeringu

<img src="../../../translated_images/cs/five-patterns-overview.160f35045ffd2a94.webp" alt="Přehled pěti vzorů prompt engineeringu" width="800"/>

Než se pustíme do pokročilých vzorů v tomto modulu, shrňme si pět základních technik promptování. Jsou to stavební kameny, které by měl znát každý prompt inženýr. Pokud jste již prošli [Quick Start modulem](../00-quick-start/README.md#2-prompt-patterns), viděli jste je už v praxi — zde je konceptuální rámec.

### Zero-Shot Prompting

Nejpřímější přístup: dejte modelu přímo instrukci bez příkladů. Model je zcela odkázán na své tréninky, aby pochopil a provedl úlohu. Funguje to dobře pro jednoduché požadavky, kde je očekávané chování jasné.

<img src="../../../translated_images/cs/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Přímá instrukce bez příkladů — model odvozuje úlohu jen z instrukce*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odpověď: "Pozitivní"
```

**Kdy použít:** Jednoduché klasifikace, přímé otázky, překlady nebo jakákoli úloha, kterou model zvládne bez dalšího vedení.

### Few-Shot Prompting

Poskytněte příklady, které ukazují vzor, podle kterého má model postupovat. Model se učí očekávaný vstup-výstup formát z vašich příkladů a aplikuje ho na nové vstupy. To výrazně zlepšuje konzistenci u úloh, kde není formát nebo chování zřejmé.

<img src="../../../translated_images/cs/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Učení z příkladů — model identifikuje vzor a aplikuje ho na nové vstupy*

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

**Kdy použít:** Vlastní klasifikace, konzistentní formátování, specifické doménové úlohy, nebo když jsou výsledky zero-shot nepřesné.

### Chain of Thought

Požádejte model, aby ukázal své uvažování krok za krokem. Místo skoku rovnou k odpovědi model rozebírá problém a pracuje s jednotlivými částmi explicitně. To zlepšuje přesnost u matematických, logických a vícestupňových úloh.

<img src="../../../translated_images/cs/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Krok za krokem uvažování — rozklad složitých problémů do explicitních logických kroků*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model ukazuje: 15 - 8 = 7, pak 7 + 12 = 19 jablek
```

**Kdy použít:** Matematické problémy, logické hádanky, debugging nebo jakákoli úloha, kde zobrazení uvažovacího procesu zvyšuje přesnost a důvěru.

### Role-Based Prompting

Než položíte otázku, nastavte AI personu nebo roli. Ta poskytuje kontext, který formuje tón, hloubku a zaměření odpovědi. „Softwarový architekt“ dává jiné rady než „junior developer“ nebo „security auditor“.

<img src="../../../translated_images/cs/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Nastavení kontextu a persony — stejná otázka dostane odlišnou odpověď podle přiřazené role*

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

**Kdy použít:** Revize kódu, doučování, specifická doménová analýza, nebo když potřebujete odpovědi přizpůsobené určité úrovni odbornosti či pohledu.

### Šablony promptů

Vytvářejte znovupoužitelné prompty s proměnnými zástupnými symboly. Místo psaní nového promptu pokaždé definujte šablonu jednou a vyplňte různé hodnoty. Třída `PromptTemplate` v LangChain4j to usnadňuje pomocí syntaxe `{{variable}}`.

<img src="../../../translated_images/cs/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Znovupoužitelné prompty s proměnnými — jedna šablona, mnoho použití*

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

**Kdy použít:** Opakované dotazy s různými vstupy, dávkové zpracování, budování opakovatelných AI workflow nebo scénáře, kde struktura promptu zůstává stejná, mění se jen data.

---

Těchto pět základů vám dává pevný nástrojový rámec pro většinu promptovacích úloh. Zbytek tohoto modulu na ně navazuje s **osmou pokročilými vzory** využívajícími řízení uvažování GPT-5.2, sebehodnocení a schopnosti strukturovaného výstupu.

## Pokročilé vzory

Po zvládnutí základů přejdeme k osmi pokročilým vzorům, které tento modul činí jedinečným. Ne všechny problémy vyžadují stejný přístup. Některé otázky potřebují rychlé odpovědi, jiné hluboké přemýšlení. Některé vyžadují viditelné uvažování, jiné jen výsledky. Každý vzor níže je optimalizován pro jiný scénář — a řízení uvažování GPT-5.2 tyto rozdíly ještě zřetelněji předvádí.

<img src="../../../translated_images/cs/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osm vzorů promptování" width="800"/>

*Přehled osmi vzorů prompt engineeringu a jejich použití*

<img src="../../../translated_images/cs/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Řízení uvažování s GPT-5.2" width="800"/>

*Řízení uvažování GPT-5.2 vám umožní specifikovat, kolik přemýšlení by model měl udělat — od rychlých přímých odpovědí po hluboký průzkum*

**Nízká horlivost (rychlé a zaměřené)** - Pro jednoduché otázky, kde chcete rychlé a přímé odpovědi. Model dělá minimální uvažování – maximálně 2 kroky. Použijte u výpočtů, hledání nebo přímočarých otázek.

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

> 💡 **Prozkoumejte s GitHub Copilot:** Otevřete [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) a zeptejte se:
> - „Jaký je rozdíl mezi nízkou a vysokou horlivostí u vzorů promptování?“
> - „Jak XML tagy v promptech pomáhají strukturovat odpověď AI?“
> - „Kdy použít vzory sebe-reflexe vs přímé instrukce?“

**Vysoká horlivost (hluboké a důkladné)** - Pro složité problémy, kde chcete komplexní analýzu. Model prozkoumává důkladně a ukazuje podrobné uvažování. Použijte u návrhu systémů, architektonických rozhodnutí nebo složitého výzkumu.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Provádění úloh (postup krok za krokem)** - Pro vícestupňové workflow. Model poskytne plán předem, vysvětluje každý krok při práci a pak shrne. Použijte u migrací, implementací nebo jakéhokoli vícestupňového procesu.

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

Promptování pomocí Chain-of-Thought explicitně žádá model, aby ukázal svůj uvažovací proces, což zlepšuje přesnost u složitých úloh. Krok za krokem rozklad pomáhá pochopit logiku jak lidem, tak AI.

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Zeptejte se na tento vzor:
> - „Jak bych adaptoval vzor provádění úloh pro dlouhodobé operace?“
> - „Jaké jsou osvědčené postupy pro strukturování úvodu k nástrojům v produkčních aplikacích?“
> - „Jak mohu zachytit a zobrazit průběžné aktualizace postupu v uživatelském rozhraní?“

<img src="../../../translated_images/cs/task-execution-pattern.9da3967750ab5c1e.webp" alt="Vzor provádění úloh" width="800"/>

*Workflow Plán → Provedení → Shrnutí pro vícestupňové úlohy*

**Sebe-reflexivní kód** - Pro generování produkčního kódu. Model generuje kód podle produkčních standardů s náležitou správou chyb. Použijte při budování nových funkcí nebo služeb.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/cs/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cyklus sebe-reflexe" width="800"/>

*Iterační smyčka zlepšování - generuj, vyhodnoť, identifikuj problémy, zlepši, opakuj*

**Strukturovaná analýza** - Pro konzistentní hodnocení. Model reviduje kód podle pevného rámce (správnost, praktiky, výkon, bezpečnost, udržovatelnost). Použijte na revize kódu nebo hodnocení kvality.

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

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Zeptejte se na strukturovanou analýzu:
> - „Jak mohu přizpůsobit rámec analýzy pro různé typy revizí kódu?“
> - „Jak nejlépe zpracovat a programově využít strukturovaný výstup?“
> - „Jak zajistit konzistentní úrovně závažnosti v různých sezeních revize?“

<img src="../../../translated_images/cs/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Vzor strukturované analýzy" width="800"/>

*Rámec pro konzistentní revize kódu s úrovněmi závažnosti*

**Vícekolový chat** - Pro konverzace vyžadující kontext. Model si pamatuje předchozí zprávy a staví na nich. Použijte na interaktivní pomoc nebo složité otázky a odpovědi.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/cs/context-memory.dff30ad9fa78832a.webp" alt="Paměť kontextu" width="800"/>

*Jak se kontext konverzace hromadí během více kol, až do dosažení limitu tokenů*

**Krok za krokem uvažování** - Pro problémy vyžadující viditelnou logiku. Model ukazuje explicitní uvažování u každého kroku. Použijte na matematické problémy, logické hádanky nebo kdykoliv potřebujete pochopit myšlenkový proces.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/cs/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Vzor krok za krokem" width="800"/>

*Rozklad problémů do explicitních logických kroků*

**Omezený výstup** - Pro odpovědi s konkrétními formátovými požadavky. Model přísně dodržuje pravidla formátu a délky. Použijte na shrnutí nebo kdy potřebujete přesnou strukturu výstupu.

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

<img src="../../../translated_images/cs/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Vzor omezeného výstupu" width="800"/>

*Vynucování specifických požadavků na formát, délku a strukturu*

## Použití existujících Azure zdrojů

**Ověření nasazení:**

Ujistěte se, že `.env` soubor existuje v kořenovém adresáři s Azure přihlašovacími údaji (vytvořený během Modulu 01):
```bash
cat ../.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spuštění aplikace:**

> **Poznámka:** Pokud jste již spustili všechny aplikace pomocí `./start-all.sh` z Modulu 01, tento modul už běží na portu 8083. Můžete přeskočit startovací příkazy níže a přejít přímo na http://localhost:8083.
**Možnost 1: Použití Spring Boot Dashboard (doporučeno pro uživatele VS Code)**

Vývojové prostředí obsahuje rozšíření Spring Boot Dashboard, které poskytuje vizuální rozhraní pro správu všech aplikací Spring Boot. Najdete ho na liště aktivit vlevo ve VS Code (hledejte ikonu Spring Boot).

Ze Spring Boot Dashboard můžete:
- Vidět všechny dostupné aplikace Spring Boot v pracovním prostoru
- Spustit/zastavit aplikace jedním kliknutím
- Zobrazit protokoly aplikací v reálném čase
- Sledovat stav aplikací

Jednoduše klikněte na tlačítko přehrávání vedle „prompt-engineering“ pro spuštění tohoto modulu, nebo spusťte všechny moduly najednou.

<img src="../../../translated_images/cs/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Možnost 2: Použití shell skriptů**

Spuštění všech webových aplikací (moduly 01-04):

**Bash:**
```bash
cd ..  # Z kořenového adresáře
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Z kořenového adresáře
.\start-all.ps1
```

Nebo spusťte jen tento modul:

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

Oba skripty automaticky načítají proměnné prostředí ze souboru `.env` v kořenovém adresáři a sestaví JARy, pokud ještě neexistují.

> **Poznámka:** Pokud chcete všechny moduly nejprve manuálně sestavit před spuštěním:
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

Otevřete http://localhost:8083 ve svém prohlížeči.

**Pro zastavení:**

**Bash:**
```bash
./stop.sh  # Pouze tento modul
# Nebo
cd .. && ./stop-all.sh  # Všechny moduly
```

**PowerShell:**
```powershell
.\stop.ps1  # Pouze tento modul
# Nebo
cd ..; .\stop-all.ps1  # Všechny moduly
```

## Snímky obrazovky aplikace

<img src="../../../translated_images/cs/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Hlavní dashboard zobrazující všech 8 vzorů prompt engineering s jejich charakteristikami a použitím*

## Prozkoumávání vzorů

Webové rozhraní vám umožňuje experimentovat s různými strategiemi promptování. Každý vzor řeší různé problémy – vyzkoušejte je a zjistěte, kdy který přístup funguje nejlépe.

> **Poznámka: Streaming vs Nestringové zpracování** — Každá stránka vzoru nabízí dvě tlačítka: **🔴 Stream Response (Live)** a možnost **Non-streaming**. Streaming používá Server-Sent Events (SSE) k zobrazení tokenů v reálném čase během generování modelem, takže okamžitě vidíte průběh. Non-streaming čeká na celou odpověď, než ji zobrazí. U promptů, které vyžadují hluboké uvažování (např. High Eagerness, Self-Reflecting Code), může non-streaming volání trvat velmi dlouho — někdy minuty — bez viditelné zpětné vazby. **Při experimentování s komplexními prompty používejte streaming**, abyste viděli práci modelu a vyhnuli se dojmu, že požadavek vypršel.
>
> **Poznámka: Požadavek na prohlížeč** — Funkce streamingu používá Fetch Streams API (`response.body.getReader()`), které vyžaduje plnohodnotný prohlížeč (Chrome, Edge, Firefox, Safari). Nepracuje ve vestavěném Simple Browser ve VS Code, protože jeho webové zobrazení nepodporuje API ReadableStream. Pokud používáte Simple Browser, tlačítka non-streaming budou fungovat normálně — pouze tlačítka streaming jsou ovlivněna. Otevřete `http://localhost:8083` v externím prohlížeči pro plný zážitek.

### Nízká vs Vysoká Eagerness

Zeptejte se jednoduchou otázku, například „Co je 15 % z 200?“ s nízkou eagerností. Dostanete okamžitou, přímou odpověď. Nyní se zeptejte na něco složitého, například „Navrhni strategii kešování pro API s vysokým zatížením“ s vysokou eagerností. Klikněte na **🔴 Stream Response (Live)** a sledujte podrobné uvažování modelu, které se zobrazuje token po tokenu. Stejný model, stejná struktura otázky – ale prompt mu říká, kolik má přemýšlet.

### Provádění úkolů (Tool Preambles)

Vícekrokové pracovní postupy těží z předběžného plánování a komentování průběhu. Model načrtne, co bude dělat, komentuje každý krok a nakonec shrne výsledky.

### Self-Reflecting Code

Vyzkoušejte „Vytvoř službu pro validaci e-mailu“. Místo pouhého generování kódu a zastavení model generuje, hodnotí podle kvalitativních kritérií, identifikuje slabiny a zlepšuje kód. Uvidíte, jak iteruje, dokud kód nesplní produkční standardy.

### Strukturovaná analýza

Kontroly kódu vyžadují konzistentní hodnotící rámec. Model analyzuje kód podle pevných kategorií (správnost, praktiky, výkon, bezpečnost) s úrovněmi závažnosti.

### Vícekroková konverzace (Multi-Turn Chat)

Zeptejte se „Co je Spring Boot?“ a hned poté pokračujte otázkou „Ukaž mi příklad“. Model si pamatuje první otázku a poskytně vám konkrétní příklad Spring Bootu. Bez paměti by druhá otázka byla příliš obecná.

### Krok za krokem (Step-by-Step Reasoning)

Vyberte matematický problém a vyzkoušejte ho s krokovým uvažováním i nízkou eagerností. Nízká eagernost dává odpověď rychle, ale nejasně. Krokové uvažování ukazuje každý výpočet a rozhodnutí.

### Omezující výstup (Constrained Output)

Pokud potřebujete specifické formáty nebo počet slov, tento vzor vynucuje přesné dodržení. Vyzkoušejte generování souhrnu přesně s 100 slovy v odrážkovém formátu.

## Co se skutečně učíte

**Úsilí při uvažování mění vše**

GPT-5.2 vám umožňuje kontrolovat výpočetní úsilí pomocí promptů. Nízké úsilí znamená rychlé odpovědi s minimálním průzkumem. Vysoké úsilí znamená, že model si dává čas na hluboké přemýšlení. Učíte se přizpůsobit úsilí složitosti úkolu – neztrácejte čas s jednoduchými otázkami, ale ani nespěchejte u složitých rozhodnutí.

**Struktura řídí chování**

Všimli jste si XML značek v promptech? Nejsou jen ozdobou. Modely spolehlivěji následují strukturované instrukce než volný text. Když potřebujete vícekrokové procesy nebo komplexní logiku, struktura pomáhá modelu sledovat, kde je a co přijde dál.

<img src="../../../translated_images/cs/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomie dobře strukturovaného promptu s jasnými sekcemi a XML-style organizací*

**Kvalita skrze sebehodnocení**

Self-reflecting vzory fungují tak, že činí kritéria kvality explicitními. Místo toho, abyste doufali, že model „to udělá správně“, přesně mu řeknete, co znamená „správně“: správná logika, zpracování chyb, výkon, bezpečnost. Model pak může vyhodnotit svůj vlastní výstup a zlepšit ho. To proměňuje generování kódu z loterie na proces.

**Kontext je omezený**

Vícekrokové konverzace fungují tak, že každému požadavku přikládají historii zpráv. Ale existuje limit – každý model má maximální počet tokenů. Jak konverzace rostou, budete potřebovat strategie, jak udržet relevantní kontext bez překročení limitu. Tento modul vám ukáže, jak paměť funguje; později se naučíte, kdy shrnout, kdy zapomenout a kdy vyhledat.

## Další kroky

**Další modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigace:** [← Předchozí: Modul 01 - Úvod](../01-introduction/README.md) | [Zpět na hlavní stránku](../README.md) | [Další: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Upozornění**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Ačkoliv usilujeme o přesnost, mějte prosím na paměti, že automatické překlady mohou obsahovat chyby či nepřesnosti. Originální dokument v jeho původním jazyce by měl být považován za rozhodující zdroj. Pro zásadní informace doporučujeme profesionální lidský překlad. Nejsme odpovědní za jakékoli nedorozumění či nesprávné výklady vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->