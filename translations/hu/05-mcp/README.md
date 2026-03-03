# Modul 05: Model Context Protocol (MCP)

## Tartalomjegyzék

- [Mit tanulhatsz meg](../../../05-mcp)
- [Mi az MCP?](../../../05-mcp)
- [Hogyan működik az MCP](../../../05-mcp)
- [Az Agentikus Modul](../../../05-mcp)
- [A példák futtatása](../../../05-mcp)
  - [Előfeltételek](../../../05-mcp)
- [Gyors kezdés](../../../05-mcp)
  - [Fájlműveletek (Stdio)](../../../05-mcp)
  - [Felügyelő Ügynök](../../../05-mcp)
    - [A demo futtatása](../../../05-mcp)
    - [Hogyan működik a Felügyelő](../../../05-mcp)
    - [Hogyan fedezi fel a FileAgent az MCP eszközöket futásidőben](../../../05-mcp)
    - [Válaszstratégiák](../../../05-mcp)
    - [A kimenet megértése](../../../05-mcp)
    - [Az agentikus modul jellemzőinek magyarázata](../../../05-mcp)
- [Kulcsfogalmak](../../../05-mcp)
- [Gratulálunk!](../../../05-mcp)
  - [Mi következik?](../../../05-mcp)

## Mit tanulhatsz meg

Beszélgető AI rendszert építettél, elsajátítottad a promptokat, választ alapoztál dokumentumokra és ügynököket hoztál létre eszközökkel. De ezek az eszközök egyediek voltak az adott alkalmazásodhoz. Mi lenne, ha AI-d hozzáférést kapna egy szabványos eszközökből álló ökoszisztémához, amit bárki létrehozhat és megoszthat? Ebben a modulban megtanulod, hogyan lehet ezt megvalósítani a Model Context Protocol (MCP) és a LangChain4j agentikus modul segítségével. Először bemutatunk egy egyszerű MCP fájlolvasót, majd megmutatjuk, hogyan lehet azt könnyen integrálni fejlett agentikus munkafolyamatokba a Supervisor Agent mintázat használatával.

## Mi az MCP?

A Model Context Protocol (MCP) pontosan ezt nyújtja — egy szabványos módot arra, hogy AI alkalmazások felfedezzék és használják a külső eszközöket. Egyedi integrációk írása helyett minden adatforráshoz vagy szolgáltatáshoz, csatlakozol MCP szerverekhez, amelyek képességeiket egységes formátumban teszik elérhetővé. AI ügynököd így automatikusan felfedezheti és használhatja ezeket az eszközöket.

Az alábbi ábra megmutatja a különbséget — MCP nélkül minden integráció egyedi, pont-pont összeköttetésre van szükség; MCP-vel egyetlen protokoll csatlakoztatja az alkalmazásodat bármely eszközhöz:

<img src="../../../translated_images/hu/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Az MCP előtt: bonyolult pont-pont integrációk. Az MCP után: egy protokoll, végtelen lehetőségek.*

Az MCP egy alapvető problémát old meg az AI fejlesztésben: minden integráció egyedi. GitHub-hoz szeretnél hozzáférni? Egyedi kód. Fájlokat akarsz olvasni? Egyedi kód. Adatbázist lekérdezni? Egyedi kód. És ezek az egyedi integrációk nem működnek más AI alkalmazásokkal.

Az MCP ezt szabványosítja. Egy MCP szerver eszközöket tesz elérhetővé egyértelmű leírásokkal és séma definíciókkal. Bármely MCP kliens csatlakozhat, felfedezheti az elérhető eszközöket és használhatja azokat. Egyszer építsd meg, mindenhol használd.

A következő ábra ezt az architektúrát szemlélteti — egyetlen MCP kliens (tehát az AI alkalmazásod) több MCP szerverhez csatlakozik, melyek saját eszközkészletüket a szabványos protokollon keresztül teszik elérhetővé:

<img src="../../../translated_images/hu/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol architektúra – szabványosított eszközfelfedezés és végrehajtás*

## Hogyan működik az MCP

A motorháztető alatt az MCP rétegzett architektúrát használ. A Java alkalmazásod (az MCP kliens) felfedezi az elérhető eszközöket, JSON-RPC kéréseket küld a szállítási rétegen (Stdio vagy HTTP) keresztül, és az MCP szerver végrehajtja az operációkat, majd visszaadja az eredményeket. A következő ábra részletezi a protokoll minden rétegét:

<img src="../../../translated_images/hu/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Az MCP működése a motorháztető alatt — a kliensek felfedezik az eszközöket, JSON-RPC üzeneteket váltanak és műveleteket hajtanak végre a szállítási rétegen keresztül.*

**Kliens-szerver architektúra**

Az MCP kliens-szerver modell alapján működik. A szerverek eszközöket szolgáltatnak — fájlok olvasása, adatbázis lekérdezés, API hívások. A kliensek (a te AI alkalmazásod) csatlakoznak a szerverekhez és használják az eszközeiket.

Az MCP használatához a LangChain4j-vel add hozzá a következő Maven függőséget:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Eszközfelfedezés**

Amikor a kliensed csatlakozik egy MCP szerverhez, megkérdezi: "Milyen eszközeid vannak?" A szerver válaszként elérhető eszközök listáját küldi leírásokkal és paraméter sémákkal. Az AI ügynököd ez alapján döntheti el, mely eszközöket használja a felhasználói kérések alapján. Az alábbi ábra ezt az egyeztetést mutatja — a kliens egy `tools/list` kérést küld, a szerver pedig visszaküldi az elérhető eszközeit leírásokkal és paraméter sémákkal:

<img src="../../../translated_images/hu/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*Az AI a startnál felfedezi az elérhető eszközöket — most már tudja, milyen képességek érhetők el és eldöntheti, melyeket használja.*

**Szállítási mechanizmusok**

Az MCP különböző szállítási mechanizmusokat támogat. A két lehetőség: Stdio (helyi alfolyamat kommunikációhoz) és Streamable HTTP (távoli szerverekhez). Ebben a modulban a Stdio szállítást mutatjuk be:

<img src="../../../translated_images/hu/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP szállítási mechanizmusok: HTTP távoli szerverekhez, Stdio helyi folyamatokhoz*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Helyi folyamatokhoz. Az alkalmazásod egy alfolyamatként indít egy szervert és a standard bemeneten/kimeneten keresztül kommunikál vele. Hasznos fájlrendszer eléréshez vagy parancssori eszközökhöz.

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

Az `@modelcontextprotocol/server-filesystem` szerver a következő eszközöket teszi elérhetővé, mind a megadott könyvtárakra korlátozva:

| Eszköz | Leírás |
|--------|--------|
| `read_file` | Egyetlen fájl tartalmának olvasása |
| `read_multiple_files` | Több fájl egyszerre történő olvasása |
| `write_file` | Fájl létrehozása vagy felülírása |
| `edit_file` | Kijelölt keresés és csere műveletek |
| `list_directory` | Fájlok és könyvtárak listázása egy útvonalon |
| `search_files` | Rekurzív keresés fájlok között egy minta alapján |
| `get_file_info` | Fájl metaadatainak lekérése (méret, időbélyegek, jogosultságok) |
| `create_directory` | Könyvtár létrehozása (beleértve a szülőkönyvtárakat is) |
| `move_file` | Fájl vagy könyvtár áthelyezése vagy átnevezése |

A következő ábra mutatja, hogyan működik a Stdio szállítás futásidőben — a Java alkalmazásod elindítja az MCP szervert gyerekfolyamatként, és stdin/stdout csöveken keresztül kommunikálnak, hálózat vagy HTTP nélkül:

<img src="../../../translated_images/hu/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio szállítás működés közben — az alkalmazásod gyerekfolyamatként indítja az MCP szervert és stdin/stdout csöveken keresztül kommunikálnak.*

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chat funkcióval:** Nyisd meg a [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) fájlt és kérdezd meg:
> - "Hogyan működik a Stdio szállítás és mikor érdemes HTTP helyett használni?"
> - "Hogyan kezeli a LangChain4j az MCP szerver folyamatok életciklusát?"
> - "Milyen biztonsági következményekkel jár, ha az AI hozzáférést kap a fájlrendszerhez?"

## Az Agentikus Modul

Míg az MCP szabványosított eszközöket kínál, a LangChain4j **agentikus modulja** deklaratív módot nyújt ügynökök építésére, akik ezeket az eszközöket koordinálják. Az `@Agent` annotáció és az `AgenticServices` segítségével interfészekből definiálhatod az ügynök viselkedését az imperatív kód helyett.

Ebben a modulban megismerkedsz a **Supervisor Agent** mintázattal — ez egy fejlett agentikus AI megközelítés, ahol egy "felügyelő" ügynök dinamikusan dönt arról, hogy mely alügynököket hívja meg felhasználói kérések alapján. A két fogalmat ötvözzük azzal, hogy egy alügynökünk MCP-alapú fájlhozzáférési képességekkel rendelkezik.

Az agentikus modul használatához add hozzá a következő Maven függőséget:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Megjegyzés:** A `langchain4j-agentic` modul külön verziót használ (`langchain4j.mcp.version`), mert eltérő kiadási ciklus szerint frissül, mint a LangChain4j magkönyvtárai.

> **⚠️ Kísérleti státusz:** A `langchain4j-agentic` modul **kísérleti**, változhat a fejlődése. Az AI asszisztensek stabil felépítése továbbra is a `langchain4j-core` és egyedi eszközök használatával (Modul 04).

## A példák futtatása

### Előfeltételek

- Elkészült a [04-es modul - Eszközök](../04-tools/README.md) (ez a modul a testreszabott eszközök koncepcióira épít és összehasonlítja az MCP eszközökkel)
- `.env` fájl a gyökérkönyvtárban Azure hitelesítő adatokkal (az `azd up` futtatásával jön létre az 01-es modul szerint)
- Java 21 vagy újabb, Maven 3.9 vagy újabb
- Node.js 16+ és npm (az MCP szerverekhez)

> **Megjegyzés:** Ha még nem állítottad be a környezeti változókat, nézd meg a [01-es modul - Bevezető](../01-introduction/README.md) szakaszt a telepítési utasításokért (`azd up` automatikusan létrehozza a `.env` fájlt), vagy másold a `.env.example` fájlt `.env`-nek a gyökérkönyvtárban és töltsd ki az értékeket.

## Gyors kezdés

**VS Code használata:** Egyszerűen kattints jobb gombbal bármelyik demo fájlra a Felfedezőben és válaszd a **"Run Java"** opciót, vagy használd az indítási konfigurációkat a Futás és Hibakeresés panelen (előtte győződj meg róla, hogy a `.env` fájlban az Azure hitelesítők helyesen vannak beállítva).

**Maven használata:** Alternatívaként parancssorból is futtathatod az alábbi példák szerint.

### Fájlműveletek (Stdio)

Ez a helyi alfolyamat alapú eszközöket mutatja be.

**✅ Nincs szükség előfeltételre** — az MCP szerver automatikusan elindul.

**Indító szkriptek használata (ajánlott):**

Az indító szkriptek automatikusan betöltik a környezeti változókat a gyökér `.env` fájlból:

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

**VS Code használata:** Kattints jobb gombbal a `StdioTransportDemo.java`-ra és válaszd a **"Run Java"** opciót (győződj meg a konfigurált `.env` fájlról).

Az alkalmazás automatikusan elindít egy fájlrendszer MCP szervert, és beolvas egy helyi fájlt. Figyeld meg, hogyan kezeli helyetted az alfolyamat menedzsmentet.

**Várt kimenet:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Felügyelő Ügynök

A **Supervisor Agent mintázat** egy **rugalmas** agentikus AI forma. Egy Felügyelő LLM-mel autonóm módon dönt arról, hogy melyik ügynököket hívja meg a felhasználói kérés alapján. A következő példában egy MCP-alapú fájlhozzáférést kombinálunk egy LLM ügynökkel, hogy létrehozzunk egy felügyelt fájl olvasás → jelentés készítés munkafolyamatot.

A demóban a `FileAgent` MCP fájlrendszer eszközökkel olvas be fájlt, a `ReportAgent` pedig egy strukturált jelentést állít elő egy 1 mondatos összefoglalóval, 3 kulcsponttal és ajánlásokkal. A Felügyelő automatikusan irányítja ezt a folyamatot:

<img src="../../../translated_images/hu/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*A Felügyelő az LLM segítségével dönti el, hogy mely ügynököket és milyen sorrendben hívja meg — nincs szükség kézi útvonalvezérlésre.*

Így néz ki az általunk használt fájlból jelentés munkafolyamat kézzel foghatóan:

<img src="../../../translated_images/hu/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*A FileAgent az MCP eszközökön keresztül olvassa be a fájlt, majd a ReportAgent átalakítja a nyers tartalmat egy strukturált jelentéssé.*

Az alábbi szekvencia diagram bemutatja a teljes Felügyelői koordinációt — az MCP szerver elindításától, a Felügyelő autonóm ügynök kiválasztásán át a stdio-n keresztüli eszközhívásokig és a végső jelentés előállításáig:

<img src="../../../translated_images/hu/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*A Felügyelő önállóan hívja meg a FileAgentet (aki az MCP szerverhez stdio-n keresztül csatlakozva olvassa a fájlt), majd a ReportAgentet hívja meg a strukturált jelentés elkészítésére — minden ügynök a közös Agentic Scope-ban tárolja az eredményét.*

Minden ügynök a **Agentic Scope**-ban (megosztott memória) tárolja az eredményét, így a későbbi ügynökök elérhetik az előző eredményeket. Ez jól szemlélteti, hogy az MCP eszközök zökkenőmentesen integrálhatók agentikus munkafolyamatokba — a Felügyelőnek nem kell tudnia *hogyan* olvassák a fájlokat, csak hogy a `FileAgent` ezt meg tudja csinálni.

#### A demo futtatása

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

**VS Code használata:** Kattints jobb gombbal a `SupervisorAgentDemo.java`-ra és válaszd a **"Run Java"** opciót (győződj meg róla, hogy a `.env` fájl helyesen be van állítva).

#### Hogyan működik a Felügyelő

Mielőtt ügynököket építenél, csatlakoztatnod kell az MCP szállítást egy klienshez és csomagolnod kell egy `ToolProvider`-t. Így válnak az MCP szerver eszközei elérhetővé az ügynökök számára:

```java
// Hozzon létre egy MCP klienst a transzportból
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Csomagolja a klienst ToolProvider-be — ez összeköti az MCP eszközöket a LangChain4j-vel
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Mostantól bármely ügynökbe befecskendezheted az `mcpToolProvider`-t, amelynek szüksége van MCP eszközökre:

```java
// 1. lépés: A FileAgent fájlokat olvas MCP eszközök segítségével
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // MCP eszközökkel rendelkezik fájlműveletekhez
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

// A Supervisor dönti el a kérés alapján, mely ügynököket hívja meg
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Hogyan fedezi fel a FileAgent az MCP eszközöket futásidőben

Felmerülhet a kérdés: **hogyan tudja a `FileAgent`, hogyan használja az npm fájlrendszer eszközöket?** A válasz az, hogy nem tudja — az **LLM** deríti ki futás közben az eszközök sémáiból.

A `FileAgent` interfész csupán egy **prompt definíció**. Nincs benne előre kódolt tudás az olyan eszközökről, mint a `read_file`, `list_directory` vagy bármely más MCP eszköz. Ez történik end-to-end:
1. **Szerver indítása:** A `StdioMcpTransport` elindítja a `@modelcontextprotocol/server-filesystem` npm csomagot alfolyamatként  
2. **Eszköz felfedezése:** A `McpClient` egy `tools/list` JSON-RPC kérést küld a szervernek, amely eszköznevekkel, leírásokkal és paramétersémákkal válaszol (pl. `read_file` — *"Egy fájl teljes tartalmának beolvasása"* — `{ path: string }`)  
3. **Séma injektálás:** A `McpToolProvider` becsomagolja ezeket a felfedezett sémákat, és elérhetővé teszi a LangChain4j számára  
4. **LLM dönt:** Amikor a `FileAgent.readFile(path)` hívás megtörténik, a LangChain4j elküldi a rendszerüzenetet, a felhasználói üzenetet, **és az eszközsémák listáját** az LLM-nek. Az LLM elolvassa az eszközleírásokat és generál egy eszközhívást (pl. `read_file(path="/some/file.txt")`)  
5. **Végrehajtás:** A LangChain4j elkapja az eszközhívást, az MCP kliensen keresztül visszairányítja a Node.js alfolyamatnak, megkapja az eredményt, és visszatáplálja az LLM-nek  

Ez ugyanaz a fent leírt [Eszköz felfedezés](../../../05-mcp) mechanizmus, de kifejezetten az ügynök munkafolyamatára alkalmazva. A `@SystemMessage` és `@UserMessage` annotációk irányítják az LLM viselkedését, míg a befecskendezett `ToolProvider` adja a **képességeket** — az LLM futásidőben kapcsolja össze a kettőt.

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chat-tel:** Nyisd meg a [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) fájlt, és kérdezd:  
> - "Hogyan tudja ez az ügynök, melyik MCP eszközt hívja meg?"  
> - "Mi történne, ha eltávolítanám a ToolProvidert az ügynök készítőjéből?"  
> - "Hogyan jutnak el az eszközsémák az LLM-hez?"

#### Válaszadási stratégiák

Amikor konfigurálsz egy `SupervisorAgent`-et, meghatározod, hogyan fogalmazza meg végső válaszát a felhasználónak, miután az alügynökök befejezték a feladataikat. Az alábbi ábra három elérhető stratégiát mutat — a LAST közvetlenül a végső ügynök kimenetét adja vissza, a SUMMARY egy LLM segítségével szintetizálja az összes kimenetet, a SCORED pedig a magasabb pontszámú választ adja vissza az eredeti kérés alapján:

<img src="../../../translated_images/hu/response-strategies.3d0cea19d096bdf9.webp" alt="Válaszadási stratégiák" width="800"/>

*Három stratégia, ahogyan a Supervisor alakítja ki a végső választ — válassz attól függően, hogy az utolsó ügynök kimenetét, egy szintetizált összefoglalót, vagy a legjobban pontozott opciót szeretnéd.*

Az elérhető stratégiák:

| Stratégia | Leírás |
|----------|-------------|
| **LAST** | A supervisor a legutoljára meghívott alügynök vagy eszköz kimenetét adja vissza. Ez akkor hasznos, ha a munkafolyamat végső ügynöke kifejezetten teljes, végleges választ állít elő (pl. egy "Összefoglaló Ügynök" egy kutatási folyamatban). |  
| **SUMMARY** | A supervisor saját belső nyelvi modelljét (LLM) használja arra, hogy összefoglalót készítsen az egész interakcióról és az összes alügynök kimenetéről, majd ezt az összefoglalót adja vissza végső válaszként. Ez tiszta, aggregált választ nyújt a felhasználónak. |  
| **SCORED** | A rendszer egy belső LLM-et használ arra, hogy mind a LAST választ, mind az összefoglalót pontozza az eredeti felhasználói kéréshez képest, majd a magasabb pontszámú kimenetet adja vissza. |

A teljes megvalósításhoz lásd a [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) fájlt.

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chat-tel:** Nyisd meg a [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) fájlt, és kérdezd:  
> - "Hogyan dönt a Supervisor, mely ügynököket hívja meg?"  
> - "Mi a különbség a Supervisor és a Sekvenciális munkafolyamat minták között?"  
> - "Hogyan testreszabhatom a Supervisor tervezési viselkedését?"

#### Az output megértése

Amikor futtatod a demót, egy strukturált bemutatót látsz arról, hogyan koordinálja a Supervisor az ügynököket. Íme, mit jelentenek az egyes részek:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**A fejléc** bevezeti a munkafolyamat koncepcióját: fókuszált pipeline a fájlbeolvasástól a jelentéskészítésig.

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
  
**Munkafolyamat diagram** mutatja az adatáramlást az ügynökök között. Minden ügynöknek meghatározott szerepe van:  
- **FileAgent** MCP eszközökkel olvassa be a fájlokat, és a nyers tartalmat a `fileContent` változóba menti  
- **ReportAgent** felhasználja ezt a tartalmat, és strukturált jelentést készít a `report` változóba

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Felhasználói kérés** mutatja a feladatot. A Supervisor ezt értelmezi és dönt úgy, hogy FileAgent → ReportAgent hívásokat kezdeményez.

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
  
**Supervisor koordináció** mutatja a 2 lépéses folyamatot működés közben:  
1. **FileAgent** MCP-n keresztül beolvassa a fájlt és tárolja a tartalmat  
2. **ReportAgent** átveszi a tartalmat és strukturált jelentést hoz létre

A Supervisor ezeket a döntéseket **önállóan** hozta meg a felhasználói kérés alapján.

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
  
#### Ügynöki modul funkciók magyarázata

A példa több fejlett ügynöki modul funkciót is bemutat. Lássuk közelebbről az Agentic Scope-ot és az Agent Listeners-t.

**Agentic Scope** a megosztott memória, ahol az ügynökök a `@Agent(outputKey="...")` annotációval tárolták az eredményeiket. Ez lehetővé teszi:  
- a későbbi ügynökök számára a korábbi eredmények elérését  
- a Supervisor számára, hogy végső választ szintetizáljon  
- neked, hogy megvizsgáld, mit állított elő az egyes ügynökök

Az alábbi diagram bemutatja, hogyan működik az Agentic Scope megosztott memóriaként a fájlból jelentés készülő munkafolyamatban — a FileAgent írja az eredményt `fileContent` kulcs alatt, a ReportAgent olvassa azt és saját eredményt ír `report` alatt:

<img src="../../../translated_images/hu/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope megosztott memória" width="800"/>

*Az Agentic Scope megosztott memória szerepét tölti be — a FileAgent írja a `fileContent`-et, a ReportAgent olvassa és írja a `report`-ot, te pedig elolvasod a végső eredményt.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Nyers fájl adatok a FileAgent-től
String report = scope.readState("report");            // Strukturált jelentés a ReportAgent-től
```
  
**Agent Listeners** lehetőséget adnak az ügynök végrehajtásának megfigyelésére és hibakeresésére. A demóban látott lépésenkénti kimenet egy AgentListenerből származik, amely minden ügynökhívásba be van fűzve:  
- **beforeAgentInvocation** - Meghíváskor hívódik meg, amikor a Supervisor kiválaszt egy ügynököt, így láthatod, melyik ügynököt és miért választotta  
- **afterAgentInvocation** - Az ügynök befejezésekor hívódik meg, megmutatva az eredményt  
- **inheritedBySubagents** - Ha igaz, hallgatózik az egész hierarchiában lévő összes ügynökre

Az alábbi diagram mutatja az Agent Listener teljes életciklusát, beleértve az `onError` kezelést az ügynök végrehajtás közbeni hibái esetén:

<img src="../../../translated_images/hu/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners életciklus" width="800"/>

*Az Agent Listeners az ügynökök végrehajtásának életciklusába kapcsolódnak — figyelik, hogy mikor indul, fejeződik be, vagy ütközik hibába egy ügynök.*

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
  
A Supervisor minta mellett a `langchain4j-agentic` modul több erőteljes munkafolyamat-mintát kínál. Az alábbi diagram mind az ötöt mutatja — az egyszerű szekvenciális pipeline-októl a humán bevonású jóváhagyási munkafolyamatokig:

<img src="../../../translated_images/hu/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Ügynök munkafolyamat-minták" width="800"/>

*Öt munkafolyamat-minta az ügynökök irányítására — az egyszerű szekvenciális pipeline-októl a humán bevonású jóváhagyási folyamatokig.*

| Minta | Leírás | Használati eset |
|---------|-------------|----------|
| **Szekvenciális** | Az ügynökök sorban futnak, az output továbbfolyik a következőhöz | Pipeline-ok: kutatás → elemzés → jelentés |
| **Párhuzamos** | Az ügynökök egyszerre futnak | Független feladatok: időjárás + hírek + részvények |
| **Ciklus** | Ismétlés amíg a feltétel teljesül | Minősítés: finomítás amíg pontszám ≥ 0,8 |
| **Feltételes** | Útválasztás feltételek alapján | Osztályozás → szakértő ügynökhöz irányítás |
| **Humán bevonású** | Emberi ellenőrzőpontok hozzáadása | Jóváhagyási munkafolyamatok, tartalmi felülvizsgálat |

## Kulcsfogalmak

Miután megismerted az MCP-t és az ügynöki modult működés közben, foglaljuk össze, mikor melyik megközelítést érdemes használni.

Az MCP egyik legnagyobb előnye a folyamatosan bővülő ökoszisztéma. Az alábbi ábra megmutatja, hogyan kapcsol egy univerzális protokoll számos MCP szervert az AI alkalmazásodhoz — a fájlrendszer és adatbázis eléréstől kezdve a GitHub-on, e-mailen, web scraping-en át sok minden másig:

<img src="../../../translated_images/hu/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP ökoszisztéma" width="800"/>

*Az MCP egy univerzális protokoll ökoszisztémát teremt — bármely MCP-kompatibilis szerver és ügyfél együttműködik, így eszközök megoszthatók alkalmazások között.*

**Az MCP** ideális, ha létező eszközök ökoszisztémáját akarod kihasználni, olyan eszközöket építesz, amelyeket több alkalmazás is megoszthat, harmadik fél szolgáltatásait szabványos protokollokon keresztül integrálnád, vagy az eszközmegvalósításokat kódmódosítás nélkül cserélnéd.

**Az ügynöki modul** akkor a legjobb választás, ha deklaratív ügynökdefiníciókra van szükséged `@Agent` annotációkkal, munkafolyamat-orchestrationt szeretnél (szekvenciális, ciklus, párhuzamos), előnyben részesíted az interfészalapú ügynöktervezést az imperatív kóddal szemben, vagy több olyan ügynököt használsz, amelyek kimeneteket osztanak meg az `outputKey` segítségével.

**A Supervisor Agent minta** akkor ragyog igazán, ha a munkafolyamat előre nem kiszámítható és az LLM dönt helyetted, ha több specializált ügynököt kell dinamikusan koordinálni, ha beszélgető rendszert építesz, amely különböző képességekhez irányít, vagy ha a legflexibilisebb, adaptív ügynök viselkedést szeretnéd.

A választás segítésére a Module 04 egyedi `@Tool` metódusai és az MCP eszközök között az alábbi összehasonlítás mutatja a legfontosabb trade-offokat — az egyedi eszközök szoros kapcsolatot és teljes típusszafety-t biztosítanak az alkalmazás-specifikus logikához, míg az MCP eszközök szabványosított, újrahasználható integrációkat kínálnak:

<img src="../../../translated_images/hu/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Egyedi eszközök vs MCP eszközök" width="800"/>

*Mikor használj egyedi @Tool metódusokat vs MCP eszközöket — az egyedi eszközök alkalmazás-specifikus logikához teljes típussafety-vel, az MCP eszközök szabványosított, alkalmazások közötti integrációkhoz.*

## Gratulálunk!

Végigmentél a LangChain4j kezdőknek szóló tanfolyamának mind az öt modulján! Íme egy áttekintés a teljes tanulási útról — az alapvető beszélgetéstől az MCP által támogatott ügynöki rendszerekig:

<img src="../../../translated_images/hu/course-completion.48cd201f60ac7570.webp" alt="Tanfolyam befejezés" width="800"/>

*Tanulási utad az öt modulon át — az alapvető chat-től az MCP-vel hajtott agentic rendszerekig.*

Befejezted a LangChain4j kezdőknek tanfolyamot. Megtanultad:  

- Hogyan építs konverzációs MI-t memóriával (01. modul)  
- Prompt tervezési mintákat különféle feladatokra (02. modul)  
- Hogyan alapoztasd válaszaidat dokumentumokra RAG-gal (03. modul)  
- Egyszerű MI ügynökök létrehozását egyedi eszközökkel (04. modul)  
- Szabványos eszközök integrálását a LangChain4j MCP és Agentic modulokkal (05. modul)  

### Mi következik ezután?

A modulok elvégzése után ismerkedj meg a [Tesztelési útmutatóval](../docs/TESTING.md), hogy lásd a LangChain4j tesztelési koncepcióit működés közben.

**Hivatalos források:**  
- [LangChain4j Dokumentáció](https://docs.langchain4j.dev/) – Átfogó útmutatók és API referencia  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) – Forráskód és példák  
- [LangChain4j Oktatóanyagok](https://docs.langchain4j.dev/tutorials/) – Lépésről lépésre oktatóanyagok különféle esetekhez  

Köszönjük, hogy elvégezted ezt a tanfolyamot!

---

**Navigáció:** [← Előző: 04. modul - Eszközök](../04-tools/README.md) | [Vissza a főoldalra](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Felelősség kizárása**:  
Ezt a dokumentumot az AI fordító szolgáltatás, a [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével fordítottuk le. Bár a pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az eredeti nyelven tekintendő hivatalos forrásnak. Fontos információk esetén javasolt szakmai, emberi fordítást igénybe venni. Nem vállalunk felelősséget az e fordítás használatából eredő félreértésekért vagy helytelen értelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->