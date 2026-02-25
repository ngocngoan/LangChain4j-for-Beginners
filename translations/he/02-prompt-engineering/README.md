# מודול 02: הנדסת פקודות עם GPT-5.2

## תוכן העניינים

- [מה תלמדו](../../../02-prompt-engineering)
- [דרישות מוקדמות](../../../02-prompt-engineering)
- [הבנת הנדסת פקודות](../../../02-prompt-engineering)
- [יסודות הנדסת פקודות](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [תבניות פקודות](../../../02-prompt-engineering)
- [תבניות מתקדמות](../../../02-prompt-engineering)
- [שימוש במשאבי Azure קיימים](../../../02-prompt-engineering)
- [צילום מסך של האפליקציה](../../../02-prompt-engineering)
- [חקירת התבניות](../../../02-prompt-engineering)
  - [רמה נמוכה לעומת רמה גבוהה של התלהבות](../../../02-prompt-engineering)
  - [ביצוע משימה (פתיחי כלי עבודה)](../../../02-prompt-engineering)
  - [קוד המשתקף בעצמו](../../../02-prompt-engineering)
  - [ניתוח מובנה](../../../02-prompt-engineering)
  - [שיחה רב-סיבובית](../../../02-prompt-engineering)
  - [חשיבה צעד אחר צעד](../../../02-prompt-engineering)
  - [פלט מוגבל](../../../02-prompt-engineering)
- [מה אתם באמת לומדים](../../../02-prompt-engineering)
- [השלבים הבאים](../../../02-prompt-engineering)

## מה תלמדו

<img src="../../../translated_images/he/what-youll-learn.c68269ac048503b2.webp" alt="מה תלמדו" width="800"/>

במודול קודם, ראיתם כיצד הזיכרון מאפשר שיחה עם בינה מלאכותית והשתמשתם במודלים של GitHub לאינטראקציות בסיסיות. כעת נתרכז באיך אתם שואלים שאלות — כלומר בפקודות עצמן — באמצעות GPT-5.2 של Azure OpenAI. האופן בו אתם מבנים את הפקודות משפיע בצורה דרמטית על איכות התגובות שתקבלו. נתחיל בסקירת טכניקות היסוד של הנדסת פקודות, ואז נעבור לשמונה תבניות מתקדמות שמשתמשות בכל יכולות GPT-5.2.

נשתמש ב-GPT-5.2 כי הוא מציג שליטה על החשיבה – תוכלו לומר למודל כמה לחשוב לפני התשובה. זה מדגיש אסטרטגיות פקודות שונות ועוזר להבין מתי להשתמש בכל גישה. כמו כן, נזכה גם למעט מגבלות קצב מצד Azure בהשוואה למודלים של GitHub.

## דרישות מוקדמות

- סיום מודול 01 (משאבי Azure OpenAI פרוסים)
- קובץ `.env` בתיקיית השורש עם אישורי Azure (נוצר על ידי `azd up` במודול 01)

> **הערה:** אם לא סיימתם את מודול 01, עקבו תחילה אחר הוראות הפריסה שם.

## הבנת הנדסת פקודות

<img src="../../../translated_images/he/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="מהי הנדסת פקודות?" width="800"/>

הנדסת פקודות היא תהליך של עיצוב טקסט קלט שמבטיח שתקבלו את התוצאות שאתם צריכים. זה לא רק לשאול שאלות – זה לארגן את הבקשות כך שהמודל יבין בדיוק מה אתם רוצים ואיך לספק זאת.

תחשבו על זה כמו לתת הוראות לעמית לעבודה. "תקן את הבאג" זה לא ברור. "תקן את החריגה של null pointer בקובץ UserService.java שורה 45 על ידי הוספת בדיקת null" זה מדויק. מודלים של שפה פועלים באותה צורה – דיוק ומבנה חשובים.

<img src="../../../translated_images/he/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="איך LangChain4j משתלב" width="800"/>

LangChain4j מספק את התשתית — חיבור למודלים, זיכרון וסוגי הודעות — בעוד שהתבניות של הפקודות הן טקסט בנוי בקפידה שנשלח דרך התשתית הזאת. אבני הבנייה המרכזיות הן `SystemMessage` (שקובע התנהגות ותפקיד לבינה המלאכותית) ו-`UserMessage` (שנושא את הבקשה שלכם בפועל).

## יסודות הנדסת פקודות

<img src="../../../translated_images/he/five-patterns-overview.160f35045ffd2a94.webp" alt="סקירת חמש תבניות הנדסת פקודות" width="800"/>

לפני שנכנס לתבניות המתקדמות במודול הזה, בואו נסקור חמש טכניקות הנדסת פקודות בסיסיות. אלה אבני הבניין שכל מהנדס פקודות צריך להכיר. אם כבר עבדתם דרך [מודול התחלה מהירה](../00-quick-start/README.md#2-prompt-patterns), כבר ראיתם את זה בפעולה — להלן המסגרת הקונספטואלית שמאחוריהם.

### Zero-Shot Prompting

הגישה הפשוטה ביותר: תנו למודל הוראה ישירה ללא דוגמאות. המודל נשען כולה על הלימוד שלו כדי להבין ולבצע את המשימה. זה מתאים היטב לבקשות פשוטות שבהן ההתנהגות המצופה ברורה.

<img src="../../../translated_images/he/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*הוראה ישירה ללא דוגמאות — המודל מסיק את המשימה מההוראה בלבד*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// תגובה: "חיובית"
```

**מתי להשתמש:** סיווגים פשוטים, שאלות ישירות, תרגומים, או כל משימה שהמודל יכול לטפל בה ללא הדרכה נוספת.

### Few-Shot Prompting

ספקו דוגמאות שמדגימות את התבנית שברצונכם שהמודל יעקוב אחריה. המודל לומד את פורמט הקלט-פלט המצופה מהדוגמאות שלכם ומיישם אותו על קלטים חדשים. זה משפר משמעותית עקביות במשימות שבהן הפורמט או ההתנהגות הרצויה אינם ברורים.

<img src="../../../translated_images/he/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

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

**מתי להשתמש:** סיווגים מותאמים, עיצוב עקבי, משימות תחומי ידע ספציפיים, או כאשר תוצאות ה-zero-shot אינן עקביות.

### Chain of Thought

בקשו מהמודל להראות את החשיבה שלו צעד אחר צעד. במקום לעבור ישר לתשובה, המודל מפרק את הבעיה ועובד כל חלק במפורש. זה משפר דיוק במשימות מתמטיות, לוגיות ובחשיבה רב-שלבית.

<img src="../../../translated_images/he/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*חשיבה צעד אחר צעד — פירוק בעיות מורכבות לשלבים לוגיים מפורשים*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// המודל מראה: 15 - 8 = 7, ואז 7 + 12 = 19 תפוחים
```

**מתי להשתמש:** בעיות מתמטיות, חידות לוגיות, איתור באגים, או כל משימה שבה הצגת תהליך החשיבה משפרת דיוק ואמון.

### Role-Based Prompting

קבעו פרסונה או תפקיד לבינה המלאכותית לפני שאתם שואלים את שאלתכם. זה מספק הקשר שעיצוב את הטון, העומק והמיקוד של התשובה. "ארכיטקט תוכנה" נותן עצות שונות מ-"מפתח צעיר" או מ-"מבקר אבטחה".

<img src="../../../translated_images/he/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*הגדרת הקשר ופרסונה — לאותה שאלה מקבלים תגובה שונה בהתאם לתפקיד המיוחס*

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

**מתי להשתמש:** סקירות קוד, הדרכה, ניתוח תחומי ידע ספציפיים, או כאשר צריך תגובות המותאמות לרמת מומחיות או נקודת מבט מסוימת.

### תבניות פקודות

צרו פקודות שימושיות חוזרות עם משתנים. במקום לכתוב פקודה חדשה בכל פעם, הגדירו פעם אחת תבנית ומלאו ערכים שונים. מחלקת `PromptTemplate` של LangChain4j עושה את זה פשוט עם תחביר `{{variable}}`.

<img src="../../../translated_images/he/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*פקודות שימוש חוזר עם משתני מציין מקום — תבנית אחת, שימושים רבים*

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

**מתי להשתמש:** שאילתות חוזרות עם קלטים שונים, עיבוד באצוות, בניית זרמי עבודה של AI שניתן להשתמש בהם שוב, או כל תרחיש שבו מבנה הפקודה נשאר זהה אבל הנתונים משתנים.

---

חמשת היסודות הללו מעניקים לכם ערכה יציבה לרוב המשימות של הנדסת פקודות. שאר המודול בונה עליהם עם **שמונה תבניות מתקדמות** המשתמשות בשליטה על החשיבה של GPT-5.2, בהערכות עצמית ובאפשרויות פלט מובנה.

## תבניות מתקדמות

לאחר שסקרנו את היסודות, נעבור לשמונה התבניות המתקדמות שעושות את המודול לייחודי. לא כל בעיה דורשת את אותה גישה. יש שאלות שצריכות תשובות מהירות, אחרות חשיבה מעמיקה. יש שדורשות חשיבה גלויה, אחרות רק תוצאות. כל תבנית מטה מותאמת לתרחיש שונה — ושליטה על החשיבה של GPT-5.2 מדגישה את ההבדלים אפילו יותר.

<img src="../../../translated_images/he/eight-patterns.fa1ebfdf16f71e9a.webp" alt="שמונה תבניות הנדסת פקודות" width="800"/>

*סקירה של שמונת תבניות הנדסת הפקודות ותחומי השימוש שלהן*

<img src="../../../translated_images/he/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="שליטה על חשיבה ב-GPT-5.2" width="800"/>

*שליטה על החשיבה של GPT-5.2 מאפשרת לציין כמה לחשוב שהמודל יעשה — מתשובות מהירות וממוקדות ועד חקירה מעמיקה*

**רמת התלהבות נמוכה (מהיר וממוקד)** - לשאלות פשוטות שבהן רוצים תשובות מהירות וישירות. המודל עושה חשיבה מינימלית – מקסימום 2 שלבים. השתמשו בזה לחישובים, חיפושים או שאלות ברורות.

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

> 💡 **חקור עם GitHub Copilot:** פתח [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ושאל:
> - "מה ההבדל בין תבניות פקודות ברמת התלהבות נמוכה לגבוהה?"
> - "איך תגי XML בפקודות עוזרים לבנות את תגובת ה-AI?"
> - "מתי להשתמש בתבניות התבוננות עצמית לעומת הוראה ישירה?"

**רמת התלהבות גבוהה (עמוק ויסודי)** - לבעיות מורכבות שבהן רוצים ניתוח מקיף. המודל חוקר לעומק ומראה חשיבה מפורטת. השתמשו בזה לעיצוב מערכות, החלטות ארכיטקטוניות או מחקר מורכב.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**ביצוע משימה (התקדמות צעד-אחר-צעד)** - לתהליכים רב-שלביים. המודל מספק תוכנית מראש, מתאר כל שלב בזמן העבודה, ואז נותן סיכום. השתמשו בזה להגרות, יישומים או כל תהליך רב-שלבי.

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

שיטה של Chain-of-Thought מבקשת במפורש שהמודל יציג את תהליך החשיבה שלו, ומשפרת דיוק במשימות מורכבות. הפירוק צעד-אחר-צעד מסייע גם לבני אדם וגם ל-AI להבין את הלוגיקה.

> **🤖 נסו עם [GitHub Copilot](https://github.com/features/copilot) Chat:** שאלו על התבנית הזאת:
> - "איך הייתי מתאים את תבנית ביצוע המשימה לפעולות ארוכות?"
> - "מהם השיטות הטובות ביותר לבניית פתיחים לכלים באפליקציות ייצור?"
> - "איך אוכל ללכוד ולהציג עדכוני התקדמות בינוניים בממשק משתמש?"

<img src="../../../translated_images/he/task-execution-pattern.9da3967750ab5c1e.webp" alt="תבנית ביצוע משימה" width="800"/>

*תכנון → ביצוע → סיכום של זרימת עבודה למשימות רב-שלביות*

**קוד המשתקף בעצמו** - לייצור קוד באיכות ייצור. המודל מייצר קוד לפי סטנדרטים עם טיפול שגיאות נכון. השתמשו בזה כשאתם בונים תכונות או שירותים חדשים.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/he/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="מחזור התבוננות עצמית" width="800"/>

*לולאת שיפור איטרטיבית – ייצור, הערכה, זיהוי בעיות, שיפור, חזרה*

**ניתוח מובנה** - להערכה עקבית. המודל סוקר קוד באמצעות מסגרת קבועה (נכונות, שיטות, ביצועים, אבטחה, תחזוקה). השתמשו בזה לסקירות קוד או הערכות איכות.

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

> **🤖 נסו עם [GitHub Copilot](https://github.com/features/copilot) Chat:** שאלו על ניתוח מובנה:
> - "איך להתאים את מסגרת הניתוח לסוגים שונים של סקירות קוד?"
> - "מה הדרך הטובה ביותר לנתח ולפעול על פלט מובנה באופן תכנותי?"
> - "איך לדאוג לרמות חומרה עקביות בין סשנים של סקירה שונים?"

<img src="../../../translated_images/he/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="תבנית ניתוח מובנה" width="800"/>

*מסגרת לסקירות קוד עקביות עם רמות חומרה*

**שיחה רב-סיבובית** - לשיחות שדורשות הקשר. המודל זוכר הודעות קודמות ובונה עליהן. השתמשו בזה למפגשי סיוע אינטראקטיביים או לשאלות ותשובות מורכבות.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/he/context-memory.dff30ad9fa78832a.webp" alt="זיכרון הקשר שיחה" width="800"/>

*כיצד ההקשר של השיחה מצטבר על פני סיבובים מרובים עד הגבלת הטוקנים*

**חשיבה צעד אחר צעד** - לבעיות שדורשות לוגיקה גלויה. המודל מראה חשיבה מפורשת לכל שלב. השתמשו בזה לבעיות מתמטיות, חידות לוגיות, או כאשר רוצים להבין את תהליך החשיבה.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/he/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="תבנית צעד אחר צעד" width="800"/>

*פירוק בעיות לשלבים לוגיים מפורשים*

**פלט מוגבל** - לתשובות עם דרישות פורמט ספציפיות. המודל מקפיד על כללי פורמט ואורך. השתמשו בזה לסיכומים או כאשר אתם זקוקים למבנה פלט מדויק.

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

**אימות פריסה:**

וודאו שקובץ `.env` קיים בתיקיית השורש עם אישורי Azure (נוצר במהלך מודול 01):
```bash
cat ../.env  # צריך להציג את AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**הפעל את האפליקציה:**

> **הערה:** אם כבר הפעלתם את כל האפליקציות באמצעות `./start-all.sh` ממודול 01, המודול כבר רץ על פורט 8083. ניתן לדלג על פקודות ההפעלה ולהיכנס ישירות לכתובת http://localhost:8083.

**אפשרות 1: שימוש ב-Spring Boot Dashboard (מומלץ למשתמשי VS Code)**

מיכל הפיתוח כולל את תוסף Spring Boot Dashboard, המספק ממשק חזותי לניהול כל אפליקציות ה-Spring Boot. ניתן למצוא אותו בסרגל הפעילות בצד שמאל של VS Code (חפשו את אייקון Spring Boot).

מה-Dashboard של Spring Boot תוכלו:
- לראות את כל אפליקציות Spring Boot הזמינות בעורך
- להפעיל/לכבות אפליקציות בלחיצה אחת
- לצפות בלוגים של האפליקציה בזמן אמת
- לנטר את מצב האפליקציה
פשוט לחצו על כפתור ההפעלה לצד "prompt-engineering" כדי להתחיל את המודול הזה, או התחילו את כל המודולים בבת אחת.

<img src="../../../translated_images/he/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**אפשרות 2: שימוש בסקריפטי Shell**

הפעלת כל יישומי האינטרנט (מודולים 01-04):

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

שני הסקריפטים טוענים אוטומטית משתני סביבה מהקובץ `.env` שבשורש ויבנו את קבצי ה-JAR אם הם אינם קיימים.

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

**כדי לעצור:**

**Bash:**
```bash
./stop.sh  # רק את המודול הזה
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

<img src="../../../translated_images/he/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*לוח הבקרה הראשי המציג את כל 8 דפוסי ההנחיה עם התכונות ושימושיהם*

## חקר הדפוסים

הממשק מאפשר לכם להתנסות באסטרטגיות הנחיה שונות. כל דפוס פותר בעיות שונות - נסו אותם כדי לראות מתי כל גישה באה לידי ביטוי.

> **הערה: שידור מול ללא שידור** — בכל דף דפוס ישנם שני כפתורים: **🔴 תגובה בשידור (Live)** ואפשרות **ללא שידור**. השידור משתמש ב-Server-Sent Events (SSE) להצגת טוקנים בזמן אמת כאשר המודל מייצר אותם, וכך אתם רואים את ההתקדמות מיד. האפשרות ללא שידור מחכה לקבל את כל התגובה לפני שמציגה אותה. עבור הנחיות שדורשות חשיבה מעמיקה (כגון High Eagerness, Self-Reflecting Code), הקריאה ללא שידור יכולה לקחת זמן רב — לפעמים דקות — ללא משוב נראה לעין. **השתמשו בשידור בעת ניסוי עם הנחיות מורכבות** כדי שתוכלו לראות את המודל עובד ולהימנע מהתרשמות שהבקשה נכשלה או התקיימה.

> **הערה: דרישת דפדפן** — תכונת השידור משתמשת ב-Fetch Streams API (`response.body.getReader()`) שדורשת דפדפן מלא (Chrome, Edge, Firefox, Safari). היא **לא** פועלת בדפדפן הפשוט המובנה ב-VS Code, מאחר ואין לו תמיכה ב-ReadableStream API. אם אתם משתמשים בדפדפן הפשוט, כפתורי הלא-שידור עדיין יעבדו כרגיל — רק כפתורי השידור מושפעים. פתחו את `http://localhost:8083` בדפדפן חיצוני לחוויה המלאה.

### נמוכה מול גבוהה בהתלהבות

שאלו שאלה פשוטה כמו "כמה זה 15% מ-200?" עם Low Eagerness. תקבלו תשובה מיידית וישירה. עכשיו שאלו משהו מורכב כמו "עצב אסטרטגיית מטמון ל-API בעומס גבוה" עם High Eagerness. לחצו על **🔴 תגובה בשידור (Live)** וצפו בהופעת הנימוקים המפורטים של המודל טוקן אחר טוקן. אותו מודל, אותו מבנה שאלה - אבל הפקודה אומרת לו כמה לחשוב.

### ביצוע משימות (פתיחי כלים)

זרימות עבודה רב-שלביות מרוויחות מתכנון מוקדם ופרויקט התקדמות. המודל מפרט מה יעשה, מספר על כל שלב, ואז מסכם את התוצאות.

### קוד עם חשיבה עצמית

נסו "צור שירות לאימות מייל". במקום רק לכתוב קוד ולהפסיק, המודל יוצר, מעריך לפי קריטריוני איכות, מזהה חולשות ומשפר. תוכלו לראות אותו חוזר על הפעולה עד שהקוד עומד בסטנדרטים לייצור.

### ניתוח מובנה

ביקורות קוד דורשות מסגרות הערכה עקביות. המודל מנתח קוד לפי קטגוריות קבועות (נכונות, נהגים, ביצועים, אבטחה) עם דרגות חומרה.

### שיחה רב-סיבובית

שאלו "מה זה Spring Boot?" ואז מייד שאלו "הראה דוגמה". המודל זוכר את השאלה הראשונה ונותן דוגמה ספציפית ל-Spring Boot. בלי זיכרון, השאלה השנייה תהיה כללית מדי.

### נימוק צעד-אחר-צעד

בחרו בעיה מתמטית ונסו אותה גם עם נימוק צעד-אחר-צעד וגם עם Low Eagerness. Low Eagerness נותן רק את התשובה - מהיר אך לא שקוף. צעד-אחר-צעד מראה כל חישוב והחלטה.

### פלט מגביל

כשאתם צריכים פורמטים מדויקים או מספר מילים מוגבל, דפוס זה יכפה הקפדה על כך. נסו לייצר סיכום בדיוק של 100 מילים בפורמט נקודות.

## מה אתם באמת לומדים

**מאמץ נימוק משנה הכול**

GPT-5.2 מאפשר לכם לשלוט במאמץ החישובי דרך ההנחיות שלכם. מאמץ נמוך משמע תגובות מהירות עם חיפושים מינימליים. מאמץ גבוה אומר שהמודל מקדיש זמן לחשיבה עמוקה. אתם לומדים להתאים את המאמץ למורכבות המשימה - לא לבזבז זמן על שאלות פשוטות, ולוודא לא למהר בהחלטות מורכבות.

**מבנה מכתיב את ההתנהגות**

שמעתם על תגיות XML בהנחיות? הן לא סתם לקישוט. דגמי שפה עוקבים אחרי הוראות מבניות באופן אמין יותר מטקסט חופשי. כשצריך תהליכים רב-שלביים או לוגיקה מורכבת, המבנה עוזר למודל להתמצא איפה הוא ומה שלב הבא.

<img src="../../../translated_images/he/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*אנטומיה של הנחיה עם מבנה ברור וחלוקה בסגנון XML*

**איכות דרך הערכה עצמית**

דפוסי החשיבה העצמית פועלים על ידי הבהרת קריטריוני האיכות. במקום לקוות שהמודל "יעשה נכון", אתם אומרים לו בדיוק מה זה "נכון": לוגיקה מדויקת, טיפול בשגיאות, ביצועים, אבטחה. המודל יכול אז להעריך את הפלט ולשפר אותו. זה הופך את יצירת הקוד מתהליך של הגרלה לתהליך מובנה.

**הקשר הוא סופי**

שיחות רב-סיבוביות עובדות על ידי הכללת היסטוריית ההודעות בכל בקשה. אבל יש גבול - לכל מודל יש מספר טוקנים מקסימלי. ככל שהשיחות מתארכות, תצטרכו אסטרטגיות לשמר רק את ההקשר הרלוונטי מבלי להגיע לתקרה. מודול זה יראה לכם איך עובד הזיכרון; בהמשך תלמדו מתי לסכם, מתי לשכוח ומתי לשלוף.

## צעדים הבאים

**מודול הבא:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**ניווט:** [← קודם: מודול 01 - מבוא](../01-introduction/README.md) | [חזרה לעיקרי](../README.md) | [הבא: מודול 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב הפטור מאחריות**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). אף שאנו שואפים לדיוק, יש לקחת בחשבון כי תרגומים אוטומטיים עשויים להכיל שגיאות או אי-דיוקים. המסמך המקורי בשפת המקור נחשב למקור הסמכותי. עבור מידע קריטי, מומלץ להיעזר בתרגום מקצועי אנושי. איננו נושאים באחריות עבור כל אי-הבנה או פרשנות שגויה הנובעים משימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->