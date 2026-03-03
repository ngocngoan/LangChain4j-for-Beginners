# Modul 05: Protokol kontextu modelu (MCP)

## Obsah

- [Čo sa naučíte](../../../05-mcp)
- [Čo je MCP?](../../../05-mcp)
- [Ako MCP funguje](../../../05-mcp)
- [Agentický modul](../../../05-mcp)
- [Spustenie príkladov](../../../05-mcp)
  - [Požiadavky](../../../05-mcp)
- [Rýchly štart](../../../05-mcp)
  - [Operácie so súbormi (Stdio)](../../../05-mcp)
  - [Supervisor Agent](../../../05-mcp)
    - [Spustenie ukážky](../../../05-mcp)
    - [Ako funguje Supervisor](../../../05-mcp)
    - [Stratégie odpovedí](../../../05-mcp)
    - [Pochopenie výstupu](../../../05-mcp)
    - [Vysvetlenie funkcií agentického modulu](../../../05-mcp)
- [Kľúčové koncepty](../../../05-mcp)
- [Blahoželáme!](../../../05-mcp)
  - [Čo ďalej?](../../../05-mcp)

## Čo sa naučíte

Vybudovali ste konverzačné AI, osvojili ste si promptovanie, zakotvili odpovede v dokumentoch a vytvorili agentov s nástrojmi. Ale všetky tie nástroje boli vytvorené na mieru pre vašu konkrétnu aplikáciu. Čo keby ste mohli dať vašej AI prístup k štandardizovanému ekosystému nástrojov, ktoré môže ktokoľvek vytvoriť a zdieľať? V tomto module sa naučíte, ako to urobiť pomocou Protokolu kontextu modelu (MCP) a agentického modulu LangChain4j. Najprv ukážeme jednoduchého MCP čítača súborov a potom, ako sa ľahko integruje do pokročilých agentických pracovných tokov pomocou vzoru Supervisor Agent.

## Čo je MCP?

Protokol kontextu modelu (MCP) poskytuje presne toto – štandardný spôsob, ako AI aplikácie môžu objavovať a používať externé nástroje. Namiesto písania vlastných integrácií pre každý zdroj dát alebo službu sa pripojíte k MCP serverom, ktoré svoje schopnosti vystavujú v konzistentnom formáte. Váš AI agent potom tieto nástroje automaticky objaví a použije.

Nižšie uvedený diagram ukazuje rozdiel — bez MCP si každá integrácia vyžaduje vlastné bodové prepojenie; s MCP jeden protokol spája vašu aplikáciu s akýmkoľvek nástrojom:

<img src="../../../translated_images/sk/mcp-comparison.9129a881ecf10ff5.webp" alt="Porovnanie MCP" width="800"/>

*Pred MCP: zložité bodové integrácie. Po MCP: jeden protokol, nekonečné možnosti.*

MCP rieši základný problém vo vývoji AI: každá integrácia je vlastná. Chcete pristupovať ku GitHub? Vlastný kód. Chcete čítať súbory? Vlastný kód. Chcete sa dotazovať do databázy? Vlastný kód. A žiadna z týchto integrácií nefunguje s inými AI aplikáciami.

MCP to štandardizuje. MCP server vystavuje nástroje s jasnými opisami a schémami. Každý MCP klient sa môže pripojiť, objaviť dostupné nástroje a používať ich. Nástroj vybudujte raz, používajte všade.

Nižšie uvedený diagram ilustruje túto architektúru — jeden MCP klient (vaša AI aplikácia) sa pripája k viacerým MCP serverom, z ktorých každý vystavuje svoj súbor nástrojov cez štandardný protokol:

<img src="../../../translated_images/sk/mcp-architecture.b3156d787a4ceac9.webp" alt="Architektúra MCP" width="800"/>

*Architektúra Protokolu kontextu modelu – štandardizované objavovanie a spúšťanie nástrojov*

## Ako MCP funguje

Pod kapotou používa MCP vrstvenú architektúru. Vaša Java aplikácia (MCP klient) objavuje dostupné nástroje, posiela JSON-RPC požiadavky cez transportnú vrstvu (Stdio alebo HTTP) a MCP server vykonáva operácie a vracia výsledky. Nasledujúci diagram rozoberá každú vrstvu tohto protokolu:

<img src="../../../translated_images/sk/mcp-protocol-detail.01204e056f45308b.webp" alt="Detail protokolu MCP" width="800"/>

*Ako MCP funguje pod kapotou – klienti objavujú nástroje, vymieňajú si JSON-RPC správy a vykonávajú operácie cez transportnú vrstvu.*

**Architektúra server-klient**

MCP používa model klient-server. Servery poskytujú nástroje – čítanie súborov, dotazovanie sa databáz, volanie API. Klienti (vaša AI aplikácia) sa pripájajú k serverom a používajú ich nástroje.

Pre použitie MCP v LangChain4j pridajte túto závislosť do Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Objavovanie nástrojov**

Keď sa klient pripojí k MCP serveru, spýta sa „Aké nástroje máte?“ Server odpovie zoznamom dostupných nástrojov, z ktorých každý má popis a parametrovú schému. Váš AI agent potom môže na základe používateľských požiadaviek rozhodnúť, ktoré nástroje použiť. Nižšie uvedený diagram znázorňuje tento handshake — klient posiela požiadavku `tools/list` a server vracia svoje dostupné nástroje s popismi a parametrovými schémami:

<img src="../../../translated_images/sk/tool-discovery.07760a8a301a7832.webp" alt="Objavovanie nástrojov MCP" width="800"/>

*AI pri spustení objavuje dostupné nástroje — teraz vie, aké schopnosti sú k dispozícii, a môže rozhodnúť, ktoré použiť.*

**Prenosové mechanizmy**

MCP podporuje rôzne prenosové mechanizmy. Dve možnosti sú Stdio (pre lokálnu komunikáciu s podprocesmi) a Streamable HTTP (pre vzdialené servery). Tento modul demonštruje Stdio transport:

<img src="../../../translated_images/sk/transport-mechanisms.2791ba7ee93cf020.webp" alt="Prenosové mechanizmy" width="800"/>

*Prenosové mechanizmy MCP: HTTP pre vzdialené servery, Stdio pre lokálne procesy*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Pre lokálne procesy. Vaša aplikácia spúšťa server ako podproces a komunikuje cez štandardný vstup/výstup. Vhodné pre prístup k súborovému systému alebo príkazovým nástrojom.

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

Server `@modelcontextprotocol/server-filesystem` vystavuje nasledujúce nástroje, všetky sandboxované na adresáre, ktoré určíte:

| Nástroj | Popis |
|---------|--------|
| `read_file` | Číta obsah jedného súboru |
| `read_multiple_files` | Číta niekoľko súborov na jedenkrát |
| `write_file` | Vytvorí alebo prepíše súbor |
| `edit_file` | Vykoná cielené nájdenie a nahradenie |
| `list_directory` | Vypíše súbory a adresáre v ceste |
| `search_files` | Rekurzívne vyhľadá súbory podľa vzoru |
| `get_file_info` | Získa metadata súboru (veľkosť, časové pečiatky, práva) |
| `create_directory` | Vytvorí adresár (vrátane rodičovských) |
| `move_file` | Presunie alebo premenová súbor alebo adresár |

Nasledujúci diagram ukazuje, ako Stdio transport funguje počas behu — vaša Java aplikácia spúšťa MCP server ako potomka a komunikujú cez rúrky stdin/stdout, bez siete alebo HTTP:

<img src="../../../translated_images/sk/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Priebeh transportu Stdio" width="800"/>

*Stdio transport v akcii — vaša aplikácia spúšťa MCP server ako podproces a komunikuje cez rúrky stdin/stdout.*

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) a spýtajte sa:
> - "Ako funguje Stdio transport a kedy by som ho mal použiť oproti HTTP?"
> - "Ako LangChain4j spravuje životný cyklus spustených MCP serverov?"
> - "Aké bezpečnostné dôsledky má umožniť AI prístup k súborovému systému?"

## Agentický modul

Zatiaľ čo MCP poskytuje štandardizované nástroje, agentický modul LangChain4j ponúka deklaratívny spôsob, ako vytvárať agentov, ktorí tieto nástroje organizujú. Anotácia `@Agent` a `AgenticServices` vám umožňuje definovať správanie agenta cez rozhrania namiesto imperatívneho kódu.

V tomto module preskúmate vzor **Supervisor Agent** — pokročilý agentický prístup, kde "supervizor" dynamicky rozhoduje, ktorých podagentov vyvolať na základe používateľskej požiadavky. Kombinujeme oba koncepty tým, že jednému z našich podagentov poskytneme schopnosti prístupu k súborom pomocou MCP.

Pre použitie agentického modulu pridajte túto závislosť do Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```


> **Poznámka:** Modul `langchain4j-agentic` používa samostatnú vlastnosť verzie (`langchain4j.mcp.version`), pretože vychádza v iných časových intervaloch než jadro LangChain4j.

> **⚠️ Experimentálne:** Modul `langchain4j-agentic` je **experimentálny** a môže sa meniť. Stabilný spôsob budovania AI asistentov zostáva `langchain4j-core` s vlastnými nástrojmi (Modul 04).

## Spustenie príkladov

### Požiadavky

- Ukončený [Modul 04 - Nástroje](../04-tools/README.md) (tento modul nadväzuje na koncepty vlastných nástrojov a porovnáva ich s MCP nástrojmi)
- Súbor `.env` v koreňovom adresári s Azure povereniami (vytvorený príkazom `azd up` v Module 01)
- Java 21+, Maven 3.9+
- Node.js 16+ a npm (pre MCP servery)

> **Poznámka:** Ak ešte nemáte nastavené environmentálne premenné, pozrite si [Modul 01 - Úvod](../01-introduction/README.md) pre inštrukcie nasadenia (`azd up` automaticky vytvorí `.env`), alebo skopírujte `.env.example` do `.env` v koreňovom adresári a doplňte svoje hodnoty.

## Rýchly štart

**Použitie VS Code:** Jednoducho kliknite pravým tlačidlom na ľubovoľný demo súbor v Prieskumníkovi a vyberte **"Run Java"**, alebo použite spustiteľné konfigurácie v paneli Spustenie a ladenie (najskôr sa uistite, že máte správne nastavený `.env` súbor s Azure prístupmi).

**Použitie Maven:** Alternatívne môžete spustiť príklady z príkazového riadku.

### Operácie so súbormi (Stdio)

Demo ukazuje použitie nástrojov založených na lokálnych podprocesoch.

**✅ Nie sú potrebné žiadne predpoklady** - MCP server sa spustí automaticky.

**Použitie startovacích skriptov (odporúčané):**

Startovacie skripty automaticky načítajú premenné prostredia z koreňového `.env` súboru:

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


**Použitie VS Code:** Kliknite pravým tlačidlom na `StdioTransportDemo.java` a vyberte **"Run Java"** (skontrolujte konfiguráciu `.env`).

Aplikácia automaticky spustí MCP server pre súborový systém a prečíta lokálny súbor. Všimnite si, ako za vás spravuje podproces.

**Očakávaný výstup:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```


### Supervisor Agent

Vzor **Supervisor Agent** je **flexibilná** forma agentického AI. Supervisor používa LLM na autonómne rozhodovanie, ktorých agentov vyvolať podľa používateľovej požiadavky. V ďalšom príklade kombinujeme MCP napájaný prístup k súborom s LLM agentom na vytvorenie spravovaného pracovného toku čítania súboru → generovania reportu.

V demo ukážke `FileAgent` číta súbor pomocou MCP nástrojov pre súborový systém a `ReportAgent` generuje štruktúrovaný report so súhrnom (1 veta), 3 kľúčovými bodmi a odporúčaniami. Supervisor tento tok automaticky riadi:

<img src="../../../translated_images/sk/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Vzor Supervisor Agent" width="800"/>

*Supervisor používa svoj LLM, aby rozhodol, ktorých agentov vyvolať a v akom poradí — nie je potrebné žiadne pevné smerovanie.*

Takto vyzerá konkrétny pracovný tok nášho pipeline od súboru k reportu:

<img src="../../../translated_images/sk/file-report-workflow.649bb7a896800de9.webp" alt="Pracovný tok súbor → report" width="800"/>

*FileAgent číta súbor cez MCP nástroje, potom ReportAgent transformuje surový obsah do štruktúrovaného reportu.*

Každý agent ukladá svoj výstup do **Agentic Scope** (zdieľanej pamäte), čo umožňuje nasledujúcim agentom pristupovať k predchádzajúcim výsledkom. Toto demonštruje, ako sa MCP nástroje hladko integrujú do agentických pracovných tokov — Supervisor nemusí vedieť *ako* sa súbory čítajú, len že to dokáže `FileAgent`.

#### Spustenie ukážky

Startovacie skripty automaticky načítajú premenné prostredia z koreňového `.env` súboru:

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


**Použitie VS Code:** Kliknite pravým tlačidlom na `SupervisorAgentDemo.java` a vyberte **"Run Java"** (skontrolujte konfiguráciu `.env`).

#### Ako funguje Supervisor

Pred vytváraním agentov musíte pripojiť MCP transport ku klientovi a zabaliť ho ako `ToolProvider`. Toto je cesta, ako nástroje servera MCP získajú dostupnosť pre vašich agentov:

```java
// Vytvorte MCP klienta z transportu
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Zabaľte klienta ako ToolProvider — toto prepojí MCP nástroje do LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```


Teraz môžete `mcpToolProvider` injektovať do ktoréhokoľvek agenta, ktorý potrebuje MCP nástroje:

```java
// Krok 1: FileAgent číta súbory pomocou nástrojov MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Má nástroje MCP pre operácie so súbormi
        .build();

// Krok 2: ReportAgent generuje štruktúrované správy
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor riadi workflow súbor → správa
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Vrátiť finálnu správu
        .build();

// Supervisor rozhoduje, ktorých agentov vyvolať na základe požiadavky
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```


#### Stratégie odpovedí

Pri konfigurácii `SupervisorAgent` špecifikujete, ako by mala agent formulovať svoju konečnú odpoveď používateľovi po dokončení úloh podagentov. Nasledujúci diagram ukazuje tri dostupné stratégie — LAST vracia priamo výstup posledného agenta, SUMMARY zhrňuje všetky výstupy cez LLM a SCORED vyberá ten výstup, ktorý dosiahol lepšie skóre voči pôvodnej požiadavke:

<img src="../../../translated_images/sk/response-strategies.3d0cea19d096bdf9.webp" alt="Stratégie odpovedí" width="800"/>

*Tri stratégie, ako Supervisor formuluje svoju konečnú odpoveď — vyberte podľa toho, či chcete výstup posledného agenta, syntetizované zhrnutie alebo najlepšie skórovanú možnosť.*

Dostupné stratégie sú:

| Stratégia | Popis |
|-----------|--------|
| **LAST** | Supervisor vracia výstup posledného podagenta alebo nástroja, ktorý bol volaný. Je to užitočné, keď je posledný agent vo workflow špecificky navrhnutý na vytvorenie kompletnej finálnej odpovede (napr. "Summary Agent" v pipeline výskumu). |
| **SUMMARY** | Supervisor použije svoj interný jazykový model (LLM), aby syntetizoval zhrnutie celého interakčného procesu a všetkých výstupov podagentov, a túto syntézu následne vráti ako konečnú odpoveď. Poskytuje tak čistú, agregovanú odpoveď používateľovi. |
| **SCORED** | Systém použije interný LLM na ohodnotenie ako LAST odpovede, tak SUMMARY interakcie voči pôvodnej požiadavke používateľa a vráti tú odpoveď, ktorá dostane vyššie skóre. |
Pozrite si [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) pre kompletnú implementáciu.

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) a spýtajte sa:
> - "Ako Supervisor rozhoduje, ktorých agentov zavolať?"
> - "Aký je rozdiel medzi vzormi Supervisor a Sequential workflow?"
> - "Ako môžem prispôsobiť plánovacie správanie Supervisora?"

#### Pochopenie výstupu

Keď spustíte demo, uvidíte štruktúrovaný prehľad toho, ako Supervisor koordinuje viacerých agentov. Tu je, čo znamená každá časť:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Hlavička** predstavuje koncept workflow: zameraný tok od čítania súboru po generovanie správy.

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

**Diagram workflow** zobrazuje tok dát medzi agentmi. Každý agent má konkrétnu úlohu:
- **FileAgent** číta súbory pomocou MCP nástrojov a ukladá surový obsah do `fileContent`
- **ReportAgent** používa tento obsah a vytvára štruktúrovanú správu v `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Žiadosť používateľa** zobrazuje úlohu. Supervisor ju analyzuje a rozhodne sa zavolať FileAgent → ReportAgent.

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

**Orchestrace Supervisora** ukazuje 2-krokový priebeh v akcii:
1. **FileAgent** číta súbor cez MCP a ukladá obsah
2. **ReportAgent** prijíma obsah a generuje štruktúrovanú správu

Supervisor urobil tieto rozhodnutia **autonómne** na základe požiadavky používateľa.

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

#### Vysvetlenie funkcií agentic modulu

Ukážka demonštruje niekoľko pokročilých vlastností agentic modulu. Pozrime sa bližšie na Agentic Scope a Agent Listeners.

**Agentic Scope** zobrazuje zdieľanú pamäť, kde agenti ukladali svoje výsledky pomocou `@Agent(outputKey="...")`. To umožňuje:
- Neskorším agentom pristupovať k výstupom predchádzajúcich agentov
- Supervisorovi syntetizovať finálnu odpoveď
- Vám si skontrolovať, čo ktorý agent vytvoril

Nižšie uvedený diagram ukazuje, ako Agentic Scope funguje ako zdieľaná pamäť v pracovnom toku zo súboru do správy — FileAgent zapisuje svoj výstup pod kľúč `fileContent`, ReportAgent ho číta a zapisuje svoj vlastný výstup pod `report`:

<img src="../../../translated_images/sk/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope funguje ako zdieľaná pamäť — FileAgent zapisuje `fileContent`, ReportAgent ho číta a zapisuje `report` a váš kód číta finálny výsledok.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Nezpracované údaje súboru z FileAgent
String report = scope.readState("report");            // Štruktúrovaná správa z ReportAgent
```

**Agent Listeners** umožňujú monitorovanie a ladenie spustenia agentov. Krok za krokom výstup, ktorý vidíte v dema, pochádza z AgentListenera, ktorý sa pripája ku každému volaniu agenta:
- **beforeAgentInvocation** - Volané, keď Supervisor vyberie agenta, umožňuje vidieť, ktorý agent bol zvolený a prečo
- **afterAgentInvocation** - Volané po dokončení agenta, zobrazuje jeho výsledok
- **inheritedBySubagents** - Ak je pravda, listener sleduje všetkých agentov v hierarchii

Nasledujúci diagram zobrazuje celý životný cyklus Agent Listenera vrátane spracovania chýb cez `onError` počas spustenia agenta:

<img src="../../../translated_images/sk/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners sa pripájajú k životnému cyklu vykonávania — monitorujú, keď agenti začínajú, končia alebo narazia na chyby.*

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
        return true; // Rozšíriť na všetkých podagentov
    }
};
```

Okrem vzoru Supervisor poskytuje modul `langchain4j-agentic` niekoľko výkonných vzorov workflow. Diagram nižšie zobrazuje všetkých päť — od jednoduchých sekvenčných pipeline až po workflow s ľudskými kontrolami:

<img src="../../../translated_images/sk/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Päť vzorov workflow na orchestráciu agentov — od jednoduchých sekvenčných pipeline až po schvaľovacie workflow s ľudským zásahom.*

| Vzor | Popis | Použitie |
|---------|-------------|----------|
| **Sekvenčný** | Spustenie agentov po poradí, výstup ide na ďalšieho | Pipeline: výskum → analýza → správa |
| **Paralelný** | Súbežné spustenie agentov | Nezávislé úlohy: počasie + správy + akcie |
| **Cyklus** | Opakovanie, kým nie je splnená podmienka | Skórovanie kvality: zlepšovať kým skóre ≥ 0,8 |
| **Podmienený** | Trasa podľa podmienok | Klasifikovať → nasmerovať na špecialistu |
| **S ľudským zásahom** | Pridať ľudské kontrolné body | Schvaľovacie workflow, revízia obsahu |

## Kľúčové koncepty

Teraz, keď ste preskúmali MCP a agentic modul v akcii, zhrnieme, kedy použiť ktorý prístup.

Jednou z najväčších výhod MCP je jeho rastúca ekosystémová podpora. Diagram nižšie ukazuje, ako jeden univerzálny protokol spája vašu AI aplikáciu so širokou škálou MCP serverov — od prístupu k súborovému systému a databázam až po GitHub, e-mail, web scraping a ďalšie:

<img src="../../../translated_images/sk/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP vytvára univerzálny protokolový ekosystém — každý MCP-kompatibilný server funguje s akýmkoľvek MCP-kompatibilným klientom, čo umožňuje zdieľanie nástrojov medzi aplikáciami.*

**MCP** je ideálny, keď chcete využiť existujúce ekosystémy nástrojov, vytvoriť nástroje, ktoré môžu používať viaceré aplikácie, integrovať služby tretích strán s bežnými protokolmi alebo meniť implementácie nástrojov bez zmeny kódu.

**Agentic modul** je najlepší, keď potrebujete deklaratívne definície agentov s anotáciami `@Agent`, potrebujete orchestráciu workflow (sekvenčné, cyklus, paralelné), preferujete návrh agentov na základe rozhraní namiesto imperatívneho kódu alebo kombinujete viacerých agentov, ktorí zdieľajú výstupy cez `outputKey`.

**Vzor Supervisor Agent** vyniká, keď workflow nie je vopred predvídateľný a chcete, aby o ňom rozhodoval LLM, keď máte viacero špecializovaných agentov vyžadujúcich dynamickú orchestráciu, pri budovaní konverzačných systémov, ktoré smerujú na rôzne schopnosti, alebo keď chcete najflexibilnejšie a najadaptívnejšie správanie agentov.

Aby ste sa rozhodli medzi vlastnými metódami `@Tool` z modulu 04 a MCP nástrojmi z tohto modulu, nasledujúce porovnanie zvýrazňuje kľúčové kompromisy — vlastné nástroje poskytujú pevné väzby a plnú typovú bezpečnosť pre špecifickú logiku aplikácie, zatiaľ čo MCP nástroje ponúkajú štandardizované, znovupoužiteľné integrácie:

<img src="../../../translated_images/sk/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Kedy použiť vlastné metódy @Tool vs MCP nástroje — vlastné nástroje pre aplikačne špecifickú logiku s plnou typovou bezpečnosťou, MCP nástroje pre štandardizované integrácie fungujúce naprieč aplikáciami.*

## Gratulujeme!

Prešli ste všetkými piatimi modulmi kurzu LangChain4j pre začiatočníkov! Tu je prehľad celej vašej učebnej cesty — od základného chatu až po agentic systémy poháňané MCP:

<img src="../../../translated_images/sk/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Vaša učebná cesta cez všetkých päť modulov — od základného chatu po agentic systémy poháňané MCP.*

Ukázali sme si:

- Ako vytvárať konverzačné AI s pamäťou (Modul 01)
- Vzory pre prompt engineering pre rôzne úlohy (Modul 02)
- Zakladanie odpovedí vo vašich dokumentoch pomocou RAG (Modul 03)
- Vytváranie základných AI agentov (asistentov) s vlastnými nástrojmi (Modul 04)
- Integráciu štandardizovaných nástrojov s LangChain4j MCP a Agentic modulmi (Modul 05)

### Čo ďalej?

Po dokončení modulov preskúmajte [Testing Guide](../docs/TESTING.md), aby ste videli koncepty testovania LangChain4j v praxi.

**Oficiálne zdroje:**
- [LangChain4j Dokumentácia](https://docs.langchain4j.dev/) - Podrobné návody a API reference
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Zdrojový kód a príklady
- [LangChain4j Tutoriály](https://docs.langchain4j.dev/tutorials/) - Postupné tutoriály pre rôzne prípady použitia

Ďakujeme, že ste absolvovali tento kurz!

---

**Navigácia:** [← Predchádzajúci: Modul 04 - Nástroje](../04-tools/README.md) | [Späť na hlavnú stránku](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vyhlásenie o zodpovednosti**:
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Hoci sa snažíme zabezpečiť presnosť, majte prosím na pamäti, že automatizované preklady môžu obsahovať chyby alebo nepresnosti. Originálny dokument v jeho pôvodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za akékoľvek nedorozumenia alebo nesprávne výklady vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->