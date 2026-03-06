# Modul 05: Protokol konteksta modela (MCP)

## Sadržaj

- [Što ćete naučiti](../../../05-mcp)
- [Što je MCP?](../../../05-mcp)
- [Kako MCP funkcionira](../../../05-mcp)
- [Agentni modul](../../../05-mcp)
- [Pokretanje primjera](../../../05-mcp)
  - [Preduvjeti](../../../05-mcp)
- [Brzi početak](../../../05-mcp)
  - [Rad s datotekama (Stdio)](../../../05-mcp)
  - [Nadzorni agent](../../../05-mcp)
    - [Pokretanje demonstracije](../../../05-mcp)
    - [Kako nadzornik funkcionira](../../../05-mcp)
    - [Kako FileAgent otkriva MCP alate u vrijeme izvođenja](../../../05-mcp)
    - [Strategije odgovora](../../../05-mcp)
    - [Razumijevanje izlaza](../../../05-mcp)
    - [Objašnjenje značajki agentnog modula](../../../05-mcp)
- [Ključni pojmovi](../../../05-mcp)
- [Čestitamo!](../../../05-mcp)
  - [Što slijedi?](../../../05-mcp)

## Što ćete naučiti

Izgradili ste konverzacijski AI, ovladali promptovima, povezali odgovore s dokumentima i kreirali agente s alatima. Ali svi ti alati bili su prilagođeni vašoj specifičnoj aplikaciji. Što ako biste svojem AI-ju mogli dati pristup standardiziranom ekosustavu alata koje svatko može stvoriti i dijeliti? U ovom modulu naučit ćete upravo to uz Protokol konteksta modela (MCP) i agentni modul LangChain4j. Prvo prikazujemo jednostavan MCP čitač datoteka, a zatim pokazujemo kako se lako integrira u napredne agentne tokove pomoću obrasca Nadzornog agenta.

## Što je MCP?

Protokol konteksta modela (MCP) pruža upravo to – standardizirani način da AI aplikacije otkriju i koriste vanjske alate. Umjesto pisanja prilagođenih integracija za svaki izvor podataka ili uslugu, povezujete se s MCP poslužiteljima koji izlažu svoje mogućnosti u dosljednom formatu. Vaš AI agent tada može automatski otkriti i koristiti te alate.

Dijagram ispod prikazuje razliku — bez MCP-a, svaka integracija zahtijeva prilagođeno povezivanje točka na točku; s MCP-om, jedan protokol povezuje vašu aplikaciju sa svakim alatom:

<img src="../../../translated_images/hr/mcp-comparison.9129a881ecf10ff5.webp" alt="Usporedba MCP-a" width="800"/>

*Prije MCP-a: složene integracije točka na točku. Nakon MCP-a: jedan protokol, beskonačne mogućnosti.*

MCP rješava temeljni problem u razvoju AI-ja: svaka integracija je prilagođena. Želite pristup GitHubu? Prilagođeni kod. Želite čitati datoteke? Prilagođeni kod. Želite upit u bazu podataka? Prilagođeni kod. I nijedna od ovih integracija ne radi s drugim AI aplikacijama.

MCP to standardizira. MCP poslužitelj izlaže alate s jasnim opisima i shemama. Svaki MCP klijent može se povezati, otkriti dostupne alate i koristiti ih. Izgradi jednom, koristi svugdje.

Naredni dijagram ilustrira ovu arhitekturu — jedan MCP klijent (vaša AI aplikacija) povezuje se s više MCP poslužitelja, od kojih svaki izlaže svoj set alata putem standardnog protokola:

<img src="../../../translated_images/hr/mcp-architecture.b3156d787a4ceac9.webp" alt="Arhitektura MCP-a" width="800"/>

*Arhitektura protokola konteksta modela – standardizirano otkrivanje i izvršavanje alata*

## Kako MCP funkcionira

Ispod haube, MCP koristi slojevitu arhitekturu. Vaša Java aplikacija (MCP klijent) otkriva dostupne alate, šalje JSON-RPC zahtjeve kroz sloj prijenosa (Stdio ili HTTP), a MCP poslužitelj izvršava operacije i vraća rezultate. Sljedeći dijagram razlaže svaki sloj ovog protokola:

<img src="../../../translated_images/hr/mcp-protocol-detail.01204e056f45308b.webp" alt="Detalji MCP protokola" width="800"/>

*Kako MCP funkcionira ispod haube — klijenti otkrivaju alate, razmjenjuju JSON-RPC poruke i izvršavaju operacije kroz sloj prijenosa.*

**Arhitektura poslužitelj-klijent**

MCP koristi model poslužitelj-klijent. Poslužitelji pružaju alate – čitanje datoteka, upite u baze, pozive API-ja. Klijenti (vaša AI aplikacija) se povezuju na poslužitelje i koriste njihove alate.

Da biste koristili MCP s LangChain4j, dodajte ovaj Maven ovisnički zapis:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```
  
**Otkrivanje alata**

Kada se vaš klijent poveže na MCP poslužitelj, pita "Koje alate imaš?" Poslužitelj odgovara s popisom dostupnih alata, svaki s opisom i shemama parametara. Vaš AI agent tada može odlučiti koje alate koristiti na temelju korisničkih zahtjeva. Dijagram ispod prikazuje ovaj dogovor — klijent šalje zahtjev `tools/list`, a poslužitelj vraća dostupne alate sa slikama i shemama parametara:

<img src="../../../translated_images/hr/tool-discovery.07760a8a301a7832.webp" alt="Otkrivanje alata MCP-a" width="800"/>

*AI na početku otkriva dostupne alate – sada zna koje mogućnosti postoje i može odlučiti koje koristiti.*

**Mehanizmi prijenosa**

MCP podržava različite mehanizme prijenosa. Dvije su opcije Stdio (za lokalnu komunikaciju među procesima) i Streamable HTTP (za udaljene poslužitelje). Ovaj modul demonstrira Stdio prijenos:

<img src="../../../translated_images/hr/transport-mechanisms.2791ba7ee93cf020.webp" alt="Mehanizmi prijenosa" width="800"/>

*Mehanizmi prijenosa MCP-a: HTTP za udaljene poslužitelje, Stdio za lokalne procese*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Za lokalne procese. Vaša aplikacija pokreće poslužitelj kao podproces i komunicira putem standardnog ulaza/izlaza. Korisno za pristup datotečnom sustavu ili komandno-linijskim alatima.

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
  
`@modelcontextprotocol/server-filesystem` poslužitelj izlaže sljedeće alate, svi ograničeni na direktorije koje definirate:

| Alat | Opis |
|------|------|
| `read_file` | Čitaj sadržaj jedne datoteke |
| `read_multiple_files` | Čitaj više datoteka u jednom pozivu |
| `write_file` | Kreiraj ili prepiši datoteku |
| `edit_file` | Izvrši ciljane pretraživanje i zamjenu |
| `list_directory` | Prikaži datoteke i direktorije na putanji |
| `search_files` | Rekurzivno traži datoteke koje odgovaraju uzorku |
| `get_file_info` | Dohvati metapodatke datoteke (veličina, vremenske oznake, dozvole) |
| `create_directory` | Kreiraj direktorij (uključujući roditeljske direktorije) |
| `move_file` | Premjesti ili preimenuj datoteku ili direktorij |

Sljedeći dijagram prikazuje kako Stdio prijenos radi u runtime-u — vaša Java aplikacija pokreće MCP poslužitelj kao podproces i oni komuniciraju preko cijevi stdin/stdout, bez mreže ili HTTP-a:

<img src="../../../translated_images/hr/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Tok Stdio prijenosa" width="800"/>

*Stdio prijenos u akciji — vaša aplikacija pokreće MCP poslužitelj kao podproces i komunicira preko cijevi stdin/stdout.*

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) i pitajte:  
> - "Kako radi Stdio prijenos i kada bih ga trebao koristiti umjesto HTTP-a?"  
> - "Kako LangChain4j upravlja životnim ciklusom pokrenutih MCP poslužiteljskih procesa?"  
> - "Koje su sigurnosne implikacije davanja AI-ju pristupa datotečnom sustavu?"

## Agentni modul

Dok MCP pruža standardizirane alate, LangChain4j-ev **agentni modul** pruža deklarativan način izrade agenata koji orkestriraju te alate. `@Agent` anotacija i `AgenticServices` omogućuju definiranje ponašanja agenata kroz sučelja umjesto imperativnog koda.

U ovom modulu istražit ćete obrazac **Nadzornog agenta** — napredni agentni AI pristup gdje "nadzorni" agent dinamički odlučuje koje pod-agente pozvati na temelju korisničkih zahtjeva. Kombinirat ćemo oba koncepta dajući jednom od naših pod-agenta MCP-pokretane mogućnosti pristupa datotekama.

Da biste koristili agentni modul, dodajte ovu Maven ovisnost:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Napomena:** `langchain4j-agentic` modul koristi zasebnu verzijsku oznaku (`langchain4j.mcp.version`) jer se objavljuje prema drugačijem rasporedu nego osnovne LangChain4j biblioteke.

> **⚠️ Eksperimentalno:** `langchain4j-agentic` modul je **eksperimentalan** i može se mijenjati. Stabilan način za izradu AI asistenata i dalje je `langchain4j-core` s prilagođenim alatima (Modul 04).

## Pokretanje primjera

### Preduvjeti

- Završeni [Modul 04 - Alati](../04-tools/README.md) (ovaj modul gradi na konceptima prilagođenih alata i uspoređuje ih s MCP alatima)
- `.env` datoteka u glavnom direktoriju s Azure vjerodajnicama (kreirana pomoću `azd up` u Modulu 01)
- Java 21+, Maven 3.9+
- Node.js 16+ i npm (za MCP poslužitelje)

> **Napomena:** Ako još niste postavili svoje varijable okruženja, pogledajte [Modul 01 - Uvod](../01-introduction/README.md) za upute o postavljanju (`azd up` automatski kreira `.env`), ili kopirajte `.env.example` u `.env` u glavnom direktoriju i ispunite svoje vrijednosti.

## Brzi početak

**Koristeći VS Code:** Jednostavno desni klik na bilo koju demo datoteku u Exploreru i odaberite **"Run Java"**, ili koristite konfiguracije pokretanja iz panela Run and Debug (prije toga provjerite je li `.env` datoteka konfigurirana s Azure vjerodajnicama).

**Koristeći Maven:** Alternativno, možete pokretati s komandne linije s primjercima ispod.

### Rad s datotekama (Stdio)

Ovo demonstrira alate temeljene na lokalnim podprocesima.

**✅ Nema potrebnih preduvjeta** - MCP poslužitelj se automatski pokreće.

**Koristeći start skripte (preporučeno):**

Start skripte automatski učitavaju varijable okruženja iz glavne `.env` datoteke:

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
  
**Koristeći VS Code:** Desni klik na `StdioTransportDemo.java` i odaberite **"Run Java"** (provjerite da je vaša `.env` konfiguracija postavljena).

Aplikacija automatski pokreće MCP poslužitelj za datotečni sustav i čita lokalnu datoteku. Primijetite kako je upravljanje podprocesom obavljeno za vas.

**Očekivani izlaz:**  
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Nadzorni agent

**Obrazac Nadzornog agenta** je **fleksibilan** tip agentnog AI-ja. Nadzornik koristi LLM da autonomno odlučuje koje agente treba pozvati na temelju zahtjeva korisnika. U sljedećem primjeru kombiniramo MCP-pokretan pristup datotekama s LLM agentom za kreiranje nadziranog toka čitanja datoteke → izvješće.

U demonstraciji, `FileAgent` čita datoteku koristeći MCP alat za datotečni sustav, a `ReportAgent` generira strukturirani izvještaj s izvršnim sažetkom (1 rečenica), 3 ključna podatka i preporukama. Nadzornik to orkestrira automatski:

<img src="../../../translated_images/hr/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Obrazac Nadzornog agenta" width="800"/>

*Nadzornik koristi svoj LLM da odluči koje agente pozvati i kojim redoslijedom — nema potrebe za hardkodiranim usmjeravanjem.*

Evo kako izgleda konkretan tijek rada u našem lancu od datoteke do izvještaja:

<img src="../../../translated_images/hr/file-report-workflow.649bb7a896800de9.webp" alt="Tijek rada: datoteka u izvještaj" width="800"/>

*FileAgent čita datoteku preko MCP alata, zatim ReportAgent pretvara sirovi sadržaj u strukturirani izvještaj.*

Sljedeći sekvencijski dijagram prikazuje potpunu orkestraciju Nadzornika — od pokretanja MCP poslužitelja, preko autonomnog odabira agenata, do poziva alata preko stdio i konačnog izvještaja:

<img src="../../../translated_images/hr/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Sekvencijski dijagram nadzornog agenta" width="800"/>

*Nadzornik autonomno poziva FileAgent (koji koristi MCP poslužitelj preko stdio za čitanje datoteke), zatim poziva ReportAgent za generiranje strukturiranog izvještaja — svaki agent sprema svoj izlaz u zajednički Agentic Scope.*

Svaki agent sprema svoj rezultat u **Agentic Scope** (zajednička memorija), što omogućuje sljedećim agentima pristup prethodnim rezultatima. Ovo demonstrira kako se MCP alati besprijekorno integriraju u agentne tokove — Nadzornik ne mora znati *kako* se datoteke čitaju, samo da `FileAgent` to može.

#### Pokretanje demonstracije

Start skripte automatski učitavaju varijable okruženja iz glavne `.env` datoteke:

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
  
**Koristeći VS Code:** Desni klik na `SupervisorAgentDemo.java` i odaberite **"Run Java"** (provjerite da je `.env` konfiguriran).

#### Kako nadzornik funkcionira

Prije izrade agenata, morate povezati MCP prijenos s klijentom i omotati ga kao `ToolProvider`. Ovo je način na koji alati MCP poslužitelja postaju dostupni vašim agentima:

```java
// Kreirajte MCP klijenta iz transporta
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Omotajte klijenta kao ToolProvider — ovo povezuje MCP alate s LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```
  
Sada možete ubrizgati `mcpToolProvider` u bilo kojeg agenta kojem trebaju MCP alati:

```java
// Korak 1: FileAgent čita datoteke koristeći MCP alate
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Ima MCP alate za operacije nad datotekama
        .build();

// Korak 2: ReportAgent generira strukturirane izvještaje
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor koordinira tijek rada datoteka → izvještaj
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Vraća konačni izvještaj
        .build();

// Supervisor odlučuje koje agente pozvati na temelju zahtjeva
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```
  
#### Kako FileAgent otkriva MCP alate u vrijeme izvođenja

Možda se pitate: **kako `FileAgent` zna koristiti npm alate za datotečni sustav?** Odgovor je da ne zna — **LLM** to otkriva u runtimeu preko shema alata.

`FileAgent` sučelje je samo **definicija prompta**. Nema hardkodirano znanje o `read_file`, `list_directory` ili bilo kojem drugom MCP alatu. Evo što se događa od početka do kraja:
1. **Pokretanje poslužitelja:** `StdioMcpTransport` pokreće npm paket `@modelcontextprotocol/server-filesystem` kao podproces
2. **Otkriće alata:** `McpClient` šalje `tools/list` JSON-RPC zahtjev poslužitelju, koji odgovara imenima alata, opisima i shemama parametara (npr. `read_file` — *"Pročitaj kompletan sadržaj datoteke"* — `{ path: string }`)
3. **Umetanje sheme:** `McpToolProvider` omotava ove otkrivene sheme i čini ih dostupnima LangChain4j
4. **LLM odlučuje:** Kada se pozove `FileAgent.readFile(path)`, LangChain4j šalje sistemsku poruku, korisničku poruku, **i popis shema alata** LLM-u. LLM čita opise alata i generira poziv alata (npr. `read_file(path="/some/file.txt")`)
5. **Izvršenje:** LangChain4j presreće poziv alata, usmjerava ga natrag MCP klijentu prema Node.js podprocesu, dobiva rezultat i prosljeđuje ga natrag LLM-u

Ovo je isti mehanizam [Otkrića alata](../../../05-mcp) opisan gore, ali primijenjen specifično na agentni tijek rada. Označbe `@SystemMessage` i `@UserMessage` usmjeravaju ponašanje LLM-a, dok umetnuti `ToolProvider` pruža **sposobnosti** — LLM u runtime-u povezuje ta dva.

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) i pitajte:
> - "Kako ovaj agent zna koji MCP alat pozvati?"
> - "Što bi se dogodilo ako uklonim ToolProvider iz graditelja agenta?"
> - "Kako se sheme alata prosljeđuju LLM-u?"

#### Strategije odgovora

Kada konfigurirate `SupervisorAgent`, određujete kako bi trebao formulirati svoj konačni odgovor korisniku nakon što podagent završi zadatke. Dijagram ispod prikazuje tri dostupne strategije — LAST izravno vraća konačni izlaz agenta, SUMMARY sintetizira sve izlaze kroz LLM, a SCORED odabire onaj koji je bolje ocijenjen u odnosu na izvorni zahtjev:

<img src="../../../translated_images/hr/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Tri strategije kako Supervisor formulira svoj konačni odgovor — odaberite ovisno želite li konačni izlaz zadnjeg agenta, sintetizirani sažetak ili najbolje ocijenjenu opciju.*

Dostupne strategije su:

| Strategija | Opis |
|------------|------|
| **LAST**   | Supervisor vraća izlaz zadnjeg pod-agenta ili pozvanog alata. Korisno je kad je konačni agent u tijeku rada posebno dizajniran za proizvodnju potpunog, konačnog odgovora (npr. "Summary Agent" u istraživačkom lancu). |
| **SUMMARY**| Supervisor koristi vlastiti interni jezični model (LLM) za sintetiziranje sažetka cijele interakcije i svih izlaza pod-agenta, te taj sažetak vraća kao konačni odgovor. Ovo pruža čist, objedinjeni odgovor korisniku. |
| **SCORED** | Sustav koristi interni LLM za ocjenjivanje kako LAST odgovora tako i SUMMARY sažetka interakcije u odnosu na izvorni korisnički zahtjev, vraćajući onaj izlaz koji je dobio višu ocjenu. |

Pogledajte [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) za kompletnu implementaciju.

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) i pitajte:
> - "Kako Supervisor odlučuje koje agente pozvati?"
> - "Koja je razlika između Supervisor i Sequential obrazaca tijeka rada?"
> - "Kako mogu prilagoditi ponašanje planiranja Supervisor-a?"

#### Razumijevanje izlaza

Kada pokrenete demo, vidjet ćete strukturirani prikaz kako Supervisor orkestrira više agenata. Evo što znači svaki odjeljak:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Zaglavlje** uvodi koncept tijeka rada: fokusirani lanac od čitanja datoteke do generiranja izvještaja.

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

**Dijagram tijeka rada** prikazuje protok podataka između agenata. Svaki agent ima specifičnu ulogu:
- **FileAgent** čita datoteke koristeći MCP alate i sprema sirovi sadržaj u `fileContent`
- **ReportAgent** koristi taj sadržaj i proizvodi strukturirani izvještaj u `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Korisnički zahtjev** prikazuje zadatak. Supervisor ga parsira i odlučuje pozvati FileAgent → ReportAgent.

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

**Supervisor Orkestracija** prikazuje 2-korak tijek u akciji:
1. **FileAgent** čita datoteku preko MCP-a i sprema sadržaj
2. **ReportAgent** prima sadržaj i generira strukturirani izvještaj

Supervisor je ove odluke donio **samostalno** na temelju korisničkog zahtjeva.

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

#### Objašnjenje značajki Agentic modula

Primjer demonstrira nekoliko naprednih značajki agentic modula. Pogledajmo bliže Agentic Scope i Agent Listeners.

**Agentic Scope** prikazuje zajedničku memoriju gdje agenti pohranjuju svoje rezultate koristeći `@Agent(outputKey="...")`. Ovo omogućuje:
- Kasnijim agentima pristup izlazima ranijih agenata
- Supervisor-u sintetiziranje konačnog odgovora
- Vama da pregledate što je svaki agent proizveo

Dijagram ispod pokazuje kako Agentic Scope radi kao zajednička memorija u tijeku datoteka-prema-izvještaju — FileAgent zapisuje svoj izlaz pod ključ `fileContent`, ReportAgent čita to i zapisuje svoj izlaz pod `report`:

<img src="../../../translated_images/hr/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope djeluje kao zajednička memorija — FileAgent zapisuje `fileContent`, ReportAgent to čita i zapisuje `report`, a vaš kod čita konačni rezultat.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Sirovi podaci datoteke iz FileAgent
String report = scope.readState("report");            // Strukturirani izvještaj iz ReportAgent
```

**Agent Listeners** omogućuju nadzor i otklanjanje pogrešaka pri izvršenju agenata. Korak-po-korak izlaz koji vidite u demo-u dolazi od AgentListener-a koji se povezuje na svaki poziv agenta:
- **beforeAgentInvocation** - Poziva se kada Supervisor odabere agenta, omogućujući pogledati koji je agent odabran i zašto
- **afterAgentInvocation** - Poziva se kada agent završi, prikazujući njegov rezultat
- **inheritedBySubagents** - Kada je true, listener prati sve agente u hijerarhiji

Sljedeći dijagram prikazuje puni životni ciklus Agent Listenera, uključujući kako `onError` obrađuje pogreške tijekom izvođenja agenta:

<img src="../../../translated_images/hr/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners se povezuju na životni ciklus izvršenja — nadziru kada agenti započnu, završe ili naiđu na pogreške.*

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
        return true; // Propagiraj na sve pod-agente
    }
};
```

Osim Supervisor obrasca, `langchain4j-agentic` modul pruža nekoliko moćnih obrazaca tijeka rada. Dijagram ispod prikazuje svih pet — od jednostavnih sekvencijalnih lanaca do odobrenja s ljudskim sudjelovanjem:

<img src="../../../translated_images/hr/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Pet obrazaca tijeka rada za orkestraciju agenata — od jednostavnih sekvencijalnih lanaca do tijekova odobrenja s ljudskim sudjelovanjem.*

| Obrazac        | Opis                              | Primjer uporabe                         |
|----------------|----------------------------------|---------------------------------------|
| **Sequential** | Izvršava agente redoslijedom, izlaz ide sljedećem | Pipeline: istraživanje → analiza → izvještaj |
| **Parallel**   | Pokreće agente simultano           | Neovisni zadaci: vremenska prognoza + vijesti + dionice |
| **Loop**       | Iterira dok se ne zadovolji uvjet | Procjena kvalitete: poboljšava dok rezultat nije ≥ 0.8 |
| **Conditional**| Usmjerava na temelju uvjeta       | Klasificira → usmjerava specijaliziranom agentu |
| **Human-in-the-Loop** | Dodaje ljudske provjere        | Tijekovi odobrenja, pregled sadržaja |

## Ključni pojmovi

Sad kad ste istražili MCP i agentic modul u praksi, sažmimo kada koristiti koji pristup.

Jedna od najvećih prednosti MCP-a je njegov rastući ekosustav. Dijagram ispod prikazuje kako jedan univerzalni protokol povezuje vašu AI aplikaciju s raznovrsnim MCP poslužiteljima — od pristupa datotečnom sustavu i bazama podataka do GitHub-a, e-pošte, web scrapinga i više:

<img src="../../../translated_images/hr/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP stvara univerzalni protokolni ekosustav — svaki MCP-kompatibilan poslužitelj radi s bilo kojim MCP-kompatibilnim klijentom, omogućujući dijeljenje alata između aplikacija.*

**MCP** je idealan kada želite iskoristiti postojeće ekosustave alata, graditi alate koje mogu dijeliti više aplikacija, integrirati usluge trećih strana putem standardnih protokola, ili zamjenjivati implementacije alata bez promjene koda.

**Agentic Modul** najbolje funkcionira kad želite deklarativne definicije agenata s `@Agent` anotacijama, trebate orkestraciju tijeka rada (sekvencijalnu, petlju, paralelnu), preferirate dizajn agenata baziran na sučeljima nad imperativnim kodom, ili kombinirate više agenata koji dijele izlaze preko `outputKey`.

**Supervisor Agent obrazac** sjaji kada tijek rada nije unaprijed predvidljiv i želite da LLM odlučuje, imate više specijaliziranih agenata kojima treba dinamička orkestracija, gradite konverzacijske sustave koji usmjeravaju na različite sposobnosti, ili želite najsvestranije, prilagodljivo ponašanje agenata.

Da bismo vam pomogli odlučiti između prilagođenih `@Tool` metoda iz Modula 04 i MCP alata iz ovog modula, sljedeća usporedba ističe ključne kompromise — prilagođeni alati pružaju usku integraciju i potpunu tipnu sigurnost za aplikacijsku logiku, dok MCP alati nude standardizirane, višekratno upotrebljive integracije:

<img src="../../../translated_images/hr/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Kad koristiti prilagođene @Tool metode nasuprot MCP alatima — prilagođeni alati za logiku specifičnu za aplikaciju s potpunom tipnom sigurnošću, MCP alati za standardizirane integracije koje rade preko aplikacija.*

## Čestitamo!

Prošli ste kroz svih pet modula tečaja LangChain4j za početnike! Evo pregleda cjelokupnog puta učenja koji ste završili — od osnovnog chata pa do agentnih sustava pokretanih MCP-om:

<img src="../../../translated_images/hr/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Vaše putovanje učenjem kroz svih pet modula — od osnovnog chata do agentnih sustava pokretanih MCP-om.*

Završili ste tečaj LangChain4j za početnike. Naučili ste:

- Kako graditi konverzacijski AI s memorijom (Modul 01)
- Uzorke za prompt inženjering za različite zadatke (Modul 02)
- Utemeljenje odgovora u vašim dokumentima s RAG-om (Modul 03)
- Kreiranje osnovnih AI agenata (asistenata) s prilagođenim alatima (Modul 04)
- Integraciju standardiziranih alata s LangChain4j MCP i Agentic modulima (Modul 05)

### Što dalje?

Nakon završetka modula, istražite [Vodič za testiranje](../docs/TESTING.md) kako biste vidjeli koncepte testiranja LangChain4j u praksi.

**Službeni resursi:**
- [LangChain4j Dokumentacija](https://docs.langchain4j.dev/) - Sveobuhvatni vodiči i referenca API-ja
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Izvorni kod i primjeri
- [LangChain4j Tutorijali](https://docs.langchain4j.dev/tutorials/) - Korak-po-korak upute za različite slučajeve uporabe

Hvala što ste završili ovaj tečaj!

---

**Navigacija:** [← Prethodno: Modul 04 - Alati](../04-tools/README.md) | [Natrag na početak](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Izjava o ograničenju odgovornosti**:
Ovaj je dokument preveden korištenjem AI usluge za prevođenje [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo osigurati točnost, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku smatra se autoritativnim izvorom. Za kritične informacije preporučuje se profesionalni ljudski prijevod. Nismo odgovorni za bilo kakva nesporazuma ili pogrešne interpretacije koje proizlaze iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->