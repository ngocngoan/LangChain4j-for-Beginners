# LangChain4j rakenduste testimine

## Sisukord

- [Kiire algus](../../../docs)
- [Mida testid katavad](../../../docs)
- [Testide käivitamine](../../../docs)
- [Testide käivitamine VS Code'is](../../../docs)
- [Testimise mustrid](../../../docs)
- [Testimise filosoofia](../../../docs)
- [Järgmised sammud](../../../docs)

See juhend tutvustab teste, mis näitavad, kuidas testida AI rakendusi ilma API võtmete või väliste teenusteta.

## Kiire algus

Käivita kõik testid ühe käsuga:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/et/test-results.ea5c98d8f3642043.webp" alt="Edukad testitulemused" width="800"/>

*Eduka testi käivitamine, kus kõik testid on läbitud, puuduvad vead*

## Mida testid katavad

See kursus keskendub **üksustestidele**, mis jooksevad kohalikult. Iga test demonstreerib kindlat LangChain4j kontseptsiooni isoleeritult.

<img src="../../../translated_images/et/testing-pyramid.2dd1079a0481e53e.webp" alt="Testimise püramiid" width="800"/>

*Testimise püramiid, mis näitab tasakaalu üksustestide (kiired, isoleeritud), integratsioonitestide (tõelised komponendid) ja lõpp-lõpuni testide vahel. See koolitus käsitleb üksustestimist.*

| Moodul | Testid | Fookus | Olulisemad failid |
|--------|--------|--------|-------------------|
| **00 - Kiire algus** | 6 | Prompti mallid ja muutujate asendamine | `SimpleQuickStartTest.java` |
| **01 - Sissejuhatus** | 8 | Vestluse mälu ja järjepidev vestlus | `SimpleConversationTest.java` |
| **02 - Promptide inseneritöö** | 12 | GPT-5.2 mustrid, innukuse tasemed, struktureeritud väljund | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Dokumendi sisestamine, manused, sarnasuse otsing | `DocumentServiceTest.java` |
| **04 - Tööriistad** | 12 | Funktsioonikutsed ja tööriistade ühendamine | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Mudeli konteksti protokoll koos Stdio transpordiga | `SimpleMcpTest.java` |

## Testide käivitamine

**Käivita kõik testid juurkataloogist:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Käivita testid konkreetsele moodulile:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Või juurest
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Või juurest
mvn --% test -pl 01-introduction
```

**Käivita üks testiklass:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Käivita konkreetne testmeetod:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#tuleks säilitada vestluse ajalugu
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#peaks säilitama vestluse ajaloo
```

## Testide käivitamine VS Code'is

Kui kasutad Visual Studio Code'i, siis Test Explorer pakub graafilist kasutajaliidest testide käivitamiseks ja silumiseks.

<img src="../../../translated_images/et/vscode-testing.f02dd5917289dced.webp" alt="VS Code testieksplorer" width="800"/>

*VS Code testieksplorer kuvab testipuu koos kõigi Java testiklasside ja individuaalsete testmeetoditega*

**Testide käivitamiseks VS Code'is:**

1. Ava Test Explorer, klõpsates tegevusribal klaasipudeli ikooni
2. Laienda testipuud, et näha kõiki mooduleid ja testiklasse
3. Klõpsa suvalise testi juures mängunupule, et test käivitada eraldi
4. Klõpsa "Run All Tests", et käivitada kogu komplekt
5. Paremklõpsa testi peal ja vali "Debug Test", et seada katkestuspunkte ning samm-sammult koodi siluda

Testieksplorer näitab rohelist linnukest edukate testide puhul ning annab veateated täpse info kohta, kui test ebaõnnestub.

## Testimise mustrid

### Muster 1: Prompti mallide testimine

Kõige lihtsam muster testib prompti malle ilma AI mudelit kutsumata. Sa kontrollid, et muutujate asendamine toimiks õigesti ja promptid on soovitud formaadis.

<img src="../../../translated_images/et/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompti malli testimine" width="800"/>

*Prompti mallide testimine, mis näitab muutujate asendamise protsessi: mall koos kohatäitjatega → väärtused rakendatud → vormindatud väljund kontrollitud*

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

See test asub failis `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Käivita:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testimisePromptMallideVormindamine
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testPromptMallideFormaatimine
```

### Muster 2: Keelemudelite imiteerimine (mockimine)

Vestlusloogika testimisel kasuta Mockito't, et luua võltsmudelid, mis tagastavad etteantud vastuseid. See muudab testid kiired, tasuta ja deterministlikud.

<img src="../../../translated_images/et/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Võltsitud vs tõeline API võrdlus" width="800"/>

*Võrdlus, mis näitab, miks testimiseks kasutatakse eelistatult moke: need on kiired, tasuta, deterministlikud ja ei vaja API võtmeid*

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
        assertThat(history).hasSize(6); // 3 kasutaja + 3 tehisintellekti sõnumit
    }
}
```

See muster ilmub failis `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mock tagab järjepideva käitumise, nii et saad kontrollida mälu haldust tõhusalt.

### Muster 3: Vestluse isolatsiooni testimine

Vestluse mälu peab hoidma mitme kasutaja andmed eraldi. See test kontrollib, et vestlused ei sega omavahel konteksti.

<img src="../../../translated_images/et/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Vestluse isolatsioon" width="800"/>

*Vestluse isolatsiooni testimine, mis näitab erinevate kasutajate eraldatud mälu hoidlaid, et vältida konteksti segunemist*

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

Iga vestlus hoiab iseseisvat ajalugu. Tootmissüsteemides on see isolatsioon ülioluline mitme kasutajaga rakendustes.

### Muster 4: Tööriistade testimine iseseisvalt

Tööriistad on funktsioonid, mida AI võib kutsuda. Testi neid otse, et veenduda, et need töötavad kõigis olukordades, sõltumata AI otsustest.

<img src="../../../translated_images/et/tools-testing.3e1706817b0b3924.webp" alt="Tööriistade testimine" width="800"/>

*Tööriistade iseseisev testimine näitab võlts tööriistade kasutamist ilma AI-interaktsioonita, et kontrollida äriloogikat*

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

Need testid failist `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` valideerivad tööriistade loogikat ilma AI kaasamiseta. Kettimise näide toob välja, kuidas ühe tööriista väljund söödetakse teise sisendiks.

### Muster 5: Mälu-põhine RAG testimine

RAG süsteemid vajavad tavaliselt vektorandmebaase ja manustusteenuseid. Mälu-põhine muster võimaldab testida kogu töövoogu ilma väliste sõltuvusteta.

<img src="../../../translated_images/et/rag-testing.ee7541b1e23934b1.webp" alt="Mälu-põhine RAG testimine" width="800"/>

*Mälu-põhine RAG testimise voog näitab dokumendi analüüsi, manuste salvestamist ja sarnasuse otsingut ilma andmebaasi vajaduseta*

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

See test failist `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` loob dokumendi mällu ning kontrollib selle lõikamist ja metaandmete haldust.

### Muster 6: MCP integratsioonitesti tegemine

MCP moodul testib Mudeli Konteksti Protokolli integreerimist stdio transpordiga. Need testid kontrollivad, et sinu rakendus saab käivitada ning suhelda MCP serveritega alamsidemuna.

Testid failis `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` valideerivad MCP kliendi käitumist.

**Neid käivitada:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Testimise filosoofia

Testeeri oma koodi, mitte AI-d. Sinu testid peaksid valideerima kirjutatud koodi, kontrollides, kuidas promptid konstrueeritakse, kuidas mälu hallatakse ja kuidas tööriistad töötavad. AI vastused varieeruvad ja neid ei tohiks osa testide kinnitusest teha. Küsi endalt, kas su prompti mall asendab muutujad õigesti, mitte kas AI annab õige vastuse.

Kasuta keelemudelite puhul moke. Need on välised sõltuvused, mis on aeglased, kallid ja mittedeterministlikud. Mockimine teeb testid kiired (millisekundites, mitte sekundites), tasuta (ilma API kuludeta) ja deterministlikud (iga kord sama tulemus).

Hoidke testid sõltumatud. Iga test peaks üles seadma oma andmed, mitte loota teistele testidele, ning puhastama enda järel. Testid peaksid lõppema edukalt sõltumata käivitamise järjekorrast.

Testeeri servajuhtumeid tavapärasest kasutusest erinevalt. Proovi tühje sisendeid, väga suuri sisendeid, erimärke, kehtetuid parameetreid ja piirilisi tingimusi. Need paljastavad tihti vead, mida tavaline kasutus ei avalda.

Kasuta kirjeldavaid nimesid. Võrdle `shouldMaintainConversationHistoryAcrossMultipleMessages()` ja `test1()`. Esimene ütleb täpselt, mida testitakse, muutes tõrgete silumise palju lihtsamaks.

## Järgmised sammud

Nüüd, kui tunned testimise mustreid, süvenda iga moodulit:

- **[00 - Kiire algus](../00-quick-start/README.md)** - Alusta prompti mallide põhialustest
- **[01 - Sissejuhatus](../01-introduction/README.md)** - Õpi vestluse mäluhalduse kohta
- **[02 - Promptide inseneritöö](../02/prompt-engineering/README.md)** - Saa valdajaks GPT-5.2 prompting mustrites
- **[03 - RAG](../03-rag/README.md)** - Ehita taasesituspõhiseid süsteeme
- **[04 - Tööriistad](../04-tools/README.md)** - Rakenda funktsioonikutsed ja tööriistade kettimine
- **[05 - MCP](../05-mcp/README.md)** - Integreeri Mudeli Konteksti Protokoll

Iga mooduli README jagab siin ülevaadetest põhjalikumaid seletusi.

---

**Navigatsioon:** [← Tagasi pealehele](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Lahtiütlus**:
See dokument on tõlgitud kasutades tehisintellekti tõlketeenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi püüame tagada täpsust, palun arvestage, et automaatsed tõlked võivad sisaldada vigu või ebatäpsusi. Originaaldokument selle emakeeles tuleks pidada ametlikuks allikaks. Olulise teabe puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta selle tõlke kasutamisest tingitud arusaamatuste või väärkohtlemiste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->