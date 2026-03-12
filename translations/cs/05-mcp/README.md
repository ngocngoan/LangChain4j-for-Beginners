# Modul 05: Protokol modelového kontextu (MCP)

## Obsah

- [Video průvodce](../../../05-mcp)
- [Co se naučíte](../../../05-mcp)
- [Co je MCP?](../../../05-mcp)
- [Jak MCP funguje](../../../05-mcp)
- [Agentický modul](../../../05-mcp)
- [Spuštění příkladů](../../../05-mcp)
  - [Požadavky](../../../05-mcp)
- [Rychlý start](../../../05-mcp)
  - [Operace se soubory (Stdio)](../../../05-mcp)
  - [Supervisor Agent](../../../05-mcp)
    - [Spuštění ukázky](../../../05-mcp)
    - [Jak Supervisor funguje](../../../05-mcp)
    - [Jak FileAgent objevuje MCP nástroje během běhu](../../../05-mcp)
    - [Strategie reakce](../../../05-mcp)
    - [Porozumění výstupu](../../../05-mcp)
    - [Vysvětlení funkcí agentického modulu](../../../05-mcp)
- [Klíčové koncepty](../../../05-mcp)
- [Gratulujeme!](../../../05-mcp)
  - [Co dál?](../../../05-mcp)

## Video průvodce

Sledujte tuto živou relaci, která vysvětluje, jak začít s tímto modulem:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="AI Agents with Tools and MCP - Live Session" width="800"/></a>

## Co se naučíte

Vybudovali jste konverzační AI, zvládli promptování, zakládali odpovědi na dokumentech a vytvořili agenty s nástroji. Ale všechny tyto nástroje byly vytvořené na míru pro vaši konkrétní aplikaci. Co kdybychom vašemu AI umožnili přístup do standardizovaného ekosystému nástrojů, které může kdokoliv vytvořit a sdílet? V tomto modulu se naučíte, jak toho dosáhnout pomocí Protokolu modelového kontextu (MCP) a agentického modulu LangChain4j. Nejprve představíme jednoduchý MCP čtečku souborů a pak ukážeme, jak ji snadno integrovat do pokročilých agentických workflow pomocí vzoru Supervisor Agenta.

## Co je MCP?

Protokol modelového kontextu (MCP) poskytuje právě to - standardní způsob, jak AI aplikace mohou objevovat a používat externí nástroje. Místo psaní vlastních integrací pro každý zdroj dat nebo službu se připojujete k MCP serverům, které své schopnosti vystavují v konzistentním formátu. Váš AI agent pak tyto nástroje může automaticky objevovat a používat.

Následující diagram ukazuje rozdíl — bez MCP vyžaduje každá integrace vlastní bodové propojení; s MCP jeden protokol propojí vaši aplikaci s jakýmkoliv nástrojem:

<img src="../../../translated_images/cs/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Před MCP: složité bodové integrace. Po MCP: jeden protokol, nekonečné možnosti.*

MCP řeší základní problém ve vývoji AI: každá integrace je na míru. Chcete přistupovat k GitHubu? Vlastní kód. Chcete číst soubory? Vlastní kód. Chcete dotazovat databázi? Vlastní kód. A žádná z těchto integrací nefunguje s jinými AI aplikacemi.

MCP to standardizuje. MCP server zpřístupňuje nástroje s jasnými popisy a schématy parametrů. Jakýkoliv MCP klient se může připojit, objevit dostupné nástroje a použít je. Napište jednou, použijte všude.

Následující diagram ilustruje tuto architekturu — jeden MCP klient (vaše AI aplikace) se připojuje k několika MCP serverům, z nichž každý vystavuje svůj vlastní soubor nástrojů prostřednictvím standardního protokolu:

<img src="../../../translated_images/cs/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Architektura Protokolu modelového kontextu - standardizované objevování a spuštění nástrojů*

## Jak MCP funguje

Pod kapotou MCP používá vrstvenou architekturu. Vaše Java aplikace (MCP klient) objevuje dostupné nástroje, posílá JSON-RPC požadavky přes transportní vrstvu (Stdio nebo HTTP) a MCP server operace provádí a vrací výsledky. Následující diagram rozkládá každou vrstvu tohoto protokolu:

<img src="../../../translated_images/cs/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Jak MCP funguje pod kapotou — klienti objevují nástroje, vyměňují JSON-RPC zprávy a vykonávají operace přes transportní vrstvu.*

**Architektura server-klient**

MCP používá model server-klient. Servery poskytují nástroje - čtení souborů, dotazování databází, volání API. Klienti (vaše AI aplikace) se připojují k serverům a používají jejich nástroje.

Pro použití MCP s LangChain4j přidejte tuto Maven závislost:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Objevování nástrojů**

Když se váš klient připojí k MCP serveru, ptá se: "Jaké nástroje máš?" Server odpoví seznamem dostupných nástrojů s popisy a schématy parametrů. Váš AI agent pak rozhoduje, které nástroje použít na základě uživatelských požadavků. Následující diagram ukazuje tento handshake — klient posílá požadavek `tools/list` a server vrací své dostupné nástroje s popisy a schématy parametrů:

<img src="../../../translated_images/cs/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI objevuje dostupné nástroje při startu — nyní ví, jaké schopnosti jsou k dispozici, a může rozhodnout, které použít.*

**Transportní mechanismy**

MCP podporuje různé transportní mechanismy. Dvě možnosti jsou Stdio (pro lokální komunikaci s podprocesy) a Streamable HTTP (pro vzdálené servery). Tento modul demonstruje transport Stdio:

<img src="../../../translated_images/cs/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*Transportní mechanismy MCP: HTTP pro vzdálené servery, Stdio pro lokální procesy*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Pro lokální procesy. Vaše aplikace spustí server jako podproces a komunikuje přes standardní vstup/výstup. Uživatelské pro přístup k souborovému systému nebo příkazovým nástrojům.

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

Server `@modelcontextprotocol/server-filesystem` zpřístupňuje tyto nástroje, všechny sandboxované do adresářů, které určíte:

| Nástroj | Popis |
|------|-------------|
| `read_file` | Čtení obsahu jednoho souboru |
| `read_multiple_files` | Čtení více souborů jedním voláním |
| `write_file` | Vytvoření nebo přepsání souboru |
| `edit_file` | Cílená úprava find-and-replace |
| `list_directory` | Výpis souborů a adresářů na cestě |
| `search_files` | Rekurzivní hledání souborů podle vzoru |
| `get_file_info` | Získání metadata souboru (velikost, časové značky, oprávnění) |
| `create_directory` | Vytvoření adresáře (včetně rodičovských adresářů) |
| `move_file` | Přesun nebo přejmenování souboru či adresáře |

Následující diagram ukazuje, jak transport Stdio funguje za běhu — vaše Java aplikace spustí MCP server jako podproces a komunikují přes stdin/stdout potrubí, bez sítě nebo HTTP:

<img src="../../../translated_images/cs/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Transport Stdio v akci — aplikace spustí MCP server jako podproces a komunikuje přes stdin/stdout.*

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) a zeptejte se:
> - "Jak funguje transport Stdio a kdy ho používat místo HTTP?"
> - "Jak LangChain4j spravuje životní cyklus spuštěných MCP serverových procesů?"
> - "Jaké jsou bezpečnostní důsledky, když AI dostane přístup k souborovému systému?"

## Agentický modul

Zatímco MCP poskytuje standardizované nástroje, agentický modul LangChain4j nabízí deklarativní způsob, jak stavět agenty, kteří tyto nástroje orchestrují. Anotace `@Agent` a `AgenticServices` vám umožní definovat chování agenta přes rozhraní místo imperativního kódu.

V tomto modulu prozkoumáte vzor **Supervisor Agenta** — pokročilý agentický AI přístup, kde „supervizor“ agent dynamicky rozhoduje, které pod-agenty vyvolat na základě uživatelských požadavků. Obě koncepty spojíme tím, že jednomu z pod-agentů poskytneme přístup k souborům poháněný MCP.

Pro použití agentického modulu přidejte tuto Maven závislost:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Poznámka:** Modul `langchain4j-agentic` používá samostatnou verzi (`langchain4j.mcp.version`), protože vychází v jiném časovém rámci než jádro LangChain4j.

> **⚠️ Experimentální:** Modul `langchain4j-agentic` je **experimentální** a může se změnit. Stabilní cesta pro tvorbu AI asistentů zůstává `langchain4j-core` s vlastními nástroji (Modul 04).

## Spuštění příkladů

### Požadavky

- Dokončený [Modul 04 - Nástroje](../04-tools/README.md) (tento modul navazuje na koncepty vlastních nástrojů a porovnává je s MCP nástroji)
- Soubor `.env` v kořenovém adresáři s Azure přihlašovacími údaji (vytvořený `azd up` v Modulu 01)
- Java 21+, Maven 3.9+
- Node.js 16+ a npm (pro MCP servery)

> **Poznámka:** Pokud jste ještě nenastavili své proměnné prostředí, podívejte se do [Modul 01 - Úvod](../01-introduction/README.md) pro pokyny k nasazení (`azd up` vytváří `.env` automaticky), nebo zkopírujte `.env.example` do `.env` v kořenovém adresáři a vyplňte hodnoty.

## Rychlý start

**Použití VS Code:** Jednoduše klikněte pravým tlačítkem na jakýkoli demo soubor v Průzkumníku a vyberte **„Run Java“**, nebo použijte spouštěcí konfigurace z panelu Spustit a ladit (nejdříve zkontrolujte, že máte nastavený `.env` soubor s Azure údaji).

**Použití Maven:** Alternativně můžete spustit příklady z příkazové řádky následujícími příkazy.

### Operace se soubory (Stdio)

Ukazuje nástroje založené na lokálních podprocesech.

**✅ Bez požadavků** - MCP server je spouštěn automaticky.

**Použití startovacích skriptů (doporučeno):**

Startovací skripty automaticky načítají proměnné prostředí z kořenového `.env` souboru:

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

**Použití VS Code:** Klikněte pravým tlačítkem na `StdioTransportDemo.java` a vyberte **„Run Java“** (ujistěte se, že máte nakonfigurovaný `.env` soubor).

Aplikace automaticky spustí MCP server pro souborový systém a načte lokální soubor. Všimněte si, jak je řízení podprocesů zajištěno za vás.

**Očekávaný výstup:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor Agent

**Vzor Supervisor Agenta** je **flexibilní** forma agentického AI. Supervisor používá LLM a autonomně rozhoduje, které agenty vyvolat na základě požadavku uživatele. V dalším příkladu spojíme přístup k souborům poháněný MCP s LLM agentem pro vytvoření workflow čtení souboru → report.

V ukázce `FileAgent` čte soubor pomocí MCP nástrojů souborového systému a `ReportAgent` generuje strukturovanou zprávu s exekutivním shrnutím (1 věta), 3 klíčovými body a doporučeními. Supervisor orchestruje tento tok automaticky:

<img src="../../../translated_images/cs/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Supervisor používá svůj LLM, aby rozhodl, které agenty vyvolat a v jakém pořadí — není potřeba pevné směrování.*

Zde je konkrétní workflow našeho pipeline ze souboru do reportu:

<img src="../../../translated_images/cs/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent čte soubor přes MCP nástroje, poté ReportAgent transformuje surový obsah do strukturované zprávy.*

Následující sekvenční diagram sleduje kompletní koordinaci Supervisora — od spuštění MCP serveru, přes autonomní výběr agentů až po volání nástrojů přes stdio a konečný report:

<img src="../../../translated_images/cs/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*Supervisor autonomně vyvolá FileAgent (který volá MCP server přes stdio, aby soubor přečetl), poté vyvolá ReportAgent k vygenerování strukturované zprávy — každý agent ukládá svůj výstup do sdířeného Agentického Scopu.*

Každý agent ukládá svůj výstup do **Agentického Scopu** (sdílené paměti), což umožňuje následujícím agentům přístup k předchozím výsledkům. To demonstruje, jak MCP nástroje plynule zapadají do agentických workflow — Supervisor nemusí vědět *jak* se soubory čtou, jen že `FileAgent` to umí.

#### Spuštění ukázky

Startovací skripty automaticky načítají proměnné prostředí z kořenového `.env` souboru:

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

**Použití VS Code:** Klikněte pravým tlačítkem na `SupervisorAgentDemo.java` a vyberte **„Run Java“** (ujistěte se, že máte nakonfigurovaný `.env` soubor).

#### Jak Supervisor funguje

Před sestavením agentů musíte připojit MCP transport k klientu a zabalit ho jako `ToolProvider`. Tímto způsobem se nástroje MCP serveru zpřístupní vašim agentům:

```java
// Vytvořte klienta MCP z transportu
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Zabalte klienta jako ToolProvider — toto propojuje nástroje MCP do LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Nyní můžete `mcpToolProvider` injektovat do jakéhokoli agenta, který potřebuje MCP nástroje:

```java
// Krok 1: FileAgent čte soubory pomocí nástrojů MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Má nástroje MCP pro operace se soubory
        .build();

// Krok 2: ReportAgent generuje strukturované zprávy
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor orchestruje workflow soubor → zpráva
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Vrátit finální zprávu
        .build();

// Supervisor rozhoduje, které agenty vyvolat na základě požadavku
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Jak FileAgent objevuje MCP nástroje během běhu

Možná vás zajímá: **jak `FileAgent` ví, jak používat npm nástroje souborového systému?** Odpověď je, že to přímo neví — **LLM** to zjistí za běhu díky schématům nástrojů.
Rozhraní `FileAgent` je jen **definice promptu**. Neobsahuje pevně dané znalosti o `read_file`, `list_directory` nebo o jakémkoli jiném nástroji MCP. Zde je, co se děje od začátku do konce:

1. **Server se spustí:** `StdioMcpTransport` spouští npm balíček `@modelcontextprotocol/server-filesystem` jako podproces
2. **Objevování nástrojů:** `McpClient` odešle JSON-RPC požadavek `tools/list` na server, který reaguje jmény nástrojů, popisy a schématy parametrů (např. `read_file` — *"Načíst celý obsah souboru"* — `{ path: string }`)
3. **Vstřikování schémat:** `McpToolProvider` zabalí nalezená schémata a zpřístupní je LangChain4j
4. **Rozhodnutí LLM:** Když je voláno `FileAgent.readFile(path)`, LangChain4j pošle systémovou zprávu, uživatelskou zprávu **a seznam schémat nástrojů** do LLM. LLM si přečte popisy nástrojů a vygeneruje volání nástroje (např. `read_file(path="/some/file.txt")`)
5. **Provedení:** LangChain4j zachytí volání nástroje, přesměruje ho přes klienta MCP zpět do Node.js podprocesu, získá výsledek a předá ho zpět LLM

Tento mechanismus je stejný jako výše popsaný [Tool Discovery](../../../05-mcp), ale použitý konkrétně pro workflow agenta. Anotace `@SystemMessage` a `@UserMessage` usměrňují chování LLM, zatímco vstříknutý `ToolProvider` mu dává **schopnosti** — LLM spojuje obojí za běhu.

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) a zeptejte se:
> - "Jak tento agent ví, který MCP nástroj zavolat?"
> - "Co by se stalo, kdybych z builderu agenta odstranil ToolProvider?"
> - "Jak se schémata nástrojů předávají do LLM?"

#### Strategie odpovědí

Při konfiguraci `SupervisorAgent` určíte, jak by měl formulovat svou konečnou odpověď uživateli poté, co sub-agent dokončí své úkoly. Diagram níže ukazuje tři dostupné strategie — LAST vrací výsledek posledního agenta přímo, SUMMARY syntetizuje všechny výstupy přes LLM a SCORED vybírá ten výstup, který dosáhne lepšího skóre vůči původnímu požadavku:

<img src="../../../translated_images/cs/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Tři strategie, jak Supervisor formulovat finální odpověď — vyberte podle toho, zda chcete výstup posledního agenta, syntetizované shrnutí nebo nejlepší hodnocenou možnost.*

Dostupné strategie jsou:

| Strategie | Popis |
|----------|-------------|
| **LAST** | Supervisor vrátí výstup posledního sub-agenta nebo volaného nástroje. Hodí se, když konečný agent ve workflow je navržený tak, aby produkoval kompletní, finální odpověď (např. "Shrnující agent" v výzkumném procesu). |
| **SUMMARY** | Supervisor použije vlastní interní jazykový model (LLM) k syntéze shrnutí celé interakce a všech výstupů sub-agentů, pak toto shrnutí vrátí jako finální odpověď. Poskytuje tak čistou, agregovanou odpověď uživateli. |
| **SCORED** | Systém používá interní LLM k ohodnocení jak odpovědi LAST, tak shrnutí SUMMARY vůči původnímu uživatelskému požadavku a vrátí výstup s lepším hodnocením. |

Kompletní implementaci naleznete v souboru [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java).

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) a zeptejte se:
> - "Jak Supervisor rozhoduje, které agenty volat?"
> - "Jaký je rozdíl mezi vzory Supervisor a Sequential workflow?"
> - "Jak si můžu přizpůsobit plánovací chování Supervisor?"

#### Porozumění výstupu

Po spuštění demoverze uvidíte strukturovaný průchod tím, jak Supervisor orchestruje více agentů. Zde je, co jednotlivé části znamenají:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Hlavička** představuje koncept workflow: zaměřený pipeline od čtení souboru k tvorbě reportu.

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

**Diagram workflow** ukazuje tok dat mezi agenty. Každý agent má konkrétní roli:
- **FileAgent** čte soubory pomocí MCP nástrojů a ukládá surový obsah do `fileContent`
- **ReportAgent** využívá tento obsah a vytváří strukturovanou zprávu v `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Uživatelský požadavek** ukazuje úkol. Supervisor jej analyzuje a rozhodne se zavolat FileAgent → ReportAgent.

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

**Orchestrace Supervisora** ukazuje dvoustupňový průběh v akci:
1. **FileAgent** čte soubor přes MCP a ukládá obsah
2. **ReportAgent** obdrží obsah a vytvoří strukturovaný report

Supervisor tato rozhodnutí učinil **autonomně** na základě uživatelského požadavku.

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

#### Vysvětlení funkcí agentního modulu

Příklad demonstruje několik pokročilých funkcí agentního modulu. Podívejme se blíže na Agentic Scope a Agent Listeners.

**Agentic Scope** ukazuje sdílenou paměť, kde agenti uloží výsledky pomocí `@Agent(outputKey="...")`. To umožňuje:
- Pozdějším agentům přistupovat k výstupům dřívějších agentů
- Supervisorovi syntetizovat finální odpověď
- Vám prohlédnout, co každý agent vytvořil

Diagram níže ukazuje, jak Agentic Scope funguje jako sdílená paměť v workflow soubor → report — FileAgent zapisuje pod klíčem `fileContent`, ReportAgent jej čte a píše vlastní výsledek pod `report`:

<img src="../../../translated_images/cs/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope slouží jako sdílená paměť — FileAgent zapisuje `fileContent`, ReportAgent čte a zapisuje `report`, a vy čtete finální výsledek.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Surová data souboru z FileAgent
String report = scope.readState("report");            // Strukturovaná zpráva z ReportAgent
```

**Agent Listeners** umožňují sledování a ladění průběhu agentů. Krok za krokem výstup ve demu pochází z AgentListeneru, který se napojuje na každé volání agenta:
- **beforeAgentInvocation** - Volá se, když Supervisor vybere agenta, abyste viděli, který agent a proč byl zvolen
- **afterAgentInvocation** - Volá se po dokončení agenta, ukazuje jeho výsledek
- **inheritedBySubagents** - Pokud je true, posluchač monitoruje agenty ve hierarchii

Následující diagram ukazuje celý životní cyklus Agent Listeneru, včetně toho, jak `onError` řeší chyby během běhu agenta:

<img src="../../../translated_images/cs/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners se napojují na životní cyklus vykonávání — sledují, kdy agenti začnou, skončí nebo narazí na chyby.*

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
        return true; // Rozšiřte na všechny pod-agenty
    }
};
```

Kromě vzoru Supervisor modul `langchain4j-agentic` nabízí několik silných workflow vzorů. Diagram níže ukazuje všech pět — od jednoduchých sekvenčních pipeline až po schvalovací workflow s lidským zásahem:

<img src="../../../translated_images/cs/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Pět vzorů workflow k orchestraci agentů — od jednoduchých sekvenčních pipeline až po schvalovací workflow s člověkem v cyklu.*

| Vzor | Popis | Příklad použití |
|---------|-------------|----------|
| **Sequential** | Spouštění agentů postupně, výstup plyne do dalšího | Pipeline: výzkum → analýza → report |
| **Parallel** | Současný běh agentů | Nezávislé úkoly: počasí + zprávy + akcie |
| **Loop** | Iterace do splnění podmínky | Hodnocení kvality: vylepšovat dokud skóre ≥ 0.8 |
| **Conditional** | Směrování podle podmínek | Klasifikace → poslat specializovanému agentovi |
| **Human-in-the-Loop** | Přidání lidských kontrolních bodů | Schvalovací workflow, kontrola obsahu |

## Klíčové pojmy

Po prozkoumání MCP a modu agentic pojďme shrnout, kdy použít který přístup.

Jednou z největších výhod MCP je jeho rostoucí ekosystém. Diagram níže ukazuje, jak jeden univerzální protokol spojuje vaši AI aplikaci s mnoha MCP servery — od přístupu k souborovému systému a databázím až po GitHub, email, web scraping a další:

<img src="../../../translated_images/cs/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP vytváří univerzální protokolový ekosystém — jakýkoli MCP-kompatibilní server funguje s jakýmkoli MCP-kompatibilním klientem, což umožňuje sdílení nástrojů mezi aplikacemi.*

**MCP** je ideální, když chcete využít existující ekosystémy nástrojů, vytvářet nástroje, které mohou používat různé aplikace, integrovat služby třetích stran se standardními protokoly nebo měnit implementace nástrojů bez úprav kódu.

**Agentic modul** je nejlepší, když chcete deklarativní definice agentů pomocí anotací `@Agent`, potřebujete orchestraci workflow (sekvenční, smyčky, paralelní), preferujete návrh agentů založený na rozhraních před imperativním kódem, nebo kombinujete více agentů, kteří sdílejí výstupy přes `outputKey`.

**Vzor Supervisor Agenta** vyniká, když workflow není předem předvídatelný a chcete, aby rozhodoval LLM, když máte více specializovaných agentů, kteří potřebují dynamickou orchestraci, při vytváření konverzačních systémů s přesměrováním na různé schopnosti, nebo pokud chcete nejflexibilnější, adaptivní chování agenta.

Pro usnadnění rozhodování mezi vlastními metodami `@Tool` z Modulu 04 a MCP nástroji z tohoto modulu zde najdete srovnání hlavních kompromisů — vlastní nástroje vám poskytují těsné propojení a plnou typovou bezpečnost pro logiku aplikace, zatímco MCP nástroje nabízejí standardizované, znovupoužitelné integrace:

<img src="../../../translated_images/cs/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Kdy použít vlastní `@Tool` metody vs MCP nástroje — vlastní nástroje pro aplikační logiku s plnou typovou bezpečností, MCP nástroje pro standardizované integrace fungující mezi aplikacemi.*

## Gratulujeme!

Prošli jste všemi pěti moduly kurzu LangChain4j pro začátečníky! Zde je přehled celé vaší cesty učením — od základního chatu až po agentní systémy poháněné MCP:

<img src="../../../translated_images/cs/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Vaše cesta učením skrz všechny pět modulů — od základního chatu až po agentní systémy s MCP.*

Dokončili jste kurz LangChain4j pro začátečníky. Naučili jste se:

- Jak stavět konverzační AI s pamětí (Modul 01)
- Vzory prompt engineeringu pro různé úkoly (Modul 02)
- Zakotvování odpovědí ve vašich dokumentech s RAG (Modul 03)
- Tvorbu základních AI agentů (asistentů) s vlastními nástroji (Modul 04)
- Integraci standardizovaných nástrojů s LangChain4j MCP a moduly Agentic (Modul 05)

### Co dál?

Po dokončení modulů prozkoumejte [Testing Guide](../docs/TESTING.md), kde uvidíte koncepty testování LangChain4j v praxi.

**Oficiální zdroje:**
- [Dokumentace LangChain4j](https://docs.langchain4j.dev/) – Podrobné návody a reference API
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) – Zdrojové kódy a příklady
- [LangChain4j Tutoriály](https://docs.langchain4j.dev/tutorials/) – Krok za krokem návody pro různé případy použití

Děkujeme, že jste dokončili tento kurz!

---

**Navigace:** [← Předchozí: Modul 04 - Nástroje](../04-tools/README.md) | [Zpět na úvod](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o přesnost, mějte prosím na paměti, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Originální dokument v jeho mateřském jazyce by měl být považován za závazný zdroj. Pro důležité informace se doporučuje profesionální lidský překlad. Nejsme odpovědní za jakékoliv nedorozumění nebo mylné výklady vzniklé použitím tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->