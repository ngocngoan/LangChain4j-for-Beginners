# Modulis 02: Užklausų inžinerija su GPT-5.2

## Turinys

- [Ką Išmoksite](../../../02-prompt-engineering)
- [Reikalavimai](../../../02-prompt-engineering)
- [Suprasti Užklausų Inžineriją](../../../02-prompt-engineering)
- [Užklausų Inžinerijos Pagrindai](../../../02-prompt-engineering)
  - [Nulinės Bandos Užklausos](../../../02-prompt-engineering)
  - [Keletos Pavyzdžių Užklausos](../../../02-prompt-engineering)
  - [Mąstymo Grandinė](../../../02-prompt-engineering)
  - [Rolės pagrindu Užklausos](../../../02-prompt-engineering)
  - [Užklausų Šablonai](../../../02-prompt-engineering)
- [Pažangiosios Šablonų Formos](../../../02-prompt-engineering)
- [Esamų Azure Išteklų Naudojimas](../../../02-prompt-engineering)
- [Programos Ekrano Kopijos](../../../02-prompt-engineering)
- [Šablonų Tyrinėjimas](../../../02-prompt-engineering)
  - [Mažas prieš didelį norą](../../../02-prompt-engineering)
  - [Užduoties Vykdymas (Įrankių Įvadai)](../../../02-prompt-engineering)
  - [Savęs Reflektuojantis Kodas](../../../02-prompt-engineering)
  - [Struktūrizuota Analizė](../../../02-prompt-engineering)
  - [Daugiatūris Pokalbis](../../../02-prompt-engineering)
  - [Žingsnis po žingsnio Mąstymas](../../../02-prompt-engineering)
  - [Apribotas Išvestis](../../../02-prompt-engineering)
- [Ką Iš tikrųjų Išmokstate](../../../02-prompt-engineering)
- [Kiti Žingsniai](../../../02-prompt-engineering)

## Ką Išmoksite

<img src="../../../translated_images/lt/what-youll-learn.c68269ac048503b2.webp" alt="Ką Išmoksite" width="800"/>

Antrajame modulyje matėte, kaip atmintis leidžia pokalbių dirbtiniam intelektui veikti ir naudojote GitHub Modelius pagrindinėms sąveikoms. Dabar susitelksime į tai, kaip užduodate klausimus — pačias užklausas — naudodami Azure OpenAI GPT-5.2. Kaip struktūruojate savo užklausas labai stipriai įtakoja gaunamų atsakymų kokybę. Pradedame nuo pagrindinių užklausų technikų apžvalgos, tada pereiname prie aštuonių pažangių šablonų, kurie maksimaliai išnaudoja GPT-5.2 galimybes.

Naudosime GPT-5.2, nes jis įvedė mąstymo valdymą - galite modeliui nurodyti, kiek mąstyti prieš atsakant. Tai daro užklausų strategijas aiškesnes ir padeda suprasti, kada naudoti kurią metodiką. Taip pat pasinaudosime Azure mažesniais ribojimais GPT-5.2 palyginti su GitHub Modeliais.

## Reikalavimai

- Baigtas Modulis 01 (Azure OpenAI ištekliai paruošti)
- `.env` failas šakniniame kataloge su Azure kredencialais (sukurtas `azd up` Modulyje 01)

> **Pastaba:** Jei dar nebaigėte Modulio 01, pirmiausia laikykitės ten pateiktų diegimo nurodymų.

## Suprasti Užklausų Inžineriją

<img src="../../../translated_images/lt/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Kas yra užklausų inžinerija?" width="800"/>

Užklausų inžinerija reiškia įvesties teksto projektavimą, kuris nuosekliai leidžia gauti reikiamus rezultatus. Tai ne tik klausimų uždavimas - tai užklausų struktūrizavimas taip, kad modelis tiksliai suprastų, ko norite ir kaip tai pateikti.

Įsivaizduokite, kad duodate instrukcijas kolegai. „Ištaisyk klaidą“ yra neaišku. „Ištaisyk null pointer exception klaidą klasėje UserService.java 45 eilutėje pridėdamas null tikrinimą“ yra tiksliau. Kalbos modeliai veikia taip pat - svarbi specifika ir struktūra.

<img src="../../../translated_images/lt/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kaip LangChain4j dera" width="800"/>

LangChain4j teikia infrastruktūrą — modelių jungtis, atmintį ir žinučių tipus — o užklausų šablonai yra tiesiog kruopščiai struktūruotas tekstas, siunčiamas per tą infrastruktūrą. Pagrindiniai blokai yra `SystemMessage` (nustato DI elgesį ir rolę) ir `UserMessage` (neša jūsų prašymą).

## Užklausų Inžinerijos Pagrindai

<img src="../../../translated_images/lt/five-patterns-overview.160f35045ffd2a94.webp" alt="Penkios užklausų inžinerijos šablonų apžvalga" width="800"/>

Prieš gilindamiesi į pažangius šablonus šiame modulyje, peržvelkime penkias pagrindines užklausų technikas. Tai blokai, kuriuos turi žinoti kiekvienas užklausų inžinierius. Jei jau išbandėte [Greitojo pradžios modulį](../00-quick-start/README.md#2-prompt-patterns), matėte jas praktiškai — čia pateikiama koncepcinė sistema.

### Nulinės Bandos Užklausos

Paprastas būdas: duokite modeliui tiesioginę instrukciją be pavyzdžių. Modelis remiasi tik savo apmokymais, kad suprastų ir atliktų užduotį. Veikia gerai paprastoms užklausoms, kuriose elgesys yra aiškus.

<img src="../../../translated_images/lt/zero-shot-prompting.7abc24228be84e6c.webp" alt="Nulinės bandos užklausos" width="800"/>

*Tiesioginė instrukcija be pavyzdžių — modelis išsiaiškina užduotį tik iš instrukcijos*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Atsakymas: "Teigiamas"
```

**Kada naudoti:** Paprastoms klasifikacijoms, tiesioginiams klausimams, vertimams ar bet kuriai užduočiai, kurią modelis gali atlikti be papildomų nurodymų.

### Keletos Pavyzdžių Užklausos

Pateikite pavyzdžių, kurie demonstruoja modelio norimą seką. Modelis išmoksta tikėtiną įvesties-išvesties formatą iš jūsų pavyzdžių ir pritaiko naujiems duomenims. Tai žymiai gerina nuoseklumą ten, kur norimas formatas ar elgesys nėra aiškus.

<img src="../../../translated_images/lt/few-shot-prompting.9d9eace1da88989a.webp" alt="Keletos pavyzdžių užklausos" width="800"/>

*Mokomasi iš pavyzdžių — modelis atpažįsta šabloną ir taiko jį naujoms įvestims*

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

**Kada naudoti:** Individualioms klasifikacijoms, nuosekliam formatavimui, domeno specifinėms užduotims ar kai nulinės bandos rezultatai yra nenuoseklūs.

### Mąstymo Grandinė

Prašykite modelio parodyti savo mąstymo eigą žingsnis po žingsnio. Vietoj tiesioginio atsakymo modelis išskirsto problemą ir žingsnis po žingsnio ją analizuoja. Tai gerina tikslumą matematikos, logikos ir daugiapakopėse užduotyse.

<img src="../../../translated_images/lt/chain-of-thought.5cff6630e2657e2a.webp" alt="Mąstymo grandinės užklausos" width="800"/>

*Žingsnis po žingsnio mąstymas — sudėtingų problemų išskaidymas į aiškius loginus žingsnius*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Modelis rodo: 15 - 8 = 7, tada 7 + 12 = 19 obuolių
```

**Kada naudoti:** Matematikos užduotims, logikos galvosūkiams, klaidų taisymui ar bet kuriai užduočiai, kur mąstymo proceso demonstravimas pagerina tikslumą ir pasitikėjimą.

### Rolės pagrindu Užklausos

Nustatykite AI asmenybę ar vaidmenį prieš užduodami klausimą. Tai suteikia kontekstą, kuris formuoja atsakymo toną, gylį ir fokusą. „Programinės įrangos architektas“ pateikia kitokias rekomendacijas nei „jaunesnysis programuotojas“ ar „saugumo auditorius“.

<img src="../../../translated_images/lt/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rolės pagrindu užklausos" width="800"/>

*Konteksto ir asmenybės nustatymas — tas pats klausimas gauna skirtingą atsakymą priklausomai nuo priskirto vaidmens*

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

**Kada naudoti:** Kodo peržiūroms, mokymui, domeno specifinėms analizėms arba kai reikia atsakymų pagal tam tikrą ekspertizės lygį ar perspektyvą.

### Užklausų Šablonai

Kurti pakartotinai naudojamus užklausų šablonus su kintamaisiais. Vietoj kiekvieną kartą rašyti naują užklausą, apibrėžkite šabloną vieną kartą ir įrašykite skirtingas reikšmes. LangChain4j `PromptTemplate` klasė leidžia paprastai naudoti `{{variable}}` sintaksę.

<img src="../../../translated_images/lt/prompt-templates.14bfc37d45f1a933.webp" alt="Užklausų šablonai" width="800"/>

*Pakartotinai naudojami šablonai su kintamaisiais — vienas šablonas, daugybė panaudojimų*

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

**Kada naudoti:** Kartotinėms užklausoms su skirtingomis įvestimis, partijų apdorojimui, pakartotinai naudojamų DI procesų kūrimui arba bet kur, kur užklausų struktūra nesikeičia, bet duomenys skiriasi.

---

Šie penki pagrindai suteikia jums tvirtą įrankių rinkinį daugumai užklausų užduočių. Likusi šio modulio dalis remiasi jais ir pristato **aštuonis pažangius šablonus**, kurie išnaudoja GPT-5.2 mąstymo valdymą, savianalizę bei struktūrizuotą išvestį.

## Pažangiosios Šablonų Formos

Kai turime pagrindus, pereikime prie aštuonių pažangių šablonų, kurie daro šį modulį unikalų. Ne visoms problemoms tinka tas pats požiūris. Kai kurios užduotys reikalauja greitų atsakymų, kitos — gilios analizės. Kai kur reikia matomo mąstymo, kitur - tik rezultatų. Kiekvienas žemiau pateiktas šablonas yra optimizuotas skirtingam scenarijui — o GPT-5.2 mąstymo valdymas dar labiau pabrėžia skirtumus.

<img src="../../../translated_images/lt/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Aštuoni užklausų šablonai" width="800"/>

*Aštuoni užklausų inžinerijos šablonai ir jų panaudojimo atvejai*

<img src="../../../translated_images/lt/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Mąstymo valdymas su GPT-5.2" width="800"/>

*GPT-5.2 mąstymo valdymas leidžia nurodyti, kiek mąstyti modeliui – nuo greitų tiesioginių atsakymų iki gilaus tyrinėjimo*

**Mažas Noras (Greita & Fokusuota)** - Paprastiems klausimams, kuriuose norite greitų, tiesioginių atsakymų. Modelis atlieka minimalų mąstymą - daugiausia 2 žingsnius. Naudokite skaičiavimams, paieškoms ar paprastiems klausimams.

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
> - „Kuo skiriasi mažo ir didelio noro užklausų šablonai?“
> - „Kaip XML žymos užklausose padeda struktūruoti DI atsakymą?“
> - „Kada naudoti savianalizės šablonus prieš tiesiogines instrukcijas?“

**Didelis Noras (Giluminis & Išsamus)** - Kompleksinėms problemoms, kurioms reikia išsamios analizės. Modelis ištiria išsamiai ir pateikia išplėstą mąstymą. Naudokite sistemų dizainui, architektūriniams sprendimams ar sudėtingiems tyrimams.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Užduoties Vykdymas (Žingsnis po Žingsnio Progresas)** - Daugiažingsniams procesams. Modelis pateikia išankstinį planą, aprašo kiekvieną žingsnį darbo metu, tada pateikia santrauką. Naudokite migracijoms, įgyvendinimams ar bet kokiam daugiažingsniam darbui.

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

Mąstymo grandinės užklausos aiškiai prašo modelio parodyti savo mąstymo procesą, kas gerina tikslumą sudėtingose užduotyse. Žingsnis po žingsnio išskaidymas padeda tiek žmonėms, tiek DI suprasti logiką.

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Pokalbiais:** Paklauskite apie šį šabloną:
> - „Kaip pritaikyti užduoties vykdymo šabloną ilgai trunkančioms operacijoms?“
> - „Kokios geriausios praktikos struktūrizuojant įrankių įvadus gamybinėse programose?“
> - „Kaip fiksuoti ir rodyti tarpinę pažangą vartotojo sąsajoje?“

<img src="../../../translated_images/lt/task-execution-pattern.9da3967750ab5c1e.webp" alt="Užduoties Vykdymo Šablonas" width="800"/>

*Planavimas → Vykdymas → Santrauka daugiažingsniams užduotims*

**Savęs Reflektuojantis Kodas** - Produkcijos kokybės kodo generavimui. Modelis generuoja kodą pagal gamybos standartus su tinkamu klaidų valdymu. Naudokite kuriant naujas funkcijas ar paslaugas.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/lt/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Savirefleksijos ciklas" width="800"/>

*Iteracinis tobulinimo ciklas - generuoti, vertinti, identifikuoti problemas, gerinti, kartoti*

**Struktūrizuota Analizė** - Nuosekliai vertinti. Modelis peržiūri kodą naudodamas fiksuotą sistemą (teisingumas, praktikos, našumas, saugumas, priežiūra). Naudokite kodo peržiūroms ar kokybės įvertinimams.

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

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Pokalbiais:** Paklauskite apie struktūrizuotą analizę:
> - „Kaip pritaikyti analizės sistemą skirtingų tipų kodo peržiūroms?“
> - „Koks geriausias būdas programiškai nuskaityti ir veikti pagal struktūrizuotą išvestį?“
> - „Kaip užtikrinti nuoseklius grėsmingumo lygius skirtingose peržiūrų sesijose?“

<img src="../../../translated_images/lt/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Struktūrizuotos analizės šablonas" width="800"/>

*Sistemos rėmas nuoseklioms kodo peržiūroms su grėsmingumo lygiais*

**Daugiatūris Pokalbis** - Pokalbiams, kuriems reikia konteksto. Modelis prisimena ankstesnes žinutes ir prisitaiko. Naudokite interaktyvioms pagalbos sesijoms ar sudėtingam klausimų-atsakymų srautui.

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

*Kaip pokalbio kontekstas kaupiasi per kelis apsisukimus iki žodžių limito*

**Žingsnis po Žingsnio Mąstymas** - Problemai, kuri reikalauja matomos logikos. Modelis rodo aiškią priežasties grandinę kiekviename žingsnyje. Naudokite matematikos uždaviniams, logikos galvosūkiams ar kai reikia suprasti mąstymo eigą.

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

*Problemos išskaidymas į aiškius loginis žingsnius*

**Apribotas Išvestis** - Atsakymams su tiksliais formato reikalavimais. Modelis griežtai laikosi formato ir ilgio taisyklių. Naudokite santraukoms ar kai reikia tikslios išvesties struktūros.

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

<img src="../../../translated_images/lt/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Apriboto Išvesties Šablonas" width="800"/>

*Griežtas formato, ilgio ir struktūros režimas*

## Esamų Azure Išteklų Naudojimas

**Patikrinkite diegimą:**

Įsitikinkite, kad šakniniame kataloge yra `.env` failas su Azure kredencialais (sukurtas Modulyje 01):
```bash
cat ../.env  # Turėtų parodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Paleiskite programą:**

> **Pastaba:** Jei jau paleidote visas programas naudodami `./start-all.sh` Modulyje 01, šis modulis jau veikia 8083 porte. Galite praleisti paleidimo komandas žemiau ir tiesiogiai eiti į http://localhost:8083.

**1 variantas: Naudojant Spring Boot Dashboard (Rekomenduojama VS Code naudotojams)**

Kūrimo konteineryje yra Spring Boot Dashboard plėtinys, kuris suteikia vizualią valdymo sąsają visoms Spring Boot programoms valdyti. Jį rasite veiklų juostoje kairėje VS Code pusėje (ieškokite Spring Boot ikonos).

Iš Spring Boot Dashboard galite:
- Matyti visas darbo srityje esančias Spring Boot programas
- Vienu paspaudimu paleisti/stabdyti programas
- Realiu laiku žiūrėti programų žurnalus
- Stebėti programos būseną
Tiesiog spustelėkite paleidimo mygtuką šalia „prompt-engineering“, kad pradėtumėte šį modulį, arba paleiskite visus modulius iš karto.

<img src="../../../translated_images/lt/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**2 variantas: naudojant shell skriptus**

Paleiskite visas žiniatinklio programas (modulius 01-04):

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

Abu skriptai automatiškai įkelia aplinkos kintamuosius iš pagrindinio `.env` failo ir sukurs JAR, jei jų nėra.

> **Pastaba:** jei norite rankiniu būdu sukompiliuoti visus modulius prieš paleidimą:
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

## Programos ekrano vaizdai

<img src="../../../translated_images/lt/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Pagrindinė valdymo skydelio sąsaja, rodanti visus 8 aktyvaus užklausų konstravimo modelius su jų savybėmis ir naudojimo atvejais*

## Modelių tyrinėjimas

Žiniatinklio sąsaja leidžia eksperimentuoti su skirtingomis užklausų strategijomis. Kiekvienas modelis sprendžia skirtingas problemas – išbandykite juos, kad suprastumėte, kada kuris metodas geriausiai veikia.

### Mažas prieš didelį entuziazmą

Užduokite paprastą klausimą „Kiek yra 15 % iš 200?“ naudodami mažą entuziazmą. Gaunate greitą ir tiesioginį atsakymą. Dabar užduokite sudėtingą klausimą, pvz., „Sukurkite talpinimo strategiją intensyviai naudojamam API“ su dideliu entuziazmu. Stebėkite, kaip modelis sulėtėja ir pateikia išsamų grindimą. Tas pats modelis, ta pati klausimo struktūra – bet užklausa nurodo, kiek mąstymo atlikti.

<img src="../../../translated_images/lt/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Greitas skaičiavimas su minimaliu grindimu*

<img src="../../../translated_images/lt/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Išsami talpinimo strategija (2.8MB)*

### Užduočių vykdymas (Įrankių įvadai)

Daugiapakopiai darbų srautai naudingiau planuojami iš anksto su eigą aprašančiomis žinutėmis. Modelis aprašo, ką darys, komentuoja kiekvieną žingsnį, o galiausiai apibendrina rezultatus.

<img src="../../../translated_images/lt/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*REST galinio taško kūrimas su žingsnis po žingsnio komentaru (3.9MB)*

### Savianalizuojantis kodas

Išbandykite „Sukurkite el. pašto validacijos paslaugą“. Modelis ne tik sukuria kodą ir sustoja, bet generuoja, įvertina pagal kokybės kriterijus, identifikuoja silpnąsias vietas ir gerina. Matysite, kaip jis iteruoja tol, kol kodas atitinka gamybos standartus.

<img src="../../../translated_images/lt/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Veiksminga el. pašto validavimo paslauga (5.2MB)*

### Struktūruota analizė

Kodo peržiūrai reikalingos nuoseklios vertinimo gairės. Modelis analizuoja kodą naudojant fiksuotas kategorijas (teisingumas, praktikos, našumas, saugumas) su skirtingais griežtumo lygiais.

<img src="../../../translated_images/lt/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Remiantis gairėmis atliekama kodo peržiūra*

### Daugiapakopis pokalbis

Užduokite „Kas yra Spring Boot?“, tada iškart paklauskite „Parodyk man pavyzdį“. Modelis prisimena pirmą klausimą ir pateikia būtent Spring Boot pavyzdį. Be atminties, antras klausimas būtų per bendras.

<img src="../../../translated_images/lt/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Konteksto išsaugojimas tarp klausimų*

### Žingsnis po žingsnio mąstymas

Pasirinkite matematinių uždavinių ir išbandykite tiek žingsnis po žingsnio mąstymo, tiek mažo entuziazmo metodus. Mažas entuziazmas greitai pateikia atsakymą – bet neaiškiai. Žingsnis po žingsnio parodo kiekvieną skaičiavimą ir sprendimą.

<img src="../../../translated_images/lt/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matematinis uždavinys su aiškiais sprendimo žingsniais*

### Apribotas išvestis

Kai reikia specifinių formatų ar žodžių skaičiaus, šis modelis reikalauja griežto laikymosi. Išbandykite sugeneruoti santrauką, kuri turi tiksliai 100 žodžių punktų formatu.

<img src="../../../translated_images/lt/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Mašininio mokymosi santrauka su formato kontrole*

## Ką iš tikrųjų išmokstate

**Mąstymo pastangos keičia viską**

GPT-5.2 leidžia kontroliuoti skaičiavimo pastangas per jūsų užklausas. Mažos pastangos reiškia greitus atsakymus su minimaliu tyrimu. Didelės – kad modelis skiria laiko giliau apmąstyti. Mokotės pritaikyti pastangas užduoties sudėtingumui – neskubėkite paprastų klausimų, bet neverskite priimti skubotų sudėtingų sprendimų.

**Struktūra reguliuoja elgesį**

Pastebėjote XML žymes užklausose? Jos nėra dekoratyvios. Modeliai patikimiau seka struktūruotas instrukcijas nei laisvą tekstą. Kai reikia daugiapakopių procesų ar sudėtingos logikos, struktūra padeda modeliui sekti, kur jis yra ir kas vyksta toliau.

<img src="../../../translated_images/lt/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Gerai struktūruotos užklausos anatomija su aiškiomis dalimis ir XML stiliaus organizacija*

**Kokybė per savarankišką vertinimą**

Savianalizuojantys modeliai veikia aiškiai išdėstant kokybės kriterijus. Vietoje to, kad tikėtumėtės, jog modelis „padarys teisingai“, jūs tiksliai nurodote, ką reiškia „teisingai“: teisinga logika, klaidų valdymas, našumas, saugumas. Tada modelis gali įvertinti savo išvestį ir patobulinti ją. Tai paverčia kodo generavimą ne loterija, o procesu.

**Kontekstas yra ribotas**

Daugiapakopiai pokalbiai veikia įtraukdami pranešimų istoriją į kiekvieną užklausą. Tačiau yra limitas – kiekvienas modelis turi maksimalią žodžių (ženklų) ribą. Augant pokalbiams, reikės strategijų, kaip išlaikyti aktualų kontekstą neviršijant ribos. Šis modulis paaiškina, kaip veikia atmintis; vėliau sužinosite, kada apibendrinti, kada pamiršti, o kada atgauti informaciją.

## Kiti žingsniai

**Kitas modulis:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigacija:** [← Ankstesnis: Modulis 01 – Įžanga](../01-introduction/README.md) | [Atgal į pradžią](../README.md) | [Sekantis: Modulis 03 – RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, atkreipkite dėmesį, kad automatizuotame vertime gali būti klaidų ar netikslumų. Originalus dokumentas gimtąja kalba turi būti laikomas autoritetingu šaltiniu. Kritinei informacijai rekomenduojama naudoti profesionalų vertimą. Mes neatsakome už bet kokius nesusipratimus ar neteisingus interpretavimus, kilusius naudojantis šiuo vertimu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->