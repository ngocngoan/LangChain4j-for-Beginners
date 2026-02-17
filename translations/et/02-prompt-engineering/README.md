# Moodul 02: Promptide inseneritöö GPT-5.2 abil

## Sisukord

- [Mida sa õpid](../../../02-prompt-engineering)
- [Eeltingimused](../../../02-prompt-engineering)
- [Promptide inseneritöö mõistmine](../../../02-prompt-engineering)
- [Promptide inseneritöö alused](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompti mallid](../../../02-prompt-engineering)
- [Täpsemad mustrid](../../../02-prompt-engineering)
- [Olemasolevate Azure'i ressursside kasutamine](../../../02-prompt-engineering)
- [Rakenduse ekraanipildid](../../../02-prompt-engineering)
- [Mustrid lahti harutamas](../../../02-prompt-engineering)
  - [Madal vs kõrge innukus](../../../02-prompt-engineering)
  - [Töö täideviimine (tööriistade eessõnad)](../../../02-prompt-engineering)
  - [Enesepeegeldav kood](../../../02-prompt-engineering)
  - [Struktureeritud analüüs](../../../02-prompt-engineering)
  - [Mitme vooru vestlus](../../../02-prompt-engineering)
  - [Samm-sammuline järeldamine](../../../02-prompt-engineering)
  - [Piiratud väljund](../../../02-prompt-engineering)
- [Mida sa tegelikult õpid](../../../02-prompt-engineering)
- [Järgmised sammud](../../../02-prompt-engineering)

## Mida sa õpid

<img src="../../../translated_images/et/what-youll-learn.c68269ac048503b2.webp" alt="Mida sa õpid" width="800"/>

Eelnenud moodulis nägid, kuidas mälu võimaldab vestlus-AI-d ja kasutasid GitHubi mudeleid põhitegevusteks. Nüüd keskendume sellele, kuidas sa küsid küsimusi — promptide endi kasutamisele — Azure OpenAI GPT-5.2 abil. Kuidas sa oma promptid struktureerid, mõjutab oluliselt vastuste kvaliteeti. Alustame põhitehnikate ülevaatega, siis liigume kaheksa täpsema mustri juurde, mis kasutavad GPT-5.2 võimalusi maksimaalselt ära.

Kasutame GPT-5.2, sest see tutvustab järelduste kontrolli — saad mudelile öelda, kui palju mõtlemist enne vastamist teha. See teeb erinevad promptimise strateegiad selgemaks ja aitab mõista, millal kasutada kumbagi lähenemist. Samuti saab kasu Azure'i madalamatest sageduspiirangutest GPT-5.2 puhul võrreldes GitHubi mudelitega.

## Eeltingimused

- Lõpetatud Moodul 01 (Azure OpenAI ressursid paigaldatud)
- Juurkaustas `.env` fail Azure'i mandaatidega (loodud `azd up` käivitamisel Moodulis 01)

> **Märkus:** Kui sa pole Moodulit 01 lõpetanud, järgi esmalt seal olevaid paigaldusjuhiseid.

## Promptide inseneritöö mõistmine

<img src="../../../translated_images/et/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Mis on promptide inseneritöö?" width="800"/>

Promptide inseneritöö tähendab sisendteksti kujundamist nii, et see pidevalt annab vajaliku tulemuse. See ei ole lihtsalt küsimuste esitamises — vaid taotluste struktureerimises nii, et mudel mõistaks täpselt, mida sa tahad ja kuidas seda esitada.

Mõtle sellele kui juhiste andmisele kolleegile. „Paranda viga“ on üldine. „Paranda nullviide UserService.java faili 45. real, lisades nullkontrolli“ on konkreetne. Keelemudelid töötavad samamoodi — täpsus ja struktuur on olulised.

<img src="../../../translated_images/et/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kuidas LangChain4j sobitub" width="800"/>

LangChain4j pakub infrastruktuuri — mudelühendused, mälu ja sõnumitüübid — samas kui promptide mustrid on lihtsalt hoolikalt struktureeritud tekstid, mida selle kaudu saadad. Põhikomponendid on `SystemMessage` (mis määrab tehisintellekti käitumise ja rolli) ja `UserMessage` (mis kannab sinu päris taotlust).

## Promptide inseneritöö alused

<img src="../../../translated_images/et/five-patterns-overview.160f35045ffd2a94.webp" alt="Viie promptide inseneritöö mustri ülevaade" width="800"/>

Enne kui sukelduda selle mooduli täpsematesse mustritesse, vaatame läbi viis põhitehnikat. Need on ehitusplokid, mida iga promptide insener peaks teadma. Kui oled juba kasutanud [Kiirstart moodulit](../00-quick-start/README.md#2-prompt-patterns), siis oled neid näinud praktikas — siin on nende kontseptuaalne raamistik.

### Zero-Shot Prompting

Kõige lihtsam lähenemine: anna mudelile otsene juhis ilma näideteta. Mudel tugineb täielikult oma treeningule, et mõista ja täita ülesannet. See toimib hästi lihtsate taotluste puhul, kus oodatav käitumine on selge.

<img src="../../../translated_images/et/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Otsene juhis ilma näideteta — mudel tuletab ülesande juhisest iseseisvalt*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Vastus: "Positiivne"
```
  
**Millal kasutada:** Lihtsad klassifikatsioonid, otsesed küsimused, tõlked või mis tahes ülesanded, mida mudel saab ilma lisajuhisteta lahendada.

### Few-Shot Prompting

Esita näited, mis demonstreerivad mustrit, mida soovid mudelil järgida. Mudel õpib sinu näidetest sisend-väljund formaadi ja rakendab seda uutele andmetele. See parandab oluliselt järjepidevust ülesannetes, kus soovitud käitumine või formaat ei ole ilmne.

<img src="../../../translated_images/et/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Õppimine näidete põhjal — mudel tuvastab mustri ja rakendab seda uutele sisenditele*

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
  
**Millal kasutada:** Kohandatud klassifikatsioonid, järjepidev vormindamine, valdkonnapõhised ülesanded või kui zero-shot tulemused on ebajärjekindlad.

### Chain of Thought

Palunud mudelil avaldada samm-sammuline mõttekäik. Selle asemel, et kohe vastata, jagab mudel probleemi osadeks ja töötab läbi iga sammu eraldi. See parandab täpsust matemaatika, loogika ja mitmesammuliste järelduste puhul.

<img src="../../../translated_images/et/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Samm-sammuline mõtlemine — keerukate probleemide jagamine selgeteks loogilisteks sammudeks*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Mudel näitab: 15 - 8 = 7, seejärel 7 + 12 = 19 õuna
```
  
**Millal kasutada:** Matemaatikaülesanded, loogikapähklid, silumine või mis tahes ülesanded, kus mõtlemisprotsessi näitamine parandab täpsust ja usaldusväärsust.

### Role-Based Prompting

Lisa AI-le enne küsimuse esitamist isikupära või roll. See lisab konteksti, mis mõjutab vastuse tooni, sügavust ja fookust. „Tarkvara arhitekt“ annab erinevat nõu kui „algaja arendaja“ või „julgeolekuauditor“.

<img src="../../../translated_images/et/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Konteksti ja isiku määramine — sama küsimuse vastus sõltub määratud rollist*

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
  
**Millal kasutada:** Koodikontrollid, juhendamine, valdkonnapõhine analüüs või kui vajad vastuseid konkreetse ekspertiisi taseme või vaatenurga järgi.

### Prompti mallid

Loo taaskasutatavad promptid muutujaplekstidega. Selle asemel, et kirjutada iga kord uus prompt, defineeri mall üks kord ja täida see erinevate väärtustega. LangChain4j `PromptTemplate` klass teeb selle lihtsaks `{{variable}}` süntaksiga.

<img src="../../../translated_images/et/prompt-templates.14bfc37d45f1a933.webp" alt="Prompti mallid" width="800"/>

*Taaskasutatavad promptid muutujaplekstidega — üks mall, palju kasutusi*

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
  
**Millal kasutada:** Korduvad päringud erinevate sisenditega, partiitöötlus, taaskasutatavate AI töölõikude loomine või mis tahes olukorras, kus prompti struktuur jääb samaks, kuid andmed muutuvad.

---

Need viis alust annavad sulle tugeva tööriistakasti enamike promptimiste ülesannete jaoks. Selle mooduli ülejäänud osa tugineb neile kaheksa täiustatud mustriga, mis kasutavad GPT-5.2 mõtlemiskontrolli, enesehindamist ja struktureeritud väljundivõimalusi.

## Täpsemad mustrid

Põhialused kaetud, vaatame kaheksat täiustatud mustrit, mis teevad selle mooduli ainulaadseks. Mitte iga probleem ei vaja sama lähenemist. Mõned küsimused vajavad kiireid vastuseid, teised sügavat mõtlemist. Mõned vajavad nähtavat loogikat, teised vaid tulemust. Iga allolev muster on optimeeritud eri olukorrale — ning GPT-5.2 mõtlemiskontroll muudab erinevused veelgi selgemaks.

<img src="../../../translated_images/et/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Kaheksa promptimise mustrit" width="800"/>

*Ülevaade kaheksast promptide inseneritöö mustrist ja nende kasutusjuhtudest*

<img src="../../../translated_images/et/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Mõtlemise kontroll GPT-5.2-ga" width="800"/>

*GPT-5.2 mõtlemiskontroll võimaldab sul määrata, kui palju mudel peaks mõtlema — alates kiiretest otsestest vastustest kuni põhjaliku uurimiseni*

<img src="../../../translated_images/et/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Mõtlemise pingutuse võrdlus" width="800"/>

*Madal innukus (kiire, otsene) vs kõrge innukus (põhjalik, uuriv) mõtlemislähenemised*

**Madal innukus (kiire ja fookustatud)** - Lihtsate küsimuste jaoks, kus tahad kiireid ja otseseid vastuseid. Mudel teeb minimaalselt mõtlemist — maksimaalselt 2 sammu. Kasuta seda arvutusteks, otsinguteks või lihtsate küsimuste puhul.

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
> - "Mis vahe on madala ja kõrge innukusega promptide mustritel?"  
> - "Kuidas XML sildid promptides aitavad AI vastust struktureerida?"  
> - "Millal kasutada enesepeegeldavaid mustreid vs otsest juhist?"

**Kõrge innukus (sügav ja põhjalik)** - keeruliste probleemide jaoks, kus soovid põhjalikku analüüsi. Mudel uurib teemad põhjalikult ja näitab üksikasjalikku mõttekäiku. Kasuta seda süsteemide projekteerimiseks, arhitektuuriliste otsuste või keeruliste uurimistööde puhul.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**Töö täideviimine (samm-sammuline edenemine)** - mitmesammuliste töölõikude jaoks. Mudel esitab esialgse plaani, jutustab läbi iga sammu, seejärel annab kokkuvõtte. Kasuta seda migratsioonide, rakenduste või mis tahes mitmesammuliste protsesside jaoks.

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
  
Chain-of-Thought promptimist palub mudelil selgelt näidata oma mõttekäiku, mis parandab täpsust keerukatel ülesannetel. Samm-sammuline lagundamine aitab nii inimestel kui AI-l mõista loogikat.

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chatiga:** Küsi selle mustri kohta:  
> - "Kuidas kohandada töö täideviimise mustrit pikaajaliste operatsioonide jaoks?"  
> - "Millised on parimad praktikad tööriistade eessõnade struktureerimiseks tootmiskeskkonnas?"  
> - "Kuidas saaksin UI-s salvestada ja kuvada vahepealseid edenemisaruandeid?"

<img src="../../../translated_images/et/task-execution-pattern.9da3967750ab5c1e.webp" alt="Töö täideviimise muster" width="800"/>

*Plaani → Täida → Kokkuvõtte tee mitmesammulistele ülesannetele*

**Enesepeegeldav kood** - tootmiskvaliteediga koodi genereerimiseks. Mudel genereerib koodi, mis vastab tootmisstandarditele ja sisaldab korralikku vigade käsitlemist. Kasuta seda uute funktsioonide või teenuste loomisel.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/et/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Enesepeegeldamise tsükkel" width="800"/>

*Iteratiivne täiustamise tsükkel – genereeri, hindan, tuvastan probleemid, parandan, korda*

**Struktureeritud analüüs** - järjepidevaks hindamiseks. Mudel vaatab koodi üle fikseeritud raamistiku alusel (õigsus, praktikad, jõudlus, turvalisus, hooldatavus). Kasuta seda koodikontrolliks või kvaliteedi hindamiseks.

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
  
> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chatiga:** Küsi struktureeritud analüüsi kohta:  
> - "Kuidas kohandada analüüsiraamistikku erinevate koodikontrollide jaoks?"  
> - "Mis on parim viis struktureeritud väljundi programmi teel töötlemiseks ja kasutamiseks?"  
> - "Kuidas tagada järjepidevad tõsidustasemed erinevates kontrollides?"

<img src="../../../translated_images/et/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Struktureeritud analüüsi muster" width="800"/>

*Raamistik järjepidevate koodikontrollide jaoks tõsidustasemete kaasamisega*

**Mitme vooru vestlus** - vestlusteks, mis vajavad konteksti. Mudel mäletab eelnevaid sõnumeid ja ehitab nende peale. Kasuta interaktiivseteks abiseanssideks või keerukateks küsimusteks-vastusteks.

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

**Samm-sammuline järeldamine** - probleemide jaoks, mis nõuavad nähtavat loogikat. Mudel näitab iga sammu selget mõttekäiku. Kasuta matemaatikaülesannete, loogikapähklite või siis, kui vajad mõtlemisprotsessi mõistmist.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/et/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Samm-sammuline muster" width="800"/>

*Probleemide jagamine loogilisteks sammudeks*

**Piiratud väljund** - vastustele, mis peavad järgima spetsiifilisi formaadireegleid. Mudel järgib rangelt formaati ja pikkuse nõudeid. Kasuta kokkuvõtete puhul või kui vajad täpset väljundi struktuuri.

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

*Spetsiifiliste formaadi, pikkuse ja struktuuri nõuete järgimine*

## Olemasolevate Azure'i ressursside kasutamine

**Kontrolli paigaldust:**

Veendu, et juurkaustas on olemas `.env` fail Azure'i mandaatidega (loodi Moodulis 01):
```bash
cat ../.env  # Tõrkeavaldus peaks kuvama AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Alusta rakendust:**

> **Märkus:** Kui oled juba avanud kõik rakendused käsuga `./start-all.sh` Moodulis 01, töötab see moodul juba pordil 8083. Võid vahele jätta alltoodud käivituskäsud ja minna otse lehele http://localhost:8083.

**Variant 1: Kasutades Spring Boot Dashboardi (Soovitatav VS Code kasutajatele)**

Arenduskonteiner sisaldab Spring Boot Dashboard laiendust, mis pakub visuaalset liidest kõigi Spring Boot rakenduste haldamiseks. Leiad selle VS Code’i vasakpoolsest menüüribast (otsi Spring Boot ikooni).
Spring Booti juhtpaneelilt saate:
- Näha kõiki tööruumis saadaolevaid Spring Booti rakendusi
- Käivitada/peatada rakendusi ühe klõpsuga
- Vaadata rakenduse logisid reaalajas
- Jälgida rakenduse olekut

Lihtsalt klõpsake "prompt-engineering" kõrval olevale esitamisnupule, et see moodul käivitada, või käivitage korraga kõik moodulid.

<img src="../../../translated_images/et/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Valik 2: Shell-skriptide kasutamine**

Käivitage kõik veebirakendused (moodulid 01-04):

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

Mõlemad skriptid laevad automaatselt keskkonnamuutujad juurkataloogi `.env` failist ja koostavad JAR-failid, kui neid pole olemas.

> **Märkus:** Kui soovite enne käivitamist kõik moodulid käsitsi koostada:
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

Avage oma brauseris http://localhost:8083.

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

*Põhijuhpaneel, mis kuvab kõiki 8 prompt-engineering-mustrit koos nende omaduste ja kasutusjuhtudega*

## Musterde uurimine

Veebiliides võimaldab teil katsetada erinevaid prompt’i strateegiaid. Iga muster lahendab erinevaid probleeme — proovige neist, et näha, millal iga lähenemine silma paistab.

### Madal vs Kõrge Tarmukus

Esitage lihtne küsimus nagu „Mis on 15% 200-st?“ kasutades Madalat Tarmukust. Saate kohese ja otsese vastuse. Nüüd küsige midagi keerukat, näiteks „Disainige kõrge liiklusega API puhul vahemällu salvestamise strateegia“ kasutades Kõrget Tarmukust. Vaadake, kuidas mudel aeglustub ja annab üksikasjaliku põhjenduse. Sama mudel, sama küsimuse struktuur – aga prompt ütleb, kui palju mõtlemist ta tegema peab.

<img src="../../../translated_images/et/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Kiire arvutus minimaalse põhjendusega*

<img src="../../../translated_images/et/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Terviklik vahemällu salvestamise strateegia (2.8MB)*

### Ülesande täitmine (Tööriistade sissejuhatused)

Mitmeastmelised töövood vajavad eelplaneerimist ja progressi jutustamist. Mudel kirjeldab, mida teeb, jutustab iga sammuga läbi ja võtab tulemused kokku.

<img src="../../../translated_images/et/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*REST lõpp-punkti loomine samm-sammult jutustades (3.9MB)*

### Enesepeegeldav kood

Proovige „Looge e-posti valideerimise teenus“. Selle asemel, et lihtsalt koodi genereerida ja seista jääda, genereerib mudel, hindab kvaliteedikriteeriumite järgi, tuvastab nõrkused ja täiustab. Näete, kuidas see iteratiivselt töötab kuni kood vastab tootmisstandarditele.

<img src="../../../translated_images/et/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Täielik e-posti valideerimise teenus (5.2MB)*

### Struktureeritud analüüs

Koodiarvustused vajavad järjepidevaid hindamiskorraldusi. Mudel analüüsib koodi fikseeritud kategooriate (õigsus, praktikad, jõudlus, turvalisus) ja raskusastmete alusel.

<img src="../../../translated_images/et/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Koodiarvustus raamistikupõhiselt*

### Mitmekäiguline vestlus

Küsige „Mis on Spring Boot?“ ja kohe seejärel „Näita mulle näidet“. Mudel mäletab teie esimest küsimust ja annab teile just Spring Booti näite. Ilma mäluta oleks see teine küsimus liiga üldine.

<img src="../../../translated_images/et/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Konteksti säilitamine küsimuste vahel*

### Samm-sammult põhjendamine

Valige matemaatikaülesanne ja proovige seda nii Samm-sammult Põhjendamise kui ka Madala Tarmukusega. Madal tarmukus annab lihtsalt vastuse – kiire, kuid varjatud. Samm-sammult näitab teile iga arvutuse ja otsuse.

<img src="../../../translated_images/et/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matemaatikaülesanne koos selgete sammudega*

### Piiratud väljund

Kui vajate konkreetseid vorminguid või sõnaloendit, sunnib see muster rangelt neid järgima. Proovige genereerida kokkuvõte täpselt 100 sõnaga ja punktformaadis.

<img src="../../../translated_images/et/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Masinõppe kokkuvõte vormingu kontrolliga*

## Mida te tegelikult õpite

**Põhjenduse pingutus muudab kõik**

GPT-5.2 võimaldab teil oma prompt’i kaudu kontrollida arvutuspingutust. Madal pingutus tähendab kiireid vastuseid minimaalse uurimisega. Kõrge pingutus tähendab, et mudel võtab aega sügavalt mõtlemiseks. Õpite pingutust ülesande keerukusele sobitama – ärge raisake aega lihtsate küsimuste peale, kuid ärge kiirustage ka keerukate otsustega.

**Struktuur juhib käitumist**

Kas märkasite prompt’ides XML-täppe? Need ei ole dekoratiivsed. Mudelid järgivad struktureeritud juhiseid usaldusväärsemalt kui vaba teksti. Kui vajate mitmeastmelisi protsesse või keerukat loogikat, aitab struktuur mudelil jälgida, kus ta on ja mis järgneb.

<img src="../../../translated_images/et/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Hea struktureeritud prompt’i anatoomia, selgete osade ja XML-laadse korraldusega*

**Kvaliteet enesehindamise kaudu**

Enesepeegeldavad mustrid töötavad, tehes kvaliteedikriteeriumid selgeks. Selle asemel, et loota, et mudel „teeb õigesti“, ütlete talle täpselt, mida „õige“ tähendab: õige loogika, veahaldus, jõudlus, turvalisus. Mudel saab seejärel oma väljundit hinnata ja täiustada. See muudab koodigeneratsiooni loteriist protsessiks.

**Kontekst on piiratud**

Mitmekäigulised vestlused toimivad, kui iga päringuga kaasas on sõnumite ajalugu. Aga on piirang – iga mudelil on maksimaalne tokenite arv. Kui vestlused kasvavad, vajate strateegiaid, et säilitada oluline kontekst ilma seda piiri ületamata. See moodul näitab teile, kuidas mälu töötab; hiljem õpite, millal kokkuvõtteid teha, millal unustada ja millal otsida.

## Järgmised sammud

**Järgmine moodul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigeerimine:** [← Eelmine: Moodul 01 - Sissejuhatus](../01-introduction/README.md) | [Tagasi põhiaknasse](../README.md) | [Järgmine: Moodul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:
See dokument on tõlgitud kasutades tehisintellektil põhinevat tõlke teenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi püüame täpsust, palun pidage meeles, et automaatsed tõlked võivad sisaldada vigu või ebatäpsusi. Originaaldokument selle emakeeles tuleks pidada autoriteetseks allikaks. Olulise info puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta ühegi arusaamatuse või valesti mõistmise eest, mis tuleneb selle tõlke kasutamisest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->