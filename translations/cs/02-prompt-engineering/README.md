# Modul 02: Prompt Engineering s GPT-5.2

## Obsah

- [Co se naučíte](../../../02-prompt-engineering)
- [Předpoklady](../../../02-prompt-engineering)
- [Pochopení prompt engineeringu](../../../02-prompt-engineering)
- [Jak toto využívá LangChain4j](../../../02-prompt-engineering)
- [Základní vzory](../../../02-prompt-engineering)
- [Použití existujících Azure zdrojů](../../../02-prompt-engineering)
- [Screenshoty aplikace](../../../02-prompt-engineering)
- [Prozkoumání vzorů](../../../02-prompt-engineering)
  - [Nízký vs vysoký zápal](../../../02-prompt-engineering)
  - [Provedení úkolu (preambule nástrojů)](../../../02-prompt-engineering)
  - [Kód se sebereflexí](../../../02-prompt-engineering)
  - [Strukturovaná analýza](../../../02-prompt-engineering)
  - [Vícekrokový chat](../../../02-prompt-engineering)
  - [Postupné uvažování](../../../02-prompt-engineering)
  - [Omezený výstup](../../../02-prompt-engineering)
- [Co se vlastně učíte](../../../02-prompt-engineering)
- [Další kroky](../../../02-prompt-engineering)

## Co se naučíte

V předchozím modulu jste viděli, jak paměť umožňuje konverzační AI a použili jste GitHub modely pro základní interakce. Teď se zaměříme na to, jak klást otázky – tedy samotné prompty – pomocí Azure OpenAI GPT-5.2. Způsob, jakým strukturalizujete své prompty, dramaticky ovlivňuje kvalitu odpovědí, které dostanete.

Použijeme GPT-5.2, protože zavádí řízení uvažování – můžete modelu říct, kolik kroků uvažování má před odpovědí vykonat. To zpřehledňuje různé strategie promptingů a pomáhá vám pochopit, kdy která přístup použít. Také využijeme Azure, které má méně omezení rychlosti u GPT-5.2 oproti GitHub modelům.

## Předpoklady

- Dokončen Modul 01 (nasazeny Azure OpenAI zdroje)
- `.env` soubor v kořenovém adresáři s Azure přihlašovacími údaji (vytvořený příkazem `azd up` v Modulu 01)

> **Poznámka:** Pokud jste Modul 01 nedokončili, nejprve postupujte podle jeho nasazovacích instrukcí.

## Pochopení prompt engineeringu

Prompt engineering je o navrhování vstupního textu, který vám konzistentně přinese požadované výsledky. Nejde jen o kladení otázek – jde o strukturování požadavků tak, aby model přesně pochopil, co chcete a jak to doručit.

Představte si, že dáváte instrukce kolegovi. „Oprav chybu“ je vágní. „Oprav výjimku null pointer v UserService.java na řádku 45 přidáním kontroly na null“ je konkrétní. Jazykové modely fungují stejně – konkrétnost a struktura jsou důležité.

## Jak toto využívá LangChain4j

Tento modul demonstruje pokročilé vzory promptingu na stejné LangChain4j základně jako předchozí moduly, s důrazem na strukturu promptů a řízení uvažování.

<img src="../../../translated_images/cs/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*Jak LangChain4j propojuje vaše prompty s Azure OpenAI GPT-5.2*

**Závislosti** – Modul 02 používá následující závislosti langchain4j definované v `pom.xml`:
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**Konfigurace OpenAiOfficialChatModel** – [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Chat model je manuálně konfigurován jako Spring bean pomocí oficiálního OpenAI klienta, který podporuje Azure OpenAI endpointy. Klíčový rozdíl oproti Modulu 01 je ve způsobu strukturování promptů odesílaných do `chatModel.chat()`, nikoli v samotném nastavení modelu.

**Systémové a uživatelské zprávy** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j odděluje typy zpráv pro přehlednost. `SystemMessage` nastavuje chování a kontext AI (například „Jste recenzent kódu“), zatímco `UserMessage` obsahuje vlastní požadavek. Toto oddělení umožňuje udržet konzistentní chování AI při různých uživatelských dotazech.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/cs/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage poskytuje trvalý kontext, zatímco UserMessages obsahují jednotlivé požadavky*

**MessageWindowChatMemory pro vícekrokové konverzace** – Pro vícekrokový konverzační vzor znovu používáme `MessageWindowChatMemory` z Modulu 01. Každá relace dostane vlastní instanci paměti uloženou v `Map<String, ChatMemory>`, což umožňuje více souběžných konverzací bez míchání kontextu.

**Šablony promptů** – Skutečný důraz je zde na prompt engineering, nikoli nové LangChain4j API. Každý vzor (nízký zápal, vysoký zápal, provádění úkolů a další) používá stejnou metodu `chatModel.chat(prompt)` ale s pečlivě strukturovanými texty promptů. XML tagy, instrukce a formátování jsou součástí prompt textu, nikoli funkcionalit LangChain4j.

**Řízení uvažování** – Uvažování GPT-5.2 se řídí instrukcemi v promptu jako například „maximálně 2 kroky uvažování“ nebo „prozkoumej důkladně“. Jsou to techniky prompt engineeringu, ne konfigurace LangChain4j knihovny. Knihovna jednoduše předává vaše prompty modelu.

Klíčové shrnutí: LangChain4j poskytuje infrastrukturu (připojení k modelu přes [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), paměť, správu zpráv přes [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), zatímco tento modul vás učí, jak vytvořit efektivní prompty v rámci této infrastruktury.

## Základní vzory

Ne všechny problémy potřebují stejný přístup. Některé dotazy vyžadují rychlé odpovědi, jiné hluboké přemýšlení. Některé potřebují viditelné uvažování, jiné pouze výsledky. Tento modul pokrývá osm vzorů promptingu – každý optimalizovaný pro různé scénáře. Vyzkoušíte si všechny, abyste zjistili, kdy který přístup funguje nejlépe.

<img src="../../../translated_images/cs/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Přehled osmi vzorů prompt engineeringu a jejich použití*

<img src="../../../translated_images/cs/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Nízký zápal (rychlý, přímý) vs vysoký zápal (důkladný, explorativní) přístupy k uvažování*

**Nízký zápal (rychlý a zaměřený)** – Pro jednoduché otázky, kde chcete rychlé, přímé odpovědi. Model provádí minimální uvažování – maximálně 2 kroky. Použijte to pro výpočty, vyhledávání nebo přímočaré dotazy.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Vyzkoušejte s GitHub Copilot:** Otevřete [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) a zeptejte se:
> - „Jaký je rozdíl mezi vzory nízkého a vysokého zápalu promptingu?“
> - „Jak XML tagy v promptech pomáhají strukturovat odpověď AI?“
> - „Kdy mám použít vzory sebereflexe oproti přímé instrukci?“

**Vysoký zápal (hluboký a důkladný)** – Pro složité problémy, kde chcete komplexní analýzu. Model podrobně zkoumá a ukazuje detailní uvažování. Použijte to pro návrh systémů, architektonická rozhodnutí nebo komplexní výzkum.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Provedení úkolu (postup krok za krokem)** – Pro vícekrokové pracovní postupy. Model poskytne plán, komentuje každý krok při práci a nakonec shrnutí. Použijte to pro migrace, implementace nebo jakýkoli vícekrokový proces.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought prompting explicitně žádá model o zobrazení jeho uvažovacího procesu, což zlepšuje přesnost u složitých úkolů. Postupné rozdělení pomáhá lidem i AI porozumět logice.

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Zeptejte se na tento vzor:
> - „Jak bych adaptoval vzor provedení úkolu pro dlouhotrvající operace?“
> - „Jaké jsou nejlepší praktiky pro strukturování preambulí nástrojů v produkčních aplikacích?“
> - „Jak zachytit a zobrazit průběžné aktualizace v uživatelském rozhraní?“

<img src="../../../translated_images/cs/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Workflow Plán → Provedení → Shrnutí pro vícekrokové úkoly*

**Kód se sebereflexí** – Pro generování produkčně kvalitního kódu. Model generuje kód, kontroluje jej podle kvalitativních kritérií a iterativně jej zlepšuje. Použijte při tvorbě nových funkcí nebo služeb.

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

<img src="../../../translated_images/cs/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iterační smyčka zlepšování – generuj, vyhodnoť, identifikuj problémy, zlepšuj, opakuj*

**Strukturovaná analýza** – Pro konzistentní hodnocení. Model recenzuje kód za použití pevného rámce (správnost, praxe, výkon, bezpečnost). Použijte pro kódové recenze nebo hodnocení kvality.

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
> - „Jak přizpůsobit analytický rámec pro různé typy kódových recenzí?“
> - „Jak nejlépe parsovat a programově zpracovat strukturovaný výstup?“
> - „Jak zajistit konzistentní úroveň závažnosti napříč různými recenzními sezeními?“

<img src="../../../translated_images/cs/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Čtyřkategorický rámec pro konzistentní kódové recenze se závažnostními úrovněmi*

**Vícekrokový chat** – Pro konverzace vyžadující kontext. Model si pamatuje předchozí zprávy a staví na nich. Použijte pro interaktivní podpůrné relace nebo komplexní Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/cs/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Jak se kumuluje kontext konverzace přes vícero kroků až do omezení tokenů*

**Postupné uvažování** – Pro problémy vyžadující viditelnou logiku. Model ukazuje explicitní uvažování pro každý krok. Použijte pro matematické problémy, logické hádanky nebo když potřebujete pochopit myšlenkový proces.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/cs/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Rozdělění problému na explicitní logické kroky*

**Omezený výstup** – Pro odpovědi se specifickými formátovacími požadavky. Model důsledně dodržuje pravidla formátu a délky. Použijte pro shrnutí nebo kdy potřebujete přesnou strukturu výstupu.

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

<img src="../../../translated_images/cs/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Uplatnění specifických požadavků na formát, délku a strukturu*

## Použití existujících Azure zdrojů

**Ověření nasazení:**

Zkontrolujte, zda existuje `.env` soubor v kořenovém adresáři s Azure přihlašovacími údaji (vytvořený během Modulu 01):
```bash
cat ../.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spuštění aplikace:**

> **Poznámka:** Pokud jste již spustili všechny aplikace pomocí `./start-all.sh` z Modulu 01, tento modul již běží na portu 8083. Můžete přeskočit startovací příkazy níže a jít přímo na http://localhost:8083.

**Možnost 1: Použití Spring Boot Dashboard (doporučeno pro uživatele VS Code)**

Dev kontejner obsahuje rozšíření Spring Boot Dashboard, které poskytuje vizuální rozhraní pro správu všech Spring Boot aplikací. Najdete jej na postranním panelu vlevo ve VS Code (ikona Spring Boot).

Ze Spring Boot Dashboard můžete:
- Vidět všechny dostupné Spring Boot aplikace v workspace
- Spouštět/zastavovat aplikace jedním kliknutím
- Sledovat logy aplikace v reálném čase
- Monitorovat stav aplikací

Stačí kliknout na tlačítko play vedle „prompt-engineering“ pro spuštění tohoto modulu, nebo spustit všechny moduly najednou.

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

Oba skripty automaticky načtou proměnné prostředí z kořenového `.env` souboru a sestaví JARy, pokud neexistují.

> **Poznámka:** Pokud raději sestavíte všechny moduly manuálně před spuštěním:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Otevřete v prohlížeči http://localhost:8083.

**Zastavení:**

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

*Hlavní dashboard zobrazující všech 8 vzorů prompt engineeringu s jejich charakteristikami a použitím*

## Prozkoumání vzorů

Webové rozhraní vám umožňuje experimentovat s různými strategiemi promptování. Každý vzor řeší jiné problémy – vyzkoušejte je a uvidíte, kdy který přístup nejvíce vynikne.

### Nízký vs vysoký zápal

Zeptejte se jednoduché otázky jako „Kolik je 15 % ze 200?“ pomocí nízkého zápalu. Dostanete okamžitou, přímou odpověď. Teď se zeptejte na něco složitého jako „Navrhni strategii cachování pro API s vysokou návštěvností“ s vysokým zápalem. Sledujte, jak model zpomalí a poskytne detailní uvažování. Stejný model, stejná struktura otázky – ale prompt mu říká, kolik má uvažovat.
<img src="../../../translated_images/cs/low-eagerness-demo.898894591fb23aa0.webp" alt="Ukázka nízké ochoty" width="800"/>

*Rychlý výpočet s minimálním uvažováním*

<img src="../../../translated_images/cs/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Ukázka vysoké ochoty" width="800"/>

*Komplexní strategie ukládání do mezipaměti (2,8MB)*

### Provádění úkolu (Úvodní texty nástrojů)

Vícekrokové pracovní postupy těží z předem plánování a průběžného komentování. Model nastíní, co udělá, popisuje každý krok a pak shrne výsledky.

<img src="../../../translated_images/cs/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Ukázka provádění úkolu" width="800"/>

*Vytvoření REST endpointu s krokovým komentářem (3,9MB)*

### Sebereflektující kód

Vyzkoušejte "Vytvoř službu pro ověření emailu". Místo pouhého generování kódu a zastavení model vytváří, hodnotí podle kriterií kvality, identifikuje slabiny a zlepšuje. Uvidíte iterace až k výrobním standardům.

<img src="../../../translated_images/cs/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Ukázka sebereflektujícího kódu" width="800"/>

*Kompletní služba ověření emailu (5,2MB)*

### Strukturovaná analýza

Code review vyžaduje konzistentní hodnotící rámce. Model analyzuje kód podle pevných kategorií (správnost, praktiky, výkon, bezpečnost) s úrovněmi závažnosti.

<img src="../../../translated_images/cs/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Ukázka strukturované analýzy" width="800"/>

*Kódová recenze založená na rámci*

### Vícekolové chatování

Zeptejte se „Co je Spring Boot?“ a hned potom „Ukaž mi příklad“. Model si pamatuje první otázku a dává příklad Spring Boot konkrétně. Bez paměti by druhá otázka byla příliš vágní.

<img src="../../../translated_images/cs/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Ukázka vícekolového chatu" width="800"/>

*Zachování kontextu přes dotazy*

### Postupné uvažování

Vyberte matematický problém a vyzkoušejte jej jak s postupným uvažováním, tak s nízkou ochotou. Nízká ochota dává jen výsledek - rychle, ale neprůhledně. Postupné ukazuje každý výpočet a rozhodnutí.

<img src="../../../translated_images/cs/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Ukázka krok za krokem uvažování" width="800"/>

*Matematický problém s explicitními kroky*

### Omezovaný výstup

Pokud potřebujete specifické formáty nebo počet slov, tento vzor přísně dodržuje pravidla. Vyzkoušejte generovat shrnutí přesně se 100 slovy v bodech.

<img src="../../../translated_images/cs/constrained-output-demo.567cc45b75da1633.webp" alt="Ukázka omezovaného výstupu" width="800"/>

*Shrnutí strojového učení s kontrolou formátu*

## Co se skutečně učíte

**Úsilí při uvažování mění vše**

GPT-5.2 umožňuje ovládat výpočetní úsilí svými podněty. Nízké úsilí znamená rychlé odpovědi s minimálním zkoumáním. Vysoké úsilí znamená, že model věnuje čas hlubokému přemýšlení. Učíte se svázat úsilí s komplexností úkolu – neztrácejte čas na jednoduché otázky, ale ani nespěchejte u složitých rozhodnutí.

**Struktura řídí chování**

Vidíte XML tagy v podnětech? Nejsou jen dekorace. Modely spolehlivěji následují strukturované instrukce než volný text. Když potřebujete vícekrokové procesy nebo složitou logiku, struktura pomáhá modelu sledovat, kde je a co přijde dál.

<img src="../../../translated_images/cs/prompt-structure.a77763d63f4e2f89.webp" alt="Struktura podnětu" width="800"/>

*Anatomie dobře strukturovaného podnětu s jasnými sekcemi a XML stylem*

**Kvalita přes sebehodnocení**

Sebereflektující vzory fungují tím, že kvalitu explicitně definují. Místo doufání, že model „to udělá správně“, říkáte mu přesně, co „správně“ znamená: správná logika, ošetření chyb, výkonnost, bezpečnost. Model pak může vyhodnotit svůj výstup a zlepšit se. To proměňuje generování kódu z loterie na proces.

**Kontext je omezený**

Vícekrokové konverzace fungují tak, že s každou žádostí zahrnují historii zpráv. Ale existuje limit – každý model má maximální počet tokenů. Jak konverzace roste, budete potřebovat strategie, jak zachovat relevantní kontext, aniž byste dosáhli stropu. Tento modul ukazuje, jak paměť funguje; později se naučíte, kdy shrnovat, kdy zapomenout a kdy vyhledat.

## Další kroky

**Další modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigace:** [← Předchozí: Modul 01 - Úvod](../01-introduction/README.md) | [Zpět na hlavní stránku](../README.md) | [Další: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o přesnost, mějte prosím na paměti, že automatické překlady mohou obsahovat chyby nebo nepřesnosti. Originální dokument v jeho rodném jazyce je třeba považovat za závazný zdroj. Pro důležité informace se doporučuje profesionální lidský překlad. Nejsme odpovědni za jakákoliv nedorozumění nebo mylné výklady vzniklé použitím tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->