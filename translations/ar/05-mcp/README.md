# الوحدة 05: بروتوكول سياق النموذج (MCP)

## جدول المحتويات

- [ما سوف تتعلمه](../../../05-mcp)
- [ما هو MCP؟](../../../05-mcp)
- [كيف يعمل MCP](../../../05-mcp)
- [الوحدة الوكيلية](../../../05-mcp)
- [تشغيل الأمثلة](../../../05-mcp)
  - [المتطلبات الأساسية](../../../05-mcp)
- [البدء السريع](../../../05-mcp)
  - [عمليات الملفات (Stdio)](../../../05-mcp)
  - [وكيل المشرف](../../../05-mcp)
    - [تشغيل العرض التوضيحي](../../../05-mcp)
    - [كيف يعمل المشرف](../../../05-mcp)
    - [استراتيجيات الاستجابة](../../../05-mcp)
    - [فهم المخرجات](../../../05-mcp)
    - [شرح ميزات الوحدة الوكيلية](../../../05-mcp)
- [المفاهيم الرئيسية](../../../05-mcp)
- [تهانينا!](../../../05-mcp)
  - [ما التالي؟](../../../05-mcp)

## ما سوف تتعلمه

لقد قمت ببناء ذكاء اصطناعي محادثي، وتعلمت كيفية التحكم في النصوص، وربط الردود بالمستندات، وإنشاء وكلاء مزودين بأدوات. لكن كل هذه الأدوات كانت مخصصة لتطبيقك الخاص. ماذا لو استطعت منح ذكاءك الاصطناعي حق الوصول إلى نظام بيئي موحد للأدوات يمكن لأي شخص إنشاءه ومشاركته؟ في هذه الوحدة، ستتعلم كيف تفعل ذلك بالضبط باستخدام بروتوكول سياق النموذج (MCP) والوحدة الوكيلية في LangChain4j. نبدأ بعرض قارئ ملفات MCP بسيط ثم نوضح كيف يندمج بسهولة في تدفقات العمل الوكيلية المتقدمة باستخدام نمط وكيل المشرف.

## ما هو MCP؟

يوفر بروتوكول سياق النموذج (MCP) هذا بالضبط - طريقة موحدة لتطبيقات الذكاء الاصطناعي لاكتشاف واستخدام الأدوات الخارجية. بدلاً من كتابة تكاملات مخصصة لكل مصدر بيانات أو خدمة، فإنك تتصل بخوادم MCP التي تعرض إمكانياتها بتنسيق متسق. يمكن لوكيل الذكاء الاصطناعي الخاص بك بعد ذلك اكتشاف هذه الأدوات واستخدامها تلقائيًا.

<img src="../../../translated_images/ar/mcp-comparison.9129a881ecf10ff5.webp" alt="مقارنة MCP" width="800"/>

*قبل MCP: تكاملات معقدة من نقطة إلى نقطة. بعد MCP: بروتوكول واحد، إمكانات لا نهائية.*

يحل MCP مشكلة أساسية في تطوير الذكاء الاصطناعي: كل تكامل مخصص. تريد الوصول إلى GitHub؟ كود مخصص. تريد قراءة الملفات؟ كود مخصص. تريد الاستعلام من قاعدة بيانات؟ كود مخصص. ولا تعمل أي من هذه التكاملات مع تطبيقات الذكاء الاصطناعي الأخرى.

يقوم MCP بتوحيد هذا. يكشف خادم MCP عن الأدوات مع أوصاف واضحة ومخططات. يمكن لأي عميل MCP الاتصال، اكتشاف الأدوات المتاحة، واستخدامها. بناء مرة واحدة، الاستخدام في كل مكان.

<img src="../../../translated_images/ar/mcp-architecture.b3156d787a4ceac9.webp" alt="بنية MCP" width="800"/>

*بنية بروتوكول سياق النموذج - اكتشاف وتنفيذ الأدوات الموحدة*

## كيف يعمل MCP

<img src="../../../translated_images/ar/mcp-protocol-detail.01204e056f45308b.webp" alt="تفاصيل بروتوكول MCP" width="800"/>

*كيف يعمل MCP تحت الغطاء — يكتشف العملاء الأدوات، يتبادلون رسائل JSON-RPC، وينفذون العمليات عبر طبقة النقل.*

**بنية الخادم-العميل**

يستخدم MCP نموذج الخادم-العميل. توفر الخوادم الأدوات - قراءة الملفات، الاستعلام من قواعد البيانات، استدعاء واجهات برمجة التطبيقات. يتصل العملاء (تطبيق الذكاء الاصطناعي الخاص بك) بالخوادم ويستخدمون أدواتها.

لاستخدام MCP مع LangChain4j، أضف هذا التابع إلى Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**اكتشاف الأدوات**

عندما يتصل عميلك بخادم MCP، يسأل "ما الأدوات التي لديك؟" يرد الخادم بقائمة الأدوات المتاحة، كل منها مع الأوصاف ومخططات المعلمات. يمكن لوكيل الذكاء الاصطناعي الخاص بك بعد ذلك تحديد الأدوات التي يجب استخدامها بناءً على طلبات المستخدم.

<img src="../../../translated_images/ar/tool-discovery.07760a8a301a7832.webp" alt="اكتشاف أدوات MCP" width="800"/>

*يكتشف الذكاء الاصطناعي الأدوات المتاحة عند بدء التشغيل — يعرف الآن الإمكانات المتوفرة ويمكنه تقرير أيها يستخدم.*

**آليات النقل**

يدعم MCP آليات نقل مختلفة. توضح هذه الوحدة النقل عبر Stdio للعمليات المحلية:

<img src="../../../translated_images/ar/transport-mechanisms.2791ba7ee93cf020.webp" alt="آليات النقل" width="800"/>

*آليات نقل MCP: HTTP للخوادم البعيدة، Stdio للعمليات المحلية*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

لعمليات محلية. يطلق تطبيقك خادمًا كعملية فرعية ويتواصل عبر الإدخال/الإخراج القياسي. مفيد للوصول إلى نظام الملفات أو أدوات سطر الأوامر.

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

<img src="../../../translated_images/ar/stdio-transport-flow.45eaff4af2d81db4.webp" alt="تدفق نقل Stdio" width="800"/>

*نقل Stdio قيد التنفيذ — يطلق تطبيقك خادم MCP كعملية طفل ويتواصل عبر أنابيب stdin/stdout.*

> **🤖 جرب مع دردشة [GitHub Copilot](https://github.com/features/copilot):** افتح [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) واسأل:
> - "كيف يعمل نقل Stdio ومتى يجب أن أستخدمه مقابل HTTP؟"
> - "كيف يدير LangChain4j دورة حياة عمليات خادم MCP التي يتم إطلاقها؟"
> - "ما هي تبعات الأمان لمنح الذكاء الاصطناعي حق الوصول إلى نظام الملفات؟"

## الوحدة الوكيلية

بينما يوفر MCP أدوات موحدة، توفر وحدة LangChain4j **الوكيلية** طريقة إعلانية لبناء وكلاء ينظمون تلك الأدوات. تسمح لك التوجيه `@Agent` وخدمات `AgenticServices` بتعريف سلوك الوكيل عبر واجهات بدلاً من الكود الإلزامي.

في هذه الوحدة، ستستكشف نمط **وكيل المشرف** — نهج ذكاء اصطناعي وكيل متقدم حيث يقرر الوكيل "المشرف" ديناميكيًا الوكلاء الفرعيين الذين يتم استدعاؤهم بناءً على طلبات المستخدم. سنجمع بين كلا المفهومين بإعطاء أحد وكلائنا الفرعيين إمكانات وصول الملفات مدعومة بـ MCP.

لاستخدام الوحدة الوكيلية، أضف هذا التابع إلى Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **⚠️ تجريبي:** وحدة `langchain4j-agentic` هي **تجريبية** وقابلة للتغيير. الطريقة المستقرة لبناء مساعدين ذكاء اصطناعي تبقى باستخدام `langchain4j-core` مع أدوات مخصصة (الوحدة 04).

## تشغيل الأمثلة

### المتطلبات الأساسية

- جافا 21 فأعلى، مافن 3.9 فأعلى
- Node.js 16 فأعلى وnpm (لسيرفرات MCP)
- متغيرات البيئة مضبوطة في ملف `.env` (من الدليل الجذري):
  - `AZURE_OPENAI_ENDPOINT`، `AZURE_OPENAI_API_KEY`، `AZURE_OPENAI_DEPLOYMENT` (نفس إعدادات الوحدات 01-04)

> **ملاحظة:** إذا لم تقم بضبط متغيرات البيئة بعد، انظر [الوحدة 00 - البدء السريع](../00-quick-start/README.md) للحصول على التعليمات، أو انسخ `.env.example` إلى `.env` في الدليل الجذري واملأ القيم.

## البدء السريع

**باستخدام VS Code:** فقط انقر بزر الماوس الأيمن على أي ملف عرض توضيحي في المستكشف واختر **"تشغيل Java"**، أو استخدم تكوينات الإطلاق من لوحة التشغيل والتصحيح (تأكد أولاً من إضافة الرمز المميز في ملف `.env`).

**باستخدام Maven:** يمكنك أيضًا التشغيل من سطر الأوامر باستخدام الأمثلة أدناه.

### عمليات الملفات (Stdio)

يوضح هذا الأدوات المحلية المبنية على العمليات الفرعية.

**✅ لا متطلبات مسبقة** - يتم إطلاق خادم MCP تلقائيًا.

**باستخدام سكريبتات البدء (موصى به):**

تقوم سكريبتات البدء بتحميل متغيرات البيئة تلقائيًا من ملف `.env` الجذري:

**باش:**
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

**استخدام VS Code:** انقر بزر الماوس الأيمن على `StdioTransportDemo.java` واختر **"تشغيل Java"** (تأكد من ضبط ملف `.env`).

يطلق التطبيق خادم MCP لنظام الملفات تلقائيًا ويقرأ ملفًا محليًا. لاحظ كيف تتم إدارة العملية الفرعية نيابة عنك.

**الناتج المتوقع:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### وكيل المشرف

نمط **وكيل المشرف** هو شكل **مرن** من ذكاء الذكاء الاصطناعي الوكلي. يستخدم المشرف نموذج لغة كبير (LLM) ليقرر بشكل مستقل الوكلاء الذين يجب استدعاؤهم بناءً على طلب المستخدم. في المثال التالي، ندمج وصول الملفات المدعوم بـ MCP مع وكيل LLM لإنشاء سير عمل قراءة ملف → تقرير بإشراف.

في العرض التوضيحي، يقرأ `FileAgent` الملف باستخدام أدوات نظام الملفات MCP، وينشئ `ReportAgent` تقريرًا منظمًا به ملخص تنفيذي (جملة واحدة)، 3 نقاط رئيسية، وتوصيات. ينظم المشرف هذا التدفق تلقائيًا:

<img src="../../../translated_images/ar/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="نمط وكيل المشرف" width="800"/>

*يستخدم المشرف نموذج اللغة لتقرير الوكلاء الذين يجب استدعاؤهم وبأي ترتيب — لا حاجة لتوجيه مشفر.*

هذا هو سير العمل الفعلي لأنبوب الملف إلى التقرير:

<img src="../../../translated_images/ar/file-report-workflow.649bb7a896800de9.webp" alt="سير عمل الملف إلى التقرير" width="800"/>

*يقرأ FileAgent الملف عبر أدوات MCP، ثم يحول ReportAgent المحتوى الخام إلى تقرير منظم.*

يخزن كل وكيل مخرجاته في **النطاق الوكيلي** (ذاكرة مشتركة)، مما يسمح للوكلاء التاليين بالوصول إلى النتائج السابقة. يوضح هذا كيف تندمج أدوات MCP بسلاسة في تدفقات العمل الوكيلية — لا يحتاج المشرف إلى معرفة *كيفية* قراءة الملفات، فقط أن `FileAgent` يمكنه فعل ذلك.

#### تشغيل العرض التوضيحي

تقوم سكريبتات البدء بتحميل متغيرات البيئة تلقائيًا من ملف `.env` الجذري:

**باش:**
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

**استخدام VS Code:** انقر بزر الماوس الأيمن على `SupervisorAgentDemo.java` واختر **"تشغيل Java"** (تأكد من ضبط ملف `.env`).

#### كيف يعمل المشرف

```java
// الخطوة 1: يقوم FileAgent بقراءة الملفات باستخدام أدوات MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // يحتوي على أدوات MCP لعمليات الملفات
        .build();

// الخطوة 2: يقوم ReportAgent بإنشاء تقارير منظمة
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// يقوم المشرف بتنظيم سير العمل من الملف إلى التقرير
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // إرجاع التقرير النهائي
        .build();

// يقرر المشرف أي الوكلاء يتم استدعاؤهم بناءً على الطلب
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### استراتيجيات الاستجابة

عند تكوين `SupervisorAgent`, تحدد كيف ينبغي له صياغة الإجابة النهائية للمستخدم بعد إتمام الوكلاء الفرعيين لمهامهم.

<img src="../../../translated_images/ar/response-strategies.3d0cea19d096bdf9.webp" alt="استراتيجيات الاستجابة" width="800"/>

*ثلاث استراتيجيات لكيفية صياغة المشرف لاستجابته النهائية — اختر بناءً على ما إذا كنت تريد مخرجات آخر وكيل، ملخصًا مجمعًا، أو الخيار الأعلى تقييمًا.*

الاستراتيجيات المتاحة هي:

| الاستراتيجية | الوصف |
|--------------|--------|
| **LAST** | يعيد المشرف مخرجات آخر وكيل فرعي أو أداة تم استدعاؤها. هذا مفيد حين يكون الوكيل الأخير في سير العمل مصممًا خصيصًا لإنتاج الجواب النهائي الكامل (مثلاً "وكيل الملخص" في خط بحث). |
| **SUMMARY** | يستخدم المشرف نموذج اللغة الداخلي الخاص به لتوليف ملخص كامل للتفاعل وكل مخرجات الوكلاء الفرعيين، ثم يعيد ذلك الملخص كاستجابة نهائية. هذا يوفر إجابة نظيفة ومجمعة للمستخدم. |
| **SCORED** | يستخدم النظام نموذجًا لغويًا داخليًا لتقييم كل من استجابة LAST والملخص SUMMARY مقابل طلب المستخدم الأصلي، ويعيد المخرجات الحاصلة على التقييم الأعلى. |

انظر [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) للتنفيذ الكامل.

> **🤖 جرب مع دردشة [GitHub Copilot](https://github.com/features/copilot):** افتح [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) واسأل:
> - "كيف يقرر المشرف الوكلاء الذين يجب استدعاؤهم؟"
> - "ما الفرق بين نمطي المشرف وتتابعي في سير العمل؟"
> - "كيف يمكنني تخصيص سلوك تخطيط المشرف؟"

#### فهم المخرجات

عند تشغيل العرض التوضيحي، سترى شرحًا منظمًا لكيفية تنظيم المشرف لوكلاء متعددين. هذا ما يعنيه كل قسم:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**العنوان** يقدم مفهوم سير العمل: أنبوب مركز من قراءة الملف إلى إنشاء التقرير.

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

**مخطط سير العمل** يظهر تدفق البيانات بين الوكلاء. لكل وكيل دور محدد:
- **FileAgent** يقرأ الملفات باستخدام أدوات MCP ويخزن المحتوى الخام في `fileContent`
- **ReportAgent** يستهلك المحتوى وينشئ تقريرًا منظمًا في `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**طلب المستخدم** يعرض المهمة. يقوم المشرف بتحليل هذا ويقرر استدعاء FileAgent → ReportAgent.

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

**تنسيق المشرف** يظهر تدفق الخطوتين كما يلي:
1. **FileAgent** يقرأ الملف عبر MCP ويخزن المحتوى
2. **ReportAgent** يستلم المحتوى وينشئ تقريرًا منظمًا

اتخذ المشرف هذه القرارات **بشكل مستقل** بناءً على طلب المستخدم.

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

#### شرح ميزات الوحدة الوكيلية

يظهر المثال عدة ميزات متقدمة للوحدة الوكيلية. لنلقِ نظرة أقرب على نطاق الوكيل ومستمعي الوكيل.

**نطاق الوكيل** يعرض الذاكرة المشتركة حيث خزّن الوكلاء نتائجهم باستخدام `@Agent(outputKey="...")`. هذا يسمح بـ:
- للوكلاء اللاحقين الوصول إلى مخرجات الوكلاء السابقين
- للمشرف بتوليف استجابة نهائية
- لك بمراجعة ما أنتجه كل وكيل

<img src="../../../translated_images/ar/agentic-scope.95ef488b6c1d02ef.webp" alt="نطاق الوكيل الذاكرة المشتركة" width="800"/>

*نطاق الوكيل يعمل كذاكرة مشتركة — يكتب FileAgent `fileContent`، يقرأ ReportAgent ذلك ويكتب `report`، ويقرأ كودك النتيجة النهائية.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // بيانات الملف الخام من FileAgent
String report = scope.readState("report");            // تقرير منظم من ReportAgent
```

**مستمعو الوكيل** يمكنون من مراقبة وتصحيح تنفيذ الوكيل. المخرجات خطوة بخطوة التي تراها في العرض التوضيحي تأتي من مستمع وكيل يرتبط بكل استدعاء وكيل:
- **beforeAgentInvocation** - يستدعى عندما يختار المشرف وكيلاً، مما يتيح لك رؤية الوكيل المختار ولماذا
- **afterAgentInvocation** - يستدعى عندما يكمل الوكيل، يظهر النتيجة الخاصة به
- **inheritedBySubagents** - عندما تكون صحيحة، يراقب المستمع جميع الوكلاء في التسلسل الهرمي

<img src="../../../translated_images/ar/agent-listeners.784bfc403c80ea13.webp" alt="دورة حياة مستمعي الوكلاء" width="800"/>

*مستمعو الوكلاء يتصلون بدورة تنفيذ الوكيل — يراقبون متى يبدأ الوكلاء، يكملون، أو يواجهون أخطاء.*

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
        return true; // نشر إلى جميع الوكلاء الفرعيين
    }
};
```

بعيدًا عن نمط المشرف، يوفر موديل `langchain4j-agentic` عدة أنماط عمل وميزات قوية:

<img src="../../../translated_images/ar/workflow-patterns.82b2cc5b0c5edb22.webp" alt="أنماط سير عمل الوكيل" width="800"/>

*خمسة أنماط سير عمل لتنظيم الوكلاء — من خطوط أنابيب تسلسلية بسيطة إلى سير عمل مراجعة بمشاركة الإنسان.*

| النمط | الوصف | الحالة الاستخدامية |
|---------|-------------|----------|
| **تسلسلي** | تنفيذ الوكلاء بالترتيب، نتيجة يمرر إلى التالي | خطوط الأنابيب: بحث → تحليل → تقرير |
| **متوازي** | تشغيل الوكلاء في نفس الوقت | مهام مستقلة: الطقس + الأخبار + الأسهم |
| **تكراري** | التكرار حتى تحقق شرط | تقييم الجودة: تحسين حتى الدرجة ≥ 0.8 |
| **شرطي** | التوجيه حسب الشروط | التصنيف → التوجيه إلى وكيل متخصص |
| **بمشاركة الإنسان** | إضافة نقاط مراجعة بشرية | سير عمل الموافقة، مراجعة المحتوى |

## المفاهيم الأساسية

بعد أن استعرضت MCP ووحدة agentic في العمل، دعنا نلخص متى تستخدم كل نهج.

<img src="../../../translated_images/ar/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="نظام MCP البيئي" width="800"/>

*MCP ينشئ نظام بروتوكول عالمي — أي خادم متوافق مع MCP يعمل مع أي عميل متوافق مع MCP، مما يتيح مشاركة الأدوات عبر التطبيقات.*

**MCP** مثالي عندما ترغب في الاستفادة من أنظمة الأدوات الموجودة، بناء أدوات يمكن مشاركتها بين تطبيقات متعددة، دمج خدمات طرف ثالث باستخدام بروتوكولات قياسية، أو تبديل تنفيذ الأدوات بدون تغيير الكود.

**وحدة Agentic** تعمل بشكل أفضل عندما تريد تعريف وكلاء بطريقة إعلانية مع التعليقات `@Agent`، تحتاج إلى تنظيم سير العمل (تسلسلي، تكراري، متوازي)، تفضل تصميم الوكيل القائم على الواجهات بدلًا من الكود الإجرائي، أو تجمع بين وكلاء متعددين يشاركون المخرجات عبر `outputKey`.

**نمط وكيل المشرف** يتألق عندما لا يكون سير العمل متوقعًا مسبقًا وتريد أن يقرر LLM، عندما لديك وكلاء متخصصون متعددون يحتاجون إلى تنظيم ديناميكي، عند بناء أنظمة محادثة توجه إلى قدرات مختلفة، أو عندما تريد سلوك وكيل أكثر مرونة وتكيفًا.

<img src="../../../translated_images/ar/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="أدوات مخصصة مقابل أدوات MCP" width="800"/>

*متى تستخدم طرق @Tool مخصصة مقابل أدوات MCP — الأدوات المخصصة للمنطق الخاص بالتطبيق مع سلامة نوع كاملة، أدوات MCP للتكاملات الموحدة التي تعمل عبر التطبيقات.*

## تهانينا!

<img src="../../../translated_images/ar/course-completion.48cd201f60ac7570.webp" alt="إتمام الدورة" width="800"/>

*رحلة تعلمك عبر جميع الوحدات الخمس — من الدردشة الأساسية إلى أنظمة agentic المدعومة بـ MCP.*

لقد أكملت دورة LangChain4j للمبتدئين. لقد تعلمت:

- كيفية بناء ذكاء اصطناعي محادثي مع ذاكرة (الوحدة 01)
- أنماط هندسة الموجهات لمهام مختلفة (الوحدة 02)
- ترسيخ الردود ضمن مستنداتك باستخدام RAG (الوحدة 03)
- إنشاء وكلاء ذكاء اصطناعي أساسيين (مساعدين) مع أدوات مخصصة (الوحدة 04)
- دمج الأدوات الموحدة باستخدام وحدات LangChain4j MCP و Agentic (الوحدة 05)

### ماذا بعد؟

بعد إكمال الوحدات، استكشف [دليل الاختبار](../docs/TESTING.md) لترى مفاهيم اختبار LangChain4j في العمل.

**الموارد الرسمية:**
- [توثيق LangChain4j](https://docs.langchain4j.dev/) - أدلة شاملة ومرجع API
- [مستودع LangChain4j على GitHub](https://github.com/langchain4j/langchain4j) - الشفرة المصدرية والأمثلة
- [دروس LangChain4j](https://docs.langchain4j.dev/tutorials/) - دروس خطوة بخطوة لحالات استخدام متعددة

شكرًا لك على إكمال هذه الدورة!

---

**التنقل:** [← السابق: الوحدة 04 - الأدوات](../04-tools/README.md) | [العودة للرئيسية](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**إخلاء المسؤولية**:  
تمت ترجمة هذا المستند باستخدام خدمة الترجمة الآلية [Co-op Translator](https://github.com/Azure/co-op-translator). وبينما نسعى جاهدين لتحقيق الدقة، يرجى العلم بأن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار المستند الأصلي بلغته الأصلية المصدر الرسمي والمعتمد. للمعلومات الحساسة أو الهامة، يُنصح بالاستعانة بترجمة بشرية محترفة. نحن غير مسؤولين عن أي سوء فهم أو تفسير ناتج عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->