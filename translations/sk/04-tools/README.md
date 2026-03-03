# Modul 04: AI Agenti s Nástrojmi

## Obsah

- [Čo sa naučíte](../../../04-tools)
- [Predpoklady](../../../04-tools)
- [Pochopenie AI agentov s nástrojmi](../../../04-tools)
- [Ako funguje volanie nástrojov](../../../04-tools)
  - [Definície nástrojov](../../../04-tools)
  - [Rozhodovanie](../../../04-tools)
  - [Vykonanie](../../../04-tools)
  - [Generovanie odpovede](../../../04-tools)
  - [Architektúra: Spring Boot automatické prepájanie](../../../04-tools)
- [Reťazenie nástrojov](../../../04-tools)
- [Spustenie aplikácie](../../../04-tools)
- [Používanie aplikácie](../../../04-tools)
  - [Vyskúšajte jednoduché použitie nástrojov](../../../04-tools)
  - [Otestujte reťazenie nástrojov](../../../04-tools)
  - [Pozrite tok konverzácie](../../../04-tools)
  - [Experimentujte s rôznymi požiadavkami](../../../04-tools)
- [Kľúčové pojmy](../../../04-tools)
  - [Vzorec ReAct (Uvažovanie a konanie)](../../../04-tools)
  - [Popisy nástrojov majú význam](../../../04-tools)
  - [Správa relácií](../../../04-tools)
  - [Spracovanie chýb](../../../04-tools)
- [Dostupné nástroje](../../../04-tools)
- [Kedy použiť agentov založených na nástrojoch](../../../04-tools)
- [Nástroje vs RAG](../../../04-tools)
- [Ďalšie kroky](../../../04-tools)

## Čo sa naučíte

Doteraz ste sa naučili viesť rozhovory s AI, efektívne štruktúrovať výzvy a zakladať odpovede na vašich dokumentoch. Ale stále tu je základné obmedzenie: jazykové modely môžu len generovať text. Nemôžu skontrolovať počasie, vykonávať výpočty, dotazovať databázy ani komunikovať s externými systémami.

Nástroje to menia. Tým, že modelu dáte prístup k funkciám, ktoré môže volať, sa z neho stáva agent, ktorý dokáže konať. Model rozhoduje, kedy potrebuje nástroj, ktorý nástroj použiť a aké parametre odovzdať. Váš kód vykoná funkciu a vráti výsledok. Model tento výsledok zapracuje do svojej odpovede.

## Predpoklady

- Dokončený [Modul 01 - Úvod](../01-introduction/README.md) (nasadené Azure OpenAI zdroje)
- Odporúčané dokončenie predchádzajúcich modulov (tento modul odkazuje na [RAG koncepty z Modulu 03](../03-rag/README.md) v porovnaní Nástrojov vs RAG)
- Súbor `.env` v koreňovom adresári s Azure prihlasovacími údajmi (vytvorený pomocou `azd up` v Module 01)

> **Poznámka:** Ak ste Modulu 01 ešte neabsolvovali, najskôr postupujte podľa tam uvedených inštrukcií na nasadenie.

## Pochopenie AI agentov s nástrojmi

> **📝 Poznámka:** Termín „agenti“ v tomto module označuje AI asistentov rozšírených o schopnosť volať nástroje. Toto sa líši od **Agentic AI** vzorcov (autonómni agenti s plánovaním, pamäťou a viacstupňovým uvažovaním), ktoré pokryjeme v [Modul 05: MCP](../05-mcp/README.md).

Bez nástrojov môže jazykový model len generovať text na základe svojich tréningových dát. Opýtajte sa ho na aktuálne počasie a musí hádať. Ak mu však dáte nástroje, vie zavolať API počasia, vykonať výpočty alebo dotazovať databázu — a potom tieto reálne výsledky zapracovať do svojej odpovede.

<img src="../../../translated_images/sk/what-are-tools.724e468fc4de64da.webp" alt="Bez nástrojov vs S nástrojmi" width="800"/>

*Bez nástrojov model len háda — s nástrojmi môže volať API, vykonávať výpočty a vracať údaje v reálnom čase.*

AI agent s nástrojmi nasleduje vzorec **Uvažovanie a konanie (ReAct)**. Model nielen odpovedá — premýšľa o tom, čo potrebuje, koná zavolaním nástroja, sleduje výsledok a potom rozhoduje, či má konať znova alebo doručiť finálnu odpoveď:

1. **Uvažuj** — Agent analyzuje otázku používateľa a určí potrebné informácie
2. **Konaj** — Agent vyberie správny nástroj, vytvorí správne parametre a zavolá ho
3. **Sleduj** — Agent získa výstup nástroja a vyhodnotí výsledok
4. **Opakuj alebo odpovedz** — Ak je potrebných viac údajov, agent sa vracia; inak zloží odpoveď v prirodzenom jazyku

<img src="../../../translated_images/sk/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Vzorec ReAct" width="800"/>

*Cyklus ReAct — agent uvažuje, čo robiť, koná volaním nástroja, sleduje výsledok a opakuje, až kým nevytvorí finálnu odpoveď.*

Toto sa deje automaticky. Definujete nástroje a ich popisy. Model sa stará o rozhodovanie, kedy a ako ich použiť.

## Ako funguje volanie nástrojov

### Definície nástrojov

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Definujete funkcie s jasnými popismi a špecifikáciami parametrov. Model vidí tieto popisy vo svojom systémovom promptu a rozumie, čo každý nástroj robí.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Vaša logika vyhľadávania počasia
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Asistent je automaticky prepojený Spring Bootom s:
// - Beanom ChatModel
// - Všetkými metódami @Tool z @Component tried
// - ChatMemoryProvider pre správu relácie
```

Nižšie uvedený diagram rozoberá každú anotáciu a ukazuje, ako každý prvok pomáha AI pochopiť, kedy volať nástroj a aké argumenty odovzdať:

<img src="../../../translated_images/sk/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatómia definícií nástrojov" width="800"/>

*Anatómia definície nástroja — @Tool hovorí AI, kedy ho použiť, @P popisuje každý parameter a @AiService všetko naštartuje pri spustení.*

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) a spýtajte sa:
> - „Ako by som integroval skutočné API počasia ako OpenWeatherMap namiesto simulovaných dát?“
> - „Čo tvorí dobrý popis nástroja, ktorý pomáha AI používať ho správne?“
> - „Ako riešiť chyby API a limity volaní v implementáciách nástrojov?“

### Rozhodovanie

Keď sa používateľ opýta „Aké je počasie v Seattli?“, model náhodne nevyberá nástroj. Porovnáva zámery používateľa so všetkými dostupnými popismi nástrojov, vyhodnotí relevantnosť a vyberie najvhodnejší nástroj. Potom vygeneruje štruktúrované volanie funkcie s vhodnými parametrami — v tomto prípade nastaví `location` na `"Seattle"`.

Ak žiadny nástroj nezodpovedá požiadavke používateľa, model odpovie zo svojej vlastnej znalostnej bázy. Ak viacero nástrojov vyhovuje, vyberie ten najšpecifickejší.

<img src="../../../translated_images/sk/decision-making.409cd562e5cecc49.webp" alt="Ako AI rozhoduje, ktorý nástroj použiť" width="800"/>

*Model hodnotí každý dostupný nástroj voči zámeru používateľa a vyberá najlepšiu zhodu — preto je dôležité písať jasné, špecifické popisy nástrojov.*

### Vykonanie

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot automaticky prepája deklaratívne rozhranie `@AiService` so všetkými zaregistrovanými nástrojmi a LangChain4j vykonáva volania nástrojov automaticky. V zákulisí prechádza volanie nástroja šiestimi fázami — od otázky používateľa v prirodzenom jazyku až po odpoveď v prirodzenom jazyku:

<img src="../../../translated_images/sk/tool-calling-flow.8601941b0ca041e6.webp" alt="Tok volania nástroja" width="800"/>

*Celý proces — používateľ kladie otázku, model vyberá nástroj, LangChain4j ho vykoná a model zapracuje výsledok do odpovede.*

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) a spýtajte sa:
> - „Ako funguje vzorec ReAct a prečo je efektívny pre AI agentov?“
> - „Ako agent rozhoduje, ktorý nástroj použiť a v akom poradí?“
> - „Čo sa stane, keď vykonanie nástroja zlyhá — ako robustne riešiť chyby?“

### Generovanie odpovede

Model obdrží údaje o počasí a naformátuje ich do prirodzenej odpovede pre používateľa.

### Architektúra: Spring Boot automatické prepájanie

Tento modul využíva integráciu LangChain4j so Spring Boot cez deklaratívne rozhrania `@AiService`. Pri spustení Spring Boot nájde každý `@Component`, ktorý obsahuje metódy označené `@Tool`, váš bean `ChatModel` a `ChatMemoryProvider` — a všetko spojí do jediného rozhrania `Assistant` bez potreby boilerplate kódu.

<img src="../../../translated_images/sk/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot automatické prepájanie architektúra" width="800"/>

*Rozhranie @AiService spája ChatModel, nástrojové komponenty a poskytovateľa pamäte — Spring Boot sa postará o celé prepájanie automaticky.*

Kľúčové výhody tohto prístupu:

- **Spring Boot automatické prepájanie** — ChatModel a nástroje sa vkladajú automaticky
- **Vzorec @MemoryId** — Automatická správa pamäte založenej na relácii
- **Jediná inštancia** — Assistant sa vytvorí raz a opakovane používa pre lepší výkon
- **Typovo bezpečné vykonávanie** — Java metódy volané priamo s konverziou typov
- **Správa viacerých krokov** — Automaticky zvláda reťazenie nástrojov
- **Žiadny boilerplate** — Žiadne manuálne volania `AiServices.builder()` alebo HashMap pamäť

Alternatívne prístupy (manuálne `AiServices.builder()`) vyžadujú viac kódu a neprinášajú výhody Spring Boot integrácie.

## Reťazenie nástrojov

**Reťazenie nástrojov** — Skutočná sila agentov na báze nástrojov sa prejaví, keď jedna otázka vyžaduje viacero nástrojov. Opýtajte sa „Aké je počasie v Seattli v Fahrenheitoch?“ a agent automaticky spojí dva nástroje: najprv zavolá `getCurrentWeather` pre teplotu v Celziách, potom tento výsledok odovzdá do `celsiusToFahrenheit` na prevod — to všetko v jednom kole konverzácie.

<img src="../../../translated_images/sk/tool-chaining-example.538203e73d09dd82.webp" alt="Príklad reťazenia nástrojov" width="800"/>

*Reťazenie nástrojov v praxi — agent najprv zavolá getCurrentWeather, potom výsledok v Celziách pošle do celsiusToFahrenheit a poskytne kombinovanú odpoveď.*

**Elegantné zlyhania** — Opýtajte sa na počasie v meste, ktoré nie je v simulovaných dátach. Nástroj vráti chybové hlásenie a AI vysvetlí, že nemôže pomôcť, namiesto toho, aby spadla. Nástroje zlyhávajú bezpečne. Diagram nižšie konfrontuje oba prístupy — s riadnym spracovaním chýb agent zachytí výnimku a odpovie užitočne, zatiaľ čo bez neho celá aplikácia spadne:

<img src="../../../translated_images/sk/error-handling-flow.9a330ffc8ee0475c.webp" alt="Tok spracovania chýb" width="800"/>

*Keď nástroj zlyhá, agent zachytí chybu a odpovie užitočným vysvetlením namiesto pádu.*

Toto sa deje v jednom kole konverzácie. Agent autonómne orchestruje viacero volaní nástrojov.

## Spustenie aplikácie

**Overenie nasadenia:**

Uistite sa, že súbor `.env` existuje v koreňovom adresári s Azure prihlasovacími údajmi (vytvorený počas Modulu 01). Spustite to z adresára modulu (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Malo by ukázať AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Malo by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spustenie aplikácie:**

> **Poznámka:** Ak ste už spustili všetky aplikácie pomocou `./start-all.sh` z koreňa (ako popísané v Module 01), tento modul už beží na porte 8084. Môžete preskočiť príkazy na spustenie a ísť priamo na http://localhost:8084.

**Možnosť 1: Použitie Spring Boot Dashboard (Odporúčané pre používateľov VS Code)**

Dev kontajner obsahuje rozšírenie Spring Boot Dashboard, ktoré poskytuje grafické rozhranie na správu všetkých Spring Boot aplikácií. Nájdete ho v Activity Bar na ľavej strane VS Code (ikona Spring Boot).

Zo Spring Boot Dashboard môžete:
- Vidieť všetky dostupné Spring Boot aplikácie v pracovnom priestore
- Jedným kliknutím spúšťať/zastavovať aplikácie
- Prezerať logy aplikácie v reálnom čase
- Monitorovať stav aplikácie

Jednoducho kliknite na tlačidlo play vedľa "tools" pre spustenie tohto modulu, alebo spustite všetky moduly naraz.

Takto vyzerá Spring Boot Dashboard vo VS Code:

<img src="../../../translated_images/sk/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard vo VS Code — spustite, zastavte a sledujte všetky moduly na jednom mieste*

**Možnosť 2: Použitie shell skriptov**

Spustite všetky webové aplikácie (moduly 01-04):

**Bash:**
```bash
cd ..  # Z koreňového adresára
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Z koreňového adresára
.\start-all.ps1
```

Alebo spustite len tento modul:

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

Oba skripty automaticky načítajú premenné prostredia zo súboru `.env` v koreňovom adresári a zostavia JARy, ak ešte neexistujú.

> **Poznámka:** Ak radšej zostavíte všetky moduly manuálne pred spustením:
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

Otvorte v prehliadači http://localhost:8084.

**Na zastavenie:**

**Bash:**
```bash
./stop.sh  # Len tento modul
# Alebo
cd .. && ./stop-all.sh  # Všetky moduly
```

**PowerShell:**
```powershell
.\stop.ps1  # Len tento modul
# Alebo
cd ..; .\stop-all.ps1  # Všetky moduly
```

## Používanie aplikácie

Aplikácia poskytuje webové rozhranie, kde môžete komunikovať s AI agentom, ktorý má prístup k nástrojom na počasie a prevod teploty. Takto vyzerá rozhranie — obsahuje príklady na rýchly štart a chat panel na odosielanie požiadaviek:
<a href="images/tools-homepage.png"><img src="../../../translated_images/sk/tools-homepage.4b4cd8b2717f9621.webp" alt="Rozhranie nástrojov AI agenta" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Rozhranie nástrojov AI agenta – rýchle príklady a chatové rozhranie na interakciu s nástrojmi*

### Vyskúšajte jednoduché použitie nástroja

Začnite jednoduchou požiadavkou: „Preveď 100 stupňov Fahrenheit na Celzius“. Agent rozpozná, že potrebuje nástroj na prevod teploty, zavolá ho s správnymi parametrami a vráti výsledok. Všimnite si, aké to pôsobí prirodzene – neurčovali ste, ktorý nástroj použiť ani ako ho volať.

### Otestujte reťazenie nástrojov

Teraz skúste niečo zložitejšie: „Aké je počasie v Seattli a preveď to na Fahrenheit?“ Sledujte, ako agent postupuje krok za krokom. Najprv získa počasie (ktoré vracia v Celziách), rozpozná, že potrebuje previesť na Fahrenheit, zavolá nástroj na prevod a oba výsledky skombinuje do jednej odpovede.

### Pozrite si tok konverzácie

Chatové rozhranie uchováva históriu konverzácie, čo vám umožňuje mať viackolové interakcie. Môžete vidieť všetky predchádzajúce otázky a odpovede, čo uľahčuje sledovanie konverzácie a pochopenie, ako agent buduje kontext cez viaceré výmeny.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/sk/tools-conversation-demo.89f2ce9676080f59.webp" alt="Konverzácia s viacerými volaniami nástrojov" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Viackolová konverzácia ukazujúca jednoduché konverzie, vyhľadávanie počasia a reťazenie nástrojov*

### Experimentujte s rôznymi požiadavkami

Skúste rôzne kombinácie:
- Vyhľadávanie počasia: „Aké je počasie v Tokiu?“
- Prevod teplôt: „Koľko je 25 °C v Kelvinoch?“
- Kombinované otázky: „Skontroluj počasie v Paríži a povedz mi, či je nad 20 °C“

Všimnite si, ako agent interpretuje prirodzený jazyk a mapuje ho na vhodné volania nástrojov.

## Kľúčové koncepty

### Vzor ReAct (Uvažovanie a konanie)

Agent strieda fázy uvažovania (rozhodovanie, čo robiť) a konania (používanie nástrojov). Tento vzor umožňuje autonómne riešenie problémov namiesto len reagovania na pokyny.

### Dôležitosť popisov nástrojov

Kvalita popisov vašich nástrojov priamo ovplyvňuje, ako dobre ich agent používa. Jasné a konkrétne popisy pomáhajú modelu pochopiť, kedy a ako volať jednotlivé nástroje.

### Správa relácií

Anotácia `@MemoryId` umožňuje automatickú správu pamäte založenú na reláciách. Každej relácii sa priradí vlastná inštancia `ChatMemory` spravovaná beanom `ChatMemoryProvider`, takže viacerí používatelia môžu súčasne interagovať s agentom bez miešania konverzácií. Nasledujúci diagram ukazuje, ako sú používatelia smerovaní do izolovaných úložísk pamäte podľa ich ID relácie:

<img src="../../../translated_images/sk/session-management.91ad819c6c89c400.webp" alt="Správa relácií s @MemoryId" width="800"/>

*Každé ID relácie mapuje na izolovanú históriu konverzácie — používatelia nikdy nevidia správy druhých.*

### Riešenie chýb

Nástroje môžu zlyhať — API môže vypršať, parametre byť neplatné, externé služby môžu prestať fungovať. Produkčné agenti potrebujú spracovanie chýb, aby model mohol vysvetliť problémy alebo skúsiť alternatívy namiesto toho, aby celá aplikácia spadla. Keď nástroj vyhodí výnimku, LangChain4j ju zachytí a chybovú správu pošle späť modelu, ktorý môže problém prirodzene opísať.

## Dostupné nástroje

Nižšie uvedený diagram zobrazuje široké ekosystémy nástrojov, ktoré môžete vybudovať. Tento modul demonštruje nástroje na počasie a teplotu, ale ten istý vzor `@Tool` funguje pre akúkoľvek Java metódu – od dotazov do databázy až po spracovanie platieb.

<img src="../../../translated_images/sk/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ekosystém nástrojov" width="800"/>

*Akákoľvek Java metóda anotovaná `@Tool` sa stáva dostupnou pre AI – vzor sa rozširuje na databázy, API, e-maily, operácie so súbormi a ďalšie.*

## Kedy používať agentov s nástrojmi

Nie každý dopyt potrebuje nástroje. Rozhodnutie závisí od toho, či AI musí komunikovať s externými systémami alebo dokáže odpovedať zo svojich vlastných znalostí. Nasledujúci sprievodca zhrňuje, kedy nástroje pridávajú hodnotu a kedy sú zbytočné:

<img src="../../../translated_images/sk/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Kedy používať nástroje" width="800"/>

*Rýchly rozhodovací sprievodca — nástroje sú vhodné na údaje v reálnom čase, výpočty a akcie; všeobecné znalosti a kreatívne úlohy nástroje nepotrebujú.*

## Nástroje vs RAG

Moduly 03 a 04 oba rozširujú, čo môže AI robiť, ale zásadne odlišnými spôsobmi. RAG poskytuje modelu prístup k **znalostiam** získavaním dokumentov. Nástroje dávajú modelu schopnosť podnikať **akcie** volaním funkcií. Nižšie uvedený diagram porovnáva tieto dva prístupy vedľa seba – od toho, ako každý pracovný tok funguje, až po kompromisy medzi nimi:

<img src="../../../translated_images/sk/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Porovnanie nástrojov a RAG" width="800"/>

*RAG získava informácie zo statických dokumentov — nástroje vykonávajú akcie a získavajú dynamické, aktuálne údaje. Mnohé produkčné systémy kombinujú oba prístupy.*

V praxi mnoho produkčných systémov kombinuje oba prístupy: RAG na zakotvenie odpovedí vo vašej dokumentácii a nástroje na získavanie živých dát alebo vykonávanie operácií.

## Ďalšie kroky

**Ďalší modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigácia:** [← Predchádzajúci: Modul 03 - RAG](../03-rag/README.md) | [Späť na hlavnú stránku](../README.md) | [Ďalší: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vyhlásenie o zodpovednosti**:
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Hoci sa snažíme o presnosť, uvedomte si, že automatizované preklady môžu obsahovať chyby alebo nepresnosti. Originálny dokument v jeho pôvodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre dôležité informácie sa odporúča profesionálny preklad vykonaný človekom. Nie sme zodpovední za akékoľvek nedorozumenia alebo nesprávne interpretácie vzniknuté použitím tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->