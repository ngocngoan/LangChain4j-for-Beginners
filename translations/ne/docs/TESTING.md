# LangChain4j अनुप्रयोगहरूको परीक्षण

## सामग्री तालिका

- [छिटो सुरु](../../../docs)
- [परीक्षाहरूले के कभर गर्छन्](../../../docs)
- [परीक्षाहरू चलाउने तरिका](../../../docs)
- [VS Code मा परीक्षणहरू चलाउने तरिका](../../../docs)
- [परीक्षणका ढाँचा](../../../docs)
- [परीक्षण दर्शनशास्त्र](../../../docs)
- [अर्को चरणहरू](../../../docs)

यो मार्गदर्शकले तपाईंलाई त्यस्ता परीक्षणहरूमा लैजान्छ जसले API कुञ्जीहरू वा बाह्य सेवाहरू बिना AI अनुप्रयोगहरू कसरी परीक्षण गर्ने देखाउँछन्।

## छिटो सुरु

एकै कमाण्डबाट सबै परीक्षणहरू चलाउनुहोस्:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/ne/test-results.ea5c98d8f3642043.webp" alt="सफल परीक्षण परिणामहरू" width="800"/>

*सफल परीक्षण कार्यान्वयन जसले सबै परीक्षणहरू पास भएका देखाउँछ, कुनै असफलता बिना*

## परीक्षणहरूले के कभर गर्छन्

यो कोर्सले **यूनिट परीक्षणहरू** मा ध्यान केन्द्रित गर्छ जुन स्थानीय रूपमा चल्छन्। प्रत्येक परीक्षणले LangChain4j को एउटा विशेष अवधारणा अलग्गै देखाउँछ।

<img src="../../../translated_images/ne/testing-pyramid.2dd1079a0481e53e.webp" alt="परीक्षण पिरामिड" width="800"/>

*परीक्षण पिरामिडले यूनिट परीक्षण (छिटो, अलग्गै), एकिकरण परीक्षण (वास्तविक कम्पोनेन्टहरू), र अन्त्य-देखि-अन्त्य परीक्षणहरूको सन्तुलन देखाउँछ। यो प्रशिक्षणले यूनिट परीक्षणलाई समेट्छ।*

| मोड्युल | परीक्षणहरू | ध्यान केन्द्रित | मुख्य फाइलहरू |
|--------|-------|-------|-----------|
| **00 - छिटो सुरु** | 6 | प्रॉम्प्ट टेम्प्लेटहरू र चल हुने मान प्रतिस्थापन | `SimpleQuickStartTest.java` |
| **01 - परिचय** | 8 | वार्तालाप स्मृति र स्टेटफुल च्याट | `SimpleConversationTest.java` |
| **02 - प्रॉम्प्ट इन्जिनियरिङ** | 12 | GPT-5.2 ढाँचा, उत्सुकता स्तरहरू, संरचित आउटपुट | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | कागजात अन्तर्भुक्ति, एम्बेडिङ्स, समानता खोज | `DocumentServiceTest.java` |
| **04 - उपकरणहरू** | 12 | फंक्शन कलिङ र उपकरण चेनिङ | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | मोडेल कन्टेक्स्ट प्रोटोकल संग Stdio ट्रान्सपोर्ट | `SimpleMcpTest.java` |

## परीक्षणहरू चलाउने तरिका

**रुटबाट सबै परीक्षणहरू चलाउनुहोस्:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**विशेष मोड्युलको परीक्षण चलाउनुहोस्:**

**Bash:**
```bash
cd 01-introduction && mvn test
# वा root बाट
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# वा मूल बाट
mvn --% test -pl 01-introduction
```

**एउटा परीक्षण कक्षा चलाउनुहोस्:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**विशिष्ट परीक्षण मेथड चलाउनुहोस्:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#कुराकानी इतिहास कायम राख्नुपर्छ
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#कुराकानी इतिहास कायम राख्नु पर्छ
```

## VS Code मा परीक्षणहरू चलाउने तरिका

यदि तपाईं Visual Studio Code प्रयोग गर्दै हुनुहुन्छ भने, Test Explorer ले परीक्षणहरू चलाउन र डिबग गर्न ग्राफिकल इन्टरफेस उपलब्ध गराउँछ।

<img src="../../../translated_images/ne/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer ले सबै Java परीक्षण कक्षाहरू र व्यक्तिगत परीक्षण मेथडहरू सहित परीक्षण वृक्ष देखाउँछ*

**VS Code मा परीक्षणहरू चलाउन:**

1. Activity Bar मा रहेको बेककर आइकनमा क्लिक गरी Test Explorer खोल्नुहोस्
2. सबै मोड्युलहरू र परीक्षण कक्षाहरू हेर्न परीक्षण वृक्ष विस्तृत पार्नुहोस्
3. कुनै पनि परीक्षणलाई व्यक्तिगत रूपमा चलाउन त्यसको छेउमा प्ले बटन क्लिक गर्नुहोस्
4. सम्पूर्ण सूट चलाउन "Run All Tests" क्लिक गर्नुहोस्
5. परीक्षणमा राइट-क्लिक गरी "Debug Test" छान्नुहोस् र ब्रेकपोइन्ट सेट गरेर कोडमा कदम चाल्नुहोस्

Test Explorer ले पास भएका परीक्षणहरूमा हरियो चेकमार्क देखाउँछ र विफल हुँदा विस्तृत त्रुटि सन्देशहरू प्रदान गर्दछ।

## परीक्षणका ढाँचा

### ढाँचा 1: प्रॉम्प्ट टेम्प्लेटहरूको परीक्षण

सबैभन्दा सरल ढाँचाले कुनै AI मोडल कल नगरी प्रॉम्प्ट टेम्प्लेटहरू परीक्षण गर्छ। तपाईंले चल हुने मान प्रतिस्थापना ठीक छ कि छैन र प्रॉम्प्टहरू अपेक्षित रूपमा फर्म्याट भए कि नभएको प्रमाणित गर्नुहुन्छ।

<img src="../../../translated_images/ne/prompt-template-testing.b902758ddccc8dee.webp" alt="प्रॉम्प्ट टेम्प्लेट परीक्षण" width="800"/>

*प्रॉम्प्ट टेम्प्लेट परीक्षण जसले चल हुने मान प्रतिस्थापन प्रवाह देखाउँछ: प्लेसहोल्डरहरूसँग टेम्प्लेट → लागु गरिएका मानहरू → फर्म्याट गरिएको आउटपुट परीक्षण*

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

यो परीक्षण `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java` मा छ।

**यसलाई चलाउनुहोस्:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#परीक्षणप्रॉम्प्टटेम्पलेटफर्म्याटिङ
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#परीक्षणप्रॉम्प्टटेम्प्लेटफर्म्याटिङ
```

### ढाँचा 2: भाषा मोडलहरूको मोकिङ

वार्तालाप तर्क परीक्षण गर्दा, Mockito प्रयोग गरेर निश्चित प्रतिक्रिया दिने नक्कली मोडलहरू बनाउनुहोस्। यसले परीक्षणहरू छिटो, निःशुल्क, र निर्धारीत बनाउँछ।

<img src="../../../translated_images/ne/mock-vs-real.3b8b1f85bfe6845e.webp" alt="मोक विरुद्ध वास्तविक API तुलना" width="800"/>

*किन मोकहरू परीक्षणका लागि प्राथमिकता पाइन्छ दर्शाउने तुलना: छिटो, निःशुल्क, निर्धारीत, र API कुञ्जी आवश्यक छैन*

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
        assertThat(history).hasSize(6); // 3 प्रयोगकर्ता + 3 AI सन्देशहरू
    }
}
```

यो ढाँचा `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` मा छ। मोकले एकरूप व्यवहार सुनिश्चित गर्छ जसले स्मृति व्यवस्थापन ठीकसँग काम गर्छ भनी प्रमाणित गर्न मद्दत गर्छ।

### ढाँचा 3: वार्तालाप पृथक्करण परीक्षण

वार्तालाप स्मृतिले धेरै प्रयोगकर्ताहरूलाई अलग राख्नुपर्छ। यो परीक्षणले वार्तालापहरू सन्दर्भहरू नमिक्स हुने दुरुत्साह नगर्ने प्रमाणित गर्छ।

<img src="../../../translated_images/ne/conversation-isolation.e00336cf8f7a3e3f.webp" alt="वार्तालाप पृथक्करण" width="800"/>

*वार्तालाप पृथक्करण परीक्षणले भिन्न प्रयोगकर्ताहरूको लागि अलग स्मृति भण्डारण देखाउँछ ताकि सन्दर्भ नमिक्स होस् नदिन*

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

प्रत्येक वार्तालापले आफ्नै स्वतन्त्र इतिहास राख्छ। उत्पादन प्रणालीहरूमा यो पृथक्करण बहु-प्रयोगकर्ता अनुप्रयोगहरूको लागि अत्यावश्यक छ।

### ढाँचा 4: उपकरणहरू स्वतन्त्र रूपमा परीक्षण

उपकरणहरू AI ले कल गर्न सक्ने फंक्शन हुन्। तिनीहरूलाई सिधा परीक्षण गर्नुहोस् ताकि AI निर्णय भन्दा पृथक् तिनीहरूले सही काम गर्छन् भनेर सुनिश्चित गर्न सकियोस्।

<img src="../../../translated_images/ne/tools-testing.3e1706817b0b3924.webp" alt="उपकरण परीक्षण" width="800"/>

*AI कल नगरी नकली उपकरण क्रियान्वयन देखाउँदै उपकरणहरू स्वतन्त्र रूपमा परीक्षण गरी व्यवसाय तर्क प्रमाणित*

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

यी परीक्षणहरू `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` बाट छन् जसले AI समावेश नगरी उपकरण तर्क समीक्षा गर्छ। चेनिङ उदाहरणले कसरी एउटा उपकरणको आउटपुट अर्कोको इनपुटमा जान्छ देखाउँछ।

### ढाँचा 5: इन-मेमोरी RAG परीक्षण

RAG प्रणालीहरूले सामान्यतया भेक्टर डाटाबेस र एम्बेडिङ सेवा आवश्यक पर्छ। इन-मेमोरी ढाँचाले तपाईंलाई बाह्य निर्भरता बिना सम्पूर्ण पाइपलाइन परीक्षण गर्न दिन्छ।

<img src="../../../translated_images/ne/rag-testing.ee7541b1e23934b1.webp" alt="इन-मेमोरी RAG परीक्षण" width="800"/>

*इन-मेमोरी RAG परीक्षण कार्यप्रवाह जसले कागजात पार्सिङ, एम्बेडिङ भण्डारण, र समानता खोज देखाउँछ, डाटाबेस आवश्यक बिना*

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

यो परीक्षण `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` बाट हो जसले मेमोरीमा कागजात बनाउँछ र चंकिङ तथा मेटाडाटा ह्यान्डलिङ परीक्षण गर्छ।

### ढाँचा 6: MCP एकीकरण परीक्षण

MCP मोड्युलले Model Context Protocol एकीकरण stdio ट्रान्सपोर्ट प्रयोग गरी परीक्षण गर्छ। यी परीक्षणहरूले तपाईंको अनुप्रयोगले MCP सर्भरहरू subprocess को रूपमा स्पन र सञ्चार गर्न सक्ने प्रमाणित गर्छ।

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` मा परीक्षणहरू छन्।

**चलाउनुहोस्:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## परीक्षण दर्शनशास्त्र

तपाईंको कोड परीक्षण गर्नुहोस्, AI होइन। तपाईंका परीक्षणहरूले कोडले कसरी प्रॉम्प्टहरू तयार गर्छ, स्मृति कसरी व्यवस्थापन गर्छ, र उपकरणहरू कसरी कार्यान्वयन हुन्छन् भनि जाँच गरेर लेखिएको कोड प्रमाणित गर्नुपर्छ। AI प्रतिक्रिया भिन्न हुन्छन् र परीक्षण दाबीहरूको हिस्सा हुनु हुँदैन। आफैंसँग प्रश्न सोध्नुहोस् - तपाईंको प्रॉम्प्ट टेम्प्लेटले चल हुने मानहरू सही प्रतिस्थापन गर्छ कि गर्दैन, AI ले सही उत्तर दिएको छ कि छैन होइन।

भाषा मोडलको लागि मोकहरू प्रयोग गर्नुहोस्। तिनीहरू बाह्य निर्भरता हुन् जुन सुस्त, महँगो, र निर्धारीत नभएका हुन्छन्। मोक गर्ने तरिका परीक्षणहरूलाई छिटो (सेकेन्डको सट्टा मिलिसेकेन्डमा), निःशुल्क (API खर्च बिना), र निर्धारीत बनाउँछ।

परीक्षणहरू स्वतन्त्र राख्नुहोस्। प्रत्येक परीक्षणले आफ्नो डाटा सेटअप गर्नुपर्छ, अरू परीक्षणमा निर्भर हुनु हुँदैन, र आफैंले सफा गर्नुपर्छ। परीक्षणहरू सञ्चालनको क्रम अनुसार पास हुनुपर्छ।

खुसी मार्ग बाहेकको किनाराका केसहरू परीक्षण गर्नुहोस्। खाली इनपुट, धेरै ठूलो इनपुट, विशेष अक्षरहरू, अमान्य प्यारामिटरहरू, र सिमानाको अवस्थाहरू प्रयास गर्नुहोस्। यी सामान्य प्रयोगले नदेखाउने बगहरू प्रायः पत्ता लगाउँछ।

वर्णनात्मक नामहरू प्रयोग गर्नुहोस्। `shouldMaintainConversationHistoryAcrossMultipleMessages()` लाई `test1()` सँग तुलना गर्नुहोस्। पहिलोले के परीक्षण भइरहेको छ ठ्याक्कै बताउँछ, जसले असफलता डिबग गर्न सजिलो बनाउँछ।

## अर्को चरणहरू

अब तपाईंले परीक्षण ढाँचाहरू बुझ्नुभएपछि, प्रत्येक मोड्युलमा गहिराइमा जानुहोस्:

- **[00 - छिटो सुरु](../00-quick-start/README.md)** - प्रॉम्प्ट टेम्प्लेटको आधारभूत कुरा सिक्नुहोस्
- **[01 - परिचय](../01-introduction/README.md)** - वार्तालाप स्मृति व्यवस्थापन सिक्नुहोस्
- **[02 - प्रॉम्प्ट इन्जिनियरिङ](../02-prompt-engineering/README.md)** - GPT-5.2 प्रॉम्प्टिङ ढाँचाहरूमा महारत हासिल गर्नुहोस्
- **[03 - RAG](../03-rag/README.md)** - पुनःप्राप्ति-वृद्धि प्रणालीहरू निर्माण गर्नुहोस्
- **[04 - उपकरणहरू](../04-tools/README.md)** - फंक्शन कलिङ र उपकरण चेनहरू लागू गर्नुहोस्
- **[05 - MCP](../05-mcp/README.md)** - Model Context Protocol एकीकरण गर्नुहोस्

हरेक मोड्युलको README मा यहाँ परीक्षण गरिएका अवधारणाहरूका विस्तृत व्याख्या उपलब्ध छन्।

---

**नेभिगेसन:** [← मुख्य पृष्ठमा फर्कनुहोस्](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यस दस्तावेजलाई AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरी अनुवाद गरिएको हो। हामी सही अनुवादको लागि प्रयासरत छौं भने पनि, कृपया ध्यान दिनुहोस् कि स्वतः अनुवादमा त्रुटि वा अशुद्धता हुनसक्छ। मूल दस्तावेजको मूल भाषा नै आधिकारिक स्रोत मानिनुपर्छ। महत्वपूर्ण जानकारीको लागि, व्यावसायिक मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न कुनै पनि गलत बुझाइ वा गलत व्याख्याको लागि हामी उत्तरदायी छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->