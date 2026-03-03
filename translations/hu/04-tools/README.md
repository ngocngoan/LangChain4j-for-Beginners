# 04-es modul: Eszközökkel rendelkező MI-ügynökök

## Tartalomjegyzék

- [Mit tanulsz](../../../04-tools)
- [Előfeltételek](../../../04-tools)
- [Eszközökkel rendelkező MI-ügynökök megértése](../../../04-tools)
- [Hogyan működik az eszközhívás](../../../04-tools)
  - [Eszközdefiníciók](../../../04-tools)
  - [Döntéshozatal](../../../04-tools)
  - [Végrehajtás](../../../04-tools)
  - [Válaszgenerálás](../../../04-tools)
  - [Architektúra: Spring Boot automatikus kötés](../../../04-tools)
- [Eszközláncolás](../../../04-tools)
- [Futtasd az alkalmazást](../../../04-tools)
- [Az alkalmazás használata](../../../04-tools)
  - [Próbálj ki egyszerű eszközhasználatot](../../../04-tools)
  - [Teszteld az eszközláncolást](../../../04-tools)
  - [Lásd a beszélgetés folyamatát](../../../04-tools)
  - [Kísérletezz különböző kérésekkel](../../../04-tools)
- [Kulcsfogalmak](../../../04-tools)
  - [ReAct minta (Érvelés és cselekvés)](../../../04-tools)
  - [Az eszközleírások számítanak](../../../04-tools)
  - [Munkamenet-kezelés](../../../04-tools)
  - [Hibakezelés](../../../04-tools)
- [Elérhető eszközök](../../../04-tools)
- [Mikor használjunk eszközalapú ügynököket](../../../04-tools)
- [Eszközök vs RAG](../../../04-tools)
- [Következő lépések](../../../04-tools)

## Mit tanulsz

Eddig megtanultad, hogyan lehet párbeszédet folytatni a MI-vel, hogyan strukturáld hatékonyan a promptokat, és hogyan alapozd meg a válaszokat dokumentumaidon. Azonban továbbra is van egy alapvető korlát: a nyelvi modellek csak szöveget tudnak generálni. Nem tudják ellenőrizni az időjárást, számításokat végezni, adatbázisokat lekérdezni vagy külső rendszerekkel kommunikálni.

Az eszközök ezt megváltoztatják. Azáltal, hogy a modell hozzáférést kap olyan funkciókhoz, amelyeket meghívhat, átalakítod egy szöveggenerátorból egy cselekvésre képes ügynökké. A modell eldönti, mikor van szüksége egy eszközre, melyik eszközt használja, és milyen paramétereket ad át. A te kódod végrehajtja a funkciót, és visszaadja az eredményt. A modell beépíti ezt az eredményt a válaszába.

## Előfeltételek

- Befejezte az [01-es modult – Bevezetés](../01-introduction/README.md) (Azure OpenAI erőforrások telepítve)
- Ajánlott a korábbi modulok elvégzése (ez a modul hivatkozik a [03-as modul RAG fogalmaira](../03-rag/README.md) az Eszközök vs RAG összehasonlításban)
- `.env` fájl a gyökérkönyvtárban az Azure hitelesítő adatokkal (a 01-es modulban az `azd up` hozta létre)

> **Megjegyzés:** Ha még nem végezted el az 01-es modult, először kövesd ott a telepítési utasításokat.

## Eszközökkel rendelkező MI-ügynökök megértése

> **📝 Megjegyzés:** A jelen modulban az "ügynökök" kifejezés olyan MI asszisztensekre utal, amelyek eszközhívási képességekkel bővültek. Ez eltér a **Agentic MI** mintáktól (önálló ügynökök tervezéssel, memóriával és többlépcsős érveléssel), melyeket a [05-ös modulban: MCP](../05-mcp/README.md) tárgyalunk majd.

Eszközök nélkül egy nyelvi modell csak a tanító adatából képes szöveget generálni. Ha megkérdezed az aktuális időjárást, csak tippelni tud. Ha eszközöket adsz neki, meghívhat egy időjárás-API-t, számításokat végezhet vagy lekérdezhet egy adatbázist — majd beépíti ezeket a valós eredményeket a válaszába.

<img src="../../../translated_images/hu/what-are-tools.724e468fc4de64da.webp" alt="Nélkülük és velük az eszközök" width="800"/>

*Eszközök nélkül a modell csak tippel — eszközökkel API-kat hívhat meg, számításokat végezhet és valós idejű adatokat adhat vissza.*

A eszközökkel rendelkező MI-ügynök egy **ReAct (Reasoning and Acting)** mintát követ. A modell nem csak válaszol — gondolkodik rajta, mire van szüksége, eszközt hív meg, megfigyeli az eredményt, majd eldönti, hogy újra cselekedjen vagy végleges választ adjon:

1. **Gondolkodás** — Az ügynök elemzi a felhasználó kérdését, és meghatározza, milyen információra van szüksége
2. **Cselekvés** — Az ügynök kiválasztja a megfelelő eszközt, generálja a helyes paramétereket, és meghívja azt
3. **Megfigyelés** — Az ügynök megkapja az eszköz kimenetét és értékeli az eredményt
4. **Ismétlés vagy válaszadás** — Ha több adat kell, az ügynök visszalép; különben összerak egy természetes nyelvű választ

<img src="../../../translated_images/hu/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct minta" width="800"/>

*A ReAct ciklus — az ügynök gondolkodik arról, mit tegyen, eszközt hív meg, megfigyeli az eredményt, és addig ismétel, amíg végső választ nem adhat.*

Ez automatikusan történik. Te definiálod az eszközöket és azok leírásait. A modell kezeli a döntéshozatalt arról, mikor és hogyan kell használni azokat.

## Hogyan működik az eszközhívás

### Eszközdefiníciók

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Jól definiált funkciókat hozol létre világos leírásokkal és paraméterspecifikációval. A modell látja ezeket a leírásokat a rendszer promptjában és megérti, hogy mit csinál az egyes eszköz.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Az időjárás lekérdezési logikád
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Az asszisztens automatikusan össze van kötve a Spring Boot által:
// - ChatModel bean
// - Minden @Tool metódus a @Component osztályokból
// - ChatMemoryProvider a munkamenet kezeléséhez
```

Az alábbi ábra lebontja az összes annotációt, és megmutatja, hogyan segíti mindegyik az MI-t abban, hogy mikor hívja az eszközt és milyen argumentumokat adjon:

<img src="../../../translated_images/hu/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Eszközdefiníciók anatómiája" width="800"/>

*Az eszközdefiníció anatómiája — az @Tool megmondja az MI-nek, mikor használja, az @P írja le minden paramétert, az @AiService pedig mindent beköt a rendszerindításkor.*

> **🤖 Próbáld ki [GitHub Copilot](https://github.com/features/copilot) Chattel:** Nyisd meg a [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) fájlt, és kérdezd:
> - "Hogyan integrálnék egy valós időjárás-API-t, mint az OpenWeatherMap a szimulált adatok helyett?"
> - "Mi tesz egy jó eszközleírást, amely segíti az MI-t a helyes használatban?"
> - "Hogyan kezeljem az API hibákat és a lekérések korlátozását az eszköz implementációban?"

### Döntéshozatal

Amikor a felhasználó megkérdezi, „Milyen az időjárás Seattle-ben?”, a modell nem véletlenszerűen választ eszközt. Összeveti a felhasználói szándékot az összes elérhető eszközleírással, pontozza őket relevancia szerint, és kiválasztja a legjobbat. Ezután generál egy strukturált függvényhívást a helyes paraméterekkel — ebben az esetben a `location` értékét `"Seattle"`-re állítja.

Ha egyetlen eszköz sem felel meg a kérésnek, a modell a saját tudásából válaszol. Több egyező eszköz esetén a legspecifikusabbat választja.

<img src="../../../translated_images/hu/decision-making.409cd562e5cecc49.webp" alt="Hogyan dönt az MI, melyik eszközt használja" width="800"/>

*A modell minden elérhető eszközt értékel a felhasználói szándék alapján, és kiválasztja a legjobbat — ezért fontos, hogy világos, specifikus eszközleírásokat írjunk.*

### Végrehajtás

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

A Spring Boot automatikusan beköti a deklaratív `@AiService` interfészt az összes regisztrált eszközzel, és a LangChain4j automatikusan végrehajtja az eszközhívásokat. A háttérben egy teljes eszközhívás hat lépésen keresztül zajlik — a felhasználó természetes nyelvű kérdésétől egészen a természetes nyelvű válaszig:

<img src="../../../translated_images/hu/tool-calling-flow.8601941b0ca041e6.webp" alt="Eszközhívás folyamata" width="800"/>

*Az end-to-end folyamat — a felhasználó kérdést tesz fel, a modell eszközt választ, a LangChain4j végrehajtja, és a modell beépíti az eredményt természetes válaszba.*

Ha futtattad a [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) példát a 00 modulban, már láttad ezt a mintát működés közben — ugyanúgy hívták a `Calculator` eszközöket is. Az alábbi szekvencia diagram pontosan megmutatja, mi történt a demó alatt:

<img src="../../../translated_images/hu/tool-calling-sequence.94802f406ca26278.webp" alt="Eszközhívás szekvencia diagram" width="800"/>

*Az eszközhívó ciklus a Gyors indulás demóból — az `AiServices` elküldi az üzenetedet és az eszköz sémákat az LLM-nek, az LLM válaszként egy függvényhívást ad, például `add(42, 58)`, a LangChain4j helyileg végrehajtja a `Calculator` metódust, majd visszatáplálja az eredményt a végső válaszhoz.*

> **🤖 Próbáld ki [GitHub Copilot](https://github.com/features/copilot) Chattel:** Nyisd meg a [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) fájlt, és kérdezd:
> - "Hogyan működik a ReAct minta, és miért hatékony az MI ügynökök számára?"
> - "Hogyan dönt az ügynök, melyik eszközt használja, és milyen sorrendben?"
> - "Mi történik, ha egy eszköz végrehajtás meghiúsul — hogyan kezeljem robosztusan a hibákat?"

### Válaszgenerálás

A modell megkapja az időjárási adatot és természetes nyelvű válaszba formázza a felhasználónak.

### Architektúra: Spring Boot automatikus kötés

Ez a modul a LangChain4j Spring Boot integrációját használja deklaratív `@AiService` interfészekkel. Induláskor a Spring Boot felfedezi az összes `@Component`-et, amelyek `@Tool` metódusokat tartalmaznak, a ChatModel bean-edet és a ChatMemoryProvider-t — majd mindezeket egyetlen `Assistant` interfészbe köti nullá boilerplate-el.

<img src="../../../translated_images/hu/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot automatikus kötés architektúra" width="800"/>

*Az @AiService interfész összeköti a ChatModelt, az eszköz komponenseket és a memória szolgáltatót — a Spring Boot mindent automatikusan beköt.*

Itt a teljes kérés életciklusa szekvencia diagramként — az HTTP kérelmtől át a controller-en, service-en és az automatikusan kötött proxy-n keresztül egészen az eszköz végrehajtásáig és vissza:

<img src="../../../translated_images/hu/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Spring Boot eszközhívás szekvencia" width="800"/>

*A teljes Spring Boot kérés életciklus — az HTTP kérés a controlleren és service-en keresztül halad a automatikusan kötött Assistant proxyig, amely automatikusan összehangolja az LLM-et és az eszközhívásokat.*

Ennek a megközelítésnek a kulcsfontosságú előnyei:

- **Spring Boot automatikus kötés** — A ChatModel és eszközök automatikusan befecskendezve
- **@MemoryId minta** — Automatikus munkamenet-alapú memória kezelés
- **Egyszeri példány** — Az Assistant egyszer létrejön és újrahasznosul jobb teljesítményért
- **Típusbiztos végrehajtás** — Java metódusok közvetlen hívása típuskonverzióval
- **Több lépéses koordináció** — Az eszközláncolást automatikusan kezeli
- **Zéró boilerplate** — Nem kell kézzel hívni `AiServices.builder()`-t vagy memória HashMap-et

Alternatív megközelítések (kézi `AiServices.builder()`) több kódot igényelnek és nélkülözik a Spring Boot integráció előnyeit.

## Eszközláncolás

**Eszközláncolás** — Az eszközalapú ügynökök valódi ereje akkor mutatkozik meg, ha egyetlen kérdés több eszközt igényel. Kérdezd meg: „Milyen az időjárás Seattle-ben Fahrenheitben?” és az ügynök automatikusan összefűz két eszközt: először meghívja a `getCurrentWeather`-t, hogy megkapja a Celsius hőmérsékletet, majd ezt az értéket átadja a `celsiusToFahrenheit`-nek a konverzióhoz — mindezt egyetlen beszélgetési fordulóban.

<img src="../../../translated_images/hu/tool-chaining-example.538203e73d09dd82.webp" alt="Eszközláncolás példa" width="800"/>

*Eszközláncolás működés közben — az ügynök először meghívja a getCurrentWeather-t, majd továbbadja a Celsius eredményt a celsiusToFahrenheit-nek, és összefűzött választ ad.*

**Jóindulatú hibakezelés** — Kérdezz időjárást olyan városra, ami nincs a szimulált adatok között. Az eszköz hibaüzenetet ad vissza, és az MI megmagyarázza, hogy nem tud segíteni ahelyett, hogy összeomlana. Az eszközök biztonságosan hibáznak. Az alábbi ábra összehasonlítja a két megközelítést — megfelelő hibakezeléssel az ügynök elkapja a kivételt és segítő választ ad, nélküle az egész alkalmazás összeomlik:

<img src="../../../translated_images/hu/error-handling-flow.9a330ffc8ee0475c.webp" alt="Hibakezelési folyamat" width="800"/>

*Ha egy eszköz hibázik, az ügynök elkapja a hibát, és segítő magyarázó választ ad összeomlás helyett.*

Ez egyetlen beszélgetési fordulóban történik. Az ügynök autonom módon koordinál több eszközhívást.

## Futtasd az alkalmazást

**Ellenőrizd a telepítést:**

Győződj meg arról, hogy a `.env` fájl létezik a gyökérkönyvtárban az Azure hitelesítő adatokkal (az 01-es modulban létrejött). Futtasd ezt a modul könyvtárából (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Meg kell jelenítenie az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Meg kell jeleníteni az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**Indítsd el az alkalmazást:**

> **Megjegyzés:** Ha már az összes alkalmazást elindítottad a `./start-all.sh`-lal a gyökérkönyvtárból (ahogy az 01-es modulban leírtuk), akkor ez a modul már fut a 8084-es porton. Az indító parancsokat alább kihagyhatod, és közvetlenül a http://localhost:8084 címre mehetsz.

**1. lehetőség: Spring Boot Dashboard használata (ajánlott VS Code felhasználóknak)**

A fejlesztői konténer tartalmazza a Spring Boot Dashboard kiegészítőt, amely egy vizuális felületet biztosít az összes Spring Boot alkalmazás kezeléséhez. A VS Code bal oldali Activity Bar-ján találod (keresd a Spring Boot ikont).

A Spring Boot Dashboard segítségével:
- Láthatod az összes elérhető Spring Boot alkalmazást a munkaterületen
- Egy kattintással indíthatod/leállíthatod az alkalmazásokat
- Valós időben nézheted az alkalmazás naplóit
- Figyelheted az alkalmazás állapotát

Egyszerűen kattints a "tools" melletti lejátszás gombra ennek a modulnak az indításához, vagy indítsd el egyszerre az összes modult.

Így néz ki a Spring Boot Dashboard VS Code-ban:

<img src="../../../translated_images/hu/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard VS Code-ban — indítsd, állítsd le és monitorozd az összes modult egy helyről*

**2. lehetőség: shell szkriptek használata**

Indítsd el az összes webalkalmazást (01–04 modulok):

**Bash:**
```bash
cd ..  # Gyökérkönyvtárból
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
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

Mindkét szkript automatikusan betölti a környezeti változókat a gyökér `.env` fájlból, és lefordítja a JAR fájlokat, ha azok nem léteznek.

> **Megjegyzés:** Ha inkább manuálisan szeretnéd lefordítani az összes modult a kezdés előtt:
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

Nyisd meg a böngésződben a http://localhost:8084 címet.

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

Az alkalmazás webes felületet biztosít, ahol egy AI ügynökkel léphetsz interakcióba, amely hozzáfér az időjárás- és hőmérséklet-konverziós eszközökhöz. Így néz ki a felület — tartalmaz gyorsindítási példákat és egy csevegőpanelt kérésküldéshez:

<a href="images/tools-homepage.png"><img src="../../../translated_images/hu/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Az AI Agent Tools felület — gyors példák és csevegőfelület az eszközökkel való interakcióhoz*

### Próbálj ki egyszerű eszközhasználatot

Kezdj egy egyszerű kéréssel: „Konvertáld át a 100 Fahrenheit fokot Celsiusra.” Az ügynök felismeri, hogy a hőmérséklet-konverziós eszközre van szükség, megfelelő paraméterekkel hívja meg, és visszaadja az eredményt. Észreveszed, milyen természetes érzés — nem kellett megadnod, melyik eszközt használd, vagy hogyan hívd meg.

### Teszteld az eszközláncolást

Most próbálj valami összetettebbet: „Milyen az időjárás Seattle-ben, és konvertáld át Fahrenheitre?” Nézd, hogyan dolgozik ezt fel az ügynök lépésről lépésre. Először lekéri az időjárást (ami Celsius fokban tér vissza), felismeri, hogy át kell konvertálni Fahrenheitre, meghívja az átalakító eszközt, majd egyesíti mindkét eredményt egy válaszban.

### Nézd meg a beszélgetés folyamatát

A csevegőfelület megőrzi a beszélgetés előzményeit, így többkörös interakciókat folytathatsz. Láthatod az összes korábbi kérdést és választ, ami megkönnyíti a párbeszéd követését és megértését, hogyan építi az ügynök a kontextust több váltás alatt.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/hu/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Többkörös beszélgetés egyszerű konverziókkal, időjáráslekérdezésekkel és eszközláncolással*

### Kísérletezz különböző kérésekkel

Próbálj ki különböző kombinációkat:
- Időjáráslekérdezések: „Milyen az időjárás Tokióban?”
- Hőmérséklet-konverziók: „Mi 25°C Kelvinben?”
- Összetett kérések: „Nézd meg Párizs időjárását, és mondd meg, hogy 20°C felett van-e”

Figyeld meg, hogyan értelmezi az ügynök a természetes nyelvet, és hogyan térképezi le azt megfelelő eszközhívásokra.

## Kulcsfogalmak

### ReAct minta (Érvelés és cselekvés)

Az ügynök váltogat az érvelés (döntés arról, mit tegyen) és a cselekvés (eszközök használata) között. Ez a minta lehetővé teszi az önálló problémamegoldást, nem csupán az utasításokra adott válaszokat.

### Az eszköz leírások számítanak

Az eszközleírások minősége közvetlenül befolyásolja, milyen jól használja azokat az ügynök. Az egyértelmű, specifikus leírások segítik a modellt, hogy mikor és hogyan hívjon meg egy-egy eszközt.

### Munkamenet-kezelés

az `@MemoryId` annotáció lehetővé teszi az automatikus munkamenet-alapú memória kezelését. Minden munkamenet-azonosítóhoz külön `ChatMemory` példány tartozik, amelyet a `ChatMemoryProvider` bean kezel, így több felhasználó egyszerre is interakcióba léphet az ügynökkel anélkül, hogy a beszélgetéseik összekeverednének. Az alábbi diagram mutatja, hogyan irányítják a különböző felhasználókat elkülönített memória tárolókhoz a munkamenet-azonosítóik alapján:

<img src="../../../translated_images/hu/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Minden munkamenet-azonosító egy elkülönített beszélgetési előzményt jelent — a felhasználók soha nem látják egymás üzeneteit.*

### Hibakezelés

Az eszközök hibázhatnak — az API-k időtúllépnek, a paraméterek lehetnek érvénytelenek, külső szolgáltatások leállhatnak. A produkciós ügynököknek szükségük van hibakezelésre, hogy a modell elmagyarázhassa a problémákat vagy alternatívákat próbálhasson meg, ahelyett, hogy az egész alkalmazás összeomlana. Ha egy eszköz kivételt dob, a LangChain4j elkapja azt és visszajuttatja a hibaüzenetet a modellnek, amely így természetes nyelven magyarázhatja el a problémát.

## Elérhető eszközök

Az alábbi diagram bemutatja azokat a széles körű eszközöket, amelyeket építhetsz. Ez a modul az időjárás- és hőmérséklet-eszközöket demonstrálja, de ugyanaz az `@Tool` minta működik bármilyen Java metódus esetén — legyen az adatbázis-lekérdezés vagy fizetési feldolgozás.

<img src="../../../translated_images/hu/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Bármelyik, @Tool annotációval ellátott Java metódus elérhetővé válik az AI számára — a minta kiterjed adatbázisokra, API-kra, e-mailekre, fájlműveletekre és még sok másra.*

## Mikor érdemes eszköz-alapú ügynököket használni

Nem minden kéréshez kellenek eszközök. A döntés attól függ, hogy az AI-nak szüksége van-e külső rendszerekkel való interakcióra, vagy saját tudásából válaszolhat. Az alábbi útmutató összefoglalja, mikor adnak értéket az eszközök, és mikor feleslegesek:

<img src="../../../translated_images/hu/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*Gyors döntési útmutató — az eszközök valós idejű adatokhoz, számításokhoz és műveletekhez valók; az általános ismeretek és kreatív feladatok nem igénylik őket.*

## Eszközök vs RAG

A 03 és 04 modulok mindketten bővítik az AI képességeit, de alapvetően eltérő módon. A RAG azzal bővíti a modellt, hogy elérést biztosít tudáshoz dokumentumokon keresztül. Az eszközök pedig a képességet adnak, hogy műveleteket hajtsanak végre függvényhívásokkal. Az alábbi diagram összehasonlítja ezt a két megközelítést — hogyan működik mindegyik munkafolyamata és milyen kompromisszumokat tartalmaznak:

<img src="../../../translated_images/hu/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*A RAG statikus dokumentumokból szerez információt — az Eszközök műveleteket hajtanak végre és dinamikus, valós idejű adatokat hoznak. Sok produkciós rendszer mindkettőt használja.*

Gyakorlatban sok produkciós rendszer mindkét megközelítést ötvözi: RAG-t a válaszok alapozásához a dokumentációdban, és Eszközöket a valós idejű adatok lekéréséhez vagy műveletek végrehajtásához.

## Következő lépések

**Következő modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigáció:** [← Előző: Modul 03 - RAG](../03-rag/README.md) | [Vissza a főoldalra](../README.md) | [Következő: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Jogi nyilatkozat**:  
Ez a dokumentum az AI fordítási szolgáltatás, a [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével készült. Bár a pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az anyanyelvén tekintendő hiteles forrásnak. Fontos információk esetén szakmai, emberi fordítást javaslunk. Nem vállalunk felelősséget az ebből a fordításból eredő félreértésekért vagy téves értelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->