# Moodul 02: Prompt-ehitamine GPT-5.2-ga

## Sisukord

- [Videojuhend](../../../02-prompt-engineering)
- [Mida Sa Õpid](../../../02-prompt-engineering)
- [Eeltingimused](../../../02-prompt-engineering)
- [Prompt-ehituse Mõistmine](../../../02-prompt-engineering)
- [Prompt-ehituse Alused](../../../02-prompt-engineering)
  - [Zero-Shot Põhimõte](../../../02-prompt-engineering)
  - [Few-Shot Põhimõte](../../../02-prompt-engineering)
  - [Mõttelaine Jada](../../../02-prompt-engineering)
  - [Rollipõhine Promptimine](../../../02-prompt-engineering)
  - [Prompt-mallid](../../../02-prompt-engineering)
- [Edasijõudnud Mustrid](../../../02-prompt-engineering)
- [Olemasolevate Azure Ressursside Kasutamine](../../../02-prompt-engineering)
- [Rakenduse Ekraanipildid](../../../02-prompt-engineering)
- [Mustrite Uurimine](../../../02-prompt-engineering)
  - [Madal vs Kõrge Töövõime](../../../02-prompt-engineering)
  - [Tööülesannete Täitmine (Tööriistade Sissejuhatused)](../../../02-prompt-engineering)
  - [Isetutvustav Kood](../../../02-prompt-engineering)
  - [Struktureeritud Analüüs](../../../02-prompt-engineering)
  - [Mitme-Korra Vestlus](../../../02-prompt-engineering)
  - [Samm-Sammult Mõtlemine](../../../02-prompt-engineering)
  - [Piiratud Väljund](../../../02-prompt-engineering)
- [Mida Sa Tõeliselt Õpid](../../../02-prompt-engineering)
- [Järgmised Sammud](../../../02-prompt-engineering)

## Videojuhend

Vaata seda otseülekannet, mis selgitab, kuidas selle mooduliga alustada: [Prompt Engineering with LangChain4j - Live Session](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## Mida Sa Õpid

<img src="../../../translated_images/et/what-youll-learn.c68269ac048503b2.webp" alt="Mida Sa Õpid" width="800"/>

Eelmises moodulis nägid, kuidas mälu võimaldab vestlus-IA-d ja kasutasid GitHubi mudeleid põhisuhtluseks. Nüüd keskendume sellele, kuidas küsida küsimusi — ehk promptide endi kasutamisele — kasutades Azure OpenAI GPT-5.2. Kuidas sa promptid struktureerid, mõjutab oluliselt vastuste kvaliteeti. Alustame põhiliste promptimisvõtete ülevaatega ja liigume siis kaheksa edasijõudnud mustri juurde, mis kasutavad GPT-5.2 täielikku potentsiaali.

Kasutame GPT-5.2, sest see omab mõtlemise juhtimise funktsiooni — saad mudelile öelda, kui palju peaks enne vastamist mõtlema. See teeb erinevad promptimisstrateegiad selgemaks ja aitab mõista, millal mida kasutada. Samuti saame kasu Azure väiksematest kiirusepiirangutest GPT-5.2 puhul võrreldes GitHubi mudelitega.

## Eeltingimused

- Moodul 01 lõpetatud (Azure OpenAI ressursid juurutatud)
- `.env` fail juurkataloogis Azure volitustega (moodul 01 `azd up` loodud)

> **Märkus:** Kui sa pole veel moodulit 01 lõpetanud, järgi esmalt seal olevaid juurutamisjuhiseid.

## Prompt-ehituse Mõistmine

<img src="../../../translated_images/et/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Mis on Prompt-ehitus?" width="800"/>

Prompt-ehitamine tähendab sisendi teksti kujundamist nii, et see pidevalt tagab vajalikud tulemused. See pole ainult küsimuste esitamine — see tähendab taotluste struktureerimist nii, et mudel mõistaks täpselt, mida soovid ja kuidas seda pakkuda.

Mõtle nagu juhendad kolleegi. „Paranda viga“ on ebamäärane. „Paranda UserService.java faili 45. reale nullpunkti tõrge lisades null-kontrolli“ on konkreetne. Keelemudelid töötavad samamoodi — spetsiifilisus ja struktuur on olulised.

<img src="../../../translated_images/et/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kuidas LangChain4j Sobib" width="800"/>

LangChain4j pakub taristut — mudelühendusi, mälu ja sõnumitüüpe — samal ajal kui promptimustritena on täpselt struktureeritud tekst, mida selle taristu kaudu saadad. Peamised ehitusplokid on `SystemMessage` (mille kaudu määratakse tehisintellekti käitumine ja roll) ja `UserMessage` (mis kannab sinu tegelikku taotlust).

## Prompt-ehituse Alused

<img src="../../../translated_images/et/five-patterns-overview.160f35045ffd2a94.webp" alt="Viie Prompt-ehituse Mustri Ülevaade" width="800"/>

Enne selle mooduli edasijõudnumate mustrite juurde asumist vaatame üle viis põhilist promptimisvõtet. Need on ehitusplokid, mida iga prompti insener peaks tundma. Kui oled juba läbinud [Quick Start mooduli](../00-quick-start/README.md#2-prompt-patterns), oled neid näinud tegutsemas — siin on nende taga olev kontseptuaalne raamistik.

### Zero-Shot Põhimõte

Lihtsaim lähenemine: anna mudelile otsene juhis ilma näideteta. Mudel tugineb täielikult enda treeningule, et mõista ja ülesannet täita. Sobib hästi otsekohesteks ülesanneteks, kus oodatav käitumine on ilmne.

<img src="../../../translated_images/et/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Põhimõte" width="800"/>

*Otsene juhis ilma näideteta — mudel järeldab ülesande ainult juhisest*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Vastus: "Positiivne"
```

**Kasutamiseks, kui:** Lihtsad klassifikatsioonid, otsesed küsimused, tõlked või mis tahes ülesanded, mida mudel saab ilma täiendava juhiseta täita.

### Few-Shot Põhimõte

Esita näited, mis näitavad mustrit, mida soovid mudelilt järgida. Mudel õpib oodatud sisendi-väljundi vormingu sinu näidetest ja rakendab seda uutele sisenditele. See parandab konsistentsust ülesannetes, kus soovitud vorming või käitumine pole nähtav.

<img src="../../../translated_images/et/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Põhimõte" width="800"/>

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

**Kasutamiseks, kui:** Kohandatud klassifikatsioonid, järjepidev vormindus, valdkonnapõhised ülesanded või kui zero-shot tulemused on ebakindlad.

### Mõttelaine Jada

Paluge mudelil näidata oma mõtlemist samm-sammult. Selle asemel, et kohe vastata, laguneb mudel probleemi detailseks lahenduseks ja töötab iga osa läbi eraldi. See parandab täpsust matemaatika, loogika ja mitmeastmeliste mõtlemisülesannete puhul.

<img src="../../../translated_images/et/chain-of-thought.5cff6630e2657e2a.webp" alt="Mõttelaine Jada Promptimine" width="800"/>

*Samm-sammult mõtlemine — keeruliste probleemide jagamine loogilisteks sammudeks*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Mudel näitab: 15 - 8 = 7, siis 7 + 12 = 19 õuna
```

**Kasutamiseks, kui:** Matemaatikaprobleemid, loogikapähklid, silumine või mis tahes ülesanne, kus mõtlemisprotsessi näitamine parandab täpsust ja usaldust.

### Rollipõhine Promptimine

Sea enne küsimust AI-le persona või roll. See annab konteksti, mis mõjutab vastuse tooni, sügavust ja fookust. „Tarkvara arhitekt“ annab erinevaid nõuandeid kui „alglaadija arendaja“ või „turbe-audiitor“.

<img src="../../../translated_images/et/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rollipõhine Promptimine" width="800"/>

*Konteksti ja persona määramine — sama küsimus saab erineva vastuse, sõltuvalt määratud rollist*

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

**Kasutamiseks, kui:** Koodi ülevaated, juhendamine, valdkonnapõhine analüüs või kui vajad vastuseid, mis on kohandatud kindlale asjatundlikkuse tasemele või vaatenurgale.

### Prompt-mallid

Loo korduvkasutatavad promptid muutujate kohtadega. Selle asemel, et iga kord uut prompti kirjutada, defineeri mall ja täida erinevad väärtused. LangChain4j `PromptTemplate` klass teeb selle lihtsaks `{{variable}}` süntaksiga.

<img src="../../../translated_images/et/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt-mallid" width="800"/>

*Korduvkasutatavad promptid muutuste kohtadega — üks mall, palju kasutusvõimalusi*

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

**Kasutamiseks, kui:** Korduvad päringud erinevate sisenditega, partiitöötlus, korduvkasutatavad IA töövood või olukorrad, kus prompti struktuur jääb samaks, kuid andmed muutuvad.

---

Need viis alust annavad tugeva tööriistakomplekti enamiku promptimisülesannete jaoks. Käesolev moodul ehitab nendele peale kaheksale edasijõudnud mustrile, mis kasutavad GPT-5.2 mõtlemise juhtimist, enesehindamist ja struktureeritud väljundi võimekust.

## Edasijõudnud Mustrid

Kui alused on läbi käidud, liigume kaheksale edasijõudnud mustrile, mis teevad selle mooduli eriliseks. Mitte iga probleem ei vaja sama lähenemist. Mõned küsimused vajavad kiireid vastuseid, teised sügavat mõtlemist. Mõned vajavad nähtavat mõtlemist, teised ainult tulemusi. Iga allolev mustritüüp on optimeeritud eri olukorrale — ja GPT-5.2 mõtlemise juhtimise funktsioon toob erinevused eriti selgelt esile.

<img src="../../../translated_images/et/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Kaheksa Prompt-ehituse Mustrit" width="800"/>

*Kaheksa prompt-ehituse mustri ülevaade ja nende kasutusvaldkonnad*

<img src="../../../translated_images/et/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Mõtlemise Juhtimine GPT-5.2-ga" width="800"/>

*GPT-5.2 mõtlemise juhtimine võimaldab määrata, kui palju mudel peaks mõtlema — alates kiiretest otsestest vastustest kuni sügava uurimiseni*

**Madal Töövõime (Kiire & Fookustatud)** - Lihtsate küsimuste jaoks, kus sa soovid kiireid ja otseseid vastuseid. Mudel teeb minimaalselt mõtlemist — maksimaalselt 2 sammu. Kasuta seda arvutuste, otsingute või lihtsate küsimuste puhul.

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
> - „Mis vahe on madala ja kõrge töövõimega promptimismustritel?“
> - „Kuidas XML sildid promptides aitavad AI vastust struktureerida?“
> - „Millal kasutada isereflekseerimise mustreid ja millal otsest juhist?“

**Kõrge Töövõime (Sügav & Põhjalik)** - Komplekssete probleemide puhul, kus soovid põhjalikku analüüsi. Mudel uurib põhjalikult ja näitab detailset mõtlemist. Kasuta seda süsteemide planeerimisel, arhitektuurivalikutes või keerulises uurimistöös.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Tööülesannete Täitmine (Samm-sammuline Edasiminek)** - Mitmeastmeliste töövoogude jaoks. Mudel pakub algplaani, kirjeldab iga sammu töö kaudu, seejärel annab kokkuvõtte. Kasuta seda migratsioonidel, rakendamisülesannetes või mitmeastmelistes protsessides.

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

Mõttelaine jada promptimine palub mudelil ekspliciitselt näidata oma mõtlemisprotsessi, parandades keerukate ülesannete täpsust. Samm-sammuline jaotus aitab nii inimestel kui ka tehisintellektil mõista loogikat.

> **🤖 Proovi GitHub Copilot Chatiga:** Küsi selle mustri kohta:
> - „Kuidas kohandada tööülesannete täitmise mustrit pikaajaliste operatsioonide jaoks?“
> - „Millised on parimad praktikad tööriistade sissejuhatuste struktuurimiseks tootmiskeskkonnas?“
> - „Kuidas tabada ja kuvada vaheskäigu edenemise uuendusi kasutajaliideses?“

<img src="../../../translated_images/et/task-execution-pattern.9da3967750ab5c1e.webp" alt="Tööülesannete Täitmise Muster" width="800"/>

*Plaani → Täida → Kokkuvõtlik töövoog mitmeastmeliste ülesannete jaoks*

**Isetutvustav Kood** - Tootmiskvaliteediga koodi genereerimiseks. Mudel genereerib koodi, järgides tootmisstandardeid ja korrapärast veahaldust. Kasuta seda uute funktsioonide või teenuste arendamisel.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/et/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Isetutvustuse Tsükkel" width="800"/>

*Iteratiivne täiustamise tsükkel - genereeri, hinda, leia probleemid, paranda, korda*

**Struktureeritud Analüüs** - Järjepideva hindamise jaoks. Mudel hindab koodi fikseeritud raamistikus (täpsus, praktikad, jõudlus, turvalisus, hooldatavus). Kasuta seda koodi ülevaadete või kvaliteedi hindamiseks.

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

> **🤖 Proovi GitHub Copilot Chatiga:** Küsi struktureeritud analüüsi kohta:
> - „Kuidas kohandada analüüsiraamistikku eri tüüpi koodi ülevaadete jaoks?“
> - „Mis on parim viis struktureeritud väljundi programmiliseks parsimiseks ja töötluseks?“
> - „Kuidas tagada järjepidevad raskusastmed erinevate ülevaatesessioonide vahel?“

<img src="../../../translated_images/et/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Struktureeritud Analüüsi Muster" width="800"/>

*Järjepideva koodi ülevaate raamistiku kasutamine raskusastmetega*

**Mitme-Korra Vestlus** - Konteksti vajavate vestluste jaoks. Mudel mäletab varasemaid sõnumeid ja ehitab neile edasi. Kasuta seda interaktiivsete abisessioonide või keeruliste Q&A jaoks.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/et/context-memory.dff30ad9fa78832a.webp" alt="Konteksti Mälu" width="800"/>

*Kuidas vestluse kontekst koguneb mitme käigu jooksul kuni juurdepääsutokenite piirini*

**Samm-Sammult Mõtlemine** - Probleemidele, mis vajavad nähtavat loogikat. Mudel näitab selgelt mõtlemist iga sammu kohta. Kasuta seda matemaatikaülesannete, loogikalõksude või juhul, kui vajad mõtlemisprotsessi mõistmist.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/et/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Samm-Sammult Muster" width="800"/>

*Probleemide lagundamine selgeteks loogilisteks sammudeks*

**Piiratud Väljund** - Vastustele, mis nõuavad kindlat vormingut. Mudel järgib rangelt formaadi ja pikkuse nõudeid. Kasuta seda kokkuvõtete jaoks või kui vajad täpset väljundistruktuuri.

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

<img src="../../../translated_images/et/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Piiratud Väljundi Muster" width="800"/>

*Kindlate formaadi, pikkuse ja struktuuri nõuete rakendamine*

## Olemasolevate Azure Ressursside Kasutamine

**Kontrolli juurutust:**

Veendu, et `.env` fail juurkataloogis sisaldab Azure volitusi (moodulis 01 loodud):
```bash
cat ../.env  # Peaks kuvama AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Alusta rakendust:**

> **Märkus:** Kui oled juba alustanud kõiki rakendusi käsuga `./start-all.sh` moodulist 01, siis see moodul töötab juba pordil 8083. Võid allolevad käivitamiskäsud vahele jätta ja minna otse aadressile http://localhost:8083.

**Variant 1: Spring Boot juhtpaneeli kasutamine (soovitatav VS Code'i kasutajatele)**
Arenduskonteiner sisaldab Spring Boot Dashboard laiendust, mis pakub visuaalset liidest kõigi Spring Boot rakenduste haldamiseks. Selle leiate VS Code'i vasakpoolsest tegevusribast (otsi Spring Boot ikooni).

Spring Boot Dashboardi kaudu saate:
- Näha kõiki tööruumis saadaolevaid Spring Boot rakendusi
- Käivitada/peatada rakendusi ühe klõpsuga
- Vaadata rakenduse logisid reaalajas
- Jälgida rakenduse olekut

Lihtsalt klõpsake nuppu "play" kõrval "prompt-engineering", et see moodul käivitada, või käivitage korraga kõik moodulid.

<img src="../../../translated_images/et/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Valik 2: Kasutades shell-skripte**

Käivitage kõik veebirakendused (moodulid 01-04):

**Bash:**
```bash
cd ..  # Juurekataloogist
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

Mõlemad skriptid laadivad automaatselt keskkonnamuutujad juurkataloogi `.env` failist ning ehitavad JAR-id, kui neid veel ei ole.

> **Märkus:** Kui eelistate enne käivitamist kõik moodulid käsitsi ehitada:
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

*Peamine armatuurlaud, mis kuvab kõiki 8 prompt-disaini mustrit koos nende omaduste ja kasutusjuhtudega*

## Mustrite uurimine

Veebiliides võimaldab teil katsetada erinevaid küsitlusstrateegiaid. Iga muster lahendab erinevaid probleeme – proovige neid, et näha, millal iga lähenemine kõige paremini toimib.

> **Märkus: voogesitus vs mittevoogesitus** — Igal mustri lehel on kaks nuppu: **🔴 Stream Response (Live)** ja **Mittevoogesituse valik**. Voogesitus kasutab Server-Sent Events (SSE), et kuvada mudeeli genereeritavad tokenid reaalajas, nii näete edenemist kohe. Mittevoogesitus ootab terve vastuse ära, enne kui selle kuvab. Sügava mõtlemisega (nt High Eagerness, Self-Reflecting Code) promptid võivad mittevoogesituse korral võtta väga kaua aega – mõnikord minuteid – ilma nähtava tagasisideta. **Kasuta voogesitust keerukate promptidega katsetamisel**, et näha mudeli tööd ja vältida muljet, justkui päring oleks aegunud.
>
> **Märkus: brauseri nõue** — Voogesitusfunktsioon kasutab Fetch Streams API-d (`response.body.getReader()`), mis nõuab täisfunktsionaalset brauserit (Chrome, Edge, Firefox, Safari). See EI toimi VS Code sisseehitatud Simple Browseris, kuna selle veebivaade ei toeta ReadableStream API-d. Kui kasutate Simple Browserit, töötavad mittevoogesituse nupud endiselt normaalselt – ainult voogesituse nupud on mõjutatud. Täieliku kogemuse saamiseks avage `http://localhost:8083` välist brauserit.

### Madal vs Kõrge innukus

Küsige lihtne küsimus nagu "Mis on 15% 200-st?" madala innukuse tasemega. Saate kiire ja otsese vastuse. Nüüd küsige keerulisem küsimus "Disainige vahemälustrateegia suure liiklusega API-le" kõrge innukuse tasemel. Klõpsake **🔴 Stream Response (Live)** ja jälgige, kuidas mudeli üksikasjalik arutelu ilmub token tokeni haaval. Sama mudel, sama küsimuse struktuur – kuid prompt ütleb, kui palju mõtlemist teha.

### Ülesande täitmine (tööriistade sissejuhatused)

Mitmeastmelised töövood saavad kasu etteplaneerimisest ja edenemise jutustamisest. Mudel kirjeldab, mida teeb, tutvustab igat sammu ja seejärel võtab tulemused kokku.

### Enesepeegelduv kood

Proovige "Looge e-kirjade valideerimise teenus". Mudel ei genereeri lihtsalt koodi ja peatu, vaid genereerib, hindab kvaliteedistandardeid, tuvastab nõrkusi ja täiustab. Näete, kuidas ta kordab seni, kuni kood vastab tootmisstandarditele.

### Struktureeritud analüüs

Koodikontrollid vajavad ühtseid hindamiskriteeriume. Mudel analüüsib koodi fikseeritud kategooriates (õigsus, praktikad, jõudlus, turvalisus) koos raskusastme tasemega.

### Mitme vooru vestlus

Küsige "Mis on Spring Boot?" ja kohe seejärel "Näidake mulle näidet". Mudel mäletab teie esimest küsimust ja annab teile spetsiifilise Spring Boot näite. Ilma mäluta oleks teine küsimus liiga ebamäärane.

### Samm-sammuline põhjendus

Valige matemaatikaülesanne ja proovige seda nii samm-sammult põhjendades kui ka madala innukusega. Madal innukus annab lihtsalt kiire ja varjatud vastuse. Samm-sammult näitab kõiki arvutusi ja otsuseid.

### Piiratud väljund

Kui vajate kindlaid vorminguid või sõnade arvu, sunnib see muster ranget järgimist. Proovige genereerida kokkuvõte täpselt 100 sõnaga punktide formaadis.

## Mida te tegelikult õpite

**Põhjendamise jõupingutus muudab kõik**

GPT-5.2 võimaldab teil oma promptide kaudu juhtida arvutuslikku pingutust. Madal pingutus tähendab kiireid vastuseid minimaalse uurimisega. Kõrge pingutus tähendab, et mudel võtab aega sügavaks mõtlemiseks. Õpite sobitama pingutust ülesande keerukusega – ärge raisake aega lihtsatele küsimustele, aga ärge kiirustage ka keeruliste otsuste tegemist.

**Struktuur juhib käitumist**

Kas olete märganud XML-silte promptides? Need ei ole lihtsalt dekoratiivsed. Mudelid järgivad struktureeritud juhiseid palju usaldusväärsemalt kui vabateksti. Kui vajate mitmeastmelisi protsesse või keerukat loogikat, aitab struktuur mudelil jälgida, kus ta on ja mis järgmiseks tuleb.

<img src="../../../translated_images/et/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Hästi struktureeritud prompti anatoomia, kus on selged sektsioonid ja XML-laadne korraldus*

**Kvaliteet enesehindamise kaudu**

Enesepeegeldavad mustrid töötavad, muutes kvaliteedikriteeriumid selgelt väljendatuks. Selle asemel, et loota, et mudel "teeb õigesti", ütlete täpselt, mida "õige" tähendab: õige loogika, veahaldus, jõudlus, turvalisus. Mudel saab siis oma väljundit hinnata ja täiustada. See muudab koodi genereerimise loteriist protsessiks.

**Kontekst on piiratud**

Mitme vooru vestlused toimivad nii, et iga päringu juurde lisatakse sõnumite ajalugu. Kuid on piir – igal mudelil on maksimaalne tokenite arv. Vestluste kasvades vajate strateegiaid, kuidas säilitada asjakohane kontekst ilma seda piirangut ületamata. See moodul näitab teile, kuidas mälu töötab; hiljem õpite, millal kokkuvõtteid teha, millal unustada ja millal taaskasutada.

## Järgmised sammud

**Järgmine moodul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigeerimine:** [← Eelmine: Moodul 01 - Sissejuhatus](../01-introduction/README.md) | [Tagasi põhilehele](../README.md) | [Järgmine: Moodul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:  
See dokument on tõlgitud kasutades tehisintellekti tõlketeenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi me püüame tagada täpsust, palun arvestage, et automatiseeritud tõlgetes võib esineda vigu või ebatäpsusi. Originaaldokument oma emakeeles tuleks pidada autoriteetseks allikaks. Kriitilise teabe puhul soovitatakse professionaalset inimtõlget. Me ei vastuta selle tõlke kasutamisest tingitud arusaamatuste või valesti mõistmiste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->