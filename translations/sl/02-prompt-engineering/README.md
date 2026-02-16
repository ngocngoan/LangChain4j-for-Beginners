# Modul 02: Inženiring pozivov z GPT-5.2

## Kazalo vsebine

- [Kaj se boste naučili](../../../02-prompt-engineering)
- [Predpogoji](../../../02-prompt-engineering)
- [Razumevanje inženiringa pozivov](../../../02-prompt-engineering)
- [Osnove inženiringa pozivov](../../../02-prompt-engineering)
  - [Zero-Shot pozivanje](../../../02-prompt-engineering)
  - [Few-Shot pozivanje](../../../02-prompt-engineering)
  - [Veriga razmišljanja](../../../02-prompt-engineering)
  - [Pozivanje na podlagi vlog](../../../02-prompt-engineering)
  - [Predloge pozivov](../../../02-prompt-engineering)
- [Napredni vzorci](../../../02-prompt-engineering)
- [Uporaba obstoječih Azure virov](../../../02-prompt-engineering)
- [Posnetki zaslona aplikacije](../../../02-prompt-engineering)
- [Raziskovanje vzorcev](../../../02-prompt-engineering)
  - [Nizka proti visoki vnemi](../../../02-prompt-engineering)
  - [Izvajanje naloge (uvodni deli orodij)](../../../02-prompt-engineering)
  - [Samo-reflektirajoča koda](../../../02-prompt-engineering)
  - [Strukturirana analiza](../../../02-prompt-engineering)
  - [Večkrožni pogovor](../../../02-prompt-engineering)
  - [Razmišljanje korak za korakom](../../../02-prompt-engineering)
  - [Omejena izhodna vsebina](../../../02-prompt-engineering)
- [Kaj resnično se učite](../../../02-prompt-engineering)
- [Naslednji koraki](../../../02-prompt-engineering)

## Kaj se boste naučili

<img src="../../../translated_images/sl/what-youll-learn.c68269ac048503b2.webp" alt="Kaj se boste naučili" width="800"/>

V prejšnjem modulu ste videli, kako spomin omogoča pogovorno umetno inteligenco in uporabili GitHub modele za osnovne interakcije. Zdaj se bomo osredotočili na to, kako postavljate vprašanja — same pozive — z uporabo Azure OpenAI GPT-5.2. Način, kako strukturirate svoje pozive, močno vpliva na kakovost odgovorov, ki jih prejmete. Začnemo s pregledom osnovnih tehnik pozivanja, nato pa preidemo na osem naprednih vzorcev, ki v celoti izkoriščajo zmogljivosti GPT-5.2.

Uporabljali bomo GPT-5.2, ker uvaja nadzor razmišljanja - modelu lahko določite, koliko razmišljanja naj opravi pred odgovorom. To naredi različne strategije pozivanja bolj očitne in vam pomaga razumeti, kdaj uporabiti vsak pristop. Prav tako bomo imeli koristi od manjših omejitev hitrosti Azure za GPT-5.2 v primerjavi z GitHub modeli.

## Predpogoji

- Dokončan Modul 01 (vzpostavljeni Azure OpenAI viri)
- Datoteka `.env` v korenski mapi z Azure poverilnicami (ustvarjena z `azd up` v Modulu 01)

> **Opomba:** Če Modul 01 še niste dokončali, sledite navodilom za namestitev najprej tam.

## Razumevanje inženiringa pozivov

<img src="../../../translated_images/sl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Kaj je inženiring pozivov?" width="800"/>

Inženiring pozivov pomeni oblikovanje vhodnega besedila, ki vam dosledno priskrbi želene rezultate. Ne gre samo za postavljanje vprašanj - gre za strukturiranje zahtev, tako da model natančno razume, kaj želite in kako to dostaviti.

Pomislite na to, kot da dajete navodila sodelavcu. "Popravi napako" je nejasno. "Popravi izjemo ničelnega kazalca v datoteki UserService.java, vrstica 45, z dodatno preveritvijo ničelne vrednosti" je specifično. Jezikovni modeli delujejo enako - specifičnost in struktura sta pomembni.

<img src="../../../translated_images/sl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kako se LangChain4j vklopi" width="800"/>

LangChain4j zagotavlja infrastrukturo — povezave z modeli, spomin in vrste sporočil — medtem ko so vzorci pozivov zgolj skrbno strukturirano besedilo, ki ga pošiljate preko te infrastrukture. Ključni gradniki so `SystemMessage` (ki določa vedenje AI in vlogo) ter `UserMessage` (ki vsebuje vašo dejansko zahtevo).

## Osnove inženiringa pozivov

<img src="../../../translated_images/sl/five-patterns-overview.160f35045ffd2a94.webp" alt="Pregled petih vzorcev inženiringa pozivov" width="800"/>

Preden se potopimo v napredne vzorce v tem modulu, si oglejmo pet temeljnih tehnik pozivanja. To so gradniki, ki jih mora poznati vsak inženir pozivov. Če ste že delali skozi [modul hitrega zagona](../00-quick-start/README.md#2-prompt-patterns), ste jih že videli v praksi — tukaj je konceptualni okvir zanje.

### Zero-Shot pozivanje

Najpreprostejši pristop: modelu dajte neposredno navodilo brez primerov. Model se popolnoma zanaša na svoje učenje, da razume in izvede nalogo. To deluje dobro za enostavne zahteve, kjer je pričakovano vedenje očitno.

<img src="../../../translated_images/sl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot pozivanje" width="800"/>

*Neposredno navodilo brez primerov — model iz navodila sam zaključi nalogo*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odgovor: "Pozitivno"
```

**Kdaj uporabiti:** preproste klasifikacije, neposredna vprašanja, prevodi ali katera koli naloga, ki jo model lahko obvlada brez dodatnih navodil.

### Few-Shot pozivanje

Dajte primere, ki kažejo vzorec, ki ga želite, da ga model sledi. Model se iz vaših primerov nauči pričakovani vhodno-izhodni format in ga uporabi za nove vhode. To močno izboljša doslednost pri nalogah, kjer želena oblika ali vedenje ni očitno.

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

**Kdaj uporabiti:** prilagojene klasifikacije, dosledno oblikovanje, področju specifične naloge ali kadar so zero-shot rezultati nedosledni.

### Veriga razmišljanja

Prosite model, naj pokaže svoje razmišljanje korak za korakom. Namesto da takoj poda odgovor, model razdeli problem in predstavi vsak del posebej. To izboljša natančnost pri matematičnih, logičnih in večstopenjskih razmišljanjih.

<img src="../../../translated_images/sl/chain-of-thought.5cff6630e2657e2a.webp" alt="Pozivanje z verigo razmišljanja" width="800"/>

*Razmišljanje korak za korakom — razbijanje zapletenih problemov na jasne logične korake*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model prikazuje: 15 - 8 = 7, nato 7 + 12 = 19 jabolk
```

**Kdaj uporabiti:** matematični problemi, logične uganke, odpravljanje napak ali katera koli naloga, kjer prikazovanje procesa razmišljanja izboljša natančnost in zaupanje.

### Pozivanje na podlagi vlog

Določite osebnost ali vlogo AI, preden postavite vprašanje. To zagotavlja kontekst, ki oblikuje ton, globino in osredotočenost odgovora. "Programsko-arhitekt" daje drugačne nasvete kot "mlajši razvijalec" ali "varnostni revizor".

<img src="../../../translated_images/sl/role-based-prompting.a806e1a73de6e3a4.webp" alt="Pozivanje na podlagi vlog" width="800"/>

*Nastavitev konteksta in osebnosti — enako vprašanje dobi različen odgovor glede na dodeljeno vlogo*

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

**Kdaj uporabiti:** pregledi kode, mentorstvo, področju specifične analize ali kadar potrebujete odgovore prilagojene določeni strokovni ravni ali perspektivi.

### Predloge pozivov

Ustvarite znova uporabne pozive z zamenljivimi bližnjicami. Namesto da vsakič pišete nov poziv, enkrat definirajte predlogo in nato vnesete različne vrednosti. Razred `PromptTemplate` v LangChain4j to enostavno omogoča s sintakso `{{variable}}`.

<img src="../../../translated_images/sl/prompt-templates.14bfc37d45f1a933.webp" alt="Predloge pozivov" width="800"/>

*Znova uporabni pozivi z zamenljivimi spremenljivkami — ena predloga, mnogo uporab*

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

**Kdaj uporabiti:** ponavljajoči se povpraševanja z različnimi vhodi, procesiranje paketov, izdelava znova uporabnih delovnih tokov AI ali katera koli situacija, kjer struktura poziva ostaja enaka, podatki pa se spreminjajo.

---

Ti pet temeljni pristopi vam dajejo trden nabor orodij za večino pozivnih nalog. Preostanek tega modula gradi na njih z **osmimi naprednimi vzorci**, ki izkoriščajo nadzor razmišljanja GPT-5.2, samooceno in zmožnosti strukturiranega izhoda.

## Napredni vzorci

Ko smo obravnavali osnove, pojdimo na osem naprednih vzorcev, ki ta modul naredijo poseben. Ne vsi problemi zahtevajo enak pristop. Nekatera vprašanja zahtevajo hitre odgovore, druga globoko razmišljanje. Nekatera potrebujejo vidno razmišljanje, druga samo rezultate. Vsak spodnji vzorec je optimiziran za drugačen scenarij — nadzor razmišljanja GPT-5.2 pa te razlike še bolj poudari.

<img src="../../../translated_images/sl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osem vzorcev pozivanja" width="800"/>

*Pregled osmih vzorcev inženiringa pozivov in njihovih primerov uporabe*

<img src="../../../translated_images/sl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Nadzor razmišljanja z GPT-5.2" width="800"/>

*Nadzor razmišljanja GPT-5.2 omogoča, da določite, koliko razmišljanja naj model opravi — od hitrih neposrednih odgovorov do globoke razprave*

<img src="../../../translated_images/sl/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Primerjava napora razmišljanja" width="800"/>

*Nizka vnema (hitro, neposredno) proti visoka vnema (temeljito, raziskovalno) pristopi k razmišljanju*

**Nizka vnema (Hitra in osredotočena)** - za enostavna vprašanja, kjer želite hitre, neposredne odgovore. Model izvaja minimalno razmišljanje - največ dva koraka. Uporabite to za izračune, iskanja ali preprosta vprašanja.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Raziskujte z GitHub Copilot:** Odprite [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) in vprašajte:
> - "Kakšna je razlika med nizko in visoko vnemo pri vzorcih pozivanja?"
> - "Kako XML oznake v pozivih pomagajo strukturirati odgovor AI?"
> - "Kdaj naj uporabim vzorce samo-refleksije in kdaj neposredna navodila?"

**Visoka vnema (Globoko in temeljito)** - za kompleksne probleme, kjer želite obsežno analizo. Model temeljito raziskuje in pokaže podrobno razmišljanje. Uporabite to za sistemsko načrtovanje, arhitekturne odločitve ali kompleksno raziskovanje.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Izvajanje naloge (napredek korak za korakom)** - za večkrokovne delovne procese. Model ponudi načrt vnaprej, pripoveduje o vsakem koraku, ko ga izvaja, in nato poda povzetek. Uporabite to za migracije, implementacije ali kateri koli večkrokovni postopek.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Pozivanje z verigo razmišljanja izrecno zahteva, da model prikaže svoj razmišljalni proces, kar izboljša natančnost pri kompleksnih nalogah. Razčlenitev korak za korakom pomaga tako ljudem kot AI razumeti logiko.

> **🤖 Poskusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Povprašajte o tem vzorcu:
> - "Kako bi prilagodil vzorec izvajanja naloge za dolge procese?"
> - "Kakšne so najboljše prakse za strukturiranje uvodnih delov orodij v produkcijskih aplikacijah?"
> - "Kako lahko zajamemo in prikažemo vmesne posodobitve napredka v uporabniškem vmesniku?"

<img src="../../../translated_images/sl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Vzorac izvajanja naloge" width="800"/>

*Načrtuj → Izvedi → Povzemi delovni tok za večstopenjske naloge*

**Samo-reflektirajoča koda** - za generiranje kode kakovosti produkcije. Model generira kodo, jo preveri glede na kriterije kakovosti in jo iterativno izboljšuje. Uporabite to pri gradnji novih funkcij ali storitev.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cikel samo-refleksije" width="800"/>

*Iterativni izboljševalni cikel - generiraj, ocenjuj, identificiraj težave, izboljšaj, ponovi*

**Strukturirana analiza** - za dosledno ocenjevanje. Model pregleda kodo z uporabo fiksnega okvira (popolnost, prakse, zmogljivost, varnost). Uporabite to za preglede kode ali ocene kakovosti.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Poskusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Povprašajte o strukturirani analizi:
> - "Kako lahko prilagodim analitični okvir za različne vrste pregledov kode?"
> - "Kakšen je najboljši način za programatično razčlenjevanje in ukrepanje glede strukturiranega izhoda?"
> - "Kako zagotoviti dosledne ravni resnosti med različnimi pregledi?"

<img src="../../../translated_images/sl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Vzorac strukturirane analize" width="800"/>

*Okvir s štirimi kategorijami za dosledne preglede kode z vrednostmi resnosti*

**Večkrožni pogovor** - za pogovore, ki potrebujejo kontekst. Model si zapomni prejšnja sporočila in gradi na njih. Uporabite to za interaktivno pomoč ali kompleksna vprašanja in odgovore.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/sl/context-memory.dff30ad9fa78832a.webp" alt="Spomin za kontekst" width="800"/>

*Kako kontekst pogovora narašča skozi več krogov dokler ni dosežen omejitev žetonov*

**Razmišljanje korak za korakom** - za probleme, ki zahtevajo vidno logiko. Model pokaže izrecno razmišljanje za vsak korak. Uporabite to za matematične probleme, logične uganke ali kadar želite razumeti proces razmišljanja.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sl/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Vzorac korak za korakom" width="800"/>

*Razbijanje problemov na jasne logične korake*

**Omejena izhodna vsebina** - za odgovore s specifičnimi zahtevami glede formata. Model strogo sledi pravilom formata in dolžine. Uporabite to za povzetke ali kadar potrebujete natančno strukturo izhoda.

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

<img src="../../../translated_images/sl/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Vzorac omejene izhodne vsebine" width="800"/>

*Uveljavljanje specifičnih zahtev za format, dolžino in strukturo*

## Uporaba obstoječih Azure virov

**Preverite namestitev:**

Prepričajte se, da datoteka `.env` obstaja v korenski mapi z Azure poverilnicami (ustvarjena med Modulom 01):
```bash
cat ../.env  # Prikaže naj AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Zaženite aplikacijo:**

> **Opomba:** Če ste že zagnali vse aplikacije z `./start-all.sh` iz Modula 01, ta modul že teče na vratih 8083. Ukaze za zagon spodaj lahko preskočite in neposredno odprete http://localhost:8083.

**Možnost 1: Uporaba Spring Boot nadzorne plošče (priporočeno za uporabnike VS Code)**

Razvojni kontejner vključuje razširitev Spring Boot nadzorne plošče, ki vam omogoča vizualno upravljanje vseh Spring Boot aplikacij. Najdete jo v vrstici dejavnosti na levi strani VS Code (poiščite ikono Spring Boot).
Iz nadzorne plošče Spring Boot lahko:
- Vidite vse razpoložljive Spring Boot aplikacije v delovnem prostoru
- Zaženete/ustavite aplikacije z enim klikom
- V realnem času spremljate dnevnike aplikacij
- Spremljate stanje aplikacij

Preprosto kliknite gumb za predvajanje poleg "prompt-engineering" za zagon tega modula ali zaženite vse module naenkrat.

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

Obe skripti samodejno naložita okoljske spremenljivke iz korenske `.env` datoteke in bosta sestavili JAR-je, če ti še ne obstajajo.

> **Opomba:** Če raje vse module ročno sestavite pred zagonom:
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

Odprite http://localhost:8083 v brskalniku.

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

## Posnetki zaslona aplikacij

<img src="../../../translated_images/sl/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Glavna nadzorna plošča prikazuje vseh 8 vzorcev za oblikovanje ukazov z njihovimi značilnostmi in primeri uporabe*

## Raziščite vzorce

Spletni vmesnik vam omogoča eksperimentiranje z različnimi strategijami oblikovanja ukazov. Vsak vzorec rešuje različne probleme – preizkusite jih, da vidite, kdaj kateri pristop najbolj ustreza.

### Nizka proti visoki zavzetosti

Postavite preprosto vprašanje, kot je "Koliko je 15 % od 200?" z uporabo Nizke zavzetosti. Dobite takojšen, neposreden odgovor. Zdaj vprašajte nekaj zapletenega, na primer "Zasnovati strategijo predpomnjenja za visoko prometni API" z uporabo Visoke zavzetosti. Opazujte, kako model upočasni in poda podrobno razlago. Enak model, ista struktura vprašanja – vendar mu ukaz pove, koliko razmišljanja naj vloži.

<img src="../../../translated_images/sl/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Hitro računanje z minimalnim razmišljanjem*

<img src="../../../translated_images/sl/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Celovita strategija predpomnjenja (2.8MB)*

### Izvajanje nalog (uvodni deli orodij)

Večstopenjski delovni tokovi imajo koristi od načrtovanja vnaprej in pripovedovanja poteka. Model predstavi, kaj bo naredil, pripoveduje o vsakem koraku, nato pa povzema rezultate.

<img src="../../../translated_images/sl/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Ustvarjanje REST končne točke s pripovedovanjem po korakih (3.9MB)*

### Samoreflektirajoča koda

Poskusite "Ustvari storitev za preverjanje veljavnosti e-pošte". Namesto da bi samo generiral kodo in se ustavil, model generira, oceni glede na kriterije kakovosti, prepozna slabosti in izboljšuje. Videli boste, kako iterira, dokler koda ne doseže proizvodnih standardov.

<img src="../../../translated_images/sl/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Popolna storitev preverjanja veljavnosti e-pošte (5.2MB)*

### Strukturirana analiza

Pregledi kode potrebujejo dosledne ocenjevalne okvire. Model analizira kodo z uporabo fiksnih kategorij (pravilenost, prakse, zmogljivost, varnost) s stopnjami resnosti.

<img src="../../../translated_images/sl/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Pregled kode na osnovi okvira*

### Večkratni pogovor

Vprašajte "Kaj je Spring Boot?" in takoj nadaljujte z "Pokaži mi primer". Model si zapomni vaše prvo vprašanje in vam posebej poda primer Spring Boot. Brez spomina bi bilo drugo vprašanje preveč nejasno.

<img src="../../../translated_images/sl/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Ohranjanje konteksta med vprašanji*

### Razmišljanje korak za korakom

Izberite matematično nalogo in jo poskusite z obema, Razmišljanjem korak za korakom in Nizko zavzetostjo. Nizka zavzetost vam le poda odgovor – hitro, a netransparentno. Razmišljanje korak za korakom vam pokaže vsak izračun in odločitev.

<img src="../../../translated_images/sl/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matematična naloga z eksplicitnimi koraki*

### Omejen izhod

Ko potrebujete posebne formate ali število besed, ta vzorec zahteva strogo upoštevanje. Poskusite ustvariti povzetek z natanko 100 besedami v obliki točk.

<img src="../../../translated_images/sl/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Povzetek strojnega učenja s kontrolirano obliko*

## Kaj se resnično učite

**Razmišljanje spremeni vse**

GPT-5.2 vam dovoljuje nadzor nad računalniškim naporom prek vaših ukazov. Nizek napor pomeni hitre odgovore z minimalnim raziskovanjem. Visok napor pomeni, da si model vzame čas za globoko razmišljanje. Učite se uskladiti napor z zahtevnostjo naloge – ne zapravljajte časa pri preprostih vprašanjih, vendar tudi ne hitite pri zapletenih odločitvah.

**Struktura vodi vedenje**

Opazite XML oznake v ukazih? Niso dekorativne. Modeli bolj zanesljivo sledijo strukturiranim navodilom kot prostemu besedilu. Ko potrebujete večstopenjske procese ali kompleksno logiko, struktura pomaga modelu slediti, kje je in kaj sledi.

<img src="../../../translated_images/sl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomija dobro strukturiranega ukaza z jasnimi deli in XML-stilom organizacije*

**Kakovost skozi samoocenjevanje**

Vzorce samoreflektiranja delujejo tako, da so kriteriji kakovosti eksplicitni. Namesto da bi upali, da model "naredi prav", mu točno poveste, kaj pomeni "prav": pravilna logika, obravnava napak, zmogljivost, varnost. Model lahko potem oceni svoj izhod in se izboljša. To spremeni generiranje kode iz loterije v proces.

**Kontekst je končen**

Večkratni pogovori delujejo tako, da v vsak zahtevek vključijo zgodovino sporočil. Toda obstaja meja – vsak model ima največje število tokenov. Ko pogovori rastejo, boste potrebovali strategije za ohranjanje pomembnega konteksta, ne da bi dosegli omejitev. Ta modul vam pokaže, kako deluje spomin; kasneje boste spoznali, kdaj povzeti, kdaj pozabiti in kdaj pridobiti.

## Naslednji koraki

**Naslednji modul:** [03-rag - RAG (generiranje s podporo iskanja)](../03-rag/README.md)

---

**Navigacija:** [← Prejšnji: Modul 01 - Uvod](../01-introduction/README.md) | [Nazaj na glavno](../README.md) | [Naslednji: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo AI prevajalske storitve [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas prosimo, da upoštevate, da samodejni prevodi lahko vsebujejo napake ali netočnosti. Izvirni dokument v njegovem izvorni jezik velja za avtoritativni vir. Za pomembne informacije priporočamo strokovni človeški prevod. Ne prevzemamo odgovornosti za morebitna nesporazume ali napačne razlage, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->