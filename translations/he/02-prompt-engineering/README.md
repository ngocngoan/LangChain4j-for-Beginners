# מודול 02: הנדסת פרומפטים עם GPT-5.2

## תוכן העניינים

- [מה תלמדו](../../../02-prompt-engineering)
- [דרישות מוקדמות](../../../02-prompt-engineering)
- [הבנת הנדסת פרומפטים](../../../02-prompt-engineering)
- [יסודות הנדסת פרומפטים](../../../02-prompt-engineering)
  - [פרומפטינג ללא דוגמאות](../../../02-prompt-engineering)
  - [פרומפטינג עם דוגמאות](../../../02-prompt-engineering)
  - [שרשרת חשיבה](../../../02-prompt-engineering)
  - [פרומפט מבוסס תפקיד](../../../02-prompt-engineering)
  - [תבניות פרומפטים](../../../02-prompt-engineering)
- [תבניות מתקדמות](../../../02-prompt-engineering)
- [שימוש במשאבי Azure קיימים](../../../02-prompt-engineering)
- [צילום מסך של האפליקציה](../../../02-prompt-engineering)
- [חקירת התבניות](../../../02-prompt-engineering)
  - [רמת התלהבות נמוכה לעומת גבוהה](../../../02-prompt-engineering)
  - [ביצוע משימות (הקדמות לכלים)](../../../02-prompt-engineering)
  - [קוד עם השתקפות עצמית](../../../02-prompt-engineering)
  - [ניתוח מובנה](../../../02-prompt-engineering)
  - [שיחת רב סבבים](../../../02-prompt-engineering)
  - [היסבר צעד-אחר-צעד](../../../02-prompt-engineering)
  - [פלט מוגבל](../../../02-prompt-engineering)
- [מה אתם באמת לומדים](../../../02-prompt-engineering)
- [הצעדים הבאים](../../../02-prompt-engineering)

## מה תלמדו

<img src="../../../translated_images/he/what-youll-learn.c68269ac048503b2.webp" alt="מה תלמדו" width="800"/>

במודול הקודם ראית כיצד זיכרון מאפשר בינה מלאכותית שוטפת והשתמשת במודלים של GitHub לאינטראקציות בסיסיות. כעת נתמקד באופן שבו אתה שואל שאלות — הפרומפטים עצמם — באמצעות GPT-5.2 של Azure OpenAI. האופן שבו אתה מבנה את הפרומפטים משפיע מאוד על איכות התגובות שתקבל. נתחיל בסקירה של טכניקות הפרומפטינג הבסיסיות, ואז נעבור לשמונה תבניות מתקדמות שממנפות במלואן את יכולות GPT-5.2.

נשתמש ב-GPT-5.2 כי הוא מציג שליטה על ההיגיון - אתה יכול לקבוע למודל כמה לחשוב לפני התשובה. זה מציג בצורה ברורה יותר את אסטרטגיות הפרומפטינג ומסייע לך להבין מתי להשתמש בכל גישה. בנוסף, ניהנה מהמגבלות הרשת הפחות מחמירות של Azure ל-GPT-5.2 לעומת מודלי GitHub.

## דרישות מוקדמות

- השלמת מודול 01 (פריסת משאבי Azure OpenAI)
- קובץ `.env` בתיקיית השורש עם אישורי Azure (נוצר על ידי `azd up` במודול 01)

> **הערה:** אם לא השלמת את מודול 01, יש לעקוב תחילה אחרי הוראות הפריסה שם.

## הבנת הנדסת פרומפטים

<img src="../../../translated_images/he/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="מהי הנדסת פרומפטים?" width="800"/>

הנדסת פרומפטים עוסקת בעיצוב טקסט קלט שמבטיח לקבל תוצאות שאתה צריך. זה לא רק לשאול שאלות — זה על בניית בקשות כך שהמודל יבין בדיוק מה אתה רוצה ואיך לספק זאת.

תחשוב על זה כמו להנחות עמית לעבודה. "תקן את הבאג" זה לא ברור. "תקן את החריגה null pointer ב-UserService.java שורה 45 על ידי הוספת בדיקת null" זה ברור ומפורט. מודלים לשוניים פועלים באותה צורה — ספציפיות ומבנה חשובים.

<img src="../../../translated_images/he/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="כיצד LangChain4j משתלב" width="800"/>

LangChain4j מספק את התשתית — חיבורי מודלים, זיכרון וסוגי הודעות — בעוד שתבניות הפרומפט הן פשוט טקסט מבנה שנשלח דרך התשתית הזאת. אבני היסוד הן `SystemMessage` (הקובעת את ההתנהגות והתפקיד של ה-AI) ו-`UserMessage` (הנושא את הבקשה האמיתית שלך).

## יסודות הנדסת פרומפטים

<img src="../../../translated_images/he/five-patterns-overview.160f35045ffd2a94.webp" alt="סקירת חמש תבניות הנדסת פרומפטים" width="800"/>

לפני שנצלול לתבניות המתקדמות במודול הזה, נעבור על חמש טכניקות פרומפטינג בסיסיות. אלו אבני היסוד שכל מהנדס פרומפט צריך לדעת. אם כבר עבדת עם [מודול ההתחלה המהירה](../00-quick-start/README.md#2-prompt-patterns), ראית את זה בפעולה — הנה המסגרת המושגית שמאחוריהן.

### פרומפטינג ללא דוגמאות

הגישה הפשוטה ביותר: תן למודל הוראה ישירה בלי דוגמאות. המודל מסתמך לגמרי על האימון שלו כדי להבין ולבצע את המשימה. זה עובד היטב למשימות פשוטות שבהן ההתנהגות הצפויה ברורה.

<img src="../../../translated_images/he/zero-shot-prompting.7abc24228be84e6c.webp" alt="פרומפטינג ללא דוגמאות" width="800"/>

*הוראה ישירה ללא דוגמאות — המודל מסיק את המשימה מההוראה לבדה*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// תגובה: "חיובי"
```
  
**מתי להשתמש:** סיווגים פשוטים, שאלות ישירות, תרגומים או כל משימה שהמודל יכול לבצע בלי הנחיות נוספות.

### פרומפטינג עם דוגמאות

ספק דוגמאות שמדגימות את הדפוס שהמודל אמור לעקוב אחריו. המודל לומד את פורמט הקלט-פלט הצפוי מהדוגמאות שלך ומיישם אותו על קלטים חדשים. זה משפר משמעותית את העקביות במשימות שבהן הפורמט או ההתנהגות הרצויה לא ברורים.

<img src="../../../translated_images/he/few-shot-prompting.9d9eace1da88989a.webp" alt="פרומפטינג עם דוגמאות" width="800"/>

*למידה מדוגמאות — המודל מזהה את הדפוס ומיישם אותו על קלטים חדשים*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```
  
**מתי להשתמש:** סיווגים מותאמים, עיצוב עקבי, משימות ספציפיות לתחום, או כשתוצאות פרומפטינג ללא דוגמאות אינן עקביות.

### שרשרת חשיבה

בקש מהמודל להראות את ההיגיון שלו צעד-אחר-צעד. במקום לעבור ישר לתשובה, המודל מפרק את הבעיה ועובר על כל שלב בצורה מפורשת. זה משפר דיוק במתמטיקה, לוגיקה ומשימות חשיבה מרובות שלבים.

<img src="../../../translated_images/he/chain-of-thought.5cff6630e2657e2a.webp" alt="פרומפטינג שרשרת חשיבה" width="800"/>

*היסבר צעד-אחר-צעד — פירוק בעיות מורכבות לשלבים לוגיים מפורשים*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// הדגם מראה: 15 - 8 = 7, ואז 7 + 12 = 19 תפוחים
```
  
**מתי להשתמש:** בעיות מתמטיות, חידות לוגיות, איתור שגיאות, או בכל משימה שבה הצגת תהליך ההיגיון משפרת דיוק ואמון.

### פרומפט מבוסס תפקיד

הגדר דמות או תפקיד ל-AI לפני ששואלים את שאלתך. זה נותן הקשר שמעצב את הטון, העומק, והמיקוד בתגובה. "מהנדס תוכנה" מעניק ייעוץ שונה מ"מפתח זוטר" או "מבדק אבטחה".

<img src="../../../translated_images/he/role-based-prompting.a806e1a73de6e3a4.webp" alt="פרומפט מבוסס תפקיד" width="800"/>

*הגדרת הקשר ודמות — אותה שאלה מקבלת תגובה שונה בהתאם לתפקיד שהוקצה*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```
  
**מתי להשתמש:** סקירות קוד, הדרכה, ניתוח תחומי, או כשנדרשים מענה מותאם לרמת מומחיות או נקודת מבט מסוימת.

### תבניות פרומפטים

צור פרומפטים לשימוש חוזר עם משתנים במקום להשיב כל פעם פרומפט חדש. במקום לכתוב פרומפט חדש בכל פעם, הגדר תבנית פעם אחת ומלא ערכים שונים. מחלקת `PromptTemplate` של LangChain4j מקלה על כך עם תחביר `{{variable}}`.

<img src="../../../translated_images/he/prompt-templates.14bfc37d45f1a933.webp" alt="תבניות פרומפטים" width="800"/>

*פרומפטים לשימוש חוזר עם משתני תבנית — תבנית אחת, שימושים רבים*

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
  
**מתי להשתמש:** שאילתות חוזרות עם קלטים שונים, עיבוד אצווה, בניית תהליכי עבודה ל-AI לשימוש חוזר, או כל תרחיש שבו מבנה הפרומפט נשאר זהה אך הנתונים משתנים.

---

חמשת היסודות הללו מספקים לך ערכת כלים יציבה לרוב המשימות. שאר המודול בונה עליהם עם **שמונה תבניות מתקדמות** שמנצלות את שליטת ההיגיון, ההערכה העצמית והפלט המבנה של GPT-5.2.

## תבניות מתקדמות

לאחר הכיסוי של היסודות, נעבור לשמונה התבניות המתקדמות שהופכות את המודול הזה לייחודי. לא כל הבעיות דורשות את אותה גישה. חלק מהשאלות זקוקות לתשובות מהירות, אחרות דורשות חשיבה מעמיקה. חלק זקוקות להיגיון גלוי, אחרות רק לתוצאה. כל תבנית מטה מותאמת לתרחיש שונה — ושליטת ההיגיון של GPT-5.2 מדגישה את ההבדלים אפילו יותר.

<img src="../../../translated_images/he/eight-patterns.fa1ebfdf16f71e9a.webp" alt="שמונה תבניות פרומפטינג" width="800"/>

*סקירה של שמונה תבניות הנדסת פרומפטים ושימושיהן*

<img src="../../../translated_images/he/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="שליטה על חשיבה עם GPT-5.2" width="800"/>

*שליטת ההיגיון של GPT-5.2 מאפשרת לך לקבוע כמה לחשוב המודל — מתשובות מהירות ישירות ועד חקירה מעמיקה*

<img src="../../../translated_images/he/reasoning-effort.db4a3ba5b8e392c1.webp" alt="השוואת מאמץ חשיבה" width="800"/>

*רמת התלהבות נמוכה (מהירה וישירה) מול גבוהה (מקיפה ומחקרית)*

**רמת התלהבות נמוכה (מהיר וממוקד)** - לשאלות פשוטות שבהן אתה רוצה תשובות מהירות וישירות. המודל מבצע היגיון מינימלי - מקסימום שני שלבים. השתמש בזה לחישובים, חיפושים או שאלות פשוטות.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```
  
> 💡 **חקור עם GitHub Copilot:** פתח את [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ושאל:  
> - "מה ההבדל בין דפוסי פרומפטינג ברמת התלהבות נמוכה לעומת גבוהה?"  
> - "איך תגיות XML בפרומפטים עוזרות במבנה התגובה של ה-AI?"  
> - "מתי כדאי להשתמש בתבניות השתקפות עצמית לעומת הוראות ישירות?"

**רמת התלהבות גבוהה (עמוקה ומקיפה)** - לבעיות מורכבות שבהן אתה רוצה ניתוח מקיף. המודל חוקר לעומק ומציג היגיון מפורט. השתמש בזה בעיצוב מערכות, החלטות ארכיטקטורה או מחקר מורכב.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**ביצוע משימה (התקדמות צעד-אחר-צעד)** - עבור זרמי עבודה מרובי שלבים. המודל מספק תוכנית מראש, מתאר כל שלב במהלך העבודה, ואז נותן סיכום. השתמש בזה במיגרציות, יישומים או בכל תהליך מרובה שלבים.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```
  
פרומפטינג של שרשרת חשיבה מבקש במפורש שהמודל יציג את תהליך ההיגיון שלו, משפר דיוק למשימות מורכבות. הפירוק צעד-אחר-צעד עוזר לבני אדם ול-AI להבין את ההיגיון.

> **🤖 נסה עם שיחת [GitHub Copilot](https://github.com/features/copilot):** שאל על דפוס זה:  
> - "איך אתאים את דפוס ביצוע המשימות לפעולות ארוכות טווח?"  
> - "מהם פרקטיקות מומלצות למבנה הקדמות לכלים באפליקציות ייצור?"  
> - "איך אני יכול ללכוד ולהציג עדכוני התקדמות ביניים בממשק משתמש?"

<img src="../../../translated_images/he/task-execution-pattern.9da3967750ab5c1e.webp" alt="תבנית ביצוע משימה" width="800"/>

*תכנון → ביצוע → סיכום לזרמי עבודה מרובי שלבים*

**קוד עם השתקפות עצמית** - ליצירת קוד באיכות ייצור. המודל מייצר קוד, בודק לפי קריטריוני איכות ומשפר אותו באופן איטרטיבי. השתמש בזה כשאתה מפתח תכונות או שירותים חדשים.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/he/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="מחזור השתקפות עצמית" width="800"/>

*לולאת שיפור איטרטיבית - יצירה, הערכה, זיהוי בעיות, שיפור, חזרה*

**ניתוח מובנה** - להערכת עקבית. המודל בודק קוד לפי מסגרת קבועה (נכונות, שיטות עבודה, ביצועים, אבטחה). השתמש בזה לסקירות קוד או הערכות איכות.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```
  
> **🤖 נסה עם שיחת [GitHub Copilot](https://github.com/features/copilot):** שאל על ניתוח מובנה:  
> - "איך להתאים אישית את מסגרת הניתוח לסוגים שונים של סקירות קוד?"  
> - "מה האופן הטוב ביותר לנתח ולעבד פלט מובנה בצורה תוכנית?"  
> - "איך להבטיח רמות חומרה עקביות בין מופעי סקירה שונים?"

<img src="../../../translated_images/he/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="תבנית ניתוח מובנה" width="800"/>

*מסגרת ארבע קטגוריות לסקירת קוד עקבית עם רמות חומרה*

**שיחת רב סבבים** - לשיחות שדורשות הקשר. המודל זוכר הודעות קודמות ובונה עליהן. השתמש בזה במפגשי עזרה אינטראקטיביים או בשאלות ותשובות מורכבות.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
<img src="../../../translated_images/he/context-memory.dff30ad9fa78832a.webp" alt="זיכרון הקשר" width="800"/>

*כיצד מצטבר הקשר שיחה במהלך סבבים רבים עד הגבלת הטוקנים*

**היסבר צעד-אחר-צעד** - לבעיות שדורשות לוגיקה גלויה. המודל מציג היגיון מפורש לכל שלב. השתמש בזה לבעיות מתמטיקה, חידות לוגיות או כשאתה רוצה להבין את תהליך החשיבה.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/he/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="תבנית צעד-אחר-צעד" width="800"/>

*פירוק בעיות לשלבים לוגיים מפורשים*

**פלט מוגבל** - לתגובות בעלי דרישות פורמט ספציפיות. המודל מקפיד על כללי הפורמט והאורך. השתמש בזה לסיכומים או כשאתה צריך מבנה פלט מדויק.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/he/constrained-output-pattern.0ce39a682a6795c2.webp" alt="תבנית פלט מוגבל" width="800"/>

*אכיפת דרישות פורמט, אורך ומבנה ספציפיות*

## שימוש במשאבי Azure קיימים

**וודא פריסה:**

ודא שקובץ `.env` קיים בתיקיית השורש עם אישורי Azure (נוצר במהלך מודול 01):  
```bash
cat ../.env  # צריך להציג את AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**הפעל את האפליקציה:**

> **הערה:** אם כבר הפעלת את כל האפליקציות באמצעות `./start-all.sh` במודול 01, מודול זה כבר רץ בפורט 8083. ניתן לדלג על פקודות ההפעלה למטה ולעבור ישירות לכתובת http://localhost:8083.

**אפשרות 1: שימוש ב-Spring Boot Dashboard (מומלץ למשתמשי VS Code)**

מכולת הפיתוח כוללת את תוסף Spring Boot Dashboard, שמספק ממשק חזותי לניהול כל אפליקציות Spring Boot. תוכל למצוא אותו בסרגל הפעולות בצד השמאלי של VS Code (חפש את האיקון של Spring Boot).
בלוח המחוונים של Spring Boot, תוכלו:
- לראות את כל יישומי Spring Boot הזמינים באזור העבודה
- להפעיל/להפסיק יישומים בלחיצה אחת
- לצפות ביומני היישום בזמן אמת
- לנטר את מצב היישום

פשוט לחצו על לחצן ההפעלה ליד "prompt-engineering" כדי להפעיל מודול זה, או להפעיל את כל המודולים בבת אחת.

<img src="../../../translated_images/he/dashboard.da2c2130c904aaf0.webp" alt="לוח מחוונים של Spring Boot" width="400"/>

**אפשרות 2: שימוש בסקריפטים בשורת פקודה**

הפעלת כל יישומי הווב (מודולים 01-04):

**Bash:**
```bash
cd ..  # מספריית השורש
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # מתיקיית השורש
.\start-all.ps1
```

או להפעיל רק את המודול הזה:

**Bash:**
```bash
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

שני הסקריפטים טוענים אוטומטית משתני סביבה מקובץ `.env` השורשי ויבנו את קבצי JAR אם הם אינם קיימים.

> **הערה:** אם אתם מעדיפים לבנות את כל המודולים ידנית לפני ההפעלה:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

פתחו את http://localhost:8083 בדפדפן שלכם.

**עצירה:**

**Bash:**
```bash
./stop.sh  # רק מודול זה
# או
cd .. && ./stop-all.sh  # כל המודולים
```

**PowerShell:**
```powershell
.\stop.ps1  # רק מודול זה
# או
cd ..; .\stop-all.ps1  # כל המודולים
```

## צילומי מסך של היישום

<img src="../../../translated_images/he/dashboard-home.5444dbda4bc1f79d.webp" alt="הדף הראשי של לוח המחוונים" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*לוח המחוונים הראשי המציג את כל 8 תבניות הנדסת הפרומפט עם המאפיינים ומקרי השימוש שלהן*

## חקר התבניות

ממשק הווב מאפשר לכם להתנסות באסטרטגיות פרומפט שונות. כל תבנית פותרת בעיות אחרות - נסו אותן כדי לראות מתי כל גישה מתאימה.

### דחיפות נמוכה מול דחיפות גבוהה

שאלו שאלה פשוטה כמו "מה זה 15% מ-200?" באמצעות דחיפות נמוכה. תקבלו תשובה מיידית וישירה. עכשיו שאלו משהו מורכב כמו "עצבו אסטרטגיית מטמון ל-API עם תנועה גבוהה" באמצעות דחיפות גבוהה. צפו כיצד המודל מאט ומספק הסברים מפורטים. אותו מודל, אותה מבנה שאלה - אבל הפרומפט מורה לו כמה לחשוב.

<img src="../../../translated_images/he/low-eagerness-demo.898894591fb23aa0.webp" alt="הדגמת דחיפות נמוכה" width="800"/>

*חישוב מהיר עם הסבר מינימלי*

<img src="../../../translated_images/he/high-eagerness-demo.4ac93e7786c5a376.webp" alt="הדגמת דחיפות גבוהה" width="800"/>

*אסטרטגיית מטמון מקיפה (2.8MB)*

### ביצוע משימות (פְרֵמבלֵי כלים)

תהליכים רב-שלביים מתועלים מתכנון מראש ורטוריקה של ההתקדמות. המודל מפרט מה הוא עומד לעשות, מספר כל שלב ואז מסכם תוצאות.

<img src="../../../translated_images/he/tool-preambles-demo.3ca4881e417f2e28.webp" alt="הדגמת ביצוע משימה" width="800"/>

*יצירת נקודת קצה REST עם רטוריקה שלב אחר שלב (3.9MB)*

### קוד עם התבוננות עצמית

נסו "צור שירות לאימות דואר אלקטרוני". במקום רק להפיק קוד ולעצור, המודל מייצר, מעריך לפי קריטריוני איכות, מזהה חולשות ומשפר. תראו אותו חוזר על עצמו עד שהקוד עומד בסטנדרטים של ייצור.

<img src="../../../translated_images/he/self-reflecting-code-demo.851ee05c988e743f.webp" alt="הדגמת קוד עם התבוננות עצמית" width="800"/>

*שירות אימות דואר אלקטרוני מלא (5.2MB)*

### ניתוח מובנה

סקירות קוד דורשות מסגרות הערכה עקביות. המודל מנתח קוד באמצעות קטגוריות קבועות (נכונות, שיטות עבודה, ביצועים, אבטחה) עם רמות חומרה.

<img src="../../../translated_images/he/structured-analysis-demo.9ef892194cd23bc8.webp" alt="הדגמת ניתוח מובנה" width="800"/>

*סקירת קוד המבוססת על מסגרת עבודה*

### שיחה במספר סבבים

שאלו "מה זה Spring Boot?" ואז המשיכו מיד עם "הראה לי דוגמה". המודל זוכר את השאלה הראשונה ומספק לכם דוגמה ספציפית ל-Spring Boot. בלי זיכרון, השאלה השנייה הייתה עמומה מדי.

<img src="../../../translated_images/he/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="הדגמת שיחה במספר סבבים" width="800"/>

*שימור הקשר בין שאלות*

### הסבר שלב אחר שלב

בחרו בעיית מתמטיקה ונסו עם הסבר שלב אחר שלב ודחיפות נמוכה. דחיפות נמוכה רק נותנת תשובה - מהירה אבל לא ברורה. הסבר שלב אחר שלב מראה כל חישוב והחלטה.

<img src="../../../translated_images/he/step-by-step-reasoning-demo.12139513356faecd.webp" alt="הדגמת הסבר שלב אחר שלב" width="800"/>

*בעיה מתמטית עם שלבים מפורשים*

### פלט מוגבל

כאשר אתם צריכים פורמטים ספציפיים או ספירת מילים, התבנית הזו אוכפת ציות מחמיר. נסו לייצר סיכום עם בדיוק 100 מילים בפורמט נקודות.

<img src="../../../translated_images/he/constrained-output-demo.567cc45b75da1633.webp" alt="הדגמת פלט מוגבל" width="800"/>

*סיכום למידת מכונה עם שליטה בפורמט*

## מה אתם באמת לומדים

**מאמץ ההסברה משנה הכל**

GPT-5.2 מאפשר לכם לשלוט במאמץ החישובי דרך הפרומפטים שלכם. מאמץ נמוך משמעותו תגובות מהירות עם חקר מינימלי. מאמץ גבוה אומר שהמודל מקדיש זמן לחשיבה מעמיקה. אתם לומדים להתאים את המאמץ למורכבות המשימה - אל תבזבזו זמן על שאלות פשוטות, אבל אל תמהרו בהחלטות מורכבות.

**מבנה מכתיב התנהגות**

שמתם לב לתגי XML בפרומפטים? הם לא לקישוט. מודלים עוקבים אחרי הוראות מובנות יותר באמינות מאשר טקסט חופשי. כשאתם צריכים תהליכים רב-שלביים או לוגיקה מורכבת, המבנה עוזר למודל לעקוב היכן הוא ומה השלב הבא.

<img src="../../../translated_images/he/prompt-structure.a77763d63f4e2f89.webp" alt="מבנה הפרומפט" width="800"/>

*מארג של פרומפט מובנה היטב עם חלקים ברורים וארגון בסגנון XML*

**איכות דרך הערכה עצמית**

התבניות עם התבוננות עצמית עובדות על ידי הצגת קריטריוני איכות במפורש. במקום לקוות שהמודל "יעשה זאת נכון", אתם אומרים לו בדיוק מה אומר "נכון": לוגיקה נכונה, טיפול בשגיאות, ביצועים, אבטחה. המודל יכול אז להעריך את הפלט שלו ולשפר. זה הופך יצירת קוד למהלך מובנה ולא למרוץ מזל.

**הקשר מוגבל**

שיחות רב-סיבוביות מתנהלות על ידי הכללת היסטוריית הודעות בכל בקשה. אבל יש גבול - לכל מודל יש מקסימום מספר טוקנים. ככל שהשיחות גדלות, תצטרכו אסטרטגיות לשמור על הקשר רלוונטי מבלי להגיע לתקרה הזו. מודול זה מראה לכם כיצד הזיכרון עובד; בהמשך תלמדו מתי לסכם, מתי לשכוח ומתי לשלוף.

## צעדים הבאים

**מודול הבא:** [03-rag - RAG (ייצור מוגבר בשחזור)](../03-rag/README.md)

---

**ניווט:** [← קודם: מודול 01 - מבוא](../01-introduction/README.md) | [חזרה לדף הראשי](../README.md) | [הבא: מודול 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**הצהרת אחריות**:
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדיוק, יש להיות מודעים לכך שתרגומים אוטומטיים עלולים להכיל שגיאות או אי-דיוקים. המסמך המקורי בשפת המקור שלו צריך להיחשב למקור הסמכותי. עבור מידע קריטי, מומלץ להשתמש בתרגום מקצועי אנושי. אנו לא נושאים באחריות לכל אי הבנות או פרשנויות שגויות הנובעות מהשימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->