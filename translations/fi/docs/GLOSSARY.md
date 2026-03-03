# LangChain4j Sanasto

## Sisällysluettelo

- [Peruskäsitteet](../../../docs)
- [LangChain4j-komponentit](../../../docs)
- [AI/ML-käsitteet](../../../docs)
- [Suojausmekanismit](../../../docs)
- [Prompt-suunnittelu](../../../docs)
- [RAG (hakupohjainen generointi)](../../../docs)
- [Agentit ja työkalut](../../../docs)
- [Agenttimoduuli](../../../docs)
- [Mallin kontekstiprotokolla (MCP)](../../../docs)
- [Azure-palvelut](../../../docs)
- [Testaus ja kehitys](../../../docs)

Nopea viite kursilla käytetyille termeille ja käsitteille.

## Peruskäsitteet

**AI Agentti** – Järjestelmä, joka käyttää tekoälyä järkeilyyn ja toimintaan itsenäisesti. [Moduuli 04](../04-tools/README.md)

**Ketju** – Toimintojen sarja, jossa tulos syötetään seuraavaan vaiheeseen.

**Paloittelu (Chunking)** – Asiakirjojen pilkkominen pienempiin osiin. Tyypillisesti 300–500 tokenia päällekkäisyydellä. [Moduuli 03](../03-rag/README.md)

**Kontekstin ikkuna** – Maksimi tokenien määrä, jonka malli voi käsitellä. GPT-5.2: 400K tokenia (enintään 272K syöte, 128K tuotos).

**Upotukset (Embeddings)** – Numeromuotoiset vektorit, jotka edustavat tekstin merkitystä. [Moduuli 03](../03-rag/README.md)

**Funktiokutsu** – Malli tuottaa jäsenneltyjä pyyntöjä kutsuakseen ulkoisia funktioita. [Moduuli 04](../04-tools/README.md)

**Hallusinaatiot** – Kun mallit tuottavat virheellistä mutta uskottavaa tietoa.

**Promptti** – Tekstisyöte kielimallille. [Moduuli 02](../02-prompt-engineering/README.md)

**Semanttinen haku** – Haku merkityksen mukaan upotuksia hyödyntäen, ei avainsanoilla. [Moduuli 03](../03-rag/README.md)

**Tilallinen vs. tilaton** – Tilaton: ei muistia. Tilallinen: ylläpitää keskusteluhistoriaa. [Moduuli 01](../01-introduction/README.md)

**Tokenit** – Perusyksiköt, joita mallit käyttävät. Vaikuttaa kustannuksiin ja rajoihin. [Moduuli 01](../01-introduction/README.md)

**Työkaluketjutus** – Peräkkäinen työkalujen suoritus, jossa tulos ohjaa seuraavaa kutsua. [Moduuli 04](../04-tools/README.md)

## LangChain4j-komponentit

**AiServices** – Luo tyyppiturvalliset tekoälypalvelu-rajapinnat.

**OpenAiOfficialChatModel** – Yhdistetty asiakas OpenAI:n ja Azure OpenAI:n malleille.

**OpenAiOfficialEmbeddingModel** – Luo upotuksia OpenAI Official -asiakasta käyttäen (tukee sekä OpenAI:ta että Azure OpenAI:ta).

**ChatModel** – Ydinkäyttöliittymä kielimalleille.

**ChatMemory** – Pitää yllä keskusteluhistoriaa.

**ContentRetriever** – Löytää relevantteja asiakirjan palasia RAG:ia varten.

**DocumentSplitter** – Pilkkoo asiakirjat paloiksi.

**EmbeddingModel** – Muuntaa tekstin numeerisiksi vektoreiksi.

**EmbeddingStore** – Tallentaa ja hakee upotuksia.

**MessageWindowChatMemory** – Pitää yllä liukuvaa ikkunaa viimeisimmistä viesteistä.

**PromptTemplate** – Luo uudelleenkäytettäviä promptteja `{{variable}}`-paikkamerkkejä käyttäen.

**TextSegment** – Tekstipala metatiedoilla. Käytetään RAG:ssa.

**ToolExecutionRequest** – Edustaa työkalun suorituspyyntöä.

**UserMessage / AiMessage / SystemMessage** – Keskusteluviestityypit.

## AI/ML-käsitteet

**Few-Shot-oppiminen** – Tarjoaa esimerkkejä promptissa. [Moduuli 02](../02-prompt-engineering/README.md)

**Suuri kielimalli (LLM)** – Suuret tekoälymallit, jotka on koulutettu valtavilla tekstidatoilla.

**Järkeilytyömäärä** – GPT-5.2:n parametri, joka ohjaa ajattelun syvyyttä. [Moduuli 02](../02-prompt-engineering/README.md)

**Lämpötila** – Ohjaa tuotoksen satunnaisuutta. Matala=deterministinen, korkea=luova.

**Vektoritietokanta** – Erityistietokanta upotuksille. [Moduuli 03](../03-rag/README.md)

**Zero-Shot-oppiminen** – Suorittaa tehtäviä ilman esimerkkejä. [Moduuli 02](../02-prompt-engineering/README.md)

## Suojausmekanismit - [Moduuli 00](../00-quick-start/README.md)

**Defense in Depth** – Monikerroksinen suojausmenetelmä, joka yhdistää sovellustason suojaukset ja palveluntarjoajan turvallisuussuodattimet.

**Kova esto** – Palveluntarjoaja palauttaa HTTP 400 -virheen vakavista sisällön rikkomuksista.

**InputGuardrail** – LangChain4j-rajapinta käyttäjän syötteen validointiin ennen LLM:ää. Säästää kustannuksia ja viiveitä estämällä haitalliset promptit varhain.

**InputGuardrailResult** – Palautustyyppi suojauksen validointiin: `success()` tai `fatal("syy")`.

**OutputGuardrail** – Rajapinta AI-vastausten validointiin ennen niiden palaut tamista käyttäjille.

**Palveluntarjoajan turvallisuussuodattimet** – AI-palveluntarjoajien sisäänrakennetut sisältösuodattimet (esim. GitHub Models), jotka pysäyttävät rikkomukset API-tasolla.

**Pehmeä kieltäytyminen** – Malli kieltäytyy kohteliaasti vastaamasta ilman virheilmoitusta.

## Prompt-suunnittelu - [Moduuli 02](../02-prompt-engineering/README.md)

**Ketjureaaliajattelu** – Askel askeleelta järkeily parempaan tarkkuuteen.

**Rajoitettu tuotos** – Määrätyn muodon tai rakenteen pakkokeino.

**Korkea innokkuus** – GPT-5.2 -kuvio perusteelliseen järkeilyyn.

**Matala innokkuus** – GPT-5.2 -kuvio nopeisiin vastauksiin.

**Monikertakeskustelu** – Kontextin ylläpito vaihdoissa.

**Roolipohjainen prompttaus** – Mallin persoonan asettaminen järjestelmäviesteillä.

**Itsearviointi** – Malli arvioi ja parantaa omaa tuotostaan.

**Rakenteellinen analyysi** – Kiinteä arviointikehys.

**Tehtävän suorituksen malli** – Suunnittele → Suorita → Tiivistä.

## RAG (hakupohjainen generointi) - [Moduuli 03](../03-rag/README.md)

**Asiakirjojen käsittelyputki** – Lataa → paloita → upota → tallenna.

**Muistipohjainen upotustallennus** – Ei-pysyvä tallennustila testaukseen.

**RAG** – Yhdistää haun ja generoinnin vastusten perustamiseksi.

**Samanlaisuuspisteet** – Semanttisen samankaltaisuuden mitta (0–1).

**Lähteiden viittaus** – Metatiedot haetusta sisällöstä.

## Agentit ja työkalut - [Moduuli 04](../04-tools/README.md)

**@Tool-annotaatio** – Merkitsee Java-metodit tekoälyn kutsuttaviksi työkaluiksi.

**ReAct-kuvio** – Järkeile → Toimi → Havainnoi → Toista.

**Istunnon hallinta** – Eri käyttäjille erilliset kontekstit.

**Työkalu** – Funktio, jota AI-agentti voi kutsua.

**Työkalun kuvaus** – Dokumentaatio työkalun tarkoituksesta ja parametreista.

## Agenttimoduuli - [Moduuli 05](../05-mcp/README.md)

**@Agent-annotaatio** – Merkitsee rajapinnat tekoälyagenteiksi, joilla on deklaratiivinen käyttäytymismäärittely.

**Agent-kuuntelija** – Koukkupiste agentin suorituksen seurantaan `beforeAgentInvocation()` ja `afterAgentInvocation()` kautta.

**Agenttinen laajuus** – Jaettu muisti, johon agentit tallentavat tulokset `outputKey`-avaimella seuraaville agenteille käytettäväksi.

**AgenticServices** – Tehdas agenttien luomiseen `agentBuilder()`- ja `supervisorBuilder()`-metodeilla.

**Ehdollinen työnkulku** – Reittaus ehtojen perusteella eri asiantuntija-agentteihin.

**Ihmisen rooli prosessissa** – Työnkulku, joka lisää ihmisen tarkastus- tai hyväksyntävaiheita.

**langchain4j-agentic** – Maven-riippuvuus deklaratiiviseen agenttirakentamiseen (kokeellinen).

**Silmukkatyönkulku** – Agentin suorituksen toisto, kunnes ehto täyttyy (esim. laatupisteet ≥ 0.8).

**outputKey** – Agentti-annotaation parametri, joka määrittää missä tulokset tallennetaan Agenttiseen laajuuteen.

**Rinnakkainen työnkulku** – Useiden agenttien yhtäaikainen suoritus riippumattomille tehtäville.

**Vastausstrategia** – Kuinka valvoja muodostaa lopullisen vastauksen: VIIMEINEN, YHTEENVETO tai PISTETTY.

**Järjestelmällinen työnkulku** – Agenttien suoritus järjestyksessä, jossa tulokset siirtyvät seuraavaan vaiheeseen.

**Valvoja-agenttikuva** – Edistynyt agenttipatteri, jossa valvoja-LLM päättää dynaamisesti alitehtävien kutsumisesta.

## Mallin kontekstiprotokolla (MCP) - [Moduuli 05](../05-mcp/README.md)

**langchain4j-mcp** – Maven-riippuvuus MCP-integraatioon LangChain4j:ssä.

**MCP** – Model Context Protocol: standardi AI-sovellusten liittämiseen ulkoisiin työkaluihin. Rakennat kerran, käytät kaikkialla.

**MCP-asiakas** – Sovellus, joka yhdistää MCP-palvelimiin löytääkseen ja käyttääkseen työkaluja.

**MCP-palvelin** – Palvelu, joka tarjoaa työkaluja MCP:n kautta selkeillä kuvauksilla ja parametrikaavoilla.

**McpToolProvider** – LangChain4j-komponentti, joka käärii MCP-työkalut käyttöön AI-palveluissa ja agenteissa.

**McpTransport** – Rajapinta MCP-kommunikaatioon. Toteutuksia ovat mm. Stdio ja HTTP.

**Stdio-kuljetus** – Paikallinen prosessikuljetus stdin/stdoutin kautta. Hyödyllinen tiedostojärjestelmän tai komentorivityökalujen käyttöön.

**StdioMcpTransport** – LangChain4j:n toteutus, joka käynnistää MCP-palvelimen aliprosessina.

**Työkalujen löytäminen** – Asiakas kysyy palvelimelta saatavilla olevat työkalut kuvauksineen ja kaavoineen.

## Azure-palvelut - [Moduuli 01](../01-introduction/README.md)

**Azure AI Search** – Pilvipohjainen haku vektoritoiminnoilla. [Moduuli 03](../03-rag/README.md)

**Azure Developer CLI (azd)** – Azure-resurssien käyttöönotto.

**Azure OpenAI** – Microsoftin yritystason tekoälypalvelu.

**Bicep** – Azure infrastruktuurin koodikieli. [Infrastruktuuriopas](../01-introduction/infra/README.md)

**Käyttöönoton nimi** – Mallin käyttöönoton nimi Azuressa.

**GPT-5.2** – Uusin OpenAI-malli, jossa järkeilyn ohjaus. [Moduuli 02](../02-prompt-engineering/README.md)

## Testaus ja kehitys - [Testausopas](TESTING.md)

**Dev Container** – Konttitoiminen kehitysympäristö. [Konfigurointi](../../../.devcontainer/devcontainer.json)

**GitHub Models** – Ilmainen tekoälymallien leikkikenttä. [Moduuli 00](../00-quick-start/README.md)

**Muistipohjainen testaus** – Testaus muistipohjaisella tallennuksella.

**Integraatiotestaus** – Testaus todellisella infrastruktuurilla.

**Maven** – Java-rakennusautomaatio.

**Mockito** – Java-kirjastojen mockaustyökalu.

**Spring Boot** – Java-sovelluskehys. [Moduuli 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty käyttäen tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, huomioithan, että automaattiset käännökset saattavat sisältää virheitä tai epätarkkuuksia. Alkuperäinen asiakirja omalla kielellään on ensisijainen lähde. Tärkeissä tiedoissa suosittelemme ammattilaisen tekemää käännöstä. Emme ole vastuussa tämän käännöksen käytöstä johtuvista väärinkäsityksistä tai virhetulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->