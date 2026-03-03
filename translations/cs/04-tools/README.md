# Modul 04: AI agenti s nástroji

## Obsah

- [Co se naučíte](../../../04-tools)
- [Předpoklady](../../../04-tools)
- [Porozumění AI agentům s nástroji](../../../04-tools)
- [Jak funguje volání nástrojů](../../../04-tools)
  - [Definice nástrojů](../../../04-tools)
  - [Rozhodování](../../../04-tools)
  - [Provedení](../../../04-tools)
  - [Generování odpovědi](../../../04-tools)
  - [Architektura: Spring Boot automatické propojení](../../../04-tools)
- [Řetězení nástrojů](../../../04-tools)
- [Spuštění aplikace](../../../04-tools)
- [Používání aplikace](../../../04-tools)
  - [Vyzkoušejte jednoduché použití nástroje](../../../04-tools)
  - [Otestujte řetězení nástrojů](../../../04-tools)
  - [Sledujte tok konverzace](../../../04-tools)
  - [Experimentujte s různými požadavky](../../../04-tools)
- [Klíčové koncepty](../../../04-tools)
  - [Vzor ReAct (Reasoning and Acting)](../../../04-tools)
  - [Popisy nástrojů jsou důležité](../../../04-tools)
  - [Správa sezení](../../../04-tools)
  - [Zpracování chyb](../../../04-tools)
- [Dostupné nástroje](../../../04-tools)
- [Kdy používat agenty založené na nástrojích](../../../04-tools)
- [Nástroje vs RAG](../../../04-tools)
- [Další kroky](../../../04-tools)

## Co se naučíte

Dosud jste se naučili, jak vést konverzace s AI, efektivně strukturovat podněty a zakotvit odpovědi ve vašich dokumentech. Ale stále existuje základní omezení: jazykové modely mohou generovat pouze text. Nemohou zjistit počasí, provádět výpočty, dotazovat se do databází nebo komunikovat s externími systémy.

Nástroje to mění. Tím, že modelu poskytnete přístup k funkcím, které může volat, proměníte ho z generátoru textu na agenta, který může podnikat akce. Model rozhoduje, kdy potřebuje nástroj, který nástroj použít a jaké parametry předat. Váš kód provede funkci a vrátí výsledek. Model začlení tento výsledek do své odpovědi.

## Předpoklady

- Dokončený [Modul 01 - Úvod](../01-introduction/README.md) (nasazeny zdroje Azure OpenAI)
- Doporučuje se dokončit předchozí moduly (tento modul odkazuje na [koncepty RAG z Modulu 03](../03-rag/README.md) v porovnání Nástroje vs RAG)
- Soubor `.env` v kořenovém adresáři s Azure přihlašovacími údaji (vytvořený pomocí `azd up` v Modulu 01)

> **Poznámka:** Pokud jste nedokončili modul 01, postupujte nejprve podle instrukcí pro nasazení tam.

## Porozumění AI agentům s nástroji

> **📝 Poznámka:** Termín „agenti“ v tomto modulu označuje AI asistenty rozšířené o schopnosti volání nástrojů. Toto se liší od vzorů **Agentic AI** (autonomní agenti s plánováním, pamětí a vícefázovým uvažováním), které pokryjeme v [Modulu 05: MCP](../05-mcp/README.md).

Bez nástrojů může jazykový model generovat pouze text na základě svého tréninkového datasetu. Zeptáte-li se ho na aktuální počasí, musí hádat. Dejte mu nástroje a může zavolat API počasí, provést výpočty nebo dotaz do databáze — a pak tyto skutečné výsledky vložit do své odpovědi.

<img src="../../../translated_images/cs/what-are-tools.724e468fc4de64da.webp" alt="Bez nástrojů vs s nástroji" width="800"/>

*Bez nástrojů model pouze hádá — s nástroji může volat API, provádět výpočty a vracet aktuální data.*

AI agent s nástroji následuje vzor **Reasoning and Acting (ReAct)**. Model nejen odpovídá — přemýšlí o tom, co potřebuje, jedná voláním nástroje, sleduje výsledek a pak se rozhodne, zda má opět jednat nebo dodat konečnou odpověď:

1. **Uvažovat** — Agent analyzuje uživatelovu otázku a zjistí, jaké informace potřebuje
2. **Jednat** — Agent vybere správný nástroj, vygeneruje správné parametry a zavolá ho
3. **Pozorovat** — Agent přijme výstup nástroje a vyhodnotí výsledek
4. **Opakovat nebo odpovědět** — Pokud je potřeba více dat, agent cyklus zopakuje; jinak sestaví odpověď v přirozeném jazyce

<img src="../../../translated_images/cs/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Vzor ReAct" width="800"/>

*Cyklický proces ReAct — agent uvažuje, co má udělat, jedná voláním nástroje, pozoruje výsledek a opakuje, dokud nemůže dodat konečnou odpověď.*

To probíhá automaticky. Definujete nástroje a jejich popisy. Model se postará o rozhodování, kdy a jak je použít.

## Jak funguje volání nástrojů

### Definice nástrojů

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Definujete funkce s jasnými popisy a specifikacemi parametrů. Model tyto popisy vidí ve svém systémovém podnětu a chápe, k čemu každý nástroj slouží.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Vaše logika vyhledávání počasí
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Asistent je automaticky propojený pomocí Spring Boot s:
// - ChatModel bean
// - Všechny metody označené @Tool z tříd @Component
// - ChatMemoryProvider pro správu relací
```

Níže uvedený diagram rozebírá každou anotaci a ukazuje, jak každý prvek pomáhá AI pochopit, kdy nástroj volat a jaké argumenty předat:

<img src="../../../translated_images/cs/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomie definice nástroje" width="800"/>

*Anatomie definice nástroje — @Tool říká AI, kdy ho použít, @P popisuje každý parametr a @AiService spojuje vše dohromady při spuštění.*

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) a zeptejte se:
> - "Jak bych integroval reálné API počasí jako OpenWeatherMap místo simulovaných dat?"
> - "Co tvoří dobrý popis nástroje, který pomáhá AI ho správně používat?"
> - "Jak řešit chyby API a limity volání v implementacích nástrojů?"

### Rozhodování

Když uživatel položí otázku „Jaké je počasí v Seattlu?“, model si náhodně nevybere nástroj. Porovná uživatelův záměr se všemi popisy dostupných nástrojů, ohodnotí jejich relevantnost a vybere nejlepší shodu. Poté vygeneruje strukturované volání funkce s odpovídajícími parametry — v tomto případě nastaví `location` na `"Seattle"`.

Pokud žádný nástroj neodpovídá požadavku uživatele, model odpoví podle svých znalostí. Pokud několik nástrojů odpovídá, vybere ten nejkonkrétnější.

<img src="../../../translated_images/cs/decision-making.409cd562e5cecc49.webp" alt="Jak AI rozhoduje, který nástroj použít" width="800"/>

*Model vyhodnocuje každý dostupný nástroj vůči záměru uživatele a vybírá nejlepší shodu — proto je důležité psát jasné a konkrétní popisy nástrojů.*

### Provedení

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot automaticky propojí deklarativní rozhraní `@AiService` se všemi registrovanými nástroji a LangChain4j provádí volání nástrojů automaticky. V zákulisí prochází volání nástroje šesti etapami — od otázky uživatele v přirozeném jazyce až k odpovědi téže formy:

<img src="../../../translated_images/cs/tool-calling-flow.8601941b0ca041e6.webp" alt="Průběh volání nástroje" width="800"/>

*Koncepce end-to-end — uživatel položí otázku, model vybere nástroj, LangChain4j ho vykoná a model výsledek zapracuje do přirozené odpovědi.*

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) a zeptejte se:
> - "Jak funguje vzor ReAct a proč je efektivní pro AI agenty?"
> - "Jak agent rozhoduje, který nástroj použít a v jakém pořadí?"
> - "Co se stane, pokud selže vykonání nástroje - jak robustně řešit chyby?"

### Generování odpovědi

Model obdrží údaje o počasí a formátuje je do přirozené odpovědi pro uživatele.

### Architektura: Spring Boot automatické propojení

Tento modul používá integraci LangChain4j se Spring Boot přes deklarativní rozhraní `@AiService`. Při spuštění Spring Boot zjistí každý `@Component`, který obsahuje metody označené `@Tool`, váš `ChatModel` bean a `ChatMemoryProvider` — a propojí je všechny do jednoho rozhraní `Assistant` bez nutnosti boilerplate kódu.

<img src="../../../translated_images/cs/spring-boot-wiring.151321795988b04e.webp" alt="Architektura Spring Boot automatického propojení" width="800"/>

*Rozhraní @AiService spojuje ChatModel, komponenty nástrojů a poskytovatele paměti — Spring Boot se stará o automatické propojení.*

Hlavní výhody tohoto přístupu:

- **Automatické propojení ve Spring Boot** — ChatModel a nástroje jsou automaticky injektovány
- **Vzor @MemoryId** — Automatická správa paměti založená na sezení
- **Jedna instance** — Assistant je vytvořen jednou a znovu použit pro lepší výkon
- **Typově bezpečné vykonání** — Java metody volány přímo s konverzí typů
- **Orchestrace více kol konverzace** — Řeší řetězení nástrojů automaticky
- **Žádný boilerplate** — Žádné manuální volání `AiServices.builder()` ani správa HashMap paměti

Alternativní přístupy (manuální `AiServices.builder()`) vyžadují více kódu a postrádají výhody integrace ve Spring Boot.

## Řetězení nástrojů

**Řetězení nástrojů** — skutečná síla agentů založených na nástrojích se ukáže, když jedna otázka vyžaduje více nástrojů. Zeptejte se „Jaké je počasí v Seattlu ve Fahrenheitech?“ a agent automaticky řetězí dva nástroje: nejprve zavolá `getCurrentWeather`, aby získal teplotu ve stupních Celsia, poté předá tuto hodnotu do `celsiusToFahrenheit` pro konverzi — to vše v jediném kole konverzace.

<img src="../../../translated_images/cs/tool-chaining-example.538203e73d09dd82.webp" alt="Příklad řetězení nástrojů" width="800"/>

*Řetězení nástrojů v akci — agent nejprve zavolá getCurrentWeather, pak výsledek v Celsiích pošle do celsiusToFahrenheit a dodá kombinovanou odpověď.*

**Elegantní selhání** — Požádejte o počasí ve městě, které není v simulovaných datech. Nástroj vrátí chybovou zprávu a AI vysvětlí, že nemůže pomoci, místo aby spadla. Nástroje selhávají bezpečně. Níže uvedený diagram ukazuje kontrast dvou přístupů — s řádným zpracováním chyb agent chybu zachytí a odpoví nápomocně, bez něj by celá aplikace spadla:

<img src="../../../translated_images/cs/error-handling-flow.9a330ffc8ee0475c.webp" alt="Tok zpracování chyb" width="800"/>

*Když nástroj selže, agent chybu zachytí a místo pádu aplikace poskytne nápomocné vysvětlení.*

To probíhá v jednom kole konverzace. Agent autonomně orchestruje více volání nástrojů.

## Spuštění aplikace

**Ověření nasazení:**

Ujistěte se, že soubor `.env` v kořenovém adresáři obsahuje Azure přihlašovací údaje (vytvořené během Modulu 01). Spusťte to z adresáře modulu (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Mělo by ukazovat AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spuštění aplikace:**

> **Poznámka:** Pokud jste již spustili všechny aplikace pomocí `./start-all.sh` z kořenového adresáře (jak je popsáno v Modulu 01), tento modul již běží na portu 8084. Můžete přeskočit příkazy pro spuštění níže a jít přímo na http://localhost:8084.

**Možnost 1: Použití Spring Boot Dashboard (doporučeno pro uživatele VS Code)**

Vývojářský kontejner obsahuje rozšíření Spring Boot Dashboard, které nabízí vizuální rozhraní pro správu všech Spring Boot aplikací. Najdete ho v Activity Baru na levé straně VS Code (hledejte ikonu Spring Boot).

Ze Spring Boot Dashboard můžete:
- Vidět všechny dostupné Spring Boot aplikace v pracovním prostoru
- Spouštět/zastavovat aplikace jediným kliknutím
- Sledovat protokoly aplikací v reálném čase
- Monitorovat stav aplikací

Jednoduše klikněte na tlačítko play vedle „tools“ pro spuštění tohoto modulu, nebo spusťte všechny moduly najednou.

Takto vypadá Spring Boot Dashboard ve VS Code:

<img src="../../../translated_images/cs/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard ve VS Code — spouštění, zastavování a monitorování všech modulů na jednom místě*

**Možnost 2: Použití shell skriptů**

Spusťte všechny webové aplikace (moduly 01-04):

**Bash:**
```bash
cd ..  # Ze základního adresáře
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Ze kořenového adresáře
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

Oba skripty automaticky načtou proměnné prostředí ze souboru `.env` v kořeni a pokud JAR soubory neexistují, vytvoří je.

> **Poznámka:** Pokud chcete nejprve ručně sestavit všechny moduly před spuštěním:
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

Otevřete http://localhost:8084 ve vašem prohlížeči.

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

Aplikace poskytuje webové rozhraní, kde můžete komunikovat s AI agentem, který má přístup k nástrojům pro počasí a převod teplot. Takto vypadá rozhraní — obsahuje příklady pro rychlý start a chatovací panel pro odesílání požadavků:
<a href="images/tools-homepage.png"><img src="../../../translated_images/cs/tools-homepage.4b4cd8b2717f9621.webp" alt="Rozhraní nástrojů AI agenta" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Rozhraní nástrojů AI agenta – rychlé příklady a chatovací rozhraní pro interakci s nástroji*

### Vyzkoušejte jednoduché použití nástroje

Začněte jednoduchým požadavkem: „Převést 100 stupňů Fahrenheita na Celsia“. Agent rozpozná, že potřebuje nástroj pro převod teploty, zavolá jej s správnými parametry a vrátí výsledek. Všimněte si, jak přirozené to působí – nemuseli jste specifikovat, který nástroj použít nebo jak jej volat.

### Otestujte řetězení nástrojů

Nyní vyzkoušejte něco složitějšího: „Jaké je počasí v Seattlu a převeďte ho na Fahrenheita?“ Sledujte, jak agent postupuje krok za krokem. Nejprve získá počasí (které je v Celsiích), rozpozná, že ho musí převést na Fahrenheita, zavolá nástroj pro převod a oba výsledky zkombinuje do jedné odpovědi.

### Podívejte se na tok konverzace

Chatovací rozhraní uchovává historii konverzace, což vám umožňuje víceturnové interakce. Můžete vidět všechny předchozí dotazy a odpovědi, což usnadňuje sledovat konverzaci a pochopit, jak si agent vytváří kontext přes více výměn.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/cs/tools-conversation-demo.89f2ce9676080f59.webp" alt="Konverzace s více voláními nástrojů" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Vícekolová konverzace ukazující jednoduché převody, vyhledávání počasí a řetězení nástrojů*

### Experimentujte s různými požadavky

Vyzkoušejte různé kombinace:
- Vyhledávání počasí: „Jaké je počasí v Tokiu?“
- Převody teploty: „Kolik je 25 °C v Kelvinech?“
- Kombinované dotazy: „Zkontroluj počasí v Paříži a řekni mi, jestli je nad 20 °C“

Všimněte si, jak agent interpretuje přirozený jazyk a přiřazuje ho ke správným voláním nástrojů.

## Klíčové koncepty

### ReAct vzor (Uvažování a Jednání)

Agent střídavě uvažuje (rozhoduje, co dělat) a jedná (používá nástroje). Tento vzor umožňuje autonomní řešení problémů místo pouhého reagování na pokyny.

### Popisy nástrojů mají význam

Kvalita vašich popisů nástrojů přímo ovlivňuje, jak dobře je agent používá. Jasné, specifické popisy pomáhají modelu pochopit, kdy a jak každý nástroj volat.

### Správa relace

Anotace `@MemoryId` umožňuje automatickou správu paměti založenou na relaci. Každé ID relace získá vlastní instanci `ChatMemory` spravovanou beanem `ChatMemoryProvider`, takže více uživatelů může s agentem komunikovat současně, aniž by se jejich konverzace míchaly. Následující diagram ukazuje, jak jsou uživatelé s různými ID relací směrováni do izolovaných pamětí:

<img src="../../../translated_images/cs/session-management.91ad819c6c89c400.webp" alt="Správa relací s @MemoryId" width="800"/>

*Každé ID relace odpovídá izolované historii konverzace — uživatelé nikdy nevidí zprávy ostatních.*

### Zpracování chyb

Nástroje mohou selhávat — API mohou vypršet, parametry mohou být neplatné, externí služby mohou být nedostupné. Produkční agenti potřebují zpracování chyb, aby model mohl vysvětlit problémy nebo zkusit alternativy místo havárie celé aplikace. Když nástroj hodí výjimku, LangChain4j ji zachytí a předá chybovou zprávu modelu, který pak může problém přirozeným jazykem vysvětlit.

## Dostupné nástroje

Diagram níže ukazuje široký ekosystém nástrojů, které můžete vytvořit. Tento modul demonstruje nástroje pro počasí a teplotu, ale stejný vzor `@Tool` funguje pro jakoukoli metodu v Javě — od dotazů do databází až po zpracování plateb.

<img src="../../../translated_images/cs/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ekosystém nástrojů" width="800"/>

*Jakákoli Java metoda anotovaná `@Tool` je dostupná pro AI — vzor se rozšiřuje na databáze, API, e-maily, operace se soubory a další.*

## Kdy používat agenty založené na nástrojích

Ne každý požadavek nástroje vyžaduje. Rozhodnutí je, zda AI potřebuje komunikovat s externími systémy, nebo zda může odpovědět ze své vlastní znalosti. Následující průvodce shrnuje, kdy nástroje přinášejí přidanou hodnotu a kdy nejsou potřeba:

<img src="../../../translated_images/cs/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Kdy používat nástroje" width="800"/>

*Rychlý průvodce rozhodnutím — nástroje jsou pro aktuální data, výpočty a akce; obecné znalosti a tvůrčí úkoly je nepotřebují.*

## Nástroje vs RAG

Moduly 03 a 04 oba rozšiřují možnosti AI, ale zásadně odlišnými způsoby. RAG poskytuje modelu přístup k **znalostem** pomocí vyhledávání dokumentů. Nástroje dávají modelu schopnost provádět **akce** voláním funkcí. Níže uvedený diagram porovnává tyto přístupy vedle sebe — od toho, jak každý workflow funguje, až po kompromisy mezi nimi:

<img src="../../../translated_images/cs/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Porovnání nástroje vs RAG" width="800"/>

*RAG získává informace ze statických dokumentů — nástroje vykonávají akce a získávají dynamická, aktuální data. Mnoho produkčních systémů kombinuje obojí.*

V praxi mnoho produkčních systémů kombinuje oba přístupy: RAG pro zakotvení odpovědí ve vaší dokumentaci a nástroje pro získávání živých dat nebo provádění operací.

## Další kroky

**Další modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigace:** [← Předchozí: Modul 03 - RAG](../03-rag/README.md) | [Zpět na hlavní](../README.md) | [Další: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o přesnost, mějte prosím na paměti, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Originální dokument v jeho rodném jazyce by měl být považován za závazný zdroj. Pro kritické informace se doporučuje profesionální lidský překlad. Nejsme odpovědní za jakákoli nedorozumění nebo mylné výklady vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->