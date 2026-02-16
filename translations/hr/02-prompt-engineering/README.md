# Modul 02: Inženjering upita s GPT-5.2

## Sadržaj

- [Što ćete naučiti](../../../02-prompt-engineering)
- [Zahtjevi](../../../02-prompt-engineering)
- [Razumijevanje inženjeringa upita](../../../02-prompt-engineering)
- [Osnove inženjeringa upita](../../../02-prompt-engineering)
  - [Zero-Shot upiti](../../../02-prompt-engineering)
  - [Few-Shot upiti](../../../02-prompt-engineering)
  - [Lanac razmišljanja](../../../02-prompt-engineering)
  - [Upiti temeljeni na ulozi](../../../02-prompt-engineering)
  - [Predlošci upita](../../../02-prompt-engineering)
- [Napredni obrasci](../../../02-prompt-engineering)
- [Korištenje postojećih Azure resursa](../../../02-prompt-engineering)
- [Prikazi aplikacija](../../../02-prompt-engineering)
- [Istraživanje obrazaca](../../../02-prompt-engineering)
  - [Nisko vs visoko nestrpljenje](../../../02-prompt-engineering)
  - [Izvršavanje zadataka (predgovori alata)](../../../02-prompt-engineering)
  - [Kod sa samorefleksijom](../../../02-prompt-engineering)
  - [Struktuirana analiza](../../../02-prompt-engineering)
  - [Višekratni razgovor](../../../02-prompt-engineering)
  - [Korak po korak razmišljanje](../../../02-prompt-engineering)
  - [Ograničeni izlaz](../../../02-prompt-engineering)
- [Što zaista učite](../../../02-prompt-engineering)
- [Sljedeći koraci](../../../02-prompt-engineering)

## Što ćete naučiti

<img src="../../../translated_images/hr/what-youll-learn.c68269ac048503b2.webp" alt="Što ćete naučiti" width="800"/>

U prethodnom modulu ste vidjeli kako memorija omogućava konverzacijsku AI i koristili ste GitHub modele za osnovne interakcije. Sada ćemo se usredotočiti na način postavljanja pitanja — same upite — koristeći Azure OpenAI-jev GPT-5.2. Način na koji strukturirate upite dramatično utječe na kvalitetu dobivenih odgovora. Počinjemo s pregledom osnovnih tehnika promptanja, zatim prelazimo na osam naprednih obrazaca koji u potpunosti koriste mogućnosti GPT-5.2.

Koristit ćemo GPT-5.2 jer uvodi kontrolu razmišljanja - možete modelu reći koliko razmišljanja treba napraviti prije odgovora. To čini različite strategije promptanja jasnijima i pomaže vam razumjeti kada koristiti svaki pristup. Također ćemo imati koristi od manjih ograničenja stope u Azure-u za GPT-5.2 u usporedbi s GitHub Modelima.

## Zahtjevi

- Dovršen Modul 01 (Azure OpenAI resursi postavljeni)
- `.env` datoteka u korijenskom direktoriju s Azure vjerodajnicama (kreirana pomoću `azd up` u Modulu 01)

> **Napomena:** Ako niste dovršili Modul 01, pratite tamošnje upute za postavljanje prije nastavka.

## Razumijevanje inženjeringa upita

<img src="../../../translated_images/hr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Što je inženjering upita?" width="800"/>

Inženjering upita odnosi se na dizajniranje ulaznog teksta koji dosljedno donosi željene rezultate. Nije samo o postavljanju pitanja - radi se o strukturiranju zahtjeva tako da model točno razumije što želite i kako to isporučiti.

Razmislite o tome kao da dajete upute kolegi. "Popravi grešku" je nejasno. "Popravi iznimku null pointer u UserService.java na liniji 45 dodavanjem null provjere" je specifično. Jezični modeli funkcioniraju na isti način - specifičnost i struktura su važni.

<img src="../../../translated_images/hr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kako se LangChain4j uklapa" width="800"/>

LangChain4j pruža infrastrukturu — veze s modelom, memoriju i vrste poruka — dok su obrasci prompta samo pažljivo strukturirani tekstovi koje šaljete preko te infrastrukture. Ključni gradivni blokovi su `SystemMessage` (koji postavlja ponašanje i ulogu AI-ja) i `UserMessage` (koji nosi vaš stvarni zahtjev).

## Osnove inženjeringa upita

<img src="../../../translated_images/hr/five-patterns-overview.160f35045ffd2a94.webp" alt="Pregled pet obrazaca inženjeringa upita" width="800"/>

Prije nego što zaronimo u napredne obrasce ovog modula, pregledajmo pet osnovnih tehnika promptanja. To su gradivni blokovi koje svaki inženjer prompta treba poznavati. Ako ste već radili kroz [Brzi početak modul](../00-quick-start/README.md#2-prompt-patterns), vidjeli ste ih u akciji — evo konceptualnog okvira iza njih.

### Zero-Shot upiti

Najjednostavniji pristup: dajte modelu izravnu uputu bez primjera. Model se u potpunosti oslanja na svoje treniranje da razumije i izvrši zadatak. Ovo dobro funkcionira za jednostavne zahtjeve gdje je očekivano ponašanje očito.

<img src="../../../translated_images/hr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot promptanje" width="800"/>

*Izravna uputa bez primjera — model zaključuje zadatak samo iz upute*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odgovor: "Pozitivno"
```

**Kada koristiti:** Jednostavne klasifikacije, izravna pitanja, prijevodi ili bilo koji zadatak koji model može obaviti bez dodatnih uputa.

### Few-Shot upiti

Pružite primjere koji demonstriraju obrazac koji želite da model slijedi. Model uči očekivani format ulaza i izlaza iz vaših primjera i primjenjuje ga na nove unose. Ovo znatno poboljšava konzistentnost za zadatke gdje željeni format ili ponašanje nije očito.

<img src="../../../translated_images/hr/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot promptanje" width="800"/>

*Učenje iz primjera — model identificira obrazac i primjenjuje ga na nove unose*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**Kada koristiti:** Prilagođene klasifikacije, dosljedno formatiranje, zadaci specifični za domenu ili kad su zero-shot rezultati nekonzistentni.

### Lanac razmišljanja

Zatražite od modela da pokaže svoj proces razmišljanja korak po korak. Umjesto da skoči odmah na odgovor, model razlaže problem i radi kroz svaki dio eksplicitno. Ovo poboljšava točnost kod matematičkih, logičkih i višekoraknih zadataka razmišljanja.

<img src="../../../translated_images/hr/chain-of-thought.5cff6630e2657e2a.webp" alt="Lanac razmišljanja promptanje" width="800"/>

*Razmišljanje korak po korak — razbijanje složenih problema na eksplicitne logičke korake*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model pokazuje: 15 - 8 = 7, zatim 7 + 12 = 19 jabuka
```

**Kada koristiti:** Matematički zadaci, logičke zagonetke, otklanjanje pogrešaka ili bilo koji zadatak gdje prikaz procesa razmišljanja poboljšava točnost i povjerenje.

### Upiti temeljeni na ulozi

Postavite personu ili ulogu AI-ja prije nego što postavite pitanje. To pruža kontekst koji oblikuje ton, dubinu i fokus odgovora. "Softverski arhitekt" daje drugačije savjete od "mlađeg programera" ili "revizora sigurnosti".

<img src="../../../translated_images/hr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Promptanje temeljeno na ulozi" width="800"/>

*Postavljanje konteksta i persone — isto pitanje dobiva različiti odgovor ovisno o dodijeljenoj ulozi*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**Kada koristiti:** Pregledi koda, podučavanje, analiza specifična za domenu ili kad trebate odgovore prilagođene određenoj razini stručnosti ili perspektivi.

### Predlošci upita

Kreirajte ponovljivo upite s varijabilnim rezerviranim mjestima. Umjesto da svaki put pišete novi upit, definirajte predložak jednom i popunjavajte različite vrijednosti. LangChain4j-ova klasa `PromptTemplate` ovo olakšava s `{{varijabla}}` sintaksom.

<img src="../../../translated_images/hr/prompt-templates.14bfc37d45f1a933.webp" alt="Predlošci upita" width="800"/>

*Ponovljivi upiti s varijabilnim rezerviranim mjestima — jedan predložak, mnogo primjena*

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

**Kada koristiti:** Ponavljani upiti s različitim unosima, obrada u serijama, izgradnja ponovljivih AI radnih tijekova ili bilo koji scenarij gdje struktura upita ostaje ista, a podaci se mijenjaju.

---

Ovih pet osnova daje vam solidan alat za većinu zadataka promptanja. Ostatak ovog modula nadograđuje ih s **osam naprednih obrazaca** koji koriste GPT-5.2 kontrolu razmišljanja, samoocjenjivanje i mogućnosti strukturiranog izlaza.

## Napredni obrasci

S osnovama pokrivenima, prijeđimo na osam naprednih obrazaca koji čine ovaj modul jedinstvenim. Nisu svi problemi jednaki. Neka pitanja traže brze odgovore, druga duboko razmišljanje. Neka zahtijevaju vidljivo razmišljanje, a druga tek rezultate. Svaki donji obrazac optimiziran je za drugačiji scenarij — a kontrola razmišljanja GPT-5.2 dodatno naglašava razlike.

<img src="../../../translated_images/hr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osam uzoraka promptanja" width="800"/>

*Pregled osam obrazaca inženjeringa promptanja i njihovih primjena*

<img src="../../../translated_images/hr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontrola razmišljanja s GPT-5.2" width="800"/>

*Kontrola razmišljanja GPT-5.2 omogućuje da navedete koliko razmišljanja model treba raditi — od brzih izravnih odgovora do dubokog istraživanja*

<img src="../../../translated_images/hr/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Usporedba napora razmišljanja" width="800"/>

*Nisko nestrpljenje (brzo, izravno) vs visoko nestrpljenje (temeljito, istraživačko) pristupi razmišljanju*

**Nisko nestrpljenje (Brzo & Fokusirano)** - Za jednostavna pitanja gdje želite brze, izravne odgovore. Model radi minimalno razmišljanje - maksimalno 2 koraka. Koristite ovo za izračune, pretrage ili jednostavna pitanja.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Istražite s GitHub Copilotom:** Otvorite [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) i pitajte:
> - "Koja je razlika između niskog i visokog nestrpljenja u obrascima promptanja?"
> - "Kako XML oznake u promptima pomažu oblikovati odgovor AI-ja?"
> - "Kada bih trebao koristiti obrasce samorefleksije vs izravne upute?"

**Visoko nestrpljenje (Duboko & Temeljito)** - Za složene probleme gdje želite sveobuhvatnu analizu. Model temeljito istražuje i pokazuje detaljno razmišljanje. Koristite za dizajn sustava, arhitektonske odluke ili složena istraživanja.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Izvršavanje zadataka (napredak korak po korak)** - Za višekorake radne tokove. Model daje plan unaprijed, pripovijeda svaki korak kako ga radi, zatim daje sažetak. Koristite za migracije, implementacije ili bilo koji višekorakni proces.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Lanac-razmišljanja promptanje eksplicitno traži od modela da pokaže svoj proces razmišljanja, poboljšavajući točnost za složene zadatke. Razlaganje korak-po-korak pomaže i ljudima i AI-ju razumjeti logiku.

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chatom:** Pitajte o ovom obrascu:
> - "Kako bih prilagodio obrazac izvršavanja zadataka za dugotrajne operacije?"
> - "Koje su najbolje prakse za strukturiranje predgovora alata u produkcijskim aplikacijama?"
> - "Kako mogu zabilježiti i prikazati međukorake napretka u korisničkom sučelju?"

<img src="../../../translated_images/hr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Obrazac izvršavanja zadataka" width="800"/>

*Plan → Izvršenje → Sažetak radnog toka za višekorake zadatke*

**Kod sa samorefleksijom** - Za generiranje koda proizvodne kvalitete. Model generira kod, provjerava ga prema kriterijima kvalitete i iterativno poboljšava. Koristite za izgradnju novih značajki ili servisa.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/hr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciklus samorefleksije" width="800"/>

*Iterativna petlja poboljšanja - generiraj, procijeni, identificiraj probleme, poboljšaj, ponovi*

**Strukturirana analiza** - Za dosljednu evaluaciju. Model pregledava kod koristeći fiksni okvir (ispravnost, prakse, učinkovitost, sigurnost). Koristite za preglede koda ili ocjenu kvalitete.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chatom:** Pitajte o strukturiranoj analizi:
> - "Kako mogu prilagoditi okvir analize za različite vrste pregleda koda?"
> - "Koji je najbolji način za parsiranje i reagiranje na strukturirani izlaz programatski?"
> - "Kako osigurati dosljedne razine ozbiljnosti kroz različite sesije pregleda?"

<img src="../../../translated_images/hr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Obrazac strukturirane analize" width="800"/>

*Okvir s četiri kategorije za dosljedne preglede koda s razinama ozbiljnosti*

**Višekratni razgovor** - Za razgovore kojima treba kontekst. Model se sjeća prethodnih poruka i gradi na njima. Koristite za interaktivne sesije pomoći ili složena pitanja i odgovore.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/hr/context-memory.dff30ad9fa78832a.webp" alt="Memorija konteksta" width="800"/>

*Kako se kontekst razgovora akumulira tijekom više okretaja dok ne dosegne limit tokena*

**Korak po korak razmišljanje** - Za probleme kojima treba vidljiva logika. Model pokazuje eksplicitno razmišljanje za svaki korak. Koristite za matematičke zadatke, logičke zagonetke ili kad trebate razumjeti proces razmišljanja.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/hr/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Obrazac korak po korak" width="800"/>

*Razbijanje problema na eksplicitne logičke korake*

**Ograničeni izlaz** - Za odgovore sa specifičnim zahtjevima formata. Model strogo slijedi pravila formata i duljine. Koristite za sažetke ili kad vam treba precizna struktura izlaza.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/hr/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Obrazac ograničenog izlaza" width="800"/>

*Primjena specifičnih zahtjeva za format, duljinu i strukturu*

## Korištenje postojećih Azure resursa

**Provjerite implementaciju:**

Provjerite postoji li `.env` datoteka u korijenskom direktoriju s Azure vjerodajnicama (kreirana tijekom Modula 01):
```bash
cat ../.env  # Trebao bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pokrenite aplikaciju:**

> **Napomena:** Ako ste već pokrenuli sve aplikacije koristeći `./start-all.sh` iz Modula 01, ovaj modul već radi na portu 8083. Možete preskočiti naredbe za pokretanje u nastavku i odmah prijeći na http://localhost:8083.

**Opcija 1: Korištenje Spring Boot Dashboarda (preporučeno za korisnike VS Codea)**

Razvojno okruženje uključuje Spring Boot Dashboard ekstenziju, koja pruža vizualno sučelje za upravljanje svim Spring Boot aplikacijama. Možete je pronaći u Activity Baru na lijevoj strani VS Codea (potražite Spring Boot ikonu).
Iz Spring Boot nadzorne ploče možete:
- Vidjeti sve dostupne Spring Boot aplikacije u radnom prostoru
- Pokrenuti/zaustaviti aplikacije jednim klikom
- Pratiti zapise aplikacije u stvarnom vremenu
- Pratiti status aplikacije

Jednostavno kliknite gumb za reprodukciju pokraj "prompt-engineering" da pokrenete ovaj modul ili pokrenite sve module odjednom.

<img src="../../../translated_images/hr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

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
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

Obje skripte automatski učitavaju varijable okoline iz glavne `.env` datoteke i gradit će JAR-ove ako ne postoje.

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

Otvorite http://localhost:8083 u vašem pregledniku.

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

## Snimke zaslona aplikacije

<img src="../../../translated_images/hr/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Glavna nadzorna ploča koja prikazuje svih 8 obrazaca prompt inženjeringa s njihovim karakteristikama i primjenama*

## Istraživanje obrazaca

Web sučelje vam omogućuje eksperimentiranje s različitim strategijama promptanja. Svaki obrazac rješava različite probleme - isprobajte ih da vidite kada koja metoda najbolje funkcionira.

### Niska i visoka želja (Eagerness)

Postavite jednostavno pitanje poput "Koliko je 15% od 200?" koristeći Nisku želju. Dobit ćete trenutno, izravno rješenje. Sada postavite nešto složeno poput "Dizajniraj strategiju keširanja za API s velikim prometom" koristeći Visoku želju. Promatrajte kako model usporava i daje detaljno obrazloženje. Isti model, ista struktura pitanja - ali prompt mu govori koliko razmišljanja treba uložiti.

<img src="../../../translated_images/hr/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Brzi izračun s minimalnim razlozima*

<img src="../../../translated_images/hr/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Opsežna strategija keširanja (2.8MB)*

### Izvršavanje zadataka (Predlošci alata)

Višestupanjski radni procesi imaju koristi od prethodnog planiranja i naracije napretka. Model opisuje što će napraviti, pripovijeda svaki korak, pa zatim sažima rezultate.

<img src="../../../translated_images/hr/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Izrada REST krajnje točke s naracijom korak-po-korak (3.9MB)*

### Samoreflektirajući kod

Isprobajte "Napravite servis za validaciju e-pošte". Umjesto da samo generira kod i stane, model generira, procjenjuje prema kriterijima kvalitete, identificira slabosti i poboljšava. Vidjet ćete kako iterira dok kod ne zadovolji proizvodne standarde.

<img src="../../../translated_images/hr/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Kompletan servis za validaciju e-pošte (5.2MB)*

### Strukturirana analiza

Pregledi koda zahtijevaju dosljedne okvire evaluacije. Model analizira kod koristeći fiksne kategorije (ispravnost, prakse, performanse, sigurnost) s razinama težine.

<img src="../../../translated_images/hr/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Pregled koda temeljen na okvire*

### Višekratni razgovor

Pitajte "Što je Spring Boot?" zatim odmah nastavite s "Pokaži mi primjer". Model pamti vaše prvo pitanje i daje vam specifičan primjer Spring Boot-a. Bez memorije, drugo pitanje bilo bi previše nejasno.

<img src="../../../translated_images/hr/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Čuvanje konteksta kroz pitanja*

### Razmišljanje korak po korak

Odaberite matematički problem i isprobajte ga s obje metode - Razmišljanje korak po korak i Niska želja. Niska želja daje samo odgovor - brzo ali nejasno. Razmišljanje korak po korak pokazuje svaki izračun i odluku.

<img src="../../../translated_images/hr/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matematički problem s eksplicitnim koracima*

### Ograničeni izlaz

Kada trebate specifične formate ili broj riječi, ovaj obrazac osigurava strogo pridržavanje pravila. Isprobajte generiranje sažetka s točno 100 riječi u obliku nabrajanja.

<img src="../../../translated_images/hr/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Sažetak strojskog učenja s kontrolom formata*

## Što zapravo učite

**Ulaganje razmišljanja mijenja sve**

GPT-5.2 vam omogućuje upravljanje računalnim naporom kroz vaše upite. Niski napor znači brze odgovore s minimalnim istraživanjem. Visoki napor znači da model uzima vremena za duboko razmišljanje. Učite uskladiti napor s složenošću zadatka - ne gubite vrijeme na jednostavna pitanja, ali ni ne žurite s kompleksnim odlukama.

**Struktura usmjerava ponašanje**

Primjećujete li XML oznake u promptima? Nisu samo ukrasne. Modeli pouzdanije slijede strukturirane upute nego slobodni tekst. Kada trebate višestupanjske procese ili složenu logiku, struktura pomaže modelu pratiti gdje se nalazi i što slijedi.

<img src="../../../translated_images/hr/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomija dobro strukturiranog prompta s jasnim sekcijama i XML-organizacijom*

**Kvaliteta kroz samo-evaluaciju**

Samoreflektirajući obrasci djeluju tako da jasno definiraju kriterije kvalitete. Umjesto da se nadaju da model "ispravno radi", kažete mu točno što "ispravno" znači: ispravna logika, rukovanje pogreškama, performanse, sigurnost. Model zatim može evaluirati vlastiti izlaz i poboljšati ga. Time generiranje koda postaje proces, a ne lutrija.

**Kontekst je ograničen**

Višekratni razgovori funkcioniraju tako što uključuju povijest poruka sa svakim zahtjevom. No postoji limit - svaki model ima maksimalan broj tokena. Kako razgovori rastu, trebate strategije za zadržavanje relevantnog konteksta bez prelaska granice. Ovaj modul vam pokazuje kako memorija funkcionira; kasnije ćete naučiti kada sažeti, kada zaboraviti i kada dohvatiti.

## Sljedeći koraci

**Sljedeći modul:** [03-rag - RAG (Generiranje potpomognuto pretraživanjem)](../03-rag/README.md)

---

**Navigacija:** [← Prethodni: Modul 01 - Uvod](../01-introduction/README.md) | [Natrag na početak](../README.md) | [Sljedeći: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Odricanje od odgovornosti**:  
Ovaj dokument je preveden pomoću AI prevoditeljskog servisa [Co-op Translator](https://github.com/Azure/co-op-translator). Iako težimo točnosti, imajte na umu da automatizirani prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na njegovom izvornom jeziku treba smatrati autoritativnim izvorom. Za važne informacije preporučuje se profesionalni ljudski prijevod. Ne snosimo odgovornost za bilo kakve nesporazume ili pogrešna tumačenja koja proizlaze iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->