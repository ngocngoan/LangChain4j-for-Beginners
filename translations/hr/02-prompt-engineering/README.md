# Modul 02: Prompt Engineering s GPT-5.2

## Sadržaj

- [Što ćete naučiti](../../../02-prompt-engineering)
- [Preduvjeti](../../../02-prompt-engineering)
- [Razumijevanje Prompt Engineeringa](../../../02-prompt-engineering)
- [Osnove Prompt Engineeringa](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Napredni obrasci](../../../02-prompt-engineering)
- [Korištenje postojećih Azure resursa](../../../02-prompt-engineering)
- [Snimke zaslona aplikacije](../../../02-prompt-engineering)
- [Istraživanje obrazaca](../../../02-prompt-engineering)
  - [Niska vs Visoka Volja](../../../02-prompt-engineering)
  - [Izvršavanje zadataka (predlošci alata)](../../../02-prompt-engineering)
  - [Kod koji samoreflektira](../../../02-prompt-engineering)
  - [Strukturirana analiza](../../../02-prompt-engineering)
  - [Višekratni razgovor](../../../02-prompt-engineering)
  - [Razumovanje korak po korak](../../../02-prompt-engineering)
  - [Ograničeni izlaz](../../../02-prompt-engineering)
- [Što zaista učite](../../../02-prompt-engineering)
- [Sljedeći koraci](../../../02-prompt-engineering)

## Što ćete naučiti

<img src="../../../translated_images/hr/what-youll-learn.c68269ac048503b2.webp" alt="Što ćete naučiti" width="800"/>

U prethodnom modulu vidjeli ste kako memorija omogućava konverzacijski AI i upotrijebili GitHub modele za osnovne interakcije. Sada ćemo se fokusirati na način na koji postavljate pitanja — same upite (prompte) — koristeći Azure OpenAI GPT-5.2. Način na koji strukturirate svoje promptove dramatično utječe na kvalitetu odgovora koje dobijete. Počinjemo pregledom osnovnih tehnika promptiranja, a zatim prelazimo na osam naprednih obrazaca koji u potpunosti koriste mogućnosti GPT-5.2.

Koristit ćemo GPT-5.2 jer uvodi kontrolu razmišljanja - možete modelu reći koliko razmišljanja treba napraviti prije odgovora. To čini različite strategije promptiranja jasnijima i pomaže vam razumjeti kada koristiti koji pristup. Također ćemo imati koristi od manjeg broja ograničenja učestalosti za GPT-5.2 u usporedbi s GitHub modelima.

## Preduvjeti

- Završeni Modul 01 (postavljeni Azure OpenAI resursi)
- `.env` datoteka u korijenskom direktoriju s Azure vjerodajnicama (kreirana pomoću `azd up` u Modulu 01)

> **Napomena:** Ako niste završili Modul 01, prvo slijedite upute za postavljanje tamo.

## Razumijevanje Prompt Engineeringa

<img src="../../../translated_images/hr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Što je Prompt Engineering?" width="800"/>

Prompt engineering je dizajniranje ulaznog teksta koji vam dosljedno daje rezultate koje trebate. Nije samo pitanje postavljanja pitanja - radi se o strukturi zahtjeva tako da model točno razumije što želite i kako to isporučiti.

Zamislite to kao davanje uputa kolegi. "Popravi grešku" je nejasno. "Popravi null pointer exception u UserService.java na liniji 45 dodavanjem provjere null vrijednosti" je specifično. Jezični modeli funkcioniraju na isti način - specifičnost i struktura su važni.

<img src="../../../translated_images/hr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kako se uklapa LangChain4j" width="800"/>

LangChain4j pruža infrastrukturu — veze s modelom, memoriju i vrste poruka — dok su obrasci promptova samo pažljivo strukturirani tekst koji šaljete kroz tu infrastrukturu. Ključni dijelovi su `SystemMessage` (koji postavlja ponašanje i ulogu AI-a) i `UserMessage` (koji nosi vaš stvarni zahtjev).

## Osnove Prompt Engineeringa

<img src="../../../translated_images/hr/five-patterns-overview.160f35045ffd2a94.webp" alt="Pregled pet obrasca Prompt Engineeringa" width="800"/>

Prije nego što zaronimo u napredne obrasce u ovom modulu, pogledajmo pet temeljnih tehnika promptiranja. To su gradivni blokovi koje bi svaki prompt inženjer trebao znati. Ako ste već prošli kroz [Quick Start modula](../00-quick-start/README.md#2-prompt-patterns), već ste ih vidjeli u praksi — ovo je konceptualni okvir iza njih.

### Zero-Shot Prompting

Najjednostavniji pristup: dajte modelu izravnu uputu bez primjera. Model se u potpunosti oslanja na svoje treniranje da razumije i izvrši zadatak. Ovo dobro funkcionira za jednostavne zahtjeve gdje je očekivano ponašanje očito.

<img src="../../../translated_images/hr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Izravna uputa bez primjera — model zaključuje zadatak samo iz upute*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odgovor: "Pozitivno"
```

**Kada koristiti:** Jednostavne klasifikacije, direktna pitanja, prijevodi ili bilo koji zadatak koji model može obaviti bez dodatnih uputa.

### Few-Shot Prompting

Dajte primjere koji pokazuju obrazac koji želite da model slijedi. Model uči očekivani format ulaza i izlaza iz vaših primjera i primjenjuje ga na nove ulaze. Ovo dramatično poboljšava dosljednost za zadatke gdje format ili ponašanje nije očito.

<img src="../../../translated_images/hr/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Učenje iz primjera — model prepoznaje obrazac i primjenjuje ga na nove primjere*

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

**Kada koristiti:** Prilagođene klasifikacije, dosljedno formatiranje, domenski specifični zadaci ili kad su rezultati zero-shot promptinga nedosljedni.

### Chain of Thought

Zatražite od modela da pokaže svoje razmišljanje korak po korak. Umjesto da odmah skoči na odgovor, model razlaže problem i eksplicitno prolazi svaki dio. Ovo poboljšava točnost za zadatke iz matematike, logike i višekoračnog razmišljanja.

<img src="../../../translated_images/hr/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Razmišljanje korak po korak — razlaganje složenih problema na logičke korake*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model pokazuje: 15 - 8 = 7, zatim 7 + 12 = 19 jabuka
```

**Kada koristiti:** Matematički problemi, logičke zagonetke, otklanjanje pogrešaka ili bilo koji zadatak gdje prikazivanje razmišljanja poboljšava točnost i pouzdanost.

### Role-Based Prompting

Postavite osobu ili ulogu za AI prije nego što postavite pitanje. To pruža kontekst koji oblikuje ton, dubinu i fokus odgovora. "Software architect" daje drukčiji savjet od "junior developer-a" ili "security auditor-a".

<img src="../../../translated_images/hr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Postavljanje konteksta i osobe — isto pitanje dobiva različit odgovor ovisno o dodijeljenoj ulozi*

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

**Kada koristiti:** Pregledi koda, podučavanje, domenski specifične analize ili kad trebate odgovore prilagođene određenim razinama stručnosti ili perspektivama.

### Prompt Templates

Kreirajte ponovo upotrebljive promptove s varijabilnim rezerviranim mjestima. Umjesto da svaki put pišete novi prompt, definirajte predložak jednom i ispunite različite vrijednosti. LangChain4j-ova klasa `PromptTemplate` to olakšava sa sintaksom `{{variable}}`.

<img src="../../../translated_images/hr/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Ponovno upotrebljivi promptovi s varijabilnim rezerviranim mjestima — jedan predložak, mnoge primjene*

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

**Kada koristiti:** Ponavljani upiti s različitim ulazima, serijska obrada, izrada ponovo upotrebljivih AI tijekova rada ili bilo koji scenarij gdje se struktura prompta ne mijenja, ali se podaci mijenjaju.

---

Ovih pet osnova daje vam snažan alat za većinu prompt zadataka. Preostali dio ovog modula gradi se na njima kroz **osam naprednih obrazaca** koji iskorištavaju kontrolu razmišljanja, samoocjenu i strukturirani izlaz GPT-5.2.

## Napredni obrasci

Kad smo pokrili osnove, prijeđimo na osam naprednih obrazaca koji ovaj modul čine jedinstvenim. Ne svi problemi zahtijevaju isti pristup. Neka pitanja trebaju brze odgovore, druga duboko razmišljanje. Neka zahtijevaju vidljivo razmišljanje, a neka samo rezultate. Svaki od dolje navedenih obrazaca optimiziran je za drugačiji scenarij — a kontrola razmišljanja GPT-5.2 razlike čini još izraženijima.

<img src="../../../translated_images/hr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osam obrazaca promptiranja" width="800"/>

*Pregled osam prompt engineering obrazaca i njihovih primjena*

<img src="../../../translated_images/hr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontrola razmišljanja s GPT-5.2" width="800"/>

*Kontrola razmišljanja GPT-5.2 omogućuje precizno određivanje koliko razmišljanja model treba napraviti — od brzih direktnih odgovora do dubokih istraživanja*

<img src="../../../translated_images/hr/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Usporedba napora razmišljanja" width="800"/>

*Niska volja (brzo, direktno) vs Visoka volja (temeljito, istraživačko) pristupi razmišljanju*

**Niska Volja (Brzo & Fokusirano)** - Za jednostavna pitanja gdje želite brze, direktne odgovore. Model radi minimalno razmišljanja - najviše 2 koraka. Koristite ovo za izračune, pretraživanja ili jednostavna pitanja.

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Istražite s GitHub Copilot:** Otvorite [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) i pitajte:
> - "Koja je razlika između obrazaca s niskom i visokom voljom promptiranja?"
> - "Kako XML oznake u promptovima pomažu strukturirati AI odgovor?"
> - "Kada koristiti obrasce samo-refleksije nasuprot direktnim uputama?"

**Visoka Volja (Duboko & Temeljito)** - Za složene probleme gdje želite sveobuhvatnu analizu. Model istražuje temeljito i prikazuje detaljno razmišljanje. Koristite ovo za dizajn sustava, arhitektonske odluke ili kompleksno istraživanje.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Izvršavanje zadataka (napredak korak po korak)** - Za višekorake radne tokove. Model pruža unaprijed plan, opisuje svaki korak dok radi, a potom daje sažetak. Koristite ovo za migracije, implementacije ili bilo koji višekorak proces.

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought promptiranje eksplicitno traži od modela da pokaže svoj proces razmišljanja, poboljšavajući točnost kod složenih zadataka. Razlaganje korak po korak pomaže ljudima i AI-ju shvatiti logiku.

> **🤖 Probajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Pitajte o ovom obrascu:
> - "Kako bih prilagodio obrazac izvršavanja zadatka za dugotrajne operacije?"
> - "Koje su najbolje prakse za strukturiranje predložaka alata u produkcijskim aplikacijama?"
> - "Kako mogu snimati i prikazivati privremena ažuriranja napretka u korisničkom sučelju?"

<img src="../../../translated_images/hr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Obrazac izvršavanja zadatka" width="800"/>

*Radni tijek Planiraj → Izvrši → Sažmi za višekorake zadatke*

**Kod koji samoreflektira** - Za generiranje koda produkcijske kvalitete. Model generira kod koji prati produkcijske standarde s propisnim upravljanjem pogreškama. Koristite ovo kod izgradnje novih značajki ili servisa.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/hr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciklus samo-refleksije" width="800"/>

*Iterativni ciklus poboljšanja - generiraj, evaluiraj, identificiraj probleme, poboljšaj, ponovi*

**Strukturirana analiza** - Za konzistentnu evaluaciju. Model pregledava kod koristeći fiksirani okvir (ispravnost, prakse, performanse, sigurnost, održivost). Koristite ovo za recenzije koda ili procjene kvalitete.

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Probajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Pitajte o strukturiranoj analizi:
> - "Kako mogu prilagoditi okvir analize za različite vrste recenzija koda?"
> - "Koji je najbolji način da se programatski parsira i djeluje na strukturirani izlaz?"
> - "Kako osigurati dosljedne razine ozbiljnosti kroz različite sesije pregleda?"

<img src="../../../translated_images/hr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Obrazac strukturirane analize" width="800"/>

*Okvir za dosljedne recenzije koda s razinama ozbiljnosti*

**Višekratni razgovor** - Za razgovore kojima je potreban kontekst. Model se sjeća prethodnih poruka i nadograđuje na njih. Koristite ovo za interaktivne sesije pomoći ili kompleksna pitanja i odgovore.

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

*Kako se kontekst razgovora akumulira kroz više krugova dok ne dosegne ograničenje tokena*

**Razumovanje korak po korak** - Za probleme koji zahtijevaju vidljivu logiku. Model prikazuje eksplicitno razmišljanje za svaki korak. Koristite ovo za matematičke zadatke, logičke zagonetke ili kada trebate razumjeti proces razmišljanja.

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

*Razlaganje problema na eksplicitne logičke korake*

**Ograničeni izlaz** - Za odgovore s posebnim zahtjevima formata. Model striktno slijedi pravila formata i dužine. Koristite ovo za sažetke ili kad trebate preciznu strukturu izlaza.

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

*Provjera specifičnih zahtjeva za format, dužinu i strukturu*

## Korištenje postojećih Azure resursa

**Provjerite postavljanje:**

Provjerite postoji li `.env` datoteka u korijenskom direktoriju s Azure vjerodajnicama (kreirana tijekom Modula 01):
```bash
cat ../.env  # Trebalo bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pokrenite aplikaciju:**

> **Napomena:** Ako ste već pokrenuli sve aplikacije koristeći `./start-all.sh` iz Modula 01, ovaj modul već radi na portu 8083. Možete preskočiti naredbe za pokretanje u nastavku i otići izravno na http://localhost:8083.

**Opcija 1: Korištenje Spring Boot Dashboarda (preporučeno za VS Code korisnike)**

Development container uključuje Spring Boot Dashboard ekstenziju, koja pruža vizualno sučelje za upravljanje svim Spring Boot aplikacijama. Možete ga pronaći u Activity Baru na lijevoj strani VS Code-a (potražite Spring Boot ikonu).
Iz Spring Boot kontrolne ploče možete:
- Vidjeti sve dostupne Spring Boot aplikacije u radnom okruženju
- Pokrenuti/zaustaviti aplikacije jednim klikom
- Prikazati dnevnike aplikacija u stvarnom vremenu
- Pratiti status aplikacije

Jednostavno kliknite gumb za reprodukciju pored "prompt-engineering" da pokrenete ovaj modul, ili pokrenite sve module odjednom.

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

Obje skripte automatski učitavaju varijable okruženja iz root `.env` datoteke i izgradit će JAR-ove ako ne postoje.

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

*Glavna kontrolna ploča koja prikazuje svih 8 uzoraka prompt inženjeringa sa njihovim karakteristikama i slučajevima upotrebe*

## Istraživanje uzoraka

Web sučelje omogućuje vam eksperimentiranje s različitim strategijama promptanja. Svaki uzorak rješava različite probleme - isprobajte ih da vidite kada koji pristup daje najbolje rezultate.

### Niska vs Visoka Žudnja

Postavite jednostavno pitanje poput "Koliko je 15% od 200?" koristeći Nisku Žudnju. Dobit ćete trenutni, direktan odgovor. Sada postavite nešto složenije poput "Dizajniraj strategiju keširanja za API s velikim prometom" koristeći Visoku Žudnju. Pogledajte kako se model usporava i pruža detaljno obrazloženje. Isti model, ista struktura pitanja - ali prompt mu govori koliko detaljno treba razmišljati.

<img src="../../../translated_images/hr/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Brza računica s minimalnim razmišljanjem*

<img src="../../../translated_images/hr/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Sveobuhvatna strategija keširanja (2.8MB)*

### Izvršenje zadatka (Preambule alata)

Višestepeni radni tokovi imaju koristi od planiranja unaprijed i naracije napretka. Model navodi što će učiniti, opisuje svaki korak, a zatim sažima rezultate.

<img src="../../../translated_images/hr/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Stvaranje REST endpointa s korak-po-korak naracijom (3.9MB)*

### Samoreflektirajući kod

Isprobajte "Napravi servis za validaciju email adrese". Umjesto da samo generira kod i stane, model generira, evaluira prema kriterijima kvalitete, identificira slabosti i poboljšava. Vidjet ćete kako iterira dok kod ne zadovolji proizvodne standarde.

<img src="../../../translated_images/hr/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Kompletan servis za validaciju emaila (5.2MB)*

### Strukturirana analiza

Pregledi koda trebaju dosljedne okvire evaluacije. Model analizira kod koristeći fiksne kategorije (točnost, praksa, performanse, sigurnost) sa stupnjevima težine.

<img src="../../../translated_images/hr/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Pregled koda temeljen na okviru*

### Višekratni dijalog

Pitajte "Što je Spring Boot?" zatim odmah nastavite sa "Pokaži mi primjer". Model pamti vaše prvo pitanje i daje vam primjer Spring Boota posebno. Bez memorije, to drugo pitanje bilo bi previše neodređeno.

<img src="../../../translated_images/hr/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Očuvanje konteksta kroz pitanja*

### Rasprava korak po korak

Odaberite matematički zadatak i isprobajte ga s obje metode: Rasprava korak po korak i Niska Žudnja. Niska žudnja vam samo daje odgovor - brzo ali netransparentno. Rasprava korak po korak prikazuje svaki izračun i odluku.

<img src="../../../translated_images/hr/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matematički zadatak s eksplicitnim koracima*

### Ograničeni izlaz

Kad trebate specifične formate ili broj riječi, ovaj uzorak strogo pridržava pravila. Isprobajte generiranje sažetka s točno 100 riječi u točkastom formatu.

<img src="../../../translated_images/hr/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Sažetak strojnog učenja s kontrolom formata*

## Što stvarno učite

**Ulaganje razmišljanja mijenja sve**

GPT-5.2 vam omogućuje kontrolu računalnog napora kroz vaše promptove. Niski napor znači brze odgovore s minimalnom analizom. Visoki napor znači da model uzima vremena za duboko razmišljanje. Učite usklađivati napor s kompleksnošću zadatka - nemojte gubiti vrijeme na jednostavna pitanja, ali ni žuriti sa složenim odlukama.

**Struktura usmjerava ponašanje**

Primijetili ste XML oznake u promptovima? One nisu dekorativne. Modeli pouzdanije prate strukturirane upute nego slobodni tekst. Kad trebate višestepene procese ili složenu logiku, struktura pomaže modelu da zna gdje se nalazi i što slijedi.

<img src="../../../translated_images/hr/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomija dobro strukturiranog prompta s jasno definiranim sekcijama i XML-stil organizacijom*

**Kvaliteta kroz samo-evaluaciju**

Samoreflektirajući uzorci funkcioniraju tako da eksplicitno definiraju kriterije kvalitete. Umjesto da se nadate da "model to radi ispravno", kažete mu točno što znači "ispravno": točna logika, rukovanje greškama, performanse, sigurnost. Model tada može evaluirati vlastiti izlaz i poboljšati se. Time se generiranje koda pretvara iz lutrije u proces.

**Kontekst je ograničen**

Višekratni dijalozi rade uključivanjem povijesti poruka u svaki zahtjev. No postoji granica - svaki model ima maksimalni broj tokena. Kako razgovori rastu, trebate strategije da zadržite relevantan kontekst bez prekoračenja tog limita. Ovaj modul vam pokazuje kako radi memorija; kasnije ćete naučiti kada sažimati, kada zaboraviti i kada dohvaćati.

## Sljedeći koraci

**Sljedeći modul:** [03-rag - RAG (Generiranje uz pojačanje dohvaćanja)](../03-rag/README.md)

---

**Navigacija:** [← Prethodno: Modul 01 - Uvod](../01-introduction/README.md) | [Natrag na početak](../README.md) | [Sljedeće: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Odricanje od odgovornosti**:
Ovaj dokument je preveden korištenjem AI prevoditeljskog servisa [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo postići točnost, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na njegovom izvornom jeziku treba se smatrati autoritativnim izvorom. Za ključne informacije preporučuje se profesionalni prijevod od strane ljudskog prevoditelja. Ne snosimo odgovornost za bilo kakva nesporazume ili pogrešne interpretacije koje proizlaze iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->