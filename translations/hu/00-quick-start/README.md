# Modul 00: Gyors kezdés

## Tartalomjegyzék

- [Bevezetés](../../../00-quick-start)
- [Mi az a LangChain4j?](../../../00-quick-start)
- [LangChain4j függőségek](../../../00-quick-start)
- [Előfeltételek](../../../00-quick-start)
- [Beállítás](../../../00-quick-start)
  - [1. Szerezd be a GitHub tokened](../../../00-quick-start)
  - [2. Állítsd be a tokened](../../../00-quick-start)
- [Futtasd a példákat](../../../00-quick-start)
  - [1. Alap chat](../../../00-quick-start)
  - [2. Prompt minták](../../../00-quick-start)
  - [3. Függvényhívás](../../../00-quick-start)
  - [4. Dokumentum Q&A (Könnyű RAG)](../../../00-quick-start)
  - [5. Felelős mesterséges intelligencia](../../../00-quick-start)
- [Mit mutat minden példa](../../../00-quick-start)
- [Következő lépések](../../../00-quick-start)
- [Hibaelhárítás](../../../00-quick-start)

## Bevezetés

Ez a gyors kezdő útmutató arra szolgál, hogy minél gyorsabban elindulj a LangChain4j használatával. Lefedi az AI-alkalmazások építésének abszolút alapjait a LangChain4j és a GitHub Models segítségével. A következő modulokban az Azure OpenAI-t fogod használni a LangChain4j-vel, hogy fejlettebb alkalmazásokat készíthess.

## Mi az a LangChain4j?

A LangChain4j egy Java könyvtár, amely leegyszerűsíti az mesterséges intelligencia alapú alkalmazások építését. HTTP kliens és JSON feldolgozás helyett tiszta Java API-kkal dolgozol.

A "chain" a LangChain elnevezésben több komponens összefűzésére utal – például összeköthetsz egy promptot egy modellel és egy elemzővel, vagy láncolhatsz több AI-hívást, ahol az egyik kimenete a következő bemenete lesz. Ez a gyors kezdés az alapokra fókuszál, mielőtt bonyolultabb láncokat fedeznénk fel.

<img src="../../../translated_images/hu/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*A LangChain4j komponenseinek összekapcsolása – építőelemek egyesülnek, hogy erőteljes AI munkafolyamatokat hozzanak létre*

Három alapelemeket használunk:

**ChatModel** – Az AI modell interakciók interfésze. Hívd meg `model.chat("prompt")` metódust és kapj vissza egy válaszüzenetet. Az `OpenAiOfficialChatModel`-t használjuk, amely olyan OpenAI-kompatibilis végpontokkal működik, mint a GitHub Models.

**AiServices** – Típusbiztos AI szolgáltatás interfészeket hoz létre. Definiálj metódusokat, annotáld őket `@Tool` címkével, és a LangChain4j kezeli az összehangolást. Az AI automatikusan meghívja a Java metódusaidat, amikor szükséges.

**MessageWindowChatMemory** – Megőrzi a beszélgetés előzményeit. Nélküle minden kérés független. Ezzel az AI emlékszik a korábbi üzenetekre és a kontextust több fordulón át fenntartja.

<img src="../../../translated_images/hu/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j architektúra – az alapelemek együtt működnek, hogy a te AI alkalmazásaidat működtessék*

## LangChain4j függőségek

Ez a gyors kezdő három Maven függőséget használ a [`pom.xml`](../../../00-quick-start/pom.xml) fájlban:

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

A `langchain4j-open-ai-official` modul biztosítja az `OpenAiOfficialChatModel` osztályt, amely kapcsolódik OpenAI-kompatibilis API-khoz. A GitHub Models ugyanezt az API formátumot használja, így nincs szükség külön adapterre – csak állítsd be az alap URL-t `https://models.github.ai/inference` értékre.

A `langchain4j-easy-rag` modul automatikusan kezeli a dokumentum feldarabolást, beágyazást és visszakeresést, így RAG alkalmazásokat építhetsz minden lépés kézi konfigurálása nélkül.

## Előfeltételek

**Fejlesztői konténer használata esetén?** A Java és Maven már telepítve van. Csak egy GitHub személyes hozzáférési tokenre van szükséged.

**Helyi fejlesztéshez:**
- Java 21+, Maven 3.9+
- GitHub személyes hozzáférési token (lásd az alábbi utasításokat)

> **Megjegyzés:** Ez a modul a GitHub Models `gpt-4.1-nano` modelljét használja. Ne módosítsd a modell nevét a kódban – úgy van konfigurálva, hogy a GitHub elérhető modelljeivel működjön.

## Beállítás

### 1. Szerezd be a GitHub tokened

1. Lépj a [GitHub Beállítások → Személyes hozzáférési tokenek](https://github.com/settings/personal-access-tokens) oldalra
2. Kattints az "Új token generálása" gombra
3. Adj egy leíró nevet (pl. "LangChain4j Demo")
4. Állítsd be a lejáratot (ajánlott 7 nap)
5. Az "Fiók jogosultságok" alatt keresd meg a "Models" részt és válaszd a "Csak olvasható" opciót
6. Kattints a "Token generálása" gombra
7. Másold ki és mentsd el a tokened – újra nem fogod látni

### 2. Állítsd be a tokened

**1. opció: VS Code használatával (ajánlott)**

Ha VS Code-ot használsz, add meg a tokened a projekt gyökérkönyvtárában lévő `.env` fájlban:

Ha nincs `.env` fájl, másold át a `.env.example` fájlt `.env` néven, vagy hozz létre egy újat a projekt gyökerében.

**Példa `.env` fájlra:**
```bash
# A /workspaces/LangChain4j-for-Beginners/.env fájlban
GITHUB_TOKEN=your_token_here
```

Ezután egyszerűen kattints jobb gombbal bármelyik demó fájlra (pl. `BasicChatDemo.java`) a Fájlkezelőben, és válaszd a **"Run Java"** opciót, vagy használd a futtatási konfigurációkat a Futtatás és Hibakeresés panelen.

**2. opció: Terminal használata**

Állítsd be a tokened környezeti változóként:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Futtasd a példákat

**VS Code használatával:** Egyszerűen kattints jobb gombbal bármelyik demó fájlra a Fájlkezelőben és válaszd a **"Run Java"** opciót, vagy használd a Futtatás és Hibakeresés panelből az indítási konfigurációkat (előtte győződj meg róla, hogy hozzáadtad a tokened a `.env` fájlhoz).

**Maven használatával:** Alternatívaként futtathatod parancssorból is:

### 1. Alap chat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Prompt minták

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Bemutatja a nulla-shot, néhány-shot, lánc-tanulás és szerepkör-alapú promptingot.

### 3. Függvényhívás

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

Az AI automatikusan meghívja a Java metódusaidat, amikor szükséges.

### 4. Dokumentum Q&A (Könnyű RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Kérdezz a dokumentumaidról az Easy RAG segítségével, amely automatikusan beágyaz és keres tartalmakat.

### 5. Felelős mesterséges intelligencia

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Lásd, hogyan szűrik a mesterséges intelligencia biztonsági szűrők a káros tartalmakat.

## Mit mutat minden példa

**Alap chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Kezdd itt, hogy lásd a LangChain4j-t a legegyszerűbb formában. Létrehozol egy `OpenAiOfficialChatModel`-t, küldesz egy promptot `.chat()`-tel, és választ kapsz. Ez bemutatja az alapokat: hogyan inicializálj modelleket egyedi végpontokkal és API kulcsokkal. Ha ezt a mintát megérted, minden más erre épül.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Próbáld ki [GitHub Copilot](https://github.com/features/copilot) Chaten:** Nyisd meg a [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) fájlt és kérdezd meg:
> - "Hogyan válthatok GitHub Models-ről Azure OpenAI-ra ebben a kódban?"
> - "Milyen más paramétereket állíthatok be az OpenAiOfficialChatModel.builder()-ben?"
> - "Hogyan adhatok hozzá streaming válaszokat a teljes válasz várása helyett?"

**Prompt Tervezés** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Most, hogy tudod, hogyan kell beszélni egy modellel, nézzük meg, mit mondasz neki. Ez a demó ugyanazt a modellt használja, de öt különböző prompt mintát mutat be. Próbáld ki a nulla-shot promptokat közvetlen utasításokhoz, néhány-shot promptokat, amelyek példákból tanulnak, lánc-gondolkodás promptokat, amelyek felfedik az érvelési lépéseket, valamint szerepkör alapú promptokat, amelyek kontextust állítanak be. Láthatod, hogyan ad ugyanaz a modell drámai módon eltérő eredményeket attól függően, hogyan fogalmazod meg a kérésedet.

A demó bemutat prompt sablonokat is, amelyek hatékony módjai, hogy újrahasználható promptokat hozz létre változókkal.
Az alábbi példa egy promptot mutat a LangChain4j `PromptTemplate` használatával, amely kitölti a változókat. Az AI a megadott célállomás és tevékenység alapján válaszol.

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

> **🤖 Próbáld ki [GitHub Copilot](https://github.com/features/copilot) Chaten:** Nyisd meg a [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) fájlt és kérdezd meg:
> - "Mi a különbség a nulla-shot és a néhány-shot prompt között, és mikor melyiket használjam?"
> - "Hogyan befolyásolja a hőmérséklet paraméter a modell válaszait?"
> - "Milyen technikákkal lehet megelőzni a prompt injekciós támadásokat éles környezetben?"
> - "Hogyan hozhatok létre újrahasználható PromptTemplate objektumokat gyakori mintákhoz?"

**Eszköz Integráció** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Itt válik hatékonnyá a LangChain4j. Az `AiServices` segítségével létrehozol egy AI asszisztenst, amely képes meghívni a Java metódusaidat. Csak annotáld a metódusokat `@Tool("leírás")` címkével, és a LangChain4j kezeli a többit – az AI automatikusan eldönti, melyik eszközt használja a felhasználó kérése alapján. Ez bemutatja a függvényhívást, kulcsfontosságú technikát az olyan AI építéséhez, amely képes cselekedni, és nem csak válaszolni.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Próbáld ki [GitHub Copilot](https://github.com/features/copilot) Chaten:** Nyisd meg a [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) fájlt és kérdezd meg:
> - "Hogyan működik az @Tool annotáció és mit csinál a LangChain4j a háttérben vele?"
> - "Az AI képes egymás után több eszközt hívni összetett problémák megoldására?"
> - "Mi történik, ha egy eszköz kivételt dob – hogyan kezeljem a hibákat?"
> - "Hogyan integrálnék egy valós API-t ahelyett, hogy csak ezt a számológép példát használnám?"

**Dokumentum Q&A (Könnyű RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Itt a RAG-ot (visszakereséssel kiegészített generálás) látod a LangChain4j "Könnyű RAG" megközelítésével. A dokumentumokat betöltjük, automatikusan feldaraboljuk és beágyazzuk egy memóriában tárolt adatbázisba, majd egy tartalom visszakereső ad releváns szövegrészeket az AI számára a kérdezéskor. Az AI a dokumentumaid alapján válaszol, nem az általános tudása alapján.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 Próbáld ki [GitHub Copilot](https://github.com/features/copilot) Chaten:** Nyisd meg a [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) fájlt és kérdezd meg:
> - "Hogyan gátolja a RAG az AI kitalációkat a modell tanítási adataihoz képest?"
> - "Mi a különbség a könnyű megközelítés és egy testreszabott RAG pipeline között?"
> - "Hogyan méretezhetem ezt több dokumentum vagy nagyobb tudásbázis kezelésére?"

**Felelős mesterséges intelligencia** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Építs AI biztonságot rétegzett védelemmel. Ez a demó két védelmi réteget mutat be, amelyek együtt dolgoznak:

**1. rész: LangChain4j Input Guardrails** – Megakadályozza a veszélyes promptokat, mielőtt elérnék az LLM-et. Készíts egyéni őrvonalakat, amelyek tiltott kulcsszavakat vagy mintákat keresnek. Ezek a te kódodban futnak, így gyorsak és költségmentesek.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**2. rész: Szolgáltató biztonsági szűrők** – A GitHub Models beépített szűrőkkel rendelkezik, amelyek elkapják, amit az őrvonalak esetleg nem látnak. Lesznek kemény tiltások (HTTP 400 hibák) súlyos szabálysértések esetén és lágy visszautasítások, amikor az AI udvariasan megtagadja.

> **🤖 Próbáld ki [GitHub Copilot](https://github.com/features/copilot) Chaten:** Nyisd meg a [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) fájlt és kérdezd meg:
> - "Mi az az InputGuardrail és hogyan készíthetek sajátot?"
> - "Mi a különbség a kemény tiltás és a lágy visszautasítás között?"
> - "Miért használjuk egyszerre az őrvonalakat és a szolgáltató szűrőket?"

## Következő lépések

**Következő modul:** [01-bevezetés - Kezdés a LangChain4j-vel és a gpt-5-tel Azure-on](../01-introduction/README.md)

---

**Navigáció:** [← Vissza a főoldalra](../README.md) | [Következő: Modul 01 - Bevezetés →](../01-introduction/README.md)

---

## Hibaelhárítás

### Első Maven build

**Probléma**: Az első `mvn clean compile` vagy `mvn package` futtatás sokáig tart (10-15 perc)

**Ok**: A Maven az összes projektfüggőséget le kell töltse (Spring Boot, LangChain4j könyvtárak, Azure SDK-k stb.) az első build során.

**Megoldás**: Ez normális viselkedés. A későbbi build-ek sokkal gyorsabbak lesznek, mivel a függőségek helyileg gyorsítótárazva vannak. A letöltési idő a hálózati sebességedtől függ.

### PowerShell Maven parancs szintaxis

**Probléma**: Maven parancsok hibával meghiúsulnak: `Unknown lifecycle phase ".mainClass=..."`
**Ok**: A PowerShell az `=` jelet változó hozzárendelési operátorként értelmezi, ami megszakítja a Maven property szintaxisát

**Megoldás**: Használja a stop-parsing operátort `--%` a Maven parancs előtt:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

A `--%` operátor azt mondja a PowerShellnek, hogy az összes további argumentumot szó szerint adja át a Mavennek értelmezés nélkül.

### Windows PowerShell Emoji megjelenítés

**Probléma**: AI válaszok helyett szemét karakterek jelennek meg (pl. `????` vagy `â??`) az emojik helyett PowerShellben

**Ok**: A PowerShell alapértelmezett kódolása nem támogatja az UTF-8 emojikat

**Megoldás**: Futtassa ezt a parancsot Java alkalmazások indítása előtt:
```cmd
chcp 65001
```

Ez kényszeríti az UTF-8 kódolást a terminálban. Alternatívaként használja a Windows Terminalt, amely jobb Unicode támogatással rendelkezik.

### API hívások hibakeresése

**Probléma**: Hitelesítési hibák, sebességkorlátok vagy váratlan válaszok az AI modelltől

**Megoldás**: A példák tartalmazzák a `.logRequests(true)` és `.logResponses(true)` használatát, hogy az API hívások megjelenjenek a konzolon. Ez segít a hitelesítési hibák, sebességkorlátok vagy váratlan válaszok hibakeresésében. Ezeket a jelzőket vegye ki a termelési környezetben a naplózási zaj csökkentése érdekében.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Nyilatkozat**:
Ezt a dokumentumot az AI fordítási szolgáltatás, a [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével fordítottuk. Bár igyekszünk pontosak lenni, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum anyanyelvén tekintendő a hiteles forrásnak. Fontos információk esetén javasolt professzionális emberi fordítást igénybe venni. Nem vállalunk felelősséget a fordítás használatából eredő félreértésekért vagy félreértelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->