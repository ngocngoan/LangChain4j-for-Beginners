# Modul 04: AI agenti z orodji

## Kazalo

- [Video vodič](../../../04-tools)
- [Kaj se boste naučili](../../../04-tools)
- [Predpogoji](../../../04-tools)
- [Razumevanje AI agentov z orodji](../../../04-tools)
- [Kako deluje klic orodja](../../../04-tools)
  - [Definicije orodij](../../../04-tools)
  - [Sprejemanje odločitev](../../../04-tools)
  - [Izvedba](../../../04-tools)
  - [Generiranje odgovora](../../../04-tools)
  - [Arhitektura: Spring Boot samodejno povezovanje](../../../04-tools)
- [Verižna uporaba orodij](../../../04-tools)
- [Zaženite aplikacijo](../../../04-tools)
- [Uporaba aplikacije](../../../04-tools)
  - [Preizkusite preprosto uporabo orodij](../../../04-tools)
  - [Testirajte verižni klic orodij](../../../04-tools)
  - [Oglejte si potek pogovora](../../../04-tools)
  - [Eksperimentirajte z različnimi zahtevki](../../../04-tools)
- [Ključni pojmi](../../../04-tools)
  - [Vzorec ReAct (razmišljanje in delovanje)](../../../04-tools)
  - [Pomen opisov orodij](../../../04-tools)
  - [Upravljanje seje](../../../04-tools)
  - [Ravnanje z napakami](../../../04-tools)
- [Na voljo orodja](../../../04-tools)
- [Kdaj uporabljati agente na osnovi orodij](../../../04-tools)
- [Orodja proti RAG](../../../04-tools)
- [Naslednji koraki](../../../04-tools)

## Video vodič

Oglejte si to v živo sejo, ki pojasnjuje, kako začeti z modulom:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="AI agenti z orodji in MCP - v živo" width="800"/></a>

## Kaj se boste naučili

Doslej ste se naučili, kako voditi pogovore z AI, učinkovito strukturirati pozive in zakoreniniti odgovore v svojih dokumentih. Toda obstaja temeljna omejitev: jezikovni modeli lahko ustvarjajo le besedilo. Ne morejo preveriti vremena, izvesti izračunov, poizvedovati po bazah podatkov ali komunicirati z zunanjimi sistemi.

Orodja to spremenijo. Z omogočanjem modelu dostopa do funkcij, ki jih lahko kliče, ga spremenite iz generatorja besedila v agenta, ki lahko izvaja dejanja. Model odloča, kdaj potrebuje orodje, katero orodje uporabiti in katere parametre posredovati. Vaša koda izvede funkcijo in vrne rezultat. Model vključi ta rezultat v svoj odgovor.

## Predpogoji

- Dokončan [Modul 01 - Uvod](../01-introduction/README.md) (uvoženi Azure OpenAI viri)
- Priporočeno dokončanje prejšnjih modulov (ta modul naslavlja [RAG koncepte iz Modula 03](../03-rag/README.md) v primerjavi Orodij z RAG)
- Datoteka `.env` v korenskem imeniku z Azure poverilnicami (ustvarjena z `azd up` v Modulu 01)

> **Opomba:** Če Modul 01 še niste dokončali, sledite navodilom za uvod tam.

## Razumevanje AI agentov z orodji

> **📝 Opomba:** Izraz "agenti" v tem modulu pomeni AI asistente, izboljšane z zmožnostmi klica orodij. To se razlikuje od vzorcev **Agentic AI** (avtonomni agenti z načrtovanjem, spominom in večstopenjskim sklepanjem), ki jih bomo obravnavali v [Modulu 05: MCP](../05-mcp/README.md).

Brez orodij lahko jezikovni model ustvarja le besedilo iz svojih podatkov. Če ga vprašaš po trenutnem vremenu, mora ugibati. Če mu daš orodja, lahko kliče vremenski API, izvaja izračune ali poizveduje v bazi podatkov — nato pa te prave rezultate vključi v svoj odgovor.

<img src="../../../translated_images/sl/what-are-tools.724e468fc4de64da.webp" alt="Brez orodij proti z orodji" width="800"/>

*Brez orodij model le ugiba — z orodji kliče API-je, izvaja izračune in vrača podatke v realnem času.*

AI agent z orodji sledi vzorcu **Razmišljanja in Delovanja (ReAct)**. Model ne le odgovarja — razmišlja, kaj potrebuje, deluje tako, da kliče orodje, opazuje rezultat in nato odloči, ali bo ponovno ukrepal ali posredoval končni odgovor:

1. **Razmišljanje** — agent analizira uporabnikovo vprašanje in ugotovi, katere informacije so potrebne
2. **Delovanje** — agent izbere pravo orodje, pripravi pravilne parametre in ga pokliče
3. **Opazovanje** — agent prejme izhod orodja in oceni rezultat
4. **Ponovitev ali odgovor** — če je potrebnih več podatkov, agent ponovi zanko; sicer sestavi odgovor v naravnem jeziku

<img src="../../../translated_images/sl/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Vzorec ReAct" width="800"/>

*Cikel ReAct — agent razmišlja, kaj narediti, deluje s klicem orodja, opazuje rezultat in ponavlja, dokler ne poda končnega odgovora.*

To se zgodi samodejno. Vi definirate orodja in njihove opise. Model sam sprejema odločitve o tem, kdaj in kako jih uporabiti.

## Kako deluje klic orodja

### Definicije orodij

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Definirate funkcije z jasnimi opisi in specifikacijami parametrov. Model vidi te opise v sistemskem pozivu in razume, kaj vsako orodje počne.

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

// Pomočnik je samodejno povezan s Spring Boot z:
// - ChatModel bean
// - Vse metode @Tool iz razredov @Component
// - ChatMemoryProvider za upravljanje sej
```

Diagram spodaj razčleni vsak anotacijo in pokaže, kako vsak element pomaga AI razumeti, kdaj klicati orodje in katere argumente posredovati:

<img src="../../../translated_images/sl/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomija definicij orodij" width="800"/>

*Anatomija definicije orodja — @Tool pove AI, kdaj ga uporabiti, @P opisuje vsak parameter, @AiService pa na začetku vse poveže.*

> **🤖 Preizkusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) in vprašajte:
> - "Kako vključim pravi vremenski API kot OpenWeatherMap namesto vzorčnih podatkov?"
> - "Kaj naredi dober opis orodja, ki AI pomaga pravilno uporabiti orodje?"
> - "Kako obravnavam napake API-ja in omejitve hitrosti v izvedbah orodij?"

### Sprejemanje odločitev

Ko uporabnik vpraša "Kakšno je vreme v Seattle?", model ne izbira naključno orodja. Primerja uporabnikov namen z vsakim opisom orodja, ki mu je na voljo, oceni relevantnost in izbere najboljšo ujemanje. Nato ustvari strukturiran klic funkcije s pravimi parametri — v tem primeru nastavi `location` na `"Seattle"`.

Če nobeno orodje ne ustreza uporabnikovi zahtevi, model odgovori na podlagi svojega znanja. Če ustreza več orodij, izbere najbolj specifično.

<img src="../../../translated_images/sl/decision-making.409cd562e5cecc49.webp" alt="Kako AI odloča, katero orodje uporabiti" width="800"/>

*Model ovrednoti vsako razpoložljivo orodje glede na uporabnikov namen in izbere najboljše ujemanje — zato so jasni, specifični opisi orodij pomembni.*

### Izvedba

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot samodejno poveže deklarativni vmesnik `@AiService` z vsemi registriranimi orodji, LangChain4j pa samodejno izvaja klice orodij. V ozadju poteka celoten klic orodja skozi šest stopenj — od uporabnikovega vprašanja v naravnem jeziku vse do odgovora v naravnem jeziku:

<img src="../../../translated_images/sl/tool-calling-flow.8601941b0ca041e6.webp" alt="Potek klica orodja" width="800"/>

*Celoten potek — uporabnik postavi vprašanje, model izbere orodje, LangChain4j ga izvede, model pa vključi rezultat v naravni odgovor.*

Če ste v Modulu 00 zagnali [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java), ste to že videli v praksi — orodje `Calculator` so poklicali na enak način. Uporabniški diagram zaporedja spodaj prikazuje, kaj se je pravzaprav zgodilo med tem poskusom:

<img src="../../../translated_images/sl/tool-calling-sequence.94802f406ca26278.webp" alt="Diagram zaporedja klica orodja" width="800"/>

*Zanka klica orodja iz demoja Quick Start — `AiServices` pošlje vaše sporočilo in sheme orodij LLM‑ju, LLM odgovori s klicem funkcije, kot je `add(42, 58)`, LangChain4j lokalno izvede metodo `Calculator`, rezultat pa poda nazaj za končni odgovor.*

> **🤖 Preizkusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) in vprašajte:
> - "Kako deluje vzorec ReAct in zakaj je učinkovit za AI agente?"
> - "Kako agent odloča, katero orodje uporabiti in v kakšnem vrstnem redu?"
> - "Kaj se zgodi, če izvedba orodja ne uspe - kako naj robustno obravnavam napake?"

### Generiranje odgovora

Model prejme vremenske podatke in jih oblikuje v odgovor v naravnem jeziku za uporabnika.

### Arhitektura: Spring Boot samodejno povezovanje

Ta modul uporablja integracijo LangChain4j za Spring Boot s deklarativnimi vmesniki `@AiService`. Ob zagonu Spring Boot odkrije vsak `@Component`, ki vsebuje metode `@Tool`, vaš `ChatModel` bean in `ChatMemoryProvider` — nato vse skupaj poveže v en sam vmesnik `Assistant` brez odvečnega kode.

<img src="../../../translated_images/sl/spring-boot-wiring.151321795988b04e.webp" alt="Arhitektura samodejnega povezovanja Spring Boot" width="800"/>

*Vmesnik @AiService združuje ChatModel, komponente orodij in dobavitelja pomnilnika — Spring Boot samodejno poskrbi za povezovanje.*

Tu je celoten življenjski cikel zahtevka kot diagram zaporedja — od HTTP zahtevka prek kontrolerja, storitve in samodejno povezanega proxyja vse do izvedbe orodja in nazaj:

<img src="../../../translated_images/sl/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Zaporedje klica orodja v Spring Boot" width="800"/>

*Celoten življenjski cikel zahtevka Spring Boot — HTTP zahtevek teče skozi kontroler in storitev do samodejno povezanega proxyja Assistant, ki samodejno organizira klice LLM in orodij.*

Glavne prednosti tega pristopa:

- **Spring Boot samodejno povezovanje** — ChatModel in orodja se samodejno vstavijo
- **Vzorec @MemoryId** — Samodejno upravljanje seje na osnovi pomnilnika
- **En sam primerek** — Asistent se ustvari enkrat in ponovno uporabi za boljšo zmogljivost
- **Varnost tipa pri izvedbi** — Java metode se kličejo neposredno s pretvorbo tipov
- **Večkrožna orkestracija** — Samodejno obravnava verižni klic orodij
- **Brez odvečnih kod** — Ni ročnih klicev `AiServices.builder()` ali pomnilniških HashMap

Alternativni pristopi (ročni `AiServices.builder()`) zahtevajo več kode in ne nudijo prednosti integracije Spring Boot.

## Verižna uporaba orodij

**Verižna uporaba orodij** — Prava moč agentov na osnovi orodij se pokaže, ko eno vprašanje zahteva več orodij. Vprašajte "Kakšno je vreme v Seattle v Fahrenheitu?" in agent samodejno veriženo uporabi dve orodji: najprej kliče `getCurrentWeather` za temperaturo v Celziju, nato to vrednost posreduje `celsiusToFahrenheit` za pretvorbo — vse v enem pogovornem krogu.

<img src="../../../translated_images/sl/tool-chaining-example.538203e73d09dd82.webp" alt="Primer verižne uporabe orodij" width="800"/>

*Verižna uporaba orodij v akciji — agent najprej kliče getCurrentWeather, nato Celsius rezultat preusmeri v celsiusToFahrenheit in poda združen odgovor.*

**Vljudno obravnavanje napak** — Vprašajte za vreme v mestu, ki ni v vzorčnih podatkih. Orodje vrne sporočilo o napaki, AI pa pojasni, da ne more pomagati, namesto da bi aplikacija zatajila. Orodja varno obravnavajo napake. Diagram spodaj primerja oba pristopa — ob pravilnem ravnanju z napakami agent prestreže izjemo in razloži, medtem ko brez tega celotna aplikacija zruši:

<img src="../../../translated_images/sl/error-handling-flow.9a330ffc8ee0475c.webp" alt="Potek ravnanja z napakami" width="800"/>

*Ko orodje zataj, agent prestreže napako in odgovori s koristnim pojasnilom namesto z zrušitvijo.*

To se zgodi v enem krogu pogovora. Agent samostojno organizira več klicev orodij.

## Zaženite aplikacijo

**Preverite nameščanje:**

Prepričajte se, da datoteka `.env` obstaja v korenskem imeniku z Azure poverilnicami (ustvarjena v Modulu 01). Zaženite to iz imenika modula (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Naj pokaže AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Naj prikazuje AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Zaženite aplikacijo:**

> **Opomba:** Če ste že zagnali vse aplikacije z `./start-all.sh` iz korenskega imenika (kot opisano v Modulu 01), ta modul že teče na vratih 8084. Lahko preskočite ukaze za zagon spodaj in se neposredno povežete na http://localhost:8084.

**Možnost 1: Uporaba Spring Boot nadzorne plošče (priporočeno uporabnikom VS Code)**

Razvojni kontejner vsebuje razširitev Spring Boot nadzorne plošče, ki omogoča vizualno upravljanje vseh Spring Boot aplikacij. Najdete jo v vrstici z dejavnostmi na levi strani VS Code (poiščite ikono Spring Boot).

Iz Spring Boot nadzorne plošče lahko:
- Vidite vse razpoložljive Spring Boot aplikacije v delovnem prostoru
- Zaženete/ustavite aplikacije z enim klikom
- V realnem času spremljate dnevnike aplikacij
- Spremljate stanje aplikacij
Preprosto kliknite gumb za predvajanje ob "tools", da zaženete ta modul, ali pa zaženite vse module hkrati.

Tako izgleda Spring Boot nadzorna plošča v VS Code:

<img src="../../../translated_images/sl/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot nadzorna plošča v VS Code — začnite, ustavite in spremljajte vse module na enem mestu*

**Možnost 2: Uporaba shell skript**

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

Ali pa zaženite samo ta modul:

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

Obe skripti samodejno naložita spremenljivke okolja iz korenske datoteke `.env` in ustvarita JAR-je, če ti še ne obstajajo.

> **Opomba:** Če želite pred zagonom ročno zgraditi vse module:
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

Odprite http://localhost:8084 v svojem brskalniku.

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

Aplikacija omogoča spletni vmesnik, kjer lahko komunicirate z AI agentom, ki ima dostop do orodij za vreme in pretvorbo temperature. Tako izgleda vmesnik — vključuje hitre začetne primere in klepetalno ploščo za pošiljanje zahtev:

<a href="images/tools-homepage.png"><img src="../../../translated_images/sl/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Vmesnik AI agent orodij - hitri primeri in klepetalni vmesnik za interakcijo z orodji*

### Poskusite enostavno uporabo orodij

Začnite z enostavno zahtevo: "Pretvori 100 stopinj Fahrenheita v Celzija". Agent prepozna, da potrebuje orodje za pretvorbo temperature, ga pokliče z ustreznimi parametri in vrne rezultat. Opazite, kako naravno deluje — niste določili, katero orodje uporabiti ali kako ga klicati.

### Preizkusite povezanost orodij

Zdaj poskusite nekaj bolj kompleksnega: "Kakšno je vreme v Seattleu in pretvori v Fahrenheite?" Spremljajte, kako agent deluje v korakih. Najprej pridobi vreme (ki vrne Celzije), nato prepozna potrebo po pretvorbi v Fahrenheite, pokliče orodje za pretvorbo in združi oba rezultata v en odgovor.

### Oglejte si potek pogovora

Klepetalni vmesnik ohranja zgodovino pogovora, kar omogoča večkrožne interakcije. Vidite lahko vse prejšnje zahtevke in odgovore, kar olajša sledenje pogovoru in razumevanje, kako agent gradi kontekst skozi več izmenjav.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/sl/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Večkrožni pogovor, ki prikazuje preproste pretvorbe, vremenske poizvedbe in povezovanje orodij*

### Eksperimentirajte z različnimi zahtevki

Poskusite različne kombinacije:
- Poizvedbe o vremenu: "Kakšno je vreme v Tokiu?"
- Pretvorbe temperature: "Koliko je 25°C v kelvinih?"
- Kombinirane poizvedbe: "Preveri vreme v Parizu in povej, če je nad 20°C"

Opazite, kako agent razlaga naravni jezik in ga preslika v ustrezne klice orodij.

## Ključni pojmi

### ReAct vzorec (razmišljanje in delovanje)

Agent izmenično razmišlja (odloča, kaj storiti) in deluje (uporablja orodja). Ta vzorec omogoča avtonomno reševanje problemov namesto zgolj odgovarjanja na ukaze.

### Opisi orodij so pomembni

Kakovost opisov vaših orodij neposredno vpliva na to, kako dobro jih agent uporablja. Jasni in specifični opisi pomagajo modelu razumeti, kdaj in kako klicati posamezno orodje.

### Upravljanje seje

Označba `@MemoryId` omogoča avtomatsko upravljanje s pomnilnikom na osnovi seje. Vsak ID seje dobi svojo instanco `ChatMemory`, ki jo upravlja `ChatMemoryProvider` bean, tako da lahko več uporabnikov hkrati sodeluje z agentom brez mešanja njihovih pogovorov. Spodnji diagram prikazuje, kako so več uporabnikov usmerjeni v izolirane shrambe pomnilnika glede na njihove ID-je sej:

<img src="../../../translated_images/sl/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Vsak ID seje ustreza izolirani zgodovini pogovora — uporabniki ne vidijo sporočil drug drugega.*

### Obdelava napak

Orodja lahko odpovejo — API-ji potečejo, parametri so lahko neveljavni, zunanji servisi so nedostopni. Produkcijski agenti potrebujejo obdelavo napak, da model lahko pojasni težave ali poskusi alternative namesto, da bi aplikacija absolutno padla. Ko orodje vrže izjemo, jo LangChain4j ujame in sporočilo o napaki prenese nazaj modelu, ki lahko nato v naravnem jeziku pojasni problem.

## Razpoložljiva orodja

Spodnji diagram prikazuje širok ekosistem orodij, ki jih lahko ustvarite. Ta modul prikazuje vremenska in temperaturna orodja, a isti vzorec `@Tool` deluje za katerokoli Java metodo — od poizvedb po podatkovnih bazah do obdelave plačil.

<img src="../../../translated_images/sl/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Katera koli Java metoda z oznako @Tool postane na voljo za AI — vzorec se razteza na baze podatkov, API-je, e-pošto, datotečne operacije in še več.*

## Kdaj uporabiti agente, ki temeljijo na orodjih

Ne vsaka zahteva potrebuje orodja. Odločitev je odvisna od tega, ali AI rabi interakcijo z zunanjimi sistemi ali lahko odgovori iz lastnega znanja. Spodnji vodič povzema, kdaj orodja dodajo vrednost in kdaj niso potrebna:

<img src="../../../translated_images/sl/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*Hiter vodič za odločanje — orodja so za podatke v realnem času, izračune in dejanja; splošno znanje in ustvarjalne naloge jih ne potrebujejo.*

## Orodja proti RAG

Moduli 03 in 04 oba razširjata zmožnosti AI, a na povsem drugačne načine. RAG modelu omogoča dostop do **znanja** z iskanjem dokumentov. Orodja modelu omogočajo izvajanje **dejanj** z klici funkcij. Spodnji diagram primerja ti dve pristopi vzporedno — od načina delovanja do kompromisov med njima:

<img src="../../../translated_images/sl/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG pridobiva informacije iz statičnih dokumentov — orodja izvajajo dejanja in pridobivajo dinamične, aktualne podatke. Veliko produkcijskih sistemov združuje obe.*

V praksi mnogi produkcijski sistemi združujejo oba pristopa: RAG za podlago odgovorov v vaši dokumentaciji in Orodja za pridobivanje živih podatkov ali izvajanje operacij.

## Naslednji koraki

**Naslednji modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigacija:** [← Prejšnji: Modul 03 - RAG](../03-rag/README.md) | [Nazaj na začetek](../README.md) | [Naslednji: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo storitve za prevajanje z umetno inteligenco [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, upoštevajte, da lahko avtomatizirani prevodi vsebujejo napake ali netočnosti. Izvirni dokument v njegovem izvornem jeziku velja za avtoritativni vir. Za ključne informacije priporočamo profesionalni človeški prevod. Ne odgovarjamo za morebitna nesporazume ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->