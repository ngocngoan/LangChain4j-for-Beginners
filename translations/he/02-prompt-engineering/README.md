# מודול 02: הנדסת פרומפטים עם GPT-5.2

## תוכן העניינים

- [מה תלמדו](../../../02-prompt-engineering)
- [דרישות מוקדמות](../../../02-prompt-engineering)
- [הבנת הנדסת פרומפטים](../../../02-prompt-engineering)
- [יסודות הנדסת פרומפטים](../../../02-prompt-engineering)
  - [פרומפטינג ללא דוגמאות (Zero-Shot)](../../../02-prompt-engineering)
  - [פרומפטינג עם מספר דוגמאות (Few-Shot)](../../../02-prompt-engineering)
  - [שרשרת מחשבה (Chain of Thought)](../../../02-prompt-engineering)
  - [פרומפטינג מבוסס תפקידים](../../../02-prompt-engineering)
  - [תבניות פרומפט](../../../02-prompt-engineering)
- [תבניות מתקדמות](../../../02-prompt-engineering)
- [שימוש במשאבי Azure קיימים](../../../02-prompt-engineering)
- [צילום מסך של היישום](../../../02-prompt-engineering)
- [חקירת התבניות](../../../02-prompt-engineering)
  - [רצון נמוך לעומת גבוה](../../../02-prompt-engineering)
  - [ביצוע משימות (הקדמות כלים)](../../../02-prompt-engineering)
  - [קוד המשקף את עצמו](../../../02-prompt-engineering)
  - [ניתוח מובנה](../../../02-prompt-engineering)
  - [שיחה רב-סבבית](../../../02-prompt-engineering)
  - [היסק צעד-אחר-צעד](../../../02-prompt-engineering)
  - [פלט מוגבל](../../../02-prompt-engineering)
- [מה אתם באמת לומדים](../../../02-prompt-engineering)
- [שלבים הבאים](../../../02-prompt-engineering)

## מה תלמדו

<img src="../../../translated_images/he/what-youll-learn.c68269ac048503b2.webp" alt="מה תלמדו" width="800"/>

במודול הקודם ראית כיצד זיכרון מאפשר בינה מלאכותית בשיחה והשתמשת במודלים של GitHub לאינטראקציות בסיסיות. כעת נתמקד כיצד אתם שואלים שאלות — הפרומפטים עצמם — באמצעות GPT-5.2 של Azure OpenAI. הדרך בה אתם מבנים את הפרומפטים משפיעה באופן דרמטי על איכות התשובות שתקבלו. נתחיל בסקירת טכניקות פרומפט בסיסיות, ולאחר מכן נעבור לשמונה תבניות מתקדמות שמנצלות את יכולות GPT-5.2 במלואן.

נשתמש ב-GPT-5.2 כי הוא מציג בקרת חשיבה - אתם יכולים להגיד למודל כמה לחשוב לפני התשובה. זה מבהיר אסטרטגיות פרומפט שונות ועוזר להבין מתי להשתמש בכל אחת מהן. ניהנה גם מהמגבלות נמוכות יותר של Azure על GPT-5.2 לעומת מודלים של GitHub.

## דרישות מוקדמות

- השלמת מודול 01 (משאבי Azure OpenAI מפעילים)
- קובץ `.env` בתיקיית השורש עם אישורים ל-Azure (נוצר על ידי `azd up` במודול 01)

> **הערה:** אם לא השלמת את מודול 01, עקוב תחילה אחר הוראות ההפעל במודול ההוא.

## הבנת הנדסת פרומפטים

<img src="../../../translated_images/he/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="מהי הנדסת פרומפטים?" width="800"/>

הנדסת פרומפטים מתמקדת בעיצוב טקסט קלט שמספק תוצאות אמינות שאתם צריכים. זה לא רק לשאול שאלות - זה לבנות בקשות באופן שהמודל יבין בדיוק מה אתם רוצים ואיך לספק את זה.

תחשבו על זה כמו לתת הוראות לעמית לעבודה. "תקן את הבאג" הוא כללי מדי. "תקן את החריגה null pointer בשורה 45 ב-UserService.java על ידי הוספת בדיקת null" הוא מדויק. מודלים לשוניים פועלים באותה צורה - דייקנות ומבנה חשובים.

<img src="../../../translated_images/he/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="כיצד LangChain4j משתלב" width="800"/>

LangChain4j מספקת את התשתית — חיבורים למודלים, זיכרון, וסוגי הודעות — בעוד דפוסי הפרומפט הם פשוט טקסט מובנה בקפידה שנשלח דרך התשתית הזו. אבני הבניין המרכזיות הן `SystemMessage` (המגדירה את התנהגות ומעמד ה-AI) ו-`UserMessage` (שנושאת את הבקשה האמיתית שלכם).

## יסודות הנדסת פרומפטים

<img src="../../../translated_images/he/five-patterns-overview.160f35045ffd2a94.webp" alt="סקירת חמש תבניות הנדסת פרומפטים" width="800"/>

לפני שנצלול לתבניות המתקדמות במודול זה, נסקור חמש טכניקות פרומפט יסודיות. אלו לבנות הבסיס שכל מהנדס פרומפטים צריך להכיר. אם כבר עברת דרך [מודול התחלה מהירה](../00-quick-start/README.md#2-prompt-patterns), כבר ראית אותן בפעולה — הנה המסגרת הרעיונית שמאחוריהן.

### פרומפטינג ללא דוגמאות (Zero-Shot)

הגישה הפשוטה ביותר: תן למודל הוראה ישירה ללא דוגמאות. המודל מסתמך על ההכשרה שלו להבנת והפעלת המשימה. עובד טוב לבקשות פשוטות שבהן ההתנהגות הצפויה ברורה.

<img src="../../../translated_images/he/zero-shot-prompting.7abc24228be84e6c.webp" alt="פרומפטינג ללא דוגמאות" width="800"/>

*הוראה ישירה ללא דוגמאות — המודל מפיק את המשימה מהוראתו בלבד*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// תגובה: "חיובי"
```

**מתי להשתמש:** סיווגים פשוטים, שאלות ישירות, תרגומים, או כל משימה שהמודל יכול לטפל בה ללא הנחיות נוספות.

### פרומפטינג עם מספר דוגמאות (Few-Shot)

ספק דוגמאות שמדגימות את התבנית שאתה רוצה שהמודל יעקוב אחריה. המודל לומד את פורמט הקלט-פלט הצפוי מהדוגמאות ומיישם אותו על קלטים חדשים. זה משפר באופן דרמטי את העקביות למשימות שבהן פורמט או התנהגות רצויים אינם מובנים מאליהם.

<img src="../../../translated_images/he/few-shot-prompting.9d9eace1da88989a.webp" alt="פרומפטינג עם מספר דוגמאות" width="800"/>

*למידה מדוגמאות — המודל מזהה את התבנית ומיישם אותה על קלטים חדשים*

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

**מתי להשתמש:** סיווגים מותאמים אישית, עיצוב עקבי, משימות ספציפיות לתחום, או כאשר תוצאות Zero-Shot אינן עקביות.

### שרשרת מחשבה (Chain of Thought)

בקש מהמודל להציג את הלוגיקה שלו צעד אחר צעד. במקום לעבור מיד לתשובה, המודל מפרק את הבעיה ועובר על כל חלק במפורש. זה משפר דיוק במתמטיקה, לוגיקה, וחשיבה רב-שלבית.

<img src="../../../translated_images/he/chain-of-thought.5cff6630e2657e2a.webp" alt="פרומפטינג שרשרת מחשבה" width="800"/>

*היסק צעד-אחר-צעד — פירוק בעיות מורכבות לשלבים לוגיים מפורשים*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// המודל מראה: 15 - 8 = 7, אז 7 + 12 = 19 תפוחים
```

**מתי להשתמש:** בעיות מתמטיות, חידות לוגיות, איתור באגים, או כל משימה שבה הצגת תהליך החשיבה משפרת דיוק ואמון.

### פרומפטינג מבוסס תפקידים

הגדר לדמות או לתפקיד ה-AI לפני שאילת השאלה שלך. זה מספק הקשר שמעצב את הטון, העומק, והמיקוד בתשובה. "ארכיטקט תוכנה" ייתן עצות שונות מ"מתכנת זוטר" או "מפקח אבטחה".

<img src="../../../translated_images/he/role-based-prompting.a806e1a73de6e3a4.webp" alt="פרומפטינג מבוסס תפקידים" width="800"/>

*הגדרת הקשר ותפקיד — לאותה שאלה מקבלים תגובה שונה בהתאם לתפקיד שהוקצה*

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

**מתי להשתמש:** סקירות קוד, הוראה פרטית, ניתוח ספציפי לתחום, או כאשר נדרשות תגובות המותאמות לרמת מומחיות או נקודת מבט מסוימת.

### תבניות פרומפט

צור פרומפטים לשימוש חוזר עם משתני מיקום. במקום לכתוב פרומפט חדש בכל פעם, הגדר תבנית פעם אחת ומלא ערכים שונים. המחלקה `PromptTemplate` של LangChain4j עושה זאת בקלות עם תחביר `{{variable}}`.

<img src="../../../translated_images/he/prompt-templates.14bfc37d45f1a933.webp" alt="תבניות פרומפט" width="800"/>

*פרומפטים רב-פעמיים עם משתנים — תבנית אחת, שימושים רבים*

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

**מתי להשתמש:** שאילתות חוזרות עם קלטים שונים, עיבוד באצוות, בניית תהליכי עבודה לשימוש חוזר, או כל מצב שבו מבנה הפרומפט נשאר זהה אך הנתונים משתנים.

---

חמשת היסודות הללו מעניקים כלי עבודה איתן לרוב משימות הפרומפטינג. שאר מודול זה בונה עליהם עם **שמונה תבניות מתקדמות** המנצלות את בקרת החשיבה, ההערכה העצמית, ויכולות הפלט המובנה של GPT-5.2.

## תבניות מתקדמות

אחרי שהכיסינו את היסודות, נעבור לשמונה תבניות מתקדמות שהופכות את המודול הזה לייחודי. לא כל בעיה דורשת את אותה גישה. חלק מהשאלות דורשות תשובות מהירות, אחרות חשיבה עמוקה. חלק דורשות היסק גלוי, ואחרות רק תוצאות. כל תבנית למטה מותאמת לתרחיש שונה — ובקרת החשיבה של GPT-5.2 מדגישה את ההבדלים אף יותר.

<img src="../../../translated_images/he/eight-patterns.fa1ebfdf16f71e9a.webp" alt="שמונה תבניות פרומפט" width="800"/>

*סקירה של שמונה תבניות הנדסת פרומפט ושימושיהן*

<img src="../../../translated_images/he/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="בקרת חשיבה עם GPT-5.2" width="800"/>

*בקרת החשיבה של GPT-5.2 מאפשרת לציין כמה המודל צריך לחשוב — מתשובות מהירות וישירות ועד חקירה מעמיקה*

<img src="../../../translated_images/he/reasoning-effort.db4a3ba5b8e392c1.webp" alt="השוואת מאמץ חשיבה" width="800"/>

*רצון נמוך (מהיר, ישיר) לעומת רצון גבוה (יסודי, חוקר) בגישות החשיבה*

**רצון נמוך (מהיר וממוקד)** - לשאלות פשוטות שבהן רצויות תשובות מהירות וישירות. המודל מבצע חשיבה מינימלית - עד שני שלבים. השתמש בזה לחישובים, חיפושים או שאלות ברורות.

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
> - "מה ההבדל בין תבניות פרומפט עם רצון נמוך לעומת גבוה?"
> - "כיצד תגיות XML בפרומפטים עוזרות למבנה תגובת ה-AI?"
> - "מתי להשתמש בתבניות של השתקפות עצמית לעומת הוראות ישירות?"

**רצון גבוה (עמוק ויסודי)** - לבעיות מורכבות שבהן דרוש ניתוח מקיף. המודל חוקר לעומק ומציג חשיבה מפורטת. השתמש בזה לעיצוב מערכות, החלטות ארכיטקטוניות או מחקר מסובך.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**ביצוע משימות (התקדמות צעד-אחר-צעד)** - לזרימות עבודה רב-שלביות. המודל מספק תוכנית מראש, מספר על כל שלב תוך כדי עבודה, ואחר כך נותן סיכום. השתמש בזה למיגרציות, מימושים, או כל תהליך בעל מספר שלבים.

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

פרומפטינג Chain-of-Thought מבקש במפורש מהמודל להראות את תהליך ההיסק שלו, ומשפר דיוק במשימות מורכבות. הפירוט הצעד-צעד עוזר הן לבני אדם והן ל-AI להבין את הלוגיקה.

> **🤖 נסה עם שיחת [GitHub Copilot](https://github.com/features/copilot):** שאל על התבנית הזו:
> - "כיצד אתאים את תבנית ביצוע המשימות לפעולות ארוכות טווח?"
> - "מהם השיטות הטובות ביותר למבנה הקדמות כלים ביישומי פרודקשן?"
> - "כיצד ללכוד ולהציג עדכוני התקדמות ביניים בממשק משתמש?"

<img src="../../../translated_images/he/task-execution-pattern.9da3967750ab5c1e.webp" alt="תבנית ביצוע משימה" width="800"/>

*תכנון → ביצוע → סיכום זרימת עבודה למשימות רב-שלביות*

**קוד המשקף את עצמו** - ליצירת קוד איכותי לפרודקשן. המודל מייצר קוד בהתאם לסטנדרטים עם טיפול שגיאות ראוי. השתמש בזה כאשר בונים תכונות או שירותים חדשים.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/he/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="מעגל השתקפות עצמית" width="800"/>

*לולאת שיפור איטרטיבית - יצירה, הערכה, זיהוי בעיות, שיפור, חזרה על התהליך*

**ניתוח מובנה** - להערכה עקבית. המודל בוחן קוד במסגרת מוגדרת (נכונות, פרקטיקות, ביצועים, אבטחה, תחזוקה). השתמש בזה לסקירות קוד או הערכות איכות.

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

> **🤖 נסה עם שיחת [GitHub Copilot](https://github.com/features/copilot):** שאל על ניתוח מובנה:
> - "כיצד להתאים את מסגרת הניתוח לסוגים שונים של סקירות קוד?"
> - "מההדרך הטובה ביותר לנתח ולהגיב לפלט מובנה באופן תכנותי?"
> - "כיצד להבטיח רמות חומרה עקביות במהלך מפגשי סקירה שונים?"

<img src="../../../translated_images/he/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="תבנית ניתוח מובנה" width="800"/>

*מסגרת לסקירות קוד עקביות עם רמות חומרה*

**שיחה רב-סבבית** - לשיחות שדורשות הקשר. המודל זוכר הודעות קודמות ובונה עליהן. השתמש בזה למפגשי עזרה אינטראקטיביים או שאלות ותשובות מורכבות.

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

*כיצד מצטבר ההקשר בשיחה לאורך מספר סבבים עד שמגיעים למגבלת הטוקנים*

**היסק צעד-אחר-צעד** - לבעיות שדורשות לוגיקה גלויה. המודל מציג היסק מפורש לכל שלב. השתמש בזה לבעיות מתמטיות, חידות לוגיות, או כשצריך להבין את תהליך החשיבה.

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

**פלט מוגבל** - לתגובות עם דרישות פורמט ספציפיות. המודל מקפיד על כללים לגבי פורמט ואורך. השתמש בזה לסיכומים או כשנדרש מבנה פלט מדויק.

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

**ודא הפעלה:**

ודא שקובץ `.env` נמצא בתיקיית השורש עם אישורים ל-Azure (נוצר במהלך מודול 01):
```bash
cat ../.env  # צריך להציג את AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**הפעל את היישום:**

> **הערה:** אם כבר הפעלת את כל היישומים באמצעות `./start-all.sh` במודול 01, המודול הזה כבר פועל על הפורט 8083. ניתן לדלג על פקודות ההפעלה למטה ולעבור ישירות אל http://localhost:8083.

**אפשרות 1: שימוש ב-Spring Boot Dashboard (מומלץ למשתמשי VS Code)**

מיכל הפיתוח כולל את התוסף Spring Boot Dashboard, שמספק ממשק חזותי לניהול כל יישומי Spring Boot. ניתן למצוא אותו בסרגל הפעילויות בצד שמאל של VS Code (חפש את סמל Spring Boot).
מן לוח המחוונים של Spring Boot, ניתן:
- לראות את כל אפליקציות ה-Spring Boot הזמינות בסביבת העבודה
- להפעיל/להפסיק אפליקציות בלחיצה אחת
- לצפות בלוגים של האפליקציה בזמן אמת
- לנטר את מצב האפליקציה

פשוט לחצו על כפתור ההפעלה ליד "prompt-engineering" כדי להתחיל את המודול הזה, או להפעיל את כל המודולים בבת אחת.

<img src="../../../translated_images/he/dashboard.da2c2130c904aaf0.webp" alt="לוח מחוונים של Spring Boot" width="400"/>

**אפשרות 2: שימוש בסקריפטים בשורת הפקודה**

הפעלת כל אפליקציות הרשת (מודולים 01-04):

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

שני הסקריפטים טוענים אוטומטית משתני סביבה מקובץ ה-`.env` שבראש התיקיה ויבנו את קבצי ה-JAR אם אינם קיימים.

> **הערה:** אם אתה מעדיף לבנות את כל המודולים באופן ידני לפני ההפעלה:
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
.\stop.ps1  # רק מודול זה
# או
cd ..; .\stop-all.ps1  # כל המודולים
```

## צילומי מסך של האפליקציה

<img src="../../../translated_images/he/dashboard-home.5444dbda4bc1f79d.webp" alt="המסך הראשי של לוח המחוונים" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*לוח המחוונים הראשי המציג את כל 8 תבניות הנדסת הפקודות עם התכונות ומקרי השימוש שלהם*

## חקר התבניות

ממשק הרשת מאפשר לך לנסות אסטרטגיות פקודה שונות. כל תבנית פותרת בעיות שונות - נסה אותן כדי לראות מתי כל גישה זוהרת.

### התלהבות נמוכה לעומת גבוהה

שאל שאלה פשוטה כמו "מהו 15% מתוך 200?" באמצעות התלהבות נמוכה. תקבל תשובה מיידית וישירה. עכשיו שאל שאלה מורכבת כמו "תכנן אסטרטגיית מטמון ל-API עם תנועה גבוהה" באמצעות התלהבות גבוהה. צפה כיצד הדגם מאט ומספק נימוקים מפורטים. אותו דגם, אותו מבנה שאלה - אבל ההנחיה אומרת לו כמה לחשוב.

<img src="../../../translated_images/he/low-eagerness-demo.898894591fb23aa0.webp" alt="הדגמת התלהבות נמוכה" width="800"/>

*חישוב מהיר עם נימוק מינימלי*

<img src="../../../translated_images/he/high-eagerness-demo.4ac93e7786c5a376.webp" alt="הדגמת התלהבות גבוהה" width="800"/>

*אסטרטגיית מטמון מקיפה (2.8MB)*

### ביצוע משימה (תבניות מקדימות לכלים)

זרימות עבודה רב-שלביות נהנות מתכנון מראש וסיפור ההתקדמות. הדגם מפרט מה הוא עומד לעשות, מספר על כל שלב, ואז מסכם תוצאות.

<img src="../../../translated_images/he/tool-preambles-demo.3ca4881e417f2e28.webp" alt="הדגמת ביצוע משימה" width="800"/>

*יצירת נקודת קצה REST עם סיפור שלב אחרי שלב (3.9MB)*

### קוד עם התבוננות עצמית

נסה "צור שירות אימות אימייל". במקום רק לייצר קוד ולעצור, הדגם מייצר, מעריך על פי קריטריוני איכות, מזהה חולשות ומשפר. תראה אותו חוזר על עצמו עד שהקוד עונה על סטנדרטים של ייצור.

<img src="../../../translated_images/he/self-reflecting-code-demo.851ee05c988e743f.webp" alt="הדגמת קוד עם התבוננות עצמית" width="800"/>

*שירות אימות אימייל מלא (5.2MB)*

### ניתוח מובנה

סקירות קוד דורשות מסגרות הערכה עקביות. הדגם מנתח קוד בעזרת קטגוריות קבועות (נכונות, נהלים, ביצועים, אבטחה) עם רמות חומרה.

<img src="../../../translated_images/he/structured-analysis-demo.9ef892194cd23bc8.webp" alt="הדגמת ניתוח מובנה" width="800"/>

*סקירת קוד במסגרת מסודרת*

### שיחה רב-סיבובית

שאל "מה זה Spring Boot?" ואז מיד המשך ב"להראות לי דוגמה". הדגם זוכר את השאלה הראשונה ומספק דוגמה ספציפית ל-Spring Boot. בלי זיכרון, השאלה השנייה הייתה כללית מדי.

<img src="../../../translated_images/he/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="הדגמת שיחה רב-סיבובית" width="800"/>

*שימור הקשר בין שאלות*

### נימוק שלב אחר שלב

בחר בעיית מתמטיקה ונסה עם נימוק שלב אחר שלב והתלהבות נמוכה. התלהבות נמוכה נותנת רק את התשובה - מהירה אך לא מפורטת. נימוק שלב אחר שלב מראה כל חישוב והחלטה.

<img src="../../../translated_images/he/step-by-step-reasoning-demo.12139513356faecd.webp" alt="הדגמת נימוק שלב אחר שלב" width="800"/>

*בעיה מתמטית עם שלבים מפורשים*

### פלט מוגבל

כשצריך פורמטים או ספירת מילים מדויקים, תבנית זו מאלצת הקפדה מחמירה. נסה ליצור סיכום עם בדיוק 100 מילים בפורמט נקודות.

<img src="../../../translated_images/he/constrained-output-demo.567cc45b75da1633.webp" alt="הדגמת פלט מוגבל" width="800"/>

*סיכום למידת מכונה עם בקרה על פורמט*

## מה באמת תלמד

**מאמץ נימוק משנה הכול**

GPT-5.2 מאפשר לך לשלוט במאמץ החישובי דרך ההנחיות שלך. מאמץ נמוך נותן תגובה מהירה עם מינימום חקר. מאמץ גבוה נותן לדגם זמן לחשוב לעומק. אתה לומד להתאים מאמץ למורכבות המשימה - לא לבזבז זמן על שאלות פשוטות, אך גם לא למהר להחליט על מורכבות.

**מבנה מנחה התנהגות**

שמת לב לתגי ה-XML בהנחיות? הם לא לקישוט. דגמים עוקבים אחרי הוראות מובנות יותר מהוראות טקסט חופשי. כשצריך תהליכים רב-שלביים או לוגיקה מורכבת, המבנה עוזר לדגם לעקוב איפה הוא ומה הבא.

<img src="../../../translated_images/he/prompt-structure.a77763d63f4e2f89.webp" alt="מבנה הנחיה" width="800"/>

*אנטומיה של הנחיה מסודרת היטב עם חלקים ברורים וארגון בסגנון XML*

**איכות דרך הערכה עצמית**

תבניות עם התבוננות עצמית עובדות על ידי הבהרת קריטריוני האיכות. במקום לקוות שהדגם "יעשה את זה נכון", אתה מגדיר לו במדויק מה פירוש "נכון": לוגיקה תקינה, טיפול בשגיאות, ביצועים, אבטחה. הדגם יכול אז להעריך את הפלט ולשפרו. זה הופך את יצירת הקוד מתהליך לוטו לתהליך מובנה.

**הקשר הוא מוגבל**

שיחות רב-סיבוביות עובדות באמצעות הכללת היסטוריית ההודעות עם כל בקשה. אבל יש גבול - לכל דגם יש מגבלת טוקנים מקסימלית. ככל ששיחות מתארכות, תצטרך אסטרטגיות לשמור על הקשר רלוונטי בלי להגיע להגבלה הזו. מודול זה מראה לך איך הזיכרון עובד; אחר כך תלמד מתי לסכם, מתי לשכוח, ומתי לשלוף.

## השלבים הבאים

**מודול הבא:** [03-rag - RAG (הפקה מוגברת בשליפה)](../03-rag/README.md)

---

**ניווט:** [← קודם: מודול 01 - מבוא](../01-introduction/README.md) | [חזרה לעמוד הראשי](../README.md) | [הבא: מודול 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב ויתור**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדיוק, יש לקחת בחשבון כי תרגומים אוטומטיים עלולים לכלול שגיאות או אי-דיוקים. המסמך המקורי בשפת המקור שלו צריך להיחשב כסמכותי. למידע קריטי מומלץ להיעזר בתרגום מקצועי אנושי. אנו לא נושאים באחריות לכל אי-הבנות או פרשנויות שגויות הנובעות מהשימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->