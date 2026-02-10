# الوحدة 02: هندسة المطالبات مع GPT-5.2

## جدول المحتويات

- [ما ستتعلمه](../../../02-prompt-engineering)
- [المتطلبات المسبقة](../../../02-prompt-engineering)
- [فهم هندسة المطالبات](../../../02-prompt-engineering)
- [كيفية استخدام هذا ل LangChain4j](../../../02-prompt-engineering)
- [النماذج الأساسية](../../../02-prompt-engineering)
- [استخدام موارد Azure الموجودة](../../../02-prompt-engineering)
- [لقطات شاشة التطبيق](../../../02-prompt-engineering)
- [استكشاف النماذج](../../../02-prompt-engineering)
  - [الرغبة المنخفضة مقابل الرغبة العالية](../../../02-prompt-engineering)
  - [تنفيذ المهام (مقدمات الأدوات)](../../../02-prompt-engineering)
  - [شفرة ذاتية التأمل](../../../02-prompt-engineering)
  - [التحليل المنظم](../../../02-prompt-engineering)
  - [الدردشة متعددة الجولات](../../../02-prompt-engineering)
  - [التفكير خطوة بخطوة](../../../02-prompt-engineering)
  - [الإخراج المقيد](../../../02-prompt-engineering)
- [ما الذي تتعلمه فعلاً](../../../02-prompt-engineering)
- [الخطوات التالية](../../../02-prompt-engineering)

## ما ستتعلمه

في الوحدة السابقة، شاهدت كيف تمكّن الذاكرة الذكاء الاصطناعي الحواري واستخدمت نماذج GitHub للتفاعلات الأساسية. الآن سنركز على كيفية طرح الأسئلة - أي المطالبات نفسها - باستخدام GPT-5.2 من Azure OpenAI. الطريقة التي تبني بها مطالباتك تؤثر بشكل كبير على جودة الردود التي تحصل عليها.

سنستخدم GPT-5.2 لأنه يقدم تحكماً في التفكير - يمكنك إخبار النموذج بكمية التفكير التي يقوم بها قبل الإجابة. هذا يجعل استراتيجيات المطالبات المختلفة أكثر وضوحاً ويساعدك على معرفة متى تستخدم كل نهج. سنستفيد أيضاً من حدود المعدل الأقل في Azure لـ GPT-5.2 مقارنة بنماذج GitHub.

## المتطلبات المسبقة

- إكمال الوحدة 01 (تم نشر موارد Azure OpenAI)
- ملف `.env` في الدليل الجذر مع بيانات اعتماد Azure (تم إنشاؤه بواسطة `azd up` في الوحدة 01)

> **ملاحظة:** إذا لم تكمل الوحدة 01 بعد، فاتبع تعليمات النشر هناك أولاً.

## فهم هندسة المطالبات

هندسة المطالبات هي تصميم نص الإدخال الذي يضمن حصولك باستمرار على النتائج التي تحتاجها. الأمر ليس مجرد طرح أسئلة - بل يتعلق بهيكلة الطلبات بحيث يفهم النموذج بالضبط ما تريد وكيفية تقديمه.

فكر في الأمر كما لو كنت تعطي تعليمات لزميل. "أصلح الخطأ" عبارة غامضة. "أصلح استثناء المؤشر الخالي في UserService.java السطر 45 بإضافة فحص خالي" محدد. نماذج اللغة تعمل بنفس الطريقة - الدقة والهيكل مهمان.

## كيفية استخدام هذا ل LangChain4j

تبيّن هذه الوحدة أنماطاً متقدمة من المطالبات باستخدام نفس أساس LangChain4j من الوحدات السابقة، مع التركيز على هيكلة المطالبات والتحكم في التفكير.

<img src="../../../translated_images/ar/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*كيفية اتصال LangChain4j بمطالباتك إلى Azure OpenAI GPT-5.2*

**التبعيات** - تستخدم الوحدة 02 التبعيات التالية لـ langchain4j المعرفة في `pom.xml`:
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

**تكوين OpenAiOfficialChatModel** - [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

يتم تكوين نموذج الدردشة يدوياً كـ bean في Spring باستخدام عميل OpenAI الرسمي، الذي يدعم نقاط نهاية Azure OpenAI. الفرق الرئيسي عن الوحدة 01 هو كيفية هيكلة المطالبات المرسلة إلى `chatModel.chat()`، وليس إعداد النموذج نفسه.

**رسائل النظام والمستخدم** - [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

يفصل LangChain4j أنواع الرسائل للوضوح. تحدد `SystemMessage` سلوك ونطاق AI (مثل "أنت مراجع شفرات")، بينما تحتوي `UserMessage` على الطلب الفعلي. هذا الفصل يسمح بالحفاظ على سلوك متسق للذكاء الاصطناعي عبر استفسارات المستخدم المختلفة.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/ar/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*توفر SystemMessage سياق متواصل بينما تحتوي UserMessages على طلبات فردية*

**MessageWindowChatMemory للجولات المتعددة** - لنمط المحادثة متعددة الجولات، نعيد استخدام `MessageWindowChatMemory` من الوحدة 01. كل جلسة تحصل على نسخة ذاكرة خاصة بها مخزنة في `Map<String, ChatMemory>`، مما يسمح بمحادثات متزامنة متعددة دون خلط السياق.

**قوالب المطالبات** - التركيز الحقيقي هنا هو هندسة المطالبات، وليس واجهات برمجة تطبيقات LangChain4j جديدة. كل نمط (الرغبة المنخفضة، الرغبة العالية، تنفيذ المهام، إلخ) يستخدم نفس طريقة `chatModel.chat(prompt)` ولكن مع سلاسل مطالبات منظمة بعناية. علامات XML، التعليمات، والتنسيق كلها جزء من نص المطالبة، وليست ميزات في LangChain4j.

**التحكم في التفكير** - يتم التحكم في جهد التفكير لـ GPT-5.2 عبر تعليمات المطالبات مثل "حد أقصى خطوتي تفكير" أو "استكشف بدقة". هذه تقنيات هندسة مطالبات، وليست إعدادات LangChain4j. المكتبة تقوم ببساطة بتوصيل مطالباتك إلى النموذج.

النتيجة الأساسية: يوفر LangChain4j البنية التحتية (اتصال النموذج عبر [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)، الذاكرة، إدارة الرسائل عبر [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java))، بينما تعلمك هذه الوحدة كيفية صياغة مطالبات فعالة داخل تلك البنية التحتية.

## النماذج الأساسية

ليست كل المشكلات بحاجة إلى نفس النهج. بعض الأسئلة تحتاج إلى إجابات سريعة، وبعضها يحتاج تفكيراً عميقاً. بعضها يحتاج تفكيراً مرئياً، وبعضها يحتاج فقط نتائج. تغطي هذه الوحدة ثمانية أنماط من المطالبات - كل منها محسن لحالات مختلفة. ستجرب كلها لتتعلم متى يعمل كل نهج بأفضل شكل.

<img src="../../../translated_images/ar/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*نظرة عامة على ثمان أنماط هندسة المطالبات وحالات استخدامها*

<img src="../../../translated_images/ar/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*الرغبة المنخفضة (سريع، مباشر) مقابل الرغبة العالية (دقيق، استكشافي) في التفكير*

**الرغبة المنخفضة (سريع ومركّز)** - للأسئلة البسيطة حيث تريد إجابات مباشرة وسريعة. يقوم النموذج بأقل تفكير - حد أقصى خطوتين. استخدم هذا للحسابات، عمليات البحث، أو الأسئلة المباشرة.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **استكشف مع GitHub Copilot:** افتح [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) واسأل:
> - "ما الفرق بين أنماط المطالبات ذات الرغبة المنخفضة والرغبة العالية؟"
> - "كيف تساعد علامات XML في المطالبات على هيكلة ردود الذكاء الاصطناعي؟"
> - "متى أستخدم أنماط التأمل الذاتي مقابل التعليمات المباشرة؟"

**الرغبة العالية (عميق وشامل)** - للمشكلات المعقدة التي تريد تحليلاً شاملاً. يستكشف النموذج بدقة ويظهر تفكيراً مفصلاً. استخدم هذا لتصميم الأنظمة، قرارات الهيكلة، أو البحوث المعقدة.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**تنفيذ المهمة (التقدم خطوة بخطوة)** - لسير عمل متعدد الخطوات. يقدم النموذج خطة مسبقة، ويروي كل خطوة أثناء العمل، ثم يعطي ملخصاً. استخدم هذا للهجرات، التطبيقات، أو أي عملية تتألف من خطوات متعددة.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

مطالبة سلسلة-التفكير تطلب صراحة من النموذج إظهار عملية تفكيره، مما يحسن الدقة في المهام المعقدة. يفيد التحليل خطوة بخطوة البشر والذكاء الاصطناعي على حد سواء لفهم المنطق.

> **🤖 جرب مع دردشة [GitHub Copilot](https://github.com/features/copilot):** اسأل عن هذا النمط:
> - "كيف يمكنني تكييف نمط تنفيذ المهمة للعمليات طويلة الأمد؟"
> - "ما هي أفضل الممارسات لهياكلة مقدمات الأدوات في تطبيقات الإنتاج؟"
> - "كيف يمكنني التقاط وعرض تحديثات التقدم الوسيط في واجهة المستخدم؟"

<img src="../../../translated_images/ar/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*التخطيط → التنفيذ → تلخيص لسير عمل المهام متعددة الخطوات*

**الشفرة ذاتية التأمل** - لتوليد شفرة بجودة الإنتاج. يقوم النموذج بتوليد الشفرة، ويفحصها وفق معايير الجودة، ويُحسّنها تدريجياً. استخدم هذا عند بناء ميزات أو خدمات جديدة.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ar/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*دورة تحسين تكرارية - توليد، تقييم، تحديد المشاكل، تحسين، تكرار*

**التحليل المنظم** - للتقييم المتسق. يراجع النموذج الشفرة باستخدام إطار ثابت (الصحة، الممارسات، الأداء، الأمان). استخدم هذا للمراجعات البرمجية أو تقييمات الجودة.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 جرب مع دردشة [GitHub Copilot](https://github.com/features/copilot):** اسأل عن التحليل المنظم:
> - "كيف يمكنني تخصيص إطار التحليل لأنواع مراجعات الشفرة المختلفة؟"
> - "ما هي أفضل طريقة لتحليل والتصرف على إخراج منظم برمجياً؟"
> - "كيف أضمن مستويات شدة متسقة عبر جلسات المراجعة المختلفة؟"

<img src="../../../translated_images/ar/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*إطار رباعي الفئات للمراجعات البرمجية المتسقة مع مستويات شدة*

**الدردشة متعددة الجولات** - للمحادثات التي تحتاج إلى سياق. يتذكر النموذج الرسائل السابقة ويبني عليها. استخدم هذا لجلسات المساعدة التفاعلية أو الأسئلة والأجوبة المعقدة.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ar/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*كيف يتراكم سياق المحادثة عبر جولات متعددة حتى يصل إلى حد الرموز*

**التفكير خطوة بخطوة** - للمشكلات التي تتطلب منطقاً مرئياً. يعرض النموذج تفكيراً صريحاً لكل خطوة. استخدم هذا للمشكلات الرياضية، الألغاز المنطقية، أو عندما تحتاج لفهم عملية التفكير.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ar/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*تفكيك المشكلات إلى خطوات منطقية صريحة*

**الإخراج المقيد** - للردود التي تحتاج إلى متطلبات تنسيق محددة. يتبع النموذج بدقة قواعد التنسيق والطول. استخدم هذا للملخصات أو عندما تحتاج إلى هيكل إخراج دقيق.

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

<img src="../../../translated_images/ar/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*فرض متطلبات التنسيق والطول والبنية المحددة*

## استخدام موارد Azure الموجودة

**تحقق من النشر:**

تأكد من وجود ملف `.env` في الدليل الجذر مع بيانات اعتماد Azure (تم إنشاؤه خلال الوحدة 01):
```bash
cat ../.env  # يجب أن يعرض AZURE_OPENAI_ENDPOINT و API_KEY و DEPLOYMENT
```

**ابدأ التطبيق:**

> **ملاحظة:** إذا كنت قد بدأت جميع التطبيقات باستخدام `./start-all.sh` من الوحدة 01، فهذه الوحدة تعمل بالفعل على المنفذ 8083. يمكنك تخطي أوامر البدء أدناه والذهاب مباشرة إلى http://localhost:8083.

**الخيار 1: استخدام لوحة تحكم Spring Boot (موصى به لمستخدمي VS Code)**

تتضمن حاوية التطوير إضافة لوحة تحكم Spring Boot، التي توفر واجهة مرئية لإدارة جميع تطبيقات Spring Boot. يمكنك العثور عليها في شريط النشاط على الجانب الأيسر من VS Code (ابحث عن رمز Spring Boot).

من لوحة تحكم Spring Boot، يمكنك:
- رؤية جميع تطبيقات Spring Boot المتاحة في مساحة العمل
- بدء/إيقاف التطبيقات بنقرة واحدة
- عرض سجلات التطبيق في الوقت الحقيقي
- مراقبة حالة التطبيق

ما عليك سوى النقر على زر التشغيل بجانب "prompt-engineering" لبدء هذه الوحدة، أو تشغيل كل الوحدات دفعة واحدة.

<img src="../../../translated_images/ar/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**الخيار 2: استخدام سكريبتات الصدفة**

ابدأ جميع تطبيقات الويب (الوحدات 01-04):

**Bash:**
```bash
cd ..  # من الدليل الجذري
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # من الدليل الجذر
.\start-all.ps1
```

أو ابدأ هذه الوحدة فقط:

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

كلا السكريبتين يقومان بتحميل المتغيرات البيئية تلقائياً من ملف `.env` في الجذر وسيبنيان ملفات JAR إذا لم تكن موجودة.

> **ملاحظة:** إذا فضلت بناء كل الوحدات يدوياً قبل البدء:
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

افتح http://localhost:8083 في متصفحك.

**للتوقف:**

**Bash:**
```bash
./stop.sh  # هذا الوحدة فقط
# أو
cd .. && ./stop-all.sh  # جميع الوحدات
```

**PowerShell:**
```powershell
.\stop.ps1  # فقط هذه الوحدة
# أو
cd ..; .\stop-all.ps1  # جميع الوحدات
```

## لقطات شاشة للتطبيق

<img src="../../../translated_images/ar/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*اللوحة الرئيسية تعرض جميع أنماط هندسة المطالبات الثمانية مع خصائصها وحالات استخدامها*

## استكشاف النماذج

تتيح لك واجهة الويب تجربة استراتيجيات مطالبات مختلفة. كل نمط يحل مشكلات مختلفة - جربها لترى متى يسطع كل نهج.

### الرغبة المنخفضة مقابل الرغبة العالية

اسأل سؤالاً بسيطاً مثل "ما هو 15٪ من 200؟" باستخدام الرغبة المنخفضة. ستحصل على إجابة فورية ومباشرة. الآن اسأل شيئاً معقداً مثل "صمم استراتيجية التخزين المؤقت لواجهة API ذات حركة مرور عالية" باستخدام الرغبة العالية. شاهد كيف يبطئ النموذج ويقدم تفكيراً مفصلاً. نفس النموذج، نفس هيكل السؤال - لكن المطالبة تخبره بكمية التفكير المطلوبة.
<img src="../../../translated_images/ar/low-eagerness-demo.898894591fb23aa0.webp" alt="عرض توضيحي للحماس المنخفض" width="800"/>

*حساب سريع مع استدلال بسيط*

<img src="../../../translated_images/ar/high-eagerness-demo.4ac93e7786c5a376.webp" alt="عرض توضيحي للحماس العالي" width="800"/>

*استراتيجية تخزين شاملة (2.8 ميجابايت)*

### تنفيذ المهام (مقدمات الأدوات)

تستفيد سير العمل متعددة الخطوات من التخطيط المسبق والرواية التقدمية. يحدد النموذج ما سيفعله، ويروي كل خطوة، ثم يلخص النتائج.

<img src="../../../translated_images/ar/tool-preambles-demo.3ca4881e417f2e28.webp" alt="عرض تنفيذ المهام" width="800"/>

*إنشاء نقطة نهاية REST مع رواية خطوة بخطوة (3.9 ميجابايت)*

### الكود العاكس للذات

جرب "إنشاء خدمة تحقق من البريد الإلكتروني". بدلاً من مجرد إنشاء الكود والتوقف، يولد النموذج، ويقيمه مقابل معايير الجودة، ويحدد نقاط الضعف، ويحسن. سترى تكرارًا حتى يصل الكود إلى معايير الإنتاج.

<img src="../../../translated_images/ar/self-reflecting-code-demo.851ee05c988e743f.webp" alt="عرض الكود العاكس للذات" width="800"/>

*خدمة تحقق كاملة للبريد الإلكتروني (5.2 ميجابايت)*

### التحليل الهيكلي

تحتاج مراجعات الكود إلى أُطر تقييم متناسقة. يحلل النموذج الكود باستخدام فئات ثابتة (الصحة، الممارسات، الأداء، الأمان) مع مستويات شدة.

<img src="../../../translated_images/ar/structured-analysis-demo.9ef892194cd23bc8.webp" alt="عرض التحليل الهيكلي" width="800"/>

*مراجعة الكود استنادًا إلى إطار عمل*

### المحادثة متعددة الأدوار

اسأل "ما هو Spring Boot؟" ثم تابع فورًا بسؤال "أرني مثالًا". يتذكر النموذج سؤالك الأول ويعطيك مثالًا محددًا على Spring Boot. بدون ذاكرة، سيكون السؤال الثاني غامضًا جدًا.

<img src="../../../translated_images/ar/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="عرض المحادثة متعددة الأدوار" width="800"/>

*الحفاظ على السياق عبر الأسئلة*

### الاستدلال خطوة بخطوة

اختر مسألة رياضية وجربها مع الاستدلال خطوة بخطوة والحماس المنخفض. الحماس المنخفض يعطيك الجواب فقط — سريع لكنه غير شفاف. الاستدلال خطوة بخطوة يعرض كل حساب وقرار.

<img src="../../../translated_images/ar/step-by-step-reasoning-demo.12139513356faecd.webp" alt="عرض الاستدلال خطوة بخطوة" width="800"/>

*مسألة رياضية مع خطوات واضحة*

### الناتج المقيد

عندما تحتاج إلى تنسيقات معينة أو عدد كلمات محدد، يفرض هذا النمط الالتزام الصارم. جرب إنشاء ملخص يتكون من 100 كلمة بالضبط بنمط نقاط.

<img src="../../../translated_images/ar/constrained-output-demo.567cc45b75da1633.webp" alt="عرض الناتج المقيد" width="800"/>

*ملخص تعلم الآلة مع التحكم بالتنسيق*

## ما الذي تتعلمه حقًا

**جهد الاستدلال يغير كل شيء**

يتيح لك GPT-5.2 التحكم في جهد الحوسبة من خلال مطالباتك. الجهد المنخفض يعني ردود سريعة مع استكشاف بسيط. الجهد العالي يعني أن النموذج يأخذ وقتًا للتفكير بعمق. أنت تتعلم مطابقة الجهد مع تعقيد المهمة - لا تضيع وقتًا في أسئلة بسيطة، لكن لا تسرع في قرارات معقدة كذلك.

**الهيكل يوجه السلوك**

هل لاحظت علامات XML في المطالبات؟ ليست للزينة. تتبع النماذج التعليمات المنظمة بشكل أكثر موثوقية من النص الحر. عندما تحتاج عمليات متعددة الخطوات أو منطق معقد، يساعد الهيكل النموذج على تتبع موقعه وما سيأتي بعد ذلك.

<img src="../../../translated_images/ar/prompt-structure.a77763d63f4e2f89.webp" alt="هيكل المطالبة" width="800"/>

*بنية مطالبة منظمة جيدًا مع أقسام واضحة وتنظيم بأسلوب XML*

**الجودة من خلال التقييم الذاتي**

تعمل أنماط العاكس الذاتي بجعل معايير الجودة واضحة. بدلاً من التمني أن "يفعل النموذج ذلك بشكل صحيح"، تخبره بالضبط ما يعنيه "الصحيح": المنطق الصحيح، معالجة الأخطاء، الأداء، الأمان. يمكن للنموذج حينها تقييم مخرجاته وتحسينها. يحول هذا إنتاج الكود من يانصيب إلى عملية منهجية.

**السياق محدود**

تعمل المحادثات متعددة الأدوار بإدراج تاريخ الرسائل مع كل طلب. لكن هناك حدًا - لكل نموذج عدد أقصى من الرموز. مع نمو المحادثات، ستحتاج استراتيجيات للحفاظ على السياق ذي الصلة دون الوصول إلى هذا الحد. تُظهر لك هذه الوحدة كيف تعمل الذاكرة؛ وستتعلم لاحقًا متى تلخص، ومتى تنسى، ومتى تسترجع.

## الخطوات التالية

**الوحدة التالية:** [03-rag - RAG (التوليد المعزز بالاستخلاص)](../03-rag/README.md)

---

**التنقل:** [← السابق: الوحدة 01 - المقدمة](../01-introduction/README.md) | [العودة إلى الرئيسي](../README.md) | [التالي: الوحدة 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**تنويه**:
تمت ترجمة هذا المستند باستخدام خدمة الترجمة الآلية [Co-op Translator](https://github.com/Azure/co-op-translator). على الرغم من سعينا لتحقيق الدقة، يُرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار المستند الأصلي بلغته الأصلية المصدر المعتمد. بالنسبة للمعلومات الحرجة، يُنصح باللجوء إلى الترجمة البشرية المهنية. نحن غير مسؤولين عن أي سوء فهم أو تفسير ناتج عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->