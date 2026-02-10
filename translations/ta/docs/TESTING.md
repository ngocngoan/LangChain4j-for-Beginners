# LangChain4j செயலிகளுக்கு சோதனை

## உள்ளடக்கம்

- [துரித தொடக்கம்](../../../docs)
- [சோதனைகள் என்ன கையாள்கின்றன](../../../docs)
- [சோதனைகள் இயக்குதல்](../../../docs)
- [VS கோடில் சோதனைகள் இயக்குதல்](../../../docs)
- [சோதனை வடிவங்கள்](../../../docs)
- [சோதனை தத்துவம்](../../../docs)
- [அடுத்த படிகள்](../../../docs)

API சாவிகள் அல்லது வெளியீட்டு சேவைகள் தேவையாலில்லாமல் AI செயலிகளை சோதனை செய்வதற்கு எப்படி சோதனைகள் நடத்துவது என்பதை காட்டு வழிகாட்டி இது.

## துரித தொடக்கம்

ஒன்றே கட்டளையுடன் அனைத்து சோதனைகளையும் இயக்குக:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/ta/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*அனைத்து சோதனைகளும் தோல்வி இல்லாமல் வெற்றிகரமாக இயங்குவதை காட்டுகிறது*

## சோதனைகள் என்ன கையாள்கின்றன

இந்த பாடநெறி உள்ளே **தனி சோதனைகள்** இடத்தில் இயங்கும் என்பதை கவனிக்கும். ஒவ்வொரு சோதனையும் தனித்தனியாக LangChain4j கருத்துக்களை காட்டுகிறது.

<img src="../../../translated_images/ta/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*தனி சோதனைகள் (அவசியம் விரைந்தவை, தனிமைப்படுத்தப்பட்டவை), ஒருங்கிணைவு சோதனைகள் (உண்மையான கூறுகள்), மற்றும் முழுமையான முடிவு சோதனைகளுக்கு இடையில் சோதனை மண்டலம். இந்த பயிற்சி தனி சோதனையை உள்ளடக்குகிறது.*

| தொகுதி | சோதனைகள் | கவனம் | முக்கியக் கோப்புகள் |
|--------|-------|-------|-----------|
| **00 - துரித தொடக்கம்** | 6 | எழுத்துரு வடிவங்கள் மற்றும் மாறி பதிலிடுதல் | `SimpleQuickStartTest.java` |
| **01 - அறிமுகம்** | 8 | உரையாடல் நினைவகம் மற்றும் நிலையான அரட்டை | `SimpleConversationTest.java` |
| **02 - எழுத்துரு பொறியியல்** | 12 | GPT-5.2 மாதிரிகள், ஆர்வ நிலைகள், கட்டமைக்கப்பட்ட பிறவ அறிவியல் | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | ஆவணம் இறக்குமதி, என்ெம்பெட்டிங்ஸ், ஒத்திசைவு தேடல் | `DocumentServiceTest.java` |
| **04 - கருவிகள்** | 12 | செயல்பாடு அழைப்பு மற்றும் கருவி சங்கிலி | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | மாதிரி உள்ளடக்கு நெறிமுறை, stdio போக்குவரத்து | `SimpleMcpTest.java` |

## சோதனைகள் இயக்குதல்

**ஒன்றைச் சில பணியிலிருந்து அனைத்து சோதனைகளையும் இயக்குக:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**ஒரு குறிப்பிட்ட தொகுதி சோதனைகளை இயக்குக:**

**Bash:**
```bash
cd 01-introduction && mvn test
# அல்லது ரூட் இருந்து
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# அல்லது ரூட் இருந்து
mvn --% test -pl 01-introduction
```

**ஒரு தனி சோதனை வகுப்பு இயக்குக:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**ஒரு குறிப்பிட்ட சோதனை முறையை இயக்கவும்:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#உரையாடல் வரலாற்றை பராமரிக்க வேண்டும்
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#உரையாடல் வரலாற்றை பராமரிக்க வேண்டும்
```

## VS Codeல் சோதனைகள் இயக்குதல்

நீங்கள் Visual Studio Code பயன்படுத்தினால், சோதனை ஆராய்பவர் சோதனைகளை இயக்கவும் தொடர் பிழைகளை கண்டறியவும் கிராபிகல் இடைமுகத்தை வழங்கும்.

<img src="../../../translated_images/ta/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code சோதனை ஆராய்பவர் அனைத்து ஜாவா சோதனை வகுப்புகளையும் தனித்தனியான சோதனை முறைகளையும் காட்டுகிறது*

**VS Codeல் சோதனைகள் இயக்க:**

1. செயல்பாட்டு பட்டியில் இருக்கை பெக்கர் ஐக்கானை சொடுக்கி சோதனை ஆராய்பவரை திறக்கவும்
2. அனைத்து தொகுதிகள் மற்றும் சோதனை வகுப்புகளைப் பார்க்க சோதனை மரத்தை விரிவாக்கவும்
3. தனித்தனியாக இயக்க வேண்டிய சோதனையை பக்கமுள்ள விளையாட்டு பொத்தானை அழுத்தவும்
4. முழு தொகுதியையும் இயக்க "Run All Tests" கிளிக் செய்க
5. எந்த சோதனையையும் வலது கிளிக் செய்து "Debug Test" தேர்வு செய்து புரிந்து பிடிக்கும் இடங்களை அமைத்து குறியாக்கம் செல்வதற்கான படிகள் எடுக்கவும்

சோதனை ஆராய்பவர் பசுமை குறி(Checkmarks) மூலம் வெற்றிகரமான சோதனைகளைக் காட்டுகிறது மேலும் தோல்வி ஏற்பட்டால் விரிவான தவறு செய்திகளை வழங்குகிறது.

## சோதனை வடிவங்கள்

### வடிவம் 1: எழுத்துரு மாதிரிகள் சோதனை

எளிமையான வடிவம் எந்த AI மாதிரியை அழைக்காது எழுத்துரு மாதிரிகளை சோதனை செய்கிறது. மாறி பதிலிடுதல் சரியாக அமைகிறதா மற்றும் எழுத்துருக்கள் பதிவுகள் உண்மையிலேயே விரும்பிய வடிவில் இருப்பதை உறுதிசெய்கிறது.

<img src="../../../translated_images/ta/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*எழுத்துரு மாதிரிகளை சோதனை செய்தால் மாறி பதிலிடுதல் அந்தக்கோப்புகளில் → மதிப்புகள் பயன்படுத்தப்பட்டது → வடிவமைக்கப்பட்ட வெளியீடு சரிபார்க்கப்பட்டது*

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

இந்த சோதனை `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java` என்ற கோப்பில் உள்ளது.

**இயக்குக:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#சோதனைசூழ்நிலையடிக்கோள் வடிவமைப்பு
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#சோதனைPrompTemplateFormatting
```

### வடிவம் 2: மொழி மாதிரிகள் மேற்கோள் கொடுக்கும்

உரையாடல் தர்க்கத்தை சோதிக்கும் போது, Mockito கொண்டு உருவாக்கப்பட்ட மாதிரிகளை பயன்படுத்துங்கள், அவை முன்னிருப்பான பதில்களை வழங்கும். இத்தகைய சோதனைகள் விரைவானவை, இலவசமாக கிடைக்கும் மற்றும் முடிவுகூர்ந்தவை.

<img src="../../../translated_images/ta/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*சோதனைகளுக்கு மோக்களே ஏன் விரும்பத்தகுந்தவை என்ற ஒப்பீடு: அவை விரைவானவை, இலவசம், முடிவுகூறியவை, API சாவிகள் தேவையில்லை*

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
        assertThat(history).hasSize(6); // 3 பயனர் + 3 AI செய்திகள்
    }
}
```

இது `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` இல் உள்ளது. மோக் ஒருங்கிணைந்த பண்பை உறுதிப்படுத்துகிறது இதன் மூலம் நினைவு நிர்வாகம் சரியாக இயங்குவதைச் சரிபார்க்கலாம்.

### வடிவம் 3: உரையாடல் தனிமைப்படுத்தல் சோதனை

உரையாடல் நினைவகம் பல பயனர்களை தனித்தனியாக வைத்திருக்க வேண்டும். இந்த சோதனை உரையாடல்கள் கான்டெக்ஸ்ட்களை கலக்காமல் வைத்திருக்கிறதா என்பதை பரிசோதிக்கிறது.

<img src="../../../translated_images/ta/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*உரையாடல் தனிமைப்படுத்தலை சோதனை செய்யும் போது வெவ்வேறு பயனர்களுக்கான தனித்த நினைவகங்கள் காணப்படுகிறது, கான்டெக்ஸ்ட் கலப்பைக் தடுக்கும்*

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

ஒவ்வொரு உரையாடலுக்கும் தனியாக சுயாதீன வரலாறு இருக்கிறது. உற்பத்தி முறைகளில், இந்த தனிமைப்படுத்தல் பல பயனர் செயலிகளுக்கு முக்கியமானது.

### வடிவம் 4: கருவிகளை தனியாக சோதனை செய்தல்

கருவிகள் AI அழைக்கக்கூடிய செயல்கள். அவைகளை நேரடியாக சோதனை செய்யுங்கள் அவற்றின் செயல்திறன் AI முடிவுகளுக்கு உட்படாமல் சரியாக இருக்கிறதா என உறுதிப்படுத்த.

<img src="../../../translated_images/ta/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*கருவிகளை தனித்தனியாக சோதனை செய்து AI அழைப்புகளின்றி வணிக தர்க்கம் சரிவர செயல்படுகிறதா என்பதைக் கண்டறியும்*

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

இவை `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` இல் உள்ள சோதனைகள், AI தொடர்பில்லாமல் கருவி தர்க்கத்தை உறுதிப்படுத்துகின்றன. சங்கிலி உதாரணம் ஒரு கருவியின் வெளியீடு மற்றொன்றில் உள்ளீடாக போக்வதை காட்டுகிறது.

### வடிவம் 5: நினைவகத்தில் RAG சோதனை

RAG முறைகள் உதாரணமாக வெக்டர் தரவுத்தளங்கள் மற்றும் எனெம்பெட்டிங் சேவைகள் தேவைபடும். நினைவக வடிவம் முழுப் படியை வெளியீட்டு சார்பற்றமாக சோதனை செய்ய உதவுகிறது.

<img src="../../../translated_images/ta/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*நினைவகத்தில் RAG சோதனை பணியோட்டம் ஆவணங்களை பகுப்பாய்வு செய்தல், எனெம்பெட்டிங் சேமிப்பு, மற்றும் ஒத்திசைவு தேடல் தரவுத்தளம் தேவை இல்லாமல்*

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

இந்த சோதனை `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java`இல் உள்ளது; இது ஆவணத்தை நினைவில் உருவாக்கி துண்டாக்கல் மற்றும் மேட்டாத்தரங்களை கையாள்வதை உறுதி செய்கிறது.

### வடிவம் 6: MCP ஒருங்கிணைவு சோதனை

MCP தொகுதி மாதிரி உள்ளடக்கு நெறிமுறை(stdio போக்குவரத்து) ஒருங்கிணைப்பை சோதனை செய்கிறது. இந்த சோதனைகள் உங்கள் செயலி MCP சர்வர்களுடன் துணைமுறை நகர்வுகள் மற்றும் தொடர்புகளை உருவாக்க மற்றும் பரிமாற முடியும் என்பதை உறுதிசெய்கிறது.

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java`இல் உள்ள சோதனைகள் MCP கிளையண்ட் பண்பை சரிபார்க்கின்றன.

**இயக்குக:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## சோதனை தத்துவம்

உங்கள் கோ드를 சோதியுங்கள், AI-ஐ அல்ல. உங்கள் சோதனைகள் நீங்கள் எழுதும் கோடு எப்படி செயல்படுகிறது என்பதை உறுதிசெய்ய வேண்டும் - எழுத்துரு உருவாக்குவது, நினைவகம் நிர்வாகம், கருவிகள் செயல் செய்யும் விதம் ஆகியவை. AI பதில்கள் மாறுபடும்; அவை சோதனை உறுதிப்பாடுகளில் இருக்க கூடாது. உங்கள் எழுத்துரு மாதிரி மாறிகள் சரியாக பதிலிடுகிறதா என்பதை கேளுங்கள், AI சரியான பதிலை தருகிறதா என்று அல்ல.

மொழி மாதிரிகளுக்கு மோக்களைப் பயன்படுத்தவும். அவை வெளியிடப்பட்ட சார்புகள், நெடிய மற்றும் செலவானவை, முடிவுகூறாதவை. மோக்கிங் சோதனைகளை வினாடிகள் அல்ல, மில்லிசெக்கன்களில் விரைவாகவும், இலவசமாகவும், ஒரே முடிவுடன் செய்ய உதவும்.

சோதனைகளை தனித்தனியாக வைத்திருங்கள். ஒவ்வொரு சோதனையும் அதன் சொந்த தரவு ஏற்பாடு செய்ய வேண்டும், மற்ற சோதனைகளின்படி சாராம்படாது, மற்றும் முடிந்த பிறகு துப்புரவு செய்ய வேண்டும். சோதனைகள் இயக்கும் வரிசையை பொருட்படுத்தாமல் கடக்க வேண்டும்.

சந்தேகமான அம்சங்களை சோதியுங்கள். வெறுமை உள்ளீடுகள், மிக பெரிய உள்ளீடுகள், சிறப்பு எழுத்துக்கள், தவறான அளவுருக்கள், எல்லை நிலைமைகள் போன்றவை. அவை பொதுவான பயன்முறையில் வெளிப்படாத பிழைகளை கண்டறியும்.

விளக்கமான பெயர்களைப் பயன்படுத்தவும். `shouldMaintainConversationHistoryAcrossMultipleMessages()` ஐ `test1()` உடன் ஒப்பிடுங்கள். முதல் படி நீங்கள் சோதிக்கும் விஷயத்தை தெளிவாக சொல்கிறது, பிழைகளை கண்டுபிடிக்க எளிதாக்குகிறது.

## அடுத்த படிகள்

இப்போது நீங்கள் சோதனை வடிவங்களை புரிந்தீர்கள், ஒவ்வொரு தொகுதியிலும் ஆழமாகப் புரிந்து கொள்ளுங்கள்:

- **[00 - துரித தொடக்கம்](../00-quick-start/README.md)** - எழுத்துரு மாதிரி அடிப்படைகளைத் தொடங்கு
- **[01 - அறிமுகம்](../01-introduction/README.md)** - உரையாடல் நினைவக நிர்வாகத்தை கற்றுக்கொள்ளுங்கள்
- **[02 - எழுத்துரு பொறியியல்](../02/prompt-engineering/README.md)** - GPT-5.2 எழுத்துரு மாதிரிகள் கற்றுக்கொள்ளுங்கள்
- **[03 - RAG](../03-rag/README.md)** - மீட்டெடுப்பு மேம்படுத்தப்பட்ட உருவாக்க அமைப்புகளை கட்டமைக்கவும்
- **[04 - கருவிகள்](../04-tools/README.md)** - செயல்பாடு அழைப்புகள் மற்றும் கருவி சங்கிலிகளை செயல்படுத்தவும்
- **[05 - MCP](../05-mcp/README.md)** - மாதிரி உள்ளடக்கு நெறிமுறையை ஒருங்கிணைக்கவும்

ஒவ்வொரு தொகுதியின் README இல் இங்கே சோதிக்கப்பட்ட கருத்துகளின் விரிவான விளக்கங்கள் உள்ளன.

---

**Navigation:** [← முதன்மை பக்கத்துக்கு திரும்பு](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**குறிப்பு**:  
இந்த ஆவணம் [Co-op Translator](https://github.com/Azure/co-op-translator) என்ற எய்ஐ மொழிபெயர்ப்பு சேவையை பயன்படுத்தி மொழிபெயர்க்கப்பட்டது. துல்லியத்தை உறுதி செய்வதற்காக முயற்சி செய்துள்ளோம் என்றாலும், தானியங்கி மொழிபெயர்ப்புகளில் பிழைகள் அல்லது தவறுகள் இருப்பதற்கு வாய்ப்பு உள்ளது. அசல் ஆவணம் அதன் தனிப்பட்ட மொழியில் அதிகாரப்பூர்வ ஆதாரமாக கருதப்பட வேண்டும். முக்கியமான தகவல்களுக்கு, தொழில்நுட்பமான மனித மொழிபெயர்ப்பை பரிந்துரைக்கிறோம். இந்த மொழிபெயர்ப்பின் பயன்பாட்டினால் எழுபடக்கூடிய தவறான புரிதல்கள் அல்லது தவறான விளக்கங்களுக்க 우리는 பொறுப்பேற்கவில்லை.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->