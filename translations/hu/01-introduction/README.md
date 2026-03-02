# 01. modul: Kezdés a LangChain4j-vel

## Tartalomjegyzék

- [Videós bemutató](../../../01-introduction)
- [Mit fogsz megtanulni](../../../01-introduction)
- [Előfeltételek](../../../01-introduction)
- [A fő probléma megértése](../../../01-introduction)
- [A tokenek megértése](../../../01-introduction)
- [Hogyan működik a memória](../../../01-introduction)
- [Hogyan használja a LangChain4j-t](../../../01-introduction)
- [Azure OpenAI infrastruktúra telepítése](../../../01-introduction)
- [Az alkalmazás helyi futtatása](../../../01-introduction)
- [Az alkalmazás használata](../../../01-introduction)
  - [Állapot nélküli csevegés (bal oldali panel)](../../../01-introduction)
  - [Állapotfüggő csevegés (jobb oldali panel)](../../../01-introduction)
- [Következő lépések](../../../01-introduction)

## Videós bemutató

Nézd meg ezt az élő adást, amely elmagyarázza, hogyan kezdj neki ennek a modulnak:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Mit fogsz megtanulni

Ha elvégezted a gyors kezdést, láttad, hogyan küldhetsz parancsokat és hogyan kapsz válaszokat. Ez az alap, de a valódi alkalmazások ennél több kell. Ez a modul megtanít arra, hogyan építs olyan beszélgető AI-t, amely emlékszik a kontextusra és fenntartja az állapotot – ez a különbség egy egyszeri demó és egy termelésre kész alkalmazás között.

Az útmutató során az Azure OpenAI GPT-5.2 verzióját használjuk, mert fejlett érvelési képességei révén jobban láthatóvá teszi a különböző minták viselkedését. Amikor memóriát adsz hozzá, egyértelműen látni fogod a különbséget. Ez megkönnyíti megérteni, hogy mit ad minden komponens az alkalmazásodhoz.

Egy alkalmazást építesz, ami mindkét mintát bemutatja:

**Állapot nélküli csevegés** – Minden kérés független. A modell nem emlékszik az előző üzenetekre. Ez volt a minta, amit a gyors kezdés során használtál.

**Állapotfüggő beszélgetés** – Minden kérés tartalmazza a beszélgetés előzményeit. A modell fenntartja a kontextust több beszélgetési körön keresztül. Erre van szükség a termelési alkalmazásokban.

## Előfeltételek

- Azure előfizetés Azure OpenAI hozzáféréssel
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Megjegyzés:** Java, Maven, Azure CLI és Azure Developer CLI (azd) előre telepítve vannak a biztosított fejlesztői konténerben.

> **Megjegyzés:** Ez a modul az Azure OpenAI GPT-5.2 verzióját használja. A telepítés automatikusan konfigurálva van az `azd up` segítségével – ne módosítsd a modell nevét a kódban.

## A fő probléma megértése

A nyelvi modellek állapot nélküliak. Minden API hívás független. Ha azt küldöd, hogy "A nevem John", majd megkérdezed: "Mi a nevem?", a modell nem tudja, hogy épp most mutatkoztál be. Minden kérés első beszélgetésként kezeli.

Ez jó egyszerű kérdés-válasz szcenáriókhoz, de használhatatlan valódi alkalmazásokhoz. Az ügyfélszolgálati chatbotoknak emlékezniük kell arra, amit mondtál nekik. A személyi asszisztenseknek kontextus kell. Bármilyen többkörös beszélgetés memóriát igényel.

<img src="../../../translated_images/hu/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Az állapot nélküli (független hívások) és állapotfüggő (kontextus-érzékeny) beszélgetések közötti különbség*

## A tokenek megértése

Mielőtt belevágnánk a beszélgetésekbe, fontos megérteni a tokeneket – az alapvető szövegegységeket, amelyeket a nyelvi modellek feldolgoznak:

<img src="../../../translated_images/hu/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Példa arra, hogyan bontja a szöveget tokenekre a modell – "I love AI!" négy külön feldolgozási egységre lesz bontva*

A tokenek azok az egységek, amelyekkel az AI modellek mérik és feldolgozzák a szöveget. Szavak, írásjelek, sőt a szóközök is lehetnek tokenek. A modellednek van egy korlátja, hogy egyszerre hány tokent tud feldolgozni (a GPT-5.2 esetében 400 000, amelyből akár 272 000 lehet bemeneti, és 128 000 kimeneti token). A tokenek megértése segít kezelni a beszélgetések hosszát és a költségeket.

## Hogyan működik a memória

A chat memória megoldja az állapot nélküli problémát azzal, hogy fenntartja a beszélgetési előzményeket. Mielőtt elküldöd a kérést a modellnek, a keretrendszer az aktuális kérés elé helyezi az előző releváns üzeneteket. Amikor azt kérdezed, "Mi a nevem?", a rendszer valójában az egész beszélgetési előzményt küldi el, így a modell látja, hogy korábban azt mondtad: "A nevem John."

A LangChain4j memóriakezelő megvalósításokat biztosít, amelyek automatikusan kezelik ezt. Te választod meg, hány üzenetet szeretnél megtartani, a keretrendszer pedig kezeli a kontextus ablakot.

<img src="../../../translated_images/hu/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*A MessageWindowChatMemory egy mozgó ablakban tartja a legutóbbi üzeneteket, automatikusan törölve a régieket*

## Hogyan használja a LangChain4j-t

Ez a modul a gyors kezdést bővíti ki a Spring Boot integrációval és a beszélgetés memóriával. Így illeszkednek össze az elemek:

**Függőségek** – Adj hozzá két LangChain4j könyvtárat:

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

A builder a hitelesítő adatokat az `azd up` által beállított környezeti változókból olvassa be. A `baseUrl` beállítása az Azure végpontodra teszi az OpenAI kliens használatát az Azure OpenAI-vel kompatibilissé.

**Beszélgetés memória** – Kövesd a chat előzményeket MessageWindowChatMemory-vel ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

A memóriát a `withMaxMessages(10)` metódussal hozd létre, hogy az utolsó 10 üzenetet tartsa meg. A felhasználói és AI üzeneteket típusos csomagolókkal add hozzá: `UserMessage.from(text)` és `AiMessage.from(text)`. Előzményeket a `memory.messages()` segítségével szerezheted be és küldheted el a modellnek. A szolgáltatás minden beszélgetési azonosítóhoz külön memóriapéldányt tárol, így egyszerre több felhasználó is cseveghet.

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chat funkcióval:** Nyisd meg a [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) fájlt, és kérdezd meg:
> - "Hogyan dönt a MessageWindowChatMemory, hogy melyik üzenetet dobja el, amikor a memória ablak megtelt?"
> - "Megvalósíthatok-e egyedi memória tárolást adatbázis használatával in-memory helyett?"
> - "Hogyan adhatnék hozzá összefoglalást a régi beszélgetési előzmények tömörítéséhez?"

Az állapot nélküli chat végpont teljesen kihagyja a memóriát – csak `chatModel.chat(prompt)` hívás történik, mint a gyors kezdésnél. Az állapotfüggő végpont hozzáadja az üzeneteket a memóriához, lekéri az előzményeket, és belefoglalja a kontextust minden kérésbe. Ugyanaz a modell konfiguráció, más-más minták.

## Azure OpenAI infrastruktúra telepítése

**Bash:**
```bash
cd 01-introduction
azd up  # Válassza ki az előfizetést és a helyszínt (az eastus2 ajánlott)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Válassza ki az előfizetést és a helyszínt (az eastus2 ajánlott)
```

> **Megjegyzés:** Ha időtúllépési hibával találkozol (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), egyszerűen futtasd újra az `azd up` parancsot. Az Azure erőforrások még telepítés alatt állhatnak a háttérben, és az ismételt futtatás lehetővé teszi, hogy a telepítés befejeződjön, amikor az erőforrások elérnek egy végleges állapotot.

Ez a következőket végzi el:
1. Telepíti az Azure OpenAI erőforrást GPT-5.2 és text-embedding-3-small modellekkel
2. Automatikusan létrehozza a `.env` fájlt a projekt gyökérkönyvtárában a hitelesítő adatokkal
3. Beállítja az összes szükséges környezeti változót

**Telepítési problémáid vannak?** Nézd meg az [Infrastruktúra README](infra/README.md) fájlt a részletes hibakeresésért, beleértve az alkönyvtár névütközéseket, az Azure Portál kézi telepítési lépéseit, és a modell konfigurációs útmutatót.

**Ellenőrizd a sikeres telepítést:**

**Bash:**
```bash
cat ../.env  # Meg kell jelenítenie az AZURE_OPENAI_ENDPOINT, API_KEY stb. értékeket.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Meg kell jelenítenie az AZURE_OPENAI_ENDPOINT, API_KEY stb. értékeket.
```

> **Megjegyzés:** Az `azd up` parancs automatikusan létrehozza a `.env` fájlt. Ha később frissíteni szeretnéd, manuálisan szerkesztheted a `.env` fájlt, vagy újra létrehozhatod a futtatásával:
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

**Ellenőrizd a telepítést:**

Győződj meg róla, hogy a `.env` fájl létezik a gyökérkönyvtárban az Azure hitelesítő adatokkal:

**Bash:**
```bash
cat ../.env  # Meg kell jeleníteni az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Meg kell jeleníteni az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**Indítsd el az alkalmazásokat:**

**1. Opció: Spring Boot Dashboard használata (ajánlott VS Code felhasználóknak)**

A fejlesztői konténer tartalmazza a Spring Boot Dashboard kiterjesztést, amely vizuális felületet biztosít az összes Spring Boot alkalmazás kezeléséhez. Megtalálod a VS Code bal oldalán az Activity Bar-ban (keresd a Spring Boot ikont).

A Spring Boot Dashboardból:
- Megnézheted az összes elérhető Spring Boot alkalmazást a munkaterületen
- Egy klikkel indíthatod vagy állíthatod le az alkalmazásokat
- Élőben megtekintheted az alkalmazás naplóit
- Figyelemmel kísérheted az alkalmazás állapotát

Egyszerűen kattints a lejátszás gombra az "introduction" nevű modulnál, hogy elindítsd ezt a modult, vagy indítsd el egyszerre az összes modult.

<img src="../../../translated_images/hu/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**2. Opció: Shell script-ek használata**

Indítsd el az összes webalkalmazást (01–04 modulok):

**Bash:**
```bash
cd ..  # A gyökérkönyvtárból
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # A gyökér könyvtárból
.\start-all.ps1
```

Vagy csak ezt a modult indítsd el:

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

Mindkét script automatikusan betölti a gyökérkönyvtári `.env` környezeti változókat, és lefordítja a JAR fájlokat, ha még nem léteznek.

> **Megjegyzés:** Ha inkább manuálisan fordítanád le az összes modult indítás előtt:
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

Az alkalmazás webes felületet biztosít két párhuzamos csevegés megvalósítással.

<img src="../../../translated_images/hu/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Vezérlőpult mind a Egyszerű Csevegés (állapot nélküli), mind a Beszélgető Csevegés (állapotfüggő) opciókkal*

### Állapot nélküli csevegés (bal oldali panel)

Ezzel kezdd. Írd be, hogy "A nevem John", majd azonnal kérdezd meg, "Mi a nevem?" A modell nem fog emlékezni, mert minden üzenet független. Ez bemutatja az egyszerű nyelvi modell integráció alapvető problémáját – nincs beszélgetési kontextus.

<img src="../../../translated_images/hu/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*Az AI nem emlékszik a nevedre az előző üzenetből*

### Állapotfüggő csevegés (jobb oldali panel)

Próbáld ki itt ugyanezt a sorrendet. Írd be, hogy "A nevem John", majd kérdezd meg, "Mi a nevem?" Most már emlékszik. A különbség a MessageWindowChatMemory – ez fenntartja a beszélgetési előzményeket és minden kéréshez mellékeli azokat. Így működnek a termelési beszélgető AI rendszerek.

<img src="../../../translated_images/hu/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*Az AI emlékszik a nevedre a beszélgetés korábbi részéből*

Mindkét panel ugyanazt a GPT-5.2 modellt használja. Az egyetlen különbség a memória. Ez világossá teszi, mit ad a memória az alkalmazásodnak és miért elengedhetetlen valódi használati esetekhez.

## Következő lépések

**Következő modul:** [02-prompt-engineering - Prompt Engineering GPT-5.2-vel](../02-prompt-engineering/README.md)

---

**Navigáció:** [← Előző: 00. modul - Gyors kezdés](../00-quick-start/README.md) | [Vissza a főoldalra](../README.md) | [Következő: 02. modul - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Nyilatkozat**:
Ezt a dokumentumot az AI fordító szolgáltatás [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével fordítottuk le. Bár igyekszünk a pontosságra, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az anyanyelvén tekintendő hivatalos forrásnak. Fontos információk esetén javasolt szakmai emberi fordítást igénybe venni. Nem vállalunk felelősséget az e fordítás használatából eredő félreértésekért vagy helytelen értelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->