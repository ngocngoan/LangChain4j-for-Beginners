# LangChain4j programų testavimas

## Turinys

- [Greitas pradėjimas](../../../docs)
- [Ką apima testai](../../../docs)
- [Testų vykdymas](../../../docs)
- [Testų vykdymas VS Code](../../../docs)
- [Testavimo modeliai](../../../docs)
- [Testavimo filosofija](../../../docs)
- [Kiti žingsniai](../../../docs)

Ši gidas jus supažindina su testais, kurie parodo, kaip testuoti DI programas nereikalaujant API raktų ar išorinių paslaugų.

## Greitas pradėjimas

Paleiskite visus testus su vienu komandos įvedimu:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

Kai visi testai sėkmingai praeina, turėtumėte matyti panašų rezultatą kaip žemiau esančioje ekrano nuotraukoje — testai vykdomi be klaidų.

<img src="../../../translated_images/lt/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Sėkmingas testų vykdymas, rodantis, kad visi testai praeina be klaidų*

## Ką apima testai

Šis kursas orientuotas į **vienetinius testus**, kurie vykdomi lokaliai. Kiekvienas testas demonstruoja konkretų LangChain4j konceptą izoliacijoje. Toliau pateikta testavimo piramidė rodo, kur tinka vienetiniai testai — jie sudaro greitą, patikimą pagrindą, ant kurio statoma visa kita testavimo strategija.

<img src="../../../translated_images/lt/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Testavimo piramidė rodanti balansą tarp vienetinių testų (greiti, izoliuoti), integracinių testų (tikri komponentai) ir pilno veikimo testų. Ši apmokyma apima vienetinius testus.*

| Modulis | Testai | Fokusas | Svarbūs failai |
|--------|-------|-------|-----------|
| **00 - Greitas pradėjimas** | 6 | Pavyzdžių šablonai ir kintamųjų pakeitimas | `SimpleQuickStartTest.java` |
| **01 - Įvadas** | 8 | Pokalbių atmintis ir būsenoje saugomi chat'ai | `SimpleConversationTest.java` |
| **02 - Pavyzdžių inžinerija** | 12 | GPT-5.2 modelių pavyzdžiai, entuziazmo lygiai, struktūruotas išvestis | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Dokumentų įvedimas, įterpimai, panašumo paieška | `DocumentServiceTest.java` |
| **04 - Įrankiai** | 12 | Funkcijų iškvietimas ir įrankių jungimas | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Modelio konteksto protokolas naudojant stdio | `SimpleMcpTest.java` |

## Testų vykdymas

**Paleisti visus testus iš pagrindinio katalogo:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Paleisti testus konkrečiam moduliui:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Arba iš root
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Arba iš šaknies
mvn --% test -pl 01-introduction
```

**Paleisti vieną testų klasę:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Paleisti konkretų testų metodą:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#turėtųIšlaikytiPokALBIOIstoriją
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#turėtųIšlaikytiPokalbioIstoriją
```

## Testų vykdymas VS Code

Jei naudojate Visual Studio Code, Test Explorer suteikia grafinę sąsają testų vykdymui ir derinimui.

<img src="../../../translated_images/lt/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code testų naršyklė rodanti testų medį su visomis Java testų klasėmis ir atskirais testų metodais*

**Norėdami paleisti testus VS Code:**

1. Atidarykite Test Explorer paspausdami mėgintuvėlio ikoną Veiklos juostoje
2. Išskleiskite testų medį, kad pamatytumėte visus modulius ir testų klases
3. Spustelėkite paleidimo mygtuką prie bet kurio testo, kad paleistumėte jį atskirai
4. Spustelėkite "Run All Tests", kad paleistumėte visą testų rinkinį
5. Dešiniuoju pelės mygtuku spustelėkite bet kurį testą ir pasirinkite "Debug Test", kad nustatytumėte pertraukos taškus ir žingsniuotumėte per kodą

Testų naršyklė rodo žalius varnele pažymėtus sėkmingus testus ir pateikia išsamius klaidų pranešimus, kai testai nepraeina.

## Testavimo modeliai

### Modelis 1: Pavyzdžių šablonų testavimas

Paprastas modelis testuoja pavyzdžių šablonus neiškviečiant jokio DI modelio. Patikrinama, ar kintamųjų pakeitimas veikia teisingai ir ar pavyzdžiai suformuoti kaip tikimasi.

<img src="../../../translated_images/lt/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Pavyzdžių šablonų testavimas rodantis kintamųjų pakeitimo kelią: šablonas su vietos žymekliais → pritaikomi reikšmės → patvirtinama suformatuota išvestis*

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
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testuotiUžklausoŠablonoFormatavimą
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testuotiKlausimoŠablonoFormatavimą
```

### Modelis 2: Kalbos modelių maketavimas

Testuojant pokalbių logiką, naudokite Mockito, kad sukurtumėte netikrus modelius, kurie grąžina iš anksto nustatytus atsakymus. Tai daro testus greitus, nemokamus ir deterministinius.

<img src="../../../translated_images/lt/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Palyginimas rodantis, kodėl maketai yra pageidautini testavimui: jie yra greiti, nemokami, deterministiniai ir nereikalauja API raktų*

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
        assertThat(history).hasSize(6); // 3 vartotojo + 3 DI žinutės
    }
}
```

Šis modelis panaudotas faile `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Maketas užtikrina nuoseklią elgseną, todėl galima patikrinti teisingą atminties valdymą.

### Modelis 3: Pokalbių izoliacijos testavimas

Pokalbių atmintis turi išlaikyti atskirus daugelio vartotojų kontekstus. Šis testas patikrina, ar pokalbiai nesimaišo.

<img src="../../../translated_images/lt/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Pokalbių izoliacijos testavimas rodo atskirus atminties saugyklas skirtingiems vartotojams, kad būtų užkirstas kelias kontekstui maišytis*

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

Kiekvienas pokalbis palaiko savo nepriklausomą istoriją. Gamybinėse sistemose ši izoliacija yra kritiškai svarbi daugiafunkcinei naudotojų aplikacijai.

### Modelis 4: Įrankių testavimas atskirai

Įrankiai yra funkcijos, kurias DI gali iškviesti. Testuokite juos tiesiogiai, kad įsitikintumėte, jog jie veikia teisingai nepriklausomai nuo DI sprendimų.

<img src="../../../translated_images/lt/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Įrankių testavimas atskirai, rodantis maketuojamą įrankio vykdymą be DI kvietimų, patvirtina verslo logiką*

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

Šie testai iš `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` tikrina įrankių logiką be DI įtraukimo. Grandinėlės pavyzdys rodo, kaip vieno įrankio išvestis įeina į kito įrankio įvestį.

### Modelis 5: RAG testavimas atmintyje

RAG sistemos tradiciškai reikalauja vektorių duomenų bazių ir įterpimų paslaugų. Modelis „atmintyje“ leidžia testuoti visą srautą be išorinių priklausomybių.

<img src="../../../translated_images/lt/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*RAG testavimo „atmintyje“ srautas, rodantis dokumentų analizę, įterpimų saugojimą ir panašumo paiešką be duomenų bazės*

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

Šis testas faile `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` sukuria dokumentą atmintyje ir patikrina jo dalijimąsi bei metaduomenų valdymą.

### Modelis 6: MCP integracinis testavimas

MCP modulis tikrina Modelio konteksto protokolo integraciją naudojant stdio transportą. Šie testai patvirtina, kad jūsų programa gali sukurti ir bendrauti su MCP serveriais kaip pakomos procesais.

Testai faile `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` vertina MCP kliento elgseną.

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

Testuokite savo kodą, ne DI. Jūsų testai turėtų tikrinti jūsų parašytą kodą, kaip konstruojami pavyzdžiai, kaip valdoma atmintis ir kaip vykdomi įrankiai. DI atsakymai kinta ir neturėtų būti testų patvirtinimų dalis. Klausykite savęs, ar jūsų pavyzdžio šablonas tinkamai pakeičia kintamuosius, o ne ar DI pateikia teisingą atsakymą.

Naudokite maketus kalbos modeliams. Tai išoriniai komponentai, kurie yra lėti, brangūs ir nedeterministiniai. Maketai leidžia testams būti greitiems (milisekundėmis vietoje sekundžių), nemokamiems (be API kaštų) ir deterministiniams (tą patį rezultatą kiekvieną kartą).

Išlaikykite testus nepriklausomais. Kiekvienas testas turi paruošti savo duomenis, nereikėti kitų testų ir po savęs išvalyti. Testai turi praeiti nepriklausomai nuo vykdymo eiliškumo.

Testuokite kraštutinius atvejus, o ne tik malonius srautus. Bandykite tuščius įvedimus, labai didelius įvedimus, specialiuosius simbolius, neteisingus parametrus ir ribines sąlygas. Dažnai tai išryškina klaidas, kurių įprastas naudojimas nerodo.

Naudokite aprašomuosius pavadinimus. Palyginkite `shouldMaintainConversationHistoryAcrossMultipleMessages()` su `test1()`. Pirmasis tiksliai nurodo, kas testuojama, kas palengvina gedimų derinimą.

## Kiti žingsniai

Dabar kai suprantate testavimo modelius, gilinkitės į kiekvieną modulį:

- **[00 - Greitas pradėjimas](../00-quick-start/README.md)** - Pradėkite nuo pavyzdžių šablonų pagrindų
- **[01 - Įvadas](../01-introduction/README.md)** - Sužinokite pokalbių atminties valdymą
- **[02 - Pavyzdžių inžinerija](../02/prompt-engineering/README.md)** - Įvaldykite GPT-5.2 pavyzdžių modelius
- **[03 - RAG](../03-rag/README.md)** - Kurkite paieškos ir generavimo sistemas
- **[04 - Įrankiai](../04-tools/README.md)** - Įgyvendinkite funkcijų iškvietimus ir įrankių grandines
- **[05 - MCP](../05-mcp/README.md)** - Integruokite Modelio konteksto protokolą

Kiekvieno modulio README pateikia detalius šio mokymo metu testuojamų konceptų paaiškinimus.

---

**Navigacija:** [← Atgal į pagrindinį](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:  
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, prašome atkreipti dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas jo gimtąja kalba turėtų būti laikomas pagrindiniu šaltiniu. Svarbiai informacijai rekomenduojama naudoti profesionalų žmogišką vertimą. Mes neatsakome už jokių nesusipratimų ar neteisingų interpretacijų, kylančių naudojant šį vertimą.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->