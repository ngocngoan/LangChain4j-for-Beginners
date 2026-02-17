# Modul 02: Prompt Engineering s GPT-5.2

## Obsah

- [Co se naučíte](../../../02-prompt-engineering)
- [Požadavky](../../../02-prompt-engineering)
- [Pochopení prompt engineeringu](../../../02-prompt-engineering)
- [Základy prompt engineeringu](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt šablony](../../../02-prompt-engineering)
- [Pokročilé vzory](../../../02-prompt-engineering)
- [Použití existujících Azure zdrojů](../../../02-prompt-engineering)
- [Snímky aplikace](../../../02-prompt-engineering)
- [Prozkoumání vzorů](../../../02-prompt-engineering)
  - [Nízká vs vysoká ochota](../../../02-prompt-engineering)
  - [Provádění úkolů (preambule nástrojů)](../../../02-prompt-engineering)
  - [Sebereflektivní kód](../../../02-prompt-engineering)
  - [Strukturovaná analýza](../../../02-prompt-engineering)
  - [Vícekolová konverzace](../../../02-prompt-engineering)
  - [Postupné uvažování krok za krokem](../../../02-prompt-engineering)
  - [Omezený výstup](../../../02-prompt-engineering)
- [Co se skutečně naučíte](../../../02-prompt-engineering)
- [Další kroky](../../../02-prompt-engineering)

## Co se naučíte

<img src="../../../translated_images/cs/what-youll-learn.c68269ac048503b2.webp" alt="Co se naučíte" width="800"/>

V předchozím modulu jste viděli, jak paměť umožňuje konverzační AI, a použili jste GitHub Models pro základní interakce. Nyní se zaměříme na to, jak klást otázky – tedy samotné prompty – s použitím Azure OpenAI a GPT-5.2. Způsob, jakým strukturalizujete prompty, zásadně ovlivňuje kvalitu odpovědí. Začneme přehledem základních technik promptingu, poté přejdeme k osmi pokročilým vzorům, které plně využívají schopnosti GPT-5.2.

Používáme GPT-5.2, protože zavádí řízení uvažování – můžete modelu říct, kolik přemýšlení má před odpovědí vykonat. To zviditelňuje různé strategie promptingu a pomáhá pochopit, kdy který přístup použít. Také využijeme výhod nižších limitů Azure pro GPT-5.2 oproti GitHub Models.

## Požadavky

- Dokončený Modul 01 (nasazené Azure OpenAI zdroje)
- Soubor `.env` v kořenovém adresáři s Azure přihlašovacími údaji (vytvořeno `azd up` v Modulu 01)

> **Poznámka:** Pokud jste Modul 01 nedokončili, nejdříve postupujte podle tamních instrukcí pro nasazení.

## Pochopení prompt engineeringu

<img src="../../../translated_images/cs/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Co je prompt engineering?" width="800"/>

Prompt engineering znamená navrhování vstupního textu, který konzistentně přináší požadované výsledky. Nejde jen o kladení otázek – jde o strukturování požadavků tak, aby model přesně pochopil, co chcete a jak to má dodat.

Představte si to jako dávání instrukcí kolegovi. „Oprav chybu“ je nejasné. „Oprav výjimku null pointer v UserService.java na řádku 45 přidáním kontroly na null“ je konkrétní. Jazykové modely fungují stejně – důležitá je konkrétnost a struktura.

<img src="../../../translated_images/cs/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Jak zapadá LangChain4j" width="800"/>

LangChain4j poskytuje infrastrukturu — připojení k modelům, paměť a typy zpráv — zatímco vzory promptů jsou jen pečlivě strukturovaný text, který tímto systémem posíláte. Klíčové stavební bloky jsou `SystemMessage` (nastavuje chování a roli AI) a `UserMessage` (nese váš skutečný požadavek).

## Základy prompt engineeringu

<img src="../../../translated_images/cs/five-patterns-overview.160f35045ffd2a94.webp" alt="Přehled pěti vzorů prompt engineeringu" width="800"/>

Než se pustíme do pokročilých vzorů tohoto modulu, připomeňme si pět základních technik promptingu. Jsou to stavební kameny, které by měl každý promptový inženýr znát. Pokud jste prošli [modul Quick Start](../00-quick-start/README.md#2-prompt-patterns), už jste je viděli v akci — zde je konceptuální rámec za nimi.

### Zero-Shot Prompting

Nejjednodušší přístup: dejte modelu přímý pokyn bez příkladů. Model spoléhá výhradně na své tréninkové znalosti, aby úkol pochopil a vykonal. Funguje to dobře u jednoduchých požadavků, kde je očekávané chování jasné.

<img src="../../../translated_images/cs/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Přímý pokyn bez příkladů — model dedukuje úkol jen z pokynu*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odpověď: "Pozitivní"
```

**Kdy použít:** Jednoduché klasifikace, přímé otázky, překlady nebo jakýkoli úkol, který model zvládne bez dalšího vedení.

### Few-Shot Prompting

Poskytněte příklady, které ukazují vzor, podle kterého má model postupovat. Model se učí očekávaný formát vstupu a výstupu z vašich příkladů a aplikuje ho na nové vstupy. To výrazně zlepšuje konzistenci u úloh, kde požadovaný formát nebo chování není zřejmé.

<img src="../../../translated_images/cs/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Učení se z příkladů — model identifikuje vzor a aplikuje ho na nové vstupy*

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

**Kdy použít:** Vlastní klasifikace, konzistentní formátování, doménově specifické úkoly nebo když jsou výsledky zero-shot nekoherentní.

### Chain of Thought

Požádejte model, aby ukázal své uvažování krok za krokem. Místo rychlého přeskoku na odpověď model rozkládá problém a explicitně řeší každý dílčí krok. To zlepšuje přesnost v matematice, logice a vícekrokovém uvažování.

<img src="../../../translated_images/cs/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Postupné uvažování — rozklad složitých problémů na konkrétní logické kroky*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model ukazuje: 15 - 8 = 7, pak 7 + 12 = 19 jablek
```

**Kdy použít:** Matematické problémy, logické rébusy, debugování nebo jakýkoli úkol, kde zobrazení procesu uvažování zvyšuje přesnost a důvěru.

### Role-Based Prompting

Nastavte AI personu nebo roli před tím, než položíte otázku. To poskytuje kontext, který ovlivní tón, hloubku a zaměření odpovědi. „Softwarový architekt“ poskytne jiné rady než „mladší vývojář“ nebo „audit bezpečnosti“.

<img src="../../../translated_images/cs/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Nastavení kontextu a role — stejná otázka dostane jinou odpověď podle přiřazené role*

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

**Kdy použít:** Kontroly kódu, doučování, doménové analýzy nebo když potřebujete odpovědi přizpůsobené úrovni odbornosti či perspektivě.

### Prompt šablony

Vytvořte znovupoužitelné prompty s proměnnými zástupnými symboly. Místo psaní nového promptu při každé příležitosti definujte šablonu jednou a do ní vkládejte různé hodnoty. Třída `PromptTemplate` v LangChain4j to usnadňuje pomocí syntaxe `{{variable}}`.

<img src="../../../translated_images/cs/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt šablony" width="800"/>

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

**Kdy použít:** Opakované dotazy s různými vstupy, dávkové zpracování, budování znovupoužitelných AI workflow nebo scénáře, kde se struktura promptu nemění, ale data ano.

---

Těchto pět základů vám dává pevný nástrojový arzenál pro většinu promptovacích úkolů. Zbytek tohoto modulu na nich staví s **osmi pokročilými vzory**, které využívají řízení uvažování GPT-5.2, sebehodnocení a možnosti strukturovaného výstupu.

## Pokročilé vzory

Po pokrytí základů přejdeme k osmi pokročilým vzorům, které tento modul činí výjimečným. Ne všechny problémy vyžadují stejný přístup. Některé otázky potřebují rychlé odpovědi, jiné hluboké zamyšlení. Některé viditelné uvažování, jiné jen výsledek. Každý níže uvedený vzor je optimalizován pro odlišný scénář — a řízení uvažování GPT-5.2 tyto rozdíly ještě více vyzdvihuje.

<img src="../../../translated_images/cs/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osm vzorů prompt engineeringu" width="800"/>

*Přehled osmi vzorů prompt engineeringu a jejich použití*

<img src="../../../translated_images/cs/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Řízení uvažování s GPT-5.2" width="800"/>

*Řízení uvažování GPT-5.2 vám umožňuje specifikovat, kolik přemýšlení má model vykonat – od rychlých přímých odpovědí až po hluboké průzkumy*

<img src="../../../translated_images/cs/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Porovnání úsilí při uvažování" width="800"/>

*Nízká ochota (rychlé, přímé) vs vysoká ochota (důkladné, průzkumné) přístupy k uvažování*

**Nízká ochota (rychlé & cílené)** - Pro jednoduché otázky, kde chcete rychlé a přímé odpovědi. Model provede minimální uvažování – maximálně 2 kroky. Použijte na výpočty, vyhledávání nebo přímočaré otázky.

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
> - „Jaký je rozdíl mezi nízko-ochotnými a vysoko-ochotnými vzory promptování?“
> - „Jak pomáhají XML značky v promptech strukturovat odpověď AI?“
> - „Kdy mám použít vzory sebereflexe vs přímé instrukce?“

**Vysoká ochota (hluboké & důkladné)** - Pro složité problémy, kde chcete komplexní analýzu. Model průzkumně zkoumá a ukazuje podrobné uvažování. Použijte pro návrhy systémů, architektonická rozhodnutí nebo komplexní výzkum.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Provádění úkolů (postupný pokrok)** - Pro vícekrokové workflow. Model nejdřív poskytne plán, popisuje každý krok během práce a nakonec dá shrnutí. Použijte pro migrace, implementace nebo jakýkoli vícekrokový proces.

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

Chain-of-Thought prompting jasně žádá model, aby ukázal proces uvažování, čímž zlepšuje přesnost u složitých úloh. Postupné rozkládání pomáhá jak lidem, tak AI lepé pochopit logiku.

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Zeptejte se na tento vzor:
> - „Jak bych přizpůsobil vzor provádění úkolů pro dlouhotrvající operace?“
> - „Jaké jsou nejlepší postupy pro strukturování předmluv nástrojů v produkčních aplikacích?“
> - „Jak zachytit a zobrazit průběžné aktualizace postupu v UI?“

<img src="../../../translated_images/cs/task-execution-pattern.9da3967750ab5c1e.webp" alt="Vzor provádění úkolů" width="800"/>

*Plán → Provádění → Shrnutí workflow pro vícekrokové úkoly*

**Sebereflektivní kód** - Pro generování kódu produkční kvality. Model generuje kód dle produkčních standardů s řádným ošetřením chyb. Použijte při vytváření nových funkcí nebo služeb.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/cs/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cyklus sebereflexe" width="800"/>

*Iterativní smyčka zlepšování – generuj, hodnot, identifikuj problémy, vylepši, opakuj*

**Strukturovaná analýza** - Pro konzistentní hodnocení. Model kontroluje kód podle pevného rámce (správnost, praxe, výkon, bezpečnost, udržovatelnost). Použijte na recenze kódu nebo hodnocení kvality.

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
> - „Jak přizpůsobit rámec analýzy pro různé typy recenzí kódu?“
> - „Jak nejlépe programově rozebrat a použít strukturovaný výstup?“
> - „Jak zajistit konzistentní úrovně závažnosti mezi různými recenzními sezeními?“

<img src="../../../translated_images/cs/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Vzor strukturované analýzy" width="800"/>

*Rámec pro konzistentní revize kódu s úrovněmi závažnosti*

**Vícekolová konverzace** - Pro rozhovory, které vyžadují kontext. Model si pamatuje předchozí zprávy a navazuje na ně. Použijte například pro interaktivní pomoc nebo složité Q&A.

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

*Jak se kontext konverzace hromadí přes několik kol až do dosažení limitu tokenů*

**Postupné uvažování krok za krokem** - Pro problémy vyžadující viditelnou logiku. Model ukazuje explicitní uvažování pro každý krok. Použijte u matematických úloh, logických hádanek nebo když potřebujete pochopit proces uvažování.

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

*Rozklad problémů na explicitní logické kroky*

**Omezený výstup** - Pro odpovědi s konkrétními požadavky na formát. Model striktně dodržuje pravidla formátu a délky. Použijte pro shrnutí nebo když potřebujete přesnou strukturu výstupu.

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

*Vynucení specifických požadavků na formát, délku a strukturu*

## Použití existujících Azure zdrojů

**Ověření nasazení:**

Zkontrolujte, zda soubor `.env` existuje v kořenovém adresáři s Azure přihlašovacími údaji (vytvořeno během Modulu 01):
```bash
cat ../.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spuštění aplikace:**

> **Poznámka:** Pokud jste již spustili všechny aplikace pomocí `./start-all.sh` z Modulu 01, tento modul už běží na portu 8083. Můžete přeskočit spouštěcí příkazy níže a jít přímo na http://localhost:8083.

**Možnost 1: Použití Spring Boot Dashboard (doporučeno pro uživatele VS Code)**

Dev kontejner obsahuje rozšíření Spring Boot Dashboard, které poskytuje vizuální rozhraní pro správu všech Spring Boot aplikací. Najdete ho v Activity Baru na levé straně VS Code (hledat ikonu Spring Boot).
Ze Spring Boot Dashboard můžete:
- Vidět všechny dostupné Spring Boot aplikace v pracovním prostoru
- Spustit/zastavit aplikace jedním kliknutím
- Prohlížet logy aplikací v reálném čase
- Monitorovat stav aplikací

Jednoduše klikněte na tlačítko přehrávání vedle "prompt-engineering" pro spuštění tohoto modulu, nebo spusťte všechny moduly najednou.

<img src="../../../translated_images/cs/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Varianta 2: Použití shell skriptů**

Spusťte všechny webové aplikace (moduly 01-04):

**Bash:**
```bash
cd ..  # Z kořenového adresáře
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Ze základního adresáře
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

Oba skripty automaticky načítají proměnné prostředí z kořenového souboru `.env` a zkompilují JARy, pokud neexistují.

> **Poznámka:** Pokud dáváte přednost manuální kompilaci všech modulů před spuštěním:
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

Otevřete si v prohlížeči http://localhost:8083.

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

## Screenshoty aplikace

<img src="../../../translated_images/cs/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Hlavní panel zobrazující všech 8 vzorů prompt engineeringu s jejich charakteristikami a použitím*

## Prozkoumání vzorů

Webové rozhraní vám umožňuje experimentovat s různými strategiemi promptů. Každý vzor řeší jiné problémy - vyzkoušejte je a uvidíte, kdy který přístup září.

### Nízká vs Vysoká Ochuzenost (Eagerness)

Zeptejte se jednoduché otázky jako "Kolik je 15 % ze 200?" s nízkou ochotou. Dostanete okamžitou, přímou odpověď. Teď položte složitější otázku jako "Navrhni cachingovou strategii pro API s vysokou návštěvností" s vysokou ochotou. Sledujte, jak model zpomalí a poskytne detailní zdůvodnění. Stejný model, stejná struktura otázky - ale prompt mu říká, kolik má myslet.

<img src="../../../translated_images/cs/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Rychlý výpočet s minimálním zdůvodněním*

<img src="../../../translated_images/cs/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Komplexní cachingová strategie (2.8MB)*

### Vykonávání úkolů (Preambly nástrojů)

Vícekrokové workflow těží z předběžného plánování a komentovaných kroků. Model popíše, co udělá, komentuje každý krok a pak shrne výsledky.

<img src="../../../translated_images/cs/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Vytvoření REST endpointu s krokovým komentářem (3.9MB)*

### Sebereflektující kód

Zkuste "Vytvoř službu pro validaci emailu". Místo pouhého generování kódu a zastavení model vytvoří, vyhodnotí podle kritérií kvality, identifikuje slabiny a vylepší. Uvidíte, jak iteruje, dokud kód nesplní produkční standardy.

<img src="../../../translated_images/cs/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Kompletní služba pro validaci emailu (5.2MB)*

### Strukturovaná analýza

Recenze kódu vyžaduje konzistentní hodnotící rámce. Model analyzuje kód podle pevných kategorií (správnost, praktiky, výkon, bezpečnost) s úrovněmi závažnosti.

<img src="../../../translated_images/cs/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Rámec pro revizi kódu*

### Vícekrokový chat

Zeptejte se "Co je Spring Boot?" a hned na to "Ukaž mi příklad". Model si pamatuje první otázku a dá vám konkrétní příklad Spring Bootu. Bez paměti by ta druhá otázka byla příliš nejasná.

<img src="../../../translated_images/cs/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Udržování kontextu přes otázky*

### Zdůvodnění krok za krokem

Vyberte matematický problém a vyzkoušejte ho jak s postupným zdůvodněním, tak s nízkou ochotou. Nízká ochota vám jen rychle dá odpověď - rychle, ale bez vysvětlení. Postupné zdůvodnění ukazuje každý výpočet a rozhodnutí.

<img src="../../../translated_images/cs/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matematický příklad s explicitními kroky*

### Omezený výstup

Když potřebujete specifické formáty nebo počet slov, tento vzor striktně vyžaduje dodržení. Vyzkoušejte generování shrnutí s přesně 100 slovy ve formátu odrážek.

<img src="../../../translated_images/cs/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Shrnutí strojového učení s kontrolou formátu*

## Co se skutečně učíte

**Úsilí o zdůvodnění mění vše**

GPT-5.2 vám umožňuje řídit výpočetní úsilí skrze prompty. Nízké úsilí znamená rychlé odpovědi s minimálním zkoumáním. Vysoké úsilí znamená, že model tráví čas hlubším myšlením. Učíte se přizpůsobit námahu složitosti úkolu – neztrácejte čas na jednoduché otázky, ale ani nespěchejte u složitých rozhodnutí.

**Struktura řídí chování**

Všimli jste si XML tagů v promptech? Není to dekorace. Modely lépe sledují strukturované instrukce než volný text. Když potřebujete vícekrokové procesy nebo složitou logiku, struktura pomáhá modelu sledovat, kde je a co přijde dál.

<img src="../../../translated_images/cs/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomie dobře strukturovaného promptu s jasnými sekcemi a XML-stylovou organizací*

**Kvalita díky sebehodnocení**

Vzory sebereflexe fungují tak, že explicitně uvádí kritéria kvality. Místo doufání, že model „to udělá správně“, mu přesně řeknete, co „správně“ znamená: správná logika, zpracování chyb, výkon, bezpečnost. Model pak může vyhodnotit svůj výstup a zlepšit ho. Transformuje generování kódu ze hry štěstí na proces.

**Kontext je omezený**

Vícekrokové konverzace fungují tím, že každému požadavku přikládají historii zpráv. Ale existuje limit - každý model má maximální počet tokenů. Jak konverzace rostou, budete potřebovat strategie, jak udržet relevantní kontext, aniž byste ho překročili. Tento modul vám ukáže, jak paměť funguje; později se naučíte, kdy shrnovat, kdy zapomenout a kdy vyhledávat.

## Další kroky

**Další modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigace:** [← Předchozí: Modul 01 - Úvod](../01-introduction/README.md) | [Zpět na hlavní](../README.md) | [Další: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Upozornění**:
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Ačkoliv usilujeme o přesnost, mějte prosím na paměti, že automatické překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho mateřském jazyce by měl být považován za autoritativní zdroj. Pro důležité informace se doporučuje profesionální lidský překlad. Nejsme odpovědní za žádné nedorozumění nebo mylné výklady vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->