# Modul 05: Protokol modelnega konteksta (MCP)

## Kazalo

- [Kaj se boste naučili](../../../05-mcp)
- [Kaj je MCP?](../../../05-mcp)
- [Kako MCP deluje](../../../05-mcp)
- [Agentni modul](../../../05-mcp)
- [Zagon primerov](../../../05-mcp)
  - [Predpogoji](../../../05-mcp)
- [Hiter začetek](../../../05-mcp)
  - [Datotečne operacije (Stdio)](../../../05-mcp)
  - [Nadzorni agent](../../../05-mcp)
    - [Zagon demonstracije](../../../05-mcp)
    - [Kako deluje nadzornik](../../../05-mcp)
    - [Kako FileAgent med izvajanjem odkrije MCP orodja](../../../05-mcp)
    - [Strategije odziva](../../../05-mcp)
    - [Razumevanje izhoda](../../../05-mcp)
    - [Pojasnilo funkcij agentnega modula](../../../05-mcp)
- [Ključni pojmi](../../../05-mcp)
- [Čestitamo!](../../../05-mcp)
  - [Kaj sledi?](../../../05-mcp)

## Kaj se boste naučili

Zgradili ste pogovorno umetno inteligenco, obvladali pozive, utemeljili odgovore v dokumentih in ustvarili agente z orodji. Vsa ta orodja pa so bila prilagojena za vašo specifično aplikacijo. Kaj pa, če bi lahko svoji umetni inteligenci dali dostop do standardiziranega ekosistema orodij, ki jih lahko kdorkoli ustvari in deli? V tem modulu se boste naučili prav to z Model Context Protocol (MCP) in agentnim modulom LangChain4j. Najprej prikažemo preprost MCP bralnik datotek in nato pokažemo, kako se enostavno vključi v napredne agentne poteke dela z uporabo vzorca Supervisor Agent.

## Kaj je MCP?

Model Context Protocol (MCP) nudi prav to – standardiziran način, da AI aplikacije odkrijejo in uporabljajo zunanja orodja. Namesto pisanja prilagojenih integracij za vsak podatek ali storitev, se povežete s strežniki MCP, ki svoje zmogljivosti razkrivajo v skladni obliki. Vaš AI agent lahko nato ta orodja samodejno odkrije in uporablja.

Diagram spodaj prikazuje razliko — brez MCP vsaka integracija zahteva prilagojeno točko do točke povezavo; z MCP en sam protokol poveže vašo aplikacijo z vsakim orodjem:

<img src="../../../translated_images/sl/mcp-comparison.9129a881ecf10ff5.webp" alt="Primerjava MCP" width="800"/>

*Pred MCP: Kompleksne točka-do-točka integracije. Po MCP: En protokol, neskončne možnosti.*

MCP rešuje temeljni problem v razvoju AI: vsaka integracija je prilagojena. Želite dostop do GitHub? Prilagojena koda. Želite brati datoteke? Prilagojena koda. Želite poizvedovati bazen podatkov? Prilagojena koda. Nobena od teh integracij ne deluje z drugimi AI aplikacijami.

MCP to standardizira. MCP strežnik razkriva orodja s jasnimi opisi in shemami parametrov. Vsak MCP klient se lahko poveže, odkrije razpoložljiva orodja in jih uporablja. Zgradi enkrat, uporabi povsod.

Diagram spodaj ponazarja to arhitekturo — en MCP klient (vaša AI aplikacija) se poveže z več MCP strežniki, pri čemer vsak razkrije svoj nabor orodij prek standardiziranega protokola:

<img src="../../../translated_images/sl/mcp-architecture.b3156d787a4ceac9.webp" alt="Arhitektura MCP" width="800"/>

*Arhitektura Model Context Protocol – standardizirano odkrivanje in izvajanje orodij*

## Kako MCP deluje

Pod pokrovom MCP uporablja slojevito arhitekturo. Vaša Java aplikacija (MCP klient) odkrije razpoložljiva orodja, pošlje JSON-RPC zahteve preko transportnega sloja (Stdio ali HTTP), MCP strežnik izvede operacije in vrne rezultate. Naslednji diagram razčleni vsak sloj tega protokola:

<img src="../../../translated_images/sl/mcp-protocol-detail.01204e056f45308b.webp" alt="Podrobnosti protokola MCP" width="800"/>

*Kako MCP deluje pod pokrovom — klienti odkrijejo orodja, izmenjujejo JSON-RPC sporočila in izvajajo operacije preko transportnega sloja.*

**Arhitektura Strežnik-Klient**

MCP uporablja model strežnik-klient. Strežniki zagotavljajo orodja – branje datotek, poizvedovanje baz, klicanje API-jev. Klienti (vaša AI aplikacija) se povežejo do strežnikov in uporabljajo njihova orodja.

Za uporabo MCP z LangChain4j dodajte to Maven odvisnost:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Odkritje orodij**

Ko se vaš klient poveže s MCP strežnikom, vpraša "Katera orodja imate?" Strežnik odgovori z seznamom razpoložljivih orodij, vsako z opisi in shemami parametrov. Vaš AI agent lahko nato izbere, katera orodja uporabiti na podlagi uporabniških zahtev. Diagram spodaj prikazuje ta rokovanje — klient pošlje zahtevo `tools/list` in strežnik vrne razpoložljiva orodja z opisi in shemami parametrov:

<img src="../../../translated_images/sl/tool-discovery.07760a8a301a7832.webp" alt="Odkritje orodij MCP" width="800"/>

*AI ob zagonu odkrije razpoložljiva orodja — zdaj ve, katere zmogljivosti so na voljo in lahko odloči, katera uporabiti.*

**Transportni mehanizmi**

MCP podpira različne transportne mehanizme. Dve možnosti sta Stdio (za lokalno komunikacijo podprocesov) in Streamable HTTP (za oddaljene strežnike). Ta modul prikazuje Stdio transport:

<img src="../../../translated_images/sl/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transportni mehanizmi" width="800"/>

*Transportni mehanizmi MCP: HTTP za oddaljene strežnike, Stdio za lokalne procese*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Za lokalne procese. Vaša aplikacija požene strežnik kot podproces in komunicira preko standardnega vhoda/izhoda. Uporabno za dostop do datotečnega sistema ali orodij ukazne vrstice.

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
| `read_file` | Prebere vsebino ene datoteke |
| `read_multiple_files` | Prebere več datotek v enem klicu |
| `write_file` | Ustvari ali prepiše datoteko |
| `edit_file` | Izvede ciljno iskanje in zamenjavo |
| `list_directory` | Nakaže datoteke in imenike na poti |
| `search_files` | Rekurzivno išče datoteke, ki ustrezajo vzorcu |
| `get_file_info` | Pridobi metapodatke datoteke (velikost, časovne žige, dovoljenja) |
| `create_directory` | Ustvari imenik (vključno z nadrejenimi imeniki) |
| `move_file` | Premakne ali preimenuje datoteko ali imenik |

Naslednji diagram prikazuje, kako Stdio transport deluje med izvajanjem — vaša Java aplikacija požene MCP strežnik kot podproces in med njima poteka komunikacija preko stdin/stdout kanalov, brez mrežne povezave ali HTTP:

<img src="../../../translated_images/sl/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Tok Stdio transporta" width="800"/>

*Stdio transport v akciji — aplikacija požene MCP strežnik kot podproces in komunicira preko stdin/stdout kanalov.*

> **🤖 Poskusi z [GitHub Copilot](https://github.com/features/copilot) Chat:** Odpri [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) in vprašaj:  
> - "Kako deluje Stdio transport in kdaj naj ga uporabim namesto HTTP?"  
> - "Kako LangChain4j upravlja življenjski cikel podprocecev MCP strežnika?"  
> - "Kakšni so varnostni vidiki dostopa AI do datotečnega sistema?"

## Agentni modul

Medtem ko MCP zagotavlja standardizirana orodja, LangChain4j-jev **agentni modul** omogoča deklarativen način gradnje agentov, ki orkestrirajo ta orodja. Anotacija `@Agent` in `AgenticServices` omogočata definiranje obnašanja agenta preko vmesnikov namesto imperativne kode.

V tem modulu boste raziskali vzorec **Supervisor Agent** — napredno agentno AI pristop, kjer "nadzorni" agent dinamično odloča, katere pod-agente sprožiti na podlagi uporabniških zahtev. Združili bomo oba koncepta tako, da bomo enemu izmed naših pod-agentov dali MCP-poganjane zmogljivosti dostopa do datotek.

Za uporabo agentnega modula dodajte to Maven odvisnost:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Opomba:** `langchain4j-agentic` modul uporablja ločeno lastnost različice (`langchain4j.mcp.version`), ker se izda po drugačnem urniku kot osnovne knjižnice LangChain4j.

> **⚠️ Eksperimentalno:** `langchain4j-agentic` modul je **eksperimentalni** in se lahko spremeni. Stabilen način za ustvarjanje AI pomočnikov ostaja `langchain4j-core` s prilagojenimi orodji (Modul 04).

## Zagon primerov

### Predpogoji

- Zaključen [Modul 04 - Orodja](../04-tools/README.md) (ta modul gradi na konceptih prilagojenih orodij in jih primerja z orodji MCP)
- Datoteka `.env` v korenskem imeniku z Azure poverilnicami (ustvarjena z `azd up` v Modulu 01)
- Java 21+, Maven 3.9+
- Node.js 16+ in npm (za MCP strežnike)

> **Opomba:** Če še niste nastavili svojih okolijskih spremenljivk, glejte [Modul 01 - Uvod](../01-introduction/README.md) za navodila o namestitvi (`azd up` samodejno ustvari `.env` datoteko), ali kopirajte `.env.example` v `.env` v korenskem imeniku in dopolnite svoje vrednosti.

## Hiter začetek

**Uporaba VS Code:** Preprosto z desnim klikom na katerokoli datoteko demo v Explorerju izberite **"Run Java"**, ali uporabite konfiguracije zagona iz panela Run and Debug (najprej poskrbite, da je vaša `.env` datoteka nastavljena z Azure poverilnicami).

**Uporaba Maven:** Lahko pa zaženete iz ukazne vrstice z naslednjimi primeri.

### Datotečne operacije (Stdio)

To prikazuje orodja na osnovi lokalnega podprocesa.

**✅ Brez potrebnih predpogojev** - MCP strežnik se zažene samodejno.

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

**Uporaba VS Code:** Z desnim klikom na `StdioTransportDemo.java` izberite **"Run Java"** (poskrbite, da je `.env` konfiguriran).

Aplikacija samodejno požene MCP strežnik za datotečni sistem in prebere lokalno datoteko. Opazite, kako se upravljanje podprocesa izvede za vas.

**Pričakovani izhod:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Nadzorni agent

Vzorec **Supervisor Agent** je **prilagodljiva** oblika agentne AI. Nadzornik s pomočjo LLM samostojno odloča, katere agente sprožiti na podlagi uporabnikove zahteve. V naslednjem primeru združimo MCP-poganjan dostop do datotek z LLM agentom za ustvarjanje nadzorovane verige branja datotek → poročila.

V demonstraciji `FileAgent` prebere datoteko z uporabo MCP orodij za datotečni sistem, `ReportAgent` pa ustvari strukturirano poročilo z izvršilnim povzetkom (1 stavek), 3 ključnimi točkami in priporočili. Nadzornik samodejno orkestrira ta potek:

<img src="../../../translated_images/sl/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Vzorec nadzornega agenta" width="800"/>

*Nadzornik uporabi svoj LLM za odločitev, katere agente sprožiti in v kakšnem vrstnem redu — ni potrebna trdo kodirana usmeritev.*

Tako izgleda konkreten potek datoteka-poročilo v našem kanalu:

<img src="../../../translated_images/sl/file-report-workflow.649bb7a896800de9.webp" alt="Potek od datoteke do poročila" width="800"/>

*FileAgent prebere datoteko preko MCP orodij, nato ReportAgent preoblikuje surovo vsebino v strukturirano poročilo.*

Naslednji zaporedni diagram prikazuje celotno orkestracijo nadzornika — od zagona MCP strežnika, preko samostojne izbire agentov nadzornika, do klicev orodij prek stdio in končnega poročila:

<img src="../../../translated_images/sl/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Zaporedni diagram nadzornega agenta" width="800"/>

*Nadzornik samodejno sproži FileAgent (ki kliče MCP strežnik preko stdio za branje datoteke), nato sproži ReportAgent za ustvarjanje strukturiranega poročila — vsak agent shrani svoj izhod v deljeni Agentic Scope.*

Vsak agent shrani svoj izhod v **Agentic Scope** (deljeni pomnilnik), kar omogoča agentom v nadaljnjih korakih dostop do prejšnjih rezultatov. To pokaže, kako se MCP orodja nemoteno vključijo v agentne delovne tokove — nadzornik ne potrebuje vedeti *kako* se datoteke berejo, le da lahko `FileAgent` to naredi.

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

**Uporaba VS Code:** Z desnim klikom na `SupervisorAgentDemo.java` izberite **"Run Java"** (poskrbite, da je `.env` konfiguriran).

#### Kako deluje nadzornik

Pred gradnjo agentov morate MCP transport povezati s klientom in ga zaviti kot `ToolProvider`. Tako postanejo MCP strežniška orodja na voljo vašim agentom:

```java
// Ustvari MCP odjemalca iz transporta
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Ovij odjemalca kot ToolProvider — s tem povežeš MCP orodja v LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Nato lahko `mcpToolProvider` vbrizgate v katerega koli agenta, ki potrebuje MCP orodja:

```java
// Korak 1: FileAgent bere datoteke z uporabo orodij MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Ima orodja MCP za datotečne operacije
        .build();

// Korak 2: ReportAgent generira strukturirana poročila
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor usklajuje potek dela datoteka → poročilo
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Vrni končno poročilo
        .build();

// Supervisor odloči, katere agente poklicati glede na zahtevo
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Kako FileAgent med izvajanjem odkrije MCP orodja

Morda se sprašujete: **kako `FileAgent` ve, kako uporabiti npm orodja datotečnega sistema?** Odgovor je, da ne ve — **LLM** to ugotovi med izvajanjem skozi sheme orodij.

Vmesnik `FileAgent` je zgolj **definicija poziva**. Nima trdo kodiranega znanja o `read_file`, `list_directory` ali katerem koli drugem MCP orodju. Takole poteka celoten proces od začetka do konca:
1. **Zagon strežnika:** `StdioMcpTransport` zažene npm paket `@modelcontextprotocol/server-filesystem` kot otroški proces  
2. **Odkritje orodij:** `McpClient` pošlje strežniku JSON-RPC zahtevo `tools/list`, ki odgovori z imeni orodij, opisi in shemami parametrov (npr. `read_file` — *"Preberi celotno vsebino datoteke"* — `{ path: string }`)  
3. **Vbrizg shem:** `McpToolProvider` ovije te odkrite sheme in jih naredi dostopne za LangChain4j  
4. **Odločitev LLM:** Ko je poklican `FileAgent.readFile(path)`, LangChain4j pošlje sistemsko sporočilo, uporabniško sporočilo **in seznam shem orodij** LLM-ju. LLM prebere opise orodij in generira klic orodja (npr. `read_file(path="/some/file.txt")`)  
5. **Izvedba:** LangChain4j prestreže klic orodja, ga usmeri skozi MCP klienta nazaj v Node.js podproces, pridobi rezultat in ga posreduje nazaj LLM-ju  

To je isti mehanizem [Odkritje orodij](../../../05-mcp), kot je bilo opisano zgoraj, vendar se uporablja posebej za delovni potek agenta. Oznake `@SystemMessage` in `@UserMessage` usmerjajo vedenje LLM-ja, medtem ko vbrizgani `ToolProvider` zagotavlja **sposobnosti** — LLM ju poveže med izvajanjem.

> **🤖 Preizkusi z [GitHub Copilot](https://github.com/features/copilot) Chat:** Odpri [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) in vprašaj:  
> - "Kako ta agent ve, katero MCP orodje poklicati?"  
> - "Kaj bi se zgodilo, če odstranite ToolProvider iz agentovskega graditelja?"  
> - "Kako se sheme orodij prenesejo do LLM-ja?"  

#### Strategije odgovora

Ko konfiguriraš `SupervisorAgent`, določiš, kako naj oblikuje svoj končni odgovor uporabniku po dokončanju podagentovih nalog. Diagram spodaj prikazuje tri razpoložljive strategije — LAST vrne izhod zadnjega agenta neposredno, SUMMARY sintetizira vse izhode z LLM-jem, SCORED pa izbere tistega z višjo oceno glede na izvirno zahtevo:  

<img src="../../../translated_images/sl/response-strategies.3d0cea19d096bdf9.webp" alt="Strategije odgovorov" width="800"/>  

*Tri strategije, kako Supervisor oblikuje svoj končni odgovor — izberi glede na to, ali želiš izhod zadnjega agenta, sintetiziran povzetek ali možnost z najboljšo oceno.*

Razpoložljive strategije so:

| Strategija | Opis |
|------------|------|
| **LAST** | Supervisor vrne izhod zadnjega klica podagenta ali orodja. To je uporabno, kadar je zadnji agent v delovnem toku posebej zasnovan za pripravo popolnega, končnega odgovora (npr. "Summary Agent" v raziskovalnem poteku). |
| **SUMMARY** | Supervisor uporabi svoj notranji jezikovni model (LLM), da sintetizira povzetek celotne interakcije in vseh izhodov podagentov ter nato vrne ta povzetek kot končni odgovor. To zagotavlja čist, združen odgovor uporabniku. |
| **SCORED** | Sistem uporabi notranji LLM za ocenjevanje tako izhoda LAST kot SUMMARY glede na izvirno uporabniško zahtevo in vrne tistega z višjo oceno. |

Ogled implementacije je v [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java).

> **🤖 Preizkusi z [GitHub Copilot](https://github.com/features/copilot) Chat:** Odpri [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) in vprašaj:  
> - "Kako Supervisor odloča, katere agente poklicati?"  
> - "Kakšna je razlika med vzorci Supervisor in Sequential workflow?"  
> - "Kako prilagodim načrtovalno vedenje Supervisorja?"  

#### Razumevanje izhoda

Ko zaženeš demo, boš videl strukturiran potek, kako Supervisor usklajuje več agentov. Tukaj je, kaj pomenijo posamezni deli:  

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Glava** predstavlja koncept delovnega toka: osredotočen potek od branja datotek do generiranja poročila.

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
  
**Diagram poteka** prikazuje pretok podatkov med agenti. Vsak agent ima specifično vlogo:  
- **FileAgent** bere datoteke preko MCP orodij in hrani surovo vsebino v `fileContent`  
- **ReportAgent** uporabi to vsebino in ustvari strukturirano poročilo v `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Uporabniška zahteva** prikazuje nalogo. Supervisor jo analizira in odloči, da pokliče FileAgent → ReportAgent.

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
  
**Usklajevanje Supervisorja** prikazuje dvofazni potek v akciji:  
1. **FileAgent** prebere datoteko preko MCP in shrani vsebino  
2. **ReportAgent** prejme vsebino in generira strukturirano poročilo  

Supervisor je te odločitve sprejel **avtonomno** glede na uporabnikovo zahtevo.

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
  
#### Pojasnilo lastnosti agentnega modula

Primer prikazuje več naprednih lastnosti agentnega modula. Poglejmo si podrobneje Agentic Scope in Agent Listenerje.

**Agentic Scope** prikazuje deljeno pomnilniško območje, kamor so agenti shranjevali svoje rezultate z uporabo `@Agent(outputKey="...")`. To omogoča:  
- Kasnejšim agentom dostop do izhodov prej izvedenih agentov  
- Supervisorju sintetizacijo končnega odgovora  
- Tebe, da pregledaš, kaj je vsak agent ustvaril  

Diagram spodaj prikazuje delovanje Agentic Scope kot deljenega pomnilnika v poteku od datoteke do poročila — FileAgent zapiše izhod pod ključ `fileContent`, ReportAgent ga prebere in zapiše svoj izhod pod `report`:  

<img src="../../../translated_images/sl/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope deljeni pomnilnik" width="800"/>  

*Agentic Scope deluje kot deljeni pomnilnik — FileAgent zapiše `fileContent`, ReportAgent ga prebere in zapiše `report`, tvoja koda pa lahko prebere končni rezultat.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Surovi podatki datoteke iz FileAgenta
String report = scope.readState("report");            // Strukturirano poročilo iz ReportAgenta
```
  
**Agent Listenerji** omogočajo spremljanje in odpravljanje napak izvajanja agentov. Korak za korakom izhod, ki ga vidiš v demu, prihaja iz AgentListenerja, ki se poveže na vsak klic agenta:  
- **beforeAgentInvocation** - Klic ob izbiri agenta s strani Supervisorja, da vidiš izbranega agenta in razlog  
- **afterAgentInvocation** - Klic po zaključku agenta, prikazuje njegov rezultat  
- **inheritedBySubagents** - Če je true, poslušalec spremlja vse agente v hierarhiji  

Naslednji diagram prikazuje celoten življenjski cikel Agent Listenerja, vključno s tem, kako `onError` upravlja napake med izvajanjem agenta:  

<img src="../../../translated_images/sl/agent-listeners.784bfc403c80ea13.webp" alt="Življenjski cikel Agent Listenerjev" width="800"/>  

*Agent Listenerji se povežejo na življenjski cikel izvajanja — spremljaj začetek, zaključek ali napake agentov.*

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
        return true; // Razširi na vse podagentje
    }
};
```
  
Poleg vzorca Supervisor modul `langchain4j-agentic` ponuja več zmogljivih delovnih vzorcev. Spodnji diagram prikazuje vseh pet — od preprostih zaporednih tokov do potekov odobritve z človekom v zanki:

<img src="../../../translated_images/sl/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Vzorci delovnih tokov agentov" width="800"/>  

*Pet vzorcev delovnih tokov za usklajevanje agentov — od preprostih zaporednih procesov do odobritvenih potekov s človekom v zanki.*

| Vzorec | Opis | Uporaba |
|---------|-------------|----------|
| **Sequential** | Izvajanje agentov v zaporedju, izhod teče v naslednjega | Pipeline: raziskava → analiza → poročilo |
| **Parallel** | Sočasno izvajanje agentov | Neodvisne naloge: vreme + novice + delnice |
| **Loop** | Ponavljanje dokler ni izpolnjen pogoj | Ocena kakovosti: izboljšaj dokler ocena ≥ 0.8 |
| **Conditional** | Usmerjanje glede na pogoje | Klasifikacija → usmeritev k specializiranemu agentu |
| **Human-in-the-Loop** | Dodajanje človeških kontrolnih točk | Odobritveni postopki, pregled vsebin |

## Ključni pojmi

Ko si raziskal MCP in agentni modul v praksi, povzamimo, kdaj uporabiti kateri pristop.

Ena največjih prednosti MCP je rastoči ekosistem. Diagram spodaj prikazuje, kako enotni univerzalni protokol povezuje tvoj AI sistem z različnimi MCP strežniki — od dostopa do datotečnega sistema in baz podatkov do GitHub, e-pošte, spletnega strganja in še več:

<img src="../../../translated_images/sl/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP ekosistem" width="800"/>  

*MCP ustvarja univerzalni protokolarni ekosistem — vsak MCP-kompatibilen strežnik deluje s katerimkoli MCP-kompatibilnim odjemalcem, omogočajoč deljenje orodij med aplikacijami.*

**MCP** je idealen, ko želiš izkoristiti obstoječe ekosisteme orodij, graditi orodja, ki jih lahko deli več aplikacij, vključevati tretje storitve s standardnimi protokoli ali zamenjati implementacije orodij brez spreminjanja kode.

**Agentni modul** najbolje deluje, ko želiš deklarativne definicije agentov z oznakami `@Agent`, potrebuješ orkestracijo delovnih tokov (zaporedno, zanko, paralelno), raje oblikuješ agente prek vmesnika kot z imperativno kodo ali kombiniraš več agentov, ki delijo izhode prek `outputKey`.

**Vzorec Supervisor Agent** se izkaže, ko delovni tok ni vnaprej predvidljiv in želiš, da LLM odloča, ko imaš več specializiranih agentov, ki potrebujejo dinamično usklajevanje, ko ustvarjaš pogovorne sisteme z usmerjanjem na različne zmogljivosti ali ko želiš najbolj prilagodljivo in odzivno vedenje agenta.

Za pomoč pri odločitvi med prilagojenimi metodami `@Tool` iz Modula 04 in MCP orodji iz tega modula spodnja primerjava izpostavi glavne prednosti in slabosti — prilagojena orodja nudijo tesno povezavo in popolno varnost tipov za aplikacijsko logiko, MCP orodja pa standardizirane, ponovno uporabne integracije:

<img src="../../../translated_images/sl/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Prilagojena orodja vs MCP orodja" width="800"/>  

*Kdaj uporabiti prilagojene metode @Tool in kdaj MCP orodja — prilagojena za aplikacijsko logiko s popolno varnostjo tipov, MCP pa za standardizirane integracije med aplikacijami.*  

## Čestitamo!

Prebral si vseh pet modulov tečaja LangChain4j za začetnike! Tukaj je pregled celotne učne poti, ki si jo opravil — od osnovnega klepeta do agentnih sistemov na osnovi MCP:  

<img src="../../../translated_images/sl/course-completion.48cd201f60ac7570.webp" alt="Zaključek tečaja" width="800"/>  

*Tvoja učna pot skozi vseh pet modulov — od osnovnega klepeta do agentnih sistemov s podporo MCP-ja.*

Zaključil si tečaj LangChain4j za začetnike. Naučil si se:

- Kako ustvariti pogovorni AI z zmožnostjo pomnjenja (Modul 01)  
- Vzorci za oblikovanje pozivov za različne naloge (Modul 02)  
- Povezava odgovorov s tvojimi dokumenti z RAG (Modul 03)  
- Ustvarjanje osnovnih AI agentov (pomočnikov) z lastnimi orodji (Modul 04)  
- Integracija standardiziranih orodij z moduloma LangChain4j MCP in Agentic (Modul 05)  

### Kaj sledi?

Po zaključku modulov preglej [Vodnik za testiranje](../docs/TESTING.md), da si ogledaš koncepte testiranja LangChain4j v praksi.

**Uradni viri:**  
- [Dokumentacija LangChain4j](https://docs.langchain4j.dev/) – Celoviti vodiči in API referenca  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) – Izvorna koda in primeri  
- [LangChain4j tutoriali](https://docs.langchain4j.dev/tutorials/) – Korak-po-korak vodiči za različne primere uporabe  

Hvala, ker si zaključil ta tečaj!

---

**Navigacija:** [← Prejšnji: Modul 04 - Orodja](../04-tools/README.md) | [Nazaj na glavno](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo storitve za avtomatski prevod [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas opozarjamo, da lahko avtomatizirani prevodi vsebujejo napake ali netočnosti. Izvirni dokument v maternem jeziku velja za avtoritativni vir. Za ključne informacije priporočamo strokovni človeški prevod. Ne odgovarjamo za morebitna nesporazume ali napačne razlage, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->