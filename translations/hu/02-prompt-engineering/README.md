# Modul 02: Prompt Engineering a GPT-5.2-vel

## Tartalomjegyzék

- [Amit megtanulsz](../../../02-prompt-engineering)
- [Előfeltételek](../../../02-prompt-engineering)
- [A prompt engineering megértése](../../../02-prompt-engineering)
- [Prompt engineering alapjai](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Szerepalapú promptolás](../../../02-prompt-engineering)
  - [Prompt sablonok](../../../02-prompt-engineering)
- [Haladó minták](../../../02-prompt-engineering)
- [Meglévő Azure erőforrások használata](../../../02-prompt-engineering)
- [Alkalmazás képernyőképei](../../../02-prompt-engineering)
- [A minták felfedezése](../../../02-prompt-engineering)
  - [Alacsony vs magas lelkesedés](../../../02-prompt-engineering)
  - [Feladatvégrehajtás (eszköz preamble-ek)](../../../02-prompt-engineering)
  - [Önreflektáló kód](../../../02-prompt-engineering)
  - [Strukturált elemzés](../../../02-prompt-engineering)
  - [Többszörös körű chat](../../../02-prompt-engineering)
  - [Lépésről lépésre gondolkodás](../../../02-prompt-engineering)
  - [Korlátozott kimenet](../../../02-prompt-engineering)
- [Amit valóban megtanulsz](../../../02-prompt-engineering)
- [Következő lépések](../../../02-prompt-engineering)

## Amit megtanulsz

<img src="../../../translated_images/hu/what-youll-learn.c68269ac048503b2.webp" alt="Amit megtanulsz" width="800"/>

Az előző modulban láttad, hogy a memória hogyan teszi lehetővé a beszélgetős AI-t, és alapvető interakciókra használtad a GitHub modelleket. Most arra fókuszálunk, hogyan teszel fel kérdéseket — maguk a promptok — az Azure OpenAI GPT-5.2 használatával. Az, hogy hogyan strukturálod a promptjaidat, drámaian befolyásolja a válaszok minőségét. Először áttekintjük az alapvető prompting technikákat, majd áttérünk nyolc haladó mintára, amelyek teljes mértékben kihasználják a GPT-5.2 képességeit.

GPT-5.2-t fogjuk használni, mert bevezeti a gondolkodás vezérlését — megmondhatod a modellnek, mennyi gondolkodásra van szükség a válaszadáshoz. Ez különösen jól láthatóvá teszi a különböző prompting stratégiákat, és segít megérteni, mikor melyik megközelítést érdemes használni. Az Azure kevesebb korlátozással rendelkezik a GPT-5.2 esetében, összehasonlítva a GitHub modellekkel.

## Előfeltételek

- Elvégezve az 01. modul (Azure OpenAI erőforrások telepítve)
- `.env` fájl a gyökérkönyvtárban az Azure hitelesítő adatokkal (az `azd up` hozta létre az 01. modulban)

> **Megjegyzés:** Ha még nem végezted el az 01. modult, először ott kövesd a telepítési útmutatót.

## A prompt engineering megértése

<img src="../../../translated_images/hu/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Mi a prompt engineering?" width="800"/>

A prompt engineering az bemeneti szöveg megtervezéséről szól, amely következetesen megadja a kívánt eredményeket. Nem csak kérdésfeltevésről van szó — hanem olyan kérések struktúrálásáról, hogy a modell pontosan értse, mit akarsz és hogyan kell teljesítenie azt.

Gondolj rá úgy, mint amikor utasításokat adsz egy kollégának. A „Javítsd meg a hibát” homályos. Az „Javítsd meg a null pointer exception hibát a UserService.java 45. sorában egy null ellenőrzés hozzáadásával” már konkrét. A nyelvi modellek ugyanígy működnek — a konkrétság és a struktúra számít.

<img src="../../../translated_images/hu/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Hogyan illeszkedik be a LangChain4j" width="800"/>

A LangChain4j biztosítja az infrastruktúrát — modellkapcsolatokat, memóriát és üzenettípusokat — míg a prompt minták csak gondosan strukturált szövegek, amelyeket ezen az infrastruktúrán keresztül küldesz. A kulcs-építőelemek a `SystemMessage` (ami beállítja az MI viselkedését és szerepét) és a `UserMessage` (ami a tényleges kérést tartalmazza).

## Prompt engineering alapjai

<img src="../../../translated_images/hu/five-patterns-overview.160f35045ffd2a94.webp" alt="Öt Prompt Engineering Minta Áttekintése" width="800"/>

Mielőtt belevágnánk a modul haladó mintáiba, tekintsük át az öt alapvető prompting technikát. Ezek az alapkövei minden prompt mérnök munkájának. Ha már dolgoztál a [Gyors kezdő modulban](../00-quick-start/README.md#2-prompt-patterns), láthattad ezeket működés közben — itt van a mögöttes koncepcionális keret.

### Zero-Shot Prompting

A legegyszerűbb megközelítés: adj a modellnek közvetlen utasítást példák nélkül. A modell kizárólag a tanuláson alapuló tudására hagyatkozik a feladat megértéséhez és végrehajtásához. Ez jól működik egyértelmű kérések esetén, ahol a várt viselkedés egyértelmű.

<img src="../../../translated_images/hu/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Közvetlen utasítás példák nélkül — a modell csak az utasításból következtet a feladatra*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Válasz: "Pozitív"
```

**Mikor használd:** Egyszerű besorolásokhoz, közvetlen kérdésekhez, fordításokhoz, vagy olyan feladatokhoz, amelyeket a modell további útmutatás nélkül is el tud végezni.

### Few-Shot Prompting

Adj példákat, amik bemutatják azt a mintát, amit a modelltől vársz. A modell ezekből megtanulja a várt bemenet-kimenet formátumot, és alkalmazza azt új bemenetek esetén. Ez drámai módon javítja a következetességet olyan feladatoknál, ahol a kívánt formátum vagy viselkedés nem egyértelmű.

<img src="../../../translated_images/hu/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Példákból tanulás — a modell felismeri a mintát és alkalmazza új bemenetekre*

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

**Mikor használd:** Egyedi besorolásokhoz, következetes formázáshoz, domain-specifikus feladatokhoz, vagy ha a zero-shot eredmények nem konzekvensek.

### Chain of Thought

Kérd meg a modellt, hogy mutassa meg a gondolkodási folyamatát lépésről lépésre. Ahelyett, hogy rögtön a válaszra ugrana, a modell lebontja a problémát, és expliciten átgondolja az egyes részeket. Ez javítja a pontosságot matematikai, logikai és többlépéses következtetési feladatoknál.

<img src="../../../translated_images/hu/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Lépésről lépésre történő érvelés — összetett problémák lebontása explicit logikai lépésekre*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// A modell megmutatja: 15 - 8 = 7, majd 7 + 12 = 19 alma
```

**Mikor használd:** Matematikai feladatokhoz, logikai rejtvényekhez, hibakereséshez, vagy bármilyen olyan feladathoz, ahol a gondolkodási folyamat bemutatása javítja a pontosságot és a bizalmat.

### Szerepalapú promptolás

Állíts be egy személyiséget vagy szerepet az MI számára, mielőtt feltennéd a kérdést. Ez kontextust ad, ami alakítja a válasz hangját, mélységét és fókuszát. Egy „szoftverarchitekt” más tanácsokat ad, mint egy „junior fejlesztő” vagy „biztonsági auditor”.

<img src="../../../translated_images/hu/role-based-prompting.a806e1a73de6e3a4.webp" alt="Szerepalapú promptolás" width="800"/>

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

**Mikor használd:** Kódáttekintésekhez, oktatáshoz, speciális domain elemzésekhez, vagy amikor adott szakértelmi szinthez vagy nézőponthoz szeretnél igazított válaszokat.

### Prompt sablonok

Hozz létre újrahasználható promptokat változó helyőrzőkkel. Ahelyett, hogy mindig új promptot írnál, definiálj egy sablont egyszer, majd töltsd fel különböző értékekkel. A LangChain4j `PromptTemplate` osztálya ezt könnyűvé teszi `{{változó}}` szintaxissal.

<img src="../../../translated_images/hu/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt sablonok" width="800"/>

*Újrahasználható promptok változó helyőrzőkkel — egy sablon, sok használat*

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

**Mikor használd:** Ismétlődő lekérdezésekhez különböző bemenetekkel, kötegelt feldolgozáshoz, újrahasználható MI munkafolyamatok építéséhez, vagy bárhol, ahol a prompt struktúrája ugyanaz, de az adatok változnak.

---

Ez az öt alapvető elem egy erős eszköztárat ad a legtöbb prompting feladathoz. A modul további része erre épít **nyolc haladó mintával**, melyek kihasználják a GPT-5.2 gondolkodás vezérlését, önértékelését és strukturált kimeneti képességeit.

## Haladó minták

Miután lefedtük az alapokat, lépjünk a nyolc haladó mintára, amelyek egyedivé teszik ezt a modult. Nem minden problémához ugyanaz a megközelítés kell. Egyes kérdések gyors válaszokat igényelnek, mások mély gondolkodást. Egyesek látható érvelést, mások csak eredményeket. Az alábbi minták mindegyike különböző szituációhoz van optimalizálva — és a GPT-5.2 gondolkodás vezérlése még erőteljesebbé teszi a különbségeket.

<img src="../../../translated_images/hu/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Nyolc Prompt Engineering Minta" width="800"/>

*Áttekintés a nyolc prompt engineering mintáról és azok alkalmazási eseteiről*

<img src="../../../translated_images/hu/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Gondolkodás vezérlése a GPT-5.2-vel" width="800"/>

*A GPT-5.2 gondolkodás vezérlés lehetővé teszi, hogy megszabjuk, mennyi gondolkodást végezzen a modell — a gyors közvetlen válaszoktól a mély feltárásig*

**Alacsony lelkesedés (gyors és fókuszált)** – Egyszerű kérdésekhez, ahol gyors, közvetlen válaszokra van szükséged. A modell minimális érvelést végez - maximum 2 lépés. Használd számításokhoz, lekérdezésekhez vagy egyértelmű kérdésekhez.

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

> 💡 **Fedezd fel a GitHub Copilot-tal:** Nyisd meg a [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)-t és kérdezd meg:
> - "Mi a különbség az alacsony és magas lelkesedésű prompting minták között?"
> - "Hogyan segítik az XML tagek a promptokban az MI válasz struktúráját?"
> - "Mikor érdemes az önreflektáló mintákat használni a közvetlen utasítás helyett?"

**Magas lelkesedés (mély és alapos)** – Összetett problémákhoz, ahol átfogó elemzést szeretnél. A modell alaposan feltárja és részletes érvelést mutat. Használd rendszerek tervezéséhez, architektúra döntésekhez vagy komplex kutatásokhoz.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Feladatvégrehajtás (lépésről lépésre haladás)** – Többlépéses munkafolyamatokhoz. A modell előre megtervezi a folyamatot, közben narrálja minden lépést, majd összefoglalót ad. Használd migrációkhoz, implementációkhoz vagy bármilyen többlépéses folyamathoz.

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

A Chain-of-Thought prompting kifejezetten kéri a modellt, hogy mutassa meg érvelési folyamatát, ami javítja a pontosságot összetett feladatoknál. A lépésenkénti bontás segíti mind az embereket, mind az MI-t a logika megértésében.

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chaten:** Kérdezz a mintáról:
> - "Hogyan adaptálnám a feladatvégrehajtás mintát hosszú futású műveletekre?"
> - "Mik a legjobb gyakorlatok az eszköz preamble-ek struktúrálására éles alkalmazásokban?"
> - "Hogyan lehet köztes előrehaladási frissítéseket rögzíteni és megjeleníteni UI-ban?"

<img src="../../../translated_images/hu/task-execution-pattern.9da3967750ab5c1e.webp" alt="Feladatvégrehajtási Minta" width="800"/>

*Tervezés → Végrehajtás → Összefoglalás munkafolyamat több lépéses feladatokhoz*

**Önreflektáló kód** – Termelési minőségű kód generálásához. A modell a gyártási szabványoknak megfelelő kódot generál megfelelő hibakezeléssel. Használd új funkciók vagy szolgáltatások építéséhez.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/hu/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Önreflexiós ciklus" width="800"/>

*Ismétlődő fejlesztési ciklus – generálás, értékelés, hibák azonosítása, javítás, ismétlés*

**Strukturált elemzés** – Következetes értékeléshez. A modell rögzített keretrendszerrel elemzi a kódot (helyesség, gyakorlatok, teljesítmény, biztonság, fenntarthatóság). Használd kódáttekintésekhez vagy minőségértékelésekhez.

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

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chaten:** Kérdezz a strukturált elemzésről:
> - "Hogyan szabhatom testre az elemzési keretrendszert különböző kódáttekintések típusaihoz?"
> - "Mi a legjobb módja annak, hogy programozottan feldolgozzam és kezeljem a strukturált kimenetet?"
> - "Hogyan biztosítható az egységes súlyossági szint különböző átvizsgálási ülések között?"

<img src="../../../translated_images/hu/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Strukturált elemzési minta" width="800"/>

*Keretrendszer következetes kódáttekintésekhez súlyossági szintekkel*

**Többszörös körű chat** – Kontextust igénylő beszélgetésekhez. A modell emlékszik a korábbi üzenetekre és épít ezekre. Használd interaktív segítségnyújtáshoz vagy komplex kérdések-válaszokhoz.

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

*Hogyan halmozódik fel a beszélgetési kontextus több kör alatt a token limit eléréséig*

**Lépésről lépésre gondolkodás** – Látható logikát igénylő problémákhoz. A modell explicit érvelést mutat minden lépéshez. Használd matematikai problémákhoz, logikai rejtvényekhez vagy amikor meg kell érteni a gondolkodási folyamatot.

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

**Korlátozott kimenet** – Formátum-követelményeket tartalmazó válaszokhoz. A modell szigorúan követi a formázási és hosszúsági szabályokat. Használd összefoglalókhoz vagy amikor precíz kimeneti struktúrára van szükséged.

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

<img src="../../../translated_images/hu/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Korlátozott kimeneti minta" width="800"/>

*Specifikus formátum, hossz és struktúra követelmények érvényesítése*

## Meglévő Azure erőforrások használata

**Telepítés ellenőrzése:**

Ellenőrizd, hogy a `.env` fájl létezik-e a gyökérkönyvtárban az Azure hitelesítő adatokkal (amely a 01. modul során jött létre):
```bash
cat ../.env  # Meg kell jelenítenie az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**Indítsd el az alkalmazást:**

> **Megjegyzés:** Ha már elindítottad az összes alkalmazást az `./start-all.sh` használatával az 01. modulból, ez a modul már fut a 8083-as porton. Az alábbi indítási parancsokat át is ugorhatod, és közvetlenül a http://localhost:8083 címet használhatod.

**1. lehetőség: Spring Boot Dashboard használata (VS Code felhasználóknak ajánlott)**

A fejlesztői konténer tartalmazza a Spring Boot Dashboard kiterjesztést, amely vizuális kezelőfelületet biztosít minden Spring Boot alkalmazás kezeléséhez. A bal oldali tevékenységsávban találod meg a VS Code-ban (keresd a Spring Boot ikont).

A Spring Boot Dashboard-on keresztül megteheted:
- Megtekintheted az összes elérhető Spring Boot alkalmazást a munkaterületen
- Egy kattintással elindíthatod/leállíthatod az alkalmazásokat
- Valós időben megtekintheted az alkalmazás naplóit
- Követheted az alkalmazás állapotát
Egyszerűen kattints a lejátszás gombra a "prompt-engineering" mellett, hogy elindítsd ezt a modult, vagy indítsd el az összes modult egyszerre.

<img src="../../../translated_images/hu/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Vezérlőpult" width="400"/>

**2. lehetőség: Shell szkriptek használata**

Indítsd el az összes webalkalmazást (01-04 modulok):

**Bash:**
```bash
cd ..  # A gyökérkönyvtárból
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Gyökérkönyvtárból
.\start-all.ps1
```

Vagy indítsd el csak ezt a modult:

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

Mindkét szkript automatikusan betölti a környezeti változókat a gyökér `.env` fájlból, és le fogja fordítani a JAR fájlokat, ha azok nem léteznek.

> **Megjegyzés:** Ha inkább manuálisan szeretnéd buildelni az összes modult az indítás előtt:
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

## Alkalmazás Képernyőképek

<img src="../../../translated_images/hu/dashboard-home.5444dbda4bc1f79d.webp" alt="Vezérlőpult Főoldal" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*A fő vezérlőpult, amely az összes 8 prompt engineering mintát, jellemzőiket és felhasználási eseteket mutatja*

## Minták felfedezése

A webes felület lehetővé teszi, hogy különböző promptolási stratégiákkal kísérletezz. Minden minta más problémát old meg – próbáld ki őket, hogy láthasd, mikor melyik megközelítés működik a legjobban.

### Alacsony és Magas Lelkesedés

Tegyél fel egy egyszerű kérdést, például "Mennyi 15% a 200-ból?" Alacsony Lelkesedéssel. Azonnali, közvetlen választ kapsz. Most kérj valami összetettebbet, például "Tervezd meg egy nagy forgalmú API gyorsítótár stratégiáját" Magas Lelkesedéssel. Figyeld meg, hogyan lassul a modell, és részletes gondolkodást nyújt. Ugyanaz a modell, ugyanaz a kérdés szerkezet – de a prompt megmondja, mennyit gondolkodjon.

<img src="../../../translated_images/hu/low-eagerness-demo.898894591fb23aa0.webp" alt="Alacsony Lelkesedés Demó" width="800"/>

*Gyors számítás minimális gondolkodással*

<img src="../../../translated_images/hu/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Magas Lelkesedés Demó" width="800"/>

*Átfogó gyorsítótár stratégia (2.8MB)*

### Feladatvégrehajtás (Eszköz Indítók)

Többlépcsős munkafolyamatok előnyt élveznek az előzetes tervezéssel és lépésenkénti narrációval. A modell leírja, mit fog csinálni, narrálja az egyes lépéseket, majd összegzi az eredményeket.

<img src="../../../translated_images/hu/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Feladat Végrehajtás Demó" width="800"/>

*REST végpont létrehozása lépésről lépésre narrációval (3.9MB)*

### Önkritikus Kód

Próbáld ki: "Hozz létre egy e-mail validációs szolgáltatást". Ahelyett, hogy csak kódot generálna és megállna, a modell létrehozza, értékeli minőségi kritériumok alapján, azonosítja a gyengeségeket, és javít. Láthatod, ahogy ismétel addig, amíg a kód eléri a gyártási szintet.

<img src="../../../translated_images/hu/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Önkritikus Kód Demó" width="800"/>

*Teljes e-mail validációs szolgáltatás (5.2MB)*

### Strukturált Elemzés

A kódáttekintések következetes értékelési keretrendszert igényelnek. A modell a kódot meghatározott kategóriák (helyesség, gyakorlatok, teljesítmény, biztonság) és súlyossági szintek szerint elemzi.

<img src="../../../translated_images/hu/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Strukturált Elemzés Demó" width="800"/>

*Keretrendszer-alapú kódáttekintés*

### Többlépéses Csevegés

Kérdezd meg: "Mi az a Spring Boot?" majd azonnal kövesd azzal: "Mutass egy példát". A modell emlékszik az első kérdésedre, és kifejezetten Spring Boot példát ad. Memória nélkül a második kérdés túlságosan általános lenne.

<img src="../../../translated_images/hu/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Többlépéses Csevegés Demó" width="800"/>

*Kontextus megőrzése kérdések között*

### Lépésenkénti Gondolkodás

Válassz egy matematikai problémát, és próbáld ki mind a Lépésenkénti Gondolkodás, mind az Alacsony Lelkesedés mintákkal. Az alacsony lelkesedés csak gyors választ ad, de átláthatatlanul. A lépésenkénti gondolkodás megmutat minden számítást és döntést.

<img src="../../../translated_images/hu/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Lépésenkénti Gondolkodás Demó" width="800"/>

*Matematikai feladat explicit lépésekkel*

### Korlátozott Kimenet

Ha meghatározott formátumra vagy szó számára van szükség, ez a minta szigorúan betartatja az előírásokat. Próbálj meg generálni egy összefoglalót pontosan 100 szóban, felsorolásos formátumban.

<img src="../../../translated_images/hu/constrained-output-demo.567cc45b75da1633.webp" alt="Korlátozott Kimenet Demó" width="800"/>

*Gépi tanulás összefoglaló formátumkontrollal*

## Amit Valójában Tanulsz

**A Gondolkodási Erőfeszítés Mindent Megváltoztat**

A GPT-5.2 lehetővé teszi, hogy a promptokon keresztül szabályozd a számítási erőfeszítést. Az alacsony erőfeszítés gyors válaszokat jelent minimális kutatással. A magas erőfeszítés azt jelenti, hogy a modell mélyen időz a gondolkodással. Megtanulod összehangolni az erőfeszítést a feladat bonyolultságával – ne pazarold az időt egyszerű kérdésekre, de ne is siess bonyolult döntéseknél.

**A Szerkezet Irányítja a Viselkedést**

Feltűntek a promptokban az XML tagek? Nem díszítés. A modellek megbízhatóbban követik a strukturált utasításokat, mint a szabad szöveget. Ha többlépcsős folyamatokra vagy összetett logikára van szükség, a szerkezet segíti a modellt nyomon követni, hol tart és mi jön ezután.

<img src="../../../translated_images/hu/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Szerkezet" width="800"/>

*Egy jól strukturált prompt anatómiája, világos szakaszokkal és XML-stílusú szervezéssel*

**Minőség Önértékeléssel**

Az önkristályos minták úgy működnek, hogy explicit módon teszik meg a minőségi kritériumokat. Ahelyett, hogy azt remélnéd, a modell "helyesen csinálja", megmondod neki pontosan, mit jelent a "helyes": helyes logika, hibakezelés, teljesítmény, biztonság. A modell így képes saját kimenetét értékelni és javítani. Ez a kódgenerálást lottóból folyamattá alakítja.

**A Kontextus Véges**

A többlépéses beszélgetések úgy működnek, hogy minden kéréshez üzenet előzményt csatolnak. De van egy határ – minden modellnek van maximális token száma. Ahogy a beszélgetések nőnek, stratégiákra lesz szükség a releváns kontextus megőrzésére anélkül, hogy elérnéd a küszöböt. Ez a modul megmutatja, hogyan működik a memória; később megtanulod, mikor foglalj össze, mikor felejts és mikor kérj vissza.

## Következő lépések

**Következő modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigáció:** [← Előző: Modul 01 - Bevezetés](../01-introduction/README.md) | [Vissza a főoldalra](../README.md) | [Következő: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Nyilatkozat**:
Ez a dokumentum a [Co-op Translator](https://github.com/Azure/co-op-translator) AI fordító szolgáltatásával készült. Bár az igényességre törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az anyanyelvi változatában tekintendő hiteles forrásnak. Fontos információk esetén profi, emberi fordítást javaslunk. Nem vállalunk felelősséget semmilyen félreértésért vagy félreértelmezésért, amely ezen fordítás használatából ered.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->