# Module 05: Itifaki ya Muktadha wa Mfano (MCP)

## Jedwali la Maudhui

- [Utajifunza Nini](../../../05-mcp)
- [MCP ni Nini?](../../../05-mcp)
- [Jinsi MCP Inavyofanya Kazi](../../../05-mcp)
- [Moduli ya Agentic](../../../05-mcp)
- [Kufanya Mifano Iendeshe](../../../05-mcp)
  - [Matangulizi](../../../05-mcp)
- [Anza Haraka](../../../05-mcp)
  - [Operesheni za Faili (Stdio)](../../../05-mcp)
  - [Wakala Msimamizi](../../../05-mcp)
    - [Kufanya Demo Iendeshe](../../../05-mcp)
    - [Jinsi Msimamizi Anavyofanya Kazi](../../../05-mcp)
    - [Mikakati ya Majibu](../../../05-mcp)
    - [Kuelewa Matokeo](../../../05-mcp)
    - [Ufafanuzi wa Sifa za Moduli ya Agentic](../../../05-mcp)
- [Madhumuni Muhimu](../../../05-mcp)
- [Hongera!](../../../05-mcp)
  - [Nini Ifuatayo?](../../../05-mcp)

## Utajifunza Nini

Umejenga AI ya mazungumzo, utaalamu wa maagizo, kuweka majibu kwenye hati, na kuunda mawakala wenye vyombo. Lakini vyombo vyote hivyo vilijengwa maalum kwa programu yako. Vipi kama ungeweza kumpa AI yako upatikanaji wa mazingira ya vyombo vilivyounganishwa kwa kiwango cha kawaida ambavyo mtu yeyote anaweza kuunda na kushiriki? Katika moduli hii, utajifunza jinsi ya kufanya hivyo kwa kutumia Itifaki ya Muktadha wa Mfano (MCP) na moduli ya agentic ya LangChain4j. Kwanza tunaonyesha msomaji rahisi wa faili wa MCP kisha tunaonyesha jinsi inavyojumuishwa kwa urahisi katika mtiririko wa kazi wa agentic wa hali ya juu kwa kutumia mfano wa Wakala Msimamizi.

## MCP ni Nini?

Itifaki ya Muktadha wa Mfano (MCP) inatoa hilo kabisa - njia ya viwango kwa programu za AI kugundua na kutumia vyombo vya nje. Badala ya kuandika mwingiliano maalum kwa kila chanzo cha data au huduma, unajiunga na seva za MCP ambazo zinaonyesha uwezo wao kwa muundo unaolingana. Wakala wako wa AI basi anaweza kugundua na kutumia vyombo hivi moja kwa moja.

Mchoro hapa chini unaonyesha tofauti — bila MCP, kila mwingiliano unahitaji waya maalum kutoka sehemu hadi sehemu; kwa MCP, itifaki moja inajiunganisha na zana yoyote:

<img src="../../../translated_images/sw/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Kabla ya MCP: Mwingiliano mgumu kutoka sehemu hadi sehemu. Baada ya MCP: Itifaki moja, fursa zisizo na ukomo.*

MCP inatatua tatizo msingi katika maendeleo ya AI: kila mwingiliano ni maalum. Unataka kufikia GitHub? Msimbo wa kawaida. Unataka kusoma faili? Msimbo wa kawaida. Unataka kuuliza hifadhidata? Msimbo wa kawaida. Na hakuna mwingiliano wa hizi unafanya kazi na programu zingine za AI.

MCP inatumia kiwango hiki. Seva ya MCP inaonyesha vyombo na maelezo wazi na miundo ya vigezo. Mteja yeyote wa MCP anaweza kuungana, kugundua vyombo vinavyopatikana, na kuvitumia. Jenga mara moja, tumia kila mahali.

Mchoro huu chini unaonyesha usanifu huu — mteja mmoja wa MCP (programu yako ya AI) huungana na seva nyingi za MCP, kila moja ikionyesha seti yake ya vyombo kupitia itifaki ya kawaida:

<img src="../../../translated_images/sw/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Usanifu wa Itifaki ya Muktadha wa Mfano - ugunduzi na utekelezaji wa vyombo vilivyounganishwa kwa kiwango cha kawaida*

## Jinsi MCP Inavyofanya Kazi

Chini ya uso, MCP hutumia usanifu wa tabaka nyingi. Programu yako ya Java (mteja wa MCP) hugundua vyombo vinavyopatikana, hutuma maombi ya JSON-RPC kupitia safu ya usafirishaji (Stdio au HTTP), na seva ya MCP hufanya shughuli na kurudisha matokeo. Mchoro ufuatao unavunja kila tabaka ya itifaki hii:

<img src="../../../translated_images/sw/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Jinsi MCP inavyofanya kazi chini ya uso — wateja hugundua vyombo, kubadilishana ujumbe wa JSON-RPC, na kutekeleza shughuli kupitia safu ya usafirishaji.*

**Usanifu wa Seva-Mteja**

MCP hutumia mfano wa mteja-seva. Seva hutoa vyombo - kusoma faili, kuuliza hifadhidata, kuita API. Wateja (programu yako ya AI) huungana na seva na kutumia vyombo vyake.

Ili kutumia MCP na LangChain4j, ongeza utegemezi huu wa Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Ugunduzi wa Zana**

Unapounganisha mteja wako na seva ya MCP, huuliza "Unazo zana gani?" Seva hujibu na orodha ya vyombo vinavyopatikana, kila moja likiwa na maelezo na miundo ya vigezo. Wakala wako wa AI basi anaweza kufanya uamuzi wa zana gani kutumia kulingana na maombi ya mtumiaji. Mchoro hapa chini unaonyesha mchakato huu — mteja hutuma ombi la `tools/list` na seva hurudisha vyombo vyake vinavyopatikana ikiwa na maelezo na miundo ya vigezo:

<img src="../../../translated_images/sw/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI hugundua vyombo vinavyopatikana wakati wa kuanzisha — sasa inajua uwezo uliopo na inaweza kuamua ipi itumike.*

**Mekanizo ya Usafirishaji**

MCP inasaidia mekanizo tofauti za usafirishaji. Chaguzi mbili ni Stdio (kwa mawasiliano ya chini ya mchakato wa ndani) na Streamable HTTP (kwa seva za mbali). Moduli hii inaonyesha usafirishaji wa Stdio:

<img src="../../../translated_images/sw/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*Mekanizo ya usafirishaji wa MCP: HTTP kwa seva za mbali, Stdio kwa michakato ya ndani*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Kwa michakato ya ndani. Programu yako hutoa seva kama mchakato mdogo na huwasiliana kupitia pembejeo/pato ya kawaida. Inafaa kwa upatikanaji wa mfumo wa faili au zana za mstari wa amri.

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

Seva ya `@modelcontextprotocol/server-filesystem` inaonyesha vyombo vifuatavyo, vyote vikiwa ndani ya maeneo (sandboxed) ya saraka unazobainisha:

| Zana | Maelezo |
|------|-------------|
| `read_file` | Soma maudhui ya faili moja |
| `read_multiple_files` | Soma faili nyingi kwa papo moja |
| `write_file` | Unda au andika faili upya |
| `edit_file` | Fanya mabadiliko ya targetted ya tafuta-na-badilisha |
| `list_directory` | Orodhesha faili na saraka katika njia fulani |
| `search_files` | Tafuta faili kwa mchakato wa kurudia ikifuatana na muundo |
| `get_file_info` | Pata metadata ya faili (ukubwa, nyakati, ruhusa) |
| `create_directory` | Unda saraka (pamoja na saraka za wazazi) |
| `move_file` | Hamisha au fanya kificho cha jina la faili au saraka |

Mchoro ufuatao unaonyesha jinsi usafirishaji wa Stdio unavyofanya kazi wakati wa utekelezaji — programu yako ya Java hutengeneza seva ya MCP kama mchakato mdogo na wanasiliana kupitia mabomba ya stdin/stdout, bila mtandao au HTTP kuhusika:

<img src="../../../translated_images/sw/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Usafirishaji wa Stdio ukienda — programu yako hutengeneza seva ya MCP kama mchakato mdogo na wanasiliana kupitia mabomba ya stdin/stdout.*

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) na uliza:
> - "Usafirishaji wa Stdio hufanya kazi vipi na ni lini nifanye matumizi yake badala ya HTTP?"
> - "LangChain4j hushughulikiaje mzunguko wa maisha ya michakato ya seva za MCP iliyotolewa?"
> - "Matokeo gani ya usalama yanapotolewa wakati AI inapitishwa upatikanaji wa mfumo wa faili?"

## Moduli ya Agentic

Wakati MCP inatoa vyombo vilivyounganishwa, moduli ya **agentic** ya LangChain4j hutoa njia ya kueleza kwa uwazi ujenzi wa mawakala wanaoendesha vyombo hivyo. Alama ya `@Agent` na `AgenticServices` hukuruhusu kufafanua tabia ya wakala kupitia kiolesura badala ya msimbo wa amri.

Katika moduli hii, utachunguza mfano wa **Wakala Msimamizi** — njia ya hali ya juu ya AI ya agentic ambapo wakala "msimamizi" hufuata kwa njia ya mabadiliko sub-agents gani waite kulingana na maombi ya mtumiaji. Tutachanganya dhana hizi mbili kwa kumpa moja wa sub-agents zetu uwezo wa upatikanaji wa faili unaotumia MCP.

Ili kutumia moduli ya agentic, ongeza utegemezi huu wa Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Kumbuka:** Moduli ya `langchain4j-agentic` hutumia mali tofauti ya toleo (`langchain4j.mcp.version`) kwa sababu hutoa ratiba tofauti na maktaba msingi za LangChain4j.

> **⚠️ Jaribio:** Moduli ya `langchain4j-agentic` ni **jaribio** na inaweza kubadilika. Njia thabiti ya kujenga wasaidizi wa AI bado ni `langchain4j-core` na vyombo maalum (Moduli 04).

## Kufanya Mifano Iendeshe

### Matangulizi

- Umekamilisha [Moduli 04 - Vyombo](../04-tools/README.md) (moduli hii inajenga juu ya dhana za vyombo maalum na inalinganisha na vyombo vya MCP)
- Faili `.env` katika saraka kuu yenye kitambulisho cha Azure (iliyo tengenezwa na `azd up` katika Moduli 01)
- Java 21+, Maven 3.9+
- Node.js 16+ na npm (kwa seva za MCP)

> **Kumbuka:** Ikiwa bado hujajiandaa mazingira yako ya mabadiliko, angalia [Moduli 01 - Utangulizi](../01-introduction/README.md) kwa maelekezo ya uanzishaji (`azd up` hutoa faili `.env` moja kwa moja), au nakili `.env.example` kwenda `.env` katika saraka kuu na jaza thamani zako.

## Anza Haraka

**Ukiongeza VS Code:** Bofya-kulia faili yoyote ya demo katika Explorer na chagua **"Run Java"**, au tumia usanidi wa kuanzisha kutoka paneli ya Run and Debug (hakikisha faili yako `.env` imefungwa na kitambulisho cha Azure kwanza).

**Ukiongeza Maven:** Vinginevyo, unaweza kuendesha kutoka mstari wa amri kwa mifano ifuatayo.

### Operesheni za Faili (Stdio)

Hii inaonyesha vyombo vinavyotumia michakato ya ndani.

**✅ Hakuna sharti la awali** - seva ya MCP hutolewa moja kwa moja.

**Kutumia Skripti za Kuanzisha (Inapendekezwa):**

Skripti za kuanzisha huchukua mabadiliko ya mazingira moja kwa moja kutoka faili kuu `.env`:

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

**Ukiongeza VS Code:** Bofya-kulia `StdioTransportDemo.java` na chagua **"Run Java"** (hakikisha faili yako `.env` imefungwa).

Programu hutengeneza seva ya MCP ya mfumo wa faili moja kwa moja na husoma faili ya ndani. Angalia jinsi usimamizi wa michakato mdogo unavyoendeshwa kwako.

**Matokeo yanayotarajiwa:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Wakala Msimamizi

Mfano wa **Wakala Msimamizi** ni aina **yenye kubadilika** ya AI ya agentic. Msimamizi hutumia LLM kuamua kwa uhuru wakala gani waite kulingana na ombi la mtumiaji. Katika mfano ujao, tunachanganya upatikanaji wa faili unaotumia MCP na wakala wa LLM kuunda mtiririko wa kufikia faili → ripoti.

Katika demo, `FileAgent` husoma faili kwa kutumia vyombo vya mfumo wa faili wa MCP, na `ReportAgent` hutengeneza ripoti yenye muhtasari wa mtendaji (sentensi 1), vidokezo kuu 3, na mapendekezo. Msimamizi huandaa mtiririko huu moja kwa moja:

<img src="../../../translated_images/sw/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Msimamizi hutumia LLM yake kuamua wakala gani waite na kwa utaratibu gani — hakuna haja ya upangaji mgumu wa mikondo.*

Huu ni mtiririko halisi wa mradi wetu wa faili-kwa-ripoti:

<img src="../../../translated_images/sw/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent husoma faili kupitia vyombo vya MCP, kisha ReportAgent hubadilisha maudhui ghafi kuwa ripoti yenye muundo.*

Kila wakala huhifadhi matokeo yake katika **Eneo la Agentic** (kumbukumbu inayoshirikiwa), kuruhusu mawakala wa chini kufikia matokeo ya awali. Hii inaonyesha jinsi vyombo vya MCP vinavyoambatana vizuri na michakato ya agentic — Msimamizi haahitaji kujua *jinsi* faili husomwa, bali kwamba `FileAgent` anaweza kufanya hivyo.

#### Kufanya Demo Iendeshe

Skripti za kuanzisha huchukua mabadiliko ya mazingira moja kwa moja kutoka faili kuu `.env`:

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

**Ukiongeza VS Code:** Bofya-kulia `SupervisorAgentDemo.java` na chagua **"Run Java"** (hakikisha faili yako `.env` imefungwa).

#### Jinsi Msimamizi Anavyofanya Kazi

Kabla ya kujenga mawakala, unahitaji kuunganisha usafirishaji wa MCP kwa mteja na kuukua kama `ToolProvider`. Hii ndiyo njia vyombo vya seva ya MCP vinavyopatikana kwa mawakala wako:

```java
// Unda mteja wa MCP kutoka kwa usafirishaji
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Funika mteja kama ToolProvider — hii inaunganisha zana za MCP katika LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Sasa unaweza kuingiza `mcpToolProvider` kwa wakala yeyote anayetumia vyombo vya MCP:

```java
// Hatua ya 1: FileAgent husoma faili kwa kutumia zana za MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Ina zana za MCP kwa ajili ya operesheni za faili
        .build();

// Hatua ya 2: ReportAgent huunda ripoti zilizopangwa
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Msimamizi huandaa mchakato wa faili → ripoti
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Rudisha ripoti ya mwisho
        .build();

// Msimamizi huchagua wakala gani waite kulingana na ombi
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Mikakati ya Majibu

Unapoweka `SupervisorAgent`, unabainisha jinsi inavyopaswa kutoa jibu lake la mwisho baada ya sub-agents kukamilisha kazi zao. Mchoro hapa chini unaonyesha mikakati mitatu inayopatikana — LAST hurudisha matokeo ya wakala wa mwisho moja kwa moja, SUMMARY hutengeneza muhtasari wa yote kupitia LLM, na SCORED huchagua matokeo ya juu zaidi kulingana na ombi la mtumiaji la awali:

<img src="../../../translated_images/sw/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Mikakati mitatu ya namna Msimamizi anavyotengeneza jibu la mwisho — chagua kulingana na kama unahitaji matokeo ya wakala wa mwisho, muhtasari uliounganishwa, au chaguo lenye alama bora.*

Mikakati inayopatikana ni:

| Mkakati | Maelezo |
|----------|-------------|
| **LAST** | Msimamizi hurudisha matokeo ya wakala wa mwisho aliyefanya kazi au zana iliyotumika. Hii ni muhimu wakati wakala wa mwisho katika mtiririko huo amekusudiwa kutoa jibu kamili, la mwisho (mfano, "Wakala wa Muhtasari" katika mtiririko wa utafiti). |
| **SUMMARY** | Msimamizi hutumia Mfano Wake wa Lugha (LLM) kuunda muhtasari wa mwingiliano wote na matokeo ya sub-agents wote, kisha hurudisha muhtasari huo kama jibu la mwisho. Hii hutoa jibu safi, lililounganishwa kwa mtumiaji. |
| **SCORED** | Mfumo hutumia LLM ya ndani kupima majibu ya LAST na SUMMARY dhidi ya ombi la mwanzo la mtumiaji, na kurudisha jibu lolote lenye alama ya juu zaidi. |
Tazama [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) kwa utekelezaji kamili.

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) na uliza:
> - "Je, Supervisor huchagua mawakala gani waite ili kuanzisha?"
> - "Tofauti gani kati ya Supervisor na mifumo ya mtiririko wa Sequential?"
> - "Ninawezaje kubinafsisha tabia ya upangaji wa Supervisor?"

#### Kuelewa Matokeo

Unapotekeleza demo, utaona maelezo yaliyopangwa juu ya jinsi Supervisor anavyoendesha mawakala wengi. Hapa ni maana ya kila sehemu:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Kichwa** kinaanzisha dhana ya mtiririko wa kazi: njia iliyoelekezwa kutoka kusoma faili hadi kutengeneza ripoti.

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

**Mchoro wa Mtiririko wa Kazi** unaonyesha mtiririko wa data kati ya mawakala. Kila wakala ana jukumu maalum:
- **FileAgent** husoma faili kwa kutumia zana za MCP na kuhifadhi maudhui ghafi katika `fileContent`
- **ReportAgent** hutumia maudhui hayo na kutengeneza ripoti iliyopangwa katika `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Ombi la Mtumiaji** linaonyesha kazi. Supervisor husoma hili na kuamua kuanzisha FileAgent → ReportAgent.

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

**Uendeshaji wa Supervisor** unaonyesha mtiririko wa hatua 2 ukiotekelezwa:
1. **FileAgent** husoma faili kupitia MCP na kuhifadhi maudhui
2. **ReportAgent** hupokea maudhui na kutengeneza ripoti iliyopangwa

Supervisor alifanya maamuzi haya **bila msaada wa mtu** kulingana na ombi la mtumiaji.

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

#### Maelezo ya Sifa za Moduli ya Agentic

Mfano unaonyesha vipengele vingi vya hali ya juu vya moduli ya agentic. Tuchunguze kwa karibu Agentic Scope na Agent Listeners.

**Agentic Scope** inaonyesha kumbukumbu ya pamoja ambapo mawakala hurekodi matokeo yao kwa kutumia `@Agent(outputKey="...")`. Hii inaruhusu:
- Mawakala wa baadaye kufikia matokeo ya mawakala wa awali
- Supervisor kuunda jibu la mwisho
- Wewe kuangalia kile kila wakala alichotengeneza

Mchoro ufuatao unaonyesha jinsi Agentic Scope inavyofanya kazi kama kumbukumbu ya pamoja katika mtiririko wa faili-kwa-ripoti — FileAgent anaandika matokeo yake chini ya ufunguo `fileContent`, ReportAgent husoma na kuandika matokeo yake chini ya `report`:

<img src="../../../translated_images/sw/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope hufanya kazi kama kumbukumbu ya pamoja — FileAgent anaandika `fileContent`, ReportAgent husoma na kuandika `report`, na msimbo wako husoma matokeo ya mwisho.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Data ghafi ya faili kutoka kwa FileAgent
String report = scope.readState("report");            // Ripoti iliyopangwa kutoka kwa ReportAgent
```

**Agent Listeners** huruhusu ufuatiliaji na utatuzi wa matatizo wakati wakala anatekeleza kazi. Matokeo ya hatua kwa hatua unayoona kwenye demo yanatokana na AgentListener inayoshikilia kila kuitwa kwa wakala:
- **beforeAgentInvocation** - Huitwa wakati Supervisor anapochagua wakala, ikikuonyesha wakala aliyechaguliwa na sababu
- **afterAgentInvocation** - Huitwa baada ya wakala kumaliza, ikionyesha matokeo yake
- **inheritedBySubagents** - Ikiwa ni kweli, msikilizaji huangalia mawakala wote katika mlinganyo

Mchoro ufuatao unaonyesha mzunguko mzima wa maisha ya Agent Listener, ikiwa ni pamoja na jinsi `onError` inavyoshughulikia makosa wakati wa utekelezaji wa wakala:

<img src="../../../translated_images/sw/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners hushikilia mzunguko wa maisha wa utekelezaji — fuatilia wakati mawakala wanaanza, wanakamilisha, au wanapokumbana na makosa.*

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
        return true; // Sambaza kwa wakala ndogo wote
    }
};
```

Mbali na mfano wa Supervisor, moduli ya `langchain4j-agentic` hutoa mifumo mingi yenye nguvu ya mtiririko wa kazi. Mchoro ufuatao unaonyesha yote matano — kuanzia mitiririko rahisi ya sequential hadi mifumo ya idhini iliyo na mwanadamu:

<img src="../../../translated_images/sw/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Mifumo mitano ya mtiririko wa kazi kwa kuongoza mawakala — kuanzia mitiririko rahisi ya sequential hadi mifumo ya idhini yenye ushiriki wa mwanadamu.*

| Mfano | Maelezo | Matumizi |
|---------|-------------|----------|
| **Sequential** | Tekeleza mawakala kwa mpangilio, matokeo yapelekwa kwa mwakilishi mwingine | Mtiririko: utafiti → uchambuzi → ripoti |
| **Parallel** | Endesha mawakala kwa wakati mmoja | Kazi huru: hali ya hewa + habari + hisa |
| **Loop** | Rudia hadi sharti lifanikishwe | Kuweka alama ya ubora: boresha hadi alama ≥ 0.8 |
| **Conditional** | Elekeza kulingana na masharti | Kainisha → elekeza kwa mtaalamu |
| **Human-in-the-Loop** | Ongeza hatua za uhakiki wa mwanadamu | Mifumo ya idhini, uhakiki wa maudhui |

## Dhana Muhimu

Sasa baada ya kuchunguza MCP na moduli ya agentic ikitokea, hapa ni muhtasari wa lini kutumia kila njia.

Moja ya faida kubwa za MCP ni mfumo wake unaokua. Mchoro ufuatao unaonyesha jinsi itifaki moja ya ulimwengu inavyounganisha programu yako ya AI na seva nyingi za MCP — kuanzia ufikivu wa mfumo wa faili na hifadhidata hadi GitHub, barua pepe, uchujaji wavuti, na zingine:

<img src="../../../translated_images/sw/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP huunda mfumo wa itifaki wa ulimwengu — seva yoyote inayolingana na MCP inaweza kufanya kazi na mteja yeyote wa MCP, kuwezesha kushirikiana kwa zana kati ya programu.*

**MCP** ni bora unapotaka kutumia mifumo ya zana zilizopo, kujenga zana zinazoweza kushirikishwa na programu nyingi, kuunganisha huduma za watu wa tatu kwa itifaki za kawaida, au kubadilisha utekelezaji wa zana bila kubadilisha msimbo.

**Moduli ya Agentic** hufanya kazi vyema unapotaka ufafanuzi wa wakala ulioelezwa kwa `@Agent`, unahitaji upangaji wa mtiririko wa kazi (sequential, loop, parallel), unapendelea muundo wa wakala kwa msingi wa interface badala ya msimbo wa amri, au unapochanganya mawakala kadhaa yanayoshiriki matokeo kupitia `outputKey`.

**Mfano wa Supervisor Agent** huonekana pale ambapo mtiririko wa kazi hauwezi kutabirika mapema na unataka LLM ichukue maamuzi, unapokuwa na mawakala walio na utaalamu tofauti zinazohitaji upangaji wa mabadiliko, wakati wa kujenga mifumo ya mazungumzo yanayoelekeza kwa uwezo tofauti, au wakati unataka tabia ya wakala yenye kubadilika na kuendeshwa.

Kusaidia uamuzi kati ya njia za `@Tool` maalum kutoka Moduli 04 na zana za MCP kutoka moduli hii, kulinganisha lifuatalo linaonyesha tofauti kuu — zana maalum zinakupa unganisho wa karibu na usalama wa aina kwa mantiki maalum ya programu, wakati zana za MCP zinatoa muunganisho ulio sawa na unaoweza kutumika tena:

<img src="../../../translated_images/sw/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

[Nini wakati wa kutumia mbinu za @Tool maalum dhidi ya zana za MCP — zana maalum kwa mantiki maalum ya programu yenye usalama wa aina kamili, zana za MCP kwa muunganisho wa viwango unaofanya kazi kati ya programu.]

## Hongera!

Umefikia mwisho wa moduli zote tano za kozi ya LangChain4j kwa Waanzaji! Hapa kuna mtazamo wa safari kamili ya kujifunza uliyoimaliza — kuanzia mazungumzo ya msingi hadi mifumo ya agentic inayotumia MCP:

<img src="../../../translated_images/sw/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Safari yako ya kujifunza kupitia moduli zote tano — kuanzia mazungumzo ya msingi hadi mifumo ya agentic inayotumia MCP.*

Umehitimu kozi ya LangChain4j kwa Waanzaji. Umejifunza:

- Jinsi ya kujenga AI ya mazungumzo yenye kumbukumbu (Moduli 01)
- Mifano ya uhandisi wa madrasha kwa kazi tofauti (Moduli 02)
- Kuweka majibu msingi kwenye nyaraka zako kwa RAG (Moduli 03)
- Kuunda mawakala wa AI wa msingi (wasaidizi) kwa zana maalum (Moduli 04)
- Kuunganisha zana za viwango na moduli za LangChain4j MCP na Agentic (Moduli 05)

### Nini Kifuatacho?

Baada ya kukamilisha moduli, chunguza [Mwongozo wa Uthibitisho](../docs/TESTING.md) kuona dhana za upimaji wa LangChain4j zikifanyiwa kazi.

**Rasilimali Rasmi:**
- [Nyaraka za LangChain4j](https://docs.langchain4j.dev/) - Miongozo kamili na rejea ya API
- [GitHub ya LangChain4j](https://github.com/langchain4j/langchain4j) - Msimbo chanzo na mifano
- [Mafunzo ya LangChain4j](https://docs.langchain4j.dev/tutorials/) - Mafunzo ya hatua kwa hatua kwa matumizi mbalimbali

Asante kwa kukamilisha kozi hii!

---

**Usogezaji:** [← Iliyotangulia: Moduli 04 - Zana](../04-tools/README.md) | [Rudi Kwenye Msingi](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tangazo la Kukataa**:  
Nyaraka hii imetafsiriwa kwa kutumia huduma ya utafsiri wa AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kufanikisha usahihi, tafadhali fahamu kuwa tafsiri zinazotekelezwa kiotomatiki zinaweza kuwa na makosa au upotoshaji. Nyaraka ya asili katika lugha yake ya msingi inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa taarifa muhimu sana, tafsiri ya kitaalamu ya binadamu inapendekezwa. Hatubebwi mzigo wa dhana potofu au tafsiri za makosa zinazotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->