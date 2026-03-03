# Moduuli 03: RAG (Hakua laajentava generointi)

## Sisällysluettelo

- [Video läpikäynti](../../../03-rag)
- [Mitä opit](../../../03-rag)
- [Esivaatimukset](../../../03-rag)
- [RAG:n ymmärtäminen](../../../03-rag)
  - [Mikä RAG-lähestymistapa tätä opetusohjelmaa käyttää?](../../../03-rag)
- [Miten se toimii](../../../03-rag)
  - [Dokumentin käsittely](../../../03-rag)
  - [Embeddingsien luominen](../../../03-rag)
  - [Semanttinen haku](../../../03-rag)
  - [Vastauksen generointi](../../../03-rag)
- [Sovelluksen suorittaminen](../../../03-rag)
- [Sovelluksen käyttö](../../../03-rag)
  - [Dokumentin lataaminen](../../../03-rag)
  - [Kysymysten esittäminen](../../../03-rag)
  - [Lähdeviitteiden tarkistaminen](../../../03-rag)
  - [Kokeile kysymyksiä](../../../03-rag)
- [Keskeiset käsitteet](../../../03-rag)
  - [Paloittelustrategia](../../../03-rag)
  - [Samankaltaisuuspisteet](../../../03-rag)
  - [Muistissa säilytys](../../../03-rag)
  - [Kontekstin hallinta](../../../03-rag)
- [Milloin RAG on tärkeä](../../../03-rag)
- [Seuraavat askeleet](../../../03-rag)

## Video läpikäynti

Katso tämä live-sessio, joka selittää, miten alkaa työskennellä tämän moduulin kanssa:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## Mitä opit

Edellisissä moduuleissa opit, kuinka käydä keskusteluja tekoälyn kanssa ja järjestää kehotteet tehokkaasti. Mutta on olemassa perustavanlaatuinen rajoitus: kielimallit tietävät vain sen, mitä ne oppivat harjoittelun aikana. Ne eivät voi vastata kysymyksiin yrityksesi käytännöistä, projektidokumentaatiosta tai muista tiedoista, joihin niitä ei ole koulutettu.

RAG (Retrieval-Augmented Generation) ratkaisee tämän ongelman. Sen sijaan, että yritettäisiin opettaa mallille tietosi (mikä on kallista ja epäkäytännöllistä), annat mallille kyvyn hakea tietoa dokumenteistasi. Kun joku esittää kysymyksen, järjestelmä löytää relevanttia tietoa ja lisää sen kehotteeseen. Malli vastaa sitten haetun kontekstin perusteella.

Ajattele RAG:ia kuin mallille annettavaa viitelähdekirjastoa. Kun kysyt kysymyksen, järjestelmä:

1. **Käyttäjän kysely** - Esität kysymyksen
2. **Embedding** - Muuntaa kysymyksen vektoriksi
3. **Vektorihaku** - Löytää samankaltaiset dokumenttipalat
4. **Kontekstin kokoaminen** - Lisää merkitykselliset palat kehotteeseen
5. **Vastaus** - LLM luo vastauksen kontekstin perusteella

Näin mallin vastaukset perustuvat todelliseen dataasi sen sijaan, että ne vain nojaisivat koulutustietoonsa tai keksisivät vastauksia.

## Esivaatimukset

- Suoritettu [Moduuli 00 - Nopeasti alkuun](../00-quick-start/README.md) (Easy RAG -esimerkin käyttöönottoa varten, jota viitataan tässä moduulissa)
- Suoritettu [Moduuli 01 - Johdanto](../01-introduction/README.md) (Azure OpenAI -resurssit käyttöön otettuna, mukaan lukien `text-embedding-3-small` embedding-malli)
- `.env`-tiedosto juurihakemistossa Azure-tunnuksilla (luotu `azd up` komennolla Moduulissa 01)

> **Huom:** Jos et ole suorittanut Moduulia 01, seuraa ensin siellä annettuja käyttöönotto-ohjeita. `azd up` -komento ottaa käyttöön sekä GPT-chat-mallin että tämän moduulin käyttämän embedding-mallin.

## RAG:n ymmärtäminen

Alla oleva kaavio havainnollistaa ydinkäsitettä: sen sijaan, että luotettaisiin ainoastaan mallin koulutusdataan, RAG antaa mallille viitelähdekirjaston dokumenteistasi, joita se voi käyttää ennen kunkin vastauksen generointia.

<img src="../../../translated_images/fi/what-is-rag.1f9005d44b07f2d8.webp" alt="Mikä on RAG" width="800"/>

*Tämä kaavio näyttää eron perinteisen LLM:n (joka arvaa koulutusdatan pohjalta) ja RAG-laajennetun LLM:n välillä (joka konsultoi ensin dokumenttejasi).*

Näin prosessin osat liittyvät toisiinsa päästä päähän. Käyttäjän kysymys kulkee neljän vaiheen läpi — embedding, vektorihaku, kontekstin kokoaminen ja vastauksen generointi — jokainen rakentuu edellisen päälle:

<img src="../../../translated_images/fi/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG-arkkitehtuuri" width="800"/>

*Tämä kaavio näyttää RAG-putken päästä päähän — käyttäjän kysely kulkee embeddingin, vektorihakujen, kontekstin kokoamisen ja vastauksen generoinnin läpi.*

Tämän moduulin loput osat käyvät läpi kukin vaiheen yksityiskohtaisesti, sisältäen koodin jota voit ajaa ja muokata.

### Mikä RAG-lähestymistapa tätä opetusohjelmaa käyttää?

LangChain4j tarjoaa kolme tapaa toteuttaa RAG, joista jokainen tarjoaa eri tason abstraktiota. Alla oleva kaavio vertailee niitä rinnakkain:

<img src="../../../translated_images/fi/rag-approaches.5b97fdcc626f1447.webp" alt="Kolme RAG-lähestymistapaa LangChain4j:ssa" width="800"/>

*Tämä kaavio vertailee LangChain4j:n kolmea RAG-lähestymistapaa — Easy, Native ja Advanced — ja näyttää niiden keskeiset osat sekä käyttötarkoitukset.*

| Lähestymistapa | Mitä se tekee | Kompromissi |
|---|---|---|
| **Easy RAG** | Kytkee kaiken automaattisesti `AiServices` ja `ContentRetriever` kautta. Määrittelet rajapinnan, liität haun, ja LangChain4j hoitaa taustalla dosenttien upotuksen, haun ja kehotteen kokoonpanon. | Vähiten koodia, mutta et näe mitä kukin vaihe tarkalleen tekee. |
| **Native RAG** | Kutsut upotusmallia, haet tallennuspaikasta, rakennat kehotteen ja generoit vastauksen – yksi näkyvä vaihe kerrallaan. | Enemmän koodia, mutta jokainen vaihe näkyvissä ja muokattavissa. |
| **Advanced RAG** | Käyttää `RetrievalAugmentor`-kehystä, jossa on plugarit kyselymuuntajille, reitittimille, uudelleenjärjestäjille ja sisällön injektoreille tuotantotason putkissa. | Maksimaalinen joustavuus, mutta merkittävästi monimutkaisempi. |

**Tämä opetusohjelma käyttää Native-lähestymistapaa.** RAG-putken jokainen vaihe — kyselyn upotus, vektorikaupan haku, kontekstin kokoaminen ja vastauksen generointi — on kirjoitettu selkeästi tiedostossa [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Tämä on tarkoituksellista: oppimateriaaliin on tärkeämpää, että näet ja ymmärrät jokaisen vaiheen kuin että koodi olisi minimoitu. Kun olet mukava sen kanssa miten osat sopivat yhteen, voit siirtyä Easy RAG:n pariin nopeisiin prototyyppeihin tai Advanced RAG:n tuotantojärjestelmiin.

> **💡 Oletko jo nähnyt Easy RAG:n toiminnassa?** [Nopeasti alkuun -moduuli](../00-quick-start/README.md) sisältää Document Q&A -esimerkin ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)), joka käyttää Easy RAG -lähestymistapaa — LangChain4j hoitaa upotuksen, haun ja kehotteen kokoonpanon automaattisesti. Tämä moduuli vie sen seuraavalle tasolle avaamalla putken, jotta voit itse nähdä ja hallita jokaista vaihetta.

Alla oleva kaavio näyttää Easy RAG -putken tuosta Quick Start -esimerkistä. Huomaa, kuinka `AiServices` ja `EmbeddingStoreContentRetriever` piilottavat kaiken monimutkaisuuden — lataat dokumentin, liität hakijan ja saat vastaukset. Tämän moduulin Native-lähestymistapa avaa jokaisen kätketyn vaiheen:

<img src="../../../translated_images/fi/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG -putki - LangChain4j" width="800"/>

*Tämä kaavio näyttää Easy RAG -putken tiedostosta `SimpleReaderDemo.java`. Vertaa sitä tämän moduulin Native-lähestymistapaan: Easy RAG piilottaa upotuksen, haun ja kehotteen kokoamisen `AiServices` ja `ContentRetriever` taakse — lataat dokumentin, liität hakijan ja saat vastaukset. Tämä moduulin Native-lähestymistapa avaa putken, jolloin kutsut jokaisen vaiheen (upota, hae, kokoa konteksti, generoi) itse, tarjoten täyden näkyvyyden ja kontrollin.*

## Miten se toimii

Tässä moduulissa RAG-putki jakautuu neljään vaiheeseen, jotka suoritetaan peräkkäin joka kerta kun käyttäjä esittää kysymyksen. Ensin ladattu dokumentti **jäsennetään ja pilkotaan paloiksi** hallittavaan muotoon. Nämä palat muunnetaan sitten **vektorisijoituksiksi** ja tallennetaan, jotta niitä voidaan vertailla matemaattisesti. Kun kysely saapuu, järjestelmä suorittaa **semanttisen haun** löytääkseen merkityksellisimmät osat, ja lopuksi välittää ne kontekstiksi LLM:lle **vastauksen generointia** varten. Alla olevat osiot käyvät läpi jokaisen vaiheen koodin ja kaavioiden kanssa. Katsotaanpa ensin dokumentin käsittelyä.

### Dokumentin käsittely

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Kun lataat dokumentin, järjestelmä jäsentää sen (PDF tai pelkkä teksti), liittää metatietoja kuten tiedostonimen, ja pilkkoo sen paloiksi — pienemmiksi osiksi, jotka mahtuvat mukavasti mallin kontekstin ikkunaan. Nämä palat limittyvät hieman, jotta rajapinnoissa ei menetetä kontekstia.

```java
// Jäsennä ladattu tiedosto ja kääri se LangChain4j-dokumenttiin
Document document = Document.from(content, metadata);

// Jaa 300-tokenin paloiksi, joissa on 30-tokenin päällekkäisyys
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Alla oleva kaavio havainnollistaa tätä visuaalisesti. Huomaa, miten jokainen pala jakaa joitain tokeneita naapurinsa kanssa — 30 tokenin limitys varmistaa, että tärkeä konteksti ei putoa rakoihin:

<img src="../../../translated_images/fi/document-chunking.a5df1dd1383431ed.webp" alt="Dokumenttipalojen pilkkominen" width="800"/>

*Tämä kaavio näyttää dokumentin pilkkomisen 300 tokenin paloihin, 30 tokenin limityksellä, jolloin konteksti säilyy paloissa.*

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatissa:** Avaa [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) ja kysy:
> - "Miten LangChain4j pilkkoo dokumentit paloihin ja miksi limitys on tärkeää?"
> - "Mikä on optimaalinen palakoko eri dokumenttityypeille ja miksi?"
> - "Miten käsittelen dokumentteja useilla kielillä tai erikoisformaatilla?"

### Embeddingsien luominen

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Jokainen pala muunnetaan numeeriseen esitykseen, jota kutsutaan embeddingiksi — käytännössä merkityksen muuttajaksi numeroiksi. Embedding-malli ei ole "älykäs" kuten chat-malli; se ei voi seurata ohjeita, järkeillä tai vastata kysymyksiin. Sen sijaan se muuntaa tekstin matemaattiseen tilaan, jossa samankaltaiset merkitykset sijoittuvat lähelle toisiaan — "car" lähelle "automobile", "refund policy" lähelle "return my money". Ajattele chat-mallia ihmisenä, jonka kanssa voit puhua; embedding-malli on erittäin hyvä arkistointijärjestelmä.

Alla oleva kaavio visualisoi tämän käsitteen — teksti syötetään sisään, numeeriset vektorit tulevat ulos, ja samankaltaiset merkitykset tuottavat lähellä sijaitsevia vektoreita:

<img src="../../../translated_images/fi/embedding-model-concept.90760790c336a705.webp" alt="Embedding-mallin konsepti" width="800"/>

*Tämä kaavio näyttää miten embedding-malli muuntaa tekstin numeerisiksi vektoreiksi, sijoittaen saman merkityksen sanat kuten "car" ja "automobile" lähelle toisiaan vektoritilassa.*

```java
@Bean
public EmbeddingModel embeddingModel() {
    return OpenAiOfficialEmbeddingModel.builder()
        .baseUrl(azureOpenAiEndpoint)
        .apiKey(azureOpenAiKey)
        .modelName(azureEmbeddingDeploymentName)
        .build();
}

EmbeddingStore<TextSegment> embeddingStore = 
    new InMemoryEmbeddingStore<>();
```
  
Alla oleva luokkakaavio näyttää kaksi erillistä virtaa RAG-putkessa ja LangChain4j-luokat, jotka toteuttavat ne. **Ingestiovirta** (suoritetaan kerran latauksen yhteydessä) pilkkoo dokumentin, muuntaa palat upotuksiksi ja tallentaa ne `.addAll()` metodilla. **Kyselyvirta** (suoritetaan aina kun käyttäjä kysyy) upottaa kysymyksen, etsii tallennuspaikasta `.search()` metodilla ja välittää osuneet kontekstit chat-mallille. Molemmat virrat kohtaavat jaetun `EmbeddingStore<TextSegment>`-rajapinnan kautta:

<img src="../../../translated_images/fi/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG -luokat" width="800"/>

*Tämä kaavio näyttää kaksi virtaa RAG-putkessa — datajen syöttö ja kysely — ja miten ne yhdistyvät yhteisen EmbeddingStoren kautta.*

Kun embeddings on tallennettu, samankaltaiset sisällöt ryhmittyvät luontaisesti vektoritilassa yhteen. Alla oleva visualisointi näyttää, miten aihepiirit kuten Tekninen dokumentaatio, Liiketoiminnan säännöt ja Usein kysytyt kysymykset muodostavat selkeitä ryhmiä, mikä mahdollistaa semanttisen haun:

<img src="../../../translated_images/fi/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektorien upotustila" width="800"/>

*Tämä visualisointi näyttää miten aihealueisiin liittyvät dokumentit ryhmittyvät 3D-vektoritilassa.*

Kun käyttäjä hakee, järjestelmä seuraa neljää vaihetta: dokumentit upotetaan kerran, kysely upotetaan jokaisella haulla, kyselyvektori verrataan kaikkiin tallennettuihin vektoreihin kosinisimilariteetin avulla, ja palautetaan top-K parhaat osumat. Alla oleva kaavio näyttää jokaisen vaiheen ja niihin liittyvät LangChain4j-luokat:

<img src="../../../translated_images/fi/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding-haun vaiheet" width="800"/>

*Tämä kaavio näyttää neljä vaihetta embedding-haussa: dokumenttien upotus, kyselyn upotus, vektorien vertailu kosinisimilariteetilla, ja parhaiden K tulosten palautus.*

### Semanttinen haku

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kun esität kysymyksen, kysymyksestä muodostetaan myös embedding. Järjestelmä vertaa kysymyksesi embeddingiä kaikkiin dokumenttipalojen embeddingsiin. Se löytää ne palat, joiden merkitys on lähimpänä — ei pelkästään avainsanojen vastaavuuden perusteella, vaan todellisen semanttisen samankaltaisuuden.

```java
Embedding queryEmbedding = embeddingModel.embed(question).content();

EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
    .queryEmbedding(queryEmbedding)
    .maxResults(5)
    .minScore(0.5)
    .build();

EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);
List<EmbeddingMatch<TextSegment>> matches = searchResult.matches();

for (EmbeddingMatch<TextSegment> match : matches) {
    String relevantText = match.embedded().text();
    double score = match.score();
}
```
  
Alla oleva kaavio vertaa semanttista hakua perinteiseen avainsanahakuun. Avainsanahaku sanalla "vehicle" ohittaa palan, jossa puhutaan "autoista ja kuorma-autoista", mutta semanttinen haku ymmärtää, että ne tarkoittavat samaa asiaa ja palauttaa sen korkealla pistemäärällä:

<img src="../../../translated_images/fi/semantic-search.6b790f21c86b849d.webp" alt="Semanttinen haku" width="800"/>

*Tämä kaavio vertailee avainsanapohjaista ja semanttista hakua, näyttäen miten semanttinen haku löytää käsitteellisesti liittyvää sisältöä, vaikka tarkat avainsanat eroavat.*
Alla olevassa arvossa samankaltaisuutta mitataan kosinisamankaltaisuuden avulla — käytännössä kysytään "osoittavatko nämä kaksi nuolta samaan suuntaan?" Kaksi tekstipalasta voivat käyttää täysin eri sanoja, mutta jos ne tarkoittavat samaa asiaa, niiden vektorit osoittavat samaan suuntaan ja pisteet ovat lähellä 1.0:

<img src="../../../translated_images/fi/cosine-similarity.9baeaf3fc3336abb.webp" alt="Kosinisamankaltaisuus" width="800"/>

*Tämä kaavio havainnollistaa kosinisamankaltaisuutta upotettujen vektoreiden välisenä kulmana — enemmän linjassa olevat vektorit saavat pisteet lähempänä 1.0, mikä osoittaa suurempaa semanttista samankaltaisuutta.*

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatilla:** Avaa [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) ja kysy:
> - "Miten samankaltaisuushaku toimii upotusten kanssa ja mikä määrää pistemäärän?"
> - "Mitä samankaltaisuuskynnystä minun tulisi käyttää ja miten se vaikuttaa tuloksiin?"
> - "Miten käsittelen tilanteet, joissa ei löydy relevantteja dokumentteja?"

### Vastausten tuottaminen

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Merkityksellisimmät tekstipalaset kootaan rakenteelliseksi kehotteeksi, joka sisältää selkeät ohjeet, haetun kontekstin ja käyttäjän kysymyksen. Malli lukee nämä tietyt palaset ja vastaa niiden perusteella — se voi käyttää vain sitä tietoa, mikä sillä on edessään, mikä estää harhan muodostumisen.

```java
String context = matches.stream()
    .map(match -> match.embedded().text())
    .collect(Collectors.joining("\n\n"));

String prompt = String.format("""
    Answer the question based on the following context.
    If the answer cannot be found in the context, say so.

    Context:
    %s

    Question: %s

    Answer:""", context, request.question());

String answer = chatModel.chat(prompt);
```

Alla oleva kaavio näyttää tämän kokoamisen toiminnassa — haun vaiheessa parhaat pisteet saaneet tekstipalaset sijoitetaan kehotteen malliin, ja `OpenAiOfficialChatModel` tuottaa perustellun vastauksen:

<img src="../../../translated_images/fi/context-assembly.7e6dd60c31f95978.webp" alt="Kontekstin kokoaminen" width="800"/>

*Tässä kaaviossa näytetään, miten parhaiten pisteytetyt tekstipalaset kootaan rakenteelliseksi kehotteeksi, jolloin malli voi tuottaa perustellun vastauksen datastasi.*

## Sovelluksen käynnistäminen

**Tarkista käyttöönotto:**

Varmista, että juuri hakemistossa on `.env`-tiedosto, jossa on Azure-tunnukset (luotu moduulissa 01). Suorita tämä moduulin hakemistosta (`03-rag/`):

**Bash:**
```bash
cat ../.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Käynnistä sovellus:**

> **Huom:** Jos olet jo käynnistänyt kaikki sovellukset käyttämällä `./start-all.sh` juuri hakemistosta (kuten moduulissa 01 kuvattu), tämä moduuli on jo käynnissä portissa 8081. Voit ohittaa alla olevat käynnistyskomennot ja mennä suoraan osoitteeseen http://localhost:8081.

**Vaihtoehto 1: Spring Boot Dashboardin käyttäminen (suositeltu VS Code -käyttäjille)**

Kehityssäiliö sisältää Spring Boot Dashboard -laajennuksen, joka tarjoaa visuaalisen käyttöliittymän kaikkien Spring Boot -sovellusten hallintaan. Löydät sen Activity Barista VS Codessa vasemmalta (etsi Spring Boot -kuvaketta).

Spring Boot Dashboardista voit:
- Nähdä kaikki käytettävissä olevat Spring Boot -sovellukset työtilassa
- Käynnistää/pysäyttää sovelluksia yhdellä napsautuksella
- Katsoa sovelluslokeja reaaliajassa
- Seurata sovellusten tilaa

Napsauta pelipainiketta "rag" vierestä käynnistääksesi tämän moduulin, tai käynnistä kaikki moduulit kerralla.

<img src="../../../translated_images/fi/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Tässä kuvakaappauksessa näkyy Spring Boot Dashboard VS Codessa, jossa voit käynnistää, pysäyttää ja valvoa sovelluksia visuaalisesti.*

**Vaihtoehto 2: Shell-skriptien käyttäminen**

Käynnistä kaikki web-sovellukset (moduulit 01-04):

**Bash:**
```bash
cd ..  # Juurihakemistosta
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Juurihakemistosta
.\start-all.ps1
```

Tai käynnistä vain tämä moduuli:

**Bash:**
```bash
cd 03-rag
./start.sh
```

**PowerShell:**
```powershell
cd 03-rag
.\start.ps1
```

Molemmat skriptit lataavat automaattisesti ympäristömuuttujat juuren `.env`-tiedostosta ja rakentavat JAR-tiedostot tarvittaessa.

> **Huom:** Jos haluat rakentaa kaikki moduulit manuaalisesti ennen käynnistystä:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Avaa selain ja mene osoitteeseen http://localhost:8081.

**Pysäytys:**

**Bash:**
```bash
./stop.sh  # Vain tämä moduuli
# Tai
cd .. && ./stop-all.sh  # Kaikki moduulit
```

**PowerShell:**
```powershell
.\stop.ps1  # Vain tämä moduuli
# Tai
cd ..; .\stop-all.ps1  # Kaikki moduulit
```

## Sovelluksen käyttäminen

Sovellus tarjoaa verkkokäyttöliittymän dokumenttien lataamiseen ja kysymyksiin.

<a href="images/rag-homepage.png"><img src="../../../translated_images/fi/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG-sovelluksen käyttöliittymä" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Tämä kuvakaappaus näyttää RAG-sovelluksen käyttöliittymän, jossa voit ladata dokumentteja ja esittää kysymyksiä.*

### Dokumentin lataaminen

Aloita lataamalla dokumentti — TXT-tiedostot toimivat parhaiten testauksessa. Tässä hakemistossa on mukana `sample-document.txt`, joka sisältää tietoja LangChain4j:n ominaisuuksista, RAG:n toteutuksesta ja parhaista käytännöistä — täydellinen testausjärjestelmäksi.

Järjestelmä käsittelee dokumenttisi, pilkkoo sen tekstipalasiin ja luo upotukset jokaiselle palaselle. Tämä tapahtuu automaattisesti latauksen yhteydessä.

### Kysy kysymyksiä

Esitä nyt konkreettisia kysymyksiä dokumentin sisällöstä. Kokeile jotain faktapohjaista, joka on selkeästi dokumentissa mainittu. Järjestelmä hakee relevantit palaset, liittää ne kehotteeseen ja tuottaa vastauksen.

### Tarkista lähdemerkinnät

Huomaa, että jokainen vastaus sisältää lähdeviitteitä samankaltaisuuspisteillä. Nämä pisteet (0–1) osoittavat, kuinka relevantti kukin pala oli kysymykseesi nähden. Korkeammat pisteet tarkoittavat parempia osumia. Näin voit varmistaa vastauksen vastaavuuden lähdemateriaaliin.

<a href="images/rag-query-results.png"><img src="../../../translated_images/fi/rag-query-results.6d69fcec5397f355.webp" alt="RAG-kyselytulokset" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Tämä kuvakaappaus näyttää kyselytulokset, generoitu vastauksen, lähdemerkinnät ja merkityspisteet jokaiselle haetulle tekstipalalle.*

### Kokeile erilaisia kysymyksiä

Kokeile erilaisia kysymystyyppejä:
- Tarkat faktat: "Mikä on pääaihe?"
- Vertailut: "Mikä on ero X:n ja Y:n välillä?"
- Yhteenvedot: "Tiivistä tärkeimmät kohdat Z:stä"

Seuraa, miten merkityspisteet muuttuvat sen mukaan, kuinka hyvin kysymyksesi vastaa dokumentin sisältöä.

## Keskeiset käsitteet

### Tekstipalojen muodostaminen

Dokumentit jaetaan 300 tokenin tekstipalasiin, joissa on 30 tokenin päällekkäisyys. Tämä tasapaino varmistaa, että jokaisella palasella on riittävästi kontekstia ollakseen merkityksellinen, mutta ne pysyvät tarpeeksi pieniä, jotta useita palasia mahtuu kehotteeseen.

### Samankaltaisuuspisteet

Jokaisella haetulla tekstipalalla on samankaltaisuuspiste, jonka arvo on 0–1 ja joka osoittaa, kuinka tarkasti se vastaa käyttäjän kysymystä. Alla oleva kaavio visualisoi pistemäärien vaihtelut ja miten järjestelmä käyttää niitä tulosten suodattamiseen:

<img src="../../../translated_images/fi/similarity-scores.b0716aa911abf7f0.webp" alt="Samankaltaisuuspisteet" width="800"/>

*Tämä kaavio näyttää pistemääräalueet 0:sta 1:een, vähimmäiskynnyksen ollessa 0.5, joka suodattaa pois ei-relevantit tekstipalaset.*

Pisteet vaihtelevat välillä 0–1:
- 0.7–1.0: Erittäin relevantti, täsmää tarkasti
- 0.5–0.7: Relevantti, hyvä konteksti
- Alle 0.5: Suodatettu pois, liian erilainen

Järjestelmä hakee vain vähimmäiskynnyksen ylittävät tekstipalaset laadun varmistamiseksi.

Upotukset toimivat hyvin, kun merkitys ryhmittyy selkeästi, mutta niillä on sokeita pisteitä. Alla oleva kaavio näyttää yleisimmät virhetilanteet — liian isot tekstipalaset tuottavat epäselvät vektorit, liian pienillä paikoilla ei ole kontekstia, epämääräiset termit osoittavat useisiin ryhmiin, ja täsmähaku (tunnisteet, osanumerot) ei toimi lainkaan upotusten kanssa:

<img src="../../../translated_images/fi/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Upotusvirheiden tyypit" width="800"/>

*Tämä kaavio näyttää yleisimmät upotusvirheiden tyypit: liian isot tai pienet tekstipalaset, epämääräiset termit, jotka osoittavat useisiin ryhmiin, sekä täsmähaku kuten tunnisteet.*

### Muistissa oleva tallennus

Tässä moduulissa käytetään yksinkertaisuuden vuoksi muistipohjaista tallennusta. Kun käynnistät sovelluksen uudelleen, ladatut dokumentit katoavat. Tuotantojärjestelmissä käytetään pysyviä vektoritietokantoja, kuten Qdrant tai Azure AI Search.

### Kontekstiikkunan hallinta

Jokaisella mallilla on maksimi kontekstiikkunan koko. Et voi sisällyttää kaikkia tekstipaloja suurista dokumenteista. Järjestelmä hakee viisi parhaimmin relevanttia tekstipalasta (oletus 5) pitäen kontekstin koossa vastauksen tarkkuuden takaamiseksi.

## Milloin RAG on olennaista

RAG ei ole aina oikea lähestymistapa. Alla oleva päätöspuu auttaa sinua päättämään, milloin RAG tuo lisäarvoa, ja milloin yksinkertaisemmat lähestymistavat — kuten sisällön suora sisällyttäminen kehotteeseen tai mallin sisäisen tiedon hyödyntäminen — riittävät:

<img src="../../../translated_images/fi/when-to-use-rag.1016223f6fea26bc.webp" alt="Milloin käyttää RAG:ia" width="800"/>

*Tämä kaavio näyttää päätöspuun siitä, milloin RAG tuo lisäarvoa verrattuna yksinkertaisempiin menetelmiin.*

## Seuraavat askeleet

**Seuraava moduuli:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Navigointi:** [← Edellinen: Moduuli 02 - Kehoteinsinööri](../02-prompt-engineering/README.md) | [Takaisin pääsivulle](../README.md) | [Seuraava: Moduuli 04 - Työkalut →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty tekoälykäännöspalvelulla [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, huomioithan, että automaattiset käännökset saattavat sisältää virheitä tai epätarkkuuksia. Alkuperäistä asiakirjaa sen alkuperäiskielellä tulee pitää virallisena lähteenä. Tärkeässä tiedossa suositellaan ammattilaisen tekemää ihmiskäännöstä. Emme ole vastuussa tästä käännöksestä aiheutuvista väärinymmärryksistä tai virhetulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->