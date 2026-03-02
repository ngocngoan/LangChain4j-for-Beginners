# ماژول ۰۲: مهندسی پرامپت با GPT-5.2

## فهرست مطالب

- [مرور ویدیو](../../../02-prompt-engineering)
- [آنچه خواهید آموخت](../../../02-prompt-engineering)
- [پیش‌نیازها](../../../02-prompt-engineering)
- [درک مهندسی پرامپت](../../../02-prompt-engineering)
- [مبانی مهندسی پرامپت](../../../02-prompt-engineering)
  - [پرامپت بدون نمونه (Zero-Shot)](../../../02-prompt-engineering)
  - [پرامپت با نمونه‌های محدود (Few-Shot)](../../../02-prompt-engineering)
  - [زنجیره فکری (Chain of Thought)](../../../02-prompt-engineering)
  - [پرامپت مبتنی بر نقش](../../../02-prompt-engineering)
  - [قالب‌های پرامپت](../../../02-prompt-engineering)
- [الگوهای پیشرفته](../../../02-prompt-engineering)
- [اجرای برنامه](../../../02-prompt-engineering)
- [اسکرین‌شات‌های برنامه](../../../02-prompt-engineering)
- [کاوش الگوها](../../../02-prompt-engineering)
  - [تمایل کم در مقابل تمایل زیاد](../../../02-prompt-engineering)
  - [اجرای کار (پیش‌گفتارهای ابزار)](../../../02-prompt-engineering)
  - [کد خودبازتابی](../../../02-prompt-engineering)
  - [تحلیل ساخت‌یافته](../../../02-prompt-engineering)
  - [چت چندمرحله‌ای](../../../02-prompt-engineering)
  - [استدلال گام‌به‌گام](../../../02-prompt-engineering)
  - [خروجی محدود شده](../../../02-prompt-engineering)
- [آنچه واقعاً می‌آموزید](../../../02-prompt-engineering)
- [مرحله بعدی](../../../02-prompt-engineering)

## مرور ویدیو

این جلسه زنده را تماشا کنید که توضیح می‌دهد چگونه با این ماژول شروع کنید:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="مهندسی پرامپت با LangChain4j - جلسه زنده" width="800"/></a>

## آنچه خواهید آموخت

نمودار زیر نمای کلی از موضوعات و مهارت‌های کلیدی که در این ماژول توسعه خواهید داد را نشان می‌دهد — از تکنیک‌های بهبود پرامپت تا جریان کاری گام‌به‌گام که دنبال خواهید کرد.

<img src="../../../translated_images/fa/what-youll-learn.c68269ac048503b2.webp" alt="آنچه خواهید آموخت" width="800"/>

در ماژول‌های قبلی، تعاملات پایه LangChain4j با مدل‌های GitHub را بررسی کردید و دیدید که چگونه حافظه AI گفتگو محور با Azure OpenAI را امکان‌پذیر می‌کند. حالا تمرکز ما روی نحوه پرسیدن سوالات — خود پرامپت‌ها — با استفاده از GPT-5.2 Azure OpenAI است. نحوه ساختاردهی پرامپت‌ها تأثیر چشمگیری بر کیفیت پاسخ‌هایی که دریافت می‌کنید دارد. ابتدا تکنیک‌های بنیادی پرامپت را مرور می‌کنیم، سپس به هشت الگوی پیشرفته می‌پردازیم که از قابلیت‌های GPT-5.2 به‌طور کامل بهره‌مند می‌شوند.

ما از GPT-5.2 استفاده می‌کنیم چون کنترل استدلال را معرفی می‌کند - می‌توانید به مدل بگویید چقدر باید قبل از پاسخ فکر کند. این باعث می‌شود استراتژی‌های مختلف پرامپت واضح‌تر شده و به شما کمک می‌کند بفهمید کِی و چگونه از هر روش استفاده کنید. همچنین از محدودیت نرخ کمتر Azure برای GPT-5.2 نسبت به مدل‌های GitHub بهره‌مند می‌شویم.

## پیش‌نیازها

- تکمیل ماژول ۰۱ (منابع Azure OpenAI مستقر شده)
- فایل `.env` در شاخه ریشه با اعتبارنامه‌های Azure (ساخته شده توسط دستور `azd up` در ماژول ۰۱)

> **توجه:** اگر ماژول ۰۱ را تکمیل نکرده‌اید، ابتدا دستورالعمل‌های استقرار آن را دنبال کنید.

## درک مهندسی پرامپت

در اصل، مهندسی پرامپت تفاوت بین دستورهای مبهم و دقیق است، همان‌طور که مقایسه زیر نشان می‌دهد.

<img src="../../../translated_images/fa/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="مهندسی پرامپت چیست؟" width="800"/>

مهندسی پرامپت مربوط به طراحی متنی است که به طور مداوم نتایجی را که نیاز دارید به شما بدهد. فقط پرسیدن سوال نیست — بلکه ساختار دادن درخواست‌هاست به طوری که مدل دقیقاً بفهمد چه می‌خواهید و چگونه آن را ارائه دهد.

این را مانند دادن دستور به یک همکار در نظر بگیرید. "رفع اشکال کن" گنگ است. "رفع خطای null pointer در UserService.java خط ۴۵ با افزودن بررسی null" دقیق است. مدل‌های زبانی نیز همین‌طور کار می‌کنند — دقت و ساختار اهمیت دارد.

نمودار زیر نشان می‌دهد که چگونه LangChain4j در این تصویر جای می‌گیرد — الگوهای پرامپت شما را از طریق واحدهای ساختاری SystemMessage و UserMessage به مدل متصل می‌کند.

<img src="../../../translated_images/fa/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="چگونگی جایگیری LangChain4j" width="800"/>

LangChain4j زیرساخت‌ها را فراهم می‌کند — اتصال به مدل‌ها، حافظه، و انواع پیام‌ها — در حالی که الگوهای پرامپت صرفاً متن‌های ساختارمندی هستند که از طریق این زیرساخت ارسال می‌شوند. واحد‌های سازنده کلیدی `SystemMessage` (که رفتار و نقش AI را مشخص می‌کند) و `UserMessage` (که درخواست واقعی شما را حمل می‌کند) هستند.

## مبانی مهندسی پرامپت

پنج تکنیک اصلی زیر پایه‌های مهندسی پرامپت مؤثر را تشکیل می‌دهند. هر یک جنبه‌ای متفاوت از چگونگی ارتباط شما با مدل‌های زبانی را پوشش می‌دهند.

<img src="../../../translated_images/fa/five-patterns-overview.160f35045ffd2a94.webp" alt="مرور پنج الگوی مهندسی پرامپت" width="800"/>

قبل از پرداختن به الگوهای پیشرفته در این ماژول، بیایید پنج تکنیک بنیادی پرامپت را مرور کنیم. این‌ها بلوک‌های ساختاری هستند که هر مهندس پرامپت باید بشناسد. اگر قبلاً ماژول [شروع سریع](../00-quick-start/README.md#2-prompt-patterns) را کار کرده‌اید، این‌ها را در عمل دیده‌اید — در اینجا چارچوب مفهومی پشت آن‌ها آورده شده است.

### پرامپت بدون نمونه (Zero-Shot Prompting)

ساده‌ترین روش: به مدل دستوری مستقیم بدون هیچ مثالی بدهید. مدل به طور کامل بر آموزش خود تکیه می‌کند تا وظیفه را بفهمد و اجرا کند. این برای درخواست‌های ساده که رفتار مورد انتظار واضح است، خوب کار می‌کند.

<img src="../../../translated_images/fa/zero-shot-prompting.7abc24228be84e6c.webp" alt="پرامپت بدون نمونه" width="800"/>

*دستور مستقیم بدون نمونه — مدل تنها از دستور، وظیفه را استنتاج می‌کند*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// پاسخ: «مثبت»
```

**زمان استفاده:** طبقه‌بندی‌های ساده، سوالات مستقیم، ترجمه‌ها یا هر وظیفه‌ای که مدل بدون راهنمایی بیشتر می‌تواند انجام دهد.

### پرامپت با نمونه‌های محدود (Few-Shot Prompting)

نمونه‌هایی ارائه دهید که الگویی که می‌خواهید مدل دنبال کند را نشان می‌دهد. مدل قالب ورودی-خروجی مورد انتظار را از نمونه‌های شما یاد می‌گیرد و آن را روی ورودی‌های جدید اعمال می‌کند. این باعث افزایش ثبات در وظایفی می‌شود که قالب یا رفتار مطلوب واضح نیست.

<img src="../../../translated_images/fa/few-shot-prompting.9d9eace1da88989a.webp" alt="پرامپت با نمونه محدود" width="800"/>

*یادگیری از نمونه‌ها — مدل الگو را تشخیص داده و روی ورودی‌های جدید اعمال می‌کند*

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

**زمان استفاده:** طبقه‌بندی‌های سفارشی، قالب‌دهی یکنواخت، وظایف حوزه‌-محور یا هنگامی که نتایج zero-shot ناپایدار هستند.

### زنجیره فکری (Chain of Thought)

از مدل بخواهید که استدلال خود را گام‌به‌گام نشان دهد. به جای پریدن مستقیم به پاسخ، مدل مسئله را تفکیک کرده و هر بخش را به صورت صریح بررسی می‌کند. این دقت در مسائل ریاضی، منطق، و استدلال چندمرحله‌ای را ارتقا می‌دهد.

<img src="../../../translated_images/fa/chain-of-thought.5cff6630e2657e2a.webp" alt="پرامپت با زنجیره فکری" width="800"/>

*استدلال گام‌به‌گام — تقسیم مسائل پیچیده به مراحل منطقی صریح*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// مدل نشان می‌دهد: ۷ = ۸ - ۱۵، سپس ۱۹ = ۱۲ + ۷ سیب‌ها
```

**زمان استفاده:** مسائل ریاضی، معماهای منطقی، اشکال‌زدایی، یا هر وظیفه‌ای که نشان دادن روند استدلال دقت و اعتماد را بهبود می‌بخشد.

### پرامپت مبتنی بر نقش

قبل از پرسیدن سوال، شخصیتی یا نقشی برای AI تعریف کنید. این زمینه‌ای فراهم می‌کند که لحن، عمق، و تمرکز پاسخ را شکل می‌دهد. «معمار نرم‌افزار» مشاوره متفاوتی نسبت به «توسعه‌دهنده تازه‌کار» یا «بازرس امنیت» می‌دهد.

<img src="../../../translated_images/fa/role-based-prompting.a806e1a73de6e3a4.webp" alt="پرامپت مبتنی بر نقش" width="800"/>

*تنظیم زمینه و نقش — همان سوال با توجه به نقش تعیین‌شده پاسخ متفاوت می‌گیرد*

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

**زمان استفاده:** بررسی کدها، آموزش، تحلیل‌های حوزه‌محور یا زمانی که به پاسخ‌های متناسب با سطح تخصص یا دیدگاه خاص نیاز دارید.

### قالب‌های پرامپت

پرامپت‌های قابل استفاده مجدد با جایگزین‌هایی برای متغیرها بسازید. به جای نوشتن پرامپت جدید هر بار، یک قالب یک‌بار تعریف کنید و مقادیر متفاوت پر کنید. کلاس `PromptTemplate` در LangChain4j این کار را با نحو `{{variable}}` آسان می‌کند.

<img src="../../../translated_images/fa/prompt-templates.14bfc37d45f1a933.webp" alt="قالب‌های پرامپت" width="800"/>

*پرامپت‌های قابل استفاده مجدد با جایگزین‌های متغیر — یک قالب، چندین کاربرد*

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

**زمان استفاده:** پرسش‌های تکراری با ورودی‌های متفاوت، پردازش دسته‌ای، ساخت جریان‌های کاری AI قابل استفاده مجدد، یا هر موقعیتی که ساختار پرامپت ثابت و داده‌ها متغیر باشند.

---

این پنج مبنا ابزار قابل اعتمادی را برای اکثر وظایف پرامپت در اختیار شما می‌گذارند. بقیه این ماژول بر اساس آن‌ها هشت الگوی پیشرفته را ارائه می‌دهد که کنترل استدلال، خودارزیابی، و قابلیت‌های خروجی ساخت‌یافته GPT-5.2 را به کار می‌گیرند.

## الگوهای پیشرفته

پس از پوشش مبانی، به هشت الگوی پیشرفته می‌رسیم که این ماژول را منحصر به فرد می‌کنند. همه مشکلات نیاز به همان روش ندارند. برخی سوالات پاسخ‌های سریع می‌خواهند، برخی دیگر تفکر عمیق. برخی نیاز به نمایش استدلال دارند، برخی فقط به نتایج نیازمندند. هر الگو برای سناریوی متفاوتی بهینه شده — و کنترل استدلال GPT-5.2 تفاوت‌ها را برجسته‌تر می‌کند.

<img src="../../../translated_images/fa/eight-patterns.fa1ebfdf16f71e9a.webp" alt="هشت الگوی مهندسی پرامپت" width="800"/>

*مروری بر هشت الگوی مهندسی پرامپت و کاربردهای آن‌ها*

GPT-5.2 بعد جدیدی به این الگوها اضافه می‌کند: *کنترل استدلال*. اسلایدر زیر نشان می‌دهد چطور می‌توانید میزان تلاش فکری مدل را تنظیم کنید — از پاسخ‌های سریع و مستقیم تا تحلیل عمیق و کامل.

<img src="../../../translated_images/fa/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="کنترل استدلال با GPT-5.2" width="800"/>

*کنترل استدلال GPT-5.2 به شما اجازه می‌دهد تعیین کنید مدل چقدر باید فکر کند — از پاسخ‌های سریع و مستقیم تا کاوش عمیق*

**تمایل کم (سریع و متمرکز)** - برای سوالات ساده که می‌خواهید پاسخ‌های سریع و مستقیم بگیرید. مدل حداقل استدلال می‌کند - حداکثر ۲ مرحله. این را برای محاسبات، جستجوها، یا سوالات ساده استفاده کنید.

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
> - "فرق الگوهای تمایل کم و تمایل زیاد در پرامپت چیست؟"
> - "چگونه برچسب‌های XML در پرامپت به ساختار پاسخ AI کمک می‌کنند؟"
> - "چه زمانی باید از الگوهای خودبازتابی استفاده کنم و چه زمانی از دستور مستقیم؟"

**تمایل زیاد (عمیق و دقیق)** - برای مشکلات پیچیده که نیاز به تحلیل جامع دارید. مدل عمیق کاوش کرده و استدلال دقیق نشان می‌دهد. این را برای طراحی سیستم، تصمیمات معماری، یا تحقیقات پیچیده استفاده کنید.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**اجرای کار (پیشرفت گام‌به‌گام)** - برای جریان‌های کاری چندمرحله‌ای. مدل یک برنامه اولیه ارائه می‌دهد، هر گام را هنگام اجرا روایت می‌کند، سپس خلاصه‌ای می‌دهد. این را برای مهاجرت‌ها، پیاده‌سازی‌ها، یا هر فرایند چندمرحله‌ای استفاده کنید.

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

پرامپت زنجیره فکری از مدل می‌خواهد روند استدلال خود را آشکار کند که دقت را برای وظایف پیچیده افزایش می‌دهد. تقسیم‌بندی گام‌به‌گام باعث می‌شود هم انسان‌ها و هم AI منطق را بهتر درک کنند.

> **🤖 با چت [GitHub Copilot](https://github.com/features/copilot) امتحان کنید:** درباره این الگو بپرسید:
> - "چگونه الگوی اجرای کار را برای عملیات طولانی‌مدت سازگار کنم؟"
> - "بهترین روش ساختاردهی پیش‌گفتارهای ابزار در برنامه‌های تولیدی چیست؟"
> - "چگونه پیشرفت میانی را در UI ضبط و نمایش دهم؟"

نمودار زیر جریان کار برنامه‌ریزی → اجرا → خلاصه را نشان می‌دهد.

<img src="../../../translated_images/fa/task-execution-pattern.9da3967750ab5c1e.webp" alt="الگوی اجرای کار" width="800"/>

*جریان برنامه‌ریزی → اجرا → خلاصه برای وظایف چندمرحله‌ای*

**کد خودبازتابی** - برای تولید کد با کیفیت تولید. مدل کدی تولید می‌کند که استانداردهای تولید، از جمله مدیریت خطا، را رعایت می‌کند. هنگام ساخت ویژگی‌ها یا سرویس‌های جدید از این استفاده کنید.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

نمودار زیر این چرخه بهبود تکراری را نمایش می‌دهد — تولید کد، ارزیابی، شناسایی ضعف‌ها، و اصلاح تا زمانی که کد به استاندارد تولید برسد.

<img src="../../../translated_images/fa/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="چرخه خودبازتابی" width="800"/>

*چرخه بهبود تکراری - تولید، ارزیابی، شناسایی مشکلات، بهبود، تکرار*

**تحلیل ساخت‌یافته** - برای ارزیابی یکنواخت. مدل کد را با چارچوبی ثابت بررسی می‌کند (درستی، شیوه‌ها، عملکرد، امنیت، قابلیت نگهداری). این را برای بررسی کد یا ارزیابی کیفیت استفاده کنید.

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

> **🤖 با چت [GitHub Copilot](https://github.com/features/copilot) امتحان کنید:** درباره تحلیل ساخت‌یافته بپرسید:
> - "چگونه چارچوب تحلیل را برای انواع بررسی کد متفاوت سفارشی کنم؟"
> - "بهترین روش برای تجزیه و استفاده از خروجی ساختاریافته به صورت برنامه‌نویسی چیست؟"
> - "چگونه سطوح شدت را در جلسات بررسی مختلف به طور یکنواخت تضمین کنم؟"

نمودار زیر نشان می‌دهد چگونه این چارچوب ساخت‌یافته یک بررسی کد را به دسته‌های سازگار با سطوح شدت تقسیم می‌کند.

<img src="../../../translated_images/fa/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="الگوی تحلیل ساخت‌یافته" width="800"/>

*چارچوب برای بررسی کد یکنواخت با سطوح شدت*

**چت چندمرحله‌ای** - برای گفتگوهایی که نیاز به زمینه دارند. مدل پیام‌های قبلی را به یاد می‌آورد و بر آن‌ها بنا می‌کند. این را برای جلسات کمک تعاملی یا پرسش و پاسخ‌های پیچیده استفاده کنید.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

نمودار زیر نشان می‌دهد چگونه زمینه گفتگو با هر نوبت جمع می‌شود و چطور با محدودیت توکن مدل مرتبط است.

<img src="../../../translated_images/fa/context-memory.dff30ad9fa78832a.webp" alt="حافظه زمینه" width="800"/>

*چگونه زمینه گفتگو در چند نوبت تجمع می‌یابد تا به حد توکن برسد*
**دلیل‌یابی مرحله‌به‌مرحله** - برای مسائلی که منطق قابل مشاهده نیاز دارند. مدل برای هر مرحله دلیل‌یابی صریح ارائه می‌دهد. از این برای مسائل ریاضی، معماهای منطقی یا زمانی که نیاز به درک فرآیند فکری دارید استفاده کنید.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

نمودار زیر نشان می‌دهد چگونه مدل مسائل را به مراحل منطقی شماره‌گذاری‌شده و صریح تقسیم می‌کند.

<img src="../../../translated_images/fa/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="الگوی مرحله‌به‌مرحله" width="800"/>

*تقسیم مسائل به مراحل منطقی صریح*

**خروجی محدود شده** - برای پاسخ‌هایی با الزامات قالب‌بندی خاص. مدل به‌دقت قوانین قالب و طول را رعایت می‌کند. از این برای خلاصه‌ها یا زمانی که به ساختار دقیق خروجی نیاز دارید استفاده کنید.

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

نمودار زیر نشان می‌دهد چگونه محدودیت‌ها مدل را هدایت می‌کنند تا خروجی مطابق با قالب و الزامات طول ارائه دهد.

<img src="../../../translated_images/fa/constrained-output-pattern.0ce39a682a6795c2.webp" alt="الگوی خروجی محدود شده" width="800"/>

*اجرای الزامات خاص قالب، طول و ساختار*

## اجرای برنامه

**بررسی استقرار:**

اطمینان حاصل کنید فایل `.env` در شاخه ریشه با اطلاعات اعتبارنامه Azure وجود دارد (ایجاد شده در طول ماژول ۰۱). این را از دایرکتوری ماژول (`02-prompt-engineering/`) اجرا کنید:

**Bash:**
```bash
cat ../.env  # باید AZURE_OPENAI_ENDPOINT، API_KEY، DEPLOYMENT را نمایش دهد
```

**PowerShell:**
```powershell
Get-Content ..\.env  # باید AZURE_OPENAI_ENDPOINT، API_KEY، DEPLOYMENT را نشان دهد
```

**راه‌اندازی برنامه:**

> **توجه:** اگر قبلاً همه برنامه‌ها را با استفاده از `./start-all.sh` از دایرکتوری ریشه (همانطور که در ماژول ۰۱ توضیح داده شده) شروع کرده‌اید، این ماژول قبلاً روی پورت ۸۰۸۳ در حال اجرا است. می‌توانید دستورات شروع زیر را رد کنید و مستقیماً به http://localhost:8083 بروید.

**گزینه ۱: استفاده از Spring Boot Dashboard (توصیه شده برای کاربران VS Code)**

کانتینر توسعه شامل افزونه Spring Boot Dashboard است که رابط کاربری بصری برای مدیریت همه برنامه‌های Spring Boot فراهم می‌کند. می‌توانید آن را در نوار فعالیت در سمت چپ VS Code (به دنبال آیکون Spring Boot) پیدا کنید.

از طریق Spring Boot Dashboard می‌توانید:
- همه برنامه‌های Spring Boot موجود در فضای کاری را مشاهده کنید
- برنامه‌ها را با یک کلیک شروع/توقف کنید
- لاگ‌های برنامه را به‌صورت زنده ببینید
- وضعیت برنامه را مانیتور کنید

فقط روی دکمه پخش کنار "prompt-engineering" کلیک کنید تا این ماژول شروع شود، یا همه ماژول‌ها را یکجا شروع کنید.

<img src="../../../translated_images/fa/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*داشبورد Spring Boot در VS Code — شروع، توقف و مدیریت همه ماژول‌ها از یک محل*

**گزینه ۲: استفاده از اسکریپت‌های شل**

تمام برنامه‌های وب (ماژول‌های ۰۱ تا ۰۴) را شروع کنید:

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

هر دو اسکریپت به‌طور خودکار متغیرهای محیطی را از فایل `.env` ریشه بارگذاری می‌کنند و در صورت عدم وجود JAR ها را می‌سازند.

> **توجه:** اگر می‌خواهید قبل از شروع همه ماژول‌ها را دستی بسازید:
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

مرورگر خود را باز کرده و به http://localhost:8083 بروید.

**برای توقف برنامه:**

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

در اینجا رابط اصلی ماژول prompt engineering است که می‌توانید همزمان با هر هشت الگو آزمایش کنید.

<img src="../../../translated_images/fa/dashboard-home.5444dbda4bc1f79d.webp" alt="داشبورد اصلی" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*داشبورد اصلی که هر ۸ الگوی مهندسی درخواست را با ویژگی‌ها و موارد کاربرد آن‌ها نشان می‌دهد*

## کاوش الگوها

رابط وب به شما اجازه می‌دهد تا با استراتژی‌های مختلف درخواست آزمایش کنید. هر الگو مسائل متفاوتی را حل می‌کند — آن‌ها را امتحان کنید تا ببینید هر رویکرد چه زمانی اثر می‌کند.

> **توجه: پخش زنده در مقایسه با غیرپخش زنده** — هر صفحه الگو دو دکمه دارد: **🔴 پاسخ زنده (Stream Response)** و گزینه **غیرپخش زنده**. پخش زنده از Server-Sent Events (SSE) برای نمایش لحظه‌ای توکن‌ها همزمان با تولید مدل استفاده می‌کند، بنابراین پیشرفت بلافاصله قابل مشاهده است. گزینه غیرپخش زنده منتظر می‌ماند تا پاسخ کامل تولید شود قبل از نمایش. برای درخواست‌هایی که استدلال عمیق لازم دارند (مثل High Eagerness، Self-Reflecting Code)، تماس غیرپخش زنده ممکن است بسیار طولانی شود — گاهی حتی دقیقه‌ها — بدون بازخورد بصری. **هنگام آزمایش با درخواست‌های پیچیده از پخش زنده استفاده کنید** تا مدل را در حال کار ببینید و از تصور تایم‌اوت درخواست جلوگیری کنید.
>
> **توجه: نیازمندی مرورگر** — ویژگی پخش زنده از Fetch Streams API (`response.body.getReader()`) استفاده می‌کند که نیاز به مرورگر کامل (Chrome, Edge, Firefox, Safari) دارد. این در Simple Browser داخلی VS Code کار نمی‌کند زیرا وب‌ویو آن از ReadableStream API پشتیبانی نمی‌کند. اگر از Simple Browser استفاده کنید، دکمه‌های غیرپخش زنده به‌طور معمول کار می‌کنند — فقط دکمه‌های پخش زنده تحت تاثیر هستند. برای تجربه کامل `http://localhost:8083` را در مرورگر خارجی باز کنید.

### Eagerness کم در مقابل زیاد

یک سوال ساده مثل "۱۵٪ از ۲۰۰ چقدر است؟" با Eagerness کم بپرسید. پاسخ فوری و مستقیم دریافت می‌کنید. حالا یک سوال پیچیده مانند "طراحی استراتژی کشینگ برای یک API پرترافیک" با Eagerness زیاد بپرسید. روی **🔴 پاسخ زنده (Stream Response)** کلیک کنید و مشاهدۀ دلیل‌یابی دقیق مدل را توکن‌به‌توکن ببینید. مدل همان است، ساختار سوال همان است — اما درخواست نشان می‌دهد چقدر باید فکر کند.

### اجرای وظایف (پیش‌گفتار ابزار)

گردش‌های کار چندمرحله‌ای از برنامه‌ریزی اولیه و روایت پیشرفت بهره می‌برند. مدل توضیح می‌دهد چه می‌کند، هر مرحله را روایت می‌کند و سپس نتایج را خلاصه می‌کند.

### کد خودبازتابی

مثلاً "ایجاد سرویس اعتبارسنجی ایمیل" را امتحان کنید. به جای فقط تولید کد و توقف، مدل کد را تولید، بر اساس معیارهای کیفیت ارزیابی، نقاط ضعف را شناسایی و بهبود می‌دهد. می‌بینید که تا رسیدن به استانداردهای تولید چندین بار تکرار می‌کند.

### تحلیل ساختاریافته

بازبینی کد به چارچوب‌های ارزیابی ثابت نیاز دارد. مدل کد را با استفاده از دسته‌بندی‌های ثابت (درستی، روش‌ها، عملکرد، امنیت) با سطوح شدت تحلیل می‌کند.

### چت چندمرحله‌ای

سوال "Spring Boot چیست؟" را بپرسید، سپس بلافاصله ادامه دهید با "یک مثال نشان بده". مدل سوال اول شما را به خاطر می‌سپارد و یک مثال Spring Boot خاص ارائه می‌دهد. بدون حافظه، سوال دوم خیلی مبهم بود.

### دلیل‌یابی مرحله‌به‌مرحله

یک مسئله ریاضی انتخاب کنید و آن را هم با دلیل‌یابی مرحله به مرحله و هم با Eagerness کم امتحان کنید. Eagerness کم فقط پاسخ را می‌دهد — سریع اما مبهم. حالت مرحله به مرحله هر محاسبه و تصمیم را نشان می‌دهد.

### خروجی محدود شده

وقتی به فرمت خاص یا تعداد دقیق کلمات نیاز دارید، این الگو پیروی دقیق را تضمین می‌کند. مثلا تولید خلاصه‌ای با دقیقاً ۱۰۰ کلمه در قالب فهرست‌وار.

## چیزی که واقعاً یاد می‌گیرید

**تلاش استدلال همه چیز را تغییر می‌دهد**

GPT-5.2 به شما امکان کنترل تلاش محاسباتی را از طریق درخواست‌های خود می‌دهد. تلاش کم به پاسخ‌های سریع با حداقل بررسی منجر می‌شود. تلاش زیاد یعنی مدل زمان می‌گذارد تا عمیق فکر کند. شما یاد می‌گیرید میزان تلاش را متناسب با پیچیدگی وظیفه تطبیق دهید — زمان خود را برای سوالات ساده تلف نکنید، اما تصمیم‌های پیچیده را نیز شتابزده نگیرید.

**ساختار رفتار را هدایت می‌کند**

توجه کنید به تگ‌های XML در درخواست‌ها؟ آن‌ها فقط تزئینی نیستند. مدل‌ها دستورالعمل‌های ساختاریافته را پایدارتر از متن آزاد دنبال می‌کنند. وقتی به فرآیندهای چندمرحله‌ای یا منطق پیچیده نیاز دارید، ساختار به مدل کمک می‌کند جایگاه خود را پیگیری کند و بداند مرحله بعدی چیست. نمودار زیر یک درخواست ساختاریافته را باز می‌کند و نشان می‌دهد چگونه تگ‌هایی مانند `<system>`, `<instructions>`, `<context>`, `<user-input>`, و `<constraints>` دستورالعمل‌ها را به بخش‌های واضح تقسیم می‌کنند.

<img src="../../../translated_images/fa/prompt-structure.a77763d63f4e2f89.webp" alt="ساختار درخواست" width="800"/>

*آناتومی یک درخواست خوب ساختار یافته با بخش‌های واضح و سازمان‌دهی به سبک XML*

**کیفیت از طریق خودارزیابی**

الگوهای خودبازتابی با بیان صریح معیارهای کیفیت کار می‌کنند. به جای امید به درست انجام دادن مدل، به آن دقیقاً می‌گویید “درست” یعنی چه: منطق صحیح، مدیریت خطا، عملکرد، امنیت. سپس مدل می‌تواند خروجی خود را ارزیابی و بهبود دهد. این تولید کد را از شانس به یک فرایند تبدیل می‌کند.

**زمینه محدود است**

گفتگوهای چندمرحله‌ای با افزودن تاریخچه پیام‌ها به هر درخواست کار می‌کنند. اما محدودیت وجود دارد — هر مدل حداکثر تعداد توکن دارد. با رشد گفتگوها، شما به استراتژی‌هایی نیاز دارید که زمینه مرتبط را نگه دارند بدون اینکه به آن سقف برسند. این ماژول به شما نشان می‌دهد حافظه چگونه کار می‌کند؛ بعداً خواهید آموخت چه زمانی خلاصه کنید، چه زمانی فراموش کنید و چه زمانی بازیابی کنید.

## گام‌های بعدی

**ماژول بعدی:** [03-rag - RAG (تولید افزوده شده بازیابی)](../03-rag/README.md)

---

**ناوبری:** [← قبلی: ماژول ۰۱ - مقدمه](../01-introduction/README.md) | [بازگشت به اصلی](../README.md) | [بعدی: ماژول ۰۳ - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**سلب مسئولیت**:
این سند با استفاده از سرویس ترجمه هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما در تلاش برای دقت هستیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است حاوی اشکالات یا نادرستی‌هایی باشند. سند اصلی به زبان بومی خود باید به عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حیاتی، توصیه می‌شود از ترجمه حرفه‌ای انسانی استفاده شود. ما در قبال هرگونه سوءتفاهم یا برداشت غلط ناشی از استفاده از این ترجمه مسئولیتی نمی‌پذیریم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->