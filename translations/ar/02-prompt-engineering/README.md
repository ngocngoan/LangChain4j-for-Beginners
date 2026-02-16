# الوحدة 02: هندسة الموجهات مع GPT-5.2

## جدول المحتويات

- [ما ستتعلمه](../../../02-prompt-engineering)
- [المتطلبات المسبقة](../../../02-prompt-engineering)
- [فهم هندسة الموجهات](../../../02-prompt-engineering)
- [أساسيات هندسة الموجهات](../../../02-prompt-engineering)
  - [الموجه الصفري](../../../02-prompt-engineering)
  - [الموجه القليل الأمثلة](../../../02-prompt-engineering)
  - [سلسلة الأفكار](../../../02-prompt-engineering)
  - [الموجه المبني على الدور](../../../02-prompt-engineering)
  - [قوالب الموجهات](../../../02-prompt-engineering)
- [الأنماط المتقدمة](../../../02-prompt-engineering)
- [استخدام موارد Azure الحالية](../../../02-prompt-engineering)
- [لقطات شاشة للتطبيق](../../../02-prompt-engineering)
- [استكشاف الأنماط](../../../02-prompt-engineering)
  - [انخفاض مقابل ارتفاع الحماس](../../../02-prompt-engineering)
  - [تنفيذ المهام (تمهيدات الأدوات)](../../../02-prompt-engineering)
  - [الكود الذي يعكس نفسه](../../../02-prompt-engineering)
  - [التحليل الهيكلي](../../../02-prompt-engineering)
  - [دردشة متعددة الأدوار](../../../02-prompt-engineering)
  - [التفكير خطوة بخطوة](../../../02-prompt-engineering)
  - [مخرجات مقيدة](../../../02-prompt-engineering)
- [ما تتعلمه حقًا](../../../02-prompt-engineering)
- [الخطوات التالية](../../../02-prompt-engineering)

## ما ستتعلمه

<img src="../../../translated_images/ar/what-youll-learn.c68269ac048503b2.webp" alt="ما ستتعلمه" width="800"/>

في الوحدة السابقة، رأيت كيف تمكن الذاكرة الذكاء الاصطناعي الحواري واستخدمت نماذج GitHub للتفاعلات الأساسية. الآن سنركز على كيفية طرح الأسئلة — أي الموجهات نفسها — باستخدام GPT-5.2 من Azure OpenAI. الطريقة التي تنشئ بها موجهاتك تؤثر بشكل كبير على جودة الردود التي تحصل عليها. نبدأ بمراجعة تقنيات التوجيه الأساسية، ثم ننتقل إلى ثمانية أنماط متقدمة تستفيد بالكامل من قدرات GPT-5.2.

سنستخدم GPT-5.2 لأنه يقدم تحكمًا في الاستدلال - يمكنك أن تخبر النموذج بكمية التفكير التي يجب أن يقوم بها قبل الإجابة. هذا يجعل استراتيجيات التوجيه المختلفة أكثر وضوحًا ويساعدك على فهم متى تستخدم كل نهج. سنستفيد أيضًا من حدود معدل أقل من Azure لـ GPT-5.2 مقارنة بنماذج GitHub.

## المتطلبات المسبقة

- إتمام الوحدة 01 (تم نشر موارد Azure OpenAI)
- وجود ملف `.env` في الدليل الجذر مع بيانات اعتماد Azure (تم إنشاؤه بواسطة `azd up` في الوحدة 01)

> **ملاحظة:** إذا لم تكمل الوحدة 01، اتبع تعليمات النشر هناك أولاً.

## فهم هندسة الموجهات

<img src="../../../translated_images/ar/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="ما هي هندسة الموجهات؟" width="800"/>

هندسة الموجهات تتعلق بتصميم نص الإدخال الذي يضمن لك الحصول على النتائج التي تحتاجها بشكل مستمر. ليست مجرد طرح أسئلة - بل هي هيكلة الطلبات بحيث يفهم النموذج بالضبط ما تريد وكيفية تقديمه.

فكر فيها كأنك تعطي تعليمات لزميل. "إصلاح الخطأ" غامض. "إصلاح استثناء مؤشر فارغ في UserService.java السطر 45 عن طريق إضافة فحص عناصر فارغة" محدد. نماذج اللغة تعمل بنفس الطريقة - الدقة والتركيب مهمان.

<img src="../../../translated_images/ar/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="كيف يتناسب LangChain4j" width="800"/>

يوفر LangChain4j البنية التحتية — اتصالات النموذج، الذاكرة، وأنواع الرسائل — بينما أنماط الموجهات هي نص منظم بعناية ترسله عبر تلك البنية التحتية. اللبنات الأساسية هي `SystemMessage` (الذي يحدد سلوك ودور الذكاء الاصطناعي) و `UserMessage` (الذي يحمل طلبك الفعلي).

## أساسيات هندسة الموجهات

<img src="../../../translated_images/ar/five-patterns-overview.160f35045ffd2a94.webp" alt="نظرة عامة على خمسة أنماط لهندسة الموجهات" width="800"/>

قبل الغوص في الأنماط المتقدمة في هذه الوحدة، دعنا نراجع خمس تقنيات توجيه أساسية. هذه هي اللبنات الأساسية التي يجب أن يعرفها كل مهندس موجهات. إذا كنت قد عملت بالفعل من خلال [وحدة البداية السريعة](../00-quick-start/README.md#2-prompt-patterns)، فقد رأيت هذه في التطبيق — وإليك الإطار المفاهيمي وراءها.

### الموجه الصفري

أسهل نهج: أعطِ النموذج تعليمات مباشرة بدون أمثلة. يعتمد النموذج كليًا على تدريبه لفهم وتنفيذ المهمة. يعمل هذا جيدًا للطلبات البسيطة حيث يكون السلوك المتوقع واضحًا.

<img src="../../../translated_images/ar/zero-shot-prompting.7abc24228be84e6c.webp" alt="الموجه الصفري" width="800"/>

*تعليمات مباشرة بدون أمثلة — يستنتج النموذج المهمة من التعليمات فقط*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// الرد: "إيجابي"
```

**متى تستخدم:** التصنيفات البسيطة، الأسئلة المباشرة، الترجمات، أو أي مهمة يمكن للنموذج التعامل معها بدون توجيه إضافي.

### الموجه القليل الأمثلة

قدم أمثلة توضح النمط الذي تريد أن يتبعه النموذج. يتعلم النموذج تنسيق الإدخال-الإخراج المتوقع من أمثلتك ويطبقه على مدخلات جديدة. هذا يحسن الاتساق بشكل كبير للمهام التي يكون فيها التنسيق أو السلوك المرغوب غير واضح.

<img src="../../../translated_images/ar/few-shot-prompting.9d9eace1da88989a.webp" alt="الموجه القليل الأمثلة" width="800"/>

*التعلم من الأمثلة — يتعرف النموذج على النمط ويطبقه على مدخلات جديدة*

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

**متى تستخدم:** التصنيفات المخصصة، التنسيق المتناسق، المهام الخاصة بالمجال، أو عندما تكون نتائج الموجه الصفري غير متسقة.

### سلسلة الأفكار

اطلب من النموذج عرض استدلاله خطوة بخطوة. بدلاً من القفز مباشرة للإجابة، يتفكك النموذج المشكلة ويعمل على كل جزء بوضوح. هذا يحسن الدقة في مسائل الرياضيات والمنطق والاستدلال متعدد الخطوات.

<img src="../../../translated_images/ar/chain-of-thought.5cff6630e2657e2a.webp" alt="موجه سلسلة الأفكار" width="800"/>

*الاستدلال خطوة بخطوة — تقسيم المشكلات المعقدة إلى خطوات منطقية واضحة*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// يظهر النموذج: ١٥ - ٨ = ٧، ثم ٧ + ١٢ = ١٩ تفاحة
```

**متى تستخدم:** مسائل الرياضيات، الألغاز المنطقية، تصحيح الأخطاء، أو أي مهمة حيث عرض عملية الاستدلال يحسن الدقة والثقة.

### الموجه المبني على الدور

حدد شخصية أو دورًا للذكاء الاصطناعي قبل طرح سؤالك. يوفر هذا سياقًا يشكل نغمة وعمق وتركيز الرد. "مهندس برمجيات" يعطي نصيحة مختلفة عن "مطور مبتدئ" أو "مراجع أمني".

<img src="../../../translated_images/ar/role-based-prompting.a806e1a73de6e3a4.webp" alt="الموجه المبني على الدور" width="800"/>

*تحديد السياق والشخصية — نفس السؤال يحصل على رد مختلف اعتمادًا على الدور المحدد*

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

**متى تستخدم:** مراجعات الشفرة، التدريس، التحليل المتخصص بالمجال، أو عندما تحتاج ردودًا مصممة لمستوى خبرة أو منظور معين.

### قوالب الموجهات

أنشئ موجهات قابلة لإعادة الاستخدام تحتوي على أماكن للمتغيرات. بدلاً من كتابة موجه جديد في كل مرة، عرف قالبًا مرة واحدة واملأ القيم المختلفة. فئة `PromptTemplate` في LangChain4j تجعل هذا سهلاً مع صيغة `{{variable}}`.

<img src="../../../translated_images/ar/prompt-templates.14bfc37d45f1a933.webp" alt="قوالب الموجهات" width="800"/>

*موجهات قابلة لإعادة الاستخدام مع أماكن للمتغيرات — قالب واحد، استخدامات متعددة*

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

**متى تستخدم:** استفسارات متكررة مع مدخلات مختلفة، المعالجة الدفعيّة، بناء سير عمل ذكاء اصطناعي قابل لإعادة الاستخدام، أو أي سيناريو يحافظ على بنية الموجه نفسها مع تغير البيانات.

---

هذه الأساسيات الخمسة تعطيك مجموعة أدوات قوية لمعظم مهام التوجيه. بقية هذه الوحدة تبني عليها مع **ثمانية أنماط متقدمة** تستفيد من تحكم GPT-5.2 في الاستدلال، والتقييم الذاتي، وقدرات المخرجات المنظمة.

## الأنماط المتقدمة

بعد تغطية الأساسيات، دعنا ننتقل إلى ثمانية أنماط متقدمة تجعل هذه الوحدة فريدة. ليست كل المشكلات تحتاج نفس النهج. بعض الأسئلة تحتاج أجوبة سريعة، وأخرى تحتاج تفكيراً عميقاً. بعضها يحتاج استدلالاً مرئياً، وأخرى تحتاج فقط النتائج. كل نمط أدناه موجه لموقف مختلف — وتحكم GPT-5.2 في الاستدلال يجعل الفروق أوضح.

<img src="../../../translated_images/ar/eight-patterns.fa1ebfdf16f71e9a.webp" alt="ثمانية أنماط لتوجيه الموجهات" width="800"/>

*نظرة عامة على ثمانية أنماط لهندسة الموجهات وحالات استخدامها*

<img src="../../../translated_images/ar/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="التحكم في الاستدلال مع GPT-5.2" width="800"/>

*تحكم GPT-5.2 في الاستدلال يسمح لك بتحديد كمية التفكير التي يجب أن يقوم بها النموذج — من الإجابات السريعة المباشرة إلى الاستكشاف العميق*

<img src="../../../translated_images/ar/reasoning-effort.db4a3ba5b8e392c1.webp" alt="مقارنة جهد الاستدلال" width="800"/>

*انخفاض الحماس (سريع ومباشر) مقابل ارتفاع الحماس (شامل واستكشافي) مناهج الاستدلال*

**انخفاض الحماس (سريع ومركز)** - للأسئلة البسيطة حيث تريد إجابات سريعة ومباشرة. يقوم النموذج بأقل استدلال - بحد أقصى خطوتين. استخدم هذا للحسابات، عمليات البحث، أو الأسئلة المباشرة.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **استكشف مع GitHub Copilot:** افتح [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) واسأل:
> - "ما الفرق بين نمطي توجيه انخفاض الحماس وارتفاع الحماس؟"
> - "كيف تساعد وسم XML في الموجهات على هيكلة رد الذكاء الاصطناعي؟"
> - "متى يجب أن أستخدم نماذج التفكير الذاتي مقابل التعليمات المباشرة؟"

**ارتفاع الحماس (عميق وشامل)** - للمشاكل المعقدة حيث تريد تحليلًا شاملًا. يستكشف النموذج بعمق ويعرض استدلالًا مفصلاً. استخدم هذا لتصميم الأنظمة، قرارات الهندسة المعمارية، أو البحث المعقد.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**تنفيذ المهام (تقدم خطوة بخطوة)** - لسير العمل المتعدد الخطوات. يوفر النموذج خطة مبدئية، يروي كل خطوة أثناء العمل، ثم يعرض ملخصًا. استخدم هذا للهجرات، التنفيذات، أو أي عملية متعددة الخطوات.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

موجه سلسلة الأفكار يطلب صراحة من النموذج عرض عملية الاستدلال الخاصة به، مما يحسن الدقة للمهام المعقدة. التقسيم خطوة بخطوة يساعد كلًا من البشر والذكاء الاصطناعي على فهم المنطق.

> **🤖 جرب مع دردشة [GitHub Copilot](https://github.com/features/copilot):** اسأل عن هذا النمط:
> - "كيف يمكنني تكييف نمط تنفيذ المهام للعمليات طويلة الأمد؟"
> - "ما هي أفضل الممارسات لهيكلة تمهيدات الأدوات في تطبيقات الإنتاج؟"
> - "كيف يمكنني التقاط وعرض تحديثات التقدم الوسيط في واجهة المستخدم؟"

<img src="../../../translated_images/ar/task-execution-pattern.9da3967750ab5c1e.webp" alt="نمط تنفيذ المهام" width="800"/>

*خطة → تنفيذ → تلخيص لسير العمل متعدد الخطوات*

**الكود الذي يعكس نفسه** - لتوليد كود بجودة إنتاجية. ينتج النموذج الكود، يفحصه مقابل معايير الجودة، ويحسنه تدريجيًا. استخدم هذا عند بناء ميزات أو خدمات جديدة.

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

<img src="../../../translated_images/ar/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="دورة التفكير الذاتي" width="800"/>

*حلقة تحسين تكرارية - توليد، تقييم، تحديد المشكلات، تحسين، تكرار*

**التحليل الهيكلي** - للتقييم المتناسق. يراجع النموذج الكود باستخدام إطار ثابت (الصحة، الممارسات، الأداء، الأمان). استخدم هذا لمراجعات الكود أو تقييمات الجودة.

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

> **🤖 جرب مع دردشة [GitHub Copilot](https://github.com/features/copilot):** اسأل عن التحليل الهيكلي:
> - "كيف يمكنني تخصيص إطار التحليل لأنواع مختلفة من مراجعات الكود؟"
> - "ما هي أفضل طريقة لتحليل والعمل على المخرجات المنظمة برمجيًا؟"
> - "كيف أضمن مستويات شدة ثابتة عبر جلسات المراجعة المختلفة؟"

<img src="../../../translated_images/ar/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="نمط التحليل الهيكلي" width="800"/>

*إطار عمل بأربع فئات لمراجعات الكود المتناسقة مع مستويات شدة*

**دردشة متعددة الأدوار** - للمحادثات التي تحتاج إلى سياق. يتذكر النموذج الرسائل السابقة ويبني عليها. استخدم هذا لجلسات المساعدة التفاعلية أو الأسئلة والأجوبة المعقدة.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ar/context-memory.dff30ad9fa78832a.webp" alt="ذاكرة السياق" width="800"/>

*كيف يتراكم سياق المحادثة عبر أدوار متعددة حتى يصل إلى حد التوكن*

**التفكير خطوة بخطوة** - للمشاكل التي تتطلب منطقًا مرئيًا. يعرض النموذج الاستدلال الواضح لكل خطوة. استخدم هذا لمسائل الرياضيات، الألغاز المنطقية، أو عندما تحتاج لفهم عملية التفكير.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ar/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="نمط خطوة بخطوة" width="800"/>

*تقسيم المشكلات إلى خطوات منطقية واضحة*

**مخرجات مقيدة** - للردود التي تتطلب تنسيقًا معينًا. يتبع النموذج قواعد التنسيق والطول بدقة. استخدم هذا للملخصات أو عندما تحتاج إلى هيكل مخرجات دقيق.

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

<img src="../../../translated_images/ar/constrained-output-pattern.0ce39a682a6795c2.webp" alt="نمط المخرجات المقيدة" width="800"/>

*فرض متطلبات تنسيق وطول وبنية محددة*

## استخدام موارد Azure الحالية

**تحقق من النشر:**

تأكد من وجود ملف `.env` في الدليل الجذر مع بيانات اعتماد Azure (تم إنشاؤه خلال الوحدة 01):
```bash
cat ../.env  # يجب أن يعرض AZURE_OPENAI_ENDPOINT و API_KEY و DEPLOYMENT
```

**بدء التطبيق:**

> **ملاحظة:** إذا كنت قد بدأت جميع التطبيقات باستخدام `./start-all.sh` من الوحدة 01، فإن هذه الوحدة تعمل بالفعل على المنفذ 8083. يمكنك تخطي أوامر بدء التشغيل أدناه والبدء مباشرة على http://localhost:8083.

**الخيار 1: استخدام لوحة تحكم Spring Boot (موصى به لمستخدمي VS Code)**

يتضمن حاوية التطوير امتداد لوحة تحكم Spring Boot، الذي يوفر واجهة بصرية لإدارة جميع تطبيقات Spring Boot. يمكنك العثور عليه في شريط النشاط على الجانب الأيسر من VS Code (ابحث عن أيقونة Spring Boot).
من لوحة تحكم Spring Boot، يمكنك:
- رؤية جميع تطبيقات Spring Boot المتاحة في مساحة العمل
- بدء/إيقاف التطبيقات بنقرة واحدة
- عرض سجلات التطبيق في الوقت الحقيقي
- مراقبة حالة التطبيق

فقط انقر على زر التشغيل بجانب "prompt-engineering" لبدء هذه الوحدة، أو ابدأ جميع الوحدات مرة واحدة.

<img src="../../../translated_images/ar/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**الخيار 2: استخدام سكربتات الشيل**

بدء تشغيل جميع تطبيقات الويب (الوحدات 01-04):

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

أو بدء تشغيل هذه الوحدة فقط:

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

يقوم كلا السكربتين تلقائيًا بتحميل متغيرات البيئة من ملف `.env` في الجذر وسيبني ملفات JAR إذا لم تكن موجودة.

> **ملاحظة:** إذا كنت تفضل بناء جميع الوحدات يدويًا قبل البدء:
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

**لإيقاف التشغيل:**

**Bash:**
```bash
./stop.sh  # هذه الوحدة فقط
# أو
cd .. && ./stop-all.sh  # جميع الوحدات
```

**PowerShell:**
```powershell
.\stop.ps1  # هذا الوحدة فقط
# أو
cd ..; .\stop-all.ps1  # جميع الوحدات
```

## لقطات شاشة للتطبيق

<img src="../../../translated_images/ar/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*اللوحة الرئيسية التي تعرض جميع أنماط هندسة الموجهات الثمانية مع خصائصها وحالات الاستخدام*

## استكشاف الأنماط

تتيح لك واجهة الويب تجربة استراتيجيات توجيه مختلفة. كل نمط يحل مشاكل مختلفة - جربها لترى متى يبرز كل أسلوب.

### الاستعداد المنخفض مقابل الاستعداد العالي

اطرح سؤالاً بسيطًا مثل "ما هو 15% من 200؟" باستخدام الاستعداد المنخفض. ستحصل على إجابة فورية ومباشرة. الآن اطرح شيئًا معقدًا مثل "صمم استراتيجية تخزين مؤقت لواجهة برمجة تطبيقات ذات حركة مرور عالية" باستخدام الاستعداد العالي. شاهد كيف يتباطأ النموذج ويقدم شرحًا تفصيليًا. نفس النموذج، نفس هيكل السؤال - لكن التوجيه يخبره بكمية التفكير المطلوبة.

<img src="../../../translated_images/ar/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*حساب سريع مع حد أدنى من التفكير*

<img src="../../../translated_images/ar/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*استراتيجية تخزين مؤقت شاملة (2.8MB)*

### تنفيذ المهام (مقدمات الأدوات)

تستفيد سير العمل متعددة الخطوات من التخطيط المسبق والسرد التدريجي. يشرح النموذج ما سيفعله، يروي كل خطوة، ثم يلخص النتائج.

<img src="../../../translated_images/ar/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*إنشاء نقطة نهاية REST مع سرد خطوة بخطوة (3.9MB)*

### الكود المنعكس ذاتيًا

جرب "إنشاء خدمة تحقق بريد إلكتروني". بدلاً من مجرد توليد الكود والتوقف، يقوم النموذج بالتوليد، والتقييم وفق معايير الجودة، وتحديد نقاط الضعف، والتحسين. سترى كيف يكرر العملية حتى يفي الكود بمعايير الإنتاج.

<img src="../../../translated_images/ar/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*خدمة تحقق بريد إلكتروني كاملة (5.2MB)*

### التحليل المنظم

تحتاج مراجعات الكود إلى أُطر تقييم ثابتة. يقوم النموذج بتحليل الكود باستخدام فئات محددة (الصحة، الممارسات، الأداء، الأمن) مع مستويات شدة.

<img src="../../../translated_images/ar/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*مراجعة كود بناءً على إطار عمل*

### المحادثات متعددة الجولات

اسأل "ما هو Spring Boot؟" ثم تابع فورًا بسؤال "أرني مثالاً". يتذكر النموذج سؤالك الأول ويعطيك مثالًا خاصًا بـ Spring Boot. بدون ذاكرة، سيكون السؤال الثاني عامًا جدًا.

<img src="../../../translated_images/ar/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*الحفاظ على السياق عبر الأسئلة*

### التفكير خطوة بخطوة

اختر مسألة رياضية وجربها باستخدام كل من التفكير خطوة بخطوة والاستعداد المنخفض. الاستعداد المنخفض يعطيك فقط الإجابة - سريع لكنه غامض. الخطوة بخطوة تعرض كل عملية حسابية وقرار.

<img src="../../../translated_images/ar/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*مسألة رياضية بخطوات صريحة*

### الإخراج المقيد

عندما تحتاج إلى تنسيقات أو عدد كلمات محدد، يفرض هذا النمط الالتزام الصارم. جرب توليد ملخص يحتوي على 100 كلمة بالضبط بتنسيق نقاط.

<img src="../../../translated_images/ar/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*ملخص تعلم آلي مع التحكم في التنسيق*

## ما تتعلمه حقًا

**جهد التفكير يغير كل شيء**

يتيح لك GPT-5.2 التحكم في الجهد الحسابي عبر توجيهاتك. الجهد المنخفض يعني ردود سريعة مع استكشاف قليل. الجهد العالي يعني أن النموذج يأخذ وقتًا للتفكير بعمق. أنت تتعلم مواءمة الجهد مع تعقيد المهمة - لا تضيع الوقت على أسئلة بسيطة، لكن لا تسرع القرارات المعقدة أيضًا.

**البنية توجه السلوك**

هل لاحظت علامات XML في التوجيهات؟ ليست للزينة. تتبع النماذج التعليمات الهيكلية بشكل أكثر موثوقية من النص الحر. عندما تحتاج إلى عمليات متعددة الخطوات أو منطق معقد، تساعد البنية النموذج على تتبع موقعه وما يلي ذلك.

<img src="../../../translated_images/ar/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*هيكل توجيه منظم جيدًا مع أقسام واضحة وتنظيم بأسلوب XML*

**الجودة عبر التقييم الذاتي**

تعمل الأنماط ذات الانعكاس الذاتي عن طريق جعل معايير الجودة صريحة. بدلًا من الأمل في أن "يفعل النموذج ذلك بشكل صحيح"، تخبره بالضبط ما يعنيه "الصحيح": منطق صحيح، معالجة الأخطاء، الأداء، الأمان. يمكن للنموذج تقييم مخرجاته وتحسينها. هذا يحول توليد الكود من لعبة حظ إلى عملية.

**السياق محدود**

تعمل المحادثات متعددة الجولات من خلال تضمين سجل الرسائل مع كل طلب. لكن هناك حدًا - لكل نموذج حد أقصى لعدد الرموز. مع نمو المحادثات، ستحتاج استراتيجيات للحفاظ على السياق ذي الصلة دون الوصول إلى الحد. توضح لك هذه الوحدة كيفية عمل الذاكرة؛ وستتعلم لاحقًا متى تلخص، ومتى تنسى، ومتى تسترجع.

## الخطوات التالية

**الوحدة التالية:** [03-rag - RAG (التوليد المعزز بالاسترجاع)](../03-rag/README.md)

---

**التنقل:** [← السابق: الوحدة 01 - مقدمة](../01-introduction/README.md) | [العودة إلى الرئيسي](../README.md) | [التالي: الوحدة 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**إخلاء المسؤولية**:  
تمت ترجمة هذا المستند باستخدام خدمة الترجمة بالذكاء الاصطناعي [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى لتحقيق الدقة، يرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو معلومات غير دقيقة. يجب اعتبار الوثيقة الأصلية بلغتها الأصلية المصدر الرسمي والموثوق. للمعلومات الهامة، يُنصح بالاستعانة بترجمة بشرية محترفة. نحن غير مسؤولين عن أي سوء فهم أو تفسير خاطئ قد ينشأ عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->