# Modul 02: Inženiring pozivov z GPT-5.2

## Kazalo

- [Pregled videa](../../../02-prompt-engineering)
- [Kaj se boste naučili](../../../02-prompt-engineering)
- [Predpogoji](../../../02-prompt-engineering)
- [Razumevanje inženiringa pozivov](../../../02-prompt-engineering)
- [Osnove inženiringa pozivov](../../../02-prompt-engineering)
  - [Zero-Shot pozivanje](../../../02-prompt-engineering)
  - [Few-Shot pozivanje](../../../02-prompt-engineering)
  - [Veriga razmišljanja](../../../02-prompt-engineering)
  - [Pozivanje na podlagi vloge](../../../02-prompt-engineering)
  - [Predloge pozivov](../../../02-prompt-engineering)
- [Napredni vzorci](../../../02-prompt-engineering)
- [Uporaba obstoječih Azure virov](../../../02-prompt-engineering)
- [Posnetki zaslona aplikacije](../../../02-prompt-engineering)
- [Raziskovanje vzorcev](../../../02-prompt-engineering)
  - [Nizka proti visoki vnemi](../../../02-prompt-engineering)
  - [Izvajanje nalog (uvodne izjave orodij)](../../../02-prompt-engineering)
  - [Samoreflektirajoča koda](../../../02-prompt-engineering)
  - [Strukturirana analiza](../../../02-prompt-engineering)
  - [Večkročni klepet](../../../02-prompt-engineering)
  - [Razmišljanje korak za korakom](../../../02-prompt-engineering)
  - [Omejen izhod](../../../02-prompt-engineering)
- [Kaj se dejansko učite](../../../02-prompt-engineering)
- [Nadaljni koraki](../../../02-prompt-engineering)

## Pregled videa

Oglejte si to live sejo, ki razlaga, kako začeti z modulom:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Inženiring pozivov z LangChain4j - Live seja" width="800"/></a>

## Kaj se boste naučili

<img src="../../../translated_images/sl/what-youll-learn.c68269ac048503b2.webp" alt="Kaj se boste naučili" width="800"/>

V prejšnjem modulu ste videli, kako pomnilnik omogoča pogovor z AI in uporabili modele GitHub za osnovne interakcije. Zdaj bomo osredotočeni na to, kako postavljate vprašanja — same pozive — z uporabo Azure OpenAI GPT-5.2. Način, kako strukturirate pozive, dramatično vpliva na kakovost odzivov, ki jih prejmete. Začnemo z pregledom osnovnih tehnik pozivanja, nato pa preidemo na osem naprednih vzorcev, ki v polni meri izkoristijo zmogljivosti GPT-5.2.

Uporabljali bomo GPT-5.2, ker uvaja nadzor nad razmišljanjem - lahko modelu poveste, koliko naj razmišlja pred odgovorom. To naredi različne strategije pozivanja bolj opazne in vam pomaga razumeti, kdaj uporabiti kateri pristop. Prav tako bomo imeli koristi od manjših omejitev hitrosti za GPT-5.2 na Azure v primerjavi z modeli GitHub.

## Predpogoji

- Zaključen Modul 01 (vzpostavljeni Azure OpenAI viri)
- Datoteka `.env` v korenski mapi z Azure poverilnicami (ustvarjena z `azd up` v Modulu 01)

> **Opomba:** Če niste zaključili Modula 01, najprej sledite navodilom za namestitev tam.

## Razumevanje inženiringa pozivov

<img src="../../../translated_images/sl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Kaj je inženiring pozivov?" width="800"/>

Inženiring pozivov je o načrtovanju vhodnega besedila, ki dosledno prinaša želene rezultate. Ni le o postavljanju vprašanj - gre za strukturiranje zahtev, da model natančno razume, kaj želite in kako to dostaviti.

Pomislite na to kot da dajete navodila sodelavcu. "Popravi napako" je nejasno. "Popravi izjemo null pointer v UserService.java vrstica 45 z dodajanjem preverjanja null" je specifično. Lingvistični modeli delujejo na enak način - pomembna sta specifičnost in struktura.

<img src="../../../translated_images/sl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kako LangChain4j ustreza" width="800"/>

LangChain4j zagotavlja infrastrukturo — povezave z modeli, pomnilnik in tipe sporočil — medtem ko so vzorci pozivov le skrbno strukturirano besedilo, ki ga pošljete skozi to infrastrukturo. Ključni gradniki so `SystemMessage` (ki določa vedenje in vlogo AI) in `UserMessage` (ki nosi vašo dejansko zahtevo).

## Osnove inženiringa pozivov

<img src="../../../translated_images/sl/five-patterns-overview.160f35045ffd2a94.webp" alt="Pregled petih vzorcev inženiringa pozivov" width="800"/>

Preden se poglobimo v napredne vzorce v tem modulu, si poglejmo pet osnovnih tehnik pozivanja. To so gradniki, ki bi jih moral poznati vsak inženir pozivov. Če ste že delali [Hitri začetek modula](../00-quick-start/README.md#2-prompt-patterns), ste jih že videli v akciji — tukaj je konceptualni okvir zanje.

### Zero-Shot pozivanje

Najenostavnejši pristop: podajte modelu neposredno navodilo brez primerov. Model se v celoti zanaša na svoje usposabljanje, da razume in opravi nalogo. To deluje dobro za preproste zahteve, kjer je pričakovano vedenje očitno.

<img src="../../../translated_images/sl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot pozivanje" width="800"/>

*Neposredno navodilo brez primerov — model sklepa nalogo samo iz navodila*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odgovor: "Pozitivno"
```

**Kdaj uporabiti:** Preproste klasifikacije, neposredna vprašanja, prevodi ali katera koli naloga, ki jo model lahko reši brez dodatnih navodil.

### Few-Shot pozivanje

Podajte primere, ki pokažejo vzorec, ki ga želite, da model sledi. Model se iz vaših primerov nauči pričakovani vhodno-izhodni format in ga uporabi na novih vnosih. To dramatično izboljša konsistentnost pri nalogah, kjer želen format ali vedenje ni očiten.

<img src="../../../translated_images/sl/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot pozivanje" width="800"/>

*Učenje iz primerov — model prepozna vzorec in ga uporablja pri novih vhodih*

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

**Kdaj uporabiti:** Prilagojene klasifikacije, dosledno formatiranje, naloge specifične za domeno ali kadar so zero-shot rezultati nedosledni.

### Veriga razmišljanja

Zahtevajte od modela, da prikaže svoje razmišljanje korak za korakom. Namesto da skoči neposredno na odgovor, model razdeli problem in izrecno obdela vsak del. To izboljša natančnost pri matematičnih, logičnih in večstopenjskih razmišljanjih.

<img src="../../../translated_images/sl/chain-of-thought.5cff6630e2657e2a.webp" alt="Veriga razmišljanja" width="800"/>

*Razmišljanje korak za korakom — razbijanje kompleksnih problemov na izrecne logične korake*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model prikazuje: 15 - 8 = 7, nato 7 + 12 = 19 jabolk
```

**Kdaj uporabiti:** Matematični problemi, logične uganke, odpravljanje napak ali katerakoli naloga, kjer prikaz procesa razmišljanja poveča natančnost in zaupanje.

### Pozivanje na podlagi vloge

Nastavite osebnost ali vlogo AI-ju preden postavite vprašanje. To zagotovi kontekst, ki oblikuje ton, globino in osredotočenost odgovora. "Programski arhitekt" daje drugačen nasvet kot "mlajši razvijalec" ali "varnostni revizor".

<img src="../../../translated_images/sl/role-based-prompting.a806e1a73de6e3a4.webp" alt="Pozivanje na podlagi vloge" width="800"/>

*Nastavljanje konteksta in osebnosti — isto vprašanje dobi drugačen odgovor glede na določeno vlogo*

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

**Kdaj uporabiti:** Pregledi kode, tutorstvo, analiza specifična za domeno ali kadar potrebujete odgovore, prilagojene določenemu nivoju strokovnosti ali perspektivi.

### Predloge pozivov

Ustvarite ponovno uporabne pozive z novimi zamenljivimi mesti. Namesto pisanja novega poziva vsakič, določite predlogo enkrat in polnite različne vrednosti. Razred `PromptTemplate` v LangChain4j to omogoča z sintakso `{{variable}}`.

<img src="../../../translated_images/sl/prompt-templates.14bfc37d45f1a933.webp" alt="Predloge pozivov" width="800"/>

*Ponovno uporabni pozivi z zamenljivimi mesti — ena predloga, več uporabe*

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

**Kdaj uporabiti:** Ponovljena vprašanja z različnimi vnosi, serijsko obdelavo, gradnjo ponovno uporabnih AI potekov ali katerikoli primer, ko struktura poziva ostaja enaka, podatki pa se spreminjajo.

---

Te pet osnovnih dajejo trden nabor orodij za večino nalog pozivanja. Preostanek tega modula izgradi na njih z **osmimi naprednimi vzorci**, ki izkoriščajo GPT-5.2 nadzor razmišljanja, samoocenjevanje in zmožnosti strukturiranega izhoda.

## Napredni vzorci

Ko smo osnove obdelali, poglejmo osem naprednih vzorcev, ki naredijo ta modul edinstven. Ne vsa vprašanja potrebujejo enak pristop. Nekatera vprašanja zahtevajo hitre odgovore, druga globoko razmišljanje. Nekatera potrebujejo vidno razmišljanje, druga samo rezultate. Vsak vzorec spodaj je optimiziran za drugačen scenarij — in GPT-5.2 nadzor razmišljanja naredi razlike še bolj izrazite.

<img src="../../../translated_images/sl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osem vzorcev pozivanja" width="800"/>

*Pregled osmih vzorcev inženiringa pozivov in njihovih uporab*

<img src="../../../translated_images/sl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Nadzor razmišljanja z GPT-5.2" width="800"/>

*Nadzor razmišljanja GPT-5.2 vam omogoča določiti, koliko naj model razmišlja — od hitrih neposrednih odgovorov do globokih raziskav*

**Nizka vnema (hitro in osredotočeno)** - Za preprosta vprašanja, kjer želite hitre, neposredne odgovore. Model izvaja minimalno razmišljanje - največ 2 koraka. Uporabite to za izračune, poizvedbe ali jasna vprašanja.

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

> 💡 **Raziskujte z GitHub Copilot:** Odprite [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) in vprašajte:
> - "Kakšna je razlika med nizko in visoko vnemo pri vzorcih pozivanja?"
> - "Kako XML oznake v pozivih pomagajo strukturirati AI odgovor?"
> - "Kdaj naj uporabim vzorce samorefleksije in kdaj neposredna navodila?"

**Visoka vnema (globoko in temeljito)** - Za kompleksne probleme, kjer želite poglobljeno analizo. Model temeljito raziskuje in prikazuje podrobno razmišljanje. Uporabite to za načrtovanje sistemov, arhitekturne odločitve ali zahtevne raziskave.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Izvajanje naloge (napredek korak za korakom)** - Za večstopenjske poteke dela. Model ponudi začetni načrt, opisuje vsak korak med delom, nato poda povzetek. Uporabite to za migracije, implementacije ali katerikoli večkorak proces.

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

Veriga razmišljanja izrecno zahteva, da model pokaže svoj proces razmišljanja, kar izboljšuje natančnost pri zahtevnih nalogah. Razčlenitev korak za korakom pomaga tako ljudem kot AI razumeti logiko.

> **🤖 Preizkusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Vprašajte o tem vzorcu:
> - "Kako prilagodim vzorec izvajanja naloge za dolgotrajne operacije?"
> - "Kakšne so najboljše prakse za strukturiranje uvodnih izjav orodij v produkcijskih aplikacijah?"
> - "Kako zajamem in prikažem vmesne napredke v uporabniškem vmesniku?"

<img src="../../../translated_images/sl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Vzorec izvajanja naloge" width="800"/>

*Načrtuj → Izvedi → Povzemi delovni tok za večkorakovne naloge*

**Samoreflektirajoča koda** - Za generiranje kode kakovosti primerne za produkcijo. Model generira kodo po produkcijskih standardih z ustreznim ravnanjem z napakami. Uporabite to pri gradnji novih funkcij ali storitev.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cikel samorefleksije" width="800"/>

*Iterativni cikel izboljšav - generiraj, ocenjuj, identificiraj težave, izboljšaj, ponovi*

**Strukturirana analiza** - Za dosledno ocenjevanje. Model pregleda kodo z uporabo fiksnega okvira (pravost, prakse, zmogljivost, varnost, vzdržljivost). Uporabite to za preglede kode ali ocene kakovosti.

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
> - "Kako prilagodim analitični okvir za različne vrste pregledov kode?"
> - "Kateri je najboljši način za programatično razčlenjevanje in uporabo strukturiranega izhoda?"
> - "Kako zagotovim dosledne nivoje resnosti skozi različne sesije pregledov?"

<img src="../../../translated_images/sl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Vzorec strukturirane analize" width="800"/>

*Okvir za dosledne preglede kode z nivoji resnosti*

**Večkročni klepet** - Za pogovore, ki potrebujejo kontekst. Model si zapomni prejšnja sporočila in gradi nanje. Uporabite to za interaktivno pomoč ali zahtevna vprašanja in odgovore.

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

*Kako se pogovorni kontekst kopiči skozi več krogov do dosega omejitve tokenov*

**Razmišljanje korak za korakom** - Za probleme, ki zahtevajo vidno logiko. Model pokaže izrecno razmišljanje za vsak korak. Uporabite to za matematične probleme, logične uganke ali kadar morate razumeti proces razmišljanja.

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

*Razbijanje problemov na izrecne logične korake*

**Omejen izhod** - Za odgovore s specifičnimi zahtevami glede formata. Model strogo sledi pravilom formata in dolžine. Uporabite to za povzetke ali kadar potrebujete precizno strukturo izhoda.

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

Prepričajte se, da datoteka `.env` obstaja v korenski mapi z Azure poverilnicami (ustvarjena med Modulom 01):
```bash
cat ../.env  # Prikazati bi moral AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Zaženite aplikacijo:**

> **Opomba:** Če ste že zagnali vse aplikacije z `./start-all.sh` iz Modula 01, ta modul že teče na pristanišču 8083. Lahko preskočite spodnje ukaze za zagon in neposredno nadaljujete na http://localhost:8083.
**Možnost 1: Uporaba Spring Boot nadzorne plošče (Priporočeno za uporabnike VS Code)**

Razvojni kontejner vključuje razširitev Spring Boot Dashboard, ki zagotavlja vizualni vmesnik za upravljanje vseh aplikacij Spring Boot. Najdete jo v vrstici z dejavnostmi na levi strani VS Code (poiščite ikono Spring Boot).

Iz Spring Boot nadzorne plošče lahko:
- Vidite vse razpoložljive aplikacije Spring Boot v delovnem prostoru
- Zaženete/ustavite aplikacije z enim klikom
- V realnem času ogledate dnevniške zapise aplikacij
- Spremljate stanje aplikacij

Preprosto kliknite gumb za predvajanje poleg "prompt-engineering" za zagon tega modula ali pa zaženite vse module naenkrat.

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

Ali zaženite le ta modul:

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

Obe skripti samodejno naložita okoljske spremenljivke iz osnovne `.env` datoteke in bodo zgradili JAR-je, če ti ne obstajajo.

> **Opomba:** Če želite pred zagonom ročno zgraditi vse module:
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

Odprite http://localhost:8083 v svojem brskalniku.

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

<img src="../../../translated_images/sl/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Glavna nadzorna plošča, ki prikazuje vseh 8 vzorcev promptnega inženiringa z njihovimi značilnostmi in področji uporabe*

## Raziskovanje vzorcev

Spletni vmesnik vam omogoča eksperimentiranje z različnimi strategijami promptanja. Vsak vzorec rešuje različne težave - preizkusite jih in preverite, kdaj kateri pristop najbolje deluje.

> **Opomba: Pretakanje proti nepretakanju** — Vsaka stran vzorca ponuja dva gumba: **🔴 Pretakanje odgovora (v živo)** in možnost **Ne-pretakanje**. Pretakanje uporablja Server-Sent Events (SSE) za prikazovanje tokenov v realnem času med generiranjem modela, tako da takoj vidite potek. Ne-pretakanje počaka na celoten odgovor, preden ga prikaže. Pri promptih, ki sprožijo globoko razmišljanje (npr. Visoka želja, Samoreflektirajoča koda), lahko nepretakanje traja zelo dolgo — včasih minute — brez vidne povratne informacije. **Za eksperimentiranje z zapletenimi prompti uporabite pretakanje**, da boste videli, kako model dela, in se izognili vtisu, da je zahteva potekla.
>
> **Opomba: Zahteva brskalnika** — Funkcija pretakanja uporablja Fetch Streams API (`response.body.getReader()`), ki zahteva poln brskalnik (Chrome, Edge, Firefox, Safari). Ne deluje v vgrajenem preprostem brskalniku VS Code, saj njegov spletni pogled ne podpira ReadableStream API-ja. Če uporabljate Preprost brskalnik, bodo gumbi za ne-pretakanje še vedno delovali normalno — le gumbi za pretakanje so prizadeti. Za polno izkušnjo odprite `http://localhost:8083` v zunanjem brskalniku.

### Nizka proti visoki želji

Postavite preprosto vprašanje, kot je "Koliko je 15 % od 200?" z nizko željo. Dobite takojšen, direkten odgovor. Zdaj postavite nekaj zapletenega, na primer "Oblikuj strategijo predpomnjenja za API z veliko obremenitvijo" z visoko željo. Kliknite **🔴 Pretakanje odgovora (v živo)** in opazujte, kako se model podrobno razmišlja token za tokenom. Enak model, enaka struktura vprašanja - a prompt mu pove, koliko razmišljanja naj vloži.

### Izvedba nalog (orodja za preample)

Večstopenjski delovni tokovi imajo koristi od predhodnega načrtovanja in pripovedovanja o poteku. Model na začetku opiše, kaj bo naredil, nato pripoveduje vsak korak in na koncu povzame rezultate.

### Samoreflektirajoča koda

Preizkusite "Ustvari storitev za preverjanje elektronskih naslovov". Namesto da bi samo generiral kodo in ustavil, model generira, oceni glede na merila kakovosti, ugotovi slabosti in izboljša. Videli boste ponavljanje, dokler koda ne doseže standardov za produkcijo.

### Strukturirana analiza

Pregledi kode potrebujejo dosledne ocenjevalne okvire. Model analizira kodo po določenih kategorijah (pravilnost, prakse, zmogljivost, varnost) z različnimi stopnjami resnosti.

### Večkratni pogovor

Vprašajte "Kaj je Spring Boot?" nato takoj nadaljujte z "Prikaži mi primer". Model si zapomni prvo vprašanje in vam podaja primer Spring Boot posebej. Brez spomina bi bilo drugo vprašanje preveč nejasno.

### Razmišljanje korak za korakom

Izberite matematično nalogo in jo poskusite rešiti z Razmišljanjem korak za korakom in z nizko željo. Nizka želja vam samo poda odgovor - hitro, a nejasno. Razmišljanje korak za korakom pa prikaže vsak izračun in odločitev.

### Omejen izhod

Ko potrebujete specifične formate ali število besed, ta vzorec strogo zagotavlja skladnost. Poskusite ustvariti povzetek z natanko 100 besedami v obliki točk.

## Kaj se pravzaprav učite

**Razmišljalna prizadevanja spreminjajo vse**

GPT-5.2 vam omogoča nadzor nad računalniškim naporom preko vaših promptov. Nizek napor pomeni hitre odzive z minimalnim raziskovanjem. Visok napor pomeni, da si model vzame čas za globoko razmišljanje. Učite se uskladiti napor s kompleksnostjo naloge - ne izgubljajte časa s preprostimi vprašanji, a tudi ne hitite pri zapletenih odločitvah.

**Struktura usmerja vedenje**

Opazili ste XML oznake v promptih? Niso le dekoracija. Modeli sledijo strukturiranim navodilom bolj zanesljivo kot prostemu besedilu. Ko potrebujete večstopenjske postopke ali zapleteno logiko, struktura pomaga modelu slediti, kje je in kaj sledi.

<img src="../../../translated_images/sl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomija dobro strukturiranega prompta s jasnimi razdelki in XML-stilom organizacije*

**Kakovost skozi samoocenjevanje**

Samoreflektirajoči vzorci delujejo tako, da naredijo merila kakovosti eksplicitna. Namesto da upate, da model "naredi prav", mu natančno poveste, kaj pomeni "prav": pravilna logika, obravnava napak, zmogljivost, varnost. Nato lahko model sam oceni svoje izhode in izboljša. S tem se generiranje kode spremeni iz loterije v proces.

**Kontekst je končen**

Večkratni pogovori delujejo tako, da vsaki zahtevi dodajo zgodovino sporočil. A obstaja meja - vsak model ima maksimalno število tokenov. Ko pogovori rastejo, boste potrebovali strategije, da ohranite relevanten kontekst, ne da bi dosegli ta limit. Ta modul prikazuje, kako deluje spomin; kasneje se boste naučili, kdaj povzeti, kdaj pozabiti in kdaj pridobiti.

## Naslednji koraki

**Naslednji modul:** [03-rag - RAG (Generiranje z iskalnim pristopom)](../03-rag/README.md)

---

**Navigacija:** [← Prejšnji: Modul 01 - Uvod](../01-introduction/README.md) | [Nazaj na začetek](../README.md) | [Naslednji: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo AI prevajalske storitve [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas opozarjamo, da lahko avtomatizirani prevodi vsebujejo napake ali netočnosti. Izvirni dokument v njegovem izvirnem jeziku se šteje za avtoritativni vir. Za pomembne informacije priporočamo strokovni človeški prevod. Za morebitna nesporazume ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda, ne prevzemamo odgovornosti.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->