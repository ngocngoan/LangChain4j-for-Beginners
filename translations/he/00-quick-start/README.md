# מודול 00: התחלה מהירה

## תוכן העניינים

- [הקדמה](../../../00-quick-start)
- [מה זה LangChain4j?](../../../00-quick-start)
- [תלויות LangChain4j](../../../00-quick-start)
- [דרישות מוקדמות](../../../00-quick-start)
- [התקנה](../../../00-quick-start)
  - [1. קבל את הטוקן שלך בגיטאהאב](../../../00-quick-start)
  - [2. הגדר את הטוקן שלך](../../../00-quick-start)
- [הרץ את הדוגמאות](../../../00-quick-start)
  - [1. צ'אט בסיסי](../../../00-quick-start)
  - [2. דפוסי פקודות](../../../00-quick-start)
  - [3. קריאת פונקציות](../../../00-quick-start)
  - [4. שאלות ותשובות במסמכים (Easy RAG)](../../../00-quick-start)
  - [5. בינה אחראית](../../../00-quick-start)
- [מה כל דוגמה מציגה](../../../00-quick-start)
- [השלבים הבאים](../../../00-quick-start)
- [פתרון בעיות](../../../00-quick-start)

## הקדמה

התחלה מהירה זו נועדה לעזור לך להתחיל לעבוד עם LangChain4j במהירות האפשרית. היא מכסה את היסודות הבסיסיים של בניית יישומי בינה מלאכותית עם LangChain4j ו-GitHub Models. במודולים הבאים תשתמש ב-Azure OpenAI יחד עם LangChain4j כדי לבנות יישומים מתקדמים יותר.

## מה זה LangChain4j?

LangChain4j היא ספריית Java שמפשטת את בניית יישומים מונעי בינה מלאכותית. במקום להתמודד עם לקוחות HTTP וניתוח JSON, אתה עובד עם ממשקי API נקיים ב-Java.

"שרשרת" ב-LangChain מתייחסת לקישור יחד של רכיבים מרובים – ייתכן ותקשר פקודה למודל, מפענח, או תרכיב קריאות AI מרובות כאשר פלט אחד מזין את הקלט הבא. התחלה מהירה זו מתמקדת ביסודות לפני חקירת שרשראות מורכבות יותר.

<img src="../../../translated_images/he/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*שרשור רכיבים ב-LangChain4j - בלוקים בסיסיים שמתחברים ליצירת זרימות עבודה חזקות של בינה מלאכותית*

נשתמש בשלושה רכיבים מרכזיים:

**ChatModel** - הממשק לאינטראקציות עם מודל הבינה המלאכותית. תקרא `model.chat("prompt")` ותקבל מחרוזת תגובה. אנחנו משתמשים ב-`OpenAiOfficialChatModel` שעובד עם נקודות קצה התואמות ל-OpenAI כמו GitHub Models.

**AiServices** - יוצר ממשקי שירות AI בטוחים טיפוסית. הגדיר שיטות, סמן אותן עם `@Tool`, ו-LangChain4j מטפל באורקסטרציה. הבינה המלאכותית קוראת אוטומטית לשיטות ה-Java שלך במידת הצורך.

**MessageWindowChatMemory** - שומר היסטוריית שיחות. בלעדיה, כל בקשה עצמאית. איתה, הבינה זוכרת הודעות קודמות ומתחזקת הקשר לאורך סבבים מרובים.

<img src="../../../translated_images/he/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*ארכיטקטורת LangChain4j - רכיבים מרכזיים שעובדים יחד כדי להניע את יישומי הבינה המלאכותית שלך*

## תלויות LangChain4j

התחלה מהירה זו משתמשת בשלוש תלויות Maven בקובץ [`pom.xml`](../../../00-quick-start/pom.xml):

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
  
מודול `langchain4j-open-ai-official` מספק את המחלקה `OpenAiOfficialChatModel` שמתחברת ל-API תואם OpenAI. GitHub Models משתמש באותו פורמט API, לכן אין צורך במתאם מיוחד – פשוט הפנה את כתובת הבסיס ל-`https://models.github.ai/inference`.

מודול `langchain4j-easy-rag` מספק חלוקה אוטומטית של מסמכים, הטמעה ושליפה כך שתוכל לבנות יישומי RAG מבלי להגדיר כל שלב ידנית.

## דרישות מוקדמות

**שימוש במכולת הפיתוח?** Java ו-Maven מותקנים כבר. דרוש לך רק טוקן גישה אישי ל-GitHub.

**פיתוח מקומי:**  
- Java 21 ומעלה, Maven 3.9 ומעלה  
- טוקן גישה אישי ל-GitHub (הוראות למטה)

> **הערה:** מודול זה משתמש ב-`gpt-4.1-nano` מ-GitHub Models. אל תשנה את שם המודל בקוד – הוא מוגדר לעבודה עם המודלים הזמינים של GitHub.

## התקנה

### 1. קבל את הטוקן שלך בגיטאהאב

1. עבור ל-[הגדרות GitHub → טוקני גישה אישיים](https://github.com/settings/personal-access-tokens)  
2. לחץ על "צור טוקן חדש"  
3. הגדר שם תיאורי (למשל, "LangChain4j Demo")  
4. הגדר תפוגה (7 ימים מומלץ)  
5. ב"Permissions", מצא "Models" והגדר ל"רק קריאה"  
6. לחץ על "צור טוקן"  
7. העתק ושמור את הטוקן – לא תוכל לראותו שוב  

### 2. הגדר את הטוקן שלך

**אפשרות 1: שימוש ב-VS Code (מומלץ)**

אם אתה משתמש ב-VS Code, הוסף את הטוקן לקובץ `.env` בתיקיית הפרויקט הראשית:

אם קובץ `.env` לא קיים, העתק `.env.example` ל-`.env` או צור קובץ `.env` חדש בתיקיית הפרויקט הראשית.

**דוגמת קובץ `.env`:**  
```bash
# בקובץ /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```
  
אז תוכל פשוט ללחוץ קליק ימני על כל קובץ דמו (למשל `BasicChatDemo.java`) ב-Explorer ולבחור **"Run Java"** או להשתמש בקונפיגורציות ההרצה בלוח ההרצה והדיבאג.

**אפשרות 2: שימוש בטרמינל**

הגדר את הטוקן כמשתנה סביבה:

**Bash:**  
```bash
export GITHUB_TOKEN=your_token_here
```
  
**PowerShell:**  
```powershell
$env:GITHUB_TOKEN=your_token_here
```
  

## הרץ את הדוגמאות

**שימוש ב-VS Code:** פשוט לחץ קליק ימני על כל קובץ דמו ב-Explorer ובחר **"Run Java"**, או השתמש בקונפיגורציות ההרצה בלוח ההרצה והדיבאג (ודא שהוספת את הטוקן לקובץ `.env` קודם).

**שימוש ב-Maven:** לחלופין, תוכל להריץ משורת הפקודה:

### 1. צ'אט בסיסי

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```
  

### 2. דפוסי פקודות

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```
  
מציג הפקדות zero-shot, few-shot, chain-of-thought, והנחיות מבוססות תפקיד.

### 3. קריאת פונקציה

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
ה-AI קורא אוטומטית לשיטות ה-Java שלך כשהן נדרשות.

### 4. שאלות ותשובות במסמכים (Easy RAG)

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
שאל שאלות על המסמכים שלך באמצעות Easy RAG עם הטמעה ושליפה אוטומטיות.

### 5. בינה אחראית

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```
  
ראה כיצד מסנני ביטחון AI חוסמים תוכן מזיק.

## מה כל דוגמה מציגה

**צ'אט בסיסי** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

תתחיל כאן לראות את LangChain4j בפשטותו. תיצור `OpenAiOfficialChatModel`, תשלח פקודה עם `.chat()`, ותקבל תגובה. זה מדגים את הבסיס: כיצד לאתחל מודלים עם נקודות קצה ומפתחות API מותאמים. ברגע שתבין דפוס זה, כל השאר נבנה עליו.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```
  
> **🤖 נסה עם [GitHub Copilot](https://github.com/features/copilot) Chat:** פתח את [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ושאל:  
> - "איך אני מעביר ממודלי GitHub ל-Azure OpenAI בקוד הזה?"  
> - "אילו פרמטרים נוספים ניתן להגדיר ב-OpenAiOfficialChatModel.builder()?"  
> - "איך מוסיפים תגובות בשידור במקום להמתין לתגובה המלאה?"

**הנדסת פקודות** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

עכשיו כשאתה יודע איך לדבר עם מודל, בוא נחקור מה אתה אומר לו. הדמו משתמש באותה תצורת מודל אבל מציג חמישה דפוסי פקודות שונים. נסה פקודות zero-shot להוראות ישירות, few-shot ללמוד מדוגמאות, chain-of-thought שמחשפות שלבי ניתוח, ופקודות מבוססות תפקיד שמגדירות הקשר. תראה איך אותו מודל נותן ת 결과 שונים דרמטיים בהתאם לאיך שאתה מנוסח.

הדמו גם מדגים תבניות פקודות שזו דרך עוצמתית ליצור פקודות שניתן להשתמש בהן שוב עם משתנים.  
הדוגמה הבאה מציגה פקודה המשתמשת ב-LangChain4j `PromptTemplate` למילוי משתנים. ה-AI יענה בהתאם ליעד ופעילות שניתנו.

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
  
> **🤖 נסה עם [GitHub Copilot](https://github.com/features/copilot) Chat:** פתח את [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ושאל:  
> - "מה ההבדל בין פקודות zero-shot ו-few-shot ומתי כדאי להשתמש בכל אחת?"  
> - "איך פרמטר הטמפרטורה משפיע על תגובות המודל?"  
> - "מהן טכניקות למניעת מתקפות הזרקת פקודות בפרודקשן?"  
> - "איך ליצור אובייקטי PromptTemplate שניתן לשימוש חוזר לדפוסים נפוצים?"

**שילוב כלים** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

כאן LangChain4j הופך לחזק. תשתמש ב-`AiServices` כדי ליצור עוזר AI שיכול לקרוא לשיטות ה-Java שלך. פשוט סמן שיטות עם `@Tool("תיאור")` ו-LangChain4j מטפל בכל השאר – ה-AI מחליט אוטומטית מתי להשתמש בכל כלי בהתאם לשאלות המשתמש. זה מדגים קריאת פונקציות, טכניקה מרכזית לבניית AI שיכול לבצע פעולות, לא רק לענות על שאלות.

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
  
> **🤖 נסה עם [GitHub Copilot](https://github.com/features/copilot) Chat:** פתח את [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ושאל:  
> - "איך עובד ה-@Tool ומה LangChain4j עושה איתו מאחורי הקלעים?"  
> - "האם ה-AI יכול לקרוא מספר כלים ברצף כדי לפתור בעיות מורכבות?"  
> - "מה קורה אם כלי זורק חריגה – איך לטפל בשגיאות?"  
> - "איך אני משלב API אמיתי במקום הדוגמה של המחשבון?"

**שאלות ותשובות במסמכים (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

כאן תראה RAG (יצירה משולבת עם שליפה) באמצעות גישת "Easy RAG" של LangChain4j. מסמכים נטענים, מחולקים ומשולבים אוטומטית לאחסון בזכרון, ואז משיב תוכן מספק מקטעים רלוונטיים ל-AI בזמן השאילתה. הבינה עונה בהתבסס על המסמכים שלך, לא על הידע הכללי שלה.

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
  
> **🤖 נסה עם [GitHub Copilot](https://github.com/features/copilot) Chat:** פתח את [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ושאל:  
> - "איך RAG מונע הזיות AI לעומת שימוש בנתוני האימון של המודל?"  
> - "מה ההבדל בין הגישה הקלה הזו לבין צנרת RAG מותאמת אישית?"  
> - "איך אני מרחיב את זה לטפל במסמכים מרובים או מאגרי ידע גדולים יותר?"

**בינה אחראית** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

בנה בטיחות AI עם הגנה מעמיקה. הדמו מראה שתי שכבות הגנה שפועלות יחד:

**חלק 1: משמרות קלט LangChain4j** - חוסמות פקודות מסוכנות לפני שהן מגיעות ל-LLM. צור משמרות מותאמות אישית שבודקות מילות מפתח או דפוסים אסורים. אלו רצות בקוד שלך, ולכן מהירות וחינמיות.

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
  
**חלק 2: מסנני בטיחות הספק** - ל-GitHub Models יש מסננים מובנים שתופסים מה שהמשמרות שלך עלולות לפספס. תראה חסימות קשות (שגיאות HTTP 400) להפרות חמורות וסירובים נעימים שבהם ה-AI מסרב בנימוס.

> **🤖 נסה עם [GitHub Copilot](https://github.com/features/copilot) Chat:** פתח את [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ושאל:  
> - "מהו InputGuardrail ואיך אני יוצר משמרות משלי?"  
> - "מה ההבדל בין חסימה קשה לסירוב רך?"  
> - "למה להשתמש גם במשמרות קלט וגם במסנני ספק יחד?"

## השלבים הבאים

**המודול הבא:** [01-introduction - התחלה עם LangChain4j ו-gpt-5 על Azure](../01-introduction/README.md)

---

**ניווט:** [← חזרה לעיקרי](../README.md) | [הבא: מודול 01 - הקדמה →](../01-introduction/README.md)

---

## פתרון בעיות

### בניית Maven בפעם הראשונה

**בעיה:** `mvn clean compile` או `mvn package` הראשוני לוקח זמן רב (10-15 דקות)

**סיבה:** Maven צריך להוריד את כל התלויות של הפרויקט (Spring Boot, ספריות LangChain4j, SDKs של Azure וכו') בבנייה הראשונה.

**פתרון:** זה התנהגות רגילה. הבניות הבאות יהיו מהירות יותר כי התלויות מאוחסנות במטמון מקומי. זמן ההורדה תלוי במהירות הרשת שלך.

### תחביר פקודת Maven ב-PowerShell

**בעיה:** פקודות Maven נכשלות עם השגיאה `Unknown lifecycle phase ".mainClass=..."`
**סיבה**: PowerShell מפרש את `=` כאופרטור הקצאת משתנה, מה ששובר את תחביר התכונות של Maven

**פתרון**: השתמש באופרטור עצירת הפיענוח `--%` לפני הפקודה של Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

האופרטור `--%` מורה ל-PowerShell להעביר את כל הפרמטרים הנותרים כפי שהם ל-Maven ללא פיענוח.

### הצגת אימוג'ים ב-Windows PowerShell

**בעיה**: תגובות ה-AI מציגות תווים לא קריאים (למשל, `????` או `â??`) במקום אימוג'ים ב-PowerShell

**סיבה**: ההצפנה ברירת המחדל של PowerShell לא תומכת באימוג'ים ב-UTF-8

**פתרון**: הרץ פקודה זו לפני הרצת אפליקציות Java:
```cmd
chcp 65001
```

זה מאלץ הצפנה ב-UTF-8 בטרמינל. לחלופין, השתמש ב-Windows Terminal שיש לו תמיכה טובה יותר ב-Unicode.

### איתור שגיאות בקריאות API

**בעיה**: שגיאות אימות, הגבלות קצב, או תגובות בלתי צפויות מהדגם של ה-AI

**פתרון**: הדוגמאות כוללות `.logRequests(true)` ו-`.logResponses(true)` כדי להציג את קריאות ה-API בקונסול. זה עוזר לזהות שגיאות אימות, הגבלות קצב, או תגובות בלתי צפויות. הסר דגלים אלו בייצור כדי לצמצם רעש בלוג.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב ויתור**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). בעוד שאנו שואפים לדיוק, יש לקחת בחשבון כי תרגומים אוטומטיים עלולים להכיל שגיאות או אי-דיוקים. המסמך המקורי בשפתו המקורית הוא המקור הסמכותי שיש להסתמך עליו. למידע קריטי מומלץ להשתמש בתרגום מקצועי על ידי בני אדם. אנו לא נושאים באחריות לכל אי-הבנה או פרשנות שגויה הנובעת משימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->