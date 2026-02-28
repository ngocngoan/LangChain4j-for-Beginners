# 02-es modul: Prompt tervezés GPT-5.2-vel

## Tartalomjegyzék

- [Videó bemutató](../../../02-prompt-engineering)
- [Mit tanulsz meg](../../../02-prompt-engineering)
- [Előfeltételek](../../../02-prompt-engineering)
- [A prompt tervezés megértése](../../../02-prompt-engineering)
- [A prompt tervezés alapjai](../../../02-prompt-engineering)
  - [Zero-Shot promptolás](../../../02-prompt-engineering)
  - [Few-Shot promptolás](../../../02-prompt-engineering)
  - [Gondolatmenet láncolata](../../../02-prompt-engineering)
  - [Szerepalapú promptolás](../../../02-prompt-engineering)
  - [Prompt sablonok](../../../02-prompt-engineering)
- [Fejlett minták](../../../02-prompt-engineering)
- [Meglévő Azure erőforrások használata](../../../02-prompt-engineering)
- [Alkalmazás képernyőképek](../../../02-prompt-engineering)
- [A minták felfedezése](../../../02-prompt-engineering)
  - [Alacsony vs magas lelkesedés](../../../02-prompt-engineering)
  - [Feladatvégrehajtás (Eszköz bevezetők)](../../../02-prompt-engineering)
  - [Önreflektáló kód](../../../02-prompt-engineering)
  - [Strukturált elemzés](../../../02-prompt-engineering)
  - [Többszörös fordulós csevegés](../../../02-prompt-engineering)
  - [Lépésről lépésre gondolkodás](../../../02-prompt-engineering)
  - [Szűkített kimenet](../../../02-prompt-engineering)
- [Mit is tanulsz valójában](../../../02-prompt-engineering)
- [Következő lépések](../../../02-prompt-engineering)

## Videó bemutató

Nézd meg ezt az élő bemutatót, amely elmagyarázza, hogyan kezdj neki ennek a modulnak:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Élő bemutató" width="800"/></a>

## Mit tanulsz meg

<img src="../../../translated_images/hu/what-youll-learn.c68269ac048503b2.webp" alt="Mit tanulsz meg" width="800"/>

Az előző modulban láttad, hogyan teszi lehetővé a memória a beszélgető AI-t, és hogyan használtad a GitHub modelleket alapvető interakciókhoz. Most arra fókuszálunk, hogyan teszel fel kérdéseket — maguk a promptok — az Azure OpenAI GPT-5.2 segítségével. A promptok felépítése jelentősen befolyásolja a válaszok minőségét. Kezdjük az alapvető prompting technikák áttekintésével, majd áttérünk nyolc fejlett mintára, amelyek teljes mértékben kihasználják a GPT-5.2 képességeit.

A GPT-5.2-t használjuk, mert bevezeti az érvelésvezérlést — megmondhatod a modellnek, mennyi gondolkodást végezzen a válaszadás előtt. Ez jól láthatóbbá teszi a különböző prompting stratégiákat, és segít megérteni, mikor melyik megközelítést alkalmazd. Emellett az Azure-nak kevesebb korlátozása van a GPT-5.2-re, mint a GitHub modellekre.

## Előfeltételek

- Az 01-es modul teljesítve (Azure OpenAI erőforrások telepítve)
- `.env` fájl a gyökérkönyvtárban Azure hitelesítő adatokkal (az `azd up` által létrehozva az 01-es modulban)

> **Megjegyzés:** Ha még nem teljesítetted az 01-es modult, először kövesd ott a telepítési utasításokat.

## A prompt tervezés megértése

<img src="../../../translated_images/hu/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Mi a prompt tervezés?" width="800"/>

A prompt tervezés arról szól, hogy olyan bemeneti szöveget alkoss, amely következetesen az elvárt eredményt hozza meg. Nem csak kérdéseket feltenni - hanem úgy strukturálni a kéréseket, hogy a modell pontosan értse, mit akarsz és hogyan adja vissza.

Gondolj rá úgy, mint egy kollégának adott utasításra. Az „Javítsd ki a hibát” túl általános. A „Javítsd ki a nullpointer kivételt a UserService.java 45. sorában null ellenőrzés hozzáadásával” konkrét. A nyelvi modellek is így működnek - a konkrétság és a struktúra számít.

<img src="../../../translated_images/hu/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Hogyan illeszkedik a LangChain4j" width="800"/>

A LangChain4j biztosítja az infrastruktúrát — modellkapcsolatokat, memóriát és üzenettípusokat — miközben a prompt minták csak gondosan strukturált szövegek, amelyeket ezen az infrastruktúrán keresztül küldesz. A kulcselemek a `SystemMessage` (ami beállítja az AI viselkedését és szerepét) és a `UserMessage` (ami a saját kérésedet hordozza).

## A prompt tervezés alapjai

<img src="../../../translated_images/hu/five-patterns-overview.160f35045ffd2a94.webp" alt="Öt alap prompt tervezési minta áttekintése" width="800"/>

Mielőtt belevágunk ennek a modulnak a fejlett mintáiba, nézzük át az öt alapvető prompting technikát. Ezek az alapkövei minden prompt mérnök munkájának. Ha már dolgoztál a [Gyors kezdés modulban](../00-quick-start/README.md#2-prompt-patterns), láttad őket működés közben — itt a mögöttes koncepcionális keret.

### Zero-Shot promptolás

A legegyszerűbb megközelítés: adj a modellnek közvetlen utasítást példák nélkül. A modell teljesen a betanítására hagyatkozik, hogy megértse és végrehajtsa a feladatot. Ez jól működik egyértelmű kéréseknél, ahol a várt viselkedés nyilvánvaló.

<img src="../../../translated_images/hu/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot promptolás" width="800"/>

*Közvetlen utasítás példák nélkül — a modell kizárólag az utasításból következtet a feladatra*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Válasz: "Pozitív"
```

**Mikor használd:** Egyszerű osztályozások, közvetlen kérdések, fordítások vagy bármilyen feladat esetén, amit a modell további útmutatás nélkül képes kezelni.

### Few-Shot promptolás

Adj példákat, amelyek megmutatják a modellt követendő mintát. A modell ezekből megtanulja a várható bemenet-kimenet formátumot, és azt alkalmazza új bemeneteken is. Ez jelentősen javítja a konzisztenciát azoknál a feladatoknál, ahol a kívánt formátum vagy viselkedés nem nyilvánvaló.

<img src="../../../translated_images/hu/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot promptolás" width="800"/>

*Tanulás példákból — a modell azonosítja a mintát és alkalmazza új bemeneteken*

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

**Mikor használd:** Egyedi osztályozásoknál, következetes formázásnál, adott szakterületre jellemző feladatoknál vagy ha a zero-shot eredmények inkonzisztensek.

### Gondolatmenet láncolata

Kérd meg a modellt, hogy lépésről lépésre mutassa be gondolkodását. Ahelyett, hogy azonnal válaszolna, a modell részletesen lebontja a problémát és minden lépést külön-külön dolgoz ki. Ez növeli a pontosságot matematikai, logikai és több lépésből álló következtetési feladatoknál.

<img src="../../../translated_images/hu/chain-of-thought.5cff6630e2657e2a.webp" alt="Gondolatmenet láncolata promptolás" width="800"/>

*Lépésről lépésre gondolkodás — összetett problémák explicit logikai lépésekre bontva*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// A modell azt mutatja: 15 - 8 = 7, majd 7 + 12 = 19 alma
```

**Mikor használd:** Matematikai feladatoknál, logikai rejtvényeknél, hibakeresésnél vagy bárhol, ahol a gondolkodási folyamat megjelenítése növeli a pontosságot és a megbízhatóságot.

### Szerepalapú promptolás

Állíts be egy személyiséget vagy szerepet az AI számára a kérdés feltétele előtt. Ez kontextust ad, amely alakítja a válasz hangnemét, mélységét és fókuszát. Egy „szoftverarchitect” más tanácsokat ad, mint egy „junior fejlesztő” vagy egy „biztonsági auditor”.

<img src="../../../translated_images/hu/role-based-prompting.a806e1a73de6e3a4.webp" alt="Szerepalapú promptolás" width="800"/>

*Kontextus és személyiség beállítása — ugyanaz a kérdés más választ kap a hozzárendelt szereptől függően*

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

**Mikor használd:** Kódáttekintéseknél, tanításnál, adott terület elemzésénél vagy ha különböző szakértelmi szintnek vagy nézőpontnak megfelelő válaszokra van szükség.

### Prompt sablonok

Hozz létre újrahasználható promptokat változó helyőrzőkkel. Újabb prompt írása helyett egyszer definiálj egy sablont, és töltsd fel más-más értékekkel. A LangChain4j `PromptTemplate` osztálya megkönnyíti ezt `{{variable}}` szintaxissal.

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

**Mikor használd:** Ismétlődő lekérdezéseknél különböző bemenetekkel, kötegelt feldolgozásnál, újrahasználható AI munkafolyamatok építésénél vagy bármilyen esetben, amikor a prompt szerkezete ugyanaz marad, de az adatok változnak.

---

Ezek az öt alapvető minta szilárd eszköztárat adnak a legtöbb prompting feladathoz. Ennek a modulnak a többi része ezekre épít **nyolc fejlett mintával**, amelyek kihasználják a GPT-5.2 érvelésvezérlését, önértékelését és strukturált kimeneti képességeit.

## Fejlett minták

Miután lefedtük az alapokat, térjünk át a nyolc fejlett mintára, amelyek igazán egyedivé teszik ezt a modult. Nem minden problémához ugyanaz a megközelítés kell. Egyes kérdések gyors választ igényelnek, mások mély gondolkodást. Egyeseknél szükséges a látható érvelés, másoknál csak az eredmény. Az alábbi minták mindegyike egy-egy különböző helyzetre optimalizált — és a GPT-5.2 érvelésvezérlése még inkább kihangsúlyozza a különbségeket.

<img src="../../../translated_images/hu/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Nyolc prompt mérnöki minta" width="800"/>

*Áttekintés a nyolc prompt tervezési mintáról és azok használati eseteiről*

<img src="../../../translated_images/hu/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Érvelésvezérlés GPT-5.2-vel" width="800"/>

*A GPT-5.2 érvelésvezérlése lehetővé teszi, hogy megadjuk, mennyi gondolkodást végezzen a modell — a gyors, közvetlen válaszoktól a mély feltárásig*

**Alacsony lelkesedés (Gyors és fókuszált)** - Egyszerű kérdésekhez, ahol gyors, konkrét válaszokat szeretnél. A modell minimális gondolkodást végez - maximum 2 lépést. Használd számításokhoz, lekérdezésekhez vagy egyszerű kérdésekhez.

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

> 💡 **Fedezd fel a GitHub Copilottal:** Nyisd meg a [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) fájlt és kérdezd:
> - "Mi a különbség az alacsony lelkesedésű és a magas lelkesedésű prompting minták között?"
> - "Hogyan segítik az XML tagek a promptokon belül az AI válasz szerkezetének meghatározását?"
> - "Mikor érdemes az önreflektáló mintákat használni a közvetlen utasítás helyett?"

**Magas lelkesedés (Mély és alapos)** - Összetett problémákhoz, ahol átfogó elemzést szeretnél. A modell alaposan feltárja és részletes érvelést mutat. Használd rendszertervezéshez, architektúra döntésekhez vagy komplex kutatáshoz.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Feladatvégrehajtás (Lépésről lépésre haladás)** - Többlépcsős munkafolyamatokhoz. A modell előre ad egy tervet, narrálja a lépéseket munka közben, majd összefoglal. Használd migrációkhoz, implementációkhoz vagy bármilyen több lépéses folyamathoz.

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

A Gondolatmenet láncolata promptolás kifejezetten kéri, hogy a modell mutassa meg érvelési folyamatát, így javítva a pontosságot összetett feladatoknál. A lépésenkénti lebontás segíti mind az emberek, mind az AI logikájának megértését.

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chattel:** Kérdezz rá erre a mintára:
> - "Hogyan alkalmaznám a feladatvégrehajtási mintát hosszú futású műveletekhez?"
> - "Mik a legjobb gyakorlatok eszköz bevezetők szerkeztezésére éles alkalmazásokban?"
> - "Hogyan tudom rögzíteni és megjeleníteni az előrehaladás köztes frissítéseit a felületen?"

<img src="../../../translated_images/hu/task-execution-pattern.9da3967750ab5c1e.webp" alt="Feladatvégrehajtási minta" width="800"/>

*Terv → Végrehajtás → Összefoglalás munkafolyamat több lépéses feladatokhoz*

**Önreflektáló kód** - Gyártásminőségű kód generálásához. A modell gyártáskövetelményeknek megfelelő kódot generál, megfelelő hibakezeléssel. Használd új funkciók vagy szolgáltatások építésénél.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/hu/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Önreflexiós ciklus" width="800"/>

*Ismétlődő javítási ciklus — generálás, értékelés, problémák azonosítása, fejlesztés, ismétlés*

**Strukturált elemzés** - Következetes értékeléshez. A modell rögzített keretrendszer alapján elemzi a kódot (helyesség, gyakorlatok, teljesítmény, biztonság, karbantarthatóság). Használd kódáttekintésekhez vagy minőségértékeléshez.

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

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chattel:** Kérdezz rá a strukturált elemzésre:
> - "Hogyan szabhatom testre az elemzési keretrendszert különböző típusú kódáttekintésekhez?"
> - "Mi a legjobb módszer a strukturált kimenet programozott feldolgozására és reakcióra?"
> - "Hogyan biztosíthatom a konzisztens súlyossági szinteket különböző áttekintési ülések között?"

<img src="../../../translated_images/hu/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Strukturált elemzési minta" width="800"/>

*Konzisztens kódáttekintési keretrendszer súlyossági szintekkel*

**Többszörös fordulós csevegés** - Kontextust igénylő beszélgetésekhez. A modell emlékszik az előző üzenetekre és épít azokra. Használd interaktív segítségnyújtáshoz vagy összetett kérdésekhez.

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

*Hogyan halmozódik fel a beszélgetés kontextusa több forduló alatt a token limitekig*

**Lépésről lépésre gondolkodás** - Látható logikát igénylő problémákhoz. A modell explicit módon mutatja az érvelést minden lépésnél. Használd matematikai feladatokhoz, logikai rejtvényekhez vagy ahol meg kell érteni a gondolkodási folyamatot.

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

*Problémák expliciten logikai lépésekre bontva*

**Szűkített kimenet** - Meghatározott formátumú válaszokhoz. A modell szigorúan követi a formátum- és hosszúsági szabályokat. Használd összefoglalókhoz vagy amikor pontos kimeneti struktúrára van szükség.

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

<img src="../../../translated_images/hu/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Szűkített kimenet minta" width="800"/>

*Speciális formátum-, hossz- és struktúraszabályok betartatása*

## Meglévő Azure erőforrások használata

**Ellenőrizd a telepítést:**

Győződj meg róla, hogy a `.env` fájl megtalálható a gyökérkönyvtárban Azure hitelesítő adatokkal (az 01-es modul futtatásakor jött létre):
```bash
cat ../.env  # Meg kell jeleníteni az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**Indítsd el az alkalmazást:**

> **Megjegyzés:** Ha már elindítottad az összes alkalmazást az 01-es modulból a `./start-all.sh` parancs segítségével, akkor ez a modul már fut a 8083-as porton. Ebben az esetben kihagyhatod az alábbi indítási parancsokat, és azonnal megnyithatod a http://localhost:8083 címet.
**1. lehetőség: Spring Boot Dashboard használata (Ajánlott VS Code felhasználóknak)**

A fejlesztői konténer tartalmazza a Spring Boot Dashboard kiterjesztést, amely vizuális felületet biztosít az összes Spring Boot alkalmazás kezeléséhez. A VS Code bal oldalán, az Aktivitás sávban találod meg (keresd a Spring Boot ikont).

A Spring Boot Dashboard segítségével:
- Megtekintheted a munkaterületen elérhető összes Spring Boot alkalmazást
- Egyszerű kattintással indíthatod/leállíthatod az alkalmazásokat
- Valós időben nézheted az alkalmazás naplóit
- Követheted az alkalmazás állapotát

Egyszerűen kattints a "prompt-engineering" melletti lejátszás gombra a modul indításához, vagy indítsd el az összes modult egyszerre.

<img src="../../../translated_images/hu/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**2. lehetőség: Shell scriptek használata**

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

Vagy csak ezt a modult indítsd el:

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

Mindkét script automatikusan betölti a környezeti változókat a gyökér `.env` fájlból, és létrehozza a JAR fájlokat, ha azok még nem léteznek.

> **Megjegyzés:** Ha inkább manuálisan építenéd meg az összes modult a futtatás előtt:
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

*A fő irányítópult, amely az összes 8 prompt-fejlesztési mintát mutatja jellemzőikkel és felhasználási eseteikkel együtt*

## A minták felfedezése

A webes felület lehetővé teszi, hogy különböző prompt-stratégiákkal kísérletezz. Minden minta más-más problémát old meg – próbáld ki őket, hogy mikor melyik működik a legjobban.

> **Megjegyzés: Streaming vs Nem streaming** — Minden mintán két gomb található: **🔴 Stream Válasz (Élő)** és egy **Nem streaming** opció. A streaming Server-Sent Events (SSE) használatával valós időben jeleníti meg a modell által generált tokeneket, így azonnal látható a folyamat. A nem streaming opció megvárja a teljes választ az megjelenítés előtt. Mély gondolkodást igénylő promptoknál (pl. Nagy Lelkesedés, Önreflektív Kód) a nem streaming hívás nagyon hosszú ideig – akár percekig – is eltarthat látható előrehaladás nélkül. **Használd a streaminget komplex promptok esetén**, hogy lásd, ahogy a modell dolgozik, és elkerüld azt a benyomást, hogy az kérés időtúllépés miatt leállt.
>
> **Megjegyzés: Böngésző követelmény** — A streaming funkció a Fetch Streams API-t (`response.body.getReader()`) használja, amely teljes értékű böngészőt igényel (Chrome, Edge, Firefox, Safari). A VS Code beépített Simple Browser-ében NEM működik, mert annak webview-ja nem támogatja a ReadableStream API-t. Ha a Simple Browser-t használod, a nem streaming gombok továbbra is működnek – csak a streaming gombok érintettek. A teljes élményért nyisd meg a `http://localhost:8083` címet egy külső böngészőben.

### Alacsony vs Magas Lelkesedés

Tegyél fel egy egyszerű kérdést, például: "Mi a 15%-a 200-nak?" Alacsony Lelkesedés mellett azonnali, közvetlen választ kapsz. Most kérdezz valami bonyolultat, például: "Tervezzen egy gyorsítótárazási stratégiát egy nagy forgalmú API-hoz" Magas Lelkesedéssel. Kattints a **🔴 Stream Válasz (Élő)** gombra, és figyeld, ahogy a modell részletes érvelése tokenről tokenre megjelenik. Ugyanaz a modell, ugyanaz a kérdés – csak a prompt határozza meg, mennyire gondolkodjon mélyen.

### Feladatvégrehajtás (Eszköz előszók)

Többlépcsős munkafolyamatok előnyösek az előzetes tervezéstől és az előrehaladás narrálásától. A modell vázolja, mit fog csinálni, minden lépést elmesél, majd összefoglalja az eredményeket.

### Önreflektív Kód

Próbáld ki: "Hozzon létre egy e-mail validációs szolgáltatást". Ahelyett, hogy csak generál és megállna, a modell generál, értékel minőségi kritériumok alapján, azonosítja a gyengeségeket, majd javít. Láthatod, hogy ismételget, amíg a kód el nem éri a gyártási szintet.

### Strukturált Elemzés

A kódellenőrzésekhez konzisztens értékelési keretek szükségesek. A modell fix kategóriák szerint elemzi a kódot (helyesség, gyakorlatok, teljesítmény, biztonság) súlyossági szintekkel.

### Többlépcsős Chat

Kérdezd meg: "Mi az a Spring Boot?" majd azonnal kövesd a kérdést: "Mutass egy példát". A modell emlékszik az első kérdésre, és pont egy Spring Boot példát ad. Memória nélkül a második kérdés túl homályos lenne.

### Lépésenkénti Érvelés

Válassz egy matematikai problémát, és próbáld ki mind a Lépésenkénti Érveléssel, mind az Alacsony Lelkesedéssel. Az alacsony lelkesedés csak a választ adja meg – gyors, de átláthatatlan. A lépésenkénti érvelés megmutatja az összes számítást és döntést.

### Korlátozott Kimenet

Ha konkrét formátumra vagy szószámra van szükséged, ez a minta szigorúan betartatja. Próbálj meg 100 pontosan szóból álló összefoglalót generálni felsorolásos formátumban.

## Amit valójában tanulsz

**Az érvelési erőfeszítés mindent megváltoztat**

A GPT-5.2 lehetővé teszi, hogy a promptjaiddal szabályozd a számítási erőfeszítést. Az alacsony erőfeszítés gyors válaszokat jelent minimális feltárással. A magas erőfeszítés azt, hogy a modell időt szán mély gondolkodásra. Megtanulod az erőfeszítést a feladat komplexitásához igazítani – ne vesztegesd az időt egyszerű kérdésekre, de ne siess a bonyolult döntéseknél sem.

**A szerkezet irányítja a viselkedést**

Észrevetted az XML címkéket a promptokban? Nem dekorációk. A modellek sokkal megbízhatóbban követik a strukturált utasításokat, mint a szabad szöveget. Ha több lépéses folyamatokra vagy összetett logikára van szükség, a szerkezet segít a modellnek követni, hol tart és mi a következő lépés.

<img src="../../../translated_images/hu/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt szerkezet" width="800"/>

*Egy jól strukturált prompt anatómiája, tiszta szakaszokkal és XML-stílusú felépítéssel*

**Minőség önértékeléssel**

Az önreflektív minták a minőségi kritériumokat explicit módon teszik egyértelművé. Ahelyett, hogy remélnéd, hogy a modell "jól csinálja", pontosan megmondod neki, mit jelent a "jól": helyes logikát, hibakezelést, teljesítményt, biztonságot. Ezután a modell képes önmaga kibocsátását értékelni és javítani. Ez a kódgenerálást egy lottóból folyamatként alakítja át.

**A kontextus véges**

A többlépcsős beszélgetések úgy működnek, hogy minden kéréshez beillesztik az üzenet előzményeit. De van korlát - minden modellnek korlátos a token száma. Ahogy a beszélgetések növekednek, stratégiákra lesz szükséged a releváns kontextus megtartásához, anélkül, hogy túllépnéd a limitet. Ez a modul megmutatja, hogyan működik a memória; később megtanulod, mikor foglalj össze, mikor felejts, és mikor kérj vissza.

## Következő lépések

**Következő modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigáció:** [← Előző: 01-es modul - Bevezetés](../01-introduction/README.md) | [Vissza a főoldalra](../README.md) | [Következő: 03-as modul - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Felelősségkizárás**:  
Ez a dokumentum az [Co-op Translator](https://github.com/Azure/co-op-translator) mesterséges intelligencia fordító szolgáltatás segítségével készült. Bár igyekszünk a pontosságra, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum anyanyelvén tekintendő hiteles forrásnak. Fontos információk esetén professzionális emberi fordítást javasolunk. Nem vállalunk felelősséget az ebből a fordításból eredő félreértésekért vagy félreértelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->