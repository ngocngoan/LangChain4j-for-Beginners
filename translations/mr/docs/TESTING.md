# LangChain4j अनुप्रयोगांची चाचणी

## अनुक्रमणिका

- [त्वरित प्रारंभ](../../../docs)
- [चाचण्या काय व्यापतात](../../../docs)
- [चाचण्या चालविणे](../../../docs)
- [VS कोड मध्ये चाचण्या चालविणे](../../../docs)
- [चाचणी नमुने](../../../docs)
- [चाचणी तत्त्वज्ञान](../../../docs)
- [पुढील पावले](../../../docs)

हा मार्गदर्शक तुम्हाला असे चाचणी कसे करायचे ते दाखवतो ज्यामुळे AI अनुप्रयोगांची चाचणी API की किंवा बाह्य सेवांशिवाय करता येते.

## त्वरित प्रारंभ

सर्व चाचण्या एकाच आदेशाने चालवा:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/mr/test-results.ea5c98d8f3642043.webp" alt="यशस्वी चाचणी निकाल" width="800"/>

*यशस्वी चाचणी चालविणे जी सर्व चाचण्या शून्य अपयशांसोबत पार पडल्या दाखवते*

## चाचण्या काय व्यापतात

हा अभ्यासक्रम मुख्यतः **युनिट चाचण्या** यावर केंद्रित आहे जो स्थानिक पातळीवर चालतात. प्रत्येक चाचणी एक विशिष्ट LangChain4j संकल्पना वेगळेपणाने दाखवते.

<img src="../../../translated_images/mr/testing-pyramid.2dd1079a0481e53e.webp" alt="चाचणी पिरामिड" width="800"/>

*चाचणी पिरामिड दाखविते युनिट चाचण्यांमध्ये (वेगवान, स्वयंपूर्ण), एकात्मिक चाचणी (खर्‍या घटकांशी) आणि एंड-टू-एंड चाचणींचा संतुलन. हा प्रशिक्षण युनिट चाचणींना समर्पित आहे.*

| मॉड्यूल | चाचण्या | लक्ष केंद्रित | प्रमुख फायली |
|--------|-------|-------|-----------|
| **00 - त्वरित प्रारंभ** | 6 | प्रॉम्प्ट टेम्पलेट आणि चल बदल | `SimpleQuickStartTest.java` |
| **01 - परिचय** | 8 | संभाषण स्मृती आणि स्थितीपूर्ण चॅट | `SimpleConversationTest.java` |
| **02 - प्रॉम्प्ट अभियांत्रिकी** | 12 | GPT-5.2 नमुने, उत्सुकता स्तर, संरचित आउटपुट | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | दस्तऐवज ग्रहण, एम्बेडिंग्ज, साम्य शोध | `DocumentServiceTest.java` |
| **04 - साधने** | 12 | फंक्शन कॉलिंग आणि साधन शृंखला | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | मॉडेल कॉन्टेक्स्ट प्रोटोकॉल विथ स्ट्डिओ ट्रान्स्पोर्ट | `SimpleMcpTest.java` |

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
# किंवा रूटमधून
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
mvn test -Dtest=SimpleConversationTest#संभाषण इतिहास राखला पाहिजे
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#संभाषणाचा इतिहास जपावा
```

## VS कोड मध्ये चाचण्या चालविणे

जर तुम्ही Visual Studio Code वापरत असाल, तर Test Explorer ग्राफिकल इंटरफेस पुरवतो ज्याद्वारे चाचण्या चालवू आणि डीबग करू शकता.

<img src="../../../translated_images/mr/vscode-testing.f02dd5917289dced.webp" alt="VS कोड टेस्ट एक्सप्लोरर" width="800"/>

*VS कोड टेस्ट एक्सप्लोरर सर्व Java चाचणी वर्ग आणि वैयक्तिक चाचणी पद्धतींसह चाचणी वृक्ष दाखवते*

**VS कोड मध्ये चाचण्या चालविण्यासाठी:**

1. Activity Bar मध्ये बीकर आयकॉनवर क्लिक करून Test Explorer उघडा
2. सर्व मॉड्यूल आणि चाचणी वर्ग पाहण्यासाठी चाचणी वृक्ष विस्तृत करा
3. कोणतीही चाचणी स्वतंत्रपणे चालविण्यासाठी प्ले बटणावर क्लिक करा
4. संपूर्ण सूट चालविण्यासाठी "Run All Tests" क्लिक करा
5. कोणतीही चाचणी सुधारणा करण्यासाठी उजवे-क्लिक करा आणि "Debug Test" निवडा जेणेकरून ब्रेकपोईंट सेट करू शकता आणि कोड स्टेप करू शकता

Test Explorer यशस्वी चाचण्यांसाठी हिरवे टिक चिन्ह दाखवतो आणि चूक झाल्यावर तपशीलवार अपयश संदेश प्रदर्शित करतो.

## चाचणी नमुने

### नमुना 1: प्रॉम्प्ट टेम्पलेटसची चाचणी

सर्वात सोपा नमुना प्रॉम्प्ट टेम्पलेट्सची चाचणी आहे ज्यामध्ये कोणत्याही AI मॉडेलला कॉल करत नाही. तुम्ही तपासता की चल बदल योग्यरित्या काम करतात आणि प्रॉम्प्ट अपेक्षेप्रमाणे फॉर्मॅट झाले आहेत.

<img src="../../../translated_images/mr/prompt-template-testing.b902758ddccc8dee.webp" alt="प्रॉम्प्ट टेम्पलेट चाचणी" width="800"/>

*प्रॉम्प्ट टेम्पलेट चाचणी जेथे चल बदल प्रक्रिया दर्शविली आहे: जागा राखीव असलेले टेम्पलेट → मूल्ये लागू केली → फॉर्मॅट्स तपासले*

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
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#चाचणीप्रॉम्पटटेम्प्लेटफॉरमॅटिंग
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#चाचणीप्रॉम्प्टटेम्पलेटफॉरमॅटिंग
```

### नमुना 2: भाषा मॉडेलची मॉकिंग

संवाद लॉजिकची चाचणी करताना, Mockito वापरून नकली मॉडेल तयार करा जे आधीच ठरविलेल्या प्रतिसादांची परतफेड करतात. यामुळे चाचणी जलद, विनामूल्य आणि ठराविक (निर्णायक) बनतात.

<img src="../../../translated_images/mr/mock-vs-real.3b8b1f85bfe6845e.webp" alt="मॉक विरुद्ध रीयल API तुलना" width="800"/>

*तुलना ज्यात मॉकस का प्राधान्य दिले जातेः ते वेगवान, मोफत, ठराविक आणि कोणतीही API की आवश्यक नसते*

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

हा नमुना `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` मध्ये आढळतो. मॉक सतत वर्तन सुनिश्चित करतो ज्यामुळे तुम्ही लक्षात ठेवण्याचे व्यवस्थापन योग्य असल्याची पुष्टी करू शकता.

### नमुना 3: संभाषण वेगळेपणा चाचणी

संभाषणाची स्मृती अनेक वापरकर्त्यांना वेगळे ठेवली पाहिजे. ही चाचणी तपासते की संभाषण संदर्भ मिसळत नाहीत.

<img src="../../../translated_images/mr/conversation-isolation.e00336cf8f7a3e3f.webp" alt="संभाषण वेगळेपणा" width="800"/>

*संभाषण वेगळेपणा चाचणी जी वेगवेगळ्या वापरकर्त्यांसाठी स्वतंत्र स्मृती संच दाखवते ज्यामुळे संदर्भ मिश्रण टाळले जाते*

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

प्रत्येक संभाषण आपला स्वतंत्र इतिहास राखते. उत्पादन प्रणालींमध्ये, हा वेगळेपणा बहु-वापरकर्ता अनुप्रयोगांसाठी अत्यावश्यक आहे.

### नमुना 4: साधने स्वतंत्रपणे चाचणी करणे

साधने ही फंक्शन्स आहेत ज्यांना AI कॉल करू शकतो. AI निर्णयांपासून स्वतंत्रपणे त्यांची चाचणी करा जेणेकरून ते बरोबर कार्य करतात याची खात्री करता येईल.

<img src="../../../translated_images/mr/tools-testing.3e1706817b0b3924.webp" alt="साधने चाचणी" width="800"/>

*AI कॉलशिवाय मॉक साधन चालविणे दाखवणारी स्वतंत्र साधन चाचणी, ज्यामुळे व्यावसायिक लॉजिकची पुष्टी होते*

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

ही चाचणी `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` मध्ये आहे, जिथे साधन लॉजिक AI सहभागाशिवाय सत्यापित केले जाते. शृंखलाबद्धतेचा नमुना दर्शवितो की एका साधनाचे आउटपुट कसे दुसऱ्या साधनाच्या इनपुटमध्ये वापरले जाते.

### नमुना 5: इन-मेमरी RAG चाचणी

RAG सिस्टीम सामान्यतः वेक्टर डेटाबेस आणि एम्बेडिंग सेवा आवश्यक करतात. इन-मेमरी नमुन्यामुळे तुम्ही संपूर्ण पाईपलाइन बाह्य अवलंबित्वांशिवाय चाचणी करू शकता.

<img src="../../../translated_images/mr/rag-testing.ee7541b1e23934b1.webp" alt="इन-मेमरी RAG चाचणी" width="800"/>

*इन-मेमरी RAG चाचणी कार्यप्रवाह जे दस्तऐवज पार्सिंग, एम्बेडिंग संग्रहण आणि समानता शोध दाखवतो, डेटाबेस न वापरता*

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

ही चाचणी `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` मधून दस्तऐवज स्मृतीत तयार करते आणि चंकिंग आणि मेटाडेटा हाताळणी तपासते.

### नमुना 6: MCP एकत्रिकरण चाचणी

MCP मॉड्यूल Model Context Protocol वापरून stdio ट्रान्स्पोर्ट एकत्रिकरणाची चाचणी करते. ही चाचणी तपासते की तुमचा अनुप्रयोग MCP सर्वर्स सबप्रोसेस म्हणून निर्माण आणि संवाद करू शकतो का.

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` मधील चाचण्या MCP क्लायंट वर्तन सत्यापित करतात.

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

तुमचा कोड तपासा, AI नाही. तुमच्या चाचण्या त्या कोडचे प्रमाणीकरण कराव्यात जे तुम्ही लिहित आहात, प्रॉम्प्ट कसे तयार केले जातात, स्मृती कशी व्यवस्थापित होते, आणि साधने कशी कार्यान्वित होतात हे पाहून. AI प्रतिसाद वेगवेगळे असू शकतात आणि ते चाचणी दावे करण्याचा भाग नसावेत. स्वतःला विचारा की तुमचा प्रॉम्प्ट टेम्पलेट योग्यरित्या चल बदल करतोय का, AI योग्य उत्तर देतोय का नाही हे नाही.

भाषा मॉडेलसाठी मॉकचा वापर करा. ते बाह्य अवलंबित्व आहेत जे हळू, महाग आणि अनिश्चयी असतात. मॉकिंगमुळे चाचण्या सेकंदांऐवजी मिलीसेकंदात जलद, कोणताही API खर्च न करता मोफत आणि प्रत्येक वेळेस एकसमान परिणाम देणाऱ्या ठराविक होतात.

चाचण्या स्वयंपूर्ण ठेवाः प्रत्येक चाचणी स्वतःचे डेटा सेट करावी, इतर चाचण्यांवर अवलंबून राहू नये, आणि स्वतःची साफसफाई करावी. चाचण्या कोणत्याही अमलबजावणी क्रमाने उत्तीर्ण होऊ शकतात.

सुखद मार्गापलीकडे कडा केसांची चाचणी करा. रिकामे इनपुट, फार मोठे इनपुट, विशेष अक्षरे, अवैध मापदंड, आणि सीमांत परिस्थिती तपासा. हे सामान्य वापरात उघड न होणाऱ्या बग्स उघडू शकतात.

वर्णनात्मक नावे वापरा. `shouldMaintainConversationHistoryAcrossMultipleMessages()` व `test1()` चे तुलना करा. पहिले नक्की कोणती चाचणी होते ते सांगते, ज्यामुळे अपयश दुरुस्त करणे खूप सोपे होते.

## पुढील पावले

आता जेव्हा तुम्हाला चाचणी नमुने समजले आहेत, तर प्रत्येक मॉड्यूलमध्ये अधिक खोलात जा:

- **[00 - त्वरित प्रारंभ](../00-quick-start/README.md)** - प्रॉम्प्ट टेम्पलेट मूलभूत गोष्टींबरोबर प्रारंभ करा
- **[01 - परिचय](../01-introduction/README.md)** - संभाषण स्मृती व्यवस्थापन शिका
- **[02 - प्रॉम्प्ट अभियांत्रिकी](../02-prompt-engineering/README.md)** - GPT-5.2 प्रॉम्प्टिंग नमुने तज्ज्ञ व्हा
- **[03 - RAG](../03-rag/README.md)** - शोध–वर्धित निर्मिती प्रणाली तयार करा
- **[04 - साधने](../04-tools/README.md)** - फंक्शन कॉलिंग आणि साधन शृंखला राबवा
- **[05 - MCP](../05-mcp/README.md)** - मॉडेल कॉन्टेक्स्ट प्रोटोकॉल एकत्रित करा

प्रत्येक मॉड्यूलची README येथे तपासलेल्या संकल्पनांची सविस्तर समज देते.

---

**नेव्हिगेशन:** [← मुख्यपृष्ठाकडे परत](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**सूचना**:
ही कागदपत्र AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) वापरून अनुवादित केली आहे. आम्ही अचूकतेसाठी प्रयत्न करतो, तरी कृपया लक्षात घ्या की स्वयंचलित अनुवादांमध्ये चुका किंवा अचूकतेचा अभाव असू शकतो. मूळ कागदपत्र त्याच्या स्थानिक भाषेत अधिकृत स्रोत मानले जावे. महत्त्वाच्या माहितीसाठी व्यावसायिक मानवी अनुवादाची शिफारस केली जाते. या अनुवादाच्या वापराने होणार्‍या कोणत्याही गैरसमज किंवा चुकीच्या अर्थ लावणीबाबत आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->