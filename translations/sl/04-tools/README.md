# Modul 04: AI agenti z orodji

## Kazalo vsebine

- [Kaj se boste naučili](../../../04-tools)
- [Predpogoji](../../../04-tools)
- [Razumevanje AI agentov z orodji](../../../04-tools)
- [Kako deluje klic orodja](../../../04-tools)
  - [Definicije orodij](../../../04-tools)
  - [Sprejemanje odločitev](../../../04-tools)
  - [Izvedba](../../../04-tools)
  - [Generiranje odziva](../../../04-tools)
  - [Arhitektura: samodejna povezava Spring Boot](../../../04-tools)
- [Verižna raba orodij](../../../04-tools)
- [Zagon aplikacije](../../../04-tools)
- [Uporaba aplikacije](../../../04-tools)
  - [Poskusite preprosto uporabo orodja](../../../04-tools)
  - [Preizkusite verižna orodja](../../../04-tools)
  - [Oglejte si potek pogovora](../../../04-tools)
  - [Eksperimentirajte z različnimi zahtevki](../../../04-tools)
- [Ključni pojmi](../../../04-tools)
  - [Vzorec ReAct (premišljanje in delovanje)](../../../04-tools)
  - [Opis orodij je pomemben](../../../04-tools)
  - [Upravljanje sej](../../../04-tools)
  - [Ravnanje z napakami](../../../04-tools)
- [Razpoložljiva orodja](../../../04-tools)
- [Kdaj uporabiti agente, ki temeljijo na orodjih](../../../04-tools)
- [Orodja proti RAG](../../../04-tools)
- [Naslednji koraki](../../../04-tools)

## Kaj se boste naučili

Do zdaj ste se naučili, kako se pogovarjati z AI, učinkovito strukturirati pozive in podpreti odgovore z vašimi dokumenti. Vendar pa obstaja temeljna omejitev: jezikovni modeli lahko generirajo le besedilo. Ne morejo preveriti vremena, izvajati izračunov, poizvedovati v podatkovnih bazah ali sodelovati z zunanjimi sistemi.

Orodja to spremenijo. Z omogočanjem dostopa modelu do funkcij, ki jih lahko kliče, ga spremenite iz generatorja besedila v agenta, ki lahko izvaja dejanja. Model odloča, kdaj potrebuje orodje, katerega orodje uporabiti in katere parametre posredovati. Vaša koda izvede funkcijo in vrne rezultat. Model vključuje ta rezultat v svoj odgovor.

## Predpogoji

- Zaključen [Modul 01 - Uvod](../01-introduction/README.md) (nameščeni Azure OpenAI viri)
- Priporočeni zaključek prejšnjih modulov (ta modul se v primerjavi orodij in RAG sklicuje na [RAG pojme iz Modula 03](../03-rag/README.md))
- Datoteka `.env` v korenski mapi z Azure poverilnicami (ustvarjena z `azd up` v Modulu 01)

> **Opomba:** Če Modula 01 niste zaključili, najprej sledite navodilom za namestitev tam.

## Razumevanje AI agentov z orodji

> **📝 Opomba:** V tem modulu izraz "agenti" označuje AI pomočnike, izboljšane z zmogljivostmi klicanja orodij. To se razlikuje od vzorcev **Agentic AI** (avtonomni agenti z načrtovanjem, spominom in večstopenjskim premišljevanjem), ki jih bomo obravnavali v [Modulu 05: MCP](../05-mcp/README.md).

Brez orodij lahko jezikovni model generira le besedilo iz svojih učnih podatkov. Če ga vprašate za trenutno vreme, mora ugibati. Če mu daste orodja, lahko kliče vremenski API, izvaja izračune ali poizveduje v bazi podatkov — nato vplete te resnične rezultate v svoj odgovor.

<img src="../../../translated_images/sl/what-are-tools.724e468fc4de64da.webp" alt="Brez orodij proti z orodji" width="800"/>

*Brez orodij model samo ugiba — z orodji lahko kliče API-je, izvaja izračune in vrača podatke v realnem času.*

AI agent z orodji sledi vzorcu **Reasoning and Acting (ReAct)**. Model se ne odzove le — razmišlja, kaj potrebuje, deluje s klicem orodja, opazuje rezultat in nato odloči, ali naj ponovno deluje ali poda končni odgovor:

1. **Premisli** — agent analizira uporabnikovo vprašanje in ugotovi, katere informacije potrebuje
2. **Ustvari dejanje** — agent izbere pravo orodje, ustvari pravilne parametre in ga kliče
3. **Opazuj** — agent prejme izhod orodja in oceni rezultat
4. **Ponovi ali odgovori** — če je potrebnih več podatkov, se agent vrne na začetek; sicer sestavi naravnavplovno besedilo kot odgovor

<img src="../../../translated_images/sl/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Vzorec ReAct" width="800"/>

*Cikel ReAct — agent premišljuje o dejanju, deluje s klicem orodja, opazuje rezultat in ponavlja, dokler ne poda končnega odgovora.*

To se zgodi samodejno. Določite orodja in njihove opise. Model sam sprejema odločitve, kdaj in kako jih uporabiti.

## Kako deluje klic orodja

### Definicije orodij

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Definirate funkcije s jasnimi opisi in specifikacijami parametrov. Model vidi te opise v svojem sistemskem pozivu in razume, kaj vsako orodje počne.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Vaša logika iskanja vremena
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Asistent je samodejno povezan s Spring Boot z:
// - ChatModel bean
// - Vse metode @Tool iz razredov @Component
// - ChatMemoryProvider za upravljanje sej
```

Diagram spodaj razčleni vsako oznako in pokaže, kako vsak del pomaga AI razumeti, kdaj naj kliče orodje in katere argumente posredovati:

<img src="../../../translated_images/sl/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomija definicij orodij" width="800"/>

*Anatomija definicije orodja — @Tool pove AI, kdaj ga uporabiti, @P opisuje vsak parameter, @AiService pa poveže vse skupaj ob zagonu.*

> **🤖 Poskusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) in vprašajte:
> - "Kako bi integriral pravi vremenski API, kot je OpenWeatherMap, namesto lažnih podatkov?"
> - "Kaj naredi dober opis orodja, ki AI pomaga pravilno uporabljati orodje?"
> - "Kako naj obravnavam napake API-ja in omejitve klicev v implementacijah orodij?"

### Sprejemanje odločitev

Ko uporabnik vpraša "Kakšno je vreme v Seattleu?", model ne izbere orodja naključno. Primerja uporabnikov namen z opisi vsakega orodja, jim dodeli ocene ustreznosti in izbere najboljšo možnost. Nato generira strukturiran klic funkcije s pravimi parametri — v tem primeru nastavi `location` na `"Seattle"`.

Če nobeno orodje ne ustreza uporabnikovi zahtevi, model odgovori iz lastnega znanja. Če več orodij ustreza, izbere najnatančnejše.

<img src="../../../translated_images/sl/decision-making.409cd562e5cecc49.webp" alt="Kako AI odloča, katero orodje uporabiti" width="800"/>

*Model oceni vsako razpoložljivo orodje glede na uporabnikov namen in izbere najboljšo ujemanje — zato je pomembno pisati jasne, specifične opise orodij.*

### Izvedba

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot samodejno poveže deklarativni vmesnik `@AiService` z vsemi registriranimi orodji, LangChain4j pa avtomatično izvaja klice orodij. Za kulisami poteka celoten klic orodja skozi šest stopenj — od uporabnikovega vprašanja v naravnem jeziku vse do odgovora v naravnem jeziku:

<img src="../../../translated_images/sl/tool-calling-flow.8601941b0ca041e6.webp" alt="Potek klica orodja" width="800"/>

*Celoten potek — uporabnik postavi vprašanje, model izbere orodje, LangChain4j ga izvede, model vplete rezultat v naravni odgovor.*

> **🤖 Poskusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) in vprašajte:
> - "Kako deluje vzorec ReAct in zakaj je učinkovit za AI agente?"
> - "Kako agent odloča, katero orodje uporabiti in v kakšnem vrstnem redu?"
> - "Kaj se zgodi, če izvedba orodja ne uspe - kako naj robustno obravnavam napake?"

### Generiranje odziva

Model prejme vremenske podatke in jih oblikuje v naravnaven odgovor za uporabnika.

### Arhitektura: samodejna povezava Spring Boot

Ta modul uporablja LangChain4j integracijo s Spring Boot z deklarativnimi vmesniki `@AiService`. Ob zagonu Spring Boot odkrije vsak `@Component` z metodami `@Tool`, vaš `ChatModel` bean in `ChatMemoryProvider` — nato jih vse poveže v en vmesnik `Assistant` brez dodatnega ročnega nastavljanja.

<img src="../../../translated_images/sl/spring-boot-wiring.151321795988b04e.webp" alt="Arhitektura samodejne povezave Spring Boot" width="800"/>

*Vmesnik @AiService povezuje ChatModel, komponente orodij in upravljavca pomnilnika — povezavo Spring Boot samodejno upravlja.*

Ključne prednosti te metode:

- **Samodejna povezava Spring Boot** — ChatModel in orodja samodejno injicirani
- **Vzorec @MemoryId** — samodejno upravljanje sejnskega pomnilnika
- **En sam primerek** — Assistant ustvarjen enkrat in ponovno uporabljen za boljšo zmogljivost
- **Tipno varna izvedba** — Java metode klicane neposredno s pretvorbo tipov
- **Večkrsna orkestracija** — samodejno upravlja verižne klice orodij
- **Brez nepotrebne kode** — ni ročnih klicev `AiServices.builder()` ali pomnilniških HashMap

Alternativni pristopi (ročni `AiServices.builder()`) zahtevajo več kode in ne ponujajo prednosti integracije Spring Boot.

## Verižna raba orodij

**Verižna raba orodij** — Pravi potencial agentov, ki temeljijo na orodjih, se pokaže, ko je za eno vprašanje potrebno več orodij. Če vprašate "Kakšno je vreme v Seattleu v Fahrenheitih?", agent samodejno združi dve orodji: najprej kliče `getCurrentWeather`, da dobi temperaturo v Celziju, nato pa ta rezultat posreduje funkciji `celsiusToFahrenheit` za pretvorbo — vse v enem samem pogovornem koraku.

<img src="../../../translated_images/sl/tool-chaining-example.538203e73d09dd82.webp" alt="Primer verižne rabe orodij" width="800"/>

*Verižna raba orodij v praksi — agent najprej kliče getCurrentWeather, nato pretvori rezultat Celzija v Fahrenheita in poda združen odgovor.*

**Nenehne napake** — Če vprašate za vreme v mestu, ki ni v lažnih podatkih, orodje vrne sporočilo o napaki, AI pa pojasni, da ne more pomagati in ne zruši aplikacije. Orodja se varno izognejo napakam. Diagram spodaj primerja obe pristopi — ob pravilnem ravnanju z napako agent ujame izjemo in poda koristno pojasnilo, sicer pa se aplikacija popolnoma zruši:

<img src="../../../translated_images/sl/error-handling-flow.9a330ffc8ee0475c.webp" alt="Potek ravnanja z napakami" width="800"/>

*Ko orodje ne uspe, agent ujame napako in poda koristno pojasnilo namesto zrušitve.*

Kazanje se zgodi v enem pogovornem koraku. Agent samostojno orkestrira več orodnih klicev.

## Zagon aplikacije

**Preverite namestitev:**

Preverite, da je datoteka `.env` v korenski mapi s poverilnicami Azure (ustvarjena v Modulu 01). Zaženite iz mape modula (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Prikazati mora AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Mora prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Zaženite aplikacijo:**

> **Opomba:** Če ste že zagnali vse aplikacije z `./start-all.sh` iz korenske mape (kot opisano v Modulu 01), ta modul že teče na vratih 8084. Ukaze za zagon lahko preskočite in pojdite neposredno na http://localhost:8084.

**Možnost 1: Uporaba Spring Boot nadzorne plošče (priporočeno uporabnikom VS Code)**

Razvojno okolje vključuje razširitev Spring Boot nadzorne plošče, ki nudi vizualni vmesnik za upravljanje vseh Spring Boot aplikacij. Najdete jo na stranski vrstici aktivnosti na levi strani v VS Code (ikona Spring Boot).

Iz nadzorne plošče lahko:
- Vidite vse razpoložljive Spring Boot aplikacije v delovnem prostoru
- Zaženete/ustavite aplikacije z enim klikom
- Ogledujete dnevniške zapise v realnem času
- Spremljate stanje aplikacij

Preprosto kliknite gumb za predvajanje poleg "tools" za zagon tega modula ali zaženite vse module naenkrat.

Tako izgleda Spring Boot nadzorna plošča v VS Code:

<img src="../../../translated_images/sl/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot nadzorna plošča" width="400"/>

*Spring Boot nadzorna plošča v VS Code — zagon, ustavitev in spremljanje vseh modulov na enem mestu*

**Možnost 2: Uporaba skript za ukazno vrstico**

Zaženite vse spletne aplikacije (moduli 01-04):

**Bash:**
```bash
cd ..  # Iz korenskega imenika
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iz korenskega imenika
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

Obe skripti samodejno naložita okoljske spremenljivke iz korenske `.env` datoteke in po potrebi sestavita JAR datoteke.

> **Opomba:** Če želite module zgraditi ročno pred zagon:
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

Odprite http://localhost:8084 v vašem brskalniku.

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

Aplikacija ponuja spletni vmesnik, kjer lahko komunicirate z AI agentom z dostopom do vremenskih in orodij za pretvorbo temperature. Tako izgleda vmesnik — vključuje primere za hiter začetek in klepetalno ploščo za pošiljanje zahtevkov:
<a href="images/tools-homepage.png"><img src="../../../translated_images/sl/tools-homepage.4b4cd8b2717f9621.webp" alt="Vmesnik orodij AI agenta" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Vmesnik orodij AI agenta - hitri primeri in klepetalni vmesnik za interakcijo z orodji*

### Preizkusite preprosto uporabo orodja

Začnite s preprostim zahtevkom: "Pretvori 100 stopinj Fahrenheita v Celzijev". Agent prepozna, da potrebuje orodje za pretvorbo temperature, ga pokliče z ustreznimi parametri in vrne rezultat. Opazite, kako naravno je to - niste določili, katero orodje uporabiti ali kako ga poklicati.

### Preizkusite verižitev orodij

Sedaj poskusite nekaj bolj zapletenega: "Kakšno je vreme v Seattlu in pretvori v Fahrenheite?" Oglejte si, kako agent ta postopek opravi v korakih. Najprej pridobi vreme (ki vrne v Celziju), prepozna potrebo po pretvorbi v Fahrenheite, pokliče orodje za pretvorbo in združi oba rezultata v en odgovor.

### Oglejte si potek pogovora

Klepetalni vmesnik ohranja zgodovino pogovora, kar vam omogoča večkrožne interakcije. Vidite lahko vse prejšnje poizvedbe in odgovore, kar olajša sledenje pogovoru in razumevanje, kako agent gradi kontekst skozi več izmenjav.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/sl/tools-conversation-demo.89f2ce9676080f59.webp" alt="Pogovor z večkratnimi klici orodij" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Večkratni pogovor, ki prikazuje preproste pretvorbe, vremenske poizvedbe in verižitev orodij*

### Eksperimentirajte z različnimi zahtevki

Preizkusite različne kombinacije:
- Vremenske poizvedbe: "Kakšno je vreme v Tokiu?"
- Pretvorbe temperatur: "Koliko je 25°C v Kelvinih?"
- Združene poizvedbe: "Preveri vreme v Parizu in mi povej, če je nad 20°C"

Opazite, kako agent interpretira naravni jezik in ga preslika v ustrezne klice orodij.

## Ključni koncepti

### Vzorec ReAct (Razmišljanje in izvajanje)

Agent izmenično razmišlja (odloča, kaj storiti) in deluje (uporablja orodja). Ta vzorec omogoča avtonomno reševanje problemov namesto le odzivanja na ukaze.

### Opisi orodij so pomembni

Kakovost opisov vaših orodij neposredno vpliva na to, kako dobro jih agent uporablja. Jasni, specifični opisi pomagajo modelu razumeti, kdaj in kako poklicati posamezno orodje.

### Upravljanje sej

Označba `@MemoryId` omogoča samodejno upravljanje spomina na podlagi sej. Vsak ID seje dobi svojo instanco `ChatMemory`, ki jo upravlja `ChatMemoryProvider` bean, tako da lahko več uporabnikov istočasno komunicira z agentom brez prepletanja pogovorov. Spodnji diagram prikazuje, kako so uporabniki usmerjeni v izolirane shrambe spominov na podlagi svojih ID sej:

<img src="../../../translated_images/sl/session-management.91ad819c6c89c400.webp" alt="Upravljanje sej z @MemoryId" width="800"/>

*Vsak ID seje se preslika na izolirano zgodovino pogovorov — uporabniki nikoli ne vidijo sporočil drug drugega.*

### Ravnanje z napakami

Orodja lahko spodletijo — API-ji potečejo, parametri so morda neveljavni, zunanji servisi odpovedo. Produkcijski agenti potrebujejo ravnanje z napakami, da model lahko pojasni probleme ali poskusi alternative namesto da aplikacija pade. Ko orodje sproži izjemo, LangChain4j jo ujame in sporočilo o napaki posreduje nazaj modelu, ki lahko nato problem pojasni v naravnem jeziku.

## Razpoložljiva orodja

Spodnji diagram prikazuje širok ekosistem orodij, ki jih lahko razvijete. Ta modul prikazuje vremenska in temperaturna orodja, a isti vzorec `@Tool` deluje za katerokoli Java metodo — od poizvedb v podatkovnih bazah do procesiranja plačil.

<img src="../../../translated_images/sl/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ekosistem orodij" width="800"/>

*Katera koli Java metoda, označena z @Tool, je na voljo za AI — vzorec se razteza na baze podatkov, API-je, e-pošto, operacije s datotekami in še več.*

## Kdaj uporabljati agente na osnovi orodij

Za vsak zahtevek niso potrebna orodja. Odločitev je odvisna od tega, ali AI potrebuje interakcijo z zunanjimi sistemi ali lahko odgovori iz lastnega znanja. Spodnji vodič povzema, kdaj orodja koristijo in kdaj niso potrebna:

<img src="../../../translated_images/sl/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Kdaj uporabljati orodja" width="800"/>

*Hiter vodič za odločanje — orodja so za podatke v realnem času, izračune in akcije; splošno znanje in ustvarjalne naloge jih ne potrebujejo.*

## Orodja proti RAG

Modula 03 in 04 oba širita zmožnosti AI, a na povsem različne načine. RAG modelu omogoča dostop do **znanja** s pridobivanjem dokumentov. Orodja modelu omogočajo izvajanje **dejanj** s klici funkcij. Spodnji diagram primerja ti dve pristopi vzporedno — od načinov delovanja do kompromisov med njima:

<img src="../../../translated_images/sl/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Primerjava orodij in RAG" width="800"/>

*RAG pridobiva informacije iz statičnih dokumentov — orodja izvajajo dejanja in pridobivajo dinamične, aktualne podatke. Mnogi produkcijski sistemi združujejo oba.*

V praksi mnogi produkcijski sistemi kombinirajo oba pristopa: RAG za utemeljitev odgovorov v dokumentaciji ter Orodja za pridobitev živih podatkov ali izvajanje operacij.

## Naslednji koraki

**Naslednji modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigacija:** [← Prejšnji: Modul 03 - RAG](../03-rag/README.md) | [Nazaj na glavno](../README.md) | [Naprej: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo storitve za avtomatski prevod [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, upoštevajte, da lahko avtomatski prevodi vsebujejo napake ali netočnosti. Izvirni dokument v izvirnem jeziku velja za avtoritativni vir. Za kritične informacije priporočamo strokovni prevod s strani človeka. Nismo odgovorni za morebitna nesporazumevanja ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->