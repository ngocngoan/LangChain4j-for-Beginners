# Moduuli 02: Kehoteinsinöörinä GPT-5.2:n kanssa

## Sisällysluettelo

- [Mitä opit](../../../02-prompt-engineering)
- [Esivaatimukset](../../../02-prompt-engineering)
- [Kehoteinsinöörnin ymmärtäminen](../../../02-prompt-engineering)
- [Kehoteinsinöörnin perusteet](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Edistyneet mallit](../../../02-prompt-engineering)
- [Olemassa olevien Azure-resurssien käyttäminen](../../../02-prompt-engineering)
- [Sovelluksen kuvakaappaukset](../../../02-prompt-engineering)
- [Mallien tutkiminen](../../../02-prompt-engineering)
  - [Matalan ja korkean innokkuuden erot](../../../02-prompt-engineering)
  - [Tehtävän suoritus (työkalujen esittely)](../../../02-prompt-engineering)
  - [Itsereflektoiva koodi](../../../02-prompt-engineering)
  - [Rakenteellinen analyysi](../../../02-prompt-engineering)
  - [Monivaiheinen keskustelu](../../../02-prompt-engineering)
  - [Askel askeleelta päättely](../../../02-prompt-engineering)
  - [Rajoitettu tulostus](../../../02-prompt-engineering)
- [Mitä todella opit](../../../02-prompt-engineering)
- [Seuraavat askeleet](../../../02-prompt-engineering)

## Mitä opit

<img src="../../../translated_images/fi/what-youll-learn.c68269ac048503b2.webp" alt="Mitä opit" width="800"/>

Edellisessä moduulissa näit, kuinka muisti mahdollistaa keskustelevan tekoälyn ja käytit GitHub-malleja perusvuorovaikutuksiin. Nyt keskitymme siihen, miten esität kysymyksiä — eli itse kehotteisiin — käyttäen Azure OpenAI:n GPT-5.2:ta. Tapa, jolla rakennat kehotteesi, vaikuttaa dramaattisesti saamaasi vastausten laatuun. Aloitamme perusteiden kertaamisesta, ja siirrymme sitten kahdeksaan edistyneeseen malliin, jotka hyödyntävät GPT-5.2:n koko kyvykkyyttä.

Käytämme GPT-5.2:ta, koska se tuo mukanaan päättelyn hallinnan — voit kertoa mallille, kuinka paljon sen tulisi miettiä ennen vastaamista. Tämä tekee eri kehotetaktiikoista selkeämpiä ja auttaa sinua ymmärtämään, milloin käyttää mitäkin lähestymistapaa. Hyödymme myös Azuren pienemmistä nopeusrajoituksista GPT-5.2:lle verrattuna GitHub-malleihin.

## Esivaatimukset

- Moduuli 01 suoritettu (Azure OpenAI -resurssit käyttöön otettu)
- `.env`-tiedosto juurihakemistossa Azuren tunnistetiedoilla (luotu `azd up` -komennolla moduulissa 01)

> **Huom:** Jos et ole vielä suorittanut moduulia 01, seuraa siellä annettuja käyttöönotto-ohjeita ensin.

## Kehoteinsinöörnin ymmärtäminen

<img src="../../../translated_images/fi/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Mitä on Kehoteinsinöörni?" width="800"/>

Kehoteinsinöörni tarkoittaa syötetekstin suunnittelua siten, että se johdonmukaisesti tuottaa tarvitsemiasi tuloksia. Kyse ei ole pelkästään kysymysten esittämisestä — vaan pyyntöjen rakenteellisuudesta niin, että malli ymmärtää täsmälleen, mitä haluat ja miten se toimitetaan.

Ajattele sitä kuin antaisit ohjeet kollegalle. "Korjaa virhe" on epämääräinen. "Korjaa null pointer -poikkeus UserService.java:n rivillä 45 lisäämällä null-tarkastus" on tarkka. Kielenmallit toimivat samalla tavalla — täsmällisyys ja rakenne ovat tärkeitä.

<img src="../../../translated_images/fi/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Miten LangChain4j sopii" width="800"/>

LangChain4j tarjoaa infrastruktuurin — mallit, muistin ja viestityypit — kun taas kehotemallit ovat vain huolellisesti rakennetut tekstit, jotka välität tämän infrastruktuurin läpi. Keskeiset rakennuspalikat ovat `SystemMessage` (joka asettaa tekoälyn käyttäytymisen ja roolin) ja `UserMessage` (joka sisältää varsinaisen pyyntösi).

## Kehoteinsinöörnin perusteet

<img src="../../../translated_images/fi/five-patterns-overview.160f35045ffd2a94.webp" alt="Viisi Kehoteinsinöörnin mallia yleiskatsaus" width="800"/>

Ennen kuin sukellamme tämän moduulin edistyneisiin malleihin, kerrataan viisi perustekniikkaa. Nämä ovat rakennuspalikoita, jotka jokaisen kehotteiden suunnittelijan tulisi hallita. Jos olet jo käynyt läpi [Pikaopas-moduulin](../00-quick-start/README.md#2-prompt-patterns), olet nähnyt nämä käytännössä — tässä on niiden käsitteellinen kehys.

### Zero-Shot Prompting

Yksinkertaisin lähestymistapa: anna mallille suora käsky ilman esimerkkejä. Malli luottaa täysin koulutukseensa ymmärtääkseen ja suorittaakseen tehtävän. Tämä toimii hyvin suoraviivaisissa pyynnöissä, joissa odotettu toiminta on ilmeinen.

<img src="../../../translated_images/fi/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Kehote" width="800"/>

*Suora ohje ilman esimerkkejä — malli päättelee tehtävän pelkän ohjeen perusteella*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Vastaus: "Positiivinen"
```

**Milloin käyttää:** Yksinkertaiset luokittelut, suorien kysymysten esittäminen, käännökset tai tehtävät, joita malli voi hoitaa ilman lisäohjeita.

### Few-Shot Prompting

Anna esimerkkejä, jotka havainnollistavat mallille halutun mallin. Malli oppii odotetun syöte-tuotos-muodon esimerkeistä ja soveltaa sitä uusiin syötteisiin. Tämä parantaa merkittävästi johdonmukaisuutta tehtävissä, joissa haluttu muoto tai toiminta ei ole ilmeistä.

<img src="../../../translated_images/fi/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Kehote" width="800"/>

*Oppiminen esimerkeistä — malli tunnistaa mallin ja soveltaa sitä uusiin tapauksiin*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**Milloin käyttää:** Räätälöidyt luokittelut, johdonmukainen muotoilu, alakohtaiset tehtävät tai kun zero-shot-tulokset ovat epätasaisia.

### Chain of Thought

Pyydä mallia näyttämään päättelynsä askel askeleelta. Sen sijaan, että hypätään suoraan vastaukseen, malli pilkkoo ongelman ja käy jokaisen osan läpi eksplisiittisesti. Tämä parantaa tarkkuutta matematiikassa, logiikassa ja monivaiheisissa päättelytehtävissä.

<img src="../../../translated_images/fi/chain-of-thought.5cff6630e2657e2a.webp" alt="Ajatusketjun Kehote" width="800"/>

*Askel askeleelta päättely — monimutkaisten ongelmien pilkkominen selkeiksi loogisiksi vaiheiksi*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Malli näyttää: 15 - 8 = 7, sitten 7 + 12 = 19 omenaa
```

**Milloin käyttää:** Matemaattiset ongelmat, loogiset pulmat, virheiden etsintä tai kaikki tehtävät, joissa päättelyprosessin näyttäminen parantaa tarkkuutta ja luottamusta.

### Role-Based Prompting

Aseta tekoälylle rooli tai persoona ennen kysymyksen esittämistä. Tämä antaa kontekstin, joka muokkaa vastausten sävyä, syvyyttä ja painopisteitä. "Ohjelmistoarkkitehti" antaa eri neuvoja kuin "juniori-kehittäjä" tai "turvatarkastaja".

<img src="../../../translated_images/fi/role-based-prompting.a806e1a73de6e3a4.webp" alt="Roolipohjainen kehote" width="800"/>

*Kontekstin ja roolin asettaminen — samaan kysymykseen saadaan eri vastaus riippuen annetusta roolista*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**Milloin käyttää:** Koodikatselmoinnit, opetus, alakohtaiset analyysit tai kun tarvitset vastauksia, jotka ovat räätälöityjä tietylle asiantuntijuustasolle tai näkökulmalle.

### Prompt Templates

Luo uudelleenkäytettäviä kehotteita muuttujapaikoilla. Sen sijaan että kirjoittaisit uuden kehotteen joka kerta, määrittele malli kerran ja täytä eri arvot. LangChain4j:n `PromptTemplate`-luokka helpottaa tätä `{{muuttuja}}`-syntaksilla.

<img src="../../../translated_images/fi/prompt-templates.14bfc37d45f1a933.webp" alt="Kehotemallit" width="800"/>

*Uudelleenkäytettävät kehotteet muuttujapaikoilla — yksi malli, monta käyttötarkoitusta*

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

**Milloin käyttää:** Toistuvat kyselyt eri syötteillä, eräkäsittely, uudelleenkäytettävien tekoälytyönkulkujen rakentaminen tai tilanteet, joissa kehotteen rakenne pysyy samana mutta data muuttuu.

---

Nämä viisi perustekniikkaa antavat sinulle vahvan työkalupakin useimpiin kehotetehtäviin. Tämä moduuli rakentaa niiden päälle kahdeksalla **edistyneellä mallilla**, jotka hyödyntävät GPT-5.2:n päättelyn hallintaa, itsereflektiota ja jäsenneltyä tulostusta.

## Edistyneet mallit

Perusteet hallussa, siirrytään kahdeksaan edistyneeseen malliin, jotka tekevät tästä moduulista ainutlaatuisen. Kaikki ongelmat eivät vaadi samaa lähestymistapaa. Jotkin kysymykset tarvitsevat nopeita vastauksia, toiset syvällistä pohdintaa. Jotkut vaativat näkyvää päättelyä, toiset pelkkiä tuloksia. Alla olevat mallit on optimoitu eri tilanteisiin — ja GPT-5.2:n päättelyn hallinta korostaa eroja entisestään.

<img src="../../../translated_images/fi/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Kahdeksan kehotemallia" width="800"/>

*Yleiskuva kahdeksasta kehotemallista ja niiden käyttötapauksista*

<img src="../../../translated_images/fi/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Päättelyn hallinta GPT-5.2 kanssa" width="800"/>

*GPT-5.2:n päättelyn hallinta antaa sinun määritellä, kuinka paljon mallin tulee miettiä — nopeista suorista vastauksista syvälliseen tutkimukseen*

<img src="../../../translated_images/fi/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Päättelyn ponnistuksen vertailu" width="800"/>

*Matan innokkuuden (nopea, suora) vs korkean innokkuuden (perusteellinen, tutkaileva) päättelymenetelmät*

**Matala innokkuus (nopea & tarkennettu)** - Yksinkertaisiin kysymyksiin, joissa haluat nopeita, suoria vastauksia. Malli toimii minimipäättelyllä - enintään kaksi vaihetta. Käytä tätä laskelmiin, hakuihin tai suoraviivaisiin kysymyksiin.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Tutki GitHub Copilotin avulla:** Avaa [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ja kysy:
> - "Mikä on ero matalan ja korkean innokkuuden kehotemallien välillä?"
> - "Miten XML-tunnisteet kehotteissa auttavat AI:n vastauksen jäsentelyssä?"
> - "Milloin minun pitäisi käyttää itsereflektoivia malleja verrattuna suoriin ohjeisiin?"

**Korkea innokkuus (syvällinen & perusteellinen)** - Monimutkaisiin ongelmiin, joissa haluat kattavan analyysin. Malli tutkii perusteellisesti ja näyttää yksityiskohtaisen päättelyn. Käytä tätä järjestelmäsuunnitteluun, arkkitehtuuripäätöksiin tai monimutkaiseen tutkimukseen.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Tehtävän suoritus (askeleittain eteneminen)** - Monivaiheisiin työnkulkuihin. Malli antaa etukäteissuunnitelman, kertoo jokaisen vaiheen aikana mitä tekee ja lopuksi tiivistää. Käytä tätä migroinneissa, toteutuksissa tai missä tahansa monivaiheisessa prosessissa.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Ajatusketjun (Chain-of-Thought) kehotteet pyytävät mallia näyttämään päättelyprosessinsa, mikä parantaa tarkkuutta monimutkaisissa tehtävissä. Askel askeleelta -erittely auttaa sekä ihmistä että tekoälyä ymmärtämään logiikan.

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatilla:** Kysy tästä mallista:
> - "Miten sopeutan tehtävän suoritusmallin pitkäkestoisiin operaatioihin?"
> - "Mitkä ovat parhaat käytännöt työkalujen esittelyjen rakenteistamiseen tuotantosovelluksissa?"
> - "Miten voin kaapata ja näyttää päivitykset välivaiheista käyttöliittymässä?"

<img src="../../../translated_images/fi/task-execution-pattern.9da3967750ab5c1e.webp" alt="Tehtävän suoritusmalli" width="800"/>

*Suunnittele → Suorita → Tiivistä -työnkulku monivaiheisille tehtäville*

**Itsereflektoiva koodi** - Tuottaa tuotantokelpoista koodia. Malli generoi koodin, tarkistaa sen laadun ja parantaa sitä iteroiden. Käytä tätä uusien ominaisuuksien tai palveluiden rakentamisessa.

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

<img src="../../../translated_images/fi/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Itsereflektion sykli" width="800"/>

*Iteratiivinen parannusprosessi – generoi, arvioi, tunnista ongelmat, paranna, toista*

**Rakenteellinen analyysi** - Johdonmukaiseen arviointiin. Malli tarkistaa koodin kiinteän viitekehyksen avulla (oikeellisuus, käytännöt, suorituskyky, turvallisuus). Käytä tätä koodikatselmointiin tai laadun arviointiin.

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

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatilla:** Kysy rakenteellisesta analyysistä:
> - "Miten voin räätälöidä analyysikehystä erilaisten koodikatselmointien tarpeisiin?"
> - "Mikä on paras tapa jäsentää ja käsitellä rakenteellista tulostetta ohjelmallisesti?"
> - "Miten varmistan johdonmukaiset vakavuustasot eri tarkastussessioissa?"

<img src="../../../translated_images/fi/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Rakenteellisen analyysin malli" width="800"/>

*Nelikategorinen kehys johdonmukaisiin koodikatselmiin vakavuustasoilla*

**Monivaiheinen keskustelu** - Keskusteluihin, joissa tarvitaan kontekstia. Malli muistaa aiemmat viestit ja rakentaa niiden päälle. Käytä tätä interaktiivisiin tukisessioihin tai monimutkaisiin kysymys-vastaus-tilanteisiin.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/fi/context-memory.dff30ad9fa78832a.webp" alt="Keskustelukonteksti muisti" width="800"/>

*Kuinka keskustelukonteksti kertyy useiden vuorojen aikana token-rajoihin asti*

**Askel askeleelta päättely** - Näkyvän loogisen mallintamisen tehtäviin. Malli näyttää eksplisiittisen päättelyn jokaisen askeleen osalta. Käytämatematiikan, logiikan pulmien tai kaikkien tilanteiden ymmärtämiseksi, joissa päättelyprosessi on tärkeä.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fi/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Askel askeleelta -malli" width="800"/>

*Ongelmien pilkkominen eksplisiittisiin loogisiin askeliin*

**Rajoitettu tulostus** - Vastauksiin, joissa vaaditaan tiettyä muotoa. Malli noudattaa tarkasti muoto- ja pituussääntöjä. Käytä yhteenvetoihin tai kun haluat tarkan tulostusrakenteen.

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

<img src="../../../translated_images/fi/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Rajoitetun tulostuksen malli" width="800"/>

*Erityisten muoto-, pituus- ja rakennevaatimusten noudattaminen*

## Olemassa olevien Azure-resurssien käyttäminen

**Varmista käyttöönotto:**

Varmista, että `.env`-tiedosto löytyy juurihakemistosta Azuren tunnistetiedoilla (luotu moduulin 01 aikana):
```bash
cat ../.env  # Pitäisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Käynnistä sovellus:**

> **Huom:** Jos olet jo käynnistänyt kaikki sovellukset `./start-all.sh` -komennolla moduulissa 01, tämä moduuli on jo käynnissä portissa 8083. Voit ohittaa alla olevat käynnistysohjeet ja mennä suoraan osoitteeseen http://localhost:8083.

**Vaihtoehto 1: Spring Boot Dashboardin käyttäminen (suositeltu VS Code -käyttäjille)**

Kehityssäiliö sisältää Spring Boot Dashboard -laajennuksen, joka tarjoaa visuaalisen käyttöliittymän kaikkien Spring Boot -sovellusten hallintaan. Löydät sen VS Code:n Aktiviteettipalkista vasemmalla (etsi Spring Boot -kuvaketta).
Spring Boot -kojelautakäyttöliittymästä voit:
- Näyttää kaikki työtilassa olevat Spring Boot -sovellukset
- Käynnistää/pysäyttää sovellukset yhdellä napsautuksella
- Tarkastella sovelluksen lokitietoja reaaliajassa
- Valvoa sovelluksen tilaa

Aloita tämä moduuli napsauttamalla pelipainiketta "prompt-engineering"-kohdan vieressä tai käynnistä kaikki moduulit yhtä aikaa.

<img src="../../../translated_images/fi/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Vaihtoehto 2: Shell-skriptien käyttäminen**

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
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

Molemmat skriptit lataavat automaattisesti ympäristömuuttujat juuren `.env`-tiedostosta ja rakentavat JAR-tiedostot, jos niitä ei ole olemassa.

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

Avaa selaimessasi osoite http://localhost:8083.

**Sovelluksen pysäyttäminen:**

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

*Pääkojelaudanäkymä, jossa esitetään kaikki 8 prompt-tekniikkaa niiden ominaisuuksineen ja käyttötapauksineen*

## Mallien tutkiminen

Verkkokäyttöliittymä antaa mahdollisuuden kokeilla erilaisia ohjausstrategioita. Jokainen malli ratkaisee erilaisia ongelmia – kokeile niitä nähdäksesi, milloin kukin lähestymistapa toimii parhaiten.

### Matala vs Korkea innokkuus

Kysy yksinkertainen kysymys, kuten "Paljonko on 15 % luvusta 200?" käyttäen matalaa innokkuutta. Saat välittömän ja suoran vastauksen. Kysy sitten monimutkainen kysymys, kuten "Suunnittele välimuististrategia korkean liikenteen API:lle" käyttäen korkeaa innokkuutta. Katso, kuinka malli hidastaa tahtia ja tarjoaa yksityiskohtaista perustelua. Sama malli, sama kysymyksen rakenne – mutta prompt ohjaa kuinka paljon ajattelua tehdään.

<img src="../../../translated_images/fi/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Nopea lasku minimaalisen päättelyn kera*

<img src="../../../translated_images/fi/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Yksityiskohtainen välimuististrategia (2.8MB)*

### Tehtävän suoritus (Työkalujen aloitusosiot)

Monivaiheiset työnkulut hyötyvät etukäteissuunnittelusta ja etenemisen kertomisesta. Malli kuvailee, mitä aikoo tehdä, kertoo jokaisen vaiheen, ja avaa lopuksi tulokset.

<img src="../../../translated_images/fi/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*REST-päätepisteen luominen vaihe vaiheelta kerrottuna (3.9MB)*

### Itsearvioiva koodi

Kokeile "Luo sähköpostin validointipalvelu". Sen sijaan, että malli vain generoi koodin ja pysähtyy, se tuottaa koodin, arvioi sitä laatukriteerien mukaan, tunnistaa heikkoudet ja parantaa sitä. Näet prosessin, jossa koodi kehitetään, kunnes se täyttää tuotantostandardit.

<img src="../../../translated_images/fi/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Täydellinen sähköpostin validointipalvelu (5.2MB)*

### Rakenteellinen analyysi

Koodikatselmuksissa tarvitaan johdonmukaisia arviointikehyksiä. Malli analysoi koodia käyttäen ennalta määrättyjä kategorioita (oikeellisuus, käytännöt, suorituskyky, turvallisuus) vakavuustason mukaan.

<img src="../../../translated_images/fi/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Kehoitteisiin perustuva koodikatselmus*

### Monivaiheinen keskustelu

Kysy "Mikä on Spring Boot?" ja kysy heti perään "Näytä minulle esimerkki". Malli muistaa ensimmäisen kysymyksesi ja antaa sinulle nimenomaan Spring Boot -esimerkin. Ilman muistia toinen kysymys olisi liian epämääräinen.

<img src="../../../translated_images/fi/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Kontekstin säilyminen kysymysten välillä*

### Vaiheittainen päättely

Valitse matemaattinen ongelma ja kokeile sitä sekä Vaiheittaisella päättelyllä että Matalalla innokkuudella. Matala innokkuus antaa sinulle vain vastauksen – nopeasti mutta läpinäkymättömästi. Vaiheittainen päättely näyttää jokaisen laskelman ja päätöksen.

<img src="../../../translated_images/fi/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matemaattinen ongelma selkeillä vaiheilla*

### Rajoitettu tuloste

Kun tarvitset tiettyjä formaatteja tai sanamääriä, tämä malli varmistaa tarkasti vaadittujen ehtojen täyttymisen. Kokeile tuottaa yhteenveto, jossa on tarkalleen 100 sanaa luettelomuodossa.

<img src="../../../translated_images/fi/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Koneoppimisen yhteenveto muoto-ohjauksella*

## Mitä todella opit

**Päättelypanos muuttaa kaiken**

GPT-5.2 antaa sinun ohjata laskennallista panosta promptien avulla. Matala panos tarkoittaa nopeita vastauksia minimaalisen tutkimisen kanssa. Korkea panos tarkoittaa, että malli käyttää aikaa syvälliseen ajatteluun. Opit sovittamaan panoksen tehtävän monimutkaisuuteen – älä tuhlaa aikaa yksinkertaisiin kysymyksiin, mutta älä kiirehdi monimutkaisia päätöksiä.

**Rakenne ohjaa käyttäytymistä**

Huomaatko XML:ää muistuttavat tunnisteet prompteissa? Ne eivät ole pelkkää koristelua. Mallit seuraavat rakenteisia ohjeita luotettavammin kuin vapaamuotoista tekstiä. Kun tarvitset monivaiheisia prosesseja tai monimutkaista logiikkaa, rakenne auttaa mallia pysymään kartalla siitä, missä se on ja mitä seuraavaksi tulee.

<img src="../../../translated_images/fi/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Hyvin rakennetun promptin anatomia, jossa selkeät osiot ja XML-tyylinen jäsennys*

**Laatu itsearvioinnin kautta**

Itsearvioivat mallit toimivat tekemällä laatukriteerit eksplisiitteinä. Sen sijaan, että toivoisit mallin "tekevän oikein", kerrot sille tarkalleen, mitä "oikein" tarkoittaa: oikea logiikka, virheenkäsittely, suorituskyky, turvallisuus. Malli voi sitten arvioida omaa tulostaan ja parantaa sitä. Tämä muuttaa koodin tuottamisen arpajaisista prosessiksi.

**Konteksti on rajallinen**

Monivaiheiset keskustelut toimivat liittämällä viestihistorian jokaiseen pyyntöön. Mutta on olemassa raja – jokaisella mallilla on maksimimäärä tokeneita. Keskustelujen kasvaessa tarvitset strategioita säilyttää olennaiset tiedot ilman, että ylittyy tämä raja. Tämä moduuli näyttää, kuinka muisti toimii; myöhemmin opit, milloin tiivistää, milloin unohtaa ja milloin hakea tietoa.

## Seuraavat askeleet

**Seuraava moduuli:** [03-rag - RAG (Hakuperustainen generointi)](../03-rag/README.md)

---

**Navigointi:** [← Edellinen: Moduuli 01 - Johdanto](../01-introduction/README.md) | [Takaisin pääsivulle](../README.md) | [Seuraava: Moduuli 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty tekoälypohjaisella käännöspalvelulla [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, pyydämme huomioimaan, että automaattikäännöksissä saattaa esiintyä virheitä tai epätarkkuuksia. Alkuperäistä asiakirjaa sen alkuperäiskielellä tulee pitää virallisena lähteenä. Tärkeiden tietojen osalta suosittelemme ammattimaista ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä aiheutuvista väärinymmärryksistä tai virhetulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->