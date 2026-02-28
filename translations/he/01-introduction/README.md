# מודול 01: התחלה עם LangChain4j

## תוכן העניינים

- [סרטון הסבר](../../../01-introduction)
- [מה תלמדו](../../../01-introduction)
- [דרישות מקדימות](../../../01-introduction)
- [הבנת הבעיה המרכזית](../../../01-introduction)
- [הבנת טוקנים](../../../01-introduction)
- [כיצד זיכרון עובד](../../../01-introduction)
- [כיצד זה משתמש ב-LangChain4j](../../../01-introduction)
- [פריסת תשתית Azure OpenAI](../../../01-introduction)
- [הרצת האפליקציה מקומית](../../../01-introduction)
- [שימוש באפליקציה](../../../01-introduction)
  - [שיחה ללא סטייט (לוח שמאל)](../../../01-introduction)
  - [שיחה עם סטייט (לוח ימין)](../../../01-introduction)
- [השלבים הבאים](../../../01-introduction)

## סרטון הסבר

צפו במפגש חי שמסביר כיצד להתחיל עם מודול זה:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## מה תלמדו

אם השלמתם את התחלה המהירה, ראיתם כיצד לשלוח הנחיות ולקבל תגובות. זו היסוד, אך אפליקציות אמיתיות דורשות יותר. מודול זה מלמד כיצד לבנות AI שיחתי שזוכר הקשר ושומר על סטייט - ההבדל בין הדגמה חד-פעמית לאפליקציה מוכנה לייצור.

נשתמש ב-GPT-5.2 של Azure OpenAI לאורך המדריך מכיוון שיכולות הרציונליזציה המתקדמות שלו מדגישות את התנהגותם של דפוסים שונים. כשאתם מוסיפים זיכרון, תראו בבירור את ההבדל. זה מקל על ההבנה מהם התרומות של כל רכיב לאפליקציה שלכם.

תבנו אפליקציה אחת שמדגימה את שני הדפוסים:

**שיחה ללא סטייט** - כל בקשה עצמאית. למודל אין זיכרון של הודעות קודמות. זהו הדפוס שהשתמשתם בו בתחילת המהירה.

**שיחה עם סטייט** - כל בקשה כוללת היסטוריית שיחה. המודל שומר על ההקשר בין פניות מרובות. זה מה שאפליקציות ייצור דורשות.

## דרישות מקדימות

- מנוי Azure עם גישה ל-Azure OpenAI  
- Java 21, Maven 3.9+  
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)  
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)  

> **הערה:** Java, Maven, Azure CLI ו-Azure Developer CLI (azd) מותקנים מראש ב-devcontainer המסופק.

> **הערה:** מודול זה משתמש ב-GPT-5.2 על Azure OpenAI. הפריסה מוגדרת אוטומטית דרך `azd up` - אל תשנו את שם הדגם בקוד.

## הבנת הבעיה המרכזית

מודלים לשוניים הם ללא סטייט. כל קריאת API היא עצמאית. אם תשלחו "שמי הוא ג'ון" ואז תשאלו "מה שמי?", למודל אין מושג שהצגת את עצמך זה עתה. הוא מתייחס לכל בקשה כאילו זו השיחה הראשונה שהייתה לך אי פעם.

זה טוב לשאלות ותשובות פשוטות אך חסר תועלת לאפליקציות אמיתיות. רובוטי שירות לקוחות צריכים לזכור מה סיפרת להם. עוזרים אישיים צריכים הקשר. כל שיחה מרובת סבבים דורשת זיכרון.

<img src="../../../translated_images/he/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*ההבדל בין שיחות ללא סטייט (קריאות עצמאיות) לשיחות עם סטייט (מודעות להקשר)*

## הבנת טוקנים

לפני שנכנסים לשיחות, חשוב להבין טוקנים - היחידות הבסיסיות של טקסט שמעובדים על ידי מודלים לשוניים:

<img src="../../../translated_images/he/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*דוגמה כיצד טקסט מפורק לטוקנים - "I love AI!" הופך ל-4 יחידות עיבוד נפרדות*

טוקנים הם האופן שבו מודלי AI מודדים ומעבדים טקסט. מילים, פיסוק ואפילו רווחים יכולים להיות טוקנים. למודל שלך יש מגבלה על מספר הטוקנים שהוא יכול לעבד בפעם אחת (400,000 עבור GPT-5.2, עם עד 272,000 טוקני קלט ועד 128,000 טוקני פלט). הבנת הטוקנים עוזרת לנהל את אורך השיחה והעלויות.

## כיצד זיכרון עובד

זיכרון שיחה פותר את הבעיה של חוסר הסטייט על ידי שמירת היסטוריית שיחה. לפני שליחת הבקשה למודל, המסגרת כוללת את ההודעות הרלוונטיות הקודמות. כשאתה שואל "מה שמי?", המערכת שולחת בפועל את כל היסטוריית השיחה, ומאפשרת למודל לראות שאמרת קודם "שמי הוא ג'ון".

LangChain4j מספק מימושים של זיכרון שמטפלים בכך אוטומטית. אתה בוחר כמה הודעות לשמור והמסגרת מנהלת את חלון ההקשר.

<img src="../../../translated_images/he/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory שומר על חלון מחליק של הודעות אחרונות, מושלכות אוטומטית כאשר הן ישנות*

## כיצד זה משתמש ב-LangChain4j

מודול זה מאריך את התחלה המהירה על ידי אינטגרציה עם Spring Boot והוספת זיכרון שיחה. כך החלקים מתחברים יחד:

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

**דגם שיחה** - הגדר את Azure OpenAI כבין Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

הבונה קורא פרטי אימות ממשתני סביבה שהוגדרו על ידי `azd up`. הגדרת `baseUrl` לנקודת הקצה של Azure שלך מאפשרת ללקוח OpenAI לפעול עם Azure OpenAI.

**זיכרון שיחה** - עקוב אחרי היסטוריית שיחה עם MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

צור זיכרון עם `withMaxMessages(10)` לשמירת 10 ההודעות האחרונות. הוסף הודעות משתמש ו-AI עם עטיפות ממוספרות: `UserMessage.from(text)` ו-`AiMessage.from(text)`. שלוף היסטוריה עם `memory.messages()` ושלח למודל. השירות מאחסן מופעי זיכרון נפרדים לכל מזהה שיחה, ומאפשר למספר משתמשים לשוחח בו-זמנית.

> **🤖 נסה עם [GitHub Copilot](https://github.com/features/copilot) צ'אט:** פתח את [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ושאל:
> - "איך MessageWindowChatMemory מחליט אילו הודעות להוריד כשהחלון מלא?"
> - "האם אני יכול לממש אחסון זיכרון מותאם אישית באמצעות מסד נתונים במקום בזיכרון הפנימי?"
> - "איך הייתי מוסיף סיכום כדי לדחוס היסטוריית שיחה ישנה?"

נקודת הקצה לשיחה ללא סטייט מדלגת על זיכרון לחלוטין - פשוט `chatModel.chat(prompt)` כאילו בתחילת מהירה. נקודת הקצה עם סטייט מוסיפה הודעות לזיכרון, שולפת היסטוריה וכוללת את ההקשר בכל בקשה. אותו הגדר דגם, רק דפוסים שונים.

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

> **הערה:** אם תיתקלו בשגיאת timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), פשוט הריצו שוב `azd up`. משאבי Azure עדיין עלולים להיות בתהליך פריסה ברקע, ונסיון חוזר מאפשר לפריסה להסתיים כאשר המשאבים מגיעים למצב סופי.

פעולה זו:
1. מפעילה משאב Azure OpenAI עם דגמי GPT-5.2 ו-text-embedding-3-small  
2. מייצרת אוטומטית קובץ `.env` בספריית הפרויקט עם פרטי אימות  
3. מגדירה את כל משתני הסביבה הדרושים  

**בעיות בפריסה?** ראו את [קובץ README של התשתית](infra/README.md) לפתרון בעיות מפורט כולל התנגשויות שמות תת-דומיין, שלבי פריסה ידניים בפורטל Azure והנחיות להגדרת דגם.

**וודאו שהפריסה הצליחה:**

**Bash:**
```bash
cat ../.env  # צריך להציג את AZURE_OPENAI_ENDPOINT, API_KEY וכו'.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # צריך להראות AZURE_OPENAI_ENDPOINT, API_KEY, וכו'.
```

> **הערה:** הפקודה `azd up` מייצרת אוטומטית את קובץ ה-`.env`. אם תצטרכו לעדכן אותו מאוחר יותר, תוכלו לערוך ידנית את קובץ ה-`.env` או לייצר מחדש על ידי הרצה של:
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

**וודאו שהפריסה:**

ודאו שקובץ `.env` קיים בספריית השורש עם פרטי Azure:

**Bash:**
```bash
cat ../.env  # צריך להציג AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # צריך להציג AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**הפעלת האפליקציות:**

**אפשרות 1: שימוש ב-Spring Boot Dashboard (מומלץ למשתמשי VS Code)**

מיכל הפיתוח כולל את תוסף Spring Boot Dashboard, המספק ממשק חזותי לניהול כל יישומי Spring Boot. תמצאו אותו בסרגל הפעילות בצד שמאל של VS Code (חפשו את סמל Spring Boot).

מ-Spring Boot Dashboard, תוכלו:
- לראות את כל יישומי Spring Boot הזמינים בסביבת העבודה  
- להפעיל/להפסיק יישומים בלחיצה אחת  
- לצפות בלוגים של האפליקציה בזמן אמת  
- לעקוב אחרי מצב היישום  

פשוט לחצו על כפתור ההפעלה ליד "introduction" כדי להתחיל במודול זה, או תתחילו את כל המודולים בבת אחת.

<img src="../../../translated_images/he/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**אפשרות 2: שימוש בסקריפטי shell**

הפעלת כל אפליקציות הרשת (מודולים 01-04):

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

או הפעלת המודול הזה בלבד:

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

שני הסקריפטים טוענים אוטומטית את משתני הסביבה מקובץ ה-`.env` בשורש ויבנו את קבצי JAR אם הם לא קיימים.

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

פתחו http://localhost:8080 בדפדפן שלכם.

**לעצירה:**

**Bash:**
```bash
./stop.sh  # מודול זה בלבד
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

האפליקציה מספקת ממשק אינטרנטי עם שתי מימושי צ'אט זה לצד זה.

<img src="../../../translated_images/he/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*לוח בקרה המציג את שתי האפשרויות: צ'אט פשוט (ללא סטייט) וצ'אט שיחתי (עם סטייט)*

### שיחה ללא סטייט (לוח שמאל)

נסו זאת תחילה. שאלו "שמי הוא ג'ון" ואז מיד שאלו "מה שמי?" המודל לא יזכור כי כל הודעה עצמאית. זה ממחיש את הבעיה המרכזית עם אינטגרציית מודלים לשוניים בסיסית - אין הקשר שיחה.

<img src="../../../translated_images/he/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*ה-AI לא זוכר את שמך מההודעה הקודמת*

### שיחה עם סטייט (לוח ימין)

עכשיו נסו את אותו רצף כאן. שאלו "שמי הוא ג'ון" ואז "מה שמי?" הפעם הוא זוכר. ההבדל הוא MessageWindowChatMemory - הוא שומר את היסטוריית השיחה וכולל אותה בכל בקשה. כך פועלת AI שיחתי בייצור.

<img src="../../../translated_images/he/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*ה-AI זוכר את שמך מהשיחה הקודמת*

שני הלוחות משתמשים באותו דגם GPT-5.2. ההבדל היחיד הוא בזיכרון. זה מבהיר מה הזיכרון מביא לאפליקציה שלך ולמה הוא חיוני בתרחישי שימוש אמיתיים.

## השלבים הבאים

**המודול הבא:** [02-prompt-engineering - هندسة הפקודות עם GPT-5.2](../02-prompt-engineering/README.md)

---

**ניווט:** [← קודם: מודול 00 - התחלה מהירה](../00-quick-start/README.md) | [חזרה לעמוד הראשי](../README.md) | [הבא: מודול 02 - هندسة הפקודות →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**התגוננות**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדייק, יש לזכור כי תרגומים אוטומטיים עלולים להכיל שגיאות או אי-דיוקים. המסמך המקורי בשפת המקור צריך להיחשב כמקור הסמכות. למידע קריטי מומלץ תרגום מקצועי על ידי אדם. אנו לא אחראים על אי הבנות או פרשנויות שגויות הנובעות משימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->