# Modul 04: AI agenti z orodji

## Kazalo

- [Kaj se boste naučili](../../../04-tools)
- [Pogoji](../../../04-tools)
- [Razumevanje AI agentov z orodji](../../../04-tools)
- [Kako deluje klic orodij](../../../04-tools)
  - [Definicije orodij](../../../04-tools)
  - [Sprejemanje odločitev](../../../04-tools)
  - [Izvrševanje](../../../04-tools)
  - [Generiranje odgovora](../../../04-tools)
  - [Arhitektura: Spring Boot samodejno povezovanje](../../../04-tools)
- [Verižna uporaba orodij](../../../04-tools)
- [Zagon aplikacije](../../../04-tools)
- [Uporaba aplikacije](../../../04-tools)
  - [Preizkusite preprosto uporabo orodja](../../../04-tools)
  - [Preizkusite verižna orodja](../../../04-tools)
  - [Oglejte si potek pogovora](../../../04-tools)
  - [Preizkušajte različne poizvedbe](../../../04-tools)
- [Ključni koncepti](../../../04-tools)
  - [Vzorec ReAct (razmišljanje in ukrepanje)](../../../04-tools)
  - [Pomen opisov orodij](../../../04-tools)
  - [Upravljanje sej](../../../04-tools)
  - [Ravnanje z napakami](../../../04-tools)
- [Razpoložljiva orodja](../../../04-tools)
- [Kdaj uporabljati agente z orodji](../../../04-tools)
- [Orodja proti RAG](../../../04-tools)
- [Naslednji koraki](../../../04-tools)

## Kaj se boste naučili

Do zdaj ste se naučili, kako voditi pogovore z AI, učinkovito strukturirati pozive in utemeljiti odgovore v vaših dokumentih. A obstaja temeljna omejitev: jezikovni modeli lahko generirajo samo besedilo. Ne morejo preveriti vremena, izvajati izračunov, poizvedovati po bazah podatkov ali komunicirati z zunanjimi sistemi.

Orodja to spremenijo. Z omogočanjem klica funkcij modelu ga spremenite iz generatorja besedila v agenta, ki lahko izvaja dejanja. Model odloča, kdaj potrebuje orodje, katero uporabiti in katere parametre posredovati. Vaša koda izvede funkcijo in vrne rezultat. Model rezultat vključi v svoj odgovor.

## Pogoji

- Dokončan modul 01 (razporejeni Azure OpenAI viri)
- Datoteka `.env` v korenski mapi z Azure poverilnicami (ustvarjena z `azd up` v modulu 01)

> **Opomba:** Če niste dokončali modula 01, najprej sledite tamkajšnjim navodilom za namestitev.

## Razumevanje AI agentov z orodji

> **📝 Opomba:** Izraz "agenti" v tem modulu se nanaša na AI asistente, opremljene s sposobnostjo klica orodij. To se razlikuje od vzorcev **Agentic AI** (avtonomni agenti z načrtovanjem, spominom in večstopenjskim razmišljanjem), ki jih bomo obravnavali v [Modulu 05: MCP](../05-mcp/README.md).

Brez orodij lahko jezikovni model generira samo besedilo iz svoje učne baze. Če ga vprašate za trenutno vreme, mora ugibati. Če mu zagotovite orodja, lahko kliče vremenski API, izvaja izračune ali poizveduje po bazi — potem pa v svoj odgovor vključi dejanske rezultate.

<img src="../../../translated_images/sl/what-are-tools.724e468fc4de64da.webp" alt="Brez orodij proti z orodji" width="800"/>

*Brez orodij model lahko le ugiba — z orodji lahko kliče API-je, izvaja izračune in vrača podatke v realnem času.*

AI agent z orodji sledi vzorcu **Razmišljanje in Ukrepanje (ReAct)**. Model ne odgovori samo, ampak razmišlja, kaj potrebuje, ukrepa s klicem orodja, opazuje rezultat in se nato odloča, ali bo še naprej ukrepal ali bo dal končni odgovor:

1. **Razmišljanje** — agent analizira uporabnikovo vprašanje in ugotovi, katere informacije potrebuje
2. **Ukrepanje** — agent izbere pravo orodje, pripravi ustrezne parametre in ga pokliče
3. **Opazovanje** — agent prejme izhod orodja in oceni rezultat
4. **Ponovitev ali odgovor** — če so potrebni dodatni podatki, se agent vrne na začetek; sicer oblikuje naraven odgovor

<img src="../../../translated_images/sl/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Vzorec ReAct" width="800"/>

*Cikel ReAct — agent razmišlja, kaj narediti, ukrepa s klicem orodja, opazuje rezultat in se ponavlja do končnega odgovora.*

To se dogaja samodejno. Vi definirate orodja in njihove opise. Model sam sprejema odločitve, kdaj in kako jih uporabiti.

## Kako deluje klic orodij

### Definicije orodij

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Definirate funkcije s jasnimi opisi in specifikacijami parametrov. Model te opise vidi v sistemskem pozivu in razume, kaj posamezno orodje počne.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Vaša logika za iskanje vremenskih podatkov
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Asistent je samodejno povezan s Spring Boot z:
// - ChatModel bean
// - Vse @Tool metode iz @Component razredov
// - ChatMemoryProvider za upravljanje sej
```
  
Diagram spodaj razčleni vsako anotacijo in pokaže, kako vsak del pomaga AI pri razumevanju, kdaj poklicati orodje in katere argumente posredovati:

<img src="../../../translated_images/sl/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomija definicij orodij" width="800"/>

*Anatomija definicije orodja — @Tool pove AI, kdaj ga uporabiti, @P opisuje vsak parameter, @AiService pa vse skupaj poveže ob zagonu.*

> **🤖 Preizkusite s [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) in vprašajte:
> - "Kako bi integriral pravi vremenski API, na primer OpenWeatherMap, namesto simuliranih podatkov?"
> - "Kaj naredi dober opis orodja, ki pomaga AI, da ga pravilno uporabi?"
> - "Kako ravnati z napakami API in omejitvami zahtev v implementacijah orodij?"

### Sprejemanje odločitev

Ko uporabnik vpraša "Kakšno je vreme v Seattleju?", model ni naključno izbere orodja. Primerja namero uporabnika s vsemi opisi orodij, ki jih ima, oceni relevantnost vsakega in izbere najboljšo ujemajočo se možnost. Nato generira strukturiran klic funkcije z ustreznimi parametri — v tem primeru nastavi `location` na `"Seattle"`.

Če nobeno orodje ne ustreza zahtevku uporabnika, model odgovori na podlagi svojega znanja. Če več orodij ustreza, izbere najbolj specifično.

<img src="../../../translated_images/sl/decision-making.409cd562e5cecc49.webp" alt="Kako AI odloča, katero orodje uporabiti" width="800"/>

*Model oceni vsako na voljo orodje glede na uporabnikovo namero in izbere najboljše ujemanje — zato je pisanje jasnih, specifičnih opisov orodij pomembno.*

### Izvrševanje

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot samodejno povezne deklarativni vmesnik `@AiService` z vsemi registriranimi orodji, LangChain4j pa orodja kliče samodejno. V ozadju poteka celoten klic orodja skozi šest faz — od naravnega jezika vprašanja uporabnika do naravnega jezika odgovora:

<img src="../../../translated_images/sl/tool-calling-flow.8601941b0ca041e6.webp" alt="Potek klica orodij" width="800"/>

*Koncni potek — uporabnik postavi vprašanje, model izbere orodje, LangChain4j ga izvede, model pa rezultat vključi v naraven odgovor.*

> **🤖 Preizkusite s [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) in vprašajte:
> - "Kako deluje vzorec ReAct in zakaj je učinkovit za AI agente?"
> - "Kako agent odloča, katero orodje uporabiti in v kakšnem vrstnem redu?"
> - "Kaj se zgodi, če izvršitev orodja spodleti - kako robustno upravljati z napakami?"

### Generiranje odgovora

Model prejme vremenske podatke in jih oblikuje v naraven jezikovni odgovor za uporabnika.

### Arhitektura: Spring Boot samodejno povezovanje

Ta modul uporablja Spring Boot integracijo LangChain4j z deklarativnimi vmesniki `@AiService`. Ob zagonu Spring Boot odkrije vsak `@Component`, ki vsebuje metode označene z `@Tool`, vaš `ChatModel` bean in `ChatMemoryProvider` — ter jih vse poveže v en sam vmesnik `Assistant` brez potrebe po dodatnem ročnem kodiranju.

<img src="../../../translated_images/sl/spring-boot-wiring.151321795988b04e.webp" alt="Arhitektura Spring Boot samodejnega povezovanja" width="800"/>

*Vmesnik @AiService povezuje ChatModel, komponente orodij in ponudnika spomina — Spring Boot samodejno poskrbi za vse povezave.*

Ključne prednosti tega pristopa:

- **Samodejno povezovanje Spring Boot** — ChatModel in orodja samodejno injicirani
- **Vzorec @MemoryId** — Avtomatsko upravljanje spomina na osnovi seje
- **Enkratni primerek** — Asistent ustvarjen enkrat in ponovno uporabljen za boljšo zmogljivost
- **Tipno varno izvrševanje** — Java metode kliče neposredno s pretvorbo tipov
- **Večkratni koraki** — Samodejno omogoča verižna orodja
- **Brez nepotrebnega kode** — Brez ročnih klicev `AiServices.builder()` ali hash mape spomina

Alternativni pristopi (ročni `AiServices.builder()`) zahtevajo več kode in nimajo prednosti integracije Spring Boot.

## Verižna uporaba orodij

**Verižna uporaba orodij** — Prava moč agentov z orodji se pokaže, ko eno vprašanje zahteva več orodij. Vprašajte "Kakšno je vreme v Seattleju v Fahrenheitu?" in agent samodejno poveže dve orodji: najprej kliče `getCurrentWeather` za temperaturo v Celziju, nato to vrednost posreduje `celsiusToFahrenheit` za pretvorbo — vse v enem pogovornem koraku.

<img src="../../../translated_images/sl/tool-chaining-example.538203e73d09dd82.webp" alt="Primer verižne uporabe orodij" width="800"/>

*Verižna uporaba orodij v akciji — agent najprej kliče getCurrentWeather, nato pretvori rezultat v Celzijih v Fahrenheite in poda združen odgovor.*

Tako izgleda v tekoči aplikaciji — agent verižne dva klica orodij v enem pogovornem koraku:

<a href="images/tool-chaining.png"><img src="../../../translated_images/sl/tool-chaining.3b25af01967d6f7b.webp" alt="Verižna uporaba orodij" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Dejanski izhod aplikacije — agent samodejno poveže getCurrentWeather → celsiusToFahrenheit v enem koraku.*

**Vljudne napake** — Vprašajte za vreme v mestu, ki ni v simuliranih podatkih. Orodje vrne sporočilo o napaki, AI pa razloži, da ne more pomagati, namesto da bi se zrušil. Orodja varno poročajo napake.

<img src="../../../translated_images/sl/error-handling-flow.9a330ffc8ee0475c.webp" alt="Potek obravnave napak" width="800"/>

*Ko orodje odpove, agent ujame napako in odgovori s koristno razlago, namesto da bi se zrušil.*

To se zgodi v enem pogovornem koraku. Agent avtonomno orkestrira več klicev orodij.

## Zagon aplikacije

**Preverite namestitev:**

Prepričajte se, da datoteka `.env` obstaja v korenski mapi z Azure poverilnicami (ustvarjena v modulu 01):
```bash
cat ../.env  # Prikaže naj AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Zaženite aplikacijo:**

> **Opomba:** Če ste že zagnali vse aplikacije z `./start-all.sh` iz modula 01, ta modul že teče na vratih 8084. Lahko preskočite spodnje ukaze in greste neposredno na http://localhost:8084.

**Možnost 1: Uporaba Spring Boot nadzorne plošče (priporočeno za uporabnike VS Code)**

Razvojni kontejner vključuje razširitev Spring Boot Dashboard, ki nudi vizualni vmesnik za upravljanje vseh Spring Boot aplikacij. Najdete jo v Aktivnostni vrstici na levi strani VS Code (poiščite ikono Spring Boot).

Iz Spring Boot nadzorne plošče lahko:  
- Vidite vse razpoložljive Spring Boot aplikacije v delovnem prostoru  
- Zaženete/ustavite aplikacije z enim klikom  
- V realnem času spremljate dnevniške datoteke aplikacij  
- Spremljate stanje aplikacije  

Preprosto kliknite gumb za predvajanje ob "tools", da zaženete ta modul, ali zaženite vse module hkrati.

<img src="../../../translated_images/sl/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

**Možnost 2: Uporaba ukaznih skript**

Zaženite vse spletne aplikacije (moduli 01-04):

**Bash:**  
```bash
cd ..  # Iz korenske mape
./start-all.sh
```
  
**PowerShell:**  
```powershell
cd ..  # Iz korenske mape
.\start-all.ps1
```
  
Ali zaženite samo ta modul:

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
  
Oba skripta samodejno naložita okoljske spremenljivke iz datoteke `.env` v korenski mapi in če JAR datoteke ne obstajajo, jih zgradita.

> **Opomba:** Če želite ročno zgraditi vse module pred zagonom:  
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
  
Odprite http://localhost:8084 v brskalniku.

**Za ustavitev:**

**Bash:**  
```bash
./stop.sh  # Samo ta modul
# Ali
cd .. && ./stop-all.sh  # Vsi moduli
```
  
**PowerShell:**  
```powershell
.\stop.ps1  # Samo ta modul
# Ali
cd ..; .\stop-all.ps1  # Vsi moduli
```
  

## Uporaba aplikacije

Aplikacija omogoča spletni vmesnik, kjer lahko komunicirate z AI agentom, ki ima dostop do vremenskih in orodij za pretvorbo temperature.

<a href="images/tools-homepage.png"><img src="../../../translated_images/sl/tools-homepage.4b4cd8b2717f9621.webp" alt="Vmesnik AI agent z orodji" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Vmesnik AI agenta z orodji - hitri primeri in klepetalni vmesnik za delo z orodji*

### Preizkusite preprosto uporabo orodja
Začnite z neposredno zahtevo: "Pretvori 100 stopinj Fahrenheita v Celzija". Agent prepozna, da potrebuje orodje za pretvorbo temperature, ga pokliče z ustreznimi parametri in vrne rezultat. Upoštevajte, kako naravno deluje - niste določili, katero orodje uporabiti ali kako ga poklicati.

### Preizkusi verižitev orodij

Sedaj poskusi nekaj bolj zapletenega: "Kakšno je vreme v Seattlu in pretvori v Fahrenheit?" Opazuj, kako agent to obravnava v korakih. Najprej pridobi vreme (ki vrne Celzije), nato prepozna potrebo po pretvorbi v Fahrenheit, pokliče orodje za pretvorbo in združi oba rezultata v en odgovor.

### Oglej si potek pogovora

Pogovorni vmesnik ohranja zgodovino pogovora, kar omogoča večkratne izmenjave. Lahko vidiš vse prejšnje poizvedbe in odgovore, kar olajša sledenje pogovoru in razumevanje, kako agent gradi kontekst skozi več izmenjav.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/sl/tools-conversation-demo.89f2ce9676080f59.webp" alt="Pogovor z več klici orodij" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Večkratni pogovor, ki prikazuje preproste pretvorbe, iskanje vremena in verižitev orodij*

### Preizkusi različne zahteve

Poskusi različne kombinacije:
- Iskanje vremena: "Kakšno je vreme v Tokiu?"
- Pretvorbe temperature: "Koliko je 25 °C v kelvinih?"
- Kombinirane poizvedbe: "Preveri vreme v Parizu in mi povej, če je nad 20 °C"

Opazuj, kako agent interpretira naravni jezik in ga preslika v ustrezne klice orodij.

## Ključni pojmi

### ReAct vzorec (Presojanje in ukrepanje)

Agent izmenično razmišlja (odloča, kaj narediti) in ukrepa (uporablja orodja). Ta vzorec omogoča avtonomno reševanje problemov namesto zgolj odgovarjanja na ukaze.

### Opisi orodij so pomembni

Kakovost opisov orodij neposredno vpliva na to, kako dobro jih agent uporablja. Jasni in specifični opisi pomagajo modelu razumeti, kdaj in kako poklicati posamezno orodje.

### Upravljanje seja

Oznaka `@MemoryId` omogoča samodejno upravljanje pomnilnika na osnovi seje. Vsak ID seje dobi svojo instanco `ChatMemory`, ki jo upravlja `ChatMemoryProvider` bean, tako da lahko več uporabnikov hkrati komunicira z agentom brez medsebojnega prepletanja pogovorov.

<img src="../../../translated_images/sl/session-management.91ad819c6c89c400.webp" alt="Upravljanje sej z @MemoryId" width="800"/>

*Vsak ID seje ustreza izolirani zgodovini pogovora — uporabniki nikoli ne vidijo sporočil drug drugega.*

### Obvladovanje napak

Orodja lahko odpovejo — API-ji potečejo, parametri so lahko neveljavni, zunanje storitve so nedosegljive. Produkcijski agenti potrebujejo obvladovanje napak, da model lahko razloži težave ali poskusi alternative, namesto da bi celotna aplikacija padla. Ko orodje vrže izjemo, LangChain4j ujame ta izjemni dogodek in sporočilo o napaki poda modelu, ki lahko nato težavo razloži v naravnem jeziku.

## Razpoložljiva orodja

Spodnja shema prikazuje širok ekosistem orodij, ki jih lahko zgradite. Ta modul prikazuje orodja za vreme in temperaturo, a isti vzorec `@Tool` deluje za katerokoli Java metodo — od poizvedb v bazi do plačilnih postopkov.

<img src="../../../translated_images/sl/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ekosistem orodij" width="800"/>

*Katera koli Java metoda označena z @Tool postane dostopna za AI — vzorec se razteza na baze podatkov, API-je, elektronsko pošto, datotečne operacije in še več.*

## Kdaj uporabljati agente na osnovi orodij

<img src="../../../translated_images/sl/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Kdaj uporabljati orodja" width="800"/>

*Hitri vodič — orodja so namenjena podatkom v realnem času, izračunom in dejanjem; splošno znanje in ustvarjalne naloge jih ne potrebujejo.*

**Uporabi orodja, kadar:**
- Odgovori zahtevajo podatke v realnem času (vreme, cene delnic, zaloge)
- Potrebuješ izvedbo izračunov onkraj preproste matematike
- Dostopaš do baz podatkov ali API-jev
- Izvajaš dejanja (pošiljanje elektronskih sporočil, ustvarjanje prijavnic, posodabljanje zapisov)
- Združuješ več virov podatkov

**Ne uporabljaj orodij, kadar:**
- Vprašanja se lahko odgovorijo s splošnim znanjem
- Odgovor je zgolj konverzacijski
- Zamuda orodij bi upočasnila izkušnjo

## Orodja proti RAG

Modula 03 in 04 oba razširjata zmožnosti AI, a na temeljno različne načine. RAG omogoča modelu dostop do **znanja** z iskanjem dokumentov. Orodja pa modelu omogočajo izvajanje **dejanj** preko klicev funkcij.

<img src="../../../translated_images/sl/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Primerjava orodja in RAG" width="800"/>

*RAG pridobiva informacije iz statičnih dokumentov — Orodja izvajajo dejanja in pridobivajo dinamične, podatke v realnem času. Mnogi produktivni sistemi združujejo oba pristopa.*

V praksi mnogi produktivni sistemi kombinirajo oba pristopa: RAG za utemeljitev odgovorov v vaši dokumentaciji in Orodja za pridobivanje aktualnih podatkov ali izvajanje operacij.

## Naslednji koraki

**Naslednji modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigacija:** [← Prejšnji: Modul 03 - RAG](../03-rag/README.md) | [Nazaj na začetek](../README.md) | [Naprej: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Opozorilo:**  
Ta dokument je bil preveden s pomočjo storitve za prevajanje z umetno inteligenco [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas opozarjamo, da avtomatizirani prevodi lahko vsebujejo napake ali netočnosti. Izvirni dokument v izvorni jezikovni verziji velja kot avtoritativni vir. Za pomembne informacije priporočamo strokovni prevod, opravljen s strani človeškega prevajalca. Ne odgovarjamo za kakršnekoli nesporazume ali napačne interpretacije, ki bi nastale zaradi uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->