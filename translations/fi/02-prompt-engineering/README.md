# Module 02: Prompt Engineering with GPT-5.2

## Table of Contents

- [What You'll Learn](../../../02-prompt-engineering)
- [Prerequisites](../../../02-prompt-engineering)
- [Understanding Prompt Engineering](../../../02-prompt-engineering)
- [Prompt Engineering Fundamentals](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Advanced Patterns](../../../02-prompt-engineering)
- [Using Existing Azure Resources](../../../02-prompt-engineering)
- [Application Screenshots](../../../02-prompt-engineering)
- [Exploring the Patterns](../../../02-prompt-engineering)
  - [Low vs High Eagerness](../../../02-prompt-engineering)
  - [Task Execution (Tool Preambles)](../../../02-prompt-engineering)
  - [Self-Reflecting Code](../../../02-prompt-engineering)
  - [Structured Analysis](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Step-by-Step Reasoning](../../../02-prompt-engineering)
  - [Constrained Output](../../../02-prompt-engineering)
- [What You're Really Learning](../../../02-prompt-engineering)
- [Next Steps](../../../02-prompt-engineering)

## What You'll Learn

<img src="../../../translated_images/fi/what-youll-learn.c68269ac048503b2.webp" alt="Mitä opit" width="800"/>

Edellisessä moduulissa näit, miten muisti mahdollistaa keskustelevan tekoälyn, ja käytit GitHub-malleja perusvuorovaikutuksiin. Nyt keskitymme siihen, miten kysyt kysymyksiä — eli itse kehotteisiin — käyttäen Azure OpenAI:n GPT-5.2:ta. Tapa, jolla jäsennät kehotteesi, vaikuttaa ratkaisevasti saamaasi vastausten laatuun. Aloitamme perustekniikoiden tarkastelulla ja siirrymme sitten kahdeksaan edistyneeseen malliin, jotka hyödyntävät GPT-5.2:n ominaisuuksia täysimääräisesti.

Käytämme GPT-5.2:ta, koska se tuo mukanaan päättelyohjauksen – voit kertoa mallille, kuinka paljon ajattelua sen tulisi tehdä ennen vastaamista. Tämä tekee erilaisista kehotetavoista selkeämpiä ja auttaa ymmärtämään, milloin käyttää kutakin lähestymistapaa. Hyödymme myös Azuren pienemmistä käyttörajoituksista GPT-5.2:lle verrattuna GitHub-malleihin.

## Prerequisites

- Suoritettu Moduuli 01 (Azure OpenAI -resurssit otettu käyttöön)
- `.env`-tiedosto juurihakemistossa, jossa Azure-tunnistetiedot (luotu `azd up`:lla Moduuli 01:ssa)

> **Huom:** Jos et ole vielä suorittanut Moduulia 01, noudata ensin siellä olevia käyttöönotto-ohjeita.

## Understanding Prompt Engineering

<img src="../../../translated_images/fi/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Mikä on kehotetekniikka?" width="800"/>

Kehotetekniikka tarkoittaa syötetekstin suunnittelua siten, että saat jatkuvasti haluamasi tulokset. Kyse ei ole pelkästään kysymysten esittämisestä — vaan pyyntöjen jäsentelystä niin, että malli ymmärtää täsmälleen, mitä haluat ja miten sen tulee toimittaa.

Ajattele sitä niin kuin antaisit ohjeita kollegalle. "Korjaa bugi" on epämääräinen. "Korjaa null pointer exception UserService.java-tiedoston rivillä 45 lisäämällä null-tarkistus" on tarkempi. Kielimallit toimivat samalla tavalla – täsmällisyydellä ja rakenteella on merkitystä.

<img src="../../../translated_images/fi/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Miten LangChain4j sopii" width="800"/>

LangChain4j tarjoaa infrastruktuurin — malliyhteydet, muistin ja viestityypit — kun taas kehotemallit ovat juuri huolellisesti jäsennettyjä tekstejä, jotka lähetetään tämän infrastruktuurin läpi. Keskeiset rakennuspalikat ovat `SystemMessage` (joka asettaa tekoälyn käyttäytymisen ja roolin) ja `UserMessage` (joka kantaa varsinaisen pyyntösi).

## Prompt Engineering Fundamentals

<img src="../../../translated_images/fi/five-patterns-overview.160f35045ffd2a94.webp" alt="Viiden kehotetekniikkamallin yleiskuva" width="800"/>

Ennen kuin sukellamme tämän moduulin edistyneisiin malleihin, käydään läpi viisi perustekniikkaa. Nämä ovat rakennuspalikoita, jotka jokaisen kehotetekniikan asiantuntijan tulisi tuntea. Jos olet jo työskennellyt läpi [Nopean aloituksen moduulin](../00-quick-start/README.md#2-prompt-patterns), olet nähnyt näitä käytännössä — tässä niille konseptuaalinen kehys.

### Zero-Shot Prompting

Yksinkertaisin lähestymistapa: anna mallille suora ohje ilman esimerkkejä. Malli luottaa täysin koulutukseensa ymmärtääkseen ja suorittaakseen tehtävän. Tämä toimii hyvin suoraviivaisten pyyntöjen kanssa, joissa odotettu käyttäytyminen on ilmeistä.

<img src="../../../translated_images/fi/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Suora ohje ilman esimerkkejä – malli päättelee tehtävän pelkän ohjeen perusteella*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Vastaus: "Positiivinen"
```

**Milloin käyttää:** Yksinkertaisiin luokitteluihin, suoriin kysymyksiin, käännöksiin tai mihin tahansa tehtävään, jonka malli osaa hoitaa ilman lisäohjausta.

### Few-Shot Prompting

Anna esimerkkejä, jotka näyttävät mallille halutun kaavan. Malli oppii odotetun syöte-tuotos-muodon esimerkkiesi avulla ja soveltaa sitä uusiin syötteisiin. Tämä parantaa merkittävästi johdonmukaisuutta tehtävissä, joissa haluttu muoto tai käyttäytyminen ei ole ilmeistä.

<img src="../../../translated_images/fi/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Oppiminen esimerkeistä – malli tunnistaa kaavan ja soveltaa sitä uusiin syötteisiin*

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

**Milloin käyttää:** Räätälöityihin luokitteluihin, johdonmukaiseen muotoiluun, alakohtaisiin tehtäviin tai kun zero-shot-tulokset ovat epävakaita.

### Chain of Thought

Pyydä mallia näyttämään päättelynsä askel askeleelta. Sen sijaan että se hyppäisi suoraan vastaukseen, malli hajottaa ongelman ja käy läpi jokaisen osan selkeästi. Tämä parantaa tarkkuutta matematiikan, logiikan ja monivaiheisten päättelytehtävien kohdalla.

<img src="../../../translated_images/fi/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Askeltainen päättely – monimutkaisten ongelmien jakaminen selkeisiin loogisiin vaiheisiin*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Malli näyttää: 15 - 8 = 7, sitten 7 + 12 = 19 omenaa
```

**Milloin käyttää:** Matematiikkaongelmiin, logiikkapulmiin, virheenkorjaukseen tai mihin tahansa tehtävään, jossa päättelyprosessin näyttäminen parantaa tarkkuutta ja luottamusta.

### Role-Based Prompting

Aseta tekoälylle persoona tai rooli ennen kysymyksen esittämistä. Tämä tarjoaa kontekstin, joka muokkaa vastauksen sävyä, syvyyttä ja painotusta. "Ohjelmistoarkkitehti" antaa erilaisia neuvoja kuin "juniori-kehittäjä" tai "tietoturvatarkastaja".

<img src="../../../translated_images/fi/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Kontekstin ja persoonan asettaminen – samaan kysymykseen saadaan erilainen vastaus riippuen määritellystä roolista*

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

**Milloin käyttää:** Koodikatselmuksiin, tutorointiin, alakohtaiseen analyysiin tai kun tarvitset vastauksia, jotka on räätälöity tietyn asiantuntemustason tai näkökulman mukaan.

### Prompt Templates

Luo uudelleenkäytettäviä kehotteita muuttujatilojen avulla. Sen sijaan että kirjoitat uuden kehotteen joka kerta, määrittele malli kerran ja täytä siihen erilaisia arvoja. LangChain4j:n `PromptTemplate`-luokka helpottaa tätä `{{variable}}`-syntaksilla.

<img src="../../../translated_images/fi/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Uudelleenkäytettävät kehotteet muuttujatilojen kanssa – yksi malli, monta käyttötarkoitusta*

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

**Milloin käyttää:** Toistuvissa kyselyissä eri syötteillä, eräkäsittelyssä, uudelleenkäytettävien tekoälytyönkulkujen rakentamisessa tai missä tahansa tilanteessa, jossa kehotteen rakenne pysyy samana mutta data vaihtuu.

---

Nämä viisi perustekniikkaa antavat sinulle hyvän työkalupakin useimpiin kehotteiden luontitehtäviin. Loppuosa tästä moduulista rakentuu niiden päälle kahdeksalla edistyneellä mallilla, jotka hyödyntävät GPT-5.2:n päättelyohjausta, itsearviointia ja jäsenneltyjä tulosteita.

## Advanced Patterns

Perusteet hallussa, siirrymme kahdeksaan edistyneeseen malliin, jotka tekevät tästä moduulista ainutlaatuisen. Kaikki ongelmat eivät tarvitse samaa lähestymistapaa. Jotkut kysymykset vaativat nopeita vastauksia, toiset syvällistä pohdintaa. Joissain tarvitaan näkyvää päättelyä, toisissa pelkkä tulos riittää. Alla olevat mallit on optimoitu eri tilanteisiin — ja GPT-5.2:n päättelyohjaus tekee näistä eroista entistä selvempiä.

<img src="../../../translated_images/fi/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Kahdeksan kehotetekniikkamallia" width="800"/>

*Yleiskuva kahdeksasta kehotetekniikkamallista ja niiden käyttötapauksista*

<img src="../../../translated_images/fi/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Päättelyohjaus GPT-5.2:lla" width="800"/>

*GPT-5.2:n päättelyohjaus antaa sinun määrittää, kuinka paljon mallin tulisi ajatella — nopeista suorista vastauksista syvälliseen pohdintaan*

<img src="../../../translated_images/fi/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Vertailu päättelyn vaivasta" width="800"/>

*Alhainen innokkuus (nopea, suora) vs Korkea innokkuus (perusteellinen, tutkiva) päättelytavat*

**Alhainen innokkuus (nopea & kohdennettu)** – Yksinkertaisiin kysymyksiin, joissa haluat nopeita ja suoria vastauksia. Malli tekee minimaalisen päättelyn – korkeintaan kaksi askelta. Käytä tätä laskutoimituksiin, hakuun tai suoraviivaisiin kysymyksiin.

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

> 💡 **Tutustu GitHub Copilotilla:** Avaa [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ja kysy:
> - "Mikä ero on alhaisen ja korkean innokkuuden kehotemallien välillä?"
> - "Miten XML-tunnisteet kehotteissa auttavat jäsentämään tekoälyn vastausta?"
> - "Milloin kannattaa käyttää itseheijastavia malleja verrattuna suoriin ohjeisiin?"

**Korkea innokkuus (syvä ja perusteellinen)** – Monimutkaisiin ongelmiin, joissa haluat kattavan analyysin. Malli tutkii perusteellisesti ja näyttää yksityiskohtaisen päättelyn. Käytä tätä järjestelmäsuunnittelussa, arkkitehtuuripäätöksissä tai monimutkaisissa tutkimuksissa.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Tehtävän suoritus (askel askeleelta eteneminen)** – Monivaiheisiin työnkulkuihin. Malli tarjoaa aluksi suunnitelman, kuvailee jokaisen vaiheen työskennellessään ja antaa lopuksi yhteenvedon. Käytä tätä migraatioihin, toteutuksiin tai mihin tahansa monivaiheiseen prosessiin.

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

Chain-of-Thought-kehotus pyytää mallia näyttämään päättelyprosessinsa, mikä parantaa tarkkuutta monimutkaisissa tehtävissä. Askel askeleelta tapahtuva jäsentely auttaa ihmisiä ja tekoälyä ymmärtämään logiikan.

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatilla:** Kysy tästä mallista:
> - "Miten mukauttaisin tehtävän suoritusmallia pitkäkestoisiin operaatioihin?"
> - "Mitkä ovat parhaat käytännöt työkalujen esilausekkeiden jäsentelyssä tuotantosovelluksissa?"
> - "Miten voin kaapata ja näyttää väliarviointeja käyttöliittymässä?"

<img src="../../../translated_images/fi/task-execution-pattern.9da3967750ab5c1e.webp" alt="Tehtävän suorituksen malli" width="800"/>

*Suunnittele → Suorita → Yhteenveto -työnkulku monivaiheisille tehtäville*

**Itseheijastava koodi** – Tuotantolaatuista koodia tuottava malli. Malli generoi koodia noudattaen tuotanto- ja virheenkäsittelystandardeja. Käytä tätä uusien ominaisuuksien tai palveluiden rakentamiseen.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fi/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Itseheijastuskierto" width="800"/>

*Iteratiivisen parantamisen sykli – generoi, arvioi, tunnista ongelmat, paranna, toista*

**Jäsennelty analyysi** – Johdonmukainen arviointi. Malli tarkistaa koodin käyttämällä kiinteää kehystä (oikeellisuus, käytännöt, suorituskyky, turvallisuus, ylläpidettävyys). Käytä tätä koodikatselmuksiin tai laatutarkastuksiin.

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

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatilla:** Kysy jäsennellystä analyysistä:
> - "Miten voin räätälöidä analyysikehyksen eri tyyppisiin koodikatselmuksiin?"
> - "Mikä on paras tapa jäsentää ja käsitellä rakenteellista tulostetta ohjelmallisesti?"
> - "Miten varmistaa johdonmukaiset vakavuustasot eri katselmuskerroilla?"

<img src="../../../translated_images/fi/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Jäsennellyn analyysin malli" width="800"/>

*Kehys johdonmukaisille koodikatselmuksille vakavuustasoineen*

**Monikierroksinen keskustelu** – Keskustelut, jotka tarvitsevat kontekstia. Malli muistaa aiemmat viestit ja rakentaa niiden päälle. Käytä tätä interaktiivisiin tukisessioihin tai monimutkaiseen kysymys- ja vastaustilanteeseen.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/fi/context-memory.dff30ad9fa78832a.webp" alt="Keskustelukontekstin muisti" width="800"/>

*Kuinka keskustelun konteksti kasautuu useiden kierrosten aikana token-rajan saavuttamiseen saakka*

**Askeltainen päättely** – Ongelmille, jotka vaativat näkyvää logiikkaa. Malli näyttää eksplisiittisen päättelyn jokaiselle askeleelle. Käytä tätä matematiikan ongelmiin, logiikkapeleihin tai tilanteisiin, joissa päättelyprosessin ymmärtäminen on tärkeää.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fi/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Askeltainen malli" width="800"/>

*Ongelmien jakaminen eksplisiittisiin loogisiin vaiheisiin*

**Rajoitettu tuloste** – Vastaukset, joilla on erityisvaatimukset muodolle. Malli noudattaa tiukasti muoto- ja pituussääntöjä. Käytä tätä yhteenvetoihin tai tilanteissa, joissa tarkka tulosteen rakenne on ratkaisevaa.

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

*Muodon, pituuden ja rakenteen tiukka noudattaminen*

## Using Existing Azure Resources

**Varmista käyttöönotto:**

Varmista, että `.env`-tiedosto on juurikansiossa Azuren tunnistetietoineen (luotu Moduuli 01:n aikana):
```bash
cat ../.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Käynnistä sovellus:**

> **Huom:** Jos olet jo käynnistänyt kaikki sovellukset käyttämällä `./start-all.sh` Moduuli 01:ssa, tämä moduuli toimii jo portissa 8083. Voit ohittaa käynnistyskomennot alla ja mennä suoraan osoitteeseen http://localhost:8083.

**Vaihtoehto 1: Spring Boot Dashboardin käyttö (suositellaan VS Code -käyttäjille)**

Dev-containerissa on Spring Boot Dashboard -laajennus, joka tarjoaa visuaalisen käyttöliittymän kaikkien Spring Boot -sovellusten hallintaan. Löydät sen VS Coden vasemman puolen Activity Barista (etsi Spring Boot -kuvake).
Spring Boot -kojelautalta voit:
- Näyttää kaikki työtilassa olevat saatavilla olevat Spring Boot -sovellukset
- Käynnistää/pysäyttää sovelluksia yhdellä napsautuksella
- Tarkastella sovelluksen lokeja reaaliajassa
- Valvoa sovelluksen tilaa

Napsauta yksinkertaisesti toistopainiketta "prompt-engineering" vieressä käynnistääksesi tämän moduulin, tai käynnistä kaikki moduulit kerralla.

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

Molemmat skriptit lataavat automaattisesti ympäristömuuttujat juurihakemiston `.env`-tiedostosta ja rakentavat JAR-tiedostot, jos niitä ei ole olemassa.

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

Avaa selain ja mene osoitteeseen http://localhost:8083.

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

<img src="../../../translated_images/fi/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Pääkojelaudanäkymä, jossa näkyy kaikki 8 prompt engineering -mallia niiden ominaisuuksineen ja käyttötapauksineen*

## Mallien tutkiminen

Verkkokäyttöliittymässä voit kokeilla erilaisia kehotusstrategioita. Jokainen malli ratkaisee eri ongelmia - kokeile niitä nähdäksesi, milloin kukin lähestymistapa toimii parhaiten.

### Alhainen vs korkea innokkuus

Kysy yksinkertainen kysymys, kuten "Mikä on 15 % luvusta 200?" käyttäen Alhaista innokkuutta. Saat heti suoran vastauksen. Kysy sitten jotain monimutkaisempaa, kuten "Suunnittele välimuististrategia korkean kuormituksen API:lle" käyttäen Korkeaa innokkuutta. Katso, kuinka malli hidastuu ja tarjoaa yksityiskohtaisia perusteluja. Sama malli, sama kysymystapa - mutta kehotus kertoo, kuinka paljon ajattelua tarvitaan.

<img src="../../../translated_images/fi/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Nopea laskutoimitus minimaalisella päättelyllä*

<img src="../../../translated_images/fi/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Yksityiskohtainen välimuististrategia (2.8MB)*

### Tehtävän suoritus (Työkalujen aloitukset)

Monivaiheiset työnkulut hyötyvät etukäteen suunnittelusta ja etenemiskertomuksesta. Malli hahmottaa, mitä se aikoo tehdä, kertoo jokaisen vaiheen, ja lopuksi tiivistää tulokset.

<img src="../../../translated_images/fi/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*REST-päätteen luominen vaihe vaiheelta kertomuksella (3.9MB)*

### Itsearvioiva koodi

Kokeile komentoa "Luo sähköpostin validointipalvelu". Sen sijaan, että se vain generoi koodin ja pysähtyy, malli luo, arvioi laatukriteerien perusteella, tunnistaa heikkoudet ja parantaa koodia. Näet, kuinka se käy läpi iteraatiota, kunnes koodi täyttää tuotantostandardit.

<img src="../../../translated_images/fi/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Täydellinen sähköpostin validointipalvelu (5.2MB)*

### Rakenteellinen analyysi

Koodikatselmukset tarvitsevat johdonmukaiset arviointikehykset. Malli analysoi koodin kiinteiden kategorioiden (oikeellisuus, käytännöt, suorituskyky, turvallisuus) ja vakavuustasojen avulla.

<img src="../../../translated_images/fi/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Kehykseen perustuva koodikatselmus*

### Usean vuorovaikutuksen keskustelu

Kysy "Mikä on Spring Boot?" ja heti perään "Näytä esimerkki". Malli muistaa ensimmäisen kysymyksen ja antaa juuri sinulle Spring Boot -esimerkin. Ilman muistia toinen kysymys olisi liian epämääräinen.

<img src="../../../translated_images/fi/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Keskustelun kontekstin säilyttäminen kysymysten välillä*

### Vaiheittainen päättely

Valitse matemaattinen ongelma ja kokeile sitä sekä Vaiheittaisen päättelyn että Alhaisen innokkuuden kanssa. Alhainen innokkuus antaa vain vastauksen - nopeasti mutta pinnallisesti. Vaihe vaiheelta näyttää jokaisen laskutoimituksen ja päätöksen.

<img src="../../../translated_images/fi/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matemaattinen ongelma yksityiskohtaisilla vaiheilla*

### Rajoitettu tuloste

Kun tarvitset tiettyjä muotoja tai sanamääriä, tämä malli varmistaa tiukan noudattamisen. Kokeile luoda yhteenveto, jossa on täsmälleen 100 sanaa luettelomuodossa.

<img src="../../../translated_images/fi/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Koneoppimisen yhteenveto muotovalvonnalla*

## Mitä todella opit

**Päättelyvaiva muuttaa kaiken**

GPT-5.2 antaa sinun hallita laskennallista vaivaa kehotteidesi kautta. Alhainen vaiva tarkoittaa nopeita vastauksia vähäisellä tutkimisella. Korkea vaiva tarkoittaa, että malli käyttää aikaa syvälliseen ajatteluun. Opit sovittamaan ponnistelun tehtävän monimutkaisuuteen - älä tuhlaa aikaa yksinkertaisiin kysymyksiin, mutta älä myöskään kiirehdi monimutkaisia päätöksiä.

**Rakenne ohjaa käyttäytymistä**

Huomaatko XML-tunnisteet kehotteissa? Ne eivät ole koristeita. Mallit seuraavat rakenneohjeita luotettavammin kuin vapaamuotoista tekstiä. Kun tarvitset monivaiheisia prosesseja tai monimutkaista logiikkaa, rakenne auttaa mallia tietämään, missä se on ja mitä tulee seuraavaksi.

<img src="../../../translated_images/fi/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Hyvin rakennetun kehotteen anatomia selkeine osioineen ja XML-tyylisine järjestelyineen*

**Laatu itsearvioinnin kautta**

Itsearvioivat mallit toimivat tekemällä laatukriteerit eksplisiittisiksi. Sen sijaan, että toivoisit mallin "tekevän oikein", kerrot tarkkaan, mitä "oikein" tarkoittaa: oikea logiikka, virheenkäsittely, suorituskyky, turvallisuus. Malli voi sitten arvioida omaa tuotostaan ja parantaa sitä. Tämä muuttaa koodin generoinnin arpajaisista prosessiksi.

**Konteksti on rajallinen**

Usean vuorovaikutuksen keskustelut toimivat sisällyttämällä viestihistoria jokaisen pyynnön mukana. Mutta on olemassa raja - jokaisella mallilla on maksimimäärä tokeneita. Keskustellessa tarvitaan strategioita pitää merkityksellinen konteksti ilman, että katto ylittyy. Tämä moduuli näyttää, miten muisti toimii; myöhemmin opit, milloin tiivistää, milloin unohtaa ja milloin hakea.

## Seuraavat vaiheet

**Seuraava moduuli:** [03-rag - RAG (hakupohjainen generointi)](../03-rag/README.md)

---

**Navigointi:** [← Edellinen: Moduuli 01 - Johdanto](../01-introduction/README.md) | [Takaisin etusivulle](../README.md) | [Seuraava: Moduuli 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:  
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, on hyvä ottaa huomioon, että automaattikäännöksissä saattaa esiintyä virheitä tai epätarkkuuksia. Alkuperäistä asiakirjaa sen alkuperäiskielellä tulisi pitää auktoritatiivisena lähteenä. Tärkeissä tiedoissa suosittelemme ammattimaisen ihmiskääntäjän käyttöä. Emme ole vastuussa tämän käännöksen käytöstä aiheutuvista väärinymmärryksistä tai virhetulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->