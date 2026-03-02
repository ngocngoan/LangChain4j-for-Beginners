# LangChain4j பயன்பாடுகளை சோதனை செய்வது

## உள்ளடக்கம்

- [தைவிர் துவக்கம்](../../../docs)
- [சோதனைகள் என்னவற்றை கவர்கிறன](../../../docs)
- [சோதனைகளை இயக்குதல்](../../../docs)
- [VS கோடில் சோதனைகளை இயக்குதல்](../../../docs)
- [சோதனை மாதிரிகள்](../../../docs)
- [சோதனை தத்துவம்](../../../docs)
- [அடுத்த படிகள்](../../../docs)

API விசைகள் அல்லது வெளிப்புற சேவைகள் தேவையில்லாமல் AI பயன்பாடுகளை எப்படி சோதிப்பது என்பதைக் காட்டும் சோதனைகள் வழியாக இந்த கையேடு உங்களை நடத்துகிறது.

## தைவிர் துவக்கம்

ஒரே கட்டளையுடன் அனைத்து சோதனைகளையும் இயக்கவும்:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

அனைத்து சோதனைகளும் கடந்தால், கீழேயுள்ள திரைக்காட்சிப்போன்ற வெளியீடு காணப்படும் — சோதனைகள் பிழையின்றி இயங்குகின்றன.

<img src="../../../translated_images/ta/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*அனைத்து சோதனைகளும் பிழையின்றி வெற்றி பெறுவதை காண்பிக்கும் வெற்றிகரமான சோதனை செயல்பாடு*

## சோதனைகள் என்னவற்றை கவர்கிறன

இந்த பாடநெறி உள்ளூர் இயக்கப்படும் **அலகு சோதனைகள்** மீது கவனம் செலுத்துகிறது. ஒவ்வொரு சோதனையும் தனித்துவமான LangChain4j கருத்தை பிரித்தெடுத்து காட்டுகிறது. கீழே உள்ள சோதனை piramide அலகு சோதனைகள் எங்கு பொருந்துகின்றன என்பதை காட்டுகிறது — அவை வேகமான, நம்பகமான அடித்தளத்தை உருவாக்கி, உங்கள் சோதனைத் திட்டத்தின் மற்ற பகுதிகள் அதன்மேல் கட்டமைக்கப்படுகின்றன.

<img src="../../../translated_images/ta/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*அலகு சோதனைகள் (வேகமான, தனித்துவமான), ஒருங்கிணைப்பு சோதனைகள் (உண்மையான கூறுகள்), மற்றும் இறுதித் துவக்க சோதனைகள் ஆகியவற்றுக்கு இடையேயான சமன்வயத்தைக் காட்டும் சோதனை piramide. இந்த பயிற்சி அலகு சோதனைகளைப் பற்றி.*

| யூனிட்டு | சோதனைகள் | கவனம் | முக்கிய கோப்புகள் |
|--------|-------|-------|-----------|
| **00 - தைவிர் துவக்கம்** | 6 | முன்மொழிவு வடிவமைப்புகள் மற்றும் மாறி இடைநிலை மாற்றம் | `SimpleQuickStartTest.java` |
| **01 - அறிமுகம்** | 8 | உரையாடல் நினைவகம் மற்றும் நிலையை பேணும் உரையாடல் | `SimpleConversationTest.java` |
| **02 - முன்மொழிவு பொறியியல்** | 12 | GPT-5.2 மாதிரிகள், ஆர்வ நிலைகள், அமைப்புக்கேற்ற வெளியீடு | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | ஆவணம் உறிஞ்சல், ஒருங்கிணைப்புகள், ஒத்திருப்புச் தேடல் | `DocumentServiceTest.java`` |
| **04 - கருவிகள்** | 12 | செயல்பாடு அழைப்பு மற்றும் கருவி தொடர் | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | stdio எடுத்துச்சொல்லும் மாடல் சூழல் நெறிமுறை | `SimpleMcpTest.java` |

## சோதனைகள் இயக்குதல்

**அனைத்து சோதனைகளையும் ரூட் இருந்து இயக்க:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**குறிப்பிட்ட யூனிட்டின் சோதனைகள் இயக்க:**

**Bash:**
```bash
cd 01-introduction && mvn test
# அல்லது ரூட் இருந்து
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# அல்லது ரூட் இலிருந்து
mvn --% test -pl 01-introduction
```

**ஒரே சோதனை வகுப்பை இயக்க:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**குறிப்பிட்ட சோதனை முறையை இயக்க:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#உரையாடல் வரலாறை பராமரிக்க வேண்டும்
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#உரையாடல் வரலாற்றை பராமரிக்க வேண்டும்
```

## VS கோடில் சோதனைகள் இயக்குதல்

Visual Studio Code பயன்படுத்தினால், Test Explorer சோதனைகளை இயக்கவும் பிழைநிரப்பவும் ஒரு வரைபட இடைமுகத்தை வழங்குகிறது.

<img src="../../../translated_images/ta/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer கூட்டு Java சோதனை வகுப்புகளையும் தனிப்பட்ட சோதனை முறைகளையும் காட்டுகிறது*

**VS Code இல் சோதனைகள் இயக்க:**

1. பணி பட்டியலில் உள்ள பீக்கர் ஐகானை கிளிக் செய்து Test Explorer திறக்கு
2. சோதனை மரத்தை விரிவாக்கி அனைத்து யூனிட்களையும் சோதனை வகுப்புகளையும் காண்க
3. தனி சோதனைகளை இயக்க எந்த சோதனையின் புற பெருகி பொத்தானை சொடுக்கவும்
4. முழு தொகுப்பையும் இயக்க "Run All Tests" பொத்தானை சொடுக்கவும்
5. எந்த சோதனையையும் வலது கிளிக் செய்து "Debug Test" தேர்வு செய்து இடையேச்சாரல் புள்ளிகளை அமைத்து படி படியே படி பிழைதிருத்தவும்

Test Explorer வெற்றிகரமாக கடந்த சோதனைகளுக்கு பச்சை சரிபார்ப்பு குறியீடுகள் காட்டுகிறது மற்றும் தோல்வி ஏற்பட்டால் விரிவான தவறு செய்திகளை வழங்குகிறது.

## சோதனை மாதிரிகள்

### மாதிரி 1: முன்மொழிவு வடிவமைப்புகளை சோதனை செய்தல்

எளிமையான மாதிரி ஏதாவது AI மாடலை அழைக்காமலும் முன்மொழிவு வடிவமைப்புகளை சோதனை செய்கிறது. மாறி இடைநிலை மாற்றம் சரியாக செயல்படுவதை மற்றும் முன்மொழிவுகள் எதிர்பார்க்கப்படும் வடிவத்தில் உள்ளதென நீங்கள் உறுதி செய்கிறீர்கள்.

<img src="../../../translated_images/ta/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*மாறி இடைநிலையில் வடிவமைப்பை சோதனை செய்வது: இடைப்பிரதிகளுடன் முன்மொழிவு → பொருத்தப்பட்ட மதிப்புகள் → வடிவமைக்கப்பட்ட வெளியீடு சரிபார்த்தல்*

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

இந்த சோதனை `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java` உள்ளடக்கத்தில் உள்ளது.

**இதை இயக்க:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#சோதனைவாக்கிய வடிவமைப்பு
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#சோதனைPromptவைஅமைத்தல்
```

### மாதிரி 2: மொழி மாதிரிகள் மொக்கு செய்தல்

உரையாடல் தர்க்கத்தை சோதிக்கும் போது, Mockito ஐ பயன்படுத்தி முன்கூட்டியே தீர்மானிக்கப்பட்ட பதில்களை வழங்கும் புனைவு மாதிரிகளை உருவாக்கவும். இவ்வாறு சோதனைகள் வேகமானவை, இலவசமானவை மற்றும் தீர்மானிக்கப்பட்டவை ஆகின்றன.

<img src="../../../translated_images/ta/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*ஏன் மொக்குகளை சோதனைகளுக்கு முன்னுரிமையிடுகிறார்கள் என்பதை காட்டும் ஒப்பிடுகை: அவை வேகமானவை, இலவசம், தீர்மானிக்கப்பட்டவை மற்றும் API விசைகள் தேவையில்லை*

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

இந்த மாதிரி `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` இல் உள்ளது. மொக்குகள் நினைவக மேலாண்மை சரியாக வேலை செய்கிறதா என்பதை உறுதி செய்ய ஒரே மாதிரிச் செயல்பாட்டை வைத்திருக்கின்றன.

### மாதிரி 3: உரையாடல் தனிமைப்படுத்தல் சோதனை

உரையாடல் நினைவகம் பல பயனர்களை தனித்தனி வைக்க வேண்டும். இந்த சோதனை உரையாடல்கள் தரவுகளை கலப்பதில்லை என்பதைக் காட்டுகிறது.

<img src="../../../translated_images/ta/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*பல்வேறு பயனர்களுக்கான தனித்தனி நினைவக சேவைகளை காட்டும் உரையாடல் தனிமைப்படுத்தல் சோதனை, சூழலை கலக்க தடுக்கும்*

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

ஒவ்வொரு உரையாடலும் அதன் தனிப்பட்ட வரலாற்றைப் பேணுகிறது. உற்பத்தித் திட்டங்களில், இது பல பயனர் பயன்பாடுகளுக்கு மிகவும் அவசியமான தனிமைப்படுத்தல்.

### மாதிரி 4: கருவிகளை தனித் தனியாக சோதனை செய்தல்

கருவிகள் AI அழைக்கக்கூடிய செயல்பாடுகள் ஆகும். AI முடிவுகளைத் தவிர நேரடியாக அவற்றைச் சோதியுங்கள், அவை முறையாக இயங்குகிறதா என்று உறுதி செய்ய.

<img src="../../../translated_images/ta/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*AI அழைப்புகள் இல்லாமல் மொங்கு கருவி செயல்பாடு மூலம் தொழில்துறை தர்க்க சூழலைச் சோதற்சி செய்தல்*

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

`04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` இல் உள்ள இச்சோதனைகள் AI தொடர்பில்லாமல் கருவி காரணியறிதல்களை சரிபார்க்கின்றன. தொடர் உதாரணம் ஒரு கருவியின் வெளியீடு மற்றொன்றின் உள்ளீடாகக் கிடைப்பதை காட்டுகிறது.

### மாதிரி 5: நினைவகத்தில் RAG சோதனை

RAG முறைமைகள் வழமையாக வெக்டர் தரவுத்தளங்கள் மற்றும் ஒருங்கிணைப்பு சேவைகளை தேவைப்படுத்துகின்றன. நினைவக மாதிரியில், வெளிப்புற சார்புகள் இல்லாமல் முழு குழாயை சோதிக்கலாம்.

<img src="../../../translated_images/ta/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*வெளிப்புற தரவுத்தளம் தேவையில்லாமல் ஆவண பகிரல், ஒருங்கிணைப்பு சேமிப்பு, ஒத்திருப்புச் தேடல் ஆகியவற்றை நினைவகத்தில் சோதனை செய்யும் செயல்முறை*

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

`03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` இல் இந்த சோதனை நினைவகத்தில் ஆவணத்தை உருவாக்கி துண்டித்து, மெட்டா தகவல்களை சரிபார்க்கிறது.

### மாதிரி 6: MCP ஒருங்கிணைப்பு சோதனை

MCP முறைமையை stdio எடுத்துச்சொல்லும் மூலம் ஒருங்கிணைக்கும் MCP தொகுதி சோதனைகள். இச்சோதனைகள் உங்கள் பயன்பாடு MCP சேவைகளை subprocess ஆக உருவாக்கி தொடர்பு கொள்ளும் திறனை உறுதி செய்கின்றன.

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` இல் உள்ள இந்த சோதனைகள் MCP கிளையண்ட் நடத்தை சரிபார்க்கின்றன.

**இவற்றை இயக்க:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## சோதனை தத்துவம்

AI-வை அல்ல, உங்கள் கோடுகளை சோதியுங்கள். நீங்கள் எழுதும் கோடு கட்டமைவு எப்படி என்பதை, நினைவக மேலாண்மை மற்றும் கருவி செயல்பாடுகளை உங்கள் சோதனைகள் சரிபார்க்க வேண்டும். AI பதில்கள் மாறுபடக்கூடியவை, அதை சோதனை நிபந்தனைகளில் சேர்க்க வேண்டாம். உங்கள் முன்மொழிவு வடிவமைப்பு மாறிகளை சரியாக மாற்றுகிறதா என்பதை பரிசீலியுங்கள், AI சரியான பதிலை தருகிறதா என்று அல்ல.

மொழி மாதிரிகளுக்கு மொக்குகளை பயன்படுத்துங்கள். அவை வெளிப்புற சார்புகளாகும்; அவை மெதுவானவை, விலைப்பட்டவை மற்றும் தீர்மானிக்க முடியாதவை. மொக்குகள் சோதனைகளை சில மில்லியசெகன்களில் விரைவாக, இலவசமாக, மற்றும் தீர்மானிக்கப்பட்டவையாக மாற்றுகின்றன.

சோதனைகள் தனித்தனியாக இருக்கட்டும். ஒவ்வொரு சோதனையும் தானாகவே தரவை அமைக்க வேண்டும், மற்ற சோதனைகளில் சார்ந்திருக்க கூடாது மற்றும் முடிந்தவுடன் சுத்தம் செய்ய வேண்டும். சோதனைகள் இயக்கிய வரிசைக்கு பாதிப்பு இல்லாமல் கடந்து விட வேண்டும்.

சந்தோசமான வழி தவிர வேறுபடுபவற்றையும் சோதியுங்கள். காலியான உள்ளீடு, மிகவும் பெரிய உள்ளீடு, சிறப்பு எழுத்துக்கள், தவறான அளவுருக்கள் மற்றும் எல்லை நிலைகள் போன்றவற்றை முயற்சிக்கவும். இவை வழக்கமான பயன்பாட்டில் தெரியாத பிழைகளை வெளிப்படுத்தும்.

விளக்கமான பெயர்களை பயன்படுத்துங்கள். `shouldMaintainConversationHistoryAcrossMultipleMessages()` மற்றும் `test1()` ஐ ஒப்பிடுக. முதல் பெயர் என்ன சோதனை செய்யப் பட்டதென்று தெளிவாக சொல்கிறது, தவறுகளை கண்டறிய உதவும்.

## அடுத்த படிகள்

சோதனை மாதிரிகளை நீங்கள் புரிந்துவிட்டீர்கள், இப்பொழுது ஒவ்வொரு யூனிட்டிலும் ஆழமாக நுழையுங்கள்:

- **[00 - தைவிர் துவக்கம்](../00-quick-start/README.md)** - முன்மொழிவு வடிவமைப்பு அடிப்படைகள்
- **[01 - அறிமுகம்](../01-introduction/README.md)** - உரையாடல் நினைவகம் மேலாண்மை கற்றல்
- **[02 - முன்மொழிவு பொறியியல்](../02-prompt-engineering/README.md)** - GPT-5.2 முன்மொழிவு மாதிரிகள் கற்றல்
- **[03 - RAG](../03-rag/README.md)** - பெறுமதி கூட்டிய உருவாக்கம் அமைப்புகள் கட்டமைத்தல்
- **[04 - கருவிகள்](../04-tools/README.md)** - செயல்பாடு அழைப்பு மற்றும் கருவி தொடர்கள் நடைமுறைப்படுத்தல்
- **[05 - MCP](../05-mcp/README.md)** - மாடல் சூழல் நெறிமுறை ஒருங்கிணைத்தல்

ஒவ்வொரு யூனிட்டின் README இல் இங்கு சோதிக்கப்பட்ட கருத்துக்களின் விரிவான விளக்கங்கள் உள்ளன.

---

**வழிசெலுத்தல்:** [← பிரதானத்துக்கு திரும்ப](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**விராதி**:  
இந்த ஆவணம் AI மொழிபெயர்ப்பு சேவை [Co-op Translator](https://github.com/Azure/co-op-translator) பயன்படுத்தி மொழிபெயர்க்கப்பட்டுள்ளது. நாங்கள் துல்லியத்தன்மைக்கு முயற்சி செய்கிறோம், எனினும் தானாக செய்யப்பட்ட மொழிபெயர்ப்புகளில் பிழைகள் அல்லது தவறுகள் இருக்க வாய்ப்பு உள்ளது என்பதை கவனிக்கவும். இயற்பொருள் ஆவணம் அதன் சொந்த மொழியிலேயே அதிகாரப்பூர்வமான வன்பொருளாக கருதப்பட வேண்டும். முக்கியமான தகவல்களுக்காக, தொழில்முறை மனித மொழிபெயர்ப்பை பரிந்துரைக்கிறோம். இந்த மொழிபெயர்ப்பின் பயன்படுத்தலால் ஏற்படும் எந்த தவறிய புரிதல் அல்லது விமர்சனங்களிலும் நாங்கள் பொறுப்பேற்கமாட்டோம்.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->