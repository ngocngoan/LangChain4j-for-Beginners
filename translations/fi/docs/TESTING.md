# LangChain4j-sovellusten testaus

## Sisällysluettelo

- [Nopea alku](../../../docs)
- [Mitä testit kattavat](../../../docs)
- [Testien suorittaminen](../../../docs)
- [Testien suorittaminen VS Codessa](../../../docs)
- [Testausmallit](../../../docs)
- [Testausfilosofia](../../../docs)
- [Seuraavat askeleet](../../../docs)

Tämä opas ohjaa sinua testien läpi, jotka osoittavat, miten AI-sovelluksia testataan ilman API-avaimia tai ulkopuolisia palveluita.

## Nopea alku

Suorita kaikki testit yhdellä komennolla:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/fi/test-results.ea5c98d8f3642043.webp" alt="Onnistuneet testitulokset" width="800"/>

*Onnistuneen testisuorituksen näyttö, jossa kaikki testit menevät läpi ilman virheitä*

## Mitä testit kattavat

Tämä kurssi keskittyy **yksikkötesteihin**, jotka ajetaan paikallisesti. Jokainen testi demonstroi tiettyä LangChain4j-konseptia erillään.

<img src="../../../translated_images/fi/testing-pyramid.2dd1079a0481e53e.webp" alt="Testauspyramidi" width="800"/>

*Testauspyramidi esittää tasapainoa yksikkötestien (nopeat, eristetyt), integraatiotestien (todelliset komponentit) ja end-to-end-testien välillä. Tämä koulutus kattaa yksikkötestauksen.*

| Moduuli | Testit | Painopiste | Keskeiset tiedostot |
|--------|-------|-------|-----------|
| **00 - Nopea alku** | 6 | Kehote-pohjat ja muuttujien korvaaminen | `SimpleQuickStartTest.java` |
| **01 - Johdanto** | 8 | Keskustelumuisti ja tilallinen chat | `SimpleConversationTest.java` |
| **02 - Kehotteiden suunnittelu** | 12 | GPT-5.2 -mallit, innokkuustasot, jäsennelty ulostulo | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Dokumenttien syöttö, upotukset, samankaltaisten haku | `DocumentServiceTest.java` |
| **04 - Työkalut** | 12 | Funktiokutsut ja työkaluketjut | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Mallikontekstiprotokolla Stdio-siirrolla | `SimpleMcpTest.java` |

## Testien suorittaminen

**Suorita kaikki testit juuri-kansiosta:**

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

**Suorita yksi testiluokka:**

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
mvn test -Dtest=SimpleConversationTest#tulisi ylläpitää keskusteluhistoriaa
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#tulisiYlläpitääKeskusteluhistoriaa
```

## Testien suorittaminen VS Codessa

Jos käytät Visual Studio Codea, Test Explorer tarjoaa graafisen käyttöliittymän testien suorittamiseen ja virheenkorjaukseen.

<img src="../../../translated_images/fi/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer näyttää testipuun kaikista Java-testiluokista ja yksittäisistä testimetodeista*

**Testien suorittaminen VS Codessa:**

1. Avaa Test Explorer klikkaamalla koeputki-kuvaketta Activity Barissa
2. Pura testipuu nähdäksesi kaikki moduulit ja testiluokat
3. Klikkaa play-painiketta minkä tahansa testin vieressä suorittaaksesi sen erikseen
4. Klikkaa "Run All Tests" suorittaaksesi koko testipaketin
5. Klikkaa testin päällä hiiren oikealla ja valitse "Debug Test" asettaaksesi breakpointteja ja askeltaaksesi koodin läpi

Test Explorer näyttää vihreät rastit läpimenneistä testeistä ja tarjoaa yksityiskohtaiset epäonnistumisviestit, kun testit epäonnistuvat.

## Testausmallit

### Malli 1: Kehote-pohjien testaus

Yksinkertaisin malli testaa kehotepohjia kutsumatta mitään AI-mallia. Varmistat, että muuttujien korvaus toimii oikein ja kehotteet ovat odotetussa muodossa.

<img src="../../../translated_images/fi/prompt-template-testing.b902758ddccc8dee.webp" alt="Kehotepohjan testaus" width="800"/>

*Kehotepohjien testaus, jossa muuttujien korvausvirtaus: pohja paikkamerkkien kanssa → arvot sovelletaan → muotoiltu ulostulo varmistetaan*

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

**Suorita se:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testaaKehyspohjanMuotoilu
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testaaPromptinMallipohjanMuotoilu
```

### Malli 2: Kielen mallien mokkaus

Keskustelulogiiikkaa testatessa käytä Mockitoa luomaan väärennettyjä malleja, jotka palauttavat ennalta määrättyjä vastauksia. Tämä tekee testeistä nopeita, ilmaisia ja deterministisiä.

<img src="../../../translated_images/fi/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Vertailu mokin ja oikean API:n välillä" width="800"/>

*Vertailu, joka näyttää, miksi mokkaus on suositeltavaa testauksessa: ne ovat nopeita, ilmaisia, deterministisiä eivätkä vaadi API-avaimia*

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

Tätä mallia käytetään tiedostossa `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mokki varmistaa johdonmukaisen käyttäytymisen, jotta muistin hallintaa voi tarkistaa oikein.

### Malli 3: Keskustelun eristäminen

Keskustelumuistin täytyy pitää useat käyttäjät erillään. Tämä testi varmistaa, etteivät keskustelut sekoita konteksteja keskenään.

<img src="../../../translated_images/fi/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Keskustelun eristäminen" width="800"/>

*Keskustelun eristämisen testaus, jossa eri käyttäjillä on omat muistinsa kontekstien sekoittamisen estämiseksi*

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

Jokainen keskustelu ylläpitää omaa itsenäistä historiaansa. Tuotantojärjestelmissä tämä eristys on elintärkeä monen käyttäjän sovelluksissa.

### Malli 4: Työkalujen testaaminen erikseen

Työkalut ovat funktioita, joita AI voi kutsua. Testaa niitä suoraan varmistaaksesi, että ne toimivat oikein riippumatta AI:n päätöksistä.

<img src="../../../translated_images/fi/tools-testing.3e1706817b0b3924.webp" alt="Työkalujen testaus" width="800"/>

*Työkalujen testaus erikseen, jossa mokkaustyökaluja käytetään ilman AI-kutsuja liiketoimintalogiikan tarkistamiseen*

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

Nämä testit tiedostosta `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` validoivat työkalulogiiikan ilman AI:n osallistumista. Ketjutusesimerkki näyttää, miten yhden työkalun ulostulo syötetään toisen työkalun sisääntuloon.

### Malli 5: Muistissa tapahtuva RAG-testaus

RAG-järjestelmät vaativat perinteisesti vektorikantoja ja upotupalveluita. Muistissa tapahtuva malli antaa testata koko putken ilman ulkopuolisia riippuvuuksia.

<img src="../../../translated_images/fi/rag-testing.ee7541b1e23934b1.webp" alt="Muistissa tapahtuva RAG-testaus" width="800"/>

*Muistissa tapahtuva RAG-testaus työnkulku, jossa dokumentti jäsennetään, upotukset tallennetaan ja samankaltaisten haku tehdään ilman tietokantaa*

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

Tämä testi tiedostosta `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` luo muistissa dokumentin ja varmistaa pilkkomisen ja metatietojen käsittelyn.

### Malli 6: MCP-integraatiotestaus

MCP-moduuli testaa Model Context Protocol -integraatiota stdio-siirron avulla. Nämä testit varmistavat, että sovellus voi käynnistää ja kommunikoida MCP-palvelimien kanssa aliohjelmina.

Testit tiedostossa `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` validoivat MCP-asiakasohjelman käyttäytymistä.

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

Testaa koodiasi, ei AI:ta. Testiesi tulisi validoida kirjoittamasi koodi tarkistamalla, miten kehotteet rakennetaan, miten muisti hallitaan ja miten työkalut suoritetaan. AI-vastaukset vaihtelevat, eikä niitä pitäisi käyttää testiväitteiden osana. Kysy itseltäsi, korvaako kehote-pohjasi muuttujat oikein, ei tarjoaanko AI oikeaa vastausta.

Käytä mokkeja kielimalleille. Ne ovat ulkoisia riippuvuuksia, jotka ovat hitaita, kalliita ja epädeterministisiä. Mokkaus tekee testeistä nopeita, millisekunneissa sekuntien sijaan, ilmaisia ilman API-kustannuksia ja deterministisiä aina samaan tulokseen johtavia.

Pidä testit itsenäisinä. Jokaisen testin tulee luoda omat lähtötietonsa, olla riippumaton muista testeistä ja siivota jälkensä. Testien tulee mennä läpi riippumatta suoritusjärjestyksestä.

Testaa reunatapaukset ilon polun ulkopuolella. Kokeile tyhjiä syötteitä, hyvin suuria syötteitä, erikoismerkkejä, virheellisiä parametreja ja rajaehdotuksia. Nämä usein paljastavat virheitä, joita normaali käyttö ei paljasta.

Käytä kuvaavia nimiä. Vertaa `shouldMaintainConversationHistoryAcrossMultipleMessages()` ja `test1()`. Ensimmäinen kertoo täsmälleen, mitä testataan, mikä helpottaa vikojen korjausta huomattavasti.

## Seuraavat askeleet

Nyt kun ymmärrät testausmallit, tutustu syvällisemmin kuhunkin moduuliin:

- **[00 - Nopea alku](../00-quick-start/README.md)** - Aloita kehotepohjien perusteista
- **[01 - Johdanto](../01-introduction/README.md)** - Opi keskustelumuistin hallinta
- **[02 - Kehotteiden suunnittelu](../02/prompt-engineering/README.md)** - Hallitse GPT-5.2-kehotesuunnittelumallit
- **[03 - RAG](../03-rag/README.md)** - Rakenna hakuja parannettuja generointijärjestelmiä
- **[04 - Työkalut](../04-tools/README.md)** - Toteuta funktiokutsut ja työkaluketjut
- **[05 - MCP](../05-mcp/README.md)** - Integroidu Mallikontekstiprotokollaan

Jokaisen moduulin README tarjoaa yksityiskohtaiset selitykset tässä testatuista konsepteista.

---

**Navigointi:** [← Takaisin pääsivulle](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, huomioithan, että automaattiset käännökset saattavat sisältää virheitä tai epätarkkuuksia. Alkuperäinen asiakirja sen alkuperäiskielellä on virallinen lähde. Tärkeiden tietojen osalta suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä aiheutuvista väärinkäsityksistä tai virhetulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->