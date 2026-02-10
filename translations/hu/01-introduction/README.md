# Modul 01: LangChain4j használatának elkezdése

## Tartalomjegyzék

- [Mit tanulhatsz meg](../../../01-introduction)
- [Előfeltételek](../../../01-introduction)
- [Az alapvető probléma megértése](../../../01-introduction)
- [A tokenek megértése](../../../01-introduction)
- [Hogyan működik a memória](../../../01-introduction)
- [Hogyan használja ezt LangChain4j](../../../01-introduction)
- [Azure OpenAI infrastruktúra telepítése](../../../01-introduction)
- [Az alkalmazás helyi futtatása](../../../01-introduction)
- [Az alkalmazás használata](../../../01-introduction)
  - [Állapotmentes csevegés (bal panel)](../../../01-introduction)
  - [Állapottartó csevegés (jobb panel)](../../../01-introduction)
- [Következő lépések](../../../01-introduction)

## Mit tanulhatsz meg

Ha elvégezted a gyors kezdő lépéseket, láttad, hogyan küldhetsz promptokat és kaphatsz válaszokat. Ez az alap, de a valódi alkalmazások többet igényelnek. Ez a modul megtanít arra, hogyan építs fel olyan konverzációs AI-t, amely emlékszik a kontextusra és állapotot tart fenn – ez a különbség egy egyszeri demo és egy éles alkalmazás között.

Ebben az útmutatóban az Azure OpenAI GPT-5.2-t használjuk, mert fejlett érvelési képességei révén jobban láthatóvá teszik a különböző minták viselkedését. Amikor memóriát adsz hozzá, egyértelműen látni fogod a különbséget. Ez megkönnyíti annak megértését, hogy mit ad hozzá az egyes komponens az alkalmazásodhoz.

Egy alkalmazást építesz, amely mindkét mintát bemutatja:

**Állapotmentes csevegés** – Minden kérés független. A modell nem emlékszik a korábbi üzenetekre. Ez a minta, amit a gyors kezdés során használtál.

**Állapottartó beszélgetés** – Minden kérés tartalmazza a beszélgetés előzményét. A modell több körön át tartja a kontextust. Erre van szükség az éles alkalmazásoknál.

## Előfeltételek

- Azure előfizetés Azure OpenAI hozzáféréssel
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Megjegyzés:** A Java, Maven, Azure CLI és Azure Developer CLI (azd) előre telepítve van a biztosított fejlesztői konténerben.

> **Megjegyzés:** Ez a modul az Azure OpenAI GPT-5.2-t használja. A telepítés automatikusan beállítódik az `azd up` paranccsal – ne módosítsd a modell nevét a kódban.

## Az alapvető probléma megértése

A nyelvi modellek állapotmentesek. Minden API hívás független. Ha elküldöd, hogy „A nevem John”, majd megkérdezed, „Mi a nevem?”, a modellnek fogalma sincs arról, hogy előzőleg bemutatkoztál. Minden kérésnél úgy jár el, mintha ez lenne az első beszélgetésed.

Ez egyszerű kérdés-válasz esetén rendben van, de valódi alkalmazásokhoz használhatatlan. Az ügyfélszolgálati chatbotoknak emlékezniük kell arra, amit mondtál. A személyi asszisztenseknek kontextus kell. Bármilyen többkörös beszélgetés memóriát igényel.

<img src="../../../translated_images/hu/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Állapotmentes vs állapottartó beszélgetések" width="800"/>

*Az állapotmentes (független hívások) és állapottartó (kontekstusérzékeny) beszélgetések közti különbség*

## A tokenek megértése

Mielőtt belevágnánk a beszélgetésekbe, fontos megérteni a tokeneket – az alapvető szövegegységeket, amelyeket a nyelvi modellek feldolgoznak:

<img src="../../../translated_images/hu/token-explanation.c39760d8ec650181.webp" alt="Token magyarázat" width="800"/>

*Példa arra, hogyan bontják a szöveget tokenekre – az "I love AI!" négy külön feldolgozási egység lesz*

A tokenekkel mérik és dolgozzák fel a szöveget az AI modellek. A szavak, írásjelek és még a szóközök is lehetnek tokenek. A modellednek van egy limitje, hogy egyszerre hány tokent tud feldolgozni (GPT-5.2 esetén 400,000, maximum 272,000 bemeneti token és 128,000 kimeneti token). A tokenek ismerete segít kezelni a beszélgetések hosszát és a költségeket.

## Hogyan működik a memória

A csevegés memóriája megoldja az állapotmentesség problémáját azzal, hogy megtartja a beszélgetés előzményeit. Mielőtt elküldené a kérést a modellnek, a keretrendszer előre hozzáfűzi a releváns korábbi üzeneteket. Amikor megkérdezed „Mi a nevem?”, a rendszer tulajdonképpen az egész beszélgetés előzményt elküldi, így a modell "láthatja", hogy korábban azt mondtad, „A nevem John”.

A LangChain4j memória implementációkat kínál, amelyek ezt automatikusan kezelik. Te választod meg, hogy hány üzenetet tartson meg, a keretrendszer pedig kezeli a kontextusablakot.

<img src="../../../translated_images/hu/memory-window.bbe67f597eadabb3.webp" alt="Memória ablak koncepció" width="800"/>

*A MessageWindowChatMemory egy csúszó ablakot tart fenn a legfrissebb üzenetekből, automatikusan eltávolítva a régieket*

## Hogyan használja ezt LangChain4j

Ez a modul kibővíti a gyors kezdést Spring Boot integrációval és konverzációs memória hozzáadásával. Így illeszkednek össze az elemek:

**Függőségek** – Adjunk hozzá két LangChain4j könyvtárat:

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

**Chat modell** – Konfiguráld az Azure OpenAI-t Spring bean-ként ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

A builder a hitelesítő adatokat az `azd up` által beállított környezeti változókból olvassa. Az `baseUrl` beállítása az Azure végpontodra teszi az OpenAI klienst Azure támogatásúvá.

**Beszélgetési memória** – Kövesd a chat előzményeket MessageWindowChatMemory-val ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

A memóriát a `withMaxMessages(10)`-nel hozd létre, hogy megtartsd az utolsó 10 üzenetet. A felhasználói és AI üzeneteket típusos csomagolókkal add hozzá: `UserMessage.from(text)` és `AiMessage.from(text)`. Az előzményt `memory.messages()`-sel kapod vissza, amit a modellhez küldesz. A szolgáltatás minden beszélgetési azonosítóhoz külön memóriapéldányt tárol, így párhuzamosan több felhasználó is cseveghet.

> **🤖 Próbáld ki [GitHub Copilot](https://github.com/features/copilot) Chat segítségével:** Nyisd meg a [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) fájlt és kérdezd meg:
> - "Hogyan dönt a MessageWindowChatMemory, hogy mely üzeneteket dobja el, amikor az ablak megtelik?"
> - "Megvalósíthatok egyedi memória tárolást adatbázissal az in-memory helyett?"
> - "Hogyan adhatnék hozzá összegzést a régi beszélgetés előzmény tömörítésére?"

Az állapotmentes chat végpont teljesen kihagyja a memóriát – csak `chatModel.chat(prompt)`-ot hív, mint a gyors kezdésben. Az állapottartó végpont viszont üzeneteket ad a memóriához, lekéri az előzményt, és minden kéréshez mellékeli a kontextust. Ugyanaz a modellkonfiguráció, eltérő minták.

## Azure OpenAI infrastruktúra telepítése

**Bash:**
```bash
cd 01-introduction
azd up  # Válassza ki a feliratkozást és a helyet (az eastus2 ajánlott)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Válassza ki az előfizetést és a helyszínt (az eastus2 ajánlott)
```

> **Megjegyzés:** Ha időtúllépési hiba lép fel (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), egyszerűen futtasd újra az `azd up` parancsot. Az Azure erőforrások még mindig települhetnek a háttérben, és az ismétlés lehetővé teszi a telepítés befejezését, amikor az erőforrások elérik a végső állapotot.

Ez megteszi a következőket:
1. Telepíti az Azure OpenAI erőforrást a GPT-5.2 és text-embedding-3-small modellekkel
2. Automatikusan generál egy `.env` fájlt a projekt gyökérkönyvtárába hitelesítő adatokkal
3. Beállítja az összes szükséges környezeti változót

**Telepítési problémák?** Lásd az [Infrastructure README](infra/README.md) fájlt részletes hibaelhárításhoz, beleértve az alkönyvtár névütközéseket, kézi Azure Portal telepítési lépéseket és modellkonfigurációs útmutatást.

**Ellenőrizd, hogy a telepítés sikeres volt-e:**

**Bash:**
```bash
cat ../.env  # Meg kell jelenítenie az AZURE_OPENAI_ENDPOINT, API_KEY stb. értékeket.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Meg kell jelenítenie az AZURE_OPENAI_ENDPOINT, API_KEY stb. értékeket.
```

> **Megjegyzés:** Az `azd up` parancs automatikusan generálja a `.env` fájlt. Ha később frissíteni kell, vagy szerkesztheted a `.env` fájlt kézzel, vagy újragenerálhatod az alábbi parancsokkal:
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

## Az alkalmazás helyi futtatása

**Telepítés ellenőrzése:**

Győződj meg róla, hogy a `.env` fájl létezik a gyökérkönyvtárban az Azure hitelesítő adatokkal:

**Bash:**
```bash
cat ../.env  # Meg kell jelenítenie az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Meg kell jelenítenie az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**Indítsd el az alkalmazásokat:**

**1. lehetőség: Spring Boot Dashboard használata (ajánlott VS Code felhasználóknak)**

A fejlesztői konténer tartalmazza a Spring Boot Dashboard bővítményt, amely vizuális felületet biztosít az összes Spring Boot alkalmazás kezeléséhez. Megtalálod a VS Code bal oldali Activity Bar-ján (keresd a Spring Boot ikont).

A Spring Boot Dashboard segítségével:
- Megnézheted az összes elérhető Spring Boot alkalmazást a workspace-ben
- Egyszerű kattintással indíthatsz/leállíthatsz alkalmazásokat
- Valós időben nézheted az alkalmazás naplóit
- Követheted az alkalmazás állapotát

Egyszerűen kattints a lejátszás gombra az „introduction” modul mellett az indításhoz, vagy indíts el egyszerre minden modult.

<img src="../../../translated_images/hu/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**2. lehetőség: Shell szkriptek használata**

Indítsd el az összes webalkalmazást (modulok 01-04):

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

Vagy csak ezt a modult indítsd:

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

Mindkét szkript automatikusan betölti a környezeti változókat a gyökér `.env` fájlból, és ha a JAR fájlok nem léteznek, felépíti azokat.

> **Megjegyzés:** Ha előnyben részesíted, hogy kézzel építsd fel az összes modult az indítás előtt:
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

Az alkalmazás egy webes felületet biztosít két párhuzamosan futó csevegés implementációval.

<img src="../../../translated_images/hu/home-screen.121a03206ab910c0.webp" alt="Alkalmazás kezdőképernyő" width="800"/>

*Irányítópult, ahol mind az egyszerű csevegés (állapotmentes), mind a beszélgetés alapú csevegés (állapottartó) opciók megjelennek*

### Állapotmentes csevegés (bal panel)

Ezt próbáld ki először. Kérdezd meg: „A nevem John”, majd azonnal „Mi a nevem?” A modell nem fog emlékezni, mert minden üzenet független. Ez jól bemutatja a nyelvi modell egyszerű integrációjának alapvető problémáját – nincs beszélgetési kontextus.

<img src="../../../translated_images/hu/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Állapotmentes csevegés bemutató" width="800"/>

*Az AI nem emlékszik a nevedre az előző üzenetből*

### Állapottartó csevegés (jobb panel)

Most próbáld meg ugyanazt itt is. Kérdezd meg: „A nevem John”, majd „Mi a nevem?” Ezúttal emlékszik. A különbség a MessageWindowChatMemory – ez megőrzi a beszélgetés előzményét és minden kéréshez mellékeli. Így működik az éles beszélgetési AI.

<img src="../../../translated_images/hu/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Állapottartó csevegés bemutató" width="800"/>

*Az AI emlékszik korábbi beszélgetésből a nevedre*

Mindkét panel ugyanazt a GPT-5.2 modellt használja. Az egyetlen különbség a memória. Ez világossá teszi, mit ad hozzá a memória az alkalmazásodhoz és miért elengedhetetlen az éles felhasználási esetekhez.

## Következő lépések

**Következő modul:** [02-prompt-engineering - Prompt tervezés GPT-5.2-vel](../02-prompt-engineering/README.md)

---

**Navigáció:** [← Előző: Modul 00 - Gyors kezdés](../00-quick-start/README.md) | [Vissza a főoldalra](../README.md) | [Következő: Modul 02 - Prompt tervezés →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Jogi nyilatkozat**:
Ez a dokumentum az AI fordítási szolgáltatás, a [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével készült. Bár a pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti, anyanyelvi dokumentum tekintendő hivatalos forrásnak. Kiemelten fontos információk esetén professzionális emberi fordítást javaslunk. Nem vállalunk felelősséget semmilyen félreértésért vagy téves értelmezésért, amely ebből a fordításból ered.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->