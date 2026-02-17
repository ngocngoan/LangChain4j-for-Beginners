# ماژول ۰۲: مهندسی پرسش با GPT-5.2

## فهرست مطالب

- [آنچه یاد می‌گیرید](../../../02-prompt-engineering)
- [پیش‌نیازها](../../../02-prompt-engineering)
- [درک مهندسی پرسش](../../../02-prompt-engineering)
- [مبانی مهندسی پرسش](../../../02-prompt-engineering)
  - [پرسش بدون نمونه (زیرو شات)](../../../02-prompt-engineering)
  - [پرسش با چند نمونه](../../../02-prompt-engineering)
  - [رشته فکری](../../../02-prompt-engineering)
  - [پرسش مبتنی بر نقش](../../../02-prompt-engineering)
  - [قالب‌های پرسش](../../../02-prompt-engineering)
- [الگوهای پیشرفته](../../../02-prompt-engineering)
- [استفاده از منابع Azure موجود](../../../02-prompt-engineering)
- [تصاویر برنامه‌ها](../../../02-prompt-engineering)
- [بررسی الگوها](../../../02-prompt-engineering)
  - [اشتیاق پایین در مقابل اشتیاق بالا](../../../02-prompt-engineering)
  - [اجرای وظایف (مقدمه‌های ابزار)](../../../02-prompt-engineering)
  - [کد خوداندیش](../../../02-prompt-engineering)
  - [تحلیل ساختاریافته](../../../02-prompt-engineering)
  - [چت چندمرحله‌ای](../../../02-prompt-engineering)
  - [استدلال گام به گام](../../../02-prompt-engineering)
  - [خروجی محدود](../../../02-prompt-engineering)
- [آنچه واقعاً یاد می‌گیرید](../../../02-prompt-engineering)
- [گام‌های بعدی](../../../02-prompt-engineering)

## آنچه یاد می‌گیرید

<img src="../../../translated_images/fa/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

در ماژول قبلی، دیدید که حافظه چگونه امکان هوش مصنوعی مکالمه‌ای را فراهم می‌کند و از مدل‌های GitHub برای تعاملات پایه استفاده کردید. حالا روی نحوه پرسیدن سوال‌ها — خود پرسش‌ها — با استفاده از GPT-5.2 از Azure OpenAI تمرکز می‌کنیم. نحوه ساختاردهی پرسش‌هایتان به شدت کیفیت پاسخ‌هایی که می‌گیرید را تحت تأثیر قرار می‌دهد. ابتدا مروری بر تکنیک‌های پایه پرسش‌سازی داریم، سپس به هشت الگوی پیشرفته می‌پردازیم که از قابلیت‌های GPT-5.2 به‌طور کامل بهره می‌برند.

ما از GPT-5.2 استفاده می‌کنیم چون کنترل استدلال را معرفی می‌کند — می‌توانید به مدل بگویید چقدر باید قبل از پاسخ دادن فکر کند. این باعث می‌شود استراتژی‌های مختلف پرسش‌سازی واضح‌تر شود و به شما کمک می‌کند بفهمید چه زمانی از کدام روش استفاده کنید. همچنین از محدودیت‌های نرخ کمتر Azure برای GPT-5.2 نسبت به مدل‌های GitHub بهره‌مند می‌شویم.

## پیش‌نیازها

- گذراندن ماژول ۰۱ (منابع Azure OpenAI مستقر شده)
- فایل `.env` در دایرکتوری اصلی حاوی اعتبارنامه‌های Azure ( ایجاد شده توسط دستور `azd up` در ماژول ۰۱)

> **نکته:** اگر ماژول ۰۱ را کامل نکرده‌اید، ابتدا دستورالعمل‌های استقرار آن را دنبال کنید.

## درک مهندسی پرسش

<img src="../../../translated_images/fa/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

مهندسی پرسش درباره طراحی متنی است که ورودی می‌دهید و بطور مداوم نتایجی که نیاز دارید را به دست می‌دهد. تنها درباره پرسیدن سوال نیست — بلکه ساختاربندی درخواست‌هاست به گونه‌ای که مدل دقیقاً بفهمد شما چه می‌خواهید و چگونه آن را ارائه دهد.

به آن مثل دادن دستورالعمل به یک همکار فکر کنید. «رفع اشکال» مبهم است. «رفع خطای اشاره‌گر null در UserService.java خط ۴۵ با اضافه کردن یک بررسی null» خاص و دقیق است. مدل‌های زبانی هم همینطور عمل می‌کنند — دقت و ساختار مهم است.

<img src="../../../translated_images/fa/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j زیرساخت‌ها را فراهم می‌کند — اتصال به مدل، حافظه، و نوع پیام‌ها — در حالی که الگوهای پرسش صرفاً متن‌های ساختاربندی‌شده‌ای هستند که از طریق آن زیرساخت ارسال می‌کنید. بلوک‌های اصلی عبارتند از `SystemMessage` (که رفتار و نقش هوش مصنوعی را تعیین می‌کند) و `UserMessage` (که درخواست واقعی شما را حمل می‌کند).

## مبانی مهندسی پرسش

<img src="../../../translated_images/fa/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

قبل از ورود به الگوهای پیشرفته در این ماژول، بیایید پنج تکنیک اساسی پرسش‌سازی را مرور کنیم. این‌ها بلوک‌های ساختاری هستند که هر مهندس پرسش باید بشناسد. اگر قبلاً ماژول [شروع سریع](../00-quick-start/README.md#2-prompt-patterns) را گذرانده‌اید، این‌ها را در عمل دیده‌اید — در اینجا چارچوب مفهومی پشت آن‌ها آمده است.

### پرسش بدون نمونه (زیرو شات)

ساده‌ترین روش: مدل را با یک دستور مستقیم بدون نمونه بدهید. مدل کاملاً بر آموزش خود تکیه می‌کند تا کار را بفهمد و اجرا کند. این برای درخواست‌های ساده‌ای که رفتار مورد انتظار واضح است، خوب عمل می‌کند.

<img src="../../../translated_images/fa/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*دستور مستقیم بدون نمونه — مدل فقط از خود دستور پرورش می‌دهد*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// پاسخ: «مثبت»
```

**چه زمانی استفاده شود:** طبقه‌بندی‌های ساده، سوالات مستقیم، ترجمه‌ها، یا هر کاری که مدل بدون راهنمایی اضافی می‌تواند انجام دهد.

### پرسش با چند نمونه

نمونه‌هایی ارائه دهید که الگوی مورد نظر شما را به مدل نشان می‌دهد. مدل قالب ورودی-خروجی مورد انتظار را از نمونه‌ها یاد می‌گیرد و آن را روی ورودی‌های جدید اعمال می‌کند. این باعث بهبود چشمگیر ثبات در وظایفی می‌شود که قالب یا رفتار مورد نظر واضح نیست.

<img src="../../../translated_images/fa/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*یادگیری از نمونه‌ها — مدل الگو را شناسایی و روی ورودی‌های جدید اعمال می‌کند*

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

**چه زمانی استفاده شود:** طبقه‌بندی‌های سفارشی، قالب‌بندی یکسان، وظایف حوزه خاص، یا زمانی که نتایج زیرو شات ناسازگار است.

### رشته فکری

از مدل بخواهید استدلال خود را گام به گام نشان دهد. به جای رفتن مستقیم به پاسخ، مدل مشکل را تقسیم می‌کند و هر قسمت را به صورت صریح بررسی می‌کند. این دقت را در مسائل ریاضی، منطق و استدلال چندمرحله‌ای بهبود می‌بخشد.

<img src="../../../translated_images/fa/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*استدلال گام به گام — تقسیم مشکلات پیچیده به مراحل منطقی صریح*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// مدل نشان می‌دهد: ۸ - ۱۵ = ۷، سپس ۱۹ = ۷ + ۱۲ سیب
```

**چه زمانی استفاده شود:** مسائل ریاضی، پازل‌های منطقی، اشکال‌زدایی، یا هر وظیفه‌ای که نشان دادن روند استدلال دقت و اعتماد را افزایش می‌دهد.

### پرسش مبتنی بر نقش

یک شخصیت یا نقش برای هوش مصنوعی قبل از پرسیدن سوال خود تعیین کنید. این زمینه‌ای فراهم می‌کند که لحن، عمق و تمرکز پاسخ را شکل می‌دهد. یک «معمار نرم‌افزار» راهنمایی متفاوتی نسبت به «توسعه‌دهنده جونیور» یا «بازرس امنیتی» می‌دهد.

<img src="../../../translated_images/fa/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*تنظیم زمینه و شخصیت — همان سوال پاسخ متفاوتی بسته به نقش داده شده دریافت می‌کند*

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

**چه زمانی استفاده شود:** بازبینی کد، آموزش، تحلیل حوزه خاص، یا زمانی که نیاز به پاسخ‌های تخصصی متناسب با سطح تجربه یا دیدگاه خاص دارید.

### قالب‌های پرسش

پرسش‌های قابل استفاده مجدد با جایگاه‌های متغیر بسازید. به جای نوشتن یک پرسش جدید هر بار، یک قالب تعریف کنید و مقادیر مختلف را در آن جایگذاری کنید. کلاس `PromptTemplate` در LangChain4j با نحو `{{variable}}` این کار را آسان می‌کند.

<img src="../../../translated_images/fa/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*پرسش‌های قابل استفاده مجدد با جایگاه متغیر — یک قالب، چند کاربرد*

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

**چه زمانی استفاده شود:** پرسش‌های تکراری با ورودی‌های مختلف، پردازش دسته‌ای، ساخت گردش‌های کاری هوش مصنوعی قابل استفاده مجدد، یا هر موقعیتی که ساختار پرسش ثابت است اما داده‌ها تغییر می‌کنند.

---

این پنج مبنا ابزار قدرتمندی برای بیشتر وظایف پرسش‌سازی در اختیار شما قرار می‌دهد. بقیه این ماژول بر این مبنا ساخته شده با **هشت الگوی پیشرفته** که از کنترل استدلال، خودارزیابی، و قابلیت‌های خروجی ساختاریافته GPT-5.2 بهره می‌برند.

## الگوهای پیشرفته

با پوشش مبانی، بیایید به هشت الگوی پیشرفته بپردازیم که این ماژول را منحصربه‌فرد می‌کند. همه مسائل نیاز به همان روش ندارند. بعضی سوالات نیاز به پاسخ‌های سریع دارند، بعضی به تفکر عمیق. بعضی نیاز به استدلال واضح دارند، برخی فقط نیاز به نتایج. هر الگویی در زیر برای سناریوی متفاوتی بهینه شده — و کنترل استدلال GPT-5.2 تفاوت‌ها را برجسته‌تر می‌کند.

<img src="../../../translated_images/fa/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*مرور هشت الگوی مهندسی پرسش و کاربردهای آن‌ها*

<img src="../../../translated_images/fa/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*کنترل استدلال GPT-5.2 به شما اجازه می‌دهد مشخص کنید مدل چقدر باید فکر کند — از پاسخ‌های سریع و مستقیم تا کاوش عمیق*

<img src="../../../translated_images/fa/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*اشتیاق پایین (سریع، مستقیم) در مقابل اشتیاق بالا (کامل، کاوشگرانه)*

**اشتیاق پایین (سریع و متمرکز)** - برای سوالات ساده که می‌خواهید پاسخ سریع و مستقیم بگیرید. مدل حداقل استدلال می‌کند - حداکثر ۲ گام. این را برای محاسبات، جستجوها، یا سوالات مستقیم استفاده کنید.

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

> 💡 **با GitHub Copilot کشف کنید:** فایل [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) را باز کرده و بپرسید:
> - "تفاوت الگوهای پرسش‌سازی اشتیاق پایین و اشتیاق بالا چیست؟"
> - "چگونه تگ‌های XML در پرسش‌ها به ساختار پاسخ هوش مصنوعی کمک می‌کنند؟"
> - "چه زمانی باید از الگوهای خوداندیشی استفاده کنم و چه زمانی دستور مستقیم بدهم؟"

**اشتیاق بالا (عمیق و کامل)** - برای مسائل پیچیده که نیاز به تحلیل جامع دارید. مدل به طور کامل کاوش می‌کند و استدلال دقیق نشان می‌دهد. این را برای طراحی سیستم، تصمیمات معماری، یا پژوهش‌های پیچیده استفاده کنید.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**اجرای وظیفه (پیشرفت مرحله به مرحله)** - برای گردش‌های کاری چندمرحله‌ای. مدل برنامه‌ای مقدماتی ارائه می‌دهد، هر مرحله را در حین انجام توضیح می‌دهد، سپس خلاصه می‌دهد. این برای مهاجرت‌ها، پیاده‌سازی‌ها، یا هر فرآیند چندمرحله‌ای کاربرد دارد.

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

پرسش رشته‌ای فکری (Chain-of-Thought) به طور صریح از مدل می‌خواهد روند استدلال خود را نشان دهد که دقت را در کارهای پیچیده افزایش می‌دهد. شکست مرحله به مرحله کمک می‌کند انسان و هوش مصنوعی منطق را بهتر بفهمند.

> **🤖 با [GitHub Copilot](https://github.com/features/copilot) Chat امتحان کنید:** درباره این الگو بپرسید:
> - "چگونه الگوی اجرای وظیفه را برای عملیات طولانی‌مدت تطبیق دهم؟"
> - "بهترین روش‌ها برای ساختاردهی مقدمه‌های ابزار در برنامه‌های تولیدی چیست؟"
> - "چگونه می‌توانم به‌روزرسانی‌های پیشرفت میانی را در رابط کاربری ثبت و نمایش دهم؟"

<img src="../../../translated_images/fa/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*برنامه‌ریزی → اجرا → خلاصه‌سازی برای وظایف چندمرحله‌ای*

**کد خوداندیش** - برای تولید کد با کیفیت تولید. مدل کدی تولید می‌کند که استانداردهای تولید را دارد و خطایابی مناسبی دارد. این را زمانی که ویژگی یا خدمات جدید می‌سازید استفاده کنید.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fa/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*چرخه بهبود تکراری - تولید، ارزیابی، شناسایی مشکلات، بهبود، تکرار*

**تحلیل ساختاریافته** - برای ارزیابی منسجم. مدل کد را با چارچوب ثابتی بررسی می‌کند (درستی، رویه‌ها، کارایی، امنیت، قابلیت نگهداری). این برای بازبینی کد یا ارزیابی کیفیت کاربردی است.

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

> **🤖 با [GitHub Copilot](https://github.com/features/copilot) Chat امتحان کنید:** درباره تحلیل ساختاریافته بپرسید:
> - "چگونه چارچوب تحلیل را برای انواع مختلف بازبینی کد سفارشی کنم؟"
> - "بهترین روش برنامه‌نویسی برای پردازش و عمل‌کردن به خروجی ساختاریافته چیست؟"
> - "چگونه اطمینان دهم سطح شدت مشکلات در جلسات بازبینی مختلف همسان باشد؟"

<img src="../../../translated_images/fa/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*چارچوب برای بازبینی کد یکسان با سطح شدت مشکلات*

**چت چندمرحله‌ای** - برای گفتگوهایی که نیاز به زمینه دارند. مدل پیام‌های قبلی را به یاد می‌آورد و روی آن‌ها بنا می‌کند. این را برای جلسات کمک تعاملی یا سوال و پاسخ پیچیده استفاده کنید.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/fa/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*چگونه زمینه مکالمه در چند نوبت تا رسیدن به حد توکن انباشته می‌شود*

**استدلال گام به گام** - برای مسائلی که نیاز به منطق قابل مشاهده دارند. مدل استدلال صریح هر گام را نشان می‌دهد. این برای مسائل ریاضی، پازل‌های منطقی، یا زمانی که نیاز به درک فرآیند تفکر دارید استفاده کنید.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fa/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*تقسیم مسائل به گام‌های منطقی صریح*

**خروجی محدود** - برای پاسخ‌هایی که قالب و فرمت خاص دارند. مدل به شدت از قوانین قالب و طول پیروی می‌کند. این را برای خلاصه‌ها یا زمانی که نیاز به ساختار خروجی دقیق دارید استفاده کنید.

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

<img src="../../../translated_images/fa/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*اجبار به رعایت قالب، طول و ساختار مشخص*

## استفاده از منابع Azure موجود

**تایید استقرار:**

اطمینان حاصل کنید فایل `.env` در دایرکتوری اصلی وجود دارد که شامل اعتبارنامه‌های Azure است (ایجاد شده در طول ماژول ۰۱):
```bash
cat ../.env  # باید AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT را نشان دهد
```

**شروع برنامه:**

> **نکته:** اگر قبلاً همه برنامه‌ها را با استفاده از `./start-all.sh` از ماژول ۰۱ راه‌اندازی کرده‌اید، این ماژول هم‌اکنون روی پورت 8083 اجرا می‌شود. می‌توانید دستورات شروع زیر را رد کرده و مستقیماً به http://localhost:8083 بروید.

**گزینه ۱: استفاده از Spring Boot Dashboard (توصیه شده برای کاربران VS Code)**

کانتینر توسعه شامل افزونه Spring Boot Dashboard است که رابط بصری برای مدیریت همه برنامه‌های Spring Boot فراهم می‌کند. می‌توانید آن را در نوار فعالیت سمت چپ VS Code (نماد Spring Boot را جستجو کنید) پیدا کنید.
از داشبورد Spring Boot، شما می‌توانید:
- همه برنامه‌های Spring Boot موجود در فضای کاری را ببینید
- برنامه‌ها را تنها با یک کلیک راه‌اندازی یا متوقف کنید
- لاگ‌های برنامه را به صورت زنده مشاهده کنید
- وضعیت برنامه را نظارت کنید

فقط روی دکمه پخش کنار "prompt-engineering" کلیک کنید تا این ماژول راه‌اندازی شود، یا همه ماژول‌ها را همزمان آغاز کنید.

<img src="../../../translated_images/fa/dashboard.da2c2130c904aaf0.webp" alt="صفحه کنترل Spring Boot" width="400"/>

**گزینه ۲: استفاده از اسکریپت‌های شل**

راه‌اندازی همه برنامه‌های وب (ماژول‌های ۰۱-۰۴):

**Bash:**
```bash
cd ..  # از دایرکتوری ریشه
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # از دایرکتوری ریشه
.\start-all.ps1
```

یا فقط این ماژول را راه‌اندازی کنید:

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

هر دو اسکریپت به طور خودکار متغیرهای محیطی را از فایل `.env` ریشه بارگذاری می‌کنند و اگر فایل‌های JAR موجود نباشند آنها را می‌سازند.

> **توجه:** اگر ترجیح می‌دهید قبل از شروع همه ماژول‌ها را به صورت دستی بسازید:
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

آدرس http://localhost:8083 را در مرورگر خود باز کنید.

**برای توقف:**

**Bash:**
```bash
./stop.sh  # فقط این ماژول
# یا
cd .. && ./stop-all.sh  # همه ماژول‌ها
```

**PowerShell:**
```powershell
.\stop.ps1  # فقط این ماژول
# یا
cd ..; .\stop-all.ps1  # همه ماژول‌ها
```

## تصاویر برنامه

<img src="../../../translated_images/fa/dashboard-home.5444dbda4bc1f79d.webp" alt="خانه داشبورد" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*داشبورد اصلی که ۸ الگوی مهندسی پرسش را با ویژگی‌ها و کاربردهایشان نشان می‌دهد*

## گردش در الگوها

رابط وب به شما اجازه می‌دهد که با استراتژی‌های مختلف پرسش آزمایش کنید. هر الگو مشکلات متفاوتی را حل می‌کند - آنها را امتحان کنید تا ببینید هر روش چه زمانی کارایی دارد.

### تمایل کم در مقابل تمایل زیاد

یک سوال ساده مانند «۱۵٪ از ۲۰۰ چقدر است؟» را با تمایل کم بپرسید. پاسخ سریع و مستقیم دریافت می‌کنید. اکنون سوالی پیچیده‌تر مثل «یک استراتژی کشینگ برای یک API پرترافیک طراحی کن» را با تمایل زیاد بپرسید. ببینید چگونه مدل کند می‌شود و تفصیلی استدلال می‌کند. همان مدل، ساختار سوال یکسان - اما پرامپت به آن می‌گوید چقدر باید فکر کند.

<img src="../../../translated_images/fa/low-eagerness-demo.898894591fb23aa0.webp" alt="نمایش تمایل کم" width="800"/>

*محاسبه سریع با حداقل استدلال*

<img src="../../../translated_images/fa/high-eagerness-demo.4ac93e7786c5a376.webp" alt="نمایش تمایل زیاد" width="800"/>

*استراتژی کشینگ جامع (۲.۸MB)*

### اجرای وظیفه (آغازگرهای ابزار)

جریان‌های کاری چندمرحله‌ای از برنامه‌ریزی اولیه و روایت پیشرفت بهره می‌برند. مدل توضیح می‌دهد چه کاری انجام می‌دهد، هر مرحله را روایت می‌کند و سپس نتایج را خلاصه می‌کند.

<img src="../../../translated_images/fa/tool-preambles-demo.3ca4881e417f2e28.webp" alt="اجرای وظیفه" width="800"/>

*ایجاد یک endpoint REST با روایت مرحله به مرحله (۳.۹MB)*

### کد خودبازتاب‌دهنده

سعی کنید «یک سرویس اعتبارسنجی ایمیل بساز». به جای فقط تولید کد و توقف، مدل کد را می‌سازد، بر اساس معیارهای کیفیت ارزیابی می‌کند، نقاط ضعف را شناسایی و بهبود می‌دهد. شما خواهید دید که مدل تکرار می‌شود تا زمانی که کد استانداردهای تولید را برآورده سازد.

<img src="../../../translated_images/fa/self-reflecting-code-demo.851ee05c988e743f.webp" alt="نمایش کد خودبازتاب‌دهنده" width="800"/>

*سرویس کامل اعتبارسنجی ایمیل (۵.۲MB)*

### تحلیل ساختاریافته

بازبینی کد نیازمند چارچوب‌های ارزیابی منظم است. مدل کد را با استفاده از دسته‌بندی‌های ثابت (درستی، روش‌ها، عملکرد، امنیت) با سطوح شدت تحلیل می‌کند.

<img src="../../../translated_images/fa/structured-analysis-demo.9ef892194cd23bc8.webp" alt="نمایش تحلیل ساختاریافته" width="800"/>

*بازبینی کد مبتنی بر چارچوب*

### چت چند مرحله‌ای

سوال «Spring Boot چیست؟» را بپرسید، سپس بلافاصله سوال «یک مثال نشان بده» را دنبال کنید. مدل سوال اول شما را به یاد می‌آورد و یک مثال مخصوص Spring Boot ارائه می‌دهد. بدون حافظه، سوال دوم خیلی مبهم بود.

<img src="../../../translated_images/fa/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="نمایش چت چند مرحله‌ای" width="800"/>

*حفظ زمینه در سراسر سوالات*

### استدلال گام به گام

یک مسئله ریاضی انتخاب کنید و آن را با هر دو روش استدلال گام به گام و تمایل کم امتحان کنید. تمایل کم فقط پاسخ را می‌دهد - سریع ولی نامشخص. روش گام به گام هر محاسبه و تصمیم را به شما نشان می‌دهد.

<img src="../../../translated_images/fa/step-by-step-reasoning-demo.12139513356faecd.webp" alt="نمایش استدلال گام به گام" width="800"/>

*مسئله ریاضی با گام‌های صریح*

### خروجی محدود شده

وقتی به قالب‌های خاص یا تعداد کلمات معین نیاز دارید، این الگو رعایت دقیق را تضمین می‌کند. امتحان کنید که خلاصه‌ای با دقیقاً ۱۰۰ کلمه در قالب بولت پوینت تولید کنید.

<img src="../../../translated_images/fa/constrained-output-demo.567cc45b75da1633.webp" alt="نمایش خروجی محدود شده" width="800"/>

*خلاصه یادگیری ماشینی با کنترل قالب*

## آنچه واقعاً یاد می‌گیرید

**تلاش استدلال همه چیز را تغییر می‌دهد**

GPT-5.2 به شما اجازه می‌دهد تلاش محاسباتی را از طریق پرامپت‌ها کنترل کنید. تلاش کم یعنی پاسخ‌های سریع با کمترین جستجو. تلاش زیاد یعنی مدل زمان می‌گذارد تا عمیق فکر کند. شما یاد می‌گیرید تلاش را با پیچیدگی کار هماهنگ کنید - وقت خود را روی سوالات ساده تلف نکنید اما در تصمیم‌گیری‌های پیچیده هم عجله نکنید.

**ساختار رفتار را هدایت می‌کند**

توجه به تگ‌های XML در پرامپت‌ها؟ آنها تزئینی نیستند. مدل‌ها دستورالعمل‌های ساختار یافته را بهتر از متن آزاد دنبال می‌کنند. وقتی به فرایندهای چندمرحله‌ای یا منطق پیچیده نیاز دارید، ساختار به مدل کمک می‌کند محل و مرحله بعد را دنبال کند.

<img src="../../../translated_images/fa/prompt-structure.a77763d63f4e2f89.webp" alt="ساختار پرامپت" width="800"/>

*ساختار یک پرامپت خوب با بخش‌های روشن و سازماندهی به سبک XML*

**کیفیت از طریق خودارزیابی**

الگوهای خودبازتاب‌دهنده با روشن کردن معیارهای کیفیت کار می‌کنند. به جای اینکه امید ببرید مدل «درست انجام دهد»، دقیقا به آن می‌گویید «درست» یعنی چه: منطق صحیح، مدیریت خطا، عملکرد، امنیت. در این صورت مدل می‌تواند خروجی خودش را ارزیابی و بهبود دهد. این باعث می‌شود تولید کد از یک قرعه‌کشی به یک فرایند تبدیل شود.

**زمینه محدود است**

مکالمات چند مرحله‌ای با درج تاریخچه پیام‌ها در هر درخواست کار می‌کنند. اما یک حد وجود دارد - هر مدل سقف تعداد توکن دارد. با افزایش گفتگوها، به استراتژی‌هایی نیاز دارید که زمینه مرتبط را حفظ کنند بدون رسیدن به این حد. این ماژول نشان می‌دهد حافظه چگونه کار می‌کند؛ بعداً یاد خواهید گرفت چه زمانی خلاصه کنید، چه زمانی فراموش کنید و چه زمانی بازیابی انجام دهید.

## مراحل بعدی

**ماژول بعدی:** [03-rag - تولید تقویت‌شده با بازیابی (RAG)](../03-rag/README.md)

---

**ناوبری:** [← قبلی: ماژول ۰۱ - معرفی](../01-introduction/README.md) | [بازگشت به اصلی](../README.md) | [بعدی: ماژول ۰۳ - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**توضیح مهم**:  
این سند با استفاده از سرویس ترجمه هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما برای دقت تلاش می‌کنیم، لطفاً بدانید که ترجمه‌های خودکار ممکن است حاوی اشتباهات یا نادرستی‌هایی باشند. سند اصلی به زبان مادری خود باید به عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حساس، استفاده از ترجمه حرفه‌ای انسانی توصیه می‌شود. ما در قبال هرگونه سوتفاهم یا سوءتفسیر ناشی از استفاده از این ترجمه مسئولیتی نداریم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->