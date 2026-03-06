# Moduli 05: Itifaki ya Muktadha wa Mfano (MCP)

## Muhtasari wa Yaliyomo

- [Utajifunza Nini](../../../05-mcp)
- [MCP ni Nini?](../../../05-mcp)
- [MCP Inavyofanya Kazi](../../../05-mcp)
- [Moduli ya Wakala](../../../05-mcp)
- [Kufanya Mifano Kazi](../../../05-mcp)
  - [Mambo Muhimu Kabla ya Kuanza](../../../05-mcp)
- [Anza Haraka](../../../05-mcp)
  - [Uendeshaji Faili (Stdio)](../../../05-mcp)
  - [Mwakala Mkufunzi](../../../05-mcp)
    - [Kufanya Demo Kazi](../../../05-mcp)
    - [Mwakala Mkufunzi Anavyofanya Kazi](../../../05-mcp)
    - [Jinsi FileAgent Inavyogundua Vifaa vya MCP Wakati wa Kutekeleza](../../../05-mcp)
    - [Mikakati ya Majibu](../../../05-mcp)
    - [Kuelewa Matokeo](../../../05-mcp)
    - [Ufafanuzi wa Vipengele vya Moduli ya Wakala](../../../05-mcp)
- [Mafahamu Muhimu](../../../05-mcp)
- [Hongera!](../../../05-mcp)
  - [Nini Ifuatayo?](../../../05-mcp)

## Utajifunza Nini

Umejenga AI ya mazungumzo, umemiliki utangulizi, umeimarisha majibu katika nyaraka, na umeunda mawakala wenye vifaa. Lakini vifaa vyote hivyo vilijengwa mahsusi kwa ajili ya programu yako maalum. Je, vingekuwaje kama ungeweza kumpa AI yako upatikanaji wa ikolojia ya kawaida ya vifaa ambavyo mtu yeyote anaweza kuunda na kushiriki? Katika moduli hii, utajifunza jinsi ya kufanya hivyo kwa kutumia Itifaki ya Muktadha wa Mfano (MCP) na moduli ya wakala ya LangChain4j. Kwanza tunaonyesha msomaji rahisi wa faili wa MCP kisha tunaonyesha jinsi unavyoungana kwa urahisi na mikondo ya kazi ya wakala wa hali ya juu kwa kutumia mfano wa Mwakala Mkufunzi.

## MCP ni Nini?

Itifaki ya Muktadha wa Mfano (MCP) hutoa hiyo hasa - njia ya kawaida kwa programu za AI kugundua na kutumia vifaa vya nje. Badala ya kuandika miunganiko maalum kwa kila chanzo cha data au huduma, unaungana na seva za MCP zinazotangaza uwezo wao kwa muundo thabiti. Wakala wako wa AI basi anaweza kugundua na kutumia vifaa hivi moja kwa moja.

Mchoro hapa chini unaonyesha tofauti — bila MCP, kila unganisho linahitaji waya maalum kutoka sehemu moja hadi nyingine; kwa MCP, itifaki moja inaunganisha programu yako kwa kifaa chochote:

<img src="../../../translated_images/sw/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Kabala MCP: Miunganiko tata kutoka sehemu hadi sehemu. Baada ya MCP: Itifaki moja, fursa zisizo na kikomo.*

MCP inatatua tatizo msingi katika maendeleo ya AI: kila muunganiko ni wa kipekee. Unataka kufikia GitHub? Kanuni maalum. Unataka kusoma faili? Kanuni maalum. Unataka kuuliza hifadhidata? Kanuni maalum. Na miunganiko hii yote haifanyi kazi na programu nyingine za AI.

MCP inafanya iwe sawa. Seva ya MCP inatangaza vifaa vyenye maelezo wazi na miundo. Mteja yeyote wa MCP anaweza kuungana, kugundua vifaa vinavyopatikana, na kuzitumia. Jenga mara moja, tumia kila mahali.

Mchoro huu hapa chini unaonyesha usanifu huu — mteja mmoja wa MCP (programu yako ya AI) unaunganisha na seva nyingi za MCP, kila moja ikitangaza seti yake ya vifaa kupitia itifaki ya kawaida:

<img src="../../../translated_images/sw/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Usanifu wa Itifaki ya Muktadha wa Mfano - ugunduzi na utekelezaji wa vifaa kwa njia thabiti*

## MCP Inavyofanya Kazi

Chini ya uso, MCP inatumia usanifu wa tabaka. Programu yako ya Java (mteja wa MCP) hugundua vifaa vinavyopatikana, hutuma maombi ya JSON-RPC kupitia safu ya usafirishaji (Stdio au HTTP), na seva ya MCP hufanya shughuli na kurejesha matokeo. Mchoro ufuatao unavunja kila tabaka ya itifaki hii:

<img src="../../../translated_images/sw/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Jinsi MCP inavyofanya kazi chini ya uso — wateja hugundua vifaa, kubadilishana ujumbe wa JSON-RPC, na kutekeleza shughuli kupitia safu ya usafirishaji.*

**Usanifu wa Seva-Mteja**

MCP inatumia mfano wa mteja-seva. Seva hutoa vifaa - kusoma faili, kuuliza hifadhidata, kuita API. Wateja (programu yako ya AI) huungana na seva na kutumia vifaa vyao.

Ili kutumia MCP na LangChain4j, ongeza utegemezi huu wa Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Ugunduzi wa Vifaa**

Wakati mteja wako anapounganisha na seva ya MCP, wanauliza "Je, mnayo vifaa gani?" Seva hurejesha orodha ya vifaa vinavyopatikana, kila moja likiwa na maelezo na miundo ya vigezo. Wakala wako wa AI anaweza kisha kuamua vifaa gani vitumike kulingana na maombi ya mtumiaji. Mchoro huu hapa chini unaonyesha handshake hii — mteja hutuma ombi la `tools/list` na seva hurudisha vifaa vyake vinavyopatikana pamoja na maelezo na miundo ya vigezo:

<img src="../../../translated_images/sw/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI inagundua vifaa vinavyopatikana wakati wa kuanzisha — sasa inajua uwezo uliopo na inaweza kuamua ni vipi vitakavyotumika.*

**Mekanismi za Usafirishaji**

MCP inaunga mkono meknanismi tofauti za usafirishaji. Chaguo mbili ni Stdio (kwa mawasiliano ya mchakato mdogo wa ndani) na Streamable HTTP (kwa seva za mbali). Moduli hii inaonyesha usafirishaji wa Stdio:

<img src="../../../translated_images/sw/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*Mekanismi za usafirishaji wa MCP: HTTP kwa seva za mbali, Stdio kwa michakato ya ndani*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Kwa michakato ya ndani. Programu yako huanzisha seva kama mchakato mdogo na huwasiliana kupitia pembejeo/matokeo ya kawaida. Inafaa kwa upatikanaji wa mfumo wa faili au vifaa vya amri ya mstari.

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

Seva ya `@modelcontextprotocol/server-filesystem` inatangaza vifaa vifuatavyo, vyote vikiwa katika mazingira yaliyowekwa kwenye folda ulizozitaja:

| Kifaa | Maelezo |
|-------|----------|
| `read_file` | Soma yaliyomo ya faili moja |
| `read_multiple_files` | Soma faili nyingi kwa mwito mmoja |
| `write_file` | Tengeneza au andika juu ya faili |
| `edit_file` | Fanya mabadiliko ya kutafuta-na-kubadilisha kwa ufanisi |
| `list_directory` | Orodhesha faili na folda kwenye njia fulani |
| `search_files` | Tafuta faili kwa kizuizi cha marudio kinacholingana na muundo |
| `get_file_info` | Pata metadata ya faili (ukubwa, muda, ruhusa) |
| `create_directory` | Tengeneza folda (ikiwa ni pamoja na folda mama) |
| `move_file` | Hamisha au badilisha jina la faili au folda |

Mchoro hapa chini unaonyesha jinsi usafirishaji wa Stdio unavyoendelea wakati wa kutekeleza — programu yako ya Java huanzisha seva ya MCP kama mchakato mdogo na huwa wanawasiliana kupitia pombi za stdin/stdout, bila mtandao au HTTP:

<img src="../../../translated_images/sw/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Usafirishaji wa Stdio ukiwa kazini — programu yako huanzisha seva ya MCP kama mchakato mdogo na kuwasiliana kupitia pombi za stdin/stdout.*

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) na uliza:
> - "Usafirishaji wa Stdio unafanya kazi vipi na ni lini ni lazima nitumie badala ya HTTP?"
> - "LangChain4j inasimamia vipi maisha ya michakato ya seva za MCP inayozalishwa?"
> - "Ni athari gani za usalama zinazoletwa na kumpa AI upatikanaji wa mfumo wa faili?"

## Moduli ya Wakala

Wakati MCP inatoa vifaa vilivyobinafsishwa, moduli ya wakala ya LangChain4j hutoa njia iliyo andikwa kuunda mawakala wanaoendesha vifaa hivyo. Alama ya `@Agent` na `AgenticServices` hukuwezesha kufafanua tabia ya wakala kupitia interface badala ya msimbo wa amri.

Katika moduli hii, utachunguza mfano wa **Mwakala Mkufunzi** — mbinu ya hali ya juu ya AI wa wakala ambapo "mkufunzi" anaamua kwa hiari ni mawakala gani wa ndogo watakaotumika kulingana na maombi ya mtumiaji. Tutachanganya dhana hizi mbili kwa kumpa mojawapo ya mawakala wetu ndogo uwezo wa kupata faili kwa nguvu za MCP.

Ili kutumia moduli ya wakala, ongeza utegemezi huu wa Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Kumbuka:** Moduli ya `langchain4j-agentic` inatumia mali tofauti ya toleo (`langchain4j.mcp.version`) kwa sababu hutolewa kwa ratiba tofauti na maktaba kuu za LangChain4j.

> **⚠️ Jaribio:** Moduli ya `langchain4j-agentic` ni **jaribio** na inaweza kubadilika. Njia thabiti ya kujenga wasaidizi wa AI bado ni kutumia `langchain4j-core` na vifaa maalum (Moduli 04).

## Kufanya Mifano Kazi

### Mambo Muhimu Kabla ya Kuanza

- Kumaliza [Moduli 04 - Vifaa](../04-tools/README.md) (moduli hii inajengwa juu ya dhana za vifaa maalum na kuzilinganisha na MCP)
- Faili `.env` katika saraka kuu yenye sera za Azure (iliyoundwa kwa `azd up` katika Moduli 01)
- Java 21+, Maven 3.9+
- Node.js 16+ na npm (kwa seva za MCP)

> **Kumbuka:** Ikiwa bado hujasanidi mabadiliko ya mazingira yako, angalia [Moduli 01 - Utangulizi](../01-introduction/README.md) kwa maelekezo ya utumaji (`azd up` huunda faili `.env` moja kwa moja), au nakili `.env.example` hadi `.env` katika saraka kuu na ujaze maadili yako.

## Anza Haraka

**Ukifanya kazi na VS Code:** Bofya kulia kwenye faili yoyote ya majaribio kwenye Explorer na chagua **"Run Java"**, au tumia usanidi wa kuzindua kutoka kwenye jopo la Run and Debug (hakikisha faili yako `.env` imewekwa na taarifa za Azure kwanza).

**Ukifanya kazi na Maven:** Vinginevyo, unaweza kuendesha kutoka kwenye mstari wa amri kwa mifano ifuatayo.

### Uendeshaji Faili (Stdio)

Hii inaonyesha vifaa vinavyotumia michakato ya ndani.

**✅ Hakuna mambo ya awali yanayohitajika** - seva ya MCP huanzishwa moja kwa moja.

**Ukifuatia Skripti za Kuanzisha (Inayopendekezwa):**

Skripti za kuanzisha huchukua moja kwa moja mabadiliko ya mazingira kutoka faili `.env` ya mzizi:

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

**Ukifanya kazi na VS Code:** Bofya kulia `StdioTransportDemo.java` na chagua **"Run Java"** (hakikisha faili yako `.env` imewekwa).

Programu huanzisha seva ya MCP wa mfumo wa faili moja kwa moja na husoma faili ya ndani. Angalia jinsi usimamizi wa mchakato mdogo unavyosimamiwa kwa niaba yako.

**Matokeo Yanayotarajiwa:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Mwakala Mkufunzi

Mfano wa **Mwakala Mkufunzi** ni aina ya **ubunifu** wa AI wa wakala. Mkufunzi hutumia LLM kuamua kwa hiari mawakala gani wawahusishe kulingana na ombi la mtumiaji. Katika mfano ujao, tunachanganya upatikanaji wa faili wenye nguvu za MCP na wakala wa LLM kuunda mtiririko wa kazi wa kusoma faili → ripoti.

Katika demo, `FileAgent` husoma faili kwa kutumia vifaa vya MCP vya mfumo wa faili, na `ReportAgent` hutengeneza ripoti yenye muhtasari wa utendaji (sentensi 1), pointi kuu 3, na mapendekezo. Mkufunzi huongoza mtiririko huu moja kwa moja:

<img src="../../../translated_images/sw/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Mkufunzi hutumia LLM yake kuamua mawakala gani wawahusishe na kwa mpangilio gani — hakuna njia za mkato zilizoandikwa ngumu zinazohitajika.*

Huu hapa ni mtiririko halisi wa kazi kwa bomba letu la faili hadi ripoti:

<img src="../../../translated_images/sw/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent husoma faili kupitia vifaa vya MCP, halafu ReportAgent hubadilisha yaliyomo ghafi kuwa ripoti yenye muundo.*

Mchoro ufuatao unaonyesha mfuatano kamili wa usimamizi wa Mkufunzi — kutoka kuanzisha seva ya MCP, kupitia uteuzi wa wakala wa hiari wa Mkufunzi, hadi kwa simu za vifaa kupitia stdio na ripoti ya mwisho:

<img src="../../../translated_images/sw/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*Mkufunzi huwahusisha moja kwa moja FileAgent (anayewaita seva ya MCP kupitia stdio kusoma faili), halafu huwahusisha ReportAgent kuunda ripoti yenye muundo — kila wakala anahifadhi matokeo yake katika Nafasi ya Wakala (Agentic Scope) iliyo gawanyika.*

Kila wakala anahifadhi matokeo yake katika **Nafasi ya Wakala** (Agentic Scope) (kumbukumbu ya pamoja), ikiruhusu mawakala wa baadaye kufikia matokeo ya awali. Hii inaonyesha jinsi vifaa vya MCP vinaunganishwa bila mshono kwenye mifumo ya kazi ya wakala — Mkufunzi hahitaji kujua *jinsi* faili husomwa, bali anajua tu `FileAgent` ana uwezo wa kufanya hivyo.

#### Kufanya Demo Kazi

Skripti za kuanzisha huchukua moja kwa moja mabadiliko ya mazingira kutoka faili `.env` ya mzizi:

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

**Ukifanya kazi na VS Code:** Bofya kulia `SupervisorAgentDemo.java` na chagua **"Run Java"** (hakikisha faili yako `.env` imewekwa).

#### Mwakala Mkufunzi Anavyofanya Kazi

Kabla ya kujenga mawakala, unahitaji kuunganisha usafirishaji wa MCP kwa mteja na kuufunika kama `ToolProvider`. Hii ndio jinsi vifaa vya seva MCP vinavyopatikana kwa mawakala wako:

```java
// Unda mteja wa MCP kutoka kwa usafirishaji
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Weka mteja kama ToolProvider — hii inaunganisha zana za MCP katika LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Sasa unaweza kuingiza `mcpToolProvider` kwa wakala yoyote anayeihitaji vifaa vya MCP:

```java
// Hatua ya 1: FileAgent husoma faili kwa kutumia zana za MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Ina zana za MCP kwa shughuli za faili
        .build();

// Hatua ya 2: ReportAgent hutoa ripoti zilizopangwa
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Msimamizi huandaa mtiririko wa kazi wa faili → ripoti
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Rudisha ripoti ya mwisho
        .build();

// Msimamizi huamua mawakala gani waite kwa msingi wa ombi
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Jinsi FileAgent Inavyogundua Vifaa vya MCP Wakati wa Kutekeleza

Unaweza kujiuliza: **je, `FileAgent` inajua jinsi ya kutumia vifaa vya mfumo wa faili vya npm vipi?** Jibu ni kwamba haijui — **LLM** huchambua kwa wakati wa kutekeleza kupitia miundo ya vifaa.

Interface ya `FileAgent` ni ufafanuzi tu wa agizo. Haina maarifa yaliyoandikwa ngumu ya `read_file`, `list_directory`, au kifaa kingine cha MCP. Hapa ni kile kinachotokea mwishowe:
1. **Seva huchipua:** `StdioMcpTransport` huanzisha kifurushi cha npm cha `@modelcontextprotocol/server-filesystem` kama mchakato mdogo  
2. **Ugundaji wa zana:** `McpClient` hutuma ombi la JSON-RPC la `tools/list` kwa seva, ambayo hutuma majina ya zana, maelezo, na skimu za vigezo (kwa mfano, `read_file` — *"Soma maudhui kamili ya faili"* — `{ path: string }`)  
3. **Ingiza skimu:** `McpToolProvider` huzunguka skimu hizi zilizogunduliwa na kuziwapatia LangChain4j  
4. **LLM huamua:** Wakati `FileAgent.readFile(path)` inapoitwa, LangChain4j hutuma ujumbe wa mfumo, ujumbe wa mtumiaji, **na orodha ya skimu za zana** kwa LLM. LLM husoma maelezo ya zana na hutengeneza simu ya zana (kwa mfano, `read_file(path="/some/file.txt")`)  
5. **Utekelezaji:** LangChain4j hushika simu ya zana, kuituma kupitia MCP client kurudi kwa mchakato ndogo wa Node.js, hupata matokeo, na kuirudisha kwa LLM  

Hii ni ile ile [Tool Discovery](../../../05-mcp) mbinu iliyotajwa hapo juu, lakini imechukuliwa mahsusi kwa mtiririko wa kazi wa wakala. Maandiko ya `@SystemMessage` na `@UserMessage` huelekeza tabia ya LLM, wakati `ToolProvider` aliyeingizwa humpa uwezo — LLM huunganisha viwili hivi wakati wa utekelezaji.

> **🤖 Jaribu kwa [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) na uliza:  
> - "Je, wakala huyu anajua vipi zana gani za MCP ya kuita?"  
> - "Nini kingetokea kama ningetenga ToolProvider kutoka kwenye muundaji wa wakala?"  
> - "Je, skimu za zana hupitishwa vipi kwa LLM?"

#### Mikakati ya Majibu

Unapopanga `SupervisorAgent`, unabainisha jinsi inavyopaswa kutengeneza jibu lake la mwisho kwa mtumiaji baada ya mawakala mdogo kumaliza kazi zao. Mchoro hapa chini unaonyesha mikakati mitatu inayopatikana — LAST hurudisha moja kwa moja matokeo ya wakala wa mwisho, SUMMARY huunganisha matokeo yote kupitia LLM, na SCORED huchagua ile yenye alama ya juu dhidi ya ombi la awali:

<img src="../../../translated_images/sw/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Mikakati mitatu ya jinsi Supervisor anavyounda jibu la mwisho — chagua kulingana na kama unataka matokeo ya wakala wa mwisho, muhtasari uliounganishwa, au chaguo lenye alama bora.*

Mikakati inayopatikana ni:

| Mkakati | Maelezo |  
|----------|-------------|  
| **LAST** | msimamizi hurudisha matokeo ya wakala mdogo au zana iliyotumika mwisho. Hii ni muhimu wakati wakala wa mwisho katika mtiririko wa kazi amebuniwa mahsusi kutoa jibu kamili, la mwisho (mfano, "Wakala wa Muhtasari" katika mchakato wa utafiti). |  
| **SUMMARY** | msimamizi hutumia Mfano wake wa Lugha (LLM) kuunganisha muhtasari wa mwingiliano mzima na matokeo yote ya mawakala mdogo, kisha hurudisha muhtasari huo kama jibu la mwisho. Hii hutoa jibu safi, lililounganishwa kwa mtumiaji. |  
| **SCORED** | mfumo hutumia LLM ya ndani kupima na jibu la LAST na SUMMARY ya mwingiliano dhidi ya ombi la awali la mtumiaji, hurudisha matokeo ambayo yana alama ya juu zaidi. |

Tazama [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) kwa utekelezaji kamili.

> **🤖 Jaribu kwa [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) na uliza:  
> - "Msimamizi huchagua vipi mawakala gani waita?"  
> - "Tofauti gani kati ya Supervisor na mifumo ya muundo wa mtiririko mfululizo?"  
> - "Ninawezaje kubinafsisha tabia ya upangaji wa Msimamizi?"

#### Kuelewa Matokeo

Unapoendesha demo, utaona maelezo yaliyo na muundo ya jinsi Msimamizi anavyoratibu mawakala wengi. Hapa ni maana ya kila sehemu:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Kichwa** kinatambulisha dhana ya mtiririko wa kazi: mchakato wa kuzingatia kutoka kusoma faili hadi kutengeneza ripoti.

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
- **FileAgent** husoma faili kwa kutumia zana za MCP na kuhifadhi yaliyomo yasiyoharibika katika `fileContent`  
- **ReportAgent** hutumia yaliyomo hayo na kutengeneza ripoti iliyopangwa katika `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Ombi la Mtumiaji** linaonyesha kazi. Msimamizi huchambua hili na kuamua kuitisha FileAgent → ReportAgent.

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
  
**Usimamizi wa Mtiririko wa Kazi** unaonyesha mtiririko wa hatua 2 ukiwa hai:  
1. **FileAgent** husoma faili kupitia MCP na kuhifadhi yaliyomo  
2. **ReportAgent** hupokea yaliyomo na hutengeneza ripoti iliyopangwa

Msimamizi alifanya maamuzi haya **kwa kujitegemea** kulingana na ombi la mtumiaji.

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

Mfano unaonyesha sifa kadhaa za hali ya juu za moduli ya agentic. Tuchunguze kwa karibu Agentic Scope na Agent Listeners.

**Agentic Scope** inaonyesha kumbukumbu ya pamoja ambapo mawakala walihifadhi matokeo yao kwa kutumia `@Agent(outputKey="...")`. Hii inaruhusu:  
- mawakala waliokuja baadaye kufikia matokeo ya mawakala wa awali  
- Msimamizi kuunganisha jibu la mwisho  
- Wewe kuchunguza kile kila wakala alichotengeneza

Mchoro hapa chini unaonyesha jinsi Agentic Scope inavyofanya kazi kama kumbukumbu ya pamoja katika mtiririko wa faili-kwa-ripoti — FileAgent huandika matokeo yake chini ya ufunguo `fileContent`, ReportAgent husoma na kuandika matokeo yake chini ya `report`:

<img src="../../../translated_images/sw/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope hutumika kama kumbukumbu ya pamoja — FileAgent huandika `fileContent`, ReportAgent husoma na kuandika `report`, na msimbo wako husoma matokeo ya mwisho.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Data ghafi ya faili kutoka kwa FileAgent
String report = scope.readState("report");            // Ripoti iliyopangwa kutoka kwa ReportAgent
```
  
**Agent Listeners** huruhusu ufuatiliaji na utambuzi wa makosa wakati wa utekelezaji wa wakala. Matokeo ya hatua kwa hatua unayoiona kwenye demo yanatokana na AgentListener anayeingia kila mawasilisho ya wakala:  
- **beforeAgentInvocation** - Inaitwa wakati Msimamizi anapochagua wakala, kukuwezesha kuona wakala aliyechaguliwa na sababu  
- **afterAgentInvocation** - Inaitwa wakala anapomaliza, kuonyesha matokeo yake  
- **inheritedBySubagents** - Inapotumika kweli, msikilizaji hufuatilia mawakala wote katika mlolongo

Mchoro ufuatao unaonesha mzunguko mzima wa maisha ya Agent Listener, ikiwa ni pamoja na jinsi `onError` inavyosimamia makosa wakati wa utekelezaji wa wakala:

<img src="../../../translated_images/sw/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners huingiza mzunguko wa maisha wa utekelezaji — fuatilia wakati mawakala wananza, wanamaliza, au wanakutana na makosa.*

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
        return true; // Sambaza kwa maajenti wote ndogo
    }
};
```
  
Zaidi ya muundo wa Msimamizi, moduli ya `langchain4j-agentic` hutoa mifumo yenye nguvu kadhaa ya mtiririko wa kazi. Mchoro hapa chini unaonyesha yote matano — kutoka mitiririko ya mfululizo rahisi hadi mitiririko ya idhini ya binadamu katika mzunguko wa maombi:

<img src="../../../translated_images/sw/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Mifumo mitano ya mtiririko wa kazi kwa kuratibu mawakala — kutoka mitiririko mfululizo rahisi hadi mitiririko ya idhini ya binadamu.*

| Mfano | Maelezo | Matumizi |  
|---------|-------------|----------|  
| **Sequential** | Endesha mawakala kwa mpangilio, matokeo hutiririka kwa wakala anayefuata | Mitiririko: utafiti → uchambuzi → ripoti |  
| **Parallel** | Anzisha mawakala kwa wakati mmoja | Kazi huru: hali ya hewa + habari + hisa |  
| **Loop** | Rudia hadi sharti litimizwe | Upimaji wa ubora: boresha hadi alama ≥ 0.8 |  
| **Conditional** | Elekeza kulingana na masharti | Kuingiza darasa → elekeza kwa wakala mtaalamu |  
| **Human-in-the-Loop** | Ongeza pointi za ukaguzi wa binadamu | Mitiririko ya idhini, ukaguzi wa maudhui |

## Dhana Muhimu

Sasa baada ya kuchunguza MCP na moduli ya agentic ikifanya kazi, hebu tufupishe lini kutumia kila njia.

Moja ya faida kuu za MCP ni ekosistemi yake inayokua. Mchoro hapa chini unaonyesha jinsi itifaki ya umoja inavyounganisha programu yako ya AI kwa aina mbali mbali za seva za MCP — kutoka upatikanaji wa mfumo wa faili na hifadhidata hadi GitHub, barua pepe, uchujaji wa wavuti, na zaidi:

<img src="../../../translated_images/sw/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP huunda ekosistemi ya itifaki ya umoja — seva yoyote inayoungwa mkono na MCP inafanya kazi na mteja yeyote anayoungwa mkono na MCP, kuwaruhusu kushirikiana kwa zana kati ya programu.*

**MCP** ni mzuri unapotaka kutumia ekosistemi za zana zilizopo, kuunda zana ambazo programu nyingi zinaweza kushirikiana, kuunganisha huduma za wahusika wa tatu kwa itifaki za kawaida, au kubadilisha utekelezaji wa zana bila kubadilisha msimbo.

**Moduli ya Agentic** hufanya vizuri zaidi wakati unahitaji maelezo ya wakala ya aina ya kueleza (declarative) kwa kutumia maandiko ya `@Agent`, unahitaji upangaji wa mtiririko wa kazi (mfululizo, mzunguko, sambamba), unapendelea muundo wa wakala unaotegemea interface badala ya msimbo wa amri, au unapochanganya mawakala wengi wanaoshirikiana matokeo kupitia `outputKey`.

**Mfano wa Msimamizi wa Wakala** huangaza wakati mtiririko wa kazi haujulikani kabla na unataka LLM iaamue, wakati una mawakala maalum wengi yanayotaka upangaji wa nguvu, wakati unajenga mifumo ya mazungumzo inayotoa njia kwa uwezo mbalimbali, au unapotaka tabia ya wakala iliyo rahisi kubadilika na kuendana.

Kusaidia kuamua kati ya mbinu za kawaida za `@Tool` kutoka Moduli 04 na zana za MCP kutoka moduli hii, kulinganisha ifuatayo linaonyesha tofauti kuu — zana za kawaida zinakupa muunganisho mkali na usalama wa aina kwa mantiki ya programu, wakati zana za MCP hutoa ushirikiano ulioboreshwa, unaoweza kutumika tena:

<img src="../../../translated_images/sw/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Nini cha kutumia mbinu za kawaida za @Tool dhidi ya zana za MCP — zana za kawaida kwa mantiki ya programu na usalama wa aina kamili, zana za MCP kwa ushirikiano uliopangwa na unaofanya kazi kwa programu nyingi.*

## Hongera!

Umefika mwisho wa moduli zote tano za kozi ya LangChain4j kwa Waanzilishi! Hapa kuna picha ya safari nzima ya kujifunza uliyoipitia — kutoka mazungumzo ya msingi hadi mifumo ya agentic yenye nguvu ya MCP:

<img src="../../../translated_images/sw/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Safari yako ya kujifunza kupitia moduli zote tano — kutoka mazungumzo ya msingi hadi mifumo ya agentic yenye nguvu ya MCP.*

Umefaulu kozi ya LangChain4j kwa Waanzilishi. Umejifunza:

- Jinsi ya kujenga AI ya mazungumzo yenye kumbukumbu (Moduli 01)  
- Mifumo ya uhandisi wa msemo kwa kazi tofauti (Moduli 02)  
- Kuweka misingi ya majibu katika nyaraka zako kwa RAG (Moduli 03)  
- Kutengeneza mawakala wa AI wa msingi (msaidizi) kwa zana za kawaida (Moduli 04)  
- Kuunganisha zana zilizo sanifu na LangChain4j MCP na moduli za Agentic (Moduli 05)

### Nini Kifuatayo?

Baada ya kumaliza moduli, chunguza [Mwongozo wa Upimaji](../docs/TESTING.md) kuona dhana za upimaji za LangChain4j zikitumika.

**Rasilimali Rasmi:**  
- [Nyaraka za LangChain4j](https://docs.langchain4j.dev/) – Miongozo kamili na rejeleo la API  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) – Chanzo cha msimbo na mifano  
- [Mafunzo ya LangChain4j](https://docs.langchain4j.dev/tutorials/) – Mafunzo hatua kwa hatua kwa matumizi mbalimbali

Asante kwa kumaliza kozi hii!

---

**Uelekeo:** [← Ile iliyotangulia: Moduli 04 - Zana](../04-tools/README.md) | [Rudi Kwenye Msingi](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Kiaragano**:
Nyaraka hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kuwa sahihi, tafadhali fahamu kuwa tafsiri za kiotomatiki zinaweza kuwa na makosa au kutokukamilika. Nyaraka ya asili katika lugha yake ya asili inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya mtaalamu wa binadamu inapendekezwa. Hatubebei dhima kwa uelewa au tafsiri potofu zinazotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->