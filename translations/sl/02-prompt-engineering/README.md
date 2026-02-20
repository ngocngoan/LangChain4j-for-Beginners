# Modul 02: Oblikovanje pozivov z GPT-5.2

## Kazalo vsebine

- [Kaj se boste naučili](../../../02-prompt-engineering)
- [Predpogoji](../../../02-prompt-engineering)
- [Razumevanje oblikovanja pozivov](../../../02-prompt-engineering)
- [Osnove oblikovanja pozivov](../../../02-prompt-engineering)
  - [Zero-Shot pozivanje](../../../02-prompt-engineering)
  - [Few-Shot pozivanje](../../../02-prompt-engineering)
  - [Veriga razmišljanja](../../../02-prompt-engineering)
  - [Pozivanje po vlogi](../../../02-prompt-engineering)
  - [Predloge pozivov](../../../02-prompt-engineering)
- [Napredni vzorci](../../../02-prompt-engineering)
- [Uporaba obstoječih virov Azure](../../../02-prompt-engineering)
- [Posnetki zaslona aplikacije](../../../02-prompt-engineering)
- [Raziščite vzorce](../../../02-prompt-engineering)
  - [Nizka proti visoki vnemi](../../../02-prompt-engineering)
  - [Izvajanje nalog (uvodi orodij)](../../../02-prompt-engineering)
  - [Samoreflektirajoča koda](../../../02-prompt-engineering)
  - [Strukturirana analiza](../../../02-prompt-engineering)
  - [Večkratni pogovor](../../../02-prompt-engineering)
  - [Razmišljanje korak za korakom](../../../02-prompt-engineering)
  - [Omejen izhod](../../../02-prompt-engineering)
- [Kaj se resnično učite](../../../02-prompt-engineering)
- [Naslednji koraki](../../../02-prompt-engineering)

## Kaj se boste naučili

<img src="../../../translated_images/sl/what-youll-learn.c68269ac048503b2.webp" alt="Kaj se boste naučili" width="800"/>

V prejšnjem modulu ste videli, kako pomnilnik omogoča konverzacijsko AI in uporabljali modele GitHub za osnovne interakcije. Zdaj se bomo osredotočili na to, kako postavljate vprašanja — same pozive — z uporabo GPT-5.2 v Azure OpenAI. Način, kako strukturirate pozive, bistveno vpliva na kakovost odgovorov, ki jih prejmete. Začnemo z pregledom osnovnih tehnik pozivanja, nato pa nadaljujemo z osmimi naprednimi vzorci, ki izkoriščajo zmogljivosti GPT-5.2.

Uporabljali bomo GPT-5.2, ker ta uvaja nadzor razmišljanja - lahko modelu poveste, koliko premisleka naj opravi pred odgovorom. To različne strategije pozivanja naredi bolj očitne in pomaga razumeti, kdaj uporabiti kateri pristop. Prav tako bomo izkoristili manj omejitev hitrosti v Azure za GPT-5.2 v primerjavi z modeli GitHub.

## Predpogoji

- Zaključen modul 01 (nameščeni viri Azure OpenAI)
- Datoteka `.env` v korenskem imeniku z Azure poverilnicami (ustvarjena z `azd up` v modulu 01)

> **Opomba:** Če modul 01 še niste zaključili, sledite tam navedenim navodilom za namestitev najprej.

## Razumevanje oblikovanja pozivov

<img src="../../../translated_images/sl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Kaj je oblikovanje pozivov?" width="800"/>

Oblikovanje pozivov pomeni ustvarjanje vhodnega besedila, ki dosledno prinaša želene rezultate. Ne gre samo za postavljanje vprašanj - gre za strukturiranje zahtev, da model natančno razume, kaj želite in kako to dostaviti.

Pomislite nanj kot na dajanje navodil sodelavcu. "Popravi napako" je nejasno. "Popravi izjemo null pointer v UserService.java vrstica 45 z dodajanjem preverjanja null" je specifično. Jezikovni modeli delujejo enako - specifičnost in struktura sta pomembni.

<img src="../../../translated_images/sl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kako se LangChain4j prilega" width="800"/>

LangChain4j zagotavlja infrastrukturo — povezave z modeli, pomnilnik in vrste sporočil — medtem ko so vzorci pozivov le skrbno strukturirano besedilo, ki ga pošljete skozi to infrastrukturo. Ključni gradniki so `SystemMessage` (ki nastavi vedenje in vlogo AI) in `UserMessage` (ki nosi vašo dejansko zahtevo).

## Osnove oblikovanja pozivov

<img src="../../../translated_images/sl/five-patterns-overview.160f35045ffd2a94.webp" alt="Pregled petih vzorcev oblikovanja pozivov" width="800"/>

Preden se poglobimo v napredne vzorce tega modula, preglejmo pet temeljnih tehnik pozivanja. To so gradniki, ki bi jih moral poznati vsak oblikovalec pozivov. Če ste že opravili [modul Hitri začetek](../00-quick-start/README.md#2-prompt-patterns), ste jih že videli v praksi — tukaj je konceptualni okvir za njimi.

### Zero-Shot pozivanje

Najpreprostejši pristop: modelu dajte neposredno navodilo brez primerov. Model se v celoti zanaša na svoje učenje, da razume in izvede nalogo. Dobro deluje za enostavne zahteve, kjer je pričakovano vedenje jasno.

<img src="../../../translated_images/sl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot pozivanje" width="800"/>

*Neposredno navodilo brez primerov — model sklepa nalogo samo iz navodila*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odgovor: "Pozitivno"
```

**Kdaj uporabiti:** Preprosta razvrščanja, neposredna vprašanja, prevodi ali katera koli naloga, ki jo model zna brez dodatnih navodil.

### Few-Shot pozivanje

Podajte primere, ki pokažejo vzorec, ki ga želite, da model sledi. Model se uči pričakovanega formata vhod-izhod iz vaših primerov in ga uporablja za nove vhode. To bistveno izboljša doslednost pri nalogah, kjer želeni format ali vedenje ni očiten.

<img src="../../../translated_images/sl/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot pozivanje" width="800"/>

*Učenje iz primerov — model prepozna vzorec in ga uporablja za nove vhode*

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

**Kdaj uporabiti:** Prilagojena razvrščanja, dosledno oblikovanje, naloge specifične za domeno ali ko so rezultati zero-shota neenakomerni.

### Veriga razmišljanja

Prosite model, naj pokaže svoje razmišljanje korak za korakom. Namesto da skoči neposredno na odgovor, model razdeli problem in jasno predela vsak del posebej. To izboljša natančnost pri matematiki, logiki in nalogah z več koraki.

<img src="../../../translated_images/sl/chain-of-thought.5cff6630e2657e2a.webp" alt="Veriga razmišljanja" width="800"/>

*Razmišljanje korak za korakom — razbijanje zapletenih problemov v jasne logične korake*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model prikazuje: 15 - 8 = 7, nato 7 + 12 = 19 jabolk
```

**Kdaj uporabiti:** Matematični problemi, logične uganke, odpravljanje napak ali katera koli naloga, kjer prikaz razmišljanja izboljša natančnost in zaupanje.

### Pozivanje po vlogi

Nastavite AI-ju osebnost ali vlogo, preden postavite vprašanje. To zagotovi kontekst, ki oblikuje ton, globino in fokus odgovora. "Softverski arhitekt" daje drugačne nasvete kot "mlajši razvijalec" ali "varnostni revizor".

<img src="../../../translated_images/sl/role-based-prompting.a806e1a73de6e3a4.webp" alt="Pozivanje po vlogi" width="800"/>

*Nastavitev konteksta in osebe — isto vprašanje prejme različen odgovor glede na dodeljeno vlogo*

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

**Kdaj uporabiti:** Pregledi kode, inštrukcije, analiza specifična za domeno ali ko potrebujete odgovore, prilagojene za določeno strokovno raven ali perspektivo.

### Predloge pozivov

Ustvarite ponovno uporabne pozive s spremenljivkami. Namesto da vsakič pišete nov poziv, definirajte predlogo in vanjo vnašajte različne vrednosti. Razred `PromptTemplate` iz LangChain4j omogoča to z uporabo sintakse `{{variable}}`.

<img src="../../../translated_images/sl/prompt-templates.14bfc37d45f1a933.webp" alt="Predloge pozivov" width="800"/>

*Ponovno uporabni pozivi s spremenljivkami — ena predloga, več uporab*

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

**Kdaj uporabiti:** Ponovljeni poizvedbi z različnimi vhodi, množični obdelavi, gradnji večkrat uporabnih AI potekov dela ali kjer koli, kjer struktura poziva ostaja ista, a se podatki spreminjajo.

---

Ti pet osnovnih vzorcev vam daje močno orodje za večino nalog oblikovanja pozivov. Preostanek modula gradi na tem z **osmimi naprednimi vzorci**, ki izkoriščajo funkcije GPT-5.2 za nadzor razmišljanja, samoevalvacijo in strukturiran izhod.

## Napredni vzorci

Ko smo pokrili osnove, preidimo na osem naprednih vzorcev, ki ta modul naredijo unikaten. Ne za vse probleme je potreben isti pristop. Nekatera vprašanja zahtevajo hitre odgovore, druga globoko razmišljanje. Nekatera potrebujejo vidno razmišljanje, druga le rezultate. Vsak spodnji vzorec je optimiziran za drugačen scenarij — in nadzor razmišljanja GPT-5.2 naredi razlike še bolj očitne.

<img src="../../../translated_images/sl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osem vzorcev pozivanja" width="800"/>

*Pregled osmih vzorcev oblikovanja pozivov in njihovih primerov uporabe*

<img src="../../../translated_images/sl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Nadzor razmišljanja z GPT-5.2" width="800"/>

*Nadzor razmišljanja GPT-5.2 omogoča določiti, koliko razmišljanja naj model opravi — od hitrih neposrednih odgovorov do globokega raziskovanja*

**Nizka vnema (Hitro in fokusirano)** - Za preprosta vprašanja, kjer želite hitre, neposredne odgovore. Model opravi minimalno razmišljanje - največ 2 koraka. Uporabite to za izračune, iskanja ali enostavna vprašanja.

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

> 💡 **Raziščite z GitHub Copilot:** Odprite [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) in vprašajte:
> - "Kakšna je razlika med nizko in visoko vnemo pozivanja?"
> - "Kako XML oznake v pozivih pomagajo strukturirati AI odgovor?"
> - "Kdaj naj uporabim vzorce samorefleksije in kdaj neposredna navodila?"

**Visoka vnema (Globoko in temeljito)** - Za kompleksne probleme, kjer želite poglobljeno analizo. Model temeljito raziskuje in kaže podrobno razmišljanje. Uporabite to za sistemski dizajn, arhitekturne odločitve ali kompleksne raziskave.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Izvajanje nalog (Korak za korakom napredek)** - Za delo v več korakih. Model poda začetni načrt, pripoveduje vsak korak, medtem ko dela, nato pa poda povzetek. Uporabite za migracije, implementacije ali katerikoli večkorakni proces.

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

Pozivanje po verigi razmišljanja izrecno zahteva, da model pokaže svoj proces razmišljanja, kar izboljša natančnost pri kompleksnih nalogah. Razčlenitev korak za korakom pomaga tako ljudem kot AI razumeti logiko.

> **🤖 Preizkusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Vprašajte o tem vzorcu:
> - "Kako prilagoditi vzorec izvajanja naloge za dolgotrajne operacije?"
> - "Kakšne so najboljše prakse za strukturiranje uvodnih delov orodij v produkcijskih aplikacijah?"
> - "Kako zajeti in prikazati vmesne posodobitve napredka v uporabniškem vmesniku?"

<img src="../../../translated_images/sl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Vzorčni izvedba naloge" width="800"/>

*Načrtuj → Izvedi → Povzemi potek dela za večkorajne naloge*

**Samoreflektirajoča koda** - Za generiranje kode produkcijske kakovosti. Model generira kodo po produkcijskih standardih z ustreznim ravnanjem z napakami. Uporabite to pri gradnji novih funkcionalnosti ali storitev.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cikel samorefleksije" width="800"/>

*Iterativni izboljševalni cikel - generiraj, ocenjuj, ugotovi težave, izboljšaj, ponovi*

**Strukturirana analiza** - Za dosledno ocenjevanje. Model pregleda kodo z uporabo fiksnega okvira (pravilnost, prakse, zmogljivost, varnost, vzdrževanje). Uporabite za preglede kode ali ocene kakovosti.

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

> **🤖 Preizkusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Vprašajte o strukturirani analizi:
> - "Kako prilagoditi analitični okvir za različne vrste pregledov kode?"
> - "Kakšen je najboljši način za programatično razčlenjevanje in ukrepanje na strukturiran izhod?"
> - "Kako zagotoviti dosledne ravni resnosti med različnimi recenzijami?"

<img src="../../../translated_images/sl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Vzorčni strukturirana analiza" width="800"/>

*Okvir za dosledne preglede kode z ravnmi resnosti*

**Večkratni pogovor** - Za pogovore, ki potrebujejo kontekst. Model si zapomni prejšnja sporočila in gradi nanje. Uporabite za interaktivne pomočnike ali kompleksno Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/sl/context-memory.dff30ad9fa78832a.webp" alt="Pomnilnik konteksta" width="800"/>

*Kako se kontekst pogovora kopiči skozi več krogov, dokler ne doseže omejitve števila tokenov*

**Razmišljanje korak za korakom** - Za probleme, ki zahtevajo vidno logiko. Model pokaže eksplicitno razmišljanje za vsak korak. Uporabite za matematične probleme, logične uganke ali kadar morate razumeti proces razmišljanja.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sl/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Vzorčni korak za korakom" width="800"/>

*Razbijanje problemov v jasne logične korake*

**Omejen izhod** - Za odgovore s specifičnimi zahtevami glede formata. Model strogo sledi pravilom formata in dolžine. Uporabite za povzetke ali kadar potrebujete natančno strukturo izhoda.

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

<img src="../../../translated_images/sl/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Vzorčni omejen izhod" width="800"/>

*Uveljavljanje specifičnih zahtev po formatu, dolžini in strukturi*

## Uporaba obstoječih virov Azure

**Preverite nameščanje:**

Prepričajte se, da datoteka `.env` obstaja v korenskem imeniku z Azure poverilnicami (ustvarjeno med modulom 01):
```bash
cat ../.env  # Naj bi prikazal AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Zaženite aplikacijo:**

> **Opomba:** Če ste že zagnali vse aplikacije z uporabo `./start-all.sh` iz modula 01, ta modul že teče na vratih 8083. Zaženete lahko ta modul tudi brez spodnjih ukazov in neposredno odprete http://localhost:8083.

**Možnost 1: Uporaba Spring Boot Dashboard (priporočeno za uporabnike VS Code)**

V razvojni vsebnik je vključena razširitev Spring Boot Dashboard, ki omogoča vizualno upravljanje vseh Spring Boot aplikacij. Najdete jo na vrstici dejavnosti na levi strani VS Code (poiščite ikono Spring Boot).

V Spring Boot Dashboard lahko:
- Vidite vse razpoložljive Spring Boot aplikacije v delovnem prostoru
- Zaženete/ustavite aplikacije z enim klikom
- Ogledate si zapise aplikacij v realnem času
- Spremljate status aplikacij
Preprosto kliknite gumb za predvajanje poleg "prompt-engineering", da začnete ta modul, ali zaženite vse module hkrati.

<img src="../../../translated_images/sl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

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

Ali zaženite samo ta modul:

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

Obe skripti samodejno naložita okoljske spremenljivke iz glavne datoteke `.env` in zgradita JAR, če ti ne obstajata.

> **Opomba:** Če želite ročno sestaviti vse module pred zagonom:
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

Odprite http://localhost:8083 v vašem brskalniku.

**Zaustavitev:**

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

## Posnetki zaslona aplikacije

<img src="../../../translated_images/sl/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Glavni nadzorni plošči, ki prikazuje vseh 8 vzorcev prompt inženiringa z njihovimi značilnostmi in primeri uporabe*

## Raziskovanje vzorcev

Spletni vmesnik vam omogoča eksperimentiranje z različnimi strategijami spodbujanja. Vsak vzorec rešuje različne probleme – preizkusite jih, da vidite, kdaj kateri pristop najbolj sije.

### Nizka proti visoki zagnanosti

Postavite preprosto vprašanje, na primer "Koliko je 15 % od 200?" z nizko zagnanostjo. Dobite takojšen, neposreden odgovor. Zdaj postavite kaj zahtevnejšega, na primer "Oblikuj strategijo predpomnjenja za API z visoko obremenitvijo" z visoko zagnanostjo. Opazujte, kako model upočasni in poda podrobna obrazložitvena pojasnila. Enak model, enaka struktura vprašanja – vendar prompt pove, koliko razmišljanja naj opravi.

<img src="../../../translated_images/sl/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Hitri izračun z minimalno obrazložitvijo*

<img src="../../../translated_images/sl/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Celovita strategija predpomnjenja (2,8 MB)*

### Izvajanje nalog (Uvodni tekst orodja)

Večstopenjski delovni procesi imajo koristi od vnaprejšnjega načrtovanja in opisovanja napredka. Model na kratko opiše, kaj bo naredil, pripoveduje o vsakem koraku in nato povzame rezultate.

<img src="../../../translated_images/sl/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Ustvarjanje REST končne točke s korakom po korak pripovedjo (3,9 MB)*

### Samoreflektirajoča koda

Poskusite "Ustvari storitev za preverjanje e-pošte". Namesto da bi le generiral kodo in se ustavil, model generira, oceni glede na kakovostne kriterije, prepozna slabosti in izboljša. Videli boste, kako iterira, dokler koda ne doseže proizvodnih standardov.

<img src="../../../translated_images/sl/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Popolna storitev preverjanja e-pošte (5,2 MB)*

### Strukturirana analiza

Pregledi kode zahtevajo dosledne okvirje ocenjevanja. Model analizira kodo z uporabo fiksnih kategorij (pravilnost, prakse, zmogljivost, varnost) z različnimi stopnjami resnosti.

<img src="../../../translated_images/sl/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Pregled kode na podlagi okvira*

### Večkratni pogovori

Vprašajte "Kaj je Spring Boot?" in takoj sledite s "Pokaži mi primer". Model si zapomni vaše prvo vprašanje in vam poda primer Spring Boot posebej za to. Brez spomina bi bilo drugo vprašanje preveč splošno.

<img src="../../../translated_images/sl/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Ohranjanje konteksta med vprašanji*

### Razmišljanje korak za korakom

Izberite matematični problem in ga poskusite tako z razmišljanjem korak za korakom kot z nizko zagnanostjo. Nizka zagnanost vam zgolj poda odgovor – hitro, a nejasno. Korak za korakom prikazuje vsak izračun in odločitev.

<img src="../../../translated_images/sl/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matematični problem z eksplicitnimi koraki*

### Ograničen izhod

Ko potrebujete specifične formate ali število besed, ta vzorec dosega strogo skladnost. Poskusite ustvariti povzetek z natanko 100 besedami v obliki pogosto uporabnih točk.

<img src="../../../translated_images/sl/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Povzetek strojnega učenja s kontrolo formata*

## Kaj se resnično učite

**Napor razmišljanja spremeni vse**

GPT-5.2 vam omogoča nadzor računalniškega napora prek vaših pozivov. Nizek napor pomeni hitre odgovore z minimalnim raziskovanjem. Visok napor pomeni, da si model vzame čas za globoko razmišljanje. Učite se uskladiti napor s kompleksnostjo naloge – ne zapravljajte časa za preprosta vprašanja, vendar se tudi ne hitite pri zahtevnih odločitvah.

**Struktura vodi vedenje**

Opazite XML oznake v pozivih? Niso dekorativne. Modeli zanesljiveje sledijo strukturiranim navodilom kot prostemu besedilu. Ko potrebujete večstopenjske procese ali kompleksno logiko, struktura modelu pomaga slediti, kje je in kaj sledi.

<img src="../../../translated_images/sl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomija dobro strukturiranega poziva s čistimi razdelki in organizacijo v slogu XML*

**Kakovost prek samoocenjevanja**

Vzorce samoreflektiranja delujejo tako, da naredijo kakovostne kriterije eksplicitne. Namesto da bi upali, da model "pravično opravi delo", točno poveste, kaj pomeni "pravično": pravilna logika, ravnanje z napakami, zmogljivost, varnost. Model nato lahko oceni svoj izhod in ga izboljša. To spremeni generiranje kode iz loterije v proces.

**Kontekst je omejen**

Večkratni pogovori delujejo tako, da vključujejo zgodovino sporočil pri vsakem pozivu. Vendar pa obstaja omejitev – vsak model ima maksimalno število tokenov. Ko pogovori rastejo, boste potrebovali strategije za ohranjanje relevantnega konteksta, ne da bi dosegli to mejo. Ta modul vam pokaže, kako spomin deluje; kasneje se boste naučili, kdaj povzeti, kdaj pozabiti in kdaj poiskati.

## Naslednji koraki

**Naslednji modul:** [03-rag - RAG (generiranje z iskanjem informacij)](../03-rag/README.md)

---

**Navigacija:** [← Prejšnji: Modul 01 - Uvod](../01-introduction/README.md) | [Nazaj na glavno](../README.md) | [Naslednji: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo storitve za avtomatski prevod AI [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas opozarjamo, da avtomatizirani prevodi lahko vsebujejo napake ali netočnosti. Izvirni dokument v njegovem izvirnem jeziku velja za avtoritativni vir. Za pomembne informacije priporočamo strokovni človeški prevod. Nismo odgovorni za kakršnekoli nesporazume ali napačne razlage, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->