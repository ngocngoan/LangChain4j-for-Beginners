# Moduli 05: Itifaki ya Muktadha wa Mfano (MCP)

## Jedwali la Yaliyomo

- [Utachojifunza](../../../05-mcp)
- [Nini MCP?](../../../05-mcp)
- [Jinsi MCP Inavyofanya Kazi](../../../05-mcp)
- [Moduli ya Wakala](../../../05-mcp)
- [Kukimbia Mifano](../../../05-mcp)
  - [Mahitaji ya Awali](../../../05-mcp)
- [Anza Haraka](../../../05-mcp)
  - [Uendeshaji wa Faili (Stdio)](../../../05-mcp)
  - [Wakala Msimamizi](../../../05-mcp)
    - [Kukimbia Demo](../../../05-mcp)
    - [Jinsi Msimamizi Anavyofanya Kazi](../../../05-mcp)
    - [Mikakati ya Majibu](../../../05-mcp)
    - [Kuelewa Matokeo](../../../05-mcp)
    - [Maelezo ya Sifa za Moduli ya Wakala](../../../05-mcp)
- [Dhana Muhimu](../../../05-mcp)
- [Hongera!](../../../05-mcp)
  - [Nini Kifuatacho?](../../../05-mcp)

## Utachojifunza

Umekuja na AI ya mazungumzo, umemiliki prompts, umeweka majibu msingi kwenye nyaraka, na umeunda mawakala wenye zana. Lakini zana zote hizo zilijengwa maalum kwa matumizi yako. Je, ungehisi vipi ikiwa unaweza kumpa AI yako upatikanaji wa mazingira ya zana zilizopangwa kwa viwango ambazo mtu yeyote anaweza kuunda na kushiriki? Katika moduli hii, utajifunza jinsi ya kufanya hivyo kwa kutumia Itifaki ya Muktadha wa Mfano (MCP) na moduli ya wakala ya LangChain4j. Kwanza tunaonyesha msomaji rahisi wa faili wa MCP kisha tunaonyesha jinsi unavyojumuishwa kwa urahisi kwenye mtiririko wa kazi wa wakala wa hali ya juu ukitumia mfano wa Wakala Msimamizi.

## Nini MCP?

Itifaki ya Muktadha wa Mfano (MCP) hutoa hasa hilo - njia ya kawaida kwa programu za AI kugundua na kutumia zana za nje. Badala ya kuandika uunganishaji maalum kwa kila chanzo cha data au huduma, unajiunga na seva za MCP zinazoweka wazi uwezo wao kwa muundo thabiti. Wakala wako wa AI anaweza kisha kugundua na kutumia zana hizi moja kwa moja.

<img src="../../../translated_images/sw/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Kabla ya MCP: Uunganishaji tata wa moja kwa moja. Baada ya MCP: Itifaki moja, fursa zisizo na kikomo.*

MCP inatatua tatizo msingi katika maendeleo ya AI: kila uunganishaji ni maalum. Unataka kufikia GitHub? Msimbo maalum. Unataka kusoma faili? Msimbo maalum. Unataka kuulizia hifadhidata? Msimbo maalum. Na hakuna uunganishaji wowote kati ya hayo unavyoendana na programu nyingine za AI.

MCP inafanya iwe za kiwango hicho. Seva ya MCP inaweka wazi zana na maelezo na miundo ya parameta wazi. Kila mteja wa MCP anaweza kuungana, kugundua zana zinazopatikana, na kuzitumia. Unda mara moja, tumia kila mahali.

<img src="../../../translated_images/sw/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Miundo ya Itifaki ya Muktadha wa Mfano - kugundua zana kwa kiwango thabiti na utekelezaji*

## Jinsi MCP Inavyofanya Kazi

<img src="../../../translated_images/sw/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Jinsi MCP inavyofanya kazi chini ya uso — wateja hugundua zana, kubadilishana ujumbe wa JSON-RPC, na kutekeleza shughuli kupitia tabaka la usafirishaji.*

**Muundo wa Seva-Mteja**

MCP hutumia mfano wa mteja-seva. Seva hutoa zana - kusoma faili, kuulizia hifadhidata, kupiga simu API. Wateja (programu yako ya AI) huungana na seva na kutumia zana zao.

Ili kutumia MCP na LangChain4j, ongeza utegemezi huu wa Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Kugundua Zana**

Unapounganisha mteja wako na seva ya MCP, huuliza "Je, una zana gani?" Seva hunjibu kwa orodha ya zana zinazopatikana, kila moja ikiwa na maelezo na miundo ya vigezo. Wakala wako wa AI anaweza kisha kuamua ni zana gani za kutumia kulingana na maombi ya mtumiaji.

<img src="../../../translated_images/sw/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI hugundua zana zilizopo mwanzoni - sasa inajua uwezo uliopo na inaweza kuamua ni zipi za kutumia.*

**Mekanizim ya Usafirishaji**

MCP inaunga mkono meganizimu tofauti za usafirishaji. Moduli hii inaonyesha usafirishaji wa Stdio kwa michakato ya ndani:

<img src="../../../translated_images/sw/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*Meganizimu ya usafirishaji ya MCP: HTTP kwa seva za mbali, Stdio kwa michakato ya ndani*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Kwa michakato ya ndani. Programu yako huanzisha seva kama mchakato mtoto na kuwasiliana kupitia ingizo/mazao ya kawaida. Inafaa kwa ufikaji wa mfumo wa faili au zana za mstari wa amri.

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

<img src="../../../translated_images/sw/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Usafirishaji wa Stdio ukiwa kazini — programu yako huanzisha seva ya MCP kama mchakato wa mtoto na kuwasiliana kupitia mabomba ya stdin/stdout.*

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) na uliza:
> - "Usafirishaji wa Stdio hufanya kazi vipi na ni lini ni sahihi kuitumia badala ya HTTP?"
> - "LangChain4j huendeshaje mzunguko wa maisha ya michakato ya seva ya MCP iliyozinduliwa?"
> - "Ni athari gani za usalama za kumpa AI upatikanaji wa mfumo wa faili?"

## Moduli ya Wakala

Wakati MCP hutoa zana za kiwango thabiti, moduli ya **wakala** ya LangChain4j hutoa njia ya kielezi ya kujenga mawakala wanaoratibu zana hizo. Onyesho la `@Agent` na `AgenticServices` hukuruhusu kufafanua tabia ya wakala kupitia kiolesura badala ya msimbo wa maagizo.

Katika moduli hii, utachunguza mfano wa **Wakala Msimamizi** — njia ya hali ya juu ya AI ya wakala ambapo wakala "msimamizi" huamua kwa nguvu sub-wakala gani waite kulingana na maombi ya mtumiaji. Tutachanganya dhana zote mbili kwa kumpa sub-wakala mmoja uwezo wa kupata faili powered na MCP.

Ili kutumia moduli ya wakala, ongeza utegemezi huu wa Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **⚠️ Jaribio:** Moduli ya `langchain4j-agentic` ni **jaribio** na inaweza kubadilika. Njia thabiti ya kujenga wasaidizi wa AI bado ni `langchain4j-core` na zana za kawaida (Moduli 04).

## Kukimbia Mifano

### Mahitaji ya Awali

- Java 21+, Maven 3.9+
- Node.js 16+ na npm (kwa seva za MCP)
- Mazingira ya kubainisha yaliyopangwa katika faili `.env` (kutoka saraka ya mzizi):
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (kama Moduli 01-04)

> **Kumbuka:** Ikiwa bado hujaunda mazingira yako, angalia [Moduli 00 - Anza Haraka](../00-quick-start/README.md) kwa maelekezo, au nakili `.env.example` kwenda `.env` katika saraka ya mzizi na uingize thamani zako.

## Anza Haraka

**Kutumia VS Code:** Bonyeza kulia faili lolote la demo kwenye Mtafutaji na chagua **"Run Java"**, au tumia mipangilio ya kuanzisha kutoka kwenye paneli ya Run and Debug (hakikisha umeongeza token yako kwa faili `.env` kwanza).

**Kutumia Maven:** Vinginevyo, unaweza kuendesha kupitia terminal kwa mifano ifuatayo.

### Uendeshaji wa Faili (Stdio)

Hii inaonyesha zana za ndani zilizo msingi wa michakato mdogo.

**✅ Hakuna mahitaji ya awali** - seva ya MCP huanzishwa moja kwa moja.

**Kutumia Skripti za Kuanzisha (Inapendekezwa):**

Skripti za kuanzisha hujaribu moja kwa moja kubeba mabadiliko ya mazingira kutoka faili `.env` ya mzizi:

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

**Kutumia VS Code:** Bonyeza kulia `StdioTransportDemo.java` na chagua **"Run Java"** (hakikisha faili yako `.env` imesanidiwa).

Programu huanzisha seva ya MCP ya mfumo wa faili moja kwa moja na husoma faili la ndani. Angalia jinsi usimamizi wa michakato mdogo unavyofanyika kwa ajili yako.

**Matokeo yanayotarajiwa:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Wakala Msimamizi

Mfano wa **Wakala Msimamizi** ni aina **nyepesi** ya AI ya wakala. Msimamizi hutumia LLM kuamua bila kuingiliwa ni mawakala gani waite kulingana na ombi la mtumiaji. Katika mfano ujao, tunachanganya ufikaji wa faili powered na MCP na wakala wa LLM kuunda mtiririko wa kusoma faili → ripoti iliyoongozwa.

Katika demo, `FileAgent` husoma faili kwa kutumia zana za mfumo wa faili za MCP, na `ReportAgent` hutengeneza ripoti yenye muhtasari wa kiutendaji (sentensi 1), hoja kuu 3, na mapendekezo. Msimamizi huandaa mtiririko huu moja kwa moja:

<img src="../../../translated_images/sw/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Msimamizi hutumia LLM yake kuamua mawakala gani waite na kwa mpangilio gani — hakuna mahitaji ya njia kali ya kuendesha.*

Huu ndio mtiririko halisi wa kazi kwa bomba letu la faili-ku-ripoti:

<img src="../../../translated_images/sw/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent husoma faili kupitia zana za MCP, kisha ReportAgent hubadilisha maudhui ghafi kuwa ripoti iliyopangwa.*

Kila wakala huhifadhi matokeo yake katika **Eneo la Wakala** (kumbukumbu ya pamoja), ikiruhusu mawakala wa baadaye kufikia matokeo ya awali. Hii inaonyesha jinsi zana za MCP zinavyounganishwa kwa urahisi kwenye mitiririko ya kazi ya wakala — Msimamizi hahitaji kujua *jinsi* faili husomwa, bali kwamba `FileAgent` anaweza kufanya hivyo.

#### Kukimbia Demo

Skripti za kuanzisha hujaribu moja kwa moja kubeba mabadiliko ya mazingira kutoka faili `.env` ya mzizi:

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

**Kutumia VS Code:** Bonyeza kulia `SupervisorAgentDemo.java` na chagua **"Run Java"** (hakikisha faili yako `.env` imesanidiwa).

#### Jinsi Msimamizi Anavyofanya Kazi

```java
// Hatua ya 1: FileAgent husoma faili kwa kutumia zana za MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Ina zana za MCP kwa ajili ya shughuli za faili
        .build();

// Hatua ya 2: ReportAgent huzalisha ripoti zilizo na muundo
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Msimamizi huoratibu mchakato wa faili → ripoti
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Rudisha ripoti ya mwisho
        .build();

// Msimamizi huamua maajenti gani wawaite kulingana na ombi
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Mikakati ya Majibu

Unaposanidi `SupervisorAgent`, unabainisha jinsi inavyopaswa kutengeneza jibu lake la mwisho kwa mtumiaji baada ya mawakala wadogo kukamilisha majukumu yao.

<img src="../../../translated_images/sw/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Mikakati mitatu ya jinsi Msimamizi anavyotengeneza jibu la mwisho — chagua kulingana na kama unataka matokeo ya wakala wa mwisho, muhtasari uliounganishwa, au chaguo lenye alama bora.*

Mikakati inayopatikana ni:

| Mkakati | Maelezo |
|----------|-------------|
| **LAST** | Msimamizi hurudisha matokeo ya wakala wa mwisho au zana iliyoitwa. Hii ni muhimu wakati wakala wa mwisho katika mtiririko wa kazi ameundwa mahsusi kutoa jibu kamili la mwisho (mfano, "Wakala Muhtasari" katika bomba la utafiti). |
| **SUMMARY** | Msimamizi hutumia Lugha Modeli (LLM) ndani yake kuunda muhtasari wa mwingiliano wote na matokeo ya wakala wadogo, kisha hurudisha muhtasari huo kama jibu la mwisho. Hii hutoa jibu safi, lililojumuishwa kwa mtumiaji. |
| **SCORED** | Mfumo hutumia LLM ndani kutathmini majibu ya LAST na muhtasari wa mwingiliano dhidi ya ombi halisi la mtumiaji, na kurudisha matokeo walio na alama bora zaidi. |

Tazama [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) kwa utekelezaji kamili.

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) na uliza:
> - "Msimamizi huamua vipi ni mawakala gani waite?"
> - "Tofauti gani kati ya mifano ya kazi ya Msimamizi na Mtiririko wa Mfululizo?"
> - "Ninawezaje kubinafsisha tabia ya mipango ya Msimamizi?"

#### Kuelewa Matokeo

Unapokimbia demo, utaona maelezo yaliyopangwa ya jinsi Msimamizi anavyoratibu mawakala wengi. Hivi ndivyo kila sehemu inavyomaanisha:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Kichwa** kinaanzisha dhana ya mtiririko wa kazi: bomba lililolengwa kutoka kusoma faili hadi kutengeneza ripoti.

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

**Ombi la Mtumiaji** linaonyesha kazi. Msimamizi ultazama hili na kuamua kuwaita FileAgent → ReportAgent.

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

**Uratibu wa Msimamizi** unaonyesha mtiririko wa hatua 2 ukiendeshwa:
1. **FileAgent** husoma faili kupitia MCP na kuhifadhi maudhui
2. **ReportAgent** hupokea maudhui na kutengeneza ripoti iliyopangwa

Msimamizi alifanya maamuzi haya **kwa uhuru** kulingana na ombi la mtumiaji.

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

#### Maelezo ya Sifa za Moduli ya Wakala

Mfano unaonyesha sifa kadhaa za hali ya juu za moduli ya wakala. Tuchukue mtazamo wa karibu juu ya Eneo la Wakala na Wasikilizaji wa Wakala.

**Eneo la Wakala** linaonyesha kumbukumbu ya pamoja ambapo mawakala walihifadhi matokeo yao kwa kutumia `@Agent(outputKey="...")`. Hii huruhusu:
- Wakala wa baadaye kufikia matokeo ya mawakala wa awali
- Msimamizi kuunganisha jibu la mwisho
- Wewe kuchunguza kile kila wakala alichotengeneza

<img src="../../../translated_images/sw/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Eneo la Wakala hufanya kazi kama kumbukumbu ya pamoja — FileAgent anaandika `fileContent`, ReportAgent husoma na kuandika `report`, na msimbo wako husoma matokeo ya mwisho.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Takwimu za faili ghafi kutoka kwa FileAgent
String report = scope.readState("report");            // Ripoti iliyoandaliwa kutoka kwa ReportAgent
```

**Wasikilizaji wa Wakala** huruhusu ufuatiliaji na utatuzi wa makosa wa utekelezaji wa wakala. Matokeo ya hatua kwa hatua unayoona kwenye demo yanatokana na AgentListener anayeshikilia kila mwito wa wakala:
- **beforeAgentInvocation** - Huitwa wakati Mkuu anapochagua wakala, kukuruhusu kuona wakala aliyechaguliwa na kwa nini
- **afterAgentInvocation** - Huitwa wakati wakala anapokamilisha, kuonyesha matokeo yake
- **inheritedBySubagents** - Wakati ni kweli, mmisikilizaji husimamia wakala wote katika mfuatano

<img src="../../../translated_images/sw/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Wasikilizi wa Wakala huungana katika mzunguko wa utekelezaji — husimamia wakati wakala wanaanza, wanakamilisha, au kukutana na makosa.*

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
        return true; // Sambaza kwa mawakala wote wadogo
    }
};
```

Zaidi ya mfano wa Mkuu, moduli ya `langchain4j-agentic` hutoa mifumo na vipengele vingi vya mtiririko wa kazi:

<img src="../../../translated_images/sw/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Mifumo mitano ya mtiririko wa kazi kwa kuongoza wakala — kutoka njia rahisi za mfululizo hadi mitiririko ya idhini yenye binadamu katikati.*

| Mfano | Maelezo | Matumizi |
|---------|-------------|----------|
| **Mfululizo** | Tengeneza wakala kwa mpangilio, matokeo yapita kwa wakala mwengine | Mipangilio: utafiti → uchambuzi → ripoti |
| **Msimulizi wa Wakati Mmoja** | Endesha wakala kwa wakati mmoja | Kazi huru: hali ya hewa + habari + hisa |
| **Mizunguko** | Rudia hadi hali itimizwe | Kupima ubora: boresha hadi alama ≥ 0.8 |
| **Sharti** | Elekeza kulingana na masharti | Kainisha → elekeza kwa mtaalamu |
| **Binadamu katikati** | Ongeza hatua za binadamu | Mitiririko ya idhini, ukaguzi wa maudhui |

## Dhana Muhimu

Sasa baada ya kuchunguza MCP na moduli ya agentic ikifanya kazi, hebu tufupishe lini kutumia kila mbinu.

<img src="../../../translated_images/sw/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP huunda mfumo wa itifaki ya ulimwengu — seva yoyote inayosaidia MCP inafanya kazi na mteja yeyote anayezingatia MCP, ikiruhusu kushirikiana kwa zana katika programu mbalimbali.*

**MCP** ni bora wakati unataka kutumia mifumo ya zana zilizopo, kujenga zana ambazo programu nyingi zinaweza kushauriana, kuunganisha huduma za watu wengine kwa itifaki za kawaida, au kubadilisha utekelezaji wa zana bila kubadilisha msimbo.

**Moduli ya Agentic** inafanya kazi vizuri unapotaka maelezo ya wakala kwa njia ya matamko na alama za `@Agent`, unahitaji usanifu wa mtiririko wa kazi (mfululizo, mzunguko, msimulizi wa wakati mmoja), unatilia mkazo muundo wa wakala kwa kutegemea interface badala ya msimbo wa amri, au unachanganya wakala wengi wanaoshirikiana matokeo kupitia `outputKey`.

**Mfano wa Mkuu wa Wakala** huangaza unapokuwa na mtiririko usiotabirika kabla na unataka LLM ichague, unapokuwa na wakala maalum wengi wanaohitaji usanifu wa mabadiliko, unapojenga mifumo ya mazungumzo inayomwelekeza mtu kwa uwezo tofauti, au unapotaka tabia rahisi na inayobadilika ya wakala.

<img src="../../../translated_images/sw/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Lini kutumia mbinu za @Tool za kawaida dhidi ya zana za MCP — zana za kawaida kwa mantiki maalum ya programu na usalama kamili wa aina, zana za MCP kwa muunganisho uliopangwa unaofanya kazi katika programu mbalimbali.*

## Hongera!

<img src="../../../translated_images/sw/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Safari yako ya kujifunza kupitia moduli zote tano — kutoka mazungumzo ya msingi hadi mifumo ya agentic inayotumia MCP.*

Umekamilisha kozi ya LangChain4j kwa Waanzilishi. Umejifunza:

- Jinsi ya kujenga AI ya mazungumzo yenye kumbukumbu (Moduli 01)
- Mifano ya uhandisi wa maelekezo kwa kazi tofauti (Moduli 02)
- Kuimarisha majibu katika nyaraka zako kwa RAG (Moduli 03)
- Kuunda mawakala wa AI wa msingi (wasaidizi) kwa zana maalum (Moduli 04)
- Kuunganisha zana zilizopangwa na moduli za LangChain4j MCP na Agentic (Moduli 05)

### Nini Ifuatayo?

Baada ya kumaliza moduli, chunguza [Mwongozo wa Upimaji](../docs/TESTING.md) kuona dhana za upimaji za LangChain4j zikifanya kazi.

**Rasilimali Rasmi:**
- [Nyaraka za LangChain4j](https://docs.langchain4j.dev/) - Miongozo kamili na kumbukumbu ya API
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Chanzo cha msimbo na mifano
- [Mafunzo ya LangChain4j](https://docs.langchain4j.dev/tutorials/) - Mafunzo kwa hatua kwa hatua kwa matumizi mbalimbali

Asante kwa kumaliza kozi hii!

---

**Uendeshaji:** [← Iliyotangulia: Moduli 04 - Zana](../04-tools/README.md) | [Rudi Kwenye Msingi](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Kutokujali**:
Hati hii imetafsiriwa kwa kutumia huduma ya utafsiri wa AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kuhakikisha usahihi, tafadhali fahamu kuwa tafsiri za kiotomatiki zinaweza kuwa na makosa au upungufu wa usahihi. Hati asili katika lugha yake ya asili inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya kitaalamu inayofanywa na binadamu inapendekezwa. Hatuhamishwi uwajibikaji wowote kwa kutoelewana au tafsiri zisizo sahihi zinazotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->