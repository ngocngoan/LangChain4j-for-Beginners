# Modulis 02: Užklausų kūrimas su GPT-5.2

## Turinys

- [Ką išmoksite](../../../02-prompt-engineering)
- [Reikalavimai](../../../02-prompt-engineering)
- [Užklausų kūrimo supratimas](../../../02-prompt-engineering)
- [Užklausų kūrimo pagrindai](../../../02-prompt-engineering)
  - [Zero-Shot užklausos](../../../02-prompt-engineering)
  - [Few-Shot užklausos](../../../02-prompt-engineering)
  - [Mąstymo grandinė](../../../02-prompt-engineering)
  - [Rolės pagrindu kuriamos užklausos](../../../02-prompt-engineering)
  - [Užklausų šablonai](../../../02-prompt-engineering)
- [Pažangiosios šablonų naudojimo strategijos](../../../02-prompt-engineering)
- [Esamų „Azure“ išteklių naudojimas](../../../02-prompt-engineering)
- [Programos ekrano kopijos](../../../02-prompt-engineering)
- [Šablonų tyrinėjimas](../../../02-prompt-engineering)
  - [Mažas prieš didelį norą](../../../02-prompt-engineering)
  - [Užduočių vykdymas (įrankių įvados)](../../../02-prompt-engineering)
  - [Savianalitiniai kodai](../../../02-prompt-engineering)
  - [Struktūruota analizė](../../../02-prompt-engineering)
  - [Daugiasluoksnis pokalbis](../../../02-prompt-engineering)
  - [Žingsnis po žingsnio mąstymas](../../../02-prompt-engineering)
  - [Apribotas išvestis](../../../02-prompt-engineering)
- [Ką iš tikrųjų išmokstate](../../../02-prompt-engineering)
- [Kiti žingsniai](../../../02-prompt-engineering)

## Ką išmoksite

<img src="../../../translated_images/lt/what-youll-learn.c68269ac048503b2.webp" alt="Ką išmoksite" width="800"/>

Antrajame modulyje matėte, kaip atmintis leidžia pokalbių dirbtiniam intelektui ir naudojote GitHub modelius pagrindiniams sąveikos veiksmams. Dabar susitelksime į tai, kaip jūs užduodate klausimus — patys užklausimai — naudodami Azure OpenAI GPT-5.2. Kaip struktūruojate savo užklausas, stipriai veikia gaunamų atsakymų kokybę. Pradėsime nuo pagrindinių užklausų kūrimo metodų apžvalgos, o tada pereisime prie aštuonių pažangių šablonų, kurie pilnai išnaudoja GPT-5.2 galimybes.

Naudosime GPT-5.2, nes jis įveda mąstymo kontrolę – galite nurodyti modeliui, kiek mąstymo reikia atlikti prieš atsakant. Tai leidžia aiškiau matyti skirtingų užklausų strategijų skirtumus ir padeda suprasti, kada naudoti kiekvieną požiūrį. Taip pat pasinaudosime „Azure“ mažesniais apribojimais GPT-5.2 lyginant su GitHub modeliais.

## Reikalavimai

- Įvykdytas Modulis 01 (įdiegti Azure OpenAI ištekliai)
- `.env` failas šakniniame kataloge su Azure paskyros duomenimis (sukurtas naudojant `azd up` Modulyje 01)

> **Pastaba:** Jei dar neįvykdėte Modulio 01, pirmiausia vadovaukitės ten pateiktomis diegimo instrukcijomis.

## Užklausų kūrimo supratimas

<img src="../../../translated_images/lt/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Kas yra užklausų kūrimas?" width="800"/>

Užklausų kūrimas reiškia įvesties teksto sukūrimą, kuris nuosekliai suteikia jums reikalingus rezultatus. Tai ne tik klausimų uždavimas – tai prašymų struktūrizavimas taip, kad modelis tiksliai suprastų, ko norite, ir kaip tai pateikti.

Įsivaizduokite, jog duodate instrukciją kolegai. „Pataisyk klaidą“ skamba neaiškiai. „Pataisyk null pointer exception klaidą UserService.java faile 45 eilutėje pridėdamas nulio patikrinimą“ yra konkretu. Kalbos modeliai veikia taip pat – svarbūs konkretumas ir struktūra.

<img src="../../../translated_images/lt/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kaip integruojasi LangChain4j" width="800"/>

LangChain4j teikia infrastruktūrą – modelių jungtis, atmintį ir žinučių tipus – o užklausų šablonai yra tiesiog kruopščiai struktūruotas tekstas, siunčiamas per šią infrastruktūrą. Pagrindiniai komponentai yra `SystemMessage` (nustatantis AI elgesį ir vaidmenį) ir `UserMessage` (kuris neša jūsų tikrąjį prašymą).

## Užklausų kūrimo pagrindai

<img src="../../../translated_images/lt/five-patterns-overview.160f35045ffd2a94.webp" alt="Penkių užklausų kūrimo šablonų apžvalga" width="800"/>

Prieš imdamiesi pažangių šablonų šiame modulyje, peržiūrėkime penkias pagrindines užklausų kūrimo technikas. Tai yra pagrindinės užklausų inžinerijos priemonės, kurias turėtų žinoti kiekvienas. Jei jau dirbote su [Greitojo starto moduliu](../00-quick-start/README.md#2-prompt-patterns), esate matę šiuos metodus — čia pateikiama jų konceptuali sistema.

### Zero-Shot užklausos

Paprastumas: pateikiate modeliui tiesioginę instrukciją be jokių pavyzdžių. Modelis remiasi tik savo mokymu, kad suprastų ir įvykdytų užduotį. Tai tinka paprastiems prašymams, kurių elgesys aiškus.

<img src="../../../translated_images/lt/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot užklausos" width="800"/>

*Tiesioginė instrukcija be pavyzdžių – modelis užduotį supranta vien iš instrukcijos*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Atsakymas: "Teigiamas"
```

**Naudojimo atvejai:** Paprastos klasifikacijos, tiesioginiai klausimai, vertimai ar bet kokios užduotys, kurias modelis gali atlikti be papildomų nurodymų.

### Few-Shot užklausos

Pateikite pavyzdžius, kurie demonstruoja norimą modelio elgesio šabloną. Modelis iš jūsų pavyzdžių išmoksta tikėtiną įvesties-išvesties formatą ir taiko jį naujoms įvestims. Tai labai pagerina nuoseklumą užduotyse, kur norimas formatas ar elgesys nėra akivaizdus.

<img src="../../../translated_images/lt/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot užklausos" width="800"/>

*Mokymasis iš pavyzdžių – modelis atpažįsta šabloną ir taiko jį naujoms įvestims*

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

**Naudojimo atvejai:** Individualios klasifikacijos, nuosekli formatavimas, konkrečios srities užduotys ar kai zero-shot rezultatai yra nenuspėjami.

### Mąstymo grandinė

Paprašykite modelio parodyti savo mąstymą žingsnis po žingsnio. Vietoje to, kad iškart duotų atsakymą, modelis suskaido problemą ir išryškina kiekvieną žingsnį. Tai pagerina tikslumą matematikos, logikos ir daugiapakopių mąstymo užduočių atvejais.

<img src="../../../translated_images/lt/chain-of-thought.5cff6630e2657e2a.webp" alt="Mąstymo grandinės užklausos" width="800"/>

*Žingsnių po žingsnio samprotavimas – kompleksiškų problemų skaidymas į aiškius loginčius žingsnius*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Modelis rodo: 15 - 8 = 7, tada 7 + 12 = 19 obuolių
```

**Naudojimo atvejai:** Matematinės užduotys, loginės problemos, klaidų taisymas arba bet kokios užduotys, kuriose samprotavimo demonstravimas gerina tikslumą ir pasitikėjimą.

### Rolės pagrindu kuriamos užklausos

Nustatykite AI personą ar vaidmenį prieš užduodami klausimą. Tai suteikia kontekstą, kuris formuoja atsakymo toną, gylį ir fokusą. „Programinės įrangos architektas“ pateikia kitokius patarimus nei „jaunesnysis programuotojas“ ar „saugumo auditorius“.

<img src="../../../translated_images/lt/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rolės pagrindu kuriamos užklausos" width="800"/>

*Konteksto ir personos nustatymas – tas pats klausimas gauna skirtingą atsakymą, priklausomai nuo priskirto vaidmens*

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

**Naudojimo atvejai:** Kodo peržiūros, mokymas, konkrečios srities analizė arba kai reikia atsakymų, pritaikytų tam tikram ekspertų lygiui ar požiūriui.

### Užklausų šablonai

Sukurkite pakartotinai naudojamas užklausas su kintamaisiais vietos rezervatoriais. Vietoje to, kad kiekvieną kartą rašytumėte naują užklausą, apibrėžkite šabloną vieną kartą ir užpildykite skirtingas vertes. LangChain4j `PromptTemplate` klasė tai palengvina naudodama `{{variable}}` sintaksę.

<img src="../../../translated_images/lt/prompt-templates.14bfc37d45f1a933.webp" alt="Užklausų šablonai" width="800"/>

*Pakartotinai naudojamos užklausos su kintamaisiais vietos ženkluose – vienas šablonas, daug panaudojimų*

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

**Naudojimo atvejai:** Pakartotiniai užklausimai su skirtingais įvesties duomenimis, masinis apdorojimas, pakartotinai naudojamų DI darbo srautų kūrimas arba bet kokiomis situacijomis, kai užklausos struktūra nesikeičia, bet keičiasi duomenys.

---

Šie penki pagrindai suteikia jums tvirtą įrankių rinkinį daugumai užklausų kūrimo užduočių. Likusi šio modulio dalis praplėsta su **aštuoniais pažangiais šablonais**, kurie išnaudoja GPT-5.2 mąstymo kontrolę, savianalizę ir struktūrizuotą išvestį.

## Pažangiosios šablonų naudojimo strategijos

Atsiradus pagrindams, pereikime prie aštuonių pažangių šablonų, kurie daro šį modulį unikalų. Ne visoms problemoms reikia vienodos strategijos. Kai kurie klausimai reikalauja greitų atsakymų, kiti – gilios analizės. Vieniems reikalingas matomas mąstymas, kitiems tik rezultatai. Žemiau kiekvienas šablonas yra optimizuotas skirtingam scenarijui – o GPT-5.2 mąstymo kontrolė dar labiau paryškina skirtumus.

<img src="../../../translated_images/lt/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Aštuoni užklausų kūrimo šablonai" width="800"/>

*Apžvalga apie aštuonis užklausų inžinerijos šablonus ir jų taikymo sritis*

<img src="../../../translated_images/lt/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Mąstymo kontrolė su GPT-5.2" width="800"/>

*GPT-5.2 mąstymo kontrolė leidžia nurodyti, kiek mąstymo modelis turi atlikti – nuo greitų tiesioginių atsakymų iki gilių tyrimų*

<img src="../../../translated_images/lt/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Mąstymo pastangų palyginimas" width="800"/>

*Mažas noras (greitas, tiesioginis) prieš aukštą norą (išsamus, tyrinėjančias) mąstymo metodai*

**Mažas noras (Greita & Susitelkusi)** — skirtas paprastiems klausimams, kur norite greitų ir tiesioginių atsakymų. Modelis atlieka minimalų mąstymą – daugiausiai 2 žingsnius. Naudokite skaičiavimams, paieškai ar paprastiems klausimams.

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

> 💡 **Išbandykite su GitHub Copilot:** Atidarykite [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ir paklauskite:
> - „Kuo skiriasi mažo ir didelio noro užklausų kūrimo šablonai?“
> - „Kaip XML žymos užklausose padeda struktūruoti DI atsakymą?“
> - „Kada naudoti savianalizės šablonus, o kada tiesiogines instrukcijas?“

**Aukštas noras (Gilus & Išsamus)** — skirta sudėtingoms problemoms, kai norite išsamios analizės. Modelis atlieka gilią analizę ir pateikia detalias samprotavimo priežastis. Naudokite sistemų projektavimui, architektūros sprendimams ar sudėtingiems tyrimams.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Užduočių vykdymas (Žingsnis po žingsnio pažanga)** — skirtas daugiažingsniams darbo procesams. Modelis pateikia išankstinį planą, pasakoja apie kiekvieną žingsnį vykdymo metu, o galiausiai pateikia santrauką. Naudokite migracijoms, įgyvendinimams ar bet kuriam daugiažingsnių procesų.

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

Mąstymo grandinės užklausos tiesiogiai prašo modelio parodyti savo samprotavimo procesą, gerindamos tikslumą sudėtingose užduotyse. Žingsnis po žingsnio išskaidymas padeda suprasti logiką tiek žmogui, tiek DI.

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) pokalbiu:** Paklauskite apie šį šabloną:
> - „Kaip pritaikyti užduoties vykdymo šabloną ilgai trunkančioms operacijoms?“
> - „Kokios geriausios praktikos įrankių įvadų struktūravimui gamybinėse programose?“
> - „Kaip galima fiksuoti ir rodyti tarpinę pažangos informaciją vartotojo sąsajoje?“

<img src="../../../translated_images/lt/task-execution-pattern.9da3967750ab5c1e.webp" alt="Užduočių vykdymo šablonas" width="800"/>

*Planavimas → Vykdymas → Apibendrinimas daugiažingsnėms užduotims*

**Savianalitiniai kodai** — skirtas generuoti gamybos kokybės kodui. Modelis kuria kodą, laikydamasis gamybos standartų ir tinkamo klaidų valdymo. Naudokite, kai kuriate naujas funkcijas ar paslaugas.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/lt/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Savianalizės ciklas" width="800"/>

*Iteratyvus tobulinimo ciklas – generuoti, vertinti, identifikuoti problemas, gerinti, kartoti*

**Struktūruota analizė** — nuosekliai vertinimui. Modelis peržiūri kodą naudodamas fiksuotą sistemą (teisingumas, praktikos, našumas, saugumas, palaikomumas). Naudokite kodo peržiūroms ar kokybės vertinimams.

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

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) pokalbiu:** Paklauskite apie struktūruotą analizę:
> - „Kaip pritaikyti analizės sistemą skirtingų tipų kodo peržiūroms?“
> - „Koks geriausias būdas programiškai apdoroti ir reaguoti į struktūruotą išvestį?“
> - „Kaip užtikrinti nuoseklius sunkumo lygius skirtingose peržiūros sesijose?“

<img src="../../../translated_images/lt/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Struktūruotos analizės šablonas" width="800"/>

*Sistema nuoseklioms kodo peržiūroms su sunkumo lygiais*

**Daugiasluoksnis pokalbis** — kai pokalbiui reikalingas kontekstas. Modelis prisimena ankstesnes žinutes ir remiasi jomis. Naudokite interaktyvioms pagalbos sesijoms ar sudėtingiems klausimams ir atsakymams.

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

*Kaip pokalbio kontekstas kaupiasi per kelis žingsnius iki žodžių limito pasiekimo*

**Žingsnis po žingsnio mąstymas** — problemoms, reikalaujančioms matomos logikos. Modelis aiškiai pateikia samprotavimą kiekviename žingsnyje. Naudokite matematikos užduotims, loginėms mįslėms ar kai reikia suprasti mąstymo procesą.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/lt/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Žingsnis po žingsnio šablonas" width="800"/>

*Problemų skaidymas į aiškius loginčius žingsnius*

**Apribotas išvestis** — atsakymams su specifiniais formato reikalavimais. Modelis griežtai laikosi formato ir ilgio taisyklių. Naudokite santraukų sudarymui ar kai jums reikia tikslios išvesties struktūros.

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

<img src="../../../translated_images/lt/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Apriboto išvesties šablonas" width="800"/>

*Griežto formato, ilgio ir struktūros reikalavimų įgyvendinimas*

## Esamų „Azure“ išteklių naudojimas

**Patikrinkite diegimą:**

Įsitikinkite, kad šakniniame kataloge yra `.env` failas su Azure paskyros duomenimis (sukurtas Modulyje 01):
```bash
cat ../.env  # Turėtų parodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Paleiskite programą:**

> **Pastaba:** Jei jau paleidote visas programas naudodami `./start-all.sh` iš Modulio 01, šis modulis jau veikia per 8083 prievadą. Galite praleisti žemiau pateiktas paleidimo komandas ir tiesiog nueiti į http://localhost:8083.

**Parinktis 1: Naudojant Spring Boot Dashboard (rekomenduojama VS Code vartotojams)**

„Dev“ konteineryje yra „Spring Boot Dashboard“ plėtinys, kuris suteikia grafinę sąsają valdyti visas Spring Boot programas. Jį rasite Activity Bar kairėje VS Code pusėje (ieškokite Spring Boot ikonos).
Iš Spring Boot prietaisų skydelio galite:
- Matyti visas turimas Spring Boot programas darbo aplinkoje
- Vienu mygtuko paspaudimu paleisti / sustabdyti programas
- Realio laiko režimu peržiūrėti programų žurnalus
- Stebėti programų būseną

Tiesiog spustelėkite leistuko mygtuką šalia „prompt-engineering“ norėdami paleisti šį modulį arba paleiskite visus modulius iš karto.

<img src="../../../translated_images/lt/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**2 variantas: naudojant apvalkalo scenarijus**

Paleiskite visas internetines programas (modulius 01-04):

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

Abu scenarijai automatiškai įkelia aplinkos kintamuosius iš pagrindinio `.env` failo ir sukompiliuos JAR failus, jei jų nėra.

> **Pastaba:** Jei norite rankiniu būdu sukompiliuoti visus modulius prieš paleidimą:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Naršyklėje atidarykite http://localhost:8083.

**Sustabdyti:**

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

*Pagrindinis prietaisų skydelis rodantis visus 8 promptų inžinerijos modelius su jų charakteristikomis ir naudojimo atvejais*

## Modelių tyrinėjimas

Tinklo sąsaja leidžia eksperimentuoti su skirtingomis užklausų strategijomis. Kiekvienas modelis sprendžia skirtingas problemas - išbandykite juos, kad pamatytumėte, kada kuris metodas geriausiai tinka.

### Mažas ir aukštas užsidegimas

Užduokite paprastą klausimą, pavyzdžiui „Kiek yra 15 % iš 200?“ naudodami Mažą užsidegimą. Gaunate momentinį, tiesioginį atsakymą. Dabar užduokite sudėtingą klausimą, pavyzdžiui „Sukurkite žymių talpyklos strategiją didelio srauto API“, naudodami Aukštą užsidegimą. Stebėkite, kaip modelis sulėtėja ir pateikia detalią analizę. Tas pats modelis, tokia pati klausimo struktūra, bet užklausa nurodo, kiek mąstymo reikia.

<img src="../../../translated_images/lt/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Greitas skaičiavimas su minimalia analize*

<img src="../../../translated_images/lt/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Išsami žymių talpyklos strategija (2.8MB)*

### Užduočių vykdymas (įrankių įvadinės dalys)

Daugiapakopiai darbo srautai naudos pradines plano sudarymo ir proceso aprašymo funkcijas. Modelis nurodo, ką darys, pasakoja apie kiekvieną žingsnį ir apibendrina rezultatus.

<img src="../../../translated_images/lt/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*REST galinio taško kūrimas su žingsnis po žingsnio aprašymu (3.9MB)*

### Savianalizės kodas

Išbandykite „Sukurti el. pašto validacijos servisą“. Vietoje vien tik generavimo ir sustojimo, modelis generuoja, vertina pagal kokybės kriterijus, identifikuoja trūkumus ir tobulina. Matysite, kaip vyksta iteracijos iki gamybinių standartų pasiekimo.

<img src="../../../translated_images/lt/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Baigtas el. pašto validacijos servisas (5.2MB)*

### Struktūruota analizė

Kodo peržiūroms reikia nuoseklios vertinimo sistemos. Modelis analizuoja kodą naudodamas fiksuotas kategorijas (teisingumas, praktikos, našumas, saugumas) su skirtingais griežtumo lygiais.

<img src="../../../translated_images/lt/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Kodo peržiūra pagal sistemą*

### Daugiapakopis pokalbis

Paklauskite „Kas yra Spring Boot?“ ir iš karto užduokite „Parodyk pavyzdį“. Modelis atsimena jūsų pirmą klausimą ir pateikia būtent Spring Boot pavyzdį. Be atminties antras klausimas būtų per daug neaiškus.

<img src="../../../translated_images/lt/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Konteksto išlaikymas per visus klausimus*

### Žingsnis po žingsnio argumentavimas

Pasirinkite matematikos uždavinį ir išbandykite tiek žingsnis po žingsnio argumentavimą, tiek Mažą užsidegimą. Mažas užsidegimas tiesiog pateikia atsakymą – greitai, bet neaiškiai. Žingsnis po žingsnio parodo kiekvieną skaičiavimą ir sprendimą.

<img src="../../../translated_images/lt/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matematikos problema su aiškiais žingsniais*

### Apribotas outputas

Kai reikia specifinių formatų ar žodžių skaičiaus, šis modelis griežtai laikosi reikalavimų. Išbandykite sugeneruoti santrauką su tiksliai 100 žodžių punktų formatu.

<img src="../../../translated_images/lt/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Mašininio mokymosi santrauka su formatavimo kontrole*

## Ko iš tikrųjų mokotės

**Argumentavimo pastangos keičia viską**

GPT-5.2 leidžia valdyti skaičiavimo pastangas per užklausas. Mažos pastangos reiškia greitus atsakymus su minimaliu tyrimu. Didelės pastangos reiškia, kad modelis skiria laiko giliam mąstymui. Mokotės pritaikyti pastangą užduoties sudėtingumui – nešvaistykite laiko paprastiems klausimams, bet ir nepraleiskite svarbių gilių sprendimų.

**Struktūra nurodo elgesį**

Pastebėjote XML žymes užklausose? Jos nėra dekoratyvinės. Modeliai labiau laikosi struktūruotų instrukcijų nei laisvo teksto. Kai reikia daugiapakopių procesų ar sudėtingos logikos, struktūra padeda modeliui sekti, kur jis yra ir kas vyksta toliau.

<img src="../../../translated_images/lt/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

\*Gerai struktūruotos užklausos anatomija su aiškiomis sekcijomis ir XML stiliaus organizavimu\*

**Kokybė per savianalizę**

Savireflektuojantys modeliai veikia aiškiai nurodydami kokybės kriterijus. Vietoje to, kad modelis „tiktų teisingai“, jūs tiksliai nurodote, ką reiškia „teisingai“: teisinga logika, klaidų tvarkymas, našumas, saugumas. Modelis tada gali įvertinti savo rezultatą ir tobulėti. Tai paverčia kodų generavimą iš loterijos į procesą.

**Kontekstas yra ribotas**

Daugiapakopiai pokalbiai veikia įtraukiant praėjusių žinučių istoriją į kiekvieną užklausą. Bet yra riba – kiekvienas modelis turi maksimalų tokenų skaičių. Kai pokalbiai ilgesni, reikia strategijų, kad išlaikyti svarbią informaciją neviršijant limito. Šis modulis parodo, kaip veikia atmintis; vėliau išmoksite kada santrumpinti, kada pamiršti ir kada atgaivinti informaciją.

## Tolimesni žingsniai

**Kitas modulis:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigacija:** [← Ankstesnis: Modulis 01 - Įvadas](../01-introduction/README.md) | [Atgal į pagrindinį](../README.md) | [Kitas: Modulis 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės atsisakymas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, prašome atkreipti dėmesį, kad automatizuoti vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas gimtąja kalba turi būti laikomas autoritetingu šaltiniu. Svarbiai informacijai rekomenduojamas profesionalus žmogaus atliktas vertimas. Mes neatsakome už bet kokius nesusipratimus ar klaidingas interpretacijas, kylančias dėl šio vertimo naudojimo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->