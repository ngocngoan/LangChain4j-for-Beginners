# 05 modulis: Modelio konteksto protokolas (MCP)

## Turinys

- [Ko išmoksite](../../../05-mcp)
- [Kas yra MCP?](../../../05-mcp)
- [Kaip veikia MCP](../../../05-mcp)
- [Agentinis modulis](../../../05-mcp)
- [Pavyzdžių paleidimas](../../../05-mcp)
  - [Reikalavimai](../../../05-mcp)
- [Greita pradžia](../../../05-mcp)
  - [Failų operacijos (Stdio)](../../../05-mcp)
  - [Supervisor agentas](../../../05-mcp)
    - [Demo paleidimas](../../../05-mcp)
    - [Kaip veikia Supervisor](../../../05-mcp)
    - [Atsakymų strategijos](../../../05-mcp)
    - [Rezultatų supratimas](../../../05-mcp)
    - [Agentinio modulio funkcijų paaiškinimas](../../../05-mcp)
- [Pagrindinės sąvokos](../../../05-mcp)
- [Sveikiname!](../../../05-mcp)
  - [Kas toliau?](../../../05-mcp)

## Ko išmoksite

Jūs sukūrėte pokalbių AI, įvaldėte užklausas, grindėte atsakymus dokumentais ir sukūrėte agentus su įrankiais. Tačiau visi tie įrankiai buvo specialiai sukurti jūsų konkrečiai programai. O kas, jei galėtumėte suteikti savo AI prieigą prie standartizuotos įrankių ekosistemos, kurią gali sukurti ir dalintis bet kas? Šiame modulyje sužinosite, kaip tai padaryti naudojant Modelio konteksto protokolą (MCP) ir LangChain4j agentinį modulį. Pirmiausia pateiksime paprastą MCP failų skaitytuvą, o tada parodysime, kaip lengvai jį integruoti į pažangias agentines darbo eigas, naudojant Supervisor agento šabloną.

## Kas yra MCP?

Modelio konteksto protokolas (MCP) suteikia būtent tai – standartinį būdą AI programoms rasti ir naudoti išorinius įrankius. Vietoje to, kad rašytumėte individualias integracijas kiekvienam duomenų šaltiniui ar paslaugai, jūs prisijungiate prie MCP serverių, kurie savo galimybes atskleidžia nuoseklia forma. Jūsų AI agentas tada gali automatiškai atrasti ir naudoti šiuos įrankius.

Žemiau esantis diagrama parodo skirtumą – be MCP, kiekviena integracija reikalauja individualių point-to-point sujungimų; su MCP vienas protokolas sujungia jūsų programą su bet kuriuo įrankiu:

<img src="../../../translated_images/lt/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Prieš MCP: sudėtingos point-to-point integracijos. Po MCP: vienas protokolas, begalinės galimybės.*

MCP sprendžia esminę problemą AI kūrime: kiekviena integracija yra individuali. Norite prisijungti prie GitHub? Individualus kodas. Norite skaityti failus? Individualus kodas. Norite vykdyti užklausas duomenų bazėje? Individualus kodas. Ir nė viena iš šių integracijų neveikia su kitomis AI programomis.

MCP tai standartizuoja. MCP serveris atskleidžia įrankius su aiškiomis aprašomis ir schemomis. Bet kuris MCP klientas gali prisijungti, atrasti galimus įrankius ir juos naudoti. Sukurkite kartą, naudokite visur.

Žemiau pavaizduota ši architektūra – vienas MCP klientas (jūsų AI programa) jungiasi prie kelių MCP serverių, kiekvienas atskleidžia savo įrankių rinkinį per standartinį protokolą:

<img src="../../../translated_images/lt/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Modelio konteksto protokolo architektūra – standartizuotas įrankių atradimas ir vykdymas*

## Kaip veikia MCP

Po gaubtu MCP naudoja sluoksniuotą architektūrą. Jūsų Java programa (MCP klientas) atranda prieinamus įrankius, siunčia JSON-RPC užklausas per transporto sluoksnį (Stdio arba HTTP), o MCP serveris vykdo veiksmus ir grąžina rezultatus. Žemiau pateikta diagrama atskleidžia kiekvieną šio protokolo sluoksnį:

<img src="../../../translated_images/lt/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Kaip veikia MCP po gaubtu – klientai atranda įrankius, keičiasi JSON-RPC žinutėmis ir vykdo veiksmus per transporto sluoksnį.*

**Serverio-Kliento architektūra**

MCP naudoja klientas-serveris modelį. Serveriai pateikia įrankius – failų skaitymą, užklausas duomenų bazėms, API iškvietimus. Klientai (jūsų AI programa) jungiasi prie serverių ir naudoja jų įrankius.

Norėdami naudoti MCP su LangChain4j, pridėkite šią Maven priklausomybę:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Įrankių atradimas**

Kai jūsų klientas jungiasi prie MCP serverio, jis paklausia „Kokius įrankius turite?“ Serveris atsako su galimų įrankių sąrašu, kurių kiekvienas turi aprašymus ir parametrų schemas. Jūsų AI agentas tada gali nuspręsti, kuriuos įrankius naudoti pagal vartotojo užklausas. Žemiau esanti diagrama rodo šį susitarimą – klientas siunčia užklausą `tools/list`, o serveris grąžina prieinamus įrankius su aprašymais ir parametrų schemomis:

<img src="../../../translated_images/lt/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI startup metu atranda prieinamus įrankius – dabar jis žino, kokios galimybės prieinamos ir gali nuspręsti, kuriuos naudoti.*

**Transporto mechanizmai**

MCP palaiko skirtingus transporto mechanizmus. Du variantai – Stdio (lokalaus subprocess komunikacijai) ir Streamable HTTP (nuotoliniams serveriams). Šiame modulyje demonstruojamas Stdio transportas:

<img src="../../../translated_images/lt/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP transporto mechanizmai: HTTP nuotoliniams serveriams, Stdio lokaliems procesams*

**Stdio** – [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Skirta lokaliems procesams. Jūsų programa atidaro serverį kaip subprocess ir bendrauja per standartinį įvesties/išvesties srautus. Naudinga filesystem prieigai ar komandų eilutės įrankiams.

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
  
`@modelcontextprotocol/server-filesystem` serveris teikia šiuos įrankius, visi saugomi ribotose katalogų teritorijose, kurias nurodote:

| Įrankis | Aprašymas |
|---------|------------|
| `read_file` | Vieno failo turinio skaitymas |
| `read_multiple_files` | Kelių failų skaitymas vienu užklausimu |
| `write_file` | Failo kūrimas arba perrašymas |
| `edit_file` | Tikslinės rasti-ir-pakeisti korekcijos |
| `list_directory` | Vietos sąrašo (failų ir katalogų) gavimas |
| `search_files` | Rekursyvus failų paieška pagal šabloną |
| `get_file_info` | Failo metaduomenų gavimas (dydis, laikai, teisės) |
| `create_directory` | Katalogo kūrimas (įskaitant tėvinius katalogus) |
| `move_file` | Failo arba katalogo perkėlimas ar pervadinimas |

Žemiau pateikta diagrama parodo, kaip Stdio transportas veikia vykdymo metu – jūsų Java programa atidaro MCP serverį kaip vaikų procesą ir jie bendrauja per stdin/stdout vamzdžius, nereikia tinklo ar HTTP:

<img src="../../../translated_images/lt/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio transportas veikime – jūsų programa atidaro MCP serverį kaip vaikų procesą ir bendrauja per stdin/stdout vamzdžius.*

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) pokalbiu:** Atidarykite [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) ir paklauskite:
> - „Kaip veikia Stdio transportas ir kada jį naudoti vietoje HTTP?“
> - „Kaip LangChain4j valdo sukurtų MCP serverio procesų gyvenimo ciklą?“
> - „Kokie yra saugumo aspektai suteikiant AI prieigą prie failų sistemos?“

## Agentinis modulis

Nors MCP suteikia standartizuotus įrankius, LangChain4j **agentinis modulis** suteikia deklaratyvų būdą kurti agentus, kurie organizuoja tuos įrankius. Anotacija `@Agent` ir klasė `AgenticServices` leidžia apibrėžti agentų elgseną per sąsajas, o ne imperatyvų kodą.

Šiame modulyje išnagrinėsite **Supervisor agento** šabloną – pažangų agentinį AI metodą, kai „supervisor“ agentas dinamiškai nusprendžia, kuriuos pagalagentus iškvies pagal vartotojo užklausas. Abi koncepcijas sujungsime suteikdami vienam subagentui failų prieigos galimybes per MCP.

Norėdami naudoti agentinį modulį, pridėkite šią Maven priklausomybę:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Pastaba:** Modulis `langchain4j-agentic` naudoja atskirą versijos parametrą (`langchain4j.mcp.version`), nes jis išleidžiamas kitu grafiku nei pagrindinės LangChain4j bibliotekos.

> **⚠️ Eksperimentinis:** Modulis `langchain4j-agentic` yra **eksperimentinis** ir gali keistis. Stabili AI asistentų kūrimo priemonė lieka `langchain4j-core` su individualiais įrankiais (4 modulis).

## Pavyzdžių paleidimas

### Reikalavimai

- Užbaigtas [4 modulis – Įrankiai](../04-tools/README.md) (šis modulis remiasi individualių įrankių konceptais ir lygina juos su MCP įrankiais)
- `.env` failas šakniniame kataloge su Azure paskyros duomenimis (sukurtas `azd up` 1 modulyje)
- Java 21+, Maven 3.9+
- Node.js 16+ ir npm (MCP serveriams)

> **Pastaba:** Jei dar nesukonfigūravote aplinkos kintamųjų, žr. [1 modulis – Įvadas](../01-introduction/README.md) kaip įdiegti (`azd up` automatiškai sukuria `.env` failą), arba nukopijuokite `.env.example` į `.env` šakniniame kataloge ir įveskite savo duomenis.

## Greita pradžia

**Naudojant VS Code:** Tiesiog dešiniuoju pele spustelėkite bet kurį demo failą Explorer lange ir pasirinkite **„Run Java“**, arba naudokite paleidimo nustatymus Run and Debug lange (prieš tai įsitikinkite, kad jūsų `.env` failas su Azure kredencialais yra sukonfigūruotas).

**Naudojant Maven:** Alternatyviai, galite paleisti komandinėje eilutėje su žemiau pateiktais pavyzdžiais.

### Failų operacijos (Stdio)

Demonstracija, kaip veikia lokaliai subprocess pagrįsti įrankiai.

**✅ Išankstinių sąlygų nereikia** – MCP serveris paleidžiamas automatiškai.

**Naudojant starto skriptus (rekomenduojama):**

Starto skriptai automatiškai įkelia aplinkos kintamuosius iš `.env` failo šakniniame kataloge:

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
  
**Naudojant VS Code:** Dešiniuoju pelės mygtuku spustelėkite `StdioTransportDemo.java` ir pasirinkite **„Run Java“** (įsitikinkite, kad `.env` failas sukonfigūruotas).

Programa automatiškai paleidžia MCP failų serverį ir skaito vietinį failą. Pastebėkite kaip subprocess valdymas atliekamas už jus.

**Laukiamas rezultatas:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```
  
### Supervisor agentas

**Supervisor agente** yra **lanksti** agentinio AI forma. Supervisor naudoja LLM, kad savarankiškai nuspręstų, kuriuos agentus iškviesti pagal vartotojo užklausą. Kitame pavyzdyje sujungsime MCP pagrįstą failų prieigą su LLM agentu, kad sukurtume prižiūrimą failų skaitymo → ataskaitos darbo eigą.

Demo `FileAgent` skaito failą naudodamas MCP failų sistemos įrankius, o `ReportAgent` generuoja struktūrizuotą ataskaitą su vykdomuoju santrauka (1 sakinys), 3 pagrindiniais punktais ir rekomendacijomis. Supervisor automatiškai koordinuoja šią eigą:

<img src="../../../translated_images/lt/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Supervisor naudoja savo LLM, kad nuspręstų, kuriuos agentus iškviesti ir kokia tvarka – nereikia kietai užkoduoto maršrutavimo.*

Štai kaip atrodo konkretūs darbo eiga mūsų failo į ataskaitą grandinei:

<img src="../../../translated_images/lt/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent skaito failą per MCP įrankius, po to ReportAgent transformuoja žalią turinį į struktūruotą ataskaitą.*

Kiekvienas agentas saugo savo išvestį Agentiniame veiklos lauke (bendroje atmintyje), leidžiant žemutiniams agentams pasiekti ankstesnius rezultatus. Tai iliustruoja, kaip MCP įrankiai sklandžiai įsilieja į agentines darbo eigas – Supervisor nereikia žinoti, *kaip* skaitomi failai, tik kad `FileAgent` gali tai atlikti.

#### Demo paleidimas

Starto skriptai automatiškai įkelia aplinkos kintamuosius iš `.env` failo šakniniame kataloge:

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
  
**Naudojant VS Code:** Dešiniuoju pele spustelėkite `SupervisorAgentDemo.java` ir pasirinkite **„Run Java“** (įsitikinkite, kad `.env` failas sukonfigūruotas).

#### Kaip veikia Supervisor

Prieš kuriant agentus, reikia prijungti MCP transportą prie kliento ir apvynioti jį kaip `ToolProvider`. Taip MCP serverio įrankiai tampa prieinami jūsų agentams:

```java
// Sukurkite MCP klientą iš transporto
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Apvyniokite klientą kaip ToolProvider — tai sujungia MCP įrankius su LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```
  
Dabar galite įšvirkšti `mcpToolProvider` į bet kurį agentą, kuriam reikia MCP įrankių:

```java
// 1 žingsnis: FileAgent skaito failus naudodamas MCP įrankius
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Turi MCP įrankius failų operacijoms
        .build();

// 2 žingsnis: ReportAgent generuoja struktūruotus ataskaitas
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor valdo failas → ataskaita darbo eigą
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Grąžina galutinę ataskaitą
        .build();

// Supervisor nusprendžia, kuriuos agentus iškviesti pagal užklausą
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```
  
#### Atsakymų strategijos

Konfigūruodami `SupervisorAgent`, nurodote, kaip jis turėtų pateikti galutinį atsakymą vartotojui, kai pagalagentai atliko savo užduotis. Žemiau diagrama rodo tris turimas strategijas – LAST iškart grąžina paskutinio agento atsakymą, SUMMARY sintetina visų išvestis per LLM, o SCORED pasirenka tą, kuris turi aukštesnį įvertinimą pagal pirminę užklausą:

<img src="../../../translated_images/lt/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Trys strategijos, kaip Supervisor suformuluoja galutinį atsakymą – pasirinkite, ar norite paskutinio agento atsakymo, sintetinio santraukos ar geriausiai įvertinto varianto.*

Galimos strategijos:

| Strategija | Aprašymas |
|------------|-----------|
| **LAST** | Supervisor grąžina paskutinio pagalagento ar įrankio išvestį. Tai naudinga, kai darbo eigos paskutinis agentas yra specialiai skirtas generuoti pilną galutinį atsakymą (pvz., „Santraukos agentas“ tyrimų grandinėje). |
| **SUMMARY** | Supervisor naudoja vidinį kalbos modelį (LLM), kad sintetintų visos sąveikos ir visų pagalagentų rezultatus į santrauką, kurią grąžina kaip galutinį atsakymą. Tai suteikia aiškų, apibendrintą atsakymą vartotojui. |
| **SCORED** | Sistema naudoja vidinį LLM, kad įvertintų tiek LAST atsakymą, tiek SUMMARY santrauką pagal originalią vartotojo užklausą ir grąžina tą, kuri gauna aukštesnį įvertinimą. |
Žr. [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) pilną įgyvendinimą.

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) pokalbiu:** Atidarykite [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) ir paklauskite:
> - "Kaip Supervisor nusprendžia, kuriuos agentus iškviesti?"
> - "Kuo skiriasi Supervisor ir Sequential darbo eigos modeliai?"
> - "Kaip galiu pritaikyti Supervisor planavimo elgseną?"

#### Išvesties supratimas

Kai paleisite demonstraciją, pamatysite struktūrizuotą peržiūrą, kaip Supervisor koordinuoja kelis agentus. Ką reiškia kiekviena dalis:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Antraštė** pristato darbo eigos koncepciją: nukreiptą srautą nuo failo skaitymo iki ataskaitos generavimo.

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

**Darbo eigos diagrama** rodo duomenų srautą tarp agentų. Kiekvienas agentas atlieka specifinį vaidmenį:
- **FileAgent** skaito failus naudodamas MCP įrankius ir saugo neapdorotą turinį `fileContent`
- **ReportAgent** naudoja tą turinį ir generuoja struktūruotą ataskaitą `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Vartotojo užklausa** rodo užduotį. Supervisor ją analizuoja ir nusprendžia iškviesti FileAgent → ReportAgent.

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

**Supervisor valdymas** demonstruoja dviejų žingsnių srautą:
1. **FileAgent** skaito failą per MCP ir saugo turinį
2. **ReportAgent** gauna turinį ir sukuria struktūruotą ataskaitą

Supervisor priėmė šiuos sprendimus **autonomiškai** remdamasis vartotojo užklausa.

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

#### Agentų modulio funkcijų paaiškinimas

Pavyzdys demonstruoja kelias pažangias agentų modulio funkcijas. Pažiūrėkime arčiau į Agentic Scope ir Agent Listeners.

**Agentic Scope** rodo bendrą atmintį, kur agentai saugojo savo rezultatus naudodami `@Agent(outputKey="...")`. Tai leidžia:
- Vėlesniems agentams pasiekti ankstesnių agentų išvestis
- Supervisor sintetinti galutinį atsakymą
- Jums patikrinti, ką kiekvienas agentas sukūrė

Žemiau pateikta diagrama rodo, kaip Agentic Scope veikia kaip bendra atmintis failas→ataskaita darbo eigoje — FileAgent rašo savo išvestį po raktu `fileContent`, ReportAgent ją skaito ir rašo savo išvestį po `report`:

<img src="../../../translated_images/lt/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope veikia kaip bendra atmintis — FileAgent rašo `fileContent`, ReportAgent ją skaito ir rašo `report`, o jūsų kodas gauna galutinį rezultatą.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Nepaliekti failo duomenys iš FileAgent
String report = scope.readState("report");            // Struktūrizuota ataskaita iš ReportAgent
```

**Agent Listeners** leidžia stebėti ir derinti agentų vykdymą. Žingsnis po žingsnio išvestis, kurią matote demonstracijoje, gaunama iš AgentListener, kuris prijungiamas prie kiekvieno agentų iškvietimo:
- **beforeAgentInvocation** - iškviečiamas, kai Supervisor pasirenka agentą, leisdamas pamatyti, kuris agentas buvo pasirinktas ir kodėl
- **afterAgentInvocation** - iškviečiamas, kai agentas baigia darbą, rodantis jo rezultatą
- **inheritedBySubagents** - jeigu tiesa, klausytojas stebi visus hierarchijos agentus

Toliau pateikta diagrama rodo pilną Agent Listener gyvavimo ciklą, įskaitant kaip `onError` tvarko klaidas vykdymo metu:

<img src="../../../translated_images/lt/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners prijungiami prie vykdymo ciklo — stebėkite, kada agentai pradeda, baigia ar patiria klaidų.*

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
        return true; // Plėsti visiems sub-agentams
    }
};
```

Be Supervisor modelio, `langchain4j-agentic` modulis suteikia keletą galingų darbo eigos modelių. Žemiau pateikta diagrama rodo visus penkis — nuo paprastų nuoseklių srautų iki žmogaus įsitraukimo patvirtinimo darbo eigos:

<img src="../../../translated_images/lt/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Penki darbo eigos modeliai agentų koordinavimui — nuo paprastų nuoseklių srautų iki žmogaus įsitraukimo patvirtinimo darbo eigos.*

| Modelis | Aprašymas | Panaudojimo atvejis |
|---------|-------------|-------------------|
| **Sequential** | Vykdyti agentus vienas po kito, išvestis teka į kitą | Srautai: tyrimai → analizė → ataskaita |
| **Parallel** | Vykdyti agentus vienu metu | Nepriklausomos užduotys: oras + naujienos + akcijos |
| **Loop** | Kartoti, kol bus įvykdyta sąlyga | Kokybės įvertinimas: tobulinti, kol balas ≥ 0,8 |
| **Conditional** | Maršrutuoti pagal sąlygas | Klasifikuoti → nukreipti pas specialistą |
| **Human-in-the-Loop** | Pridėti žmogaus patvirtinimo taškus | Patvirtinimo darbo eigos, turinio peržiūra |

## Pagrindinės sąvokos

Dabar, kai išbandėte MCP ir agentų modulį veikiant, apibendrinkime, kada naudoti kurį požiūrį.

Vienas iš didžiausių MCP privalumų — auganti ekosistema. Žemiau diagrama rodo, kaip vienas universalus protokolas sujungia jūsų DI programą su įvairiais MCP serveriais — nuo failų sistemos ir duomenų bazių prieigos iki GitHub, el. pašto, žiniatinklio rinkimo ir kt.:

<img src="../../../translated_images/lt/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP sukuria universalią protokolo ekosistemą — bet kuris MCP suderinamas serveris veikia su bet kuriuo MCP suderinamu klientu, leidžiant dalytis įrankiais tarp programų.*

**MCP** ypač tinka, kai norite naudoti esamą įrankių ekosistemą, kurti įrankius, kuriais gali dalytis kelios programos, integruoti trečiųjų šalių paslaugas per standartinius protokolus arba keisti įrankių įgyvendinimus nekeisdami kodo.

**Agentic modulis** geriausiai veikia, kai norite deklaratyvių agentų apibrėžimų su `@Agent` anotacijomis, reikia darbo eigos koordinavimo (nuosekliai, cikliškai, lygiagrečiai), mėgstate agentų dizainą pagal sąsajas vietoj imperatyvaus kodo arba derinate kelis agentus, kurie dalijasi išvestimis per `outputKey`.

**Supervisor Agent modelis** išsiskiria, kai darbo eiga nėra iš anksto prognozuojama ir norite, kad LLM priimtų sprendimus, kai turite kelis specializuotus agentus, reikalingus dinamiškai koordinuoti, kai kuriate pokalbių sistemas su skirtingų galimybių maršrutavimu arba kai norite lanksčiausio, adaptyviausio agentų elgesio.

Kad padėti pasirinkti tarp specialių `@Tool` metodų iš 04 modulio ir MCP įrankių iš šio modulio, pateikiamas svarbiausių skirtumų palyginimas — specialūs įrankiai suteikia glaudų susiejimą ir pilną tipų saugumą specifiškai programos logikai, MCP įrankiai siūlo standartizuotas, pakartotinai naudojamas integracijas:

<img src="../../../translated_images/lt/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Kada naudoti specialius @Tool metodus vs MCP įrankius — specialūs įrankiai programos specifinei logikai su pilnu tipų saugumu, MCP įrankiai standartizuotoms integracijoms, veikiančioms tarp programų.*

## Sveikiname!

Jūs baigėte visus penkis LangChain4j pradedančiųjų kurso modulius! Štai pilnas jūsų įgytas mokymosi kelias — nuo paprasto pokalbio iki MCP pagrįstų agentų sistemų:

<img src="../../../translated_images/lt/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Jūsų mokymosi kelias per visus penkis modulius — nuo paprasto pokalbio iki MCP pagrįstų agentų sistemų.*

Jūs baigėte LangChain4j pradedančiųjų kursą. Išmokote:

- Kaip kurti pokalbių DI su atmintimi (01 modulis)
- Skirtingų užduočių promptų inžinerijos modelius (02 modulis)
- Kaip pagrįsti atsakymus savo dokumentais naudojant RAG (03 modulis)
- Kaip kurti pagrindinius DI agentus (asistentus) su specialiais įrankiais (04 modulis)
- Kaip integruoti standartizuotus įrankius su LangChain4j MCP ir Agentic moduliais (05 modulis)

### Kas toliau?

Baigę modulius, tyrinėkite [Testavimo Vadovą](../docs/TESTING.md), kad pamatytumėte LangChain4j testavimo koncepcijas veikiant.

**Oficialūs ištekliai:**
- [LangChain4j dokumentacija](https://docs.langchain4j.dev/) – išsamios gairės ir API aprašymai
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) – šaltinio kodas ir pavyzdžiai
- [LangChain4j pamokos](https://docs.langchain4j.dev/tutorials/) – žingsnis po žingsnio pamokos įvairioms sritims

Ačiū, kad baigėte šį kursą!

---

**Navigacija:** [← Ankstesnis: 04 modulis - Įrankiai](../04-tools/README.md) | [Atgal į pagrindinį](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors stengiamės užtikrinti tikslumą, atkreipkite dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas gimtąja kalba laikomas autoritetingu šaltiniu. Svarbiai informacijai rekomenduojama profesionali žmogaus atlikta vertimo paslauga. Mes neatsakome už bet kokius nesusipratimus ar neteisingus aiškinimus, kilusius naudojant šį vertimą.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->