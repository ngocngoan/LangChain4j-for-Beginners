# الوحدة 02: هندسة التهيئة مع GPT-5.2

## جدول المحتويات

- [شرح بالفيديو](../../../02-prompt-engineering)
- [ما الذي ستتعلمه](../../../02-prompt-engineering)
- [المتطلبات المسبقة](../../../02-prompt-engineering)
- [فهم هندسة التهيئة](../../../02-prompt-engineering)
- [أساسيات هندسة التهيئة](../../../02-prompt-engineering)
  - [تهيئة بدون أمثلة](../../../02-prompt-engineering)
  - [تهيئة قليلة الأمثلة](../../../02-prompt-engineering)
  - [سلسلة التفكير](../../../02-prompt-engineering)
  - [تهيئة بناءً على الدور](../../../02-prompt-engineering)
  - [قوالب التهيئة](../../../02-prompt-engineering)
- [الأنماط المتقدمة](../../../02-prompt-engineering)
- [استخدام موارد Azure الموجودة](../../../02-prompt-engineering)
- [لقطات شاشة التطبيق](../../../02-prompt-engineering)
- [استكشاف الأنماط](../../../02-prompt-engineering)
  - [الحماسة المنخفضة مقابل العالية](../../../02-prompt-engineering)
  - [تنفيذ المهام (تمهيدات الأدوات)](../../../02-prompt-engineering)
  - [الكود العاكس لنفسه](../../../02-prompt-engineering)
  - [التحليل الهيكلي](../../../02-prompt-engineering)
  - [الدردشة متعددة الأدوار](../../../02-prompt-engineering)
  - [التفكير خطوة بخطوة](../../../02-prompt-engineering)
  - [الإخراج المقيد](../../../02-prompt-engineering)
- [ما الذي تتعلمه حقًا](../../../02-prompt-engineering)
- [الخطوات التالية](../../../02-prompt-engineering)

## شرح بالفيديو

شاهد هذه الجلسة المباشرة التي تشرح كيفية البدء بهذه الوحدة: [هندسة التهيئة مع LangChain4j - الجلسة المباشرة](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## ما الذي ستتعلمه

<img src="../../../translated_images/ar/what-youll-learn.c68269ac048503b2.webp" alt="ما الذي ستتعلمه" width="800"/>

في الوحدة السابقة، شاهدت كيف تمكن الذاكرة الذكاء الاصطناعي المحادثي واستخدمت نماذج GitHub للتفاعلات الأساسية. الآن سنركز على كيفية طرح الأسئلة — التهيئات نفسها — باستخدام GPT-5.2 من Azure OpenAI. طريقة هيكلة التهيئات تؤثر بشكل جذري على جودة الردود التي تحصل عليها. نبدأ بمراجعة تقنيات التهيئة الأساسية، ثم ننتقل إلى ثمانية أنماط متقدمة تستفيد بالكامل من قدرات GPT-5.2.

سنستخدم GPT-5.2 لأنه يقدم تحكمًا في التفكير - يمكنك أن تخبر النموذج بكمية التفكير التي يجب أن يقوم بها قبل الإجابة. هذا يجعل استراتيجيات التهيئة المختلفة أكثر وضوحًا ويساعدك على فهم متى تستخدم كل طريقة. كما سنستفيد من القيود الأقل لمعدلات استخدام GPT-5.2 في Azure مقارنة بنماذج GitHub.

## المتطلبات المسبقة

- إنهاء الوحدة 01 (نشر موارد Azure OpenAI)
- وجود ملف `.env` في الدليل الجذري مع بيانات اعتماد Azure (تم إنشاؤه بواسطة `azd up` في الوحدة 01)

> **ملاحظة:** إذا لم تكمل الوحدة 01، فاتبع تعليمات النشر هناك أولاً.

## فهم هندسة التهيئة

<img src="../../../translated_images/ar/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="ما هي هندسة التهيئة؟" width="800"/>

هندسة التهيئة تعني تصميم نص الإدخال بحيث تحصل باستمرار على النتائج التي تحتاجها. الأمر ليس مجرد طرح أسئلة - بل هو تنظيم الطلبات بحيث يفهم النموذج بالضبط ما تريد وكيفية تقديمه.

فكر في الأمر كأنك تعطي تعليمات لزميل. "إصلاح الخطأ" أمر غامض. "إصلاح استثناء المؤشر الخالي في ملف UserService.java السطر 45 بإضافة فحص null" أمر محدد. تعمل نماذج اللغة بنفس الطريقة - الدقة والهيكلة مهمتان.

<img src="../../../translated_images/ar/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="كيف يتناسب LangChain4j" width="800"/>

يوفر LangChain4j البنية التحتية — اتصالات النموذج، الذاكرة، وأنواع الرسائل — بينما أنماط التهيئة هي نصوص منظمة بعناية ترسل من خلال هذه البنية. اللبنات الأساسية هي `SystemMessage` (التي تضبط سلوك ودور الذكاء الاصطناعي) و`UserMessage` (التي تحمل طلبك الفعلي).

## أساسيات هندسة التهيئة

<img src="../../../translated_images/ar/five-patterns-overview.160f35045ffd2a94.webp" alt="نظرة عامة على خمسة أنماط لهندسة التهيئة" width="800"/>

قبل الخوض في الأنماط المتقدمة في هذه الوحدة، دعنا نراجع خمس تقنيات تهيئة أساسية. هذه هي اللبنات التي يجب أن يعرفها كل مهندس تهيئة. إذا كنت قد عملت بالفعل من خلال [وحدة بدء سريعة](../00-quick-start/README.md#2-prompt-patterns)، فقد شاهدت هذه المعلومات في العمل — هذه هي الإطار النظري وراءها.

### تهيئة بدون أمثلة

أبسط طريقة: أعط النموذج تعليمات مباشرة بدون أمثلة. يعتمد النموذج كليًا على تدريبه لفهم وتنفيذ المهمة. يعمل هذا جيدًا للطلبات البسيطة حيث يكون السلوك المتوقع واضحًا.

<img src="../../../translated_images/ar/zero-shot-prompting.7abc24228be84e6c.webp" alt="تهيئة بدون أمثلة" width="800"/>

*تعليمات مباشرة بدون أمثلة — يستنتج النموذج المهمة من التعليمات وحدها*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// الاستجابة: "إيجابي"
```

**متى تستخدمها:** التصنيفات البسيطة، الأسئلة المباشرة، الترجمات، أو أي مهمة يمكن للنموذج التعامل معها دون توجيه إضافي.

### تهيئة قليلة الأمثلة

قدم أمثلة توضح النمط الذي تريد أن يتبعه النموذج. يتعلم النموذج تنسيق الإدخال-الإخراج المتوقع من أمثلتك ويطبقه على المدخلات الجديدة. هذا يحسن الاتساق بشكل كبير للمهام التي يكون فيها التنسيق أو السلوك المطلوب غير واضح.

<img src="../../../translated_images/ar/few-shot-prompting.9d9eace1da88989a.webp" alt="تهيئة قليلة الأمثلة" width="800"/>

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

**متى تستخدمها:** التصنيفات المخصصة، التنسيق المتسق، المهام الخاصة بالمجال، أو عندما تكون نتائج التهيئة بدون أمثلة غير متسقة.

### سلسلة التفكير

اطلب من النموذج إظهار تفكيره خطوة بخطوة. بدلاً من القفز مباشرة للإجابة، يقوم النموذج بتفكيك المشكلة ويعمل على كل جزء بوضوح. هذا يحسن الدقة في المسائل الرياضية، المنطق، والمهام متعددة الخطوات.

<img src="../../../translated_images/ar/chain-of-thought.5cff6630e2657e2a.webp" alt="تهيئة سلسلة التفكير" width="800"/>

*التفكير خطوة بخطوة — تفكيك المشكلات المعقدة إلى خطوات منطقية واضحة*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// يُظهر النموذج: 15 - 8 = 7، ثم 7 + 12 = 19 تفاحة
```

**متى تستخدمها:** مسائل الرياضيات، الألغاز المنطقية، التصحيح، أو أي مهمة حيث يعزز إظهار عملية التفكير الدقة والثقة.

### تهيئة بناءً على الدور

حدد شخصية أو دور للذكاء الاصطناعي قبل طرح سؤالك. هذا يوفر سياقًا يشكل نغمة، عمق، وتركيز الرد. "مهندس برمجيات" يقدم نصائح مختلفة عن "مطور مبتدئ" أو "مراجع أمني".

<img src="../../../translated_images/ar/role-based-prompting.a806e1a73de6e3a4.webp" alt="تهيئة بناءً على الدور" width="800"/>

*تحديد السياق والشخصية — نفس السؤال يتلقى ردًا مختلفًا حسب الدور المعين*

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

**متى تستخدمها:** مراجعات الكود، التدريس، التحليل الخاص بالمجال، أو عندما تحتاج ردودًا مخصصة لمستوى خبرة أو وجهة نظر معينة.

### قوالب التهيئة

أنشئ تهيئات قابلة لإعادة الاستخدام مع متغيرات مكانية. بدلاً من كتابة تهيئة جديدة في كل مرة، عرّف قالبًا مرة واملأه بقيم مختلفة. تجعل فئة `PromptTemplate` في LangChain4j هذا الأمر سهلاً باستخدام صيغة `{{variable}}`.

<img src="../../../translated_images/ar/prompt-templates.14bfc37d45f1a933.webp" alt="قوالب التهيئة" width="800"/>

*تهيئات قابلة لإعادة الاستخدام مع متغيرات مكانية — قالب واحد، استخدامات كثيرة*

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

**متى تستخدمها:** استعلامات متكررة بمدخلات مختلفة، المعالجة الدُفعية، بناء سير عمل ذكاء اصطناعي قابل لإعادة الاستخدام، أو أي سيناريو يبقى فيه هيكل التهيئة نفسه ولكن تتغير البيانات.

---

تعطيك هذه الأساسيات الخمسة مجموعة أدوات قوية لمعظم مهام التهيئة. تبني بقية هذه الوحدة على ذلك مع **ثمانية أنماط متقدمة** تستفيد من تحكم التفكير في GPT-5.2، التقييم الذاتي، وإمكانات الإخراج الهيكلي.

## الأنماط المتقدمة

مع تغطية الأساسيات، لننتقل إلى الأنماط المتقدمة الثمانية التي تجعل هذه الوحدة فريدة. ليست كل المشاكل تحتاج نفس الطريقة. بعض الأسئلة تحتاج إجابات سريعة، وأخرى تحتاج تفكيرًا عميقًا. بعضها يحتاج إلى التفكير الظاهر، والبعض الآخر يحتاج فقط إلى النتائج. كل نمط أدناه مُحسن لسيناريو مختلف — وتحكم التفكير في GPT-5.2 يجعل الفروق أكثر وضوحًا.

<img src="../../../translated_images/ar/eight-patterns.fa1ebfdf16f71e9a.webp" alt="ثمانية أنماط للتهيئة" width="800"/>

*نظرة عامة على ثمانية أنماط لهندسة التهيئة وحالات استخدامها*

<img src="../../../translated_images/ar/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="التحكم في التفكير مع GPT-5.2" width="800"/>

*تحكم GPT-5.2 في التفكير يتيح لك تحديد مقدار التفكير الذي يجب أن يقوم به النموذج — من إجابات سريعة مباشرة إلى استكشاف عميق*

**حماسة منخفضة (سريع ومركز)** - للأسئلة البسيطة حيث تريد إجابات سريعة ومباشرة. يقوم النموذج بأدنى قدر من التفكير - بحد أقصى خطوتين. استخدم هذا للحسابات، الاستعلامات، أو الأسئلة المباشرة.

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

> 💡 **استكشف باستخدام GitHub Copilot:** افتح [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) واطرح:
> - "ما الفرق بين أنماط التهيئة ذات الحماسة المنخفضة والحماسة العالية؟"
> - "كيف تساعد علامات XML في التهيئات على هيكلة رد الذكاء الاصطناعي؟"
> - "متى يجب أن أستخدم أنماط الانعكاس الذاتي مقابل التعليمات المباشرة؟"

**حماسة عالية (عميق وشامل)** - للمشاكل المعقدة حيث تريد تحليلًا شاملاً. يستكشف النموذج بالتفصيل ويظهر التفكير المفصل. استخدم هذا لتصميم الأنظمة، قرارات الهندسة المعمارية، أو البحث المعقد.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**تنفيذ المهمة (تقدم خطوة بخطوة)** - لسير عمل متعدد الخطوات. يقدم النموذج خطة أولية، يسرد كل خطوة أثناء التنفيذ، ثم يعطي ملخصًا. استخدم هذا للترحيلات، التنفيذ، أو أي عملية متعددة الخطوات.

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

تهيئة سلسلة التفكير تطلب من النموذج بشكل صريح إظهار عملية تفكيره، مما يحسن الدقة في المهام المعقدة. يساعد التفكيك خطوة بخطوة البشر والذكاء الاصطناعي على فهم المنطق.

> **🤖 جرب مع دردشة [GitHub Copilot](https://github.com/features/copilot):** اسأل عن هذا النمط:
> - "كيف يمكنني تكييف نمط تنفيذ المهمة للعمليات طويلة الأمد؟"
> - "ما هي أفضل الممارسات لهياكلة تمهيدات الأدوات في التطبيقات الإنتاجية؟"
> - "كيف يمكنني تسجيل وعرض تحديثات التقدم الوسيط في واجهة المستخدم؟"

<img src="../../../translated_images/ar/task-execution-pattern.9da3967750ab5c1e.webp" alt="نمط تنفيذ المهمة" width="800"/>

*التخطيط → التنفيذ → التلخيص لسير عمل المهام متعددة الخطوات*

**الكود العاكس لنفسه** - لتوليد كود بجودة الإنتاج. يولد النموذج الكود متبعًا معايير الإنتاج مع معالجة صحيحة للأخطاء. استخدم هذا عند بناء ميزات أو خدمات جديدة.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ar/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="دورة الانعكاس الذاتي" width="800"/>

*حلقة تحسين تكرارية - توليد، تقييم، تحديد المشكلات، تحسين، تكرار*

**التحليل الهيكلي** - للتقييم المتسق. يراجع النموذج الكود باستخدام إطار عمل ثابت (الصحة، الممارسات، الأداء، الأمان، القابلية للصيانة). استخدم هذا للمراجعات البرمجية أو تقييمات الجودة.

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

> **🤖 جرب مع دردشة [GitHub Copilot](https://github.com/features/copilot):** اسأل عن التحليل الهيكلي:
> - "كيف يمكنني تخصيص إطار التحليل لأنواع مختلفة من مراجعات الكود؟"
> - "ما هي أفضل طريقة لتحليل الإخراج الهيكلي والتصرف عليه برمجيًا؟"
> - "كيف أضمن اتساق مستويات الخطورة عبر جلسات المراجعة المختلفة؟"

<img src="../../../translated_images/ar/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="نمط التحليل الهيكلي" width="800"/>

*إطار عمل للمراجعات البرمجية المتسقة مع مستويات خطورة*

**الدردشة متعددة الأدوار** - للمحادثات التي تحتاج إلى سياق. يتذكر النموذج الرسائل السابقة ويبني على أساسها. استخدم هذا لجلسات المساعدة التفاعلية أو الأسئلة المعقدة والأجوبة.

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

*كيف يتراكم سياق المحادثة عبر أدوار متعددة حتى يصل إلى حد الرموز*

**التفكير خطوة بخطوة** - للمشاكل التي تحتاج منطقًا واضحًا. يعرض النموذج تفكيرًا صريحًا لكل خطوة. استخدم هذا لمسائل الرياضيات، الألغاز المنطقية، أو عندما تحتاج لفهم عملية التفكير.

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

*تفكيك المشاكل إلى خطوات منطقية صريحة*

**الإخراج المقيد** - للردود ذات متطلبات تنسيق محددة. يتبع النموذج قواعد التنسيق والطول بدقة. استخدم هذا للملخصات أو عندما تحتاج لهيكل إخراج دقيق.

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

*فرض متطلبات تنسيق، طول، وهيكل محددة*

## استخدام موارد Azure الموجودة

**تحقق من النشر:**

تأكد من وجود ملف `.env` في الدليل الجذري مع بيانات اعتماد Azure (تم إنشاؤه خلال الوحدة 01):
```bash
cat ../.env  # يجب أن يعرض AZURE_OPENAI_ENDPOINT و API_KEY و DEPLOYMENT
```

**ابدأ التطبيق:**

> **ملاحظة:** إذا كنت قد بدأت جميع التطبيقات باستخدام الأمر `./start-all.sh` من الوحدة 01، فهذه الوحدة تعمل بالفعل على المنفذ 8083. يمكنك تخطي أوامر البدء أدناه والانتقال مباشرة إلى http://localhost:8083.

**الخيار 1: استخدام لوحة تحكم Spring Boot (موصى به لمستخدمي VS Code)**
تتضمن حاوية التطوير امتداد لوحة تحكم Spring Boot، والذي يوفر واجهة بصرية لإدارة جميع تطبيقات Spring Boot. يمكنك العثور عليها في شريط الأنشطة على الجانب الأيسر من VS Code (ابحث عن أيقونة Spring Boot).

من لوحة تحكم Spring Boot، يمكنك:
- رؤية جميع تطبيقات Spring Boot المتاحة في مساحة العمل
- بدء/إيقاف التطبيقات بنقرة واحدة
- عرض سجلات التطبيق في الوقت الحقيقي
- مراقبة حالة التطبيق

ببساطة انقر على زر التشغيل بجانب "prompt-engineering" لبدء هذا الوحدة، أو ابدأ جميع الوحدات مرة واحدة.

<img src="../../../translated_images/ar/dashboard.da2c2130c904aaf0.webp" alt="لوحة تحكم Spring Boot" width="400"/>

**الخيار 2: استخدام سكريبتات الشل**

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

يقوم كلا السكريبتين تلقائيًا بتحميل متغيرات البيئة من ملف `.env` الرئيسي وسيبني ملفات JAR إذا لم تكن موجودة.

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
./stop.sh  # هذا الوحدة فقط
# أو
cd .. && ./stop-all.sh  # كل الوحدات
```

**PowerShell:**
```powershell
.\stop.ps1  # هذا الموديل فقط
# أو
cd ..; .\stop-all.ps1  # كل الموديلات
```

## لقطات شاشة التطبيق

<img src="../../../translated_images/ar/dashboard-home.5444dbda4bc1f79d.webp" alt="الصفحة الرئيسية للوحة التحكم" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*لوحة التحكم الرئيسية تعرض جميع أنماط هندسة المطالبات الثمانية مع خصائصها وحالات الاستخدام الخاصة بها*

## استكشاف الأنماط

تتيح لك الواجهة الويب تجربة استراتيجيات مطالبات مختلفة. كل نمط يحل مشكلات مختلفة - جربها لترى متى يبرز كل نهج.

> **ملاحظة: التدفق المباشر مقابل غير المباشر** — تقدم كل صفحة نمط زرين: **🔴 بث الاستجابة (مباشر)** وخيار **غير مباشر**. يستخدم التدفق المباشر Server-Sent Events (SSE) لعرض الرموز في الوقت الحقيقي أثناء توليدها بواسطة النموذج، لذا ترى التقدم فورًا. الخيار غير المباشر ينتظر الاستجابة كاملة قبل عرضها. للمطالبات التي تتطلب تفكيرًا عميقًا (مثل High Eagerness، Self-Reflecting Code)، قد تستغرق المكالمة غير المباشرة وقتًا طويلًا — أحيانًا دقائق — دون أي رد فعل مرئي. **استخدم التدفق المباشر عند تجربة المطالبات المعقدة** حتى تتمكن من رؤية عمل النموذج وتجنب الانطباع بأن الطلب انتهى مهلة استجابته.
>
> **ملاحظة: متطلبات المتصفح** — تستخدم ميزة التدفق المباشر Fetch Streams API (`response.body.getReader()`) والتي تتطلب متصفحًا كاملاً (Chrome, Edge, Firefox, Safari). لا تعمل في متصفح VS Code المدمج البسيط Simple Browser، حيث لا يدعم WebView الخاص به واجهة ReadableStream API. إذا كنت تستخدم Simple Browser، ستظل أزرار الوضع غير المباشر تعمل بشكل طبيعي — فقط أزرار التدفق المباشر تتأثر. افتح `http://localhost:8083` في متصفح خارجي لتجربة كاملة.

### القليل مقابل الكثير من الاجتهاد

اطرح سؤالًا بسيطًا مثل "ما هو 15% من 200؟" باستخدام Low Eagerness. ستحصل على إجابة فورية ومباشرة. الآن اطرح سؤالًا معقدًا مثل "صمم استراتيجية تخزين مؤقت لواجهة برمجة تطبيقات ذات حركة مرور عالية" باستخدام High Eagerness. انقر على **🔴 بث الاستجابة (مباشر)** وشاهد تفكير النموذج التفصيلي يظهر رمزًا تلو الآخر. نفس النموذج، نفس هيكل السؤال - لكن المطالبة تخبره بكمية التفكير المطلوبة.

### تنفيذ المهام (مقدمات الأدوات)

تستفيد تدفقات العمل متعددة الخطوات من التخطيط المسبق وسرد التقدم. يوضح النموذج ما سيفعله، يسرد كل خطوة، ثم يلخص النتائج.

### شفرة ذات تفكير ذاتي

جرب "إنشاء خدمة تحقق من البريد الإلكتروني". بدلاً من مجرد توليد الشفرة والتوقف، ينتج النموذج، يقيمها وفقًا لمعايير الجودة، يحدد نقاط الضعف، ويحسن. سترى أنه يكرر حتى تلبي الشفرة معايير الإنتاج.

### التحليل الهيكلي

تحتاج مراجعات الشفرة إلى أطر تقييم ثابتة. يحلل النموذج الشفرة باستخدام فئات محددة (الصحة، الممارسات، الأداء، الأمن) مع مستويات شدة.

### دردشة متعددة الأدوار

اسأل "ما هو Spring Boot؟" ثم تابع فورًا بـ "أرني مثالًا". يتذكر النموذج سؤالك الأول ويعطيك مثال Spring Boot تحديدًا. بدون ذاكرة، سيكون السؤال الثاني عامًا جدًا.

### التفكير خطوة بخطوة

اختر مسألة رياضية وجربها باستخدام كل من التفكير خطوة بخطوة وLow Eagerness. القليل من الاجتهاد يعطيك الإجابة فقط - سريع لكنه غامض. التفكير خطوة بخطوة يريك كل حساب وقرار.

### إخراج مقيد

عندما تحتاج إلى تنسيقات محددة أو عدد كلمات معين، يفرض هذا النمط الالتزام الصارم. جرب توليد ملخص بدقة 100 كلمة بتنسيق نقاط.

## ما تتعلمه حقًا

**جهد التفكير يغير كل شيء**

يسمح لك GPT-5.2 بالتحكم في الجهد الحاسوبي من خلال مطالباتك. الجهد القليل يعني استجابات سريعة مع استكشاف minimal. الجهد العالي يعني أن النموذج يأخذ وقته للتفكير بعمق. أنت تتعلم مطابقة الجهد مع تعقيد المهمة - لا تضيع الوقت في الأسئلة البسيطة، لكن لا تتعجل في القرارات المعقدة أيضًا.

**البنية توجه السلوك**

هل لاحظت علامات XML في المطالبات؟ ليست للزينة. تتبع النماذج التعليمات المنظمة بشكل أكثر موثوقية من النص الحر. عندما تحتاج عمليات متعددة الخطوات أو منطق معقد، تساعد البنية النموذج على تتبع مكانه وما يأتي بعد.

<img src="../../../translated_images/ar/prompt-structure.a77763d63f4e2f89.webp" alt="بنية المطالبة" width="800"/>

*تشريح مطالبة منظمة بشكل جيد مع أقسام واضحة وتنظيم بأسلوب XML*

**الجودة من خلال التقييم الذاتي**

تعمل أنماط التفكير الذاتي بجعل معايير الجودة صريحة. بدلاً من الأمل أن "يفعل النموذج ذلك بشكل صحيح"، تخبره بالضبط ما يعنيه "الصحيح": المنطق السليم، التعامل مع الأخطاء، الأداء، الأمان. يمكن للنموذج بعدها تقييم مخرجاته وتحسينها. هذا يحول توليد الشيفرة من لعبة حظ إلى عملية.

**السياق محدود**

تعمل المحادثات متعددة الأدوار بإدراج تاريخ الرسائل مع كل طلب. لكن هناك حدًا - لكل نموذج عدد رموز أقصى. مع نمو المحادثات، ستحتاج لاستراتيجيات للحفاظ على السياق ذي الصلة دون تجاوز السقف. تُظهر لك هذه الوحدة كيف يعمل الذاكرة؛ ستتعلم لاحقًا متى تلخص، متى تنسى، ومتى تسترجع.

## الخطوات التالية

**الوحدة التالية:** [03-rag - RAG (التوليد المعزز بالاسترجاع)](../03-rag/README.md)

---

**التنقل:** [← السابق: الوحدة 01 - المقدمة](../01-introduction/README.md) | [العودة إلى الرئيسية](../README.md) | [التالي: الوحدة 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**إخلاء مسؤولية**:  
تمت ترجمة هذا المستند باستخدام خدمة الترجمة الآلية [Co-op Translator](https://github.com/Azure/co-op-translator). على الرغم من أننا نسعى للدقة، يرجى العلم بأن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار المستند الأصلي بلغته الأصلية هو المصدر المعتمد والموثوق. بالنسبة للمعلومات الحيوية، يُنصح بالترجمة البشرية المهنية. نحن غير مسؤولين عن أي سوء فهم أو تفسير خاطئ ينشأ عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->