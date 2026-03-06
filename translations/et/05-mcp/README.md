# Moodul 05: Mudeli kontekstiprotokoll (MCP)

## Sisukord

- [Mida sa õpid](../../../05-mcp)
- [Mis on MCP?](../../../05-mcp)
- [Kuidas MCP töötab](../../../05-mcp)
- [Agentne moodul](../../../05-mcp)
- [Näidete käivitamine](../../../05-mcp)
  - [Eeltingimused](../../../05-mcp)
- [Kiire algus](../../../05-mcp)
  - [Failioperatsioonid (Stdio)](../../../05-mcp)
  - [Järelevalveagent](../../../05-mcp)
    - [Demo käivitamine](../../../05-mcp)
    - [Kuidas järelevalve töötab](../../../05-mcp)
    - [Kuidas FileAgent leiab MCP tööriistu jooksuajal](../../../05-mcp)
    - [Vastustesatsioonid](../../../05-mcp)
    - [Väljundi mõistmine](../../../05-mcp)
    - [Agentmooduli funktsioonide selgitus](../../../05-mcp)
- [Põhimõisted](../../../05-mcp)
- [Palju õnne!](../../../05-mcp)
  - [Mis järgmiseks?](../../../05-mcp)

## Mida sa õpid

Oled ehitanud vestluslikku tehisintellekti, valdanud käivitajaid, sidunud vastuseid dokumentidega ja loonud agenete tööriistadega. Kuid kõik need tööriistad olid kohandatud sinu konkreetse rakenduse jaoks. Mis siis, kui saaksid anda oma AI-le juurdepääsu standardiseeritud tööriistade ökosüsteemile, mida igaüks saab luua ja jagada? Selles moodulis õpid just seda Model Context Protocoli (MCP) ja LangChain4j agentmooduli abil. Näitame kõigepealt lihtsat MCP faililugejat ja seejärel seda, kuidas see hõlpsasti integreerub keerukamates agentsetes töövoogudes Supervisori agendi mustri abil.

## Mis on MCP?

Model Context Protocol (MCP) pakub täpselt seda — standardset viisi AI-rakendustel avastada ja kasutada väliseid tööriistu. Selle asemel, et kirjutada iga andmeallika või teenuse jaoks eraldi integratsioone, ühendad MCP serveritega, mis avaldavad oma võimed järjepidevas formaadis. Sinu AI agent saab seejärel neid tööriistu automaatselt avastada ja kasutada.

Joonis allpool näitab erinevust — ilma MCP-ta nõuab iga integratsioon kohandatud punkt-punkt ühendust; MCP-ga ühendab üks protokoll sinu rakenduse suvalise tööriistaga:

<img src="../../../translated_images/et/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Enne MCP-d: keerulised punkt-punkt integratsioonid. Pärast MCP-d: üks protokoll, lõputud võimalused.*

MCP lahendab AI arenduses põhiprobleemi: iga integratsioon on eraldi. Tahad juurdepääsu GitHubile? Kohandatud kood. Tahad faile lugeda? Kohandatud kood. Tahad andmebaasipäringuid teha? Kohandatud kood. Ja ükski neist integratsioonidest ei tööta teiste AI-rakendustega.

MCP standardiseerib selle. MCP server avaldab tööriistad selgete kirjelduste ja skeemidega. Iga MCP klient saab ühenduda, avastada saadaval olevad tööriistad ja neid kasutada. Tee korra, kasuta kõikjal.

Joonis allpool illustreerib seda arhitektuuri — üks MCP klient (sinu AI rakendus) ühendub mitme MCP serveriga, millest igaüks avaldab oma tööriistakomplekti standardprotokolli kaudu:

<img src="../../../translated_images/et/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol arhitektuur — standardiseeritud tööriistade avastamine ja kasutamine*

## Kuidas MCP töötab

Sisemiselt kasutab MCP kihilist arhitektuuri. Sinu Java rakendus (MCP klient) avastab saadaval olevad tööriistad, saadab JSON-RPC päringuid transpordikihi (Stdio või HTTP) kaudu, ja MCP server täidab operatsioone ning tagastab tulemused. Järgmine joonis näitab selle protokolli iga kihti:

<img src="../../../translated_images/et/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Kuidas MCP töötab sisemiselt — kliendid avastavad tööriistad, vahetavad JSON-RPC sõnumeid ja täidavad operatsioone transpordikihi kaudu.*

**Server-kliendi arhitektuur**

MCP kasutab klient-server mudelit. Serverid pakuvad tööriistu — failide lugemine, andmebaaside päringud, API-de kutsumine. Kliendid (sinu AI rakendus) ühenduvad serveritega ja kasutavad nende tööriistu.

MCP kasutamiseks LangChain4j-ga lisa see Maven sõltuvus:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Tööriistade avastamine**

Kui sinu klient ühendub MCP serveriga, küsib ta: „Millised tööriistad sul on?“ Server vastab nimekirjaga saadaval olevatest tööriistadest, igaühel kirjeldused ja parameetri skeemid. Sinu AI agent saab seejärel otsustada, milliseid tööriistu kasutada kasutaja päringute põhjal. Joonis allpool näitab selle kättesaadet — klient saadab `tools/list` päringu ja server tagastab oma saadaval olevad tööriistad koos kirjelduste ja parameetri skeemidega:

<img src="../../../translated_images/et/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI avastab käivitamisel saadaval olevad tööriistad — ta teab nüüd, milliseid võimalusi on ja saab otsustada, milliseid kasutada.*

**Transpordimehhanismid**

MCP toetab erinevaid transpordimehhanisme. Valikuteks on Stdio (kohalike alamprotsesside suhtlemiseks) ja Streamable HTTP (kaugserverite jaoks). See moodul demonstreerib Stdio transporti:

<img src="../../../translated_images/et/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP transpordimehhanismid: HTTP kaugserverite jaoks, Stdio kohalike protsesside jaoks*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Kohalike protsesside jaoks. Sinu rakendus käivitab serveri alamprotsessina ja suhtleb läbi standardse sisendi/väljundi. Kasulik failisüsteemile juurdepääsuks või käsureatööriistade jaoks.

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

`@modelcontextprotocol/server-filesystem` server pakub järgmisi tööriistu, kõik piiratud sinu määratud kataloogidega:

| Tööriist | Kirjeldus |
|----------|-----------|
| `read_file` | Loe ühe faili sisu |
| `read_multiple_files` | Loe mitu faili korraga |
| `write_file` | Loo või kirjuta fail üle |
| `edit_file` | Tee sihipäraseid otsi-ja-asenda parandusi |
| `list_directory` | Loetle failid ja kataloogid teekonnal |
| `search_files` | Rekursiivselt otsi faile, mis vastavad mustrile |
| `get_file_info` | Saa faili metaandmed (suurus, ajastused, õigused) |
| `create_directory` | Loo kataloog (kaasas ka peamiskataloogid) |
| `move_file` | Liiguta või ümbernimeta fail või kataloog |

Allolev joonis näitab, kuidas Stdio transport töötab jooksuajal — sinu Java rakendus käivitab MCP serveri alamprotsessina ja nad suhtlevad stdin/stdout torude kaudu, ilma võrguta või HTTP-ta:

<img src="../../../translated_images/et/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio transport töös — sinu rakendus käivitab MCP serveri alamprotsessina ja suhtleb stdin/stdout torude kaudu.*

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chatiga:** Ava [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) ja küsi:
> - "Kuidas Stdio transport töötab ja millal seda HTTP-ga võrreldes kasutada?"
> - "Kuidas haldab LangChain4j MCP serveriprotsesside elutsüklit?"
> - "Millised on AI failisüsteemile juurdepääsu turvalisuse aspektid?"

## Agentne moodul

Kuigi MCP pakub standardiseeritud tööriistu, pakub LangChain4j **agentne moodul** deklaratiivset viisi agentide loomisel, kes orkestreerivad neid tööriistu. `@Agent` annotatsioon ja `AgenticServices` võimaldavad määratleda agentsi käitumist liideste kaudu, mitte imperatiivse koodina.

Selles moodulis uurid **Järelevalveagendi** mustrit — arenenud agentset AI lähenemist, kus „järelevalve“ agent otsustab dünaamiliselt, milliseid subagente kasutaja päringute põhjal kutsuda. Ühendame need mõlemad, andes ühele meie subagentidest MCP-võimsusega failijuurdepääsu.

Agentse mooduli kasutamiseks lisa see Maven sõltuvus:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Märkus:** `langchain4j-agentic` moodul kasutab eraldi versiooni omadust (`langchain4j.mcp.version`), sest see vabastatakse teistsuguse ajakavaga kui LangChain4j tuumikraamatukogud.

> **⚠️ Katseversioon:** `langchain4j-agentic` moodul on **katseversioon** ja võib muutuda. Stabiilne viis AI assistentide loomiseks jääb olema `langchain4j-core` koos kohandatud tööriistadega (Moodul 04).

## Näidete käivitamine

### Eeltingimused

- Läbitud [Moodul 04 - Tööriistad](../04-tools/README.md) (see moodul põhineb kohandatud tööriistade kontseptsioonil ja võrdleb neid MCP tööriistadega)
- `.env` fail juurkaustas Azure tõenditega (loodud `azd up`-ga Moodul 01-s)
- Java 21+, Maven 3.9+
- Node.js 16+ ja npm (MCP serverite jaoks)

> **Märkus:** Kui sa pole veel seadistanud oma keskkonnamuutujaid, vaata [Moodul 01 - Sissejuhatus](../01-introduction/README.md) juurutamisjuhiseid (`azd up` loob `.env` faili automaatselt), või kopeeri `.env.example` fail `.env`-ks juurkausta ja täida oma andmed.

## Kiire algus

**VS Code kasutamine:** Lihtsalt tee paremklõps mis tahes demo failil Exploreri paneelis ja vali **„Run Java“**, või kasuta Käivita ja Debug paneeli käivituskonfiguratsioone (veendu esmalt, et sinu `.env` fail oleks Azure tõenditega seadistatud).

**Maven kasutamine:** Alternatiivselt saad jooksutada käsurealt alljärgnevate näidetega.

### Failioperatsioonid (Stdio)

See demonstreerib kohalikul alamprotsessil põhinevaid tööriistu.

**✅ Eeltingimusi ei ole vaja** - MCP server käivitatakse automaatselt.

**Käivitusskriptide kasutamine (Soovitatav):**

Käivitusskriptid laadivad automaatselt keskkonnamuutujad juurkausta `.env` failist:

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

**VS Code kasutamine:** Tee paremklõps `StdioTransportDemo.java` peal ja vali **„Run Java“** (veendu, et sinu `.env` fail on seadistatud).

Rakendus käivitab failisüsteemi MCP serveri automaatselt ja loeb kohalikku faili. Pane tähele, kuidas alamprotsessi haldus toimub sinu eest.

**Oodatav väljund:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Järelevalveagent

**Järelevalveagendi muster** on **paindlik** agentse AI vorm. Järelevalve kasutab LLM-i, et autonoomselt otsustada, milliseid agente käivitada kasutaja päringu põhjal. Järgmises näites ühendame MCP-võimelise failijuurdepääsu LLM-agentiga, et luua juhitud faili lugemise → aruande töövoog.

Demos loeb `FileAgent` faili MCP failisüsteemi tööriistade abil ja `ReportAgent` genereerib struktureeritud aruande tegevjuhi kokkuvõtte (1 lause), 3 võtmetähtsusega punktiga ja soovitustega. Järelevalve orkestreerib selle töövoo automaatselt:

<img src="../../../translated_images/et/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Järelevalve kasutab oma LLM-i otsustamaks, milliseid agente kutsuda ja mis järjekorras — ei mingit koodipõhist marsruutimist.*

Siin on konkreetne töövoog meie failist aruandeni torujuhtmele:

<img src="../../../translated_images/et/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent loeb faili MCP tööriistade kaudu, siis ReportAgent teisendab tooresisu struktureeritud aruandeks.*

Järgmine järjestikuse diagramm jälgib kogu juhendava järelevalve orkestrimist — MCP serveri käivitamisest, läbi Järelevalve autonoomse agente valiku, kuni tööriistakõnede ja lõppraporti genereerimiseni üle stdio:

<img src="../../../translated_images/et/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*Järelevalve kutsub autonoomselt FileAgenti (kes kutsub MCP serverit üle stdio faili lugemiseks), siis kutsub ReportAgenti struktureeritud aruande tegemiseks — iga agent salvestab väljundi jagatud Agentse ulatuse mälu.*

Iga agent salvestab oma väljundi **Agentse ulatusse** (jagatud mällu), võimaldades alluvatel agentidel ligipääsu varasematele tulemustele. See demonstreerib, kuidas MCP tööriistad integreeruvad sujuvalt agentsetesse töövoogudesse — Järelevalve ei pea teadma *kuidas* faile loetakse, vaid ainult seda, et `FileAgent` suudab seda teha.

#### Demo käivitamine

Käivitusskriptid laadivad automaatselt keskkonnamuutujad juurkausta `.env` failist:

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

**VS Code kasutamine:** Tee paremklõps `SupervisorAgentDemo.java` peal ja vali **„Run Java“** (veendu, et sinu `.env` fail on seadistatud).

#### Kuidas järelevalve töötab

Enne agentide loomist pead MCP transpordi ühendama kliendiga ja mähkima selle `ToolProvider`-iks. See on, kuidas MCP serveri tööriistad muutuvad sinu agentidele kättesaadavaks:

```java
// Loo MCP klient transpordist
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Paki klient ümber ToolProvider'iks — see ühendab MCP tööriistad LangChain4j-ga
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Nüüd saad `mcpToolProvider` süstida igasse agendi, kes vajab MCP tööriistu:

```java
// 1. samm: FileAgent loeb faile, kasutades MCP tööriistu
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Omab MCP tööriistu failide haldamiseks
        .build();

// 2. samm: ReportAgent genereerib struktureeritud aruanded
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor organiseerib faili → aruande töövoo
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Tagastab lõpliku aruande
        .build();

// Supervisor otsustab päringu alusel, milliseid agente käivitada
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Kuidas FileAgent leiab MCP tööriistad jooksuajal

Võid imestada: **kuidas `FileAgent` teab, kuidas npm failisüsteemi tööriistu kasutada?** Vastus on, et ta ei tea — **LLM** leiab selle välja jooksuajal tööriistade skeemide alusel.

`FileAgent` liides on lihtsalt **käivitusdefinitsioon**. Tal pole eelnevalt kodeeritud teadmisi `read_file`, `list_directory` või muude MCP tööriistade kohta. Siin on, mis juhtumipõhiselt juhtub lõpuni välja:
1. **Server käivitamine:** `StdioMcpTransport` käivitab `@modelcontextprotocol/server-filesystem` npm paketi lasteprotsessina  
2. **Tööriistade avastamine:** `McpClient` saadab serverile `tools/list` JSON-RPC päringu, millele server vastab tööriistade nimede, kirjelduste ja parameetrite skeemidega (nt `read_file` — *"Loe faili kogu sisu"* — `{ path: string }`)  
3. **Skeemi süstimine:** `McpToolProvider` pakib need avastatud skeemid ja teeb need LangChain4j-le kättesaadavaks  
4. **LLM otsustab:** Kui kutsutakse `FileAgent.readFile(path)`, saadab LangChain4j süsteemi sõnumi, kasutaja sõnumi **ja tööriistade skeemide nimekirja** LLM-ile. LLM loeb tööriistade kirjeldusi ja genereerib tööriistakutse (nt `read_file(path="/some/file.txt")`)  
5. **Täideviimine:** LangChain4j püüab tööriistakutse kinni, suunab selle MCP kliendi kaudu Node.js alamprotsessile, saab tulemuse ja edastab selle tagasi LLM-ile  

See on sama eespool kirjeldatud [Tööriistade avastamine](../../../05-mcp) mehhanism, kuid rakendatud agentide töövoole. `@SystemMessage` ja `@UserMessage` annotatsioonid juhendavad LLM-i käitumist, samas kui süstitud `ToolProvider` annab talle **võimalused** — LLM ühendab need jooksuajal.  

> **🤖 Proovi koos [GitHub Copilot](https://github.com/features/copilot) Chatiga:** Ava [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) ja küsi:  
> - "Kuidas see agent teab, millist MCP tööriista kutsuda?"  
> - "Mis juhtuks, kui ma eemaldaksin ToolProvideri agentide ehitajast?"  
> - "Kuidas tööriistade skeemid LLM-ile edastatakse?"  

#### Vastuse strateegiad  

Kui seadistad `SupervisorAgent`, määrad, kuidas see peaks pärast alamagentide ülesannete täitmist kasutajale lõpliku vastuse formuleerima. Järgnev diagramm näitab kolme saadaolevat strateegiat — LAST tagastab viimase agendi väljundi otse, SUMMARY sünteesib kõik väljundid LLM-i abil ning SCORED valib selle, mille skoor on algse päringu suhtes kõrgem:  

<img src="../../../translated_images/et/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>  

*Kolm strateegiat, kuidas Supervisor vormistab lõpliku vastuse — vali vastavalt sellele, kas soovid viimase agendi väljundit, sünteesitud kokkuvõtet või kõrgeima skooriga valikut.*  

Saadaval strateegiad on:  

| Strateegia | Kirjeldus |  
|------------|-----------|  
| **LAST**   | Juhataja tagastab viimase alamagendi või tööriista tagastuse. Kasulik, kui töövoo viimane agent on spetsiaalselt loodud täieliku lõpliku vastuse andmiseks (nt "Kokkuvõtte Agent" uurimistöö torus). |  
| **SUMMARY** | Juhataja kasutab oma sisemist keelemudelit (LLM), et sünteesida kogu interaktsiooni ja kõigi alamagentide väljundite kokkuvõte ning tagastab selle lõpliku vastusena. See annab kasutajale puhta, koondatud vastuse. |  
| **SCORED** | Süsteem kasutab sisemist LLM-i nii LAST vastuse kui ka SUMMARY kokkusummutuse hindamiseks originaalsete kasutajapäringute suhtes ning tagastab selle väljundi, mille skoor on kõrgem. |  

Vaata täielikku rakendust failist [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java).  

> **🤖 Proovi koos [GitHub Copilot](https://github.com/features/copilot) Chatiga:** Ava [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) ja küsi:  
> - "Kuidas juhataja otsustab, milliseid agente kutsuda?"  
> - "Mis vahe on Supervisoril ja järjestikulisel töövoo mustril?"  
> - "Kuidas saan juhataja planeerimise käitumist kohandada?"  

#### Väljundi mõistmine  

Kui demo käivitad, näed struktureeritud ülevaadet, kuidas juhataja organiseerib mitut agenti. Iga osa tähendus on järgmine:  

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Päis** tutvustab töövoo mõistet: fokuseeritud toru failide lugemisest aruande genereerimiseni.  

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
  
**Töövoo diagramm** näitab andmevoogu agentide vahel. Igal agenil on kindel roll:  
- **FileAgent** loeb faile MCP tööriistade abil ja salvestab tooraine `fileContent` alla  
- **ReportAgent** kasutab seda sisu ja genereerib struktureeritud aruande `report` alla  

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Kasutajapäring** näitab ülesannet. Juhataja analüüsib seda ja otsustab kutsuda FileAgent → ReportAgent.  

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
  
**Juhataja orkestreerimine** näitab 2-astmelist voogu:  
1. **FileAgent** loeb faili MCP kaudu ja salvestab sisu  
2. **ReportAgent** saab sisu ja genereerib struktureeritud aruande  

Juhataja tegi need otsused **autonoomselt** kasutajapäringu põhjal.  

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
  
#### Agentset moodulit funktsioonide selgitus  

Näide demonstreerib mitmeid agentse mooduli arenenud funktsioone. Vaatleme lähemalt Agentset mälu ja Agent Listener‘eid.  

**Agentne mälu** näitab jagatud mälu, kuhu agentid salvestasid oma tulemused kasutades `@Agent(outputKey="...")`. See võimaldab:  
- Hilisematelt agentidelt varem tehtud väljunditele ligipääsu  
- Juhatajal sünteesida lõplikku vastust  
- Sul on võimalus uurida, mida iga agent tootis  

Järgmine diagramm näitab, kuidas agentne mälu toimib jagatud mäluna failist aruandeni töövoos — FileAgent kirjutab väljundi `fileContent` alla, ReportAgent loeb selle ja kirjutab oma väljundi `report` alla:  

<img src="../../../translated_images/et/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>  

*Agentne mälu toimib jagatud mäluna — FileAgent kirjutab `fileContent`, ReportAgent loeb ja kirjutab `report` ning su kood loeb lõpptulemust.*  

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Toorfaili andmed FileAgendilt
String report = scope.readState("report");            // Struktureeritud aruanne ReportAgendilt
```
  
**Agent Listenerid** võimaldavad jälgida ja siluda agentide täitmist. Demo samm-sammult väljund tuleb Agent Listener‘ist, mis haakub iga agentide kutsega:  
- **beforeAgentInvocation** – kutsutakse, kui juhataja valib agendi, võimaldades näha, milline agent valiti ja miks  
- **afterAgentInvocation** – kutsutakse, kui agent lõpetab, kuvades selle tulemuse  
- **inheritedBySubagents** – kui tõene, jälgib listener kõiki agente hierarhias  

Järgmine diagramm näitab täielikku Agent Listeneri elutsüklit, sh kuidas `onError` käitleb vigu agentide täitmisel:  

<img src="../../../translated_images/et/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>  

*Agent listenerid haakuvad täitmise elutsüklisse — jälgi, millal agent algab, lõpetab või tekib vigu.*  

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
        return true; // Levita kõigile alamagentidele
    }
};
```
  
Lisaks Supervisor mustrile pakub `langchain4j-agentic` moodul mitmeid võimsaid töövoo mustreid. Järgmine diagramm näitab kõiki viit — lihtsatest järjestikustest torudest kuni inimsekkusega kinnitustöövoogudeni:  

<img src="../../../translated_images/et/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>  

*Viis töövoo mustrit agentide orkestreerimiseks — lihtsatest järjestikustest torudest kuni inimsekkusega kinnitustöövoogudeni.*  

| Muster | Kirjeldus | Kasutusjuht |  
|--------|-----------|-------------|  
| **Järjestikune** | Käivita agentide järjestus, väljund voolab järgmisele | Torud: uurimine → analüüs → aruanne |  
| **Paralleel** | Käivita agentid samaaegselt | Sõltumatud ülesanded: ilm + uudised + börs |  
| **Silmus** | Korda kuni tingimus täidetud | Kvaliteedi hindamine: täienda kuni skoor ≥ 0.8 |  
| **Tingimuslik** | Suuna tingimuste alusel | Klassiﬁtseerimine → suuna spetsialistile |  
| **Inimene-lõksus** | Lisa inimlikud kontrollpunktid | Kinnitustöövood, sisu ülevaatus |  

## Peamised mõisted  

Nüüd, kus oled MCP ja agentse mooduli rakenduse läbi uurinud, võtame kokku, millal kumbagi lähenemist kasutada.  

Üks MCP suurimaid eeliseid on kasvav ökosüsteem. Järgmine diagramm näitab, kuidas üks universaalne protokoll ühendab sinu tehisintellekti rakenduse paljude MCP serveritega — alates failisüsteemi ja andmebaasi juurdepääsust kuni GitHubi, e-posti, veebikraapimise ja muuni:  

<img src="../../../translated_images/et/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>  

*MCP loob universaalse protokolli ökosüsteemi — iga MCP-ühilduv server töötab iga MCP-ühilduva kliendiga, võimaldades tööriistade jagamist rakenduste vahel.*  

**MCP** sobib ideaalselt, kui soovid kasutada olemasolevaid tööriistade ökosüsteeme, luua tööriistu, mida mitmed rakendused saavad jagada, integreerida kolmandate osapoolte teenuseid standardprotokollide abil või vahetada tööriista rakendusi ilma koodi muutmata.  

**Agentne moodul** sobib kõige paremini, kui soovid deklaratiivseid agentide definitsioone `@Agent` annotatsioonidega, vajad töövoo orkestreerimist (järjestikune, silmus, paralleel), eelistad liidestepõhist agentide disaini imperatiivse koodi asemel või kombineerid mitut agenti, kes jagavad väljundeid `outputKey` kaudu.  

**Supervisor Agent muster** paistab silma siis, kui töövoog ei ole eelnevalt prognoositav ja soovid, et LLM otsustaks, kui sul on mitu spetsialiseeritud agenti, kes vajavad dünaamilist orkestreerimist, kui ehitad vestlussüsteeme, mis suunavad erinevatele võimalustele, või kui soovid kõige paindlikumat ja kohanevamat agentide käitumist.  

Selleks, et aidata valida kohandatud `@Tool` meetodite (Moodul 04) ja MCP tööriistade (see moodul) vahel, toob järgmine võrdlus välja peamised kompromissid — kohandatud tööriistad annavad tiheda sidumise ja täieliku tüübiturvalisuse rakenduspõhise loogika jaoks, MCP tööriistad pakuvad standarditud ja taaskasutatavaid integratsioone:  

<img src="../../../translated_images/et/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>  

*Millal kasutada kohandatud @Tool meetodeid vs MCP tööriistu — kohandatud tööriistad rakenduspõhisele loogikale täieliku tüübiturvalisusega, MCP tööriistad standardiseeritud integratsioonide jaoks mitmetes rakendustes.*  

## Palju õnne!  

Oled läbinud kõigi viie LangChain4j algajate kursuse mooduli! Siin on ülevaade kogu õppeteekonnast — algsest vestlusest kuni MCP-toega agentsete süsteemideni:  

<img src="../../../translated_images/et/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>  

*Sinu õppeteekond kõigi viie mooduli jooksul — põhivestlusest MCP-toega agentsete süsteemideni.*  

Oled lõpetanud LangChain4j algajate kursuse ja õppinud:  

- Kuidas ehitada vestluslikku tehisintellekti mälu abil (Moodul 01)  
- Sisselülitusmustrid erinevate ülesannete jaoks (Moodul 02)  
- Kuidas siduda vastuseid dokumentidega RAG abil (Moodul 03)  
- Põhiliste AI agentide (assistendide) loomine kohandatud tööriistadega (Moodul 04)  
- Standardiseeritud tööriistade integreerimine LangChain4j MCP ja agentse mooduliga (Moodul 05)  

### Mis edasi?  

Moodulite läbimise järel tutvu [Testimise juhendiga](../docs/TESTING.md), et näha LangChain4j testimise kontseptsioone praktikas.  

**Ametlikud ressursid:**  
- [LangChain4j dokumentatsioon](https://docs.langchain4j.dev/) – põhjalikud juhendid ja API referencia  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) – lähtekood ja näited  
- [LangChain4j õpetused](https://docs.langchain4j.dev/tutorials/) – samm-sammult juhendid erinevate kasutusjuhtude jaoks  

Täname, et lõpetasid selle kursuse!  

---  

**Navigeerimine:** [← Eelmine: Moodul 04 - Tööriistad](../04-tools/README.md) | [Tagasi põhilehele](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Lahtiütlus**:
See dokument on tõlgitud kasutades tehisintellekti tõlke teenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi püüame täpsust, palun pidage meeles, et automaatsed tõlked võivad sisaldada vigu või ebatäpsusi. Originaaldokument selle emakeeles tuleks pidada autoriteetseks allikaks. Olulise teabe korral soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta selle tõlke kasutamisest tekkida võivate arusaamatuste või valesti mõistmiste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->