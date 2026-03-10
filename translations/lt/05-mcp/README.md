# Modulis 05: Modelio konteksto protokolas (MCP)

## Turinys

- [Vaizdo įrašo peržiūra](../../../05-mcp)
- [Ko išmoksite](../../../05-mcp)
- [Kas yra MCP?](../../../05-mcp)
- [Kaip veikia MCP](../../../05-mcp)
- [Agentinis modulis](../../../05-mcp)
- [Pavyzdžių vykdymas](../../../05-mcp)
  - [Reikalavimai](../../../05-mcp)
- [Greitas pradėjimas](../../../05-mcp)
  - [Failų operacijos (Stdio)](../../../05-mcp)
  - [Prižiūrintis agentas](../../../05-mcp)
    - [Demo vykdymas](../../../05-mcp)
    - [Kaip veikia prižiūrintis](../../../05-mcp)
    - [Kaip FileAgent atranda MCP įrankius vykdymo metu](../../../05-mcp)
    - [Atsakymų strategijos](../../../05-mcp)
    - [Išvesties supratimas](../../../05-mcp)
    - [Agentinio modulio funkcijų paaiškinimas](../../../05-mcp)
- [Pagrindinės sąvokos](../../../05-mcp)
- [Sveikiname!](../../../05-mcp)
  - [Kas toliau?](../../../05-mcp)

## Vaizdo įrašo peržiūra

Peržiūrėkite šią tiesioginę sesiją, kurioje paaiškinama, kaip pradėti dirbti su šiuo moduliu:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="AI Agents with Tools and MCP - Live Session" width="800"/></a>

## Ko išmoksite

Jūs sukūrėte pokalbių AI, įvaldėte užklausas, pagrindėte atsakymus dokumentuose ir sukūrėte agentus su įrankiais. Tačiau visi šie įrankiai buvo pritaikyti konkrečiai jūsų programai. O kas, jei galėtumėte suteikti savo AI prieigą prie standartizuotos įrankių ekosistemos, kurią gali kurti ir dalintis bet kas? Šiame modulyje sužinosite, kaip tai padaryti naudojant Modelio konteksto protokolą (MCP) ir LangChain4j agentinį modulį. Pirmiausia pristatome paprastą MCP failų skaitytuvą, o vėliau parodome, kaip jis lengvai integruojamas į pažangias agentines darbo eigas naudojant Prižiūrinčio agento modelį.

## Kas yra MCP?

Modelio konteksto protokolas (MCP) suteikia būtent tai – standartinį būdą AI programoms atrasti ir naudoti išorinius įrankius. Vietoj to, kad rašytumėte pritaikytas integracijas kiekvienam duomenų šaltiniui ar paslaugai, jūs prisijungiate prie MCP serverių, kurie savo galimybes pateikia nuoseklia forma. Jūsų AI agentas tada gali automatiškai atrasti ir naudoti šiuos įrankius.

Žemiau pateiktas diagrama parodo skirtumą – be MCP kiekviena integracija reikalauja pritaikyto taško-taško sujungimo; su MCP vienas protokolas sujungia jūsų programą su bet kokiu įrankiu:

<img src="../../../translated_images/lt/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Prieš MCP: Sudėtingos taško-taško integracijos. Po MCP: Vienas protokolas, beribės galimybės.*

MCP sprendžia esminę problemą AI kūrime: kiekviena integracija yra pritaikyta. Norite gauti prieigą prie GitHub? Reikia pritaikyto kodo. Norite skaityti failus? Pritaikytas kodas. Norite užduoti klausimą duomenų bazei? Pritaikytas kodas. Ir nė viena iš šių integracijų neveikia su kitomis AI programomis.

MCP tai standartizuoja. MCP serveris pateikia įrankius su aiškiomis aprašų ir schemų specifikacijomis. Bet kuris MCP klientas gali prisijungti, atrasti prieinamus įrankius ir juos naudoti. Sukurkite vieną kartą, naudokite visur.

Žemiau pateikta diagrama iliustruoja šią architektūrą – vienas MCP klientas (jūsų AI programa) jungiasi prie kelių MCP serverių, kurie per standartinį protokolą atskleidžia savo įrankių rinkinį:

<img src="../../../translated_images/lt/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Modelio konteksto protokolo architektūra – standartizuotas įrankių atradimas ir vykdymas*

## Kaip veikia MCP

Viduje MCP naudoja sluoksniuotą architektūrą. Jūsų Java programa (MCP klientas) atranda prieinamus įrankius, siunčia JSON-RPC užklausas per transporto sluoksnį (Stdio arba HTTP), o MCP serveris vykdo operacijas ir grąžina rezultatus. Toliau pateikta diagrama suskaido kiekvieną šio protokolo sluoksnį:

<img src="../../../translated_images/lt/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Kaip MCP veikia viduje — klientai atranda įrankius, keičiasi JSON-RPC žinutėmis ir vykdo operacijas per transporto sluoksnį.*

**Serverio-kliento architektūra**

MCP naudoja klientų-serverių modelį. Serveriai teikia įrankius – skaito failus, užklausia duomenų bazių, kviečia API. Klientai (jūsų AI programa) jungiasi prie serverių ir naudoja jų įrankius.

Norėdami naudoti MCP su LangChain4j, pridėkite šią Maven priklausomybę:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Įrankių atradimas**

Kai jūsų klientas jungiasi prie MCP serverio, jis klausia „Kokius įrankius turite?“ Serveris atsako su prieinamų įrankių sąrašu, kartu su aprašymais ir parametrų schemomis. Jūsų AI agentas tada gali nuspręsti, kuriuos įrankius naudoti pagal vartotojo užklausas. Žemiau pateikta diagrama rodo šį susitarimą – klientas siunčia `tools/list` užklausą, o serveris grąžina savo prieinamus įrankius su aprašymais ir parametrų schemomis:

<img src="../../../translated_images/lt/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI atranda prieinamus įrankius paleidimo metu — dabar žino, kokios galimybės yra, ir gali nuspręsti, kuriuos naudoti.*

**Transporto mechanizmai**

MCP palaiko skirtingus transporto mechanizmus. Dvi galimybės yra Stdio (lokalios subprocess komunikacijai) ir Streamable HTTP (nuotoliniams serveriams). Šis modulis demonstruoja Stdio transportą:

<img src="../../../translated_images/lt/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP transporto mechanizmai: HTTP nuotoliniams serveriams, Stdio vietiniams procesams*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Skirta vietiniams procesams. Jūsų programa paleidžia serverį kaip subprocess ir komunikuoja per standartinį įvestį/išvestį. Naudinga prieigos prie failų sistemų ar komandų eilutės įrankiams atvejais.

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

`@modelcontextprotocol/server-filesystem` serveris pateikia šiuos įrankius, visi ribojami jūsų nurodytuose kataloguose:

| Įrankis | Aprašymas |
|------|-------------|
| `read_file` | Nuskaito vieno failo turinį |
| `read_multiple_files` | Nuskaito kelis failus per vieną kvietimą |
| `write_file` | Sukuria arba perrašo failą |
| `edit_file` | Atlieka tikslias paieškos ir pakeitimo operacijas |
| `list_directory` | Išvardina failus ir katalogus nurodytame kelyje |
| `search_files` | Rekursyviai ieško failų pagal šabloną |
| `get_file_info` | Gaukite failo metaduomenis (dydis, laiko žymės, leidimai) |
| `create_directory` | Sukuria katalogą (įskaitant tėvinius katalogus) |
| `move_file` | Perkelia arba pervadina failą ar katalogą |

Toliau pateikta diagrama rodo, kaip Stdio transportas veikia vykdymo metu – jūsų Java programa paleidžia MCP serverį kaip vaikų procesą ir jie komunikuoja per stdin/stdout vamzdžius, neprisijungiant prie tinklo ar HTTP:

<img src="../../../translated_images/lt/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio transportas veikia — jūsų programa paleidžia MCP serverį kaip vaikų procesą ir komunikuoja per stdin/stdout vamzdžius.*

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) pokalbiu:** Atidarykite [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) ir klauskite:
> - „Kaip veikia Stdio transportas ir kada jį naudoti vietoje HTTP?“
> - „Kaip LangChain4j valdo paleistų MCP serverių procesų ciklą?“
> - „Kokios saugumo pasekmės dalinant AI prieigą prie failų sistemos?“

## Agentinis modulis

Nors MCP suteikia standartizuotus įrankius, LangChain4j **agentinis modulis** suteikia deklaratyvų būdą kurti agentus, kurie valdo tuos įrankius. `@Agent` anotacija ir `AgenticServices` leidžia apibrėžti agentų elgesį per sąsajas vietoje imperatyvaus kodo.

Šiame modulyje gilinsimės į **Prižiūrinčio agento** modelį – pažangų agentinį AI požiūrį, kai „prižiūrintis“ agentas dinamiškai sprendžia, kuriuos potipius iškviesti pagal vartotojo užklausas. Derinsime abu konceptus suteikdami vienam iš mūsų potipių MCP pagrindu veikiančias failų prieigos galimybes.

Norėdami naudoti agentinį modulį, pridėkite šią Maven priklausomybę:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **Pastaba:** `langchain4j-agentic` modulis naudoja atskirą versijos savybę (`langchain4j.mcp.version`), nes jis išleidžiamas kitu grafiku nei pagrindinės LangChain4j bibliotekos.

> **⚠️ Eksperimentinis:** `langchain4j-agentic` modulis yra **eksperimentinis** ir gali keistis. Stabilus AI asistentų kūrimo būdas išlieka `langchain4j-core` su pritaikytais įrankiais (Modulis 04).

## Pavyzdžių vykdymas

### Reikalavimai

- Užbaigtas [Modulis 04 - Įrankiai](../04-tools/README.md) (šis modulis remiasi pritaikytų įrankių konceptais ir lygina juos su MCP įrankiais)
- `.env` failas šakniniame kataloge su Azure paskyros duomenimis (sukurtas komandą `azd up` Modulyje 01)
- Java 21+, Maven 3.9+
- Node.js 16+ ir npm (MCP serveriams)

> **Pastaba:** Jei dar nesukonfigūravote aplinkos kintamųjų, žr. [Modulis 01 - Įvadas](../01-introduction/README.md) apie diegimo instrukcijas (`azd up` automatiškai sukuria `.env` failą), arba nukopijuokite `.env.example` į `.env` šakniniame kataloge ir užpildykite savo duomenis.

## Greitas pradėjimas

**Naudojant VS Code:** Tiesiog dešiniuoju pelės mygtuku spustelėkite bet kurį demo failą Explorer lange ir pasirinkite **„Run Java“** arba naudokite paleidimo konfigūracijas Run and Debug skiltyje (įsitikinkite, kad jūsų `.env` failas su Azure duomenimis jau sukonfigūruotas).

**Naudojant Maven:** Alternatyviai, galite paleisti iš komandinės eilutės naudojant žemiau pateiktus pavyzdžius.

### Failų operacijos (Stdio)

Tai demonstruoja vietinius subprocess pagrindu veikiančius įrankius.

**✅ Nereikia jokių papildomų reikalavimų** – MCP serveris paleidžiamas automatiškai.

**Naudojant paleidimo skriptus (rekomenduojama):**

Paleidimo skriptai automatiškai įkelia aplinkos kintamuosius iš šakniniame kataloge esančio `.env` failo:

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

Programa automatiškai paleidžia failų sistemos MCP serverį ir nuskaito vietinį failą. Atkreipkite dėmesį, kaip valdomas subprocesų valdymas.

**Tikėtina išvestis:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Prižiūrintis agentas

**Prižiūrinčio agento modelis** yra **lanksti** agentinio AI forma. Prižiūrintis naudoja LLM savarankiškai nuspręsti, kuriuos agentus iškviesti pagal vartotojo užklausą. Tolimesniame pavyzdyje deriname MCP pagrindu veikiančią failų prieigą su LLM agentu, kad sukurtume prižiūrimą failo skaitymo → ataskaitos kūrimo darbo eigą.

Demonstruojant, `FileAgent` skaito failą naudodamas MCP failų sistemos įrankius, o `ReportAgent` generuoja struktūruotą ataskaitą su vykdomąja santrauka (1 sakinys), 3 pagrindiniais punktais ir rekomendacijomis. Prižiūrintis automatiškai organizuoja šią eigą:

<img src="../../../translated_images/lt/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Prižiūrintis naudoja savo LLM, kad nuspręstų, kuriuos agentus iškviesti ir kokia tvarka — nereikia jokių įkoduotų maršrutų.*

Štai kaip atrodo konkretus darbo eiga mūsų failo-į-ataskaitą pavyzdžiui:

<img src="../../../translated_images/lt/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent skaito failą per MCP įrankius, o ReportAgent transformuoja žalią turinį į struktūruotą ataskaitą.*

Toliau pateikta sekos diagrama seka visą Prižiūrinties agento koordinavimą – nuo MCP serverio paleidimo, per Prižiūrinties savarankišką agentų atranką, iki įrankių kvietimų per stdio ir galutinės ataskaitos:

<img src="../../../translated_images/lt/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*Prižiūrintis savarankiškai iškviečia FileAgent (jis per MCP serverį perskaito failą naudodamas stdio), tada iškviečia ReportAgent generuoti struktūruotą ataskaitą — kiekvienas agentas saugo savo rezultatus bendrame Agentic Scope.*

Kiekvienas agentas saugo savo išvestį **Agentic Scope** (bendroje atmintyje), leidžiant žemyn nuomonei agentams pasiekti ankstesnius rezultatus. Tai rodo, kaip MCP įrankiai sklandžiai integruojasi į agentines darbo eigas – Prižiūrintis neturi žinoti *kaip* failai skaitomi, tik kad `FileAgent` tai sugeba.

#### Demo vykdymas

Paleidimo skriptai automatiškai įkelia aplinkos kintamuosius iš šakniniame kataloge esančio `.env` failo:

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

**Naudojant VS Code:** Dešiniuoju pelės mygtuku spustelėkite `SupervisorAgentDemo.java` ir pasirinkite **„Run Java“** (įsitikinkite, kad `.env` failas sukonfigūruotas).

#### Kaip veikia prižiūrintis

Prieš kuriant agentus, turite prijungti MCP transportą prie kliento ir apvynioti jį kaip `ToolProvider`. Tai leidžia MCP serverio įrankiams tapti prieinamais jūsų agentams:

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

// 2 žingsnis: ReportAgent generuoja struktūrizuotas ataskaitas
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor koordinuoja failo → ataskaitos darbo eigą
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Grąžinti galutinę ataskaitą
        .build();

// Supervisor nusprendžia, kuriuos agentus iškviesti pagal užklausą
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Kaip FileAgent atranda MCP įrankius vykdymo metu

Gal kyla klausimas: **kaip `FileAgent` žino, kaip naudoti npm failų sistemos įrankius?** Atsakymas – jis nežino tiesiogiai – **LLM** sužino tai vykdymo metu per įrankių schemas.
`FileAgent` sąsaja yra tik **užklausos apibrėžimas**. Ji neturi jokios įkoduotos žinios apie `read_file`, `list_directory` ar bet kurį kitą MCP įrankį. Štai, kas nutinka nuo pradžios iki pabaigos:

1. **Serveris paleidžiamas:** `StdioMcpTransport` paleidžia `@modelcontextprotocol/server-filesystem` npm paketą kaip vaiko procesą
2. **Įrankių aptikimas:** `McpClient` siunčia `tools/list` JSON-RPC užklausą serveriui, kuris atsako įrankių pavadinimais, aprašymais ir parametrų schemomis (pvz., `read_file` — *„Perskaityti visą failo turinį“* — `{ path: string }`)
3. **Schemų injekcija:** `McpToolProvider` apvynioja šias rastas schemas ir suteikia jas LangChain4j
4. **LLM sprendžia:** Kai kviečiamas `FileAgent.readFile(path)`, LangChain4j siunčia sistemos žinutę, vartotojo žinutę, **ir įrankių schemų sąrašą** LLM. LLM perskaito įrankių aprašymus ir generuoja įrankio kvietimą (pvz., `read_file(path="/some/file.txt")`)
5. **Vykdymas:** LangChain4j nutraukia įrankio kvietimą, nukreipia jį per MCP klientą atgal į Node.js subprocessą, gauna rezultatą ir grąžina jį LLM

Tai tas pats [Įrankių aptikimo](../../../05-mcp) mechanizmas, aprašytas aukščiau, bet pritaikytas tiesiogiai agentų darbo eigai. `@SystemMessage` ir `@UserMessage` anotacijos nukreipia LLM elgesį, tuo tarpu įinjektuotas `ToolProvider` suteikia **galimybes** — LLM runtime metu sujungia abu.

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) ir užduokite:
> - „Kaip šis agentas žino, kurį MCP įrankį iškviesti?“
> - „Kas nutiktų, jei pašalinčiau ToolProvider iš agento kūrėjo?“
> - „Kaip įrankių schemos perduodamos LLM?“

#### Atsako strategijos

Konfigūruodami `SupervisorAgent`, nurodote, kaip jis turėtų formuluoti galutinį atsakymą vartotojui, kai pagalbiniai agentai baigia savo užduotis. Žemiau pateiktame diagramos rodomos trys prieinamos strategijos — LAST tiesiog grąžina paskutinio agento išvestį, SUMMARY sintetina visus atsakymus per LLM, o SCORED pasirenka aukščiausią balą gavusį variantą pagal pradinį užklausimą:

<img src="../../../translated_images/lt/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Trys strategijos, kaip Supervisor suformuluoja galutinį atsakymą — pasirinkite pagal tai, ar norite paskutinio agento išvesties, sintezuoto santraukos, ar geriausiai įvertinto varianto.*

Prieinamos strategijos:

| Strategija | Aprašymas |
|----------|-------------|
| **LAST** | Supervisor grąžina paskutinio pagalbinio agento arba įrankio išvestį. Tai naudinga, kai paskutinis darbo eigos agentas specialiai sukurtas pateikti galutinį, visapusišką atsakymą (pvz., „Santraukos agentas“ tyrimų grandinėje). |
| **SUMMARY** | Supervisor naudoja savo vidinį kalbos modelį (LLM) apibendrinti visą sąveiką ir visų pagalbinių agentų iškvietimus, tuomet grąžina šią santrauką kaip galutinį atsakymą. Tai suteikia aiškų, apibendrintą atsakymą vartotojui. |
| **SCORED** | Sistema naudoja vidinį LLM įvertinti tiek LAST atsakymą, tiek SUMMARY santrauką pagal pradinį vartotojo užklausimą ir grąžina tą, kurį LLM įvertina aukščiau. |

Žr. [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) pilnai implementacijai.

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) ir paklauskite:
> - „Kaip Supervisor nusprendžia, kuriuos agentus iškviesti?“
> - „Kuo Supervisor skirtumas nuo Sekvencinio darbo eigos modelių?“
> - „Kaip galiu pritaikyti Supervizoriaus planavimo elgseną?“ 

#### Išvesties supratimas

Paleidus demonstraciją, pamatysite struktūruotą apžvalgą, kaip Supervisor koordinuoja kelis agentus. Štai ką reiškia kiekviena dalis:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Antraštė** pristato darbo eigos koncepciją: orientuotą grandinę nuo failų skaitymo iki ataskaitos generavimo.

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

**Darbo eigos diagrama** rodo duomenų srautą tarp agentų. Kiekvienas agentas turi specifinę reikšmę:
- **FileAgent** skaito failus per MCP įrankius ir saugo neapdorotą turinį `fileContent`
- **ReportAgent** naudoja tą turinį ir generuoja struktūrizuotą ataskaitą `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Vartotojo užklausa** rodo užduotį. Supervisor ją išanalizuoja ir nusprendžia iškviesti FileAgent → ReportAgent.

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

**Supervisor koordinavimas** rodo 2 žingsnių veikimą:
1. **FileAgent** skaito failą per MCP ir saugo turinį
2. **ReportAgent** gauna turinį ir sukuria struktūrizuotą ataskaitą

Supervisor šiuos sprendimus priėmė **autonomiškai** pagal vartotojo užklausą.

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

#### Agentų modulio funkcionalumo paaiškinimas

Pavyzdyje demonstruojama keletas pažangių agentų modulio funkcijų. Pažvelkime atidžiau į Agentic Scope ir Agent Listeners.

**Agentic Scope** rodo bendrą atmintį, kur agentai saugo savo rezultatus naudodami `@Agent(outputKey="...")`. Tai leidžia:
- Vėlesniems agentams pasiekti ankstesnių agentų išvestis
- Supervisor suvesti galutinį atsakymą
- Jums peržiūrėti, ką kiekvienas agentas pagamino

Žemiau diagrama rodo, kaip Agentic Scope veikia kaip bendra atmintis failo-į-ataskaitą darbo eigoje — FileAgent rašo savo išvestį raktu `fileContent`, ReportAgent skaito ją ir rašo savo išvestį po `report`:

<img src="../../../translated_images/lt/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope veikia kaip bendra atmintis — FileAgent įrašo `fileContent`, ReportAgent ją skaito ir įrašo `report`, o jūsų kodas skaito galutinį rezultatą.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Nepaliekti failo duomenys iš FileAgent
String report = scope.readState("report");            // Struktūrizuota ataskaita iš ReportAgent
```

**Agentų klausytojai** leidžia stebėti ir derinti agentų vykdymą. Žingsnis po žingsnio išvestis, kurią matote demonstracijoje, ateina iš AgentListener, prijungto prie kiekvieno agento kvietimo:
- **beforeAgentInvocation** - Kviečiamas, kai Supervisor pasirenka agentą, leidžiantis matyti, kuris agentas buvo pasirinktas ir kodėl
- **afterAgentInvocation** - Kviečiamas, kai agentas baigia darbą, parodantis jo rezultatą
- **inheritedBySubagents** - Kai tiesa, klausytojas stebi visus agentus hierarchijoje

Žemiau esanti diagrama rodo visą Agent Listener gyvavimo ciklą, įskaitant kaip `onError` tvarko klaidas agentų vykdyme:

<img src="../../../translated_images/lt/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners jungiasi prie vykdymo ciklo — stebi, kada agentai pradeda, baigia ar susiduria su klaidomis.*

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
        return true; // Paskleisti visiems subagentams
    }
};
```

Be Supervisor modelio, `langchain4j-agentic` modulis suteikia kelis galingus darbo eigos modelius. Žemiau diagrama rodo penkis visus — nuo paprastų sekvencinių grandinių iki žmogaus įtrauktų patvirtinimo darbo eigų:

<img src="../../../translated_images/lt/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Penki darbo eigos modeliai agentų koordinavimui — nuo paprastų sekvencinių grandinių iki žmogaus įtraukimo patvirtinimo darbo eigų.*

| Modelis | Aprašymas | Naudojimo atvejis |
|---------|-------------|------------------|
| **Sequential** | Vykdyti agentus viena po kitos, išvestis teka į sekantį | Grandinės: tyrimai → analizė → ataskaita |
| **Parallel** | Vykdyti agentus vienu metu | Nepriklausomos užduotys: oras + naujienos + akcijos |
| **Loop** | Kartoti, kol sąlyga įvykdyta | Kokybės įvertinimas: tobulinti kol balas ≥ 0.8 |
| **Conditional** | Nuvesti pagal sąlygas | Klasifikuoti → nukreipti pas specialistą agentą |
| **Human-in-the-Loop** | Įtraukti žmogaus patvirtinimus | Patvirtinimo darbo eigos, turinio peržiūra |

## Pagrindinės sąvokos

Dabar, kai susipažinote su MCP ir agentų moduliu veikiant, apibendrinkime, kada naudoti kurią metodiką.

Viena iš didžiausių MCP privalumų — plėtojama ekosistema. Žemiau diagrama rodo, kaip vienas universalus protokolas jungia jūsų AI programą su įvairiais MCP serveriais — nuo failų sistemos ir duomenų bazių prieigos iki GitHub, el. pašto, svetainių skanavimo ir kt.:

<img src="../../../translated_images/lt/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP sukuria universalų protokolo ekologinį tinklą — bet kuris MCP suderinamas serveris veikia su bet kuriuo MCP suderinamu klientu, leidžiant įrankių dalijimąsi tarp programų.*

**MCP** ypač tinka, kai norite pasinaudoti egzistuojančiomis įrankių ekosistemomis, kurti įrankius, kuriuos gali naudoti kelios programos, integruoti trečiųjų šalių paslaugas pagal standartinius protokolus arba keisti įrankių įgyvendinimą nekeisdami kodo.

**Agentų modulis** tinkamiausias, kai norite deklaratyvių agentų apibrėžimų su `@Agent` anotacijomis, reikia darbo eigos koordinavimo (sekvencinė, ciklinė, paralelinė), preferuojate sąsajos pagrindu sukurtą agentų dizainą vietoje imperatyvaus kodo arba sujungiame kelis agentus, kurie dalijasi išvestimis per `outputKey`.

**Supervisor Agent modelis** išsiskiria, kai darbo eiga nėra iš anksto nuspėjama ir norite, kad LLM spręstų, kai turite kelis specializuotus agentus dinamiškai koordinuoti, kuriate pokalbių sistemas, nukreipiančias skirtingoms galimybėms, arba kai norite lankstų, adaptuojamą agentų elgesį.

Kad padėtume nuspręsti tarp pasirinktinių `@Tool` metodų iš 04 modulio ir MCP įrankių iš šio modulio, žemiau pateikiamas svarbiausių skirtumų palyginimas — pasirinktiniai įrankiai suteikia glaudų sujungimą ir pilną tipiškumo saugumą programai specifinėje logikoje, o MCP įrankiai – standartizuotas, pakartotinai naudojamas integracijas:

<img src="../../../translated_images/lt/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Kai naudoti pasirinktinius @Tool metodus prieš MCP įrankius — pasirinktiniams įrankiams programai specifinė logika su pilna tipiškumo kontrole, MCP įrankiams standartizuotos integracijos, veikiančios su visomis programomis.*

## Sveikiname!

Jūs praėjote per visus penkis LangChain4j pradedančiųjų kursų modulius! Štai pilnas jūsų įgytas mokymosi kelias — nuo pagrindinio pokalbio iki MCP įgalintų agentų sistemų:

<img src="../../../translated_images/lt/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Jūsų mokymosi kelias per visus penkis modulius — nuo pagrindinio pokalbio iki MCP įgalintų agentų sistemų.*

Jūs baigėte LangChain4j pradedančiųjų kursą. Išmokote:

- Kaip kurti pokalbių AI su atmintimi (1 modulis)
- Užklausų inžinerijos šablonus skirtingoms užduotims (2 modulis)
- Atsakymų pagrindimą savo dokumentuose naudojant RAG (3 modulis)
- Pagrindinių AI agentų (asistentų) kūrimą su pasirinktinais įrankiais (4 modulis)
- Standartizuotų įrankių integraciją su LangChain4j MCP ir agentų moduliais (5 modulis)

### Kas toliau?

Baigus modulius, išnagrinėkite [Testavimo gidą](../docs/TESTING.md) ir pamatykite LangChain4j testavimo koncepcijas veikime.

**Oficialūs ištekliai:**
- [LangChain4j dokumentacija](https://docs.langchain4j.dev/) – išsamios instrukcijos ir API aprašymai
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) – šaltinio kodas ir pavyzdžiai
- [LangChain4j pamokos](https://docs.langchain4j.dev/tutorials/) – žingsnis po žingsnio pamokos įvairiems naudojimo atvejams

Dėkojame, kad baigėte šį kursą!

---

**Navigacija:** [← Ankstesnis: 04 modulis - Įrankiai](../04-tools/README.md) | [Atgal į pradžią](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:  
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors stengiamės užtikrinti tikslumą, atkreipkite dėmesį, kad automatizuoti vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas gimtąja kalba turėtų būti laikomas autoritetingu šaltiniu. Svarbiai informacijai rekomenduojamas profesionalus žmogaus vertimas. Mes neprisiimame atsakomybės už bet kokius nesusipratimus ar neteisingus aiškinimus, kylančius naudojantis šiuo vertimu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->