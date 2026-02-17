# Modul 02: Prompt Műszaki Tervezés GPT-5.2-vel

## Tartalomjegyzék

- [Mit fogsz megtanulni](../../../02-prompt-engineering)
- [Előfeltételek](../../../02-prompt-engineering)
- [A prompt műszaki tervezés megértése](../../../02-prompt-engineering)
- [Prompt műszaki tervezés alapjai](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Gondolatmenet láncolat](../../../02-prompt-engineering)
  - [Szerepalapú promptolás](../../../02-prompt-engineering)
  - [Prompt sablonok](../../../02-prompt-engineering)
- [Haladó minták](../../../02-prompt-engineering)
- [Meglévő Azure erőforrások használata](../../../02-prompt-engineering)
- [Alkalmazás képernyőképei](../../../02-prompt-engineering)
- [A minták felfedezése](../../../02-prompt-engineering)
  - [Alacsony vs magas eltökéltség](../../../02-prompt-engineering)
  - [Feladat végrehajtás (eszköz-kezdetek)](../../../02-prompt-engineering)
  - [Önreflektáló kód](../../../02-prompt-engineering)
  - [Strukturált elemzés](../../../02-prompt-engineering)
  - [Többmenetes csevegés](../../../02-prompt-engineering)
  - [Lépésről lépésre való gondolkodás](../../../02-prompt-engineering)
  - [Korlátozott kimenet](../../../02-prompt-engineering)
- [Mit is tanulsz valójában](../../../02-prompt-engineering)
- [Következő lépések](../../../02-prompt-engineering)

## Mit fogsz megtanulni

<img src="../../../translated_images/hu/what-youll-learn.c68269ac048503b2.webp" alt="Mit fogsz megtanulni" width="800"/>

Az előző modulban láttad, hogyan teszi lehetővé a memória a beszélgető AI-t, és használtad a GitHub Modelleket alapvető interakciókra. Most arra koncentrálunk, hogyan teszel fel kérdéseket — maga a prompt technika — az Azure OpenAI GPT-5.2 segítségével. A promptok struktúrája drámaian befolyásolja a válaszok minőségét. Kezdünk az alapvető prompt technikák áttekintésével, majd áttérünk nyolc haladó mintára, amelyek teljes mértékben kihasználják a GPT-5.2 képességeit.

A GPT-5.2-t használjuk, mert bevezeti az érvelés szabályozását – megmondhatod a modellnek, mennyi gondolkodást végezzen a válasz előtt. Ez a különböző prompt stratégiákat jobban láthatóvá teszi, és segít megérteni, mikor melyiket érdemes használni. Az Azure emellett kevesebb korlátozással bír a GPT-5.2 esetében a GitHub Modellekhez képest.

## Előfeltételek

- Az 01-es modul befejezve (Azure OpenAI erőforrások telepítve)
- `.env` fájl a gyökérkönyvtárban Azure hitelesítő adatokkal (`azd up` által létrehozva az 01-es modulban)

> **Megjegyzés:** Ha nem fejezted be az 01-es modult, először kövesd ott a telepítési utasításokat.

## A prompt műszaki tervezés megértése

<img src="../../../translated_images/hu/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Mi a Prompt Műszaki Tervezés?" width="800"/>

A prompt műszaki tervezés arról szól, hogy olyan bemeneti szöveget alakíts ki, amely következetesen megadja a kívánt eredményeket. Nem csak kérdéseket teszel fel — hanem úgy struktúrálod a kéréseket, hogy a modell pontosan megértse, mit akarsz, és hogyan teljesítse azt.

Gondolj rá úgy, mint amikor utasítást adsz egy kollégának. Az, hogy „Javítsd a hibát” homályos. Azt mondani, hogy „Javítsd a null pointer kivételt a UserService.java 45. sorában egy null ellenőrzés hozzáadásával” már konkrét. A nyelvi modellek is így működnek – számít a részletesség és a struktúra.

<img src="../../../translated_images/hu/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Hogy illeszkedik a LangChain4j" width="800"/>

A LangChain4j biztosítja az infrastruktúrát — modellkapcsolatokat, memóriát és üzenettípusokat — míg a prompt minták csak gondosan strukturált szövegek, amelyeket ezen az infrastruktúrán keresztül küldesz. A kulcsfontosságú építőelemek a `SystemMessage` (amely beállítja az AI viselkedését és szerepét) és a `UserMessage` (amely maga a kérésedet tartalmazza).

## Prompt műszaki tervezés alapjai

<img src="../../../translated_images/hu/five-patterns-overview.160f35045ffd2a94.webp" alt="Öt Prompt Műszaki Tervezési Minta Áttekintése" width="800"/>

Mielőtt belevágnánk a modul haladó mintáiba, tekintsük át az öt alapvető prompt technikát. Ezek az alapkövei minden prompt mérnöki feladatnak. Ha már dolgoztál a [Gyors Kezdés modullal](../00-quick-start/README.md#2-prompt-patterns), akkor láttad őket használat közben — itt a mögöttes koncepcionális keretrendszerük.

### Zero-Shot Prompting

A legegyszerűbb megközelítés: közvetlen utasítást adsz a modellnek példa nélkül. A modell kizárólag a tanítása alapján érti meg és hajtja végre a feladatot. Ez jól működik egyszerű kéréseknél, ahol az elvárt viselkedés egyértelmű.

<img src="../../../translated_images/hu/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Közvetlen utasítás példák nélkül — a modell az utasításból következtet maga a feladatra*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Válasz: "Pozitív"
```

**Mikor használd:** Egyszerű osztályozások, közvetlen kérdések, fordítások vagy bármilyen feladat, amelyet a modell további útmutatás nélkül kezel.

### Few-Shot Prompting

Adj meg példákat, amelyek bemutatják a modelltől elvárt mintát. A modell megtanulja az elvárt bemenet-kimenet formátumot a példáid alapján, és alkalmazza új bemenetekre. Ez jelentősen javítja a következetességet olyan feladatoknál, ahol a kívánt formátum vagy viselkedés nem nyilvánvaló.

<img src="../../../translated_images/hu/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Példákból tanulás — a modell felismeri a mintát és alkalmazza új bemeneteken*

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

**Mikor használd:** Egyedi osztályozásokhoz, következetes formázáshoz, speciális feladatoknál vagy amikor a zero-shot eredmények nem következetesek.

### Gondolatmenet láncolat

Kérd meg a modellt, hogy mutassa meg lépésről lépésre a gondolkodását. Ahelyett, hogy azonnal a válaszra ugorna, a modell lebontja a problémát és minden részt külön kifejt. Ez javítja a pontosságot matematikai, logikai és több lépéses gondolkodású feladatoknál.

<img src="../../../translated_images/hu/chain-of-thought.5cff6630e2657e2a.webp" alt="Gondolatmenet láncolat" width="800"/>

*Lépésenkénti érvelés — összetett problémák explicit logikai részekre bontása*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// A modell megmutatja: 15 - 8 = 7, majd 7 + 12 = 19 alma
```

**Mikor használd:** Matematikai problémák, logikai rejtvények, hibakeresés vagy bármilyen feladat, ahol az érvelési folyamat megjelenítése növeli a pontosságot és a bizalmat.

### Szerepalapú promptolás

Állíts be egy személyiséget vagy szerepet az AI-nak, mielőtt feltennéd a kérdésed. Ez kontextust ad, amely alakítja a válasz stílusát, mélységét és fókuszát. Egy "szoftver architekt" más tanácsot ad, mint egy "junior fejlesztő" vagy egy "biztonsági auditor".

<img src="../../../translated_images/hu/role-based-prompting.a806e1a73de6e3a4.webp" alt="Szerepalapú promptolás" width="800"/>

*Kontextus és személyiség beállítása — ugyanaz a kérdés különböző választ kap a szereptől függően*

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

**Mikor használd:** Kódáttekintésekhez, oktatáshoz, speciális elemzésekhez vagy ha adott szakértelemi szinthez vagy nézőponthoz igazított válaszokra van szükség.

### Prompt sablonok

Hozz létre újrahasználható promptokat változó helykitöltőkkel. Egy új prompt írása helyett egyszer definiálsz egy sablont, majd különböző értékeket töltesz bele. A LangChain4j `PromptTemplate` osztálya ezt egyszerűvé teszi `{{variable}}` szintaxissal.

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

**Mikor használd:** Ismétlődő lekérdezések különböző bemenetekkel, kötegelt feldolgozás, újrahasználható AI munkafolyamatok építése vagy bármilyen helyzet, ahol a prompt struktúrája ugyanaz marad, de az adatok változnak.

---

Ez az öt alapvető minta erős eszköztárat nyújt a legtöbb promptolási feladathoz. A modul további része nyolc haladó mintával bővíti, amelyek a GPT-5.2 érvelési szabályozását, önértékelését és strukturált kimeneti képességeit használják ki.

## Haladó minták

Az alapok után térjünk rá azokra a nyolc haladó mintára, amelyek egyedivé teszik ezt a modult. Nem minden problémához ugyanaz a megközelítés kell. Egyes kérdések gyors válaszokat igényelnek, mások mély gondolkodást. Egyesek látható érvelést várnak el, míg másoknak csak az eredmény kell. Az alábbi minták mind egy-egy különböző helyzethez vannak optimalizálva — és a GPT-5.2 érvelési szabályozása még inkább hangsúlyossá teszi a különbségeket.

<img src="../../../translated_images/hu/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Nyolc promptolási minta" width="800"/>

*Áttekintés a nyolc prompt műszaki tervezési mintáról és alkalmazási eseteikről*

<img src="../../../translated_images/hu/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Érvelés szabályozás GPT-5.2-vel" width="800"/>

*A GPT-5.2 érvelés szabályozásával megadhatod, mennyit gondolkodjon a modell — a gyors közvetlen válaszoktól a mély feltárásig*

<img src="../../../translated_images/hu/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Érvelés erőfeszítés összehasonlítása" width="800"/>

*Alacsony eltökéltség (gyors, közvetlen) vs magas eltökéltség (alapos, feltáró) érvelési megközelítések*

**Alacsony eltökéltség (Gyors és fókuszált)** - Egyszerű kérdésekhez, ahol gyors, közvetlen válaszokat szeretnél. A modell minimális érvelést végez - legfeljebb 2 lépést. Számításokhoz, adatlekérdezésekhez vagy egyenes kérdésekhez használd.

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

> 💡 **Fedezd fel GitHub Copilot-tal:** Nyisd meg a [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) fájlt és kérdezd meg:
> - „Mi a különbség az alacsony és a magas eltökéltségű prompt minták között?”
> - „Hogyan segítik az XML tagek a promptok AI válaszának struktúrálását?”
> - „Mikor érdemes az önreflektáló mintákat használni a közvetlen utasítás helyett?”

**Magas eltökéltség (Mély és alapos)** - Bonyolult problémákhoz, ahol átfogó elemzést szeretnél. A modell alaposan feltárja a témát és részletes érvelést mutat. Rendszertervezéshez, architektúra döntésekhez vagy komplex kutatáshoz használd.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Feladat végrehajtás (Lépésről lépésre haladás)** - Többlépcsős munkafolyamatokhoz. A modell előre megtervezi a lépéseket, narrálja mindegyiket munka közben, majd összefoglalót ad. Használd migrációkhoz, implementációkhoz vagy bármilyen több lépéses folyamathoz.

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

A Gondolatmenet láncolat prompt kifejezetten kéri a modellt, hogy mutassa meg érvelési folyamatát, ami javítja a pontosságot összetett feladatoknál. A lépésenkénti bontás segíti mind az embert, mind az AI-t a logika megértésében.

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Csevegéssel:** Kérdezz erről a mintáról:
> - „Hogyan adaptálnám a feladat végrehajtási mintát hosszan futó műveletekhez?”
> - „Mik a legjobb gyakorlatok a eszköz előszavak strukturálásához gyártási alkalmazásokban?”
> - „Hogyan tudom rögzíteni és megjeleníteni a közbenső előrehaladás frissítéseket a UI-ban?”

<img src="../../../translated_images/hu/task-execution-pattern.9da3967750ab5c1e.webp" alt="Feladat Végrehajtási Minta" width="800"/>

*Tervezés → Végrehajtás → Összefoglalás munkafolyamat több lépéses feladatokhoz*

**Önreflektáló kód** - Termelési minőségű kód generálásához. A modell betartja a gyártási szabványokat, megfelelő hibakezeléssel. Használd új funkciók vagy szolgáltatások készítéséhez.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/hu/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Önreflexiós ciklus" width="800"/>

*Iteratív fejlesztési ciklus – generálás, értékelés, hibák azonosítása, javítás, ismétlés*

**Strukturált elemzés** - Következetes értékeléshez. A modell egy rögzített keretrendszerrel vizsgálja a kódot (helyesség, gyakorlatok, teljesítmény, biztonság, karbantarthatóság). Használd kódáttekintésekhez vagy minőségértékeléshez.

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

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Csevegéssel:** Kérdezz a strukturált elemzésről:
> - „Hogyan testreszabhatom az elemzési keretrendszert különböző kódáttekintési típusokhoz?”
> - „Mi a legjobb módja a strukturált kimenet programozott elemzésének és kezelésének?”
> - „Hogyan biztosítható a következetes súlyossági szint különböző áttekintési alkalmak között?”

<img src="../../../translated_images/hu/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Strukturált Elemzési Minta" width="800"/>

*Kódáttekintések keretrendszere súlyossági szintekkel*

**Többmenetes csevegés** - Olyan beszélgetésekhez, amelyek kontextust igényelnek. A modell megjegyzi a korábbi üzeneteket és erre épít. Használd interaktív segítségnyújtáshoz vagy komplex kérdés-válasz helyzetekhez.

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

*Hogyan halmozódik fel a beszélgetés kontextusa több kör alatt, amíg eléri a token limitet*

**Lépésről lépésre való gondolkodás** - Olyan problémákhoz, amelyeknél látható logika szükséges. A modell explicit módon mutatja meg minden lépés érvelését. Használd matematikai problémákhoz, logikai fejtörőkhöz vagy amikor meg kell értened a gondolkodási folyamatot.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/hu/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Lépésről lépésre Minta" width="800"/>

*Problémák explicit logikai lépésekre bontása*

**Korlátozott kimenet** - Olyan válaszokhoz, amelyekhez specifikus formátum kell. A modell szigorúan betartja a formátum és hosszúság szabályokat. Használd összefoglalókhoz vagy amikor pontos kimeneti struktúrára van szükség.

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

<img src="../../../translated_images/hu/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Korlátozott Kimeneti Minta" width="800"/>

*Specifikus formátum, hosszúság és struktúra követelmények érvényesítése*

## Meglévő Azure erőforrások használata

**Telepítés ellenőrzése:**

Győződj meg róla, hogy a `.env` fájl jelen van a gyökérkönyvtárban Azure hitelesítőkkel (az 01-es modul alatt létrehozva):
```bash
cat ../.env  # Meg kell jelenítenie az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**Az alkalmazás indítása:**

> **Megjegyzés:** Ha már elindítottad az összes alkalmazást az 01-es modulban a `./start-all.sh` segítségével, ez a modul már fut a 8083-as porton. A lentebb lévő indítási parancsokat át is ugorhatod, és közvetlenül megnézheted a http://localhost:8083 címen.

**1. lehetőség: Spring Boot Dashboard használata (ajánlott VS Code használóknak)**

A fejlesztői konténer tartalmazza a Spring Boot Dashboard bővítményt, amely vizuális felületet biztosít az összes Spring Boot alkalmazás kezelésére. A VS Code bal oldali Activity Bar-jában találod (keresd a Spring Boot ikont).
A Spring Boot Vezérlőpultból a következőket teheted:
- Megtekintheted az összes elérhető Spring Boot alkalmazást a munkaterületen
- Egyetlen kattintással elindíthatod/leállíthatod az alkalmazásokat
- Valós időben megtekintheted az alkalmazásnaplókat
- Nyomon követheted az alkalmazások állapotát

Egyszerűen kattints a "prompt-engineering" mellett lévő lejátszás gombra az adott modul indításához, vagy indítsd el az összes modult egyszerre.

<img src="../../../translated_images/hu/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**2. lehetőség: Shell szkriptek használata**

Indítsd el az összes webalkalmazást (01-04-es modulok):

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

Vagy indíts csak ezt a modult:

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

Mindkét szkript automatikusan betölti a környezeti változókat a gyökér `.env` fájlból, és ha a JAR-ok nem léteznek, le is fordítja őket.

> **Megjegyzés:** Ha inkább manuálisan szeretnéd az összes modult lefordítani az indítás előtt:
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
cd ..; .\stop-all.ps1  # Az összes modul
```

## Alkalmazás képernyőképek

<img src="../../../translated_images/hu/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*A fővezérlőpult, amely az összes 8 prompt fejlesztési mintát mutatja jellemzőikkel és felhasználási esetekkel*

## A minták felfedezése

A webes felület lehetővé teszi, hogy kipróbáld a különböző promptálási stratégiákat. Minden egyes minta más problémát old meg – próbáld ki őket, hogy lásd, mikor melyik megközelítés mutatkozik meg igazán.

### Alacsony és magas igyekezet

Tegyél fel egy egyszerű kérdést, például "Mi a 15%-a 200-nak?" Alacsony igyekezettel. Azonnal, közvetlen választ kapsz. Most kérdezz valami összetettet, például: "Tervezzen egy gyorsítótárazási stratégiát egy nagy forgalmú API-hoz" Magas igyekezettel. Nézd meg, ahogy a modell lelassul, és részletes indoklást ad. Ugyanaz a modell, ugyanaz a kérdésfelépítés – de a prompt megmondja, mennyi gondolkodást vár el.

<img src="../../../translated_images/hu/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Gyors számítás minimális érveléssel*

<img src="../../../translated_images/hu/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Átfogó gyorsítótárazási stratégia (2,8MB)*

### Feladatvégrehajtás (Eszköz bevezetők)

A több lépésből álló munkafolyamatok hasznát veszik az előzetes tervezésnek és a haladás narrálásának. A modell felvázolja, mit fog tenni, narrálja az egyes lépéseket, majd összegzi az eredményeket.

<img src="../../../translated_images/hu/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*REST végpont létrehozása lépésről lépésre (3,9MB)*

### Önállóan reflektáló kód

Próbáld ki: "Hozz létre egy e-mail érvényesítő szolgáltatást". Ahelyett, hogy csak generálna kódot és megállna, a modell generál, értékeli a minőségi kritériumok alapján, azonosítja a gyengeségeket, és javítja. Láthatod, hogy addig ismétli, amíg a kód el nem éri a gyártási szintet.

<img src="../../../translated_images/hu/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Teljes e-mail érvényesítő szolgáltatás (5,2MB)*

### Strukturált elemzés

A kódáttekintésekhez következetes értékelési keretrendszer szükséges. A modell előre definiált kategóriákban (helyesség, gyakorlatok, teljesítmény, biztonság) elemzi a kódot súlyossági szintekkel.

<img src="../../../translated_images/hu/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Keretrendszer-alapú kódáttekintés*

### Többfordulós csevegés

Kérdezd meg: "Mi az a Spring Boot?" majd azonnal kövesd a "Mutass példát!" kérdéssel. A modell emlékszik az első kérdésedre, és kifejezetten egy Spring Boot példát ad. Memória nélkül a második kérdés túl homályos lenne.

<img src="../../../translated_images/hu/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Válaszok kontextusának megőrzése kérdésenként*

### Lépésenkénti érvelés

Válassz egy matek feladatot, és próbáld ki lépésenkénti érveléssel és alacsony igyekezettel is. Az alacsony igyekezet csak az eredményt adja meg – gyors, de átláthatatlan. A lépésenkénti megmutatja a számításokat és döntéseket.

<img src="../../../translated_images/hu/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matek feladat explicit lépésekkel*

### Korlátozott kimenet

Ha konkrét formátumra vagy szószámra van szükséged, ez a minta szigorúan betartja az előírásokat. Próbálj meg egy összefoglalót generálni pontosan 100 szóban pontokba szedve.

<img src="../../../translated_images/hu/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Gépi tanulás összefoglaló formátum-vezérléssel*

## Amit valóban megtanulsz

**Az érvelési erőfeszítés mindent megváltoztat**

A GPT-5.2 lehetővé teszi, hogy a promptjaiddal szabályozd a számítási erőfeszítést. Az alacsony erőfeszítés gyors választ jelent minimális feltárással. A magas erőfeszítés azt, hogy a modell mélyen elgondolkodik. Megtanulod, hogy milyen mértékben érdemes az erőfeszítést igazítani a feladat összetettségéhez – egyszerű kérdésekhez ne pazarold az időt, de bonyolult döntésekhez se siess.

**A szerkezet irányítja a viselkedést**

Láttad az XML címkéket a promptokban? Ezek nem csupán díszek. A modellek megbízhatóbban követik a jól strukturált instrukciókat, mint a szabad szöveget. Ha több lépésből álló folyamatokra vagy komplex logikára van szükség, a szerkezet segít a modellnek nyomon követni, hol tart, és mi jön ezután.

<img src="../../../translated_images/hu/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Egy jól strukturált prompt anatómiája világos szakaszokkal és XML-stílusú rendezéssel*

**Minőség önértékeléssel**

Az önreflektáló minták úgy működnek, hogy a minőségi kritériumokat explicit módon megadják. Ahelyett, hogy csak bizakodnál, hogy a modell „jól csinálja”, pontosan megmondod neki, mit jelent a „jó”: helyes logika, hibakezelés, teljesítmény, biztonság. A modell értékelni tudja a saját kimenetét és javítani is. Ez a kódgenerálást lottóból folyamatgé teszi.

**A kontextus véges**

A többfordulós beszélgetések úgy működnek, hogy minden kéréshez hozzácsatolják az előzményeket. De van egy határ – minden modellnek van maximális token száma. Ahogy nő a beszélgetés, stratégiák kellenek a releváns kontextus megőrzésére anélkül, hogy elérnéd a plafont. Ez a modul megmutatja, hogyan működik a memória; később megtanulod, mikor foglalj össze, mikor felejts, és mikor hívd elő újra.

## Következő lépések

**Következő modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigáció:** [← Előző: 01-es modul - Bevezetés](../01-introduction/README.md) | [Vissza a főoldalra](../README.md) | [Következő: 03-as modul - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Felelősség kizárása**:  
Ezt a dokumentumot az AI fordító szolgáltatás [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével fordítottuk le. Bár a pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az anyanyelvén tekintendő irányadónak. Fontos információk esetén professzionális emberi fordítást javaslunk. Nem vállalunk felelősséget az ebből eredő félreértésekért vagy téves értelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->