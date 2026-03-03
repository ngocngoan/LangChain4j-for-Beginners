# Modul 04: AI agenti s alatima

## Sadržaj

- [Što ćete naučiti](../../../04-tools)
- [Preduvjeti](../../../04-tools)
- [Razumijevanje AI agenata s alatima](../../../04-tools)
- [Kako funkcionira pozivanje alata](../../../04-tools)
  - [Definicije alata](../../../04-tools)
  - [Donošenje odluka](../../../04-tools)
  - [Izvršenje](../../../04-tools)
  - [Generiranje odgovora](../../../04-tools)
  - [Arhitektura: Spring Boot automatsko povezivanje](../../../04-tools)
- [Povezivanje alata](../../../04-tools)
- [Pokreni aplikaciju](../../../04-tools)
- [Korištenje aplikacije](../../../04-tools)
  - [Isprobajte jednostavnu upotrebu alata](../../../04-tools)
  - [Testirajte povezivanje alata](../../../04-tools)
  - [Vidi tijek razgovora](../../../04-tools)
  - [Eksperimentirajte s različitim zahtjevima](../../../04-tools)
- [Ključni koncepti](../../../04-tools)
  - [ReAct obrazac (razmišljanje i djelovanje)](../../../04-tools)
  - [Opis alata je važan](../../../04-tools)
  - [Upravljanje sesijama](../../../04-tools)
  - [Rukovanje pogreškama](../../../04-tools)
- [Dostupni alati](../../../04-tools)
- [Kada koristiti agente temeljene na alatima](../../../04-tools)
- [Alati vs RAG](../../../04-tools)
- [Sljedeći koraci](../../../04-tools)

## Što ćete naučiti

Do sada ste naučili kako razgovarati s AI-jem, učinkovito strukturirati upite i temeljiti odgovore na vašim dokumentima. No postoji osnovno ograničenje: jezični modeli mogu samo generirati tekst. Ne mogu provjeriti vremensku prognozu, obavljati izračune, postavljati upite u baze podataka ili komunicirati s vanjskim sustavima.

Alati to mijenjaju. Davanjem modelu pristup funkcijama koje može pozivati, pretvarate ga iz generatora teksta u agenta koji može poduzeti radnje. Model odlučuje kada treba alat, koji alat koristiti i koje parametre proslijediti. Vaš kod izvršava funkciju i vraća rezultat. Model uključuje rezultat u svoj odgovor.

## Preduvjeti

- Završeni [Modul 01 - Uvod](../01-introduction/README.md) (Azure OpenAI resursi postavljeni)
- Preporučeno završeni prethodni moduli (ovaj modul se referira na [RAG koncepte iz Modula 03](../03-rag/README.md) u usporedbi Alata i RAG-a)
- `.env` datoteka u root direktoriju s Azure vjerodajnicama (kreirana naredbom `azd up` u Modulu 01)

> **Napomena:** Ako niste završili Modul 01, pratite tamo upute za postavljanje prvo.

## Razumijevanje AI agenata s alatima

> **📝 Napomena:** Pojam „agenti“ u ovom modulu odnosi se na AI asistente poboljšane mogućnostima pozivanja alata. Ovo je različito od **Agentic AI** obrazaca (autonomni agenti s planiranjem, memorijom i višekoraknim zaključivanjem) koje ćemo pokriti u [Modulu 05: MCP](../05-mcp/README.md).

Bez alata, jezični model može samo generirati tekst iz svoje obuke. Pitate li ga kakvo je vrijeme, on samo nagađa. Dajući mu alate, može pozvati vremenski API, izračunati ili poslužiti upit bazi podataka — zatim te stvarne rezultate uklopiti u svoj odgovor.

<img src="../../../translated_images/hr/what-are-tools.724e468fc4de64da.webp" alt="Bez alata vs S alatima" width="800"/>

*Bez alata model može samo nagađati — s alatima može pozvati API-je, raditi izračune i vraćati podatke u stvarnom vremenu.*

AI agent s alatima slijedi obrazac **Razmišljanja i Djelovanja (ReAct)**. Model ne samo odgovara — razmišlja o tome što mu treba, djeluje pozivajući alat, promatra rezultat, pa odlučuje hoće li ponovno djelovati ili dati konačan odgovor:

1. **Razmišlja** — agent analizira korisničko pitanje i određuje koju informaciju treba
2. **Djeluje** — agent odabire pravi alat, generira ispravne parametre i poziva ga
3. **Promatra** — agent prima izlaz alata i evaluira rezultat
4. **Ponavlja ili odgovara** — ako treba više podataka agent se vraća na početak, inače sastavlja prirodni odgovor

<img src="../../../translated_images/hr/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct obrazac" width="800"/>

*ReAct ciklus — agent razmišlja što učiniti, djeluje pozivajući alat, promatra rezultat i petlja sve dok ne da konačni odgovor.*

To se događa automatski. Definirate alate i njihove opise. Model odlučuje kada i kako ih koristiti.

## Kako funkcionira pozivanje alata

### Definicije alata

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Definirate funkcije s jasnim opisima i specifikacijama parametara. Model na temelju tih opisa u sustavnom pitanju razumije što svaki alat radi.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Vaša logika za pretraživanje vremenske prognoze
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Asistent je automatski povezan putem Spring Boot-a sa:
// - ChatModel bean
// - Sve @Tool metode iz @Component klasa
// - ChatMemoryProvider za upravljanje sesijom
```

Dijagram ispod razlaže svaku anotaciju i pokazuje kako svaki dio pomaže AI-ju razumjeti kada pozvati alat i koje argumente proslijediti:

<img src="../../../translated_images/hr/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomija definicija alata" width="800"/>

*Anatomija definicije alata — @Tool govori AI-ju kada ga koristiti, @P opisuje svaki parametar, a @AiService povezuje sve tijekom pokretanja.*

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) chatom:** Otvorite [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) i pitajte:
> - „Kako integrirati stvarni API za vremensku prognozu poput OpenWeatherMap umjesto lažnih podataka?“
> - „Što čini dobar opis alata koji pomaže AI-ju da ga pravilno koristi?“
> - „Kako rješavati API pogreške i ograničenja u implementacijama alata?“

### Donošenje odluka

Kad korisnik pita: „Kakvo je vrijeme u Seattleu?“, model ne bira alat nasumično. On uspoređuje namjeru korisnika sa svakim opisom alata kojem ima pristup, boduje relevantnost i odabire najbolji. Zatim generira strukturirani poziv funkcije s ispravnim parametrima — u ovom slučaju postavlja `location` na `"Seattle"`.

Ako nijedan alat ne odgovara zahtjevu korisnika, model odgovara iz vlastitog znanja. Ako više alata odgovara, bira najprecizniji.

<img src="../../../translated_images/hr/decision-making.409cd562e5cecc49.webp" alt="Kako AI odlučuje koji alat koristiti" width="800"/>

*Model evaluira svaki dostupni alat u odnosu na korisničku namjeru i odabire najbolji — zato je pisanje jasnih, specifičnih opisa alata važno.*

### Izvršenje

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot automatski povezuje deklarativni `@AiService` interface sa svim registriranim alatima, a LangChain4j automatski izvršava pozive alata. Iza scene, kompletan poziv alatu prolazi kroz šest faza — od korisničkog pitanja na prirodnom jeziku do konačnog odgovora na prirodnom jeziku:

<img src="../../../translated_images/hr/tool-calling-flow.8601941b0ca041e6.webp" alt="Tijek pozivanja alata" width="800"/>

*Krajnji tijek — korisnik postavlja pitanje, model bira alat, LangChain4j ga izvršava, a model utkiva rezultat u prirodni odgovor.*

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) chatom:** Otvorite [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) i pitajte:
> - „Kako ReAct obrazac funkcionira i zašto je učinkovit za AI agente?“
> - „Kako agent odlučuje koji alat koristiti i kojim redoslijedom?“
> - „Što se događa ako izvršenje alata ne uspije — kako sigurno rukovati pogreškama?“

### Generiranje odgovora

Model prima vremenske podatke i oblikuje ih u odgovor na prirodnom jeziku za korisnika.

### Arhitektura: Spring Boot automatsko povezivanje

Ovaj modul koristi LangChain4j integraciju za Spring Boot s deklarativnim `@AiService` sučeljima. Pri pokretanju Spring Boot pronalazi svaki `@Component` koji sadrži `@Tool` metode, vaš `ChatModel` bean i `ChatMemoryProvider` — i povezuje ih u jedan `Assistant` interface bez potrebe za dodatnim kodom.

<img src="../../../translated_images/hr/spring-boot-wiring.151321795988b04e.webp" alt="Arhitektura Spring Boot automatskog povezivanja" width="800"/>

*Sučelje @AiService povezuje ChatModel, komponente alata i memorijski provajder — Spring Boot samostalno obavlja povezivanje.*

Ključne prednosti ovog pristupa:

- **Spring Boot automatsko povezivanje** — ChatModel i alati se automatski ubacuju
- **@MemoryId obrazac** — Automatsko upravljanje memorijom zasnovano na sesijama
- **Jedinstvena instanca** — Assistant se kreira jednom i koristi za bolju izvedbu
- **Izvršenje s tipizacijom** — Java metode se pozivaju izravno s konverzijom tipova
- **Višekratno upravljanje razgovorom** — Automatski rukuje povezivanjem alata
- **Nula dodatnog koda** — Nema ručnih poziva `AiServices.builder()` ili memorijskih HashMap-a

Alternativni pristupi (ručni `AiServices.builder()`) zahtijevaju više koda i nemaju prednosti Spring Boot integracije.

## Povezivanje alata

**Povezivanje alata** — Prava snaga agenata temeljenih na alatima pokazuje se kada jedno pitanje zahtijeva više alata. Pitate „Kakvo je vrijeme u Seattleu u Fahrenheitu?“ i agent automatski povezuje dva alata: prvo poziva `getCurrentWeather` za temperaturu u Celzijusima, a zatim tu vrijednost prosljeđuje u `celsiusToFahrenheit` za konverziju — sve u jednom koraku razgovora.

<img src="../../../translated_images/hr/tool-chaining-example.538203e73d09dd82.webp" alt="Primjer povezivanja alata" width="800"/>

*Povezivanje alata u akciji — agent prvo poziva getCurrentWeather, zatim prenosi Celsius rezultat u celsiusToFahrenheit i daje kombinirani odgovor.*

**Elegantno rukovanje pogreškama** — Pitate li za vrijeme u gradu koji nije u lažnim podacima, alat vraća poruku o pogrešci, a AI objašnjava da ne može pomoći umjesto da pada. Alati sigurno prijavljuju pogreške. Dijagram ispod uspoređuje dva pristupa — uz pravilno rukovanje pogreškama agent hvata iznimku i pomaže, bez njega cijela aplikacija pada:

<img src="../../../translated_images/hr/error-handling-flow.9a330ffc8ee0475c.webp" alt="Tijek rukovanja pogreškama" width="800"/>

*Kada alat zakaže, agent hvata pogrešku i odgovara korisno umjesto da pada aplikacija.*

Sve se ovo događa u jednom koraku razgovora. Agent samostalno orkestrira više poziva alata.

## Pokreni aplikaciju

**Provjerite postavljanje:**

Provjerite postoji li `.env` datoteka u root direktoriju s Azure vjerodajnicama (kreirana tijekom Modula 01). Pokrenite iz direktorija modula (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Trebao bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Trebao bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pokreni aplikaciju:**

> **Napomena:** Ako ste već pokrenuli sve aplikacije koristeći `./start-all.sh` iz root direktorija (kako je opisano u Modulu 01), ovaj modul već radi na portu 8084. Možete preskočiti naredbe za pokretanje i otići direktno na http://localhost:8084.

**Opcija 1: Korištenje Spring Boot nadzorne ploče (preporučeno za VS Code korisnike)**

Dev container uključuje ekstenziju Spring Boot Dashboard koja nudi vizualno sučelje za upravljanje svim Spring Boot aplikacijama. Pronaći ćete je na bočnoj traci s lijeve strane u VS Codeu (prepoznatljivo po Spring Boot ikoni).

Iz Spring Boot Dashboard možete:
- Vidjeti sve dostupne Spring Boot aplikacije u workspaceu
- Pokrenuti/zaustaviti aplikacije jednim klikom
- Pregledavati zapise aplikacije u stvarnom vremenu
- Pratiti status aplikacije

Jednostavno kliknite tipku za pokretanje pored "tools" da biste startali ovaj modul, ili pokrenite sve module odjednom.

Evo kako Spring Boot Dashboard izgleda u VS Code:

<img src="../../../translated_images/hr/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot nadzorna ploča" width="400"/>

*Spring Boot Dashboard u VS Codeu — pokrenite, zaustavite i pratite sve module s jednog mjesta*

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

Obje skripte automatski učitavaju varijable okoline iz root `.env` datoteke i izgradit će JAR-ove ako ne postoje.

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

Otvori http://localhost:8084 u vašem pregledniku.

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

Aplikacija pruža web sučelje preko kojeg možete komunicirati s AI agentom koji ima pristup alatima za vremensku prognozu i pretvorbu temperature. Evo kako sučelje izgleda — uključuje primjere za brzo pokretanje i chat panel za slanje zahtjeva:
<a href="images/tools-homepage.png"><img src="../../../translated_images/hr/tools-homepage.4b4cd8b2717f9621.webp" alt="Sučelje AI Agent Alata" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Sučelje AI Agent Alata - brzi primjeri i chat sučelje za interakciju s alatima*

### Isprobajte Jednostavnu Upotrebu Alata

Započnite s jednostavnim zahtjevom: "Pretvori 100 stupnjeva Fahrenheit u Celzijus". Agent prepoznaje da treba alat za konverziju temperature, poziva ga s ispravno proslijeđenim parametrima i vraća rezultat. Primijetite koliko je to prirodno - niste specificirali koji alat koristiti niti kako ga pozvati.

### Testirajte Povezivanje Alata

Sada pokušajte nešto složenije: "Kakvo je vrijeme u Seattleu i pretvori ga u Fahrenheit?" Promatrajte kako agent radi to korak po korak. Najprije dohvaća vremensku prognozu (koja vraća u Celzijusima), prepoznaje da treba napraviti konverziju u Fahrenheit, poziva alat za konverziju i kombinira oba rezultata u jedan odgovor.

### Pogledajte Tijek Razgovora

Chat sučelje održava povijest razgovora, omogućujući višekratnu interakciju. Možete vidjeti sve prethodne upite i odgovore, što olakšava praćenje razgovora i razumijevanje kako agent gradi kontekst kroz više razmjena.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/hr/tools-conversation-demo.89f2ce9676080f59.webp" alt="Razgovor s Višestrukim Pozivima Alata" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Višekratni razgovor prikazujući jednostavne konverzije, dohvat vremena i povezivanje alata*

### Eksperimentirajte s Različitim Zahtjevima

Isprobajte različite kombinacije:
- Dohvat vremena: "Kakvo je vrijeme u Tokiju?"
- Konverzije temperature: "Koliko je 25°C u Kelvinima?"
- Kombinirani upiti: "Provjeri vrijeme u Parizu i reci mi je li iznad 20°C"

Primijetite kako agent interpretira prirodni jezik i preslikava ga u odgovarajuće pozive alata.

## Ključni Koncepti

### ReAct Uzorak (Razmišljanje i Djelovanje)

Agent naizmjence razmišlja (odlučuje što učiniti) i djeluje (koristi alate). Ovaj uzorak omogućuje autonomno rješavanje problema, a ne samo odgovaranje na instrukcije.

### Opisi Alata Su Važni

Kvaliteta opisa vaših alata izravno utječe na to koliko dobro agent koristi te alate. Jasni, specifični opisi pomažu modelu da razumije kada i kako pozvati svaki alat.

### Upravljanje Sesijama

Oznaka `@MemoryId` omogućuje automatsko upravljanje memorijom temeljeno na sesiji. Svaki ID sesije ima svoju `ChatMemory` instancu kojom upravlja `ChatMemoryProvider` bean, tako da više korisnika može istovremeno razgovarati s agentom bez miješanja razgovora. Sljedeći dijagram prikazuje kako se višestruki korisnici usmjeravaju u izolirane memorijske spremnike na temelju njihovih ID-eva sesija:

<img src="../../../translated_images/hr/session-management.91ad819c6c89c400.webp" alt="Upravljanje Sesijama s @MemoryId" width="800"/>

*Svaki ID sesije mapira se na izoliranu povijest razgovora — korisnici nikada ne vide poruke jedni drugih.*

### Rukovanje Greškama

Alati mogu zakaći — API-ji isteknu, parametri mogu biti nevažeći, vanjske usluge mogu biti nedostupne. Produkcijski agenti trebaju rukovanje greškama kako bi model mogao objasniti probleme ili pokušati alternative umjesto da cijela aplikacija padne. Kada alat baci iznimku, LangChain4j ju hvata i šalje poruku o grešci natrag modelu, koji potom može na prirodnom jeziku objasniti problem.

## Dostupni Alati

Donji dijagram prikazuje široki ekosustav alata koje možete izgraditi. Ovaj modul demonstrira alate za vremensku prognozu i temperaturu, ali isti `@Tool` uzorak radi za bilo koju Java metodu — od upita baza podataka do plaćanja.

<img src="../../../translated_images/hr/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ekosustav Alata" width="800"/>

*Bilo koja Java metoda označena s @Tool postaje dostupna AI-u — uzorak se proširuje na baze podataka, API-je, email, rad s datotekama i još mnogo toga.*

## Kada Koristiti Agente Temeljene na Alatima

Ne treba svaki zahtjev koristiti alate. Odluka ovisi o tome treba li AI komunicirati s vanjskim sustavima ili može odgovoriti iz vlastitog znanja. Sljedeći vodič sažima kada alati dodaju vrijednost, a kada nisu potrebni:

<img src="../../../translated_images/hr/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Kada Koristiti Alate" width="800"/>

*Brzi vodič za odluke — alati su za podatke u stvarnom vremenu, izračune i akcije; opće znanje i kreativne zadatke ne trebaju alati.*

## Alati naspram RAG-a

Moduli 03 i 04 oboje proširuju sposobnosti AI-a, ali na temeljno različite načine. RAG daje modelu pristup **znanju** dohvaćajući dokumente. Alati daju modelu mogućnost **izvršavanja radnji** pozivom funkcija. Donji dijagram uspoređuje ova dva pristupa jedan uz drugi — od načina na koji svaki radni tijek operira do kompromisa između njih:

<img src="../../../translated_images/hr/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Usporedba Alata i RAG-a" width="800"/>

*RAG dohvaća informacije iz statičnih dokumenata — Alati izvršavaju akcije i dohvaćaju dinamične, stvarne podatke. Mnogi produkcijski sustavi kombiniraju oba.*

U praksi mnogi produkcijski sustavi kombiniraju oba pristupa: RAG za utemeljenje odgovora u vašoj dokumentaciji, a Alati za dohvat živih podataka ili izvođenje operacija.

## Sljedeći Koraci

**Sljedeći Modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigacija:** [← Prethodno: Modul 03 - RAG](../03-rag/README.md) | [Natrag na Početak](../README.md) | [Sljedeće: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Odricanje od odgovornosti**:  
Ovaj dokument preveden je pomoću AI usluge za prevođenje [Co-op Translator](https://github.com/Azure/co-op-translator). Iako težimo točnosti, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati autoritativnim izvorom. Za važne informacije preporučuje se stručni ljudski prijevod. Ne preuzimamo odgovornost za bilo kakve nesporazume ili pogrešne interpretacije koje proizlaze iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->