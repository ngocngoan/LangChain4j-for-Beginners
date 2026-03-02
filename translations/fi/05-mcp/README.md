# Moduuli 05: Model Context Protocol (MCP)

## Sisällysluettelo

- [Mitä opit](../../../05-mcp)
- [Mikä on MCP?](../../../05-mcp)
- [Miten MCP toimii](../../../05-mcp)
- [Agenttimoduuli](../../../05-mcp)
- [Esimerkkien suorittaminen](../../../05-mcp)
  - [Edellytykset](../../../05-mcp)
- [Pika-aloitus](../../../05-mcp)
  - [Tiedostotoiminnot (Stdio)](../../../05-mcp)
  - [Valvoja-agentti](../../../05-mcp)
    - [Esittelyn suorittaminen](../../../05-mcp)
    - [Miten valvoja toimii](../../../05-mcp)
    - [Vastausstrategiat](../../../05-mcp)
    - [Tuloksen tulkinta](../../../05-mcp)
    - [Agenttimoduulin ominaisuuksien selitys](../../../05-mcp)
- [Keskeiset käsitteet](../../../05-mcp)
- [Onneksi olkoon!](../../../05-mcp)
  - [Mitä seuraavaksi?](../../../05-mcp)

## Mitä opit

Olet rakentanut keskustelevaa tekoälyä, hallinnut kehotteita, juurruttanut vastaukset dokumentteihin ja luonut agenteja työkaluilla. Mutta kaikki nämä työkalut olivat räätälöityjä juuri sinun sovellukseesi. Entä jos voisit antaa tekoälyllesi pääsyn standardoituihin työkaluekosysteemeihin, joita kuka tahansa voi luoda ja jakaa? Tässä moduulissa opit tekemään juuri tämän Model Context Protocolin (MCP) ja LangChain4j:n agenttimoduulin avulla. Esittelemme ensin yksinkertaisen MCP-tiedostonlukijan ja näytämme, miten se helposti integroidaan edistyneisiin agenttiprosesseihin Supervisor Agent -kuvion avulla.

## Mikä on MCP?

Model Context Protocol (MCP) tarjoaa juuri tämän – standardimenetelmän tekoälysovelluksille löytää ja käyttää ulkoisia työkaluja. Sen sijaan, että kirjoittaisit räätälöityjä integraatioita jokaiselle datalähteelle tai palvelulle, yhdistyt MCP-palvelimiin, jotka tarjoavat kyvykkyytensä johdonmukaisessa muodossa. Tekoälyagenttisi voi sitten löytää ja käyttää näitä työkaluja automaattisesti.

Alla oleva kaavio näyttää eron — ilman MCP:tä jokainen integraatio vaatii räätälöityä pisteestä pisteeseen kytkentää; MCP:n kanssa yksi protokolla yhdistää sovelluksesi mihin tahansa työkaluun:

<img src="../../../translated_images/fi/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Ennen MCP:tä: Monimutkaiset pisteestä pisteeseen integraatiot. MCP:n jälkeen: Yksi protokolla, loputtomat mahdollisuudet.*

MCP ratkaisee perustavanlaatuisen ongelman tekoälyn kehittämisessä: kaikki integraatiot ovat räätälöityjä. Haluatko käyttää GitHubia? Räätälöity koodi. Haluatko lukea tiedostoja? Räätälöity koodi. Haluatko kysyä tietokantaa? Räätälöity koodi. Eikä mikään näistä integraatioista toimi muiden tekoälysovellusten kanssa.

MCP standardoi tämän. MCP-palvelin paljastaa työkalut selkeillä kuvauksilla ja skeemoilla. Mikä tahansa MCP-asiakas voi yhdistyä, löytää käytettävissä olevat työkalut ja käyttää niitä. Rakenna kerran, käytä kaikkialla.

Alla oleva kaavio havainnollistaa tätä arkkitehtuuria — yksittäinen MCP-asiakas (tekoälysovelluksesi) yhdistyy useisiin MCP-palvelimiin, joista jokainen tarjoaa oman työkalusarjansa standardoidun protokollan kautta:

<img src="../../../translated_images/fi/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocolin arkkitehtuuri – standardoitu työkalujen löytäminen ja suoritus*

## Miten MCP toimii

Taustalla MCP käyttää kerrostettua arkkitehtuuria. Java-sovelluksesi (MCP-asiakas) löytää käytettävissä olevat työkalut, lähettää JSON-RPC-pyyntöjä kuljetuskerroksen (Stdio tai HTTP) kautta, ja MCP-palvelin suorittaa toiminnot ja palauttaa tulokset. Seuraava kaavio jäsentää tämän protokollan jokaisen kerroksen:

<img src="../../../translated_images/fi/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Miten MCP toimii taustalla — asiakkaat löytävät työkaluja, vaihtavat JSON-RPC-viestejä ja suorittavat toimintoja kuljetuskerroksen kautta.*

**Palvelin-asiakasarkkitehtuuri**

MCP käyttää palvelin-asiakas-mallia. Palvelimet tarjoavat työkaluja – tiedostojen lukemista, tietokantojen kyselyä, API-kutsuja. Asiakkaat (tekoälysovelluksesi) yhdistyvät palvelimiin ja käyttävät niiden työkaluja.

Käyttääksesi MCP:tä LangChain4j:n kanssa, lisää tämä Maven-riippuvuus:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Työkalujen löytäminen**

Kun asiakkaasi yhdistyy MCP-palvelimeen, se kysyy "Mitkä työkalut teillä on?" Palvelin vastaa listalla käytettävissä olevista työkaluista, joilla on kuvaukset ja parametrien skeemat. Tekoälyagenttisi voi sitten päättää, mitä työkaluja käyttää käyttäjän pyyntöjen perusteella. Alla oleva kaavio näyttää tämän kädenpuristuksen — asiakas lähettää `tools/list` -pyynnön ja palvelin palauttaa käytettävissä olevat työkalut kuvauksineen ja parametrien skeemoineen:

<img src="../../../translated_images/fi/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*Tekoäly löytää käytettävissä olevat työkalut käynnistyksessä — se tietää nyt, mitä kyvykkyyksiä on saatavilla ja voi päättää, mitä käyttää.*

**Kuljetusmekanismit**

MCP tukee erilaisia kuljetusmekanismeja. Kaksi vaihtoehtoa ovat Stdio (paikallinen aliohjelmaprosessien kommunikointi) ja Streamable HTTP (etäpalvelimille). Tämä moduuli demonstroi Stdio-kuljetusta:

<img src="../../../translated_images/fi/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP:n kuljetusmekanismit: HTTP etäpalvelimille, Stdio paikallisille prosesseille*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Paikallisia prosesseja varten. Sovelluksesi käynnistää palvelimen aliohjelmaprosessina ja kommunikoi tavallisen syötön/ulostulon kautta. Hyödyllinen tiedostojärjestelmän pääsyyn tai komentorivityökaluihin.

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

`@modelcontextprotocol/server-filesystem` -palvelin tarjoaa seuraavat työkalut, kaikki rajattu määrittämiisi hakemistoihin:

| Työkalu | Kuvaus |
|------|-------------|
| `read_file` | Lue yhden tiedoston sisältö |
| `read_multiple_files` | Lue useita tiedostoja yhdessä kutsussa |
| `write_file` | Luo tai korvaa tiedoston |
| `edit_file` | Tee kohdistettuja korvaushakuja |
| `list_directory` | Listaa tiedostot ja hakemistot polussa |
| `search_files` | Hae rekursiivisesti tiedostoja, jotka vastaavat mallia |
| `get_file_info` | Hanki tiedoston metatiedot (koko, aikaleimat, oikeudet) |
| `create_directory` | Luo hakemisto (mukaan lukien yläkansiot) |
| `move_file` | Siirrä tai nimeä tiedosto tai hakemisto uudelleen |

Seuraava kaavio näyttää, miten Stdio-kuljetus toimii ajon aikana — Java-sovelluksesi käynnistää MCP-palvelimen lapsiprosessina ja ne kommunikoivat stdin/stdout-putkien kautta, ilman verkkoa tai HTTP:tä:

<img src="../../../translated_images/fi/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio-kuljetus toiminnassa — sovelluksesi käynnistää MCP-palvelimen lapsiprosessina ja kommunikoi stdin/stdout-putkien kautta.*

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatin kanssa:** Avaa [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) ja kysy:
> - "Miten Stdio-kuljetus toimii ja milloin se kannattaa valita HTTP:n sijaan?"
> - "Miten LangChain4j hallinnoi käynnistettyjen MCP-palvelinprosessien elinkaarta?"
> - "Mitkä ovat tietoturvariskit, kun annetaan tekoälyn käyttää tiedostojärjestelmää?"

## Agenttimoduuli

Vaikka MCP tarjoaa standardoidut työkalut, LangChain4j:n **agenttimoduuli** tarjoaa deklaratiivisen tavan rakentaa agenteja, jotka orkestroivat näitä työkaluja. `@Agent`-annotaatio ja `AgenticServices` mahdollistavat agentin käyttäytymisen määrittämisen rajapintojen avulla imperatiivisen koodin sijaan.

Tässä moduulissa tutustut **Supervisor Agent** -kuvioon — edistyneeseen agenttipohjaiseen tekoälymenetelmään, jossa "valvoja" agentti päättää dynaamisesti, mitä aliagentteja kutsutaan käyttäjän pyyntöjen perusteella. Yhdistämme molemmat konseptit antamalla yhdelle aliagentille MCP-pohjaiset tiedostojen käsittelyominaisuudet.

Käyttääksesi agenttimoduulia, lisää tämä Maven-riippuvuus:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Huom:** `langchain4j-agentic`-moduuli käyttää erillistä versiokenttää (`langchain4j.mcp.version`), koska sen julkaisuaikataulu poikkeaa LangChain4j:n ydinkirjastoista.

> **⚠️ Kokeellinen:** `langchain4j-agentic`-moduuli on **kokeellinen** ja voi muuttua. Vakaampi tapa rakentaa tekoälyavustajia on edelleen `langchain4j-core` räätälöidyillä työkaluilla (Moduuli 04).

## Esimerkkien suorittaminen

### Edellytykset

- Suoritettu [Moduuli 04 - Työkalut](../04-tools/README.md) (tämä moduuli rakentaa räätälöityjen työkalujen konsepteille ja vertaa niitä MCP-työkaluihin)
- `.env`-tiedosto juurihakemistossa Azure-tunnuksilla (luotu `azd up` -komennolla Moduulissa 01)
- Java 21+, Maven 3.9+
- Node.js 16+ ja npm (MCP-palvelimia varten)

> **Huom:** Jos et ole vielä määrittänyt ympäristömuuttujiasi, katso [Moduuli 01 - Johdanto](../01-introduction/README.md) käyttöönotto-ohjeet (`azd up` luo `.env`-tiedoston automaattisesti), tai kopioi `.env.example` tiedostoksi `.env` juurihakemistoon ja täytä arvosi.

## Pika-aloitus

**VS Code -käytössä:** Napsauta hiiren oikealla mitä tahansa demon tiedostoa Explorerissa ja valitse **"Run Java"**, tai käytä suoritus- ja debug-konfiguraatioita Run and Debug -paneelissa (varmista, että `.env`-tiedostosi on ensin määritetty Azure-tunnuksilla).

**Mavenin käyttö:** Vaihtoehtoisesti voit suorittaa komentoja komentoriviltä alla olevien esimerkkien mukaisesti.

### Tiedostotoiminnot (Stdio)

Tämä demonstroi paikallisiin aliohjelmaprosesseihin perustuvia työkaluja.

**✅ Ei edellytyksiä** - MCP-palvelin käynnistetään automaattisesti.

**Käyttämällä aloitusskriptejä (suositeltu):**

Aloitusskriptit lataavat automaattisesti ympäristömuuttujat juurihakemiston `.env`-tiedostosta:

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

**VS Code -käytössä:** Napsauta hiiren oikealla `StdioTransportDemo.java` ja valitse **"Run Java"** (varmista, että `.env` on määritetty).

Sovellus käynnistää tiedostojärjestelmän MCP-palvelimen automaattisesti ja lukee paikallisen tiedoston. Huomioi, miten aliohjelmaprosessin hallinta hoidetaan puolestasi.

**Odotettu tulos:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Valvoja-agentti

**Valvoja-agenttimalli** on **joustava** agenttipohjaisen tekoälyn muoto. Valvoja käyttää LLM:ää päättääkseen itsenäisesti, mitä agenteja kutsutaan käyttäjän pyynnön perusteella. Seuraavassa esimerkissä yhdistämme MCP-pohjaisen tiedostojen käsittelyn ja LLM-agentin luodaksemme valvotun tiedoston luku → raportin koostamistyönkulun.

Demossa `FileAgent` lukee tiedoston MCP-tiedostojärjestelmätyökaluilla, ja `ReportAgent` luo rakenteellisen raportin toimeenpanevan yhteenvedon (1 lause), 3 avainkohtaa ja suositukset. Valvoja orkestroi tämän työnkulun automaattisesti:

<img src="../../../translated_images/fi/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Valvoja käyttää LLM:ään päättääkseen, mitä agenteja kutsutaan ja missä järjestyksessä — ei tarvetta kovakoodatulle reititykselle.*

Näin konkreettinen työnkulku näyttää tiedostosta raporttiin putkelle:

<img src="../../../translated_images/fi/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent lukee tiedoston MCP-työkalujen kautta, ja ReportAgent muuttaa raakasisällön rakenteelliseksi raportiksi.*

Jokainen agentti tallentaa tuloksensa **Agentic Scopeen** (jaettuun muistiin), mahdollistaen jälkimmäisten agenttien pääsyn aikaisempiin tuloksiin. Tämä havainnollistaa, miten MCP-työkalut integroituvat saumattomasti agenttiprosesseihin — Valvojan ei tarvitse tietää *miten* tiedostot luetaan, vaan vain että `FileAgent` osaa tehdä sen.

#### Esittelyn suorittaminen

Aloitusskriptit lataavat automaattisesti ympäristömuuttujat juurihakemiston `.env`-tiedostosta:

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

**VS Code -käytössä:** Napsauta hiiren oikealla `SupervisorAgentDemo.java` ja valitse **"Run Java"** (varmista, että `.env` on määritetty).

#### Miten valvoja toimii

Ennen agenttien rakentamista sinun tulee yhdistää MCP-kuljetus asiakkaaseen ja kääriä se `ToolProvider`iksi. Näin MCP-palvelimen työkalut tulevat saataville agenteillesi:

```java
// Luo MCP-asiakas siirtokerroksesta
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Kääri asiakas ToolProvideriksi — tämä yhdistää MCP-työkalut LangChain4j:hin
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Nyt voit injektoida `mcpToolProvider`-instanssin mihin tahansa agenttiin, joka tarvitsee MCP-työkaluja:

```java
// Vaihe 1: FileAgent lukee tiedostoja MCP-työkaluilla
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Sisältää MCP-työkaluja tiedostojen käsittelyyn
        .build();

// Vaihe 2: ReportAgent luo jäsenneltyjä raportteja
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor koordinoi tiedosto → raportti -työnkulun
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Palauta lopullinen raportti
        .build();

// Supervisor päättää pyynnön perusteella, mitä agentteja kutsutaan
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Vastausstrategiat

Kun määrität `SupervisorAgentin`, määrittelet miten se laatii lopullisen vastauksen käyttäjälle alikäsittelijöiden tehtyjen toimintojen jälkeen. Alla oleva kaavio näyttää kolme saatavilla olevaa strategiaa — LAST palauttaa suoraan viimeisen agentin tuloksen, SUMMARY tiivistää kaikki vastaukset LLM:llä ja SCORED valitsee korkeamman pistemäärän alkuperäisen pyynnön perusteella:

<img src="../../../translated_images/fi/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Kolme strategiaa siitä, miten Valvoja muodostaa lopullisen vastauksensa — valitse haluatko viimeisen agentin tuloksen, tiivistetyn yhteenvedon vai parhaan pisteytyksen.*

Saatavilla olevat strategiat ovat:

| Strategia | Kuvaus |
|----------|-------------|
| **LAST** | Valvoja palauttaa viimeisen alikäsittelijän tai työkalun tuottaman tuloksen. Tämä on hyödyllistä, kun työnkulun viimeinen agentti on nimenomaan suunniteltu tuottamaan täydellinen loppuvastaus (esim. "Yhteenvetoagentti" tutkimusputkessa). |
| **SUMMARY** | Valvoja käyttää sisäistä kielimalliaan (LLM) tiivistämään koko vuorovaikutuksen ja kaikkien aliagenttien vastaukset yhteen ja palauttaa tämän tiivistelmän lopullisena vastauksena. Tämä tarjoaa käyttäjälle selkeän, koottavan vastauksen. |
| **SCORED** | Järjestelmä käyttää sisäistä LLM:ää pisteyttämään sekä LAST-vastauksen että SUMMARY-tiivistelmän käyttäjän alkuperäistä pyyntöä vastaan ja palauttaa sen tuloksen, jolla on korkeampi pistemäärä. |
Katso täydellinen toteutus tiedostosta [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java).

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatilla:** Avaa [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) ja kysy:
> - "Miten Supervisor päättää, mitä agentteja kutsutaan?"
> - "Mikä on ero Supervisor- ja Sequential-työvuorokuvioiden välillä?"
> - "Miten voin räätälöidä Supervisorin suunnittelukäyttäytymistä?"

#### Tulosteen ymmärtäminen

Kun ajat demon, näet jäsennellyn läpikäynnin siitä, miten Supervisor orkestroi useita agentteja. Tässä, mitä kukin osio tarkoittaa:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Otsikko** esittelee työnkulun käsitteen: tarkennettu putkisto tiedoston lukemisesta raportin luomiseen.

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

**Työnkulun Kaavio** näyttää tiedon kulun agenttien välillä. Jokaisella agentilla on tietty rooli:
- **FileAgent** lukee tiedostoja MCP-työkaluilla ja tallentaa raakadatan muuttujaan `fileContent`
- **ReportAgent** käyttää tätä sisältöä ja tuottaa jäsennellyn raportin `report`-muuttujaan

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Käyttäjän Pyyntö** näyttää tehtävän. Supervisor jäsentää tämän ja päättää kutsua FileAgent → ReportAgent.

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

**Supervisorin Orkestraatio** näyttää 2-vaiheisen työnkulun toiminnassa:
1. **FileAgent** lukee tiedoston MCP:n kautta ja tallentaa sisällön
2. **ReportAgent** vastaanottaa sisällön ja tuottaa jäsennellyn raportin

Supervisor teki nämä päätökset **autonomisesti** käyttäjän pyynnön pohjalta.

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

#### Selitys Agentic-moduulin ominaisuuksista

Esimerkki demonstroi useita agentic-moduulin edistyneitä ominaisuuksia. Tarkastellaan tarkemmin Agentic Scopeta ja Agent-kuuntelijoita.

**Agentic Scope** näyttää jaetun muistin, johon agentit tallensivat tulokset käyttäen `@Agent(outputKey="...")`. Tämä mahdollistaa:
- Myöhempien agenttien pääsyn aiempien agenttien tuloksiin
- Supervisorin mahdollisuuden koostaa lopullisen vastauksen
- Sinun tarkastella, mitä kukin agentti tuotti

Alla oleva kaavio näyttää, kuinka Agentic Scope toimii jaettuna muistina tiedostosta raporttiin -työnkulussa — FileAgent kirjoittaa tuloksensa avaimella `fileContent`, ReportAgent lukee sen ja kirjoittaa oman tuloksensa avaimella `report`:

<img src="../../../translated_images/fi/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope toimii jaettuna muistina — FileAgent kirjoittaa `fileContent`:n, ReportAgent lukee sen ja kirjoittaa `report`:n, ja koodisi lukee lopullisen tuloksen.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Raakatiedot tiedostosta FileAgent
String report = scope.readState("report");            // Jäsennelty raportti ReportAgentilta
```

**Agent Listeners** mahdollistavat agenttien suorituksen seurannan ja vianmäärityksen. Demon askel askeleelta tulostettu siirtymä tulee AgentListeneristä, joka kytkeytyy jokaiseen agenttikutsuun:
- **beforeAgentInvocation** - Kutsutaan kun Supervisor valitsee agentin, jolloin näet, mikä agentti valittiin ja miksi
- **afterAgentInvocation** - Kutsutaan kun agentti on suoritettu, näyttää sen tuloksen
- **inheritedBySubagents** - Kun tosi, kuuntelija seuraa kaikkia agentteja hierarkiassa

Seuraava kaavio näyttää koko Agent Listener -elinkaaren, mukaan lukien miten `onError` käsittelee virheitä agentin suorituksen aikana:

<img src="../../../translated_images/fi/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners kytkeytyvät suorituksen elinkaareen — seuraavat, milloin agentit alkavat, valmistuvat tai kohtaavat virheitä.*

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
        return true; // Levitä kaikille ala-agentteille
    }
};
```

Supervisor-kuvion lisäksi `langchain4j-agentic`-moduuli tarjoaa useita tehokkaita työnkulkuja. Alla oleva kaavio näyttää viisi mallia — yksinkertaisista peräkkäisistä putkistoista ihmisen osallistamiseen perustuviin hyväksyntätyönkulkuihin:

<img src="../../../translated_images/fi/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Viisi työnkulkua agenttien orkestrointiin — yksinkertaisista peräkkäisistä putkistoista ihmisen osallistamaa hyväksyntätyönkulkuihin.*

| Kuvio | Kuvaus | Käyttötapaus |
|---------|-------------|----------|
| **Sequential** | Suoritetaan agentit järjestyksessä, tuloste kulkee seuraavalle | Putkistot: tutki → analysoi → raportoi |
| **Parallel** | Ajetaan agentit samanaikaisesti | Riippumattomat tehtävät: sää + uutiset + osakkeet |
| **Loop** | Toistetaan kunnes ehto täyttyy | Laadun arviointi: tarkenna kunnes piste ≥ 0.8 |
| **Conditional** | Reititys ehtojen mukaan | Luokittelu → ohjautuminen spesialistille |
| **Human-in-the-Loop** | Lisätään ihmisen tarkistuspisteitä | Hyväksyntätyönkulut, sisällön tarkistus |

## Keskeiset käsitteet

Nyt kun olet tutustunut MCP:hen ja agentic-moduuliin käytännössä, tehdään yhteenveto milloin kutakin lähestymistapaa kannattaa käyttää.

Yksi MCP:n suurimmista eduista on sen kasvava ekosysteemi. Alla oleva kaavio näyttää, kuinka yksi yleisprotokolla yhdistää tekoälysovelluksesi monipuolisiin MCP-palvelimiin — tiedostojärjestelmästä ja tietokannasta GitHubiin, sähköpostiin, web-scrapingiin ja muuhun:

<img src="../../../translated_images/fi/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP luo universaalin protokollan ekosysteemin — mikä tahansa MCP-yhteensopiva palvelin toimii minkä tahansa MCP-yhteensopivan asiakkaan kanssa mahdollistaen työkalujen jakamisen sovellusten välillä.*

**MCP** on ihanteellinen, kun haluat hyödyntää olemassa olevia työkaluekosysteemejä, rakentaa työkaluja joita useat sovellukset jakavat, integroida kolmansien osapuolten palveluita standardiprotokollilla tai vaihtaa työkalujen toteutuksia ilman koodimuutoksia.

**Agentic-moduuli** toimii parhaiten, kun haluat deklaratiiviset agenttien määritelmät `@Agent`-annotaatioilla, tarvitset työnkulun orkestrointia (peräkkäinen, looppi, rinnakkainen), suosittelet rajapintapohjaista agenttisuunnittelua imperatiivisen koodin sijasta tai yhdistät useita agenteja, jotka jakavat tuloksia avaimen `outputKey` kautta.

**Supervisor Agent -malli** loistaa, kun työnkulku ei ole ennakoitavissa ja haluat LLM:n päättävän, kun sinulla on useita erikoistuneita agentteja jotka tarvitsevat dynaamista orkestrointia, kun rakennat keskustelujärjestelmiä jotka ohjaavat eri kykyihin tai kun haluat joustavimman, adaptiivisimman agenttikäyttäytymisen.

Auttaaksemme sinua päättämään mukautettujen `@Tool`-metodien (moduuli 04) ja MCP-työkalujen (tässä moduulissa) välillä, seuraava vertailu korostaa keskeisiä valintoja — mukautetut työkalut tarjoavat tiukan kytkennän ja täyden tyyppiturvallisuuden sovelluskohtaiselle logiikalle, kun MCP-työkalut tarjoavat standardoidut, uudelleenkäytettävät integraatiot:

<img src="../../../translated_images/fi/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Milloin käyttää mukautettuja @Tool-menetelmiä vs MCP-työkaluja — mukautetut työkalut sovelluskohtaiselle logiikalle täydellä tyyppiturvallisuudella, MCP-työkalut standardoiduille integraatioille sovellusten välillä.*

## Onnittelut!

Olet käynyt läpi kaikki viisi moduulia LangChain4j for Beginners -kurssilta! Tässä kokonaiskuva oppimismatkasta, jonka olet suorittanut — peruskeskustelusta MCP-tehoisiin agenttijärjestelmiin:

<img src="../../../translated_images/fi/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Oppimismatkasi kaikki viisi moduulia — peruskeskustelusta MCP-tehoisiin agenttijärjestelmiin.*

Olet suorittanut LangChain4j for Beginners -kurssin. Olet oppinut:

- Miten rakentaa keskusteleva tekoäly muistilla (moduuli 01)
- Tarvelauseiden suunnittelumallit eri tehtäviin (moduuli 02)
- Vastausten perustaminen omiin dokumentteihisi RAG:n avulla (moduuli 03)
- Perusagenttien (apulaisten) luominen mukautetuilla työkaluilla (moduuli 04)
- Standardoitujen työkalujen integrointi LangChain4j MCP:n ja Agentic-moduulien kautta (moduuli 05)

### Mitä seuraavaksi?

Moduulien suorittamisen jälkeen tutustu [Testing Guide](../docs/TESTING.md) -oppaaseen nähdäksesi LangChain4j testauskonsepteja käytännössä.

**Viralliset resurssit:**
- [LangChain4j Dokumentaatio](https://docs.langchain4j.dev/) - Laajat oppaat ja API-dokumentaatio
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Lähdekoodi ja esimerkit
- [LangChain4j Opetusohjelmat](https://docs.langchain4j.dev/tutorials/) - Askelsarjaiset opetusohjelmat eri käyttötarkoituksiin

Kiitos, että suoristit tämän kurssin!

---

**Navigointi:** [← Edellinen: Moduuli 04 - Tools](../04-tools/README.md) | [Takaisin pääsivulle](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, otathan huomioon, että automaattikäännöksissä saattaa esiintyä virheitä tai epätarkkuuksia. Alkuperäistä asiakirjaa sen alkuperäiskielellä tulee pitää virallisena lähteenä. Tärkeissä tiedoissa suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä aiheutuneista väärinymmärryksistä tai virhetulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->