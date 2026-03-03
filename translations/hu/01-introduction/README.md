# Modul 01: Kezdés a LangChain4j-vel

## Tartalomjegyzék

- [Videó bemutató](../../../01-introduction)
- [Amit megtanulsz](../../../01-introduction)
- [Előfeltételek](../../../01-introduction)
- [A fő probléma megértése](../../../01-introduction)
- [Tokenek megértése](../../../01-introduction)
- [Hogyan működik a memória](../../../01-introduction)
- [Hogyan használja ezt a LangChain4j](../../../01-introduction)
- [Azure OpenAI infrastruktúra telepítése](../../../01-introduction)
- [Alkalmazás futtatása helyileg](../../../01-introduction)
- [Az alkalmazás használata](../../../01-introduction)
  - [Állapot nélküli csevegés (bal panel)](../../../01-introduction)
  - [Állapotfüggő csevegés (jobb panel)](../../../01-introduction)
- [Következő lépések](../../../01-introduction)

## Videó bemutató

Nézd meg ezt az élő bemutatót, amely elmagyarázza, hogyan kezdj neki ennek a modulnak:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Amit megtanulsz

A gyors start során GitHub modelleket használtál promptok küldésére, eszközök meghívására, RAG pipeline építésére és guardrail tesztelésére. Ezek a bemutatók megmutatták, mi lehetséges — most áttérünk az Azure OpenAI és a GPT-5.2 használatára, és elkezdjük produkciós stílusú alkalmazások építését. Ez a modul olyan beszélgető AI-ra fókuszál, amely emlékszik a kontextusra és fenntartja az állapotot — ezek azok a fogalmak, amelyeket a gyors start demók használtak a háttérben, de nem magyaráztak el.

Az egész útmutató során az Azure OpenAI GPT-5.2-jét használjuk, mert fejlett érvelési képességei miatt a különböző minták viselkedése egyértelműbb. Amikor memóriát adsz hozzá, tisztán látni fogod a különbséget. Ez megkönnyíti annak megértését, hogy az egyes komponensek mit hoznak az alkalmazásodba.

Egy alkalmazást építesz, amely mindkét mintát bemutatja:

**Állapot nélküli csevegés** - Minden kérés független. A modell nem emlékszik az előző üzenetekre. Ez az a minta, amit a gyors startban használtál.

**Állapotfüggő beszélgetés** - Minden kérés tartalmazza a beszélgetés előzményeit. A modell megőrzi a kontextust több fordulón át. Erre van szükség a produkciós alkalmazásokban.

## Előfeltételek

- Azure előfizetés Azure OpenAI hozzáféréssel
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Megjegyzés:** A Java, Maven, Azure CLI és Azure Developer CLI (azd) előre telepítve van a biztosított devcontainerben.

> **Megjegyzés:** Ez a modul az Azure OpenAI GPT-5.2-jét használja. A telepítés automatikusan a `azd up` paranccsal történik — ne módosítsd a modell nevét a kódban.

## A fő probléma megértése

A nyelvi modellek állapot nélküli működésűek. Minden API hívás független. Ha elküldöd, hogy „A nevem John”, majd megkérdezed, „Mi a nevem?”, a modell nem tudja, hogy épp most mutatkoztál be. Minden kérés úgy kezel, mintha az lenne az első beszélgetésed valaha.

Ez egyszerű kérdés-válasz esetén rendben van, de valódi alkalmazásokhoz haszontalan. Az ügyfélszolgálati botoknak emlékezniük kell arra, amit mondtál. A személyi asszisztenseknek szükségük van kontextusra. Bármilyen többfordulós beszélgetés memóriát igényel.

Az alábbi ábra összehasonlítja a két megközelítést — bal oldalon egy állapot nélküli hívás, amely elfelejti a neved; jobb oldalon egy állapotfüggő hívás, melyet a ChatMemory támogat, és emlékszik rá.

<img src="../../../translated_images/hu/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*A különbség az állapot nélküli (független hívások) és az állapotfüggő (kontextus-érzékeny) beszélgetések között*

## Tokenek megértése

Mielőtt belevágnánk a beszélgetésekbe, fontos megérteni a tokeneket — a szöveg alapvető egységeit, amelyeket a nyelvi modellek feldolgoznak:

<img src="../../../translated_images/hu/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Példa arra, hogyan bontja szét a szöveget tokenekre — az "I love AI!" 4 különálló feldolgozási egységgé válik*

A tokenek azok, amivel az AI modellek mérik és dolgozzák fel a szöveget. Szavak, írásjelek és akár szóközök is lehetnek tokenek. A modellednek van egy limitje, hogy mennyi tokent tud egyszerre feldolgozni (GPT-5.2 esetén 400,000, ebből legfeljebb 272,000 bemeneti és 128,000 kimeneti token). A tokenek megértése segít a beszélgetések hosszának és költségeinek kezelésében.

## Hogyan működik a memória

A beszélgetési memória megoldja az állapot nélküli problémát azáltal, hogy megőrzi a beszélgetés előzményeit. Mielőtt elküldenéd a kérésed a modellnek, a keretrendszer hozzáfűzi a releváns korábbi üzeneteket. Amikor megkérdezed, hogy „Mi a nevem?”, a rendszer valójában az egész beszélgetési előzményt elküldi, lehetővé téve, hogy a modell lássa, hogy korábban azt mondtad, „A nevem John”.

A LangChain4j memóriamegoldásokat kínál, amelyek ezt automatikusan kezelik. Meghatározod, hány üzenetet szeretnél megtartani, és a keretrendszer kezeli a kontextusablakot. Az alábbi ábra megmutatja, hogyan tartja karban a MessageWindowChatMemory az üzenetek csúszó ablakát.

<img src="../../../translated_images/hu/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*A MessageWindowChatMemory egy csúszó ablakban tartja a legutóbbi üzeneteket, automatikusan kidobva a régi üzeneteket*

## Hogyan használja ezt a LangChain4j

Ez a modul kiterjeszti a gyors startot a Spring Boot integrálásával és beszélgetési memória hozzáadásával. Így illeszkednek össze a részek:

**Függőségek** — Adj hozzá két LangChain4j könyvtárat:

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

**Chat modell** — Konfiguráld az Azure OpenAI-t Spring bean-ként ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

A builder a `azd up` által beállított környezeti változókból olvassa a hitelesítő adatokat. A `baseUrl` beállítása az Azure végpontodra teszi az OpenAI klienst Azure OpenAI-kompatibilissé.

**Beszélgetési memória** — Kövesd a chat előzményeit a MessageWindowChatMemory-val ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Hozz létre memóriát `withMaxMessages(10)` paraméterrel, hogy az utolsó 10 üzenetet megtartsa. Adj hozzá felhasználói és AI üzeneteket típusos csomagolókkal: `UserMessage.from(text)` és `AiMessage.from(text)`. A történetet a `memory.messages()` segítségével kérheted le, és elküldheted a modellnek. A szolgáltatás külön memória példányokat tárol beszélgetésazonosítónként, így egyszerre több felhasználó is cseveghet.

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chat segítségével:** Nyisd meg a [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) fájlt, és kérdezd meg:
> - "Hogyan dönt a MessageWindowChatMemory arról, mely üzeneteket dobja el, amikor az ablak megtelik?"
> - "Megvalósíthatok-e egyéni memóriatárolót adatbázis használatával in-memory helyett?"
> - "Hogyan adhatnék hozzá összefoglalást, hogy tömörítsem a régi beszélgetési előzményeket?"

Az állapot nélküli csevegés végpont teljesen kihagyja a memóriát — csak `chatModel.chat(prompt)`, mint a gyors startban. Az állapotfüggő végpont hozzáadja az üzeneteket a memóriához, lekéri a történetet, és minden kéréshez csatolja azt. Ugyanaz a modell konfiguráció, de más minták.

## Azure OpenAI infrastruktúra telepítése

**Bash:**
```bash
cd 01-introduction
azd up  # Válassza ki az előfizetést és a helyszínt (ajánlott: eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Válassza ki az előfizetést és a helyet (az eastus2 ajánlott)
```

> **Megjegyzés:** Ha időtúllépési hibába futsz (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), egyszerűen futtasd újra az `azd up` parancsot. Az Azure erőforrások még háttérben települhetnek, az ismétlés lehetővé teszi, hogy a telepítés befejeződjön, amint az erőforrások végleges állapotba kerülnek.

Ez a következőket teszi:
1. Telepíti az Azure OpenAI erőforrást a GPT-5.2-vel és a text-embedding-3-small modellekkel
2. Automatikusan létrehozza a `.env` fájlt a projekt gyökérkönyvtárában a hitelesítő adatokkal
3. Beállít minden szükséges környezeti változót

**Telepítési problémák esetén?** Lásd az [infrastruktúra README](infra/README.md) fájlt a részletes hibakereséshez, többek között aldomain névütközések, manuális Azure Portal telepítési lépések és modell konfigurációs útmutató.

**Ellenőrizd, hogy sikeres volt-e a telepítés:**

**Bash:**
```bash
cat ../.env  # Meg kell jelenítenie az AZURE_OPENAI_ENDPOINT, API_KEY stb. értékeket.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Az AZURE_OPENAI_ENDPOINT, API_KEY stb. értékét kell megjeleníteni.
```

> **Megjegyzés:** Az `azd up` parancs automatikusan létrehozza a `.env` fájlt. Ha később frissíteni kell, kézzel szerkesztheted, vagy újragenerálhatod a következőkkel:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```


## Alkalmazás futtatása helyileg

**Ellenőrizd a telepítést:**

Győződj meg arról, hogy a `.env` fájl létezik a gyökérkönyvtárban az Azure hitelesítő adatokkal. Futtasd ezt a modul könyvtárából (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Meg kell jeleníteni az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Meg kell jeleníteni AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**Indítsd el az alkalmazásokat:**

**1. lehetőség: Spring Boot Dashboard használata (Ajánlott VS Code felhasználóknak)**

A fejlesztői konténer tartalmazza a Spring Boot Dashboard kiterjesztést, amely vizuális felületet nyújt az összes Spring Boot alkalmazás kezelésére. Megtalálod az Activity Bar bal oldalán VS Code-ban (keresd a Spring Boot ikont).

A Spring Boot Dashboard segítségével:
- Láthatod a munkaterületen elérhető összes Spring Boot alkalmazást
- Egy kattintással indíthatod/leállíthatod az alkalmazásokat
- Valós időben követheted az alkalmazás naplóit
- Figyelemmel kísérheted az alkalmazások állapotát

Egyszerűen kattints a lejátszás gombra az "introduction" modul mellett ennek indításához, vagy indítsd el az összes modult egyszerre.

<img src="../../../translated_images/hu/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard VS Code-ban — indítsd, állítsd le és figyeld az összes modult egy helyen*

**2. lehetőség: Shell scriptek használata**

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

Vagy indítsd csak ezt a modult:

**Bash:**
```bash
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Mindkét script automatikusan betölti a környezeti változókat a gyökér `.env` fájlból és lefordítja a JAR fájlokat, ha még nem léteznek.

> **Megjegyzés:** Ha előbb szeretnéd kézzel lefordítani az összes modult:
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


Nyisd meg a http://localhost:8080 címet a böngésződben.

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


## Az alkalmazás használata

Az alkalmazás egy webes felületet biztosít két párhuzamosan megjelenített csevegés implementációval.

<img src="../../../translated_images/hu/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Irányítópult, amely mind a Simple Chat (állapot nélküli), mind a Conversational Chat (állapotfüggő) opciókat mutatja*

### Állapot nélküli csevegés (bal panel)

Ezzel próbáld először. Írd be, hogy „A nevem John”, majd azonnal kérdezd meg: „Mi a nevem?” A modell nem fog emlékezni, mert minden üzenet független. Ez demonstrálja az alapvető nyelvi modell integráció fő problémáját — nincs beszélgetési kontextus.

<img src="../../../translated_images/hu/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*Az AI nem emlékszik az előző üzenetben mondott nevedre*

### Állapotfüggő csevegés (jobb panel)

Most próbáld meg ugyanezt itt. Írd be, hogy „A nevem John”, majd „Mi a nevem?” Ezúttal emlékszik. A különbség a MessageWindowChatMemory — ez kezeli a beszélgetési előzményeket és minden kéréshez csatolja azt. Így működik a produkciós beszélgető AI.

<img src="../../../translated_images/hu/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*Az AI emlékszik a beszélgetés korábbi részében mondott nevedre*

Mindkét panel ugyanazt a GPT-5.2 modellt használja. Az egyetlen különbség a memória. Ez világossá teszi, hogy a memória mit ad hozzá az alkalmazásodhoz, és miért alapvető a valódi használati esetekhez.

## Következő lépések

**Következő modul:** [02-prompt-engineering - Prompt Engineering GPT-5.2-vel](../02-prompt-engineering/README.md)

---

**Navigáció:** [← Előző: Modul 00 - Gyors start](../00-quick-start/README.md) | [Vissza a főoldalra](../README.md) | [Következő: Modul 02 - Prompttervezés →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Jogi nyilatkozat**:
Ezt a dokumentumot az AI fordító szolgáltatás [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével fordítottuk. Bár pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum anyanyelvű változata tekintendő hivatalos forrásnak. Fontos információk esetén professzionális emberi fordítást javaslunk. Nem vállalunk felelősséget az ezen fordítás használatából eredő félreértésekért vagy helytelen értelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->