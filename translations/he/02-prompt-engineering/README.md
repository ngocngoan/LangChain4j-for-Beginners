# מודול 02: הנדסת פרומפטים עם GPT-5.2

## תוכן העניינים

- [סרטון הדרכה](../../../02-prompt-engineering)
- [מה תלמדו](../../../02-prompt-engineering)
- [דרישות מקדימות](../../../02-prompt-engineering)
- [הבנת הנדסת הפרומפט](../../../02-prompt-engineering)
- [יסודות הנדסת פרומפטים](../../../02-prompt-engineering)
  - [איפשור ירי אפס (Zero-Shot)](../../../02-prompt-engineering)
  - [איפשור ירי מעומד (Few-Shot)](../../../02-prompt-engineering)
  - [שרשרת מחשבה](../../../02-prompt-engineering)
  - [איפשור מבוסס תפקיד](../../../02-prompt-engineering)
  - [תבניות פרומפט](../../../02-prompt-engineering)
- [דפוסים מתקדמים](../../../02-prompt-engineering)
- [שימוש במשאבי Azure קיימים](../../../02-prompt-engineering)
- [צילומי מסך של היישום](../../../02-prompt-engineering)
- [חקירת הדפוסים](../../../02-prompt-engineering)
  - [נכונות נמוכה מול גבוה](../../../02-prompt-engineering)
  - [ביצוע משימות (הקדמות לכלים)](../../../02-prompt-engineering)
  - [קוד בעל חשיבה פנימית](../../../02-prompt-engineering)
  - [ניתוח מובנה](../../../02-prompt-engineering)
  - [שיחה מרובת סבבים](../../../02-prompt-engineering)
  - [היסק שלב אחר שלב](../../../02-prompt-engineering)
  - [פלט מוגבל](../../../02-prompt-engineering)
- [מה אתם באמת לומדים](../../../02-prompt-engineering)
- [השלבים הבאים](../../../02-prompt-engineering)

## סרטון הדרכה

צפו במפגש חי המסביר כיצד להתחיל עם המודול הזה: [Prompt Engineering with LangChain4j - Live Session](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## מה תלמדו

<img src="../../../translated_images/he/what-youll-learn.c68269ac048503b2.webp" alt="מה תלמדו" width="800"/>

במודול הקודם ראיתם כיצד זיכרון מאפשר שיחות עם AI והשתמשתם במודלים של GitHub לאינטראקציות בסיסיות. כעת נתמקד באיך לשאול שאלות — הפרומפטים עצמם — באמצעות GPT-5.2 של Azure OpenAI. האופן בו אתם בונים את הפרומפטים משפיע משמעותית על איכות התשובות שתקבלו. נתחיל בסקירת טכניקות יסוד של פרומפטינג, ואז נעבור לשמונה דפוסים מתקדמים שמנצלים במלואם את יכולות GPT-5.2.

נשתמש ב-GPT-5.2 כי הוא מציג שליטה על ההסקה - ניתן להורות למודל כמה לחשוב לפני מענה. זה מדגיש אסטרטגיות פרומפט שונות ועוזר להבין מתי להשתמש בכל גישה. בנוסף, נהנה ממגבלות קצב נמוכות יותר ב-Azure עבור GPT-5.2 בהשוואה למודלים של GitHub.

## דרישות מקדימות

- השלמת מודול 01 (משאבי Azure OpenAI פרוסים)
- קובץ `.env` בתיקיית השורש עם אישורי Azure (נוצר על ידי `azd up` במודול 01)

> **הערה:** אם לא השלמתם את מודול 01, עקבו תחילה אחר הוראות הפריסה שם.

## הבנת הנדסת הפרומפט

<img src="../../../translated_images/he/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="מהי הנדסת פרומפט?" width="800"/>

הנדסת פרומפט היא על עיצוב טקסט קלט שמביא באופן עקבי לתוצאות שאתם צריכים. זה לא רק לשאול שאלות - זה לבנות בקשות כך שהמודל יבין בדיוק מה אתם רוצים ואיך לספק את זה.

חשבו על זה כמו לתת הוראות לעמית לעבודה. "תקן את הבאג" הוא משפט מבלבל. "תקן את החריגת null pointer ב-UserService.java שורה 45 על ידי הוספת בדיקת null" הוא מפורט. מודלים לשוניים פועלים באותו אופן - ספציפיות ומבנה חשובים.

<img src="../../../translated_images/he/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="איך LangChain4j משתלב" width="800"/>

LangChain4j מספק את התשתית — חיבור למודלים, זיכרון וסוגי הודעות — בעוד דפוסי הפרומפט הם פשוט טקסט מבנה שאתם שולחים דרך התשתית הזו. אבני הבניין המרכזיות הן `SystemMessage` (הקובעת את התנהגות ותפקיד ה-AI) ו-`UserMessage` (שאחראית על הבקשה שלכם).

## יסודות הנדסת פרומפטים

<img src="../../../translated_images/he/five-patterns-overview.160f35045ffd2a94.webp" alt="סקירת חמש דפוסי הנדסת פרומפטים" width="800"/>

לפני שנצלול לדפוסים המתקדמים במודול זה, נסקור חמש טכניקות יסוד לפרומפטים. אלה אבני בניין שכל מהנדס פרומפטים צריך להכיר. אם כבר עבדתם עם [מודול התחלה מהירה](../00-quick-start/README.md#2-prompt-patterns), ראיתם אותם בפעולה — להלן המסגרת המושגית שמאחוריהם.

### איפשור ירי אפס (Zero-Shot Prompting)

הגישה הפשוטה ביותר: לתת למודל הוראה ישירה בלי דוגמאות. המודל מסתמך לגמרי על האימון שלו כדי להבין ולבצע את המשימה. זה עובד טוב לבקשות פשוטות שבהן ההתנהגות הצפויה ברורה.

<img src="../../../translated_images/he/zero-shot-prompting.7abc24228be84e6c.webp" alt="איפשור ירי אפס" width="800"/>

*הוראה ישירה בלי דוגמאות — המודל מסיק את המשימה מההוראה לבדה*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// תגובה: "חיובי"
```

**מתי להשתמש:** סיווגים פשוטים, שאלות ישירות, תרגומים, או כל משימה שהמודל יכול לטפל בה בלי הדרכה נוספת.

### איפשור ירי מעומד (Few-Shot Prompting)

הציגו דוגמאות שמדגימות את הדפוס שאתם רוצים שהמודל יעקוב אחריו. המודל לומד את פורמט הקלט-פלט המצופה מהדוגמאות ומיישם זאת על קלטים חדשים. זה משפר משמעותית עקביות במשימות שבהן הפורמט או ההתנהגות הרצויה אינם ברורים.

<img src="../../../translated_images/he/few-shot-prompting.9d9eace1da88989a.webp" alt="איפשור ירי מעומד" width="800"/>

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

**מתי להשתמש:** סיווגים מותאמים, עיצוב עקבי, משימות ספציפיות לתחום, או כשתוצאות ירי אפס אינן עקביות.

### שרשרת מחשבה (Chain of Thought)

בקשו מהמודל להראות את ההסקה שלו שלב אחר שלב. במקום לקפוץ ישר לתשובה, המודל מפרק את הבעיה ועובר על כל חלק במפורש. זה משפר דיוק במטלות חשבון, לוגיקה והסקת מסקנות מרובת שלבים.

<img src="../../../translated_images/he/chain-of-thought.5cff6630e2657e2a.webp" alt="שרשרת מחשבה בפרומפט" width="800"/>

*היסק שלב אחר שלב — פירוק בעיות מורכבות לצעדים לוגיים מפורשים*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// המודל מראה: 7 = 8 - 15, ואז 19 = 12 + 7 תפוחים
```

**מתי להשתמש:** בעיות מתמטיות, חידות לוגיות, איתור באגים, או כל משימה שבה הצגת תהליך ההסקה משפרת דיוק ואמון.

### איפשור מבוסס תפקיד (Role-Based Prompting)

קבעו פרסונה או תפקיד ל-AI לפני ששואלים את שאלתכם. זה מספק הקשר שמעצב את הטון, העומק והפוקוס של התשובה. "אדריכל תוכנה" נותן ייעוץ שונה מ"מפתח מתחיל" או "מבקר אבטחה".

<img src="../../../translated_images/he/role-based-prompting.a806e1a73de6e3a4.webp" alt="איפשור מבוסס תפקיד" width="800"/>

*קביעת הקשר ופרסונה — אותה שאלה מקבלת תשובה שונה בהתאם לתפקיד שהוקצה*

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

**מתי להשתמש:** סקירות קוד, מנטורינג, ניתוחים ספציפיים לתחום, או כשצריך תשובות המותאמות לרמת מומחיות או נקודת מבט מסוימת.

### תבניות פרומפט (Prompt Templates)

צרו פרומפטים לשימוש חוזר עם משתנים במקום קבוע. במקום לכתוב פרומפט חדש בכל פעם, הגדירו תבנית פעם אחת ומלאו ערכים שונים. מחלקת `PromptTemplate` של LangChain4j עושה זאת בקלות עם תחביר `{{variable}}`.

<img src="../../../translated_images/he/prompt-templates.14bfc37d45f1a933.webp" alt="תבניות פרומפט" width="800"/>

*פרומפטים לשימוש חוזר עם משתנים — תבנית אחת, שימושים רבים*

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

**מתי להשתמש:** שאילתות חוזרות עם קלטים שונים, עיבוד אצוות, בניית זרימות עבודה של AI לשימוש חוזר, או בכל תרחיש שבו מבנה הפרומפט נשאר זהה אך הנתונים משתנים.

---

חמשת היסודות האלה מספקים לכם ערכת כלים איתנה לרוב המשימות בפרומפטינג. שאר המודול בונה עליהם עם **שמונה דפוסים מתקדמים** המנצלים את יכולות השליטה בהסקה, הערכה עצמית ופלט מובנה של GPT-5.2.

## דפוסים מתקדמים

אחרי שהכיסוי של היסודות, נעבור לשמונה הדפוסים המתקדמים שהופכים את המודול לייחודי. לא כל בעיה זקוקה לאותה גישה. יש שאלות שדורשות תשובות מהירות, אחרות חשיבה מעמיקה. יש שצריכות הסבר נראֶה, אחרות רק תוצאות. כל דפוס מטה מותאם לתרחיש שונה — ושליטת ההסקה של GPT-5.2 מדגישה את ההבדלים אפילו יותר.

<img src="../../../translated_images/he/eight-patterns.fa1ebfdf16f71e9a.webp" alt="שמונה דפוסי פרומפטינג" width="800"/>

*סקירה של שמונת דפוסי הנדסת הפרומפט והיישומים שלהם*

<img src="../../../translated_images/he/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="שליטת הסקה עם GPT-5.2" width="800"/>

*שליטת ההסקה של GPT-5.2 מאפשרת לציין כמה לחשוב - מתשובות ישירות ומהירות ועד חקירה מעמיקה*

**נכונות נמוכה (מהיר וממוקד)** - לשאלות פשוטות שבהן רוצים תשובות מהירות וישירות. המודל מבצע הסקה מינימלית - עד שני שלבים. השתמשו בזה לחישובים, חיפושים או שאלות פשוטות.

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

> 💡 **חקרו עם GitHub Copilot:** פתחו [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ושאלו:
> - "מה ההבדל בין דפוסי פרומפט של נכונות נמוכה לעומת גבוהה?"
> - "איך תגיות XML בפרומפטים מסייעות למבנה התגובה של ה-AI?"
> - "מתי כדאי להשתמש בדפוסי חשיבה עצמית מול הוראות ישירות?"

**נכונות גבוהה (עמוק ומקיף)** - לבעיות מורכבות שבהן רוצים ניתוח מקיף. המודל חוקר לעומק ומציג הסקה מפורטת. השתמשו בזה לעיצוב מערכות, קבלת החלטות ארכיטקטוניות או מחקר מורכב.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**ביצוע משימות (התקדמות שלב אחרי שלב)** - לזרימות עבודה מרובות שלבים. המודל מספק תוכנית מראש, מתעד כל שלב תוך עבודה ומסכם בסוף. השתמשו בזה למעברים, מימושים או תהליכים מרובי שלבים.

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

פרומפט של שרשרת מחשבה מבקש במפורש מהמודל להראות את תהליך ההסקה שלו, ומשפר דיוק במשימות מורכבות. הפירוק שלב אחר שלב עוזר גם לבני אדם וגם ל-AI להבין את הלוגיקה.

> **🤖 נסו עם שיחת [GitHub Copilot](https://github.com/features/copilot):** שאלו על דפוס זה:
> - "איך הייתי מתאים את דפוס ביצוע המשימה לפעולות ארוכות טווח?"
> - "מהן ההרגלים הטובים ביותר לבניית הקדמות לכלים ביישומים פרודקשן?"
> - "איך אפשר ללכוד ולהציג עדכוני התקדמות בינוניים בממשק משתמש?"

<img src="../../../translated_images/he/task-execution-pattern.9da3967750ab5c1e.webp" alt="דפוס ביצוע משימה" width="800"/>

*תכנון → ביצוע → סיכום לזרימות עבודה מרובות שלבים*

**קוד בעל חשיבה פנימית** - ליצירת קוד איכותי לפרודקשן. המודל מייצר קוד לפי סטנדרטים עם טיפול בשגיאות מתאים. השתמשו בזה בעת בניית תכונות או שירותים חדשים.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/he/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="מחזור חשיבה פנימית" width="800"/>

*לולאת שיפור איטרטיבית - יצירה, הערכה, זיהוי בעיות, שיפור, חזרה*

**ניתוח מובנה** - להערכה עקבית. המודל סוקר קוד באמצעות מסגרת קבועה (נכונות, שיטות עבודה, ביצועים, אבטחה, תחזוקה). השתמשו בזה לסקירות קוד או הערכות איכות.

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

> **🤖 נסו עם שיחת [GitHub Copilot](https://github.com/features/copilot):** שאלו על ניתוח מובנה:
> - "איך להתאים את מסגרת הניתוח לסוגים שונים של סקירות קוד?"
> - "מה הדרך הטובה ביותר לפרש ולפעול לפי פלט מובנה בתוכנה?"
> - "איך להבטיח רמות חומרה עקביות בין סבבי סקירה שונים?"

<img src="../../../translated_images/he/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="דפוס ניתוח מובנה" width="800"/>

*מסגרת לסקירות קוד עקביות עם רמות חומרה*

**שיחה מרובת סבבים** - לשיחות שזקוקות להקשר. המודל זוכר הודעות קודמות ובונה עליהן. השתמשו בזה למפגשי עזרה אינטראקטיביים או שאלות מורכבות.

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

*איך ההקשר של השיחה מצטבר על פני סבבים מרובים עד להגבלת התווים*

**היסק שלב אחר שלב** - לבעיות שדורשות לוגיקה גלויה. המודל מציג הסקה מפורשת בכל שלב. השתמשו בזה לבעיות מתמטיות, חידות לוגיות, או כשדרושה הבנה של תהליך המחשבה.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/he/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="דפוס שלב אחר שלב" width="800"/>

*פירוק בעיות לצעדים לוגיים מפורשים*

**פלט מוגבל** - לתגובות עם דרישות פורמט ספציפיות. המודל עומד בקפדנות בכללי הפורמט והאורך. השתמשו בזה לתמציות או כשנדרש מבנה פלט מדויק.

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

## שימוש במשאבי Azure קיימים

**אימות הפריסה:**

ודאו שקובץ `.env` נמצא בתיקיית השורש עם אישורי Azure (נוצר במהלך מודול 01):
```bash
cat ../.env  # צריך להציג את AZURE_OPENAI_ENDPOINT, את API_KEY ואת DEPLOYMENT
```

**הפעלת היישום:**

> **הערה:** אם כבר הפעלתם את כל היישומים באמצעות `./start-all.sh` ממודול 01, מודול זה כבר רץ על פורט 8083. תוכלו לדלג על פקודות ההפעלה למטה ולעבור ישירות לכתובת http://localhost:8083.

**אפשרות 1: שימוש ב-Spring Boot Dashboard (מומלץ למשתמשי VS Code)**
מיכל הפיתוח כולל את תוסף Spring Boot Dashboard, המספק ממשק חזותי לניהול כל יישומי Spring Boot. ניתן למצוא אותו בסרגל הפעילות בצד שמאל של VS Code (חפשו את סמל Spring Boot).

מ-Spring Boot Dashboard, תוכלו:
- לראות את כל יישומי Spring Boot הזמינים בסביבת העבודה
- להפעיל/להפסיק יישומים בלחיצה אחת
- לצפות ביומני היישום בזמן אמת
- לנטר את מצב היישום

פשוט לחצו על כפתור ההפעלה שלצד "prompt-engineering" כדי להפעיל את המודול הזה, או להפעיל את כל המודולים בבת אחת.

<img src="../../../translated_images/he/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**אפשרות 2: שימוש בסקריפטים של shell**

הפעלת כל יישומי הווב (מודולים 01-04):

**Bash:**
```bash
cd ..  # מהתיקייה הראשית
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

שני הסקריפטים טוענים אוטומטית משתני סביבה מהקובץ `.env` שמשורש ויבנו את קבצי ה-JAR אם הם לא קיימים.

> **הערה:** אם אתם מעדיפים לבנות את כל המודולים ידנית לפני ההפעלה:
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

פתחו בבראוזר את http://localhost:8083.

**להפסקה:**

**Bash:**
```bash
./stop.sh  # רק במודול זה
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

*לוח המחוונים הראשי המציג את כל 8 דפוסי הנדסת הפרומפט עם התכונות ומקרי השימוש שלהם*

## חקר הדפוסים

הממשק האינטרנטי מאפשר לכם להתנסות באסטרטגיות פרומפט שונות. כל דפוס פותר בעיות שונות - נסו אותם כדי לראות מתי כל גישה מתבלטת.

> **הערה: סטרימינג מול לא סטרימינג** — כל דף דפוס מציע שני כפתורים: **🔴 Stream Response (Live)** ואפשרות **לא סטרימינג**. סטרימינג משתמש באירועים שנשלחים מהשרת (SSE) להצגת הטוקנים בזמן אמת ככל שהמודל מייצר אותם, כך שתראו את ההתקדמות מיד. אופציית הלא-סטרימינג מחכה לתשובה המלאה לפני התצוגה. עבור פרומפטים שמעוררים חשיבה עמוקה (למשל High Eagerness, Self-Reflecting Code), הקריאה ללא סטרימינג יכולה לקחת זמן רב מאוד - לפעמים דקות - ללא משוב נראה לעין. **השתמשו בסטרימינג כשמתנסים בפרומפטים מורכבים** כדי לראות את המודל פועל ולהימנע מהרגשה שהבקשה נתקעה.
>
> **הערה: דרישת דפדפן** — תכונת הסטרימינג משתמשת ב-Fetch Streams API (`response.body.getReader()`) שדורש דפדפן מלא (Chrome, Edge, Firefox, Safari). היא **לא** עובדת בדפדפן הפשוט המובנה של VS Code, כיוון ש-webview שלו אינו תומך ב-ReadableStream API. אם תשתמשו בדפדפן הפשוט, כפתורי הלא-סטרימינג יעבדו כרגיל — רק הכפתורים לסטרימינג יושפעו. פתחו את `http://localhost:8083` בדפדפן חיצוני לחוויה מלאה.

### Low vs High Eagerness

שאלו שאלה פשוטה כמו "מה זה 15% מ-200?" תוך שימוש ב-Low Eagerness. תקבלו תשובה מיידית וישירה. עכשיו שאלו משהו מורכב כמו "עצבו אסטרטגיית קאשינג ל-API עם תעבורה גבוהה" תוך שימוש ב-High Eagerness. לחצו על **🔴 Stream Response (Live)** וצפו בהופעת החשיבה המפורטת של המודל טוקן אחר טוקן. אותו מודל, אותו מבנה שאלה - אך הפרומפט מורה כמה לחשוב.

### ביצוע משימות (Tool Preambles)

זרימות עבודה רב-שלביות משתפרות מתכנון מראש וסיפור ההתקדמות. המודל מפרט מה יבוצע, מספר כל שלב ואז מסכם תוצאות.

### קוד המשקף את עצמו (Self-Reflecting Code)

נסו "צרו שירות אימות אימייל". במקום רק לייצר קוד ולהפסיק, המודל מייצר, מעריך מול קריטריוני איכות, מזהה חולשות ומשפר. תראו אותו חוזר על התהליך עד שהקוד עומד בסטנדרטים של ייצור.

### ניתוח מובנה

ביקורות קוד דורשות מסגרות הערכה עקביות. המודל מנתח קוד לפי קטגוריות קבועות (נכונות, פרקטיקות, ביצועים, אבטחה) עם דרגות חומרה שונות.

### שיחה מרובת סבבים

שאלו "מה זה Spring Boot?" ואז מיד המשיכו ב"תראה לי דוגמה". המודל זוכר את השאלה הראשונה ונותן דוגמה ספציפית ל-Spring Boot. בלי זיכרון, השאלה השנייה הייתה תהיה כללית מדי.

### חשיבה שלב-אחר-שלב

בחרו בעיה מתמטית ונסו לפתור אותה הן עם חשיבה שלב-אחר-שלב והן עם Low Eagerness. Low eagerness נותן את התשובה - מהיר אבל אפל. חשיבה שלב-אחר-שלב מראה כל חישוב והחלטה.

### פלט מוגבל

כשאתם צריכים פורמטים מדויקים או ספירת מילים, דפוס זה אוכף עמידה מחמירה. נסו לייצר סיכום עם בדיוק 100 מילים בפורמט בולטים.

## מה שאתם באמת לומדים

**מאמץ חשיבתי משנה את הכל**

GPT-5.2 מאפשר לכם לשלוט במאמץ החישובי דרך הפרומפטים שלכם. מאמץ נמוך משמעותו תגובות מהירות עם חקירה מינימלית. מאמץ גבוה משמעותו שהמודל מבלה זמן בחשיבה עמוקה. אתם לומדים להתאים מאמץ למורכבות המשימה - אל תבזבזו זמן על שאלות פשוטות, אבל אל תרוצו גם בהחלטות מורכבות.

**מבנה מדריך התנהגות**

שמתם לב לתגי XML בפרומפטים? הם לא רק קישוט. מודלים עוקבים אחרי הוראות מובנות בצורה אמינה יותר מטקסט חופשי. כשאתם צריכים תהליכים רב-שלביים או לוגיקה מורכבת, מבנה עוזר למודל לעקוב איפה הוא ומה הבא.

<img src="../../../translated_images/he/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*אנטומיה של פרומפט מובנה היטב עם חלקים ברורים וארגון בסגנון XML*

**איכות דרך הערכה עצמית**

הדפוסים שמשקפים את עצמם פועלים על ידי הצגת קריטריוני איכות במפורש. במקום לקוות שהמודל "יעשה את זה נכון", אתם אומרים לו בדיוק מה פירוש "נכון": לוגיקה נכונה, טיפול בשגיאות, ביצועים, אבטחה. המודל יכול אז להעריך את הפלט שלו ולשפר. זה הופך יצירת קוד מלוטו לתהליך.

**הקשר הוא מוגבל**

שיחות מרובות סבבים פועלות על ידי הכללת היסטוריית הודעות עם כל בקשה. אבל יש גבול - לכל מודל יש מספר מקסימלי של טוקנים. ככל שהשיחות מתארכות, תצטרכו אסטרטגיות לשמור על ההקשר הרלוונטי מבלי להגיע לגג. המודול הזה מראה לכם איך זיכרון עובד; בהמשך תלמדו מתי לסכם, מתי לשכוח, ומתי לשחזר פרטים.

## השלבים הבאים

**מודול הבא:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**ניווט:** [← קודם: מודול 01 - מבוא](../01-introduction/README.md) | [חזרה לעמוד הראשי](../README.md) | [הבא: מודול 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב ויתור**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדייק, יש לזכור כי תרגומים אוטומטיים עלולים להכיל שגיאות או אי דיוקים. המסמך המקורי בשפתו המקורית נחשב למקור הסמכותי. למידע קריטי מומלץ להשתמש בתרגום מקצועי אנושי. איננו אחראים על הבנות או פרשנויות שגויות הנובעות מהשימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->