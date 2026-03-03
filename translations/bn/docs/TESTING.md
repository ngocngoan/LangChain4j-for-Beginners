# LangChain4j অ্যাপ্লিকেশন টেস্টিং

## বিষয়বস্তু

- [দ্রুত শুরু](../../../docs)
- [টেস্টগুলো কি ঢেকে রাখে](../../../docs)
- [টেস্ট চালানো](../../../docs)
- [VS কোডে টেস্ট চালানো](../../../docs)
- [টেস্টিং প্যাটার্নস](../../../docs)
- [টেস্টিং দর্শন](../../../docs)
- [পরবর্তী ধাপসমূহ](../../../docs)

এই গাইডটি আপনাকে এমন টেস্টগুলোর মাধ্যমে চালনা করে যা দেখায় কিভাবে API কী বা বাহ্যিক সার্ভিস ছাড়াই AI অ্যাপ্লিকেশনগুলো টেস্ট করতে হয়।

## দ্রুত শুরু

একটি কমান্ডে সব টেস্ট চালান:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

যখন সব টেস্ট সফল হয়, আপনি নিচের স্ক্রিনশটের মতো আউটপুট দেখতে পাবেন — কোনো ব্যর্থতা ছাড়াই টেস্ট চলছে।

<img src="../../../translated_images/bn/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*সফল টেস্ট কার্যকরী হওয়া দেখাচ্ছে সবগুলো টেস্ট শূন্য ব্যর্থতা সহ পাস করছে*

## টেস্টগুলো কি ঢেকে রাখে

এই কোর্সটি ফোকাস করে **ইউনিট টেস্টস**-এ যা লোকালি চলে। প্রতিটি টেস্ট একটি নির্দিষ্ট LangChain4j ধারণাকে আলাদা করে প্রদর্শন করে। নিচের টেস্টিং পিরামিড দেখায় ইউনিট টেস্ট কোথায় ফিট হয় — এগুলো দ্রুত, নির্ভরযোগ্য ভিত্তি গঠন করে, যার উপর আপনার বাকি টেস্ট কৌশল তৈরি হয়।

<img src="../../../translated_images/bn/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*টেস্টিং পিরামিড ইউনিট টেস্ট (দ্রুত, পৃথক), ইন্টিগ্রেশন টেস্ট (বাস্তব কম্পোনেন্ট), এবং এন্ড-টু-এন্ড টেস্টের মধ্যে সামঞ্জস্য দেখাচ্ছে। এই প্রশিক্ষণে ইউনিট টেস্টিং অন্তর্ভুক্ত।*

| মডিউল | টেস্ট সংখ্যা | ফোকাস | প্রধান ফাইল |
|--------|-------|-------|-----------|
| **00 - দ্রুত শুরু** | 6 | প্রম্পট টেমপ্লেট এবং ভ্যারিয়েবল প্রতিস্থাপন | `SimpleQuickStartTest.java` |
| **01 - পরিচিতি** | 8 | কথোপকথন স্মৃতি ও স্টেটফুল চ্যাট | `SimpleConversationTest.java` |
| **02 - প্রম্পট ইঞ্জিনিয়ারিং** | 12 | GPT-5.2 প্যাটার্ন, আগ্রহের স্তর, কাঠামোগত আউটপুট | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | ডকুমেন্ট ইনজেশন, এম্বেডিংস, সাদৃশ্য অনুসন্ধান | `DocumentServiceTest.java` |
| **04 - টুলস** | 12 | ফাংশন কলিং এবং টুল চেইনিং | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | মডেল কনটেক্সট প্রটোকল উইথ স্টডিও ট্রান্সপোর্ট | `SimpleMcpTest.java` |

## টেস্ট চালানো

**রুট থেকে সব টেস্ট চালান:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**নির্দিষ্ট মডিউলের টেস্ট চালান:**

**Bash:**
```bash
cd 01-introduction && mvn test
# অথবা রুট থেকে
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# অথবা মূল থেকে
mvn --% test -pl 01-introduction
```

**একক টেস্ট ক্লাস চালান:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**নির্দিষ্ট টেস্ট মেথড চালান:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#কথোপকথনের ইতিহাস বজায় রাখা উচিত
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#আলাপন ইতিহাস বজায় রাখা উচিত
```


## VS কোডে টেস্ট চালানো

যদি আপনি Visual Studio Code ব্যবহার করেন, Test Explorer আপনাকে একটি গ্রাফিক্যাল ইন্টারফেস দেয় টেস্ট চালানো এবং ডিবাগ করার জন্য।

<img src="../../../translated_images/bn/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS কোড টেস্ট এক্সপ্লোরার দেখাচ্ছে সব জাভা টেস্ট ক্লাস এবং পৃথক টেস্ট মেথডের টেস্ট গাছ*

**VS কোডে টেস্ট চালানোর জন্য:**

1. অ্যাক্টিভিটি বারে বিটার আইকনে ক্লিক করে Test Explorer খুলুন  
2. টেস্ট গাছ বাড়ান সব মডিউল এবং টেস্ট ক্লাস দেখতে  
3. যেকোনো টেস্টের পাশে প্লে বাটনে ক্লিক করুন পৃথকভাবে চালানোর জন্য  
4. "Run All Tests" এ ক্লিক করে সম্পূর্ণ স্যুট চালান  
5. যেকোনো টেস্টে রাইট-ক্লিক করে "Debug Test" নির্বাচন করুন ব্রেকপয়েন্ট সেট করতে এবং কোড ধাপে ধাপে দেখতে

Test Explorer সব পাস করা টেস্টের জন্য সবুজ চেকমার্ক দেখায় এবং ব্যর্থ হলে বিস্তারিত ত্রুটি বার্তা প্রদান করে।

## টেস্টিং প্যাটার্নস

### প্যাটার্ন ১: প্রম্পট টেমপ্লেট টেস্টিং

সবচেয়ে সহজ প্যাটার্ন হল AI মডেল কল না করে প্রম্পট টেমপ্লেট টেস্ট করা। আপনি যাচাই করেন যে ভ্যারিয়েবল প্রতিস্থাপন সঠিকভাবে কাজ করে এবং প্রম্পট সঠিকভাবে ফর্ম্যাট হয়েছে।

<img src="../../../translated_images/bn/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*প্রম্পট টেমপ্লেট টেস্টিং দেখাচ্ছে ভ্যারিয়েবল প্রতিস্থাপনের ফ্লো: প্লেসবোল্ডারসহ টেমপ্লেট → মান প্রয়োগ → ফর্ম্যাটেড আউটপুট যাচাই*

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

এই টেস্টটি অবস্থিত `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`।

**চালান:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#টেস্টপ্রম্পটটেমপ্লেটফরম্যাটিং
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#টেস্টপ্রোম্পটটেমপ্লেটফরম্যাটিং
```


### প্যাটার্ন ২: ভাষা মডেল মকিং

কথোপকথন লজিক টেস্ট করার সময়, Mockito ব্যবহার করুন নকল মডেল তৈরি করতে যা নির্ধারিত রেসপন্স দেয়। এতে টেস্ট দ্রুত, ফ্রি এবং ডিটারমিনিস্টিক হয়।

<img src="../../../translated_images/bn/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*তুলনা দেখাচ্ছে কেন টেস্টিংয়ের জন্য মক পছন্দসই: এগুলো দ্রুত, ফ্রি, ডিটারমিনিস্টিক এবং কোনো API কী লাগে না*

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
        assertThat(history).hasSize(6); // ৩ ব্যবহারকারী + ৩ এআই বার্তা
    }
}
```

এই প্যাটার্নটি `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`-তে আছে। মক নিশ্চিত করে consistent আচরণ যাতে স্মৃতি ব্যবস্থাপনা সঠিক কাজ করে তা যাচাই করা যায়।

### প্যাটার্ন ৩: কথোপকথন বিচ্ছিন্নতা টেস্টিং

কথোপকথন স্মৃতি একাধিক ব্যবহারকারীকে আলাদা রাখতে হবে। এই টেস্ট যাচাই করে যে কথোপকথনগুলি প্রসঙ্গ মেশানো হয় না।

<img src="../../../translated_images/bn/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*কথোপকথন বিচ্ছিন্নতা টেস্টিং দেখাচ্ছে আলাদা ব্যবহারকারীর জন্য আলাদা স্মৃতি স্টোর যা প্রসঙ্গ মেশানো প্রতিরোধ করে*

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

প্রত্যেক কথোপকথন তার স্বাধীন ইতিহাস বজায় রাখে। প্রকৃত সিস্টেমে, এই বিচ্ছিন্নতা মাল্টি-ইউজার অ্যাপ্লিকেশনগুলোর জন্য অত্যাবশ্যক।

### প্যাটার্ন ৪: টুলস স্বাধীনভাবে টেস্ট করা

টুলস হল ফাংশন যেগুলো AI কল করতে পারে। সরাসরি টুলগুলো টেস্ট করুন নিশ্চিত করতে কাজ ঠিকঠাক করে, AI-র সিদ্ধান্ত নির্বিশেষে।

<img src="../../../translated_images/bn/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*স্বতন্ত্রভাবে টুলস টেস্টিং দেখাচ্ছে মক টুল এক্সিকিউশন AI কল ছাড়াই ব্যবসায়িক লজিক যাচাই করার জন্য*

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

এসব টেস্ট `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` থেকে এসেছে যা নিশ্চিত করে টুল লজিক AI জড়িত ছাড়াই কাজ করে। চেইনিং উদাহরণ দেখায় কিভাবে একটি টুলের আউটপুট অন্যটির ইনপুট ফিড হয়।

### প্যাটার্ন ৫: ইন-মেমোরি RAG টেস্টিং

RAG সিস্টেম সাধারণত ভেক্টর ডাটাবেস এবং এম্বেডিং সার্ভিস প্রয়োজন। ইন-মেমোরি প্যাটার্ন এ নিয়ে আসে পুরো পাইপলাইন টেস্ট করার সুযোগ বাহ্যিক নির্ভরতা ছাড়াই।

<img src="../../../translated_images/bn/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*ইন-মেমোরি RAG টেস্টিং ওয়ার্কফ্লো দেখাচ্ছে ডকুমেন্ট পার্সিং, এম্বেডিং সংরক্ষণ, এবং সাদৃশ্য অনুসন্ধান ডাটাবেস ছাড়াই*

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

এই টেস্টটি `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` থেকে, যেটা ইন-মেমোরি ডকুমেন্ট তৈরি করে এবং চাংকিং ও মেটাডাটা হ্যান্ডলিং যাচাই করে।

### প্যাটার্ন ৬: MCP ইন্টিগ্রেশন টেস্টিং

MCP মডিউল মডেল কনটেক্সট প্রটোকল ইন্টিগ্রেশন পরীক্ষা করে stdio ট্রান্সপোর্ট ব্যবহার করে। এই টেস্টগুলি যাচাই করে আপনার অ্যাপ MCP সার্ভারগুলোকে সাবপ্রসেস হিসেবে স্পন এবং কমিউনিকেট করতে পারে।

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java`-এর টেস্টগুলো MCP ক্লায়েন্ট আচরণ নিশ্চিত করে।

**চালান:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```


## টেস্টিং দর্শন

আপনার কোড টেস্ট করুন, AI নয়। আপনার টেস্টগুলো যাচাই করবে আপনি যেই কোড লিখেছেন সেটা কতটা সঠিক যেমন প্রম্পট কিভাবে তৈরি হচ্ছে, স্মৃতি কিভাবে ব্যবস্থাপিত হচ্ছে, এবং টুল কিভাবে নির্বাহ হচ্ছে। AI রেসপন্স ভিন্ন হতে পারে এবং তা টেস্টের অংশ হওয়া উচিত নয়। নিজেকে জিজ্ঞাসা করুন আপনার প্রম্পট টেমপ্লেট ভ্যারিয়েবল সঠিক প্রতিস্থাপন করছে কি না, AI সঠিক উত্তর দিচ্ছে কি না নয়।

ভাষা মডেলের জন্য মক ব্যবহার করুন। এগুলো বাহ্যিক নির্ভরতা যেগুলো ধীর, ব্যয়বহুল, এবং অনির্ধারিত। মকিং করে টেস্ট দ্রুত হয় মিলিসেকেন্ডে না যে সেকেন্ডে, ফ্রি হয় কোনো API খরচ ছাড়াই, এবং ডিটারমিনিস্টিক হয় প্রতিবার একই ফলাফল দিয়ে।

টেস্টগুলো স্বাধীন রাখুন। প্র প্রতিটি টেস্ট নিজস্ব ডেটা সেট আপ করবে, অন্য টেস্টের উপর নির্ভর করবে না, এবং নিজেই ক্লিন আপ করবে। টেস্টগুলো চালানোর ক্রম অনুসারে পাস না হওয়া উচিত।

হ্যাপি পাথ ছাড়াও এজ কেস টেস্ট করুন। খালি ইনপুট, খুব বড় ইনপুট, বিশেষ অক্ষর, অবৈধ প্যারামিটার, এবং সীমানা শর্ত পরীক্ষা করুন। এগুলো প্রায়শই এমন বাগ খুঁজে বের করে যা সাধারণ ব্যবহার ফাঁকি দেয়।

বর্ণনামূলক নাম ব্যবহার করুন। `shouldMaintainConversationHistoryAcrossMultipleMessages()` এর সাথে তুলনা করুন `test1()` এর। প্রথমটি আপনি কী টেস্ট করছেন ঠিকই বলে দেয়, যা ব্যর্থতা ডিবাগ সহজ করে।

## পরবর্তী ধাপসমূহ

এখন যেহেতু আপনি টেস্টিং প্যাটার্নগুলো বুঝে গেছেন, প্রতিটি মডিউলে গভীরভাবে প্রবেশ করুন:

- **[00 - দ্রুত শুরু](../00-quick-start/README.md)** - প্রম্পট টেমপ্লেট বেসিক দিয়ে শুরু করুন  
- **[01 - পরিচিতি](../01-introduction/README.md)** - কথোপকথন স্মৃতি ব্যবস্থাপনা শিখুন  
- **[02 - প্রম্পট ইঞ্জিনিয়ারিং](../02-prompt-engineering/README.md)** - GPT-5.2 প্রম্পটিং প্যাটার্ন মাস্টার করুন  
- **[03 - RAG](../03-rag/README.md)** - রিট্রিভাল-অগমেন্টেড জেনারেশন সিস্টেম তৈরি করুন  
- **[04 - টুলস](../04-tools/README.md)** - ফাংশন কলিং এবং টুল চেইন বাস্তবায়ন করুন  
- **[05 - MCP](../05-mcp/README.md)** - মডেল কনটেক্সট প্রটোকল ইন্টিগ্রেট করুন  

প্রতিটি মডিউলের README-তে এখানে টেস্ট হওয়া ধারণাগুলোর বিস্তারিত ব্যাখ্যা আছে।

---

**নেভিগেশন:** [← মেইনে ফিরে যান](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ইনকার্জনোটঃ**  
এই নথিটি AI অনুবাদ সেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। যদিও আমরা সঠিকতার জন্য চেষ্টা করি, স্বয়ংক্রিয় অনুবাদে ভুল বা অসঙ্গতি থাকতে পারে। মূল নথিটি তার নিজ ভাষাতেই কর্তৃত্বপূর্ণ উৎস হিসেবে বিবেচনা করা উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানব অনুবাদের পরামর্শ দেওয়া হয়। এই অনুবাদের ব্যবহারে কোনো ভুল বোঝাবুঝি বা বিভ্রাটের জন্য আমরা দায়িত্বশীল নই।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->