# Modul 05: Protokol kontextu modelu (MCP)

## Obsah

- [Co se naučíte](../../../05-mcp)
- [Co je MCP?](../../../05-mcp)
- [Jak MCP funguje](../../../05-mcp)
- [Agentický modul](../../../05-mcp)
- [Spouštění příkladů](../../../05-mcp)
  - [Požadavky](../../../05-mcp)
- [Rychlý start](../../../05-mcp)
  - [Operace se soubory (Stdio)](../../../05-mcp)
  - [Supervisor agent](../../../05-mcp)
    - [Spouštění demo verze](../../../05-mcp)
    - [Jak Supervisor funguje](../../../05-mcp)
    - [Strategie odpovědí](../../../05-mcp)
    - [Porozumění výstupu](../../../05-mcp)
    - [Vysvětlení funkcí agentického modulu](../../../05-mcp)
- [Klíčové koncepty](../../../05-mcp)
- [Gratulujeme!](../../../05-mcp)
  - [Co dál?](../../../05-mcp)

## Co se naučíte

Vytvořili jste konverzační AI, zvládli jste prompty, uzemnili odpovědi do dokumentů a vytvořili agenty s nástroji. Ale všechny tyto nástroje byly šité na míru pro vaši konkrétní aplikaci. Co kdybyste vaší AI mohli zpřístupnit standardizovaný ekosystém nástrojů, které může kdokoliv vytvářet a sdílet? V tomto modulu se naučíte, jak to udělat pomocí Protokolu kontextu modelu (MCP) a agentického modulu LangChain4j. Nejprve ukážeme jednoduchý MCP čtečku souborů a poté, jak se snadno integruje do pokročilých agentických workflow pomocí vzoru Supervisor Agent.

## Co je MCP?

Protokol kontextu modelu (MCP) poskytuje právě to - standardní způsob, jak mohou AI aplikace objevovat a používat externí nástroje. Místo psaní vlastních integrací pro každý datový zdroj nebo službu se připojíte k MCP serverům, které zpřístupňují své funkce v jednotném formátu. Váš AI agent pak může tyto nástroje automaticky objevit a používat.

Následující diagram ukazuje rozdíl — bez MCP vyžaduje každá integrace vlastní bodové propojení; s MCP jediný protokol propojuje vaši aplikaci s jakýmkoli nástrojem:

<img src="../../../translated_images/cs/mcp-comparison.9129a881ecf10ff5.webp" alt="Porovnání MCP" width="800"/>

*Před MCP: složité bodové integrace. Po MCP: jeden protokol, nekonečné možnosti.*

MCP řeší základní problém vývoje AI: každá integrace je vlastní. Chcete přístup k GitHubu? Vlastní kód. Chcete číst soubory? Vlastní kód. Chcete dotazovat databázi? Vlastní kód. A žádné z těchto integrací nefunguje s jinými AI aplikacemi.

MCP to standardizuje. MCP server vystavuje nástroje s jasnými popisy a schématy parametrů. Jakýkoli MCP klient se může připojit, objevit dostupné nástroje a použít je. Napište jednou, použijte všude.

Následující diagram znázorňuje tuto architekturu — jediný MCP klient (vaše AI aplikace) se připojuje k několika MCP serverům, z nichž každý zpřístupňuje vlastní sadu nástrojů prostřednictvím standardního protokolu:

<img src="../../../translated_images/cs/mcp-architecture.b3156d787a4ceac9.webp" alt="Architektura MCP" width="800"/>

*Architektura Protokolu kontextu modelu - standardizované objevování a provádění nástrojů*

## Jak MCP funguje

V jádru využívá MCP vrstvenou architekturu. Vaše Java aplikace (MCP klient) objevuje dostupné nástroje, posílá JSON-RPC požadavky přes transportní vrstvu (Stdio nebo HTTP), a MCP server vykonává operace a vrací výsledky. Následující diagram rozebírá každou vrstvu tohoto protokolu:

<img src="../../../translated_images/cs/mcp-protocol-detail.01204e056f45308b.webp" alt="Detail protokolu MCP" width="800"/>

*Jak MCP funguje v pozadí — klienti objevují nástroje, vyměňují JSON-RPC zprávy a vykonávají operace přes transportní vrstvu.*

**Server-klient architektura**

MCP používá model klient-server. Servery poskytují nástroje - čtení souborů, dotazování databází, volání API. Klienti (vaše AI aplikace) se připojují k serverům a používají jejich nástroje.

Pro použití MCP s LangChain4j přidejte tuto Maven závislost:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Objevení nástrojů**

Když se váš klient připojí k MCP serveru, ptá se: „Jaké nástroje máte?“ Server odpoví seznamem dostupných nástrojů, každý s popisy a schématy parametrů. Váš AI agent pak může rozhodnout, které nástroje použije na základě uživatelských požadavků. Následující diagram ukazuje tento handshake — klient posílá požadavek `tools/list` a server vrací své dostupné nástroje s popisy a schématy parametrů:

<img src="../../../translated_images/cs/tool-discovery.07760a8a301a7832.webp" alt="Objevení nástrojů MCP" width="800"/>

*AI objeví dostupné nástroje při spuštění - nyní ví, jaké schopnosti jsou k dispozici a může rozhodnout, které použije.*

**Transportní mechanismy**

MCP podporuje různé transportní mechanismy. Dvě možnosti jsou Stdio (pro lokální komunikaci podprocesů) a Streamable HTTP (pro vzdálené servery). Tento modul demonstruje Stdio transport:

<img src="../../../translated_images/cs/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transportní mechanismy" width="800"/>

*Transportní mechanismy MCP: HTTP pro vzdálené servery, Stdio pro lokální procesy*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Pro lokální procesy. Vaše aplikace spustí server jako podproces a komunikuje přes standardní vstup/výstup. Užívá se pro přístup k souborovým systémům nebo příkazovým nástrojům.

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

Server `@modelcontextprotocol/server-filesystem` vystavuje následující nástroje, všechny sandboxované do adresářů, které určíte:

| Nástroj | Popis |
|---------|--------|
| `read_file` | Přečte obsah jednoho souboru |
| `read_multiple_files` | Přečte více souborů v jednom volání |
| `write_file` | Vytvoří nebo přepíše soubor |
| `edit_file` | Provede cílené úpravy typu najít-a-nahradit |
| `list_directory` | Vypíše soubory a adresáře na cestě |
| `search_files` | Rekurzivně hledá soubory odpovídající vzoru |
| `get_file_info` | Získá metadata souboru (velikost, časové značky, oprávnění) |
| `create_directory` | Vytvoří adresář (včetně nadřazených adresářů) |
| `move_file` | Přesune nebo přejmenuje soubor či adresář |

Následující diagram ukazuje, jak Stdio transport funguje během běhu — vaše Java aplikace spustí MCP server jako podproces a komunikují přes stdin/stdout potrubí, bez síťové nebo HTTP komunikace:

<img src="../../../translated_images/cs/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Průběh Stdio transportu" width="800"/>

*Stdio transport v akci — vaše aplikace spustí MCP server jako podproces a komunikuje přes stdin/stdout potrubí.*

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) a zeptejte se:
> - "Jak funguje Stdio transport a kdy ho mám použít oproti HTTP?"
> - "Jak LangChain4j spravuje životní cyklus spuštěných MCP serverových procesů?"
> - "Jaká jsou bezpečnostní rizika poskytnutí AI přístupu k souborovému systému?"

## Agentický modul

Zatímco MCP poskytuje standardizované nástroje, LangChain4j **agentický modul** nabízí deklarativní způsob, jak stavět agenty, kteří tyto nástroje orchestrují. Anotace `@Agent` a `AgenticServices` vám umožní definovat chování agenta pomocí rozhraní místo imperativního kódu.

V tomto modulu prozkoumáte vzor **Supervisor Agent** — pokročilý přístup agentické AI, kdy „supervisor“ agent dynamicky rozhoduje, které pod-agenty vyvolat podle uživatelských požadavků. Kombinujeme oba koncepty tím, že jednomu z našich pod-agentů poskytneme schopnosti přístupu k souborům pomocí MCP.

Pro použití agentického modulu přidejte tuto Maven závislost:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Poznámka:** Modul `langchain4j-agentic` používá samostatnou verzi (`langchain4j.mcp.version`), protože vychází v jiném harmonogramu než jádro LangChain4j knihoven.

> **⚠️ Experimentální:** Modul `langchain4j-agentic` je **experimentální** a může se měnit. Stabilní způsob tvorby AI asistentů zůstává `langchain4j-core` s vlastními nástroji (Modul 04).

## Spouštění příkladů

### Požadavky

- Dokončený [Modul 04 - Nástroje](../04-tools/README.md) (tento modul staví na konceptech vlastních nástrojů a jejich srovnání s MCP nástroji)
- `.env` soubor v kořenovém adresáři s Azure přihlašovacími údaji (vytvořený příkazem `azd up` v Modulu 01)
- Java 21+, Maven 3.9+
- Node.js 16+ a npm (pro MCP servery)

> **Poznámka:** Pokud jste ještě nenastavili své environmentální proměnné, podívejte se na [Modul 01 - Úvod](../01-introduction/README.md) pro instrukce nasazení (`azd up` automaticky vytvoří `.env` soubor), nebo zkopírujte `.env.example` do `.env` v kořenovém adresáři a vyplňte své hodnoty.

## Rychlý start

**Použití VS Code:** Jednoduše klikněte pravým tlačítkem na jakýkoliv demo soubor v průzkumníku a vyberte **"Run Java"**, nebo využijte konfigurační profily z panelu Run and Debug (ujistěte se, že váš `.env` soubor je nejprve správně nastaven s Azure přihlašovacími údaji).

**Použití Maven:** Alternativně můžete spustit z příkazové řádky podle níže uvedených příkladů.

### Operace se soubory (Stdio)

Toto demonstruje nástroje založené na lokálním podprocesu.

**✅ Není potřeba žádné předchozí nastavení** - MCP server se spustí automaticky.

**Použití spouštěcích skriptů (doporučeno):**

Spouštěcí skripty automaticky načítají proměnné prostředí z kořenového `.env` souboru:

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

**Použití VS Code:** Klikněte pravým tlačítkem na `StdioTransportDemo.java` a vyberte **"Run Java"** (ujistěte se, že máte nastavený `.env` soubor).

Aplikace automaticky spustí MCP server souborového systému a přečte lokální soubor. Všimněte si, jak je správa podprocesu zajištěna za vás.

**Očekávaný výstup:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor agent

Vzorec **Supervisor Agent** je **flexibilní** forma agentické AI. Supervisor používá LLM, aby autonomně rozhodl, které agenty vyvolat na základě uživatelského požadavku. V dalším příkladu kombinujeme MCP-poháněný přístup k souborům s LLM agentem pro vytvoření workflow čtení souboru → generování zprávy.

V demu `FileAgent` čte soubor pomocí MCP nástrojů souborového systému a `ReportAgent` vygeneruje strukturovanou zprávu s výkonným shrnutím (1 věta), 3 klíčovými body a doporučeními. Supervisor tento tok orchestruje automaticky:

<img src="../../../translated_images/cs/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Vzor Supervisor Agent" width="800"/>

*Supervisor používá svůj LLM k rozhodnutí, které agenty vyvolat a v jakém pořadí — není potřeba žádné tvrdé kódování tras.*

Takto vypadá konkrétní workflow našeho pipeline z souboru do zprávy:

<img src="../../../translated_images/cs/file-report-workflow.649bb7a896800de9.webp" alt="Workflow soubor → zpráva" width="800"/>

*FileAgent čte soubor přes MCP nástroje, potom ReportAgent přemění surový obsah na strukturovanou zprávu.*

Každý agent ukládá svůj výstup do **Agentické oblasti** (sdílená paměť), což umožňuje následujícím agentům přístup k předchozím výsledkům. Toto demonstruje, jak MCP nástroje plynule integrují agentické workflow — Supervisor nemusí vědět, *jak* se soubory čtou, jen že `FileAgent` to umí.

#### Spouštění demo verze

Spouštěcí skripty automaticky načítají proměnné prostředí z kořenového `.env` souboru:

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

**Použití VS Code:** Klikněte pravým tlačítkem na `SupervisorAgentDemo.java` a vyberte **"Run Java"** (ujistěte se, že máte nastavený `.env` soubor).

#### Jak Supervisor funguje

Než začnete stavět agenty, musíte připojit MCP transport ke klientovi a zabalit ho jako `ToolProvider`. Tímto způsobem se MCP nástroje serveru stanou dostupnými pro vaše agenty:

```java
// Vytvořte MCP klienta z transportu
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Zabalte klienta jako ToolProvider — to propojuje MCP nástroje do LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Nyní můžete vpravit `mcpToolProvider` do jakéhokoli agenta, který potřebuje MCP nástroje:

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

// Supervisor řídí workflow soubor → zpráva
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Vrátit konečnou zprávu
        .build();

// Supervisor rozhoduje, které agenty vyvolat na základě požadavku
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Strategie odpovědí

Když konfigurujete `SupervisorAgent`, určujete, jak má formulovat svou finální odpověď uživateli poté, co pod-agenti dokončí své úkoly. Níže uvedený diagram ukazuje tři dostupné strategie — LAST vrací přímo výstup posledního agenta, SUMMARY syntetizuje všechny výstupy přes LLM, a SCORED vybírá ten s vyšším skóre vůči původnímu požadavku:

<img src="../../../translated_images/cs/response-strategies.3d0cea19d096bdf9.webp" alt="Strategie odpovědí" width="800"/>

*Tři strategie, jak Supervisor formuluje finální odpověď — vyberte podle toho, chcete-li poslední výstup agenta, syntetizované shrnutí, nebo nejlepší skórovanou možnost.*

Dostupné strategie jsou:

| Strategie | Popis |
|-----------|-------|
| **LAST** | Supervisor vrací výstup posledního pod-agenta nebo volaného nástroje. Hodí se, když je poslední agent ve workflow speciálně navržen pro vytvoření kompletní konečné odpovědi (např. „Shrnující agent“ ve výzkumném pipeline). |
| **SUMMARY** | Supervisor použije svůj vlastní interní jazykový model (LLM) k syntéze shrnutí celé interakce a všech výstupů pod-agentů a toto shrnutí vrátí jako finální odpověď. Poskytuje čistou, agregovanou odpověď uživateli. |
| **SCORED** | Systém používá interní LLM k ohodnocení jak LAST odpovědi, tak SUMMARY interakce vůči původnímu uživatelskému požadavku a vrací tu, která získala vyšší skóre. |
Viz [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) pro kompletní implementaci.

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) a zeptejte se:
> - „Jak Supervisor rozhoduje, které agenty vyvolat?“
> - „Jaký je rozdíl mezi vzory workflow Supervisor a Sequential?“
> - „Jak mohu přizpůsobit plánovací chování Supervisora?“

#### Pochopení výstupu

Když spustíte demo, uvidíte strukturovaný průchod tím, jak Supervisor orchestruje více agentů. Zde je význam jednotlivých částí:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Hlavička** uvádí koncept workflow: zaměřený proces od čtení souboru až po generování reportu.

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

**Diagram workflow** ukazuje tok dat mezi agenty. Každý agent má specifickou roli:
- **FileAgent** čte soubory pomocí MCP nástrojů a ukládá surový obsah do `fileContent`
- **ReportAgent** spotřebovává tento obsah a vytváří strukturovaný report v `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Uživatelská žádost** ukazuje úkol. Supervisor ji rozparsuje a rozhodne o vyvolání FileAgent → ReportAgent.

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

**Orchestrace Supervisora** ukazuje 2-krokový proces v akci:
1. **FileAgent** přečte soubor přes MCP a uloží obsah
2. **ReportAgent** přijme obsah a vytvoří strukturovaný report

Supervisor tato rozhodnutí učinil **autonomně** na základě požadavku uživatele.

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

Příklad demonstruje několik pokročilých funkcí agentního modulu. Pojďme si blíže představit Agentic Scope a Agent Listeners.

**Agentic Scope** ukazuje sdílenou paměť, kam agenti ukládají své výsledky pomocí `@Agent(outputKey="...")`. To umožňuje:
- Následujícím agentům přistupovat k výstupům předchozích agentů
- Supervisorovi syntetizovat finální odpověď
- Vám kontrolovat, co každý agent vytvořil

Níže uvedený diagram ukazuje, jak Agentic Scope funguje jako sdílená paměť ve workflow ze souboru do reportu — FileAgent zapisuje výstup pod klíč `fileContent`, ReportAgent čte tento klíč a zapisuje svůj výstup pod `report`:

<img src="../../../translated_images/cs/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope funguje jako sdílená paměť — FileAgent zapisuje `fileContent`, ReportAgent to čte a zapisuje `report` a váš kód čte konečný výsledek.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Surová data souboru od FileAgent
String report = scope.readState("report");            // Strukturovaná zpráva od ReportAgent
```

**Agent Listeners** umožňují sledování a ladění provádění agentů. Krok za krokem výstup, který vidíte v demu, pochází z Agent Listener, který se připojuje ke každému vyvolání agenta:
- **beforeAgentInvocation** - Volá se, když Supervisor vybere agenta, takže vidíte, který agent byl zvolen a proč
- **afterAgentInvocation** - Volá se, když agent dokončí, a ukazuje jeho výsledek
- **inheritedBySubagents** - Pokud je true, posluchač sleduje všechny agenty v hierarchii

Následující diagram ukazuje celý životní cyklus Agent Listener, včetně toho, jak `onError` řeší chyby během provádění agenta:

<img src="../../../translated_images/cs/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners se připojují k životnímu cyklu provádění — sledujte, kdy agenti začínají, dokončují nebo narážejí na chyby.*

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
        return true; // Propagovat na všechny podjednotky
    }
};
```

Kromě vzoru Supervisora poskytuje modul `langchain4j-agentic` několik výkonných vzorů workflow. Diagram níže ukazuje všech pět — od jednoduchých sekvenčních pipeline až po workflow s lidským schvalováním:

<img src="../../../translated_images/cs/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Pět vzorů workflow pro orchestraci agentů — od jednoduchých sekvenčních pipeline až po workflow s lidským schvalováním.*

| Vzor | Popis | Použití |
|---------|-------------|----------|
| **Sekvenční** | Spouští agenty za sebou, výstup jde do dalšího | Pipeline: výzkum → analýza → report |
| **Paralelní** | Spouští agenty současně | Nezávislé úkoly: počasí + zprávy + akcie |
| **Smyčka** | Iteruje dokud není splněna podmínka | Hodnocení kvality: zdokonalovat dokud skóre ≥ 0,8 |
| **Podmíněný** | Směruje podle podmínek | Klasifikace → směrování na specialistu |
| **Člověk v procesu** | Přidává lidská kontrolní místa | Schvalovací workflow, kontrola obsahu |

## Klíčové koncepty

Nyní, když jste si prohlédli MCP a agentní modul v praxi, shrňme, kdy použít který přístup.

Jednou z největších výhod MCP je jeho rostoucí ekosystém. Diagram níže ukazuje, jak jeden univerzální protokol propojuje vaši AI aplikaci s širokou škálou MCP serverů — od přístupu k filesystemu a databázím až po GitHub, e-mail, web scraping a další:

<img src="../../../translated_images/cs/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP vytváří ekosystém univerzálního protokolu — jakýkoli MCP-kompatibilní server funguje s jakýmkoli MCP-kompatibilním klientem, což umožňuje sdílení nástrojů napříč aplikacemi.*

**MCP** je ideální, když chcete využít existující ekosystémy nástrojů, vytvářet nástroje sdílené více aplikacemi, integrovat třetí strany pomocí standardních protokolů nebo měnit implementace nástrojů bez změny kódu.

**Agentní modul** se hodí, když chcete deklarativní definice agentů s anotacemi `@Agent`, potřebujete orchestraci workflow (sekvenční, smyčka, paralelní), upřednostňujete návrh agentů založený na rozhraní před imperativním kódem nebo kombinujete více agentů, kteří sdílejí výstupy přes `outputKey`.

**Vzor Supervisora** vyniká, když není workflow dopředu předvídatelný a chcete, aby rozhodovala LLM, když máte více specializovaných agentů vyžadujících dynamickou orchestraci, při budování konverzačních systémů směrujících na různé schopnosti, nebo když chcete nejflexibilnější, adaptivní chování agentů.

Pro usnadnění rozhodování mezi vlastními `@Tool` metodami z modulu 04 a MCP nástroji z tohoto modulu následující srovnání zdůrazňuje klíčové kompromisy — vlastní nástroje poskytují těsnou provázanost a plnou typovou bezpečnost pro aplikační logiku, MCP nástroje nabízejí standardizované, znovupoužitelné integrace:

<img src="../../../translated_images/cs/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Kdy použít vlastní @Tool metody vs MCP nástroje — vlastní nástroje pro aplikační logiku s plnou typovou bezpečností, MCP nástroje pro standardizované integrace fungující napříč aplikacemi.*

## Gratulujeme!

Prošli jste všemi pěti moduly kurzu LangChain4j pro začátečníky! Zde je pohled na celou dokončenou cestu učení — od základního chatu až po agentní systémy poháněné MCP:

<img src="../../../translated_images/cs/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Vaše cesta učením přes všech pět modulů — od základního chatu až po agentní systémy s MCP.*

Dokončili jste kurz LangChain4j pro začátečníky. Naučili jste se:

- Jak vytvářet konverzační AI s pamětí (Modul 01)
- Vzory návrhu promptů pro různé úkoly (Modul 02)
- Zakotvení odpovědí v dokumentech pomocí RAG (Modul 03)
- Vytváření základních AI agentů (asistentů) s vlastními nástroji (Modul 04)
- Integrace standardizovaných nástrojů pomocí LangChain4j MCP a agentního modulu (Modul 05)

### Co dál?

Po dokončení modulů prozkoumejte [Testing Guide](../docs/TESTING.md), kde uvidíte koncepty testování LangChain4j v praxi.

**Oficiální zdroje:**
- [Dokumentace LangChain4j](https://docs.langchain4j.dev/) – Podrobné průvodce a reference API
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) – Zdrojový kód a příklady
- [LangChain4j Tutoriály](https://docs.langchain4j.dev/tutorials/) – Krok za krokem návody pro různé použití

Děkujeme, že jste dokončili tento kurz!

---

**Navigace:** [← Předchozí: Modul 04 - Nástroje](../04-tools/README.md) | [Zpět na hlavní stránku](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o přesnost, mějte prosím na paměti, že automatické překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho mateřském jazyce by měl být považován za autoritativní zdroj. Pro zásadní informace se doporučuje profesionální lidský překlad. Nebereme na sebe odpovědnost za jakákoliv nedorozumění nebo nesprávné interpretace vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->