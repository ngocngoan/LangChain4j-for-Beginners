# LangChain4j অ্যাপ্লিকেশন পরীক্ষা

## বিষয়বস্তু সূচি

- [দ্রুত শুরু](../../../docs)
- [পরীক্ষাগুলো কি কভার করে](../../../docs)
- [পরীক্ষাগুলো চালানো](../../../docs)
- [VS কোডে পরীক্ষাগুলো চালানো](../../../docs)
- [পরীক্ষার প্যাটার্ন](../../../docs)
- [পরীক্ষার দর্শন](../../../docs)
- [পরবর্তী ধাপসমূহ](../../../docs)

এই গাইডটি আপনাকে পরীক্ষা সম্পর্কে ধারণা দেয় যা দেখায় কীভাবে API কী বা বাহ্যিক সার্ভিস ছাড়াও AI অ্যাপ্লিকেশন পরীক্ষা করা যায়।

## দ্রুত শুরু

একটি কমান্ড দিয়ে সব পরীক্ষা চালান:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/bn/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*সফলভাবে পরীক্ষার ফলাফল যা দেখাচ্ছে সব পরীক্ষা পাস করেছে এবং কোনো ব্যর্থতা নেই*

## পরীক্ষাগুলো কি কভার করে

এই কোর্সটি **ইউনিট টেস্ট** এর উপর ফোকাস করে যা লোকালি চলে। প্রতিটি পরীক্ষা পৃথক LangChain4j ধারণা আলাদা করে দেখায়।

<img src="../../../translated_images/bn/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*পরীক্ষার পিরামিড যা দেখায় ইউনিট টেস্ট (দ্রুত, পৃথক), ইন্টিগ্রেশন টেস্ট (বাস্তব উপাদান), এবং এন্ড-টু-এন্ড টেস্টের মধ্যে ভারসাম্য। এই প্রশিক্ষণ ইউনিট টেস্টিং কভার করে।*

| মডিউল | পরীক্ষা | ফোকাস | মূল ফাইলসমূহ |
|--------|--------|-------|---------------|
| **00 - দ্রুত শুরু** | 6 | প্রম্পট টেমপ্লেট এবং ভেরিয়েবল পরিবর্তন | `SimpleQuickStartTest.java` |
| **01 - পরিচিতি** | 8 | কথা বলার স্মৃতি এবং রাজ্যযুক্ত চ্যাট | `SimpleConversationTest.java` |
| **02 - প্রম্পট ইঞ্জিনিয়ারিং** | 12 | GPT-5.2 প্যাটার্ন, আগ্রহের স্তর, গঠনমূলক আউটপুট | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | ডকুমেন্ট ইনজেশন, এম্বেডিং, সাদৃশ্য অনুসন্ধান | `DocumentServiceTest.java` |
| **04 - টুলস** | 12 | ফাংশন কলিং এবং টুল চেইনিং | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | মডেল কনটেক্সট প্রোটোকল স্টডিও ট্রান্সপোর্টের সাথে | `SimpleMcpTest.java` |

## পরীক্ষাগুলো চালানো

**রুট থেকে সব পরীক্ষা চালান:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**নির্দিষ্ট মডিউলের জন্য পরীক্ষা চালান:**

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
mvn test -Dtest=SimpleConversationTest#কথা বলার ইতিহাস বজায় রাখা উচিত
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#কথা বলার ইতিহাস বজায় রাখা উচিত
```

## VS কোডে পরীক্ষাগুলো চালানো

যদি আপনি ভিজুয়াল স্টুডিও কোড ব্যবহার করেন, টেস্ট এক্সপ্লোরার গ্রাফিক্যাল ইন্টারফেস দেয় পরীক্ষাগুলো চালানো এবং ডিবাগ করার জন্য।

<img src="../../../translated_images/bn/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS কোড টেস্ট এক্সপ্লোরার দেখাচ্ছে টেস্ট গাছ, সব জাভা টেস্ট ক্লাস এবং পৃথক টেস্ট মেথডসহ*

**VS কোডে পরীক্ষা চালানোর ধাপসমূহ:**

1. অ্যাক্টিভিটি বারের বেকার আইকনে ক্লিক করে টেস্ট এক্সপ্লোরার খুলুন
2. সব মডিউল এবং টেস্ট ক্লাস দেখতে টেস্ট গাছ বিস্তৃত করুন
3. কোনো একটি টেস্ট চালাতে পাশে প্লে বাটনে ক্লিক করুন
4. পুরো টেস্ট চালানোর জন্য "Run All Tests" ক্লিক করুন
5. কোনো টেস্টে ডিবাগ সেট করতে রাইট-ক্লিক করে "Debug Test" সিলেক্ট করুন

টেস্ট এক্সপ্লোরার পাস হওয়া টেস্টের জন্য সবুজ চেকমার্ক দেখায় এবং ব্যর্থ হলে বিস্তারিত বার্তা প্রদান করে।

## পরীক্ষার প্যাটার্ন

### প্যাটার্ন ১: প্রম্পট টেমপ্লেটের পরীক্ষা

সবচেয়ে সহজ প্যাটার্ন হচ্ছে প্রম্পট টেমপ্লেট পরীক্ষা যা কোনো AI মডেল কল করে না। আপনি যাচাই করবেন ভেরিয়েবল পরিবর্তন সঠিকভাবে হচ্ছে এবং প্রম্পটগুলি প্রত্যাশিত ফরম্যাটে আছে।

<img src="../../../translated_images/bn/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*প্রম্পট টেমপ্লেট টেস্টিং দেখায় ভেরিয়েবল সাবস্টিটিউশনের ধারা: প্লেসহোল্ডার সহ টেমপ্লেট → মান প্রয়োগ → ফরম্যাটেড আউটপুট যাচাই*

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

এই পরীক্ষা `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java` ফাইলে অবস্থিত।

**চালান:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#টেস্টপ্রম্পটটেমপ্লেটফরম্যাটিং
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#টেস্টপ্রম্পটটেমপ্লেটফরম্যাটিং
```

### প্যাটার্ন ২: ভাষার মডেল মক করা

কথোপকথন লজিক পরীক্ষার সময়, Mockito ব্যবহার করে নকল মডেল তৈরি করুন যা পূর্বনির্ধারিত উত্তর দেয়। এটি পরীক্ষাগুলো দ্রুত, বিনামূল্যে, এবং নির্ধারিত করে তোলে।

<img src="../../../translated_images/bn/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*তুলনামূলক দেখানো হচ্ছে কেন পরীক্ষা করার জন্য মক ব্যবহার প্রাধান্য পায়: কারণ এগুলো দ্রুত, বিনামূল্যে, নির্ধারিত, এবং API কী লাগে না*

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

এই প্যাটার্নটি `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` এ আছে। মক নিশ্চিত করে ধারাবাহিক আচরণ যাতে আপনি যাচাই করতে পারেন স্মৃতি ব্যবস্থাপনা সঠিকভাবে কাজ করছে।

### প্যাটার্ন ৩: কথোপকথনের বিচ্ছিন্নতা পরীক্ষা

কথোপকথনের স্মৃতি অবশ্যই একাধিক ব্যবহারকারীর আলাদা রাখতে হবে। এই পরীক্ষা যাচাই করে কথোপকথনসমূহ কনটেক্সট মিশ্রিত নয়।

<img src="../../../translated_images/bn/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*কথোপকথন বিচ্ছিন্নতা পরীক্ষা দেখায় পৃথক ব্যবহারকারীদের জন্য আলাদা স্মৃতি সংরক্ষণ যাতে কনটেক্সট মিশে না যায়*

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

প্রত্যেক কথোপকথন তার স্বাধীন ইতিহাস ধরে রাখে। প্রোডাকশনে, এই বিচ্ছিন্নতা বহু-ব্যবহারকারীর অ্যাপ্লিকেশনের জন্য অত্যন্ত গুরুত্বপূর্ণ।

### প্যাটার্ন ৪: টুলস স্বাধীনভাবে পরীক্ষা করা

টুলস হলো ফাংশন যা AI কল করতে পারে। সরাসরি পরীক্ষা করুন যাতে সেগুলো সঠিকভাবে কাজ করে AI সিদ্ধান্ত নির্বিশেষে।

<img src="../../../translated_images/bn/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*টুলস স্বাধীনভাবে পরীক্ষা যা দেখায় মক টুল কার্যক্রম AI কল ছাড়াই ব্যবসায়ের লজিক যাচাই করতে*

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

এই পরীক্ষাগুলো `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` থেকে এসেছে এবং টুল লজিক যাচাই করে AI জড়িত না থাকেও। চেইনিং উদাহরণ দেখায় কিভাবে এক টুলের আউটপুট অন্যটির ইনপুট হয়।

### প্যাটার্ন ৫: ইন-মেমরি RAG পরীক্ষা

RAG সিস্টেম সাধারণত ভেক্টর ডাটাবেস এবং এম্বেডিং সার্ভিস প্রয়োজন। ইন-মেমরি প্যাটার্ন আপনাকে সমস্ত পাইপলাইন পরীক্ষা করতে দেয় বাহ্যিক নির্ভরতা ছাড়াই।

<img src="../../../translated_images/bn/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*ইন-মেমরি RAG পরীক্ষা কার্যপ্রবাহ দেখায় ডকুমেন্ট পার্সিং, এম্বেডিং স্টোরেজ, এবং সাদৃশ্য অনুসন্ধান ডাটাবেস ছাড়াই*

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

এই পরীক্ষা `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` থেকে এসেছে, যা একটি ডকুমেন্ট ইন-মেমরিতে তৈরি করে এবং চঙ্কিং ও মেটাডাটা পরিচালনা যাচাই করে।

### প্যাটার্ন ৬: MCP ইন্টিগ্রেশন পরীক্ষা

MCP মডিউল মডেল কনটেক্সট প্রোটোকল ইন্টিগ্রেশন পরীক্ষা করে stdio ট্রান্সপোর্ট ব্যবহার করে। এই পরীক্ষা যাচাই করে যে আপনার অ্যাপ্লিকেশন MCP সার্ভারকে সাবপ্রসেস হিসেবে শুরু এবং যোগাযোগ করতে পারে।

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` এ পরীক্ষা MCP ক্লায়েন্ট আচরণ যাচাই করে।

**চালান:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## পরীক্ষার দর্শন

আপনার কোড পরীক্ষা করুন, AI নয়। আপনার পরীক্ষাগুলো সেই কোড যাচাই করবে যা আপনি লিখেছেন যেমন প্রম্পট কিভাবে তৈরি হয়, স্মৃতি কিভাবে পরিচালিত হয়, এবং টুলস কিভাবে কার্যকর হয়। AI প্রতিক্রিয়া ভিন্ন হতে পারে এবং সেটি পরীক্ষার অংশ হওয়া উচিত নয়। নিজেকে প্রশ্ন করুন আপনার প্রম্পট টেমপ্লেট সঠিকভাবে ভেরিয়েবল পরিবর্তন করছে কি না, AI সঠিক উত্তর দিচ্ছে কি না নয়।

ভাষার মডেলের জন্য মক ব্যবহার করুন। এগুলো বাহ্যিক নির্ভরতা যা ধীর, ব্যয়বহুল, এবং নির্ধারিত নয়। মকিং পরীক্ষাগুলো দ্রুত (মিলিসেকেন্ডে) করে, বিনামূল্যে (কোনো API খরচ ছাড়া), এবং একই ফলাফল বারবার দেয়।

পরীক্ষাগুলো স্বাধীন রাখুন। প্রত্যেক পরীক্ষা তার নিজস্ব ডেটা সেটআপ করবে, অন্য পরীক্ষার উপর নির্ভর করবে না, এবং পরিশেষে নিজেই পরিষ্কার করবে। পরীক্ষাগুলো যেকোনো ক্রমে চালালে পাস করা উচিত।

সুখী পথে ছাড়াও এজ কেস পরীক্ষা করুন। ফাঁকা ইনপুট, খুব বড় ইনপুট, বিশেষ অক্ষর, অবৈধ প্যারামিটার, এবং সীমান্ত অবস্থা চেষ্টা করুন। এগুলো সাধারণ ব্যবহারে প্রকাশ পায় না এমন বাগ ফাঁস করে।

বর্ণনামূলক নাম ব্যবহার করুন। উদাহরণস্বরূপ `shouldMaintainConversationHistoryAcrossMultipleMessages()` এবং `test1()` তুলনা করুন। প্রথমটি স্পষ্টভাবে বলে কী পরীক্ষা হচ্ছে, ব্যর্থ হলে ডিবাগ করা সহজ হয়।

## পরবর্তী ধাপসমূহ

এখন যখন আপনি পরীক্ষার প্যাটার্ন বুঝে গেছেন, প্রতিটি মডিউল আরও গভীরে অন্বেষণ করুন:

- **[00 - দ্রুত শুরু](../00-quick-start/README.md)** - প্রম্পট টেমপ্লেটের বেসিক দিয়ে শুরু করুন
- **[01 - পরিচিতি](../01-introduction/README.md)** - কথোপকথন স্মৃতি ব্যবস্থাপনা শিখুন
- **[02 - প্রম্পট ইঞ্জিনিয়ারিং](../02-prompt-engineering/README.md)** - GPT-5.2 প্রম্পট প্যাটার্নের মাস্টারি নিন
- **[03 - RAG](../03-rag/README.md)** - রিট্রিভাল-অগমেন্টেড জেনারেশন সিস্টেম তৈরি করুন
- **[04 - টুলস](../04-tools/README.md)** - ফাংশন কলিং ও টুল চেইন ইমপ্লিমেন্ট করুন
- **[05 - MCP](../05-mcp/README.md)** - মডেল কনটেক্সট প্রোটোকল ইন্টিগ্রেশন করুন

প্রতিটি মডিউলের README এখানে পরীক্ষা করা ধারণাগুলোর বিস্তারিত ব্যাখ্যা দেয়।

---

**নেভিগেশন:** [← প্রধান পৃষ্ঠায় ফিরে যান](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**অস্বীকারোক্তি**:  
এই নথিটি AI অনুবাদ সেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। আমরা সঠিকতার জন্য চেষ্টা করি, তবে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা অসঙ্গতির সম্ভাবনা থাকতে পারে। মূল নথি তার নিজ ভাষায়ই কর্তৃত্ববাহী উৎস হিসেবে বিবেচনা করা উচিত। গুরুতর তথ্যের জন্য পেশাদার মানব অনুবাদের পরামর্শ দেওয়া হয়। এই অনুবাদের ব্যবহারে হওয়া যেকোন ভুল বোঝাবুঝি বা ভুল ব্যাখ্যার জন্য আমরা দায়বদ্ধ থাকব না।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->