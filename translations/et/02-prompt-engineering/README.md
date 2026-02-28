# Moodul 02: Promptide ehitamine GPT-5.2-ga

## Sisukord

- [Videojuhend](../../../02-prompt-engineering)
- [Mida sa õpid](../../../02-prompt-engineering)
- [Eeltingimused](../../../02-prompt-engineering)
- [Mõistes promptide ehitamist](../../../02-prompt-engineering)
- [Promptide ehitamise põhialused](../../../02-prompt-engineering)
  - [Null-kaugus promptimine](../../../02-prompt-engineering)
  - [Mõne näite promptimine](../../../02-prompt-engineering)
  - [Mõtte ahela meetod](../../../02-prompt-engineering)
  - [Rollipõhine promptimine](../../../02-prompt-engineering)
  - [Promptide mallid](../../../02-prompt-engineering)
- [Arendatud mustrid](../../../02-prompt-engineering)
- [Olemasolevate Azure ressursside kasutamine](../../../02-prompt-engineering)
- [Rakenduse ekraanipildid](../../../02-prompt-engineering)
- [Mustrite uurimine](../../../02-prompt-engineering)
  - [Madal vs kõrge valmisolek](../../../02-prompt-engineering)
  - [Ülesande täitmine (tööriistade sissejuhatused)](../../../02-prompt-engineering)
  - [Isepeegeldav kood](../../../02-prompt-engineering)
  - [Struktureeritud analüüs](../../../02-prompt-engineering)
  - [Mitme vooru vestlus](../../../02-prompt-engineering)
  - [Samm-sammult põhjendamine](../../../02-prompt-engineering)
  - [Piiratud väljund](../../../02-prompt-engineering)
- [Mida sa tegelikult õpid](../../../02-prompt-engineering)
- [Järgmised sammud](../../../02-prompt-engineering)

## Videojuhend

Vaata seda otseülekannet, mis selgitab, kuidas selle mooduliga alustada:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Mida sa õpid

<img src="../../../translated_images/et/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

Eelmises moodulis nägid, kuidas mälu võimaldab vestlus-IA-d ja kasutasid GitHubi mudeleid põhilisteks interaktsioonideks. Nüüd keskendume sellele, kuidas küsimusi esitada — promptidele endile — kasutades Azure OpenAI GPT-5.2. Kuidas sa oma prompte struktureerid, mõjutab dramaatiliselt vastuste kvaliteeti. Alustame põhitehnikate ülevaatega, seejärel liigume edasi kaheksa arendatud mustri poole, mis kasutavad GPT-5.2 võimekust täielikult ära.

Kasutame GPT-5.2, kuna see tutvustab põhjendamise juhtimist - sa saad mudelile öelda, kui palju mõtlemist ta enne vastamist tegema peaks. See muudab erinevad promptimise strateegiad selgemaks ja aitab sul mõista, millal mida kasutada. Lisaks on Azure'l GPT-5.2 jaoks vähem kasutuspiiranguid võrreldes GitHubi mudelitega.

## Eeltingimused

- Läbitud Moodul 01 (Azure OpenAI ressursid juurutatud)
- `.env` fail juurkataloogis Azure volitustega (loodud `azd up` käsuga Moodulis 01)

> **Märkus:** Kui sa pole Moodulit 01 lõpetanud, järgi seal esmalt juurutamise juhiseid.

## Mõistes promptide ehitamist

<img src="../../../translated_images/et/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Mis on promptide ehitamine?" width="800"/>

Promptide ehitamine tähendab sisendteksti kujundamist nii, et see pidevalt annaks vajalikke tulemusi. See ei seisne ainult küsimuste esitamisel - vaid ka taotluste struktureerimises nii, et mudel mõistaks täpselt, mida sa tahad ja kuidas seda esitada.

Mõtle sellele nagu kolleegile juhiste andmine. "Paranda viga" on ebamäärane. "Paranda UserService.java faili 45. real esinev null-viitaja erand, lisades nulli kontrolli" on konkreetne. Keelemudelid töötavad samamoodi - täpsus ja struktuur on olulised.

<img src="../../../translated_images/et/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kuidas LangChain4j sobib" width="800"/>

LangChain4j pakub infrastruktuuri — mudeliühendused, mälu ja sõnumitüübid — samal ajal kui promptide mustrid on lihtsalt hoolikalt struktureeritud tekst, mida selle infrastruktuuri kaudu saadad. Põhikomponentideks on `SystemMessage` (mis määrab IA käitumise ja rolli) ja `UserMessage` (mis kannab sinu tegelikku taotlust).

## Promptide ehitamise põhialused

<img src="../../../translated_images/et/five-patterns-overview.160f35045ffd2a94.webp" alt="Viie promptide ehitamise mustri ülevaade" width="800"/>

Enne kui sukeldume selle mooduli arendatud mustritesse, vaatame viis põhitehnikat üle. Need on ehitusplokid, mida iga promptide ehitaja peaks tundma. Kui oled juba läbinud [Kiirstardi mooduli](../00-quick-start/README.md#2-prompt-patterns), oled neid seal näinud — siin on nende mõtteline raamistik.

### Null-kaugus promptimine

Lihtsaim lähenemine: anna mudelile otsene juhis ilma näideteta. Mudel tugineb täielikult oma koolitusel, et mõista ja täita ülesannet. See töötab hästi lihtsate taotluste puhul, kus eeldatav käitumine on ilmselge.

<img src="../../../translated_images/et/zero-shot-prompting.7abc24228be84e6c.webp" alt="Null-kaugus promptimine" width="800"/>

*Otsene juhis ilma näideteta — mudel järeldab ülesande ainult juhiseta*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Vasta: "Positiivne"
```

**Millal kasutada:** Lihtsad klassifikatsioonid, otsesed küsimused, tõlked või kõik ülesanded, mida mudel suudab ilma täiendava juhenduseta täita.

### Mõne näite promptimine

Esita näited, mis demonstreerivad mustrit, mida soovid mudelilt järgida. Mudel õpib sinu näidete põhjal oodatud sisend-väljundi vormingu ja rakendab seda uute sisendite puhul. See parandab märkimisväärselt järjepidevust ülesannetes, kus soovitud formaat või käitumine ei ole ilmne.

<img src="../../../translated_images/et/few-shot-prompting.9d9eace1da88989a.webp" alt="Mõne näite promptimine" width="800"/>

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

**Millal kasutada:** Kohandatud klassifikatsioonid, järjepidev vormindus, domeenispetsiifilised ülesanded või kui null-kauguse tulemused on ebaühtlased.

### Mõtte ahela meetod

Paluge mudelil näidata oma põhjendus samm-sammult. Selle asemel, et kohe vastusest mööda minna, lagundab mudel probleemi ja töötab läbi iga osa täpselt. See parandab täpsust matemaatikas, loogikas ja mitmeastmeliste mõtlemisülesannete puhul.

<img src="../../../translated_images/et/chain-of-thought.5cff6630e2657e2a.webp" alt="Mõtte ahela promptimine" width="800"/>

*Samm-sammult põhjendamine — keeruliste probleemide lahtilõhkumine selgeteks loogilisteks sammudeks*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Mudel näitab: 15 - 8 = 7, seejärel 7 + 12 = 19 õuna
```

**Millal kasutada:** Matemaatikaülesanded, loogikapuzzle’id, silumine või igasugune ülesanne, kus põhjendusprotsessi näitamine suurendab täpsust ja usaldusväärsust.

### Rollipõhine promptimine

Sea IA-le isiksus või roll enne oma küsimuse esitamist. See annab konteksti, mis mõjutab vastuse tooni, sügavust ja fookust. "Tarkvaraarhitekt" annab erineva nõu kui "noorem arendaja" või "turvaaudiitor".

<img src="../../../translated_images/et/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rollipõhine promptimine" width="800"/>

*Konteksti ja isiksuse määramine — sama küsimus saab erineva vastuse sõltuvalt määratud rollist*

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

**Millal kasutada:** Koodikontrollid, juhendamine, domeenispetsiifiline analüüs või kui vajad vastuseid, mis on kohandatud mingile konkreetsele teadmiste tasemele või vaatenurgale.

### Promptide mallid

Loo taaskasutatavad promptid muutujate kohatäitetega. Selle asemel, et iga kord uut prompti kirjutada, defineeri mall üks kord ja täida erinevate väärtustega. LangChain4j `PromptTemplate` klass teeb selle lihtsaks kasutades `{{variable}}` süntaksit.

<img src="../../../translated_images/et/prompt-templates.14bfc37d45f1a933.webp" alt="Promptide mallid" width="800"/>

*Taaskasutatavad promptid muutujate kohatäidetega — üks mall, palju kasutusi*

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

**Millal kasutada:** Korduvad päringud erinevate sisenditega, partiitöötlus, taaskasutatavate IA töövoogude loomine või igas olukorras, kus prompti struktuur jääb samaks, kuid andmed muutuvad.

---

Need viis põhialust annavad sulle tugeva tööriistakomplekti enamiku promptimise ülesannete jaoks. Selle mooduli ülejäänud osa ehitab nendele peale kaheksa arenenud mustriga, mis kasutavad GPT-5.2 põhjendusjuhtimist, enesehindamist ja struktureeritud väljundivõimalusi.

## Arendatud mustrid

Pärast põhialuste käsitlemist liigume kaheksa arenenud mustri juurde, mis teevad selle mooduli ainulaadseks. Kõik probleemid ei vaja sama lähenemist. Mõned küsimused vajavad kiireid vastuseid, teised sügavat mõtlemist. Mõned vajavad nähtavat põhjendamist, teised ainult tulemusi. Iga alljärgnev muster on optimeeritud erinevale olukorrale — ja GPT-5.2 põhjendusjuhtimine muudab erinevused veelgi ilmsemaks.

<img src="../../../translated_images/et/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Kaheksa promptide ehitamise mustrit" width="800"/>

*Kaheksa promptide ehitamise mustri ja nende kasutusjuhtumite ülevaade*

<img src="../../../translated_images/et/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Põhjendusjuhtimine GPT-5.2-ga" width="800"/>

*GPT-5.2 põhjendusjuhtimine võimaldab sul määrata, kui palju mõtlemist mudel peaks tegema — alates kiirtest otsestest vastustest kuni sügava uurimiseni*

**Madal valmisolek (Kiire ja keskendunud)** - Lihtsate küsimuste jaoks, kus tahad kiireid ja otsekoheseid vastuseid. Mudel teeb minimaalset põhjendamist - maksimaalselt 2 sammu. Kasuta seda arvutusteks, otsinguteks või lihtsate päringute jaoks.

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

> 💡 **Uuri koos GitHub Copilotiga:** Ava [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ja küsi:
> - "Mis vahe on madala ja kõrge valmisoleku promptimismustritel?"
> - "Kuidas aitavad XML sildid promptides struktureerida IA vastust?"
> - "Millal peaks kasutama enesepeegeldamise mustreid ja millal otseseid juhiseid?"

**Kõrge valmisolek (Sügav ja põhjalik)** - Keerukate probleemide jaoks, kus soovid põhjalikku analüüsi. Mudel uurib põhjalikult ja näitab detailset põhjendust. Kasuta seda süsteemide disainiks, arhitektuuriliste otsuste tegemiseks või keerukateks uuringuteks.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Ülesande täitmine (Samm-sammult edenemine)** - Mitmeastmeliste töövoogude jaoks. Mudel annab alguses plaani, kirjeldab iga sammu läbimist ja annab seejärel kokkuvõtte. Kasuta seda migratsioonide, rakenduste või mistahes mitmesammuliste protsesside puhul.

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

Mõtte ahela promptimine palub mudelil selgelt näidata oma põhjendusprotsessi, mis parandab täpsust keerukate ülesannete puhul. Samm-sammult lahtilõhkumine aitab nii inimestel kui ka IA-l loogikat paremini mõista.

> **🤖 Proovi koos [GitHub Copilot](https://github.com/features/copilot) Chatiga:** Küsi selle mustri kohta:
> - "Kuidas kohandada ülesande täitmise mustrit pikkade toimingute jaoks?"
> - "Millised on parimad tavad tööriistade sissejuhatuste struktureerimisel tootmisrakendustes?"
> - "Kuidas saada vahereportite kuvamist kasutajaliideses?"

<img src="../../../translated_images/et/task-execution-pattern.9da3967750ab5c1e.webp" alt="Ülesande täitmise muster" width="800"/>

*Plaani → Täida → Kokkuvõtlik töövoog mitmeastmeliste ülesannete jaoks*

**Isepeegeldav kood** - Tootmiskvaliteediga koodi genereerimiseks. Mudel genereerib koodi, mis järgib tootmisstandardeid ja korralikku veakäsitlust. Kasuta seda uute funktsioonide või teenuste loomisel.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/et/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Isepeegeldamise tsükkel" width="800"/>

*Iteratiivse täiustuse tsükkel - genereeri, hinda, tuvastaja probleemid, parenda, korda*

**Struktureeritud analüüs** - Järjepidevaks hindamiseks. Mudel vaatab koodi üle kindla raamistiku alusel (õigsus, tavad, jõudlus, turvalisus, hooldatavus). Kasuta seda koodikontrollides või kvaliteedihinnangutes.

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

> **🤖 Proovi koos [GitHub Copilot](https://github.com/features/copilot) Chatiga:** Küsi struktureeritud analüüsi kohta:
> - "Kuidas kohandada analüüsiraamistikku erinevat tüüpi koodikontrollide jaoks?"
> - "Mis on parim viis struktureeritud väljundi programmiliseks töötlemiseks?"
> - "Kuidas tagada ühtlane raskusaste erinevates kontrollsessioonides?"

<img src="../../../translated_images/et/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Struktureeritud analüüsi muster" width="800"/>

*Raamistik järjepidevate koodikontrollide jaoks raskusastmetega*

**Mitme vooru vestlus** - Vestlused, mis vajavad konteksti. Mudel mäletab eelnevaid sõnumeid ja ehitab nende peale. Kasuta seda interaktiivseteks abisessioonideks või keerukate küsimuste-vastuste jaoks.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/et/context-memory.dff30ad9fa78832a.webp" alt="Konteksti mälu" width="800"/>

*Kuidas vestluse kontekst koguneb mitme vooru vältel kuni tokenite limiidini*

**Samm-sammult põhjendamine** - Probleemide jaoks, mis vajavad nähtavat loogikat. Mudel näitab iga sammu kohta otsest põhjendust. Kasuta seda matemaatikaprobleemide, loogikapuzzle’de või siis, kui vajad mõtlemisprotsessi mõistmist.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/et/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Samm-sammult muster" width="800"/>

*Probleemide lahtilõhkumine selgeteks loogilisteks sammudeks*

**Piiratud väljund** - Vastuste jaoks, millel on spetsiifilised formaadi nõuded. Mudel järgib rangelt formaadi- ja pikkusjuhiseid. Kasuta seda kokkuvõtete tegemiseks või kui vajad täpset väljundistruktuuri.

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

<img src="../../../translated_images/et/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Piiratud väljundi muster" width="800"/>

*Spetsiifiliste formaadi, pikkuse ja struktuuri nõuete täitmine*

## Olemasolevate Azure ressursside kasutamine

**Kontrolli juurutust:**

Veendu, et `.env` fail on juurkataloogis Azure volitustega (loodud Moodulis 01):
```bash
cat ../.env  # Peaks näitama AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Rakenduse käivitamine:**

> **Märkus:** Kui oled juba kõik rakendused käivitanud `./start-all.sh` käsuga Moodulis 01, siis see moodul juba töötab pordil 8083. Sa võid allolevad käivitamiskäsud vahele jätta ja minna otse lehele http://localhost:8083.
**Variant 1: Spring Boot Dashboardi kasutamine (soovitatav VS Code kasutajatele)**

Arenduskonteiner sisaldab Spring Boot Dashboard laiendust, mis pakub visuaalset kasutajaliidest kõigi Spring Boot rakenduste haldamiseks. Leiate selle VS Code vasakpoolsest tegevusribast (otsige Spring Boot ikooni).

Spring Boot Dashboardist saate:
- Vaadata kõiki tööruumis olevaid Spring Boot rakendusi
- Käivitada/peatada rakendusi ühe klikiga
- Vaadata rakenduse logisid reaalajas
- Jälgida rakenduse olekut

Lihtsalt klõpsake “prompt-engineering” kõrval olevat mängimisnuppu, et see moodul käivitada, või käivitage korraga kõik moodulid.

<img src="../../../translated_images/et/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Variant 2: Shell-skriptide kasutamine**

Käivitage kõik veebirakendused (moodulid 01-04):

**Bash:**
```bash
cd ..  # Juure kataloogist
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Juurekataloogist
.\start-all.ps1
```

Või käivitage ainult see moodul:

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

Mõlemad skriptid laadivad automaatselt juure `.env` faili keskkonnamuutujad ja koostavad JAR failid, kui need puuduvad.

> **Märkus:** Kui soovite kõik moodulid enne käivitamist käsitsi koostada:
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

Avage brauseris aadress http://localhost:8083.

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

<img src="../../../translated_images/et/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Peamine juhtpaneel, mis näitab kõiki 8 prompt-engineerimise mustrit koos nende omaduste ja kasutusjuhtudega*

## Musterde uurimine

Veebiliides võimaldab katsetada erinevaid promptimise strateegiaid. Iga muster lahendab erinevaid probleeme - proovi neid, et näha, millal iga lahendus silma paistab.

> **Märkus: voogedastus vs mittevoogedastus** — Igal mustri lehel on kaks nuppu: **🔴 Stream Response (Live)** ja **Mittevoogedastus** valik. Voogedastus kasutab Server-Sent Events (SSE), et kuvada mudeeli genereeritud tokenid reaalajas, nii et näed edusamme kohe. Mittevoogedastus valik ootab kogu vastuse lõpuni, enne kui selle kuvab. Sügavat mõtlemist nõudvate promptide puhul (nt High Eagerness, Self-Reflecting Code) võib mittevoogedastus kutsuda väga kaua aega — mõnikord minuteid — ilma nähtava tagasisideta. **Kasuta voogedastust keerukate promptide katsetamisel**, et näha mudeeli tööd ja vältida muljet, et päring aegus.
>
> **Märkus: brauseri nõue** — Voogedastusfunktsioon kasutab Fetch Streams API-d (`response.body.getReader()`), mis nõuab täisfunktsionaalsusega brauserit (Chrome, Edge, Firefox, Safari). See EI toimi VS Code sisseehitatud Simple Browseris, kuna selle veebivaade ei toeta ReadableStream API-d. Kui kasutad Simple Browserit, toimivad mittevoogedastuse nupud tavapäraselt — ainult voogedastuse nupud on mõjutatud. Täieliku kogemuse saamiseks ava `http://localhost:8083` välises brauseris.

### Madal vs Kõrge Innukus

Esita lihtne küsimus nagu „Mis on 15% 200-st?“ kasutades madalat innukust. Saad kohese ja otsese vastuse. Nüüd esita keerulisem küsimus: „Tööta välja vahemällu salvestamise strateegia kõrge liiklusega API-le“ kasutades kõrget innukust. Klõpsa **🔴 Stream Response (Live)** ja vaata mudeeli üksikasjalikku põhjendust token-tokeni haaval. Sama mudel, sama küsimus - kuid prompt ütleb, kui palju mõtlemist tehakse.

### Ülesande Täitmine (Tööriistade Eelsõnad)

Mitmeetapilised töövood vajavad eelplaneerimist ja edenemise jutustamist. Mudel kirjeldab, mida ta teeb, jutustab iga sammu, ning võtab tulemused kokku.

### Enesereflekteeriv Kood

Proovi "Loo e-posti valideerimise teenus". Selle asemel, et lihtsalt koodi genereerida ja peatuda, genereerib mudel, hindab kvaliteedikriteeriumite alusel, tuvastab nõrkused ja parandab. Näed, kuidas ta iteratiivselt parandab, kuni kood vastab tootmisstandarditele.

### Struktureeritud Analüüs

Koodülevaated vajavad ühtset hindamisraamistikku. Mudel analüüsib koodi fikseeritud kategooriate kaupa (õigsus, praktikad, jõudlus, turvalisus) koos raskusastme tasemetega.

### Mitmekäiguline Vestlus

Küsi „Mis on Spring Boot?“ ja kohe seejärel „Näita mulle näidet“. Mudel mäletab sinu esimest küsimust ja annab spetsiifilise Spring Boot näite. Ilma mäluta oleks see teine küsimus liiga ebamäärane.

### Järg-järgult Põhjendamine

Võta mõni matemaatikaülesanne ja proovi nii järg-järgult põhjendamist kui ka madalat innukust. Madal innukus annab ainult vastuse - kiire, kuid ebaselge. Järg-järgult näitab iga arvutust ja otsust.

### Piiratud Väljund

Kui vajad kindlaid vorminguid või sõnade arvu, sunnib see muster rangelt järgima nõudeid. Proovi genereerida kokkuvõtet täpselt 100 sõnaga märksõnade vormis.

## Mida Sa Tegelikult Õpid

**Põhjenduspingutus muudab kõik**

GPT-5.2 võimaldab sul kontrollida arvutuspingutust promptide kaudu. Madal pingutus tähendab kiireid vastuseid minimaalse uurimisega. Kõrge pingutus tähendab, et mudel võtab aega sügavamaks mõtlemiseks. Õpid sobitama pingutust ülesande keerukusega - ära raiska aega lihtsatele küsimustele, kuid ära kiirusta keeruliste otsustega.

**Struktuur juhib käitumist**

Kas märkad XML-tähti promptides? Need ei ole dekoratiivsed. Mudelid järgivad struktureeritud juhiseid usaldusväärsemalt kui vabateksti. Kui vajad mitme sammu protsessi või keerulist loogikat, aitab struktuur mudelil jälgida, kus ta on ja mis tuleb järgmisena.

<img src="../../../translated_images/et/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Hea struktureeritud prompti anatoomia koos selgete osade ja XML-laadse korraldusega*

**Kvaliteet enesehindamise kaudu**

Enesereflekteerivad mustrid toimivad, tehes kvaliteedikriteeriumid selgelt nähtavaks. Kujutle, et lootes, et mudel "teeb õigesti" – selle asemel ütled täpselt, mida "õige" tähendab: õige loogika, veakäsitlus, jõudlus, turvalisus. Seejärel saab mudel oma väljundit hinnata ja parandada. See muudab koodigeneratsiooni loteriist protsessiks.

**Kontekst on piiratud**

Mitmekäigulised vestlused toimivad sõnumiajaloo lisamisega igale päringule. Kuid on limiit – igal mudelil on maksimaalne tokenite arv. Kuna vestlused kasvavad, vajad strateegiaid asjakohase konteksti hoidmiseks ilma lakke sattumata. See moodul näitab, kuidas mälu töötab; hiljem õpid, millal kokku võtta, millal unustada ja millal taastada.

## Järgmised Sammud

**Järgmine moodul:** [03-rag - RAG (otsimisega täiustatud genereerimine)](../03-rag/README.md)

---

**Navigatsioon:** [← Eelmine: Moodul 01 - Sissejuhatus](../01-introduction/README.md) | [Tagasi põhilehele](../README.md) | [Järgmine: Moodul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Lahtiütlus**:  
See dokument on tõlgitud kasutades tehisintellektil põhinevat tõlketeenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi püüame täpsust, palun arvestage, et automatiseeritud tõlked võivad sisaldada vigu või ebatäpsusi. Originaaldokument oma emakeeles tuleks pidada usaldusväärseks allikaks. Kriitilise teabe puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta selle tõlke kasutamisest tingitud arusaamatuste või valesti mõistmiste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->