# Moodul 02: Prompti inseneritöö GPT-5.2-ga

## Sisukord

- [Mida sa õpid](../../../02-prompt-engineering)
- [Eeltingimused](../../../02-prompt-engineering)
- [Prompti inseneritöö mõistmine](../../../02-prompt-engineering)
- [Prompti insenerimise alused](../../../02-prompt-engineering)
  - [Zero-Shot promptimine](../../../02-prompt-engineering)
  - [Few-Shot promptimine](../../../02-prompt-engineering)
  - [Mõttekäikude jada](../../../02-prompt-engineering)
  - [Rollipõhine promptimine](../../../02-prompt-engineering)
  - [Prompti mallid](../../../02-prompt-engineering)
- [Täiustatud mustrid](../../../02-prompt-engineering)
- [Olemasolevate Azure ressursside kasutamine](../../../02-prompt-engineering)
- [Rakenduse ekraanipildid](../../../02-prompt-engineering)
- [Mustrite uurimine](../../../02-prompt-engineering)
  - [Madala vs kõrge innukuse erinevus](../../../02-prompt-engineering)
  - [Ülesande täitmine (tööriistade sissejuhatused)](../../../02-prompt-engineering)
  - [Enesepeegeldav kood](../../../02-prompt-engineering)
  - [Struktureeritud analüüs](../../../02-prompt-engineering)
  - [Mitme vooru jututoa vestlus](../../../02-prompt-engineering)
  - [Samm-sammuline põhjendus](../../../02-prompt-engineering)
  - [Piiratud väljund](../../../02-prompt-engineering)
- [Mida sa tegelikult õpid](../../../02-prompt-engineering)
- [Järgmised sammud](../../../02-prompt-engineering)

## Mida sa õpid

<img src="../../../translated_images/et/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

Eelnevas moodulis nägid, kuidas mälu võimaldab vestluslikku tehisintellekti ning kasutasid GitHubi mudeleid põhiliste interaktsioonide jaoks. Nüüd keskendume sellele, kuidas sa küsimusi esitad — ehk promptidele endile — kasutades Azure OpenAI GPT-5.2. Sinu promptide struktuur mõjutab oluliselt vastuste kvaliteeti. Alustame põhiliste promptimise tehnikate ülevaatega, seejärel liigume edasi kaheksa täiustatud mustri juurde, mis kasutavad GPT-5.2 võimeid täielikult ära.

Kasutame GPT-5.2, sest see toob kaasa mõtlemise kontrolli – saad määrata mudelile, kui palju ta enne vastamist mõtlema peaks. See teeb erinevad promptimisstrateegiad selgemaks ning aitab mõista, millal millist lähenemist kasutada. Samuti saame kasu Azure väiksematest kasutuspiirangutest GPT-5.2 puhul võrreldes GitHubi mudelitega.

## Eeltingimused

- Lõpetatud moodul 01 (Azure OpenAI ressursid juurutatud)
- Juurtasemes olemas `.env` fail Azure mandaadiga (`azd up` moodulis 01 lõi selle automaatselt)

> **Märkus:** Kui sa pole moodulit 01 veel lõpetanud, järgi esmalt sealset juurutusjuhendit.

## Prompti inseneritöö mõistmine

<img src="../../../translated_images/et/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Mis on prompti inseneritöö?" width="800"/>

Prompti inseneritöö tähendab sisendteksti kujundamist nii, et saaksid järjepidevalt soovitud tulemusi. See ei ole pelgalt küsimuste esitamist – see on taotluste struktureerimine nii, et mudel mõistab täpselt, mida sa tahad ja kuidas seda edastada.

Mõtle sellele nagu kolleegile juhiste andmine. "Paranda viga" on ebamäärane. "Paranda nullviidete erind UserService.java failis real 45, lisades nullkontrolli" on konkreetne. Keelemudelid toimivad samamoodi – täpsus ja struktuur on olulised.

<img src="../../../translated_images/et/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Kuidas LangChain4j sobitub" width="800"/>

LangChain4j pakub infrastruktuuri — mudeliga ühendused, mälu ja sõnumitüübid — ning prompti mustrid on lihtsalt hoolikalt struktureeritud tekst, mida selle infrastruktuuri kaudu saadad. Põhikomponendid on `SystemMessage` (mis määrab tehisintellekti käitumise ja rolli) ja `UserMessage` (mis kannab sinu tegelikku päringut).

## Prompti insenerimise alused

<img src="../../../translated_images/et/five-patterns-overview.160f35045ffd2a94.webp" alt="Viie prompti insenerimisemustri ülevaade" width="800"/>

Enne selle mooduli täiustatud mustrite juurde asumist vaatame üle viis põhilist promptimise tehnikat. Need on igale prompti insenerile vajalikud ehituskivid. Kui oled juba [Kiiralgus mooduli](../00-quick-start/README.md#2-prompt-patterns) läbi teinud, siis oled neid näinud tegutsemas — siin on nende kontseptuaalne raamistik.

### Zero-Shot promptimine

Lihtsaim lähenemine: anna mudelile otse juhis ilma näideteta. Mudel tugineb täielikult oma treeningule, et mõista ja täita ülesannet. See toimib hästi lihtsate päringute puhul, kus oodatav käitumine on ilmselge.

<img src="../../../translated_images/et/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot promptimine" width="800"/>

*Otsejuhtimine ilma näideteta — mudel tuletab ülesande vaid juhisest*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Vastus: "Positiivne"
```

**Kasutusjuhud:** Lihtsad klassifikatsioonid, otsesed küsimused, tõlked või kõik ülesanded, mida mudel suudab täita ilma täiendava juhenduseta.

### Few-Shot promptimine

Esita näited, mis demonstreerivad mustrit, mida soovid mudeli järgida. Mudel õpib sinu näidetest sisendi-väljundi formaadi ja rakendab seda uutele sisenditele. See parandab järjepidevust ülesannete puhul, kus soovitud vorming või käitumine pole ilmne.

<img src="../../../translated_images/et/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot promptimine" width="800"/>

*Õppimine näidete põhjal — mudel tunneb mustri ära ja rakendab seda uutele sisenditele*

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

**Kasutusjuhud:** Kohandatud klassifikatsioonid, järjepidev vormindamine, domeenipõhised ülesanded või kui zero-shot tulemused on ebajärjekindlad.

### Mõttekäikude jada

Paluge mudelil näidata oma põhjendus samm-sammult. Selle asemel, et kohe vastusele hüpata, jagab mudel ülesande osadeks ja töötleb neid ükshaaval. See parandab täpsust matemaatika, loogika ja mitmeastmeliste mõttekäikude puhul.

<img src="../../../translated_images/et/chain-of-thought.5cff6630e2657e2a.webp" alt="Mõttekäikude jada" width="800"/>

*Samm-sammuline põhjendus — keeruliste probleemide lahendamine selgete loogiliste sammudena*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Mudel näitab: 15 - 8 = 7, siis 7 + 12 = 19 õuna
```

**Kasutusjuhud:** Matemaatikaülesanded, loogikapähklid, silumine või kõik ülesanded, kus põhjendusprotsessi näitamine parandab täpsust ja usaldusväärsust.

### Rollipõhine promptimine

Sea AI-le persona või roll enne küsimuse esitamist. See annab konteksti, mis mõjutab vastuse tooni, sügavust ja fookust. Näiteks "tarkvara arhitekt" annab erinevat nõu kui "noorem arendaja" või "turvaeestvaataja".

<img src="../../../translated_images/et/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rollipõhine promptimine" width="800"/>

*Konteksti ja persona seadmine — sama küsimus saab erineva vastuse vastavalt määratud rollile*

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

**Kasutusjuhud:** Koodi ülevaated, juhendamine, domeenipõhine analüüs või kui vajad vastuseid, mis on kohandatud konkreetsele eksperditasemele või perspektiivile.

### Prompti mallid

Loo korduvkasutatavad promptid muutuvate kohtadega. Uue prompti kirjutamise asemel defineeri üks mall ning täida see erinevate väärtustega. LangChain4j `PromptTemplate` klass teeb seda lihtsaks `{{muutuja}}` süntaksiga.

<img src="../../../translated_images/et/prompt-templates.14bfc37d45f1a933.webp" alt="Prompti mallid" width="800"/>

*Korduvkasutatavad promptid muutuvate kohtadega — üks mall, palju kasutusvõimalusi*

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

**Kasutusjuhud:** Korduvad päringud erinevate sisenditega, partii töötlemine, korduvkasutatavate tehisintellekti töövoogude loomine või kõik olukorrad, kus prompti struktuur jääb samaks, kuid andmed muutuvad.

---

Need viis põhitõde annavad sulle tugeva tööriistakomplekti enamiku promptimise ülesannete jaoks. Selle mooduli ülejäänud osa loobub neist kaheksa täiustatud mustriga, mis kasutavad GPT-5.2 mõtlemise kontrolli, enesehindamise ja struktureeritud väljundit.

## Täiustatud mustrid

Pärast põhialuste katmist liigume kaheksa täiustatud mustrini, mis teevad selle mooduli eriliseks. Kõik probleemid ei vaja sama lähenemist. Mõned küsimused vajavad kiireid vastuseid, teised põhjalikku mõtlemist. Mõned nõuavad nähtavat põhjendust, teised ainult tulemusi. Iga alljärgnev muster on optimeeritud erineva stsenaariumi jaoks — ja GPT-5.2 mõtlemise kontroll muudab nende erinevused veelgi silmatorkavamaks.

<img src="../../../translated_images/et/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Kaheksa promptimisemustrit" width="800"/>

*Ülevaade kaheksast prompti inseneritöö mustrist ja nende kasutusjuhtudest*

<img src="../../../translated_images/et/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Mõtlemise kontroll GPT-5.2-ga" width="800"/>

*GPT-5.2 mõtlemise kontroll laseb määrata, kui palju mudel peaks mõtlema — kiiretest otsestest vastustest sügava analüüsini*

**Madala innukusega (Kiire ja sihitud)** - Lihtsate küsimuste jaoks, kus soovid kiireid ja otseseid vastuseid. Mudel teeb minimaalset mõtlemist - maksimaalselt 2 sammu. Kasuta seda arvutuste, otsingute või lihtsate küsimuste jaoks.

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
> - "Milline on erinevus madala ja kõrge innukusega promptimismustrite vahel?"
> - "Kuidas aitavad promptides olevad XML sildid AI vastuste struktuuri kujundada?"
> - "Millal kasutada enesepeegeldamise mustreid ja millal otsest juhist?"

**Kõrge innukusega (Põhjalik ja sügav)** - Keerukate probleemide jaoks, kus soovid põhjalikku analüüsi. Mudel uurib põhjalikult ja näitab üksikasjalikku põhjendust. Kasuta seda süsteemide kavandamisel, arhitektuuriliste otsuste tegemisel või keerulise uurimistöö puhul.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Ülesande täitmine (Samm-sammuline edenemine)** - Mitmeastmeliste töövoogude jaoks. Mudel annab algse plaani, kirjeldab iga sammu töös, siis esitab kokkuvõtte. Kasuta seda migratsioonide, rakenduste või mistahes mitmesammulise protsessi korral.

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

Mõttekäikude-jada promptimine palub mudelil näidata oma põhjendusprotsessi, parandades täpsust keerukatel ülesannetel. Samm-sammuline lagundamine aitab nii inimesel kui ka tehisintellektil loogikat mõista.

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chatiga:** Küsi selle mustri kohta:
> - "Kuidas kohandada ülesande täitmise mustrit pikaajaliste operatsioonide puhul?"
> - "Millised on parimad praktikad tööriistade sissejuhatuste struktureerimisel tootmises?"
> - "Kuidas püüda ja kuvada vahepealseid edenemis-uuendusi kasutajaliideses?"

<img src="../../../translated_images/et/task-execution-pattern.9da3967750ab5c1e.webp" alt="Ülesande täitmise muster" width="800"/>

*Plaani → Täida → Tee kokkuvõte töövoog mitmeastmeliste ülesannete jaoks*

**Enesepeegeldav kood** - Toodangukvaliteediga koodi genereerimiseks. Mudel genereerib koodi vastavalt tootmisstandarditele koos nõuetekohase vigade käsitlemisega. Kasuta seda uute funktsioonide või teenuste loomisel.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/et/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Enesepeegeldamise tsükkel" width="800"/>

*Iteratiivne täiustamise tsükkel - genereeri, hinda, tuvast, paranda, korda*

**Struktureeritud analüüs** - Järjekindlaks hindamiseks. Mudel vaatab koodi läbi fikseeritud raamistiku alusel (õigsus, praktikad, jõudlus, turvalisus, hooldatavus). Kasuta seda koodi ülevaadetes või kvaliteedi hindamisel.

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
> - "Kuidas kohandada analüüsiraamistikku erinevate koodiülevaadete jaoks?"
> - "Kuidas programmiliselt töödelda ja kasutada struktureeritud väljundit?"
> - "Kuidas tagada ühtlane tõsidustaseme määramine erinevate ülevaatussessioonide vahel?"

<img src="../../../translated_images/et/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Struktureeritud analüüsi muster" width="800"/>

*Raamistiku loomine järjekindlateks koodiülevaatusteks koos tõsidustasemete määramisega*

**Mitme vooru jututoa vestlus** - Vestlused, mis vajavad konteksti meenutamist. Mudel mäletab eelnevaid sõnumeid ja ehitab nende peale uusi vastuseid. Kasuta seda interaktiivseteks abiseanssideks või keerukateks küsitlusteks.

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

*Kuidas vestluskontekst koguneb mitme vooru jooksul kuni tokeni limiidini*

**Samm-sammuline põhjendus** - Probleemide jaoks, mis vajavad nähtavat loogikat. Mudel näitab iga sammu konkreetset põhjendust. Kasuta seda matemaatikaülesannete, loogikapähklite või olukordade jaoks, kus tuleb mõista mõtlemisprotsessi.

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

*Probleemide jaotamine selgeteks loogilisteks sammudeks*

**Piiratud väljund** - Vastuste jaoks, millele on seatud spetsiifilised formaadi nõuded. Mudel järgib rangelt vormi ja pikkuse reegleid. Kasuta seda kokkuvõtete või olukordade jaoks, kus vajatakse täpset väljundistruktuuri.

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

*Spetsiifiliste vormingu, pikkuse ja struktuuri nõuete tagamine*

## Olemasolevate Azure ressursside kasutamine

**Kontrolli juurutamist:**

Veendu, et juurtasemes oleks `.env` fail Azure mandaadiga (loodi moodulis 01):
```bash
cat ../.env  # Tuleb näidata AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Serveri käivitamine:**

> **Märkus:** Kui oled juba kõik rakendused käivitanud käskudega `./start-all.sh` moodulis 01, siis käesolev moodul töötab port 8083 peal. Võid jätkata otse aadressiga http://localhost:8083 ja jätta allolevad käivituskäsud vahele.

**Variant 1: Spring Boot Dashboard'i kasutamine (soovitatav VS Code kasutajatele)**

Arenduskonteiner sisaldab Spring Boot Dashboard laiendust, mis pakub visuaalset liidest kõigi Spring Boot rakenduste haldamiseks. Leiad selle VS Code vasakpoolse tegevusriba ikoonina (otsige Spring Boot ikooni).

Sellest Dashboards saad:
- Vaadata kõiki tööruumis olevaid Spring Boot rakendusi
- Käivitada/peatada rakendusi ühe klikiga
- Vaadata rakenduste logisid reaalajas
- Jälgida rakenduste olekut
Lihtsalt klõpsa nuppu "play" kõrval "prompt-engineering", et see moodul käivitada, või alusta kõiki mooduleid korraga.

<img src="../../../translated_images/et/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Variant 2: Shell-skriptide kasutamine**

Käivita kõik veebi rakendused (moodulid 01-04):

**Bash:**
```bash
cd ..  # Juurekataloogist
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Juurekaustast
.\start-all.ps1
```

Või käivita ainult see moodul:

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

Mõlemad skriptid laadivad automaatselt keskkonnamuutujad juurkataloogis olevast `.env` failist ja ehitavad JARid, kui neid veel ei ole.

> **Märkus:** Kui eelistad kõiki mooduleid käsitsi enne käivitamist ehitada:
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

Ava oma brauseris http://localhost:8083.

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

*Peamine juhtpaneel, kus kuvatakse kõik 8 prompt-engineering mustrit koos nende omaduste ja kasutusjuhtumitega*

## Musterite uurimine

Veebiliides võimaldab sul katsetada erinevaid promptimise strateegiaid. Iga muster lahendab erinevaid probleeme - proovi neid, et näha, millal iga lähenemine kõige paremini töötab.

> **Märkus: Järjepidev vs Järjepidevust mitte kasutav** — Iga mustri lehel on kaks nuppu: **🔴 Stream Response (Live)** ja **Järjepidevust mitte kasutav** variant. Järjepidevus kasutab Server-Sent Events (SSE), et kuvada mudeleid genereeritavaid tokeneid reaalajas, nii näed progressi kohe. Järjepidevust mitte kasutav variant ootab kogu vastuse lõpuni, enne kui selle kuvab. Sügavamat mõtlemist nõudvate promptide (nt High Eagerness, Self-Reflecting Code) puhul võib järjepidevust mitte kasutav kutsumine võtta väga kaua aega — vahel minuteid — ilma nähtava tagasisideta. **Kasuta järjepidevust, kui katsetad keerulisi prompt'e,** et näha mudeli töötamist ja vältida muljet, et päring on aegunud.
>
> **Märkus: Brauseri nõue** — Järjepidevus funktsioon kasutab Fetch Streams API-d (`response.body.getReader()`), mis nõuab täisfunktsionaalset brauserit (Chrome, Edge, Firefox, Safari). See **ei tööta** VS Code'i sisseehitatud Simple Browser-is, kuna selle webview ei toeta ReadableStream API-d. Kui kasutad Simple Browserit, töötavad järjepidevust mitte kasutavad nupud normaalselt — ainult järjepidevuse nupud on mõjutatud. Täispika kogemuse saamiseks ava `http://localhost:8083` välises brauseris.

### Madal vs Kõrge Soovikus

Esita lihtne küsimus nagu "Kui palju on 15% 200-st?" madala soovikusega. Saad kohese ja otsese vastuse. Küsi nüüd midagi keerukamat nagu "Kujunda kõrge koormusega API jaoks vahemällu salvestamise strateegia" kõrge soovikusega. Klõpsa **🔴 Stream Response (Live)** ja vaata, kuidas mudeli üksikasjalikud mõttekäigud ilmuvad token tokeni haaval. Sama mudel, sama küsimuse struktuur - aga prompt ütleb, kui palju mõtlemist teha.

### Ülesande Täitmine (Tööriistade Sissejuhatused)

Mitmeastmelised töövood saavad kasu eelnevast planeerimisest ja edenemise jutustamisest. Mudel kirjeldab, mida ta teeb, räägib iga sammu läbi ja võtab tulemused kokku.

### Eneseanalüüsiga Kood

Proovi "Loo e-posti valideerimise teenus". Selle asemel, et lihtsalt koodi genereerida ja peatuda, genereerib mudel, hindab kvaliteedikriteeriumite põhjal, tuvastab nõrkused ja parandab. Näed seda kordamas, kuni kood vastab tootmisstandarditele.

### Struktureeritud Analüüs

Koodi ülevaated vajavad järjekindlaid hindamiskategooriaid. Mudel analüüsib koodi kindlate kategooriate abil (korrektsus, tavad, jõudlus, turvalisus) raskusastme tasemega.

### Mitme Käiguga Vestlus

Küsi "Mis on Spring Boot?" ja kohe seejärel "Näita mulle näidet". Mudel mäletab sinu esimest küsimust ja annab spetsiaalse Spring Boot näite. Mälu puudumisel oleks teine küsimus liiga üldine.

### Samm-sammult Mõtlemine

Võta mõni matemaatikaülesanne ja proovi seda nii Samm-sammult Mõtlemise kui Madala Soovikusega. Madal soovikus annab sulle lihtsalt vastuse - kiire, aga vähem läbipaistev. Samm-sammult näitab sulle kõiki arvutusi ja otsuseid.

### Piiratud Väljund

Kui vajad spetsiifilisi formaate või sõnade arvu, siis see muster tagab rangelt nõuetele vastava väljundi. Proovi genereerida kokkuvõtet täpselt 100 sõnaga punktide kujul.

## Mida Sa Tõeliselt Õpid

**Mõtlemise Pingutus Muudab Kõike**

GPT-5.2 võimaldab sul juhtida arvutuspingutust oma promptide kaudu. Madal pingutus tähendab kiireid vastuseid minimaalse uurimisega. Kõrge pingutus tähendab, et mudel võtab aega sügavalt mõtlemiseks. Õpid kohandama pingutust ülesande keerukusega - ära raiska aega lihtsate küsimuste peale, kuid ära ka kiirusta keeruliste otsustega.

**Struktuur Suunab Käitumist**

Pane tähele XML-silte promptides? Need ei ole lihtsalt kaunistuseks. Mudelid järgivad struktureeritud juhiseid usaldusväärsemalt kui vabateksti. Kui vajad mitmeastmelisi protsesse või keerukat loogikat, aitab struktuur mudelil jälgida, kus ta parasjagu on ja mis järgmisena tuleb.

<img src="../../../translated_images/et/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Hästi struktureeritud prompti anatoomia koos selgete osade ja XML-laadse organiseerimisega*

**Kvaliteet Läbi Enesehindamise**

Eneseanalüüsiga mustrid töötavad kvaliteedikriteeriumite sellega sidumisega. Selle asemel, et loota, et mudel "teeb õigesti", ütled talle täpselt, mida "õige" tähendab: korrektne loogika, vigade käsitlus, jõudlus, turvalisus. Mudel saab enda väljundi hinnata ja parandada. See muudab koodi genereerimise loteriist protsessiks.

**Kontekst on Lõplik**

Mitmekäigulised vestlused töötavad sõnumilooga iga päringu järel. Kuid on piir: igal mudelil on maksimaalne tokenite arv. Vestluste kasvades on sul vaja strateegiaid olulise konteksti säilitamiseks ilma limiiti jõudmata. See moodul näitab sulle, kuidas mälu töötab; hiljem õpid, millal teha kokkuvõtteid, millal unustada ja millal taasesitada.

## Järgmised Sammud

**Järgmine Moodul:** [03-rag - RAG (otsingupõhine genereerimine)](../03-rag/README.md)

---

**Navigeerimine:** [← Eelmine: Moodul 01 - Sissejuhatus](../01-introduction/README.md) | [Tagasi Avalehele](../README.md) | [Järgmine: Moodul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:  
See dokument on tõlgitud AI tõlketeenuse [Co-op Translator](https://github.com/Azure/co-op-translator) abil. Kuigi püüame täpsust tagada, palun arvestage, et automatiseeritud tõlked võivad sisaldada vigu või ebatäpsusi. Originaaldokument selle emakeeles tuleks pidada autoriteetseks allikaks. Kriitilise teabe puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta selle tõlke kasutamisest tekkida võivate arusaamatuste ega valesti mõistmiste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->