# LangChain4j Applications စမ်းသပ်ခြင်း

## မူကြောင်းဇယား

- [အမြန်စတင်မှု](../../../docs)
- [စမ်းသပ်မှုများ ဘာတွေ ပါဝင်သလဲ](../../../docs)
- [စမ်းသပ်မှုများကို ပြုလုပ်ခြင်း](../../../docs)
- [VS Code မှာ စမ်းသပ်မှုများ ပြုလုပ်ခြင်း](../../../docs)
- [စမ်းသပ်မှု ပုံစံများ](../../../docs)
- [စမ်းသပ်မှု အယူအဆ](../../../docs)
- [နောက်တစ်ဆင့်](../../../docs)

ဤလမ်းညွှန်သည် API key မလိုအပ်ဘဲ သို့မဟုတ် အပြင်စနစ်များမလိုအပ်ဘဲ AI applications များကို စမ်းသပ်နည်းများကို ပြသသော စမ်းသပ်မှုများကို ရှင်းပြသည်။

## အမြန်စတင်မှု

အဘယ်သူမျှမ စမ်းသပ်မှုများအားလုံးကို အမိန့်တစ်ခုဖြင့် ပြုလုပ်ပါ။

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

စမ်းသပ်မှုအားလုံး ပြီးဆုံးသွားပါက အောက်တွင် ပြထားသည့် screenshot ကဲ့သို့ output တွေ့ပါမည် — စမ်းသပ်မှုများ ကွဲလွှတ်မှုမရှိပေါင်းလုံးပြီးလုပ်ဆောင်သည်။

<img src="../../../translated_images/my/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*စမ်းသပ်မှု အောင်မြင်စွာ ပြုလုပ်ခြင်းနှင့် စမ်းသပ်မှုအားလုံးကွဲလွှတ်မှုမရှိဖြင့် ပြပွဲ*

## စမ်းသပ်မှုများ ဘာတွေ ပါဝင်သလဲ

ဤသင်တန်းသည် **unit tests** အား အခြေပြုသည်။ စမ်းသပ်မှု တစ်ခုချင်းစီသည် LangChain4j အဆောက်အအုံတစ်ခုကို အထူးသဖြင့် မဟာဗျူဟာ တစ်ခုအဖြစ် မူတည်ကာ လုပ်ဆောင်သည်။ အောက်ပါ စမ်းသပ်မှု ပီရမစ်သည် unit test များ၏ တည်ရှိရာနေရာကို ဖော်ပြသည် — ၎င်းတို့သည် သင့်တစ်ခုချင်း test မဟာဗျူဟာ အခြေခံ သာမာန်၊ အမြန်နှင့် ယုံကြည်စိတ်ချရသော အခြေခံဖြစ်သည်။

<img src="../../../translated_images/my/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Unit tests (မြန်ဆန်ပြီး ပိတ်ဆို့ခြင်းမရှိသော), integration tests (အမှန်တကယ် ကိရိယာများဖြင့်), နှင့် end-to-end tests တို့အကြား ကျိုးကြောင်းဆက်နွယ်မှုအချိုးအစား ကို ဖော်ပြထားသည်။ ဤသင်တန်းသည် unit testing ကို ဖော်ပြသည်။*

| Module | Tests | အာရုံစိုက်ချက် | အဓိကဖိုင်များ |
|--------|-------|-----------------|----------------|
| **00 - Quick Start** | 6 | Prompt နမူနာများနှင့် အစားထိုးခြင်း | `SimpleQuickStartTest.java` |
| **01 - Introduction** | 8 | စကားပြောမှတ်ဉာဏ်နှင့် အခြေအနေရှိသော စကားဝိုင်း | `SimpleConversationTest.java` |
| **02 - Prompt Engineering** | 12 | GPT-5.2 ပုံစံများ၊ စိတ်အားထက်သန်မှု အဆင့်များ၊ ဖွဲ့စည်းတည့်သော output | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | စာရွက်စာတမ်း ရယူခြင်း၊ embedding များ၊ ဆင်တူမှု ရှာဖွေမှု | `DocumentServiceTest.java` |
| **04 - Tools** | 12 | ဖွင့်ဆိုမှုလုပ်ဆောင်ခြင်းနှင့် ကိရိယာလိုက်လိုက်မှု | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol နှင့် Stdio မောင်းနှင်မှု | `SimpleMcpTest.java` |

## စမ်းသပ်မှုများကို ပြုလုပ်ခြင်း

**root directory မှ စမ်းသပ်မှုအားလုံးကို ပြုလုပ်ရန်:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**အထူးသတ်မှတ် module အတွက် စမ်းသပ်မှုများပြုလုပ်ရန်:**

**Bash:**
```bash
cd 01-introduction && mvn test
# မူလမှ သို့မဟုတ်
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# ဒါမှမဟုတ် root မှာနေရာယူပါ
mvn --% test -pl 01-introduction
```

**Test class တစ်ခုလုံးကို ပြုလုပ်ရန်:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**အထူးသတ်မှတ် စမ်းသပ်မှုနည်းလမ်းတစ်ခုကို ပြုလုပ်ရန်:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#စကားပြောဆိုမှုမှတ်တမ်းကို ထိန်းသိမ်းထားသင့်သည်
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#စကားပြောသမိုင်းကို ထိန်းသိမ်းထားသင့်သည်။
```

## VS Code တွင် စမ်းသပ်မှုများ ပြုလုပ်ခြင်း

Visual Studio Code သုံးပါက Test Explorer သည် စမ်းသပ်မှုများကို အကြမ်းဖျဉ်မှု ပြုလုပ်ရန် နှင့် debug ပြုလုပ်ရန် ဂရပ်ဖစ်နယ်အင်တာဖေ့စ် ဖြစ်သည်။

<img src="../../../translated_images/my/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer သည် Java test class အားလုံးနှင့် တစ်ကိုယ်ရည် စမ်းသပ်မှု နည်းလမ်းများကို ပြသနေသည်*

**VS Code တွင် စမ်းသပ်မှုများပြုလုပ်ရန်:**

1. Activity Bar တွင် Beaker အိုင်ကွန်ကိုနှိပ်ကာ Test Explorer ကိုဖွင့်ပါ
2. စမ်းသပ်မှု အဖွဲ့အစည်းနှင့် test classes အားလုံးကို ကြည့်ရှုရန် စမ်းသပ်မှု ထောက်ပံ့ပွားမှု များကို ကျယ်ပြန့်စွာ ဖွင့်ပါ
3. စမ်းသပ်မှု တစ်ခုချင်းကို လုပ်ဆောင်ရန် ဘေးရှိ play အိုင်ကွန်ကို နှိပ်ပါ
4. စမ်းသပ်မှုအားလုံးကို လုပ်ဆောင်ရန် "Run All Tests" ကို နှိပ်ပါ
5. စမ်းသပ်မှု တစ်ခုကိုညာခလုတ်နှိပ်ပြီး "Debug Test" ကို ရွေးချယ်ကာ breakpoint များ ထားပြီး ကုဒ်ကို အဆင့်လိုက် မိတ်ဆက်ကြည့်ပါ

Test Explorer တွင် အောင်မြင်သော စမ်းသပ်မှုများအတွက် အစိမ်းရောင်ကွင်းကပ် အတည်ပြုချက်များ ပြသပြီး စမ်းသပ်မှု မအောင်မြင်ပါက အသေးစိတ် အရှုံးရှာမှု မက်ဆေ့များ ထောက်ပြသည်။

## စမ်းသပ်မှု ပုံစံများ

### ပုံစံ ၁: Prompt Template စမ်းသပ်ခြင်း

အလွယ်ဆုံး ပုံစံမှာ AI မော်ဒယ် မခေါ်ဘဲ prompt template များကို စမ်းသပ်သည်။ ခေါင်းစဉ်အစားထိုးမှုမှန်ကန်စွာ လုပ်ဆောင်သည်ဟု အတည်ပြုခြင်းနှင့် ပုံစံတည့်တည့်ဖြင့် ဖော်ပြခြင်းကို လုပ်ဆောင်သည်။

<img src="../../../translated_images/my/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Prompt template စမ်းသပ်မှု ဖော်ပြချက်: နမူနာဖြင့် အစားထိုးမှု → တန်ဖိုးများ ထည့်သွင်း → ဖော်ပြမှု အောင်မြင်သည်ဟု စစ်ဆေးခြင်း*

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

ဤစမ်းသပ်မှုသည် `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java` တွင် အားကျဆုံးခြင်း။

**ပြုလုပ်ရန်:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#စမ်းသပ်မေးခွန်းပုံစံဖော်ပြချက်အတန်းစဉ်ရေးဆွဲခြင်း
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#စမ်းသပ်မည့် Prompt Template ပုံစံရေးဆွဲခြင်း
```

### ပုံစံ ၂: Language Model များ ကို Mock လုပ်ခြင်း

စကားပြောထိန်းချုပ်မှု မြန်နှုန်းနှင့် ပေါ့ပါးစေသည့် စမ်းသပ်မှုများအတွက် Mockito ကိုအသုံးပြုပြီး အတုမော်ဒယ်များဖန်တီးကြသည်။ ဤနည်းလမ်းသည် စမ်းသပ်မှုများအား မြန်ဆန်၊ အခမဲ့နှင့် ရလဒ်တည်ငြိမ်စေသည်။

<img src="../../../translated_images/my/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*စမ်းသပ်ရန် mocks များကို ဘာကြောင့် မော်ဒယ်အသုံးချခြင်းထက် မတူညီကြောင်းဖော်ပြသည်: မြန်ဆန်, အခမဲ့, ရလဒ်တည်ငြိမ်, API key မလိုအပ်*

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
        assertThat(history).hasSize(6); // အသုံးပြုသူ ၃ ဦး + AI မက်ဆေ့ခ်ျ ၃ ခု
    }
}
```

ဤပုံစံသည် `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` တွင် ပါဝင်သည်။ Mock မော်ဒယ်သည် ရေရှည်တည်ငြိမ်တည်ရှိမှုရှိစေရန် memory management မှန်ကန်မှုကို စစ်ဆေးနိုင်စေရန် ဖြစ်သည်။

### ပုံစံ ၃: စကားပြောတည်ငြိမ်မှု စမ်းသပ်ခြင်း

စကားပြောမှတ်ဉာဏ်သည် အသုံးပြုသူများ အရေအတွက် များစွာရှိခြင်းကွဲလွဲသည့် စနစ်ဖြစ်ရန် လိုအပ်သည်။ ဤစမ်းသပ်မှုသည် စကားပြောများ၏ context မတူမတူ လွှဲပြောင်းမှု မဖြစ်စေရန် စစ်ဆေးသည်။

<img src="../../../translated_images/my/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*တစ်ဦးချင်းစကားပြောမှတ်ဉာဏ် store များကား။ အသုံးပြုသူ မတူကွဲပြားမှု မိမိ Context မမှားခန့်မှန်းပေးသည်*

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

စကားပြောတစ်ခုစီသည် အလိုအလျောက် သီးခြားမှတ်တမ်းထားသည်။ ထုတ်လုပ်ရေးစနစ်များတွင် ဤကွဲပြားမှု ဖြစ်ရပ်သည် များစွာသော အသုံးပြုသူများအတွက် အရေးကြီးသည်။

### ပုံစံ ၄: Tools များကို သီးခြား စမ်းသပ်ခြင်း

Tools များသည် AI ကခေါ်ယူသည့် function များဖြစ်သည်။ AI ဆုံးဖြတ်ချက်မလိုအပ်ဘဲ တိုက်ရိုက် စမ်းသပ်ခြင်းဖြင့် ကောင်းမွန်စွာလုပ်ဆောင်နေသော tool များဖြစ်ကြောင်း သေချာစေရန် ရည်ရွယ်သည်။

<img src="../../../translated_images/my/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*AI မပါသော mock tool အကောင်အထည်ဖော်ခြင်းမှ စီးပွားရေး ဒေတာလုပ်ငန်းကိုစစ်ဆေးခြင်း*

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

`04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` ထဲမှ ဤစမ်းသပ်မှုများမှာ AI ပါဝင်ခြင်းမရှိဘဲ tool များ၏လုပ်ဆောင်မှုအဆင့်မြှင့်သုံးသပ်ပုံဖြစ်သည်။ ထို chaining ဥပမာသည် tool တစ်ခု၏ output သည် နောက် tool တစ်ခုရဲ့ input အဖြစ် အသုံးပြုသည်ကို ဖော်ပြသည်။

### ပုံစံ ၅: In-Memory RAG စမ်းသပ်ခြင်း

RAG စနစ်များသည် ပုံမှန်အားဖြင့် vector database နှင့် embedding service များ လိုအပ်သည်။ In-memory ပုံစံသည် အပြင်မဟုတ်သော ဆက်စပ်မှုများ မလိုအပ်ဘဲ စစ်ဆေးနိုင်ခြင်းကို ခွင့်ပြုသည်။

<img src="../../../translated_images/my/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*စာရွက်စာတမ်း ဖြတ်တိုက်ခြင်း embedding သိုလှောင်မှုနှင့် ဆင်တူရှာဖွေမှုကို database မပါဘဲ ပြုလုပ်ခြင်း ပုံစံ*

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

ဤစမ်းသပ်မှုသည် `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` မှဖြစ်ပြီး memory တွင် စာရွက်စာတမ်းတည်ဆောက်ခြင်း၊ ခွဲခြမ်းခြင်းနှင့် မီတာဒေတာ ကို စစ်ဆေးသည်။

### ပုံစံ ၆: MCP ပေါင်းစည်း စမ်းသပ်ခြင်း

MCP module သည် stdio transport ကို အသုံးပြု Model Context Protocol ပေါင်းစည်းခြင်း စမ်းသပ်မှုများ ဖြစ်သည်။ ဤစမ်းသပ်မှုများသည် MCP servers များကို subprocess အနေဖြင့်ဖန်တီးပြီး ဆက်သွယ်နိုင်ခြေရှိမှုကို အတည်ပြုသည်။

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

## စမ်းသပ်မှု အယူအဆ

AI ကို မစမ်းသပ်ပါနဲ့၊ သင့်ကုဒ်ကို စမ်းသပ်ပါ။ သင့်စမ်းသပ်မှုများသည် prompt များ စတင်ဖန်တီးပုံ၊ မှတ်ဉာဏ် ကမ်းလှမ်းပုံနှင့် tools များ လုပ်ဆောင်မှုကို မှန်ကန်စွာ အသုံးပြုမှု အတည်ပြုရန် ရည်ရွယ်သည်။ AI ရဲ့ဖြေဆိုချက်များသည် မတည်ငြိမ်သည့် အချက်များဖြင့် စမ်းသပ်မှုများတွင် ပါဝင်သင့်ခြင်းမဟုတ်ပါ။ prompt template တွင် အစားထိုးမှုများ မွန်ကန်စွာဖြစ်သည်ဟု စစ်ဆေးပါ၊ AI မှ တုံ့ပြန်ချက် မှန်ကန်သည်ဟု မမေးပါနှင့်။

Language model များအတွက် mocks ကို အသုံးပြုပါ။ ဤကိစ္စများသည် ပင်မ အဆင့်သည် ငြင်းဆန်၊ အရမ်းကြီးသော ကုန်ကျစရိတ်ရှိသည်နှင့် ရလဒ် မတူညီနိုင်သော အပြင် ပျင်းရိစေပါသည်။ Mocking က စမ်းသပ်မှုများကို စက္ကန့်များ အစား မီလီစက္ကန့် အတွင်း အမြန်ဆုံး ပြုလုပ်နိုင်စေပြီး၊ အခမဲ့ဖြစ်စေပြီး၊ တစ်ကြိမ်တည်း ရလဒ်သန့်ရှင်းစေသည်။

စမ်းသပ်မှုများကို လွတ်လပ်နေသည်စေပါ။ စမ်းသပ်မှု တစ်ခုချင်းစီသည် မိမိအချက်အလက်ကို သီးသန့်ပြင်ဆင်ပြီး၊ အခြား စမ်းသပ်မှုများကို မူတည်ခြင်းမရှိဘဲ ပြုလုပ်ရန်နှင့် ပြီးနောက် ကိုယ်ပိုင် သန့်ရှင်းမှု လုပ်သင့်သည်။ စမ်းသပ်မှုများသည် လည်ပတ်မှု အတွ Reihenfolge ဖျက်ပစ်ခြင်းမရှိဘဲ အောင်မြင်ရမည်။

ပျော်မွေ့မှု လမ်းကြောင်း မကျော်လွှားသော အခြေအနေများကို စမ်းသပ်ပါ။ ပျက်စီးသော input များ၊ အလွန်ကြီးသော input များ၊ အထူးသဏ္ဍာန်များ၊ မမှန်ကန်သော ပါရာမီတာများ နှင့် အကန့်အသတ် အခြေအနေများ ကို စမ်းသပ်ပါ။ ဤအခြေအနေများမှာ မကြာခဏအဖောင်းပိုက်များကို ဖော်ထုတ်ပေးသည်။

ဖော်ပြချက် နာမည်များ အသုံးပြုပါ။ `shouldMaintainConversationHistoryAcrossMultipleMessages()` နှင့် `test1()` ကို နှိုင်းယှဉ်ပါ။ ပထမအချက်သည် သတ်မှတ်ထားသည့် စမ်းသပ်မှုအကြောင်းကို တိတိကျကျ ဖော်ပြပေးကာ ပြန်လည်စစ်ဆေးရာတွင် ပိုမိုလွယ်ကူစေသည်။

## နောက်တစ်ဆင့်

စမ်းသပ်မှုပုံစံများကို နားလည်ပြီးနောက် အုပ်စုတစ်ခုချင်းစီရှင်းလင်းစွာ လေ့လာပါ။

- **[00 - Quick Start](../00-quick-start/README.md)** - Prompt template အခြေခံများဖြင့် စတင်ပါ
- **[01 - Introduction](../01-introduction/README.md)** - စကားပြောမှတ်ဉာဏ် စီမံခန့်ခွဲမှု သင်ယူပါ
- **[02 - Prompt Engineering](../02-prompt-engineering/README.md)** - GPT-5.2 prompt patterns ကိုကျွမ်းကျင်ပါ
- **[03 - RAG](../03-rag/README.md)** - Retrieval-augmented generation စနစ်များ တည်ဆောက်ပါ
- **[04 - Tools](../04-tools/README.md)** - ဖွင့်ဆိုမှုနှင့် ကိရိယာလိုက်လိုက်မှု ကို တည်ဆောက်ပါ
- **[05 - MCP](../05-mcp/README.md)** - Model Context Protocol ပေါင်းစပ်ပါ

အုပ်စုတိုင်း၏ README တွင် ဤတွင် စမ်းသပ်ထားသည့် အယူအဆအသေးစိတ်ကို ဖော်ပြထားသည်။

---

**လမ်းညွှန်:** [← နောက်သို့](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**အချက်ပြချက်**  
ဤစာရွက်ကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) ဖြင့် ဘာသာပြန်ထားပါသည်။ တိကျမှုအတွက် ကြိုးစားသည်ဖြစ်သော်လည်း ကွန်ပျူတာအလိုအလျောက် ဘာသာပြန်ချက်များတွင် အမှားများ သို့မဟုတ် မှန်ကန်မှုမရှိမှုများ ဖြစ်ပေါ်နိုင်ကြောင်း သတိပြုပါရန် လိုအပ်ပါသည်။ မူရင်းစာရွက်ကို ဒေသဆိုင်ရာဘာသာဖြင့်သာ တရားဝင်အရင်းအမြစ်အဖြစ် ယူဆရန် ပိုမို အရေးကြီးပါသည်။ အရေးကြီးသော အချက်အလက်များအတွက် လူ့ဘာသာပြန်ပညာရှင်ဖြင့် ဘာသာပြန်ခြင်းကို အကြံပြုပါသည်။ ဤဘာသာပြန်ချက်ကို အသုံးပြုရာမှ ဖြစ်ပေါ်သော နားလည်မှုမှားခြင်းများ သို့မဟုတ် မှားဖတ်ခြင်းများအတွက် ကျွန်ုပ်တို့ တာဝန်မခံပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->