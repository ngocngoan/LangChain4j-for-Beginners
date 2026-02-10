# מודול 02: הנדסת פרומפטים עם GPT-5.2

## תוכן העניינים

- [מה תלמדו](../../../02-prompt-engineering)
- [דרישות מוקדמות](../../../02-prompt-engineering)
- [הבנת הנדסת פרומפטים](../../../02-prompt-engineering)
- [כיצד זה משתמש ב-LangChain4j](../../../02-prompt-engineering)
- [התבניות המרכזיות](../../../02-prompt-engineering)
- [שימוש במשאבי Azure קיימים](../../../02-prompt-engineering)
- [צילום מסך של היישום](../../../02-prompt-engineering)
- [חקירת התבניות](../../../02-prompt-engineering)
  - [רמת מוטיבציה נמוכה מול גבוהה](../../../02-prompt-engineering)
  - [ביצוע משימה (הקדמות לכלים)](../../../02-prompt-engineering)
  - [קוד המשקף את עצמו](../../../02-prompt-engineering)
  - [ניתוח מובנה](../../../02-prompt-engineering)
  - [שיח רב-סיבובי](../../../02-prompt-engineering)
  - [היגיון שלב אחר שלב](../../../02-prompt-engineering)
  - [פלט מוגבל](../../../02-prompt-engineering)
- [מה אתם באמת לומדים](../../../02-prompt-engineering)
- [השלבים הבאים](../../../02-prompt-engineering)

## מה תלמדו

במודול הקודם ראיתם כיצד זיכרון מאפשר בינה מלאכותית שיחתית והשתמשתם במודלים של GitHub לאינטראקציות בסיסיות. כעת נתמקד באיך אתם שואלים שאלות - בפרומפטים עצמם - באמצעות GPT-5.2 של Azure OpenAI. האופן שבו אתם מבנים את הפרומפטים משפיע בצורה דרמטית על איכות התשובות שתקבלו.

נשתמש ב-GPT-5.2 מפני שהוא מציג בקרת היגיון - אתם יכולים להורות למודל כמה לחשוב לפני שעני. זה הופך אסטרטגיות פרומפט שונות ליותר ברורות ועוזר לכם להבין מתי להשתמש בכל גישה. בנוסף, ניהנה ממגבלות פחות מחמירות של Azure עבור GPT-5.2 בהשוואה למודלים של GitHub.

## דרישות מוקדמות

- סיום מודול 01 (משאבי Azure OpenAI פרוסים)
- קובץ `.env` בספרייה הראשית עם אישורי Azure (נוצר ע"י `azd up` במודול 01)

> **הערה:** אם לא השלמתם את מודול 01, עקבו תחילה אחרי הוראות הפריסה שם.

## הבנת הנדסת פרומפטים

הנדסת פרומפטים עוסקת בתכנון טקסט קלט שמקבל באופן עקבי את התוצאות שאתם צריכים. זה לא רק שאלה של לשאול שאלות - זה עניין של מבנה בקשות כך שהמודל יבין בדיוק מה אתם רוצים ואיך לספק זאת.

תחשבו על זה כמו לתת הוראות לעמית לעבודה. "תקן את הבאג" הוא עמום. "תקן את חריגת ה-null pointer ב-UserService.java שורה 45 על ידי הוספת בדיקת null" הוא מדויק. מודלים לשוניים פועלים באותו אופן - דיוק ומבנה חשובים.

## כיצד זה משתמש ב-LangChain4j

מודול זה מדגים דפוסי פרומפט מתקדמים באמצעות אותו בסיס LangChain4j מהמודולים הקודמים, עם דגש על מבנה הפרומפטים ובקרת היגיון.

<img src="../../../translated_images/he/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*כיצד LangChain4j מחבר את הפרומפטים שלכם ל-Azure OpenAI GPT-5.2*

**תלויות** - מודול 02 משתמש בתלויות langchain4j הבאות המוגדרות ב־`pom.xml`:  
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```
  
**הגדרת OpenAiOfficialChatModel** - [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

מודל הצ׳אט מוגדר ידנית כבין Spring באמצעות הלקוח הרשמי של OpenAI, התומך בנקודות הקצה של Azure OpenAI. ההבדל המרכזי מ־מודול 01 הוא כיצד אנו מבנים את הפרומפטים הנשלחים ל־`chatModel.chat()`, לא בהגדרת המודל עצמו.

**הודעות מערכת ומשתמש** - [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j מפריד את סוגי ההודעות להבהרה. `SystemMessage` מגדיר את התנהגות והקשר ה-AI (כמו "אתה מבקר קוד"), בעוד ש־`UserMessage` מכיל את הבקשה בפועל. הפרדה זו מאפשרת לשמור על התנהגות AI עקבית בכל שאילתות המשתמש השונות.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```
  
<img src="../../../translated_images/he/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage מספק הקשר מתמשך בעוד UserMessages מכילות בקשות פרטניות*

**MessageWindowChatMemory לשיח רב-סיבובי** - עבור דפוס שיח רב סיבובי, אנו משתמשים ב־`MessageWindowChatMemory` ממודול 01. כל מושב מקבל מופע זיכרון משלו המאוחסן ב־`Map<String, ChatMemory>`, מה שמאפשר שיחות מקבילות מרובות ללא ערבוב ההקשר.

**תבניות פרומפט** - המיקוד האמיתי כאן הוא הנדסת פרומפטים, לא API חדשים של LangChain4j. כל דפוס (רמת מוטיבציה נמוכה, גבוהה, ביצוע משימות וכו׳) משתמש באותה שיטת `chatModel.chat(prompt)` אך עם מחרוזות פרומפט מעוצבות בקפידה. תגי XML, הוראות ועיצוב הם חלק מהטקסט של הפרומפט, לא תכונות של LangChain4j.

**בקרת היגיון** - מאמץ ההיגיון של GPT-5.2 נשלט באמצעות הוראות בפרומפט כמו "מקסימום 2 שלבי היגיון" או "חקור לעומק". אלו טכניקות הנדסת פרומפטים, לא קונפיגורציות של LangChain4j. הספרייה פשוט מעבירה את הפרומפטים שלכם למודל.

הלקח המרכזי: LangChain4j מספקת את התשתית (התחברות למודל דרך [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), זיכרון, טיפול בהודעות דרך [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), בעוד שהמודול הזה מלמד אתכם כיצד ליצור פרומפטים יעילים בתוך תשתית זו.

## התבניות המרכזיות

לא כל הבעיות דורשות את אותה גישה. חלק מהשאלות זקוקות לתשובות מהירות, אחרות דורשות חשיבה עמוקה. חלק זקוק להיגיון גלוי, חלק רק לתוצאות. מודול זה מכסה שמונה דפוסי פרומפט - כל אחד מותאם לתרחישים שונים. תתנסו בכולם כדי ללמוד מתי כל גישה עובדת הכי טוב.

<img src="../../../translated_images/he/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*סקירה של שמונת דפוסי הנדסת הפרומפט ושימושי כל אחד מהם*

<img src="../../../translated_images/he/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*רמת מוטיבציה נמוכה (מהיר וישיר) מול גבוהה (יסודי, חוקר) בגישות היגיון*

**רמת מוטיבציה נמוכה (מהירה וממוקדת)** - לשאלות פשוטות בהן אתם רוצים תשובות מהירות וישירות. המודל עורך מעט היגיון - מקסימום שני שלבים. השתמשו בזה לחישובים, חיפושים או שאלות פשוטות.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```
  
> 💡 **חקור עם GitHub Copilot:** פתח את [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ושאל:  
> - "מה ההבדל בין דפוסי פרומפט עם רמת מוטיבציה נמוכה לעומת גבוהה?"  
> - "כיצד תגי ה-XML בפרומפטים מסייעים במבנה תגובת ה-AI?"  
> - "מתי כדאי להשתמש בדפוסי השתקפות עצמית מול הוראות ישירות?"

**רמת מוטיבציה גבוהה (עמוקה ויסודית)** - עבור בעיות מורכבות בהן רוצים ניתוח מקיף. המודל חוקר לעומק ומציג היגיון מפורט. השתמשו בזה לעיצוב מערכות, החלטות ארכיטקטוניות או מחקר מורכב.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**ביצוע משימה (התקדמות שלב אחר שלב)** - עבור תהליכים רב-שלביים. המודל מספק תוכנית מקדימה, מספר כל שלב תוך עבודה, ואז מסכם. השתמשו בזה למיגרציות, יישומים או כל תהליך רב-שלבי.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```
  
פרומפט סחורת-חשיבה (Chain-of-Thought) מבקש במפורש מהמודל להראות את תהליך ההיגיון שלו, ומשפר דיוק במשימות מורכבות. הפירוק שלב אחר שלב מסייע גם לבני אדם וגם ל-AI להבין את הלוגיקה.

> **🤖 נסה עם שיחת [GitHub Copilot](https://github.com/features/copilot):** שאל על דפוס זה:  
> - "כיצד אני מתאים את דפוס ביצוע המשימה עבור פעולות ארוכות?"  
> - "מהן השיטות הטובות ביותר למבנה הקדמות כלי ביישומים בפרודקשן?"  
> - "איך אוכל ללכוד ולהציג עדכוני התקדמות ביניים בממשק משתמש?"

<img src="../../../translated_images/he/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*תוכנית → ביצוע → סיכום לתהליכים רב-שלביים*

**קוד המשקף את עצמו** - להפקת קוד באיכות פרודקשן. המודל מייצר קוד, בודק אותו מול קריטריוני איכות ומשפר אותו באופן איטרטיבי. השתמשו בזה כשבונים תכונות או שירותים חדשים.

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
  
<img src="../../../translated_images/he/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*לולאת שיפור איטרטיבית - יצירה, הערכה, זיהוי בעיות, שיפור, חזרה*

**ניתוח מובנה** - להערכה עקבית. המודל סוקר קוד באמצעות מסגרת קבועה (נכונות, שיטות עבודה, ביצועים, אבטחה). השתמשו בזה לסקירות קוד או הערכות איכות.

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
> - "כיצד אני מתאים את מסגרת הניתוח לסוגים שונים של סקירות קוד?"  
> - "מהי הדרך הטובה ביותר לנתח ולהפעיל פלט מובנה תכנית?"  
> - "כיצד מבטיחים עקביות ברמות חומרה בין מושבי סקירה שונים?"

<img src="../../../translated_images/he/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*מסגרת ארבע-קטגוריות לסקירות קוד עקביות עם רמות חומרה*

**שיח רב-סיבובי** - לשיחות שדורשות הקשר. המודל זוכר הודעות קודמות ובונה עליהן. השתמשו בזה למפגשי עזרה אינטראקטיביים או שאלות ותשובות מורכבות.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
<img src="../../../translated_images/he/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*איך מצטבר הקשר השיח על פני סיבובים מרובים עד להגיע למגבלת הטוקנים*

**היגיון שלב אחר שלב** - לבעיות שדורשות לוגיקה גלויה. המודל מציג היגיון מפורש לכל שלב. השתמשו בזה לבעיות מתמטיות, חידות לוגיות או כשדורשים להבין את תהליך המחשבה.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/he/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*פירוק בעיות לצעדים לוגיים מפורשים*

**פלט מוגבל** - לתשובות עם דרישות פורמט ספציפיות. המודל מקפיד על כלל פורמט ואורך. השתמשו בזה לסיכומים או כשצריך מבנה פלט מדויק.

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
  
<img src="../../../translated_images/he/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*אכיפת דרישות פורמט, אורך ומבנה ספציפי*

## שימוש במשאבי Azure קיימים

**אימות הפריסה:**

ודאו שקובץ `.env` קיים בספרייה הראשית עם אישורי Azure (נוצר במהלך מודול 01):  
```bash
cat ../.env  # צריך להציג AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**הפעלת היישום:**

> **הערה:** אם כבר הפעלתם את כל היישומים באמצעות `./start-all.sh` במודול 01, מודול זה כבר רץ על הפורט 8083. ניתן לדלג על פקודות ההפעלה למטה ולעבור ישירות ל־http://localhost:8083.

**אפשרות 1: שימוש ב-Spring Boot Dashboard (מומלץ למשתמשי VS Code)**

מיכל הפיתוח כולל את תוסף Spring Boot Dashboard, המספק ממשק ויזואלי לניהול כל יישומי Spring Boot. ניתן למצוא אותו בסרגל הפעילויות בצד שמאל של VS Code (חפשו את סמל Spring Boot).

משם תוכלו:  
- לראות את כל יישומי Spring Boot הזמינים בסביבת העבודה  
- להפעיל/להפסיק יישומים בלחיצה אחת  
- לצפות ביומני היישום בזמן אמת  
- לנטר את מצב היישום

פשוט לחצו על כפתור ההפעלה ליד "prompt-engineering" כדי להפעיל מודול זה, או להפעיל את כל המודולים בבת אחת.

<img src="../../../translated_images/he/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**אפשרות 2: שימוש בסקריפטים בשורת פקודה**

הפעלת כל יישומי האינטרנט (מודולים 01-04):

**Bash:**  
```bash
cd ..  # מתיקיית השורש
./start-all.sh
```
  
**PowerShell:**  
```powershell
cd ..  # מתיקיית השורש
.\start-all.ps1
```
  
או להפעיל רק את מודול זה:

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
  
שני הסקריפטים טוענים אוטומטית משתני סביבה מקובץ `.env` שבספרייה הראשית ויבנו את קבצי JAR אם אינם קיימים.

> **הערה:** אם תרצו לבנות את כל המודולים ידנית לפני ההפעלה:  
>  
> **Bash:**  
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
  
> **PowerShell:**  
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
  
פתחו את http://localhost:8083 בדפדפן שלכם.

**לעצור:**

**Bash:**  
```bash
./stop.sh  # רק מודול זה
# או
cd .. && ./stop-all.sh  # כל המודולים
```
  
**PowerShell:**  
```powershell
.\stop.ps1  # מודול זה בלבד
# או
cd ..; .\stop-all.ps1  # כל המודולים
```
  
## צילום מסך של היישום

<img src="../../../translated_images/he/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*הלוח הראשי המציג את שמונת דפוסי הנדסת הפרומפטים עם מאפייניהם ושימושיהם*

## חקירת התבניות

ממשק האינטרנט מאפשר לכם להתנסות באסטרטגיות פרומפט שונות. כל דפוס פותר בעיות שונות - נסו אותם כדי לראות מתי כל גישה תאירה.

### רמת מוטיבציה נמוכה מול גבוהה

שאול שאלה פשוטה כמו "מה זה 15% מתוך 200?" עם רמת מוטיבציה נמוכה. תקבלו תשובה מיידית וישירה. עכשיו שאלו משהו מורכב כמו "עצב אסטרטגיית מטמון ל-API בכבד תנועה" עם רמת מוטיבציה גבוהה. צפו כיצד המודל מתעכב ומספק היגיון מפורט. אותו מודל, אותו מבנה שאלה - אבל הפרומפט קובע כמה לחשוב.
<img src="../../../translated_images/he/low-eagerness-demo.898894591fb23aa0.webp" alt="הדגמת רמת התלהבות נמוכה" width="800"/>

*חישוב מהיר עם הסברים מינימליים*

<img src="../../../translated_images/he/high-eagerness-demo.4ac93e7786c5a376.webp" alt="הדגמת רמת התלהבות גבוהה" width="800"/>

*אסטרטגיית מטמון מקיפה (2.8MB)*

### ביצוע משימה (פתיחי כלים)

זרימות עבודה מרובות שלבים נהנות מתכנון מראש ונארטיב של ההתקדמות. המודל משרטט מה יעשה, מתאר כל שלב ואז מסכם תוצאות.

<img src="../../../translated_images/he/tool-preambles-demo.3ca4881e417f2e28.webp" alt="הדגמת ביצוע משימה" width="800"/>

*יצירת נקודת קצה REST עם תיאור שלב אחר שלב (3.9MB)*

### קוד המשתקף בעצמו

נסה "צור שירות אימות אימייל". במקום רק לייצר קוד ולעצור, המודל יוצר, מעריך מול קריטריוני איכות, מזהה חולשות ומשפר. תראה אותו חוזר על עצמו עד שהקוד עובר את תקני הייצור.

<img src="../../../translated_images/he/self-reflecting-code-demo.851ee05c988e743f.webp" alt="הדגמת קוד המשתקף בעצמו" width="800"/>

*שירות אימות אימייל מלא (5.2MB)*

### ניתוח מובנה

ביקורות קוד דורשות מסגרות הערכה עקביות. המודל מנתח קוד באמצעות קטגוריות קבועות (נכונות, פרקטיקות, ביצועים, אבטחה) עם רמות חומרה.

<img src="../../../translated_images/he/structured-analysis-demo.9ef892194cd23bc8.webp" alt="הדגמת ניתוח מובנה" width="800"/>

*ביקורת קוד מבוססת מסגרת*

### שיחה רב-סבבית

שאל "מה זה Spring Boot?" ואז מיד המשך ב-"הראה לי דוגמה". המודל זוכר את שאלתך הראשונה ונותן לך דוגמה מ-Spring Boot ספציפית. ללא זיכרון, השאלה השנייה הייתה כללית מדי.

<img src="../../../translated_images/he/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="הדגמת שיחה רב-סבבית" width="800"/>

*שימור הקשר בין שאלות*

### נימוק שלב אחר שלב

בחר בעיה מתמטית ונסה עם נימוק שלב אחר שלב ורמת התלהבות נמוכה. רמת התלהבות נמוכה נותנת רק את התשובה - מהר אך לא ברור. נימוק שלב אחר שלב מראה כל חישוב והחלטה.

<img src="../../../translated_images/he/step-by-step-reasoning-demo.12139513356faecd.webp" alt="הדגמת נימוק שלב אחר שלב" width="800"/>

*בעיה מתמטית עם שלבים מפורשים*

### פלט מוגבל

כשצריך פורמטים ספציפיים או ספירת מילים, התבנית הזו מוודאת דביקות מחמירה. נסה ליצור סיכום עם בדיוק 100 מילים בפורמט נקודות.

<img src="../../../translated_images/he/constrained-output-demo.567cc45b75da1633.webp" alt="הדגמת פלט מוגבל" width="800"/>

*סיכום למידת מכונה עם שליטה על פורמט*

## מה שאתם באמת לומדים

**מאמץ נימוק משנה הכל**

GPT-5.2 מאפשר לך לשלוט במאמץ חישובי דרך ההנחות שלך. מאמץ נמוך פירושו תגובות מהירות עם חקר מינימלי. מאמץ גבוה אומר שהמודל משקיע זמן במחשבה עמוקה. אתם לומדים להתאים מאמץ למורכבות המשימה - אל תבזבזו זמן על שאלות פשוטות, אך אל תמהרו להחליט בהחלטות מורכבות.

**מבנה מדריך התנהגות**

שמעתם על תגי XML בהנחות? הם לא לקישוט. מודלים פועלים באמינות גבוהה יותר לפי הוראות מבניות מאשר טקסט חופשי. כשצריך תהליכים מרובי שלבים או לוגיקה מורכבת, המבנה מסייע למודל לעקוב מהיכן הוא ומה הבא.

<img src="../../../translated_images/he/prompt-structure.a77763d63f4e2f89.webp" alt="מבנה הנחיה" width="800"/>

*אנטומיה של הנחיה מובנית היטב עם סעיפים ברורים וארגון בסגנון XML*

**איכות דרך הערכה עצמית**

הדפוסים המשתקפים בעצמם עובדים על ידי הפיכת קריטריוני האיכות למפורשים. במקום לקוות שהמודל "יעשה נכון", אתה אומר לו בדיוק מה זה "נכון": לוגיקה נכונה, טיפול בשגיאות, ביצועים ואבטחה. המודל יכול אז להעריך את התוצרים שלו ולשפר אותם. זה הופך את יצירת הקוד מתהליך של מזל לתהליך מובנה.

**הקשר מוגבל**

שיחות רב-סבביות פועלות על ידי שילוב היסטוריית ההודעות בכל בקשה. אבל יש גבול - לכל מודל יש ספירת תווים מקסימלית. ככל שהשיחות מתרחבות, תצטרך אסטרטגיות לשמור על הקשר רלוונטי מבלי לחרוג מהמגבלה. המודול הזה מראה איך הזיכרון עובד; אחר כך תלמד מתי לסכם, מתי לשכוח ומתי לשחזר.

## הצעדים הבאים

**מודול הבא:** [03-rag - RAG (הפקה משולבת שליפה)](../03-rag/README.md)

---

**ניווט:** [← הקודם: מודול 01 - מבוא](../01-introduction/README.md) | [חזרה לעיקרי](../README.md) | [הבא: מודול 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב ויתור**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). אנו שואפים לדיוק, אך יש לקחת בחשבון כי תרגומים אוטומטיים עלולים להכיל שגיאות או אי-דיוקים. יש להסתמך על המסמך המקורי בשפת המקור כמקור הסמכותי. למידע קריטי מומלץ להשתמש בתרגום מקצועי שנעשה על ידי אדם. איננו אחראים לכל הבנה שגויה או פרשנות מוטעית הנובעת משימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->