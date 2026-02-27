# מודול 01: התחלה עם LangChain4j

## תוכן העניינים

- [סרטון הדרכה](../../../01-introduction)
- [מה תלמדו](../../../01-introduction)
- [דרישות מוקדמות](../../../01-introduction)
- [הבנת הבעיה המרכזית](../../../01-introduction)
- [הבנת טוקנים](../../../01-introduction)
- [איך זיכרון עובד](../../../01-introduction)
- [איך זה משתמש ב-LangChain4j](../../../01-introduction)
- [פריסת תשתית Azure OpenAI](../../../01-introduction)
- [הרצת האפליקציה מקומית](../../../01-introduction)
- [שימוש באפליקציה](../../../01-introduction)
  - [צ'אט ללא זיכרון (חלון שמאלי)](../../../01-introduction)
  - [צ'אט עם זיכרון (חלון ימני)](../../../01-introduction)
- [שלבים הבאים](../../../01-introduction)

## סרטון הדרכה

צפו בסשן חי המסביר איך להתחיל עם מודול זה: [התחלה עם LangChain4j - סשן חי](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## מה תלמדו

אם השלמתם את ההתחלה המהירה, ראיתם איך לשלוח שאלות ולקבל תשובות. זו הבסיס, אך אפליקציות אמיתיות דורשות יותר. מודול זה ילמד אותך כיצד לבנות AI שיחתי שזוכר הקשר ושומר על מצב — ההבדל בין הדגמה חד-פעמית לאפליקציה מוכנה לפרודקשן.

נשתמש ב-GPT-5.2 של Azure OpenAI לאורך כל המדריך כי כישורי החשיבה המתקדמים שלו מבהירים את ההבדלים בין דפוסים שונים. כשאתה מוסיף זיכרון, אתה תראה את ההבדל בבירור. זה מקל להבין מה כל רכיב מביא לאפליקציה שלך.

תבנה אפליקציה אחת שמדגימה את שני הדפוסים:

**צ'אט ללא זיכרון** — כל בקשה עצמאית. למודל אין זיכרון של ההודעות הקודמות. זה הדפוס שהשתמשת בו בהתחלה המהירה.

**שיחה עם זיכרון** — כל בקשה כוללת היסטוריית שיחה. המודל שומר על ההקשר לאורך מספר סבבים. זה מה שאפליקציות פרודקשן דורשות.

## דרישות מוקדמות

- מנוי Azure עם גישה ל-Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **הערה:** Java, Maven, Azure CLI ו-Azure Developer CLI (azd) מותקנים מראש במיכל הפיתוח המסופק.

> **הערה:** מודול זה משתמש ב-GPT-5.2 ב-Azure OpenAI. הפריסה מוגדרת אוטומטית דרך `azd up` – אל תשנה את שם המודל בקוד.

## הבנת הבעיה המרכזית

מודלים לשוניים הם ללא זיכרון. כל קריאה ל-API היא עצמאית. אם תשלח "שמי ג'ון" ואז תשאל "איך קוראים לי?", המודל אינו מודע שהצגת את עצמך זה עתה. הוא מתייחס לכל בקשה כאילו זו השיחה הראשונה שלך אי פעם.

זה בסדר עבור שאלות ותשובות פשוטות אך לא שימושי עבור אפליקציות אמיתיות. בוטי שירות לקוחות צריכים לזכור מה סיפרת להם. עוזרים אישיים צריכים הקשר. כל שיחה בעלת כמה סבבים דורשת זיכרון.

<img src="../../../translated_images/he/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="שיחות ללא זיכרון לעומת עם זיכרון" width="800"/>

*ההבדל בין שיחות ללא זיכרון (קריאות עצמאיות) לבין שיחות עם זיכרון (מודעות להקשר)*

## הבנת טוקנים

לפני שנכנסים לשיחות, חשוב להבין טוקנים — יחידות טקסט בסיסיות שמודלים שפתיים מעבדים:

<img src="../../../translated_images/he/token-explanation.c39760d8ec650181.webp" alt="הסבר טוקן" width="800"/>

*דוגמה כיצד טקסט מפורק לטוקנים - "אני אוהב AI!" הופך ל-4 יחידות עיבוד נפרדות*

טוקנים הם כיצד מודלי AI מודדים ומעבדים טקסט. מילים, פיסוק, ואפילו רווחים יכולים להיות טוקנים. למודל שלך יש מגבלה בכמה טוקנים הוא יכול לעבד בבת אחת (400,000 ל-GPT-5.2, עם עד 272,000 טוקני קלט ו-128,000 טוקני פלט). הבנת טוקנים מסייעת בניהול אורך השיחה והעלויות.

## איך זיכרון עובד

זיכרון שיחה פותר את הבעיה של היעדר זיכרון על ידי שמירת היסטוריית השיחה. לפני שליחת הבקשה למודל, המסגרת מוסיפה את ההודעות הרלוונטיות הקודמות. כשאתה שואל "איך קוראים לי?", המערכת שולחת למעשה את כל היסטוריית השיחה, מאפשרת למודל לראות שאמרת קודם "שמי ג'ון".

LangChain4j מספקת יישומי זיכרון שמטפלים בזה אוטומטית. אתה בוחר כמה הודעות לשמור והמסגרת מנהלת את חלון ההקשר.

<img src="../../../translated_images/he/memory-window.bbe67f597eadabb3.webp" alt="רעיון חלון זיכרון" width="800"/>

*MessageWindowChatMemory שומר על חלון מתגלגל של הודעות אחרונות, ומסיר אוטומטית הודעות ישנות*

## איך זה משתמש ב-LangChain4j

מודול זה מרחיב את ההתחלה המהירה על ידי שילוב Spring Boot והוספת זיכרון שיחה. כך החלקים משתלבים:

**תלויות** — הוסף שתי ספריות LangChain4j:

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

**מודל צ'אט** — הגדר את Azure OpenAI כ-bean של Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

הבונה קורא אישורים ממשתני סביבת מערכת שנקבעו על ידי `azd up`. הגדרת `baseUrl` לנקודת הקצה של Azure שלך גורמת ללקוח OpenAI לעבוד עם Azure OpenAI.

**זיכרון שיחה** — עקוב אחרי היסטוריית השיחה עם MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

צור זיכרון עם `withMaxMessages(10)` לשמור את 10 ההודעות האחרונות. הוסף הודעות משתמש ו-AI באמצעות עטיפות טיפוסיות: `UserMessage.from(text)` ו-`AiMessage.from(text)`. שלוף היסטוריה עם `memory.messages()` ושלח למודל. השירות מאחסן מופעי זיכרון נפרדים לכל מזהה שיחה, מה שמאפשר למספר משתמשים לשוחח במקביל.

> **🤖 נסה עם [GitHub Copilot](https://github.com/features/copilot) Chat:** פתח את [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ושאל:
> - "איך MessageWindowChatMemory מחליט אילו הודעות להוריד כשהחלון מלא?"
> - "האם אפשר לממש זיכרון מותאם אישית באמצעות מסד נתונים במקום בזיכרון?"
> - "איך להוסיף סיכום כדי לדחוס היסטוריית שיחות ישנה?"

נקודת הקצה לצ'אט ללא זיכרון מדלגת על הזיכרון לחלוטין — פשוט `chatModel.chat(prompt)` כמו בהתחלה המהירה. נקודת הקצה עם זיכרון מוסיפה הודעות לזיכרון, מושכת היסטוריה וכוללת את ההקשר עם כל בקשה. הגדרת המודל זהה, דפוסים שונים.

## פריסת תשתית Azure OpenAI

**באש:**
```bash
cd 01-introduction
azd up  # בחר מנוי ומיקום (מומלץ eastus2)
```

**פאוורשל:**
```powershell
cd 01-introduction
azd up  # בחר מנוי ומיקום (מומלץ eastus2)
```

> **הערה:** אם תיתקל בשגיאת timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), פשוט הרץ שוב `azd up`. מקורות Azure עדיין עשויים להיות בתהליך פריסה ברקע, וניסיון חוזר מאפשר לפריסה להסתיים כשהמקורות מגיעים למצב סופי.

זה יבצע:
1. פריסת משאב Azure OpenAI עם דגמי GPT-5.2 ו-text-embedding-3-small
2. יצירת קובץ `.env` בשורש הפרויקט עם אישורים באופן אוטומטי
3. קביעת כל משתני הסביבה הדרושים

**יש בעיות בפריסה?** ראה את [קובץ README עבור תשתית](infra/README.md) לפתרון בעיות מפורט כולל מחלוקות בשמות תת-דומיין, שלבי פריסה ידניים בפורטל Azure, והנחיות קונפיגורציה למודל.

**אמת שהפריסה הצליחה:**

**באש:**
```bash
cat ../.env  # צריך להראות AZURE_OPENAI_ENDPOINT, API_KEY, וכו'.
```

**פאוורשל:**
```powershell
Get-Content ..\.env  # אמור להראות AZURE_OPENAI_ENDPOINT, API_KEY, וכו'.
```

> **הערה:** הפקודה `azd up` יוצרת את קובץ `.env` אוטומטית. אם תצטרך לעדכן אותו מאוחר יותר, אתה יכול לערוך את קובץ `.env` ידנית או לייצר אותו מחדש באמצעות הרצת:
>
> **באש:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **פאוורשל:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## הרצת האפליקציה מקומית

**אמת את הפריסה:**

ודא שקובץ `.env` קיים בתיקיית השורש עם אישורי Azure:

**באש:**
```bash
cat ../.env  # אמור להציג AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**פאוורשל:**
```powershell
Get-Content ..\.env  # צריך להציג AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**הפעל את האפליקציות:**

**אפשרות 1: שימוש ב-Spring Boot Dashboard (מומלץ למשתמשי VS Code)**

מיכל הפיתוח כולל את תוסף Spring Boot Dashboard, שמספק ממשק ויזואלי לניהול כל אפליקציות Spring Boot. ניתן למצוא אותו בסרגל הפעילות בצד שמאל של VS Code (חפש את האייקון של Spring Boot).

מה-Spring Boot Dashboard אפשר:
- לראות את כל אפליקציות ה-Spring Boot הזמינות בסביבת העבודה
- להפעיל/להפסיק אפליקציות בלחיץ כפתור אחד
- לצפות בלוגים של האפליקציות בזמן אמת
- לנטר את מצב האפליקציה

פשוט לחץ על כפתור הפעל ליד "introduction" כדי להתחיל במודול זה, או הפעל את כל המודולים בבת אחת.

<img src="../../../translated_images/he/dashboard.69c7479aef09ff6b.webp" alt="לוח בקרה של Spring Boot" width="400"/>

**אפשרות 2: שימוש בסקריפטים של שורת פקודה**

הפעל את כל יישומי הווב (מודולים 01-04):

**באש:**
```bash
cd ..  # מהתיקיה הראשית
./start-all.sh
```

**פאוורשל:**
```powershell
cd ..  # מתיקיית השורש
.\start-all.ps1
```

או הפעל רק את מודול זה:

**באש:**
```bash
cd 01-introduction
./start.sh
```

**פאוורשל:**
```powershell
cd 01-introduction
.\start.ps1
```

שני הסקריפטים טוענים אוטומטית משתני סביבה מקובץ `.env` בשורש, ויבנו את קבצי ה-JAR במידה והם אינם קיימים.

> **הערה:** אם אתה מעדיף לבנות את כל המודולים ידנית לפני ההפעלה:
>
> **באש:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **פאוורשל:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

פתח http://localhost:8080 בדפדפן שלך.

**לעצור את האפליקציה:**

**באש:**
```bash
./stop.sh  # רק מודול זה
# או
cd .. && ./stop-all.sh  # כל המודולים
```

**פאוורשל:**
```powershell
.\stop.ps1  # רק מודול זה
# או
cd ..; .\stop-all.ps1  # כל המודולים
```

## שימוש באפליקציה

האפליקציה מספקת ממשק ווב עם שתי ממשקי צ'אט לצד זה.

<img src="../../../translated_images/he/home-screen.121a03206ab910c0.webp" alt="מסך ראשי של האפליקציה" width="800"/>

*לוח בקרה המציג את שתי האפשרויות: צ'אט פשוט (ללא זיכרון) וצ'אט שיחתי (עם זיכרון)*

### צ'אט ללא זיכרון (חלון שמאלי)

נסה את זה קודם. שאל "שמי ג'ון" ואז מיד שאל "איך קוראים לי?" המודל לא יזכור כי כל הודעה עצמאית. זה מדגים את הבעיה המרכזית בשילוב מודלים שפתיים בסיסיים — חוסר הקשר לשיחה.

<img src="../../../translated_images/he/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="הדגמת צ'אט ללא זיכרון" width="800"/>

*ה-AI לא זוכר את שמך מההודעה הקודמת*

### צ'אט עם זיכרון (חלון ימני)

עכשיו נסה את אותו סדר כאן. שאל "שמי ג'ון" ואז "איך קוראים לי?" הפעם הוא זוכר. ההבדל הוא MessageWindowChatMemory — הוא שומר היסטוריית שיחה ומשלב אותה עם כל בקשה. כך עובד AI שיחתי בפרודקשן.

<img src="../../../translated_images/he/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="הדגמת צ'אט עם זיכרון" width="800"/>

*ה-AI זוכר את שמך מהשיחה הקודמת*

שני החלונות משתמשים באותו מודל GPT-5.2. ההבדל היחיד הוא בזיכרון. זה מבהיר מה הזיכרון מביא לאפליקציה ולמה הוא חיוני לשימוש אמיתי.

## שלבים הבאים

**מודול הבא:** [02-הנדסת פרומפט - הנדסת פרומפט עם GPT-5.2](../02-prompt-engineering/README.md)

---

**ניווט:** [← קודם: מודול 00 - התחלה מהירה](../00-quick-start/README.md) | [חזרה לדף הראשי](../README.md) | [הבא: מודול 02 - הנדסת פרומפט →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**הצהרת אי-אחריות**:
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). בעוד שאנו שואפים לדיוק, יש לקחת בחשבון שתירגומים אוטומטיים עשויים להכיל שגיאות או אי-דיוקים. המסמך המקורי בשפה המקורית שלו צריך להיחשב כמקור המהימן. למידע קריטי מומלץ להשתמש בתרגום מקצועי של מתרגם אדם. אנו לא נושאים באחריות לכל אי-הבנה או פרשנות שגויה הנובעות משימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->