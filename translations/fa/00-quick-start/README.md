# ماژول ۰۰: شروع سریع

## فهرست مطالب

- [مقدمه](../../../00-quick-start)
- [LangChain4j چیست؟](../../../00-quick-start)
- [وابستگی‌های LangChain4j](../../../00-quick-start)
- [پیش‌نیازها](../../../00-quick-start)
- [تنظیمات](../../../00-quick-start)
  - [۱. دریافت توکن GitHub خود](../../../00-quick-start)
  - [۲. تنظیم توکن خود](../../../00-quick-start)
- [اجرای نمونه‌ها](../../../00-quick-start)
  - [۱. گفتگوی پایه](../../../00-quick-start)
  - [۲. الگوهای پرامپت](../../../00-quick-start)
  - [۳. فراخوانی تابع](../../../00-quick-start)
  - [۴. پرسش و پاسخ درباره مستندات (RAG)](../../../00-quick-start)
  - [۵. هوش مصنوعی مسئولانه](../../../00-quick-start)
- [هر مثال چه چیزی را نشان می‌دهد](../../../00-quick-start)
- [قدم‌های بعدی](../../../00-quick-start)
- [عیب‌یابی](../../../00-quick-start)

## مقدمه

این شروع سریع با هدف راه‌اندازی و استفاده سریع شما از LangChain4j طراحی شده است. این بخش اصول پایه ساخت برنامه‌های هوش مصنوعی با LangChain4j و مدل‌های GitHub را پوشش می‌دهد. در ماژول‌های بعدی از Azure OpenAI با LangChain4j برای ساخت برنامه‌های پیشرفته‌تر استفاده خواهید کرد.

## LangChain4j چیست؟

LangChain4j یک کتابخانه جاوا است که ساخت برنامه‌های مبتنی بر هوش مصنوعی را ساده می‌کند. به جای کار با کلاینت‌های HTTP و تجزیه JSON، با APIهای تمیز جاوا کار می‌کنید.

اصطلاح "زنجیره" در LangChain به زنجیره کردن چندین مؤلفه اشاره دارد - ممکن است یک پرامپت را به یک مدل و سپس به یک پارسر متصل کنید، یا چندین فراخوانی هوش مصنوعی را به هم زنجیر کنید که خروجی یکی ورودی بعدی شود. این شروع سریع بر اصول بنیادی تمرکز دارد قبل از کاوش زنجیره‌های پیچیده‌تر.

<img src="../../../translated_images/fa/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*زنجیره کردن مؤلفه‌ها در LangChain4j - بلوک‌های سازنده که به هم متصل می‌شوند تا گردش‌کارهای قدرتمند هوش مصنوعی بسازند*

ما از سه مؤلفه اصلی استفاده می‌کنیم:

**ChatLanguageModel** - رابط تعامل با مدل هوش مصنوعی. با فراخوانی `model.chat("prompt")` پاسخ متنی دریافت می‌کنید. ما از `OpenAiOfficialChatModel` استفاده می‌کنیم که با نقاط پایانی سازگار با OpenAI مانند مدل‌های GitHub کار می‌کند.

**AiServices** - ایجاد رابط‌های خدمات AI با نوع امن. متدها را تعریف کنید، با `@Tool` حاشیه‌نویسی کنید و LangChain4j ترتیب اجرا را مدیریت می‌کند. هوش مصنوعی به طور خودکار در مواقع نیاز متدهای جاوای شما را فراخوانی می‌کند.

**MessageWindowChatMemory** - حفظ سابقه گفت‌وگو. بدون این، هر درخواست مستقل است. با آن، هوش مصنوعی پیام‌های قبلی را به خاطر می‌سپارد و متن را در چند نوبت حفظ می‌کند.

<img src="../../../translated_images/fa/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*معماری LangChain4j - مؤلفه‌های اصلی که در کنار هم برای تقویت برنامه‌های هوش مصنوعی شما کار می‌کنند*

## وابستگی‌های LangChain4j

این شروع سریع از دو وابستگی Maven در [`pom.xml`](../../../00-quick-start/pom.xml) استفاده می‌کند:

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

ماژول `langchain4j-open-ai-official` کلاس `OpenAiOfficialChatModel` را ارائه می‌دهد که به APIهای سازگار با OpenAI متصل می‌شود. مدل‌های GitHub از همان فرمت API استفاده می‌کنند، بنابراین نیاز به آداپتور مخصوصی نیست - کافی است آدرس پایه را روی `https://models.github.ai/inference` تنظیم کنید.

## پیش‌نیازها

**از Dev Container استفاده می‌کنید؟** جاوا و Maven قبلاً نصب شده‌اند. فقط به یک توکن دسترسی شخصی GitHub نیاز دارید.

**در توسعه محلی:**
- جاوا ۲۱+، Maven 3.9+
- توکن دسترسی شخصی GitHub (دستورالعمل‌ها در ادامه)

> **توجه:** این ماژول از `gpt-4.1-nano` مدل‌های GitHub استفاده می‌کند. نام مدل را در کد تغییر ندهید - به گونه‌ای پیکربندی شده که با مدل‌های موجود GitHub کار کند.

## تنظیمات

### ۱. دریافت توکن GitHub خود

1. به [تنظیمات GitHub → توکن‌های دسترسی شخصی](https://github.com/settings/personal-access-tokens) بروید
2. روی "Generate new token" کلیک کنید
3. یک نام توصیفی انتخاب کنید (مثلاً "LangChain4j Demo")
4. زمان انقضا را تنظیم کنید (۷ روز توصیه می‌شود)
5. در بخش "دسترسی‌های حساب"، "Models" را پیدا کرده و روی "فقط خواندن" تنظیم کنید
۶. روی "Generate token" کلیک کنید
۷. توکن خود را کپی کرده و ذخیره کنید - دیگر قابل مشاهده نخواهد بود

### ۲. تنظیم توکن خود

**گزینه ۱: استفاده از VS Code (توصیه شده)**

اگر از VS Code استفاده می‌کنید، توکن خود را در فایل `.env` در ریشه پروژه اضافه کنید:

اگر فایل `.env` وجود ندارد، `.env.example` را به `.env` کپی کنید یا یک فایل `.env` جدید در ریشه پروژه بسازید.

**نمونه فایل `.env`:**
```bash
# در /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

سپس می‌توانید به سادگی روی هر فایل دمو (مثلاً `BasicChatDemo.java`) در اکسپلورر راست‌کلیک کرده و **"Run Java"** را انتخاب کنید یا از پیکربندی‌های اجرا در پنل Run and Debug استفاده نمایید.

**گزینه ۲: استفاده از ترمینال**

توکن را به عنوان متغیر محیطی تنظیم کنید:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## اجرای نمونه‌ها

**استفاده از VS Code:** به سادگی روی هر فایل دمو در اکسپلورر راست‌کلیک کنید و **"Run Java"** را انتخاب کنید، یا از پیکربندی‌های اجرا در پنل Run and Debug استفاده کنید (ابتدا مطمئن شوید توکن خود را در فایل `.env` اضافه کرده‌اید).

**استفاده از Maven:** همچنین می‌توانید از خط فرمان اجرا کنید:

### ۱. گفتگوی پایه

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### ۲. الگوهای پرامپت

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

الگوهای zero-shot، few-shot، chain-of-thought و نقش‌محور را نشان می‌دهد.

### ۳. فراخوانی تابع

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

هوش مصنوعی به طور خودکار متدهای جاوای شما را در مواقع نیاز فراخوانی می‌کند.

### ۴. پرسش و پاسخ درباره مستندات (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

سؤالاتی درباره محتوای فایل `document.txt` بپرسید.

### ۵. هوش مصنوعی مسئولانه

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

نحوه فیلترهای ایمنی هوش مصنوعی برای جلوگیری از محتوای مضر را مشاهده کنید.

## هر مثال چه چیزی را نشان می‌دهد

**گفتگوی پایه** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

از اینجا شروع کنید تا LangChain4j را در ساده‌ترین شکلش ببینید. شما یک `OpenAiOfficialChatModel` ایجاد می‌کنید، پرامپتی با `.chat()` ارسال می‌کنید و پاسخ دریافت می‌کنید. این مثال پایه‌ها را نشان می‌دهد: چگونه مدل‌ها را با نقاط پایانی سفارشی و کلیدهای API مقداردهی اولیه کنید. وقتی این الگو را فهمیدید، سایر موارد بر اساس آن ساخته می‌شوند.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 امتحان با [GitHub Copilot](https://github.com/features/copilot) Chat:** فایل [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) را باز کنید و بپرسید:
> - "چگونه در این کد از مدل‌های GitHub به Azure OpenAI سوئیچ کنم؟"
> - "چه پارامترهای دیگری را می‌توانم در OpenAiOfficialChatModel.builder() پیکربندی کنم؟"
> - "چگونه پاسخ‌های استریم شده اضافه کنم به جای انتظار برای پاسخ کامل؟"

**مهندسی پرامپت** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

حالا که می‌دانید چگونه به مدل صحبت کنید، بیایید بررسی کنیم چه چیزی به آن می‌گویید. این دمو از همان تنظیمات مدل استفاده می‌کند اما پنج الگوی مختلف پرامپت را نشان می‌دهد. الگوهای zero-shot برای دستورهای مستقیم، few-shot که از نمونه‌ها یاد می‌گیرند، chain-of-thought که مراحل استدلال را نشان می‌دهد و نقش‌محور که زمینه را تعیین می‌کند را امتحان کنید. خواهید دید چگونه همان مدل بر اساس نحوه طرح‌بندی درخواست‌تان نتایج بسیار متفاوتی ارائه می‌دهد.

این دمو همچنین قالب‌های پرامپت را نشان می‌دهد، که راهی قدرتمند برای ساخت پرامپت‌های قابل استفاده مجدد با متغیرها هستند.
مثال زیر پرامپتی با استفاده از `PromptTemplate` LangChain4j را نشان می‌دهد که متغیرها را پر می‌کند. هوش مصنوعی بر اساس مقصد و فعالیت داده شده پاسخ می‌دهد.

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

> **🤖 امتحان با [GitHub Copilot](https://github.com/features/copilot) Chat:** فایل [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) را باز کنید و بپرسید:
> - "تفاوت پرامپت zero-shot و few-shot چیست و کی باید از هر کدام استفاده کنم؟"
> - "پارامتر دما چگونه بر پاسخ‌های مدل تأثیر می‌گذارد؟"
> - "چه تکنیک‌هایی برای جلوگیری از حملات تزریق پرامپت در تولید وجود دارد؟"
> - "چگونه می‌توانم اشیای PromptTemplate قابل استفاده مجدد برای الگوهای رایج بسازم؟"

**ادغام ابزارها** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

اینجاست که LangChain4j قدرتمند می‌شود. از `AiServices` استفاده خواهید کرد تا یک دستیار AI بسازید که می‌تواند متدهای جاوای شما را فراخوانی کند. کافی است متدها را با `@Tool("description")` حاشیه‌نویسی کنید و LangChain4j بقیه کار را انجام می‌دهد - هوش مصنوعی به طور خودکار تصمیم می‌گیرد چه زمانی از هر ابزار استفاده کند بر اساس خواسته کاربر. این تکنیک فراخوانی تابع را نشان می‌دهد، یک تکنیک کلیدی برای ساخت هوش مصنوعی که می‌تواند عملیات انجام دهد، نه فقط پاسخ دهد.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 امتحان با [GitHub Copilot](https://github.com/features/copilot) Chat:** فایل [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) را باز کنید و بپرسید:
> - "حاشیه‌نویسی @Tool چگونه کار می‌کند و LangChain4j پشت صحنه با آن چه می‌کند؟"
> - "آیا هوش مصنوعی می‌تواند چند ابزار را به ترتیب برای حل مشکلات پیچیده فراخوانی کند؟"
> - "اگر یک ابزار خطا بدهد چه اتفاقی می‌افتد - چگونه باید خطاها را مدیریت کنم؟"
> - "چگونه یک API واقعی را به جای این مثال ماشین‌حساب ادغام کنم؟"

**پرسش و پاسخ درباره مستندات (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

در اینجا بنیاد RAG (تولید تقویت‌شده با بازیابی) را خواهید دید. به جای تکیه بر داده‌های آموزش مدل، محتوا از [`document.txt`](../../../00-quick-start/document.txt) بارگذاری می‌شود و در پرامپت وارد می‌شود. هوش مصنوعی بر اساس سند شما پاسخ می‌دهد، نه دانش عمومی خود. این اولین قدم برای ساخت سیستم‌هایی است که می‌توانند با داده‌های خود شما کار کنند.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **توجه:** این روش ساده کل مستند را در پرامپت بارگذاری می‌کند. برای فایل‌های بزرگ‌تر از ۱۰ کیلوبایت، محدودیت‌های بافت [context] را رد خواهید کرد. ماژول ۰۳ درباره تقسیم‌بندی و جستجوی وکتور برای سیستم‌های RAG تولیدی پوشش می‌دهد.

> **🤖 امتحان با [GitHub Copilot](https://github.com/features/copilot) Chat:** فایل [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) را باز کنید و بپرسید:
> - "RAG چگونه از هذیان‌های AI نسبت به استفاده از داده‌های آموزش مدل جلوگیری می‌کند؟"
> - "تفاوت این روش ساده با استفاده از تعبیه‌های وکتور برای بازیابی چیست؟"
> - "چگونه می‌توانم برای کار با چند سند یا پایگاه‌های دانش بزرگ‌تر این را مقیاس‌بندی کنم؟"
> - "بهترین روش‌ها برای ساختاردهی پرامپت تا مطمئن شویم AI فقط از متن داده شده استفاده می‌کند، چیست؟"

**هوش مصنوعی مسئولانه** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

ایمنی هوش مصنوعی را با دفاع در عمق بسازید. این دمو دو لایه حفاظت که با هم کار می‌کنند را نشان می‌دهد:

**قسمت ۱: محافظ‌های ورودی LangChain4j** - جلوگیری از پرامپت‌های خطرناک قبل از رسیدن به مدل زبانی بزرگ. محافظ‌های سفارشی ایجاد کنید که برای کلمات کلیدی یا الگوهای ممنوعه بررسی انجام می‌دهند. این‌ها در کد شما اجرا می‌شوند، بنابراین سریع و رایگان هستند.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**قسمت ۲: فیلترهای ایمنی ارائه‌دهنده** - مدل‌های GitHub دارای فیلترهای داخلی هستند که مواردی را که محافظ شما ممکن است از دست بدهد، می‌گیرند. بلوک‌های سخت (خطاهای HTTP ۴۰۰) برای تخلفات جدی و ردهای نرم که AI مودبانه امتناع می‌کند را خواهید دید.

> **🤖 امتحان با [GitHub Copilot](https://github.com/features/copilot) Chat:** فایل [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) را باز کنید و بپرسید:
> - "InputGuardrail چیست و چگونه خودم یکی بسازم؟"
> - "تفاوت بین بلوک سخت و رد نرم چیست؟"
> - "چرا باید هم محافظ‌ها و هم فیلترهای ارائه‌دهنده را با هم استفاده کنیم؟"

## قدم‌های بعدی

**ماژول بعدی:** [۰۱-مقدمه - شروع کار با LangChain4j و gpt-5 در Azure](../01-introduction/README.md)

---

**جهت‌یابی:** [← بازگشت به اصلی](../README.md) | [بعدی: ماژول ۰۱ - مقدمه →](../01-introduction/README.md)

---

## عیب‌یابی

### ساخت نخستین بار Maven

**مشکل:** اجرای اولیه `mvn clean compile` یا `mvn package` زمان زیادی می‌برد (۱۰-۱۵ دقیقه)

**علت:** Maven باید تمام وابستگی‌های پروژه (Spring Boot، کتابخانه‌های LangChain4j، SDKهای Azure و غیره) را در ساخت اول دانلود کند.

**راه‌حل:** این رفتار طبیعی است. ساخت‌های بعدی بسیار سریع‌تر خواهند بود زیرا وابستگی‌ها به صورت محلی کش می‌شوند. زمان دانلود به سرعت اینترنت شما بستگی دارد.
### نحو دستور Maven در پاورشل

**مشکل**: دستورات Maven با خطای `Unknown lifecycle phase ".mainClass=..."` مواجه می‌شوند

**علت**: پاورشل علامت `=` را به عنوان عملگر انتساب متغیر تفسیر می‌کند که نحو ویژگی Maven را خراب می‌کند

**راه‌حل**: از عملگر توقف تفسیر `--%` قبل از دستور Maven استفاده کنید:

**پاورشل:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**باش:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

عملگر `--%` به پاورشل می‌گوید که تمام آرگومان‌های باقی‌مانده را به صورت دقیق و بدون تفسیر به Maven منتقل کند.

### نمایش ایموجی در Windows PowerShell

**مشکل**: پاسخ‌های هوش مصنوعی به جای ایموجی‌ها، کاراکترهای ناخواسته (مثلاً `????` یا `â??`) را در پاورشل نشان می‌دهند

**علت**: کدگذاری پیش‌فرض پاورشل از ایموجی‌های UTF-8 پشتیبانی نمی‌کند

**راه‌حل**: قبل از اجرای برنامه‌های جاوا این دستور را اجرا کنید:
```cmd
chcp 65001
```

این کار باعث استفاده از کدگذاری UTF-8 در ترمینال می‌شود. به طور جایگزین می‌توانید از Windows Terminal استفاده کنید که پشتیبانی بهتری از یونیکد دارد.

### اشکال‌زدایی تماس‌های API

**مشکل**: خطاهای احراز هویت، محدودیت نرخ یا پاسخ‌های غیرمنتظره از مدل هوش مصنوعی

**راه‌حل**: نمونه‌ها شامل `.logRequests(true)` و `.logResponses(true)` هستند که تماس‌های API را در کنسول نمایش می‌دهند. این به حل مشکلات خطای احراز هویت، محدودیت نرخ یا پاسخ‌های غیرمنتظره کمک می‌کند. برای کاهش نویز گزارش‌ها این پرچم‌ها را در محیط تولید حذف کنید.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**سلب مسئولیت**:
این سند با استفاده از سرویس ترجمه هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما برای دقت تلاش می‌کنیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است حاوی خطاها یا نادرستی‌هایی باشند. سند اصلی به زبان بومی آن باید به عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حیاتی، ترجمه انسانی و حرفه‌ای توصیه می‌شود. ما در قبال هر گونه سوء‌تفاهم یا برداشت نادرست ناشی از استفاده از این ترجمه مسئولیتی نداریم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->