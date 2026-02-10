# اختبار تطبيقات LangChain4j

## جدول المحتويات

- [البدء السريع](../../../docs)
- [ما تغطيه الاختبارات](../../../docs)
- [تشغيل الاختبارات](../../../docs)
- [تشغيل الاختبارات في VS Code](../../../docs)
- [نماذج الاختبار](../../../docs)
- [فلسفة الاختبار](../../../docs)
- [الخطوات التالية](../../../docs)

يرشدك هذا الدليل عبر الاختبارات التي توضّح كيفية اختبار تطبيقات الذكاء الاصطناعي بدون الحاجة إلى مفاتيح API أو خدمات خارجية.

## البدء السريع

قم بتشغيل جميع الاختبارات بأمر واحد:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/ar/test-results.ea5c98d8f3642043.webp" alt="نتائج الاختبارات الناجحة" width="800"/>

*تنفيذ اختبار ناجح يظهر اجتياز جميع الاختبارات بدون أي فشل*

## ما تغطيه الاختبارات

يركز هذا المقرر على **اختبارات الوحدة** التي تُجرى محليًا. كل اختبار يوضّح مفهومًا معينًا من LangChain4j بشكل معزول.

<img src="../../../translated_images/ar/testing-pyramid.2dd1079a0481e53e.webp" alt="هرم الاختبار" width="800"/>

*هرم الاختبار يظهر التوازن بين اختبارات الوحدة (سريعة، معزولة)، اختبارات التكامل (مكونات حقيقية)، واختبارات من البداية إلى النهاية. هذا التدريب يغطي اختبار الوحدة.*

| الوحدة | الاختبارات | التركيز | الملفات الرئيسية |
|--------|------------|---------|-------------------|
| **00 - البدء السريع** | 6 | قوالب الطلبات واستبدال المتغيرات | `SimpleQuickStartTest.java` |
| **01 - مقدمة** | 8 | ذاكرة المحادثة والدردشة الحالة | `SimpleConversationTest.java` |
| **02 - هندسة الطلبات** | 12 | أنماط GPT-5.2، مستويات الحماس، المخرجات المنظمة | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | استيعاب الوثائق، التضمينات، البحث بالتشابه | `DocumentServiceTest.java` |
| **04 - الأدوات** | 12 | استدعاء الوظائف وربط الأدوات | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | بروتوكول سياق النموذج مع نقل stdio | `SimpleMcpTest.java` |

## تشغيل الاختبارات

**تشغيل جميع الاختبارات من الجذر:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**تشغيل اختبارات وحدة محددة:**

**Bash:**
```bash
cd 01-introduction && mvn test
# أو من الجذر
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# أو من الجذر
mvn --% test -pl 01-introduction
```

**تشغيل فصل اختبار واحد:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**تشغيل طريقة اختبار محددة:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#يجب_الحفاظ_على_تاريخ_المحادثة
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#يجب الحفاظ على سجل المحادثة
```

## تشغيل الاختبارات في VS Code

إذا كنت تستخدم Visual Studio Code، يوفر مستكشف الاختبارات واجهة رسومية لتشغيل وتصحيح الأخطاء للاختبارات.

<img src="../../../translated_images/ar/vscode-testing.f02dd5917289dced.webp" alt="مستكشف اختبارات VS Code" width="800"/>

*مستكشف اختبارات VS Code يعرض شجرة الاختبارات مع جميع فصول اختبار جافا وطُرُق الاختبار الفردية*

**لتشغيل الاختبارات في VS Code:**

1. افتح مستكشف الاختبارات بالنقر على أيقونة القارورة في شريط النشاط
2. وسّع شجرة الاختبارات لرؤية جميع الوحدات وفصول الاختبار
3. اضغط زر التشغيل بجانب أي اختبار لتشغيله بشكل فردي
4. اضغط "Run All Tests" لتنفيذ كامل الحزمة
5. انقر بزر الماوس الأيمن على أي اختبار واختر "Debug Test" لوضع نقاط توقف والتنقل في الكود

يعرض مستكشف الاختبارات علامات تحقق خضراء للاختبارات الناجحة ويوفر رسائل فشل مفصلة عند الفشل.

## نماذج الاختبار

### النموذج 1: اختبار قوالب الطلب

النموذج الأبسط يختبر قوالب الطلب بدون استدعاء أي نموذج ذكاء اصطناعي. تتحقق من أن استبدال المتغيرات يعمل بشكل صحيح وأن الطلبات مصاغة كما هو متوقع.

<img src="../../../translated_images/ar/prompt-template-testing.b902758ddccc8dee.webp" alt="اختبار قالب الطلب" width="800"/>

*اختبار قوالب الطلب يعرض تدفق استبدال المتغيرات: القالب مع أماكن الانتظار → القيم المطبقة → التحقق من المخرجات المنسقة*

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

يعيش هذا الاختبار في `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**قم بتشغيله:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#اختبار_تنسيق_نموذج_المطالبة
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#اختبار_تنسيق_قالب_المطالبة
```

### النموذج 2: استخدام النماذج المزيفة (Mocking)

عند اختبار منطق المحادثة، استخدم Mockito لإنشاء نماذج مزيفة تُرجع ردودًا محددة مسبقًا. هذا يجعل الاختبارات سريعة، مجانية، وحتمية النتائج.

<img src="../../../translated_images/ar/mock-vs-real.3b8b1f85bfe6845e.webp" alt="مقارنة بين النموذج المزيف والنموذج الحقيقي" width="800"/>

*مقارنة توضح سبب تفضيل النماذج المزيفة للاختبار: سريعة، مجانية، حتمية، ولا تتطلب مفاتيح API*

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
        assertThat(history).hasSize(6); // ٣ رسائل من المستخدم + ٣ رسائل من الذكاء الاصطناعي
    }
}
```

يظهر هذا النموذج في `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. تحافظ النماذج المزيفة على سلوك ثابت حتى تتمكن من التحقق من إدارة الذاكرة بدقة.

### النموذج 3: اختبار عزل المحادثة

يجب أن تبقي ذاكرة المحادثة المستخدمين منفصلين. يضمن هذا الاختبار عدم خلط سياقات المحادثة.

<img src="../../../translated_images/ar/conversation-isolation.e00336cf8f7a3e3f.webp" alt="عزل المحادثة" width="800"/>

*اختبار عزل المحادثة يعرض مخازن ذاكرة منفصلة لمستخدمين مختلفين لمنع خلط السياقات*

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

يحافظ كل محادثة على سجله المستقل الخاص. في أنظمة الإنتاج، هذا العزل ضروري لتطبيقات متعددة المستخدمين.

### النموذج 4: اختبار الأدوات بشكل مستقل

الأدوات هي وظائف يمكن للذكاء الاصطناعي استدعاؤها. اختبرها مباشرة لضمان عملها بشكل صحيح بغض النظر عن قرارات الذكاء الاصطناعي.

<img src="../../../translated_images/ar/tools-testing.3e1706817b0b3924.webp" alt="اختبار الأدوات" width="800"/>

*اختبار الأدوات بشكل مستقل يعرض تنفيذ أدوات مزيفة بدون استدعاءات الذكاء الاصطناعي للتحقق من منطق الأعمال*

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

تُجري هذه الاختبارات من `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` التحقق من منطق الأدوات بدون تدخل الذكاء الاصطناعي. يُظهر مثال الربط كيف تغذي مخرجات أداة واحدة مدخلات أخرى.

### النموذج 5: اختبار RAG داخل الذاكرة

تتطلب أنظمة RAG عادة قواعد بيانات متجهية وخدمات تضمين. يسمح نموذج الذاكرة باختبار كامل الخط من غير الاعتماد على خدمات خارجية.

<img src="../../../translated_images/ar/rag-testing.ee7541b1e23934b1.webp" alt="اختبار RAG داخل الذاكرة" width="800"/>

*اختبار RAG داخل الذاكرة يعرض سير عمل تحليل الوثائق، تخزين التضمينات، والبحث بالتشابه بدون الحاجة إلى قاعدة بيانات*

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

ينشئ هذا الاختبار من `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` وثيقة في الذاكرة ويتحقق من التفكيك والتعامل مع البيانات الوصفية.

### النموذج 6: اختبار تكامل MCP

تختبر وحدة MCP بروتوكول سياق النموذج باستخدام نقل stdio. تتحقق هذه الاختبارات من قدرة تطبيقك على تشغيل والتواصل مع خوادم MCP كعمليات فرعية.

تختبر الاختبارات في `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` سلوك عميل MCP.

**قم بتشغيلها:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## فلسفة الاختبار

اختبر كودك، لا تختبر الذكاء الاصطناعي. يجب أن تتحقق اختباراتك من الكود الذي تكتبه من خلال فحص كيفية بناء الطلبات، كيفية إدارة الذاكرة، وكيفية تنفيذ الأدوات. ردود الذكاء الاصطناعي متغيرة ولا يجب أن تكون جزءًا من تحقق الاختبارات. اسأل نفسك إذا كانت قوالب الطلب تستبدل المتغيرات بشكل صحيح، وليس إذا كان الذكاء الاصطناعي يعطي الإجابة الصحيحة.

استخدم النماذج المزيفة للنماذج اللغوية. فهي تبعيات خارجية بطيئة ومكلفة وغير حتمية. يجعل المزيف الاختبارات سريعة بوقت بضع ميلي ثانية بدلًا من ثوانٍ، مجانية دون تكاليف API، وحتمية بنفس النتيجة في كل مرة.

حافظ على استقلالية الاختبارات. يجب أن يُعد كل اختبار بياناته الخاصة، لا تعتمد على اختبارات أخرى، وأن ينظف بعد التنفيذ. يجب أن تنجح الاختبارات بغض النظر عن ترتيب التنفيذ.

اختبر الحالات الحدية إلى ما بعد المسار السعيد. جرب المدخلات الفارغة، المدخلات الكبيرة جدًا، الرموز الخاصة، المعلمات غير الصالحة، والظروف الحدودية. غالبًا ما تكشف هذه عن أخطاء لا يظهرها الاستخدام العادي.

استخدم أسماء وصفية. قارن بين `shouldMaintainConversationHistoryAcrossMultipleMessages()` و `test1()`. الأول يخبرك بالضبط ما الذي يُختبر، مما يسهل كثيرًا تحديد أسباب الفشل.

## الخطوات التالية

الآن بعد أن فهمت نماذج الاختبار، اغص أعمق في كل وحدة:

- **[00 - البدء السريع](../00-quick-start/README.md)** - ابدأ بأساسيات قوالب الطلب
- **[01 - مقدمة](../01-introduction/README.md)** - تعلّم إدارة ذاكرة المحادثة
- **[02 - هندسة الطلبات](../02-prompt-engineering/README.md)** - اتقن أنماط توجيه GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - طور أنظمة توليد مدعومة بالاسترجاع
- **[04 - الأدوات](../04-tools/README.md)** - نفذ استدعاء الوظائف وربط الأدوات
- **[05 - MCP](../05-mcp/README.md)** - دمج بروتوكول سياق النموذج

كل ملف README للوحدة يوفر شروحات مفصلة للمفاهيم التي تم اختبارها هنا.

---

**التنقل:** [← العودة إلى الرئيسي](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**تنويه**:
تمت ترجمة هذا المستند باستخدام خدمة الترجمة الآلية [Co-op Translator](https://github.com/Azure/co-op-translator). رغم سعينا لتحقيق الدقة، يرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار المستند الأصلي بلغته الأصلية المصدر الرسمي والمعتمد. للمعلومات الهامة، يُنصح بالاعتماد على الترجمة البشرية المهنية. نحن غير مسؤولين عن أي سوء فهم أو تفسير ناتج عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->