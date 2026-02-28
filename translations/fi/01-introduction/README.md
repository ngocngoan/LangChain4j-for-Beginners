# Moduuli 01: Aloittaminen LangChain4j:n kanssa

## Sisällysluettelo

- [Videoesittely](../../../01-introduction)
- [Mitä opit](../../../01-introduction)
- [Esivaatimukset](../../../01-introduction)
- [Ymmärrä ydinkysymys](../../../01-introduction)
- [Ymmärrä tokenit](../../../01-introduction)
- [Miten muisti toimii](../../../01-introduction)
- [Miten tämä käyttää LangChain4j:ä](../../../01-introduction)
- [Ota Azure OpenAI -infrastruktuuri käyttöön](../../../01-introduction)
- [Suorita sovellus paikallisesti](../../../01-introduction)
- [Sovelluksen käyttäminen](../../../01-introduction)
  - [Tilaton keskustelu (vasen paneeli)](../../../01-introduction)
  - [Tila säilyvä keskustelu (oikea paneeli)](../../../01-introduction)
- [Seuraavat askeleet](../../../01-introduction)

## Videoesittely

Katso tämä live-istunto, joka selittää, miten pääset alkuun tämän moduulin kanssa: [Getting Started with LangChain4j - Live Session](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Mitä opit

Jos suoritat pikakäynnistyksen, näet kuinka lähettää pyyntöjä ja saada vastauksia. Se on perusta, mutta todelliset sovellukset tarvitsevat enemmän. Tässä moduulissa opit rakentamaan keskustelevaa tekoälyä, joka muistaa kontekstin ja ylläpitää tilaa – ero kertaluonteisen demon ja tuotantovalmiin sovelluksen välillä.

Käytämme tässä oppaassa Azure OpenAI:n GPT-5.2:ta, koska sen kehittyneet päättelyominaisuudet tekevät eri mallikuvioiden käytöksen selkeämmäksi. Kun lisäät muistin, huomaat eron selvästi. Tämä helpottaa ymmärtämään, mitä kukin komponentti tuo sovellukseesi.

Rakennat yhden sovelluksen, joka havainnollistaa molempia kuvioita:

**Tilaton keskustelu** – Jokainen pyyntö on itsenäinen. Malli ei muista aiempia viestejä. Tämä on se kuvio, jota käytit pikakäynnistyksessä.

**Tila säilyvä keskustelu** – Jokainen pyyntö sisältää keskusteluhistorian. Malli ylläpitää kontekstia useiden vuorojen ajan. Tämä on, mitä tuotantosovellukset vaativat.

## Esivaatimukset

- Azure-tilaus, jossa on Azure OpenAI -käyttöoikeus
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Huom:** Java, Maven, Azure CLI ja Azure Developer CLI (azd) ovat valmiiksi asennettuna mukana toimitetussa kehityssäiliössä.

> **Huom:** Tämä moduuli käyttää GPT-5.2:ta Azure OpenAI:ssa. Käyttöönotto määritellään automaattisesti `azd up` -komennolla – älä muuta mallin nimeä koodissa.

## Ymmärrä ydinkysymys

Kielimallit ovat tilattomia. Jokainen API-kutsu on riippumaton. Jos lähetät "Nimeni on John" ja sitten kysyt "Mikä nimeni on?", mallilla ei ole aavistustakaan, että juuri esittelit itsesi. Se käsittelee jokaisen pyynnön kuin se olisi ensimmäinen keskustelusi koskaan.

Tämä toimii yksinkertaisissa kysymys-vastaus-tilanteissa, mutta on hyödytöntä oikeissa sovelluksissa. Asiakaspalvelubottien täytyy muistaa, mitä kerroit niille. Henkilökohtaisten avustajien täytyy ymmärtää konteksti. Mikä tahansa usean vuoron keskustelu vaatii muistia.

<img src="../../../translated_images/fi/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Ero tilattomien (riippumattomat kutsut) ja tila säilyvien (kontekstia ymmärtävä) keskustelujen välillä*

## Ymmärrä tokenit

Ennen keskusteluihin siirtymistä on tärkeää ymmärtää tokenit – perusyksiköt, joita kielimallit käsittelevät:

<img src="../../../translated_images/fi/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Esimerkki siitä, miten teksti pilkotaan tokeneiksi – "I love AI!" muuttuu 4 erilliseksi prosessointiyksiköksi*

Tokenit ovat keino, jolla tekoälymallit mittaavat ja käsittelevät tekstiä. Sanat, välimerkit ja jopa välilyönnit voivat olla tokeneita. Mallillasi on rajallinen määrä tokeneita, joita se voi käsitellä kerralla (400 000 GPT-5.2:lla, josta enintään 272 000 on syöte- ja 128 000 tulostokenia). Tokenien ymmärtäminen auttaa hallitsemaan keskustelun pituutta ja kustannuksia.

## Miten muisti toimii

Keskustelumuisti ratkaisee tilattomuusongelman ylläpitämällä keskusteluhistoriaa. Ennen kuin lähetät pyynnön mallille, kehys lisää mukaan oleelliset aiemmat viestit. Kun kysyt "Mikä nimeni on?", järjestelmä lähettää kokonaisen keskusteluhistorian, mikä antaa mallille tiedon, että sanoit aiemmin "Nimeni on John."

LangChain4j tarjoaa muistirakenteita, jotka hoitavat tämän automaattisesti. Valitset, kuinka monta viestiä säilytetään, ja kehys hallitsee kontekstin ikkunaa.

<img src="../../../translated_images/fi/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory ylläpitää liukuvaa ikkunaa viimeisimmistä viesteistä, pudottaen automaattisesti vanhat pois*

## Miten tämä käyttää LangChain4j:ä

Tämä moduuli laajentaa pikakäynnistystä integroimalla Spring Bootin ja lisäämällä keskustelumuistin. Näin osat sopivat yhteen:

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

Rakentaja lukee tunnistetiedot ympäristömuuttujista, jotka `azd up` on asettanut. Asettamalla `baseUrl` osoittamaan Azure-päätteeseen saat OpenAI-asiakkaan toimimaan Azure OpenAI:n kanssa.

**Keskustelumuisti** – Seuraa keskusteluhistoriaa MessageWindowChatMemoryllä ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Luo muisti `withMaxMessages(10)`, joka pitää viimeiset 10 viestiä. Lisää käyttäjän ja tekoälyn viestit typetyillä käärimillä: `UserMessage.from(text)` ja `AiMessage.from(text)`. Hae historia `memory.messages()` ja lähetä se mallille. Palvelu tallentaa erillisiä muistiesimerkkejä keskustelu-ID:n mukaan, mahdollistaen useiden käyttäjien samanaikaisen keskustelun.

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatin kanssa:** Avaa [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ja kysy:
> - "Miten MessageWindowChatMemory päättää, mitkä viestit poistetaan, kun ikkuna on täynnä?"
> - "Voinko toteuttaa mukautetun muistivarastoinnin tietokannan avulla muistin sijaan?"
> - "Miten lisäisin yhteenvedon puristamaan vanhaa keskusteluhistoriaa?"

Tilaton chat-päätepiste ohittaa muistin kokonaan – vain `chatModel.chat(prompt)` kuten pikakäynnistyksessä. Tila säilyvä päätepiste lisää viestit muistiin, hakee historian ja sisällyttää tuon kontekstin jokaiseen pyyntöön. Sama malliasetus, eri kuviot.

## Ota Azure OpenAI -infrastruktuuri käyttöön

**Bash:**
```bash
cd 01-introduction
azd up  # Valitse tilaus ja sijainti (suositellaan eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Valitse tilaus ja sijainti (suositeltu itäinen Yhdysvallat 2)
```

> **Huom:** Jos kohtaat aikakatkaisun virheen (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), suorita vain `azd up` uudelleen. Azure-resurssit voivat olla yhä määrittymässä taustalla, ja uudelleenyritys antaa käyttöönoton valmistua, kun resurssit saavuttavat lopullisen tilan.

Tämä:
1. Ottaa käyttöön Azure OpenAI -resurssin GPT-5.2- ja text-embedding-3-small -malleilla
2. Luo automaattisesti `.env`-tiedoston projektin juureen tunnistustiedoilla
3. Määrittää kaikki tarvittavat ympäristömuuttujat

**Onko käyttöönotossa ongelmia?** Katso [Infrastruktuurin README](infra/README.md) lisävianmääritystä varten, mukaan lukien aliverkkotunnusten nimikonfliktit, manuaaliset Azure Portal -asennusohjeet ja mallikonfigurointiohjeet.

**Varmista, että käyttöönotto onnistui:**

**Bash:**
```bash
cat ../.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY jne.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY jne.
```

> **Huom:** `azd up` -komento luo `.env` -tiedoston automaattisesti. Jos tarvitset päivitystä myöhemmin, voit joko muokata `.env` -tiedostoa käsin tai luoda sen uudelleen suorittamalla:
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

Varmista, että `.env`-tiedosto on olemassa juurikansiossa Azure-tunnuksilla:

**Bash:**
```bash
cat ../.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Käynnistä sovellukset:**

**Vaihtoehto 1: Spring Boot Dashboardin käyttäminen (Suositellaan VS Code -käyttäjille)**

Kehityssäiliössä on mukana Spring Boot Dashboard -laajennus, joka tarjoaa visuaalisen käyttöliittymän kaikkien Spring Boot -sovellusten hallintaan. Löydät sen VS Code:n vasemman laidan Activity Barista (etsi Spring Boot -kuvake).

Spring Boot Dashboardista voit:
- Näyttää kaikki työtilassa olevat Spring Boot -sovellukset
- Käynnistää/pysäyttää sovelluksia yhdellä napsautuksella
- Katsoa sovelluksen lokit reaaliajassa
- Valvoa sovelluksen tilaa

Klikkaa pelipainiketta "introduction" käynnistääksesi tämän moduulin tai käynnistä kaikki moduulit kerralla.

<img src="../../../translated_images/fi/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Vaihtoehto 2: Käytä komentojonoja**

Käynnistä kaikki web-sovellukset (moduulit 01-04):

**Bash:**
```bash
cd ..  # Juurihakemistosta
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

Molemmat skriptit lataavat automaattisesti ympäristömuuttujat juuren `.env` -tiedostosta ja rakentavat JAR-tiedostot, jos niitä ei ole.

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

Avaa selaimessa osoite http://localhost:8080.

**Pysäyttämiseen:**

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

Sovellus tarjoaa web-käyttöliittymän, jossa on kaksi rinnakkaista chattiratkaisua.

<img src="../../../translated_images/fi/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Hallintapaneeli näyttää sekä Yksinkertaisen keskustelun (tilaton) että Keskusteleva keskustelu (tila säilyvä) vaihtoehdot*

### Tilaton keskustelu (vasen paneeli)

Kokeile tätä ensin. Kysy "Nimeni on John" ja heti perään "Mikä nimeni on?" Malli ei muista, koska jokainen viesti on itsenäinen. Tämä havainnollistaa ydinkysymystä peruskielimallien integroinnissa – ei keskustelukontekstia.

<img src="../../../translated_images/fi/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*Tekoäly ei muista nimeäsi edellisestä viestistä*

### Tila säilyvä keskustelu (oikea paneeli)

Kokeile samaa sarjaa täällä. Kysy "Nimeni on John" ja sitten "Mikä nimeni on?" Tällä kertaa se muistaa. Erona on MessageWindowChatMemory – se ylläpitää keskusteluhistoriaa ja liittää sen jokaiseen pyyntöön. Näin tuotantokeskusteleva tekoäly toimii.

<img src="../../../translated_images/fi/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*Tekoäly muistaa nimesi aiemmasta keskustelusta*

Molemmat paneelit käyttävät samaa GPT-5.2 -mallia. Ainoa ero on muisti. Tämä tekee selväksi, mitä muisti tuo sovellukseesi ja miksi se on välttämätön todellisissa käyttötapauksissa.

## Seuraavat askeleet

**Seuraava moduuli:** [02-prompt-engineering - Prompt Engineering with GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigointi:** [← Edellinen: Moduuli 00 - Pikakäynnistys](../00-quick-start/README.md) | [Takaisin päähakemistoon](../README.md) | [Seuraava: Moduuli 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Pyrimme tarkkuuteen, mutta ota huomioon, että automaattiset käännökset saattavat sisältää virheitä tai epätarkkuuksia. Alkuperäistä asiakirjaa sen alkuperäiskielellä tulee pitää auktoriteettisena lähteenä. Tärkeässä tiedossa suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä aiheutuvista väärinymmärryksistä tai virhetulkoinneista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->