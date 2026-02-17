# Module 02: Kehota insinöörityö GPT-5.2:lla

## Sisällysluettelo

- [Mitä opit](../../../02-prompt-engineering)
- [Esivaatimukset](../../../02-prompt-engineering)
- [Kehota insinöörityön ymmärtäminen](../../../02-prompt-engineering)
- [Kehota insinöörityön perusteet](../../../02-prompt-engineering)
  - [Zero-Shot-kehote](../../../02-prompt-engineering)
  - [Few-Shot-kehote](../../../02-prompt-engineering)
  - [Ajatusketju](../../../02-prompt-engineering)
  - [Roolipohjainen kehote](../../../02-prompt-engineering)
  - [Kehotepohjat](../../../02-prompt-engineering)
- [Edistyneet mallit](../../../02-prompt-engineering)
- [Olemassa olevien Azure-resurssien käyttäminen](../../../02-prompt-engineering)
- [Sovelluksen kuvakaappaukset](../../../02-prompt-engineering)
- [Mallien tutkiminen](../../../02-prompt-engineering)
  - [Alhainen vs korkea innokkuus](../../../02-prompt-engineering)
  - [Tehtävän suoritus (työkalujen esipuheet)](../../../02-prompt-engineering)
  - [Itsepeilaava koodi](../../../02-prompt-engineering)
  - [Rakenteellinen analyysi](../../../02-prompt-engineering)
  - [Monivaiheinen keskustelu](../../../02-prompt-engineering)
  - [Vaiheittainen päättely](../../../02-prompt-engineering)
  - [Rajoitettu tuloste](../../../02-prompt-engineering)
- [Mitä todella opit](../../../02-prompt-engineering)
- [Seuraavat askeleet](../../../02-prompt-engineering)

## Mitä opit

<img src="../../../translated_images/fi/what-youll-learn.c68269ac048503b2.webp" alt="Mitä opit" width="800"/>

Edellisessä moduulissa näit, kuinka muisti mahdollistaa keskustelevaa tekoälyä ja käytit GitHub-malleja peruskeskusteluihin. Nyt keskitymme siihen, miten esität kysymyksiä – eli itse kehotteisiin – käyttämällä Azure OpenAI:n GPT-5.2:ta. Se, miten rakennat kehotteesi, vaikuttaa dramaattisesti saamiesi vastausten laatuun. Aloitamme perustavanlaatuisista kehotetekniikoista ja siirrymme sitten kahdeksaan edistyneeseen malliin, jotka hyödyntävät GPT-5.2:n koko potentiaalin.

Käytämme GPT-5.2:ta koska se tarjoaa päättelyn hallinnan – voit kertoa mallille, kuinka paljon sen tulee ajatella ennen vastaamista. Tämä tekee eri kehotustrategioista selkeämpiä ja auttaa ymmärtämään, milloin käyttää mitäkin lähestymistapaa. Lisäksi hyödymme Azuren vähentyneistä rajoitteista GPT-5.2:n kanssa verrattuna GitHub-malleihin.

## Esivaatimukset

- Moduuli 01 suoritettu (Azure OpenAI -resurssit käyttöönotettu)
- `.env`-tiedosto juurihakemistossa Azuren tunnistetiedoilla (luotu `azd up` -komennolla moduulissa 01)

> **Huom:** Jos et ole vielä suorittanut moduulia 01, noudata ensin siellä olevia käyttöönotto-ohjeita.

## Kehota insinöörityön ymmärrys

<img src="../../../translated_images/fi/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Mitä on kehotainsinöörityö?" width="800"/>

Kehota insinöörityö tarkoittaa syötteen suunnittelua siten, että saat johdonmukaisesti tarvitsemiasi tuloksia. Kyse ei ole vain kysymysten esittämisestä – kyse on pyyntöjen rakenteistamisesta niin, että malli ymmärtää tarkalleen mitä haluat ja miten sen tulee vastata.

Ajattele sitä kuin antaisit ohjeita kollegalle. ”Korjaa virhe” on epämääräinen. ”Korjaa UserService.java:n rivillä 45 oleva null pointer -poikkeus lisäämällä null-tarkistus” on tarkka. Kielenmallit toimivat samalla tavalla – tarkkuus ja rakenne ovat tärkeitä.

<img src="../../../translated_images/fi/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Miten LangChain4j sopii kokonaisuuteen" width="800"/>

LangChain4j tarjoaa infrastruktuurin — malliyhteydet, muistin ja viestityypit — kun taas kehomallit ovat vain huolellisesti rakennettua tekstiä, jonka lähetät tämän infrastruktuurin läpi. Keskeiset rakennuspalikat ovat `SystemMessage` (asetetaan tekoälyn käyttäytyminen ja rooli) ja `UserMessage` (kantaa varsinaisen pyyntösi).

## Kehota insinöörityön perusteet

<img src="../../../translated_images/fi/five-patterns-overview.160f35045ffd2a94.webp" alt="Viiden kehotemallin yleiskatsaus" width="800"/>

Ennen kuin sukellamme tämän moduulin edistyneisiin malleihin, käydään läpi viisi perustavaa kehotustekniikkaa. Nämä ovat rakennuspalikoita, jotka jokaisen kehotainsinöörin tulisi tuntea. Jos olet jo tehnyt [Pika-aloitusmoduulin](../00-quick-start/README.md#2-prompt-patterns), olet nähnyt nämä käytännössä — tässä niiden konseptuaalinen kehys.

### Zero-Shot-kehote

Yksinkertaisin lähestymistapa: anna mallille suora ohje ilman esimerkkejä. Malli perustuu täysin koulutukseensa ymmärtääkseen ja suorittaakseen tehtävän. Tämä toimii hyvin suoraviivaisten pyyntöjen kanssa, joissa odotettu käyttäytyminen on ilmeistä.

<img src="../../../translated_images/fi/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot-kehote" width="800"/>

*Suora ohje ilman esimerkkejä — malli päättelee tehtävän pelkästään ohjeesta*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Vastaus: "Positiivinen"
```

**Milloin käyttää:** Yksinkertaisiin luokituksiin, suoraviivaisiin kysymyksiin, käännöksiin tai mihin tahansa tehtävään, jonka malli osaa hoitaa ilman lisäohjeistusta.

### Few-Shot-kehote

Tarjoa esimerkkejä, jotka demonstroivat mallille toivottua kaavaa. Malli oppii odotetun syöte-tuotos-muodon esimerkeistäsi ja soveltaa sitä uusiin syötteisiin. Tämä parantaa merkittävästi johdonmukaisuutta tehtävissä, joissa haluttu muoto tai käyttäytyminen ei ole selvä.

<img src="../../../translated_images/fi/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot-kehote" width="800"/>

*Oppiminen esimerkeistä — malli tunnistaa kaavan ja soveltaa sitä uusiin syötteisiin*

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

**Milloin käyttää:** Räätälöityihin luokituksiin, johdonmukaiseen muotoiluun, alakohtaisiin tehtäviin tai kun zero-shot-tulokset ovat epäjohdonmukaisia.

### Ajatusketju

Pyydä mallia näyttämään päättelynsä vaihe vaiheelta. Sen sijaan, että se hyppäisi suoraan vastaukseen, malli purkaa ongelman osiin ja työskentelee jokaisen osan läpi eksplisiittisesti. Tämä parantaa tarkkuutta matematiikassa, logiikassa ja monivaiheisissa päättelytehtävissä.

<img src="../../../translated_images/fi/chain-of-thought.5cff6630e2657e2a.webp" alt="Ajatusketju-kehote" width="800"/>

*Vaiheittainen päättely — monimutkaisten ongelmien jakaminen selkeisiin loogisiin askeleisiin*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Malli näyttää: 15 - 8 = 7, sitten 7 + 12 = 19 omenaa
```

**Milloin käyttää:** Matematiikan tehtävissä, logiikkapalapeleissä, virheenkorjauksessa tai muissa tehtävissä, joissa päättelyprosessin näyttäminen parantaa tarkkuutta ja luottamusta.

### Roolipohjainen kehote

Aseta tekoälylle persona tai rooli ennen kysymyksesi esittämistä. Tämä tarjoaa kontekstin, joka muokkaa vastausten sävyä, syvyyttä ja painopistettä. ”Ohjelmistoarkkitehti” antaa eri neuvon kuin ”juniori-kehittäjä” tai ”tietoturva-auditoija”.

<img src="../../../translated_images/fi/role-based-prompting.a806e1a73de6e3a4.webp" alt="Roolipohjainen kehote" width="800"/>

*Kontekstin ja roolin asettaminen — samaan kysymykseen saadaan eri vastaus valitun roolin mukaan*

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

**Milloin käyttää:** Koodikatselmuksissa, opetuksessa, alakohtaisessa analyysissä tai kun tarvitset vastauksia, jotka on räätälöity tiettyyn asiantuntemuksen tasoon tai näkökulmaan.

### Kehotepohjat

Luo uudelleen käytettäviä kehotteita muuttujien paikkamerkeillä. Sen sijaan, että kirjoittaisit uuden kehotteen joka kerta, määrittele pohja kerran ja täytä eri arvot. LangChain4j:n `PromptTemplate`-luokka tekee tämän helpoksi `{{variable}}`-syntaksilla.

<img src="../../../translated_images/fi/prompt-templates.14bfc37d45f1a933.webp" alt="Kehotepohjat" width="800"/>

*Uudelleenkäytettävät kehote-pohjat, joissa on muuttujapaikkoja — yksi pohja, monta käyttötapaa*

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

**Milloin käyttää:** Toistuvissa kyselyissä eri syötteillä, eräkäsittelyssä, uudelleenkäytettävien tekoälytyönkulkujen rakentamisessa tai missä tahansa tilanteessa, jossa kehotteen rakenne pysyy samana mutta data muuttuu.

---

Nämä viisi perustekniikkaa antavat sinulle vankan työkalupakin useimpiin kehotustehtäviin. Tämän moduulin muut mallit perustuvat niihin ja sisältävät **kahdeksan edistyneen mallia**, jotka hyödyntävät GPT-5.2:n päättelykontrollia, itsearviointia ja rakenteellista tulostetta.

## Edistyneet mallit

Kun perusteet on käyty läpi, siirrytään kahdeksaan edistyneeseen malliin, jotka tekevät tästä moduulista ainutlaatuisen. Kaikki ongelmat eivät vaadi samaa lähestymistapaa. Jotkut kysymykset vaativat nopeita vastauksia, toiset syvällistä analyysiä. Jotkut vaativat näkyvää päättelyä, toiset vain tuloksia. Alla jokainen malli on optimoitu eri tilanteeseen — ja GPT-5.2:n päättelyhallinta tekee eroista jopa vielä selvempiä.

<img src="../../../translated_images/fi/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Kahdeksan kehotemallia" width="800"/>

*Kahdeksan kehotteiden insinöörityömallin yleiskatsaus ja käyttötapaukset*

<img src="../../../translated_images/fi/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Päättelykontrolli GPT-5.2:lla" width="800"/>

*GPT-5.2:n päättelykontrolli antaa sinun määräytyä, kuinka paljon mallin tulee ajatella – nopeista suorista vastauksista syvällisiin selvityksiin*

**Alhainen innokkuus (Nopea & Kohdistettu)** – Yksinkertaisiin kysymyksiin, joissa haluat nopeasti suoraviivaiset vastaukset. Malli päättelee minimissään – maksimissaan 2 askelta. Käytä tätä laskelmissa, hakuissa tai suorissa kysymyksissä.

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Tutki GitHub Copilotilla:** Avaa [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ja kysy:
> - "Mikä ero on alhaisen ja korkean innokkuuden kehotemalleilla?"
> - "Miten XML-tunnisteet kehotteissa auttavat AI:n vastauksen rakenteistamisessa?"
> - "Milloin käytän itsepeilaavia malleja vs. suoria ohjeita?"

**Korkea innokkuus (Syvä & Perusteellinen)** – Monimutkaisiin ongelmiin, joissa haluat kattavan analyysin. Malli tutkii perusteellisesti ja näyttää yksityiskohtaisen päättelyn. Käytä tätä järjestelmäsuunnittelussa, arkkitehtuuripäätöksissä tai monimutkaisessa tutkimuksessa.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Tehtävän suoritus (Vaiheittainen eteneminen)** – Monivaiheisiin työnkulkuihin. Malli tarjoaa etukäteissuunnitelman, kertoo jokaisen vaiheen etenemisen aikana ja antaa lopuksi yhteenvedon. Käytä tätä migraatioissa, toteutuksissa tai missä tahansa monivaiheisessa prosessissa.

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

Ajatusketjun kehotteessa pyydetään mallia näyttämään päättelyprosessinsa, mikä parantaa tarkkuutta monimutkaisissa tehtävissä. Vaiheittainen erittely auttaa sekä ihmisiä että tekoälyä ymmärtämään logiikkaa.

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatissa:** Kysy tästä mallista:
> - "Miten mukauttaisin tehtävän suoritusmallia pitkäkestoisiin operaatioihin?"
> - "Mitkä ovat parhaita käytäntöjä työkalujen esipuheiden rakenteistamisessa tuotantosovelluksissa?"
> - "Miten voin kaapata ja näyttää välivaiheen edistymisraportit käyttöliittymässä?"

<img src="../../../translated_images/fi/task-execution-pattern.9da3967750ab5c1e.webp" alt="Tehtävän suoritusmalli" width="800"/>

*Suunnittele → Suorita → Yhteenveto -työnkulku monivaiheisille tehtäville*

**Itsepeilaava koodi** – Tuottaa tuotantotason koodia. Malli generoi koodin noudattaen tuotantostandardeja ja asianmukaista virheenkäsittelyä. Käytä tätä uusien ominaisuuksien tai palveluiden rakentamiseen.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fi/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Itsepeilausjakso" width="800"/>

*Iteratiivinen parantamissykli — luo, arvioi, tunnista ongelmat, paranna, toista*

**Rakenteellinen analyysi** – Johdonmukaiseen arviointiin. Malli arvioi koodia kiinteän viitekehyksen mukaisesti (oikeellisuus, käytännöt, suorituskyky, tietoturva, ylläpidettävyys). Käytä tätä koodikatselmuksissa tai laadunarvioinneissa.

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatissa:** Kysy rakenteellisesta analyysistä:
> - "Miten voin räätälöidä analyysikehystä erilaisiin koodikatselmuksiin?"
> - "Mikä on paras tapa jäsentää ja käyttää rakenteellista tulostetta ohjelmallisesti?"
> - "Miten varmistetaan johdonmukaiset vakavuustasot eri katselukertojen välillä?"

<img src="../../../translated_images/fi/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Rakenteellisen analyysin malli" width="800"/>

*Keinot johdonmukaisille koodikatselmuksille vakavuustasoineen*

**Monivaiheinen keskustelu** – Keskusteluihin, jotka tarvitsevat kontekstia. Malli muistaa aiemmat viestit ja rakentaa niiden päälle. Käytä vuorovaikutteisissa tukisessioissa tai monimutkaisissa kysymys-vastaus-tilanteissa.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/fi/context-memory.dff30ad9fa78832a.webp" alt="Keskustelukonteksti ja muisti" width="800"/>

*Miten keskustelukonteksti kasautuu useiden kierrosten aikana kunnes tokeneiden enimmäismäärä saavutetaan*

**Vaiheittainen päättely** – Ongelmille, jotka vaativat näkyvää logiikkaa. Malli näyttää eksplisiittisen päättelyn jokaiselle askeleelle. Käytä matematiikan tehtäviin, logiikkapalapeleihin tai kun haluat ymmärtää päättelyprosessin.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fi/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Vaiheittaisen päättelyn malli" width="800"/>

*Ongelmien pilkkominen selkeiksi loogisiksi askeliksi*

**Rajoitettu tuloste** – Vastauksille, joilla on erityisiä muoto- ja pituusvaatimuksia. Malli noudattaa tiukasti muoto- ja pituussääntöjä. Käytä tiivistelmiin tai kun tarvitset täsmällistä tulostestruktuuria.

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

<img src="../../../translated_images/fi/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Rajoitetun tulosteen malli" width="800"/>

*Erityisten muoto-, pituus- ja rakennevaatimusten noudattaminen*

## Olemassa olevien Azure-resurssien käyttäminen

**Tarkista käyttöönotto:**

Varmista, että `.env`-tiedosto on juurihakemistossa Azuren tunnistetiedoilla (luotu moduulin 01 aikana):
```bash
cat ../.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Käynnistä sovellus:**

> **Huom:** Jos olet jo käynnistänyt kaikki sovellukset käyttämällä `./start-all.sh` -skriptiä moduulissa 01, tämä moduuli on jo käynnissä portissa 8083. Voit hypätä alla olevat käynnistyskomennot ja siirtyä suoraan osoitteeseen http://localhost:8083.

**Vaihtoehto 1: Spring Boot Dashboardin käyttö (suositeltu VS Code -käyttäjille)**

Kehityssäiliö sisältää Spring Boot Dashboard -laajennuksen, joka tarjoaa visuaalisen käyttöliittymän kaikkien Spring Boot -sovellusten hallintaan. Löydät sen VS Coden vasemman laidan Aktiviteettipalkista (etsi Spring Boot -ikonia).

Spring Boot Dashboardista voit:
- Näyttää kaikki työtilan Spring Boot -sovellukset
- Käynnistää/pysäyttää sovelluksia yhdellä napsautuksella
- Tarkastella sovellusten lokitietoja reaaliajassa
- Seurata sovelluksen tilaa
Yksinkertaisesti napsauta "prompt-engineering" -kohdan vieressä olevaa toistopainiketta aloittaaksesi tämän moduulin, tai käynnistä kaikki moduulit kerralla.

<img src="../../../translated_images/fi/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot -hallintapaneeli" width="400"/>

**Vaihtoehto 2: Kuoriskriptien käyttäminen**

Käynnistä kaikki verkkosovellukset (moduulit 01-04):

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

Molemmat skriptit lataavat automaattisesti ympäristömuuttujat juurihakemiston `.env`-tiedostosta ja rakentavat JAR-tiedostot, jos niitä ei ole vielä olemassa.

> **Huom:** Jos haluat rakentaa kaikki moduulit manuaalisesti ennen käynnistystä:
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

Avaa selaimessasi osoite http://localhost:8083.

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

## Sovelluksen kuvakaappaukset

<img src="../../../translated_images/fi/dashboard-home.5444dbda4bc1f79d.webp" alt="Hallintapaneelin etusivu" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Päähallintapaneeli, joka näyttää kaikki 8 kehotemallinnuskuviota niiden ominaisuuksineen ja käyttötapauksineen*

## Kuvioiden tutkiminen

Verkkokäyttöliittymä antaa sinun kokeilla erilaisia kehotustrategioita. Jokainen kuvio ratkaisee eri ongelmia – kokeile niitä nähdäksesi, milloin kukin lähestymistapa toimii parhaiten.

### Matala vs Korkea Tarkoituksenmukaisuus

Kysy yksinkertainen kysymys, kuten "Mikä on 15% luvusta 200?" käyttäen Matalaa Tarkoituksenmukaisuutta. Saat välittömän, suoran vastauksen. Kysy sitten monimutkaisempi kysymys, kuten "Suunnittele välimuististrategia suuren liikenteen API:lle" käyttäen Korkeaa Tarkoituksenmukaisuutta. Katso, miten malli hidastuu ja antaa yksityiskohtaiset perustelut. Sama malli, sama kysymyksen rakenne – mutta kehotus kertoo sille, kuinka paljon ajattelua sen pitää tehdä.

<img src="../../../translated_images/fi/low-eagerness-demo.898894591fb23aa0.webp" alt="Matala Tarkoituksenmukaisuus Demo" width="800"/>

*Nopea laskutoimitus minimiä älyä käyttäen*

<img src="../../../translated_images/fi/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Korkea Tarkoituksenmukaisuus Demo" width="800"/>

*Kattava välimuististrategia (2.8MB)*

### Tehtävän suoritus (Työkalun alkuosat)

Monivaiheiset työnkulut hyötyvät etukäteissuunnittelusta ja etenemisen kertomisesta. Malli selostaa, mitä se aikoo tehdä, kertoo jokaisen vaiheen yksityiskohdat ja lopuksi tiivistää tulokset.

<img src="../../../translated_images/fi/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Tehtävän suoritus Demo" width="800"/>

*REST-päätepisteen luominen vaihe vaiheelta selostettuna (3.9MB)*

### Itsearvioiva Koodi

Kokeile "Luo sähköpostin validointipalvelu". Sen sijaan, että malli vain generoisi koodin ja pysähtyisi, se luo, arvioi laatukriteerien mukaan, tunnistaa heikkoudet ja parantaa koodia. Näet, kuinka se toistaa prosessia, kunnes koodi täyttää tuotantostandardit.

<img src="../../../translated_images/fi/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Itsearvioiva Koodi Demo" width="800"/>

*Täydellinen sähköpostin validointipalvelu (5.2MB)*

### Rakenteellinen Analyysi

Koodikatselmukset tarvitsevat johdonmukaisia arviointikehyksiä. Malli analysoi koodin kiinteiden kategorioiden (oikeellisuus, käytännöt, suorituskyky, turvallisuus) mukaan ja käyttää vakavuustasoja.

<img src="../../../translated_images/fi/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Rakenteellinen Analyysi Demo" width="800"/>

*Kehyspohjainen koodikatselmus*

### Monivaiheinen Keskustelu

Kysy "Mikä on Spring Boot?" ja heti sen jälkeen "Näytä minulle esimerkki". Malli muistaa ensimmäisen kysymyksesi ja antaa sinulle nimenomaan Spring Boot -esimerkin. Ilman muistia toinen kysymys olisi liian epämääräinen.

<img src="../../../translated_images/fi/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Monivaiheinen Keskustelu Demo" width="800"/>

*Kontekstin säilyttäminen kysymysten välillä*

### Askel Askeleelta -Päättely

Valitse matemaattinen ongelma ja kokeile sitä sekä Askel Askeleelta -Päättelyllä että Matalalla Tarkoituksenmukaisuudella. Matala tarkoituksenmukaisuus antaa vain nopean vastauksen – nopea mutta hämärä. Askel askeleelta näyttää jokaisen laskutoimituksen ja päätöksen.

<img src="../../../translated_images/fi/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Askel Askeleelta -Päättely Demo" width="800"/>

*Matemaattinen ongelma selkeillä vaiheilla*

### Rajattu Tulostus

Kun tarvitset tiettyjä formaatteja tai sanamääriä, tämä kuvio varmistaa tiukan noudattamisen. Kokeile luoda yhteenveto, jossa on tasan 100 sanaa luettelomuodossa.

<img src="../../../translated_images/fi/constrained-output-demo.567cc45b75da1633.webp" alt="Rajattu Tulostus Demo" width="800"/>

*Koneoppimisen yhteenveto formaatin hallinnalla*

## Mitä Oikeastaan Opit

**Päätelmien tekemisen vaivannäkö muuttaa kaiken**

GPT-5.2 antaa sinun hallita laskentatehoa kehotteillasi. Matala vaivannäkö tarkoittaa nopeita vastauksia minimaalisella pohdinnalla. Korkea vaivannäkö tarkoittaa, että malli käyttää aikaa syvälliseen ajatteluun. Olet oppimassa sovittamaan vaivannäön tehtävän monimutkaisuuteen – älä tuhlaa aikaa yksinkertaisiin kysymyksiin, mutta älä myöskään kiirehdi monimutkaisia päätöksiä.

**Rakenne ohjaa toimintaa**

Huomasitko XML-tunnisteet kehotteissa? Ne eivät ole koristeita. Mallit noudattavat rakenteellisia ohjeita luotettavammin kuin vapaamuotoista tekstiä. Kun tarvitset monivaiheisia prosesseja tai monimutkaista logiikkaa, rakenne auttaa mallia seuraamaan, missä se on ja mitä seuraavaksi tulee.

<img src="../../../translated_images/fi/prompt-structure.a77763d63f4e2f89.webp" alt="Kehotteen rakenne" width="800"/>

*Hyvin rakennetun kehotteen anatomia, jossa on selkeät osiot ja XML-tyylinen järjestely*

**Laatu itsearvioinnin kautta**

Itsearvioivat kuviot toimivat tekemällä laatukriteerit eksplisiittisiksi. Sen sijaan, että toivoisit mallin "tekevän oikein", kerrot sille tarkalleen, mitä "oikein" tarkoittaa: oikea logiikka, virheenkäsittely, suorituskyky, turvallisuus. Malli voi sitten arvioida omaa tuotostaan ja parantaa sitä. Tämä muuttaa koodin generoinnin arpajaisista prosessiksi.

**Konteksti on rajallinen**

Monivaiheiset keskustelut toimivat sisällyttämällä keskusteluhistorian jokaiseen pyyntöön. Mutta on olemassa raja – jokaisella mallilla on maksimimäärä tokenia. Keskustelujen kasvaessa tarvitset strategioita pitää olennaiset kontekstit ilman, että saavut rajan. Tämä moduuli näyttää, miten muisti toimii; myöhemmin opit, milloin tiivistää, milloin unohtaa ja milloin hakea.

## Seuraavat Askeleet

**Seuraava moduuli:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigointi:** [← Edellinen: Moduuli 01 - Johdanto](../01-introduction/README.md) | [Takaisin pääsivulle](../README.md) | [Seuraava: Moduuli 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Pyrimme tarkkuuteen, mutta huomioithan, että automaattikäännöksissä voi esiintyä virheitä tai epätarkkuuksia. Alkuperäinen asiakirja sen alkuperäiskielellä on virallinen lähde. Tärkeissä tiedoissa suositellaan ammattimaista ihmiskäännöstä. Emme vastaa tämän käännöksen käytöstä johtuvista väärinymmärryksistä tai virhetulkinnasta.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->