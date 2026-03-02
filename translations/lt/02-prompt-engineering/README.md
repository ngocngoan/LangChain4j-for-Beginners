# Modulis 02: Užklausų inžinerija su GPT-5.2

## Turinys

- [Vaizdo įrašo peržiūra](../../../02-prompt-engineering)
- [Ko išmoksite](../../../02-prompt-engineering)
- [Reikalavimai](../../../02-prompt-engineering)
- [Suprasti užklausų inžineriją](../../../02-prompt-engineering)
- [Užklausų inžinerijos pagrindai](../../../02-prompt-engineering)
  - [Nulinis pavyzdys (Zero-Shot Prompting)](../../../02-prompt-engineering)
  - [Keletas pavyzdžių (Few-Shot Prompting)](../../../02-prompt-engineering)
  - [Minties grandinė (Chain of Thought)](../../../02-prompt-engineering)
  - [Pagrįsta vaidmenimis (Role-Based Prompting)](../../../02-prompt-engineering)
  - [Užklausų šablonai](../../../02-prompt-engineering)
- [Pažangios modelių formos](../../../02-prompt-engineering)
- [Esamų Azure išteklių naudojimas](../../../02-prompt-engineering)
- [Programėlės ekrano nuotraukos](../../../02-prompt-engineering)
- [Modelių tyrinėjimas](../../../02-prompt-engineering)
  - [Mažas prieš Didelis Entuziazmas](../../../02-prompt-engineering)
  - [Užduoties vykdymas (įrankių įvadas)](../../../02-prompt-engineering)
  - [Savirefleksinis kodas](../../../02-prompt-engineering)
  - [Struktūrizuota analizė](../../../02-prompt-engineering)
  - [Daugiaprasmių pokalbių valdymas](../../../02-prompt-engineering)
  - [Žingsnis po žingsnio samprotavimas](../../../02-prompt-engineering)
  - [Apribotas rezultatas](../../../02-prompt-engineering)
- [Ką jūs tikrai išmokstate](../../../02-prompt-engineering)
- [Tolimesni žingsniai](../../../02-prompt-engineering)

## Vaizdo įrašo peržiūra

Žiūrėkite šią tiesioginę sesiją, kurioje paaiškinama, kaip pradėti naudotis šiuo moduliu:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Tiesioginė sesija" width="800"/></a>

## Ko išmoksite

<img src="../../../translated_images/lt/what-youll-learn.c68269ac048503b2.webp" alt="Ko išmoksite" width="800"/>

Antrajame modulyje matėte, kaip atmintis leidžia pokalbių AI veikti ir naudojote GitHub modelius pagrindinėms sąveikoms. Dabar sutelksime dėmesį į tai, kaip užduodate klausimus – pačias užklausas – naudojant Azure OpenAI GPT-5.2. Užklausų struktūra stipriai veikia atsakymų kokybę. Pradedame peržiūrėdami pagrindines užklausų kūrimo technikas, o tada pereisime prie aštuonių pažangių modelių, kurie maksimaliai išnaudoja GPT-5.2 galimybes.

Naudosime GPT-5.2, nes jis pristato samprotavimo valdymą – galite modeliui pasakyti, kiek jis turi mąstyti prieš atsakydamas. Tai daro skirtingas užklausų strategijas aiškesnes ir padeda suprasti, kada naudoti kurią iš jų. Taip pat pasinaudosime Azure mažesniais GPT-5.2 greičio apribojimais, palyginti su GitHub modeliais.

## Reikalavimai

- Užbaigtas Modulis 01 (diegiami Azure OpenAI ištekliai)
- `.env` failas šakniniame kataloge su Azure prisijungimo duomenimis (sukurtas „azd up“ Modulyje 01)

> **Pastaba:** Jei dar neužbaigėte Modulio 01, pirmiausia vykdykite ten esančias diegimo instrukcijas.

## Suprasti užklausų inžineriją

<img src="../../../translated_images/lt/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Kas yra užklausų inžinerija?" width="800"/>

Užklausų inžinerija – tai įvesties teksto kūrimas, kuris nuspėjamai duoda jums reikalingus rezultatus. Tai ne tik klausimų uždavimas – tai užklausų struktūrizavimas taip, kad modelis tiksliai suprastų, ko norite ir kaip tai pateikti.

Įsivaizduokite, kad duodate instrukcijas kolegai. „Ištaisyk klaidą“ yra neaišku. „Ištaisyk null pointer exception UserService.java 45 eilutėje, pridėdamas null tikrinimą“ – konkrečiai. Kalbos modeliai veikia panašiai – svarbi konkretika ir struktūra.

<img src="../../../translated_images/lt/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kaip įsikomponuoja LangChain4j" width="800"/>

LangChain4j suteikia infrastruktūrą — modelių jungtis, atmintį ir žinučių tipus — o užklausų modeliai yra tiesiog kruopščiai sukurti tekstai, siunčiami per šią infrastruktūrą. Pagrindiniai statybiniai blokai yra `SystemMessage` (nustato AI elgesį ir vaidmenį) ir `UserMessage` (neša jūsų tikrąjį užklausimą).

## Užklausų inžinerijos pagrindai

<img src="../../../translated_images/lt/five-patterns-overview.160f35045ffd2a94.webp" alt="Penki užklausų inžinerijos modeliai – apžvalga" width="800"/>

Prieš gilindamiesi į pažangius modulius, peržvelkime penkias pagrindines užklausų kūrimo technikas. Tai statybiniai blokai, kuriuos turi žinoti kiekvienas užklausų inžinierius. Jei jau esate dirbę su [Greitojo starto moduliu](../00-quick-start/README.md#2-prompt-patterns), matėte juos veikiant – čia pateikiama jų koncepcinė sistema.

### Nulinis pavyzdys (Zero-Shot Prompting)

Paprastas metodas: duoti modelį tiesioginę instrukciją be pavyzdžių. Modelis visiškai remiasi savo apmokymu, kad suprastų ir atliktų užduotį. Tai puikiai tinka paprastiems užklausimams, kai numatomas elgesys yra aiškus.

<img src="../../../translated_images/lt/zero-shot-prompting.7abc24228be84e6c.webp" alt="Nulinis pavyzdys" width="800"/>

*Tiesioginė instrukcija be pavyzdžių – modelis supranta užduotį vien tik pagal instrukciją*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Atsakymas: "Teigiamas"
```

**Kada naudoti:** Paprasta klasifikacija, tiesioginiai klausimai, vertimai arba bet kokios užduotys, kurias modelis gali atlikti be papildomos pagalbos.

### Keletas pavyzdžių (Few-Shot Prompting)

Pateikite pavyzdžius, kurie demonstruoja modelio norimą laikytis modelį. Modelis iš jūsų pavyzdžių išmoksta pageidaujamą įvesties ir išvesties formatą ir jį pritaiko naujoms įvestims. Tai žymiai pagerina nuoseklumą užduotyse, kurių norimas formatas ar elgesys nėra akivaizdus.

<img src="../../../translated_images/lt/few-shot-prompting.9d9eace1da88989a.webp" alt="Keletas pavyzdžių" width="800"/>

*Mokymasis iš pavyzdžių – modelis atpažįsta modelį ir pritaiko jį naujoms įvestims*

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

**Kada naudoti:** Individualizuotoms klasifikacijoms, nuosekliam formatavimui, specifinėms sritims arba kai nulinio pavyzdžio rezultatai yra nepatikimi.

### Minties grandinė (Chain of Thought)

Prašykite modelio parodyti savo samprotavimus žingsnis po žingsnio. Vietoj tiesioginio atsakymo modelis išskaido problemą ir aiškiai dirba per kiekvieną jo dalį. Tai pagerina tikslumą matematikos, logikos ir daugiapakopių samprotavimų užduotyse.

<img src="../../../translated_images/lt/chain-of-thought.5cff6630e2657e2a.webp" alt="Minties grandinės užklausos" width="800"/>

*Samprotavimų žingsnis po žingsnio – sudėtingų problemų skaidymas į aiškius loginius žingsnius*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Modelis rodo: 15 - 8 = 7, tada 7 + 12 = 19 obuolių
```

**Kada naudoti:** Matematikos uždaviniams, logikos galvosūkams, derinimui ar bet kokiai užduočiai, kur samprotavimų proceso parodymas pagerina tikslumą ir pasitikėjimą.

### Pagrįsta vaidmenimis (Role-Based Prompting)

Nustatykite AI asmenybę ar vaidmenį prieš tai užduodami klausimą. Tai suteikia kontekstą, kuris formuoja atsakymo toną, gilumą ir dėmesį. „Programinės įrangos architektas“ duoda kitokius patarimus nei „jaunesnysis programuotojas“ ar „saugumo audito specialistas“.

<img src="../../../translated_images/lt/role-based-prompting.a806e1a73de6e3a4.webp" alt="Užklausos pagal vaidmenį" width="800"/>

*Konteksto ir asmenybės nustatymas – tas pats klausimas gauna skirtingą atsakymą priklausomai nuo priskirto vaidmens*

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

**Kada naudoti:** Kodo peržiūroms, mokymui, specifinei sričių analizei arba kai reikia atsakymų, pritaikytų konkrečiam ekspertų lygiui ar požiūriui.

### Užklausų šablonai

Kurti pakartotinai naudojamas užklausas su kintamaisiais žymekliais. Vietoj to, kad kiekvieną kartą rašytumėte naują užklausą, apibrėžkite šabloną kartą ir pildykite skirtingas reikšmes. LangChain4j klasė `PromptTemplate` tai palengvina su `{{variable}}` sintakse.

<img src="../../../translated_images/lt/prompt-templates.14bfc37d45f1a933.webp" alt="Užklausų šablonai" width="800"/>

*Pakartotinai naudojamos užklausos su kintamaisiais – vienas šablonas, daugybė panaudojimų*

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

**Kada naudoti:** Kartotini užklausimai su skirtingomis įvestimis, partijų apdorojimas, pakartotinai naudojamų AI darbo eigų kūrimas arba bet kuri situacija, kai užklausos struktūra išlieka ta pati, bet kinta duomenys.

---

Šie penki pagrindai suteikia tvirtą įrankių rinkinį daugumai užklausų užduočių. Kitas šio modulio turinys remiasi jais su **aštuoniais pažangiais modeliais**, kurie išnaudoja GPT-5.2 samprotavimo valdymą, savirefleksiją ir struktūrizuoto rezultato galimybes.

## Pažangios modelių formos

Įsisavinę pagrindus, pereikime prie aštuonių pažangių modelių, kurie daro šį modulį unikalų. Ne visoms problemoms reikia to paties požiūrio. Kai kurie klausimai reikalauja greitų atsakymų, kiti – gilaus apmąstymo. Kai kuriems reikia matomo samprotavimo, kitiems – tik rezultatų. Kiekvienas žemiau pateiktas modelis yra optimizuotas skirtingam scenarijui – o GPT-5.2 samprotavimo valdymas dar labiau ryškina skirtumus.

<img src="../../../translated_images/lt/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Aštuoni užklausų inžinerijos modeliai" width="800"/>

*Aštuoni užklausų inžinerijos modeliai ir jų taikymo atvejai*

<img src="../../../translated_images/lt/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Samprotavimo valdymas su GPT-5.2" width="800"/>

*GPT-5.2 samprotavimo valdymas leidžia nurodyti, kiek mąstyti modelis turėtų – nuo greitų tiesioginių atsakymų iki gilaus tyrimo*

**Mažas entuziazmas (Greita ir aktualesnė)** – skirtas paprastiems klausimams, kai norite greitų, tiesioginių atsakymų. Modelis atlieka minimalų samprotavimą – maksimaliai 2 žingsnius. Naudokite tai skaičiavimams, paieškoms ar paprastiems klausimams.

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

> 💡 **Ištirkite su GitHub Copilot:** Atidarykite [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ir paklauskite:
> - "Kuo skiriasi žemų ir aukštų entuziazmo užklausų modeliai?"
> - "Kaip XML žymos užklausose padeda struktūruoti AI atsakymą?"
> - "Kada naudoti savirefleksijos modelius, o kada tiesiogines instrukcijas?"

**Aukštas entuziazmas (Giluminis ir išsamus)** – sudėtingoms problemoms, kai norite išsamaus analizės. Modelis giliai tyrinėja ir rodo išsamų samprotavimą. Naudokite tai sistemos projektavimui, architektūros sprendimams ar kompleksiniams tyrimams.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Užduoties vykdymas (Žingsnis po žingsnio pažanga)** – daugiažingsnėms darbo eigos užduotims. Modelis pateikia pradinį planą, pasakoja apie kiekvieną žingsnį jo vykdymo metu, o pabaigoje pateikia santrauką. Naudokite tai migracijoms, įgyvendinimams ar bet kokiam daugiažingsniam procesui.

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

Minties grandinės užklausa aiškiai prašo modelio parodyti samprotavimo procesą, kas pagerina sudėtingų užduočių tikslumą. Žingsnis po žingsnio išskaidymas padeda tiek žmonėms, tiek AI suprasti logiką.

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) pokalbių funkcija:** Paklauskite apie šį modelį:
> - "Kaip pritaikyti užduoties vykdymo modelį ilgoms operacijoms?"
> - "Kokios geros praktikos įrankių įvadų struktūravimui gamybinėse programėlėse?"
> - "Kaip galima sugauti ir rodyti tarpinius progreso atnaujinimus vartotojo sąsajoje?"

<img src="../../../translated_images/lt/task-execution-pattern.9da3967750ab5c1e.webp" alt="Užduoties vykdymo modelis" width="800"/>

*Planavimas → Vykdymas → Santrauka daugiažingsnėms užduotims*

**Savirefleksinis kodas** – kodui gamybinės kokybės generavimui. Modelis generuoja kodą, laikantis gamybinės kokybės standartų su tinkamu klaidų valdymu. Naudokite tai naujų funkcijų ar paslaugų kūrimui.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/lt/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Savirefleksijos ciklas" width="800"/>

*Iteracinio tobulinimo ciklas – generavimas, vertinimas, problemų identifikavimas, tobulinimas, pakartojimas*

**Struktūrizuota analizė** – sistemingam vertinimui. Modelis peržiūri kodą naudodamas fiksuotą sistemą (teisingumas, praktikos, našumas, saugumas, priežiūra). Naudokite tai kodo peržiūroms ar kokybės vertinimams.

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

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) pokalbių funkcija:** Paklauskite apie struktūrizuotą analizę:
> - "Kaip pritaikyti analizės sistemą skirtingų tipų kodo peržiūroms?"
> - "Koks geriausias būdas apdoroti ir veikti pagal struktūrizuotą išvestį programiškai?"
> - "Kaip užtikrinti nuoseklius rimtumo lygius skirtingose peržiūrų sesijose?"

<img src="../../../translated_images/lt/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Struktūrizuotos analizės modelis" width="800"/>

*Sistema nuoseklioms kodo peržiūroms su rimtumo lygiais*

**Daugiapakopis pokalbis** – pokalbiams, kuriems reikia konteksto. Modelis prisimena ankstesnes žinutes ir kuria toliau. Naudokite tai interaktyviems pagalbos seansams ar sudėtingiems klausimų atsakymams.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/lt/context-memory.dff30ad9fa78832a.webp" alt="Konteksto atmintis" width="800"/>

*Kaip pokalbio kontekstas kaupiasi per kelis žingsnius iki žodžių apribojimo*

**Žingsnis po žingsnio samprotavimas** – problemoms, kurioms reikalingas matomas loginis procesas. Modelis rodo aiškius samprotavimus kiekvienam žingsniui. Naudokite tai matematikos problemoms, logikos galvosūkiams ar kai reikia suprasti mąstymo procesą.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/lt/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Žingsnis po žingsnio modelis" width="800"/>

*Problemos skaidymas į aiškius loginius žingsnius*

**Apribotas rezultatas** – atsakymams su specifiniais formato reikalavimais. Modelis griežtai laikosi formato ir ilgumo taisyklių. Naudokite tai santraukų kūrimui ar kai reikia tikslios išvesties struktūros.

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

<img src="../../../translated_images/lt/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Apriboto rezultato modelis" width="800"/>

*Griežtų formato, ilgumo ir struktūros reikalavimų taikymas*

## Esamų Azure išteklių naudojimas

**Patikrinkite diegimą:**

Įsitikinkite, kad `.env` failas yra šakniniame kataloge su Azure prisijungimo duomenimis (sukurtas modulyje 01):
```bash
cat ../.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Paleiskite programėlę:**

> **Pastaba:** Jei jau paleidote visas programas naudodami `./start-all.sh` Modulyje 01, šis modulis jau veikia 8083 prievade. Galite praleisti paleidimo komandas žemiau ir tiesiog eiti į http://localhost:8083.
**1 variantas: naudojant Spring Boot darbalaukį (rekomenduojama VS Code vartotojams)**

Dev konteineryje yra Spring Boot darbalaukio plėtinys, kuris suteikia vizualią sąsają valdyti visas Spring Boot programas. Jį galite rasti veiklos juostoje kairėje VS Code pusėje (ieškokite Spring Boot ikonos).

Iš Spring Boot darbalaukio galite:
- Matyti visas turimas Spring Boot programas darbo aplinkoje
- Vienu spustelėjimu paleisti/stabdyti programas
- Realaus laiko režimu peržiūrėti programų žurnalus
- Stebėti programų būseną

Tiesiog spustelėkite grojimo mygtuką šalia „prompt-engineering“, kad paleistumėte šį modulį, arba paleiskite visus modulius iškart.

<img src="../../../translated_images/lt/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**2 variantas: naudojant komandas terminale**

Paleisti visas internetines programas (modulius 01–04):

**Bash:**
```bash
cd ..  # Iš šakninių katalogų
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iš šakninių katalogų
.\start-all.ps1
```

Arba paleisti tik šį modulį:

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

Abi komandos automatiškai įkelia aplinkos kintamuosius iš šakniniame kataloge esančio `.env` failo ir sukurs JAR failus, jei jų dar nėra.

> **Pastaba:** Jei norite rankiniu būdu sukompiliuoti visus modulius prieš paleidimą:
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

**Norint sustabdyti:**

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

## Programos ekrano kopijos

<img src="../../../translated_images/lt/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Pagrindinis darbalaukis, rodantis visas 8 promptų inžinerijos schemas su jų charakteristikomis ir naudojimo atvejais*

## Schemos tyrinėjimas

Internetinė sąsaja leidžia eksperimentuoti su skirtingomis užklausų strategijomis. Kiekviena schema sprendžia skirtingas problemas – išbandykite jas, kad pamatytumėte, kada kuri strategija veikia geriausiai.

> **Pastaba: Srautinė vs Nesrautinė perkėla** — Kiekviename schemos puslapyje yra du mygtukai: **🔴 Stream Response (Live)** ir **Nesrautinė** parinktis. Srautinė perkėla naudoja Server-Sent Events (SSE), kad realiu laiku rodytų modelio generuojamus žodžius, tad matote progresą iš karto. Nesrautinė parinktis laukia visos atsakymo pabaigos, kol jį parodo. Užklausoms, kurios sukelia gilų apmąstymą (pvz., Didelis entuziazmas, Savianalizė kode), nesrautinė perkėla gali užtrukti labai ilgai – kartais kelias minutes – be jokios matomos grįžtamosios informacijos. **Naudokite srautinę perkėlą eksperimentuodami su sudėtingomis užklausomis**, kad matytumėte, kaip modelis dirba, ir išvengtumėte įspūdžio, kad užklausa užstrigo.
>
> **Pastaba: Naršyklės reikalavimas** — Srautinė perkėla naudoja Fetch Streams API (`response.body.getReader()`), kuri reikalauja pilnavertės naršyklės (Chrome, Edge, Firefox, Safari). Ji **neveikia** VS Code įmontuotame Simple Browser, nes jo žiniatinklio vaizdas nepalaiko ReadableStream API. Jei naudojate Simple Browser, nesrautiniai mygtukai veiks įprastai – paveikti tik srautinių mygtukų funkcionalumas. Atidarykite `http://localhost:8083` išorinėje naršyklėje, kad gautumėte pilną patirtį.

### Mažas vs didelis entuziazmas

Užduokite paprastą klausimą, pvz., „Kiek yra 15 % iš 200?“ naudojant Mažą entuziazmą. Gaunate momentinį tiesioginį atsakymą. Dabar užduokite sudėtingą užduotį, pvz., „Sukurkite kešavimo strategiją didelio srauto API“, naudodami Didelį entuziazmą. Spustelėkite **🔴 Stream Response (Live)** ir stebėkite, kaip modelio detalus mąstymas atsiskleidžia žodis po žodžio. Tas pats modelis, ta pati klausimo struktūra – tačiau užklausa nurodo, kiek mąstymo atlikti.

### Užduočių vykdymas (įrankių įžangos)

Daugiapakopėms užduotims reikia išankstinio planavimo ir proceso aprašymo. Modelis aprašo, ką darys, pasakoja apie kiekvieną žingsnį ir apibendrina rezultatus.

### Savianalizės kodas

Išbandykite „Sukurti el. pašto validacijos paslaugą“. Vietoje to, kad tik sugeneruotų kodą ir sustotų, modelis generuoja, vertina pagal kokybės kriterijus, identifikuoja silpnas vietas ir tobulina. Matysite, kaip jis iteruoja tol, kol kodas atitinka gamybos standartus.

### Struktūruota analizė

Kodo peržiūroms reikalingi nuoseklūs vertinimo kriterijai. Modelis analizuoja kodą naudodamas fiksuotas kategorijas (teisingumas, praktikos, našumas, saugumas) su skirtingais svarbumo lygiais.

### Daugiapakopis pokalbis

Užduokite klausimą „Kas yra Spring Boot?“, o tada iškart paklauskite „Pateikite pavyzdį“. Modelis prisimena pirmąjį klausimą ir pateikia specifinį Spring Boot pavyzdį. Be atminties antras klausimas būtų per daug neaiškus.

### Žingsnis po žingsnio mąstymas

Pasirinkite matematikos uždavinį ir išbandykite jį tiek su Žingsnis po žingsnio mąstymu, tiek su Mažu entuziazmu. Mažas entuziazmas tiesiog pateikia atsakymą – greita, bet neaišku. Žingsnis po žingsnio parodo kiekvieną skaičiavimą ir sprendimą.

### Ribotas išvesties formatas

Kai reikia specifinių formatų ar žodžių skaičiaus, ši schema reikalauja griežto laikymosi. Išbandykite sugeneruoti santrauką, kuri turi turėti tiksliai 100 žodžių, pateiktų sąrašo formatu.

## Ką jūs iš tikrųjų mokotės

**Mąstymo pastangos keičia viską**

GPT-5.2 leidžia valdyti skaičiavimo pastangas per užklausas. Mažos pastangos reiškia greitus atsakymus su minimaliu tyrinėjimu. Didelės pastangos reiškia, kad modelis skiria laiko giliau mąstyti. Mokotės suderinti pastangas su užduoties sudėtingumu – nevarkite dėl paprastų klausimų, bet ir neskubėkite priimti sudėtingų sprendimų.

**Struktūra nukreipia elgesį**

Pastebėjote XML žymes užklausose? Jos nėra tik dekoracija. Modeliai patikimiau laikosi struktūrinio nurodymo nei laisvo teksto. Reikalaujant daugiažingsnių procesų ar sudėtingos logikos struktūra padeda modeliui sekti, kur jis yra ir kas bus toliau.

<img src="../../../translated_images/lt/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Gerai suorganizuotos užklausos anatomija su aiškiomis dalimis ir XML stiliaus struktūra*

**Kokybė per savianalizę**

Savianalizės schemos veikia aiškiai nurodant kokybės kriterijus. Vietoje to, kad tikėtumėtės, jog modelis „padarys teisingai“, jūs tiksliai nurodote, ką reiškia „teisingai“: teisinga logika, klaidų valdymas, našumas, saugumas. Modelis tada gali įvertinti savo rezultatus ir tobulinti juos. Tai paverčia kodo generavimą ne loterija, o procesu.

**Kontextas yra ribotas**

Daugiapakopiai pokalbiai veikia įtraukiant pranešimų istoriją į kiekvieną užklausą. Tačiau yra limitas – kiekvienas modelis turi maksimalų žodžių kiekį. Didėjant pokalbiams, reikės strategijų išlaikyti svarbiausią kontekstą nesiekus limito. Šis modulis parodo, kaip veikia atmintis; vėliau išmoksite, kada apibendrinti, kada pamiršti ir kada atkurti informaciją.

## Tolimesni žingsniai

**Kitas modulis:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigacija:** [← Ankstesnis: 01 modulis - Įvadas](../01-introduction/README.md) | [Atgal į pradžią](../README.md) | [Kitas: 03 modulis - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors stengiamės užtikrinti tikslumą, prašome atkreipti dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas jo gimtąja kalba turėtų būti laikomas autoritetingu šaltiniu. Svarbiai informacijai rekomenduojama naudoti profesionalų žmogišką vertimą. Mes neatsakome už jokią painiavą ar klaidingą supratimą, kilusį dėl šio vertimo naudojimo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->