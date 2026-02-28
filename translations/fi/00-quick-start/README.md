# Module 00: Pikakäynnistys

## Sisällysluettelo

- [Johdanto](../../../00-quick-start)
- [Mikä on LangChain4j?](../../../00-quick-start)
- [LangChain4j Riippuvuudet](../../../00-quick-start)
- [Edellytykset](../../../00-quick-start)
- [Asetus](../../../00-quick-start)
  - [1. Hanki GitHub-tunnuksesi](../../../00-quick-start)
  - [2. Aseta tunnuksesi](../../../00-quick-start)
- [Suorita esimerkit](../../../00-quick-start)
  - [1. Peruschat](../../../00-quick-start)
  - [2. Kehotekuviot](../../../00-quick-start)
  - [3. Funktiokutsut](../../../00-quick-start)
  - [4. Dokumenttikysymykset (Easy RAG)](../../../00-quick-start)
  - [5. Vastuullinen tekoäly](../../../00-quick-start)
- [Mitä kukin esimerkki näyttää](../../../00-quick-start)
- [Seuraavat askeleet](../../../00-quick-start)
- [Vianmääritys](../../../00-quick-start)

## Johdanto

Tämä pikakäynnistys on tarkoitettu saamaan sinut nopeasti alkuun LangChain4j:n kanssa. Se kattaa tekoälysovellusten rakentamisen kaikkein perusasiat LangChain4j:llä ja GitHub-malleilla. Seuraavissa moduuleissa käytät Azure OpenAI:ta LangChain4j:n kanssa rakentaaksesi edistyneempiä sovelluksia.

## Mikä on LangChain4j?

LangChain4j on Java-kirjasto, joka yksinkertaistaa tekoälyllä käytettävien sovellusten rakentamista. Sen sijaan, että käsittelisit HTTP-asiakkaita ja JSONin jäsentämistä, työskentelet puhtaiden Java-rajapintojen kanssa. 

LangChainin "ketju" viittaa useiden komponenttien ketjuttamiseen - voit yhdistää kehotemallin parseriin tai ketjuttaa useita tekoälykutsuja, joissa yhden tulos syötetään seuraavaan syötteeseen. Tämä pikakäynnistys keskittyy perusteisiin ennen monimutkaisempien ketjujen tutkimista.

<img src="../../../translated_images/fi/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Ketjutuskonsepti" width="800"/>

*Komponenttien ketjuttaminen LangChain4j:ssä – rakennuspalikat yhdistyvät luomaan tehokkaita tekoälytyönkulkuja*

Käytämme kolmea ydinkomponenttia:

**ChatModel** - Rajapinta tekoälymallien vuorovaikutukseen. Kutsu `model.chat("kehotus")` ja saat vastausmerkkijonon. Käytämme `OpenAiOfficialChatModel`-luokkaa, joka toimii OpenAI-yhteensopivien rajapintojen, kuten GitHub-mallien, kanssa.

**AiServices** - Luo tyyppiturvallisia tekoälypalvelujen rajapintoja. Määrittele metodit, merkitse ne `@Tool`-annotaatiolla, ja LangChain4j huolehtii orkestroinnista. Tekoäly kutsuu automaattisesti Java-metodejasi tarpeen mukaan.

**MessageWindowChatMemory** - Ylläpitää keskusteluhistoriaa. Ilman tätä jokainen pyyntö on erillinen. Tämän avulla tekoäly muistaa aiemmat viestit ja ylläpitää kontekstia useamman vuorovaikutuksen ajan.

<img src="../../../translated_images/fi/architecture.eedc993a1c576839.webp" alt="LangChain4j arkkitehtuuri" width="800"/>

*LangChain4j arkkitehtuuri – ydinkomponentit työskentelevät yhdessä voimaannuttamaan tekoälysovelluksiasi*

## LangChain4j Riippuvuudet

Tämä pikakäynnistys käyttää kolmea Maven-riippuvuutta tiedostossa [`pom.xml`](../../../00-quick-start/pom.xml):

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

`langchain4j-open-ai-official`-moduuli tarjoaa `OpenAiOfficialChatModel`-luokan, joka yhdistää OpenAI-yhteensopiviin API:hin. GitHub-mallit käyttävät samaa API-muotoa, joten erillistä sovitinta ei tarvita - vain määritä perus-URL osoittamaan `https://models.github.ai/inference`.

`langchain4j-easy-rag`-moduuli tarjoaa automaattisen dokumenttien pilkkomisen, upotuksen ja haun, jotta voit rakentaa RAG-sovelluksia ilman manuaalista jokaisen vaiheen konfigurointia.

## Edellytykset

**Käytätkö Dev Containeria?** Java ja Maven ovat jo asennettuina. Tarvitset vain GitHubin henkilökohtaisen käyttötunnuksen.

**Paikallinen kehitys:**
- Java 21+, Maven 3.9+
- GitHubin henkilökohtainen käyttötunnus (ohjeet alla)

> **Huom:** Tämä moduuli käyttää GitHub-mallien `gpt-4.1-nano`-mallia. Älä muuta mallin nimeä koodissa – se on määritetty toimimaan GitHubin käytettävissä olevien mallien kanssa.

## Asetus

### 1. Hanki GitHub-tunnuksesi

1. Mene kohtaan [GitHub-asetukset → Henkilökohtaiset käyttötunnukset](https://github.com/settings/personal-access-tokens)
2. Klikkaa "Luo uusi tunnus"
3. Anna kuvaava nimi (esim. "LangChain4j Demo")
4. Aseta vanhenemisaika (7 päivää suositeltu)
5. Kohdassa "Tilin oikeudet" etsi "Models" ja aseta se "Vain luku"
6. Klikkaa "Luo tunnus"
7. Kopioi ja tallenna tunnuksesi – et näe sitä enää uudelleen

### 2. Aseta tunnuksesi

**Vaihtoehto 1: Käytä VS Codea (suositeltu)**

Jos käytät VS Codea, lisää tunnuksesi `.env`-tiedostoon projektin juureen:

Jos `.env`-tiedostoa ei ole, kopioi `.env.example` tiedostoksi `.env` tai luo uusi `.env` tiedosto projektin juureen.

**Esimerkki `.env`-tiedostosta:**
```bash
# Kansiossa /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Sitten voit yksinkertaisesti klikata hiiren oikealla mitä tahansa demotiedostoa (esim. `BasicChatDemo.java`) Explorerissa ja valita **"Run Java"** tai käyttää suorituksen asetuksia Run and Debug -paneelista.

**Vaihtoehto 2: Käytä päätettä**

Aseta tunnus ympäristömuuttujaksi:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Suorita esimerkit

**VS Codea käyttäen:** Klikkaa hiiren oikealla mitä tahansa demotiedostoa Explorerissa ja valitse **"Run Java"**, tai käytä suorituksen asetuksia Run and Debug -paneelista (varmista, että olet lisännyt tunnuksesi `.env`-tiedostoon ensin).

**Mavenia käyttäen:** Vaihtoehtoisesti voit suorittaa komentoriviltä:

### 1. Peruschat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Kehotekuviot

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Näyttää zero-shot, few-shot, chain-of-thought, ja roolipohjaiset kehotteet.

### 3. Funktiokutsut

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

Tekoäly kutsuu automaattisesti Java-metodejasi tarpeen mukaan.

### 4. Dokumenttikysymykset (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Esitä kysymyksiä dokumenteistasi Easy RAG:n avulla automaattisen upotuksen ja haun kanssa.

### 5. Vastuullinen tekoäly

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Näet kuinka tekoälyn turvallisuussuodattimet estävät haitallista sisältöä.

## Mitä kukin esimerkki näyttää

**Peruschat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Aloita tästä nähdäksesi LangChain4j:n yksinkertaisimmillaan. Luot `OpenAiOfficialChatModel`-olion, lähetät kehoteen `.chat()`-metodilla ja saat vastauksen. Tämä näyttää perustan: miten mallit alustetaan mukautetuilla rajapinnoilla ja API-avaimilla. Kun ymmärrät tämän mallin, kaikki muu rakentuu sen päälle.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Kokeile [GitHub Copilotin](https://github.com/features/copilot) Chatin kanssa:** Avaa [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ja kysy:
> - "Miten vaihtaisin GitHub-malleista Azure OpenAI:hin tässä koodissa?"
> - "Mitä muita parametreja voin konfiguroida OpenAiOfficialChatModel.builder()-metodissa?"
> - "Miten lisään striimaavat vastaukset odottamisen sijaan?"

**Kehotetekniikat** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Nyt kun tiedät, miten puhut mallille, tutustutaan siihen, mitä sanot sille. Tämä demo käyttää samaa mallin asetusta mutta näyttää viisi erilaista kehotemallia. Kokeile zero-shot-kehotteita suorina ohjeina, few-shot-kehotteita esimerkkien perusteella, ketjutettua ajattelua paljastavia kehotteita ja roolipohjaisia kehotteita kontekstin asettamiseksi. Näet, miten sama malli antaa dramaattisesti erilaisia tuloksia pyynnön muotoilusta riippuen.

Demo näyttää myös kehotepohjat, jotka ovat tehokas tapa luoda uudelleenkäytettäviä kehotteita muuttujilla.
Alla oleva esimerkki näyttää kehotteen käyttäen LangChain4j:n `PromptTemplate`-luokkaa muuttujien täyttämiseen. Tekoäly vastaa annetun määränpään ja toiminnan perusteella.

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

> **🤖 Kokeile [GitHub Copilotin](https://github.com/features/copilot) Chatin kanssa:** Avaa [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ja kysy:
> - "Mikä on ero zero-shotin ja few-shotin välillä ja milloin kumpaakin pitäisi käyttää?"
> - "Miten lämpötila-parametri vaikuttaa mallin vastauksiin?"
> - "Mitkä ovat tekniikoita estää kehotteiden injektiohyökkäyksiä tuotannossa?"
> - "Miten luon uudelleenkäytettäviä PromptTemplate-olioita yleisille malleille?"

**Työkalujen integrointi** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Tässä LangChain4j todellakin vahvistuu. Käytät `AiServices`-luokkaa luodaksesi tekoälyavustajan, joka voi kutsua Java-metodejasi. Merkitse metodit vain `@Tool("kuvaus")` -annotaatiolla ja LangChain4j hoitaa loput – tekoäly päättää automaattisesti, mitä työkaluja käyttää käyttäjän kysymyksen perusteella. Tämä demonstroi funktiokutsuja, avaintekniikkaa rakentaa tekoälyä, joka voi toimia eikä pelkästään vastata kysymyksiin.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Kokeile [GitHub Copilotin](https://github.com/features/copilot) Chatin kanssa:** Avaa [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ja kysy:
> - "Miten @Tool-annotaatio toimii ja mitä LangChain4j tekee sen kanssa taustalla?"
> - "Voiko tekoäly kutsua useita työkaluja peräkkäin ratkaistakseen monimutkaisia ongelmia?"
> - "Mitä tapahtuu jos työkalu heittää poikkeuksen – miten virheet pitäisi käsitellä?"
> - "Miten integroin oikean API:n tämän laskin-esimerkin sijaan?"

**Dokumenttikysymykset (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Tässä näet RAG:n (retrieval-augmented generation) LangChain4j:n "Easy RAG" -lähestymistavalla. Dokumentit ladataan, pilkotaan ja upotetaan automaattisesti muistinvaraiselle tallennusalueelle, josta sisällönhakija toimittaa sopivia osuuksia tekoälylle kyselyhetkellä. Tekoäly vastaa dokumenttiesi perusteella, ei yleisen tietämyksensä pohjalta.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 Kokeile [GitHub Copilotin](https://github.com/features/copilot) Chatin kanssa:** Avaa [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ja kysy:
> - "Miten RAG estää tekoälyharhoja verrattuna mallin koulutusdataan perustuvaan käyttöön?"
> - "Mikä ero on tämän helpon lähestymistavan ja räätälöidyn RAG-putken välillä?"
> - "Miten skaalaan tämän käsittelemään useita dokumentteja tai suurempia tietopohjia?"

**Vastuullinen tekoäly** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Rakenna tekoälyturvallisuutta syvyyssuodattimilla. Tämä demo näyttää kaksi suojaustasoa työskentelemässä yhdessä:

**Osa 1: LangChain4j Syötteen suojakaiteet** – Estä vaaralliset kehoteet ennen kuin ne saavuttavat LLM:n. Luo omia suojakaiteita, jotka tarkistavat kielletyt avainsanat tai mallit. Nämä toimivat koodissasi, joten ne ovat nopeita ja ilmaisia.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**Osa 2: Tarjoajan turvallisuussuodattimet** – GitHub-malleissa on sisäänrakennetut suodattimet, jotka pysäyttävät sen, minkä suojakaiteet saattavat jättää huomaamatta. Näet kovia estojärjestelmiä (HTTP 400 -virheitä) vakavissa rikkomuksissa ja pehmeitä kieltäytymisiä, joissa tekoäly kohteliaasti kieltäytyy.

> **🤖 Kokeile [GitHub Copilotin](https://github.com/features/copilot) Chatin kanssa:** Avaa [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ja kysy:
> - "Mikä on InputGuardrail ja miten luon oman?"
> - "Mikä ero on kovalla estolla ja pehmeällä kieltäytymisellä?"
> - "Miksi käyttää sekä suojakaiteita että tarjoajan suodattimia yhdessä?"

## Seuraavat askeleet

**Seuraava moduuli:** [01-introduction - Aloittaminen LangChain4j:n ja gpt-5:n kanssa Azurella](../01-introduction/README.md)

---

**Navigointi:** [← Takaisin pääsivulle](../README.md) | [Seuraava: Moduuli 01 - Johdanto →](../01-introduction/README.md)

---

## Vianmääritys

### Ensimmäinen Maven-kokoitus

**Ongelma:** Ensimmäinen `mvn clean compile` tai `mvn package` kestää kauan (10-15 minuuttia)

**Syy:** Mavenin täytyy ladata kaikki projektin riippuvuudet (Spring Boot, LangChain4j-kirjastot, Azure SDK:t jne.) ensimmäisellä kerralla.

**Ratkaisu:** Tämä on normaalia käytöstä. Seuraavat kerrat ovat paljon nopeampia, koska riippuvuudet ovat välimuistissa paikallisesti. Latausaika riippuu verkkoyhteyden nopeudesta.

### PowerShellin Maven-komentojen syntaksi

**Ongelma:** Maven-komennot epäonnistuvat virheilmoituksella `Unknown lifecycle phase ".mainClass=..."`
**Syy**: PowerShell tulkitsee `=` muuttujan arvon määrityksen operaattorina, mikä rikkoo Mavenin ominaisuussyntaksin

**Ratkaisu**: Käytä pysäytyksen jäsentämisoperaattoria `--%` ennen Maven-komentoa:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%`-operaattori kertoo PowerShellille, että kaikki jäljellä olevat argumentit välitetään Mavenille kirjaimellisesti ilman tulkintaa.

### Windows PowerShellin emojien näyttö

**Ongelma**: AI-vastaukset näyttävät roskamerkkejä (esim. `????` tai `â??`) emojien sijaan PowerShellissä

**Syy**: PowerShellin oletuskoodaus ei tue UTF-8-emojia

**Ratkaisu**: Suorita tämä komento ennen Java-sovellusten käynnistämistä:
```cmd
chcp 65001
```

Tämä pakottaa UTF-8-koodauksen terminaalissa. Vaihtoehtoisesti käytä Windows Terminalia, jossa on parempi Unicode-tuki.

### API-kutsujen virheenkorjaus

**Ongelma**: Todennusvirheitä, nopeusrajoituksia tai odottamattomia vastauksia AI-mallilta

**Ratkaisu**: Esimerkeissä käytetään `.logRequests(true)` ja `.logResponses(true)`, jotka näyttävät API-kutsut konsolissa. Tämä auttaa selvittämään todennusvirheitä, nopeusrajoituksia tai odottamattomia vastauksia. Poista nämä liput tuotannossa, jotta lokit eivät täyty liiaksi.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty tekoälypohjaisella käännöspalvelulla [Co-op Translator](https://github.com/Azure/co-op-translator). Pyrimme tarkkuuteen, mutta otathan huomioon, että automaattiset käännökset saattavat sisältää virheitä tai epätarkkuuksia. Alkuperäinen asiakirja sen alkuperäiskielellä on virallinen lähde. Tärkeissä tiedoissa suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä johtuvista väärinymmärryksistä tai virhetulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->