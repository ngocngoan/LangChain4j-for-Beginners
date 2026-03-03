# Moduuli 02: Prompt Engineering GPT-5.2:n kanssa

## Sisällysluettelo

- [Videokävely](../../../02-prompt-engineering)
- [Mitkä asiat opit](../../../02-prompt-engineering)
- [Esivaatimukset](../../../02-prompt-engineering)
- [Prompt Engineeringin ymmärtäminen](../../../02-prompt-engineering)
- [Prompt Engineering -perusteet](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Edistyneet mallit](../../../02-prompt-engineering)
- [Sovelluksen ajaminen](../../../02-prompt-engineering)
- [Sovelluksen ruutukaappaukset](../../../02-prompt-engineering)
- [Mallien tutkiminen](../../../02-prompt-engineering)
  - [Matalan ja korkean innokkuuden erot](../../../02-prompt-engineering)
  - [Tehtävän suoritus (työkalujen alkutekstit)](../../../02-prompt-engineering)
  - [Itseään peilaava koodi](../../../02-prompt-engineering)
  - [Rakenteellinen analyysi](../../../02-prompt-engineering)
  - [Monivaiheinen keskustelu](../../../02-prompt-engineering)
  - [Askel askeleelta -päättely](../../../02-prompt-engineering)
  - [Rajoitettu tulostus](../../../02-prompt-engineering)
- [Mitä todella opit](../../../02-prompt-engineering)
- [Seuraavat askeleet](../../../02-prompt-engineering)

## Videokävely

Katso tämä live-istunto, joka selittää, kuinka pääset alkuun tämän moduulin kanssa:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Mitkä asiat opit

Seuraava kaavio tarjoaa yleiskuvan keskeisistä aiheista ja taidoista, joita kehität tässä moduulissa — promptien hienosäätötekniikoista askel askeleelta -työnkulkuun, jota seuraat.

<img src="../../../translated_images/fi/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

Edellisissä moduuleissa tutustuit perus LangChain4j -vuorovaikutuksiin GitHub-mallien kanssa ja näit, miten muisti mahdollistaa Azure OpenAI:n kanssa käydyn keskustelun tekoälyn. Nyt keskitymme siihen, miten esität kysymyksiä — itse prompteihin — käyttäen Azure OpenAI:n GPT-5.2:ta. Tapa, jolla rakentelet promptit, vaikuttaa dramaattisesti saamaasi vastauksen laatuun. Aloitamme perustekniikoiden kertaamisella, ja siirrymme sitten kahdeksaan edistyneeseen malliin, jotka hyödyntävät GPT-5.2:n ominaisuuksia täysimääräisesti.

Käytämme GPT-5.2:ta, koska se tuo mukanaan päättelyohjauksen — voit kertoa mallille, kuinka paljon ajattelua sen tulisi tehdä ennen vastaamista. Tämä tekee eri promptaustaktiikoista selkeämpiä ja auttaa sinua ymmärtämään, milloin mitäkin lähestymistapaa kannattaa käyttää. Lisäksi hyödymme Azuren pienemmistä käyttörajoituksista GPT-5.2:lle verrattuna GitHub-malleihin.

## Esivaatimukset

- Moduuli 01 suoritettu (Azure OpenAI -resurssit otettu käyttöön)
- `.env`-tiedosto juurihakemistossa, jossa Azure-tunnukset (luotu moduulin 01 `azd up` -komennolla)

> **Huom:** Jos et ole suorittanut moduulia 01, seuraa ensin siellä olevia käyttöönotto-ohjeita.

## Prompt Engineeringin ymmärtäminen

Prompt engineering on pohjimmiltaan ero epämääräisten ja tarkkojen ohjeiden välillä, kuten alla oleva vertailu havainnollistaa.

<img src="../../../translated_images/fi/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Prompt engineering tarkoittaa syötteen suunnittelua siten, että saat johdonmukaisesti tarvitsemiasi tuloksia. Kyse ei ole vain kysymysten esittämisestä — vaan pyynnön rakentamisesta niin, että malli ymmärtää tarkalleen, mitä haluat ja miten se toimitetaan.

Ajattele sitä kuin annettaisit ohjeita kollegalle. "Korjaa virhe" on epämääräinen ohje. "Korjaa null pointer exception UserService.java:n rivillä 45 lisäämällä null-tarkistus" on tarkka. Kielimallit toimivat samalla tavalla — tarkkuus ja rakenne ratkaisevat.

Alla oleva kaavio näyttää, miten LangChain4j liittyy tähän kuvaan — se yhdistää promptimallit malliin SystemMessage- ja UserMessage-rakenteiden kautta.

<img src="../../../translated_images/fi/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j tarjoaa infrastruktuurin — malliyhteydet, muistin ja viestityypit — kun taas promptimallit ovat vain huolellisesti rakennetun tekstin lähettämistä tämän infrastruktuurin läpi. Keskeiset rakennuspalikat ovat `SystemMessage` (joka määrittää tekoälyn käyttäytymisen ja roolin) ja `UserMessage` (joka kantaa varsinaisen pyynnön).

## Prompt Engineering -perusteet

Alla esitetyt viisi ydintekniikkaa muodostavat tehokkaan prompt engineeringin perustan. Kukin käsittelee eri näkökulmaa siihen, miten kommunikoit kielimallien kanssa.

<img src="../../../translated_images/fi/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Ennen kuin sukellamme tämän moduulin edistyneisiin malleihin, kertaamme viisi perustekniikkaa. Nämä ovat rakennuspalikoita, jotka jokaisen prompt-insinöörin tulee tuntea. Jos olet jo käynyt läpi [Quick Start -moduulin](../00-quick-start/README.md#2-prompt-patterns), olet nähnyt niitä käytännössä — tässä on niiden konseptuaalinen kehys.

### Zero-Shot Prompting

Yksinkertaisin tapa: anna mallille suora ohje ilman esimerkkejä. Malli luottaa täysin koulutukseensa ymmärtääkseen ja suorittaakseen tehtävän. Tämä toimii hyvin suoraviivaisissa pyynnöissä, joissa odotettu käyttäytyminen on ilmeistä.

<img src="../../../translated_images/fi/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Suora ohje ilman esimerkkejä — malli päättelee tehtävän pelkästään ohjeesta*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Vastaus: "Positiivinen"
```

**Milloin käyttää:** Yksinkertaiset luokitukset, suorat kysymykset, käännökset tai mikä tahansa tehtävä, jonka malli osaa hoitaa ilman lisäohjeita.

### Few-Shot Prompting

Anna esimerkkejä, jotka näyttävät mallille, mitä mallia haluat sen noudattavan. Malli oppii odotetun syöte-tuotos-muodon esimerkeistäsi ja soveltaa sitä uusiin syötteisiin. Tämä parantaa merkittävästi johdonmukaisuutta tehtävissä, joissa haluttu muoto tai käyttäytyminen ei ole ilmeistä.

<img src="../../../translated_images/fi/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Oppiminen esimerkeistä — malli tunnistaa mallin ja soveltaa sitä uusiin syötteisiin*

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

**Milloin käyttää:** Räätälöidyt luokitukset, johdonmukainen muotoilu, toimialakohtaiset tehtävät tai silloin kun zero-shot-tulokset ovat epäjohdonmukaisia.

### Chain of Thought

Pyydä mallia näyttämään päättelynsä vaihe vaiheelta. Sen sijaan, että hyppäisi suoraan vastaukseen, malli pilkkoo ongelman ja käsittelee jokaisen osan erikseen. Tämä parantaa tarkkuutta matemaattisissa, loogisissa ja monivaiheisissa päättelytehtävissä.

<img src="../../../translated_images/fi/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Askel askeleelta -päättely — monimutkaisten ongelmien pilkkominen eksplisiittisiin loogisiin vaiheisiin*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Malli näyttää: 15 - 8 = 7, sitten 7 + 12 = 19 omenaa
```

**Milloin käyttää:** Matematiikan ongelmat, loogiset pulmat, virheen etsintä tai mikä tahansa tehtävä, jossa päättelyprosessin näyttäminen parantaa tarkkuutta ja luottamusta.

### Role-Based Prompting

Aseta tekoälylle persoona tai rooli ennen kuin esität kysymyksen. Tämä antaa kontekstin, joka muokkaa vastauksen sävyä, syvyyttä ja painopistettä. "Ohjelmistoarkkitehti" antaa erilaista neuvontaa kuin "juniori-kehittäjä" tai "turvallisuustarkastaja".

<img src="../../../translated_images/fi/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Kontekstin ja persoonan asettaminen — samaan kysymykseen saadaan eri vastauksia roolista riippuen*

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

**Milloin käyttää:** Koodikatselmukset, opetus, toimialakohtainen analyysi tai silloin kun tarvitset vastauksia, jotka on räätälöity tietyn asiantuntemustason tai näkökulman mukaan.

### Prompt Templates

Luo uudelleenkäytettäviä prompt-malleja muuttujapaikoilla. Sen sijaan, että kirjoittaisit uuden promptin joka kerta, määrittele mallipohja kerran ja täytä eri arvot. LangChain4j:n `PromptTemplate`-luokka tekee tämän helpoksi `{{variable}}`-syntaksilla.

<img src="../../../translated_images/fi/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Uudelleenkäytettävät promptit muuttujapaikoilla — yksi malli, monta käyttötarkoitusta*

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

**Milloin käyttää:** Toistuvat kyselyt eri syötteillä, eräkäsittely, uudelleenkäytettävien tekoälytyönkulkujen rakentaminen tai mikä tahansa tilanne, jossa promptin rakenne pysyy samana mutta data vaihtelee.

---

Nämä viisi perustekniikkaa tarjoavat vahvan työkalupakin useimpiin promptaustehtäviin. Loput tästä moduulista rakentuvat niiden päälle käyttäen **kahtatoista edistynyttä mallia**, jotka hyödyntävät GPT-5.2:n päättelyohjausta, itsearviointia ja rakenteellista tulostusta.

## Edistyneet mallit

Kun perusteet on käyty läpi, siirrytään kahdeksaan edistyneeseen malliin, jotka tekevät tästä moduulista ainutlaatuisen. Kaikki ongelmat eivät vaadi samaa lähestymistapaa. Jotkut kysymykset tarvitsevat nopeita vastauksia, toiset syvällistä pohdintaa. Jotkin vaativat näkyvää päättelyä, toiset vain tuloksia. Jokainen alla oleva malli on optimoitu eri tilanteeseen — ja GPT-5.2:n päättelyohjaus korostaa näiden eroja entisestään.

<img src="../../../translated_images/fi/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Yleiskatsaus kahdeksaan prompt engineering -malliin ja niiden käyttötapauksiin*

GPT-5.2 lisää näihin malleihin uuden ulottuvuuden: *päättelyohjauksen*. Liukusäädin alla näyttää, kuinka voit säätää mallin ajatteluprosessin voimakkuuden — nopeista suoriin vastauksiin ja syvälliseen, perusteelliseen analyysiin.

<img src="../../../translated_images/fi/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2:n päättelyohjaus antaa sinun määrittää, kuinka paljon mallin tulee ajatella — nopeista suorista vastauksista syvälliseen tarkasteluun*

**Matalan innokkuuden tila (Nopea & Tämänhetkinen)** - Yksinkertaisiin kysymyksiin, joissa haluat nopeita, suoria vastauksia. Malli käyttää minimaalista päättelyä – enintään 2 askelta. Käytä tätä laskuihin, hakuun tai suoraviivaisiin kysymyksiin.

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

> 💡 **Tutki GitHub Copilotin kanssa:** Avaa [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ja kysy:
> - "Mikä on ero matalan ja korkean innokkuuden promptausmallien välillä?"
> - "Miten XML-tunnisteet prompteissa auttavat muotoilemaan tekoälyn vastauksen?"
> - "Milloin käytän itsearviointimalleja verrattuna suoriin ohjeisiin?"

**Korkean innokkuuden tila (Syvällinen & Perusteellinen)** - Monimutkaisiin ongelmiin, joissa haluat kattavan analyysin. Malli tutkii aihetta perusteellisesti ja näyttää yksityiskohtaisen päättelyn. Käytä tätä järjestelmäsuunnittelussa, arkkitehtuuripäätöksissä tai monimutkaisessa tutkimuksessa.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Tehtävän suoritus (askel askeleelta etenevä prosessi)** - Monivaiheisiin työnkulkuihin. Malli antaa etukäteen suunnitelman, kuvailee kunkin vaiheen työskentelyn aikana, ja lopuksi esittää yhteenvedon. Käytä tätä migraatioissa, toteutuksissa tai missä tahansa monivaiheisessa prosessissa.

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

Chain-of-Thought -promptaus pyytää mallia erittäin selkeästi näyttämään päättelyprosessinsa, mikä parantaa tarkkuutta monimutkaisissa tehtävissä. Askel askeleelta -erittely auttaa sekä ihmisiä että tekoälyä ymmärtämään logiikan.

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatin kanssa:** Kysy tästä mallista:
> - "Miten mukauttaisin tehtävän suoritusmallin pitkäkestoisia operaatioita varten?"
> - "Mitkä ovat parhaat käytännöt työkalujen aloitustekstien järjestämisessä tuotantosovelluksissa?"
> - "Miten voin kaapata ja näyttää välivaiheen etenemispäivityksiä käyttöliittymässä?"

Alla oleva kaavio havainnollistaa tätä Suunnittelu → Suoritus → Yhteenveto -työnkulkua.

<img src="../../../translated_images/fi/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Suunnittelu → Suoritus → Yhteenveto -työnkulku monivaiheisissa tehtävissä*

**Itseään peilaava koodi** – Tuotantokelpoisen koodin generointiin. Malli luo koodia noudattaen tuotantostandardeja ja huolella virheenkäsittelyä. Käytä tätä rakentaessasi uusia ominaisuuksia tai palveluita.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

Alla oleva kaavio näyttää tämän iteratiivisen parannuskierron — generoi, arvioi, tunnista heikkoudet ja hienosäädä kunnes koodi täyttää tuotantovaatimukset.

<img src="../../../translated_images/fi/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iteratiivinen parannuskierto - generointi, arviointi, ongelmien tunnistus, parannus, toisto*

**Rakenteellinen analyysi** — Johdonmukaiseen arviointiin. Malli tarkastelee koodia kiinteän kehyksen mukaisesti (oikeellisuus, käytännöt, suorituskyky, turvallisuus, ylläpidettävyys). Käytä tätä koodikatselmuksissa tai laadunarvioinneissa.

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

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatin kanssa:** Kysy rakenteellisesta analyysista:
> - "Miten räätälöin analyysikehyksen eri tyyppisiin koodikatselmuksiin?"
> - "Mikä on paras tapa jäsentää ja hyödyntää rakenteellista tulostetta ohjelmallisesti?"
> - "Miten varmistan johdonmukaiset vakavuustasot eri katselmusistunnoissa?"

Seuraava kaavio näyttää, miten tämä rakenne jäsentää koodikatselmuksen johdonmukaisiin kategorioihin vakavuustasojen kanssa.

<img src="../../../translated_images/fi/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Kehys johdonmukaisiin koodikatselmuksiin vakavuustasoineen*

**Monivaiheinen keskustelu** — Keskusteluihin, joissa tarvitaan kontekstia. Malli muistaa aiemmat viestit ja rakentaa niistä eteenpäin. Käytä tätä vuorovaikutteisissa tukisessioissa tai monimutkaisessa kysymys-vastaus -toiminnassa.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

Alla oleva kaavio havainnollistaa, miten keskustelukonteksti karttuu kierrosten myötä ja miten se liittyy mallin token-rajoitukseen.

<img src="../../../translated_images/fi/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Kuinka keskustelukonteksti karttuu useiden vaihtojen aikana token-rajan saavuttamiseen saakka*
**Vaiheittainen päättely** – Ongelmissa, jotka vaativat näkyvää logiikkaa. Malli näyttää selkeän päättelyn jokaiselle vaiheelle. Käytä tätä matemaattisissa ongelmissa, logiikkapähkinöissä tai kun haluat ymmärtää ajatteluprosessin.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

Alla oleva kaavio havainnollistaa, miten malli jakaa ongelmat selkeiksi, numeroiduiksi loogisiksi vaiheiksi.

<img src="../../../translated_images/fi/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Vaiheittainen malli" width="800"/>

*Ongelmien jakaminen selkeisiin loogisiin vaiheisiin*

**Rajoitettu tuloste** – Vastauksissa, joissa vaaditaan tiettyä muotoa. Malli noudattaa tiukasti muoto- ja pituussääntöjä. Käytä tätä yhteenvetoihin tai silloin, kun tarvitset tarkan tulosteen rakenteen.

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

Seuraava kaavio näyttää, miten rajoitukset ohjaavat mallia tuottamaan vastauksia, jotka noudattavat täsmällisesti vaatimuksia muodon ja pituuden suhteen.

<img src="../../../translated_images/fi/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Rajoitetun tulosteen malli" width="800"/>

*Tarkasti määritettyjen muoto-, pituus- ja rakennevaatimusten noudattaminen*

## Suorita sovellus

**Vahvista käyttöönotto:**

Varmista, että `.env`-tiedosto on juurihakemistossa ja sisältää Azure-tunnistetiedot (luotu moduulissa 01). Aja tämä moduulihakemistosta (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Käynnistä sovellus:**

> **Huom:** Jos olet jo käynnistänyt kaikki sovellukset komennolla `./start-all.sh` juurihakemistosta (kuten moduulissa 01 kuvattiin), tämä moduuli on jo käynnissä portissa 8083. Voit ohittaa alla olevat käynnistyskomennot ja mennä suoraan osoitteeseen http://localhost:8083.

**Vaihtoehto 1: Spring Boot Dashboardin käyttö (suositellaan VS Code -käyttäjille)**

Kehityssäilö sisältää Spring Boot Dashboard -laajennuksen, joka tarjoaa visuaalisen käyttöliittymän kaikkien Spring Boot -sovellusten hallintaan. Löydät sen VS Coden vasemmanpuoleisesta Activity Barista (etsi Spring Boot -kuvake).

Spring Boot Dashboardista voit:
- Näyttää kaikki käytettävissä olevat Spring Boot -sovellukset työtilassa
- Käynnistää/pysäyttää sovellukset yhdellä napsautuksella
- Tarkastella sovelluksen lokeja reaaliajassa
- Valvoa sovellusten tilannetta

Klikkaa yksinkertaisesti toistopainiketta "prompt-engineering" -kohdan vieressä käynnistääksesi tämän moduulin, tai käynnistä kaikki moduulit kerralla.

<img src="../../../translated_images/fi/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard VS Codessa – käynnistä, pysäytä ja valvo kaikki moduulit yhdestä paikasta*

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

Kummatkin skriptit lataavat automaattisesti ympäristömuuttujat juurihakemiston `.env`-tiedostosta ja rakentavat JAR-tiedostot, jos niitä ei ole olemassa.

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

Avaa selaimessa http://localhost:8083.

**Pysäytä sovellus:**

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

## Sovelluksen kuvakaappaukset

Tässä on prompt engineering -moduulin pääkäyttöliittymä, jossa voit kokeilla kaikkia kahdeksaa mallia rinnakkain.

<img src="../../../translated_images/fi/dashboard-home.5444dbda4bc1f79d.webp" alt="Hallintapaneelin etusivu" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Päähallintapaneeli näyttää kaikki 8 prompt engineering -mallia niiden ominaisuuksineen ja käyttötapauksineen*

## Mallien tutkiminen

Verkkokäyttöliittymä antaa kokeilla eri ohjausstrategioita. Jokainen malli ratkaisee eri ongelmia – kokeile niitä nähdäksesi, milloin kukin lähestymistapa toimii parhaiten.

> **Huom: Virtautus vs Ei-virtautus** — Jokaisella mallisivulla on kaksi painiketta: **🔴 Virtauta vastaus (reaaliaikainen)** ja **Ei-virtautus** -vaihtoehto. Virtautus käyttää Server-Sent Events (SSE) -tekniikkaa näyttäen tokeneita reaaliajassa, kun malli tuottaa niitä, joten näet edistymisen heti. Ei-virtautus-vaihtoehto odottaa koko vastauksen valmiiksi ennen näyttämistä. Syvällistä päättelyä vaativissa kehotteissa (esim. High Eagerness, Self-Reflecting Code) ei-virtautus-kutsu voi kestää hyvin kauan – joskus minuutteja – eikä anna näkyvää palautetta. **Käytä virtausta monimutkaisissa kokeiluissa**, jotta näet mallin toiminnan ja vältät vaikutelman aikakatkaisusta.
>
> **Huom: Selaimen vaatimukset** — Virtausominaisuus käyttää Fetch Streams -API:a (`response.body.getReader()`), joka vaatii täysimittaisen selaimen (Chrome, Edge, Firefox, Safari). Se EI toimi VS Coden sisäänrakennetussa Simple Browserissa, koska sen webview ei tue ReadableStream API:a. Jos käytät Simple Browseria, ei-virtautus-painikkeet toimivat normaalisti – virtauspainikkeet ovat ainoastaan rajoitettuja. Avaa `http://localhost:8083` ulkoisessa selaimessa saadaksesi täyden kokemuksen.

### Matala vs Korkea innokkuus

Kysy yksinkertainen kysymys kuten "Mikä on 15 % luvusta 200?" käyttäen Matala innokkuus -tapaa. Saat välittömän, suoraviivaisen vastauksen. Kysy sitten jotain monimutkaista, esimerkiksi "Suunnittele välimuististrategia suurliikenteiselle API:lle", käyttäen Korkea innokkuus -tapaa. Klikkaa **🔴 Virtauta vastaus (reaaliaikainen)** ja katso, kuinka mallin yksityiskohtainen päättely ilmestyy token tokenilta. Sama malli, sama kysymysrakenne – mutta kehotus määrää, kuinka paljon sen tulee ajatella.

### Tehtävän suoritus (Työkalujen esilauseet)

Monivaiheiset työnkulut hyötyvät etukäteissuunnittelusta ja etenemisen artikkeloinnista. Malli hahmottelee, mitä se aikoo tehdä, kuvailee kunkin vaiheen ja tiivistää tulokset.

### Itsearvioiva koodi

Kokeile "Luo sähköpostin validointipalvelu". Sen sijaan, että malli vain generoi koodin ja pysähtyy, se luo, arvioi laatukriteerien mukaan, tunnistaa heikkoudet ja parantaa. Näet, kuinka se toistaa, kunnes koodi täyttää tuotantovaatimukset.

### Jäsennelty analyysi

Koodikatselmukset tarvitsevat yhdenmukaiset arviointikehykset. Malli analysoi koodia kiinteiden kategorioiden (oikeellisuus, käytännöt, suorituskyky, turvallisuus) mukaan vakavuustasoilla.

### Monivaiheinen keskustelu

Kysy "Mikä on Spring Boot?" ja seuraa heti kysymällä "Näytä esimerkki". Malli muistaa ensimmäisen kysymyksen ja antaa juuri sinulle sopivan Spring Boot -esimerkin. Ilman muistia toinen kysymys olisi liian epämääräinen.

### Vaiheittainen päättely

Valitse matemaattinen tehtävä ja kokeile sekä Vaiheittaisella päättelyllä että Matala innokkuus -tavalla. Matala innokkuus antaa vain vastauksen – nopeasti mutta läpinäkymättömästi. Vaiheittainen näyttää jokaisen laskelman ja päätöksen.

### Rajoitettu tuloste

Kun tarvitset tiettyjä muotoja tai sanamääriä, tämä malli varmistaa tarkkojen vaatimusten noudattamisen. Kokeile tuottaa yhteenveto, jossa on täsmälleen 100 sanaa bullet point -muodossa.

## Mitä todella opit

**Päättelyn vaivannäkö muuttaa kaiken**

GPT-5.2 antaa sinun hallita laskennallista vaivannäköä kehotteidesi kautta. Matala vaivannäkö tarkoittaa nopeita vastauksia vähäisellä etsinnällä. Korkea vaivannäkö tarkoittaa, että malli käyttää aikaa syvälliseen ajatteluun. Opit sovittamaan vaivannäön tehtävän monimutkaisuuteen – älä käytä aikaa yksinkertaisiin kysymyksiin, mutta älä myöskään kiirehdi monimutkaisia päätöksiä.

**Rakenne ohjaa käyttäytymistä**

Huomaatko kehotteiden XML-tunnisteet? Ne eivät ole koristeita. Mallit seuraavat rakenteellisia ohjeistuksia luotettavammin kuin vapaata tekstiä. Kun tarvitset monivaiheisia prosesseja tai monimutkaista logiikkaa, rakenne auttaa mallia seuraamaan, missä se on ja mitä seuraavaksi tehdään. Alla oleva kaavio purkaa hyvin jäsennellyn kehotteen näyttämällä, miten tunnisteet kuten `<system>`, `<instructions>`, `<context>`, `<user-input>` ja `<constraints>` järjestävät ohjeet selkeisiin osiin.

<img src="../../../translated_images/fi/prompt-structure.a77763d63f4e2f89.webp" alt="Kehotteen rakenne" width="800"/>

*Hyvin jäsennellyn kehotteen anatomia, jossa selkeät osiot ja XML-tyylinen järjestely*

**Laatu itsearvioinnilla**

Itsearvioiviin malleihin sisältyy laatukriteerien selkeä määrittely. Sen sijaan, että toivoisit mallin "tekevän oikein", kerrot sille täsmälleen, mitä "oikein" tarkoittaa: oikea logiikka, virheenkäsittely, suorituskyky, turvallisuus. Malli voi sitten arvioida omaa tuotostaan ja parantaa sitä. Tämä muuttaa koodin generoinnin arpajaisista prosessiksi.

**Konteksti on rajallinen**

Monivaiheiset keskustelut toimivat sisällyttämällä viestihistorian jokaiseen pyyntöön. Mutta on olemassa raja – jokaisella mallilla on enimmäistokenien määrä. Kun keskustelut kasvavat, tarvitset strategioita pitää olennaiset tiedot ilman, että ylität tämän rajan. Tämä moduuli näyttää, miten muisti toimii; myöhemmin opit, milloin tiivistää, milloin unohtaa ja milloin hakea uudelleen.

## Seuraavat askeleet

**Seuraava moduuli:** [03-rag - RAG (Hakua täydentävä generointi)](../03-rag/README.md)

---

**Navigointi:** [← Edellinen: Moduuli 01 - Johdanto](../01-introduction/README.md) | [Takaisin pääsivulle](../README.md) | [Seuraava: Moduuli 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty käyttäen tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, tulee huomioida, että automaattikäännöksissä saattaa esiintyä virheitä tai epätarkkuuksia. Alkuperäinen asiakirja omalla kielellään on virallinen lähde. Tärkeissä tiedoissa suositellaan ammattilaisen tekemää ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä johtuvista väärinymmärryksistä tai tulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->