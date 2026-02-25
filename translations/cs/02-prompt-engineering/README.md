# Modul 02: Vytváření promptů s GPT-5.2

## Obsah

- [Co se naučíte](../../../02-prompt-engineering)
- [Předpoklady](../../../02-prompt-engineering)
- [Pochopení vytváření promptů](../../../02-prompt-engineering)
- [Základy vytváření promptů](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Pokročilé vzory](../../../02-prompt-engineering)
- [Použití existujících zdrojů Azure](../../../02-prompt-engineering)
- [Snímky aplikace](../../../02-prompt-engineering)
- [Prozkoumání vzorů](../../../02-prompt-engineering)
  - [Nízká vs vysoká ochota](../../../02-prompt-engineering)
  - [Provedení úkolu (preambly nástrojů)](../../../02-prompt-engineering)
  - [Sebereflektující kód](../../../02-prompt-engineering)
  - [Strukturovaná analýza](../../../02-prompt-engineering)
  - [Vícekolové chatování](../../../02-prompt-engineering)
  - [Krok za krokem uvažování](../../../02-prompt-engineering)
  - [Omezený výstup](../../../02-prompt-engineering)
- [Co se opravdu naučíte](../../../02-prompt-engineering)
- [Další kroky](../../../02-prompt-engineering)

## Co se naučíte

<img src="../../../translated_images/cs/what-youll-learn.c68269ac048503b2.webp" alt="Co se naučíte" width="800"/>

V předchozím modulu jste viděli, jak paměť umožňuje konverzační AI, a použili jste GitHub Models pro základní interakce. Nyní se zaměříme na to, jak klást otázky — samotné prompty — pomocí Azure OpenAI GPT-5.2. Způsob strukturování promptů výrazně ovlivňuje kvalitu odpovědí, které dostanete. Začneme přehledem základních technik vytváření promptů a poté přejdeme k osmi pokročilým vzorům, které plně využívají možnosti GPT-5.2.

Použijeme GPT-5.2, protože zavádí kontrolu uvažování - můžete modelu říct, kolik přemýšlení má před odpovědí provést. To činí různé strategie vytváření promptů zřetelnějšími a pomáhá pochopit, kdy použít který přístup. Také využijeme menších limitů Azure pro GPT-5.2 oproti GitHub Models.

## Předpoklady

- Dokončený Modul 01 (nasazené Azure OpenAI zdroje)
- Soubor `.env` v kořenovém adresáři s přihlašovacími údaji Azure (vytvořený pomocí `azd up` v Modulu 01)

> **Poznámka:** Pokud jste Modul 01 nedokončili, nejprve postupujte podle tamních pokynů k nasazení.

## Pochopení vytváření promptů

<img src="../../../translated_images/cs/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Co je vytváření promptů?" width="800"/>

Vytváření promptů je o navrhování vstupního textu, který vám spolehlivě přinese požadované výsledky. Nejde jen o kladení otázek - je to o strukturování požadavků tak, aby model přesně pochopil, co chcete, a jak to má dodat.

Představte si to jako dávání instrukcí kolegovi. „Oprav chybu“ je vágní. „Oprav výjimku null pointer v UserService.java na řádku 45 přidáním kontroly na null“ je konkrétní. Jazykové modely fungují stejně – specifikace a struktura jsou důležité.

<img src="../../../translated_images/cs/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Jak LangChain4j zapadá" width="800"/>

LangChain4j poskytuje infrastrukturu — připojení k modelům, paměť a typy zpráv — zatímco vzory promptů jsou jen pečlivě strukturovaný text, který touto infrastrukturou posíláte. Klíčové stavební bloky jsou `SystemMessage` (nastavuje chování a roli AI) a `UserMessage` (nese váš skutečný požadavek).

## Základy vytváření promptů

<img src="../../../translated_images/cs/five-patterns-overview.160f35045ffd2a94.webp" alt="Přehled pěti vzorů vytváření promptů" width="800"/>

Než se ponoříme do pokročilých vzorů tohoto modulu, pojďme si shrnout pět základních technik vytváření promptů. Jsou to stavební kameny, které by měl znát každý prompt inženýr. Pokud jste už pracovali s [rychlým startem](../00-quick-start/README.md#2-prompt-patterns), viděli jste je v praxi — tady je konceptuální rámec za nimi.

### Zero-Shot Prompting

Nejjednodušší přístup: dejte modelu přímý pokyn bez příkladů. Model se spoléhá zcela na své trénování, aby pochopil a vykonal úkol. Funguje to dobře pro přímočaré požadavky, kde je očekávané chování jasné.

<img src="../../../translated_images/cs/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Přímý pokyn bez příkladů — model vyvozuje úkol pouze z instrukce*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odpověď: "Pozitivní"
```

**Kdy použít:** Jednoduché klasifikace, přímé otázky, překlady nebo jakýkoli úkol, který model zvládne bez dalších instrukcí.

### Few-Shot Prompting

Poskytněte příklady, které ukazují vzor, podle kterého má model postupovat. Model se z vašich příkladů naučí očekávaný formát vstupu a výstupu a použije ho na nové vstupy. To výrazně zlepšuje konzistenci u úloh, kde není žádoucí formát nebo chování zřejmé.

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

**Kdy použít:** Vlastní klasifikace, konzistentní formátování, úkoly specifické pro doménu nebo když jsou výsledky zero-shot nekonzistentní.

### Chain of Thought

Požádejte model, aby ukázal své uvažování krok za krokem. Místo aby skočil rovnou k odpovědi, model rozloží problém a postupně ho řeší explicitně. To zlepšuje přesnost u matematiky, logiky a vícekrokového uvažování.

<img src="../../../translated_images/cs/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Krok za krokem uvažování — rozdělení složitých problémů na explicitní logické kroky*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model ukazuje: 15 - 8 = 7, pak 7 + 12 = 19 jablek
```

**Kdy použít:** Matematické problémy, logické hádanky, ladění nebo jakýkoli úkol, kde zobrazení procesu uvažování zlepšuje přesnost a důvěru.

### Role-Based Prompting

Nastavte AI personu nebo roli před položení dotazu. To poskytuje kontext, který formuje tón, hloubku a zaměření odpovědi. „Software architekt“ dává jiné rady než „junior vývojář“ nebo „audit bezpečnosti“.

<img src="../../../translated_images/cs/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Nastavení kontextu a persony — stejná otázka dostane jinou odpověď podle přiřazené role*

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

**Kdy použít:** Revize kódu, doučování, doménové analýzy nebo když chcete odpovědi přizpůsobené určité úrovni odbornosti či pohledu.

### Prompt Templates

Vytvořte znovupoužitelné prompty s proměnnými zástupnými znaky. Místo psaní nového promptu pokaždé, definujte šablonu jednou a doplňujte různé hodnoty. Třída `PromptTemplate` LangChain4j to usnadňuje pomocí syntaxe `{{variable}}`.

<img src="../../../translated_images/cs/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

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

**Kdy použít:** Opakované dotazy s různými vstupy, dávkové zpracování, vytváření znovupoužitelných AI workflow nebo kdykoli, kdy struktura promptu zůstává stejná, ale data se mění.

---

Těchto pět základů vám poskytne solidní nástroj pro většinu úloh vytváření promptů. Zbytek tohoto modulu na nich staví pomocí **osmi pokročilých vzorů**, které využívají kontrolu uvažování GPT-5.2, sebehodnocení a schopnosti strukturovaného výstupu.

## Pokročilé vzory

S pokrytými základy přejdeme k osmi pokročilým vzorům, které tento modul činí jedinečným. Ne všechny problémy vyžadují stejný přístup. Některé otázky potřebují rychlé odpovědi, jiné hluboké přemýšlení. Některé potřeby viditelné uvažování, jiné jen výsledky. Každý níže uvedený vzor je optimalizován pro jiný scénář — a kontrola uvažování GPT-5.2 tyto rozdíly ještě více zvýrazňuje.

<img src="../../../translated_images/cs/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osm vzorů promptů" width="800"/>

*Přehled osmi vzorů vytváření promptů a jejich použití*

<img src="../../../translated_images/cs/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontrola uvažování s GPT-5.2" width="800"/>

*Kontrola uvažování GPT-5.2 umožňuje specifikovat, kolik přemýšlení má model vykonat — od rychlých přímých odpovědí po hluboké zkoumání*

**Nízká ochota (rychlé a zaměřené)** - Pro jednoduché otázky, kdy chcete rychlé, přímé odpovědi. Model provádí minimální uvažování - maximálně 2 kroky. Použijte při výpočtech, hledání nebo přímých dotazech.

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
> - „Jaký je rozdíl mezi nízkou a vysokou ochotou ve vzorech vytváření promptů?“
> - „Jak XML tagy v promptech pomáhají strukturovat odpověď AI?“
> - „Kdy mám použít vzory sebereflexe vs přímé instrukce?“

**Vysoká ochota (hluboké a důkladné)** - Pro složité problémy, kde chcete komplexní analýzu. Model zkoumá důkladně a ukazuje detailní uvažování. Použijte pro systémové návrhy, architektonická rozhodnutí nebo komplexní výzkum.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Provedení úkolu (postup krok za krokem)** - Pro vícekroková workflow. Model nejprve poskytne plán, popisuje každý krok při práci a nakonec shrne. Použijte pro migrace, implementace nebo jakýkoli vícekrokový proces.

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

Přístup Chain-of-Thought explicitně žádá model, aby ukázal své uvažování, což zlepšuje přesnost u složitých úkolů. Rozpis krok za krokem pomáhá lidem i AI lépe pochopit logiku.

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Zeptejte se na tento vzor:
> - „Jak přizpůsobím vzor provedení úkolu pro dlouhotrvající operace?“
> - „Jaké jsou nejlepší postupy pro strukturování preambulí nástrojů v produkčních aplikacích?“
> - „Jak mohu zachytit a zobrazit průběžné aktualizace pokroku v uživatelském rozhraní?“

<img src="../../../translated_images/cs/task-execution-pattern.9da3967750ab5c1e.webp" alt="Vzor provedení úkolu" width="800"/>

*Plán → Provedení → Shrnutí workflow pro vícekrokové úkoly*

**Sebereflektující kód** - Pro generování kódu produkční kvality. Model generuje kód dle produkčních standardů s patřičným zpracováním chyb. Použijte při vytváření nových funkcí nebo služeb.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/cs/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cyklus sebereflexe" width="800"/>

*Iterační smyčka zlepšování - generuj, vyhodnoť, identifikuj problémy, zlepši, opakuj*

**Strukturovaná analýza** - Pro konzistentní hodnocení. Model kontroluje kód podle pevného rámce (správnost, praktiky, výkon, bezpečnost, udržovatelnost). Použijte při revizích kódu nebo hodnocení kvality.

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
> - „Jak přizpůsobím analytický rámec pro různé typy revizí kódu?“
> - „Jak nejlépe programově zpracovat a jednat podle strukturovaného výstupu?“
> - „Jak zajistím konzistentní úrovně závažnosti napříč různými recenzními sezeními?“

<img src="../../../translated_images/cs/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Vzor strukturované analýzy" width="800"/>

*Rámec pro konzistentní revize kódu s úrovněmi závažnosti*

**Vícekolové chatování** - Pro konverzace, které potřebují kontext. Model si pamatuje předchozí zprávy a staví na nich. Použijte pro interaktivní pomoc nebo složité dotazy a odpovědi.

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

*Jak kontext konverzace narůstá během vícenásobných kol až do dosažení limitu tokenů*

**Krok za krokem uvažování** - Pro problémy vyžadující viditelnou logiku. Model ukazuje explicitní uvažování pro každý krok. Použijte u matematických úloh, logických hádanek nebo když potřebujete pochopit proces myšlení.

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

*Rozložení problémů na explicitní logické kroky*

**Omezený výstup** - Pro odpovědi s požadavky na specifický formát. Model přísně dodržuje pravidla formátu a délky. Použijte pro shrnutí nebo když potřebujete přesnou strukturu výstupu.

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

*Prosazování specifických požadavků na formát, délku a strukturu*

## Použití existujících zdrojů Azure

**Ověření nasazení:**

Ujistěte se, že soubor `.env` existuje v kořenovém adresáři s přihlašovacími údaji Azure (vytvořenými během Modulu 01):
```bash
cat ../.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spuštění aplikace:**

> **Poznámka:** Pokud jste již všechny aplikace spustili pomocí `./start-all.sh` z Modulu 01, tento modul již běží na portu 8083. Spouštěcí příkazy níže můžete přeskočit a přejít přímo na http://localhost:8083.

**Možnost 1: Použití Spring Boot Dashboard (doporučeno pro uživatele VS Code)**

Vývojový kontejner obsahuje rozšíření Spring Boot Dashboard, které poskytuje vizuální rozhraní pro správu všech Spring Boot aplikací. Najdete ho v liště aktivit na levé straně VS Code (hledejte ikonu Spring Boot).

Ze Spring Boot Dashboard lze:
- Vidět všechny dostupné Spring Boot aplikace v pracovním prostoru
- Spouštět/zastavovat aplikace jedním kliknutím
- Sledovat živé logy aplikace
- Monitorovat stav aplikace
Jednoduše klikněte na tlačítko přehrát vedle „prompt-engineering“ pro spuštění tohoto modulu, nebo spusťte všechny moduly najednou.

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

Oba skripty automaticky načítají proměnné prostředí ze souboru `.env` v kořenovém adresáři a pokud JAR soubory neexistují, zkompilují je.

> **Poznámka:** Pokud raději kompilujete všechny moduly ručně před spuštěním:
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

**Jak zastavit:**

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

*Hlavní dashboard ukazující všech 8 vzorů prompt engineering s jejich charakteristikami a použitím*

## Prozkoumávání vzorů

Webové rozhraní vám umožňuje experimentovat s různými strategiemi promptování. Každý vzor řeší jiné problémy – vyzkoušejte je a zjistěte, kdy který přístup vyniká.

> **Poznámka: Streamování vs neserializované (Non-Streaming)** — Každá stránka vzoru nabízí dvě tlačítka: **🔴 Stream Response (Live)** a možnost **Non-streaming**. Streamování využívá Server-Sent Events (SSE), díky čemuž jsou tokeny z modelu zobrazovány v reálném čase, takže vidíte průběh ihned. Non-streaming možnost počká na celou odpověď před jejím zobrazením. U promptů vyžadujících hluboké uvažování (např. High Eagerness, Self-Reflecting Code) může volání non-streaming trvat velmi dlouho – i minuty – bez viditelné zpětné vazby. **Používejte streamování při experimentování s komplexními prompty**, abyste viděli, jak model pracuje, a nedostali dojem, že požadavek vypršel.
>
> **Poznámka: Požadavky na prohlížeč** — Funkce streamování používá Fetch Streams API (`response.body.getReader()`), které vyžaduje plnohodnotný prohlížeč (Chrome, Edge, Firefox, Safari). Nepracuje v integrovaném Simple Browseru ve VS Code, protože jeho webview nepodporuje ReadableStream API. Pokud používáte Simple Browser, tlačítka non-streaming fungují normálně – pouze tlačítka streamingu jsou ovlivněna. Pro plný zážitek otevřete `http://localhost:8083` v externím prohlížeči.

### Nízká vs Vysoká ochota (Eagerness)

Zeptejte se jednoduché otázky jako „Co je 15 % ze 200?“ s nízkou ochotou. Dostanete okamžitou, přímou odpověď. Teď se zeptejte na něco složitějšího jako „Navrhni cachingovou strategii pro vysoce zatížené API“ s vysokou ochotou. Klikněte na **🔴 Stream Response (Live)** a sledujte, jak model generuje detailní uvažování token po tokenu. Stejný model, stejná struktura otázky – ale prompt mu říká, kolik má přemýšlet.

### Vykonávání úkolů (Tool Preambles)

Vícekrokové workflowy těží z předem plánovaného postupu a komentování průběhu. Model popíše, co udělá, komentuje každý krok a pak shrne výsledky.

### Sebereflektivní kód

Vyzkoušejte „Vytvoř ověřovací službu pro e-mail“. Místo toho, aby model jen vygeneroval kód a skončil, ho vytváří, vyhodnocuje podle kritérií kvality, identifikuje slabiny a zlepšuje. Uvidíte, jak iteruje, dokud kód nesplní produkční standardy.

### Strukturovaná analýza

Recenze kódu potřebují konzistentní hodnotící rámec. Model analyzuje kód pomocí pevných kategorií (správnost, praktiky, výkon, bezpečnost) a úrovní závažnosti.

### Vícekolové konverzace

Zeptejte se „Co je Spring Boot?“ a hned poté „Ukaž mi příklad“. Model si pamatuje první otázku a dá vám příklad Spring Boot konkrétně. Bez paměti by druhá otázka byla příliš vágní.

### Postupné uvažování

Vyberte matematický příklad a zkuste ho vyřešit jak s Postupným uvažováním, tak s nízkou ochotou. Nízká ochota vám jen dá odpověď – rychle, ale neprůhledně. Postupné uvažování ukazuje každý výpočet a rozhodnutí.

### Omezený výstup

Když potřebujete konkrétní formáty nebo počet slov, tento vzor vyžaduje přísné dodržování. Vyzkoušejte vytvoření shrnutí přesně o 100 slovech v bodech.

## Co se opravdu učíte

**Úsilí při uvažování mění vše**

GPT-5.2 vám umožňuje kontrolovat výpočetní úsilí pomocí promptů. Nízké úsilí znamená rychlé odpovědi s minimálním zkoumáním. Vysoké úsilí znamená, že model si dá čas na hluboké přemýšlení. Učíte se přizpůsobit úsilí složitosti úkolu – neztrácejte čas s jednoduchými otázkami, ale ani nespěchejte složitá rozhodnutí.

**Struktura vede chování**

Všimli jste si XML tagů v promptech? Nejsou jen dekorace. Modely spolehlivěji sledují strukturované instrukce než volný text. Když potřebujete vícekrokové procesy nebo složitou logiku, struktura pomáhá modelu sledovat, kde je a co bude dál.

<img src="../../../translated_images/cs/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomie dobře strukturovaného promptu s jasnými sekcemi a XML-stylovou organizací*

**Kvalita přes sebehodnocení**

Sebereflektivní vzory fungují tak, že explicitně definují kritéria kvality. Místo toho, abyste doufali, že model „to udělá správně“, řeknete mu přesně, co znamená „správně“: správná logika, ošetření chyb, výkon, bezpečnost. Model pak může zhodnotit svůj výstup a zlepšit se. To mění generování kódu z loterie na proces.

**Kontext je konečný**

Vícekolové konverzace fungují tak, že ke každému požadavku přikládají historii zpráv. Ale existuje limit – každý model má maximální počet tokenů. Jak konverzace rostou, budete potřebovat strategie, jak udržet relevantní kontext a nepřekročit limit. Tento modul ukazuje, jak paměť funguje; později se naučíte, kdy shrnovat, kdy zapomenout a kdy vyhledávat.

## Další kroky

**Další modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigace:** [← Předchozí: Modul 01 - Úvod](../01-introduction/README.md) | [Zpět na hlavní](../README.md) | [Další: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o přesnost, mějte prosím na paměti, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho rodném jazyce je třeba považovat za autoritativní zdroj. Pro zásadní informace se doporučuje profesionální lidský překlad. Nejsme odpovědní za jakékoliv nedorozumění nebo nesprávné výklady vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->