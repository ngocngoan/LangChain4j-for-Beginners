# Modul 02: Prompt Tervezés GPT-5.2-vel

## Tartalomjegyzék

- [Mit Fogsz Tanulni](../../../02-prompt-engineering)
- [Előfeltételek](../../../02-prompt-engineering)
- [A Prompt Tervezés Megértése](../../../02-prompt-engineering)
- [Hogyan Használja a LangChain4j-t](../../../02-prompt-engineering)
- [Az Alappillérek](../../../02-prompt-engineering)
- [Már Lévő Azure Erőforrások Használata](../../../02-prompt-engineering)
- [Alkalmazás Képernyőképek](../../../02-prompt-engineering)
- [A Minták Felfedezése](../../../02-prompt-engineering)
  - [Alacsony vs Magas Lelkesedés](../../../02-prompt-engineering)
  - [Feladat Végrehajtás (Eszköz Bevezetők)](../../../02-prompt-engineering)
  - [Önreflektáló Kód](../../../02-prompt-engineering)
  - [Strukturált Elemzés](../../../02-prompt-engineering)
  - [Többfordulós Csevegés](../../../02-prompt-engineering)
  - [Lépésről Lépésre Gondolkodás](../../../02-prompt-engineering)
  - [Korlátozott Kimenet](../../../02-prompt-engineering)
- [Amit Valójában Tanulsz](../../../02-prompt-engineering)
- [Következő Lépések](../../../02-prompt-engineering)

## Mit Fogsz Tanulni

Az előző modulban láttad, hogyan teszi lehetővé a memória a beszélgető AI-t, és használtad a GitHub Modelleket alapvető interakciókra. Most arra fókuszálunk, hogyan teszed fel a kérdéseket – maguk a promptok – az Azure OpenAI GPT-5.2 segítségével. Az, hogy hogyan strukturálod a promptokat, drasztikusan befolyásolja a válaszok minőségét.

A GPT-5.2-t használjuk, mert bevezeti az érvelés szabályozását – megmondhatod a modellnek, mennyi gondolkodást végezzen a válaszadást megelőzően. Ez megmutatja a különböző promptolási stratégiákat, és segít megérteni, mikor melyik megközelítést célszerű használni. Az Azure GPT-5.2 kevesebb korlátozással rendelkezik a GitHub Modellekhez képest, amiből szintén előnyt élvezünk.

## Előfeltételek

- Az 01-es modul befejezése (Azure OpenAI erőforrások telepítve)
- `.env` fájl a gyökérkönyvtárban az Azure hitelesítő adataival (amit az 01-es modulban az `azd up` hozott létre)

> **Megjegyzés:** Ha még nem fejezted be az 01-es modult, előbb kövesd ott a telepítési útmutatót.

## A Prompt Tervezés Megértése

A prompt tervezés arról szól, hogy olyan bemeneti szöveget alakíts ki, ami következetesen megadja a szükséges eredményeket. Nem csak kérdéseket feltenni – hanem úgy strukturálni a kéréseket, hogy a modell pontosan értse, mit akarsz és hogyan kell azt teljesítenie.

Gondolj rá úgy, mintha egy kollégának adnál utasítást. A „Javítsd meg a hibát” túl általános. A „Javítsd meg a null pointer exception-t a UserService.java 45. sorában null ellenőrzés hozzáadásával” konkrét. A nyelvi modellek is ugyanígy működnek – a pontosság és a struktúra számít.

## Hogyan Használja a LangChain4j-t

Ez a modul haladó promptolási mintákat mutat be ugyanazon LangChain4j alapokra építve, mint az előző modulok, a fókusz pedig a promptok szerkezetén és az érvelés szabályozásán van.

<img src="../../../translated_images/hu/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*Hogyan kapcsolja össze a LangChain4j a promptjaidat az Azure OpenAI GPT-5.2-vel*

**Függőségek** – A modul 02 a `pom.xml`-ben definiált alábbi langchain4j függőségeket használja:
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**OpenAiOfficialChatModel konfiguráció** – [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

A chat modell kézzel konfigurálva van Spring komponensként az OpenAI hivatalos kliensével, ami támogatja az Azure OpenAI végpontokat. A lényegi különbség az 01-es modulhoz képest nem a modell beállításában van, hanem abban, hogyan strukturáljuk a `chatModel.chat()`-nek küldött promptokat.

**Rendszer és Felhasználói Üzenetek** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

A LangChain4j üzenettípusokat különválaszt az áttekinthetőség kedvéért. A `SystemMessage` beállítja az AI viselkedését és kontextusát (például: „Te egy kódértékelő vagy”), míg a `UserMessage` tartalmazza a tényleges kérést. Ez a szétválasztás lehetővé teszi, hogy az AI viselkedése konzisztens maradjon különböző felhasználói kérdések esetén.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/hu/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*A SystemMessage állandó kontextust ad, míg a UserMessages tartalmazzák az egyedi kéréseket*

**MessageWindowChatMemory a Többfordulós Beszélgetéshez** – A többfordulós beszélgetési minta esetén újrahasználjuk a Modul 01-ből a `MessageWindowChatMemory`-t. Minden munkamenet saját memóriával rendelkezik a `Map<String, ChatMemory>` tárolóban, ami lehetővé teszi több párhuzamos beszélgetés futtatását anélkül, hogy a kontextus összekeveredne.

**Prompt Sablonok** – Itt az igazi fókusz a prompt tervezés, nem pedig az új LangChain4j API-k. Minden mintában (alacsony lelkesedés, magas lelkesedés, feladat végrehajtás stb.) ugyanazt a `chatModel.chat(prompt)` metódust használjuk, de gondosan strukturált prompt sztringekkel. Az XML tag-ek, utasítások és formázás mind a prompt szöveg részét képezik, nem a LangChain4j funkcióit.

**Érvelés Szabályozás** – A GPT-5.2 érvelési erőfeszítését prompt utasításokkal szabályozzuk, mint például „maximum 2 érvelési lépés” vagy „alapos feltárás”. Ezek prompt tervezési technikák, nem LangChain4j beállítások. A könyvtár egyszerűen átadja a promptokat a modellnek.

A fő tanulság: A LangChain4j biztosítja az infrastruktúrát (modellkapcsolat a [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), memória és üzenetkezelés a [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) segítségével), míg ez a modul megtanítja, hogyan készíts hatékony promptokat ezen az infrastruktúrán belül.

## Az Alappillérek

Nem minden problémához ugyanaz az út vezet. Egyes kérdések gyors válaszokat igényelnek, mások mély gondolkodást. Egyesek látható érvelést, mások csak az eredményt. Ez a modul nyolc promptolási mintát fed le – mindegyik különböző helyzetekre optimalizálva. Ki fogod próbálni mindegyiket, hogy megtudd, mikor melyik a leghatékonyabb.

<img src="../../../translated_images/hu/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*A nyolc prompt tervezési minta áttekintése és azok felhasználási esetei*

<img src="../../../translated_images/hu/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Alacsony lelkesedés (gyors, közvetlen) vs magas lelkesedés (alapos, feltáró) érvelési megközelítések*

**Alacsony Lelkesedés (Gyors & Fókuszált)** – Egyszerű kérdésekhez, ahol a gyors, közvetlen válaszokat akarod. A modell minimális érvelést végez – maximum 2 lépést. Használd számításokhoz, lekérdezésekhez vagy egyértelmű kérdésekhez.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Fedezd fel GitHub Copilottal:** Nyisd meg a [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) fájlt, és kérdezd meg:
> - „Mi a különbség az alacsony lelkesedésű és magas lelkesedésű promptolási minták között?”
> - „Hogyan segítik az XML tagek a promptok az AI válaszának strukturálását?”
> - „Mikor érdemes az önreflektáló mintákat használni a közvetlen utasítás helyett?”

**Magas Lelkesedés (Mély & Alapos)** – Komplex problémákhoz, ahol átfogó elemzést akarsz. A modell alaposan feltárja az összefüggéseket és részletes érvelést mutat. Használd rendszertervezéshez, architektúra döntésekhez vagy bonyolult kutatáshoz.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Feladat Végrehajtás (Lépésenkénti Folyamat)** – Több lépést tartalmazó munkafolyamatokhoz. A modell előre megtervezi, minden lépést elmagyaráz, majd összefoglal. Használd migrációkhoz, implementációkhoz vagy bármilyen több lépéses folyamathoz.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

A gondolatmenet láncolása (Chain-of-Thought) explicit módon kéri a modellt, hogy mutassa meg az érvelési folyamatát, ez javítja a pontosságot komplex feladatoknál. A lépésenkénti bontás segíti az emberi és AI logika megértését egyaránt.

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chat-tel:** Kérdezz rá erre a mintára:
> - „Hogyan adaptálnám a feladat végrehajtási mintát hosszú futamidejű műveletekhez?”
> - „Mik a legjobb gyakorlatok az eszköz bevezetők szerkezetének kialakításához éles alkalmazásokban?”
> - „Hogyan lehet a közbenső előrehaladási állapotokat rögzíteni és megjeleníteni egy felhasználói felületen?”

<img src="../../../translated_images/hu/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Tervezés → Végrehajtás → Összegzés több lépéses feladatok esetén*

**Önreflektáló Kód** – Termelési minőségű kód generálásához. A modell kódot generál, ellenőrzi minőségi szempontok szerint, majd iteratívan javítja. Új funkciók vagy szolgáltatások építéséhez ideális.

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

<img src="../../../translated_images/hu/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iteratív javítási ciklus - generálás, értékelés, hibák azonosítása, javítás, ismétlés*

**Strukturált Elemzés** – Következetes értékeléshez. A modell fix keretrendszerrel vizsgálja a kódot (helyesség, bevált gyakorlatok, teljesítmény, biztonság). Kódáttekintésekhez vagy minőségértékeléshez használható.

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

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chat-tel:** Kérdezz rá a strukturált elemzésre:
> - „Hogyan lehet testre szabni az elemzési keretrendszert különböző kódáttekintési típusokhoz?”
> - „Mi a legjobb módja a strukturált kimenet programozott feldolgozásának és használatának?”
> - „Hogyan biztosítható a konzisztens súlyossági szintek alkalmazása különböző áttekintési munkamenetek során?”

<img src="../../../translated_images/hu/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Négy kategóriás keretrendszer konzisztens kódáttekintésekhez súlyossági szintekkel*

**Többfordulós Csevegés** – Kontexuszt igénylő beszélgetésekhez. A modell emlékszik az előző üzenetekre és tovább épít rájuk. Interaktív segítségnyújtáshoz vagy komplex kérdés-válasz szituációkhoz ideális.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/hu/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Hogyan halmozódik a beszélgetési kontextus több fordulón keresztül, amíg eléri a token limitet*

**Lépésről Lépésre Gondolkodás** – Látható logikát igénylő problémákhoz. A modell minden lépést expliciten érvelve mutat be. Matek feladatokhoz, logikai rejtvényekhez vagy amikor meg akarod érteni a gondolkodási folyamatot.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/hu/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Problémák explicit logikai lépésekre bontása*

**Korlátozott Kimenet** – Speciális formátumú válaszokhoz. A modell szigorúan követi a formátum és hosszúság szabályokat. Összefoglalókhoz vagy pontos output struktúrát igénylő helyzetekhez.

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

<img src="../../../translated_images/hu/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Specifikus formátum, hosszúság és szerkezet követelményeinek érvényesítése*

## Már Lévő Azure Erőforrások Használata

**Ellenőrizd a telepítést:**

Győződj meg róla, hogy a gyökérkönyvtárban létezik `.env` fájl az Azure hitelesítő adatokat tartalmazva (az 01-es modul futtatásakor jött létre):
```bash
cat ../.env  # Meg kell jeleníteni az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**Indítsd el az alkalmazást:**

> **Megjegyzés:** Ha már elindítottad az összes alkalmazást az `./start-all.sh` használatával az 01-es modulból, akkor ez a modul már fut a 8083-as porton. A lentebb lévő indító parancsokat kihagyhatod, és közvetlenül a http://localhost:8083 oldalt használhatod.

**1. Lehetőség: Spring Boot Dashboard használata (VS Code felhasználóknak ajánlott)**

A fejlesztői konténer tartalmazza a Spring Boot Dashboard bővítményt, ami vizuális felületet ad minden Spring Boot alkalmazás kezeléséhez. Megtalálod a tevékenységi menü bal oldalán a Spring Boot ikon mellett.

A Spring Boot Dashboardból tudod:
- Megtekinteni az összes elérhető Spring Boot alkalmazást a munkaterületen
- Egy kattintással indítani/leállítani alkalmazásokat
- Valós idejű naplókat megtekinteni
- Monitorozni az alkalmazás állapotát

Egyszerűen kattints a lejátszás gombra a „prompt-engineering” modul mellett, vagy indítsd egyszerre az összes modult.

<img src="../../../translated_images/hu/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**2. Lehetőség: Shell script-ek használata**

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

Mindkét script automatikusan betölti a környezeti változókat a gyökér `.env` fájlból és buildeli a JAR-okat, ha még nem léteznek.

> **Megjegyzés:** Ha inkább manuálisan buildelnéd az összes modult indítás előtt:
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

Nyisd meg a http://localhost:8083 oldalt a böngésződben.

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

<img src="../../../translated_images/hu/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Fő dashboard, amely a nyolc prompt tervezési mintát mutatja be jellemzőikkel és használati eseteikkel*

## A Minták Felfedezése

A web felület lehetővé teszi, hogy különböző promptolási stratégiákat próbálj ki. Minden minta más problémákat old meg – próbáld ki őket, hogy meglásd, mikor melyik működik a legjobban.

### Alacsony vs Magas Lelkesedés

Tegyél fel egyszerű kérdést, például „Mi 15% 200-ból?” Alacsony Lelkesedéssel. Azonnali, közvetlen választ kapsz. Most kérdezz valami bonyolultabbat, például „Tervezzen egy cache-elési stratégiát egy nagy forgalmú API-hoz” Magas Lelkesedéssel. Figyeld, ahogy a modell lassabban dolgozik, és részletes érvelést ad. Ugyanaz a modell, ugyanaz a kérdés szerkezete – de a prompt megmondja, mennyi gondolkodást végezzen.
<img src="../../../translated_images/hu/low-eagerness-demo.898894591fb23aa0.webp" alt="Alacsony lelkesedés demó" width="800"/>

*Gyors számítás minimális érveléssel*

<img src="../../../translated_images/hu/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Magas lelkesedés demó" width="800"/>

*Átfogó gyorsítótárazási stratégia (2.8MB)*

### Feladat-végrehajtás (Eszköz bevezetők)

A több lépéses munkafolyamatok előnyösek az előzetes tervezés és a folyamatbeszámolók során. A modell felvázolja, mit fog csinálni, elmeséli az egyes lépéseket, majd összefoglalja az eredményeket.

<img src="../../../translated_images/hu/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Feladat-végrehajtás demó" width="800"/>

*REST végpont létrehozása lépésről lépésre narrálva (3.9MB)*

### Önreflektív kód

Próbáld ki a "Készíts egy email érvényesítő szolgáltatást" kérdést. Ahelyett, hogy csak kódot generálna és megállna, a modell generál, értékel minőségi kritériumok alapján, felismeri a gyengeségeket, majd javít. Láthatod, ahogy iterál, amíg a kód megfelel a gyártási szabványoknak.

<img src="../../../translated_images/hu/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Önreflektív kód demó" width="800"/>

*Teljes email érvényesítő szolgáltatás (5.2MB)*

### Strukturált elemzés

A kódértékelések állandó értékelési kereteket igényelnek. A modell előre meghatározott kategóriák szerint elemzi a kódot (helyesség, gyakorlatok, teljesítmény, biztonság) súlyossági szintekkel.

<img src="../../../translated_images/hu/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Strukturált elemzés demó" width="800"/>

*Keretrendszer-alapú kódértékelés*

### Több fordulós csevegés

Kérdezd meg: "Mi az a Spring Boot?" majd azonnal kövesd a kérdést: "Mutass egy példát!" A modell emlékszik az első kérdésedre, és kifejezetten egy Spring Boot példát ad. Memória nélkül a második kérdés túl általános lenne.

<img src="../../../translated_images/hu/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Több fordulós csevegés demó" width="800"/>

*Kontekstus megőrzése kérdések között*

### Lépésről lépésre érvelés

Válassz egy matekfeladatot, és próbáld meg mind a Lépésről lépésre Érvelést, mind az Alacsony Lelkesedést. Az alacsony lelkesedés gyorsan válaszol csak az eredményt adva – gyors, de átláthatatlan. A lépésenkénti módszer minden számítást és döntést megmutat.

<img src="../../../translated_images/hu/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Lépésről lépésre érvelés demó" width="800"/>

*Matematikai feladat explicit lépésekkel*

### Korlátozott kimenet

Ha specifikus formátumokra vagy szószámra van szükség, ez a minta szigorú betartást követel meg. Próbálj meg egy pontosan 100 szavas összefoglalót generálni pontokba szedve.

<img src="../../../translated_images/hu/constrained-output-demo.567cc45b75da1633.webp" alt="Korlátozott kimenet demó" width="800"/>

*Gépi tanulás összefoglaló formátum-ellenőrzéssel*

## Amit Valóban Tanulsz

**Az érvelési erőfeszítés mindent megváltoztat**

A GPT-5.2 engedi, hogy irányítsd a számítási erőfeszítést a promptokon keresztül. Az alacsony erőfeszítés gyors válaszokat eredményez minimális feltárással. A magas erőfeszítés mély gondolkodást jelent. Megtanulod az erőfeszítést a feladat bonyolultságához igazítani – ne pazarolj időt egyszerű kérdésekre, de ne siess a bonyolult döntésekkel se.

**A szerkezet irányítja a viselkedést**

Észreveszed az XML tageket a promptokban? Nem csak díszítések. A modellek a strukturált utasításokat sokkal megbízhatóbban követik, mint a szabad szöveget. Ha több lépéses folyamatokra vagy összetett logikára van szükséged, a szerkezet segít, hogy a modell tudja, hol tart és mi jön még.

<img src="../../../translated_images/hu/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt szerkezet" width="800"/>

*Egy jól strukturált prompt anatómiája, tiszta szakaszokkal és XML-stílusú szervezéssel*

**Minőség önértékeléssel**

Az önreflektív minták úgy működnek, hogy explicit módon megadják a minőségi kritériumokat. Ahelyett, hogy azt remélnéd, a modell "jól csinálja", pontosan megmondod neki, mit jelent a "jó": helyes logika, hibakezelés, teljesítmény, biztonság. A modell így ki tudja értékelni a saját outputját és javítani. Ez a kódgenerálást a lottóból folyamattá alakítja.

**A kontextus véges**

A több fordulós beszélgetések úgy működnek, hogy minden kéréshez tartalmazzák az előzményeket. De van határ – minden modellnek van maximális token száma. Ahogy nő a beszélgetés, stratégiákra lesz szükséged, hogy megőrizd a releváns kontextust anélkül, hogy elérnéd ezt a plafont. Ez a modul megmutatja, hogyan működik a memória; később megtanulod, mikor kell összefoglalni, mikor felejteni és mikor előhívni.

## Következő lépések

**Következő modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigáció:** [← Előző: Modul 01 - Bevezetés](../01-introduction/README.md) | [Vissza a főoldalra](../README.md) | [Következő: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Nyilatkozat**:
Ezt a dokumentumot az AI fordító szolgáltatás [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével fordítottuk. Bár a pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum a saját nyelvén tekintendő hivatalos forrásnak. Fontos információk esetén szakmai emberi fordítást javasolunk. Nem vállalunk felelősséget az ebből eredő félreértésekért vagy félreértelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->