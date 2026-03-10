# מודול 05: פרוטוקול הקשר מודל (MCP)

## תוכן העניינים

- [סיור וידאו](../../../05-mcp)
- [מה תלמדו](../../../05-mcp)
- [מה זה MCP?](../../../05-mcp)
- [איך MCP עובד](../../../05-mcp)
- [המודול האג'נטי](../../../05-mcp)
- [הרצת הדוגמאות](../../../05-mcp)
  - [תנאים מוקדמים](../../../05-mcp)
- [התחלה מהירה](../../../05-mcp)
  - [פעולות קבצים (Stdio)](../../../05-mcp)
  - [סוכן מפקח](../../../05-mcp)
    - [הרצת הדמו](../../../05-mcp)
    - [איך המפקח עובד](../../../05-mcp)
    - [איך FileAgent מגלֶה כלים של MCP בזמן ריצה](../../../05-mcp)
    - [אסטרטגיות תגובה](../../../05-mcp)
    - [הבנת התוצאה](../../../05-mcp)
    - [הסבר על תכונות המודול האג'נטי](../../../05-mcp)
- [מושגי מפתח](../../../05-mcp)
- [מזל טוב!](../../../05-mcp)
  - [מה הלאה?](../../../05-mcp)

## סיור וידאו

צפו במפגש חי זה שמסביר כיצד להתחיל עם מודול זה:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="סוכני בינה מלאכותית עם כלים ו-MCP - מפגש חי" width="800"/></a>

## מה תלמדו

בניתם בינה מלאכותית שיחה, שלטתם בפרומפטים, היססתם תשובות במסמכים, ויצרתם סוכנים עם כלים. אבל כל הכלים האלה היו מותאמים במיוחד ליישום הספציפי שלכם. מה אם יכולתם לתת לבינה המלאכותית שלכם גישה לאקוסיסטם סטנדרטי של כלים שכל אחד יכול ליצור ולשתף? במודול זה תלמדו כיצד לעשות זאת בדיוק בעזרת פרוטוקול הקשר מודל (MCP) ומודול האג'נטי של LangChain4j. ראשית נציג קורא קבצים פשוט ב-MCP ואז נראה כיצד הוא משתלב בקלות בזרימות עבודה אג׳נטיות מתקדמות באמצעות דפוס סוכן מפקח.

## מה זה MCP?

פרוטוקול הקשר מודל (MCP) נותן בדיוק את זה - דרך סטנדרטית לאפליקציות AI למצוא ולהשתמש בכלים חיצוניים. במקום לכתוב אינטגרציות מותאמות אישית לכל מקור נתונים או שירות, מחוברים לשרתים של MCP המציגים את היכולות שלהם בפורמט עקבי. הסוכן האינטיליגנטי שלך יכול אז לגלות ולהשתמש בכלים אלו אוטומטית.

האיור שלמטה מראה את ההבדל — בלי MCP, כל אינטגרציה דורשת חיבור נקודה-לנקודה מותאם; עם MCP, פרוטוקול אחד מחבר את האפליקציה לכל כלי:

<img src="../../../translated_images/he/mcp-comparison.9129a881ecf10ff5.webp" alt="השוואת MCP" width="800"/>

*לפני MCP: אינטגרציות מורכבות נקודה-לנקודה. אחרי MCP: פרוטוקול אחד, אפשרויות אינסופיות.*

MCP פותר בעיה מרכזית בפיתוח AI: כל אינטגרציה היא מותאמת אישית. רוצים לגשת לגיטהאב? קוד מותאם. רוצים לקרוא קבצים? קוד מותאם. רוצים לשאול בסיס נתונים? קוד מותאם. וגם אף אינטגרציה כזו לא עובדת עם יישומי AI אחרים.

MCP מאחד את זה. שרת MCP מציג כלים עם תיאורים ברורים וסכימות. כל לקוח MCP יכול להתחבר, לגלות כלים זמינים, ולהשתמש בהם. בנו פעם אחת, השתמשו בכל מקום.

האיור שלמטה ממחיש את הארכיטקטורה הזו — לקוח MCP יחיד (האפליקציה שלכם) מתחבר למספר שרתי MCP, שכל אחד מציג את קבוצת הכלים שלו דרך הפרוטוקול הסטנדרטי:

<img src="../../../translated_images/he/mcp-architecture.b3156d787a4ceac9.webp" alt="ארכיטקטורת MCP" width="800"/>

*ארכיטקטורת פרוטוקול הקשר מודל - גילוי והרצת כלים סטנדרטיים*

## איך MCP עובד

מתחת למכסה המנוע, MCP משתמש בארכיטקטורה שכבתית. אפליקציית Java שלכם (הלקוח של MCP) מגלה כלים זמינים, שולחת בקשות JSON-RPC דרך שכבת תעבורה (Stdio או HTTP), ושרת MCP מריץ פעולות ומחזיר תוצאות. האיור הבא מפרט כל שכבה של הפרוטוקול הזה:

<img src="../../../translated_images/he/mcp-protocol-detail.01204e056f45308b.webp" alt="פירוט פרוטוקול MCP" width="800"/>

*איך MCP עובד מתחת לפני השטח — לקוחות מגלים כלים, מחליפים הודעות JSON-RPC, ומבצעים פעולות דרך שכבת תעבורה.*

**ארכיטקטורת לקוח-שרת**

MCP משתמש במודל לקוח-שרת. השרתים מספקים כלים - קריאת קבצים, שאילתות לבסיסי נתונים, קריאות API. הלקוחות (האפליקציה שלכם) מתחברים לשרתים ומשתמשים בכלים שלהם.

כדי להשתמש ב-MCP עם LangChain4j, הוסיפו את התלות הבאה למייבן:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**גילוי כלים**

כאשר הלקוח שלכם מתחבר לשרת MCP, הוא שואל "איזה כלים יש לך?" השרת משיב ברשימת כלים זמינים, כל אחד עם תיאורים וסכימות פרמטרים. הסוכן האינטיליגנטי שלכם יכול אז להחליט באילו כלים להשתמש בהתבסס על בקשות המשתמש. האיור למטה מראה את ניסיון היד — הלקוח שולח בקשת `tools/list` והשרת מחזיר את הכלים הזמינים שלו עם תיאורים וסכימות פרמטרים:

<img src="../../../translated_images/he/tool-discovery.07760a8a301a7832.webp" alt="גילוי כלים ב-MCP" width="800"/>

*הבינה המלאכותית מגלתה כלים זמינים בזמן האתחול — היא יודעת כעת אילו יכולות קיימות ויכולה להחליט באילו להשתמש.*

**מנגנוני תעבורה**

MCP תומך במנגנוני תעבורה שונים. שתי האפשרויות הן Stdio (לתקשורת בתהליכים משניים מקומיים) ו-HTTP סטרימינג (לשרתים מרוחקים). המודול הזה מדגים את תעבורת ה-Stdio:

<img src="../../../translated_images/he/transport-mechanisms.2791ba7ee93cf020.webp" alt="מנגנוני תעבורה" width="800"/>

*מנגנוני תעבורה של MCP: HTTP לשרתים מרוחקים, Stdio לתהליכים מקומיים*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

לתהליכים מקומיים. האפליקציה שלכם מפעילה שרת כתהליך בן ומתקשרת איתו דרך קלט/פלט סטנדרטי. שימושי לגישה למערכת הקבצים או לכלי שורת פקודה.

```java
McpTransport stdioTransport = new StdioMcpTransport.Builder()
    .command(List.of(
        npmCmd, "exec",
        "@modelcontextprotocol/server-filesystem@2025.12.18",
        resourcesDir
    ))
    .logEvents(false)
    .build();
```

שרת `@modelcontextprotocol/server-filesystem` מציג את הכלים הבאים, כולם מוגבלים לתיקיות שציינתם:

| כלי | תיאור |
|------|---------|
| `read_file` | קריאת תוכן של קובץ בודד |
| `read_multiple_files` | קריאת מספר קבצים בקריאה אחת |
| `write_file` | יצירה או כתיבה מעל קובץ |
| `edit_file` | ביצוע עריכות מיקוד של חיפוש והחלפה |
| `list_directory` | רשימת קבצים ותיקיות בנתיב מסוים |
| `search_files` | חיפוש רקורסיבי של קבצים התואמים לתבנית |
| `get_file_info` | קבלת מטא-דטה של הקובץ (גודל, זמן, הרשאות) |
| `create_directory` | יצירת תיקייה (כולל תיקיות אב) |
| `move_file` | העברת או שינוי שם של קובץ או תיקייה |

האיור הבא מראה איך תעבורת Stdio פועלת בזמן ריצה — האפליקציה שלכם מפעילה את שרת MCP כתהליך בן ומתקשרת אתו דרך צינורות stdin/stdout, ללא מעורבות תקשורת רשת או HTTP:

<img src="../../../translated_images/he/stdio-transport-flow.45eaff4af2d81db4.webp" alt="זרימת תעבורה Stdio" width="800"/>

*תעבורת Stdio בפעולה — האפליקציה שלכם מפעילה את שרת MCP כתהליך בן ומתקשרת אתו דרך stdin/stdout.*

> **🤖 נסו עם [GitHub Copilot](https://github.com/features/copilot) צ'אט:** פתחו את [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) ושאלו:
> - "איך תעבורת Stdio עובדת ומתי להשתמש בה במקום HTTP?"
> - "איך LangChain4j מנהל את מחזור החיים של תהליכי שרת MCP שמופעלים?"
> - "מהן ההשלכות הביטחוניות של מתן גישה לבינה למערכת הקבצים?"

## המודול האג'נטי

בעוד MCP נותן כלים סטנדרטיים, מודול האג'נטי של LangChain4j מספק דרך הכרזתית לבנות סוכנים שמאורכבים מהכלים הללו. התווית `@Agent` ו-`AgenticServices` מאפשרות להגדיר התנהגות סוכן דרך ממשקים במקום קוד אימפרטיבי.

במודול הזה תחקרו את דפוס ה-**סוכן מפקח** — גישה אג'נטית מתקדמת שבה סוכן "מפקח" מחליט בצורה דינמית אילו סוכנים משניים להפעיל בהתבסס על בקשות המשתמש. נחבר את שתי התפיסות על ידי מתן גישת קבצים מונעת MCP לאחד הסוכנים המשניים שלנו.

כדי להשתמש במודול האג'נטי, הוסיפו את התלות הבאה למייבן:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **הערה:** מודול `langchain4j-agentic` משתמש במאפיין גרסה נפרד (`langchain4j.mcp.version`) כי הוא יוצא לאור בלוח זמנים שונה מספריות הליבה של LangChain4j.

> **⚠️ ניסיוני:** מודול `langchain4j-agentic` הוא **ניסיוני** ועשוי להשתנות. הדרך היציבה לבנות עוזרי AI היא עדיין עם `langchain4j-core` ועם כלים מותאמים אישית (מודול 04).

## הרצת הדוגמאות

### תנאים מוקדמים

- השלמת [מודול 04 - כלים](../04-tools/README.md) (מודול זה מבוסס על מושגי כלים מותאמים אישית ומשווה אותם לכלי MCP)
- קובץ `.env` בתיקיית השורש עם אישורי Azure (נוצר על ידי `azd up` במודול 01)
- Java 21+, Maven 3.9+
- Node.js 16+ ו-npm (לשרתים של MCP)

> **הערה:** אם עדיין לא הגדרתם משתני סביבה, ראו [מודול 01 - מבוא](../01-introduction/README.md) להוראות פריסה (`azd up` יוצר אוטומטית את קובץ `.env`), או העתיקו `.env.example` ל-`.env` בתיקיית השורש ומלאו את הערכים שלכם.

## התחלה מהירה

**בשימוש VS Code:** פשוט לחצו לחיצה ימנית על כל קובץ דמו ב-Explorer ובחרו **"Run Java"**, או השתמשו בקונפיגורציות ההפעלה מפאנל Run and Debug (ודאו שקובץ ה-`.env` שלכם מוגדר עם אישורי Azure לפני כן).

**בשימוש Maven:** לחלופין, אפשר להריץ משורת הפקודה עם הדוגמאות שלמטה.

### פעולות קבצים (Stdio)

דוגמה זו מדגימה כלים מבוססי תהליכים משניים מקומיים.

**✅ אין צורך בתנאים מוקדמים** - שרת MCP מופעל אוטומטית.

**בשימוש סקריפטים להפעלה (מומלץ):**

הסקריפטים האלה טוענים אוטומטית משתני סביבה מתוך קובץ ה-`.env` שבשורש:

**ב-Bash:**
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```

**ב-PowerShell:**
```powershell
cd 05-mcp
.\start-stdio.ps1
```

**בשימוש VS Code:** לחצו לחיצה ימנית על `StdioTransportDemo.java` ובחרו **"Run Java"** (וודאו שקובץ ה-`.env` שלכם מוגדר).

האפליקציה מפעילה אוטומטית שרת MCP למערכת הקבצים וקוראת קובץ מקומי. שימו לב איך ניהול התהליך מתבצע עבורכם.

**פלט צפוי:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### סוכן מפקח

דפוס סוכן המפקח הוא צורת AI אג׳נטית **גמישה**. המפקח משתמש ב-LLM כדי להחליט באופן עצמאי אילו סוכנים להפעיל בהתבסס על בקשת המשתמש. בדוגמה הבאה נחבר גישת קבצים מונעת MCP יחד עם סוכן LLM ליצירת זרימת עבודה של קריאת קובץ → דוח מונחה.

בדמו, `FileAgent` קורא קובץ באמצעות כלי מערכת הקבצים של MCP, ו-`ReportAgent` מייצר דוח מובנה הכולל סיכום מנהלים (משפט אחד), 3 נקודות מפתח, והמלצות. המפקח מארגן את הזרימה הזו אוטומטית:

<img src="../../../translated_images/he/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="דפוס סוכן מפקח" width="800"/>

*המפקח משתמש ב-LLM שלו כדי להחליט אילו סוכנים להפעיל ובאיזה סדר — אין צורך בנתיב קשיח.*

כך נראית זרימת העבודה הממשית במפעל הקבצים-לדוח שלנו:

<img src="../../../translated_images/he/file-report-workflow.649bb7a896800de9.webp" alt="זרימת עבודה מקובץ לדוח" width="800"/>

*FileAgent קורא את הקובץ דרך כלי MCP, ואז ReportAgent ממיר את התוכן הגולמי לדוח מובנה.*

תרשים הרצף הבא עוקב אחרי האורקסטרציה המלאה של המפקח — מהפעלת שרת MCP, דרך בחירת סוכנים אוטונומית של המפקח, לקריאות הכלים דרך stdio ולדוח הסופי:

<img src="../../../translated_images/he/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="תרשים רצף סוכן מפקח" width="800"/>

*המפקח מפעיל באופן אוטונומי את FileAgent (שקורא את הקובץ באמצעות שרת MCP דרך stdio), ואז מפעיל את ReportAgent ליצירת דוח מובנה — כל סוכן מאחסן את הפלט שלו בזיכרון Agentic Scope המשותף.*

כל סוכן מאחסן את הפלט שלו ב-**Agentic Scope** (זכרון משותף), המאפשר לסוכנים הבאים לגשת לתוצאות קודמות. הדבר ממחיש כיצד כלים של MCP משתלבים בצורה חלקה בזרימות עבודה אג׳נטיות — המפקח לא צריך לדעת *איך* קבצים נקראים, רק ש-`FileAgent` יכול לעשות זאת.

#### הרצת הדמו

הסקריפטים אוטומטית טוענים משתני סביבה מתוך קובץ ה-`.env` שבשורש:

**ב-Bash:**
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```

**ב-PowerShell:**
```powershell
cd 05-mcp
.\start-supervisor.ps1
```

**בשימוש VS Code:** לחצו לחיצה ימנית על `SupervisorAgentDemo.java` ובחרו **"Run Java"** (וודאו שקובץ ה-`.env` שלכם מוגדר).

#### איך המפקח עובד

לפני בניית סוכנים, יש לחבר את תעבורת MCP ללקוח ולעטוף אותו כ-`ToolProvider`. כך כלים של שרת MCP זמינים לסוכנים שלכם:

```java
// צור לקוח MCP מהטרנספורט
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// עטוף את הלקוח כספק כלי — זה מחבר כלים של MCP ל-LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

כעת תוכלו להזריק `mcpToolProvider` לכל סוכן שזקוק לכלי MCP:

```java
// שלב 1: FileAgent קורא קבצים באמצעות כלי MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // מכיל כלי MCP לפעולות קבצים
        .build();

// שלב 2: ReportAgent מייצר דוחות מבנים
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// המפקח מארגן את זרימת העבודה של קובץ → דוח
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // החזר את הדוח הסופי
        .build();

// המפקח מחליט אילו סוכנים להפעיל בהתאם לבקשה
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### איך FileAgent מגלֶה כלים של MCP בזמן ריצה

ייתכן ותהיו סקרנים: **איך `FileAgent` יודע כיצד להשתמש בכלי מערכת הקבצים של npm?** התשובה היא שהוא לא — ה-**LLM** מחליט על כך בזמן ריצה דרך סכימות הכלים.
ממשק `FileAgent` הוא רק **הגדרת פרומפט**. אין לו ידע מוצמד מראש על `read_file`, `list_directory`, או כל כלי MCP אחר. הנה מה שקורה מקצה לקצה:

1. **הפעלת השרת:** `StdioMcpTransport` מפעיל את חבילת ה-npm של `@modelcontextprotocol/server-filesystem` כתהליך ילד
2. **גילוי כלי:** ה-`McpClient` שולח בקשת JSON-RPC של `tools/list` לשרת, שמחזיר שמות כלים, תיאורים וסכימות פרמטרים (לדוגמה, `read_file` — *"קרא את תוכן הקובץ המלא"* — `{ path: string }`)
3. **הזרקת סכימה:** `McpToolProvider` עוטף את הסכימות שהתגלו ומספק אותן ל-LangChain4j
4. **החלטת LLM:** כאשר קוראים ל-`FileAgent.readFile(path)`, LangChain4j שולח את הודעת המערכת, הודעת המשתמש, **והרשימה של סכימות הכלים** ל-LLM. ה-LLM קורא את תיאורי הכלים ומייצר קריאת כלי (לדוגמה, `read_file(path="/some/file.txt")`)
5. **ביצוע:** LangChain4j לוכד את קריאת הכלי, מנתב אותה דרך לקוח ה-MCP חזרה לתת-תהליך ה-Node.js, מקבל את התוצאה ומחזיר אותה ל-LLM

זוהי אותה מנגנון [גילוי כלים](../../../05-mcp) שתואר למעלה, אך מיושם במיוחד לזרימת העבודה של הסוכן. ההערות `@SystemMessage` ו-`@UserMessage` מנחות את התנהגות ה-LLM, בעוד ש-`ToolProvider` המוזרק נותן לו את ה**יכולות** — ה-LLM גשרת בין השניים בזמן ריצה.

> **🤖 נסה עם הצ'אט של [GitHub Copilot](https://github.com/features/copilot):** פתח את [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) ושאל:
> - "איך הסוכן הזה יודע איזה כלי MCP לקרוא?"
> - "מה יקרה אם אסיר את ה-ToolProvider מבונה הסוכן?"
> - "איך סכימות הכלים מועברות ל-LLM?"

#### אסטרטגיות תגובה

כאשר אתה מגדיר `SupervisorAgent`, אתה מציין כיצד עליו לנסח את התשובה הסופית למשתמש לאחר שהסוכנים המשניים השלימו את משימותיהם. התרשים למטה מציג את שלוש האסטרטגיות הזמינות — LAST מחזיר ישירות את הפלט של הסוכן האחרון, SUMMARY מסנתז את כל הפלטים באמצעות LLM, ו-SCORED בוחר את האופציה שקיבלה ניקוד גבוה יותר מול הבקשה המקורית:

<img src="../../../translated_images/he/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*שלוש אסטרטגיות לאופן שבו המפקח מנסח את תגובתו הסופית — בחר על פי האם אתה רוצה את פלט הסוכן האחרון, סיכום מסונתז, או את האפשרות עם הניקוד הגבוה ביותר.*

האסטרטגיות הזמינות הן:

| אסטרטגיה | תיאור |
|----------|-------------|
| **LAST** | המפקח מחזיר את הפלט של הסוכן המשני או הכלי האחרון שנקרא. זה שימושי כאשר הסוכן האחרון בזרימת העבודה מיועד במיוחד להפיק את התשובה הסופית והשלמה (למשל, "סוכן סיכום" בצינור מחקר). |
| **SUMMARY** | המפקח משתמש ב-LLM הפנימי שלו כדי לסנתז סיכום של כל האינטראקציה וכל הפלטים של הסוכנים המשניים, ואז מחזיר את הסיכום הזה כתגובה סופית. זה מספק תשובה מסודרת ומרוכזת למשתמש. |
| **SCORED** | המערכת משתמשת ב-LLM פנימי כדי לדרג את תגובת ה-LAST ואת ה-SUMMARY של האינטראקציה מול הבקשה המקורית של המשתמש, ומחזירה את האופציה שהתקבלה לה את הניקוד הגבוה יותר. |

ראה את [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) למימוש המלא.

> **🤖 נסה עם הצ'אט של [GitHub Copilot](https://github.com/features/copilot):** פתח את [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) ושאל:
> - "איך המפקח מחליט אילו סוכנים להפעיל?"
> - "מה ההבדל בין דפוס העבודה Supervisor ל-Sequential?"
> - "איך אני יכול להתאים אישית את התכנון של המפקח?"

#### הבנת הפלט

כאשר תריץ את הדמו, תראה הליכה מובנית דרך האופן שבו המפקח מארגן סוכנים מרובים. הנה מה שכל חלק אומר:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**כותרת** מציגה את רעיון זרימת העבודה: צינור ממוקד מקריאת קבצים להפקת דוחות.

```
--- WORKFLOW ---------------------------------------------------------
  ┌─────────────┐      ┌──────────────┐
  │  FileAgent  │ ───▶ │ ReportAgent  │
  │ (MCP tools) │      │  (pure LLM)  │
  └─────────────┘      └──────────────┘
   outputKey:           outputKey:
   'fileContent'        'report'

--- AVAILABLE AGENTS -------------------------------------------------
  [FILE]   FileAgent   - Reads files via MCP → stores in 'fileContent'
  [REPORT] ReportAgent - Generates structured report → stores in 'report'
```

**תרשים זרימת עבודה** מציג את זרימת הנתונים בין הסוכנים. לכל סוכן תפקיד ספציפי:
- **FileAgent** קורא קבצים באמצעות כלי MCP ומאחסן את התוכן הגולמי ב-`fileContent`
- **ReportAgent** צורך את התוכן הזה ומפיק דו"ח מובנה ב-`report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**בקשת משתמש** מציגה את המשימה. המפקח מנתח את הבקשה ומחליט להפעיל FileAgent → ReportAgent.

```
--- SUPERVISOR ORCHESTRATION -----------------------------------------
  The Supervisor decides which agents to invoke and passes data between them...

  +-- STEP 1: Supervisor chose -> FileAgent (reading file via MCP)
  |
  |   Input: .../file.txt
  |
  |   Result: LangChain4j is an open-source, provider-agnostic Java framework for building LLM...
  +-- [OK] FileAgent (reading file via MCP) completed

  +-- STEP 2: Supervisor chose -> ReportAgent (generating structured report)
  |
  |   Input: LangChain4j is an open-source, provider-agnostic Java framew...
  |
  |   Result: Executive Summary...
  +-- [OK] ReportAgent (generating structured report) completed
```

**אורקסטרציה של המפקח** מציגה את זרימת שני השלבים בפעולה:
1. **FileAgent** קורא את הקובץ דרך MCP ושומר את התוכן
2. **ReportAgent** מקבל את התוכן ומפיק דו"ח מובנה

המפקח קיבל החלטות אלו **באופן אוטונומי** על סמך בקשת המשתמש.

```
--- FINAL RESPONSE ---------------------------------------------------
Executive Summary
...

Key Points
...

Recommendations
...

--- AGENTIC SCOPE (Data Flow) ----------------------------------------
  Each agent stores its output for downstream agents to consume:
  * fileContent: LangChain4j is an open-source, provider-agnostic Java framework...
  * report: Executive Summary...
```

#### הסבר על תכונות מודול Agentic

הדוגמה מדגימה מספר תכונות מתקדמות של מודול Agentic. בואו נבחן מקרוב את Agentic Scope ו-Agent Listeners.

**Agentic Scope** מציג את הזיכרון המשותף שבו סוכנים אחסנו את התוצאות שלהם באמצעות `@Agent(outputKey="...")`. זה מאפשר:
- לסוכנים מאוחרים לגשת לפלטים של סוכנים קודמים
- למפקח לסנתז תגובה סופית
- לך לבחון מה כל סוכן הפיק

התרשים למטה מציג כיצד Agentic Scope פועל כזיכרון משותף בזרימת עבודה של קובץ לדוח — FileAgent כותב את הפלט תחת המפתח `fileContent`, ReportAgent קורא אותו וכותב פלט משלו תחת `report`:

<img src="../../../translated_images/he/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope משמש כזיכרון משותף — FileAgent כותב `fileContent`, ReportAgent קורא וכותב `report`, והקוד שלך קורא את התוצאה הסופית.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // נתוני קובץ גולמיים מ-FileAgent
String report = scope.readState("report");            // דוח מובנה מ-ReportAgent
```

**Agent Listeners** מאפשרים ניטור ו-debug של ביצוע הסוכן. הפלט שלב אחר שלב שאתה רואה בדמו מגיע מ-AgentListener שמתחבר לכל קריאת סוכן:
- **beforeAgentInvocation** - נקרא כאשר המפקח בוחר סוכן, מאפשר לך לראות איזה סוכן נבחר ומדוע
- **afterAgentInvocation** - נקרא כאשר סוכן משלים פעולה, ומציג את התוצאה שלו
- **inheritedBySubagents** - כאשר true, המאזין עוקב אחרי כל הסוכנים בהיררכיה

התרשים הבא מציג את מחזור החיים המלא של Agent Listener, כולל כיצד `onError` מטפל בכשלים במהלך ביצוע הסוכן:

<img src="../../../translated_images/he/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners מתחברים למחזור החיים של הביצוע — עוקבים מתי סוכנים מתחילים, מסיימים או נתקלים בשגיאות.*

```java
AgentListener monitor = new AgentListener() {
    private int step = 0;
    
    @Override
    public void beforeAgentInvocation(AgentRequest request) {
        step++;
        System.out.println("  +-- STEP " + step + ": " + request.agentName());
    }
    
    @Override
    public void afterAgentInvocation(AgentResponse response) {
        System.out.println("  +-- [OK] " + response.agentName() + " completed");
    }
    
    @Override
    public boolean inheritedBySubagents() {
        return true; // להפיץ לכל הסוכנים המשניים
    }
};
```

מעבר לדפוס Supervisor, מודול `langchain4j-agentic` מספק מספר דפוסי עבודה רבי עוצמה. התרשים למטה מציג את כולם — מצינורות פשוטים רציפים ועד זרימות עבודה עם אישור אנושי:

<img src="../../../translated_images/he/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*חמישה דפוסי זרימת עבודה לאורקסטרציה של סוכנים — מצינורות רציפים פשוטים ועד זרימות עבודה עם מעקב אנושי.*

| דפוס | תיאור | מקרה שימוש |
|---------|-------------|----------|
| **Sequential** | הפעלת סוכנים לפי סדר, הפלט זורם הבא | צינורות: מחקר → ניתוח → דוח |
| **Parallel** | הפעלת סוכנים במקביל | משימות עצמאיות: מזג אוויר + חדשות + מניות |
| **Loop** | איטרציה עד שתקיים תנאי | ניקוד איכות: שפר עד ניקוד ≥ 0.8 |
| **Conditional** | ניתוב על בסיס תנאים | סיווג → ניתוב לסוכן מומחה |
| **Human-in-the-Loop** | הוספת נקודות ביקורת אנושיות | זרימות אישור, סקירת תוכן |

## מושגים מרכזיים

עכשיו כשחקרת את MCP ואת מודול ה-agentic בפעולה, נסכם מתי להשתמש בכל גישה.

אחת היתרונות הגדולים של MCP היא האקוסיסטם המתפתח שלו. התרשים למטה מציג כיצד פרוטокол אוניברסלי יחיד מחבר את אפליקציית ה-AI שלך למגוון רחב של שרתי MCP — מגישה למערכת הקבצים ומסדי נתונים ועד GitHub, דואר אלקטרוני, גרידת רשת ועוד:

<img src="../../../translated_images/he/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP יוצר אקוסיסטם פרוטוקולי אוניברסלי — כל שרת תואם MCP עובד עם כל לקוח תואם MCP, ומאפשר שיתוף כלים בין אפליקציות.*

**MCP** אידיאלי כאשר אתה רוצה לנצל אקוסיסטמות כלים קיימות, לבנות כלים שיכולים להיות משותפים בין אפליקציות, לשלב שירותי צד-שלישי עם פרוטוקולים סטנדרטיים, או להחליף מימושי כלים בלי לשנות קוד.

**מודול Agentic** מתאים במיוחד כאשר אתה רוצה הגדרות סוכן הצהרתי באמצעות הערות `@Agent`, צריך אורקסטרציה של זרימת עבודה (רצוף, לולאה, מקבילי), מעדיף עיצוב סוכנים מבוסס ממשק על פני קוד אימפרטיבי, או משלב מספר סוכנים שמשתפים פלטים דרך `outputKey`.

**דפוס הסוכן Supervisor** זוהר כאשר זרימת העבודה אינה צפויה מראש ואתה רוצה שה-LLM יחליט, כאשר יש לך סוכנים רבים ומומחים שזקוקים לאורקסטרציה דינמית, כשבונים מערכות שיחה שמנתבות ליכולות שונות, או כאשר אתה רוצה את התנהגות הסוכן הגמישה, ההסתגלותית ביותר.

כדי לסייע לך להחליט בין שיטות `@Tool` מותאמות אישית מ-Module 04 לבין כלי MCP מהמודול הזה, ההשוואה הבאה מדגישה את הפשרות המרכזיות — כלים מותאמים אישית מספקים קישור הדוק ובטיחות טיפוסים מלאה ללוגיקה אפליקטיבית ספציפית, בעוד שכלי MCP מציעים אינטגרציה סטנדרטית שניתנת לשימוש חוזר:

<img src="../../../translated_images/he/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*מתי להשתמש בשיטות @Tool מותאמות מול כלי MCP — כלים מותאמים ללוגיקה ספציפית עם בטיחות טיפוסים מלאה, כלי MCP לאינטגרציות סטנדרטיות שעובדות בין אפליקציות.*

## מזל טוב!

השלמת את כל חמשת המודולים של קורס LangChain4j למתחילים! הנה מבט על מסע הלמידה המלא שהשלמת — מאינטראקציות שיחה בסיסיות ועד מערכות agentic מופעלות על ידי MCP:

<img src="../../../translated_images/he/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*מסע הלמידה שלך דרך כל חמשת המודולים — משיחה בסיסית ועד מערכות agentic בהנעת MCP.*

השלמת את קורס LangChain4j למתחילים. למדת:

- איך לבנות AI שיחתי עם זיכרון (מודול 01)
- דפוסי הנדסת פרומפט למשימות שונות (מודול 02)
- עיגון תגובות במסמכים שלך עם RAG (מודול 03)
- יצירת סוכני AI בסיסיים (עוזרים) עם כלים מותאמים אישית (מודול 04)
- אינטגרציה של כלים סטנדרטיים עם מודולי LangChain4j MCP ו-Agentic (מודול 05)

### מה הלאה?

לאחר שהשלמת את המודולים, חקור את [מדריך הבדיקה](../docs/TESTING.md) כדי לראות מושגי בדיקה ב-LangChain4j בפעולה.

**משאבים רשמיים:**
- [תיעוד LangChain4j](https://docs.langchain4j.dev/) - מדריכים מקיפים ו-API
- [GitHub של LangChain4j](https://github.com/langchain4j/langchain4j) - קוד מקור ודוגמאות
- [מדריכי LangChain4j](https://docs.langchain4j.dev/tutorials/) - מדריכים שלב אחר שלב למקרי שימוש שונים

תודה שהשלמת את הקורס!

---

**ניווט:** [← קודם: מודול 04 - כלים](../04-tools/README.md) | [חזרה לעמוד הראשי](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב ויתור**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדיוק, יש להבין כי תרגומים אוטומטיים עלולים לכלול שגיאות או אי-דיוקים. המסמך המקורי בשפת המקור שלו מהווה את המקור הסמכותי. למידע קריטי מומלץ לבצע תרגום מקצועי על ידי אדם. איננו אחראים על כל אי-הבנה או פרשנות שגויה הנובעת משימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->