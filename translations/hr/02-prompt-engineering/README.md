# Modul 02: Inženjering upita s GPT-5.2

## Sadržaj

- [Što ćete naučiti](../../../02-prompt-engineering)
- [Preduvjeti](../../../02-prompt-engineering)
- [Razumijevanje inženjeringa upita](../../../02-prompt-engineering)
- [Osnove inženjeringa upita](../../../02-prompt-engineering)
  - [Zero-Shot upiti](../../../02-prompt-engineering)
  - [Few-Shot upiti](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Upiti temeljeni na ulozi](../../../02-prompt-engineering)
  - [Predlošci upita](../../../02-prompt-engineering)
- [Napredni obrasci](../../../02-prompt-engineering)
- [Korištenje postojećih Azure resursa](../../../02-prompt-engineering)
- [Snimke zaslona aplikacije](../../../02-prompt-engineering)
- [Istraživanje obrazaca](../../../02-prompt-engineering)
  - [Nisko vs visoko nestrpljenje](../../../02-prompt-engineering)
  - [Izvršavanje zadataka (uvodne riječi alata)](../../../02-prompt-engineering)
  - [Kod s introspekcijom](../../../02-prompt-engineering)
  - [Strukturirana analiza](../../../02-prompt-engineering)
  - [Višekratni chat](../../../02-prompt-engineering)
  - [Korak-po-korak razmišljanje](../../../02-prompt-engineering)
  - [Ograničen izlaz](../../../02-prompt-engineering)
- [Što zapravo učite](../../../02-prompt-engineering)
- [Sljedeći koraci](../../../02-prompt-engineering)

## Što ćete naučiti

<img src="../../../translated_images/hr/what-youll-learn.c68269ac048503b2.webp" alt="Što ćete naučiti" width="800"/>

U prethodnom modulu vidjeli ste kako memorija omogućava konverzacijsku umjetnu inteligenciju i koristili ste GitHub modele za osnovne interakcije. Sada ćemo se usredotočiti na način postavljanja pitanja — sami upiti — koristeći Azure OpenAI GPT-5.2. Način na koji strukturirate upite dramatično utječe na kvalitetu odgovora koje dobivate. Počinjemo pregledom osnovnih tehnika postavljanja upita, a zatim prelazimo na osam naprednih obrazaca koji u potpunosti iskorištavaju mogućnosti GPT-5.2.

Koristit ćemo GPT-5.2 jer uvodi kontrolu razmišljanja — možete modelu reći koliko razmišljanja treba prije odgovaranja. To čini različite strategije upita jasnijim i pomaže vam razumjeti kada koristiti koji pristup. Također ćemo imati koristi od manje ograničenja brzine za GPT-5.2 u Azureu u usporedbi s GitHub modelima.

## Preduvjeti

- Završeni Modul 01 (Azure OpenAI resursi postavljeni)
- `.env` datoteka u glavnom direktoriju s Azure vjerodajnicama (kreirana naredbom `azd up` u Modulu 01)

> **Napomena:** Ako niste završili Modul 01, najprije slijedite upute za postavljanje tamo.

## Razumijevanje inženjeringa upita

<img src="../../../translated_images/hr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Što je inženjering upita?" width="800"/>

Inženjering upita odnosi se na dizajniranje ulaznog teksta koji dosljedno daje rezultate koje trebate. Nije riječ samo o postavljanju pitanja — riječ je o strukturiranju zahtjeva tako da model točno razumije što želite i kako to isporučiti.

Zamislite to kao davanje uputa kolegi. "Popravi grešku" je nejasno. "Popravi NullPointerException u UserService.java liniji 45 dodavanjem provjere na null" je specifično. Jezični modeli funkcioniraju na isti način — specifičnost i struktura su važni.

<img src="../../../translated_images/hr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kako LangChain4j pristaje" width="800"/>

LangChain4j pruža infrastrukturu — veze modela, memoriju i vrste poruka — dok su obrasci upita samo pažljivo strukturirani tekst koji šaljete kroz tu infrastrukturu. Ključni gradivni blokovi su `SystemMessage` (koja postavlja ponašanje i ulogu AI) i `UserMessage` (koja nosi vaš stvarni zahtjev).

## Osnove inženjeringa upita

<img src="../../../translated_images/hr/five-patterns-overview.160f35045ffd2a94.webp" alt="Pregled pet obrazaca inženjeringa upita" width="800"/>

Prije nego što zaronimo u napredne obrasce u ovom modulu, pregledajmo pet osnovnih tehnika postavljanja upita. To su temeljni gradivni blokovi koje svaki inženjer upita treba poznavati. Ako ste već prošli kroz [Quick Start modul](../00-quick-start/README.md#2-prompt-patterns), vidjeli ste ih u akciji — evo konceptualnog okvira koji stoji iza njih.

### Zero-Shot upiti

Najjednostavniji pristup: dajte modelu izravnu naredbu bez primjera. Model se u potpunosti oslanja na svoje učenje da razumije i izvrši zadatak. Ovo dobro funkcionira za jasne zahtjeve gdje je očekivano ponašanje očito.

<img src="../../../translated_images/hr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot upiti" width="800"/>

*Izravna naredba bez primjera — model zaključuje zadatak samo iz naredbe*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odgovor: "Pozitivno"
```
  
**Kada koristiti:** Jednostavne klasifikacije, izravna pitanja, prijevodi ili bilo koji zadatak koji model može obraditi bez dodatnih uputa.

### Few-Shot upiti

Dajte primjere koji pokazuju obrazac koji želite da model prati. Model uči očekivani ulazno-izlazni format iz vaših primjera i primjenjuje ga na nove ulaze. Ovo znatno povećava dosljednost za zadatke gdje željeni format ili ponašanje nisu očiti.

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
  
**Kada koristiti:** Prilagođene klasifikacije, dosljedno formatiranje, zadaci specifični za domenu ili kad su rezultati zero-shot nestabilni.

### Chain of Thought

Zatražite od modela da pokaže svoje razmišljanje korak po korak. Umjesto da izravno daje odgovor, model razlaže problem i prolazi kroz svaki dio eksplicitno. To povećava točnost u matematici, logici i zadacima s višekoraknim razmišljanjem.

<img src="../../../translated_images/hr/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought upiti" width="800"/>

*Razmišljanje korak po korak — razbijanje složenih problema u eksplicitne logičke korake*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model pokazuje: 15 - 8 = 7, zatim 7 + 12 = 19 jabuka
```
  
**Kada koristiti:** Matematički problemi, logičke zagonetke, otklanjanje pogrešaka ili svaki zadatak gdje je pokazivanje procesa razmišljanja korisno za točnost i pouzdanost.

### Upiti temeljeni na ulozi

Postavite personu ili ulogu AI prije nego što postavite pitanje. To pruža kontekst koji oblikuje ton, dubinu i fokus odgovora. „Softverski arhitekt“ daje drugačiji savjet nego „mlađi programer“ ili „sigurnosni revizor“.

<img src="../../../translated_images/hr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Upiti temeljeni na ulozi" width="800"/>

*Postavljanje konteksta i persone — isto pitanje dobije različiti odgovor ovisno o dodijeljenoj ulozi*

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
  
**Kada koristiti:** Pregledi koda, podučavanje, domena-specifična analiza ili kada trebate odgovore prilagođene određenoj razini stručnosti ili perspektivi.

### Predlošci upita

Kreirajte ponovne upite s varijabilnim pokazivačima. Umjesto da uvijek pišete novi upit, definirajte predložak jednom i unosite različite vrijednosti. LangChain4j-ova klasa `PromptTemplate` to olakšava s `{{variable}}` sintaksom.

<img src="../../../translated_images/hr/prompt-templates.14bfc37d45f1a933.webp" alt="Predlošci upita" width="800"/>

*Ponovno upotrebljivi upiti s varijabilnim prazninama — jedan predložak, mnogo upotreba*

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
  
**Kada koristiti:** Ponavljani upiti s različitim ulazima, serijski procesi, izgradnja ponovljivih AI tokova rada ili svaki scenarij gdje struktura upita ostaje ista, ali se podaci mijenjaju.

---

Ovih pet osnova daje vam snažan alat za većinu zadataka postavljanja upita. Ostatak ovog modula gradi na njima s **osam naprednih obrazaca** koji iskorištavaju mogućnosti kontrola razmišljanja, samoocjene i strukturiranog izlaza GPT-5.2.

## Napredni obrasci

S osnovama pokrivenim, prijeđimo na osam naprednih obrazaca koji čine ovaj modul jedinstvenim. Ne svi problemi zahtijevaju isti pristup. Neka pitanja trebaju brze odgovore, druga duboko razmišljanje. Neki trebaju vidljivo razmišljanje, drugi samo rezultate. Svaki obrazac dolje je optimiziran za drugačiji scenarij — a kontrola razmišljanja GPT-5.2 te razlike čini još izraženijima.

<img src="../../../translated_images/hr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osam obrazaca za postavljanje upita" width="800"/>

*Pregled osam obrazaca inženjeringa upita i njihovih slučajeva upotrebe*

<img src="../../../translated_images/hr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontrola razmišljanja s GPT-5.2" width="800"/>

*GPT-5.2 kontrola razmišljanja omogućuje vam da navedete koliko razmišljanja model treba imati — od brzih izravnih odgovora do dubokog istraživanja*

**Nisko nestrpljenje (brzo i fokusirano)** - Za jednostavna pitanja gdje želite brze, izravne odgovore. Model izvodi minimalno razmišljanje - maksimalno 2 koraka. Koristite za izračune, pretraživanja ili jasna pitanja.

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
> - "Koja je razlika između niskog i visokog nestrpljenja u obrascima upita?"
> - "Kako XML oznake u upitima pomažu strukturirati AI odgovor?"
> - "Kada koristiti obrasce za samo-refleksiju naspram izravnih naredbi?"

**Visoko nestrpljenje (duboko i temeljito)** - Za složene probleme gdje želite sveobuhvatnu analizu. Model detaljno istražuje i pokazuje detaljno razmišljanje. Koristite za dizajn sustava, arhitektonske odluke ili složena istraživanja.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**Izvršavanje zadataka (napredak korak-po-korak)** - Za višekorake tijekove rada. Model pruža plan unaprijed, opisuje svaki korak tijekom rada, zatim daje sažetak. Koristite za migracije, implementacije ili bilo koji višekorak proces.

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
  
Chain-of-Thought upiti eksplicitno traže od modela da pokaže svoj proces razmišljanja, čime se povećava točnost kod složenih zadataka. Razlaganje korak-po-korak pomaže i ljudima i AI razumjeti logiku.

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Pitajte o ovom obrascu:
> - "Kako bih prilagodio obrazac izvršavanja zadataka za dugotrajne operacije?"
> - "Koje su najbolje prakse za strukturiranje uvodnih riječi alata u proizvodnim aplikacijama?"
> - "Kako mogu uhvatiti i prikazati međurezultate napretka u korisničkom sučelju?"

<img src="../../../translated_images/hr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Obrazac izvršavanja zadataka" width="800"/>

*Plan → Izvršenje → Sažetak tijeka rada za višekorake zadatke*

**Kod s introspekcijom** - Za generiranje koda proizvodne kvalitete. Model generira kod koji slijedi produkcijske standarde s pravilnim upravljanjem pogreškama. Koristite pri izgradnji novih funkcionalnosti ili servisa.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/hr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciklus samo-refleksije" width="800"/>

*Iterativni krug poboljšanja - generiranje, evaluacija, identificiranje problema, poboljšanje, ponavljanje*

**Strukturirana analiza** - Za dosljednu evaluaciju. Model pregledava kod koristeći fiksni okvir (ispravnost, prakse, performanse, sigurnost, održivost). Koristite za preglede koda ili ocjene kvalitete.

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
> - "Kako osigurati dosljednu razinu ozbiljnosti kroz različite seanse pregleda?"

<img src="../../../translated_images/hr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Obrazac strukturirane analize" width="800"/>

*Okvir za dosljedne preglede koda s razinama ozbiljnosti*

**Višekratni chat** - Za razgovore kojima je potreban kontekst. Model pamti prethodne poruke i gradi na njima. Koristite za interaktivne sesije pomoći ili složena pitanja i odgovore.

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

*Kako se kontekst razgovora gomila kroz više okreta dok se ne dosegne limit tokena*

**Korak-po-korak razmišljanje** - Za probleme kojima je potrebna vidljiva logika. Model pokazuje eksplicitno razmišljanje za svaki korak. Koristite za matematičke probleme, logičke zagonetke ili kada trebate razumjeti proces razmišljanja.

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

*Razbijanje problema na eksplicitne logičke korake*

**Ograničen izlaz** - Za odgovore sa specifičnim zahtjevima formata. Model strogo prati pravila o formatu i dužini. Koristite za sažetke ili kad trebate preciznu strukturu izlaza.

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

*Nametanje specifičnih zahtjeva formata, dužine i strukture*

## Korištenje postojećih Azure resursa

**Provjera implementacije:**

Provjerite postoji li `.env` datoteka u glavnom direktoriju s Azure vjerodajnicama (kreirana tijekom Modula 01):
```bash
cat ../.env  # Trebalo bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Pokretanje aplikacije:**

> **Napomena:** Ako ste već pokrenuli sve aplikacije koristeći `./start-all.sh` iz Modula 01, ovaj modul već radi na portu 8083. Možete preskočiti naredbe za pokretanje u nastavku i direktno otići na http://localhost:8083.

**Opcija 1: Korištenje Spring Boot nadzorne ploče (Preporučeno za VS Code korisnike)**

Dev kontejner uključuje ekstenziju Spring Boot nadzorne ploče koja pruža vizualno sučelje za upravljanje svim Spring Boot aplikacijama. Možete je pronaći u traci aktivnosti na lijevoj strani VS Code-a (potražite Spring Boot ikonu).

Iz Spring Boot nadzorne ploče možete:
- Vidjeti sve dostupne Spring Boot aplikacije u radnom prostoru
- Pokrenuti/zaustaviti aplikacije jednim klikom
- Pratiti aplikacijske dnevnike u stvarnom vremenu
- Nadzirati status aplikacije
Jednostavno kliknite gumb za reprodukciju pored "prompt-engineering" da započnete ovaj modul, ili pokrenite sve module odjednom.

<img src="../../../translated_images/hr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Nadzorna ploča" width="400"/>

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

> **Napomena:** Ako više volite ručno izgraditi sve module prije pokretanja:
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

Otvorite http://localhost:8083 u svojem pregledniku.

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

<img src="../../../translated_images/hr/dashboard-home.5444dbda4bc1f79d.webp" alt="Glavna nadzorna ploča" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Glavna nadzorna ploča koja prikazuje svih 8 uzoraka prompt inženjeringa s njihovim karakteristikama i slučajevima upotrebe*

## Istraživanje uzoraka

Web sučelje vam omogućuje eksperimentiranje s različitim strategijama promptanja. Svaki uzorak rješava različite probleme - isprobajte ih da vidite kada koji pristup najbolje funkcionira.

### Niska vs Visoka marljivost

Postavite jednostavno pitanje poput "Koliko je 15% od 200?" koristeći Nisku marljivost. Dobit ćete trenutni, izravan odgovor. Sada postavite nešto složenije poput "Dizajniraj strategiju keširanja za API s velikim prometom" koristeći Visoku marljivost. Promatrajte kako se model usporava i pruža detaljno obrazloženje. Isti model, ista struktura pitanja - ali prompt mu govori koliko razmišljanja treba uložiti.

<img src="../../../translated_images/hr/low-eagerness-demo.898894591fb23aa0.webp" alt="Demo niske marljivosti" width="800"/>

*Brzi izračun s minimalnim razmišljanjem*

<img src="../../../translated_images/hr/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Demo visoke marljivosti" width="800"/>

*Sveobuhvatna strategija keširanja (2.8MB)*

### Izvršavanje zadataka (Alatne upute)

Višestepeni radni tokovi koriste početno planiranje i naraciju napretka. Model opisuje što će učiniti, pripovijeda svaki korak, zatim sažima rezultate.

<img src="../../../translated_images/hr/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Demo izvršavanja zadataka" width="800"/>

*Stvaranje REST endpointa s naracijom korak-po-korak (3.9MB)*

### Samoreflektirajući kod

Probajte "Stvori servis za validaciju e-pošte". Umjesto da samo generira kod i stane, model generira, ocjenjuje prema kriterijima kvalitete, identificira slabosti i poboljšava. Vidjet ćete kako iterira dok kod ne zadovolji proizvodne standarde.

<img src="../../../translated_images/hr/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Demo samoreflektirajućeg koda" width="800"/>

*Potpuni servis za validaciju e-pošte (5.2MB)*

### Strukturirana analiza

Pregledi koda trebaju konzistentne okvire za evaluaciju. Model analizira kod koristeći fiksne kategorije (ispravnost, prakse, performanse, sigurnost) s razinama ozbiljnosti.

<img src="../../../translated_images/hr/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Demo strukturirane analize" width="800"/>

*Pregled koda temeljen na okviru*

### Višekratni dijalog

Upitajte "Što je Spring Boot?" zatim odmah postavite "Pokaži mi primjer". Model pamti vaše prvo pitanje i daje vam upravo primjer za Spring Boot. Bez memorije, drugo pitanje bilo bi previše nejasno.

<img src="../../../translated_images/hr/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Demo višekratnog dijaloga" width="800"/>

*Očuvanje konteksta kroz pitanja*

### Razmišljanje korak po korak

Odaberite matematički problem i isprobajte ga s obje metode: Razmišljanje korak po korak i Niska marljivost. Niska marljivost samo daje odgovor - brzo, ali nejasno. Razmišljanje korak po korak pokazuje svaki izračun i odluku.

<img src="../../../translated_images/hr/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Demo razmišljanja korak po korak" width="800"/>

*Matematički problem s eksplicitnim koracima*

### Ograničeni ishod

Kada trebate specifične formate ili broj riječi, ovaj uzorak nameće strogo pridržavanje. Pokušajte generirati sažetak s točno 100 riječi u obliku nabrajanja.

<img src="../../../translated_images/hr/constrained-output-demo.567cc45b75da1633.webp" alt="Demo ograničenog ishoda" width="800"/>

*Sažetak strojne inteligencije s kontrolom formata*

## Što zapravo učite

**Napori u razmišljanju mijenjaju sve**

GPT-5.2 vam omogućuje kontrolu računalnog napora putem promptova. Niski napor znači brze odgovore s minimalnim istraživanjem. Visoki napor znači da model uzima vremena za duboko razmišljanje. Učite uskladiti napor s kompleksnošću zadatka - ne gubite vrijeme na jednostavna pitanja, ali nemojte ni žuriti s kompleksnim odlukama.

**Struktura vodi ponašanje**

Primijetite XML oznake u promptovima? Nisu ukrasne. Modeli dosljednije prate strukturirane upute nego slobodan tekst. Kad trebate višestepene procese ili složenu logiku, struktura pomaže modelu pratiti gdje se nalazi i što slijedi.

<img src="../../../translated_images/hr/prompt-structure.a77763d63f4e2f89.webp" alt="Struktura prompta" width="800"/>

*Anatomija dobro strukturiranog prompta s jasnim sekcijama i XML-stilom organizacije*

**Kvaliteta kroz samoocjenu**

Samoreflektirajući uzorci funkcioniraju tako da jasno definiraju kriterije kvalitete. Umjesto da se nadate da model "radi ispravno", kažete mu točno što "ispravno" znači: ispravna logika, rukovanje pogreškama, performanse, sigurnost. Model tada može ocijeniti vlastiti izlaz i poboljšati ga. Time se generiranje koda mijenja iz lutrije u proces.

**Kontekst je ograničen**

Višekratni dijalozi funkcioniraju uključivanjem povijesti poruka u svaki zahtjev. Ali postoji limit - svaki model ima maksimalan broj tokena. Kako dijalozi rastu, treba vam strategija za zadržavanje relevantnog konteksta bez prekoračenja tog limita. Ovaj modul pokazuje kako memorija funkcionira; kasnije ćete naučiti kada sažimati, kada zaboraviti, a kada dohvaćati.

## Sljedeći koraci

**Sljedeći modul:** [03-rag - RAG (Generiranje uz podršku dohvaćanja)](../03-rag/README.md)

---

**Navigacija:** [← Prethodni: Modul 01 - Uvod](../01-introduction/README.md) | [Natrag na početak](../README.md) | [Sljedeći: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Odricanje od odgovornosti**:
Ovaj dokument preveden je korištenjem AI prevoditeljskog servisa [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo postići točnost, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na njegovom izvornom jeziku treba smatrati autoritativnim izvorom. Za kritične informacije preporučuje se profesionalni ljudski prijevod. Ne snosimo odgovornost za bilo kakve nesporazume ili pogrešna tumačenja proizašla iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->