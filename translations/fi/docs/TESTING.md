# LangChain4j-sovellusten testaus

## Sisällysluettelo

- [Pikaopas](../../../docs)
- [Mitä testit kattavat](../../../docs)
- [Testien suorittaminen](../../../docs)
- [Testien suorittaminen VS Codessa](../../../docs)
- [Testausmallit](../../../docs)
- [Testausfilosofia](../../../docs)
- [Seuraavat askeleet](../../../docs)

Tämä opas kävelee sinut läpi testien, jotka osoittavat, kuinka testata tekoälysovelluksia ilman, että tarvitset API-avaimia tai ulkoisia palveluita.

## Pikaopas

Suorita kaikki testit yhdellä komennolla:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

Kun kaikki testit onnistuvat, näet alla olevan kuvakaappauksen kaltaisen lopputuloksen — testit suoritetaan ilman yhtään virhettä.

<img src="../../../translated_images/fi/test-results.ea5c98d8f3642043.webp" alt="Onnistuneet testitulokset" width="800"/>

*Onnistuneen testiajon tulos näyttää kaikki testit läpäistyinä ilman virheitä*

## Mitä testit kattavat

Tämä kurssi keskittyy **yksikkötesteihin**, jotka suoritetaan paikallisesti. Jokainen testi esittelee tietyn LangChain4j-konseptin erillään. Alla oleva testauspyramidi näyttää, mihin yksikkötestit sopivat — ne muodostavat nopean ja luotettavan perustan, jolle muu testausstrategiasi rakentuu.

<img src="../../../translated_images/fi/testing-pyramid.2dd1079a0481e53e.webp" alt="Testauspyramidi" width="800"/>

*Testauspyramidi näyttää tasapainon yksikkötestien (nopeat, eristetyt), integraatiotestien (todelliset komponentit) ja loppukäyttäjätestien välillä. Tämä koulutus kattaa yksikkötestauksen.*

| Moduuli | Testit | Painopiste | Keskeiset tiedostot |
|--------|-------|-------|-----------|
| **00 - Pikaopas** | 6 | Kehote-pohjat ja muuttujien korvaus | `SimpleQuickStartTest.java` |
| **01 - Johdanto** | 8 | Keskustelumuisti ja tilallinen keskustelu | `SimpleConversationTest.java` |
| **02 - Kehoteinsinöörityö** | 12 | GPT-5.2-kuviot, innokkuustasot, jäsennelty tuloste | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Asiakirjojen sisäänotto, upotukset, samankaltaisuushaku | `DocumentServiceTest.java` |
| **04 - Työkalut** | 12 | Funktiokutsut ja työkaluketjut | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol stdio-siirrolla | `SimpleMcpTest.java` |

## Testien suorittaminen

**Suorita kaikki testit juurihakemistosta:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Suorita testit tietylle moduulille:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Tai juurihakemistosta
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Tai juurihakemistosta
mvn --% test -pl 01-introduction
```

**Suorita yksittäinen testiluokka:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Suorita tietty testimetodi:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#tulisiSäilyttääKeskusteluhistoria
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#tulisiko säilyttää keskusteluhistoria
```

## Testien suorittaminen VS Codessa

Jos käytät Visual Studio Codea, Test Explorer tarjoaa graafisen käyttöliittymän testien suorittamiseen ja virheenkorjaukseen.

<img src="../../../translated_images/fi/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer näyttää testipuuston, jossa kaikki Java-testiluokat ja yksittäiset testimetodit*

**Testien suorittaminen VS Codessa:**

1. Avaa Test Explorer napsauttamalla reagenssipulloa Aktiviteettipalkissa
2. Laajenna testipuuta nähdäksesi kaikki moduulit ja testiluokat
3. Napsauta esityspainiketta minkä tahansa testin vieressä suorittaaksesi sen yksittäin
4. Napsauta "Run All Tests" suorittaaksesi koko testipaketin
5. Oikeaklikkaa mitä tahansa testiä ja valitse "Debug Test" asettaaksesi taukopaikkoja ja astuaksesi koodiin

Test Explorer näyttää vihreät valintamerkit läpäistyille testeille ja antaa yksityiskohtaiset virheilmoitukset, jos testit epäonnistuvat.

## Testausmallit

### Kuvio 1: Kehote-mallipohjien testaus

Yksinkertaisin kuvio testaa kehote-mallipohjia ilman, että kutsutaan mitään tekoälymallia. Varmistat, että muuttujien korvaus toimii oikein ja kehote on muotoiltu odotetusti.

<img src="../../../translated_images/fi/prompt-template-testing.b902758ddccc8dee.webp" alt="Kehote-mallipohjan testaus" width="800"/>

*Kehote-mallipohjien testaus näyttää muuttujien korvausprosessin: pohja paikkamerkeillä → arvot sovelletaan → muotoiltu tuloste tarkistetaan*

```java
@Test
@DisplayName("Should format prompt template with variables")
void testPromptTemplateFormatting() {
    PromptTemplate template = PromptTemplate.from(
        "Best time to visit {{destination}} for {{activity}}?"
    );
    
    Prompt prompt = template.apply(Map.of(
        "destination", "Paris",
        "activity", "sightseeing"
    ));
    
    assertThat(prompt.text()).isEqualTo("Best time to visit Paris for sightseeing?");
}
```

Tämä testi sijaitsee tiedostossa `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Suorita:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testaaKehyksenMuotoilua
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testaaKehyksenMuotoilua
```

### Kuvio 2: Kielen mallien mokkaus

Keskustelulogiikan testaamiseen käytä Mockitoa luomaan väärennettyjä malleja, jotka palauttavat ennalta määritettyjä vastauksia. Näin testit ovat nopeita, ilmaisia ja määrätietoisia.

<img src="../../../translated_images/fi/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mokki vs Todellinen API -vertailu" width="800"/>

*Vertailu, miksi mokkeja suositaan testauksessa: ne ovat nopeita, ilmaisia, määrätietoisia eikä niihin tarvita API-avaimia*

```java
@ExtendWith(MockitoExtension.class)
class SimpleConversationTest {
    
    private ConversationService conversationService;
    
    @Mock
    private OpenAiOfficialChatModel mockChatModel;
    
    @BeforeEach
    void setUp() {
        ChatResponse mockResponse = ChatResponse.builder()
            .aiMessage(AiMessage.from("This is a test response"))
            .build();
        when(mockChatModel.chat(anyList())).thenReturn(mockResponse);
        
        conversationService = new ConversationService(mockChatModel);
    }
    
    @Test
    void shouldMaintainConversationHistory() {
        String conversationId = conversationService.startConversation();
        
        ChatResponse mockResponse1 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 1"))
            .build();
        ChatResponse mockResponse2 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 2"))
            .build();
        ChatResponse mockResponse3 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 3"))
            .build();
        
        when(mockChatModel.chat(anyList()))
            .thenReturn(mockResponse1)
            .thenReturn(mockResponse2)
            .thenReturn(mockResponse3);

        conversationService.chat(conversationId, "First message");
        conversationService.chat(conversationId, "Second message");
        conversationService.chat(conversationId, "Third message");

        List<ChatMessage> history = conversationService.getHistory(conversationId);
        assertThat(history).hasSize(6); // 3 käyttäjä- + 3 tekoälyviestiä
    }
}
```

Tämä kuvio esiintyy tiedostossa `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mokki varmistaa johdonmukaisen käyttäytymisen, jotta voit tarkistaa, että muistin hallinta toimii oikein.

### Kuvio 3: Keskustelun eristäminen

Keskustelumuistin tulee pitää useat käyttäjät erillään. Tämä testi varmistaa, etteivät keskustelut sekoita konteksteja.

<img src="../../../translated_images/fi/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Keskustelun eristäminen" width="800"/>

*Keskustelun eristämisen testaus osoittaa erilliset muistikaupat eri käyttäjille kontekstin sekoittumisen estämiseksi*

```java
@Test
void shouldIsolateConversationsByid() {
    String conv1 = conversationService.startConversation();
    String conv2 = conversationService.startConversation();
    
    ChatResponse mockResponse = ChatResponse.builder()
        .aiMessage(AiMessage.from("Response"))
        .build();
    when(mockChatModel.chat(anyList())).thenReturn(mockResponse);

    conversationService.chat(conv1, "Message for conversation 1");
    conversationService.chat(conv2, "Message for conversation 2");

    List<ChatMessage> history1 = conversationService.getHistory(conv1);
    List<ChatMessage> history2 = conversationService.getHistory(conv2);
    
    assertThat(history1).hasSize(2);
    assertThat(history2).hasSize(2);
}
```

Jokaisella keskustelulla on oma itsenäinen historiansa. Tuotantojärjestelmissä tämä eristys on kriittinen monikäyttäjäsovelluksille.

### Kuvio 4: Työkalujen erillinen testaus

Työkalut ovat toimintoja, joita tekoäly voi kutsua. Testaa niitä suoraan varmistaaksesi, että ne toimivat oikein riippumatta tekoälyn päätöksistä.

<img src="../../../translated_images/fi/tools-testing.3e1706817b0b3924.webp" alt="Työkalujen testaus" width="800"/>

*Työkalujen itsenäinen testaus näyttää moki-työkalun suorituksen ilman tekoälykutsuja liiketoimintalogiikan varmistamiseksi*

```java
@Test
void shouldConvertCelsiusToFahrenheit() {
    TemperatureTool tempTool = new TemperatureTool();
    String result = tempTool.celsiusToFahrenheit(25.0);
    assertThat(result).containsPattern("77[.,]0°F");
}

@Test
void shouldDemonstrateToolChaining() {
    WeatherTool weatherTool = new WeatherTool();
    TemperatureTool tempTool = new TemperatureTool();

    String weatherResult = weatherTool.getCurrentWeather("Seattle");
    assertThat(weatherResult).containsPattern("\\d+°C");

    String conversionResult = tempTool.celsiusToFahrenheit(22.0);
    assertThat(conversionResult).containsPattern("71[.,]6°F");
}
```

Nämä testit tiedostosta `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` validoivat työkalulogiikan ilman tekoälyosallistumista. Ketjutusesimerkki näyttää, kuinka yhden työkalun tulos syötetään toisen syötteeksi.

### Kuvio 5: Muistiin perustuva RAG-testaus

RAG-järjestelmät vaativat perinteisesti vektoripohjaiset tietokannat ja upotuspalvelut. Muistiin perustuva kuvio antaa sinun testata koko putkea ilman ulkoisia riippuvuuksia.

<img src="../../../translated_images/fi/rag-testing.ee7541b1e23934b1.webp" alt="Muistiin perustuva RAG-testaus" width="800"/>

*Muistiin perustuva RAG-testing työnkulku näyttää asiakirjan jäsentämisen, upotustallennuksen ja samankaltaisuushaun ilman tietokantaa*

```java
@Test
void testProcessTextDocument() {
    String content = "This is a test document.\nIt has multiple lines.";
    InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    
    DocumentService.ProcessedDocument result = 
        documentService.processDocument(inputStream, "test.txt");

    assertNotNull(result);
    assertTrue(result.segments().size() > 0);
    assertEquals("test.txt", result.segments().get(0).metadata().getString("filename"));
}
```

Tämä testi tiedostosta `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` luo asiakirjan muistiin ja varmistaa pilkkomisen ja metatietojen käsittelyn.

### Kuvio 6: MCP-integraatiotestaus

MCP-moduuli testaa Model Context Protocol -integraatiota stdio-siirron avulla. Nämä testit varmistavat, että sovelluksesi voi käynnistää ja kommunikoida MCP-palvelimien kanssa aliprosesseina.

Testit tiedostossa `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` validoivat MCP-asiakkaan käyttäytymisen.

**Suorita ne:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Testausfilosofia

Testaa koodiasi, älä tekoälyä. Testiesi tulisi validoida kirjoittamasi koodi tarkistamalla, miten kehotteet rakentuvat, miten muistia hallitaan ja miten työkalut suoritetaan. Tekoälyn vastaukset vaihtelevat eikä niiden pitäisi olla osa testiväittämiä. Kysy itseltäsi, korvaako kehote-mallisi muuttujat oikein, älä sitä, antaako tekoäly oikean vastauksen.

Käytä mokkeja kielimalleille. Ne ovat ulkoisia riippuvuuksia, jotka ovat hitaita, kalliita ja epädeterministisiä. Mokkaus tekee testeistä nopeita, millisekunneissa sekuntien sijaan; ilmaisia ilman API-kustannuksia ja määrätietoisia samaan tulokseen joka kerta.

Pidä testit itsenäisinä. Jokaisen testin tulisi luoda omat datansa, olla riippumatta muista testeistä ja siivota jälkensä. Testien tulee läpäistä ajosta riippumatta.

Testaa reunatapauksia onnellisen polun ulkopuolella. Kokeile tyhjiä syötteitä, hyvin suuria syötteitä, erikoismerkkejä, virheellisiä parametreja ja raja-arvoja. Nämä paljastavat usein virheitä, joita normaali käyttö ei paljasta.

Käytä kuvaavia nimiä. Vertaa `shouldMaintainConversationHistoryAcrossMultipleMessages()` ja `test1()` -nimiä. Ensimmäinen kertoo tarkalleen, mitä testataan, jolloin virheenkorjaus on helpompaa.

## Seuraavat askeleet

Nyt kun ymmärrät testausmallit, sukella syvemmälle kuhunkin moduuliin:

- **[00 - Pikaopas](../00-quick-start/README.md)** - Aloita kehote-mallipohjien perusteista
- **[01 - Johdanto](../01-introduction/README.md)** - Opi keskustelumuistin hallinta
- **[02 - Kehoteinsinöörityö](../02/prompt-engineering/README.md)** - Hallitse GPT-5.2-kehote-kuviot
- **[03 - RAG](../03-rag/README.md)** - Rakenna tiedonhakupohjaiset generointijärjestelmät
- **[04 - Työkalut](../04-tools/README.md)** - Toteuta funktiokutsut ja työkaluketjut
- **[05 - MCP](../05-mcp/README.md)** - Integroi Model Context Protocol

Jokaisen moduulin README tarjoaa yksityiskohtaiset selitykset täällä testatuista konsepteista.

---

**Navigointi:** [← Takaisin pääsivulle](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty AI-käännöspalvelulla [Co-op Translator](https://github.com/Azure/co-op-translator). Pyrimme tarkkuuteen, mutta ole hyvä ja huomioi, että automaattiset käännökset saattavat sisältää virheitä tai epätarkkuuksia. Alkuperäistä asiakirjaa sen omalla kielellä on pidettävä auktoritatiivisena lähteenä. Tärkeiden tietojen osalta suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä aiheutuvista väärinymmärryksistä tai virheellisistä tulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->