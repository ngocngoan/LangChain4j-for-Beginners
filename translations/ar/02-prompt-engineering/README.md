# الوحدة 02: هندسة الموجهات مع GPT-5.2

## جدول المحتويات

- [ما الذي ستتعلمه](../../../02-prompt-engineering)
- [المتطلبات الأساسية](../../../02-prompt-engineering)
- [فهم هندسة الموجهات](../../../02-prompt-engineering)
- [أساسيات هندسة الموجهات](../../../02-prompt-engineering)
  - [الموجه بدون أمثلة](../../../02-prompt-engineering)
  - [الموجه مع أمثلة قليلة](../../../02-prompt-engineering)
  - [سلسلة التفكير](../../../02-prompt-engineering)
  - [الموجه المبني على الدور](../../../02-prompt-engineering)
  - [قوالب الموجه](../../../02-prompt-engineering)
- [الأنماط المتقدمة](../../../02-prompt-engineering)
- [استخدام موارد Azure الحالية](../../../02-prompt-engineering)
- [لقطات الشاشة للتطبيق](../../../02-prompt-engineering)
- [استكشاف الأنماط](../../../02-prompt-engineering)
  - [الحماس المنخفض مقابل العالي](../../../02-prompt-engineering)
  - [تنفيذ المهام (مقدمات الأدوات)](../../../02-prompt-engineering)
  - [الكود الذي يعكس نفسه](../../../02-prompt-engineering)
  - [التحليل المهيكل](../../../02-prompt-engineering)
  - [دردشة متعددة الجولات](../../../02-prompt-engineering)
  - [التفكير خطوة بخطوة](../../../02-prompt-engineering)
  - [الإخراج المقيد](../../../02-prompt-engineering)
- [ما الذي تتعلمه حقًا](../../../02-prompt-engineering)
- [الخطوات التالية](../../../02-prompt-engineering)

## ما الذي ستتعلمه

<img src="../../../translated_images/ar/what-youll-learn.c68269ac048503b2.webp" alt="ما الذي ستتعلمه" width="800"/>

في الوحدة السابقة، رأيت كيف تمكّن الذاكرة الذكاء الاصطناعي الحواري واستخدمت نماذج GitHub للتفاعلات الأساسية. الآن سنركز على كيفية طرح الأسئلة — أي الموجهات نفسها — باستخدام GPT-5.2 من Azure OpenAI. الطريقة التي تبني بها موجهاتك تؤثر بشكل كبير على جودة الردود التي تحصل عليها. نبدأ بمراجعة تقنيات الموجهات الأساسية، ثم ننتقل إلى ثمانية أنماط متقدمة تستغل كامل قدرات GPT-5.2.

سوف نستخدم GPT-5.2 لأنه يقدم تحكمًا في التفكير — يمكنك إخبار النموذج بكمية التفكير التي يجب القيام بها قبل الإجابة. هذا يجعل استراتيجيات الموجهات المختلفة أكثر وضوحًا ويساعدك على فهم متى تستخدم كل نهج. كما سنستفيد من قيود المعدل الأقل على GPT-5.2 في Azure مقارنة بنماذج GitHub.

## المتطلبات الأساسية

- إتمام الوحدة 01 (تم نشر موارد Azure OpenAI)
- ملف `.env` في الدليل الجذر يحتوي على بيانات اعتماد Azure (تم إنشاؤه بواسطة `azd up` في الوحدة 01)

> **ملاحظة:** إذا لم تكمل الوحدة 01، اتبع تعليمات النشر هناك أولاً.

## فهم هندسة الموجهات

<img src="../../../translated_images/ar/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="ما هي هندسة الموجهات؟" width="800"/>

هندسة الموجهات تتعلق بتصميم النص المدخل الذي يضمن باستمرار حصولك على النتائج التي تحتاجها. الأمر لا يقتصر على طرح الأسئلة فقط - بل يتعلق ببناء الطلبات بحيث يفهم النموذج بالضبط ما تريد وكيف يقدمه.

فكر فيه مثل إعطاء تعليمات لزميل. "صلح الخطأ" عبارة غامضة. "صلح استثناء المؤشر الخالي في UserService.java السطر 45 عن طريق إضافة فحص مؤشر خالي" هو محدد. نماذج اللغة تعمل بنفس الطريقة — الدقة والبناء مهمان.

<img src="../../../translated_images/ar/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="كيف يتناسب LangChain4j" width="800"/>

يوفر LangChain4j البنية التحتية — الاتصالات بالنموذج، والذاكرة، وأنواع الرسائل — بينما أنماط الموجهات هي مجرد نصوص منظمة بعناية ترسل عبر هذه البنية التحتية. الركائز الأساسية هي `SystemMessage` (التي تحدد سلوك ودور الذكاء الاصطناعي) و`UserMessage` (التي تحمل طلبك الفعلي).

## أساسيات هندسة الموجهات

<img src="../../../translated_images/ar/five-patterns-overview.160f35045ffd2a94.webp" alt="مراجعة خمسة أنماط لهندسة الموجهات" width="800"/>

قبل الغوص في الأنماط المتقدمة في هذه الوحدة، دعنا نراجع خمسة تقنيات موجهات أساسية. هذه هي اللبنات الأساسية التي يجب أن يعرفها كل مهندس موجهات. إذا كنت قد عملت بالفعل من خلال [وحدة البداية السريعة](../00-quick-start/README.md#2-prompt-patterns)، فقد شاهدت هذه الأنماط قيد التنفيذ — إليك الإطار المفاهيمي خلفها.

### الموجه بدون أمثلة

النهج الأبسط: تعطي النموذج تعليمات مباشرة بدون أمثلة. يعتمد النموذج كليًا على تدريبه لفهم وتنفيذ المهمة. هذا يعمل جيدًا للطلبات المباشرة حيث السلوك المتوقع واضح.

<img src="../../../translated_images/ar/zero-shot-prompting.7abc24228be84e6c.webp" alt="الموجه بدون أمثلة" width="800"/>

*تعليمات مباشرة بدون أمثلة — النموذج يستنتج المهمة من التعليمات فقط*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// الاستجابة: "إيجابي"
```

**متى تستخدمه:** التصنيفات البسيطة، الأسئلة المباشرة، الترجمات، أو أي مهمة يمكن للنموذج التعامل معها دون توجيه إضافي.

### الموجه مع أمثلة قليلة

قدّم أمثلة توضح النمط الذي تريد للنموذج اتباعه. يتعلم النموذج تنسيق الإدخال والإخراج المتوقع من أمثلتك ويطبقه على مدخلات جديدة. هذا يحسن الاتساق بشكل كبير للمهام التي لا يكون فيها تنسيق أو سلوك مرغوب واضح.

<img src="../../../translated_images/ar/few-shot-prompting.9d9eace1da88989a.webp" alt="الموجه مع أمثلة قليلة" width="800"/>

*التعلم من الأمثلة — النموذج يحدد النمط ويطبقه على مدخلات جديدة*

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

**متى تستخدمه:** التصنيفات المخصصة، التنسيق المتسق، المهام الخاصة بالمجال، أو عندما تكون نتائج الموجه بدون أمثلة غير متسقة.

### سلسلة التفكير

اطلب من النموذج عرض تفكيره خطوة بخطوة. بدلاً من القفز مباشرة للإجابة، يكسر النموذج المشكلة ويعمل على كل جزء بشكل واضح. هذا يحسن الدقة في الرياضيات، المنطق، والمهام ذات التفكير متعدد الخطوات.

<img src="../../../translated_images/ar/chain-of-thought.5cff6630e2657e2a.webp" alt="الموجه بسلسلة التفكير" width="800"/>

*التفكير خطوة بخطوة — تفكيك المشكلات المعقدة إلى خطوات منطقية صريحة*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// يوضح النموذج: ١٥ - ٨ = ٧، ثم ٧ + ١٢ = ١٩ تفاحة
```

**متى تستخدمه:** مسائل الرياضيات، الألغاز المنطقية، تصحيح الأخطاء، أو أي مهمة حيث يؤدي إظهار عملية التفكير إلى تحسين الدقة والثقة.

### الموجه المبني على الدور

حدد شخصًا أو دورًا للذكاء الاصطناعي قبل طرح سؤالك. هذا يوفر سياقًا يشكل نغمة العمق والتركيز في الرد. "مهندس برمجيات" يعطي نصيحة مختلفة عن "مطور مبتدئ" أو "مراجع أمني".

<img src="../../../translated_images/ar/role-based-prompting.a806e1a73de6e3a4.webp" alt="الموجه المبني على الدور" width="800"/>

*تحديد السياق والشخصية — نفس السؤال يحصل على رد مختلف بناءً على الدور المعين*

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

**متى تستخدمه:** مراجعات الكود، التدريس، التحليل الخاص بالمجال، أو عندما تحتاج ردودًا مخصصة لمستوى خبرة أو منظور معين.

### قوالب الموجه

أنشئ موجهات قابلة لإعادة الاستخدام مع متغيرات مكانية. بدلاً من كتابة موجه جديد في كل مرة، عرّف قالبًا مرة واحدة واملأ قيم مختلفة. تجعل فئة `PromptTemplate` في LangChain4j هذا سهلاً باستخدام بناء الجملة `{{variable}}`.

<img src="../../../translated_images/ar/prompt-templates.14bfc37d45f1a933.webp" alt="قوالب الموجه" width="800"/>

*موجهات قابلة لإعادة الاستخدام مع متغيرات مكانية — قالب واحد، استخدامات متعددة*

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

**متى تستخدمها:** الاستعلامات المتكررة مع مدخلات مختلفة، المعالجة الدُفعات، بناء سير عمل ذكاء اصطناعي قابل لإعادة الاستخدام، أو أي سيناريو حيث يبقى هيكل الموجه ثابتًا لكن البيانات تتغير.

---

تقدم هذه الأساسيات الخمسة مجموعة أدوات متينة لمعظم مهام الموجهات. يبني بقية هذه الوحدة عليها مع **ثمانية أنماط متقدمة** تستفيد من تحكم GPT-5.2 في التفكير، والتقييم الذاتي، وقدرات الإخراج المهيكل.

## الأنماط المتقدمة

بعد تغطية الأساسيات، لننتقل إلى الأنماط المتقدمة الثمانية التي تجعل هذه الوحدة فريدة. ليست كل المشكلات تحتاج إلى نفس النهج. بعض الأسئلة تحتاج إجابات سريعة، وأخرى تحتاج تفكيرًا عميقًا. بعضها يحتاج تفكيرًا مرئيًا، وبعضها يحتاج فقط إلى النتائج. كل نمط أدناه مُحسّن لسيناريو مختلف — وتحكم التفكير في GPT-5.2 يجعل الفروقات أكثر وضوحًا.

<img src="../../../translated_images/ar/eight-patterns.fa1ebfdf16f71e9a.webp" alt="ثمانية أنماط للهندسة الموجهة" width="800"/>

*نظرة عامة على ثمانية أنماط لهندسة الموجهات وحالات استخدامها*

<img src="../../../translated_images/ar/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="تحكم التفكير مع GPT-5.2" width="800"/>

*تحكم GPT-5.2 في التفكير يتيح لك تحديد مدى التفكير الذي يجب على النموذج القيام به — من الإجابات السريعة المباشرة إلى الاستكشاف العميق*

**الحماس المنخفض (سريع ومركز)** - للأسئلة البسيطة حيث تريد إجابات سريعة ومباشرة. يقوم النموذج بأدنى قدر من التفكير - بحد أقصى خطوتين. استخدم هذا للحسابات، البحث، أو الأسئلة المباشرة.

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **استكشف مع GitHub Copilot:** افتح [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) واسأل:
> - "ما الفرق بين أنماط الموجه الحماس المنخفض والحماس العالي؟"
> - "كيف تساعد علامات XML في الموجهات على هيكلة استجابة الذكاء الاصطناعي؟"
> - "متى يجب أن أستخدم أنماط التفكير الذاتي مقابل التعليم المباشر؟"

**الحماس العالي (عميق وشامل)** - للمشكلات المعقدة حيث تريد تحليلًا شاملاً. يستكشف النموذج بعمق ويعرض تفكيرًا مفصلًا. استخدم هذا لتصميم النظام، قرارات الهندسة المعمارية، أو البحوث المعقدة.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**تنفيذ المهام (تقدم خطوة بخطوة)** - لسير العمل متعدد الخطوات. يوفر النموذج خطة مسبقة، يروي كل خطوة أثناء العمل، ثم يعطي ملخصًا. استخدم هذا للهجرات، التنفيذات، أو أي عملية متعددة الخطوات.

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

الموجه بسلسلة التفكير يطلب صراحة من النموذج عرض عملية تفكيره، مما يحسن الدقة للمهام المعقدة. التفصيل خطوة بخطوة يساعد كل من البشر والذكاء الاصطناعي على فهم المنطق.

> **🤖 جرب مع [GitHub Copilot](https://github.com/features/copilot) Chat:** اسأل عن هذا النمط:
> - "كيف أُكيف نمط تنفيذ المهام للعمليات طويلة الأمد؟"
> - "ما هي أفضل الممارسات لترتيب مقدمات الأدوات في تطبيقات الإنتاج؟"
> - "كيف يمكنني التقاط وعرض تحديثات التقدم المتوسطة في واجهة المستخدم؟"

<img src="../../../translated_images/ar/task-execution-pattern.9da3967750ab5c1e.webp" alt="نمط تنفيذ المهام" width="800"/>

*تخطيط → تنفيذ → تلخيص سير العمل للمهام متعددة الخطوات*

**الكود الذي يعكس نفسه** - لإنتاج كود بجودة الإنتاج. يولد النموذج كودًا متبعًا معايير الإنتاج مع معالجة صحيحة للأخطاء. استخدم هذا عند بناء ميزات أو خدمات جديدة.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ar/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="دورة التفكير الذاتي" width="800"/>

*حلقة تحسين تكرارية - توليد، تقييم، تحديد المشاكل، تحسين، تكرار*

**التحليل المهيكل** - للتقييم المتسق. يراجع النموذج الكود باستخدام إطار عمل ثابت (الصحة، الممارسات، الأداء، الأمان، سهولة الصيانة). استخدم هذا لمراجعات الكود أو تقييم الجودة.

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 جرب مع [GitHub Copilot](https://github.com/features/copilot) Chat:** اسأل عن التحليل المهيكل:
> - "كيف يمكنني تخصيص إطار التحليل لأنواع مختلفة من مراجعات الكود؟"
> - "ما هي أفضل طريقة لتحليل والتصرف في الإخراج المهيكل برمجيًا؟"
> - "كيف أضمن مستويات شدة متسقة عبر جلسات مراجعة مختلفة؟"

<img src="../../../translated_images/ar/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="نمط التحليل المهيكل" width="800"/>

*إطار عمل لمراجعات كود متسقة بمستويات شدة محددة*

**دردشة متعددة الجولات** - للمحادثات التي تحتاج إلى سياق. يتذكر النموذج الرسائل السابقة ويبني عليها. استخدم هذا لجلسات المساعدة التفاعلية أو الأسئلة المعقدة.

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

*كيف يتراكم سياق المحادثة عبر جولات متعددة حتى الوصول إلى حد الرموز*

**التفكير خطوة بخطوة** - للمشكلات التي تتطلب منطقًا مرئيًا. يعرض النموذج تفكيرًا صريحًا لكل خطوة. استخدم هذا لمشاكل الرياضيات، الألغاز المنطقية، أو عندما تحتاج لفهم عملية التفكير.

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

*تفكيك المشكلات إلى خطوات منطقية صريحة*

**الإخراج المقيد** - للردود ذات متطلبات تنسيق محددة. يتبع النموذج بدقة قواعد التنسيق والطول. استخدم هذا للملخصات أو عندما تحتاج إلى هيكل إخراج دقيق.

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

<img src="../../../translated_images/ar/constrained-output-pattern.0ce39a682a6795c2.webp" alt="نمط الإخراج المقيد" width="800"/>

*فرض متطلبات التنسيق والطول والبنية المحددة*

## استخدام موارد Azure الحالية

**تحقق من النشر:**

تأكد من وجود ملف `.env` في الدليل الجذر يحتوي على بيانات اعتماد Azure (تم إنشاؤه أثناء الوحدة 01):
```bash
cat ../.env  # يجب عرض AZURE_OPENAI_ENDPOINT، API_KEY، DEPLOYMENT
```

**تشغيل التطبيق:**

> **ملاحظة:** إذا كنت قد بدأت جميع التطبيقات بالفعل باستخدام `./start-all.sh` من الوحدة 01، فإن هذه الوحدة تعمل بالفعل على المنفذ 8083. يمكنك تخطي أوامر البدء أدناه والانتقال مباشرة إلى http://localhost:8083.

**الخيار 1: استخدام لوحة تحكم Spring Boot (مُوصى به لمستخدمي VS Code)**

تتضمن حاوية التطوير إضافة لوحة تحكم Spring Boot، التي توفر واجهة مرئية لإدارة جميع تطبيقات Spring Boot. يمكنك العثور عليها في شريط النشاط على الجانب الأيسر من VS Code (ابحث عن أيقونة Spring Boot).

من خلال لوحة تحكم Spring Boot، يمكنك:
- رؤية جميع تطبيقات Spring Boot المتاحة في مساحة العمل
- بدء/إيقاف التطبيقات بنقرة واحدة
- عرض سجلات التطبيقات في الوقت الفعلي
- مراقبة حالة التطبيق
فقط انقر على زر التشغيل بجانب "prompt-engineering" لبدء هذه الوحدة، أو ابدأ جميع الوحدات مرة واحدة.

<img src="../../../translated_images/ar/dashboard.da2c2130c904aaf0.webp" alt="لوحة تحكم Spring Boot" width="400"/>

**الخيار 2: استخدام سكريبتات الشيل**

ابدأ جميع تطبيقات الويب (الوحدات 01-04):

**Bash:**
```bash
cd ..  # من الدليل الجذر
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # من الدليل الجذري
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

يقوم كلا السكريبتين بتحميل متغيرات البيئة تلقائيًا من ملف `.env` الجذر وسيبني ملفات JAR إذا لم تكن موجودة.

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

**لإيقاف:**

**Bash:**
```bash
./stop.sh  # هذا الوحدة فقط
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

<img src="../../../translated_images/ar/dashboard-home.5444dbda4bc1f79d.webp" alt="الصفحة الرئيسية للوحة التحكم" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*لوحة التحكم الرئيسية تعرض كل 8 أنماط هندسة البرمجة مع خصائصها وحالات استخدامها*

## استكشاف الأنماط

تتيح لك الواجهة الإلكترونية تجربة استراتيجيات توفير الأوامر المختلفة. يحل كل نمط مشاكل مختلفة - جربها لترى متى يبرز كل نهج.

> **ملاحظة: البث مقابل عدم البث** — تقدم كل صفحة نمط زرين: **🔴 بث الاستجابة (مباشر)** وخيار **غير البث**. يستخدم البث أحداث الخادم المرسلة (SSE) لعرض الرموز في الوقت الفعلي أثناء توليد النموذج لها، لذا ترى التقدم على الفور. خيار عدم البث ينتظر الاستجابة كاملة قبل عرضها. بالنسبة للأوامر التي تحفز تفكيرًا عميقًا (مثل High Eagerness وSelf-Reflecting Code)، قد يستغرق الاتصال غير البث وقتًا طويلاً — أحيانًا دقائق — بدون أي ردود مرئية. **استخدم البث عند تجربة أوامر معقدة** حتى ترى النموذج يعمل وتتجنب الانطباع بأن الطلب انتهت صلاحيته.  
>
> **ملاحظة: متطلبات المتصفح** — تستخدم ميزة البث Fetch Streams API (`response.body.getReader()`) والتي تتطلب متصفحًا كاملاً (كروم، إيدج، فايرفوكس، سفاري). لا تعمل في متصفح VS Code البسيط المدمج، حيث أن عارض الويب الخاص به لا يدعم ReadableStream API. إذا استخدمت المتصفح البسيط، ستظل أزرار عدم البث تعمل بشكل طبيعي — فقط أزرار البث هي المتأثرة. افتح `http://localhost:8083` في متصفح خارجي لتجربة كاملة.

### انخفاض مقابل ارتفاع الحماس

اسأل سؤالًا بسيطًا مثل "ما هو 15% من 200؟" باستخدام Low Eagerness. ستحصل على إجابة فورية ومباشرة. الآن اسأل شيئًا معقدًا مثل "صمم استراتيجية تخزين مؤقت لواجهة برمجة التطبيقات ذات الحركة العالية" باستخدام High Eagerness. انقر على **🔴 بث الاستجابة (مباشر)** وشاهد تفكير النموذج المفصل يظهر رمزًا برمز. نفس النموذج، نفس هيكل السؤال - لكن الأمر يخبره كم يفكر.

### تنفيذ المهام (المقدمات الأداة)

تفيد سير العمل متعدد الخطوات من التخطيط المسبق وسرد التقدم. يحدد النموذج ما سيفعله، يروي كل خطوة، ثم يلخص النتائج.

### الكود ذو التفكير الذاتي

جرب "أنشئ خدمة للتحقق من صحة البريد الإلكتروني". بدلًا من توليد الكود والتوقف، يقوم النموذج بالإنشاء، والتقييم مقابل معايير الجودة، وتحديد نقاط الضعف، والتحسين. سترى تكراره حتى يلبي الكود معايير الإنتاج.

### التحليل المنظم

تحتاج مراجعات الكود إلى أطر تقييم ثابتة. يحلل النموذج الكود باستخدام فئات ثابتة (الصحة، الممارسات، الأداء، الأمان) بمستويات شدة.

### المحادثة متعددة الأدوار

اسأل "ما هو Spring Boot؟" ثم تابع فورًا بـ "أرني مثالًا". يتذكر النموذج سؤالك الأول ويعطيك مثال Spring Boot خاصة. بدون ذاكرة، سيكون السؤال الثاني غامضًا جدًا.

### التفكير خطوة بخطوة

اختر مسألة رياضية وجربها باستخدام كل من التفكير خطوة بخطوة وLow Eagerness. الحماس المنخفض يعطيك الإجابة فقط - سريع لكن غير واضح. التفكير خطوة بخطوة يعرض لك كل حساب وقرار.

### الإخراج المقيد

عندما تحتاج تنسيقات محددة أو عدد كلمات معين، يفرض هذا النمط الالتزام الصارم. جرب توليد ملخص مكون من 100 كلمة بالضبط بنقاط.

## ما تتعلمه بالفعل

**جهد التفكير يغير كل شيء**

يتيح لك GPT-5.2 التحكم في الجهد الحاسوبي من خلال الأوامر. الجهد المنخفض يعني استجابات سريعة مع استكشاف بسيط. الجهد العالي يعني أن النموذج يأخذ وقتًا للتفكير بعمق. أنت تتعلم كيف تطابق الجهد مع تعقيد المهمة - لا تهدر الوقت على الأسئلة البسيطة، ولا تستعجل في القرارات المعقدة أيضًا.

**الهيكلة توجه السلوك**

هل لاحظت علامات XML في الأوامر؟ ليست للزينة. تتبع النماذج التعليمات المنظمة بشكل أكثر موثوقية من النص الحر. عندما تحتاج عمليات متعددة الخطوات أو منطق معقد، تساعد الهيكلة النموذج على تتبع مكانه وما يأتي بعده.

<img src="../../../translated_images/ar/prompt-structure.a77763d63f4e2f89.webp" alt="هيكلة الأمر" width="800"/>

*تشريح أمر منظم جيدًا بأقسام واضحة وتنظيم بأسلوب XML*

**الجودة من خلال التقييم الذاتي**

تعمل أنماط التفكير الذاتي بجعل معايير الجودة صريحة. بدلًا من الأمل بأن "ينجز النموذج الأمر بشكل صحيح"، تخبره بالضبط ماذا يعني "صحيح": منطق سليم، معالجة للأخطاء، أداء، أمان. ثم يستطيع النموذج تقييم مخرجاته وتحسينها. هذا يحول توليد الكود من لعب حظ إلى عملية.

**السياق محدود**

تعمل المحادثات متعددة الأدوار عن طريق تضمين سجل الرسائل مع كل طلب. لكن هناك حد - لكل نموذج عدد أقصى من الرموز. مع نمو المحادثات، ستحتاج استراتيجيات للحفاظ على السياق المهم دون الوصول إلى هذا الحد. تعرض هذه الوحدة كيف تعمل الذاكرة؛ ستتعلم لاحقًا متى تلخص، متى تنسى، ومتى تسترجع.

## الخطوات التالية

**الوحدة التالية:** [03-rag - RAG (التوليد المعزز بالاستخراج)](../03-rag/README.md)

---

**التنقل:** [← السابق: الوحدة 01 - مقدمة](../01-introduction/README.md) | [العودة إلى الرئيسي](../README.md) | [التالي: الوحدة 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**إخلاء المسؤولية**:
تمت ترجمة هذا المستند باستخدام خدمة الترجمة الآلية [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى لتحقيق الدقة، يُرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار المستند الأصلي بلغته الأصلية المصدر الموثوق به. بالنسبة للمعلومات الحرجة، يُنصح بالاعتماد على الترجمة البشرية المهنية. نحن غير مسؤولين عن أي سوء فهم أو تفسير خاطئ ناتج عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->