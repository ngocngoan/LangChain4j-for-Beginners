# Modul 05: Protokol modelového kontextu (MCP)

## Obsah

- [Co se naučíte](../../../05-mcp)
- [Co je MCP?](../../../05-mcp)
- [Jak MCP funguje](../../../05-mcp)
- [Agentický modul](../../../05-mcp)
- [Spuštění ukázek](../../../05-mcp)
  - [Požadavky](../../../05-mcp)
- [Rychlý start](../../../05-mcp)
  - [Operace se soubory (Stdio)](../../../05-mcp)
  - [Supervisor Agent](../../../05-mcp)
    - [Spuštění demo ukázky](../../../05-mcp)
    - [Jak Supervisor funguje](../../../05-mcp)
    - [Strategie odpovědí](../../../05-mcp)
    - [Porozumění výstupu](../../../05-mcp)
    - [Vysvětlení funkcí agentického modulu](../../../05-mcp)
- [Klíčové koncepty](../../../05-mcp)
- [Gratulujeme!](../../../05-mcp)
  - [Co dál?](../../../05-mcp)

## Co se naučíte

Postavili jste konverzační AI, ovládli jste promptování, propojili odpovědi s dokumenty a vytvořili agenty s nástroji. Ale všechny tyto nástroje byly stavěny na míru vaší konkrétní aplikaci. Co kdybyste mohli vaší AI dát přístup ke standardizovanému ekosystému nástrojů, které může kdokoli vytvářet a sdílet? V tomto modulu se naučíte, jak to udělat pomocí Protokolu modelového kontextu (MCP) a agentického modulu LangChain4j. Nejprve ukážeme jednoduchého MCP čtečku souborů a pak, jak se snadno integruje do pokročilých agentických workflow pomocí vzoru Supervisor Agent.

## Co je MCP?

Protokol modelového kontextu (MCP) poskytuje právě to - standardní způsob, jak mohou AI aplikace objevovat a používat externí nástroje. Místo psaní vlastních integrací pro každý zdroj dat nebo službu se připojujete k MCP serverům, které vystavují své schopnosti v konzistentním formátu. Váš AI agent pak může tyto nástroje automaticky objevovat a používat.

<img src="../../../translated_images/cs/mcp-comparison.9129a881ecf10ff5.webp" alt="Srovnání MCP" width="800"/>

*Před MCP: složité point-to-point integrace. Po MCP: Jeden protokol, nekonečné možnosti.*

MCP řeší základní problém ve vývoji AI: každá integrace je na míru. Chcete přistupovat k GitHubu? Vlastní kód. Chcete číst soubory? Vlastní kód. Chcete dotazovat databázi? Vlastní kód. A žádná z těchto integrací nefunguje s jinými AI aplikacemi.

MCP toto standardizuje. MCP server vystavuje nástroje s jasnými popisy a schématy parametrů. Jakýkoli MCP klient se může připojit, objevit dostupné nástroje a používat je. Postavíte jednou, používáte všude.

<img src="../../../translated_images/cs/mcp-architecture.b3156d787a4ceac9.webp" alt="Architektura MCP" width="800"/>

*Architektura Protokolu modelového kontextu – standardizované objevování a spouštění nástrojů*

## Jak MCP funguje

<img src="../../../translated_images/cs/mcp-protocol-detail.01204e056f45308b.webp" alt="Detail protokolu MCP" width="800"/>

*Jak MCP funguje pod pokličkou — klienti objevují nástroje, vyměňují si zprávy JSON-RPC a vykonávají operace přes transportní vrstvu.*

**Architektura server-klient**

MCP používá model klient-server. Servery poskytují nástroje - čtení souborů, dotazy do databází, volání API. Klienti (vaše AI aplikace) se k serverům připojují a používají jejich nástroje.

Pro použití MCP s LangChain4j přidejte tuto závislost v Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Objevování nástrojů**

Když se váš klient připojí k MCP serveru, zeptá se „Jaké nástroje máš?“ Server odpoví seznamem dostupných nástrojů, každý s popisem a schématem parametrů. Váš AI agent pak může rozhodnout, které nástroje použije na základě požadavků uživatele.

<img src="../../../translated_images/cs/tool-discovery.07760a8a301a7832.webp" alt="Objevování nástrojů MCP" width="800"/>

*AI objeví dostupné nástroje při spuštění – nyní ví, jaké schopnosti jsou k dispozici, a může rozhodnout, které použít.*

**Transportní mechanismy**

MCP podporuje různé transportní mechanismy. Tento modul demonstruje Stdio transport pro lokální procesy:

<img src="../../../translated_images/cs/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transportní mechanismy" width="800"/>

*Transportní mechanismy MCP: HTTP pro vzdálené servery, Stdio pro lokální procesy*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Pro lokální procesy. Vaše aplikace spustí server jako podproces a komunikuje přes standardní vstup/výstup. Užitočné pro přístup k souborovému systému nebo příkazové nástroje.

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

<img src="../../../translated_images/cs/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Průběh transportu Stdio" width="800"/>

*Stdio transport v akci — vaše aplikace spouští MCP server jako dětský proces a komunikuje přes stdin/stdout pipe.*

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) a zeptejte se:
> - „Jak funguje Stdio transport a kdy by měl být použit místo HTTP?“
> - „Jak LangChain4j spravuje životní cyklus spouštěných MCP server procesů?“
> - „Jaké jsou bezpečnostní dopady poskytnutí AI přístupu k souborovému systému?“

## Agentický modul

Zatímco MCP poskytuje standardizované nástroje, agentický modul LangChain4j nabízí deklarativní způsob, jak stavět agenty, kteří orchestrují tyto nástroje. Anotace `@Agent` a `AgenticServices` vám umožňují definovat chování agenta přes rozhraní místo imperativního kódu.

V tomto modulu prozkoumáte vzor **Supervisor Agent** — pokročilý agentický přístup, kdy „supervisor“ agent dynamicky rozhoduje, které pod-agenty vyvolat podle uživatelských požadavků. Kombinujeme oba koncepty tak, že jednomu z našich pod-agentů dáme možnost přístupu k souborům pomocí MCP.

Pro použití agentického modulu přidejte tuto závislost v Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **⚠️ Experimentální:** Modul `langchain4j-agentic` je **experimentální** a může se měnit. Stabilní způsob tvorby AI asistentů zůstává přes `langchain4j-core` s vlastními nástroji (Modul 04).

## Spuštění ukázek

### Požadavky

- Java 21+, Maven 3.9+
- Node.js 16+ a npm (pro MCP servery)
- Nastavené proměnné prostředí v `.env` souboru (z kořenové složky):
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (stejné jako v Modulech 01-04)

> **Poznámka:** Pokud jste si ještě nenastavili proměnné prostředí, podívejte se na [Modul 00 - Rychlý start](../00-quick-start/README.md) pro instrukce, nebo zkopírujte `.env.example` do `.env` v kořenovém adresáři a vyplňte hodnoty.

## Rychlý start

**Použití VS Code:** Jednoduše pravým klikem na libovolný demo soubor v Průzkumníku zvolte **„Run Java“**, nebo použijte spouštěcí konfigurace v panelu Spuštění a ladění (ujistěte se, že jste nejdříve přidali token do `.env` souboru).

**Použití Maven:** Alternativně můžete spustit z příkazové řádky pomocí níže uvedených příkladů.

### Operace se soubory (Stdio)

Tento příklad ukazuje nástroje založené na lokálního podprocesu.

**✅ Nejsou potřeba žádné předchozí požadavky** – MCP server je spuštěn automaticky.

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

**Použití VS Code:** Pravým klikem na `StdioTransportDemo.java` zvolte **„Run Java“** (ujistěte se, že máte nakonfigurován `.env` soubor).

Aplikace automaticky spustí MCP server pro souborový systém a přečte lokální soubor. Všimněte si, jak je správa podprocesů řešena za vás.

**Očekávaný výstup:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor Agent

Vzor **Supervisor Agent** je **flexibilní** forma agentické AI. Supervisor používá LLM k autonomnímu rozhodování, kteří agenti mají být vyvoláni na základě uživatelova požadavku. V další ukázce zkombinujeme přístup k souborům přes MCP s LLM agentem za účelem vytvoření workflow čtení souboru → generování reportu pod dohledem supervisora.

V demu `FileAgent` čte soubor pomocí MCP nástrojů souborového systému a `ReportAgent` generuje strukturovaný report s výkonným shrnutím (1 věta), 3 klíčovými body a doporučeními. Supervisor tento tok automaticky řídí:

<img src="../../../translated_images/cs/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Vzor Supervisor Agent" width="800"/>

*Supervisor používá své LLM, aby rozhodl, které agenty a v jakém pořadí zavolat – žádné pevné směrování není potřeba.*

Takto vypadá konkrétní workflow námi použitého pipeline od souboru k reportu:

<img src="../../../translated_images/cs/file-report-workflow.649bb7a896800de9.webp" alt="Workflow soubor → report" width="800"/>

*FileAgent čte soubor přes MCP nástroje, pak ReportAgent transformuje surový obsah do strukturovaného reportu.*

Každý agent ukládá svůj výstup v **Agentic Scope** (sdílené paměti), což umožňuje ostatním agentům přístup k předchozím výsledkům. To ukazuje, jak MCP nástroje plynule zapadají do agentických workflow — Supervisor nemusí vědět *jak* se soubory čtou, pouze že to `FileAgent` umí.

#### Spuštění demo

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

**Použití VS Code:** Pravým klikem na `SupervisorAgentDemo.java` zvolte **„Run Java“** (ujistěte se, že `.env` soubor je nakonfigurován).

#### Jak Supervisor funguje

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

// Supervisor řídí pracovní postup soubor → zpráva
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Vrátit výslednou zprávu
        .build();

// Supervisor rozhoduje, které agenty vyvolat na základě požadavku
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Strategie odpovědí

Při konfiguraci `SupervisorAgent` určujete, jak má formulovat svou konečnou odpověď uživateli po dokončení úkolů pod-agentů.

<img src="../../../translated_images/cs/response-strategies.3d0cea19d096bdf9.webp" alt="Strategie odpovědí" width="800"/>

*Tři strategie, jak Supervisor formulují svou závěrečnou odpověď – vyberte podle toho, zda chcete výstup posledního agenta, syntetizované shrnutí nebo nejlepší hodnocenou možnost.*

Dostupné strategie jsou:

| Strategie | Popis |
|----------|-------------|
| **LAST** | Supervisor vrátí výstup posledního pod-agenta nebo nástroje, který byl zavolán. To je užitečné, když poslední agent v workflow je speciálně navržený pro vytvoření konečné odpovědi (například „Summary Agent“ v badatelském pipeline). |
| **SUMMARY** | Supervisor použije své vlastní interní jazykové modely (LLM) k vytvoření shrnutí celé interakce a všech výstupů pod-agentů, které pak vrátí jako závěrečnou odpověď. To poskytuje čistou, agregovanou odpověď uživateli. |
| **SCORED** | Systém využije interní LLM k bodování jak odpovědi LAST, tak shrnutí SUMMARY vůči původnímu požadavku uživatele a vrátí výstup, který obdržel lepší skóre. |

Podívejte se na [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) pro kompletní implementaci.

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) a zeptejte se:
> - „Jak Supervisor rozhoduje, které agenty zavolat?“
> - „Jaký je rozdíl mezi Supervisorem a vzory sekvenčního workflow?“
> - „Jak mohu přizpůsobit plánování Supervisora?“

#### Porozumění výstupu

Když spustíte demo, uvidíte strukturovaný průchod tím, jak Supervisor koordinuje více agentů. Tady je význam jednotlivých částí:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Hlavička** představuje koncept workflow: zaměřený pipeline od čtení souboru ke generování reportu.

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
- **ReportAgent** zpracovává tento obsah a vytváří strukturovaný report v `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Uživatelský požadavek** ukazuje úkol. Supervisor jej zpracuje a rozhodne se spustit FileAgent → ReportAgent.

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

**Supervisor orchestrace** demonstruje dvoufázový tok v praxi:
1. **FileAgent** čte soubor přes MCP a ukládá obsah
2. **ReportAgent** obdrží obsah a vygeneruje strukturovaný report

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

#### Vysvětlení funkcí agentického modulu

Ukázka demonstruje několik pokročilých funkcí agentického modulu. Podívejme se blíže na Agentic Scope a Agent Listeners.

**Agentic Scope** zobrazuje sdílenou paměť, kam agenti ukládají své výsledky pomocí `@Agent(outputKey="...")`. To umožňuje:
- Následným agentům přístup k výstupům předchozích agentů
- Supervisorovi syntetizovat závěrečnou odpověď
- Vám prohlédnout, co který agent vytvořil

<img src="../../../translated_images/cs/agentic-scope.95ef488b6c1d02ef.webp" alt="Sdílená paměť Agentic Scope" width="800"/>

*Agentic Scope působí jako sdílená paměť — FileAgent zapisuje `fileContent`, ReportAgent ho čte a zapisuje `report`, a váš kód čte finální výsledek.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Surová data souboru z FileAgent
String report = scope.readState("report");            // Strukturovaná zpráva z ReportAgent
```

**Agent Listeners** umožňují sledovat a ladit běh agentů. Krok po kroku výstup, který vidíte v demu, pochází z AgentListeneru, který se zapojuje do každého volání agenta:
- **beforeAgentInvocation** - Volá se, když Supervisor vybere agenta, což vám umožní vidět, který agent byl vybrán a proč
- **afterAgentInvocation** - Volá se po dokončení agenta, zobrazí jeho výsledek
- **inheritedBySubagents** - Když je pravda, posluchač sleduje všechny agenty v hierarchii

<img src="../../../translated_images/cs/agent-listeners.784bfc403c80ea13.webp" alt="Životní cyklus posluchačů agentů" width="800"/>

*Posluchači agentů se připojují do životního cyklu provádění — sledují, kdy agenti začínají, končí nebo narážejí na chyby.*

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
        return true; // Propagovat na všechny pod-agenty
    }
};
```

Kromě vzoru Supervisor poskytuje modul `langchain4j-agentic` několik výkonných vzorů pracovních toků a funkcí:

<img src="../../../translated_images/cs/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Vzory pracovních toků agentů" width="800"/>

*Pět vzorů pracovních toků pro orchestraci agentů — od jednoduchých sekvenčních linek až po schvalovací pracovní toky s lidským zásahem.*

| Vzor | Popis | Použití |
|---------|-------------|----------|
| **Sekvenční** | Spouští agenty v pořadí, výstup přechází na další | Linky: výzkum → analýza → report |
| **Paralelní** | Spouští agenty současně | Nezávislé úkoly: počasí + zprávy + akcie |
| **SmLoop** | Opakuje, dokud není podmínka splněna | Hodnocení kvality: zdokonalovat, dokud skóre ≥ 0,8 |
| **Podmíněný** | Směruje podle podmínek | Klasifikace → předání specialistovi |
| **Lidský v cyklu** | Přidává lidské kontrolní body | Schvalovací pracovní toky, kontrola obsahu |

## Klíčové koncepty

Nyní, když jste prozkoumali MCP a modul agentic v praxi, shrňme, kdy použít který přístup.

<img src="../../../translated_images/cs/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="Ekosystém MCP" width="800"/>

*MCP vytváří univerzální protokolový ekosystém — jakýkoli MCP-kompatibilní server funguje s jakýmkoli MCP-kompatibilním klientem, což umožňuje sdílení nástrojů mezi aplikacemi.*

**MCP** je ideální, když chcete využít existující ekosystémy nástrojů, vytvářet nástroje sdílené více aplikacemi, integrovat služby třetích stran se standardními protokoly nebo měnit implementaci nástrojů bez změny kódu.

**Modul Agentic** nejlépe funguje, pokud chcete deklarativní definice agentů s anotacemi `@Agent`, potřebujete orchestraci pracovních toků (sekvenční, smyčka, paralelní), preferujete návrh agentů založený na rozhraních místo imperativního kódu nebo kombinujete více agentů sdílejících výstupy přes `outputKey`.

**Vzor Supervisor Agent** vyniká, když není workflow předem předvídatelný a chcete, aby LLM rozhodoval, když máte více specializovaných agentů vyžadujících dynamickou orchestraci, při budování konverzačních systémů směrujících na různé schopnosti nebo když chcete nejflexibilnější, adaptivní chování agenta.

<img src="../../../translated_images/cs/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Vlastní nástroje vs MCP nástroje" width="800"/>

*Kdy použít vlastní metody @Tool versus MCP nástroje — vlastní nástroje pro specifickou logiku aplikace s plnou typovou bezpečností, MCP nástroje pro standardizované integrace fungující napříč aplikacemi.*

## Gratulujeme!

<img src="../../../translated_images/cs/course-completion.48cd201f60ac7570.webp" alt="Dokončení kurzu" width="800"/>

*Vaše cesta učením napříč všemi pěti moduly — od základního chatu po agentní systémy poháněné MCP.*

Úspěšně jste dokončili kurz LangChain4j pro začátečníky. Naučili jste se:

- Jak vytvořit konverzační AI s pamětí (Modul 01)
- Vzory promptinženýrství pro různé úkoly (Modul 02)
- Zakotvit odpovědi v dokumentech pomocí RAG (Modul 03)
- Vytvářet základní AI agenty (asistenty) s vlastními nástroji (Modul 04)
- Integrovat standardizované nástroje s moduly LangChain4j MCP a Agentic (Modul 05)

### Co dál?

Po dokončení modulů prozkoumejte [Testovací příručku](../docs/TESTING.md), kde uvidíte koncepty testování LangChain4j v akci.

**Oficiální zdroje:**
- [Dokumentace LangChain4j](https://docs.langchain4j.dev/) - Komplexní průvodce a referenční API
- [GitHub LangChain4j](https://github.com/langchain4j/langchain4j) - Zdrojový kód a příklady
- [Tutoriály LangChain4j](https://docs.langchain4j.dev/tutorials/) - Krok za krokem průvodci pro různá použití

Děkujeme, že jste dokončili tento kurz!

---

**Navigace:** [← Předchozí: Modul 04 - Nástroje](../04-tools/README.md) | [Zpět na hlavní stránku](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). I když usilujeme o přesnost, mějte prosím na paměti, že automatické překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho rodném jazyce by měl být považován za rozhodující zdroj. Pro důležité informace se doporučuje profesionální lidský překlad. Nejsme odpovědní za jakékoliv nedorozumění nebo mylné výklady vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->