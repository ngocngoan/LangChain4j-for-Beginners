# LangChain4j programų testavimas

## Turinys

- [Greitas pradėjimas](../../../docs)
- [Ką apima testai](../../../docs)
- [Testų vykdymas](../../../docs)
- [Testavimas VS Code](../../../docs)
- [Testavimo modeliai](../../../docs)
- [Testavimo filosofija](../../../docs)
- [Kiti žingsniai](../../../docs)

Šiame vadove pateikiama informacija apie testus, kurie demonstruoja, kaip testuoti AI programas nereikalaujant API raktų ar išorinių paslaugų.

## Greitas pradėjimas

Visus testus paleiskite vienu komandos įrašu:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/lt/test-results.ea5c98d8f3642043.webp" alt="Sekmingi testų rezultatai" width="800"/>

*Sėkmingas testų vykdymas, kai visi testai praeina be klaidų*

## Ką apima testai

Šis kursas orientuojasi į **vienetinius testus**, kurie vykdomi vietoje. Kiekvienas testas demonstruoja specifinę LangChain4j koncepciją izoliuotai.

<img src="../../../translated_images/lt/testing-pyramid.2dd1079a0481e53e.webp" alt="Testavimo piramidė" width="800"/>

*Testavimo piramidė, rodanti balansą tarp vienetinių testų (greiti, izoliuoti), integracijos testų (tikri komponentai) ir galutinio testavimo. Šis mokymas apima vienetinius testus.*

| Modulis | Testai | Dėmesys | Pagrindiniai failai |
|---------|--------|---------|---------------------|
| **00 - Greitas pradėjimas** | 6 | Užklausų šablonai ir kintamųjų pakeitimas | `SimpleQuickStartTest.java` |
| **01 - Įvadas** | 8 | Pokalbių atmintis ir būsenos palaikymas | `SimpleConversationTest.java` |
| **02 - Užklausų inžinerija** | 12 | GPT-5.2 modeliai, entuziazmo lygiai, struktūrizuotas outputas | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Dokumentų apdorojimas, įterpiniai, panašumo paieška | `DocumentServiceTest.java` |
| **04 - Įrankiai** | 12 | Funkcijų kvietimas ir įrankių grandinė | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Modelio konteksto protokolas su Stdio transportu | `SimpleMcpTest.java` |

## Testų vykdymas

**Paleiskite visus testus iš root direktorijos:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Paleiskite testus konkrečiam moduliui:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Arba iš šaknies
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Arba iš root
mvn --% test -pl 01-introduction
```

**Paleiskite vieną testų klasę:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Paleiskite konkretų testų metodą:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#turėtųIšlaikytiPokALBĮIstoriją
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#turėtųIšlaikytiPokalbįIstoriją
```

## Testų vykdymas VS Code

Jei naudojate Visual Studio Code, Testų Explorer suteikia grafinę sąsają testų vykdymui ir derinimui.

<img src="../../../translated_images/lt/vscode-testing.f02dd5917289dced.webp" alt="VS Code testų naršyklė" width="800"/>

*VS Code Testų explorer rodantis testų medį su visomis Java testų klasėmis ir atskirais testų metodais*

**Kaip paleisti testus VS Code:**

1. Atidarykite Testų Explorer spustelėdami apie indelio piktogramą veiklos juostoje
2. Išskleiskite testų medį, kad pamatytumėte visus modulius ir testų klases
3. Spustelėkite „play“ mygtuką prie bet kurio testo, norėdami jį paleisti atskirai
4. Paspauskite „Run All Tests“ palaikyti visą testų rinkinį
5. Paspauskite dešinį pelės klavišą ant testo ir pasirinkite „Debug Test“, kad nustatytumėte pertraukos taškus ir žingsniuotumėte per kodą

Testų explorer parodo žalius varnelės ženklus praeinantiems testams ir pateikia išsamius klaidų pranešimus, kai testai nepraeina.

## Testavimo modeliai

### Modelis 1: Užklausų šablonų testavimas

Paprastas modelis testuoja užklausų šablonus neatliekant jokio AI modelio kvietimo. Patikrinama, kad kintamųjų pakeitimas veiktų teisingai ir užklausos būtų tinkamai suformatuotos.

<img src="../../../translated_images/lt/prompt-template-testing.b902758ddccc8dee.webp" alt="Užklausų šablonų testavimas" width="800"/>

*Užklausų šablonų testavimas, rodantis kintamųjų pakeitimo srautą: šablonas su vietos ženkliais → pritaikytos reikšmės → patvirtintas suformatuotas outputas*

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

Šis testas yra faile `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Paleiskite jį:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testuotiUžklausosŠablonoFormatavimą
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testoUžklausosŠablonoFormatavimas
```

### Modelis 2: Kalbos modelių imitavimas su Mockito

Testuojant pokalbio logiką, naudokite Mockito, kad sukurtumėte dirbtinius modelius, grąžinančius iš anksto nustatytus atsakymus. Tai leidžia testams būti greitiems, nemokamiems ir deterministiniams.

<img src="../../../translated_images/lt/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Imitacinio ir tikro API palyginimas" width="800"/>

*Palyginimas, kodėl imitacijos naudojamos testavimui: jos yra greitos, nemokamos, deterministinės ir nereikalauja API raktų*

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
        assertThat(history).hasSize(6); // 3 naudotojo + 3 DI žinutės
    }
}
```

Šis modelis naudojamas faile `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Imitacija užtikrina nuoseklią elgseną, kad būtų galima patikrinti teisingą atminties valdymą.

### Modelis 3: Pokalbio izoliacijos testavimas

Pokalbio atmintis turi atskirti kelis vartotojus. Šis testas patikrina, ar pokalbiai nesimaišo tarpusavyje.

<img src="../../../translated_images/lt/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Pokalbio izoliacija" width="800"/>

*Pokalbio izoliacijos testavimas, rodantis atskiras atminties saugyklas skirtingiems vartotojams, kad būtų išvengta konteksto maišymo*

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

Kiekvienas pokalbis palaiko nepriklausomą istoriją. Produkcijos sistemose ši izoliacija yra kritiškai svarbi daugiafunkcėms programoms.

### Modelis 4: Įrankių testavimas atskirai

Įrankiai yra funkcijos, kurias AI gali iškviesti. Testuokite juos tiesiogiai, kad įsitikintumėte, jog jie veikia teisingai, nepriklausomai nuo AI sprendimų.

<img src="../../../translated_images/lt/tools-testing.3e1706817b0b3924.webp" alt="Įrankių testavimas" width="800"/>

*Įrankių testavimas be AI kvietimų, naudojant imitacijas, kad būtų patikrinta verslo logika*

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

Šie testai iš `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` patikrina įrankių logiką be AI dalyvavimo. Grandinės pavyzdys rodo, kaip vieno įrankio outputas tiekiamas kaip įvestis kitam.

### Modelis 5: Atmintyje saugomas RAG testavimas

RAG sistemos dažniausiai reikalauja vektorinės duomenų bazės ir įterpimo paslaugų. Atmintyje saugomas modelis leidžia testuoti visą procesą be išorinių priklausomybių.

<img src="../../../translated_images/lt/rag-testing.ee7541b1e23934b1.webp" alt="Atmintyje saugas RAG testavimas" width="800"/>

*Atmintyje saugomo RAG testavimo darbo eiga demonstravimas: dokumentų analizė, įterpinių saugykla ir panašumo paieška be duomenų bazės*

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

Šis testas iš `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` sukuria dokumentą atmintyje ir tikrina jo skilimą į dalis bei metaduomenų apdorojimą.

### Modelis 6: MCP integracijos testavimas

MCP modulis testuoja Modelio konteksto protokolo integraciją naudojant stdio transportą. Šie testai patikrina, ar jūsų programa gali paleisti ir komunikuoti su MCP serveriais kaip subprocessais.

Testai faile `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` tikrina MCP kliento elgseną.

**Paleiskite juos:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Testavimo filosofija

Testuokite savo kodą, o ne AI. Testai turi tikrinti jūsų rašomą kodą patikrindami, kaip konstruojami užklausų šablonai, kaip valdoma atmintis ir kaip vykdomi įrankiai. AI atsakymai kinta ir neturėtų būti naudojami testų patvirtinimams. Klausykite savęs, ar jūsų užklausų šablonas teisingai pakeičia kintamuosius, o ne ar AI pateikia teisingą atsakymą.

Naudokite imitacijas kalbos modeliams. Tai išorinės priklausomybės, kurios yra lėtos, brangios ir nedeterministinės. Imitavimas leidžia testams būti greitiems su milisekundžių uždaviniu, nemokamiems be jokių API kaštų ir deterministiniams su vienodu rezultatu kiekvieną kartą.

Išlaikykite testus nepriklausomus. Kiekvienas testas turi susikurti savo duomenis, nesiremti kitų testų rezultatais ir atlikti valymą po savęs. Testai turi praeiti nepriklausomai nuo vykdymo tvarkos.

Testuokite ir ribines situacijas, ne tik sėkmingus scenarijus. Išbandykite tuščius įvestis, labai dideles įvestis, specialius simbolius, neteisingus parametrus ir ribines sąlygas. Tai dažnai atskleidžia klaidas, kurios įprasto naudojimo metu nepasirodo.

Naudokite aprašomuosius pavadinimus. Palyginkite `shouldMaintainConversationHistoryAcrossMultipleMessages()` su `test1()`. Pirmasis tiksliai nurodo, kas testuojama, todėl gedimų derinimas tampa daug paprastesnis.

## Kiti žingsniai

Dabar, kai suprantate testavimo modelius, gilinkitės į kiekvieną modulį:

- **[00 - Greitas pradėjimas](../00-quick-start/README.md)** - Pradėkite nuo užklausų šablonų pagrindų
- **[01 - Įvadas](../01-introduction/README.md)** - Sužinokite pokalbio atminties valdymą
- **[02 - Užklausų inžinerija](../02/prompt-engineering/README.md)** - Įvaldykite GPT-5.2 šablonų modelius
- **[03 - RAG](../03-rag/README.md)** - Kurkite duomenų paiešką papildančias generavimo sistemas
- **[04 - Įrankiai](../04-tools/README.md)** - Įgyvendinkite funkcijų kvietimą ir įrankių grandines
- **[05 - MCP](../05-mcp/README.md)** - Integruokite Modelio konteksto protokolą

Kiekvieno modulio README pateikia detalius čia testuotų koncepcijų paaiškinimus.

---

**Navigacija:** [← Atgal į Pagrindinį](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:  
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors stengiamės užtikrinti tikslumą, atkreipkite dėmesį, kad automatiniai vertimai gali turėti klaidų arba netikslumų. Originalus dokumentas jo gimtąja kalba laikomas autoritetingu šaltiniu. Kritinei informacijai rekomenduojama naudoti profesionalų vertimą, atliktą žmogaus. Mes neatsakome už jokius nesusipratimus ar neteisingą interpretaciją, kylančią dėl šio vertimo naudojimo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->