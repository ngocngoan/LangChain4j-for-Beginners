# Moduuli 05: Model Context Protocol (MCP)

## Sisällysluettelo

- [Mitä Opit](../../../05-mcp)
- [Mikä on MCP?](../../../05-mcp)
- [Miten MCP Toimii](../../../05-mcp)
- [Agenttimoduuli](../../../05-mcp)
- [Esimerkkien Suorittaminen](../../../05-mcp)
  - [Esivaatimukset](../../../05-mcp)
- [Pika-aloitus](../../../05-mcp)
  - [Tiedostotoiminnot (Stdio)](../../../05-mcp)
  - [Valvoja-agentti](../../../05-mcp)
    - [Demonstroinnin Suorittaminen](../../../05-mcp)
    - [Kuinka Valvoja Toimii](../../../05-mcp)
    - [Kuinka FileAgent Löytää MCP-työkalut Ajonaikana](../../../05-mcp)
    - [Vastausstrategiat](../../../05-mcp)
    - [Tuloksen Ymmärtäminen](../../../05-mcp)
    - [Agenttimoduulin Ominaisuuksien Selitys](../../../05-mcp)
- [Keskeiset Käsitteet](../../../05-mcp)
- [Onnittelut!](../../../05-mcp)
  - [Mitä Seuraavaksi?](../../../05-mcp)

## Mitä Opit

Olet rakentanut keskustelevaa tekoälyä, hallinnut kehotteet, perustanut vastaukset dokumentteihin ja luonut agenteja työkaluilla. Mutta kaikki nuo työkalut oli räätälöity juuri sinun sovelluksellesi. Entä jos voisit antaa tekoälyllesi pääsyn standardoituihin työkalujen ekosysteemeihin, joita kuka tahansa voi luoda ja jakaa? Tässä moduulissa opit tekemään juuri niin Model Context Protocolin (MCP) ja LangChain4j:n agenttimoduulin avulla. Ensin esittelemme yksinkertaisen MCP-tiedostojen lukijan ja sen jälkeen näytämme, miten se helposti integroituu edistyneisiin agenttimaisiin työnkulkuihin käyttäen Valvoja-agenttipatternia.

## Mikä on MCP?

Model Context Protocol (MCP) tarjoaa juuri tämän - standardoidun tavan tekoälysovelluksille löytää ja käyttää ulkoisia työkaluja. Sen sijaan että kirjoittaisit räätälöityjä integraatioita jokaiselle tietolähteelle tai palvelulle, yhdistät MCP-palvelimiin, jotka tarjoavat kykynsä yhtenäisessä muodossa. Tekoälyagenttisi voi sitten automaattisesti löytää ja käyttää näitä työkaluja.

Alla oleva kaavio havainnollistaa eron — ilman MCP: tä jokainen integraatio vaatii räätälöityä pistettä-pisteeseen yhdistämistä; MCP: n kanssa yksi protokolla yhdistää sovelluksesi mihin tahansa työkaluihin:

<img src="../../../translated_images/fi/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP-vertaus" width="800"/>

*Ennen MCP: tä: Monimutkaiset pistettä-pisteeseen integraatiot. MCP:n jälkeen: Yksi protokolla, loputtomat mahdollisuudet.*

MCP ratkoo perustavan ongelman tekoälyn kehityksessä: jokainen integraatio on räätälöity. Haluatko käyttää GitHubia? Räätälöity koodi. Haluat lukea tiedostoja? Räätälöity koodi. Haluat kysyä tietokantaa? Räätälöity koodi. Eikä mikään näistä integraatioista toimi muiden tekoälysovellusten kanssa.

MCP standardisoi tämän. MCP-palvelin tarjoaa työkalut selkeillä kuvauksilla ja skeemoilla. Mikä tahansa MCP-asiakas voi yhdistää, löytää käytettävissä olevat työkalut ja käyttää niitä. Rakenna kerran, käytä kaikkialla.

Alla oleva kaavio havainnollistaa tätä arkkitehtuuria — yksi MCP-asiakas (tekoälysovelluksesi) yhdistää useisiin MCP-palvelimiin, jotka kukin tarjoavat omat työkalunsa standardoidun protokollan kautta:

<img src="../../../translated_images/fi/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP-arkkitehtuuri" width="800"/>

*Model Context Protocol -arkkitehtuuri - standardoitu työkalujen löytäminen ja suorittaminen*

## Miten MCP Toimii

Taustalla MCP käyttää kerroksellista arkkitehtuuria. Java-sovelluksesi (MCP-asiakas) löytää käytettävissä olevat työkalut, lähettää JSON-RPC-pyyntöjä kuljetuskerroksen (Stdio tai HTTP) kautta, ja MCP-palvelin suorittaa operaatiot ja palauttaa tulokset. Seuraava kaavio avaa tämän protokollan jokaisen kerroksen:

<img src="../../../translated_images/fi/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP-protokollan Yksityiskohdat" width="800"/>

*Miten MCP toimii taustalla — asiakkaat löytävät työkalut, vaihtavat JSON-RPC-viestejä ja suorittavat toimintoja kuljetuskerroksen kautta.*

**Palvelin-asiakas Arkkitehtuuri**

MCP perustuu asiakas-palvelin -malliin. Palvelimet tarjoavat työkaluja - tiedostojen lukeminen, tietokantakyselyt, API-kutsut. Asiakkaat (tekoälysovelluksesi) yhdistävät palvelimiin ja käyttävät niiden työkaluja.

Käyttääksesi MCP:tä LangChain4j:n kanssa, lisää tämä Maven-riippuvuus:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Työkalujen Löytäminen**

Kun asiakkaasi yhdistää MCP-palvelimeen, se kysyy "Mitä työkaluja sinulla on?" Palvelin vastaa käytettävissä olevien työkalujen listalla, kullekin on kuvaukset ja parametrien skeemat. Tekoälyagenttisi voi sen jälkeen päättää, mitä työkaluja käyttää käyttäjäpyyntöjen perusteella. Alla oleva kaavio näyttää tämän kättelyn — asiakas lähettää `tools/list` -pyynnön ja palvelin palauttaa käytettävissä olevat työkalut kuvauksineen ja parametriskeemoineen:

<img src="../../../translated_images/fi/tool-discovery.07760a8a301a7832.webp" alt="MCP-työkalujen löytäminen" width="800"/>

*Tekoäly löytää käytettävissä olevat työkalut käynnistyksessä — se tietää nyt käytettävissä olevat kyvyt ja voi päättää, mitä käyttää.*

**Kuljetusmekanismit**

MCP tukee erilaisia kuljetusmekanismeja. Kaksi vaihtoehtoa ovat Stdio (paikallinen aliohjelmaprosessiviestintä) ja Streamable HTTP (etäpalvelimille). Tämä moduuli esittelee Stdio-kuljetuksen:

<img src="../../../translated_images/fi/transport-mechanisms.2791ba7ee93cf020.webp" alt="Kuljetusmekanismit" width="800"/>

*MCP:n kuljetusmekanismit: HTTP etäpalvelimille, Stdio paikallisille prosesseille*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Paikallisille prosesseille. Sovelluksesi luo palvelimen aliohjelmaprosessina ja kommunikoi standardin syötön/ulostulon kautta. Hyödyllistä tiedostojärjestelmän käytössä tai komentorivityökaluissa.

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

`@modelcontextprotocol/server-filesystem` -palvelin tarjoaa seuraavat työkalut, kaikki rajoitettuina määrittelemiisi kansioihin:

| Työkalu | Kuvaus |
|------|-------------|
| `read_file` | Lue yksittäisen tiedoston sisältö |
| `read_multiple_files` | Lue useita tiedostoja yhdellä kutsulla |
| `write_file` | Luo tai ylikirjoita tiedosto |
| `edit_file` | Tee kohdennettuja etsi-ja-korvaa-muutoksia |
| `list_directory` | Listaa tiedostot ja kansiot polussa |
| `search_files` | Etsi rekursiivisesti tiedostoja, jotka vastaavat mallia |
| `get_file_info` | Hanki tiedoston metatiedot (koko, aikaleimat, käyttöoikeudet) |
| `create_directory` | Luo kansio (mukaan lukien vanhemmat kansiot) |
| `move_file` | Siirrä tai nimeä tiedosto tai kansio uudelleen |

Seuraava kaavio näyttää, miten Stdio-kuljetus toimii ajonaikana — Java-sovelluksesi käynnistää MCP-palvelimen lapsiprosessina ja ne kommunikoivat stdin/stdout-putkia pitkin, ilman verkkoa tai HTTP:tä:

<img src="../../../translated_images/fi/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio-kuljetuksen toimintavirta" width="800"/>

*Stdio-kuljetus käytännössä — sovelluksesi käynnistää MCP-palvelimen lapsiprosessina ja kommunikoi stdin/stdout-putkien kautta.*

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatillä:** Avaa [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) ja kysy:
> - "Miten Stdio-kuljetus toimii ja milloin sitä tulisi käyttää verrattuna HTTP:hen?"
> - "Miten LangChain4j hallitsee käynnistettyjen MCP-palvelinprosessiensa elinkaarta?"
> - "Mitkä ovat tietoturvaan liittyvät vaikutukset, kun annetaan tekoälyn käyttää tiedostojärjestelmää?"

## Agenttimoduuli

Vaikka MCP tarjoaa standardoituja työkaluja, LangChain4j:n **agenttimoduuli** tarjoaa deklaratiivisen tavan rakentaa agenteja, jotka orkestroivat näitä työkaluja. `@Agent`-annotaatio ja `AgenticServices` antavat sinun määritellä agentin käyttäytymisen rajapintojen kautta, ei imperatiivisella koodilla.

Tässä moduulissa tutustut **Valvoja-agenttipatteriin** — edistyneeseen agenttimalliin, jossa "valvoja"-agentti päättää dynaamisesti, mitä alagisteja kutsutaan käyttäjän pyyntöjen perusteella. Yhdistämme molemmat käsitteet antamalla yhdelle alagisteistämme MCP:n tarjoamat tiedostojen käyttömahdollisuudet.

Käyttääksesi agenttimoduulia, lisää tämä Maven-riippuvuus:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Huom:** `langchain4j-agentic`-moduuli käyttää erillistä versio-ominaisuutta (`langchain4j.mcp.version`), koska sen julkaisuaikataulu poikkeaa LangChain4j:n ydinkirjastoista.

> **⚠️ Kokeellinen:** `langchain4j-agentic`-moduuli on **kokeellinen** ja voi muuttua. Vakaa tapa rakentaa tekoälyavustajia on jatkaa `langchain4j-core`-moduulin ja mukautettujen työkalujen (Moduuli 04) käyttöä.

## Esimerkkien Suorittaminen

### Esivaatimukset

- Suoritettu [Moduuli 04 - Työkalut](../04-tools/README.md) (tässä moduulissa rakennetaan mukautettuihin työkaluihin ja verrataan niitä MCP-työkaluihin)
- `.env`-tiedosto juurihakemistossa Azure-tunnuksilla (luotu `azd up`:lla Moduulissa 01)
- Java 21+, Maven 3.9+
- Node.js 16+ ja npm (MCP-palvelimille)

> **Huom:** Jos et ole vielä määrittänyt ympäristömuuttujia, katso [Moduuli 01 - Johdanto](../01-introduction/README.md) käyttöönotto-ohjeet (`azd up` luo `.env`-tiedoston automaattisesti), tai kopioi `.env.example` nimellä `.env` juurihakemistoon ja täytä tiedot.

## Pika-aloitus

**VS Code -käytössä:** Klikkaa hiiren oikealla mitä tahansa demonstraatiotiedostoa Explorerissa ja valitse **"Run Java"**, tai käytä ajoasetuksia Run and Debug -paneelista (varmistu, että `.env` on asetettu Azure-tunnuksilla).

**Mavenilla:** Vaihtoehtoisesti voit ajaa komentoriviltä esimerkkien mukaan.

### Tiedostotoiminnot (Stdio)

Tämä demonstroi paikallisia aliohjelmaprosessipohjaisia työkaluja.

**✅ Ei esivaatimuksia** - MCP-palvelin käynnistyy automaattisesti.

**Aloituskomentosarjojen käyttö (suositeltu):**

Käynnistyskäsikirjoitukset lataavat automaattisesti ympäristömuuttujat juurihakemiston `.env`-tiedostosta:

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

**VS Code -käytössä:** Klikkaa hiiren oikealla `StdioTransportDemo.java` ja valitse **"Run Java"** (varmistu `.env`-asetuksesta).

Sovellus käynnistää tiedostojärjestelmän MCP-palvelimen automaattisesti ja lukee paikallisen tiedoston. Huomaa, miten aliohjelmaprosessin hallinta hoituu puolestasi.

**Odotettu tulostus:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Valvoja-agentti

**Valvoja-agenttipattern** on **joustava** agenttipohjainen tekoälyä koskeva muoto. Valvoja käyttää LLM:ää autonomisesti päättämään, mitä agenteja kutsutaan käyttäjän pyynnön perusteella. Seuraavassa esimerkissä yhdistämme MCP:n tiedostokäytön LLM-agenttiin luodaksemme valvotun tiedostonluku → raportti -työnkulun.

Demossa `FileAgent` lukee tiedoston MCP:n tiedostojärjestelmätyökalujen avulla ja `ReportAgent` tuottaa rakenteellisen raportin, joka sisältää johdon yhteenvedon (1 lause), 3 keskeistä kohtaa ja suositukset. Valvoja orkestroi tämän työnkulun automaattisesti:

<img src="../../../translated_images/fi/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Valvoja-agenttipattern" width="800"/>

*Valvoja käyttää omaa LLM:ää päättäen, mitä agenteja kutsutaan ja missä järjestyksessä — kovakoodattua reititystä ei tarvita.*

Tältä konkreettinen työnkulku näyttää tiedosto-raportti-pipeline:ssamme:

<img src="../../../translated_images/fi/file-report-workflow.649bb7a896800de9.webp" alt="Tiedostosta Raporttiin Työnkulku" width="800"/>

*FileAgent lukee tiedoston MCP-työkalujen kautta, sitten ReportAgent muuntaa raakadatan rakenteelliseksi raportiksi.*

Seuraava sekvenssikaavio kuvaa Valvojan täydellistä orkestrointia — MCP-palvelimen käynnistämisestä, Valvojan autonomiseen agenttien valintaan, stdio:n kautta tapahtuvaan työkalukutsuun ja lopulliseen raporttiin:

<img src="../../../translated_images/fi/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Valvoja-agentin sekvenssikaavio" width="800"/>

*Valvoja kutsuu autonomisesti FileAgentin (joka soittaa MCP-palvelimelle stdio:n ylitse lukeakseen tiedoston), sitten kutsuu ReportAgentin tuottamaan rakenteellisen raportin — jokainen agentti tallentaa tuloksensa jaetulle Agentic Scope -muistialueelle.*

Jokainen agentti tallentaa tuloksensa **Agentic Scope**en (jaettava muisti), mahdollistaen myöhempien agenttien pääsyn aiempiin tuloksiin. Tämä havainnollistaa, kuinka MCP-työkalut integroituvat saumattomasti agenttipohjaisiin työnkulkuihin — Valvojan ei tarvitse tietää *miten* tiedostoja luetaan, vaan vain että `FileAgent` voi tehdä sen.

#### Demon Suorittaminen

Käynnistyskäsikirjoitukset lataavat automaattisesti ympäristömuuttujat juurihakemiston `.env`-tiedostosta:

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

**VS Code -käytössä:** Klikkaa hiiren oikealla `SupervisorAgentDemo.java` ja valitse **"Run Java"** (varmistu `.env`-asetuksesta).

#### Kuinka Valvoja Toimii

Ennen agenttien rakentamista sinun täytyy yhdistää MCP-kuljetus asiakkaaseen ja kääriä se `ToolProvider`:iksi. Näin MCP-palvelimen työkalut tulevat saataville agenteillesi:

```java
// Luo MCP-asiakas kuljetuksesta
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Kääri asiakas ToolProvideriksi — tämä yhdistää MCP-työkalut LangChain4j:hin
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Nyt voit injektoida `mcpToolProvider`n mihin tahansa agenttiin, joka tarvitsee MCP-työkaluja:

```java
// Vaihe 1: FileAgent lukee tiedostoja käyttäen MCP-työkaluja
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Sisältää MCP-työkaluja tiedostotoimintoihin
        .build();

// Vaihe 2: ReportAgent luo rakenteellisia raportteja
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor ohjaa tiedosto → raportti työnkulun
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Palauta lopullinen raportti
        .build();

// Supervisor päättää, mitä agenteja kutsutaan pyynnön perusteella
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Kuinka FileAgent Löytää MCP-työkalut Ajonaikana

Saatat miettiä: **Miten `FileAgent` tietää, miten käyttää npm-tiedostojärjestelmätyökaluja?** Vastaus on, että se ei tiedä — **LLM** selvittää sen ajonaikana työkaluskeemojen kautta.

`FileAgent`-rajapinta on vain **kehotteen määrittely**. Siinä ei ole kovakoodattua tietoa `read_file`, `list_directory` tai muista MCP-työkaluista. Tässä mitä tapahtuu päästä päähän:
1. **Palvelin käynnistää:** `StdioMcpTransport` käynnistää `@modelcontextprotocol/server-filesystem` npm-paketin lapsiprosessina  
2. **Työkalujen havainto:** `McpClient` lähettää `tools/list` JSON-RPC -pyynnön palvelimelle, joka vastaa työkalujen nimillä, kuvauksilla ja parametrien skeemoilla (esim. `read_file` — *"Lue tiedoston koko sisältö"* — `{ path: string }`)  
3. **Skeeman injektointi:** `McpToolProvider` ympäröi löydetyt skeemat ja tarjoaa ne LangChain4j:lle  
4. **LLM päättää:** Kun `FileAgent.readFile(path)` kutsutaan, LangChain4j lähettää järjestelmäviestin, käyttäjäviestin **ja listan työkaluskeemoista** LLM:lle. LLM lukee työkalujen kuvaukset ja generoi työkalukutsun (esim. `read_file(path="/some/file.txt")`)  
5. **Suoritus:** LangChain4j sieppaa työkalukutsun, reitittää sen MCP-asiakkaan kautta takaisin Node.js-aliprosessille, saa tuloksen ja syöttää sen takaisin LLM:lle  

Tämä on sama [Työkalujen havaitsemisen](../../../05-mcp) mekanismi kuin yllä kuvattu, mutta sovellettuna nimenomaan agentin työnkulkuun. `@SystemMessage` ja `@UserMessage` -merkinnät ohjaavat LLM:n käyttäytymistä, kun taas injektoitu `ToolProvider` antaa sille **ominaisuudet** — LLM yhdistää nämä kaksi suorituksen aikana.

> **🤖 Kokeile [GitHub Copilotin](https://github.com/features/copilot) Chatin kanssa:** Avaa [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) ja kysy:  
> - "Mistä tämä agentti tietää, mitä MCP-työkalua kutsua?"  
> - "Mitä tapahtuisi, jos poistaisin ToolProviderin agentin rakentajasta?"  
> - "Miten työkaluskeemat välitetään LLM:lle?"  

#### Vastausstrategiat

Kun konfiguroit `SupervisorAgent`-agentin, määrität miten sen tulee muotoilla lopullinen vastauksensa käyttäjälle, kun alagentit ovat suorittaneet tehtävänsä. Alla oleva kaavio näyttää kolme käytettävissä olevaa strategiaa — LAST palauttaa viimeisen agentin tuloksen suoraan, SUMMARY tiivistää kaikki vastaukset LLM:n avulla, ja SCORED valitsee paremman pisteytyksen alkuperäiseen pyyntöön nähden:

<img src="../../../translated_images/fi/response-strategies.3d0cea19d096bdf9.webp" alt="Vastausstrategiat" width="800"/>

*Kolme strategiaa, miten Supervisor muodostaa lopullisen vastauksen — valitse sen mukaan, haluatko viimeisen agentin vastauksen, tiivistelmän vai parhaiten pisteytetyn vaihtoehdon.*

Käytettävissä olevat strategiat ovat:

| Strategia | Kuvaus |
|----------|-------------|
| **LAST** | Voimvalvoja palauttaa viimeisen alagentin tai kutsutun työkalun tuloksen. Tämä on hyödyllistä, kun työnkulun viimeinen agentti on tarkoitettu tuottamaan kattava, lopullinen vastaus (esim. "Yhteenvetoagentti" tutkimusputkessa). |
| **SUMMARY** | Voimvalvoja käyttää omaa sisäistä kielimalliaan (LLM) tiivistämään koko vuorovaikutuksen ja kaikkien alagenttien vastaukset yhteen, ja palauttaa tämän yhteenvedon lopullisena vastauksena. Tämä tarjoaa käyttäjälle selkeän, koottua vastauksen. |
| **SCORED** | Järjestelmä käyttää sisäistä LLM:ää pisteyttämään sekä LAST-vastauksen että koko vuorovaikutuksen SUMMARYn alkuperäiseen käyttäjäpyyntöön nähden, ja palauttaa paremmin pisteytetyn tuloksen. |

Katso täydellinen toteutus [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) tiedostosta.

> **🤖 Kokeile [GitHub Copilotin](https://github.com/features/copilot) Chatin kanssa:** Avaa [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) ja kysy:  
> - "Miten Supervisor päättää, mitä agenteja kutsua?"  
> - "Mikä on ero Supervisorin ja Sequential-työnkulkujen välillä?"  
> - "Miten voin mukauttaa Supervisorin suunnittelukäyttäytymistä?"  

#### Tulosten ymmärtäminen

Kun suoritat demon, näet rakenteellisen läpikäynnin siitä, miten Supervisor orkestroi useita agenteja. Tässä mitä kukin osa tarkoittaa:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Otsikko** esittelee työnkulun käsitteen: fokusoitu putki tiedostojen lukemisesta raportin luomiseen.

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
  
**Työnkulun kaavio** näyttää tiedonkulun agenttien välillä. Jokaisella agentilla on oma roolinsa:  
- **FileAgent** lukee tiedostoja MCP-työkaluilla ja tallentaa raakadatan `fileContent`-kenttään  
- **ReportAgent** käyttää tuota sisältöä ja tuottaa jäsennellyn raportin `report`-kenttään

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Käyttäjäpyyntö** näyttää tehtävän. Supervisor jäsentää pyynnön ja päättää kutsua FileAgent → ReportAgent.

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
  
**Supervisorin orkestrointi** näyttää kaksi askelta toiminnassa:  
1. **FileAgent** lukee tiedoston MCP:n kautta ja tallentaa sisällön  
2. **ReportAgent** saa sisällön ja generoi jäsennellyn raportin  

Supervisor teki nämä päätökset **itseohjautuvasti** käyttäjän pyynnön perusteella.

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
  
#### Agenttikomponenttien ominaisuuksien selitys

Esimerkki demonstroi useita agenttikomponentin edistyneitä ominaisuuksia. Tarkastellaan erityisesti Agentic Scopeta ja Agent-kuuntelijoita.

**Agentic Scope** näyttää jaetun muistin, johon agentit tallensivat tuloksensa käyttäen `@Agent(outputKey="...")`. Tämä mahdollistaa:  
- Myöhemmille agenteille pääsyn aiempiin tuloksiin  
- Supervisorin kyvyn syntetisoida lopullinen vastaus  
- Sinun mahdollisuuden tarkastella mitä kukin agentti tuotti

Alla oleva kaavio näyttää, miten Agentic Scope toimii jaettuna muistina tiedostosta raporttiin -työnkulussa — FileAgent kirjoittaa `fileContent`-avaimelle, ReportAgent lukee sen ja kirjoittaa oman tuloksensa `report`-avaimelle:

<img src="../../../translated_images/fi/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope jaettu muisti" width="800"/>

*Agentic Scope toimii jaettuna muistina — FileAgent kirjoittaa `fileContent`, ReportAgent lukee ja kirjoittaa `report`, ja koodisi lukee lopputuloksen.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Raakadata tiedostosta FileAgent
String report = scope.readState("report");            // Jäsennelty raportti ReportAgentiltä
```
  
**Agent-kuuntelijat** mahdollistavat agentin suorituksen seurannan ja virheenkorjauksen. Demon vaihe vaiheelta näyttämä tuloste tulee AgentListeneristä, joka kytkeytyy jokaiseen agentin kutsuun:  
- **beforeAgentInvocation** - Kutsutaan, kun Supervisor valitsee agentin, jolloin näet valinnan syyn  
- **afterAgentInvocation** - Kutsutaan, kun agentti suorittaa tehtävänsä, ja näyttää tuloksen  
- **inheritedBySubagents** - Kun true, kuuntelija seuraa kaikkia alagentteja hierarkiassa  

Seuraava kaavio näyttää Agent Listenerin koko elinkaaren, mukaan lukien miten `onError` käsittelee virheitä suorituksen aikana:

<img src="../../../translated_images/fi/agent-listeners.784bfc403c80ea13.webp" alt="Agent-kuuntelijoiden elinkaari" width="800"/>

*Agent-kuuntelijat kytkeytyvät suorituksen elinkaareen — seuraa, milloin agentit aloittavat, päättävät tai kohtaavat virheitä.*

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
        return true; // Levitä kaikille ala-agenteille
    }
};
```
  
Supervisor-mallin lisäksi `langchain4j-agentic` moduuli tarjoaa useita tehokkaita työnkulkuja. Alla oleva kaavio näyttää kaikki viisi — yksinkertaisista peräkkäisistä putkista ihmisen ohjauksiin perustuville hyväksymisprosesseille:

<img src="../../../translated_images/fi/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agenttien työnkulkumallit" width="800"/>

*Viisi työnkulkumallia agenttien orkestrointiin — yksinkertaisista peräkkäisistä putkista ihmisen ohjauksiin perustuville hyväksymisprosesseille.*

| Malli | Kuvaus | Käyttötapaus |
|---------|-------------|----------|
| **Sequential** | Suorita agentit peräkkäin, tulos siirtyy seuraavalle | Putket: tutkimus → analyysi → raportointi |
| **Parallel** | Suorita agentit samanaikaisesti | Riippumattomat tehtävät: sää + uutiset + osakkeet |
| **Loop** | Toista kunnes ehto täyttyy | Laadun pisteytys: hienosäädä kunnes pistemäärä ≥ 0.8 |
| **Conditional** | Reititä ehtojen mukaan | Luokittelu → erikoisagentille |
| **Human-in-the-Loop** | Lisää ihmisen tarkistuspisteitä | Hyväksymiskulut, sisältöarviointi |

## Keskeiset käsitteet

Nyt kun olet tutustunut MCP:hen ja agenttikomponenttiin käytännössä, kerrataan milloin käyttää kutakin lähestymistapaa.

Yksi MCP:n suurimmista eduista on sen kasvava ekosysteemi. Alla oleva kaavio näyttää, miten yksi yleinen protokolla yhdistää tekoälysovelluksesi monenlaisiin MCP-palvelimiin — tiedostojärjestelmästä ja tietokantayhteyksistä GitHubiin, sähköpostiin, nettisivujen kaavintaan ja muuhun:

<img src="../../../translated_images/fi/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP-ekosysteemi" width="800"/>

*MCP luo yleisen protokollaekosysteemin — mikä tahansa MCP-yhteensopiva palvelin toimii minkä tahansa MCP-yhteensopivan asiakkaan kanssa, mahdollistaen työkalujen jakamisen sovellusten välillä.*

**MCP** on ihanteellinen, kun haluat hyödyntää olemassa olevia työkaluekosysteemejä, rakentaa työkaluja, joita useat sovellukset voivat käyttää, integroida kolmansien osapuolten palveluita standardien protokollien kautta tai vaihtaa työkalujen toteutuksia muuttamatta koodia.

**Agenttikomponentti** toimii parhaiten, kun haluat deklaratiivisia agenttien määritelmiä `@Agent`-annotaatioilla, tarvitset työnkulkujen orkestrointia (peräkkäinen, silmukka, rinnakkainen), suosittelet rajapintaan perustuvaa agenttisuunnittelua imperatiivisen koodin sijaan tai yhdistät useita agenteja, jotka jakavat tuloksia `outputKey`-avaimen kautta.

**Supervisor Agent -malli** loistaa silloin, kun työnkulku ei ole ennakoitavissa etukäteen ja haluat LLM:n päättävän, kun sinulla on useita erikoistuneita agenteja, jotka tarvitsevat dynaamista orkestrointia, kun rakennat keskustelujärjestelmiä, jotka reitittävät eri ominaisuuksiin, tai kun haluat joustavimman ja sopeutuvimman agenttikäyttäytymisen.

Auttaaksemme sinua päättämään räätälöityjen `@Tool`-metodien (Module 04) ja MCP-työkalujen (tämän moduulin) välillä, seuraava vertailu korostaa keskeisiä valintakriteereitä — räätälöidyt työkalut tarjoavat tiukan kytkennän ja täyden tyypintarkkuuden sovelluskohtaiselle logiikalle, kun taas MCP-työkalut tarjoavat standardoituja ja uudelleenkäytettäviä integraatioita:

<img src="../../../translated_images/fi/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Räätälöidyt työkalut vs MCP-työkalut" width="800"/>

*Milloin käyttää räätälöityjä @Tool-metodeja vs MCP-työkaluja — räätälöidyt työkalut sovelluskohtaiselle logiikalle täydellä tyypintarkkuudella, MCP-työkalut standardoiduille integraatioille, jotka toimivat useissa sovelluksissa.*

## Onnittelut!

Olet suorittanut kaikki viisi LangChain4j for Beginners -kurssin moduulia! Tässä katsaus koko oppimismatkaan — peruskeskusteluista MCP:n tehostamiin agenttijärjestelmiin:

<img src="../../../translated_images/fi/course-completion.48cd201f60ac7570.webp" alt="Kurssin suoritus" width="800"/>

*Oppimismatkasi kaikki viisi moduulia läpi — peruskeskustelusta MCP-vetoisiin agenttijärjestelmiin.*

Olet suorittanut LangChain4j for Beginners -kurssin. Olet oppinut:

- Kuinka rakentaa keskustelevaa tekoälyä muistin avulla (Module 01)  
- Kehote- ja prompttien suunnittelumallit eri tehtäviin (Module 02)  
- Vastausten pohjustaminen omiin dokumentteihisi RAG:n avulla (Module 03)  
- Perusagenttien (avustajien) luominen räätälöidyillä työkaluilla (Module 04)  
- Standardoitujen työkalujen integrointi LangChain4j MCP- ja Agentic-moduuleilla (Module 05)  

### Mitä seuraavaksi?

Moduulien suorittamisen jälkeen tutustu [Testing Guide](../docs/TESTING.md) -oppaaseen nähdäksesi LangChain4j:n testauskonseptit käytännössä.

**Viralliset resurssit:**  
- [LangChain4j-dokumentaatio](https://docs.langchain4j.dev/) – kattavat oppaat ja API-viitetiedot  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) – lähdekoodi ja esimerkit  
- [LangChain4j-oppaat](https://docs.langchain4j.dev/tutorials/) – vaiheittaiset opastusmateriaalit eri käyttötarkoituksiin  

Kiitos, että suoristit tämän kurssin!

---

**Navigointi:** [← Edellinen: Module 04 - Tools](../04-tools/README.md) | [Takaisin pääsivulle](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:  
Tämä asiakirja on käännetty tekoälypohjaisella käännöspalvelulla [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, otathan huomioon, että automaattikäännöksissä saattaa esiintyä virheitä tai epätarkkuuksia. Alkuperäinen asiakirja sen omalla kielellä on virallinen lähde. Tärkeissä asioissa suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä johtuvista väärinymmärryksistä tai tulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->