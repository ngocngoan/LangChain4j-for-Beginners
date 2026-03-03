# Modul 05: Protokol konteksta modela (MCP)

## Kazalo

- [Kaj se boste naučili](../../../05-mcp)
- [Kaj je MCP?](../../../05-mcp)
- [Kako MCP deluje](../../../05-mcp)
- [Agentni modul](../../../05-mcp)
- [Zagon primerov](../../../05-mcp)
  - [Predpogoji](../../../05-mcp)
- [Hiter začetek](../../../05-mcp)
  - [Operacije z datotekami (Stdio)](../../../05-mcp)
  - [Nadzorni agent](../../../05-mcp)
    - [Zagon demonstracije](../../../05-mcp)
    - [Kako nadzornik deluje](../../../05-mcp)
    - [Strategije odziva](../../../05-mcp)
    - [Razumevanje izhoda](../../../05-mcp)
    - [Pojasnilo funkcij agentnega modula](../../../05-mcp)
- [Ključni koncepti](../../../05-mcp)
- [Čestitke!](../../../05-mcp)
  - [Kaj sledi?](../../../05-mcp)

## Kaj se boste naučili

Zgradili ste pogovorno umetno inteligenco, obvladali ukaze, povezali odgovore z dokumenti in ustvarili agente z orodji. Toda vsa ta orodja so bila posebej izdelana za vašo aplikacijo. Kaj pa, če bi lahko svoji AI omogočili dostop do standardiziranega ekosistema orodij, ki jih lahko kdorkoli ustvari in deli? V tem modulu se boste naučili prav tega z Model Context Protocol (MCP) in agentnim modulom LangChain4j. Najprej prikažemo preprost MCP bralnik datotek, nato pa, kako se zlahka integrira v napredne agentne delovne tokove z vzorcem Nadzornega agenta.

## Kaj je MCP?

Protokol konteksta modela (MCP) zagotavlja prav to – standardiziran način za AI aplikacije, da odkrijejo in uporabljajo zunanja orodja. Namesto pisanja posebnih integracij za vsak vir podatkov ali storitev se povežete s strežniki MCP, ki razkrivajo svoje zmogljivosti v dosledni obliki. Vaš AI agent lahko nato te funkcije samodejno odkrije in uporablja.

Spodnja shema prikazuje razliko — brez MCP vsaka integracija zahteva posebno povezavo ena na ena; z MCP pa en sam protokol povezuje vašo aplikacijo z vsakim orodjem:

<img src="../../../translated_images/sl/mcp-comparison.9129a881ecf10ff5.webp" alt="Primerjava MCP" width="800"/>

*Pred MCP: zapletene povezave ena na ena. Po MCP: en protokol, neskončne možnosti.*

MCP rešuje temeljni problem razvoja AI: vsaka integracija je po meri. Želite dostop do GitHuba? Posebna koda. Želite brati datoteke? Posebna koda. Želite poizvedovati bazo podatkov? Posebna koda. Te integracije pa ne delujejo z drugimi AI aplikacijami.

MCP to standardizira. MCP strežnik predstavlja orodja z jasno opisanim vmesnikom in shemami. Vsak MCP odjemalec se lahko poveže, odkrije na voljo orodja in jih uporablja. Zgradiš enkrat, uporabljaš kjerkoli.

Spodnja shema prikazuje to arhitekturo — en MCP odjemalec (vaša AI aplikacija) se povezuje z več MCP strežniki, od katerih vsak predstavlja svoj nabor orodij prek standardnega protokola:

<img src="../../../translated_images/sl/mcp-architecture.b3156d787a4ceac9.webp" alt="Arhitektura MCP" width="800"/>

*Arhitektura protokola konteksta modela - standardizirano odkrivanje in izvajanje orodij*

## Kako MCP deluje

"Pod pokrovom" MCP uporablja slojevno arhitekturo. Vaša Java aplikacija (MCP odjemalec) odkrije na voljo orodja, pošlje JSON-RPC zahtevke prek transportnega sloja (Stdio ali HTTP), MCP strežnik pa izvede operacije in vrne rezultate. Spodnja shema razčleni vsak sloj tega protokola:

<img src="../../../translated_images/sl/mcp-protocol-detail.01204e056f45308b.webp" alt="Podrobnosti protokola MCP" width="800"/>

*Kako MCP deluje v ozadju — odjemalci odkrijejo orodja, izmenjujejo JSON-RPC sporočila in izvajajo operacije prek transportnega sloja.*

**Arhitektura strežnik-odjemalec**

MCP uporablja model strežnik-odjemalec. Strežniki nudijo orodja – branje datotek, poizvedbe baz, klici API. Odjemalci (vaša AI aplikacija) se povežejo s strežniki in uporabljajo njihova orodja.

Za uporabo MCP z LangChain4j dodajte naslednjo Maven odvisnost:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Odkritje orodij**

Ko se vaš odjemalec poveže z MCP strežnikom, vpraša "Katera orodja imate?" Strežnik odgovori s seznamom na voljo orodij, vsakim z opisom in parametričnimi shemami. Vaš AI agent nato lahko odloči, katera orodja uporabiti glede na zahteve uporabnika. Spodnja shema prikazuje ta pozdravni protokol — odjemalec pošlje zahtevek `tools/list`, strežnik pa vrne svoja orodja z opisi in parametričnimi shemami:

<img src="../../../translated_images/sl/tool-discovery.07760a8a301a7832.webp" alt="Odkritje orodij MCP" width="800"/>

*AI odkrije razpoložljiva orodja ob zagonu — zdaj ve, katere zmogljivosti so na voljo in se lahko odloči, katera uporabiti.*

**Transportni mehanizmi**

MCP podpira različne transportne mehanizme. Dve možnosti sta Stdio (za lokalno komunikacijo s podprocesi) in Streamable HTTP (za oddaljene strežnike). Ta modul prikazuje Stdio transport:

<img src="../../../translated_images/sl/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transportni mehanizmi" width="800"/>

*Transportni mehanizmi MCP: HTTP za oddaljene strežnike, Stdio za lokalne procese*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Za lokalne procese. Vaša aplikacija zažene strežnik kot podproces in komunicira preko standardnega vhoda/izhoda. Uporabno za dostop do datotečnega sistema ali orodij ukazne vrstice.

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

Strežnik `@modelcontextprotocol/server-filesystem` razkriva naslednja orodja, vsa omejena na imenike, ki jih določite:

| Orodje | Opis |
|--------|-------|
| `read_file` | Prebere vsebino ene same datoteke |
| `read_multiple_files` | Prebere več datotek v enem klicu |
| `write_file` | Ustvari ali prepiše datoteko |
| `edit_file` | Izvede ciljno iskanje in zamenjavo |
| `list_directory` | Naredi seznam datotek in imenikov na poti |
| `search_files` | Rekurzivno išče datoteke, ki ustrezajo vzorcu |
| `get_file_info` | Pridobi metapodatke datoteke (velikost, časovne žige, dovoljenja) |
| `create_directory` | Ustvari imenik (vključno z nadrejenimi imeniki) |
| `move_file` | Premakne ali preimenuje datoteko ali imenik |

Spodnja shema prikazuje, kako Stdio transport deluje med izvajanjem — vaša Java aplikacija zažene MCP strežnik kot otroški proces in komunicirata preko pip stdin/stdout brez omrežja ali HTTP:

<img src="../../../translated_images/sl/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Potek Stdio transporta" width="800"/>

*Stdio transport v akciji — vaša aplikacija zažene MCP strežnik kot otroški proces in komunicira preko pip stdin/stdout.*

> **🤖 Poskusite s [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) in vprašajte:
> - "Kako deluje Stdio transport in kdaj naj ga uporabim namesto HTTP?"
> - "Kako LangChain4j upravlja življenjski cikel procesov MCP strežnikov?"
> - "Kakšne so varnostne posledice, če AI omogočim dostop do datotečnega sistema?"

## Agentni modul

Medtem ko MCP zagotavlja standardizirana orodja, LangChain4j-jev **agentni modul** zagotavlja deklarativen način gradnje agentov, ki orkestrirajo ta orodja. Oznaka `@Agent` in `AgenticServices` vam omogočata, da določite vedenje agenta preko vmesnikov namesto imperative kode.

V tem modulu boste raziskali vzorec **Nadzorni agent** — napreden agentni pristop, kjer "nadzornik" dinamično odloča, katere podagente poklicati glede na zahtevo uporabnika. Združili bomo oba koncepta tako, da bomo enemu izmed naših podagentov dali MCP-pogonjene možnosti dostopa do datotek.

Za uporabo agentnega modula dodajte naslednjo Maven odvisnost:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Opomba:** Modul `langchain4j-agentic` uporablja ločeno lastnost verzije (`langchain4j.mcp.version`), ker je izdan po drugačnem urniku kot jedrne knjižnice LangChain4j.

> **⚠️ Eksperimentalno:** Modul `langchain4j-agentic` je **eksperimentalni** in se lahko spreminja. Stabilen način gradnje AI asistentov ostaja `langchain4j-core` z lastnimi orodji (Modul 04).

## Zagon primerov

### Predpogoji

- Zaključen [Modul 04 - Orodja](../04-tools/README.md) (ta modul nadgradi koncepte lastnih orodij in jih primerja z MCP orodji)
- `.env` datoteka v korenskem imeniku z Azure poverilnicami (ustvarjena z `azd up` v Modulu 01)
- Java 21+, Maven 3.9+
- Node.js 16+ in npm (za MCP strežnike)

> **Opomba:** Če še niste nastavili okoljskih spremenljivk, glejte [Modul 01 - Uvod](../01-introduction/README.md) za navodila za nameščanje (`azd up` ustvari `.env` datoteko samodejno), ali pa kopirajte `.env.example` v `.env` v korenskem imeniku in vnesite svoje vrednosti.

## Hiter začetek

**Uporaba VS Code:** Preprosto kliknite desni gumb miške na katero koli demo datoteko v Explorerju in izberite **"Run Java"** ali uporabite konfiguracije zagona iz plošče Run and Debug (prej poskrbite, da je vaša `.env` datoteka konfigurirana z Azure poverilnicami).

**Uporaba Mavena:** Alternativno lahko zaženete iz ukazne vrstice z naslednjimi primeri.

### Operacije z datotekami (Stdio)

Ta primer prikazuje orodja, ki temeljijo na lokalnih podprocesih.

**✅ Ni potrebnih predpogojev** - MCP strežnik se zažene samodejno.

**Uporaba zagonskih skript (priporočeno):**

Zagonske skripte samodejno naložijo okoljske spremenljivke iz korenske `.env` datoteke:

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

**Uporaba VS Code:** Desni klik na `StdioTransportDemo.java` in izberite **"Run Java"** (preverite, da je vaša `.env` datoteka konfigurirana).

Aplikacija samodejno zažene filesystem MCP strežnik in prebere lokalno datoteko. Opazite, kako za vas upravlja podproces.

**Pričakovani izhod:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Nadzorni agent

Vzorčni vzorec **Nadzorni agent** je **prilagodljiva** oblika agentne AI. Nadzornik uporablja LLM za avtonomno odločanje, katere agente poklicati glede na uporabnikovo zahtevo. V naslednjem primeru združimo MCP-podprti dostop do datotek z LLM agentom za ustvarjanje nadzorovanega delovnega toka branja datoteke → poročila.

V demonstraciji `FileAgent` bere datoteko z uporabo MCP orodij datotečnega sistema, `ReportAgent` pa ustvari strukturirano poročilo z izvršnim povzetkom (1 stavek), 3 ključnimi točkami in priporočili. Nadzornik orkestrira ta potek samodejno:

<img src="../../../translated_images/sl/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Vzorčni vzorec nadzornega agenta" width="800"/>

*Nadzornik uporablja svoj LLM za odločanje, katere agente poklicati in v kakšnem vrstnem redu — ni potrebe po trdo kodiranem usmerjanju.*

Tako izgleda konkreten delovni tok za naš cevovod datoteka → poročilo:

<img src="../../../translated_images/sl/file-report-workflow.649bb7a896800de9.webp" alt="Delovni tok od datoteke do poročila" width="800"/>

*FileAgent prebere datoteko preko MCP orodij, nato ReportAgent preoblikuje surovo vsebino v strukturirano poročilo.*

Vsak agent shrani svoj izhod v **Agentic Scope** (deljeno pomnilnik), kar omogoča dostop naslednjim agentom do prejšnjih rezultatov. To prikazuje, kako se MCP orodja brezhibno vključijo v agentne delovne tokove — Nadzornik ne potrebuje vedeti *kako* se datoteke berejo, samo da to zmore `FileAgent`.

#### Zagon demonstracije

Zagonske skripte samodejno naložijo okoljske spremenljivke iz korenske `.env` datoteke:

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

**Uporaba VS Code:** Desni klik na `SupervisorAgentDemo.java` in izberite **"Run Java"** (preverite, da je vaša `.env` datoteka konfigurirana).

#### Kako nadzornik deluje

Preden zgradite agente, morate povezati MCP transport s klientom in ga zaviti kot `ToolProvider`. Tako MCP strežnikova orodja postanejo na voljo vašim agentom:

```java
// Ustvari MCP odjemalca iz transporta
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Ovij odjemalca kot ToolProvider — to povezuje MCP orodja v LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Nato lahko `mcpToolProvider` vstavite v katerega koli agenta, ki potrebuje MCP orodja:

```java
// Korak 1: FileAgent bere datoteke z uporabo orodij MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Ima orodja MCP za datotečne operacije
        .build();

// Korak 2: ReportAgent ustvari strukturirana poročila
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Nadzornik usklajuje potek dela datoteka → poročilo
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Vrni končno poročilo
        .build();

// Nadzornik odloči, katere agente aktivirati glede na zahtevo
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Strategije odziva

Pri konfiguraciji `SupervisorAgent` določite, kako naj oblikuje svoj končni odgovor uporabniku po tem, ko podagentje opravijo svoje naloge. Spodnja shema prikazuje tri razpoložljive strategije — LAST vrne izhod zadnjega agenta neposredno, SUMMARY sintetizira vse izhode preko LLM, SCORED pa izbere tistega z višjo oceno glede na prvotno zahtevo:

<img src="../../../translated_images/sl/response-strategies.3d0cea19d096bdf9.webp" alt="Strategije odziva" width="800"/>

*Tri strategije, kako nadzornik oblikuje končni odgovor — izberite glede na to, ali želite izhod zadnjega agenta, sintetiziran povzetek ali najbolj ocenjen odgovor.*

Razpoložljive strategije so:

| Strategija | Opis |
|------------|------|
| **LAST** | Nadzornik vrne izhod zadnjega poklicanega podagenta ali orodja. To je uporabno, kadar je zadnji agent v delovnem toku posebej zasnovan za ustvarjanje popolnega, končnega odgovora (npr. "Agent za povzetke" v raziskovalnem cevovodu). |
| **SUMMARY** | Nadzornik uporabi svoj notranji jezikovni model (LLM), da sintetizira povzetek celotne interakcije in vseh izhodov podagentov, nato vrne ta povzetek kot končni odgovor. To uporabniku zagotovi čist, združen odgovor. |
| **SCORED** | Sistem uporabi notranji LLM, da oceni tako zadnji odgovor (LAST) kot povzetek (SUMMARY) glede na prvotno zahtevo uporabnika, in vrne tistega, ki dobi višjo oceno. |
Oglejte si [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) za popolno implementacijo.

> **🤖 Preizkusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) in vprašajte:
> - "Kako Supervisor odloči, katere agente aktivirati?"
> - "Kakšna je razlika med vzorci Supervisor in Sekvenčnega poteka dela?"
> - "Kako lahko prilagodim načrtovalno vedenje Supervisorja?"

#### Razumevanje izhoda

Ko zaženete demo, boste videli strukturiran prikaz, kako Supervisor usklajuje več agentov. Tukaj je, kaj vsak del pomeni:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Glava** predstavlja koncept poteka dela: osredotočen tok od branja datotek do generiranja poročila.

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

**Diagram poteka dela** prikazuje pretok podatkov med agenti. Vsak agent ima specifično vlogo:
- **FileAgent** bere datoteke z orodji MCP in shrani surovo vsebino v `fileContent`
- **ReportAgent** porabi to vsebino in ustvari strukturirano poročilo v `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Uporabniška zahteva** prikazuje nalogo. Supervisor to analizira in se odloči za aktivacijo FileAgent → ReportAgent.

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

**Orkestracija Supervisorja** prikazuje 2-korakni potek dela v akciji:
1. **FileAgent** prebere datoteko preko MCP in shrani vsebino
2. **ReportAgent** prejme vsebino in generira strukturirano poročilo

Supervisor je te odločitve sprejel **avtonomno** na podlagi zahtevka uporabnika.

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

#### Razlaga funkcij Agentic modula

Primer prikazuje več naprednih funkcij agentic modula. Podrobneje si poglejmo Agentic Scope in Agent Listeners.

**Agentic Scope** prikazuje deljeno pomnilniško območje, kjer so agenti shranjevali rezultate z `@Agent(outputKey="...")`. To omogoča:
- Kasnejšim agentom dostop do izhodov predhodnih agentov
- Supervisorju ustvarjanje končnega odgovora
- Vam pregled nad tem, kaj je posamezen agent ustvaril

Spodnji diagram prikazuje, kako Agentic Scope deluje kot deljeni pomnilnik v poteku od datoteke do poročila — FileAgent zapiše svoj izhod pod ključem `fileContent`, ReportAgent ga prebere in zapiše svoj izhod pod `report`:

<img src="../../../translated_images/sl/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope deluje kot deljeni pomnilnik — FileAgent piše `fileContent`, ReportAgent bere to in piše `report`, vaša koda pa bere končni rezultat.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Surovi podatki datoteke iz FileAgent
String report = scope.readState("report");            // Strukturirano poročilo iz ReportAgent
```

**Agent Listeners** omogočajo spremljanje in razhroščevanje izvoza agentov. Korak-po-korak izhod, ki ga vidite v demu, prihaja iz AgentListenerja, ki se poveže z vsako aktivacijo agenta:
- **beforeAgentInvocation** - Pokliče se, ko Supervisor izbere agenta, kar vam pokaže, kateri agent je bil izbran in zakaj
- **afterAgentInvocation** - Pokliče se, ko agent zaključi, prikazuje njegov rezultat
- **inheritedBySubagents** - Če je true, poslušalec spremlja vse agente v hierarhiji

Naslednji diagram prikazuje celoten cikel življenjske dobe Agent Listenerja, vključno s tem, kako `onError` obravnava napake med izvajanjem agenta:

<img src="../../../translated_images/sl/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners se povezujejo z izvajanjem — spremljajo začetek, zaključek ali pojav napak pri agentih.*

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
        return true; // Razširi na vse podagente
    }
};
```

Poleg vzorca Supervisor modul `langchain4j-agentic` zagotavlja več močnih vzorcev potekov dela. Spodnji diagram prikazuje vseh pet — od preprostih sekvenčnih tokov do človeškega nadzora v procesu potrjevanja:

<img src="../../../translated_images/sl/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Pet vzorcev potekov dela za usklajevanje agentov — od preprostih sekvenčnih tokov do potrjevalnih procesov s človeškim nadzorom.*

| Vzorec | Opis | Uporaba |
|---------|-------------|----------|
| **Sekvenčno** | Izvajanje agentov po vrsti, izhod teče naprej | Pipelines: raziskava → analiza → poročilo |
| **Vzporedno** | Sočasno izvajanje agentov | Neodvisne naloge: vreme + novice + borza |
| **Zanka** | Ponavljanje, dokler ni izpolnjen pogoj | Ocena kakovosti: izboljševanje do ocene ≥ 0.8 |
| **Pogojno** | Usmerjanje glede na pogoje | Klasifikacija → usmerjanje k specialistu |
| **Človek v zanki** | Dodajanje človeških kontrolnih točk | Procesi odobritve, pregled vsebin |

## Ključni koncepti

Zdaj, ko ste raziskali MCP in agentic modul v praksi, povzamimo, kdaj uporabiti kateri pristop.

Ena največjih prednosti MCP je njena širša ekosistem. Spodnji diagram prikazuje, kako enoten univerzalni protokol povezuje vašo AI aplikacijo z različnimi MCP strežniki — od dostopa do datotečnih sistemov in baz do GitHub, e-pošte, spletnega strganja in še več:

<img src="../../../translated_images/sl/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP ustvarja univerzalen ekosistem protokolov — vsak MCP združljiv strežnik deluje z vsakim MCP združljivim odjemalcem, kar omogoča delitev orodij med aplikacijami.*

**MCP** je idealen, ko želite izkoristiti obstoječe ekosisteme orodij, graditi orodja, ki jih lahko delijo različne aplikacije, integrirati storitve tretjih oseb s standardnimi protokoli ali zamenjevati implementacije orodij brez spreminjanja kode.

**Agentic modul** deluje najbolje, ko želite deklarativne definicije agentov z `@Agent` anotacijami, potrebujete orkestracijo potekov dela (sekvenčno, zanka, vzporedno), raje uporabljate zasnovo agentov na osnovi vmesnikov kot imperativno kodo ali kombinirate več agentov, ki delijo izhode prek `outputKey`.

**Vzorec Supervisor Agent** izstopa, ko potek dela ni vnaprej predvidljiv in želite, da odloča LLM, ko imate več specializiranih agentov, ki potrebujejo dinamično orkestracijo, ko gradite pogovorne sisteme, ki usmerjajo k različnim sposobnostim, ali ko želite najbolj prilagodljivo, adaptivno vedenje agenta.

Da vam pomagamo izbrati med lastnimi `@Tool` metodami iz Modula 04 in MCP orodji iz tega modula, spodnja primerjava izpostavi ključne prednosti — lastna orodja vam nudijo tesno povezavo in popolno varnost tipov za logiko specifično za aplikacijo, medtem ko MCP orodja ponujajo standardizirane, večkrat uporabne integracije:

<img src="../../../translated_images/sl/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Kdaj uporabiti lastne @Tool metode proti MCP orodjem — lastna orodja za specifično logiko aplikacije s popolno varnostjo tipa, MCP orodja za standardizirane integracije, ki delujejo med aplikacijami.*

## Čestitke!

Uspešno ste zaključili vseh pet modulov tečaja LangChain4j za začetnike! Tukaj je pogled na celotno učno pot, ki ste jo opravili — od osnovnega klepeta do agentic sistemov, podprtih z MCP:

<img src="../../../translated_images/sl/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Vaša učna pot skozi vseh pet modulov — od osnovnega klepeta do agentic sistemov podprtih z MCP.*

Zaključili ste tečaj LangChain4j za začetnike. Naučili ste se:

- Kako graditi pogovorni AI s pomnilnikom (Modul 01)
- Vzorci inženiringa pozivov za različna opravila (Modul 02)
- Vezanje odgovorov na vaše dokumente z RAG (Modul 03)
- Ustvarjanje osnovnih AI agentov (asistentov) z lastnimi orodji (Modul 04)
- Integracija standardiziranih orodij z LangChain4j MCP in Agentic moduli (Modul 05)

### Kaj sledi?

Po zaključenih modulih raziščite [Vodnik za testiranje](../docs/TESTING.md), da vidite koncepte testiranja LangChain4j v praksi.

**Uradni viri:**
- [Dokumentacija LangChain4j](https://docs.langchain4j.dev/) - Vseobsegajoči vodiči in API referenca
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Izvorna koda in primeri
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - Korak-po-korak vodiči za različne primere uporabe

Hvala, ker ste zaključili ta tečaj!

---

**Navigacija:** [← Prejšnji: Modul 04 - Orodja](../04-tools/README.md) | [Nazaj na glavno](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:  
Ta dokument je bil preveden z uporabo AI prevajalske storitve [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, upoštevajte, da avtomatizirani prevodi lahko vsebujejo napake ali netočnosti. Izvirni dokument v njegovem izvirnem jeziku je treba obravnavati kot avtoritativni vir. Za ključne informacije priporočamo strokovni človeški prevod. Ne odgovarjamo za morebitna nesporazume ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->