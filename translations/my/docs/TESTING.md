# LangChain4j အပလီကေးရှင်းများ စမ်းသပ်ခြင်း

## အကြောင်းအရာ စာရင်း

- [လျင်မြန်စွာ စတင်ခြင်း](../../../docs)
- [စမ်းသပ်ချက်များ ပတ်သက်သောအကြောင်း](../../../docs)
- [စမ်းသပ်ချက်များ ပြေးဆွဲခြင်း](../../../docs)
- [VS Code တွင် စမ်းသပ်ချက်များ ပြေးဆွဲခြင်း](../../../docs)
- [စမ်းသပ်မှု ပုံစံများ](../../../docs)
- [စမ်းသပ်မှုအယူအဆ](../../../docs)
- [နောက်တစ်ဆင့်များ](../../../docs)

ဤလမ်းညွှန်သည် API key သို့မဟုတ် ပြင်ပဝန်ဆောင်မှုများ မလိုအပ်ဘဲ AI အပလီကေးရှင်းများကို စမ်းသပ်ရန် နည်းလမ်းများကို ဖော်ပြသည့် စမ်းသပ်ချက်များကို လမ်းပြပါသည်။

## လျင်မြန်စွာ စတင်ခြင်း

တစ်ချက်တည်းဖြင့် စမ်းသပ်ချက်အားလုံးကို ပြေးဆွဲပါ။

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/my/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*အောင်မြင်စွာ စမ်းသပ်မှုဆောင်ရွက်ပြီး အပျက် ၀ မရှိသည့် စမ်းသပ်ချက်များ ပြသခြင်း*

## စမ်းသပ်ချက်များ ပတ်သက်သောအကြောင်း

ဤသင်ခန်းစာသည် ဒေသခံတွင် လည်ပတ်သော **ယူနစ်စမ်းသပ်ချက်များ** အား အဓိကထားပါသည်။ အစီအစဉ်တိုင်းသည် LangChain4j ၏ သက်ဆိုင်ရာ စိတ်ကူးများကို သီးခြား ဖေါ်ပြသည်။

<img src="../../../translated_images/my/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*စမ်းသပ်မှု ပျရမစ်ပြသည် - ယူနစ်စမ်းသပ်မှုများ (လျင်မြန်၊ သီးခြား), ပေါင်းစပ်စမ်းသပ်မှုများ (အစိတ်အပေါ်များ), နှင့် အဆုံးသတ်စမ်းသပ်မှုများ။ ဤသင်တန်းသည် ယူနစ်စမ်းသပ်မှုတွင် အာရုံစိုက်သည်။*

| ကဏ္ဍ | စမ်းသပ်ချက်များ | အာရုံစိုက်မှု | အဓိကဖိုင်များ |
|--------|-------|-------|-----------|
| **00 - လျင်မြန်စတင်ခြင်း** | 6 | အကြံပြုစာသားပုံစံများနှင့် အပြောင်းအလဲဒေတာထည့်သွင်းခြင်း | `SimpleQuickStartTest.java` |
| **01 - နိဒါန်း** | 8 | စကားပြောမှတ်ဉာဏ်နှင့် အခြေအနေနှင့် တိတိကျကျ စကားပြောခြင်း | `SimpleConversationTest.java` |
| **02 - အကြံပြုစာသား အင်ဂျင်နီယာ** | 12 | GPT-5.2 ပုံစံများ၊ စိတ်ပါဝင်စားမှု အဆင့်များ၊ ဖွဲ့စည်းတည်ဆောက်မှုပုံစံ | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | စာရွက်စာတမ်း ဝင်ရောက် ဆန်းစစ်ခြင်း၊ အလျှောက်အဆွဲ၊ နီးစပ်မှု ရှာဖွေခြင်း | `DocumentServiceTest.java` |
| **04 - Tools** | 12 | Function ခေါ်ဆိုခြင်းနှင့် Tools ဆက်သွယ်ခြင်း | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol နှင့် Stdio ထုတ်လွှင့်မှု | `SimpleMcpTest.java` |

## စမ်းသပ်ချက်များ ပြေးဆွဲခြင်း

**Root မှ စမ်းသပ်ချက် စုစုပေါင်း ပြေးဆွဲရန်:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**အထူး module အတွက် စမ်းသပ်ရန်:**

**Bash:**
```bash
cd 01-introduction && mvn test
# ဒါမှမဟုတ် root မှပင်
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# ဒါမှမဟုတ် root မှစ၍
mvn --% test -pl 01-introduction
```

**တစ်ခုတည်းသော စမ်းသပ်ရန် class ပြေးရန်:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**တိကျသော စမ်းသပ်မှု method တစ်ခု ပြေးရန်:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#စကားပြောဆိုမှု သမိုင်းမှတ်တမ်းကို ထိန်းသိမ်းထားရန်လိုသည်
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#စကားပြောဆိုမှုမှတ်တမ်းကိုထိန်းသိမ်းထားသင့်သည်
```

## VS Code တွင် စမ်းသပ်မှုများ ပြေးဆွဲခြင်း

Visual Studio Code ကို အသုံးပြုပါက Test Explorer သည် စမ်းသပ်မှုများကို ပြေးဆွဲခြင်းနှင့် debug လုပ်ရန် ရုပ်မြင် интерфေ့ကို ပေးဆောင်သည်။

<img src="../../../translated_images/my/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer သည် Java စမ်းသပ် class များနှင့် တစ်ခုချင်းစီ စမ်းသပ် method များပါသော စမ်းသပ်ခြင်း ဗဟိုပြ များကို ပြသသည်*

**VS Code တွင် စမ်းသပ်ရန်:**

1. Activity Bar တွင် beaker ပုံတွင် ကလစ်၍ Test Explorer ဖြင့် ဖွင့်ပါ
2. စမ်းသပ်ခြင်းပင်လယ်စာရင်း ထွက်ရန် ပြုလုပ်ပြီး module များနှင့် စမ်းသပ် class များ ကြည့်ရှုပါ
3. တစ်ခုချင်းစီ စမ်းသပ်ရန် play ခလုတ်ကို နှိပ်ပါ
4. စုစုပေါင်း စမ်းသပ်ရန် "Run All Tests" ကို ရွေးချယ်ပါ
5. စမ်းသပ်မှု တစ်ခုနှင့်ရင်း နေရာညှိသည့် "Debug Test" ကို right-click ဖြင့် ရွေးချယ်ပါ၊ breakpoint ကြပ်၍ code step through လုပ်နိုင်ပါသည်

Test Explorer မှ သစ်ရွက် အမှတ် အနီလက္ခဏာနှင့် စမ်းသပ်မှု မအောင်မြင်ပါက အသေးစိတ် အချက်ပြချက်များကို ပြပါသည်။

## စမ်းသပ်မှု ပုံစံများ

### ပုံစံ ၁: အကြံပြုစာသား ပုံစံ စမ်းသပ်ခြင်း

အလွန် ရိုးရှင်းဆုံး ပုံစံမှာ AI မော်ဒယ် မခေါ်ဘဲ အကြံပြုစာသား ပုံစံများကို စမ်းသပ်သည်။ မတော်တဆ အချက်အလက်များ ပြောင်းလဲသွင်းခြင်းကို စစ်ဆေးပြီး prompt များမှန်ကန်စွာ ပြုလုပ်ထားသည်ကို အတည်ပြုသည်။

<img src="../../../translated_images/my/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*အကြံပြုစာသား ပုံစံ စမ်းသပ်မှု အကြောင်း ပြသချက်: placeholder ပါသော ပုံစံ → တန်ဖိုး ထည့်သွင်းခြင်း → ဖေါ်ပြချက် စစ်ဆေးခြင်း*

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

ဤ စမ်းသပ်ချက်ကို `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java` တွင် တွေ့ရှိနိုင်သည်။

**ပြေးရန်:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#စမ်းသပ်ရန်PromptTemplateFormatကိုဖော်ပြခြင်း
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#စမ်းသပ်မှုတမ်းပလိတ်ဖွဲ့စည်းမှု
```

### ပုံစံ ၂: ဘာသာစကား မော်ဒယ်များကို မော့ခ်လုပ်ခြင်း

စကားပြော ဆက်ဆံရေး များစမ်းသပ်ရာတွင် Mockito ကို အသုံးပြု၍ ကြိုတင် သတ်မှတ်ထားသည့် တုံ့ပြန်မှုများ ပေးသော တု မော်ဒယ်များ ဖန်တီးနိုင်သည်။ ဤနည်းဖြင့်စမ်းသပ်မှုများ လျင်မြန်ပြီး အခမဲ့ဖြစ်ကာ ရွေးချယ်နိုင်မှုရှိသည်။

<img src="../../../translated_images/my/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*မော့ခ်သည် အကြောင်းပြချက်များ - လျင်မြန်၊ အခမဲ့၊ ရွေးချယ်နိုင်ပြီး API key မလိုအပ်ခြင်း*

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
        assertThat(history).hasSize(6); // အသုံးပြုသူ ၃ ယောက် + AI မက်ဆေ့ခ်ျ ၃ ခု
    }
}
```

ဤ ပုံစံကို `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` တွင် တွေ့သည်။ မော့ခ်သည် စိတ်သွင်းမှု စီမံခန့်ခွဲမှု မှန်ကန်မှုကို အတည်ပြုရန် တည်ငြိမ်မှုရှိစေသည်။

### ပုံစံ ၃: စကားပြော ဤဇိုင်းမှု စမ်းသပ်ခြင်း

စကားပြောမှတ်ဉာဏ်သည် အသုံးပြုသူများ မတူညီမှုများကို သီးခြား ထိန်းသိမ်းထားရမည်။ ဤ စမ်းသပ်ချက်သည် စကားပြောအကြောင်းအရာ မပြည့်စုံစွာ ပေါင်းစပ် မလုပ်ကြောင်း အတည်ပြုသည်။

<img src="../../../translated_images/my/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*စကားပြော ဤဇိုင်းမှုပုံစံ - အသုံးပြုသူ မတူညီမှုများအတွက် သီးရှားသော မှတ်ဉာဏ်ဆိုင်ရာ ပြသချက်*

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

စကားပြောတိုင်းသည် သီးခြား သမိုင်းကြောင်းကို ထိန်းသိမ်းထားသည်။ ထုတ်လုပ်မှုဆိုင်ရာ စနစ်များတွင် ဒီ ဤဇိုင်းမှုသည် အရေးပါသည်။

### ပုံစံ ၄: Tools ကို သီးခြား စမ်းသပ်ခြင်း

Tools သည် AI ၏ ဖုန်းခေါ်ခွင့်ဖြစ်သည်။ AI ဆုံးဖြတ်ချက် မလိုဘဲ တိုက်ရိုက် စမ်းသပ်၍ စီးပွားရေး အချက်အလက်မှန်ကန်မှုကို အတည်ပြုပါ။

<img src="../../../translated_images/my/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Tools ကို သီးခြား စမ်းသပ်မှု - AI မခေါ်ဘဲ မော့ခ် tool အကောင်အထည်ဖော်မှု*

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

ဒီ စမ်းသပ်ချက်များကို `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` မှာတွေ့ရသည်။ Tool chaining နမူနာမှာ တစ်ခု၏ အကုန်အကျမှ တစ်ခုသို့ ထည့်သွင်းသည့် နည်းလမ်းကို ပြသသည်။

### ပုံစံ ၅: မှတ်ဉာဏ်အတွင်း RAG စမ်းသပ်ခြင်း

RAG စနစ်များသည် လတ်တလော ပြင်ပဓာတ်မှတ်တမ်းများနှင့် embedding ဝန်ဆောင်မှုများလိုအပ်သည်။ မှတ်ဉာဏ်တွင်း ပုံစံသည် ပြင်ပ အားထားမှု များ မလိုအပ်ဘဲ pipeline အားလုံး စမ်းသပ်နိုင်သည်။

<img src="../../../translated_images/my/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*မှတ်ဉာဏ်အတွင်း RAG စမ်းသပ်မှု လုပ်ငန်းစဉ် - စာရွက် စလုံးထုတ်ခြင်း၊ embedding သိမ်းဆည်းခြင်းနှင့် နီးစပ်မှု ရှာဖွေမှု*

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

ဤ စမ်းသပ်ချက်ကို `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` မှာ တွေ့ရသည်။ စာရွက်များကို မှတ်ဉာဏ်အတွင်း ဖန်တီးပြီး စာလုံးခွဲခြင်းနှင့် meta-data ကို စစ်ဆေးသည်။

### ပုံစံ ၆: MCP ပေါင်းစည်းမှု စမ်းသပ်ခြင်း

MCP module သည် Model Context Protocol ကို stdio ကူးပြောင်းမှုဖြင့် စမ်းသပ်သည်။ သင်၏ အပလီကေးရှင်းသည် MCP server များကို subprocess အဖြစ် ဖန်တီးပြီး ဆက်သွယ်နိုင်မှုများကို ဤစမ်းသပ်ချက်များ တည်းဖြတ်သည်။

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` တွင် MCP client အပြုအမူကို စစ်ဆေးသည်။

**စမ်းသပ်ရန်:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## စမ်းသပ်မှုအယူအဆ

သင်၏ ကိုဒ်ကို စမ်းသပ်ပါ၊ AI ကို မဟုတ်ပါ။ သင်၏စမ်းသပ်ချက်များသည် prompt များကို ဘယ်လို ဖန်တီးမည်၊ မှတ်ဉာဏ် စီမံခြင်းနှင့် tool များ အကောင်အထည်ဖော်ပုံကို အတည်ပြုရန် ဖြစ်ရမည်။ AI တုံ့ပြန်ချက်များသည် မတည်ငြိမ်သောကြောင့် စမ်းသပ်ချက်ထဲ ထည့်သင့်မည် မဟုတ်ပါ။ သင်၏ prompt ပုံစံသည် တန်ဖိုးများကို မှန်ကန်စွာ စက်နှိပ်သည်မှာ ဖြစ်/ မဖြစ်ကို စစ်ဆေးပါ၊ AI မှ တုံ့ပြန်ချက်မှန်ကန်သည်ကို မမေးရ။

ဘာသာစကား မော်ဒယ်များကို မော့ခ်သုံးပါ။ ပိုရင်းနှီးပြီး နှေးကွေး၊ အပေါက်ကြေးများရှိသော ပြင်ပ ထောက်ခံမှုဖြစ်သည်။ မော့ခ်သည် စမ်းသပ်မှုများကို စက္ကန့်များမဟုတ် င milliseconds အတွင်း ပြီးစီးစေသည်၊ အခမဲ့ ဖြစ်စေသည်၊ တည်ငြိမ်စေသည်။

စမ်းသပ်မှုများကို လွတ်လပ်စွာ ထားပါ။ တစ်ခုချင်းစီသည် ကိုယ်ပိုင် ဒေတာပြင်ဆင်ပြီး အခြား စမ်းသပ်မှုများ မအခြေခံဘဲ သန့်ရှင်းမှု ပြုလုပ်ရမည်။ စမ်းသပ်မှုသည် အဆင့်အတန်း မရွေး အောင်မြင်ရမည်။

ပျော်ရွှင်သောလမ်းကြောင်း အပြင် ထိပ်တန်းဖြတ်ရပ်များကို စမ်းသပ်ပါ။ အလွတ် input များ၊ အလွန်ကြီးမားသော input များ၊ အထူးကာရက်တာများ၊ မမှန်ကန်သော ပရမားတာများ နှင့် ကန့်သတ်မှုများ စမ်းသပ်ပါ။ ပုံမှန်အသုံးပြုမှုတွင် မတွေ့ရသော bugs များ ရှာဖွေဖော်ထုတ်နိုင်သည်။

ဖော်ပြချက်အမည်များကို သုံးပါ။ `shouldMaintainConversationHistoryAcrossMultipleMessages()` နှင့် `test1()` နှိုင်းယှဉ်ပါ။ ပထမဆုံးသည် စမ်းသပ်နေသည့် အကြောင်းအရာကို ဖော်ပြ၍ အမှားဖြေရှင်းရာကို ပိုလွယ်ကူစေသည်။

## နောက်တစ်ဆင့်များ

ယခုသင်သည် စမ်းသပ်မှု ပုံစံများကို နားလည်ပြီး သင်တန်းတစ်ခုချင်းစီထဲသို့ နက်နက်ရှိုင်းရှိုင်း ဝင်ရောက်လိုက်ပါ။

- **[00 - လျင်မြန်စတင်ခြင်း](../00-quick-start/README.md)** - အကြံပြုစာသား ပုံစံ မူလအကြောင်းအရာများဖြင့် စတင်ပါ
- **[01 - နိဒါန်း](../01-introduction/README.md)** - စကားပြောမှတ်ဉာဏ် စီမံခန့်ခွဲမှုပိုင်း လေ့လာပါ
- **[02 - အကြံပြုစာသား အင်ဂျင်နီယာ](../02-prompt-engineering/README.md)** - GPT-5.2 ပုံစံများကို ကျွမ်းကျင်ပါ
- **[03 - RAG](../03-rag/README.md)** - retrieval-augmented generation စနစ်များ တည်ဆောက်ပါ
- **[04 - Tools](../04-tools/README.md)** - function ခေါ်ဆိုခြင်းနှင့် tool ဆက်သွယ်မှု တည်ဆောက်ပါ
- **[05 - MCP](../05-mcp/README.md)** - Model Context Protocol ပေါင်းစည်းပါ

module တစ်ခုချင်းစီ၏ README တွင် ဤနေရာတွင် စမ်းသပ်သော အယူအဆများကို အသေးစိတ် ရှင်းလင်းထားသည်။

---

**သွားရာလမ်း:** [← မူလစာမျက်နှာသို့ ပြန်သွားရန်](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**အတည်မပြုချက်**  
ဤစာရွက်စာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှုဖြစ်သော [Co-op Translator](https://github.com/Azure/co-op-translator) ဖြင့်ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှန်ကန်မှုအတွက်ကြိုးစားသော်လည်း၊ အလိုအလျောက်ဘာသာပြန်မှုမှာ အမှားများ သို့မဟုတ် မှားယွင်းမှုများ ပါဝင်နိုင်သည့်အတွက် သတိပြုပါရန် အပ်ပါသည်။ မူလစာရွက်စာတမ်းမှ မူရင်းဘာသာဖြင့်သာ စိစစ်စရာအကျိုးရှိသည်ဟု ဆိုလိုပါသည်။ အရေးကြီးသောသတင်းအချက်အလက်များအတွက် မူကြမ်းလူသားဘာသာပြန်ခြင်းကို အကြံပြုပါသည်။ ဤဘာသာပြန်ချက်ကို အသုံးပြုရာမှ ဖြစ်ပေါ်သော နားလည်ချက်မှားခြင်းများအတွက် ကျွန်ုပ်တို့တာဝန်မရှိပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->