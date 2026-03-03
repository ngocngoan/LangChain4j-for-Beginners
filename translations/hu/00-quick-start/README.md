# Modul 00: Gyors áttekintés

## Tartalomjegyzék

- [Bevezető](../../../00-quick-start)
- [Mi az a LangChain4j?](../../../00-quick-start)
- [LangChain4j Függőségek](../../../00-quick-start)
- [Előfeltételek](../../../00-quick-start)
- [Beállítás](../../../00-quick-start)
  - [1. Szerezd meg a GitHub tokenedet](../../../00-quick-start)
  - [2. Állítsd be a tokenedet](../../../00-quick-start)
- [Futtasd a példákat](../../../00-quick-start)
  - [1. Alap chat](../../../00-quick-start)
  - [2. Prompt minták](../../../00-quick-start)
  - [3. Függvényhívás](../../../00-quick-start)
  - [4. Dokumentum kérdések és válaszok (Easy RAG)](../../../00-quick-start)
  - [5. Felelős MI](../../../00-quick-start)
- [Mit mutat be az egyes példa](../../../00-quick-start)
- [Következő lépések](../../../00-quick-start)
- [Hibaelhárítás](../../../00-quick-start)

## Bevezető

Ez a gyors indítás célja, hogy minél gyorsabban elindulj a LangChain4j használatával. Megismerteti az AI alkalmazások LangChain4j-vel és GitHub modellekkel történő alapvető építését. A következő modulokban Azure OpenAI és GPT-5.2-re váltasz, és mélyebben belemerülsz az egyes fogalmakba.

## Mi az a LangChain4j?

A LangChain4j egy Java könyvtár, amely leegyszerűsíti az MI-vezérelt alkalmazások készítését. HTTP kliensek és JSON feldolgozás helyett tiszta Java API-kkal dolgozol. 

A LangChain "lánc" arra utal, hogy több komponens láncolódik össze – például összekapcsolhatsz egy promptot egy modellel és egy elemzővel, vagy több MI hívás láncolódik, ahol az egyik kimenet a következő bemenete. Ez a gyorsstart az alapokra fókuszál, mielőtt bonyolultabb láncok felé lépne.

<img src="../../../translated_images/hu/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Komponensek láncolása LangChain4j-ben – építőkövek kapcsolódnak, hogy erős MI munkafolyamatokat hozzanak létre*

Három alapvető komponenst használunk:

**ChatModel** - Az MI modellel való interakciók felülete. Meghívod: `model.chat("prompt")` és kapsz válaszstringet. Az `OpenAiOfficialChatModel`-t használjuk, amely kompatibilis OpenAI végpontokkal, például GitHub modellekkel.

**AiServices** - Típusbiztos MI szolgáltatási interfészek létrehozása. Metódusokat definiálsz, `@Tool` annotációval látod el őket, és a LangChain4j kezeli az összehangolást. Az MI automatikusan hívja Java metódusaidat, ha szükséges.

**MessageWindowChatMemory** - A beszélgetés előzményeit tárolja. Enélkül minden kérés független. Ezzel az MI megjegyzi a korábbi üzeneteket és megőrzi a kontextust több fordulón át.

<img src="../../../translated_images/hu/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j architektúra – alapvető komponensek, amelyek együtt működnek az MI alkalmazásaid hajtásához*

## LangChain4j Függőségek

Ez a gyorsindítás három Maven függőséget használ a [`pom.xml`](../../../00-quick-start/pom.xml) fájlban:

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

A `langchain4j-open-ai-official` modul biztosítja az `OpenAiOfficialChatModel` osztályt, amely OpenAI-kompatibilis API-khoz kapcsolódik. A GitHub Modellek ugyanazt az API formátumot használják, ezért nincs szükség külön adapterre – csak állítsd be az alap URL-t `https://models.github.ai/inference`-re.

A `langchain4j-easy-rag` modul automatikus dokumentumfelosztást, beágyazást és visszakeresést biztosít, így RAG alkalmazásokat építhetsz anélkül, hogy manuálisan konfigurálnád az egyes lépéseket.

## Előfeltételek

**Fejlesztői konténer használata?** Java és Maven már telepítve van. Csak a GitHub Személyes Hozzáférési Tokent kell beszerezned.

**Helyi fejlesztés:**
- Java 21+, Maven 3.9+
- GitHub Személyes Hozzáférési Token (utasítások lent)

> **Megjegyzés:** Ez a modul a GitHub Modellek `gpt-4.1-nano` változatát használja. Ne módosítsd a modell nevét a kódban – ez a GitHub elérhető modelljeihez van konfigurálva.

## Beállítás

### 1. Szerezd meg a GitHub tokenedet

1. Nyisd meg a [GitHub Beállítások → Személyes hozzáférési tokenek](https://github.com/settings/personal-access-tokens) oldalt
2. Kattints a „Generate new token” gombra
3. Adj meg egy leíró nevet (pl. „LangChain4j Demo”)
4. Állíts be lejárati időt (7 nap javasolt)
5. Az „Account permissions” alatt keresd meg a „Models” opciót, és állítsd „Read-only”-ra
6. Kattints a „Generate token” gombra
7. Másold ki és mentsd el a tokenedet – később nem fogod látni újra

### 2. Állítsd be a tokenedet

**1. lehetőség: VS Code használata (ajánlott)**

Ha VS Code-ot használsz, add hozzá a tokened a projekt gyökérkönyvtárában lévő `.env` fájlhoz:

Ha a `.env` fájl nem létezik, másold a `.env.example` fájlt `.env` néven, vagy hozz létre egy új `.env` fájlt a projekt gyökerében.

**Példa `.env` fájl:**
```bash
# A /workspaces/LangChain4j-for-Beginners/.env fájlban
GITHUB_TOKEN=your_token_here
```

Ezután egyszerűen kattints jobb gombbal egy bemutató fájlra (pl. `BasicChatDemo.java`) a Felfedezőben és válaszd a **„Run Java”** opciót, vagy használd a „Run and Debug” panel indítási konfigurációit.

**2. lehetőség: Terminál használata**

Állítsd be a token környezeti változóként:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Futtasd a példákat

**VS Code használatával:** Egyszerűen kattints jobb gombbal egy bemutató fájlra a Felfedezőben, és válaszd a **„Run Java”** lehetőséget, vagy a „Run and Debug” panel indítási konfigurációit használd (előtte természetesen adj hozzá egy tokent a `.env` fájlhoz).

**Maven használatával:** Vagy a parancssorból futtathatod:

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

Mutatja a zero-shot, few-shot, chain-of-thought és szerepkör-alapú promptokat.

### 3. Függvényhívás

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

Az MI automatikusan hívja a Java metódusaidat, amikor szükséges.

### 4. Dokumentum kérdések és válaszok (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Kérdezz a dokumentumaidról az Easy RAG használatával, automatikus beágyazás és visszakeresés mellett.

### 5. Felelős MI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Nézd meg, hogyan blokkolják az MI biztonsági szűrők a káros tartalmakat.

## Mit mutat be az egyes példa

**Alap chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Kezdj itt, hogy meglásd a LangChain4j legegyszerűbb használatát. Létrehozol egy `OpenAiOfficialChatModel`-t, küldesz egy promptot `.chat()`-pel, és kapsz választ. Ez megmutatja az alapokat: hogyan inicializálj modelleket egyedi végpontokkal és API kulcsokkal. Ha megérted ezt a mintát, minden más erre épül.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chattel:** Nyisd meg a [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) fájlt és kérdezd:
> - „Hogyan válthatnék a GitHub Modellekről Azure OpenAI-ra ebben a kódban?”
> - „Milyen egyéb paramétereket állíthatok az OpenAiOfficialChatModel.builder()-ben?”
> - „Hogyan adhatok streaming válaszokat a teljes válaszra való várakozás helyett?”

**Prompt mérnökség** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Most, hogy tudod, hogyan beszélj a modellel, nézzük meg, hogy mit mondasz neki. Ez a demó ugyanazt a modell beállítást használja, de öt különböző prompt mintát mutat be. Próbáld ki a zero-shot promptokat közvetlen utasításokra, few-shot promptokat példák alapján való tanulásra, chain-of-thought promptokat az érvelési lépések felfedésére, és szerepalapú promptokat kontextus beállítására. Láthatod, hogyan ad ugyanaz a modell drámaian különböző eredményeket az igény keretezése alapján.

A demó továbbá bemutat prompt sablonokat, amelyek hatékony módszert nyújtanak újrafelhasználható promptok létrehozására változókkal.
Az alábbi példa egy promptot mutat be a LangChain4j `PromptTemplate` használatával változók kitöltésére. Az MI a megadott célállomás és tevékenység alapján válaszol.

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

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chattel:** Nyisd meg a [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) fájlt és kérdezd:
> - „Mi a különbség a zero-shot és a few-shot prompt között, és mikor melyiket érdemes használni?”
> - „Hogyan befolyásolja a hőmérséklet paraméter a modell válaszait?”
> - „Milyen technikákkal lehet megelőzni a prompt injekciós támadásokat éles környezetben?”
> - „Hogyan hozhatok létre újrafelhasználható PromptTemplate objektumokat gyakori mintákhoz?”

**Eszköz integráció** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Itt lesz igazán erős a LangChain4j. `AiServices` használatával létrehozhatsz egy MI asszisztenst, amely hívja a Java metódusaidat. Csak annotáld a metódusokat `@Tool("leírás")`-ral és a LangChain4j elintézi a többit – az MI automatikusan dönti el, mikor használja az egyes eszközöket a felhasználói kérés alapján. Ez bemutatja a függvényhívást mint kulcsfontosságú technikát az MI vezérelt akciók építéséhez, nem csak kérdések megválaszolásához.

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

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chattel:** Nyisd meg a [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) fájlt és kérdezd:
> - „Hogyan működik az @Tool annotáció, és mit csinál vele a LangChain4j a háttérben?”
> - „Lehet-e az MI több eszközt egymás után hívni összetett problémák megoldására?”
> - „Mi történik, ha egy eszköz kivételt dob – hogyan kezeljem a hibákat?”
> - „Hogyan integrálnék egy valódi API-t a kalkulátor példa helyett?”

**Dokumentum kérdések és válaszok (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Itt láthatod a RAG (retrieval-augmented generation, tudás-kiegészítő generálás) működését a LangChain4j „Easy RAG” megközelítésével. A dokumentumokat betöltik, automatikusan felosztják és beágyazzák egy memóriában tárolt adatbázisba, majd egy tartalom visszakereső szolgáltatás szolgálat releváns részeket az MInek a lekérdezés idején. Az MI a dokumentumaid alapján válaszol, nem az általa általánosan ismert tudásra támaszkodik.

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

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chattel:** Nyisd meg a [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) fájlt és kérdezd:
> - „Hogyan akadályozza meg a RAG az MI tévesztéseket a modell tanítási adataihoz képest?”
> - „Miben különbözik ez az egyszerű megközelítés egy egyedi RAG csővezetéktől?”
> - „Hogyan skáláznám ezt több dokumentumra vagy nagyobb tudásbázisokra?”

**Felelős MI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Építs MI biztonságot több szintű védelemmel. Ez a demó két védelmi réteget mutat be, amelyek együtt működnek:

**1. rész: LangChain4j bemeneti korlátok** – Megakadályozzák, hogy veszélyes promptok eljussanak a LLM-hez. Egyedi korlátokat hozhatsz létre, amelyek tiltott kulcsszavakat vagy mintákat ellenőriznek. Ezek a kódodban futnak, így gyorsak és ingyenesek.

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

**2. rész: Szolgáltató biztonsági szűrők** – A GitHub Modellek beépített szűrőket használnak, amelyek elkapják, amit a te korlátjaid esetleg nem. Látsz szigorú blokkokat (HTTP 400 hibákat) súlyos szabálysértéseknél, valamint finomabb visszautasításokat, amikor az MI udvariasan elutasít.

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chattel:** Nyisd meg a [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) fájlt és kérdezd:
> - „Mi az InputGuardrail és hogyan készíthetem el a sajátomat?”
> - „Mi a különbség a szigorú blokkolás és a finom elutasítás között?”
> - „Miért használjuk együtt a korlátokat és a szolgáltató szűrőket?”

## Következő lépések

**Következő modul:** [01-bevezető - Kezdés a LangChain4j-vel](../01-introduction/README.md)

---

**Navigáció:** [← Vissza a főoldalra](../README.md) | [Következő: Modul 01 - Bevezető →](../01-introduction/README.md)

---

## Hibaelhárítás

### Első Maven build

**Probléma:** Az első `mvn clean compile` vagy `mvn package` futtatás sokáig tart (10-15 perc)

**Ok:** A Mavennek meg kell töltenie az összes projektfüggőséget (Spring Boot, LangChain4j könyvtárak, Azure SDK-k stb.) az első build során.

**Megoldás:** Ez normális viselkedés. A további build-ek sokkal gyorsabbak lesznek, mert a függőségek lokálisan cache-lődnek. A letöltési idő a hálózati sebességtől függ.

### PowerShell Maven parancs szintaxis hiba

**Probléma:** A Maven parancsok hibára futnak `Unknown lifecycle phase ".mainClass=..."` üzenettel
**Ok**: A PowerShell az `=` jelet változóértékadásként értelmezi, ami megszakítja a Maven tulajdonság szintaxisát

**Megoldás**: Használja a stop-parsing `--%` operátort a Maven parancs előtt:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

A `--%` operátor megmondja a PowerShell-nek, hogy az összes fennmaradó argumentumot szó szerint továbbítsa a Mavennek értelmezés nélkül.

### Windows PowerShell Emoji megjelenítés

**Probléma**: Az AI válaszok helyett szemét karakterek jelennek meg (pl. `????` vagy `â??`) PowerShell-ben az emojik helyett

**Ok**: A PowerShell alapértelmezett kódolása nem támogatja az UTF-8 emojikat

**Megoldás**: Futtassa ezt a parancsot Java alkalmazások futtatása előtt:
```cmd
chcp 65001
```

Ez kényszeríti az UTF-8 kódolást a terminálban. Alternatív megoldásként használja a Windows Terminalt, amely jobb Unicode támogatással rendelkezik.

### API hívások hibakeresése

**Probléma**: Hitelesítési hibák, aránykorlátozások vagy váratlan válaszok az AI modelltől

**Megoldás**: A példák tartalmazzák a `.logRequests(true)` és `.logResponses(true)` hívásokat, hogy az API hívások megjelenjenek a konzolon. Ez segít a hitelesítési hibák, aránykorlátozások vagy váratlan válaszok hibakeresésében. Ezeket a zászlókat távolítsa el az éles környezetben a naplózási zaj csökkentése érdekében.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Jogi Nyilatkozat**:
Ez a dokumentum az AI fordítási szolgáltatás [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével készült. Bár az pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum a saját nyelvén tekintendő hiteles forrásnak. Kritikus információk esetén professzionális emberi fordítást javaslunk. Nem vállalunk felelősséget az ebből eredő félreértésekért vagy félreértelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->