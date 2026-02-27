# מודול 05: פרוטוקול הקשר המודל (MCP)

## תוכן העניינים

- [מה תלמד](../../../05-mcp)
- [מה זה MCP?](../../../05-mcp)
- [איך MCP עובד](../../../05-mcp)
- [המודול האג'נטי](../../../05-mcp)
- [הרצת הדוגמאות](../../../05-mcp)
  - [דרישות מוקדמות](../../../05-mcp)
- [התחלה מהירה](../../../05-mcp)
  - [פעולות על קבצים (Stdio)](../../../05-mcp)
  - [סוכן המפקח](../../../05-mcp)
    - [הרצת הדמו](../../../05-mcp)
    - [איך המפקח עובד](../../../05-mcp)
    - [אסטרטגיות תגובה](../../../05-mcp)
    - [הבנת הפלט](../../../05-mcp)
    - [הסבר על תכונות המודול האג'נטי](../../../05-mcp)
- [מושגים מרכזיים](../../../05-mcp)
- [מזל טוב!](../../../05-mcp)
  - [מה הלאה?](../../../05-mcp)

## מה תלמד

כבר בנית בינה מלאכותית שיחתית, שלטת בפרומפטים, מבוססת תגובות על מסמכים ויצרת סוכנים עם כלים. אבל כל הכלים האלה היו מותאמים באופן ייחודי עבור האפליקציה הספציפית שלך. מה אם היית יכול לתת לבינה המלאכותית שלך גישה לאקוסיסטם סטנדרטי של כלים שכל אחד יכול ליצור ולשתף? במודול זה תלמד כיצד לעשות זאת באמצעות פרוטוקול הקשר המודל (MCP) והמודול האג'נטי של LangChain4j. ראשית נציג קורא קבצים פשוט ב-MCP ואז נראה איך הוא משתלב בקלות עם תזרימי עבודה אג'נטים מתקדמים באמצעות תבנית סוכן המפקח.

## מה זה MCP?

פרוטוקול הקשר המודל (MCP) מספק בדיוק את זה – דרך סטנדרטית לאפליקציות בינה מלאכותית לגלות ולהשתמש בכלים חיצוניים. במקום לכתוב אינטגרציות מותאמות לכל מקור נתונים או שירות, אתה מתחבר לשרתי MCP שמציגים את היכולות שלהם בפורמט אחיד. הסוכן הבינה המלאכותית שלך יכול אז לגלות ולהשתמש בכלים האלה אוטומטית.

<img src="../../../translated_images/he/mcp-comparison.9129a881ecf10ff5.webp" alt="השוואת MCP" width="800"/>

*לפני MCP: אינטגרציות מורכבות מנקודה לנקודה. אחרי MCP: פרוטוקול אחד, אפשרויות אינסופיות.*

MCP פותר בעיה יסודית בפיתוח בינה מלאכותית: כל אינטגרציה היא מותאמת אישית. רוצים לגשת ל-GitHub? קוד מותאם. רוצים לקרוא קבצים? קוד מותאם. רוצים לשאול מסד נתונים? קוד מותאם. ואף אחת מהאינטגרציות האלה לא עובדת עם אפליקציות בינה מלאכותית אחרות.

MCP מסטנדרט את זה. שרת MCP מציג כלים עם תיאורים ברורים וסכימות. כל לקוח MCP יכול להתחבר, לגלות את הכלים הזמינים ולהשתמש בהם. בונה פעם אחת, משתמש בכל מקום.

<img src="../../../translated_images/he/mcp-architecture.b3156d787a4ceac9.webp" alt="ארכיטקטורת MCP" width="800"/>

*ארכיטקטורת פרוטוקול הקשר המודל - גילוי והרצת כלים סטנדרטיים*

## איך MCP עובד

<img src="../../../translated_images/he/mcp-protocol-detail.01204e056f45308b.webp" alt="פרטי פרוטוקול MCP" width="800"/>

*איך MCP עובד מתחת למכסה המנוע — לקוחות מגלים כלים, מחליפים הודעות JSON-RPC ומבצעים פעולות דרך שכבת נשיאה.*

**ארכיטקטורת שרת-לקוח**

MCP משתמש במודל שרת-לקוח. השרתים מספקים כלים – קריאת קבצים, שאילתות למסדי נתונים, קריאה ל-APIs. הלקוחות (אפליקציית הבינה המלאכותית שלך) מתחברים לשרתים ומשתמשים בכלים שלהם.

כדי להשתמש ב-MCP עם LangChain4j, הוסף את התלות הבאה ב-Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**גילוי כלים**

כאשר הלקוח שלך מתחבר לשרת MCP, הוא שואל "אילו כלים יש לכם?" השרת משיב ברשימת כלים זמינים, כל אחד עם תיאורים וסכימות פרמטרים. הסוכן הבינה המלאכותית שלך יכול אז להחליט אילו כלים להשתמש על סמך בקשות המשתמש.

<img src="../../../translated_images/he/tool-discovery.07760a8a301a7832.webp" alt="גילוי כלים ב-MCP" width="800"/>

*הבינה המלאכותית מגלה כלים זמינים בעת התחלה — עכשיו היא יודעת אילו יכולות זמינות ויכולה להחליט אילו להשתמש.*

**מנגנוני נשיאה**

MCP תומך במנגנוני נשיאה שונים. מודול זה מדגים את נשיאת Stdio עבור תהליכים מקומיים:

<img src="../../../translated_images/he/transport-mechanisms.2791ba7ee93cf020.webp" alt="מנגנוני נשיאה" width="800"/>

*מנגנוני נשיאת MCP: HTTP לשרתי מרוחק, Stdio לתהליכים מקומיים*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

לתהליכים מקומיים. האפליקציה שלך מפעילה שרת כתת-תהליך ומתקשרת דרך קלט/פלט סטנדרטי. שימושי לגישה למערכת הקבצים או כלי שורת פקודה.

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

<img src="../../../translated_images/he/stdio-transport-flow.45eaff4af2d81db4.webp" alt="זרימת נשיאת Stdio" width="800"/>

*נשיאת Stdio בפעולה — האפליקציה שלך מפעילה את שרת MCP כתהליך ילד ומתקשרת דרך צינורות stdin/stdout.*

> **🤖 נסה עם [GitHub Copilot](https://github.com/features/copilot) Chat:** פתח את [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) ושאל:
> - "איך עובדת נשיאת Stdio ומתי כדאי להשתמש בה במקום HTTP?"
> - "איך LangChain4j מנהל את מחזור החיים של תהליכי שרת MCP שיוצרים?"
> - "מהן ההשלכות הביטחוניות של מתן גישה לבינה המלאכותית למערכת הקבצים?"

## המודול האג'נטי

בעוד MCP מספק כלים סטנדרטיים, מודול ה-agentic של LangChain4j מספק דרך דקלרטיבית לבניית סוכנים שמתזמרים את הכלים האלה. ההערה `@Agent` ו-`AgenticServices` מאפשרות להגדיר התנהגות סוכן דרך ממשקים במקום קוד אימפרטיבי.

במודול זה תחקור את תבנית ה-**סוכן המפקח** — גישה מתקדמת של AI אג'נטי שבה סוכן "מפקח" מחליט באופן דינמי אילו תת-סוכנים להפעיל בהתבסס על בקשות המשתמש. נשילב את שני המושגים בכך שנעניק לאחד התת-סוכנים שלנו יכולות גישה לקבצים המופעלות על ידי MCP.

כדי להשתמש במודול האג'נטי, הוסף את התלות הזו במייבן:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **⚠️ ניסיוני:** מודול `langchain4j-agentic` הוא **ניסיוני** ועשוי להשתנות. הדרך היציבה לבניית עוזרי AI נותרת באמצעות `langchain4j-core` עם כלים מותאמים אישית (מודול 04).

## הרצת הדוגמאות

### דרישות מוקדמות

- Java 21+, Maven 3.9+
- Node.js 16+ ו-npm (לשרתי MCP)
- משתני סביבה מוגדרים בקובץ `.env` (מתיקיית השורש):
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (כמו במודולים 01-04)

> **הערה:** אם עדיין לא הגדרת את משתני הסביבה שלך, ראה [מודול 00 - התחלה מהירה](../00-quick-start/README.md) להוראות, או העתק `.env.example` ל-`.env` בתיקיית השורש ומלא את הערכים שלך.

## התחלה מהירה

**שימוש ב-VS Code:** פשוט לחץ קליק ימני על קובץ הדמו כלשהו בתוך ה-Explorer ובחר **"Run Java"**, או השתמש בקונפיגורציות ההפעלה מפאנל Run and Debug (ודא שהוספת את הטוקן שלך לקובץ `.env` קודם).

**שימוש ב-Maven:** לחלופין, תוכל להריץ מהשורת הפקודה עם הדוגמאות למטה.

### פעולות על קבצים (Stdio)

כביטוי לכלים מבוססי תת-תהליך מקומי.

**✅ אין צורך בדרישות מוקדמות** - שרת MCP מופעל אוטומטית.

**שימוש בסקריפטים להפעלה (מומלץ):**

סקריפטים להפעלה טוענים באופן אוטומטי משתני סביבה מקובץ ה-.env בשורש:

**בוש:**
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```

**פאוורשלים:**
```powershell
cd 05-mcp
.\start-stdio.ps1
```

**שימוש ב-VS Code:** לחץ קליק ימני על `StdioTransportDemo.java` ובחר **"Run Java"** (ודא שקובץ ה-.env שלך מוגדר).

האפליקציה מפעילה את שרת ה-MCP של מערכת הקבצים אוטומטית וקוראת קובץ מקומי. שים לב איך ניהול תת-ההליך מתבצע עבורך.

**פלט צפוי:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### סוכן המפקח

תבנית ה-**סוכן המפקח** היא צורת AI אג'נטי **גמישה**. מפקח משתמש במודל שפה גדול (LLM) כדי להחליט באופן עצמאי אילו סוכנים להפעיל בהתבסס על בקשת המשתמש. בדוגמה הבאה, אנו משלבים גישה לקבצים מופעלת בעזרת MCP עם סוכן LLM ליצירת תזרים עבודה של קריאה לדוח מבוקר.

בדמו, `FileAgent` קורא קובץ באמצעות כלים של מערכת הקבצים ב-MCP, ו-`ReportAgent` מייצר דוח מובנה עם סיכום מנכ"ל (משפט אחד), 3 נקודות עיקריות והמלצות. המפקח מתזמר את התהליך הזה אוטומטית:

<img src="../../../translated_images/he/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="דפוס סוכן המפקח" width="800"/>

*המפקח משתמש ב-LLM שלו כדי להחליט אילו סוכנים להפעיל ובאיזה סדר — אין צורך בנתיבי הפעלה קשיחים.*

ככה נראה תזרימי העבודה הקונקרטי לצינור קובץ לדוח:

<img src="../../../translated_images/he/file-report-workflow.649bb7a896800de9.webp" alt="תזרימי עבודה מקובץ לדוח" width="800"/>

*FileAgent קורא את הקובץ דרך כלים של MCP, ואז ReportAgent ממיר את התוכן הגולמי לדוח מובנה.*

כל סוכן שומר את הפלט שלו ב-**Agentic Scope** (זיכרון משותף), מה שמאפשר לסוכנים הבאים לגשת לתוצאות הקודמות. זה מראה איך כלים של MCP משתלבים בצורה חלקה בתזרימי עבודה אג'נטיים — למפקח אין צורך לדעת *איך* קוראים קבצים, רק ש-`FileAgent` יכול לעשות זאת.

#### הרצת הדמו

סקריפטים להפעלה טוענים אוטומטית את משתני הסביבה מקובץ ה-.env בשורש:

**בוש:**
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```

**פאוורשלים:**
```powershell
cd 05-mcp
.\start-supervisor.ps1
```

**שימוש ב-VS Code:** לחץ קליק ימני על `SupervisorAgentDemo.java` ובחר **"Run Java"** (ודא שקובץ ה-.env שלך מוגדר).

#### איך המפקח עובד

```java
// שלב 1: FileAgent קורא קבצים באמצעות כלים של MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // כולל כלים של MCP עבור פעולות בקבצים
        .build();

// שלב 2: ReportAgent יוצר דוחות מובנים
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// הממונה מארגן את זרימת העבודה מקובץ לדוח
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // מחזיר את הדוח הסופי
        .build();

// הממונה מחליט אילו סוכנים להפעיל בהתבסס על הבקשה
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### אסטרטגיות תגובה

כשאתה מגדיר `SupervisorAgent`, אתה מציין איך עליו לנסח את התשובה הסופית למשתמש לאחר שהתת-סוכנים סיימו את המשימות שלהם.

<img src="../../../translated_images/he/response-strategies.3d0cea19d096bdf9.webp" alt="אסטרטגיות תגובה" width="800"/>

*שלוש אסטרטגיות לאיך המפקח מנוסח את התגובה הסופית — בחר בהתאם אם ברצונך לקבל את הפלט של הסוכן האחרון, סיכום מסונתז, או את האפשרות עם הציון הגבוה ביותר.*

האסטרטגיות הזמינות הן:

| אסטרטגיה | תיאור |
|----------|-------------|
| **LAST** | המפקח מחזיר את הפלט של תת-הסוכן או הכלי האחרון שהופעל. זה שימושי כשסוכן הסיום בתזרים העבודה מתוכנן במיוחד לייצר את התשובה הסופית והשלמה (למשל, "סוכן סיכום" בצינור מחקר). |
| **SUMMARY** | המפקח משתמש במודל השפה הפנימי (LLM) שלו כדי ליצור סיכום של כל האינטראקציה וכל פלטי תת-הסוכנים, ואז מחזיר את הסיכום כתשובה הסופית. זה מספק תשובה מקובצת ונקייה למשתמש. |
| **SCORED** | המערכת משתמשת ב-LLM פנימי כדי לדרג הן את התגובה האחרונה והן את סיכום האינטראקציה מול בקשת המשתמש המקורית, ומחזירה את הפלט שקיבל את הדירוג הגבוה יותר. |

עיין ב-[SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) למימוש המלא.

> **🤖 נסה עם [GitHub Copilot](https://github.com/features/copilot) Chat:** פתח את [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) ושאל:
> - "איך המפקח מחליט אילו סוכנים להפעיל?"
> - "מה ההבדל בין תבניות מפקח ותבניות תזרים עבודה סדרתי?"
> - "איך אפשר להתאים אישית את התכנון של המפקח?"

#### הבנת הפלט

כאשר אתה מריץ את הדמו, תראה סיקור מובנה של איך המפקח מתזמר מספר סוכנים. הנה מה כל חלק אומר:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**הכותרת** מציגה את רעיון תזרים העבודה: צינור ממוקד מקריאת קובץ ליצירת דוח.

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

**תרשים תזרים עבודה** מראה את זרימת הנתונים בין הסוכנים. לכל סוכן יש תפקיד ספציפי:
- **FileAgent** קורא קבצים באמצעות כלים של MCP ושומר את התוכן הגולמי ב-`fileContent`
- **ReportAgent** צורך את התוכן הזה ומייצר דוח מובנה ב-`report`

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

**תזמור המפקח** מראה את זרימת שני השלבים בפעולה:
1. **FileAgent** קורא את הקובץ דרך MCP ושומר את התוכן
2. **ReportAgent** מקבל את התוכן ומייצר דוח מובנה

המפקח קיבל את ההחלטות הללו **באופן עצמאי** בהתבסס על בקשת המשתמש.

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

#### הסבר על תכונות המודול האג'נטי

הדוגמא מדגימה מספר תכונות מתקדמות של המודול האג'נטי. נסתכל מקרוב על Agentic Scope ומאזיני סוכן.

**Agentic Scope** מציגה את הזיכרון המשותף שבו הסוכנים שמרו את התוצאות שלהם באמצעות `@Agent(outputKey="...")`. זה מאפשר:
- לסוכנים מאוחרים יותר לגשת לפלטים של סוכנים מוקדמים
- למפקח ליצור סינתזה של התגובה הסופית
- לך לבדוק מה כל סוכן הפיק

<img src="../../../translated_images/he/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope זיכרון משותף" width="800"/>

*Agentic Scope מתפקד כזיכרון משותף — FileAgent כותב ל-`fileContent`, ReportAgent קורא זאת וכותב ל-`report`, והקוד שלך קורא את התוצאה הסופית.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // נתוני קובץ גלם מ-FileAgent
String report = scope.readState("report");            // דוח ממוסד מ-ReportAgent
```

**Agent Listeners** מאפשרים ניטור ודיבאג של ביצועי סוכן. הפלט הצעד-אחר-צעד שאתה רואה בדמו מגיע מ-AgentListener שמתחבר לכל קריאת סוכן:
- **beforeAgentInvocation** - נקרא כאשר המפקח בוחר סוכן, מאפשר לראות איזה סוכן נבחר ולמה  
- **afterAgentInvocation** - נקרא כאשר סוכן משלים, מציג את התוצאה שלו  
- **inheritedBySubagents** - כאשר נכון, המאזין עוקב אחרי כל הסוכנים בהיררכיה  

<img src="../../../translated_images/he/agent-listeners.784bfc403c80ea13.webp" alt="מחזור החיים של מאזיני סוכן" width="800"/>

*מאזיני סוכנים מחוברים למחזור החיים של ההרצה — עוקבים מתי סוכנים מתחילים, משלימים או נתקלים בשגיאות.*

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
  
מעבר לתבנית המפקח, מודול `langchain4j-agentic` מספק מספר תבניות תזרימי עבודה ותכונות חזקות:

<img src="../../../translated_images/he/workflow-patterns.82b2cc5b0c5edb22.webp" alt="תבניות תזרימי עבודה של סוכן" width="800"/>

*חמש תבניות תזרימי עבודה לאירגון סוכנים — מצנרת סדרתית פשוטה ועד תזרימי אישור עם מעורבות אדם.*

| תבנית | תיאור | מקרה שימוש |
|---------|-------------|----------|
| **סדרתי** | להריץ סוכנים בסדר, הפלט זורם לסוכן הבא | צנרות: מחקר → ניתוח → דיווח |
| **מקביל** | להריץ סוכנים בו-זמנית | משימות עצמאיות: מזג אוויר + חדשות + מניות |
| **לולאה** | לחזור עד שמתקיים תנאי | דירוג איכות: לשפר עד ניקוד ≥ 0.8 |
| **תנאי** | לסנן לפי תנאים | סיווג → לשלוח לסוכן מומחה |
| **מעורבות אדם** | להוסיף נקודות ביקורת אנושיות | תזרימי אישור, סקירת תוכן |

## מושגים מרכזיים

כעת כשחקרתם את MCP ואת מודול הסוכנים בפעולה, נסכם מתי להשתמש בכל גישה.

<img src="../../../translated_images/he/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="אקוסיסטם MCP" width="800"/>

*MCP יוצר אקוסיסטם פרוטוקול אוניברסלי — כל שרת התואם MCP עובד עם כל לקוח התואם MCP, מה שמאפשר שיתוף כלים בין יישומים שונים.*

**MCP** מתאים כשאתם רוצים לנצל אקוסיסטם כלים קיימת, לבנות כלים שיכולים לשמש מספר יישומים, לשלב שירותים צד שלישי עם פרוטוקולים סטנדרטיים, או להחליף מימושי כלים ללא שינוי בקוד.

**מודול הסוכנים** הכי מתאים כשאתם רוצים הגדרות סוכנים הצהרתיות עם הערות `@Agent`, צריכים תזמור תזרימי עבודה (סדרתי, לולאה, מקביל), מעדיפים עיצוב סוכן מבוסס ממשק על פני קוד אימפרטיבי, או משלבים מספר סוכנים שמשתפים פלט דרך `outputKey`.

**תבנית סוכן המפקח** זוהרת כאשר תזרימי העבודה אינם צפויים מראש ואתם רוצים שה-LLM יחליט, כשיש לכם מספר סוכנים ממוקדים שדורשים תזמור דינמי, כשבונים מערכות שיחה שמפנות ליכולות שונות, או כשאתם רוצים את ההתנהגות הגמישה והמתאימה ביותר של הסוכן.

<img src="../../../translated_images/he/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="כלים מותאמים מול כלים MCP" width="800"/>

*מתי להשתמש בשיטות @Tool מותאמות לעומת כלים MCP — כלים מותאמים ללוגיקה ספציפית ליישום עם בטיחות טיפוסים מלאה, כלים MCP לאינטגרציות סטנדרטיות הפועלות בין יישומים.*

## מזל טוב!

<img src="../../../translated_images/he/course-completion.48cd201f60ac7570.webp" alt="סיום הקורס" width="800"/>

*מסע הלמידה שלך דרך כל חמשת המודולים — משיחה בסיסית ועד מערכות סוכנים מופעלות MCP.*

השלמת את קורס LangChain4j למתחילים. למדת:

- איך לבנות בינה מלאכותית שיחתית עם זיכרון (מודול 01)  
- תבניות הנדסת פקודות למשימות שונות (מודול 02)  
- עיגון תגובות במסמכים עם RAG (מודול 03)  
- יצירת סוכני AI בסיסיים (עוזרים) עם כלים מותאמים (מודול 04)  
- שילוב כלים סטנדרטיים עם מודולי LangChain4j MCP ו-Agentic (מודול 05)  

### מה הלאה?

לאחר שסיימת את המודולים, חקור את [מדריך הבדיקה](../docs/TESTING.md) כדי לראות את מושגי הבדיקה של LangChain4j בפעולה.

**משאבים רשמיים:**  
- [תיעוד LangChain4j](https://docs.langchain4j.dev/) - מדריכים מקיפים ו-reference API  
- [GitHub של LangChain4j](https://github.com/langchain4j/langchain4j) - קוד מקור ודוגמאות  
- [הדרכות LangChain4j](https://docs.langchain4j.dev/tutorials/) - הדרכות שלב-אחר-שלב למגוון מקרים  

תודה שהשלמתם את הקורס!

---

**ניווט:** [← קודם: מודול 04 - כלים](../04-tools/README.md) | [חזרה לעמוד הראשי](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב ויתר**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדיוק, שימו לב כי תרגומים אוטומטיים עלולים להכיל שגיאות או אי-דיוקים. יש להתייחס למסמך המקורי בשפתו המקורית כסמכות המאשרת. למידע קריטי מומלץ להשתמש בתרגום מקצועי מבוצע בידי אדם. אנו לא נושאים באחריות על כל אי-הבנה או פרשנות שגויה הנובעת משימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->