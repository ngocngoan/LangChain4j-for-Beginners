# מודול 00: התחלה מהירה

## תוכן העניינים

- [מבוא](../../../00-quick-start)
- [מה זה LangChain4j?](../../../00-quick-start)
- [תלויות LangChain4j](../../../00-quick-start)
- [דרישות מוקדמות](../../../00-quick-start)
- [הגדרה](../../../00-quick-start)
  - [1. קבל את טוקן ה-GitHub שלך](../../../00-quick-start)
  - [2. הגדר את הטוקן שלך](../../../00-quick-start)
- [הרץ את הדוגמאות](../../../00-quick-start)
  - [1. שיחה בסיסית](../../../00-quick-start)
  - [2. דפוסי הפעלה](../../../00-quick-start)
  - [3. קריאת פונקציה](../../../00-quick-start)
  - [4. שאלות ותשובות על מסמכים (RAG)](../../../00-quick-start)
  - [5. בינה מלאכותית אחראית](../../../00-quick-start)
- [מה כל דוגמה מציגה](../../../00-quick-start)
- [השלבים הבאים](../../../00-quick-start)
- [פתרון תקלות](../../../00-quick-start)

## מבוא

ההתחלה המהירה הזו מיועדת להביא אותך לפעולה עם LangChain4j במהירות האפשרית. היא מכסה את היסודות המוחלטים של בניית יישומים מבוססי בינה מלאכותית עם LangChain4j ודגמי GitHub. במודולים הבאים תשתמש ב-Azure OpenAI עם LangChain4j כדי לבנות יישומים מתקדמים יותר.

## מה זה LangChain4j?

LangChain4j היא ספריית Java שמפשטת את בניית יישומים מונחי בינה מלאכותית. במקום להתמודד עם לקוחות HTTP וניתוח JSON, אתה עובד עם ממשקי Java נקיים.

"השרשרת" ב-LangChain מתייחסת לקישור בין רכיבים מרובים - אתה עשוי לקשר פרומפט למודל לפארסר, או לקשר מספר קריאות AI יחד כאשר הפלט של אחד מזין את הקלט של הבא. התחלה מהירה זו מתמקדת ביסודות לפני חקירת שרשראות מורכבות יותר.

<img src="../../../translated_images/he/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*קישור רכיבים ב-LangChain4j - אבני בניין שמתחברות ליצירת זרימות עבודה חזקות של AI*

נשתמש בשלושה רכיבים מרכזיים:

**ChatLanguageModel** - ממשק לאינטראקציות עם מודל הבינה המלאכותית. קוראים `model.chat("prompt")` ומקבלים מחרוזת תגובה. אנחנו משתמשים ב-`OpenAiOfficialChatModel` שעובד עם נקודות קצה תואמות OpenAI כגון דגמי GitHub.

**AiServices** - יוצר ממשקי שירות AI בטיפוסים בטוחים. מגדירים מתודות, מסמנים אותן ב-`@Tool`, ו-LangChain4j מטפל באורקסטרציה. ה-AI קורא אוטומטית למתודות Java שלך כשצריך.

**MessageWindowChatMemory** - שומר היסטוריית שיחה. בלעדיו, כל בקשה היא עצמאית. איתו, ה-AI זוכר הודעות קודמות ושומר על קונטקסט לאורך מספר סבבים.

<img src="../../../translated_images/he/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*ארכיטקטורת LangChain4j - רכיבים מרכזיים עובדים יחד להפעלת היישומים שלך עם AI*

## תלויות LangChain4j

התחלה מהירה זו משתמשת בשתי תלויות Maven בתוך [`pom.xml`](../../../00-quick-start/pom.xml):

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
```

מודול `langchain4j-open-ai-official` מספק את מחלקת `OpenAiOfficialChatModel` שמתחברת ל-APIs תואמים OpenAI. דגמי GitHub משתמשים באותו פורמט API, כך שאין צורך במתאם מיוחד - פשוט כוונן את כתובת הבסיס ל-`https://models.github.ai/inference`.

## דרישות מוקדמות

**משתמש במכולת Dev?** Java ו-Maven כבר מותקנים. כל מה שאתה צריך הוא טוקן גישה אישי ל-GitHub.

**פיתוח מקומי:**
- Java 21 ומעלה, Maven 3.9 ומעלה
- טוקן גישה אישי ל-GitHub (הוראות למטה)

> **הערה:** מודול זה משתמש ב-`gpt-4.1-nano` מדגמי GitHub. אל תשנה את שם המודל בקוד - הוא מוגדר לעבודה עם הדגמים הזמינים של GitHub.

## הגדרה

### 1. קבל את טוקן ה-GitHub שלך

1. עבור אל [הגדרות GitHub → טוקנים אישיים](https://github.com/settings/personal-access-tokens)
2. לחץ על "Generate new token"
3. קבע שם תיאורי (למשל, "LangChain4j Demo")
4. הגדר תוקף (מומלץ 7 ימים)
5. תחת "הרשאות חשבון", מצא "Models" והגדר ל-"קריאה בלבד"
6. לחץ על "Generate token"
7. העתק ושמור את הטוקן שלך - לא תראה אותו שוב

### 2. הגדר את הטוקן שלך

**אפשרות 1: שימוש ב-VS Code (מומלץ)**

אם אתה משתמש ב-VS Code, הוסף את הטוקן לקובץ `.env` בשורש הפרויקט:

אם קובץ `.env` לא קיים, העתק `.env.example` ל-`.env` או צור קובץ `.env` חדש בשורש הפרויקט.

**קובץ `.env` לדוגמה:**
```bash
# בקובץ /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

לאחר מכן תוכל פשוט ללחוץ קליק ימני על כל קובץ דמו (למשל, `BasicChatDemo.java`) בחוקר הקבצים ולבחור **"Run Java"**, או להשתמש בקונפיגורציות ההפעלה מפאנל הריצה והניפוי.

**אפשרות 2: שימוש במסוף**

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

**שימוש ב-VS Code:** פשוט לחץ קליק ימני על כל קובץ דמו בחוקר הקבצים ובחר **"Run Java"**, או השתמש בקונפיגורציות ההפעלה מפאנל הריצה והניפוי (ודא שהוספת את הטוקן ל-`.env` קודם).

**שימוש ב-Maven:** לחלופין, תוכל להריץ משורת הפקודה:

### 1. שיחה בסיסית

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. דפוסי הפעלה

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

מציג הפעלה ללא דוגמאות, עם מספר דוגמאות, שרשרת מחשבה, והפעלת תפקיד.

### 3. קריאת פונקציה

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

ה-AI קורא אוטומטית למתודות Java שלך כשצריך.

### 4. שאלות ותשובות על מסמכים (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

שאל שאלות בנוגע לתוכן ב-`document.txt`.

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

**שיחה בסיסית** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

התחל כאן כדי לראות את LangChain4j בפשטותו. תיצור `OpenAiOfficialChatModel`, תשלח פרומפט עם `.chat()`, ותקבל תגובה. זה מדגים את הבסיס: איך לאתחל מודלים עם נקודות קצה ומפתחות API מותאמים. לאחר שתבין דפוס זה, כל השאר בנוי עליו.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 נסה עם [GitHub Copilot](https://github.com/features/copilot) Chat:** פתח את [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ושאל:
> - "איך אעבור מדגמי GitHub ל-Azure OpenAI בקוד הזה?"
> - "אילו פרמטרים נוספים אני יכול לקנפג ב-OpenAiOfficialChatModel.builder()?"
> - "איך להוסיף תגובות סטרימינג במקום להמתין לתגובה מלאה?"

**הנדסת פרומפטים** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

עכשיו כשאתה יודע איך לדבר עם מודל, בוא נחקור מה אתה אומר לו. הדמו הזה משתמש באותה הגדרת מודל אך מציג חמישה דפוסי הפעלה שונים. נסה הפעלות ללא דוגמאות להוראות ישירות, הפעלות עם מספר דוגמאות שלומדות מדוגמאות, הפעלות בשרשרת מחשבה שמגלות שלבי נימוק, והפעלות מבוססות תפקיד שמגדירות הקשר. תראה איך אותו מודל נותן תוצאות שונות בצורה דרמטית בהתאם לאופן בו אתה ממסגר את הבקשה.

הדמו גם מדגים תבניות פרומפט, שהן דרך חזקה ליצור פרומפטים שימושיים עם משתנים.
הדוגמה למטה מראה פרומפט המשתמש ב-PromptTemplate של LangChain4j למילוי משתנים. ה-AI ישיב בהתבסס על היעד והפעילות שסופקו.

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
> - "מה ההבדל בין הפעלה ללא דוגמאות לבין הפעלה עם מספר דוגמאות, ומתי כדאי להשתמש בכל אחת?"
> - "איך פרמטר הטמפרטורה משפיע על תגובות המודל?"
> - "מהן כמה טכניקות למניעת התקפות הזרקת פרומפט בפרודקשן?"
> - "איך ליצור אובייקטים של PromptTemplate רב שימושיים לדפוסים נפוצים?"

**אינטגרציה של כלים** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

כאן LangChain4j הופך לעוצמתי. תשתמש ב-`AiServices` כדי ליצור עוזר AI שיכול לקרוא למתודות Java שלך. פשוט סמן מתודות ב-`@Tool("description")` ו-LangChain4j מטפל בשאר - ה-AI מחליט אוטומטית מתי להשתמש בכל כלי בהתאם למה שהמשתמש מבקש. זה מדגים קריאת פונקציה, טכניקה מרכזית לבניית AI שיכול לבצע פעולות, לא רק לענות על שאלות.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 נסה עם [GitHub Copilot](https://github.com/features/copilot) Chat:** פתח את [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ושאל:
> - "איך עובד הסימון @Tool ומה LangChain4j עושה איתו מאחורי הקלעים?"
> - "האם ה-AI יכול לקרוא לכמה כלים ברצף כדי לפתור בעיות מורכבות?"
> - "מה קורה אם כלי זורק שגיאה - איך כדאי לטפל בשגיאות?"
> - "איך הייתי משתלב API אמיתי במקום דוגמת המכונה המחשבת פה?"

**שאלות ותשובות על מסמכים (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

כאן תראה את הבסיס של RAG (יצירה משולבת של מידע חזותי). במקום לסמוך על נתוני האימון של המודל, אתה טוען תוכן מ-[`document.txt`](../../../00-quick-start/document.txt) כולל אותו בפרומפט. ה-AI משיב על בסיס המסמך שלך, לא על פי הידע הכללי שלו. זו הצעד הראשון לבניית מערכות שיכולות לעבוד עם הנתונים שלך.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **הערה:** גישה פשוטה זו טוענת את כל המסמך לפרומפט. עבור קבצים גדולים (>10KB), תחרוג ממגבלות ההקשר. מודול 03 מכסה חלוקה (chunking) וחיפוש וקטורי למערכות RAG בפרודקשן.

> **🤖 נסה עם [GitHub Copilot](https://github.com/features/copilot) Chat:** פתח את [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ושאל:
> - "איך RAG מונעת הלוצינציות AI לעומת שימוש בנתוני אימון המודל?"
> - "מה ההבדל בין גישה פשוטה זו לבין שימוש בהטמעות וקטוריות לחיפוש?"
> - "איך אני יכול להרחיב זאת לטיפול במסמכים מרובים או בבסיסי ידע גדולים יותר?"
> - "מה הונהלים הטובים ביותר למבנה הפרומפט כדי להבטיח שה-AI ישתמש רק בקונטקסט שניתן?"

**בינה מלאכותית אחראית** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

בנה בטיחות AI עם הגנה בשכבות. הדמו הזה מראה שתי שכבות הגנה שעובדות יחד:

**חלק 1: משטרות קלט של LangChain4j** - חוסם פרומפטים מסוכנים לפני שהם מגיעים ל-LLM. צור משטרות מותאמות שבודקות מילות מפתח או דפוסים אסורים. אלו רצות בקוד שלך, כך שהן מהירות וחינמיות.

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

**חלק 2: מסנני בטיחות של ספק** - לדגמי GitHub יש מסננים מובנים שסוחטים את מה שהמשטרות שלך עלולות לפספס. תראה חסימות חזקות (שגיאות HTTP 400) עבור הפרות חמורות וסירובים רכים שבהם ה-AI מתנגד בנימוס.

> **🤖 נסה עם [GitHub Copilot](https://github.com/features/copilot) Chat:** פתח את [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ושאל:
> - "מהו InputGuardrail ואיך אני יוצר משלי?"
> - "מה ההבדל בין חסימה חזקה לסירוב רך?"
> - "מדוע להשתמש גם במשטרות וגם במסנני ספק יחד?"

## השלבים הבאים

**מודול הבא:** [01-introduction - התחלה עם LangChain4j ו-gpt-5 ב-Azure](../01-introduction/README.md)

---

**ניווט:** [← חזרה לעיקרי](../README.md) | [הבא: מודול 01 - מבוא →](../01-introduction/README.md)

---

## פתרון תקלות

### בניית Maven בפעם הראשונה

**בעיה**: `mvn clean compile` או `mvn package` ראשוניים לוקחים זמן רב (10-15 דקות)

**סיבה**: Maven צריך להוריד את כל התלויות של הפרויקט (Spring Boot, ספריות LangChain4j, SDKs של Azure וכו') בבנייה הראשונה.

**פתרון**: זה התנהגות רגילה. הבניות הבאות יהיו הרבה יותר מהירות כי התלויות שמורות במטמון מקומי. זמן ההורדה תלוי במהירות הרשת שלך.
### תחביר פקודת Maven ב-PowerShell

**בעיה**: פקודות Maven נכשלות עם הודעת שגיאה `Unknown lifecycle phase ".mainClass=..."`

**סיבה**: PowerShell מפרש את `=` כאופרטור שיוך משתנה, מה ששובר את תחביר המאפיין של Maven

**פתרון**: השתמש באופרטור עצירת הפירסום `--%` לפני פקודת Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

האופרטור `--%` אומר ל-PowerShell להעביר את כל הפרמטרים הנותרים בצורה מילולית ל-Maven ללא פירוש.

### הצגת אימוג'ים ב-Windows PowerShell

**בעיה**: תגובות AI מציגות תווים חסרי משמעות (למשל, `????` או `â??`) במקום אימוג'ים ב-PowerShell

**סיבה**: הקידוד הדיפולטי של PowerShell אינו תומך באימוג'ים ב-UTF-8

**פתרון**: הפעל פקודה זו לפני הפעלת יישומי Java:
```cmd
chcp 65001
```

זה מכריח קידוד UTF-8 במסוף. לחלופין, השתמש ב-Windows Terminal שיש לו תמיכה טובה יותר ביוניקוד.

### איתור תקלות בקריאות API

**בעיה**: שגיאות אימות, הגבלות קצב, או תגובות בלתי צפויות מהמודל AI

**פתרון**: הדוגמאות כוללות `.logRequests(true)` ו-`.logResponses(true)` להצגת קריאות API במסוף. זה עוזר לאתר שגיאות אימות, הגבלות קצב, או תגובות בלתי צפויות. הסר דגלים אלה בייצור כדי להפחית רעש בלוג.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב ויתור:**  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדייק, יש להיות מודעים לכך שתרגומים אוטומטיים עשויים להכיל שגיאות או אי דיוקים. המסמך המקורי בשפתו היעודית הוא המקור הסמכותי. לצורך מידע קריטי מומלץ להיעזר בתרגום מקצועי של בני אדם. איננו אחראים לכל אי הבנה או פרשנות מוטעת הנובעים משימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->