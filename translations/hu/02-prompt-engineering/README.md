# Modul 02: Prompt mérnökség a GPT-5.2-vel

## Tartalomjegyzék

- [Amit megtanulsz](../../../02-prompt-engineering)
- [Előfeltételek](../../../02-prompt-engineering)
- [A prompt mérnökség megértése](../../../02-prompt-engineering)
- [Prompt mérnökség alapjai](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Szerepalapú prompting](../../../02-prompt-engineering)
  - [Prompt sablonok](../../../02-prompt-engineering)
- [Haladó minták](../../../02-prompt-engineering)
- [Már meglévő Azure erőforrások használata](../../../02-prompt-engineering)
- [Alkalmazás képernyőképek](../../../02-prompt-engineering)
- [Minták felfedezése](../../../02-prompt-engineering)
  - [Alacsony vs Magas lelkesedés](../../../02-prompt-engineering)
  - [Feladatvégrehajtás (eszköz-előszó)](../../../02-prompt-engineering)
  - [Önreflektáló kód](../../../02-prompt-engineering)
  - [Strukturált elemzés](../../../02-prompt-engineering)
  - [Többszörös körös csevegés](../../../02-prompt-engineering)
  - [Lépésről lépésre érvelés](../../../02-prompt-engineering)
  - [Korlátozott kimenet](../../../02-prompt-engineering)
- [Mit tanulsz valójában](../../../02-prompt-engineering)
- [Következő lépések](../../../02-prompt-engineering)

## Amit megtanulsz

<img src="../../../translated_images/hu/what-youll-learn.c68269ac048503b2.webp" alt="Amit megtanulsz" width="800"/>

Az előző modulban láttad, hogyan teszi lehetővé az emlékezet a beszélgetős MI-t, és használtad a GitHub Modelleket alapvető interakciókra. Most arra fókuszálunk, hogyan teszel fel kérdéseket — magukat a promptokat — az Azure OpenAI GPT-5.2 használatával. A promptok felépítése drámaian befolyásolja a válaszok minőségét. Először áttekintjük az alapvető prompting technikákat, majd bemutatunk nyolc haladó mintát, amelyek teljes mértékben kihasználják a GPT-5.2 képességeit.

A GPT-5.2-t használjuk, mert bevezeti az érvelés szabályozását - megmondhatod a modellnek, mennyit gondolkodjon a válaszadás előtt. Ez világosabbá teszi a különböző prompting stratégiákat, és segít megérteni, mikor melyik megközelítést alkalmazd. Ezenkívül az Azure kevesebb ráfordítást engedélyez GPT-5.2-re a GitHub Modellekhez képest.

## Előfeltételek

- Az 01-es modul befejezve (Azure OpenAI erőforrások telepítve)
- `.env` fájl a gyökérkönyvtárban Azure hitelesítő adatokkal (az 01-es modul `azd up` parancsával létrehozva)

> **Megjegyzés:** Ha még nem fejezted be az 01-es modult, először kövesd ott a telepítési utasításokat.

## A prompt mérnökség megértése

<img src="../../../translated_images/hu/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Mi az a prompt mérnökség?" width="800"/>

A prompt mérnökség az input szöveg megtervezéséről szól, amely következetesen az elvárt eredményeket hozza. Nem csupán kérdések feltevése — hanem a kérés olyan szerkesztése, hogy a modell pontosan értse, mit akarsz, és hogyan adja azt meg.

Gondolj rá úgy, mintha utasításokat adnál egy kollégának. A "Javítsd ki a hibát" túl homályos. A "Javítsd ki a null pointer exception-t a UserService.java 45. sorában, null ellenőrzés hozzáadásával" konkrét. A nyelvi modellek pont így működnek — a pontosság és a szerkezet számít.

<img src="../../../translated_images/hu/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Hogyan illeszkedik a LangChain4j" width="800"/>

A LangChain4j az infrastruktúrát biztosítja — modellkapcsolatokat, memóriát, és üzenettípusokat — míg a prompt minták csak gondosan strukturált szövegek, amelyeket ezen az infrastruktúrán át küldesz. Az alapvető építőelemek a `SystemMessage` (amely beállítja a MI viselkedését és szerepét) és a `UserMessage` (amely a tényleges kérésed hordozza).

## Prompt mérnökség alapjai

<img src="../../../translated_images/hu/five-patterns-overview.160f35045ffd2a94.webp" alt="Az öt prompt mérnökség minta áttekintése" width="800"/>

Mielőtt belemerülnénk a haladó mintákba ebben a modulban, tekintsük át az öt alapvető prompting technikát. Ezek minden promptmérnök alapvető eszköztára. Ha már dolgoztál a [Gyors kezdés modullal](../00-quick-start/README.md#2-prompt-patterns), akkor találkoztál már velük — itt a mögöttes elméleti keret.

### Zero-Shot Prompting

A legegyszerűbb megközelítés: adjunk a modellnek egy közvetlen utasítást példák nélkül. A modell kizárólag a tanulására hagyatkozik a feladat megértéséhez és végrehajtásához. Ez jól működik egyszerű, egyértelmű kérésnél, ahol a várt viselkedés nyilvánvaló.

<img src="../../../translated_images/hu/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Közvetlen utasítás példák nélkül — a modell csak az utasításból következtet a feladatra*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Válasz: "Pozitív"
```

**Mikor használd:** Egyszerű osztályozások, közvetlen kérdések, fordítások vagy bármilyen olyan feladat esetén, amelynél a modell további útmutatás nélkül is elboldogul.

### Few-Shot Prompting

Adj példákat, amelyek bemutatják a követendő mintát. A modell megtanulja a várt bemenet-kimenet formátumot a példákból, és alkalmazza azt új bemeneteken. Ez jelentősen javítja a következetességet ott, ahol a kívánt formátum vagy viselkedés nem nyilvánvaló.

<img src="../../../translated_images/hu/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Példákból tanulás — a modell azonosítja a mintát és alkalmazza új bemeneteken*

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

**Mikor használd:** Egyedi osztályozásokhoz, következetes formázáshoz, adott szakterülethez kötött feladatokhoz vagy ha a zero-shot eredmények következetlenek.

### Chain of Thought

Kérd meg a modellt, hogy lépésről lépésre mutassa be az érvelését. Ahelyett, hogy azonnal válaszolna, a modell lebontja a problémát és részletesen dolgozza fel az egyes részeket. Ez növeli a pontosságot matematikai, logikai és több lépéses érvelési feladatoknál.

<img src="../../../translated_images/hu/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Lépésről lépésre érvelés — összetett problémák bontása explicit logikai lépésekre*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// A modell bemutatja: 15 - 8 = 7, majd 7 + 12 = 19 alma
```

**Mikor használd:** Matematikai feladatokhoz, logikai rejtvényekhez, hibakereséshez vagy bármilyen olyan feladathoz, ahol az érvelési folyamat megjelenítése növeli a pontosságot és a bizalmat.

### Szerepalapú prompting

Állíts be egy személyiséget vagy szerepet a MI számára a kérdésed feltevése előtt. Ez kontextust ad, amely alakítja a válasz tónusát, mélységét és fókuszát. Egy "szoftverarchitekt" más tanácsot ad, mint egy "junior fejlesztő" vagy egy "biztonsági auditor".

<img src="../../../translated_images/hu/role-based-prompting.a806e1a73de6e3a4.webp" alt="Szerepalapú prompting" width="800"/>

*Kontextus és személyiség beállítása — ugyanaz a kérdés más választ kap a megadott szereptől függően*

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

**Mikor használd:** Kódáttekintésekhez, oktatáshoz, adott szakterület elemzéséhez vagy amikor olyan válaszokra van szükség, amelyek egy bizonyos szakértői szinthez vagy nézőponthoz igazodnak.

### Prompt sablonok

Készíts újrahasználható promptokat változó helykitöltőkkel. Ahelyett, hogy minden alkalommal új promptot írnál, egyszer definiálsz egy sablont, majd különböző értékeket töltesz bele. A LangChain4j `PromptTemplate` osztálya ezt egyszerűvé teszi a `{{variable}}` szintaxissal.

<img src="../../../translated_images/hu/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt sablonok" width="800"/>

*Újrahasználható promptok változó helykitöltőkkel — egy sablon, sok felhasználás*

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

**Mikor használd:** Ismétlődő lekérdezések különböző bemenetekkel, kötegelt feldolgozás, újrahasználható MI munkafolyamatok építése vagy bármilyen olyan szituáció, ahol a prompt szerkezete ugyanaz marad, de az adatok változnak.

---

Ezek az öt alapvető technika szilárd eszköztárat adnak a legtöbb prompting feladathoz. A modul többi része erre épít, a GPT-5.2 érvelés szabályozását, önértékelését és strukturált kimenetét kihasználva **nyolc haladó mintával**.

## Haladó minták

Az alapok lefektetése után lépjünk tovább a nyolc haladó mintára, amelyek egyedivé teszik ezt a modult. Nem minden problémához ugyanaz a megközelítés szükséges. Néhány kérdés gyors választ igényel, mások mély gondolkodást. Néhány esetben látható érvelés kell, máskor csak az eredmény. Az alábbi minták mindegyike eltérő forgatókönyvre optimalizált — és a GPT-5.2 érvelés szabályozása még jobban kiemeli a különbségeket.

<img src="../../../translated_images/hu/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Nyolc prompting minta" width="800"/>

*A nyolc prompt mérnökségi minta áttekintése és alkalmazási esetei*

<img src="../../../translated_images/hu/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Érvelés szabályozás a GPT-5.2-vel" width="800"/>

*A GPT-5.2 érvelés szabályozásával megadhatod, mennyit gondolkodjon a modell — a gyors, közvetlen válaszoktól a mély elemzésig*

**Alacsony lelkesedés (Gyors & fókuszált)** - Egyszerű kérdésekhez, ahol gyors, közvetlen választ szeretnél. A modell minimális érvelést végez - maximum 2 lépést. Használd számításokhoz, keresésekhez vagy egyértelmű kérdésekhez.

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

> 💡 **Fedezd fel a GitHub Copilot-tal:** Nyisd meg a [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) fájlt, és kérdezd meg:
> - "Mi a különbség az alacsony és magas lelkesedésű prompting minták között?"
> - "Hogyan segítik az XML tagek a promptok strukturálását az MI válaszában?"
> - "Mikor használjak önreflektáló mintákat a közvetlen utasítás helyett?"

**Magas lelkesedés (Mély & alapos)** - Összetett problémákhoz, ahol átfogó elemzést szeretnél. A modell alaposan vizsgál és részletes érvelést mutat. Használd rendszertervezéshez, architektúrai döntésekhez vagy összetett kutatáshoz.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Feladatvégrehajtás (Lépésről lépésre haladás)** - Többlépcsős munkafolyamatokhoz. A modell előzetes tervet ad, narrálja az egyes lépéseket, majd összegzi az eredményt. Használd migrációkhoz, implementációkhoz vagy bármilyen többlépéses folyamathoz.

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

A Chain-of-Thought prompting kifejezetten kéri a modellt, hogy mutassa be érvelési folyamatát, növelve így az összetett feladatok pontosságát. A lépésenkénti bontás segíti az embereket és az MI-t is az érvelés megértésében.

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chat-en:** Kérdezz erről a mintáról:
> - "Hogyan igazítanám a feladatvégrehajtó mintát hosszú futású műveletekhez?"
> - "Mik a legjobb gyakorlatok az eszköz-előszó szerkezetének kialakításához éles alkalmazásokban?"
> - "Hogyan lehet elkapni és megjeleníteni a közbenső haladási frissítéseket a felhasználói felületen?"

<img src="../../../translated_images/hu/task-execution-pattern.9da3967750ab5c1e.webp" alt="Feladatvégrehajtási minta" width="800"/>

*Terv → Végrehajtás → Összegzés munkafolyamat többlépcsős feladatokhoz*

**Önreflektáló kód** - Termelési minőségű kód generálásához. A modell a gyártási szabványoknak megfelelő kódot hoz létre, hibakezeléssel. Használd új funkciók vagy szolgáltatások építéséhez.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/hu/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Önreflexiós ciklus" width="800"/>

*Iteratív javítási kör – generálás, értékelés, hibák azonosítása, fejlesztés, ismétlés*

**Strukturált elemzés** - Következetes értékeléshez. A modell egy rögzített keretrendszer alapján vizsgálja a kódot (helyesség, gyakorlatok, teljesítmény, biztonság, karbantarthatóság). Használd kódáttekintésekhez vagy minőségvizsgálatokhoz.

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

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chat-en:** Kérdezz strukturált elemzésről:
> - "Hogyan szabhatom testre az elemzési keretrendszert különböző típusú kódáttekintésekhez?"
> - "Mi a legjobb módszer a strukturált kimenet programozott feldolgozására és feldolgozására?"
> - "Hogyan biztosíthatom az egységes súlyossági szinteket különböző átvizsgálási ülések között?"

<img src="../../../translated_images/hu/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Strukturált elemzés minta" width="800"/>

*Kódáttekintésekhez keretrendszer súlyossági szintekkel*

**Többszörös körös csevegés** - Kontextust igénylő beszélgetésekhez. A modell emlékszik az előző üzenetekre és épít rájuk. Használd interaktív segítségnyújtó ülésekhez vagy összetett kérdés-válasz helyzetekhez.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/hu/context-memory.dff30ad9fa78832a.webp" alt="Kontextus memória" width="800"/>

*Hogyan halmozódik a beszélgetés kontextusa többszörös körökön át, amíg el nem éri a token limitet*

**Lépésről lépésre érvelés** - Látható logikát igénylő problémákhoz. A modell minden lépéshez explicit érvelést mutat. Használd matekfeladatokhoz, logikai rejtvényekhez vagy amikor érteni akarod a gondolkodási folyamatot.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/hu/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Lépésről lépésre minta" width="800"/>

*Problémák explicit logikai lépésekre bontása*

**Korlátozott kimenet** - Specifikus formátumot igénylő válaszokhoz. A modell szigorúan követi a formátum- és hosszúságszabályokat. Használd összefoglalókhoz vagy ha pontos kimeneti struktúrát szeretnél.

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

<img src="../../../translated_images/hu/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Korlátozott kimenet minta" width="800"/>

*Specifikus formátum, hossz és szerkezet követelményeinek betartása*

## Már meglévő Azure erőforrások használata

**Telepítés ellenőrzése:**

Győződj meg arról, hogy a gyökérkönyvtárban létezik `.env` fájl Azure hitelesítő adatokkal (az 01-es modul során készült):
```bash
cat ../.env  # Meg kell jelenítenie az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**Indítsd el az alkalmazást:**

> **Megjegyzés:** Ha már elindítottad az összes alkalmazást a `./start-all.sh` segítségével az 01-es modulból, ez a modul már fut a 8083-as porton. Így kihagyhatod az alábbi indítási parancsokat, és közvetlenül a http://localhost:8083 oldalra mehetsz.

**1. lehetőség: Spring Boot Dashboard használata (ajánlott VS Code felhasználóknak)**

A fejlesztői konténer tartalmazza a Spring Boot Dashboard bővítményt, amely vizuális felületet biztosít az összes Spring Boot alkalmazás kezeléséhez. A VS Code bal oldali tevékenységi sávban találod (keresd a Spring Boot ikont).

A Spring Boot Dashboard segítségével:
- Megtekintheted a workspace-ben elérhető összes Spring Boot alkalmazást
- Egy kattintással elindíthatod vagy leállíthatod az alkalmazásokat
- Valós időben böngészheted az alkalmazás naplóit
- Figyelemmel kísérheted az alkalmazások állapotát
Egyszerűen kattints a lejátszás gombra a "prompt-engineering" mellett a modul elindításához, vagy indíts el egyszerre minden modult.

<img src="../../../translated_images/hu/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**2. lehetőség: Shell szkriptek használata**

Indítsd el az összes webalkalmazást (01-04 modulok):

**Bash:**
```bash
cd ..  # A gyökérkönyvtárból
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # A gyökérkönyvtárból
.\start-all.ps1
```

Vagy indítsd csak ezt a modult:

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

Mindkét szkript automatikusan betölti a környezeti változókat a gyökér `.env` fájlból, és lefordítja a JAR fájlokat, ha azok még nem léteznek.

> **Megjegyzés:** Ha előbb manuálisan szeretnéd lefordítani az összes modult az indítás előtt:
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

Nyisd meg a http://localhost:8083 címet a böngésződben.

**Leállításhoz:**

**Bash:**
```bash
./stop.sh  # Csak ez a modul
# Vagy
cd .. && ./stop-all.sh  # Minden modul
```

**PowerShell:**
```powershell
.\stop.ps1  # Csak ez a modul
# Vagy
cd ..; .\stop-all.ps1  # Minden modul
```

## Alkalmazás képernyőképek

<img src="../../../translated_images/hu/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*A fő irányítópult, amely az összes 8 prompt-mintázatot mutatja azok jellemzőivel és használati eseteivel együtt*

## A mintázatok felfedezése

A webes felület lehetővé teszi, hogy különböző prompt-stratégiákkal kísérletezz. Minden mintázat más-más problémát old meg – próbáld ki őket, hogy meglásd, mikor melyik megközelítés működik a legjobban.

> **Megjegyzés: Streaming vagy nem-streaming** — Minden mintázat oldalán két gomb található: **🔴 Stream Response (Élő)** és egy **Nem-streaming** opció. A streaming Server-Sent Events (SSE) technológiát használ, hogy valós időben jelenítse meg a tokeneket, ahogy a modell generálja őket, így azonnal láthatod az előrehaladást. A nem-streaming opció megvárja a teljes válasz beérkezését, mielőtt megjelenítené azt. Mély gondolkodást igénylő promptoknál (pl. High Eagerness, Self-Reflecting Code) a nem-streaming hívás sokáig is eltarthat — néha percekig — látható visszajelzés nélkül. **Használd a streaminget komplex promptok tesztelésekor**, így láthatod a modell működését, és elkerülheted a timeout érzetét.
>
> **Megjegyzés: Böngészőkövetelmény** — A streaming funkció a Fetch Streams API-t (`response.body.getReader()`) használja, amelyhez teljes értékű böngésző szükséges (Chrome, Edge, Firefox, Safari). Nem működik a VS Code beépített Simple Browserében, mert annak webnézője nem támogatja a ReadableStream API-t. Ha a Simple Browser-t használod, a nem-streaming gombok továbbra is működnek rendesen – csak a streaming gombok érintettek. A teljes élményhez nyisd meg a `http://localhost:8083` címet egy külső böngészőben.

### Alacsony és magas erőfeszítés

Tegyél fel egy egyszerű kérdést, mint például „Mennyi a 15%-a 200-nak?” alacsony erőfeszítéssel. Azonnal kapsz egy közvetlen választ. Most kérj valami bonyolultat, például „Tervezzen egy gyorsítótárazási stratégiát egy nagy forgalmú API-hoz” magas erőfeszítéssel. Kattints a **🔴 Stream Response (Élő)** gombra, és figyeld, hogyan jelenik meg részletes gondolkodás tokenről tokenre. Ugyanaz a modell, ugyanaz a kérdés szerkezete – de a prompt megmondja, mennyi gondolkodást igényeljen.

### Feladatvégrehajtás (Eszköz bevezetők)

Többlépcsős munkafolyamatoknál előnyös az előzetes tervezés és a folyamat narrálása. A modell vázolja a teendőket, majd lépésről lépésre meséli el a folyamatot, végül összefoglalja az eredményeket.

### Önreflektáló kód

Próbáld ki: „Hozz létre egy e-mail érvényesítő szolgáltatást”. Ahelyett, hogy csak generálná a kódot és megállna, a modell generál, értékeli minőségi kritériumok alapján, felismeri a gyengeségeket, és javítja azt. Láthatod, ahogy iterál, amíg a kód eléri a gyártási szintet.

### Strukturált elemzés

Kódellenőrzésekhez következetes értékelési keretre van szükség. A modell előre meghatározott kategóriák szerint elemzi a kódot (helyesség, gyakorlatok, teljesítmény, biztonság) súlyossági szintekkel.

### Többlépcsős csevegés

Kérdezd meg: „Mi az a Spring Boot?” majd azonnal tedd fel a következő kérdést: „Mutass egy példát”. A modell emlékszik az első kérdésedre, és kifejezetten egy Spring Boot példát ad. Memória nélkül a második kérdés túl általános lenne.

### Lépésről lépésre gondolkodás

Válassz ki egy matematikai feladatot, és próbáld ki mind a Lépésről lépésre gondolkodással, mind alacsony erőfeszítéssel. Az alacsony erőfeszítés gyors, de átláthatatlan választ ad. A lépésről lépésre megmutat minden számítást és döntést.

### Korlátozott kimenet

Amikor konkrét formátumokra vagy szó számra van szükséged, ez a mintázat szigorúan betartatja az előírásokat. Próbálj meg egy összefoglalót generálni pontosan 100 szóban, felsorolás formátumban.

## Amit valójában tanulsz

**A gondolkodás erőfeszítése mindent megváltoztat**

A GPT-5.2 lehetővé teszi, hogy a promptokon keresztül szabályozd a számítási erőfeszítést. Az alacsony erőfeszítés gyors válaszokat ad minimális felfedezéssel. A magas erőfeszítés azt jelenti, hogy a modell időt szán mély gondolkodásra. Megtanulod az erőfeszítés mértékét igazítani a feladat összetettségéhez – ne pazarold az időt egyszerű kérdéseken, de ne siess el bonyolult döntéseket sem.

**A szerkezet irányítja a viselkedést**

Észrevetted az XML címkéket a promptokban? Nem díszítések. A modellek megbízhatóbban követik a strukturált utasításokat, mint a szabad szöveget. Többlépcsős folyamatok vagy összetett logika esetén a szerkezet segít a modellnek nyomon követni, hol tart és mi következik.

<img src="../../../translated_images/hu/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Egy jól strukturált prompt anatómiája világos szekciókkal és XML-stílusú szervezéssel*

**Minőség önértékeléssel**

Az önreflektáló mintázatok azzal működnek, hogy a minőségi kritériumokat explicit módon megadják. Ahelyett, hogy reméled, hogy a modell „jól csinálja”, megmondod neki pontosan, mit jelent a „jól”: helyes logika, hibakezelés, teljesítmény, biztonság. A modell így képes értékelni a saját kimenetét és javítani azt. Ez a kódgenerálást lottóból folyamatá alakítja.

**A kontextus véges**

A többlépcsős beszélgetések az üzenettörténet minden kérésnél való befoglalásával működnek. De van korlát – minden modellnek maximális token száma van. Ahogy növekszik a beszélgetés, stratégiákra lesz szükséged, hogy megőrizd a releváns kontextust anélkül, hogy elérnéd a határt. Ez a modul megmutatja, hogyan működik a memória; később megtanulod, mikor érdemes összefoglalni, mikor elfelejteni és mikor visszahívni.

## Következő lépések

**Következő modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigáció:** [← Előző: Modul 01 - Bevezetés](../01-introduction/README.md) | [Vissza a főoldalra](../README.md) | [Következő: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Felelősség kizárása**:  
Ezt a dokumentumot az AI fordítószolgáltatás, a [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével fordítottuk. Bár a pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatizált fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum, az anyanyelvén tekintendő a hivatalos forrásnak. Fontos információk esetén profi, emberi fordítást javaslunk. Nem vállalunk felelősséget a fordítás használatából eredő félreértések vagy téves értelmezések miatt.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->