# מודול 00: התחלה מהירה

## תוכן העניינים

- [הקדמה](../../../00-quick-start)
- [מה זה LangChain4j?](../../../00-quick-start)
- [תלויות LangChain4j](../../../00-quick-start)
- [דרישות מוקדמות](../../../00-quick-start)
- [התקנה](../../../00-quick-start)
  - [1. קבל את אסימון ה-GitHub שלך](../../../00-quick-start)
  - [2. הגדר את האסימון שלך](../../../00-quick-start)
- [הרץ את הדוגמאות](../../../00-quick-start)
  - [1. שיחת בסיס](../../../00-quick-start)
  - [2. תבניות פרומפט](../../../00-quick-start)
  - [3. קריאת פונקציות](../../../00-quick-start)
  - [4. שאלות ותשובות על מסמכים (Easy RAG)](../../../00-quick-start)
  - [5. בינה מלאכותית אחראית](../../../00-quick-start)
- [מה כל דוגמה מציגה](../../../00-quick-start)
- [שלבים הבאים](../../../00-quick-start)
- [פתרון בעיות](../../../00-quick-start)

## הקדמה

ההתחלה המהירה הזו מיועדת לקבל אותך עובדים עם LangChain4j במהירות האפשרית. היא מכסה את הבסיס המוחלט בבניית יישומי בינה מלאכותית עם LangChain4j ודגמי GitHub. במודולים הבאים תעבור ל-Azure OpenAI ו-GPT-5.2 ותעמיק בכל מושג.

## מה זה LangChain4j?

LangChain4j היא ספריית Java שמפשטת את בניית היישומים המונעים בינה מלאכותית. במקום להתמודד עם לקוחות HTTP וניתוח JSON, אתה עובד עם ממשקי API נקיים של Java.

המילה "chain" ב-LangChain מתייחסת לקישור של רכיבים מרובים יחד - ניתן לקשר פרומפט לדגם, לדפדפן, או לקשר קריאות AI מרובות יחד כאשר פלט אחד מזין את הקלט הבא. התחלת מהירה זו מתמקדת ביסודות לפני חקירת שרשראות מורכבות יותר.

<img src="../../../translated_images/he/langchain-concept.ad1fe6cf063515e1.webp" alt="רעיונון השרשרת LangChain4j" width="800"/>

*שרשור רכיבים ב-LangChain4j - אבני בניין שמתחברים ליצירת זרימות עבודה עוצמתיות של בינה מלאכותית*

נשתמש בשלושה רכיבים מרכזיים:

**ChatModel** - הממשק לאינטראקציות עם מודל AI. קרא `model.chat("prompt")` וקבל מחרוזת תגובה. אנו משתמשים ב-`OpenAiOfficialChatModel` שעובד עם נקודות קצה תואמות OpenAI כמו דגמי GitHub.

**AiServices** - יוצר ממשקי שירות AI בטוחים טיפוסית. הגדר שיטות, סמן אותן ב-`@Tool`, ו-LangChain4j מנהל את התזמור. ה-AI קורא אוטומטית את שיטות ה-Java שלך כשנדרש.

**MessageWindowChatMemory** - שומר היסטוריית שיחה. בלעדיו, כל בקשה היא עצמאית. איתו, ה-AI זוכר הודעות קודמות ושומר הקשר במספר סבבים.

<img src="../../../translated_images/he/architecture.eedc993a1c576839.webp" alt="ארכיטקטורת LangChain4j" width="800"/>

*ארכיטקטורת LangChain4j - רכיבים מרכזיים העובדים ביחד להנעת יישומי ה-AI שלך*

## תלויות LangChain4j

ההתחלה המהירה הזו משתמשת בשלוש תלויות של Maven בקובץ [`pom.xml`](../../../00-quick-start/pom.xml):

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

מודול `langchain4j-open-ai-official` מספק את המחלקה `OpenAiOfficialChatModel` שמתחברת ל-APIs תואמי OpenAI. דגמי GitHub משתמשים באותו פורמט API, כך שאין צורך במתאם מיוחד - פשוט הצבע על כתובת הבסיס ל-`https://models.github.ai/inference`.

מודול `langchain4j-easy-rag` מספק חלוקה, הטמעה ושליפה אוטומטית של מסמכים כך שתוכל לבנות יישומי RAG ללא הגדרה ידנית של כל שלב.

## דרישות מוקדמות

**משתמש ב-Dev Container?** Java ו-Maven מותקנים מראש. כל מה שצריך הוא אסימון גישה אישי ל-GitHub.

**פיתוח מקומי:**
- Java 21+, Maven 3.9+
- אסימון גישה אישי ל-GitHub (הוראות למטה)

> **הערה:** מודול זה משתמש ב-`gpt-4.1-nano` מדגמי GitHub. אל תשנה את שם הדגם בקוד - הוא מוגדר לעבוד עם דגמי GitHub הזמינים.

## התקנה

### 1. קבל את אסימון ה-GitHub שלך

1. עבור אל [הגדרות GitHub → אסימוני גישה אישית](https://github.com/settings/personal-access-tokens)
2. לחץ על "Generate new token"
3. קבע שם תיאורי (למשל, "LangChain4j Demo")
4. הגדר תוקף (מומלץ 7 ימים)
5. תחת "הרשאות חשבון", אתר "Models" והגדר לקריאה בלבד
6. לחץ על "Generate token"
7. העתק ושמור את האסימון - לא תראה אותו שוב

### 2. הגדר את האסימון שלך

**אפשרות 1: שימוש ב-VS Code (מומלץ)**

אם אתה משתמש ב-VS Code, הוסף את האסימון שלך לקובץ `.env` בספריית הפרויקט:

אם קובץ `.env` לא קיים, העתק את `.env.example` ל-`.env` או צור קובץ `.env` חדש בספריית הפרויקט.

**קובץ `.env` לדוגמה:**
```bash
# בקובץ /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

אז תוכל פשוט ללחוץ קליק ימני על כל קובץ דמו (למשל `BasicChatDemo.java`) בסייר ולבחור **"Run Java"**, או להשתמש בהרצות מתוך לוח ההפעלה והניפוי.

**אפשרות 2: שימוש בטרמינל**

הגדר את האסימון כמשתנה סביבה:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## הרץ את הדוגמאות

**שימוש ב-VS Code:** פשוט לחץ קליק ימני על כל קובץ דמו בסייר ובחר **"Run Java"**, או השתמש בהרצות מלוח ההפעלה והניפוי (ודא שהוספת את האסימון לקובץ `.env` קודם).

**שימוש ב-Maven:** לחלופין, תוכל להריץ משורת הפקודה:

### 1. שיחת בסיס

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. תבניות פרומפט

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

מציג אפס-שיט, כמה-שיט, שרשרת מחשבה, ופרומפטים מבוססי תפקיד.

### 3. קריאת פונקציות

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

ה-AI קורא אוטומטית את שיטות ה-Java שלך כשנדרש.

### 4. שאלות ותשובות על מסמכים (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

שאל שאלות על המסמכים שלך באמצעות Easy RAG עם הטמעה ושליפה אוטומטית.

### 5. בינה מלאכותית אחראית

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

ראה כיצד מסנני בטיחות AI חוסמים תוכן מזיק.

## מה כל דוגמה מציגה

**שיחת בסיס** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

התחל כאן כדי לראות את LangChain4j בפשטותו. תיצור `OpenAiOfficialChatModel`, תשלח פרומפט עם `.chat()`, ותקבל תגובה בחזרה. זה מדגים את היסודות: איך לאתחל דגמים עם נקודות קצה מקוסטמות ומפתחות API. ברגע שתבין את התבנית הזו, כל השאר מתבסס עליה.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 נסה עם צ'אט [GitHub Copilot](https://github.com/features/copilot):** פתח את [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ושאל:
> - "איך אני מעביר מדגמי GitHub ל-Azure OpenAI בקוד הזה?"
> - "אילו פרמטרים נוספים אני יכול להגדיר ב-OpenAiOfficialChatModel.builder()?"
> - "איך מוסיפים תגובות סטרימינג במקום לחכות לתגובה מלאה?"

**הנדסת פרומפט** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

עכשיו כשאתה יודע איך לדבר עם דגם, בוא נחקור מה אתה אומר לו. הדמו הזה משתמש באותה הגדרת דגם אך מציג חמישה דפוסי פרומפט שונים. נסה פרומפטים אפס-שיט להוראות ישירות, כמה-שיט שלומדים מדוגמאות, שרשרת מחשבה שחושפת שלבי הסבר, ופרומפטים מבוססי תפקיד שמגדירים הקשר. תראה איך אותו דגם נותן תוצאות שונות מאוד לפי אופן ניסוח הבקשה.

הדמו גם מציג תבניות פרומפט, דרך עוצמתית ליצור פרומפטים ברי שימוש חוזר עם משתנים.
הדוגמה למטה מראה פרומפט שמשתמש ב-PromptTemplate של LangChain4j למילוי משתנים. ה-AI יענה בהתבסס על היעד והפעילות שסופקו.

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

> **🤖 נסה עם צ'אט [GitHub Copilot](https://github.com/features/copilot):** פתח את [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ושאל:
> - "מה ההבדל בין אפס-שיט לכמה-שיט, ומתי כדאי להשתמש בכל אחד?"
> - "איך פרמטר הטמפרטורה משפיע על תגובות המודל?"
> - "אילו טכניקות קיימות למניעת התקפות הזרקת פרומפט בפרודקשן?"
> - "איך ליצור אובייקטים של PromptTemplate ברי שימוש חוזר לתבניות נפוצות?"

**אינטגרציית כלים** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

כאן LangChain4j הופך לעוצמתי. תשתמש ב-`AiServices` כדי ליצור עוזר AI שיכול לקרוא לשיטות ה-Java שלך. פשוט סמן שיטות ב-`@Tool("תיאור")` ו-LangChain4j מטפל בשאר - ה-AI מחליט אוטומטית מתי להשתמש בכל כלי לפי מה שהמשתמש מבקש. זה מדגים קריאת פונקציות, טכניקה מרכזית לבניית AI שיכול לפעול, לא רק לענות על שאלות.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 נסה עם צ'אט [GitHub Copilot](https://github.com/features/copilot):** פתח את [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ושאל:
> - "איך באנוטציית @Tool עובדת ומה LangChain4j עושה איתה מאחורי הקלעים?"
> - "האם ה-AI יכול לקרוא לכלים מרובים ברצף לפתור בעיות מורכבות?"
> - "מה קורה אם כלי זורק חריגה - איך להתמודד עם שגיאות?"
> - "איך הייתי משלב API אמיתי במקום דוגמת המכונה המכפלה הזו?"

**שאלות ותשובות על מסמכים (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

כאן תראה RAG (הפקה משודרגת בשליפה) באמצעות גישת "Easy RAG" של LangChain4j. מסמכים מועלים, מחולקים ומוטמעים אוטומטית בחנות זיכרון, ואז מחלץ תוכן מספק חתיכות רלוונטיות ל-AI בזמן השאילתה. ה-AI עונה על סמך המסמכים שלך, לא הידע הכללי שלו.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 נסה עם צ'אט [GitHub Copilot](https://github.com/features/copilot):** פתח את [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ושאל:
> - "איך RAG מונע הזיות AI לעומת שימוש בנתוני אימון המודל?"
> - "מה ההבדל בין הגישה הפשוטה הזו לצינור RAG מותאם אישית?"
> - "איך הייתי מגדיל את זה לטפל במסמכים מרובים או בסיסי ידע גדולים יותר?"

**בינה מלאכותית אחראית** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

בנה בטיחות AI עם הגנה לעומק. הדמו מציג שני שכבות הגנה שפועלות יחד:

**חלק 1: משמר קלט LangChain4j** - חוסם פרומפטים מסוכנים לפני שישיגו את ה-LLM. צור משמרות קלט מותאמות שמאתרות מילים או דפוסים אסורים. אלו רצות בקוד שלך, כך שהן מהירות וללא עלות.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**חלק 2: מסנני בטיחות הספק** - לדגמי GitHub יש מסננים מובנים שתופסים מה שהמשמרות שלך עלולות לפספס. תראה חסימות קשות (שגיאות HTTP 400) להפרות חמורות וסירובים רכים שבהם ה-AI מסרב בנימוס.

> **🤖 נסה עם צ'אט [GitHub Copilot](https://github.com/features/copilot):** פתח את [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ושאל:
> - "מה זה InputGuardrail ואיך יוצרים משמר קלט משלך?"
> - "מה ההבדל בין חסימה קשה לסירוב רך?"
> - "למה להשתמש גם במשמרות קלט וגם במסנני ספקים יחד?"

## שלבים הבאים

**המודול הבא:** [01-introduction - התחלה עם LangChain4j](../01-introduction/README.md)

---

**ניווט:** [← חזרה לעמוד ראשי](../README.md) | [הבא: מודול 01 - הקדמה →](../01-introduction/README.md)

---

## פתרון בעיות

### בניית Maven בפעם הראשונה

**בעיה**: `mvn clean compile` או `mvn package` ראשוני אורך זמן רב (10-15 דקות)

**סיבה**: Maven צריך להוריד את כל התלויות של הפרויקט (Spring Boot, ספריות LangChain4j, SDKים של Azure וכו') בבנייה הראשונה.

**פתרון**: זה התנהגות תקינה. הבניות הבאות יהיו מהירות יותר כי התלויות נשמרות במטמון במחשב המקומי. זמן ההורדה תלוי במהירות החיבור שלך.

### תחביר פקודת Maven ב-PowerShell

**בעיה**: פקודות Maven נכשלות עם השגיאה `Unknown lifecycle phase ".mainClass=..."`
**סיבה**: PowerShell מפרש את `=` כאופרטור להקצאת משתנה, ובכך שובר את תחביר התכונות של Maven

**פתרון**: השתמש באופרטור עצירת הניתוח `--%` לפני פקודת Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

האופרטור `--%` מורה ל-PowerShell להעביר את כל הפרמטרים הנותרים כמו שהם ל-Maven ללא פירוש.

### הצגת אימוג'ים ב-Windows PowerShell

**בעיה**: תגובות של AI מציגות תווים משובשים (למשל, `????` או `â??`) במקום אימוג'ים ב-PowerShell

**סיבה**: הקידוד הברירת מחדל של PowerShell אינו תומך באימוג'ים ב-UTF-8

**פתרון**: הרץ את הפקודה הזו לפני ביצוע אפליקציות Java:
```cmd
chcp 65001
```

זה מאלץ קידוד UTF-8 במסוף. לחלופין, השתמש ב-Windows Terminal שיש לו תמיכה טובה יותר ב-Unicode.

### ניתוח שגיאות בקריאות API

**בעיה**: שגיאות אימות, הגבלות על קצב, או תגובות בלתי צפויות מהמודל של ה-AI

**פתרון**: הדוגמאות כוללות `.logRequests(true)` ו-`.logResponses(true)` להצגת קריאות API בקונסולה. זה עוזר לאתר שגיאות אימות, הגבלות קצב, או תגובות בלתי צפויות. הסר דגלים אלו בפרודקשן להפחתת רעש ברישומים.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב ויתור**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדייק, יש להיות מודעים לכך שתרגומים אוטומטיים עלולים להכיל שגיאות או אי-דיוקים. יש לראות במסמך המקורי בשפה המקורית כמקור הסמכות. למידע קריטי מומלץ להשתמש בתרגום מקצועי על ידי מתרגם אנושי. אנו לא נושאים באחריות על כל אי-הבנות או פירושים שגויים הנובעים משימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->