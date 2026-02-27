# Moodul 05: Mudeli konteksti protokoll (MCP)

## Sisukord

- [Mida sa õpid](../../../05-mcp)
- [Mis on MCP?](../../../05-mcp)
- [Kuidas MCP töötab](../../../05-mcp)
- [Agentne moodul](../../../05-mcp)
- [Näidete käivitamine](../../../05-mcp)
  - [Eeltingimused](../../../05-mcp)
- [Kiire algus](../../../05-mcp)
  - [Failioperatsioonid (Stdio)](../../../05-mcp)
  - [Järelevalve agent](../../../05-mcp)
    - [Demo jooksutamine](../../../05-mcp)
    - [Kuidas järelevalve töötab](../../../05-mcp)
    - [Vastusstrateegiad](../../../05-mcp)
    - [Väljundi mõistmine](../../../05-mcp)
    - [Agentse mooduli funktsioonide seletus](../../../05-mcp)
- [Põhimõisted](../../../05-mcp)
- [Palju õnne!](../../../05-mcp)
  - [Mis järgmiseks?](../../../05-mcp)

## Mida sa õpid

Oled loonud vestlusliku tehisintellekti, valdanud promte, sidunud vastused dokumentidega ja loonud agendid tööriistadega. Kuid need tööriistad olid kõik sinu konkreetse rakenduse jaoks kohandatud. Mis siis, kui saaksid anda oma tehisintellektile juurdepääsu standardiseeritud tööriistade ökosüsteemile, mida igaüks saab luua ja jagada? Selles moodulis õpidki seda tegema Mudeli konteksti protokolli (MCP) ja LangChain4j agentse mooduli abil. Esmalt demonstreerime lihtsat MCP faililugejat ja seejärel näitame, kuidas see lihtsalt integreerub täiustatud agentsetesse töövoogudesse kasutades järelevalve agendi mustrit.

## Mis on MCP?

Mudeli konteksti protokoll (MCP) pakub just seda - standardset viisi AI rakendustel avastada ja kasutada väliseid tööriistu. Selle asemel, et kirjutada kohandatud integratsioone iga andmeallika või teenuse jaoks, ühendud MCP serveritega, mis avaldavad oma võimalused ühtses formaadis. Sinu tehisintellekti agent saab siis need tööriistad automaatselt avastada ja kasutada.

<img src="../../../translated_images/et/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Enne MCP-d: keerukad otseühendused. Pärast MCP-d: üks protokoll, lõputud võimalused.*

MCP lahendab AI arenduses põhimõttelise probleemi: iga integratsioon on kohandatud. Tahad GitHubi kasutada? Kohandatud kood. Tahad faile lugeda? Kohandatud kood. Tahad pärida andmebaasi? Kohandatud kood. Ja ükski neist integratsioonidest ei toimi teiste AI rakendustega.

MCP standardiseerib selle. MCP server avaldab tööriistad selgete kirjelduste ja skeemidega. Iga MCP klient saab ühendada, avastada saadavalolevad tööriistad ja neid kasutada. Ehita kord, kasuta kõikjal.

<img src="../../../translated_images/et/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Mudeli konteksti protokolli arhitektuur - standardiseeritud tööriistade avastamine ja täitmine*

## Kuidas MCP töötab

<img src="../../../translated_images/et/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Kuidas MCP töötab - kliendid avastavad tööriistu, vahetavad JSON-RPC sõnumeid ja täidavad toiminguid transpordi kihi kaudu.*

**Serveri-Kliendi arhitektuur**

MCP kasutab kliendi-serveri mudelit. Serverid pakuvad tööriistu - failide lugemist, andmebaasi päringuid, API kutsed. Kliendid (sinu AI rakendus) ühenduvad serveritega ja kasutavad nende tööriistu.

MCP kasutamiseks koos LangChain4j-ga lisa see Maven sõltuvus:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```
  
**Tööriistade avastamine**

Kui sinu klient ühendub MCP serveriga, küsib ta: "Milliseid tööriistu sul on?" Server vastab saadaval olevate tööriistade nimekirjaga, igaühel kirjeldus ja parameetrite skeemid. Sinu AI agent saab siis otsustada, milliseid tööriistu kasutaja päringu põhjal kasutada.

<img src="../../../translated_images/et/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI avastab käivitamisel saadaval olevad tööriistad — nüüd teab, millised võimalused on saadaval ja saab valida, milliseid kasutada.*

**Transpordimehhanismid**

MCP toetab erinevaid transpordimehhanisme. Selles moodulis demonstreeritakse Stdio transpordimeetodit kohalike protsesside jaoks:

<img src="../../../translated_images/et/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP transpordimehhanismid: HTTP kaugserverite jaoks, Stdio kohalike protsesside jaoks*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Kohalike protsesside jaoks. Sinu rakendus käivitab serveri alamprotsessina ja suhtleb standardse sisendi/väljundi kaudu. Kasulik failisüsteemile ligipääsuks või käsureatööriistadele.

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
  
<img src="../../../translated_images/et/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio transport tegevuses — sinu rakendus käivitab MCP serveri lapseprotsessina ja suhtleb stdin/stdout torude kaudu.*

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chatiga:** Ava [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) ja küsi:
> - "Kuidas Stdio transport töötab ja millal peaksin seda kasutama võrreldes HTTP-ga?"
> - "Kuidas haldab LangChain4j MCP serveriprotsesside elutsüklit?"
> - "Millised on turvariskid, kui anda AI-le failisüsteemile juurdepääs?"

## Agentne moodul

Kuigi MCP pakub standardiseeritud tööriistu, pakub LangChain4j **agentne moodul** deklaratiivset viisi agentide loomiseks, kes orkestreerivad neid tööriistu. `@Agent` annotatsioon ja `AgenticServices` võimaldavad defineerida agendi käitumist liideste kaudu, mitte imperatiivse koodi kaudu.

Selles moodulis uurid **Järelevalve agendi** mustrit — arenenud agentset AI lähenemist, kus "järelevalve" agent otsustab dünaamiliselt, milliseid alaagente kasutaja päringu põhjal kutsuda. Kombineerime mõlemad kontseptsioonid, andes ühele meie alaagentidest MCP-toega failide ligipääsu võimed.

Agentse mooduli kasutamiseks lisa see Maven sõltuvus:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
  
> **⚠️ Eksperimentaalne:** `langchain4j-agentic` moodul on **eksperimentaalne** ja võib muutuda. Stabiilne viis AI assistentide ehitamiseks jääb `langchain4j-core` koos kohandatud tööriistadega (Moodul 04).

## Näidete käivitamine

### Eeltingimused

- Java 21+, Maven 3.9+
- Node.js 16+ ja npm (MCP serverite jaoks)
- Keskkonnamuutujad seadistatud `.env` failis (juurkaustast):
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (nagu Moodulites 01-04)

> **Märkus:** Kui sa pole oma keskkonnamuutujaid veel seadistanud, vaata [Moodul 00 - Kiire algus](../00-quick-start/README.md) juhiseid või kopeeri `.env.example` fail `.env` faili juurkaustas ja täida oma väärtused.

## Kiire algus

**VS Code kasutamine:** Lihtsalt paremklõpsa mõnel demo failil Exploreris ja vali **"Run Java"** või kasuta käivituskonfiguratsioone Run and Debug paneelil (veendu, et oled oma tokeni `.env` faili lisanud).

**Maveni kasutamine:** Alternatiivina võid käivitada näited käsurealt alljärgnevalt.

### Failioperatsioonid (Stdio)

See demonstreerib kohalikke alamprotsessidel põhinevaid tööriistu.

**✅ Eeltingimusi pole vaja** - MCP server käivitub automaatselt.

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
  
**VS Code kasutamine:** Paremklõpsa `StdioTransportDemo.java` failil ja vali **"Run Java"** (veendu, et `.env` fail on õigesti seadistatud).

Rakendus käivitab automaatselt failisüsteemi MCP serveri ja loeb kohalikku faili. Märka, kuidas alamprotsesside haldus on sinu eest tehtud.

**Oodatav väljund:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```
  
### Järelevalve agent

**Järelevalve agendi muster** on **paindlik** viis agentseks AI-ks. Järelevalve kasutab LLM-i, et iseseisvalt otsustada, milliseid agente kutsuda vastavalt kasutaja päringule. Järgmises näites kombineerime MCP-toega failide ligipääsu LLM agendiga, luues järelevalve all faililugemise → aruande töövoo.

Demos loeb `FileAgent` faili MCP failisüsteemi tööriistadega ja `ReportAgent` genereerib struktureeritud aruande koos juhtemomendiga (1 lause), 3 peamise punktiga ja soovitustega. Järelevalve juhib seda voogu automaatselt:

<img src="../../../translated_images/et/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Järelevalve kasutab oma LLM-i, et otsustada, milliseid agente ja mis järjekorras kutsuda — kõvade reeglite koodita.*

Siin on konkreetne töövoog meie failist aruandeni torujuhtmele:

<img src="../../../translated_images/et/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent loeb faili MCP tööriistade kaudu, seejärel ReportAgent muudab toore sisu struktuurseks aruandeks.*

Iga agent salvestab oma väljundi **Agentse ulatuse** (jagatud mälu), võimaldades alluvatel agentidel varasemaid tulemusi kasutada. See demonstreerib, kuidas MCP tööriistad sujuvalt agentsetesse töövoogudesse integreeruvad — Järelevalve ei pea teadma *kuidas* faile loetakse, vaid ainult, et `FileAgent` suudab seda teha.

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
  
**VS Code kasutamine:** Paremklõpsa `SupervisorAgentDemo.java` failil ja vali **"Run Java"** (veendu, et `.env` fail on seadistatud).

#### Kuidas järelevalve töötab

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

// Juht koordineerib faili → aruande töövoogu
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Tagastab lõpliku aruande
        .build();

// Juht otsustab, milliseid agente päringu põhjal kutsuda
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```
  
#### Vastusstrateegiad

Kui seadistad `SupervisorAgent`-i, määrad, kuidas ta kasutajale lõpliku vastuse formuleerib pärast alaagentide töö lõpetamist.

<img src="../../../translated_images/et/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Kolm strateegiat, kuidas Järelevalve esitab lõpliku vastuse — vali, kas soovid viimase agendi väljundit, sünteesitud kokkuvõtet või parima skooriga valikut.*

Saadaval strateegiad on:

| Strateegia | Kirjeldus |
|------------|-----------|
| **LAST** | Järelevalve tagastab viimase subagendi või tööriista väljundi. See sobib, kui töövoo lõplik agent on spetsiaalselt loodud lõpplahenduse andmiseks (nt "Kokkuvõtte agent" uurimistöö torujuhtmes). |
| **SUMMARY** | Järelevalve kasutab oma sisemist keelemudelit (LLM), et sünteesida kokkuvõte kogu suhtlusest ja kõigist alaagentide vastustest ning tagastab selle kokkuvõtte lõpliku vastusena. See annab kasutajale puhta ja koondatud vastuse. |
| **SCORED** | Süsteem kasutab sisemist LLM-i, et skoorida nii VIIMAST vastust kui ka KOKKUVÕTET kasutaja algse päringu alusel ning tagastab selle väljundi, millel on kõrgem skoor. |

Vaata täielikku implementatsiooni [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) failist.

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chatiga:** Ava [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) ja küsi:
> - "Kuidas Järelevalve otsustab, milliseid agente kutsuda?"
> - "Mis vahe on Järelevalve ja järjestikuse töövoo mustrite vahel?"
> - "Kuidas saan kohandada Järelevalve planeerimis käitumist?"

#### Väljundi mõistmine

Demot käivitades näed struktureeritud ülevaadet, kuidas Järelevalve juhib mitut agenti. Siin on, mida iga sektsioon tähendab:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Päis** tutvustab töövoo kontseptsiooni: keskendunud torujuhe failist aruande genereerimiseni.

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
  
**Töövoo diagramm** kuvab andmevoo agentide vahel. Igal agendil on konkreetne roll:
- **FileAgent** loeb faile MCP tööriistadega ja salvestab toore sisu `fileContent`-i
- **ReportAgent** kasutab seda sisu ja toodab struktureeritud aruande `report`-i

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Kasutaja päring** näitab ülesannet. Järelevalve analüüsib seda ja otsustab kutsuda FileAgent → ReportAgent.

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
  
**Järelevalve orkestreerimine** kuvab 2-astmelise voo tegevuses:
1. **FileAgent** loeb faili MCP kaudu ja salvestab sisu
2. **ReportAgent** võtab sisu vastu ja genereerib struktureeritud aruande

Järelevalve tegi need otsused **autonoomselt** kasutaja päringu põhjal.

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
  
#### Agentse mooduli funktsioonide seletus

Näide demonstreerib mitmeid agentse mooduli arenenud funktsioone. Vaatame lähemalt Agentic Scope ja Agent Listenerid.

**Agentic Scope** näitab jagatud mäluruumi, kuhu agendid salvestavad oma tulemusi kasutades `@Agent(outputKey="...")`. See võimaldab:
- Hilisematel agentidel pääseda ligi varasemate agentide väljunditele
- Järelevalvel sünteesida lõplikku vastust
- Sul on võimalik uurida, mida iga agent tootis

<img src="../../../translated_images/et/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope toimib jagatud mäluna — FileAgent kirjutab `fileContent`i, ReportAgent loeb seda ja kirjutab `report`i ning sinu kood loeb lõpplahendust.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Pärisfaili andmed FileAgentilt
String report = scope.readState("report");            // Struktureeritud aruanne ReportAgentilt
```
  
**Agent Listenerid** võimaldavad jälgida ja siluda agentide täitmist. Demos näha olev samm-sammuline väljund pärineb AgentListenerist, mis on ühendatud iga agendi kutsesse:
- **beforeAgentInvocation** - Kutsutakse, kui juhendaja valib agendi, võimaldades näha, milline agent valiti ja miks
- **afterAgentInvocation** - Kutsutakse, kui agent lõpetab, näidates selle tulemust
- **inheritedBySubagents** - Kui tõene, jälgib kuulaja kõiki agente hierarhias

<img src="../../../translated_images/et/agent-listeners.784bfc403c80ea13.webp" alt="Agentide kuulajate elutsükkel" width="800"/>

*Agentide kuulajad ühinevad täitmise elutsükliga — jälgivad, millal agent alustab, lõpetab või kogeb vigu.*

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

Lisaks juhendaja mustrile pakub moodul `langchain4j-agentic` mitmeid võimsaid töövoo mustreid ja funktsioone:

<img src="../../../translated_images/et/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agentide töövoo mustrid" width="800"/>

*Viis töövoo mustrit agentide orkestreerimiseks — alates lihtsatest järjestikustest torujuhtmetest kuni inimese osalusega kinnitustöövoogudeni.*

| Muster | Kirjeldus | Kasutusjuhtum |
|---------|-------------|----------|
| **Järjestikune** | Täida agentide toimingud järjest, väljund läheb järgmisele | Torujuhtmed: uurimine → analüüs → aruanne |
| **Paralleelne** | Käivita agentide toimingud samaaegselt | Sõltumatud ülesanded: ilm + uudised + aktsiad |
| **Tsükkel** | Korda seni, kuni tingimus on täidetud | Kvaliteedi hindamine: täiusta seni, kuni hinne ≥ 0.8 |
| **Tingimuslik** | Määra marsruut tingimuste alusel | Klassifitseeri → suuna spetsialistile |
| **Inimene-silmuses** | Lisa inim-punktid kinnituseks | Kinnitustöövood, sisukontroll |

## Peamised mõisted

Nüüd, kui oled uurinud MCP ja agentic mooduli tööd, võrdleme, millal kasutada kumbagi lähenemist.

<img src="../../../translated_images/et/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP ökosüsteem" width="800"/>

*MCP loob universaalse protokollide ökosüsteemi — iga MCP-ga ühilduv server töötab mis tahes MCP-ga ühilduva kliendiga, võimaldades tööriistade jagamist rakenduste vahel.*

**MCP** sobib ideaalselt, kui soovid kasutada olemasolevaid tööriistade ökosüsteeme, luua tööriistu, mida saab jagada mitme rakenduse vahel, integreerida kolmandate osapoolte teenuseid standardprotokollidega või vahetada tööriistade teostusi ilma koodi muutmata.

**Agentic Moodul** töötab kõige paremini, kui soovid deklaratiivseid agentide definitsioone koos `@Agent` annotatsioonidega, vajad töövoo orkestreerimist (järjestikune, tsükkel, paralleel), eelistad liidese-põhist agentide kavandamist käsurealisele koodile või kombineerid mitu agenti, mis jagavad väljundeid `outputKey` kaudu.

**Juhendaja tapattern** paistab silma, kui töövoog ei ole ette ennustatav ja soovid, et LLM otsustaks, kui sul on mitu spetsialiseerunud agenti, kes vajavad dünaamilist orkestreerimist, vestlussüsteemide loomisel, mis juhivad erinevatele võimetele või kui soovid kõige paindlikumat ja kohanduvamat agentide käitumist.

<img src="../../../translated_images/et/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Kohandatud tööriistad vs MCP tööriistad" width="800"/>

*Millal kasutada kohandatud @Tool meetodeid vs MCP tööriistu — kohandatud tööriistad rakenduse spetsiifilise loogika jaoks täis tüübiturvalisusega, MCP tööriistad standardiseeritud integreerimiseks, mis toimivad rakenduste piiril.*

## Palju õnne!

<img src="../../../translated_images/et/course-completion.48cd201f60ac7570.webp" alt="Kursuse lõpetamine" width="800"/>

*Sinu õpiteekond läbi kõigi viie mooduli — alates põhilisest vestlusest kuni MCP-toega agenticsüsteemideni.*

Oled lõpetanud kursuse LangChain4j algajatele. Sa õppisid:

- Kuidas ehitada mäluga vestluslikku tehisintellekti (Moodul 01)
- Sõelumisoskuse mustrid erinevate ülesannete jaoks (Moodul 02)
- Vastuste sidumine sinu dokumentidega RAG abil (Moodul 03)
- Elementaarsete AI agentide (assistendid) loomine kohandatud tööriistadega (Moodul 04)
- Standardiseeritud tööriistade integreerimine LangChain4j MCP ja Agentic moodulitega (Moodul 05)

### Mis edasi?

Pärast moodulite lõpetamist vaata [Testimise juhendit](../docs/TESTING.md), et näha LangChain4j testimise kontseptsioone praktikas.

**Ametlikud ressursid:**
- [LangChain4j dokumentatsioon](https://docs.langchain4j.dev/) - Üksikasjalikud juhendid ja API viide
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Algkood ja näited
- [LangChain4j õpikud](https://docs.langchain4j.dev/tutorials/) - Samm-sammult juhendid erinevates kasutusjuhtudes

Täname, et lõpetasid selle kursuse!

---

**Navigeerimine:** [← Eelmine: Moodul 04 - Tööriistad](../04-tools/README.md) | [Tagasi avalehhele](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Eitus**:
See dokument on tõlgitud kasutades tehisintellektil põhinevat tõlketeenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi püüame täpsust, palun arvestage, et automatiseeritud tõlked võivad sisaldada vigu või ebatäpsusi. Originaaldokument selles algkeeles tuleks pidada autoritatiivseks allikaks. Tähtsa info puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta missuguste arusaamatuste või väärtõlgenduste eest, mis võivad tekkida selle tõlke kasutamisest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->