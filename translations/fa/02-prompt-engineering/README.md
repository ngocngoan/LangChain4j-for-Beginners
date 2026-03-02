# ماژول ۰۲: مهندسی پرامپت با GPT-5.2

## فهرست مطالب

- [مرور ویدئو](../../../02-prompt-engineering)
- [آنچه خواهید آموخت](../../../02-prompt-engineering)
- [پیش‌نیازها](../../../02-prompt-engineering)
- [درک مهندسی پرامپت](../../../02-prompt-engineering)
- [اصول مهندسی پرامپت](../../../02-prompt-engineering)
  - [پرامپت بدون نمونه](../../../02-prompt-engineering)
  - [پرامپت با چند نمونه](../../../02-prompt-engineering)
  - [زنجیره تفکر](../../../02-prompt-engineering)
  - [پرامپت مبتنی بر نقش](../../../02-prompt-engineering)
  - [الگوهای پرامپت](../../../02-prompt-engineering)
- [الگوهای پیشرفته](../../../02-prompt-engineering)
- [استفاده از منابع موجود Azure](../../../02-prompt-engineering)
- [تصاویر برنامه](../../../02-prompt-engineering)
- [کاوش در الگوها](../../../02-prompt-engineering)
  - [اشتیاق کم در مقابل اشتیاق زیاد](../../../02-prompt-engineering)
  - [اجرای وظایف (مقدمات ابزار)](../../../02-prompt-engineering)
  - [کد خودبازتابی](../../../02-prompt-engineering)
  - [تحلیل ساختاریافته](../../../02-prompt-engineering)
  - [چت چند مرحله‌ای](../../../02-prompt-engineering)
  - [تفکر گام به گام](../../../02-prompt-engineering)
  - [خروجی محدود شده](../../../02-prompt-engineering)
- [واقعاً چه چیزی می‌آموزید](../../../02-prompt-engineering)
- [گام‌های بعدی](../../../02-prompt-engineering)

## مرور ویدئو

این جلسه زنده را ببینید که توضیح می‌دهد چگونه با این ماژول شروع کنید:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="مهندسی پرامپت با LangChain4j - جلسه زنده" width="800"/></a>

## آنچه خواهید آموخت

<img src="../../../translated_images/fa/what-youll-learn.c68269ac048503b2.webp" alt="آنچه خواهید آموخت" width="800"/>

در ماژول قبلی دیدید چگونه حافظه امکان هوش مصنوعی مکالمه‌ای را فراهم می‌کند و از مدل‌های GitHub برای تعاملات پایه استفاده کردید. اکنون تمرکز ما بر چگونگی پرسیدن سوالات — یعنی خود پرامپت‌ها — با استفاده از GPT-5.2 در Azure OpenAI است. نحوه ساختاردهی پرامپت‌ها به شدت بر کیفیت پاسخ‌هایی که دریافت می‌کنید تأثیر می‌گذارد. ابتدا تکنیک‌های پایه پرامپت را مرور می‌کنیم و سپس به هشت الگوی پیشرفته می‌پردازیم که تمام ظرفیت‌های GPT-5.2 را به کار می‌گیرند.

ما از GPT-5.2 استفاده می‌کنیم چون کنترل استدلال را معرفی می‌کند — می‌توانید به مدل بگویید قبل از پاسخ دادن چقدر فکر کند. این باعث می‌شود استراتژی‌های مختلف پرامپت واضح‌تر باشند و به شما کمک می‌کند بدانید چه موقع از هر رویکرد استفاده کنید. همچنین از محدودیت‌های نرخ کمتر Azure برای GPT-5.2 نسبت به مدل‌های GitHub بهره‌مند می‌شویم.

## پیش‌نیازها

- تکمیل ماژول ۰۱ (منابع Azure OpenAI مستقر شده)
- فایل `.env` در دایرکتوری ریشه با اطلاعات اعتبار Azure (ایجاد شده توسط `azd up` در ماژول ۰۱)

> **توجه:** اگر ماژول ۰۱ را کامل نکرده‌اید، ابتدا دستورالعمل‌های استقرار را در آنجا دنبال کنید.

## درک مهندسی پرامپت

<img src="../../../translated_images/fa/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="مهندسی پرامپت چیست؟" width="800"/>

مهندسی پرامپت درباره طراحی متن ورودی است که پیوسته نتایج مورد نیاز شما را به دست می‌دهد. فقط پرسیدن سوال نیست - بلکه ساختاربندی درخواست‌هاست به گونه‌ای که مدل دقیقاً بفهمد چه می‌خواهید و چگونه آن را ارائه دهد.

آن را مانند دادن دستور به یک همکار در نظر بگیرید. "باگ را رفع کن" مبهم است. "خطا در کوچک‌کننده اشاره‌گر تهی در UserService.java خط ۴۵ را با اضافه کردن بررسی تهی برطرف کن" مشخص است. مدل‌های زبانی هم همین‌گونه کار می‌کنند — خاص بودن و ساختار اهمیت دارد.

<img src="../../../translated_images/fa/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="نحوه جایگاه LangChain4j" width="800"/>

LangChain4j زیرساخت — اتصالات مدل، حافظه، و انواع پیام‌ها — را فراهم می‌کند در حالی که الگوهای پرامپت صرفاً متن‌های ساختاربندی شده‌ای هستند که از طریق آن زیرساخت ارسال می‌کنید. بلوک‌های کلیدی `SystemMessage` (که رفتار و نقش هوش مصنوعی را تنظیم می‌کند) و `UserMessage` (که درخواست واقعی شما را حمل می‌کند) هستند.

## اصول مهندسی پرامپت

<img src="../../../translated_images/fa/five-patterns-overview.160f35045ffd2a94.webp" alt="مروری بر پنج الگوی مهندسی پرامپت" width="800"/>

قبل از ورود به الگوهای پیشرفته این ماژول، بیایید پنج تکنیک پایه پرامپت را مرور کنیم. این‌ها بلوک‌های ساختمانی هستند که هر مهندس پرامپت باید بداند. اگر قبلاً ماژول [شروع سریع](../00-quick-start/README.md#2-prompt-patterns) را گذرانده‌اید، این‌ها را در عمل دیده‌اید — اینجا چارچوب مفهومی پشت آن‌هاست.

### پرامپت بدون نمونه

ساده‌ترین روش: به مدل یک دستور مستقیم بدون نمونه بدهید. مدل کاملاً به آموزش خود متکی است تا وظیفه را بفهمد و اجرا کند. این برای درخواست‌های ساده که رفتار مورد انتظار واضح است، خوب عمل می‌کند.

<img src="../../../translated_images/fa/zero-shot-prompting.7abc24228be84e6c.webp" alt="پرامپت بدون نمونه" width="800"/>

*دستور مستقیم بدون نمونه — مدل وظیفه را فقط از دستور استنباط می‌کند*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// پاسخ: «مثبت»
```

**کی استفاده کنیم:** طبقه‌بندی‌های ساده، سوالات مستقیم، ترجمه‌ها یا هر وظیفه‌ای که مدل می‌تواند بدون راهنمایی بیشتر انجام دهد.

### پرامپت با چند نمونه

نمونه‌هایی ارائه دهید که الگوی مورد نظر شما را نشان می‌دهند. مدل قالب ورودی-خروجی مورد انتظار را از نمونه‌ها یاد می‌گیرد و آن را به ورودی‌های جدید تعمیم می‌دهد. این باعث بهبود چشمگیر یکنواختی برای وظایفی می‌شود که الگوی مورد نظر یا رفتار واضح نیست.

<img src="../../../translated_images/fa/few-shot-prompting.9d9eace1da88989a.webp" alt="پرامپت با چند نمونه" width="800"/>

*یادگیری از نمونه‌ها — مدل الگو را شناسایی کرده و روی ورودی‌های جدید اعمال می‌کند*

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

**کی استفاده کنیم:** طبقه‌بندی‌های سفارشی، قالب‌بندی یکنواخت، وظایف خاص دامنه، یا زمانی که نتایج پرامپت بدون نمونه ناپایدار است.

### زنجیره تفکر

از مدل بخواهید استدلال خود را گام به گام نشان دهد. به جای پرش مستقیم به پاسخ، مدل مسئله را تجزیه کرده و هر قسمت را به صراحت کار می‌کند. این دقت را در مسائل ریاضی، منطقی و استدلال چند مرحله‌ای بهبود می‌بخشد.

<img src="../../../translated_images/fa/chain-of-thought.5cff6630e2657e2a.webp" alt="پرامپت زنجیره تفکر" width="800"/>

*تفکر گام به گام — شکستن مسائل پیچیده به مراحل منطقی صریح*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// مدل نشان می‌دهد: ۱۵ - ۸ = ۷، سپس ۷ + ۱۲ = ۱۹ سیب
```

**کی استفاده کنیم:** مسائل ریاضی، معماهای منطقی، اشکال‌زدایی، یا هر وظیفه‌ای که نشان دادن فرآیند استدلال دقت و اعتماد را افزایش می‌دهد.

### پرامپت مبتنی بر نقش

قبل از پرسیدن سوال، شخصیتی یا نقش برای هوش مصنوعی تنظیم کنید. این زمینه‌ای فراهم می‌کند که لحن، عمق و تمرکز پاسخ را شکل می‌دهد. یک "معمار نرم‌افزار" مشاوره‌ای متفاوت از یک "توسعه‌دهنده تازه‌کار" یا "بازرس امنیت" می‌دهد.

<img src="../../../translated_images/fa/role-based-prompting.a806e1a73de6e3a4.webp" alt="پرامپت مبتنی بر نقش" width="800"/>

*تنظیم زمینه و شخصیت — همان سوال پاسخ متفاوتی بسته به نقش اختصاص یافته دریافت می‌کند*

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

**کی استفاده کنیم:** بازبینی کد، آموزش، تحلیل‌های خاص دامنه، یا زمانی که نیاز به پاسخ‌های متناسب با سطح تخصص یا دیدگاه خاص است.

### الگوهای پرامپت

پرامپت‌های قابل استفاده مجدد با جای‌گزین‌های متغیر بسازید. به جای نوشتن یک پرامپت جدید هر بار، یک قالب تعریف کنید و مقادیر مختلف را پر کنید. کلاس `PromptTemplate` در LangChain4j این کار را با نحو `{{variable}}` ساده می‌کند.

<img src="../../../translated_images/fa/prompt-templates.14bfc37d45f1a933.webp" alt="الگوهای پرامپت" width="800"/>

*پرامپت‌های قابل استفاده مجدد با جایگزین‌های متغیر — یک قالب، استفاده‌های متعدد*

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

**کی استفاده کنیم:** پرس‌وجوهای تکراری با ورودی‌های مختلف، پردازش دسته‌ای، ساخت گردش‌کارهای قابل استفاده مجدد هوش مصنوعی، یا هر سناریویی که ساختار پرامپت ثابت اما داده تغییر کند.

---

این پنج اصل به شما مجموعه ابزار محکمی برای بیشتر وظایف پرامپت می‌دهند. بقیه این ماژول بر پایه آن‌ها ساخته شده با **هشت الگوی پیشرفته** که کنترل استدلال، خودارزیابی و قابلیت‌های خروجی ساختاریافته GPT-5.2 را به کار می‌گیرند.

## الگوهای پیشرفته

پس از پوشش اصول، به هشت الگوی پیشرفته می‌رویم که این ماژول را منحصر به فرد می‌کنند. همه مسائل به همان رویکرد نیاز ندارند. برخی سوالات پاسخ‌های سریع می‌خواهند، برخی نیاز به تفکر عمیق دارند. برخی به استدلال قابل مشاهده نیاز دارند، برخی فقط نتایج. هر الگو برای یک سناریوی متفاوت بهینه شده — و کنترل استدلال GPT-5.2 تفاوت‌ها را برجسته‌تر می‌کند.

<img src="../../../translated_images/fa/eight-patterns.fa1ebfdf16f71e9a.webp" alt="هشت الگوی پرامپت" width="800"/>

*مروری بر هشت الگوی مهندسی پرامپت و موارد استفاده آن‌ها*

<img src="../../../translated_images/fa/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="کنترل استدلال با GPT-5.2" width="800"/>

*کنترل استدلال GPT-5.2 به شما اجازه می‌دهد مشخص کنید مدل چقدر باید فکر کند — از پاسخ‌های سریع و مستقیم تا کاوش عمیق*

**اشتیاق کم (سریع و متمرکز)** - برای سوالات ساده که پاسخ‌های سریع و مستقیم می‌خواهید. مدل حداقل استدلال انجام می‌دهد — حداکثر ۲ مرحله. این را برای محاسبات، جستجوها یا سوالات ساده استفاده کنید.

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

> 💡 **کشف با GitHub Copilot:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) را باز کنید و بپرسید:
> - "تفاوت الگوهای پرامپت اشتیاق کم و اشتیاق زیاد چیست؟"
> - "چگونه برچسب‌های XML در پرامپت‌ها به ساختاردهی پاسخ هوش مصنوعی کمک می‌کنند؟"
> - "چه زمانی باید الگوهای خودبازتابی را در مقابل دستور مستقیم استفاده کنم؟"

**اشتیاق زیاد (عمیق و جامع)** - برای مسائل پیچیده که تحلیل جامع می‌خواهید. مدل به طور کامل کاوش می‌کند و استدلال‌های دقیق نشان می‌دهد. این را برای طراحی سیستم، تصمیمات معماری، یا تحقیقات پیچیده استفاده کنید.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**اجرای وظیفه (پیشرفت گام به گام)** - برای جریان‌های کاری چند مرحله‌ای. مدل برنامه‌ای مقدماتی ارائه می‌دهد، هر گام را هنگام انجام شرح می‌دهد، سپس خلاصه‌ای می‌دهد. این را برای مهاجرت‌ها، پیاده‌سازی‌ها یا هر فرایند چند مرحله‌ای استفاده کنید.

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

پرامپت زنجیره تفکر به طور واضح از مدل می‌خواهد فرآیند استدلال خود را نشان دهد و دقت را برای وظایف پیچیده افزایش می‌دهد. تقسیم‌بندی مرحله به مرحله به انسان‌ها و هوش مصنوعی کمک می‌کند منطق را درک کنند.

> **🤖 با [GitHub Copilot](https://github.com/features/copilot) Chat امتحان کنید:** درباره این الگو بپرسید:
> - "چگونه الگوی اجرای وظیفه را برای عملیات طولانی مدت سازگار کنم؟"
> - "بهترین روش‌ها برای ساختاردهی مقدمات ابزار در برنامه‌های تولید چیست؟"
> - "چگونه می‌توانم به‌روزرسانی‌های پیشرفت میانی را در رابط کاربری ضبط و نمایش دهم؟"

<img src="../../../translated_images/fa/task-execution-pattern.9da3967750ab5c1e.webp" alt="الگوی اجرای وظیفه" width="800"/>

*برنامه‌ریزی → اجرا → خلاصه برای وظایف چند مرحله‌ای*

**کد خودبازتابی** - برای تولید کد با کیفیت تولید. مدل کد مطابق استانداردهای تولید با مدیریت خطاهای صحیح تولید می‌کند. این را هنگام ساخت ویژگی‌ها یا سرویس‌های جدید استفاده کنید.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fa/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="چرخه خودبازتابی" width="800"/>

*حلقه بهبود تکراری - تولید، ارزیابی، شناسایی مشکلات، بهبود، تکرار*

**تحلیل ساختاریافته** - برای ارزیابی یکنواخت. مدل کد را با چارچوب ثابت (درستی، شیوه‌ها، کارایی، امنیت، قابلیت نگهداری) مرور می‌کند. این را برای بازبینی کد یا ارزیابی کیفیت استفاده کنید.

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
> - "چگونه می‌توانم چارچوب تحلیل را برای انواع مختلف بازبینی کد سفارشی کنم؟"
> - "بهترین راه برای تجزیه و تحلیل خروجی ساختاریافته به صورت برنامه‌ای چیست؟"
> - "چگونه می‌توانم سطوح شدت را در جلسات بازبینی مختلف یکسان نگه دارم؟"

<img src="../../../translated_images/fa/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="الگوی تحلیل ساختاریافته" width="800"/>

*چارچوبی برای بازبینی‌های کد یکنواخت با سطوح شدت*

**چت چند مرحله‌ای** - برای گفتگوهایی که نیاز به زمینه دارند. مدل پیام‌های قبلی را به یاد می‌آورد و روی آن‌ها می‌سازد. این برای جلسات راهنمایی تعاملی یا سوال و جواب‌های پیچیده استفاده کنید.

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

*چگونه زمینه گفتگو در چند نوبت جمع می‌شود تا به محدودیت توکن می‌رسد*

**تفکر گام به گام** - برای مسائلی که نیاز به منطق قابل مشاهده دارند. مدل استدلال صریح برای هر گام نشان می‌دهد. این را برای مسائل ریاضی، معماهای منطقی، یا زمانی که باید فرآیند تفکر را بفهمید استفاده کنید.

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

*تقسیم مسئله به مراحل منطقی صریح*

**خروجی محدود شده** - برای پاسخ‌هایی با الزامات قالب خاص. مدل به شدت قواعد قالب و طول را رعایت می‌کند. این را برای خلاصه‌ها یا زمانی که به ساختار خروجی دقیق نیاز دارید استفاده کنید.

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

<img src="../../../translated_images/fa/constrained-output-pattern.0ce39a682a6795c2.webp" alt="الگوی خروجی محدود شده" width="800"/>

*اجبار به رعایت الزامات قالب، طول و ساختار خاص*

## استفاده از منابع موجود Azure

**بررسی استقرار:**

اطمینان حاصل کنید فایل `.env` در دایرکتوری ریشه با اطلاعات اعتبار Azure وجود دارد (ایجاد شده در جریان ماژول ۰۱):
```bash
cat ../.env  # باید AZURE_OPENAI_ENDPOINT، API_KEY، DEPLOYMENT را نمایش دهد
```

**شروع برنامه:**

> **توجه:** اگر قبلاً همه برنامه‌ها را با استفاده از `./start-all.sh` از ماژول ۰۱ راه‌اندازی کرده‌اید، این ماژول قبلاً در پورت ۸۰۸۳ در حال اجرا است. می‌توانید فرمان‌های شروع زیر را رد کنید و مستقیماً به http://localhost:8083 بروید.
**گزینه ۱: استفاده از Spring Boot Dashboard (توصیه شده برای کاربران VS Code)**

کانتینر توسعه شامل افزونه Spring Boot Dashboard است که رابط بصری برای مدیریت همه برنامه‌های Spring Boot را فراهم می‌کند. می‌توانید آن را در نوار فعالیت در سمت چپ VS Code پیدا کنید (آیکون Spring Boot را جستجو کنید).

از طریق Spring Boot Dashboard می‌توانید:
- تمام برنامه‌های Spring Boot موجود در فضای کاری را ببینید
- برنامه‌ها را با یک کلیک شروع/متوقف کنید
- گزارش‌های برنامه را به صورت زنده مشاهده کنید
- وضعیت برنامه را مانیتور کنید

کافی است روی دکمه پخش کنار "prompt-engineering" کلیک کنید تا این ماژول اجرا شود، یا همه ماژول‌ها را همزمان شروع کنید.

<img src="../../../translated_images/fa/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**گزینه ۲: استفاده از اسکریپت‌های شل**

شروع همه برنامه‌های وب (ماژول‌های ۰۱-۰۴):

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

یا فقط این ماژول را شروع کنید:

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

هر دو اسکریپت به طور خودکار متغیرهای محیطی را از فایل `.env` ریشه بارگذاری می‌کنند و در صورتی که فایل‌های JAR وجود نداشته باشند، آن‌ها را می‌سازند.

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

مرورگر خود را باز کرده و به آدرس http://localhost:8083 بروید.

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

<img src="../../../translated_images/fa/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*داشبورد اصلی که همه ۸ الگوی مهندسی پرامپت به همراه ویژگی‌ها و موارد استفاده آن‌ها را نشان می‌دهد*

## بررسی الگوها

رابط وب به شما امکان می‌دهد با استراتژی‌های پرامپت متنوع آزمایش کنید. هر الگو مسائل مختلفی را حل می‌کند - آن‌ها را امتحان کنید تا ببینید هر روش در چه زمانی بهترین عملکرد را دارد.

> **توجه: استریمینگ در مقابل غیر استریمینگ** — هر صفحه الگو دو دکمه دارد: **🔴 پاسخ استریم (زنده)** و گزینه **غیر استریمینگ**. در حالت استریم، از رویدادهای ارسال شده از سرور (SSE) استفاده می‌شود تا توکن‌ها به صورت زنده نمایش داده شوند، بنابراین پیشرفت را بلافاصله مشاهده می‌کنید. گزینه غیر استریمینگ منتظر پاسخ کامل است تا آن را نمایش دهد. برای پرامپت‌هایی که تفکر عمیق را برمی‌انگیزند (مثلاً پرامپت‌های با اشتیاق بالا، کد خودبازتابی)، تماس غیر استریمینگ می‌تواند بسیار طولانی شود — گاهی دقیقه‌ها — بدون هیچ بازخورد قابل مشاهده. **هنگام آزمایش پرامپت‌های پیچیده از استریمینگ استفاده کنید** تا مدل در حال کار را ببینید و فکر نکنید درخواست زمان‌بر شده است.
>
> **توجه: نیازمندی مرورگر** — قابلیت استریم از Fetch Streams API (`response.body.getReader()`) استفاده می‌کند که نیاز به مرورگر کامل (Chrome، Edge، Firefox، Safari) دارد. این قابلیت در مرورگر ساده داخلی VS Code کار نمی‌کند، زیرا وب‌ویوی آن API ReadableStream را پشتیبانی نمی‌کند. اگر از مرورگر ساده استفاده کنید، دکمه‌های غیر استریمینگ به طور عادی کار می‌کنند — فقط دکمه‌های استریمینگ تحت تاثیر هستند. برای تجربه کامل به مرورگر خارجی `http://localhost:8083` را باز کنید.

### اشتیاق پایین در مقابل اشتیاق بالا

یک سؤال ساده مثل «۱۵٪ از ۲۰۰ چقدر است؟» با اشتیاق پایین بپرسید. پاسخ سریع و مستقیم دریافت خواهید کرد. حالا یک سؤال پیچیده مثل «طراحی استراتژی کشینگ برای یک API پر ترافیک» با اشتیاق بالا بپرسید. دکمه **🔴 پاسخ استریم (زنده)** را بزنید و دلیل‌یابی مفصل مدل را توکن به توکن مشاهده کنید. مدل همان است، ساختار سؤال یکسان، اما پرامپت تعیین می‌کند چقدر باید فکر کند.

### اجرای وظایف (ابزارهای پیش‌فرض)

رویکردهای چند مرحله‌ای از برنامه‌ریزی اولیه و روایت پیشرفت بهره می‌برند. مدل شرح می‌دهد چه کاری انجام می‌دهد، هر مرحله را روایت می‌کند، و سپس نتایج را خلاصه می‌کند.

### کد خودبازتابی

امتحان کنید: «یک سرویس اعتبارسنجی ایمیل بساز». به جای فقط تولید کد و توقف، مدل کد ایجاد می‌کند، آن را با معیارهای کیفیت ارزیابی می‌کند، نقاط ضعف را شناسایی می‌کند و بهبود می‌دهد. خواهید دید که تا زمانی که کد به استانداردهای تولید برسد، تکرار می‌شود.

### تحلیل ساختاریافته

بررسی کد نیاز به چارچوب‌های ارزیابی ثابت دارد. مدل کد را با دسته‌بندی‌های مشخص (درستی، شیوه‌ها، عملکرد، امنیت) و میزان شدت بررسی می‌کند.

### چت چند دوری

بپرسید «Spring Boot چیست؟» و بلافاصله دنبال آن «یک مثال نشان بده». مدل سؤال اول شما را به خاطر می‌آورد و یک مثال Spring Boot مخصوص می‌دهد. بدون حافظه، سؤال دوم خیلی مبهم می‌شد.

### استدلال گام به گام

یک مسئله ریاضی انتخاب کنید و با هر دو روش استدلال گام به گام و اشتیاق پایین امتحان کنید. اشتیاق پایین فقط پاسخ می‌دهد - سریع اما مبهم. استدلال گام به گام هر محاسبه و تصمیم را نشان می‌دهد.

### خروجی محدود شده

وقتی نیاز به فرمت یا تعداد کلمات خاص دارید، این الگو رعایت دقیق این محدودیت‌ها را تضمین می‌کند. مثلاً خلاصه‌ای با دقیقاً ۱۰۰ کلمه به صورت نکته‌وار تولید کنید.

## آنچه واقعاً یاد می‌گیرید

**تلاش استدلال همه چیز را تغییر می‌دهد**

GPT-5.2 به شما اجازه می‌دهد میزان تلاش محاسباتی را از طریق پرامپت‌ها کنترل کنید. تلاش کم یعنی پاسخ‌های سریع با کاوش محدود. تلاش زیاد یعنی مدل وقت می‌گذارد تا عمیق فکر کند. شما یاد می‌گیرید تلاش را متناسب با پیچیدگی کار تنظیم کنید - وقت را برای سؤالات ساده تلف نکنید، اما در تصمیمات پیچیده عجله نکنید.

**ساختار رفتار را هدایت می‌کند**

توجه کنید برچسب‌های XML در پرامپت‌ها؟ آن‌ها تزئینی نیستند. مدل‌ها دستورالعمل‌های ساختاریافته را بهتر از متن آزاد دنبال می‌کنند. وقتی فرآیندهای چندمرحله‌ای یا منطق پیچیده نیاز دارید، ساختار به مدل کمک می‌کند بداند کجاست و بعد چه باید بکند.

<img src="../../../translated_images/fa/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*تشریح یک پرامپت ساختاریافته خوب با بخش‌های واضح و سازماندهی سبک XML*

**کیفیت از طریق خودارزیابی**

الگوهای خودبازتابی با بیان واضح معیارهای کیفیت کار می‌کنند. به جای اینکه امیدوار باشید مدل «درست انجام دهد»، دقیقاً تعریف می‌کنید «درست» یعنی چه: منطق صحیح، مدیریت خطا، عملکرد، امنیت. مدل می‌تواند خروجی خودش را ارزیابی کند و بهبود دهد. این فرآیند تولید کد را از لاتاری به روند تبدیل می‌کند.

**زمینه محدود است**

گفت‌وگوهای چند دوری با گنجاندن تاریخچه پیام‌ها در هر درخواست کار می‌کند. اما محدودیت وجود دارد - هر مدل تعداد توکن‌های محدودی دارد. با رشد گفت‌وگو، باید استراتژی‌هایی برای حفظ زمینه مرتبط بدون رسیدن به سقف داشته باشید. این ماژول به شما می‌آموزد حافظه چگونه کار می‌کند؛ بعداً یاد می‌گیرید چه زمانی خلاصه کنید، چه زمانی فراموش کنید و چه زمانی بازیابی کنید.

## مراحل بعدی

**ماژول بعدی:** [03-rag - RAG (تولید تقویت‌شده با بازیابی)](../03-rag/README.md)

---

**ناوبری:** [← قبلی: ماژول ۰۱ - معرفی](../01-introduction/README.md) | [بازگشت به اصلی](../README.md) | [بعدی: ماژول ۰۳ - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**سلب مسئولیت**:
این سند با استفاده از سرویس ترجمه هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما در تلاش برای دقت هستیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است حاوی خطاها یا نادرستی‌هایی باشند. سند اصلی به زبان مادری خود باید به عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حیاتی، ترجمه حرفه‌ای انسانی توصیه می‌شود. ما در قبال هرگونه سوءتفاهم یا تفسیر نادرست ناشی از استفاده از این ترجمه مسئولیتی نداریم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->