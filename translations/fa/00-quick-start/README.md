# ماژول ۰۰: شروع سریع

## فهرست مطالب

- [مقدمه](../../../00-quick-start)
- [LangChain4j چیست؟](../../../00-quick-start)
- [وابستگی‌های LangChain4j](../../../00-quick-start)
- [پیش‌نیازها](../../../00-quick-start)
- [راه‌اندازی](../../../00-quick-start)
  - [۱. دریافت توکن گیت‌هاب](../../../00-quick-start)
  - [۲. تنظیم توکن خود](../../../00-quick-start)
- [اجرای نمونه‌ها](../../../00-quick-start)
  - [۱. چت پایه](../../../00-quick-start)
  - [۲. الگوهای پرامپت](../../../00-quick-start)
  - [۳. فراخوانی تابع](../../../00-quick-start)
  - [۴. پرسش و پاسخ اسناد (Easy RAG)](../../../00-quick-start)
  - [۵. هوش مصنوعی مسئولانه](../../../00-quick-start)
- [هر مثال چه چیزی نشان می‌دهد](../../../00-quick-start)
- [گام‌های بعدی](../../../00-quick-start)
- [عیب‌یابی](../../../00-quick-start)

## مقدمه

این شروع سریع برای این است که شما را سریع‌تر با LangChain4j راه‌اندازی کند. این راهنما اصول اولیه ساخت برنامه‌های هوش مصنوعی با LangChain4j و مدل‌های گیت‌هاب را پوشش می‌دهد. در ماژول‌های بعدی به Azure OpenAI و GPT-5.2 سوئیچ خواهید کرد و هر مفهوم را عمیق‌تر بررسی می‌کنید.

## LangChain4j چیست؟

LangChain4j یک کتابخانه جاوا است که ساخت برنامه‌های هوش مصنوعی را ساده می‌کند. به جای کار با کلاینت‌های HTTP و پارس کردن JSON، با APIهای تمیز جاوا کار می‌کنید.

«زنجیره» در LangChain به معنی زنجیر کردن چندین مولفه است - ممکن است یک پرامپت را به یک مدل و سپس به یک پارسر متصل کنید، یا چند فراخوانی AI را به هم زنجیر کنید که خروجی یکی به ورودی بعدی می‌رود. این شروع سریع روی اصول اولیه متمرکز است پیش از آنکه زنجیره‌های پیچیده‌تر را بررسی کنیم.

<img src="../../../translated_images/fa/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*زنجیر کردن مولفه‌ها در LangChain4j - بلوک‌های سازنده متصل می‌شوند تا گردش‌کارهای قدرتمند هوش مصنوعی ایجاد کنند*

ما از سه مولفه اصلی استفاده می‌کنیم:

**ChatModel** - رابط تعامل با مدل هوش مصنوعی. تابع `model.chat("prompt")` را فراخوانی کنید و یک رشته پاسخ دریافت کنید. ما از `OpenAiOfficialChatModel` استفاده می‌کنیم که با نقطه‌های پایانی سازگار با OpenAI مانند مدل‌های GitHub کار می‌کند.

**AiServices** - رابط‌های سرویس هوش مصنوعی با نوع ایمن ایجاد می‌کند. متدها را تعریف کنید، آن‌ها را با `@Tool` حاشیه‌نویسی کنید و LangChain4j هماهنگی را مدیریت می‌کند. هوش مصنوعی به طور خودکار هنگام نیاز متدهای جاوای شما را فراخوانی می‌کند.

**MessageWindowChatMemory** - تاریخچه گفتگو را نگهداری می‌کند. بدون آن، هر درخواست مستقل است. با آن، هوش مصنوعی پیام‌های قبلی را به خاطر می‌سپارد و در چند نوبت زمینه را حفظ می‌کند.

<img src="../../../translated_images/fa/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*معماری LangChain4j - مولفه‌های اصلی که با هم کار می‌کنند تا برنامه‌های هوش مصنوعی شما را توانمند کنند*

## وابستگی‌های LangChain4j

این شروع سریع از سه وابستگی Maven در [`pom.xml`](../../../00-quick-start/pom.xml) استفاده می‌کند:

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

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

ماژول `langchain4j-open-ai-official` کلاس `OpenAiOfficialChatModel` را فراهم می‌کند که به APIهای سازگار با OpenAI متصل می‌شود. مدل‌های GitHub از همان قالب API استفاده می‌کنند، بنابراین نیاز به آداپتور خاصی نیست – کافی است آدرس پایه را به `https://models.github.ai/inference` تنظیم کنید.

ماژول `langchain4j-easy-rag` جداسازی خودکار اسناد، جاسازی و بازیابی را فراهم می‌کند تا بتوانید برنامه‌های RAG بسازید بدون اینکه هر مرحله را دستی پیکربندی کنید.

## پیش‌نیازها

**از Dev Container استفاده می‌کنید؟** جاوا و Maven قبلاً نصب شده‌اند. فقط به توکن دسترسی شخصی گیت‌هاب نیاز دارید.

**توسعه محلی:**
- جاوا ۲۱ یا بالاتر، Maven 3.9 یا بالاتر
- توکن دسترسی شخصی گیت‌هاب (دستورالعمل‌ها در پایین)

> **توجه:** این ماژول از مدل `gpt-4.1-nano` از مدل‌های GitHub استفاده می‌کند. نام مدل را در کد تغییر ندهید – این نام برای کار با مدل‌های در دسترس GitHub تنظیم شده است.

## راه‌اندازی

### ۱. دریافت توکن گیت‌هاب

۱. به [تنظیمات گیت‌هاب → توکن‌های دسترسی شخصی](https://github.com/settings/personal-access-tokens) بروید  
۲. روی «Generate new token» کلیک کنید  
۳. یک نام توصیفی تنظیم کنید (مثلاً «LangChain4j Demo»)  
۴. تاریخ انقضا را تنظیم کنید (۷ روز توصیه می‌شود)  
۵. زیر «دسترسی‌های حساب»، بخش «Models» را پیدا کنید و روی «فقط خواندنی» تنظیم کنید  
۶. روی «Generate token» کلیک کنید  
۷. توکن را کپی و ذخیره کنید – دوباره نمایش داده نخواهد شد  

### ۲. تنظیم توکن خود

**گزینه ۱: استفاده از VS Code (توصیه شده)**

اگر از VS Code استفاده می‌کنید، توکن خود را به فایل `.env` در ریشه پروژه اضافه کنید:

اگر فایل `.env` وجود ندارد، فایل `.env.example` را به `.env` کپی کنید یا یک فایل `.env` جدید در ریشه پروژه بسازید.

**مثال فایل `.env`:**  
```bash
# در /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```
  
سپس می‌توانید به سادگی روی هر فایل دمو (مثلاً `BasicChatDemo.java`) در اکسپلورر راست‌کلیک کرده و **«Run Java»** را انتخاب کنید یا از پیکربندی‌های اجرا در پنل Run and Debug استفاده کنید.

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

**استفاده از VS Code:** فقط روی هر فایل دمو در اکسپلورر راست‌کلیک کرده و **«Run Java»** را انتخاب کنید، یا از پیکربندی‌های اجرا در پنل Run and Debug استفاده کنید (اطمینان حاصل کنید که اول توکن را به فایل `.env` اضافه کرده‌اید).

**استفاده از Maven:** به طور متناوب، می‌توانید از خط فرمان اجرا کنید:

### ۱. چت پایه

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
  
الگوهای zero-shot، few-shot، chain-of-thought و مبتنی بر نقش را نشان می‌دهد.

### ۳. فراخوانی تابع

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
هوش مصنوعی به طور خودکار هنگام نیاز متدهای جاوای شما را فراخوانی می‌کند.

### ۴. پرسش و پاسخ اسناد (Easy RAG)

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
سوالاتی در مورد اسناد خود با استفاده از Easy RAG با جاسازی و بازیابی خودکار بپرسید.

### ۵. هوش مصنوعی مسئولانه

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```
  
ببینید چگونه فیلترهای ایمنی هوش مصنوعی محتوای مضر را مسدود می‌کنند.

## هر مثال چه چیزی نشان می‌دهد

**چت پایه** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

از این‌جا شروع کنید تا LangChain4j را در ساده‌ترین حالت خودش ببینید. یک `OpenAiOfficialChatModel` می‌سازید، یک پرامپت با `.chat()` می‌فرستید و پاسخ را دریافت می‌کنید. این پایه را نشان می‌دهد: چگونه مدل‌ها را با نقاط پایانی سفارشی و کلید API مقداردهی اولیه کنید. وقتی این الگو را فهمیدید، بقیه‌ چیزها بر اساس آن ساخته می‌شوند.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```
  
> **🤖 امتحان با چت [GitHub Copilot](https://github.com/features/copilot):** فایل [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) را باز کنید و بپرسید:  
> - "چگونه در این کد از مدل‌های GitHub به Azure OpenAI سوئیچ کنم؟"  
> - "چه پارامترهای دیگری را می‌توانم در OpenAiOfficialChatModel.builder() تنظیم کنم؟"  
> - "چگونه می‌توانم به جای انتظار برای پاسخ کامل، پاسخ‌های استریم شده اضافه کنم؟"

**مهندسی پرامپت** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

حالا که می‌دانید چگونه با مدل صحبت کنید، بیایید ببینیم چه چیزی به آن می‌گویید. این دمو از همان تنظیم مدل استفاده می‌کند اما پنج الگوی پرامپت مختلف را نشان می‌دهد. پرامپت‌های zero-shot برای دستورهای مستقیم، few-shot که از مثال‌ها یاد می‌گیرند، chain-of-thought که مراحل استدلال را نشان می‌دهد و پرامپت‌های مبتنی بر نقش که زمینه را تنظیم می‌کنند را امتحان کنید. خواهید دید همان مدل بسته به چارچوب بندی درخواست شما چطور پاسخ‌های بسیار متفاوتی می‌دهد.

این دمو همچنین الگوهای پرامپت را نشان می‌دهد که روشی قدرتمند برای ایجاد پرامپت‌های قابل استفاده مجدد با متغیرها هستند.  
مثال زیر پرامپتی را با استفاده از `PromptTemplate` در LangChain4j برای پر کردن متغیرها نشان می‌دهد. هوش مصنوعی بر اساس مقصد و فعالیت داده شده پاسخ خواهد داد.

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
  
> **🤖 امتحان با چت [GitHub Copilot](https://github.com/features/copilot):** فایل [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) را باز کنید و بپرسید:  
> - "تفاوت پرامپت zero-shot و few-shot چیست و چه زمانی هرکدام را باید استفاده کنم؟"  
> - "پارامتر دما چگونه روی پاسخ‌های مدل تاثیر می‌گذارد؟"  
> - "چند تکنیک برای جلوگیری از حملات تزریق پرامپت در تولید چیست؟"  
> - "چگونه می‌توانم اشیاء PromptTemplate قابل استفاده مجدد برای الگوهای رایج بسازم؟"

**ادغام ابزار** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

این‌جا LangChain4j قدرتمند می‌شود. شما از `AiServices` استفاده می‌کنید تا دستیار هوش مصنوعی بسازید که بتواند متدهای جاوای شما را فراخوانی کند. کافی است متدها را با `@Tool("توضیح")` حاشیه‌نویسی کنید و LangChain4j بقیه کارها را انجام می‌دهد – هوش مصنوعی به طور خودکار تصمیم می‌گیرد کی از هر ابزار استفاده کند بر اساس درخواست کاربر. این تکنیک فراخوانی تابع را نشان می‌دهد که کلید ساخت هوش مصنوعی است که می‌تواند اقدام انجام دهد نه فقط پاسخ دهد.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```
  
> **🤖 امتحان با چت [GitHub Copilot](https://github.com/features/copilot):** فایل [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) را باز کنید و بپرسید:  
> - "برچسب @Tool چگونه کار می‌کند و LangChain4j پشت صحنه با آن چه می‌کند؟"  
> - "آیا هوش مصنوعی می‌تواند چند ابزار را به ترتیب برای حل مسائل پیچیده فراخوانی کند؟"  
> - "اگر یک ابزار استثنا بیندازد چه اتفاقی می‌افتد – چطور باید خطاها را مدیریت کنم؟"  
> - "چگونه یک API واقعی را به جای این مثال ماشین حساب یکپارچه کنم؟"

**پرسش و پاسخ اسناد (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

اینجا RAG (تولید تقویت‌شده با بازیابی) با روش «Easy RAG» در LangChain4j را می‌بینید. اسناد بارگذاری، به صورت خودکار تقسیم و در یک حافظه درجا جاسازی می‌شوند، سپس بازیاب محتوا بخش‌های مرتبط را در زمان پرسش به هوش مصنوعی می‌دهد. هوش مصنوعی بر اساس اسناد شما پاسخ می‌دهد نه دانش عمومی خود.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```
  
> **🤖 امتحان با چت [GitHub Copilot](https://github.com/features/copilot):** فایل [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) را باز کنید و بپرسید:  
> - "چگونه RAG از هذیان‌های هوش مصنوعی جلوگیری می‌کند در مقایسه با استفاده از داده‌های آموزش مدل؟"  
> - "تفاوت این روش آسان با یک خط لوله RAG سفارشی چیست؟"  
> - "چگونه این را مقیاس‌پذیر کنم تا چندین سند یا پایگاه دانش بزرگ‌تر را اداره کند؟"

**هوش مصنوعی مسئولانه** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

ساخت ایمنی هوش مصنوعی با محافظت چندلایه. این دمو دو لایه حفاظت که با هم کار می‌کنند را نشان می‌دهد:

**بخش ۱: محافظ پایش ورودی LangChain4j** - پرامپت‌های خطرناک را قبل از رسیدن به مدل بزرگ زبان مسدود می‌کند. محافظ‌های سفارشی بسازید که کلمات یا الگوهای ممنوعه را بررسی کنند. این‌ها در کد شما اجرا می‌شوند، پس سریع و رایگان‌اند.

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
  
**بخش ۲: فیلترهای ایمنی ارائه‌دهنده** - مدل‌های GitHub دارای فیلترهای داخلی هستند که مواردی را که محافظ‌های شما ممکن است از دست بدهند، می‌گیرند. بلوک‌های سخت (خطای HTTP 400) برای تخلفات شدید و ردهای نرم که هوش مصنوعی مودبانه امتناع می‌کند را می‌بینید.

> **🤖 امتحان با چت [GitHub Copilot](https://github.com/features/copilot):** فایل [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) را باز کنید و بپرسید:  
> - "InputGuardrail چیست و چگونه می‌توانم محافظ خودم را بسازم؟"  
> - "تفاوت بلوک سخت و رد نرم چیست؟"  
> - "چرا باید هم محافظ‌ها و هم فیلترهای ارائه‌دهنده را با هم استفاده کرد؟"

## گام‌های بعدی

**ماژول بعدی:** [۰۱-مقدمه - شروع کار با LangChain4j](../01-introduction/README.md)

---

**ناوبری:** [← بازگشت به اصلی](../README.md) | [بعدی: ماژول ۰۱ - مقدمه →](../01-introduction/README.md)

---

## عیب‌یابی

### ساخت نخستین‌بار Maven

**مشکل:** اجرای اولیه `mvn clean compile` یا `mvn package` زمان زیادی می‌برد (۱۰-۱۵ دقیقه)

**علت:** Maven نیاز دارد تمام وابستگی‌های پروژه (Spring Boot، کتابخانه‌های LangChain4j، SDKهای Azure و غیره) را برای اولین ساخت دانلود کند.

**راه حل:** این رفتار عادی است. ساخت‌های بعدی بسیار سریع‌تر خواهند بود چون وابستگی‌ها به صورت محلی کش می‌شوند. زمان دانلود بستگی به سرعت شبکه شما دارد.

### نحو دستورات Maven در PowerShell

**مشکل:** دستورات Maven با خطای `Unknown lifecycle phase ".mainClass=..."` شکست می‌خورند
**علت**: پاورشل `=` را به عنوان عملگر انتساب متغیر تفسیر می‌کند که باعث خراب شدن نحو خاصیت Maven می‌شود

**راه‌حل**: از عملگر توقف پارس `--%` قبل از فرمان Maven استفاده کنید:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

عملگر `--%` به پاورشل می‌گوید که تمام آرگومان‌های باقی‌مانده را بدون تفسیر به صورت دقیق به Maven ارسال کند.

### نمایش شکلک در Windows PowerShell

**مشکل**: پاسخ‌های AI به جای شکلک‌ها کاراکترهای نامفهوم (مثل `????` یا `â??`) را نمایش می‌دهند در پاورشل

**علت**: رمزگذاری پیش‌فرض پاورشل از شکلک‌های UTF-8 پشتیبانی نمی‌کند

**راه‌حل**: این دستور را قبل از اجرای برنامه‌های Java اجرا کنید:
```cmd
chcp 65001
```

این باعث اجبار رمزگذاری UTF-8 در ترمینال می‌شود. همچنین می‌توانید از Windows Terminal که پشتیبانی بهتری از یونیکد دارد استفاده کنید.

### اشکال‌زدایی تماس‌های API

**مشکل**: خطاهای احراز هویت، محدودیت‌های نرخ، یا پاسخ‌های غیرمنتظره از مدل AI

**راه‌حل**: مثال‌ها شامل `.logRequests(true)` و `.logResponses(true)` هستند تا تماس‌های API را در کنسول نشان دهند. این کمک می‌کند تا خطاهای احراز هویت، محدودیت‌های نرخ، یا پاسخ‌های غیرمنتظره را عیب‌یابی کنید. این گزینه‌ها را در محیط تولید حذف کنید تا حجم لاگ کاهش یابد.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**سلب مسئولیت**:  
این سند با استفاده از سرویس ترجمه هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما در پی دقت هستیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است حاوی خطاها یا نادقتی‌هایی باشند. سند اصلی به زبان بومی خود باید به‌عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حیاتی، استفاده از ترجمه حرفه‌ای انسانی توصیه می‌شود. ما مسئول هیچ گونه سوءتفاهم یا تفسیر نادرستی که از کاربرد این ترجمه ناشی شود، نمی‌باشیم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->