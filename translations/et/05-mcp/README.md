# Moodul 05: Mudeli kontekstiprotokoll (MCP)

## Sisukord

- [Mida sa õpid](../../../05-mcp)
- [Mis on MCP?](../../../05-mcp)
- [Kuidas MCP töötab](../../../05-mcp)
- [Agentne moodul](../../../05-mcp)
- [Näidete käivitamine](../../../05-mcp)
  - [Eeldused](../../../05-mcp)
- [Kiire algus](../../../05-mcp)
  - [Failioperatsioonid (Stdio)](../../../05-mcp)
  - [Järelevalveagent](../../../05-mcp)
    - [Demo käivitamine](../../../05-mcp)
    - [Kuidas järelevalve töötab](../../../05-mcp)
    - [Vastusstrateegiad](../../../05-mcp)
    - [Väljundi mõistmine](../../../05-mcp)
    - [Agentse mooduli funktsioonide selgitus](../../../05-mcp)
- [Põhimõisted](../../../05-mcp)
- [Õnnitlused!](../../../05-mcp)
  - [Mis edasi?](../../../05-mcp)

## Mida sa õpid

Oled ehitanud konversatsioonilist tehisintellekti, valdanud käivituspäringuid, maandanud vastuseid dokumentidesse ja loonud agente koos tööriistadega. Kuid kõik need tööriistad olid spetsiaalselt sinu rakenduse jaoks kohandatud. Mis oleks, kui saaksid anda oma tehisintellektile juurdepääsu standardiseeritud tööriistade ökosüsteemile, mida igaüks võib luua ja jagada? Selles moodulis õpid, kuidas teha just seda Mudeli kontekstiprotokolli (MCP) ja LangChain4j agentse mooduli abil. Esmalt tutvustame lihtsat MCP faililugejat ja seejärel näitame, kuidas see hõlpsasti integreerub arenenud agentsetesse töövoogudesse, kasutades järelevalveagendi mustrit.

## Mis on MCP?

Mudeli kontekstiprotokoll (MCP) pakub täpselt seda – standardset viisi tehisintellekti rakenduste väljaspool asuvate tööriistade avastamiseks ja kasutamiseks. Kui senini tuli iga andmeallika või teenuse jaoks kirjutada kohandatud integratsioon, siis MCP kaudu ühendada MCP serveritega, mis avaldavad oma võimekused ühtses formaadis. Sinu AI agent saab need tööriistad automaatselt üles leida ja kasutada.

Järgmine skeem näitab erinevust – ilma MCP-ta nõuab iga integratsioon kohandatud punkt-punkti ühendusi; MCP korral ühendab üks protokoll sinu rakenduse mis tahes tööriistaga:

<img src="../../../translated_images/et/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Enne MCP-d: keerulised punkt-punkti integratsioonid. Pärast MCP-d: üks protokoll, lõputud võimalused.*

MCP lahendab tehisintellekti arenduse põhiprobleemi: iga integratsioon on kohandatud. Tahad ligi pääseda GitHubile? Kohandatud kood. Tahad faile lugeda? Kohandatud kood. Tahad pärida andmebaasi? Kohandatud kood. Ja ükski nendest integratsioonidest ei tööta teiste AI rakendustega.

MCP standardiseerib selle. MCP server avaldab tööriistu selgete kirjelduste ja skeemidega. Iga MCP klient saab ühendada, avastada saadaolevad tööriistad ja neid kasutada. Ehita korra, kasuta kõikjal.

Järgmine skeem illustreerib seda arhitektuuri – üks MCP klient (sinu AI rakendus) ühendub mitme MCP serveriga, millest igaüks pakub oma tööriistakomplekti standardprotokolli kaudu:

<img src="../../../translated_images/et/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Mudeli kontekstiprotokolli arhitektuur – standardiseeritud tööriistade avastamine ja käitamine*

## Kuidas MCP töötab

Sisemiselt kasutab MCP kihilist arhitektuuri. Sinu Java rakendus (MCP klient) avastab saadaolevad tööriistad, saadab JSON-RPC päringuid transpordikihi (Stdio või HTTP) kaudu ja MCP server täidab toiminguid ning tagastab tulemused. Järgmine skeem jagab protokolli kihid lahti:

<img src="../../../translated_images/et/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Kuidas MCP töötab sisemiselt – kliendid avastavad tööriistu, vahetavad JSON-RPC sõnumeid ja täidavad toiminguid transpordikihi kaudu.*

**Server-kliendi arhitektuur**

MCP kasutab klient-server mudelit. Serverid pakuvad tööriistu – failide lugemine, andmebaaside pärimine, API-de kutsumine. Kliendid (sinu AI rakendus) ühenduvad serveritega ja kasutavad nende tööriistu.

MCP kasutamiseks LangChain4j-ga lisa see Maven sõltuvus:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Tööriistade avastamine**

Kui sinu klient ühendub MCP serveriga, küsib ta: „Millised tööriistad sul on?“ Server vastab saadaolevate tööriistade nimekirjaga, igaühel kirjeldused ja parameetrite skeemid. Sinu AI agent saab seejärel kasutaja päringute alusel otsustada, milliseid tööriistu kasutada. Alloleval skeemil on see käepigistus – klient saadab `tools/list` päringu ja server tagastab oma tööriistad kirjelduste ja parameetrite skeemidega:

<img src="../../../translated_images/et/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI avastab käivitamisel saadaolevad tööriistad – ta teab nüüd, millised võimalused on olemas ja saab otsustada, mida kasutada.*

**Transpordimehhanismid**

MCP toetab eri transpordimehhanisme. Valikud on Stdio (kohaliku alaprotsessi suhtlus) ja Streamable HTTP (kaugserverid). See moodul demonstreerib Stdio transporti:

<img src="../../../translated_images/et/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP transpordimehhanismid: HTTP kaugserverite jaoks, Stdio kohalike protsesside jaoks*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Kohalike protsesside jaoks. Sinu rakendus käivitab serveri alaprotsessina ja suhtleb läbi standardse sisendi/väljundi. Sobib failisüsteemi juurdepääsu või käsureatööriistade jaoks.

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

`@modelcontextprotocol/server-filesystem` server pakub järgmisi tööriistu, mis kõik on sulgudega määratud kaustadele, mida sa määrad:

| Tööriist | Kirjeldus |
|----------|-----------|
| `read_file` | Ühe faili sisu lugemine |
| `read_multiple_files` | Mitme faili lugemine ühe päringuga |
| `write_file` | Faili loomine või ülekirjutamine |
| `edit_file` | Sihtotstarbelised otsi-ja-asenda muudatused |
| `list_directory` | Failide ja kaustade nimekirjade kuvamine teel |
| `search_files` | Failide rekursiivne otsing mustri järgi |
| `get_file_info` | Faili metaandmed (suurus, ajatemplid, õigused) |
| `create_directory` | Kausta loomine (ka vanemkaustad) |
| `move_file` | Faili või kausta liigutamine või ümbernimetamine |

Järgmine skeem näitab, kuidas Stdio transport töötab käitamise ajal – sinu Java rakendus käivitab MCP serveri lapseprotsessina ja nad suhtlevad stdin/stdout torude kaudu, võrgustikku ega HTTP-d ei kasutata:

<img src="../../../translated_images/et/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio transport tegevuses – sinu rakendus käivitab MCP serveri lapseprotsessina ja suhtleb stdin/stdout torude kaudu.*

> **🤖 Proovi koos [GitHub Copilot](https://github.com/features/copilot) Chatiga:** Ava [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) ja küsi:
> - "Kuidas Stdio transport töötab ja millal peaks seda kasutama HTTP asemel?"
> - "Kuidas LangChain4j haldab MCP serveriprotsesside elutsüklit?"
> - "Millised on turvariskid AI-le failisüsteemi ligipääsu andmisel?"

## Agentne moodul

Kui MCP pakub standardiseeritud tööriistu, siis LangChain4j **agentne moodul** võimaldab deklaratiivset moodi ehitada agente, kes korraldavad nende tööriistade kasutamist. `@Agent` annotatsioon ja `AgenticServices` võimaldavad määratleda agendi käitumist liideste kaudu, mitte imperatiivse koodina.

Selles moodulis uurid **järelevalveagendi** mustrit – arenenud agentset AI lähenemist, kus „järelevalve“ agent otsustab dünaamiliselt, milliseid alamagente kasutaja päringu põhjal kutsuda. Ühendame mõlemad – anname ühele alamagendile MCP-põhised failijuurdepääsu võimed.

Agentse mooduli kasutamiseks lisa see Maven sõltuvus:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Märkus:** `langchain4j-agentic` moodul kasutab eraldi versioonipropertit (`langchain4j.mcp.version`), sest see ilmub teistel aegadel kui põhilised LangChain4j teegid.

> **⚠️ Eksperimentaalne:** `langchain4j-agentic` moodul on **eksperimentaalne** ja võib muutuda. Stabiilne viis AI assistentide loomiseks jääb `langchain4j-core` koos kohandatud tööriistadega (moodul 04).

## Näidete käivitamine

### Eeldused

- Läbitud [Moodul 04 – Tööriistad](../04-tools/README.md) (see moodul tugineb kohandatud tööriistadele ja võrdleb neid MCP-ga)
- `.env` fail juurkaustas koos Azure volitustega (loodud `azd up` käsklusega Moodulis 01)
- Java 21+, Maven 3.9+
- Node.js 16+ ja npm (MCP serverite jaoks)

> **Märkus:** Kui sa pole veel seadistanud keskkonnamuutujaid, vaata [Moodul 01 – Sissejuhatus](../01-introduction/README.md) juurutusjuhiseid (`azd up` loob `.env` faili automaatselt), või kopeeri `.env.example` `.env` failiks juurkausta ja täida oma väärtused.

## Kiire algus

**VS Code kasutamisel:** Paremklõpsa Exploreris mistahes demo faili peal ja vali **„Run Java“** või kasuta käivituskonfiguratsioone Run and Debug paneelil (kindlusta esmalt `.env` faili Azure volitustega).

**Maven kasutamisel:** Või võid jooksutada ka käsurealt allolevate näidete abil.

### Failioperatsioonid (Stdio)

See demonstreerib kohaliku alaprotsessi-põhiseid tööriistu.

**✅ Eeldusi ei ole** - MCP server käivitub automaatselt.

**Käivita skriptide abil (soovitatav):**

Käivitusskriptid laadivad automaatselt keskkonnamuutujad juurikaustas olevast `.env` failist:

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

**VS Code’is:** Paremklõpsa `StdioTransportDemo.java` peal ja vali **„Run Java“** (kindlusta `.env` fail on seadistatud).

Rakendus käivitab automaatselt failisüsteemi MCP serveri ja loeb kohalikku faili. Märka, kuidas alaprotsessi haldus on sinu eest tehtud.

**Oodatud väljund:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Järelevalveagent

**Järelevalveagendi muster** on **paindlik** agentse AI vorm. Järelevalve kasutab suurt keelemudelit (LLM), et iseseisvalt otsustada, milliseid agente kutsuda kasutaja päringu põhjal. Järgmises näites ühendame MCP-põhise failijuurdepääsu ja LLM-agendi, luues järelevalve all oleva faili lugemise → aruande koostamise töövoo.

Demos loeb `FileAgent` faili, kasutades MCP failisüsteemi tööriistu, ja `ReportAgent` genereerib struktureeritud aruande koos kokkuvõttega (1 lause), 3 võtmeteemaga ja soovitustega. Järelevalve agent korraldab selle töövoo automaatselt:

<img src="../../../translated_images/et/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Järelevalve kasutab oma LLM-i, et otsustada, milliseid agente kutsuda ja mis järjekorras – pole vaja koodis ranget marsruutimist.*

Siin on meie faili–aruande töövoo konkreetne näide:

<img src="../../../translated_images/et/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent loeb faili MCP tööriistade kaudu, seejärel ReportAgent muudab tooresisu struktureeritud aruandeks.*

Iga agent salvestab oma väljundi **Agentse ulatuse** (jagamismälu), mis võimaldab järgmistel agentidel varasemaid tulemusi kasutada. See demonstreerib, kuidas MCP tööriistad integreeruvad sujuvalt agentsetesse töövoogudesse – järelevalve ei pea teadma, *kuidas* faile loetakse, vaid ainult, et `FileAgent` suudab seda teha.

#### Demo käivitamine

Käivitusskriptid laadivad automaatselt keskkonnamuutujad juurikaustas olevast `.env` failist:

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

**VS Code’is:** Paremklõpsa `SupervisorAgentDemo.java` peal ja vali **„Run Java“** (kindlusta `.env` fail on seadistatud).

#### Kuidas järelevalve töötab

Enne agentide loomist pead ühendama MCP transpordi kliendiga ja mähkima selle `ToolProvider` objektiks. See võimaldab MCP serveri tööriistadel saada sinu agentidele kättesaadavaks:

```java
// Loo MCP klient transpordist
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Seo klient ToolProvider-iks — see ühendab MCP tööriistad LangChain4j-ga
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Seejärel saad `mcpToolProvider` süstida igasse agenti, kes MCP tööriistu vajab:

```java
// Samm 1: FileAgent loeb faile, kasutades MCP tööriistu
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Omab MCP tööriistu failioperatsioonideks
        .build();

// Samm 2: ReportAgent genereerib struktureeritud aruandeid
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Juhataja koordineerib faili → aruande töövoogu
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Tagasta lõplik aruanne
        .build();

// Juhataja otsustab päringu põhjal, milliseid agente kutsuda
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Vastusstrateegiad

Kui konfigureerid `SupervisorAgent`i, määrad, kuidas ta formuleerib lõpliku vastuse kasutajale pärast subagentide ülesannete lõpetamist. Alloleval skeemil on kolm valikut – LAST tagastab viimase agendi väljundi otse, SUMMARY sünteesib kõik väljundid LLM-i abil kokkuvõtteks, ja SCORED valib parema skooriga vastuse algse päringu põhjal:

<img src="../../../translated_images/et/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Kolm strateegiat, kuidas järelevalve formuleerib lõpliku vastuse – vali, kas soovid viimase agendi väljundit, sünteesitud kokkuvõtet või parimat vastusevarianti.*

Saadaval strateegiad on:

| Strateegia | Kirjeldus |
|------------|-----------|
| **LAST** | Järelevalve tagastab viimase alamagendi või tööriista väljundi. Sobib olukordades, kus töövoo viimane agent on spetsiaalselt loodud täieliku lõpliku vastuse andmiseks (näiteks „Kokkuvõtte agent“ uurimistöö torustikus). |
| **SUMMARY** | Järelevalve kasutab oma sisemist keelemudelit (LLM) kogu suhtluse ja all-agentide väljundite kokkuvõtte sünteesiks ning tagastab selle kokkuvõtte lõpliku vastusena. Pakub kasutajale kena ja koondatud vastuse. |
| **SCORED** | Süsteem kasutab sisemist LLM-i nii LAST kui ka SUMMARY vastuste hindamiseks originaalpäringu suhtes ja tagastab selle väljundi, mille skoor on kõrgem. |
Vaata täielikku rakendust failist [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java).

> **🤖 Proovi koos [GitHub Copilot](https://github.com/features/copilot) Chat'iga:** Ava [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) ja küsi:
> - "Kuidas otsustab Supervisor, milliseid agente kutsuda?"
> - "Mis vahe on Supervisor ja Sequential töövoo mustritel?"
> - "Kuidas saan kohandada Supervisori planeerimise käitumist?"

#### Tulemuse mõistmine

Kui jooksutad demo, näed struktureeritud läbivaatust, kuidas Supervisor orkestreerib mitut agenti. Siin on, mida iga osa tähendab:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Päis** tutvustab töövoo kontseptsiooni: keskendunud torujuhe failide lugemisest aruande genereerimiseni.

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

**Töövoo diagramm** näitab andmevoogu agentide vahel. Iga agentil on konkreetne roll:
- **FileAgent** loeb faile MCP tööriistade abil ja salvestab toormaterjali `fileContent`-i
- **ReportAgent** kasutab seda sisu ja loob struktureeritud aruande `report`-i

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Kasutaja päring** näitab ülesannet. Supervisor analüüsib seda ja otsustab kutsuda FileAgent → ReportAgent.

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

**Supervisori orkestreerimine** näitab 2-sammulist toimingut:
1. **FileAgent** loeb faili MCP kaudu ja salvestab sisu
2. **ReportAgent** saab selle sisu ja loob struktureeritud aruande

Supervisor tegi need otsused **autonoomselt** kasutaja päringu põhjal.

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

#### Agentimooduli funktsioonide selgitus

Näide demonstreerib mitmeid agentimooduli täiustatud funktsioone. Vaatame lähemalt Agentic Scope ja Agent Listeners.

**Agentic Scope** näitab jagatud mälu, kuhu agentid salvestasid oma tulemused, kasutades `@Agent(outputKey="...")`. See võimaldab:
- Hilisematel agentidel ligi pääseda varasemate agentide väljunditele
- Supervisoril sünteesida lõplik vastus
- Sul inspekteerida, mida iga agent tootis

Järgmine diagramm näitab, kuidas Agentic Scope toimib jagatud mäluna failist aruandeni töövoos — FileAgent kirjutab väljundi võtme `fileContent` alla, ReportAgent loeb selle ja kirjutab oma väljundi võtme `report` alla:

<img src="../../../translated_images/et/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope toimib jagatud mäluna — FileAgent kirjutab `fileContent`, ReportAgent loeb seda ja kirjutab `report`, ja sinu kood loeb lõplikku tulemust.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Toorfaili andmed FileAgentilt
String report = scope.readState("report");            // Struktureeritud aruanne ReportAgentilt
```

**Agent Listeners** võimaldavad jälgida ja siluda agentide tööd. Sinu demoes nähtud samm-sammult väljund pärineb AgentListener’ist, mis on seotud iga agendi kutsumisega:
- **beforeAgentInvocation** – Kutsutakse, kui Supervisor valib agendi, võid näha, milline agent valiti ja miks
- **afterAgentInvocation** – Kutsutakse agendi lõpetamisel, kuvades selle tulemuse
- **inheritedBySubagents** – Kui on tõene, jälgib kuulaja kõiki hierarhias olevaid agente

Järgmine diagramm näitab täismahus Agent Listener elutsüklit, kaasa arvatud `onError`, mis käsitleb agentide töövigu:

<img src="../../../translated_images/et/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners haakuvad täitmistsükliga — jälgi, millal agent alustab, lõpetab või esineb vigu.*

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

Lisaks Supervisor mustrile pakub `langchain4j-agentic` moodul mitmeid võimsaid töövoo mustreid. Alljärgnev diagramm näitab kõiki viit — lihtsatest järjestikustest torujuhtmetest kuni inimtsükliga heakskiidu töövoogudeni:

<img src="../../../translated_images/et/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Viis töövoo mustrit agentide orkestreerimiseks — lihtsatest järjestikustest torujuhtmetest kuni inimtsükliga heakskiidu töövoogudeni.*

| Muster | Kirjeldus | Kasutusjuhtum |
|---------|-------------|----------|
| **Sequential** | Käivita agentid järjekorras, väljund voolab järgmisele | Torud: uurimine → analüüs → aruanne |
| **Parallel** | Käivita agentid samal ajal | Sõltumatud ülesanded: ilm + uudised + aktsiad |
| **Loop** | Korda seni, kuni tingimus täidetud | Kvaliteedi hindamine: täpsusta kuni skoor ≥ 0.8 |
| **Conditional** | Suuna tingimuste alusel | Klassifitseeri → suuna spetsialistagentile |
| **Human-in-the-Loop** | Lisa inimlikud kontrollpunktid | Heakskiidu töövood, sisu ülevaatus |

## Peamised mõisted

Nüüd, kus oled uurinud MCP ja agentimooduli toimimist, võtame kokku, millal kasutada kumbagi lähenemist.

Üks MCP suurimaid eeliseid on kasvav ökosüsteem. Järgmine diagramm näitab, kuidas üks universaalne protokoll ühendab sinu tehisintellekti rakenduse paljude MCP serveritega — failisüsteemidest ja andmebaasitest kuni GitHubi, e-posti, veebikraapimise ja muuni:

<img src="../../../translated_images/et/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP loob universaalse protokolli ökosüsteemi — iga MCP-ühilduv server töötab iga MCP-ühilduva kliendiga, võimaldades tööriistade jagamist rakenduste vahel.*

**MCP** on ideaalne, kui soovid kasutada olemasolevaid tööriistade ökosüsteeme, luua tööriistu, mida saavad jagada mitmed rakendused, integreerida kolmandate osapoolte teenuseid standardsete protokollidega või vahetada tööriistade rakendusi ilma koodi muutmata.

**Agentic Moodul** sobib kõige paremini, kui soovid deklaratiivseid agendi definitsioone `@Agent` annotatsioonidega, vajad töövoo orkestreerimist (järjestikune, tsükkel, paralleelne), eelistad liidese-põhist agentide disaini imperatiivse koodi asemel või kombineerid mitut agenti, kes jagavad väljundeid `outputKey` kaudu.

**Supervisor Agent mustrile** tuleb kasuks, kui töövoog ei ole eelnevalt ennustatav ja soovid, et LLM otsustaks, kui sul on mitu spetsialiseeritud agenti, kelle dünaamiline orkestreerimine on vajalik, kui ehitad vestlussüsteeme, mis suunavad erinevatele võimekustele, või kui soovid kõige paindlikumat, kohanemisvõimelist agendi käitumist.

Et aidata valida kohandatud `@Tool` meetodite (Moodul 04) ja MCP tööriistade (see moodul) vahel, toob järgmine võrdlus esile peamised kompromissid — kohandatud tööriistad annavad tiheda sideme ja täieliku tüübiturbe rakenduse spetsiifilise loogika jaoks, samas kui MCP tööriistad pakuvad standardiseeritud, taaskasutatavaid integratsioone:

<img src="../../../translated_images/et/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Millal kasutada kohandatud @Tool meetodeid vs MCP tööriistu — kohandatud tööriistad rakenduse spetsiifilise loogika jaoks täieliku tüübiturbega, MCP tööriistad standardiseeritud integratsioonide jaoks, mis töötavad üle rakenduste.*

## Palju õnne!

Sa jõudsid läbi kõik viis moodulit kursusel LangChain4j algajatele! Siin on pilk kogu õpiteekonnale, mida oled läbinud — algtaseme vestlusest kuni MCP-toega agentisüsteemideni:

<img src="../../../translated_images/et/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Sinu õpiteekond läbi viie mooduli — algtaseme vestlusest kuni MCP-toega agentisüsteemideni.*

Oled lõpetanud LangChain4j algajate kursuse. Sa õppisid:

- Kuidas ehitada vestluslikku tehisintellekti mäluga (Moodul 01)
- Käsuandmete kujundamise mustrid erinevate ülesannete jaoks (Moodul 02)
- Vastuste sidumine oma dokumentidega RAG abil (Moodul 03)
- Põhiliste AI agentide loomine (assistendid) kohandatud tööriistadega (Moodul 04)
- Standardiseeritud tööriistade integreerimine LangChain4j MCP ja Agentic moodulitega (Moodul 05)

### Mis järgmiseks?

Pärast moodulite lõpetamist uuri [Testimise juhendit](../docs/TESTING.md), et näha LangChain4j testimise kontseptsioone praktikas.

**Ametlikud ressursid:**
- [LangChain4j dokumentatsioon](https://docs.langchain4j.dev/) – põhjalikud juhendid ja API ülevaade
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) – lähtekood ja näited
- [LangChain4j Õpetused](https://docs.langchain4j.dev/tutorials/) – samm-sammult õpetused erinevate kasutusjuhtude jaoks

Täname, et läbisid selle kursuse!

---

**Navigatsioon:** [← Eelmine: Moodul 04 - Tööriistad](../04-tools/README.md) | [Tagasi avalehele](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:
See dokument on tõlgitud tehisintellekti tõlketeenuse [Co-op Translator](https://github.com/Azure/co-op-translator) abil. Kuigi püüame täpsust, tuleb arvestada, et automaatsed tõlked võivad sisaldada vigu või ebatäpsusi. Algne dokument selle emakeeles tuleks lugeda autoriteetseks allikaks. Olulise teabe puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta selle tõlke kasutamisest tingitud arusaamatuste või valesti mõistmiste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->