# Module 00: Nopeasti liikkeelle

## Sisällysluettelo

- [Johdanto](../../../00-quick-start)
- [Mikä on LangChain4j?](../../../00-quick-start)
- [LangChain4j-riippuvuudet](../../../00-quick-start)
- [Ennen aloittamista](../../../00-quick-start)
- [Asetukset](../../../00-quick-start)
  - [1. Hanki GitHub-tunnuksesi](../../../00-quick-start)
  - [2. Aseta tunnuksesi](../../../00-quick-start)
- [Suorita esimerkit](../../../00-quick-start)
  - [1. Peruschat](../../../00-quick-start)
  - [2. Kehote-kuviot](../../../00-quick-start)
  - [3. Funktiokutsu](../../../00-quick-start)
  - [4. Asiakirjakyselyt (RAG)](../../../00-quick-start)
  - [5. Vastuullinen tekoäly](../../../00-quick-start)
- [Mitä kukin esimerkki näyttää](../../../00-quick-start)
- [Seuraavat vaiheet](../../../00-quick-start)
- [Vianetsintä](../../../00-quick-start)

## Johdanto

Tämä pika-aloitus on tarkoitettu saamaan sinut nopeasti käynnistymään LangChain4j:n kanssa. Se kattaa tekoälysovellusten rakentamisen perusteet LangChain4j:llä ja GitHub-malleilla. Seuraavissa moduuleissa käytät Azure OpenAI:ta LangChain4j:n kanssa rakentamaan kehittyneempiä sovelluksia.

## Mikä on LangChain4j?

LangChain4j on Java-kirjasto, joka yksinkertaistaa tekoälyä hyödyntävien sovellusten rakentamista. HTTP-asiakkaiden ja JSON-tulkkauksen sijaan työskentelet selkeiden Java-API:en kanssa.

LangChainin "ketju" viittaa useiden komponenttien ketjuttamiseen – saatat ketjuttaa kehotteen malliin ja sitten tulkin läpi, tai ketjuttaa useita tekoälykutsuja, joissa yhden tulos toimii seuraavan syötteenä. Tämä pika-aloitus keskittyy perusteisiin ennen monimutkaisempien ketjujen tutkimista.

<img src="../../../translated_images/fi/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Komponenttien ketjuttaminen LangChain4j:ssä - rakennuspalikat kytkeytyvät yhteen luodakseen tehokkaita tekoälytyönkulkuja*

Käytämme kolmea ydinkomponenttia:

**ChatLanguageModel** – Rajapinta tekoälymallin kanssa kommunikointiin. Kutsu `model.chat("prompt")` ja saat vastausmerkkijonon. Käytämme `OpenAiOfficialChatModel`:ia, joka toimii OpenAI-yhteensopivien päätepisteiden kanssa, kuten GitHub Models.

**AiServices** – Luo tyypin turvallisia tekoälypalvelujen rajapintoja. Määrittele metodit, merkitse ne `@Tool` -annotaatiolla, ja LangChain4j huolehtii orkestroinnista. Tekoäly kutsuu Java-menetelmiäsi automaattisesti tarpeen mukaan.

**MessageWindowChatMemory** – Säilyttää keskustelun historian. Ilman tätä jokainen pyyntö on itsenäinen. Tämän kanssa tekoäly muistaa aiemmat viestit ja ylläpitää kontekstia useiden vuorojen yli.

<img src="../../../translated_images/fi/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j-arkkitehtuuri – ydin komponentit toimivat yhdessä voimaannuttaakseen tekoälysovelluksiasi*

## LangChain4j-riippuvuudet

Tämä pika-aloitus käyttää kahta Maven-riippuvuutta tiedostossa [`pom.xml`](../../../00-quick-start/pom.xml):

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
```

`langchain4j-open-ai-official` -moduuli tarjoaa `OpenAiOfficialChatModel`-luokan, joka yhdistää OpenAI-yhteensopiviin API:hin. GitHub Models käyttää samaa API-muotoa, joten erillistä sovitinta ei tarvita – osoita vain perus-URL `https://models.github.ai/inference` -osoitteeseen.

## Ennen aloittamista

**Käytätkö Dev Containeria?** Java ja Maven ovat jo asennettuja. Tarvitset vain GitHubin henkilökohtaisen käyttöoikeustunnuksen.

**Paikallinen kehitys:**
- Java 21+, Maven 3.9+
- GitHubin henkilökohtainen käyttöoikeustunnus (ohjeet alla)

> **Huom:** Tämä moduuli käyttää GitHub Models:n `gpt-4.1-nano` -mallia. Älä muuta mallin nimeä koodissa – se on konfiguroitu toimimaan GitHubin saatavilla olevien mallien kanssa.

## Asetukset

### 1. Hanki GitHub-tunnuksesi

1. Mene [GitHub Asetukset → Henkilökohtaiset käyttöoikeustunnukset](https://github.com/settings/personal-access-tokens)
2. Klikkaa "Luo uusi tunnus"
3. Anna kuvaava nimi (esim. "LangChain4j Demo")
4. Aseta vanhenemispäivä (7 päivää suositeltu)
5. "Tilin oikeudet" -kohdasta etsi "Models" ja valitse "Vain luku"
6. Klikkaa "Luo tunnus"
7. Kopioi ja tallenna tunnuksesi – et näe sitä enää

### 2. Aseta tunnuksesi

**Vaihtoehto 1: VS Code (Suositeltu)**

Jos käytät VS Codea, lisää tunnuksesi projektin juureen `.env`-tiedostoon:

Jos `.env`-tiedostoa ei ole, kopioi `.env.example` tiedostoksi `.env` tai luo uusi `.env` tiedosto projektin juureen.

**Esimerkki `.env`-tiedostosta:**
```bash
# Tiedostossa /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Sitten voit yksinkertaisesti klikata hiiren oikealla mitä tahansa demotiedostoa (esim. `BasicChatDemo.java`) Explorerissa ja valita **"Run Java"** tai käyttää käynnistyskonfiguraatioita Run and Debug -paneelista.

**Vaihtoehto 2: Komentorivi**

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

**VS Code:** Klikkaa hiiren oikealla mitä tahansa demo tiedostoa Explorerissa ja valitse **"Run Java"**, tai käytä Run and Debug -paneelin käynnistysvalintoja (muista lisätä tunnuksesi ensin `.env`-tiedostoon).

**Maven:** Vaihtoehtoisesti voit ajaa komennon riviltä:

### 1. Peruschat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Kehote-kuviot

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Näyttää zero-shot, few-shot, chain-of-thought ja roolipohjaiset kehotteet.

### 3. Funktiokutsu

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

Tekoäly kutsuu automaattisesti Java-metodejasi tarvittaessa.

### 4. Asiakirjakyselyt (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Kysy kysymyksiä sisällöstä tiedostossa `document.txt`.

### 5. Vastuullinen tekoäly

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Näet miten tekoälyn suojaussuodattimet estävät haitallista sisältöä.

## Mitä kukin esimerkki näyttää

**Peruschat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Aloita tästä nähdäksesi LangChain4j:n yksinkertaisimmillaan. Luot `OpenAiOfficialChatModel`-olion, lähetät kehotteen `.chat()`-metodilla ja saat vastauksen. Tämä osoittaa perustan: miten alustetaan malleja mukautetuilla päätepisteillä ja API-avaimilla. Kun ymmärrät tämän kaavan, kaikki muu rakentuu sen päälle.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chat:lla:** Avaa [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ja kysy:
> - "Miten siirtyisin GitHub Modelsista Azure OpenAI:hin tässä koodissa?"
> - "Mitkä muut parametrit ovat konfiguroitavissa OpenAiOfficialChatModel.builder():ssä?"
> - "Kuinka lisään suoratoistovastaukset sen sijaan, että odotan täydellistä vastausta?"

**Kehotteiden suunnittelu** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Nyt kun tiedät, miten puhut mallille, tutustutaan mitä sille sanot. Tämä demo käyttää samaa mallin asetusta, mutta näyttää viisi erilaista kehote-kuviota. Kokeile zero-shot-kehotteita suoria ohjeita varten, few-shot-kehotteita, joissa opitaan esimerkkien avulla, chain-of-thoughtia, joka paljastaa päättelyvaiheet, ja roolipohjaisia kehotteita, jotka luovat kontekstin. Näet, miten sama malli antaa dramaattisesti erilaisia vastauksia kehotteen muotoilun perusteella.

Demo näyttää myös kehote-pohjat, jotka ovat tehokas tapa luoda uudelleenkäytettäviä kehotteita muuttujilla.
Alla oleva esimerkki osoittaa kehotteen, joka käyttää LangChain4j:n `PromptTemplate`-luokkaa täyttämään muuttujat. Tekoäly vastaa annetun kohteen ja toiminnan perusteella.

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

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chat:lla:** Avaa [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ja kysy:
> - "Mikä on ero zero-shotin ja few-shotin välillä, ja milloin kumpaakin tulisi käyttää?"
> - "Miten lämpötila-parametri vaikuttaa mallin vastauksiin?"
> - "Mitkä ovat keinoja estää kehotepohjaisten hyökkäysten riskit tuotannossa?"
> - "Kuinka luoda uudelleenkäytettäviä PromptTemplate-olioita yleisiin kuvioihin?"

**Työkalujen integrointi** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Tässä LangChain4j tulee voimakkaaksi. Käytät `AiServices`-luokkaa luodaksesi tekoälyavustajan, joka voi kutsua Java-metodejasi. Merkitse vain metodit `@Tool("kuvaus")` -annotaatiolla ja LangChain4j huolehtii muusta – tekoäly päättää automaattisesti, milloin käyttää kutakin työkalua käyttäjän pyynnöstä. Tämä näyttää funktiokutsut, keskeisen tekniikan tekoälyn rakentamisessa, joka voi suorittaa toimintoja eikä vain vastaa kysymyksiin.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chat:lla:** Avaa [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ja kysy:
> - "Miten @Tool-annotaatio toimii ja mitä LangChain4j tekee sen kanssa taustalla?"
> - "Voiko tekoäly kutsua useita työkaluja peräkkäin ratkaistakseen monimutkaisia ongelmia?"
> - "Mitä tapahtuu, jos työkalu heittää poikkeuksen – miten virheitä tulisi käsitellä?"
> - "Miten yhdistän oikean API:n tämän laskin-esimerkin sijaan?"

**Asiakirjakyselyt (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Tässä näet RAG:n (retrieval-augmented generation) perustan. Mallin koulutusdataan luottamisen sijaan lataat sisältöä tiedostosta [`document.txt`](../../../00-quick-start/document.txt) ja liität sen kehotteeseen. Tekoäly vastaa dokumenttisi perusteella, ei yleisen tietämyksensä. Tämä on ensimmäinen askel kohti järjestelmiä, jotka toimivat omien tietojesi kanssa.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Huom:** Tämä yksinkertainen lähestymistapa lataa koko dokumentin kehotteeseen. Suurissa tiedostoissa (>10KB) ylität kontekstin rajoitukset. Moduuli 03 käsittelee pilkkomista ja vektorihaun tuotantokäyttöön.

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chat:lla:** Avaa [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ja kysy:
> - "Miten RAG estää tekoälyn harhailemisen verrattuna mallin koulutusdataan luottamiseen?"
> - "Mikä on ero tämän yksinkertaisen lähestymistavan ja vektoriesitysten haun välillä?"
> - "Kuinka laajennan tätä käsittelemään useita asiakirjoja tai suurempia tietokantoja?"
> - "Mitkä ovat parhaat käytännöt kehotteen rakenteessa, jotta tekoäly käyttäisi vain annettua kontekstia?"

**Vastuullinen tekoäly** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Rakenna tekoälyn turvallisuus kerroksittain. Tämä demo näyttää kaksi suojakerrosta, jotka toimivat yhdessä:

**Osa 1: LangChain4j Syötteen suojaukset** – Estää vaaralliset kehotteet ennen LLM:lle pääsyä. Luo mukautettuja turvarajoja, jotka tarkistavat kielletyt avainsanat tai mallit. Nämä toimivat koodissasi, joten ne ovat nopeita ja ilmaisia.

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

**Osa 2: Palveluntarjoajan turvasuodattimet** – GitHub Modelsissa on sisäänrakennettuja suodattimia, jotka kiinniottavat mitä suojasi saattavat missata. Näet kovia estoja (HTTP 400 -virheet) vakavissa rikkomuksissa ja pehmeitä kieltäytymisiä, joissa tekoäly kohteliaasti kieltäytyy.

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chat:lla:** Avaa [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ja kysy:
> - "Mikä on InputGuardrail ja kuinka luon oman?"
> - "Mikä ero on kovalla estolla ja pehmeällä kieltäytymisellä?"
> - "Miksi käyttää sekä guardraileja että palveluntarjoajan suodattimia yhdessä?"

## Seuraavat vaiheet

**Seuraava moduuli:** [01-introduction - Käyttö LangChain4j:n ja gpt-5:n kanssa Azurella](../01-introduction/README.md)

---

**Navigointi:** [← Takaisin pääsivulle](../README.md) | [Seuraava: Moduuli 01 - Johdanto →](../01-introduction/README.md)

---

## Vianetsintä

### Ensimmäinen Maven-käännös

**Ongelma:** Ensimmäinen `mvn clean compile` tai `mvn package` kestää kauan (10-15 minuuttia)

**Syy:** Maven lataa kaikki projektin riippuvuudet (Spring Boot, LangChain4j-kirjastot, Azure SDK:t jne.) ensimmäisellä käännöksellä.

**Ratkaisu:** Tämä on normaali käytös. Seuraavat käännökset ovat paljon nopeampia, koska riippuvuudet ovat välimuistissa paikallisesti. Latausaika riippuu verkkoyhteytesi nopeudesta.
### PowerShell Maven -komentojensyntaksi

**Ongelma**: Maven-komennot epäonnistuvat virheellä `Unknown lifecycle phase ".mainClass=..."`

**Syy**: PowerShell tulkitsee `=`-merkin muuttujan asetusoperaattorina, mikä rikkoo Maven-ominaisuuksien syntaksia

**Ratkaisu**: Käytä pysäytyksen jäsentämisoperaattoria `--%` ennen Maven-komentoa:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%`-operaattori kertoo PowerShellille, että se välittää kaikki jäljellä olevat argumentit kirjaimellisesti Mavenille ilman tulkintaa.

### Windows PowerShell Emojit näyttö

**Ongelma**: AI-vastaukset näyttävät roskakirjaimia (esim. `????` tai `â??`) PowerShellissä emojien sijasta

**Syy**: PowerShellin oletuskoodaus ei tue UTF-8 emojeita

**Ratkaisu**: Suorita tämä komento ennen Java-sovellusten ajamista:
```cmd
chcp 65001
```

Tämä pakottaa UTF-8-koodauksen pääteikkunaan. Vaihtoehtoisesti käytä Windows Terminalia, joka tukee paremmin Unicodea.

### API-kutsujen vianmääritys

**Ongelma**: Todennusvirheet, rajapinnan käyttörajoitukset tai odottamattomat vastaukset AI-mallilta

**Ratkaisu**: Esimerkeissä käytetään `.logRequests(true)` ja `.logResponses(true)` näyttääksesi API-kutsut konsolissa. Tämä auttaa selvittämään todennusvirheitä, rajapinnan käyttörajoituksia tai odottamattomia vastauksia. Poista nämä liput tuotannossa vähentääksesi lokien määrää.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, otathan huomioon, että automaattikäännöksissä saattaa esiintyä virheitä tai epätarkkuuksia. Alkuperäinen asiakirja omalla kielellään on virallinen lähde. Tärkeissä asioissa suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä aiheutuvista väärinymmärryksistä tai -tulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->