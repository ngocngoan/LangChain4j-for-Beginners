# LangChain4j అనువర్తనాలను పరీక్షించడం

## విషయ సూచిక

- [వేగవంతమైన ప్రారంభం](../../../docs)
- [పరీక్షలు ఏం కవర్ చేస్తాయో](../../../docs)
- [పరీక్షలను నిర్వహించడం](../../../docs)
- [విఎస్ కోడ్‌లో పరీక్షలు నిర్వహించడం](../../../docs)
- [పరీక్షించడం నమూనాలు](../../../docs)
- [పరీక్షా తత్వశాస్త్రం](../../../docs)
- [తదుపరి దశలు](../../../docs)

ఈ గైడ్ API కీలులు లేదా బాహ్య సేవలు అవసరం లేకుండా AI అనువర్తనాలను ఎలా పరీక్షించాలో చూపించే ప్రశ్నావళులను మీరు ఎలా నిర్వహించాలో నడిపిస్తుంది.

## వేగవంతమైన ప్రారంభం

ఒకే కమాండ్‌తో అన్ని పరీక్షలను నడిపించండి:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/te/test-results.ea5c98d8f3642043.webp" alt="సఫలమైన పరీక్ష ఫలితాలు" width="800"/>

*సుమారు విఫలములకు లేని అన్ని పరీక్షలు ఉత్తీర్ణమవుతున్న విజయవంతమైన పరీక్ష అమలు చూపడం*

## పరీక్షలు ఏం కవర్ చేస్తాయో

ఈ కోర్సు స్థానికంగా నడిచే **యూనిట్ టెస్టులు** మీద దృష్టి సారిస్తుంది. ప్రతి పరీక్ష ఒక ప్రత్యేక LangChain4j కాన్సెప్ట్‌ను వేరుగా చూపిస్తుంది.

<img src="../../../translated_images/te/testing-pyramid.2dd1079a0481e53e.webp" alt="పరీక్షా పిరమిడ్" width="800"/>

*యూనిట్ పరీక్షలు (వేగవంతమైనవి, వేరుగా ఉన్నవి), ఇంటిగ్రేషన్ పరీక్షలు (ness నిజమైన భాగాలు), మరియు ఎండ్-టూ-ఎండ్ పరీక్షల మధ్య సంతులనం చూపించే పరీక్షా పిరమిడ్. ఈ శిక్షణ యూనిట్ పరీక్షల పైనే మెలుకువ.*

| మాడ్యూల్ | పరీక్షలు | దృష్టి | ప్రధాన ఫైళ్లు |
|--------|-------|-------|-----------|
| **00 - వేగవంతమైన ప్రారంభం** | 6 | ప్రాంప్ట్ టెంప్లేట్లు మరియు మార్పిడి వేరియబులు | `SimpleQuickStartTest.java` |
| **01 - పరిచయం** | 8 | సంభాషణ స్మృతి మరియు రాష్ట్రపట్ల చాట్ | `SimpleConversationTest.java` |
| **02 - ప్రాంప్ట్ ఇంజనీరింగ్** | 12 | GPT-5.2 నమూనాలు, ఉత్సాహ స్థాయిలు, నిర్మిత అవుట్‌పుట్ | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | డాక్యుమెంట్ ఇంజెస్టన్, ఎంబెడ్డింగ్లు, సారూప్యత శోధన | `DocumentServiceTest.java` |
| **04 - సాధనాలు** | 12 | ఫంక్షన్ కాల్లు మరియు సాధన చైనింగ్ | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | మోడల్ కాన్టెక్స్ట్ ప్రోటోకాల్ తో Stdio ట్రాన్స్‌పోర్ట్ | `SimpleMcpTest.java` |

## పరీక్షలను నిర్వహించడం

**రూట్ నుండి అన్ని పరీక్షలను నడపండి:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**ఒక నిర్దిష్ట మాడ్యూల్ కోసం పరీక్షలు నడపండి:**

**Bash:**
```bash
cd 01-introduction && mvn test
# లేదా రూట్ నుండి
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# లేదా రూట్ నుండి
mvn --% test -pl 01-introduction
```

**ఒకే పరీక్ష తరగతిని నడపండి:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**ఒక నిర్దిష్ట పరీక్ష విధానాన్ని నడపండి:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#సంభాషణ చరిత్రను నిలిపి ఉంచాలి
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#సంభాషణ చరిత్రను నిర్వహించాలి
```

## విఎస్ కోడ్‌లో పరీక్షలు నిర్వహించడం

మీరు Visual Studio Code ఉపయోగిస్తుంటే, టెస్ట్ ఎక్స్‌ప్లోరర్ పరీక్షలను నడిపించడానికి మరియు డీబగ్గింగ్ చేయడానికి గ్రాఫికల్ ఇంటర్‌ఫేస్‌ను అందిస్తుంది.

<img src="../../../translated_images/te/vscode-testing.f02dd5917289dced.webp" alt="VS కోడ్ టెస్ట్ ఎక్స్‌ప్లోరర్" width="800"/>

*VS కోడ్ టెస్ట్ ఎక్స్‌ప్లోరర్ అన్ని జావా పరీక్ష తరగతులు మరియు వ్యక్తిగత పరీక్ష విధానాలతో కూడిన పరీక్ష వృక్షాన్ని చూపిస్తోంది*

**విఎస్ కోడ్‌లో పరీక్షలు నడిపించడానికి:**

1. యాక్టివిటీ బార్‌లో బీకర్ ఐకాన్ పై క్లిక్ చేసి టెస్ట్ ఎక్స్‌ప్లోరర్ తెరవండి
2. అన్ని మాడ్యూల్లు మరియు పరీక్ష తరగతులు చూడటానికి పరీక్ష వృక్షాన్ని విస్తరించండి
3. వేరే వేరే పరీక్షను నడపడానికి దాని పక్కనున్న ప్లే బటన్‌పై క్లిక్ చేయండి
4. మొత్తం సూట్ నడిపించడానికి "Run All Tests" పై క్లిక్ చేయండి
5. ఎటువంటి పరీక్షపై రైట్-క్లిక్ చేసి బ్రేక్ పాయింట్లు సెట్ చేసి కోడ్ దశలు తెలుసుకోవడానికి "Debug Test" ఎంపిక చేయండి

టెస్ట్ ఎక్స్‌ప్లోరర్ పాస్ అయిన పరీక్షలకు ఆకుపచ్చ టిక్ మార్కులు చూపిస్తుంది మరియు విఫలమయ్యేందుకు పూర్తిస్థాయి లోపపు సందేశాలను అందిస్తుంది.

## పరీక్షించడం నమూనాలు

### నమూనా 1: ప్రాంప్ట్ టెంప్లేట్లను పరీక్షించడం

ఈ సరళమైన నమూనా ఏ AI మోడల్‌ను పిలవకుండా ప్రాంప్ట్ టెంప్లేట్లను పరీక్షిస్తుంది. మీరు వేరియబుల్ మార్పిడి సక్రమంగా జరుగుతుందా అని ధృవీకరిస్తారు మరియు టెంప్లేట్లు అనుకూలంగా ఉంటాయో చూద్దాము.

<img src="../../../translated_images/te/prompt-template-testing.b902758ddccc8dee.webp" alt="ప్రాంప్ట్ టెంప్లెట్ పరీక్ష" width="800"/>

*వేరియబుల్ సబ్‌స్టిట్యూషన్ ప్రవాహం చూపిస్తున్న ప్రాంప్ట్ టెంప్లేట్లను పరీక్షించడం: ప్లేస్‌హోల్డర్లు ఉన్న టెంప్లేట్ → విలువలు వర్తించబడ్డాయి → ఫార్మాట్ చేసిన ఔట్‌పుట్ ధృవీకరించబడింది*

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

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#పరీక్షించు ప్రాంప్ట్ టెంప్లెట్ ఫార్మాటింగ్
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#టెస్ట్ ప్రాంప్ట్ టెంప్లేట్ ఫార్మాటింగ్
```

### నమూనా 2: లాంగ్వేజ్ మోడల్స్ ను మాక్ చేయడం

సంభాషణ లాజిక్ పరీక్షించేటప్పుడు, Mockito ఉపయోగించి నిశ్చిత ప్రతిస్పందనలను ఇచ్చే నకిలీ మోడల్స్ సృష్టించండి. ఇది పరీక్షలను వేగవంతమైనవి, ఉచితమైనవి మరియు నిర్దిష్టమైనవి చేస్తుంది.

<img src="../../../translated_images/te/mock-vs-real.3b8b1f85bfe6845e.webp" alt="మాక్ VS రియల్ API తులన" width="800"/>

*పరీక్ష కోసం మాక్‌లను ఎందుకు ఇష్టపడాలో చూపిస్తున్న తులన: ఇవి వేగంగా, ఉచితం, నిర్దిష్టం, మరియు API కీలు అవసరం లేవు*

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
        assertThat(history).hasSize(6); // 3 ఉపయోగकर्ता + 3 AI సందేశాలు
    }
}
```

ఈ నమూనా `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` లో కనిపిస్తుంది. మాక్ నిరంతర ప్రవర్తనను నిర్ధారిస్తుంది కాబట్టి మీరు మెమరీ నిర్వహణ సక్రమంగా పనిచేస్తుందా అన్నది ధృవీకరించవచ్చు.

### నమూనా 3: సంభాషణ వేరుపాటు పరీక్షించడం

సంభాషణ స్మృతి అనేక వినియోగదారులను వేరు వేరు ఉంచాలి. ఈ పరీక్ష సంభాషణలు కాన్టెక్స్ట్ మిశ్రమం కాకుండా ఉండటం నిర్ధారిస్తుంది.

<img src="../../../translated_images/te/conversation-isolation.e00336cf8f7a3e3f.webp" alt="సంభాషణ వేరుపాటు" width="800"/>

*కాంటెక్స్ట్ మిశ్రమానికి దూరం ఉండేలా వేరు వేరే వినియోగదారులకు వేరుగా మెమరీస్టోర్లు చూపిస్తున్న సంభాషణ వేరుపాటు పరీక్ష*

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

ప్రతి సంభాషణ స్వతంత్ర చరిత్రని నిర్వహిస్తుంది. ఉత్పత్తి వ్యవస్థలలో, ఈ వేరుపాటు బహుళ వినియోగదారుల అనువర్తనాలకు కీలకం.

### నమూనా 4: సాధనాలను స్వతంత్రంగా పరీక్షించడం

సాధనాలు AI పిలవగల ఫంక్షన్లు. AI నిర్ణయాలకు సంబంధం లేకుండా ఇవి సక్రమంగా పనిచేస్తున్నాయా అని చూసేందుకు నేరుగా పరీక్షించండి.

<img src="../../../translated_images/te/tools-testing.3e1706817b0b3924.webp" alt="సాధనాల పరీక్ష" width="800"/>

*AI కాల్‌లు లేకుండా నాక్ సాధన అమలును చూపిస్తూ వ్యాపార లాజిక్ ధృవీకరించే సాధనాలను స్వతంత్రంగా పరీక్షించడం*

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

`04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` నుండి ఈ పరీక్షలు AI పాల్గొనకుండానే సాధన లాజిక్ సరి చూసేలా నిర్ధారిస్తాయి. చైనింగ్ ఉదాహరణ ఒక సాధన ఔట్‌పుట్ మళ్లీ మరొక సాధన ఇన్‌పుట్‌గా ఎలా ఇవ్వబడుతుందో చూపిస్తుంది.

### నమూనా 5: ইন-মemory RAG పరీక్ష

RAG సిస్టమ్లు సాధారణంగా వెక్టర్ డేటాబేస్లు మరియు ఎంబెడ్డింగ్ సేవలు అవసరం. ఇన్-మెమరీ నమూనా మీరు మొత్తం పైప్లైన్‌ను బాహ్య ఆధారాలు లేకుండా పరీక్షించగలుగుతుంది.

<img src="../../../translated_images/te/rag-testing.ee7541b1e23934b1.webp" alt="ఇన్-మెమరీ RAG పరీక్ష" width="800"/>

*డాక్యుమెంట్ పార్సింగ్, ఎంబెడ్డింగ్ నిల్వ, మరియు సారూప్యత శోధనను డేటాబేస్ అవసరం లేకుండా చూపిస్తూ ఇన్-మెమరీ RAG పరీక్ష వర్క్‌ఫ్లో*

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

`03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` నుండి ఈ పరీక్ష డాక్యుమెంట్‌ను మెమరీలో సృష్టించి చంకింగ్ మరియు మెటాడేటా నిర్వహణను ధృవీకరిస్తుంది.

### నమూనా 6: MCP ఇంటిగ్రేషన్ పరీక్ష

MCP మాడ్యూల్ stdio ట్రాన్స్‌పోర్ట్ ఉపయోగించి మోడల్ కాన్టెక్స్ట్ ప్రోటోకాల్ ఇంటిగ్రేషన్‌ను పరీక్షిస్తుంది. ఈ పరీక్షలు మీ అనువర్తనం MCP సర్వర్‌లను సబ్-ప్రాసెస్‌లుగా స్టార్ట్ చేసి కమ్యూనికేట్ చేయగలదని ధృవీకరిస్తాయి.

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` లోని పరీక్షలు MCP క్లయింట్ ప్రవర్తనని ధృవీకరిస్తాయి.

**వాటిని నడపండి:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## పరీక్షా తత్వశాస్త్రం

మీ కోడ్‌ను పరీక్షించండి, AIని కాదు. మీరు రాసిన కోడ్ ఎలా పనిచేస్తుందో, ప్రాంప్ట్లు ఎలా రూపొందించబడతాయో, మెమరీ ఎలా నిర్వహించబడుతుందో, సాధనాలు ఎలా అమలు უწుతుందో మాత్రమే ధృవీకరించాలి. AI ప్రతిస్పందనలు మారవచ్చు కాబట్టి అవి పరీక్షా నిర్ణయాలలో భాగం కావు. మీ ప్రాంప్ట్ టెంప్లేట్ సక్రమంగా వేరియబుల్స్ మార్చడమేనా అనేది అడగండి, AI సరైన జవాబు ఇస్తోందా కాదు.

లాంగ్వేజ్ మోడల్స్ కోసం మాక్‌లను ఉపయోగించండి. అవి బాహ్య ఆధారాలు, మెల్లగానే ఉంటాయి, ఖరీదైనవి, మరియు నిర్దిష్టత లేనివి. మాక్ చేయడం వల్ల పరీక్షలు సెలవుల స్థానంలో మిల్లీసెకన్లలో వేగంగా, ఖరీదు లేకుండా మరియు ప్రతి సారి సమాన ఫలితంతో నడుస్తాయి.

పరీక్షలను స్వతంత్రంగా ఉంచండి. ప్రతి పరీక్ష స్వంత డేటాను తయారుచేసుకోవాలి, ఇతర పరీక్షలపై ఆధారపడకూడదు, మరియు తాము ముగిసిన తర్వాత శుభ్రపరుచుకోవాలి. పరీక్షలు అమలు క్రమం నుంచి స్వతంత్రంగా ఉత్తీర్ణమవ్వాలి.

సంతోషకరమైన మార్గం దాటి చివరి సందర్భాలను పరీక్షించండి. ఖాళీ ఇన్‌పుట్‌లు, చాలా పెద్ద ఇన్‌పుట్‌లు, ప్రత్యేక అక్షరాలు, చెల్లనిది పరామితులు, మరియు సరిహద్దు పరిస్థితులు ప్రయత్నించండి. ఇవి సాధారణ ఉపయోగంలో పట్టించుకోకపోయే లోపాలను బయటపెట్టగలవు.

వివరణాత్మక పేర్లను ఉపయోగించండి. `shouldMaintainConversationHistoryAcrossMultipleMessages()` మరియు `test1()` ని పోల్చండి. మొదటిది మీరు ఏమి పరీక్షిస్తున్నారో స్పష్టంగా చెప్తుంది, దోషాల్ని దొరకడం సులభం అవుతుంది.

## తదుపరి దశలు

ఇప్పుడు మీరు పరీక్షా నమూనాలను అర్థం చేసుకున్నందున, ప్రతి మాడ్యూల్ లోకి లోతుగా వెళ్ళండి:

- **[00 - వేగవంతమైన ప్రారంభం](../00-quick-start/README.md)** - ప్రాంప్ట్ టెంప్లేట్ మూలాలను తెలుసుకోండి
- **[01 - పరిచయం](../01-introduction/README.md)** - సంభాషణ స్మృతి నిర్వహణ నేర్చుకోండి
- **[02 - ప్రాంప్ట్ ఇంజనీరింగ్](../02-prompt-engineering/README.md)** - GPT-5.2 ప్రాంప్టింగ్ నమూనాలలో నైపుణ్యం సాధించండి
- **[03 - RAG](../03-rag/README.md)** - రిట్రీవల్-ఆగ్మెంటెడ్ జనరేషన్ సిస్టమ్స్ నిర్మించండి
- **[04 - సాధనాలు](../04-tools/README.md)** - ఫంక్షన్ కాల్లు మరియు సాధన చైన్స్ అమలు చేయండి
- **[05 - MCP](../05-mcp/README.md)** - మోడల్ కాన్టెక్స్ట్ ప్రోటోకాల్ ఇంటిగ్రేట్ చేయండి

ప్రతి మాడ్యూల్ README ఇక్కడ పరీక్షించిన కాన్సెప్ట్‌ల యొక్క విస్తృత వివరాలు అందిస్తుంది.

---

**నావిగేషన్:** [← మెయిన్‌కు తిరిగి](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**అస్పష్టత**:  
ఈ పత్రాన్ని AI అనువాద సేవ [Co-op Translator](https://github.com/Azure/co-op-translator) ఉపయోగించి అనువదించబడింది. మనం ఖచ్చితత్వానికి ప్రయత్నిస్తున్నప్పటికీ, ఆటోమేటెడ్ అనువాదాలలో తప్పులు లేదా లోపాలు ఉండవచ్చు. మొదటి భాషలో ఉన్న అసలు పత్రం అధికారిక మూలంగా తీసుకోవాలి. ముఖ్యమైన సమాచారం కోసం, వృత్తిపరమైన మానవ అనువాద సేవలను ఉపయోగించడం సిఫార్సు చేయబడుతుంది. ఈ అనువాదం ఉపయోగిస్తూ ఏర్పడిన ఏ అవగాహన లోపాలు లేదా తప్పు అర్థం సూచనలకు మేము బాధ్యత వహించము.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->