# מודול 01: התחלה עם LangChain4j

## תוכן העניינים

- [מדריך וידאו](../../../01-introduction)
- [מה תלמדו](../../../01-introduction)
- [דרישות מוקדמות](../../../01-introduction)
- [הבנת הבעיה המרכזית](../../../01-introduction)
- [הבנת טוקנים](../../../01-introduction)
- [איך זיכרון עובד](../../../01-introduction)
- [איך זה משתמש ב-LangChain4j](../../../01-introduction)
- [פריסת תשתית Azure OpenAI](../../../01-introduction)
- [הרצת האפליקציה מקומית](../../../01-introduction)
- [שימוש באפליקציה](../../../01-introduction)
  - [שיחת ללא זיכרון (לוח שמאלי)](../../../01-introduction)
  - [שיחת עם זיכרון (לוח ימני)](../../../01-introduction)
- [השלבים הבאים](../../../01-introduction)

## מדריך וידאו

צפו במפגש חי שמסביר איך להתחיל עם מודול זה:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## מה תלמדו

בהתחלה המהירה השתמשתם במודלים של GitHub כדי לשלוח הנחיות, לקרוא כלים, לבנות צינור RAG ולבדוק כללי שמירה. הדמואים האלו הראו מה אפשרי — עכשיו נעבור ל-Azure OpenAI ו-GPT-5.2 ונתחיל לבנות אפליקציות בסגנון ייצור. מודול זה מתמקד בבינה מלאכותית שיחתית שזוכרת הקשר ומנהלת מצב — המושגים שהדמואים של ההתחלה המהירה השתמשו בהם מאחורי הקלעים אך לא הסבירו.

נשתמש ב-GPT-5.2 של Azure OpenAI לאורך מדריך זה כי יכולות ההנמקה המתקדמות שלו ממחישות טוב יותר את התנהגותם של דפוסים שונים. כשמוסיפים זיכרון, רואים בבירור את ההבדל. זה מקל על ההבנה מה כל מרכיב מביא לאפליקציה שלכם.

תבנו אפליקציה אחת שמדגימה את שני הדפוסים:

**שיחת ללא זיכרון** - כל בקשה היא עצמאית. למודל אין זיכרון של הודעות קודמות. זהו הדפוס שבו השתמשתם בהתחלה המהירה.

**שיחה עם זיכרון** - כל בקשה כוללת היסטוריית שיחה. המודל שומר הקשר על פני מספר סבבים. כך אפליקציות ייצור אמיתיות פועלות.

## דרישות מוקדמות

- מנוי Azure עם גישה ל-Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **הערה:** Java, Maven, Azure CLI ו-Azure Developer CLI (azd) מותקנים מראש ב-devcontainer המסופק.

> **הערה:** מודול זה משתמש ב-GPT-5.2 על Azure OpenAI. הפריסה מוגדרת אוטומטית דרך `azd up` - אל תשנו את שם המודל בקוד.

## הבנת הבעיה המרכזית

מודלי שפה הם ללא זיכרון. כל קריאת API היא עצמאית. אם אתה שולח "השם שלי הוא ג'ון" ואז שואל "מה השם שלי?", המודל אינו מודע לכך שהצגת את עצמך זה עתה. הוא מתייחס לכל בקשה כאילו זו השיחה הראשונה שהיתה לך אי פעם.

זה בסדר לשאלות ותשובות פשוטות אך חסר תועלת לאפליקציות אמיתיות. רובוטים של שירות לקוחות צריכים לזכור מה שסיפרת להם. עוזרים אישיים צריכים הקשר. כל שיחה רב-סבבית דורשת זיכרון.

הדיאגרמה הבאה משווה בין שתי הגישות — בצד שמאל קריאה ללא זיכרון ששוכחת את שמך; מצד ימין קריאה עם זיכרון מגובה ב-ChatMemory שזוכרת אותו.

<img src="../../../translated_images/he/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*ההבדל בין שיחות ללא זיכרון (קריאות עצמאיות) ושיחות עם זיכרון (מודעות להקשר)*

## הבנת טוקנים

לפני שנכנסים לשיחות, חשוב להבין טוקנים - יחידות הבסיס של טקסט שמודלי שפה מעבדים:

<img src="../../../translated_images/he/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*דוגמה לאופן שבו טקסט מפורק לטוקנים - "I love AI!" הופך ל-4 יחידות עיבוד נפרדות*

טוקנים הם האופן שבו מודלי בינה מלאכותית מודדים ומעבדים טקסט. מילים, פיסוק ואפילו רווחים יכולים להיות טוקנים. למודל שלך יש מגבלה על כמות הטוקנים שהוא יכול לעבד בבת אחת (400,000 ל-GPT-5.2, עם עד 272,000 טוקנים קלט ו-128,000 טוקני פלט). הבנת הטוקנים עוזרת לך לנהל את אורך השיחות והעלויות.

## איך זיכרון עובד

זיכרון שיחה פותר את בעיית היעדר הזיכרון באמצעות שמירת היסטוריית השיחה. לפני שליחת הבקשה למודל, המסגרת מוסיפה את ההודעות הרלוונטיות הקודמות. כשאתה שואל "מה השם שלי?", המערכת למעשה שולחת את כל ההיסטוריה של השיחה, ומאפשרת למודל לראות שאמרת קודם "השם שלי הוא ג'ון."

LangChain4j מספק מימושי זיכרון שעושים זאת אוטומטית. אתה בוחר כמה הודעות לשמור והמסגרת מנהלת את חלון ההקשר. הדיאגרמה למטה מראה איך MessageWindowChatMemory שומר חלון מחליק של הודעות אחרונות.

<img src="../../../translated_images/he/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory שומר חלון מחליק של הודעות אחרונות, ומסיר אוטומטית הודעות ישנות*

## איך זה משתמש ב-LangChain4j

מודול זה מרחיב את ההתחלה המהירה באמצעות שילוב Spring Boot והוספת זיכרון שיחה. כך החלקים מתחברים יחד:

**תלויות** - הוסף שתי ספריות LangChain4j:

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

**מודל שיחה** - הגדר את Azure OpenAI כבין של Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

הבונה קורא את האישורים ממשתני סביבה שהוגדרו על ידי `azd up`. הגדרת `baseUrl` לכתובת ה-endpoint של Azure שלך מאפשרת ללקוח OpenAI לעבוד עם Azure OpenAI.

**זיכרון שיחה** - עקוב אחרי היסטוריית השיחות באמצעות MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

צור זיכרון עם `withMaxMessages(10)` לשמירת 10 ההודעות האחרונות. הוסף הודעות משתמש ובינה מלאכותית בעטיפות טיפוסיות: `UserMessage.from(text)` ו-`AiMessage.from(text)`. שלוף היסטוריה עם `memory.messages()` ושלח אותה למודל. השירות שומר מופעי זיכרון נפרדים לכל מזהה שיחה, ומאפשר למספר משתמשים לנהל שיחות בו זמנית.

> **🤖 נסה עם [GitHub Copilot](https://github.com/features/copilot) Chat:** פתח את [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ושאל:
> - "איך MessageWindowChatMemory מחליט אילו הודעות להסיר כשהחלון מלא?"
> - "האם אפשר לממש אחסון זיכרון מותאם אישית באמצעות מסד נתונים במקום בזיכרון פנימי?"
> - "איך הייתי מוסיף סיכום ללחוץ את היסטוריית השיחה הישנה?"

נקודת הקצה של שיחת ללא זיכרון מדלגת על הזיכרון לגמרי - פשוט `chatModel.chat(prompt)` כמו בהתחלה המהירה. נקודת הקצה של שיחה עם זיכרון מוסיפה הודעות לזיכרון, מושכת היסטוריה ומכלילה את ההקשר בכל בקשה. אותה הגדרת מודל, דפוסים שונים.

## פריסת תשתית Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # בחר מנוי ומיקום (מומלץ eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # בחר מנוי ומיקום (מומלץ eastus2)
```

> **הערה:** אם אתה נתקל בשגיאת timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), פשוט הרץ `azd up` שוב. משאבי Azure עשויים להיות עדיין בתהליך פריסה ברקע, ונסיון חוזר מאפשר לפריסה להסתיים כשהמשאבים מגיעים למצב סופי.

זה י:
1. יפרוס משאב Azure OpenAI עם מודלים GPT-5.2 ו-text-embedding-3-small
2. ייצור אוטומטית קובץ `.env` בשורש הפרויקט עם האישורים
3. יקבע את כל משתני הסביבה הנדרשים

**בעיות בפריסה?** ראה את [קובץ ההסבר התשתיתי](infra/README.md) לפתרון בעיות מפורט כולל התנגשויות בשמות תת-דומיין, שלבי פריסה ידנית ב-Azure Portal, והנחיות להגדרת מודלים.

**אימות פריסה הצליחה:**

**Bash:**
```bash
cat ../.env  # צריך להראות את AZURE_OPENAI_ENDPOINT, API_KEY וכו'.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # צריך להציג AZURE_OPENAI_ENDPOINT, API_KEY, וכו'.
```

> **הערה:** פקודת `azd up` יוצרת אוטומטית את הקובץ `.env`. אם אתה צריך לעדכן אותו מאוחר יותר, אתה יכול לערוך את הקובץ `.env` ידנית או ליצור מחדש על ידי הרצת:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## הרצת האפליקציה מקומית

**אימות פריסה:**

ודא שקובץ `.env` קיים בספרייה הראשית עם אישורי Azure. הרץ זאת מספריית המודול (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # צריך להראות AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # צריך להציג את AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**הפעל את האפליקציות:**

**אפשרות 1: שימוש ב-Spring Boot Dashboard (מומלץ למשתמשי VS Code)**

מיכל הפיתוח כולל את הרחבת Spring Boot Dashboard, המספקת ממשק חזותי לניהול כל אפליקציות Spring Boot. ניתן למצוא אותה ב-Activity Bar בצד שמאל של VS Code (חפש את סמל Spring Boot).

מ-Spring Boot Dashboard, אתה יכול:
- לראות את כל אפליקציות ה-Spring Boot הזמינות בסביבת העבודה
- להפעיל/להפסיק אפליקציות בלחיצה פשוטה
- לצפות בלוגים של האפליקציה בזמן אמת
- לנטר סטטוס האפליקציה

פשוט לחץ על כפתור ההפעלה ליד "introduction" כדי להתחיל במודול הזה, או הפעל את כל המודולים בבת אחת.

<img src="../../../translated_images/he/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*הלוח של Spring Boot ב-VS Code — הפעלה, עצירה ומעקב אחר כל המודולים במקום אחד*

**אפשרות 2: שימוש בסקריפטים של shell**

הפעל את כל אפליקציות הווב (מודולים 01-04):

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

או הפעל רק את המודול הזה:

**Bash:**
```bash
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

שני הסקריפטים טוענים אוטומטית משתני סביבה מקובץ `.env` בשורש וייבנו את קבצי ה-JAR אם אינם קיימים.

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

פתח את http://localhost:8080 בדפדפן שלך.

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

## שימוש באפליקציה

האפליקציה מספקת ממשק אינטרנט עם שתי יישומי שיחה לצד זה.

<img src="../../../translated_images/he/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*לוח המחוונים מציג את שתי אפשרויות השיחה: Simple Chat (ללא זיכרון) ו-Conversational Chat (עם זיכרון)*

### שיחת ללא זיכרון (לוח שמאלי)

נסו את זה קודם. שאלו "השם שלי הוא ג'ון" ואז מיד שאלו "מה השם שלי?" המודל לא יזכור כי כל הודעה עצמאית. זה מדגים את הבעיה המרכזית עם אינטגרציה בסיסית של מודל שפה - אין הקשר שיחה.

<img src="../../../translated_images/he/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*ה-AI לא זוכר את שמך מהודעה קודמת*

### שיחת עם זיכרון (לוח ימני)

עכשיו נסו את אותה הרצף כאן. שאלו "השם שלי הוא ג'ון" ואז "מה השם שלי?" הפעם הוא זוכר. ההבדל הוא MessageWindowChatMemory - שומר היסטוריית שיחה ומכליל אותה בכל בקשה. כך בינה מלאכותית שיחתית במצב ייצור פועלת.

<img src="../../../translated_images/he/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*ה-AI זוכר את שמך משלב קודם בשיחה*

שני הלוחות משתמשים באותו מודל GPT-5.2. ההבדל היחיד הוא הזיכרון. זה מבהיר מה הזיכרון מביא לאפליקציה שלך ומדוע הוא חיוני לשימושים אמיתיים.

## השלבים הבאים

**מודול הבא:** [02-prompt-engineering - הנדסת פרומפט עם GPT-5.2](../02-prompt-engineering/README.md)

---

**ניווט:** [← הקודם: מודול 00 - התחלה מהירה](../00-quick-start/README.md) | [חזרה לעמוד הראשי](../README.md) | [הבא: מודול 02 - הנדסת פרומפט →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב ויתור**:
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). על אף שאנו מתאמצים להשיג דיוק, יש להיזהר כי תרגומים אוטומטיים עשויים לכלול טעויות או אי דיוקים. המסמך המקורי בשפתו המקורית ייחשב כמקור הסמכות. למידע קריטי מומלץ להיעזר בתרגום מקצועי בידי מתרגם אנושי. אנו לא נושאים באחריות לכל אי הבנה או פרשנות שגויה הנובעות משימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->