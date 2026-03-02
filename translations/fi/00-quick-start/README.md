# Module 00: Quick Start

## Table of Contents

- [Introduction](../../../00-quick-start)
- [What is LangChain4j?](../../../00-quick-start)
- [LangChain4j Dependencies](../../../00-quick-start)
- [Prerequisites](../../../00-quick-start)
- [Setup](../../../00-quick-start)
  - [1. Get Your GitHub Token](../../../00-quick-start)
  - [2. Set Your Token](../../../00-quick-start)
- [Run the Examples](../../../00-quick-start)
  - [1. Basic Chat](../../../00-quick-start)
  - [2. Prompt Patterns](../../../00-quick-start)
  - [3. Function Calling](../../../00-quick-start)
  - [4. Document Q&A (Easy RAG)](../../../00-quick-start)
  - [5. Responsible AI](../../../00-quick-start)
- [What Each Example Shows](../../../00-quick-start)
- [Next Steps](../../../00-quick-start)
- [Troubleshooting](../../../00-quick-start)

## Introduction

Tämä pikaopas on tarkoitettu saamaan sinut nopeasti käyntiin LangChain4j:n kanssa. Se kattaa AI-sovellusten rakentamisen aivan perusteet LangChain4j:n ja GitHub-mallien avulla. Seuraavissa moduuleissa siirryt Azure OpenAI:hin ja GPT-5.2:een ja sukellat syvemmälle jokaiseen käsitteeseen.

## What is LangChain4j?

LangChain4j on Java-kirjasto, joka yksinkertaistaa tekoälyllä varustettujen sovellusten rakentamista. HTTP-asiakkaiden ja JSON-jäsentämisen sijaan työskentelet puhtaiden Java-rajapintojen kanssa.

LangChainin "ketju" tarkoittaa useiden komponenttien ketjuttamista yhteen – voit ketjuttaa esimerkiksi kehotteen malliin ja sieltä jäsentimeen, tai ketjuttaa useita tekoälykutsuja, joissa yksi lähtö syöttää seuraavaa syötettä. Tämä pikaopas keskittyy perusteisiin ennen monimutkaisempien ketjujen tutkimista.

<img src="../../../translated_images/fi/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Komponenttien ketjuttaminen LangChain4j:ssä – rakennuspalikat yhdistyvät luoden tehokkaita tekoälytyönkulkuja*

Käytämme kolmea ydinkomponenttia:

**ChatModel** – Rajapinta tekoälymallien vuorovaikutukseen. Kutsu `model.chat("prompt")` ja saat vastausmerkkijonon. Käytämme `OpenAiOfficialChatModel`-luokkaa, joka toimii OpenAI-yhteensopivien rajapintojen kuten GitHub-mallien kanssa.

**AiServices** – Luo tyyppiturvallisia tekoälypalvelunrajapintoja. Määrittele metodit, merkkaa ne `@Tool`-annotaatiolla, ja LangChain4j hoitaa orkestroinnin. Tekoäly kutsuu automaattisesti Java-menetelmiäsi tarpeen mukaan.

**MessageWindowChatMemory** – Säilyttää keskusteluhistorian. Ilman tätä jokainen pyyntö on itsenäinen. Tämän kanssa tekoäly muistaa edelliset viestit ja ylläpitää kontekstia useiden vuorojen yli.

<img src="../../../translated_images/fi/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j-arkkitehtuuri – ydinkomponentit toimivat yhdessä tehostaen tekoälysovelluksiasi*

## LangChain4j Dependencies

Tämä pikaopas käyttää kolmea Maven-riippuvuutta tiedostossa [`pom.xml`](../../../00-quick-start/pom.xml):

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

`langchain4j-open-ai-official`-moduuli tarjoaa `OpenAiOfficialChatModel`-luokan, joka yhdistää OpenAI-yhteensopiviin rajapintoihin. GitHub-mallit käyttävät samaa rajapintamuotoa, joten erityistä sovitinta ei tarvita – osoita vain perus-URL `https://models.github.ai/inference`.

`langchain4j-easy-rag`-moduuli tarjoaa automaattisen asiakirjojen pilkkomisen, upotuksen ja haun, jotta voit rakentaa RAG-sovelluksia ilman manuaalista konfigurointia jokaiseen vaiheeseen.

## Prerequisites

**Käytätkö Dev Containeria?** Java ja Maven on jo asennettu. Tarvitset vain GitHubin henkilökohtaisen käyttöoikeustunnuksen.

**Paikallinen kehitys:**
- Java 21+, Maven 3.9+
- GitHubin henkilökohtainen käyttöoikeustunnus (ohjeet alla)

> **Huom:** Tässä moduulissa käytetään `gpt-4.1-nano` GitHub-malleista. Älä muuta mallin nimeä koodissa – se on määritetty toimimaan GitHubin saatavilla olevien mallien kanssa.

## Setup

### 1. Get Your GitHub Token

1. Mene osoitteeseen [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Klikkaa "Generate new token"
3. Anna kuvaava nimi (esim. "LangChain4j Demo")
4. Aseta vanhenemisaika (suositus 7 päivää)
5. "Account permissions" kohdassa etsi "Models" ja aseta "Read-only"
6. Klikkaa "Generate token"
7. Kopioi ja tallenna tunnuksesi – et näe sitä enää uudelleen

### 2. Set Your Token

**Vaihtoehto 1: VS Code (suositus)**

Jos käytät VS Codea, lisää tunnuksesi `.env`-tiedostoon projektin juureen:

Jos `.env`-tiedostoa ei ole, kopioi `.env.example` nimellä `.env` tai luo uusi `.env` projektiin.

**Esimerkki `.env`-tiedostosta:**
```bash
# Tiedostossa /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Sitten voit yksinkertaisesti klikata hiiren oikealla mitä tahansa demotiedostoa (esim. `BasicChatDemo.java`) Explorerissa ja valita **"Run Java"** tai käyttää Run and Debug -paneelin käynnistyskonfiguraatioita.

**Vaihtoehto 2: Terminaali**

Aseta tunnus ympäristömuuttujaksi:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Run the Examples

**VS Code:** Klikkaa hiiren oikealla mitä tahansa demotiedostoa Explorerissa ja valitse **"Run Java"**, tai käytä Run and Debug -paneelin käynnistyskonfiguraatioita (muista lisätä token ensin `.env`-tiedostoon).

**Mavenilla:** Vaihtoehtoisesti voit ajaa komentoriviltä:

### 1. Basic Chat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Prompt Patterns

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Näyttää zero-shot, few-shot, chain-of-thought ja roolipohjaiset kehotteet.

### 3. Function Calling

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

Tekoäly kutsuu automaattisesti Java-menetelmiäsi tarvittaessa.

### 4. Document Q&A (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Kysy kysymyksiä dokumenteistasi Easy RAG:n avulla, jossa upotus ja haku tapahtuvat automaattisesti.

### 5. Responsible AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Katso miten tekoälyn turvallisuussuodattimet estävät haitallista sisältöä.

## What Each Example Shows

**Basic Chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Aloita tästä nähdäksesi LangChain4j:n yksinkertaisimmillaan. Luo `OpenAiOfficialChatModel`, lähetä kehotteella `.chat()` ja saa vastaus. Tämä näyttää perustan: miten mallin alustus custom-päätepisteillä ja API-avaimilla tehdään. Kun ymmärrät tämän mallin, kaikki muu rakentuu sen varaan.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Kokeile [GitHub Copilotin](https://github.com/features/copilot) Chatilla:** Avaa [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ja kysy:
> - "Miten vaihtaisin koodissa GitHub-malleista Azure OpenAI:hin?"
> - "Mitä muita parametreja voin määrittää OpenAiOfficialChatModel.builder()-metodissa?"
> - "Miten lisäisin streaming-vastaukset sen sijaan että odotan koko vastausta?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Nyt kun tiedät, miten puhut mallille, tutki mitä sanot sille. Tämä demo käyttää samaa mallikonfiguraatiota mutta näyttää viisi erilaista kehotemallia. Kokeile zero-shot-kehotteita suorille ohjeille, few-shot-kehotteita jotka oppivat esimerkeistä, chain-of-thought-kehotteita jotka paljastavat päättelyvaiheita ja roolipohjaisia kehotteita, jotka määrittävät kontekstin. Näet miten sama malli antaa dramaattisen erilaisia vastauksia kehotteen asettelun perusteella.

Demo näyttää myös kehotepohjat, jotka ovat tehokas tapa luoda uudelleen käytettäviä kehotteita muuttujilla.
Alla oleva esimerkki näyttää kehotteen LangChain4j `PromptTemplate`-objektia käyttäen, jossa täytetään muuttujia. Tekoäly vastaa annetun kohteen ja aktiviteetin perusteella.

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

> **🤖 Kokeile [GitHub Copilotin](https://github.com/features/copilot) Chatilla:** Avaa [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ja kysy:
> - "Mikä ero on zero-shot- ja few-shot-kehotteilla, ja milloin käytän kumpaakin?"
> - "Miten lämpötila-parametri vaikuttaa mallin vastauksiin?"
> - "Mitkä ovat tekniikoita estää kehotepohjien injektointihyökkäykset tuotannossa?"
> - "Miten luon uudelleenkäytettäviä PromptTemplate-objekteja yleisiin malleihin?"

**Tool Integration** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Tässä LangChain4j muuttuu tehokkaaksi. Käytät `AiServices`-luokkaa tekoälyavustajan luomiseen, joka voi kutsua Java-metodejasi. Riittää, että merkitset metodit `@Tool("kuvaus")`-annotaatiolla ja LangChain4j hoitaa loput – tekoäly päättää automaattisesti, milloin käyttää mitäkin työkalua käyttäjän pyynnön mukaan. Tämä esittelee funktiokutsut, keskeisen tekniikan tekoälyn rakentamiseen, joka voi suorittaa toimintoja eikä vain vastata kysymyksiin.

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

> **🤖 Kokeile [GitHub Copilotin](https://github.com/features/copilot) Chatilla:** Avaa [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ja kysy:
> - "Miten @Tool-annotaatio toimii ja mitä LangChain4j tekee sillä taustalla?"
> - "Voiko tekoäly kutsua useita työkaluja peräkkäin ratkaistakseen monimutkaisia ongelmia?"
> - "Mitä tapahtuu, jos työkalu heittää poikkeuksen – miten virheet pitäisi käsitellä?"
> - "Miten integroisin oikean API:n tämän laskuesimerkin sijaan?"

**Document Q&A (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Tässä näet RAG:n (retrieval-augmented generation) käyttäen LangChain4j:n "Easy RAG" -menetelmää. Asiakirjat ladataan, pilkotaan automaattisesti ja upotetaan muistiin, minkä jälkeen sisältöhaku toimittaa relevanteimmat palat tekoälylle kyselyhetkellä. Tekoäly vastaa asiakirjojesi perusteella, ei yleisen tietämyksensä.

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

> **🤖 Kokeile [GitHub Copilotin](https://github.com/features/copilot) Chatilla:** Avaa [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ja kysy:
> - "Miten RAG estää tekoälyn harhaluulot verrattuna mallin koulutusdataan?"
> - "Mikä ero on tässä helpossa lähestymistavassa ja mukautetussa RAG-putkessa?"
> - "Miten skaalaisin tämän käsittelemään useita asiakirjoja tai isompia tietovarastoja?"

**Responsible AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Rakenna tekoälyn turvallisuus kerrospintaisesti. Tämä demo näyttää kaksi suojaustasoa, jotka toimivat yhdessä:

**Osa 1: LangChain4j Input Guardrails** – Estää vaaralliset kehotteet ennen niiden pääsyä LLM:ään. Luo omat suojakaiteet, jotka tarkistavat kielletyt avainsanat tai kaavat. Nämä toimivat koodissasi, joten ne ovat nopeita ja ilmaisia.

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

**Osa 2: Tarjoajan turvafiltterit** – GitHub-malleissa on sisäänrakennetut suodattimet, jotka tarttuvat siihen, mitä suojakaiteesi voivat jäädä huomaamatta. Näet tiukat estot (HTTP 400 -virheet) vakavissa rikkomuksissa ja pehmeät kieltäytymiset, joissa tekoäly kohteliaasti kieltäytyy.

> **🤖 Kokeile [GitHub Copilotin](https://github.com/features/copilot) Chatilla:** Avaa [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ja kysy:
> - "Mikä on InputGuardrail ja miten luon oman?"
> - "Mikä ero on kovalla estolla ja pehmeällä kieltäytymisellä?"
> - "Miksi käyttää sekä suojakaiteita että tarjoajan suodattimia yhdessä?"

## Next Steps

**Seuraava moduuli:** [01-introduction - Getting Started with LangChain4j](../01-introduction/README.md)

---

**Navigointi:** [← Takaisin pääsivulle](../README.md) | [Seuraava: Module 01 - Introduction →](../01-introduction/README.md)

---

## Troubleshooting

### First-Time Maven Build

**Ongelma**: Ensimmäinen `mvn clean compile` tai `mvn package` kestää kauan (10–15 minuuttia)

**Syy**: Mavenin täytyy ladata kaikki projektin riippuvuudet (Spring Boot, LangChain4j-kirjastot, Azure SDK:t jne.) ensimmäisellä kerralla.

**Ratkaisu**: Tämä on normaalia käyttäytymistä. Seuraavat kerrat ovat paljon nopeampia, kun riippuvuudet on tallennettu paikallisesti. Latausaika riippuu verkkoyhteyden nopeudesta.

### PowerShell Maven Command Syntax

**Ongelma:** Maven-komennot epäonnistuvat virheellä `Unknown lifecycle phase ".mainClass=..."`
**Syy**: PowerShell tulkitsee `=` muuttujan määrittämisen operaattorina, mikä rikkoo Maven-ominaisuussyntaksin

**Ratkaisu**: Käytä pysäytyksen jäsentämisoperaattoria `--%` ennen Maven-komentoa:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%`-operaattori kertoo PowerShellille, että kaikki jäljellä olevat argumentit annetaan kirjaimellisesti Mavenille ilman tulkintaa.

### Windows PowerShellin emojeiden näyttö

**Ongelma**: AI-vastauksissa näkyy roska merkkeinä (esim. `????` tai `â??`) PowerShellissä emojien sijaan

**Syy**: PowerShellin oletuskoodaus ei tue UTF-8-emojia

**Ratkaisu**: Suorita tämä komento ennen Java-sovellusten käynnistystä:
```cmd
chcp 65001
```

Tämä pakottaa UTF-8-koodauksen terminaaliin. Vaihtoehtoisesti käytä Windows Terminalia, jossa on parempi Unicode-tuki.

### API-pyyntöjen virheenkorjaus

**Ongelma**: Todennusvirheitä, pyyntörajoituksia tai odottamattomia vastauksia AI-mallilta

**Ratkaisu**: Esimerkeissä on `.logRequests(true)` ja `.logResponses(true)` näyttääksesi API-kutsut konsolissa. Tämä auttaa ratkaisemaan todennusvirheitä, pyyntörajoituksia tai odottamattomia vastauksia. Poista nämä liput tuotannossa vähentääksesi lokiääntä.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, otathan huomioon, että automaattiset käännökset voivat sisältää virheitä tai epätarkkuuksia. Alkuperäistä asiakirjaa sen alkuperäiskielellä tulisi pitää virallisena lähteenä. Tärkeiden tietojen osalta suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä aiheutuvista väärinkäsityksistä tai virhetulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->