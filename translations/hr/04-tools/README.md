# Modul 04: AI Agent s Alatima

## Sadržaj

- [Što ćete naučiti](../../../04-tools)
- [Preduvjeti](../../../04-tools)
- [Razumijevanje AI agenata s alatima](../../../04-tools)
- [Kako pozivanje alata funkcionira](../../../04-tools)
  - [Definicije alata](../../../04-tools)
  - [Donošenje odluka](../../../04-tools)
  - [Izvršenje](../../../04-tools)
  - [Generiranje odgovora](../../../04-tools)
  - [Arhitektura: Spring Boot automatsko povezivanje](../../../04-tools)
- [Lanac alata](../../../04-tools)
- [Pokrenite aplikaciju](../../../04-tools)
- [Korištenje aplikacije](../../../04-tools)
  - [Isprobajte jednostavnu upotrebu alata](../../../04-tools)
  - [Testirajte lanac alata](../../../04-tools)
  - [Pogledajte tijek razgovora](../../../04-tools)
  - [Eksperimentirajte s različitim zahtjevima](../../../04-tools)
- [Ključni pojmovi](../../../04-tools)
  - [ReAct obrazac (razumijevanje i djelovanje)](../../../04-tools)
  - [Opis alata je važan](../../../04-tools)
  - [Upravljanje sesijom](../../../04-tools)
  - [Rukovanje pogreškama](../../../04-tools)
- [Dostupni alati](../../../04-tools)
- [Kada koristiti agente temeljene na alatima](../../../04-tools)
- [Alati naspram RAG](../../../04-tools)
- [Sljedeći koraci](../../../04-tools)

## Što ćete naučiti

Do sada ste naučili kako voditi razgovore s AI, učinkovito strukturirati upite i povezivati odgovore s vašim dokumentima. Ali i dalje postoji temeljno ograničenje: jezični modeli mogu samo generirati tekst. Ne mogu provjeriti vrijeme, izračunati, pretraživati baze podataka niti komunicirati s vanjskim sustavima.

Alati to mijenjaju. Davanjem modelu pristupa funkcijama koje može pozivati, pretvarate ga iz generatora teksta u agenta koji može poduzimati radnje. Model odlučuje kada mu je potreban alat, koji alat koristiti i koje parametre proslijediti. Vaš kod izvršava funkciju i vraća rezultat. Model uklapa taj rezultat u svoj odgovor.

## Preduvjeti

- Završeni Modul 01 (Azure OpenAI resursi postavljeni)
- `.env` datoteka u korijenskom direktoriju s Azure vjerodajnicama (kreirana pomoću `azd up` u Modulu 01)

> **Napomena:** Ako niste završili Modul 01, prvo slijedite upute za postavljanje tamo.

## Razumijevanje AI agenata s alatima

> **📝 Napomena:** Pojam "agenti" u ovom modulu odnosi se na AI asistente proširene mogućnošću pozivanja alata. Ovo se razlikuje od **Agentic AI** obrazaca (autonomni agenti s planiranjem, memorijom i višestupanjskim rezoniranjem) koje ćemo obraditi u [Modulu 05: MCP](../05-mcp/README.md).

Bez alata, jezični model može samo generirati tekst prema svojim podacima za učenje. Pitajte ga za trenutno vrijeme i on mora nagađati. Dajte mu alate i može pozvati vremenski API, izvoditi izračune ili pretraživati bazu podataka — a zatim te stvarne rezultate inkorporirati u svoj odgovor.

<img src="../../../translated_images/hr/what-are-tools.724e468fc4de64da.webp" alt="Bez alata naspram s alatima" width="800"/>

*Bez alata model može samo nagađati — s alatima može pozivati API-je, izvoditi izračune i vraćati podatke u stvarnom vremenu.*

AI agent s alatima prati obrazac **Razumijevanje i Djelovanje (ReAct)**. Model ne odgovara samo — on razmišlja što mu treba, djeluje pozivajući alat, promatra rezultat i zatim odlučuje treba li ponovno djelovati ili dati konačni odgovor:

1. **Razumijevanje** — agent analizira korisnikovo pitanje i određuje koje informacije su mu potrebne
2. **Djelovanje** — agent odabire pravi alat, generira ispravne parametre i poziva ga
3. **Promatranje** — agent prima izlaz alata i procjenjuje rezultat
4. **Ponavljanje ili odgovor** — ako treba više podataka, agent se vraća na početak; inače sastavlja odgovor na prirodnom jeziku

<img src="../../../translated_images/hr/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct obrazac" width="800"/>

*ReAct ciklus — agent razmišlja što učiniti, djeluje pozivanjem alata, promatra rezultat i ponavlja dok ne može dati konačan odgovor.*

Sve se to događa automatski. Vi definirate alate i njihove opise. Model sam donosi odluke kada i kako ih koristiti.

## Kako pozivanje alata funkcionira

### Definicije alata

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Definirate funkcije s jasnim opisima i specifikacijama parametara. Model vidi te opise u svom sistemskom upitu i razumije što svaki alat radi.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Vaša logika pretraživanja vremena
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Asistent je automatski povezan pomoću Spring Boot-a s:
// - ChatModel bean
// - Sve @Tool metode iz @Component klasa
// - ChatMemoryProvider za upravljanje sesijama
```

Dijagram ispod razlaže svaku anotaciju i prikazuje kako svaki dio pomaže AI-ju da razumije kada pozvati alat i koje argumente proslijediti:

<img src="../../../translated_images/hr/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomija definicija alata" width="800"/>

*Anatomija definicije alata — @Tool kaže AI-ju kada ga koristiti, @P opisuje svaki parametar, a @AiService povezuje sve zajedno pri pokretanju.*

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) i pitajte:
> - "Kako bih integrirao stvarni vremenski API poput OpenWeatherMap umjesto lažnih podataka?"
> - "Što čini dobar opis alata koji pomaže AI-ju da ga ispravno koristi?"
> - "Kako se nositi s pogreškama API-ja i ograničenjima u implementacijama alata?"

### Donošenje odluka

Kad korisnik pita "Kako je vrijeme u Seattleu?", model ne bira alat nasumično. On uspoređuje korisničku namjeru sa svim opisima alata kojima ima pristup, ocjenjuje svaki za relevantnost i odabire najbolju podudarnost. Zatim generira strukturirani poziv funkciji s ispravnim parametrima — u ovom slučaju postavljajući `location` na `"Seattle"`.

Ako nijedan alat ne odgovara korisničkom upitu, model odgovara na temelju vlastitog znanja. Ako više alata odgovara, model bira najspecifičniji.

<img src="../../../translated_images/hr/decision-making.409cd562e5cecc49.webp" alt="Kako AI donosi odluke koji alat koristiti" width="800"/>

*Model vrednuje svaki dostupan alat prema korisničkoj namjeri i odabire najbolju podudarnost — zbog toga je važno pisati jasne, specifične opise alata.*

### Izvršenje

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot automatski povezuje deklarativni `@AiService` sučelje sa svim registriranim alatima, a LangChain4j automatski izvršava pozive alata. Iza scene, kompletan poziv alatu prolazi kroz šest faza — od korisničkog pitanja na prirodnom jeziku do konačnog odgovora na prirodnom jeziku:

<img src="../../../translated_images/hr/tool-calling-flow.8601941b0ca041e6.webp" alt="Tijek poziva alata" width="800"/>

*Kraj-do-kraja tijek — korisnik postavlja pitanje, model bira alat, LangChain4j ga izvršava, a model uklapa rezultat u prirodan odgovor.*

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) i pitajte:
> - "Kako ReAct obrazac funkcionira i zašto je učinkovit za AI agente?"
> - "Kako agent odlučuje koji alat koristiti i kojim redoslijedom?"
> - "Što se događa ako izvršenje alata ne uspije - kako robusno rukovati pogreškama?"

### Generiranje odgovora

Model prima vremenske podatke i formatira ih u odgovor na prirodnom jeziku za korisnika.

### Arhitektura: Spring Boot automatsko povezivanje

Ovaj modul koristi LangChain4j integraciju sa Spring Boot-om s deklarativnim `@AiService` sučeljima. Pri pokretanju Spring Boot pronalazi svaki `@Component` koji sadrži `@Tool` metode, vaš `ChatModel` bean i `ChatMemoryProvider` — te ih sve povezuje u jedno `Assistant` sučelje bez dodatnog koda.

<img src="../../../translated_images/hr/spring-boot-wiring.151321795988b04e.webp" alt="Arhitektura Spring Boot automatskog povezivanja" width="800"/>

*@AiService sučelje povezuje ChatModel, komponente alata i pružatelja memorije — Spring Boot sve automatski povezuje.*

Ključne prednosti ovog pristupa:

- **Spring Boot automatsko povezivanje** — ChatModel i alati automatski ubrizgani
- **@MemoryId obrazac** — Automatsko upravljanje memorijom po sesijama
- **Jedinstvena instanca** — Assistant se stvara jednom i ponovno koristi za bolju izvedbu
- **Izvršenje sa sigurnošću tipova** — Java metode se pozivaju izravno s konverzijom tipova
- **Orkestracija višekratnih pitanja** — Automatski upravlja lancem alata
- **Nula boilerplate koda** — Nema ručnih poziva `AiServices.builder()` niti memorijskih HashMapa

Alternativni pristupi (ručni `AiServices.builder()`) zahtijevaju više koda i nemaju prednosti integracije sa Spring Boot-om.

## Lanac alata

**Lanac alata** — Prava snaga agenata temeljenih na alatima dolazi do izražaja kada jedno pitanje zahtijeva više alata. Pitajte "Kako je vrijeme u Seattleu u Fahrenheitima?" i agent automatski povezuje dva alata: prvo poziva `getCurrentWeather` da dobije temperaturu u Celzijusima, zatim tu vrijednost prosljeđuje `celsiusToFahrenheit` za konverziju — sve u jednoj rundi razgovora.

<img src="../../../translated_images/hr/tool-chaining-example.538203e73d09dd82.webp" alt="Primjer lanca alata" width="800"/>

*Lanac alata u akciji — agent prvo poziva getCurrentWeather, zatim prosljeđuje Celsius rezultat u celsiusToFahrenheit i daje kombinirani odgovor.*

Ovako to izgleda u pokrenutoj aplikaciji — agent povezuje dva poziva alata u jednoj rundi razgovora:

<a href="images/tool-chaining.png"><img src="../../../translated_images/hr/tool-chaining.3b25af01967d6f7b.webp" alt="Lanac alata" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Stvarni ispis aplikacije — agent automatski povezuje getCurrentWeather → celsiusToFahrenheit u jednom koraku.*

**Ljubazni neuspjesi** — Pitajte za vrijeme u gradu koji nije u lažnim podacima. Alat će vratiti poruku o pogrešci, a AI objasni da ne može pomoći umjesto da se ruši. Alati sigurno propadaju.

<img src="../../../translated_images/hr/error-handling-flow.9a330ffc8ee0475c.webp" alt="Tijek rukovanja pogreškama" width="800"/>

*Kad alat ne uspije, agent uhvati pogrešku i odgovara korisnim objašnjenjem umjesto da se aplikacija sruši.*

Sve se događa u jednoj rundi razgovora. Agent samostalno orkestrira više poziva alata.

## Pokrenite aplikaciju

**Provjerite postavljanje:**

Provjerite postoji li `.env` datoteka u korijenu s Azure vjerodajnicama (kreirana tijekom Modula 01):
```bash
cat ../.env  # Trebao bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pokrenite aplikaciju:**

> **Napomena:** Ako ste već pokrenuli sve aplikacije pomoću `./start-all.sh` iz Modula 01, ovaj modul već radi na portu 8084. Možete preskočiti naredbe za pokretanje u nastavku i izravno otići na http://localhost:8084.

**Opcija 1: Korištenje Spring Boot nadzorne ploče (preporučeno za korisnike VS Code-a)**

Dev kontejner uključuje eksstenziju Spring Boot Dashboard koja pruža vizualno sučelje za upravljanje svim Spring Boot aplikacijama. Pronaći ćete je u Aktivnostnoj traci na lijevoj strani VS Code-a (potražite ikonu Spring Boot).

Iz Spring Boot Dashboard možete:
- Vidjeti sve dostupne Spring Boot aplikacije u radnom prostoru
- Pokrenuti/zaustaviti aplikacije jednim klikom
- Pratiti zapise aplikacija u stvarnom vremenu
- Nadzirati status aplikacije

Jednostavno kliknite gumb za reprodukciju pored "tools" da pokrenete ovaj modul ili pokrenite sve module odjednom.

<img src="../../../translated_images/hr/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot nadzorna ploča" width="400"/>

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

Obje skripte automatski učitavaju varijable okoline iz `.env` datoteke u korijenu i izgradit će JAR-ove ako ne postoje.

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

Aplikacija pruža web sučelje gdje možete komunicirati s AI agentom koji ima pristup alatima za vremensku prognozu i pretvorbu temperatura.

<a href="images/tools-homepage.png"><img src="../../../translated_images/hr/tools-homepage.4b4cd8b2717f9621.webp" alt="Sučelje AI Agent Alata" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Sučelje AI Agent Alata - brzi primjeri i razgovorni interfejs za interakciju s alatima*

### Isprobajte jednostavnu upotrebu alata
Započnite s jednostavnim zahtjevom: "Pretvori 100 stupnjeva Fahrenheita u Celzijuse". Agent prepoznaje da mu je potreban alat za pretvorbu temperature, poziva ga s ispravnim parametrima i vraća rezultat. Primijetite koliko ovo djeluje prirodno - niste specificirali koji alat koristiti ili kako ga pozvati.

### Testiranje povezivanja alata

Sada pokušajte nešto složenije: "Kakvo je vrijeme u Seattleu i pretvori ga u Fahrenheit?" Promatrajte kako agent kroz korake rješava ovaj zahtjev. Prvo dobiva vremensku prognozu (koja vraća Celzijuse), zatim prepoznaje potrebu za pretvorbom u Fahrenheit, poziva alat za pretvorbu i kombinira oba rezultata u jedan odgovor.

### Pogledajte tok razgovora

Chat sučelje održava povijest razgovora, što omogućava višestruke interakcije u više koraka. Možete vidjeti sve prethodne upite i odgovore, što olakšava praćenje razgovora i razumijevanje kako agent gradi kontekst kroz više izmjena.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/hr/tools-conversation-demo.89f2ce9676080f59.webp" alt="Razgovor s višestrukim pozivima alata" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Višekratni razgovor koji pokazuje jednostavne pretvorbe, pronalaženje vremena i povezivanje alata*

### Eksperimentirajte s različitim zahtjevima

Isprobajte različite kombinacije:
- Pregled vremenske prognoze: "Kakvo je vrijeme u Tokiju?"
- Pretvorbe temperature: "Koliko je 25°C u Kelvinima?"
- Kombinirani upiti: "Provjeri vrijeme u Parizu i reci mi je li iznad 20°C"

Primijetite kako agent tumači prirodni jezik i povezuje ga s odgovarajućim pozivima alata.

## Ključni koncepti

### ReAct obrazac (razmišljanje i djelovanje)

Agent se izmjenjuje između rezoniranja (odlučivanja što napraviti) i djelovanja (korištenja alata). Ovaj obrazac omogućava autonomno rješavanje problema umjesto pukog odgovaranja na upute.

### Opisi alata su važni

Kvaliteta opisa vaših alata direktno utječe na to koliko ih agent dobro koristi. Jasni, specifični opisi pomažu modelu razumjeti kada i kako pozvati svaki alat.

### Upravljanje sesijama

`@MemoryId` anotacija omogućava automatsko upravljanje memorijom temeljenom na sesiji. Svaki identifikator sesije dobiva vlastitu `ChatMemory` instancu kojom upravlja `ChatMemoryProvider` bean, tako da više korisnika može istovremeno razgovarati s agentom bez miješanja njihovih razgovora.

<img src="../../../translated_images/hr/session-management.91ad819c6c89c400.webp" alt="Upravljanje sesijama s @MemoryId" width="800"/>

*Svaki identifikator sesije odgovara izoliranoj povijesti razgovora — korisnici nikada ne vide poruke drugih.*

### Rukovanje pogreškama

Alati mogu zakašnjavati — API-ji mogu imati vremenska ograničenja, parametri mogu biti neispravni, vanjske usluge mogu pasti. Producentni agenti trebaju rukovanje pogreškama kako bi model mogao objasniti problem ili pokušati alternativu umjesto da se cijela aplikacija sruši. Kada alat baci iznimku, LangChain4j je uhvati i prenese poruku o pogrešci natrag modelu, koji potom može na prirodnom jeziku objasniti problem.

## Dostupni alati

Dijagram ispod prikazuje širok ekosustav alata koje možete razvijati. Ovaj modul demonstrira alate za vrijeme i temperaturu, ali isti `@Tool` obrazac radi za bilo koju Java metodu — od upita baze podataka do obrade plaćanja.

<img src="../../../translated_images/hr/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ekosustav alata" width="800"/>

*Svaka Java metoda označena s @Tool postaje dostupna AI-u — obrazac se širi na baze podataka, API-je, email, rad s datotekama i još mnogo toga.*

## Kada koristiti agente temeljene na alatima

<img src="../../../translated_images/hr/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Kada koristiti alate" width="800"/>

*Brzi vodič – alati služe za stvarne podatke u stvarnom vremenu, izračune i akcije; opće znanje i kreativni zadaci ih ne trebaju.*

**Koristite alate kada:**
- Odgovor zahtijeva podatke u stvarnom vremenu (vrijeme, cijene dionica, zalihe)
- Trebate izvoditi izračune složenije od jednostavne matematike
- Pristupate bazama podataka ili API-jima
- Izvršavate radnje (slanje emailova, kreiranje tiketa, ažuriranje zapisa)
- Kombinirate više izvora podataka

**Nemojte koristiti alate kada:**
- Pitanja se mogu odgovoriti iz općeg znanja
- Odgovor je isključivo razgovorni
- Kašnjenje alata bi usporilo iskustvo

## Alati vs RAG

Moduli 03 i 04 oba proširuju mogućnosti AI, ali na temeljno različite načine. RAG daje modelu pristup **znanju** pronalaženjem dokumenata. Alati daju modelu sposobnost poduzimanja **akcija** pozivom funkcija.

<img src="../../../translated_images/hr/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Usporedba alata i RAG-a" width="800"/>

*RAG dohvaća informacije iz statičkih dokumenata — alati izvršavaju akcije i dohvaćaju dinamične, stvarne podatke. Mnogi produkcijski sustavi kombiniraju oba.*

U praksi, mnogi produkcijski sustavi kombiniraju oba pristupa: RAG za temeljenje odgovora u vašoj dokumentaciji, a alate za dohvaćanje živih podataka ili izvođenje operacija.

## Sljedeći koraci

**Sljedeći modul:** [05-mcp - Protokol konteksta modela (MCP)](../05-mcp/README.md)

---

**Navigacija:** [← Prethodni: Modul 03 - RAG](../03-rag/README.md) | [Natrag na početak](../README.md) | [Sljedeći: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Izjava o odricanju od odgovornosti**:
Ovaj dokument je preveden pomoću AI usluge za prijevod [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo postići točnost, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati autoritativnim izvorom. Za važne informacije preporučuje se profesionalni ljudski prijevod. Nismo odgovorni za bilo kakva nesporazume ili pogrešna tumačenja koja proizlaze iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->