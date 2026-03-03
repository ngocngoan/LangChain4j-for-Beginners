# בדיקת יישומי LangChain4j

## תוכן העניינים

- [התחלה מהירה](../../../docs)
- [מה הבדיקות מכסות](../../../docs)
- [הרצת הבדיקות](../../../docs)
- [הרצת בדיקות ב-VS Code](../../../docs)
- [דפוסי בדיקה](../../../docs)
- [פילוסופיית בדיקה](../../../docs)
- [השלבים הבאים](../../../docs)

מדריך זה מלמד כיצד לבדוק יישומי AI ללא צורך במפתחות API או שירותים חיצוניים.

## התחלה מהירה

הרץ את כל הבדיקות באמצעות פקודה אחת:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

כאשר כל הבדיקות עוברות, תראה פלט כמו בתמונה למטה — בדיקות רצות ללא כישלונות.

<img src="../../../translated_images/he/test-results.ea5c98d8f3642043.webp" alt="תוצאות בדיקות מוצלחות" width="800"/>

*הרצת בדיקות מוצלחת המציגה את מעבר כל הבדיקות ללא כישלונות*

## מה הבדיקות מכסות

קורס זה מתמקד ב**בדיקות יחידה** שרצות מקומית. כל בדיקה מדגימה רעיון ספציפי ב־LangChain4j באופן מבודד. פירמידת הבדיקות למטה מראה היכן מתמקמים בדיקות היחידה — הן היסוד המהיר והאמין שעליו נשענת שאר אסטרטגיית הבדיקה שלך.

<img src="../../../translated_images/he/testing-pyramid.2dd1079a0481e53e.webp" alt="פירמידת בדיקה" width="800"/>

*פירמידת בדיקה המציגה את האיזון בין בדיקות יחידה (מהירות, מבודדות), בדיקות אינטגרציה (רכיבים אמיתיים) ובדיקות מקצה לקצה. הכשרה זו מכסה בדיקות יחידה.*

| מודול | בדיקות | מיקוד | קבצים עיקריים |
|--------|-------|-------|-----------|
| **00 - התחלה מהירה** | 6 | תבניות פקודות והחלפת משתנים | `SimpleQuickStartTest.java` |
| **01 - מבוא** | 8 | זיכרון שיחה וצ׳אט מדינתי | `SimpleConversationTest.java` |
| **02 - הנדסת פקודות** | 12 | דפוסי GPT-5.2, דרגות נכונות, פלט מובנה | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | ספיגת מסמכים, הטמעות, חיפוש דמיון | `DocumentServiceTest.java` |
| **04 - כלים** | 12 | קריאת פונקציות ורצף כלים | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | פרוטוקול הקשר לדגם עם תקשורת stdio | `SimpleMcpTest.java` |

## הרצת הבדיקות

**הפעל את כל הבדיקות מהספרייה הראשית:**

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

**הרץ מחלקת בדיקות אחת:**

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
mvn test -Dtest=SimpleConversationTest#האם לשמור על היסטוריית שיחה
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#יש לשמור על היסטוריית שיחה
```

## הרצת בדיקות ב-VS Code

אם אתה משתמש ב-Visual Studio Code, חלון Test Explorer מספק ממשק גרפי להרצה ודיבוג של הבדיקות.

<img src="../../../translated_images/he/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*חלון VS Code Test Explorer המציג עץ בדיקות עם כל מחלקות הבדיקה בג׳אווה ושיטות בדיקה בודדות*

**כיצד להפעיל בדיקות ב-VS Code:**

1. פתח את Test Explorer על ידי לחיצה על סמל הכור במערכת הפעילות
2. פתח את עץ הבדיקות בכדי לראות את כל המודולים ומחלקות הבדיקה
3. לחץ על כפתור ההפעלה ליד כל בדיקה להפעלה בודדת שלה
4. לחץ על "הרץ את כל הבדיקות" כדי להריץ את כל הסט
5. לחץ על הכפתור הימני בכל בדיקה ובחר "Debug Test" להגדרת נקודות עצירה ושלב דרך הקוד

האקספלורר מציג סימני וי ירוקים לבדיקות שעוברות ומספק הודעות כשל מפורטות כשיש כישלון.

## דפוסי בדיקה

### דפוס 1: בדיקת תבניות פקודות

הדפוס הפשוט ביותר בודק תבניות פקודות מבלי לקרוא למודל AI. אתה מוודא שהחלפת המשתנים עובדת כהלכה והפקודות מעוצבות כצפוי.

<img src="../../../translated_images/he/prompt-template-testing.b902758ddccc8dee.webp" alt="בדיקת תבניות פקודות" width="800"/>

*בדיקת תבניות פקודות המראה את זרימת החלפת המשתנים: תבנית עם מקומות מילוי → ערכים מוחלים → הפלט המעוצב נבדק*

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

בדיקה זו נמצאת ב־`00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**הפעל אותה:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#בדיקת עיצוב תבנית ההנחיה
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#פורמט תבנית הבקשה למבחן
```

### דפוס 2: יצירת דגמים מדומים (Mocking)

כשבודקים לוגיקת שיחה, השתמש ב-Mockito ליצירת דגמים מדומים שמחזירים תגובות קבועות מראש. זה מייעל את הבדיקות והופך אותן למהירות, חינמיות ודטרמיניסטיות.

<img src="../../../translated_images/he/mock-vs-real.3b8b1f85bfe6845e.webp" alt="השוואת קריאת API מדומה מול אמיתית" width="800"/>

*השוואה שמדגימה מדוע עדיף להשתמש בדגמי מוק עבור בדיקות: הם מהירים, חינמיים, ודטרמיניסטיים ובנוסף אינם דורשים מפתחות API*

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

דפוס זה מופיע ב־`01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. המוק מבטיח התנהגות עקבית כך שניתן לוודא שזיכרון השיחה מנוהל כהלכה.

### דפוס 3: בדיקת בידוד שיחה

זיכרון השיחה חייב להפריד בין משתמשים שונים. בדיקה זו מוודאת ששיחות לא מתערבבות זו בזו.

<img src="../../../translated_images/he/conversation-isolation.e00336cf8f7a3e3f.webp" alt="בידוד שיחה" width="800"/>

*בדיקת בידוד שיחה שמראה ניתוב זיכרונות נפרדים למשתמשים שונים למניעת עירבוב הקשר*

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

כל שיחה שומרת היסטוריה עצמאית משלה. במערכות ייצור, הבידוד הזה הוא קריטי ליישומים מרובי משתמשים.

### דפוס 4: בדיקת כלים באופן עצמאי

כלים הם פונקציות שה-AI יכול לקרוא להן. בדוק אותם ישירות כדי לוודא שהם עובדים נכון בלי תלות בהחלטות AI.

<img src="../../../translated_images/he/tools-testing.3e1706817b0b3924.webp" alt="בדיקת כלים" width="800"/>

*בדיקת כלים באופן עצמאי המראה הפעלת כלים מדומים ללא קריאות AI לאימות הלוגיקה העסקית*

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

בדיקות אלו מ־`04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` מאמתות לוגיקת כלים ללא מעורבות AI. דוגמת הרצף מראה כיצד פלט של כלי אחד מוזן כקלט לאחר.

### דפוס 5: בדיקת RAG בזיכרון

מערכות RAG במקור דורשות מסדי נתונים וקטוריים ושירותי הטמעות. דפוס הבדיקה בזיכרון מאפשר לבדוק את כל הצינור ללא תלות חיצונית.

<img src="../../../translated_images/he/rag-testing.ee7541b1e23934b1.webp" alt="בדיקת RAG בזיכרון" width="800"/>

*זרימת בדיקת RAG בזיכרון שמדגימה ניתוח מסמכים, אחסון הטמעות, וחיפוש דמיון ללא צורך במסד נתונים*

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

בדיקה זו מ־`03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` יוצרת מסמך בזיכרון ובודקת את פיצולו ואת ניהול המידע המטא.

### דפוס 6: בדיקת אינטגרציה של MCP

מודול MCP בודק את אינטגרציית פרוטוקול הקשר לדגם באמצעות תקשורת stdio. בדיקות אלו בודקות שהיישום שלך יכול ליצור ולתקשר עם שרתי MCP כתהליכים משניים.

בדיקות ב־`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` מאמתות את התנהגות לקוח MCP.

**הפעל אותן:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## פילוסופיית בדיקה

בדוק את הקוד שלך, לא את ה-AI. הבדיקות שלך צריכות לאמת את הקוד שכתבת על ידי בדיקה כיצד נבנות הפקודות, כיצד מנוהל הזיכרון וכיצד הכלים מתבצעים. תגובות AI משתנות ואינן צריכות להיות חלק מההנחות בבדיקה. שאל את עצמך האם תבנית הפקודה שלך מחליפה משתנים כראוי, לא האם ה-AI נותן את התשובה הנכונה.

השתמש במוקים לדגמי השפה. אלה תלות חיצונית שיכולה להיות איטית, יקרה ואינה דטרמיניסטית. מוקים מקצרים את זמן הבדיקה למילישניות במקום שניות, חינמיים ללא עלויות API, ודטרמיניסטיים עם תוצאה זהה בכל פעם.

שמור על בדיקות עצמאיות. כל בדיקה צריכה להגדיר את הנתונים שלה, לא להסתמך על בדיקות אחרות, ולנקות אחריה. בדיקות צריכות לעבור בכל סדר הרצה.

בדוק מקרים קיצוניים מעבר לדרך הטבעית. נסה קלטים ריקים, גדולים מאוד, תווים מיוחדים, פרמטרים לא תקינים, ותנאי גבול. אלה לעיתים חושפים באגים שלא מופיעים בשימוש רגיל.

השתמש בשמות תיאורים. השווה בין `shouldMaintainConversationHistoryAcrossMultipleMessages()` ל־`test1()`. הראשון אומר בדיוק מה נבדק, מה שמקל על איתור תקלות.

## השלבים הבאים

כעת כשאתה מבין את דפוסי הבדיקה, צלול עמוק יותר בכל מודול:

- **[00 - התחלה מהירה](../00-quick-start/README.md)** - התחיל מבסיס תבניות פקודות
- **[01 - מבוא](../01-introduction/README.md)** - למד ניהול זיכרון שיחה
- **[02 - הנדסת פקודות](../02/prompt-engineering/README.md)** - התמחה בדפוסי הנעת GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - בנה מערכות יצור-מגובה בדילוגים
- **[04 - כלים](../04-tools/README.md)** - מימש קריאות לפונקציות ורצפי כלים
- **[05 - MCP](../05-mcp/README.md)** - שלב פרוטוקול הקשר לדגם

קובצי README של כל מודול מספקים הסברים מפורטים על המושגים הנבדקים כאן.

---

**ניווט:** [← חזרה לעמוד הראשי](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב ויתור**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדייק, יש להבחין כי תרגומים אוטומטיים עלולים לכלול שגיאות או אי דיוקים. המסמך המקורי בשפת המקור שלו הוא המקור הסמכותי. עבור מידע קריטי מומלץ להיעזר בתרגום מקצועי אנושי. אנו לא נושאים באחריות לכל אי הבנה או פרשנות שגויה הנובעת משימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->