# Modul 02: Prompt inženjering s GPT-5.2

## Sadržaj

- [Video vodič](../../../02-prompt-engineering)
- [Što ćete naučiti](../../../02-prompt-engineering)
- [Preduvjeti](../../../02-prompt-engineering)
- [Razumijevanje prompt inženjeringa](../../../02-prompt-engineering)
- [Osnove prompt inženjeringa](../../../02-prompt-engineering)
  - [Zero-Shot promptanje](../../../02-prompt-engineering)
  - [Few-Shot promptanje](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based promptanje](../../../02-prompt-engineering)
  - [Predlošci promptova](../../../02-prompt-engineering)
- [Napredni obrasci](../../../02-prompt-engineering)
- [Korištenje postojećih Azure resursa](../../../02-prompt-engineering)
- [Snimke zaslona aplikacije](../../../02-prompt-engineering)
- [Istraživanje obrazaca](../../../02-prompt-engineering)
  - [Mala protiv velike spremnosti](../../../02-prompt-engineering)
  - [Izvršavanje zadatka (preambule alata)](../../../02-prompt-engineering)
  - [Samoreflektirajući kod](../../../02-prompt-engineering)
  - [Strukturirana analiza](../../../02-prompt-engineering)
  - [Višekratni razgovor](../../../02-prompt-engineering)
  - [Razumijevanje korak po korak](../../../02-prompt-engineering)
  - [Ograničeni izlaz](../../../02-prompt-engineering)
- [Što stvarno učite](../../../02-prompt-engineering)
- [Sljedeći koraci](../../../02-prompt-engineering)

## Video vodič

Pogledajte ovu sesiju uživo koja objašnjava kako započeti s ovim modulom:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt inženjering s LangChain4j - sesija uživo" width="800"/></a>

## Što ćete naučiti

<img src="../../../translated_images/hr/what-youll-learn.c68269ac048503b2.webp" alt="Što ćete naučiti" width="800"/>

U prethodnom modulu vidjeli ste kako memorija omogućava konverzacijski AI i koristili GitHub modele za osnovne interakcije. Sada ćemo se usredotočiti na način postavljanja pitanja — same promptove — koristeći Azure OpenAI GPT-5.2. Način na koji strukturirate promptove drastično utječe na kvalitetu dobivenih odgovora. Počinjemo s pregledom osnovnih tehnika promptanja, zatim prelazimo na osam naprednih obrazaca koji u potpunosti koriste mogućnosti GPT-5.2.

Koristit ćemo GPT-5.2 jer uvodi kontrolu rezoniranja — možete modelu reći koliko razmišljanja treba napraviti prije odgovora. To čini različite strategije promptanja jasnijima i pomaže vam razumjeti kada koristiti koji pristup. Također ćemo imati koristi od manjeg ograničenja stope za GPT-5.2 na Azureu u odnosu na GitHub modele.

## Preduvjeti

- Završeni Modul 01 (Azure OpenAI resursi postavljeni)
- `.env` datoteka u korijenu direktorija s Azure vjerodajnicama (kreirana naredbom `azd up` u Modulu 01)

> **Napomena:** Ako niste završili Modul 01, prvo slijedite upute za postavljanje tamo.

## Razumijevanje prompt inženjeringa

<img src="../../../translated_images/hr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Što je prompt inženjering?" width="800"/>

Prompt inženjering je dizajniranje ulaznog teksta koji dosljedno daje rezultate koje trebate. Nije samo o postavljanju pitanja - radi se o strukturiranju zahtjeva tako da model točno razumije što želite i kako to isporučiti.

Zamislite to kao davanje uputa kolegi. "Popravi grešku" je nejasno. "Popravi iznimku null pointer u UserService.java linija 45 dodavanjem provjere null" je specifično. Modeli jezika funkcioniraju na isti način — specifičnost i struktura su bitni.

<img src="../../../translated_images/hr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kako se uklapa LangChain4j" width="800"/>

LangChain4j pruža infrastrukturu — veze s modelom, memoriju i vrste poruka — dok su obrasci promptova samo pažljivo strukturirani tekst koji šaljete kroz tu infrastrukturu. Ključni građevni blokovi su `SystemMessage` (koji postavlja ponašanje i ulogu AI-ja) i `UserMessage` (koji nosi vaš stvarni zahtjev).

## Osnove prompt inženjeringa

<img src="../../../translated_images/hr/five-patterns-overview.160f35045ffd2a94.webp" alt="Pregled pet obrazaca za prompt inženjering" width="800"/>

Prije nego što zaronimo u napredne obrasce ovog modula, pregledajmo pet osnovnih tehnika promptanja. To su temeljni blokovi koje svaki prompt inženjer treba znati. Ako ste već radili kroz [Quick Start modul](../00-quick-start/README.md#2-prompt-patterns), vidjeli ste ih u akciji — evo konceptualnog okvira iza njih.

### Zero-Shot promptanje

Najjednostavniji pristup: dajte modelu direktnu uputu bez primjera. Model se u potpunosti oslanja na svoje treniranje da razumije i izvrši zadatak. Ovo dobro funkcionira za jednostavne zahtjeve gdje je očekivano ponašanje očito.

<img src="../../../translated_images/hr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot promptanje" width="800"/>

*Direktna uputa bez primjera — model zaključuje zadatak samo iz upute*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odgovor: "Pozitivno"
```

**Kada koristiti:** Jednostavne klasifikacije, direktna pitanja, prijevodi ili bilo koji zadatak koji model može obaviti bez dodatnih smjernica.

### Few-Shot promptanje

Dajte primjere koji pokazuju obrazac koji želite da model slijedi. Model uči očekivani format ulaza i izlaza iz vaših primjera i primjenjuje ga na nove ulaze. Ovo znatno poboljšava dosljednost za zadatke gdje željeni format ili ponašanje nije očito.

<img src="../../../translated_images/hr/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot promptanje" width="800"/>

*Učenje iz primjera — model prepoznaje obrazac i primjenjuje ga na nove ulaze*

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

**Kada koristiti:** Prilagođene klasifikacije, dosljedno formatiranje, zadaci specifični za domen, ili kad su zero-shot rezultati nedosljedni.

### Chain of Thought

Zatražite od modela da pokaže svoj proces razmišljanja korak po korak. Umjesto da odmah da odgovor, model razlaže problem i rješava svaki dio eksplicitno. Ovo poboljšava točnost na zadacima iz matematike, logike i višestepenog rezoniranja.

<img src="../../../translated_images/hr/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought promptanje" width="800"/>

*Razmišljanje korak po korak — razlaganje složenih problema na eksplicitne logičke korake*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model prikazuje: 15 - 8 = 7, zatim 7 + 12 = 19 jabuka
```

**Kada koristiti:** Matematički problemi, logičke zagonetke, otklanjanje grešaka ili bilo koji zadatak gdje prikazivanje procesa razmišljanja poboljšava točnost i povjerenje.

### Role-Based promptanje

Postavite persona ili ulogu za AI prije postavljanja pitanja. Ovo pruža kontekst koji oblikuje ton, dubinu i fokus odgovora. "Softverski arhitekt" daje drugačiji savjet nego "mlađi programer" ili "sigurnosni auditor".

<img src="../../../translated_images/hr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based promptanje" width="800"/>

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

### Predlošci promptova

Kreirajte ponovno upotrebljive promptove s varijabilnim mjestima za zamjenu. Umjesto da svaki put pišete novi prompt, definirajte predložak jednom i ispunite ga različitim vrijednostima. LangChain4j klasa `PromptTemplate` olakšava to s `{{variable}}` sintaksom.

<img src="../../../translated_images/hr/prompt-templates.14bfc37d45f1a933.webp" alt="Predlošci promptova" width="800"/>

*Ponovno upotrebljivi promptovi s varijabilnim mjestima — jedan predložak, mnogo primjena*

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

**Kada koristiti:** Ponavljani upiti s različitim ulazima, obrada u serijama, izgradnja ponovno upotrebljivih AI tijekova rada ili bilo koji scenarij gdje struktura prompta ostaje ista, ali se podaci mijenjaju.

---

Ovih pet osnova daje vam čvrst alat za većinu zadataka promptanja. Ostatak ovog modula nadograđuje se na njima s **osam naprednih obrazaca** koji iskorištavaju kontrolu rezoniranja GPT-5.2, samoocjenu i mogućnosti strukturiranog izlaza.

## Napredni obrasci

S osnovama pokrivenima, krenimo na osam naprednih obrazaca koji ovaj modul čine jedinstvenim. Ne svi problemi zahtijevaju isti pristup. Neka pitanja trebaju brze odgovore, druga duboko razmišljanje. Neki trebaju vidljivo rezoniranje, a drugi samo rezultate. Svaki obrazac u nastavku je optimiziran za različitu situaciju — a kontrola rezoniranja GPT-5.2 pojačava razlike.

<img src="../../../translated_images/hr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osam obrazaca za prompt inženjering" width="800"/>

*Pregled osam obrazaca prompt inženjeringa i njihovih upotreba*

<img src="../../../translated_images/hr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontrola rezoniranja s GPT-5.2" width="800"/>

*Kontrola rezoniranja GPT-5.2 vam omogućuje da specificirate koliko razmišljanja model treba napraviti — od brzih direktnih odgovora do dubokom istraživanju*

**Mala spremnost (brzo i fokusirano)** - Za jednostavna pitanja gdje želite brze, direktne odgovore. Model radi minimalno rezoniranje - maksimalno 2 koraka. Koristite ovo za izračune, upite ili jasna pitanja.

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
> - "Koja je razlika između obrazaca s niskom i visokom spremnošću?"
> - "Kako XML oznake u promptovima pomažu u strukturiranju AI odgovora?"
> - "Kada trebam koristiti obrasce samorefleksije u odnosu na direktne upute?"

**Visoka spremnost (duboko i temeljito)** - Za složene probleme gdje želite sveobuhvatnu analizu. Model temeljito istražuje i pokazuje detaljno rezoniranje. Koristite ovo za dizajn sustava, arhitektonske odluke ili kompleksna istraživanja.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Izvršavanje zadatka (napredak korak po korak)** - Za višestepene tijekove rada. Model daje plan unaprijed, opisuje svaki korak dok radi, pa na kraju daje sažetak. Koristite ovo za migracije, implementacije ili bilo koji višestepeni proces.

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

Chain-of-Thought promptanje eksplicitno traži od modela da pokaže svoj proces razmišljanja, čime se poboljšava točnost za složene zadatke. Razlaganje korak po korak pomaže i ljudima i AI-u da razumiju logiku.

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Pitajte o ovom obrascu:
> - "Kako bih prilagodio obrazac izvršavanja zadatka za dugotrajne operacije?"
> - "Koje su najbolje prakse za strukturiranje preambula alata u proizvodnim aplikacijama?"
> - "Kako mogu uhvatiti i prikazati međufaze napretka u korisničkom sučelju?"

<img src="../../../translated_images/hr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Obrazac izvršavanja zadatka" width="800"/>

*Planiranje → Izvršenje → Sažetak tijeka rada za višestepene zadatke*

**Samoreflektirajući kod** - Za generiranje koda proizvodne kvalitete. Model generira kod u skladu s proizvodnim standardima i pravilnim rukovanjem greškama. Koristite ovo pri izradi novih značajki ili servisa.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/hr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciklus samorefleksije" width="800"/>

*Iterativni ciklus poboljšanja - generiraj, ocijeni, identificiraj probleme, poboljšaj, ponovi*

**Strukturirana analiza** - Za dosljedne evaluacije. Model pregledava kod koristeći fiksni okvir (ispravnost, prakse, izvedba, sigurnost, održivost). Koristite ovo za preglede koda ili procjene kvalitete.

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

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Pitajte o strukturiranoj analizi:
> - "Kako mogu prilagoditi okvir analize za različite vrste pregleda koda?"
> - "Koji je najbolji način za parsiranje i programatsku obradu strukturiranog izlaza?"
> - "Kako osigurati dosljedne razine ozbiljnosti kod različitih sesija pregleda?"

<img src="../../../translated_images/hr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Obrazac strukturirane analize" width="800"/>

*Okvir za dosljedne preglede koda sa razinama ozbiljnosti*

**Višekratni razgovor** - Za konverzacije kojima treba kontekst. Model pamti prethodne poruke i gradi na njima. Koristite ovo za interaktivne pomoćne sesije ili složena pitanja i odgovore.

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

*Kako se kontekst razgovora kumulira kroz više krugova do limita tokena*

**Razumijevanje korak po korak** - Za probleme kojima treba vidljiva logika. Model pokazuje eksplicitno rezoniranje za svaki korak. Koristite ovo za matematičke probleme, logičke zagonetke ili kad trebate razumjeti proces razmišljanja.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/hr/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Obrazac korak-po-korak" width="800"/>

*Razlaganje problema na eksplicitne logičke korake*

**Ograničeni izlaz** - Za odgovore sa specifičnim zahtjevima formata. Model strogo slijedi pravila o formatu i duljini. Koristite ovo za sažetke ili kad trebate preciznu strukturu izlaza.

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

*Primjena specifičnih zahtjeva formata, duljine i strukture*

## Korištenje postojećih Azure resursa

**Provjerite postavljanje:**

Provjerite postoji li `.env` datoteka u korijenu direktorija s Azure vjerodajnicama (kreirana tijekom Modula 01):
```bash
cat ../.env  # Trebao bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pokrenite aplikaciju:**

> **Napomena:** Ako ste već pokrenuli sve aplikacije koristeći `./start-all.sh` iz Modula 01, ovaj modul već radi na portu 8083. Možete preskočiti naredbe za pokretanje u nastavku i direktno prijeći na http://localhost:8083.
**Opcija 1: Korištenje Spring Boot nadzorne ploče (Preporučeno za korisnike VS Codea)**

Dev container uključuje Spring Boot Dashboard ekstenziju, koja pruža vizualno sučelje za upravljanje svim Spring Boot aplikacijama. Možete je pronaći u Activity Baru na lijevoj strani VS Codea (potražite Spring Boot ikonu).

S Spring Boot Dashboarda možete:
- Vidjeti sve dostupne Spring Boot aplikacije u radnom prostoru
- Pokrenuti/zaustaviti aplikacije jednim klikom
- Pratiti logove aplikacije u stvarnom vremenu
- Nadzirati status aplikacije

Samo kliknite na tipku za reprodukciju pored "prompt-engineering" da biste pokrenuli ovaj modul, ili pokrenite sve module odjednom.

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

Obje skripte automatski učitavaju varijable okoline iz korijenskog `.env` fajla i izgradit će JAR-ove ako ne postoje.

> **Napomena:** Ako radije želite ručno izgraditi sve module prije pokretanja:
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

Otvorite http://localhost:8083 u pregledniku.

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

<img src="../../../translated_images/hr/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Glavna nadzorna ploča prikazuje svih 8 uzoraka prompt inženjeringa s njihovim karakteristikama i slučajevima upotrebe*

## Istraživanje uzoraka

Web sučelje vam omogućuje eksperimentiranje s različitim strategijama promptanja. Svaki uzorak rješava različite probleme – isprobajte ih da vidite kada koji pristup najbolje funkcionira.

> **Napomena: Streaming vs Non-Streaming** — Svaka stranica uzorka nudi dvije tipke: **🔴 Stream Response (Live)** i **Non-streaming** opciju. Streaming koristi Server-Sent Events (SSE) za prikaz tokena u stvarnom vremenu dok ih model generira, tako da odmah vidite napredak. Non-streaming opcija čeka cijeli odgovor prije prikaza. Za promptove koji zahtijevaju duboko razmišljanje (npr. High Eagerness, Self-Reflecting Code), non-streaming poziv može trajati vrlo dugo – ponekad minute – bez vidljive povratne informacije. **Koristite streaming kada eksperimentirate s kompleksnim promptovima** kako biste vidjeli rad modela i izbjegli dojam da je zahtjev istekao.
>
> **Napomena: Zahtjevi preglednika** — Streaming značajka koristi Fetch Streams API (`response.body.getReader()`) koji zahtijeva punopravni preglednik (Chrome, Edge, Firefox, Safari). Ne radi u ugrađenom Simple Browseru VS Codea, jer njegov webview ne podržava ReadableStream API. Ako koristite Simple Browser, non-streaming tipke će i dalje normalno raditi – samo streaming tipke su pogođene. Otvorite `http://localhost:8083` u vanjskom pregledniku za potpunu funkcionalnost.

### Niska vs Visoka želja (Eagerness)

Postavite jednostavno pitanje poput "Što je 15% od 200?" koristeći Nisku želju. Dobit ćete brzi, direktni odgovor. Sad postavite složenije pitanje poput "Dizajniraj strategiju cacheiranja za API s velikim prometom" koristeći Visoku želju. Kliknite **🔴 Stream Response (Live)** i promatrajte kako se detaljno rezoniranje modela pojavljuje token-po-token. Isti model, ista struktura pitanja – ali prompt mu govori koliko razmišljanja treba uložiti.

### Izvršavanje zadataka (Tool Preambles)

Višestepeni tijekovi rada imaju koristi od planiranja unaprijed i praćenja napretka. Model izlaže što će napraviti, opisuje svaki korak, te zatim sumira rezultate.

### Samoreflektirajući kod

Isprobajte "Napravite servis za validaciju e-mail adresa". Umjesto da samo generira kod i stane, model generira, procjenjuje prema kriterijima kvalitete, identificira slabosti i poboljšava. Vidjet ćete kako iterira sve dok kod ne zadovolji produkcijske standarde.

### Strukturalna analiza

Pregledi koda trebaju dosljedne evaluacijske okvire. Model analizira kod koristeći fiksne kategorije (ispravnost, prakse, performanse, sigurnost) s razinama ozbiljnosti.

### Višekratni razgovori (multi-turn chat)

Pitajte "Što je Spring Boot?" i odmah nastavite s "Pokaži mi primjer". Model pamti prvo pitanje i daje vam točno primjer Spring Boota. Bez memorije, drugo pitanje bilo bi previše općenito.

### Razmišljanje korak-po-korak

Odaberite matematički zadatak i isprobajte ga s metodom korak-po-korak i Niskom željom. Niska želja samo daje odgovor – brzo ali nejasno. Korak-po-korak pokazuje svaki izračun i odluku.

### Ograničeni izlaz

Kad trebate specifične formate ili broj riječi, ovaj uzorak strogo primjenjuje pravila. Pokušajte generirati sažetak s točno 100 riječi u obliku nabrajanja.

## Što zapravo učite

**Razmišljanje mijenja sve**

GPT-5.2 vam dopušta upravljanje računalnim naporom kroz vaše prompte. Niski napor znači brze odgovore s minimalnim istraživanjem. Visoki napor znači da model uzima vrijeme za duboko razmišljanje. Učite uskladiti napor s kompleksnošću zadatka – ne gubite vrijeme na jednostavna pitanja, ali ni ne žurite sa složenim odlukama.

**Struktura vodi ponašanje**

Primjećujete li XML oznake u promptovima? Nisu dekoracija. Modeli pouzdanije prate strukturirane upute nego slobodan tekst. Kad trebate višestepene procese ili složenu logiku, struktura pomaže modelu pratiti gdje se nalazi i što slijedi.

<img src="../../../translated_images/hr/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomija dobro strukturiranog prompta s jasnim odjeljcima i XML-style organizacijom*

**Kvaliteta kroz samo-evaluaciju**

Uzorci samoreflektiranja rade tako da eksplicitno definiraju kriterije kvalitete. Umjesto da se nadate da model "radi ispravno", kažete mu točno što "ispravno" znači: ispravna logika, rukovanje pogreškama, performanse, sigurnost. Model tada može evaluirati vlastiti izlaz i poboljšati se. To kod generiranje pretvara iz lutrije u proces.

**Kontekst je ograničen**

Višekratni razgovori funkcioniraju uključivanjem povijesti poruka sa svakim zahtjevom. Ali postoji granica – svaki model ima maksimalan broj tokena. Kako razgovori rastu, potreban vam je strateški pristup da održite relevantan kontekst bez prelaska te granice. Ovaj modul vam pokazuje kako memorija funkcionira; kasnije ćete naučiti kada sažeti, kada zaboraviti i kada dohvatiti.

## Sljedeći koraci

**Sljedeći modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigacija:** [← Prethodno: Modul 01 - Uvod](../01-introduction/README.md) | [Natrag na početak](../README.md) | [Sljedeće: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Odricanje od odgovornosti**:  
Ovaj dokument je preveden korištenjem AI usluge za prijevod [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo postići točnost, molimo imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku smatra se autoritativnim izvorom. Za kritične informacije preporučuje se profesionalni ljudski prijevod. Nismo odgovorni za bilo kakve nesporazume ili pogrešne interpretacije koje mogu proizaći iz upotrebe ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->