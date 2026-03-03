# اختبار تطبيقات LangChain4j

## جدول المحتويات

- [بدء سريع](../../../docs)
- [ما تغطيه الاختبارات](../../../docs)
- [تشغيل الاختبارات](../../../docs)
- [تشغيل الاختبارات في VS Code](../../../docs)
- [أنماط الاختبار](../../../docs)
- [فلسفة الاختبار](../../../docs)
- [الخطوات التالية](../../../docs)

توجهك هذه الدليل خلال الاختبارات التي توضح كيفية اختبار تطبيقات الذكاء الاصطناعي دون الحاجة إلى مفاتيح API أو خدمات خارجية.

## بدء سريع

شغّل جميع الاختبارات بأمر واحد:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

عندما تجتاز جميع الاختبارات، ينبغي أن ترى مخرجات مشابهة للصورة أدناه — تم تشغيل الاختبارات بدون أي إخفاقات.

<img src="../../../translated_images/ar/test-results.ea5c98d8f3642043.webp" alt="نتائج الاختبار الناجحة" width="800"/>

*تنفيذ اختبار ناجح يظهر جميع الاختبارات تجتاز بدون أي إخفاقات*

## ما تغطيه الاختبارات

يركز هذا المقرر على **اختبارات الوحدة** التي تُشغل محليًا. كل اختبار يوضح مفهومًا معينًا من LangChain4j بشكل منعزل. هرم الاختبار أدناه يبين مكان اختبارات الوحدة — تشكل الأساس السريع والموثوق الذي يبني عليه باقي استراتيجيتك للاختبار.

<img src="../../../translated_images/ar/testing-pyramid.2dd1079a0481e53e.webp" alt="هرم الاختبار" width="800"/>

*هرم الاختبار يوضح التوازن بين اختبارات الوحدة (سريعة، معزولة)، اختبارات التكامل (مكونات حقيقية)، واختبارات من النهاية إلى النهاية. هذا التدريب يغطي اختبار الوحدة.*

| الوحدة | الاختبارات | التركيز | الملفات الرئيسية |
|--------|-------|-------|-----------|
| **00 - بدء سريع** | 6 | قوالب المطالبات واستبدال المتغيرات | `SimpleQuickStartTest.java` |
| **01 - مقدمة** | 8 | ذاكرة المحادثة والدردشة القائمة على الحالة | `SimpleConversationTest.java` |
| **02 - هندسة المطالبات** | 12 | أنماط GPT-5.2، مستويات الحماس، المخرجات المهيكلة | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | استيعاب الوثائق، التضمينات، البحث التشابهي | `DocumentServiceTest.java` |
| **04 - الأدوات** | 12 | استدعاء الدوال وربط الأدوات | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | بروتوكول سياق النموذج مع نقل stdio | `SimpleMcpTest.java` |

## تشغيل الاختبارات

**شغّل جميع الاختبارات من الجذر:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**شغّل اختبارات لوحدة محددة:**

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

**شغّل فئة اختبار واحدة:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**شغّل طريقة اختبار محددة:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#يجب_الحفاظ_على_تاريخ_المحادثة
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#يجب_الحفاظ_على_سجل_المحادثة
```

## تشغيل الاختبارات في VS Code

إذا كنت تستخدم Visual Studio Code، يوفر مستكشف الاختبارات واجهة رسومية لتشغيل وتصحيح الاختبارات.

<img src="../../../translated_images/ar/vscode-testing.f02dd5917289dced.webp" alt="مستكشف اختبار VS Code" width="800"/>

*مستكشف اختبار VS Code يعرض شجرة الاختبار مع جميع فئات اختبار جافا وأساليب الاختبار الفردية*

**لتشغيل الاختبارات في VS Code:**

1. افتح مستكشف الاختبارات بالنقر على أيقونة القارورة في شريط النشاط
2. وسّع شجرة الاختبارات لرؤية جميع الوحدات وفئات الاختبار
3. انقر على زر التشغيل بجانب أي اختبار لتشغيله بشكل فردي
4. انقر على "تشغيل جميع الاختبارات" لتنفيذ المجموعة كاملة
5. انقر بزر الفأرة الأيمن على أي اختبار واختر "تصحيح الاختبار" لتعيين نقاط توقف والتنقل خلال الكود

يعرض مستكشف الاختبارات علامات تحقق خضراء للاختبارات التي اجتازت ويقدم رسائل تفصيلية عند الفشل.

## أنماط الاختبار

### النمط 1: اختبار قوالب المطالبات

أبسط نمط يختبر قوالب المطالبات دون استدعاء أي نموذج ذكاء اصطناعي. تتحقق من أن استبدال المتغيرات يعمل بشكل صحيح وأن المطالبات منسقة كما هو متوقع.

<img src="../../../translated_images/ar/prompt-template-testing.b902758ddccc8dee.webp" alt="اختبار قالب المطالبة" width="800"/>

*اختبار قوالب المطالبات يعرض تدفق استبدال المتغيرات: القالب مع عناصر نائب → تطبيق القيم → التحقق من المخرجات المنسقة*

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

يُوجد هذا الاختبار في `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**شغّله:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#اختبار_تنسيق_نموذج_المطالبة
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#اختبار تنسيق قالب الطلب
```

### النمط 2: تمويه نماذج اللغة

عند اختبار منطق المحادثة، استخدم Mockito لإنشاء نماذج وهمية تعيد ردودًا محددة مسبقًا. هذا يجعل الاختبارات سريعة، مجانية، وحتمية.

<img src="../../../translated_images/ar/mock-vs-real.3b8b1f85bfe6845e.webp" alt="مقارنة تمويه مقابل API حقيقي" width="800"/>

*مقارنة توضح لماذا يُفضل استخدام التمويه للاختبار: إنها سريعة، مجانية، حتمية، ولا تتطلب مفاتيح API*

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
        assertThat(history).hasSize(6); // 3 رسائل من المستخدم + 3 رسائل من الذكاء الاصطناعي
    }
}
```

يظهر هذا النمط في `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. يضمن التمويه سلوكًا متسقًا حتى تتمكن من التحقق من أن إدارة الذاكرة تعمل بشكل صحيح.

### النمط 3: اختبار عزل المحادثة

يجب على ذاكرة المحادثة أن تحافظ على فصل المستخدمين المتعددين. يتحقق هذا الاختبار من أن المحادثات لا تخلط بين السياقات.

<img src="../../../translated_images/ar/conversation-isolation.e00336cf8f7a3e3f.webp" alt="عزل المحادثة" width="800"/>

*اختبار عزل المحادثة يظهر تخزين ذاكرة منفصلة لمستخدمين مختلفين لمنع اختلاط السياقات*

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

تحافظ كل محادثة على تاريخها الخاص المستقل. في الأنظمة الإنتاجية، هذا العزل ضروري لتطبيقات المستخدمين المتعددين.

### النمط 4: اختبار الأدوات بشكل مستقل

الأدوات هي دوال يمكن للذكاء الاصطناعي استدعاؤها. اختبرها مباشرة للتأكد من أنها تعمل بشكل صحيح بغض النظر عن قرارات الذكاء الاصطناعي.

<img src="../../../translated_images/ar/tools-testing.3e1706817b0b3924.webp" alt="اختبار الأدوات" width="800"/>

*اختبار الأدوات بشكل مستقل يعرض تنفيذ أداة وهمية بدون استدعاءات الذكاء الاصطناعي للتحقق من منطق الأعمال*

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

تحقق هذه الاختبارات من `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` من منطق الأدوات بدون تداخل الذكاء الاصطناعي. يظهر مثال الربط كيف يمر مخرج أداة إلى مدخل أخرى.

### النمط 5: اختبار RAG في الذاكرة

تتطلب أنظمة RAG تقليديًا قواعد بيانات متجهية وخدمات تضمين. يتيح نمط الذاكرة اختبار الخطوط كاملة دون الاعتماد على خدمات خارجية.

<img src="../../../translated_images/ar/rag-testing.ee7541b1e23934b1.webp" alt="اختبار RAG في الذاكرة" width="800"/>

*تدفق عمل اختبار RAG في الذاكرة يظهر تحليل الوثائق، تخزين التضمينات، والبحث التشابهي دون الحاجة لقاعدة بيانات*

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

يخلق هذا الاختبار من `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` مستندًا في الذاكرة ويتحقق من تجزئة المحتوى والتعامل مع البيانات الوصفية.

### النمط 6: اختبار تكامل MCP

اختبار وحدة MCP يختبر تكامل بروتوكول سياق النموذج باستخدام نقل stdio. تتحقق هذه الاختبارات من أن تطبيقك يمكنه تشغيل والتواصل مع خوادم MCP كعمليات فرعية.

تتحقق الاختبارات في `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` من سلوك عميل MCP.

**شغّلها:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## فلسفة الاختبار

اختبر كودك، لا اختبر الذكاء الاصطناعي. يجب أن تتحقق اختباراتك من الكود الذي تكتبه عبر فحص كيفية بناء المطالبات، كيفية إدارة الذاكرة، وكيف تنفذ الأدوات. ردود الذكاء الاصطناعي متغيرة ولا يجب أن تكون جزءًا من تأكيدات الاختبار. اسأل نفسك ما إذا كانت قالب مطالبتك يستبدل المتغيرات بشكل صحيح، وليس ما إذا كان الذكاء الاصطناعي يعطي الإجابة الصحيحة.

استخدم التمويه لنماذج اللغة. إنها تبعيات خارجية بطيئة، مكلفة، وغير حتمية. يجعل التمويه الاختبارات سريعة بالوحدات المللي ثانية بدل الثواني، مجانية بدون تكاليف API، وحتمية دائمًا تعطي نفس النتيجة.

حافظ على استقلالية الاختبارات. يجب أن يهيئ كل اختبار بياناته الخاصة، لا يعتمد على اختبارات أخرى، وينظف بعد نفسه. يجب أن تجتاز الاختبارات بغض النظر عن ترتيب التنفيذ.

اختبر الحالات الحدية بما يتجاوز المسار السعيد. جرّب المدخلات الفارغة، المدخلات الكبيرة جدًا، الأحرف الخاصة، المعلمات غير الصالحة، والشروط الحدية. غالبًا ما تكشف هذه عن عيوب لا يظهرها الاستخدام الطبيعي.

استخدم أسماء وصفية. قارن بين `shouldMaintainConversationHistoryAcrossMultipleMessages()` و`test1()`. الأولى تخبرك بالضبط ما الذي يُختبر، مما يجعل تصحيح الأخطاء أسهل بكثير.

## الخطوات التالية

الآن بعد أن فهمت أنماط الاختبار، تعمّق أكثر في كل وحدة:

- **[00 - بدء سريع](../00-quick-start/README.md)** - ابدأ بأساسيات قوالب المطالبات
- **[01 - مقدمة](../01-introduction/README.md)** - تعلّم إدارة ذاكرة المحادثة
- **[02 - هندسة المطالبات](../02-prompt-engineering/README.md)** - أتقن أنماط GPT-5.2 للمطالبات
- **[03 - RAG](../03-rag/README.md)** - ابنِ أنظمة التوليد المعززة بالاسترجاع
- **[04 - الأدوات](../04-tools/README.md)** - نفّذ استدعاء الدوال وربط الأدوات
- **[05 - MCP](../05-mcp/README.md)** - دمج بروتوكول سياق النموذج

يوفر ملف README في كل وحدة شروحات مفصلة للمفاهيم التي تم اختبارها هنا.

---

**التنقل:** [← العودة إلى الرئيسي](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**إخلاء المسؤولية**:  
تمت ترجمة هذا المستند باستخدام خدمة الترجمة الآلية [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى للحفاظ على الدقة، يرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار المستند الأصلي بلغته الأصلية هو المصدر المعتمد والرسمي. للمعلومات الحرجة، يُوصى بالاستعانة بترجمة بشرية محترفة. نحن غير مسؤولين عن أي سوء فهم أو تفسير ناتج عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->