# Modul 05: Model Context Protocol (MCP)

## Tartalomjegyzék

- [Mit fogsz megtanulni](../../../05-mcp)
- [Mi az MCP?](../../../05-mcp)
- [Hogyan működik az MCP](../../../05-mcp)
- [Az ügynöki modul](../../../05-mcp)
- [A példák futtatása](../../../05-mcp)
  - [Előfeltételek](../../../05-mcp)
- [Gyors kezdés](../../../05-mcp)
  - [Fájlműveletek (Stdio)](../../../05-mcp)
  - [Felügyelő ügynök](../../../05-mcp)
    - [A demó futtatása](../../../05-mcp)
    - [Hogyan működik a felügyelő](../../../05-mcp)
    - [Válaszstratégiák](../../../05-mcp)
    - [Az eredmény megértése](../../../05-mcp)
    - [Az ügynöki modul funkcióinak magyarázata](../../../05-mcp)
- [Kulcsfogalmak](../../../05-mcp)
- [Gratulálunk!](../../../05-mcp)
  - [Mi következik?](../../../05-mcp)

## Mit fogsz megtanulni

Már építettél beszélgető AI-t, elsajátítottad a promptokat, megalapozott válaszokat készítettél dokumentumokból, és létrehoztál eszközökkel rendelkező ügynököket. De ezek az eszközök mind az adott alkalmazásodra szabva készültek. Mi lenne, ha AI-d hozzáférést kaphatna egy szabványosított ökoszisztémához, amelyet bárki létrehozhat és megoszthat? Ebben a modulban épp ezt tanulod meg a Model Context Protocol (MCP) és a LangChain4j ügynöki modul segítségével. Először bemutatunk egy egyszerű MCP fájlolvasót, majd megmutatjuk, hogyan integrálható ez könnyedén fejlett ügynöki munkafolyamatokba a Felügyelő Ügynök mintájának használatával.

## Mi az MCP?

A Model Context Protocol (MCP) éppen ezt biztosítja - egy szabványos módot arra, hogy AI alkalmazások felfedezzék és használják a külső eszközöket. Egyedi integrációk írása helyett minden adatforráshoz vagy szolgáltatáshoz, kapcsolódj MCP szerverekhez, amelyek képességeiket konzisztens formátumban teszik elérhetővé. Az AI ügynököd így automatikusan felfedezheti és használhatja ezeket az eszközöket.

Az alábbi ábra bemutatja a különbséget — MCP nélkül minden integráció egyedi pont-pont összekötést igényel; MCP-vel egyetlen protokoll köti össze az alkalmazásodat bármely eszközzel:

<img src="../../../translated_images/hu/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*MCP előtt: bonyolult egyedi pont-pont integrációk. MCP után: Egy protokoll, végtelen lehetőségek.*

Az MCP megold egy alapvető problémát az AI fejlesztésben: minden integráció egyedi. GitHub-hoz akarsz hozzáférni? Egyedi kód. Fájlokat olvasnál? Egyedi kód. Adatbázist kérdeznél le? Egyedi kód. És egyik integráció sem működik más AI alkalmazásokkal.

Az MCP ezt szabványosítja. Egy MCP szerver eszközöket kínál világos leírással és sémákkal. Bármely MCP kliens kapcsolódhat, felfedezheti az elérhető eszközöket, és használhatja azokat. Egyszer építsd meg, bárhol használd.

Az alábbi ábra ezt az architektúrát szemlélteti — egyetlen MCP kliens (a te AI alkalmazásod) több MCP szerverhez kapcsolódik, amelyek mind egyedi eszközkészletüket teszik elérhetővé a szabványos protokollon keresztül:

<img src="../../../translated_images/hu/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol architektúra - szabványosított eszköz-felfedezés és végrehajtás*

## Hogyan működik az MCP

A háttérben az MCP rétegzett architektúrát használ. A Java alkalmazásod (az MCP kliens) felfedezi az elérhető eszközöket, JSON-RPC kéréseket küld egy transzport rétegen keresztül (Stdio vagy HTTP), az MCP szerver végrehajtja a műveleteket és visszaadja az eredményeket. Az alábbi ábra bontja le a protokoll rétegeit:

<img src="../../../translated_images/hu/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Hogyan működik az MCP a háttérben — a kliensek felfedezik az eszközöket, JSON-RPC üzeneteket cserélnek, és műveleteket hajtanak végre egy transzport rétegen keresztül.*

**Szerver-Kliens architektúra**

Az MCP kliens-szerver modellt használ. A szerverek eszközöket biztosítanak - fájlok olvasása, adatbázis lekérdezések, API hívások. A kliensek (az AI alkalmazásod) kapcsolódnak a szerverekhez és használják az eszközeiket.

Az MCP használatához LangChain4j-vel add hozzá ezt a Maven-függőséget:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Eszköz felfedezés**

Amikor a kliensed kapcsolódik egy MCP szerverhez, megkérdezi: "Milyen eszközeid vannak?" A szerver válaszként elérhető eszközök listáját küldi leírásokkal és paraméter-sémákkal. Az AI ügynököd így eldöntheti, mely eszközöket használja a felhasználói kérések alapján. Az alábbi ábra ezt az üdvözlési kézfogást mutatja — a kliens egy `tools/list` kérést küld, és a szerver visszaküldi az elérhető eszközöket leírásokkal és paraméter-sémákkal:

<img src="../../../translated_images/hu/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*Az AI induláskor felfedezi az elérhető eszközöket — így tudja, milyen képességek állnak rendelkezésre, és dönthet, melyeket használja.*

**Transzport mechanizmusok**

Az MCP különböző transzport mechanizmusokat támogat. A két opciónk a Stdio (helyi alfolyamat kommunikációhoz) és Streamable HTTP (távoli szerverekhez). Ez a modul a Stdio transzportot mutatja be:

<img src="../../../translated_images/hu/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP transzport mechanizmusok: HTTP távoli szerverekhez, Stdio helyi folyamatokhoz*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Helyi folyamatokhoz. Az alkalmazás egy szervert indít alfolyamatként, és a szabványos bemenet/kimeneten keresztül kommunikál. Hasznos fájlrendszer-hozzáféréshez vagy parancssori eszközökhöz.

```java
McpTransport stdioTransport = new StdioMcpTransport.Builder()
    .command(List.of(
        npmCmd, "exec",
        "@modelcontextprotocol/server-filesystem@2025.12.18",
        resourcesDir
    ))
    .logEvents(false)
    .build();
```

Az `@modelcontextprotocol/server-filesystem` szerver a következő eszközöket kínálja, mindegyik az általad megadott könyvtárakra korlátozva:

| Eszköz | Leírás |
|------|-------------|
| `read_file` | Egyetlen fájl tartalmának olvasása |
| `read_multiple_files` | Több fájl egy hívásban történő olvasása |
| `write_file` | Fájl létrehozása vagy felülírása |
| `edit_file` | Célzott keresés és csere műveletek végrehajtása |
| `list_directory` | Fájlok és könyvtárak listázása egy útvonalon |
| `search_files` | Rekurzívan keres fájlokat mintának megfelelően |
| `get_file_info` | Fájl metaadatainak lekérése (méret, időbélyegek, jogosultságok) |
| `create_directory` | Könyvtár létrehozása (szülő könyvtárakkal együtt) |
| `move_file` | Fájl vagy könyvtár áthelyezése, átnevezése |

Az alábbi ábra bemutatja, hogyan működik a Stdio transzport futásidőben — a Java alkalmazásod egy MCP szervert indít gyerekfolyamatként, és stdin/stdout csöveken keresztül kommunikálnak, hálózat vagy HTTP nélkül:

<img src="../../../translated_images/hu/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio transzport működés közben — az alkalmazásod gyerekfolyamatként indítja az MCP szervert, és stdin/stdout csöveken keresztül kommunikálnak.*

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chattel:** Nyisd meg a [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) fájlt és kérdezd meg:
> - "Hogyan működik a Stdio transzport, és mikor használjam a HTTP-vel szemben?"
> - "Hogyan kezeli a LangChain4j az MCP szerver folyamatok életciklusát?"
> - "Milyen biztonsági kockázatokkal jár, ha az AI hozzáférést kap a fájlrendszerhez?"

## Az ügynöki modul

Míg az MCP szabványosított eszközöket biztosít, a LangChain4j **ügynöki modulja** deklaratív módot kínál arra, hogy ügynököket építs, amelyek koordinálják ezeket az eszközöket. Az `@Agent` annotáció és az `AgenticServices` lehetővé teszi, hogy interfészeken keresztül, imperatív kód helyett definiáld az ügynökök viselkedését.

Ebben a modulban megismerkedsz a **Felügyelő Ügynök** mintával — egy haladó ügynöki AI megközelítéssel, ahol egy "felügyelő" ügynök dinamikusan dönt, hogy melyik al-ügynököket hívja meg a felhasználói kérések alapján. Egyesítjük a két koncepciót úgy, hogy egyik al-ügynökünk MCP-alapú fájlhozzáféréssel rendelkezik.

Az ügynöki modul használatához add hozzá ezt a Maven-függőséget:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Megjegyzés:** A `langchain4j-agentic` modul külön verzió tulajdonságot használ (`langchain4j.mcp.version`), mert más ütemezés szerint jelenik meg, mint a core LangChain4j könyvtárak.

> **⚠️ Kísérleti:** A `langchain4j-agentic` modul **kísérleti jellegű**, és változhat. Az AI asszisztensek építésének stabil módja továbbra is a `langchain4j-core` egyedi eszközökkel (04. modul).

## A példák futtatása

### Előfeltételek

- Elvégzett [04-es modul - Eszközök](../04-tools/README.md) (ez a modul az egyedi eszközökre épít és összehasonlítja azokat az MCP eszközökkel)
- `.env` fájl a gyökérkönyvtárban Azure hitelesítési adatokkal (amit a `azd up` hoz létre az 01-es modulban)
- Java 21+, Maven 3.9+
- Node.js 16+ és npm (az MCP szerverekhez)

> **Megjegyzés:** Ha még nem állítottad be a környezeti változókat, lásd a [01-es modul - Bevezetés](../01-introduction/README.md) telepítési utasításait (`azd up` automatikusan létrehozza a `.env` fájlt), vagy másold a `.env.example` fájlt `.env`-re a gyökérkönyvtárban és töltsd ki a megfelelőt.

## Gyors kezdés

**VS Code használata:** Egyszerűen kattints jobb gombbal bármelyik demó fájlra a Fájlkezelőben, és válaszd a **„Run Java”** lehetőséget, vagy használd a Futás és Hibakeresés panel indítási konfigurációit (győződj meg róla, hogy előbb a `.env` fájlban az Azure hitelesítők be vannak állítva).

**Mavennel:** Alternatívaként futtathatod parancssorból a következő példák szerint.

### Fájl műveletek (Stdio)

Ez a helyi alfolyamat alapú eszközöket mutatja be.

**✅ Nincs szükség előfeltételekre** - az MCP szervert az alkalmazás automatikusan elindítja.

**Indító szkriptek használata (ajánlott):**

Az indító szkriptek automatikusan betöltik a környezeti változókat a gyökérkönyvtár `.env` fájljából:

**Bash:**
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-stdio.ps1
```

**VS Code használata:** Jobb klikk a `StdioTransportDemo.java` fájlra, majd válaszd a **„Run Java”** lehetőséget (győződj meg arról, hogy a `.env` be van állítva).

Az alkalmazás automatikusan elindít egy fájlrendszer MCP szervert és beolvassa egy helyi fájl tartalmát. Figyeld meg, hogyan kezeli az alfolyamat kezelést az alkalmazás helyetted.

**Várt kimenet:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Felügyelő ügynök

A **Felügyelő Ügynök minta** egy **rugalmas** formája az ügynöki AI-nak. Egy felügyelő az LLM segítségével autonóm módon dönt arról, hogy mely ügynököket hívja meg a felhasználó kérései alapján. A következő példában kombináljuk az MCP-alapú fájlhozzáférést egy LLM ügynökkel, hogy létrehozzunk egy felügyelt fájlolvasás → jelentés munkafolyamatot.

A demóban a `FileAgent` fájlt olvas MCP fájlrendszer eszközökkel, a `ReportAgent` strukturált jelentést generál egy vezetői összefoglalóval (1 mondat), 3 kulcsponttal és ajánlásokkal. A Felügyelő automatikusan koordinálja ezt a folyamatot:

<img src="../../../translated_images/hu/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*A felügyelő az LLM-jét használja, hogy eldöntse, mely ügynököket hívjon meg, és milyen sorrendben — nincs szükség keménykódolt útvonalválasztásra.*

Ez a konkrét munkafolyamat a fájlból jelentésre csővezetékünk számára:

<img src="../../../translated_images/hu/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*A FileAgent az MCP eszközökön keresztül olvassa a fájlt, majd a ReportAgent az nyers tartalmat strukturált jelentéssé alakítja.*

Minden ügynök kimenetét az **Agentic Scope**-ban (megosztott memória) tárolja, így az utólagos ügynökök elérhetik a korábbi eredményeket. Ez demonstrálja, hogy az MCP eszközök hogyan integrálódnak zökkenőmentesen az ügynöki munkafolyamatokba — a Felügyelőnek nem kell tudnia, *hogyan* olvassák a fájlokat, csak annyi a dolga, hogy a `FileAgent` képes rá.

#### A demó futtatása

Az indító szkriptek automatikusan betöltik a környezeti változókat a gyökér `.env` fájlból:

**Bash:**
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-supervisor.ps1
```

**VS Code használata:** Jobb kattintás a `SupervisorAgentDemo.java` fájlra, majd válaszd a **„Run Java”** lehetőséget (győződj meg róla, hogy a `.env` be van állítva).

#### Hogyan működik a felügyelő

Az ügynökök építése előtt össze kell kötni az MCP transzportot egy klienssel, és becsomagolni mint `ToolProvider`. Így válnak az MCP szerver eszközei elérhetővé az ügynökeid számára:

```java
// Hozzon létre egy MCP klienst a transzportból
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Csomagolja be az ügyfelet ToolProvider-ként — ez hidat képez az MCP eszközök és a LangChain4j között
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Most már injektálhatod a `mcpToolProvider`-t bármelyik ügynökbe, amelynek szüksége van MCP eszközökre:

```java
// 1. lépés: A FileAgent fájlokat olvas MCP eszközökkel
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Rendelkezik MCP eszközökkel fájlműveletekhez
        .build();

// 2. lépés: A ReportAgent strukturált jelentéseket készít
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// A Supervisor irányítja a fájl → jelentés munkafolyamatot
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Visszaadja a végső jelentést
        .build();

// A Supervisor dönt arról, mely ügynököket hívja meg a kérés alapján
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Válaszstratégiák

Amikor beállítod a `SupervisorAgent`-et, meg kell adnod, hogyan fogalmazza meg a végső választ a felhasználónak, miután az al-ügynökök befejezték a feladataikat. Az alábbi ábra a három elérhető stratégiát mutatja be — a LAST közvetlenül az utolsó ügynök kimenetét adja vissza, a SUMMARY egy LLM segítségével szintetizálja az összes kimenetet, az SCORED pedig a hármat értékeli az eredeti kéréshez képest és a jobb pontszámút választja:

<img src="../../../translated_images/hu/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Három stratégia arra, hogyan fogalmazza meg a Felügyelő a végső válaszát — válaszd aszerint, hogy az utolsó ügynök kimenetét, egy összefoglaló szintézist, vagy a legjobbra értékelt opciót szeretnéd.*

Az elérhető stratégiák:

| Stratégia | Leírás |
|----------|-------------|
| **LAST** | A felügyelő az utolsó al-ügynök vagy eszköz által szolgáltatott kimenetet adja vissza. Ez hasznos, ha a munkafolyamat utolsó ügynöke kifejezetten a teljes, végső válasz előállítására van tervezve (pl. egy "Összefoglaló Ügynök" egy kutatási folyamatban). |
| **SUMMARY** | A felügyelő a saját belső nyelvi modelljét használja az egész interakció és az összes al-ügynök kimenetének összesítésére, és ezt a szintetizált összefoglalót adja vissza végső válaszként. Ez tiszta, összefoglalt választ biztosít a felhasználónak. |
| **SCORED** | A rendszer egy belső LLM-et használ arra, hogy pontozza mind a LAST választ, mind a SUMMARY-t az eredeti felhasználói kéréshez képest, és azt a kimenetet adja vissza, amelyik jobb pontszámot kapott. |
Teljes megvalósításért lásd a [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) fájlt.

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chat-et:** Nyisd meg a [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) fájlt, és kérdezd meg:
> - „Hogyan dönt a Supervisor, mely ügynököket hívja meg?”
> - „Mi a különbség a Supervisor és a Szekvenciális munkafolyamat minták között?”
> - „Hogyan testreszabhatom a Supervisor tervezési viselkedését?”

#### A kimenet megértése

Ha futtatod a demót, egy strukturált végigvezetést látsz arról, hogy a Supervisor miként irányít több ügynököt. Íme, mit jelent az egyes szakasz:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**A fejlécek** bemutatják a munkafolyamat koncepcióját: egy fókuszált csővezetéket az állományolvasástól a jelentéskészítésig.

```
--- WORKFLOW ---------------------------------------------------------
  ┌─────────────┐      ┌──────────────┐
  │  FileAgent  │ ───▶ │ ReportAgent  │
  │ (MCP tools) │      │  (pure LLM)  │
  └─────────────┘      └──────────────┘
   outputKey:           outputKey:
   'fileContent'        'report'

--- AVAILABLE AGENTS -------------------------------------------------
  [FILE]   FileAgent   - Reads files via MCP → stores in 'fileContent'
  [REPORT] ReportAgent - Generates structured report → stores in 'report'
```

**A munkafolyamat diagramja** megmutatja az adatáramlást az ügynökök között. Minden ügynöknek megvan a maga szerepe:
- **FileAgent** MCP eszközökkel olvassa be a fájlokat, és tárolja a nyers tartalmat a `fileContent` változóban
- **ReportAgent** felhasználja ezt a tartalmat, és struktúrált jelentést készít a `report` változóban

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Felhasználói kérés** mutatja a feladatot. A Supervisor ezt feldolgozza és úgy dönt, hogy FileAgent → ReportAgent sorrendben hívja meg az ügynököket.

```
--- SUPERVISOR ORCHESTRATION -----------------------------------------
  The Supervisor decides which agents to invoke and passes data between them...

  +-- STEP 1: Supervisor chose -> FileAgent (reading file via MCP)
  |
  |   Input: .../file.txt
  |
  |   Result: LangChain4j is an open-source, provider-agnostic Java framework for building LLM...
  +-- [OK] FileAgent (reading file via MCP) completed

  +-- STEP 2: Supervisor chose -> ReportAgent (generating structured report)
  |
  |   Input: LangChain4j is an open-source, provider-agnostic Java framew...
  |
  |   Result: Executive Summary...
  +-- [OK] ReportAgent (generating structured report) completed
```

**Supervisor szervezés** megmutatja a kétszakaszos folyamatot:
1. **FileAgent** MCP-n keresztül olvassa be a fájlt és tárolja a tartalmat
2. **ReportAgent** megkapja a tartalmat és struktúrált jelentést készít

A Supervisor ezeket a döntéseket **önállóan** hozta a felhasználói kérés alapján.

```
--- FINAL RESPONSE ---------------------------------------------------
Executive Summary
...

Key Points
...

Recommendations
...

--- AGENTIC SCOPE (Data Flow) ----------------------------------------
  Each agent stores its output for downstream agents to consume:
  * fileContent: LangChain4j is an open-source, provider-agnostic Java framework...
  * report: Executive Summary...
```

#### Magyarázat az Agentic Modul funkcióira

A példa több fejlett funkcióját is bemutatja az agentic modulnak. Vizsgáljuk meg közelebbről az Agentic Scope-ot és az Agent Listeners-eket.

**Agentic Scope** mutatja a megosztott memóriát, ahol az ügynökök az `@Agent(outputKey="...")` annotációval tárolták az eredményeket. Ez lehetővé teszi:
- Hogy későbbi ügynökök elérjék a korábbi ügynökök kimenetét
- Hogy a Supervisor összesítse a végső választ
- Hogy te megvizsgálhasd, mit állított elő az egyes ügynök

Az alábbi diagram azt mutatja, hogyan működik az Agentic Scope megosztott memóriaként a fájl-jelentés munkafolyamatban — a FileAgent a `fileContent` kulcs alatt írja az eredményt, a ReportAgent ezt olvassa és a `report` kulcs alatt írja a saját eredményét:

<img src="../../../translated_images/hu/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Az Agentic Scope megosztott memóriaként működik — a FileAgent írja a `fileContent`-et, a ReportAgent olvassa azt, majd írja a `report`-ot, és a kódod olvassa a végső eredményt.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Nyers fájladat a FileAgent-től
String report = scope.readState("report");            // Strukturált jelentés a ReportAgent-től
```

**Agent Listeners** lehetővé teszik az ügynökök futásának figyelését és hibakeresését. A demóban látott lépésről lépésre történő kimenet egy olyan AgentListenerből származik, amely minden ügynök-aktiváláshoz kapcsolódik:
- **beforeAgentInvocation** - Akkor hívódik meg, amikor a Supervisor kiválaszt egy ügynököt, lehetővé téve, hogy lásd, melyik ügynököt miért választotta
- **afterAgentInvocation** - Akkor hívódik meg, amikor egy ügynök befejeződik, megjelenítve az eredményét
- **inheritedBySubagents** - Ha true, a hallgató figyeli a hierarchiában lévő összes ügynököt

Az alábbi diagram bemutatja az Agent Listener teljes életciklusát, beleértve, hogy az `onError` hogyan kezeli a hibákat az ügynök futása során:

<img src="../../../translated_images/hu/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Az Agent Listeners-ek bekapcsolódnak a futás életciklusába — figyelik, mikor indul el, fejeződik be vagy hibázik egy ügynök.*

```java
AgentListener monitor = new AgentListener() {
    private int step = 0;
    
    @Override
    public void beforeAgentInvocation(AgentRequest request) {
        step++;
        System.out.println("  +-- STEP " + step + ": " + request.agentName());
    }
    
    @Override
    public void afterAgentInvocation(AgentResponse response) {
        System.out.println("  +-- [OK] " + response.agentName() + " completed");
    }
    
    @Override
    public boolean inheritedBySubagents() {
        return true; // Terjessze az összes alügynökre
    }
};
```

A Supervisor minta mellett a `langchain4j-agentic` modul több erőteljes munkafolyamat-mintát kínál. Az alábbi diagram az ötöt mutatja be — az egyszerű szekvenciális csővezetékektől az emberi ellenőrzést igénylő jóváhagyási folyamatokig:

<img src="../../../translated_images/hu/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Öt munkafolyamat-minta az ügynökök irányítására — az egyszerű szekvenciális csővezetékektől az emberi beavatkozást igénylő jóváhagyási folyamatokig.*

| Minta | Leírás | Használati eset |
|---------|-------------|----------|
| **Szekvenciális** | Az ügynökök sorban futnak, a kimenet a következőnek megy | Csővezetékek: kutatás → elemzés → jelentés |
| **Párhuzamos** | Az ügynökök egyszerre futnak | Független feladatok: időjárás + hírek + részvények |
| **Ciklus** | Ismétlődik, amíg a feltétel teljesül | Minőségi pontozás: finomítás amíg pont ≥ 0,8 |
| **Feltételes** | Feltételek alapján irányít | Osztályozás → specialistához irányítás |
| **Emberi beavatkozás** | Emberi ellenőrző pontokat ad hozzá | Jóváhagyási folyamatok, tartalmi felülvizsgálat |

## Kulcsfogalmak

Most, hogy megismerted az MCP-t és az agentic modult a gyakorlatban, összefoglaljuk, mikor használj melyik megközelítést.

Az MCP egyik legnagyobb előnye a növekvő ökoszisztéma. Az alábbi diagram mutatja, hogyan kapcsol egyetlen univerzális protokoll az AI alkalmazásodat sokféle MCP szerverhez — fájlrendszer- és adatbázis-hozzáféréstől a GitHub, e-mail, webscraping és egyebek felé:

<img src="../../../translated_images/hu/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*Az MCP univerzális protokoll-ökoszisztémát hoz létre — bármelyik MCP-kompatibilis szerver működik bármelyik MCP-kompatibilis klienssel, lehetővé téve az eszközök megosztását alkalmazások között.*

**Az MCP** ideális, ha meglévő eszközök ökoszisztémáját akarod kihasználni, olyan eszközöket építesz, amiket több alkalmazás használhat, harmadik féltől származó szolgáltatásokat integrálsz szabványos protokollokkal, vagy eszközmegvalósításokat cserélnél anélkül, hogy a kódot módosítanád.

**Az Agentic Modul** akkor a legjobb választás, ha deklaratív ügynökdefiníciókra van szükséged `@Agent` annotációkkal, munkafolyamat-orchestrationt akarsz (szekvenciális, ciklus, párhuzamos), inkább interfész alapú ügynöktervezést preferálsz az imperatív kód helyett, vagy ha több ügynököt kombinálsz, amelyek megosztott `outputKey` értékeken alapulnak.

**A Supervisor Agent minta** akkor tündököl, ha a munkafolyamat előre nem kiszámítható és szeretnéd, hogy a LLM döntse el, ha több specializált ügynököt kell dinamikusan szervezni, ha beszélgetéses rendszereket építesz, amelyek különböző képességekre irányítanak, vagy ha a legrugalmasabb, legadaptívabb ügynök viselkedést akarod.

Ahhoz, hogy segítsen dönteni a Module 04 egyedi `@Tool` metódusai és az MCP eszközök között, az alábbi összehasonlítás kiemeli a fő kompromisszumokat — az egyedi eszközök szoros kötést és teljes típusbiztonságot adnak az alkalmazás-specifikus logikához, míg az MCP eszközök szabványosított, újrahasznosítható integrációk:

<img src="../../../translated_images/hu/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Mikor használj egyedi @Tool metódusokat vs MCP eszközöket — egyedi eszközök az alkalmazás-specifikus logikához teljes típusbiztonsággal, MCP eszközök a szabványos integrációkért, amelyek több alkalmazásban is működnek.*

## Gratulálunk!

Végigmentél a LangChain4j kezdőknek kurzus mind az öt modulján! Íme egy áttekintés a teljes tanulási útról, amit bejártál — az alapvető chat-től az MCP-vel hajtott agentikus rendszerekig:

<img src="../../../translated_images/hu/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*A tanulási utad mind az öt modult átöleli — az alapvető chattől az MCP erővel rendelkező agentikus rendszerekig.*

Befejezted a LangChain4j kezdőknek kurzust. Megtanultad:

- Hogyan építs beszélgető AI-t memóriával (01. modul)
- Prompt tervezési mintákat különböző feladatokhoz (02. modul)
- Hogyan helyezz válaszokat a dokumentumaidhoz RAG segítségével (03. modul)
- Alapvető AI ügynökök (asszisztensek) létrehozását egyedi eszközökkel (04. modul)
- Szabványosított eszközök integrálását a LangChain4j MCP és Agentic modulokkal (05. modul)

### Mi következik ezután?

A modulok elvégzése után nézd meg a [Tesztelési Útmutatót](../docs/TESTING.md), hogy lásd a LangChain4j tesztelési koncepciókat működés közben.

**Hivatalos források:**
- [LangChain4j Dokumentáció](https://docs.langchain4j.dev/) – Átfogó útmutatók és API referencia
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) – Forráskód és példák
- [LangChain4j Oktatóanyagok](https://docs.langchain4j.dev/tutorials/) – Lépésről lépésre oktatóanyagok különböző felhasználási esetekhez

Köszönjük, hogy elvégezted ezt a kurzust!

---

**Navigáció:** [← Előző: Modul 04 - Eszközök](../04-tools/README.md) | [Vissza a főoldalra](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Jogi nyilatkozat**:  
Ezt a dokumentumot az AI fordító szolgáltatás [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével fordítottuk le. Bár a pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum a saját nyelvén tekintendő hiteles forrásnak. Kritikus információk esetén javasolt szakmai, emberi fordítást igénybe venni. Nem vállalunk felelősséget az ebből a fordításból eredő félreértésekért vagy téves értelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->