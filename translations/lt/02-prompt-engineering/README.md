# 02 modulis: Promptų inžinerija su GPT-5.2

## Turinys

- [Ko sužinosite](../../../02-prompt-engineering)
- [Išankstiniai reikalavimai](../../../02-prompt-engineering)
- [Promptų inžinerijos supratimas](../../../02-prompt-engineering)
- [Promptų inžinerijos pagrindai](../../../02-prompt-engineering)
  - [Nulinio šūvio promptai](../../../02-prompt-engineering)
  - [Kelių pavyzdžių promptai](../../../02-prompt-engineering)
  - [Minties grandinė](../../../02-prompt-engineering)
  - [Pagal vaidmenį kuriami promptai](../../../02-prompt-engineering)
  - [Promptų šablonai](../../../02-prompt-engineering)
- [Pažangūs modeliai](../../../02-prompt-engineering)
- [Esamų „Azure“ išteklių naudojimas](../../../02-prompt-engineering)
- [Programėlės ekrano kopijos](../../../02-prompt-engineering)
- [Modelių tyrinėjimas](../../../02-prompt-engineering)
  - [Mažas vs didelis entuziazmas](../../../02-prompt-engineering)
  - [Užduočių vykdymas (įrankių įvadai)](../../../02-prompt-engineering)
  - [Savianalizės kodas](../../../02-prompt-engineering)
  - [Struktūruota analizė](../../../02-prompt-engineering)
  - [Daugiapakopis pokalbis](../../../02-prompt-engineering)
  - [Žingsnis po žingsnio samprotavimas](../../../02-prompt-engineering)
  - [Apribota išvestis](../../../02-prompt-engineering)
- [Ką iš tiesų mokotės](../../../02-prompt-engineering)
- [Kiti žingsniai](../../../02-prompt-engineering)

## Ko sužinosite

<img src="../../../translated_images/lt/what-youll-learn.c68269ac048503b2.webp" alt="Ko sužinosite" width="800"/>

Antrajame modulyje pamatėte, kaip atmintis leidžia naudoti pokalbio DI ir naudojote GitHub modelius pagrindiniams sąveikavimams. Dabar susitelksime į tai, kaip užduoti klausimus — pačius promptus — naudojant „Azure OpenAI“ GPT-5.2. Kaip struktūrizuojate savo promptus, labai paveikia atsakymų kokybę. Pradedame nuo pagrindinių promptų kūrimo technikų apžvalgos, tada pereiname prie aštuonių pažangių modelių, kurie pilnai išnaudoja GPT-5.2 galimybes.

Naudosime GPT-5.2, nes jis pristato samprotavimų valdymą – galite nurodyti modeliui, kiek mąstyti prieš atsakant. Tai leidžia aiškiau matyti skirtingas promptų strategijas ir suprasti, kada naudoti kurią. Taip pat pasinaudosime Azure mažesniais GPT-5.2 apribojimais, palyginti su GitHub modeliais.

## Išankstiniai reikalavimai

- Užbaigtas 01 modulis (įdiegti „Azure OpenAI“ ištekliai)
- `.env` failas pagrindiniame kataloge su Azure kredencialais (sukurtas modulio 01 metu naudojant `azd up`)

> **Pastaba:** Jei dar neužbaigėte 01 modulio, pirmiausia atlikite jame nurodytą diegimą.

## Promptų inžinerijos supratimas

<img src="../../../translated_images/lt/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Kas yra promptų inžinerija?" width="800"/>

Promptų inžinerija reiškia įvesties teksto kūrimą taip, kad nuosekliai gautumėte reikiamus rezultatus. Tai ne tik klausimų uždavimas – svarbu struktūrizuoti užklausas, kad modelis tiksliai suprastų, ko norite ir kaip tai pateikti.

Galvokite apie tai kaip nurodymų davimą kolegai. „Ištaisyk klaidą“ yra neaišku. „Ištaisyk null pointer klaidą UserService.java 45 eilutėje pridėdamas nulio patikrinimą“ yra konkretu. Kalbos modeliai veikia taip pat – svarbi specifika ir struktūra.

<img src="../../../translated_images/lt/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kaip veikia LangChain4j" width="800"/>

LangChain4j suteikia infrastruktūrą — modelių jungtis, atmintį ir žinučių tipus — o promptų modeliai yra tiesiog kruopščiai struktūruotas tekstas, perduodamas per tą infrastruktūrą. Pagrindiniai elementai – `SystemMessage` (kuris nustato DI elgesį ir vaidmenį) bei `UserMessage` (kuris neša jūsų prašymą).

## Promptų inžinerijos pagrindai

<img src="../../../translated_images/lt/five-patterns-overview.160f35045ffd2a94.webp" alt="Penki pagrindiniai promptų inžinerijos modeliai" width="800"/>

Prieš gilindamiesi į pažangius modelius šiame modulyje, peržiūrėkime penkias pagrindines promptų kūrimo technikas. Tai yra esminiai įrankiai kiekvienam promptų inžinieriui. Jei jau dirbote su [Greitojo starto moduliu](../00-quick-start/README.md#2-prompt-patterns), šias technikas jau matėte – čia conceptualiai išdėstyta jų esmė.

### Nulinio šūvio promptai

Paprastasis metodas: pateikite modeliui tiesioginį nurodymą be pavyzdžių. Modelis visiškai pasikliauja savo mokymu suprasti ir vykdyti užduotį. Tai veikia gerai paprastoms užklausoms, kur elgesys yra akivaizdus.

<img src="../../../translated_images/lt/zero-shot-prompting.7abc24228be84e6c.webp" alt="Nulinio šūvio promptai" width="800"/>

*Tiesioginis nurodymas be pavyzdžių – modelis pats supranta užduotį pagal instrukciją*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Atsakymas: "Teigiamas"
```

**Kada naudoti:** Paprastos klasifikacijos, tiesioginiai klausimai, vertimai arba bet kuri užduotis, kurią modelis gali atlikti be papildomų nurodymų.

### Kelių pavyzdžių promptai

Pateikite pavyzdžius, kurie rodo, kokį modelio elgesį norite matyti. Modelis mokosi iš jūsų pavyzdžių tikėtino įvesties-išvesties formato ir taiko tai naujoms įvestims. Tai žymiai pagerina nuoseklumą užduotyse, kur pageidaujamas formatas ar elgesys nėra aiškus.

<img src="../../../translated_images/lt/few-shot-prompting.9d9eace1da88989a.webp" alt="Kelių pavyzdžių promptai" width="800"/>

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

**Kada naudoti:** Specifinės klasifikacijos, nuoseklus formatavimas, konkrečios srities užduotys arba kai nulinio šūvio rezultatai yra nenuoseklūs.

### Minties grandinė

Prašykite modelio parodyti savo samprotavimą žingsnis po žingsnio. Vietoje tiesaus atsakymo modelis išskaido problemą ir sąmoningai išnagrinėja kiekvieną dalį. Tai pagerina tikslumą matematikos, logikos ir daugiapakopių samprotavimo užduotyse.

<img src="../../../translated_images/lt/chain-of-thought.5cff6630e2657e2a.webp" alt="Minties grandinės promptai" width="800"/>

*Žingsnis po žingsnio samprotavimas – sudėtingų problemų suskaidymas į aiškius loginius žingsnius*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Modelis rodo: 15 - 8 = 7, tada 7 + 12 = 19 obuolių
```

**Kada naudoti:** Matematikos užduotys, loginiai galvosūkiai, derinimas arba bet kuri užduotis, kurioje matomas samprotavimo procesas gerina tikslumą ir pasitikėjimą.

### Pagal vaidmenį kuriami promptai

Nustatykite DI personažą ar vaidmenį prieš užduodami klausimą. Tai suteikia kontekstą, kuris formuoja atsakymo toną, gylį ir fokusuotumą. „Programinės įrangos architektas“ duoda kitokias rekomendacijas nei „jaunesnysis programuotojas“ ar „saugumo auditorius“.

<img src="../../../translated_images/lt/role-based-prompting.a806e1a73de6e3a4.webp" alt="Pagal vaidmenį kuriami promptai" width="800"/>

*Konteksto ir personažo nustatymas – tas pats klausimas gauna skirtingą atsakymą priklausomai nuo priskirto vaidmens*

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

**Kada naudoti:** Kodo peržiūros, mokymas, specifinės srities analizė arba kai reikia atsakymų, pritaikytų konkrečios patirties lygiui ar požiūriui.

### Promptų šablonai

Sukurkite pakartotinai naudojamus promptus su kintamaisiais vietos laikikliais. Vietoje kiekvieną kartą rašyti naują promptą, apibrėžkite šabloną vieną kartą ir pildykite skirtingomis reikšmėmis. LangChain4j `PromptTemplate` klasė leidžia tai daryti su `{{variable}}` sintakse.

<img src="../../../translated_images/lt/prompt-templates.14bfc37d45f1a933.webp" alt="Promptų šablonai" width="800"/>

*Pakartotiniai promptai su kintamaisiais – vienas šablonas, daug panaudojimų*

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

**Kada naudoti:** Pasikartojančios užklausos su skirtingomis įvestimis, masinis apdorojimas, pereinamų DI darbo srautų kūrimas arba bet kuri situacija, kur prompto struktūra išlieka nesikeičianti, o duomenys keičiasi.

---

Šie penki pagrindai suteikia tvirtą įrankių rinkinį daugumai promptų užduočių. Toliau šiame modulyje šie pagrindai plėtojami su **aštuoniais pažangiais modeliais**, kurie išnaudoja GPT-5.2 samprotavimo valdymą, savianalizę ir struktūruotą išvestį.

## Pažangūs modeliai

Įsisavinus pagrindus, pereikime prie aštuonių pažangių modelių, dėl kurių šis modulis išsiskiria. Ne visoms problemoms tinka tas pats metodas. Kai kuriems klausimams reikia greitų atsakymų, kitiems – gilaus mąstymo. Kai kurie reikalauja matomo samprotavimo, kiti – tik rezultatų. Žemiau pateikti modeliai optimizuoti skirtingoms situacijoms – ir GPT-5.2 samprotavimo valdymas akivaizdžiai pabrėžia skirtumus.

<img src="../../../translated_images/lt/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Aštuoni promptų inžinerijos modeliai" width="800"/>

*Aštuoni promptų inžinerijos modeliai ir jų panaudojimo atvejai*

<img src="../../../translated_images/lt/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Samprotavimo valdymas su GPT-5.2" width="800"/>

*GPT-5.2 samprotavimo valdymas leidžia nurodyti, kiek modelis turi mąstyti – nuo greitų tiesioginių atsakymų iki gilios analizės*

**Mažas entuziazmas (Greiti ir fokusuoti)** – paprastiems klausimams, kai norite greitų tiesioginių atsakymų. Modelis daro minimaliai samprotavimų – daugiausia 2 žingsnius. Naudokite skaičiavimams, paieškoms ar paprastiems klausimams.

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
> - „Kuo skiriasi mažo ir didelio entuziazmo promptų modeliai?“
> - „Kaip XML žymės promptuose padeda struktūrizuoti DI atsakymą?“
> - „Kada naudoti savirefleksijos modelius, o kada tiesioginius nurodymus?“

**Didelis entuziazmas (Gilus ir kruopštus)** – sudėtingoms problemoms, kai norite išsamios analizės. Modelis kruopščiai nagrinėja ir demonstruoja detalias samprotavimų grandines. Naudokite sistemų projektui, architektūros sprendimams ar sudėtingiems tyrimams.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Užduočių vykdymas (Žingsnis po žingsnio eiga)** – daugiapakopiams darbo srautams. Modelis pateikia pradines gaires, komentuoja kiekvieną žingsnį vykdymo metu, o pabaigoje pateikia santrauką. Naudokite migracijoms, įgyvendinimams ar bet kuriam procesui su keliomis stadijomis.

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

Minties grandinės promptai aiškiai prašo modelio parodyti samprotavimų procesą, kas padidina tikslumą sudėtingose užduotyse. Žingsnių išskaidymas padeda tiek žmonėms, tiek DI suprasti logiką.

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) pokalbiu:** Paklauskite apie šį modelį:
> - „Kaip pritaikyti užduoties vykdymo modelį ilgai trunkančioms operacijoms?“
> - „Kokios gerosios praktikos yra dėl įrankių įvadų struktūrizavimo gamybinėse programėlėse?“
> - „Kaip sugauti ir parodyti tarpinius pažangos atnaujinimus naudotojo sąsajoje?“

<img src="../../../translated_images/lt/task-execution-pattern.9da3967750ab5c1e.webp" alt="Užduočių vykdymo modelis" width="800"/>

*Planavimas → Vykdymas → Santrauka daugiapakopėms užduotims*

**Savianalizės kodas** – gamybinės kokybės kodo generavimui. Modelis kuria kodą pagal gamybinės kokybės standartus su tinkamu klaidų valdymu. Naudokite naujų funkcijų ar servisų kūrimui.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/lt/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Savianalizės ciklas" width="800"/>

*Iteracinis tobulinimo ciklas – generuoti, vertinti, rasti trūkumus, tobulinti, kartoti*

**Struktūruota analizė** – nuosekliai vertinant. Modelis peržiūri kodą naudodamas fiksuotą sistemą (teisingumas, praktikos, našumas, saugumas, palaikymas). Naudokite kodo peržiūroms ar kokybės vertinimui.

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
> - „Kaip pritaikyti analizės sistemą skirtingoms kodo peržiūrų rūšims?“
> - „Koks geriausias būdas programiškai apdoroti struktūruotą išvestį?“
> - „Kaip užtikrinti nuoseklius sunkumo lygius skirtingose peržiūrų sesijose?“

<img src="../../../translated_images/lt/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Struktūruotos analizės modelis" width="800"/>

*Sistema nuoseklioms kodo peržiūroms su sunkumo laipsniais*

**Daugiapakopis pokalbis** – pokalbiams, kuriems reikalingas kontekstas. Modelis prisimena ankstesnes žinutes ir juo remiasi. Naudokite interaktyvioms pagalbos sesijoms ar sudėtingoms klausimų-atsakymų sesijoms.

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

*Kaip pokalbių kontekstas kaupiasi per kelis ciklus, kol pasiekiamas žetonų limitas*

**Žingsnis po žingsnio samprotavimas** – problemoms, kurios reikalauja matomos logikos. Modelis parodo aiškų kiekvieno žingsnio samprotavimą. Naudokite matematikos užduotims, loginėms mįslėms arba kai norite suprasti mąstymo procesą.

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

*Problemų suskaidymas į aiškius loginius žingsnius*

**Apribota išvestis** – atsakymams, kuriems taikomi specifiniai formato reikalavimai. Modelis griežtai laikosi formato ir ilgio taisyklių. Naudokite santraukoms arba kai reikia tikslios struktūros.

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

<img src="../../../translated_images/lt/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Apribotos išvesties modelis" width="800"/>

*Specifinių formato, ilgio ir struktūros reikalavimų užtikrinimas*

## Esamų „Azure“ išteklių naudojimas

**Patikrinkite diegimą:**

Įsitikinkite, kad `.env` failas yra pagrindiniame kataloge su Azure kredencialais (sukurtas modulio 01 metu):
```bash
cat ../.env  # Turėtų parodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Paleiskite programą:**

> **Pastaba:** Jei jau paleidote visas programėles naudodami `./start-all.sh` iš modulio 01, šis modulis jau veikia 8083 porte. Galite praleisti toliau pateiktas paleidimo komandas ir eiti tiesiai į http://localhost:8083.

**1 variantas: naudokite „Spring Boot Dashboard“ (rekomenduojama VS Code naudotojams)**

Dev konteineryje įdiegta „Spring Boot Dashboard“ plėtinys, kuris suteikia vizualią sąsają valdyti visas Spring Boot programėles. Jį rasite veiklos juostoje kairėje VS Code pusėje (pažymėtas Spring Boot piktograma).

Iš „Spring Boot Dashboard“ galite:
- Matyti visas prieinamas Spring Boot programėles darbinėje aplinkoje
- Pradėti/stabdyti programėles vienu paspaudimu
- Realizuotai matyti programėlių žurnalus
- Stebėti programėlių būseną
Tiesiog spustelėkite paleidimo mygtuką šalia „prompt-engineering“, kad paleistumėte šį modulį, arba paleiskite visus modulius iš karto.

<img src="../../../translated_images/lt/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot pagrindinis ekranas" width="400"/>

**2 variantas: naudojant shell skriptus**

Paleiskite visas žiniatinklio programas (1-4 moduliai):

**Bash:**
```bash
cd ..  # Iš šakninių katalogų
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iš šakninių direktorijų
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

Abu skriptai automatiškai užkrauna aplinkos kintamuosius iš .env failo šakniniame kataloge ir sukuria JAR failus, jei jų nėra.

> **Pastaba:** Jei norite rankiniu būdu sukompiliuoti visus modulius prieš paleidžiant:
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

Ataikykite adresą http://localhost:8083 savo naršyklėje.

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

<img src="../../../translated_images/lt/dashboard-home.5444dbda4bc1f79d.webp" alt="Pagrindinis valdymo skydelis" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Pagrindinis valdymo skydelis, rodantis visus 8 promptų inžinerijos modelius su jų savybėmis ir naudojimo atvejais*

## Tyrinėjame modelius

Žiniatinklio sąsaja leidžia eksperimentuoti su įvairiomis paskatinimo strategijomis. Kiekvienas modelis sprendžia skirtingas problemas – išbandykite juos, kad pamatytumėte, kada kiekvienas metodas veikia geriausiai.

> **Pastaba: Srautinė ir nesrautinė eiga** — Kiekviename modelio puslapyje yra du mygtukai: **🔴 Srautinis atsakymas (gyvai)** ir **Nesrautinė** opcija. Srautinė eiga naudoja Server-Sent Events (SSE), kad parodytų žodžius realiu laiku modelio generavimo metu, tad iš karto matote eigą. Nesrautinė opcija laukia viso atsakymo, kol jį parodo. Sudėtingiems užklausoms (pvz., Didelis noras, Savirefleksinis kodas) nesrautinis kvietimas gali užtrukti labai ilgai – kartais net minutes – be jokios matomos reakcijos. **Naudokite srautinį režimą, kai eksperimentuojate su sudėtingais prašymais**, kad matytumėte, kaip modelis dirba, ir išvengtumėte įspūdžio, jog užklausa užstrigo.
>
> **Pastaba: Naršyklės reikalavimai** — Srautinė funkcija naudoja Fetch Streams API (`response.body.getReader()`), kurią palaiko tik pilnos naršyklės (Chrome, Edge, Firefox, Safari). Ji **neveikia** VS Code integruotoje Simple Browser, nes jo webview nepalaiko ReadableStream API. Jei naudojate Simple Browser, nesrautiniai mygtukai veiks, o srautiniai neveiks. Pilnai patirčiai atidarykite `http://localhost:8083` išorinėje naršyklėje.

### Mažas ir didelis noras (Low vs High Eagerness)

Užduokite paprastą klausimą „Kiek yra 15 % nuo 200?“ naudodami Mažą norą. Gaunate greitą, tiesioginį atsakymą. Dabar užduokite sudėtingą klausimą „Sukurkite kešavimo strategiją itin apkrautam API“ su Dideliu noru. Spustelėkite **🔴 Srautinis atsakymas (gyvai)** ir stebėkite išsamų modelio samprotavimą žodis po žodžio. Tas pats modelis, ta pati klausimo struktūra – bet užklausa nurodo, kiek giliai galvoti.

### Užduočių vykdymas (Įrankių įvadas)

Daugiapakopiai darbų srautai naudoja išankstinį planavimą ir progresijos aprašymą. Modelis nusako, ką darys, komentuoja kiekvieną žingsnį ir vėliau apibendrina rezultatus.

### Savirefleksinis kodas

Išbandykite „Sukurti el. pašto validavimo paslaugą“. Vietoj to, kad tiesiog sugeneruotų kodą ir sustotų, modelis generuoja, vertina pagal kokybės kriterijus, aptinka silpnas vietas ir tobulina. Matysite, kaip jis iteruoja, kol kodas atitinka gamybos standartus.

### Struktūruota analizė

Kodo peržiūroms reikalingi nuoseklūs vertinimo pagrindai. Modelis analizuoja kodą pagal nustatytas kategorijas (teisingumas, praktikos, našumas, saugumas) su rimtumo lygiais.

### Daugiakartinis pokalbis

Paklauskite „Kas yra Spring Boot?“ ir iš karto pridėkite „Parodyk man pavyzdį“. Modelis prisimena pirmą klausimą ir pateikia specialų Spring Boot pavyzdį. Be atminties antras klausimas būtų per neaiškus.

### Žingsnis po žingsnio samprotavimas

Pasirinkite matematikos uždavinį ir išbandykite jį tiek su Žingsnis po žingsnio samprotavimu, tiek su Mažu noru. Mažas noras pateikia tik atsakymą – greitai, bet neaiškiai. Žingsnis po žingsnio parodo kiekvieną skaičiavimą ir sprendimą.

### Apribotas išvesties formatas

Kai reikia specifinių formatų ar žodžių skaičiaus, šis modelis užtikrina griežtą laikymąsi. Išbandykite sukurti santrauką tiksliai iš 100 žodžių punktų formatu.

## Ko iš tikrųjų mokotės

**Samprotavimo pastangos keičia viską**

GPT-5.2 leidžia valdyti skaičiavimo pastangas per savo užklausas. Mažos pastangos reiškia greitą atsakymą su minimalia paieška. Didelės pastangos – gilų, apgalvotą atsakymą. Mokotės atitikti pastangas užduoties sudėtingumui – nešvaistykite laiko paprastiems klausimams, bet neskubėkite sudėtingų sprendimų.

**Struktūra nukreipia elgesį**

Pastebėjote XML žymes užklausoje? Jos nėra dekoratyvios. Modeliai patikimiau atlieka struktūrizuotas instrukcijas nei laisvo teksto. Kai reikia daugiapakopių procesų ar sudėtingos logikos, struktūra padeda modeliui sekti, kur jis yra ir kas laukia.

<img src="../../../translated_images/lt/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt struktūra" width="800"/>

*Gerai struktūrizuoto užklausos anatomija su aiškiomis sekcijomis ir XML stiliaus organizacija*

**Kokybė per savęs vertinimą**

Savirefleksiniai modeliai veikia išreikšdami kokybės kriterijus. Vietoj to, kad tikėtumėtės, jog modelis „atliks gerai“, tiksliai nurodote, ką reiškia „gerai“: teisinga logika, klaidų valdymas, našumas, saugumas. Tada modelis gali įvertinti savo išvestį ir tobulinti. Tai paverčia kodo generavimą procesu, o ne loterija.

**Kontekstas yra ribotas**

Daugiakartiniai pokalbiai veikia perduodant žinučių istoriją kiekviename užklausoje. Tačiau yra riba – kiekvienas modelis turi maksimalų tokenų skaičių. Didėjant pokalbiams reikalingos strategijos, leidžiančios išlaikyti svarbią informaciją, nepasiekiant ribos. Šis modulis parodo, kaip veikia atmintis; vėliau sužinosite, kada apibendrinti, kada pamiršti ir kada atsigauti informaciją.

## Sekantys žingsniai

**Kitas modulis:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigacija:** [← Ankstesnis: Modulis 01 - Įvadas](../01-introduction/README.md) | [Atgal į pagrindinį](../README.md) | [Kitas: Modulis 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors stengiamės užtikrinti tikslumą, prašome atkreipti dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Pirminis dokumentas originalo kalba laikomas pagrindiniu šaltiniu. Svarbiai informacijai rekomenduojama naudoti profesionalų žmogaus vertimą. Mes neatsakome už jokius nesusipratimus ar neteisingus interpretavimus, kilusius dėl šio vertimo naudojimo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->