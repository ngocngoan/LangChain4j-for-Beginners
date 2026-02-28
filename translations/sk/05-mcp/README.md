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
  - [Supervízny agent](../../../05-mcp)
    - [Spustenie demo](../../../05-mcp)
    - [Ako funguje Supervízor](../../../05-mcp)
    - [Strategie reakcií](../../../05-mcp)
    - [Pochopenie výstupu](../../../05-mcp)
    - [Vysvetlenie funkcií agentického modulu](../../../05-mcp)
- [Kľúčové koncepty](../../../05-mcp)
- [Gratulujeme!](../../../05-mcp)
  - [Čo bude ďalej?](../../../05-mcp)

## Čo sa naučíte

Vybudovali ste konverzačné AI, ovládli ste promptovanie, zakotvili odpovede v dokumentoch a vytvorili agentov s nástrojmi. Ale všetky tie nástroje boli vytvorené na mieru pre vašu špecifickú aplikáciu. Čo ak by ste svojmu AI mohli dať prístup k štandardizovanému ekosystému nástrojov, ktoré môže ktokoľvek vytvárať a zdieľať? V tomto module sa naučíte, ako to dosiahnuť pomocou protokolu Model Context Protocol (MCP) a agentického modulu LangChain4j. Najskôr ukážeme jednoduchého MCP čitateľa súborov a potom, ako sa ľahko integruje do pokročilých agentických workflowov pomocou vzoru Supervízny agent.

## Čo je MCP?

Model Context Protocol (MCP) poskytuje presne to – štandardný spôsob, ako môžu AI aplikácie objavovať a využívať externé nástroje. Namiesto písania vlastných integrácií pre každý zdroj dát alebo službu sa pripájate na MCP servery, ktoré vystavujú svoje schopnosti v konzistentnom formáte. Váš AI agent potom môže tieto nástroje automaticky objavovať a používať.

<img src="../../../translated_images/sk/mcp-comparison.9129a881ecf10ff5.webp" alt="Porovnanie MCP" width="800"/>

*Pred MCP: zložité integrácie bod-bod. Po MCP: jeden protokol, nekonečné možnosti.*

MCP rieši základný problém vo vývoji AI: každá integrácia je na mieru. Chcete pristupovať na GitHub? Vlastný kód. Čítať súbory? Vlastný kód. Klásť dotazy do databázy? Vlastný kód. A žiadna z týchto integrácií nefunguje s inými AI aplikáciami.

MCP to štandardizuje. MCP server vystavuje nástroje s jasnými popismi a schémami. Akýkoľvek MCP klient sa môže pripojiť, objaviť dostupné nástroje a používať ich. Napíš raz, používaj všade.

<img src="../../../translated_images/sk/mcp-architecture.b3156d787a4ceac9.webp" alt="Architektúra MCP" width="800"/>

*Architektúra Model Context Protocol – štandardizované objavovanie a vykonávanie nástrojov*

## Ako MCP funguje

<img src="../../../translated_images/sk/mcp-protocol-detail.01204e056f45308b.webp" alt="Detail protokolu MCP" width="800"/>

*Ako MCP funguje pod kapotou — klienti objavujú nástroje, vymieňajú si správy JSON-RPC a vykonávajú operácie cez transportnú vrstvu.*

**Architektúra Server-Klient**

MCP používa model klient-server. Servery poskytujú nástroje - čítanie súborov, dotazovanie databáz, volania API. Klienti (vaša AI aplikácia) sa pripájajú k serverom a používajú ich nástroje.

Ak chcete používať MCP s LangChain4j, pridajte túto Maven závislosť:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Objavovanie nástrojov**

Keď sa váš klient pripojí k MCP serveru, opýta sa "Aké nástroje máte?" Server odpovie zoznamom dostupných nástrojov, každý s popismi a schémami parametrov. Váš AI agent potom môže rozhodnúť, ktoré nástroje použiť na základe požiadaviek používateľa.

<img src="../../../translated_images/sk/tool-discovery.07760a8a301a7832.webp" alt="Objavovanie nástrojov MCP" width="800"/>

*AI objavuje dostupné nástroje pri štarte — teraz vie, čo môže, a môže rozhodnúť, ktoré z nich použiť.*

**Transportné mechanizmy**

MCP podporuje rôzne transportné mechanizmy. Tento modul demonštruje Stdio transport pre lokálne procesy:

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

<img src="../../../translated_images/sk/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Tok Stdio transportu" width="800"/>

*Stdio transport v akcii — vaša aplikácia spustí MCP server ako detský proces a komunikuje cez pipy stdin/stdout.*

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) a spýtajte sa:
> - "Ako funguje Stdio transport a kedy by som ho mal používať oproti HTTP?"
> - "Ako LangChain4j spravuje životný cyklus spustených MCP serverových procesov?"
> - "Aké sú bezpečnostné dôsledky udelenia AI prístupu k súborovému systému?"

## Agentický modul

Kým MCP poskytuje štandardizované nástroje, LangChain4j **agentický modul** ponúka deklaratívny spôsob, ako tvoriť agentov, ktorí tieto nástroje koordinujú. Anotácia `@Agent` a `AgenticServices` vám umožňujú definovať správanie agenta cez rozhrania namiesto imperatívneho kódu.

V tomto module preskúmate vzor **Supervízny agent** — pokročilý agentický AI prístup, kde „supervízor“ dynamicky rozhoduje, ktorých podagentov vyvolať podľa požiadaviek používateľa. Kombinujeme oba koncepty tak, že jednému z podagentov dáme možnosti prístupu k súborom cez MCP.

Ak chcete používať agentický modul, pridajte túto Maven závislosť:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **⚠️ Experimentálne:** Modul `langchain4j-agentic` je **experimentálny** a môže sa meniť. Stabilný spôsob tvorby AI asistentov zostáva `langchain4j-core` s vlastnými nástrojmi (Modul 04).

## Spustenie príkladov

### Požiadavky

- Java 21+, Maven 3.9+
- Node.js 16+ a npm (pre MCP servery)
- Prostredné premenné nakonfigurované v `.env` súbore (v koreňovom adresári):
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (rovnako ako v moduloch 01-04)

> **Poznámka:** Ak ste ešte nenakonfigurovali svoje environmentálne premenné, pozrite si [Modul 00 - Rýchly štart](../00-quick-start/README.md) pre inštrukcie alebo skopírujte `.env.example` do `.env` v koreňovom adresári a doplňte svoje hodnoty.

## Rýchly štart

**Použitie VS Code:** Jednoducho kliknite pravým tlačidlom na ľubovoľný demo súbor v Prieskumníkovi a vyberte **„Run Java“**, alebo použite launch konfigurácie z panelu Spustenie a ladenie (uistite sa, že ste najskôr pridali svoj token do súboru `.env`).

**Použitie Maven:** Alternatívne môžete spustiť z príkazového riadku nasledovné príklady.

### Operácie so súbormi (Stdio)

Toto demonštruje nástroje založené na lokálnych podprocesoch.

**✅ Nepotrebujete žiadne predpoklady** - MCP server sa spustí automaticky.

**Použitie spúšťacích skriptov (odporúčané):**

Spúšťacie skripty automaticky načítajú premenné prostredia z koreňového súboru `.env`:

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

**Použitie VS Code:** Kliknite pravým tlačidlom na `StdioTransportDemo.java` a vyberte **„Run Java“** (uistite sa, že váš `.env` súbor je nakonfigurovaný).

Aplikácia automaticky spustí MCP server súborového systému a prečíta lokálny súbor. Všímajte si, ako je za vás spravované spúšťanie podprocesu.

**Očakávaný výstup:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervízny agent

Vzor **Supervízny agent** je **flexibilná** forma agentického AI. Supervízor používa LLM, aby autonómne rozhodol, ktorých agentov vyvolať podľa požiadavky používateľa. V ďalšom príklade kombinujeme MCP-ovo napájaný prístup k súborom s LLM agentom, aby sme vytvorili workflow čítania súboru → generovania reportu pod dohľadom.

V deme `FileAgent` číta súbor použitím MCP nástrojov súborového systému a `ReportAgent` generuje štruktúrovanú správu s výkonným zhrnutím (1 veta), 3 kľúčovými bodmi a odporúčaniami. Supervízor tento tok automaticky koordinuje:

<img src="../../../translated_images/sk/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Vzor Supervízny agent" width="800"/>

*Supervízor používa svoj LLM na rozhodnutie, ktorých agentov vyvolá a v akom poradí — nie je potrebné pevné smerovanie.*

Takto vyzerá konkrétny workflow nášho potrubia „zo súboru do správy“:

<img src="../../../translated_images/sk/file-report-workflow.649bb7a896800de9.webp" alt="Workflow zo súboru do správy" width="800"/>

*FileAgent číta súbor pomocou MCP nástrojov, potom ReportAgent transformuje surový obsah na štruktúrovaný report.*

Každý agent ukladá svoj výstup do **Agentického Priestoru** (zdieľaná pamäť), čo umožňuje nasledujúcim agentom pristupovať k predchádzajúcim výsledkom. To demonštruje, ako sa MCP nástroje hladko integrujú do agentických workflowov — Supervízor nemusí vedieť *ako* sa súbory čítajú, len že to zvládne `FileAgent`.

#### Spustenie demo

Spúšťacie skripty automaticky načítajú premenné prostredia z koreňového `.env` súboru:

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

**Použitie VS Code:** Kliknite pravým na `SupervisorAgentDemo.java` a vyberte **„Run Java“** (uistite sa, že váš `.env` súbor je nakonfigurovaný).

#### Ako funguje Supervízor

```java
// Krok 1: FileAgent číta súbory pomocou MCP nástrojov
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Má MCP nástroje na manipuláciu so súbormi
        .build();

// Krok 2: ReportAgent generuje štruktúrované správy
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor koordinuje workflow súbor → správa
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Vrátiť finálnu správu
        .build();

// Supervisor rozhoduje, ktorých agentov vyvolať na základe požiadavky
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Strategie reakcií

Keď konfiguruješ `SupervisorAgent`, určuješ, ako by mal formulovať svoju finálnu odpoveď používateľovi po dokončení úloh pod-agentov.

<img src="../../../translated_images/sk/response-strategies.3d0cea19d096bdf9.webp" alt="Strategie reakcií" width="800"/>

*Tri stratégie, ako Supervízor formuluje svoju odpoveď — vyber podľa toho, či chceš výstup posledného agenta, zhrnutie alebo najlepšiu zodpovedajúcu možnosť.*

Dostupné stratégie sú:

| Stratégia | Popis |
|----------|-------------|
| **LAST** | Supervízor vráti výstup posledného pod-agenta alebo nástroja, ktorý bol volaný. Toto je užitočné, keď je posledný agent vo workflow špeciálne navrhnutý na vytvorenie kompletného, finálneho odpovede (napr. „Agent zhrnutia“ v pipeline výskumu). |
| **SUMMARY** | Supervízor použije svoj vlastný interný jazykový model (LLM) na zhrnutie celej interakcie a všetkých výstupov pod-agentov, potom toto zhrnutie vráti ako finálnu odpoveď. Poskytuje čistú, agregovanú odpoveď používateľovi. |
| **SCORED** | Systém použije interný LLM na ohodnotenie ako LAST odpovede, tak SUMMARY interakcie voči pôvodnej požiadavke používateľa a vráti ten výstup, ktorý má vyššie skóre. |

Pozrite si [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) pre kompletnú implementáciu.

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) a spýtajte sa:
> - "Ako sa Supervízor rozhoduje, ktorých agentov vyvolať?"
> - "Aký je rozdiel medzi Supervíznym a Sekvenčným workflow vzorom?"
> - "Ako môžem prispôsobiť plánovacie správanie Supervízora?"

#### Pochopenie výstupu

Keď spustíte demo, uvidíte štruktúrovaný priebeh, ako Supervízor koordinuje viacerých agentov. Toto znamená každý oddiel:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Hlavička** predstavuje koncept workflow: zamerané potrubie od čítania súboru po generovanie správy.

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

**Diagram workflow** ukazuje tok dát medzi agentmi. Každý agent má konkrétnu úlohu:
- **FileAgent** číta súbory cez MCP nástroje a ukladá surový obsah do `fileContent`
- **ReportAgent** spotrebováva tento obsah a produkuje štruktúrovanú správu v `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Požiadavka používateľa** zobrazuje úlohu. Supervízor ju analyzuje a rozhodne sa vyvolať FileAgent → ReportAgent.

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

**Orchestrace Supervízora** ukazuje 2-krokový tok v akcii:
1. **FileAgent** číta súbor cez MCP a ukladá obsah
2. **ReportAgent** prijíma obsah a generuje štruktúrovaný report

Supervízor urobil tieto rozhodnutia **autonómne** na základe požiadavky používateľa.

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

#### Vysvetlenie funkcií agentického modulu

Príklad demonštruje niekoľko pokročilých funkcií agentického modulu. Pozrime sa bližšie na Agentický Priestor a Agent Listenerov.

**Agentický Priestor** ukazuje zdieľanú pamäť, kam agenti uložili svoje výsledky pomocou `@Agent(outputKey="...")`. Toto umožňuje:
- Neskorším agentom pristupovať k výstupom predchádzajúcich agentov
- Supervízorovi syntetizovať finálnu odpoveď
- Vám skúmať, čo každý agent vyprodukoval

<img src="../../../translated_images/sk/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentický Priestor Zdieľaná Pamäť" width="800"/>

*Agentický Priestor slúži ako zdieľaná pamäť — FileAgent zapisuje `fileContent`, ReportAgent ho číta a zapisuje `report` a váš kód číta finálny výsledok.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Neformátované údaje súboru z FileAgent
String report = scope.readState("report");            // Štruktúrovaná správa z ReportAgent
```

**Agent Listeneri** umožňujú monitorovanie a ladenie vykonávania agentov. Krok za krokom výstup, ktorý vidíte v deme, prichádza z AgentListenera, ktorý je napojený na každé vyvolanie agenta:
- **beforeAgentInvocation** - Volané, keď dozorca vyberie agenta, čo vám umožní vidieť, ktorý agent bol zvolený a prečo
- **afterAgentInvocation** - Volané, keď agent dokončí svoju činnosť, zobrazuje jeho výsledok
- **inheritedBySubagents** - Ak je pravda, poslucháč sleduje všetkých agentov v hierarchii

<img src="../../../translated_images/sk/agent-listeners.784bfc403c80ea13.webp" alt="Životný cyklus poslucháčov agentov" width="800"/>

*Poslucháči agentov sa pripájajú k životnému cyklu vykonávania — sledujú, kedy agenti začínajú, dokončujú alebo sa stretnú s chybami.*

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

Okrem vzoru dozorcu poskytuje modul `langchain4j-agentic` niekoľko výkonných vzorov pracovných tokov a funkcií:

<img src="../../../translated_images/sk/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Vzor pracovných tokov agentov" width="800"/>

*Päť vzorov pracovných tokov pre organizovanie agentov — od jednoduchých sekvenčných liniek až po schvaľovacie pracovné toky s človekom v slučke.*

| Vzor | Popis | Použitie |
|---------|-------------|----------|
| **Sekvenčný** | Vykonáva agentov po poriadku, výstup preteká na ďalšieho | Linky: výskum → analýza → report |
| **Paralelný** | Spúšťa agentov súčasne | Nezávislé úlohy: počasie + správy + akcie |
| **Slučka** | Opakuje, kým nie je splnená podmienka | Hodnotenie kvality: upravovať, kým skóre ≥ 0.8 |
| **Podmienený** | Smeruje podľa podmienok | Klasifikovať → smerovať na špecialistu |
| **Človek v slučke** | Pridáva kontrolné body človeka | Schvaľovacie procesy, kontrola obsahu |

## Kľúčové koncepty

Teraz, keď ste preskúmali MCP a modul agentic v praxi, zhrňme, kedy použiť ktorý prístup.

<img src="../../../translated_images/sk/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="Ekosystém MCP" width="800"/>

*MCP vytvára univerzálny protokolový ekosystém — akýkoľvek server kompatibilný s MCP funguje s akýmkoľvek klientom kompatibilným s MCP, čo umožňuje zdieľanie nástrojov naprieč aplikáciami.*

**MCP** je ideálny, keď chcete využiť existujúce ekosystémy nástrojov, vytvárať nástroje, ktoré môžu zdieľať viaceré aplikácie, integrovať služby tretích strán so štandardnými protokolmi, alebo zamieňať implementácie nástrojov bez zmeny kódu.

**Modul Agentic** je najvhodnejší, keď chcete deklaratívne definície agentov s anotáciami `@Agent`, potrebujete orchestráciu pracovných tokov (sekvenčná, slučka, paralelná), preferujete návrh agentov založený na rozhraniach pred imperatívnym kódom, alebo kombinujete viacerých agentov, ktorí zdieľajú výstupy cez `outputKey`.

**Vzor Dozorca agenta** je najlepší, keď pracovný tok nie je vopred predvídateľný a chcete, aby rozhodoval LLM, keď máte viacero špecializovaných agentov potrebujúcich dynamickú orchestráciu, pri budovaní konverzačných systémov smerujúcich na rôzne schopnosti, alebo keď chcete najflexibilnejšie a adaptívne správanie agenta.

<img src="../../../translated_images/sk/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Vlastné nástroje vs MCP nástroje" width="800"/>

*Kedy použiť vlastné metódy @Tool vs MCP nástroje — vlastné nástroje pre aplikačnú logiku s plnou typovou bezpečnosťou, MCP nástroje pre štandardizované integrácie fungujúce naprieč aplikáciami.*

## Gratulujeme!

<img src="../../../translated_images/sk/course-completion.48cd201f60ac7570.webp" alt="Dokončenie kurzu" width="800"/>

*Vaša vzdelávacia cesta cez všetkých päť modulov — od základného chatu až po systémy agentic poháňané MCP.*

Dokončili ste kurz LangChain4j pre začiatočníkov. Naučili ste sa:

- Ako vytvoriť konverzačné AI s pamäťou (Modul 01)
- Vzory prompt engineering pre rôzne úlohy (Modul 02)
- Zakladanie odpovedí na vašich dokumentoch pomocou RAG (Modul 03)
- Vytváranie základných AI agentov (asistentov) s vlastnými nástrojmi (Modul 04)
- Integráciu štandardizovaných nástrojov s modulmi LangChain4j MCP a Agentic (Modul 05)

### Čo ďalej?

Po dokončení modulov preskúmajte [Testing Guide](../docs/TESTING.md), aby ste videli koncepty testovania LangChain4j v praxi.

**Oficiálne zdroje:**
- [LangChain4j Dokumentácia](https://docs.langchain4j.dev/) - Komplexné návody a API referencie
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Zdrojový kód a príklady
- [LangChain4j Tutoriály](https://docs.langchain4j.dev/tutorials/) - Krok za krokom tutoriály pre rôzne použitia

Ďakujeme, že ste dokončili tento kurz!

---

**Navigácia:** [← Predchádzajúci: Modul 04 - Nástroje](../04-tools/README.md) | [Späť na hlavný](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vyhlásenie o zodpovednosti**:
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, majte prosím na pamäti, že automatizované preklady môžu obsahovať chyby alebo nepresnosti. Originálny dokument v jeho pôvodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre dôležité informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za žiadne nedorozumenia alebo nesprávne výklady vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->