# Module 01: Aloittaminen LangChain4j:n kanssa

## Sisällysluettelo

- [Videoesittely](../../../01-introduction)
- [Mitä opit](../../../01-introduction)
- [Esivaatimukset](../../../01-introduction)
- [Ymmärtäminen ydinkysymyksestä](../../../01-introduction)
- [Tokenien ymmärtäminen](../../../01-introduction)
- [Miten muisti toimii](../../../01-introduction)
- [Miten tämä käyttää LangChain4j:ta](../../../01-introduction)
- [Ota Azure OpenAI -ympäristö käyttöön](../../../01-introduction)
- [Suorita sovellus paikallisesti](../../../01-introduction)
- [Sovelluksen käyttäminen](../../../01-introduction)
  - [Stateless-chat (vasen paneeli)](../../../01-introduction)
  - [Stateful-chat (oikea paneeli)](../../../01-introduction)
- [Seuraavat askeleet](../../../01-introduction)

## Videoesittely

Katso tämä live-sessio, joka selittää, miten aloitetaan tämän moduulin kanssa:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Mitä opit

Pika-alussa käytit GitHub-malleja lähettääksesi kehotteita, kutsuaksesi työkaluja, rakentaaksesi RAG-putken ja testataksesi suojauksia. Ne demot näyttivät, mitä on mahdollista — nyt siirrymme Azure OpenAI:n ja GPT-5.2:n käyttöön ja alamme rakentaa tuotantotason sovelluksia. Tämä moduuli keskittyy keskustelevaan tekoälyyn, joka muistaa kontekstin ja ylläpitää tilaa — käsitteet, joita nämä pika-alun demot käyttivät taustalla mutta eivät selittäneet.

Käytämme tässä oppaassa Azure OpenAI:n GPT-5.2:ta, koska sen edistyneet päättelykyvyt tekevät eri mallien käyttäytymisen selkeämmäksi. Kun lisäät muistin, näet eron selvästi. Tämä helpottaa ymmärtämään, mitä kukin komponentti tuo sovellukseesi.

Rakennat yhden sovelluksen, joka havainnollistaa molempia malleja:

**Stateless-chat** – Jokainen pyyntö on itsenäinen. Malli ei muista aiempia viestejä. Tämä on malli, jota käytit pika-alussa.

**Stateful-keskustelu** – Jokainen pyyntö sisältää keskusteluhistorian. Malli ylläpitää kontekstia useiden vuorojen ajan. Tätä tuotantosovellukset vaativat.

## Esivaatimukset

- Azure-tilaus, jossa on Azure OpenAI -käyttöoikeus
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Huom:** Java, Maven, Azure CLI ja Azure Developer CLI (azd) on esiasennettu mukana toimitettuun devcontaineriin.

> **Huom:** Tämä moduuli käyttää GPT-5.2:a Azure OpenAI:ssa. Käyttöönotto määritellään automaattisesti `azd up` -komennolla – älä muuta mallin nimeä koodissa.

## Ymmärtäminen ydinkysymyksestä

Kielimallit ovat tilattomia. Jokainen API-kutsu on itsenäinen. Jos lähetät "Nimeni on John" ja sitten kysyt "Mikä nimeni on?", malli ei tiedä, että juuri esittäydyit. Se käsittelee jokaisen pyynnön kuin se olisi ensimmäinen keskustelusi.

Tämä sopii yksinkertaisiin kyselyihin, mutta on hyödytöntä oikeissa sovelluksissa. Asiakaspalvelubottien pitää muistaa, mitä kerroit niille. Henkilökohtaisten avustajien tarvitsee saada konteksti. Kaikki monivuoroinen keskustelu vaatii muistia.

Seuraava kaavio vertaa kahta lähestymistapaa — vasemmalla tilaton kutsu, joka unohtaa nimesi; oikealla tilaava kutsu, jota tukee ChatMemory ja joka muistaa sen.

<img src="../../../translated_images/fi/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Ero tilattoman (itsenäiset kutsut) ja tilaavan (kontekstia huomioivan) keskustelun välillä*

## Tokenien ymmärtäminen

Ennen kuin sukellat keskusteluihin, on tärkeää ymmärtää tokenit – perusyksiköt tekstissä, joita kielimallit käsittelevät:

<img src="../../../translated_images/fi/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Esimerkki siitä, miten teksti pilkotaan tokeneiksi – "I love AI!" muuttuu 4 erilliseksi käsittelyyksiköksi*

Tokenit ovat mittayksikkö AI-malleille tekstin mittaamisessa ja käsittelyssä. Sanat, välimerkit ja jopa välilyönnit voivat olla tokeneita. Mallillasi on raja, kuinka monta tokenia se voi käsitellä kerralla (GPT-5.2:lle 400 000, josta enintään 272 000 syöttötokeneita ja 128 000 tulostokoneita). Tokenien ymmärtäminen auttaa hallitsemaan keskustelun pituutta ja kustannuksia.

## Miten muisti toimii

Chat-muisti ratkaisee tilattomuuden ongelman ylläpitämällä keskusteluhistoriaa. Ennen kuin lähetät pyynnön mallille, kehys lisää mukaan aiemmin soveltuvia viestejä. Kun kysyt "Mikä nimeni on?", järjestelmä lähettää koko keskusteluhistorian, jolloin malli näkee, että sanoit aiemmin "Nimeni on John."

LangChain4j tarjoaa muistiratkaisuja, jotka hoitavat tämän automaattisesti. Valitset, kuinka monta viestiä säilytät, ja kehys hallinnoi kontekstin ikkunaa. Alla olevassa kaaviossa näkyy, miten MessageWindowChatMemory ylläpitää liukuaakkkoa viimeisimmistä viesteistä.

<img src="../../../translated_images/fi/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory ylläpitää liukuvaa ikkunaa viimeisimmistä viesteistä ja pudottaa automaattisesti vanhoja*

## Miten tämä käyttää LangChain4j:ta

Tämä moduuli laajentaa pika-aloitusta integroimalla Spring Bootin ja lisäämällä keskustelumuistin. Näin osat toimivat yhdessä:

**Riippuvuudet** – Lisää kaksi LangChain4j-kirjastoa:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**Chat-malli** – Määritä Azure OpenAI Spring beaniksi ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

Builder lukee käyttöoikeustiedot ympäristömuuttujista, jotka ovat asetettu `azd up`:lla. Määrittämällä `baseUrl` Azure-päätepisteellesi saat OpenAI-asiakkaan toimimaan Azure OpenAI:n kanssa.

**Keskustelumuisti** – Seuraa chat-historiaa MessageWindowChatMemorylla ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Luo muisti `withMaxMessages(10)` pitämään viimeiset 10 viestiä. Lisää käyttäjän ja tekoälyn viestit tyypeillä kääreillä: `UserMessage.from(text)` ja `AiMessage.from(text)`. Hae historia `memory.messages()`-metodilla ja lähetä se mallille. Palvelu tallentaa erilliset muisti-instanssit keskustelu-ID:lle, jolloin useat käyttäjät voivat keskustella samanaikaisesti.

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatin kanssa:** Avaa [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ja kysy:
> - "Miten MessageWindowChatMemory päättää, mitkä viestit pudottaa, kun ikkuna on täynnä?"
> - "Voinko toteuttaa oman muistivaraston käyttämällä tietokantaa muistin sijaan?"
> - "Miten lisäisin tiivistämisen vanhan keskusteluhistorian puristamiseksi?"

Stateless-chatin päätepiste ohittaa muistin täysin – pelkkä `chatModel.chat(prompt)` kuten pika-alussa. Stateful-päätepiste lisää viestit muistiin, hakee historian ja sisältää kontekstin jokaisessa pyynnössä. Sama malli, eri mallit.

## Ota Azure OpenAI -ympäristö käyttöön

**Bash:**
```bash
cd 01-introduction
azd up  # Valitse tilaus ja sijainti (eastus2 suositeltu)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Valitse tilaus ja sijainti (suositellaan eastus2)
```

> **Huom:** Jos kohtaat aikakatkaisuvian (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), aja yksinkertaisesti `azd up` uudelleen. Azure-resurssit voivat olla vielä pystytystilassa taustalla, ja uudelleenyritys sallii käyttöönoton valmistumisen, kun resurssit saavuttavat lopullisen tilan.

Tämä:
1. Ota Azure OpenAI -resurssi käyttöön GPT-5.2- ja text-embedding-3-small -malleilla
2. Luo automaattisesti `.env`-tiedoston projektin juureen käyttöoikeuksilla
3. Asettaa kaikki tarvittavat ympäristömuuttujat

**Onko käyttöönotossa ongelmia?** Katso [Infrastructure README](infra/README.md) -tiedosto vianmäärityksestä, mukaan lukien aliverkkotunnuskonfliktit, manuaaliset Azure Portal -asennusohjeet ja mallin konfigurointiohjeet.

**Varmista, että käyttöönotto onnistui:**

**Bash:**
```bash
cat ../.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY jne.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Pitäisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, jne.
```

> **Huom:** `azd up` -komento luo `.env`-tiedoston automaattisesti. Jos sinun täytyy päivittää sitä myöhemmin, voit joko muokata `.env`-tiedostoa käsin tai luoda sen uudelleen suorittamalla:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## Suorita sovellus paikallisesti

**Varmista käyttöönotto:**

Varmista, että `.env`-tiedosto on olemassa juurihakemistossa, sisältäen Azure-käyttäjätunnukset. Suorita tämä moduulikansiosta (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Pitäisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Näyttää AZURE_OPENAI_ENDPOINTin, API_KEYn, DEPLOYMENTin
```

**Käynnistä sovellukset:**

**Vaihtoehto 1: Spring Boot Dashboardin käyttö (suositeltu VS Code -käyttäjille)**

Dev-container sisältää Spring Boot Dashboard -laajennuksen, joka tarjoaa visuaalisen käyttöliittymän kaikkien Spring Boot -sovellusten hallintaan. Löydät sen VS Coden vasemman reunan Aktiviteettipalkista (etsi Spring Boot -ikonia).

Spring Boot Dashboardista voit:
- Nähdä kaikki käytettävissä olevat Spring Boot -sovellukset työtilassa
- Käynnistää/pysäyttää sovellukset yhdellä napsautuksella
- Katsoa sovelluslokia reaaliajassa
- Valvoa sovellusten tilaa

Napsauta toistopainiketta "introduction"-moduulin vieressä käynnistääksesi tämän moduulin, tai käynnistä kaikki moduulit kerralla.

<img src="../../../translated_images/fi/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard VS Codessa — käynnistä, pysäytä ja valvo kaikki moduulit yhdestä paikasta*

**Vaihtoehto 2: Kuoriskriptien käyttö**

Käynnistä kaikki web-sovellukset (moduulit 01–04):

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
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Molemmat skriptit lataavat automaattisesti ympäristömuuttujat juuren `.env`-tiedostosta ja rakentavat JAR-tiedostot, jos niitä ei vielä ole.

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

Avaa http://localhost:8080 selaimessasi.

**Pysäyttääksesi:**

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

Sovellus tarjoaa verkkokäyttöliittymän, jossa on kaksi viestien toteutusta rinnakkain.

<img src="../../../translated_images/fi/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Hallintapaneeli, joka näyttää sekä yksinkertaisen chatin (tilaton) että keskusteleva chat (tilallinen)*

### Stateless-chat (vasen paneeli)

Kokeile ensin tätä. Kysy "Nimeni on John" ja heti perään "Mikä nimeni on?" Malli ei muista, koska jokainen viesti on itsenäinen. Tämä havainnollistaa perustavanlaatuista ongelmaa peruskielimallin integraatiossa – ei keskustelukontekstia.

<img src="../../../translated_images/fi/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*Tekoäly ei muista nimeäsi edellisestä viestistä*

### Stateful-chat (oikea paneeli)

Kokeile nyt samaa sarjaa täällä. Kysy "Nimeni on John" ja sitten "Mikä nimeni on?" Tällä kertaa se muistaa. Erona on MessageWindowChatMemory – se ylläpitää keskusteluhistoriaa ja sisällyttää sen jokaiseen pyyntöön. Näin tuotantokeskustelevat tekoälyt toimivat.

<img src="../../../translated_images/fi/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*Tekoäly muistaa nimesi aiemmasta keskustelusta*

Molemmissa paneeleissa käytetään samaa GPT-5.2-mallia. Ainoa ero on muisti. Tämä tekee selväksi, mitä muisti tuo sovellukseesi ja miksi se on olennaista todellisiin käyttötapauksiin.

## Seuraavat askeleet

**Seuraava moduuli:** [02-prompt-engineering - Kehoteinsinöörityö GPT-5.2:n kanssa](../02-prompt-engineering/README.md)

---

**Navigointi:** [← Edellinen: Module 00 - Pika-aloitus](../00-quick-start/README.md) | [Takaisin pääsivulle](../README.md) | [Seuraava: Module 02 - Kehoteinsinöörityö →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, on hyvä huomioida, että automaattiset käännökset saattavat sisältää virheitä tai epätarkkuuksia. Alkuperäistä asiakirjaa sen alkuperäiskielellä tulee pitää määräävänä lähteenä. Tärkeissä tiedoissa suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä aiheutuvista väärinkäsityksistä tai virhetulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->