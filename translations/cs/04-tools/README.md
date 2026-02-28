# Modul 04: AI agenty s nástroji

## Obsah

- [Co se naučíte](../../../04-tools)
- [Požadavky](../../../04-tools)
- [Pochopení AI agentů s nástroji](../../../04-tools)
- [Jak funguje volání nástrojů](../../../04-tools)
  - [Definice nástrojů](../../../04-tools)
  - [Rozhodování](../../../04-tools)
  - [Vykonání](../../../04-tools)
  - [Generování odpovědi](../../../04-tools)
  - [Architektura: Spring Boot automatické propojení](../../../04-tools)
- [Řetězení nástrojů](../../../04-tools)
- [Spuštění aplikace](../../../04-tools)
- [Používání aplikace](../../../04-tools)
  - [Vyzkoušejte jednoduché použití nástroje](../../../04-tools)
  - [Otestujte řetězení nástrojů](../../../04-tools)
  - [Podívejte se na tok konverzace](../../../04-tools)
  - [Experimentujte s různými požadavky](../../../04-tools)
- [Klíčové koncepty](../../../04-tools)
  - [Vzor ReAct (rozumování a jednání)](../../../04-tools)
  - [Popisy nástrojů jsou důležité](../../../04-tools)
  - [Správa relace](../../../04-tools)
  - [Zpracování chyb](../../../04-tools)
- [Dostupné nástroje](../../../04-tools)
- [Kdy používat agenty založené na nástrojích](../../../04-tools)
- [Nástroje vs RAG](../../../04-tools)
- [Další kroky](../../../04-tools)

## Co se naučíte

Doposud jste se naučili, jak vést konverzace s AI, efektivně strukturovat podněty a zakládat odpovědi na dokumentech. Přesto však existuje zásadní omezení: jazykové modely umí generovat pouze text. Neumí zjistit počasí, provádět výpočty, dotazovat se do databází ani komunikovat s externími systémy.

Nástroje toto mění. Tím, že modelu poskytnete přístup k funkcím, které může volat, proměníte ho z generátoru textu na agenta, který může podnikat akce. Model rozhoduje, kdy potřebuje nástroj, který nástroj použít a jaké parametry předat. Váš kód vykoná funkci a vrátí výsledek. Model tento výsledek zahrne do své odpovědi.

## Požadavky

- Dokončený modul 01 (nasazení Azure OpenAI zdrojů)
- Soubor `.env` v kořenovém adresáři s přihlašovacími údaji Azure (vytvořeno příkazem `azd up` v modulu 01)

> **Poznámka:** Pokud jste modul 01 nedokončili, nejdříve postupujte podle návodu na nasazení v něm.

## Pochopení AI agentů s nástroji

> **📝 Poznámka:** Termín „agentů“ v tomto modulu označuje AI asistenty rozšířené o schopnosti volání nástrojů. Toto se liší od vzorů **Agentic AI** (autonomní agenti s plánováním, pamětí a víceúrovňovým uvažováním), které probereme v [Modul 05: MCP](../05-mcp/README.md).

Bez nástrojů může jazykový model generovat pouze text na základě svého tréninku. Zeptáte-li se jej na aktuální počasí, musí jen hádat. S nástroji může zavolat API počasí, provést výpočty nebo dotaz do databáze — a skutečné výsledky pak zakomponovat do odpovědi.

<img src="../../../translated_images/cs/what-are-tools.724e468fc4de64da.webp" alt="Bez nástrojů vs S nástroji" width="800"/>

*Bez nástrojů model jen hádá — s nástroji může volat API, spouštět výpočty a vracet data v reálném čase.*

AI agent s nástroji následuje vzor **Rozumování a jednání (ReAct)**. Model nerezpondé jen tak - uvažuje o tom, co potřebuje, jedná voláním nástroje, sleduje výsledek a rozhodne, zda jednat znovu, nebo dodat konečnou odpověď:

1. **Rozumování** — agent analyzuje uživatelovu otázku a určí, jaké informace potřebuje
2. **Jednání** — agent vybere správný nástroj, vygeneruje správné parametry a zavolá ho
3. **Sledování** — agent přijme výstup nástroje a vyhodnotí ho
4. **Opakování nebo odpověď** — pokud je potřeba více dat, agent se vrátí zpět; jinak složí odpověď v přirozeném jazyce

<img src="../../../translated_images/cs/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Vzor" width="800"/>

*Cyklus ReAct — agent rozumuje, co udělat, jedná voláním nástroje, sleduje výsledek a opakuje dokud nedodá konečnou odpověď.*

Toto probíhá automaticky. Definujete nástroje a jejich popisy. Model se postará o rozhodování, kdy a jak je použít.

## Jak funguje volání nástrojů

### Definice nástrojů

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Definujete funkce s jasnými popisy a specifikacemi parametrů. Model vidí tyto popisy v systému promptu a chápe, co každý nástroj dělá.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Logika vyhledávání počasí
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Asistent je automaticky propojen Spring Bootem s:
// - Beanem ChatModel
// - Všechny metody @Tool z tříd označených @Component
// - ChatMemoryProvider pro správu relací
```

Diagram níže rozebírá každou anotaci a ukazuje, jak každý prvek pomáhá AI pochopit, kdy nástroj volat a jaké argumenty předat:

<img src="../../../translated_images/cs/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomie definic nástrojů" width="800"/>

*Anatomie definice nástroje — @Tool říká AI, kdy ho použít, @P popisuje každý parametr a @AiService vše při spuštění propojistí.*

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) a zeptejte se:
> - „Jak bych integroval skutečné API počasí jako OpenWeatherMap místo testovacích dat?“
> - „Co dělá dobrý popis nástroje, který pomáhá AI používat ho správně?“
> - „Jak řešit chyby API a limity volání v implementacích nástrojů?“

### Rozhodování

Když uživatel zeptá „Jaké je počasí v Seattlu?“, model náhodně nevybere nástroj. Porovná uživatelův záměr se všemi popisy nástrojů, které mu jsou dostupné, vyhodnotí relevanci a vybere nejlepší shodu. Potom generuje strukturované volání funkce s pravými parametry — v tomto případě nastavení `location` na `"Seattle"`.

Pokud žádný nástroj neodpovídá uživatelově požadavku, model odpovídá ze své vlastní znalosti. Pokud se hodí více nástrojů, vybere ten nejpřesnější.

<img src="../../../translated_images/cs/decision-making.409cd562e5cecc49.webp" alt="Jak AI rozhoduje, který nástroj použít" width="800"/>

*Model vyhodnocuje každý dostupný nástroj v porovnání s uživatelovým záměrem a vybírá nejlepší shodu — proto je důležité psát jasné, specifické popisy nástrojů.*

### Vykonání

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot automaticky propojí deklarativní rozhraní `@AiService` se všemi registrovanými nástroji a LangChain4j volání nástrojů vykonává automaticky. Za scénou celý průběh volání nástroje prochází šesti fázemi — od přirozeně znějící otázky uživatele až po výslednou odpověď v přirozeném jazyce:

<img src="../../../translated_images/cs/tool-calling-flow.8601941b0ca041e6.webp" alt="Průběh volání nástroje" width="800"/>

*Konečný průběh — uživatel položí otázku, model vybere nástroj, LangChain4j ho spustí a model výsledek zakomponuje do odpovědi.*

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) a zeptejte se:
> - „Jak funguje vzor ReAct a proč je účinný pro AI agenty?“
> - „Jak agent rozhoduje, který nástroj použít a v jakém pořadí?“
> - „Co se stane, když selže vykonání nástroje - jak robustně řešit chyby?“

### Generování odpovědi

Model přijme data o počasí a naformátuje je do odpovědi v přirozeném jazyce pro uživatele.

### Architektura: Spring Boot automatické propojení

Tento modul využívá integraci LangChain4j se Spring Bootem pomocí deklarativních rozhraní `@AiService`. Při spuštění Spring Boot detekuje každý `@Component`, který obsahuje `@Tool` metody, váš `ChatModel` bean a `ChatMemoryProvider` — a pak vše propojí do jednoho rozhraní `Assistant` bez potřeby šablonového kódu.

<img src="../../../translated_images/cs/spring-boot-wiring.151321795988b04e.webp" alt="Architektura Spring Boot automatického propojení" width="800"/>

*Rozhraní @AiService propojuje ChatModel, komponenty nástrojů a poskytovatele paměti — Spring Boot se stará o veškeré propojování automaticky.*

Hlavní výhody tohoto přístupu:

- **Spring Boot automatické propojení** — ChatModel a nástroje jsou automaticky injektovány
- **Vzor @MemoryId** — Automatická správa paměti podle relace
- **Jeden instance** — Assistant vytvořen jednou a opakovaně použít pro lepší výkon
- **Typově bezpečné vykonání** — Java metody jsou volány přímo s konverzí typů
- **Orchestrace vícero záběrů** — Řeší automaticky řetězení nástrojů
- **Nulový šablonový kód** — Žádné ruční volání `AiServices.builder()` nebo manuální mapy paměti

Alternativní přístupy (ruční `AiServices.builder()`) vyžadují více kódu a chybí výhody integrace Spring Bootu.

## Řetězení nástrojů

**Řetězení nástrojů** — skutečná síla agentů založených na nástrojích se projeví, když jedna otázka vyžaduje použití více nástrojů. Položíte otázku „Jaké je počasí v Seattlu ve Fahrenheitech?“ a agent automaticky propojí dva nástroje: nejdříve zavolá `getCurrentWeather` pro získání teploty ve stupních Celsia, poté předá tento výsledek do `celsiusToFahrenheit` pro převod — vše v rámci jednoho kola konverzace.

<img src="../../../translated_images/cs/tool-chaining-example.538203e73d09dd82.webp" alt="Příklad řetězení nástrojů" width="800"/>

*Řetězení nástrojů v akci — agent zavolá nejdříve getCurrentWeather, pak přeposílá výsledek ve °C do celsiusToFahrenheit a dodá kombinovanou odpověď.*

Takto to vypadá v běžící aplikaci — agent řetězí dvě volání nástrojů v jedné konverzační otočce:

<a href="images/tool-chaining.png"><img src="../../../translated_images/cs/tool-chaining.3b25af01967d6f7b.webp" alt="Řetězení nástrojů" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Skutečný výstup aplikace — agent automaticky řetězí getCurrentWeather → celsiusToFahrenheit v jednom kole.*

**Elegantní selhání** — požádáte-li o počasí ve městě, které není v testovacích datech, nástroj vrátí chybovou zprávu a AI vysvětlí, že nemůže pomoci, místo aby spadla. Nástroje selhávají bezpečně.

<img src="../../../translated_images/cs/error-handling-flow.9a330ffc8ee0475c.webp" alt="Tok zpracování chyb" width="800"/>

*Když nástroj selže, agent chybu zachytí a odpoví užitečným vysvětlením místo pádu.*

To vše probíhá v jedné konverzační otočce. Agent orchestruje vícero volání nástrojů autonomně.

## Spuštění aplikace

**Ověření nasazení:**

Ujistěte se, že soubor `.env` v kořenovém adresáři obsahuje přihlašovací údaje Azure (vytvořené během modulu 01):
```bash
cat ../.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spuštění aplikace:**

> **Poznámka:** Pokud jste již spustili všechny aplikace pomocí `./start-all.sh` z modulu 01, tento modul již běží na portu 8084. Můžete přeskočit níže uvedené příkazy a rovnou přejít na http://localhost:8084.

**Možnost 1: Použití Spring Boot Dashboard (doporučeno pro uživatele VS Code)**

Vývojářský kontejner obsahuje rozšíření Spring Boot Dashboard, které poskytuje vizuální rozhraní pro správu všech Spring Boot aplikací. Najdete jej v Activity Bar vlevo ve VS Code (ikona Spring Boot).

V Spring Boot Dashboard můžete:
- Vidět všechny dostupné Spring Boot aplikace v workspace
- Spustit/zastavit aplikace jedním kliknutím
- Prohlížet logy aplikací v reálném čase
- Monitorovat stav aplikací

Stačí kliknout na tlačítko play vedle „tools“ pro spuštění tohoto modulu, nebo spustit všechny moduly najednou.

<img src="../../../translated_images/cs/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

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
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

Oba skripty automaticky načtou proměnné prostředí ze souboru `.env` v kořenovém adresáři a pokud JAR soubory neexistují, sestaví je.

> **Poznámka:** Pokud chcete ručně sestavit všechny moduly před spuštěním:
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

Otevřete http://localhost:8084 ve svém prohlížeči.

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

## Používání aplikace

Aplikace nabízí webové rozhraní, kde můžete komunikovat s AI agentem, který má přístup k nástrojům pro počasí a převod teplot.

<a href="images/tools-homepage.png"><img src="../../../translated_images/cs/tools-homepage.4b4cd8b2717f9621.webp" alt="Rozhraní AI agentů s nástroji" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Rozhraní AI agentů s nástroji - rychlé příklady a chatovací rozhraní pro interakci s nástroji*

### Vyzkoušejte jednoduché použití nástroje
Začněte přímočarou žádostí: „Převést 100 stupňů Fahrenheita na Celsia“. Agent rozpozná, že potřebuje nástroj pro převod teploty, zavolá jej s správnými parametry a vrátí výsledek. Všimněte si, jak přirozeně to působí – nestanovili jste, který nástroj použít nebo jak jej volat.

### Testování řetězení nástrojů

Nyní zkuste něco složitějšího: „Jaké je počasí v Seattlu a převést to na Fahrenheit?“ Sledujte, jak agent postupuje krok za krokem. Nejprve získá počasí (které vrací Celsia), rozpozná, že je třeba převést na Fahrenheit, zavolá nástroj pro převod a zkombinuje oba výsledky do jedné odpovědi.

### Sledujte tok konverzace

Chatovací rozhraní uchovává historii konverzace, což umožňuje vícetahové interakce. Vidíte všechny předchozí dotazy a odpovědi, což usnadňuje sledování konverzace a pochopení, jak si agent buduje kontext přes několik výměn.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/cs/tools-conversation-demo.89f2ce9676080f59.webp" alt="Konverzace s více voláními nástrojů" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Víceotáčková konverzace ukazující jednoduché převody, vyhledávání počasí a řetězení nástrojů*

### Experimentujte s různými dotazy

Vyzkoušejte různé kombinace:
- Vyhledávání počasí: „Jaké je počasí v Tokiu?“
- Převody teplot: „Co je 25 °C v Kelvinech?“
- Kombinované dotazy: „Zkontroluj počasí v Paříži a řekni mi, jestli je nad 20 °C“

Všimněte si, jak agent interpretuje přirozený jazyk a mapuje jej na vhodná volání nástrojů.

## Klíčové koncepty

### Vzor ReAct (Uvažování a akce)

Agent střídá uvažování (rozhodování, co dělat) a akci (používání nástrojů). Tento vzor umožňuje autonomní řešení problémů místo pouhého plnění instrukcí.

### Popisy nástrojů jsou důležité

Kvalita vašich popisů nástrojů přímo ovlivňuje, jak dobře je agent používá. Jasné a specifické popisy pomáhají modelu pochopit, kdy a jak každý nástroj volat.

### Správa sezení

Anotace `@MemoryId` umožňuje automatickou správu paměti na základě sezení. Každé ID sezení získává svou vlastní instanci `ChatMemory` spravovanou beanem `ChatMemoryProvider`, takže více uživatelů může současně komunikovat s agentem, aniž by se jejich konverzace prolínaly.

<img src="../../../translated_images/cs/session-management.91ad819c6c89c400.webp" alt="Správa sezení s @MemoryId" width="800"/>

*Každé ID sezení odpovídá izolované historii konverzace — uživatelé nikdy nevidí zprávy ostatních.*

### Zpracování chyb

Nástroje mohou selhat — API mohou vypršet, parametry mohou být neplatné, externí služby mohou padnout. Produkční agenti potřebují zpracování chyb, aby model mohl problémy vysvětlit nebo zkusit alternativy místo pádu celé aplikace. Když nástroj vyhodí výjimku, LangChain4j ji zachytí a předá modelu chybovou zprávu, která pak může problém vysvětlit přirozeným jazykem.

## Dostupné nástroje

Diagram níže ukazuje široký ekosystém nástrojů, které můžete vytvořit. Tento modul demonstruje nástroje pro počasí a teplotu, ale stejný vzor `@Tool` funguje pro jakoukoli javasovou metodu — od dotazů do databáze po zpracování plateb.

<img src="../../../translated_images/cs/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ekosystém nástrojů" width="800"/>

*Jakákoli java metoda anotovaná `@Tool` je pro AI dostupná — vzor se rozšiřuje na databáze, API, e-maily, operace se soubory a další.*

## Kdy používat agenti založené na nástrojích

<img src="../../../translated_images/cs/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Kdy používat nástroje" width="800"/>

*Rychlý průvodce — nástroje jsou vhodné pro data v reálném čase, výpočty a akce; obecné znalosti a kreativita je nepotřebují.*

**Používejte nástroje, když:**
- Odpověď vyžaduje data v reálném čase (počasí, ceny akcií, zásoby)
- Potřebujete provést složitější výpočty než základní matematiku
- Přistupujete k databázím či API
- Provádíte akce (odesílání e-mailů, vytváření tiketů, aktualizace záznamů)
- Kombinujete více zdrojů dat

**Nepoužívejte nástroje, když:**
- Odpověď lze získat z obecné znalosti
- Odpověď je čistě konverzační
- Latence nástroje by byla příliš dlouhá a zpomalila by zážitek

## Nástroje vs RAG

Moduly 03 a 04 oba rozšiřují schopnosti AI, ale zásadně odlišnými způsoby. RAG dává modelu přístup k **znalostem** získáním dokumentů. Nástroje dávají modelu schopnost podnikat **akce** voláním funkcí.

<img src="../../../translated_images/cs/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Porovnání Nástrojů a RAG" width="800"/>

*RAG získává informace ze statických dokumentů — Nástroje vykonávají akce a získávají dynamická, aktuální data. Mnoho produkčních systémů kombinuje obojí.*

V praxi mnoho produkčních systémů kombinuje oba přístupy: RAG pro založení odpovědí na vašich dokumentech a Nástroje pro získávání živých dat nebo provádění operací.

## Další kroky

**Další modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigace:** [← Předchozí: Modul 03 - RAG](../03-rag/README.md) | [Zpět na hlavní stránku](../README.md) | [Další: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o odmítnutí odpovědnosti**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o přesnost, mějte prosím na paměti, že automatické překlady mohou obsahovat chyby nebo nepřesnosti. Originální dokument v jeho původním jazyce by měl být považován za autoritativní zdroj. Pro zásadní informace se doporučuje profesionální lidský překlad. Nejsme odpovědní za jakákoliv nedorozumění nebo nesprávné výklady vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->