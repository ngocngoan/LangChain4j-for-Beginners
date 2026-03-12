# Modul 05: Model Context Protocol (MCP)

## Innehållsförteckning

- [Videogenomgång](../../../05-mcp)
- [Vad du kommer att lära dig](../../../05-mcp)
- [Vad är MCP?](../../../05-mcp)
- [Hur MCP fungerar](../../../05-mcp)
- [Agentmodulen](../../../05-mcp)
- [Köra exemplen](../../../05-mcp)
  - [Förkunskaper](../../../05-mcp)
- [Snabbstart](../../../05-mcp)
  - [Filoperationer (Stdio)](../../../05-mcp)
  - [Supervisor-agent](../../../05-mcp)
    - [Köra demon](../../../05-mcp)
    - [Hur Supervisorn fungerar](../../../05-mcp)
    - [Hur FileAgent upptäcker MCP-verktyg vid körning](../../../05-mcp)
    - [Svarsstrategier](../../../05-mcp)
    - [Förstå utdata](../../../05-mcp)
    - [Förklaring av Agentmodulens funktioner](../../../05-mcp)
- [Nyckelbegrepp](../../../05-mcp)
- [Grattis!](../../../05-mcp)
  - [Vad händer härnäst?](../../../05-mcp)

## Videogenomgång

Se denna livesession som förklarar hur du kommer igång med denna modul:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="AI Agents with Tools and MCP - Live Session" width="800"/></a>

## Vad du kommer att lära dig

Du har byggt konversationell AI, bemästrat promptar, förankrat svar i dokument och skapat agenter med verktyg. Men alla dessa verktyg var specialbyggda för din specifika applikation. Tänk om du kunde ge din AI tillgång till ett standardiserat ekosystem av verktyg som vem som helst kan skapa och dela? I denna modul får du lära dig just det med Model Context Protocol (MCP) och LangChain4j:s agentmodul. Vi visar först en enkel MCP-filuppläsare och visar sedan hur den lätt integreras i avancerade agentarbetsflöden med Supervisor Agent-mönstret.

## Vad är MCP?

Model Context Protocol (MCP) erbjuder just det – ett standardiserat sätt för AI-applikationer att upptäcka och använda externa verktyg. Istället för att skriva specialanpassade integrationer för varje datakälla eller tjänst kopplar du till MCP-servrar som exponerar sina funktionaliteter i ett konsekvent format. Din AI-agent kan sedan automatiskt upptäcka och använda dessa verktyg.

Diagrammet nedan visar skillnaden — utan MCP kräver varje integration specialanpassad punkt-till-punkt-koppling; med MCP kopplar ett enda protokoll din app till vilket verktyg som helst:

<img src="../../../translated_images/sv/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Före MCP: Komplexa punkt-till-punkt-integrationer. Efter MCP: Ett protokoll, oändliga möjligheter.*

MCP löser ett grundläggande problem inom AI-utveckling: varje integration är anpassad. Vill du nå GitHub? Specialkod. Vill du läsa filer? Specialkod. Vill du göra databasfrågor? Specialkod. Och ingen av dessa integrationer fungerar med andra AI-applikationer.

MCP standardiserar detta. En MCP-server exponerar verktyg med tydliga beskrivningar och scheman. Varje MCP-klient kan koppla upp sig, upptäcka tillgängliga verktyg och använda dem. Bygg en gång, använd överallt.

Diagrammet nedan illustrerar denna arkitektur — en enskild MCP-klient (din AI-applikation) kopplar mot flera MCP-servrar, där varje server exponerar sina egna verktyg genom det standardiserade protokollet:

<img src="../../../translated_images/sv/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol-arkitektur – standardiserad upptäckt och användning av verktyg*

## Hur MCP fungerar

Under ytan använder MCP en lagerindelad arkitektur. Din Java-applikation (MCP-klienten) upptäcker tillgängliga verktyg, skickar JSON-RPC-förfrågningar genom ett transportlager (Stdio eller HTTP), och MCP-servern utför operationer och returnerar resultat. Följande diagram bryter ner varje lager i detta protokoll:

<img src="../../../translated_images/sv/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Hur MCP fungerar under ytan — klienter upptäcker verktyg, utbyter JSON-RPC-meddelanden och utför operationer via ett transportlager.*

**Server-klient-arkitektur**

MCP använder en klient-server-modell. Servrar tillhandahåller verktyg – läser filer, frågar databaser, anropar API:er. Klienter (din AI-applikation) kopplar till servrar och använder deras verktyg.

För att använda MCP med LangChain4j, lägg till detta Maven-beroende:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Verktygsupptäckt**

När din klient kopplar till en MCP-server frågar den "Vilka verktyg har du?" Servern svarar med en lista över tillgängliga verktyg, var och en med beskrivningar och parameterscheman. Din AI-agent kan sedan avgöra vilka verktyg som ska användas baserat på användarens förfrågningar. Diagrammet nedan visar denna handskakning — klienten skickar en `tools/list`-förfrågan och servern returnerar sina tillgängliga verktyg med beskrivningar och parameterscheman:

<img src="../../../translated_images/sv/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI:n upptäcker tillgängliga verktyg vid start — den vet nu vilka funktionaliteter som finns tillgängliga och kan avgöra vilka som ska användas.*

**Transportmekanismer**

MCP stödjer olika transportmekanismer. De två alternativen är Stdio (för lokal subprocess-kommunikation) och Streamable HTTP (för fjärrservrar). Denna modul demonstrerar Stdio-transporten:

<img src="../../../translated_images/sv/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP transportmekanismer: HTTP för fjärrservrar, Stdio för lokala processer*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

För lokala processer. Din applikation startar en server som subprocess och kommunicerar via standardin- och utström. Användbart för filsystemstillgång eller kommandoradsverktyg.

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

`@modelcontextprotocol/server-filesystem`-servern exponerar följande verktyg, alla isolerade till de kataloger du specificerar:

| Verktyg | Beskrivning |
|------|-------------|
| `read_file` | Läser innehållet i en enskild fil |
| `read_multiple_files` | Läser flera filer i ett anrop |
| `write_file` | Skapar eller skriver över en fil |
| `edit_file` | Gör riktade sök-och-ersätt-redigeringar |
| `list_directory` | Listar filer och kataloger på en sökväg |
| `search_files` | Söker rekursivt efter filer som matchar ett mönster |
| `get_file_info` | Hämtar filmetadata (storlek, tidsstämplar, behörigheter) |
| `create_directory` | Skapar en katalog (inklusive överordnade kataloger) |
| `move_file` | Flyttar eller byter namn på en fil eller katalog |

Följande diagram visar hur Stdio-transport fungerar under körning — din Java-applikation startar MCP-servern som en underordnad process och de kommunicerar genom stdin/stdout-rör, utan nätverk eller HTTP-inblandning:

<img src="../../../translated_images/sv/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio-transport i aktion — din applikation startar MCP-servern som en underordnad process och kommunicerar via stdin/stdout-rör.*

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) och fråga:
> - "Hur fungerar Stdio-transporten och när bör jag använda den istället för HTTP?"
> - "Hur hanterar LangChain4j livscykeln för uppstartade MCP-serverprocesser?"
> - "Vilka säkerhetsaspekter finns med att ge AI tillgång till filsystemet?"

## Agentmodulen

Medan MCP erbjuder standardiserade verktyg, ger LangChain4j:s **agentmodul** ett deklarativt sätt att bygga agenter som orkestrerar dessa verktyg. `@Agent`-annoteringen och `AgenticServices` låter dig definiera agentbeteenden via gränssnitt istället för imperativ kod.

I denna modul utforskar du **Supervisor Agent**-mönstret — en avancerad agentbaserad AI-metod där en "supervisor"-agent dynamiskt bestämmer vilka subagenter som ska anropas baserat på användarens förfrågningar. Vi kombinerar båda koncepten genom att ge en av våra subagenter MCP-drivna filåtkomstmöjligheter.

För att använda agentmodulen, lägg till detta Maven-beroende:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Notera:** `langchain4j-agentic`-modulen använder en separat versions-egenskap (`langchain4j.mcp.version`) eftersom den släpps enligt en annan tidsplan än de kärna LangChain4j-biblioteken.

> **⚠️ Experimentell:** `langchain4j-agentic`-modulen är **experimentell** och kan ändras. Det stabila sättet att bygga AI-assistenter är fortfarande `langchain4j-core` med egna verktyg (Modul 04).

## Köra exemplen

### Förkunskaper

- Avslutad [Modul 04 - Verktyg](../04-tools/README.md) (denna modul bygger på konceptet med egna verktyg och jämför med MCP-verktyg)
- `.env`-fil i rotkatalogen med Azure-uppgifter (skapad av `azd up` i Modul 01)
- Java 21+, Maven 3.9+
- Node.js 16+ och npm (för MCP-servrar)

> **Notera:** Om du inte har konfigurerat dina miljövariabler än, se [Modul 01 - Introduktion](../01-introduction/README.md) för distributionsinstruktioner (`azd up` skapar `.env`-filen automatiskt), eller kopiera `.env.example` till `.env` i rotkatalogen och fyll i dina värden.

## Snabbstart

**Använda VS Code:** Högerklicka på valfri demo-fil i Utforskaren och välj **"Run Java"**, eller använd körkonfigurationerna från panelen Kör och Felsök (se till att din `.env` är korrekt konfigurerad med Azure-uppgifter först).

**Använda Maven:** Alternativt kan du köra från kommandoraden med exemplen nedan.

### Filoperationer (Stdio)

Detta visar lokala subprocess-baserade verktyg.

**✅ Inga förkunskaper krävs** – MCP-servern startas automatiskt.

**Använda startskript (rekommenderas):**

Startskripten laddar automatiskt miljövariabler från rotens `.env`-fil:

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

**Använda VS Code:** Högerklicka på `StdioTransportDemo.java` och välj **"Run Java"** (se till att din `.env` är konfigurerad).

Applikationen startar automatiskt en MCP-server för filsystemet och läser en lokal fil. Lägg märke till hur subprocess-hanteringen sköts åt dig.

**Förväntat utdata:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor-agent

**Supervisor Agent-mönstret** är en **flexibel** form av agentbaserad AI. En Supervisor använder ett LLM för att autonomt avgöra vilka agenter som ska anropas baserat på användarens förfrågan. I nästa exempel kombinerar vi MCP-drivna filåtkomstverktyg med en LLM-agent för att skapa ett övervakat fil-läs → rapport-arbetsflöde.

I demon läser `FileAgent` en fil med MCP-filsystemverktyg, och `ReportAgent` genererar en strukturerad rapport med en sammanfattning (1 mening), 3 nyckelpunkter och rekommendationer. Supervisorn orkestrerar denna process automatiskt:

<img src="../../../translated_images/sv/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Supervisorn använder sitt LLM för att bestämma vilka agenter som ska anropas och i vilken ordning — ingen hårdkodad ruttning behövs.*

Så här ser det konkreta arbetsflödet ut för vår fil-till-rapport-pipeline:

<img src="../../../translated_images/sv/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent läser filen via MCP-verktyg, sedan omvandlar ReportAgent det råa innehållet till en strukturerad rapport.*

Följande sekvensdiagram visar hela Supervisor-orkestreringen — från att starta MCP-servern, genom Supervisorns autonoma agentval, till verktygsanrop över stdio och slutrapporten:

<img src="../../../translated_images/sv/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*Supervisorn anropar autonomt FileAgent (som anropar MCP-servern över stdio för att läsa filen), sedan anropar den ReportAgent för att generera en strukturerad rapport — varje agent lagrar sin utdata i det delade Agentic Scope.*

Varje agent lagrar sitt resultat i **Agentic Scope** (delat minne), vilket möjliggör för efterföljande agenter att läsa tidigare resultat. Detta demonstrerar hur MCP-verktyg smidigt integreras i agentarbetsflöden — Supervisorn behöver inte veta *hur* filer läses, bara att `FileAgent` kan göra det.

#### Köra demon

Startskripten laddar automatiskt miljövariabler från rotens `.env`-fil:

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

**Använda VS Code:** Högerklicka på `SupervisorAgentDemo.java` och välj **"Run Java"** (se till att din `.env` är konfigurerad).

#### Hur Supervisorn fungerar

Innan du bygger agenter behöver du koppla MCP-transporten till en klient och omsluta den som en `ToolProvider`. Så här blir MCP-serverns verktyg tillgängliga för dina agenter:

```java
// Skapa en MCP-klient från transporten
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Wrappa klienten som en ToolProvider — detta kopplar MCP-verktyg till LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Nu kan du injicera `mcpToolProvider` i vilken agent som helst som behöver MCP-verktyg:

```java
// Steg 1: FileAgent läser filer med hjälp av MCP-verktyg
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Har MCP-verktyg för filoperationer
        .build();

// Steg 2: ReportAgent genererar strukturerade rapporter
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor koordinerar arbetsflödet fil → rapport
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Returnera den slutgiltiga rapporten
        .build();

// Supervisorn avgör vilka agenter som ska anropas baserat på begäran
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Hur FileAgent upptäcker MCP-verktyg vid körning

Du kanske undrar: **hur vet `FileAgent` hur man använder npm-filsystemverktygen?** Svaret är att det gör den inte — **LLM:en** räknar ut det vid körning genom verktygsscheman.
`FileAgent`-gränssnittet är bara en **promptdefinition**. Det har ingen hårdkodad kunskap om `read_file`, `list_directory` eller något annat MCP-verktyg. Så här fungerar det från början till slut:

1. **Server startas:** `StdioMcpTransport` startar `@modelcontextprotocol/server-filesystem` npm-paketet som en underordnad process  
2. **Verktygsupptäckt:** `McpClient` skickar en `tools/list` JSON-RPC-begäran till servern, som svarar med verktygsnamn, beskrivningar och parameterscheman (t.ex. `read_file` — *"Läser hela innehållet i en fil"* — `{ path: string }`)  
3. **Schema-injektion:** `McpToolProvider` omsluter dessa upptäckta scheman och gör dem tillgängliga för LangChain4j  
4. **LLM bestämmer:** När `FileAgent.readFile(path)` anropas skickar LangChain4j systemmeddelandet, användarmeddelandet **och listan med verktygsscheman** till LLM. LLM läser verktygsbeskrivningarna och genererar ett verktygsanrop (t.ex. `read_file(path="/some/file.txt")`)  
5. **Exekvering:** LangChain4j fångar verktygsanropet, leder det genom MCP-klienten tillbaka till Node.js-underprocessen, får resultatet och matar tillbaka det till LLM

Det här är samma [Verktygsupptäcktsmekanism](../../../05-mcp) som beskrivits ovan, men tillämpad specifikt på agentflödet. `@SystemMessage` och `@UserMessage`-annoteringarna styr LLM:s beteende, medan den injicerade `ToolProvider` ger **möjligheterna** — LLM kopplar ihop de två vid körning.

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) och fråga:  
> - "Hur vet denna agent vilka MCP-verktyg som ska anropas?"  
> - "Vad händer om jag tar bort ToolProvider från agentbyggaren?"  
> - "Hur skickas verktygsscheman till LLM?"

#### Svarsstrategier

När du konfigurerar en `SupervisorAgent` specificerar du hur den ska formulera sitt slutgiltiga svar till användaren efter att underordnade agenter har slutfört sina uppgifter. Diagrammet nedan visar de tre tillgängliga strategierna — LAST returnerar den slutliga agentens utmatning direkt, SUMMARY syntetiserar alla utmatningar via en LLM och SCORED väljer det som får högst poäng mot den ursprungliga förfrågan:

<img src="../../../translated_images/sv/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Tre strategier för hur Supervisor formulerar sitt slutgiltiga svar — välj baserat på om du vill ha den sista agentens utdata, en syntetiserad sammanfattning eller det bästa poängsatta alternativet.*

De tillgängliga strategierna är:

| Strategi | Beskrivning |
|----------|-------------|
| **LAST** | Supervisorn returnerar utdata från den sista underagenten eller verktyget som anropades. Detta är användbart när den sista agenten i arbetsflödet är specifikt designad för att producera det kompletta, slutgiltiga svaret (t.ex. en "Sammanfattningsagent" i en forskningspipeline). |
| **SUMMARY** | Supervisorn använder sin egen interna språkmodell (LLM) för att syntetisera en sammanfattning av hela interaktionen och alla underagenters utdata, och returnerar sedan denna sammanfattning som slutgiltigt svar. Detta ger ett rent, sammanställt svar till användaren. |
| **SCORED** | Systemet använder en intern LLM för att poängsätta både svaret från LAST och sammanfattningen från SUMMARY mot den ursprungliga användarförfrågan och returnerar det svar som får högst poäng. |

Se [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) för komplett implementation.

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) och fråga:  
> - "Hur avgör Supervisor vilka agenter som ska anropas?"  
> - "Vad är skillnaden mellan Supervisor och Sequential arbetsflödesmönster?"  
> - "Hur kan jag anpassa Supervisorns planeringsbeteende?"

#### Förstå utdata

När du kör demon ser du en strukturerad genomgång av hur Supervisorn orkestrerar flera agenter. Här är vad varje sektion betyder:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Rubriken** introducerar arbetsflödeskonceptet: en fokuserad pipeline från filinläsning till rapportskapande.

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
  
**Arbetsflödesdiagrammet** visar dataflödet mellan agenter. Varje agent har en specifik roll:  
- **FileAgent** läser filer med MCP-verktyg och lagrar råinnehåll i `fileContent`  
- **ReportAgent** använder det innehållet och producerar en strukturerad rapport i `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Användarförfrågan** visar uppgiften. Supervisorn parser detta och bestämmer att anropa FileAgent → ReportAgent.

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
  
**Supervisor-orkestreringen** visar det tvåstegsflöde i aktion:  
1. **FileAgent** läser filen via MCP och lagrar innehållet  
2. **ReportAgent** tar emot innehållet och genererar en strukturerad rapport

Supervisorn fattade dessa beslut **självständigt** baserat på användarens förfrågan.

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
  
#### Förklaring av Agentic Modules funktioner

Exemplet visar flera avancerade funktioner i agentic-modulen. Låt oss titta närmare på Agentic Scope och Agent Listeners.

**Agentic Scope** visar det delade minnet där agenter lagrade sina resultat med `@Agent(outputKey="...")`. Detta möjliggör:  
- Att senare agenter kan nå tidigare agenters utdata  
- Supervisorn kan syntetisera ett slutgiltigt svar  
- Du kan inspektera vad varje agent producerade

Diagrammet nedan visar hur Agentic Scope fungerar som delat minne i arbetsflödet från fil till rapport — FileAgent skriver sin utdata under nyckeln `fileContent`, ReportAgent läser den och skriver sin egen utdata under `report`:

<img src="../../../translated_images/sv/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope fungerar som delat minne — FileAgent skriver `fileContent`, ReportAgent läser det och skriver `report`, och din kod läser slutresultatet.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Rå fil data från FileAgent
String report = scope.readState("report");            // Strukturerad rapport från ReportAgent
```
  
**Agent Listeners** möjliggör övervakning och felsökning av agenter vid exekvering. Steg-för-steg-utdata du ser i demon kommer från en AgentListener som kopplar in sig vid varje agentanrop:  
- **beforeAgentInvocation** - Kallas när Supervisorn väljer en agent, vilket låter dig se vilken agent som valdes och varför  
- **afterAgentInvocation** - Kallas när en agent avslutar och visar dess resultat  
- **inheritedBySubagents** - När sant övervakar lyssnaren alla agenter i hierarkin

Följande diagram visar den fullständiga livscykeln för Agent Listener, inklusive hur `onError` hanterar fel under agentkörning:

<img src="../../../translated_images/sv/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners kopplar in sig i exekveringslivscykeln — övervaka när agenter startar, slutförs eller stöter på fel.*

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
        return true; // Sprid till alla underagenter
    }
};
```
  
Utöver Supervisor-mönstret tillhandahåller `langchain4j-agentic` flera kraftfulla arbetsflödesmönster. Diagrammet nedan visar alla fem — från enkla sekventiella pipelines till godkännandeflöden med mänsklig inblandning:

<img src="../../../translated_images/sv/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Fem arbetsflödesmönster för att orkestrera agenter — från enkla sekventiella pipelines till godkännandeflöden med mänsklig inblandning.*

| Mönster | Beskrivning | Användningsfall |
|---------|-------------|-----------------|
| **Sequential** | Exekverar agenter i ordning, utdata flödar till nästa | Pipelines: forskning → analys → rapport |
| **Parallel** | Kör agenter samtidigt | Oberoende uppgifter: väder + nyheter + aktier |
| **Loop** | Itererar tills villkor uppfylls | Kvalitetsbedömning: förfina tills poäng ≥ 0,8 |
| **Conditional** | Rutar baserat på villkor | Klassificera → rutt till specialistagent |
| **Human-in-the-Loop** | Lägger till mänskliga kontrollpunkter | Godkännandeflöden, innehållsgranskning |

## Nyckelbegrepp

Nu när du utforskat MCP och agentic-modulen i praktiken, låt oss sammanfatta när varje metod bör användas.

En av MCP:s största fördelar är dess växande ekosystem. Diagrammet nedan visar hur ett universellt protokoll kopplar samman din AI-applikation med en mängd olika MCP-servrar — från filsystem och databasåtkomst till GitHub, e-post, webbsökning med mera:

<img src="../../../translated_images/sv/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP skapar ett universellt protokollekosystem — vilken MCP-kompatibel server som helst fungerar med vilken MCP-kompatibel klient som helst, vilket möjliggör verktygsdelning över applikationer.*

**MCP** är idealiskt när du vill utnyttja befintliga verktygsekosystem, bygga verktyg som flera applikationer kan dela, integrera tredjepartstjänster med standardprotokoll eller byta verktygsimplementationer utan att ändra koden.

**Agentic Module** fungerar bäst när du vill ha deklarativa agentdefinitioner med `@Agent`-annoteringar, behöver arbetsflödesorkestrering (sekventiell, loop, parallell), föredrar gränssnittsbaserad agentdesign framför imperativ kod, eller kombinerar flera agenter som delar utdata via `outputKey`.

**Supervisor Agent-mönstret** passar när arbetsflödet inte går att förutsäga i förväg och du vill att LLM ska bestämma, när du har flera specialiserade agenter som behöver dynamisk orkestrering, när du bygger konversationssystem som dirigerar till olika kapaciteter, eller när du vill ha det mest flexibla, anpassningsbara agentbeteendet.

För att hjälpa dig välja mellan anpassade `@Tool`-metoder från Modul 04 och MCP-verktyg från denna modul, lyfter följande jämförelse fram viktiga avvägningar — anpassade verktyg ger tät koppling och full typkontroll för app-specifik logik, medan MCP-verktyg erbjuder standardiserade, återanvändbara integrationer:

<img src="../../../translated_images/sv/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*När använda anpassade @Tool-metoder vs MCP-verktyg — anpassade verktyg för app-specifik logik med full typkontroll, MCP-verktyg för standardiserade integrationer som fungerar över applikationer.*

## Grattis!

Du har tagit dig igenom alla fem moduler i LangChain4j för nybörjarkursen! Här är en översikt över hela läranderesan du slutfört — från grundläggande chatt hela vägen till MCP-drivna agentbaserade system:

<img src="../../../translated_images/sv/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Din läranderesa genom alla fem moduler — från grundläggande chatt till MCP-drivna agentbaserade system.*

Du har slutfört LangChain4j för nybörjare-kursen. Du har lärt dig:

- Hur man bygger konversationsbaserad AI med minne (Modul 01)  
- Mönster för promptutformning för olika uppgifter (Modul 02)  
- Att förankra svar i dina dokument med RAG (Modul 03)  
- Skapa grundläggande AI-agenter (assistenter) med anpassade verktyg (Modul 04)  
- Integrera standardiserade verktyg med LangChain4j MCP och Agentic moduler (Modul 05)

### Vad händer härnäst?

Efter avslutade moduler, utforska [Testing Guide](../docs/TESTING.md) för att se LangChain4j testkoncept i praktiken.

**Officiella resurser:**  
- [LangChain4j Dokumentation](https://docs.langchain4j.dev/) - Omfattande guider och API-referens  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Källkod och exempel  
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - Steg-för-steg handledningar för olika användningsfall

Tack för att du genomförde denna kurs!

---

**Navigering:** [← Föregående: Modul 04 - Verktyg](../04-tools/README.md) | [Tillbaka till början](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, vänligen observera att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess modersmål bör betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för några missförstånd eller feltolkningar som uppstår vid användning av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->