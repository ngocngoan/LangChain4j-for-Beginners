# الوحدة 02: هندسة المطالبات مع GPT-5.2

## جدول المحتويات

- [ما الذي ستتعلمه](../../../02-prompt-engineering)
- [المتطلبات الأساسية](../../../02-prompt-engineering)
- [فهم هندسة المطالبات](../../../02-prompt-engineering)
- [أساسيات هندسة المطالبات](../../../02-prompt-engineering)
  - [المطالبة بدون أمثلة](../../../02-prompt-engineering)
  - [المطالبة بعدد قليل من الأمثلة](../../../02-prompt-engineering)
  - [سلسلة التفكير](../../../02-prompt-engineering)
  - [المطالبة المعتمدة على الدور](../../../02-prompt-engineering)
  - [نماذج المطالبات](../../../02-prompt-engineering)
- [الأنماط المتقدمة](../../../02-prompt-engineering)
- [استخدام موارد Azure الموجودة](../../../02-prompt-engineering)
- [لقطات تطبيقية](../../../02-prompt-engineering)
- [استكشاف الأنماط](../../../02-prompt-engineering)
  - [الحماس المنخفض مقابل الحماس العالي](../../../02-prompt-engineering)
  - [تنفيذ المهام (مقدمات الأدوات)](../../../02-prompt-engineering)
  - [رمز الانعكاس الذاتي](../../../02-prompt-engineering)
  - [التحليل المنظم](../../../02-prompt-engineering)
  - [الدردشة ذات الدور متعدد](../../../02-prompt-engineering)
  - [التفكير خطوة بخطوة](../../../02-prompt-engineering)
  - [الإخراج المقيد](../../../02-prompt-engineering)
- [ما الذي تتعلمه فعليًا](../../../02-prompt-engineering)
- [الخطوات التالية](../../../02-prompt-engineering)

## ما الذي ستتعلمه

<img src="../../../translated_images/ar/what-youll-learn.c68269ac048503b2.webp" alt="ما الذي ستتعلمه" width="800"/>

في الوحدة السابقة، رأيت كيف يتيح الذاكرة الذكاء الاصطناعي الحواري واستخدمت نماذج GitHub للتفاعلات الأساسية. الآن سنركز على كيفية طرح الأسئلة — أي المطالبات نفسها — باستخدام GPT-5.2 من Azure OpenAI. الطريقة التي تبني بها مطالباتك تؤثر بشكل كبير على جودة الردود التي تحصل عليها. نبدأ بمراجعة التقنيات الأساسية للمطالبات، ثم ننتقل إلى ثمانية أنماط متقدمة تستفيد بالكامل من قدرات GPT-5.2.

سنستخدم GPT-5.2 لأنه يقدم تحكمًا في التفكير — يمكنك إخبار النموذج بكمية التفكير التي يجب أن يقوم بها قبل الإجابة. هذا يجعل استراتيجيات المطالبات المختلفة أكثر وضوحًا ويساعدك على فهم متى تستخدم كل نهج. سنستفيد أيضًا من قيود المعدل الأقل على GPT-5.2 عبر Azure مقارنة بنماذج GitHub.

## المتطلبات الأساسية

- إتمام الوحدة 01 (تم نشر موارد Azure OpenAI)
- ملف `.env` في الدليل الجذري يحتوي على بيانات اعتماد Azure (تم إنشاؤه بواسطة `azd up` في الوحدة 01)

> **ملاحظة:** إذا لم تكمل الوحدة 01، فاتبع تعليمات النشر هناك أولًا.

## فهم هندسة المطالبات

<img src="../../../translated_images/ar/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="ما هي هندسة المطالبات؟" width="800"/>

هندسة المطالبات تتعلق بتصميم نص الإدخال الذي تحصل منه بشكل مستمر على النتائج التي تحتاجها. الأمر لا يقتصر فقط على طرح أسئلة — بل يتعلق بهيكلة الطلبات بحيث يفهم النموذج بالضبط ما تريد وكيف تقدمه.

فكر في الأمر مثل إعطاء تعليمات لزميل عمل. "صلح الخطأ" أمر غامض. "صلح استثناء المؤشر الفارغ في UserService.java السطر 45 بإضافة فحص فارغ" هو محدد جدًا. نماذج اللغة تعمل بنفس الطريقة — التحديد والبنية مهمان.

<img src="../../../translated_images/ar/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="كيف تناسب LangChain4j" width="800"/>

يوفر LangChain4j البنية التحتية — اتصالات النماذج، الذاكرة، وأنواع الرسائل — في حين أن أنماط المطالبات هي فقط نصوص منظمة بعناية ترسل عبر تلك البنية التحتية. اللبنات الأساسية هي `SystemMessage` (الذي يحدد سلوك ودور الذكاء الاصطناعي) و `UserMessage` (الذي يحمل طلبك الفعلي).

## أساسيات هندسة المطالبات

<img src="../../../translated_images/ar/five-patterns-overview.160f35045ffd2a94.webp" alt="نظرة عامة على خمسة أنماط لهندسة المطالبات" width="800"/>

قبل الخوض في الأنماط المتقدمة في هذه الوحدة، دعنا نراجع خمس تقنيات طالبات أساسية. هذه هي اللبنات الأساسية التي يجب أن يعرفها كل مهندس مطالبات. إذا كنت قد عملت بالفعل في [وحدة البداية السريعة](../00-quick-start/README.md#2-prompt-patterns)، فقد شاهدت هذه التقنيات في التطبيق — إليك الإطار المفاهيمي وراءها.

### المطالبة بدون أمثلة

النهج الأبسط: أعط النموذج تعليمات مباشرة بدون أمثلة. يعتمد النموذج تمامًا على تدريبه لفهم وتنفيذ المهمة. هذا يعمل جيدًا للطلبات البسيطة حيث يكون السلوك المتوقع واضحًا.

<img src="../../../translated_images/ar/zero-shot-prompting.7abc24228be84e6c.webp" alt="المطالبة بدون أمثلة" width="800"/>

*تعليمات مباشرة بدون أمثلة — النموذج يستنتج المهمة من التعليمات فقط*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// الرد: "إيجابي"
```

**متى تستخدمه:** التصنيفات البسيطة، الأسئلة المباشرة، الترجمات، أو أي مهمة يمكن للنموذج التعامل معها بدون توجيه إضافي.

### المطالبة بعدد قليل من الأمثلة

قدّم أمثلة توضح النمط الذي تريد من النموذج اتباعه. يتعلم النموذج تنسيق الإدخال-الإخراج المتوقع من أمثلتك ويطبقه على المدخلات الجديدة. هذا يحسن بشكل كبير الاتساق في المهام التي لا يكون فيها التنسيق أو السلوك المطلوب واضحًا.

<img src="../../../translated_images/ar/few-shot-prompting.9d9eace1da88989a.webp" alt="المطالبة بعدد قليل من الأمثلة" width="800"/>

*التعلم من الأمثلة — النموذج يتعرف على النمط ويطبقه على المدخلات الجديدة*

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

**متى تستخدمه:** التصنيفات المخصصة، التنسيق المتسق، المهام الخاصة بالمجال، أو عندما تكون نتائج المطالبة بدون أمثلة غير متسقة.

### سلسلة التفكير

اطلب من النموذج عرض تفكيره خطوة بخطوة. بدلاً من القفز مباشرة إلى إجابة، يحلل النموذج المشكلة ويعمل على كل جزء بشكل صريح. هذا يحسن الدقة في مسائل الرياضيات والمنطق وأعمال التفكير متعددة الخطوات.

<img src="../../../translated_images/ar/chain-of-thought.5cff6630e2657e2a.webp" alt="سلسلة التفكير" width="800"/>

*التفكير خطوة بخطوة — تفكيك المشكلات المعقدة إلى خطوات منطقية صريحة*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// يظهر النموذج: ١٥ - ٨ = ٧، ثم ٧ + ١٢ = ١٩ تفاحة
```

**متى تستخدمه:** مسائل الرياضيات، الألغاز المنطقية، تصحيح الأخطاء، أو أي مهمة حيث يُحسن عرض عملية التفكير الدقة والثقة.

### المطالبة المعتمدة على الدور

حدد شخصية أو دور للذكاء الاصطناعي قبل طرح سؤالك. هذا يوفر سياقًا يشكل نغمة، وعمق، وتركيز الرد. "مهندس برمجيات" يعطي نصيحة مختلفة عن "مطور مبتدئ" أو "مدقق أمني".

<img src="../../../translated_images/ar/role-based-prompting.a806e1a73de6e3a4.webp" alt="المطالبة المعتمدة على الدور" width="800"/>

*تحديد السياق والشخصية — نفس السؤال يحصل على رد مختلف حسب الدور المعين*

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

**متى تستخدمه:** مراجعات الكود، التدريس، التحليل الخاص بالمجال، أو عندما تحتاج ردود مخصصة لمستوى خبرة أو منظور معين.

### نماذج المطالبات

أنشئ مطالبات قابلة لإعادة الاستخدام مع متغيرات مكانية. بدلاً من كتابة مطالبة جديدة في كل مرة، عرّف قالبًا مرة واملأ بقيم مختلفة. تجعل فئة `PromptTemplate` في LangChain4j هذا سهلاً باستخدام صيغة `{{variable}}`.

<img src="../../../translated_images/ar/prompt-templates.14bfc37d45f1a933.webp" alt="نماذج المطالبات" width="800"/>

*مطالبات قابلة لإعادة الاستخدام مع متغيرات مكانية — قالب واحد، استخدامات متعددة*

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

**متى تستخدمه:** استعلامات متكررة بمدخلات مختلفة، معالجة دفعات، بناء سير عمل ذكاء اصطناعي قابل لإعادة الاستخدام، أو أي سيناريو حيث تبقى بنية المطالبة نفسها ولكن البيانات تتغير.

---

هذه الأساسيات الخمسة تعطيك مجموعة أدوات قوية لمعظم مهام المطالبات. بقية هذه الوحدة تبني عليها مع **ثمانية أنماط متقدمة** تستفيد من التحكم بالتفكير، والتقييم الذاتي، وقدرات الإخراج المنظم في GPT-5.2.

## الأنماط المتقدمة

مع تغطية الأساسيات، دعونا ننتقل إلى الأنماط المتقدمة الثمانية التي تجعل هذه الوحدة فريدة. ليست كل المشاكل بحاجة إلى نفس النهج. بعض الأسئلة تحتاج إجابات سريعة، وأخرى تحتاج تفكيرًا عميقًا. بعضها يحتاج تفكيرًا مرئيًا، والبعض الآخر يحتاج فقط النتائج. كل نمط أدناه مُحسّن لسيناريو مختلف — وتحكم التفكير في GPT-5.2 يجعل الفروقات أكثر وضوحًا.

<img src="../../../translated_images/ar/eight-patterns.fa1ebfdf16f71e9a.webp" alt="ثمانية أنماط للمطالبات" width="800"/>

*نظرة عامة على الأنماط الثمانية لهندسة المطالبات وحالات استخدامها*

<img src="../../../translated_images/ar/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="التحكم بالتفكير مع GPT-5.2" width="800"/>

*تحكم GPT-5.2 بالتفكير يتيح لك تحديد مقدار التفكير الذي يجب أن يقوم به النموذج — من إجابات سريعة ومباشرة إلى استكشاف عميق*

<img src="../../../translated_images/ar/reasoning-effort.db4a3ba5b8e392c1.webp" alt="مقارنة جهد التفكير" width="800"/>

*الحماس المنخفض (سريع، مباشر) مقابل الحماس العالي (شامل، استكشافي) في أساليب التفكير*

**الحماس المنخفض (سريع ومركز)** - للأسئلة البسيطة حيث تريد إجابات سريعة ومباشرة. يقوم النموذج بتفكير بسيط - بحد أقصى خطوتين. استخدم هذا للحسابات، عمليات البحث، أو الأسئلة البسيطة.

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
> - "ما الفرق بين نمطي المطالبة منخفض الحماس وعالي الحماس؟"
> - "كيف تساعد علامات XML في المطالبات على هيكلة رد الذكاء الاصطناعي؟"
> - "متى يجب أن أستخدم أنماط الانعكاس الذاتي مقابل التعليمات المباشرة؟"

**الحماس العالي (عميق وشامل)** - للمشاكل المعقدة حيث تريد تحليلاً شاملاً. يستكشف النموذج بعمق ويُظهر تفكيرًا مفصلاً. استخدم هذا لتصميم الأنظمة، قرارات الهندسة، أو بحوث معقدة.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**تنفيذ المهام (تقدم خطوة بخطوة)** - لسير عمل متعدد الخطوات. يقدم النموذج خطة مبدئية، يروي كل خطوة أثناء العمل، ثم يقدّم ملخصًا. استخدم هذا للهجرات، تنفيذ المشاريع، أو أي عملية متعددة الخطوات.

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

المطالبة بـ Chain-of-Thought تطلب بشكل صريح من النموذج عرض عملية تفكيره، مما يحسن الدقة للمهام المعقدة. التفكيك خطوة بخطوة يساعد البشر والذكاء الاصطناعي على فهم المنطق.

> **🤖 جرب عبر [GitHub Copilot](https://github.com/features/copilot) Chat:** اسأل عن هذا النمط:
> - "كيف سأكيف نمط تنفيذ المهمة للعمليات طويلة الأمد؟"
> - "ما أفضل الممارسات لهياكلة مقدمات الأدوات في التطبيقات الإنتاجية؟"
> - "كيف يمكنني التقاط وعرض تحديثات التقدم الوسيط في واجهة المستخدم؟"

<img src="../../../translated_images/ar/task-execution-pattern.9da3967750ab5c1e.webp" alt="نم نمط تنفيذ المهمة" width="800"/>

*خطة → تنفيذ → تلخيص لسير العمل للمهام متعددة الخطوات*

**رمز الانعكاس الذاتي** - لتوليد كود بجودة إنتاجية. يولد النموذج الكود متبعًا معايير الإنتاج مع معالجة أخطاء صحيحة. استخدم هذا لبناء ميزات أو خدمات جديدة.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ar/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="دورة الانعكاس الذاتي" width="800"/>

*حلقة تحسين متكررة - توليد، تقييم، تحديد المشكلات، تحسين، تكرار*

**التحليل المنظم** - للتقييم المتسق. يراجع النموذج الكود باستخدام إطار عمل ثابت (الصحة، الممارسات، الأداء، الأمان، القابلية للصيانة). استخدم هذا لمراجعات الكود أو تقييمات الجودة.

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

> **🤖 جرب عبر [GitHub Copilot](https://github.com/features/copilot) Chat:** اسأل عن التحليل المنظم:
> - "كيف يمكنني تخصيص إطار التحليل لأنواع مختلفة من مراجعات الكود؟"
> - "ما هي أفضل طريقة لتحليل والعمل على نتائج الإخراج المنظم برمجيًا؟"
> - "كيف أضمن مستويات شدة متسقة عبر جلسات المراجعة المختلفة؟"

<img src="../../../translated_images/ar/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="نم نمط التحليل المنظم" width="800"/>

*إطار عمل لمراجعات كود متسقة مع مستويات شدة*

**الدردشة ذات الدور متعدد** - للمحادثات التي تحتاج إلى سياق. يتذكر النموذج الرسائل السابقة ويبني عليها. استخدم هذا لجلسات المساعدة التفاعلية أو الأسئلة والأجوبة المعقدة.

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

*كيف يتجمع سياق المحادثة خلال عدة أدوار حتى يصل حد الرموز*

**التفكير خطوة بخطوة** - للمشاكل التي تحتاج إلى منطق مرئي. يعرض النموذج تفكيرًا صريحًا لكل خطوة. استخدم هذا لمشاكل الرياضيات، الألغاز المنطقية، أو عندما تحتاج إلى فهم عملية التفكير.

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

**الإخراج المقيد** - للردود التي تتطلب قواعد تنسيق محددة. يلتزم النموذج بدقة بقواعد التنسيق والطول. استخدم هذا للملخصات أو عندما تحتاج بنية إخراج دقيقة.

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

<img src="../../../translated_images/ar/constrained-output-pattern.0ce39a682a6795c2.webp" alt="نم نمط الإخراج المقيد" width="800"/>

*فرض قواعد محددة للتنسيق والطول والبنية*

## استخدام موارد Azure الموجودة

**تحقق من النشر:**

تأكد من وجود ملف `.env` في الدليل الجذري يحتوي على بيانات اعتماد Azure (تم إنشاؤه خلال الوحدة 01):
```bash
cat ../.env  # يجب أن يعرض AZURE_OPENAI_ENDPOINT، API_KEY، DEPLOYMENT
```

**ابدأ التطبيق:**

> **ملاحظة:** إذا كنت قد بدأت كل التطبيقات بالفعل باستخدام `./start-all.sh` من الوحدة 01، فإن هذه الوحدة تعمل بالفعل على المنفذ 8083. يمكنك تخطي أوامر البدء أدناه والانتقال مباشرة إلى http://localhost:8083.

**الخيار 1: استخدام لوحة تحكم Spring Boot (مفضل لمستخدمي VS Code)**

حاوية التطوير تتضمن امتداد لوحة تحكم Spring Boot، الذي يوفر واجهة مرئية لإدارة جميع تطبيقات Spring Boot. يمكنك العثور عليه في شريط الأنشطة على الجانب الأيسر من VS Code (ابحث عن أيقونة Spring Boot).
من لوحة تحكم Spring Boot، يمكنك:
- رؤية جميع تطبيقات Spring Boot المتاحة في مساحة العمل
- بدء/إيقاف التطبيقات بنقرة واحدة
- عرض سجلات التطبيق في الوقت الحقيقي
- مراقبة حالة التطبيق

فقط انقر على زر التشغيل بجوار "prompt-engineering" لبدء هذه الوحدة، أو ابدأ جميع الوحدات مرة واحدة.

<img src="../../../translated_images/ar/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**الخيار 2: استخدام سكريبتات الشل**

بدء جميع تطبيقات الويب (الوحدات 01-04):

**Bash:**
```bash
cd ..  # من الدليل الجذر
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # من دليل الجذر
.\start-all.ps1
```

أو بدء هذه الوحدة فقط:

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

كلا السكريبتين يحملان متغيرات البيئة تلقائيًا من ملف `.env` الأساسي وسيقومان ببناء ملفات JAR إذا لم تكن موجودة.

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
.\stop.ps1  # هذا الموديول فقط
# أو
cd ..; .\stop-all.ps1  # جميع الموديولات
```

## لقطات شاشة للتطبيق

<img src="../../../translated_images/ar/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*لوحة التحكم الرئيسية تعرض جميع أنماط هندسة المحفزات الثمانية مع خصائصها وحالات الاستخدام*

## استكشاف الأنماط

تتيح لك الواجهة الإلكترونية تجربة استراتيجيات تحفيزية مختلفة. يحل كل نمط مشاكل مختلفة - جربها لترى متى يبرز كل نهج.

### الحماس المنخفض مقابل الحماس العالي

اسأل سؤالًا بسيطًا مثل "ما هو 15٪ من 200؟" باستخدام الحماس المنخفض. ستحصل على إجابة فورية ومباشرة. الآن اسأل شيئًا معقدًا مثل "صمم استراتيجية تخزين مؤقت لواجهة برمجة تطبيقات عالية الحركة" باستخدام الحماس العالي. شاهد كيف يتباطأ النموذج ويقدم تعليلًا مفصلاً. نفس النموذج، نفس هيكل السؤال - لكن المحفز يخبره بكمية التفكير المطلوبة.

<img src="../../../translated_images/ar/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*حساب سريع مع تعليل بسيط*

<img src="../../../translated_images/ar/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*استراتيجية تخزين مؤقت شاملة (2.8MB)*

### تنفيذ المهام (مقدمات الأدوات)

تستفيد سير العمل متعددة الخطوات من التخطيط المسبق وسرد التقدم. يحدد النموذج ما سيفعله، يروي كل خطوة، ثم يلخص النتائج.

<img src="../../../translated_images/ar/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*إنشاء نقطة نهاية REST مع سرد خطوة بخطوة (3.9MB)*

### الشفرة ذات التفكير الذاتي

جرّب "إنشاء خدمة تحقق من صحة البريد الإلكتروني". بدلًا من فقط توليد الشفرة والتوقف، يولد النموذج، يقيم وفقًا لمعايير الجودة، يحدد نقاط الضعف، ويحسن. سترى تكرارًا حتى تلبي الشفرة المعايير الإنتاجية.

<img src="../../../translated_images/ar/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*خدمة تحقق من البريد الإلكتروني كاملة (5.2MB)*

### التحليل المنظم

تحتاج مراجعات الشفرة إلى أُطر تقييم ثابتة. يحلل النموذج الشفرة باستخدام فئات محددة (الصحة، الممارسات، الأداء، الأمان) مع مستويات شدة.

<img src="../../../translated_images/ar/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*مراجعة الشفرة بناءً على إطار العمل*

### المحادثة متعددة الأدوار

اسأل "ما هو Spring Boot؟" ثم تابع فورًا بـ "أرني مثالًا". يتذكر النموذج سؤالك الأول ويعطيك مثالًا محددًا عن Spring Boot. بدون الذاكرة، سيكون السؤال الثاني غامضًا جدًا.

<img src="../../../translated_images/ar/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*الحفاظ على السياق عبر الأسئلة*

### التعليل خطوة بخطوة

اختر مسألة رياضية وجربها مع كل من التعليل خطوة بخطوة والحماس المنخفض. الحماس المنخفض يعطيك فقط الإجابة - سريع لكنه غير واضح. يظهر التعليل خطوة بخطوة كل حساب وقرار.

<img src="../../../translated_images/ar/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*مسألة رياضية مع خطوات واضحة*

### الإخراج المقيد

عندما تحتاج إلى تنسيقات محددة أو عدد كلمات معين، يفرض هذا النمط الالتزام الصارم. جرب توليد ملخص يحتوي بالضبط على 100 كلمة بتنسيق نقاط.

<img src="../../../translated_images/ar/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*ملخص تعلم آلي مع تحكم في التنسيق*

## ما تتعلمه حقًا

**جهد التعليل يغير كل شيء**

يسمح لك GPT-5.2 بالتحكم في الجهد الحسابي من خلال محفزاتك. الجهد المنخفض يعني استجابات سريعة مع استكشاف محدود. الجهد العالي يعني أن النموذج يأخذ وقتًا للتفكير بعمق. تتعلم مطابقة الجهد مع تعقيد المهمة - لا تضيع وقتًا على أسئلة بسيطة، ولا تتسرع في قرارات معقدة.

**البنية توجه السلوك**

هل لاحظت علامات XML في المحفزات؟ ليست للزينة. تتبع النماذج التعليمات المنظمة بشكل أكثر موثوقية من النص الحر. عندما تحتاج إلى عمليات متعددة الخطوات أو منطق معقد، تساعد البنية النموذج على تتبع مكان وجوده وما هو التالي.

<img src="../../../translated_images/ar/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*تشريح محفز منظّم جيدًا بأقسام واضحة وتنظيم بأسلوب XML*

**الجودة من خلال التقييم الذاتي**

تعمل الأنماط ذات التفكير الذاتي بجعل معايير الجودة واضحة. بدلًا من التمني أن "يفعلها النموذج بشكل صحيح"، تخبره بالضبط ما يعنيه "الصحيح": منطق صحيح، التعامل مع الأخطاء، الأداء، الأمان. يمكن للنموذج تقييم مخرجاته وتحسينها. هذا يحول توليد الشفرة من لعبة حظ إلى عملية منهجية.

**السياق محدود**

تعمل المحادثات متعددة الأدوار عن طريق تضمين تاريخ الرسائل مع كل طلب. لكن هناك حدًا - لكل نموذج حد أقصى من الرموز. مع نمو المحادثات، ستحتاج إلى استراتيجيات للحفاظ على السياق المهم دون الوصول إلى هذا الحد. تعرض هذه الوحدة كيف تعمل الذاكرة؛ وفي وقت لاحق ستتعلم متى تلخص، متى تنسى، ومتى تسترجع.

## الخطوات التالية

**الوحدة التالية:** [03-rag - RAG (التوليد المعزز بالاستخراج)](../03-rag/README.md)

---

**التنقل:** [← السابق: الوحدة 01 - المقدمة](../01-introduction/README.md) | [العودة للرئيسية](../README.md) | [التالي: الوحدة 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**تنويه**:
تمت ترجمة هذا المستند باستخدام خدمة الترجمة بالذكاء الاصطناعي [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى جاهدين لتحقيق الدقة، يرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار الوثيقة الأصلية بلغتها الأصلية المصدر الموثوق به. للمعلومات الحساسة، يُنصح باستخدام الترجمة البشرية الاحترافية. نحن غير مسؤولين عن أي سوء فهم أو تفسيرات خاطئة ناتجة عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->