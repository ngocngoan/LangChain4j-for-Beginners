# Modulis 02: Užklausų Inžinerija su GPT-5.2

## Turinys

- [Ko Išmoksite](../../../02-prompt-engineering)
- [Išankstiniai Reikalavimai](../../../02-prompt-engineering)
- [Užklausų Inžinerijos Supratimas](../../../02-prompt-engineering)
- [Užklausų Inžinerijos Pagrindai](../../../02-prompt-engineering)
  - [Zero-Shot Užklausos](../../../02-prompt-engineering)
  - [Few-Shot Užklausos](../../../02-prompt-engineering)
  - [Mąstymo Grandinė](../../../02-prompt-engineering)
  - [Rolės Pagrindu Užklausos](../../../02-prompt-engineering)
  - [Užklausų Šablonai](../../../02-prompt-engineering)
- [Išplėstiniai Modeliai](../../../02-prompt-engineering)
- [Esamų Azure Išteklių Naudojimas](../../../02-prompt-engineering)
- [Programos Ekrano Kopijos](../../../02-prompt-engineering)
- [Modelių Tyrinėjimas](../../../02-prompt-engineering)
  - [Mažas prieš Didelį Užsidegimą](../../../02-prompt-engineering)
  - [Užduočių Vykdymas (Įrankių Įžangos)](../../../02-prompt-engineering)
  - [Savirefleksinis Kodas](../../../02-prompt-engineering)
  - [Struktūruota Analizė](../../../02-prompt-engineering)
  - [Daugkartinis Pokalbis](../../../02-prompt-engineering)
  - [Žingsnis po Žingsnio Mąstymas](../../../02-prompt-engineering)
  - [Apribotas Išvestis](../../../02-prompt-engineering)
- [Ką Iš Tikrųjų Mokotės](../../../02-prompt-engineering)
- [Kiti Žingsniai](../../../02-prompt-engineering)

## Ko Išmoksite

<img src="../../../translated_images/lt/what-youll-learn.c68269ac048503b2.webp" alt="Ko Išmoksite" width="800"/>

Antrajame modulyje matėte, kaip atmintis leidžia pokalbių AI veikti ir kaip naudoti GitHub modelius pagrindinėms sąveikoms. Dabar sutelksime dėmesį, kaip užduoti klausimus — pačius užklausimus — naudojant Azure OpenAI GPT-5.2. Užklausimų struktūra labai lemia gaunamų atsakymų kokybę. Pradėsime nuo pagrindinių užklausimų technikų apžvalgos, tada pereisime prie aštuonių išplėstinių modelių, kurie išnaudoja visą GPT-5.2 galimybių spektrą.

Naudosime GPT-5.2, nes jis įveda mąstymo valdymą – galite nurodyti modeliui, kiek labai galvoti prieš atsakant. Tai leidžia geriau suprasti, kada naudoti kurią strategiją. Taip pat pasinaudosime tuo, kad Azure GPT-5.2 turi mažiau apribojimų nei GitHub modeliai.

## Išankstiniai Reikalavimai

- Baigtas Modulis 01 (Azure OpenAI ištekliai įdiegti)
- `.env` failas pagrindiniame kataloge su Azure kredencialais (sukurtas vykdant `azd up` modulyje 01)

> **Pastaba:** Jei neužbaigėte Modulio 01, pirmiausia atlikite ten nurodytus diegimo veiksmus.

## Užklausų Inžinerijos Supratimas

<img src="../../../translated_images/lt/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Kas yra Užklausų Inžinerija?" width="800"/>

Užklausų inžinerija yra apie tokio įvesties teksto kūrimą, kuris nuosekliai suteikia reikiamus rezultatus. Tai ne tik klausimų uždavimas – tai užklausų struktūrizavimas taip, kad modelis tiksliai suprastų, ko norite ir kaip tai pateikti.

Galvokite apie tai kaip nurodymų kolegai davimą. „Ištaisyk klaidą“ yra neaišku. „Pataisyk null pointer exception klaidą UserService.java faile 45 eilutėje pridėdamas null tikrinimą“ yra konkretu. Kalbos modeliai veikia taip pat – svarbu specifika ir struktūra.

<img src="../../../translated_images/lt/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kaip LangChain4j Prisitaiko" width="800"/>

LangChain4j teikia infrastruktūrą — modelių jungtis, atmintį ir žinučių tipus — o užklausų šablonai yra tik kruopščiai struktūruotas tekstas, kurį siunčiate per šią infrastruktūrą. Pagrindiniai blokai yra `SystemMessage` (nustato DI elgseną ir vaidmenį) bei `UserMessage` (perneša jūsų faktinį užklausimą).

## Užklausų Inžinerijos Pagrindai

<img src="../../../translated_images/lt/five-patterns-overview.160f35045ffd2a94.webp" alt="Penki Užklausų Inžinerijos Modeliai" width="800"/>

Prieš gilindamiesi į šio modulio išplėstinius modelius, peržiūrėkime penkias pagrindines užklausų technikas. Tai pagrindiniai blokai, kuriuos turi žinoti kiekvienas užklausų inžinierius. Jei jau dirbote su [Greito Pradžios moduliu](../00-quick-start/README.md#2-prompt-patterns), matėte juos veikime — čia pateikiama jų konceptualinė sistema.

### Zero-Shot Užklausos

Paprastas būdas: duokite modeliui tiesioginę instrukciją be pavyzdžių. Modelis visiškai remiasi savo mokymu, kad suprastų ir atliktų užduotį. Tai puikiai tinka paprastoms užklausoms, kur tikėtina elgsena yra aiški.

<img src="../../../translated_images/lt/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Užklausos" width="800"/>

*Tiesioginė instrukcija be pavyzdžių — modelis iš instrukcijos paties išveda užduotį*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Atsakymas: "Teigiamas"
```

**Kada naudoti:** Paprastoms klasifikacijoms, tiesioginiams klausimams, vertimams ar kitoms užduotims, kurias modelis gali atlikti be papildomų nurodymų.

### Few-Shot Užklausos

Pateikite pavyzdžių, kurie parodo, kokio modelio elgesio tikitės. Modelis mokosi įvesties-išvesties formato pagal jūsų pavyzdžius ir taiko jį naujoms įvestims. Tai gerokai pagerina nuoseklumą užduotyse, kuriose norimas formatas ar elgesys nėra akivaizdus.

<img src="../../../translated_images/lt/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Užklausos" width="800"/>

*Mokymasis iš pavyzdžių — modelis identifikuoja modelį ir taiko naujoms įvestims*

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

**Kada naudoti:** Individualioms klasifikacijoms, nuosekliam formatavimui, specifinėms sritims ar kai zero-shot rezultatai yra nenuoseklūs.

### Mąstymo Grandinė

Prašykite modelio parodyti savo mąstymą žingsnis po žingsnio. Vietoj tiesioginio atsakymo modelis suskaidys problemą ir patars kiekvieną dalį atskirai. Tai pagerina tikslumą matematikos, logikos ir sudėtingų užduočių sprendimuose.

<img src="../../../translated_images/lt/chain-of-thought.5cff6630e2657e2a.webp" alt="Mąstymo Grandinės Užklausos" width="800"/>

*Žingsnis po žingsnio mąstymas — sudėtingų problemų skaidymas į aiškius loginius žingsnius*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Modelis rodo: 15 - 8 = 7, tada 7 + 12 = 19 obuolių
```

**Kada naudoti:** Matematikos uždaviniams, loginėms dėlionėms, derinimui ar bet kuriai užduočiai, kurioje mąstymo proceso atskleidimas gerina tikslumą ir pasitikėjimą.

### Rolės Pagrindu Užklausos

Nustatykite DI asmenybę ar vaidmenį prieš užduodami klausimą. Tai suteikia kontekstą, kuris formuoja atsakymo toną, gilumą ir dėmesį. „Programinės įrangos architektas“ pateikia kitokias rekomendacijas nei „jaunesnysis programuotojas“ ar „saugumo auditorius“.

<img src="../../../translated_images/lt/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rolės Pagrindu Užklausos" width="800"/>

*Konteksto ir asmenybės nustatymas — ta pats klausimas gauna skirtingą atsakymą priklausomai nuo priskirto vaidmens*

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

**Kada naudoti:** Kodo peržiūroms, mokymui, specifinėms sričių analizėms ar kai reikia atsakymų pritaikytų konkrečiam ekspertizės lygiui ar perspektyvai.

### Užklausų Šablonai

Sukurkite pakartotinai naudojamas užklausas su kintamaisiais. Vietoje naujos užklausos kūrimo kiekvieną kartą, apibrėžkite šabloną vieną kartą ir pildykite skirtingas reikšmes. LangChain4j `PromptTemplate` klasė tai palengvina naudodama `{{kintamasis}}` sintaksę.

<img src="../../../translated_images/lt/prompt-templates.14bfc37d45f1a933.webp" alt="Užklausų Šablonai" width="800"/>

*Pakartotinai naudojamos užklausos su kintamaisiais — vienas šablonas, daug panaudojimų*

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

**Kada naudoti:** Kartojamoms užklausoms su skirtingais duomenimis, masiniam apdorojimui, pakartotinai naudojamiems DI darbo srautams arba kai užklausos struktūra išlieka ta pati, bet keičiasi duomenys.

---

Šie penki pagrindai suteikia tvirtą įrankių rinkinį daugumai užklausų užduočių. Likusi šio modulio dalis plečiasi su **aštuoniais išplėstiniais modeliais**, kurie išnaudoja GPT-5.2 mąstymo valdymą, savianalizę ir struktūruotos išvesties galimybes.

## Išplėstiniai Modeliai

Pabaigę pagrindus, pereikime prie aštuonių pažangių modelių, kurie daro šį modulį išskirtinį. Ne visoms problemoms tinka tas pats požiūris. Kai kurios užklausos reikalauja greitų atsakymų, o kitos gilios analizės. Vienos turi rodyti mąstymą, kitos tiesiog pateikti rezultatus. Kiekvienas žemiau esantis modelis optimizuotas skirtingai situacijai — o GPT-5.2 mąstymo valdymas tai dar labiau sustiprina.

<img src="../../../translated_images/lt/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Aštuoni Užklausų Modeliai" width="800"/>

*Septyni užklausų inžinerijos modeliai ir jų naudojimo atvejai*

<img src="../../../translated_images/lt/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Mąstymo Valdymas su GPT-5.2" width="800"/>

*GPT-5.2 mąstymo valdymas leidžia nurodyti, kiek modelis turi mąstyti — nuo greitų tiesioginių atsakymų iki gilaus tyrinėjimo*

<img src="../../../translated_images/lt/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Mąstymo Pastangų Palyginimas" width="800"/>

*Mažas užsidegimas (greitas, tiesioginis) prieš didelį užsidegimą (išsamus, tyrinėjamasis) mąstymo metodai*

**Mažas Užsidegimas (Greita ir Koncentruota)** – Paprastiems klausimams, kai norite greito, tiesioginio atsakymo. Modelis atlieka minimalų mąstymą – daugiausia 2 žingsnius. Naudokite tai skaičiavimams, paieškoms ar paprastiems klausimams.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Ištirkite su GitHub Copilot:** Atidarykite [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ir paklauskite:
> - „Kuo skiriasi mažo ir didelio užsidegimo užklausų modeliai?“
> - „Kaip XML žymos užklausose padeda struktūrizuoti DI atsakymą?“
> - „Kada naudoti savirefleksinius modelius, o kada tiesiogines instrukcijas?“

**Didelis Užsidegimas (Gilūs ir Išsamūs)** – Sudėtingoms problemoms, kai reikalinga išsami analizė. Modelis tyrinėja atidžiai ir rodo detalią logiką. Naudokite sistemų dizainui, architektūros sprendimams ar sudėtingiems tyrimams.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Užduočių Vykdymas (Žingsnis po Žingsnio Pažanga)** – Daugiažingsniams darbo procesams. Modelis pateikia planą iš anksto, seka kiekvieną žingsnį vykdymo metu, tada pateikia santrauką. Naudokite migracijoms, įgyvendinimams ar bet kuriems daug žingsnių reikalaujantiems procesams.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Mąstymo grandinės užklausos prašo modelio parodyti mąstymo procesą, taip gerinant sudėtingų užduočių tikslumą. Žingsnis po žingsnio išskaidymas padeda tiek žmonėms, tiek DI suprasti logiką.

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Pokalbiu:** Paklauskite apie šį modelį:
> - „Kaip pritaikyti užduočių vykdymo modelį ilgalaikėms operacijoms?“
> - „Kokios yra geriausios praktikos įrankių įvadų struktūrizavimui gamybinėse programose?“
> - „Kaip surinkti ir pateikti tarpinio progreso atnaujinimus naudotojo sąsajoje?“

<img src="../../../translated_images/lt/task-execution-pattern.9da3967750ab5c1e.webp" alt="Užduočių Vykdymo Modelis" width="800"/>

*Planavimas → Vykdymas → Santrauka daugiažingsnėms užduotims*

**Savirefleksinis Kodas** – Kuriant gamybos klasės kodą. Modelis generuoja kodą, tikrina jį pagal kokybės kriterijus ir tobulina iteratyviai. Naudokite tai, kai kuriate naujas funkcijas ar paslaugas.

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

<img src="../../../translated_images/lt/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Savianalizės Ciklas" width="800"/>

*Iteratyvus tobulinimo ciklas - generavimas, vertinimas, problemų nustatymas, tobulinimas, kartojimas*

**Struktūruota Analizė** – Nuosekliam vertinimui. Modelis peržiūri kodą naudodamas fiksuotą sistemą (teisingumas, praktikos, našumas, saugumas). Naudokite kodo peržiūroms ar kokybės vertinimams.

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

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Pokalbiu:** Paklauskite apie struktūruotą analizę:
> - „Kaip pritaikyti analizės sistemą skirtingų tipų kodo peržiūroms?“
> - „Koks geriausias būdas programiškai apdoroti struktūruotą išvestį?“
> - „Kaip užtikrinti nuoseklius sunkumo lygius skirtinguose peržiūrų etapuose?“

<img src="../../../translated_images/lt/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Struktūruotos Analizės Modelis" width="800"/>

*Keturių kategorijų sistema nuoseklioms kodo peržiūroms su sunkumo lygiais*

**Daugkartinis Pokalbis** – Pokalbiams, kuriems reikalingas kontekstas. Modelis prisimena ankstesnes žinutes ir jas plėtoja. Naudokite interaktyvioms pagalbos sesijoms ar sudėtingiems klausimų-atsakymų scenarijams.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/lt/context-memory.dff30ad9fa78832a.webp" alt="Konteksto Atmintis" width="800"/>

*Kaip pokalbio kontekstas kaupiasi per kelis žingsnius iki tokenų limito*

**Žingsnis po Žingsnio Mąstymas** – Problemoms, kur reikia matomo loginio aiškumo. Modelis atskleidžia aiškų mąstymą kiekviename žingsnyje. Naudokite matematikos uždaviniams, loginėms dėlionėms ar kai norite suprasti mąstymo procesą.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/lt/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Žingsnis po Žingsnio Modelis" width="800"/>

*Problemos išskaidymas į aiškius loginius žingsnius*

**Apribotas Išvestis** – Atsakymams su specifiškais formatavimo reikalavimais. Modelis griežtai laikosi formatų ir ilgio taisyklių. Naudokite santraukose ar kai reikalinga tiksli išvesties struktūra.

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

<img src="../../../translated_images/lt/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Apriboto Išvesties Modelis" width="800"/>

*Specifiškų formato, ilgio ir struktūros reikalavimų užtikrinimas*

## Esamų Azure Išteklių Naudojimas

**Patikrinkite diegimą:**

Įsitikinkite, kad pagrindiniame kataloge yra `.env` failas su Azure kredencialais (sukurtas Modulyje 01):
```bash
cat ../.env  # Turėtų parodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Paleiskite programą:**

> **Pastaba:** Jei jau paleidote visas programas naudodami `./start-all.sh` iš Modulio 01, šis modulis jau veikia 8083 prievade. Galite praleisti paleidimo komandų vykdymą ir tiesiog atsidaryti http://localhost:8083.

**1 variantas: Naudojant Spring Boot Dashboard (Rekomenduojama VS Code vartotojams)**

Kūrimo konteineryje yra Spring Boot Dashboard plėtinys, kuris suteikia vizualią sąsają valdyti visas Spring Boot programas. Jį rasite veiklų juostoje kairėje VS Code pusėje (ieškokite Spring Boot piktogramos).
Iš Spring Boot valdymo skydelio galite:
- Matyti visas prieinamas Spring Boot programas darbo aplinkoje
- Vienu spustelėjimu įjungti/išjungti programas
- Realaus laiko režimu peržiūrėti programų žurnalus
- Stebėti programų būseną

Tiesiog spustelėkite paleidimo mygtuką šalia „prompt-engineering“ norėdami paleisti šį modulį arba paleiskite visus modulius vienu metu.

<img src="../../../translated_images/lt/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**2 variantas: Naudojant shell scenarijus**

Paleiskite visas internetines programas (modulius 01-04):

**Bash:**
```bash
cd ..  # Iš šaknininio katalogo
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iš šakninių katalogų
.\start-all.ps1
```

Arba paleiskite tik šį modulį:

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

Abu scenarijai automatiškai įkelia aplinkos kintamuosius iš pagrindinio `.env` failo ir, jei reikia, sukuria JAR failus.

> **Pastaba:** Jei norite visus modulius susikurti rankiniu būdu prieš paleidžiant:
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

Atidarykite http://localhost:8083 savo naršyklėje.

**Norėdami sustabdyti:**

**Bash:**
```bash
./stop.sh  # Tik šis modulis
# Arba
cd .. && ./stop-all.sh  # Visi moduliai
```

**PowerShell:**
```powershell
.\stop.ps1  # Tik šis modulis
# Arba
cd ..; .\stop-all.ps1  # Visi moduliai
```

## Programos ekrano nuotraukos

<img src="../../../translated_images/lt/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Pagrindinis valdymo skydelis, rodantis visas 8 užklausų inžinerijos šablonus su jų charakteristikomis ir naudojimo atvejais*

## Šablonų tyrinėjimas

Internetinė sąsaja leidžia eksperimentuoti su skirtingomis užklausų strategijomis. Kiekvienas šablonas sprendžia skirtingas problemas – išbandykite juos ir pamatykite, kada kuris metodas geriausiai veikia.

### Mažas ir didelis entuziazmas

Užduokite paprastą klausimą, pavyzdžiui, „Kiek yra 15 % iš 200?“ naudodami mažą entuziazmą. Jūs gausite greitą, tiesioginį atsakymą. Dabar užduokite kažką sudėtingo, pavyzdžiui, „Sukurkite talpyklos strategiją didelio srauto API“ naudodami didelį entuziazmą. Žiūrėkite, kaip modelis sulėtėja ir pateikia išsamų paaiškinimą. Tas pats modelis, ta pati klausimo struktūra – bet užklausa nurodo, kiek svarstymo reikia.

<img src="../../../translated_images/lt/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Greitas skaičiavimas su minimaliu svarstymu*

<img src="../../../translated_images/lt/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Išsami talpyklos strategija (2,8MB)*

### Užduoties vykdymas (Įrankių įvadvai)

Daugiažingsniai procesai naudingiausi, kai yra išankstinis planavimas ir proceso pasakojimas. Modelis išdėsto, ką darys, pasakoja apie kiekvieną žingsnį ir apibendrina rezultatus.

<img src="../../../translated_images/lt/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*REST galinio taško kūrimas su žingsnis po žingsnio pasakojimu (3,9MB)*

### Savianalizuojantis kodas

Išbandykite „Sukurti el. pašto patvirtinimo paslaugą“. Vietoj to, kad tiesiog generuotų kodą ir sustotų, modelis generuoja, įvertina pagal kokybės kriterijus, nustato silpnybes ir tobulina. Pamatysite, kaip jis kartoja, kol kodas pasiekia gamybinį lygį.

<img src="../../../translated_images/lt/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Visiška el. pašto patvirtinimo paslauga (5,2MB)*

### Struktūruota analizė

Kodo peržiūroms reikalingi nuoseklūs vertinimo pagrindai. Modelis analizuoja kodą pagal fiksuotas kategorijas (teisingumas, praktika, našumas, saugumas) su skirtingais rimtumo lygiais.

<img src="../../../translated_images/lt/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Į pagrindą įdedama kodo peržiūra*

### Daugkartinis pokalbis

Paklauskite „Kas yra Spring Boot?“ ir iš karto paklauskite „Pateikite pavyzdį“. Modelis prisimena jūsų pirmą klausimą ir pateikia būtent Spring Boot pavyzdį. Be atminties, antras klausimas būtų per daug neaiškus.

<img src="../../../translated_images/lt/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Konteksto išlaikymas tarp klausimų*

### Žingsnis po žingsnio mąstymas

Pasirinkite matematikos uždavinį ir išbandykite jį naudodami Žingsnis po žingsnio mąstymą ir Mažą entuziazmą. Mažas entuziazmas pateikia tik atsakymą – greitai, bet neaiškiai. Žingsnis po žingsnio parodo visus skaičiavimus ir sprendimus.

<img src="../../../translated_images/lt/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matematinė problema su aiškiais etapais*

### Apribotas išvestis

Kai reikia specifinių formatų ar žodžių skaičiaus, šis šablonas griežtai laikosi reikalavimų. Išbandykite sukurti santrauką, kuri turi tiksliai 100 žodžių ir yra pateikta punktų forma.

<img src="../../../translated_images/lt/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Mašininio mokymosi santrauka su formato kontrole*

## Ką Iš Tikrųjų Išmokstate

**Mąstymo pastangos keičia viską**

GPT-5.2 leidžia valdyti skaičiavimo pastangas per jūsų užklausas. Mažos pastangos reiškia greitus atsakymus su minimaliu tyrinėjimu. Didelės pastangos leidžia modeliui giliau apmąstyti. Jūs mokotės derinti pastangas prie užduoties sudėtingumo – nešvaistykite laiko paprastiems klausimams, bet ir neskubėkite priimti sudėtingų sprendimų.

**Struktūra vadovauja elgesiui**

Pastebite XML žymas užklausose? Jos nėra dekoratyvios. Modeliai labiau paiso struktūruotų nurodymų nei laisvo teksto. Kai reikia daugiažingsnių procesų ar sudėtingos logikos, struktūra padeda modeliui sekti, kur jis yra ir kas toliau.

<img src="../../../translated_images/lt/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Gerai struktūruotos užklausos anatomija su aiškiomis skyriais ir XML stiliaus organizacija*

**Kokybė per savianalizę**

Savianalizuojantys šablonai veikia taip, kad kokybės kriterijai yra aiškiai išdėstyti. Vietoj to, kad tikėtumėtės, jog modelis „padarys teisingai“, jūs tiksliai nurodote, ką reiškia „teisingai“: teisinga logika, klaidų valdymas, našumas, saugumas. Modelis gali įvertinti savo išvestį ir tobulėti. Tai paverčia kodo generavimą iš loterijos į procesą.

**Kontekstas yra ribotas**

Daugkartiniai pokalbiai veikia pridėdami žinučių istoriją prie kiekvieno užklausos. Tačiau yra ribos – kiekvienas modelis turi maksimalų žetonų skaičių. Kai pokalbiai ilgėja, reikės strategijų, kaip išlaikyti aktualų kontekstą, neviršijant ribos. Šis modulis parodo, kaip veikia atmintis; vėliau sužinosite, kada apibendrinti, kada pamiršti ir kada atkurti informaciją.

## Tolimesni žingsniai

**Kitas modulis:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigacija:** [← Ankstesnis: Modulis 01 - Įvadas](../01-introduction/README.md) | [Atgal į pagrindinį](../README.md) | [Kitas: Modulis 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors stengiamės užtikrinti tikslumą, atkreipkite dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Pradinė dokumento versija gimtąja kalba laikoma autoritetingu šaltiniu. Svarbiai informacijai rekomenduojame naudoti profesionalų žmogaus vertimą. Mes neatsakome už bet kokius nesusipratimus ar neteisingus aiškinimus, atsiradusius dėl šio vertimo naudojimo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->