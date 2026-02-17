# Modul 02: Inženiring pozivov z GPT-5.2

## Kazalo

- [Kaj se boste naučili](../../../02-prompt-engineering)
- [Pogoji za začetek](../../../02-prompt-engineering)
- [Razumevanje inženiringa pozivov](../../../02-prompt-engineering)
- [Osnove inženiringa pozivov](../../../02-prompt-engineering)
  - [Zero-Shot pozivanje](../../../02-prompt-engineering)
  - [Few-Shot pozivanje](../../../02-prompt-engineering)
  - [Veriga misli](../../../02-prompt-engineering)
  - [Pozivanje na osnovi vloge](../../../02-prompt-engineering)
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
  - [Korak za korakom razmišljanje](../../../02-prompt-engineering)
  - [Omejen izhod](../../../02-prompt-engineering)
- [Kaj se v resnici učite](../../../02-prompt-engineering)
- [Nadaljnji koraki](../../../02-prompt-engineering)

## Kaj se boste naučili

<img src="../../../translated_images/sl/what-youll-learn.c68269ac048503b2.webp" alt="Kaj se boste naučili" width="800"/>

V prejšnjem modulu ste videli, kako pomnilnik omogoča pogovorni AI in uporabili modele GitHub za osnovne interakcije. Zdaj se bomo osredotočili na to, kako postavljate vprašanja – same pozive – z uporabo GPT-5.2 iz Azure OpenAI. Način, kako strukturirate pozive, dramatično vpliva na kakovost odgovorov, ki jih prejmete. Začnemo z revizijo osnovnih tehnik pozivanja, nato pa preidemo na osem naprednih vzorcev, ki v celoti izkoristijo zmogljivosti GPT-5.2.

Uporabljali bomo GPT-5.2, ker uvaja nadzor razmišljanja – lahko modelu poveste, koliko naj razmišlja pred odgovorom. To naredi različne strategije pozivanja bolj vidne in pomaga razumeti, kdaj uporabiti kateri pristop. Prav tako bomo imeli korist od manj omejitev hitrosti za GPT-5.2 v primerjavi z modeli GitHub.

## Pogoji za začetek

- Zaključen Modul 01 (nameščeni viri Azure OpenAI)
- `.env` datoteka v korenski mapi z Azure poverilnicami (ustvarjena z `azd up` v Modulu 01)

> **Opomba:** Če Modula 01 še niste zaključili, sledite najprej tamkajšnjim navodilom za namestitev.

## Razumevanje inženiringa pozivov

<img src="../../../translated_images/sl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Kaj je inženiring pozivov?" width="800"/>

Inženiring pozivov je oblikovanje vhodnega besedila, ki vam dosledno prinaša želeni rezultat. Ni samo vprašanje postavljanje – gre za strukturiranje zahtev, da model natančno razume, kaj želite, in kako to dostaviti.

Pomislite, kot da dajete navodila sodelavcu. "Popravi napako" je nejasno. "Popravi izjemo null pointer v UserService.java vrstica 45 z dodatkom preverjanja null" je specifično. Jezikovni modeli delujejo na enak način – pomembna sta specifičnost in struktura.

<img src="../../../translated_images/sl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kako se LangChain4j vklopi" width="800"/>

LangChain4j zagotavlja infrastrukturo — povezave modelov, pomnilnik in vrste sporočil — medtem ko so vzorci pozivov le skrbno strukturirano besedilo, ki ga pošljete skozi to infrastrukturo. Ključni gradniki so `SystemMessage` (ki nastavi vedenje in vlogo AI) ter `UserMessage` (ki nosi vašo dejansko zahtevo).

## Osnove inženiringa pozivov

<img src="../../../translated_images/sl/five-patterns-overview.160f35045ffd2a94.webp" alt="Pregled petih vzorcev inženiringa pozivov" width="800"/>

Preden se poglobimo v napredne vzorce tega modula, si poglejmo pet temeljnih tehnik pozivanja. To so gradniki, ki jih mora poznati vsak inženir pozivov. Če ste že delali skozi [modul hitrega začetka](../00-quick-start/README.md#2-prompt-patterns), ste jih že videli v praksi — tukaj je konceptualni okvir za njimi.

### Zero-Shot pozivanje

Najpreprostejši pristop: modelu dajte neposredno navodilo brez primerov. Model povsem zanaša na svoje učenje, da razume in izvede nalogo. To dobro deluje pri preprostih zahtevah, kjer je pričakovano vedenje očitno.

<img src="../../../translated_images/sl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot pozivanje" width="800"/>

*Neposredno navodilo brez primerov – model izvede nalogo na podlagi navodila*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odgovor: "Pozitivno"
```

**Kdaj uporabiti:** Enostavne klasifikacije, neposredna vprašanja, prevodi ali katera koli naloga, ki jo model zmore brez dodatnih navodil.

### Few-Shot pozivanje

Podajte primere, ki pokažejo vzorec, ki ga želite, da model sledi. Model se iz vaših primerov nauči pričakovani vhodno-izhodni format in ga uporabi pri novih vnosih. To pomembno izboljša doslednost pri nalogah, kjer zahtevani format ali vedenje ni očitno.

<img src="../../../translated_images/sl/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot pozivanje" width="800"/>

*Učenje iz primerov – model prepozna vzorec in ga uporabi na novih vhodih*

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

**Kdaj uporabiti:** Prilagojene klasifikacije, dosledno oblikovanje, domeno specifične naloge ali kadar so zero-shot rezultati nedosledni.

### Veriga misli

Prosite model, naj pokaže svoje razmišljanje korak za korakom. Namesto da skoči neposredno na odgovor, model razdeli problem in obravnava vsak del posebej. To poveča natančnost pri matematičnih, logičnih in večkorakih nalogah.

<img src="../../../translated_images/sl/chain-of-thought.5cff6630e2657e2a.webp" alt="Pozivanje z verigo misli" width="800"/>

*Korak za korakom razmišljanje – razčlenjevanje kompleksnih problemov na izrecne logične korake*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model prikazuje: 15 - 8 = 7, nato 7 + 12 = 19 jabolk
```

**Kdaj uporabiti:** Matematični problemi, logične uganke, odpravljanje napak ali katera koli naloga, kjer prikaz razmišljanja poveča natančnost in zaupanje.

### Pozivanje na osnovi vloge

Nastavite osebnost ali vlogo AI pred postavitvijo vprašanja. To zagotavlja kontekst, ki oblikuje ton, globino in osredotočenost odgovora. "Programski arhitekt" daje drugačna priporočila kot "mlajši razvijalec" ali "revizor varnosti".

<img src="../../../translated_images/sl/role-based-prompting.a806e1a73de6e3a4.webp" alt="Pozivanje na osnovi vloge" width="800"/>

*Nastavitev konteksta in osebnosti – isto vprašanje dobi drugačen odgovor glede na dodeljeno vlogo*

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

**Kdaj uporabiti:** Pregledi kode, mentorstvo, domeno specifične analize ali kadar potrebujete odgovore, prilagojene določenemu strokovnemu nivoju ali perspektivi.

### Predloge pozivov

Ustvarite ponovno uporabne pozive z naslovniki za spremenljivke. Namesto, da vedno znova pišete nov poziv, definirajte predlogo enkrat in vstavite različne vrednosti. Razred `PromptTemplate` v LangChain4j to olajša z uporabo sintakse `{{spremenljivka}}`.

<img src="../../../translated_images/sl/prompt-templates.14bfc37d45f1a933.webp" alt="Predloge pozivov" width="800"/>

*Ponovno uporabni pozivi s spremenljivkami – ena predloga, več uporab*

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

**Kdaj uporabiti:** Ponavljajoče se poizvedbe z različnimi vnosi, obdelava v serijah, ustvarjanje ponovno uporabnih AI tokov dela ali kjerkoli, kjer struktura poziva ostane enaka, podatki pa se spreminjajo.

---

Ti pet osnovnih tehnik vam daje trden komplet orodij za večino pozivalnih nalog. Preostali del modula nadgradi to z **osmimi naprednimi vzorci**, ki izkoriščajo nadzor razmišljanja GPT-5.2, samoocenjevanje in zmožnosti strukturiranega izhoda.

## Napredni vzorci

Ko smo pokrili osnove, pojdimo na osmice naprednih vzorcev, ki ta modul naredijo edinstven. Ne potrebujejo vsi problemi istega pristopa. Nekatera vprašanja zahtevajo hitre odgovore, druga globoko razmišljanje. Nekatera potrebujejo vidno razmišljanje, druga samo rezultate. Vsak spodaj prikazan vzorec je optimiziran za drugačen scenarij — in nadzor razmišljanja GPT-5.2 še poudari razlike.

<img src="../../../translated_images/sl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osem vzorcev za pozivanje" width="800"/>

*Pregled osmih vzorcev inženiringa pozivov in njihovih primerov uporabe*

<img src="../../../translated_images/sl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Nadzor razmišljanja z GPT-5.2" width="800"/>

*Nadzor razmišljanja GPT-5.2 omogoča specificirati, koliko naj model razmišlja — od hitrih neposrednih odgovorov do globokega raziskovanja*

<img src="../../../translated_images/sl/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Primerjava truda razmišljanja" width="800"/>

*Nizka vnema (hitro, neposredno) proti visoki vnemi (temeljito, raziskovalno) pristopi k razmišljanju*

**Nizka vnema (hitro in osredotočeno)** - Za preprosta vprašanja, kjer želite hitre, neposredne odgovore. Model opravi minimalno razmišljanje - največ 2 koraka. Uporabite za izračune, iskanja ali preprosta vprašanja.

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

> 💡 **Raziskujte s GitHub Copilot:** Odprite [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) in vprašajte:
> - "Kakšna je razlika med nizko in visoko vnemo pri vzorcih pozivanja?"
> - "Kako XML oznake v pozivih pomagajo strukturirati odgovor AI?"
> - "Kdaj naj uporabim vzorce samorefleksije in kdaj neposredna navodila?"

**Visoka vnema (globoko in temeljito)** - Za kompleksne probleme, kjer želite poglobljeno analizo. Model temeljito raziskuje in pokaže podrobno razmišljanje. Uporabite za načrtovanje sistemov, arhitekturne odločitve ali zahtevne raziskave.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Izvajanje nalog (napredek korak za korakom)** - Za večkorakne poteke. Model najprej predstavi načrt, nato poroča o vsakem koraku med izvajanjem in na koncu poda povzetek. Uporabite za migracije, izvedbe ali katerikoli večkorakni proces.

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

Pozivanje z verigo misli eksplicitno zahteva od modela, da pokaže proces razmišljanja, kar poveča natančnost pri kompleksnih nalogah. Razčlenjevanje korak za korakom pomaga tako ljudem kot AI razumeti logiko.

> **🤖 Poskusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Vprašajte o tem vzorcu:
> - "Kako bi prilagodil vzorec izvajanja nalog za dolgotrajne operacije?"
> - "Kakšne so najboljše prakse za strukturiranje uvodov orodij v produkcijskih aplikacijah?"
> - "Kako lahko ujamem in prikažem vmesne posodobitve napredka v uporabniškem vmesniku?"

<img src="../../../translated_images/sl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Vzorec izvajanja nalog" width="800"/>

*Načrt → Izvedba → Povzetek potek za večkorake naloge*

**Samoreflektirajoča koda** - Za generiranje kode kakovosti primerni za produkcijo. Model generira kodo po produkcijskih standardih z ustreznim ravnanjem z napakami. Uporabite, ko gradite nove funkcionalnosti ali storitve.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cikel samorefleksije" width="800"/>

*Iterativni zanki izboljšav – generiraj, ocenjuj, identificiraj težave, izboljšaj, ponovi*

**Strukturirana analiza** - Za dosledno ocenjevanje. Model pregleduje kodo s fiksnim okvirjem (pravilnost, prakse, zmogljivost, varnost, vzdržljivost). Uporabite za preglede kode ali ocene kakovosti.

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
> - "Kako prilagodim analizni okvir za različne vrste pregledov kode?"
> - "Kako najbolj učinkovito programatično razčlenim in ukrepam na strukturiran izhod?"
> - "Kako zagotovim doslednost stopenj resnosti skozi različne preglede?"

<img src="../../../translated_images/sl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Vzorec strukturirane analize" width="800"/>

*Okvir za dosledne preglede kode s stopnjami resnosti*

**Večkratni pogovor** - Za pogovore, ki potrebujejo kontekst. Model si zapomni prejšnja sporočila in gradi naprej na njih. Uporabite za interaktivne pomoči ali kompleksne Q&A.

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

*Kako se kontekst pogovora kopiči skozi več krogov do dosega omejitve tokenov*

**Korak-za-korakom razmišljanje** - Za probleme, ki zahtevajo vidno logiko. Model pokaže izrecno razmišljanje za vsak korak. Uporabite za matematične probleme, logične uganke ali kadar želite razumeti miselni proces.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sl/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Vzorec korak-za-korakom" width="800"/>

*Razčlenjevanje problemov na izrecne logične korake*

**Omejen izhod** - Za odgovore s specifičnimi zahtevami glede formata. Model striktno sledi pravilom formata in dolžine. Uporabite za povzetke ali kadar potrebujete natančno strukturo izhoda.

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

*Uveljavljanje specifičnih zahtev za format, dolžino in strukturo*

## Uporaba obstoječih virov Azure

**Preverite namestitev:**

Prepričajte se, da je `.env` datoteka v korenski mapi z Azure poverilnicami (ustvarjena med Modulom 01):
```bash
cat ../.env  # Prikazati bi moral AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Zaženite aplikacijo:**

> **Opomba:** Če ste aplikacije že zagnali z `./start-all.sh` iz Modula 01, ta modul že deluje na portu 8083. Ukaze za zagon lahko preskočite in greste neposredno na http://localhost:8083.

**Možnost 1: Uporaba Spring Boot nadzorne plošče (priporočeno za uporabnike VS Code)**

Razvojno okolje vsebuje razširitev Spring Boot Dashboard, ki ponuja vizualni vmesnik za upravljanje vseh Spring Boot aplikacij. Najdete jo v vrstici aktivnosti na levi strani VS Code (poiščite ikono Spring Boot).
Iz nadzorne plošče Spring Boot lahko:
- Vidite vse razpoložljive Spring Boot aplikacije v delovnem prostoru
- Zaženete/ustavite aplikacije z enim klikom
- V realnem času ogledate dnevnike aplikacij
- Spremljate stanje aplikacije

Preprosto kliknite gumb za predvajanje poleg "prompt-engineering", da zaženete ta modul, ali zaženite vse module hkrati.

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

Obe skripti samodejno naložita okoljske spremenljivke iz korenske datoteke `.env` in bosta sestavili JAR-je, če ti ne obstajajo.

> **Opomba:** Če raje ročno sestavite vse module, preden jih zaženete:
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

*Glavna nadzorna plošča, ki prikazuje vseh 8 vzorcev prompt inženiringa z njihovimi značilnostmi in primeri uporabe*

## Raziščite vzorce

Spletni vmesnik vam omogoča eksperimentiranje z različnimi strategijami prompotovanja. Vsak vzorec rešuje različne probleme – preizkusite jih, da boste videli, kdaj kateri pristop zasije.

### Nizka proti visoki vnemi

Vprašajte preprosto vprašanje, kot je "Koliko je 15 % od 200?" z nizko vnemo. Dobili boste takojšen, neposreden odgovor. Zdaj vprašajte nekaj zapletenega, kot je "Oblikuj strategijo predpomnjenja za API z visokim prometom" z visoko vnemo. Opazujte, kako se model upočasni in poda podrobno obrazložitev. Isti model, ista struktura vprašanja – a prompt mu pove, koliko naj razmišlja.

<img src="../../../translated_images/sl/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Hiter izračun z minimalno obrazložitvijo*

<img src="../../../translated_images/sl/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Celovita strategija predpomnjenja (2.8MB)*

### Izvedba naloge (Uvodne besede orodij)

Večstopenjski delovni procesi koristijo načrtovanje vnaprej in pripovedovanje poteka. Model predstavi, kaj bo storil, pripoveduje vsak korak in nato povzame rezultate.

<img src="../../../translated_images/sl/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Ustvarjanje REST endpointa z opisom korak za korakom (3.9MB)*

### Samoreflektirajoča koda

Poskusite "Ustvari storitev za preverjanje e-pošte". Namesto da bi samo generiral kodo in se ustavil, model generira, ocenjuje glede na kriterije kakovosti, prepozna slabosti in izboljšuje. Videli boste, kako ponavlja, dokler koda ne doseže produkcijskih standardov.

<img src="../../../translated_images/sl/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Popolna storitev za preverjanje e-pošte (5.2MB)*

### Strukturirana analiza

Pregledi kode potrebujejo dosledne okvirje za ocenjevanje. Model analizira kodo z uporabo določenih kategorij (pravilnost, prakse, zmogljivost, varnost) z različnimi stopnjami resnosti.

<img src="../../../translated_images/sl/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Pregled kode na osnovi okvira*

### Večkratno pogovarjanje

Vprašajte "Kaj je Spring Boot?" in nato takoj dodajte "Pokaži mi primer". Model si zapomni vaše prvo vprašanje in vam posebej poda primer Spring Boot. Brez spomina bi bilo drugo vprašanje preveč nejasno.

<img src="../../../translated_images/sl/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Ohranjanje konteksta med vprašanji*

### Korak-po-korak razlaga

Izberite matematični problem in ga poskusite z razlago korak-po-korak in nizko vnemo. Nizka vnema vam le poda odgovor – hitro, a nejasno. Korak-po-korak pokaže vsak izračun in odločitev.

<img src="../../../translated_images/sl/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matematični problem z izrecnimi koraki*

### Omejen izhod

Ko potrebujete specifične formate ali število besed, ta vzorec zahteva strogo spoštovanje. Poskusite ustvariti povzetek z natanko 100 besedami v obliki označenih seznamov.

<img src="../../../translated_images/sl/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Povzetek strojnega učenja s kontrolo formata*

## Kaj se resnično učite

**Napor razmišljanja spreminja vse**

GPT-5.2 vam omogoča nadzor računalniškega napora preko vaših promptov. Nizek napor pomeni hitre odgovore z minimalnim raziskovanjem. Visok napor pomeni, da si model vzame čas za poglobljeno razmišljanje. Učite se uskladiti napor s kompleksnostjo naloge – ne izgubljajte časa za preprosta vprašanja, a ne hitite pri zapletenih odločitvah.

**Struktura vodi vedenje**

Opazili ste XML oznake v promptih? Niso zgolj dekoracija. Modeli sledijo strukturiranim navodilom bolj zanesljivo kot prostemu besedilu. Ko potrebujete večstopenjske procese ali zapleteno logiko, struktura pomaga modelu slediti, kje je in kaj sledi.

<img src="../../../translated_images/sl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomija dobro strukturiranega prompta z jasnimi odseki in XML-stil organizacijo*

**Kakovost skozi samoocenjevanje**

Samoreflektirajoči vzorci delujejo tako, da naredijo kriterije kakovosti izrecne. Namesto da bi upali, da bo model "storil prav", mu natančno poveste, kaj pomeni "prav": pravilna logika, obravnava napak, zmogljivost, varnost. Model lahko nato oceni svoj izhod in izboljša. To spremeni generiranje kode iz loterije v proces.

**Kontekst je omejen**

Večkratni pogovori delujejo tako, da vključujejo zgodovino sporočil z vsakim zahtevkom. A obstaja meja – vsak model ima največje število tokenov. Ko pogovori rastejo, boste potrebovali strategije, da ohranite relevanten kontekst brez preseganja omejitev. Ta modul vam pokaže, kako spomin deluje; kasneje se boste naučili, kdaj povzeti, kdaj pozabiti in kdaj pridobiti.

## Naslednji koraki

**Naslednji modul:** [03-rag - RAG (izboljšano generiranje z iskanjem)](../03-rag/README.md)

---

**Navigacija:** [← Prejšnji: Modul 01 - Uvod](../01-introduction/README.md) | [Nazaj na glavno](../README.md) | [Naslednji: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo AI prevajalske storitve [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, upoštevajte, da lahko samodejni prevodi vsebujejo napake ali netočnosti. Izvirni dokument v njegovem izvirnem jeziku se šteje za avtoritativni vir. Za kritične informacije priporočamo profesionalni človeški prevod. Nismo odgovorni za kakršne koli nesporazume ali napačne razlage, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->