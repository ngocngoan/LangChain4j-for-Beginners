# LangChain4j అనువర్తనాలను పరీక్షించడం

## సూచిక

- [త్వరిత ప్రారంభం](../../../docs)
- [పరీక్షలు ఏం కవర్ చేస్తాయో](../../../docs)
- [పరీక్షలు ఎలా నడపాలి](../../../docs)
- [VS కోడ్‌లో పరీక్షలు నడపడం](../../../docs)
- [పరీక్షా నమూనాలు](../../../docs)
- [పరీక్షా తత్వశాస్త్రం](../../../docs)
- [తరువాతి దశలు](../../../docs)

ఈ గైడ్ API కీలు లేదా బాహ్య సర్వీసులు అవసరం లేకుండానే AI అనువర్తనాలను ఎలా పరీక్షించాలో చూపించే పరీక్షల ద్వారా మీకు మార్గనిర్దేశనం చేస్తుంది.

## త్వరిత ప్రారంభం

ఒకే కమాండ్‌తో అన్ని పరీక్షలను నడపండి:

**బాష్:**
```bash
mvn test
```

**పవర్‌షెల్:**
```powershell
mvn --% test
```

అన్ని పరీక్షలు విజయవంతంగా పూర్తయ్యినప్పుడు, క్రింది స్క్రీన్‌షాట్ లాంటి అవుట్‌పుట్ కనిపిస్తుంది — పరీక్షలు ఎటువంటి వైఫల్యాలు లేకుండానే నడుస్తాయి.

<img src="../../../translated_images/te/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*విజయవంతమైన పరీక్ష నిర్వహణలో అన్ని పరీక్షలు వైఫల్యాలు లేకుండా విజయవంతంగా పూర్తి అవుతున్నాయి*

## పరీక్షలు ఏం కవర్ చేస్తాయో

ఈ కోర్సు స్థానికంగా నడిచే **యూనిట్ పరీక్షల** పై దృష్టి పెట్టింది. ప్రతి పరీక్ష ఒక నిర్దిష్ట LangChain4j భావనను వేరుగా చూపిస్తుంది. క్రింది పరీక్ష హిరార్కీ యూనిట్ పరీక్షలు ఎక్కడ फिटవుతున్నాయో తెలియజేస్తుంది — అవి త్వరితమైన, విశ్వసనీయమైన పునాది వాటి మీద మీ ఇతర పరీక్షా చర్యలు బిల్డ్ అవుతాయి.

<img src="../../../translated_images/te/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*యూనిట్ పరీక్షలు (త్వరితమైన, వేరుచేస్తున్న), ఇంటిగ్రేషన్ పరీక్షలు (నిజమైన కొంపోనెంట్లు), ఎండ్-టు-ఎండ్ పరీక్షల మద్య సమతుల్యాన్ని చూపే పరీక్షా పిరమిడ్. ఈ శిక్షణ యూనిట్ పరీక్షలను కవర్ చేస్తుంది.*

| మాడ్యూల్ | పరీక్షలు | ఫోకస్ | ముఖ్య ఫైళ్ళు |
|--------|-------|-------|-----------|
| **00 - త్వరిత ప్రారంభం** | 6 | ప్రాంప్ట్ టెంప్లేట్లు మరియు వేరియబుల్ ప్రత్యామ్నాయం | `SimpleQuickStartTest.java` |
| **01 - పరిచయం** | 8 | సంభాషణ జ్ఞాపకం మరియు స్థితిస్థాపక చాట్ | `SimpleConversationTest.java` |
| **02 - ప్రాంప్ట్ ఇంజనీరింగ్** | 12 | GPT-5.2 నమూనాలు, ఉత్సాహ స్థాయిలు, నిర్మాణాత్మక అవుట్‌పుట్ | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | డాక్యుమెంట్ ఇంజెస్ట్, ఎంబెడింగ్స్, సమానత్వ శోధన | `DocumentServiceTest.java` |
| **04 - టూల్స్** | 12 | ఫంక్షన్ కాలింగ్ మరియు టూల్ చైనింగ్ | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | మోడల్ కాంటెక్స్ట్ ప్రోటోకాల్ స్త్డీయో ట్రాన్స్పోర్ట్‌తో | `SimpleMcpTest.java` |

## పరీక్షలు ఎలా నడపాలి

**రూట్ నుండి అన్ని పరీక్షలను నడపండి:**

**బాష్:**
```bash
mvn test
```

**పవర్‌షెల్:**
```powershell
mvn --% test
```

**ఒక నిర్దిష్ట మాడ్యూల్ కోసం పరీక్షలు నడపండి:**

**బాష్:**
```bash
cd 01-introduction && mvn test
# లేదా రూట్ నుండి
mvn test -pl 01-introduction
```

**పవర్‌షెల్:**
```powershell
cd 01-introduction; mvn --% test
# లేదా రూట్ నుండి
mvn --% test -pl 01-introduction
```

**ఒకటే పరీక్ష తరగతిని నడపండి:**

**బాష్:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**పవర్‌షెల్:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**ఒక నిర్దిష్ట పరీక్ష పద్ధతిని నడపండి:**

**బాష్:**
```bash
mvn test -Dtest=SimpleConversationTest#సంభాషణ చరిత్రను నిర్వహించాలి
```

**పవర్‌షెల్:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#సంభాషణ చరిత్రను నిర్వహించాలి
```

## VS కోడ్‌లో పరీక్షలు నడపడం

మీరు Visual Studio Code వాడుతున్నట్లయితే, Test Explorer పరీక్షలను నడపడానికి మరియు డీబగ్ చేసేందుకు గ్రాఫికల్ ఇంటర్‌ఫేస్ అందిస్తుంది.

<img src="../../../translated_images/te/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS కోడ్ టెస్ట్ ఎక్స్ప్లోరర్ అన్ని జావా పరీక్ష తరగతులు మరియు వ్యక్తిగత పరీక్ష పద్ధతులతో కూడిన పరీక్ష డెండర్ చూపిస్తోంది*

**VS కోడ్‌లో పరీక్షలు నడపడానికి:**

1. Activity Barలోని బీకర్ ఐకాన్‌పై క్లిక్ చేసి Test Explorer తెరవండి
2. అన్ని మాడ్యూల్స్ మరియు పరీక్ష తరగతులు చూసేందుకు పరీక్ష చెట్లను విస్తరించండి
3. ఏదైనా పరీక్ష పక్కన ఉన్న ప్లే బటనును క్లిక్ చేసి ఆ పరీక్షను వేరుగా నడపండి
4. మొత్తం సూట్ నడప "Run All Tests" క్లిక్ చేయండి
5. ఏదైనా పరీక్ష‌పై రైట్-క్లిక్ చేసి "Debug Test" ఎంచుకొని బ్రేక్ఫ్ పాయింట్లు పెడుతూ కోడ్ లో దశలవారీగా వెళ్లండి

పరీక్షలు విజయవంతం అయితే గ్రీన్ చెక్మార్క్ కనిపిస్తుంది మరియు విఫలాలైనా వివరమైన సందేశాలు చూపిస్తుంది.

## పరీక్షా నమూనాలు

### నమూనా 1: ప్రాంప్ట్ టెంప్లేట్లను పరీక్షించడం

అతి సరళమైన నమూనా AI మోడల్‌ను కాల్ చేయకుండా ప్రాంప్ట్ టెంప్లేట్లను పరీక్షిస్తుంది. వేరియబుల్ ప్రత్యామ్నాయం సరిగ్గా పనిచేస్తుందో మరియు ప్రాంప్ట్‌లు ఆశించినట్లుగా ఫార్మాట్ అయ్యాయో సరిచూడవచ్చు.

<img src="../../../translated_images/te/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*వేరియబుల్ ప్రత్యామ్నాయం ప్రవాహం చూపిస్తూ ప్రాంప్ట్ టెంప్లేట్లను పరీక్షించడం: ప్లేస్‌హోల్డర్లతో టెంప్లేట్ → విలువలు వర్తింపజేయబడటం → ఫార్మాటెడ్ అవుట్‌పుట్ నిర్ధారణ*

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

ఈ పరీక్ష `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java` లో ఉంది.

**దాన్ని నడపండి:**

**బాష్:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#టెస్ట్ ప్రాంప్ట్ టెంప్లేట్ ఫార్మాటింగ్
```

**పవర్‌షెల్:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#పరీక్షప్రాంప్ట్ టెంప్లేట్ ఫార్మాటింగ్
```

### నమూనా 2: భాషా మోడల్స్‌ను మాక్ చేయడం

సంభాషణ తర్కాన్ని పరీక్షించేటప్పుడు Mockito ఉపయోగించి ముందుగా నిర్ణయించిన ప్రత్యుత్తరాలను అందించే నకిలీ మోడల్స్ సృష్టించండి. ఇది పరీక్షలను తక్షణ, ఉచిత మరియు నిర్ణీతం చేస్తుంది.

<img src="../../../translated_images/te/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*మాక్‌లు ఎందుకు వాస్తవ API కంటే సూచించే టెస్టింగ్ కష్టత తక్కువవో చూపే పోలిక: అవి త్వరితమైనవి, ఉచితమైనవి, నిర్ణీతమైనవి మరియు API కీలు అవసరం కాదు*

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
        assertThat(history).hasSize(6); // 3 వినియోగదారుడు + 3 AI సందేశాలు
    }
}
```

ఈ నమూనా `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` లో ఉంది. మాక్ స్థిరమైన ప్రవర్తనను నిర్ధారిస్తుంది కాబట్టి జ్ఞాపకం నిర్వహణ సరిగ్గా పనిచేస్తుందో మీరు ధృవీకరించవచ్చు.

### నమూనా 3: సంభాషణ వేరుచేయడం పరీక్షించడం

సంభాషణ జ్ఞాపకం పలు వినియోగదారులను వేరుగా ఉంచాలి. ఈ పరీక్ష సంభాషణలు కాన్టెక్స్ట్ మిశ్రమం చేయకుండా వేరుగా ఉన్నాయో ధృవీకరిస్తుంది.

<img src="../../../translated_images/te/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*పారిశ్రామిక జ్ఞాపక నిల్వలను వేరుగా ఉంచి కాన్టెక్స్ట్ మిక్సింగ్ నివారిస్తున్నట్లు చూపిస్తూ సంభాషణ వేరుచేత పలుకుల పరీక్ష*

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

ప్రతి సంభాషణకు స్వతంత్ర చరిత్ర ఉంటుంది. ఉత్పత్తి వ్యవస్థల్లో, ఈ వేరుచేత బహువినియోగదారు అనువర్తనాలకు అత్యంత అవసరం.

### నమూనా 4: టూల్స్‌ను స్వతంత్రంగా పరీక్షించడం

టూల్స్ అనేవి AI కాల్ చేయగల ఫంక్షన్లు. అవి AI నిర్ణయాలకు సలవుచూంతున్నా సరే సరిగ్గా పనిచేస్తున్నాయో ప్రత్యక్షంగా పరీక్షించండి.

<img src="../../../translated_images/te/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*AI కాల్స్ లేకుండా మాక్ టూల్ అమలు చూపిస్తూ వ్యాపార తర్కాన్ని ధృవీకరించే టూల్స్‌ను స్వతంత్రంగా పరీక్షించడం*

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

ఈ పరీక్షలు `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` నుంచి ఉన్నాయి. AI భాగస్వామ్యం లేకుండా టూల్ తర్కాన్ని ధృవీకరిస్తాయి. చైనింగ్ ఉదాహరణ ఒక టూల్ అవుట్‌పుట్ మరొకదాని ఇన్‌పుట్‌గా ఎలా పనిచేస్తుందో చూపిస్తుంది.

### నమూనా 5: ఇన్-మెమరీ RAG పరీక్ష

RAG వ్యవస్థలకు సాధారణంగా వెక్టార్ డేటాబేసులు మరియు ఎంబెడ్డింగ్ సర్వీసులు అవసరం. ఇన్-మెమరీ నమూనా మొత్తం పైప్లైన్ ను బాహ్య ఆధారిత సదుపాయాలు లేకుండానే పరీక్షించగలుగుతుంది.

<img src="../../../translated_images/te/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*డాక్యుమెంట్ పార్సింగ్, ఎంబెడ్డింగ్ నిల్వ, సమానత్వ శోధనను డేటాబేస్ అవసరం లేకుండా చూపించే ఇన్-మెమరీ RAG పరీక్షా తరలింపు*

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

ఈ పరీక్ష `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` నుండి ఉంది. ఇన్-మెమరీలో డాక్యుమెంట్ సృష్టించి చంకింగ్ మరియు మెటాడేటా నిర్వహణను ధృవీకరిస్తుంది.

### నమూనా 6: MCP ఇంటిగ్రేషన్ పరీక్ష

MCP మాడ్యూల్ stdio ట్రాన్స్పోర్ట్ ఉపయోగించి మోడల్ కాంటెక్స్ట్ ప్రోటోకాల్ ఇంటిగ్రేషన్‌ని పరీక్షిస్తుంది. ఈ పరీక్షలు మీ అనువర్తనం MCP సర్వర్లను ఉపప్రక్రియలుగా స్పాన్ చేసి కమ్యూనికేట్ చేయగలదని ధృవీకరిస్తాయి.

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` లోని పరీక్షలు MCP క్లీంట్ ప్రవర్తనను ధృవీకరిస్తాయి.

**దాన్ని నడిపించండి:**

**బాష్:**
```bash
cd 05-mcp && mvn test
```

**పవర్‌షెల్:**
```powershell
cd 05-mcp; mvn --% test
```

## పరీక్షా తత్వశాస్త్రం

AIని కాదు, మీ కోడ్‌ను పరీక్షించండి. మీ పరీక్షలు మీరు రాసే కోడ్ ఎలా పనిచేస్తుందో ధృవీకరించాలి: ప్రాంప్ట్‌లు ఎలా నిర్మించబడ్డాయో, జ్ఞాపకం ఎలా నిర్వహించబడుతున్నదో, టూల్స్ ఎలా అమలవుతున్నాయో చూసుకోండి. AI స్పందనలు మారవచ్చు మరియు పరీక్షా ధృవీకరణలో భాగం కాకూడదు. మీ ప్రాంప్ట్ టెంప్లేట్ సరిగ్గా వేరియబుల్స్ ప్రత్యామ్నాయం చేస్తున్నదా అనే విషయాన్ని చూసుకోండి, AI సరైన జవాబు ఇస్తుందా కాదా కాదు.

భాషా మోడల్స్ కోసం మాక్‌లను వాడండి. అవి బాహ్య ఆధారాలు, ఆలస్యంగా ఉంటాయి, ఖరీదైనవి, మరియు నిర్ణీతం కాలేవి. మాక్ వాడటం వలన పరీక్షలు సెకన్లతో కాక, మిల్లీసెకన్లలో వేగంగా, ఉచితంగా API ఖర్చులు లేకుండా, మరియు ప్రతిసారి ఒకటే ఫలితంతో ఉంటాయి.

పరీక్షలను స్వతంత్రంగా ఉంచండి. ప్రతి పరీక్ష తన డేటాను సెట్ చేసుకోవాలి, ఇతర పరీక్షలపై ఆధారపడి ఉండకూడదు, మరియు స్వయంగా శుభ్రపరచుకోవాలి. పరీక్షలు ఎటువంటి అమలులోనైనా విజయవంతం కావాలి.

సంతృప్తికర మార్గం కంటే ఎక్కువ ఎడ్జ్ కేసులను పరీక్షించండి. ఖాళీ ఇన్‌పుట్లు, చాలా పెద్ద ఇన్‌పుట్లు, ప్రత్యేక అక్షరాలు, చెల్లని పారామీటర్లు, మరియు సరిహద్దు పరిస్థితులు ప్రయత్నించండి. ఇవి సాధారణ ఉపయోగం బయట బగ్స్‌ను బయటపెడతాయి.

వర్ణనాత్మక పేర్లను వాడండి. `shouldMaintainConversationHistoryAcrossMultipleMessages()` తో `test1()`ని పోల్చండి. మొదటి పేరుతో మీరు ఏమి పరీక్షిస్తున్నారో ఖచ్చితంగా తెలుస్తుంది, డీబగ్గింగ్ బాధలను సులభతరం చేస్తుంది.

## తర్వాతి దశలు

ఇప్పుడు మీరు పరీక్షా నమూనాలు అర్థం చేసుకున్నందున, ప్రతి మాడ్యూల్ లో మరింత లోతుగా ప్రవేశించండి:

- **[00 - త్వరిత ప్రారంభం](../00-quick-start/README.md)** - ప్రాంప్ట్ టెంప్లేట్ మౌలికాలు నేర్చుకోండి
- **[01 - పరిచయం](../01-introduction/README.md)** - సంభాషణ జ్ఞాపక నిర్వహణ తెలుసుకోండి
- **[02 - ప్రాంప్ట్ ఇంజనీరింగ్](../02-prompt-engineering/README.md)** - GPT-5.2 ప్రాంప్ట్ నమూనాలు మాస్టరు అవ్వండి
- **[03 - RAG](../03-rag/README.md)** - రిట్రీవల్-ఆగ్మెంటెడ్ జనరేషన్ సిస్టమ్స్ నిర్మించండి
- **[04 - టూల్స్](../04-tools/README.md)** - ఫంక్షన్ కాలింగ్ మరియు టూల్ చైన్స్ అమలు చేయండి
- **[05 - MCP](../05-mcp/README.md)** - మోడల్ కాంటెక్స్ట్ ప్రోటోకాల్ ఇంటిగ్రేట్ చేయండి

ప్రతి మాడ్యూల్ README ఇక్కడ పరీక్షించిన భావনার వివరణలను అందిస్తుంది.

---

**నావిగేషన్:** [← ప్రధానానికి తిరిగి](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**నిరాకరణ**:  
ఈ డాక్యుమెంట్‌ను AI అనువాద සේవ అయిన [Co-op Translator](https://github.com/Azure/co-op-translator) ఉపయోగించి అనువదించబడింది. మేము ఖచ్చితత్వాన్ని లక్ష్యంగా పెట్టుకున్నప్పటికీ, స్వయంచాలక అనువాదాల్లో లోపాలు లేదా తప్పు సమాచారాలు ఉండవచ్చు అని దయచేసి గమనించండి. ఈ డాక్యుమెంట్ యొక్క అసలు మూల భాషలోనిది అధికారిక వనరు అని పరిగణించాలి. ముఖ్యమైన సమాచారం కోసం, వృత్తిపరమైన మానవ అనువాదాన్ని ఉచితం. ఈ అనువాదం వలన కలిగే ఏవైనా అవగాహన లోపాలు లేదా తప్పు అర్థం చేసుకోవడంపై మేము బాధ్యత వహించమేము.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->