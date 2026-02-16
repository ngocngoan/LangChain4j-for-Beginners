# Modul 00: Gyors kezdés

## Tartalomjegyzék

- [Bevezetés](../../../00-quick-start)
- [Mi az a LangChain4j?](../../../00-quick-start)
- [LangChain4j függőségek](../../../00-quick-start)
- [Előfeltételek](../../../00-quick-start)
- [Beállítás](../../../00-quick-start)
  - [1. Szerezd meg a GitHub Tokened](../../../00-quick-start)
  - [2. Állítsd be a tokened](../../../00-quick-start)
- [Futtasd a példákat](../../../00-quick-start)
  - [1. Alap Chat](../../../00-quick-start)
  - [2. Prompt minták](../../../00-quick-start)
  - [3. Függvényhívás](../../../00-quick-start)
  - [4. Dokumentum Kérdés-Válasz (RAG)](../../../00-quick-start)
  - [5. Felelős MI](../../../00-quick-start)
- [Mit mutat minden példa](../../../00-quick-start)
- [Következő lépések](../../../00-quick-start)
- [Hibakeresés](../../../00-quick-start)

## Bevezetés

Ez a gyors kezdő útmutató arra szolgál, hogy a LangChain4j használatával minél hamarabb elindulj. Lefedi az AI alkalmazások építésének legfontosabb alapjait LangChain4j és GitHub Modellek használatával. A következő modulokban Azure OpenAI-t fogsz használni a LangChain4j-vel, hogy összetettebb alkalmazásokat építs.

## Mi az a LangChain4j?

A LangChain4j egy Java könyvtár, amely leegyszerűsíti az AI-meghajtású alkalmazások fejlesztését. Az HTTP klienssel és JSON elemzéssel való bajlódás helyett tiszta Java API-kkal dolgozol.

A LangChain név a "láncolást" jelenti, amikor több komponens kapcsolódik egymáshoz – például egy prompt láncolása egy modellhez, majd egy parserhez, vagy több AI hívás láncolása, ahol az egyik kimenete a következő bemenetévé válik. Ez a gyors kezdő a fundamentumokra koncentrál, mielőtt összetettebb láncokat fedeznétek fel.

<img src="../../../translated_images/hu/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j láncolási koncepció" width="800"/>

*Komponensek láncolása LangChain4j-ben – építőelemek kapcsolódnak, hogy erőteljes AI munkafolyamatokat hozzanak létre*

Három fő komponenst használunk:

**ChatLanguageModel** – az AI modellel való interakciók kezelője. Meghívod `model.chat("prompt")`-tal és kapsz egy válasz sztringet. Mi az `OpenAiOfficialChatModel`-t használjuk, amely OpenAI-kompatibilis végpontokkal, például GitHub Modellekkel működik.

**AiServices** – típusbiztos AI szolgáltatás interfészek létrehozása. Definiálsz metódusokat, megjelölöd őket `@Tool` annotációval, és a LangChain4j végzi az összehangolást. Az AI automatikusan meghívja a Java metódusaidat, ha szükséges.

**MessageWindowChatMemory** – a beszélgetés előzményeit tárolja. Enélkül minden kérés független. Ezzel az AI emlékszik a korábbi üzenetekre és megőrzi a kontextust több körön keresztül.

<img src="../../../translated_images/hu/architecture.eedc993a1c576839.webp" alt="LangChain4j architektúra" width="800"/>

*LangChain4j architektúra – a fő komponensek együtt dolgoznak, hogy meghajtják AI alkalmazásaidat*

## LangChain4j függőségek

Ez a gyors kezdő két Maven függőséget használ a [`pom.xml`](../../../00-quick-start/pom.xml) fájlban:

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
```

A `langchain4j-open-ai-official` modul biztosítja az `OpenAiOfficialChatModel` osztályt, amely OpenAI-kompatibilis API-khoz csatlakozik. A GitHub Modellek ugyanazt az API formátumot használják, így nincs szükség speciális adapterre – csak állítsd be az alap URL-t `https://models.github.ai/inference`-re.

## Előfeltételek

**Dev Container használata esetén?** A Java és Maven már telepítve van. Csak egy GitHub Személyes Hozzáférési Tokenre van szükséged.

**Helyi fejlesztéshez:**
- Java 21+, Maven 3.9+
- GitHub Személyes Hozzáférési Token (lásd az alábbi útmutatót)

> **Megjegyzés:** Ez a modul a GitHub Modellek `gpt-4.1-nano` modelljét használja. Ne módosítsd a modell nevét a kódban – úgy van konfigurálva, hogy a GitHub elérhető modelljeivel működjön.

## Beállítás

### 1. Szerezd meg a GitHub Tokened

1. Lépj a [GitHub Beállítások → Személyes Hozzáférési Tokenek](https://github.com/settings/personal-access-tokens) oldalra
2. Kattints az „Új token létrehozása” gombra
3. Adj egy beszédes nevet (pl. „LangChain4j Demo”)
4. Állíts be lejárati időt (ajánlott 7 nap)
5. Az „Fiók engedélyei” alatt keresd meg a „Modellek”-t és állítsd olvasási jogra („Read-only”)
6. Kattints a „Token létrehozása” gombra
7. Másold ki és mentsd el a tokent – később nem fogod újra látni

### 2. Állítsd be a tokened

**1. lehetőség: VS Code használata (Ajánlott)**

Ha VS Code-ot használsz, add hozzá a tokent a projekt gyökérkönyvtárában lévő `.env` fájlba:

Ha a `.env` fájl nem létezik, másold le a `.env.example` fájlt `.env`-nek vagy hozz létre egy új `.env` fájlt a projekt gyökerében.

**Példa `.env` fájl:**
```bash
# A /workspaces/LangChain4j-for-Beginners/.env fájlban
GITHUB_TOKEN=your_token_here
```

Ezután egyszerűen jobb klikkelj bármelyik demó fájlon (pl. `BasicChatDemo.java`) a Felfedezőben, és válaszd a **"Run Java"** parancsot, vagy használd a Futtatás és Hibakeresés panelen az indítási konfigurációkat.

**2. lehetőség: Terminál használata**

Állítsd be a tokent környezeti változóként:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Futtasd a példákat

**VS Code használatával:** Egyszerűen jobb kattintás bármelyik demó fájlon a Felfedezőben és válaszd a **"Run Java"** parancsot, vagy használd az indítási konfigurációkat a Futtatás és Hibakeresés panelen (előtte győződj meg róla, hogy hozzáadtad a tokened a `.env` fájlhoz).

**Maven használatával:** Alternatívaként futtathatod parancssorból is:

### 1. Alap Chat

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

Bemutatja a zero-shot, few-shot, láncolt gondolkodás, és szerepalapú promptokat.

### 3. Függvényhívás

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

Az AI automatikusan meghívja a Java metódusaidat, ha szükséges.

### 4. Dokumentum Kérdés-Válasz (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Tegyél fel kérdéseket a `document.txt` tartalmával kapcsolatban.

### 5. Felelős MI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Nézd meg, hogyan blokkolják az AI biztonsági szűrők a káros tartalmakat.

## Mit mutat minden példa

**Alap Chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Innen indulj, hogy a LangChain4j alapjait megismerd. Létrehozol egy `OpenAiOfficialChatModel` példányt, küldesz egy promptot `.chat()`-tel, és kapsz egy választ. Ez bemutatja az alapokat: hogyan inicializálj modelleket egyedi végpontokkal és API kulcsokkal. Ha ezt a mintát megérted, minden más erre épül.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Próbáld ki [GitHub Copilot](https://github.com/features/copilot) Chattel:** Nyisd meg a [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) fájlt és kérdezd meg:
> - „Hogyan váltanék GitHub Modellekről Azure OpenAI-ra ebben a kódban?”
> - „Milyen más paramétereket lehet beállítani az OpenAiOfficialChatModel.builder()-ben?”
> - „Hogyan tudok streaming választ hozzáadni a teljes válaszra való várás helyett?”

**Prompt tervezés** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Most, hogy tudod, hogyan beszélj egy modellel, nézzük meg, mit mondasz neki. Ez a demó ugyanolyan modell beállítást használ, de öt különböző prompt mintát mutat be. Próbáld ki a zero-shot promptokat közvetlen utasításokkal, a few-shot promptokat példákkal való tanulással, láncolt gondolkodás promptokat, amelyek bemutatják az érvelési lépéseket, és szerepalapú promptokat a kontextus beállításához. Látni fogod, hogy ugyanaz a modell hogyan ad drámai módon különböző eredményeket a kérés megfogalmazásától függően.

A demó bemutatja a prompt sablonokat is, amelyek erőteljes módjai az újrahasználható promptok létrehozásának változókkal.
Az alábbi példa egy promptot mutat be a LangChain4j `PromptTemplate` használatával, amely kitölti a változókat. Az AI a megadott célhely és tevékenység alapján válaszol.

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

> **🤖 Próbáld ki [GitHub Copilot](https://github.com/features/copilot) Chattel:** Nyisd meg a [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) fájlt és kérdezd meg:
> - „Mi a különbség a zero-shot és few-shot promptolás között, és mikor melyiket használjam?”
> - „Hogyan hat a model válaszaira a 'temperature' paraméter?”
> - „Milyen technikák vannak a prompt inject támadások megelőzésére éles környezetben?”
> - „Hogyan tudok újrahasználható PromptTemplate objektumokat létrehozni gyakori mintákhoz?”

**Eszköz integráció** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Itt válik erőssé a LangChain4j. Az `AiServices` segítségével egy AI asszisztenst hozol létre, amely képes meghívni a Java metódusaidat. Csak jelöld meg a metódusokat `@Tool("leírás")` annotációval, a LangChain4j pedig intézi a többit – az AI automatikusan eldönti, mikor melyik eszközt használja a felhasználó kérdése alapján. Ez bemutatja a függvényhívást, ami kulcsfontosságú technika az olyan AI építéséhez, amely képes cselekedni és nem csak válaszolni.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Próbáld ki [GitHub Copilot](https://github.com/features/copilot) Chattel:** Nyisd meg a [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) fájlt és kérdezd meg:
> - „Hogyan működik az @Tool annotáció, és mit csinál a LangChain4j a háttérben vele?”
> - „Tud az AI több eszközt egymás után hívni összetett problémák megoldására?”
> - „Mi történik, ha egy eszköz kivételt dob – hogyan kezeljem a hibákat?”
> - „Hogyan integrálnék egy valós API-t a kalkulátor példa helyett?”

**Dokumentum Kérdés-Válasz (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Itt látod a RAG (visszakeresésen alapuló generálás) alapjait. Ahelyett, hogy a modell tanítóadataira támaszkodnál, beolvasol tartalmat a [`document.txt`](../../../00-quick-start/document.txt) fájlból és beilleszted azt a promptba. Az AI a dokumentumod alapján válaszol, nem az általános tudása alapján. Ez az első lépés olyan rendszerek építéséhez, amelyek a saját adataiddal dolgoznak.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Megjegyzés:** Ez az egyszerű módszer az egész dokumentumot beleteszi a promptba. Nagyobb fájlok (>10KB) esetén túlléped a kontextus határokat. A 03-as modul foglalkozik a darabolással és vektoros kereséssel az éles RAG rendszerekhez.

> **🤖 Próbáld ki [GitHub Copilot](https://github.com/features/copilot) Chattel:** Nyisd meg a [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) fájlt és kérdezd meg:
> - „Hogyan előzi meg a RAG az AI tévesztéseket a modell tanítóadatainak használatához képest?”
> - „Mi a különbség az egyszerű megközelítés és a vektoros beágyazásokkal való keresés között?”
> - „Hogyan skálázhatom ezt több dokumentum vagy nagyobb tudásbázis kezelésére?”
> - „Mik a legjobb gyakorlatok a prompt struktúrázására, hogy az AI csak a megadott kontextust használja?”

**Felelős MI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Építs AI biztonságot mélységi védelemmel. Ez a demó két védelmi réteget mutat be együttműködve:

**1. rész: LangChain4j bemeneti védőkorlátai** – Blokkolja a veszélyes promptokat, mielőtt elérnék a LLM-et. Hozz létre egyedi védőkorlátokat, amelyek tiltott kulcsszavakat vagy mintákat keresnek. Ezek a kódban futnak, gyorsak és ingyenesek.

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

**2. rész: Szolgáltató biztonsági szűrők** – A GitHub Modellek beépített szűrőkkel rendelkeznek, amelyek elkapják, amit a védőkorlátaid talán nem. Láthatod a kemény blokkokat (HTTP 400 hibák) súlyos szabályszegések esetén, és a lágy visszautasításokat, amikor az AI udvariasan megtagadja a választ.

> **🤖 Próbáld ki [GitHub Copilot](https://github.com/features/copilot) Chattel:** Nyisd meg a [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) fájlt és kérdezd meg:
> - „Mi az az InputGuardrail és hogyan készíthetek sajátot?”
> - „Mi a különbség a kemény blokk és a lágy visszautasítás között?”
> - „Miért használjunk egyszerre védőkorlátokat és szolgáltatói szűrőket?”

## Következő lépések

**Következő modul:** [01-introduction - Kezdés LangChain4j-vel és gpt-5-tel Azure-on](../01-introduction/README.md)

---

**Navigáció:** [← Vissza a főoldalra](../README.md) | [Következő: Modul 01 - Bevezetés →](../01-introduction/README.md)

---

## Hibakeresés

### Első Maven build

**Probléma:** Az első `mvn clean compile` vagy `mvn package` hosszú ideig tart (10-15 perc)

**Ok:** A Mavennek először le kell töltenie az összes projektfüggőséget (Spring Boot, LangChain4j könyvtárak, Azure SDK-k stb.).

**Megoldás:** Ez normális viselkedés. A következő build-ek sokkal gyorsabbak lesznek, mivel a függőségek helyben lesznek tárolva. A letöltési idő a hálózati sebességtől függ.
### PowerShell Maven parancsszintaxis

**Probléma**: A Maven parancsok hibával `Unknown lifecycle phase ".mainClass=..."` sikertelenek

**Oka**: A PowerShell az `=` jelet változó hozzárendelési operátorként értelmezi, ami megszakítja a Maven tulajdonság szintaxist

**Megoldás**: Használja a megállító szintaxist `--%` a Maven parancs előtt:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

A `--%` operátor megmondja a PowerShellnek, hogy az összes további argumentumot szó szerint továbbítsa a Mavennek, értelmezés nélkül.

### Windows PowerShell Emoji megjelenítés

**Probléma**: AI válaszok helyett szemét karakterek jelennek meg (pl. `????` vagy `â??`) PowerShell-ben az emoji helyett

**Oka**: A PowerShell alapértelmezett kódolása nem támogatja az UTF-8 emojikat

**Megoldás**: Futtassa ezt a parancsot Java alkalmazások futtatása előtt:
```cmd
chcp 65001
```

Ez kényszeríti az UTF-8 kódolást a terminálban. Alternatívaként használja a Windows Terminalt, amely jobb Unicode támogatással rendelkezik.

### API hívások hibakeresése

**Probléma**: Hitelesítési hibák, lekérdezési korlátok vagy váratlan válaszok az AI modelltől

**Megoldás**: A példák tartalmazzák a `.logRequests(true)` és `.logResponses(true)` beállításokat, hogy megjelenítsék az API hívásokat a konzolon. Ez segít a hitelesítési hibák, lekérdezési korlátok vagy váratlan válaszok elhárításában. Eltávolítsa ezeket a jelölőket a gyártási környezetben a naplózási zaj csökkentésére.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Felelősség kizárása**:  
Ezt a dokumentumot az AI alapú [Co-op Translator](https://github.com/Azure/co-op-translator) fordítószolgáltatás segítségével fordítottuk. Bár a pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások tartalmazhatnak hibákat vagy pontatlanságokat. Az eredeti, anyanyelvi dokumentum tekintendő a hiteles forrásnak. Kritikus információk esetén professzionális, emberi fordítást javaslunk. Nem vállalunk felelősséget az ebből adódó félreértésekért vagy félreértelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->