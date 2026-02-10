# تست برنامه‌های LangChain4j

## فهرست مطالب

- [شروع سریع](../../../docs)
- [موارد تحت پوشش در تست‌ها](../../../docs)
- [اجرای تست‌ها](../../../docs)
- [اجرای تست‌ها در VS Code](../../../docs)
- [الگوهای تست](../../../docs)
- [فلسفه تست](../../../docs)
- [گام‌های بعدی](../../../docs)

این راهنما شما را از طریق تست‌هایی می‌برد که نشان می‌دهد چگونه می‌توان برنامه‌های هوش مصنوعی را بدون نیاز به کلیدهای API یا سرویس‌های خارجی تست کرد.

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

<img src="../../../translated_images/fa/test-results.ea5c98d8f3642043.webp" alt="نتایج موفقیت‌آمیز تست" width="800"/>

*اجرای موفق تست که نشان می‌دهد همه تست‌ها با صفر خطا عبور کرده‌اند*

## موارد تحت پوشش در تست‌ها

این دوره بر روی **تست‌های واحد** که به‌صورت محلی اجرا می‌شوند تمرکز دارد. هر تست یک مفهوم خاص LangChain4j را به صورت جداگانه نشان می‌دهد.

<img src="../../../translated_images/fa/testing-pyramid.2dd1079a0481e53e.webp" alt="هرم تست" width="800"/>

*هرم تست که تعادل بین تست‌های واحد (سریع، ایزوله)، تست‌های یکپارچه‌سازی (کامپوننت‌های واقعی) و تست‌های انتها به انتها را نشان می‌دهد. این آموزش شامل تست واحد است.*

| ماژول | تعداد تست‌ها | محور اصلی | فایل‌های کلیدی |
|--------|-------------|-----------|----------------|
| **00 - شروع سریع** | 6 | قالب‌های پرامپت و جایگزینی متغیرها | `SimpleQuickStartTest.java` |
| **01 - مقدمه** | 8 | حافظه مکالمه و گفتگو با حالت حفظ شده | `SimpleConversationTest.java` |
| **02 - مهندسی پرامپت** | 12 | الگوهای GPT-5.2، سطوح اشتیاق، خروجی ساختار یافته | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | ورود اسناد، جاسازی‌ها، جستجوی شباهت | `DocumentServiceTest.java` |
| **04 - ابزارها** | 12 | فراخوانی تابع و زنجیره‌سازی ابزارها | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | پروتکل زمینه مدل با انتقال stdio | `SimpleMcpTest.java` |

## اجرای تست‌ها

**اجرای تمام تست‌ها از شاخه ریشه:**

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

**اجرای یک کلاس تست واحد:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**اجرای یک متد تست خاص:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#باید تاریخچه مکالمه را حفظ کند
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#باید تاریخچه مکالمه حفظ شود
```

## اجرای تست‌ها در VS Code

اگر از Visual Studio Code استفاده می‌کنید، Test Explorer یک رابط گرافیکی برای اجرای تست‌ها و اشکال‌زدایی آن‌ها ارائه می‌دهد.

<img src="../../../translated_images/fa/vscode-testing.f02dd5917289dced.webp" alt="مرورگر تست VS Code" width="800"/>

*مرورگر تست VS Code که درخت تست را با تمام کلاس‌های تست جاوا و متدهای تست جداگانه نشان می‌دهد*

**برای اجرای تست در VS Code:**

1. مرورگر تست را با کلیک روی آیکون بیکر در نوار فعالیت باز کنید
2. درخت تست را باز کنید تا همه ماژول‌ها و کلاس‌های تست را ببینید
3. روی دکمه پخش کنار هر تست کلیک کنید تا به صورت جداگانه اجرا شود
4. روی «Run All Tests» کلیک کنید تا کل مجموعه اجرا شود
5. روی هر تست کلیک راست کرده و «Debug Test» را انتخاب کنید تا نقاط توقف تعریف و مرحله‌به‌مرحله کد را دنبال کنید

مرورگر تست علامت تأیید سبز برای تست‌های موفق نمایش می‌دهد و در هنگام شکست پیام‌های خطای دقیق ارائه می‌کند.

## الگوهای تست

### الگو ۱: تست قالب‌های پرامپت

ساده‌ترین الگو، قالب‌های پرامپت را بدون فراخوانی هیچ مدل هوش مصنوعی تست می‌کند. شما تأیید می‌کنید که جایگزینی متغیرها به درستی انجام می‌شود و پرامپت‌ها به شکل مورد انتظار قالب‌بندی شده‌اند.

<img src="../../../translated_images/fa/prompt-template-testing.b902758ddccc8dee.webp" alt="تست قالب پرامپت" width="800"/>

*تست قالب پرامپت که جریان جایگزینی متغیر را نشان می‌دهد: قالب با جایگزین‌ها → اعمال مقادیر → خروجی قالب‌بندی‌شده تأیید شده*

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

این تست در فایل `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java` قرار دارد.

**اجرای آن:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#قالب‌بندی_الگوی_پرومت را آزمایش کنید
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#قالب‌بندی قالب پرسش آزمایشی
```

### الگو ۲: ساخت مدل‌های زبان جعلی (Mocking)

وقتی منطق مکالمه را تست می‌کنید، از Mockito استفاده کنید تا مدل‌های جعلی ایجاد کنید که پاسخ‌های از پیش تعیین شده بازگردانند. این باعث می‌شود تست‌ها سریع، رایگان و قطعی باشند.

<img src="../../../translated_images/fa/mock-vs-real.3b8b1f85bfe6845e.webp" alt="مقایسه Mock با API واقعی" width="800"/>

*مقایسه‌ای که نشان می‌دهد چرا Mock برای تست ترجیح داده می‌شود: سریع، رایگان، قطعی و بدون نیاز به کلید API*

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
        assertThat(history).hasSize(6); // ۳ پیام از کاربر + ۳ پیام از هوش مصنوعی
    }
}
```

این الگو در `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` ظاهر می‌شود. مدل جعلی تضمین می‌کند رفتار ثابتی وجود داشته باشد تا بتوانید تأیید کنید مدیریت حافظه به درستی انجام می‌شود.

### الگو ۳: تست ایزولاسیون مکالمه

حافظه مکالمه باید کاربران مختلف را جدا نگه دارد. این تست تأیید می‌کند که مکالمات زمینه‌ها را با هم مخلوط نمی‌کنند.

<img src="../../../translated_images/fa/conversation-isolation.e00336cf8f7a3e3f.webp" alt="ایزولاسیون مکالمه" width="800"/>

*تست ایزولاسیون مکالمه که فروشگاه‌های حافظه جداگانه برای کاربران مختلف را نشان می‌دهد تا از مخلوط شدن زمینه جلوگیری شود*

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

هر مکالمه تاریخچه مستقل خود را حفظ می‌کند. در سیستم‌های تولیدی، این ایزولاسیون برای برنامه‌های چندکاربره حیاتی است.

### الگو ۴: تست مستقل ابزارها

ابزارها توابعی هستند که هوش مصنوعی می‌تواند فراخوانی کند. آن‌ها را مستقیم تست کنید تا مطمئن شوید بدون توجه به تصمیمات هوش مصنوعی به درستی کار می‌کنند.

<img src="../../../translated_images/fa/tools-testing.3e1706817b0b3924.webp" alt="تست ابزارها" width="800"/>

*تست مستقل ابزارها نشان دهنده اجرای ابزارهای جعلی بدون فراخوانی هوش مصنوعی برای بررسی منطق کسب‌وکار*

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

این تست‌ها در `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` قرار دارند و منطق ابزارها را بدون دخالت هوش مصنوعی تایید می‌کنند. مثال زنجیره‌سازی نشان می‌دهد که چگونه خروجی یک ابزار به ورودی ابزار دیگری متصل می‌شود.

### الگو ۵: تست RAG در حافظه داخلی

سیستم‌های RAG معمولاً به پایگاه داده‌های برداری و سرویس‌های جاسازی نیاز دارند. الگوی درون‌حافظه به شما امکان می‌دهد تمام جریان کار را بدون وابستگی‌های خارجی تست کنید.

<img src="../../../translated_images/fa/rag-testing.ee7541b1e23934b1.webp" alt="تست RAG در حافظه داخلی" width="800"/>

*فرآیند تست RAG در حافظه داخلی که تجزیه سند، ذخیره جاسازی و جستجوی شباهت را بدون نیاز به پایگاه داده نشان می‌دهد*

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

این تست از `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` یک سند را در حافظه ایجاد می‌کند و تأیید می‌کند که تقسیم‌بندی و مدیریت متادیتا درست انجام می‌شود.

### الگو ۶: تست یکپارچه‌سازی MCP

ماژول MCP پروتکل زمینه مدل را با استفاده از انتقال stdio تست می‌کند. این تست‌ها تأیید می‌کنند که برنامه شما می‌تواند سرورهای MCP را به عنوان فرایندهای فرعی اجرا و با آن‌ها ارتباط برقرار کند.

تست‌های موجود در `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` رفتار کلاینت MCP را تایید می‌کنند.

**اجرای آن‌ها:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## فلسفه تست

کد خود را تست کنید، نه هوش مصنوعی را. تست‌های شما باید کدی را که نوشته‌اید اعتبارسنجی کنند با چک کردن نحوه ساخت پرامپت‌ها، نحوه مدیریت حافظه و اجرای ابزارها. پاسخ‌های هوش مصنوعی متفاوت است و نباید بخشی از ادعاهای تست باشند. از خود بپرسید آیا قالب پرامپت شما به درستی متغیرها را جایگزین می‌کند، نه اینکه آیا هوش مصنوعی پاسخ درست می‌دهد یا خیر.

از Mock برای مدل‌های زبان استفاده کنید. آن‌ها وابستگی‌های خارجی هستند که کند، گران و غیرقطعی‌اند. Mock‌ کردن تست‌ها را سریع با میلی‌ثانیه به جای ثانیه، رایگان بدون هزینه API و قطعی با همان نتیجه هر بار می‌سازد.

تست‌ها را مستقل نگه دارید. هر تست باید داده‌های خودش را تنظیم کند، به تست‌های دیگر وابسته نباشد و پس از اجرا پاکسازی انجام دهد. تست‌ها باید فارغ از ترتیب اجرا، موفق شوند.

موارد لبه‌ای را فراتر از مسیر خوش‌آیند تست کنید. ورودی‌های خالی، ورودی‌های بسیار بزرگ، کاراکترهای خاص، پارامترهای نامعتبر و شرایط مرزی را امتحان کنید. این‌ها اغلب باگ‌هایی را نشان می‌دهند که استفاده عادی آشکار نمی‌کند.

از نام‌های توصیفی استفاده کنید. مقایسه کنید `shouldMaintainConversationHistoryAcrossMultipleMessages()` با `test1()`. اولی دقیقاً می‌گوید چه چیزی تست می‌شود و اشکال‌زدایی شکست‌ها را بسیار آسان‌تر می‌کند.

## گام‌های بعدی

اکنون که الگوهای تست را درک کرده‌اید، عمیق‌تر به هر ماژول بپردازید:

- **[00 - شروع سریع](../00-quick-start/README.md)** - شروع با اصول قالب‌های پرامپت
- **[01 - مقدمه](../01-introduction/README.md)** - یادگیری مدیریت حافظه مکالمه
- **[02 - مهندسی پرامپت](../02-prompt-engineering/README.md)** - تسلط بر الگوهای پرامپت GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - ساخت سیستم‌های تولید تقویت شده با بازیابی اطلاعات
- **[04 - ابزارها](../04-tools/README.md)** - اجرای فراخوانی تابع و زنجیره ابزارها
- **[05 - MCP](../05-mcp/README.md)** - یکپارچه‌سازی پروتکل زمینه مدل

README هر ماژول توضیحات جامعی از مفاهیم تست‌شده در اینجا ارائه می‌دهد.

---

**ناوبری:** [← بازگشت به اصلی](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**توضیح مهم**:  
این سند با استفاده از سرویس ترجمه هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما در تلاش برای دقت هستیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است حاوی خطاها یا نادرستی‌هایی باشند. سند اصلی به زبان بومی آن باید به عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حیاتی، ترجمه حرفه‌ای توسط انسان توصیه می‌شود. ما مسئول سوءتفاهم‌ها یا برداشت‌های نادرست ناشی از استفاده از این ترجمه نیستیم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->