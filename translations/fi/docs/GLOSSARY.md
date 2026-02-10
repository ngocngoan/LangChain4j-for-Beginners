# LangChain4j Sanasto

## Sisältö

- [Keskeiset käsitteet](../../../docs)
- [LangChain4j-komponentit](../../../docs)
- [AI/ML-käsitteet](../../../docs)
- [Suojakeinot](../../../docs)
- [Promptin suunnittelu](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agentit ja työkalut](../../../docs)
- [Agenttimoduuli](../../../docs)
- [Mallin kontekstiprotokolla (MCP)](../../../docs)
- [Azure-palvelut](../../../docs)
- [Testaus ja kehitys](../../../docs)

Nopea viite kurssilla käytetyille termeille ja käsitteille.

## Keskeiset käsitteet

**AI Agentti** – Järjestelmä, joka käyttää tekoälyä päättelyyn ja itsenäiseen toimintaan. [Moduuli 04](../04-tools/README.md)

**Ketju** – Toimintojen sarja, jossa yksi vaihe syöttää seuraavalle.

**Palastelu** – Asiakirjojen jakaminen pienempiin osiin. Tyypillisesti 300–500 tokenia päällekkäisyydellä. [Moduuli 03](../03-rag/README.md)

**Kontekstiaukkko** – Maksimimäärä tokeneita, jonka malli pystyy käsittelemään. GPT-5.2: 400 000 tokenia.

**Empattat** – Numeraaliset vektorit, jotka kuvaavat tekstin merkitystä. [Moduuli 03](../03-rag/README.md)

**Funktiokutsu** – Malli muodostaa rakenteellisia pyyntöjä ulkoisten toimintojen kutsumiseksi. [Moduuli 04](../04-tools/README.md)

**Hallusinaatio** – Kun mallit tuottavat virheellistä mutta uskottavaa tietoa.

**Prompt** – Tekstisyöte kielimallille. [Moduuli 02](../02-prompt-engineering/README.md)

**Semanttinen haku** – Merkitykseen perustuva haku käyttäen empattiota, ei avainsanoja. [Moduuli 03](../03-rag/README.md)

**Tilan säilyttäminen vs. tilaton** – Tilaton: ei muistia. Tilallinen: säilyttää keskusteluhistorian. [Moduuli 01](../01-introduction/README.md)

**Tokenit** – Perusyksiköt, joita mallit käsittelevät. Vaikuttaa kustannuksiin ja rajoihin. [Moduuli 01](../01-introduction/README.md)

**Työkaluketjuttaminen** – Työkalujen peräkkäinen suoritus, jossa tulos ohjaa seuraavaa kutsua. [Moduuli 04](../04-tools/README.md)

## LangChain4j-komponentit

**AiServices** – Luo tyyppiturvallisia tekoälypalvelujen rajapintoja.

**OpenAiOfficialChatModel** – Yhdistetty asiakas OpenAI- ja Azure OpenAI -malleille.

**OpenAiOfficialEmbeddingModel** – Luo empattatoita OpenAI Official -asiakkaalla (tukee sekä OpenAI että Azure OpenAI).

**ChatModel** – Keskeinen rajapinta kielimalleille.

**ChatMemory** – Säilyttää keskusteluhistorian.

**ContentRetriever** – Löytää relevantteja asiakasosioita RAG:ille.

**DocumentSplitter** – Jakaa asiakirjat osioihin.

**EmbeddingModel** – Muuntaa tekstin numeraaliseksi vektoriksi.

**EmbeddingStore** – Tallentaa ja hakee empattatoita.

**MessageWindowChatMemory** – Säilyttää liukuvaa ikkuna viimeisimmistä viesteistä.

**PromptTemplate** – Luo uudelleenkäytettäviä promptteja `{{variable}}`-paikkamerkkeillä.

**TextSegment** – Tekstipalanen metadatalla. Käytetään RAG:issa.

**ToolExecutionRequest** – Edustaa työkalun suorituspyyntöä.

**UserMessage / AiMessage / SystemMessage** – Keskustelun viestityypit.

## AI/ML-käsitteet

**Few-Shot-opetus** – Esimerkkien antaminen promteissa. [Moduuli 02](../02-prompt-engineering/README.md)

**Suurten kielimallien (LLM) mallit** – Tekoälymalleja, joita on koulutettu valtavilla tekstidatoilla.

**Päättelyponnistus** – GPT-5.2-parametri, joka ohjaa ajattelun syvyyttä. [Moduuli 02](../02-prompt-engineering/README.md)

**Lämpötila** – Ohjaa satunnaisuutta vastauksissa. Matala = deterministinen, korkea = luova.

**Vektoripohjainen tietokanta** – Erityinen tietokanta empattatoille. [Moduuli 03](../03-rag/README.md)

**Zero-Shot-opetus** – Tehtävien suorittaminen ilman esimerkkejä. [Moduuli 02](../02-prompt-engineering/README.md)

## Suojakeinot - [Moduuli 00](../00-quick-start/README.md)

**Suojaus kerroksittain** – Monikerroksinen turvallisuus, jossa yhdistetään sovellustason suojakeinot tarjoajien turvasuodattimiin.

**Kova esto** – Tarjoaja palauttaa HTTP 400 -virheen vakavista sisältörikkomuksista.

**InputGuardrail** – LangChain4j-rajapinta käyttäjän syötteen validointiin ennen LLM:ää. Säästää kustannuksia ja viivettä estämällä haitalliset promptit varhaisessa vaiheessa.

**InputGuardrailResult** – Palautustyyppi suojakeinon validoinnista: `success()` tai `fatal("reason")`.

**OutputGuardrail** – Rajapinta AI-vastausten validointiin ennen käyttäjille palauttamista.

**Tarjoajan turvasuodattimet** – Sisäänrakennetut sisältösuodattimet AI-tarjoajilta (esim. GitHubin mallit), jotka havaitsevat loukkaukset API-tasolla.

**Pehmeä kieltäytyminen** – Malli kohteliaasti kieltäytyy vastaamasta ilman virheilmoitusta.

## Promptin suunnittelu - [Moduuli 02](../02-prompt-engineering/README.md)

**Ajatusketju** – Vaiheittainen päättely paremman tarkkuuden saavuttamiseksi.

**Rajoitettu vastaus** – Tietyn muodon tai rakenteen vaatiminen.

**Korkea innokkuus** – GPT-5.2-malli perusteelliselle päättelylle.

**Matala innokkuus** – GPT-5.2-malli nopeille vastauksille.

**Monikierroksinen keskustelu** – Kontekstin ylläpito vuorovaikutusten välillä.

**Roolipohjainen prompttaus** – Mallin persoonan asettaminen järjestelmäviestillä.

**Itsearviointi** – Malli arvioi ja parantaa tuottamaansa sisältöä.

**Rakenteellinen analyysi** – Kiinteä arviointikehys.

**Tehtävän suoritusmalli** – Suunnittele → Suorita → Tiivistä.

## RAG (Retrieval-Augmented Generation) - [Moduuli 03](../03-rag/README.md)

**Asiakirjankäsittelyputki** – Lataa → palastele → upota → tallenna.

**Muistipohjainen empattiovarasto** – Ei-pysyvä tallennus testaukseen.

**RAG** – Yhdistää haun ja generoinnin maadoittaakseen vastaukset.

**Vastaavuuspistemäärä** – Semanttisen samankaltaisuuden mitta (0–1).

**Lähdeviite** – Metatiedot haetusta sisällöstä.

## Agentit ja työkalut - [Moduuli 04](../04-tools/README.md)

**@Tool-merkintä** – Merkitsee Java-metodit AI-kutsuttaviksi työkaluiksi.

**ReAct-kuvio** – Päättely → Toiminta → Havainnointi → Toisto.

**Istunnon hallinta** – Eri käyttäjille omat kontekstit.

**Työkalu** – Toiminto, jota AI-agentti voi kutsua.

**Työkalun kuvaus** – Dokumentaatio työkalun tarkoituksesta ja parametreista.

## Agenttimoduuli - [Moduuli 05](../05-mcp/README.md)

**@Agent-merkintä** – Merkitsee rajapinnat AI-agenteiksi, joissa käytetään deklaratiivista käyttäytymismäärittelyä.

**Agenttikuuntelija** – Koukku agentin suorituksen seurannalle `beforeAgentInvocation()`- ja `afterAgentInvocation()`-kutsujen kautta.

**Agenttinen konteksti** – Jaettu muisti, johon agentit tallentavat tuloksia `outputKey`:n avulla muiden agenttien käytettäväksi.

**AgenticServices** – Tehdas agenttien luomiseen `agentBuilder()` ja `supervisorBuilder()` avulla.

**Ehdollinen työnkulku** – Reititys tilanneperusteisesti eri erikoistuneille agenteille.

**Ihminen silmukassa** – Työnkulku, joka lisää ihmisen tarkastuspisteitä hyväksyntää tai sisällön tarkistusta varten.

**langchain4j-agentic** – Maven-riippuvuus deklaratiiviseen agenttien rakentamiseen (kokeellinen).

**Toistuva työnkulku** – Toistaa agentin suorituksen, kunnes ehto täyttyy (esim. laatupistemäärä ≥ 0.8).

**outputKey** – Agenttimerkinnän parametri, joka määrittää, mihin Agentic Scope:n tulokset tallennetaan.

**Rinnakkainen työnkulku** – Suorittaa useita agenteja samanaikaisesti riippumattomissa tehtävissä.

**Vastausstrategia** – Miten valvoja muotoilee lopullisen vastauksen: LAST, SUMMARY tai SCORED.

**Peräkkäinen työnkulku** – Suorittaa agentit järjestyksessä, jossa tulos siirtyy seuraavaan vaiheeseen.

**Valvojaagenttikuva** – Edistynyt agenttikuva, jossa valvoja-LLM päättää dynaamisesti, mitä alianteja kutsutaan.

## Mallin kontekstiprotokolla (MCP) - [Moduuli 05](../05-mcp/README.md)

**langchain4j-mcp** – Maven-riippuvuus MCP-integraatiolle LangChain4j:ssa.

**MCP** – Mallin kontekstiprotokolla: standardi tekoälysovellusten liittämiseen ulkoisiin työkaluihin. Luo kerran, käytä kaikkialla.

**MCP-asiakas** – Sovellus, joka yhdistyy MCP-palvelimiin löytääkseen ja käyttäen työkaluja.

**MCP-palvelin** – Palvelu, joka tarjoaa työkaluja MCP:n kautta selkeillä kuvauksilla ja parametrikaavoilla.

**McpToolProvider** – LangChain4j-komponentti, joka käärii MCP-työkalut tekoälypalveluihin ja agenteille.

**McpTransport** – Rajapinta MCP-yhteydelle. Toteutuksia ovat mm. Stdio ja HTTP.

**Stdio Transport** – Paikallinen prosessin kuljetus stdin/stdout:n kautta. Hyödyllinen tiedostojärjestelmään pääsyyn tai komentorivityökaluihin.

**StdioMcpTransport** – LangChain4j-toteutus, joka käynnistää MCP-palvelimen aliprosessina.

**Työkalujen löytäminen** – Asiakas kysyy palvelimelta käytettävissä olevat työkalut kuvauksineen ja kaavoineen.

## Azure-palvelut - [Moduuli 01](../01-introduction/README.md)

**Azure AI Search** – Pilvipohjainen haku vektorikapasiteeteilla. [Moduuli 03](../03-rag/README.md)

**Azure Developer CLI (azd)** – Ottaa käyttöön Azure-resursseja.

**Azure OpenAI** – Microsoftin yritystason tekoälypalvelu.

**Bicep** – Azure infrastruktuurikoodi-kieli. [Infrastruktuuriohje](../01-introduction/infra/README.md)

**Käyttöönoton nimi** – Nimi mallin käyttöönotolle Azure:ssa.

**GPT-5.2** – Uusin OpenAI-malli päättelyn ohjauksella. [Moduuli 02](../02-prompt-engineering/README.md)

## Testaus ja kehitys - [Testausopas](TESTING.md)

**Dev Container** – Konttisoitu kehitysympäristö. [Konfiguraatio](../../../.devcontainer/devcontainer.json)

**GitHubin mallit** – Ilmainen tekoälymallien leikkikenttä. [Moduuli 00](../00-quick-start/README.md)

**Muistipohjainen testaus** – Testaus muistivarastolla.

**Integraatiotestaus** – Testaus todellisella infrastruktuurilla.

**Maven** – Java-rakennusautomaatiotyökalu.

**Mockito** – Java-mokkauksien kirjasto.

**Spring Boot** – Java-sovelluskehys. [Moduuli 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, huomioithan, että automaattikäännöksissä saattaa esiintyä virheitä tai epätarkkuuksia. Alkuperäistä asiakirjaa sen alkuperäisellä kielellä tulee pitää auktoritatiivisena lähteenä. Tärkeissä tiedoissa suosittelemme ammattimaisen ihmiskääntäjän käyttämistä. Emme ole vastuussa mahdollisista väärinymmärryksistä tai tulkinnoista, jotka johtuvat tämän käännöksen käytöstä.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->