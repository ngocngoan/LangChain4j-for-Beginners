# LangChain4j ایپلیکیشنز کی ٹیسٹنگ

## فہرست مضامین

- [فوری آغاز](../../../docs)
- [ٹیسٹ کیا شامل ہیں](../../../docs)
- [ٹیسٹ چلانا](../../../docs)
- [VS کوڈ میں ٹیسٹ چلانا](../../../docs)
- [ٹیسٹنگ کے پیٹرنز](../../../docs)
- [ٹیسٹنگ کا فلسفہ](../../../docs)
- [اگلے اقدامات](../../../docs)

یہ گائیڈ آپ کو ان ٹیسٹس سے گزارتا ہے جو دکھاتے ہیں کہ AI ایپلیکیشنز کو API کیز یا بیرونی خدمات کی ضرورت کے بغیر کیسے ٹیسٹ کیا جائے۔

## فوری آغاز

تمام ٹیسٹس کو ایک کمانڈ سے چلائیں:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/ur/test-results.ea5c98d8f3642043.webp" alt="کامیاب ٹیسٹ کے نتائج" width="800"/>

*کامیاب ٹیسٹ انجام دہی میں تمام ٹیسٹ بغیر کسی ناکامی کے پاس ہو رہے ہیں*

## ٹیسٹ کیا شامل ہیں

یہ کورس **یونٹ ٹیسٹس** پر مرکوز ہے جو مقامی طور پر چلتے ہیں۔ ہر ٹیسٹ ایک مخصوص LangChain4j تصور کو الگ تھلگ دکھاتا ہے۔

<img src="../../../translated_images/ur/testing-pyramid.2dd1079a0481e53e.webp" alt="ٹیسٹنگ کا مثلث" width="800"/>

*ٹیسٹنگ کے مثلث میں یونٹ ٹیسٹس (تیز، الگ تھلگ)، انٹیگریشن ٹیسٹس (حقیقی کمپونینٹس)، اور اینڈ ٹو اینڈ ٹیسٹس کے درمیان توازن دکھایا گیا ہے۔ یہ تربیت یونٹ ٹیسٹنگ پر محیط ہے۔*

| ماڈیول | ٹیسٹس | توجہ | اہم فائلیں |
|--------|-------|-------|-----------|
| **00 - فوری آغاز** | 6 | پرامپٹ ٹیمپلیٹس اور متغیرات کی تبدیلی | `SimpleQuickStartTest.java` |
| **01 - تعارف** | 8 | گفتگو کی یادداشت اور ریاستی چیٹ | `SimpleConversationTest.java` |
| **02 - پرامپٹ انجینئرنگ** | 12 | GPT-5.2 پیٹرنز، جوش کی سطحیں، منظم آؤٹ پٹ | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | دستاویز انٹیک، ایمبیڈنگز، تشابہی تلاش | `DocumentServiceTest.java` |
| **04 - ٹولز** | 12 | فنکشن کالنگ اور ٹول چیننگ | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | ماڈل کانٹیکسٹ پروٹوکول اسٹڈیئو ٹرانسپورٹ کے ساتھ | `SimpleMcpTest.java` |

## ٹیسٹ چلانا

**روٹ سے تمام ٹیسٹس چلائیں:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**کسی مخصوص ماڈیول کے ٹیسٹس چلائیں:**

**Bash:**
```bash
cd 01-introduction && mvn test
# یا جڑ سے
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# یا روٹ سے
mvn --% test -pl 01-introduction
```

**ایک مخصوص ٹیسٹ کلاس چلائیں:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**کسی مخصوص ٹیسٹ میتھڈ کو چلائیں:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#گفتگو کی تاریخ کو برقرار رکھنا چاہیے
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#گفتگو کی تاریخ کو برقرار رکھنا چاہئے
```


## VS کوڈ میں ٹیسٹ چلانا

اگر آپ Visual Studio Code استعمال کر رہے ہیں، تو Test Explorer ایک گرافیکل انٹرفیس فراہم کرتا ہے جہاں سے ٹیسٹس کو چلایا اور ڈیبگ کیا جا سکتا ہے۔

<img src="../../../translated_images/ur/vscode-testing.f02dd5917289dced.webp" alt="VS کوڈ ٹیسٹ ایکسپلورر" width="800"/>

*VS کوڈ ٹیسٹ ایکسپلورر مختلف جاوا ٹیسٹ کلاسز اور انفرادی ٹیسٹ میتھڈز کو ٹیسٹ درخت میں دکھا رہا ہے*

**VS کوڈ میں ٹیسٹس چلانے کے لیے:**

1. ایکٹیویٹی بار میں بییکر آئیکن پر کلک کر کے Test Explorer کھولیں
2. تمام ماڈیولز اور ٹیسٹ کلاسز دیکھنے کے لیے ٹیسٹ درخت کو بڑھائیں
3. کسی بھی ٹیسٹ کے ساتھ چلائیں کے بٹن پر کلک کر کے اسے انفرادی طور پر چلائیں
4. پوری سُوٹ چلانے کے لیے "Run All Tests" پر کلک کریں
5. کسی بھی ٹیسٹ پر رائٹ کلک کر کے "Debug Test" منتخب کریں تاکہ بریک پوائنٹس لگا کر کوڈ میں قدم بہ قدم جائیں

Test Explorer پاس ہونے والے ٹیسٹس کے لیے سبز چیک مارکس دکھاتا ہے اور ناکامی کی صورت میں تفصیلی خرابی پیغامات فراہم کرتا ہے۔

## ٹیسٹنگ کے پیٹرنز

### پیٹرن 1: پرامپٹ ٹیمپلیٹس کی ٹیسٹنگ

سب سے آسان پیٹرن پرامپٹ ٹیمپلیٹس کی ٹیسٹنگ ہے بغیر کسی AI ماڈل کو کال کیے۔ آپ تصدیق کرتے ہیں کہ متغیر کی تبدیلی صحیح طریقے سے کام کر رہی ہے اور پرامپٹ متوقع فارمیٹ میں ہیں۔

<img src="../../../translated_images/ur/prompt-template-testing.b902758ddccc8dee.webp" alt="پرامپٹ ٹیمپلیٹ ٹیسٹنگ" width="800"/>

*پرامپٹ ٹیمپلیٹس کی ٹیسٹنگ، متغیر تبدیلی کے عمل کو دکھاتے ہوئے: ٹیمپلیٹ میں پلیس ہولڈرز → اقدار لگائی گئیں → فارمیٹ شدہ آؤٹ پٹ کی تصدیق*

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

یہ ٹیسٹ `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java` میں موجود ہے۔

**چلائیں:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#جانچ_پرومپٹ_سانچے_کی_فارمیٹنگ
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#ٹیسٹ پرامپٹ ٹیمپلیٹ فارمیٹنگ
```


### پیٹرن 2: زبان کے ماڈلز کی موکنگ

گفتگو کی منطق ٹیسٹ کرتے وقت Mockito کا استعمال کریں تاکہ جعلی ماڈلز بنائیں جو پہلے سے طے شدہ جوابات دیں۔ یہ ٹیسٹس کو تیز، مفت، اور طے شدہ بناتا ہے۔

<img src="../../../translated_images/ur/mock-vs-real.3b8b1f85bfe6845e.webp" alt="موک بمقابلہ حقیقی API موازنہ" width="800"/>

*موازنہ دکھاتے ہوئے کہ ٹیسٹنگ کے لیے موکس کیوں پسندیدہ ہیں: وہ تیز، مفت، طے شدہ اور API کیز کی ضرورت نہیں رکھتے*

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
        assertThat(history).hasSize(6); // 3 صارف + 3 AI پیغامات
    }
}
```

یہ پیٹرن `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` میں پایا جاتا ہے۔ موک مسلسل رویہ کو یقینی بناتا ہے تاکہ آپ یادداشت کے انتظام کی درستگی کی تصدیق کر سکیں۔

### پیٹرن 3: گفتگو کی الگ تھلگ ٹیسٹنگ

گفتگو کی یادداشت کو متعدد صارفین کو الگ رکھنا ہوتا ہے۔ یہ ٹیسٹ تصدیق کرتا ہے کہ گفتگو کے سیاق و سباق نہیں ملتے۔

<img src="../../../translated_images/ur/conversation-isolation.e00336cf8f7a3e3f.webp" alt="گفتگو کی الگ تھلگ" width="800"/>

*گفتگو کی الگ تھلگ ٹیسٹنگ دکھاتے ہوئے مختلف صارفین کے لیے علیحدہ یادداشتیں تاکہ سیاق و سباق کا امتزاج نہ ہو*

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

ہر گفتگو اپنی آزاد تاریخ رکھتی ہے۔ پیداواری نظام میں یہ الگ تھلگ کئی صارفین کی ایپلیکیشنز کے لیے انتہاﺋی اہم ہے۔

### پیٹرن 4: ٹولز کی آزادانہ ٹیسٹنگ

ٹولز وہ فنکشنز ہیں جنہیں AI کال کر سکتا ہے۔ انہیں براہِ راست ٹیسٹ کریں تاکہ یہ یقینی بن سکے کہ وہ AI کے فیصلوں سے آزاد درست کام کرتے ہیں۔

<img src="../../../translated_images/ur/tools-testing.3e1706817b0b3924.webp" alt="ٹولز کی ٹیسٹنگ" width="800"/>

*ٹولز کی آزادانہ ٹیسٹنگ، موک ٹول کے نفاذ کو بغیر AI کے کال کے دکھاتے ہوئے تاکہ کاروباری منطق کی تصدیق ہو*

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

یہ ٹیسٹس `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` سے ہیں جو AI کی شرکت کے بغیر ٹول منطق کی توثیق کرتے ہیں۔ چیننگ کی مثال دکھاتی ہے کہ ایک ٹول کا آؤٹ پٹ دوسرے کے ان پٹ میں کیسے جاتا ہے۔

### پیٹرن 5: ان میموری RAG ٹیسٹنگ

RAG سسٹمز عام طور پر ویکٹر ڈیٹا بیسز اور ایمبیڈنگ سروسز کی ضرورت رکھتے ہیں۔ ان میموری پیٹرن آپ کو پورا پائپ لائن بغیر بیرونی انحصار کے ٹیسٹ کرنے دیتا ہے۔

<img src="../../../translated_images/ur/rag-testing.ee7541b1e23934b1.webp" alt="ان میموری RAG ٹیسٹنگ" width="800"/>

*ان میموری RAG ٹیسٹنگ ورک فلو دکھاتے ہوئے دستاویز کی تشریح، ایمبیڈنگ اسٹوریج، اور تشابہی تلاش بغیر ڈیٹا بیس کی ضرورت کے*

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

یہ ٹیسٹ `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` سے ہے جو ایک دستاویز میموری میں بناتا ہے اور چنکنگ و میٹا ڈیٹا کی ہینڈلنگ کی تصدیق کرتا ہے۔

### پیٹرن 6: MCP انٹیگریشن ٹیسٹنگ

MCP ماڈیول ماڈل کانٹیکسٹ پروٹوکول کی انٹیگریشن کو stdio ٹرانسپورٹ کے ذریعے ٹیسٹ کرتا ہے۔ یہ ٹیسٹس تصدیق کرتے ہیں کہ آپکی ایپلیکیشن MCP سرورز کو subprocess کے طور پر چلا سکتی ہے اور ان سے رابطہ کرسکتی ہے۔

ٹیسٹس `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` میں MCP کلائنٹ رویہ کی توثیق کرتے ہیں۔

**انہیں چلائیں:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```


## ٹیسٹنگ کا فلسفہ

اپنا کوڈ ٹیسٹ کریں، AI کو نہیں۔ آپ کے ٹیسٹس وہ کوڈ ویریفائی کریں جو آپ لکھتے ہیں، یہ چیک کرکے کہ پرامپٹس کیسے بنے، یادداشت کیسے سنبھالی گئی، اور ٹولز کیسے چلائے جاتے ہیں۔ AI جوابات مختلف ہو سکتے ہیں اور ٹیسٹ دعووں کا حصہ نہیں ہونے چاہئیں۔ اپنے آپ سے پوچھیں کہ کیا آپ کا پرامپٹ ٹیمپلیٹ متغیرات کو صحیح طریقے سے بدل رہا ہے، نہ کہ کیا AI صحیح جواب دیتا ہے۔

زبان کے ماڈلز کے لیے موک استعمال کریں۔ یہ خارجی انحصار ہیں جو سست، مہنگے، اور غیر معین ہوتے ہیں۔ موکنگ ٹیسٹس کو سیکنڈز کی بجائے ملی سیکنڈز میں تیز، مفت اور ہر بار یکساں نتیجہ فراہم کرتی ہے۔

ٹیسٹس کو آزاد رکھیں۔ ہر ٹیسٹ اپنا ڈیٹا سیٹ کرے، دوسرے ٹیسٹس پر انحصار نہ کرے، اور اپنے بعد صفائی کرے۔ ٹیسٹس چلنے کے ترتیب سے قطع نظر کامیاب ہونے چاہئیں۔

خوشی کی راہ سے آگے کنارے کے کیسز بھی ٹیسٹ کریں۔ خالی ان پٹس، بہت بڑے ان پٹس، خاص حروف، غلط پیرامیٹرز، اور حد بندی کی حالتیں آزمائیں۔ یہ اکثر کیڑے دکھاتے ہیں جو عام استعمال پر سامنے نہیں آتے۔

وضاحتی نام استعمال کریں۔ `shouldMaintainConversationHistoryAcrossMultipleMessages()` کا موازنہ `test1()` سے کریں۔ پہلا بالکل بتاتا ہے کہ کیا ٹیسٹ ہو رہا ہے، جس سے ناکامی کی صورت میں ڈیبگ کرنا آسان ہوتا ہے۔

## اگلے اقدامات

اب جب کہ آپ ٹیسٹنگ پیٹرنز سمجھ گئے ہیں، ہر ماڈیول میں گہرائی سے جائیں:

- **[00 - فوری آغاز](../00-quick-start/README.md)** - پرامپٹ ٹیمپلیٹ کی بنیادی باتوں سے آغاز کریں
- **[01 - تعارف](../01-introduction/README.md)** - گفتگو کی یادداشت کے انتظام کو سیکھیں
- **[02 - پرامپٹ انجینئرنگ](../02-prompt-engineering/README.md)** - GPT-5.2 پرامپٹنگ پیٹرنز میں مہارت حاصل کریں
- **[03 - RAG](../03-rag/README.md)** - بازیافت سے متعلق جنریٹیو سسٹمز بنائیں
- **[04 - ٹولز](../04-tools/README.md)** - فنکشن کالنگ اور ٹول چینز نافذ کریں
- **[05 - MCP](../05-mcp/README.md)** - ماڈل کانٹیکسٹ پروٹوکول انٹیگریٹ کریں

ہر ماڈیول کا README یہاں ٹیسٹ کیے گئے تصورات کی تفصیلی وضاحت فراہم کرتا ہے۔

---

**نیویگیشن:** [← مرکزی صفحے پر واپس](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**توضیح**:  
یہ دستاویز AI ترجمہ سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کے ذریعے ترجمہ کی گئی ہے۔ اگرچہ ہم درستگی کی کوشش کرتے ہیں، براہ کرم اس بات سے آگاہ رہیں کہ خودکار ترجمے میں غلطیاں یا عدم صحت ہو سکتی ہے۔ اصل دستاویز اپنی مادری زبان میں قابل اعتماد ماخذ سمجھی جانی چاہیے۔ اہم معلومات کے لیے پیشہ ورانہ انسانی ترجمہ کی سفارش کی جاتی ہے۔ اس ترجمے کے استعمال سے پیدا ہونے والی کسی بھی غلط فہمی یا غلط تشریح کی ذمہ داری ہم پر عائد نہیں ہوتی۔
<!-- CO-OP TRANSLATOR DISCLAIMER END -->