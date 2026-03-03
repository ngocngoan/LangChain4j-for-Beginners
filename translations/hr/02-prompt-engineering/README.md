# Modul 02: Inženjering prompta s GPT-5.2

## Sadržaj

- [Video vodič](../../../02-prompt-engineering)
- [Što ćete naučiti](../../../02-prompt-engineering)
- [Preduvjeti](../../../02-prompt-engineering)
- [Razumijevanje inženjeringa prompta](../../../02-prompt-engineering)
- [Osnove inženjeringa prompta](../../../02-prompt-engineering)
  - [Zero-Shot promptiranje](../../../02-prompt-engineering)
  - [Few-Shot promptiranje](../../../02-prompt-engineering)
  - [Lanac misli](../../../02-prompt-engineering)
  - [Promptiranje temeljeno na ulozi](../../../02-prompt-engineering)
  - [Predlošci prompta](../../../02-prompt-engineering)
- [Napredni obrasci](../../../02-prompt-engineering)
- [Pokretanje aplikacije](../../../02-prompt-engineering)
- [Snimke zaslona aplikacije](../../../02-prompt-engineering)
- [Istraživanje obrazaca](../../../02-prompt-engineering)
  - [Niska nasuprot visokoj žustrini](../../../02-prompt-engineering)
  - [Izvršavanje zadatka (prolog za alate)](../../../02-prompt-engineering)
  - [Kod za samorefleksiju](../../../02-prompt-engineering)
  - [Strukturirana analiza](../../../02-prompt-engineering)
  - [Višekratni razgovor](../../../02-prompt-engineering)
  - [Korak po korak rezoniranje](../../../02-prompt-engineering)
  - [Ograničeni izlaz](../../../02-prompt-engineering)
- [Što zapravo učite](../../../02-prompt-engineering)
- [Sljedeći koraci](../../../02-prompt-engineering)

## Video vodič

Pogledajte ovu live sesiju koja objašnjava kako započeti s ovim modulom:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Inženjering prompta s LangChain4j - live sesija" width="800"/></a>

## Što ćete naučiti

Sljedeći dijagram pruža pregled ključnih tema i vještina koje ćete razviti u ovom modulu — od tehnika rafiniranja prompta do korak-po-korak tijeka rada koji ćete pratiti.

<img src="../../../translated_images/hr/what-youll-learn.c68269ac048503b2.webp" alt="Što ćete naučiti" width="800"/>

U prethodnim modulima istražili ste osnovne interakcije LangChain4j s GitHub modelima i vidjeli kako memorija omogućuje konverzacijski AI s Azure OpenAI. Sada ćemo se usredotočiti na način na koji postavljate pitanja — same promptove — koristeći Azure OpenAI GPT-5.2. Način na koji strukturirate svoje promptove drastično utječe na kvalitetu odgovora koje dobijete. Počinjemo pregledom osnovnih tehnika promptiranja, a zatim prelazimo na osam naprednih obrazaca koji u potpunosti koriste mogućnosti GPT-5.2.

Koristit ćemo GPT-5.2 jer uvodi kontrolu rezoniranja - možete modelu reći koliko razmišljanja da obavi prije odgovora. Ovo čini različite strategije promptiranja jasnijima i pomaže vam razumjeti kada koristiti koji pristup. Također ćemo imati koristi od manjih ograničenja stope u Azure-u za GPT-5.2 u usporedbi s GitHub modelima.

## Preduvjeti

- Završeni Modul 01 (Azure OpenAI resursi postavljeni)
- `.env` datoteka u korijenskom direktoriju s Azure vjerodajnicama (kreirana pomoću `azd up` u Modulu 01)

> **Napomena:** Ako niste završili Modul 01, prvo slijedite upute za postavljanje tamo.

## Razumijevanje inženjeringa prompta

U svojoj srži, inženjering prompta je razlika između nejasnih uputa i preciznih, što donji primjer ilustrira.

<img src="../../../translated_images/hr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Što je inženjering prompta?" width="800"/>

Inženjering prompta odnosi se na dizajniranje ulaznog teksta koji dosljedno daje rezultate koje trebate. Nije samo pitanje postavljanja pitanja - riječ je o strukturiranju zahtjeva tako da model točno razumije što želite i kako vam to dostaviti.

Zamislite da dajete upute kolegi. "Popravi bug" je nejasno. "Popravi iznimku null pointer u UserService.java, red 45, dodajući provjeru na null" je specifično. Jezični modeli rade na isti način - specifičnost i struktura su bitni.

Dijagram ispod prikazuje kako se LangChain4j uklapa u ovu sliku — povezujući vaše obrasce prompta s modelom kroz gradivne blokove SystemMessage i UserMessage.

<img src="../../../translated_images/hr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kako se LangChain4j uklapa" width="800"/>

LangChain4j pruža infrastrukturu — veze modela, memoriju i vrste poruka — dok su obrasci prompta samo pažljivo strukturirani tekst koji šaljete kroz tu infrastrukturu. Ključni gradivni blokovi su `SystemMessage` (koji postavlja ponašanje i ulogu AI-a) i `UserMessage` (koja nosi vaš stvarni zahtjev).

## Osnove inženjeringa prompta

Pet osnovnih tehnika prikazanih ispod čine temelj učinkovite izrade prompta. Svaka pokriva različit aspekt načina komunikacije s jezičnim modelima.

<img src="../../../translated_images/hr/five-patterns-overview.160f35045ffd2a94.webp" alt="Pregled pet obrazaca inženjeringa prompta" width="800"/>

Prije nego što u ovom modulu prijeđemo na napredne obrasce, pregledajmo pet temeljnih tehnika promptiranja. To su gradivni blokovi koje svaki inženjer prompta treba poznavati. Ako ste već radili kroz [modul brzog početka](../00-quick-start/README.md#2-prompt-patterns), već ste ih vidjeli u akciji — evo konceptualnog okvira iza njih.

### Zero-Shot promptiranje

Najjednostavniji pristup: dajte modelu izravnu uputu bez primjera. Model se u potpunosti oslanja na svoje treniranje da razumije i izvrši zadatak. Ovo dobro funkcionira za jednostavne zahtjeve gdje je očekivano ponašanje očito.

<img src="../../../translated_images/hr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot promptiranje" width="800"/>

*Izravna uputa bez primjera — model zaključuje zadatak samo iz upute*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odgovor: "Pozitivno"
```

**Kada koristiti:** Jednostavne klasifikacije, izravna pitanja, prijevodi ili bilo koji zadatak koji model može obraditi bez dodatnih uputa.

### Few-Shot promptiranje

Pružite primjere koji demonstriraju obrazac koji želite da model slijedi. Model uči očekivani format ulaz-izlaz iz vaših primjera i primjenjuje ga na nove ulaze. Ovo dramatično poboljšava konzistentnost za zadatke gdje željeni format ili ponašanje nije očito.

<img src="../../../translated_images/hr/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot promptiranje" width="800"/>

*Učenje iz primjera — model identificira obrazac i primjenjuje ga na nove ulaze*

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

**Kada koristiti:** Prilagođene klasifikacije, dosljedno formatiranje, zadaci specifični za domenu ili kada su rezultati zero-shot promptiranja nekonzistentni.

### Lanac misli

Zatražite od modela da pokaže svoje rezoniranje korak-po-korak. Umjesto da preskoči direktno na odgovor, model razlaže problem i razrađuje svaki dio eksplicitno. Ovo poboljšava točnost kod matematičkih, logičkih i višestepenih razmišljanja.

<img src="../../../translated_images/hr/chain-of-thought.5cff6630e2657e2a.webp" alt="Promptiranje lanca misli" width="800"/>

*Rezoniranje korak po korak — razbijanje složenih problema u eksplicitne logičke korake*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model prikazuje: 15 - 8 = 7, zatim 7 + 12 = 19 jabuka
```

**Kada koristiti:** Matematički problemi, logičke zagonetke, debugiranje ili bilo koji zadatak gdje pokazivanje procesa rezoniranja povećava točnost i povjerenje.

### Promptiranje temeljeno na ulozi

Postavite personu ili ulogu AI-u prije nego što postavite pitanje. Ovo pruža kontekst koji oblikuje ton, dubinu i fokus odgovora. "Softverski arhitekt" daje drukčiji savjet od "mlađeg developera" ili "auditor sigurnosti".

<img src="../../../translated_images/hr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Promptiranje temeljeno na ulozi" width="800"/>

*Postavljanje konteksta i persone — isto pitanje daje različite odgovore ovisno o dodijeljenoj ulozi*

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

**Kada koristiti:** Pregledi koda, podučavanje, analiza specifična za domenu ili kada trebate odgovore prilagođene određenoj razini stručnosti ili perspektivi.

### Predlošci prompta

Napravite višekratno upotrebljive promtove s varijabilnim mjestima za unos. Umjesto da svaki put pišete novi prompt, definirajte predložak jednom i ispunjavajte različite vrijednosti. LangChain4j klasa `PromptTemplate` to olakšava s `{{variable}}` sintaksom.

<img src="../../../translated_images/hr/prompt-templates.14bfc37d45f1a933.webp" alt="Predlošci prompta" width="800"/>

*Višekratno upotrebljivi promptovi s varijabilnim mjestima — jedan predložak, mnoge namjene*

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

**Kada koristiti:** Ponavljani upiti s različitim ulazima, paketna obrada, izgradnja višekratno upotrebljivih AI tijekova rada ili bilo koji scenarij gdje struktura prompta ostaje ista, ali se podaci mijenjaju.

---

Ovih pet temelja daje vam solidan alat za većinu zadataka promptiranja. Ostatak ovog modula gradi na njima s **osam naprednih obrazaca** koji iskorištavaju GPT-5.2-ove sposobnosti kontrole rezoniranja, samoocjenjivanja i strukturiranog izlaza.

## Napredni obrasci

S osnovama pokrivenima, prijeđimo na osam naprednih obrazaca koji ovaj modul čine jedinstvenim. Nisu svi problemi jednaki. Neka pitanja zahtijevaju brze odgovore, druga duboko razmišljanje. Neka zahtijevaju vidljivo rezoniranje, a druga samo rezultate. Svaki obrazac niže je optimiziran za drugačiji scenarij — i GPT-5.2 kontrola rezoniranja čini razlike još izraženijima.

<img src="../../../translated_images/hr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osam obrazaca promptiranja" width="800"/>

*Pregled osam obrazaca inženjeringa prompta i njihovih primjena*

GPT-5.2 dodaje još jednu dimenziju ovim obrascima: *kontrolu rezoniranja*. Klizač ispod pokazuje kako možete prilagoditi količinu razmišljanja modela — od brzih, izravnih do dubokih, temeljitih analiza.

<img src="../../../translated_images/hr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontrola rezoniranja s GPT-5.2" width="800"/>

*GPT-5.2 kontrola rezoniranja vam omogućuje da odredite koliko razmišljanja model treba napraviti — od brzih izravnih odgovora do dubokih istraživanja*

**Niska žustrina (brzo i fokusirano)** - Za jednostavna pitanja gdje želite brze, izravne odgovore. Model minimalno rezonira - maksimalno 2 koraka. Koristite za izračune, pretrage ili jednostavna pitanja.

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

> 💡 **Istražite s GitHub Copilotom:** Otvorite [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) i pitajte:
> - "Koja je razlika između obrazaca promptiranja niske i visoke žustrine?"
> - "Kako XML oznake u promptovima pomažu strukturirati AI odgovor?"
> - "Kada bih trebao koristiti obrasce samorefleksije, a kada direktne upute?"

**Visoka žustrina (duboko i temeljito)** - Za složene probleme gdje želite podrobnu analizu. Model temeljito razmatra i prikazuje detaljno rezoniranje. Koristite za dizajn sustava, arhitektonske odluke ili kompleksna istraživanja.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Izvršavanje zadatka (napredak korak-po-korak)** - Za tijekove rada s više koraka. Model daje početni plan, pripovijeda svaki korak dok ih izvršava, zatim daje sažetak. Koristite za migracije, implementacije ili bilo koji višekoračni proces.

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

Lanac misli promptiranje eksplicitno traži od modela da prikaže proces rezoniranja, što poboljšava preciznost za složene zadatke. Razbijanje korak-po-korak pomaže ljudima i AI-u da razumiju logiku.

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) chatom:** Pitajte o ovom obrascu:
> - "Kako bih prilagodio obrazac izvršavanja zadatka za dugotrajne operacije?"
> - "Koje su najbolje prakse za strukturiranje prologa alata u produkcijskim aplikacijama?"
> - "Kako mogu snimiti i prikazati međukorake napretka u korisničkom sučelju?"

Dijagram ispod ilustrira ovaj tijek rada Plan → Izvrši → Sažmi.

<img src="../../../translated_images/hr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Obrazac izvršavanja zadatka" width="800"/>

*Tijek rada Plan → Izvrši → Sažmi za višestepene zadatke*

**Kod za samorefleksiju** - Za generiranje koda kvalitete pogodnog za produkciju. Model generira kod koji slijedi produkcijske standarde s odgovarajućim rukovanjem greškama. Koristite kada gradite nove značajke ili servise.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

Dijagram ispod prikazuje ovaj iterativni ciklus poboljšanja — generiraj, ocijeni, identificiraj slabosti, usavrši dok kod ne zadovolji produkcijske standarde.

<img src="../../../translated_images/hr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciklus samorefleksije" width="800"/>

*Iterativni ciklus poboljšanja - generiraj, ocijeni, prepoznaj probleme, poboljšaj, ponovi*

**Strukturirana analiza** - Za dosljednu evaluaciju. Model pregledava kod koristeći fiksni okvir (ispravnost, prakse, izvedba, sigurnost, održivost). Koristite za preglede koda ili ocjenu kvalitete.

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

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) chatom:** Pitajte o strukturiranoj analizi:
> - "Kako mogu prilagoditi analitički okvir za različite vrste pregleda koda?"
> - "Koji je najbolji način za parsiranje i programsku obradu strukturiranog izlaza?"
> - "Kako osigurati dosljedne razine težine kroz različite revizijske sesije?"

Dijagram u nastavku prikazuje kako ovaj strukturirani okvir organizira pregled koda u dosljedne kategorije s razinama težine.

<img src="../../../translated_images/hr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Obrazac strukturirane analize" width="800"/>

*Okvir za dosljedne preglede koda s razinama težine*

**Višekratni razgovor** - Za razgovore kojima treba kontekst. Model pamti prethodne poruke i gradi na njima. Koristite za interaktivne pomoći ili složena pitanja i odgovore.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

Dijagram ispod vizualizira kako se kontekst razgovora nakuplja s svakim okretom i kako se to odnosi na ograničenje tokena modela.

<img src="../../../translated_images/hr/context-memory.dff30ad9fa78832a.webp" alt="Memorija konteksta" width="800"/>

*Kako se kontekst razgovora nakuplja kroz više okreta dok ne dosegnu ograničenje tokena*
**Korak-po-korak razmišljanje** - Za probleme koji zahtijevaju vidljivu logiku. Model prikazuje eksplicitno razmišljanje za svaki korak. Koristite ovo za matematičke zadatke, logičke zagonetke ili kada trebate razumjeti proces razmišljanja.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

Dijagram u nastavku ilustrira kako model razlaže probleme u eksplicitne, numerirane logičke korake.

<img src="../../../translated_images/hr/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Razlaganje problema u eksplicitne logičke korake*

**Ograničeni izlaz** - Za odgovore sa specifičnim zahtjevima formata. Model strogo slijedi pravila formata i duljine. Koristite ovo za sažetke ili kada trebate preciznu strukturu izlaza.

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

Sljedeći dijagram prikazuje kako ograničenja usmjeravaju model da proizvede izlaz koji strogo poštuje vaše zahtjeve za format i duljinu.

<img src="../../../translated_images/hr/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Provjera specifičnih zahtjeva za format, duljinu i strukturu*

## Pokrenite aplikaciju

**Provjerite implementaciju:**

Provjerite da `.env` datoteka postoji u korijenskom direktoriju s Azure vjerodajnicama (kreirano tijekom Modula 01). Pokrenite ovo iz direktorija modula (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # Trebao bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Trebao bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pokrenite aplikaciju:**

> **Napomena:** Ako ste već pokrenuli sve aplikacije koristeći `./start-all.sh` iz korijenskog direktorija (kako je opisano u Modulu 01), ovaj modul već radi na portu 8083. Možete preskočiti naredbe za start i direktno otvoriti http://localhost:8083.

**Opcija 1: Koristeći Spring Boot Dashboard (Preporučeno za korisnike VS Code-a)**

Dev container uključuje Spring Boot Dashboard ekstenziju, koja pruža vizualno sučelje za upravljanje svim Spring Boot aplikacijama. Možete je pronaći na Activity Baru na lijevoj strani VS Code-a (tražite ikonu Spring Boota).

Iz Spring Boot Dashboarda možete:
- Vidjeti sve dostupne Spring Boot aplikacije u workspaceu
- Pokrenuti/zaustaviti aplikacije jednim klikom
- Pratiti logove aplikacija u stvarnom vremenu
- Nadgledati status aplikacije

Jednostavno kliknite na gumb za pokretanje pokraj "prompt-engineering" da pokrenete ovaj modul, ili pokrenite sve module odjednom.

<img src="../../../translated_images/hr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard u VS Code — pokrenite, zaustavite i pratite sve module s jednog mjesta*

**Opcija 2: Koristeći shell skripte**

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

Otvorite http://localhost:8083 u vašem pregledniku.

**Zaustavljanje:**

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

Ovdje je glavno sučelje modula prompt engineering, gdje možete isprobavati svih osam uzoraka jedan pored drugoga.

<img src="../../../translated_images/hr/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Glavni dashboard koji prikazuje svih 8 uzoraka prompt engineeringa s njihovim karakteristikama i slučajevima upotrebe*

## Istraživanje uzoraka

Web sučelje vam omogućuje isprobavanje različitih strategija promptanja. Svaki uzorak rješava različite probleme - isprobajte ih da vidite kada koji pristup najbolje funkcionira.

> **Napomena: Streaming vs. Non-Streaming** — Svaka stranica uzorka nudi dva gumba: **🔴 Stream Response (Live)** i **Ne-streaming** opciju. Streaming koristi Server-Sent Events (SSE) za prikaz tokena u stvarnom vremenu dok model generira odgovor, tako da odmah vidite napredak. Ne-streaming opcija čeka cijeli odgovor prije prikaza. Za promptove koji pokreću duboko razmišljanje (npr. High Eagerness, Self-Reflecting Code), ne-streaming poziv može trajati vrlo dugo – ponekad minute – bez vidljive povratne informacije. **Koristite streaming pri eksperimentiranju s kompleksnim promptovima** kako biste vidjeli rad modela i izbjegli dojam da je zahtjev istekao.
>
> **Napomena: Zahtjev za preglednikom** — Streaming funkcija koristi Fetch Streams API (`response.body.getReader()`) koji zahtijeva pravi preglednik (Chrome, Edge, Firefox, Safari). Ne radi u VS Code ugrađenom Simple Browseru, jer njegov webview ne podržava ReadableStream API. Ako koristite Simple Browser, gumbi za ne-streaming će raditi normalno — samo streaming gumbi su pogođeni. Otvorite `http://localhost:8083` u vanjskom pregledniku za potpunu funkcionalnost.

### Nisko vs. visoko intenzivno razmišljanje (Low vs High Eagerness)

Postavite jednostavno pitanje poput "Koliko je 15% od 200?" koristeći Low Eagerness. Dobit ćete trenutni, izravan odgovor. Sada postavite nešto složenije poput "Dizajniraj strategiju cachinga za API s velikim prometom" koristeći High Eagerness. Kliknite **🔴 Stream Response (Live)** i promatrajte detaljno razmišljanje modela, token po token. Isti model, ista struktura pitanja – ali prompt mu govori koliko duboko da razmišlja.

### Izvršavanje zadataka (Tool Preambles)

Višekoraci tijekovi rada imaju koristi od unaprijed planiranja i praćenja napretka. Model izlaže što će napraviti, opisuje svaki korak, zatim sažima rezultate.

### Samoreflektirajući kod

Isprobajte "Napravite servis za validaciju email adresa". Umjesto da samo generira kod i stane, model generira, evaluira prema kriterijima kvalitete, identificira slabosti i poboljšava. Vidjet ćete iteracije dok kod ne ispuni proizvodne standarde.

### Strukturirana analiza

Pregledi koda zahtijevaju konzistentne okvire evaluacije. Model analizira kod koristeći fiksne kategorije (ispravnost, prakse, performanse, sigurnost) s razinama težine.

### Višekratna konverzacija (Multi-Turn Chat)

Postavite pitanje "Što je Spring Boot?" zatim odmah nastavite s "Pokaži mi primjer". Model pamti prvo pitanje i daje vam specifičan Spring Boot primjer. Bez memorije, drugo pitanje bilo bi previše neodređeno.

### Korak-po-korak razmišljanje

Odaberite matematički problem i isprobajte oba načina: Step-by-Step Reasoning i Low Eagerness. Low eagerness odmah daje odgovor – brzo, ali nejasno. Korak-po-korak prikazuje svaki izračun i odluku.

### Ograničeni izlaz

Kad trebate specifične formate ili broj riječi, ovaj uzorak strogo provodi te zahtjeve. Isprobajte generiranje sažetka s točno 100 riječi u obliku nabrajanja.

## Što stvarno učite

**Uloženi napor u razmišljanje mijenja sve**

GPT-5.2 vam dopušta kontrolu računalnog napora kroz vaše promptove. Niska razina znači brze odgovore s minimalnim istraživanjem. Visoka razina znači da model uzima vremena za duboko razmišljanje. Učite kako prilagoditi trud složenosti zadatka – ne trošite vrijeme na jednostavna pitanja, ali nemojte ni žuriti s kompleksnim odlukama.

**Struktura vodi ponašanje**

Primjećujete XML oznake u promptovima? One nisu ukrasne. Modeli pouzdanije prate strukturirane upute nego slobodni tekst. Kad trebate višekorake procese ili složenu logiku, struktura pomaže modelu pratiti gdje je i što slijedi. Dijagram ispod razlaže dobro strukturiran prompt pokazujući kako oznake poput `<system>`, `<instructions>`, `<context>`, `<user-input>`, i `<constraints>` organiziraju vaše upute u jasne sekcije.

<img src="../../../translated_images/hr/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomija dobro strukturiranog prompta s jasnim sekcijama i XML-stil organizacijom*

**Kvaliteta kroz samo-evaluaciju**

Uzorke samoreflektirajućeg rada obilježava eksplicitno definiranje kriterija kvalitete. Umjesto da se nada da će model "ispravno" odraditi, vi mu točno kažete što znači "ispravno": točna logika, obrada pogrešaka, performanse, sigurnost. Model tada može evaluirati vlastiti izlaz i poboljšati ga. To proces generiranja koda pretvara iz lutrije u kontrolirani proces.

**Kontekst je ograničen**

Višekratne konverzacije funkcioniraju tako da uz svaki zahtjev uključuju povijest poruka. Ali postoji ograničenje - svaki model ima maksimalan broj tokena. Kako se konverzacije povećavaju, potrebno je koristiti strategije da se relevantan kontekst zadrži bez prekoračenja limita. Ovaj modul pokazuje kako memorija radi; kasnije ćete naučiti kada sažimati, zaboraviti i ponovno dohvatiti.

## Sljedeći koraci

**Sljedeći modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigacija:** [← Prethodno: Modul 01 - Uvod](../01-introduction/README.md) | [Natrag na početak](../README.md) | [Sljedeće: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Odricanje od odgovornosti**:
Ovaj je dokument preveden pomoću AI usluge za prevođenje [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo postići točnost, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati autoritativnim izvorom. Za kritične informacije preporučuje se profesionalni ljudski prijevod. Ne odgovaramo za bilo kakve nesporazume ili pogrešna tumačenja koja proizlaze iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->