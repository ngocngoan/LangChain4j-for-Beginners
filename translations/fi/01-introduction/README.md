# Moduuli 01: LangChain4j:llä Aloittaminen

## Sisällysluettelo

- [Videokävely](../../../01-introduction)
- [Mitä Opit](../../../01-introduction)
- [Edellytykset](../../../01-introduction)
- [Ymmärtäen Keskeinen Ongelma](../../../01-introduction)
- [Ymmärtäen Tokenit](../../../01-introduction)
- [Miten Muisti Toimii](../../../01-introduction)
- [Miten Tämä Käyttää LangChain4j:ää](../../../01-introduction)
- [Azure OpenAI -infrastruktuurin Käyttöönotto](../../../01-introduction)
- [Sovelluksen Suorittaminen Paikallisesti](../../../01-introduction)
- [Sovelluksen Käyttö](../../../01-introduction)
  - [Tilaton Keskustelu (Vasen Paneeli)](../../../01-introduction)
  - [Tilallinen Keskustelu (Oikea Paneeli)](../../../01-introduction)
- [Seuraavat Askeleet](../../../01-introduction)

## Videokävely

Katso tämä live-sessio, joka selittää, miten aloitat tämän moduulin kanssa:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="LangChain4j:llä Aloittaminen - Live-sessio" width="800"/></a>

## Mitä Opit

Jos suoritat pika-alun, näit, miten lähettää kehotteita ja saada vastauksia. Se on perusta, mutta todelliset sovellukset tarvitsevat enemmän. Tämä moduuli opettaa sinut rakentamaan keskustelullisen tekoälyn, joka muistaa kontekstin ja ylläpitää tilaa – ero kertaluonteisen demon ja tuotantovalmiin sovelluksen välillä.

Käytämme tässä oppaassa Azure OpenAI:n GPT-5.2:ta koko ajan, koska sen edistyneet päättelykyvyt tekevät erilaisten kaavojen käyttäytymisen selvemmäksi. Kun lisäät muistin, näet eron selvästi. Tämä helpottaa ymmärtämään, mitä kukin komponentti tuo sovellukseesi.

Rakennat yhden sovelluksen, joka demonstroi molempia kaavoja:

**Tilaton Keskustelu** – Jokainen pyyntö on riippumaton. Mallilla ei ole muistia aiemmista viesteistä. Tätä kaavaa käytit pika-alussa.

**Tilallinen Keskustelu** – Jokainen pyyntö sisältää keskusteluhistorian. Malli ylläpitää kontekstia useiden vuorojen ajan. Tätä tuotantosovellukset vaativat.

## Edellytykset

- Azure-tilaus, jossa on Azure OpenAI -käyttöoikeus
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Huom:** Java, Maven, Azure CLI ja Azure Developer CLI (azd) ovat valmiiksi asennettuina mukana toimitetussa devcontainerissa.

> **Huom:** Tämä moduuli käyttää GPT-5.2:ta Azure OpenAI:ssa. Käyttöönotto konfiguroidaan automaattisesti komennolla `azd up` – älä muuta mallin nimeä koodissa.

## Ymmärtäen Keskeinen Ongelma

Kielimallit ovat tilattomia. Jokainen API-kutsu on erillinen. Jos sanot "Nimeni on John" ja sitten kysyt "Mikä nimeni on?", mallilla ei ole mitään tietoa, että juuri esittelit itsesi. Se käsittelee jokaisen pyynnön kuin se olisi ensimmäinen keskustelu koskaan.

Tämä toimii yksinkertaisessa kysymys-vastaus-tilanteessa, mutta on hyödytöntä oikeissa sovelluksissa. Asiakaspalvelubottien täytyy muistaa, mitä kerroit niille. Henkilökohtaisten avustajien täytyy ymmärtää kontekstia. Mikä tahansa monivuoropuhelu tarvitsee muistia.

<img src="../../../translated_images/fi/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Tilaton vs Tilallinen Keskustelu" width="800"/>

*Ero tilattoman (itsenäiset kutsut) ja tilallisen (kontekstitietoinen) keskustelun välillä*

## Ymmärtäen Tokenit

Ennen kuin sukellat keskusteluihin, on tärkeää ymmärtää tokenit – perusyksiköt, joita kielimallit käsittelevät tekstissä:

<img src="../../../translated_images/fi/token-explanation.c39760d8ec650181.webp" alt="Token Selitys" width="800"/>

*Esimerkki siitä, miten teksti pilkotaan tokeneiksi – "I love AI!" muodostuu 4 eri käsittelyyksiköksi*

Tokenit ovat tapa, jolla tekoälymallit mittaavat ja käsittelevät tekstiä. Sanat, välimerkit ja jopa välilyönnit voivat olla tokeneita. Mallillasi on raja, kuinka monta tokenia se voi käsitellä kerralla (GPT-5.2:ssa 400 000 tokenia, joista enintään 272 000 syöttötokenia ja 128 000 tulostokenia). Tokenien ymmärtäminen auttaa hallitsemaan keskustelun pituutta ja kustannuksia.

## Miten Muisti Toimii

Keskustelumuis­ti ratkaisee tilattomuuden ongelman ylläpitämällä keskusteluhistoriaa. Ennen kuin lähetät pyynnön mallille, kehys lisää mukaan relevantit aiemmat viestit. Kun kysyt "Mikä nimeni on?", järjestelmä oikeasti lähettää koko keskusteluhistorian, jolloin malli näkee, että sanoit aiemmin "Nimeni on John."

LangChain4j tarjoaa muistirakenteet, jotka hoitavat tämän automaattisesti. Voit valita, kuinka monta viestiä säilytät, ja kehys hallinnoi konteksti-ikkunaa.

<img src="../../../translated_images/fi/memory-window.bbe67f597eadabb3.webp" alt="Muisti-ikkunan Käsite" width="800"/>

*MessageWindowChatMemory ylläpitää liukuvaa ikkunaa uusimmista viesteistä, pudoten automaattisesti vanhat pois*

## Miten Tämä Käyttää LangChain4j:ää

Tämä moduuli laajentaa pika-aloitusta integroimalla Spring Bootin ja lisäämällä keskustelumui­stin. Näin osat sopivat yhteen:

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

**Keskustelumalli** – Konfiguroi Azure OpenAI Spring beanina ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Rakentaja lukee tunnistetiedot ympäristömuuttujista, jotka `azd up` asettaa. `baseUrl`-asetuksella omaan Azure-päätepisteeseen OpenAI-asiakas toimii Azuren kanssa.

**Keskustelumuis­ti** – Seuraa keskusteluhistoriaa MessageWindowChatMemoryllä ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Luo muisti `withMaxMessages(10)`-asetuksella, joka säilyttää viimeiset 10 viestiä. Lisää käyttäjän ja tekoälyn viestit tyyppisillä kääreillä: `UserMessage.from(text)` ja `AiMessage.from(text)`. Hae historia `memory.messages()` ja lähetä mallille. Palvelu tallentaa erilliset muistit kutakin keskustelu-ID:tä kohden, mahdollistaen useiden käyttäjien keskustelun yhtä aikaa.

> **🤖 Kokeile [GitHub Copilotin](https://github.com/features/copilot) Chatin kanssa:** Avaa [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ja kysy:
> - "Miten MessageWindowChatMemory päättää, mitkä viestit pudottaa, kun ikkuna on täysi?"
> - "Voinko toteuttaa oman muistivarastoinnin tietokantaa käyttäen muistissa?"
> - "Miten lisäisin yhteenvedon vanhan keskusteluhistorian puristamiseksi?"

Tilaton chat-päätepiste ohittaa muistin kokonaan – vain `chatModel.chat(prompt)` kuten pika-alussa. Tilallinen päätepiste lisää viestit muistiin, hakee historian ja sisällyttää sen jokaisen pyynnön kontekstiin. Sama mallikokoonpano, eri kaavat.

## Azure OpenAI -infrastruktuurin Käyttöönotto

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

> **Huom:** Jos saat aikakatkaisun virheen (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), suorita vain `azd up` uudelleen. Azure-resurssit voivat vielä olla käyttöönotossa taustalla, ja uudelleenyrittäminen sallii käyttöönoton valmistumisen, kun resurssit ovat valmiita.

Tämä tekee:
1. Asentaa Azure OpenAI -resurssin GPT-5.2- ja text-embedding-3-small-malleilla
2. Luoda automaattisesti `.env`-tiedoston projektin juureen tunnistetiedoilla
3. Määrittää kaikki tarvittavat ympäristömuuttujat

**Jos käyttöönotossa ongelmia:** Katso [Infrastructure README](infra/README.md) yksityiskohtaista vianetsintää varten, mukaan lukien aliverkkotunnuksen ristiriidat, manuaaliset Azure Portal -asennusvaiheet ja mallikonfiguraatio-ohjeet.

**Varmista käyttöönoton onnistuminen:**

**Bash:**
```bash
cat ../.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY jne.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY jne.
```

> **Huom:** `azd up` -komento generoi `.env`-tiedoston automaattisesti. Jos haluat päivittää sitä myöhemmin, voit joko muokata `.env`-tiedostoa manuaalisesti tai luoda sen uudelleen ajamalla:
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

## Sovelluksen Suorittaminen Paikallisesti

**Varmista käyttöönotto:**

Tarkista, että `.env`-tiedosto on juurihakemistossa Azure-tunnistetiedoilla:

**Bash:**
```bash
cat ../.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Käynnistä sovellukset:**

**Vaihtoehto 1: Spring Boot Dashboardin käyttäminen (suositeltu VS Code -käyttäjille)**

Dev container sisältää Spring Boot Dashboard -laajennuksen, joka tarjoaa visuaalisen käyttöliittymän kaikkien Spring Boot -sovellusten hallintaan. Löydät sen VS Coden vasemman laidan Activity Barista (etsi Spring Boot -ikonia).

Spring Boot Dashboardista voit:
- Näyttää kaikki työtilassa olevat Spring Boot -sovellukset
- Käynnistää/pysäyttää sovelluksia yhdellä napsautuksella
- Katsoa sovelluslokeja reaaliaikaisesti
- Valvoa sovellusten tilaa

Napsauta vain toistopainiketta "introduction"-kohdan vieressä käynnistääksesi tämän moduulin tai käynnistä kaikki moduulit kerralla.

<img src="../../../translated_images/fi/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Vaihtoehto 2: Shell-skriptien käyttäminen**

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

Molemmat skriptit lataavat automaattisesti ympäristömuuttujat juuren `.env`-tiedostosta ja rakentavat JAR-tiedostot, jos niitä ei ole olemassa.

> **Huom:** Jos haluat rakentaa kaikki moduulit manuaalisesti ennen käynnistämistä:
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

Avaa selainosoitteessa http://localhost:8080.

**Lopettaaksesi:**

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

## Sovelluksen Käyttö

Sovellus tarjoaa verkkokäyttöliittymän, jossa on kaksi eri chat-ratkaisua rinnakkain.

<img src="../../../translated_images/fi/home-screen.121a03206ab910c0.webp" alt="Sovelluksen Koti-näyttö" width="800"/>

*Dashboard, joka näyttää sekä Yksinkertaisen Chatin (tilaton) että Keskustelukeskustelun (tilallinen) vaihtoehdot*

### Tilaton Keskustelu (Vasen Paneeli)

Kokeile tätä ensin. Kysy "Nimeni on John" ja heti perään "Mikä nimeni on?" Malli ei muista, koska jokainen viesti on oma itsenäinen yksikkönsä. Tämä demonstroi perusongelman, joka liittyy pelkkään kielimallien integrointiin – ei keskustelukontekstia.

<img src="../../../translated_images/fi/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Tilaton Chat Demo" width="800"/>

*Tekoäly ei muista nimeäsi edellisestä viestistä*

### Tilallinen Keskustelu (Oikea Paneeli)

Kokeile nyt samaa sekvenssiä tässä. Kysy "Nimeni on John" ja sitten "Mikä nimeni on?" Tällä kertaa se muistaa. Erona on MessageWindowChatMemory – se ylläpitää keskusteluhistoriaa ja sisällyttää sen jokaisen pyynnön kontekstiin. Näin tuotantokäyttöön tarkoitettu keskustelullinen tekoäly toimii.

<img src="../../../translated_images/fi/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Tilallinen Chat Demo" width="800"/>

*Tekoäly muistaa nimesi aiemmasta keskustelusta*

Molemmat paneelit käyttävät samaa GPT-5.2-mallia. Ainoa ero on muisti. Tämä selkeyttää, mitä muisti tuo sovellukseesi ja miksi se on välttämätöntä todellisissa käyttötapauksissa.

## Seuraavat Askeleet

**Seuraava Moduuli:** [02-prompt-engineering - Kehota-suunnittelu GPT-5.2:lla](../02-prompt-engineering/README.md)

---

**Navigointi:** [← Edellinen: Moduuli 00 - Pika-Alku](../00-quick-start/README.md) | [Takaisin Pääsivulle](../README.md) | [Seuraava: Moduuli 02 - Kehota-suunnittelu →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:  
Tämä asiakirja on käännetty tekoälypohjaisella käännöspalvelulla [Co-op Translator](https://github.com/Azure/co-op-translator). Pyrimme tarkkuuteen, mutta automaattiset käännökset saattavat sisältää virheitä tai epätarkkuuksia. Alkuperäistä asiakirjaa sen alkuperäisellä kielellä tulee pitää auktoritatiivisena lähteenä. Tärkeissä tiedoissa suosittelemme ammattimaisen ihmiskääntäjän käyttöä. Emme vastaa tämän käännöksen käytöstä mahdollisesti aiheutuvista väärinymmärryksistä tai virhetulkinnasta.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->