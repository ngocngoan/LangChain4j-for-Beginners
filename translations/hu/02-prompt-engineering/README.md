# Modul 02: Prompt Műszaki Tervezés a GPT-5.2-vel

## Tartalomjegyzék

- [Videós Bemutató](../../../02-prompt-engineering)
- [Amit Meg Fogsz Tanulni](../../../02-prompt-engineering)
- [Előfeltételek](../../../02-prompt-engineering)
- [A Prompt Műszaki Tervezés Megértése](../../../02-prompt-engineering)
- [A Prompt Műszaki Tervezés Alapjai](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Gondolatmenet Láncolata](../../../02-prompt-engineering)
  - [Szerepalapú Prompting](../../../02-prompt-engineering)
  - [Prompt Sablonok](../../../02-prompt-engineering)
- [Fejlett Minták](../../../02-prompt-engineering)
- [Az Alkalmazás Futtatása](../../../02-prompt-engineering)
- [Alkalmazás Képernyőképek](../../../02-prompt-engineering)
- [A Minták Felfedezése](../../../02-prompt-engineering)
  - [Alacsony vs Magas Lelkesedés](../../../02-prompt-engineering)
  - [Feladatvégrehajtás (Eszköz Előszövegek)](../../../02-prompt-engineering)
  - [Önreflektáló Kód](../../../02-prompt-engineering)
  - [Strukturált Elemzés](../../../02-prompt-engineering)
  - [Többkörös Csevegés](../../../02-prompt-engineering)
  - [Lépésről Lépésre Logika](../../../02-prompt-engineering)
  - [Korlátozott Kimenet](../../../02-prompt-engineering)
- [Amit Valójában Meg Fogsz Tanulni](../../../02-prompt-engineering)
- [Következő Lépések](../../../02-prompt-engineering)

## Videós Bemutató

Nézd meg ezt az élő bemutatót, amely megmutatja, hogyan kezdhetsz neki ennek a modulnak:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Amit Meg Fogsz Tanulni

A következő ábra áttekintést ad a kulcsfontosságú témákról és készségekről, amelyeket ebben a modulban fejleszteni fogsz — a prompt finomítási technikáktól a lépésről lépésre követett munkafolyamatig.

<img src="../../../translated_images/hu/what-youll-learn.c68269ac048503b2.webp" alt="Amit Meg Fogsz Tanulni" width="800"/>

Az előző modulokban megismerted az alapvető LangChain4j interakciókat a GitHub modellekkel, és láthattad, hogyan teszi lehetővé a memória a beszélgető AI működését Azure OpenAI-val. Most arra koncentrálunk, hogyan teszel fel kérdéseket — vagyis magukra a promptokra — Azure OpenAI GPT-5.2 használatával. Az, hogy hogyan strukturálod a promptjaidat, drámaian befolyásolja a kapott válaszok minőségét. Kezdjük az alapvető prompting technikák áttekintésével, majd továbblépünk nyolc fejlett mintára, amelyek teljes mértékben kihasználják a GPT-5.2 képességeit.

A GPT-5.2-t azért használjuk, mert bevezeti az érvelés szabályozását — megmondhatod a modellnek, mennyi gondolkodást végezzen mielőtt válaszol. Ez világosabbá teszi a különböző prompting stratégiákat, és segít megérteni, mikor melyik megközelítést használd. Továbbá, az Azure kevesebb korlátozást tesz a GPT-5.2-re a GitHub modellekhez képest.

## Előfeltételek

- Az 01-es modul elvégzése (Azure OpenAI erőforrások telepítve)
- `.env` fájl a gyökérkönyvtárban Azure hitelesítő adatokkal (az `azd up` parancs által létrehozva az 01-es modulban)

> **Megjegyzés:** Ha még nem végezted el az 01-es modult, először kövesd ott a telepítési útmutatót.

## A Prompt Műszaki Tervezés Megértése

Lényegében a prompt műszaki tervezés a homályos és a pontos utasítások közti különbség, amint ezt az alábbi összehasonlítás is illusztrálja.

<img src="../../../translated_images/hu/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Mi a Prompt Műszaki Tervezés?" width="800"/>

A prompt műszaki tervezés az olyan bemeneti szöveg megtervezéséről szól, amely következetesen eléri a kívánt eredményeket. Nemcsak kérdések feltevéséről van szó - arról is, hogy úgy struktúrálod a kéréseket, hogy a modell pontosan értse, mit akarsz és hogyan kell teljesíteni.

Gondolj rá úgy, mint egy kollégának adott utasításra. „Javítsd meg a hibát” homályos. „Javítsd meg a null pointer kivételt a UserService.java 45. sorában null ellenőrzéssel” specifikus. A nyelvi modellek ugyanígy működnek — a specifikusság és a struktúra számít.

Az alábbi ábra mutatja, hogyan illeszkedik ide a LangChain4j — összekapcsolva a prompt mintáidat a modellel a SystemMessage és UserMessage építőelemek segítségével.

<img src="../../../translated_images/hu/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Hogyan Illeszkedik a LangChain4j" width="800"/>

A LangChain4j biztosítja az infrastruktúrát — modellkapcsolatok, memória és üzenettípusok — miközben a prompt minták egyszerűen gondosan strukturált szövegek, amelyeket ezen infrastruktúrán keresztül küldesz. A kulcsépítőelemek a `SystemMessage` (amely beállítja az MI viselkedését és szerepét) és a `UserMessage` (amely hordozza a tényleges kérésedet).

## A Prompt Műszaki Tervezés Alapjai

Az alább bemutatott öt alapvető technika alkotja a hatékony prompt műszaki tervezés alapját. Mindegyik más aspektusát célozza meg annak, hogyan kommunikálsz a nyelvi modellekkel.

<img src="../../../translated_images/hu/five-patterns-overview.160f35045ffd2a94.webp" alt="Öt Prompt Műszaki Tervezési Minta Áttekintése" width="800"/>

Mielőtt belevágnánk a modul fejlett mintáiba, tekintsük át az öt alapvető prompting technikát. Ezek azok az építőelemek, amelyeket minden prompt mérnöknek ismernie kell. Ha már dolgoztál a [Gyors kezdő modulon](../00-quick-start/README.md#2-prompt-patterns), láthattad őket működés közben — itt a mögöttes fogalmi keret.

### Zero-Shot Prompting

A legegyszerűbb megközelítés: közvetlen utasítást adsz a modellnek példa nélkül. A modell teljes egészében a tréningjére hagyatkozik a feladat megértésében és végrehajtásában. Ez jól működik egyértelmű kéréseknél, ahol a várt viselkedés nyilvánvaló.

<img src="../../../translated_images/hu/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Közvetlen utasítás példák nélkül — a modell csak az utasításból következteti a feladatot*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Válasz: "Pozitív"
```
  
**Mikor használd:** Egyszerű osztályozásoknál, közvetlen kérdéseknél, fordításnál vagy bármilyen feladatnál, amelyet a modell további útmutatás nélkül képes kezelni.

### Few-Shot Prompting

Adj példákat, amelyek megmutatják a modellt követendő mintát. A modell megtanulja az elvárt input-output formátumot a példáidból, majd alkalmazza azt új bemenetekre. Ez jelentősen javítja a konzisztenciát ott, ahol a kívánt formátum vagy viselkedés nem nyilvánvaló.

<img src="../../../translated_images/hu/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Példákból tanul — a modell felismeri a mintát és új bemenetekre alkalmazza*

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
  
**Mikor használd:** Egyedi osztályozásokhoz, egységes formázáshoz, speciális szakterületi feladatokhoz, vagy ha a zero-shot eredmények következetlenek.

### Gondolatmenet Láncolata

Kérd meg a modellt, hogy lépésről lépésre mutassa be az érvelését. Ahelyett, hogy azonnal válaszolna, a modell bontsa le a problémát és részletezze a részeket. Ez javítja a pontosságot matekban, logikában és többlépéses érvelésben.

<img src="../../../translated_images/hu/chain-of-thought.5cff6630e2657e2a.webp" alt="Gondolatmenet Láncolata Prompting" width="800"/>

*Lépésről lépésre való érvelés — bonyolult problémák explicit logikai lépésekre bontása*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// A modell így mutatja: 15 - 8 = 7, majd 7 + 12 = 19 alma
```
  
**Mikor használd:** Matekfeladatoknál, logikai rejtvényeknél, hibakeresésnél vagy bármilyen feladatnál, ahol az érvelési folyamat bemutatása javítja a pontosságot és a bizalmat.

### Szerepalapú Prompting

Állíts be egy személyiséget vagy szerepet az MI-nek a kérdésed előtt. Ez kontextust ad, amely alakítja a válasz hangvételét, mélységét és fókuszát. Egy „szoftverarchitekt” más tanácsot ad, mint egy „junior fejlesztő” vagy egy „biztonsági auditor”.

<img src="../../../translated_images/hu/role-based-prompting.a806e1a73de6e3a4.webp" alt="Szerepalapú Prompting" width="800"/>

*Kontextus és személyiség beállítása — ugyanaz a kérdés más választ kap a kijelölt szereptől függően*

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
  
**Mikor használd:** Kódáttekintéseknél, oktatásban, speciális szakterületi elemzéseknél vagy amikor a válaszokat egy adott szakértelmi szint vagy szemlélet szerint kell igazítani.

### Prompt Sablonok

Hozz létre újrahasználható promptokat változó helyőrzőkkel. Ahelyett, hogy mindig új promptot írjál, egyszer definiálj egy sablont, majd töltsd ki különböző értékekkel. A LangChain4j `PromptTemplate` osztálya ezt megkönnyíti a `{{variable}}` szintaxissal.

<img src="../../../translated_images/hu/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Sablonok" width="800"/>

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
  
**Mikor használd:** Ismétlődő lekérdezéseknél, kötegelt feldolgozásnál, újrahasználható AI munkafolyamatok építésénél, vagy bármilyen helyzetben, ahol a prompt szerkezete ugyanaz marad, de az adatok változnak.

---

Ezek az öt alapvető technika szilárd eszköztárat adnak a legtöbb prompting feladathoz. A modul további részében **nyolc fejlett mintát** ismerhetsz meg, amelyek kihasználják a GPT-5.2 érvelés szabályozását, önértékelését és strukturált kimeneti képességeit.

## Fejlett Minták

Miután áttekintettük az alapokat, lépjünk tovább a nyolc fejlett mintára, amelyek egyedivé teszik ezt a modult. Nem minden problémához ugyanaz a megközelítés kell. Egyes kérdések gyors válaszokat igényelnek, mások mély gondolkodást. Egyesek látható érvelést, mások csak eredményeket. Az alábbi minták mindegyike egy adott szituációra optimalizált — és a GPT-5.2 érvelés szabályozása még nyilvánvalóbbá teszi a különbségeket.

<img src="../../../translated_images/hu/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Nyolc Prompt Minta" width="800"/>

*A nyolc prompt műszaki tervezési minták áttekintése és felhasználási eseteik*

A GPT-5.2 egy további dimenziót ad ezekhez a mintákhoz: *érvelés szabályozás*. Az alábbi csúszka mutatja, hogyan állíthatod be a modell gondolkodási erőfeszítését — a gyors, közvetlen válaszoktól a mély, alapos elemzésig.

<img src="../../../translated_images/hu/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Érvelés Szabályozás a GPT-5.2-vel" width="800"/>

*A GPT-5.2 érvelés szabályozásával megadhatod, mennyi gondolkodást végezzen a modell — gyors közvetlen válaszoktól a mély feltárásig*

**Alacsony Lelkesedés (Gyors és Fókuszált)** - Egyszerű kérdésekhez, ahol gyors, közvetlen válaszokat szeretnél. A modell minimális érvelést végez - maximum 2 lépést. Használd számításokhoz, lekérdezésekhez vagy egyértelmű kérdésekhez.

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
  
> 💡 **Fedezd fel GitHub Copilot-tal:** Nyisd meg a [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) fájlt, és kérdezd meg:
> - „Mi a különbség az alacsony és magas lelkesedésű prompting minták között?”
> - „Hogyan segítenek az XML tagek a promptok AI válaszának strukturálásában?”
> - „Mikor használjam az önreflektáló mintákat a közvetlen utasítással szemben?”

**Magas Lelkesedés (Mély és Alapos)** - Összetett problémákhoz, ahol átfogó elemzésre van szükség. A modell alaposan feltár, részletes érvelést mutat. Használd rendszertervezéshez, architektúra döntésekhez vagy komplex kutatáshoz.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**Feladatvégrehajtás (Lépésről lépésre haladás)** - Többlépéses munkafolyamatokhoz. A modell előre megtervezi a lépéseket, elmondja, mit csinál, majd összefoglal. Használd migrációkhoz, implementációkhoz vagy bármilyen többlépéses feladathoz.

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
  
A Chain-of-Thought prompting kifejezetten kéri a modellt, hogy mutassa meg az érvelési folyamatát, javítva a pontosságot összetett feladatoknál. A lépésenkénti bontás segíti az embert és az MI-t is az logika megértésében.

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chat-ben:** Kérdezz erről a mintáról:
> - „Hogyan adaptálnám a feladatvégrehajtás mintát hosszú futású műveletekhez?”
> - „Mik a legjobb gyakorlatok az eszköz előszövegek strukturálásához éles alkalmazásokban?”
> - „Hogyan tudok megjeleníteni köztes előrehaladási frissítéseket egy felhasználói felületen?”

Az alábbi ábra szemlélteti ezt a Tervezés → Végrehajtás → Összefoglalás munkafolyamatot.

<img src="../../../translated_images/hu/task-execution-pattern.9da3967750ab5c1e.webp" alt="Feladatvégrehajtási Minta" width="800"/>

*Tervezés → Végrehajtás → Összefoglalás munkafolyamat többlépéses feladatokhoz*

**Önreflektáló Kód** - Termelési minőségű kódgeneráláshoz. A modell a gyártási szabványoknak megfelelő, hibakezeléssel ellátott kódot hoz létre. Használd új funkciók vagy szolgáltatások építésére.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```
  
Az alábbi ábra mutatja ezt az iteratív fejlesztési ciklust — generálás, értékelés, gyengeségek felismerése, majd finomítás, amíg a kód megfelel a gyártási szabványoknak.

<img src="../../../translated_images/hu/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Önreflexiós Ciklus" width="800"/>

*Iteratív fejlesztési ciklus - generálás, értékelés, problémák felismerése, javítás, ismétlés*

**Strukturált Elemzés** - Következetes értékeléshez. A modell előre meghatározott keretrendszer szerint vizsgálja a kódot (helyesség, gyakorlatok, teljesítmény, biztonság, karbantarthatóság). Használd kódáttekintésekhez vagy minőségértékelésekhez.

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
  
> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chat-ben:** Kérdezz strukturált elemzésről:
> - „Hogyan szabhatom testre az elemzési keretrendszert különféle kódáttekintésekhez?”
> - „Mi a legjobb módja a strukturált kimenet feldolgozásának programozottan?”
> - „Hogyan biztosítom a következetes súlyossági szinteket különböző áttekintési üléseken?”

Az alábbi ábra mutatja, hogyan szervezi ez a strukturált keretrendszer a kódáttekintést következetes kategóriákba súlyossági szintekkel.

<img src="../../../translated_images/hu/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Strukturált Elemzési Minta" width="800"/>

*Következetes kódáttekintési keretrendszer súlyossági szintekkel*

**Többkörös Csevegés** - Olyan beszélgetésekhez, amelyeknek kontextusra van szükségük. A modell emlékszik az előző üzenetekre és épít rájuk. Használd interaktív segítségnyújtó ülésekhez vagy komplex kérdés-válasz helyzetekhez.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
Az alábbi ábra szemlélteti, hogyan halmozódik fel a beszélgetési kontextus minden körrel, és hogyan viszonyul ez a modell token limitjéhez.

<img src="../../../translated_images/hu/context-memory.dff30ad9fa78832a.webp" alt="Kontextus Memória" width="800"/>

*Hogyan halmozódik fel a beszélgetési kontextus többszöri körök alatt, amíg eléri a token korlátot*
**Lépésről-lépésre gondolkodás** – Látható logikát igénylő problémák esetén. A modell minden lépéshez expliciten bemutatja az okfejtést. Használd ezt matematikai problémákhoz, logikai rejtvényekhez, vagy amikor meg kell értened a gondolkodási folyamatot.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

Az alábbi ábra bemutatja, hogyan bontja a modell a problémákat explicit, számozott logikai lépésekre.

<img src="../../../translated_images/hu/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Lépésről-lépésre minta" width="800"/>

*Problémák explicit logikai lépésekre bontása*

**Korlátozott kimenet** – Specifikus formátumkövetelményekkel bíró válaszokhoz. A modell szigorúan követi a formátum- és hosszúságszabályokat. Használd összefoglalókhoz vagy precíz kimenetstruktúrát igénylő esetekben.

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

A következő ábra bemutatja, hogyan irányítják a korlátok a modellt, hogy szigorúan az előírt formátum- és hosszúságszabályokat kövesse.

<img src="../../../translated_images/hu/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Korlátozott kimenet minta" width="800"/>

*Specifikus formátum, hossz és struktúra követelményének érvényesítése*

## A alkalmazás futtatása

**Telepítés ellenőrzése:**

Győződj meg róla, hogy a `.env` fájl létezik a gyökérkönyvtárban az Azure hitelesítő adatokkal (amely a 01-es modulban készült). Futtasd ezt a modul könyvtárából (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # Meg kell jelenítenie az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Meg kell jelenítenie az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**Indítsd el az alkalmazást:**

> **Megjegyzés:** Ha már elindítottad az összes alkalmazást a gyökérkönyvtárból `./start-all.sh` segítségével (ahogy az 01-es modul leírja), ez a modul már fut a 8083-as porton. Ekkor kihagyhatod az alábbi indítási parancsokat, és közvetlenül a http://localhost:8083 oldalra léphetsz.

**1. lehetőség: Spring Boot Dashboard használata (Ajánlott VS Code felhasználóknak)**

A fejlesztői környezet tartalmazza a Spring Boot Dashboard bővítményt, ami vizuális felületet nyújt az összes Spring Boot alkalmazás kezelésére. A VS Code bal oldali tevékenységsávjában találod meg (a Spring Boot ikon alapján).

A Spring Boot Dashboardból:
- Megtekintheted az összes elérhető Spring Boot alkalmazást a munkaterületen
- Egy kattintással indíthatsz vagy állíthatsz le alkalmazásokat
- Valós időben nézheted az alkalmazás naplóit
- Figyelemmel kísérheted az alkalmazás állapotát

Egyszerűen kattints a lejátszás gombra a "prompt-engineering" modul mellett a modul indításához, vagy indíts el egyszerre minden modult.

<img src="../../../translated_images/hu/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*A Spring Boot Dashboard a VS Code-ban — indítsd, állítsd le és monitorozd az összes modult egy helyről*

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

Mindkét szkript automatikusan betölti a környezeti változókat a gyökér `.env` fájlból, és ha nem léteznek, legyártja a JAR fájlokat.

> **Megjegyzés:** Ha inkább manuálisan szeretnéd buildelni az összes modult indítás előtt:
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

Nyisd meg böngészőben a http://localhost:8083 címet.

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

Itt a prompt engineering modul fő felülete, ahol egyszerre próbálhatod ki mind a nyolc mintát.

<img src="../../../translated_images/hu/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*A fő dashboard, amely az összes 8 prompt mérnöki mintát mutatja jellemzőikkel és használati eseteikkel*

## Minták felfedezése

A webes felület lehetővé teszi a különböző promptolási stratégiák kipróbálását. Mindegyik minta más-más problémára ad választ – próbáld ki, hogy lásd, mikor működik jól melyik megközelítés.

> **Megjegyzés: Streaming és nem streaming** — Minden mintalapon két gombot találsz: **🔴 Stream Response (Live)** és egy **nem streaming** opciót. A streaming Server-Sent Events (SSE) használatával azonnal megjeleníti azokat a tokeneket, amelyeket a modell generál, így az előrehaladást közvetlenül láthatod. A nem streaming opció megvárja a teljes választ, mielőtt megjelenítené azt. Mély okfejtést igénylő promptoknál (például High Eagerness, Self-Reflecting Code) a nem streaming hívás nagyon hosszú ideig is eltarthat — akár percekig — látványos visszajelzés nélkül. **Használj streaminget, amikor összetett promptokkal kísérletezel**, így látod a modell működését és elkerülöd azt a benyomást, hogy a kérés időtúllépett.
> 
> **Megjegyzés: Böngészőkövetelmény** — A streaming funkció a Fetch Streams API-t (`response.body.getReader()`) használja, ami teljes böngészőt igényel (Chrome, Edge, Firefox, Safari). Ez **nem** működik a VS Code beépített Simple Browserében, mert annak webnézete nem támogatja a ReadableStream API-t. Ha Simple Brosert használsz, a nem streaming gombok továbbra is normálisan működnek — csak a streaming gombokat érinti ez a korlátozás. A teljes élményhez külső böngészőben nyisd meg a `http://localhost:8083` címet.

### Alacsony vs Magas lelkesedés

Tegyél fel egy egyszerű kérdést, például "Mi a 15%-a 200-nak?" Alacsony lelkesedéssel. Azonnali, közvetlen választ kapsz. Most kérdezz valami bonyolultabbat, például "Tervezzen meg egy caching stratégiát egy nagy forgalmú API-hoz" Magas lelkesedéssel. Kattints a **🔴 Stream Response (Live)** gombra, és nézd, miként jelenik meg részletes gondolkodás lépésről lépésre tokenenként. Ugyanaz a modell, ugyanaz a kérdés felépítés – de a prompt megmondja, mennyi gondolkodást végezzen.

### Feladatvégrehajtás (Eszköz bevezetések)

Többlépcsős munkafolyamatoknál előnyös az előzetes tervezés és a folyamat narrálása. A modell ismerteti, mit fog csinálni, lépésről lépésre mesél, majd összegzi az eredményeket.

### Önreflektáló kód

Próbáld ki a "Hozz létre egy email érvényesítő szolgáltatást" promptot. Ahelyett, hogy csak kódot generálna és megállna, a modell generál, értékel minőségi kritériumok alapján, azonosít gyengeségeket, majd javít. Láthatod, ahogy iterál, amíg a kód megfelel a termelési szabványoknak.

### Strukturált elemzés

Kódáttekintésekhez következetes értékelési keretek kellenek. A modell a kódot fix kategóriák szerint elemzi (helyesség, gyakorlatok, teljesítmény, biztonság), súlyossági szintekkel.

### Többkörös csevegés

Kérdezd meg: "Mi az a Spring Boot?" majd közvetlenül kövesd azzal: "Mutass példát." A modell emlékszik az első kérdésre, és egy Spring Boot példát ad. Memória nélkül a második kérdés túl homályos lenne.

### Lépésről-lépésre gondolkodás

Válassz egy matekfeladatot, és próbáld ki egyszerre a Lépésről-lépésre gondolkodást és az Alacsony lelkesedést. Az alacsony lelkesedés gyorsan csak a választ adja meg – gyors, de átláthatatlan. A lépésről lépésre bemutatja minden számítást és döntést.

### Korlátozott kimenet

Amikor meghatározott formátumra vagy szóközszámra van szükség, ez a minta szigorúan érvényesíti az előírásokat. Próbálj meg egy pontosan 100 szóból álló összefoglalót generálni felsorolásos formában.

## Amit valójában tanulsz

**Az okfejtés erőfeszítése mindent megváltoztat**

A GPT-5.2 lehetővé teszi, hogy a promptok által szabályozd a számítási erőfeszítést. Az alacsony erőfeszítés gyors válaszokat eredményez minimális feltárással. A magas erőfeszítés azt jelenti, hogy a modell mélyen és alaposan gondolkodik. Megtanulod az erőfeszítést a feladat komplexitásához igazítani – ne pazarold az időt egyszerű kérdésekre, de ne siesd el a bonyolult döntéseket sem.

**A struktúra irányítja a viselkedést**

Észrevetted az XML címkéket a promptokban? Nem dekoratívak. A modellek a strukturált utasításokat megbízhatóbban követik, mint az szabad szöveget. Többlépcsős folyamatokhoz vagy összetett logikához a struktúra segít a modellnek követni, hol tart és mi következik. Az alábbi ábra egy jól strukturált promptot bont le, bemutatva, hogyan rendezi az utasításokat XML-stílusú címkék, mint `<system>`, `<instructions>`, `<context>`, `<user-input>`, és `<constraints>`.

<img src="../../../translated_images/hu/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt szerkezete" width="800"/>

*Egy jól strukturált prompt anatómiája tiszta szakaszokkal és XML-stílusú szervezéssel*

**Minőség önértékeléssel**

Az önreflektáló minták úgy működnek, hogy a minőségi kritériumokat explicit módon megadják. Ahelyett, hogy remélnéd, hogy a modell "jól csinálja", pontosan megmondod neki, mit jelent a "helyes": logikai helyesség, hibakezelés, teljesítmény, biztonság. A modell így képes saját kimenetét értékelni és javítani. Ez előállításból folyamatot csinál.

**A kontextus véges**

A többkörös beszélgetések úgy működnek, hogy a lekérdezéshez mellékelik az üzenet előzményeit. Viszont van korlát – minden modellhez van maximális token-szám. Ahogy nő a beszélgetés, stratégiák kellenek, hogy a releváns kontextust megtartsd anélkül, hogy túllépnéd a plafont. Ez a modul megmutatja, hogyan működik a memória; később megtanulod, mikor foglalj össze, mikor felejts, és mikor hívj elő.

## Következő lépések

**Következő modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigáció:** [← Előző: 01-es modul - Bevezetés](../01-introduction/README.md) | [Vissza a főoldalra](../README.md) | [Következő: 03-as modul - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Jogi nyilatkozat**:
Ez a dokumentum az [Co-op Translator](https://github.com/Azure/co-op-translator) AI fordító szolgáltatásával készült. Bár az pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az eredeti nyelven tekintendő hiteles forrásnak. Fontos információk esetén szakmai emberi fordítást javasolunk. Nem vállalunk felelősséget a fordítás használatából eredő félreértésekért vagy helytelen értelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->