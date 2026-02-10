# LangChain4j अनुप्रयोगों का परीक्षण

## सामग्री सूची

- [त्वरित आरंभ](../../../docs)
- [परीक्षण क्या कवर करते हैं](../../../docs)
- [परीक्षण चलाना](../../../docs)
- [VS कोड में परीक्षण चलाना](../../../docs)
- [परीक्षण पैटर्न](../../../docs)
- [परीक्षण दर्शन](../../../docs)
- [अगले कदम](../../../docs)

यह गाइड आपको उन परीक्षणों के माध्यम से ले जाता है जो दिखाते हैं कि कैसे API कुंजियाँ या बाहरी सेवाओं की आवश्यकता के बिना AI अनुप्रयोगों का परीक्षण किया जा सकता है।

## त्वरित आरंभ

सभी परीक्षण एक ही कमांड से चलाएँ:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/hi/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*सफलतापूर्वक परीक्षण निष्पादन दिखाते हुए जहाँ सभी परीक्षण बिना कोई विफलता के पास हैं*

## परीक्षण क्या कवर करते हैं

यह कोर्स **यूनिट परीक्षणों** पर केंद्रित है जो स्थानीय रूप से चलते हैं। प्रत्येक परीक्षण एक विशिष्ट LangChain4j अवधारणा को अलगाव में प्रदर्शित करता है।

<img src="../../../translated_images/hi/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*परीक्षण पिरामिड जो यूनिट परीक्षण (तेज़, अलग-थलग), एकीकरण परीक्षण (वास्तविक घटक), और एंड-टू-एंड परीक्षण के बीच संतुलन दिखाता है। यह प्रशिक्षण यूनिट परीक्षण को कवर करता है।*

| मॉड्यूल | परीक्षण | फोकस | मुख्य फाइलें |
|--------|-------|-------|-----------|
| **00 - त्वरित आरंभ** | 6 | प्रॉम्प्ट टेम्पलेट और वेरिएबल प्रतिस्थापन | `SimpleQuickStartTest.java` |
| **01 - परिचय** | 8 | संवाद स्मृति और स्टेटफुल चैट | `SimpleConversationTest.java` |
| **02 - प्रॉम्प्ट इंजीनियरिंग** | 12 | GPT-5.2 पैटर्न, उत्सुकता स्तर, संरचित आउटपुट | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | दस्तावेज़ इनटेक, एम्बेडिंग, समानता खोज | `DocumentServiceTest.java` |
| **04 - टूल्स** | 12 | फ़ंक्शन कॉलिंग और टूल चेनिंग | `SimpleToolsTest.java`` |
| **05 - MCP** | 8 | मॉडल कॉन्टेक्स्ट प्रोटोकॉल स्टडिओ ट्रांसपोर्ट के साथ | `SimpleMcpTest.java` |

## परीक्षण चलाना

**रूट से सभी परीक्षण चलाएँ:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**विशेष मॉड्यूल के लिए परीक्षण चलाएँ:**

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

**एकल टेस्ट क्लास चलाएँ:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**विशेष टेस्ट मेथड चलाएँ:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#वार्तालाप इतिहास बनाए रखना चाहिए
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#वार्तालाप इतिहास बनाए रखना चाहिए
```

## VS कोड में परीक्षण चलाना

यदि आप Visual Studio Code का उपयोग कर रहे हैं, तो टेस्ट एक्सप्लोरर परीक्षणों को चलाने और डीबग करने के लिए एक ग्राफिकल इंटरफ़ेस प्रदान करता है।

<img src="../../../translated_images/hi/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS कोड टेस्ट एक्सप्लोरर जो सभी जावा टेस्ट क्लास और व्यक्तिगत टेस्ट मेथड के साथ टेस्ट ट्री दिखाता है*

**VS कोड में परीक्षण चलाने के लिए:**

1. एक्टिविटी बार में बीकर आइकन पर क्लिक करके टेस्ट एक्सप्लोरर खोलें
2. सभी मॉड्यूल और टेस्ट क्लास को देखने के लिए टेस्ट ट्री को एक्सपैंड करें
3. किसी भी टेस्ट के पास प्ले बटन पर क्लिक करें इसे व्यक्तिगत रूप से चलाने के लिए
4. पूरा सूट चलाने के लिए "Run All Tests" पर क्लिक करें
5. ब्रेकपॉइंट सेट करने और कोड में डिबगिंग के लिए किसी भी टेस्ट पर राइट-क्लिक करें और "Debug Test" चुनें

टेस्ट एक्सप्लोरर पास होने वाले परीक्षणों के लिए हरे चेकमार्क दिखाता है और विफल होने पर विस्तृत असफलता संदेश प्रदान करता है।

## परीक्षण पैटर्न

### पैटर्न 1: प्रॉम्प्ट टेम्पलेट परीक्षण

सबसे सरल पैटर्न AI मॉडल को कॉल किए बिना प्रॉम्प्ट टेम्पलेट का परीक्षण करता है। आप यह सत्यापित करते हैं कि वेरिएबल प्रतिस्थापन सही तरीके से काम करता है और प्रॉम्प्ट अपेक्षित रूप में स्वरूपित है।

<img src="../../../translated_images/hi/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*प्रॉम्प्ट टेम्पलेट परीक्षण दिखाते हुए वेरिएबल प्रतिस्थापन प्रवाह: प्लेसहोल्डर के साथ टेम्पलेट → लागू किए गए मान → स्वरूपित आउटपुट सत्यापित*

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

यह परीक्षण `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java` में है।

**इसे चलाएँ:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#टेस्टप्रॉम्प्टटेम्पलेटफॉर्मेटिंग
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#परीक्षणप्रॉम्प्टटेम्प्लेटफॉर्मैटिंग
```

### पैटर्न 2: भाषा मॉडलों का मॉकिंग

संवाद तर्क परीक्षण करते समय, Mockito का उपयोग करके नकली मॉडल बनाएं जो पूर्व निर्धारित प्रतिक्रियाएँ लौटाते हैं। इससे परीक्षण तेज़, मुफ्त और निश्चित होते हैं।

<img src="../../../translated_images/hi/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*तुलना दिखाती है कि परीक्षण के लिए मॉक क्यों बेहतर हैं: ये तेज़, मुफ्त, निश्चित हैं और API कुंजियों की आवश्यकता नहीं है*

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

यह पैटर्न `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` में दिखता है। मॉक सही व्यवहार सुनिश्चित करता है ताकि आप सत्यापित कर सकें कि मेमोरी प्रबंधन सही है।

### पैटर्न 3: संवाद अलगाव परीक्षण

संवाद स्मृति को कई उपयोगकर्ताओं को अलग रखना चाहिए। यह परीक्षण सत्यापित करता है कि संवाद परिप्रेक्ष्य नहीं मिलाते।

<img src="../../../translated_images/hi/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*संवाद अलगाव परीक्षण जिसमें अलग-अलग उपयोगकर्ताओं के लिए अलग-अलग स्मृति स्टोर दिखाए गए हैं ताकि संदर्भ मिश्रण न हो*

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

प्रत्येक संवाद अपनी स्वतंत्र इतिहास बनाए रखता है। उत्पादन सिस्टम में, यह अलगाव बहु-उपयोगकर्ता अनुप्रयोगों के लिए महत्वपूर्ण है।

### पैटर्न 4: टूल्स का स्वतंत्र परीक्षण

टूल्स वे फ़ंक्शन हैं जिन्हें AI कॉल कर सकता है। इन्हें सीधे परीक्षण करें ताकि यह सुनिश्चित हो सके कि वे AI निर्णयों की परवाह किए बिना सही काम करते हैं।

<img src="../../../translated_images/hi/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*स्वतंत्र रूप से टूल्स का परीक्षण करते हुए दिखाते हुए कि मॉक टूल निष्पादन बिना AI कॉल के व्यावसायिक तर्क को सत्यापित करता है*

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

ये परीक्षण `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` से हैं जो AI भागीदारी के बिना टूल तर्क को मान्य करते हैं। चेनिंग उदाहरण में दिखाया गया है कि एक टूल का आउटपुट दूसरे के इनपुट में कैसे जाता है।

### पैटर्न 5: इन-मेमोरी RAG परीक्षण

RAG सिस्टम आमतौर पर वेक्टर डेटाबेस और एम्बेडिंग सेवाओं की मांग करते हैं। इन-मेमोरी पैटर्न आपको बाहरी निर्भरताओं के बिना पूरे पाइपलाइन का परीक्षण करने देता है।

<img src="../../../translated_images/hi/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*इन-मेमोरी RAG परीक्षण कार्यप्रवाह जिसमें दस्तावेज़ पार्सिंग, एम्बेडिंग संग्रहण, और समानता खोज बिना डेटाबेस की जरूरत दिखा रहा है*

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

यह परीक्षण `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` से है जो इन-मेमोरी दस्तावेज़ बनाता है और चंकिन्ग तथा मेटाडाटा हैंडलिंग सत्यापित करता है।

### पैटर्न 6: MCP इंटीग्रेशन परीक्षण

MCP मॉड्यूल मॉडल कॉन्टेक्स्ट प्रोटोकॉल इंटीग्रेशन का परीक्षण करता है स्टडिओ ट्रांसपोर्ट का उपयोग करते हुए। ये परीक्षण आपके अनुप्रयोग को MCP सर्वर subprocess के रूप में स्पॉन और संवाद करने में सक्षम बनाते हैं।

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` में टेस्ट MCP क्लाइंट व्यवहार को वैध करते हैं।

**इन्हें चलाएँ:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## परीक्षण दर्शन

अपने कोड का परीक्षण करें, AI का नहीं। आपके परीक्षण उस कोड को मान्य करना चाहिए जो आप लिखते हैं यह जांचकर कि प्रॉम्प्ट कैसे बनते हैं, मेमोरी कैसे प्रबंधित होती है, और टूल्स कैसे निष्पादित होते हैं। AI प्रतिक्रियाएँ भिन्न हो सकती हैं और उन्हें परीक्षण दावे का हिस्सा नहीं होना चाहिए। अपने आप से पूछें कि क्या आपका प्रॉम्प्ट टेम्पलेट सही तरीके से वेरिएबल प्रतिस्थापित करता है, न कि कि AI सही उत्तर देता है।

भाषा मॉडलों के लिए मॉक का उपयोग करें। ये बाहरी निर्भरता हैं जो धीमे, महंगे, और गैर-निश्चित होते हैं। मॉकिंग परीक्षणों को मिलीसेकंड में तेज़, बिना API लागत के मुफ्त, और हर बार समान परिणाम के लिए निश्चित बनाता है।

परीक्षण स्वतंत्र रखें। प्रत्येक परीक्षण को अपनी खुद की डेटा सेटअप करनी चाहिए, अन्य परीक्षणों पर निर्भर नहीं होना चाहिए, और स्वयं की सफाई करनी चाहिए। परीक्षण निष्पादन क्रम से स्वतंत्र रूप से पास होने चाहिए।

सुखद मार्ग से परे किनारे के मामले परीक्षण करें। खाली इनपुट, बहुत बड़े इनपुट, विशेष वर्ण, अमान्य पैरामीटर, और सीमा स्थितियां आज़माएं। ये अक्सर बग प्रकट करते हैं जो सामान्य उपयोग में नहीं दिखते।

वर्णनात्मक नामों का उपयोग करें। `shouldMaintainConversationHistoryAcrossMultipleMessages()` की तुलना `test1()` से करें। पहला आपको बताता है कि क्या परीक्षण किया जा रहा है, जिससे विफलताओं का डीबगिंग आसान होता है।

## अगले कदम

अब जब आप परीक्षण पैटर्न समझ गए हैं, तो प्रत्येक मॉड्यूल में गहराई से जाएँ:

- **[00 - त्वरित आरंभ](../00-quick-start/README.md)** - प्रॉम्प्ट टेम्पलेट बुनियादी बातें सीखें
- **[01 - परिचय](../01-introduction/README.md)** - संवाद स्मृति प्रबंधन सीखें
- **[02 - प्रॉम्प्ट इंजीनियरिंग](../02-prompt-engineering/README.md)** - GPT-5.2 प्रॉम्प्टिंग पैटर्न मास्टर करें
- **[03 - RAG](../03-rag/README.md)** - पुनः प्राप्ति-संवर्धित उत्पादन सिस्टम बनाएं
- **[04 - टूल्स](../04-tools/README.md)** - फ़ंक्शन कॉलिंग और टूल चेन लागू करें
- **[05 - MCP](../05-mcp/README.md)** - मॉडल कॉन्टेक्स्ट प्रोटोकॉल एकीकृत करें

प्रत्येक मॉड्यूल का README यहाँ परीक्षण की गई अवधारणाओं का विस्तृत विवरण प्रदान करता है।

---

**नेविगेशन:** [← मुख्य पृष्ठ पर वापस](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यह दस्तावेज़ एआई अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके अनुवादित किया गया है। जबकि हम सटीकता के लिए प्रयासरत हैं, कृपया ध्यान दें कि स्वचालित अनुवाद में त्रुटियां या अशुद्धियां हो सकती हैं। मूल दस्तावेज़ अपनी मूल भाषा में प्राधिकृत स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए, पेशेवर मानव अनुवाद की अनुशंसा की जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम ज़िम्मेदार नहीं हैं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->