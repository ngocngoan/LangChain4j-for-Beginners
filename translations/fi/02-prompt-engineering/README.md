# Module 02: Kehota suunnittelu GPT-5.2:n kanssa

## Sisällysluettelo

- [Mitä opit](../../../02-prompt-engineering)
- [Esivaatimukset](../../../02-prompt-engineering)
- [Ymmärtäminen Kehota Suunnittelua](../../../02-prompt-engineering)
- [Kehota Suunnittelun Perusteet](../../../02-prompt-engineering)
  - [Nollanäytteen Kehotus](../../../02-prompt-engineering)
  - [Vähän Näytteitä Kehotus](../../../02-prompt-engineering)
  - [Ajatusketju](../../../02-prompt-engineering)
  - [Roolipohjainen Kehotus](../../../02-prompt-engineering)
  - [Kehotepohjat](../../../02-prompt-engineering)
- [Edistyneet Kuviot](../../../02-prompt-engineering)
- [Olemassa olevien Azure-resurssien käyttäminen](../../../02-prompt-engineering)
- [Sovelluksen kuvakaappaukset](../../../02-prompt-engineering)
- [Kuvioiden tutkiminen](../../../02-prompt-engineering)
  - [Matala vs Korkea Innokkuus](../../../02-prompt-engineering)
  - [Tehtävän Suorittaminen (Työkalun esipuheet)](../../../02-prompt-engineering)
  - [Itsearvioiva Koodi](../../../02-prompt-engineering)
  - [Rakenteellinen Analyysi](../../../02-prompt-engineering)
  - [Monivuoroinen Keskustelu](../../../02-prompt-engineering)
  - [Vaiheittainen Päättely](../../../02-prompt-engineering)
  - [Rajoitettu Tuotos](../../../02-prompt-engineering)
- [Mitä todella opit](../../../02-prompt-engineering)
- [Seuraavat Vaiheet](../../../02-prompt-engineering)

## Mitä opit

<img src="../../../translated_images/fi/what-youll-learn.c68269ac048503b2.webp" alt="Mitä opit" width="800"/>

Edellisessä moduulissa näit, miten muisti mahdollistaa keskustelullisen tekoälyn ja käytit GitHub-malleja perustason vuorovaikutukseen. Nyt keskitymme siihen, miten esität kysymyksiä — itse kehotuksiin — käyttäen Azure OpenAI:n GPT-5.2:ta. Tapa, jolla rakentelet kehotuksiasi, vaikuttaa dramaattisesti saamiisi vastauksiin. Aloitamme tarkastelemalla perustekniikoita ja siirrymme sitten kahdeksaan edistyneeseen kuviin, jotka hyödyntävät GPT-5.2:n kyvykkyyksiä täysimääräisesti.

Käytämme GPT-5.2:ta, koska se tuo ajattelun ohjauksen - voit kertoa mallille, kuinka paljon pohdintaa sen tulisi tehdä ennen vastaamista. Tämä tekee eri kehotusstrategioista selvempiä ja auttaa ymmärtämään, milloin käyttää mitäkin lähestymistapaa. Hyödymme myös Azuren vähemmistä rajoituksista GPT-5.2:n suhteen verrattuna GitHub-malleihin.

## Esivaatimukset

- Moduuli 01 suoritettu (Azure OpenAI -resurssit asennettu)
- `.env`-tiedosto juurihakemistossa Azure-tunnistetiedoilla (luotu `azd up` -komennolla Moduulissa 01)

> **Huom:** Jos et ole vielä suorittanut Moduulia 01, seuraa ensin siellä annettuja käyttöönotto-ohjeita.

## Ymmärrystä Kehota Suunnittelua

<img src="../../../translated_images/fi/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Mikä on Kehota Suunnittelu?" width="800"/>

Kehota suunnittelu tarkoittaa syötetyn tekstin suunnittelua siten, että saat johdonmukaisesti haluamasi tulokset. Kyse ei ole vain kysymysten esittämisestä — vaan pyyntöjen rakentelemisesta niin, että malli ymmärtää täsmälleen, mitä haluat ja miten se pitää toimittaa.

Ajattele sitä kuin ohjeiden antamista kollegalle. "Korjaa vika" on epämääräinen. "Korjaa null pointer -poikkeus UserService.java -tiedoston rivillä 45 lisäämällä null-tarkistus" on tarkempi. Kielenmallit toimivat samalla periaatteella — yksityiskohtaisuus ja rakenne ovat tärkeitä.

<img src="../../../translated_images/fi/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Miten LangChain4j sopii" width="800"/>

LangChain4j tarjoaa infrastruktuurin — malliyhteydet, muistin ja viestityypit — kun taas kehotuskuviot ovat vain huolellisesti rakennetun tekstin lähettämistä tämän infrastruktuurin kautta. Avainteknisiä osia ovat `SystemMessage` (joka asettaa tekoälyn käyttäytymisen ja roolin) ja `UserMessage` (joka kantaa varsinaisen pyyntösi).

## Kehota Suunnittelun Perusteet

<img src="../../../translated_images/fi/five-patterns-overview.160f35045ffd2a94.webp" alt="Viisi Kehota Suunnittelun Kuviota Yleiskatsaus" width="800"/>

Ennen kuin sukellamme tämän moduulin edistyneisiin kuvioihin, katsotaan viisi perustekniikkaa. Nämä ovat rakennuspalikoita, jotka jokaisen kehotussuunnittelijan tulisi tuntea. Jos olet jo työskennellyt [Nopean alun moduulin](../00-quick-start/README.md#2-prompt-patterns) kanssa, olet nähnyt nämä käytännössä — tässä niiden käsitteellinen kehys.

### Nollanäytteen Kehotus

Yksinkertaisin lähestymistapa: anna mallille suora ohje ilman esimerkkejä. Malli luottaa kokonaan koulutukseensa ymmärtääkseen ja suorittaakseen tehtävän. Tämä toimii hyvin suoraviivaisten pyyntöjen kohdalla, joissa odotettu käyttäytyminen on ilmeistä.

<img src="../../../translated_images/fi/zero-shot-prompting.7abc24228be84e6c.webp" alt="Nollanäytteen Kehotus" width="800"/>

*Suora ohje ilman esimerkkejä — malli päättelee tehtävän pelkän ohjeen perusteella*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Vastaus: "Positiivinen"
```

**Milloin käyttää:** Yksinkertaiset luokittelut, suorat kysymykset, käännökset tai tehtävät, joita malli osaa hoitaa ilman lisäohjausta.

### Vähän Näytteitä Kehotus

Tarjoa esimerkkejä, jotka havainnollistavat mallille haluamaasi kaavaa. Malli oppii odotetun syöte-lähtö-muodon esimerkeistä ja soveltaa sitä uusiin syötteisiin. Tämä parantaa johdonmukaisuutta merkittävästi tehtävissä, joissa haluttu muoto tai käyttäytyminen ei ole ilmeistä.

<img src="../../../translated_images/fi/few-shot-prompting.9d9eace1da88989a.webp" alt="Vähän Näytteitä Kehotus" width="800"/>

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

**Milloin käyttää:** Mukautetut luokittelut, johdonmukainen muotoilu, alakohtaiset tehtävät tai kun nollanäytteen tulokset ovat epäjohdonmukaisia.

### Ajatusketju

Pyydä mallia näyttämään päättelyä vaihe vaiheelta. Sen sijaan, että hypittäisiin suoraan vastaukseen, malli purkaa ongelman ja etenee selvästi osissa. Tämä parantaa tarkkuutta matematiikan, logiikan ja monivaiheisen päättelyn tehtävissä.

<img src="../../../translated_images/fi/chain-of-thought.5cff6630e2657e2a.webp" alt="Ajatusketjun Kehotus" width="800"/>

*Vaiheittainen päättely — monimutkaisten ongelmien pilkkominen eksplisiittisiin loogisiin vaiheisiin*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Malli näyttää: 15 - 8 = 7, sitten 7 + 12 = 19 omenaa
```

**Milloin käyttää:** Matematiikan tehtävät, logiikkapelit, virheenkorjaus tai tehtävät, joissa päättelyn esittäminen parantaa tarkkuutta ja luottamusta.

### Roolipohjainen Kehotus

Aseta tekoälylle rooli tai persoona ennen kysymyksen esittämistä. Tämä antaa kontekstin, joka muokkaa vastauksen sävyä, syvyyttä ja painopistettä. "Ohjelmistoarkkitehti" antaa eri neuvoja kuin "junior-kehittäjä" tai "turva-auditoija".

<img src="../../../translated_images/fi/role-based-prompting.a806e1a73de6e3a4.webp" alt="Roolipohjainen Kehotus" width="800"/>

*Kontekstin ja roolin asettaminen — sama kysymys saa eri vastauksen roolista riippuen*

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

**Milloin käyttää:** Koodikatselmukset, opetus, alakohtainen analyysi tai kun tarvitset vastauksia, jotka on räätälöity tietyn asiantuntijatason tai näkökulman mukaan.

### Kehotepohjat

Luo uudelleenkäytettäviä kehotuksia muuttujapaikoilla. Sen sijaan, että kirjoittaisit uuden kehotuksen joka kerta, määrittele malli kerran ja täytä eri arvot. LangChain4j:n `PromptTemplate`-luokka tekee tämän helpoksi käyttämällä `{{variable}}`-syntaksia.

<img src="../../../translated_images/fi/prompt-templates.14bfc37d45f1a933.webp" alt="Kehotepohjat" width="800"/>

*Uudelleenkäytettävät kehotukset muuttujapaikoilla — yksi malli, monia käyttötapoja*

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

**Milloin käyttää:** Toistuvat kyselyt eri syötteillä, eräajot, uudelleenkäytettävien tekoälytyönkulkujen rakentaminen tai missä tahansa tilanteessa, jossa kehotuksen rakenne pysyy samana, mutta data muuttuu.

---

Nämä viisi perustetta tarjoavat vahvan työkalupakin yleisimpiin kehotustehtäviin. Tämän moduulin loput rakentuvat niiden päälle kahdeksalla edistyneellä kuviolla, jotka hyödyntävät GPT-5.2:n ajattelun ohjausta, itsearviointia ja rakenteellista ulostuloa.

## Edistyneet Kuviot

Kun perustaidot on käyty läpi, siirrytään kahdeksaan edistyneeseen kuvioon, jotka tekevät tästä moduulista ainutlaatuisen. Kaikki ongelmat eivät vaadi samaa lähestymistapaa. Joihinkin kysymyksiin haluat nopeita vastauksia, toisiin syvällistä pohdintaa. Joissain tarvitaan näkyvää päättelyä, toisissa pelkät tulokset. Alla oleva kukin kuvio on optimoitu eri tilanteeseen — ja GPT-5.2:n ajattelun ohjaus tekee eroista entistä selvempiä.

<img src="../../../translated_images/fi/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Kahdeksan Kehotuksen Kuviota" width="800"/>

*Kahdeksan kehotussuunnittelun kuvion yleiskatsaus ja käyttötapaukset*

<img src="../../../translated_images/fi/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Ajattelun Ohjaus GPT-5.2:n kanssa" width="800"/>

*GPT-5.2:n ajattelun ohjaus antaa sinulle mahdollisuuden määrittää, kuinka paljon mallin tulee ajatella — nopeista suoraviivaisista vastauksista syvälliseen pohdintaan*

**Matala Innokkuus (Nopea & Kohdennettu)** – Yksinkertaisiin kysymyksiin, joihin haluat nopeita, suoraviivaisia vastauksia. Malli tekee minimimäärän päättelyä — enintään 2 askelta. Käytä tätä laskuihin, hakuun tai yksinkertaisiin kysymyksiin.

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
> - "Mikä ero on matalan ja korkean innokkuuden kehotuskuvioiden välillä?"
> - "Miten XML-tunnisteet kehotuksissa auttavat AI-vastauksen rakennetta?"
> - "Milloin käytän itsearviointikuviota ja milloin suoraa ohjetta?"

**Korkea Innokkuus (Syvällinen & Huolellinen)** – Monimutkaisiin ongelmiin, joihin haluat kattavan analyysin. Malli tutkii perusteellisesti ja näyttää yksityiskohtaisen päättelyn. Käytä tätä järjestelmäsuunnitteluun, arkkitehtuuripäätöksiin tai monimutkaiseen tutkimukseen.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Tehtävän Suorittaminen (Vaiheittainen eteneminen)** – Monivaiheisille työnkuluillE. Malli antaa etukäteen suunnitelman, kuvailee kutakin askelta työskennellessään ja antaa lopuksi yhteenvedon. Käytä tätä migraatioissa, toteutuksissa tai missä tahansa monivaiheisessa prosessissa.

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

Ajatusketju-kehotus pyytää nimenomaisesti mallia näyttämään päättelyprosessinsa, mikä parantaa tarkkuutta monimutkaisissa tehtävissä. Vaiheittainen jäsennys auttaa sekä ihmisiä että tekoälyä ymmärtämään logiikan.

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatin kanssa:** Kysy tästä kuviosta:
> - "Kuinka sovittaisin tehtävän suorituskuvion pitkäkestoisiin operaatioihin?"
> - "Mitkä ovat parhaat käytännöt työkaluesipuheiden rakenteistamiseen tuotantosovelluksissa?"
> - "Kuinka voin kaapata ja näyttää välivaiheen edistymisraportit käyttöliittymässä?"

<img src="../../../translated_images/fi/task-execution-pattern.9da3967750ab5c1e.webp" alt="Tehtävän Suorituskuvio" width="800"/>

*Suunnittele → Suorita → Tiivistä työkulku monivaiheisille tehtäville*

**Itsearvioiva Koodi** – Tuotantotasoisen koodin generointiin. Malli luo koodia, joka noudattaa tuotantostandardeja asianmukaisella virheenkäsittelyllä. Käytä tätä uusien ominaisuuksien tai palveluiden rakentamiseen.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fi/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Itsearviointisykli" width="800"/>

*Iteratiivinen parantamissykli – generoi, arvioi, tunnista virheet, paranna, toista*

**Rakenteellinen Analyysi** – Johdonmukaiseen arviointiin. Malli tarkastaa koodin kiinteällä viitekehyksellä (oikeellisuus, käytännöt, suorituskyky, turvallisuus, ylläpidettävyys). Käytä tätä koodikatselmuksiin tai laatutarkastuksiin.

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

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatin kanssa:** Kysy rakenteellisesta analyysistä:
> - "Kuinka räätälöin analyysikehyksen erilaisten koodikatselmusten tarpeisiin?"
> - "Mikä on paras tapa jäsentää ja käsitellä rakenteellista ulostuloa ohjelmallisesti?"
> - "Kuinka varmistaa johdonmukaiset vakavuustasot eri katselukerroilla?"

<img src="../../../translated_images/fi/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Rakenteellisen Analyysin Kuvio" width="800"/>

*Viitekehys johdonmukaisille koodikatselmuksille vakavuustasoineen*

**Monivuoroinen Keskustelu** – Keskusteluihin, jotka tarvitsevat kontekstia. Malli muistaa aiemmat viestit ja rakentaa niiden päälle. Käytä tätä interaktiivisissa tukisessioissa tai monimutkaisessa kysely-vastaus -tilanteessa.

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

*Kuinka keskustelukonteksti kertyy useiden vuorojen aikana token-rajan täyttymiseen asti*

**Vaiheittainen Päättely** – Ongelmille, jotka vaativat näkyvää logiikkaa. Malli näyttää eksplisiittisen päättelyn joka askeleella. Käytä tätä matemaattisissa tehtävissä, logiikkapeleissä tai kun haluat ymmärtää ajatteluprosessia.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fi/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Vaiheittaisen Päättelyn Kuvio" width="800"/>

*Ongelmien pilkkominen eksplisiittisiin loogisiin vaiheisiin*

**Rajoitettu Tuotos** – Vastauksille, joilla on tiettyjä muoto- ja pituusvaatimuksia. Malli noudattaa tarkasti muoto- ja pituussääntöjä. Käytä tätä tiivistelmissä tai kun tarvitset täsmällistä tuotteen rakennetta.

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

<img src="../../../translated_images/fi/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Rajoitetun Tuotoksen Kuvio" width="800"/>

*Tiettyjen muoto-, pituus- ja rakennevaatimusten noudattaminen*

## Olemassa olevien Azure-resurssien käyttäminen

**Tarkista käyttöönotto:**

Varmista, että `.env`-tiedosto on olemassa juurihakemistossa Azuren tunnuksilla (luotu Moduulissa 01):
```bash
cat ../.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Käynnistä sovellus:**

> **Huom:** Jos olet jo käynnistänyt kaikki sovellukset `./start-all.sh` -komennolla Moduulissa 01, tämä moduuli on jo käynnissä portissa 8083. Voit ohittaa alla olevat käynnistyskomennot ja siirtyä suoraan osoitteeseen http://localhost:8083.

**Vaihtoehto 1: Spring Boot Dashboardin käyttäminen (suositellaan VS Code -käyttäjille)**

Kehityskontti sisältää Spring Boot Dashboard -laajennuksen, joka tarjoaa visuaalisen käyttöliittymän hallita kaikkia Spring Boot -sovelluksia. Löydät sen vasemman puolen Activity Barista (etsi Spring Boot -kuvake).

Spring Boot Dashboardista voit:
- Näyttää kaikki käytettävissä olevat Spring Boot -sovellukset työtilassa
- Käynnistää/pysäyttää sovelluksia yhdellä napsautuksella
- Tarkastella sovellusten lokitiedostoja reaaliajassa
- Seurata sovellusten tilaa
Yksinkertaisesti klikkaa toistopainiketta "prompt-engineering" kohdalla aloittaaksesi tämän moduulin, tai käynnistä kaikki moduulit kerralla.

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

Molemmat skriptit lataavat automaattisesti ympäristömuuttujat juurihakemiston `.env`-tiedostosta ja kääntävät JAR-tiedostot, jos niitä ei ole olemassa.

> **Huom:** Jos haluat kääntää kaikki moduulit manuaalisesti ennen käynnistämistä:
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

*Pääkohtaus, jossa näkyvät kaikki 8 prompt-tekniikkaa niiden ominaisuuksineen ja käyttötapauksineen*

## Tekniikoiden tutkiminen

Web-käyttöliittymä antaa sinun kokeilla eri kehotusstrategioita. Jokainen malli ratkaisee erilaisia ongelmia – kokeile niitä nähdäksesi, milloin kukin lähestymistapa toimii parhaiten.

> **Huom: Striimaus vs Ei-striimaus** — Jokaisella mallisivulla on kaksi painiketta: **🔴 Stream Response (Live)** ja **Non-streaming** -vaihtoehto. Striimaus käyttää Server-Sent Events (SSE) -tekniikkaa näyttämään tunnisteita reaaliajassa, kun malli muodostaa niitä, joten näet etenemisen heti. Ei-striimaava vaihtoehto odottaa koko vastauksen ennen näyttämistä. Syvällistä päättelyä vaativissa kehotteissa (esim. High Eagerness, Self-Reflecting Code) ei-striimaava kutsu saattaa kestää hyvin pitkään – joskus minuutteja – ilman näkyvää palautetta. **Käytä striimausta monimutkaisia kehotteita kokeillessa** nähdäksesi mallin toiminnan ja välttääksesi vaikutelman aikakatkaisusta.
>
> **Huom: Selaimen vaatimus** — Striimausominaisuus käyttää Fetch Streams API:a (`response.body.getReader()`), joka vaatii täysimittaisen selaimen (Chrome, Edge, Firefox, Safari). Se ei toimi VS Code:n sisäänrakennetussa Simple Browserissa, koska sen webview ei tue ReadableStream API:a. Simple Browseria käytettäessä ei-striimauspainikkeet toimivat normaalisti – ainoastaan striimauspainikkeet eivät toimi. Avaa `http://localhost:8083` ulkoisessa selaimessa saadaksesi täydellisen kokemuksen.

### Matala vs Korkea innokkuus

Kysy yksinkertainen kysymys, kuten "Mikä on 15 % luvusta 200?" käyttäen matalaa innokkuutta. Saat välittömän ja suoraviivaisen vastauksen. Kysy sitten monimutkaisempi, kuten "Suunnittele välimuististrategia suuren liikenteen API:lle" käyttäen korkeaa innokkuutta. Klikkaa **🔴 Stream Response (Live)** ja seuraa mallin yksityiskohtaista päättelyä token tokenilta. Sama malli, sama kysymyksen rakenne - mutta promptti kertoo, kuinka paljon ajattelua tehdä.

### Tehtävän suoritus (Työkalun johdannot)

Monivaiheiset työnkulut hyötyvät ennakkosuunnittelusta ja etenemisen kerronnasta. Malli hahmottelee, mitä se aikoo tehdä, kuvailee jokaisen vaiheen ja lopuksi tiivistää tulokset.

### Itsearvioiva koodi

Kokeile "Luo sähköpostin validointipalvelu". Sen sijaan, että malli vain generoi koodin ja pysähtyy, se generoi, arvioi laadun kriteereitä vastaan, löytää heikkoudet ja parantaa koodia. Näet sen toistavan, kunnes koodi täyttää tuotantovaatimukset.

### Rakenteellinen analyysi

Koodikatselmuksiin tarvitaan johdonmukaiset arviointikehykset. Malli analysoi koodin kiinteiden kategorioiden (oikeellisuus, käytännöt, suorituskyky, turvallisuus) ja vakavuustasojen perusteella.

### Moni-kierroksinen chat

Kysy "Mikä on Spring Boot?" ja seuraa heti perään "Näytä esimerkki". Malli muistaa ensimmäisen kysymyksesi ja antaa juuri Spring Boot -esimerkin. Ilman muistia toinen kysymys olisi liian epämääräinen.

### Askel-askeleelta päättely

Valitse matematiikan tehtävä ja kokeile sitä sekä Askel-askeleelta päättelyn että matalan innokkuuden kanssa. Matala innokkuus antaa vain vastauksen – nopeaa mutta läpinäkymätöntä. Askel-askeleelta näyttää jokaisen laskun ja päätöksen.

### Rajoitettu tulostus

Kun tarvitset tiettyjä muotoja tai sanamääriä, tämä malli pitää tiukasti kiinni vaatimuksista. Kokeile luoda tiivistelmä, jossa on täsmälleen 100 sanaa luettelona.

## Mitä todella opit

**Päättelypanos muuttaa kaiken**

GPT-5.2 antaa sinun hallita laskennallista panosta kehotteiden kautta. Matala panos tarkoittaa nopeita vastauksia vähäisellä tutkimuksella. Korkea panos tarkoittaa, että malli käyttää aikaa syvälliseen ajatteluun. Opit sovittamaan panoksen tehtävän monimutkaisuuteen – älä tuhlaa aikaa yksinkertaisiin kysymyksiin, mutta älä myöskään kiirehdi monimutkaisia päätöksiä.

**Rakenne ohjaa käyttäytymistä**

Huomasitko XML-tunnisteet kehotteissa? Ne eivät ole koristeita. Mallit noudattavat rakenteellisia ohjeita luotettavammin kuin vapaamuotoista tekstiä. Kun tarvitset monivaiheisia prosesseja tai monimutkaista logiikkaa, rakenne auttaa mallia seuraamaan, missä se on ja mitä seuraavaksi tehdään.

<img src="../../../translated_images/fi/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Hyvin rakennetun kehotteen anatomia, jossa on selkeät osiot ja XML-tyylinen järjestely*

**Laadun varmistus itsearvioinnin avulla**

Itsearvioivat mallit toimivat tekemällä laatukriteerit eksplisiittisiksi. Sen sijaan, että toivot mallin "tekevän oikein", kerrot sille tarkalleen, mitä "oikein" tarkoittaa: oikea logiikka, virheenkäsittely, suorituskyky, turvallisuus. Malli voi sitten arvioida omaa tuotostaan ja parantaa sitä. Tämä muuttaa koodin generoinnin arpajaisista hallituksi prosessiksi.

**Konteksti on rajallinen**

Monikierroksiset keskustelut toimivat sisällyttämällä viestihistorian jokaiseen pyyntöön. Mutta on olemassa raja – jokaisella mallilla on maksimimäärä tokeneita. Keskustelujen kasvaessa tarvitset strategioita pitää oleellinen konteksti, etteivät ne ylitä rajaa. Tämä moduuli näyttää, miten muisti toimii; myöhemmin opit, milloin tiivistää, milloin unohtaa ja milloin hakea tietoa.

## Seuraavat askeleet

**Seuraava moduuli:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigointi:** [← Edellinen: Moduuli 01 - Johdanto](../01-introduction/README.md) | [Takaisin päävalikkoon](../README.md) | [Seuraava: Moduuli 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:  
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, huomioithan, että automaattiset käännökset saattavat sisältää virheitä tai epätarkkuuksia. Alkuperäinen asiakirja omalla kielellään on virallinen lähde. Tärkeissä asioissa suosittelemme ammattimaista ihmiskäännöstä. Emme ole vastuussa mahdollisista väärinymmärryksistä tai virhetulkioista, jotka johtuvat tämän käännöksen käytöstä.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->