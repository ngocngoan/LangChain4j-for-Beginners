# Modul 05: Protokol kontextu modelu (MCP)

## Obsah

- [Čo sa naučíte](../../../05-mcp)
- [Čo je MCP?](../../../05-mcp)
- [Ako MCP funguje](../../../05-mcp)
- [Agentický modul](../../../05-mcp)
- [Spustenie príkladov](../../../05-mcp)
  - [Predpoklady](../../../05-mcp)
- [Rýchly štart](../../../05-mcp)
  - [Súborové operácie (Stdio)](../../../05-mcp)
  - [Supervízny agent](../../../05-mcp)
    - [Spustenie demo ukážky](../../../05-mcp)
    - [Ako funguje supervízor](../../../05-mcp)
    - [Ako FileAgent zistí MCP nástroje za behu](../../../05-mcp)
    - [Strategie odpovedí](../../../05-mcp)
    - [Pochopenie výstupu](../../../05-mcp)
    - [Vysvetlenie funkcií agentického modulu](../../../05-mcp)
- [Kľúčové koncepty](../../../05-mcp)
- [Blahoželáme!](../../../05-mcp)
  - [Čo ďalej?](../../../05-mcp)

## Čo sa naučíte

Vybudovali ste konverzačnú AI, zvládli promptovanie, zakotvili odpovede v dokumentoch a vytvorili agentov s nástrojmi. Ale všetky tie nástroje boli na mieru vytvorené pre vašu konkrétnu aplikáciu. Čo keby ste mohli vašej AI poskytnúť prístup ku štandardizovanému ekosystému nástrojov, ktoré môže ktokoľvek vytvárať a zdieľať? V tomto module sa naučíte presne toto pomocou Protokolu kontextu modelu (MCP) a agentického modulu LangChain4j. Najprv ukážeme jednoduchý MCP čítač súborov a potom ukážeme, ako sa ľahko integruje do pokročilých agentických pracovných tokov pomocou vzoru Supervízneho agenta.

## Čo je MCP?

Protokol kontextu modelu (MCP) poskytuje presne to - štandardný spôsob, ako AI aplikácie môžu objavovať a používať externé nástroje. Namiesto písania vlastných integrácií pre každý zdroj dát alebo službu sa pripájate k MCP serverom, ktoré svoje schopnosti vystavujú v konzistentnom formáte. Váš AI agent potom môže tieto nástroje automaticky objaviť a používať.

Diagram nižšie ukazuje rozdiel — bez MCP každá integrácia vyžaduje vlastné bodové prepojenia; s MCP jediný protokol pripája vašu aplikáciu k akémukoľvek nástroju:

<img src="../../../translated_images/sk/mcp-comparison.9129a881ecf10ff5.webp" alt="Porovnanie MCP" width="800"/>

*Pred MCP: Zložité bodové integrácie. Po MCP: Jeden protokol, nekonečné možnosti.*

MCP rieši základný problém vo vývoji AI: každá integrácia je na mieru. Chcete pristupovať ku GitHub? Vlastný kód. Chcete čítať súbory? Vlastný kód. Chcete dopytovať databázu? Vlastný kód. A žiadna z týchto integrácií nefunguje s inými AI aplikáciami.

MCP štandardizuje toto. MCP server vystavuje nástroje s jasnými popismi a schémami parametrov. Akýkoľvek MCP klient sa môže pripojiť, zistiť dostupné nástroje a používať ich. Postav raz, používaj všade.

Diagram nižšie ilustruje túto architektúru — jediný MCP klient (vaša AI aplikácia) sa pripája k viacerým MCP serverom, každý vystavuje vlastnú sadu nástrojov cez štandardný protokol:

<img src="../../../translated_images/sk/mcp-architecture.b3156d787a4ceac9.webp" alt="Architektúra MCP" width="800"/>

*Architektúra Protokolu kontextu modelu - štandardizované objavovanie a vykonávanie nástrojov*

## Ako MCP funguje

Pod kapotou MCP používa vrstvovú architektúru. Vaša Java aplikácia (MCP klient) zistí dostupné nástroje, posiela JSON-RPC požiadavky cez transportnú vrstvu (Stdio alebo HTTP) a MCP server vykonáva operácie a vracia výsledky. Nasledujúci diagram rozoberá každú vrstvu tohto protokolu:

<img src="../../../translated_images/sk/mcp-protocol-detail.01204e056f45308b.webp" alt="Detail protokolu MCP" width="800"/>

*Ako MCP funguje pod kapotou — klienti zisťujú nástroje, vymieňajú si JSON-RPC správy a vykonávajú operácie cez transportnú vrstvu.*

**Architektúra server-klient**

MCP používa model klient-server. Servery poskytujú nástroje - čítanie súborov, dopyty do databáz, volania API. Klienti (vaša AI aplikácia) sa pripájajú k serverom a používajú ich nástroje.

Na použitie MCP s LangChain4j pridajte tento závislostný modul do Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Objavovanie nástrojov**

Keď sa váš klient pripojí k MCP serveru, pýta sa "Aké nástroje máte?" Server odpovie zoznamom dostupných nástrojov, každý s popisom a parametrovou schémou. Váš AI agent potom môže rozhodnúť, ktoré nástroje použiť podľa požiadaviek používateľa. Diagram nižšie ukazuje tento handshake — klient posiela požiadavku `tools/list` a server vracia svoje dostupné nástroje s popismi a schémami parametrov:

<img src="../../../translated_images/sk/tool-discovery.07760a8a301a7832.webp" alt="Objavovanie nástrojov MCP" width="800"/>

*AI objavuje dostupné nástroje pri spustení — teraz vie, aké schopnosti sú dostupné a môže rozhodnúť, ktoré použiť.*

**Transportné mechanizmy**

MCP podporuje rôzne transportné mechanizmy. Dve možnosti sú Stdio (pre lokálnu komunikáciu s podprocesmi) a Streamable HTTP (pre vzdialené servery). Tento modul demonštruje transport Stdio:

<img src="../../../translated_images/sk/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transportné mechanizmy" width="800"/>

*Transportné mechanizmy MCP: HTTP pre vzdialené servery, Stdio pre lokálne procesy*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Pre lokálne procesy. Vaša aplikácia spustí server ako podproces a komunikuje cez štandardný vstup/výstup. Vhodné pre prístup k súborovému systému alebo príkazové nástroje.

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

Server `@modelcontextprotocol/server-filesystem` vystavuje nasledujúce nástroje, všetky ohraničené na adresáre, ktoré zadáte:

| Nástroj | Popis |
|------|-------------|
| `read_file` | Číta obsah jedného súboru |
| `read_multiple_files` | Číta viac súborov v jednom volaní |
| `write_file` | Vytvorí alebo prepíše súbor |
| `edit_file` | Vykoná cielené nájdenie a nahradenie |
| `list_directory` | Vypíše súbory a adresáre na ceste |
| `search_files` | Rekurzívne vyhľadá súbory podľa vzoru |
| `get_file_info` | Získa metadáta súboru (veľkosť, časové značky, povolenia) |
| `create_directory` | Vytvorí adresár (vrátane nadriadených adresárov) |
| `move_file` | Presunie alebo premenová súbor či adresár |

Nasledujúci diagram ukazuje, ako Stdio transport funguje za behu — vaša Java aplikácia spustí MCP server ako podriadený proces a komunikujú cez stdin/stdout potrubia, bez siete alebo HTTP:

<img src="../../../translated_images/sk/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Tok Stdio transportu" width="800"/>

*Stdio transport v akcii — vaša aplikácia spúšťa MCP server ako podproces a komunikuje cez stdin/stdout potrubia.*

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) a spýtajte sa:
> - "Ako funguje Stdio transport a kedy ho použiť namiesto HTTP?"
> - "Ako LangChain4j spravuje životný cyklus spustených MCP serverových procesov?"
> - "Aké sú bezpečnostné dôsledky poskytnutia AI prístupu k súborovému systému?"

## Agentický modul

Zatiaľ čo MCP poskytuje štandardizované nástroje, agentický modul LangChain4j poskytuje deklaratívny spôsob, ako stavať agentov, ktorí tieto nástroje zariaďujú. Anotácia `@Agent` a `AgenticServices` vám umožňujú definovať správanie agenta prostredníctvom rozhraní namiesto imperatívneho kódu.

V tomto module preskúmate vzor **Supervízneho agenta** — pokročilý agentický prístup AI, kde "supervízor" dynamicky rozhoduje, ktorých podagentov vyvolať podľa požiadaviek používateľa. Spojíme oba koncepty tým, že jednému z našich podagentov dáme schopnosti MCP-poháňaného prístupu k súborom.

Na použitie agentického modulu pridajte túto závislosť do Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Poznámka:** Modul `langchain4j-agentic` používa samostatnú vlastnosť verzie (`langchain4j.mcp.version`), pretože vychádza v inom časovom harmonograme než jadro LangChain4j knižníc.

> **⚠️ Experimentálne:** Modul `langchain4j-agentic` je **experimentálny** a môže sa meniť. Stabilným spôsobom budovania AI asistentov zostáva `langchain4j-core` s vlastnými nástrojmi (Modul 04).

## Spustenie príkladov

### Predpoklady

- Dokončený [Modul 04 - Nástroje](../04-tools/README.md) (tento modul nadväzuje na koncepty vlastných nástrojov a porovnáva ich s MCP nástrojmi)
- Súbor `.env` v koreňovom adresári s povoleniami Azure (vytvorený príkazom `azd up` v Module 01)
- Java 21+ a Maven 3.9+
- Node.js 16+ a npm (pre MCP servery)

> **Poznámka:** Ak ešte nemáte nastavené svoje premenné prostredia, pozrite [Modul 01 - Úvod](../01-introduction/README.md) pre inštrukcie nasadenia (`azd up` automaticky vytvorí súbor `.env`), alebo skopírujte `.env.example` do `.env` v koreňovom adresári a vyplňte svoje hodnoty.

## Rýchly štart

**Použitie VS Code:** Jednoducho kliknite pravým tlačidlom na akýkoľvek demo súbor v Exploreri a vyberte **"Run Java"**, alebo použite spúšťacie konfigurácie v paneli Run and Debug (predtým sa uistite, že váš `.env` súbor je správne nastavený s Azure povoleniami).

**Použitie Maven:** Alternatívne môžete spustiť z príkazového riadku pomocou nižšie uvedených príkladov.

### Súborové operácie (Stdio)

Toto demonštruje lokálne nástroje založené na podprocesoch.

**✅ Nie sú potrebné žiadne predpoklady** - MCP server sa spustí automaticky.

**Použitie štartovacích skriptov (odporúčané):**

Štartovacie skripty automaticky načítajú premenné prostredia zo súboru `.env` v koreňovom adresári:

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

**Použitie VS Code:** Kliknite pravým tlačidlom na `StdioTransportDemo.java` a vyberte **"Run Java"** (uistite sa, že váš `.env` je správne nakonfigurovaný).

Aplikácia automaticky spustí MCP server súborového systému a prečíta lokálny súbor. Všimnite si, ako je riadenie podprocesu za vás vyriešené.

**Očakávaný výstup:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervízny agent

**Vzor Supervízneho agenta** je **flexibilná** forma agentickej AI. Supervízor používa LLM na autonómne rozhodovanie, ktorých agentov vyvolať na základe požiadavky používateľa. V nasledujúcom príklade kombinujeme MCP-poháňaný prístup k súborom s LLM agentom, aby sme vytvorili riadený pracovný tok čítania súboru → zostavenia správy.

V dema `FileAgent` číta súbor pomocou MCP nástrojov súborového systému a `ReportAgent` generuje štruktúrovanú správu so zhrnutím pre vedenie (1 veta), 3 kľúčovými bodmi a odporúčaniami. Supervízor tento tok orchestruje automaticky:

<img src="../../../translated_images/sk/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Vzor supervízneho agenta" width="800"/>

*Supervízor používa svoje LLM na rozhodovanie, ktorých agentov vyvolať a v akom poradí — nie je potrebné žiadne pevné routovanie.*

Takto vyzerá konkrétny pracovný tok pre náš pipeline súbor → správa:

<img src="../../../translated_images/sk/file-report-workflow.649bb7a896800de9.webp" alt="Pracovný tok súbor - správa" width="800"/>

*FileAgent číta súbor cez MCP nástroje, potom ReportAgent transformuje surový obsah do štruktúrovanej správy.*

Nasledujúci sekvenčný diagram sleduje kompletnú orchestráciu Supervízora — od spustenia MCP servera, cez autonómny výber agentov Supervízorom, po volania nástrojov cez stdio a finálnu správu:

<img src="../../../translated_images/sk/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Sekvenčný diagram supervízneho agenta" width="800"/>

*Supervízor autonómne vyvoláva FileAgent (ktorý volá MCP server cez stdio na čítanie súboru), potom vyvolá ReportAgent na generovanie štruktúrovanej správy — každý agent ukladá svoj výstup do zdieľanej agentickej pamäte (Agentic Scope).*

Každý agent ukladá svoj výstup do **Agentic Scope** (zdieľanej pamäte), čo umožňuje nasledujúcim agentom pristupovať k predchádzajúcim výsledkom. Toto demonštruje, ako MCP nástroje hladko zapadajú do agentických pracovných tokov — Supervízor nemusí vedieť *ako* sa súbory čítajú, stačí mu vedieť, že to vie `FileAgent`.

#### Spustenie demo ukážky

Štartovacie skripty automaticky načítajú premenné prostredia zo súboru `.env` v koreňovom adresári:

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

**Použitie VS Code:** Kliknite pravým tlačidlom na `SupervisorAgentDemo.java` a vyberte **"Run Java"** (uistite sa, že váš `.env` je správne nakonfigurovaný).

#### Ako funguje supervízor

Predtým, než vytvoríte agentov, musíte pripojiť MCP transport ku klientovi a obaliť ho ako `ToolProvider`. Takto sa MCP nástroje servera sprístupnia vašim agentom:

```java
// Vytvorte MCP klienta z transportu
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Zabaliť klienta ako ToolProvider — toto prepája MCP nástroje s LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Teraz môžete vstreknúť `mcpToolProvider` do akéhokoľvek agenta, ktorý potrebuje MCP nástroje:

```java
// Krok 1: FileAgent číta súbory pomocou MCP nástrojov
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Má MCP nástroje na prácu so súbormi
        .build();

// Krok 2: ReportAgent generuje štruktúrované správy
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor riadi pracovný tok súbor → správa
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Vrátiť finálnu správu
        .build();

// Supervisor rozhoduje, ktorých agentov vyvolať na základe požiadavky
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Ako FileAgent zistí MCP nástroje za behu

Možno sa pýtate: **Ako `FileAgent` vie, ako používať npm nástroje súborového systému?** Odpoveď je, že nevie — **LLM** si to vyhodnotí za behu podľa schém nástrojov.

Rozhranie `FileAgent` je len **definícia promptu**. Nemá žiadne pevne zakódované znalosti o `read_file`, `list_directory` alebo inom MCP nástroji. Takto to funguje od začiatku do konca:
1. **Spustenie servera:** `StdioMcpTransport` spúšťa npm balíček `@modelcontextprotocol/server-filesystem` ako podriadený proces  
2. **Objavovanie nástrojov:** `McpClient` posiela serveru JSON-RPC požiadavku `tools/list`, ktorý odpovie názvami nástrojov, popismi a schémami parametrov (napr. `read_file` — *"Prečítať celý obsah súboru"* — `{ path: string }`)  
3. **Vkladanie schém:** `McpToolProvider` obalí tieto objavené schémy a sprístupní ich pre LangChain4j  
4. **Rozhodnutie LLM:** Keď sa zavolá `FileAgent.readFile(path)`, LangChain4j pošle systémovú správu, správu používateľa **a zoznam schém nástrojov** do LLM. LLM prečíta popisy nástrojov a vytvorí volanie nástroja (napr. `read_file(path="/some/file.txt")`)  
5. **Spustenie:** LangChain4j zachytí volanie nástroja, nasmeruje ho cez MCP klienta späť do podprocesu Node.js, získa výsledok a posiela ho späť do LLM  

Ide o ten istý mechanizmus [Objavovania nástrojov](../../../05-mcp) opísaný vyššie, ale aplikovaný špecificky na pracovný tok agenta. Anotácie `@SystemMessage` a `@UserMessage` riadia správanie LLM, zatiaľ čo vstreknutý `ToolProvider` mu poskytuje **schopnosti** — LLM prepája tieto dve časti za behu.

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) a spýtajte sa:  
> - "Ako tento agent vie, ktorý MCP nástroj zavolať?"  
> - "Čo by sa stalo, keby som z buildera agenta odstránil ToolProvider?"  
> - "Ako sa schémy nástrojov posielajú do LLM?"

#### Stratégie odpovedí

Keď konfigurujete `SupervisorAgent`, určujete, ako by mal formulovať svoju finálnu odpoveď používateľovi po dokončení úloh podagentov. Nasledujúci diagram ukazuje tri dostupné stratégie — LAST vráti priamo výstup posledného agenta, SUMMARY zosumarizuje všetky výstupy cez LLM a SCORED vyberie ten, ktorý má vyššie skóre podľa pôvodnej požiadavky:

<img src="../../../translated_images/sk/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Tri stratégie pre formuláciu finálnej odpovede Supervisora — vyberte podľa toho, či chcete posledný výstup agenta, syntetizované zhrnutie alebo najlepšie ohodnotenú možnosť.*

Dostupné stratégie sú:

| Stratégia | Popis |
|----------|-------------|
| **LAST** | Supervisor vracia výstup posledného zavolaného podagenta alebo nástroja. Je to vhodné, keď posledný agent vo workflow je navrhnutý na vytvorenie úplnej konečnej odpovede (napr. "Summarizujúci agent" v výskumnej pipeline). |  
| **SUMMARY** | Supervisor použije vlastný interný jazykový model (LLM) na syntézu zhrnutia celej interakcie a všetkých výstupov podagentov a toto zhrnutie potom vráti ako finálnu odpoveď. Poskytuje čistú, agregovanú odpoveď používateľovi. |  
| **SCORED** | Systém použije interný LLM na ohodnotenie odpovede LAST a SUMMARY voči pôvodnej požiadavke používateľa a vráti ten výstup, ktorý má vyššie skóre. |  

Pozrite si kompletnú implementáciu v [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java).

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) a spýtajte sa:  
> - "Ako Supervisor rozhoduje, ktorých agentov zavolať?"  
> - "Aký je rozdiel medzi vzormi Supervisora a Sekvenčného workflow?"  
> - "Ako môžem prispôsobiť plánovacie správanie Supervisora?"

#### Pochopenie výstupu

Keď spustíte demo, uvidíte štruktúrovaný prehľad, ako Supervisor koordinuje viacerých agentov. Tu je význam jednotlivých častí:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Hlavička** predstavuje koncept workflow: zameraná pipeline od čítania súboru po generovanie reportu.  

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
- **ReportAgent** používa tento obsah a vytvára štruktúrovaný report v `report`  

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Požiadavka používateľa** zobrazuje zadanú úlohu. Supervisor ju analyzuje a rozhodne sa zavolať FileAgent → ReportAgent.  

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
  
**Orchestrácia Supervisora** ukazuje 2-krokový priebeh v praxi:  
1. **FileAgent** prečíta súbor cez MCP a uloží obsah  
2. **ReportAgent** dostane obsah a vygeneruje štruktúrovaný report  

Supervisor urobil tieto rozhodnutia **autonómne** na základe používateľovej požiadavky.  

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
  
#### Vysvetlenie funkcií agentickej modulu

Príklad demonštruje niekoľko pokročilých funkcií agentického modulu. Pozrime sa bližšie na Agentický Scope a Agentické Listeneri.

**Agentický Scope** ukazuje zdieľanú pamäť, kde agenti ukladali svoje výsledky pomocou `@Agent(outputKey="...")`. To umožňuje:  
- Neskorším agentom pristupovať k výstupom predchádzajúcich agentov  
- Supervisorovi zosumarizovať finálnu odpoveď  
- Vám skontrolovať, čo ktorý agent vyprodukoval  

Diagram nižšie ukazuje, ako Agentický Scope funguje ako zdieľaná pamäť v pracovnom toku súbor → report — FileAgent zapíše výstup pod kľúč `fileContent`, ReportAgent ho prečíta a zapíše svoj výstup pod `report`:

<img src="../../../translated_images/sk/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentický Scope funguje ako zdieľaná pamäť — FileAgent uloží `fileContent`, ReportAgent ho prečíta a zapíše `report`, a váš kód číta finálny výsledok.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Nezriedené dáta súboru z FileAgent
String report = scope.readState("report");            // Štruktúrovaná správa z ReportAgent
```
  
**Agentickí Listeneri** umožňujú monitorovanie a ladenie behu agentov. Krok po kroku výstup v demu pochádza z AgentListenera, ktorý sa pripája ku každej výzve agenta:  
- **beforeAgentInvocation** - Zavolaný, keď Supervisor vyberá agenta, ukazuje, ktorý agent bol vybraný a prečo  
- **afterAgentInvocation** - Zavolaný po dokončení agenta, zobrazuje jeho výsledok  
- **inheritedBySubagents** - Keď je true, listener sleduje všetkých agentov v hierarchii  

Nasledujúci diagram ukazuje celý životný cyklus Agent Listeneru, vrátane spracovania chýb `onError`, ktoré sa objavia počas behu agenta:

<img src="../../../translated_images/sk/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agentickí Listeneri sa pripájajú k životnému cyklu vykonávania — monitorujú štart, dokončenie alebo chyby agentov.*

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
        return true; // Šíriť na všetkých podagentov
    }
};
```
  
Okrem vzoru Supervisora poskytuje modul `langchain4j-agentic` niekoľko silných vzorov workflow. Nižšie sú všetky päť — od jednoduchých sekvenčných pipeline po schvaľovacie workflow s človekom v slučke:

<img src="../../../translated_images/sk/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Päť vzorov workflow na orchestráciu agentov — od jednoduchých sekvenčných pipeline po schvaľovacie workflow s človekom v slučke.*

| Vzor | Popis | Použitie |
|---------|-------------|----------|
| **Sekvenčný** | Spustenie agentov v poradí, výstup tečie ďalej | Pipeline: výskum → analýza → report |
| **Paralelný** | Spustenie agentov súčasne | Nezávislé úlohy: počasie + správy + akcie |
| **Slučka** | Iterovanie, kým sa nesplní podmienka | Hodnotenie kvality: dolaďovanie, kým skóre ≥ 0,8 |
| **Podmienený** | Smerovanie podľa podmienok | Klasifikácia → pridelenie špecialistovi |
| **Človek v slučke** | Pridanie ľudských kontrol | Schvaľovacie workflow, kontrola obsahu |

## Kľúčové koncepty

Keď ste už preskúmali MCP a agentický modul v praxi, zhrnajme, kedy použiť ktorý prístup.

Jednou z najväčších výhod MCP je jeho rastúca ekosystémová podpora. Diagram nižšie ukazuje, ako univerzálny protokol prepája vašu AI aplikáciu s množstvom MCP serverov — od súborových systémov a databáz po GitHub, email, web scraping a ďalšie:

<img src="../../../translated_images/sk/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP tvorí univerzálny protokolový ekosystém — každý MCP-kompatibilný server funguje s akýmkoľvek MCP-kompatibilným klientom, čo umožňuje zdieľanie nástrojov medzi aplikáciami.*

**MCP** je ideálny, keď chcete využiť existujúci ekosystém nástrojov, vytvárať nástroje, ktoré môžu používať viaceré aplikácie, integrovať služby tretích strán štandardnými protokolmi alebo meniť implementácie nástrojov bez zásahu do kódu.

**Agentický modul** najlepšie funguje, keď chcete deklaratívne definície agentov s anotáciami `@Agent`, potrebujete orchestráciu workflow (sekvenčné, slučka, paralelné), uprednostňujete návrh agentov cez rozhrania pred imperatívnym kódom alebo kombinujete viacerých agentov, ktorí si zdieľajú výstupy cez `outputKey`.

**Vzor Supervisor Agent** vyniká, keď workflow nie je vopred predvídateľný a chcete, aby rozhodoval LLM, keď máte viacerých špecializovaných agentov vyžadujúcich dynamickú orchestráciu, pri budovaní konverzačných systémov smerujúcich na rôzne schopnosti alebo keď chcete čo najflexibilnejšie a adaptívne správanie agentov.

Na pomoc pri rozhodovaní medzi vlastnými metódami `@Tool` z Modulu 04 a MCP nástrojmi z tohto modulu výhody a nevýhody sú zhrnuté v porovnaní nižšie — vlastné nástroje vám dajú úzke prepojenie a plnú typovú bezpečnosť pre aplikačnú logiku, zatiaľ čo MCP nástroje ponúkajú štandardizované, znovu použiteľné integrácie:

<img src="../../../translated_images/sk/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Kedy používať vlastné metódy @Tool vs MCP nástroje — vlastné nástroje pre aplikačnú logiku s plnou typovou bezpečnosťou, MCP nástroje pre štandardizované integrácie naprieč aplikáciami.*

## Gratulujeme!

Prešli ste všetkými piatimi modulmi kurzu LangChain4j pre začiatočníkov! Tu je pohľad na celú naučenú cestu — od základného chatu až po agentické systémy poháňané MCP:

<img src="../../../translated_images/sk/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Vaša cesta učením cez všetkých päť modulov — od základného chatu po agentické systémy poháňané MCP.*

Ukázali ste sa schopnými:  

- Ako vytvárať konverzačnú AI s pamäťou (Modul 01)  
- Vzory prompt engineering pre rôzne úlohy (Modul 02)  
- Zakladanie odpovedí na dokumentoch pomocou RAG (Modul 03)  
- Tvorbu základných AI agentov (asistentov) s vlastnými nástrojmi (Modul 04)  
- Integráciu štandardizovaných nástrojov s LangChain4j MCP a agentickými modulmi (Modul 05)  

### Čo ďalej?

Po dokončení modulov skúmajte [Testing Guide](../docs/TESTING.md), aby ste videli koncepty testovania LangChain4j v praxi.

**Oficiálne zdroje:**  
- [Dokumentácia LangChain4j](https://docs.langchain4j.dev/) - komplexné návody a API referencie  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - zdrojový kód a príklady  
- [LangChain4j Tutoriály](https://docs.langchain4j.dev/tutorials/) - krok za krokom tutoriály pre rôzne použitia  

Ďakujeme, že ste dokončili tento kurz!

---

**Navigácia:** [← Predchádzajúci: Modul 04 - Nástroje](../04-tools/README.md) | [Späť na Hlavnú stránku](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vyhlásenie o zodpovednosti**:  
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, majte prosím na pamäti, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho pôvodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre dôležité informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za akékoľvek nedorozumenia alebo nesprávne interpretácie vzniknuté použitím tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->