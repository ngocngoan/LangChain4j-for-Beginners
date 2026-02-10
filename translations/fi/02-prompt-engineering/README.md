# Module 02: Kehoteinsinööritys GPT-5.2:n kanssa

## Sisällysluettelo

- [Mitä opit](../../../02-prompt-engineering)
- [Esivaatimukset](../../../02-prompt-engineering)
- [Kehoteinsinöörityksen ymmärtäminen](../../../02-prompt-engineering)
- [Miten tämä käyttää LangChain4j:ää](../../../02-prompt-engineering)
- [Ydinkuvioiden esittely](../../../02-prompt-engineering)
- [Olemassa olevien Azure-resurssien käyttäminen](../../../02-prompt-engineering)
- [Sovelluksen kuvakaappaukset](../../../02-prompt-engineering)
- [Kuvioiden tutkiminen](../../../02-prompt-engineering)
  - [Alhainen vs Korkea innokkuus](../../../02-prompt-engineering)
  - [Tehtävän suoritus (työkalujen alkusanat)](../../../02-prompt-engineering)
  - [Itseheijastava koodi](../../../02-prompt-engineering)
  - [Jäsennelty analyysi](../../../02-prompt-engineering)
  - [Moniosainen keskustelu](../../../02-prompt-engineering)
  - [Askel askeleelta päättely](../../../02-prompt-engineering)
  - [Rajoitettu tulostus](../../../02-prompt-engineering)
- [Mitä todella opit](../../../02-prompt-engineering)
- [Seuraavat vaiheet](../../../02-prompt-engineering)

## Mitä opit

Edellisessä moduulissa näit, miten muisti mahdollistaa keskustelevan tekoälyn ja käytit GitHub-malleja perusvuorovaikutuksiin. Nyt keskitymme siihen, miten esität kysymyksiä – eli kehotteisiin itseensä – käyttäen Azure OpenAI:n GPT-5.2:ta. Se, miten rakennat kehotteesi, vaikuttaa merkittävästi saamiesi vastausten laatuun.

Käytämme GPT-5.2:ta, koska se tuo mukanaan päättelyohjauksen – voit kertoa mallille, kuinka paljon sen tulee ajatella ennen vastaamista. Tämä tekee eri kehotteenrakennusstrategioista selkeämpiä ja auttaa sinua ymmärtämään, milloin käytetään mitäkin lähestymistapaa. Hyödymme myös Azuren vähäisemmistä nopeusrajoituksista GPT-5.2:n kohdalla verrattuna GitHub-malleihin.

## Esivaatimukset

- Module 01 suoritettu (Azure OpenAI -resurssit käyttöönotettu)
- Juureen tallennettu `.env`-tiedosto Azure-tunnuksilla (luotu `azd up` -komennolla Module 01:n aikana)

> **Huom:** Jos et ole suorittanut Module 01:tä, noudata ensin siellä annettuja käyttöönotto-ohjeita.

## Kehoteinsinöörityksen ymmärtäminen

Kehoteinsinöörisyys tarkoittaa syötetekstin suunnittelua siten, että saat johdonmukaisesti tarvitsemasi tulokset. Kyse ei ole pelkästään kysymysten esittämisestä – vaan pyyntöjen jäsentämisestä niin, että malli ymmärtää täsmälleen, mitä haluat ja miten se toimitetaan.

Ajattele sitä kuin antaisit ohjeita kollegalle. "Korjaa bugi" on epämääräinen. "Korjaa UserService.java -tiedoston rivin 45 null-viittausvirhe lisäämällä null-tarkistus" on täsmällinen. Kielenmallit toimivat samalla tavalla – täsmällisyys ja rakenne ovat tärkeitä.

## Miten tämä käyttää LangChain4j:ää

Tämä moduuli demonstroi edistyneitä kehotemalleja käyttäen samaa LangChain4j-pohjaa kuin edelliset moduulit, painottaen kehotteen rakennetta ja päättelyohjausta.

<img src="../../../translated_images/fi/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*Kuinka LangChain4j yhdistää kehotteesi Azure OpenAI GPT-5.2:een*

**Riippuvuudet** – Module 02 käyttää `pom.xml`-tiedostossa määriteltyjä langchain4j-riippuvuuksia:  
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
  
**OpenAiOfficialChatModel -määritys** – [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Keskustelumalli on manuaalisesti konfiguroitu Spring beaniksi käyttäen OpenAI Official -asiakasta, joka tukee Azure OpenAI -päätepisteitä. Keskeinen ero Module 01:een nähden on se, miten jäsennämme `chatModel.chat()` -metodille lähetettyjä kehotteita, ei itse mallin määrittely.

**Järjestelmä- ja käyttäjäviestit** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j erottaa viestityypit selkeyden vuoksi. `SystemMessage` määrittää tekoälyn käyttäytymisen ja kontekstin (esim. "Olet koodin tarkastaja"), kun taas `UserMessage` sisältää varsinaisen pyynnön. Tämä erottelu mahdollistaa johdonmukaisen tekoälyn käyttäytymisen eri käyttäjäkyselyissä.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```
  
<img src="../../../translated_images/fi/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage tarjoaa pysyvän kontekstin, kun UserMessages sisältävät yksittäiset pyynnöt*

**MessageWindowChatMemory monivaiheisissa keskusteluissa** – Monivaiheisen keskustelun kuviossa käytämme Module 01:stä tuttua `MessageWindowChatMemory`-luokkaa. Jokaiselle istunnolle annetaan oma muisti-instanssi tallennettuna `Map<String, ChatMemory>`-rakenteeseen, mikä mahdollistaa samanaikaiset useat keskustelut ilman kontekstisekoituksia.

**Kehotepohjat** – Tässä keskitytään kehoteinsinöörisyyteen, ei uusiin LangChain4j-rajapintoihin. Jokainen kuvio (alhainen innokkuus, korkea innokkuus, tehtävän suoritus jne.) käyttää samaa `chatModel.chat(prompt)` -metodia, mutta huolellisesti jäsenneltyjen kehotetekstien kanssa. XML-tunnisteet, ohjeistukset ja muotoilu ovat osa kehotetekstiä, eivät LangChain4j-ominaisuuksia.

**Päättelyohjaus** – GPT-5.2:n päättelytyötä ohjataan kehotteissa annettavilla ohjeilla, kuten "enintään 2 päättelyaskelta" tai "tutki perusteellisesti". Nämä ovat kehoteinsinöörisyyden menetelmiä, eivät LangChain4j:n määrityksiä. Kirjasto tuo kehotteesi mallille sellaisenaan.

Keskeinen opetus: LangChain4j tarjoaa infrastruktuurin (malliyhteyden [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java) kautta, muistin ja viestien käsittelyn [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) kautta), kun taas tämä moduuli opettaa, miten rakennat tehokkaita kehotteita tuon infrastruktuurin sisällä.

## Ydinkuvioiden esittely

Kaikki ongelmat eivät vaadi samaa lähestymistapaa. Jotkut kysymykset kaipaavat nopeita vastauksia, toiset syvällistä pohdintaa. Toisissa näkyvä päättely on tarpeen, toisissa pelkkä lopputulos riittää. Tässä moduulissa käsitellään kahdeksan kehotemallia, jotka on optimoitu erilaisiin tilanteisiin. Kokeilet kaikkia oppiaksesi, milloin kukin toimii parhaiten.

<img src="../../../translated_images/fi/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Yhdeksän kehotteidenrakennuskuvion yleiskatsaus ja käyttötapaukset*

<img src="../../../translated_images/fi/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Alhainen innokkuus (nopea, suora) vs Korkea innokkuus (perusteellinen, tutkiva) päättelytavat*

**Alhainen innokkuus (nopea & fokusoitu)** – Yksinkertaisiin kysymyksiin, joissa haluat nopean, suoraviivaisen vastauksen. Malli suorittaa minimipäättelyä – enintään 2 askelta. Käytä tätä laskuihin, hakuun tai suoraviivaisiin kysymyksiin.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```
  
> 💡 **Tutustu GitHub Copilotilla:** Avaa [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ja kysy:  
> - "Mikä ero on alhaisen ja korkean innokkuuden kehotemalleilla?"  
> - "Miten XML-tunnisteet kehotteissa auttavat jäsentämään tekoälyn vastausta?"  
> - "Milloin käytän itseheijastavia kuvioita vs suoraa ohjeistusta?"

**Korkea innokkuus (syvällinen & perusteellinen)** – Monimutkaisiin ongelmiin, joissa haluat kattavan analyysin. Malli tutkii perusteellisesti ja näyttää yksityiskohtaisen päättelyn. Käytä tätä järjestelmän suunnittelussa, arkkitehtuuripäätöksissä tai monimutkaisessa tutkimuksessa.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**Tehtävän suoritus (askel askeleelta eteneminen)** – Monivaiheisiin työnkulkuihin. Malli antaa etukäteen suunnitelman, kertoo etenemisestä jokaisen vaiheen aikana ja lopuksi tekee yhteenvedon. Käytä tähän migraatioissa, toteutuksissa tai missä tahansa monivaiheisessa prosessissa.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```
  
Ketjupäättely-kehote pyytää mallia osoittamaan päättelyprosessinsa, mikä parantaa tarkkuutta monimutkaisissa tehtävissä. Askel askeleelta -erittely auttaa sekä ihmisiä että tekoälyä ymmärtämään logiikan.

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatilla:** Kysy tästä kuviosta:  
> - "Miten mukautan tehtävän suoritus -kuviota pitkäkestoisille operaatioille?"  
> - "Mitkä ovat parhaat käytännöt työkalujen alkusanomien jäsentämiseen tuotantosovelluksissa?"  
> - "Miten voin kaapata ja näyttää välivaiheen etenemisilmoitukset käyttöliittymässä?"

<img src="../../../translated_images/fi/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Suunnittele → Suorita → Yhteenveto -työnkulku monivaiheisille tehtäville*

**Itseheijastava koodi** – Tuotantolaatuista koodia generoitaessa. Malli luo koodin, tarkistaa sen laatuvaatimuksia vastaan ja parantaa sitä iteratiivisesti. Käytä uutta ominaisuutta tai palvelua rakentaessasi.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/fi/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iteratiivisen parantamisen sykli – luo, arvioi, tunnista ongelmat, paranna, toista*

**Jäsennelty analyysi** – Johdonmukaisia arviointeja varten. Malli tarkastelee koodia kiinteän kehyksen mukaan (korrektius, käytännöt, suorituskyky, turvallisuus). Käytä tätä koodikatselmuksissa tai laadun arvioinneissa.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```
  
> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatilla:** Kysy jäsennellystä analyysistä:  
> - "Miten muokkaan analyysikehystä eri tyyppisiin koodikatselmuksiin?"  
> - "Mikä on paras tapa jäsentää ja käsitellä jäsenneltyä tulostusta ohjelmallisesti?"  
> - "Kuinka varmistan johdonmukaiset vakavuustasot eri katselmusistuntojen välillä?"

<img src="../../../translated_images/fi/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Neljän kategorian kehys johdonmukaisiin koodikatselmuksiin vakavuustasoilla*

**Moniosainen keskustelu** – Keskusteluihin, jotka tarvitsevat kontekstia. Malli muistaa aiemmat viestit ja rakentaa niiden päälle. Käytä tätä interaktiivisiin tukisessioihin tai monimutkaisiin kysymys-vastaus -tilanteisiin.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
<img src="../../../translated_images/fi/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Kuinka keskustelun konteksti kertyy monien vuorojen yli token-rajaan asti*

**Askel askeleelta päättely** – Ongelmille, jotka vaativat näkyvää loogista päättelyä. Malli näyttää eksplisiittisen päättelyn jokaiselle askeleelle. Käytä tätä matematiikkaan, loogisiin pulmiin tai silloin, kun haluat ymmärtää ajatteluprosessia.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/fi/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Ongelman pilkkominen eksplisiittisiin loogisiin askeliin*

**Rajoitettu tulostus** – Vastauksiin, joilla on erityiset muoto- tai pituusvaatimukset. Malli noudattaa tarkasti asetettuja muoto- ja pituussääntöjä. Käytä tiivistelmien laatimiseen tai kun tarvitset tarkan tulostusrakenteen.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/fi/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Erityisten muoto-, pituus- ja rakennevaatimusten noudattaminen*

## Olemassa olevien Azure-resurssien käyttäminen

**Tarkista käyttöönotto:**

Varmista, että juurihakemistossa on `.env`-tiedosto Azure-tunnuksilla (luotu Module 01:n aikana):  
```bash
cat ../.env  # Tulis näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Käynnistä sovellus:**

> **Huom:** Jos olet jo käynnistänyt kaikki sovellukset komennolla `./start-all.sh` Module 01:stä, tämä moduuli on jo käynnissä portissa 8083. Voit ohittaa alla olevat käynnistyskomennot ja siirtyä suoraan osoitteeseen http://localhost:8083.

**Vaihtoehto 1: Spring Boot Dashboardin käyttö (suositeltu VS Code -käyttäjille)**

Kehityssäiliössä on mukana Spring Boot Dashboard -laajennus, joka tarjoaa visuaalisen käyttöliittymän kaikkien Spring Boot -sovellusten hallintaan. Löydät sen VS Coden vasemmanpuoleisesta Activity Barista (etsi Spring Boot -ikonia).

Spring Boot Dashboardista voit:  
- Näyttää kaikki työtilan saatavilla olevat Spring Boot -sovellukset  
- Käynnistää/pysäyttää sovellukset yhdellä klikkauksella  
- Katsoa sovelluslokeja reaaliajassa  
- Valvoa sovelluksen tilaa  

Klikkaa yksinkertaisesti toistopainiketta "prompt-engineering" -sovelluksen kohdalla käynnistääksesi tämän moduulin, tai käynnistä kaikki moduulit kerralla.

<img src="../../../translated_images/fi/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

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
cd 02-prompt-engineering
./start.sh
```
  
**PowerShell:**  
```powershell
cd 02-prompt-engineering
.\start.ps1
```
  
Molemmat skriptit lataavat automaattisesti ympäristömuuttujat juuren `.env`-tiedostosta ja rakentavat JAR:t, jos niitä ei vielä ole.

> **Huom:** Jos haluat rakentaa moduulit manuaalisesti ennen käynnistystä:  
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
  
Avaa http://localhost:8083 selaimessasi.

**Keskeyttääksesi:**

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
  
## Sovelluksen kuvakaappaukset

<img src="../../../translated_images/fi/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Pääsivu, joka näyttää kaikki 8 kehotteidenrakennuskuviota niiden ominaisuuksineen ja käyttötapauksineen*

## Kuvioiden tutkiminen

Verkkokäyttöliittymä antaa sinun kokeilla eri kehotusstrategioita. Jokainen kuvio ratkaisee erilaisia ongelmia – kokeile niitä nähdäksesi, milloin mikäkin lähestymistapa toimii parhaiten.

### Alhainen vs Korkea innokkuus

Kysy yksinkertainen kysymys, kuten "Mikä on 15 % luvusta 200?" käyttäen Alhaisen innokkuuden mallia. Saat heti suoran vastauksen. Kysy sitten jotain monimutkaisempaa, kuten "Suunnittele välimuististrategia korkean liikenteen API:lle" käyttäen Korkean innokkuuden mallia. Katso, miten malli hidastuu ja antaa yksityiskohtaisen päättelyn. Sama malli, sama kysymysrakenne – mutta kehotteessa kerrotaan, kuinka paljon mallin on ajateltava.
<img src="../../../translated_images/fi/low-eagerness-demo.898894591fb23aa0.webp" alt="Vaikeustason Demo" width="800"/>

*Nopea laskenta vähäisellä päättelyllä*

<img src="../../../translated_images/fi/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Korkean halukkuuden demo" width="800"/>

*Laaja välimuististrategia (2.8MB)*

### Tehtävän suoritus (Työkalujen esiluvut)

Monivaiheiset työnkulut hyötyvät ennakkosuunnittelusta ja etenemiskerronnasta. Malli hahmottelee, mitä se aikoo tehdä, kuvailee jokaisen vaiheen ja sitten tiivistää tulokset.

<img src="../../../translated_images/fi/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Tehtävän suoritus -demo" width="800"/>

*REST-päätepisteen luominen vaihe vaiheelta kertoen (3.9MB)*

### Itsetarkasteleva koodi

Kokeile "Luo sähköpostin varmennuspalvelu". Sen sijaan, että malli vain generoi koodin ja pysähtyy, se tuottaa, arvioi laatuvaatimuksia vastaan, tunnistaa puutteet ja parantaa. Näet sen toistavan, kunnes koodi täyttää tuotantostandardit.

<img src="../../../translated_images/fi/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Itsetarkasteleva koodi -demo" width="800"/>

*Täydellinen sähköpostin varmennuspalvelu (5.2MB)*

### Rakenneanalyysi

Koodikatselmukset tarvitsevat yhdenmukaiset arviointikehykset. Malli analysoi koodia kiinteiden kategorioiden (oikeellisuus, käytännöt, suorituskyky, turvallisuus) ja vakavuustasojen avulla.

<img src="../../../translated_images/fi/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Rakenneanalyysi-demo" width="800"/>

*Kehykseen perustuva koodikatselmus*

### Monivuoroinen keskustelu

Kysy "Mikä on Spring Boot?" ja heti perään "Näytä esimerkki". Malli muistaa ensimmäisen kysymyksesi ja antaa juuri sinulle Spring Boot -esimerkin. Ilman muistia toinen kysymys olisi liian epämääräinen.

<img src="../../../translated_images/fi/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Monivuoroinen keskustelu -demo" width="800"/>

*Kontekstin säilyttäminen kysymysten välillä*

### Vaiheittainen päättely

Valitse matemaattinen ongelma ja kokeile sitä sekä vaiheittaisella päättelyllä että vähäisellä halukkuudella. Vähäisellä halukkuudella saat vain vastauksen – nopea mutta epäselvä. Vaiheittain näytetään jokainen laskutoimitus ja päätös.

<img src="../../../translated_images/fi/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Vaiheittainen päättely -demo" width="800"/>

*Matemaattinen ongelma selkein askelin*

### Rajattu tulostus

Kun tarvitset erityisiä muotoja tai sanamääriä, tämä malli pakottaa tiukkaan noudattamiseen. Kokeile luoda yhteenveto, jossa on täsmälleen 100 sanaa luettelomuodossa.

<img src="../../../translated_images/fi/constrained-output-demo.567cc45b75da1633.webp" alt="Rajattu tulostus -demo" width="800"/>

*Koneoppimisen tiivistelmä muodon hallinnalla*

## Mitä Oikeasti Opit

**Päättelypanos Muuttaa Kaiken**

GPT-5.2 antaa sinun hallita laskentapanosta syötteidesi kautta. Matala panos tarkoittaa nopeita vastauksia vähäisellä tutkimisella. Korkea panos tarkoittaa, että malli käyttää aikaa syvälliseen ajatteluun. Opit sovittamaan panoksen tehtävän monimutkaisuuteen – älä tuhlaa aikaa yksinkertaisiin kysymyksiin, mutta älä myöskään kiirehdi monimutkaisia päätöksiä.

**Rakenne Ohjaa Käyttäytymistä**

Huomaatko XML-tunnisteet syötteissä? Ne eivät ole koristeita. Mallit seuraavat rakenteellisia ohjeita luotettavammin kuin vapaamuotoista tekstiä. Kun tarvitset monivaiheisia prosesseja tai monimutkaista logiikkaa, rakenne auttaa mallia seuraamaan, missä se on ja mikä tulee seuraavaksi.

<img src="../../../translated_images/fi/prompt-structure.a77763d63f4e2f89.webp" alt="Syötteen Rakenne" width="800"/>

*Hyvin rakennetun syötteen anatomia selkeillä osioilla ja XML-tyylisellä organisoinnilla*

**Laatu Itsearvioinnin Kautta**

Itsetarkastelevat mallit toimivat tekemällä laatukriteerit eksplisiittisiksi. Sen sijaan että toivoisit mallin "tekevän oikein", kerrot sille tarkalleen, mitä "oikein" tarkoittaa: oikea logiikka, virheenkäsittely, suorituskyky, turvallisuus. Malli voi sitten arvioida omaa tuotostaan ja parantaa sitä. Tämä muuttaa koodin generoinnin lotosta prosessiksi.

**Konteksti On Rajallinen**

Monivuoroiset keskustelut toimivat sisällyttämällä viestihistorian jokaiseen pyyntöön. Mutta on olemassa raja – jokaisella mallilla on maksimimäärä tokeneita. Keskustelujen kasvaessa tarvitset strategioita pitää relevantti konteksti ilman, että raja ylittyy. Tämä moduuli näyttää, miten muisti toimii; myöhemmin opit, milloin tiivistää, milloin unohtaa ja milloin hakea uudelleen.

## Seuraavat Askeleet

**Seuraava moduuli:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigointi:** [← Edellinen: Moduuli 01 - Johdanto](../01-introduction/README.md) | [Takaisin pääsivulle](../README.md) | [Seuraava: Moduuli 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:  
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Pyrimme tarkkuuteen, mutta otathan huomioon, että automatisoiduissa käännöksissä saattaa esiintyä virheitä tai epätarkkuuksia. Alkuperäinen asiakirja sen alkuperäiskielellä on virallinen lähde. Tärkeissä asioissa suositellaan ammattimaista ihmiskääntäjää. Emme ole vastuussa mahdollisista väärinymmärryksistä tai tulkinnoista, jotka johtuvat tämän käännöksen käytöstä.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->