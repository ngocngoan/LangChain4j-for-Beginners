# Module 02: Kehoteinsinööritys GPT-5.2:n kanssa

## Sisällysluettelo

- [Videoesittely](../../../02-prompt-engineering)
- [Mitä opit](../../../02-prompt-engineering)
- [Esivaatimukset](../../../02-prompt-engineering)
- [Kehoteinsinöörityksen ymmärtäminen](../../../02-prompt-engineering)
- [Kehoteinsinöörityksen perusteet](../../../02-prompt-engineering)
  - [Zero-Shot-kehote](../../../02-prompt-engineering)
  - [Few-Shot-kehote](../../../02-prompt-engineering)
  - [Ajatusketju](../../../02-prompt-engineering)
  - [Roolipohjainen kehote](../../../02-prompt-engineering)
  - [Kehotepohjat](../../../02-prompt-engineering)
- [Edistyneet kaavat](../../../02-prompt-engineering)
- [Olemassa olevien Azure-resurssien käyttäminen](../../../02-prompt-engineering)
- [Sovelluksen kuvakaappaukset](../../../02-prompt-engineering)
- [Kaavojen tutkiminen](../../../02-prompt-engineering)
  - [Matala vs korkea innokkuus](../../../02-prompt-engineering)
  - [Tehtävän suoritus (työkalun esittelyt)](../../../02-prompt-engineering)
  - [Itsearvioiva koodi](../../../02-prompt-engineering)
  - [Rakenteellinen analyysi](../../../02-prompt-engineering)
  - [Monivaiheinen keskustelu](../../../02-prompt-engineering)
  - [Vaiheittainen päättely](../../../02-prompt-engineering)
  - [Rajoitettu tuloste](../../../02-prompt-engineering)
- [Mitä todella opit](../../../02-prompt-engineering)
- [Seuraavat askeleet](../../../02-prompt-engineering)

## Videoesittely

Katso tämä suora lähetys, joka selittää, miten aloitetaan tämän moduulin kanssa: [Prompt Engineering with LangChain4j - Live Session](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## Mitä opit

<img src="../../../translated_images/fi/what-youll-learn.c68269ac048503b2.webp" alt="Mitä opit" width="800"/>

Edellisessä moduulissa näit, miten muisti mahdollistaa keskustelevan tekoälyn ja käytit GitHub-malleja peruskeskusteluihin. Nyt keskitymme siihen, miten kysyt kysymyksiä — itse kehote — käyttäen Azure OpenAI:n GPT-5.2:ta. Tapa, jolla muotoilet kehotteesi, vaikuttaa dramaattisesti saamiesi vastausten laatuun. Aloitamme perustelevilla kehotetekniikoilla, ja etenemme kahdeksaan edistyneeseen kaavaan, jotka hyödyntävät GPT-5.2:n ominaisuuksia täysimittaisesti.

Käytämme GPT-5.2:ta, koska se tuo päättelyn hallinnan — voit kertoa mallille, kuinka paljon sen tulee ajatella ennen vastaamista. Tämä tekee erilaisten kehote-strategioiden erot selvemmiksi ja auttaa ymmärtämään, milloin käyttää mitäkin lähestymistapaa. Hyödymme myös Azure:n pienemmistä käyttörajoituksista GPT-5.2:ssa verrattuna GitHub-malleihin.

## Esivaatimukset

- Moduuli 01 suoritettu (Azure OpenAI -resurssit käyttöönotettu)
- `.env`-tiedosto juurihakemistossa Azure-tunnuksilla (luotu `azd up` komennolla moduulissa 01)

> **Huom:** Jos et ole suorittanut moduulia 01, noudata ensin siellä annettuja käyttöönotto-ohjeita.

## Kehoteinsinöörityksen ymmärtäminen

<img src="../../../translated_images/fi/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Mikä on kehoteinsinööritys?" width="800"/>

Kehoteinsinööritys tarkoittaa syötteen suunnittelua, jolla saat tasaisesti tarvitsemasi tulokset. Kyse ei ole pelkästään kysymysten esittämisestä — vaan pyyntöjen rakenteen tekemisestä niin, että malli ymmärtää täsmälleen mitä haluat ja miten sen tulee toimittaa.

Ajattele sitä kuin antaisit ohjeita kollegalle. "Korjaa bugi" on epämääräinen. "Korjaa null pointer exception UserService.java tiedoston rivillä 45 lisäämällä null-tarkistus" on täsmällinen. Kielenmallit toimivat samalla tavalla — täsmällisyys ja rakenne ovat tärkeitä.

<img src="../../../translated_images/fi/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Miten LangChain4j sopii" width="800"/>

LangChain4j tarjoaa infrastruktuurin — malliyhteydet, muistin ja viestityypit — kun taas kehote-kaavat ovat huolellisesti rakennetut tekstit, jotka lähetät tuon infrastruktuurin kautta. Keskeisiä rakennuspalikoita ovat `SystemMessage` (joka määrittelee tekoälyn käytöksen ja roolin) sekä `UserMessage` (joka välittää varsinaisen pyyntösi).

## Kehoteinsinöörityksen perusteet

<img src="../../../translated_images/fi/five-patterns-overview.160f35045ffd2a94.webp" alt="Viiden perustavanlaatuisen kehoteinsinöörityskaavan yleiskuva" width="800"/>

Ennen kuin sukellamme tämän moduulin edistyneisiin kaavoihin, käydään läpi viisi perustavaa kehoteinsinööritystekniikkaa. Nämä ovat rakennuspalikoita, jotka jokaisen kehoteinsinöörin tulisi tuntea. Jos olet jo käynyt läpi [Nopean aloituksen moduulin](../00-quick-start/README.md#2-prompt-patterns), olet nähnyt nämä käytännössä — tässä on niiden käsitekehys.

### Zero-Shot-kehote

Yksinkertaisin lähestymistapa: anna mallille suora ohje ilman esimerkkejä. Malli luottaa täysin koulutukseensa ymmärtääkseen ja suorittaakseen tehtävän. Tämä toimii hyvin suoraviivaisissa pyynnöissä, joissa odotettu käyttäytyminen on selvä.

<img src="../../../translated_images/fi/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot-kehote" width="800"/>

*Suora ohje ilman esimerkkejä — malli päättelee tehtävän pelkän ohjeen perusteella*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Vastaus: "Positiivinen"
```

**Milloin käyttää:** Yksinkertaisiin luokitteluihin, suoriin kysymyksiin, käännöksiin tai tehtäviin, jotka malli osaa ilman lisäohjeistusta.

### Few-Shot-kehote

Anna esimerkkejä, jotka havainnollistavat mallin haluttua toimintamallia. Malli oppii esimerkeistä odotetun syöte-tulos-muodon ja soveltaa sen uusiin syötteisiin. Tämä parantaa johdonmukaisuutta tehtävissä, joissa haluttu muoto tai käyttäytyminen ei ole ilmeinen.

<img src="../../../translated_images/fi/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot-kehote" width="800"/>

*Oppiminen esimerkeistä — malli tunnistaa kaavan ja käyttää sitä uusiin syötteisiin*

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

**Milloin käyttää:** Mukautettu luokittelu, johdonmukainen muotoilu, toimialakohtaiset tehtävät tai kun zero-shot-tulokset ovat epävakaita.

### Ajatusketju

Pyydä mallia näyttämään päättelynsä vaihe vaiheelta. Sen sijaan että se antaisi suoran vastauksen, malli purkaa ongelman ja työskentelee jokaisen osan läpi eksplisiittisesti. Tämä parantaa tarkkuutta matematiikassa, logiikassa ja monivaiheisissa päättelytehtävissä.

<img src="../../../translated_images/fi/chain-of-thought.5cff6630e2657e2a.webp" alt="Ajatusketju-kehote" width="800"/>

*Vaiheittainen päättely — monimutkaisten ongelmien jakaminen eksplisiittisiin loogisiin vaiheisiin*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Malli näyttää: 15 - 8 = 7, sitten 7 + 12 = 19 omenaa
```

**Milloin käyttää:** Matematiikka, logiikkapähkinät, virheenkorjaus tai tehtävät, joissa päättelyprosessin näyttäminen parantaa tarkkuutta ja luottamusta.

### Roolipohjainen kehote

Määritä tekoälylle rooli tai persoona ennen kysymyksen esittämistä. Tämä antaa kontekstin, joka muokkaa vastauksen sävyä, syvyyttä ja painopistettä. "Ohjelmistoarkkitehti" antaa eri neuvoja kuin "juniori kehittäjä" tai "turvallisuusauditoija".

<img src="../../../translated_images/fi/role-based-prompting.a806e1a73de6e3a4.webp" alt="Roolipohjainen kehote" width="800"/>

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

**Milloin käyttää:** Koodikatselmukset, opastus, toimialakohtainen analyysi tai tilanteissa, joissa tarvitset vastauksia, jotka sopivat tietylle asiantuntemustasolle tai näkökulmalle.

### Kehotepohjat

Luo uudelleenkäytettäviä kehote-pohjia muuttujapaikoilla. Sen sijaan, että kirjoittaisit uuden kehotteen joka kerta, määritä pohja kerran ja täytä siihen eri arvot. LangChain4j:n `PromptTemplate`-luokka tekee tämän helpoksi käyttämällä `{{variable}}`-syntaksia.

<img src="../../../translated_images/fi/prompt-templates.14bfc37d45f1a933.webp" alt="Kehotepohjat" width="800"/>

*Uudelleenkäytettävät kehote-pohjat muuttujapaikoilla — yksi pohja, monta käyttötapaa*

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

**Milloin käyttää:** Toistuvat kyselyt eri syötteillä, erätalous, uudelleenkäytettävien tekoälytyönkulkujen rakentaminen tai tilanteissa, joissa kehote-rakenne pysyy samana mutta data vaihtuu.

---

Nämä viisi perusteetta antavat sinulle vankan työkalupakin useimpiin kehotetehtäviin. Tämän moduulin loput osat rakentuvat niiden päälle kahdeksalla **edistyneellä kaavalla**, jotka hyödyntävät GPT-5.2:n päättelykontrollia, itsearviointia ja rakenteellista tulostusta.

## Edistyneet kaavat

Perusteet hallussa, siirrymme nyt kahdeksaan edistyneeseen kaavaan, jotka tekevät tästä moduulista ainutlaatuisen. Kaikki ongelmat eivät vaadi samaa lähestymistapaa. Jotkin kysymykset kaipaavat nopeita vastauksia, toiset syvällistä ajattelua. Jotkin tarvitsevat näkyvää päättelyä, toiset pelkkiä tuloksia. Alla olevat kaavat on optimoitu eri tilanteisiin — ja GPT-5.2:n päättelykontrolli tekee eroista vielä selvempiä.

<img src="../../../translated_images/fi/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Kahdeksan kehotekaavaa" width="800"/>

*Kahdeksan kehoteinsinöörityskaavan yleiskuva ja käyttötapaukset*

<img src="../../../translated_images/fi/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Päättelykontrolli GPT-5.2:n kanssa" width="800"/>

*GPT-5.2:n päättelykontrolli antaa sinun määritellä, kuinka paljon mallin tulee ajatella — nopeista suorista vastauksista syvälliseen tutkiskeluun*

**Matala innokkuus (nopea & keskittynyt)** - Yksinkertaisiin kysymyksiin, joissa haluat nopeat ja suoraviivaiset vastaukset. Malli suorittaa maksimi 2 päättelyaskelta. Käytä tätä laskuihin, hakuun tai suoraviivaisiin kysymyksiin.

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
> - "Mikä ero on matalan ja korkean innokkuuden kehote-kaavojen välillä?"
> - "Miten XML-tagit kehotteissa auttavat AI:n vastauksen jäsentämisessä?"
> - "Milloin käytän itsearviointikaavoja verrattuna suoriin ohjeisiin?"

**Korkea innokkuus (syvällinen & perusteellinen)** - Monimutkaisiin ongelmiin, joissa haluat kattavan analyysin. Malli tutkii asiaa tarkasti ja näyttää yksityiskohtaista päättelyä. Käytä tätä järjestelmäsuunnittelussa, arkkitehtuuripäätöksissä tai monimutkaisessa tutkimuksessa.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Tehtävän suoritus (vaiheittainen eteneminen)** - Monivaiheisiin työnkulkuihin. Malli antaa etukäteissuunnitelman, kuvailee jokaisen vaiheen edetessään ja lopuksi esittää yhteenvedon. Käytä tätä siirroissa, toteutuksissa tai missä tahansa monivaiheisessa prosessissa.

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

Ajatusketju-kehotte pyytää mallia näyttämään päättelyprosessinsa eksplisiittisesti, mikä parantaa tarkkuutta monimutkaisissa tehtävissä. Vaiheittainen jako auttaa sekä ihmisiä että tekoälyä ymmärtämään logiikan.

> **🤖 Kokeile [GitHub Copilot](https://github.com/features/copilot) Chatin kanssa:** Kysy tästä kaavasta:
> - "Miten mukauttaisin tehtävän suoritus -kaavaa pitkäkestoisiin toimintoihin?"
> - "Mitkä ovat parhaat käytännöt työkaluesittelyjen rakenteistamiseen tuotantosovelluksissa?"
> - "Miten voin tallentaa ja näyttää väliaikaisia etenemisilmoituksia käyttöliittymässä?"

<img src="../../../translated_images/fi/task-execution-pattern.9da3967750ab5c1e.webp" alt="Tehtävän suorituskaava" width="800"/>

*Suunnittele → Toteuta → Yhteenveto työnkulku monivaiheisiin tehtäviin*

**Itsearvioiva koodi** - Tuotantolaatuista koodia varten. Malli luo koodia, joka noudattaa tuotannon standardeja ja sisältää asianmukaisen virheenkäsittelyn. Käytä tätä, kun rakennat uusia ominaisuuksia tai palveluita.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fi/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Itsearviointisykli" width="800"/>

*Iteratiivinen parantamissilmukka - generoi, arvioi, tunnista ongelmat, paranna, toista*

**Rakenteellinen analyysi** - Johdonmukaiseen arviointiin. Malli tarkastelee koodia kiinteän kehyksen kautta (oikeellisuus, käytännöt, suorituskyky, turvallisuus, ylläpidettävyys). Käytä tätä koodikatselmuksiin tai laadun arviointiin.

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
> - "Miten voin räätälöidä analyysikehystä eri koodikatselmustyypeille?"
> - "Mikä on paras tapa jäsentää ja toimia rakenteellisen tulosteen kanssa ohjelmallisesti?"
> - "Miten varmistetaan johdonmukaiset vakavuustasot eri katselmusistuntojen välillä?"

<img src="../../../translated_images/fi/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Rakenteellisen analyysin kaava" width="800"/>

*Johdonmukaisen koodikatselmuksen kehys vakavuustasoineen*

**Monivaiheinen keskustelu** - Keskustelut, jotka tarvitsevat kontekstin. Malli muistaa aiemmat viestit ja rakentaa niiden päälle. Käytä tätä interaktiivisiin tukisessioihin tai monimutkaisiin kysymys-vastaus -tilanteisiin.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/fi/context-memory.dff30ad9fa78832a.webp" alt="Keskustelun kontekstimuiisti" width="800"/>

*Kuinka keskustelun konteksti kertyy useiden kierrosten ajan, kunnes token-raja ylittyy*

**Vaiheittainen päättely** - Ongelmille, jotka vaativat näkyvää logiikkaa. Malli näyttää eksplisiittisen päättelyn jokaiselle askeleelle. Käytä tätä matematiikkaan, logiikkapähkinöihin tai silloin, kun haluat ymmärtää ajatteluprosessia.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fi/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Vaiheittaisen päättelyn kaava" width="800"/>

*Ongelmien jakaminen eksplisiittisiin loogisiin vaiheisiin*

**Rajoitettu tuloste** - Vastausten formaatin tarkkoja vaatimuksia varten. Malli noudattaa tiukasti muoto- ja pituussääntöjä. Käytä tätä tiivistelmiin tai silloin, kun tarvitset tarkan ulostulorakenteen.

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

<img src="../../../translated_images/fi/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Rajoitetun tulosteen kaava" width="800"/>

*Tarkat formaatti-, pituus- ja rakennevaatimusten noudattaminen*

## Olemassa olevien Azure-resurssien käyttäminen

**Tarkista käyttöönotto:**

Varmista, että `.env`-tiedosto on juurihakemistossa ja sisältää Azure-tunnukset (luotu moduulin 01 aikana):
```bash
cat ../.env  # Tulisi näyttää AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Käynnistä sovellus:**

> **Huom:** Jos olet jo käynnistänyt kaikki sovellukset käyttämällä `./start-all.sh` komentoa moduulissa 01, tämä moduuli on jo käynnissä portissa 8083. Voit hypätä alla olevat käynnistyskomennot yli ja mennä suoraan osoitteeseen http://localhost:8083.

**Vaihtoehto 1: Spring Boot -hallintapaneelin käyttäminen (suositeltu VS Code -käyttäjille)**
Kehityssäiliö sisältää Spring Boot Dashboard -laajennuksen, joka tarjoaa visuaalisen käyttöliittymän kaikkien Spring Boot -sovellusten hallintaan. Löydät sen VS Coden vasemman reunan Aktiviteettipalkista (etsi Spring Boot -kuvaketta).

Spring Boot Dashboardista voit:
- Näyttää kaikki käytettävissä olevat Spring Boot -sovellukset työtilassa
- Käynnistää/pysäyttää sovelluksia yhdellä napsautuksella
- Katsoa sovelluslokeja reaaliajassa
- Seurata sovelluksen tilaa

Napsauta yksinkertaisesti "prompt-engineering" moduulin vieressä olevaa toistopainiketta käynnistääksesi tämän moduulin, tai käynnistä kaikki moduulit kerralla.

<img src="../../../translated_images/fi/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Vaihtoehto 2: Shell-skriptien käyttäminen**

Käynnistä kaikki web-sovellukset (moduulit 01-04):

**Bash:**
```bash
cd ..  # Juurikansiosta
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

Molemmat skriptit lataavat automaattisesti ympäristömuuttujat juurihakemiston `.env`-tiedostosta ja rakentavat JAR-tiedostot, jos niitä ei ole.

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
.\stop.ps1  # Tämä moduuli vain
# Tai
cd ..; .\stop-all.ps1  # Kaikki moduulit
```

## Sovelluksen kuvaruutukuvat

<img src="../../../translated_images/fi/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Pääpaneeli, joka näyttää kaikki 8 prompt engineering -mallia niiden ominaisuuksineen ja käyttötapauksineen*

## Mallien tutkiminen

Web-käyttöliittymä antaa sinun kokeilla erilaisia promptausstrategioita. Jokainen malli ratkaisee erilaisia ongelmia – kokeile niitä nähdäksesi, milloin kukin lähestymistapa toimii parhaiten.

> **Huom: Suoratoisto vs. ei-suoratoisto** — Jokaisella mallisivulla on kaksi painiketta: **🔴 Stream Response (Live)** ja **Ei-suoratoisto** -vaihtoehto. Suoratoisto käyttää Server-Sent Events (SSE) -tekniikkaa näyttääkseen tokeneita reaaliajassa mallin generoitaessa niitä, joten näet etenemisen heti. Ei-suoratoistovaihtoehto odottaa koko vastauksen valmistumista ennen näyttämistä. Syväanalyysiä vaativien kehotteiden (esim. High Eagerness, Self-Reflecting Code) ei-suoratoistokutsu voi kestää erittäin pitkään — joskus minuutteja — ilman näkyvää palautetta. **Käytä suoratoistoa monimutkaisia kehotteita kokeillessasi**, niin näet mallin toimivan ja vältyt vaikutelmalta, että pyyntö on aikakatkaistu.
>
> **Huom: Selaimen vaatimukset** — Suoratoisto-ominaisuus käyttää Fetch Streams API:a (`response.body.getReader()`), joka vaatii täysimittaisen selaimen (Chrome, Edge, Firefox, Safari). Se ei toimi VS Coden sisäisessä Simple Browserissa, koska sen webview ei tue ReadableStream API:a. Jos käytät Simple Browseria, ei-suoratoistopainikkeet toimivat normaalisti – pelkästään suoratoistopainikkeet ovat rajoitettuja. Avaa `http://localhost:8083` ulkoisessa selaimessa saadaksesi täyden kokemuksen.

### Matala vs. korkea innokkuus

Kysy yksinkertainen kysymys kuten "Mikä on 15% luvusta 200?" käyttäen matalaa innokkuutta. Saat välittömän ja suoraviivaisen vastauksen. Kysy nyt jotain monimutkaisempaa, kuten "Suunnittele välimuististrategia korkealiikenteiselle API:lle" käyttäen korkeaa innokkuutta. Klikkaa **🔴 Stream Response (Live)** ja seuraa mallin yksityiskohtaista päättelyä token tokenilta. Sama malli, sama kysymysrakenne – mutta kehotteessa kerrotaan, kuinka paljon mallin tulee miettiä.

### Tehtävien suoritus (työkalun alkutekstit)

Monivaiheiset työnkulut hyötyvät etukäteissuunnittelusta ja etenemiskertomuksesta. Malli kuvailee suunnitelmansa, selostaa jokaisen vaiheen ja lopuksi tiivistää tulokset.

### Itsearvioiva koodi

Kokeile "Luo sähköpostin validointipalvelu". Sen sijaan, että mallin generoima koodi olisi vain valmis, malli laatii, arvioi laatukriteerien mukaan, tunnistaa puutteet ja parantaa koodia. Näet sen tekevän toistoja, kunnes koodi täyttää tuotantostandardit.

### Jäsennelty analyysi

Koodikatselmukset tarvitsevat johdonmukaiset arviointikehykset. Malli analysoi koodin kiinteillä kategorioilla (oikeellisuus, käytännöt, suorituskyky, turvallisuus) käyttäen vakavuustasoja.

### Monikierroksinen keskustelu

Kysy "Mikä on Spring Boot?" ja seuraa välittömästi kysymällä "Näytä minulle esimerkki". Malli muistaa ensimmäisen kysymyksesi ja antaa juuri Spring Boot -esimerkin. Ilman muistia tuo toinen kysymys olisi liian epämääräinen.

### Askeltainen päättely

Valitse matemaattinen ongelma ja kokeile sitä sekä askeltaisen päättelyn että matalan innokkuuden kanssa. Matala innokkuus antaa vastauksen nopeasti, mutta pinnallisesti. Askeltainen päättely näyttää jokaisen laskun ja päätöksen.

### Rajoitettu tuloste

Kun tarvitset tiettyjä muotoja tai sanojen määrän, tämä malli valvoo tarkasti vaatimusten noudattamista. Kokeile luoda yhteenveto, jossa on täsmälleen 100 sanaa ja luettelomuodossa.

## Mitä oikeasti opit

**Päättelypanos muuttaa kaiken**

GPT-5.2 antaa sinun säädellä laskennallista panosta kehotteillasi. Matala panos tarkoittaa nopeita vastauksia ja vähäistä tutkiskelua. Korkea panos saa mallin miettimään syvällisesti. Opit sovittamaan ponnistelun tehtävän monimutkaisuuteen – älä tuhlaa aikaa yksinkertaisiin kysymyksiin, mutta älä myöskään kiirehdi monimutkaisia päätöksiä.

**Rakenne ohjaa toimintaa**

Huomaatko kehotteiden XML-tunnisteet? Ne eivät ole koristeita. Mallit seuraavat jäsenneltyjä ohjeita luotettavammin kuin vapaamuotoista tekstiä. Kun tarvitset monivaiheisia prosesseja tai monimutkaista logiikkaa, rakenne auttaa mallia seuraamaan, missä se on ja mitä tulee seuraavaksi.

<img src="../../../translated_images/fi/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Hyvin jäsennetyn kehotteen anatomia selkeillä osioilla ja XML-tyylisellä järjestelyllä*

**Laatu itsearvioinnin kautta**

Itsearvioivat mallit toimivat tekemällä laatukriteerit eksplisiittisiksi. Sen sijaan, että toivoisit mallin "tekevän oikein", kerrot sille täsmälleen, mitä "oikein" tarkoittaa: oikea logiikka, virheenkäsittely, suorituskyky, turvallisuus. Malli voi sitten arvioida omaa tuotostaan ja parantaa sitä. Tämä muuttaa koodin generoinnin arpajaisesta hallituksi prosessiksi.

**Konteksti on rajallinen**

Monikierroksiset keskustelut toimivat sisältämällä viestihistorian jokaisen pyynnön mukana. Mutta on rajansa – jokaisella mallilla on maksimimäärä tokeneita. Keskustelujen kasvaessa tarvitset strategioita pitää relevantti konteksti ilman, että ylität tämän rajan. Tämä moduuli näyttää, miten muisti toimii; myöhemmin opit, milloin tiivistää, milloin unohtaa ja milloin hakea tiedot.

## Seuraavat askeleet

**Seuraava moduuli:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigointi:** [← Edellinen: Moduuli 01 - Johdanto](../01-introduction/README.md) | [Takaisin pääsivulle](../README.md) | [Seuraava: Moduuli 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty tekoälypohjaisella käännöspalvelulla [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, otathan huomioon, että automaattikäännöksissä saattaa esiintyä virheitä tai epätarkkuuksia. Alkuperäinen asiakirja sen omalla kielellä tulee pitää ensisijaisena lähteenä. Tärkeissä asioissa suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa käännöksen käytöstä aiheutuvista väärinymmärryksistä tai virhetulkintojen seurauksista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->