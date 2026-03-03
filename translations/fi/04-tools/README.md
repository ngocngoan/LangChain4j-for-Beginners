# Module 04: Työkalujen kanssa toimivat tekoälyagentit

## Sisällysluettelo

- [Mitä Opit](../../../04-tools)
- [Esivaatimukset](../../../04-tools)
- [Tekoälyagenttien ymmärtäminen työkalujen avulla](../../../04-tools)
- [Kuinka työkalujen kutsuminen toimii](../../../04-tools)
  - [Työkalujen määritelmät](../../../04-tools)
  - [Päätöksenteko](../../../04-tools)
  - [Suoritus](../../../04-tools)
  - [Vastauksen luominen](../../../04-tools)
  - [Arkkitehtuuri: Spring Bootin automaattinen yhdistäminen](../../../04-tools)
- [Työkaluketjutus](../../../04-tools)
- [Sovelluksen käynnistäminen](../../../04-tools)
- [Sovelluksen käyttö](../../../04-tools)
  - [Kokeile yksinkertaista työkalun käyttöä](../../../04-tools)
  - [Testaa työkaluketjutusta](../../../04-tools)
  - [Katso keskustelun kulku](../../../04-tools)
  - [Kokeile erilaisia pyyntöjä](../../../04-tools)
- [Keskeiset käsitteet](../../../04-tools)
  - [ReAct-malli (Päättely ja Toiminta)](../../../04-tools)
  - [Tärkeitä ovat työkalujen kuvaukset](../../../04-tools)
  - [Istunnon hallinta](../../../04-tools)
  - [Virheiden käsittely](../../../04-tools)
- [Saatavilla olevat työkalut](../../../04-tools)
- [Milloin käyttää työkalupohjaisia agentteja](../../../04-tools)
- [Työkalut vs RAG](../../../04-tools)
- [Seuraavat askeleet](../../../04-tools)

## Mitä Opit

Tähän asti olet oppinut käymään keskusteluja tekoälyn kanssa, rakentamaan kehotteet tehokkaasti ja perustamaan vastaukset dokumentteihisi. Mutta yhä on perusrajoitus: kielimallit voivat vain luoda tekstiä. Ne eivät voi tarkistaa säätä, suorittaa laskelmia, kysellä tietokantoja tai olla vuorovaikutuksessa ulkoisten järjestelmien kanssa.

Työkalut muuttavat tätä tilannetta. Antamalla mallille pääsyn kutsuttaviin toimintoihin, muutat sen tekstinluojasta agentiksi, joka voi suorittaa toimenpiteitä. Malli päättää, milloin se tarvitsee työkalua, mitä työkalua käyttää ja mitkä parametrit antaa. Koodisi suorittaa funktion ja palauttaa tuloksen. Malli sisällyttää tuloksen vastaukseensa.

## Esivaatimukset

- Suorittanut [Moduuli 01 - Johdanto](../01-introduction/README.md) (Azure OpenAI -resurssit otettu käyttöön)
- Suositellaan edellisten moduulien suorittamista (tässä moduulissa viitataan [RAG-käsitteisiin Moduulista 03](../03-rag/README.md) työkaluja vertaillessa)
- `.env`-tiedosto juurihakemistossa Azure-tunnuksilla (luotu `azd up` -komennolla Moduulissa 01)

> **Huom:** Jos et ole suorittanut Moduulia 01, noudata ensin siellä annettuja käyttöönotto-ohjeita.

## Tekoälyagenttien ymmärrys työkalujen avulla

> **📝 Huom:** Tässä moduulissa termi "agentit" tarkoittaa tekoälyavustajia, jotka on parannettu työkalujen kutsumismahdollisuuksilla. Tämä eroaa **Agenttipohjaisesta tekoälystä** (autonomiset agentit, joissa on suunnittelu, muisti ja monivaiheinen päättely), joita käsittelemme [Moduulissa 05: MCP](../05-mcp/README.md).

Ilman työkaluja kielimalli voi vain tuottaa tekstiä koulutusdatansa pohjalta. Kysy siltä nykyistä säätä, niin sen täytyy arvata. Anna sille työkalut, ja se voi kutsua sää-API:n, suorittaa laskelmia tai kysellä tietokantaa — ja kietoa ne todelliset tulokset vastaukseensa.

<img src="../../../translated_images/fi/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*Ilman työkaluja malli voi vain arvata — työkaluilla se voi kutsua API:ja, suorittaa laskelmia ja palauttaa reaaliaikaista tietoa.*

Työkalujen kanssa toimiva tekoälyagentti seuraa **Reasoning and Acting (ReAct)** -mallia. Malli ei pelkästään vastaa — se miettii, mitä tarvitsee, toimii kutsumalla työkalua, havainnoi tuloksen ja päättää, toimiiko uudelleen vai antaa lopullisen vastauksen:

1. **Päättely** — Agentti analysoi käyttäjän kysymyksen ja määrittää, mitä tietoa se tarvitsee
2. **Toiminta** — Agentti valitsee oikean työkalun, luo oikeat parametrit ja kutsuu sen
3. **Havainnointi** — Agentti vastaanottaa työkalun tuloksen ja arvioi sen
4. **Toisto tai vastaus** — Jos tarvitaan lisää tietoa, agentti toistaa; muuten se muodostaa luonnollisen kielen vastauksen

<img src="../../../translated_images/fi/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*ReAct-sykli — agentti päättelyttää, mitä tehdä, toimii kutsumalla työkalua, havainnoi tuloksen ja toistaa, kunnes voi antaa lopullisen vastauksen.*

Tämä tapahtuu automaattisesti. Määrittelet työkalut ja niiden kuvaukset. Malli hoitaa päätöksenteon siitä, milloin ja miten niitä käytetään.

## Kuinka työkalujen kutsuminen toimii

### Työkalujen määritelmät

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Määrittelet funktiot selkeillä kuvauksilla ja parametrien spesifikaatioilla. Malli näkee nämä kuvaukset järjestelmäkehotteessaan ja ymmärtää, mitä kukin työkalu tekee.

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

// Assistentti on automaattisesti kytketty Spring Bootilla seuraaviin:
// - ChatModel bean
// - Kaikki @Tool-metodit @Component-luokista
// - ChatMemoryProvider istunnon hallintaan
```

Alla oleva kaavio purkaa jokaisen annotaation ja näyttää, miten kukin osa auttaa tekoälyä ymmärtämään, milloin työkalu kutsutaan ja mitä argumentteja annetaan:

<img src="../../../translated_images/fi/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*Työkalumääritelmän anatomia — @Tool kertoo tekoälylle, milloin työkalu käytetään, @P kuvaa jokaisen parametrin ja @AiService yhdistää kaiken käynnistyksen yhteydessä.*

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatilla:** Avaa [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) ja kysy:
> - "Miten integroisin oikean säätiedon API:n, kuten OpenWeatherMap, mallin sijaan?"
> - "Mikä tekee hyvästä työkalukuvauksesta, joka auttaa tekoälyä käyttämään sitä oikein?"
> - "Miten käsittelen API-virheitä ja käyttörajoituksia työkalujen toteutuksissa?"

### Päätöksenteko

Kun käyttäjä kysyy "Mikä on sää Seattlella?", malli ei valitse työkalua sattumanvaraisesti. Se vertaa käyttäjän tarkoitusta jokaiseen työkaluun, arvioi ne merkityksen perusteella ja valitsee parhaan vastaavuuden. Se sitten muodostaa rakenteellisen funktiokutsun oikeilla parametreilla — tässä tapauksessa asettaa `location` arvoksi `"Seattle"`.

Jos mikään työkalu ei sovi käyttäjän pyyntöön, malli vastaa omien tietojensa pohjalta. Jos useampi työkalu soveltuu, se valitsee spesifisemmän.

<img src="../../../translated_images/fi/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*Malli arvioi kaikki käytettävissä olevat työkalut käyttäjän tarkoituksen perusteella ja valitsee parhaan — siksi on tärkeää kirjoittaa selkeät, tarkat työkalukuvaukset.*

### Suoritus

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot yhdistää automaattisesti deklaratiivisen `@AiService`-rajapinnan kaikilla rekisteröidyillä työkaluilla, ja LangChain4j suorittaa työkalukutsut automaattisesti. Kulissien takana täydellinen työkalukutsu kulkee kuuden vaiheen läpi — käyttäjän luonnollisesta kielikysymyksestä luonnollisen kielen vastaukseen asti:

<img src="../../../translated_images/fi/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*Kokonaisvirtaus — käyttäjä kysyy, malli valitsee työkalun, LangChain4j suorittaa sen, ja malli kietoo tuloksen luonnolliseen vastaukseen.*

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatilla:** Avaa [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) ja kysy:
> - "Miten ReAct-malli toimii ja miksi se on tehokas tekoälyagenteille?"
> - "Miten agentti päättää, mitä työkalua käyttää ja missä järjestyksessä?"
> - "Mitä tapahtuu, jos työkalun suoritus epäonnistuu — miten käsittelen virheitä luotettavasti?"

### Vastauksen luominen

Malli vastaanottaa säätiedon ja muotoilee sen käyttäjälle luonnollisen kielen vastaukseksi.

### Arkkitehtuuri: Spring Bootin automaattinen yhdistäminen

Tämä moduuli käyttää LangChain4j:n Spring Boot -integraatiota deklaratiivisten `@AiService`-rajapintojen kanssa. Käynnistyksen yhteydessä Spring Boot löytää jokaisen `@Component`:in, jossa on `@Tool`-metodeja, ChatModel-pavun ja ChatMemoryProviderin — ja yhdistää ne kaikki yhdeksi `Assistant`-rajapinnaksi ilman tarpeetonta koodia.

<img src="../../../translated_images/fi/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*@AiService-rajapinta yhdistää ChatModelin, työkalukomponentit ja muistipalvelun — Spring Boot hoitaa kaiken yhdistämisen automaattisesti.*

Keskeiset hyödyt:

- **Spring Boot -automaattiyhdistäminen** — ChatModel ja työkalut syötetään automaattisesti
- **@MemoryId-malli** — Istuntokohtainen muistinhallinta automaattisesti
- **Yksi instanssi** — Assistant luodaan kerran ja käytetään uudelleen paremman suorituskyvyn vuoksi
- **Tyyppiturvallinen suoritus** — Java-metodit kutsutaan suoraan tyyppimuunnoksin
- **Monikertaiset vuorot** — Käsittelee työkaluketjutuksen automaattisesti
- **Nolla boilerplate-koodia** — Ei manuaalisia `AiServices.builder()`-kutsuja tai muistihakemistoja

Vaihtoehtoiset tavat (manuaalinen `AiServices.builder()`) vaativat enemmän koodia eivätkä hyödynnä Spring Bootin integraatiota.

## Työkaluketjutus

**Työkaluketjutus** — Työkalupohjaisten agenttien todellinen voima näkyy, kun yksittäinen kysymys vaatii useita työkaluja. Kysy "Mikä on sää Seattlella Fahrenheit-asteina?" ja agentti ketjuttaa automaattisesti kaksi työkalua: ensin se kutsuu `getCurrentWeather` -metodia saadakseen lämpötilan celsiusasteina, sitten se välittää arvon `celsiusToFahrenheit` -työkalulle muunnosta varten — kaikki yhdessä keskusteluvuorossa.

<img src="../../../translated_images/fi/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*Työkaluketjutus käytännössä — agentti kutsuu ensin getCurrentWeatherin, sitten syöttää Celsius-tuloksen celsiusToFahrenheitiin ja antaa yhdistetyn vastauksen.*

**Sulava virheenkäsittely** — Kysy säätä kaupungissa, joka ei ole esimerkkidatassa. Työkalu palauttaa virheilmoituksen, ja tekoäly selittää, ettei voi auttaa sen sijaan että kaatuisi. Työkalut epäonnistuvat turvallisesti. Alla oleva kaavio vertailee kahta lähestymistapaa — kun virheenkäsittely on kunnossa, agentti sieppaa poikkeuksen ja vastaa avuliaasti; muuten koko sovellus kaatuu:

<img src="../../../translated_images/fi/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*Kun työkalu epäonnistuu, agentti nappaa virheen ja vastaa hyödyllisesti sen sijaan että kaatuu.*

Tämä tapahtuu yhdessä keskusteluvuorossa. Agentti orkestroi useita työkalukutsuja itsenäisesti.

## Sovelluksen käynnistäminen

**Tarkista käyttöönotto:**

Varmista, että `.env`-tiedosto on juurihakemistossa Azure-tunnuksilla (luotu Moduuli 01 aikana). Suorita tämä moduulihakemistosta (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Käynnistä sovellus:**

> **Huom:** Jos olet jo käynnistänyt kaikki sovellukset `./start-all.sh` -komennolla juurihakemistosta (kuten Moduulissa 01 kuvattu), tämä moduuli on jo käynnissä portissa 8084. Voit ohittaa käynnistyskomennot alla ja mennä suoraan osoitteeseen http://localhost:8084.

**Vaihtoehto 1: Spring Boot Dashboardin käyttö (suositeltu VS Code -käyttäjille)**

Kehityssäiliö sisältää Spring Boot Dashboard -laajennuksen, joka tarjoaa visuaalisen käyttöliittymän hallita kaikkia Spring Boot -sovelluksia. Löydät sen toiminta- palkista VS Code:ssa vasemmalla (etsi Spring Boot -ikonia).

Dashboardista voit:
- Näyttää kaikki käytettävissä olevat Spring Boot -sovellukset työtilassa
- Käynnistää/pysäyttää sovelluksia yhdellä klikkauksella
- Katsoa sovelluslokeja reaaliaikaisesti
- Valvoa sovellusten tilaa

Napsauta "tools"-kohdan pesäpallopainiketta käynnistääksesi tämän moduulin tai käynnistä kaikki moduulit kerralla.

Näin Spring Boot Dashboard näyttää VS Code:ssa:

<img src="../../../translated_images/fi/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard VS Code:ssa — käynnistä, pysäytä ja valvo kaikki moduulit yhdestä paikasta*

**Vaihtoehto 2: Shell-skriptien käyttö**

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

Molemmat skriptit lataavat automaattisesti ympäristömuuttujat juurihakemiston `.env`-tiedostosta ja rakentavat jar-tiedostot, jos niitä ei ole.

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

Avaa selaimessasi http://localhost:8084.

**Pysäytä:**

**Bash:**
```bash
./stop.sh  # Tämä moduuli ainoastaan
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

Sovellus tarjoaa web-käyttöliittymän, jossa voit olla vuorovaikutuksessa tekoälyagentin kanssa, jolla on pääsy sää- ja lämpötilamuunnostyökaluihin. Näin käyttöliittymä näyttää — siinä on pikakäynnistysesimerkkejä ja keskustelupaneeli pyyntöjen lähettämiseen:
<a href="images/tools-homepage.png"><img src="../../../translated_images/fi/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agenttyökalujen rajapinta" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI Agenttyökalujen rajapinta - nopeita esimerkkejä ja keskustelurajapinta työkalujen käyttöön*

### Kokeile yksinkertaista työkalun käyttöä

Aloita suoraviivaisella pyynnöllä: "Muuta 100 astetta Fahrenheitista Celsiukseksi". Agentti tunnistaa tarvitsevansa lämpötilamuunnostyökalun, kutsuu sitä oikeilla parametreilla ja palauttaa tuloksen. Huomaa, kuinka luonnolliselta tämä tuntuu – et määrittänyt, mitä työkalua käyttää tai miten sitä kutsutaan.

### Testaa työkaluketjutusta

Kokeile nyt monimutkaisempaa: "Mikä on sää Seattlessa ja muuta se Fahrenheitiksi?" Katso, miten agentti etenee vaiheittain. Se ensin hakee sään (joka palauttaa Celsius-asteet), tunnistaa tarvitsevansa muunnoksen Fahrenheitiksi, kutsuu muunnostyökalua ja yhdistää molemmat tulokset yhdeksi vastaukseksi.

### Katso keskustelun kulku

Keskustelurajapinta ylläpitää keskusteluhistoriaa, jolloin voit käydä monivaiheisia vuorovaikutuksia. Näet kaikki aiemmat kyselyt ja vastaukset, mikä helpottaa keskustelun seuraamista ja ymmärtämään, miten agentti rakentaa kontekstia useiden vaihdosten aikana.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/fi/tools-conversation-demo.89f2ce9676080f59.webp" alt="Keskustelu, jossa useita työkalukutsuja" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Monivaiheinen keskustelu, jossa tehdään yksinkertaisia muunnoksia, säähaut ja työkaluketjutusta*

### Kokeile eri pyyntöjä

Kokeile erilaisia yhdistelmiä:
- Säähaut: "Mikä on sää Tokiossa?"
- Lämpötilamuunnokset: "Mikä on 25°C Kelvineinä?"
- Yhdistetyt kyselyt: "Tarkista sää Pariisissa ja kerro, onko yli 20°C"

Huomaa, miten agentti tulkitsee luonnollista kieltä ja sovittaa sen sopiviin työkalukutsuihin.

## Keskeiset käsitteet

### ReAct-malli (Päättely ja Toiminta)

Agentti vuorottelee päättelyn (päätöksen tekemisen) ja toiminnan (työkalujen käytön) välillä. Tämä malli mahdollistaa itsenäisen ongelmanratkaisun pelkän ohjeisiin vastaamisen sijaan.

### Työkalukuvaukset ovat tärkeitä

Työkalukuvauksiesi laatu vaikuttaa suoraan siihen, kuinka hyvin agentti osaa niitä käyttää. Selkeät, täsmälliset kuvaukset auttavat mallia ymmärtämään, milloin ja miten kutakin työkalua kutsutaan.

### Istunnon hallinta

`@MemoryId`-annotaatio mahdollistaa automaattisen istuntopohjaisen muistin hallinnan. Jokaisella istunto-ID:llä on oma `ChatMemory`-instanssinsa, jota `ChatMemoryProvider`-bean hallinnoi, joten useat käyttäjät voivat olla vuorovaikutuksessa agentin kanssa samanaikaisesti ilman, että keskustelut sekoittuvat keskenään. Seuraava kaavio näyttää, miten useat käyttäjät ohjataan erillisiin muistivarastoihin istunto-ID:n perusteella:

<img src="../../../translated_images/fi/session-management.91ad819c6c89c400.webp" alt="Istunnon hallinta @MemoryId:n avulla" width="800"/>

*Jokainen istunto-ID vastaa omaa eristettyä keskusteluhistoriaa — käyttäjät eivät koskaan näe toistensa viestejä.*

### Virheenkäsittely

Työkalut voivat epäonnistua — API:t voivat aikakatkaista, parametrit voivat olla virheellisiä, ulkoiset palvelut voivat mennä alas. Tuotantoagenttien täytyy käsitellä virheitä, jotta malli voi selittää ongelmia tai kokeilla vaihtoehtoja eikä koko sovellus kaadu. Kun työkalu heittää poikkeuksen, LangChain4j tarttuu siihen ja syöttää virheilmoituksen takaisin mallille, joka voi sitten selittää ongelman luonnollisella kielellä.

## Saatavilla olevat työkalut

Alla oleva kaavio näyttää laajan työkaluekosysteemin, jonka voit rakentaa. Tämä moduuli demonstroi sää- ja lämpötilatyökaluja, mutta sama `@Tool`-malli toimii mille tahansa Java-metodille – tietokantakyselyistä maksujen käsittelyyn.

<img src="../../../translated_images/fi/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Työkaluekosysteemi" width="800"/>

*Jokainen @Tool-annotaatiolla varustettu Java-metodi tulee AI:n käyttöön — malli ulottuu tietokantoihin, API:hin, sähköposteihin, tiedostotoimintoihin ja muuhun.*

## Milloin käyttää työkalupohjaisia agentteja

Kaikki pyynnöt eivät tarvitse työkaluja. Päätös perustuu siihen, tarvitseeko tekoäly olla vuorovaikutuksessa ulkoisten järjestelmien kanssa vai pystyykö se vastaamaan omasta tietämyksestään. Seuraava opas tiivistää, milloin työkalut tuovat lisäarvoa ja milloin ne ovat tarpeettomia:

<img src="../../../translated_images/fi/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Milloin käyttää työkaluja" width="800"/>

*Nopea päätöspuu — työkalut ovat tarkoitettu reaaliaikaiseen tietoon, laskelmiin ja toimiin; yleinen tietämyspohjainen ja luovat tehtävät eivät niitä tarvitse.*

## Työkalut vs RAG

Moduulit 03 ja 04 laajentavat molemmat tekoälyn kykyjä, mutta perustavanlaatuisesti eri tavoilla. RAG antaa mallille pääsyn **tietoon** hakemalla dokumentteja. Työkalut antavat mallille kyvyn tehdä **toimia** kutsumalla funktioita. Alla oleva kaavio vertailee näitä kahta lähestymistapaa rinnakkain — siitä, miten kunkin työnkulku toimii ja millaisia kompromisseja niiden välillä on:

<img src="../../../translated_images/fi/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Työkalut vs RAG vertailu" width="800"/>

*RAG hakee tietoa staattisista dokumenteista — Työkalut toteuttavat toimintoja ja noutavat dynaamista, reaaliaikaista dataa. Monet tuotantojärjestelmät yhdistävät molemmat.*

Käytännössä monet tuotantojärjestelmät yhdistävät molemmat lähestymistavat: RAG pohjaa vastaukset dokumentaatioosi, ja Työkalut hakevat live-dataa tai suorittavat toimintoja.

## Seuraavat askelet

**Seuraava moduuli:** [05-mcp - Mallikontekstiprotokolla (MCP)](../05-mcp/README.md)

---

**Navigointi:** [← Edellinen: Moduuli 03 - RAG](../03-rag/README.md) | [Takaisin päävalikkoon](../README.md) | [Seuraava: Moduuli 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:  
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, otathan huomioon, että automaattikäännöksissä saattaa esiintyä virheitä tai epätarkkuuksia. Alkuperäinen asiakirja sen omalla kielellä on ensisijainen lähde. Tärkeiden tietojen osalta suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä aiheutuvista väärinkäsityksistä tai virhetulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->