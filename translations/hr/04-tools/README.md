# Modul 04: AI Agenti s Alatima

## Sadržaj

- [Video vodič](../../../04-tools)
- [Što ćete naučiti](../../../04-tools)
- [Preduvjeti](../../../04-tools)
- [Razumijevanje AI agenata s alatima](../../../04-tools)
- [Kako funkcionira pozivanje alata](../../../04-tools)
  - [Definicije alata](../../../04-tools)
  - [Donosenje odluka](../../../04-tools)
  - [Izvršenje](../../../04-tools)
  - [Generiranje odgovora](../../../04-tools)
  - [Arhitektura: Spring Boot automatsko povezivanje](../../../04-tools)
- [Lanca alata](../../../04-tools)
- [Pokrenite aplikaciju](../../../04-tools)
- [Korištenje aplikacije](../../../04-tools)
  - [Isprobajte jednostavnu uporabu alata](../../../04-tools)
  - [Testirajte povezivanje alata](../../../04-tools)
  - [Pogledajte tijek razgovora](../../../04-tools)
  - [Eksperimentirajte s različitim upitima](../../../04-tools)
- [Ključni pojmovi](../../../04-tools)
  - [ReAct obrazac (Razmišljanje i djelovanje)](../../../04-tools)
  - [Opis alata je važan](../../../04-tools)
  - [Upravljanje sesijama](../../../04-tools)
  - [Rukovanje greškama](../../../04-tools)
- [Dostupni alati](../../../04-tools)
- [Kada koristiti agente temeljene na alatima](../../../04-tools)
- [Alati vs RAG](../../../04-tools)
- [Sljedeći koraci](../../../04-tools)

## Video vodič

Pogledajte ovu sesiju uživo koja objašnjava kako započeti s ovim modulom:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="AI Agents with Tools and MCP - Live Session" width="800"/></a>

## Što ćete naučiti

Do sada ste naučili kako voditi razgovore s AI, učinkovito strukturirati upite i povezivati odgovore s vašim dokumentima. No postoji osnovno ograničenje: modeli jezika mogu generirati samo tekst. Ne mogu provjeriti vremensku prognozu, izvršavati izračune, pretraživati baze podataka ili komunicirati s vanjskim sustavima.

Alati to mijenjaju. Dajući modelu pristup funkcijama koje može pozvati, pretvarate ga iz generatora teksta u agenta koji može poduzimati akcije. Model odlučuje kada mu treba alat, koji alat koristiti i koje parametre proslijediti. Vaš kod izvršava funkciju i vraća rezultat. Model zatim uklapa taj rezultat u svoj odgovor.

## Preduvjeti

- Završili [Modul 01 - Uvod](../01-introduction/README.md) (postavljeni Azure OpenAI resursi)
- Preporučeno završiti prethodne module (ovaj modul se poziva na [RAG koncepte iz Modula 03](../03-rag/README.md) u usporedbi Alati vs RAG)
- `.env` datoteka u korijenskom direktoriju s Azure vjerodajnicama (kreirana pomoću `azd up` u Modulu 01)

> **Napomena:** Ako niste završili Modul 01, slijedite tamo upute za postavljanje prije nastavka.

## Razumijevanje AI agenata s alatima

> **📝 Napomena:** Termin „agenti“ u ovom modulu odnosi se na AI asistente proširene mogućnošću pozivanja alata. Ovo je različito od **Agentic AI** obrazaca (autonomni agenti s planiranjem, memorijom i višekorak razmišljanjem) koje ćemo obraditi u [Modul 05: MCP](../05-mcp/README.md).

Bez alata, model jezika može samo generirati tekst iz svojih podataka za treniranje. Pitajte ga za vremensku prognozu i on može samo nagađati. Dajte mu alate i može pozvati vremenski API, izvesti izračune ili pretražiti bazu podataka — zatim uklopiti te stvarne rezultate u svoj odgovor.

<img src="../../../translated_images/hr/what-are-tools.724e468fc4de64da.webp" alt="Bez alata vs S alatima" width="800"/>

*Bez alata model samo nagađa — s alatima može pozivati API-je, izvršavati izračune i vraćati podatke u stvarnom vremenu.*

AI agent s alatima slijedi obrazac **Razmišljanje i djelovanje (ReAct)**. Model ne samo da odgovara — razmišlja što mu treba, djeluje pozivajući alat, promatra rezultat, a zatim odlučuje hoće li opet djelovati ili dati konačni odgovor:

1. **Razmišlja** — agent analizira korisnikovo pitanje i odlučuje koju informaciju treba
2. **Djeluje** — agent odabire pravi alat, generira točne parametre i poziva ga
3. **Promatra** — agent prima izlaz alata i evaluira rezultat
4. **Ponavlja ili odgovara** — ako je potrebno više podataka, agent počinje ispočetka; inače sastavlja odgovor na prirodnom jeziku

<img src="../../../translated_images/hr/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct obrazac" width="800"/>

*Ciklus ReAct — agent razmišlja što učiniti, djeluje pozivajući alat, promatra rezultat i ponavlja dok ne može dati konačni odgovor.*

Ovo se odvija automatski. Definirate alate i njihove opise. Model donosi odluke o tome kada i kako ih koristiti.

## Kako funkcionira pozivanje alata

### Definicije alata

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Definirate funkcije s jasnim opisima i specifikacijama parametara. Model vidi te opise u svom sistemskom promptu i razumije što svaki alat radi.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Vaša logika pretrage vremena
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Asistent je automatski povezan preko Spring Boot sa:
// - ChatModel bean
// - Sve @Tool metode iz @Component klasa
// - ChatMemoryProvider za upravljanje sesijama
```

Dijagram ispod detaljno objašnjava svaku oznaku i pokazuje kako svaki dio pomaže AI-u razumjeti kada pozvati alat i koje argumente proslijediti:

<img src="../../../translated_images/hr/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomija definicija alata" width="800"/>

*Anatomija definicije alata — @Tool govori AI-u kada ga koristiti, @P opisuje svaki parametar, a @AiService povezuje sve zajedno pri pokretanju.*

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) i pitajte:
> - "Kako bih integrirao pravi vremenski API poput OpenWeatherMap umjesto lažnih podataka?"
> - "Što čini dobar opis alata koji pomaže AI-u da ga ispravno koristi?"
> - "Kako rukovati API greškama i ograničenjima učestalosti u implementacijama alata?"

### Donošenje odluka

Kad korisnik pita "Kakvo je vrijeme u Seattleu?", model nasumično ne bira alat. On uspoređuje korisnikovu namjeru sa svim opisima alata kojima ima pristup, ocjenjuje relevantnost svakog i odabire najbolju podudarnost. Zatim generira strukturirani poziv funkcije s pravim parametrima — u ovom slučaju postavljajući `location` na `"Seattle"`.

Ako nijedan alat ne odgovara korisnikovom zahtjevu, model se vraća odgovoru iz vlastitog znanja. Ako ih više odgovara, bira najtočniji.

<img src="../../../translated_images/hr/decision-making.409cd562e5cecc49.webp" alt="Kako AI odlučuje koji alat koristiti" width="800"/>

*Model evaluira svaki dostupni alat u odnosu na namjeru korisnika i odabire najbolju opciju — zato je pisanje jasnih i specifičnih opisa alata važno.*

### Izvršenje

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot automatski povezuje deklarativni `@AiService` sučelje sa svim registriranim alatima, a LangChain4j automatski izvršava pozive alata. Iza scene, kompletan poziv alata prolazi kroz šest faza — od korisnikovog pitanja na prirodnom jeziku sve do odgovora na prirodnom jeziku:

<img src="../../../translated_images/hr/tool-calling-flow.8601941b0ca041e6.webp" alt="Tijek poziva alata" width="800"/>

*Cjelokupni tijek — korisnik postavi pitanje, model odabere alat, LangChain4j ga izvrši, a model uklopi rezultat u prirodni odgovor.*

Ako ste pokrenuli [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) u Modulu 00, već ste vidjeli ovaj obrazac u praksi — alati `Calculator` su pozivani na isti način. Sljedeći sekvencijski dijagram prikazuje što se točno dogodilo tijekom tog demo primjera:

<img src="../../../translated_images/hr/tool-calling-sequence.94802f406ca26278.webp" alt="Sekvencijski dijagram poziva alata" width="800"/>

*Petlja poziva alata iz Quick Start demo primjera — `AiServices` šalje vašu poruku i šeme alata LLM-u, LLM odgovara pozivom funkcije poput `add(42, 58)`, LangChain4j lokalno izvršava metodu `Calculator`, i vraća rezultat za konačni odgovor.*

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) i pitajte:
> - "Kako funkcionira ReAct obrazac i zašto je efikasan za AI agente?"
> - "Kako agent odlučuje koji alat koristiti i kojim redoslijedom?"
> - "Što se događa ako izvršenje alata ne uspije - kako trebam robusno rukovati greškama?"

### Generiranje odgovora

Model prima vremenske podatke i formatira ih u odgovor na prirodnom jeziku za korisnika.

### Arhitektura: Spring Boot automatsko povezivanje

Ovaj modul koristi LangChain4j integraciju za Spring Boot s deklarativnim `@AiService` sučeljima. Pri pokretanju Spring Boot otkriva svaki `@Component` koji sadrži `@Tool` metode, vaš `ChatModel` instancu i `ChatMemoryProvider` — te ih sve povezuje u jedno `Assistant` sučelje bez dodatnog koda.

<img src="../../../translated_images/hr/spring-boot-wiring.151321795988b04e.webp" alt="Arhitektura automatskog povezivanja Spring Boot" width="800"/>

*Sučelje @AiService povezuje ChatModel, komponente alata i davatelja memorije — Spring Boot automatski upravlja povezivanjem.*

Evo potpunog životnog ciklusa zahtjeva kao sekvencijskog dijagrama — od HTTP zahtjeva preko kontrolera, servisa i proxy-ja s automatskim povezivanjem, sve do izvršenja alata i natrag:

<img src="../../../translated_images/hr/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Sekvenca poziva alata u Spring Bootu" width="800"/>

*Cjelokupan životni ciklus zahtjeva u Spring Bootu — HTTP zahtjev prolazi kroz kontroler i servis do automatski povezanog proxy-ja Assistant-a, koji orkestrira LLM i pozive alata.*

Ključne prednosti ovog pristupa:

- **Spring Boot automatsko povezivanje** — ChatModel i alati automatski injektirani
- **@MemoryId obrazac** — Automatsko upravljanje memorijom temeljeno na sesiji
- **Pojedinačna instanca** — Assistant kreiran jednom i ponovno korišten radi bolje izvedbe
- **Izvršenje tipa sigurno za tipove** — Java metode pozvane izravno s konverzijom tipova
- **Orkestracija više koraka** — Automatska podrška za povezivanje alata u lanac
- **Nula dodatnog koda** — Nema ručnih `AiServices.builder()` poziva ili memorijskih HashMapa

Alternativni pristupi (ručni `AiServices.builder()`) zahtijevaju više koda i nemaju prednosti Spring Boot integracije.

## Povezivanje alata

**Povezivanje alata** — Prava snaga agenata temeljena na alatima dolazi do izražaja kada jedno pitanje zahtijeva više alata. Pitajte: "Kakvo je vrijeme u Seattleu u Fahrenheitu?" i agent automatski povezuje dva alata: prvo poziva `getCurrentWeather` da dobije temperaturu u Celzijusima, zatim tu vrijednost prosljeđuje u `celsiusToFahrenheit` za konverziju — sve u jednom krugu razgovora.

<img src="../../../translated_images/hr/tool-chaining-example.538203e73d09dd82.webp" alt="Primjer povezivanja alata" width="800"/>

*Povezivanje alata u praksi — agent prvo poziva getCurrentWeather, zatim šalje rezultat u Celzijusima u celsiusToFahrenheit i daje objedinjeni odgovor.*

**Graceful Failures** — Pitajte za vremensku prognozu u gradu koji nije u lažnim podacima. Alat vraća poruku o grešci, a AI objašnjava da ne može pomoći umjesto da se sruši. Alati sigurnosno prijavljuju greške. Dijagram ispod uspoređuje oba pristupa — uz pravilno rukovanje greškama, agent uhvati iznimku i odgovori korisno, bez toga cijela aplikacija pada:

<img src="../../../translated_images/hr/error-handling-flow.9a330ffc8ee0475c.webp" alt="Tijek rukovanja greškama" width="800"/>

*Kada alat zakaže, agent uhvati grešku i odgovara korisnim objašnjenjem umjesto rušenja.*

Ovo se događa u jednom krugu razgovora. Agent samostalno orkestrira višestruke pozive alata.

## Pokrenite aplikaciju

**Provjerite implementaciju:**

Provjerite postoji li `.env` datoteka u korijenskom direktoriju s Azure vjerodajnicama (kreirana tijekom Modula 01). Pokrenite ovo iz direktorija modula (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Trebalo bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Trebalo bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pokrenite aplikaciju:**

> **Napomena:** Ako ste već pokrenuli sve aplikacije koristeći `./start-all.sh` iz korijenskog direktorija (kako je opisano u Modulu 01), ovaj modul već radi na portu 8084. Možete preskočiti naredbe za pokretanje ispod i odmah otići na http://localhost:8084.

**Opcija 1: Korištenje Spring Boot Dashboarda (Preporučeno za VS Code korisnike)**

Dev kontejner uključuje ekstenziju Spring Boot Dashboard koja pruža vizualno sučelje za upravljanje svim Spring Boot aplikacijama. Možete je pronaći u Activity Baru na lijevoj strani VS Code-a (potražite Spring Boot ikonu).

Iz Spring Boot Dashboarda možete:
- Vidjeti sve dostupne Spring Boot aplikacije u radnom prostoru
- Pokrenuti/zaustaviti aplikacije jednim klikom
- Prikazivati logove aplikacije u stvarnom vremenu
- Pratiti status aplikacije
Jednostavno kliknite gumb za reprodukciju pored "tools" za pokretanje ovog modula ili pokrenite sve module odjednom.

Evo kako Spring Boot nadzorna ploča izgleda u VS Code-u:

<img src="../../../translated_images/hr/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot nadzorna ploča u VS Code-u — pokrenite, zaustavite i pratite sve module s jednog mjesta*

**Opcija 2: Korištenje shell skripti**

Pokrenite sve web aplikacije (moduli 01-04):

**Bash:**
```bash
cd ..  # Iz korijenskog direktorija
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iz korijenskog direktorija
.\start-all.ps1
```

Ili pokrenite samo ovaj modul:

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

Obje skripte automatski učitavaju varijable okoline iz glavne `.env` datoteke i izgrađuju JAR-ove ako ne postoje.

> **Napomena:** Ako želite ručno izgraditi sve module prije pokretanja:
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

Otvorite http://localhost:8084 u vašem pregledniku.

**Za zaustavljanje:**

**Bash:**
```bash
./stop.sh  # Samo ovaj modul
# Ili
cd .. && ./stop-all.sh  # Svi moduli
```

**PowerShell:**
```powershell
.\stop.ps1  # Samo ovaj modul
# Ili
cd ..; .\stop-all.ps1  # Svi moduli
```

## Korištenje aplikacije

Aplikacija pruža web sučelje gdje možete komunicirati s AI agentom koji ima pristup alatima za vremensku prognozu i pretvorbu temperature. Evo kako sučelje izgleda — uključuje primjere za brz početak i chat panel za slanje upita:

<a href="images/tools-homepage.png"><img src="../../../translated_images/hr/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Sučelje AI Agent Tools - brzi primjeri i chat sučelje za interakciju s alatima*

### Isprobajte jednostavno korištenje alata

Započnite s jednostavnim zahtjevom: "Pretvori 100 stupnjeva Fahrenheita u Celzijuse". Agent prepoznaje da mu treba alat za pretvorbu temperature, poziva ga s pravim parametrima i vraća rezultat. Primijetite koliko je prirodno — niste naveli koji alat koristiti niti kako ga pozvati.

### Isprobajte povezivanje alata

Sada pokušajte složeniji zahtjev: "Kakvo je vrijeme u Seattleu i pretvori ga u Fahrenheite?" Promatrajte kako agent radi u koracima. Prvo dohvaća vremensku prognozu (koja vraća Celzijuse), prepoznaje da treba pretvoriti u Fahrenheite, poziva alat za pretvorbu i kombinira oba rezultata u jedan odgovor.

### Pogledajte tijek razgovora

Chat sučelje održava povijest razgovora, što vam omogućuje višekratne interakcije. Možete vidjeti sve prethodne upite i odgovore, olakšavajući praćenje razgovora i razumijevanje kako agent gradi kontekst kroz više izmjena.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/hr/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Višekratni razgovor koji pokazuje jednostavne pretvorbe, provjere vremena i povezivanje alata*

### Eksperimentirajte s različitim zahtjevima

Isprobajte razne kombinacije:
- Provjere vremena: "Kakvo je vrijeme u Tokiju?"
- Pretvorba temperatura: "Koliko je 25°C u Kelvinima?"
- Kombinirani upiti: "Provjeri vrijeme u Parizu i reci mi je li iznad 20°C"

Primijetite kako agent interpretira prirodni jezik i preslikava ga na odgovarajuće pozive alata.

## Ključni koncepti

### ReAct obrazac (razmišljanje i djelovanje)

Agent naizmjenično razmišlja (odlučuje što napraviti) i djeluje (koristi alate). Ovaj obrazac omogućava autonomno rješavanje problema umjesto da samo reagira na upute.

### Opisi alata su važni

Kvaliteta opisa vaših alata izravno utječe na to koliko ih agent koristi. Jasni, specifični opisi pomažu modelu razumjeti kada i kako pozvati svaki alat.

### Upravljanje sesijama

`@MemoryId` anotacija omogućava automatsko upravljanje memorijom na temelju sesija. Svaki ID sesije dobiva vlastitu `ChatMemory` instancu kojom upravlja `ChatMemoryProvider` bean, pa više korisnika može istovremeno komunicirati s agentom bez miješanja razgovora. Sljedeća dijagram ilustrira kako se više korisnika usmjerava u izolirane memorijske pohrane temeljene na njihovim ID-evima sesija:

<img src="../../../translated_images/hr/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Svaki ID sesije preslikava se na izoliranu povijest razgovora — korisnici nikada ne vide poruke jedni drugih.*

### Rukovanje greškama

Alati mogu zakazati — API-ji mogu isteći, parametri mogu biti neispravni, vanjske usluge mogu pasti. Produkcijski agenti trebaju rukovanje greškama kako bi model mogao objasniti probleme ili pokušati alternative umjesto da se cijela aplikacija sruši. Kada alat baci iznimku, LangChain4j ju uhvati i prosljeđuje poruku o grešci natrag modelu, koji potom može objasniti problem prirodnim jezikom.

## Dostupni alati

Sljedeći dijagram prikazuje širok ekosustav alata koje možete izgraditi. Ovaj modul pokazuje alate za vremensku prognozu i temperaturu, ali isti `@Tool` obrazac djeluje za bilo koju Java metodu — od upita u bazu podataka do obrade plaćanja.

<img src="../../../translated_images/hr/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Svaka Java metoda označena s @Tool postaje dostupna AI-u — obrazac se proširuje na baze podataka, API-je, email, rad s datotekama i još mnogo toga.*

## Kada koristiti agente temeljene na alatima

Ne svaki zahtjev treba alate. Odluka ovisi o tome treba li AI-u interakcija s vanjskim sustavima ili može odgovoriti iz vlastitog znanja. Sljedeći vodič sažima kada alati donose vrijednost, a kada nisu potrebni:

<img src="../../../translated_images/hr/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*Brzi vodič za odluke — alati služe za podatke u stvarnom vremenu, izračune i akcije; opće znanje i kreativni zadaci ih ne trebaju.*

## Alati nasuprot RAG-u

Moduli 03 i 04 proširuju mogućnosti AI-a, ali na sasvim različite načine. RAG omogućuje modelu pristup **znanju** dohvaćanjem dokumenata. Alati omogućuju modelu da poduzima **akcije** pozivanjem funkcija. Sljedeći dijagram uspoređuje ove dvije pristupe - od načina rada svakog tijeka rada do kompromisa između njih:

<img src="../../../translated_images/hr/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG dohvaća informacije iz statičnih dokumenata — Alati izvršavaju akcije i dohvaćaju dinamičke, stvarne podatke. Mnogi produkcijski sustavi kombiniraju oboje.*

U praksi mnogi produkcijski sustavi kombiniraju oba pristupa: RAG za utemeljenje odgovora u vašoj dokumentaciji, i Alate za dohvaćanje živih podataka ili izvođenje operacija.

## Sljedeći koraci

**Sljedeći modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigacija:** [← Prethodni: Modul 03 - RAG](../03-rag/README.md) | [Povratak na početak](../README.md) | [Sljedeći: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Izjava o odricanju od odgovornosti**:
Ovaj dokument preveden je korištenjem AI usluge za prijevod [Co-op Translator](https://github.com/Azure/co-op-translator). Iako težimo točnosti, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku smatra se službenim i autoritativnim. Za važne informacije preporučuje se profesionalni ljudski prijevod. Ne snosimo odgovornost za bilo kakva nesporazuma ili pogrešne interpretacije proizašle iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->