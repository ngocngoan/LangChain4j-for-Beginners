# ماژول ۰۲: مهندسی پرامپت با GPT-5.2

## فهرست مطالب

- [بررسی ویدیویی](../../../02-prompt-engineering)
- [آنچه خواهید آموخت](../../../02-prompt-engineering)
- [پیش‌نیازها](../../../02-prompt-engineering)
- [درک مهندسی پرامپت](../../../02-prompt-engineering)
- [مبانی مهندسی پرامپت](../../../02-prompt-engineering)
  - [پرامپت صفر شات](../../../02-prompt-engineering)
  - [پرامپت چند شات](../../../02-prompt-engineering)
  - [زنجیره تفکر](../../../02-prompt-engineering)
  - [پرامپت مبتنی بر نقش](../../../02-prompt-engineering)
  - [قالب‌های پرامپت](../../../02-prompt-engineering)
- [الگوهای پیشرفته](../../../02-prompt-engineering)
- [استفاده از منابع موجود Azure](../../../02-prompt-engineering)
- [تصاویر برنامه](../../../02-prompt-engineering)
- [بررسی الگوها](../../../02-prompt-engineering)
  - [انگیزه کم در مقابل زیاد](../../../02-prompt-engineering)
  - [اجرای کار (مقدمه ابزارها)](../../../02-prompt-engineering)
  - [کد خودبازتابی](../../../02-prompt-engineering)
  - [تحلیل ساختاریافته](../../../02-prompt-engineering)
  - [چت چند مرحله‌ای](../../../02-prompt-engineering)
  - [استدلال گام به گام](../../../02-prompt-engineering)
  - [خروجی محدود](../../../02-prompt-engineering)
- [آنچه واقعاً یاد می‌گیرید](../../../02-prompt-engineering)
- [گام‌های بعدی](../../../02-prompt-engineering)

## بررسی ویدیویی

این جلسه زنده را تماشا کنید که توضیح می‌دهد چگونه با این ماژول شروع کنید: [مهندسی پرامپت با LangChain4j - جلسه زنده](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## آنچه خواهید آموخت

<img src="../../../translated_images/fa/what-youll-learn.c68269ac048503b2.webp" alt="آنچه خواهید آموخت" width="800"/>

در ماژول قبلی، دیدید که چگونه حافظه به هوش مصنوعی مکالمه‌ای کمک می‌کند و از مدل‌های GitHub برای تعاملات پایه استفاده کردید. اکنون تمرکز ما روی نحوه پرسیدن سوالات — خود پرامپت‌ها — با استفاده از GPT-5.2 از Azure OpenAI است. نحوه ساختار دادن پرامپت‌ها تأثیر زیادی بر کیفیت پاسخ‌هایی دارد که دریافت می‌کنید. ما با مرور تکنیک‌های پایه پرامپت شروع می‌کنیم، سپس به هشت الگوی پیشرفته می‌پردازیم که از قابلیت‌های GPT-5.2 بهره کامل می‌برند.

ما از GPT-5.2 استفاده خواهیم کرد چون کنترل استدلال را معرفی می‌کند — می‌توانید به مدل بگویید چقدر باید قبل از پاسخ فکر کند. این باعث می‌شود استراتژی‌های مختلف پرامپت واضح‌تر شده و به شما کمک می‌کند بفهمید هر روش را کی استفاده کنید. همچنین محدودیت‌های کمتر نرخ در Azure نسبت به مدل‌های GitHub برای GPT-5.2 یک مزیت است.

## پیش‌نیازها

- تکمیل ماژول ۰۱ (منابع Azure OpenAI مستقر شده)
- فایل `.env` در شاخه ریشه با اطلاعات ورود Azure (ایجاد شده توسط `azd up` در ماژول ۰۱)

> **توجه:** اگر ماژول ۰۱ را تکمیل نکرده‌اید، ابتدا دستورالعمل‌های استقرار آن را دنبال کنید.

## درک مهندسی پرامپت

<img src="../../../translated_images/fa/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="مهندسی پرامپت چیست؟" width="800"/>

مهندسی پرامپت درباره طراحی متن ورودی است که به طور مداوم نتایج مورد نیازتان را به شما بدهد. این فقط درباره پرسیدن سوال نیست — بلکه درباره ساختاربندی درخواست‌هاست طوری که مدل دقیقاً بفهمد چه می‌خواهید و چگونه به شما تحویل دهد.

آن را مثل دادن دستورالعمل به یک همکار تصور کنید. "اشکال را رفع کن" مبهم است. "استثنای اشاره به null در خط ۴۵ فایل UserService.java را با اضافه کردن بررسی null برطرف کن" مشخص است. مدل‌های زبانی هم همینطور هستند — مشخص بودن و ساختار اهمیت دارند.

<img src="../../../translated_images/fa/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="چگونه LangChain4j جای می‌گیرد" width="800"/>

LangChain4j زیرساخت لازم — اتصال مدل، حافظه، و انواع پیام‌ها — را فراهم می‌کند در حالی که الگوهای پرامپت فقط متن‌هایی هستند که با دقت ساختاربندی شده و از این زیرساخت عبور می‌کنند. بلوک‌های کلیدی عبارتند از `SystemMessage` (که رفتار و نقش هوش مصنوعی را تعیین می‌کند) و `UserMessage` (که درخواست واقعی شما را حمل می‌کند).

## مبانی مهندسی پرامپت

<img src="../../../translated_images/fa/five-patterns-overview.160f35045ffd2a94.webp" alt="نمای کلی پنج الگوی مهندسی پرامپت" width="800"/>

قبل از ورود به الگوهای پیشرفته در این ماژول، بیایید پنج تکنیک پایه پرامپت را مرور کنیم. این‌ها بلوک‌های ساختمانی هستند که هر مهندس پرامپت باید بداند. اگر قبلاً ماژول [شروع سریع](../00-quick-start/README.md#2-prompt-patterns) را گذرانده‌اید، این‌ها را در عمل دیده‌اید — اینجا چارچوب مفهومی پشت آن‌ها است.

### پرامپت صفر شات

ساده‌ترین روش: به مدل دستور مستقیم بدهید بدون نمونه‌ای. مدل کاملاً به آموزش خود متکی است تا وظیفه را بفهمد و اجرا کند. این برای درخواست‌های ساده که رفتار مورد انتظار واضح است خوب کار می‌کند.

<img src="../../../translated_images/fa/zero-shot-prompting.7abc24228be84e6c.webp" alt="پرامپت صفر شات" width="800"/>

*دستور مستقیم بدون نمونه — مدل از روی دستور، کار را استنباط می‌کند*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// پاسخ: "مثبت"
```

**چه زمانی استفاده شود:** طبقه‌بندی‌های ساده، سوالات مستقیم، ترجمه‌ها یا هر کاری که مدل بتواند بدون راهنمایی اضافی انجام دهد.

### پرامپت چند شات

نمونه‌هایی ارائه دهید که الگوی مورد نظر برای مدل را نشان دهند. مدل فرمت ورودی-خروجی مورد انتظار را از نمونه‌های شما می‌آموزد و آن را برای ورودی‌های جدید به کار می‌برد. این به شکل چشمگیری سازگاری را در کارهایی که فرمت یا رفتار مورد نظر واضح نیست ارتقا می‌دهد.

<img src="../../../translated_images/fa/few-shot-prompting.9d9eace1da88989a.webp" alt="پرامپت چند شات" width="800"/>

*یادگیری از نمونه‌ها — مدل الگو را شناسایی کرده و به ورودی‌های جدید اعمال می‌کند*

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

**چه زمانی استفاده شود:** طبقه‌بندی‌های سفارشی، قالب‌بندی ثابت، کارهای تخصصی حوزه یا زمانی که نتایج صفر شات ناپایدار هستند.

### زنجیره تفکر

از مدل بخواهید استدلال خود را گام به گام نشان دهد. به جای پریدن مستقیم به جواب، مدل مسئله را تجزیه کرده و هر بخش را صراحتاً حل می‌کند. این دقت را در ریاضیات، منطق و استدلال چندمرحله‌ای بهبود می‌بخشد.

<img src="../../../translated_images/fa/chain-of-thought.5cff6630e2657e2a.webp" alt="پرامپت زنجیره تفکر" width="800"/>

*استدلال گام به گام — شکستن مسائل پیچیده به گام‌های منطقی صریح*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// مدل نشان می‌دهد: ۱۵ - ۸ = ۷، سپس ۷ + ۱۲ = ۱۹ سیب
```

**چه زمانی استفاده شود:** مسائل ریاضی، معماهای منطقی، اشکال‌زدایی یا هر کاری که نمایش فرایند استدلال دقت و اعتماد را بالا می‌برد.

### پرامپت مبتنی بر نقش

قبل از پرسیدن سوال، یک شخصیت یا نقش برای هوش مصنوعی تنظیم کنید. این زمینه‌ای فراهم می‌کند که لحن، عمق و تمرکز پاسخ را شکل می‌دهد. یک «معمار نرم‌افزار» توصیه‌های متفاوتی نسبت به «توسعه‌دهنده جوان» یا «ممیزی امنیتی» می‌دهد.

<img src="../../../translated_images/fa/role-based-prompting.a806e1a73de6e3a4.webp" alt="پرامپت مبتنی بر نقش" width="800"/>

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

**چه زمانی استفاده شود:** بررسی کدها، آموزش، تحلیل‌های تخصصی حوزه یا زمانی که پاسخ‌ها باید بر اساس سطح تخصص یا دیدگاه خاص تنظیم شوند.

### قالب‌های پرامپت

پرامپت‌های قابل استفاده مجدد با جایگزین‌های متغیر ایجاد کنید. به جای نوشتن پرامپت جدید هر بار، یک قالب تعریف کنید و مقادیر مختلف در آن پر شود. کلاس `PromptTemplate` در LangChain4j این کار را با سینتکس `{{variable}}` آسان می‌کند.

<img src="../../../translated_images/fa/prompt-templates.14bfc37d45f1a933.webp" alt="قالب‌های پرامپت" width="800"/>

*پرامپت‌های قابل استفاده مجدد با جایگزین‌های متغیر — یک قالب، چند استفاده*

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

**چه زمانی استفاده شود:** پرسش‌های تکراری با ورودی‌های مختلف، پردازش دسته‌ای، ساخت جریان‌های کاری هوش مصنوعی قابل استفاده مجدد یا هر سناریویی که ساختار پرامپت ثابت باشد ولی داده تغییر کند.

---

این پنج پایه به شما جعبه‌ابزار محکمی برای اغلب وظایف پرامپت می‌دهد. بقیه این ماژول بر اساس آن‌ها با **هشت الگوی پیشرفته** ساخته شده که کنترل استدلال، خودارزیابی و قابلیت‌های خروجی ساختاریافته GPT-5.2 را به کار می‌گیرند.

## الگوهای پیشرفته

با پوشش مبانی، حالا به هشت الگوی پیشرفته می‌رویم که این ماژول را منحصربه‌فرد می‌کنند. همه مسائل به یک روش نیاز ندارند. برخی سوالات پاسخ سریع می‌خواهند، برخی نیاز به تفکر عمیق دارند. برخی نیاز به استدلال دیدنی دارند، برخی فقط جواب می‌خواهند. هر الگو در پایین برای سناریوی خاصی بهینه شده — و کنترل استدلال GPT-5.2 تفاوت‌ها را برجسته‌تر می‌کند.

<img src="../../../translated_images/fa/eight-patterns.fa1ebfdf16f71e9a.webp" alt="هشت الگوی پرامپت" width="800"/>

*نمای کلی هشت الگوی مهندسی پرامپت و موارد استفاده آن‌ها*

<img src="../../../translated_images/fa/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="کنترل استدلال با GPT-5.2" width="800"/>

*کنترل استدلال GPT-5.2 به شما اجازه می‌دهد مشخص کنید مدل چقدر باید فکر کند — از پاسخ‌های سریع و مستقیم تا اکتشاف عمیق*

**انگیزه کم (سریع و متمرکز)** - برای سوالات ساده که پاسخ سریع و مستقیم می‌خواهید. مدل حداقل استدلال انجام می‌دهد - حداکثر ۲ مرحله. این را برای محاسبات، جستجوها یا سوالات ساده استفاده کنید.

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

> 💡 **کاوش با GitHub Copilot:** فایل [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) را باز کنید و بپرسید:
> - "تفاوت بین الگوهای انگیزه کم و انگیزه زیاد در پرامپت‌ها چیست؟"
> - "چگونه تگ‌های XML در پرامپت‌ها به ساختار پاسخ هوش مصنوعی کمک می‌کنند؟"
> - "کی باید از الگوهای خودبازتابی استفاده کنم و کی از دستور مستقیم؟"

**انگیزه زیاد (عمیق و کامل)** - برای مسائل پیچیده که تحلیل جامع می‌خواهید. مدل به طور کامل بررسی می‌کند و استدلال مفصل نشان می‌دهد. این را برای طراحی سیستم، تصمیمات معماری یا تحقیق پیچیده استفاده کنید.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**اجرای کار (پیشرفت گام به گام)** - برای جریان‌های کاری چند مرحله‌ای. مدل برنامه‌ای مقدماتی ارائه می‌دهد، هر گام را در حین کار توضیح می‌دهد، سپس خلاصه‌ای ارائه می‌کند. این را برای مهاجرت‌ها، پیاده‌سازی‌ها یا هر فرایند چند مرحله‌ای استفاده کنید.

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

پرامپت زنجیره تفکر از مدل صریحاً می‌خواهد روند استدلالش را نشان دهد، که دقت در کارهای پیچیده را افزایش می‌دهد. تجزیه گام به گام به انسان‌ها و هوش مصنوعی کمک می‌کند منطق را درک کنند.

> **🤖 با [GitHub Copilot](https://github.com/features/copilot) Chat امتحان کنید:** درباره این الگو بپرسید:
> - "چگونه الگوی اجرای کار را برای عملیات بلندمدت سازگار کنم؟"
> - "بهترین روش‌ها برای ساختار دادن مقدمه ابزارها در برنامه‌های تولیدی چیست؟"
> - "چگونه می‌توانم به‌روزرسانی‌های پیشرفت میانی را در رابط کاربری ثبت و نمایش دهم؟"

<img src="../../../translated_images/fa/task-execution-pattern.9da3967750ab5c1e.webp" alt="الگوی اجرای کار" width="800"/>

*جریان کار برنامه‌ریزی → اجرا → خلاصه برای کارهای چند مرحله‌ای*

**کد خودبازتابی** - برای تولید کد با کیفیت تولید. مدل کد مطابق استانداردهای تولید با مدیریت خطای مناسب تولید می‌کند. این هنگام ساخت ویژگی‌ها یا سرویس‌های جدید استفاده می‌شود.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fa/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="چرخه خودبازتابی" width="800"/>

*چرخه بهبود تکراری - تولید، ارزیابی، شناسایی مشکلات، بهبود، تکرار*

**تحلیل ساختاریافته** - برای ارزیابی سازگار. مدل کد را با استفاده از چارچوب ثابت (درستی، شیوه‌ها، کارایی، امنیت، نگهداری) بررسی می‌کند. این را برای بازبینی کد یا ارزشیابی کیفیت استفاده کنید.

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
> - "بهترین روش برای تجزیه و واکنش به خروجی ساختاریافته برنامه‌نویسی شده چیست؟"
> - "چگونه اطمینان حاصل کنم سطح شدت در جلسات بازبینی مختلف همسان باشد؟"

<img src="../../../translated_images/fa/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="الگوی تحلیل ساختاریافته" width="800"/>

*چارچوب برای بازبینی کد سازگار با سطوح شدت*

**چت چند مرحله‌ای** - برای گفتگوهایی که به زمینه نیاز دارند. مدل پیام‌های قبلی را به خاطر می‌سپارد و روی آن‌ها بنا می‌کند. این را برای جلسات کمک تعاملی یا سوال و جواب پیچیده استفاده کنید.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/fa/context-memory.dff30ad9fa78832a.webp" alt="حافظه زمینه‌ای" width="800"/>

*چگونه زمینه مکالمه در چند مرحله تجمع می‌یابد تا رسیدن به حد توکن*

**استدلال گام به گام** - برای مسائلی که نیاز به منطق قابل مشاهده دارند. مدل استدلال صریح برای هر مرحله نشان می‌دهد. این برای مسائل ریاضی، معماهای منطقی یا وقتی که می‌خواهید فرایند تفکر را بفهمید کاربرد دارد.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fa/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="الگوی گام به گام" width="800"/>

*شکستن مسائل به گام‌های منطقی صریح*

**خروجی محدود** - برای پاسخ‌هایی با الزامات فرمت مشخص. مدل دقیقاً فرمت و قواعد طول را دنبال می‌کند. این را برای خلاصه‌ها یا وقتی ساختار خروجی دقیق لازم است استفاده کنید.

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

<img src="../../../translated_images/fa/constrained-output-pattern.0ce39a682a6795c2.webp" alt="الگوی خروجی محدود" width="800"/>

*اجرای الزامات خاص فرمت، طول و ساختار*

## استفاده از منابع موجود Azure

**تأیید استقرار:**

اطمینان حاصل کنید فایل `.env` در شاخه ریشه با اطلاعات ورود Azure وجود دارد (ایجاد شده در ماژول ۰۱):
```bash
cat ../.env  # باید AZURE_OPENAI_ENDPOINT، API_KEY، DEPLOYMENT را نمایش دهد
```

**شروع برنامه:**

> **توجه:** اگر قبلاً همه برنامه‌ها را با `./start-all.sh` از ماژول ۰۱ اجرا کرده‌اید، این ماژول همین حالا روی پورت ۸۰۸۳ در حال اجرا است. می‌توانید فرمان‌های شروع زیر را رد کنید و مستقیماً به http://localhost:8083 بروید.

**گزینه ۱: استفاده از Spring Boot Dashboard (توصیه شده برای کاربران VS Code)**
کانتینر توسعه شامل افزونه Spring Boot Dashboard است که یک رابط بصری برای مدیریت تمام برنامه‌های Spring Boot فراهم می‌کند. می‌توانید آن را در نوار فعالیت در سمت چپ VS Code پیدا کنید (نماد Spring Boot را جستجو کنید).

از طریق Spring Boot Dashboard، می‌توانید:
- تمام برنامه‌های Spring Boot موجود در فضای کاری را ببینید
- برنامه‌ها را با یک کلیک شروع/متوقف کنید
- لاگ‌های برنامه را به صورت هم‌زمان مشاهده کنید
- وضعیت برنامه را نظارت کنید

فقط روی دکمه پخش کنار "prompt-engineering" کلیک کنید تا این ماژول را شروع کنید یا همه ماژول‌ها را همزمان اجرا کنید.

<img src="../../../translated_images/fa/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**گزینه ۲: استفاده از اسکریپت‌های شل**

همه برنامه‌های وب (ماژول‌های ۰۱-۰۴) را اجرا کنید:

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

یا فقط این ماژول را اجرا کنید:

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

هر دو اسکریپت به صورت خودکار متغیرهای محیطی را از فایل `.env` ریشه بارگذاری می‌کنند و اگر فایل‌های JAR وجود نداشته باشند، آن‌ها را می‌سازند.

> **توجه:** اگر ترجیح می‌دهید همه ماژول‌ها را به صورت دستی قبل از شروع بسازید:
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

مرورگر خود را باز کرده و وارد http://localhost:8083 شوید.

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

## اسکرین‌شات‌های برنامه

<img src="../../../translated_images/fa/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*داشبورد اصلی که هر ۸ الگوی مهندسی پرامپت را با خصوصیات و کاربردهای آن‌ها نشان می‌دهد*

## بررسی الگوها

رابط وب به شما امکان می‌دهد با استراتژی‌های مختلف پرامپت آزمایش کنید. هر الگو مشکلات مختلفی را حل می‌کند — آن‌ها را امتحان کنید تا ببینید هر روش در چه مواردی بهتر عمل می‌کند.

> **توجه: استریمینگ در مقابل غیر استریمینگ** — هر صفحه الگو دو دکمه ارائه می‌دهد: **🔴 پاسخ استریم (زنده)** و گزینه **غیر استریمینگ**. استریمینگ از رویدادهای ارسال شده توسط سرور (SSE) استفاده می‌کند تا توکن‌ها را به هنگام تولید مدل به صورت زنده نمایش دهد، بنابراین پیشرفت به سرعت نشان داده می‌شود. گزینه غیر استریمینگ منتظر پاسخ کامل می‌ماند تا آن را نمایش دهد. برای پرامپت‌هایی که سبب استدلال عمیق می‌شوند (مثلاً High Eagerness، Self-Reflecting Code)، فراخوانی غیر استریمینگ ممکن است زمان زیادی - گاهی دقیقه‌ها - طول بکشد بدون هیچ بازخورد قابل مشاهده. **هنگام آزمایش پرامپت‌های پیچیده از استریمینگ استفاده کنید** تا بتوانید مدل را در حال کار ببینید و از تصور پایان زمان درخواست جلوگیری کنید.
>
> **توجه: نیازمندی مرورگر** — ویژگی استریمینگ از Fetch Streams API (`response.body.getReader()`) استفاده می‌کند که یک مرورگر کامل (Chrome، Edge، Firefox، Safari) لازم دارد. این قابلیت در مرورگر ساده تعبیه‌شده VS Code کار نمی‌کند، زیرا وب‌ویو آن از ReadableStream API پشتیبانی نمی‌کند. اگر از مرورگر ساده استفاده کنید، دکمه‌های غیر استریمینگ به طور معمول کار خواهند کرد — فقط دکمه‌های استریمینگ تحت تأثیر قرار می‌گیرند. برای تجربه کامل مرورگر را باز کنید: `http://localhost:8083`.

### کم‌انگیزه در مقابل پرانگیزه

یک سؤال ساده مثل "۱۵٪ از ۲۰۰ چقدر است؟" را با کم‌انگیزه بپرسید. پاسخ فوری و مستقیم دریافت می‌کنید. حالا یک سؤال پیچیده مثل "یک استراتژی کشینگ برای یک API پردازش‌گر پر ترافیک طراحی کن" را با پرانگیزه بپرسید. روی **🔴 پاسخ استریم (زنده)** کلیک کنید و منطق دقیق مدل را توکن به توکن مشاهده کنید. مدل و ساختار سؤال یکسان است - اما پرامپت تعیین می‌کند چقدر باید فکر کند.

### اجرای وظایف (مقدمات ابزار)

فرآیندهای چند مرحله‌ای از برنامه‌ریزی و روایت پیشرفت بهره می‌برند. مدل اقدامات خود را فهرست می‌کند، هر مرحله را شرح می‌دهد و سپس نتایج را خلاصه می‌کند.

### کد خودبازتابی

"یک سرویس اعتبارسنجی ایمیل بساز" را امتحان کنید. به جای اینکه فقط کد را تولید کند و متوقف شود، مدل کد را تولید، بر اساس معیارهای کیفیت ارزیابی، نقاط ضعف را شناسایی و بهبود می‌بخشد. مشاهده خواهید کرد که مدل چند بار تکرار می‌کند تا کد به استانداردهای تولید برسد.

### تحلیل ساختاری

بازبینی کد نیاز به چارچوب‌های ارزیابی سازگار دارد. مدل کد را بر اساس دسته‌بندی‌های ثابت (درستی، روش‌ها، عملکرد، امنیت) با سطوح شدت تحلیل می‌کند.

### گفتگوی چندمرحله‌ای

بپرسید "Spring Boot چیست؟" سپس بلافاصله دنبال کنید با "یک مثال نشان بده". مدل سؤال اول شما را به یاد می‌آورد و به طور خاص یک مثال Spring Boot ارائه می‌دهد. بدون حافظه، سؤال دوم خیلی مبهم خواهد بود.

### استدلال گام به گام

یک مسئله ریاضی انتخاب کنید و آن را با استدلال گام به گام و کم‌انگیزه امتحان کنید. کم‌انگیزه فقط پاسخ را می‌دهد - سریع اما مبهم. استدلال گام به گام هر محاسبه و تصمیم را به شما نشان می‌دهد.

### خروجی محدود

وقتی نیاز به فرمت‌ها یا تعداد کلمات خاص دارید، این الگو رعایت دقیق را اجباری می‌کند. سعی کنید یک خلاصه با دقیقاً ۱۰۰ کلمه در قالب نکات گلوله‌ای تولید کنید.

## آنچه واقعاً یاد می‌گیرید

**تلاش در استدلال همه چیز را تغییر می‌دهد**

GPT-5.2 به شما امکان می‌دهد میزان تلاش محاسباتی را از طریق پرامپت‌ها کنترل کنید. تلاش کم یعنی پاسخ‌های سریع با حداقل جستجو. تلاش بالا یعنی مدل زمان می‌گذارد تا عمیق فکر کند. شما یاد می‌گیرید تلاش را با پیچیدگی کار هماهنگ کنید - وقت را برای سوالات ساده هدر ندهید، اما در تصمیمات پیچیده هم عجله نکنید.

**ساختار رفتار را راهنمایی می‌کند**

توجه کرده‌اید تگ‌های XML در پرامپت‌ها هستند؟ این‌ها تزئینی نیستند. مدل‌ها دستورالعمل‌های ساختاریافته را بسیار پایدارتر از متن آزاد دنبال می‌کنند. وقتی فرآیندهای چندمرحله‌ای یا منطق پیچیده لازم است، ساختار به مدل کمک می‌کند موقعیت و مراحل بعدی را دنبال کند.

<img src="../../../translated_images/fa/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*ساختار یک پرامپت خوب با بخش‌های واضح و ترتیب‌بندی به سبک XML*

**کیفیت از طریق خودارزیابی**

الگوهای خودبازتابی با ارائه معیارهای کیفیت به صورت واضح کار می‌کنند. به جای اینکه امیدوار باشید مدل "درست انجام دهد"، دقیقاً می‌گویید "درست" یعنی چه: منطق صحیح، مدیریت خطا، عملکرد، امنیت. سپس مدل می‌تواند خروجی خودش را ارزیابی و بهبود دهد. این فرآیند تولید کد را از یک بخت‌آزمایی به یک روند تبدیل می‌کند.

**محدودیت زمینه**

مکالمات چند مرحله‌ای با اضافه کردن تاریخچه پیام به هر درخواست کار می‌کنند. اما یک محدودیت وجود دارد - هر مدل تعداد توکن حداکثر دارد. با افزایش مکالمات، باید استراتژی‌هایی برای حفظ زمینه مرتبط بدون رسیدن به سقف به کار برید. این ماژول نحوه کار حافظه را نشان می‌دهد؛ بعداً خواهید آموخت چه زمانی خلاصه کنید، چه زمانی فراموش کنید و چه زمانی بازیابی کنید.

## مراحل بعدی

**ماژول بعدی:** [03-rag - RAG (تولید تقویت شده با بازیابی)](../03-rag/README.md)

---

**مسیر:** [← قبلی: ماژول ۰۱ - مقدمه](../01-introduction/README.md) | [بازگشت به اصلی](../README.md) | [بعدی: ماژول ۰۳ - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**سلب مسئولیت**:  
این سند با استفاده از سرویس ترجمه هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما در تلاش برای دقت هستیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است حاوی اشتباهات یا نواقصی باشند. سند اصلی به زبان اصلی آن باید به عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حیاتی، ترجمه حرفه‌ای انسانی توصیه می‌شود. ما مسئول هیچ گونه سوءتفاهم یا تفسیر نادرستی که ناشی از استفاده از این ترجمه باشد، نیستیم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->