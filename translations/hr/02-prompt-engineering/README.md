# Modul 02: Inženjering upita s GPT-5.2

## Sadržaj

- [Što ćete naučiti](../../../02-prompt-engineering)
- [Preduvjeti](../../../02-prompt-engineering)
- [Razumijevanje inženjeringa upita](../../../02-prompt-engineering)
- [Temelji inženjeringa upita](../../../02-prompt-engineering)
  - [Zero-Shot upiti](../../../02-prompt-engineering)
  - [Few-Shot upiti](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Upiti uloge](../../../02-prompt-engineering)
  - [Predlošci upita](../../../02-prompt-engineering)
- [Napredni obrasci](../../../02-prompt-engineering)
- [Korištenje postojećih Azure resursa](../../../02-prompt-engineering)
- [Prikazi aplikacije](../../../02-prompt-engineering)
- [Istraživanje obrazaca](../../../02-prompt-engineering)
  - [Niska vs Visoka Volja](../../../02-prompt-engineering)
  - [Izvršenje zadatka (UVODI alata)](../../../02-prompt-engineering)
  - [Samo-reflektirajući kod](../../../02-prompt-engineering)
  - [Strukturirana analiza](../../../02-prompt-engineering)
  - [Višekratni dijalog](../../../02-prompt-engineering)
  - [Razumijevanje korak po korak](../../../02-prompt-engineering)
  - [Ograničeni ispis](../../../02-prompt-engineering)
- [Što zaista učite](../../../02-prompt-engineering)
- [Sljedeći koraci](../../../02-prompt-engineering)

## Što ćete naučiti

<img src="../../../translated_images/hr/what-youll-learn.c68269ac048503b2.webp" alt="Što ćete naučiti" width="800"/>

U prethodnom modulu vidjeli ste kako memorija omogućava konverzacijski AI i koristili GitHub modele za osnovnu interakciju. Sada se usredotočujemo na to kako postavljate pitanja — same upite — koristeći Azure OpenAI-jev GPT-5.2. Način na koji strukturirate upite dramatično utječe na kvalitetu dobivenih odgovora. Počinjemo s pregledom osnovnih tehnika upita, zatim pristupamo osam naprednih obrazaca koji u potpunosti iskorištavaju mogućnosti GPT-5.2.

Koristit ćemo GPT-5.2 jer uvodi kontrolu razmišljanja - možete modelu reći koliko treba razmišljati prije odgovora. To čini različite strategije upita jasnijima i pomaže vam razumjeti kada koristiti koji pristup. Također ćemo imati koristi od Azureovih manjih ograničenja brzine za GPT-5.2 u usporedbi s GitHub modelima.

## Preduvjeti

- Završeni Modul 01 (postavljeni Azure OpenAI resursi)
- `.env` datoteka u korijenskom direktoriju s Azure vjerodajnicama (kreirana pomoću `azd up` u Modulu 01)

> **Napomena:** Ako niste završili Modul 01, najprije slijedite upute za postavljanje tamo.

## Razumijevanje inženjeringa upita

<img src="../../../translated_images/hr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Što je inženjering upita?" width="800"/>

Inženjering upita je dizajniranje ulaznog teksta koji vam dosljedno donosi rezultate koje trebate. Nije samo o postavljanju pitanja - riječ je o strukturiranju zahtjeva tako da model točno razumije što želite i kako to isporučiti.

Razmislite o tome kao davanju uputa kolegi. "Ispravi grešku" je neodređeno. "Ispravi iznimku nulnog pokazivača u UserService.java na liniji 45 dodavanjem provjere null" je specifično. Modeli jezika funkcioniraju na isti način - specifičnost i struktura su važni.

<img src="../../../translated_images/hr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kako LangChain4j pristaje" width="800"/>

LangChain4j pruža infrastrukturu — veze s modelima, memoriju i vrste poruka — dok su obrasci upita samo pažljivo strukturirani tekst koji šaljete kroz tu infrastrukturu. Ključni gradivni elementi su `SystemMessage` (koja postavlja ponašanje i ulogu AI-ja) i `UserMessage` (koja prenosi vaš stvarni zahtjev).

## Temelji inženjeringa upita

<img src="../../../translated_images/hr/five-patterns-overview.160f35045ffd2a94.webp" alt="Pregled pet obrazaca inženjeringa upita" width="800"/>

Prije nego što zaronimo u napredne obrasce ovog modula, obnovimo pet temeljnih tehnika upita. To su osnovni gradivni blokovi koje bi svaki inženjer upita trebao poznavati. Ako ste već prošli kroz [Quick Start modul](../00-quick-start/README.md#2-prompt-patterns), vidjeli ste ih u akciji — ovdje je konceptualni okvir iza njih.

### Zero-Shot upiti

Najjednostavniji pristup: dajte modelu direktnu instrukciju bez primjera. Model se u potpunosti oslanja na svoje treniranje da razumije i izvrši zadatak. To dobro funkcionira za jednostavne zahtjeve gdje je očekivano ponašanje očito.

<img src="../../../translated_images/hr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot upiti" width="800"/>

*Direktna instrukcija bez primjera — model zaključuje zadatak samo iz instrukcije*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odgovor: "Pozitivno"
```

**Kada koristiti:** Jednostavne klasifikacije, direktna pitanja, prijevodi ili bilo koji zadatak koji model može riješiti bez dodatnih uputa.

### Few-Shot upiti

Pružite primjere koji pokazuju obrazac kojeg želite da model slijedi. Model uči očekivani format ulaz-izlaz iz vaših primjera i primjenjuje ga na nove ulaze. To dramatično poboljšava dosljednost kod zadataka gdje željeni format ili ponašanje nije očit.

<img src="../../../translated_images/hr/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot upiti" width="800"/>

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

**Kada koristiti:** Prilagođene klasifikacije, dosljedno formatiranje, zadaci specifični za domen, ili kad su rezultati zero-shot neujednačeni.

### Chain of Thought

Zamolite model da pokaže svoje razmišljanje korak po korak. Umjesto da odmah pređe na odgovor, model razlaže problem i eksplicitno rješava svaki dio. To poboljšava točnost kod matematike, logike i višekoraknih zadataka razmišljanja.

<img src="../../../translated_images/hr/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought upiti" width="800"/>

*Razmišljanje korak po korak — razlaganje složenih problema u eksplicitne logičke korake*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model prikazuje: 15 - 8 = 7, zatim 7 + 12 = 19 jabuka
```

**Kada koristiti:** Matematički problemi, logičke zagonetke, otklanjanje pogrešaka ili bilo koji zadatak gdje pokazivanje procesa razmišljanja poboljšava točnost i povjerenje.

### Upiti uloge

Postavite personu ili ulogu AI-u prije nego postavite pitanje. To pruža kontekst koji oblikuje ton, dubinu i fokus odgovora. "Softverski arhitekt" daje drugačiji savjet od "mlađeg developera" ili "sigurnosnog revizora".

<img src="../../../translated_images/hr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Upiti uloge" width="800"/>

*Postavljanje konteksta i persone — isto pitanje dobiva drugačiji odgovor ovisno o dodijeljenoj ulozi*

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

**Kada koristiti:** Pregledi koda, podučavanje, analiza specifična za domenu ili kada su potrebni odgovori prilagođeni određenoj razini stručnosti ili perspektivi.

### Predlošci upita

Kreirajte višekratno upotrebljive upite s varijabilnim mjestima za unos. Umjesto da svaki put pišete novi upit, definirajte predložak jednom i ubacite različite vrijednosti. LangChain4j klasa `PromptTemplate` to omogućuje s `{{variable}}` sintaksom.

<img src="../../../translated_images/hr/prompt-templates.14bfc37d45f1a933.webp" alt="Predlošci upita" width="800"/>

*Višekratno upotrebljivi upiti s varijablama — jedan predložak, mnogo upotreba*

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

**Kada koristiti:** Ponavljani upiti s različitim ulazima, obrada skupova, izgradnja višekratnih AI radnih tijekova ili bilo koji scenarij gdje struktura upita ostaje ista, ali se podaci mijenjaju.

---

Ovih pet temelja daje vam solidan alat za većinu zadataka upita. Ostatak ovog modula gradi na njima s **osam naprednih obrazaca** koji koriste kontrolu razmišljanja GPT-5.2, samo-evaluaciju i mogućnosti strukturiranog ispisa.

## Napredni obrasci

Sada kad smo pokrili temelje, prelazimo na osam naprednih obrazaca koji čine ovaj modul jedinstvenim. Nisu svi problemi isti i ne zahtijevaju isti pristup. Neka pitanja trebaju brze odgovore, druga duboko razmišljanje. Neka trebaju vidljivo razmišljanje, a nekima su potrebni samo rezultati. Svaki obrazac ispod optimiziran je za drugačiji scenarij — a kontrola razmišljanja GPT-5.2 čini razlike još izraženijima.

<img src="../../../translated_images/hr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osam obrazaca upita" width="800"/>

*Pregled osam obrazaca inženjeringa upita i njihovih slučajeva korištenja*

<img src="../../../translated_images/hr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontrola razmišljanja sa GPT-5.2" width="800"/>

*Kontrola razmišljanja GPT-5.2 omogućuje specificiranje koliko model treba razmišljati — od brzih direktnih odgovora do dubinskog istraživanja*

**Niska volja (brzo i usmjereno)** - Za jednostavna pitanja gdje želite brze, direktne odgovore. Model primjenjuje minimalno razmišljanje - najviše 2 koraka. Koristite ovo za izračune, pretraživanja ili jednostavna pitanja.

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

> 💡 **Istražite s GitHub Copilot-om:** Otvorite [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) i pitajte:
> - "Koja je razlika između niske i visoke volje u obrascima upita?"
> - "Kako XML oznake u upitima pomažu strukturirati AI-jeve odgovore?"
> - "Kada koristiti obrasce samo-refleksije nasuprot direktnim uputama?"

**Visoka volja (duboko i temeljito)** - Za složene probleme gdje želite iscrpnu analizu. Model detaljno razmatra i prikazuje razmišljanje. Koristite za dizajn sustava, arhitektonske odluke ili složena istraživanja.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Izvršenje zadatka (napredak korak po korak)** - Za višekorake tokove rada. Model pruža plan unaprijed, opisuje svaki korak dok radi i daje sažetak na kraju. Koristite za migracije, implementacije ili bilo koji višekorakni proces.

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

Chain-of-Thought upiti eksplicitno traže od modela da prikaže svoj proces razmišljanja, poboljšavajući točnost složenih zadataka. Razlaganje korak po korak pomaže i ljudima i AI-ju razumjeti logiku.

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Pitajte o ovom obrascu:
> - "Kako bih prilagodio obrazac izvršenja zadatka za dugotrajne operacije?"
> - "Koje su najbolje prakse za strukturiranje uvoda alata u produkcijskim aplikacijama?"
> - "Kako mogu zabilježiti i prikazati ažuriranja međukoraka u sučelju?"

<img src="../../../translated_images/hr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Obrazac izvršenja zadatka" width="800"/>

*Radni tok Plan → Izvršenje → Sažetak za višekorake zadatke*

**Samo-reflektirajući kod** - Za generiranje produkcijski kvalitetnog koda. Model generira kod prema standardima produkcije s pravilnim rukovanjem pogreškama. Koristite ovo pri izgradnji novih značajki ili servisa.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/hr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Samosvjesni ciklus" width="800"/>

*Iterativna petlja poboljšanja - generiraj, ocijeni, identificiraj probleme, poboljšaj, ponovi*

**Strukturirana analiza** - Za dosljednu evaluaciju. Model pregledava kod koristeći fiksirani okvir (ispravnost, prakse, performanse, sigurnost, održivost). Koristite za preglede koda ili procjene kvalitete.

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
> - "Koji je najbolji način za parsiranje i programsku obradu strukturiranog izlaza?"
> - "Kako osigurati dosljedne razine ozbiljnosti kroz različite sesije pregleda?"

<img src="../../../translated_images/hr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Obrazac strukturirane analize" width="800"/>

*Okvir za dosljedne preglede koda s razinama ozbiljnosti*

**Višekratni dijalog** - Za razgovore kojima je potreban kontekst. Model pamti prethodne poruke i nadograđuje na njih. Koristite za interaktivne sesije pomoći ili složena pitanja i odgovore.

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

*Kako se kontekst razgovora akumulira tijekom više koraka dok ne dosegne limit tokena*

**Razumijevanje korak po korak** - Za probleme koji zahtijevaju vidljivu logiku. Model pokazuje eksplicitno razmišljanje za svaki korak. Koristite za matematičke probleme, logičke zagonetke ili kada trebate razumjeti proces razmišljanja.

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

**Ograničeni ispis** - Za odgovore sa specifičnim zahtjevima formata. Model strogo slijedi pravila formata i duljine. Koristite za sažetke ili kada trebate preciznu strukturu ispisa.

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

<img src="../../../translated_images/hr/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Obrazac ograničenog ispisa" width="800"/>

*Provođenje specifičnih pravila formata, duljine i strukture*

## Korištenje postojećih Azure resursa

**Provjerite postavljanje:**

Provjerite postoji li `.env` datoteka u korijenskom direktoriju s Azure vjerodajnicama (kreirana tijekom Modula 01):
```bash
cat ../.env  # Trebalo bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pokrenite aplikaciju:**

> **Napomena:** Ako ste već pokrenuli sve aplikacije pomoću `./start-all.sh` iz Modula 01, ovaj modul je već aktivan na portu 8083. Možete preskočiti naredbe za pokretanje ispod i otići izravno na http://localhost:8083.

**Opcija 1: Korištenje Spring Boot nadzorne ploče (preporučeno za korisnike VS Code-a)**

Dev kontejner uključuje proširenje Spring Boot Dashboard, koje pruža vizualno sučelje za upravljanje svim Spring Boot aplikacijama. Možete ga pronaći na traci aktivnosti na lijevoj strani VS Code-a (potražite ikonu Spring Boot).

Iz Spring Boot Dashboarda možete:
- Vidjeti sve dostupne Spring Boot aplikacije u radnom prostoru
- Pokrenuti/zaustaviti aplikacije jednim klikom
- Pregledavati zapise aplikacija u stvarnom vremenu
- Pratiti status aplikacija
Jednostavno kliknite gumb za reprodukciju pored "prompt-engineering" kako biste pokrenuli ovaj modul, ili pokrenite sve module odjednom.

<img src="../../../translated_images/hr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot kontrolna ploča" width="400"/>

**Opcija 2: Korištenje shell skripti**

Pokrenite sve web aplikacije (module 01-04):

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

Oba skripta automatski učitavaju varijable okoline iz korijenskog `.env` datoteke i izgradit će JAR-ove ako ne postoje.

> **Napomena:** Ako radije želite ručno izgraditi sve module prije pokretanja:
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

<img src="../../../translated_images/hr/dashboard-home.5444dbda4bc1f79d.webp" alt="Početna stranica kontrolne ploče" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Glavna kontrolna ploča koja prikazuje svih 8 obrazaca prompt inženjeringa s njihovim karakteristikama i slučajevima korištenja*

## Istraživanje uzoraka

Web sučelje omogućuje vam eksperimentiranje s različitim strategijama prompta. Svaki obrazac rješava različite probleme – isprobajte ih kako biste vidjeli kada se koji pristup ističe.

> **Napomena: Streaming naspram Nestreaminga** — Svaka stranica obrasca nudi dva gumba: **🔴 Stream Response (uživo)** i opciju **Non-streaming**. Streaming koristi Server-Sent Events (SSE) za prikaz tokena u stvarnom vremenu dok model generira, tako da odmah vidite napredak. Opcija non-streaming čeka na cijeli odgovor prije prikaza. Za upite koji zahtijevaju duboko rezoniranje (npr., High Eagerness, Self-Reflecting Code), non-streaming poziv može potrajati vrlo dugo — ponekad minute — bez vidljive povratne informacije. **Koristite streaming pri eksperimentiranju sa složenim promptovima** kako biste mogli vidjeti rad modela i izbjeći dojam da je zahtjev istekao.
>
> **Napomena: Zahtjev preglednika** — Streaming značajka koristi Fetch Streams API (`response.body.getReader()`) koji zahtijeva puni preglednik (Chrome, Edge, Firefox, Safari). Ne radi u ugrađenom Simple Browseru VS Code-a, jer njegov webview ne podržava ReadableStream API. Ako koristite Simple Browser, non-streaming gumbi će raditi normalno — samo su streaming gumbi pogođeni. Otvorite `http://localhost:8083` u vanjskom pregledniku za potpuni doživljaj.

### Niska naspram Visoke željnosti

Postavite jednostavno pitanje poput "Koliko je 15% od 200?" koristeći Low Eagerness. Dobit ćete trenutni i direktan odgovor. Sad postavite složenije pitanje poput "Dizajniraj strategiju predmemoriranja za API s velikim prometom" koristeći High Eagerness. Kliknite **🔴 Stream Response (uživo)** i promatrajte kako se detaljno rezoniranje modela pojavljuje token po token. Isti model, ista struktura pitanja – ali prompt mu govori koliko treba razmišljati.

### Izvršavanje zadataka (Tool Preambles)

Višestruki koraci u tijeku rada koriste pri-planiranje i naraciju napretka. Model opisuje što će napraviti, objašnjava svaki korak, zatim sažima rezultate.

### Samoreflektirajući kod

Isprobajte "Izradi servis za validaciju emaila". Umjesto da samo generira kod i stane, model generira, ocjenjuje prema kriterijima kvalitete, identificira slabosti te poboljšava. Vidjet ćete kako ponavlja dok kod ne zadovolji proizvodne standarde.

### Strukturirana analiza

Pregled koda zahtijeva dosljedne evaluacijske okvire. Model analizira kod koristeći fiksne kategorije (ispravnost, prakse, performanse, sigurnost) s različitim stupnjevima ozbiljnosti.

### Višekratni razgovor

Pitajte "Što je Spring Boot?" zatim odmah nastavite s "Pokaži mi primjer". Model pamti vaše prvo pitanje i daje vam specifičan primjer Spring Boota. Bez memorije, drugo pitanje bilo bi previše općenito.

### Razmišljanje korak po korak

Odaberite mat problem i isprobajte ga i s korak-po-korak razmišljanjem i s Low Eagerness. Low eagerness vam samo daje odgovor - brzo, ali nejasno. Korak-po-korak pokazuje svaki proračun i odluku.

### Ograničen izlaz

Kad trebate specifične formate ili broj riječi, ovaj obrazac primjenjuje strogo pridržavanje. Isprobajte generirati sažetak s točno 100 riječi u formatiranju s nabrajanjima.

## Što zapravo učite

**Napori rezoniranja mijenjaju sve**

GPT-5.2 vam omogućuje upravljanje računalnim naporom kroz vaše promptove. Niski napor znači brze odgovore s minimalnim istraživanjem. Visoki napor znači da model uzima vrijeme za duboko razmišljanje. Učite kako uskladiti napor s kompleksnošću zadatka – nemojte gubiti vrijeme na jednostavna pitanja, ali ni pritiskati složene odluke.

**Struktura vodi ponašanje**

Primjećujete XML oznake u promptovima? One nisu dekoracija. Modeli pouzdanije prate strukturirane upute nego slobodan tekst. Kad vam trebaju višekorakni procesi ili složena logika, struktura pomaže modelu pratiti gdje se nalazi i što slijedi.

<img src="../../../translated_images/hr/prompt-structure.a77763d63f4e2f89.webp" alt="Struktura prompta" width="800"/>

*Anatomija dobro strukturiranog prompta s jasno definiranim poglavljima i XML-stilom organizacije*

**Kvaliteta kroz samoprocjenu**

Samoreflektirajući obrasci rade tako da eksplicitno definiraju kriterije kvalitete. Umjesto da se nadate da model "radi ispravno," kažete mu što "ispravno" znači: pravilna logika, rukovanje pogreškama, performanse, sigurnost. Model zatim može procijeniti vlastiti izlaz i poboljšati ga. Time generiranje koda postaje proces, a ne lutrija.

**Kontekst je ograničen**

Višekratni razgovori funkcioniraju uključivanjem povijesti poruka u svaki zahtjev. Ali postoji ograničenje – svaki model ima maksimalan broj tokena. Kako razgovori rastu, trebat ćete strategije za održavanje relevantnog konteksta bez prekoračenja. Ovaj modul pokazuje kako memorija funkcionira; kasnije ćete naučiti kada sažeti, kada zaboraviti i kada dohvatiti.

## Sljedeći koraci

**Sljedeći modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigacija:** [← Prethodni: Modul 01 - Uvod](../01-introduction/README.md) | [Natrag na glavni izbornik](../README.md) | [Sljedeći: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Odricanje od odgovornosti**:
Ovaj dokument preveden je pomoću AI usluge za prevođenje [Co-op Translator](https://github.com/Azure/co-op-translator). Iako težimo točnosti, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati službenim izvorom. Za kritične informacije preporuča se profesionalni ljudski prijevod. Nismo odgovorni za bilo kakve nesporazume ili pogrešna tumačenja proizašla iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->