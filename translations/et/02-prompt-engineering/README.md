# Moodul 02: Prompt Engineering GPT-5.2 abil

## Sisukord

- [Mida sa õpid](../../../02-prompt-engineering)
- [Nõuded eeltingimustele](../../../02-prompt-engineering)
- [Prompt engineering'u mõistmine](../../../02-prompt-engineering)
- [Prompt engineering'u põhialused](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Rollipõhine promptimine](../../../02-prompt-engineering)
  - [Prompt template'id](../../../02-prompt-engineering)
- [Täpsemad mustrid](../../../02-prompt-engineering)
- [Olemasolevate Azure ressursside kasutamine](../../../02-prompt-engineering)
- [Rakenduse ekraanipildid](../../../02-prompt-engineering)
- [Mustrid lahtimuukimine](../../../02-prompt-engineering)
  - [Madal vs kõrge innukus](../../../02-prompt-engineering)
  - [Ülesande täitmine (tööriista sissejuhatused)](../../../02-prompt-engineering)
  - [Isereflektsioonikood](../../../02-prompt-engineering)
  - [Struktureeritud analüüs](../../../02-prompt-engineering)
  - [Mitme vooru vestlus](../../../02-prompt-engineering)
  - [Sammu-sammuline põhjendus](../../../02-prompt-engineering)
  - [Piiratud väljund](../../../02-prompt-engineering)
- [Mida sa tegelikult õpid](../../../02-prompt-engineering)
- [Järgmised sammud](../../../02-prompt-engineering)

## Mida sa õpid

<img src="../../../translated_images/et/what-youll-learn.c68269ac048503b2.webp" alt="Mida sa õpid" width="800"/>

Eelmises moodulis nägid, kuidas mälu võimaldab vestlusliku tehisintellekti tööd ja kasutasid GitHubi mudeleid põhisuhtlusteks. Nüüd keskendume sellele, kuidas sa küsid küsimusi — sellele, milliseid prompt'e sa kasutad — kasutades Azure OpenAI GPT-5.2 mudelit. See, kuidas sa oma prompt'e struktureerid, mõjutab oluliselt vastuste kvaliteeti. Alustame põhitehnikate ülevaatega ja liigume seejärel edasi kaheksa täpsema mustrini, mis kasutavad täielikult GPT-5.2 võimalusi.

Kasutame GPT-5.2, sest see toob sisse mõtlemise juhtimise — saad mudelile ütelda, kui palju ta mõtlema peab enne vastamist. See muudab erinevad promptimise strateegiad selgemaks ja aitab mõista, millal millist kasutada. Samuti kasutame ära Azure väiksemaid piiranguid GPT-5.2 puhul võrreldes GitHubi mudelitega.

## Nõuded eeltingimustele

- Lõpetatud Moodul 01 (Azure OpenAI ressursid paigaldatud)
- Juurekataloogis olemas `.env` fail Azure volitustega (loodud `azd up` käsuga Moodulis 01)

> **Märkus:** Kui sa ei ole moodulit 01 lõpetanud, järgi esmalt seal olevaid juurutusjuhiseid.

## Prompt engineering'u mõistmine

<img src="../../../translated_images/et/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Mis on prompt engineering?" width="800"/>

Prompt engineering tähendab sellise sisendi kavandamist, mis toob järjekindlalt soovitud tulemused. See ei ole lihtsalt küsimuste esitamine — see tähendab päringute ülesehitamist nii, et mudel täpselt mõistaks, mida sa tahad ja kuidas seda esitada.

Mõtle sellele nagu juhiste andmisele kolleegile. „Paranda viga“ on liiga üldine. „Paranda nullviite erand UserService.java faili 45. real, lisades nullkontrolli“ on konkreetne. Keelemudelid töötavad samamoodi — täpsus ja struktuur loevad.

<img src="../../../translated_images/et/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kuidas LangChain4j sobitub" width="800"/>

LangChain4j pakub infrastruktuuri — mudeli ühendused, mälu ja sõnumitüübid — samal ajal kui promptimismustrid on lihtsalt hoolikalt struktureeritud tekst, mida sa selle infrastruktuuri kaudu saadad. Peamised ehitusplokid on `SystemMessage` (mis määrab AI käitumise ja rolli) ja `UserMessage` (mis kannab sinu tegelikku päringut).

## Prompt engineering'u põhialused

<img src="../../../translated_images/et/five-patterns-overview.160f35045ffd2a94.webp" alt="Viie prompt engineering mustri ülevaade" width="800"/>

Enne kui sukeldume täpsematesse mustritesse seal moodulis, vaatame üle viis põhitehnikat promptimiseks. Need on ehitusplokid, mida iga prompt engineer peaks tundma. Kui sa oled juba töötanud läbi [Kiirestargi mooduli](../00-quick-start/README.md#2-prompt-patterns), oled neid praktikas näinud — siin on nende taga olev kontseptuaalne raamistik.

### Zero-Shot Prompting

Kõige lihtsam meetod: anna mudelile otsekohene juhis ilma näideteta. Mudel tugineb täielikult oma treeningule, et mõista ja täita ülesannet. See töötab hästi lihtsate päringute puhul, kus eeldatav käitumine on selge.

<img src="../../../translated_images/et/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Otsekohene juhis ilma näideteta — mudel järeldab ülesande ainult juhisest*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Vastus: "Positiivne"
```

**Millal kasutada:** Lihtsad klassifikatsioonid, otsesed küsimused, tõlked või muu, mida mudel suudab ilma lisajuhisteta teha.

### Few-Shot Prompting

Anna näited, mis demonstreerivad mustrit, mida tahad mudelil järgida. Mudel õpib sinu näidetest etteantud sisendi-väljundi vormingu ja rakendab seda uutele sisenditele. See parendab järjepidevust ülesannetes, kus soovitud formaat või käitumine pole ilmne.

<img src="../../../translated_images/et/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Õppimine näidetest — mudel tuvastab mustri ja rakendab seda uutele sisenditele*

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

**Millal kasutada:** Kohandatud klassifikatsioonid, järjepidev vormindus, domeenispetsiifilised ülesanded või kui zero-shot tulemused on ebaühtlased.

### Chain of Thought

Palju mudelil näidata oma põhjendusi samm-sammult. Selle asemel, et kohe vastuseni jõuda, lõhub mudel probleemi osadeks ja töötab iga osa läbi selgelt. See parandab täpsust matemaatikas, loogikas ja mitmesammulistes ülesannetes.

<img src="../../../translated_images/et/chain-of-thought.5cff6630e2657e2a.webp" alt="Sammu-sammult põhjendus" width="800"/>

*Sammu-sammuline põhjendus — keeruliste probleemide lagundamine selgeteks loogilisteks sammudeks*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Mudel näitab: 15 - 8 = 7, seejärel 7 + 12 = 19 õuna
```

**Millal kasutada:** Matemaatikaprobleemid, loogikapõlved, silumine või mis tahes ülesanne, kus põhjendusprotsessi näitamine parandab täpsust ja usaldusväärsust.

### Rollipõhine promptimine

Sea AI jaoks isiksus või roll enne küsimuse esitamist. See annab konteksti, mis kujundab vastuse tooni, sügavuse ja fookuse. „Tarkvaraarhitekt“ annab teistsugust nõu kui „junior arendaja“ või „turbekontrollija“.

<img src="../../../translated_images/et/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rollipõhine promptimine" width="800"/>

*Konteksti ja isiksuse seadmine — sama küsimus saab erineva vastuse vastavalt määratud rollile*

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

**Millal kasutada:** Koodi ülevaated, juhendamine, domeenispetsiifiline analüüs või kui vajad vastuseid, mis on kohandatud konkreetse ekspertiisi taseme või vaatenurgaga.

### Prompt template'id

Loo korduvkasutatavad prompt'id muutujate kohtadega. Selle asemel, et kirjutada uus prompt iga kord, defineeri template üks kord ja täida erinevate väärtustega. LangChain4j `PromptTemplate` klass muudab selle mugavalt `{{muutuja}}` süntaksiga.

<img src="../../../translated_images/et/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt template'id" width="800"/>

*Korduvkasutatavad prompt'id muutujate kohtadega — üks template, mitu kasutust*

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

**Millal kasutada:** Korduvad päringud erinevate sisenditega, partii töötlemine, korduvkasutatavate AI töövoogude loomine või mis tahes olukorras, kus prompt'i struktuur jääb samaks, kuid andmed muutuvad.

---

Need viis põhialust annavad sulle tugeva tööriistakomplekti enamiku promptimise ülesannete jaoks. Selle mooduli ülejäänud osa ehitab nendele peale kaheksa täpse mustri näol, mis kasutavad GPT-5.2 mõtlemise juhtimist, isereflektsiooni ja struktureeritud väljundit.

## Täpsemad mustrid

Põhialuste selgeks saamise järel liigume kaheksa edasijõudnute mustrini, mis muudavad selle mooduli eriliseks. Kõik probleemid ei vaja sama lähenemist. Mõned küsimused vajavad kiireid vastuseid, teised sügavat mõtlemist. Mõned vajavad nähtavat põhjendust, teised ainult tulemusi. Iga allpool olev muster on optimeeritud erinevaks olukorraks — ja GPT-5.2 mõtlemise juhtimine rõhutab neid erinevusi veelgi.

<img src="../../../translated_images/et/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Kaheksa promptimise mustrit" width="800"/>

*Kaheksa prompt engineering mustri ülevaade ja nende kasutusjuhud*

<img src="../../../translated_images/et/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Mõtlemise juhtimine GPT-5.2-ga" width="800"/>

*GPT-5.2 mõtlemise juhtimine võimaldab määrata, kui palju mudel peab mõtlema — kiiretest otsestest vastustest kuni sügava uurimiseni*

**Madal innukus (kiire ja fokuseeritud)** – lihtsate küsimuste jaoks, kus tahad kiireid ja otseseid vastuseid. Mudel teeb minimaalset põhjendust — maksimaalselt 2 sammu. Kasuta seda arvutusteks, otsinguteks või lihtsate küsimuste puhul.

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
> - "Mis vahe on madala ja kõrge innukusega promptimismustritel?"
> - "Kuidas aitavad XML märgendid prompt'ides AI vastust struktureerida?"
> - "Millal kasutada isereflektsiooni mudeleid vs otsekoheseid juhiseid?"

**Kõrge innukus (sügav ja põhjalik)** – keerukate probleemide jaoks, kus vajad põhjalikku analüüsi. Mudel uurib põhjalikult ja näitab detailset põhjendust. Kasuta seda süsteemidisaini, arhitektuuriliste otsuste või keerulise uurimustöö puhul.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Ülesande täitmine (sammu-sammult jätkutegevused)** – mitmesammuliste töövoogude jaoks. Mudel annab alguses plaani, kirjeldab iga sammu töö käigus ja seejärel esitab kokkuvõtte. Kasuta seda migratsioonide, rakenduste või mis tahes mitmesammulise protsessi puhul.

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

Chain-of-Thought promptimine palub mudelil näidata oma põhjendusprotsessi, mis parandab täpsust keerulistes ülesannetes. Samm-sammuline jaotus aitab nii inimestel kui ka tehisintellektil mõista loogikat.

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chat’iga:** Küsi selle mustri kohta:
> - "Kuidas kohandada ülesande täitmise mustrit pikaajaliste operatsioonide jaoks?"
> - "Millised on parimad praktikad tööriista sissejuhatuste struktuuris tootmiskeskkonnas?"
> - "Kuidas koguda ja kuvada vahepealseid edenemiseteateid kasutajaliideses?"

<img src="../../../translated_images/et/task-execution-pattern.9da3967750ab5c1e.webp" alt="Ülesande täitmise muster" width="800"/>

*Plaani → Täida → Kokkuvõtlik töövoog mitmesammuliste ülesannete jaoks*

**Isereflekteeriv kood** – tootmiskvaliteediga koodi genereerimiseks. Mudel genereerib koodi, järgides tootmisstandardeid ning kohustuslikku veahaldust. Kasuta seda uute funktsioonide või teenuste loomisel.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/et/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Isetutvustuse tsükkel" width="800"/>

*Iteratiivne täiustamise tsükkel – genereerimine, hindamine, vigade tuvastamine, parandamine, kordamine*

**Struktureeritud analüüs** – järjekindla hindamise jaoks. Mudel vaatab koodi üle kindla raamistiku alusel (õigsus, parimad praktikad, jõudlus, turvalisus, hooldatavus). Kasuta seda koodi ülevaadetes või kvaliteedi hindamises.

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

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chat’iga:** Küsi struktureeritud analüüsi kohta:
> - "Kuidas kohandada analüüsiraamistikku erinevate koodiülevaadete tüüpide jaoks?"
> - "Mis on parim viis struktureeritud väljundit programmeeritult töödelda ja selle põhjal tegutseda?"
> - "Kuidas tagada järjepidevad tõsiduse tasemed erinevates ülevaatussessioonides?"

<img src="../../../translated_images/et/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Struktureeritud analüüsi muster" width="800"/>

*Järjepidevate koodiülevaadete raamistiku ja tõsiseid tasemete jaoks*

**Mitme vooru vestlus** – konteksti vajavate vestluste jaoks. Mudel mäletab eelnevaid sõnumeid ja ehitab nende põhjal edasi. Kasuta seda interaktiivseteks abiseanssideks või keerukateks küsimuste ja vastustega.

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

*Kuidas vestluse kontekst koguneb mitme vooru jooksul kuni tokenite piirini*

**Sammu-sammuline põhjendus** – probleemide jaoks, mis vajavad nähtavat loogikat. Mudel näitab iga sammu selgelt välja põhjendatuna. Kasuta seda matemaatikaprobleemide, logic-ülesannete või siis, kui vajad mõtlemisprotsessi mõistmist.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/et/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Sammu-sammuline muster" width="800"/>

*Probleemide lagundamine selgeteks loogilisteks sammudeks*

**Piiratud väljund** – vastuste jaoks, millel on spetsiifilised formaadi nõuded. Mudel järgib rangelt vormingu ja pikkuse reegleid. Kasuta seda kokkuvõtete tegemisel või kui vajad täpset väljundistruktuuri.

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

*Spetsiifiliste formaadi, pikkuse ja struktuuri nõuete jõustamine*

## Olemasolevate Azure ressursside kasutamine

**Kontrolli käivitust:**

Veendu, et juurekataloogis oleks `.env` fail Azure volitustega (loodud Moodulis 01):
```bash
cat ../.env  # Peaks kuvama AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Käivita rakendus:**

> **Märkus:** Kui sa oled juba kõik rakendused käivitanud käsuga `./start-all.sh` Moodulis 01, siis see moodul jookseb juba pordil 8083. Sa võid allolevad käivitamiskäsud vahele jätta ja minna otse aadressile http://localhost:8083.

**Variant 1: Spring Boot Dashboardi kasutamine (Soovitatav VS Code kasutajatele)**

Dev konteiner sisaldab Spring Boot Dashboard laiendust, mis pakub visuaalset kasutajaliidest kõikide Spring Boot rakenduste haldamiseks. Sa leiad selle VS Code activity baarist vasakult (otsi Spring Boot ikooni).

Spring Boot Dashboardilt saad:
- Vaadata kõiki töökeskkonnas saadavalolevaid Spring Boot rakendusi
- Käivitada/peatada rakendusi ühe klikiga
- Vaadata rakenduste logisid reaalajas
- Jälgida rakenduste olekut
Lihtsalt klõpsake "prompt-engineering" kõrval olevat esitamisnuppu, et alustada seda moodulit, või käivitage korraga kõik moodulid.

<img src="../../../translated_images/et/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Valik 2: Shell-skriptide kasutamine**

Käivitage kõik veebirakendused (moodulid 01-04):

**Bash:**
```bash
cd ..  # Juurestikust
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Juuremoodulist
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

Mõlemad skriptid loevad automaatselt keskkonnamuutujaid juurkataloogi `.env` failist ja ehitavad JAR-failid, kui neid veel pole.

> **Märkus:** Kui soovite enne käivitamist kõik moodulid käsitsi ehitada:
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

Avage oma brauseris aadress http://localhost:8083.

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

*Peamine armatuurlaud, mis näitab kõiki 8 prompt-engineering mustrit nende omaduste ja kasutusjuhtudega*

## Musterduse uurimine

Veebiliides võimaldab teil katsetada erinevaid promptimise strateegiaid. Iga muster lahendab erinevaid probleeme - proovige neid, et näha, millal milline lähenemine kõige paremini töötab.

### Madal vs Kõrge Põnevus

Esitage lihtne küsimus, näiteks "Mis on 15% arvust 200?" kasutades Madalat Põnevust. Saate kohese ja otsese vastuse. Nüüd küsige midagi keerulisemat, näiteks "Loo vahemällu salvestamise strateegia kõrge koormusega API jaoks" kasutades Kõrget Põnevust. Vaadake, kuidas mudel aeglustub ja annab üksikasjalikku põhjendust. Sama mudel, sama küsimuse struktuur - kuid prompt näitab, kui palju mõtlemist teha.

<img src="../../../translated_images/et/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Kiire arvutus minimaalse põhjendusega*

<img src="../../../translated_images/et/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Kõikehõlmav vahemällu salvestamise strateegia (2.8MB)*

### Ülesande täitmine (tööriistade sissejuhatused)

Mitmetasandilised töövood saavad kasu eelplaneerimisest ja edusammude kirjeldamisest. Mudel kirjeldab, mida ta teeb, selgitab iga sammu ja seejärel võtab tulemused kokku.

<img src="../../../translated_images/et/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*REST-endpointi loomine samm-sammult juhendamisega (3.9MB)*

### Enesepeegeldav kood

Proovige "Loo e-posti valideerimise teenus". Selle asemel, et lihtsalt koodi genereerida ja peatuda, genereerib mudel, hindab kvaliteedikriteeriumite alusel, tuvastab nõrkused ja parandab. Näete, kuidas see kordab protsessi, kuni kood vastab tootmistandarditele.

<img src="../../../translated_images/et/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Täielik e-posti valideerimise teenus (5.2MB)*

### Struktureeritud analüüs

Koodi ülevaated vajavad ühtseid hindamisraamistikke. Mudel analüüsib koodi fikseeritud kategooriate kaupa (õigsus, tavad, jõudlus, turvalisus) koos raskusastmetega.

<img src="../../../translated_images/et/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Raamistikupõhine koodi ülevaade*

### Mitme-käiguline vestlus

Küsige "Mis on Spring Boot?" ja siis kohe jätkake "Näita mulle näidet". Mudel mäletab teie esimest küsimust ja annab teile spetsiaalse Spring Boot näite. Ilma mäluta oleks teine küsimus liiga ebamäärane.

<img src="../../../translated_images/et/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Konteksti säilitamine küsimuste vahel*

### Samm-sammult põhjendamine

Valige matemaatiline ülesanne ja proovige seda nii samm-sammult põhjendamise kui ka Madala Põnevusega. Madal põnevus annab lihtsalt vastuse - kiire, kuid varjatud. Samm-sammult näitab teile iga arvutust ja otsustust.

<img src="../../../translated_images/et/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matemaatiline ülesanne selgete sammudega*

### Piiratud väljund

Kui vajate kindlaid formaate või sõnade arvu, siis see muster nõuab rangelt nende järgimist. Proovige luua kokkuvõte, mis sisaldab täpselt 100 sõna punktide kujul.

<img src="../../../translated_images/et/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Masinõppe kokkuvõte vormindamise kontrolliga*

## Mida te tegelikult õpite

**Põhjenduspingutus muudab kõik**

GPT-5.2 võimaldab teil reguleerida arvutuslikku pingutust läbi promptide. Madal pingutus tähendab kiireid vastuseid minimaalse uurimisega. Kõrge pingutus tähendab, et mudel võtab aega sügavamalt mõtlemiseks. Õpite kohandama pingutust ülesande keerukusega - ärge raisake aega lihtsatele küsimustele, aga ärge ka kiirustage keerukat otsustamist.

**Struktuur juhib käitumist**

Pane tähele XML-silte promptides? Need ei ole kaunistuseks. Mudelid järgivad struktureeritud juhiseid palju usaldusväärsemalt kui vabateksti. Kui vajate mitmeastmelisi protsesse või keerukat loogikat, aitab struktuur mudelil jälgida, kus ta asub ja mis järgmiseks tuleb.

<img src="../../../translated_images/et/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Hea struktuuriga prompti anatoomia selgete osade ja XML-laadse korraldusega*

**Kvaliteet läbi enesehindamise**

Enesepeegeldavad mustrid töötavad, tehes kvaliteedikriteeriumid selgeks. Selle asemel, et loota, et mudel "teeb õigesti", ütlete talle täpselt, mida "õige" tähendab: korrektne loogika, vigade käsitlemine, jõudlus, turvalisus. Seejärel saab mudel ise oma väljundit hinnata ja parandada. See muudab koodigeneratsiooni loteriist protsessiks.

**Kontekst on piiratud**

Mitme-käigulised vestlused töötavad, kaasates iga päringuga sõnumi ajaloo. Kuid on piirang - igal mudelil on maksimaalne tokenite arv. Vestluste korral kasvades vajate strateegiaid, et hoida oluline kontekst ilma sellele piirmäärale vastu sattumata. See moodul näitab teile, kuidas mälu töötab; hiljem õpite, millal teha kokkuvõte, millal unustada ja millal andmeid tagasi tuua.

## Järgmised sammud

**Järgmine moodul:** [03-rag - RAG (otsingupõhine täiendus)](../03-rag/README.md)

---

**Navigeerimine:** [← Eelmine: Moodul 01 - Sissejuhatus](../01-introduction/README.md) | [Tagasi peamenüüsse](../README.md) | [Järgmine: Moodul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Hoiatus**:
See dokument on tõlgitud kasutades tehisintellekti tõlketeenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi püüame täpsust, palun arvestage, et automatiseeritud tõlgetes võib esineda vigu või ebatäpsusi. Originaaldokument selle emakeeles on pidada autoriteetseks allikaks. Tähtsa info puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta selle tõlke kasutamisest tingitud arusaamatuste või valesti mõistmiste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->