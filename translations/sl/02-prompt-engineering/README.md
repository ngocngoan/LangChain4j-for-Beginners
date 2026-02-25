# Modul 02: Inženiring pozivov z GPT-5.2

## Kazalo

- [Kaj se boste naučili](../../../02-prompt-engineering)
- [Predpogoji](../../../02-prompt-engineering)
- [Razumevanje inženiringa pozivov](../../../02-prompt-engineering)
- [Osnove inženiringa pozivov](../../../02-prompt-engineering)
  - [Zero-Shot pozivanje](../../../02-prompt-engineering)
  - [Few-Shot pozivanje](../../../02-prompt-engineering)
  - [Veriga misli](../../../02-prompt-engineering)
  - [Pozivanje na podlagi vlog](../../../02-prompt-engineering)
  - [Predloge pozivov](../../../02-prompt-engineering)
- [Napredni vzorci](../../../02-prompt-engineering)
- [Uporaba obstoječih Azure virov](../../../02-prompt-engineering)
- [Posnetki zaslona aplikacije](../../../02-prompt-engineering)
- [Raziščite vzorce](../../../02-prompt-engineering)
  - [Nizka proti visoki vnemi](../../../02-prompt-engineering)
  - [Izvajanje nalog (uvodni teksti orodij)](../../../02-prompt-engineering)
  - [Samoreflektirajoča koda](../../../02-prompt-engineering)
  - [Strukturirana analiza](../../../02-prompt-engineering)
  - [Večkrožni pogovor](../../../02-prompt-engineering)
  - [Razmišljanje korak za korakom](../../../02-prompt-engineering)
  - [Omejen izhod](../../../02-prompt-engineering)
- [Kaj se v resnici naučite](../../../02-prompt-engineering)
- [Naslednji koraki](../../../02-prompt-engineering)

## Kaj se boste naučili

<img src="../../../translated_images/sl/what-youll-learn.c68269ac048503b2.webp" alt="Kaj se boste naučili" width="800"/>

V prejšnjem modulu ste videli, kako spomin omogoča pogovorni AI in uporabili modele GitHub za osnovne interakcije. Zdaj se bomo osredotočili na to, kako zastavljate vprašanja — na same pozive — z uporabo Azure OpenAI GPT-5.2. Način, kako strukturirate pozive, močno vpliva na kakovost odgovorov, ki jih dobite. Začnemo s pregledom osnovnih tehnik pozivanja, nato pa preidemo na osem naprednih vzorcev, ki v celoti izkoriščajo zmogljivosti GPT-5.2.

Uporabljali bomo GPT-5.2, ker uvaja nadzor razmišljanja — lahko modelu poveste, koliko razmišljanja naj opravi pred odgovorom. To naredi različne strategije pozivanja bolj očitne in vam pomaga razumeti, kdaj uporabiti posamezni pristop. Prav tako bomo imeli koristi od manj omejitev hitrosti pri GPT-5.2 v primerjavi z modeli GitHub v Azure.

## Predpogoji

- Dokončan Modul 01 (Azure OpenAI viri nameščeni)
- Datoteka `.env` v korenski mapi z Azure poverilnicami (ustvarjena z `azd up` v Modulu 01)

> **Opomba:** Če niste dokončali Modula 01, najprej sledite navodilom za namestitev tam.

## Razumevanje inženiringa pozivov

<img src="../../../translated_images/sl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Kaj je inženiring pozivov?" width="800"/>

Inženiring pozivov pomeni oblikovanje vhodnega besedila, ki vam dosledno prinese želene rezultate. Ne gre samo za zastavljanje vprašanj — gre za strukturiranje zahtevkov tako, da model natančno razume, kaj želite in kako to dostaviti.

Pomislite na to kot da dajete navodila sodelavcu. „Popravi napako“ je nejasno. „Popravi izjemo null pointer v UserService.java na vrstici 45 tako, da dodaš preverjanje null“ je specifično. Jezikovni modeli delujejo enako — pomembna je specifičnost in struktura.

<img src="../../../translated_images/sl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kako LangChain4j pristaja" width="800"/>

LangChain4j zagotavlja infrastrukturo — povezave z modeli, spomin in vrste sporočil — medtem ko so vzorci pozivov le skrbno strukturirano besedilo, ki ga pošljete prek te infrastrukture. Ključni gradniki so `SystemMessage` (nastavi vedenje in vlogo AI) in `UserMessage` (nese vaš dejanski zahtevek).

## Osnove inženiringa pozivov

<img src="../../../translated_images/sl/five-patterns-overview.160f35045ffd2a94.webp" alt="Pregled petih vzorcev inženiringa pozivov" width="800"/>

Preden se poglobimo v napredne vzorce v tem modulu, si poglejmo pet osnovnih tehnik pozivanja. To so gradniki, ki jih mora poznati vsak inženir pozivov. Če ste že prešli skozi [Hitri zagon modul](../00-quick-start/README.md#2-prompt-patterns), ste jih že videli v akciji — tukaj je konceptualni okvir za njimi.

### Zero-Shot pozivanje

Najpreprostejši pristop: modelu daste neposredno navodilo brez primerov. Model se v celoti zanaša na svoje učenje, da razume in izvede nalogo. To dobro deluje za enostavne zahteve, kjer je pričakovano vedenje očitno.

<img src="../../../translated_images/sl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot pozivanje" width="800"/>

*Neposredno navodilo brez primerov — model sklepa nalogo samo iz navodila*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odgovor: "Pozitivno"
```

**Kdaj uporabiti:** Preproste klasifikacije, neposredna vprašanja, prevodi ali kakršnakoli naloga, ki jo model lahko izvede brez dodatnih navodil.

### Few-Shot pozivanje

Podajte primere, ki pokažejo vzorec, ki ga želite, da model sledi. Model se nauči pričakovan format vhod-izhod iz vaših primerov in ga uporablja pri novih vhodih. To močno izboljša doslednost pri nalogah, kjer želeni format ali vedenje ni očitno.

<img src="../../../translated_images/sl/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot pozivanje" width="800"/>

*Učenje iz primerov — model prepozna vzorec in ga uporabi na novih vhodih*

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

**Kdaj uporabiti:** Prilagojene klasifikacije, dosledno oblikovanje, naloge specifične za domeno ali kadar so rezultati zero-shot neenotni.

### Veriga misli

Prosite model, naj pokaže svoje razmišljanje korak za korakom. Namesto da skoči naravnost na odgovor, model razdeli problem in postopoma obravnava vsak del. To izboljša natančnost pri matematičnih, logičnih in večstopenjskih nalogah.

<img src="../../../translated_images/sl/chain-of-thought.5cff6630e2657e2a.webp" alt="Veriga misli pozivanje" width="800"/>

*Razmišljanje korak za korakom — razbijanje kompleksnih problemov na jasne logične korake*

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

### Pozivanje na podlagi vlog

Določite osebnost ali vlogo AI pred zastavljanjem vprašanja. To da kontekst, ki oblikuje ton, globino in osredotočenost odgovora. „Programs ekta arhitekt“ daje drugačne nasvete kot „mlajši razvijalec“ ali „varnostni pregledovalec“.

<img src="../../../translated_images/sl/role-based-prompting.a806e1a73de6e3a4.webp" alt="Pozivanje na podlagi vlog" width="800"/>

*Nastavitev konteksta in osebnosti — isto vprašanje dobi drugačen odgovor, odvisno od dodeljene vloge*

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

**Kdaj uporabiti:** Pregledi kode, tutorstvo, domena-specifična analiza ali kadar potrebujete odgovore, prilagojene specifični ravni strokovnosti ali perspektivi.

### Predloge pozivov

Ustvarite ponovno uporabne pozive z uporabo spremenljivk. Namesto da vsakokrat pišete nov poziv, definirajte predlogo in vanjo vnesete različne vrednosti. Razred `PromptTemplate` v LangChain4j to omogoča s sintakso `{{variable}}`.

<img src="../../../translated_images/sl/prompt-templates.14bfc37d45f1a933.webp" alt="Predloge pozivov" width="800"/>

*Ponovno uporabni pozivi s spremenljivkami — ena predloga, veliko uporab*

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

**Kdaj uporabiti:** Ponovljeni poizvedbi z različnimi vhodi, serijska obdelava, gradnja ponovno uporabnih AI potekov dela ali kadar se struktura poziva ne spremeni, le podatki se.

---

Ti pet osnov daje močno orodje za večino nalog pozivanja. Preostanek modula gradi na njih z **osmimi naprednimi vzorci**, ki izkoriščajo nadzor razmišljanja GPT-5.2, samoevalvacijo in možnosti strukturiranega izhoda.

## Napredni vzorci

Z osnovami pokritimi, preidimo na osmih naprednih vzorcev, ki dajejo temu modulu edinstven značaj. Ne vsi problemi potrebujejo isti pristop. Nekatera vprašanja zahtevajo hitre odgovore, druga globoko razmišljanje. Nekatera potrebujejo vidno razmišljanje, druga zgolj rezultate. Vsak spodaj je optimiziran za drugačen scenarij — nadzor razmišljanja GPT-5.2 naredi razlike še bolj izrazite.

<img src="../../../translated_images/sl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osem vzorcev pozivanja" width="800"/>

*Pregled osmih vzorcev inženiringa pozivov in njihovih uporab*

<img src="../../../translated_images/sl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Nadzor razmišljanja z GPT-5.2" width="800"/>

*Nadzor razmišljanja GPT-5.2 vam omogoča določiti, koliko razmišljanja naj model izvede — od hitrih neposrednih odgovorov do globinske raziskave*

**Nizka vnema (hitro in osredotočeno)** - Za enostavna vprašanja, kjer želite hitre, neposredne odgovore. Model naredi minimalno razmišljanje - največ 2 koraka. Uporabite za izračune, iskanja ali enostavna vprašanja.

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
> - "Kakšna je razlika med nizko in visoko vnemo pri vzorcih pozivanja?"
> - "Kako XML oznake v pozivih pomagajo strukturirati odgovor AI?"
> - "Kdaj naj uporabim vzorce samorefleksije in kdaj neposredna navodila?"

**Visoka vnema (globoko in temeljito)** - Za kompleksne probleme, kjer želite obsežno analizo. Model raziskuje temeljito in pokaže podrobno razmišljanje. Uporabite za načrtovanje sistema, odločitve o arhitekturi ali kompleksne raziskave.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Izvajanje nalog (napredek korak za korakom)** - Za večstopenjske delovne tokove. Model ponudi načrt na začetku, pripoveduje vsak korak med delom, nato pa poda povzetek. Uporabite za migracije, implementacije ali katerikoli večstopenjski proces.

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

Veriga misli pozivanje eksplicitno zahteva, da model pokaže proces razmišljanja, kar izboljša natančnost pri kompleksnih nalogah. Razbijanje korak za korakom pomaga tako ljudem kot AI razumeti logiko.

> **🤖 Poskusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Vprašajte o tem vzorcu:
> - "Kako bi prilagodil vzorec izvajanja nalog za dolgotrajne operacije?"
> - "Kakšne so najboljše prakse za strukturiranje uvodnih tekstov orodij v produkcijskih aplikacijah?"
> - "Kako zajeti in prikazati posredne posodobitve napredka v uporabniškem vmesniku?"

<img src="../../../translated_images/sl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Vzorec izvajanja nalog" width="800"/>

*Načrt → Izvedba → Povzetek za večstopenjske naloge*

**Samoreflektirajoča koda** - Za generiranje kode produkcijske kakovosti. Model generira kodo v skladu s produkcijskimi standardi s primerno obravnavo napak. Uporabite pri gradnji novih funkcionalnosti ali storitev.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cikel samorefleksije" width="800"/>

*Iterativni cikel izboljšave — generiraj, ocenjuj, identificiraj težave, izboljšaj, ponovi*

**Strukturirana analiza** - Za dosledno ocenjevanje. Model pregleda kodo z uporabo fiksnega okvira (popolnost, prakse, zmogljivost, varnost, vzdržljivost). Uporabite za pregled kode ali oceno kakovosti.

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

> **🤖 Poskusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Vprašajte o strukturirani analizi:
> - "Kako prilagoditi analitični okvir za različne vrste pregledov kode?"
> - "Kakšen je najboljši način za programatsko razčlenjevanje in ukrepanje glede strukturiranega izhoda?"
> - "Kako zagotoviti dosledne nivoje resnosti med različnimi sejami pregledov?"

<img src="../../../translated_images/sl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Vzorec strukturirane analize" width="800"/>

*Okvir za dosledne preglede kode z nivoji resnosti*

**Večkrožni pogovor** - Za pogovore, ki potrebujejo kontekst. Model si zapomni prejšnja sporočila in gradi nanje. Uporabite za interaktivne pomožne seje ali kompleksna vprašanja in odgovore.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/sl/context-memory.dff30ad9fa78832a.webp" alt="Spomin konteksta" width="800"/>

*Kako se kontekst pogovora nabira skozi več krogov, dokler ne doseže omejitve žetonov*

**Razmišljanje korak za korakom** - Za probleme, ki zahtevajo vidno logiko. Model pokaže eksplicitno razmišljanje za vsak korak. Uporabite pri matematičnih problemih, logičnih ugankah ali kadar želite razumeti proces mišljenja.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sl/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Vzorec korak za korakom" width="800"/>

*Razbijanje problemov na jasne logične korake*

**Omejen izhod** - Za odgovore z zahtevami po specifičnem formatu. Model strogo sledi pravilom formata in dolžine. Uporabite za povzetke ali kadar potrebujete natančno strukturo izhoda.

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

<img src="../../../translated_images/sl/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Vzorec omejenega izhoda" width="800"/>

*Uveljavljanje specifičnih zahtev glede formata, dolžine in strukture*

## Uporaba obstoječih Azure virov

**Preverite namestitev:**

Prepričajte se, da datoteka `.env` obstaja v korenski mapi z Azure poverilnicami (ustvarjena v Modulu 01):
```bash
cat ../.env  # Moralo bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Zaženite aplikacijo:**

> **Opomba:** Če ste že zagnali vse aplikacije z uporabo `./start-all.sh` iz Modula 01, ta modul že teče na vratih 8083. Lahko preskočite spodnja ukaza za zagon in se neposredno povežete na http://localhost:8083.

**Možnost 1: Uporaba Spring Boot nadzorne plošče (priporočeno za uporabnike VS Code)**

Razvojni kontejner vključuje razširitev Spring Boot Dashboard, ki nudi vizualni vmesnik za upravljanje vseh aplikacij Spring Boot. Najdete jo v vrstici za dejavnosti na levi strani VS Code (poiščite ikono Spring Boot).

V Spring Boot Dashboard lahko:
- Vidite vse razpoložljive Spring Boot aplikacije v delovnem prostoru
- Zaženete/ustavite aplikacije z enim klikom
- Ogledate si dnevniške zapise aplikacij v realnem času
- Spremljate stanje aplikacije
Preprosto kliknite gumb za predvajanje zraven "prompt-engineering", da zaženete ta modul, ali začnite vse module naenkrat.

<img src="../../../translated_images/sl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Nadzorna plošča" width="400"/>

**Možnost 2: Uporaba ukaznih skript**

Zaženite vse spletne aplikacije (module 01-04):

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

Obe skripti samodejno naložita spremenljivke okolja iz glavne datoteke `.env` in bosta zgradili JAR-je, če ti ne obstajajo.

> **Opomba:** Če želite ročno zgraditi vse module pred zagonom:
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

**Za zaustavitev:**

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

<img src="../../../translated_images/sl/dashboard-home.5444dbda4bc1f79d.webp" alt="Domača stran nadzorne plošče" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Glavna nadzorna plošča, ki prikazuje vseh 8 obrazcev prompt-engineeringa z njihovimi značilnostmi in primeri uporabe*

## Raziščite vzorce

Spletni vmesnik vam omogoča eksperimentiranje z različnimi strategijami pozivanja. Vsak vzorec rešuje različne težave - preizkusite jih in poglejte, kdaj kateri pristop izstopa.

> **Opomba: Streamanje vs Nestransko prenašanje** — Vsaka stran vzorca ponuja dva gumba: **🔴 Stream Response (V živo)** in možnost **Nestriimanja**. Streamanje uporablja Server-Sent Events (SSE), da prikaže tokene v realnem času, ko jih model ustvarja, zato vidite napredek takoj. Nestriimanje počaka na celoten odgovor, preden ga prikaže. Za pozive, ki sprožijo globoko razmišljanje (npr. High Eagerness, Samoreflektirajoča koda), lahko klic nestriimanja traja zelo dolgo — včasih minute — brez vidnih povratnih informacij. **Uporabljajte streamanje pri eksperimentiranju z zahtevnimi pozivi**, da vidite, kako model deluje, in preprečite vtis, da je zahteva potekla.
>
> **Opomba: Zahteva za brskalnik** — Funkcija streamanja uporablja Fetch Streams API (`response.body.getReader()`), ki zahteva poln brskalnik (Chrome, Edge, Firefox, Safari). Ne deluje v vgrajenem Simple Browserju v VS Code, saj njegova spletna vsebina ne podpira ReadableStream API. Če uporabljate Simple Browser, bodo gumbi za nestriimanje delovali normalno — samo gumbi za streamanje so omejeni. Za polno izkušnjo odprite `http://localhost:8083` v zunanjem brskalniku.

### Nizka vs Visoka radovednost

Postavite preprosto vprašanje, kot je "Koliko je 15 % od 200?" z nizko radovednostjo. Dobite takojšen, neposreden odgovor. Zdaj postavite nekaj zahtevnejšega, kot je "Oblikuj strategijo predpomnjenja za API z veliko obremenitvijo" z visoko radovednostjo. Kliknite **🔴 Stream Response (V živo)** in opazujte podrobno razmišljanje modela, ki se pojavlja token za tokenom. Enak model, ista struktura vprašanja - a poziv mu pove, koliko razmišljanja naj vloži.

### Izvajanje nalog (Predgovori orodij)

Večstopenjski delovni tokovi imajo koristi od predhodnega načrtovanja in pripovedovanja poteka. Model opiše, kaj bo storil, pripoveduje vsak korak, nato pa povzame rezultate.

### Samoreflektirajoča koda

Poskusite z "Ustvari storitev za preverjanje veljavnosti e-pošte". Namesto da bi samo generiral kodo in se ustavil, model generira, ocenjuje glede na kriterije kakovosti, ugotavlja pomanjkljivosti in izboljšuje. Videli boste, kako ponavlja, dokler koda ne izpolnjuje proizvodnih standardov.

### Strukturirana analiza

Pregledi kode potrebujejo dosledne okvire ocenjevanja. Model analizira kodo s fiksnimi kategorijami (pravilnost, prakse, zmogljivost, varnost) z različnimi stopnjami resnosti.

### Večkratni pogovori

Vprašajte "Kaj je Spring Boot?" in takoj nato "Pokaži mi primer". Model si zapomni vaše prvo vprašanje in vam predstavi primer Spring Boot posebej za vas. Brez spomina bi bilo drugo vprašanje preveč nejasno.

### Korak za korakom obrazložitev

Izberite matematični problem in ga poskusite z obema: Korak za korakom obrazložitev in nizko radovednostjo. Nizka radovednost vam preprosto da odgovor - hitro, a nejasno. Korak za korakom vam pokaže vsak izračun in odločitev.

### Omejen izhod

Če potrebujete specifične formate ali število besed, ta vzorec dosledno zagotavlja skladnost. Poskusite ustvariti povzetek z natančno 100 besedami v obliki označenih točk.

## Kaj se resnično učite

**Učinek razmišljanja spremeni vse**

GPT-5.2 vam omogoča nadzor računske moči preko vaših pozivov. Nizka moč pomeni hitre odzive z minimalnim raziskovanjem. Visoka moč pomeni, da model vzame čas za globoko razmišljanje. Učite se prilagajati trud zahtevnosti naloge - ne izgubljajte časa z enostavnimi vprašanji, vendar ne hitite pri zahtevnih odločitvah.

**Struktura usmerja vedenje**

Opazili ste XML oznake v pozivih? Niso le okras. Modeli zanesljiveje sledijo strukturiranim navodilom kot prostemu besedilu. Ko potrebujete večstopenjske procese ali kompleksno logiko, struktura pomaga modelu slediti, kje je in kaj sledi.

<img src="../../../translated_images/sl/prompt-structure.a77763d63f4e2f89.webp" alt="Struktura poziva" width="800"/>

*Anatomija dobro strukturiranega poziva s jasnimi deli in organizacijo v XML stilu*

**Kakovost skozi samoocenjevanje**

Vzorec samoreflektiranja deluje tako, da eksplicitno navede kriterije kakovosti. Namesto da bi upali, da "bo model naredil prav", mu natančno poveste, kaj pomeni "prav": pravilna logika, obravnava napak, zmogljivost, varnost. Model lahko nato oceni svoj izhod in izboljša. To spremeni generiranje kode iz loterije v proces.

**Kontekst je omejen**

Večkratni pogovori delujejo tako, da vsaki zahtevi dodajajo zgodovino sporočil. A obstaja omejitev - vsak model ima maksimalno število tokenov. Ko se pogovori daljšajo, boste potrebovali strategije, da ohranite relevanten kontekst brez preseganja meje. Ta modul vam pokaže, kako deluje spomin; pozneje se boste naučili, kdaj povzeti, kdaj pozabiti in kdaj povrniti.

## Naslednji koraki

**Naslednji modul:** [03-rag - RAG (Generiranje z iskanjem informacij)](../03-rag/README.md)

---

**Navigacija:** [← Prejšnji: Modul 01 - Uvod](../01-introduction/README.md) | [Nazaj na glavno](../README.md) | [Naprej: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
To besedilo je bilo prevedeno z uporabo storitve za avtomatski prevod AI [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, upoštevajte, da lahko avtomatski prevodi vsebujejo napake ali netočnosti. Izvirni dokument v izvorni jezik je treba upoštevati kot avtoritativni vir. Za pomembne informacije priporočamo strokovni človeški prevod. Nismo odgovorni za morebitne nesporazume ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->