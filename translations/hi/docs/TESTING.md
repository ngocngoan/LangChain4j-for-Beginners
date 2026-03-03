# LangChain4j अनुप्रयोगों का परीक्षण

## विषय सूची

- [त्वरित प्रारंभ](../../../docs)
- [परीक्षण क्या कवर करते हैं](../../../docs)
- [परीक्षण चलाना](../../../docs)
- [VS Code में परीक्षण चलाना](../../../docs)
- [परीक्षण पैटर्न](../../../docs)
- [परीक्षण दर्शन](../../../docs)
- [अगले कदम](../../../docs)

यह गाइड आपको उन परीक्षणों के माध्यम से ले जाता है जो दिखाते हैं कि API कुंजियों या बाहरी सेवाओं की आवश्यकता के बिना AI अनुप्रयोगों का परीक्षण कैसे किया जाए।

## त्वरित प्रारंभ

सभी परीक्षण एक कमांड के साथ चलाएं:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

जब सभी परीक्षण सफल हो जाते हैं, तो आपको नीचे दिए गए स्क्रीनशॉट के समान आउटपुट दिखाई देगा — बिना गलती के परीक्षण चलते हैं।

<img src="../../../translated_images/hi/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*सफल परीक्षण निष्पादन जो दिखाता है कि सभी परीक्षण बिना किसी असफलता के पास हो गए हैं*

## परीक्षण क्या कवर करते हैं

यह कोर्स **यूनिट टेस्ट** पर केंद्रित है जो स्थानीय रूप से चलते हैं। प्रत्येक परीक्षण एक विशिष्ट LangChain4j अवधारणा को अलग से प्रदर्शित करता है। नीचे दिया गया परीक्षण पिरामिड दिखाता है कि यूनिट टेस्ट कहाँ फिट होते हैं — वे तेज़, भरोसेमंद आधार बनाते हैं जिस पर आपकी बाकी परीक्षण रणनीति आधारित होती है।

<img src="../../../translated_images/hi/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*परीक्षण पिरामिड जो यूनिट टेस्ट (तेज, अलग), इंटीग्रेशन टेस्ट (वास्तविक घटक), और एंड-टू-एंड टेस्ट के बीच संतुलन दिखाता है। यह प्रशिक्षण यूनिट परीक्षण को कवर करता है।*

| मॉड्यूल | परीक्षण | ध्यान केंद्रित | मुख्य फाइलें |
|--------|-------|-------|-----------|
| **00 - त्वरित प्रारंभ** | 6 | प्रॉम्प्ट टेम्पलेट और वेरिएबल प्रतिस्थापन | `SimpleQuickStartTest.java` |
| **01 - परिचय** | 8 | संवाद स्मृति और Stateful चैट | `SimpleConversationTest.java` |
| **02 - प्रॉम्प्ट इंजीनियरिंग** | 12 | GPT-5.2 पैटर्न, उत्सुकता स्तर, संरचित आउटपुट | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | दस्तावेज़ इनटेक, एम्बेडिंग, समानता खोज | `DocumentServiceTest.java` |
| **04 - टूल्स** | 12 | फ़ंक्शन कॉलिंग और टूल चेनिंग | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | मॉडल कंटेक्स्ट प्रोटोकॉल विद Stdio ट्रांसपोर्ट | `SimpleMcpTest.java` |

## परीक्षण चलाना

**रूट से सभी परीक्षण चलाएं:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**किसी विशेष मॉड्यूल के लिए परीक्षण चलाएं:**

**Bash:**
```bash
cd 01-introduction && mvn test
# या रूट से
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# या root से
mvn --% test -pl 01-introduction
```

**एकल परीक्षण क्लास चलाएं:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**विशिष्ट परीक्षण मेथड चलाएं:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#वार्तालाप इतिहास को बनाए रखना चाहिए
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#वार्तालाप इतिहास बनाए रखना चाहिए
```

## VS Code में परीक्षण चलाना

यदि आप Visual Studio Code का उपयोग कर रहे हैं, तो टेस्ट एक्सप्लोरर परीक्षण चलाने और डीबग करने के लिए एक ग्राफिकल इंटरफ़ेस प्रदान करता है।

<img src="../../../translated_images/hi/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code टेस्ट एक्सप्लोरर जो सभी जावा परीक्षण क्लास और व्यक्तिगत परीक्षण विधियों के साथ परीक्षण ट्री को दिखाता है*

**VS Code में परीक्षण चलाने के लिए:**

1. एक्टिविटी बार में बीकर आइकन पर क्लिक करके टेस्ट एक्सप्लोरर खोलें
2. सभी मॉड्यूल और परीक्षण क्लास देखने के लिए परीक्षण ट्री का विस्तार करें
3. किसी भी परीक्षण के बगल में प्ले बटन पर क्लिक करके उसे अकेले चलाएं
4. पूरी सूट चलाने के लिए "Run All Tests" पर क्लिक करें
5. किसी भी परीक्षण पर राइट-क्लिक करके "Debug Test" चुनें ताकि ब्रेकपॉइंट सेट कर सकें और कोड स्टेप थ्रू कर सकें

परीक्षण पास होने पर टेस्ट एक्सप्लोरर हरे चेकमार्क दिखाता है और असफल होने पर विस्तृत त्रुटि संदेश प्रदान करता है।

## परीक्षण पैटर्न

### पैटर्न 1: प्रॉम्प्ट टेम्पलेट का परीक्षण

सबसे सरल पैटर्न AI मॉडल को कॉल किए बिना प्रॉम्प्ट टेम्पलेट का परीक्षण करता है। आप जांचते हैं कि वेरिएबल प्रतिस्थापन सही तरीके से काम करता है और प्रॉम्प्ट अपेक्षित रूप में स्वरूपित है।

<img src="../../../translated_images/hi/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*प्रॉम्प्ट टेम्पलेट परीक्षण दिखाता है वेरिएबल प्रतिस्थापन फ्लो: प्लेसहोल्डर के साथ टेम्पलेट → मान लागू → स्वरूपित आउटपुट सत्यापित*

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

यह परीक्षण `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java` में मौजूद है।

**इसे चलाएं:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#परीक्षणप्रॉम्प्टटेम्पलेटफॉर्मैटिंग
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#परीक्षणप्रांप्टटेम्पलेटस्वरूपण
```

### पैटर्न 2: भाषा मॉडलों का मॉकिंग

जब आप संवाद लॉजिक का परीक्षण करते हैं, तो Mockito का उपयोग करके नकली मॉडल बनाएं जो पूर्वनिर्धारित प्रतिक्रियाएं लौटाते हैं। इससे परीक्षण तेज़, मुफ्त और निर्धारक होते हैं।

<img src="../../../translated_images/hi/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*तुलना दिखाती है कि परीक्षण के लिए मॉक क्यों बेहतर हैं: वे तेज़, मुफ्त, निर्धारक हैं और API कुंजी की आवश्यकता नहीं होती*

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
        assertThat(history).hasSize(6); // 3 उपयोगकर्ता + 3 एआई संदेश
    }
}
```

यह पैटर्न `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` में मौजूद है। मॉक सुनिश्चित करता है कि व्यवहार सुसंगत रहे ताकि आप मेमोरी प्रबंधन सही ढंग से होता है यह सत्यापित कर सकें।

### पैटर्न 3: संवाद पृथक्करण का परीक्षण

संवाद स्मृति को कई उपयोगकर्ताओं को अलग रखना चाहिए। यह परीक्षण सत्यापित करता है कि संवाद संदर्भों को मिलाते नहीं हैं।

<img src="../../../translated_images/hi/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*संवाद पृथक्करण परीक्षण जो अलग-अलग उपयोगकर्ताओं के लिए अलग-अलग मेमोरी स्टोर दिखाता है ताकि संदर्भ मिश्रण न हो*

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

प्रत्येक संवाद अपनी स्वतंत्र हिस्ट्री बनाए रखता है। उत्पादन प्रणालियों में, यह पृथक्करण मल्टी-यूजर अनुप्रयोगों के लिए महत्वपूर्ण है।

### पैटर्न 4: टूल्स को स्वतंत्र रूप से परीक्षण करना

टूल वे फंक्शन हैं जिन्हें AI कॉल कर सकता है। उन्हें सीधे परीक्षण करें ताकि यह सुनिश्चित हो कि वे AI निर्णयों से स्वतंत्र रूप से सही काम करते हैं।

<img src="../../../translated_images/hi/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*टूल परीक्षण जो मॉक टूल के निष्पादन को बिना AI कॉल के दिखाता है ताकि बिजनेस लॉजिक सत्यापित किया जा सके*

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

ये परीक्षण `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` से हैं जो AI शामिल किए बिना टूल लॉजिक की पुष्टि करते हैं। चेनिंग उदाहरण दिखाता है कि कैसे एक टूल का आउटपुट दूसरे के इनपुट में जाता है।

### पैटर्न 5: इन-मेमोरी RAG परीक्षण

RAG सिस्टम आमतौर पर वेक्टर डेटाबेस और एम्बेडिंग सेवाओं की आवश्यकता होते हैं। इन-मेमोरी पैटर्न आपको पूरी पाइपलाइन बाहरी आश्रयों के बिना परीक्षण करने देता है।

<img src="../../../translated_images/hi/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*इन-मेमोरी RAG परीक्षण वर्कफ़्लो जो दस्तावेज़ पार्सिंग, एम्बेडिंग संग्रहण, और समानता खोज दिखाता है बिना डेटाबेस की आवश्यकता के*

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

यह परीक्षण `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` में है जो मेमोरी में एक दस्तावेज़ बनाता है और चंकिन्ग तथा मेटाडेटा हैंडलिंग सत्यापित करता है।

### पैटर्न 6: MCP इंटीग्रेशन परीक्षण

MCP मॉड्यूल मॉडल कंटेक्स्ट प्रोटोकॉल एकीकरण को stdio ट्रांसपोर्ट के माध्यम से परीक्षण करता है। ये परीक्षण सत्यापित करते हैं कि आपका ऐप MCP सर्वर को सक्सप्रोसेस के रूप में लॉन्च और संचार कर सकता है।

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` में परीक्षण MCP क्लाइंट व्यवहार की पुष्टि करते हैं।

**इन्हें चलाएं:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## परीक्षण दर्शन

अपने कोड का परीक्षण करें, AI का नहीं। आपके परीक्षण कोड की जांच करनी चाहिए कि प्रॉम्प्ट कैसे बनाए गए हैं, मेमोरी कैसे प्रबंधित होती है, और टूल कैसे निष्पादित होते हैं। AI प्रतिक्रियाएँ भिन्न हो सकती हैं और उन्हें परीक्षण दावों में शामिल नहीं किया जाना चाहिए। खुद से पूछें कि क्या आपका प्रॉम्प्ट टेम्पलेट वेरिएबल सही ढंग से प्रतिस्थापित करता है, न कि AI सही उत्तर देता है या नहीं।

भाषा मॉडलों के लिए मॉक का उपयोग करें। वे बाहरी निर्भरता हैं जो धीमे, महंगे, और गैर-निर्धारक होते हैं। मॉकिंग परीक्षण को तेजी से बनाता है (मिलीसेकंड में बजाय सेकंड के), मुफ्त API लागत से मुक्त, और हर बार समान परिणाम के साथ निर्धारक।

परीक्षण स्वतंत्र रखें। प्रत्येक परीक्षण को अपने डेटा को सेटअप करना चाहिए, अन्य परीक्षणों पर निर्भर नहीं होना चाहिए, और खुद को साफ करना चाहिए। परीक्षण निष्पादन क्रम से स्वतंत्र रूप से पास होना चाहिए।

सुखद मार्ग से परे किनारे के मामले भी टेस्ट करें। खाली इनपुट, बहुत बड़े इनपुट, विशेष अक्षर, अवैध पैरामीटर, और सीमा स्थितियां आज़माएं। ये अक्सर बग उजागर करते हैं जिन्हें सामान्य उपयोग नहीं दिखाता।

वर्णनात्मक नामों का उपयोग करें। `shouldMaintainConversationHistoryAcrossMultipleMessages()` की तुलना `test1()` से करें। पहला स्पष्ट रूप से बताता है कि क्या परीक्षण किया जा रहा है, जिससे डिबगिंग में आसानी होती है।

## अगले कदम

अब जब आप परीक्षण पैटर्न समझ चुके हैं, तो प्रत्येक मॉड्यूल में गहराई से जाएं:

- **[00 - त्वरित प्रारंभ](../00-quick-start/README.md)** - प्रॉम्प्ट टेम्पलेट के मूल से शुरुआत करें
- **[01 - परिचय](../01-introduction/README.md)** - संवाद स्मृति प्रबंधन सीखें
- **[02 - प्रॉम्प्ट इंजीनियरिंग](../02-prompt-engineering/README.md)** - GPT-5.2 प्रॉम्प्टिंग पैटर्न मास्टर करें
- **[03 - RAG](../03-rag/README.md)** - रिट्रीवल-ऑगमेंटेड जनरेशन सिस्टम बनाएं
- **[04 - टूल्स](../04-tools/README.md)** - फ़ंक्शन कॉलिंग और टूल चेन लागू करें
- **[05 - MCP](../05-mcp/README.md)** - मॉडल कंटेक्स्ट प्रोटोकॉल को एकीकृत करें

प्रत्येक मॉड्यूल की README यहाँ परीक्षण किए गए अवधारणाओं के विस्तृत स्पष्टीकरण प्रदान करती है।

---

**नेविगेशन:** [← मुख्य पृष्ठ पर वापस](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:  
इस दस्तावेज़ का अनुवाद एआई अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके किया गया है। हालांकि हम सटीकता के लिए प्रयासरत हैं, कृपया ध्यान दें कि स्वचालित अनुवाद में त्रुटियाँ या असंगतियाँ हो सकती हैं। मूल दस्तावेज़ अपनी मूल भाषा में प्रामाणिक स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए, पेशेवर मानव अनुवाद की सलाह दी जाती है। इस अनुवाद के उपयोग से होने वाली किसी भी गलतफहमी या गलत व्याख्या की जिम्मेदारी हम पर नहीं होगी।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->