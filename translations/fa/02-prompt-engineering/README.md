# ماژول ۰۲: مهندسی پرامپت با GPT-5.2

## فهرست مطالب

- [آنچه می‌آموزید](../../../02-prompt-engineering)
- [پیش‌نیازها](../../../02-prompt-engineering)
- [درک مهندسی پرامپت](../../../02-prompt-engineering)
- [مبانی مهندسی پرامپت](../../../02-prompt-engineering)
  - [پرامپت بدون نمونه (Zero-Shot)](../../../02-prompt-engineering)
  - [پرامپت با چند نمونه (Few-Shot)](../../../02-prompt-engineering)
  - [زنجیره تفکر](../../../02-prompt-engineering)
  - [پرامپت براساس نقش](../../../02-prompt-engineering)
  - [قالب‌های پرامپت](../../../02-prompt-engineering)
- [الگوهای پیشرفته](../../../02-prompt-engineering)
- [استفاده از منابع موجود Azure](../../../02-prompt-engineering)
- [اسکرین‌شات‌های برنامه](../../../02-prompt-engineering)
- [بررسی الگوها](../../../02-prompt-engineering)
  - [اشتیاق کم در مقابل اشتیاق بالا](../../../02-prompt-engineering)
  - [اجرای وظیفه (مقدمات ابزار)](../../../02-prompt-engineering)
  - [کد خودبازتابی](../../../02-prompt-engineering)
  - [تحلیل ساختاری](../../../02-prompt-engineering)
  - [چت چند مرحله‌ای](../../../02-prompt-engineering)
  - [استدلال گام به گام](../../../02-prompt-engineering)
  - [خروجی محدودشده](../../../02-prompt-engineering)
- [آنچه واقعاً یاد می‌گیرید](../../../02-prompt-engineering)
- [گام‌های بعدی](../../../02-prompt-engineering)

## آنچه می‌آموزید

<img src="../../../translated_images/fa/what-youll-learn.c68269ac048503b2.webp" alt="آنچه می‌آموزید" width="800"/>

در ماژول قبلی، دیدید که چگونه حافظه به هوش مصنوعی مکالمه‌ای کمک می‌کند و از مدل‌های GitHub برای تعاملات پایه‌ای استفاده کردید. اکنون تمرکز خود را روی نحوه پرسیدن سوالات — یعنی خود پرامپت‌ها — با استفاده از GPT-5.2 در Azure OpenAI می‌گذاریم. نحوه ساختاردهی پرامپت‌ها به طور چشمگیری بر کیفیت پاسخ‌هایی که دریافت می‌کنید تأثیر می‌گذارد. ما با مروری بر تکنیک‌های پایه پرامپت شروع می‌کنیم، سپس به هشت الگوی پیشرفته می‌پردازیم که از قابلیت‌های GPT-5.2 به طور کامل بهره می‌برند.

از GPT-5.2 استفاده خواهیم کرد چون کنترل استدلال را معرفی می‌کند - شما می‌توانید به مدل بگویید چقدر باید قبل از پاسخ دادن فکر کند. این امر استراتژی‌های مختلف پرامپت‌نویسی را واضح‌تر می‌کند و کمک می‌کند بفهمید هر روش را چه زمانی باید به کار ببرید. همچنین از محدودیت‌های کمتری که Azure برای GPT-5.2 نسبت به مدل‌های GitHub دارد بهره‌مند می‌شویم.

## پیش‌نیازها

- اتمام ماژول ۰۱ (منابع Azure OpenAI مستقر شده)
- فایل `.env` در دایرکتوری ریشه با اطلاعات اعتبار Azure (ایجاد شده توسط `azd up` در ماژول ۰۱)

> **توجه:** اگر ماژول ۰۱ را تمام نکرده‌اید، ابتدا دستورالعمل‌های استقرار آن را دنبال کنید.

## درک مهندسی پرامپت

<img src="../../../translated_images/fa/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="مهندسی پرامپت چیست؟" width="800"/>

مهندسی پرامپت درباره طراحی متنی است که ورودی می‌دهد و به طور مداوم نتایج مورد نیاز شما را به دست می‌آورد. این فقط پرسیدن سوال نیست - بلکه ساختاردهی درخواست‌هاست تا مدل دقیقاً بفهمد چه می‌خواهید و چگونه باید آن را ارائه دهد.

این را مانند دادن دستورالعمل به یک همکار در نظر بگیرید. "باگ را برطرف کن" مبهم است. "استثنای اشاره‌گر تهی در UserService.java خط ۴۵ را با اضافه کردن بررسی تهی رفع کن" مشخص است. مدل‌های زبانی نیز همینطور کار می‌کنند — دقت و ساختار اهمیت دارد.

<img src="../../../translated_images/fa/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="نحوه همخوانی LangChain4j" width="800"/>

LangChain4j زیرساخت را فراهم می‌کند — ارتباط با مدل‌ها، حافظه، و انواع پیام — در حالی که الگوهای پرامپت فقط متن‌هایی با ساختار دقیق هستند که از طریق این زیرساخت ارسال می‌کنید. بلوک‌های کلیدی عبارتند از `SystemMessage` (که رفتار و نقش هوش مصنوعی را تعیین می‌کند) و `UserMessage` (که درخواست واقعی شما را حمل می‌کند).

## مبانی مهندسی پرامپت

<img src="../../../translated_images/fa/five-patterns-overview.160f35045ffd2a94.webp" alt="مروری بر پنج الگوی مهندسی پرامپت" width="800"/>

قبل از ورود به الگوهای پیشرفته این ماژول، بیایید پنج تکنیک پایه پرامپت را مرور کنیم. این‌ها بلوک‌های ساختاری هستند که هر مهندس پرامپت باید بشناسد. اگر قبلاً ماژول [شروع سریع](../00-quick-start/README.md#2-prompt-patterns) را انجام داده‌اید، این‌ها را در عمل دیده‌اید — در اینجا چارچوب مفهومی پشت آن‌ها است.

### پرامپت بدون نمونه (Zero-Shot)

ساده‌ترین روش: به مدل دستور مستقیم بدون هیچ نمونه‌ای بدهید. مدل کاملاً به آموزش خود تکیه می‌کند تا وظیفه را بفهمد و اجرا کند. این برای درخواست‌های ساده که رفتار مورد انتظار مشخص است خوب عمل می‌کند.

<img src="../../../translated_images/fa/zero-shot-prompting.7abc24228be84e6c.webp" alt="پرامپت بدون نمونه" width="800"/>

*دستور مستقیم بدون نمونه — مدل فقط از روی دستور وظیفه را استنتاج می‌کند*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// پاسخ: "مثبت"
```

**زمان استفاده:** طبقه‌بندی‌های ساده، سوالات مستقیم، ترجمه‌ها، یا هر وظیفه‌ای که مدل بدون راهنمایی اضافی می‌تواند انجام دهد.

### پرامپت با چند نمونه (Few-Shot)

نمونه‌هایی ارائه دهید که الگوی مورد نظر را به مدل نشان دهند. مدل فرمت ورودی-خروجی مورد انتظار را از نمونه‌های شما یاد می‌گیرد و آن را روی ورودی‌های جدید اعمال می‌کند. این به طور چشمگیری بهبود یکنواختی را برای وظایفی که فرمت یا رفتار دلخواه واضح نیستند به همراه دارد.

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

**زمان استفاده:** طبقه‌بندی‌های سفارشی، قالب‌بندی منظم، وظایف خاص حوزه، یا زمانی که نتایج zero-shot ناهمگون هستند.

### زنجیره تفکر (Chain of Thought)

از مدل بخواهید استدلال خود را گام به گام نشان دهد. به جای رفتن مستقیم به جواب، مدل مسئله را تجزیه می‌کند و هر بخش را به صورت صریح طی می‌کند. این دقت را در مسایل ریاضی، منطق و استدلال چندمرحله‌ای بهبود می‌بخشد.

<img src="../../../translated_images/fa/chain-of-thought.5cff6630e2657e2a.webp" alt="زنجیره تفکر پرامپت" width="800"/>

*استدلال گام به گام — تقسیم مسائل پیچیده به قدم‌های منطقی واضح*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// مدل نشان می‌دهد: ۱۵ - ۸ = ۷، سپس ۷ + ۱۲ = ۱۹ سیب
```

**زمان استفاده:** مسائل ریاضی، معماهای منطقی، اشکال‌زدایی، یا هر وظیفه‌ای که نمایش فرآیند استدلال موجب افزایش دقت و اعتمادپذیری می‌شود.

### پرامپت براساس نقش

پرسونا یا نقشی برای هوش مصنوعی تعیین کنید قبل از پرسیدن سوال. این زمینه‌ای فراهم می‌کند که لحن، عمق و تمرکز پاسخ را شکل می‌دهد. "معمار نرم‌افزار" مشاوره‌ای متفاوت از "توسعه‌دهنده جونیور" یا "ممیز امنیتی" می‌دهد.

<img src="../../../translated_images/fa/role-based-prompting.a806e1a73de6e3a4.webp" alt="پرامپت براساس نقش" width="800"/>

*تنظیم زمینه و پرسونا — همان سوال پاسخ متفاوتی با توجه به نقش تعیین شده می‌گیرد*

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

**زمان استفاده:** بازبینی کد، تدریس، تحلیل‌های حوزه‌ای خاص، یا زمانی که نیاز به پاسخ‌های متناسب با سطح تخصص یا دیدگاه خاص دارید.

### قالب‌های پرامپت

پرامپت‌های قابل استفاده مجدد با مکان‌نگهدارهای متغیر ایجاد کنید. به جای نوشتن پرامپت جدید هر بار، یک قالب تعریف کنید و مقادیر مختلف را در آن قرار دهید. کلاس `PromptTemplate` در LangChain4j این کار را با نحو `{{variable}}` ساده می‌کند.

<img src="../../../translated_images/fa/prompt-templates.14bfc37d45f1a933.webp" alt="قالب‌های پرامپت" width="800"/>

*پرامپت‌های قابل استفاده مجدد با مکان‌نگهدارهای متغیر — یک قالب، کاربردهای متعدد*

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

**زمان استفاده:** پرسش‌های تکراری با ورودی‌های مختلف، پردازش دسته‌ای، ساخت گردش‌کار AI قابل استفاده مجدد، یا هر سناریویی که ساختار پرامپت ثابت است ولی داده‌ها تغییر می‌کند.

---

این پنج اصل، ابزار قوی‌ای برای بیشتر وظایف پرامپت‌نویسی به شما می‌دهند. بقیه این ماژول بر پایه آن‌ها با **هشت الگوی پیشرفته** که از کنترل استدلال، خودارزیابی و قابلیت‌های خروجی ساختاری GPT-5.2 بهره می‌برند ساخته شده است.

## الگوهای پیشرفته

پس از پوشش مبانی، به هشت الگوی پیشرفته می‌پردازیم که این ماژول را منحصر به فرد می‌کند. همه مسائل نیاز به یک روش یکسان ندارند. برخی سوالات نیاز به پاسخ سریع دارند، برخی نیاز به تفکر عمیق. برخی نیاز به استدلال قابل مشاهده دارند، برخی فقط به نتایج نیاز دارند. هر الگوی زیر برای یک سناریوی متفاوت بهینه شده — و کنترل استدلال GPT-5.2 تفاوت‌ها را بیشتر برجسته می‌کند.

<img src="../../../translated_images/fa/eight-patterns.fa1ebfdf16f71e9a.webp" alt="هشت الگوی پرامپت" width="800"/>

*مروری بر هشت الگوی مهندسی پرامپت و موارد استفاده آن‌ها*

<img src="../../../translated_images/fa/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="کنترل استدلال با GPT-5.2" width="800"/>

*کنترل استدلال GPT-5.2 به شما امکان می‌دهد مشخص کنید مدل چقدر باید فکر کند — از پاسخ‌های سریع مستقیم تا کاوش عمیق*

<img src="../../../translated_images/fa/reasoning-effort.db4a3ba5b8e392c1.webp" alt="مقایسه تلاش استدلال" width="800"/>

*اشتیاق کم (سریع، مستقیم) در مقابل اشتیاق زیاد (جامع، کاوشگرانه) در رویکردهای استدلال*

**اشتیاق کم (سریع و متمرکز)** - برای سوالات ساده که می‌خواهید پاسخ سریع و مستقیم دریافت کنید. مدل حداقل استدلال را انجام می‌دهد - حداکثر دو گام. این روش را برای محاسبات، جستجوها یا سوالات سرراست به کار ببرید.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **با GitHub Copilot کاوش کنید:** فایل [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) را باز کنید و بپرسید:
> - "تفاوت الگوهای پرامپت اشتیاق کم و اشتیاق بالا چیست؟"
> - "چگونه تگ‌های XML در پرامپت‌ها به ساختاردهی پاسخ AI کمک می‌کنند؟"
> - "چه زمانی باید از الگوهای خودبازتابی استفاده کنم و چه زمانی دستور مستقیم بدهم؟"

**اشتیاق زیاد (عمیق و جامع)** - برای مسائل پیچیده که می‌خواهید تحلیل کامل انجام شود. مدل به طور مفصل بررسی می‌کند و استدلال دقیق ارائه می‌دهد. این روش را برای طراحی سیستم، تصمیمات معماری یا تحقیقات پیچیده به کار ببرید.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**اجرای وظیفه (پیشرفت گام به گام)** - برای جریان‌های کاری چند مرحله‌ای. مدل برنامه‌ای ارائه می‌دهد، هر مرحله را هنگام انجام شرح می‌دهد و سپس خلاصه می‌کند. این روش را برای مهاجرت‌ها، پیاده‌سازی‌ها یا هر روند چند مرحله‌ای به کار ببرید.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

پرامپت زنجیره تفکر به صراحت از مدل می‌خواهد فرآیند استدلال خود را نشان دهد که دقت را در کارهای پیچیده افزایش می‌دهد. تجزیه گام به گام به انسان و AI کمک می‌کند منطق را بفهمند.

> **🤖 با [گفتگوی GitHub Copilot](https://github.com/features/copilot) امتحان کنید:** درباره این الگو بپرسید:
> - "چگونه الگوی اجرای وظیفه را برای عملیات طولانی‌مدت سازگار کنم؟"
> - "بهترین روش‌های ساختاردهی مقدمات ابزار در برنامه‌های تولیدی چیست؟"
> - "چگونه می‌توانم به‌روزرسانی‌های پیشرفت میانی را در رابط کاربری ضبط و نمایش دهم؟"

<img src="../../../translated_images/fa/task-execution-pattern.9da3967750ab5c1e.webp" alt="الگوی اجرای وظیفه" width="800"/>

*برنامه → اجرا → خلاصه برای وظایف چند مرحله‌ای*

**کد خودبازتابی** - برای تولید کد با کیفیت تولید. مدل کد تولید می‌کند، آن را با معیارهای کیفیت بررسی می‌کند و به طور تکراری بهبود می‌دهد. از این روش برای ساخت ویژگی‌ها یا خدمات جدید استفاده کنید.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fa/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="چرخه خودبازتابی" width="800"/>

*حلقه بهبود تکراری - تولید، ارزیابی، شناسایی مشکلات، بهبود، تکرار*

**تحلیل ساختاری** - برای ارزیابی منظم. مدل کد را با چارچوبی ثابت (درستی، شیوه‌ها، عملکرد، امنیت) بازبینی می‌کند. این روش را برای بررسی کد یا ارزیابی کیفیت به کار ببرید.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 با [گفتگوی GitHub Copilot](https://github.com/features/copilot) امتحان کنید:** درباره تحلیل ساختاری بپرسید:
> - "چگونه می‌توانم چارچوب تحلیل را برای انواع مختلف بازبینی‌های کد شخصی‌سازی کنم؟"
> - "بهترین روش برای پارس و اجرای خروجی ساختاری‌شده برنامه‌نویسی چیست؟"
> - "چگونه می‌توانم سطح شدت موارد را در جلسات بازبینی مختلف هماهنگ نگه دارم؟"

<img src="../../../translated_images/fa/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="الگوی تحلیل ساختاری" width="800"/>

*چارچوب چهار دسته‌ای برای بررسی کد منظم با سطوح شدت*

**چت چند مرحله‌ای** - برای گفتگوهایی که نیاز به زمینه دارند. مدل پیام‌های قبلی را به خاطر می‌سپارد و بر آن‌ها بنا می‌کند. این روش را برای جلسات کمک تعاملی یا پرسش و پاسخ پیچیده به کار ببرید.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/fa/context-memory.dff30ad9fa78832a.webp" alt="حافظه زمینه" width="800"/>

*چگونه زمینه گفتگو طی چند مرحله انباشته می‌شود تا به حد توکن‌ها برسد*

**استدلال گام به گام** - برای مسائلی که نیاز به منطق قابل مشاهده دارند. مدل استدلال صریح برای هر گام را نشان می‌دهد. این روش را برای مسائل ریاضی، معماهای منطقی یا زمانی که باید فرآیند تفکر را بفهمید به کار ببرید.

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

*تقسیم مسائل به گام‌های منطقی صریح*

**خروجی محدودشده** - برای پاسخ‌هایی که نیاز به فرمت خاص دارند. مدل دقیقاً از قوانین فرمت و طول تبعیت می‌کند. این روش را برای خلاصه‌سازی‌ها یا زمانی که نیاز به ساختار خروجی دقیق دارید به کار ببرید.

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

*اجرای دقیق نیازمندی‌های فرمت، طول و ساختار*

## استفاده از منابع موجود Azure

**بررسی استقرار:**

اطمینان حاصل کنید فایل `.env` در دایرکتوری ریشه با اطلاعات اعتبار Azure وجود دارد (ایجاد شده در حین ماژول ۰۱):
```bash
cat ../.env  # باید AZURE_OPENAI_ENDPOINT، API_KEY، DEPLOYMENT را نشان دهد
```

**شروع برنامه:**

> **توجه:** اگر قبلاً همه برنامه‌ها را با `./start-all.sh` از ماژول ۰۱ شروع کرده‌اید، این ماژول در پورت ۸۰۸۳ در حال اجراست. می‌توانید دستورات راه‌اندازی زیر را رد کنید و مستقیماً به http://localhost:8083 بروید.

**گزینه ۱: استفاده از Spring Boot Dashboard (توصیه شده برای کاربران VS Code)**

کانتینر توسعه شامل افزونه Spring Boot Dashboard است که رابط بصری برای مدیریت همه برنامه‌های Spring Boot فراهم می‌کند. می‌توانید آن را در نوار فعالیت در سمت چپ VS Code پیدا کنید (آیکون Spring Boot را جستجو کنید).
از داشبورد Spring Boot، شما می‌توانید:
- تمام برنامه‌های موجود Spring Boot را در فضای کاری ببینید
- با یک کلیک برنامه‌ها را شروع یا متوقف کنید
- لاگ‌های برنامه را به‌صورت زنده مشاهده کنید
- وضعیت برنامه را زیر نظر داشته باشید

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

یا فقط همین ماژول را شروع کنید:

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

هر دو اسکریپت به طور خودکار متغیرهای محیطی را از فایل اصلی `.env` بارگذاری می‌کنند و در صورت عدم وجود، فایل‌های JAR را ایجاد می‌کنند.

> **توجه:** اگر ترجیح می‌دهید قبل از شروع همه ماژول‌ها را به‌صورت دستی بسازید:
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

در مرورگر خود آدرس http://localhost:8083 را باز کنید.

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

*داشبورد اصلی که تمام ۸ الگوی مهندسی پرامپت را با ویژگی‌ها و موارد استفاده‌شان نمایش می‌دهد*

## بررسی الگوها

رابط وب به شما اجازه می‌دهد با استراتژی‌های مختلف پرامپت آزمایش کنید. هر الگو مشکلات متفاوتی را حل می‌کند - آنها را امتحان کنید تا ببینید هر روش در چه زمانی بهتر عمل می‌کند.

### اشتیاق کم در مقابل اشتیاق زیاد

یک سوال ساده مثل "۱۵٪ از ۲۰۰ چقدر است؟" را با اشتیاق کم بپرسید. پاسخ سریع و مستقیم دریافت می‌کنید. حالا یک سوال پیچیده مثل "یک استراتژی کش برای یک API پربازدید طراحی کن" با اشتیاق زیاد بپرسید. ببینید مدل چگونه کند می‌شود و استدلال مفصل ارائه می‌دهد. همان مدل، همان ساختار سوال - اما پرامپت به آن می‌گوید چقدر باید فکر کند.

<img src="../../../translated_images/fa/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*محاسبه سریع با کمترین استدلال*

<img src="../../../translated_images/fa/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*استراتژی کش جامع (۲.۸MB)*

### اجرای وظیفه (مقدمات ابزار)

روندهای چندمرحله‌ای از برنامه‌ریزی اولیه و روایت پیشرفت بهره‌مند می‌شوند. مدل شرح می‌دهد چه کاری انجام می‌دهد، هر مرحله را روایت می‌کند، سپس نتایج را خلاصه می‌کند.

<img src="../../../translated_images/fa/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*ایجاد یک اندپوینت REST با روایت قدم به قدم (۳.۹MB)*

### کد خودبازتابی

"یک سرویس اعتبارسنجی ایمیل بساز" را امتحان کنید. به جای تولید کد و توقف، مدل تولید می‌کند، بر اساس معیارهای کیفیت ارزیابی می‌کند، نقاط ضعف را شناسایی کرده و بهبود می‌بخشد. خواهید دید که تا رسیدن به استانداردهای تولید کد، بارها تکرار می‌کند.

<img src="../../../translated_images/fa/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*سرویس اعتبارسنجی ایمیل کامل (۵.۲MB)*

### تحلیل ساختاریافته

بررسی کدها نیاز به چارچوب‌های ارزیابی یکسان دارد. مدل کد را با دسته‌بندی‌های ثابت (درستی، شیوه‌ها، عملکرد، امنیت) به همراه سطوح شدت تحلیل می‌کند.

<img src="../../../translated_images/fa/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*بررسی کد مبتنی بر چارچوب*

### گفتگو چندمرحله‌ای

سوال "Spring Boot چیست؟" را بپرسید و بلافاصله با "برایم یک مثال نشان بده" دنبال کنید. مدل سوال اول شما را به خاطر می‌سپرد و دقیقاً یک مثال Spring Boot می‌دهد. بدون حافظه، سوال دوم بسیار مبهم بود.

<img src="../../../translated_images/fa/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*حفظ زمینه در میان سوالات مختلف*

### استدلال گام‌به‌گام

یک مسئله ریاضی انتخاب کنید و آن را هم با استدلال گام‌به‌گام و هم با اشتیاق کم امتحان کنید. اشتیاق کم فقط پاسخ می‌دهد - سریع ولی مبهم. استدلال گام‌به‌گام تمام محاسبات و تصمیمات را نشان می‌دهد.

<img src="../../../translated_images/fa/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*مسئله ریاضی با گام‌های واضح*

### خروجی محدود

وقتی به فرمت یا تعداد کلمات مشخصی نیاز دارید، این الگو رعایت دقیق را الزامی می‌کند. خلاصه‌ای با دقیقاً ۱۰۰ کلمه در قالب گلوله‌ای تولید کنید.

<img src="../../../translated_images/fa/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*خلاصه یادگیری ماشین با کنترل قالب*

## واقعاً چه چیزی یاد می‌گیرید

**تلاش استدلال همه چیز را تغییر می‌دهد**

GPT-5.2 به شما امکان کنترل میزان تلاش محاسباتی از طریق پرامپت‌هایتان را می‌دهد. تلاش کم یعنی پاسخ‌های سریع با حداقل کاوش. تلاش زیاد یعنی مدل زمان می‌گذارد تا عمیقاً فکر کند. شما یاد می‌گیرید تلاش را با پیچیدگی وظیفه تطبیق دهید - وقتتان را برای سوالات ساده تلف نکنید، اما تصمیمات پیچیده را نیز شتاب‌زده نگیرید.

**ساختار رفتار را هدایت می‌کند**

توجه کردید که تگ‌های XML در پرامپت‌ها هستند؟ آنها زینتی نیستند. مدل‌ها دستورالعمل‌های ساخت‌یافته را قابل اطمینان‌تر از متن آزاد دنبال می‌کنند. وقتی به روندهای چندمرحله‌ای یا منطق پیچیده نیاز دارید، ساختار به مدل کمک می‌کند مکان خود و گام بعدی را دنبال کند.

<img src="../../../translated_images/fa/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*سازماندهی یک پرامپت خوب ساخت‌یافته با بخش‌های روشن و ساختار به سبک XML*

**کیفیت از طریق ارزیابی خودکار**

الگوهای خودبازتابی با مشخص کردن معیارهای کیفیت کار می‌کنند. به جای اینکه امیدوار باشید مدل "درست انجام دهد"، دقیقاً می‌گویید «درست» یعنی چه: منطق صحیح، مدیریت خطا، عملکرد، امنیت. سپس مدل می‌تواند خروجی خود را ارزیابی و بهبود دهد. این فرآیند تولید کد را از یک بخت‌آزمایی به فرایندی تبدیل می‌کند.

**حافظه محدود است**

گفتگوهای چندمرحله‌ای با درج تاریخچه پیام هر درخواست کار می‌کنند. اما محدودیت وجود دارد - هر مدل یک حداکثر تعداد توکن دارد. با افزایش گفتگوها، لازم است راهبردهایی برای حفظ زمینه مرتبط بدون رسیدن به حد سقف به کار ببرید. این ماژول طرز کار حافظه را نشان می‌دهد؛ بعداً خواهید آموخت که چه زمانی خلاصه کنید، چه زمانی فراموش کنید و چه زمانی بازیابی کنید.

## مراحل بعدی

**ماژول بعدی:** [03-rag - RAG (تولید افزوده با بازیابی)](../03-rag/README.md)

---

**ناوبری:** [← قبلی: ماژول ۰۱ - معرفی](../01-introduction/README.md) | [بازگشت به اصلی](../README.md) | [بعدی: ماژول ۰۳ - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**سلب مسئولیت**:  
این سند با استفاده از سرویس ترجمه هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما در تلاش برای دقت هستیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است حاوی اشتباهات یا نادقتی‌هایی باشند. سند اصلی به زبان مادری آن باید به عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حیاتی، استفاده از ترجمه حرفه‌ای انسانی توصیه می‌شود. ما در قبال هرگونه سوءتفاهم یا برداشت نادرست ناشی از استفاده از این ترجمه مسئولیتی نداریم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->