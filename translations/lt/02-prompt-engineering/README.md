# Modulis 02: Užklausų inžinerija su GPT-5.2

## Turinys

- [Vaizdo įrašo pristatymas](../../../02-prompt-engineering)
- [Ką išmoksite](../../../02-prompt-engineering)
- [Priešmokymai](../../../02-prompt-engineering)
- [Kas yra užklausų inžinerija](../../../02-prompt-engineering)
- [Užklausų inžinerijos pagrindai](../../../02-prompt-engineering)
  - [Zero-Shot užklausos](../../../02-prompt-engineering)
  - [Few-Shot užklausos](../../../02-prompt-engineering)
  - [Mąstymo grandinė](../../../02-prompt-engineering)
  - [Rolės pagrindu veikianti užklausa](../../../02-prompt-engineering)
  - [Užklausų šablonai](../../../02-prompt-engineering)
- [Išplėstiniai modeliai](../../../02-prompt-engineering)
- [Esamų Azure išteklių naudojimas](../../../02-prompt-engineering)
- [Programos ekrano nuotraukos](../../../02-prompt-engineering)
- [Modelių tyrinėjimas](../../../02-prompt-engineering)
  - [Mažas prieš didelį entuziazmą](../../../02-prompt-engineering)
  - [Užduočių vykdymas (Įrankių įvadai)](../../../02-prompt-engineering)
  - [Savarankiškas reflektavimas](../../../02-prompt-engineering)
  - [Struktūrinė analizė](../../../02-prompt-engineering)
  - [Daugiaetapiai pokalbiai](../../../02-prompt-engineering)
  - [Žingsnis po žingsnio mąstymas](../../../02-prompt-engineering)
  - [Apribotas išvestis](../../../02-prompt-engineering)
- [Ką iš tikrųjų išmokstate](../../../02-prompt-engineering)
- [Tolimesni žingsniai](../../../02-prompt-engineering)

## Vaizdo įrašo pristatymas

Žiūrėkite šią tiesioginę sesiją, kurioje paaiškinama, kaip pradėti darbą su šiuo moduliu: [Prompt Engineering with LangChain4j - Live Session](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## Ką išmoksite

<img src="../../../translated_images/lt/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

Ankstesniame modulyje matėte, kaip atmintis leidžia pokalbių DI ir naudojote GitHub modelius pagrindinėms sąveikoms. Dabar sutelksime dėmesį į klausimų uždavimą – pačias užklausas – naudojant Azure OpenAI GPT-5.2. Kaip struktūruojate savo užklausas, smarkiai įtakoja atsakymų kokybę. Pradedame nuo pagrindinių užklausų technikų apžvalgos, tada judame prie aštuonių pažangių modelių, kurie išnaudoja GPT-5.2 galimybes.

Naudosime GPT-5.2, nes jis įveda mąstymo valdymą – galite nustatyti modeliui, kiek ir kaip gilintis prieš atsakant. Tai leidžia aiškiau matyti skirtingas užklausų strategijas ir padeda suprasti, kada naudoti kurį metodą. Taip pat pasinaudosime Azure mažesniais GPT-5.2 greičio apribojimais, palyginti su GitHub modeliais.

## Priešmokymai

- Įvykdytas 01 modulis (parengti Azure OpenAI ištekliai)
- `.env` failas pagrindiniame kataloge su Azure kredencialais (sukurtas komandą `azd up` 01 modulyje)

> **Pastaba:** Jei dar nebaigėte 01 modulio, pirmiausia sekite ten pateiktas diegimo instrukcijas.

## Kas yra užklausų inžinerija

<img src="../../../translated_images/lt/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Užklausų inžinerija – tai įvesčių tekstų projektavimas, kuris nuosekliai duoda jums reikiamus rezultatus. Tai ne tik klausimų uždavimas – tai prašymų struktūravimas taip, kad modelis tiksliai suprastų, ko norite ir kaip tai pateikti.

Įsivaizduokite, kad duodate nurodymus kolegai. „Pataisyk klaidą“ yra neaišku. „Pataisyk null pointer exception UserService.java 45 eilutėje pridėdamas nulio patikrinimą“ yra konkretu. Kalbos modeliai veikia taip pat – svarbu konkretumas ir struktūra.

<img src="../../../translated_images/lt/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j suteikia infrastruktūrą — modelių jungtis, atmintį ir žinučių tipus — tuo tarpu užklausų modeliai yra tiesiog atsakingai struktūruotas tekstas, kuris siunčiamas per tą infrastruktūrą. Pagrindiniai blokai yra `SystemMessage` (nustato DI elgesį ir vaidmenį) ir `UserMessage` (kuris neša jūsų faktinį prašymą).

## Užklausų inžinerijos pagrindai

<img src="../../../translated_images/lt/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Prieš pradėdami pažengusių modelių pristatymą šiame modulyje, peržvelkime penkias pagrindines užklausų technikas. Tai yra pagrindiniai blokai, kuriuos turi žinoti kiekvienas užklausų inžinierius. Jei jau dirbote su [Greito pradžios moduliu](../00-quick-start/README.md#2-prompt-patterns), tai matėte jas veiksme – čia pateikiama jų konceptuali sistema.

### Zero-Shot užklausos

Paprastesnis būdas: duokite modeliui tiesioginį nurodymą be pavyzdžių. Modelis visiškai remiasi savo apmokymu užduočiai suprasti ir atlikti. Tai gerai veikia paprastoms užklausoms, kuriose aiškus elgesys yra akivaizdus.

<img src="../../../translated_images/lt/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Tiesioginis nurodymas be pavyzdžių – modelis išveda užduotį tik iš nurodymo*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Atsakymas: "Teigiamas"
```

**Kada naudoti:** paprastos klasifikacijos, tiesioginiai klausimai, vertimai arba bet kokios užduotys, kurias modelis gali atlikti be papildomų nurodymų.

### Few-Shot užklausos

Pateikite pavyzdžius, kurie parodo modelio norimą elgesio modelį. Modelis išmoksta tikėtą įvesties-išvesties formatą iš jūsų pavyzdžių ir taiko jį naujoms įvestims. Tai žymiai pagerina nuoseklumą atliekant užduotis, kai pageidaujamas formatas ar elgesys nėra akivaizdus.

<img src="../../../translated_images/lt/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Mokymasis iš pavyzdžių – modelis atpažįsta modelį ir taiko jį naujoms įvestims*

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

**Kada naudoti:** pasirinktinei klasifikacijai, nuosekliam formatavimui, srities specifinėms užduotims ar kai zero-shot rezultatai yra nesuderinti.

### Mąstymo grandinė

Prašykite modelio parodyti savo mąstymą žingsnis po žingsnio. Užuot iškart pateikęs atsakymą, modelis išskaido problemą ir aiškiai dirba su kiekvienu jos aspektu. Tai pagerina tikslumą matematikos, logikos ir daugiapakopių mąstymo užduočių atvejais.

<img src="../../../translated_images/lt/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Žingsnis po žingsnio mąstymas – sudėtingų problemų išskaidymas į aiškius loginčius veiksmus*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Modelis rodo: 15 - 8 = 7, tada 7 + 12 = 19 obuolių
```

**Kada naudoti:** matematikos užduotims, loginėms mįslėms, derinimui arba bet kuriai užduočiai, kur parodytas mąstymo procesas pagerina tikslumą ir pasitikėjimą.

### Rolės pagrindu veikianti užklausa

Priskirkite DI asmenybę ar vaidmenį prieš užduodant klausimą. Tai suteikia kontekstą, kuris formuoja atsakymo toną, gylį ir dėmesį. „Programinės įrangos architektas“ pateiks kitokias rekomendacijas nei „jaunesnysis programuotojas“ ar „saugumo auditorius“.

<img src="../../../translated_images/lt/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Konteksto ir asmenybės nustatymas – tas pats klausimas gauna skirtingą atsakymą, priklausomai nuo priskirto vaidmens*

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

**Kada naudoti:** kodo peržiūroms, mokymui, srities specifinėms analizėms arba kai reikia atsakymų pritaikytų pagristam ekspertizės lygiui arba perspektyvai.

### Užklausų šablonai

Sukurkite pakartotinai naudojamus užklausų šablonus su kintamųjų vietomis. Vietoj to, kad rašytumėte naują užklausą kiekvieną kartą, apibrėžkite šabloną vieną kartą ir pildykite skirtingas reikšmes. LangChain4j `PromptTemplate` klasė tai palengvina naudodama `{{variable}}` sintaksę.

<img src="../../../translated_images/lt/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Pakartotinai naudojamos užklausos su kintamųjų vietomis – vienas šablonas, daug panaudojimų*

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

**Kada naudoti:** pakartotinėms užklausoms su skirtingomis įvestimis, partijų apdorojimui, kuriant pakartotinai naudojamus DI darbo srautus arba bet kurioje situacijoje, kur užklausos struktūra lieka ta pati, bet keičiasi duomenys.

---

Šie penki pagrindai suteikia tvirtą įrankių rinkinį daugumai užklausų užduočių. Likusi modulio dalis grindžiama jais ir pristato **aštuonis pažangius modelius**, kurie išnaudoja GPT-5.2 mąstymo valdymą, saviįvertinimą ir struktūrinės išvesties galimybes.

## Išplėstiniai modeliai

Turėdami pagrindus, pereikime prie aštuonių pažangių modelių, kurie daro šį modulį išskirtinį. Ne visoms problemoms tinka tas pats požiūris. Kai kuriems klausimams reikia greitų atsakymų, kitiems – gilaus apmąstymo. Kai kuriems – matomo mąstymo, o kitiems užtenka tik rezultatų. Kiekvienas modelis žemiau yra optimizuotas skirtingai situacijai – ir GPT-5.2 mąstymo valdymas dar labiau išryškina skirtumus.

<img src="../../../translated_images/lt/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Aštuoni užklausų inžinerijos modelių apžvalga ir jų panaudojimo sritys*

<img src="../../../translated_images/lt/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 mąstymo valdymas leidžia nurodyti, kiek mąstymo turi atlikti modelis – nuo greitų tiesioginių atsakymų iki gilios analizės*

**Mažas entuziazmas (greitai ir fokusas)** – paprastiems klausimams, kur reikia greitų, tiesioginių atsakymų. Modelis atlieka minimalų mąstymą – daugiausiai 2 žingsnius. Naudokite tai skaičiavimams, paieškoms ar paprastiems klausimams.

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

> 💡 **Išbandykite su GitHub Copilot:** Atverkite [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ir paklauskite:
> - „Kuo skiriasi mažo entuziazmo ir didelio entuziazmo užklausų modeliai?“
> - „Kaip XML žymės užklausose padeda struktūruoti DI atsakymą?“
> - „Kada naudoti saviįvertinimo modelius, o kada tiesioginius nurodymus?“

**Didelis entuziazmas (gilus ir kruopštus)** – sudėtingoms problemoms, kur norite išsamios analizės. Modelis kruopščiai nagrinėja ir pateikia detalią argumentaciją. Naudokite tai sistemos dizainui, architektūros sprendimams ar sudėtingiems tyrimams.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Užduočių vykdymas (žingsnis po žingsnio)** – daugiažingsniams darbo srautams. Modelis pateikia planą iš anksto, pasakoja apie kiekvieną žingsnį darbo metu ir galiausiai pateikia santrauką. Naudokite migracijoms, įgyvendinimams ar bet kokiems daugiažingsniams procesams.

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

Mąstymo grandinės užklausa tiesiogiai reikalauja, kad modelis parodytų savo mąstymo procesą, kas pagerina sudėtingų užduočių tikslumą. Žingsnis po žingsnio išskaidymas padeda tiek žmonėms, tiek DI suprasti logiką.

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) pokalbiu:** Paklauskite apie šį modelį:
> - „Kaip adaptuoti užduočių vykdymo modelį ilgalaikėms operacijoms?“
> - „Kokios geriausios praktikos kuriant įrankių įvadus gamybos aplikacijose?“
> - „Kaip UI fiksuoti ir rodyti tarpinę pažangos informaciją?“

<img src="../../../translated_images/lt/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Planavimas → Vykdymas → Santrauka daugiažingsnių užduočių darbo srautui*

**Saviįvertinantis kodas** – gamybos klasės kodo generavimui. Modelis kuria kodą laikydamasis gamybos standartų su tinkamu klaidų valdymu. Naudokite tai kuriant naujas funkcijas ar paslaugas.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/lt/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iteracinio tobulėjimo ciklas – generuoti, vertinti, identifikuoti problemas, gerinti, kartoti*

**Struktūrinė analizė** – nuosekliam vertinimui. Modelis peržiūri kodą pagal nustatytą schemą (tikslumas, praktikos, našumas, saugumas, palaikymas). Naudokite kodo peržiūroms arba kokybės vertinimams.

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

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) pokalbiu:** Paklauskite apie struktūrinę analizę:
> - „Kaip pritaikyti analizės sistemą skirtingų tipų kodo peržiūroms?“
> - „Koks geriausias būdas programiškai apdoroti ir veikti pagal struktūrinės išvesties duomenis?“
> - „Kaip užtikrinti nuoseklius rimtumo lygius per skirtingas peržiūros sesijas?“

<img src="../../../translated_images/lt/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Sistema nuoseklioms kodo peržiūroms su rimtumo lygiais*

**Daugiaetapiai pokalbiai** – pokalbiams, kuriems reikia konteksto. Modelis prisimena ankstesnes žinutes ir jas naudoja. Naudokite interaktyvioms pagalbos sesijoms arba sudėtingiems klausimams-atsakymams.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/lt/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Kaip pokalbio kontekstas kaupiasi per kelis ciklus, kol pasiekiamas tokenų limitas*

**Žingsnis po žingsnio mąstymas** – užduotims, kurioms reikia matomos logikos. Modelis rodo aiškią argumentaciją kiekvienam etapui. Naudokite matematikos užduotims, logikos mįslėms arba kai reikia suprasti mąstymo procesą.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/lt/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Sudėtingų problemų išskaidymas į aiškius loginčius veiksmus*

**Apribotas išvestis** – atsakymams su konkrečiais formato reikalavimais. Modelis griežtai laikosi formato ir ilgio taisyklių. Naudokite santraukoms arba kai reikia tikslios išvesties struktūros.

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

<img src="../../../translated_images/lt/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Konkrečių formato, ilgio ir struktūros reikalavimų užtikrinimas*

## Esamų Azure išteklių naudojimas

**Patikrinkite diegimą:**

Įsitikinkite, kad pagrindiniame kataloge yra `.env` failas su Azure kredencialais (sukurtas 01 modulio metu):
```bash
cat ../.env  # Turėtų parodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Paleiskite programą:**

> **Pastaba:** Jei jau paleidote visas programas naudodami `./start-all.sh` 01 modulyje, šis modulis jau veikia 8083 prievade. Galite praleisti žemiau pateiktas starto komandas ir eiti tiesiai į http://localhost:8083.

**1 variantas: naudokite Spring Boot valdymo priemonę (rekomenduojama VS Code vartotojams)**
Dev konteineryje yra pridėta Spring Boot valdymo skydelio plėtinys, kuris suteikia vizualią sąsają visoms Spring Boot programoms valdyti. Jį rasite veiklos juostoje kairėje VS Code pusėje (ieškokite Spring Boot ikonos).

Iš Spring Boot valdymo skydelio galite:
- Matyti visas darbo erdvėje esančias Spring Boot programas
- Vienu spustelėjimu paleisti/stabdyti programas
- Realio laiko režimu peržiūrėti programų žurnalus
- Stebėti programų būklę

Tiesiog spustelėkite paleidimo mygtuką šalia „prompt-engineering“, kad paleistumėte šį modulį, arba paleiskite visus modulius iš karto.

<img src="../../../translated_images/lt/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**2 variantas: Naudojant shell scenarijus**

Paleiskite visas žiniatinklio programas (modulius 01-04):

**Bash:**
```bash
cd ..  # Iš šakniniame kataloge
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iš šakninių katalogo
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

Abu scenarijai automatiškai įkelia aplinkos kintamuosius iš šakniniame lygmenyje esančio `.env` failo ir sukurs JAR failus, jei jų nėra.

> **Pastaba:** Jei norite paleisti visų modulių statymą rankiniu būdu prieš paleidžiant:
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

Naršyklėje atidarykite http://localhost:8083.

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

## Programos ekrano kopijos

<img src="../../../translated_images/lt/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Pagrindinis valdymo skydelis, rodantis visas 8 promptų inžinerijos schemas su jų savybėmis ir panaudojimo atvejais*

## Schemos tyrinėjimas

Žiniatinklio sąsaja leidžia eksperimentuoti su skirtingomis prašymų strategijomis. Kiekviena schema sprendžia skirtingas problemas – išbandykite jas, kad pamatytumėte, kada kiekvienas metodas geriausiai veikia.

> **Pastaba: Srautinė transliacija prieš nesrautinę** — Kiekviename schemos puslapyje yra du mygtukai: **🔴 Srautinė atsakymo transliacija (tiesioginė)** ir **Nesrautinė** parinktis. Srautinė naudoja Server-Sent Events (SSE), kad rodytų žodžius realiu laiku, kol modelis juos generuoja, todėl matote pažangą iš karto. Nesrautinė parinktis laukia pilno atsakymo prieš jį rodant. Prašymams, kurie sukelia gilų mąstymą (pvz., Didelis entuziazmas, Savianalitinė kodo vertinimas), nesrautinė užklausa gali užtrukti labai ilgai – kartais minutes – be jokios matomos reakcijos. **Naudokite srautinę transliaciją, kai eksperimentuojate su sudėtingais prašymais**, kad galėtumėte matyti modelio darbą ir išvengtumėte įspūdžio, kad užklausa užstrigo.
>
> **Pastaba: Naršyklės reikalavimas** — Srautinė funkcija naudoja Fetch Streams API (`response.body.getReader()`), kuri reikalauja pilnos naršyklės (Chrome, Edge, Firefox, Safari). Ji **neveikia** VS Code įmontuotame Simple Browser, nes jo webview nepalaiko ReadableStream API. Jei naudojate Simple Browser, nesrautiniai mygtukai veiks įprastai – paveikia tik srautinės transliacijos mygtukai. Pilnai patirčiai atidarykite `http://localhost:8083` išorinėje naršyklėje.

### Mažas prieš didelį entuziazmą

Užduokite paprastą klausimą, pavyzdžiui, „Kiek yra 15 % iš 200?“ naudodami Mažą entuziazmą. Gaunate greitą ir tiesioginį atsakymą. Dabar užduokite sudėtingesnį klausimą, pavyzdžiui, „Sukurkite talpyklos strategiją didelio eismo API“ naudodami Didelį entuziazmą. Spustelėkite **🔴 Srautinė atsakymo transliacija (tiesioginė)** ir stebėkite, kaip atsiranda detalus modelio samprotavimas žodis po žodžio. Tas pats modelis, ta pati klausimo struktūra – bet prašymas nurodo, kiek mąstymo atlikti.

### Užduoties vykdymas (priemonės pradinės eilutės)

Daugiažingsniai darbo procesai naudingesni dėl išankstinio planavimo ir proceso aprašymo. Modelis išdėsto, ką darys, pasakoja apie kiekvieną žingsnį, tada apibendrina rezultatus.

### Savianalitinė kodo refleksija

Išbandykite „Sukurkite el. pašto tikrinimo paslaugą“. Vietoje tik generuoti kodą ir sustoti, modelis generuoja, vertina pagal kokybės kriterijus, identifikuoja silpnybes ir tobulina. Matysite, kaip jis iteruoja, kol kodas atitinka gamybos standartus.

### Struktūrinė analizė

Kodo peržiūrai reikalingi nuoseklūs vertinimo pagrindai. Modelis analizuoja kodą naudojant fiksuotas kategorijas (teisingumas, praktikos, našumas, saugumas) su svarbių lygių klasifikacija.

### Daugkartinis pokalbis

Paklauskite „Kas yra Spring Boot?“ tada nedelsdami tęskite „Pateikite pavyzdį“. Modelis prisimena pirmą klausimą ir pateikia konkretų Spring Boot pavyzdį. Be atminties antras klausimas būtų pernelyg bendras.

### Žingsnis po žingsnio samprotavimas

Pasirinkite matematinę užduotį ir išbandykite tiek Žingsnis po žingsnio samprotavimą, tiek Mažą entuziazmą. Mažas entuziazmas duoda tik atsakymą – greitą, bet neaiškų. Žingsnis po žingsnio parodo kiekvieną skaičiavimą ir sprendimą.

### Apribotas išvesties formatas

Kai reikia specifinių formatų ar žodžių skaičiaus, ši schema užtikrina griežtą laikymąsi. Išbandykite sugeneruoti santrauką su tiksliai 100 žodžių punktų formatu.

## Ko jūs iš tikrųjų mokotės

**Samprotavimo pastangos keičia viską**

GPT-5.2 leidžia valdyti skaičiavimo pastangas per jūsų prašymus. Mažos pastangos reiškia greitus atsakymus su minimaliu tyrimu. Didelės pastangos reiškia, kad modelis rimtai apmąsto. Mokotės pritaikyti pastangas prie užduoties sudėtingumo – nešvaistykite laiko paprastiems klausimams, bet ir neskubėkite su sudėtingais sprendimais.

**Struktūra valdo elgesį**

Pastebėjote XML žymes prašymuose? Jos nėra dekoratyvios. Modeliai laikosi struktūruotų nurodymų patikimiau nei laisvo teksto. Kai reikalingi daugiažingsniai procesai ar sudėtinga logika, struktūra padeda modeliui sekti, kur jis yra ir kas bus toliau.

<img src="../../../translated_images/lt/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Gerai struktūruoto prašymo anatomija su aiškiomis dalimis ir XML stiliaus organizacija*

**Kokybė per savianalizę**

Savianalitinės schemos veikia, padarydamos kokybės kriterijus akivaizdžius. Vietoje to, kad tikėtumėtės, jog modelis „pavyks“, jūs tiksliai nurodote, ką reiškia „teisinga“: taisyklinga logika, klaidų valdymas, našumas, saugumas. Modelis gali įvertinti savo išvestį ir tobulinti. Tai paverčia kodo generavimą iš loterijos į procesą.

**Kontekstas yra ribotas**

Daugkartiniai pokalbiai veikia įtraukiant žinučių istoriją su kiekvienu užsakymu. Bet yra riba – kiekvienas modelis turi maksimalią žodžių/ištraukoms ribą. Augant pokalbiams, reikės strategijų svarbiam kontekstui išlaikyti, neperžengiant ribos. Šis modulis parodo, kaip veikia atmintis; vėliau išmoksite, kada santrumuoti, kada pamiršti ir kada atsinešti informaciją.

## Kiti žingsniai

**Kitas modulis:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigacija:** [← Ankstesnis: Modulis 01 - Įvadas](../01-introduction/README.md) | [Atgal į pagrindinį](../README.md) | [Kitas: Modulis 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:  
Šis dokumentas buvo išverstas naudojant AI vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors stengiamės užtikrinti tikslumą, atkreipkite dėmesį, kad automatizuoti vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas gimtąja kalba turi būti laikomas pagrindiniu šaltiniu. Kritinei informacijai rekomenduojama naudoti profesionalų žmogiškąjį vertimą. Mes neatsakome už bet kokius nesusipratimus ar klaidingas interpretacijas, kylančias dėl šio vertimo naudojimo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->