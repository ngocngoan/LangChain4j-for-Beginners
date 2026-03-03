# מודול 02: הנדסת פרומפטים עם GPT-5.2

## תוכן העניינים

- [הדרכה בוידאו](../../../02-prompt-engineering)
- [מה תלמד](../../../02-prompt-engineering)
- [דרישות מקדימות](../../../02-prompt-engineering)
- [הבנת הנדסת פרומפטים](../../../02-prompt-engineering)
- [יסודות הנדסת פרומפטים](../../../02-prompt-engineering)
  - [פרומפטים ללא דוגמאות (Zero-Shot)](../../../02-prompt-engineering)
  - [פרומפטים עם מספר דוגמאות (Few-Shot)](../../../02-prompt-engineering)
  - [שרשרת חשיבה](../../../02-prompt-engineering)
  - [פרומפטים מבוססי תפקיד](../../../02-prompt-engineering)
  - [תבניות פרומפטים](../../../02-prompt-engineering)
- [תבניות מתקדמות](../../../02-prompt-engineering)
- [הרצת האפליקציה](../../../02-prompt-engineering)
- [צילום מסך של האפליקציה](../../../02-prompt-engineering)
- [חקירת התבניות](../../../02-prompt-engineering)
  - [רצון נמוך מול גבוה](../../../02-prompt-engineering)
  - [ביצוע משימות (הקדמות לכלים)](../../../02-prompt-engineering)
  - [קוד תוך-השקפה עצמית](../../../02-prompt-engineering)
  - [ניתוח מובנה](../../../02-prompt-engineering)
  - [שיחות מרובות סבבים](../../../02-prompt-engineering)
  - [הסקת מסקנות שלב אחר שלב](../../../02-prompt-engineering)
  - [תפוקת פלט מוגבלת](../../../02-prompt-engineering)
- [מה אתה באמת לומד](../../../02-prompt-engineering)
- [השלבים הבאים](../../../02-prompt-engineering)

## הדרכה בוידאו

צפה במפגש חי זה המבהיר כיצד להתחיל עם המודול:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## מה תלמד

הדיאגרמה הבאה מספקת סקירה של הנושאים המרכזיים והכישורים שתפתח במודול זה — מטכניקות שיפור הפרומפט ועד תהליך העבודה שלב אחר שלב שתעקוב אחריו.

<img src="../../../translated_images/he/what-youll-learn.c68269ac048503b2.webp" alt="מה תלמד" width="800"/>

במודולים הקודמים חקרת אינטראקציות בסיסיות עם LangChain4j ו-GitHub Models וראית כיצד הזיכרון מאפשר AI שיחתי עם Azure OpenAI. כעת נתמקד כיצד אתה שואל שאלות — הפרומפטים עצמם — באמצעות GPT-5.2 של Azure OpenAI. האופן בו אתה מבנה את הפרומפטים שלך משפיע באופן דרמטי על איכות התגובות. נתחיל בסקירה של טכניקות הפרומפט הבסיסיות, ואז נעבור לשמונה תבניות מתקדמות שמנצלות באופן מלא את היכולות של GPT-5.2.

נשתמש ב-GPT-5.2 משום שהוא מציג שליטה בחשיבה - אתה יכול להגיד למודל כמה לחשוב לפני שהוא עונה. זה מדגיש אסטרטגיות פרומפט שונות ועוזר לך להבין מתי להשתמש בכל גישה. כמו כן, ניהנה מהמגבלות הקצב הטובות יותר של Azure ל-GPT-5.2 בהשוואה ל-GitHub Models.

## דרישות מקדימות

- השלמת מודול 01 (משאבי Azure OpenAI מוגדרים)
- קובץ `.env` בספרייה הראשית עם אישורי Azure (נוצר על ידי `azd up` במודול 01)

> **הערה:** אם לא השלמת את מודול 01, בצע תחילה את הוראות ההטמעה שם.

## הבנת הנדסת פרומפטים

בסיס הנדסת הפרומפטים הוא ההבדל בין הוראות מעורפלות לבין מדויקות, כפי שמדגים ההשוואה למטה.

<img src="../../../translated_images/he/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="מהי הנדסת פרומפטים?" width="800"/>

הנדסת פרומפטים עוסקת בתכנון טקסט קלט שמקבל עמודות תוצאות בתצורה עקבית. זה לא רק לשאול שאלות – זה לבנות בקשות כך שהמודל יבין בדיוק מה אתה רוצה וכיצד להעניק זאת.

תחשוב על זה כמו מתן הוראות לעמית לעבודה. "תקן את הבאג" היא הוראה מעורפלת. "תקן את החריגה null pointer ב-UserService.java שורה 45 על ידי הוספת בדיקת null" היא מדויקת. מודלי שפה פועלים באותו אופן - ספציפיות ומבנה הם חשובים.

הדיאגרמה הבאה מציגה כיצד LangChain4j משתלב בתמונה — מחבר את דפוסי הפרומפט שלך למודל דרך רכיבי הבנייה SystemMessage ו-UserMessage.

<img src="../../../translated_images/he/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="כיצד LangChain4j משתלב" width="800"/>

LangChain4j מספק את התשתית — חיבורים למודלים, זיכרון, וסוגי הודעות — בעוד שדפוסי הפרומפט הם פשוט טקסט מובנה בקפידה שאתה שולח דרך התשתית הזו. אבני הבניין המרכזיות הן `SystemMessage` (שקובעת את התנהגות ותפקיד ה-AI) ו-`UserMessage` (שנושא את הבקשה הממשית שלך).

## יסודות הנדסת פרומפטים

חמש טכניקות הליבה המוצגות למטה מהוות את הבסיס להנדסת פרומפטים יעילה. כל אחת מתמקדת בהיבט אחר של האופן בו אתה מתקשר עם מודלי שפה.

<img src="../../../translated_images/he/five-patterns-overview.160f35045ffd2a94.webp" alt="סקירת חמשת דפוסי הנדסת פרומפטים" width="800"/>

לפני שנצלול לתבניות המתקדמות במודול זה, נעבור על חמש טכניקות פרומפט בסיסיות. אלו אבני הבניין שכל מהנדס פרומפט צריך להכיר. אם כבר עבדת עם [מודול ההתחלה המהירה](../00-quick-start/README.md#2-prompt-patterns), ראית אותן בפועל — הנה המסגרת התיאורטית שמאחוריהן.

### פרומפט ללא דוגמאות (Zero-Shot)

הגישה הפשוטה ביותר: תן למודל הוראה ישירה ללא דוגמאות. המודל מתבסס לחלוטין על האימון שלו כדי להבין ולבצע את המשימה. זה עובד היטב עבור בקשות פשוטות שבהן ההתנהגות המצופה ברורה.

<img src="../../../translated_images/he/zero-shot-prompting.7abc24228be84e6c.webp" alt="פרומפט ללא דוגמאות" width="800"/>

*הוראה ישירה ללא דוגמאות — המודל מסיק את המשימה מההוראה בלבד*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// תגובה: "חיובי"
```

**מתי להשתמש:** סיווגים פשוטים, שאלות ישירות, תרגומים, או כל משימה שהמודל יכול לטפל בה ללא הנחיות נוספות.

### פרומפט עם מספר דוגמאות (Few-Shot)

ספק דוגמאות המדגימות את הדפוס שברצונך שהמודל יעקוב אחריו. המודל לומד את פורמט הקלט-פלט המצופה מהדוגמאות שלך ומיישם אותו על קלטים חדשים. זה משפר באופן דרמטי את העקביות במשימות שבהן הפורמט או ההתנהגות הרצויים אינם מובנים מאליהם.

<img src="../../../translated_images/he/few-shot-prompting.9d9eace1da88989a.webp" alt="פרומפט עם מספר דוגמאות" width="800"/>

*לומד מדוגמאות — המודל מזהה את הדפוס ומחיל אותו על קלטים חדשים*

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

**מתי להשתמש:** סיווגים מותאמים אישית, עיצוב עקבי, משימות ספציפיות לתחום, או כאשר תוצאות ה-zero-shot אינן עקביות.

### שרשרת חשיבה (Chain of Thought)

בקש מהמודל להראות את ההיגיון שלו שלב אחר שלב. במקום לקפוץ ישר לתשובה, המודל מפצל את הבעיה ועובד על כל חלק במפורש. זה משפר את הדיוק בבעיות מתמטיות, לוגיות, ובהסקת מסקנות מרובה שלבים.

<img src="../../../translated_images/he/chain-of-thought.5cff6630e2657e2a.webp" alt="פרומפט שרשרת חשיבה" width="800"/>

*היגיון שלב אחר שלב — מפצל בעיות מורכבות לשלבים לוגיים מפורשים*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// המודל מראה: 15 - 8 = 7, ואז 7 + 12 = 19 תפוחים
```

**מתי להשתמש:** בעיות מתמטיות, חידות לוגיות, איתור שגיאות, או כל משימה שבה הצגת תהליך ההיגיון משפרת דיוק ואמון.

### פרומפטים מבוססי תפקיד (Role-Based Prompting)

הגדר אישיות או תפקיד ל-AI לפני ששואלים את שאלתך. זה מספק הקשר שמעצב את הטון, העומק, והפוקוס של התגובה. "ארכיטקט תוכנה" נותן עצות שונות מ"מתכנת צעיר" או "בודק אבטחה".

<img src="../../../translated_images/he/role-based-prompting.a806e1a73de6e3a4.webp" alt="פרומפט מבוסס תפקיד" width="800"/>

*קביעת הקשר ואישיות — אותה שאלה מקבלת תגובה שונה בהתאם לתפקיד שיוחס*

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

**מתי להשתמש:** סקירות קוד, הדרכה, ניתוח תחום ספציפי, או כשאתה צריך תגובות המותאמות לרמת מומחיות או נקודת מבט מסוימת.

### תבניות פרומפטים (Prompt Templates)

צור פרומפטים לשימוש חוזר עם משתנים למילוי. במקום לכתוב פרומפט חדש בכל פעם, הגדר תבנית אחת ומלא בה ערכים שונים. מחלקת `PromptTemplate` של LangChain4j מקלה על כך בשימוש בתחביר `{{variable}}`.

<img src="../../../translated_images/he/prompt-templates.14bfc37d45f1a933.webp" alt="תבניות פרומפטים" width="800"/>

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

**מתי להשתמש:** שאילתות חוזרות עם קלטים שונים, עיבוד באצוות, בניית תהליכי עבודה של AI לשימוש חוזר, או בכל מצב שבו מבנה הפרומפט נשאר זהה אך הנתונים משתנים.

---

חמשת היסודות הללו מעניקים לך ערכת כלים איתנה לרוב משימות הפרומפט. שאר המודול ממשיך איתם עם **שמונה תבניות מתקדמות** המנצלות את בקרת החשיבה, ההערכה העצמית, ויכולות הפלט המובנה של GPT-5.2.

## תבניות מתקדמות

לאחר כיסוי היסודות, נמשיך לשמונה תבניות מתקדמות שמייחדות מודול זה. לא כל בעיה זקוקה לאותה גישה. חלק מהשאלות דורשות תשובות מהירות, אחרות דורשות חשיבה מעמיקה. חלק דורשות היגיון גלוי, אחרות רק תוצאות. כל תבנית למטה מותאמת לסיטואציה שונה — ובקרת החשיבה של GPT-5.2 מדגישה את ההבדלים עוד יותר.

<img src="../../../translated_images/he/eight-patterns.fa1ebfdf16f71e9a.webp" alt="שמונה דפוסי הנדסת פרומפטים" width="800"/>

*סקירה של שמונת דפוסי הנדסת הפרומפט והשימושים שלהם*

GPT-5.2 מוסיף מימד חדש לתבניות האלה: *בקרת חשיבה*. המחוון למטה מראה כיצד ניתן לכוון את מאמץ החשיבה של המודל — מתשובות מהירות וישירות ועד ניתוח עמוק ויסודי.

<img src="../../../translated_images/he/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="בקרת חשיבה עם GPT-5.2" width="800"/>

*בקרת החשיבה של GPT-5.2 מאפשרת לך לציין כמה לחשוב המודל צריך — מתשובות מהירות ועד לחקירה מעמיקה*

**רצון נמוך (מהיר וממוקד)** - לשאלות פשוטות שבהן רוצים תשובות מהירות וישירות. המודל מבצע מינימום חשיבה - מקסימום 2 שלבים. השתמש בזה לחישובים, חיפושים, או שאלות פשוטות.

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
> - "מה ההבדל בין דפוסי פרומפט של רצון נמוך לעומת גבוה?"
> - "כיצד תגיות XML בפרומפטים עוזרות במבנה תגובת ה-AI?"
> - "מתי יש להשתמש בתבניות השיקוף העצמי מול הוראות ישירות?"

**רצון גבוה (עמוק ויסודי)** - לבעיות מורכבות שבהן רוצים ניתוח מעמיק. המודל חוקר באופן יסודי ומציג היגיון מפורט. השתמש בזה לתכנון מערכות, החלטות ארכיטקטורה, או מחקר מורכב.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**ביצוע משימה (התקדמות שלב אחר שלב)** - עבור תהליכים מרובי שלבים. המודל מספק תוכנית מראש, מתאר כל שלב בזמן העבודה, ואז נותן סיכום. השתמש בזה להעברות, יישומים, או כל תהליך מרובה שלבים.

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

פרומפט שרשרת-חשיבה מבקש במפורש מהמודל להראות את תהליך ההיגיון שלו, ומשפר דיוק במשימות מורכבות. הפירוט שלב אחר שלב מסייע גם לבני אדם וגם ל-AI להבין את הלוגיקה.

> **🤖 נסה עם צ'אט של [GitHub Copilot](https://github.com/features/copilot):** שאל על התבנית הזו:
> - "כיצד אתאים את דפוס ביצוע המשימה לפעולות ארוכות-טווח?"
> - "מהם ההרגלים הטובים ביותר למבני הקדמות לכלים באפליקציות פרודקשן?"
> - "כיצד אוכל לתפוס ולהציג עדכוני התקדמות ביניים בממשק משתמש?"

הדיאגרמה הבאה ממחישה את תהליך העבודה תכנון → ביצוע → סיכום.

<img src="../../../translated_images/he/task-execution-pattern.9da3967750ab5c1e.webp" alt="דפוס ביצוע משימה" width="800"/>

*תהליך תכנון → ביצוע → סיכום למשימות מרובות שלבים*

**קוד תוך-השקפה עצמית** - ליצירת קוד איכותי לפרודקשן. המודל מייצר קוד בהתאם לתקני פרודקשן עם טיפול נאות בשגיאות. השתמש בזה בעת בניית תכונות או שירותים חדשים.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

הדיאגרמה הבאה מראה את הלולאת השיפור החוזרת הזו — ייצור, הערכה, זיהוי חולשות, ושיפור עד שהקוד עומד בתקן הפרודקשן.

<img src="../../../translated_images/he/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="לולאת השיקוף העצמי" width="800"/>

*לולאת שיפור חוזרת - יצירה, הערכה, זיהוי בעיות, שיפור, חזרה*

**ניתוח מובנה** - להערכה עקבית. המודל בודק קוד תוך שימוש במסגרת קבועה (נכונות, נהלים, ביצועים, אבטחה, תחזוקה). השתמש בזה לסקירות קוד או הערכות איכות.

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

> **🤖 נסה עם צ'אט של [GitHub Copilot](https://github.com/features/copilot):** שאל על ניתוח מובנה:
> - "כיצד אוכל להתאים את מסגרת הניתוח לסוגים שונים של סקירות קוד?"
> - "מה הדרך הטובה ביותר לפרש ולהשתמש בפלט מובנה באופן תכנותי?"
> - "כיצד להבטיח רמות חומרה עקביות על פני סבבי סקירה שונים?"

הדיאגרמה הבאה מראה כיצד המסגרת המובנית מארגנת סקירת קוד לקטגוריות עקביות עם רמות חומרה.

<img src="../../../translated_images/he/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="דפוס ניתוח מובנה" width="800"/>

*מסגרת לסקירות קוד עקביות עם רמות חומרה*

**שיחות מרובות סבבים** - לשיחות שדורשות הקשר. המודל זוכר הודעות קודמות ובונה על פיהן. השתמש בזה למפגשי עזרה אינטראקטיביים או שאלות ותשובות מורכבות.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

הדיאגרמה הבאה ממחישה כיצד מצטבר הקשר השיח עם כל סבב וכיצד הוא משפיע על מגבלת הטוקנים של המודל.

<img src="../../../translated_images/he/context-memory.dff30ad9fa78832a.webp" alt="זיכרון הקשר" width="800"/>

*כיצד מצטבר הקשר השיח על פני מספר סבבים עד להגעה למגבלת הטוקנים*
**הסבר צעד-אחר-צעד** - עבור בעיות הדורשות לוגיקה גלויה. המודל מציג הסבר מפורש עבור כל שלב. השתמש בזה עבור בעיות מתמטיות, חידות לוגיות, או כאשר אתה צריך להבין את תהליך החשיבה.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

הדיאגרמה למטה ממחישה כיצד המודל מפרק בעיות לשלבים לוגיים מפורשים וממוספרים.

<img src="../../../translated_images/he/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="תבנית צעד-אחר-צעד" width="800"/>

*פירוק בעיות לשלבים לוגיים מפורשים*

**פלט מוגבל** - עבור תגובות עם דרישות פורמט ספציפיות. המודל עומד בקפדנות בחוקי הפורמט והאורך. השתמש בזה עבור סיכומים או כאשר אתה צריך מבנה פלט מדויק.

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

הדיאגרמה הבאה מראה כיצד מגבלות מנחות את המודל לייצר פלט המתאים בקפדנות לדרישות הפורמט והאורך שלך.

<img src="../../../translated_images/he/constrained-output-pattern.0ce39a682a6795c2.webp" alt="תבנית פלט מוגבל" width="800"/>

*אכיפת דרישות פורמט, אורך ומבנה ספציפיות*

## הפעלת היישום

**אימות פריסה:**

ודא שקובץ ה-`.env` קיים בתיקיית השורש עם האישורים של Azure (נוצר במהלך מודול 01). הפעל זאת מתיקיית המודול (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # צריך להציג את AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # צריך להציג את AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**הפעל את היישום:**

> **הערה:** אם כבר הפעלת את כל היישומים באמצעות `./start-all.sh` מתיקיית השורש (כמתואר במודול 01), מודול זה כבר רץ על פורט 8083. ניתן לדלג על פקודות ההפעלה למטה ולעבור ישירות ל-http://localhost:8083.

**אפשרות 1: שימוש בלוח ניהול Spring Boot (מומלץ למשתמשי VS Code)**

מיכל הפיתוח כולל את התוסף Spring Boot Dashboard, המספק ממשק ויזואלי לניהול כל יישומי Spring Boot. ניתן למצוא אותו בסרגל הפעילות בצד השמאלי של VS Code (חפש את סמל Spring Boot).

ממול לוח ניהול Spring Boot, ניתן:
- לראות את כל יישומי Spring Boot הזמינים בסביבת העבודה
- להפעיל/להפסיק יישומים בלחיצה אחת
- להציג לוגים של היישום בזמן אמת
- לנטר את מצב היישום

פשוט לחץ על כפתור ההפעלה ליד "prompt-engineering" כדי להפעיל את המודול, או הפעל את כל המודולים ביחד.

<img src="../../../translated_images/he/dashboard.da2c2130c904aaf0.webp" alt="לוח ניהול Spring Boot" width="400"/>

*לוח ניהול Spring Boot ב-VS Code — הפעלה, הפסקה ומעקב אחר כל המודולים ממקום אחד*

**אפשרות 2: שימוש בסקריפטים של shell**

הפעל את כל יישומי הרשת (מודולים 01-04):

**Bash:**
```bash
cd ..  # מתיקיית השורש
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # מספריית השורש
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

שני הסקריפטים טוענים אוטומטית משתני סביבה מקובץ ה-`.env` בשורש ויבנו את קבצי ה-JAR אם הם לא קיימים.

> **הערה:** אם אתה מעדיף לבנות את כל המודולים ידנית לפני ההפעלה:
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
./stop.sh  # מודול זה בלבד
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

הנה הממשק הראשי של מודול הנדסת הפרומפטים, שבו ניתן להתנסות בכל שמונת הדפוסים זה לצד זה.

<img src="../../../translated_images/he/dashboard-home.5444dbda4bc1f79d.webp" alt="הממשק הראשי" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*הלוח הראשי המציג את כל 8 דפוסי הנדסת פרומפט עם התכונות ומקרי השימוש שלהם*

## חקר הדפוסים

ממשק הרשת מאפשר לך להתנסות באסטרטגיות פרומפט שונות. כל דפוס פותר בעיות שונות - נסה אותם כדי לראות מתי כל גישה בולטת.

> **הערה: שידור מול לא-שידור** — בכל דף דפוס מוצעים שני כפתורים: **🔴 שידור תגובה (חי)** ואפשרות **לא-שידור**. שידור משתמש ב-Server-Sent Events (SSE) להצגת הטוקנים בזמן אמת כשהמודל מייצר אותם, כך שאתה רואה התקדמות מיידית. האופציה הלא-שידורית מחכה לתגובה המלאה לפני ההצגה. עבור פרומפטים המפעילים חשיבה מעמיקה (למשל, High Eagerness, קוד המשתקף עצמי), הקריאה הלא-שידורית יכולה לקחת זמן רב מאוד — לפעמים דקות — ללא משוב גלוי. **השתמש בשידור בעת התנסות עם פרומפטים מורכבים** כדי שתוכל לראות את המודל פועל ולהימנע מהתרשמות שהבקשה נתקעה.
>
> **הערת דפדפן** — תכונת השידור משתמשת ב-Fetch Streams API (`response.body.getReader()`) הדורש דפדפן מלא (Chrome, Edge, Firefox, Safari). היא **אינה עובדת** בדפדפן הפשוט המובנה של VS Code, שכן ה-webview שלו לא תומך ב-ReadableStream API. אם אתה משתמש בדפדפן הפשוט, הכפתורים הלא-שידוריים יעבדו כרגיל — רק כפתורי השידור מושפעים. פתח `http://localhost:8083` בדפדפן חיצוני לחוויה מלאה.

### נמוך מול גבוה בהתלהבות

שאול שאלה פשוטה כמו "מה זה 15% מתוך 200?" באמצעות Low Eagerness. תקבל תשובה ישירה ומידית. עכשיו שאל שאלה מורכבת כמו "עצב אסטרטגיית מטמון ל-API בתעבורה גבוהה" באמצעות High Eagerness. לחץ **🔴 שידור תגובה (חי)** וצפה בהופעת ההסבר המפורט של המודל טוקן אחרי טוקן. אותו מודל, אותה שאלה – אבל הפרומפט מורה כמה לחשוב.

### ביצוע משימה (קדימונים לכלים)

זרימות עבודה מרובות שלבים מרוויחות מתכנון מראש וסיפור התקדמות. המודל מפרט מה יעשה, מתאר כל שלב, ואז מסכם תוצאות.

### קוד המשתקף עצמי

נסה "צור שירות לאימות דואר אלקטרוני". במקום רק להפיק קוד ולעצור, המודל מייצר, מעריך לפי קריטריוני איכות, מזהה חולשות ומשפר. תראה אותו חוזר על עצמו עד שהקוד עומד בסטנדרטים של ייצור.

### ניתוח מובנה

סקירות קוד דורשות מסגרות הערכה עקביות. המודל מנתח קוד לפי קטגוריות קבועות (נכונות, שיטות, ביצועים, אבטחה) עם רמות חומרה.

### שיחה מרובת סבבים

שאול "מה זה Spring Boot?" ואז מיד המשך עם "הראה לי דוגמה". המודל זוכר את השאלה הראשונה ונותן דוגמת Spring Boot ספציפית. בלי זיכרון, השאלה השנייה הייתה מעורפלת מדי.

### הסבר צעד-אחר-צעד

בחר בעיית מתמטיקה ונסה אותה הן עם הסבר צעד-אחר-צעד והן עם Low Eagerness. ההתלהבות הנמוכה נותנת רק את התשובה - מהיר אך לא שקוף. בהסבר צעד-אחר-צעד תראה כל חישוב והחלטה.

### פלט מוגבל

כשהדרוש פורמטים מסוימים או ספירת מילים, דפוס זה מאכף עמידה מדויקת. נסה לייצר סיכום במדויק עם 100 מילים בפורמט נקודות.

## מה שלומדים באמת

**מאמץ ההסבר משנה הכל**

GPT-5.2 מאפשר לך לשלוט במאמץ המחשובי דרך הפרומפטים שלך. מאמץ נמוך נותן תגובות מהירות עם חקירה מינימלית. מאמץ גבוה מביא את המודל להשקיע מחשבה עמוקה. אתה לומד להתאים את המאמץ למורכבות המשימה — אל תבזבז זמן על שאלות פשוטות, אבל אל תמהר בהחלטות מורכבות.

**מבנה מנהל התנהגות**

שמתי לב לתגי XML בפרומפטים? הם לא רק לקישוט. מודלים עומדים בהוראות מובנות בצורה אמינה יותר מאשר טקסט חופשי. כשאתה צריך תהליכים רב-שלביים או לוגיקה מורכבת, המבנה עוזר למודל לעקוב מהיכן הוא ומה הבא. הדיאגרמה למטה מפרקת פרומפט ממוסגר היטב, ומראה כיצד תגיות כמו `<system>`, `<instructions>`, `<context>`, `<user-input>`, ו-`<constraints>` מארגנות את ההוראות לחלקים ברורים.

<img src="../../../translated_images/he/prompt-structure.a77763d63f4e2f89.webp" alt="מבנה הפרומפט" width="800"/>

*אנטומיה של פרומפט מובנה היטב עם חלקים ברורים וארגון בסגנון XML*

**איכות דרך הערכה עצמית**

דפוסי ההשתקפות העצמית פועלים על ידי הפיכת קריטריוני האיכות למפורשים. במקום לקוות שהמודל "יעשה נכון", אתה אומר במדויק מה זה "נכון": לוגיקה נכונה, טיפול בשגיאות, ביצועים, אבטחה. המודל יכול להעריך את הפלט שלו ולשפר אותו. זה ממיר ייצור קוד מגלגל לוטו לתהליך מוגדר.

**הקשר הוא מוגבל**

שיחות רב-סבביות פועלות על ידי הכללת היסטוריית ההודעות עם כל בקשה. אך יש גבול - לכל מודל יש מספר טוקנים מקסימלי. ככל שהשיחות מתארכות, תזדקק לאסטרטגיות לשמירה על ההקשר הרלוונטי מבלי להגיע לתקרה הזו. מודול זה מראה לך כיצד זיכרון עובד; מאוחר יותר תלמד מתי לסכם, מתי לשכוח ומתי לשלוף.

## צעדים הבאים

**מודול הבא:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**ניווט:** [← קודם: מודול 01 - מבוא](../01-introduction/README.md) | [חזרה לעיקרי](../README.md) | [הבא: מודול 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**הצהרת זכויות**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). על אף שאנו שואפים לדיוק, יש להביא בחשבון כי תרגומים אוטומטיים עלולים להכיל שגיאות או אי-דיוקים. המסמך המקורי בשפתו המקורית ייחשב כמקור הסמכות. עבור מידע קריטי, מומלץ לתרגם על ידי מתרגם מקצועי אנושי. אנו אינם אחראים לכל אי הבנה או פרשנות שגויה הנובעים משימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->