# Modulis 05: Modelio konteksto protokolas (MCP)

## Turinys

- [Ko išmoksite](../../../05-mcp)
- [Kas yra MCP?](../../../05-mcp)
- [Kaip veikia MCP](../../../05-mcp)
- [Agentinis modulis](../../../05-mcp)
- [Pavyzdžių paleidimas](../../../05-mcp)
  - [Reikalavimai](../../../05-mcp)
- [Greitas pradžia](../../../05-mcp)
  - [Failų operacijos (Stdio)](../../../05-mcp)
  - [Prižiūrintis agentas](../../../05-mcp)
    - [Demo paleidimas](../../../05-mcp)
    - [Kaip veikia prižiūrintis agentas](../../../05-mcp)
    - [Atsakymo strategijos](../../../05-mcp)
    - [Išvesties supratimas](../../../05-mcp)
    - [Agentinio modulio funkcijų paaiškinimas](../../../05-mcp)
- [Pagrindinės sąvokos](../../../05-mcp)
- [Sveikiname!](../../../05-mcp)
  - [Kas toliau?](../../../05-mcp)

## Ko išmoksite

Jūs sukūrėte pokalbių AI, įvaldėte užklausas, grindėte atsakymus dokumentais ir sukūrėte agentus su įrankiais. Tačiau visi šie įrankiai buvo specialiai sukurti jūsų konkrečiai programai. O jei galėtumėte suteikti savo AI prieigą prie standartizuotos įrankių ekosistemos, kuria bet kas gali kurti ir dalintis? Šiame modulyje sužinosite, kaip tai padaryti naudodami Modelio konteksto protokolą (MCP) ir LangChain4j agentinį modulį. Pirmiausia pristatome paprastą MCP failų skaitytuvą, o tada parodome, kaip jį lengvai įtraukti į pažangius agentinius darbo srautus, naudojant Prižiūrinčio agente modelį.

## Kas yra MCP?

Modelio konteksto protokolas (MCP) suteikia būtent tai – standartinį būdą AI programoms rasti ir naudoti išorinius įrankius. Vietoj to, kad rašytumėte pasirinktines integracijas kiekvienam duomenų šaltiniui ar paslaugai, jungiatės prie MCP serverių, kurie savo galimybes pateikia nuosekliu formatu. Jūsų AI agentas tada gali automatiškai atrasti ir naudoti šiuos įrankius.

<img src="../../../translated_images/lt/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP lyginamoji schema" width="800"/>

*Prieš MCP: sudėtingos taškas į tašką integracijos. Po MCP: vienas protokolas, begalybė galimybių.*

MCP išsprendžia pagrindinę problemą AI kūrime: kiekviena integracija yra individuali. Norite prisijungti prie GitHub? Pasirinktinis kodas. Norite skaityti failus? Pasirinktinis kodas. Norite užklausti duomenų bazės? Pasirinktinis kodas. Ir nė viena iš šių integracijų neveikia su kitomis AI programomis.

MCP tai standartizuoja. MCP serveris pateikia įrankius su aiškiais aprašymais ir schemomis. Bet kuris MCP klientas gali prisijungti, atrasti turimus įrankius ir juos panaudoti. Sukurkite kartą, naudokite visur.

<img src="../../../translated_images/lt/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP architektūra" width="800"/>

*Modelio konteksto protokolo architektūra – standartizuota įrankių paieška ir vykdymas*

## Kaip veikia MCP

<img src="../../../translated_images/lt/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP protokolo detalė" width="800"/>

*Kaip MCP veikia po gaubtu – klientai atranda įrankius, keičiasi JSON-RPC žinutėmis ir vykdo operacijas per transporto sluoksnį.*

**Serverio-kliento architektūra**

MCP naudoja klientų ir serverių modelį. Serveriai teikia įrankius – failų skaitymą, duomenų bazių užklausas, API kvietimus. Klientai (jūsų AI programėlė) jungiasi prie serverių ir naudoja jų įrankius.

Norėdami naudoti MCP su LangChain4j, pridėkite šią Maven priklausomybę:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```
  
**Įrankių atradimas**

Kai jūsų klientas jungiasi prie MCP serverio, jis paklausia „Kokius įrankius turite?“ Serveris atsako su sąrašu turimų įrankių, kiekvienas su aprašymais ir parametro schemomis. Jūsų AI agentas tada gali nuspręsti, kuriuos įrankius naudoti pagal naudotojo užklausas.

<img src="../../../translated_images/lt/tool-discovery.07760a8a301a7832.webp" alt="MCP įrankių atradimas" width="800"/>

*AI startup metu atranda turimus įrankius – dabar žino, kokios galimybės pasiekiamos ir gali nuspręsti, kuriuos naudoti.*

**Transporto mechanizmai**

MCP palaiko skirtingus transporto mechanizmus. Šis modulis demonstruoja Stdio transportą lokaliems procesams:

<img src="../../../translated_images/lt/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transporto mechanizmai" width="800"/>

*MCP transporto mechanizmai: HTTP nuotoliniams serveriams, Stdio lokaliems procesams*

**Stdio** – [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Skirta lokaliems procesams. Jūsų programa paleidžia serverį kaip subprocess ir bendrauja per standartinį įvestį/išvestį. Naudinga prieigai prie failų sistemos ar komandų eilutės įrankių.

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
  
<img src="../../../translated_images/lt/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio transporto srautas" width="800"/>

*Stdio transportas veikia taip – jūsų programa paleidžia MCP serverį kaip vaikų procesą ir bendrauja per stdin/stdout vamzdžius.*

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) ir užduokite klausimus:
> - „Kaip veikia Stdio transportas ir kada jį naudoti vietoje HTTP?“
> - „Kaip LangChain4j valdo paleistų MCP serverių procesų gyvavimo ciklą?“
> - „Kokios saugumo pasekmės, suteikiant AI prieigą prie failų sistemos?“

## Agentinis modulis

Nors MCP suteikia standartizuotus įrankius, LangChain4j **agentinis modulis** siūlo deklaratyvų būdą kurti agentus, kurie koordinuoja tuos įrankius. `@Agent` anotacija ir `AgenticServices` leidžia apibrėžti agento elgesį per sąsajas, o ne imperatyvų kodą.

Šiame modulyje nagrinėsite **Prižiūrinčio agento** modelį – pažangią agentinę AI paradigmą, kur „prižiūrintis“ agentas dinamiškai parenka, kuriuos subagentus iškviesti pagal naudotojo užklausą. Mes sujungsime abi koncepcijas suteikdami vienam iš subagentų MCP pagrindu veikiančias failų prieigos galimybes.

Norėdami naudoti agentinį modulį, pridėkite šią Maven priklausomybę:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
  
> **⚠️ Eksperimentinė:** `langchain4j-agentic` modulis yra **eksperimentinis** ir gali keistis. Stabilus būdas kurti AI asistentus išlieka `langchain4j-core` su pasirinktinais įrankiais (Modulis 04).

## Pavyzdžių paleidimas

### Reikalavimai

- Java 21+, Maven 3.9+
- Node.js 16+ ir npm (MCP serveriams)
- Aplinkos kintamieji sukonfigūruoti `.env` faile (iš šakninių katalogo):
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (kaip ir Moduliuose 01-04)

> **Pastaba:** Jei dar nesate sukonfigūravę aplinkos kintamųjų, žr. [Modulis 00 – Greitas pradžia](../00-quick-start/README.md), arba nukopijuokite `.env.example` į `.env` šakniniame kataloge ir įrašykite savo reikšmes.

## Greitas pradžia

**Naudojantis VS Code:** Tiesiog dešiniuoju pelės klavišu spustelėkite bet kurį demo failą Eksploreriuje ir pasirinkite **„Run Java“**, arba naudokite paleidimo konfigūracijas Run and Debug lange (įsitikinkite, kad prieš tai pridėjote savo tokeną į `.env` failą).

**Naudojant Maven:** Taip pat galite paleisti iš komandų eilutės su žemiau pateiktais pavyzdžiais.

### Failų operacijos (Stdio)

Tai demonstruoja lokalius subprocess pagrįstus įrankius.

**✅ Nereikia jokių išankstinių sąlygų** – MCP serveris paleidžiamas automatiškai.

**Naudojant paleidimo scenarijus (rekomenduojama):**

Paleidimo scenarijai automatiškai įkelia aplinkos kintamuosius iš šakniniame kataloge esančio `.env` failo:

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
  
**Naudojantis VS Code:** Dešiniuoju pelės mygtuku spustelėkite `StdioTransportDemo.java` ir pasirinkite **„Run Java“** (įsitikinkite, kad jūsų `.env` failas sukonfigūruotas).

Programa automatiškai paleidžia failų sistemos MCP serverį ir skaito vietinį failą. Atkreipkite dėmesį, kaip valdomas subprocess paleidimas.

**Laukiama išvestis:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```
  
### Prižiūrintis agentas

**Prižiūrinčio agente modelis** yra **lanksti** agentinės AI forma. Prižiūrintis naudoja LLM autonomiškai nuspręsti, kuriuos agentus iškviesti pagal naudotojo užklausą. Kitame pavyzdyje derinsime MCP pagrindu veikiančią failų prieigą su LLM agentu, kad sukurtume prižiūrimą failų skaitymo → ataskaitos generavimo darbo srautą.

Demo metu `FileAgent` skaito failą naudodamas MCP failų sistemos įrankius, o `ReportAgent` generuoja struktūruotą ataskaitą su vykdomuoju santrauka (viena frazė), 3 pagrindiniais punktais ir rekomendacijomis. Prižiūrintis automatiškai koordinuoja šį srautą:

<img src="../../../translated_images/lt/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Prižiūrinčio agente modelis" width="800"/>

*Prižiūrintis naudoja savo LLM, kad nuspręstų, kuriuos agentus iškviesti ir kokia tvarka – nereikia kieto kodavimo maršrutų.*

Štai kaip atrodo konkretus darbo srautas mūsų failo-į-ataskaitą kanale:

<img src="../../../translated_images/lt/file-report-workflow.649bb7a896800de9.webp" alt="Failas į ataskaitą darbo srautas" width="800"/>

*FileAgent skaito failą per MCP įrankius, tada ReportAgent transformuoja neapdorotą turinį į struktūruotą ataskaitą.*

Kiekvienas agentas saugo savo išvestį **Agentiniame lauke** (bendroje atmintyje), leidžiančioje tolesniems agentams pasiekti ankstesnius rezultatus. Tai parodo, kaip MCP įrankiai sklandžiai integruojasi į agentinius darbo srautus – Prižiūrintis neturi žinoti *kaip* nuskaityti failus, tik kad `FileAgent` tai gali padaryti.

#### Demo paleidimas

Paleidimo scenarijai automatiškai įkelia aplinkos kintamuosius iš šakniniame kataloge esančio `.env` failo:

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
  
**Naudojantis VS Code:** Dešiniuoju pelės mygtuku spustelėkite `SupervisorAgentDemo.java` ir pasirinkite **„Run Java“** (įsitikinkite, kad jūsų `.env` failas sukonfigūruotas).

#### Kaip veikia prižiūrintis agentas

```java
// 1 žingsnis: FileAgent skaito failus naudodamasis MCP įrankiais
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Turi MCP įrankius failų operacijoms
        .build();

// 2 žingsnis: ReportAgent generuoja struktūruotas ataskaitas
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor koordinuoja failo → ataskaitos darbo eigą
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Grąžina galutinę ataskaitą
        .build();

// Supervisor nusprendžia, kuriuos agentus iškviesti pagal užklausą
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```
  
#### Atsakymo strategijos

Konfigūruodami `SupervisorAgent`, nurodote, kaip jis turėtų formuluoti galutinį atsakymą naudotojui, kai subagentai baigia savo užduotis.

<img src="../../../translated_images/lt/response-strategies.3d0cea19d096bdf9.webp" alt="Atsakymo strategijos" width="800"/>

*Trys strategijos, kaip Prižiūrintis suformuluoja galutinį atsakymą – pasirinkite, ar norite paskutinio agento išvesties, santraukos ar geriausiai įvertintos parinkties.*

Galimos strategijos:

| Strategija | Aprašas |
|------------|----------|
| **LAST**   | Prižiūrintis grąžina paskutinio iškviesto subagento arba įrankio išvestį. Tai naudinga, kai paskutinis darbo srauto agentas specialiai skirtas pateikti galutinį atsakymą (pvz., „Santraukos agentas“ tyrimų kanale). |
| **SUMMARY**| Prižiūrintis naudoja savo vidinį kalbos modelį (LLM), kad sintetintų visos sąveikos ir visų subagentų išvesties santrauką ir pateikia ją kaip galutinį atsakymą. Tai suteikia aiškų, apibendrintą atsakymą naudotojui. |
| **SCORED** | Sistema naudoja vidinį LLM įvertinti tiek **LAST** atsakymą, tiek **SUMMARY** santrauką pagal originalią naudotojo užklausą ir grąžina aukščiausiai įvertintą variantą. |

Žr. [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) pilną įgyvendinimą.

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) ir užduokite klausimus:
> - „Kaip Prižiūrintis nusprendžia, kuriuos agentus iškviesti?“
> - „Kuo skiriasi Prižiūrinčio ir Sekvencinio darbo srautų modeliai?“
> - „Kaip pritaikyti Prižiūrinties planavimo elgesį?“

#### Išvesties supratimas

Paleidus demo, pamatysite struktūruotą vaizdą, kaip Prižiūrintis koordinuoja kelis agentus. Štai ką reiškia kiekviena skiltis:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Antraštė** pristato darbo srauto koncepciją: sutelkti kanalas nuo failų skaitymo iki ataskaitos kūrimo.

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
  
**Darbo srauto diagrama** rodo duomenų srautą tarp agentų. Kiekvienas agentas atlieka konkrečią rolę:
- **FileAgent** skaito failus naudodamas MCP įrankius ir saugo neapdorotą turinį `fileContent`
- **ReportAgent** naudoja tą turinį ir kuria struktūruotą ataskaitą `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Naudotojo užklausa** rodo užduotį. Prižiūrintis ją analizuoja ir nusprendžia iškviesti FileAgent → ReportAgent.

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
  
**Prižiūrinčio koordinavimas** demonstruoja 2 žingsnių srautą veiksme:
1. **FileAgent** skaito failą per MCP ir saugo turinį
2. **ReportAgent** gauna turinį ir generuoja struktūruotą ataskaitą

Prižiūrintis priėmė šiuos sprendimus **autonomiškai** pagal naudotojo užklausą.

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
  
#### Agentinio modulio funkcijų paaiškinimas

Pavyzdyje demonstruojamos kelios agentinio modulio pažangios funkcijos. Pažvelkime atidžiau į Agentinį lauką ir Agentų klausytojus.

**Agentinis laukas** rodo bendrą atmintį, kur agentai saugojo rezultatus naudodami `@Agent(outputKey="...")`. Tai leidžia:
- Vėlesniems agentams pasiekti ankstesnių agentų rezultatus
- Prižiūrinčiajam sintetinti galutinį atsakymą
- Jums stebėti, ką kiekvienas agentas pagamino

<img src="../../../translated_images/lt/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentinis laukas – bendra atmintis" width="800"/>

*Agentinis laukas veikia kaip bendroji atmintis – FileAgent rašo `fileContent`, ReportAgent jį skaito ir rašo `report`, o jūsų kodas skaito galutinį rezultatą.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Žali failo duomenys iš FileAgent
String report = scope.readState("report");            // Struktūrizuota ataskaita iš ReportAgent
```
  
**Agentų klausytojai** leidžia stebėti ir derinti agentų vykdymą. Skirtas žingsnis po žingsnio išvestis, kurią matote demo, gaunama iš AgentListener, kuris prijungiamas prie kiekvieno agento iškvietimo:
- **beforeAgentInvocation** - Iškviečiamas, kai Supervisor pasirenka agentą, leidžiant pamatyti, kuris agentas buvo pasirinktas ir kodėl
- **afterAgentInvocation** - Iškviečiamas, kai agentas baigia darbą, rodant jo rezultatą
- **inheritedBySubagents** - Kai yra tiesa, klausytojas stebi visus hierarchijoje esančius agentus

<img src="../../../translated_images/lt/agent-listeners.784bfc403c80ea13.webp" alt="Agentų klausytojai gyvenimo ciklas" width="800"/>

*Agentų klausytojai įsijungia į vykdymo gyvenimo ciklą — stebi, kada agentai pradeda, baigia arba susiduria su klaidomis.*

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

Be Supervisor modelio, `langchain4j-agentic` modulis suteikia keletą galingų darbo procesų modelių ir funkcijų:

<img src="../../../translated_images/lt/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agentų darbo procesų modeliai" width="800"/>

*Penki darbo proceso modeliai agentų orkestravimui — nuo paprastų sekvencinių kanalų iki žmogaus įtraukties patvirtinimo darbo procesų.*

| Modelis | Aprašymas | Panaudojimo atvejis |
|---------|------------|--------------------|
| **Sekvencinis** | Vykdyti agentus po vieną, išvestis teka į kitą | Kanalai: tyrimas → analizė → ataskaita |
| **Lygiagretus** | Vykdyti agentus vienu metu | Nepriklausomos užduotys: oras + naujienos + akcijos |
| **Ciklas** | Kartoti, kol sąlyga įvykdoma | Kokybės vertinimas: tobulinti, kol balas ≥ 0.8 |
| **Sąlyginis** | Maršrutuoti pagal sąlygas | Klasifikuoti → nukreipti specializuotam agentui |
| **Žmogus-šioje-kilpoje** | Pridėti žmogaus patikrinimo punktus | Patvirtinimo darbo procesai, turinio peržiūra |

## Pagrindinės sąvokos

Dabar, kai susipažinote su MCP ir agentic moduliu praktiškai, apžvelkime, kada naudoti kiekvieną metodą.

<img src="../../../translated_images/lt/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP ekosistema" width="800"/>

*MCP sukuria universalią protokolo ekosistemą — bet kuris MCP suderinamas serveris veikia su bet kuriuo MCP suderinamu klientu, leidžiantį dalintis įrankiais tarp programų.*

**MCP** yra idealus, kai norite pasinaudoti esamomis įrankių ekosistemomis, kurti įrankius, kuriais gali dalintis kelios programos, integruoti trečiųjų šalių paslaugas pagal standartinius protokolus arba keisti įrankių įgyvendinimus nekoduojant.

**Agentic modulis** geriausiai tinka, kai norite deklaratyvių agentų aprašymų su `@Agent` anotacijomis, reikia darbo proceso orkestracijos (sekvencinė, ciklinė, lygiagreti), pirmenybę teikiate sąsajos pagrindu sukurtam agentei nei imperatyviai logikai arba kai derinate kelis agentus, kurie dalinasi išvesties duomenimis per `outputKey`.

**Supervisor agento modelis** išryškėja, kai darbo procesas iš anksto neprognozuojamas ir norite, kad LLM priimtų sprendimus, kai turite kelis specializuotus agentus, kuriems reikalinga dinaminė orkestracija, kai kuriate pokalbių sistemas, kurios nukreipia į skirtingas galimybes, arba kai norite kuo lankstesnio, prisitaikančio agente elgesio.

<img src="../../../translated_images/lt/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Individualūs įrankiai vs MCP įrankiai" width="800"/>

* Kada naudoti individualias @Tool metodikas prieš MCP įrankius — individualūs įrankiai programos specifinei logikai su pilna tipo sauga, MCP įrankiai standartizuotoms integracijoms, veikiančioms skirtingose programose.*

## Sveikiname!

<img src="../../../translated_images/lt/course-completion.48cd201f60ac7570.webp" alt="Kurso baigimas" width="800"/>

*Jūsų mokymosi kelionė per visus penkis modulius — nuo bazinio pokalbio iki MCP pagrįstų agentic sistemų.*

Jūs baigėte LangChain4j pradedantiesiems kursą. Išmokote:

- Kaip kurti pokalbių AI su atmintimi (1 modulis)
- Raginimų inžinerijos modelius skirtingoms užduotims (2 modulis)
- Atsakymų grindimą savo dokumentais naudojant RAG (3 modulis)
- Paprastų AI agentų (asistentų) kūrimą su individualiais įrankiais (4 modulis)
- Standartizuotų įrankių integravimą su LangChain4j MCP ir Agentic moduliais (5 modulis)

### Kas toliau?

Baigę modulius, susipažinkite su [Testavimo vadovu](../docs/TESTING.md), kad išvystytumėte LangChain4j testavimo koncepcijas praktikoje.

**Oficialūs ištekliai:**
- [LangChain4j dokumentacija](https://docs.langchain4j.dev/) - Išsamūs vadovai ir API nuorodos
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Šaltinio kodas ir pavyzdžiai
- [LangChain4j pamokos](https://docs.langchain4j.dev/tutorials/) - Žingsnis po žingsnio pamokos įvairiems naudojimo atvejams

Dėkojame, kad baigėte šį kursą!

---

**Naršymas:** [← Ankstesnis: 4 modulis - Įrankiai](../04-tools/README.md) | [Atgal į pagrindinį](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:  
Šis dokumentas buvo išverstas naudojantis dirbtinio intelekto vertimo paslauga [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, atkreipkite dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas gimtąja kalba turėtų būti laikomas autoritetingu šaltiniu. Dėl svarbios informacijos rekomenduojamas profesionalus žmogiškas vertimas. Mes neatsakome už bet kokius nesusipratimus ar klaidingas interpretacijas, kylančias naudojant šį vertimą.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->