# الوحدة 02: هندسة المطالبات مع GPT-5.2

## جدول المحتويات

- [مراجعة الفيديو](../../../02-prompt-engineering)
- [ما الذي ستتعلمه](../../../02-prompt-engineering)
- [المتطلبات المسبقة](../../../02-prompt-engineering)
- [فهم هندسة المطالبات](../../../02-prompt-engineering)
- [أساسيات هندسة المطالبات](../../../02-prompt-engineering)
  - [المطالبة بدون أمثلة](../../../02-prompt-engineering)
  - [المطالبة مع أمثلة قليلة](../../../02-prompt-engineering)
  - [سلسلة التفكير](../../../02-prompt-engineering)
  - [المطالبة بناءً على الدور](../../../02-prompt-engineering)
  - [نماذج المطالبات](../../../02-prompt-engineering)
- [أنماط متقدمة](../../../02-prompt-engineering)
- [استخدام موارد Azure الحالية](../../../02-prompt-engineering)
- [لقطات شاشة التطبيق](../../../02-prompt-engineering)
- [استكشاف الأنماط](../../../02-prompt-engineering)
  - [الحماس المنخفض مقابل الحماس العالي](../../../02-prompt-engineering)
  - [تنفيذ المهام (المقدمات للأدوات)](../../../02-prompt-engineering)
  - [الكود المعتمد على الذات](../../../02-prompt-engineering)
  - [التحليل المنظم](../../../02-prompt-engineering)
  - [الدردشة متعددة الأدوار](../../../02-prompt-engineering)
  - [التفكير خطوة بخطوة](../../../02-prompt-engineering)
  - [الإخراج المقيد](../../../02-prompt-engineering)
- [ما الذي تتعلمه فعلاً](../../../02-prompt-engineering)
- [الخطوات التالية](../../../02-prompt-engineering)

## مراجعة الفيديو

شاهد هذه الجلسة الحية التي تشرح كيفية البدء مع هذه الوحدة:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="هندسة المطالبات مع LangChain4j - جلسة مباشرة" width="800"/></a>

## ما الذي ستتعلمه

<img src="../../../translated_images/ar/what-youll-learn.c68269ac048503b2.webp" alt="ما الذي ستتعلمه" width="800"/>

في الوحدة السابقة، رأيت كيف تمكن الذاكرة الذكاء الاصطناعي الحواري واستخدمت نماذج GitHub للتفاعلات الأساسية. الآن سنركز على كيفية طرح الأسئلة - أي المطالبات نفسها - باستخدام GPT-5.2 من Azure OpenAI. الطريقة التي تبني بها مطالباتك تؤثر بشكل كبير على جودة الردود التي تحصل عليها. نبدأ بمراجعة تقنيات المطالبات الأساسية، ثم ننتقل إلى ثمانية أنماط متقدمة تستغل كامل قدرات GPT-5.2.

سنستخدم GPT-5.2 لأنه يقدم قدرة التحكم في التفكير - يمكنك إخبار النموذج بكمية التفكير التي يجب أن يقوم بها قبل الإجابة. هذا يجعل استراتيجيات المطالبات المختلفة أكثر وضوحًا ويساعدك على فهم متى تستخدم كل نهج. سنستفيد أيضًا من قيود معدل أقل على GPT-5.2 من Azure مقارنة بنماذج GitHub.

## المتطلبات المسبقة

- إكمال الوحدة 01 (توفير موارد Azure OpenAI)
- وجود ملف `.env` في الدليل الجذري مع بيانات اعتماد Azure (تم إنشاؤه بواسطة الأمر `azd up` في الوحدة 01)

> **ملاحظة:** إذا لم تكمل الوحدة 01، فاتبع تعليمات النشر هناك أولاً.

## فهم هندسة المطالبات

<img src="../../../translated_images/ar/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="ما هي هندسة المطالبات؟" width="800"/>

هندسة المطالبات تتعلق بتصميم نص الإدخال الذي يضمن حصولك على النتائج التي تحتاجها بشكل مستمر. ليس فقط طرح الأسئلة - بل هي هيكلة الطلبات بحيث يفهم النموذج بالضبط ما تريد وكيفية تقديمه.

فكر فيها مثل إعطاء تعليمات لزميل في العمل. "أصلح الخطأ" هو غامض. "أصلح استثناء المؤشر null في UserService.java السطر 45 عن طريق إضافة فحص null" هو محدد. نماذج اللغة تعمل بنفس الطريقة - الدقة والتركيب مهمان.

<img src="../../../translated_images/ar/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="كيف يتناسب LangChain4j" width="800"/>

يوفر LangChain4j البنية التحتية — الاتصالات بالنماذج، والذاكرة، وأنواع الرسائل — بينما أنماط المطالبات هي مجرد نصوص منظمة بعناية ترسل عبر تلك البنية. اللبنات الأساسية هي `SystemMessage` (تحدد سلوك ودور الذكاء الاصطناعي) و `UserMessage` (تحمل طلبك الفعلي).

## أساسيات هندسة المطالبات

<img src="../../../translated_images/ar/five-patterns-overview.160f35045ffd2a94.webp" alt="نظرة عامة على خمسة أنماط لهندسة المطالبات" width="800"/>

قبل الغوص في الأنماط المتقدمة في هذه الوحدة، لنراجع خمس تقنيات أساسية للمطالبات. هذه هي اللبنات الأساسية التي يجب أن يعرفها كل مهندس مطالبات. إذا كنت قد أنجزت بالفعل [وحدة البدء السريع](../00-quick-start/README.md#2-prompt-patterns)، فقد شاهدت هذه التقنيات عمليًا — إليك الإطار المفاهيمي وراءها.

### المطالبة بدون أمثلة

النهج الأبسط: إعطاء النموذج تعليمات مباشرة بدون أمثلة. يعتمد النموذج كليًا على تدريبه لفهم وتنفيذ المهمة. هذا يعمل جيدًا للطلبات المباشرة حيث السلوك المتوقع واضح.

<img src="../../../translated_images/ar/zero-shot-prompting.7abc24228be84e6c.webp" alt="المطالبة بدون أمثلة" width="800"/>

*تعليمات مباشرة بدون أمثلة — يستنتج النموذج المهمة من التعليمات فقط*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// الرد: "إيجابي"
```

**متى تستخدم:** التصنيفات البسيطة، الأسئلة المباشرة، الترجمات، أو أي مهمة يمكن للنموذج التعامل معها بدون إرشادات إضافية.

### المطالبة مع أمثلة قليلة

قدم أمثلة توضح النمط الذي تريد أن يتبعه النموذج. يتعلم النموذج شكل الإدخال-الإخراج المتوقع من أمثلتك ويطبقه على مدخلات جديدة. هذا يحسن التناسق بشكل كبير للمهام التي يكون فيها الشكل أو السلوك المطلوب غير واضح.

<img src="../../../translated_images/ar/few-shot-prompting.9d9eace1da88989a.webp" alt="المطالبة مع أمثلة قليلة" width="800"/>

*التعلم من الأمثلة — يحدد النموذج النمط ويطبقه على مدخلات جديدة*

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

**متى تستخدم:** التصنيفات المخصصة، التنسيق المتناسق، المهام الخاصة بالمجال، أو عندما تكون نتائج المطالبة بدون أمثلة غير متسقة.

### سلسلة التفكير

اطلب من النموذج أن يعرض تفكيره خطوة بخطوة. بدلاً من القفز مباشرة إلى الإجابة، يقوم النموذج بتفصيل المشكلة والعمل على كل جزء بشكل صريح. هذا يحسن الدقة في مسائل الرياضيات، المنطق، والمهام التي تحتاج إلى تفكير متعدد الخطوات.

<img src="../../../translated_images/ar/chain-of-thought.5cff6630e2657e2a.webp" alt="مطالبة سلسلة التفكير" width="800"/>

*تفكير خطوة بخطوة — تقسيم المشاكل المعقدة إلى خطوات منطقية صريحة*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// يوضح النموذج: ١٥ - ٨ = ٧، ثم ٧ + ١٢ = ١٩ تفاحة
```

**متى تستخدم:** مسائل الرياضيات، الألغاز المنطقية، التصحيح، أو أي مهمة يظهر فيها عرض طريقة التفكير تحسن الدقة والثقة.

### المطالبة بناءً على الدور

حدد شخصية أو دور للذكاء الاصطناعي قبل طرح السؤال. هذا يوفر سياقًا يشكل نغمة، عمق وتركيز الرد. "مهندس برمجيات" يعطي نصائح مختلفة عن "مطور مبتدئ" أو "مراجع أمني".

<img src="../../../translated_images/ar/role-based-prompting.a806e1a73de6e3a4.webp" alt="المطالبة بناءً على الدور" width="800"/>

*تحديد السياق والشخصية — نفس السؤال يعطي رد مختلف حسب الدور المعين*

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

**متى تستخدم:** مراجعات الكود، التدريس، التحليل الخاص بالمجال، أو عندما تحتاج ردودًا مصممة لمستوى خبرة أو منظور معين.

### نماذج المطالبات

أنشئ مطالبات قابلة لإعادة الاستخدام مع أماكن متغيرة. بدلاً من كتابة مطالبة جديدة في كل مرة، عرّف قالبًا مرة واحدة واملأه بقيم مختلفة. فئة `PromptTemplate` في LangChain4j تجعل هذا سهلاً باستخدام صيغة `{{variable}}`.

<img src="../../../translated_images/ar/prompt-templates.14bfc37d45f1a933.webp" alt="نماذج المطالبات" width="800"/>

*مطالبات قابلة لإعادة الاستخدام مع أماكن متغيرة — قالب واحد، استخدامات متعددة*

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

**متى تستخدم:** الاستفسارات المتكررة مع مدخلات مختلفة، المعالجة الدُفعية، بناء تدفقات عمل ذكاء اصطناعي قابلة لإعادة الاستخدام، أو أي سيناريو يبقى فيه هيكل المطالبة نفسه لكن تتغير البيانات.

---

هذه المبادئ الخمسة تمنحك مجموعة أدوات قوية لمعظم مهام المطالبات. باقي هذه الوحدة يبني عليها مع **ثماني أنماط متقدمة** تستفيد من قدرات التحكم في التفكير، التقييم الذاتي، والإخراج المنظم في GPT-5.2.

## أنماط متقدمة

بعد تغطية الأساسيات، لننتقل إلى الأنماط المتقدمة الثمانية التي تجعل هذه الوحدة فريدة. ليست كل المشاكل تحتاج لنفس النهج. بعض الأسئلة تحتاج إلى إجابات سريعة، وأخرى تحتاج إلى تفكير عميق. بعض تحتاج لتفكير مرئي، وأخرى تحتاج فقط نتائج. كل نمط أدناه مثالي لسيناريو مختلف — وقدرة التحكم في التفكير في GPT-5.2 تجعل الفروقات أكثر وضوحًا.

<img src="../../../translated_images/ar/eight-patterns.fa1ebfdf16f71e9a.webp" alt="ثمانية أنماط للمطالبات" width="800"/>

*نظرة عامة على الأنماط الثمانية لهندسة المطالبات وحالات استخدامها*

<img src="../../../translated_images/ar/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="التحكم في التفكير مع GPT-5.2" width="800"/>

*تتحكم قدرة GPT-5.2 على التفكير في مقدار التفكير الذي يجب أن يقوم به النموذج — من الإجابات السريعة المباشرة إلى الاستكشاف العميق*

**حماس منخفض (سريع ومركز)** - للأسئلة البسيطة حيث تريد إجابات سريعة ومباشرة. يقوم النموذج بأدنى قدر من التفكير - أقصى حد خطوتين. استخدمه للحسابات، عمليات البحث، أو الأسئلة المباشرة.

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
> - "ما الفرق بين أنماط المطالبة ذات الحماس المنخفض والحماس العالي؟"
> - "كيف تساعد الوسوم XML في المطالبات على هيكلة رد الذكاء الاصطناعي؟"
> - "متى يجب أن أستخدم أنماط التفكير الذاتي مقابل التعليمات المباشرة؟"

**حماس عالي (عميق وشامل)** - للمشاكل المعقدة حيث تحتاج إلى تحليل شامل. يستكشف النموذج بدقة ويعرض التفكير التفصيلي. استخدمه لتصميم الأنظمة، قرارات الهندسة المعمارية، أو البحوث المعقدة.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**تنفيذ المهمة (تقدم خطوة بخطوة)** - لعمليات العمل التي تتطلب عدة خطوات. يقدم النموذج خطة مبدئية، يروي كل خطوة أثناء العمل، ثم يعطي ملخصًا. استخدم ذلك للهجرات، التنفيذ، أو أي عملية متعددة الخطوات.

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

مطالبة سلسلة التفكير تطلب من النموذج صراحة عرض طريقته في التفكير، مما يحسن الدقة للمهام المعقدة. التفصيل خطوة بخطوة يساعد البشر والذكاء الاصطناعي على فهم المنطق.

> **🤖 جرب مع دردشة [GitHub Copilot](https://github.com/features/copilot):** اسأل عن هذا النمط:
> - "كيف يمكنني تكييف نمط تنفيذ المهمة للعمليات ذات المدة الطويلة؟"
> - "ما هي أفضل الممارسات لهياكلة مقدمات الأدوات في التطبيقات الإنتاجية؟"
> - "كيف يمكنني التقاط وعرض تحديثات التقدم المرحلية في واجهة المستخدم؟"

<img src="../../../translated_images/ar/task-execution-pattern.9da3967750ab5c1e.webp" alt="نمط تنفيذ المهمة" width="800"/>

*خطة → تنفيذ → تلخيص سير العمل للمهام متعددة الخطوات*

**الكود المعتمد على الذات** - لتوليد كود عالي الجودة للإنتاج. يولد النموذج الكود باتباع معايير الإنتاج مع التعامل الصحيح للأخطاء. استخدمه عند بناء ميزات أو خدمات جديدة.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ar/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="دورة التفكير الذاتي" width="800"/>

*حلقة تحسين تكرارية - توليد، تقييم، تحديد المشاكل، تحسين، تكرار*

**التحليل المنظم** - للتقييم المتسق. يراجع النموذج الكود باستخدام إطار ثابت (الصحة، الممارسات، الأداء، الأمان، القابلية للصيانة). استخدمه لمراجعات الكود أو تقييمات الجودة.

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

> **🤖 جرب مع دردشة [GitHub Copilot](https://github.com/features/copilot):** اسأل عن التحليل المنظم:
> - "كيف يمكنني تخصيص إطار التحليل لأنواع مختلفة من مراجعات الكود؟"
> - "ما هي أفضل طريقة لتحليل الإخراج المنظم واستخدامه برمجيًا؟"
> - "كيف أضمن مستويات خطورة متناسقة عبر جلسات مراجعة مختلفة؟"

<img src="../../../translated_images/ar/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="نمط التحليل المنظم" width="800"/>

*إطار عمل لمراجعات كود متسقة مع مستويات الخطورة*

**الدردشة متعددة الأدوار** - للمحادثات التي تحتاج إلى سياق. يتذكر النموذج الرسائل السابقة ويبني عليها. استخدمه لجلسات المساعدة التفاعلية أو الأسئلة المعقدة.

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

*كيف يتراكم سياق المحادثة عبر أدوار متعددة حتى يصل لحد التوكن*

**التفكير خطوة بخطوة** - للمشاكل التي تحتاج إلى منطق مرئي. يعرض النموذج طريقة التفكير الصريحة لكل خطوة. استخدمه لمسائل الرياضيات، الألغاز المنطقية، أو عندما تحتاج لفهم طريقة التفكير.

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

*تفصيل المشاكل إلى خطوات منطقية صريحة*

**الإخراج المقيد** - للردود التي تتطلب صيغة معينة. يتبع النموذج قواعد الصيغة والطول بدقة. استخدمه للملخصات أو عندما تحتاج إلى هيكل إخراج دقيق.

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

*فرض متطلبات صيغة، طول وبنية محددة*

## استخدام موارد Azure الحالية

**تحقق من النشر:**

تأكد من وجود ملف `.env` في الدليل الجذري مع بيانات اعتماد Azure (تم إنشاؤه أثناء الوحدة 01):
```bash
cat ../.env  # يجب أن يعرض AZURE_OPENAI_ENDPOINT و API_KEY و DEPLOYMENT
```

**ابدأ التطبيق:**

> **ملاحظة:** إذا كنت قد بدأت جميع التطبيقات بالفعل باستخدام `./start-all.sh` من الوحدة 01، فإن هذه الوحدة تعمل بالفعل على المنفذ 8083. يمكنك تخطي أوامر البدء أدناه والانتقال مباشرة إلى http://localhost:8083.
**الخيار 1: استخدام لوحة تحكم Spring Boot (موصى به لمستخدمي VS Code)**

تتضمن حاوية التطوير امتداد لوحة تحكم Spring Boot، الذي يوفر واجهة بصرية لإدارة جميع تطبيقات Spring Boot. يمكنك العثور عليه في شريط النشاط على الجانب الأيسر من VS Code (ابحث عن أيقونة Spring Boot).

من لوحة تحكم Spring Boot، يمكنك:
- رؤية جميع تطبيقات Spring Boot المتاحة في مساحة العمل
- بدء/إيقاف التطبيقات بنقرة واحدة
- عرض سجلات التطبيق في الوقت الحقيقي
- مراقبة حالة التطبيق

فقط انقر على زر التشغيل بجانب "prompt-engineering" لبدء هذه الوحدة، أو ابدأ جميع الوحدات مرة واحدة.

<img src="../../../translated_images/ar/dashboard.da2c2130c904aaf0.webp" alt="لوحة تحكم Spring Boot" width="400"/>

**الخيار 2: استخدام سكربتات الشل**

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

كلا السكربتين يقومان بتحميل متغيرات البيئة تلقائيًا من ملف `.env` الجذري وسيبنيان ملفات JAR إذا لم تكن موجودة.

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

**للتوقف:**

**Bash:**
```bash
./stop.sh  # هذا الوحدة فقط
# أو
cd .. && ./stop-all.sh  # جميع الوحدات
```

**PowerShell:**
```powershell
.\stop.ps1  # هذا الموديول فقط
# أو
cd ..; .\stop-all.ps1  # جميع الموديولات
```

## لقطات شاشة للتطبيق

<img src="../../../translated_images/ar/dashboard-home.5444dbda4bc1f79d.webp" alt="الصفحة الرئيسية للوحة التحكم" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*لوحة التحكم الرئيسية تعرض جميع أنماط هندسة البرومبت الثمانية مع خصائصها وحالات الاستخدام*

## استكشاف الأنماط

واجهة الويب تتيح لك تجربة استراتيجيات برومبت مختلفة. كل نمط يحل مشكلات مختلفة - جربها لترى متى يبرع كل نهج.

> **ملاحظة: البث مقابل عدم البث** — كل صفحة نمط تقدم زرين: **🔴 بث الاستجابة (مباشر)** وخيار **بدون بث**. يستخدم البث Server-Sent Events (SSE) لعرض الرموز المميزة في الزمن الحقيقي أثناء توليد النموذج لها، لذا ترى التقدم فورًا. خيار بدون بث ينتظر اكتمال الاستجابة قبل عرضها. للبرومبتات التي تحفز التفكير العميق (مثل High Eagerness، ترميز التفكير الذاتي)، يمكن أن يستغرق الاتصال بدون بث وقتًا طويلاً — أحيانًا دقائق — بدون أي تغذية راجعة مرئية. **استخدم البث عند تجربة برومبتات معقدة** حتى تستطيع رؤية عمل النموذج وتتجنب انطباع أن الطلب قد انتهى صلاحيته.
>
> **ملاحظة: متطلبات المتصفح** — ميزة البث تستخدم Fetch Streams API (`response.body.getReader()`) الذي يتطلب متصفحًا كاملاً (Chrome، Edge، Firefox، Safari). لا يعمل في Simple Browser المدمج في VS Code، لأن الويب فيوه الخاص به لا يدعم ReadableStream API. إذا كنت تستخدم Simple Browser، فإن أزرار عدم البث ستعمل بشكل طبيعي — فقط أزرار البث هي المتأثرة. افتح `http://localhost:8083` في متصفح خارجي لتجربة كاملة.

### الحماس المنخفض مقابل الحماس العالي

اطرح سؤالًا بسيطًا مثل "ما هو 15% من 200؟" باستخدام الحماس المنخفض. ستحصل على إجابة فورية ومباشرة. الآن اطلب شيئًا معقدًا مثل "صمم استراتيجية تخزين مؤقت لواجهة برمجة تطبيقات ذات حركة مرور عالية" باستخدام الحماس العالي. انقر **🔴 بث الاستجابة (مباشر)** وراقب تفكير النموذج التفصيلي يظهر رمزًا برمز. نفس النموذج، نفس هيكل السؤال - لكن البرومبت يخبره بمدى التفكير المطلوب.

### تنفيذ المهام (مقدمات الأدوات)

تستفيد سير العمل متعددة الخطوات من التخطيط المسبق وسرد التقدم. يقوم النموذج بتحديد ما سيقوم به، يرصد كل خطوة، ثم يلخص النتائج.

### ترميز التفكير الذاتي

جرّب "إنشاء خدمة للتحقق من صحة البريد الإلكتروني". بدلاً من مجرد توليد الشفرة والتوقف، يقوم النموذج بالتوليد، التقييم مقابل معايير الجودة، تحديد نقاط الضعف، والتحسين. سترى ذلك يكرر العملية حتى تلبي الشفرة معايير الإنتاج.

### التحليل المُنظم

تحتاج مراجعات الشفرة إلى أطر تقييم موحدة. يقوم النموذج بتحليل الشفرة باستخدام فئات ثابتة (الصحة، الممارسات، الأداء، الأمان) مع مستويات شدة.

### الدردشة متعددة الأدوار

اطرح "ما هو Spring Boot؟" ثم تابع فورًا بسؤال "أرني مثالًا". يتذكر النموذج سؤالك الأول ويعطيك مثالًا محددًا لـ Spring Boot. بدون ذاكرة، سيكون السؤال الثاني غامضًا جدًا.

### التفكير خطوة بخطوة

اختر مسألة رياضية وجربها بالتفكير خطوة بخطوة وبالحماس المنخفض. الحماس المنخفض يعطيك فقط الجواب - بسرعة ولكن غير شفاف. التفكير خطوة بخطوة يظهر لك كل حساب وقرار.

### الإخراج المقيد

عندما تحتاج إلى تنسيقات محددة أو عدد كلمات معين، يفرض هذا النمط التقيد الصارم. جرب توليد ملخص مكون من 100 كلمة تمامًا بصيغة نقاط.

## ما الذي تتعلمه حقًا

**جهد التفكير يغير كل شيء**

يسمح لك GPT-5.2 بالتحكم في جهد الحوسبة من خلال برومبتاتك. الجهد المنخفض يعني استجابات سريعة مع استكشاف محدود. الجهد العالي يعني أن النموذج يأخذ وقتًا للتفكير بعمق. أنت تتعلم مطابقة الجهد مع تعقيد المهمة - لا تهدر الوقت على الأسئلة البسيطة، ولا تستعجل القرارات المعقدة أيضًا.

**البنية توجه السلوك**

هل لاحظت علامات XML في البرومبتات؟ ليست للزينة. تتبع النماذج التعليمات الهيكلية بدرجة أكبر من النصوص الحرة. عندما تحتاج إلى عمليات متعددة الخطوات أو منطق معقد، تساعد البنية النموذج على تتبع مكانه وما التالي.

<img src="../../../translated_images/ar/prompt-structure.a77763d63f4e2f89.webp" alt="بنية البرومبت" width="800"/>

*تشريح برومبت منظم جيدًا بأقسام واضحة وتنظيم بأسلوب XML*

**الجودة عبر التقييم الذاتي**

تعمل الأنماط التي تعكس الذات بجعل معايير الجودة صريحة. بدلاً من الاعتماد على "أن يفعل النموذج ذلك بشكل صحيح"، تخبره بالضبط ما معنى "صحيح": منطق صحيح، التعامل مع الأخطاء، الأداء، الأمان. يمكن للنموذج حينها تقييم مخرجاته وتحسينها. هذا يحوّل توليد الشفرة من يانصيب إلى عملية منهجية.

**السياق محدود**

تعمل المحادثات متعددة الأدوار بإدراج سجل الرسائل مع كل طلب. لكن هناك حد - لكل نموذج حد أقصى لعدد الرموز. مع نمو المحادثات، ستحتاج إلى استراتيجيات للحفاظ على السياق ذي الصلة دون الوصول إلى هذا الحد الأعلى. توضح هذه الوحدة كيفية عمل الذاكرة؛ ستتعلم لاحقًا متى تلخص، متى تنسى، ومتى تسترجع.

## الخطوات التالية

**الوحدة التالية:** [03-rag - RAG (التوليد المدعوم بالاستخراج)](../03-rag/README.md)

---

**التنقل:** [← السابق: الوحدة 01 - مقدمة](../01-introduction/README.md) | [العودة إلى الرئيسية](../README.md) | [التالي: الوحدة 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**تنويه**:
تمت ترجمة هذا المستند باستخدام خدمة الترجمة الآلية [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى لتحقيق الدقة، يرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار المستند الأصلي بلغته الأصلية هو المصدر الرسمي والمعتمد. للمعلومات الحساسة، يُنصح بالاستعانة بترجمة بشرية محترفة. نحن غير مسؤولين عن أي سوء فهم أو تفسير ناتج عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->