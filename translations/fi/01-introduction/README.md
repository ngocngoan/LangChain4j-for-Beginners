# Module 01: Aloittaminen LangChain4j:llä

## Sisällysluettelo

- [Mitä opit](../../../01-introduction)
- [Esivaatimukset](../../../01-introduction)
- [Ymmärtäminen keskeisestä ongelmasta](../../../01-introduction)
- [Tokenien ymmärtäminen](../../../01-introduction)
- [Miten muisti toimii](../../../01-introduction)
- [Miten tämä käyttää LangChain4j:ää](../../../01-introduction)
- [Azure OpenAI -infrastruktuurin käyttöönotto](../../../01-introduction)
- [Sovelluksen paikallinen suorittaminen](../../../01-introduction)
- [Sovelluksen käyttö](../../../01-introduction)
  - [Tilavapaa keskustelu (vasen paneeli)](../../../01-introduction)
  - [Tilallinen keskustelu (oikea paneeli)](../../../01-introduction)
- [Seuraavat askeleet](../../../01-introduction)

## Mitä opit

Jos kävit pikakäynnistyksen läpi, näit miten lähetetään kehotteita ja saadaan vastauksia. Se on perusta, mutta todelliset sovellukset tarvitsevat enemmän. Tässä moduulissa opit rakentamaan keskustelevaa tekoälyä, joka muistaa kontekstin ja ylläpitää tilaa - ero yksittäisen demon ja tuotantovalmiin sovelluksen välillä.

Käytämme tässä oppaassa Azure OpenAI:n GPT-5.2:ta, koska sen edistynyt päättelykyky tekee erilaisten mallien käyttäytymisen selkeämmäksi. Kun lisäät muistin, huomaat eron selvästi. Tämä helpottaa ymmärtämään, mitä kukin komponentti tuo sovellukseesi.

Rakennat yhden sovelluksen, joka demonstroi molempia malleja:

**Tilavapaa keskustelu** – Jokainen pyyntö on riippumaton. Malli ei muista aiempia viestejä. Tämä on se malli, jota käytit pikakäynnistyksessä.

**Tilallinen keskustelu** – Jokainen pyyntö sisältää keskusteluhistorian. Malli ylläpitää kontekstia useamman vuoron ajan. Tämä on mitä tuotantosovellukset vaativat.

## Esivaatimukset

- Azure-tilaus, jossa Azure OpenAI -käyttöoikeus
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Huom:** Java, Maven, Azure CLI ja Azure Developer CLI (azd) ovat valmiiksi asennettu devcontainerissa.

> **Huom:** Tämä moduuli käyttää GPT-5.2:ta Azure OpenAI:ssa. Käyttöönotto konfiguroidaan automaattisesti `azd up` -komennolla – älä muuta mallin nimeä koodissa.

## Ymmärtäminen keskeisestä ongelmasta

Kielimallit ovat tilattomia. Jokainen API-kutsu on itsenäinen. Jos lähetät "Nimeni on John" ja sitten kysyt "Mikä nimeni on?", malli ei tiedä, että juuri esittelit itsesi. Se käsittelee jokaisen pyynnön ikään kuin se olisi ensimmäinen keskustelusi.

Tämä sopii yksinkertaiseen kysymys-vastaus -käyttöön, mutta on hyödytöntä todellisissa sovelluksissa. Asiakaspalvelurobotit tarvitsevat muistaa, mitä kerroit niille. Henkilökohtaiset avustajat tarvitsevat kontekstin. Mikä tahansa monivuoroinen keskustelu vaatii muistia.

<img src="../../../translated_images/fi/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Ero tilattoman (itsenäiset kutsut) ja tilaistietoisen (kontekstia hyödyntävän) keskustelun välillä*

## Tokenien ymmärtäminen

Ennen kuin syvennyt keskusteluihin, on tärkeää ymmärtää tokenit – tekstin perusyksiköt, joita kielimallit käsittelevät:

<img src="../../../translated_images/fi/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Esimerkki siitä, miten teksti pilkotaan tokeneiksi – "I love AI!" tulee neljäksi erilliseksi käsittelyyksiköksi*

Tokenit ovat se, miten tekoälymallit mittaavat ja käsittelevät tekstiä. Sanat, välimerkit ja jopa välilyönnit voivat olla tokeneita. Mallillasi on raja sille, kuinka monta tokenia se voi käsitellä kerralla (400 000 GPT-5.2:lle, josta 272 000 on syötettä ja 128 000 tuotosta). Tokenien ymmärtäminen auttaa hallitsemaan keskustelun pituutta ja kustannuksia.

## Miten muisti toimii

Keskustelumuisti ratkaisee tilattomuusongelman pitämällä kirjaa keskusteluhistoriasta. Ennen kuin lähetät pyynnön mallille, kehys lisää siihen asiaankuuluvat aiemmat viestit. Kun kysyt "Mikä nimeni on?", järjestelmä todellisuudessa lähettää koko keskusteluhistorian, mikä antaa mallille mahdollisuuden nähdä, että sanoit aiemmin "Nimeni on John."

LangChain4j tarjoaa muistien toteutuksia, jotka hoitavat tämän automaattisesti. Voit valita kuinka monta viestiä pidät tallessa ja kehys hallinnoi konteksti-ikkunaa.

<img src="../../../translated_images/fi/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory ylläpitää liukuvaa ikkunaa uusimmista viesteistä ja poistaa vanhoja automaattisesti*

## Miten tämä käyttää LangChain4j:ää

Tämä moduuli laajentaa pikakäynnistystä integroimalla Spring Bootin ja lisäämällä keskustelumuistin. Näin palaset sopivat yhteen:

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

**Keskustelumalli** – Määritä Azure OpenAI Spring beaniksi ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Rakentaja lukee tunnistetiedot ympäristömuuttujista, jotka `azd up` asettaa. Kun määrität `baseUrl`-kentän Azure-päätteen osoitteeseen, OpenAI-asiakas toimii Azure OpenAI:n kanssa.

**Keskustelumuisti** – Seuraa keskusteluhistoriaa käyttämällä MessageWindowChatMemorya ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Luo muisti `withMaxMessages(10)` -asetuksella, jolla pidetään viimeiset 10 viestiä. Lisää käyttäjän ja tekoälyn viestit tyypitetyillä kuorilla: `UserMessage.from(text)` ja `AiMessage.from(text)`. Hae historia `memory.messages()` -metodilla ja lähetä se mallille. Palvelu tallentaa erilliset muistit kutakin keskustelu-ID:tä varten, mahdollistaen useiden käyttäjien samanaikaisen keskustelun.

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatin kanssa:** Avaa [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ja kysy:
> - "Miten MessageWindowChatMemory päättää, mitkä viestit pudottaa kun ikkuna on täynnä?"
> - "Voinko toteuttaa oman muistin tallennuksen tietokantaa käyttäen muistin sijaan?"
> - "Miten lisäisin tiivistämisen vanhojen keskusteluhistorioiden pakkaamiseksi?"

Tilaton chat-päätepiste ohittaa muistin kokonaan – vain `chatModel.chat(prompt)` kuten pikakäynnistyksessä. Tilallinen päätepiste lisää viestit muistiin, hakee historian ja sisältää sen kontekstin jokaisessa pyynnössä. Sama mallin konfiguraatio, eri mallit.

## Azure OpenAI -infrastruktuurin käyttöönotto

**Bash:**
```bash
cd 01-introduction
azd up  # Valitse tilaus ja sijainti (eastus2 suositellaan)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Valitse tilaus ja sijainti (suositus eastus2)
```

> **Huom:** Jos kohtaat aikakatkaisun virheen (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), suorita vain `azd up` uudelleen. Azure-resurssit voivat olla yhä provisioitavana taustalla, ja uudelleenyrittäminen sallii käyttöönoton valmistumisen, kun resurssit saavuttavat päättävän tilan.

Tämä tekee seuraavaa:
1. Ottaa käyttöön Azure OpenAI -resurssin GPT-5.2:lla ja text-embedding-3-small -malleilla
2. Luo automaattisesti `.env`-tiedoston projektin juureen tunnistetiedoilla
3. Asettaa kaikki tarvittavat ympäristömuuttujat

**Ongelmat käyttöönotossa?** Katso [Infrastructure README](infra/README.md) -tiedosto tarkempia ohjeita alialueen nimeämiskonflikteista, manuaalisista Azure Portal -käyttöönotoista ja mallin konfiguroinnista.

**Vahvista käyttöönotto onnistui:**

**Bash:**
```bash
cat ../.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY jne.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY jne.
```

> **Huom:** `azd up` luo automaattisesti `.env`-tiedoston. Jos sinun täytyy päivittää sitä myöhemmin, voit joko muokata `.env`-tiedostoa käsin tai luoda sen uudelleen suorittamalla:
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

## Sovelluksen paikallinen suorittaminen

**Vahvista käyttöönotto:**

Varmista, että `.env`-tiedosto on olemassa juurihakemistossa ja sisältää Azure-tunnistetiedot:

**Bash:**
```bash
cat ../.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Käynnistä sovellukset:**

**Vaihtoehto 1: Spring Boot Dashboardin käyttö (suositeltu VS Code -käyttäjille)**

Dev containerissa on Spring Boot Dashboard -laajennus, joka tarjoaa visuaalisen käyttöliittymän hallita kaikkia Spring Boot -sovelluksia. Löydät sen VS Coden vasemmasta toimintaikkunasta (etsimellä Spring Boot -ikonia).

Spring Boot Dashboardista voit:
- Näyttää kaikki käytettävissä olevat Spring Boot -sovellukset työtilassa
- Käynnistää/pysäyttää sovelluksia yhdellä klikkauksella
- Tarkastella sovelluslokeja reaaliajassa
- Valvoa sovelluksen tilaa

Klikkaa toistopainiketta "introduction" -moduulin käynnistämiseksi tai käynnistä kaikki moduulit kerralla.

<img src="../../../translated_images/fi/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Vaihtoehto 2: Shell-skriptien käyttö**

Käynnistä kaikki web-sovellukset (moduulit 01-04):

**Bash:**
```bash
cd ..  # Juurikansiosta
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Juurikansiosta
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

Molemmat skriptit lataavat automaattisesti ympäristömuuttujat juuren `.env`-tiedostosta ja rakentavat JAR-paketit, jos niitä ei vielä ole.

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

Avaa selaimessa http://localhost:8080

**Pysäyttääksesi:**

**Bash:**
```bash
./stop.sh  # Tämä moduuli vain
# Tai
cd .. && ./stop-all.sh  # Kaikki moduulit
```

**PowerShell:**
```powershell
.\stop.ps1  # Vain tämä moduuli
# Tai
cd ..; .\stop-all.ps1  # Kaikki moduulit
```

## Sovelluksen käyttö

Sovellus tarjoaa web-käyttöliittymän kahdella päällekkäin olevalla chat-toteutuksella.

<img src="../../../translated_images/fi/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Hallintapaneeli näyttää sekä yksinkertaisen chatin (tilaton) että keskustelullisen chatin (tilallinen) vaihtoehdot*

### Tilavapaa keskustelu (vasen paneeli)

Kokeile ensin tätä. Kysy "Nimeni on John" ja heti perään "Mikä nimeni on?" Malli ei muista, koska jokainen viesti on itsenäinen. Tämä demonstroi keskeisen ongelman peruskielimallin integroinnissa – ei keskustelukontekstia.

<img src="../../../translated_images/fi/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*Tekoäly ei muista nimeäsi edellisestä viestistä*

### Tilallinen keskustelu (oikea paneeli)

Kokeile nyt samaa sekvenssiä täällä. Kysy "Nimeni on John" ja sitten "Mikä nimeni on?" Tällä kertaa se muistaa. Erona on MessageWindowChatMemory – se ylläpitää keskusteluhistoriaa ja sisällyttää sen jokaiseen pyyntöön. Näin tuotantotason keskusteleva tekoäly toimii.

<img src="../../../translated_images/fi/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*Tekoäly muistaa nimesi keskustelun aiemmasta kohdasta*

Molemmat paneelit käyttävät samaa GPT-5.2 -mallia. Ainoa ero on muisti. Tämä tekee selväksi, mitä muisti tuo sovellukseesi ja miksi se on välttämätön oikeassa käytössä.

## Seuraavat askeleet

**Seuraava moduuli:** [02-prompt-engineering - Kehotusmallinnus GPT-5.2:n kanssa](../02-prompt-engineering/README.md)

---

**Navigointi:** [← Edellinen: Module 00 - Pikakäynnistys](../00-quick-start/README.md) | [Takaisin pääsivulle](../README.md) | [Seuraava: Module 02 - Kehotusmallinnus →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:  
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, automaattiset käännökset saattavat sisältää virheitä tai epätarkkuuksia. Alkuperäistä asiakirjaa sen alkuperäiskielellä tulee pitää ensisijaisena lähteenä. Tärkeissä asioissa suositellaan ammattilaisen tekemää ihmiskäännöstä. Emme ota vastuuta tämän käännöksen käytöstä aiheutuvista väärinymmärryksistä tai virhetulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->