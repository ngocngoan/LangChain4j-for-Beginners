# الوحدة 02: هندسة المطالبات مع GPT-5.2

## جدول المحتويات

- [ما الذي ستتعلمه](../../../02-prompt-engineering)
- [المتطلبات الأساسية](../../../02-prompt-engineering)
- [فهم هندسة المطالبات](../../../02-prompt-engineering)
- [أساسيات هندسة المطالبات](../../../02-prompt-engineering)
  - [المطالبة بدون أمثلة](../../../02-prompt-engineering)
  - [المطالبة ببعض الأمثلة](../../../02-prompt-engineering)
  - [سلسلة التفكير](../../../02-prompt-engineering)
  - [المطالبة المعتمدة على الدور](../../../02-prompt-engineering)
  - [قوالب المطالبات](../../../02-prompt-engineering)
- [الأنماط المتقدمة](../../../02-prompt-engineering)
- [استخدام الموارد الحالية في أزور](../../../02-prompt-engineering)
- [لقطات شاشة التطبيق](../../../02-prompt-engineering)
- [استكشاف الأنماط](../../../02-prompt-engineering)
  - [انخفاض الحماس مقابل ارتفاع الحماس](../../../02-prompt-engineering)
  - [تنفيذ المهمة (مقدمات الأدوات)](../../../02-prompt-engineering)
  - [الكود العاكس لنفسه](../../../02-prompt-engineering)
  - [التحليل المنظم](../../../02-prompt-engineering)
  - [الدردشة متعددة الجولات](../../../02-prompt-engineering)
  - [التفكير خطوة بخطوة](../../../02-prompt-engineering)
  - [الإخراج المقيد](../../../02-prompt-engineering)
- [ما الذي تتعلمه حقًا](../../../02-prompt-engineering)
- [الخطوات التالية](../../../02-prompt-engineering)

## ما الذي ستتعلمه

<img src="../../../translated_images/ar/what-youll-learn.c68269ac048503b2.webp" alt="ما الذي ستتعلمه" width="800"/>

في الوحدة السابقة، رأيت كيف تمكن الذاكرة الذكاء الاصطناعي المحادثي واستخدمت نماذج GitHub للتفاعلات الأساسية. الآن سنركز على كيفية طرح الأسئلة — أي المطالبات نفسها — باستخدام GPT-5.2 من Azure OpenAI. الطريقة التي تبني بها مطالباتك تؤثر بشكل كبير على جودة الردود التي تحصل عليها. نبدأ بمراجعة تقنيات المطالبة الأساسية، ثم ننتقل إلى ثمانية أنماط متقدمة تستفيد بالكامل من قدرات GPT-5.2.

سنستخدم GPT-5.2 لأنه يقدم التحكم في التفكير — يمكنك إخبار النموذج بكمية التفكير التي يجب أن يقوم بها قبل الإجابة. هذا يجعل استراتيجيات المطالبة المختلفة أكثر وضوحًا ويساعدك على فهم متى تستخدم كل نهج. كما سنستفيد من حدود المعدل الأقل في Azure لـ GPT-5.2 مقارنة بنماذج GitHub.

## المتطلبات الأساسية

- إكمال الوحدة 01 (تم نشر موارد Azure OpenAI)
- وجود ملف `.env` في الدليل الجذري يحتوي على بيانات اعتماد Azure (تم إنشاؤه بواسطة `azd up` في الوحدة 01)

> **ملاحظة:** إذا لم تكمل الوحدة 01، فاتبع تعليمات النشر هناك أولاً.

## فهم هندسة المطالبات

<img src="../../../translated_images/ar/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="ما هي هندسة المطالبات؟" width="800"/>

هندسة المطالبات هي تصميم نص الإدخال بحيث تحصل باستمرار على النتائج التي تحتاجها. الأمر لا يقتصر على طرح الأسئلة فقط — بل يتعلق بهيكلة الطلبات حتى يفهم النموذج بالضبط ما تريد وكيفية تقديمه.

فكر فيها على أنها إعطاء تعليمات لزميلك في العمل. "إصلاح الخطأ" عبارة غامضة. "إصلاح استثناء مؤشر السلسلة null pointer exception في UserService.java السطر 45 عن طريق إضافة فحص على null" عبارة محددة. نماذج اللغة تعمل بنفس الطريقة — الدقة والهيكلة مهمة.

<img src="../../../translated_images/ar/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="كيف يتناسب LangChain4j" width="800"/>

يوفر LangChain4j البنية التحتية — اتصالات النماذج، الذاكرة، وأنواع الرسائل — بينما أنماط المطالبات هي مجرد نص منسق بعناية ترسله عبر تلك البنية التحتية. اللبنات الأساسية هي `SystemMessage` (الذي يحدد سلوك ودور الـ AI) و `UserMessage` (الذي يحمل طلبك الفعلي).

## أساسيات هندسة المطالبات

<img src="../../../translated_images/ar/five-patterns-overview.160f35045ffd2a94.webp" alt="نظرة عامة على خمسة أنماط هندسة المطالبات" width="800"/>

قبل الغوص في الأنماط المتقدمة في هذه الوحدة، دعنا نراجع خمسة تقنيات مطالبة أساسية. هذه هي اللبنات الأساسية التي يجب أن يعرفها كل مهندس مطالبة. إذا كنت قد عملت بالفعل عبر [وحدة البداية السريعة](../00-quick-start/README.md#2-prompt-patterns)، فقد شاهدت هذه التقنيات أثناء التنفيذ — إليك الإطار المفاهيمي الذي يقف خلفها.

### المطالبة بدون أمثلة

الأسهل: أعطِ النموذج تعليمات مباشرة من دون أمثلة. يعتمد النموذج كليًا على تدريبه لفهم وتنفيذ المهمة. يعمل هذا جيدًا للطلبات البسيطة حيث يكون السلوك المتوقع واضحًا.

<img src="../../../translated_images/ar/zero-shot-prompting.7abc24228be84e6c.webp" alt="المطالبة بدون أمثلة" width="800"/>

*تعليمات مباشرة بدون أمثلة — يستنتج النموذج المهمة من التعليمات فقط*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// رد: "إيجابي"
```
  
**متى تستخدم:** التصنيفات البسيطة، الأسئلة المباشرة، الترجمات، أو أي مهمة يمكن للنموذج التعامل معها بدون إرشادات إضافية.

### المطالبة ببعض الأمثلة

قدم أمثلة توضح النمط الذي تريد من النموذج اتباعه. يتعلم النموذج تنسيق الإدخال-الإخراج المتوقع من أمثلتك ويطبقه على مدخلات جديدة. يحسن هذا بشكل كبير من الاتساق في المهام التي يكون فيها الشكل أو السلوك المطلوب غير واضح.

<img src="../../../translated_images/ar/few-shot-prompting.9d9eace1da88989a.webp" alt="المطالبة ببعض الأمثلة" width="800"/>

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
  
**متى تستخدم:** التصنيفات المخصصة، التنسيق المتسق، المهام الخاصة بالمجال، أو عندما تكون نتائج المطالبة بدون أمثلة غير متسقة.

### سلسلة التفكير

اطلب من النموذج عرض تفكيره خطوة بخطوة. بدلًا من القفز مباشرة إلى الإجابة، يقوم النموذج بتحليل المشكلة ويعمل على كل جزء بشكل واضح. هذا يحسن الدقة في الرياضيات، المنطق، والمهام متعددة الخطوات.

<img src="../../../translated_images/ar/chain-of-thought.5cff6630e2657e2a.webp" alt="مطالبة سلسلة التفكير" width="800"/>

*تفكير خطوة بخطوة — تحليل المشاكل المعقدة إلى خطوات منطقية صريحة*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// يُظهر النموذج: ١٥ - ٨ = ٧، ثم ٧ + ١٢ = ١٩ تفاحة
```
  
**متى تستخدم:** مسائل الرياضيات، الألغاز المنطقية، التصحيح، أو أي مهمة يظهر فيها عرض عملية التفكير تحسينًا في الدقة والثقة.

### المطالبة المعتمدة على الدور

حدد شخصية أو دور للـ AI قبل طرح سؤالك. هذا يوفر سياقًا يشكل نغمة، عمق وتركيز الرد. "مهندس برمجيات" يقدم نصيحة مختلفة عن "مطور مبتدئ" أو "مدقق أمني".

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
  
**متى تستخدم:** مراجعات الكود، التدريس، التحليل الخاص بالمجال، أو عندما تحتاج ردودًا مخصصة لمستوى خبرة أو منظور معين.

### قوالب المطالبات

أنشئ مطالبات قابلة لإعادة الاستخدام مع متغيرات كأماكن مخصصة. بدلًا من كتابة مطالبة جديدة في كل مرة، عرّف قالبًا مرة واملأه بقيم مختلفة. فئة `PromptTemplate` في LangChain4j تسهل هذا باستخدام صيغة `{{variable}}`.

<img src="../../../translated_images/ar/prompt-templates.14bfc37d45f1a933.webp" alt="قوالب المطالبات" width="800"/>

*مطالبات قابلة لإعادة الاستخدام مع متغيرات — قالب واحد، استخدامات متعددة*

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
  
**متى تستخدم:** استعلامات متكررة مع مدخلات مختلفة، المعالجة الدُفعية، بناء تدفقات عمل AI قابلة لإعادة الاستخدام، أو أي سيناريو يبقى فيه هيكل المطالبة نفسه لكن البيانات تتغير.

---

تعطيك هذه الأساسيات الخمسة مجموعة أدوات قوية لمعظم مهام المطالبة. بقية هذه الوحدة تبني عليها مع **ثمانية أنماط متقدمة** تستغل تحكم GPT-5.2 في التفكير، التقييم الذاتي، وقدرات الناتج المنظم.

## الأنماط المتقدمة

مع تغطية الأساسيات، دعنا ننتقل إلى الأنماط المتقدمة الثمانية التي تجعل هذه الوحدة فريدة. ليست كل المشاكل تحتاج لنفس النهج. بعض الأسئلة تحتاج إجابات سريعة، وأخرى تحتاج تفكيرًا عميقًا. بعضها يحتاج تفكيرًا مرئيًا، والبعض الآخر يحتاج فقط النتائج. كل نمط أدناه محسّن لسيناريو مختلف — وتحكم التفكير في GPT-5.2 يجعل الاختلافات أكثر وضوحًا.

<img src="../../../translated_images/ar/eight-patterns.fa1ebfdf16f71e9a.webp" alt="ثمانية أنماط للمطالبة" width="800"/>

*نظرة عامة على ثمانية أنماط لهندسة المطالبات وحالات استخدامها*

<img src="../../../translated_images/ar/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="التحكم في التفكير مع GPT-5.2" width="800"/>

*تحكم GPT-5.2 في التفكير يسمح لك بتحديد كمية التفكير التي يجب أن يقوم بها النموذج — من إجابات سريعة مباشرة إلى استكشاف عميق*

**انخفاض الحماس (سريع ومركّز)** - للأسئلة البسيطة حيث تريد إجابات سريعة ومباشرة. يقوم النموذج بأدنى قدر من التفكير - بحد أقصى خطوتين. استخدم هذا للحسابات، البحث أو الأسئلة المباشرة.

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
  
> 💡 **جرب مع GitHub Copilot:** افتح [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) واسأل:  
> - "ما الفرق بين أنماط المطالبة ذات الحماس المنخفض والحماس العالي؟"  
> - "كيف تساعد علامات XML في المطالبات على هيكلة رد الذكاء الاصطناعي؟"  
> - "متى يجب أن أستخدم أنماط التفكير الذاتي مقابل التعليمات المباشرة؟"

**ارتفاع الحماس (عميق ودقيق)** - للمشاكل المعقدة حيث تريد تحليلاً شاملاً. يستكشف النموذج بتمعن ويظهر تفكيرًا مفصلًا. استخدم هذا لتصميم النظام، قرارات الهندسة المعمارية، أو الأبحاث المعقدة.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**تنفيذ المهمة (التقدم خطوة بخطوة)** - لأعمال التدفق متعددة الخطوات. يقدم النموذج خطة مبدئية، يروي كل خطوة أثناء العمل، ثم يعطي ملخصًا. استخدم هذا للهجرات، التنفيذات، أو أي عملية متعددة الخطوات.

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
  
مطالبة سلسلة التفكير تطلب من النموذج بشكل صريح عرض عملية تفكيره، مما يحسن الدقة للمهام المعقدة. يسمح التحليل خطوة بخطوة لكل من البشر والذكاء الاصطناعي بفهم المنطق.

> **🤖 جرب مع دردشة [GitHub Copilot](https://github.com/features/copilot):** اسأل عن هذا النمط:  
> - "كيف يمكنني تعديل نمط تنفيذ المهمة للعمليات ذات التشغيل الطويل؟"  
> - "ما أفضل الممارسات لتنظيم مقدمات الأدوات في تطبيقات الإنتاج؟"  
> - "كيف يمكنني التقاط وعرض تحديثات التقدم الوسيط في واجهة المستخدم؟"

<img src="../../../translated_images/ar/task-execution-pattern.9da3967750ab5c1e.webp" alt="نمط تنفيذ المهمة" width="800"/>

*خطة → تنفيذ → ملخص لتدفقات العمل متعددة الخطوات*

**الكود العاكس لنفسه** - لتوليد كود عالي الجودة للإنتاج. يقوم النموذج بإنشاء كود يتبع معايير الإنتاج مع معالجة مناسبة للأخطاء. استخدم هذا عند بناء ميزات أو خدمات جديدة.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/ar/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="دورة التفكير الذاتي" width="800"/>

*حلقة تحسين تكرارية - إنشاء، تقييم، تحديد المشكلات، تحسين، تكرار*

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
  
> **🤖 جرب مع دردشة [GitHub Copilot](https://github.com/features/copilot):** اسأل عن التحليل المنظم:  
> - "كيف يمكنني تخصيص إطار التحليل لأنواع مختلفة من مراجعات الكود؟"  
> - "ما أفضل طريقة لتحليل والتعامل مع الناتج المنظم برمجيًا؟"  
> - "كيف أضمن مستويات شدة متسقة عبر جلسات مراجعة مختلفة؟"

<img src="../../../translated_images/ar/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="نمط التحليل المنظم" width="800"/>

*إطار عمل لمراجعات كود متسقة مع مستويات الشدة*

**الدردشة متعددة الجولات** - للمحادثات التي تحتاج إلى سياق. يتذكر النموذج الرسائل السابقة ويبني عليها. استخدم هذا لجلسات المساعدة التفاعلية أو الأسئلة المعقدة.

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

*كيف يتراكم سياق المحادثة على عدة جولات حتى الوصول إلى حد الرموز*

**التفكير خطوة بخطوة** - للمشاكل التي تتطلب منطقًا مرئيًا. يعرض النموذج تفكيرًا صريحًا لكل خطوة. استخدم هذا في مسائل الرياضيات، الألغاز المنطقية، أو عندما تحتاج إلى فهم عملية التفكير.

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

**الإخراج المقيد** - للردود التي تتطلب تنسيقًا محددًا. يتبع النموذج قواعد صارمة لشكل وطول الإخراج. استخدم هذا للملخصات أو عندما تحتاج إلى هيكل إخراج دقيق.

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

## استخدام الموارد الحالية في أزور

**التحقق من النشر:**

تأكد من وجود ملف `.env` في الدليل الجذري مع بيانات اعتماد Azure (تم إنشاؤه أثناء الوحدة 01):  
```bash
cat ../.env  # يجب عرض AZURE_OPENAI_ENDPOINT و API_KEY و DEPLOYMENT
```
  
**بدء التطبيق:**

> **ملاحظة:** إذا كنت قد بدأت جميع التطبيقات بالفعل باستخدام `./start-all.sh` من الوحدة 01، فهذه الوحدة تعمل بالفعل على المنفذ 8083. يمكنك تخطي أوامر البدء أدناه والانتقال مباشرة إلى http://localhost:8083.

**الخيار 1: استخدام لوحة تحكم Spring Boot (موصى به لمستخدمي VS Code)**

يحتوي حاوية التطوير على إضافة لوحة تحكم Spring Boot، التي توفر واجهة بصرية لإدارة جميع تطبيقات Spring Boot. يمكنك العثور عليها في شريط النشاط على الجانب الأيسر من VS Code (ابحث عن أيقونة Spring Boot).

من لوحة تحكم Spring Boot، يمكنك:  
- رؤية جميع تطبيقات Spring Boot المتاحة في مساحة العمل  
- بدء/إيقاف التطبيقات بنقرة واحدة  
- عرض سجلات التطبيق في الوقت الفعلي  
- مراقبة حالة التطبيق
ببساطة انقر على زر التشغيل بجانب "prompt-engineering" لبدء هذه الوحدة، أو ابدأ جميع الوحدات مرة واحدة.

<img src="../../../translated_images/ar/dashboard.da2c2130c904aaf0.webp" alt="لوحة تحكم Spring Boot" width="400"/>

**الخيار 2: استخدام سكربتات الشل**

ابدأ جميع تطبيقات الويب (الوحدات 01-04):

**Bash:**
```bash
cd ..  # من الدليل الجذري
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

تقوم كلا السكربتين بتحميل متغيرات البيئة تلقائيًا من ملف `.env` الموجود في الجذر وستبني ملفات JAR إذا لم تكن موجودة.

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
./stop.sh  # هذا النموذج فقط
# أو
cd .. && ./stop-all.sh  # جميع النماذج
```

**PowerShell:**
```powershell
.\stop.ps1  # هذا الوحدة فقط
# أو
cd ..; .\stop-all.ps1  # كل الوحدات
```

## لقطات شاشة للتطبيق

<img src="../../../translated_images/ar/dashboard-home.5444dbda4bc1f79d.webp" alt="الرئيسية في لوحة التحكم" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*لوحة التحكم الرئيسية تعرض جميع أنماط هندسة البرومبت الثمانية مع خصائصها وحالات استخدامها*

## استكشاف الأنماط

تتيح لك واجهة الويب تجربة استراتيجيات برومبت مختلفة. كل نمط يحل مشاكل مختلفة - جربها لترى متى يبرز كل نهج.

### الحماس المنخفض مقابل الحماس العالي

اسأل سؤالًا بسيطًا مثل «ما هو 15% من 200؟» باستخدام الحماس المنخفض. ستحصل على إجابة فورية ومباشرة. الآن اسأل شيئًا معقدًا مثل «صمم استراتيجية تخزين مؤقت لـ API عالي المرور» باستخدام الحماس العالي. شاهد كيف يتباطأ النموذج ويقدم تفسيرات مفصلة. نفس النموذج، نفس هيكل السؤال - لكن البرومبت يخبره بكمية التفكير المطلوب.

<img src="../../../translated_images/ar/low-eagerness-demo.898894591fb23aa0.webp" alt="عرض الحماس المنخفض" width="800"/>

*حساب سريع مع تفكير قليل*

<img src="../../../translated_images/ar/high-eagerness-demo.4ac93e7786c5a376.webp" alt="عرض الحماس العالي" width="800"/>

*استراتيجية تخزين مؤقت شاملة (2.8MB)*

### تنفيذ المهام (مقدمات الأدوات)

تستفيد سير العمل متعدد الخطوات من التخطيط المسبق وسرد التقدم. يوضح النموذج ما سيفعله، يروي كل خطوة، ثم يلخص النتائج.

<img src="../../../translated_images/ar/tool-preambles-demo.3ca4881e417f2e28.webp" alt="عرض تنفيذ المهام" width="800"/>

*إنشاء واجهة REST مع سرد خطوة بخطوة (3.9MB)*

### كود ذاتي التأمل

جرّب “إنشاء خدمة تحقق من البريد الإلكتروني”. بدلاً من إنشاء الكود والتوقف، يقوم النموذج بالتوليد، ويقيم الجودة، ويحدد نقاط الضعف، ويحسن الأداء. سترى تكرارًا حتى يصل الكود إلى معايير الإنتاج.

<img src="../../../translated_images/ar/self-reflecting-code-demo.851ee05c988e743f.webp" alt="عرض الكود الذاتي التأمل" width="800"/>

*خدمة تحقق من البريد الإلكتروني كاملة (5.2MB)*

### التحليل المنظم

تحتاج مراجعات الكود إلى أطر تقييم متسقة. يقوم النموذج بتحليل الكود باستخدام فئات ثابتة (الصحة، الممارسات، الأداء، الأمان) مع مستويات شدة.

<img src="../../../translated_images/ar/structured-analysis-demo.9ef892194cd23bc8.webp" alt="عرض التحليل المنظم" width="800"/>

*مراجعة الكود بناءً على إطار عمل*

### محادثة متعددة الأدوار

اسأل "ما هو Spring Boot؟" ثم تابع فورًا بقول "أرني مثالًا". يتذكر النموذج سؤالك الأول ويعطيك مثالًا على Spring Boot بالتحديد. بدون الذاكرة، سيكون السؤال الثاني غامضًا جدًا.

<img src="../../../translated_images/ar/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="عرض المحادثة متعددة الأدوار" width="800"/>

*الحفاظ على السياق عبر الأسئلة*

### التفكير خطوة بخطوة

اختر مسألة رياضية وجربها بكل من التفكير خطوة بخطوة والحماس المنخفض. الحماس المنخفض يعطيك الإجابة فقط - سريع لكنه غير واضح. التفكير خطوة بخطوة يظهر لك كل حساب وقرار.

<img src="../../../translated_images/ar/step-by-step-reasoning-demo.12139513356faecd.webp" alt="عرض التفكير خطوة بخطوة" width="800"/>

*مسألة رياضية مع خطوات واضحة*

### الإخراج المقيد

عندما تحتاج إلى تنسيقات محددة أو عدد كلمات معين، يفرض هذا النمط التزامًا صارمًا. جرب إنشاء ملخص يحتوي بالضبط على 100 كلمة بصيغة نقاط.

<img src="../../../translated_images/ar/constrained-output-demo.567cc45b75da1633.webp" alt="عرض الإخراج المقيد" width="800"/>

*ملخص تعلم الآلة مع تحكم بالتنسيق*

## ما تتعلمه فعليًا

**جهد التفكير يغير كل شيء**

تتيح لك GPT-5.2 التحكم في جهد الحوسبة من خلال البرومبتات الخاصة بك. الجهد المنخفض يعني ردود سريعة مع استكشاف قليل. الجهد العالي يعني أن النموذج يأخذ وقتًا للتفكير بعمق. أنت تتعلم مطابقة الجهد لتعقيد المهمة - لا تهدر الوقت على أسئلة بسيطة، ولا تستعجل في القرارات المعقدة.

**البنية توجه السلوك**

هل لاحظت علامات XML في البرومبتات؟ هي ليست للزينة. تتبع النماذج التعليمات المنظمة بشكل أكثر موثوقية من النصوص الحرة. عندما تحتاج لعمليات متعددة الخطوات أو منطق معقد، تساعد البنية النموذج في تتبع موقعه وماذا يأتي بعده.

<img src="../../../translated_images/ar/prompt-structure.a77763d63f4e2f89.webp" alt="هيكل البرومبت" width="800"/>

*تشريح برومبت منظم جيدًا بأقسام واضحة وتنظيم بأسلوب XML*

**الجودة عبر التقييم الذاتي**

تعمل الأنماط ذات التأمل الذاتي عن طريق جعل معايير الجودة صريحة. بدلاً من الأمل في أن "يفعل النموذج الأمر بشكل صحيح"، تخبره بالضبط ماذا يعني "الصحيح": المنطق السليم، التعامل مع الأخطاء، الأداء، الأمان. يمكن للنموذج بعدها تقييم ناتجه وتحسينه. يحول هذا توليد الكود من يانصيب إلى عملية.

**السياق محدود**

تعمل المحادثات متعددة الأدوار عن طريق تضمين تاريخ الرسائل مع كل طلب. لكن هناك حد - لكل نموذج حد أقصى لعدد الرموز. مع ازدياد المحادثات، ستحتاج لاستراتيجيات للحفاظ على السياق المهم دون الوصول لهذا الحد. تعرض هذه الوحدة كيف تعمل الذاكرة؛ ستتعلم لاحقًا متى تلخص، ومتى تنسى، ومتى تسترجع.

## الخطوات التالية

**الوحدة التالية:** [03-rag - التوليد المعزز بالاسترجاع (RAG)](../03-rag/README.md)

---

**التنقل:** [← السابق: الوحدة 01 - المقدمة](../01-introduction/README.md) | [العودة للرئيسية](../README.md) | [التالي: الوحدة 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**إخلاء مسؤولية**:  
تمت ترجمة هذا المستند باستخدام خدمة الترجمة الآلية [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى جاهدين لتحقيق الدقة، يرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار المستند الأصلي بلغته الأصلية المصدر الموثوق به. بالنسبة للمعلومات الهامة، يُنصح بالترجمة البشرية الاحترافية. نحن غير مسؤولين عن أي سوء فهم أو تفسير ناتج عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->