# בדיקת יישומי LangChain4j

## תוכן העניינים

- [התחלה מהירה](../../../docs)
- [מה שהבדיקות מכסות](../../../docs)
- [הרצת הבדיקות](../../../docs)
- [הרצת בדיקות ב-VS Code](../../../docs)
- [דפוסי בדיקות](../../../docs)
- [פילוסופיית הבדיקה](../../../docs)
- [השלבים הבאים](../../../docs)

המדריך הזה מוביל אותך דרך הבדיקות שמדגימות כיצד לבדוק יישומי AI מבלי לדרוש מפתחות API או שירותים חיצוניים.

## התחלה מהירה

הרץ את כל הבדיקות עם פקודה אחת:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/he/test-results.ea5c98d8f3642043.webp" alt="תוצאות בדיקה מוצלחות" width="800"/>

*הרצת בדיקות מוצלחת המראה שכל הבדיקות עברו ללא כישלונות*

## מה שהבדיקות מכסות

הקורס הזה מתמקד ב**בדיקות יחידה** שמתבצעות מקומית. כל בדיקה מדגימה מושג ספציפי של LangChain4j בנפרד.

<img src="../../../translated_images/he/testing-pyramid.2dd1079a0481e53e.webp" alt="פירמידת בדיקות" width="800"/>

*פירמידת בדיקות המציגה את האיזון בין בדיקות יחידה (מהירות, מבודדות), בדיקות אינטגרציה (רכיבים אמיתיים) ובדיקות מקצה לקצה. ההדרכה הזו מתמקדת בבדיקות יחידה.*

| מודול | בדיקות | התמקדות | קבצים מרכזיים |
|--------|-------|-------|-----------|
| **00 - התחלה מהירה** | 6 | תבניות פרומט והחלפת משתנים | `SimpleQuickStartTest.java` |
| **01 - מבוא** | 8 | זיכרון שיחה וצ'אט עם מצב | `SimpleConversationTest.java` |
| **02 - הנדסת פרומט** | 12 | דפוסי GPT-5.2, רמות היערכות, פלט מובנה | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | קליטת מסמכים, אמבדינג, חיפוש דמיון | `DocumentServiceTest.java` |
| **04 - כלים** | 12 | קריאת פונקציות ורצף כלים | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | פרוטוקול הקשר מודל עם תעבורת stdio | `SimpleMcpTest.java` |

## הרצת הבדיקות

**הרץ את כל הבדיקות מה-root:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**הרץ בדיקות למודול ספציפי:**

**Bash:**
```bash
cd 01-introduction && mvn test
# או מהשורש
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# או מהשורש
mvn --% test -pl 01-introduction
```

**הרץ מחלקת בדיקות יחידה:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**הרץ שיטת בדיקה ספציפית:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#יש לשמור על היסטוריית שיחה
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#יש לשמור על היסטוריית שיחה
```

## הרצת בדיקות ב-VS Code

אם אתה משתמש ב-Visual Studio Code, סייר הבדיקות מספק ממשק גרפי להרצה וניפוי שגיאות של הבדיקות.

<img src="../../../translated_images/he/vscode-testing.f02dd5917289dced.webp" alt="סייר בדיקות ב-VS Code" width="800"/>

*סייר הבדיקות ב-VS Code המציג את עץ הבדיקות עם כל מחלקות הבדיקה ב-Java ושיטות בדיקה בודדות*

**כדי להריץ בדיקות ב-VS Code:**

1. פתח את סייר הבדיקות על ידי לחיצה על סמל הבקבוקון בסרגל הפעילות
2. הרחב את עץ הבדיקות כדי לראות את כל המודולים ומחלקות הבדיקה
3. לחץ על כפתור ההפעלה לצד כל בדיקה להרצתה בנפרד
4. לחץ על "Run All Tests" כדי להפעיל את כל המערך
5. לחצן ימני על בדיקה ובחר "Debug Test" כדי להגדיר נקודות עצירה ולעבור בקוד שלב אחר שלב

סייר הבדיקות מציג סימני בדיקה ירוקים לבדיקות שעוברות ומספק הודעות כישלון מפורטות במקרה של כישלון.

## דפוסי בדיקות

### דפוס 1: בדיקת תבניות פרומט

דפוס הבדיקה הפשוט ביותר בודק תבניות פרומט מבלי לקרוא למודל AI. אתה מאמת שהחלפת המשתנים התבצעה נכון ושהפרומט מעוצב כמצופה.

<img src="../../../translated_images/he/prompt-template-testing.b902758ddccc8dee.webp" alt="בדיקת תבניות פרומט" width="800"/>

*בדיקת תבניות פרומט המראה את זרימת החלפת המשתנים: תבנית עם מחזיקי מקום → ערכים מוחלים → פלט מעוצב מאומת*

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

בדיקה זו נמצאת ב-`00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**להריץ אותה:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#בדיקת עיצוב תבנית ההוראה
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#בדיקת עיצוב תבנית הבקשה
```

### דפוס 2: שימוש במודלים דמה

בזמן בדיקת לוגיקת שיחה, השתמש ב-Mockito ליצירת מודלים מזויפים שמחזירים תגובות קבועות מראש. זה הופך את הבדיקות למהירות, חינמיות ודטרמיניסטיות.

<img src="../../../translated_images/he/mock-vs-real.3b8b1f85bfe6845e.webp" alt="השוואת API מזויף מול אמיתי" width="800"/>

*השוואה שמראה מדוע העדיפות היא לשימוש בדמו בבדיקות: הן מהירות, חינמיות, דטרמיניסטיות ואינן דורשות מפתחות API*

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
        assertThat(history).hasSize(6); // 3 הודעות משתמש + 3 הודעות בינה מלאכותית
    }
}
```

דפוס זה מופיע ב-`01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. המוק מבטיח התנהגות עקבית כך שתוכל לוודא שהניהול של הזיכרון עובד נכון.

### דפוס 3: בדיקת בידוד שיחה

זיכרון השיחה חייב לשמור על הפרדה בין משתמשים שונים. בדיקה זו מאמתת ששיחות לא מערבבות הקשרים.

<img src="../../../translated_images/he/conversation-isolation.e00336cf8f7a3e3f.webp" alt="בידוד שיחה" width="800"/>

*בדיקת בידוד שיחה שמראה חנויות זיכרון נפרדות למשתמשים שונים כדי למנוע ערבוב הקשרים*

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

כל שיחה שומרת היסטוריה עצמאית משלה. במערכות ייצור, בידוד זה חיוני ליישומים מרובי משתמשים.

### דפוס 4: בדיקת כלים בנפרד

כלים הם פונקציות שה-AI יכול לקרוא להן. בדוק אותם ישירות כדי לוודא שהם עובדים כהלכה ללא תלות בהחלטות ה-AI.

<img src="../../../translated_images/he/tools-testing.3e1706817b0b3924.webp" alt="בדיקת כלים" width="800"/>

*בדיקת כלים בנפרד שמראה הרצת כלי מוק ללא קריאות AI לאימות לוגיקת עסקים*

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

בדיקות אלו מתוך `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` מאמתות את הלוגיקה בלי מעורבות AI. הדוגמא של השרשור מראה כיצד פלט של כלי אחד נמסר כקלט לאחד אחר.

### דפוס 5: בדיקת RAG בזיכרון

מערכות RAG בדרך כלל דורשות מסדי נתונים ושרותי אמבדינג. דפוס הבדיקה בזיכרון מאפשר לבדוק את כל התהליך מבלי תלות חיצונית.

<img src="../../../translated_images/he/rag-testing.ee7541b1e23934b1.webp" alt="בדיקת RAG בזיכרון" width="800"/>

*תהליך בדיקת RAG בזיכרון שמראה ניתוח מסמך, אחסון אמבדינג, וחיפוש דמיון מבלי לדרוש מסד נתונים*

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

בדיקה זו מתוך `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` יוצרת מסמך בזיכרון ומאמתת פיצול לטקסטים וטיפול במטא-דאטה.

### דפוס 6: בדיקות אינטגרציה MCP

מודול MCP בוחן אינטגרציה של פרוטוקול הקשר מודל באמצעות תעבורת stdio. הבדיקות מאמתות שהיישום שלך יכול להפעיל ולתקשר עם שרתי MCP כתהליכים משניים.

הבדיקות ב-`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` מאמתות את התנהגות לקוח MCP.

**להריץ אותן:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## פילוסופיית הבדיקה

בדוק את הקוד שלך, לא את ה-AI. הבדיקות שלך צריכות לאמת את הקוד שכתבת על ידי בדיקה כיצד בנויים הפרומטים, איך מנוהל הזיכרון ואיך הכלים מבוצעים. תגובות ה-AI משתנות ואינן חלק מאימות הבדיקות. שאל את עצמך האם תבנית הפרומט מחליפה משתנים נכון, לא האם ה-AI נותן את התשובה הנכונה.

השתמש במוק למודלים לשפה. הם תלות חיצונית שהיא איטית, יקרה ולא דטרמיניסטית. שימוש במוק הופך את הבדיקות למהירות עם מילישניות במקום שניות, חינמיות ללא עלויות API, ודטרמיניסטיות עם תוצאה זהה בכל פעם.

שמור על בדיקות עצמאיות. כל בדיקה צריכה להכין את הנתונים שלה בעצמה, לא להסתמך על בדיקות אחרות, ולנקות אחרי עצמה. הבדיקות אמורות לעבור ללא תלות בסדר הריצה.

בדוק תרחישים קיצוניים מעבר לנתיב השמח. נסה קלטים ריקים, קלטים גדולים מאוד, תווים מיוחדים, פרמטרים לא חוקיים ותנאי גבול. אלו לעיתים חושפים באגים שהשימוש הרגיל לא מגלה.

השתמש בשמות תיאוריים. השווה בין `shouldMaintainConversationHistoryAcrossMultipleMessages()` לבין `test1()`. הראשון אומר בדיוק מה נבדק, מה שמקל על איתור שגיאות במקרה של כישלון.

## השלבים הבאים

כעת כשאתה מבין את דפוסי הבדיקות, צלול לעומק בכל מודול:

- **[00 - התחלה מהירה](../00-quick-start/README.md)** - התחל עם יסודות תבניות הפרומט
- **[01 - מבוא](../01-introduction/README.md)** - למד ניהול זיכרון שיחה
- **[02 - הנדסת פרומט](../02/prompt-engineering/README.md)** - התמקצע בדפוסי פרומט GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - בנה מערכות יצירת תוכן עם הרחבה אווירים
- **[04 - כלים](../04-tools/README.md)** - יישם קריאות פונקציות ורצפי כלים
- **[05 - MCP](../05-mcp/README.md)** - שלב פרוטוקול הקשר מודל

קובצי ה-README של כל מודול מספקים הסברים מפורטים של המושגים שנבדקים כאן.

---

**ניווט:** [← חזרה לדף הראשי](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב ויתור**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). אנו שואפים לדיוק, אך יש לקחת בחשבון שתרגומים אוטומטיים עלולים להכיל שגיאות או אי דיוקים. המסמך המקורי בשפתו המקורית ייחשב כמקור הסמכות. עבור מידע קריטי, מומלץ להשתמש בתרגום מקצועי אנושי. אנו לא אחראים לכל אי הבנה או פרשנות שגוייה הנובעות מהשימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->