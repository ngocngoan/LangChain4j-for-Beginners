# Modul 02: Inženiring pozivov z GPT-5.2

## Kazalo

- [Video vodič](../../../02-prompt-engineering)
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
- [Zaženite aplikacijo](../../../02-prompt-engineering)
- [Posnetki zaslona aplikacije](../../../02-prompt-engineering)
- [Raziskovanje vzorcev](../../../02-prompt-engineering)
  - [Nizka proti visoki zavzetosti](../../../02-prompt-engineering)
  - [Izvajanje nalog (uvodniki orodij)](../../../02-prompt-engineering)
  - [Koda za samorefleksijo](../../../02-prompt-engineering)
  - [Strukturirana analiza](../../../02-prompt-engineering)
  - [Večokratni klepet](../../../02-prompt-engineering)
  - [Razmišljanje korak za korakom](../../../02-prompt-engineering)
  - [Omejen izhod](../../../02-prompt-engineering)
- [Kaj se resnično učite](../../../02-prompt-engineering)
- [Nadaljnji koraki](../../../02-prompt-engineering)

## Video vodič

Oglejte si to v živo predavanje, ki razlaga, kako začeti z modulom:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Inženiring pozivov z LangChain4j - v živo" width="800"/></a>

## Kaj se boste naučili

Naslednji diagram prikazuje pregled ključnih tem in veščin, ki jih boste razvili v tem modulu — od tehnik izboljševanja pozivov do korak za korakom poteka, ki ga boste sledili.

<img src="../../../translated_images/sl/what-youll-learn.c68269ac048503b2.webp" alt="Kaj se boste naučili" width="800"/>

V prejšnjih modulih ste spoznali osnovne interakcije LangChain4j z modeli GitHub in videli, kako spomin omogoča konverzacijsko AI z Azure OpenAI. Zdaj se bomo osredotočili na to, kako postavljate vprašanja — torej same pozive — z uporabo GPT-5.2 Azure OpenAI. Način, kako strukturirate svoje pozive, drastično vpliva na kakovost prejetih odgovorov. Začnemo z pregledom osnovnih tehnik pozivanja, nato pa preidemo na osem naprednih vzorcev, ki v celoti izkoriščajo zmogljivosti GPT-5.2.

Uporabljali bomo GPT-5.2, ker uvaja nadzor nad razmišljanjem — lahko modelu poveste, koliko razmišljanja naj opravi pred odgovorom. To naredi različne strategije pozivanja bolj opazne in pomaga razumeti, kdaj uporabiti kateri pristop. Prav tako bomo imeli koristi od manjših omejitev hitrosti v Azure za GPT-5.2 v primerjavi z modeli GitHub.

## Predpogoji

- Zaključen Modul 01 (nameščeni Azure OpenAI viri)
- `.env` datoteka v glavni mapi z Azure poverilnicami (ustvarjena z `azd up` v Modulu 01)

> **Opomba:** Če niste zaključili Modula 01, najprej sledite tam navodilom za namestitev.

## Razumevanje inženiringa pozivov

V jedru je inženiring pozivov razlika med nejasnimi in natančnimi navodili, kot kaže spodnja primerjava.

<img src="../../../translated_images/sl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Kaj je inženiring pozivov?" width="800"/>

Inženiring pozivov pomeni oblikovanje vhodnega besedila, ki vam dosledno prinese želene rezultate. Ne gre samo za postavljanje vprašanj — gre za strukturiranje zahtev, da model natančno razume, kaj želite in kako to zagotoviti.

Pomislite na to kot da dajete navodila sodelavcu. "Odpravi napako" je nejasno. "Odpravi izjemo na null pointer v UserService.java vrstica 45 z dodajanjem preverjanja na null" je specifično. Jezikovni modeli delujejo enako — pomembna sta natančnost in struktura.

Spodnji diagram prikazuje, kako LangChain4j ustreza tej sliki — povezuje vaše vzorce pozivov z modelom preko gradnikov SystemMessage in UserMessage.

<img src="../../../translated_images/sl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kako LangChain4j ustreza" width="800"/>

LangChain4j zagotavlja infrastrukturo — povezave do modelov, spomin in tipe sporočil — medtem ko so vzorci pozivov le skrbno strukturirano besedilo, ki ga pošljete skozi to infrastrukturo. Ključna gradnika sta `SystemMessage` (ki nastavi vedenje in vlogo AI) in `UserMessage` (ki nosi vašo dejansko zahtevo).

## Osnove inženiringa pozivov

Pet osnovnih tehnik, prikazanih spodaj, tvori temelj učinkovitega inženiringa pozivov. Vsaka naslavlja drugačen vidik, kako komunicirate z jezikovnimi modeli.

<img src="../../../translated_images/sl/five-patterns-overview.160f35045ffd2a94.webp" alt="Pregled petih vzorcev inženiringa pozivov" width="800"/>

Preden se poglobimo v napredne vzorce tega modula, si poglejmo pet temeljev tehnik pozivanja. To so gradniki, ki jih mora poznati vsak inženir pozivov. Če ste že delali skozi [Hitri začetek modul](../00-quick-start/README.md#2-prompt-patterns), ste jih že videli v akciji — tukaj je konceptualni okvir zanje.

### Zero-Shot pozivanje

Najpreprostejši pristop: modelu daste neposredno navodilo brez primerov. Model se v celoti zanaša na svoje usposabljanje, da razume in izvede nalogo. To deluje dobro za preproste zahteve, kjer je pričakovano vedenje očitno.

<img src="../../../translated_images/sl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot pozivanje" width="800"/>

*Neposredno navodilo brez primerov — model razbere nalogo samo iz navodila*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odgovor: "Pozitivno"
```

**Kdaj uporabiti:** Enostavne klasifikacije, neposredna vprašanja, prevodi ali katerakoli naloga, ki jo model lahko izvede brez dodatnih navodil.

### Few-Shot pozivanje

Podajte primere, ki pokažejo vzorec, ki ga želite, da model sledi. Model se iz vaših primerov nauči pričakovanega vhodno-izhodnega formata in ga uporabi na novih vhodih. To močno izboljša doslednost za naloge, kjer želeni format ali vedenje ni očitno.

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

Model prosite, naj pokaže svoje razmišljanje korak za korakom. Namesto da skoči neposredno do odgovora, model razgradi problem in razločno obdela vsak del. To izboljša natančnost pri matematičnih, logičnih in večstopenjskih nalogah.

<img src="../../../translated_images/sl/chain-of-thought.5cff6630e2657e2a.webp" alt="Veriga misli pozivanja" width="800"/>

*Razmišljanje korak za korakom — razbijanje kompleksnih problemov v eksplicitne logične korake*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model prikazuje: 15 - 8 = 7, nato 7 + 12 = 19 jabolk
```

**Kdaj uporabiti:** Matematične težave, logične uganke, odpravljanje napak ali katera koli naloga, kjer prikaz procesa razmišljanja izboljša natančnost in zaupanje.

### Pozivanje na podlagi vlog

Nastavite persono ali vlogo za AI pred postavitvijo vprašanja. To daje kontekst, ki oblikuje ton, globino in fokus odgovora. "Programski arhitekt" daje drugačne nasvete kot "mlajši razvijalec" ali "varnostni revizor".

<img src="../../../translated_images/sl/role-based-prompting.a806e1a73de6e3a4.webp" alt="Pozivanje na podlagi vlog" width="800"/>

*Nastavljanje konteksta in persone — isto vprašanje dobi različne odgovore glede na dodeljeno vlogo*

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

**Kdaj uporabiti:** Pregledi kode, poučevanje, domeno specifične analize ali kadar potrebujete odgovore prilagojene določeni ravni strokovnosti ali perspektivi.

### Predloge pozivov

Ustvarite ponovno uporabne pozive s spremenljivkami kot držali. Namesto da vsakokrat pišete nov poziv, definirajte predlogo enkrat in napolnite različne vrednosti. Razred `PromptTemplate` v LangChain4j to olajša s sintakso `{{variable}}`.

<img src="../../../translated_images/sl/prompt-templates.14bfc37d45f1a933.webp" alt="Predloge pozivov" width="800"/>

*Ponovno uporabni pozivi s spremenljivkami kot držali — ena predloga, več uporab*

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

**Kdaj uporabiti:** Ponavljane poizvedbe z različnimi vhodi, paketno obdelavo, gradnjo ponovno uporabnih AI potekov dela ali kakršen koli scenarij, kjer struktura poziva ostaja enaka, podatki pa se spreminjajo.

---

Te pet osnovnih tehnik vam daje trden komplet orodij za večino nalog pozivanja. Preostanek tega modula gradi na njih s **osmimi naprednimi vzorci**, ki uporabljajo nadzor nad razmišljanjem GPT-5.2, samoocenjevanje in zmogljivosti strukturiranega izhoda.

## Napredni vzorci

Ko smo pokrili osnove, preidimo k osmim naprednim vzorcem, ki naredijo ta modul unikaten. Ne vsi problemi zahtevajo enak pristop. Nekatera vprašanja potrebujejo hitre odgovore, druga globoko razmišljanje. Nekatera potrebujejo vidno razmišljanje, druga samo rezultate. Vsak vzorec spodaj je optimiziran za drugačen scenarij — in nadzor razmišljanja GPT-5.2 naredi razlike še bolj izrazite.

<img src="../../../translated_images/sl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osem vzorcev pozivanja" width="800"/>

*Pregled osmih vzorcev inženiringa pozivov in njihovi primeri uporabe*

GPT-5.2 dodaja še eno dimenzijo tem vzorcem: *nadzor razmišljanja*. Drsnik spodaj prikazuje, kako lahko prilagodite trud razmišljanja modela — od hitrih neposrednih odgovorov do globoke, temeljite analize.

<img src="../../../translated_images/sl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Nadzor razmišljanja z GPT-5.2" width="800"/>

*Nadzor razmišljanja GPT-5.2 omogoča, da določite, koliko razmišljanja mora model opraviti — od hitrih neposrednih odgovorov do globokega raziskovanja*

**Nizka zavzetost (hitro in osredotočeno)** - Za enostavna vprašanja, kjer želite hitre, neposredne odgovore. Model opravi minimalno razmišljanje - največ 2 koraka. Uporabite to za izračune, poizvedbe ali neposredna vprašanja.

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
> - "Kakšna je razlika med nizko in visoko zavzetostjo vzorcev pozivanja?"
> - "Kako XML oznake v pozivih pomagajo strukturirati AI odgovor?"
> - "Kdaj naj uporabim vzorce za samorefleksijo in kdaj neposredna navodila?"

**Visoka zavzetost (globoko in temeljito)** - Za kompleksne probleme, kjer želite podrobno analizo. Model tukaj temeljito raziskuje in pokaže podrobno razmišljanje. Uporabite to za sistemske načrte, odločitve o arhitekturi ali kompleksne raziskave.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Izvajanje nalog (napredek korak za korakom)** - Za večstopenjske poteke dela. Model posreduje načrt vnaprej, opisuje vsak korak med delom in nato poda povzetek. Uporabite to za migracije, implementacije ali katerikoli večstopenjski proces.

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

Veriga misli pozivanje izrecno zahteva od modela, da pokaže svoj proces razmišljanja, kar izboljšuje natančnost pri zahtevnih nalogah. Razčlenitev korak za korakom pomaga tako ljudem kot AI razumeti logiko.

> **🤖 Preizkusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Vprašajte o tem vzorcu:
> - "Kako bi prilagodil vzorec izvajanja nalog za dolgo trajajoče operacije?"
> - "Kakšne so najboljše prakse za strukturiranje uvodnikov orodij v produkcijskih aplikacijah?"
> - "Kako lahko zajamem in prikažem posredne posodobitve napredka v uporabniškem vmesniku?"

Spodnji diagram ponazarja ta potek dela Načrtuj → Izvedi → Povzemi.

<img src="../../../translated_images/sl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Vzorčni potek izvajanja nalog" width="800"/>

*Načrtuj → Izvedi → Povzemi potek dela za večstopenjske naloge*

**Koda za samorefleksijo** - Za generiranje kode produkcijske kakovosti. Model generira kodo po produkcijskih standardih z ustreznim upravljanjem napak. Uporabite to pri gradnji novih funkcionalnosti ali storitev.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

Spodnji diagram prikazuje ta iterativni zanko izboljševanja — generiraj, oceni, prepoznaj slabosti in izboljšaj, dokler koda ne doseže produkcijskih standardov.

<img src="../../../translated_images/sl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cikel samorefleksije" width="800"/>

*Iterativna zanka izboljševanja - generiraj, oceni, prepoznaj težave, izboljšaj, ponovi*

**Strukturirana analiza** - Za dosledno ocenjevanje. Model pregleda kodo z uporabo fiksnega okvira (pravilnost, prakse, zmogljivost, varnost, vzdržljivost). Uporabite to za preglede kode ali ocene kakovosti.

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
> - "Kako lahko prilagodim analitični okvir za različne vrste pregledov kode?"
> - "Kateri je najboljši način za programatično razčlenjevanje in ukrepanje na strukturiran izhod?"
> - "Kako zagotovim dosledne nivoje resnosti skozi različne seje pregledov?"

Spodnji diagram prikazuje, kako ta strukturirani okvir organizira pregled kode v dosledne kategorije z nivoji resnosti.

<img src="../../../translated_images/sl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Vzorčni okvir strukturirane analize" width="800"/>

*Okvir za dosledne preglede kode z nivoji resnosti*

**Večkratni klepet** - Za pogovore, ki potrebujejo kontekst. Model si zapomni prejšnja sporočila in gradi nanje. Uporabite to za interaktivne podporne seje ali kompleksna vprašanja in odgovore.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

Spodnji diagram ponazarja, kako se kontekst pogovora kopiči z vsakim krogom in kako se to nanaša na omejitev modelovih tokenov.

<img src="../../../translated_images/sl/context-memory.dff30ad9fa78832a.webp" alt="Spomin konteksta" width="800"/>

*Kako se kontekst pogovora kopiči skozi več krogov do dosega omejitve tokenov*
**Korak za korakom razmišljanje** - Za probleme, ki zahtevajo vidno logiko. Model pokaže eksplicitno razmišljanje za vsak korak. Uporabite to za matematične probleme, logične uganke ali ko morate razumeti proces razmišljanja.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

Diagram spodaj prikazuje, kako model razdeli probleme na eksplicitne, številčene logične korake.

<img src="../../../translated_images/sl/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Korak za korakom vzorec" width="800"/>

*Razčlenjevanje problemov na eksplicitne logične korake*

**Omejen izhod** - Za odzive s posebnimi zahtevami za format. Model strogo upošteva pravila za format in dolžino. Uporabite to za povzetke ali ko potrebujete natančno strukturo izhoda.

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

Naslednji diagram kaže, kako omejitve usmerjajo model, da ustvari izhod, ki strogo sledi vašim zahtevam glede formata in dolžine.

<img src="../../../translated_images/sl/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Vzorec omejenega izhoda" width="800"/>

*Uveljavljanje specifičnih zahtev za format, dolžino in strukturo*

## Zagon aplikacije

**Preverite namestitev:**

Poskrbite, da datoteka `.env` obstaja v korenski mapi z Azure poverilnicami (ustvarjena v Modul 01). Zaženite to iz mape modula (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # Naj pokaže AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Prikazati bi moralo AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Začnite aplikacijo:**

> **Opomba:** Če ste že zagnali vse aplikacije z `./start-all.sh` iz korenske mape (kot je opisano v Modul 01), ta modul že teče na portu 8083. Lahko preskočite ukaze za začetek spodaj in pojdite neposredno na http://localhost:8083.

**Možnost 1: Uporaba Spring Boot Dashboard (Priporočeno za uporabnike VS Code)**

Razvojno okolje vsebuje razširitev Spring Boot Dashboard, ki omogoča vizualni vmesnik za upravljanje vseh Spring Boot aplikacij. Najdete jo v vrstici z aktivnostmi na levi strani VS Code (poiščite ikono Spring Boot).

Iz Spring Boot Dashboarda lahko:
- Vidite vse razpoložljive Spring Boot aplikacije v delovnem prostoru
- Zaženete/ustavite aplikacije z enim klikom
- V živo spremljate dnevnike aplikacij
- Nadzorujete stanje aplikacij

Preprosto kliknite gumb za predvajanje zraven "prompt-engineering", da zaženete ta modul, ali zaženite vse module naenkrat.

<img src="../../../translated_images/sl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot nadzorna plošča" width="400"/>

*Spring Boot Dashboard v VS Code — začni, ustavi in nadziraj vse module na enem mestu*

**Možnost 2: Uporaba shell skript**

Zaženite vse spletne aplikacije (moduli 01-04):

**Bash:**
```bash
cd ..  # Iz korenskega imenika
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iz korenske mape
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

Obe skripti samodejno naložita okoljske spremenljivke iz korenske datoteke `.env` in ustvarita JAR datoteke, če še ne obstajajo.

> **Opomba:** Če želite zgraditi vse module ročno pred zagonom:
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

**Za ustavitev:**

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

Tukaj je glavni vmesnik modula za prompt engineering, kjer lahko poskusite vseh osem vzorcev drug ob drugem.

<img src="../../../translated_images/sl/dashboard-home.5444dbda4bc1f79d.webp" alt="Glavna stran nadzorne plošče" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Glavna nadzorna plošča z vsemi 8 vzorci za prompt engineering z njihovimi značilnostmi in primeri uporabe*

## Raziskovanje vzorcev

Spletni vmesnik vam omogoča, da eksperimentirate z različnimi strategijami pozivanja. Vsak vzorec rešuje različne težave – preizkusite jih, da vidite, kdaj kateri pristop najbolje deluje.

> **Opomba: Tokovno proti netokovnemu** — Vsaka stran vzorca ponuja dva gumba: **🔴 Tokovni odgovor (v živo)** in možnost **Netokovno**. Tokovno uporablja Server-Sent Events (SSE), da v realnem času prikazuje tokene, ko jih model ustvarja, tako da napredek takoj vidite. Netokovna možnost počaka, da se celoten odgovor ustvari, preden ga prikaže. Za pozive, ki sprožijo globoko razmišljanje (npr. High Eagerness, Self-Reflecting Code), netokovni klic lahko traja zelo dolgo – tudi minute – brez vidne povratne informacije. **Pri eksperimentiranju s kompleksnimi pozivi uporabljajte tokovno,** da boste lahko videli model v akciji in se izognili vtisu, da je zahteva potekla.
>
> **Opomba: Zahteve brskalnika** — Tokovna funkcija uporablja Fetch Streams API (`response.body.getReader()`), ki zahteva poln brskalnik (Chrome, Edge, Firefox, Safari). Ne deluje v vgrajenem preprostem brskalniku VS Code, saj njegova spletna ogledala ne podpirajo ReadableStream API. Če uporabljate Simple Browser, bodo netokovni gumbi še vedno delovali normalno – samo tokovni so vplivani. Odprite `http://localhost:8083` v zunanjem brskalniku za polno izkušnjo.

### Nizka proti visoki zavzetosti

Postavite preprosto vprašanje, kot je "Koliko je 15 % od 200?" z nizko zavzetostjo. Dobite takojšen, neposreden odgovor. Zdaj postavite nekaj zahtevnejšega, na primer "Oblikuj strategijo predpomnjenja za API z veliko obremenitvijo" z visoko zavzetostjo. Kliknite **🔴 Tokovni odgovor (v živo)** in opazujte podrobno razmišljanje modela, ki se prikaže token za tokenom. Enak model, enaka struktura vprašanja – a poziv mu pove, koliko razmišljanja mora vložiti.

### Izvajanje naloge (orodna uvodna sporočila)

Večstopenjski delovni procesi imajo koristi od načrtovanja vnaprej in pripovedovanja o napredku. Model predstavi, kaj bo naredil, predstavi vsak korak in nato povzame rezultate.

### Samoreflektirajoča koda

Poskusite "Ustvari storitev za preverjanje e-pošte". Namesto, da bi samo generiral kodo in se ustavil, model ustvari, ovrednoti glede na merila kakovosti, opredeli slabosti in izboljšuje. Videli boste, kako iterira, dokler koda ne doseže proizvodnih standardov.

### Strukturirana analiza

Pregledi kode potrebujejo konzistentne ocenjevalne okvire. Model analizira kodo z uporabo fiksnih kategorij (pravilnost, prakse, zmogljivost, varnost) z različnimi stopnjami resnosti.

### Večkratni klepet

Vprašajte "Kaj je Spring Boot?" nato takoj nadaljujte z "Pokaži mi primer". Model se spomni vašega prvega vprašanja in vam posebej poda primer Spring Boot. Brez spomina bi bilo drugo vprašanje preveč nejasno.

### Korak za korakom razmišljanje

Izberite matematični problem in ga poskusite tako z razmišljanjem korak za korakom kot z nizko zavzetostjo. Nizka zavzetost vam samo da odgovor - hitro, a nejasno. Korak za korakom prikaže vsak izračun in odločitev.

### Omejen izhod

Ko potrebujete specifične formate ali število besed, ta vzorec zagotavlja strogo upoštevanje. Poskusite ustvariti povzetek z natanko 100 besedami v obliki alinej.

## Kaj se pravzaprav učite

**Napori pri razmišljanju spreminjajo vse**

GPT-5.2 vam omogoča nadzor nad računalniškim naporom preko vaših pozivov. Nizki napori pomenijo hitre odgovore z minimalno raziskavo. Visoki napori pomenijo, da model potrebuje čas za globoko razmišljanje. Učite se prilagajati napor kompleksnosti naloge - ne izgubljajte časa s preprostimi vprašanji, a tudi ne hitite pri zahtevnih odločitvah.

**Struktura usmerja vedenje**

Opazili ste XML oznake v pozivih? Niso dekorativne. Modeli bolje sledijo strukturiranim navodilom kot prostemu besedilu. Ko potrebujete večstopenjske procese ali zapleteno logiko, struktura pomaga modelu slediti, kje je in kaj sledi. Diagram spodaj razčleni dobro strukturiran poziv, ki prikazuje, kako oznake, kot so `<system>`, `<instructions>`, `<context>`, `<user-input>` in `<constraints>` organizirajo vaša navodila v jasne sekcije.

<img src="../../../translated_images/sl/prompt-structure.a77763d63f4e2f89.webp" alt="Struktura poziva" width="800"/>

*Anatomija dobro strukturiranega poziva z jasnimi sekcijami in XML-stilno organizacijo*

**Kakovost prek samoevalvacije**

Vzorec samoreflektiranja deluje tako, da kakovostna merila naredi eksplicitna. Namesto da bi upali, da model "naredi prav", mu točno poveste, kaj "prav" pomeni: pravilna logika, obdelava napak, zmogljivost, varnost. Model lahko nato oceni svoj izhod in ga izboljša. Tako generiranje kode ne postane loterija, ampak proces.

**Kontekst je omejen**

Večkratni pogovori delujejo tako, da imajo vključeno zgodovino sporočil z vsakim zahtevkom. A obstaja meja - vsak model ima maksimalno število tokenov. Ko pogovori rastejo, boste potrebovali strategije za ohranjanje relevantnega konteksta brez preseganja te meje. Ta modul vam pokaže, kako deluje spomin; pozneje se boste naučili, kdaj povzeti, kdaj pozabiti in kdaj poiskati.

## Naslednji koraki

**Naslednji modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigacija:** [← Prejšnje: Modul 01 - Uvod](../01-introduction/README.md) | [Nazaj na glavno](../README.md) | [Naslednje: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo AI prevajalske storitve [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, upoštevajte, da lahko samodejni prevodi vsebujejo napake ali netočnosti. Izvirni dokument v njegovem maternem jeziku naj velja za zavezujoči vir. Za kritične informacije priporočamo strokovni človeški prevod. Nismo odgovorni za morebitne nesporazume ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->