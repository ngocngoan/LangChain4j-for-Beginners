# Modul 04: AI agenti s nástroji

## Obsah

- [Co se naučíte](../../../04-tools)
- [Předpoklady](../../../04-tools)
- [Porozumění AI agentům s nástroji](../../../04-tools)
- [Jak funguje volání nástrojů](../../../04-tools)
  - [Definice nástrojů](../../../04-tools)
  - [Rozhodování](../../../04-tools)
  - [Vykonání](../../../04-tools)
  - [Generování odpovědi](../../../04-tools)
  - [Architektura: Spring Boot Auto-Wiring](../../../04-tools)
- [Řetězení nástrojů](../../../04-tools)
- [Spuštění aplikace](../../../04-tools)
- [Použití aplikace](../../../04-tools)
  - [Vyzkoušejte jednoduché použití nástroje](../../../04-tools)
  - [Otestujte řetězení nástrojů](../../../04-tools)
  - [Podívejte se na tok konverzace](../../../04-tools)
  - [Experimentujte s různými požadavky](../../../04-tools)
- [Klíčové koncepty](../../../04-tools)
  - [Vzor ReAct (Reasoning and Acting)](../../../04-tools)
  - [Popisy nástrojů jsou důležité](../../../04-tools)
  - [Správa relace](../../../04-tools)
  - [Zpracování chyb](../../../04-tools)
- [Dostupné nástroje](../../../04-tools)
- [Kdy používat agenty založené na nástrojích](../../../04-tools)
- [Nástroje vs RAG](../../../04-tools)
- [Další kroky](../../../04-tools)

## Co se naučíte

Zatím jste se naučili, jak vést konverzace s AI, efektivně strukturovat podněty a zakládat odpovědi na vašich dokumentech. Přesto stále existuje základní omezení: jazykové modely dokážou generovat pouze text. Nemohou zjistit počasí, provádět výpočty, dotazovat databáze nebo komunikovat s externími systémy.

Nástroje to mění. Tím, že modelu poskytnete přístup k funkcím, které může volat, proměníte ho z generátoru textu na agenta, který může podnikat akce. Model rozhoduje, kdy potřebuje nástroj, který nástroj použít a jaké předat parametry. Váš kód spustí funkci a vrátí výsledek. Model tento výsledek zahrne do své odpovědi.

## Předpoklady

- Dokončený [Modul 01 - Úvod](../01-introduction/README.md) (nasazené zdroje Azure OpenAI)
- Doporučené dokončení předchozích modulů (tento modul odkazuje na [koncepty RAG z Modulu 03](../03-rag/README.md) v porovnání Nástrojů vs RAG)
- Soubor `.env` v kořenovém adresáři s přihlašovacími údaji Azure (vytvořený příkazem `azd up` v Modulu 01)

> **Poznámka:** Pokud jste Modul 01 nedokončili, nejprve postupujte podle tamních pokynů k nasazení.

## Porozumění AI agentům s nástroji

> **📝 Poznámka:** Termín „agenti“ v tomto modulu označuje AI asistenty s rozšířenými možnostmi volání nástrojů. To se liší od vzorů **Agentic AI** (autonomních agentů s plánováním, pamětí a vícestupňovým uvažováním), které probereme v [Modulu 05: MCP](../05-mcp/README.md).

Bez nástrojů může jazykový model generovat text pouze ze svých trénovacích dat. Zeptáte-li se na aktuální počasí, musí hádat. Pokud mu dáte nástroje, může zavolat API počasí, provést výpočty nebo dotazovat databázi — a tyto reálné výsledky pak vtvořit do své odpovědi.

<img src="../../../translated_images/cs/what-are-tools.724e468fc4de64da.webp" alt="Bez nástrojů vs s nástroji" width="800"/>

*Bez nástrojů model jen hádá — s nástroji může volat API, provádět výpočty a vracet data v reálném čase.*

AI agent s nástroji používá vzor **Reasoning and Acting (ReAct)**. Model nepouze odpovídá — přemýšlí o tom, co potřebuje, jedná voláním nástroje, sleduje výsledek a pak rozhodne, zda má opět jednat nebo odpovědět:

1. **Uvažovat** — agent analyzuje uživatelovu otázku a určí, jaké informace potřebuje
2. **Jednat** — agent vybere správný nástroj, vygeneruje správné parametry a zavolá ho
3. **Pozorovat** — agent obdrží výstup nástroje a zhodnotí výsledek
4. **Opakovat nebo odpovědět** — pokud je potřeba více dat, agent se vrací na začátek; jinak sestaví odpověď v přirozeném jazyce

<img src="../../../translated_images/cs/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Vzor ReAct" width="800"/>

*Cyklus ReAct — agent uvažuje, co dělat, jedná voláním nástroje, sleduje výsledek a opakuje, dokud nemůže dodat konečnou odpověď.*

To probíhá automaticky. Definujete nástroje a jejich popisy. Model řeší rozhodování, kdy a jak je použít.

## Jak funguje volání nástrojů

### Definice nástrojů

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Definujete funkce s jasnými popisy a specifikacemi parametrů. Model tyto popisy vidí v systémovém promptu a rozumí, co každý nástroj dělá.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Vaše logika pro vyhledávání počasí
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Asistent je automaticky propojen Spring Bootem s:
// - Beanem ChatModel
// - Všemi metodami @Tool z tříd označených @Component
// - Poskytovatelem ChatMemory pro správu relací
```

Následující diagram rozebírá každou anotaci a ukazuje, jak každý prvek pomáhá AI pochopit, kdy volat nástroj a jaké argumenty předat:

<img src="../../../translated_images/cs/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Stavba definic nástrojů" width="800"/>

*Stavba definice nástroje — @Tool říká AI, kdy ho použít, @P popisuje každý parametr a @AiService vše při spuštění propojí.*

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) a zeptejte se:
> - "Jak integrovat skutečné API počasí jako OpenWeatherMap místo simulovaných dat?"
> - "Co tvoří dobrý popis nástroje, který pomáhá AI jej správně použít?"
> - "Jak řešit chyby API a limity volání v implementaci nástrojů?"

### Rozhodování

Když uživatel zeptá "Jaké je počasí v Seattlu?", model si náhodně nevybere nástroj. Porovná uživatelův záměr s popisy všech dostupných nástrojů, vyhodnotí jejich relevantnost a vybere nejlepší shodu. Poté vygeneruje strukturované volání funkce s odpovídajícími parametry — v tomto případě nastaví `location` na `"Seattle"`.

Pokud žádný nástroj neodpovídá žádosti uživatele, model odpovídá ze svých vlastních znalostí. Pokud na dotaz odpovídá víc nástrojů, vybere ten nejkonkrétnější.

<img src="../../../translated_images/cs/decision-making.409cd562e5cecc49.webp" alt="Jak AI rozhoduje, který nástroj použít" width="800"/>

*Model posuzuje každý dostupný nástroj vůči uživatelovu záměru a vybírá nejlepší shodu — proto jsou důležité srozumitelné, specifické popisy nástrojů.*

### Vykonání

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot automaticky propojuje deklarativní rozhraní `@AiService` se všemi registrovanými nástroji a LangChain4j provádí volání nástrojů automaticky. V pozadí kompletní volání nástroje prochází šesti fázemi — od přirozené otázky uživatele až po odpověď v přirozeném jazyce:

<img src="../../../translated_images/cs/tool-calling-flow.8601941b0ca041e6.webp" alt="Tok volání nástrojů" width="800"/>

*Konec-konců — uživatel položí otázku, model vybere nástroj, LangChain4j ho vykoná a model zakomponuje výsledek do odpovědi.*

Pokud jste spustili [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) v Modulu 00, už jste tento vzor viděli — nástroje `Calculator` byly volány stejným způsobem. Následující sekvenční diagram ukazuje přesně, co se při té ukázce dělo pod pokličkou:

<img src="../../../translated_images/cs/tool-calling-sequence.94802f406ca26278.webp" alt="Sekvence volání nástrojů" width="800"/>

*Smyčka volání nástrojů z demo Quick Start — `AiServices` posílá vaši zprávu a schéma nástrojů do LLM, LLM odpovídá voláním funkce jako `add(42, 58)`, LangChain4j lokálně spustí metodu `Calculator` a vrátí výsledek pro konečnou odpověď.*

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) a zeptejte se:
> - "Jak funguje vzor ReAct a proč je efektivní pro AI agenty?"
> - "Jak agent rozhoduje, který nástroj použít a v jakém pořadí?"
> - "Co se stane, když volání nástroje selže – jak robustně řešit chyby?"

### Generování odpovědi

Model obdrží data o počasí a naformátuje je do odpovědi v přirozeném jazyce pro uživatele.

### Architektura: Spring Boot Auto-Wiring

Tento modul využívá LangChain4j integraci se Spring Bootem a deklarativní rozhraní `@AiService`. Po spuštění Spring Boot najde každý `@Component` obsahující metody s `@Tool`, váš bean `ChatModel` a `ChatMemoryProvider` — a všechny je propojí do jediného rozhraní `Assistant` bez nutnosti písemné konfigurace.

<img src="../../../translated_images/cs/spring-boot-wiring.151321795988b04e.webp" alt="Architektura Spring Boot Auto-Wiring" width="800"/>

*Rozhraní @AiService spojuje ChatModel, komponenty nástrojů a správce paměti — Spring Boot se postará o kompletní propojení.*

Zde je plný životní cyklus požadavku jako sekvenční diagram — od HTTP požadavku přes controller, service a auto-wired proxy až k vykonání nástroje a zpět:

<img src="../../../translated_images/cs/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Sekvence volání nástrojů ve Spring Bootu" width="800"/>

*Celý životní cyklus požadavku Spring Boot — HTTP požadavek prochází controllerem a service k auto-wired proxy Assistant, který automaticky orchestruje volání LLM a nástrojů.*

Klíčové výhody tohoto přístupu:

- **Spring Boot auto-wiring** — ChatModel a nástroje jsou automaticky injektovány
- **Vzor @MemoryId** — Automatická správa paměti dle relace
- **Jediná instance** — Assistant se vytvoří jednou a znovu používá pro lepší výkon
- **Typově bezpečné vykonání** — Java metody volány přímo s konverzí typů
- **Orchestrace vícenásobných kol** — Automaticky řeší řetězení nástrojů
- **Nulový boilerplate** — Žádné manuální volání `AiServices.builder()` ani správa HashMap paměti

Alternativní přístup (manuální `AiServices.builder()`) vyžaduje více kódu a postrádá výhody integrace Spring Boot.

## Řetězení nástrojů

**Řetězení nástrojů** — skutečná síla agentů založených na nástrojích se ukáže, když jedna otázka vyžaduje vícero nástrojů. Položíte otázku: „Jaké je počasí v Seattlu ve Fahrenheitech?“ a agent automaticky vytvoří řetězec ze dvou nástrojů: nejprve zavolá `getCurrentWeather`, aby získal teplotu ve stupních Celsia, pak tento výsledek předá do `celsiusToFahrenheit` pro převod — vše v jediném kole konverzace.

<img src="../../../translated_images/cs/tool-chaining-example.538203e73d09dd82.webp" alt="Příklad řetězení nástrojů" width="800"/>

*Řetězení nástrojů v akci — agent nejprve volá getCurrentWeather, pak výsledek Celsia předá do celsiusToFahrenheit a přinese výslednou odpověď.*

**Elegantní selhání** — požádejte o počasí ve městě, které není v simulovaných datech. Nástroj vrátí chybovou zprávu a AI vysvětlí, že nemůže pomoci, místo aby spadla. Nástroje selhávají bezpečně. Následující diagram porovnává oba přístupy — při správném zpracování chyby agent chybu zachytí a zdvořile odpoví, bez toho celý program spadne:

<img src="../../../translated_images/cs/error-handling-flow.9a330ffc8ee0475c.webp" alt="Tok zpracování chyb" width="800"/>

*Když nástroj selže, agent chybu zachytí a odpoví s vysvětlením místo pádu aplikace.*

To proběhne v jednom kole konverzace. Agent autonomně orchestruje vícenásobná volání nástrojů.

## Spuštění aplikace

**Ověření nasazení:**

Ujistěte se, že soubor `.env` existuje v kořenovém adresáři a obsahuje přihlašovací údaje Azure (vytvořeno v průběhu Modulu 01). Z příkazové řádky modulu (`04-tools/`) spusťte:

**Bash:**
```bash
cat ../.env  # Mělo by zobrazovat AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spusťte aplikaci:**

> **Poznámka:** Pokud jste již spustili všechny aplikace pomocí `./start-all.sh` z kořenového adresáře (jak je popsáno v Modulu 01), tento modul už běží na portu 8084. Můžete přeskočit níže uvedené příkazy a rovnou otevřít http://localhost:8084.

**Možnost 1: Použití Spring Boot Dashboard (doporučeno pro uživatele VS Code)**

Vývojářský kontejner obsahuje rozšíření Spring Boot Dashboard, které poskytuje vizuální rozhraní pro správu všech Spring Boot aplikací. Najdete ho v liště aktivit na levé straně VS Code (ikona Spring Boot).

Ze Spring Boot Dashboard můžete:
- Vidět všechny dostupné Spring Boot aplikace v pracovním prostoru
- Startovat/zastavovat aplikace jedním kliknutím
- Zobrazovat logy aplikací v reálném čase
- Monitorovat stav aplikací

Stačí kliknout na tlačítko přehrávání vedle „tools“ pro spuštění tohoto modulu, nebo spustit všechny moduly najednou.

Takto vypadá Spring Boot Dashboard ve VS Code:

<img src="../../../translated_images/cs/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard ve VS Code — spouštějte, zastavujte a sledujte všechny moduly na jednom místě*

**Možnost 2: Použití shell skriptů**

Spusťte všechny webové aplikace (moduly 01-04):

**Bash:**
```bash
cd ..  # Ze kořenového adresáře
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Ze složky root
.\start-all.ps1
```

Nebo spusťte pouze tento modul:

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

Oba skripty automaticky načítají proměnné prostředí ze souboru `.env` v kořenovém adresáři a vytvoří JAR soubory, pokud neexistují.

> **Poznámka:** Pokud raději chcete všechny moduly sestavit ručně před spuštěním:
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

Otevřete v prohlížeči adresu http://localhost:8084.

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

## Použití aplikace

Aplikace poskytuje webové rozhraní, kde můžete komunikovat s AI agentem, který má přístup k nástrojům pro počasí a převod teplot. Takto vypadá rozhraní — obsahuje příklady pro rychlý start a panel chatu pro odesílání požadavků:

<a href="images/tools-homepage.png"><img src="../../../translated_images/cs/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Rozhraní AI Agent Tools - rychlé příklady a chatovací rozhraní pro interakci s nástroji*

### Vyzkoušejte jednoduché použití nástrojů

Začněte jednoduchým požadavkem: „Převeď 100 stupňů Fahrenheita na Celsia“. Agent rozpozná, že potřebuje nástroj pro převod teplot, zavolá jej se správnými parametry a vrátí výsledek. Všimněte si, jak přirozené to působí — neuváděli jste, který nástroj použít nebo jak jej zavolat.

### Otestujte řetězení nástrojů

Nyní zkuste něco složitějšího: „Jaké je počasí v Seattlu a převeď ho na Fahrenheity?“ Sledujte, jak agent postupně řeší tento dotaz. Nejprve zjistí počasí (vrátí Celsia), rozpozná potřebu převodu na Fahrenheity, zavolá převodní nástroj a oba výsledky spojí do jedné odpovědi.

### Sledujte tok rozhovoru

Chatovací rozhraní uchovává historii konverzace, což umožňuje vícenásobné výměny zpráv. Vidíte všechny předchozí dotazy a odpovědi, což usnadňuje sledovat konverzaci a chápat, jak agent buduje kontext během mnoha výměn.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/cs/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Vícekolová konverzace ukazující jednoduché převody, vyhledávání počasí a řetězení nástrojů*

### Experimentujte s různými požadavky

Vyzkoušejte různé kombinace:
- Vyhledávání počasí: „Jaké je počasí v Tokiu?“
- Převody teplot: „Kolik je 25 °C ve Kelvinech?“
- Kombinované dotazy: „Zkontroluj počasí v Paříži a řekni mi, jestli je nad 20 °C“

Všimněte si, jak agent interpretuje přirozený jazyk a mapuje ho na vhodná volání nástrojů.

## Klíčové koncepty

### Vzor ReAct (Reasoning and Acting)

Agent střídá fáze uvažování (rozhodování, co dělat) a jednání (používání nástrojů). Tento vzor umožňuje autonomní řešení problémů místo pouhého reagování na příkazy.

### Popisy nástrojů jsou důležité

Kvalita popisů vašich nástrojů přímo ovlivňuje, jak je agent používá. Jasné a konkrétní popisy pomáhají modelu pochopit, kdy a jak každý nástroj zavolat.

### Správa relací

Anotace `@MemoryId` umožňuje automatickou správu paměti na základě relace. Každé ID relace získá vlastní instanci `ChatMemory`, kterou spravuje bean `ChatMemoryProvider`, takže více uživatelů může zároveň interagovat s agentem, aniž by se jejich konverzace propletly. Následující diagram ukazuje, jak jsou uživatelé směrováni do izolovaných úložišť paměti podle svých ID relace:

<img src="../../../translated_images/cs/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Každé ID relace odpovídá izolované historii konverzace — uživatelé nikdy nevidí zprávy druhých.*

### Zpracování chyb

Nástroje mohou selhat — API může vypršet časový limit, parametry mohou být neplatné, externí služby mohou být nedostupné. Produkční agenti vyžadují zpracování chyb, aby model mohl vysvětlit problémy nebo zkusit náhradní postupy místo zhroucení celé aplikace. Pokud nástroj vyhodí výjimku, LangChain4j ji zachytí a pošle chybovou zprávu zpět modelu, který může problém vysvětlit přirozeným jazykem.

## Dostupné nástroje

Diagram níže ukazuje širokou škálu nástrojů, které můžete vytvořit. Tento modul demonstruje nástroje pro počasí a teploty, ale stejný vzor `@Tool` funguje pro jakoukoli metodu v Javě — od databázových dotazů po zpracování plateb.

<img src="../../../translated_images/cs/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Každá metoda v Javě anotovaná `@Tool` je dostupná pro AI — vzor zahrnuje databáze, API, e-maily, operace se soubory a další.*

## Kdy používat agenty založené na nástrojích

Ne každý požadavek vyžaduje nástroje. Rozhodnutí závisí na tom, zda AI potřebuje komunikovat s externími systémy, nebo může odpovědět ze své vlastní znalosti. Následující průvodce shrnuje, kdy nástroje přinášejí přidanou hodnotu, a kdy nejsou potřeba:

<img src="../../../translated_images/cs/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*Krátký průvodce rozhodováním — nástroje jsou pro aktuální data, výpočty a akce; obecné znalosti a kreativní úkoly je nepotřebují.*

## Nástroje vs RAG

Moduly 03 a 04 oba rozšiřují schopnosti AI, ale zcela odlišným způsobem. RAG dává modelu přístup ke **znalostem** získáváním dokumentů. Nástroje dávají modelu schopnost provádět **akce** voláním funkcí. Diagram níže porovnává oba přístupy vedle sebe — od jejich pracovních toků až po kompromisy mezi nimi:

<img src="../../../translated_images/cs/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG vyhledává informace ve statických dokumentech — Nástroje provádějí akce a získávají dynamická data v reálném čase. Mnoho produkčních systémů kombinuje obojí.*

V praxi mnoho produkčních systémů kombinuje oba přístupy: RAG pro opření odpovědí o vaše dokumenty, a Nástroje pro získávání živých dat či vykonávání operací.

## Další kroky

**Další modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigace:** [← Předchozí: Modul 03 - RAG](../03-rag/README.md) | [Zpět na hlavní stránku](../README.md) | [Další: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o přesnost, mějte na paměti, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho rodném jazyce by měl být považován za závazný zdroj. Pro důležité informace se doporučuje profesionální lidský překlad. Neneseme odpovědnost za jakékoliv nedorozumění nebo chybné interpretace vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->