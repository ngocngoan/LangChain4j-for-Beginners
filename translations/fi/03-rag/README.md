# Moduuli 03: RAG (Retrieval-Augmented Generation)

## Sisällysluettelo

- [Videokävely](../../../03-rag)
- [Mitä opit](../../../03-rag)
- [Esivaatimukset](../../../03-rag)
- [RAG:n ymmärtäminen](../../../03-rag)
  - [Mitä RAG-lähestymistapaa tämä opas käyttää?](../../../03-rag)
- [Miten se toimii](../../../03-rag)
  - [Dokumentin käsittely](../../../03-rag)
  - [Embeddingien luonti](../../../03-rag)
  - [Semanttinen haku](../../../03-rag)
  - [Vastausten generointi](../../../03-rag)
- [Sovelluksen suorittaminen](../../../03-rag)
- [Sovelluksen käyttäminen](../../../03-rag)
  - [Dokumentin lataaminen](../../../03-rag)
  - [Kysymysten esittäminen](../../../03-rag)
  - [Lähdeviitteiden tarkistaminen](../../../03-rag)
  - [Kokeile kysymyksiä](../../../03-rag)
- [Keskeiset käsitteet](../../../03-rag)
  - [Palojen pilkkomisstrategia](../../../03-rag)
  - [Samankaltaisuuspisteet](../../../03-rag)
  - [Muistissa tapahtuva tallennus](../../../03-rag)
  - [Kontekstin hallinta](../../../03-rag)
- [Milloin RAG on tärkeä](../../../03-rag)
- [Seuraavat askeleet](../../../03-rag)

## Videokävely

Katso tämä liveistunto, joka selittää, miten moduulin kanssa pääsee alkuun: [RAG with LangChain4j - Live Session](https://www.youtube.com/watch?v=_olq75ZH_eY)

## Mitä opit

Edellisissä moduuleissa opit käymään keskusteluja tekoälyn kanssa ja muodostamaan pyyntöjä tehokkaasti. Mutta niiden perusrajoitus on: kielimallit tietävät vain sen, mitä ne oppivat harjoittelun aikana. Ne eivät voi vastata kysymyksiin yrityksesi politiikoista, projektidokumentaatiostasi tai mistä tahansa tiedosta, jota ne eivät ole oppineet.

RAG (Retrieval-Augmented Generation) ratkaisee tämän ongelman. Sen sijaan, että mallille opetettaisiin tietosi (mikä on kallista ja epäkäytännöllistä), annat sille kyvyn hakea asiakirjoistasi. Kun joku esittää kysymyksen, järjestelmä löytää siihen liittyvää tietoa ja lisää sen kehoteviestiin. Malli vastaa sitten kootun kontekstin perusteella.

Ajattele RAG:ia mallille annetuksi viitetietokirjastoksi. Kun kysyt kysymyksen, järjestelmä:

1. **Käyttäjän kysely** - Sinä esität kysymyksen  
2. **Embedding** - Muuntaa kysymyksesi vektoriksi  
3. **Vektorihaku** - Löytää samanlaisia dokumenttipaloja  
4. **Kontekstin kokoaminen** - Lisää aiheeseen liittyvät palat kehoteviestiin  
5. **Vastaus** - LLM luo vastauksen kontekstin perusteella  

Tämä sitoo mallin vastaukset todellisiin tietoihisi sen sijaan, että ne perustuisivat pelkkään harjoittelutietoon tai keksittäisiin vastauksia.

## Esivaatimukset

- Suoritettu [Moduuli 00 - Nopeasti alkuun](../00-quick-start/README.md) (Helpolle RAG-esimerkkille, johon yllä viitataan)  
- Suoritettu [Moduuli 01 - Johdanto](../01-introduction/README.md) (Azure OpenAI -resurssit käytössä, mukaan lukien `text-embedding-3-small` embedding-malli)  
- `.env`-tiedosto juuressa, jossa Azure-tunnukset (luotu `azd up` -komennolla Modulissa 01)  

> **Huom:** Jos et ole vielä suorittanut Moduulia 01, noudata ensin siellä annettuja käyttöönotto-ohjeita. `azd up` -komento ottaa käyttöön sekä GPT-keskustelumallin että tämän moduulin käyttämän embedding-mallin.

## RAG:n ymmärtäminen

Alla oleva kuva havainnollistaa ydinkäsitettä: sen sijaan, että luotettaisiin vain mallin harjoitteluaineistoon, RAG antaa sille asiakirjasi viitetietokirjastoksi, johon se voi tukeutua ennen jokaista vastauksen generointia.

<img src="../../../translated_images/fi/what-is-rag.1f9005d44b07f2d8.webp" alt="Mikä on RAG" width="800"/>

*Tämä kuva näyttää eron tavallisen LLM:n (joka arvaa harjoitteludatan perusteella) ja RAG-parannetun LLM:n (joka konsultoi ensin asiakirjojasi) välillä.*

Tässä miten palaset yhdistyvät päästä päähän. Käyttäjän kysymys liikkuu neljän vaiheen läpi — embedding, vektorihaku, kontekstin kokoaminen ja vastausten generointi — jokainen edellisen päälle rakentuva:

<img src="../../../translated_images/fi/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG-arkkitehtuuri" width="800"/>

*Tämä kuva näyttää RAG-putken päästä päähän — käyttäjän kysely kulkee embeddingin, vektorihakemisen, kontekstin kokoamisen ja vastausten generoinnin läpi.*

Tämän moduulin loput osat käyvät läpi jokaisen vaiheen yksityiskohtaisesti koodin kanssa, jota voit ajaa ja muokata.

### Mitä RAG-lähestymistapaa tämä opas käyttää?

LangChain4j tarjoaa kolme tapaa toteuttaa RAG, jokainen eri abstraktiotasolla. Alla oleva kuva vertaa niitä rinnakkain:

<img src="../../../translated_images/fi/rag-approaches.5b97fdcc626f1447.webp" alt="Kolme RAG-lähestymistapaa LangChain4j:ssa" width="800"/>

*Tämä kuva vertailee Kolmea LangChain4j:n RAG-lähestymistapaa — Helppo, Natiivi ja Edistynyt — ja näyttää niiden keskeiset osat ja käyttötarkoitukset.*

| Lähestymistapa | Mitä se tekee | Kompromissi |
|---|---|---|
| **Helppo RAG** | Kytkee kaiken automaattisesti `AiServices`- ja `ContentRetriever`-komponenttien kautta. Merkitset rajapinnan, liität hakutoiminnon, ja LangChain4j hoitaa embeddingin, haun ja kehoteviestin kokoamisen taustalla. | Vähäinen koodi, mutta et näe mitä tapahtuu jokaisessa vaiheessa. |
| **Natiivi RAG** | Kutsut embedding-mallin, haet tallennuksesta, koot kehoteviestin ja generoit vastauksen itse — yksi selkeä vaihe kerrallaan. | Lisää koodia, mutta jokainen vaihe on näkyvä ja muokattavissa. |
| **Edistynyt RAG** | Käyttää `RetrievalAugmentor`-kehystä, jossa on liitettäviä kyselyn muuntajia, reitittimiä, uudelleenjärjestäjiä ja sisältöinjektoreita tuotantotason putkia varten. | Maksimaalinen joustavuus, mutta huomattavasti monimutkaisempi. |

**Tämä opas käyttää Natiivia lähestymistapaa.** Jokainen RAG-putken vaihe — kyselyn embedding, vektorihakeminen, kontekstin kokoaminen ja vastauksen generointi — on kirjoitettu selkeästi tiedostossa [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Tämä on tarkoituksellista: opetusmateriaalina on tärkeämpää, että näet ja ymmärrät jokaisen vaiheen hahmottua, kuin että koodi olisi minimoitu. Kun tunnet palaset ja kuinka ne liittyvät toisiinsa, voit siirtyä Helppoon RAGiin nopeita prototyyppejä varten tai Edistyneeseen RAGiin tuotantojärjestelmiä varten.

> **💡 Oletko jo nähnyt Helpon RAGin toiminnassa?** [Nopeasti alkuun -moduuli](../00-quick-start/README.md) sisältää Document Q&A -esimerkin ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)), joka käyttää Helppoa RAG-lähestymistapaa — LangChain4j hoitaa embeddingin, haun ja kehoteviestin kokoamisen automaattisesti. Tämä moduuli ottaa seuraavan askeleen avaamalla putken niin, että voit nähdä ja hallita kutakin vaihetta itse.

<img src="../../../translated_images/fi/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Helppo RAG-putki - LangChain4j" width="800"/>

*Tämä kuva näyttää Helpon RAG-putken tiedostosta `SimpleReaderDemo.java`. Vertaa tätä Natiivin lähestymistavan kanssa, jota tämä moduuli käyttää: Helppo RAG piilottaa embeddingin, haun ja kehoteviestin kokoamisen `AiServices`- ja `ContentRetriever`-komponenttien taakse — lataat dokumentin, liität hakutoiminnon ja saat vastaukset. Natiivi lähestymistapa tässä moduulissa avaa putken siten, että kutsut jokaisen vaiheen (embed, haku, kontekstin kokoaminen, generointi) itse, tarjoten täyden näkyvyyden ja hallinnan.*

## Miten se toimii

Tämän moduulin RAG-putki jakautuu neljään vaiheeseen, jotka suoritetaan peräkkäin aina kun käyttäjä esittää kysymyksen. Ensin ladattu dokumentti **jäsennetään ja pilkotaan** hallittaviin palasiin. Nämä palat muunnetaan sitten **vektoriedustuksiksi (embeddings)**, jotka tallennetaan vertailun mahdollistamiseksi. Kun kysely saapuu, järjestelmä suorittaa **semanttisen haun** löytääkseen asiaankuuluvimmat palat, ja lopulta välittää ne kontekstina LLM:lle **vastauksen generointia** varten. Seuraavat osiot käyvät läpi jokaisen vaiheen koodin ja kaavioiden kera. Aloitetaan ensimmäisestä vaiheesta.

### Dokumentin käsittely

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Kun lataat dokumentin, järjestelmä jäsentää sen (PDF tai pelkkä teksti), liittää metatietoja kuten tiedostonimen ja pilkkoo sen sitten paloiksi — pienemmiksi osiksi, jotka mahtuvat mukavasti mallin konteksti-ikkunaan. Palaset hieman limittäytyvät, jotta kontekstin rajakohdissa ei menetetä tietoa.

```java
// Jäsennä ladattu tiedosto ja kääri se LangChain4j-dokumentiksi
Document document = Document.from(content, metadata);

// Pilko 300 tokenin paloiksi, joissa on 30 tokenin päällekkäisyys
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Alla oleva kaavio näyttää tämän toiminnan visuaalisesti. Huomaa, kuinka jokainen pala jakaa joitain tokeneita naapureidensa kanssa — 30 tokenin limitys varmistaa, ettei tärkeä konteksti putoa raoista:

<img src="../../../translated_images/fi/document-chunking.a5df1dd1383431ed.webp" alt="Dokumentin pilkkominen" width="800"/>

*Tämä kaavio osoittaa dokumentin pilkkomisen 300 tokenin paloiksi 30 tokenin limityksellä, jolloin konteksti rajojen välillä säilyy.*

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatissa:** Avaa [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) ja kysy:
> - "Miten LangChain4j pilkkoo dokumentit paloiksi ja miksi limitys on tärkeää?"
> - "Mikä on optimaalinen palakoko eri dokumenttityypeille ja miksi?"
> - "Miten käsitellä monikielisiä dokumentteja tai erityistä muotoilua?"

### Embeddingien luonti

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Jokainen pala muunnetaan numeeriseksi esitykseksi, jota kutsutaan embeddingiksi — käytännössä merkityksen muuntajaksi numeroiksi. Embedding-malli ei ole "älykäs" kuten keskustelumalli; se ei pysty seuraamaan ohjeita, päättämään tai vastaamaan kysymyksiin. Se voi kuitenkin kartoittaa tekstin matemaattiseen tilaan, jossa samanlaiset merkitykset sijoittuvat lähelle toisiaan — "auto" lähelle "ajoneuvo", "hyvityskäytäntö" lähelle "palauta rahani." Ajattele keskustelumallia ihmisenä, jolle voit puhua; embedding-malli on erinomaisen tehokas arkistointijärjestelmä.

<img src="../../../translated_images/fi/embedding-model-concept.90760790c336a705.webp" alt="Embedding-mallin käsite" width="800"/>

*Tämä kuva osoittaa, miten embedding-malli muuntaa tekstin numeerisiksi vektoreiksi, asettaen samanlaiset merkitykset — kuten "auto" ja "ajoneuvo" — lähelle toisiaan vektoritilassa.*

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
  
Luokkakaavio alla näyttää kaksi erillistä virtausta RAG-putkessa ja LangChain4j-luokat, jotka toteuttavat ne. **Ingestio-virtaus** (suoritetaan kerran latauksen yhteydessä) pilkkoo dokumentin, luo embeddingit paloille ja tallentaa ne `.addAll()`-kutsulla. **Kyselyvirtaus** (ajetaan jokaisella käyttäjän kyselyllä) luo kyselyn embeddingin, hakee tallennuksesta `.search()`-kutsulla ja välittää vastaavat kontekstit keskustelumallille. Molemmat virrat yhdistyvät jaettuun `EmbeddingStore<TextSegment>` -rajapintaan:

<img src="../../../translated_images/fi/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j:n RAG-luokat" width="800"/>

*Tämä kuva näyttää kaksi RAG-putken virtausta — ingestio ja kysely — ja kuinka ne yhdistyvät yhteisen EmbeddingStore-rajapinnan kautta.*

Kun embeddingit on tallennettu, samanlainen sisältö luonnollisesti ryhmittyy vektoritilassa. Alla oleva visualisointi näyttää, miten saman aihepiirin dokumentit päätyvät lähelle toisiaan, mikä tekee semanttisesta hausta mahdollista:

<img src="../../../translated_images/fi/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektoriedustusten tila" width="800"/>

*Tämä visualisointi näyttää, miten samankaltaiset dokumentit ryhmittyvät 3D-vektoritilassa eri aihealueiden, kuten Tekniset dokumentit, Liiketoimintasäännöt ja Usein kysytyt kysymykset, muodostaen erillisiä ryhmiä.*

Kun käyttäjä hakee, järjestelmä seuraa neljää askelta: embeddingin luonti dokumenteille kerran, kyselyn embedding jokaisella haulla, kyselyvektorin vertailu kaikkia tallennettuja vektoreita vastaan kosinisamanlaisuudella ja sadoista pala-alueista palautetaan top-K parasta osumaa. Alla oleva kaavio käy läpi jokaisen vaiheen ja siihen liittyvät LangChain4j-luokat:

<img src="../../../translated_images/fi/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding-haun vaiheet" width="800"/>

*Tämä kuva esittää nelivaiheisen embedding-haun prosessin: dokumenttien embedding, kyselyn embedding, vektorien vertailu kosinisamanlaisuudella ja parhaiden top-K tulosten palautus.*

### Semanttinen haku

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kun esität kysymyksen, myös kysymys muunnetaan embeddingiksi. Järjestelmä vertaa kysymyksesi embeddingiä kaikkien dokumenttipalojen embeddingeihin. Se löytää eniten merkitykseltään samankaltaiset palat — ei pelkästään avainsanojen osumat, vaan todellisen semanttisen samankaltaisuuden.

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
  
Alla oleva kaavio vertaa semanttista hakua perinteiseen avainsanahakuun. Avainsanahaku sanalla "ajoneuvo" ei löydä palaa "autot ja kuorma-autot", mutta semanttinen haku ymmärtää, että ne tarkoittavat samaa asiaa ja palauttaa tuloksen korkealla pisteellä:

<img src="../../../translated_images/fi/semantic-search.6b790f21c86b849d.webp" alt="Semanttinen haku" width="800"/>

*Tämä kuva vertaa avainsanaperusteista hakua ja semanttista hakua, ja näyttää kuinka semanttinen haku löytää käsitteellisesti läheistä sisältöä, vaikka tarkat avainsanat poikkeavat.*

Taustalla samankaltaisuus mitataan kosinisamanlaisuudella — käytännössä kysyen "osoittavatko nämä kaksi nuolta samaan suuntaan?" Kahdella palalla voi olla täysin erilaiset sanat, mutta jos ne tarkoittavat samaa, niiden vektorit osoittavat samaan suuntaan ja pisteet ovat lähellä 1.0:

<img src="../../../translated_images/fi/cosine-similarity.9baeaf3fc3336abb.webp" alt="Kosinisamanlaisuus" width="800"/>

*Tämä kuva havainnollistaa kosinisamanlaisuutta embeddausvektorien välisten kulman suuruutena — mitä lähempänä kaksi vektoria ovat toisiaan (pisteet lähellä 1.0), sitä korkeampi on niiden semanttinen samankaltaisuus.*
> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatin kanssa:** Avaa [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) ja kysy:
> - "Miten samankaltaisuushaku toimii upotusten kanssa ja mikä määrää pistemäärän?"
> - "Mikä samankaltaisuuskynnys pitäisi käyttää ja miten se vaikuttaa tuloksiin?"
> - "Miten käsitellä tilanteet, joissa ei löydy relevantteja dokumentteja?"

### Vastauksen generointi

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Merkityksellisimmät palat kootaan rakenteelliseksi kehotteeksi, joka sisältää selkeät ohjeet, haetun kontekstin ja käyttäjän kysymyksen. Malli lukee nämä tietyt palat ja vastaa niiden perusteella — se voi käyttää vain sitä, mikä sillä on edessään, mikä estää väärien tietojen keksimisen.

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

Alla oleva kaavio näyttää tämän koostamisen toiminnassa — haun vaiheessa parhaiten pisteytyneet palat upotetaan kehotemalliin, ja `OpenAiOfficialChatModel` generoi perustellun vastauksen:

<img src="../../../translated_images/fi/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Tämä kaavio näyttää, miten parhaiten pisteytyneet palat kootaan rakenteelliseksi kehotteeksi, jolloin malli voi generoida perustellun vastauksen datastasi.*

## Suorita sovellus

**Varmista käyttöönotto:**

Varmista, että `.env`-tiedosto on olemassa juurihakemistossa Azure-tunnuksilla (luotu Moduulin 01 aikana):

**Bash:**
```bash
cat ../.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Käynnistä sovellus:**

> **Huom:** Jos olet jo käynnistänyt kaikki sovellukset käyttämällä `./start-all.sh` Moduulista 01, tämä moduuli on jo käynnissä portissa 8081. Voit ohittaa alla olevat käynnistyskomennot ja mennä suoraan osoitteeseen http://localhost:8081.

**Vaihtoehto 1: Käyttämällä Spring Boot Dashboard -laajennusta (Suositeltu VS Code -käyttäjille)**

Kehityssäiliö sisältää Spring Boot Dashboard -laajennuksen, joka tarjoaa visuaalisen käyttöliittymän kaikkien Spring Boot -sovellusten hallintaan. Löydät sen Activity Barista vasemmalta puolelta VS Codea (etsi Spring Boot -ikonia).

Spring Boot Dashboardista voit:
- Näyttää kaikki työtilan saatavilla olevat Spring Boot -sovellukset
- Käynnistää/pysäyttää sovelluksia yhdellä napsautuksella
- Tarkastella sovelluslokeja reaaliajassa
- Valvoa sovelluksen tilaa

Napsauta "rag"-kohdan vieressä olevaa toistopainiketta käynnistääksesi tämän moduulin tai käynnistä kaikki moduulit kerralla.

<img src="../../../translated_images/fi/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Tässä kuvakaappauksessa näkyy Spring Boot Dashboard VS Codessa, jossa voit käynnistää, pysäyttää ja valvoa sovelluksia visuaalisesti.*

**Vaihtoehto 2: Käyttämällä komentojonoja**

Käynnistä kaikki web-sovellukset (moduulit 01-04):

**Bash:**
```bash
cd ..  # Juurikansiosta
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

Molemmat skriptit lataavat automaattisesti ympäristömuuttujat juurihakemiston `.env`-tiedostosta ja rakentavat JAR-tiedostot, jos niitä ei vielä ole.

> **Huom:** Jos haluat mieluummin rakentaa kaikki moduulit manuaalisesti ennen käynnistystä:
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

Avaa selaimessasi http://localhost:8081.

**Pysäytä sovellus:**

**Bash:**
```bash
./stop.sh  # Vain tämä moduuli
# Tai
cd .. && ./stop-all.sh  # Kaikki moduulit
```

**PowerShell:**
```powershell
.\stop.ps1  # Tämä moduuli vain
# Tai
cd ..; .\stop-all.ps1  # Kaikki moduulit
```

## Sovelluksen käyttö

Sovellus tarjoaa verkkokäyttöliittymän dokumenttien lataukseen ja kysymysten esittämiseen.

<a href="images/rag-homepage.png"><img src="../../../translated_images/fi/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Tämä kuvakaappaus näyttää RAG-sovelluksen käyttöliittymän, jossa lataat dokumentteja ja esität kysymyksiä.*

### Lataa dokumentti

Aloita lataamalla dokumentti – TXT-tiedostot toimivat parhaiten testauksessa. Tältä hakemistosta löytyy `sample-document.txt`, joka sisältää tietoa LangChain4j:n ominaisuuksista, RAG-toteutuksesta ja parhaista käytännöistä – täydellinen testaukseen.

Järjestelmä käsittelee dokumenttisi, pilkkoo sen paloiksi ja luo upotukset jokaiselle palalle. Tämä tapahtuu automaattisesti latauksen yhteydessä.

### Esitä kysymyksiä

Esitä nyt tarkkoja kysymyksiä dokumentin sisällöstä. Kokeile jotain faktaan perustuvaa, joka on selkeästi dokumentissa mainittu. Järjestelmä etsii relevantteja paloja, lisää ne kehotteeseen ja generoi vastauksen.

### Tarkista lähdeviitteet

Huomaa, että jokainen vastaus sisältää lähdeviitteitä samankaltaisuuspisteineen. Nämä pisteet (0–1) näyttävät, kuinka relevantti kukin pala kysymyksellesi oli. Korkeammat pisteet tarkoittavat parempia osumia. Tämä mahdollistaa vastauksen varmentamisen lähdeaineiston perusteella.

<a href="images/rag-query-results.png"><img src="../../../translated_images/fi/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Tässä kuvakaappauksessa näkyvät kyselytulokset generoituine vastauksineen, lähdeviitteineen ja merkityspisteineen jokaiselle haetulle palalle.*

### Kokeile erilaisia kysymyksiä

Kokeile erilaisia kysymystyyppejä:
- Tarkat faktat: "Mikä on pääaihe?"
- Vertailut: "Mikä on ero X:n ja Y:n välillä?"
- Yhteenvetoja: "Tiivistä keskeiset kohdat Z:stä"

Seuraa, miten merkityspisteet muuttuvat sen mukaan, kuinka hyvin kysymyksesi vastaa dokumentin sisältöä.

## Keskeiset käsitteet

### Paloitusstrategia

Dokumentit jaetaan 300 tokenin paloihin, joissa on 30 tokenin päällekkäisyys. Tämä tasapaino takaa, että jokaisella palalla on riittävästi kontekstia ollakseen merkityksellinen, mutta palat pysyvät tarpeeksi pieniä, jotta kehotteeseen mahtuu useita paloja.

### Samankaltaisuuspisteet

Jokaisella haetulla palalla on samankaltaisuuspiste väliltä 0–1, joka kertoo, kuinka läheisesti se vastaa käyttäjän kysymystä. Alla oleva kaavio visualisoi pistealueet ja kuinka järjestelmä käyttää niitä suodattaakseen tuloksia:

<img src="../../../translated_images/fi/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Tämä kaavio näyttää pistealueet 0–1 välillä, jossa vähimmäiskynnys 0,5 suodattaa pois epäolennaiset palat.*

Pisteet vaihtelevat välillä 0–1:
- 0,7–1,0: Erittäin relevantti, täsmälleen vastaava
- 0,5–0,7: Relevantti, hyvä konteksti
- Alle 0,5: Suodatettu pois, liian erilainen

Järjestelmä hakee vain ne palat, jotka ovat vähimmäiskynnyksen yläpuolella laadun varmistamiseksi.

Upotukset toimivat hyvin, kun merkitykset ryhmittyvät selkeästi, mutta niissä on myös heikkouksia. Alla oleva kaavio näyttää yleiset epäonnistumistilanteet — liian suuret palat tuottavat epäselviä vektoreita, liian pienet palat eivät tarjoa kontekstia, monitulkintaiset termit viittaavat useaan ryhmään, ja täsmähakuja (esim. tunnisteet, osanumero) ei voi tehdä upotusten avulla lainkaan:

<img src="../../../translated_images/fi/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Tämä kaavio näyttää yleiset upotusten epäonnistumistilanteet: liian suuret palat, liian pienet palat, monitulkintaiset termit, ja täsmähaut kuten tunnisteet.*

### Muistinvarainen tallennus

Tässä moduulissa käytetään yksinkertaisuuden vuoksi muistinvaraista tallennusta. Kun käynnistät sovelluksen uudelleen, ladatut dokumentit häviävät. Tuotantojärjestelmissä käytetään pysyviä vektoritietokantoja, kuten Qdrant tai Azure AI Search.

### Konteksti-ikkunan hallinta

Jokaisella mallilla on maksimikoko konteksti-ikkunalle. Et voi sisällyttää kaikkia suurten dokumenttien paloja. Järjestelmä hakee viisi (oletus) merkityksellisintä palaa pysyäkseen rajoissa ja tarjotakseen riittävän kontekstin tarkkoihin vastauksiin.

## Milloin RAG on tärkeä

RAG ei ole aina oikea lähestymistapa. Alla oleva päätösopas auttaa sinua päättämään, milloin RAG lisää arvoa verrattuna yksinkertaisempiin tapoihin — kuten sisällön suoraan lisääminen kehotteeseen tai mallin sisäänrakennetun tiedon hyödyntäminen:

<img src="../../../translated_images/fi/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Tämä kaavio näyttää päätösoppaan siitä, milloin RAG tuottaa lisäarvoa verrattuna yksinkertaisempiin ratkaisuihin.*

**Käytä RAGia, kun:**
- Vastaat kysymyksiin omista dokumenteista
- Tiedot muuttuvat usein (käytännöt, hinnat, tekniset tiedot)
- Tarkkuus vaatii lähdeviitteiden antamista
- Sisältö on liian laaja mahtuakseen yhteen kehotteeseen
- Tarvitset todennettavia ja perusteltuja vastauksia

**Älä käytä RAGia, kun:**
- Kysymykset vaativat yleistä tietoa, joka mallilla jo on
- Tarvitaan reaaliaikaista tietoa (RAG toimii ladatuilla dokumenteilla)
- Sisältö on niin pieni, että sen voi sisällyttää suoraan kehotteeseen

## Seuraavat askeleet

**Seuraava moduuli:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Navigointi:** [← Edellinen: Moduuli 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Takaisin pääsivulle](../README.md) | [Seuraava: Moduuli 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty käyttäen tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, huomioithan, että automaattikäännöksissä saattaa esiintyä virheitä tai epätarkkuuksia. Alkuperäinen asiakirja sen alkuperäiskielellä tulee pitää ensisijaisena lähteenä. Tärkeissä asioissa suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä aiheutuvista väärinymmärryksistä tai tulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->