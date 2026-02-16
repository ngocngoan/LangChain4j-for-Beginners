# Modul 02: Prompt Engineering s GPT-5.2

## Obsah

- [Co se naučíte](../../../02-prompt-engineering)
- [Předpoklady](../../../02-prompt-engineering)
- [Pochopení Prompt Engineering](../../../02-prompt-engineering)
- [Základy Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Šablony promptů](../../../02-prompt-engineering)
- [Pokročilé vzory](../../../02-prompt-engineering)
- [Použití existujících Azure zdrojů](../../../02-prompt-engineering)
- [Snímky obrazovky aplikace](../../../02-prompt-engineering)
- [Prozkoumání vzorů](../../../02-prompt-engineering)
  - [Nízká vs vysoká ochota](../../../02-prompt-engineering)
  - [Provádění úkolů (preambule nástrojů)](../../../02-prompt-engineering)
  - [Sebereflexivní kód](../../../02-prompt-engineering)
  - [Strukturovaná analýza](../../../02-prompt-engineering)
  - [Multi-turn chat](../../../02-prompt-engineering)
  - [Postupné uvažování](../../../02-prompt-engineering)
  - [Omezený výstup](../../../02-prompt-engineering)
- [Co se skutečně učíte](../../../02-prompt-engineering)
- [Další kroky](../../../02-prompt-engineering)

## Co se naučíte

<img src="../../../translated_images/cs/what-youll-learn.c68269ac048503b2.webp" alt="Co se naučíte" width="800"/>

V předchozím modulu jste viděli, jak paměť umožňuje konverzační AI, a používali jste GitHub modely pro základní interakce. Nyní se zaměříme na to, jak klást otázky — samotné prompty — použitím Azure OpenAI GPT-5.2. Způsob, jak strukturalizujete své prompty, zásadně ovlivňuje kvalitu odpovědí. Začneme přehledem základních technik prompting, poté přejdeme k osmi pokročilým vzorům, které plně využívají schopnosti GPT-5.2.

Použijeme GPT-5.2, protože zavádí řízení uvažování – můžete modelu říct, kolik myšlení má před odpovědí provést. To činí různé strategie promptování srozumitelnějšími a pomáhá pochopit, kdy použít kterou metodu. Také využijeme Azure s nižšími omezeními rychlosti pro GPT-5.2 v porovnání s GitHub modely.

## Předpoklady

- Dokončený Modul 01 (nasazení Azure OpenAI zdrojů)
- Soubor `.env` v kořenovém adresáři s Azure přihlašovacími údaji (vytvořený pomocí `azd up` v Modulu 01)

> **Poznámka:** Pokud jste ještě nedokončili Modul 01, nejprve postupujte podle pokynů pro nasazení tam.

## Pochopení Prompt Engineering

<img src="../../../translated_images/cs/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Co je Prompt Engineering?" width="800"/>

Prompt engineering spočívá v navrhování vstupního textu, který konzistentně přináší požadované výsledky. Nejde jen o kladení otázek – jde o strukturování požadavků tak, aby model přesně chápal, co chcete a jak to dodat.

Představte si to jako dávání instrukcí kolegovi. „Oprav chybu“ je vágní. „Oprav chybu null pointer výjimky v UserService.java na řádku 45 přidáním kontroly na null“ je konkrétní. Jazykové modely fungují stejně – konkrétnost a struktura jsou důležité.

<img src="../../../translated_images/cs/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Jak zapadá LangChain4j" width="800"/>

LangChain4j poskytuje infrastrukturu — připojení k modelu, paměť a typy zpráv — zatímco vzory promptů jsou pouze pečlivě strukturovaný text, který touto infrastrukturou posíláte. Klíčovými stavebními bloky jsou `SystemMessage` (která nastavuje chování a roli AI) a `UserMessage` (která přenáší váš skutečný požadavek).

## Základy Prompt Engineering

<img src="../../../translated_images/cs/five-patterns-overview.160f35045ffd2a94.webp" alt="Přehled pěti vzorů Prompt Engineering" width="800"/>

Než se pustíme do pokročilých vzorů v tomto modulu, zopakujme si pět základních technik promptování. Jsou to stavební kameny, které by měl znát každý prompt inženýr. Pokud jste již prošli [Rychlý start modul](../00-quick-start/README.md#2-prompt-patterns), viděli jste je v akci — zde je jejich koncepční rámec.

### Zero-Shot Prompting

Nejjednodušší přístup: dejte modelu přímý pokyn bez příkladů. Model se spoléhá zcela na své trénování, aby úkol pochopil a provedl. Funguje to dobře u jednoduchých požadavků, kde je očekávané chování očividné.

<img src="../../../translated_images/cs/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Přímý pokyn bez příkladů — model odvodí úkol pouze z instrukce*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odpověď: "Pozitivní"
```

**Kdy použít:** Jednoduché klasifikace, přímé otázky, překlady nebo jakýkoli úkol, který model zvládne bez dalšího vedení.

### Few-Shot Prompting

Poskytněte příklady, které ukazují vzor, podle kterého má model postupovat. Model se z vašich příkladů naučí očekávaný formát vstupu a výstupu a aplikuje ho na nové vstupy. Výrazně to zlepšuje konzistenci u úkolů, kde není požadovaný formát jasný.

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

**Kdy použít:** Vlastní klasifikace, konzistentní formátování, doménově specifické úkoly nebo kdy jsou výsledky zero-shot nekonzistentní.

### Chain of Thought

Požádejte model, aby ukázal své uvažování krok za krokem. Místo přímé odpovědi model rozkládá problém a explicite prochází každou část. To zvyšuje přesnost u matematiky, logiky a vícekrokového uvažování.

<img src="../../../translated_images/cs/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Postupné uvažování — rozklady složitých problémů do explicitních logických kroků*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model ukazuje: 15 - 8 = 7, pak 7 + 12 = 19 jablek
```

**Kdy použít:** Matematické problémy, logické hádanky, ladění kódu nebo úkoly, kde zobrazení procesu uvažování zlepšuje přesnost a důvěru.

### Role-Based Prompting

Před položení otázky nastavte AI personu nebo roli. Toto poskytuje kontext, který formuje tón, hloubku a zaměření odpovědi. „Softwarový architekt“ dává jiné rady než „junior vývojář“ nebo „security auditor“.

<img src="../../../translated_images/cs/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Nastavení kontextu a osoby — stejná otázka dostane jinou odpověď podle přiřazené role*

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

**Kdy použít:** Kontroly kódu, doučování, doménově specifické analýzy nebo když potřebujete odpovědi přizpůsobené určité úrovni odbornosti či pohledu.

### Šablony promptů

Vytvářejte znovupoužitelné prompty s proměnnými zástupci. Místo psaní nového promptu každýkrát definujte šablonu jednou a vyplňujte ji různými hodnotami. Třída `PromptTemplate` v LangChain4j to usnadňuje pomocí syntaxe `{{variable}}`.

<img src="../../../translated_images/cs/prompt-templates.14bfc37d45f1a933.webp" alt="Šablony promptů" width="800"/>

*Opakovaně použitelné prompty s proměnnými zástupci — jedna šablona, mnoho použití*

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

**Kdy použít:** Opakované dotazy s různými vstupy, dávkové zpracování, stavba znovupoužitelných AI workflow nebo scénáře, kde struktura promptu zůstává stejná, ale data se mění.

---

Těchto pět základů vám poskytuje pevný nástroj pro většinu prompting úkolů. Zbytek tohoto modulu na nich staví s **osmi pokročilými vzory**, které využívají řízení uvažování GPT-5.2, sebehodnocení a schopnosti strukturovaného výstupu.

## Pokročilé vzory

Po zvládnutí základů přejdeme k osmi pokročilým vzorům, které činí tento modul unikátním. Ne všechny problémy vyžadují stejný přístup. Některé otázky potřebují rychlé odpovědi, jiné hluboké přemýšlení. Některé vyžadují viditelné uvažování, jiné jen výsledky. Každý níže uvedený vzor je optimalizován pro jiný scénář — a řízení uvažování GPT-5.2 tyto rozdíly činí ještě výraznějšími.

<img src="../../../translated_images/cs/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osm vzorů prompt engineeringu" width="800"/>

*Přehled osmi vzorů prompt engineeringu a jejich použití*

<img src="../../../translated_images/cs/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Řízení uvažování s GPT-5.2" width="800"/>

*Řízení uvažování GPT-5.2 vám umožňuje specifikovat, kolik myšlení by model měl provést — od rychlých přímých odpovědí po hluboké zkoumání*

<img src="../../../translated_images/cs/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Porovnání náročnosti uvažování" width="800"/>

*Nízká ochota (rychlý, přímý) vs vysoká ochota (důkladný, zkoumavý) přístupy k uvažování*

**Nízká ochota (rychlé a zaměřené)** – Pro jednoduché otázky, kde chcete rychlé, přímé odpovědi. Model provádí minimální uvažování – maximálně 2 kroky. Použijte pro výpočty, vyhledávání nebo přímé otázky.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Prozkoumejte s GitHub Copilot:** Otevřete [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) a zeptejte se:
> - „Jaký je rozdíl mezi nízkou a vysokou ochotou ve vzorech promptování?“
> - „Jak XML značky v promtech pomáhají strukturovat odpověď AI?“
> - „Kdy mám používat vzory sebereflexe vs přímé instrukce?“

**Vysoká ochota (hluboké a důkladné)** – Pro složité problémy, kde chcete komplexní analýzu. Model zkoumá důkladně a ukazuje podrobné uvažování. Použijte pro návrh systémů, architektonická rozhodnutí nebo složitý výzkum.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Provádění úkolů (postupný průběh)** – Pro vícekrokové workflow. Model poskytne plán dopředu, komentuje každý krok při práci a nakonec shrne. Použijte při migracích, implementacích nebo jakémkoli vícekrokovém procesu.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought prompting explicitně žádá model, aby ukázal svůj proces uvažování, což zvyšuje přesnost u složitých úkolů. Postupné rozebrání pomáhá lidem i AI pochopit logiku.

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Zeptejte se na tento vzor:
> - „Jak přizpůsobím vzor provádění úkolů pro dlouhotrvající operace?“
> - „Jaké jsou nejlepší praktiky pro strukturování preambulí nástrojů v produkčních aplikacích?“
> - „Jak mohu zachytávat a zobrazovat průběžné aktualizace postupu v UI?“

<img src="../../../translated_images/cs/task-execution-pattern.9da3967750ab5c1e.webp" alt="Vzor provádění úkolů" width="800"/>

*Workflow Plán → Provedení → Shrnutí pro vícekrokové úkoly*

**Sebereflexivní kód** – Pro generování produkčního kódu. Model generuje kód, kontroluje jej podle kvalitativních kritérií a postupně jej vylepšuje. Použijte při tvorbě nových funkcí nebo služeb.

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

<img src="../../../translated_images/cs/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cyklus sebereflexe" width="800"/>

*Iterativní smyčka zlepšování - generuj, hodnot, identifikuj problémy, zlepšuj, opakuj*

**Strukturovaná analýza** – Pro konzistentní hodnocení. Model prohlíží kód podle pevného rámce (správnost, praktiky, výkon, bezpečnost). Použijte pro code review nebo hodnocení kvality.

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

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Zeptejte se na strukturovanou analýzu:
> - „Jak mohu přizpůsobit analytický rámec pro různé typy code review?“
> - „Jak nejlépe zpracovat a programově využít strukturovaný výstup?“
> - „Jak zajistit konzistentní úrovně závažnosti napříč různými recenzními sezeními?“

<img src="../../../translated_images/cs/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Vzor strukturované analýzy" width="800"/>

*Čtyřkategorický rámec pro konzistentní code review s úrovněmi závažnosti*

**Multi-turn chat** – Pro konverzace, které vyžadují kontext. Model si pamatuje předchozí zprávy a staví na nich. Použijte pro interaktivní pomoc nebo složitá Q&A.

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

*Jak se kontext konverzace akumuluje přes více kol až do dosažení limitu tokenů*

**Postupné uvažování** – Pro problémy vyžadující viditelnou logiku. Model ukazuje explicitní uvažování u každého kroku. Použijte pro matematické problémy, logické hádanky nebo když potřebujete pochopit proces myšlení.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/cs/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Vzor postupného uvažování" width="800"/>

*Rozklad problémů do explicitních logických kroků*

**Omezený výstup** – Pro odpovědi s konkrétními formátovacími požadavky. Model přesně dodržuje pravidla formátu a délky. Použijte pro shrnutí nebo v případech, kdy potřebujete přesnou strukturu výstupu.

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

**Ověřte nasazení:**

Ujistěte se, že soubor `.env` v kořenovém adresáři s Azure přihlašovacími údaji (vytvořený během Modulu 01) existuje:
```bash
cat ../.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spuštění aplikace:**

> **Poznámka:** Pokud jste již spustili všechny aplikace pomocí `./start-all.sh` z Modulu 01, tento modul již běží na portu 8083. Můžete přeskočit příkazy pro spuštění níže a jít rovnou na http://localhost:8083.

**Možnost 1: Použití Spring Boot Dashboard (doporučeno pro uživatele VS Code)**

Vývojový kontejner obsahuje rozšíření Spring Boot Dashboard, které poskytuje vizuální rozhraní pro správu všech Spring Boot aplikací. Najdete jej v liště aktivit na levé straně VS Code (hledejte ikonu Spring Boot).
Z panelu Spring Boot Dashboard můžete:
- Vidět všechny dostupné aplikace Spring Boot v pracovním prostoru
- Spustit/zastavit aplikace jediným kliknutím
- Zobrazit protokoly aplikací v reálném čase
- Monitorovat stav aplikací

Jednoduše klikněte na tlačítko přehrávání vedle "prompt-engineering" pro spuštění tohoto modulu, nebo spusťte všechny moduly najednou.

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

Oba skripty automaticky načtou proměnné prostředí ze souboru `.env` v kořeni a sestaví JAR soubory, pokud neexistují.

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

*Hlavní panel zobrazující všech 8 vzorů prompt engineeringu s jejich charakteristikami a použitím*

## Prozkoumávání vzorů

Webové rozhraní vám umožní experimentovat s různými strategiemi promptingu. Každý vzor řeší jiné problémy – vyzkoušejte je a zjistěte, kdy který přístup září.

### Nízká vs vysoká iniciativnost

Zeptejte se jednoduché otázky jako „Co je 15 % ze 200?“ pomocí Nízké iniciativnosti. Dostanete okamžitou, přímou odpověď. Nyní zeptejte se na něco složitého jako „Navrhni strategii cache pro API s vysokým provozem“ pomocí Vysoké iniciativnosti. Sledujte, jak model zpomalí a poskytne podrobné zdůvodnění. Stejný model, stejná struktura otázky – ale prompt říká, kolik má model přemýšlet.

<img src="../../../translated_images/cs/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Rychlý výpočet s minimálním zdůvodněním*

<img src="../../../translated_images/cs/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Komplexní strategie cache (2,8MB)*

### Vykonávání úkolů (předmluvy nástrojů)

Vícefázové pracovní postupy těží z předem plánování a průběžného komentování. Model nastíní, co udělá, komentuje každý krok a nakonec shrne výsledky.

<img src="../../../translated_images/cs/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Vytvoření REST endpointu s průvodním komentářem krok za krokem (3,9MB)*

### Sebereflektivní kód

Zkuste „Vytvoř službu na validaci e-mailů“. Místo prostého generování kódu a ukončení, model kód generuje, hodnotí podle kvalitativních kritérií, identifikuje slabiny a vylepšuje. Uvidíte, jak iteruje, dokud kód nesplní výrobní standardy.

<img src="../../../translated_images/cs/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Kompletní služba validace e-mailu (5,2MB)*

### Strukturovaná analýza

Kontroly kódu vyžadují konzistentní hodnotící rámec. Model analyzuje kód pomocí pevných kategorií (správnost, postupy, výkon, bezpečnost) s úrovněmi závažnosti.

<img src="../../../translated_images/cs/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Hodnocení kódu pomocí rámce*

### Vícekolové konverzace

Zeptejte se „Co je Spring Boot?“ a ihned navázejte „Ukaž mi příklad“. Model si pamatuje první otázku a poskytne vám konkrétní příklad Spring Bootu. Bez paměti by druhá otázka byla příliš obecná.

<img src="../../../translated_images/cs/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Zachování kontextu napříč otázkami*

### Postupné zdůvodňování

Vyberte si matematický problém a zkuste jej řešit jak s Postupným zdůvodňováním, tak s Nízkou iniciativností. Nízká iniciativnost vám jen rychle dá odpověď – rychlá, ale neprůhledná. Postupné zdůvodňování ukazuje každý výpočet a rozhodnutí.

<img src="../../../translated_images/cs/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matematický problém s explicitními kroky*

### Omezený výstup

Pokud potřebujete konkrétní formáty nebo počty slov, tento vzor vynucuje přesné dodržení. Zkuste vygenerovat shrnutí přesně se 100 slovy ve formátu odrážek.

<img src="../../../translated_images/cs/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Shrnutí strojového učení s kontrolou formátu*

## Co se skutečně učíte

**Námaha zdůvodňování mění všechno**

GPT-5.2 vám umožňuje řídit výpočetní námahu pomocí promptů. Nízká námaha znamená rychlé odpovědi s minimálním zkoumáním. Vysoká námaha znamená, že model věnuje čas důkladnému přemýšlení. Učíte se přizpůsobit námahu složitosti úkolu – neztrácejte čas na jednoduché otázky, ale ani nespěchejte u složitých rozhodnutí.

**Struktura řídí chování**

Všimli jste si XML tagů v promptech? Nejsou pouze dekorativní. Modely spolehlivěji následují strukturované pokyny než volný text. Když potřebujete vícestupňové procesy nebo složitou logiku, struktura pomáhá modelu sledovat, kde se nachází a co bude následovat.

<img src="../../../translated_images/cs/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomie dobře strukturovaného promptu s jasnými sekcemi a XML-stylovou organizací*

**Kvalita skrze sebehodnocení**

Sebereflektivní vzory fungují tak, že explicitně uvádějí kritéria kvality. Místo abyste doufali, že model „to udělá správně“, říkáte mu přesně, co „správně“ znamená: korektní logiku, zpracování chyb, výkon, bezpečnost. Model pak může hodnotit vlastní výstup a zlepšovat se. To proměňuje generování kódu z loterie na proces.

**Kontext je omezený**

Vícekolové rozhovory fungují tak, že s každým požadavkem posíláte historii zpráv. Ale je tu limit – každý model má maximální počet tokenů. Jak se konverzace zvětšují, budete potřebovat strategie, jak udržet relevantní kontext, aniž byste překročili limit. Tento modul ukazuje, jak paměť funguje; později se naučíte, kdy shrnovat, kdy zapomínat a kdy zpětně získávat informace.

## Další kroky

**Další modul:** [03-rag - RAG (Generování rozšířené vyhledáváním)](../03-rag/README.md)

---

**Navigace:** [← Předchozí: Modul 01 - Úvod](../01-introduction/README.md) | [Zpět na hlavní stránku](../README.md) | [Další: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). I když usilujeme o přesnost, mějte prosím na paměti, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho mateřském jazyce by měl být považován za rozhodující zdroj. Pro důležité informace se doporučuje využít profesionální lidský překlad. Nezodpovídáme za jakékoli nedorozumění či mylné výklady vzniklé použitím tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->