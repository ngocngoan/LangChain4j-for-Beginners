# ماژول ۰۲: مهندسی پرامپت با GPT-5.2

## فهرست مطالب

- [آنچه خواهید آموخت](../../../02-prompt-engineering)
- [پیش‌نیازها](../../../02-prompt-engineering)
- [درک مهندسی پرامپت](../../../02-prompt-engineering)
- [چگونگی استفاده از LangChain4j](../../../02-prompt-engineering)
- [الگوهای اصلی](../../../02-prompt-engineering)
- [استفاده از منابع موجود Azure](../../../02-prompt-engineering)
- [اسکرین‌شات‌های برنامه](../../../02-prompt-engineering)
- [کاوش در الگوها](../../../02-prompt-engineering)
  - [علاقه کم در مقابل علاقه زیاد](../../../02-prompt-engineering)
  - [اجرای کار (شروع ابزارها)](../../../02-prompt-engineering)
  - [کد خودبازتابی](../../../02-prompt-engineering)
  - [تحلیل ساختاریافته](../../../02-prompt-engineering)
  - [گفتگوی چندمرحله‌ای](../../../02-prompt-engineering)
  - [ استدلال مرحله به مرحله](../../../02-prompt-engineering)
  - [خروجی محدودشده](../../../02-prompt-engineering)
- [آنچه واقعاً می‌آموزید](../../../02-prompt-engineering)
- [گام‌های بعدی](../../../02-prompt-engineering)

## آنچه خواهید آموخت

در ماژول قبلی، دیدید چگونه حافظه به هوش مصنوعی مکالمه‌ای کمک می‌کند و از مدل‌های GitHub برای تعاملات پایه استفاده کردید. اکنون تمرکز ما بر نحوه پرسیدن سوالات - یعنی پرامپت‌ها - با استفاده از GPT-5.2 سرویس Azure OpenAI است. نحوه ساختاردهی پرامپت شما کیفیت پاسخ‌های دریافتی را به شدت تحت تأثیر قرار می‌دهد.

ما از GPT-5.2 استفاده می‌کنیم زیرا کنترل استدلال را معرفی می‌کند - می‌توانید به مدل بگویید چقدر باید پیش از پاسخ دادن فکر کند. این باعث آشکارتر شدن استراتژی‌های مختلف پرامپت‌نویسی می‌شود و به شما کمک می‌کند بفهمید هر رویکرد را کی باید به کار برید. همچنین از محدودیت‌های نرخ کمتر Azure برای GPT-5.2 نسبت به مدل‌های GitHub بهره‌مند خواهیم شد.

## پیش‌نیازها

- اتمام ماژول ۰۱ (منابع Azure OpenAI مستقر شده)
- فایل `.env` در دایرکتوری ریشه با اعتبارنامه‌های Azure (ایجاد شده توسط `azd up` در ماژول ۰۱)

> **نکته:** اگر ماژول ۰۱ را کامل نکرده‌اید، ابتدا دستورالعمل‌های استقرار آن را دنبال کنید.

## درک مهندسی پرامپت

مهندسی پرامپت یعنی طراحی متن ورودی که به طور مداوم نتایج مورد نیاز شما را به دست می‌دهد. فقط درباره پرسیدن سوال نیست - بلکه ساختاردهی درخواست‌هاست به طوری که مدل دقیقاً بفهمد چه می‌خواهید و چگونه آن را ارائه دهد.

به آن مانند دادن دستورالعمل به یک همکار فکر کنید. «اشکال را برطرف کن» مبهم است. «اشکال null pointer در UserService.java خط ۴۵ را با اضافه کردن چک null اصلاح کن» دقیق است. مدل‌های زبانی هم همینطور کار می‌کنند - دقت و ساختار مهم است.

## چگونگی استفاده از LangChain4j

این ماژول الگوهای پیشرفته پرامپت را با استفاده از همان پایه LangChain4j ماژول‌های قبلی نشان می‌دهد، با تمرکز بر ساختار پرامپت و کنترل استدلال.

<img src="../../../translated_images/fa/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*چگونه LangChain4j پرامپت‌های شما را به Azure OpenAI GPT-5.2 متصل می‌کند*

**وابستگی‌ها** - ماژول ۰۲ از وابستگی‌های langchain4j زیر در `pom.xml` استفاده می‌کند:  
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```
  
**پیکربندی OpenAiOfficialChatModel** - [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

مدل چت به صورت دستی به عنوان یک bean در Spring با استفاده از کلاینت رسمی OpenAI که نقاط پایانی Azure OpenAI را پشتیبانی می‌کند، پیکربندی شده است. تفاوت کلیدی با ماژول ۰۱ این است که پرامپت‌هایی که به `chatModel.chat()` ارسال می‌کنیم چطور ساختار می‌یابند، نه خود تنظیم مدل.

**پیام‌های سیستم و کاربر** - [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j انواع پیام‌ها را برای وضوح جدا می‌کند. `SystemMessage` رفتار و زمینه هوش مصنوعی را تنظیم می‌کند (مثل «شما یک بازبین کد هستید») و `UserMessage` شامل درخواست واقعی است. این جداسازی به شما امکان می‌دهد رفتار هوش مصنوعی را در پرسش‌های مختلف کاربر سازگار نگه دارید.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```
  
<img src="../../../translated_images/fa/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage زمینه پایدار فراهم می‌کند در حالی که UserMessages شامل درخواست‌های جداگانه است*

**MessageWindowChatMemory برای چندمرحله‌ای** - برای الگوی مکالمه چندمرحله‌ای، از `MessageWindowChatMemory` در ماژول ۰۱ مجدداً استفاده می‌کنیم. هر جلسه یک نمونه حافظه مخصوص به خود دارد که در `Map<String, ChatMemory>` ذخیره می‌شود، اجازه می‌دهد چند مکالمه همزمان بدون اختلاط زمینه انجام شود.

**قالب‌های پرامپت** - تمرکز واقعی اینجا مهندسی پرامپت است، نه APIهای جدید LangChain4j. هر الگو (علاقه کم، علاقه زیاد، اجرای کار و غیره) از همان متد `chatModel.chat(prompt)` اما با رشته‌های پرامپت دقیقا ساختاربندی شده استفاده می‌کند. تگ‌های XML، دستورات و قالب‌بندی‌ها همه بخشی از متن پرامپت است، نه قابلیت‌های LangChain4j.

**کنترل استدلال** - میزان تلاش استدلال در GPT-5.2 از طریق دستورالعمل‌های پرامپت مانند «حداکثر ۲ مرحله استدلال» یا «بررسی عمیق» کنترل می‌شود. این‌ها تکنیک‌های مهندسی پرامپت هستند، نه پیکربندی‌های LangChain4j. این کتابخانه فقط پرامپت‌های شما را به مدل می‌رساند.

نکته اصلی: LangChain4j زیرساخت‌ها را فراهم می‌کند (اتصال مدل از طریق [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)، حافظه، مدیریت پیام از طریق [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java))، در حالی که این ماژول به شما یاد می‌دهد چگونه پرامپت‌های مؤثر در آن زیرساخت خلق کنید.

## الگوهای اصلی

هر مشکلی نیاز به رویکرد یکسان ندارد. برخی سوالات به پاسخ سریع نیاز دارند، بعضی به تفکر عمیق. بعضی به استدلال قابل مشاهده نیاز دارند، بعضی فقط نتیجه. این ماژول هشت الگوی پرامپت را پوشش می‌دهد که هر کدام برای شرایط خاص بهینه شده‌اند. شما با همه آن‌ها آزمایش خواهید کرد تا بفهمید هر رویکرد چه زمانی بهترین است.

<img src="../../../translated_images/fa/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*نمای کلی هشت الگوی مهندسی پرامپت و موارد استفاده آن‌ها*

<img src="../../../translated_images/fa/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*مقایسه رویکردهای استدلال با علاقه کم (سریع، مستقیم) در مقابل علاقه زیاد (عمیق، پژوهشی)*

**علاقه کم (سریع و متمرکز)** - برای سوالات ساده که پاسخ سریع و مستقیم می‌خواهید. مدل حداقل استدلال را انجام می‌دهد - حداکثر ۲ مرحله. از این برای محاسبات، جستجوها یا پرسش‌های سرراست استفاده کنید.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```
  
> 💡 **کشف با GitHub Copilot:** فایل [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) را باز کنید و بپرسید:  
> - «تفاوت بین الگوهای علاقه کم و علاقه زیاد پرامپت چیست؟»  
> - «چگونه تگ‌های XML در پرامپت‌ها به ساختار پاسخ AI کمک می‌کنند؟»  
> - «چه زمانی باید الگوهای خودبازتابی را نسبت به دستور مستقیم به کار ببرم؟»

**علاقه زیاد (عمیق و کامل)** - برای مشکلات پیچیده که تحلیل جامع می‌خواهید. مدل کاملاً بررسی می‌کند و استدلال دقیق نشان می‌دهد. از این برای طراحی سیستم، تصمیمات معماری، یا پژوهش‌های پیچیده استفاده کنید.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**اجرای کار (پیشرفت مرحله به مرحله)** - برای گردش کار چند مرحله‌ای. مدل برنامه‌ای از پیش فراهم می‌کند، هر مرحله را هنگام کار شرح می‌دهد و سپس خلاصه می‌دهد. از این برای مهاجرت‌ها، پیاده‌سازی‌ها، یا هر فرایند چند مرحله‌ای استفاده کنید.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```
  
پرامپت زنجیره اندیشه (Chain-of-Thought) عمداً از مدل می‌خواهد روند استدلالش را نشان دهد که باعث بهبود دقت برای کارهای پیچیده می‌شود. تفکیک مرحله به مرحله به هر دو انسان و AI کمک می‌کند منطق را بفهمند.

> **🤖 امتحان با چت [GitHub Copilot](https://github.com/features/copilot):** درباره این الگو سوال کنید:  
> - «چگونه الگوی اجرای کار را برای عملیات طولانی مدت سازگار کنم؟»  
> - «بهترین روش‌های ساختاردهی شروع ابزارها در برنامه‌های تولیدی چیست؟»  
> - «چطور می‌توانم به‌روزرسانی‌های پیشرفت میانی را در رابط کاربری ضبط و نمایش دهم؟»

<img src="../../../translated_images/fa/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*گردش کار برنامه‌ریزی → اجرا → خلاصه برای کارهای چندمرحله‌ای*

**کد خودبازتابی** - برای تولید کد با کیفیت تولید. مدل کد تولید می‌کند، آن را با معیارهای کیفیت بررسی می‌کند و به صورت تکراری بهبود می‌دهد. هنگام ساخت ویژگی‌ها یا سرویس‌های جدید از این استفاده کنید.

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
  
<img src="../../../translated_images/fa/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*چرخه بهبود تکراری - تولید، ارزیابی، شناسایی اشکالات، بهبود، تکرار*

**تحلیل ساختاریافته** - برای ارزیابی مداوم. مدل کد را با چارچوب ثابتی (درستی، شیوه‌ها، عملکرد، امنیت) بررسی می‌کند. برای بازبینی کد یا ارزیابی کیفیت استفاده کنید.

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
  
> **🤖 امتحان با چت [GitHub Copilot](https://github.com/features/copilot):** درباره تحلیل ساختاریافته سوال کنید:  
> - «چطور چارچوب تحلیل را برای انواع مختلف بازبینی کد سفارشی کنم؟»  
> - «بهترین روش برای پارس کردن و عمل به خروجی ساختاریافته چیست؟»  
> - «چطور اطمینان حاصل کنم سطوح شدت در جلسات مختلف بازبینی سازگار است؟»

<img src="../../../translated_images/fa/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*چارچوب چهار دسته‌ای برای بازبینی‌های مداوم کد با سطوح شدت*

**گفتگوی چندمرحله‌ای** - برای مکالماتی که به زمینه نیاز دارند. مدل پیام‌های قبلی را به یاد می‌آورد و بر اساس آن‌ها می‌سازد. برای جلسات پشتیبانی تعاملی یا پرسش و پاسخ‌های پیچیده استفاده کنید.

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

**استدلال مرحله به مرحله** - برای مشکلاتی که منطق قابل مشاهده نیاز دارند. مدل استدلال صریح برای هر مرحله نشان می‌دهد. از این برای مسائل ریاضی، پازل‌های منطقی، یا زمانی که باید فرآیند تفکر را بفهمید استفاده کنید.

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

*شکستن مشکلات به مراحل منطقی صریح*

**خروجی محدودشده** - برای پاسخ‌هایی با الزامات قالب خاص. مدل به شدت از قواعد قالب و طول پیروی می‌کند. از این برای خلاصه‌ها یا زمانی که ساختار خروجی دقیق نیاز دارید استفاده کنید.

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

*اجرای مقررات خاص قالب، طول و ساختار*

## استفاده از منابع موجود Azure

**تأیید استقرار:**

مطمئن شوید فایل `.env` در دایرکتوری ریشه با اعتبارنامه‌های Azure موجود است (ایجاد شده توسط ماژول ۰۱):  
```bash
cat ../.env  # باید AZURE_OPENAI_ENDPOINT، API_KEY، DEPLOYMENT را نشان دهد
```
  
**راه‌اندازی برنامه:**

> **نکته:** اگر قبلاً همه برنامه‌ها را با `./start-all.sh` از ماژول ۰۱ اجرا کرده‌اید، این ماژول هم‌اکنون روی پورت ۸۰۸۳ در حال اجراست. می‌توانید دستورات شروع زیر را رد کنید و مستقیماً به http://localhost:8083 بروید.

**گزینه ۱: استفاده از Spring Boot Dashboard (توصیه شده برای کاربران VS Code)**

کانتینر توسعه شامل افزونه Spring Boot Dashboard است، که رابط بصری برای مدیریت همه برنامه‌های Spring Boot فراهم می‌کند. می‌توانید آن را در نوار فعالیت سمت چپ VS Code پیدا کنید (آیکون Spring Boot را جستجو کنید).

از Spring Boot Dashboard شما می‌توانید:  
- همه برنامه‌های Spring Boot موجود در فضای کاری را ببینید  
- تنها با یک کلیک برنامه‌ها را شروع/توقف کنید  
- لاگ‌های برنامه را به صورت زنده مشاهده کنید  
- وضعیت برنامه را پایش کنید

فقط روی دکمه پخش کنار "prompt-engineering" کلیک کنید تا این ماژول راه‌اندازی شود، یا همه ماژول‌ها را همزمان اجرا کنید.

<img src="../../../translated_images/fa/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**گزینه ۲: استفاده از اسکریپت‌های شل**

تمام برنامه‌های وب (ماژول‌های ۰۱ تا ۰۴) را اجرا کنید:

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
  
هر دو اسکریپت به طور خودکار متغیرهای محیطی را از فایل `.env` ریشه بارگذاری می‌کنند و اگر فایل JAR وجود نداشته باشد آن را می‌سازند.

> **نکته:** اگر ترجیح می‌دهید قبل از شروع دستی همه ماژول‌ها را بسازید:  
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
  
مرورگر خود را باز کرده و به http://localhost:8083 مراجعه کنید.

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

*داشبورد اصلی که همه ۸ الگوی مهندسی پرامپت را با ویژگی‌ها و موارد استفاده‌شان نشان می‌دهد*

## کاوش در الگوها

رابط وب به شما امکان آزمایش استراتژی‌های مختلف پرامپت را می‌دهد. هر الگو مشکلات متفاوتی را حل می‌کند - آن‌ها را امتحان کنید تا ببینید هر رویکرد چه زمانی بهترین عملکرد را دارد.

### علاقه کم در مقابل علاقه زیاد

یک سوال ساده مثل «۱۵٪ از ۲۰۰ چقدر است؟» با استفاده از علاقه کم بپرسید. پاسخ فوری و مستقیم می‌گیرید. حالا سوال پیچیده‌ای مثل «یک استراتژی کشینگ برای یک API پرترافیک طراحی کن» با علاقه زیاد بپرسید. ببینید مدل چگونه کند می‌شود و استدلال کامل ارائه می‌دهد. همان مدل، همان ساختار سوال - اما پرامپت به آن می‌گوید چقدر فکر کند.
<img src="../../../translated_images/fa/low-eagerness-demo.898894591fb23aa0.webp" alt="نمایش کم‌اشتیاق" width="800"/>

*محاسبه سریع با حداقل استدلال*

<img src="../../../translated_images/fa/high-eagerness-demo.4ac93e7786c5a376.webp" alt="نمایش پر اشتیاق" width="800"/>

*استراتژی ذخیره‌سازی جامع (۲.۸ مگابایت)*

### اجرای کار (مقدمه ابزارها)

گردش‌های کاری چندمرحله‌ای از برنامه‌ریزی اولیه و روایت پیشرفت بهره می‌برند. مدل توضیح می‌دهد چه کاری انجام خواهد داد، هر مرحله را روایت می‌کند، سپس نتایج را خلاصه می‌کند.

<img src="../../../translated_images/fa/tool-preambles-demo.3ca4881e417f2e28.webp" alt="نمایش اجرای کار" width="800"/>

*ایجاد یک نقطه انتهایی REST با روایت مرحله به مرحله (۳.۹ مگابایت)*

### کد خودبازتابی

عبارت «یک سرویس اعتبارسنجی ایمیل ایجاد کن» را امتحان کنید. به جای صرفاً تولید کد و توقف، مدل تولید می‌کند، طبق معیارهای کیفیت ارزیابی می‌کند، ضعف‌ها را شناسایی می‌کند و بهبود می‌دهد. می‌بینید که تا زمانی که کد به استانداردهای تولید برسد، مرحله به مرحله تکرار می‌کند.

<img src="../../../translated_images/fa/self-reflecting-code-demo.851ee05c988e743f.webp" alt="نمایش کد خودبازتابی" width="800"/>

*سرویس کامل اعتبارسنجی ایمیل (۵.۲ مگابایت)*

### تحلیل ساختارمند

بازبینی کد نیازمند چارچوب‌های ارزیابی سازگار است. مدل کد را با استفاده از دسته‌بندی‌های ثابت (صحت، شیوه‌ها، کارایی، امنیت) و سطوح شدت تحلیل می‌کند.

<img src="../../../translated_images/fa/structured-analysis-demo.9ef892194cd23bc8.webp" alt="نمایش تحلیل ساختارمند" width="800"/>

*بازبینی کد مبتنی بر چارچوب*

### چت چند مرحله‌ای

بپرسید «Spring Boot چیست؟» و سپس فوراً سؤال «یک مثال نشان بده» را مطرح کنید. مدل سؤال اول شما را به یاد می‌آورد و به طور خاص یک مثال Spring Boot ارائه می‌دهد. بدون حافظه، سؤال دوم خیلی مبهم بود.

<img src="../../../translated_images/fa/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="نمایش چت چند مرحله‌ای" width="800"/>

*حفظ زمینه در بین سوالات*

### استدلال مرحله به مرحله

یک مسئله ریاضی انتخاب کنید و آن را با هر دو روش استدلال مرحله‌به‌مرحله و کم‌اشتیاق امتحان کنید. کم‌اشتیاق فقط پاسخ را می‌دهد - سریع ولی مبهم. استدلال مرحله به مرحله هر محاسبه و تصمیم را نشان می‌دهد.

<img src="../../../translated_images/fa/step-by-step-reasoning-demo.12139513356faecd.webp" alt="نمایش استدلال مرحله به مرحله" width="800"/>

*مسئله ریاضی با مراحل صریح*

### خروجی محدود شده

وقتی به فرمت یا تعداد کلمات خاص نیاز دارید، این الگو رعایت دقیق را الزامی می‌کند. خلاصه‌ای با دقیقاً ۱۰۰ کلمه در قالب نقطه‌دار تولید کنید.

<img src="../../../translated_images/fa/constrained-output-demo.567cc45b75da1633.webp" alt="نمایش خروجی محدود شده" width="800"/>

*خلاصه یادگیری ماشین با کنترل قالب*

## آنچه واقعاً یاد می‌گیرید

**تلاش استدلال همه چیز را تغییر می‌دهد**

GPT-5.2 اجازه می‌دهد تلاش محاسباتی را از طریق درخواست‌هایتان کنترل کنید. تلاش کم یعنی پاسخ‌های سریع با حداقل کاوش. تلاش زیاد یعنی مدل زمان می‌گذارد تا عمیق فکر کند. شما می‌آموزید تلاش را با پیچیدگی کار هماهنگ کنید - وقتتان را روی سوالات ساده تلف نکنید، ولی تصمیمات پیچیده را هم شتاب‌زده نگیرید.

**ساختار رفتار را هدایت می‌کند**

توجه کنید به تگ‌های XML در درخواست‌ها؟ اینها تزئینی نیستند. مدل‌ها دستورات ساختارمند را از متن آزاد قابل اطمینان‌تر دنبال می‌کنند. وقتی به فرایندهای چندمرحله‌ای یا منطق پیچیده نیاز دارید، ساختار به مدل کمک می‌کند بفهمد کجاست و بعد از آن چه باید بکند.

<img src="../../../translated_images/fa/prompt-structure.a77763d63f4e2f89.webp" alt="ساختار درخواست" width="800"/>

*آناتومی یک درخواست خوب ساختاربندی شده با بخش‌های واضح و سازماندهی به سبک XML*

**کیفیت از طریق خودارزیابی**

الگوهای خودبازتابی با روشن کردن معیارهای کیفیت کار می‌کنند. به جای اینکه امیدوار باشید مدل «درست انجام می‌دهد»، دقیقاً به آن می‌گویید «درست» یعنی چه: منطق صحیح، مدیریت خطا، کارایی، امنیت. سپس مدل می‌تواند خروجی خودش را ارزیابی و بهبود دهد. این روند تولید کد را از قمار به یک فرایند تبدیل می‌کند.

**زمینه محدود است**

گفتگوهای چندمرحله‌ای با شمل تاریخچه پیام در هر درخواست کار می‌کنند. اما محدودیتی وجود دارد - هر مدل تعداد توکن حداکثری دارد. با افزایش گفتگوها، نیاز به راهکارهایی برای حفظ زمینه مرتبط بدون رسیدن به این سقف خواهید داشت. این بخش نحوه کار حافظه را نشان می‌دهد؛ بعداً یاد خواهید گرفت چه زمانی خلاصه کنید، چه زمانی فراموش کنید، و چه زمانی بازیابی کنید.

## مراحل بعدی

**ماژول بعدی:** [03-rag - تولید افزوده بازیابی (RAG)](../03-rag/README.md)

---

**ناوبری:** [← قبلی: ماژول ۰۱ - معرفی](../01-introduction/README.md) | [بازگشت به اصلی](../README.md) | [بعدی: ماژول ۰۳ - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**سلب مسئولیت**:  
این سند با استفاده از خدمات ترجمه ماشینی هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما در پی دقت هستیم، لطفاً آگاه باشید که ترجمه‌های خودکار ممکن است شامل خطاها یا اشتباهاتی باشد. سند اصلی به زبان بومی خود باید به عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حیاتی، استفاده از ترجمه حرفه‌ای انسانی توصیه می‌شود. ما مسئول هیچگونه سوءتفاهم یا برداشت نادرست ناشی از استفاده این ترجمه نیستیم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->