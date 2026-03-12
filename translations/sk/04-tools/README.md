# Modul 04: AI agenti s nástrojmi

## Obsah

- [Prehľad videa](../../../04-tools)
- [Čo sa naučíte](../../../04-tools)
- [Predpoklady](../../../04-tools)
- [Pochopenie AI agentov s nástrojmi](../../../04-tools)
- [Ako funguje volanie nástrojov](../../../04-tools)
  - [Definície nástrojov](../../../04-tools)
  - [Rozhodovanie](../../../04-tools)
  - [Vykonanie](../../../04-tools)
  - [Generovanie odpovede](../../../04-tools)
  - [Architektúra: Spring Boot automatické prepojenie](../../../04-tools)
- [Reťazenie nástrojov](../../../04-tools)
- [Spustenie aplikácie](../../../04-tools)
- [Používanie aplikácie](../../../04-tools)
  - [Vyskúšajte jednoduché použitie nástroja](../../../04-tools)
  - [Testovanie reťazenia nástrojov](../../../04-tools)
  - [Zobraziť tok konverzácie](../../../04-tools)
  - [Experimentovanie s rôznymi požiadavkami](../../../04-tools)
- [Kľúčové koncepty](../../../04-tools)
  - [ReAct vzor (reasoning and acting)](../../../04-tools)
  - [Opis nástrojov je dôležitý](../../../04-tools)
  - [Správa relácií](../../../04-tools)
  - [Spracovanie chýb](../../../04-tools)
- [Dostupné nástroje](../../../04-tools)
- [Kedy používať agentov založených na nástrojoch](../../../04-tools)
- [Nástroje verzus RAG](../../../04-tools)
- [Ďalšie kroky](../../../04-tools)

## Prehľad videa

Pozrite si živú reláciu, ktorá vysvetľuje, ako začať s týmto modulom:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="AI agenti s nástrojmi a MCP - živá relácia" width="800"/></a>

## Čo sa naučíte

Doteraz ste sa naučili viesť rozhovory s AI, efektívne štruktúrovať promptovanie a zakladať odpovede na vašich dokumentoch. Ale stále existuje základné obmedzenie: jazykové modely dokážu generovať len text. Nevedia skontrolovať počasie, robiť výpočty, dotazovať databázy ani interagovať s externými systémami.

Nástroje to menia. Tým, že modelu dáte prístup k funkciám, ktoré môže volať, premeníte ho z generátora textu na agenta, ktorý môže konať. Model rozhoduje, kedy potrebuje nástroj, ktorý nástroj použiť a aké parametre odovzdať. Váš kód vykoná funkciu a vráti výsledok. Model potom tento výsledok zakomponuje do svojej odpovede.

## Predpoklady

- Dokončený [Modul 01 - Úvod](../01-introduction/README.md) (nasadené Azure OpenAI zdroje)
- Odporúča sa dokončiť predchádzajúce moduly (tento modul odkazuje na [koncepty RAG z Modulu 03](../03-rag/README.md) v porovnaní Nástroje verzus RAG)
- Súbor `.env` v koreňovom adresári s Azure povereniami (vytvorený pomocou `azd up` v Module 01)

> **Poznámka:** Ak Modul 01 nie je dokončený, najskôr postupujte podľa jeho inštrukcií na nasadenie.

## Pochopenie AI agentov s nástrojmi

> **📝 Poznámka:** Termín "agenti" v tomto module označuje AI asistentov rozšírených o schopnosť volať nástroje. Toto sa líši od **Agentic AI** vzorov (autonómni agenti s plánovaním, pamäťou a viacstupňovým uvažovaním), ktoré pokryjeme v [Module 05: MCP](../05-mcp/README.md).

Bez nástrojov môže jazykový model len generovať text zo svojich trénovacích dát. Ak sa ho opýtate na aktuálne počasie, musí hádať. Ak mu dáte nástroje, môže volať weather API, robiť výpočty, dotazovať databázu — a potom skutočné výsledky „vložiť“ do svojej odpovede.

<img src="../../../translated_images/sk/what-are-tools.724e468fc4de64da.webp" alt="Bez nástrojov verzus s nástrojmi" width="800"/>

*Bez nástrojov model háda — s nástrojmi môže volať API, robiť výpočty a vracať dáta v reálnom čase.*

AI agent s nástrojmi nasleduje vzor **Reasoning and Acting (ReAct)**. Model nielen odpovedá — rozmýšľa, čo potrebuje, koná volaním nástroja, pozoruje výsledok a potom sa rozhoduje, či má konať znova alebo doručiť konečnú odpoveď:

1. **Reason (rozumovať)** — Agent analyzuje otázku používateľa a určí, aké informácie potrebuje
2. **Act (konať)** — Agent vyberie správny nástroj, generuje správne parametre a zavolá ho
3. **Observe (pozorovať)** — Agent dostane výstup nástroja a vyhodnotí výsledok
4. **Repeat or Respond (opakovať alebo odpovedať)** — Ak je potrebných viac dát, agent sa vracia späť; inak zloží odpoveď v prirodzenom jazyku

<img src="../../../translated_images/sk/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct vzor" width="800"/>

*Cyklus ReAct — agent rozumuje, čo má urobiť, koná volaním nástroja, pozoruje výsledok a opakuje, kým môže doručiť konečnú odpoveď.*

Toto sa deje automaticky. Definujete nástroje a ich opisy. Model sa stará o rozhodovanie kedy a ako ich používať.

## Ako funguje volanie nástrojov

### Definície nástrojov

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Definujete funkcie s jasnými popismi a špecifikáciami parametrov. Model vidí tieto popisy v systémovom prompte a rozumie, čo každý nástroj robí.

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

// Asistent je automaticky prepojený pomocou Spring Boot s:
// - Bean ChatModel
// - Všetky metódy @Tool z tried @Component
// - ChatMemoryProvider pre správu relácií
```

Nižšie uvedený diagram rozoberá každú anotáciu a vysvetľuje, ako každý prvok pomáha AI pochopiť, kedy má nástroj volať a aké argumenty odovzdať:

<img src="../../../translated_images/sk/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatómia definícií nástrojov" width="800"/>

*Anatómia definície nástroja — @Tool hovorí AI, kedy ho použiť, @P popisuje každý parameter a @AiService všetko pri spustení prepojí.*

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) a opýtajte sa:
> - „Ako začleniť skutočné weather API ako OpenWeatherMap namiesto mock dát?“
> - „Čo robí dobrý opis nástroja, ktorý pomáha AI správne ho používať?“
> - „Ako spracovať chyby API a limitácie rýchlosti v implementáciách nástrojov?“

### Rozhodovanie

Keď používateľ položí otázku „Aké je počasie v Seattli?“, model nevyberá nástroj náhodne. Porovnáva zámery používateľa so všetkými dostupnými opismi nástrojov, hodnotí ich relevantnosť a vyberie najlepšie zodpovedajúci nástroj. Potom vygeneruje štruktúrované volanie funkcie so správnymi parametrami — v tomto prípade nastaví `location` na `"Seattle"`.

Ak žiadny nástroj nevyhovuje požiadavke používateľa, model odpovie z vlastného poznania. Ak vyhovuje viac nástrojov, vyberie ten najšpecifickejší.

<img src="../../../translated_images/sk/decision-making.409cd562e5cecc49.webp" alt="Ako AI rozhoduje, ktorý nástroj použiť" width="800"/>

*Model vyhodnocuje každý dostupný nástroj voči zámeru používateľa a vyberá najlepší zápas — preto je dôležité písať jasné a konkrétne opisy nástrojov.*

### Vykonanie

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot automaticky prepája deklaratívne `@AiService` rozhranie so všetkými zaregistrovanými nástrojmi a LangChain4j volania nástrojov vykonáva automaticky. V pozadí prechádza kompletné volanie nástroja cez šesť fáz — od otázky používateľa v prirodzenom jazyku až po odpoveď v prirodzenom jazyku:

<img src="../../../translated_images/sk/tool-calling-flow.8601941b0ca041e6.webp" alt="Priebeh volania nástroja" width="800"/>

*End-to-end tok — používateľ položí otázku, model vyberie nástroj, LangChain4j ho vykoná a model výsledok zahŕňa do prirodzenej odpovede.*

Ak ste spustili [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) v Module 00, už ste videli tento vzor v akcii — `Calculator` nástroje sa volali rovnako. Nasledujúci sekvenčný diagram ukazuje presne, čo sa stalo v pozadí počas tejto ukážky:

<img src="../../../translated_images/sk/tool-calling-sequence.94802f406ca26278.webp" alt="Sekvenčný diagram volania nástroja" width="800"/>

*Cyklus volania nástroja z Quick Start demo — `AiServices` odosiela vašu správu a schémy nástrojov do LLM, LLM odpovie volaním funkcie ako `add(42, 58)`, LangChain4j vykoná metódu `Calculator` lokálne a vráti výsledok pre konečnú odpoveď.*

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) a opýtajte sa:
> - „Ako funguje ReAct vzor a prečo je efektívny pre AI agentov?“
> - „Ako agent rozhoduje, ktorý nástroj použiť a v akom poradí?“
> - „Čo sa stane, ak zlyhá vykonanie nástroja — ako robustne spracovať chyby?“

### Generovanie odpovede

Model dostane údaje o počasí a naformátuje ich do odpovede v prirodzenom jazyku pre používateľa.

### Architektúra: Spring Boot automatické prepojenie

Tento modul používa LangChain4j integráciu so Spring Boot cez deklaratívne `@AiService` rozhrania. Pri spustení Spring Boot automaticky nájde každý `@Component` obsahujúci `@Tool` metódy, vašu `ChatModel` bean a `ChatMemoryProvider` — a všetko to prepojí do jedného `Assistant` rozhrania bez boilerplatu.

<img src="../../../translated_images/sk/spring-boot-wiring.151321795988b04e.webp" alt="Architektúra Spring Boot automatického prepojenia" width="800"/>

*Rozhranie @AiService prepája ChatModel, komponenty nástrojov a poskytovateľa pamäte — Spring Boot všetko automaticky spraví.*

Tu je celý životný cyklus požiadavky vo forme sekvenčného diagramu — od HTTP požiadavky cez kontrolér, službu a automaticky prepojený proxy až po vykonanie nástroja a späť:

<img src="../../../translated_images/sk/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Sekvenčný diagram volania nástrojov Spring Boot" width="800"/>

*Kompletný životný cyklus požiadavky Spring Boot — HTTP požiadavka prechádza kontrolérom a službou k proxy asistenta, ktorý automaticky orchestruje LLM a volania nástrojov.*

Hlavné výhody tohto prístupu:

- **Spring Boot automatické prepojenie** — ChatModel a nástroje automaticky injektované
- **@MemoryId vzor** — automatická správa pamäte na základe relácie
- **Jedna inštancia** — asistent vytvorený raz a znovu použitý pre lepší výkon
- **Typovo bezpečné vykonanie** — Java metódy volané priamo s konverziou typov
- **Vícekroková orchestrácia** — automaticky spracováva reťazenie nástrojov
- **Žiadny boilerplate** — žiadne manuálne volania `AiServices.builder()` alebo HashMap pamäte

Alternatívne prístupy (manuálne `AiServices.builder()`) vyžadujú viac kódu a nevyužívajú výhody Spring Boot integrácie.

## Reťazenie nástrojov

**Reťazenie nástrojov** — Skutočná sila agentov založených na nástrojoch sa ukáže, keď jedna otázka vyžaduje viac nástrojov. Spýtajte sa „Aké je počasie v Seattli vo Fahrenheitoch?“ a agent automaticky spojí dva nástroje: najprv zavolá `getCurrentWeather` a získa teplotu v Celziách, potom tento výsledok predá `celsiusToFahrenheit` na prepočet — to všetko v jednom kole rozhovoru.

<img src="../../../translated_images/sk/tool-chaining-example.538203e73d09dd82.webp" alt="Príklad reťazenia nástrojov" width="800"/>

*Reťazenie nástrojov v akcii — agent najprv zavolá getCurrentWeather, potom výsledok v Celziach pošle do celsiusToFahrenheit a doručí zloženú odpoveď.*

**Elegantné chybové spracovanie** — Spýtajte sa na počasie v meste, ktoré nie je v mock dátach. Nástroj vráti chybovú správu a AI vysvetlí, že nemôže pomôcť, namiesto toho, aby aplikácia spadla. Nástroje zlyhávajú bezpečne. Nižšie uvedený diagram porovnáva oba prístupy — s riadnym spracovaním chýb agent zachytí výnimku a odpovie užitočne, bez neho aplikácia úplne spadne:

<img src="../../../translated_images/sk/error-handling-flow.9a330ffc8ee0475c.webp" alt="Priebeh spracovania chýb" width="800"/>

*Keď nástroj zlyhá, agent zachytí chybu a odpovie užitočným vysvetlením namiesto pádu.*

Toto sa deje v jednom kole rozhovoru. Agent autonómne orchestruje viacero volaní nástrojov.

## Spustenie aplikácie

**Overenie nasadenia:**

Uistite sa, že súbor `.env` existuje v koreňovom adresári s Azure povereniami (vytvorený počas Modulu 01). Spustite to z adresára modulu (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Malo by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Mala by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spustenie aplikácie:**

> **Poznámka:** Ak ste už spustili všetky aplikácie pomocou `./start-all.sh` z koreňového adresára (ako je popísané v Module 01), tento modul už beží na porte 8084. Môžete vynechať spúšťacie príkazy nižšie a ísť priamo na http://localhost:8084.

**Možnosť 1: Použitie Spring Boot Dashboard (odporúčané pre používateľov VS Code)**

Vývojový kontajner obsahuje rozšírenie Spring Boot Dashboard, ktoré poskytuje vizuálne rozhranie na správu všetkých Spring Boot aplikácií. Nájdete ho v bočnom paneli (Activity Bar) na ľavej strane vo VS Code (ikona Spring Boot).

V Spring Boot Dashboard môžete:
- Vidieť všetky dostupné Spring Boot aplikácie v pracovnom priestore
- Spustiť/zastaviť aplikácie jedným kliknutím
- Zobraziť logy aplikácie v reálnom čase
- Monitorovať stav aplikácie
Jednoducho kliknite na tlačidlo pre spustenie vedľa „tools“, aby ste spustili tento modul, alebo spustite všetky moduly naraz.

Takto vyzerá Spring Boot Dashboard vo VS Code:

<img src="../../../translated_images/sk/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard vo VS Code — spúšťanie, zastavenie a monitorovanie všetkých modulov z jedného miesta*

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

Alebo spustite iba tento modul:

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

Oba skripty automaticky načítajú premenné prostredia zo súboru `.env` v koreňovom adresári a zostavia JAR súbory, ak ešte neexistujú.

> **Poznámka:** Ak si prajete zostaviť všetky moduly ručne pred spustením:
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

Otvorte http://localhost:8084 vo vašom prehliadači.

**Na zastavenie:**

**Bash:**
```bash
./stop.sh  # Iba tento modul
# Alebo
cd .. && ./stop-all.sh  # Všetky moduly
```

**PowerShell:**
```powershell
.\stop.ps1  # Iba tento modul
# Alebo
cd ..; .\stop-all.ps1  # Všetky moduly
```

## Používanie aplikácie

Aplikácia poskytuje webové rozhranie, kde môžete komunikovať s AI agentom, ktorý má prístup k nástrojom na počasie a konverziu teploty. Takto vyzerá rozhranie - obsahuje rýchle ukážky a chatovací panel na zasielanie požiadaviek:

<a href="images/tools-homepage.png"><img src="../../../translated_images/sk/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Rozhranie AI Agent Tools - rýchle príklady a chatovacie rozhranie pre interakciu s nástrojmi*

### Vyskúšajte jednoduché použitie nástroja

Začnite jednoduchou požiadavkou: „Preveď 100 stupňov Fahrenheita na Celziovu teplotu“. Agent rozpozná, že potrebuje nástroj na konverziu teploty, zavolá ho s správnymi parametrami a vráti výsledok. Všimnite si, aké prirodzené to pôsobí – nešpecifikovali ste, ktorý nástroj použiť alebo ako ho zavolať.

### Otestujte reťazenie nástrojov

Teraz skúste niečo zložitejšie: „Aké je počasie v Seattli a preveď ho na Fahrenheit?“ Sledujte, ako agent postupuje v krokoch. Najprv získa počasie (ktoré vracia hodnotu v Celziovej teplote), rozpozná, že potrebuje prepočítať na Fahrenheit, zavolá nástroj na konverziu a nakoniec skombinuje oba výsledky do jednej odpovede.

### Pozrite sa na tok konverzácie

Chatové rozhranie uchováva históriu konverzácie, čo umožňuje viackolové interakcie. Vidíte všetky predchádzajúce otázky a odpovede, čo uľahčuje sledovanie konverzácie a pochopenie, ako agent počas viacerých výmen buduje kontext.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/sk/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Viackolová konverzácia ukazujúca jednoduché konverzie, vyhľadávanie počasia a reťazenie nástrojov*

### Experimentujte s rôznymi požiadavkami

Vyskúšajte rôzne kombinácie:
- Vyhľadávanie počasia: „Aké je počasie v Tokiu?“
- Konverzia teplôt: „Koľko je 25°C v Kelvinoch?“
- Kombinované otázky: „Skontroluj počasie v Paríži a povedz mi, či je nad 20°C“

Všimnite si, ako agent interpretuje prirodzený jazyk a mapuje ho na správne volania nástrojov.

## Kľúčové koncepty

### ReAct vzor (Rozumovanie a konanie)

Agent strieda rozumovanie (rozhodovanie, čo robiť) a konanie (používanie nástrojov). Tento vzor umožňuje autonómne riešenie problémov namiesto iba reagovania na pokyny.

### Popisy nástrojov sú dôležité

Kvalita vašich popisov nástrojov priamo ovplyvňuje, ako ich agent používa. Jasné a špecifické popisy pomáhajú modelu pochopiť, kedy a ako každý nástroj volať.

### Správa relácií

Anotácia `@MemoryId` umožňuje automatickú správu pamäte viazanú na relácie. Každé ID relácie získa vlastnú inštanciu `ChatMemory`, ktorú spravuje bean `ChatMemoryProvider`, takže viacerí používatelia môžu s agentom komunikovať súbežne bez prelínania konverzácií. Nasledujúci diagram ukazuje, ako sú viacerí používatelia smerovaní do izolovaných pamätí na základe ich ID relácie:

<img src="../../../translated_images/sk/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Každé ID relácie sa mapuje do izolovanej histórie konverzácií — používatelia nikdy nevidia správy ostatných.*

### Spracovanie chýb

Nástroje môžu zlyhať — API môžu mať časový limit, parametre môžu byť neplatné, externé služby môžu padnúť. Produkční agenti potrebujú spracovanie chýb, aby model mohol vysvetliť problémy alebo skúsiť alternatívy namiesto zlyhania celej aplikácie. Keď nástroj vyhodí výnimku, LangChain4j ju zachytí a pošle chybovú správu spätne do modelu, ktorý potom môže vysvetliť problém prirodzeným jazykom.

## Dostupné nástroje

Nasledujúci diagram ukazuje širokú ekosystém nástrojov, ktoré môžete vytvoriť. Tento modul demonštruje nástroje na počasie a teplotu, ale rovnaký vzor `@Tool` funguje pre akúkoľvek Java metódu — od dotazov na databázy po spracovanie platieb.

<img src="../../../translated_images/sk/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Každá Java metóda anotovaná `@Tool` je dostupná pre AI — tento vzor sa rozširuje na databázy, API, e-maily, operácie so súbormi a viac.*

## Kedy používať agentov založených na nástrojoch

Nie každá požiadavka vyžaduje nástroje. Rozhodnutie spočíva v tom, či AI musí komunikovať s externými systémami, alebo či môže odpovedať zo svojich znalostí. Nasledujúci sprievodca zhrňuje, kedy nástroje prinášajú hodnotu a kedy nie sú potrebné:

<img src="../../../translated_images/sk/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*Rýchly rozhodovací sprievodca — nástroje sú vhodné pre aktuálne údaje, výpočty a akcie; všeobecné znalosti a tvorivé úlohy ich nepotrebujú.*

## Nástroje vs RAG

Moduly 03 a 04 oba rozširujú možnosti AI, ale zásadne odlišným spôsobom. RAG poskytuje modelu prístup ku **znalostiam** vyhľadávaním dokumentov. Nástroje dávajú modelu schopnosť vykonávať **akcie** volaním funkcií. Nasledujúci diagram porovnáva tieto dva prístupy vedľa seba — od spôsobu ich fungovania až po kompromisy medzi nimi:

<img src="../../../translated_images/sk/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG získava informácie zo statických dokumentov — nástroje vykonávajú akcie a získavajú dynamické, aktuálne údaje. Mnoho produkčných systémov kombinuje oba prístupy.*

V praxi mnohé produkčné systémy kombinujú oba prístupy: RAG na zakotvenie odpovedí vo vašej dokumentácii a nástroje na získavanie živých údajov alebo vykonávanie operácií.

## Ďalšie kroky

**Ďalší modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigácia:** [← Predchádzajúci: Modul 03 - RAG](../03-rag/README.md) | [Späť na hlavnú stránku](../README.md) | [Ďalší: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vyhlásenie o vylúčení zodpovednosti**:
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Hoci sa snažíme o presnosť, berte prosím na vedomie, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Originálny dokument v jeho pôvodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre dôležité informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za akékoľvek nepochopenia alebo nesprávne výklady vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->