# Modul 04: AI agenti s nástrojmi

## Obsah

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
- [Použitie aplikácie](../../../04-tools)
  - [Vyskúšajte jednoduché použitie nástroja](../../../04-tools)
  - [Testujte reťazenie nástrojov](../../../04-tools)
  - [Zobraziť tok rozhovoru](../../../04-tools)
  - [Experimentujte s rôznymi požiadavkami](../../../04-tools)
- [Kľúčové pojmy](../../../04-tools)
  - [ReAct vzor (rozumej a konaj)](../../../04-tools)
  - [Dôležitosť popisov nástrojov](../../../04-tools)
  - [Správa relácie](../../../04-tools)
  - [Riešenie chýb](../../../04-tools)
- [Dostupné nástroje](../../../04-tools)
- [Kedy používať agentov založených na nástrojoch](../../../04-tools)
- [Nástroje vs RAG](../../../04-tools)
- [Ďalšie kroky](../../../04-tools)

## Čo sa naučíte

Doteraz ste sa naučili, ako viesť rozhovory s AI, ako efektívne štruktúrovať podnety a ako zakladať odpovede na vašich dokumentoch. Ale stále existuje základné obmedzenie: jazykové modely vedia iba generovať text. Nedokážu skontrolovať počasie, robiť výpočty, dopytovať databázy ani komunikovať s externými systémami.

Nástroje toto menia. Keď modelu umožníte volať funkcie, transformujete ho z generátora textu na agenta, ktorý môže robiť akcie. Model rozhoduje, kedy potrebuje nástroj, ktorý nástroj použiť a aké parametre odovzdať. Váš kód vykoná funkciu a vráti výsledok. Model tento výsledok zapracuje do svojej odpovede.

## Predpoklady

- Dokončený Modul 01 (nasadené Azure OpenAI zdroje)
- Súbor `.env` v koreňovom adresári s Azure povereniami (vytvorené príkazom `azd up` v Module 01)

> **Poznámka:** Ak ste Modul 01 nedokončili, najskôr postupujte podľa inštrukcií na nasadenie v ňom.

## Pochopenie AI agentov s nástrojmi

> **📝 Poznámka:** Termín „agenti“ v tomto module označuje AI asistentov vylepšených o schopnosť volania nástrojov. To sa líši od **Agentic AI** vzorov (autonómni agenti s plánovaním, pamäťou a viacstupňovým uvažovaním), ktoré pokryjeme v [Module 05: MCP](../05-mcp/README.md).

Bez nástrojov môže jazykový model iba generovať text na základe svojich trénovacích dát. Spýtajte sa ho na aktuálne počasie a len háda. Dajte mu nástroje a môže volať API počasia, robiť výpočty alebo dopytovať databázu – a tieto skutočné výsledky zapracovať do odpovede.

<img src="../../../translated_images/sk/what-are-tools.724e468fc4de64da.webp" alt="Bez nástrojov vs S nástrojmi" width="800"/>

*Bez nástrojov model iba háda — s nástrojmi môže volať API, robiť výpočty a prinášať aktuálne údaje.*

AI agent s nástrojmi nasleduje **vzor ReAct (rozumej a konaj)**. Model nielen odpovedá — rozmýšľa o tom, čo potrebuje, koná volaním nástroja, sleduje výsledok a rozhoduje, či znova konať alebo dodať výslednú odpoveď:

1. **Rozumieť** — Agent analyzuje používateľovu otázku a zisťuje, aké informácie potrebuje
2. **Konať** — Agent vyberie správny nástroj, vygeneruje správne parametre a zavolá ho
3. **Sledovať** — Agent dostane výstup nástroja a vyhodnotí ho
4. **Opakovať alebo odpovedať** — Ak je potrebných viac dát, agent sa vráti k začiatku; inak vytvorí odpoveď v prirodzenom jazyku

<img src="../../../translated_images/sk/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct vzor" width="800"/>

*Cyklus ReAct — agent rozmýšľa, čo robiť, koná volaním nástroja, sleduje výsledok a opakuje, kým nedodá odpoveď.*

Toto prebieha automaticky. Vy definujete nástroje a ich popisy. Model rozhoduje, kedy a ako ich použiť.

## Ako funguje volanie nástrojov

### Definície nástrojov

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Definujete funkcie s jasnými popismi a špecifikáciami parametrov. Model vidí tieto popisy vo svojom systémovom podnete a chápe, čo každý nástroj robí.

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
// - ChatModel bean
// - Všetky @Tool metódy z tried označených @Component
// - ChatMemoryProvider pre správu relácií
```

Nižšie zobrazený diagram rozoberá každú anotáciu a ukazuje, ako každý diel pomáha AI pochopiť, kedy volať nástroj a aké argumenty odovzdať:

<img src="../../../translated_images/sk/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatómia definícií nástrojov" width="800"/>

*Anatómia definície nástroja — @Tool hovorí AI, kedy ho použiť, @P popisuje každý parameter a @AiService prepája všetko pri štarte.*

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) a spýtajte sa:
> - „Ako by som integroval skutočné weather API ako OpenWeatherMap namiesto simulovaných dát?“
> - „Čo robí dobrý popis nástroja, ktorý pomáha AI nástroj správne použiť?“
> - „Ako riešiť chyby API a limity volaní pri implementácii nástrojov?“

### Rozhodovanie

Keď používateľ položí otázku „Aké je počasie v Seattli?“, model náhodne nevyberá nástroj. Porovnáva zámer používateľa so všetkými popismi nástrojov, ktoré má k dispozícii, vyhodnotí ich relevantnosť a vyberie najlepší zodpovedajúci nástroj. Potom vygeneruje štruktúrované volanie funkcie s pravými parametrami – v tomto prípade nastaví `location` na `"Seattle"`.

Ak žiadny nástroj nespĺňa požiadavku používateľa, model odpovie na základe svojich vedomostí. Ak sa nájde viac nástrojov, vyberie ten najšpecifickejší.

<img src="../../../translated_images/sk/decision-making.409cd562e5cecc49.webp" alt="Ako AI rozhoduje, ktorý nástroj použiť" width="800"/>

*Model vyhodnocuje všetky dostupné nástroje voči zámeru používateľa a vyberie ten najvhodnejší — preto je dôležité písať jasné, konkrétne popisy nástrojov.*

### Vykonanie

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot automaticky prepája deklaratívne rozhranie `@AiService` so všetkými registrovanými nástrojmi a LangChain4j vykonáva volania nástrojov automaticky. V pozadí prechádza celé volanie nástroja šiestimi fázami – od otázky používateľa v prirodzenom jazyku až po odpoveď v prirodzenom jazyku:

<img src="../../../translated_images/sk/tool-calling-flow.8601941b0ca041e6.webp" alt="Tok volania nástroja" width="800"/>

*Kompletný tok – používateľ položí otázku, model vyberie nástroj, LangChain4j ho vykoná a model zapracuje výsledok do odpovede.*

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) a spýtajte sa:
> - „Ako funguje ReAct vzor a prečo je efektívny pre AI agentov?“
> - „Ako agent rozhoduje, ktorý nástroj použiť a v akom poradí?“
> - „Čo sa stane, ak volanie nástroja zlyhá – ako robustne riešiť chyby?“

### Generovanie odpovede

Model dostane údaje o počasí a formátuje ich do prirodzenej jazykovej odpovede pre používateľa.

### Architektúra: Spring Boot automatické prepojenie

Tento modul využíva integráciu LangChain4j so Spring Boot cez deklaratívne rozhrania `@AiService`. Pri štarte Spring Boot objaví každý `@Component`, ktorý obsahuje metódy s anotáciou `@Tool`, vašu `ChatModel` komponentu a `ChatMemoryProvider` — a všetko to spojí do jediného rozhrania `Assistant` bez potreby písania boilerplate kódu.

<img src="../../../translated_images/sk/spring-boot-wiring.151321795988b04e.webp" alt="Architektúra Spring Boot automatického prepojenia" width="800"/>

*Rozhranie @AiService spája ChatModel, komponenty nástrojov a poskytovateľa pamäte — Spring Boot automaticky rieši prepojenie.*

Kľúčové výhody tohto prístupu:

- **Spring Boot automatické prepojenie** — ChatModel a nástroje sa injektujú automaticky
- **@MemoryId vzor** — Automatická správa pamäte na základe relácie
- **Jediná inštancia** — Asistent sa vytvorí raz a znovu používa pre lepší výkon
- **Typovo bezpečné vykonanie** — Java metódy volané priamo so konverziou typov
- **Viackroková orchestrácia** — Automaticky zvládne reťazenie nástrojov
- **Žiadny boilerplate** — Žiadne manuálne volania AiServices.builder() alebo pamäťové HashMapy

Alternatívne prístupy (manuálne `AiServices.builder()`) vyžadujú viac kódu a postrádajú výhody Spring Boot integrácie.

## Reťazenie nástrojov

**Reťazenie nástrojov** — Skutočná sila agentov založených na nástrojoch sa prejaví, keď jedna otázka vyžaduje použitie viacerých nástrojov. Pri otázke „Aké je počasie v Seattli vo Fahrenheit?“ agent automaticky spája dva nástroje: najskôr volá `getCurrentWeather` pre teplotu v Celziách, potom tento výsledok predá do `celsiusToFahrenheit` na prevod — všetko v jednom rozhovorovom kole.

<img src="../../../translated_images/sk/tool-chaining-example.538203e73d09dd82.webp" alt="Príklad reťazenia nástrojov" width="800"/>

*Reťazenie nástrojov v praxi — agent najskôr zavolá getCurrentWeather, potom výsledok v Celziách pošle do celsiusToFahrenheit a dodá kombinovanú odpoveď.*

Takto to vyzerá v bežiacej aplikácii — agent spája dva volania nástrojov v jednom rozhovorovom kole:

<a href="images/tool-chaining.png"><img src="../../../translated_images/sk/tool-chaining.3b25af01967d6f7b.webp" alt="Reťazenie nástrojov" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Skutočný výstup aplikácie — agent automaticky spája getCurrentWeather → celsiusToFahrenheit v jednom kole.*

**Elegantné zlyhanie** — Požiadajte o počasie v meste, ktoré nie je v simulovaných dátach. Nástroj vráti chybovú správu a AI vysvetlí, že nemôže pomôcť namiesto toho, aby vypadla. Nástroje bezpečne zlyhávajú.

<img src="../../../translated_images/sk/error-handling-flow.9a330ffc8ee0475c.webp" alt="Tok riešenia chýb" width="800"/>

*Ak nástroj zlyhá, agent zachytí chybu a odpovie s užitočným vysvetlením namiesto pádu.*

Toto prebieha v rámci jedného rozhovorového kola. Agent autonómne orchestruje viacero volaní nástrojov.

## Spustenie aplikácie

**Overenie nasadenia:**

Uistite sa, že súbor `.env` je v koreňovom adresári s Azure povereniami (vytvorený počas Modulu 01):
```bash
cat ../.env  # Malo by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spustenie aplikácie:**

> **Poznámka:** Ak ste už spustili všetky aplikácie pomocou `./start-all.sh` z Modulu 01, tento modul už beží na porte 8084. Môžete preskočiť nižšie uvedené príkazy na spustenie a ísť priamo na http://localhost:8084.

**Možnosť 1: Použitie Spring Boot Dashboard (Odporúčané pre používateľov VS Code)**

Vývojársky kontajner obsahuje rozšírenie Spring Boot Dashboard, ktoré poskytuje vizuálne rozhranie na správu všetkých Spring Boot aplikácií. Nájdete ho na paneli aktivít vľavo vo VS Code (ikona Spring Boot).

V Spring Boot Dashboard môžete:
- Vidieť všetky dostupné Spring Boot aplikácie v pracovnom priestore
- Spúšťať/zastavovať aplikácie jedným kliknutím
- Prezerať logy aplikácií v reálnom čase
- Monitorovať stav aplikácie

Stačí kliknúť na tlačidlo play vedľa "tools" pre spustenie tohto modulu, alebo spustiť všetky moduly naraz.

<img src="../../../translated_images/sk/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

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

> **Poznámka:** Ak chcete pred spustením manuálne zostaviť všetky moduly:
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

## Použitie aplikácie

Aplikácia poskytuje webové rozhranie, v ktorom môžete komunikovať s AI agentom, ktorý má prístup k nástrojom na počasie a prevod teplôt.

<a href="images/tools-homepage.png"><img src="../../../translated_images/sk/tools-homepage.4b4cd8b2717f9621.webp" alt="Rozhranie AI agent s nástrojmi" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Rozhranie AI agent s nástrojmi – rýchle príklady a chatovacie rozhranie pre interakciu s nástrojmi*

### Vyskúšajte jednoduché použitie nástroja
Začnite s jednoduchou požiadavkou: "Preveďte 100 stupňov Fahrenheita na Celziovu teplotu". Agent rozpozná, že potrebuje nástroj na konverziu teploty, zavolá ho s správnymi parametrami a vráti výsledok. Všimnite si, ako prirodzene to pôsobí – neurčili ste, ktorý nástroj použiť alebo ako ho zavolať.

### Test reťazenia nástrojov

Teraz vyskúšajte niečo zložitejšie: "Aké je počasie v Seattli a preveď to na Fahrenheit?" Sledujte, ako agent postupuje krok za krokom. Najprv získa počasie (ktoré vracia Celziovu teplotu), uvedomí si, že potrebuje previesť na Fahrenheit, zavolá nástroj na konverziu a skombinuje obe výsledky do jednej odpovede.

### Pozrite si tok konverzácie

Chatové rozhranie uchováva históriu konverzácie, čo vám umožňuje viesť viacstupňové interakcie. Vidíte všetky predchádzajúce otázky a odpovede, čo uľahčuje sledovanie konverzácie a pochopenie, ako agent buduje kontext počas viacerých výmen.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/sk/tools-conversation-demo.89f2ce9676080f59.webp" alt="Konverzácia s viacerými volaniami nástrojov" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Viacstupňová konverzácia ukazujúca jednoduché konverzie, vyhľadávanie počasia a reťazenie nástrojov*

### Experimentujte s rôznymi požiadavkami

Vyskúšajte rôzne kombinácie:
- Vyhľadávanie počasia: "Aké je počasie v Tokiu?"
- Konverzie teploty: "Koľko je 25 °C v Kelvinoch?"
- Kombinované otázky: "Skontroluj počasie v Paríži a povedz mi, či je viac ako 20 °C"

Všimnite si, ako agent interpretuje prirodzený jazyk a priraďuje ho k vhodným volaniam nástrojov.

## Kľúčové koncepty

### ReAct vzor (Uvažovanie a konanie)

Agent strieda uvažovanie (rozhodovanie, čo robiť) a konanie (používanie nástrojov). Tento vzor umožňuje autonómne riešenie problémov namiesto jednoduchého reagovania na inštrukcie.

### Popisy nástrojov sú dôležité

Kvalita popisov vašich nástrojov priamo ovplyvňuje, ako dobre ich agent používa. Jasné, konkrétne popisy pomáhajú modelu pochopiť, kedy a ako zavolať každý nástroj.

### Správa relácií

Anotácia `@MemoryId` umožňuje automatickú správu pamäte založenú na reláciách. Každé ID relácie dostane svoju vlastnú inštanciu `ChatMemory` spravovanú komponentom `ChatMemoryProvider`, takže s agentom môže súčasne komunikovať viacerí používatelia bez miešania ich konverzácií.

<img src="../../../translated_images/sk/session-management.91ad819c6c89c400.webp" alt="Správa relácií s @MemoryId" width="800"/>

*Každé ID relácie zodpovedá izolovanej histórii konverzácie – používatelia nikdy nevidia správy iných.*

### Spracovanie chýb

Nástroje môžu zlyhať – API môžu mať vypršaný čas, parametre môžu byť neplatné, vonkajšie služby môžu padnúť. Produkčné agenti potrebujú spracovanie chýb, aby model vedel vysvetliť problém alebo vyskúšať alternatívy namiesto zhroucenia celej aplikácie. Keď nástroj vyhodí výnimku, LangChain4j ju zachytáva a posiela späť modelu, ktorý potom dokáže problém vysvetliť prirodzeným jazykom.

## Dostupné nástroje

Nižšie uvedený diagram ukazuje široké spektrum nástrojov, ktoré môžete vytvoriť. Tento modul ukazuje nástroje na počasie a teplotu, ale rovnaký vzor `@Tool` funguje pre akúkoľvek Java metódu – od databázových dotazov po spracovanie platieb.

<img src="../../../translated_images/sk/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ekosystém nástrojov" width="800"/>

*Akákoľvek Java metóda anotovaná `@Tool` sa stáva dostupnou pre AI – vzor sa rozširuje na databázy, API, email, operácie so súbormi a ďalšie.*

## Kedy používať agentov založených na nástrojoch

<img src="../../../translated_images/sk/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Kedy používať nástroje" width="800"/>

*Rýchly rozhodovací sprievodca – nástroje sú na dáta v reálnom čase, výpočty a akcie; všeobecné znalosti a kreatívne úlohy ich nevyžadujú.*

**Používajte nástroje, keď:**
- Odpoveď vyžaduje údaje v reálnom čase (počasie, ceny akcií, stav zásob)
- Potrebujete vykonať výpočty nad rámec jednoduchej matematiky
- Pristupujete k databázam alebo API
- Vykonávate akcie (odosielanie emailov, vytváranie tiketov, aktualizácia záznamov)
- Kombinujete viaceré zdroje údajov

**Nepoužívajte nástroje, keď:**
- Otázky sa dajú zodpovedať z všeobecných znalostí
- Odpoveď je čisto konverzačná
- Latencia nástrojov by spomalila používateľský zážitok

## Nástroje vs RAG

Moduly 03 a 04 rozširujú schopnosti AI, ale zásadne odlišne. RAG poskytuje modelu prístup k **vedomostiam** cez vyhľadávanie dokumentov. Nástroje dávajú modelu možnosť vykonávať **akcie** volaním funkcií.

<img src="../../../translated_images/sk/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Porovnanie nástrojov a RAG" width="800"/>

*RAG získava informácie zo statických dokumentov – nástroje vykonávajú akcie a získavajú dynamické, aktuálne dáta. Mnohé produkčné systémy kombinujú obe prístupy.*

V praxi mnohé produkčné systémy kombinujú oba prístupy: RAG na zakladanie odpovedí vo vašej dokumentácii a nástroje na získavanie živých údajov alebo vykonávanie operácií.

## Ďalšie kroky

**Nasledujúci modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigácia:** [← Predchádzajúci: Modul 03 - RAG](../03-rag/README.md) | [Späť na hlavnú stránku](../README.md) | [Ďalší: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vyhlásenie o zodpovednosti**:  
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, majte prosím na pamäti, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Originálny dokument v jeho pôvodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre dôležité informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za akékoľvek nedorozumenia alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->