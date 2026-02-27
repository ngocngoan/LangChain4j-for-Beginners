# Modul 02: Prompt Engineering s GPT-5.2

## Obsah

- [Video průvodce](../../../02-prompt-engineering)
- [Co se naučíte](../../../02-prompt-engineering)
- [Požadavky](../../../02-prompt-engineering)
- [Pochopení Prompt Engineering](../../../02-prompt-engineering)
- [Základy Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Pokročilé vzory](../../../02-prompt-engineering)
- [Použití existujících Azure zdrojů](../../../02-prompt-engineering)
- [Snímky obrazovky aplikace](../../../02-prompt-engineering)
- [Prozkoumání vzorů](../../../02-prompt-engineering)
  - [Nízká vs Vysoká ochota](../../../02-prompt-engineering)
  - [Vykonávání úkolů (preambly nástrojů)](../../../02-prompt-engineering)
  - [Sebereflektující kód](../../../02-prompt-engineering)
  - [Strukturovaná analýza](../../../02-prompt-engineering)
  - [Vícekolová konverzace](../../../02-prompt-engineering)
  - [Krok za krokem uvažování](../../../02-prompt-engineering)
  - [Omezený výstup](../../../02-prompt-engineering)
- [Co se opravdu učíte](../../../02-prompt-engineering)
- [Další kroky](../../../02-prompt-engineering)

## Video průvodce

Sledujte živé vysílání, které vysvětluje, jak začít s tímto modulem: [Prompt Engineering s LangChain4j - živá relace](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## Co se naučíte

<img src="../../../translated_images/cs/what-youll-learn.c68269ac048503b2.webp" alt="Co se naučíte" width="800"/>

V předchozím modulu jste viděli, jak paměť umožňuje konverzační AI a používali jste GitHub Modely pro základní interakce. Nyní se zaměříme na to, jak klást otázky — samotné prompty — pomocí Azure OpenAI GPT-5.2. Způsob, jakým strukturalizujete své prompty, výrazně ovlivňuje kvalitu odpovědí, které dostanete. Začneme přehledem základních technik promptování a poté přejdeme k osmi pokročilým vzorům, které plně využívají schopnosti GPT-5.2.

Použijeme GPT-5.2, protože zavádí kontrolu uvažování – můžete modelu říct, kolik přemýšlení má před odpovědí udělat. To dělá různé strategie promptování více zřetelnými a pomáhá porozumět, kdy použít který přístup. Také využijeme méně limitů Azure pro GPT-5.2 ve srovnání s GitHub Modely.

## Požadavky

- Dokončený Modul 01 (nasazení Azure OpenAI zdrojů)
- Soubor `.env` v kořenovém adresáři s Azure přihlašovacími údaji (vytvořený příkazem `azd up` v Modulu 01)

> **Poznámka:** Pokud jste nezvládli Modul 01, nejprve postupujte podle tamních instrukcí pro nasazení.

## Pochopení Prompt Engineering

<img src="../../../translated_images/cs/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Co je Prompt Engineering?" width="800"/>

Prompt engineering znamená navrhování vstupního textu, který vám konzistentně přináší požadované výsledky. Nejde jen o kladení otázek – jde o strukturování požadavků tak, aby model přesně rozuměl tomu, co chcete, a jak to má dodat.

Představte si to jako dávání instrukcí kolegovi. „Oprav chybu“ je vágní. „Oprav nulovou referenci v UserService.java na řádku 45 přidáním kontroly null“ je specifické. Jazykové modely fungují stejně – důležitá je specifičnost a struktura.

<img src="../../../translated_images/cs/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Jak zapadá LangChain4j" width="800"/>

LangChain4j poskytuje infrastrukturu — připojení k modelům, paměť a typy zpráv — zatímco vzory promptů jsou jen pečlivě strukturované texty, které touto infrastrukturou posíláte. Klíčovými stavebními kameny jsou `SystemMessage` (nastavující chování a roli AI) a `UserMessage` (která nese váš skutečný požadavek).

## Základy Prompt Engineering

<img src="../../../translated_images/cs/five-patterns-overview.160f35045ffd2a94.webp" alt="Pět základních vzorů Prompt Engineering" width="800"/>

Než přejdeme k pokročilým vzorům v tomto modulu, pojďme si zopakovat pět základních technik promptování. To jsou stavební kameny, které by měl znát každý prompt engineer. Pokud jste už prošli [Quick Start modul](../00-quick-start/README.md#2-prompt-patterns), viděli jste je v akci — zde je jejich koncepční rámec.

### Zero-Shot Prompting

Nejsnazší přístup: dejte modelu přímý příkaz bez příkladů. Model se spoléhá výhradně na své trénování, aby úkol pochopil a vykonal. Funguje to dobře pro jednoduché požadavky, kde je očekávané chování zřejmé.

<img src="../../../translated_images/cs/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Přímý příkaz bez příkladů — model odvozuje úkol pouze z instrukce*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odpověď: "Pozitivní"
```

**Kdy použít:** Jednoduché klasifikace, přímé otázky, překlady nebo jakýkoli úkol, který model zvládne bez dalšího vedení.

### Few-Shot Prompting

Poskytněte příklady, které ilustrují vzor, kterým má model postupovat. Model se naučí očekávaný vstup-výstup formát z vašich příkladů a aplikuje jej na nové vstupy. To výrazně zlepšuje konzistenci u úkolů, kde požadovaný formát nebo chování není zřejmé.

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

**Kdy použít:** Vlastní klasifikace, konzistentní formátování, doménově specifické úkoly nebo když jsou výsledky zero-shot nekonzistentní.

### Chain of Thought

Požádejte model, aby ukázal své uvažování krok za krokem. Místo aby skočil rovnou k odpovědi, model problém rozloží a otevřeně projde každou část. To zlepšuje přesnost v matematice, logice a vícekrokovém uvažování.

<img src="../../../translated_images/cs/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Krok za krokem uvažování — rozkládání složitých problémů na jasné logické kroky*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model ukazuje: 15 - 8 = 7, pak 7 + 12 = 19 jablek
```

**Kdy použít:** Matematické problémy, logické hádanky, ladění chyb nebo jakýkoli úkol, kde zobrazení procesu uvažování zvyšuje přesnost a důvěru.

### Role-Based Prompting

Nastavte pro AI personu nebo roli před tím, než položíte otázku. To poskytuje kontext, který ovlivňuje tón, hloubku a zaměření odpovědi. „Softwarový architekt“ poskytne jinou radu než „junior vývojář“ nebo „security auditor“.

<img src="../../../translated_images/cs/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Nastavení kontextu a persony — ta samá otázka dostane jinou odpověď podle přiřazené role*

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

**Kdy použít:** Kontroly kódu, doučování, doménově specifické analýzy nebo když potřebujete odpovědi přizpůsobené určité úrovni odbornosti či perspektivy.

### Prompt Templates

Vytvářejte znovupoužitelné prompty s proměnnými zástupnými symboly. Místo psaní nového promptu pokaždé definujte jednou šablonu a doplňujte různé hodnoty. Třída `PromptTemplate` v LangChain4j to usnadňuje pomocí syntaxe `{{variable}}`.

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

**Kdy použít:** Opakované dotazy s různými vstupy, dávkové zpracování, budování znovupoužitelných AI workflow nebo scénáře, kde struktura promptu zůstává stejná, ale mění se data.

---

Těchto pět základů vám dává pevný nástrojový set pro většinu promptovacích úkolů. Zbytek tohoto modulu na ně navazuje pomocí **osmi pokročilých vzorů**, které využívají kontrolu uvažování, sebehodnocení a strukturované výstupy GPT-5.2.

## Pokročilé vzory

Po zvládnutí základů přejdeme k osmi pokročilým vzorům, které činí tento modul jedinečným. Ne všechny problémy vyžadují stejný přístup. Některé otázky si žádají rychlé odpovědi, jiné hluboké přemýšlení. Některé potřebují viditelné uvažování, jiné jen výsledky. Každý níže uvedený vzor je optimalizován pro jiný scénář – a kontrola uvažování GPT-5.2 tyto rozdíly ještě více zvýrazňuje.

<img src="../../../translated_images/cs/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osm vzorů promptování" width="800"/>

*Přehled osmi vzorů prompt engineering a jejich použití*

<img src="../../../translated_images/cs/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontrola uvažování s GPT-5.2" width="800"/>

*Kontrola uvažování GPT-5.2 vám umožňuje určit, kolik přemýšlení má model udělat — od rychlých přímých odpovědí po hluboké zkoumání*

**Nízká ochota (rychlé a zaměřené)** – Pro jednoduché otázky, kde chcete rychlé a přímé odpovědi. Model dělá minimální uvažování – maximálně 2 kroky. Použijte pro výpočty, dotazy nebo přímé otázky.

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
> - „Jak XML tagy v prompty pomáhají strukturovat odpověď AI?“
> - „Kdy mám používat vzory sebereflexe vs přímé instrukce?“

**Vysoká ochota (hluboké a důkladné)** – Pro složité problémy, kde chcete komplexní analýzu. Model důkladně zkoumá a ukazuje detailní uvažování. Použijte pro návrh systémů, architektonická rozhodnutí nebo složitý výzkum.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Vykonávání úkolů (postup krok za krokem)** – Pro vícekrokové pracovní postupy. Model poskytne plán předem, komentuje každý krok během práce a nakonec shrne. Použijte pro migrace, implementace nebo jakýkoli vícekrokový proces.

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

Chain-of-Thought prompting explicitně žádá model, aby ukázal svůj process uvažování, což zlepšuje přesnost u složitých úkolů. Postupné rozložení pomáhá pochopit logiku jak lidem, tak AI.

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Zeptejte se na tento vzor:
> - „Jak bych přizpůsobil vzor vykonávání úkolu pro dlouhotrvající operace?“
> - „Jaké jsou nejlepší postupy pro strukturování preamblů nástrojů v produkčních aplikacích?“
> - „Jak zachytit a zobrazit průběžné aktualizace v uživatelském rozhraní?“

<img src="../../../translated_images/cs/task-execution-pattern.9da3967750ab5c1e.webp" alt="Vzor vykonávání úkolů" width="800"/>

*Workflow Plánuj → Vykonej → Shrň pro vícekrokové úkoly*

**Sebereflektující kód** – Pro generování produkčního kódu. Model generuje kód dle produkčních standardů s odpovídajícím zpracováním chyb. Použijte při vývoji nových funkcí či služeb.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/cs/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cyklus sebereflexe" width="800"/>

*Iterativní smyčka zlepšení – generuj, vyhodnoť, identifikuj problémy, vylepši, opakuj*

**Strukturovaná analýza** – Pro konzistentní hodnocení. Model kontroluje kód dle pevného rámce (správnost, postupy, výkon, bezpečnost, udržovatelnost). Použijte pro code review nebo hodnocení kvality.

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
> - „Jak přizpůsobit analýzu pro různé typy code review?“
> - „Jak programově zpracovat a reagovat na strukturovaný výstup?“
> - „Jak zajistit konzistentní úrovně závažnosti mezi různými kontrolními sezeními?“

<img src="../../../translated_images/cs/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Vzor strukturované analýzy" width="800"/>

*Rámec pro konzistentní code review se stupni závažnosti*

**Vícekolová konverzace** – Pro rozhovory vyžadující kontext. Model si pamatuje předchozí zprávy a navazuje na ně. Použijte pro interaktivní pomoc nebo složité otázky a odpovědi.

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

*Jak se konverzační kontext nahromadí přes více kol až do dosažení limitu tokenů*

**Krok za krokem uvažování** – Pro problémy vyžadující viditelnou logiku. Model ukazuje jasné uvažování pro každý krok. Použijte u matematických problémů, logických hádanek nebo tam, kde chcete pochopit proces myšlení.

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

**Omezený výstup** – Pro odpovědi s požadavky na specifický formát. Model přísně dodržuje pravidla formátu a délky. Použijte pro shrnutí nebo když potřebujete přesnou strukturu výstupu.

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

*Dodržování specifických požadavků na formát, délku a strukturu*

## Použití existujících Azure zdrojů

**Ověření nasazení:**

Ujistěte se, že soubor `.env` v kořenovém adresáři obsahuje Azure přihlašovací údaje (vytvořeno během Modulu 01):
```bash
cat ../.env  # Mělo by ukázat AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spuštění aplikace:**

> **Poznámka:** Pokud jste již spustili všechny aplikace pomocí `./start-all.sh` z Modulu 01, tento modul již běží na portu 8083. Můžete přeskočit spouštěcí příkazy níže a jít přímo na http://localhost:8083.

**Možnost 1: Použití Spring Boot Dashboard (doporučeno pro uživatele VS Code)**
Vývojové kontejner zahrnuje rozšíření Spring Boot Dashboard, které poskytuje vizuální rozhraní pro správu všech aplikací Spring Boot. Najdete ho na postranním panelu vlevo ve VS Code (hledejte ikonu Spring Boot).

Ve Spring Boot Dashboard můžete:
- Vidět všechny dostupné aplikace Spring Boot v pracovním prostoru
- Spustit/zastavit aplikace jediným kliknutím
- Zobrazovat protokoly aplikací v reálném čase
- Monitorovat stav aplikace

Jednoduše klikněte na tlačítko přehrávání vedle „prompt-engineering“ pro spuštění tohoto modulu, nebo spusťte všechny moduly najednou.

<img src="../../../translated_images/cs/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Možnost 2: Použití shell skriptů**

Spusťte všechny webové aplikace (moduly 01-04):

**Bash:**
```bash
cd ..  # Ze kořenového adresáře
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Ze kořenového adresáře
.\start-all.ps1
```

Nebo spusťte pouze tento modul:

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

Oba skripty automaticky načtou proměnné prostředí z kořenového souboru `.env` a zkompilují JARy, pokud neexistují.

> **Poznámka:** Pokud dáváte přednost ručnímu sestavení všech modulů před spuštěním:
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

## Snímky obrazovky aplikace

<img src="../../../translated_images/cs/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Hlavní dashboard zobrazující všech 8 vzorů promptového inženýrství s jejich charakteristikami a použitím*

## Prozkoumání vzorů

Webové rozhraní vám umožňuje experimentovat s různými strategiemi promptování. Každý vzor řeší jiné problémy – vyzkoušejte je a uvidíte, kdy každý přístup opravdu vyniká.

> **Poznámka: Streaming vs Nesynchronní** — Každá stránka vzoru nabízí dvě tlačítka: **🔴 Streamovat odpověď (naživo)** a možnost **Nesynchronní**. Streaming používá Server-Sent Events (SSE) k zobrazení tokenů v reálném čase, jak je model generuje, takže vidíte postup okamžitě. Nesynchronní režim čeká na celou odpověď, než ji zobrazí. U promptů vyžadujících hluboké uvažování (např. High Eagerness, Self-Reflecting Code) může nesynchronní volání trvat velmi dlouho – někdy i minuty – bez viditelné odezvy. **Používejte streaming při experimentování s komplexními prompty**, abyste viděli model pracovat a vyhnuli se dojmu, že požadavek vypršel.
>
> **Poznámka: Požadavek na prohlížeč** — Funkce streamingu využívá Fetch Streams API (`response.body.getReader()`), které vyžaduje plnohodnotný prohlížeč (Chrome, Edge, Firefox, Safari). Nepracuje ve vestavěném jednoduchém prohlížeči VS Code, protože jeho webview nepodporuje ReadableStream API. Pokud používáte Simple Browser, nesynchronní tlačítka fungují normálně – pouze streamovací tlačítka jsou ovlivněna. Pro plný zážitek otevřete `http://localhost:8083` v externím prohlížeči.

### Nízká vs Vysoká Eagerness (ochota)

Zeptejte se jednoduché otázky jako „Kolik je 15 % z 200?“ s Nízkou Eagerness. Získáte okamžitou, přímou odpověď. Teď zeptejte složitější otázku „Navrhni cachingovou strategii pro API s vysokou návštěvností“ s Vysokou Eagerness. Klikněte na **🔴 Streamovat odpověď (naživo)** a sledujte, jak model zobrazuje detailní uvažování token po tokenu. Stejný model, stejná struktura otázky – ale prompt mu říká, kolik má přemýšlet.

### Provádění úkolů (preambly nástrojů)

Vícekrokové pracovní postupy profitují z předběžného plánování a vyprávění průběhu. Model nastíní, co udělá, popisuje každý krok a pak shrne výsledky.

### Sebereflexivní kód

Vyzkoušejte „Vytvoř službu ověřování e-mailů“. Místo toho, aby pouze generoval kód a skončil, model generuje, hodnotí podle kvalitativních kritérií, identifikuje slabiny a zlepšuje. Uvidíte, jak iteruje, dokud kód nesplní produkční standardy.

### Strukturovaná analýza

Code review potřebuje konzistentní hodnotící rámce. Model analyzuje kód podle pevných kategorií (správnost, postupy, výkon, bezpečnost) s úrovněmi závažnosti.

### Vícekolový chat

Zeptejte se „Co je Spring Boot?“ a pak hned pokračujte „Ukázat mi příklad“. Model si pamatuje první otázku a dává vám konkrétní příklad Spring Boot. Bez paměti by druhá otázka byla příliš vágní.

### Krok za krokem uvažování

Vyberte matematický problém a zkuste ho řešit jak Krok za krokem, tak Nízkou Eagerness. Nízká eagerness vám dá jen odpověď – rychle, ale neprůhledně. Krok za krokem ukazuje každý výpočet a rozhodnutí.

### Omezovaný výstup

Když potřebujete přesné formáty nebo počet slov, tento vzor vynucuje přísné dodržení. Zkuste generovat shrnutí přesně o 100 slovech v bodech.

## Co se opravdu učíte

**Úsilí v uvažování mění všechno**

GPT-5.2 vám umožňuje řídit výpočetní úsilí prostřednictvím vašich promptů. Nízké úsilí znamená rychlé odpovědi bez větší prozkoumávání. Vysoké úsilí znamená, že model tráví čas hlubokým uvažováním. Učíte se přizpůsobit úsilí složitosti úkolu – neplýtvejte časem na jednoduché otázky, ale ani nespěchejte při složitých rozhodnutích.

**Struktura řídí chování**

Všimli jste si XML tagů v promptech? Nejsou dekorativní. Modely spolehlivěji dodržují strukturované instrukce než volný text. Když potřebujete vícekrokové postupy nebo složitou logiku, struktura pomáhá modelu sledovat, kde je a co přijde dále.

<img src="../../../translated_images/cs/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomie dobře strukturovaného promptu s jasnými sekcemi a organizací ve stylu XML*

**Kvalita skrze sebehodnocení**

Sebereflexivní vzory fungují tak, že explicitně udávají kvalitativní kritéria. Místo aby model „doufal, že to udělá správně“, přesně mu říkáte, co znamená „správně“: korektní logika, zpracování chyb, výkon, bezpečnost. Model pak dokáže vyhodnotit svůj výstup a zlepšit ho. To proměňuje generování kódu z loterie na proces.

**Kontext je omezený**

Vícekolové konverzace fungují tak, že každé žádosti přidávají historii zpráv. Ale je tu limit – každý model má maximální počet tokenů. Jak konverzace rostou, budete potřebovat strategie, jak udržet relevantní kontext, aniž byste dosáhli stropu. Tento modul vám ukáže, jak paměť funguje; později se naučíte, kdy shrnovat, kdy zapomínat a kdy vyhledávat.

## Další kroky

**Další modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigace:** [← Předchozí: Modul 01 - Úvod](../01-introduction/README.md) | [Zpět na hlavní stránku](../README.md) | [Další: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o přesnost, mějte prosím na paměti, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Originální dokument v jeho původním jazyce by měl být považován za autoritativní zdroj. Pro důležité informace doporučujeme profesionální lidský překlad. Nepřejímáme odpovědnost za jakákoli nedorozumění nebo nesprávné interpretace vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->