# מודול 01: התחלה עם LangChain4j

## תוכן העניינים

- [מה תלמדו](../../../01-introduction)
- [דרישות מוקדמות](../../../01-introduction)
- [הבנת הבעיה המרכזית](../../../01-introduction)
- [הבנת אסימונים](../../../01-introduction)
- [איך זיכרון עובד](../../../01-introduction)
- [איך זה משתמש ב-LangChain4j](../../../01-introduction)
- [פריסת תשתית Azure OpenAI](../../../01-introduction)
- [הרצת האפליקציה מקומית](../../../01-introduction)
- [שימוש באפליקציה](../../../01-introduction)
  - [צ'אט ללא מצב (הלוח השמאלי)](../../../01-introduction)
  - [צ'אט עם מצב (הלוח הימני)](../../../01-introduction)
- [צעדים הבאים](../../../01-introduction)

## מה תלמדו

אם השלמת את ההתחלה המהירה, ראית איך לשלוח הנחיות ולקבל תגובות. זו הבסיס, אבל אפליקציות אמיתיות צריכות יותר. במודול זה תלמד כיצד לבנות AI שיחתי שזוכר הקשר ושומר על מצב - ההבדל בין הדגמה חד-פעמית לאפליקציה מוכנה לייצור.

נשתמש ב-GPT-5.2 של Azure OpenAI לאורך ההדרכה הזו כי היכולות המתקדמות שלו בניתוח הופכות את ההתנהגות של התבניות השונות לברורה יותר. כשאתה מוסיף זיכרון, תראה את ההבדל בצורה ברורה. זה מקל על ההבנה מה כל רכיב מביא לאפליקציה שלך.

תבנה אפליקציה אחת שמדגימה את שתי התבניות:

**צ'אט ללא מצב** - כל בקשה היא עצמאית. למודל אין זיכרון של ההודעות הקודמות. זו התבנית שבה השתמשת בהתחלה המהירה.

**שיחה עם מצב** - כל בקשה כוללת היסטוריית שיחה. המודל שומר הקשר לאורך מספר סבבים. זה מה שאפליקציות ייצור דורשות.

## דרישות מוקדמות

- מנוי Azure עם גישה ל-Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **הערה:** Java, Maven, Azure CLI ו-Azure Developer CLI (azd) מותקנים מראש ב-devcontainer המסופק.

> **הערה:** המודול משתמש ב-GPT-5.2 על Azure OpenAI. הפריסה מוגדרת אוטומטית באמצעות `azd up` - אין לשנות את שם הדגם בקוד.

## הבנת הבעיה המרכזית

מודלי שפה הם ללא מצב. כל קריאת API היא עצמאית. אם תשלח "שמי ג'ון" ואז תשאל "מה השם שלי?", המודל לא יודע שהכרת את עצמך זה עכשיו. הוא מתייחס לכל בקשה כאילו זו השיחה הראשונה שלך אי פעם.

זה בסדר לשאלות ותשובות פשוטות, אבל חסר תועלת לאפליקציות אמיתיות. בוטים של שירות לקוחות צריכים לזכור מה סיפרת להם. עוזרים אישיים זקוקים להקשר. כל שיחה מרובת סבבים דורשת זיכרון.

<img src="../../../translated_images/he/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="שיחות ללא מצב לעומת שיחות עם מצב" width="800"/>

*ההבדל בין שיחות ללא מצב (קריאות עצמאיות) ושיחות עם מצב (מודעות להקשר)*

## הבנת אסימונים

לפני שנצלול לשיחות, חשוב להבין אסימונים - היחידות הבסיסיות של הטקסט שמעובדים על ידי מודלי השפה:

<img src="../../../translated_images/he/token-explanation.c39760d8ec650181.webp" alt="הסבר על אסימונים" width="800"/>

*דוגמה כיצד טקסט מפורק לאסימונים - "אני אוהב AI!" הופך ל-4 יחידות עיבוד נפרדות*

אסימונים הם איך מודלי AI מודדים ומעבדים טקסט. מילים, פיסוק ואפילו רווחים יכולים להיות אסימונים. למודל שלך יש גבול לכמות האסימונים שהוא יכול לעבד בבת אחת (400,000 ל-GPT-5.2, עם עד 272,000 אסימוני קלט ו-128,000 אסימוני פלט). הבנה של אסימונים עוזרת לך לנהל אורך שיחה ועלויות.

## איך זיכרון עובד

זיכרון צ'אט פותר את בעיית היעדר המצב על ידי שמירת היסטוריית שיחה. לפני שליחת הבקשה למודל, המסגרת מוסיפה הודעות רלוונטיות קודמות. כשאתה שואל "מה השם שלי?", המערכת באמת שולחת את כל היסטוריית השיחה, כך שהמודל רואה שכבר אמרת "שמי ג'ון".

LangChain4j מספק מימושים של זיכרון שמטפלים בכך אוטומטית. אתה בוחר כמה הודעות לשמור והמסגרת מנהלת את חלון ההקשר.

<img src="../../../translated_images/he/memory-window.bbe67f597eadabb3.webp" alt="מושג חלון זיכרון" width="800"/>

*MessageWindowChatMemory שומר על חלון מחליק של הודעות אחרונות, ומסיר אוטומטית ישנות*

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

**מודל צ'אט** - תגדיר Azure OpenAI כ-bean בספרינג ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

הבנאי קורא את האישורים ממשתני הסביבה שהוגדרו ע"י `azd up`. הגדרת `baseUrl` לנקודת הקצה של Azure שלך מאפשרת ללקוח OpenAI לעבוד עם Azure OpenAI.

**זיכרון שיחה** - עקוב אחר היסטוריית הצ'אט באמצעות MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

צור זיכרון עם `withMaxMessages(10)` כדי לשמור את 10 ההודעות האחרונות. הוסף הודעות משתמש ו-AI עם עטיפות טיפוסיות: `UserMessage.from(text)` ו-`AiMessage.from(text)`. אחזר היסטוריה עם `memory.messages()` ושלח למודל. השירות מאחסן מופעי זיכרון נפרדים לכל מזהה שיחה, המאפשרים למספר משתמשים לנהל שיחות במקביל.

> **🤖 נסה עם [GitHub Copilot](https://github.com/features/copilot) Chat:** פתח את [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ושאל:
> - "איך MessageWindowChatMemory מחליט אילו הודעות לזרוק כשהחלון מלא?"
> - "האם אפשר לממש אחסון זיכרון מותאם עם בסיס נתונים במקום בזיכרון?"
> - "איך הייתי מוסיף סיכום כדי לדחוס היסטוריית שיחה ישנה?"

נקודת קצה של צ'אט ללא מצב מדלגת על הזיכרון לחלוטין - פשוט `chatModel.chat(prompt)` כמו בהתחלה המהירה. נקודת הקצה עם מצב מוסיפה הודעות לזיכרון, מאחזרת היסטוריה וכוללת את ההקשר בכל בקשה. אותה תצורת מודל, תבניות שונות.

## פריסת תשתית Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # בחר מנוי ומיקום (eastus2 מומלץ)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # בחר מנוי ומיקום (מומלץ eastus2)
```

> **הערה:** אם נתקלת בשגיאת timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), פשוט הרץ שוב `azd up`. משאבי Azure עשויים עדיין להסתפק ברקע, וניסיון חוזר מאפשר לסיום הפריסה כשהמשאבים מגיעים למצב סופי.

הדבר יבצע:
1. פריסת משאב Azure OpenAI עם דגמי GPT-5.2 ו-text-embedding-3-small
2. יצירה אוטומטית של קובץ `.env` בשורש הפרויקט עם האישורים
3. הגדרת כל משתני הסביבה הדרושים

**יש לך בעיות בפריסה?** עיין ב-[README תשתית](infra/README.md) לפתרונות מפורטים כולל בעיות שמות תת-דומיין, שלבים לפריסה ידנית בפורטל Azure והנחיות תצורת דגם.

**ווידוא שהפריסה הצליחה:**

**Bash:**
```bash
cat ../.env  # אמור להראות את AZURE_OPENAI_ENDPOINT, API_KEY, וכו'.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # צריך להציג את AZURE_OPENAI_ENDPOINT, API_KEY, וכו'.
```

> **הערה:** הפקודה `azd up` יוצרת אוטומטית את קובץ `.env`. אם תצטרך לעדכן אותו מאוחר יותר, תוכל לערוך את קובץ `.env` באופן ידני או לייצר אותו מחדש על ידי הרצה של:
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

**ווידוא הפריסה:**

וודא שקובץ `.env` קיים בתיקיית השורש עם האישורים של Azure:

**Bash:**
```bash
cat ../.env  # צריך להציג AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # אמור להראות את AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**הפעל את האפליקציות:**

**אפשרות 1: שימוש ב-Spring Boot Dashboard (מומלץ למשתמשי VS Code)**

ה-dev container כולל את הרחבת Spring Boot Dashboard, המספקת ממשק חזותי לניהול כל אפליקציות Spring Boot. ניתן למצוא אותו בסרגל הפעילות בצד שמאל של VS Code (חפש את סמל Spring Boot).

מה-Dashboard של Spring Boot, תוכל:
- לראות את כל אפליקציות Spring Boot הזמינות בסביבת העבודה
- להפעיל/להפסיק אפליקציות בלחיצה אחת
- לצפות ביומני האפליקציה בזמן אמת
- לנטר את מצב האפליקציה

פשוט לחץ על כפתור ההפעלה לצד "introduction" כדי להפעיל את המודול הזה, או להפעיל את כל המודולים בבת אחת.

<img src="../../../translated_images/he/dashboard.69c7479aef09ff6b.webp" alt="לוח בקרה של Spring Boot" width="400"/>

**אפשרות 2: שימוש בסקריפטים של shell**

הפעל את כל אפליקציות הרשת (מודולים 01-04):

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
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

שני הסקריפטים טוענים אוטומטית משתני סביבה מקובץ `.env` בשורש ויבנו את קבצי JAR אם אינם קיימים.

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

האפליקציה מספקת ממשק רשת עם שתי מימושי צ'אט לצד אחד לשני.

<img src="../../../translated_images/he/home-screen.121a03206ab910c0.webp" alt="מסך הבית של האפליקציה" width="800"/>

*לוח בקרה המציג את צ'אט פשוט (ללא מצב) ואת צ'אט שיחתי (עם מצב)*

### צ'אט ללא מצב (הלוח השמאלי)

נסה את זה קודם. שאל "שמי ג'ון" ואז מיד שאל "מה השם שלי?" המודל לא יזכור כי כל הודעה היא עצמאית. זה מדגים את הבעיה המרכזית באינטגרציה בסיסית של מודל שפה - אין הקשר שיחה.

<img src="../../../translated_images/he/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="הדגמת צ'אט ללא מצב" width="800"/>

*ה-AI לא זוכר את שמך מההודעה הקודמת*

### צ'אט עם מצב (הלוח הימני)

עכשיו נסה אותו רצף כאן. שאל "שמי ג'ון" ואז "מה השם שלי?" הפעם הוא זוכר. ההבדל הוא MessageWindowChatMemory - הוא שומר היסטוריית שיחה וכולל אותה בכל בקשה. כך עובד AI שיחתי במערכת ייצור.

<img src="../../../translated_images/he/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="הדגמת צ'אט עם מצב" width="800"/>

*ה-AI זוכר את שמך מהשיחה הקודמת*

שני הלוחות משתמשים באותו מודל GPT-5.2. ההבדל היחיד הוא הזיכרון. זה מבהיר מה זיכרון מביא לאפליקציה שלך ולמה הוא חיוני למקרי שימוש אמיתיים.

## צעדים הבאים

**המודול הבא:** [02-מהנדס-הנחיות - הנחיית פרומפטים עם GPT-5.2](../02-prompt-engineering/README.md)

---

**ניווט:** [← הקודם: מודול 00 - התחלה מהירה](../00-quick-start/README.md) | [חזרה לעמוד הראשי](../README.md) | [הבא: מודול 02 - הנחיית פרומפט →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**הבהרה**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדיוק, יש לקחת בחשבון כי תרגומים אוטומטיים עלולים להכיל שגיאות או אי-דיוקים. המסמך המקורי בשפת המקור מהווה את המקור הסמכותי. למידע קריטי מומלץ להשתמש בתרגום מקצועי שנעשה על ידי אדם. אנו לא נושאים באחריות על אי הבנות או פרשנויות שגויות הנובעות משימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->