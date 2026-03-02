# LangChain4j अनुप्रयोगांची चाचणी

## अनुक्रमणिका

- [जलद प्रारंभ](../../../docs)
- [चाचणी काय झाकतात](../../../docs)
- [चाचण्या चालविणे](../../../docs)
- [VS कोडमध्ये चाचण्या चालविणे](../../../docs)
- [चाचणी नमुने](../../../docs)
- [चाचणी तत्त्वज्ञान](../../../docs)
- [पुढील पाउले](../../../docs)

हा मार्गदर्शक आपल्याला API की किंवा बाह्य सेवा न वापरता AI अनुप्रयोग कसे चाचणी करायच्या याचे उदाहरण देणाऱ्या चाचण्या कशा चालवायच्या हे सांगतो.

## जलद प्रारंभ

सर्व चाचण्या एकाच आज्ञेत चालवा:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

सर्व चाचण्या यशस्वी झाल्यावर तुमच्याकडे खालीलप्रमाणे स्क्रीनशॉटसारखे आउटपुट दिसेल — चाचण्या शून्य चुका न करता चालतात.

<img src="../../../translated_images/mr/test-results.ea5c98d8f3642043.webp" alt="यशस्वी चाचणी परिणाम" width="800"/>

*सर्व चाचण्या यशस्वी झाल्याचे दर्शविणारे चाचणी निष्पादन*

## चाचणी काय झाकतात

हा अभ्यासक्रम मुख्यतः **युनिट चाचण्यांवर** लक्ष केंद्रित करतो जे स्थानिकरित्या चालतात. प्रत्येक चाचणी स्वतंत्रपणे LangChain4j संकल्पना दाखवते. खालील चाचणी पिरॅमिड दर्शवतो की युनिट चाचण्या कुठे बसतात — त्या वेगवान आणि विश्वासार्ह पाया तयार करतात ज्यावर तुमच्या इतर चाचणी धोरणाची पायाभरणी असते.

<img src="../../../translated_images/mr/testing-pyramid.2dd1079a0481e53e.webp" alt="चाचणी पिरॅमिड" width="800"/>

*युनिट चाचणी (वेगवान, स्वतंत्र), इंटीग्रेशन चाचणी (खऱ्या घटकांसह), आणि एंड-टू-एंड चाचणी यांच्यातील संतुलन दर्शवते. हा प्रशिक्षण युनिट चाचणीवर आधारित आहे.*

| मॉड्यूल | चाचण्या | लक्ष केंद्रित | मुख्य फायली |
|--------|---------|-------------|-------------|
| **00 - जलद प्रारंभ** | 6 | प्रॉम्प्ट साचे आणि व्हेरीएबल सबस्टिट्यूशन | `SimpleQuickStartTest.java` |
| **01 - परिचय** | 8 | संभाषण स्मृती आणि Stateful चॅट | `SimpleConversationTest.java` |
| **02 - प्रॉम्प्ट अभियांत्रिकी** | 12 | GPT-5.2 नमुने, उत्कटता स्तर, संरचित आउटपुट | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | दस्तऐवज आंतरभूत करणे, एम्बेडिंग्ज, सादृश्य शोध | `DocumentServiceTest.java` |
| **04 - साधने** | 12 | फंक्शन कॉलिंग आणि टूल चेनिंग | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | मॉडेल संदर्भ प्रोटोकॉल Stdio ट्रान्सपोर्टसह | `SimpleMcpTest.java` |

## चाचण्या चालविणे

**रूटमधून सर्व चाचण्या चालवा:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**विशिष्ट मॉड्यूलसाठी चाचण्या चालवा:**

**Bash:**
```bash
cd 01-introduction && mvn test
# किंवा मुळापासून
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# किंवा मूळ पासून
mvn --% test -pl 01-introduction
```

**एकल चाचणी वर्ग चालवा:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**विशिष्ट चाचणी पद्धत चालवा:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#संभाषण इतिहास राखावा
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#संभाषण इतिहास राखला पाहिजे
```

## VS कोडमध्ये चाचण्या चालविणे

जर तुम्ही Visual Studio Code वापरत असाल, तर Test Explorer चाचण्या चालविण्यासाठी आणि डिबग करण्यासाठी ग्राफिकल इंटरफेस उपलब्ध करतो.

<img src="../../../translated_images/mr/vscode-testing.f02dd5917289dced.webp" alt="VS कोड टेस्ट एक्सप्लोरर" width="800"/>

*VS कोड टेस्ट एक्सप्लोरर सर्व Java चाचणी वर्ग आणि स्वतंत्र चाचणी पद्धतींसह चाचणी वृक्ष दाखवत आहे*

**VS कोडमध्ये चाचण्या चालवण्यासाठी:**

1. Activity Bar मध्ये बीकर चिन्हावर क्लिक करून Test Explorer उघडा
2. सर्व मॉड्यूल आणि चाचणी वर्ग पाहण्यासाठी चाचणी वृक्ष विस्तृत करा
3. कोणतीही चाचणी एकट्यासाठी चालविण्यासाठी प्ले बटण क्लिक करा
4. संपूर्ण संच चालवण्यासाठी "Run All Tests" क्लिक करा
5. कोणतीही चाचणी वर उजवे क्लिक करून "Debug Test" निवडा, ब्रेकपॉइंट सेट करा आणि कोड स्टेप करा

Test Explorer चाचणी पास झाल्यास हिरव्या चेकमार्क्स दाखवतो आणि चाचणी अयशस्वी झाल्यास तपशीलवार त्रुटी संदेश प्रदान करतो.

## चाचणी नमुने

### नमुना 1: प्रॉम्प्ट टेम्पलेट्सची चाचणी

सर्वात सोपा नमुना AI मॉडेल कॉल न करता प्रॉम्प्ट टेम्पलेट्सची चाचणी करतो. यात व्हेरीएबल सबस्टिट्यूशन योग्यप्रकारे होते आणि प्रॉम्प्ट अपेक्षित स्वरूपात तयार होतात हे तपासले जाते.

<img src="../../../translated_images/mr/prompt-template-testing.b902758ddccc8dee.webp" alt="प्रॉम्प्ट टेम्पलेट चाचणी" width="800"/>

*व्हेरीएबल सबस्टिट्यूशन प्रवाह दाखविणारे प्रॉम्प्ट टेम्पलेट चाचणी: प्लेसहोल्डर्ससह टेम्पलेट → मूल्ये लागू केली → स्वरूपित आउटपुट पडताळले*

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

ही चाचणी `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java` मध्ये आहे.

**चालवा:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#चाचणीप्रॉम्प्टसाचा टेम्पलेट स्वरूपांकन
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#चाचणीप्रॉम्प्टटेम्पलेटफॉरमॅटिंग
```

### नमुना 2: भाषा मॉडेल्सचे मॉकिंग

संभाषण लॉजिकची चाचणी करताना Mockito वापरून ठरवलेले प्रतिसाद देणारी खोटी मॉडेल तयार करा. यामुळे चाचण्या वेगवान, विनामूल्य आणि निश्चित होतात.

<img src="../../../translated_images/mr/mock-vs-real.3b8b1f85bfe6845e.webp" alt="मॉक विरुद्ध वास्तविक API तुलना" width="800"/>

*चाचणीसाठी मॉक का प्राधान्य दिले जाते हे दाखवते: ते वेगवान, विनामूल्य, निश्चित आणि API कीची गरज नसलेले असतात*

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
        assertThat(history).hasSize(6); // 3 वापरकर्ता + 3 AI संदेश
    }
}
```

हा नमुना `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` मध्ये आढळतो. मॉक सुनिश्चित करतो की वागणूक सुसंगत राहील ज्यामुळे स्मृती व्यवस्थापन योग्य कार्य करतो हे तपासता येते.

### नमुना 3: संभाषण वेगळेपणा चाचणी

संभाषण स्मृतीने अनेक वापरकर्ते स्वतंत्र ठेवले पाहिजेत. ही चाचणी तपासते की संभाषणे एकमेकाचा संदर्भ मिसळत नाहीत.

<img src="../../../translated_images/mr/conversation-isolation.e00336cf8f7a3e3f.webp" alt="संभाषण वेगळेपणा" width="800"/>

*वेगळ्या वापरकर्त्यांसाठी स्वतंत्र स्मृती संच दर्शविणारे संभाषण वेगळेपणा चाचणी स्पष्टीकरण*

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

प्रत्येक संभाषणाची स्वतंत्र इतिहास ठेवली जातो. उत्पादन प्रणालींमध्ये हा वेगळेपणा बहु-वापरकर्ता अनुप्रयोगांसाठी अत्यंत महत्वाचा असतो.

### नमुना 4: साधने स्वतंत्र चाचणी

साधने AI कॉल करू शकणाऱ्या फंक्शन्स आहेत. ते थेट चाचणी करून खात्री करा की ते AI निर्णयांपासून स्वतंत्रपणे नीट कार्य करतात.

<img src="../../../translated_images/mr/tools-testing.3e1706817b0b3924.webp" alt="साधने चाचणी" width="800"/>

*AI कॉलशिवाय मॉक साधन कार्यान्वयन दर्शविणारे स्वतंत्र साधन चाचणी, व्यावसायिक लॉजिक तपासते*

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

ही चाचणी `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` मधून साधन लॉजिक AI शिवाय तपासते. चेनिंग उदाहरण दाखवते की एका साधनाचा आउटपुट दुसर्‍या साधनाच्या इनपुटमध्ये कसा जाईल.

### नमुना 5: इन-मेमरी RAG चाचणी

RAG प्रणाली पारंपरिकरीत्या व्हेक्टर डेटाबेस आणि एम्बेडिंग सेवा आवश्यक असतात. इन-मेमरी नमुना पूर्ण पाईपलाइन बाह्य अवलंबित्वांशिवाय टेस्ट करण्यास अनुमती देतो.

<img src="../../../translated_images/mr/rag-testing.ee7541b1e23934b1.webp" alt="इन-मेमरी RAG चाचणी" width="800"/>

*डॉक्युमेंट पार्सिंग, एम्बेडिंग स्टोरेज, आणि सादृश्य शोध दाखवणारा इन-मेमरी RAG चाचणी कार्यप्रवाह*

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

ही चाचणी `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` मध्ये आहे. डॉक्युमेंट इन-मेमरी तयार करते आणि चंकिंग व मेटाडेटा हँडलिंग पडताळते.

### नमुना 6: MCP इंटीग्रेशन चाचणी

MCP मॉड्यूल Model Context Protocol इंटीग्रेशन stdio ट्रान्सपोर्ट वापरते. ही चाचणी तपासते की तुमचा अनुप्रयोग MCP सर्व्हर सबप्रोसेस म्हणून उभारू आणि संवाद साधू शकतो.

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` मधील चाचण्या MCP क्लायंटचे वर्तन पडताळतात.

**चालवा:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## चाचणी तत्त्वज्ञान

तुमचा कोड चाचणी करा, AI नाही. तुमच्या चाचण्यांनी तुम्ही लिहिलेला कोड पडताळला पाहिजे, जसे प्रॉम्प्ट कसे तयार होतात, स्मृती कशी व्यवस्थापित होते, आणि साधने कशी कार्यान्वित होतात. AI प्रतिसाद वेगवेगळे असतात आणि चाचणी दावे भाग नसावे. स्वतःला विचारा, तुमचा प्रॉम्प्ट टेम्पलेट व्हेरीएबल योग्य पद्धतीने बदलतो का, AI बरोबर उत्तर देते की नाही नाही.

भाषा मॉडेलसाठी मॉक वापरा. ते बाह्य अवलंबित्वे आहेत जे धीमे, महागडे, आणि निश्चित नाहीत. मॉकिंग मुळे चाचण्या मिलीसेकंदांत वेगवान, विनामूल्य आणि कायमस्वरूपी परिणामांसह होतात.

चाचण्या स्वतंत्र ठेवा. प्रत्येक चाचणीने स्वतःची डेटा तयार करावी, इतर चाचण्यांवर अवलंबून राहू नये, आणि नंतर स्वच्छ करावे. चाचण्या कोणत्याही क्रमाने चालल्या तरी यशस्वी होतात.

आनंददायक मार्गाव्यतिरिक्त सीमांत परिस्थिती चाचणी करा. रिक्त इनपुट, अतिशय मोठे इनपुट, विशेष चिन्हे, अवैध पॅरामीटर्स आणि बाउंडरी कंडिशन्स तपासा. या अनेकदा अशा बग उघड करतात जे सामान्य वापर करताना दिसत नाहीत.

वर्णनात्मक नावे वापरा. `shouldMaintainConversationHistoryAcrossMultipleMessages()` ची तुलना `test1()` शी करा. पहिल्या नावाने तुम्हाला नक्की काय चाचणी केली जात आहे हे स्पष्ट सांगते, ज्यामुळे अपयशाचे निराकरण खूप सोपे होते.

## पुढील पाउले

चाचणी नमुने समजल्यावर प्रत्येक मॉड्यूलमध्ये सखोल जाता:

- **[00 - जलद प्रारंभ](../00-quick-start/README.md)** - प्रॉम्प्ट टेम्पलेट मूलभूत गोष्टींसह प्रारंभ करा
- **[01 - परिचय](../01-introduction/README.md)** - संभाषण स्मृती व्यवस्थापन शिका
- **[02 - प्रॉम्प्ट अभियांत्रिकी](../02-prompt-engineering/README.md)** - GPT-5.2 प्रॉम्प्टिंग नमुने आत्मसात करा
- **[03 - RAG](../03-rag/README.md)** - पुनर्प्राप्ती-आधारित जनरेशन सिस्टिम तयार करा
- **[04 - साधने](../04-tools/README.md)** - फंक्शन कॉलिंग आणि टूल चेन लागू करा
- **[05 - MCP](../05-mcp/README.md)** - मॉडेल संदर्भ प्रोटोकॉल इंटीग्रेट करा

प्रत्येक मॉड्यूलचे README येथे चाचणी केलेल्या संकल्पनांचे तपशीलवार स्पष्टीकरण देते.

---

**नेव्हिगेशन:** [← मुख्यांकडे परत](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:  
हा दस्तऐवज AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) वापरून भाषांतरित केला आहे. आम्ही अचूकतेसाठी प्रयत्नशील आहोत, तरी कृपया लक्षात ठेवा की स्वयंचलित अनुवादांमध्ये चूक किंवा अचूकतेची कमतरता असू शकते. मूळ दस्तऐवज त्याच्या स्थानिक भाषेत अधिकारप्राप्त स्रोत मानला जावा. महत्त्वपूर्ण माहितींसाठी व्यावसायिक मानवी अनुवादाची शिफारस केली जाते. या अनुवादाच्या वापरामुळे उद्भवणाऱ्या कोणत्याही गैरसमजुती किंवा चुकीच्या समजुतीबद्दल आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->