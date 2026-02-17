# Modul 02: Prompt Engineering s GPT-5.2

## Obsah

- [Co se naučíte](../../../02-prompt-engineering)
- [Požadavky](../../../02-prompt-engineering)
- [Pochopení Prompt Engineeringu](../../../02-prompt-engineering)
- [Základy Prompt Engineeringu](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Šablony promptů](../../../02-prompt-engineering)
- [Pokročilé vzory](../../../02-prompt-engineering)
- [Použití existujících zdrojů Azure](../../../02-prompt-engineering)
- [Snímky obrazovky aplikace](../../../02-prompt-engineering)
- [Prozkoumání vzorů](../../../02-prompt-engineering)
  - [Nízká vs vysoká ochota](../../../02-prompt-engineering)
  - [Provádění úkolů (úvody nástrojů)](../../../02-prompt-engineering)
  - [Sebereflektivní kód](../../../02-prompt-engineering)
  - [Strukturovaná analýza](../../../02-prompt-engineering)
  - [Vícekolové chatování](../../../02-prompt-engineering)
  - [Postupné uvažování](../../../02-prompt-engineering)
  - [Omezený výstup](../../../02-prompt-engineering)
- [Co se opravdu naučíte](../../../02-prompt-engineering)
- [Další kroky](../../../02-prompt-engineering)

## Co se naučíte

<img src="../../../translated_images/cs/what-youll-learn.c68269ac048503b2.webp" alt="Co se naučíte" width="800"/>

V předchozím modulu jste viděli, jak paměť umožňuje konverzační AI, a použili jste GitHub Models pro základní interakce. Nyní se zaměříme na to, jak pokládat otázky — tedy samotné prompty — pomocí GPT-5.2 od Azure OpenAI. Způsob, jakým strukturalizujete prompty, výrazně ovlivňuje kvalitu odpovědí, které získáte. Začneme přehledem základních technik promptování, poté přejdeme k osmi pokročilým vzorům, které plně využívají schopnosti GPT-5.2.

Použijeme GPT-5.2, protože zavádí řízení uvažování — můžete modelu říct, kolik by měl před odpovědí přemýšlet. To činí jednotlivé strategie promptování zřetelnějšími a pomáhá pochopit, kdy kterou použít. Také využijeme méně omezení rychlosti Azure pro GPT-5.2 ve srovnání s GitHub Models.

## Požadavky

- Dokončen modul 01 (nasazeny zdroje Azure OpenAI)
- `.env` soubor v hlavním adresáři s Azure přihlašovacími údaji (vytvořený `azd up` v modulu 01)

> **Poznámka:** Pokud jste modul 01 nedokončili, nejprve postupujte podle tamních pokynů k nasazení.

## Pochopení Prompt Engineeringu

<img src="../../../translated_images/cs/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Co je Prompt Engineering?" width="800"/>

Prompt engineering je o navrhování vstupního textu, který vám konzistentně přinese požadované výsledky. Nejde jen o kladení otázek — jde o strukturování požadavků tak, aby model přesně chápal, co chcete a jak to dodat.

Představte si to jako dávání pokynů kolegovi. „Oprav chybu“ je nejasné. „Oprav výjimku null pointer v UserService.java na řádku 45 přidáním kontroly na null“ je konkrétní. Jazykové modely fungují stejně — konkrétnost a struktura jsou důležité.

<img src="../../../translated_images/cs/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Jak zapadá LangChain4j" width="800"/>

LangChain4j poskytuje infrastrukturu — propojení modelů, paměť a typy zpráv — zatímco vzory promptů jsou jen pečlivě strukturovaný text, který přes tu infrastrukturu posíláte. Klíčovými stavebními kameny jsou `SystemMessage` (nastavuje chování a roli AI) a `UserMessage` (který nese váš skutečný požadavek).

## Základy Prompt Engineeringu

<img src="../../../translated_images/cs/five-patterns-overview.160f35045ffd2a94.webp" alt="Přehled pěti vzorů Prompt Engineering" width="800"/>

Než se pustíme do pokročilých vzorů v tomto modulu, projděme si pět základních technik promptování. Jsou to stavební bloky, které by měl každý prompt engineer znát. Pokud jste již pracovali s [rychlým startem](../00-quick-start/README.md#2-prompt-patterns), tyto jste už viděli v akci — zde je koncepční rámec za nimi.

### Zero-Shot Prompting

Nejjednodušší přístup: dejte modelu přímý pokyn bez příkladů. Model spoléhá zcela na své školení, aby úkol pochopil a provedl. Funguje to dobře u přímočarých požadavků, kde je očekávané chování jasné.

<img src="../../../translated_images/cs/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Přímý pokyn bez příkladů — model vyvozuje úkol pouze z instrukce*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odezva: "Pozitivní"
```

**Kdy použít:** Jednoduché klasifikace, přímé otázky, překlady nebo jakýkoliv úkol, který model zvládne bez dalšího vedení.

### Few-Shot Prompting

Dodáte příklady, které ukazují vzor, jímž má model řídit odpověď. Model se z příkladů naučí očekávaný formát vstupu a výstupu a aplikuje jej na nové vstupy. To výrazně zvyšuje konzistenci u úkolů, kde požadovaný formát nebo chování není zřejmé.

<img src="../../../translated_images/cs/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Učení se z příkladů — model identifikuje vzor a aplikujte jej na nové vstupy*

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

**Kdy použít:** Vlastní klasifikace, konzistentní formátování, úkoly specifické pro doménu nebo když jsou výsledky zero-shot nekonzistentní.

### Chain of Thought

Požádejte model, aby ukázal své uvažování krok za krokem. Místo okamžité odpovědi model rozebere problém a každou část explicitně zpracuje. To zlepšuje přesnost u matematických, logických a vícekrokových úloh.

<img src="../../../translated_images/cs/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Postupné uvažování — rozdělení složitých problémů do explicitních logických kroků*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model ukazuje: 15 - 8 = 7, pak 7 + 12 = 19 jablek
```

**Kdy použít:** Matematické příklady, logické hádanky, ladění kódu nebo jakýkoliv úkol, kde zobrazení uvažovacího procesu zvyšuje přesnost a důvěru.

### Role-Based Prompting

Nastavte AI roli nebo personu před položením otázky. To poskytuje kontext, který formuje tón, hloubku a zaměření odpovědi. "Software architect" dává jiné rady než "junior developer" nebo "security auditor".

<img src="../../../translated_images/cs/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Nastavení kontextu a persony — stejná otázka dostane různou odpověď podle přiřazené role*

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

**Kdy použít:** Revize kódu, doučování, doménová analýza nebo když potřebujete odpovědi přizpůsobené určité odborné úrovni či pohledu.

### Šablony promptů

Vytvářejte znovupoužitelné prompty s proměnnými zástupnými znaky. Místo psaní nového promptu pokaždé definujte šablonu jednou a vyplňujte různé hodnoty. Třída `PromptTemplate` v LangChain4j to usnadňuje pomocí syntaxe `{{variable}}`.

<img src="../../../translated_images/cs/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt šablony" width="800"/>

*Znovupoužitelné prompty s proměnnými zástupnými znaky — jedna šablona, mnoho použití*

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

**Kdy použít:** Opakované dotazy s různými vstupy, dávkové zpracování, budování opakovatelných AI pracovních toků nebo jakýkoliv scénář, kde se struktura promptu nemění, ale data ano.

---

Těchto pět základů vám poskytne solidní nástroje pro většinu úkolů s promptováním. Zbytek tohoto modulu na nich staví s **osmi pokročilými vzory**, které využívají řízení uvažování GPT-5.2, sebehodnocení a schopnosti strukturovaného výstupu.

## Pokročilé vzory

Po zvládnutí základů přejdeme k osmi pokročilým vzorům, které tento modul činí jedinečným. Ne všechny problémy vyžadují stejný přístup. Některé otázky potřebují rychlé odpovědi, jiné hluboké uvažování. Některé vyžadují viditelné uvažování, jiné jen výsledky. Každý níže uvedený vzor je optimalizován pro jiný scénář — a řízení uvažování GPT-5.2 vykresluje rozdíly ještě výrazněji.

<img src="../../../translated_images/cs/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osm vzorů promptování" width="800"/>

*Přehled osmi vzorů prompt engineeringu a jejich použití*

<img src="../../../translated_images/cs/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Řízení uvažování s GPT-5.2" width="800"/>

*Řízení uvažování GPT-5.2 umožňuje určit, kolik má model myslet — od rychlých přímých odpovědí až po hluboké zkoumání*

**Nízká ochota (rychlé a cílené)** - Pro jednoduché otázky, kde chcete rychlé, přímé odpovědi. Model dělá minimální uvažování - maximálně 2 kroky. Použijte pro výpočty, vyhledávání nebo přímočaré otázky.

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
> - „Jaký je rozdíl mezi vzory nízké a vysoké ochoty promptování?“
> - „Jak pomáhají XML značky v promtech strukturovat odpověď AI?“
> - „Kdy je lepší použít sebereflektivní vzory vs přímé instrukce?“

**Vysoká ochota (hloubkové a důkladné)** - Pro složité problémy, kde chcete komplexní analýzu. Model důkladně zkoumá a ukazuje podrobné uvažování. Použijte pro návrhy systémů, architektonická rozhodnutí nebo složitý výzkum.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Provádění úkolu (postup krok za krokem)** - Pro vícekrokové pracovní postupy. Model nejdřív poskytne plán, popisuje každý krok při práci a nakonec shrne. Použijte pro migrace, implementace nebo jakýkoliv vícekrokový proces.

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

Chain-of-Thought promptování explicitně žádá model o ukázání uvažovacího procesu, což zvyšuje přesnost u složitých úkolů. Postupné členění pomáhá porozumět logice jak lidem, tak AI.

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Zeptejte se na tento vzor:
> - „Jak bych přizpůsobil vzor provádění úkolu pro dlouhotrvající operace?“
> - „Jaké jsou nejlepší praktiky pro strukturování úvodů nástrojů v produkčních aplikacích?“
> - „Jak mohu zaznamenávat a zobrazovat mezistavy průběhu v uživatelském rozhraní?“

<img src="../../../translated_images/cs/task-execution-pattern.9da3967750ab5c1e.webp" alt="Vzor provádění úkolu" width="800"/>

*Plán → Provedení → Shrnutí pracovního postupu pro vícekrokové úkoly*

**Sebereflektivní kód** - Pro generování kódu produkční kvality. Model vytváří kód podle produkčních standardů s řádným zpracováním chyb. Použijte při budování nových funkcí nebo služeb.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/cs/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cyklus sebereflexe" width="800"/>

*Iterativní zlepšování - generuj, vyhodnoť, identifikuj problémy, zlepši, opakuj*

**Strukturovaná analýza** - Pro konzistentní hodnocení. Model recenzuje kód pomocí pevného rámce (správnost, postupy, výkon, bezpečnost, udržovatelnost). Použijte pro revize kódu nebo hodnocení kvality.

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
> - „Jak nejlépe programově zpracovat a použít strukturovaný výstup?“
> - „Jak zajistit konzistentní úrovně závažnosti u různých revizních relací?“

<img src="../../../translated_images/cs/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Vzor strukturované analýzy" width="800"/>

*Rámec pro konzistentní revize kódu s úrovněmi závažnosti*

**Vícekolové chatování** - Pro konverzace vyžadující kontext. Model si pamatuje předchozí zprávy a staví na nich. Použijte pro interaktivní pomoc nebo složité Q&A.

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

*Jak se kontext konverzace hromadí přes více kol až do dosažení limitu tokenů*

**Postupné uvažování** - Pro problémy vyžadující viditelnou logiku. Model ukazuje explicitní uvažování u každého kroku. Použijte pro matematické úlohy, logické hádanky nebo když potřebujete pochopit myšlenkový proces.

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

**Omezený výstup** - Pro odpovědi se specifickými formátovacími požadavky. Model přísně dodržuje pravidla formátu a délky. Použijte pro shrnutí nebo kdy potřebujete přesnou strukturu výstupu.

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

*Vymáhání specifických formátů, délek a strukturálních požadavků*

## Použití existujících zdrojů Azure

**Ověření nasazení:**

Ujistěte se, že v hlavním adresáři existuje soubor `.env` s Azure přihlašovacími údaji (vytvořený během modulu 01):
```bash
cat ../.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spuštění aplikace:**

> **Poznámka:** Pokud jste již spustili všechny aplikace pomocí `./start-all.sh` z modulu 01, tento modul již běží na portu 8083. Můžete přeskočit spouštěcí příkazy níže a rovnou přejít na http://localhost:8083.

**Možnost 1: Použití Spring Boot Dashboard (doporučeno pro uživatele VS Code)**

Vývojový kontejner obsahuje rozšíření Spring Boot Dashboard, které poskytuje vizuální rozhraní pro správu všech aplikací Spring Boot. Najdete ho na postranním panelu vlevo ve VS Code (hledejte ikonu Spring Boot).

Ze Spring Boot Dashboard můžete:
- Vidět všechny dostupné aplikace Spring Boot v pracovním prostoru
- Jedním kliknutím spouštět/zastavovat aplikace
- Sledovat logy aplikací v reálném čase
- Monitorovat stav aplikace
Jednoduše klikněte na tlačítko přehrávání vedle „prompt-engineering“ pro spuštění tohoto modulu, nebo spusťte všechny moduly najednou.

<img src="../../../translated_images/cs/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Možnost 2: Použití shell skriptů**

Spusťte všechny webové aplikace (moduly 01-04):

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

Oba skripty automaticky načtou proměnné prostředí ze souboru `.env` v kořenovém adresáři a sestaví JARy, pokud neexistují.

> **Poznámka:** Pokud dáváte přednost manuálnímu sestavení všech modulů před spuštěním:
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

Otevřete v prohlížeči http://localhost:8083.

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

## Snímky aplikace

<img src="../../../translated_images/cs/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Hlavní dashboard zobrazující všech 8 vzorů prompt engineeringu s jejich charakteristikami a případy použití*

## Prozkoumání vzorů

Webové rozhraní vám umožňuje experimentovat s různými strategiemi zadávání promptů. Každý vzor řeší jiné problémy – vyzkoušejte je a uvidíte, kdy který přístup vyniká.

### Nízká vs Vysoká aktivita

Zeptejte se jednoduchou otázku jako „Kolik je 15 % ze 200?“ s nízkou aktivitou. Dostanete okamžitou, přímou odpověď. Nyní položte složitější otázku jako „Navrhněte caching strategii pro vysoce zatížené API“ s vysokou aktivitou. Sledujte, jak model zpomalí a poskytne podrobné zdůvodnění. Stejný model, stejná struktura otázky – ale prompt mu říká, kolik má přemýšlet.

<img src="../../../translated_images/cs/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Rychlý výpočet s minimálním zdůvodněním*

<img src="../../../translated_images/cs/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Komplexní caching strategie (2.8MB)*

### Provádění úkolů (Předmluvy nástrojů)

Vícekrokové pracovní postupy profitují z plánování a průběžného komentování. Model popíše, co udělá, komentuje každý krok a nakonec shrne výsledky.

<img src="../../../translated_images/cs/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Vytvoření REST endpointu s krokovým komentářem (3.9MB)*

### Kód s vlastním sebereflexivním hodnocením

Vyzkoušejte „Vytvoř emailovou validační službu“. Místo pouhého generování kódu a zastavení model vytvoří, vyhodnotí podle kvalitativních kritérií, identifikuje slabiny a zlepší se. Uvidíte, jak iteruje, dokud kód nesplní produkční standardy.

<img src="../../../translated_images/cs/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Kompletní emailová validační služba (5.2MB)*

### Strukturální analýza

Kontroly kódu potřebují konzistentní hodnotící rámce. Model analyzuje kód podle pevných kategorií (správnost, praktiky, výkon, zabezpečení) s úrovněmi závažnosti.

<img src="../../../translated_images/cs/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Kontrola kódu založená na rámcích*

### Vícekrokový chat

Zeptejte se „Co je Spring Boot?“ a hned potom „Ukaž mi příklad“. Model si pamatuje první otázku a poskytne vám specifický příklad Spring Boot. Bez paměti by druhá otázka byla příliš neurčitá.

<img src="../../../translated_images/cs/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Uchování kontextu napříč otázkami*

### Zdůvodnění krok za krokem

Vyberte matematický problém a vyzkoušejte ho s oběma režimy: krok za krokem a nízkou aktivitou. Nízká aktivita vám dá jen odpověď – rychle, ale bez vysvětlení. Krok za krokem vám ukáže každý výpočet a rozhodnutí.

<img src="../../../translated_images/cs/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matematický problém s explicitními kroky*

### Omezující výstup

Když potřebujete konkrétní formáty nebo počet slov, tento vzor striktně vynucuje pravidla. Vyzkoušejte vygenerovat shrnutí přesně se 100 slovy v odrážkovém formátu.

<img src="../../../translated_images/cs/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Shrnutí strojového učení s kontrolou formátu*

## Co se opravdu učíte

**Úsilí o zdůvodnění mění vše**

GPT-5.2 vám umožňuje ovládat výpočetní náročnost pomocí promptů. Nízké úsilí znamená rychlé odpovědi s minimálním prozkoumáváním. Vysoké úsilí znamená, že model věnuje čas důkladnému přemýšlení. Učíte se přizpůsobit úsilí složitosti úkolu – neztrácejte čas s jednoduchými otázkami, ale nespěchejte ani u složitých rozhodnutí.

**Struktura řídí chování**

Všimněte si XML tagů v promptech? Nejsou dekorativní. Modely spolehlivěji dodržují strukturované instrukce než volný text. Když potřebujete vícestupňové procesy nebo složitou logiku, struktura pomáhá modelu sledovat, kde je a co přijde dál.

<img src="../../../translated_images/cs/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomie dobře strukturovaného promptu s jasnými sekcemi a XML stylem organizace*

**Kvalita díky sebehodnocení**

Vzory s vlastním sebereflexivním hodnocením fungují tak, že explicitně definují kritéria kvality. Místo doufání, že model „to udělá správně“, mu přesně říkáte, co znamená „správně“: správná logika, ošetření chyb, výkon, zabezpečení. Model pak může vyhodnotit svůj vlastní výstup a zlepšit ho. To proměňuje generování kódu z loterie na proces.

**Kontext je omezený**

Vícekrokové konverzace fungují tak, že se s každým požadavkem posílá historie zpráv. Ale je tu limit – každý model má maximální počet tokenů. Jak se konverzace rozrůstá, budete potřebovat strategie, jak zachovat relevantní kontext, aniž byste limit překročili. Tento modul vám ukáže, jak paměť funguje; později se naučíte, kdy shrnovat, kdy zapomenout a kdy znovu načíst.

## Další kroky

**Další modul:** [03-rag - RAG (Generování doplněné o vyhledávání)](../03-rag/README.md)

---

**Navigace:** [← Předchozí: Modul 01 - Úvod](../01-introduction/README.md) | [Zpět na hlavní stránku](../README.md) | [Další: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:  
Tento dokument byl přeložen pomocí služby automatického překladu AI [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o přesnost, mějte prosím na paměti, že automatické překlady mohou obsahovat chyby nebo nepřesnosti. Originální dokument v jeho mateřském jazyce by měl být považován za autoritativní zdroj. Pro důležité informace doporučujeme profesionální překlad provedený lidským překladatelem. Nejsme odpovědni za případné nedorozumění či nesprávné výklady vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->