# LangChain4j अनुप्रयोगहरू परीक्षण

## सामग्री तालिका

- [छिटो सुरु](../../../docs)
- [परीक्षणहरूले के समेट्छन्](../../../docs)
- [परीक्षणहरू चलाउने](../../../docs)
- [VS कोडमा परीक्षणहरू चलाउने](../../../docs)
- [परीक्षण ढाँचा](../../../docs)
- [परीक्षण दर्शनशास्त्र](../../../docs)
- [अगाडि के गर्ने](../../../docs)

यो मार्गनिर्देशनले तपाईंलाई ती परीक्षणहरू मार्फत लैजान्छ जसले देखाउँछन् कसरी API कुञ्जीहरू वा बाह्य सेवाहरू बिना AI अनुप्रयोगहरू परीक्षण गर्ने।

## छिटो सुरु

एकै कमाण्डले सबै परीक्षणहरू चलाउनुहोस्:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

सबै परीक्षणहरू पास भएपछि, तलको स्क्रिनशट जस्तो आउटपुट देख्नुपर्नेछ — परीक्षणहरू बिना कुनै फेलियर चल्दछन्।

<img src="../../../translated_images/ne/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*सफल परीक्षण कार्यान्वयन देखाउँदै सबै परीक्षणहरू शून्य असफलतासँग पास भएका छन्*

## परीक्षणहरूले के समेट्छन्

यो कोर्षले **स्थानिय रूपमा चल्ने एकाई परीक्षणहरू** मा ध्यान केन्द्रित गर्दछ। प्रत्येक परीक्षणले एक विशिष्ट LangChain4j अवधारणा अलग्गै देखाउँछ। परीक्षण पिरामिडले देखाउँछ कहाँ एकाई परीक्षणहरू फिट हुन्छन् — ती छिटो, भरपर्दो आधार बनाउँछन् जुन तपाईको परीक्षण रणनीतिमा बाँकी सबैले निर्माण गर्छ।

<img src="../../../translated_images/ne/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*परीक्षण पिरामिड जसले एकाई परीक्षणहरू (छिटो, अलग्गै), एकीकरण परीक्षणहरू (वास्तविक कम्पोनेन्टहरू), र अन्त्य-देखि-अन्त्य परीक्षणहरू बीचको सन्तुलन देखाउँछ। यो तालिमले एकाई परीक्षणलाई समेट्छ।*

| मोड्युल | परीक्षणहरू | केन्द्रित विषय | मुख्य फाइलहरू |
|--------|-------|-------|-----------|
| **00 - छिटो सुरु** | 6 | प्रॉम्प्ट टेम्प्लेट र भेरिएबल प्रतिस्थापन | `SimpleQuickStartTest.java` |
| **01 - परिचय** | 8 | संवाद मेमोरी र अवस्थात्मक च्याट | `SimpleConversationTest.java` |
| **02 - प्रॉम्प्ट इन्जिनियरिङ** | 12 | GPT-5.2 ढाँचाहरू, तत्परता स्तरहरू, संरचित आउटपुट | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | कागजात समावेशन, एम्बेडिङ, समानता खोज | `DocumentServiceTest.java` |
| **04 - उपकरणहरू** | 12 | फङ्क्शन कलिङ र उपकरण श्रृंखला | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | मोडेल कन्टेक्स्ट प्रोटोकल stdio ट्रान्सपोर्टसहित | `SimpleMcpTest.java` |

## परीक्षणहरू चलाउने

**रुटबाट सबै परीक्षणहरू चलाउनुहोस्:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**विशिष्ट मोड्युलका लागि परीक्षणहरू चलाउने:**

**Bash:**
```bash
cd 01-introduction && mvn test
# वा रुटबाट
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# वा रुटबाट
mvn --% test -pl 01-introduction
```

**एकल परीक्षण क्लास चलाउने:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**विशिष्ट परीक्षण मेथड चलाउने:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#कुराकानी इतिहास राख्नुपर्छ
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#कुराकानी इतिहास राख्नुपर्छ
```

## VS कोडमा परीक्षणहरू चलाउने

यदि तपाईं Visual Studio Code प्रयोग गर्दै हुनुहुन्छ भने, टेस्ट एक्सप्लोररले परीक्षणहरू चलाउन र डीबग गर्न ग्राफिकल इन्टरफेस प्रदान गर्छ।

<img src="../../../translated_images/ne/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS कोड टेस्ट एक्सप्लोररले सबै Java परीक्षण क्लासहरू र व्यक्तिगत परीक्षण मेथडहरूको परीक्षण रूख देखाउँछ*

**VS कोडमा परीक्षणहरू चलाउन:**

1. एक्टिभिटी बारमा बीकर आइकनमा क्लिक गरेर टेस्ट एक्सप्लोरर खोल्नुहोस्
2. सबै मोड्युलहरू र परीक्षण क्लासहरू हेर्न रूख विस्तार गर्नुहोस्
3. व्यक्तिगत परीक्षण चलाउन कुनै पनि परीक्षणको छेउमा प्ले बटन क्लिक गर्नुहोस्
4. पूरै सेटप चलाउन "Run All Tests" मा क्लिक गर्नुहोस्
5. कुनै पनि परीक्षणमा राइट-क्लिक गरेर "Debug Test" छान्नुहोस् ब्रेकपोइन्ट सेट गर्न र कोडमा स्टेप गर्न

टेस्ट एक्सप्लोररले पास भएका परीक्षणहरूका लागि हरियो टिक देखाउँछ र फेल हुनेबित्तिकै विस्तृत त्रुटि सन्देश प्रदान गर्छ।

## परीक्षण ढाँचा

### ढाँचा 1: प्रॉम्प्ट टेम्प्लेट परीक्षण

सरलतम ढाँचाले AI मोडेल कल नगरी प्रॉम्प्ट टेम्प्लेटहरू परीक्षण गर्छ। तपाईंले भेरिएबल प्रतिस्थापन सहि भईरहेको छ कि छैन र प्रॉम्प्टहरू अपेक्षाकृत ढाँचामा छन् कि छैनन् भनी जाँच गर्नुहुन्छ।

<img src="../../../translated_images/ne/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*प्रॉम्प्ट टेम्प्लेट परीक्षण देखाउँदै भेरिएबल प्रतिस्थापन प्रवाह: प्लेसहोल्डर भएको टेम्प्लेट → मानहरू लागू गरिएका → स्वरूपित आउटपुट पुष्टि गरियो*

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

**चलाउन:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#परीक्षणप्रॉम्प्टढाँचासज्जा
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#परीक्षणप्रॉम्प्टटेम्प्लेटफर्म्याटिङ
```

### ढाँचा 2: भाषा मोडेलहरूको मॉकिङ

संवाद तर्क परीक्षण गर्दा, Mockito प्रयोग गरेर अघोषित प्रतिक्रिया दिने गलत मोडेलहरू सिर्जना गर्नुहोस्। यसले परीक्षणहरूलाई छिटो, निःशुल्क र निर्धारणयोग्य बनाउँछ।

<img src="../../../translated_images/ne/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*परीक्षणका लागि मॉकहरू किन मनपर्छन् भन्ने तुलना: ती छिटो, निःशुल्क, निर्धारणयोग्य, र API कुञ्जीहरू आवश्यक पर्दैनन्*

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

यो ढाँचाले `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` मा देखिन्छ। मॉकले निश्चित व्यवहार सुनिश्चित गर्छ ताकि मेमोरी व्यवस्थापन सहि छ कि होइन जाँच्न सकियोस्।

### ढाँचा 3: संवाद अलगाव परीक्षण

संवाद मेमोरीले धेरै प्रयोगकर्ता अलग्गै राख्नुपर्छ। यो परीक्षणले संवादहरूले सन्दर्भ नमिलाउने सुनिश्चित गर्छ।

<img src="../../../translated_images/ne/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*संवाद अलगाव परीक्षण देखाउँदै फरक प्रयोगकर्ताहरूका लागि अलग मेमोरी स्टोरहरूले सन्दर्भ नमिल्ने गर्छ*

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

प्रत्येक संवादले आफ्नै स्वतन्त्र इतिहास राख्दछ। उत्पादन प्रणालीहरूमा, यो अलगाव बहु-प्रयोगकर्ता अनुप्रयोगहरूमा अतिप्रधान हुन्छ।

### ढाँचा 4: उपकरणहरू स्वतन्त्र रूपमा परीक्षण

उपकरणहरू फङ्क्सनहरू हुन् जुन AI ले कल गर्न सक्छ। AI निर्णय बिना नै उनीहरूलाई सिधा परीक्षण गरेर निश्चित गर्नुहोस् कि काम गरिरहेका छन्।

<img src="../../../translated_images/ne/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*उपकरणहरू स्वतन्त्र रुपमा परीक्षण गर्दै मॉक उपकरण निष्पादन देखाउँदै ब्यापार तर्क पुष्टि गर्न, AI कल बिना*

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

यी परीक्षणहरू `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` बाट छन् जसले उपकरण तर्क AI संलग्नता बिना प्रमाणित गर्छ। चेनिङ उदाहरणले देखाउँछ कसरी एउटा उपकरणको आउटपुट अर्कोको इनपुटमा जान्छ।

### ढाँचा 5: मेमोरी भित्रको RAG परीक्षण

RAG प्रणालीहरूले सामान्यतया भेक्टर डाटाबेस र एम्बेडिङ सेवाहरूको आवश्यकता पर्दछ। इन-मेमोरी ढाँचाले सम्पूर्ण पाइपलाइन बाह्य आश्रय बिना परीक्षण गर्न दिन्छ।

<img src="../../../translated_images/ne/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*इन-मेमोरी RAG परीक्षण कार्यप्रवाह देखाउँदै कागजात पार्सिङ, एम्बेडिङ स्टोरेज, र समानता खोज बिना डेटाबेस आवश्यकताको*

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

यो परीक्षण `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` बाट हो जसले कागजात मेमोरीमा बनाउँछ र चंकिङ र मेटाडाटा ह्यान्डलिङ पुष्टि गर्छ।

### ढाँचा 6: MCP एकीकरण परीक्षण

MCP मोड्युलले stdio ट्रान्सपोर्ट प्रयोग गरी मोडेल कन्टेक्स्ट प्रोटोकलको एकीकरण परीक्षण गर्छ। यी परीक्षणहरूले पुष्टि गर्छन् तपाईंको अनुप्रयोग MCP सर्भरहरूलाई subprocess रूपमा उत्पन्न गरी संवाद गर्न सक्षम छ।

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` मा परीक्षणहरूले MCP क्लाइन्ट व्यवहार प्रमाणित गर्छ।

**चलाउन:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## परीक्षण दर्शनशास्त्र

तपाईंको कोड परीक्षण गर्नुहोस्, AI होइन। तपाईंका परीक्षणहरूले तपाईंले लेखेको कोडलाई प्रमाणीकरण गर्नुपर्छ, जस्तै कसरी प्रॉम्प्टहरू बनाउनुभयो, कसरी मेमोरी व्यवस्थापन भयो, र कसरी उपकरणहरूले काम गरे। AI प्रतिक्रियाहरू फरक फरक हुन्छन् र परीक्षण दावीहरूको भाग हुनु हुँदैन। आफैंलाई सोध्नुहोस् कि तपाईंको प्रॉम्प्ट टेम्प्लेटले सहि रूपमा भेरिएबलहरू प्रतिस्थापन गर्यो कि गरेन, AI ले सही जवाफ दियो कि दिएन होइन।

भाषा मोडेलहरूका लागि मॉकहरू प्रयोग गर्नुहोस्। तिनीहरू बाह्य आश्रय हुन् जुन सुस्त, महँगो, र निर्धारण नभएका हुन्छन्। मॉकिङले परीक्षणहरूलाई सेकन्डको सट्टा मिलिसेकेन्डमा छिटो, निःशुल्क API लागत बिना, र हरेक पटक एउटै नतिजामा निर्धारणयोग्य बनाउँछ।

परीक्षणहरू स्वतन्त्र राख्नुहोस्। प्रत्येक परीक्षणले आफ्नै डाटा सेट अप गर्नुपर्छ, अरू परीक्षणहरूमा निर्भर हुनु हुँदैन र आफैं सफा गर्नुपर्छ। परीक्षणहरूले चलाउने क्रम जे भए पनि पास हुनुपर्छ।

खुसी मार्ग भन्दा बाहिरका सीमाहरू परीक्षण गर्नुहोस्। खाली इनपुट, धेरै ठूला इनपुटहरू, विशेष अक्षरहरू, अमान्य प्यारामिटरहरू, र सिमानाहरू प्रयास गर्नुहोस्। यी प्रायः त्यस्ता बगहरू पत्ता लगाउँछन् जुन सामान्य प्रयोगले देखाउँदैन।

वर्णनात्मक नामहरू प्रयोग गर्नुहोस्। `shouldMaintainConversationHistoryAcrossMultipleMessages()` लाई `test1()` सँग तुलना गर्नुहोस्। पहिलोले कुन कुरा परीक्षण हुँदैछ स्पष्ट पार्छ, जसले नभएको बेला डिबग गर्ने काम सजिलो बनाउँछ।

## अगाडि के गर्ने

अब तपाईं परीक्षण ढाँचाहरू बुझ्नु भयो, प्रत्येक मोड्युलमा बढी गहिराईमा जानुहोस्:

- **[00 - छिटो सुरु](../00-quick-start/README.md)** - प्रॉम्प्ट टेम्प्लेट मूल कुरा सुरु गर्नुहोस्
- **[01 - परिचय](../01-introduction/README.md)** - संवाद मेमोरी व्यवस्थापन सिक्नुहोस्
- **[02 - प्रॉम्प्ट इन्जिनियरिङ](../02-prompt-engineering/README.md)** - GPT-5.2 प्रॉम्प्टिङ ढाँचामा मास्टर गर्नुहोस्
- **[03 - RAG](../03-rag/README.md)** - रिट्रिभल-अग्मेन्टेड जेनेरेशन प्रणालीहरू बनाउनुहोस्
- **[04 - उपकरणहरू](../04-tools/README.md)** - फङ्क्शन कलिङ र उपकरण श्रृंखला कार्यान्वयन गर्नुहोस्
- **[05 - MCP](../05-mcp/README.md)** - मोडेल कन्टेक्स्ट प्रोटोकल एकीकरण गर्नुहोस्

प्रत्येक मोड्युलको README यहाँ परीक्षण गरिएका अवधारणाहरूको विस्तृत व्याख्या प्रदान गर्छ।

---

**नेभिगेसन:** [← मुख्यमा फर्कनुहोस्](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अपवाद**:
यस दस्तावेजलाई AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरी अनुवाद गरिएको हो। हामी सटीकताको लागि प्रयासरत छौं, तर कृपया ध्यान दिनुहोस् कि स्वचालित अनुवादमा त्रुटिहरू वा अशुद्धिहरू हुन सक्छन्। मूल दस्तावेज यसको मूल भाषामा आधिकारिक स्रोत मान्नुपर्छ। महत्वपूर्ण जानकारीको लागि, व्यावसायिक मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न कुनै पनि गलतफहमी वा गलत व्याख्याका लागि हामी जिम्मेवार छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->