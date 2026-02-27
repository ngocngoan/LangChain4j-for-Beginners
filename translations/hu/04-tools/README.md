# 04. modul: AI ügynökök eszközökkel

## Tartalomjegyzék

- [Mit fogsz megtanulni](../../../04-tools)
- [Előfeltételek](../../../04-tools)
- [Az AI ügynökök megértése eszközökkel](../../../04-tools)
- [Hogyan működik az eszközhívás](../../../04-tools)
  - [Eszközdefiníciók](../../../04-tools)
  - [Döntéshozatal](../../../04-tools)
  - [Végrehajtás](../../../04-tools)
  - [Válaszgenerálás](../../../04-tools)
  - [Architektúra: Spring Boot automatikus bekötés](../../../04-tools)
- [Eszközláncolás](../../../04-tools)
- [Az alkalmazás futtatása](../../../04-tools)
- [Az alkalmazás használata](../../../04-tools)
  - [Egyszerű eszközhasználat kipróbálása](../../../04-tools)
  - [Eszközláncolás tesztelése](../../../04-tools)
  - [Beszélgetés menetének megtekintése](../../../04-tools)
  - [Különböző kérések tesztelése](../../../04-tools)
- [Kulcsfogalmak](../../../04-tools)
  - [ReAct minta (Gondolkodás és cselekvés)](../../../04-tools)
  - [Az eszközleírások számítanak](../../../04-tools)
  - [Munkamenet-kezelés](../../../04-tools)
  - [Hibakezelés](../../../04-tools)
- [Elérhető eszközök](../../../04-tools)
- [Mikor használjunk eszközalapú ügynököket](../../../04-tools)
- [Eszközök vs RAG](../../../04-tools)
- [Következő lépések](../../../04-tools)

## Mit fogsz megtanulni

Eddig megtanultad, hogyan folytass párbeszédet az AI-val, hogyan alakítsd hatékonyan a promptokat, és hogyan alapozd válaszaidat dokumentumaidra. De van egy alapvető korlát: a nyelvi modellek csak szöveget tudnak generálni. Nem tudják lekérdezni az időjárást, számításokat végezni, adatbázisokat lekérdezni vagy külső rendszerekkel kommunikálni.

Az eszközök ezt megváltoztatják. Ha a modell hozzáférést kap olyan funkciókhoz, amelyeket hívhat, átalakul szöveggenerátorból egy olyan ügynökké, aki cselekedni tud. A modell dönt arról, mikor van szüksége eszközre, melyik eszközt használja, és milyen paramétereket ad át. Az általad írt kód végrehajtja a funkciót és visszaadja az eredményt, amelyet a modell belefoglal a válaszába.

## Előfeltételek

- A 01-es modul befejezve (Azure OpenAI erőforrások telepítve)
- `.env` fájl a gyökérkönyvtárban Azure hitelesítő adatokat tartalmazva (a 01-es modul `azd up` paranccsal létrehozva)

> **Megjegyzés:** Ha nem végezted el az 01-es modult, először kövesd ott a telepítési útmutatót.

## Az AI ügynökök megértése eszközökkel

> **📝 Megjegyzés:** Ebben a modulban az „ügynökök” kifejezés olyan AI asszisztenseket jelent, amelyek eszközhívási képességekkel lettek kibővítve. Ez eltér a [05-ös modulban](../05-mcp/README.md) bemutatott **Agentic AI** mintáktól (önálló ügynökök tervezéssel, memóriával és többlépéses érveléssel).

Eszközök nélkül a nyelvi modell csak a tanulási adataiból képes szöveget generálni. Ha megkérdezed tőle az aktuális időjárást, tippelnie kell. Ha eszközök állnak rendelkezésére, képes lehet egy időjárás-API hívására, számítások elvégzésére vagy adatbázis lekérdezésére – és az így kapott valós adatokat beépíteni a válaszába.

<img src="../../../translated_images/hu/what-are-tools.724e468fc4de64da.webp" alt="Eszközök nélkül vs eszközökkel" width="800"/>

*Eszközök nélkül a modell csak tippel – eszközökkel API-kat hívhat, számításokat végezhet, és valós idejű adatokat ad vissza.*

Az eszközökkel rendelkező AI ügynök a **ReAct (Reasoning and Acting)** mintát követi. A modell nem csak válaszol – átgondolja, mire van szüksége, cselekszik egy eszköz hívásával, megfigyeli az eredményt, majd eldönti, hogy újra cselekszik vagy elküldi a végleges választ:

1. **Érvelés** — Az ügynök elemzi a felhasználó kérdését és meghatározza, milyen információra van szüksége
2. **Cselekvés** — Az ügynök kiválasztja a megfelelő eszközt, előállítja a helyes paramétereket, majd meghívja azt
3. **Megfigyelés** — Az ügynök megkapja az eszköz eredményét és értékeli azt
4. **Ismétlés vagy válaszadás** — Ha további adatra van szükség, az ügynök visszatér az előző lépésekhez; különben megfogalmaz egy természetes nyelvű választ

<img src="../../../translated_images/hu/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct minta" width="800"/>

*A ReAct ciklus – az ügynök megfontolja, mit tegyen, eszközt hív, megfigyeli az eredményt és ismétel, amíg képes a végső válasz megadására.*

Ez automatikusan történik. Te definiálod az eszközöket és azok leírásait. A modell dönti el, mikor és hogyan használja őket.

## Hogyan működik az eszközhívás

### Eszközdefiníciók

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Funkciókat definiálsz világos leírásokkal és paraméter specifikációkkal. A modell ezeket a leírásokat látja a rendszerpromptban, és érti, hogy mit csinál az adott eszköz.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Az időjárás lekérdezés logikája
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Az asszisztens automatikusan össze van kötve a Spring Boot által a következőkkel:
// - ChatModel bean
// - Minden @Tool metódus az @Component osztályokból
// - ChatMemoryProvider a munkamenet kezeléséhez
```
  
Az alábbi diagram minden annotációt lebont, és megmutatja, hogyan segít minden elem az AI-nak megérteni, mikor kell meghívni az eszközt és milyen argumentumokat kell átadni:

<img src="../../../translated_images/hu/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Az eszközdefiníciók anatómiája" width="800"/>

*Egy eszközdefiníció anatómiája – az @Tool megmondja az AI-nak, mikor használja, az @P leírja minden paramétert, az @AiService pedig az induláskor összeköti az egészet.*

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chattel:** Nyisd meg a [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) fájlt, és kérdezd meg:  
> - "Hogyan integrálnék egy valódi időjárás API-t, például az OpenWeatherMap-et a mock adatok helyett?"  
> - "Mi alkot egy jó eszközleírást, ami segíti az AI-t helyesen használni az eszközt?"  
> - "Hogyan kezelem az API hibákat és a ráta korlátokat az eszköz megvalósításaidban?"

### Döntéshozatal

Amikor a felhasználó megkérdezi, hogy „Milyen az időjárás Seattle-ben?”, a modell nem véletlenszerűen választ eszközt. Összeveti a felhasználó szándékát minden elérhető eszközleírással, pontozza relevancia alapján, és kiválasztja a legmegfelelőbbet. Majd strukturált funkcióhívást generál a helyes paraméterekkel – ebben az esetben `location` = `"Seattle"`.

Ha egyetlen eszköz sem egyezik a felhasználói kérésre, a modell a saját tudásából próbál válaszolni. Ha több eszköz is megfelel, a legspecifikusabbat választja.

<img src="../../../translated_images/hu/decision-making.409cd562e5cecc49.webp" alt="Hogyan dönt az AI, melyik eszközt használja" width="800"/>

*A modell minden elérhető eszközt értékel a felhasználó szándékához képest, és kiválasztja a legmegfelelőbbet – ezért fontos, hogy az eszközleírások világosak és specifikusak legyenek.*

### Végrehajtás

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

A Spring Boot automatikusan beköti a deklaratív `@AiService` interfészt az összes regisztrált eszközhöz, és a LangChain4j automatikusan végrehajtja az eszközhívásokat. A háttérben egy teljes eszközhívás hat szakaszon keresztül fut le – a felhasználó természetes nyelvű kérdésétől a természetes nyelvű válaszig:

<img src="../../../translated_images/hu/tool-calling-flow.8601941b0ca041e6.webp" alt="Eszközhívás folyamata" width="800"/>

*Teljes áramlás – a felhasználó kérdez, a modell eszközt választ, a LangChain4j végrehajtja, a modell pedig beépíti az eredményt a válaszba.*

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chattel:** Nyisd meg a [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) fájlt, és kérdezd meg:  
> - "Hogyan működik a ReAct minta és miért hatékony AI ügynökök számára?"  
> - "Hogyan dönt az ügynök, melyik eszközt milyen sorrendben használja?"  
> - "Mi történik, ha egy eszköz végrehajtása meghiúsul – hogyan kezeljem robusztusan a hibákat?"

### Válaszgenerálás

A modell megkapja az időjárásadatokat, és természetes nyelven megfogalmazza a választ a felhasználó számára.

### Architektúra: Spring Boot automatikus bekötés

Ez a modul a LangChain4j Spring Boot integrációját használja deklaratív `@AiService` interfészekkel. Induláskor a Spring Boot felfedezi az összes `@Component` osztályt, amelyek tartalmaznak `@Tool` metódusokat, a `ChatModel` bean-edet, és a `ChatMemoryProvider`-t – ezekből egyetlen `Assistant` interfészt köt össze nulla boilerplate kóddal.

<img src="../../../translated_images/hu/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot automatikus bekötési architektúra" width="800"/>

*Az @AiService interfész összeköti a ChatModelt, az eszközkomponenseket és a memória szolgáltatót – a Spring Boot kezeli az összes bekötést automatikusan.*

A megközelítés fő előnyei:

- **Spring Boot automatikus bekötés** — ChatModel és eszközök automatikus injektálása  
- **@MemoryId minta** — Automatikus munkamenetalapú memória kezelése  
- **Egyetlen példány** — Az asszisztens egyszer jön létre, újrafelhasználható a jobb teljesítményért  
- **Típusbiztos végrehajtás** — Java metódusok közvetlen hívása típuskonverzióval  
- **Több körös koordináció** — Az eszközláncolást automatikusan kezeli  
- **Nincs boilerplate** — Nincs manuális `AiServices.builder()` hívás vagy memória HashMap

Alternatív megoldások (kézi `AiServices.builder()`) több kódot igényelnek és nem élvezik a Spring Boot integráció előnyeit.

## Eszközláncolás

**Eszközláncolás** — az eszközalapú ügynökök valódi ereje akkor mutatkozik meg, amikor egy kérdés több eszközt igényel. Ha megkérdezed: „Milyen az időjárás Seattle-ben Fahrenheitben?”, az ügynök automatikusan összefűz két eszközt: először meghívja a `getCurrentWeather`-t a Celsius hőmérséklethez, majd ezt átadja a `celsiusToFahrenheit`-nek az átváltáshoz – mindez egy beszélgetési körben.

<img src="../../../translated_images/hu/tool-chaining-example.538203e73d09dd82.webp" alt="Eszközláncolás példája" width="800"/>

*Eszközláncolás működés közben – az ügynök először a getCurrentWeather-t hívja, majd a Celsius eredményt bemeneti adatként használja a celsiusToFahrenheit számára, és egy kombinált választ ad.*

Az alkalmazásban így néz ki – az ügynök két eszközhívást láncol össze egy beszélgetési körben:

<a href="images/tool-chaining.png"><img src="../../../translated_images/hu/tool-chaining.3b25af01967d6f7b.webp" alt="Eszközláncolás" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Valós alkalmazás kimenet – az ügynök automatikusan láncolja a getCurrentWeather → celsiusToFahrenheit hívásokat egy körben.*

**Kegyes hibakezelés** — Ha olyan város időjárását kéred le, ami nincs a mock adatok között, az eszköz hibát ad vissza, és az AI elmagyarázza, hogy nem tud segíteni ahelyett, hogy összeomlana. Az eszközök biztonságosan hibáznak.

<img src="../../../translated_images/hu/error-handling-flow.9a330ffc8ee0475c.webp" alt="Hibakezelési folyamat" width="800"/>

*Ha egy eszköz meghibásodik, az ügynök elkapja a hibát és segítőkész magyarázatot ad ahelyett, hogy összeomlana.*

Ez egyetlen beszélgetési körben történik. Az ügynök önállóan koordinál több eszköz hívást.

## Az alkalmazás futtatása

**Telepítés ellenőrzése:**

Ellenőrizd, hogy a `.env` fájl létezik-e a gyökérkönyvtárban Azure hitelesítő adatokkal (a 01-es modul során létrejött):  
```bash
cat ../.env  # Meg kell jeleníteni az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```
  
**Indítsd el az alkalmazást:**

> **Megjegyzés:** Ha már az összes alkalmazást elindítottad a 01-es modulból a `./start-all.sh`-lal, akkor ez a modul már fut a 8084-es porton. A lenti indító parancsokat kihagyhatod és közvetlenül a http://localhost:8084 címen nyithatod meg.

**1. lehetőség: Spring Boot Dashboard használata (VS Code felhasználóknak ajánlott)**

A fejlesztői konténer tartalmazza a Spring Boot Dashboard bővítményt, ami vizuális felületet biztosít az összes Spring Boot alkalmazás kezelésére. A VS Code Activity Bar bal oldalán találod (keresd a Spring Boot ikont).

A Spring Boot Dashboardból megteheted:  
- Az összes elérhető Spring Boot alkalmazás megtekintése a munkaterületen  
- Alkalmazások indítása/leállítása egy kattintással  
- Valós idejű naplók megtekintése  
- Alkalmazás állapotának figyelése

Egyszerűen kattints a "tools" melletti lejátszás gombra az indításhoz, vagy indítsd el egyszerre az összes modult.

<img src="../../../translated_images/hu/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

**2. lehetőség: shell script használata**

Indítsd el az összes webalkalmazást (01-04 modulok):

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
  
Vagy csak ezt a modult:

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
  
Mindkét script automatikusan tölti be a környezeti változókat a gyökér `.env` fájlból, és buildeli a JAR fájlokat, ha azok még nem léteznek.

> **Megjegyzés:** Ha manuálisan szeretnéd buildelni az összes modult indulás előtt:  
>
> **Bash:**  
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
  
> **PowerShell:**  
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
  
Nyisd meg a http://localhost:8084 címet a böngésződben.

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

Az alkalmazás egy webes felületet biztosít, ahol egy AI ügynökkel kommunikálhatsz, amely rendelkezik időjárás és hőmérséklet átváltó eszközökkel.

<a href="images/tools-homepage.png"><img src="../../../translated_images/hu/tools-homepage.4b4cd8b2717f9621.webp" alt="AI ügynök eszközök kezelőfelülete" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Az AI Agent Tools kezelőfelülete – gyors példák és chat interfész az eszközökkel való interakcióhoz*

### Egyszerű eszközhasználat kipróbálása
Kezdje egy egyszerű kéréssel: „Alakítsa át a 100 Fahrenheit-fokot Celsiusra”. Az ügynök felismeri, hogy hőmérséklet-átalakító eszközre van szüksége, a megfelelő paraméterekkel meghívja azt, és visszaadja az eredményt. Figyelje meg, milyen természetesnek tűnik ez – nem kellett megadnia, melyik eszközt használja vagy hogyan hívja meg.

### Eszközláncolás tesztelése

Most próbáljon meg valami összetettebbet: „Milyen az időjárás Seattle-ben, és alakítsa át Fahrenheit-be?” Figyelje, ahogy az ügynök lépésről lépésre dolgozza fel ezt. Először lekéri az időjárást (ami Celsius-ban ad választ), felismeri, hogy át kell alakítania Fahrenheit-be, meghívja az átalakító eszközt, majd mindkét eredményt összefűzi egy válaszba.

### Tekintse meg a beszélgetés menetét

A csevegőfelület megőrzi a beszélgetés előzményeit, lehetővé téve többfordulós interakciókat. Láthatja az összes korábbi lekérdezést és választ, így könnyen nyomon követheti a beszélgetést és megértheti, hogyan építi az ügynök a kontextust több csere során.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/hu/tools-conversation-demo.89f2ce9676080f59.webp" alt="Több eszközmeghívást tartalmazó beszélgetés" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Többfordulós beszélgetés, amely egyszerű átalakításokat, időjárás-lekérdezéseket és eszközláncolást mutat be*

### Kísérletezzen különböző kérésekkel

Próbáljon ki különféle kombinációkat:
- Időjárás-lekérdezések: „Milyen az időjárás Tokióban?”
- Hőmérséklet-átalakítások: „Mennyi 25 °C Kelvinben?”
- Összetett lekérdezések: „Nézze meg az időjárást Párizsban, és mondja meg, hogy 20 °C felett van-e”

Figyelje meg, hogyan értelmezi az ügynök a természetes nyelvet, és hogyan térképezi azt megfelelő eszközmeghívásokra.

## Kulcsfogalmak

### ReAct minta (Gondolkodás és cselekvés)

Az ügynök váltogat a gondolkodás (eldönti, mit tegyen) és a cselekvés (eszközök használata) között. Ez a mintázat lehetővé teszi az autonóm problémamegoldást, nem csupán az utasítások teljesítését.

### Az eszközleírások számítanak

Az eszközleírások minősége közvetlenül befolyásolja, milyen jól tudja az ügynök használni azokat. Egyértelmű, konkrét leírások segítik a modellt megérteni, mikor és hogyan hívjon meg egy eszközt.

### Munkamenet-kezelés

Az `@MemoryId` annotáció lehetővé teszi az automatikus munkamenet-alapú memória kezelését. Minden munkamenetazonosító saját `ChatMemory` példányt kap, amit a `ChatMemoryProvider` bean kezel, így több felhasználó is párhuzamosan tud az ügynökkel kommunikálni anélkül, hogy beszélgetéseik összekeverednének.

<img src="../../../translated_images/hu/session-management.91ad819c6c89c400.webp" alt="Munkamenet-kezelés az @MemoryId használatával" width="800"/>

*Minden munkamenetazonosító egy elkülönített beszélgetési előzményt tárol — a felhasználók sosem látják egymás üzeneteit.*

### Hibakezelés

Az eszközök néha hibázhatnak — API-hívások időtúlléphetnek, a paraméterek érvénytelenek lehetnek, külső szolgáltatások leállhatnak. A gyártásban használt ügynököknek szükségük van hibakezelésre, hogy a modell elmagyarázhassa a problémákat vagy alternatívákat próbálhasson anélkül, hogy az egész alkalmazás összeomlana. Amikor egy eszköz kivételt dob, a LangChain4j elkapja azt és visszajuttatja a hibaüzenetet a modellnek, amely természetes nyelven képes elmagyarázni a gondot.

## Elérhető eszközök

Az alábbi ábra az eszközök széles ökoszisztémáját mutatja be, amelyeket létrehozhat. Ez a modul bemutatja az időjárás- és hőmérséklet-eszközöket, de ugyanaz az `@Tool` minta bármilyen Java metódushoz működik — az adatbázis-lekérdezésektől a fizetésfeldolgozásig.

<img src="../../../translated_images/hu/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Eszközök ökoszisztémája" width="800"/>

*Bármely `@Tool` annotációval ellátott Java metódus elérhetővé válik az AI számára — a mintázat kiterjed adatbázisokra, API-kra, e-mailre, fájlműveletekre és még sok másra.*

## Mikor használjon eszköz-alapú ügynököket

<img src="../../../translated_images/hu/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Mikor használjunk eszközöket" width="800"/>

*Gyors döntési útmutató — az eszközök valós idejű adatokhoz, számításokhoz és műveletekhez valók; az általános tudáshoz és kreatív feladatokhoz nem feltétlenül szükségesek.*

**Eszközöket használjon, ha:**
- A válaszadás valós idejű adatokat igényel (időjárás, részvényárak, készlet)
- Összetettebb számításokat kell végeznie az egyszerű matematikán túl
- Adatbázisokhoz vagy API-khoz kell hozzáférnie
- Műveleteket kell végrehajtania (e-mailek küldése, jegyek létrehozása, rekordok frissítése)
- Több adatforrást kell kombinálnia

**Ne használjon eszközöket, ha:**
- A kérdések válaszai általános tudásból adhatók meg
- A válasz csak beszélgetési jellegű
- Az eszköz késleltetése lassítaná a felhasználói élményt

## Eszközök vs RAG

A 03-as és 04-es modulok egyaránt bővítik az AI képességeit, de alapvetően eltérő módon. A RAG a modellt a **tudás** elérésével támogatja dokumentumok lekérésével. Az eszközök a modellnek képességet adnak műveletek végrehajtására funkcióhívások által.

<img src="../../../translated_images/hu/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Eszközök és RAG összehasonlítása" width="800"/>

*A RAG statikus dokumentumokból szerez információt — az eszközök műveleteket hajtanak végre és dinamikus, valós idejű adatokat szolgáltatnak. Sok gyártási rendszer mindkettőt alkalmazza.*

A gyakorlatban sok gyártási rendszer egyesíti mindkét megközelítést: RAG a dokumentációban található válaszokra támaszkodva, és eszközök az élő adatok lekéréséhez vagy műveletek végrehajtásához.

## Következő lépések

**Következő modul:** [05-mcp - Modell Kontextus Protokoll (MCP)](../05-mcp/README.md)

---

**Navigáció:** [← Előző: 03-as modul – RAG](../03-rag/README.md) | [Vissza a főoldalra](../README.md) | [Következő: 05-ös modul – MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Jogi nyilatkozat**:
Ezt a dokumentumot az AI fordító szolgáltatás, a [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével fordítottuk. Bár igyekszünk a pontosságra, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum anyanyelvén tekintendő hiteles forrásnak. Fontos információk esetén javasoljuk a profi emberi fordítást. Nem vállalunk felelősséget az ebből eredő félreértésekért vagy téves értelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->