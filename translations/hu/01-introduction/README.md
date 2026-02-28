# Modul 01: Kezdés a LangChain4j-vel

## Tartalomjegyzék

- [Videós bemutató](../../../01-introduction)
- [Mit fogsz tanulni](../../../01-introduction)
- [Előfeltételek](../../../01-introduction)
- [Az alapvető probléma megértése](../../../01-introduction)
- [A tokenek megértése](../../../01-introduction)
- [Hogyan működik a memória](../../../01-introduction)
- [Hogyan használja ezt a LangChain4j](../../../01-introduction)
- [Azure OpenAI infrastruktúra telepítése](../../../01-introduction)
- [Alkalmazás futtatása helyben](../../../01-introduction)
- [Az alkalmazás használata](../../../01-introduction)
  - [Állapot nélküli chat (bal panel)](../../../01-introduction)
  - [Állapotfüggő chat (jobb panel)](../../../01-introduction)
- [Következő lépések](../../../01-introduction)

## Videós bemutató

Nézd meg ezt az élő bemutatót, amely elmagyarázza, hogyan kezdj neki ennek a modulnak: [Getting Started with LangChain4j - Live Session](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Mit fogsz tanulni

Ha befejezted a gyors kezdést, láthattad, hogyan küldhetsz promptokat és hogyan kapsz választ. Ez az alap, de a valódi alkalmazások ennél többre van szükségük. Ebben a modulban megtanulod, hogyan építs olyan csevegő AI-t, amely emlékszik a kontextusra és fenntartja az állapotot – ez a különbség egy egyszeri demo és egy éles környezetre kész alkalmazás között.

Az útmutató során végig az Azure OpenAI GPT-5.2-t használjuk, mert fejlett érvelési képességei világossá teszik a különböző minták viselkedését. Amikor memóriát adsz hozzá, egyértelműen látod a különbséget. Ez megkönnyíti megérteni, hogy az egyes komponensek mit hoznak az alkalmazásodhoz.

Egyetlen alkalmazást fogsz építeni, amely mindkét mintát demonstrálja:

**Állapot nélküli chat** – Minden kérés független. A modell nem emlékszik az előző üzenetekre. Ez az a minta, amit a gyors kezdésben használtál.

**Állapotfüggő beszélgetés** – Minden kérés tartalmazza az előzményeket. A modell fenntartja a kontextust több körön át. Ezt igénylik az éles alkalmazások.

## Előfeltételek

- Azure előfizetés Azure OpenAI hozzáféréssel
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Megjegyzés:** Java, Maven, Azure CLI és az Azure Developer CLI (azd) előre telepítve van a biztosított fejlesztői konténerben.

> **Megjegyzés:** Ez a modul GPT-5.2-t használ Azure OpenAI-n. A telepítés automatikusan konfigurálódik az `azd up` parancs által – ne módosítsd a modell nevét a kódban.

## Az alapvető probléma megértése

A nyelvi modellek állapot nélküli működésűek. Minden API-hívás független. Ha elküldöd, hogy „A nevem John”, majd megkérdezed, „Mi a nevem?”, a modellnek fogalma sincs arról, hogy éppen bemutatkoztál. Úgy kezeli minden kérést, mintha ez lenne az első beszélgetésed.

Ez egyszerű kérdések-válaszok esetén rendben van, de a valódi alkalmazásokhoz használhatatlan. Az ügyfélszolgálati chatbotoknak emlékezniük kell arra, amit mondtál nekik. A személyi asszisztenseknek szükségük van kontextusra. Bármely többszöri beszélgetés memóriát igényel.

<img src="../../../translated_images/hu/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Állapot nélküli kontra állapotfüggő beszélgetések" width="800"/>

*Az állapot nélküli (független hívások) és állapotfüggő (kontextusérzékeny) beszélgetések közötti különbség*

## A tokenek megértése

Mielőtt belemerülnénk a beszélgetésekbe, fontos megérteni a tokeneket – a szöveg alapegységeit, amelyeket a nyelvi modellek feldolgoznak:

<img src="../../../translated_images/hu/token-explanation.c39760d8ec650181.webp" alt="Token magyarázat" width="800"/>

*Példa arra, hogyan törik szét a szöveg tokenekre – az „I love AI!” 4 különálló feldolgozási egységgé válik*

A tokenek segítségével mérik és dolgozzák fel a szöveget az AI modellek. Szavak, írásjelek és akár szóközök is lehetnek tokenek. A modellednek van egy korlátja, hogy egyszerre hány tokent tud feldolgozni (GPT-5.2 esetén 400 000, ebből maximum 272 000 bemeneti és 128 000 kimeneti token). A tokenek megértése segít kezelni a beszélgetések hosszát és a költségeket.

## Hogyan működik a memória

A chat memória megoldja az állapot nélküli problémát azáltal, hogy fenntartja a beszélgetés előzményeit. Mielőtt elküldenéd a kérelmet a modellnek, a keretrendszer hozzáfűzi a releváns korábbi üzeneteket. Amikor megkérdezed, hogy „Mi a nevem?”, a rendszer valójában az egész beszélgetési előzményt elküldi, lehetővé téve, hogy a modell lássa: korábban azt mondtad, „A nevem John.”

A LangChain4j memória megvalósításokat biztosít, amelyek ezt automatikusan kezelik. Te választod ki, hogy hány üzenetet akarsz megőrizni, a keretrendszer pedig kezeli a kontextusablakot.

<img src="../../../translated_images/hu/memory-window.bbe67f597eadabb3.webp" alt="Memória ablak koncepció" width="800"/>

*MessageWindowChatMemory egy gördülő ablakot tart fenn a legutóbbi üzenetekből, automatikusan eltávolítva a régieket*

## Hogyan használja ezt a LangChain4j

Ez a modul kiterjeszti a gyors kezdést a Spring Boot integrációjával és a beszélgetés memóriával. Így illeszkednek össze az elemek:

**Függőségek** – Két LangChain4j könyvtárat adj hozzá:

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

**Chat modell** – Azure OpenAI konfigurálása Spring bean-ként ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

A builder a hitelesítő adatokat az `azd up` által beállított környezeti változókból olvassa. A `baseUrl` beállítása az Azure végpontra teszi az OpenAI klienst Azure OpenAI-kompatibilissé.

**Beszélgetés memória** – Beszélgetési előzmények követése a MessageWindowChatMemory-vel ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Memóriát hozol létre a `withMaxMessages(10)`-zel, hogy az utolsó 10 üzenetet tartsd meg. Felhasználói és AI üzeneteket típusos wrapperrel adhatsz hozzá: `UserMessage.from(text)` és `AiMessage.from(text)`. Az előzményeket `memory.messages()`-sel lekérheted, és elküldheted a modellnek. A szolgáltatás külön memória példányokat tárol beszélgetési azonosítónként, így több felhasználó is párhuzamosan cseveghet.

> **🤖 Próbáld ki a [GitHub Copilottal](https://github.com/features/copilot) Chat-en:** Nyisd meg a [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) fájlt, és kérdezd meg:
> - „Hogyan dönt a MessageWindowChatMemory, hogy mely üzeneteket dobja el, amikor a memória ablak megtelik?”
> - „Megvalósíthatok egyedi memória tárolást adatbázis használatával memóriabeli helyett?”
> - „Hogyan adhatnék hozzá összegzést a régi beszélgetési előzmények tömörítéséhez?”

Az állapot nélküli chat végpont teljesen kihagyja a memóriát – csak `chatModel.chat(prompt)` hívás, mint a gyors kezdésben. Az állapotfüggő végpont hozzáadja az üzeneteket a memóriához, lekéri az előzményeket, és ezen kontextust mindegyik kéréshez belefoglalja. Ugyanaz a modell konfiguráció, más minták.

## Azure OpenAI infrastruktúra telepítése

**Bash:**
```bash
cd 01-introduction
azd up  # Válassza ki az előfizetést és a helyszínt (az eastus2 ajánlott)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Válassza ki az előfizetést és a helyet (eastus2 ajánlott)
```

> **Megjegyzés:** Ha időtúllépési hibát tapasztalsz (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), egyszerűen futtasd az `azd up` parancsot újra. Az Azure erőforrások még háttérben települhetnek, az ismétlés lehetővé teszi, hogy a telepítés befejeződjön, amikor az erőforrások elérik a végleges állapotot.

Ez:
1. Telepíti az Azure OpenAI erőforrást GPT-5.2 és text-embedding-3-small modellekkel
2. Automatikusan generál `.env` fájlt a projekt gyökérkönyvtárába a hitelesítő adatokkal
3. Beállít minden szükséges környezeti változót

**Telepítési problémád van?** Nézd meg az [infrastruktúra README](infra/README.md) fájlt részletes hibakezelésért, beleértve az aldomain névütközéseket, manuális Azure Portál telepítési lépéseket és modellkonfigurációs útmutatót.

**Ellenőrizd, hogy sikerült-e a telepítés:**

**Bash:**
```bash
cat ../.env  # Meg kell jelenítenie az AZURE_OPENAI_ENDPOINT, API_KEY, stb. értékeket.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Meg kell jelenítenie az AZURE_OPENAI_ENDPOINT, API_KEY, stb. értékeket.
```

> **Megjegyzés:** Az `azd up` parancs automatikusan generálja a `.env` fájlt. Ha később frissíteni szeretnéd, vagy manuálisan szerkesztheted a `.env`-t, vagy újragenerálhatod a következőkkel:
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

## Alkalmazás futtatása helyben

**Ellenőrizd a telepítést:**

Győződj meg róla, hogy a `.env` fájl létezik a gyökérkönyvtárban, és tartalmazza az Azure hitelesítő adatokat:

**Bash:**
```bash
cat ../.env  # Meg kell jelenítenie az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Meg kell jeleníteni az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**Indítsd el az alkalmazásokat:**

**1. lehetőség: Spring Boot Dashboard használata (ajánlott VS Code felhasználóknak)**

A fejlesztői konténer tartalmazza a Spring Boot Dashboard kiterjesztést, amely vizuális felületet biztosít az összes Spring Boot alkalmazás kezeléséhez. A VS Code bal oldali tevékenységsávjában találod (keresd a Spring Boot ikont).

A Spring Boot Dashboard segítségével:
- Láthatod az összes elérhető Spring Boot alkalmazást a munkaterületen
- Egy kattintással indíthatsz/leállíthatsz alkalmazásokat
- Élőben nézheted az alkalmazás naplóit
- Figyelemmel kísérheted az alkalmazás állapotát

Egyszerűen kattints a „play” gombra az „introduction” mellett ennek a modulnak az elindításához, vagy indítsd el az összes modult egyszerre.

<img src="../../../translated_images/hu/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**2. lehetőség: Shell script-ek használata**

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

Vagy csak ezt a modult:

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

Mindkét script automatikusan betölti a környezeti változókat a gyökér `.env` fájljából, és elkészíti a JAR fájlokat, ha még nem léteznek.

> **Megjegyzés:** Ha inkább manuálisan akarod megépíteni az összes modult indulás előtt:
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

Nyisd meg böngészőben a http://localhost:8080 címet.

**Leállítás:**

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

Az alkalmazás webes felületet biztosít két párhuzamos chat megvalósítással.

<img src="../../../translated_images/hu/home-screen.121a03206ab910c0.webp" alt="Alkalmazás főképernyője" width="800"/>

*Irányítópult a Simpla Chat (állapot nélküli) és a Beszélgetés alapú Chat (állapotfüggő) opciókkal*

### Állapot nélküli chat (bal panel)

Először próbáld ki ezt. Írd be, hogy „A nevem John”, majd azonnal kérdezd meg, „Mi a nevem?” A modell nem fog emlékezni, mert minden üzenet független. Ez demonstrálja az alapvető nyelvi modell integrációk problémáját – nincs beszélgetési kontextus.

<img src="../../../translated_images/hu/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Állapot nélküli chat demó" width="800"/>

*Az AI nem emlékszik az előző üzenetből a nevedre*

### Állapotfüggő chat (jobb panel)

Most próbáld meg ugyanazt itt. Írd be, hogy „A nevem John”, majd „Mi a nevem?” Most emlékszik. A különbség a MessageWindowChatMemory – fenntartja a beszélgetési előzményeket, és ehhez minden kéréshez hozzáadja azokat. Így működik az éles környezetben használt beszélgető AI.

<img src="../../../translated_images/hu/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Állapotfüggő chat demó" width="800"/>

*Az AI emlékszik a beszélgetés korábbi pontján mondott nevedre*

Mindkét panel ugyanazt a GPT-5.2 modellt használja. Az egyetlen különbség a memória. Ez világossá teszi, hogy mit ad hozzá az alkalmazásodhoz a memória, és miért elengedhetetlen a valódi felhasználási esetekhez.

## Következő lépések

**Következő modul:** [02-prompt-engineering - Prompt tervezés GPT-5.2-vel](../02-prompt-engineering/README.md)

---

**Navigáció:** [← Előző: Modul 00 - Gyors kezdés](../00-quick-start/README.md) | [Vissza a főoldalra](../README.md) | [Következő: Modul 02 - Prompt tervezés →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Jogi nyilatkozat**:  
Ez a dokumentum az AI fordító szolgáltatás, a [Co-op Translator](https://github.com/Azure/co-op-translator) használatával készült. Bár a pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum, az anyanyelvi változat tekintendő hivatalos forrásnak. Fontos információk esetén professzionális emberi fordítást javaslunk. Nem vállalunk felelősséget a fordítás használatából eredő félreértésekért vagy téves értelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->