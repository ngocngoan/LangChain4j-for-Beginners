# Moduuli 04: AI-agentit työkaluilla

## Sisällysluettelo

- [Videoesittely](../../../04-tools)
- [Mitä opit](../../../04-tools)
- [Esivaatimukset](../../../04-tools)
- [Ymmärtäminen: AI-agentit työkaluilla](../../../04-tools)
- [Kuinka työkalukutsut toimivat](../../../04-tools)
  - [Työkalujen määrittelyt](../../../04-tools)
  - [Päätöksenteko](../../../04-tools)
  - [Suoritus](../../../04-tools)
  - [Vastauksen luominen](../../../04-tools)
  - [Arkkitehtuuri: Spring Boot automaattijohtaminen](../../../04-tools)
- [Työkaluketjutus](../../../04-tools)
- [Sovelluksen käynnistäminen](../../../04-tools)
- [Sovelluksen käyttö](../../../04-tools)
  - [Kokeile yksinkertaista työkalun käyttöä](../../../04-tools)
  - [Testaa työkaluketjutusta](../../../04-tools)
  - [Katso keskustelun kulku](../../../04-tools)
  - [Kokeile erilaisia pyyntöjä](../../../04-tools)
- [Keskeiset käsitteet](../../../04-tools)
  - [ReAct-malli (järjestelmällinen ajattelu ja toiminta)](../../../04-tools)
  - [Työkalujen kuvaukset ovat tärkeitä](../../../04-tools)
  - [Istunnon hallinta](../../../04-tools)
  - [Virheenkäsittely](../../../04-tools)
- [Saatavilla olevat työkalut](../../../04-tools)
- [Milloin käyttää työkalupohjaisia agenteja](../../../04-tools)
- [Työkalut vs RAG](../../../04-tools)
- [Seuraavat askeleet](../../../04-tools)

## Videoesittely

Katso tämä suora sessio, joka selittää, kuinka aloittaa tämän moduulin kanssa:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="AI Agents with Tools and MCP - Live Session" width="800"/></a>

## Mitä opit

Tähän mennessä olet oppinut käymään keskusteluja tekoälyn kanssa, rakentamaan kehotteet tehokkaasti ja perustamaan vastaukset asiakirjoihisi. Mutta on vielä yksi perustavanlaatuinen rajoitus: kielimallit pystyvät tuottamaan vain tekstiä. Ne eivät voi tarkistaa säätä, suorittaa laskelmia, hakea tietokannoista tai olla vuorovaikutuksessa ulkoisten järjestelmien kanssa.

Työkalut muuttavat tämän. Antamalla mallille pääsyn funktioihin, joita se voi kutsua, muutat sen tekstintuottajasta agentiksi, joka voi toimia. Malli päättää, milloin se tarvitsee työkalua, mitä työkalua käyttää ja mitä parametreja antaa. Koodisi suorittaa funktion ja palauttaa tuloksen. Malli sisällyttää tämän tuloksen vastaukseensa.

## Esivaatimukset

- Suoritettu [Moduuli 01 - Johdanto](../01-introduction/README.md) (Azure OpenAI -resurssit otettu käyttöön)
- Suositellaan suoritettuja aiempia moduuleja (tässä moduulissa viitataan [RAG-käsitteisiin Moduulista 03](../03-rag/README.md) vertailussa Tools vs RAG)
- `.env`-tiedosto juurihakemistossa Azure-tunnuksilla (luotu `azd up` -komennolla Moduulissa 01)

> **Huom:** Jos et ole suorittanut Moduulia 01, seuraa siellä annettuja käyttöönotto-ohjeita ensin.

## Ymmärtäminen: AI-agentit työkaluilla

> **📝 Huom:** Tässä moduulissa termi "agentit" tarkoittaa työkaluja kutsuvilla ominaisuuksilla vahvistettuja tekoälyavustajia. Tämä eroaa **Agentic AI** -malleista (autonomiset agentit, joilla on suunnittelu, muisti ja monivaiheinen päättely), joita käsittelemme [Moduulissa 05: MCP](../05-mcp/README.md).

Ilman työkaluja kielimalli voi luoda tekstiä vain koulutusdatastaan. Kysy säästä ja sen täytyy arvata. Anna työkalut, niin se voi kutsua sää-API:ta, tehdä laskutoimituksia tai hakea tietokannasta — ja kutoa nämä todelliset tulokset vastaukseensa.

<img src="../../../translated_images/fi/what-are-tools.724e468fc4de64da.webp" alt="Ilman työkaluja vs työkalujen kanssa" width="800"/>

*Ilman työkaluja malli vain arvaa — työkaluilla se voi kutsua API:ita, suorittaa laskelmia ja palauttaa reaaliaikaista tietoa.*

AI-agentti työkaluilla noudattaa **ReAct** (Reasoning and Acting) -mallia. Malli ei vain vastaa — se miettii, mitä tarvitsee, toimii kutsumalla työkalua, havaitsee tuloksen ja päättää sitten, toimiiko uudestaan vai antaa lopullisen vastauksen:

1. **Päättele** — Agentti analysoi käyttäjän kysymyksen ja määrittää tarvittavat tiedot
2. **Toimi** — Agentti valitsee oikean työkalun, luo oikeat parametrit ja kutsuu sitä
3. **Havaitse** — Agentti vastaanottaa työkalun tuloksen ja arvioi sen
4. **Toista tai Vastaa** — Jos tarvitaan lisää tietoa, agentti kiertää uudestaan; muuten muodostaa luonnollisen kielen vastauksen

<img src="../../../translated_images/fi/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*ReAct-sykli — agentti päättää mitä tehdä, toimii kutsumalla työkalua, havaitsee tuloksen ja toistaa kunnes voi antaa lopullisen vastauksen.*

Tämä tapahtuu automaattisesti. Määrittelet työkalut ja niiden kuvaukset. Malli hoitaa päätöksenteon siitä, milloin ja miten niitä käyttää.

## Kuinka työkalukutsut toimivat

### Työkalujen määrittelyt

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Määrittelet funktiot selkeine kuvauksineen ja parametri-spesifikaatioineen. Malli näkee nämä kuvaukset järjestelmäkehotteessaan ja ymmärtää, mitä kukin työkalu tekee.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Säähautasi logiikka
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Avustaja kytketään automaattisesti Spring Bootin avulla:
// - ChatModel-palvelu
// - Kaikki @Tool-menetelmät @Component-luokista
// - ChatMemoryProvider istunnon hallintaan
```
  
Alla oleva kaavio purkaa jokaisen annotaation ja näyttää, miten jokainen osa auttaa tekoälyä ymmärtämään, milloin kutsua työkalua ja mitä argumentteja antaa:

<img src="../../../translated_images/fi/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Työkalumäärittelyn rakenne" width="800"/>

*Työkalumäärittelyn rakenne — @Tool kertoo tekoälylle, milloin käyttää työkalua, @P kuvaa jokaisen parametrin ja @AiService kytkee kaiken yhteen käynnistyksen yhteydessä.*

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatin kanssa:** Avaa [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) ja kysy:  
> - "Miten integroidaan oikea säähallinta-API, kuten OpenWeatherMap, testidatan sijaan?"  
> - "Mikä tekee hyvästä työkalukuvauksesta, joka auttaa tekoälyä käyttämään työkalua oikein?"  
> - "Miten käsitellään API-virheitä ja käyttörajoituksia työkalujen toteutuksissa?"

### Päätöksenteko

Kun käyttäjä kysyy "Mikä on sää Seattlella?", malli ei valitse työkalua sattumalta. Se vertaa käyttäjän aikomusta jokaiseen käytettävissä olevaan työkalukuvauskseen, pisteyttää ne osuvuuden mukaan ja valitsee parhaan. Se luo sitten rakenteellisen funktiokutsun oikeilla parametreilla — tässä tapauksessa asettaa `location` arvoksi `"Seattle"`.

Jos mikään työkalu ei vastaa käyttäjän pyyntöä, malli vastaa omien tietojensa perusteella. Jos useampi työkalu sopii, se valitsee tarkimman.

<img src="../../../translated_images/fi/decision-making.409cd562e5cecc49.webp" alt="Kuinka tekoäly päättää mitä työkalua käyttää" width="800"/>

*Malli arvioi jokaisen saatavilla olevan työkalun käyttäjän aikomukseen nähden ja valitsee parhaan — siksi selkeiden ja täsmällisten työkalukuvauksien kirjoittaminen on tärkeää.*

### Suoritus

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot liittää automaattisesti `@AiService` -rajapinnan kaikkiin rekisteröityihin työkaluihin, ja LangChain4j suorittaa työkalukutsut automaattisesti. Taustalla täydellinen työkalukutsu kulkee kuuden vaiheen läpi — käyttäjän luonnollisen kielen kysymyksestä aina takaisin luonnollisen kielen vastaukseen:

<img src="../../../translated_images/fi/tool-calling-flow.8601941b0ca041e6.webp" alt="Työkalukutsun kulku" width="800"/>

*Loppuun asti kulkeva prosessi — käyttäjä kysyy, malli valitsee työkalun, LangChain4j suorittaa sen ja malli kytkee tuloksen luonnolliseen vastaukseen.*

Jos olet ajanut [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) Moduulissa 00, olet nähnyt saman mallin toiminnassa — `Calculator`-työkaluja kutsuttiin samalla tavalla. Alla oleva sekvenssikaavio näyttää tarkalleen, mitä demon aikana tapahtui:

<img src="../../../translated_images/fi/tool-calling-sequence.94802f406ca26278.webp" alt="Työkalukutsun sekvenssikaavio" width="800"/>

*Työkalukutsun silmukka Quick Start -demosta — `AiServices` lähettää viestisi ja työkalujen skeemat LLM:lle, LLM vastaa funktiokutsulla kuten `add(42, 58)`, LangChain4j suorittaa `Calculator`-metodin paikallisesti ja syöttää tuloksen takaisin lopullista vastausta varten.*

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatin kanssa:** Avaa [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) ja kysy:  
> - "Miten ReAct-malli toimii ja miksi se on tehokas AI-agenteille?"  
> - "Miten agentti päättää, mitä työkalua käyttää ja missä järjestyksessä?"  
> - "Mitä tapahtuu, jos työkalun suoritus epäonnistuu — miten virheiden käsittely tulisi toteuttaa luotettavasti?"

### Vastauksen luominen

Malli vastaanottaa säädatan ja muotoilee siitä käyttäjälle luonnollisen kielen vastauksen.

### Arkkitehtuuri: Spring Boot automaattijohtaminen

Tämä moduuli käyttää LangChain4j:n Spring Boot -integraatiota deklaratiivisten `@AiService` -rajapintojen kanssa. Käynnistyksen yhteydessä Spring Boot löytää kaikki `@Component`it, jotka sisältävät `@Tool`-metodeja, ChatModel-beanin ja ChatMemoryProviderin — ja liittää ne kaikki yhdeksi `Assistant`-rajapinnaksi ilman boilerplate-koodia.

<img src="../../../translated_images/fi/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot automaattijohtamisen arkkitehtuuri" width="800"/>

*@AiService-rajapinta sitoo yhteen ChatModelin, työkalukomponentit ja muistinhallinnan — Spring Boot hoitaa kaiken liitännän automaattisesti.*

Tässä koko pyyntöjen elinkaari sekvenssikaaviona — HTTP-pyynnöstä ohjaimeen, palveluun, automaattijohtimeen, työkalukutsuun ja takaisin:

<img src="../../../translated_images/fi/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Spring Boot työkalukutsun sekvenssi" width="800"/>

*Koko Spring Boot -pyynnön elinkaari — HTTP-pyyntö kulkee ohjaimen ja palvelun kautta automaattijohtoisen Assistant-proxyn läpi, joka orkestroi LLM:n ja työkalukutsut automaattisesti.*

Tämän lähestymistavan keskeiset edut:

- **Spring Boot automaattijohtaminen** — ChatModel ja työkalut injektoidaan automaattisesti
- **@MemoryId-malli** — Istuntopohjainen automaattinen muistinhallinta
- **Yksi instanssi** — Assistant luodaan kerran ja käytetään uudestaan suorituskyvyn parantamiseksi
- **Tyyppiturvallinen suoritus** — Java-metodit kutsutaan suoraan tyypinmuunnoksella
- **Monivuoroinen orkestrointi** — Hallitsee työkaluketjutuksen automaattisesti
- **Ei boilerplate-koodia** — Ei käsin tehtäviä `AiServices.builder()` -kutsuja tai muistihashtabeja

Vaihtoehtoiset lähestymistavat (käsin tehtävät `AiServices.builder()` -kutsut) vaativat enemmän koodia ja eivät hyödynnä Spring Boot -integraation etuja.

## Työkaluketjutus

**Työkaluketjutus** — Työkalupohjaisten agenttien todellinen voima näkyy, kun yksittäinen kysymys vaatii useita työkaluja. Kysy "Mikä on Seattlelän sää fahrenheit-asteina?" ja agentti ketjuttaa automaattisesti kaksi työkalua: ensin se kutsuu `getCurrentWeather` saadakseen lämpötilan celsiusasteina, sitten siirtää arvon `celsiusToFahrenheit`-työkalulle muuntamista varten — kaikki yhdessä keskusteluvuorossa.

<img src="../../../translated_images/fi/tool-chaining-example.538203e73d09dd82.webp" alt="Työkaluketjutuksen esimerkki" width="800"/>

*Työkaluketjutus toiminnassa — agentti kutsuu ensin getCurrentWeather, sitten ohjaa celsius-tuloksen celsiusToFahrenheit-työkalulle ja tarjoaa yhdistetyn vastauksen.*

**Sulava virheiden käsittely** — Kysy sää jossain kaupungissa, jota ei ole testidatassa. Työkalu palauttaa virheilmoituksen ja tekoäly selittää, ettei voi auttaa sen sijaan, että kaatuisi. Työkalut käsittelevät virheensä turvallisesti. Alla oleva kaavio vertaa kahta lähestymistapaa — asianmukaisella virheenkäsittelyllä agentti sieppaa poikkeuksen ja vastaa avuliaasti, ilman sitä koko sovellus kaatuu:

<img src="../../../translated_images/fi/error-handling-flow.9a330ffc8ee0475c.webp" alt="Virheenkäsittelyn kulku" width="800"/>

*Kun työkalu epäonnistuu, agentti sieppaa virheen ja vastaa avuliaalla selityksellä kaatumisen sijaan.*

Tämä tapahtuu yhdellä keskustelukierroksella. Agentti orkestroi useita työkalukutsuja itsenäisesti.

## Sovelluksen käynnistäminen

**Tarkista käyttöönotto:**

Varmista, että `.env`-tiedosto on juurihakemistossa ja sisältää Azure-tunnukset (luotu Moduulin 01 aikana). Aja komennot moduulihakemistosta (`04-tools/`):

**Bash:**  
```bash
cat ../.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**PowerShell:**  
```powershell
Get-Content ..\.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Käynnistä sovellus:**

> **Huom:** Jos olet jo käynnistänyt kaikki sovellukset `./start-all.sh` -skriptillä juurihakemistosta (kuten Moduulin 01 ohjeissa), tämä moduuli on jo käynnissä portissa 8084. Voit ohittaa alla olevat käynnistyskomennot ja mennä suoraan osoitteeseen http://localhost:8084.

**Vaihtoehto 1: Spring Boot Dashboardin käyttö (suositeltu VS Code -käyttäjille)**

Kehityskontti sisältää Spring Boot Dashboard -laajennuksen, joka tarjoaa visuaalisen käyttöliittymän kaikkien Spring Boot -sovellusten hallintaan. Löydät sen VS Coden vasemman laidan Activity Bar -palkista (etsi Spring Boot -kuvake).

Spring Boot Dashboardista voit:  
- Näyttää kaikki käytettävissä olevat Spring Boot -sovellukset työtilassa  
- Käynnistää/pysäyttää sovellukset yhdellä napsautuksella  
- Katsoa sovelluslokeja reaaliaikaisesti  
- Monitoroida sovelluksen tilaa  
Aloita tämä moduuli yksinkertaisesti napsauttamalla "tools"-kohdan vieressä olevaa toistopainiketta tai käynnistä kaikki moduulit kerralla.

Tältä Spring Boot Dashboard näyttää VS Codessa:

<img src="../../../translated_images/fi/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard VS Codessa — käynnistä, pysäytä ja valvo kaikkia moduuleja yhdestä paikasta*

**Vaihtoehto 2: Shell-komentosarjojen käyttö**

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
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

Molemmat skriptit lataavat automaattisesti ympäristömuuttujat juurikansion `.env`-tiedostosta ja rakentavat JAR-tiedostot, jos ne eivät ole olemassa.

> **Huom:** Jos haluat rakentaa kaikki moduulit manuaalisesti ennen käynnistämistä:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Avaa selaimessa osoite http://localhost:8084.

**Pysäyttääksesi:**

**Bash:**
```bash
./stop.sh  # Tämä moduuli vain
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

Sovellus tarjoaa selainkäyttöliittymän, jonka kautta voit olla vuorovaikutuksessa tekoälyagentin kanssa, jolla on pääsy sää- ja lämpötilamuuntotyökaluihin. Tältä käyttöliittymä näyttää — se sisältää pikaesimerkit ja chat-paneelin pyyntöjen lähettämiseen:

<a href="images/tools-homepage.png"><img src="../../../translated_images/fi/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI Agent Tools -käyttöliittymä - pikaesimerkkejä ja chat-käyttöliittymä työkalujen käyttöön*

### Kokeile yksinkertaista työkalun käyttöä

Aloita suoraviivaisella pyynnöllä: "Muuta 100 astetta Fahrenheitista Celsiukseksi". Agentti tunnistaa, että tarvitsee lämpötilamuuntotyökalun, kutsuu sitä oikeilla parametreilla ja palauttaa tuloksen. Huomaa, kuinka luonnolliselta tämä tuntuu — et määritellyt, mitä työkalua käyttää tai miten sitä kutsutaan.

### Testaa työkalujen ketjutusta

Kokeile nyt jotain monimutkaisempaa: "Mikä on sää Seattlessa ja muuta se Fahrenheit-asteiksi?" Katso, kuinka agentti toimii tämän läpi vaiheittain. Se hakee ensin sään (joka palauttaa Celsius-asteet), tunnistaa, että tarvitsee muuntaa Fahrenheitiksi, kutsuu muunnostyökalua ja yhdistää molemmat tulokset yhdeksi vastaukseksi.

### Katso keskustelun kulku

Chat-käyttöliittymä säilyttää keskusteluhistorian, jolloin voit käydä monivuorovaikutteisia keskusteluja. Näet kaikki aiemmat kyselyt ja vastaukset, mikä helpottaa keskustelun seuraamista ja ymmärtämään, miten agentti rakentaa kontekstia useiden vaihdosten aikana.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/fi/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Monivuoroinen keskustelu, jossa tehdään yksinkertaisia muunnoksia, säähaut ja työkaluketjutuksia*

### Kokeile erilaisia pyyntöjä

Kokeile erilaisia yhdistelmiä:
- Säätiedotukset: "Mikä on sää Tokiossa?"
- Lämpötilamuunnokset: "Mikä on 25°C Kelvineinä?"
- Yhdistetyt kyselyt: "Tarkista sää Pariisissa ja kerro, onko lämpötila yli 20°C"

Huomaa, kuinka agentti tulkitsee luonnollista kieltä ja mapittaa sen sopiviksi työkalukutsuiksi.

## Keskeiset käsitteet

### ReAct-malli (Päättely ja toiminta)

Agentti vuorottelee päättelyn (päätös, mitä tehdä) ja toimimisen (työkalujen käyttö) välillä. Tämä malli mahdollistaa autonomisen ongelmanratkaisun pelkän ohjeiden seuraamisen sijaan.

### Työkalujen kuvauksilla on merkitystä

Työkalukuvauksesi laatu vaikuttaa suoraan siihen, kuinka hyvin agentti osaa käyttää niitä. Selkeät ja täsmälliset kuvaukset auttavat mallia ymmärtämään, milloin ja miten kutakin työkalua kutsutaan.

### Istunnon hallinta

`@MemoryId`-annotaatio mahdollistaa automaattisen istuntokohtaisen muistinhallinnan. Jokainen istuntotunnus saa oman `ChatMemory`-instanssin, jota hallinnoi `ChatMemoryProvider`-bean, joten useat käyttäjät voivat olla vuorovaikutuksessa agentin kanssa samanaikaisesti ilman, että keskustelut sekoittuvat. Seuraava kaavio näyttää, miten useat käyttäjät ohjataan erillisiin muistivarastoihin istuntotunnustensa perusteella:

<img src="../../../translated_images/fi/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Jokainen istuntotunnus vastaa erillistä keskusteluhistoriaa — käyttäjät eivät näe toistensa viestejä.*

### Virheenkäsittely

Työkalut voivat epäonnistua — API:t aikakatkaisevat, parametrit voivat olla virheellisiä, ulkoiset palvelut voivat olla alas. Tuotantoagentit tarvitsevat virheenkäsittelyn, jotta malli voi selittää ongelmat tai yrittää vaihtoehtoja sen sijaan, että koko sovellus kaatuu. Kun työkalu heittää poikkeuksen, LangChain4j sieppaa sen ja välittää virheilmoituksen takaisin mallille, joka voi sitten selittää ongelman luonnollisella kielellä.

## Saatavilla olevat työkalut

Alla oleva kaavio näyttää laajan työkaluekosysteemin, jonka voit rakentaa. Tämä moduuli demonstroi sää- ja lämpötilatyökaluja, mutta sama `@Tool`-malli toimii minkä tahansa Java-metodin kanssa — tietokantakyselyistä maksuliikenteeseen.

<img src="../../../translated_images/fi/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Mikä tahansa `@Tool`-annotaatiolla varustettu Java-metodi tulee saataville tekoälylle — malli laajenee tietokantoihin, API:ihin, sähköposteihin, tiedostotoimintoihin ja muuhun.*

## Milloin käyttää työkalupohjaisia agenteja

Kaikki pyynnöt eivät tarvitse työkaluja. Päätös perustuu siihen, tarvitseeko tekoäly olla vuorovaikutuksessa ulkoisten järjestelmien kanssa vai voiko se vastata omasta tiedostaan. Seuraava opas tiivistää, milloin työkalut tuovat lisäarvoa ja milloin ne ovat tarpeettomia:

<img src="../../../translated_images/fi/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*Nopea päätösopas — työkalut ovat reaaliaikaisiin tietoihin, laskelmiin ja toimintoihin; yleistä tietoa ja luovia tehtäviä varten niitä ei tarvita.*

## Työkalut vs RAG

Moduulit 03 ja 04 laajentavat tekoälyn kykyjä, mutta pohjimmiltaan eri tavoin. RAG antaa mallille pääsyn **tietoon** hakemalla dokumentteja. Työkalut antavat mallille kyvyn suorittaa **toimintoja** kutsumalla funktioita. Alla oleva kaavio vertaa näitä kahta lähestymistapaa rinnakkain — miten kukin työnkulku toimii ja mitkä ovat niiden kompromissit:

<img src="../../../translated_images/fi/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG hakee tietoa staattisista dokumenteista — Työkalut suorittavat toimia ja hakevat dynaamista, reaaliaikaista dataa. Monet tuotantojärjestelmät yhdistävät molemmat.*

Käytännössä monet tuotantojärjestelmät yhdistävät molemmat lähestymistavat: RAG vahvistamaan vastaukset dokumentaatiollasi ja Työkalut live-datan hakemiseen tai operaatioiden suorittamiseen.

## Seuraavat askeleet

**Seuraava moduuli:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigointi:** [← Edellinen: Moduuli 03 - RAG](../03-rag/README.md) | [Takaisin pääsivulle](../README.md) | [Seuraava: Moduuli 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty tekoälypohjaisella käännöspalvelulla [Co-op Translator](https://github.com/Azure/co-op-translator). Pyrimme tarkkuuteen, mutta ota huomioon, että automaattiset käännökset saattavat sisältää virheitä tai epätarkkuuksia. Alkuperäinen asiakirja sen omalla kielellä tulee katsoa viralliseksi lähteeksi. Tärkeissä asioissa suositellaan ammattimaista ihmiskääntäjää. Emme ole vastuussa tämän käännöksen käytöstä johtuvista väärinymmärryksistä tai virhetulkintojen seurauksista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->