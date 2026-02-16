# Modul 02: Prompt Mérnökség GPT-5.2-vel

## Tartalomjegyzék

- [Mit fogsz megtanulni](../../../02-prompt-engineering)
- [Előfeltételek](../../../02-prompt-engineering)
- [A prompt mérnökség megértése](../../../02-prompt-engineering)
- [A prompt mérnökség alapjai](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Gondolatlánc](../../../02-prompt-engineering)
  - [Szerepalapú promptolás](../../../02-prompt-engineering)
  - [Prompt sablonok](../../../02-prompt-engineering)
- [Haladó minták](../../../02-prompt-engineering)
- [Meglévő Azure erőforrások használata](../../../02-prompt-engineering)
- [Alkalmazás képernyőképek](../../../02-prompt-engineering)
- [Minták felfedezése](../../../02-prompt-engineering)
  - [Alacsony vs magas lelkesedés](../../../02-prompt-engineering)
  - [Feladatvégrehajtás (Eszköz bevezetők)](../../../02-prompt-engineering)
  - [Önreflektáló kód](../../../02-prompt-engineering)
  - [Strukturált elemzés](../../../02-prompt-engineering)
  - [Többfordulós csevegés](../../../02-prompt-engineering)
  - [Lépésről lépésre történő érvelés](../../../02-prompt-engineering)
  - [Korlátozott kimenet](../../../02-prompt-engineering)
- [Valójában mit tanulsz](../../../02-prompt-engineering)
- [Következő lépések](../../../02-prompt-engineering)

## Mit fogsz megtanulni

<img src="../../../translated_images/hu/what-youll-learn.c68269ac048503b2.webp" alt="Mit fogsz megtanulni" width="800"/>

Az előző modulban láttad, hogyan teszi lehetővé a memória a beszélgető AI-t, és használtad a GitHub Modelleket alapvető interakciókra. Most arra koncentrálunk, hogyan teszel fel kérdéseket — maguk a promptok — az Azure OpenAI GPT-5.2 használatával. Az, ahogyan strukturálod a promptokat, drámaian befolyásolja a kapott válaszok minőségét. Először áttekintjük az alapvető promptolási technikákat, majd nyolc haladó mintát mutatunk be, amelyek teljes mértékben kihasználják a GPT-5.2 képességeit.

A GPT-5.2-t használjuk, mert bevezeti az érvelés vezérlését — megmondhatod a modellnek, mennyi gondolkodást végezzen válaszadás előtt. Ez átláthatóbbá teszi a különböző promptolási stratégiákat, és segít megérteni, mikor melyik megközelítést érdemes használni. Emellett az Azure kevesebb korlátozással jár GPT-5.2 esetén, mint a GitHub Modellek.

## Előfeltételek

- Befejezett 01-es modul (Azure OpenAI erőforrások telepítve)
- `.env` fájl a gyökérkönyvtárban az Azure hitelesítő adatokkal (az `azd up` által létrehozva a 01-es modulban)

> **Megjegyzés:** Ha még nem fejezted be az 01-es modult, először kövesd ott a telepítési utasításokat.

## A prompt mérnökség megértése

<img src="../../../translated_images/hu/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Mi a prompt mérnökség?" width="800"/>

A prompt mérnökség arról szól, hogy úgy tervezzük meg a bemeneti szöveget, hogy konzisztensen megkapjuk a kívánt eredményeket. Nem csak kérdéseket teszünk fel — hanem úgy strukturáljuk a kéréseket, hogy a modell pontosan értse, mit akarunk és hogyan szolgáltassa azt.

Gondolj rá úgy, mintha egy kollégának adnál utasítást. A „Javítsd ki a hibát” homályos. A „Javítsd ki a nullpointer kivételt a UserService.java 45. sorában, null ellenőrzés hozzáadásával” pontos. A nyelvi modellek ugyanígy működnek — a pontosság és a struktúra számít.

<img src="../../../translated_images/hu/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Hogyan illeszkedik a LangChain4j" width="800"/>

A LangChain4j biztosítja az infrastruktúrát — modellkapcsolatokat, memóriát és üzenettípusokat — miközben a prompt minták csak gondosan strukturált szövegek, amelyeket ezen az infrastruktúrán keresztül küldesz. A kulcsfontosságú építőelemek a `SystemMessage` (amely beállítja az AI viselkedését és szerepét) és a `UserMessage` (amely tartalmazza a tényleges kérésedet).

## A prompt mérnökség alapjai

<img src="../../../translated_images/hu/five-patterns-overview.160f35045ffd2a94.webp" alt="Öt prompt mérnökségi minta áttekintése" width="800"/>

Mielőtt belevágnánk a modul haladó mintáiba, tekintsük át az öt alapvető promptolási technikát. Ezek az építőelemek, amelyeket minden prompt mérnöknek ismernie kell. Ha már dolgoztál a [Gyors indítás modulban](../00-quick-start/README.md#2-prompt-patterns), láthattad őket működés közben — itt a mögöttes koncepcionális keretrendszer.

### Zero-Shot Prompting

A legegyszerűbb megközelítés: adj a modellnek közvetlen utasítást példa nélkül. A modell teljes egészében a képzésére támaszkodik a feladat megértésében és végrehajtásában. Ez jól működik egyszerű kéréseknél, ahol a várt viselkedés nyilvánvaló.

<img src="../../../translated_images/hu/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Közvetlen utasítás példák nélkül — a modell kizárólag az utasításból következteti a feladatot*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Válasz: "Pozitív"
```

**Mikor használd:** Egyszerű osztályozásokhoz, közvetlen kérdésekhez, fordításokhoz, vagy bármilyen feladathoz, amelyet a modell további útmutatás nélkül tud kezelni.

### Few-Shot Prompting

Adj példákat, amelyek bemutatják a modelltől elvárt mintát. A modell megtanulja a várt bemeneti-kimeneti formát a példáidból, és annak alapján alkalmazza új bemenetekre. Ez drámaian javítja a konzisztenciát olyan feladatoknál, ahol a kívánt formátum vagy viselkedés nem nyilvánvaló.

<img src="../../../translated_images/hu/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Példákból tanulva — a modell felismeri a mintát és alkalmazza új bemeneteken*

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

**Mikor használd:** Egyedi osztályozáshoz, konzisztens formázáshoz, domain-specifikus feladatokhoz, vagy amikor a zero-shot eredmények következetlenek.

### Gondolatlánc

Kérd meg a modellt, hogy mutassa meg az érvelését lépésről lépésre. Ahelyett, hogy egyből válaszolna, a modell lebontja a problémát és explicit módon dolgozik át minden részt. Ez javítja a pontosságot matematikai, logikai és többlépéses érvelési feladatoknál.

<img src="../../../translated_images/hu/chain-of-thought.5cff6630e2657e2a.webp" alt="Gondolatlánc promptolás" width="800"/>

*Lépésről lépésre történő érvelés — összetett problémák explicit logikai lépésekre bontása*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// A modell azt mutatja: 15 - 8 = 7, majd 7 + 12 = 19 alma
```

**Mikor használd:** Matematikai feladatokhoz, logikai rejtvényekhez, hibakereséshez, vagy bármilyen feladathoz, ahol az érvelési folyamat bemutatása javítja a pontosságot és a bizalmat.

### Szerepalapú promptolás

Állíts be egy személyiséget vagy szerepet az AI számára, mielőtt feltennéd a kérdésedet. Ez olyan kontextust biztosít, amely alakítja a válasz hangvételét, mélységét és fókuszát. Egy „szoftverarchitekt” más tanácsot ad, mint egy „junior fejlesztő” vagy egy „biztonsági ellenőr”.

<img src="../../../translated_images/hu/role-based-prompting.a806e1a73de6e3a4.webp" alt="Szerepalapú promptolás" width="800"/>

*Kontextus és személyiség beállítása — ugyanaz a kérdés eltérő választ kap a kiosztott szereptől függően*

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

**Mikor használd:** Kódáttekintésekhez, oktatáshoz, domain-specifikus elemzésekhez, vagy amikor válaszokra van szükséged, amelyek igazodnak egy adott szakmai szinthez vagy nézőponthoz.

### Prompt sablonok

Hozz létre újrahasználható promptokat változó helyőrzőkkel. Ahelyett, hogy minden alkalommal új promptot írnál, egyszer definiálj egy sablont, majd töltsd fel különböző értékekkel. A LangChain4j `PromptTemplate` osztálya ezt megkönnyíti a `{{variable}}` szintaxissal.

<img src="../../../translated_images/hu/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt sablonok" width="800"/>

*Újrahasználható promptok változó helyőrzőkkel — egy sablon, sok felhasználás*

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

**Mikor használd:** Ismétlődő lekérdezésekhez különböző bemenetekkel, kötegelt feldolgozáshoz, újrahasználható AI munkafolyamatok építéséhez, vagy bármilyen helyzetben, ahol a prompt struktúrája változatlan, de az adatok változnak.

---

Ezek az öt alapvető szabályzat egy erős eszköztárat adnak a legtöbb promptolási feladathoz. A modul többi része ezekre épít, nyolc **haladó mintával**, amelyek kihasználják a GPT-5.2 érvelés vezérlését, önértékelését és strukturált kimenet képességeit.

## Haladó minták

Az alapok lefektetése után lépjünk át a nyolc haladó mintára, amelyek ezt a modult egyedivé teszik. Nem minden probléma igényli ugyanazt a megközelítést. Egyes kérdések gyors válaszokat igényelnek, mások mély gondolkodást. Egyesek látható érvelést szeretnének, mások csak eredményeket. Az alábbi minták mindegyike más forgatókönyv igényeire van optimalizálva — és a GPT-5.2 érvelés vezérlése még markánsabbá teszi a különbségeket.

<img src="../../../translated_images/hu/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Nyolc prompt mérnökségi minta" width="800"/>

*A nyolc prompt mérnökségi minta és azok használati eseteinek áttekintése*

<img src="../../../translated_images/hu/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Érvelés vezérlés GPT-5.2-vel" width="800"/>

*A GPT-5.2 érvelés vezérlése lehetővé teszi, hogy meghatározd, mennyi gondolkodást végezzen a modell — a gyors közvetlen válaszoktól a mély feltárásig*

<img src="../../../translated_images/hu/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Érvelési erőfeszítés összehasonlítása" width="800"/>

*Alacsony lelkesedés (gyors, közvetlen) vs magas lelkesedés (alapos, feltáró) érvelési megközelítések*

**Alacsony lelkesedés (Gyors és fókuszált)** – Egyszerű kérdésekhez, ahol gyors, közvetlen válaszokat szeretnél. A modell minimális érvelést végez – maximum 2 lépést. Használd ezt számításokra, lekérdezésekre vagy egyszerű kérdésekhez.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Fedezd fel a GitHub Copilot-tal:** Nyisd meg a [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)-t és kérdezd meg:
> - „Mi a különbség az alacsony és magas lelkesedésű prompt minták között?”
> - „Hogyan segítik az XML címkék a promptok az AI válaszának strukturálását?”
> - „Mikor használjam az önreflexiós mintákat a közvetlen utasítás helyett?”

**Magas lelkesedés (Mély és alapos)** – Összetett problémákhoz, ahol átfogó elemzést szeretnél. A modell alaposan feltárja a kérdést, részletes érvelést mutat. Használd rendszertervezéshez, architektúra döntésekhez vagy komplex kutatásokhoz.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Feladat végrehajtás (Lépésről lépésre haladás)** – Többlépéses munkafolyamatokhoz. A modell előzetes tervet ad, narrálja a lépéseket munka közben, majd összefoglalót ad. Használd migrációkhoz, implementációkhoz vagy bármilyen többlépéses folyamathoz.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

A Gondolatlánc promptolás kifejezetten kéri, hogy a modell mutassa be az érvelési folyamatát, ami javítja az összetett feladatok pontosságát. A lépésenkénti bontás segíti az embereket és az AI-t is a logika megértésében.

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Csevegéssel:** Kérdezz rá erre a mintára:
> - „Hogyan adaptálnám a feladat végrehajtás mintát hosszú futású műveletekhez?”
> - „Milyen bevált gyakorlatok vannak az eszköz bevezetők strukturálására éles alkalmazásokban?”
> - „Hogyan lehet közbenső előrehaladási frissítéseket rögzíteni és megjeleníteni UI-ban?”

<img src="../../../translated_images/hu/task-execution-pattern.9da3967750ab5c1e.webp" alt="Feladat végrehajtási minta" width="800"/>

*Terv → Végrehajtás → Összefoglalás munkafolyamat többlépéses feladatokhoz*

**Önreflektáló kód** – Termelő minőségű kód generálásához. A modell kódot generál, értékeli a minőséget, és iteratívan javítja. Használd új funkciók vagy szolgáltatások építéséhez.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/hu/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Önreflexiós ciklus" width="800"/>

*Iteratív javítási ciklus - generálás, értékelés, problémák azonosítása, fejlesztés, ismétlés*

**Strukturált elemzés** – Konzisztens értékeléshez. A modell átvizsgálja a kódot egy fix keretrendszer szerint (helyesség, gyakorlatok, teljesítmény, biztonság). Használd kódáttekintésekhez vagy minőségértékelésekhez.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Csevegést:** Kérdezz rá a strukturált elemzésre:
> - „Hogyan személyre szabhatom az elemzési keretrendszert különböző típusú kódáttekintésekhez?”
> - „Mi a legjobb módja a strukturált kimenetek programozott feldolgozásának és reagálásának?”
> - „Hogyan biztosíthatom az egységes súlyossági szinteket különböző áttekintő ülések között?”

<img src="../../../translated_images/hu/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Strukturált elemzési minta" width="800"/>

*Négy kategóriás keretrendszer a konzisztens kódáttekintéshez, súlyossági szintekkel*

**Többfordulós csevegés** – Kontextust igénylő beszélgetésekhez. A modell emlékszik az előző üzenetekre és azok alapján építkezik. Használd interaktív segítségnyújtásra vagy komplex kérdés-felelet szekvenciákhoz.

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

*Hogyan gyűlik össze a beszélgetési kontextus többlépéses váltások során, egészen a token limitig*

**Lépésről lépésre történő érvelés** – Látható logikát igénylő problémákhoz. A modell explicit módon mutatja be az érvelést minden lépésnél. Használd matematikai feladatokhoz, logikai rejtvényekhez vagy amikor meg kell értened a gondolkodási folyamatot.

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

**Korlátozott kimenet** – Olyan válaszokhoz, amelyek konkrét formátumkövetelményeket támasztanak. A modell szigorúan követi a formázási és hosszúsági szabályokat. Használd összefoglalókhoz vagy ahol pontos kimeneti struktúra kell.

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

*Konkrét formátum, hosszúság és struktúrakövetelmények érvényesítése*

## Meglévő Azure erőforrások használata

**Ellenőrizd a telepítést:**

Győződj meg róla, hogy a `.env` fájl létezik a gyökérkönyvtárban az Azure hitelesítő adatokkal (a 01-es modul során létrejött):
```bash
cat ../.env  # Meg kell jeleníteni az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**Indítsd el az alkalmazást:**

> **Megjegyzés:** Ha már elindítottad az összes alkalmazást a `./start-all.sh` segítségével az 01-es modulból, akkor ez a modul már fut a 8083-as porton. Kihagyhatod az alábbi indítási parancsokat, és közvetlenül megnyithatod a http://localhost:8083 címet.

**1. lehetőség: Spring Boot Dashboard használata (ajánlott VS Code felhasználóknak)**

A fejlesztői konténer tartalmazza a Spring Boot Dashboard kiegészítőt, amely egy vizuális felületet biztosít az összes Spring Boot alkalmazás kezeléséhez. Ezt a VS Code bal oldali tevékenységsávjában találod meg (keresd a Spring Boot ikont).
A Spring Boot műszerfalról az alábbiakat teheti meg:
- Megtekintheti a munkaterületen elérhető összes Spring Boot alkalmazást
- Egyetlen kattintással indíthatja/leállíthatja az alkalmazásokat
- Valós időben megtekintheti az alkalmazás naplóit
- Figyelemmel kísérheti az alkalmazás állapotát

Egyszerűen kattintson a lejátszás gombra a "prompt-engineering" mellett ezen modul indításához, vagy indítsa el az összes modult egyszerre.

<img src="../../../translated_images/hu/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**2. lehetőség: Shell szkriptek használata**

Indítsa el az összes webalkalmazást (01-04 modulok):

**Bash:**
```bash
cd ..  # Gyökérkönyvtárból
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # A gyökérkönyvtárból
.\start-all.ps1
```

Vagy indítsa el csak ezt a modult:

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

Mindkét szkript automatikusan betölti a környezeti változókat a gyökér `.env` fájlból, és építi a JAR fájlokat, ha még nem léteznek.

> **Megjegyzés:** Ha inkább manuálisan szeretné elkészíteni az összes modult az indítás előtt:
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

Nyissa meg böngészőjében a http://localhost:8083 oldalt.

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

*A fő műszerfal, amely az összes 8 prompt mérnöki mintát mutatja jellemzőikkel és felhasználási esetekkel*

## Minták felfedezése

A webes felület lehetővé teszi különböző promptolási stratégiák kipróbálását. Minden minta más-más problémákat old meg - próbálja ki, mikor melyik megközelítés működik a legjobban.

### Alacsony vs. Magas Késztetés

Tegyen fel egy egyszerű kérdést, például "Mi a 15%-a 200-nak?" Alacsony Késztetés mellett. Azonnali, közvetlen választ kap. Most kérdezzen egy bonyolultat, például "Tervezzen gyorsítótár-stratégiát egy nagy forgalmú API-hoz" Magas Késztetés módban. Figyelje, hogyan lassul a modell, és részletes magyarázatot ad. Ugyanaz a modell, ugyanaz a kérdés szerkezet - a prompt megmondja, mennyi gondolkodást vár el.

<img src="../../../translated_images/hu/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Gyors számítás minimális érveléssel*

<img src="../../../translated_images/hu/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Átfogó gyorsítótár-stratégia (2.8MB)*

### Feladatvégrehajtás (Eszköz Bevezető Szövegek)

A több lépésből álló munkafolyamatok előnyét élvezik az előzetes tervezés és az előrehaladás elbeszélése. A modell vázolja, mit fog csinálni, narrálja az egyes lépéseket, majd összegzi az eredményeket.

<img src="../../../translated_images/hu/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*REST végpont létrehozása lépésről lépésre narrálva (3.9MB)*

### Önrreflektáló Kód

Próbálja ki: "Hozzon létre egy e-mail érvényesítési szolgáltatást". Ahelyett, hogy csak kódot generálna és megállna, a modell generál, értékel a minőségi kritériumok alapján, azonosít gyengeségeket, és javít. Láthatja, hogyan iterál addig, amíg a kód megfelel a termelési szabványoknak.

<img src="../../../translated_images/hu/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Teljes e-mail érvényesítési szolgáltatás (5.2MB)*

### Strukturált Elemzés

A kódellenőrzéshez konzisztens értékelési keretrendszerek kellenek. A modell előre definiált kategóriák (helyesség, gyakorlatok, teljesítmény, biztonság) szerint elemzi a kódot, súlyossági szintekkel.

<img src="../../../translated_images/hu/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Keretrendszer alapú kódellenőrzés*

### Több Lépéses Csevegés

Kérdezze meg: "Mi az a Spring Boot?" majd rögtön utána: "Mutass példát." A modell emlékszik az első kérdésére, és konkrét Spring Boot példát ad. Memória nélkül a második kérdés túl általános lett volna.

<img src="../../../translated_images/hu/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Kontektsmegőrzés kérdések között*

### Lépésről Lépésre Érvelés

Válasszon egy matematikai feladatot, és próbálja ki mind a Lépésről Lépésre Érveléssel, mind Alacsony Késztetéssel. Az alacsony késztetés csak a választ adja meg - gyors, de átláthatatlan. A lépésről lépésre mutat minden számítást és döntést.

<img src="../../../translated_images/hu/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matematikai feladat explicit lépésekkel*

### Korlátozott Kimenet

Ha specifikus formátumokra vagy szószámra van szükség, ez a minta szigorúan betartatja az előírásokat. Próbáljon meg generálni egy összefoglalót pontosan 100 szóban, pontokba szedve.

<img src="../../../translated_images/hu/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Gépi tanulási összefoglaló formátumszabályozással*

## Amit Valóban Tanul

**Az Érvelési Erőfeszítés Mindent Megváltoztat**

A GPT-5.2 lehetővé teszi, hogy promptok segítségével szabályozza a számítási erőfeszítést. Az alacsony erőfeszítés gyors választ jelent minimális feltárással. A magas erőfeszítés azt, hogy a modell időt szán mély gondolkodásra. Megtanulja az erőfeszítést igazítani a feladat összetettségéhez - egyszerű kérdéseknél ne pazarolja az időt, de bonyolult döntéseknél se siessen el semmit.

**A Szerkezet Irányítja a Viselkedést**

Észrevette az XML tageket a promptokban? Nem dekorációk. A modellek megbízhatóbban követnek strukturált utasításokat, mint szabad szöveget. Ha több lépéses folyamatokra vagy összetett logikára van szükség, a szerkezet segít a modellnek nyomon követni, hol tart és mi következik.

<img src="../../../translated_images/hu/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Egy jól strukturált prompt anatómiája tiszta szekciókkal és XML-stílusú rendezéssel*

**Minőség Önerőltetéssel**

Az önreflektáló minták úgy működnek, hogy a minőségi kritériumokat explicitté teszik. Ahelyett, hogy bíznánk benne, hogy "jól csinálja", megmondja pontosan, mit jelent a "jól": helyes logika, hibakezelés, teljesítmény, biztonság. A modell így értékelheti saját kimenetét és javíthatja azt. Ez a kódgenerálást szerencsejátékról folyamatra változtatja.

**A Kontextus Véges**

A több lépéses beszélgetések úgy működnek, hogy minden kéréshez történetüzenet történik. De van egy határ - minden modellnek maximális token számkorlátja van. Ahogy nő a beszélgetés, stratégiák kellenek, hogy a releváns kontextust megtartsa anélkül, hogy túllépné ezt a korlátot. Ez a modul megmutatja, hogyan működik a memória; később megtanulja, mikor foglalja össze, mikor felejt, és mikor hívja elő.

## Következő lépések

**Következő modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigáció:** [← Előző: Modul 01 - Bevezetés](../01-introduction/README.md) | [Vissza a főoldalra](../README.md) | [Következő: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Jogi nyilatkozat**:
Ez a dokumentum az [Co-op Translator](https://github.com/Azure/co-op-translator) AI fordító szolgáltatásával készült. Bár igyekszünk pontos fordítást biztosítani, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az anyanyelvén tekintendő hivatalos forrásnak. Fontos információk esetén javasolt szakmai, emberi fordítás igénybevétele. Nem vállalunk felelősséget a fordítás használatából eredő félreértésekért vagy félreértelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->