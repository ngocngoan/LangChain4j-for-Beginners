# LangChain4j ایپلیکیشنز کی جانچ

## فہرست مضامین

- [جلدی شروع کریں](../../../docs)
- [ٹیسٹ کیا شامل کرتے ہیں](../../../docs)
- [ٹیسٹ چلانا](../../../docs)
- [VS کوڈ میں ٹیسٹ چلانا](../../../docs)
- [جانچ کے نمونے](../../../docs)
- [جانچ کا فلسفہ](../../../docs)
- [اگلے اقدامات](../../../docs)

یہ رہنما آپ کو ان ٹیسٹوں سے گزارتا ہے جو یہ ظاہر کرتے ہیں کہ AI ایپلیکیشنز کو API کیز یا بیرونی خدمات کی ضرورت کے بغیر کیسے ٹیسٹ کیا جائے۔

## جلدی شروع کریں

تمام ٹیسٹ ایک ہی کمانڈ سے چلائیں:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

جب تمام ٹیسٹ کامیابی سے گزرتے ہیں، تو آپ کو نیچے دی گئی اسکرین شاٹ جیسا نتیجہ دیکھنا چاہیے — ٹیسٹ صفر نقص کے ساتھ چلتے ہیں۔

<img src="../../../translated_images/ur/test-results.ea5c98d8f3642043.webp" alt="کامیاب ٹیسٹ کے نتائج" width="800"/>

*کامیاب ٹیسٹ نفاذ دکھا رہا ہے تمام ٹیسٹ صفر ناکامی کے ساتھ پاس ہو رہے ہیں*

## ٹیسٹ کیا شامل کرتے ہیں

یہ کورس **یونٹ ٹیسٹ** پر مرکوز ہے جو مقامی طور پر چلتے ہیں۔ ہر ٹیسٹ ایک مخصوص LangChain4j تصور کو الگ تھلگ دکھاتا ہے۔ نیچے دی گئی جانچ پائرمیڈ دکھاتی ہے کہ یونٹ ٹیسٹ کہاں فٹ بیٹھتے ہیں — یہ آپ کی باقی ٹیسٹنگ حکمت عملی کے لیے تیز، قابل اعتماد بنیاد فراہم کرتے ہیں۔

<img src="../../../translated_images/ur/testing-pyramid.2dd1079a0481e53e.webp" alt="جانچ کا پائرمیڈ" width="800"/>

*جانچ کا پائرمیڈ یونٹ ٹیسٹ (تیز، الگ تھلگ)، انٹیگریشن ٹیسٹ (حقیقی اجزاء)، اور اینڈ ٹو اینڈ ٹیسٹ کے درمیان توازن دکھا رہا ہے۔ یہ تربیت یونٹ ٹیسٹنگ پر محیط ہے۔*

| ماڈیول | ٹیسٹ | توجہ مرکوز | اہم فائلیں |
|--------|-------|-------|-----------|
| **00 - جلدی شروع کریں** | 6 | پرامپٹ ٹیمپلیٹس اور متغیر تبدیلی | `SimpleQuickStartTest.java` |
| **01 - تعارف** | 8 | گفتگو کی یادداشت اور ریاستی چیٹ | `SimpleConversationTest.java` |
| **02 - پرامپٹ انجینئرنگ** | 12 | GPT-5.2 پیٹرنز، خواہش کی سطحیں، ساخت شدہ آؤٹ پٹ | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | دستاویزات کی انٹیک، ایمبیڈنگز، مماثلت کی تلاش | `DocumentServiceTest.java`` |
| **04 - ٹولز** | 12 | فنکشن کالنگ اور ٹول چیننگ | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | ماڈل کانٹیکسٹ پروٹوکول stdio ٹرانسپورٹ کے ساتھ | `SimpleMcpTest.java` |

## ٹیسٹ چلانا

**روٹ سے تمام ٹیسٹ چلائیں:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**مخصوص ماڈیول کے ٹیسٹ چلائیں:**

**Bash:**
```bash
cd 01-introduction && mvn test
# یا جڑ سے
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# یا جڑ سے
mvn --% test -pl 01-introduction
```

**ایک واحد ٹیسٹ کلاس چلائیں:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**مخصوص ٹیسٹ میتھڈ چلائیں:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#گفتگو کی تاریخ کو برقرار رکھنا چاہیے
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#گفتگو کی تاریخ کو برقرار رکھنا چاہیے
```

## VS کوڈ میں ٹیسٹ چلانا

اگر آپ Visual Studio Code استعمال کر رہے ہیں، تو Test Explorer ٹیسٹ چلانے اور ڈیبگ کرنے کے لیے گرافیکل انٹرفیس مہیا کرتا ہے۔

<img src="../../../translated_images/ur/vscode-testing.f02dd5917289dced.webp" alt="VS کوڈ ٹیسٹ ایکسپلورر" width="800"/>

*VS کوڈ ٹیسٹ ایکسپلورر ٹیسٹ ٹری دکھا رہا ہے جس میں تمام جاوا ٹیسٹ کلاسز اور انفرادی ٹیسٹ میتھڈز موجود ہیں*

**VS کوڈ میں ٹیسٹ چلانے کے لیے:**

1. ایکٹیویٹی بار میں بیکر آئیکن پر کلک کرکے Test Explorer کھولیں
2. تمام ماڈیولز اور ٹیسٹ کلاسز دیکھنے کے لیے ٹیسٹ ٹری کو پھیلائیں
3. کسی بھی ٹیسٹ کے ساتھ پلے بٹن پر کلک کرکے اسے الگ سے چلائیں
4. "Run All Tests" پر کلک کرکے پوری سُوٹ چلائیں
5. کسی بھی ٹیسٹ پر رائٹ کلک کرکے "Debug Test" منتخب کریں تاکہ بریک پوائنٹس سیٹ کر کے کوڈ میں قدم بہ قدم جائیں

Test Explorer پاس ہونے والے ٹیسٹوں کے لیے سبز نشانیاں دکھاتا ہے اور ناکام ہونے پر تفصیلی نقص پیغامات دیتا ہے۔

## جانچ کے نمونے

### نمونہ 1: پرامپٹ ٹیمپلیٹ کی جانچ

سب سے آسان نمونہ پرامپٹ ٹیمپلیٹس کو AI ماڈل کو کال کیے بغیر ٹیسٹ کرتا ہے۔ آپ تصدیق کرتے ہیں کہ متغیرات کی تبدیلی صحیح کام کر رہی ہے اور پرامپٹس متوقع انداز میں فارمیٹ ہوئے ہیں۔

<img src="../../../translated_images/ur/prompt-template-testing.b902758ddccc8dee.webp" alt="پرامپٹ ٹیمپلیٹ کی جانچ" width="800"/>

*پرامپٹ ٹیمپلیٹس کی جانچ دکھا رہی ہے متغیر تبدیلی کا عمل: پلیس ہولڈرز کے ساتھ ٹیمپلیٹ → اقدار کا اطلاق → فارمیٹ شدہ نتیجہ کی تصدیق*

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

یہ ٹیسٹ `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java` میں ہے۔

**چلائیں:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#ٹیسٹ پرامپٹ ٹیمپلیٹ کی فارمیٹنگ
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#ٹیسٹ پرامپٹ ٹیمپلیٹ کی ترتیب
```

### نمونہ 2: زبان کے ماڈلز کی موکنگ

جب گفتگو کے منطق کو ٹیسٹ کریں، تو Mockito استعمال کریں جعلی ماڈلز بنانے کے لیے جو متعین جوابات واپس دیتے ہیں۔ اس سے ٹیسٹ تیز، مفت، اور قطعی ہو جاتے ہیں۔

<img src="../../../translated_images/ur/mock-vs-real.3b8b1f85bfe6845e.webp" alt="موک اور اصلی API کا موازنہ" width="800"/>

*موازنہ دکھا رہا ہے کہ ٹیسٹنگ کے لیے موکس کیوں ترجیح دی جاتی ہیں: یہ تیز، مفت، قطعی ہیں اور API کیز کی ضرورت نہیں ہوتی*

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
        assertThat(history).hasSize(6); // 3 صارف + 3 اے آئی پیغامات
    }
}
```

یہ نمونہ `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` میں ہے۔ موک مستقل رویہ یقینی بناتا ہے تاکہ آپ تصدیق کر سکیں کہ میموری کا انتظام صحیح ہے۔

### نمونہ 3: گفتگو کی علیحدگی کی جانچ

گفتگو کی یادداشت کو متعدد صارفین کو الگ رکھنا چاہیے۔ یہ ٹیسٹ تصدیق کرتا ہے کہ گفتگو کے متن آپس میں مکس نہیں ہوتے۔

<img src="../../../translated_images/ur/conversation-isolation.e00336cf8f7a3e3f.webp" alt="گفتگو کی علیحدگی" width="800"/>

*گفتگو کی علیحدگی کی جانچ دکھا رہی ہے کہ مختلف صارفین کے لیے علیحدہ یادداشتیں اس لیے کہ سیاق و سباق مکس نہ ہو*

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

ہر گفتگو اپنی آزاد تاریخ رکھتی ہے۔ پروڈکشن سسٹمز میں یہ علیحدگی کثیر صارف ایپلیکیشنز کے لیے نہایت اہم ہے۔

### نمونہ 4: ٹولز کی آزادانہ جانچ

ٹولز وہ فنکشنز ہیں جنہیں AI کال کر سکتا ہے۔ انہیں براہ راست ٹیسٹ کریں تاکہ یہ یقینی بنایا جا سکے کہ وہ AI کے فیصلوں سے قطع نظر صحیح کام کر رہے ہیں۔

<img src="../../../translated_images/ur/tools-testing.3e1706817b0b3924.webp" alt="ٹولز کی جانچ" width="800"/>

*ٹولز کی آزادانہ جانچ دکھا رہی ہے موک ٹول کا نفاذ بغیر AI کال کے کاروباری منطق کی تصدیق کے لیے*

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

یہ ٹیسٹ `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` سے ہیں جو AI مداخلت کے بغیر ٹول منطق کی توثیق کرتے ہیں۔ چیننگ کی مثال دکھاتی ہے کہ کس طرح ایک ٹول کی آؤٹ پٹ دوسرے کے ان پٹ میں جاتی ہے۔

### نمونہ 5: ان-میموری RAG ٹیسٹنگ

RAG سسٹمز روایتی طور پر ویکٹر ڈیٹابیسز اور ایمبیڈنگ سروسز کا تقاضا کرتے ہیں۔ ان-میموری نمونہ آپ کو بغیر بیرونی انحصار کے پوری پائپ لائن ٹیسٹ کرنے دیتا ہے۔

<img src="../../../translated_images/ur/rag-testing.ee7541b1e23934b1.webp" alt="ان-میموری RAG ٹیسٹنگ" width="800"/>

*ان-میموری RAG ٹیسٹنگ ورک فلو دکھا رہا ہے دستاویز کی تجزیہ، ایمبیڈنگ اسٹوریج، اور مماثلت کی تلاش بغیر ڈیٹابیس کی ضرورت*

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

یہ ٹیسٹ `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` میں ہے جو دستاویز کو میموری میں بناتا ہے اور چنکنگ اور میٹا ڈیٹا ہینڈلنگ کی تصدیق کرتا ہے۔

### نمونہ 6: MCP انٹیگریشن ٹیسٹنگ

MCP ماڈیول ماڈل کانٹیکسٹ پروٹوکول کی انٹیگریشن stdio ٹرانسپورٹ کے ساتھ ٹیسٹ کرتا ہے۔ یہ ٹیسٹ اس بات کی تصدیق کرتے ہیں کہ آپ کی ایپلیکیشن MCP سرورز کو بطور subprocess پیدا کر کے مواصلت کر سکتی ہے۔

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` میں ٹیسٹ MCP کلائنٹ کے رویے کی توثیق کرتے ہیں۔

**انہیں چلائیں:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## جانچ کا فلسفہ

اپنا کوڈ ٹیسٹ کریں، AI کو نہیں۔ آپ کے ٹیسٹ اس کوڈ کو درست ثابت کرنے چاہئیں جو آپ لکھتے ہیں، یہ دیکھ کر کہ پرامپٹس کیسے بنائے جاتے ہیں، میموری کیسے مینیج کی جاتی ہے، اور ٹولز کیسے چلتے ہیں۔ AI کے جوابات مختلف ہو سکتے ہیں اور انہیں ٹیسٹ کے دعووں کا حصہ نہیں ہونا چاہیے۔ اپنے آپ سے پوچھیں کہ آیا آپ کا پرامپٹ ٹیمپلیٹ متغیرات کو صحیح طریقے سے بدل رہا ہے، نہ کہ AI صحیح جواب دے رہا ہے یا نہیں۔

زبان کے ماڈلز کے لیے موکس استعمال کریں۔ وہ بیرونی انحصارات ہیں جو سست، مہنگے، اور غیر قطعی ہوتے ہیں۔ موکنگ ٹیسٹ کو سیکنڈز کی بجائے ملی سیکنڈز میں تیز، مفت اور ہر بار ایک جیسے نتائج فراہم کرتی ہے۔

ٹیسٹوں کو خودمختار رکھیں۔ ہر ٹیسٹ اپنا ڈیٹا تیار کرے، دوسرے ٹیسٹوں پر انحصار نہ کرے، اور اپنے بعد صفائی کرے۔ ٹیسٹوں کو کرانے کے حکم سے قطع نظر کامیاب ہونا چاہیے۔

خوشگوار راستے سے آگے کنارے کے کیسز ٹیسٹ کریں۔ خالی ان پٹ، بہت بڑے ان پٹ، خاص کردار، غلط پیرا میٹرز، اور بارڈر کنڈیشنز آزمائیں۔ یہ اکثر وہ بگ ظاہر کرتے ہیں جو عام استعمال میں نہیں نکلتے۔

تشریحی نام استعمال کریں۔ `shouldMaintainConversationHistoryAcrossMultipleMessages()` کا موازنہ کریں `test1()` سے۔ پہلا آپ کو بالکل بتاتا ہے کہ کیا ٹیسٹ ہو رہا ہے، جو ناکامی کی ڈیبگنگ کو آسان بناتا ہے۔

## اگلے اقدامات

اب جب آپ جانچ کے نمونوں کو سمجھ گئے ہیں، تو ہر ماڈیول میں مزید گہرائی میں جائیں:

- **[00 - جلدی شروع کریں](../00-quick-start/README.md)** - پرامپٹ ٹیمپلیٹ کی بنیادی باتوں سے شروع کریں
- **[01 - تعارف](../01-introduction/README.md)** - گفتگو کی یادداشت کا انتظام سیکھیں
- **[02 - پرامپٹ انجینئرنگ](../02-prompt-engineering/README.md)** - GPT-5.2 کے پرامپٹنگ پیٹرنز میں مہارت حاصل کریں
- **[03 - RAG](../03-rag/README.md)** - بازیافت سے بڑھا ہوا جنریشن سسٹمز بنائیں
- **[04 - ٹولز](../04-tools/README.md)** - فنکشن کالنگ اور ٹول چینز کو نافذ کریں
- **[05 - MCP](../05-mcp/README.md)** - ماڈل کانٹیکسٹ پروٹوکول کو انٹیگریٹ کریں

ہر ماڈیول کی README یہاں ٹیسٹ کیے گئے تصورات کی تفصیلی وضاحت فراہم کرتی ہے۔

---

**رہنمائی:** [← مرکزی صفحہ پر واپس جائیں](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ڈس کلیمر**:  
اس دستاویز کا ترجمہ AI ترجمہ سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کے ذریعے کیا گیا ہے۔ اگرچہ ہم درستگی کے لیے کوشاں ہیں، براہ کرم اس بات کا خیال رکھیں کہ خودکار ترجمے میں غلطیاں یا نقائص ہو سکتے ہیں۔ اصل دستاویز اپنی مادری زبان میں معتبر ماخذ سمجھا جائے گا۔ اہم معلومات کے لیے پیشہ ورانہ انسانی ترجمہ کی سفارش کی جاتی ہے۔ اس ترجمے کے استعمال سے ہونے والی کسی بھی غلط فہمی یا بدفہمی کی ذمہ داری ہم پر عائد نہیں ہوتی۔
<!-- CO-OP TRANSLATOR DISCLAIMER END -->