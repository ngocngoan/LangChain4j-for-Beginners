# מודול 05: פרוטוקול הקשר מודל (MCP)

## תוכן העניינים

- [מה תלמד](../../../05-mcp)
- [מה זה MCP?](../../../05-mcp)
- [איך MCP עובד](../../../05-mcp)
- [המודול הסוכני](../../../05-mcp)
- [הרצת הדוגמאות](../../../05-mcp)
  - [דרישות מוקדמות](../../../05-mcp)
- [התחלה מהירה](../../../05-mcp)
  - [פעולות קבצים (Stdio)](../../../05-mcp)
  - [סוכן מפקח](../../../05-mcp)
    - [הרצת הדמו](../../../05-mcp)
    - [איך המפקח עובד](../../../05-mcp)
    - [אסטרטגיות תגובה](../../../05-mcp)
    - [הבנת הפלט](../../../05-mcp)
    - [הסבר על תכונות המודול הסוכני](../../../05-mcp)
- [מושגי מפתח](../../../05-mcp)
- [שיהיה בהצלחה!](../../../05-mcp)
  - [מה הלאה?](../../../05-mcp)

## מה תלמד

כבר בנית בינה מלאכותית שיחתית, שלטת בפרומפטים, עיגנת תגובות במסמכים, ויצרת סוכנים עם כלים. אבל כל הכלים האלה היו מותאמים במיוחד עבור היישום הספציפי שלך. מה אם היית יכול לתת לבינה המלאכותית שלך גישה לסביבה סטנדרטית של כלים שכל אחד יכול ליצור ולשתף? במודול זה תלמד כיצד לעשות זאת עם פרוטוקול הקשר מודל (MCP) ומודול הסוכני של LangChain4j. ראשית נציג קורא קבצים פשוט של MCP ואז נראה כיצד הוא משתלב בקלות בתוך תהליכים סוכניים מתקדמים באמצעות תבנית סוכן המפקח.

## מה זה MCP?

פרוטוקול הקשר מודל (MCP) מספק בדיוק את זה — דרך סטנדרטית לאפליקציות בינה מלאכותית לגלות ולהשתמש בכלים חיצוניים. במקום לכתוב אינטגרציות מותאמות אישית עבור כל מקור מידע או שירות, אתה מחבר לשרתי MCP שמציגים את היכולות שלהם בפורמט עקבי. סוכן הבינה המלאכותית שלך יכול אז לגלות ולהשתמש בכלים אלה אוטומטית.

הדיאגרמה מטה מראה את ההבדל — בלי MCP, כל אינטגרציה דורשת חיבור נקודה לנקודה מותאם אישית; עם MCP, פרוטוקול אחד מחבר את האפליקציה שלך לכל כלי:

<img src="../../../translated_images/he/mcp-comparison.9129a881ecf10ff5.webp" alt="השוואת MCP" width="800"/>

*לפני MCP: אינטגרציות נקודה לנקודה מורכבות. אחרי MCP: פרוטוקול אחד, אפשרויות אינסופיות.*

MCP פותר בעיה יסודית בפיתוח בינה מלאכותית: כל אינטגרציה היא מותאמת אישית. רוצים לגשת ל-GitHub? קוד מותאם אישית. רוצים לקרוא קבצים? קוד מותאם אישית. רוצים לשאול בסיס נתונים? קוד מותאם אישית. ולפחות אינטגרציה כזו לא עובדת עם אפליקציות בינה מלאכותית אחרות.

MCP מאחד את זה. שרת MCP חושף כלים עם תיאורים וסכימות ברורות. כל לקוח MCP יכול להתחבר, לגלות כלים זמינים, ולהשתמש בהם. בנה פעם אחת, השתמש בכל מקום.

הדיאגרמה מטה ממחישה את הארכיטקטורה הזו — לקוח MCP יחיד (האפליקציה שלך) מתחבר למספר שרתי MCP, שכל אחד מהם מציג סט כלים משלו דרך הפרוטוקול הסטנדרטי:

<img src="../../../translated_images/he/mcp-architecture.b3156d787a4ceac9.webp" alt="ארכיטקטורת MCP" width="800"/>

*ארכיטקטורת פרוטוקול הקשר מודל - גילוי כלים ופעולתם בסטנדרט מותאם*

## איך MCP עובד

מתחת לפני השטח, MCP משתמש בארכיטקטורה בשכבות. אפליקציית Java שלך (לקוח MCP) מגלה כלים זמינים, שולחת בקשות JSON-RPC דרך שכבת תעבורה (Stdio או HTTP), ושרת MCP מבצע פעולות ומחזיר תוצאות. הדיאגרמה הבאה מפרקת כל שכבה בפרוטוקול זה:

<img src="../../../translated_images/he/mcp-protocol-detail.01204e056f45308b.webp" alt="פרטי פרוטוקול MCP" width="800"/>

*איך MCP פועל מתחת למכסה — לקוחות מגלים כלים, מחליפים הודעות JSON-RPC, ומבצעים פעולות דרך שכבת תעבורה.*

**ארכיטקטורת שרת-לקוח**

MCP משתמש במודל לקוח-שרת. שרתים מספקים כלים — קריאת קבצים, שאילתות לבסיסי נתונים, קריאות API. לקוחות (האפליקציה שלך) מתחברים לשרתים ומשתמשים בכלים שלהם.

כדי להשתמש ב-MCP עם LangChain4j, הוסף תלות Maven זו:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**גילוי כלים**

כשלקוחך מתחבר לשרת MCP, הוא שואל "אילו כלים יש לכם?" השרת משיב עם רשימת כלים זמינים, כל אחד עם תיאורים וסכימות לפרמטרים. סוכן הבינה המלאכותית שלך יכול אז להחליט אילו כלים להשתמש בהתבסס על בקשות משתמש. הדיאגרמה מטה מראה את לחיצת היד הזו — הלקוח שולח בקשת `tools/list` והשרת מחזיר את הכלים הזמינים עם תיאורים וסכימות פרמטרים:

<img src="../../../translated_images/he/tool-discovery.07760a8a301a7832.webp" alt="גילוי כלים ב-MCP" width="800"/>

*ה-AI מגלה כלים זמינים בעת ההפעלה — הוא עכשיו יודע אילו יכולות קיימות ויכול להחליט אילו מהם להשתמש.*

**מנגנוני תעבורה**

MCP תומך במנגנוני תעבורה שונים. שתי האפשרויות הן Stdio (לתקשורת תהליכים מקומיים) ו-Streamable HTTP (לשרתים מרוחקים). מודול זה מדגים את תעבורת ה-Stdio:

<img src="../../../translated_images/he/transport-mechanisms.2791ba7ee93cf020.webp" alt="מנגנוני תעבורה" width="800"/>

*מנגנוני תעבורה ב-MCP: HTTP לשרתים מרוחקים, Stdio לתהליכים מקומיים*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

לתהליכים מקומיים. האפליקציה שלך מפעילה שרת כתהליך משנה ומתקשרת דרכו דרך קלט/פלט סטנדרטי. שימושי לגישה לקבצים או כלים לשורת הפקודה.

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

שרת `@modelcontextprotocol/server-filesystem` חושף את הכלים הבאים, כולם מוגבלים לתיקיות שתגדיר:

| כלי | תיאור |
|------|-------------|
| `read_file` | קריאת תוכן של קובץ בודד |
| `read_multiple_files` | קריאת קבצים מרובים בשיחה אחת |
| `write_file` | יצירה או דריסה של קובץ |
| `edit_file` | עריכה ממוקדת חיפוש-החלפה |
| `list_directory` | רשימת קבצים ותיקיות בנתיב |
| `search_files` | חיפוש רקורסיבי של קבצים התואמים לתבנית |
| `get_file_info` | קבלת מידע מטה-דאטה על קובץ (גודל, תאריכים, הרשאות) |
| `create_directory` | יצירת תיקייה (כולל תיקיות אב) |
| `move_file` | העברה או שינוי שם של קובץ או תיקייה |

הדיאגרמה הבאה מראה כיצד תעבורת Stdio פועלת בזמן ריצה — אפליקציית Java שלך מפעילה את שרת MCP כתהליך ילד והם מתקשרים דרך צינורות stdin/stdout, ללא רשת או HTTP:

<img src="../../../translated_images/he/stdio-transport-flow.45eaff4af2d81db4.webp" alt="זרימת תעבורת Stdio" width="800"/>

*תעבורת Stdio בפעולה — האפליקציה שלך מפעילה את שרת MCP כתהליך ילד ומתקשרת דרך צינורות stdin/stdout.*

> **🤖 נסה עם [GitHub Copilot](https://github.com/features/copilot) Chat:** פתח את [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) ושאל:
> - "איך תעבורת Stdio פועלת ומתי כדאי להשתמש בה מול HTTP?"
> - "איך LangChain4j מנהלת את מחזור החיים של תהליכי שרת MCP?"

> - "מהן ההשלכות האבטחתיות של מתן גישה לבינה המלאכותית למערכת הקבצים?"

## המודול הסוכני

בעוד MCP מספק כלים סטנדרטיים, מודול הסוכני של LangChain4j מספק דרך דקלרטיבית לבנות סוכנים שמאירים את הכלים האלה. התגית `@Agent` והשירותים `AgenticServices` מאפשרים להגדיר את התנהגות הסוכן דרך ממשקים במקום קוד אימפראטיבי.

במודול זה תחקור את תבניות סוכן המפקח (Supervisor Agent) — גישה מתקדמת סוכנית שבה "מפקח" מחליט דינמית אילו תת-סוכנים להפעיל לפי בקשות המשתמש. נשילב בין שני המושגים בכך שניתן לאחד התת-סוכנים גישת גישה לקבצים עם כלים מונעים MCP.

כדי להשתמש במודול הסוכני, הוסף תלות Maven זו:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **הערה:** מודול `langchain4j-agentic` משתמש במאפיין גרסה נפרד (`langchain4j.mcp.version`) משום שהוא משוחרר בלוח זמנים שונה מספריות הליבה של LangChain4j.

> **⚠️ ניסיוני:** מודול `langchain4j-agentic` הוא **ניסיוני** וניתן לשינויים. הדרך היציבה לבנות עוזרי AI נשארת `langchain4j-core` עם כלים מותאמים (מודול 04).

## הרצת הדוגמאות

### דרישות מוקדמות

- השלמת [מודול 04 - כלים](../04-tools/README.md) (מודול זה מבוסס על מושגי כלים מותאמים ומשווה אותם לכלי MCP)
- קובץ `.env` בתיקיית השורש עם קרדנציאליים של Azure (נוצר על ידי `azd up` במודול 01)
- Java 21+, Maven 3.9+
- Node.js 16+ ו-npm (לשרתי MCP)

> **הערה:** אם עדיין לא הגדרת את משתני הסביבה שלך, ראה [מודול 01 - מבוא](../01-introduction/README.md) להוראות הפריסה (`azd up` יוצר את הקובץ `.env` אוטומטית), או העתק את `.env.example` ל-`.env` בתיקיית השורש ומלא את הערכים שלך.

## התחלה מהירה

**בשימוש ב-VS Code:** פשוט קליק ימני על כל קובץ דמו ב-Explorer ובחר **"Run Java"**, או השתמש בקונפיגורציות ההפעלה מפאנל הריצה והדיבוג (ודא שקובץ `.env` שלך מכיל את קרדנציאליי Azure תחילה).

**בשימוש ב-Maven:** לחלופין, תוכל להריץ מהשורת פקודה עם הדוגמאות למטה.

### פעולות קבצים (Stdio)

דוגמה זו ממחישה כלים מבוססי תהליך משנה מקומי.

**✅ אין דרישות מוקדמות** - שרת MCP מופעל אוטומטית.

**בשימוש בסקריפטים להפעלה (מומלץ):**

סקריפטים אלו טוענים אוטומטית משתני סביבה מקובץ `.env` בתיקיית השורש:

**Bash:**
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-stdio.ps1
```

**בשימוש ב-VS Code:** קליק ימני על `StdioTransportDemo.java` ובחר **"Run Java"** (ודא שקובץ `.env` שלך מוגדר).

האפליקציה מפעילה שרת MCP למערכת הקבצים אוטומטית וקוראת קובץ מקומי. שים לב כיצד ניהול תהליך הילד מתבצע עבורך.

**פלט צפוי:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### סוכן מפקח

תבנית **סוכן המפקח** היא צורה **גמישה** של סוכן AI. מפקח משתמש ב-LLM כדי להחליט באופן אוטונומי אילו סוכנים להפעיל בהתבסס על בקשת המשתמש. בדוגמה הבאה, אנו משלבים גישה לקבצים מונעת MCP עם סוכן LLM כדי ליצור זרימת עבודה של קריאת קובץ → הפקת דוח בפיקוח.

בדמו, `FileAgent` קורא קובץ בעזרת כלים של מערכת הקבצים MCP, ו-`ReportAgent` מייצר דוח מובנה עם סיכום מנהלים (משפט אחד), 3 נקודות עיקריות והמלצות. המפקח מנחה את הזרימה הזו אוטומטית:

<img src="../../../translated_images/he/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="תבנית סוכן מפקח" width="800"/>

*המפקח משתמש ב-LLM שלו להחליט אילו סוכנים להפעיל ובאיזה סדר — לא נדרש ניתוב מקודד מראש.*

כך נראית זרימת העבודה הממשית עבור הפייפליין קובץ לדוח שלנו:

<img src="../../../translated_images/he/file-report-workflow.649bb7a896800de9.webp" alt="זרימת עבודה קובץ לדוח" width="800"/>

*FileAgent קורא את הקובץ דרך כלים של MCP, ואז ReportAgent ממיר את התוכן הגולמי לדוח מובנה.*

כל סוכן שומר את הפלט שלו ב**Agentic Scope** (זיכרון משותף), ומאפשר לסוכנים הבאים לגשת לתוצאות הקודמות. זה מדגים כיצד כלים של MCP משתלבים ללא תפרים בזרימות עבודה סוכניות — המפקח לא צריך לדעת *איך* הקבצים נקראים, רק ש-`FileAgent` יכול לעשות זאת.

#### הרצת הדמו

הסקריפטים טוענים אוטומטית משתני סביבה מקובץ `.env` בתיקיית השורש:

**Bash:**
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-supervisor.ps1
```

**בשימוש ב-VS Code:** קליק ימני על `SupervisorAgentDemo.java` ובחר **"Run Java"** (ודא שקובץ `.env` שלך מוגדר).

#### איך המפקח עובד

לפני בניית סוכנים, עליך לחבר את תעבורת MCP ללקוח ולעטוף זאת כ-`ToolProvider`. כך כלים של שרת MCP הופכים לזמינים לסוכניך:

```java
// צור לקוח MCP מהתחבורה
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// עטוף את הלקוח כספק כלי — זה מחבר כלים של MCP ל-LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

כעת תוכל להזריק את `mcpToolProvider` לכל סוכן שצריך כלים של MCP:

```java
// שלב 1: FileAgent קורא קבצים באמצעות כלים של MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // כולל כלים של MCP לפעולות על קבצים
        .build();

// שלב 2: ReportAgent מייצר דוחות מבניים
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// ה-Supervisor מתזמר את תהליך הקובץ → דוח
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // מחזיר את הדוח הסופי
        .build();

// ה-Supervisor מחליט אילו סוכנים להפעיל בהתאם לבקשה
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### אסטרטגיות תגובה

כשאתה מגדיר `SupervisorAgent`, אתה מציין כיצד עליו לגבש את התשובה הסופית למשתמש לאחר שהתת-סוכנים סיימו את משימותיהם. הדיאגרמה מטה מראה את שלוש האסטרטגיות הזמינות — LAST מחזיר את הפלט של הסוכן האחרון ישירות, SUMMARY מסכם את כל הפלטים דרך LLM, ו-SCORED בוחר את התוצאה שדירגה גבוה יותר מול הבקשה המקורית:

<img src="../../../translated_images/he/response-strategies.3d0cea19d096bdf9.webp" alt="אסטרטגיות תגובה" width="800"/>

*שלוש אסטרטגיות לגיבוש התגובה הסופית של המפקח — בחר לפי אם ברצונך לקבל את פלט הסוכן האחרון, סיכום מסונתז, או את האופציה עם הציון הגבוה ביותר.*

האסטרטגיות הזמינות הן:

| אסטרטגיה | תיאור |
|----------|-------------|
| **LAST** | המפקח מחזיר את הפלט של התת-סוכן או הכלי האחרון שהופעל. זה שימושי כשהסוכן הסופי בזרימת העבודה מיועד במיוחד להפיק את התשובה הסופית המלאה (למשל, "סוכן סיכום" בפייפליין מחקר). |
| **SUMMARY** | המפקח משתמש במודל השפה הפנימי שלו לסינתזת סיכום של כל האינטראקציה וכל הפלטים של התת-סוכנים, ואז מחזיר את הסיכום הזה כתשובה סופית. זה מספק תשובה נקייה ומרוכזת למשתמש. |
| **SCORED** | המערכת משתמשת ב-LLM פנימי כדי לדרג את תגובת ה-LAST ואת סיכום ה-SUMMARY מול הבקשה המקורית, ומחזירה את הפלט שקיבל את הציון הגבוה יותר. |
ראה את [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) למימוש המלא.

> **🤖 נסה עם שיחת [GitHub Copilot](https://github.com/features/copilot):** פתח את [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) ושאל:
> - "איך הממונה מחליט אילו סוכנים לזמן?"
> - "מה ההבדל בין דפוסי זרימת עבודה Supervisor ו-Sequential?"
> - "איך אני יכול להתאים אישית את התכנון של הממונה?"

#### הבנת הפלט

כשתריץ את הדמו, תראה הליכה מובנית כיצד הממונה מארגן מספר סוכנים. הנה מה שכל חלק מתכוון:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**הכותרת** מציגה את מושג זרימת העבודה: צינור ממוקד מקריאת קבצים ליצירת דוח.

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

**דיאגרמת זרימת עבודה** מראה את זרימת הנתונים בין הסוכנים. לכל סוכן יש תפקיד ספציפי:
- **FileAgent** קורא קבצים באמצעות כלים של MCP ושומר תוכן גולמי ב- `fileContent`
- **ReportAgent** צורך את התוכן הזה ומפיק דוח מובנה ב- `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**בקשת המשתמש** מראה את המשימה. הממונה מפרש זאת ומחליט לזמן FileAgent → ReportAgent.

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

**ניהול ההרכבה של הממונה** מראה את הזרימה בשני שלבים בפעולה:
1. **FileAgent** קורא את הקובץ דרך MCP ושומר את התוכן
2. **ReportAgent** מקבל את התוכן ומייצר דוח מובנה

הממונה קיבל את ההחלטות האלה **באופן אוטונומי** בהתבסס על בקשת המשתמש.

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

#### הסבר תכונות מודול Agentic

הדוגמה ממחישה כמה תכונות מתקדמות של המודול ה-Agentic. נבחן מקרוב את Agentic Scope ו-Agent Listeners.

**Agentic Scope** מראה את הזיכרון המשותף שבו הסוכנים שמרו את התוצאות שלהם באמצעות `@Agent(outputKey="...")`. זה מאפשר:
- לסוכנים מאוחרים לגשת לתוצרים של סוכנים קודמים
- לממונה לייצר תגובה סינתטית סופית
- לך לבדוק מה כל סוכן הפיק

הדיאגרמה למטה מראה כיצד עובד Agentic Scope בתור זיכרון משותף בזרימת עבודה של קובץ לדוח — FileAgent כותב את תוצרו תחת המפתח `fileContent`, ReportAgent קורא את זה וכותב תוצר תחת `report`:

<img src="../../../translated_images/he/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope מתפקד כזיכרון משותף — FileAgent כותב `fileContent`, ReportAgent קורא את זה וכותב `report`, והקוד שלך קורא את התוצאה הסופית.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // נתוני קובץ גולמיים מ-FileAgent
String report = scope.readState("report");            // דוח מובנה מ-ReportAgent
```

**Agent Listeners** מאפשרים מעקב ודיבוג של ביצוע הסוכנים. הפלט צעד-אחר-צעד שאתה רואה בדמו מגיע מ-AgentListener שנקשר לכל קריאה של סוכן:
- **beforeAgentInvocation** - נקרא כאשר הממונה בוחר סוכן, כדי שתוכל לראות איזה סוכן נבחר ולמה
- **afterAgentInvocation** - נקרא כאשר סוכן מסתיים, ומציג את תוצאתו
- **inheritedBySubagents** - כשנכון, המאזין עוקב אחרי כל הסוכנים בהיררכיה

הדיאגרמה הבאה מראה את מחזור החיים המלא של Agent Listener, כולל איך `onError` מטפל בכשלונות בעת ביצוע סוכן:

<img src="../../../translated_images/he/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners נקשרים למחזור חיי הביצוע — עוקבים אחרי מתי סוכנים מתחילים, מסתיימים או נתקלים בשגיאות.*

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

מעבר לדפוס Supervisor, המודול `langchain4j-agentic` מספק כמה דפוסי זרימת עבודה חזקים. הדיאגרמה למטה מציגה את כולן — מצינורות רציפים פשוטים ועד זרימות עבודה עם ביקורת אדם-במעגל:

<img src="../../../translated_images/he/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*חמישה דפוסי זרימת עבודה לארגון סוכנים — מצינורות רציפים פשוטים ועד זרימות עבודה עם אישור אדם-במעגל.*

| דפוס | תיאור | מקרה שימוש |
|---------|-------------|----------|
| **Sequential** | ביצוע סוכנים לפי סדר, פלט זורם לסוכן הבא | צינורות: מחקר → ניתוח → דוח |
| **Parallel** | הרצת סוכנים במקביל | משימות עצמאיות: מזג אוויר + חדשות + מניות |
| **Loop** | חזרה עד שתגובת תנאי מתקיים | דירוג איכות: לדייק עד ציונים ≥ 0.8 |
| **Conditional** | ניתוב בהתאם לתנאים | סיווג → ניתוב לסוכן מומחה |
| **Human-in-the-Loop** | הוספת נקודות ביקורת אדם | זרימות אישור, סקירת תוכן |

## מושגים מרכזיים

עכשיו כשחקרת את MCP ואת מודול ה-Agentic בפעולה, נסכם מתי להשתמש בכל גישה.

אחת היתרונות הגדולים של MCP היא האקוסיסטם המתפתח שלה. הדיאגרמה למטה מראה איך פרוטוקול אוניברסלי יחיד מחבר את אפליקציית הבינה המלאכותית שלך למגוון רחב של שרתי MCP — מגישה למערכת קבצים ומסד נתונים ל-GitHub, אימייל, דף אינטרנט ועוד:

<img src="../../../translated_images/he/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP יוצר אקוסיסטם של פרוטוקולים אוניברסליים — כל שרת תואם MCP עובד עם כל לקוח תואם MCP, ומאפשר שיתוף כלים בין אפליקציות.*

**MCP** אידיאלי כשרוצים לנצל אקוסיסטם קיימת של כלים, לבנות כלים שחולקים אותם מספר אפליקציות, לשלב שירותים חיצוניים באמצעות פרוטוקולים סטנדרטיים, או להחליף יישומי כלים בלי לשנות קוד.

**מודול ה-Agentic** מתאים ביותר כשאתה רוצה הגדרות סוכנים דקלרטיביות עם אנוטציות `@Agent`, צריך ניהול זרימת עבודה (רציף, לולאה, מקביל), מעדיף עיצוב סוכן מבוסס ממשק על פני קוד אימפרטיבי, או משלב כמה סוכנים שמשתפים תוצרים דרך `outputKey`.

**דפוס סוכן Supervisor** בולט כשזרימת העבודה אינה צפויה מראש ואתה רוצה שה-LLM יחליט, כשיש לך סוכנים מומחים רבים שצריכים ארגון דינמי, כשבונים מערכות שיח שמפנות ליכולות שונות, או כשרוצים את ההתנהגות הסוכנת הגמישה והמתאימה ביותר.

כדי לסייע לך להחליט בין שיטות `@Tool` מותאמות מ-Module 04 לכלי MCP מהמודול הזה, ההשוואה הבאה מדגישה את התשלומים המרכזיים — כלים מותאמים מעניקים קישור הדוק ובטיחות טיפוס מלאה עבור לוגיקה ספציפית לאפליקציה, בעוד שכלי MCP מציעים אינטגרציות סטנדרטיות שניתנות לשימוש חוזר:

<img src="../../../translated_images/he/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*מתי להשתמש בשיטות @Tool מותאמות לעומת כלי MCP — כלים מותאמים ללוגיקה ייעודית עם בטיחות טיפוס מלאה, כלי MCP לאינטגרציות סטנדרטיות שמשתפות פעולה בין אפליקציות.*

## ברכות!

סיימת את כל חמשת המודולים של קורס LangChain4j למתחילים! הנה מבט על המסע המלא שסיימת — מצ׳אט בסיסי ועד מערכות Agentic מונעות MCP:

<img src="../../../translated_images/he/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*מסע הלמידה שלך בכל חמשת המודולים — מצ׳אט בסיסי ועד מערכות Agentic מונעות MCP.*

סיימת את קורס LangChain4j למתחילים. למדת:

- איך לבנות בינה מלאכותית שיח עם זיכרון (Module 01)
- דפוסי הנדסת פרומפט למשימות שונות (Module 02)
- עיגון תגובות במסמכים שלך עם RAG (Module 03)
- יצירת סוכני AI בסיסיים (עוזרים) עם כלים מותאמים (Module 04)
- שילוב כלים סטנדרטיים עם מודולי LangChain4j MCP ו-Agentic (Module 05)

### מה הלאה?

לאחר שסיימת את המודולים, גש ל-[מדריך הבדיקות](../docs/TESTING.md) כדי לראות את מושגי הבדיקות של LangChain4j בפעולה.

**משאבים רשמיים:**
- [תיעוד LangChain4j](https://docs.langchain4j.dev/) - מדריכים מקיפים ומסמכי API
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - קוד מקור ודוגמאות
- [מדריכי LangChain4j](https://docs.langchain4j.dev/tutorials/) - מדריכי שלב-אחר-שלב למקרי שימוש שונים

תודה שסיימת את הקורס הזה!

---

**ניווט:** [← קודם: מודול 04 - כלים](../04-tools/README.md) | [חזרה לעמוד הראשי](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב ויתור**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדיוק, יש לקחת בחשבון שתרגומים ממוחשבים עשויים להכיל שגיאות או אי דיוקים. יש להתייחס למסמך המקורי בשפת המקור כמקור הסמכותי. למידע קריטי מומלץ להשתמש בתרגום מקצועי על ידי מתרגם אנושי. אנו לא אחראים לכל אי הבנה או פירוש שגוי הנובע משימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->