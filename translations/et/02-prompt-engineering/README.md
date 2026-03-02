# Moodul 02: Päringu inseneriteadus GPT-5.2-ga

## Sisukord

- [Video juhend](../../../02-prompt-engineering)
- [Mida sa õpid](../../../02-prompt-engineering)
- [Eeltingimused](../../../02-prompt-engineering)
- [Mõistmine, mis on päringu inseneriteadus](../../../02-prompt-engineering)
- [Päringu inseneriteaduse alused](../../../02-prompt-engineering)
  - [Nullnäitega päring](../../../02-prompt-engineering)
  - [Mõne näitega päring](../../../02-prompt-engineering)
  - [Mõttekäigu ahel](../../../02-prompt-engineering)
  - [Rollipõhine päring](../../../02-prompt-engineering)
  - [Päringumallid](../../../02-prompt-engineering)
- [Täiustatud mustrid](../../../02-prompt-engineering)
- [Rakenduse käivitamine](../../../02-prompt-engineering)
- [Rakenduse ekraanipildid](../../../02-prompt-engineering)
- [Mustrid lahti seletatuna](../../../02-prompt-engineering)
  - [Madal vs kõrge innukus](../../../02-prompt-engineering)
  - [Ülesande täitmine (tööriista sissejuhatused)](../../../02-prompt-engineering)
  - [Enda peegeldav kood](../../../02-prompt-engineering)
  - [Struktureeritud analüüs](../../../02-prompt-engineering)
  - [Mitme vooru vestlus](../../../02-prompt-engineering)
  - [Samm-sammult mõtlemine](../../../02-prompt-engineering)
  - [Piiratud väljund](../../../02-prompt-engineering)
- [Mida sa tegelikult õpid](../../../02-prompt-engineering)
- [Järgmised sammud](../../../02-prompt-engineering)

## Video juhend

Vaata seda otseülekannet, mis selgitab, kuidas selle mooduliga alustada:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Päringu inseneriteadus LangChain4j-ga – otseülekanne" width="800"/></a>

## Mida sa õpid

Järgnev diagramm annab ülevaate peamistest teemadest ja oskustest, mida selles moodulis arendad — alates päringu täpsustamise tehnikatest kuni samm-sammulise töövooni, mida järgid.

<img src="../../../translated_images/et/what-youll-learn.c68269ac048503b2.webp" alt="Mida sa õpid" width="800"/>

Eelmistes moodulites uurisid LangChain4j põhilisi käitumisi GitHubi mudelitega ja nägid, kuidas mälu võimaldab vestluslikku tehisintellekti Azure OpenAI abil. Nüüd keskendume sellele, kuidas sa küsimusi esitad — päringutele endile — kasutades Azure OpenAI GPT-5.2. Viis, kuidas sa oma päringud struktureerid, mõjutab oluliselt vastuste kvaliteeti. Alustame põhitehnikate ülevaatega ja liigume edasi kaheksasse täiustatud mustrisse, mis kasutavad täielikult GPT-5.2 võimeid.

Kasutame GPT-5.2, sest see lisab mõtlemise juhtimise - saad mudelile öelda, kui palju ta peaks enne vastamist mõtlema. See muudab erinevad päringustrateegiad selgemaks ja aitab mõista, millal iga lähenemist kasutada. Samuti on GPT-5.2-l Azure keskkonnas vähem kiiruspiiranguid võrreldes GitHubi mudelitega.

## Eeltingimused

- Lõpetatud Moodul 01 (Azure OpenAI ressursid juurutatud)
- Juurekataloogis `.env` fail koos Azure mandaatidega (tehtud `azd up` käsklusega Moodul 01-s)

> **Märkus:** Kui sa pole Moodulit 01 lõpetanud, järgi sealset juurutusjuhendit esmalt.

## Mõistmine, mis on päringu inseneriteadus

Päringu inseneriteadus seisneb olemuselt erinevuses ebaselgete ja täpsete juhiste vahel, nagu allolev võrdlus näitab.

<img src="../../../translated_images/et/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Mis on päringu inseneriteadus?" width="800"/>

Päringu inseneriteadus tähendab sisendi teksti disainimist nii, et see annab järjepidevalt soovitud tulemuse. See ei seisne ainult küsimuste esitamises — vaid päringute struktuuris, nii et mudel mõistab täpselt, mida sa soovid ja kuidas seda toimetada.

Mõtle sellele kui kolleegile juhiste andmisele. „Paranda viga“ on ebamäärane. „Paranda nullviite erind UserService.java faili 45. real, lisades nullkontrolli“ on konkreetne. Keelemudelid töötavad samal viisil — täpsus ja struktuur on olulised.

Järgnev diagramm näitab, kuidas LangChain4j seda pilti täiendab — ühendades su päringumustrid mudeliga läbi SystemMessage ja UserMessage ehituskivide.

<img src="../../../translated_images/et/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kuidas LangChain4j sobitub" width="800"/>

LangChain4j pakub infrastruktuuri — mudeliühendused, mälu ja sõnumite tüübid — samas kui päringumustrid on lihtsalt hoolikalt struktureeritud tekst, mida sa selle infrastruktuuri kaudu saadad. Peamised ehituskivid on `SystemMessage` (mis määrab tehisintellekti käitumise ja rolli) ning `UserMessage` (mis kannab sinu tegelikku päringut).

## Päringu inseneriteaduse alused

Viis põhilist tehnikat, mis allpool näidatud, moodustavad tõhusa päringu inseneriteaduse vundamendi. Igaüks neist käsitleb erinevat aspekti, kuidas keelega mudelitega suhelda.

<img src="../../../translated_images/et/five-patterns-overview.160f35045ffd2a94.webp" alt="Viie päringu insenerteaduse mustri ülevaade" width="800"/>

Enne kui sukelduda selle mooduli täiustatud mustritesse, vaatleme üle viis alustehnikat. Need on ehituskivid, mida iga päringu insener peaks teadma. Kui oled juba läbinud [Kiirstardi mooduli](../00-quick-start/README.md#2-prompt-patterns), oled neid juba näinud — siin on nende kontseptuaalne raamistik.

### Nullnäitega päring

Lihtsaim lähenemine: anna mudelile otsene juhis ilma näideteta. Mudel tugineb üksnes oma treeningandmetele, et mõista ja täita ülesanne. See toimib hästi lihtsate päringute puhul, kus oodatav käitumine on ilmne.

<img src="../../../translated_images/et/zero-shot-prompting.7abc24228be84e6c.webp" alt="Nullnäitega päring" width="800"/>

*Otsene juhis ilma näideteta — mudel tuletab ülesande ainult juhisest*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Vastus: "Positiivne"
```

**Millal kasutada:** Lihtsad klassifikatsioonid, otsesed küsimused, tõlked või igasugune ülesanne, mida mudel suudab täita ilma täiendava juhiseta.

### Mõne näitega päring

Anna näited, mis demonstreerivad mustrit, mida mudel peaks järgima. Mudel õpib näidetest sisendi-väljundi formaadi ja rakendab seda uutele sisenditele. See parandab järjepidevust ülesannetes, kus soovitud formaat või käitumine pole ilmselge.

<img src="../../../translated_images/et/few-shot-prompting.9d9eace1da88989a.webp" alt="Mõne näitega päring" width="800"/>

*Näidetest õppimine — mudel tuvastab mustri ja rakendab seda uutele sisenditele*

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

**Millal kasutada:** Kohandatud klassifikatsioonid, järjepidev vormindus, domeenispetsiifilised ülesanded või olukorrad, kus nullnäitega tulemused on ebajärjekindlad.

### Mõttekäigu ahel

Paluge mudelil näidata oma mõtlemist samm-sammult. Selle asemel, et kohe vastusele jõuda, lahendab mudel probleemi ja töötab läbi iga osa selgelt. See parandab täpsust matemaatika, loogika ja mitmeastmeliste ülesannete puhul.

<img src="../../../translated_images/et/chain-of-thought.5cff6630e2657e2a.webp" alt="Mõttekäigu ahela päring" width="800"/>

*Samm-sammuline mõtlemine — keeruliste probleemide jagamine loogilisteks sammudeks*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Mudel näitab: 15 - 8 = 7, siis 7 + 12 = 19 õuna
```

**Millal kasutada:** Matemaatikaprobleemid, loogikapusled, silumine või igasugune ülesanne, kus mõtlemisprotsessi näitamine parandab täpsust ja usaldusväärsust.

### Rollipõhine päring

Sea AI-le persona või roll enne küsimust. See annab konteksti, mis määrab vastuse tooni, sügavuse ja fookuse. „Tarkvaraarhitekt“ annab erinevat nõu kui „noorem arendaja“ või „turbeauditor“.

<img src="../../../translated_images/et/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rollipõhine päring" width="800"/>

*Konteksti ja persona kehtestamine — sama küsimus saab erineva vastuse vastavalt määratud rollile*

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

**Millal kasutada:** Koodiülevaated, juhendamine, domeenispetsiifiline analüüs või kui vajad vastuseid, mis on kohandatud konkreetse pädevustaseme või vaatenurga järgi.

### Päringumallid

Loo taaskasutatavad päringud muutujate kohatäitjatega. Selle asemel, et iga kord uut päringut kirjutada, defineeri mall üks kord ja täida erinevad väärtused. LangChain4j `PromptTemplate` klass teeb selle lihtsaks `{{muutuja}}` süntaksiga.

<img src="../../../translated_images/et/prompt-templates.14bfc37d45f1a933.webp" alt="Päringumallid" width="800"/>

*Taaskasutatavad päringud muutujate kohatäitjatega — üks mall, palju kasutuskordi*

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

**Millal kasutada:** Korduvad päringud erinevate sisenditega, partiitöötlus, taaskasutatavate AI töövoogude loomine või igasugune olukord, kus päringu struktuur jääb samaks, kuid andmed muutuvad.

---

Need viis alusteadust annavad sulle tugeva tööriistakomplekti enamikele päringutöödele. Ülejäänud moodul ehitab nendele põhinevalt kaheksale täiustatud mustrile, mis kasutavad GPT-5.2 mõtlemise juhtimist, enesehinnangut ja struktureeritud väljundi võimeid.

## Täiustatud mustrid

Pärast alustehnikate katmist liigume kaheksasse täiustatud mustrisse, mis teevad selle mooduli unikaalseks. Mitte kõik probleemid ei vaja sama lähenemist. Mõned küsimused vajavad kiireid vastuseid, teised põhjalikku mõtlemist. Mõned vajavad nähtavat järeldust, teised vajavad lihtsalt tulemusi. Iga alljärgnev muster on optimeeritud erineva stsenaariumi jaoks — ja GPT-5.2 mõtlemise juhtimine teeb need erinevused veelgi selgemaks.

<img src="../../../translated_images/et/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Kaheksa päringu insenerteaduse mustrit" width="800"/>

*Kaheksa päringu insenerteaduse mustri ülevaade ja nende kasutusjuhud*

GPT-5.2 lisab neile mustritele veel ühe mõõtme: *mõtlemise juhtimise*. Allolev liugur näitab, kuidas saad mudeli mõtlemise pingutust reguleerida — alates kiiretest otsestest vastustest kuni sügava ja põhjaliku analüüsini.

<img src="../../../translated_images/et/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Mõtlemise juhtimine GPT-5.2-ga" width="800"/>

*GPT-5.2 mõtlemise juhtimine võimaldab määrata, kui palju mudel peaks mõtlema — alates kiiretest otsestest vastustest kuni sügava uurimiseni*

**Madal innukus (kiire ja fokusseeritud)** – lihtsate küsimuste jaoks, kus vajad kiireid ja otseseid vastuseid. Mudel teeb minimaalset mõtlemist – maksimaalselt 2 sammu. Kasuta seda arvutusteks, otsinguteks või lihtsateks küsimusteks.

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

> 💡 **Uuri GitHub Copilotiga:** Ava [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ja küsi:
> - "Mis vahe on madala ja kõrge innukusega päringu mustritel?"
> - "Kuidas XML sildid päringutes aitavad AI vastust struktureerida?"
> - "Millal peaksin kasutama enesepeegeldamise mustreid ja millal otseseid juhiseid?"

**Kõrge innukus (põhjalik ja põhjalik)** – keerukate probleemide jaoks, kus vajad laiaulatuslikku analüüsi. Mudel uurib põhjalikult ja näitab detailset mõtlemist. Kasuta seda süsteemide disaini, arhitektuuriotsuste või keeruka uurimuse jaoks.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Ülesande täitmine (samm-sammuline edenemine)** – mitmeastmeliste töövoogude jaoks. Mudel annab alguses plaani, kirjeldab iga toimingut töö käigus ja annab seejärel kokkuvõtte. Kasuta seda migratsioonide, juurutuste või mistahes mitmeastmelise protsessi jaoks.

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

Mõttekäigu ahela päring palub mudelil selgelt näidata järeldusprotsessi, parandades keerukate ülesannete täpsust. Samm-sammuline jaotus aitab nii inimestel kui tehisintellektil loogikat mõista.

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chat'iga:** Küsi selle mustri kohta:
> - "Kuidas kohandada ülesande täitmise mustrit pikkadeks operatsioonideks?"
> - "Millised on parimad tavad tööriista sissejuhatuste struktuuri jaoks tootmises?"
> - "Kuidas püüda ja kuvada vahepealseid edenemisteateid kasutajaliideses?"

Järgnev diagramm illustreerib seda Plaani → Täida → Kokkuvõtte töövoogu.

<img src="../../../translated_images/et/task-execution-pattern.9da3967750ab5c1e.webp" alt="Ülesande täitmise muster" width="800"/>

*Plaani → Täida → Kokkuvõtte töövoog mitmeastmeliste ülesannete jaoks*

**Enda peegeldav kood** – tootmiskvaliteediga koodi genereerimiseks. Mudel genereerib koodi, järgides tootmistandardeid ja korralikku veahaldust. Kasuta seda uute funktsioonide või teenuste loomisel.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

Allolev diagramm näitab iteratiivset täiustamisringi — genereeri, hinda, tuvasta nõrkused ja täiusta, kuni kood vastab tootmisnõuetele.

<img src="../../../translated_images/et/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Enesepeegeldamise tsükkel" width="800"/>

*Iteratiivne täiustamisring – genereeri, hinda, tuvastamisprobleemid, paranda, korda*

**Struktureeritud analüüs** – järjepidevaks hindamiseks. Mudel vaatab koodi üle fikseeritud raamistikus (õigsus, tavad, jõudlus, turvalisus, hooldatavus). Kasuta seda koodiülevaadete või kvaliteedi hindamise puhul.

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

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chat'iga:** Küsi struktureeritud analüüsi kohta:
> - "Kuidas kohandada analüüsiraamistikku erinevate tüüpi koodiülevaadete jaoks?"
> - "Mis on parim viis struktureeritud väljundi lugemiseks ja tegevuse võtmiseks programmeeritult?"
> - "Kuidas tagada järjepidev raskusastmete tase erinevate ülevaatesessioonide vahel?"

Järgmine diagramm näitab, kuidas see struktureeritud raamistik korraldab koodiülevaate järjekindlatesse kategooriatesse raskusastmega.

<img src="../../../translated_images/et/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Struktureeritud analüüsi muster" width="800"/>

*Järjepidevate koodiülevaadete raamistik raskusastmetega*

**Mitme vooru vestlus** – vestluste jaoks, mis vajavad konteksti. Mudel mäletab varasemaid sõnumeid ja arendab neid edasi. Kasuta seda interaktiivsetes abisessioonides või keerukates Q&A-stseenides.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

Järgmine diagramm visualiseerib, kuidas vestluse kontekst akumuleerub iga vooruga ja kuidas see on seotud mudeli tokeni piiranguga.

<img src="../../../translated_images/et/context-memory.dff30ad9fa78832a.webp" alt="Vestluse konteksti mälu" width="800"/>

*Kuidas vestluse kontekst akumuleerub mitme vooru jooksul kuni tokeni piirini*
**Samm-sammult põhjendus** – Probleemide puhul, mis vajavad nähtavat loogikat. Mudel näitab iga sammu jaoks selget põhjendust. Kasuta seda matemaatikaülesannete, loogikapuslede või siis, kui vajad arusaamist mõtlemisprotsessist.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

Järgmine joonis illustreerib, kuidas mudel jaotab probleeme selgeteks, nummerdatud loogilisteks sammudeks.

<img src="../../../translated_images/et/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Samm-sammult muster" width="800"/>

*Probleemide jaotamine selgeteks loogilisteks sammudeks*

**Piiratud väljund** – Vastuste puhul, millel on konkreetse formaadi nõuded. Mudel järgib rangelt formaadi ja pikkuse reegleid. Kasuta seda kokkuvõtete tegemiseks või kui vajad täpset väljundi struktuuri.

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

Järgmine joonis näitab, kuidas piirangud juhivad mudelit genereerima väljundit, mis striktse järgib sinu formaadi ja pikkuse nõudeid.

<img src="../../../translated_images/et/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Piiratud väljundi muster" width="800"/>

*Konkreetsed formaadi, pikkuse ja struktuuri nõuded*

## Käivita rakendus

**Kontrolli juurutust:**

Veendu, et `.env` fail eksisteerib juurkataloogis koos Azure'i volitustega (loodud moodulis 01). Käivita see mooduli kataloogist (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # Peaks näitama AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Peaks kuvama AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Alusta rakendust:**

> **Märkus:** Kui oled juba kõik rakendused käivitanud käsuga `./start-all.sh` juurkataloogist (nagu kirjeldatud moodulis 01), siis see moodul töötab juba pordil 8083. Võid järgmised käivituskäsud vahele jätta ja minna otse aadressile http://localhost:8083.

**Valik 1: Kasutades Spring Boot Dashboard’i (Soovitatav VS Code kasutajatele)**

Arendus konteiner sisaldab Spring Boot Dashboard laiendust, mis pakub visuaalset kasutajaliidest kõigi Spring Boot rakenduste haldamiseks. Leiad selle VS Code vasaku külje Activity Bar’ist (otsa otsi Spring Boot ikooni).

Spring Boot Dashboard’ist saad:
- Näha kõiki tööruumis saadaolevaid Spring Boot rakendusi
- Alustada/peatada rakendusi ühe klikiga
- Vaadata rakenduste logisid reaalajas
- Jälgida rakenduse olekut

Lihtsalt klõpsa mängunupule "prompt-engineering" kõrval, et alustada seda moodulit või käivita korraga kõik moodulid.

<img src="../../../translated_images/et/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard VS Code’is — alusta, peata ja jälgi kõiki mooduleid ühest kohast*

**Valik 2: Kasutades shell skripte**

Käivita kõik veebirakendused (moodulid 01–04):

**Bash:**
```bash
cd ..  # Juurekataloogist
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Juure kataloogist
.\start-all.ps1
```

Või alusta ainult seda moodulit:

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

Mõlemad skriptid laadivad automaatselt keskkonnamuutujad juurest `.env` failist ja koostavad JAR-failid, kui neid pole olemas.

> **Märkus:** Kui soovid kõik moodulid käsitsi enne käivitamist kompileerida:
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

Ava oma brauseris aadress http://localhost:8083.

**Peatamiseks:**

**Bash:**
```bash
./stop.sh  # Ainult see moodul
# Või
cd .. && ./stop-all.sh  # Kõik moodulid
```

**PowerShell:**
```powershell
.\stop.ps1  # Ainult see moodul
# Või
cd ..; .\stop-all.ps1  # Kõik moodulid
```

## Rakenduse ekraanipildid

Siin on prompt engineering mooduli põhiliides, kus saad katsetada kõiki kaheksat mustrit kõrvuti.

<img src="../../../translated_images/et/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Avaleht" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Põhidiagramm, mis kuvab kõiki 8 prompt engineering mustrit nende omaduste ja kasutusjuhtumitega*

## Musterite uurimine

Veebiliides võimaldab sul katsetada erinevaid promptimise strateegiaid. Iga muster lahendab erinevaid probleeme – proovi neid, et näha, millal milline lähenemine töötab kõige paremini.

> **Märkus: Voogedastus vs Mittevoogedastus** — Iga mustri lehel on kaks nuppu: **🔴 Voogvastus (Live)** ja **Mittevoogedastus** valik. Voogedastus kasutab Server-Sent Events (SSE), et kuvada mudelei genereeritavaid silte reaalajas, nii näed edenemist kohe. Mittevoogedastus ootab kogu vastust ära enne kuvamist. Sügava põhjenduse korral (nt High Eagerness, Self-Reflecting Code) võib mittevoogedastus võtta väga kaua aega — mõnikord minuteid — ilma nähtava tagasisideta. **Kasuta voogedastust keerukate promptidega katsetamisel**, et näha mudeli tööd ja vältida arusaama, et päring on aegunud.
>
> **Märkus: Brauseri nõuded** — Voogedastuse funktsioon kasutab Fetch Streams API-d (`response.body.getReader()`), mis nõuab täielikku brauserit (Chrome, Edge, Firefox, Safari). See **ei tööta** VS Code'i sisseehitatud Simple Browser'is, kuna selle veebivaade ei toeta ReadableStream API-d. Kui kasutad Simple Browser’i, siis mittevoogedastusnupud töötavad tavalise kujul – ainult voogedastusnupud on mõjutatud. Ava täielikuks kogemuseks `http://localhost:8083` välises brauseris.

### Madal vs Kõrge Janunevus

Esita lihtne küsimus nagu "Mis on 15% arvust 200?" kasutades Madalat Janunevust. Saad kohese, otsese vastuse. Nüüd esita keerukam küsimus nagu "Töötle välja kõrge koormusega API vahemälustrateegia" kasutades Kõrget Janunevust. Vajuta **🔴 Voogvastus (Live)** ja vaata, kuidas mudeli detailne põhjendus ilmub sümboli kaupa. Sama mudel, sama küsimuse struktuur – aga prompt ütleb, kui palju mõelda.

### Ülesande täitmine (Tööriistade eelsõnad)

Mitmesammulised töövood saavad kasu eelplaneerimisest ja protsessi jutustamisest. Mudel kirjeldab, mida teeb, kirjeldab iga sammu ja seejärel võtab tulemused kokku.

### Enesepeegeldav kood

Proovi "Loo e-posti valideerimise teenus". Selle asemel, et lihtsalt koodi genereerida ja peatuda, genereerib mudel, hindab seda kvaliteedikriteeriumite järgi, tuvastab nõrkused ja täiustab. Näed, kuidas see iteratsioonivalt töötab, kuni kood vastab tootmisstandarditele.

### Struktureeritud analüüs

Koodikontrollid vajavad järjepidevaid hindamisraamistikke. Mudel analüüsib koodi fikseeritud kategooriate alusel (õigsus, praktikad, jõudlus, turvalisus) koos tõsidustasemega.

### Mitmesammuline vestlus

Küsi "Mis on Spring Boot?" ja seejärel kohe "Näita mulle näidet". Mudel mäletab sinu esimest küsimust ja annab just selle põhjal Spring Boot näite. Ilma mäluta oleks teine küsimus liiga ebamäärane.

### Samm-sammult põhjendus

Vali mõni matemaatikaülesanne ja proovi seda nii Samm-sammult põhjendusega kui ka Madala Janunevusega. Madal janunevus annab lihtsalt vastuse – kiire, aga mustri mõttes ebaselge. Samm-sammult näitab kõiki arvutusi ja otsuseid.

### Piiratud väljund

Kui vajad teatud formaate või sõnade arvu, tagab see muster rangelt nõuete järgimise. Proovi genereerida kokkuvõte, mis sisaldab täpselt 100 sõna märkepunktides.

## Mida sa tegelikult õpid

**Põhjenduse pingutus muudab kõik**

GPT-5.2 võimaldab sul juhtida arvutuspingutust läbi promptide. Madal pingutus tähendab kiireid vastuseid minimaalse uurimisega. Kõrge pingutus tähendab, et mudel võtab aega sügavaks mõtlemiseks. Õpid sejama pingutuse ülesande keerukusega – ära raiska aega lihtsatele küsimustele, aga ära kiirusta keeruliste otsuste tegemisega.

**Struktuur juhib käitumist**

Kui märkad promptis XML-tähiseid? Need ei ole lihtsalt kaunistuseks. Mudelid järgivad struktureeritud juhiseid usaldusväärsemalt kui vabateksti. Kui vajad mitmesammulisi protsesse või keerulist loogikat, aitab struktuur mudelil jälgida, kus ta parasjagu on ja mis järgmine samm on. Järgmine joonis jagab hästi struktureeritud prompti, näidates, kuidas sildid nagu `<system>`, `<instructions>`, `<context>`, `<user-input>`, ja `<constraints>` organiseerivad juhised selgeteks sektsioonideks.

<img src="../../../translated_images/et/prompt-structure.a77763d63f4e2f89.webp" alt="Prompti struktuur" width="800"/>

*Hästi struktureeritud prompti anatoomia selgete sektsioonide ja XML-laadse korraldusega*

**Kvaliteet läbi enesehindamise**

Enesepeegeldamise mustrid töötavad, tehes kvaliteedikriteeriumid eksplitsiitseks. Selle asemel, et loota, et mudel "teeb õigesti", ütled täpselt, mida tähendab "õige": korrektne loogika, veakäsitlus, jõudlus, turvalisus. Seejärel saab mudel enda väljundit hinnata ja paremaks muuta. See muudab koodigeneratsiooni loteriist protsessiks.

**Kontekst on piiratud**

Mitmesammulised vestlused toimivad, kaasates iga päringu juurde sõnumi ajaloo. Kuid on piir – iga mudeli tokenite arv on maksimaalne. Vestluse kasvades vajad strateegiaid, et hoida asjakohane kontekst ilma selle piirmäära ületamata. See moodul näitab sulle, kuidas mälu töötab; hiljem õpid, millal võtta kokku, millal unustada ja millal taastada.

## Järgmised sammud

**Järgmine moodul:** [03-rag - RAG (teadmistepõhine genereerimine)](../03-rag/README.md)

---

**Navigeerimine:** [← Eelmine: Moodul 01 - Sissejuhatus](../01-introduction/README.md) | [Tagasi põhilehele](../README.md) | [Järgmine: Moodul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:  
See dokument on tõlgitud kasutades tehisintellektil põhinevat tõlketeenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi püüdleme täpsuse poole, palume arvestada, et automatiseeritud tõlked võivad sisaldada vigu või ebatäpsusi. Originaaldokument selle emakeeles tuleks pidada autoriteetseks allikaks. Olulise teabe puhul on soovitatav kasutada professionaalseid inimtõlkeid. Me ei vastuta selle tõlke kasutamisest tulenevate arusaamatuste või valesti mõistmiste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->