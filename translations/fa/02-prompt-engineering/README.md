# ماژول ۰۲: مهندسی پرامپت با GPT-5.2

## فهرست مطالب

- [آنچه خواهید آموخت](../../../02-prompt-engineering)
- [پیش‌نیازها](../../../02-prompt-engineering)
- [درک مهندسی پرامپت](../../../02-prompt-engineering)
- [اصول مهندسی پرامپت](../../../02-prompt-engineering)
  - [پرامپت صفر-شات](../../../02-prompt-engineering)
  - [پرامپت چند-شات](../../../02-prompt-engineering)
  - [زنجیره تفکر](../../../02-prompt-engineering)
  - [پرامپت مبتنی بر نقش](../../../02-prompt-engineering)
  - [الگوهای پرامپت](../../../02-prompt-engineering)
- [الگوهای پیشرفته](../../../02-prompt-engineering)
- [استفاده از منابع موجود Azure](../../../02-prompt-engineering)
- [تصاویر برنامه](../../../02-prompt-engineering)
- [کاوش الگوها](../../../02-prompt-engineering)
  - [انگیزش کم در مقابل انگیزش زیاد](../../../02-prompt-engineering)
  - [اجرای وظیفه (مقدمه ابزارها)](../../../02-prompt-engineering)
  - [کد خودبازتابی](../../../02-prompt-engineering)
  - [تحلیل ساختاری](../../../02-prompt-engineering)
  - [چت چند نوبتی](../../../02-prompt-engineering)
  - [استدلال گام به گام](../../../02-prompt-engineering)
  - [خروجی محدودشده](../../../02-prompt-engineering)
- [آنچه واقعاً می‌آموزید](../../../02-prompt-engineering)
- [گام‌های بعدی](../../../02-prompt-engineering)

## آنچه خواهید آموخت

<img src="../../../translated_images/fa/what-youll-learn.c68269ac048503b2.webp" alt="آنچه خواهید آموخت" width="800"/>

در ماژول قبلی، دیدید که چگونه حافظه امکان گفتگو با هوش مصنوعی را فراهم می‌کند و از مدل‌های گیت‌هاب برای تعاملات پایه استفاده کردید. اکنون تمرکز ما بر نحوه پرسیدن سوال‌ها — خود پرامپت‌ها — با استفاده از GPT-5.2 از Azure OpenAI است. شیوه ساختاردهی پرامپت‌ها تأثیر قابل توجهی بر کیفیت پاسخ‌هایی که دریافت می‌کنید دارد. ابتدا مرور می‌کنیم بر تکنیک‌های پایه پرامپت‌نویسی، سپس به هشت الگوی پیشرفته می‌پردازیم که از قابلیت‌های GPT-5.2 به طور کامل بهره می‌برند.

ما از GPT-5.2 استفاده می‌کنیم زیرا امکان کنترل استدلال را فراهم می‌کند - می‌توانید به مدل بگویید چقدر باید قبل از پاسخ فکر کند. این امر استراتژی‌های مختلف پرامپت‌نویسی را واضح‌تر می‌کند و به شما کمک می‌کند بفهمید هر روش را چه زمانی به کار ببرید. همچنین از محدودیت‌های کمتر نرخ Azure برای GPT-5.2 نسبت به مدل‌های گیت‌هاب بهره‌مند می‌شویم.

## پیش‌نیازها

- اتمام ماژول ۰۱ (استقرار منابع Azure OpenAI)
- فایل `.env` در دایرکتوری ریشه با اعتبارنامه‌های Azure (ساخته شده توسط `azd up` در ماژول ۰۱)

> **توجه:** اگر ماژول ۰۱ را تکمیل نکرده‌اید، ابتدا دستورالعمل‌های استقرار آنجا را دنبال کنید.

## درک مهندسی پرامپت

<img src="../../../translated_images/fa/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="مهندسی پرامپت چیست؟" width="800"/>

مهندسی پرامپت در مورد طراحی متنی ورودی است که به طور مداوم نتایج مورد نیاز شما را ارائه دهد. فقط پرسیدن سوال نیست — بلکه ساختاردهی درخواست‌ها طوری است که مدل دقیقاً بفهمد شما چه می‌خواهید و چگونه باید ارائه دهد.

آن را مثل دادن دستورالعمل به یک همکار در نظر بگیرید. «رفع باگ» مبهم است. «رفع استثنای اشاره‌گر تهی در UserService.java خط ۴۵ با اضافه کردن بررسی تهی» دقیق است. مدل‌های زبانی به همین صورت عمل می‌کنند — دقت و ساختار اهمیت دارند.

<img src="../../../translated_images/fa/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="چگونه LangChain4j جای می‌گیرد" width="800"/>

LangChain4j زیرساخت را فراهم می‌کند — اتصالات مدل، حافظه و انواع پیام‌ها — در حالی که الگوهای پرامپت فقط متن‌های ساختارمند دقیقی هستند که از طریق آن زیرساخت ارسال می‌کنید. بلوک‌های اصلی ساختار `SystemMessage` (که رفتار و نقش هوش مصنوعی را تعیین می‌کند) و `UserMessage` (که درخواست واقعی شما را حمل می‌کند) هستند.

## اصول مهندسی پرامپت

<img src="../../../translated_images/fa/five-patterns-overview.160f35045ffd2a94.webp" alt="مروری بر پنج الگوی مهندسی پرامپت" width="800"/>

قبل از ورود به الگوهای پیشرفته در این ماژول، بیایید پنج تکنیک بنیادین پرامپت‌نویسی را مرور کنیم. این‌ها بلوک‌های ساختاری هستند که هر مهندس پرامپت باید بداند. اگر قبلاً از طریق [ماژول شروع سریع](../00-quick-start/README.md#2-prompt-patterns) کار کرده‌اید، این‌ها را در عمل دیده‌اید — در اینجا چارچوب مفهومی پشت آنها آمده است.

### پرامپت صفر-شات

ساده‌ترین رویکرد: به مدل یک دستور مستقیم بدون نمونه بدهید. مدل کاملاً بر آموزش خود تکیه می‌کند تا وظیفه را بفهمد و اجرا کند. این برای درخواست‌های ساده که رفتار مورد انتظار مشخص است، خوب عمل می‌کند.

<img src="../../../translated_images/fa/zero-shot-prompting.7abc24228be84e6c.webp" alt="پرامپت صفر-شات" width="800"/>

*دستور مستقیم بدون نمونه — مدل تنها از روی دستور، وظیفه را استنباط می‌کند*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// پاسخ: "مثبت"
```

**چه زمانی استفاده شود:** دسته‌بندی‌های ساده، سوالات مستقیم، ترجمه‌ها یا هر کاری که مدل بتواند بدون راهنمایی اضافی انجام دهد.

### پرامپت چند-شات

مثال‌هایی ارائه دهید که الگوی مورد نظر را به مدل نشان دهند. مدل فرمت ورودی-خروجی مورد انتظار را از مثال‌های شما یاد می‌گیرد و آن را به ورودی‌های جدید اعمال می‌کند. این باعث بهبود چشمگیر ثبات برای کارهایی می‌شود که فرمت یا رفتار مطلوب واضح نیست.

<img src="../../../translated_images/fa/few-shot-prompting.9d9eace1da88989a.webp" alt="پرامپت چند-شات" width="800"/>

*یادگیری از مثال‌ها — مدل الگو را شناسایی کرده و به ورودی‌های جدید اعمال می‌کند*

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

**چه زمانی استفاده شود:** دسته‌بندی‌های سفارشی، قالب‌بندی ثابت، کارهای حوزه خاص، یا وقتی نتایج صفر-شات نامنسجم است.

### زنجیره تفکر

از مدل بخواهید استدلال خود را مرحله به مرحله نشان دهد. به جای اینکه مستقیم به پاسخ برسد، مدل مسئله را تجزیه می‌کند و هر قسمت را به طور صریح بررسی می‌کند. این دقت را در ریاضی، منطق و استدلال‌های چند مرحله‌ای بهبود می‌بخشد.

<img src="../../../translated_images/fa/chain-of-thought.5cff6630e2657e2a.webp" alt="زنجیره تفکر" width="800"/>

*استدلال گام‌به‌گام — شکستن مسائل پیچیده به مراحل منطقی صریح*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// مدل نشان می‌دهد: ۱۵ - ۸ = ۷، سپس ۷ + ۱۲ = ۱۹ سیب
```

**چه زمانی استفاده شود:** مسائل ریاضی، معماهای منطقی، رفع اشکال، یا هر کاری که نشان دادن فرآیند استدلال دقت و اعتماد را افزایش می‌دهد.

### پرامپت مبتنی بر نقش

پیش از پرسیدن سوال، یک شخصیت یا نقش برای هوش مصنوعی تعیین کنید. این زمینه‌ای فراهم می‌کند که لحن، عمق و تمرکز پاسخ را شکل می‌دهد. یک «معمار نرم‌افزار» مشاوره متفاوتی نسبت به «توسعه‌دهنده تازه‌کار» یا «بازرس امنیت» می‌دهد.

<img src="../../../translated_images/fa/role-based-prompting.a806e1a73de6e3a4.webp" alt="پرامپت مبتنی بر نقش" width="800"/>

*تنظیم زمینه و شخصیت — همان سوال پاسخ متفاوتی بر اساس نقش اختصاص داده شده دریافت می‌کند*

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

**چه زمانی استفاده شود:** بررسی کد، تدریس، تحلیل حوزه خاص، یا وقتی به پاسخ‌های متناسب با سطح تخصص یا دیدگاه خاص نیاز دارید.

### الگوهای پرامپت

پرامپت‌های قابل استفاده مجدد با جایگزین‌های متغیر بسازید. به جای نوشتن پرامپت جدید هر بار، یک الگو تعریف کنید و مقادیر مختلف را پر کنید. کلاس `PromptTemplate` در LangChain4j این کار را با سینتکس `{{variable}}` آسان می‌کند.

<img src="../../../translated_images/fa/prompt-templates.14bfc37d45f1a933.webp" alt="الگوهای پرامپت" width="800"/>

*پرامپت‌های قابل استفاده مجدد با جایگزین‌های متغیر — یک الگو، چند استفاده*

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

**چه زمانی استفاده شود:** پرسش‌های تکراری با ورودی‌های مختلف، پردازش دسته‌ای، ساخت جریان‌های کاری قابل استفاده مجدد هوش مصنوعی، یا هر موقعیتی که ساختار پرامپت ثابت ولی داده‌ها تغییر کنند.

---

این پنج اصل به شما ابزار قدرتمندی برای اغلب کارهای پرامپت‌نویسی می‌دهند. باقی این ماژول بر اساس آنها با **هشت الگوی پیشرفته** ساخته شده است که کنترل استدلال، خودارزیابی و قابلیت‌های خروجی ساختاری GPT-5.2 را به کار می‌گیرند.

## الگوهای پیشرفته

پس از پوشش اصول پایه، به هشت الگوی پیشرفته می‌رویم که این ماژول را منحصر به فرد می‌سازند. همه مسائل به یک روش نیاز ندارند. برخی سوالات پاسخ سریع می‌خواهند، برخی تفکر عمیق. برخی به استدلال قابل مشاهده نیاز دارند، برخی فقط به نتایج. هر الگو در زیر برای سناریوی متفاوتی بهینه شده است — و کنترل استدلال GPT-5.2 تفاوت‌ها را بیشتر آشکار می‌کند.

<img src="../../../translated_images/fa/eight-patterns.fa1ebfdf16f71e9a.webp" alt="هشت الگوی پرامپت‌نویسی" width="800"/>

*مروری بر هشت الگوی مهندسی پرامپت و موارد استفاده آن‌ها*

<img src="../../../translated_images/fa/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="کنترل استدلال با GPT-5.2" width="800"/>

*کنترل استدلال GPT-5.2 به شما امکان می‌دهد مشخص کنید مدل چقدر باید فکر کند — از پاسخ‌های سریع و مستقیم تا کاوش عمیق*

**انگیزش کم (سریع و متمرکز)** - برای سوالات ساده که پاسخ سریع و مستقیم می‌خواهید. مدل حداقل استدلال را انجام می‌دهد - حداکثر ۲ مرحله. این را برای محاسبات، جستجوها یا سوالات ساده استفاده کنید.

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

> 💡 **با GitHub Copilot کاوش کنید:** فایل [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) را باز کنید و بپرسید:
> - "تفاوت بین الگوهای انگیزش کم و انگیزش زیاد در پرامپت‌نویسی چیست؟"
> - "چگونه تگ‌های XML در پرامپت‌ها به ساختاردهی پاسخ هوش مصنوعی کمک می‌کنند؟"
> - "چه زمانی باید از الگوهای خودبازتابی استفاده کنم در مقابل دستور مستقیم؟"

**انگیزش زیاد (عمیق و جامع)** - برای مشکلات پیچیده که تحلیل کامل می‌خواهید. مدل به‌طور دقیق کاوش می‌کند و استدلال مفصل ارائه می‌دهد. برای طراحی سیستم، تصمیمات معماری یا تحقیقات پیچیده استفاده کنید.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**اجرای وظیفه (پیشرفت گام‌به‌گام)** - برای جریان‌های کاری چند مرحله‌ای. مدل یک برنامه اولیه ارائه می‌دهد، هر مرحله را به‌تدریج تشریح می‌کند، سپس خلاصه می‌دهد. از این برای مهاجرت‌ها، پیاده‌سازی‌ها یا هر روند چند مرحله‌ای استفاده کنید.

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

پرامپت زنجیره تفکر به طور واضح از مدل می‌خواهد فرآیند استدلال خود را نشان دهد و دقت را برای کارهای پیچیده بهبود می‌بخشد. تجزیه و تحلیل گام به گام به انسان‌ها و هوش مصنوعی کمک می‌کند منطق را بفهمند.

> **🤖 با [چت GitHub Copilot](https://github.com/features/copilot) امتحان کنید:** درباره این الگو بپرسید:
> - "چگونه الگوی اجرای وظیفه را برای عملیات طولانی‌مدت سازگار کنم؟"
> - "بهترین روش‌های ساختاردهی مقدمه ابزارها در برنامه‌های تولیدی چیست؟"
> - "چگونه می‌توانم پیشرفت‌های میانی را در UI ثبت و نمایش دهم؟"

<img src="../../../translated_images/fa/task-execution-pattern.9da3967750ab5c1e.webp" alt="الگوی اجرای وظیفه" width="800"/>

*برنامه‌ریزی → اجرا → خلاصه‌سازی برای کارهای چند مرحله‌ای*

**کد خودبازتابی** - برای تولید کد با کیفیت تولید. مدل کدی تولید می‌کند که استانداردهای تولید را رعایت کرده و مدیریت خطاهای مناسب دارد. وقتی ویژگی یا سرویس جدید می‌سازید این را استفاده کنید.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fa/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="چرخه خودبازتابی" width="800"/>

*حلقه بهبود تکراری — تولید، ارزیابی، شناسایی مشکلات، بهبود، تکرار*

**تحلیل ساختاری** - برای ارزیابی منظم. مدل کد را با چارچوب ثابتی بازبینی می‌کند (درستی، شیوه‌ها، عملکرد، امنیت، قابلیت نگهداری). این را برای بررسی کد یا ارزیابی کیفیت استفاده کنید.

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

> **🤖 با [چت GitHub Copilot](https://github.com/features/copilot) امتحان کنید:** درباره تحلیل ساختاری بپرسید:
> - "چگونه چارچوب تحلیل را برای انواع مختلف بررسی‌های کد سفارشی کنم؟"
> - "بهترین راه برای برنامه‌نویسی گرفتن و استفاده از خروجی ساختاری چیست؟"
> - "چگونه اطمینان دهم که سطوح شدت در جلسات بازبینی مختلف یکسان باشد؟"

<img src="../../../translated_images/fa/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="الگوی تحلیل ساختاری" width="800"/>

*چارچوبی برای بازبینی کد با ثبات و سطح شدت*

**چت چند نوبتی** - برای گفتگوهایی که نیاز به زمینه دارند. مدل پیام‌های قبلی را به یاد می‌آورد و بر آن‌ها ساخته می‌شود. این را برای جلسات کمک تعاملی یا پرسش و پاسخ پیچیده استفاده کنید.

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

*چگونگی تجمع زمینه گفتگو در چند نوبت تا رسیدن به حد توکن*

**استدلال گام‌به‌گام** - برای مشکلاتی که به منطق قابل مشاهده نیاز دارند. مدل برای هر مرحله استدلال صریح نشان می‌دهد. این را برای مسائل ریاضی، معماهای منطقی یا وقتی که باید فرآیند تفکر را بفهمید استفاده کنید.

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

*شکستن مسائل به مراحل منطقی صریح*

**خروجی محدودشده** - برای پاسخ‌هایی با نیازمندی‌های فرمت مشخص. مدل به شدت قوانین فرمت و طول را دنبال می‌کند. این را برای خلاصه‌ها یا وقتی به ساختار خروجی دقیق نیاز دارید استفاده کنید.

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

<img src="../../../translated_images/fa/constrained-output-pattern.0ce39a682a6795c2.webp" alt="الگوی خروجی محدودشده" width="800"/>

*اجرای قوانین فرمت، طول و ساختار مشخص*

## استفاده از منابع موجود Azure

**تأیید استقرار:**

اطمینان حاصل کنید که فایل `.env` در دایرکتوری ریشه با اعتبارنامه‌های Azure وجود دارد (ساخته شده در طول ماژول ۰۱):
```bash
cat ../.env  # باید AZURE_OPENAI_ENDPOINT، API_KEY، DEPLOYMENT را نمایش دهد
```

**شروع برنامه:**

> **توجه:** اگر قبلاً همه برنامه‌ها را با دستور `./start-all.sh` از ماژول ۰۱ اجرا کرده‌اید، این ماژول در پورت ۸۰۸۳ در حال اجرا است. می‌توانید دستورات شروع زیر را رد کنید و مستقیماً به http://localhost:8083 بروید.

**گزینه ۱: استفاده از Spring Boot Dashboard (توصیه شده برای کاربران VS Code)**

کانتینر توسعه شامل افزونه Spring Boot Dashboard است که رابط بصری برای مدیریت همه برنامه‌های Spring Boot ارائه می‌دهد. آن را در نوار فعالیت سمت چپ VS Code (نماد Spring Boot) می‌توانید بیابید.

از Spring Boot Dashboard می‌توانید:
- همه برنامه‌های Spring Boot موجود در فضای کاری را ببینید
- با یک کلیک برنامه‌ها را شروع/متوقف کنید
- لاگ‌های برنامه را به صورت زنده مشاهده کنید
- وضعیت برنامه را مانیتور کنید
به سادگی دکمه پخش کنار "prompt-engineering" را کلیک کنید تا این ماژول شروع شود، یا همه ماژول‌ها را هم‌زمان اجرا کنید.

<img src="../../../translated_images/fa/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**گزینه ۲: استفاده از اسکریپت‌های شل**

شروع همه برنامه‌های وب (ماژول‌های ۰۱-۰۴):

**بش:**
```bash
cd ..  # از پوشه ریشه
./start-all.sh
```

**پاورشل:**
```powershell
cd ..  # از پوشهٔ ریشه
.\start-all.ps1
```

یا تنها این ماژول را شروع کنید:

**بش:**
```bash
cd 02-prompt-engineering
./start.sh
```

**پاورشل:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

هر دو اسکریپت به طور خودکار متغیرهای محیطی را از فایل .env ریشه بارگذاری می‌کنند و اگر فایل‌های JAR وجود نداشته باشند، آن‌ها را می‌سازند.

> **نکته:** اگر ترجیح می‌دهید همه ماژول‌ها را به صورت دستی قبل از شروع بسازید:
>
> **بش:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **پاورشل:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

آدرس http://localhost:8083 را در مرورگر خود باز کنید.

**برای توقف:**

**بش:**
```bash
./stop.sh  # فقط این ماژول
# یا
cd .. && ./stop-all.sh  # همه ماژول‌ها
```

**پاورشل:**
```powershell
.\stop.ps1  # فقط این ماژول
# یا
cd ..; .\stop-all.ps1  # همه ماژول‌ها
```

## تصاویر برنامه

<img src="../../../translated_images/fa/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*داشبورد اصلی که همه ۸ الگوی مهندسی پرامپت را با ویژگی‌ها و موارد استفاده‌شان نشان می‌دهد*

## بررسی الگوها

رابط وب به شما امکان می‌دهد با استراتژی‌های مختلف پرامپت آزمایش کنید. هر الگو مسئله‌ای متفاوت را حل می‌کند - آن‌ها را امتحان کنید تا ببینید هر روش کی بهتر عمل می‌کند.

### تمایل کم در مقابل تمایل زیاد

یک سؤال ساده مثل "۱۵٪ از ۲۰۰ چند است؟" را با تمایل کم بپرسید. پاسخ سریع و مستقیم دریافت خواهید کرد. حالا یک سؤال پیچیده مثل "طراحی استراتژی کشینگ برای یک API پر ترافیک" را با تمایل زیاد بپرسید. ببینید مدل چطور کند می‌شود و استدلال دقیق ارائه می‌دهد. مدل یکسان، ساختار سؤال یکسان - ولی پرامپت به مدل می‌گوید چقدر فکر کند.

<img src="../../../translated_images/fa/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*محاسبه سریع با حداقل استدلال*

<img src="../../../translated_images/fa/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*استراتژی کشینگ جامع (۲.۸MB)*

### اجرای وظیفه (پیش‌نوشت‌های ابزار)

جریان‌های کاری چندمرحله‌ای از برنامه‌ریزی و روایت پیشرفت بهره می‌برند. مدل شرح می‌دهد که چه خواهد کرد، هر مرحله را روایت می‌کند، سپس نتایج را خلاصه می‌کند.

<img src="../../../translated_images/fa/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*ایجاد یک نقطه پایانی REST با روایت مرحله به مرحله (۳.۹MB)*

### کد خودبازتابانه

عبارت "ساخت سرویس اعتبارسنجی ایمیل" را امتحان کنید. به جای فقط تولید کد و توقف، مدل کد می‌سازد، بر اساس معیارهای کیفیت ارزیابی می‌کند، ضعف‌ها را شناسایی و بهبود می‌بخشد. خواهید دید که مدل تکرار می‌کند تا کد به استانداردهای تولید برسد.

<img src="../../../translated_images/fa/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*سرویس کامل اعتبارسنجی ایمیل (۵.۲MB)*

### تحلیل ساختاریافته

بازبینی‌های کد به چارچوب‌های ارزیابی منسجم نیاز دارند. مدل کد را بر اساس دسته‌بندی‌های ثابت (درستی، شیوه‌ها، عملکرد، امنیت) با سطوح شدت مختلف تحلیل می‌کند.

<img src="../../../translated_images/fa/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*بازبینی کد مبتنی بر چارچوب*

### گفتگوی چند مرحله‌ای

سؤال "Spring Boot چیست؟" را بپرسید و بلافاصله دنبال آن "یک مثال نشان بده" را مطرح کنید. مدل سؤال اول شما را به یاد دارد و مثال مربوط به Spring Boot را به طور مشخص می‌دهد. بدون حافظه، سؤال دوم خیلی مبهم بود.

<img src="../../../translated_images/fa/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*حفظ زمینه سؤال‌ها*

### استدلال مرحله به مرحله

یک مسئله ریاضی انتخاب کنید و آن را هم با استدلال مرحله به مرحله و هم با تمایل کم امتحان کنید. تمایل کم فقط پاسخ را سریع می‌دهد - اما مبهم است. مرحله به مرحله هر محاسبه و تصمیم را نشان می‌دهد.

<img src="../../../translated_images/fa/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*مسئله ریاضی با مراحل واضح*

### خروجی محدود

وقتی به فرمت یا تعداد کلمات مشخص نیاز دارید، این الگو رعایت دقیق را الزام می‌کند. تولید یک خلاصه با دقیقاً ۱۰۰ کلمه به صورت بولت پوینت را امتحان کنید.

<img src="../../../translated_images/fa/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*خلاصه یادگیری ماشین با کنترل فرمت*

## آنچه واقعاً یاد می‌گیرید

**تلاش استدلال همه چیز را تغییر می‌دهد**

GPT-5.2 به شما اجازه می‌دهد میزان تلاش محاسباتی را از طریق پرامپت‌های خود کنترل کنید. تلاش کم به معنی پاسخ‌های سریع با حداقل اکتشاف است. تلاش زیاد یعنی مدل زمان می‌گذارد تا عمیق فکر کند. شما یاد می‌گیرید تلاش را با پیچیدگی کار تطبیق دهید - زمان را روی سوالات ساده تلف نکنید، اما در تصمیمات پیچیده نیز عجله نکنید.

**ساختار رفتار را هدایت می‌کند**

به تگ‌های XML در پرامپت‌ها دقت کنید؟ این‌ها تزئینی نیستند. مدل‌ها دستورالعمل‌های ساختاریافته را بهتر از متن آزاد دنبال می‌کنند. وقتی به فرآیندهای چندمرحله‌ای یا منطق پیچیده نیاز دارید، ساختار کمک می‌کند مدل بداند کجاست و مرحله بعد چیست.

<img src="../../../translated_images/fa/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*تشریح یک پرامپت ساختاریافته با بخش‌های واضح و سازماندهی به سبک XML*

**کیفیت از طریق خودارزیابی**

الگوهای خودبازتابانه با مشخص کردن معیارهای کیفیت کار می‌کنند. به جای اینکه امیدوار باشید مدل «درست انجام دهد»، دقیقاً می‌گویید «درست» یعنی چه: منطق صحیح، مدیریت خطا، عملکرد، امنیت. مدل می‌تواند خروجی خود را ارزیابی و بهبود دهد. این کدسازی را از قرعه‌کشی به فرایند تبدیل می‌کند.

**زمینه محدود است**

گفتگوهای چند مرحله‌ای با گنجاندن تاریخچه پیام در هر درخواست کار می‌کنند. اما محدودیتی وجود دارد - هر مدل حداکثر تعداد توکن دارد. با افزایش گفتگوها، به استراتژی‌هایی نیاز دارید که زمینه مرتبط را بدون رسیدن به سقف حفظ کنند. این ماژول نشان می‌دهد حافظه چگونه کار می‌کند؛ بعداً یاد می‌گیرید چه زمانی خلاصه کنید، چه زمانی فراموش کنید و چه زمانی بازیابی کنید.

## مراحل بعدی

**ماژول بعدی:** [03-rag - RAG (تولید با بازیابی افزوده)](../03-rag/README.md)

---

**ناوبری:** [← قبلی: ماژول ۰۱ - معرفی](../01-introduction/README.md) | [بازگشت به اصلی](../README.md) | [بعدی: ماژول ۰۳ - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**سلب مسئولیت**:
این سند با استفاده از سرویس ترجمه هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما برای دقت تلاش می‌کنیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است حاوی خطا یا نواقص باشند. سند اصلی به زبان بومی آن باید به عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حیاتی، استفاده از ترجمه حرفه‌ای انسانی توصیه می‌شود. ما مسئول هیچ‌گونه سوء تفاهم یا تفسیر نادرستی که ناشی از استفاده از این ترجمه باشد، نمی‌باشیم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->