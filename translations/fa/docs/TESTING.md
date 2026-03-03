# تست برنامه‌های LangChain4j

## فهرست مطالب

- [شروع سریع](../../../docs)
- [موضوعات پوشش داده شده در تست‌ها](../../../docs)
- [اجرای تست‌ها](../../../docs)
- [اجرای تست‌ها در VS Code](../../../docs)
- [الگوهای تست](../../../docs)
- [فلسفه تست](../../../docs)
- [گام‌های بعدی](../../../docs)

این راهنما شما را با تست‌هایی آشنا می‌کند که نشان می‌دهند چگونه برنامه‌های هوش مصنوعی را بدون نیاز به کلیدهای API یا سرویس‌های خارجی تست کنید.

## شروع سریع

تمام تست‌ها را با یک دستور اجرا کنید:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

وقتی همه تست‌ها با موفقیت اجرا شدند، باید خروجی مانند تصویر زیر ببینید — تست‌ها بدون خطا اجرا شده‌اند.

<img src="../../../translated_images/fa/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*اجرای موفق تست‌ها که همه تست‌ها را بدون خطا نشان می‌دهد*

## موضوعات پوشش داده شده در تست‌ها

این دوره بر **تست‌های واحد** که به صورت محلی اجرا می‌شوند تمرکز دارد. هر تست یک مفهوم خاص از LangChain4j را به صورت جداگانه نشان می‌دهد. هرم تست زیر محل قرارگیری تست‌های واحد را نشان می‌دهد — آنها پایه‌ای سریع و قابل اعتماد هستند که بقیه استراتژی تست شما بر اساس آن ساخته می‌شود.

<img src="../../../translated_images/fa/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*هرم تست که تعادل بین تست‌های واحد (سریع، جداگانه)، تست‌های یکپارچه‌سازی (کامپوننت‌های واقعی) و تست‌های انتها-به-انتها را نشان می‌دهد. این آموزش بر تست واحد متمرکز است.*

| ماژول | تست‌ها | محور تمرکز | فایل‌های کلیدی |
|--------|-------|-------|-----------|
| **00 - شروع سریع** | 6 | قالب‌های پرسش و جایگزینی متغیرها | `SimpleQuickStartTest.java` |
| **01 - معرفی** | 8 | حافظه مکالمه و چت حالت‌مند | `SimpleConversationTest.java` |
| **02 - مهندسی پرسش** | 12 | الگوهای GPT-5.2، سطوح اشتیاق، خروجی ساختاریافته | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | دریافت اسناد، تعبیه‌ها، جستجوی مشابهت | `DocumentServiceTest.java` |
| **04 - ابزارها** | 12 | فراخوانی تابع و زنجیره‌سازی ابزارها | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | پروتکل زمینه مدل با انتقال stdio | `SimpleMcpTest.java` |

## اجرای تست‌ها

**اجرای همه تست‌ها از ریشه:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**اجرای تست‌های یک ماژول خاص:**

**Bash:**
```bash
cd 01-introduction && mvn test
# یا از ریشه
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# یا از ریشه
mvn --% test -pl 01-introduction
```

**اجرای یک کلاس تست خاص:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**اجرای یک متد تست مشخص:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#باید سابقه مکالمه را حفظ کند
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#آیا باید سابقه گفتگو حفظ شود؟
```

## اجرای تست‌ها در VS Code

اگر از Visual Studio Code استفاده می‌کنید، Test Explorer یک رابط گرافیکی برای اجرای تست‌ها و اشکال‌زدایی ارائه می‌دهد.

<img src="../../../translated_images/fa/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*Test Explorer در VS Code درخت تست‌ها را با همه کلاس‌های تست جاوا و متدهای تست جداگانه نشان می‌دهد*

**برای اجرای تست‌ها در VS Code:**

1. تست اکسپلورر را با کلیک بر روی آیکون ارلن آزمایش در نوار فعالیت باز کنید
2. درخت تست را گسترش دهید تا همه ماژول‌ها و کلاس‌های تست را ببینید
3. روی دکمه پخش کنار هر تست کلیک کنید تا به صورت جداگانه اجرا شود
4. روی "Run All Tests" کلیک کنید تا کل مجموعه اجرا شود
5. روی هر تست راست‌کلیک کنید و "Debug Test" را انتخاب کنید تا نقاط قطع تنظیم و کد مرحله‌به‌مرحله اجرا شود

Test Explorer تیک‌های سبز برای تست‌های موفق نشان می‌دهد و پیام‌های خطای دقیق را در زمان شکست تست‌ها ارائه می‌دهد.

## الگوهای تست

### الگو ۱: تست قالب پرسش‌ها

ساده‌ترین الگو قالب پرسش‌ها را بدون فراخوانی هیچ مدل هوش مصنوعی تست می‌کند. شما می‌سنجید که جایگزینی متغیرها به درستی انجام شده و قالب‌ها طبق انتظار فرمت شده‌اند.

<img src="../../../translated_images/fa/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*تست قالب‌های پرسش که جریان جایگزینی متغیرها را نشان می‌دهد: قالب با مکان‌نماها → مقادیر اعمال شده → خروجی فرمت شده تأیید شده*

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

این تست در `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java` قرار دارد.

**برای اجرا:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#قالب‌بندی الگوی درخواست آزمایشی
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#قالب‌بندی آزمون الگوخواسته
```

### الگو ۲: شبیه‌سازی مدل‌های زبانی

در تست منطق مکالمه، از Mockito برای ساخت مدل‌های جعلی استفاده کنید که پاسخ‌های از پیش تعیین‌شده برمی‌گردانند. این موجب سرعت، رایگان بودن و قطعی بودن تست‌ها می‌شود.

<img src="../../../translated_images/fa/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*مقایسه‌ای که نشان می‌دهد چرا شبیه‌سازی‌ها برای تست ترجیح داده می‌شوند: سریع، رایگان، قطعی و بدون نیاز به کلیدهای API هستند*

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
        assertThat(history).hasSize(6); // ۳ پیام کاربر + ۳ پیام هوش مصنوعی
    }
}
```

این الگو در `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` ظاهر می‌شود. شبیه‌سازی رفتار یکسان را تضمین می‌کند تا بتوانید مدیریت حافظه را به درستی تأیید کنید.

### الگو ۳: تست جداسازی مکالمه

حافظه مکالمه باید کاربران مختلف را جدا نگه دارد. این تست تأیید می‌کند که مکالمات زمینه‌ها را مخلوط نمی‌کنند.

<img src="../../../translated_images/fa/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*تست جداسازی مکالمه که فروشگاه‌های حافظه جداگانه برای کاربران مختلف برای جلوگیری از مخلوط شدن زمینه‌ها را نشان می‌دهد*

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

هر مکالمه تاریخچه مستقل خود را حفظ می‌کند. در سیستم‌های تولید، این جداسازی برای برنامه‌های چند کاربره حیاتی است.

### الگو ۴: تست ابزارها به صورت مستقل

ابزارها توابعی هستند که هوش مصنوعی می‌تواند فراخوانی کند. آنها را به صورت مستقیم تست کنید تا مطمئن شوید فارغ از تصمیمات هوش مصنوعی درست کار می‌کنند.

<img src="../../../translated_images/fa/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*تست ابزارها به صورت مستقل که اجرای ابزارهای شبیه‌سازی‌شده بدون فراخوانی هوش مصنوعی را برای تأیید منطق کسب‌وکار نشان می‌دهد*

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

این تست‌ها از `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` منطق ابزار را بدون دخالت هوش مصنوعی اعتبارسنجی می‌کنند. مثال زنجیره‌سازی نشان می‌دهد چگونه خروجی ابزار یک ورودی برای دیگری است.

### الگو ۵: تست RAG در حافظه

سیستم‌های RAG معمولاً به پایگاه داده‌های برداری و سرویس‌های تعبیه نیاز دارند. الگوی درون حافظه اجازه می‌دهد کل خط لوله بدون وابستگی‌های خارجی تست شود.

<img src="../../../translated_images/fa/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*روند تست RAG در حافظه که تجزیه سند، ذخیره تعبیه‌ها و جستجوی مشابهت را بدون نیاز به پایگاه داده نشان می‌دهد*

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

این تست از `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` سندی را در حافظه ایجاد می‌کند و تجزیه بخش‌بندی و مدیریت متادیتا را تأیید می‌کند.

### الگو ۶: تست یکپارچه‌سازی MCP

ماژول MCP تست پروتکل زمینه مدل را با استفاده از انتقال stdio انجام می‌دهد. این تست‌ها اطمینان می‌دهند که برنامه شما می‌تواند سرورهای MCP را به عنوان زیرفرآیند اجرا و با آنها ارتباط برقرار کند.

تست‌ها در `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` رفتار کلاینت MCP را اعتبارسنجی می‌کنند.

**برای اجرا:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## فلسفه تست

کد خود را تست کنید، نه هوش مصنوعی را. تست‌های شما باید کدی را که می‌نویسید با بررسی نحوه ساخت پرسش‌ها، مدیریت حافظه و اجرای ابزارها اعتبارسنجی کنند. پاسخ‌های هوش مصنوعی متفاوت هستند و نباید بخشی از تأییدیه‌های تست باشند. از خود بپرسید آیا قالب پرسش شما متغیرها را به درستی جایگزین می‌کند، نه اینکه آیا هوش مصنوعی پاسخ درست می‌دهد.

از شبیه‌سازی‌ها برای مدل‌های زبانی استفاده کنید. آنها وابستگی‌های خارجی هستند که کند، گران و غیرقطعی هستند. شبیه‌سازی تست‌ها را سریع با میلی‌ثانیه به جای ثانیه، رایگان بدون هزینه API و قطعی با همان نتیجه در هر بار اجرا می‌کند.

تست‌ها را مستقل نگه دارید. هر تست باید داده‌های خودش را راه‌اندازی کند، به تست‌های دیگر وابسته نباشد و پس از اجرا پاکسازی کند. تست‌ها باید فارغ از ترتیب اجرا پذیرفته شوند.

موارد حاشیه‌ای فراتر از مسیر خوشحال را تست کنید. ورودی‌های خالی، خیلی بزرگ، کاراکترهای خاص، پارامترهای نامعتبر و شرایط مرزی را امتحان کنید. این موارد اغلب خطاهایی را نشان می‌دهند که استفاده عادی نشان نمی‌دهد.

از نام‌های توصیفی استفاده کنید. مقایسه کنید `shouldMaintainConversationHistoryAcrossMultipleMessages()` با `test1()`. اولی دقیقا می‌گوید چه چیزی تست می‌شود و عیب‌یابی خطاها را بسیار آسان می‌کند.

## گام‌های بعدی

حالا که الگوهای تست را درک کردید، به صورت عمیق‌تر به هر ماژول بپردازید:

- **[00 - شروع سریع](../00-quick-start/README.md)** - شروع با اصول قالب پرسش
- **[01 - معرفی](../01-introduction/README.md)** - یادگیری مدیریت حافظه مکالمه
- **[02 - مهندسی پرسش](../02/prompt-engineering/README.md)** - تسلط بر الگوهای GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - ساخت سیستم‌های تولید تکمیل شده با بازیابی
- **[04 - ابزارها](../04-tools/README.md)** - پیاده‌سازی فراخوانی توابع و زنجیره ابزارها
- **[05 - MCP](../05-mcp/README.md)** - یکپارچه‌سازی پروتکل زمینه مدل

README هر ماژول توضیحات مفصل مفاهیم تست شده در اینجا را ارائه می‌دهد.

---

**ناوبری:** [← بازگشت به اصلی](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**توضیح مسئولیت**:  
این سند با استفاده از سرویس ترجمه ماشینی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما در تلاش برای دقت هستیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است شامل خطاها یا نواقصی باشند. سند اصلی به زبان مادری آن باید به عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حساس، ترجمه حرفه‌ای توسط انسان توصیه می‌شود. ما در قبال هر گونه سوءتفاهم یا تفسیر نادرست ناشی از استفاده از این ترجمه مسئولیتی نداریم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->