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
    - [Kako nadzorni agent radi](../../../05-mcp)
    - [Strategije odgovora](../../../05-mcp)
    - [Razumijevanje izlaza](../../../05-mcp)
    - [Objašnjenje značajki agentnog modula](../../../05-mcp)
- [Ključni pojmovi](../../../05-mcp)
- [Čestitamo!](../../../05-mcp)
  - [Što slijedi?](../../../05-mcp)

## Što ćete naučiti

Izgradili ste konverzacijski AI, savladali upite, povezali odgovore s dokumentima i kreirali agente s alatima. No svi ti alati bili su prilagođeni vašoj specifičnoj aplikaciji. Što ako biste svojem AI-u mogli dati pristup standardiziranom ekosustavu alata koje svatko može stvoriti i dijeliti? U ovom modulu naučit ćete upravo to s Protokolom konteksta modela (MCP) i agentnim modulom LangChain4j-a. Prvo prikazujemo jednostavan MCP čitač datoteka, a zatim pokazujemo kako se lako integrira u napredne agentske procese koristeći obrazac Nadzornog agenta.

## Što je MCP?

Protokol konteksta modela (MCP) pruža upravo to - standardizirani način da AI aplikacije otkriju i koriste vanjske alate. Umjesto da pišete prilagođene integracije za svaki izvor podataka ili uslugu, povezujete se s MCP poslužiteljima koji izlažu svoje mogućnosti u dosljednom formatu. Vaš AI agent tada može automatski otkriti i koristiti te alate.

Dijagram u nastavku prikazuje razliku — bez MCP-a svaka integracija zahtijeva prilagođeno točka-do-točka povezivanje; s MCP-om, jedan protokol povezuje vašu aplikaciju s bilo kojim alatom:

<img src="../../../translated_images/hr/mcp-comparison.9129a881ecf10ff5.webp" alt="Usporedba MCP-a" width="800"/>

*Prije MCP-a: složene integracije točka-do-točka. Nakon MCP-a: jedan protokol, neograničene mogućnosti.*

MCP rješava temeljni problem u razvoju AI-a: svaka integracija je prilagođena. Želite li pristupiti GitHubu? Prilagođeni kod. Želite li čitati datoteke? Prilagođeni kod. Želite li upit u bazu podataka? Prilagođeni kod. I nijedna od tih integracija ne radi s drugim AI aplikacijama.

MCP standardizira ovo. MCP poslužitelj izlaže alate s jasnim opisima i shemama. Bilo koji MCP klijent može se povezati, otkriti dostupne alate i koristiti ih. Izgradite jednom, koristite svugdje.

Dijagram u nastavku ilustrira ovu arhitekturu — jedan MCP klijent (vaša AI aplikacija) povezuje se s više MCP poslužitelja, od kojih svaki izlaže vlastiti set alata kroz standardni protokol:

<img src="../../../translated_images/hr/mcp-architecture.b3156d787a4ceac9.webp" alt="Arhitektura MCP-a" width="800"/>

*Arhitektura Protokola konteksta modela - standardizirano otkrivanje i izvršenje alata*

## Kako MCP funkcionira

Ispod haube, MCP koristi slojevitu arhitekturu. Vaša Java aplikacija (MCP klijent) otkriva dostupne alate, šalje JSON-RPC zahtjeve preko transportnog sloja (Stdio ili HTTP), a MCP poslužitelj izvršava operacije i vraća rezultate. Sljedeći dijagram prikazuje svaki sloj ovog protokola:

<img src="../../../translated_images/hr/mcp-protocol-detail.01204e056f45308b.webp" alt="Detalji MCP protokola" width="800"/>

*Kako MCP radi ispod haube — klijenti otkrivaju alate, razmjenjuju JSON-RPC poruke i izvršavaju operacije preko transportnog sloja.*

**Arhitektura Poslužitelj-Klijent**

MCP koristi model poslužitelj-klijent. Poslužitelji pružaju alate - za čitanje datoteka, upite baza podataka, pozive API-ja. Klijenti (vaša AI aplikacija) se povezuju na poslužitelje i koriste njihove alate.

Za korištenje MCP-a s LangChain4j dodajte ovu Maven ovisnost:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Otkrivanje alata**

Kada se vaš klijent poveže s MCP poslužiteljem, pita "Koje alate imate?" Poslužitelj odgovara popisom dostupnih alata, svaki s opisima i shemama parametara. Vaš AI agent može zatim odlučiti koje alate koristiti prema zahtjevima korisnika. Dijagram u nastavku prikazuje taj dogovor — klijent šalje zahtjev `tools/list` i poslužitelj vraća dostupne alate s opisima i parametrima:

<img src="../../../translated_images/hr/tool-discovery.07760a8a301a7832.webp" alt="Otkrivanje MCP alata" width="800"/>

*AI otkriva dostupne alate pri pokretanju — sada zna koje mogućnosti postoje i može odlučiti koje koristiti.*

**Transportni mehanizmi**

MCP podržava različite transportne mehanizme. Dvije opcije su Stdio (za lokalnu komunikaciju podprocesa) i Streamable HTTP (za udaljene poslužitelje). Ovaj modul demonstrira Stdio transport:

<img src="../../../translated_images/hr/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transportni mehanizmi" width="800"/>

*Transportni mehanizmi MCP-a: HTTP za udaljene poslužitelje, Stdio za lokalne procese*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Za lokalne procese. Vaša aplikacija pokreće poslužitelj kao podproces i komunicira kroz standardni ulaz/izlaz. Korisno za pristup datotečnom sustavu ili alatima naredbenog retka.

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

Poslužitelj `@modelcontextprotocol/server-filesystem` izlaže sljedeće alate, svi ograničeni na mape koje navedete:

| Alat | Opis |
|------|-------|
| `read_file` | Čita sadržaj jedne datoteke |
| `read_multiple_files` | Čita više datoteka u jednom pozivu |
| `write_file` | Stvara ili prepisuje datoteku |
| `edit_file` | Izvršava ciljane izmjene pronalaženjem i zamjenom |
| `list_directory` | Nabrajanje datoteka i mapa na lokaciji |
| `search_files` | Rekurzivno pretraživanje datoteka po uzorku |
| `get_file_info` | Dohvaća metapodatke datoteke (veličina, vremenske oznake, dozvole) |
| `create_directory` | Stvara mapu (uključujući roditeljske mape) |
| `move_file` | Premješta ili preimenuje datoteku ili mapu |

Sljedeći dijagram prikazuje kako Stdio transport radi u runtimeu — vaša Java aplikacija pokreće MCP poslužitelj kao podproces i oni komuniciraju putem cijevi stdin/stdout, bez mreže ili HTTP-a:

<img src="../../../translated_images/hr/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Tok Stdio transporta" width="800"/>

*Stdio transport u akciji — vaša aplikacija pokreće MCP poslužitelj kao podproces i komunicira putem cijevi stdin/stdout.*

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) i pitajte:
> - "Kako Stdio transport radi i kada ga treba koristiti u odnosu na HTTP?"
> - "Kako LangChain4j upravlja životnim ciklusom pokrenutih MCP poslužiteljskih procesa?"
> - "Koje su sigurnosne implikacije davanja AI pristupa datotečnom sustavu?"

## Agentni modul

Dok MCP pruža standardizirane alate, LangChain4j-ov **agentni modul** pruža deklarativan način za izgradnju agenata koji orkestriraju te alate. `@Agent` anotacija i `AgenticServices` omogućuju definiranje ponašanja agenta preko sučelja umjesto imperativnog koda.

U ovom modulu istražujete obrazac **Nadzornog agenta** — napredni agentski AI pristup gdje "nadzorni" agent dinamički odlučuje koje pod-agente pozvati na temelju korisničkih zahtjeva. Kombinirat ćemo oba koncepta dajeći jednom od pod-agenta MCP-potpomognute mogućnosti pristupa datotekama.

Za korištenje agentnog modula dodajte ovu Maven ovisnost:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Napomena:** Modul `langchain4j-agentic` koristi zasebnu verzijsku varijablu (`langchain4j.mcp.version`) jer se objavljuje u drugom rasporedu od glavnih LangChain4j biblioteka.

> **⚠️ Eksperimentalno:** Modul `langchain4j-agentic` je **eksperimentalan** i može se mijenjati. Stabilan način za izgradnju AI asistenata i dalje je `langchain4j-core` s prilagođenim alatima (Modul 04).

## Pokretanje primjera

### Preduvjeti

- Završeni [Modul 04 - Alati](../04-tools/README.md) (ovaj modul nadograđuje koncepte prilagođenih alata i uspoređuje s MCP alatima)
- `.env` datoteka u korijenskom direktoriju s Azure vjerodajnicama (kreirana komandom `azd up` u Modulu 01)
- Java 21+, Maven 3.9+
- Node.js 16+ i npm (za MCP poslužitelje)

> **Napomena:** Ako još niste konfigurirali varijable okoline, pogledajte [Modul 01 - Uvod](../01-introduction/README.md) za upute oko postavljanja (`azd up` automatski kreira `.env` datoteku), ili kopirajte `.env.example` u `.env` u korijenskom direktoriju i popunite svoje vrijednosti.

## Brzi početak

**Korištenje VS Code:** Jednostavno kliknite desnom tipkom miša na bilo koju demo datoteku u Exploreru i odaberite **"Run Java"**, ili koristite konfiguracije za pokretanje iz panela Run and Debug (prvo pripazite da je `.env` datoteka konfigurirana sa Azure vjerodajnicama).

**Korištenje Mavena:** Alternativno, možete pokrenuti iz naredbenog retka s dolje navedenim primjerima.

### Rad s datotekama (Stdio)

Ovo demonstrira alate temeljene na lokalnim podprocesima.

**✅ Nema potrebnih preduvjeta** - MCP poslužitelj se pokreće automatski.

**Korištenje start skripti (Preporučeno):**

Start skripte automatski učitavaju varijable okoline iz korijenske `.env` datoteke:

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

**Korištenje VS Code:** Kliknite desnom tipkom miša na `StdioTransportDemo.java` i odaberite **"Run Java"** (pazite da je vaša `.env` datoteka konfigurirana).

Aplikacija automatski pokreće MCP poslužitelj za datotečni sustav i čita lokalnu datoteku. Primijetite kako se upravljanje podprocesima rješava automatski.

**Očekivani izlaz:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Nadzorni agent

**Obrazac Nadzornog agenta** je **fleksibilan** oblik agentskog AI-a. Nadzornik koristi LLM da autonomno odluči koje agente pozvati na temelju korisničkog zahtjeva. U sljedećem primjeru kombiniramo MCP-potpomognuti pristup datotekama s LLM agentom da kreiramo nadzirani tijek čitanja datoteke → izvješća.

U demonstraciji, `FileAgent` čita datoteku koristeći MCP alate datotečnog sustava, a `ReportAgent` generira strukturirani izvještaj s izvršnim sažetkom (1 rečenica), 3 ključne točke i preporukama. Nadzorni agent orkestrira taj tijek automatski:

<img src="../../../translated_images/hr/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Obrazac nadzornog agenta" width="800"/>

*Nadzorni agent koristi svoj LLM da odluči koje agente pozvati i kojim redoslijedom — nije potrebna hardkodirana ruta.*

Evo kako konkretan tijek rada izgleda za našu datoteku-u-izvještaj obradu:

<img src="../../../translated_images/hr/file-report-workflow.649bb7a896800de9.webp" alt="Tijek rada datoteka u izvještaj" width="800"/>

*FileAgent čita datoteku preko MCP alata, zatim ReportAgent pretvara sirovi sadržaj u strukturirani izvještaj.*

Svaki agent pohranjuje svoj izlaz u **Agentni prostor** (zajednička memorija), što omogućava sljedećim agentima pristup prethodnim rezultatima. Ovo demonstrira kako se MCP alati neprimjetno uklapaju u agentske tijekove — Nadzorni agent ne mora znati *kako* se datoteke čitaju, samo da to `FileAgent` može napraviti.

#### Pokretanje demonstracije

Start skripte automatski učitavaju varijable okoline iz korijenske `.env` datoteke:

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

**Korištenje VS Code:** Kliknite desnom tipkom miša na `SupervisorAgentDemo.java` i odaberite **"Run Java"** (pazite da je `.env` datoteka konfigurirana).

#### Kako nadzorni agent radi

Prije nego što izgradite agente, morate povezati MCP transport s klijentom i umotati ga kao `ToolProvider`. Ovako alati MCP poslužitelja postaju dostupni vašim agentima:

```java
// Kreirajte MCP klijenta iz transporta
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Omotajte klijenta kao ToolProvider — ovo povezuje MCP alate u LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Sada možete ubaciti `mcpToolProvider` u bilo koji agent kojem trebaju MCP alati:

```java
// Korak 1: FileAgent čita datoteke koristeći MCP alate
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Ima MCP alate za rad s datotekama
        .build();

// Korak 2: ReportAgent generira strukturirane izvještaje
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor koordinira radni tijek datoteka → izvještaj
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Vrati konačni izvještaj
        .build();

// Supervisor odlučuje koje agente pozvati na temelju zahtjeva
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Strategije odgovora

Kada konfigurirate `SupervisorAgent`, određujete kako treba formulirati svoj konačni odgovor korisniku nakon što su pod-agenti završili zadatke. Dijagram u nastavku prikazuje tri dostupne strategije — LAST vraća konačni izlaz zadnjeg agenta, SUMMARY sintetizira sve izlaze putem LLM-a, a SCORED bira opciju s višim rezultatom u odnosu na izvorni zahtjev:

<img src="../../../translated_images/hr/response-strategies.3d0cea19d096bdf9.webp" alt="Strategije odgovora" width="800"/>

*Tri strategije kako Nadzorni agent formulira svoj završni odgovor — odaberite prema tome želite li izlaz zadnjeg agenta, sintetizirani sažetak ili opciju s najboljom ocjenom.*

Dostupne strategije su:

| Strategija | Opis |
|------------|-------|
| **LAST** | Nadzornik vraća izlaz zadnjeg pozvanog pod-agenta ili alata. Korisno je kada je zadnji agent u tijeku namijenjen proizvesti kompletan, konačni odgovor (npr. "Agent za sažetak" u istraživačkom tijeku). |
| **SUMMARY** | Nadzornik koristi svoj interni jezični model (LLM) za sintetiziranje sažetka cijele interakcije i svih izlaza pod-agenta, zatim vraća taj sažetak kao konačni odgovor. Ovo pruža čist, objedinjeni odgovor korisniku. |
| **SCORED** | Sustav koristi interni LLM da ocijeni i LAST odgovor i SUMMARY sažetak interakcije u odnosu na izvorni zahtjev korisnika, te vraća izlaz koji dobije višu ocjenu. |
Pogledajte [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) za potpunu implementaciju.

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) i pitajte:
> - "Kako Supervisor odlučuje koje agente pozvati?"
> - "Koja je razlika između Supervisor i Sequential obrazaca tijeka rada?"
> - "Kako mogu prilagoditi Supervisorovo ponašanje planiranja?"

#### Razumijevanje izlaza

Kad pokrenete demo, vidjet ćete strukturirani pregled kako Supervisor orkestrira više agenata. Evo što svaki odjeljak znači:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Zaglavlje** uvodi koncept tijeka rada: fokusirani proces od čitanja datoteke do generiranja izvještaja.

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

**Dijagram tijeka rada** prikazuje tok podataka između agenata. Svaki agent ima specifičnu ulogu:
- **FileAgent** čita datoteke koristeći MCP alate i pohranjuje sirovi sadržaj u `fileContent`
- **ReportAgent** koristi taj sadržaj i proizvodi strukturirani izvještaj u `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Zahtjev korisnika** prikazuje zadatak. Supervisor analizira ovaj zahtjev i odlučuje pozvati FileAgent → ReportAgent.

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

**Orkestracija Supervizora** prikazuje tok u 2 koraka u akciji:
1. **FileAgent** čita datoteku putem MCP i sprema sadržaj
2. **ReportAgent** prima sadržaj i generira strukturirani izvještaj

Supervisor je ove odluke donio **autonomno** na temelju zahtjeva korisnika.

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

#### Objašnjenje značajki agentnog modula

Primjer demonstrira nekoliko naprednih značajki agentnog modula. Pogledajmo bliže Agentic Scope i Agent Listeners.

**Agentic Scope** prikazuje zajedničku memoriju gdje su agenti pohranili svoje rezultate koristeći `@Agent(outputKey="...")`. To omogućuje:
- Kasnijim agentima pristup rezultatima ranijih agenata
- Supervisoru sintetizirati konačni odgovor
- Vama pregledati što je svaki agent proizveo

Donji dijagram prikazuje kako Agentic Scope funkcionira kao zajednička memorija u tijeku rada od datoteke do izvještaja — FileAgent zapisuje svoj izlaz pod ključem `fileContent`, ReportAgent čita to i zapisuje vlastiti izlaz pod `report`:

<img src="../../../translated_images/hr/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope djeluje kao zajednička memorija — FileAgent zapisuje `fileContent`, ReportAgent čita i zapisuje `report`, a vaš kod čita konačni rezultat.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Neobrađeni podaci iz datoteke od FileAgent
String report = scope.readState("report");            // Strukturirani izvještaj od ReportAgent
```

**Agent Listeners** omogućuju praćenje i otklanjanje pogrešaka pri izvršavanju agenata. Izlaz korak-po-korak koji vidite u demo verziji dolazi iz AgentListenera koji se povezuje s svakim pozivom agenta:
- **beforeAgentInvocation** - Poziva se kada Supervisor odabere agenta, omogućujući vam da vidite koji je agent izabran i zašto
- **afterAgentInvocation** - Poziva se kada agent završi, prikazujući njegov rezultat
- **inheritedBySubagents** - Kada je true, listener nadgleda sve agente u hijerarhiji

Sljedeći dijagram prikazuje cijeli životni ciklus Agent Listener-a, uključujući kako `onError` obrađuje pogreške tijekom izvršavanja agenta:

<img src="../../../translated_images/hr/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners se povezuju s životnim ciklusom izvršavanja — prate kada agenti počinju, završavaju ili nailaze na pogreške.*

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
        return true; // Proširi na sve pod-agente
    }
};
```

Osim Supervisor obrasca, modul `langchain4j-agentic` pruža nekoliko moćnih obrazaca tijeka rada. Donji dijagram prikazuje svih pet — od jednostavnih sekvencijalnih procesa do odobrenja s uključenjem čovjeka:

<img src="../../../translated_images/hr/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Pet obrazaca tijeka rada za orkestraciju agenata — od jednostavnih sekvencijalnih procesa do odobrenja s uključenjem čovjeka.*

| Obrazac | Opis | Primjena |
|---------|-------|----------|
| **Sekvencijalni** | Izvršava agente redom, izlaz prelazi na sljedećeg | Procesi: istraživanje → analiza → izvještaj |
| **Paralelni** | Pokreće agente istovremeno | Nezavisni zadaci: vrijeme + vijesti + dionice |
| **Petlja** | Ponavlja dok se ne ispuni uvjet | Procjena kvalitete: poboljštava dok rezultat ≥ 0.8 |
| **Uvjetni** | Usmjerava na temelju uvjeta | Klasificira → usmjerava specijalističkom agentu |
| **Čovjek u petlji** | Dodaje ljudske provjere | Tijekovi odobrenja, pregled sadržaja |

## Ključni pojmovi

Sad kad ste istražili MCP i agentni modul u praksi, sažmimo kada koristiti koji pristup.

Jedna od najvećih prednosti MCP-a je njegova rastuća ekosfera. Donji dijagram prikazuje kako jedan univerzalni protokol povezuje vašu AI aplikaciju s širokom lepezom MCP servera — od pristupa datotečnom sustavu i bazama podataka do GitHub-a, e-pošte, web scraping-a i više:

<img src="../../../translated_images/hr/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP stvara univerzalnu ekosferu protokola — svaki MCP-kompatibilni server radi s bilo kojim MCP-klijentom, omogućujući dijeljenje alata među aplikacijama.*

**MCP** je idealan kad želite iskoristiti postojeće ekosustave alata, izgraditi alate koje više aplikacija može zajednički koristiti, integrirati usluge trećih strana standardnim protokolima ili mijenjati implementacije alata bez promjene koda.

**Agentic Modul** najbolje funkcionira kada želite deklarativne definicije agenata s `@Agent` oznakama, trebate orkestriranje tijeka rada (sekvencijalno, petlja, paralelno), preferirate dizajn agenata temeljen na sučeljima umjesto imperativnog koda ili kombinirate više agenata koji međusobno dijele izlaz putem `outputKey`.

**Obrazac Supervisor Agenta** sjaji kad tijek rada nije predvidiv unaprijed i želite da LLM odluči, kad imate više specijaliziranih agenata koji trebaju dinamičku orkestraciju, kod izgradnje konverzacijskih sustava koji usmjeravaju prema različitim mogućnostima ili kada želite najfleksibilnije i najprilagodljivije ponašanje agenta.

Da vam pomognemo odlučiti između prilagođenih `@Tool` metoda iz Modula 04 i MCP alata iz ovog modula, sljedeća usporedba ističe ključne kompromise — prilagođeni alati nude čvrstu povezanost i punu tipnu sigurnost za specifičnu logiku aplikacije, dok MCP alati pružaju standardizirane, višekratne integracije:

<img src="../../../translated_images/hr/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Kad koristiti prilagođene @Tool metode, a kad MCP alate — prilagođeni alati za app-specifičnu logiku s punom tipnom sigurnošću, MCP alati za standardizirane integracije koje rade kroz aplikacije.*

## Čestitamo!

Prošli ste kroz svih pet modula tečaja LangChain4j za početnike! Evo pregleda cjelokupnog učenju putovanja koje ste završili — od osnovnog chata do agentnih sustava pokretanih MCP-om:

<img src="../../../translated_images/hr/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Vaše putovanje učenjem kroz svih pet modula — od osnovnog chata do agentnih sustava pokretanih MCP-om.*

Završili ste tečaj LangChain4j za početnike. Naučili ste:

- Kako graditi konverzacijsku AI s memorijom (Modul 01)
- Obrasce dizajnerskog prompta za različite zadatke (Modul 02)
- Povezivanje odgovora s vašim dokumentima pomoću RAG-a (Modul 03)
- Izradu osnovnih AI agenata (pomoćnika) s prilagođenim alatima (Modul 04)
- Integraciju standardiziranih alata s LangChain4j MCP i Agentic modulima (Modul 05)

### Što dalje?

Nakon završetka modula, istražite [Vodič za testiranje](../docs/TESTING.md) da vidite koncepte testiranja LangChain4j u praksi.

**Službeni resursi:**
- [Dokumentacija LangChain4j](https://docs.langchain4j.dev/) - Sveobuhvatni vodiči i API referenca
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Izvorni kod i primjeri
- [LangChain4j Tutorijali](https://docs.langchain4j.dev/tutorials/) - Korak-po-korak tutorijali za različite slučajeve upotrebe

Hvala što ste prošli ovaj tečaj!

---

**Navigacija:** [← Prethodno: Modul 04 - Alati](../04-tools/README.md) | [Natrag na početak](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Odricanje od odgovornosti**:
Ovaj dokument je preveden pomoću AI prevoditeljskog servisa [Co-op Translator](https://github.com/Azure/co-op-translator). Iako težimo točnosti, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba se smatrati službenim i autoritativnim izvorom. Za kritične informacije preporučuje se profesionalni ljudski prijevod. Nismo odgovorni za bilo kakve nesporazume ili pogrešna tumačenja koja proizlaze iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->