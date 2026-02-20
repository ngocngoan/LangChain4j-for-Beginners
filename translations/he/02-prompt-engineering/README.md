# מודול 02: הנדסת פרומפטים עם GPT-5.2

## תוכן העניינים

- [מה תלמדו](../../../02-prompt-engineering)
- [דרישות מוקדמות](../../../02-prompt-engineering)
- [הבנת הנדסת פרומפטים](../../../02-prompt-engineering)
- [יסודות הנדסת פרומפטים](../../../02-prompt-engineering)
  - [פרומפטינג בפתח אפס (Zero-Shot)](../../../02-prompt-engineering)
  - [פרומפטינג עם דוגמאות מועטות (Few-Shot)](../../../02-prompt-engineering)
  - [שרשרת מחשבות (Chain of Thought)](../../../02-prompt-engineering)
  - [פרומפטינג מבוסס תפקיד (Role-Based)](../../../02-prompt-engineering)
  - [תבניות פרומפט](../../../02-prompt-engineering)
- [תבניות מתקדמות](../../../02-prompt-engineering)
- [שימוש במשאבים קיימים ב-Azure](../../../02-prompt-engineering)
- [צילום מסך של היישום](../../../02-prompt-engineering)
- [העמקת התבניות](../../../02-prompt-engineering)
  - [חריצות נמוכה מול גבוהה (Low vs High Eagerness)](../../../02-prompt-engineering)
  - [ביצוע משימות (פתיחי כלים)](../../../02-prompt-engineering)
  - [קוד עם רפלקסיה עצמית](../../../02-prompt-engineering)
  - [ניתוח מובנה](../../../02-prompt-engineering)
  - [שיחת רב-סבב](../../../02-prompt-engineering)
  - [הסקת מסקנות שלב-אחר-שלב](../../../02-prompt-engineering)
  - [פלט מוגבל ומוגדר](../../../02-prompt-engineering)
- [מה אתם בעצם לומדים](../../../02-prompt-engineering)
- [השלבים הבאים](../../../02-prompt-engineering)

## מה תלמדו

<img src="../../../translated_images/he/what-youll-learn.c68269ac048503b2.webp" alt="מה תלמדו" width="800"/>

במודול הקודם ראיתם כיצד זיכרון מאפשר בינה מלאכותית שיחית והשתמשתם בדגמים של GitHub לאינטראקציות בסיסיות. כעת נתמקד באיך לשאול שאלות — בפרומפטים עצמם — תוך שימוש ב-GPT-5.2 של Azure OpenAI. האופן בו אתם בונים את הפרומפטים משפיע באופן דרמטי על איכות התשובות שתקבלו. נפתח בסקירה של טכניקות הפרומפטינג הבסיסיות, ולאחר מכן נעבור לשמונה תבניות מתקדמות שמנצלות את כל יכולות GPT-5.2.

נשתמש ב-GPT-5.2 כי הוא מציג בקרת חשיבה – אפשר ליידע את המודל כמה עליו לחשוב לפני התשובה. זה מאפשר להבהיר אסטרטגיות פרומפט שונות ועוזר להבין מתי להשתמש בכל גישה. גם נרוויח מהגבלות שימוש נמוכות יותר של Azure עבור GPT-5.2 לעומת דגמים של GitHub.

## דרישות מוקדמות

- השלמת מודול 01 (משאבי Azure OpenAI מוצבים)
- קובץ `.env` בתיקיית השורש עם אישורי Azure (נוצר על ידי `azd up` במודול 01)

> **הערה:** אם לא השלמתם את מודול 01, עקבו אחרי הוראות הפריסה שם קודם.

## הבנת הנדסת פרומפטים

<img src="../../../translated_images/he/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="מהי הנדסת פרומפטים?" width="800"/>

הנדסת פרומפטים היא עיצוב טקסט כניסה שמביא תוצאות עקביות. זה לא רק לשאול שאלות – אלא לבנות בקשות כך שהמודל יבין בדיוק מה אתם רוצים ואיך לספק זאת.

תחשבו על זה כמו מתן הוראות לעמית לעבודה. "תקן את הבאג" זה לא מדויק. "תקן את החריגה null pointer ב-UserService.java שורה 45 על ידי הוספת בדיקת null" הוא מדויק. דגמי שפה עובדים באותה צורה – דיוק ומבנה חשובים.

<img src="../../../translated_images/he/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="איך LangChain4j משתלב" width="800"/>

LangChain4j מספק את התשתית — חיבורי דגם, זיכרון וסוגי הודעות — בעוד שהתבניות של הפרומפט הן טקסט בנוי בקפידה שאתם שולחים דרך התשתית הזו. אבני הבניין המרכזיות הן `SystemMessage` (שקובע את התפקיד וההתנהגות של ה-AI) ו-`UserMessage` שמכיל את הבקשה שלכם בפועל.

## יסודות הנדסת פרומפטים

<img src="../../../translated_images/he/five-patterns-overview.160f35045ffd2a94.webp" alt="סקירה של חמש תבניות הנדסת פרומפטים" width="800"/>

לפני שנצלול לתבניות המתקדמות במודול הזה, נסקור חמש טכניקות פרומפטינג בסיסיות. אלו אבני הבניין שכל מהנדס פרומפט צריך להכיר. אם כבר עשית את [מודול ההתחלה המהירה](../00-quick-start/README.md#2-prompt-patterns), כבר ראית אותם בפעולה – הנה המסגרת התיאורטית מאחוריהן.

### פרומפטינג בפתח אפס (Zero-Shot Prompting)

הגישה הפשוטה ביותר: תן למודל הוראה ישירה ללא דוגמאות. המודל מסתמך לחלוטין על האימון שלו כדי להבין ולבצע את המשימה. זה עובד טוב לבקשות פשוטות שבהן ההתנהגות הצפויה ברורה.

<img src="../../../translated_images/he/zero-shot-prompting.7abc24228be84e6c.webp" alt="פרומפטינג בפתח אפס" width="800"/>

*הוראה ישירה ללא דוגמאות — המודל מסיק את המשימה מההוראה בלבד*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// תגובה: "חיובי"
```
  
**מתי להשתמש:** סיווגים פשוטים, שאלות ישירות, תרגומים, או כל משימה שהמודל יכול לבצע בלי הדרכה נוספת.

### פרומפטינג עם דוגמאות מועטות (Few-Shot Prompting)

ספק דוגמאות הממחישות את הדפוס שהמודל צריך לעקוב אחריו. המודל לומד את פורמט הקלט-פלט המצופה מתוך הדוגמאות ומיישם אותו על קלטים חדשים. זה משפר באופן דרמטי עקביות למשימות שבהן הפורמט או ההתנהגות הרצויה אינם ברורים.

<img src="../../../translated_images/he/few-shot-prompting.9d9eace1da88989a.webp" alt="פרומפטינג עם דוגמאות מועטות" width="800"/>

*לימוד מדוגמאות — המודל מזהה את הדפוס ומחיל אותו על קלטים חדשים*

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
  
**מתי להשתמש:** סיווגים מותאמים אישית, פורמט קבוע, משימות תחום ספציפיות, או בעת קבלת תוצאות לא עקביות ב-zero-shot.

### שרשרת מחשבות (Chain of Thought)

בקש מהמודל להראות את תהליך החשיבה שלב אחר שלב. במקום לקפוץ ישר לתשובה, המודל מפרק את הבעיה ועובר על כל שלב במפורש. זה משפר דיוק במתמטיקה, היגיון ומשימות הסקה מרובות שלבים.

<img src="../../../translated_images/he/chain-of-thought.5cff6630e2657e2a.webp" alt="פרומפטינג שרשרת מחשבות" width="800"/>

*הסקת מסקנות שלב אחר שלב – פירוק בעיות מורכבות לצעדים לוגיים מפורשים*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// המודל מראה: 15 - 8 = 7, ואז 7 + 12 = 19 תפוחים
```
  
**מתי להשתמש:** בעיות מתמטיות, חידות היגיון, איתור שגיאות, או כל משימה שבה הצגת תהליך החשיבה משפרת דיוק ואמון.

### פרומפטינג מבוסס תפקיד (Role-Based Prompting)

קבע דמות או תפקיד ל-AI לפני ששואלים את השאלה. זה נותן הקשר שמשפיע על הטון, העומק והפוקוס בתגובה. "ארכיטקט תוכנה" ייתן עצות שונות מ"מתכנת זוטר" או "בודק אבטחה".

<img src="../../../translated_images/he/role-based-prompting.a806e1a73de6e3a4.webp" alt="פרומפטינג מבוסס תפקיד" width="800"/>

*הגדרת הקשר ודמות – אותה שאלה מקבלת תגובות שונות בהתאם לתפקיד שהוקצה*

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
  
**מתי להשתמש:** ביקורות קוד, הדרכה, ניתוח תחום ספציפי, או כשצריך תגובות מותאמות לפי רמת מומחיות או זווית מסוימת.

### תבניות פרומפט (Prompt Templates)

צור פרומפטים שניתן להשתמש בהם חוזר עם משתנים. במקום לכתוב פרומפט חדש כל פעם, הגדר תבנית פעם אחת ומלא בערכים שונים. מחלקת `PromptTemplate` של LangChain4j עושה זאת בקלות עם תחביר `{{variable}}`.

<img src="../../../translated_images/he/prompt-templates.14bfc37d45f1a933.webp" alt="תבניות פרומפט" width="800"/>

*פרומפטים לשימוש חוזר עם משתנים – תבנית אחת, שימושים רבים*

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
  
**מתי להשתמש:** שאילתות חוזרות עם קלטים שונים, עיבוד אצווה, בניית זרימות עבודה ל-AI לשימוש חוזר, או בכל מצב שבו מבנה הפרומפט נשאר זהה והנתונים משתנים.

---

חמשת היסודות האלה מספקים ערכת כלים איתנה לרוב משימות הפרומפטינג. שאר המודול מתבסס עליהם עם **שמונה תבניות מתקדמות** שמנצלות את בקרת החשיבה, ההערכה העצמית ויכולות הפלט המובנה של GPT-5.2.

## תבניות מתקדמות

לאחר כיסינו את היסודות, נעבור לשמונה התבניות המתקדמות שהופכות מודול זה לייחודי. לא כל בעיה דורשת את אותה גישה. יש שאלות שדורשות תשובות מהירות, אחרות חשיבה מעמיקה. יש כאלו שצריכות חשיבה גלויה, אחרות רק תוצאות. כל תבנית מטה מותאמת לתרחיש שונה — ובקרת החשיבה של GPT-5.2 מדגישה את ההבדלים אפילו יותר.

<img src="../../../translated_images/he/eight-patterns.fa1ebfdf16f71e9a.webp" alt="שמונה תבניות פרומפט" width="800"/>

*סקירה של שמונה תבניות הנדסת פרומפטים ושימושיהן*

<img src="../../../translated_images/he/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="בקרת חשיבה עם GPT-5.2" width="800"/>

*בקרת החשיבה של GPT-5.2 מאפשרת לציין כמה לחשוב המודל – מתשובות ישירות ומהירות ועד חקירה מעמיקה*

**חריצות נמוכה (מהיר וממוקד)** - לשאלות פשוטות שבהן רוצים תשובות ישירות ומהירות. המודל עושה מינימום חשיבה – עד 2 שלבים. השתמשו בזה לחישובים, חיפושים או שאלות ישירות.

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```
  
> 💡 **חקור עם GitHub Copilot:** פתח את [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ושאל:  
> - "מה ההבדל בין דפוסי פרומפט של חריצות נמוכה לעומת גבוהה?"  
> - "איך תגיות XML בפרומפטים עוזרות לבנות את תגובת ה-AI?"  
> - "מתי להשתמש בתבניות של רפלקסיה עצמית לעומת הוראות ישירות?"

**חריצות גבוהה (עמוק ומקיף)** - לבעיות מורכבות שבהן רוצים ניתוח מקיף. המודל חוקר ביסודיות ומציג הסברים מפורטים. מתאים לתכנון מערכת, החלטות ארכיטקטוניות או מחקר מורכב.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**ביצוע משימה (התקדמות שלב-אחר-שלב)** - לזירוז משימות רב-שלביות. המודל מספק תוכנית מקדימה, מתאר כל שלב בעבודה, ואז נותן סיכום. מתאים למיגרציות, יישומים, או תהליכים מרובי שלבים.

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```
  
פרומפטינג בשרשרת חשיבה מבקש במפורש מהמודל להראות את תהליך החשיבה שלו, ומשפר דיוק במשימות מורכבות. הפירוק שלב-אחר-שלב מסייע גם לבני אדם וגם ל-AI להבין את ההיגיון.

> **🤖 נסה עם [שיחת GitHub Copilot](https://github.com/features/copilot):** שאל על הדפוס הזה:  
> - "איך אתאים את דפוס ביצוע המשימות עבור פעולות ארוכות טווח?"  
> - "מה אמצעי העבודה הטובים ביותר למבנה פתיחי כלים ביישומים פרודקשן?"  
> - "איך אפשר ללכוד ולהציג עדכוני התקדמות ביניים בממשק משתמש?"

<img src="../../../translated_images/he/task-execution-pattern.9da3967750ab5c1e.webp" alt="דפוס ביצוע משימה" width="800"/>

*תכנון → ביצוע → סיכום לעבודה רב-שלבית*

**קוד עם רפלקסיה עצמית** - ליצירת קוד באיכות פרודקשן. המודל מייצר קוד בהתאם לסטנדרטים עם טיפול שגיאות הולם. השתמשו בזה בפיתוח תכונות או שירותים חדשים.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/he/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="מחזור רפלקסיה עצמית" width="800"/>

*לולאת שיפור איטרטיבית - יצירה, הערכה, זיהוי בעיות, שיפור, חזרה*

**ניתוח מובנה** - להערכה עקבית. המודל בודק קוד במסגרת קבועה (נכונות, פרקטיקות, ביצועים, אבטחה, ניהול תחזוקה). השתמשו בזה לבדיקות קוד או הערכות איכות.

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```
  
> **🤖 נסה עם [שיחת GitHub Copilot](https://github.com/features/copilot):** שאל על ניתוח מובנה:  
> - "איך להתאים את מסגרת הניתוח לסוגים שונים של ביקורות קוד?"  
> - "מה הדרך הטובה ביותר לנתח ולפעול על פלט מובנה בצורה תכנתית?"  
> - "איך לוודא רמות חומרה עקביות במהלך מפגשי ביקורת שונים?"

<img src="../../../translated_images/he/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="דפוס ניתוח מובנה" width="800"/>

*מסגרת לבדיקות קוד עקביות עם רמות חומרה*

**שיחת רב-סבב** - לשיחות שדורשות הקשר. המודל זוכר הודעות קודמות וממשיך מהן. ייעודי לסשנים אינטראקטיביים או שאלות ותשובות מורכבות.

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

*איך הקשר השיחה מצטבר לאורך כמה סבבים עד הגבלת הטוקנים*

**הסקת מסקנות שלב-אחר-שלב** - לבעיות שדורשות לוגיקה גלויה. המודל מציג הסקת מסקנות מפורשת לכל שלב. מתאים לבעיות מתמטיות, חידות לוגיות, או כשצריך להבין תהליך חשיבה.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/he/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="דפוס פעולות שלב-אחר-שלב" width="800"/>

*פירוק בעיות לצעדים לוגיים מפורשים*

**פלט מוגבל ומוגדר** - לתגובות שדורשות פורמט ספציפי. המודל מקפיד על כללי פורמט ואורך. מתאים לסיכומים או כשצריך מבנה פלט מדויק.

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
  
<img src="../../../translated_images/he/constrained-output-pattern.0ce39a682a6795c2.webp" alt="דפוס פלט מוגבל" width="800"/>

*אכיפת דרישות פורמט, אורך ומבנה ספציפיות*

## שימוש במשאבים קיימים ב-Azure

**אמת את הפריסה:**

ודא שקובץ `.env` קיים בתיקיית השורש עם אישורי Azure (נוצר במהלך מודול 01):  
```bash
cat ../.env  # צריך להראות AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**הפעל את היישום:**

> **הערה:** אם כבר הפעלת את כל היישומים באמצעות `./start-all.sh` במודול 01, המודול הזה כבר רץ על פורט 8083. אפשר לדלג על פקודות ההפעלה ולפנות ישירות לכתובת http://localhost:8083.

**אפשרות 1: שימוש ב-Spring Boot Dashboard (מומלץ למשתמשי VS Code)**

מיכל הפיתוח (dev container) כולל את התוסף Spring Boot Dashboard, שמספק ממשק ויזואלי לניהול כל יישומי Spring Boot. תוכל למצוא אותו בסרגל הפעילות בצד שמאל של VS Code (חפש את האייקון של Spring Boot).

דרך Spring Boot Dashboard, תוכל:  
- לראות את כל יישומי Spring Boot זמינים בסביבת העבודה  
- להפעיל/להפסיק יישומים בלחיצה אחת  
- לצפות בלוגים של היישומים בזמן אמת  
- לנטר את מצב היישומים
פשוט לחץ על כפתור ההפעלה ליד "prompt-engineering" כדי להתחיל מודול זה, או התחל את כל המודולים בבת אחת.

<img src="../../../translated_images/he/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**אפשרות 2: שימוש בסקריפטים של shell**

הפעל את כל יישומי הווב (מודולים 01-04):

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

או הפעל רק את המודול הזה:

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

שני הסקריפטים טוענים אוטומטית משתני סביבה מקובץ `.env` בשורש ויבנו את ה-JARs אם הם לא קיימים.

> **הערה:** אם אתה מעדיף לבנות את כל המודולים בעצמך לפני ההפעלה:
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

פתח את http://localhost:8083 בדפדפן שלך.

**כדי לעצור:**

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

## צילומי מסך של היישום

<img src="../../../translated_images/he/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*לוח המחוונים הראשי המציג את כל 8 תבניות הנדסת הפרומפט עם המאפיינים ומקרי השימוש שלהן*

## חקירת התבניות

ממשק האינטרנט מאפשר לך להתנסות באסטרטגיות פרומפט שונות. כל תבנית פותרת בעיות שונות - נסה אותן כדי לראות מתי כל גישה זורחת.

### נכונות נמוכה לעומת גבוהה

שאל שאלה פשוטה כמו "מה זה 15% מ-200?" באמצעות נכונות נמוכה. תקבל תשובה מיידית וישירה. עכשיו שאל משהו מורכב כמו "עצב אסטרטגיית מטמון ל-API עם תנועה גבוהה" באמצעות נכונות גבוהה. צפה איך המודל מאט ומספק הסבר מפורט. אותו מודל, אותה מבנה שאלה - אבל הפרומפט אומר לו כמה לחשוב.

<img src="../../../translated_images/he/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*חישוב מהיר עם הסבר מינימלי*

<img src="../../../translated_images/he/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*אסטרטגיית מטמון מקיפה (2.8MB)*

### ביצוע משימות (הקדמות כלים)

זרימות עבודה רב-שלביות נהנות מתכנון מראש ורטוריקה של התקדמות. המודל מציג את מה שיעשה, מתאר כל שלב, ואז מסכם את התוצאות.

<img src="../../../translated_images/he/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*יצירת נקודת קצה REST עם תיאור שלב אחר שלב (3.9MB)*

### קוד המשקף את עצמו

נסה "צור שירות לאימות אימייל". במקום רק לייצר קוד ולעצור, המודל מייצר, מעריך מול קריטריוני איכות, מזהה נקודות תורפה ומשפר. תראה אותו חוזר על עצמו עד שהקוד עומד בסטנדרטים של הפקה.

<img src="../../../translated_images/he/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*שירות אימות אימייל מלא (5.2MB)*

### ניתוח מובנה

סקירות קוד זקוקות למסגרות הערכה עקביות. המודל מנתח קוד באמצעות קטגוריות קבועות (נכונות, פרקטיקות, ביצועים, אבטחה) עם רמות חומרה.

<img src="../../../translated_images/he/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*סקירת קוד במסגרת פורמלית*

### שיחה רב-סיבובית

שאל "מה זה Spring Boot?" ואז מיד שאל "הראה לי דוגמה". המודל זוכר את השאלה הראשונה שלך ונותן דוגמה ל-Spring Boot בדיוק אליה. בלי זיכרון, השאלה השנייה הייתה עמומה מדי.

<img src="../../../translated_images/he/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*שמירת הקשר בין השאלות*

### הסקת מסקנות שלב אחרי שלב

בחר בעיית מתמטיקה ונסה אותה גם עם הסקת מסקנות שלב אחרי שלב וגם עם נכונות נמוכה. נכונות נמוכה רק נותנת לך את התשובה - מהיר אך לא שקוף. הסקת מסקנות שלב אחר שלב מראה כל חישוב והחלטה.

<img src="../../../translated_images/he/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*בעיית מתמטיקה עם שלבים מפורשים*

### יציאה מוגבלת

כאשר אתה צריך פורמטים ספציפיים או מספר מילים מדויק, תבנית זו מכריחה עמידה קפדנית. נסה לייצר סיכום עם בדיוק 100 מילים בפורמט רשימות.

<img src="../../../translated_images/he/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*סיכום למידת מכונה עם שליטה בפורמט*

## מה שאתה באמת לומד

**מאמץ ההסקה משנה הכל**

GPT-5.2 מאפשר לך לשלוט במאמץ החישובי דרך הפרומפטים שלך. מאמץ נמוך אומר תגובות מהירות עם חקירה מינימלית. מאמץ גבוה אומר שהמודל לוקח זמן לחשוב לעומק. אתה לומד להתאים את המאמץ למורכבות המשימה - אל תבזבז זמן על שאלות פשוטות, אך אל תמהר גם בהחלטות מורכבות.

**מבנה מנחה את ההתנהגות**

מתבונן בתגיות XML בפרומפטים? הן לא לקישוט. מודלים עוקבים אחרי הוראות מובנות בצורה אמינה יותר מטקסט חופשי. כשאתה צריך תהליכים רב-שלביים או לוגיקה מורכבת, מבנה עוזר למודל לעקוב איפה הוא ומה מגיע אחר כך.

<img src="../../../translated_images/he/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*אנטומיה של פרומפט מובנה היטב עם חלקים ברורים וארגון בסגנון XML*

**איכות דרך הערכה עצמית**

התבניות המשקפות את עצמן עובדות על ידי הפיכת קריטריוני איכות למפורשים. במקום לקוות שהמודל "יעשה את זה נכון", אתה אומר לו בדיוק מה זה "נכון": לוגיקה תקינה, טיפול בשגיאות, ביצועים, אבטחה. המודל יכול אז להעריך את הפלט שלו ולשפר. זה הופך יצירת קוד מתהליך של הגרלה לתהליך מובנה.

**הקשר הוא סופי**

שיחות רב-סיבוביות עובדות על ידי הכללת היסטוריית הודעות בכל בקשה. אבל יש גבול - לכל מודל יש מונה תווים מקסימלי. ככל שהשיחות מתארכות, תצטרך אסטרטגיות לשמור על הקשר רלוונטי מבלי להגיע לגבול. מודול זה מראה איך הזיכרון עובד; בהמשך תלמד מתי לסכם, מתי לשכוח ומתי לשלוף.

## הצעדים הבאים

**מודול הבא:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**ניווט:** [← קודם: מודול 01 - מבוא](../01-introduction/README.md) | [חזרה לעיקרי](../README.md) | [הבא: מודול 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**הפצה**
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדיוק, יש לקחת בחשבון שתירגומים אוטומטיים עלולים להכיל שגיאות או אי-דיוקים. המסמך המקורי בשפתו המקורית הוא המקור המוסמך והמהימן. למידע קריטי מומלץ להשתמש בתרגום מקצועי על ידי מתרגם אנושי. איננו אחראים לכל אי הבנות או פרשנויות שגויות הנובעות משימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->