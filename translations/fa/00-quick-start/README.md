# ماژول ۰۰: شروع سریع

## فهرست مطالب

- [مقدمه](../../../00-quick-start)
- [LangChain4j چیست؟](../../../00-quick-start)
- [وابستگی‌های LangChain4j](../../../00-quick-start)
- [پیش‌نیازها](../../../00-quick-start)
- [راه‌اندازی](../../../00-quick-start)
  - [۱. دریافت توکن گیت‌هاب](../../../00-quick-start)
  - [۲. تنظیم توکن](../../../00-quick-start)
- [اجرای نمونه‌ها](../../../00-quick-start)
  - [۱. چت پایه](../../../00-quick-start)
  - [۲. الگوهای پرامپت](../../../00-quick-start)
  - [۳. فراخوانی تابع](../../../00-quick-start)
  - [۴. سؤال و جواب مستند (Easy RAG)](../../../00-quick-start)
  - [۵. هوش مصنوعی مسئول](../../../00-quick-start)
- [هر نمونه چه چیزی را نشان می‌دهد](../../../00-quick-start)
- [گام‌های بعدی](../../../00-quick-start)
- [عیب‌یابی](../../../00-quick-start)

## مقدمه

این شروع سریع برای این است که شما را هرچه سریع‌تر با LangChain4j راه‌اندازی کند. این سند مبانی مطلق ساخت برنامه‌های هوش مصنوعی با LangChain4j و مدل‌های گیت‌هاب را پوشش می‌دهد. در ماژول‌های بعدی، شما از Azure OpenAI با LangChain4j برای ساخت برنامه‌های پیشرفته‌تر استفاده خواهید کرد.

## LangChain4j چیست؟

LangChain4j یک کتابخانه جاوا است که ساخت برنامه‌های مبتنی بر هوش مصنوعی را ساده می‌کند. به جای کار با کلاینت‌های HTTP و تجزیه JSON، شما با APIهای جاوای تمیز کار می‌کنید.

"زنجیره" در LangChain به کنار هم قرار دادن چند مؤلفه اشاره دارد - ممکن است یک پرامپت را به مدل و سپس به یک پارسر زنجیر کنید، یا چند فراخوانی AI را به هم متصل کنید که خروجی یکی ورودی بعدی باشد. این شروع سریع روی اصول اولیه تمرکز دارد قبل از اینکه به زنجیره‌های پیچیده‌تر بپردازد.

<img src="../../../translated_images/fa/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*زنجیره‌سازی مؤلفه‌ها در LangChain4j - بلوک‌های ساختمانی به هم متصل می‌شوند تا جریان‌های کاری قدرتمند هوش مصنوعی بسازند*

ما از سه مؤلفه اصلی استفاده خواهیم کرد:

**ChatModel** – رابط تعامل با مدل هوش مصنوعی. با فراخوانی `model.chat("prompt")` یک رشته پاسخ دریافت کنید. ما از `OpenAiOfficialChatModel` استفاده می‌کنیم که با نقطه انتهایی‌های سازگار OpenAI مانند مدل‌های گیت‌هاب کار می‌کند.

**AiServices** – ایجاد رابط‌های سرویس هوش مصنوعی نوع ایمن. متدها را تعریف کنید، با `@Tool` آنها را علامت‌گذاری کنید، و LangChain4j هماهنگی را مدیریت می‌کند. هوش مصنوعی به صورت خودکار هنگام نیاز متدهای جاوای شما را فراخوانی می‌کند.

**MessageWindowChatMemory** – تاریخچه گفتگو را حفظ می‌کند. بدون آن هر درخواست مستقل است. با آن، هوش مصنوعی پیام‌های قبلی را به خاطر می‌سپارد و زمینه را در چند نوبت حفظ می‌کند.

<img src="../../../translated_images/fa/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*معماری LangChain4j - مؤلفه‌های اصلی که با هم کار می‌کنند تا برنامه‌های هوش مصنوعی شما را فعال کنند*

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

ماژول `langchain4j-open-ai-official` کلاس `OpenAiOfficialChatModel` را فراهم می‌کند که به APIهای سازگار با OpenAI متصل می‌شود. مدل‌های گیت‌هاب از همان فرمت API استفاده می‌کنند، بنابراین نیازی به آداپتور خاصی نیست - فقط آدرس پایه را به `https://models.github.ai/inference` اشاره دهید.

ماژول `langchain4j-easy-rag` تقسیم‌بندی خودکار سند، جاسازی و بازیابی را فراهم می‌کند تا بتوانید برنامه‌های RAG بسازید بدون اینکه هر مرحله را دستی پیکربندی کنید.

## پیش‌نیازها

**استفاده از کانتینر توسعه؟** جاوا و Maven از قبل نصب شده‌اند. تنها نیاز به توکن دسترسی شخصی گیت‌هاب دارید.

**توسعه محلی:**
- جاوا ۲۱ به بالا، Maven ۳.۹ به بالا
- توکن دسترسی شخصی گیت‌هاب (دستورالعمل‌ها در زیر)

> **توجه:** این ماژول از مدل `gpt-4.1-nano` از مدل‌های گیت‌هاب استفاده می‌کند. نام مدل در کد را تغییر ندهید – این پیکربندی‌شده است که با مدل‌های موجود گیت‌هاب کار کند.

## راه‌اندازی

### ۱. دریافت توکن گیت‌هاب

1. به [تنظیمات گیت‌هاب → توکن‌های دسترسی شخصی](https://github.com/settings/personal-access-tokens) بروید
2. روی "Generate new token" کلیک کنید
3. یک نام توصیفی تنظیم کنید (مثلاً "LangChain4j Demo")
4. انقضا را تنظیم کنید (۷ روز توصیه می‌شود)
5. در بخش "Account permissions"، گزینه "Models" را روی "فقط خواندنی" قرار دهید
6. روی "Generate token" کلیک کنید
۷. توکن را کپی و ذخیره کنید - دیگر قابل مشاهده نیست

### ۲. تنظیم توکن

**گزینه ۱: استفاده از VS Code (توصیه شده)**

اگر از VS Code استفاده می‌کنید، توکن خود را در فایل `.env` در ریشه پروژه اضافه کنید:

اگر فایل `.env` وجود نداشت، `.env.example` را به `.env` کپی کنید یا یک فایل `.env` جدید در ریشه پروژه بسازید.

**مثال فایل `.env`:**
```bash
# در /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

پس به راحتی می‌توانید روی هر فایل دمو (مانند `BasicChatDemo.java`) در اکسپلورر کلیک راست کرده و گزینه **"Run Java"** را انتخاب کنید یا از پیکربندی‌های راه‌اندازی در پنل Run و Debug استفاده کنید.

**گزینه ۲: استفاده از ترمینال**

توکن را به عنوان متغیر محیطی تنظیم کنید:

**بش:**
```bash
export GITHUB_TOKEN=your_token_here
```

**پاورشل:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## اجرای نمونه‌ها

**استفاده از VS Code:** به سادگی روی هر فایل دمو در اکسپلورر کلیک راست کرده و گزینه **"Run Java"** را انتخاب کنید، یا از پیکربندی‌های راه‌اندازی در پنل Run و Debug استفاده کنید (ابتدا باید توکن خود را در فایل `.env` اضافه کرده باشید).

**استفاده از Maven:** به صورت جایگزین، می‌توانید از خط فرمان اجرا کنید:

### ۱. چت پایه

**بش:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**پاورشل:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### ۲. الگوهای پرامپت

**بش:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**پاورشل:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

الگوهای صفر-شات، چند-شات، زنجیره افکار و نقش‌محور را نشان می‌دهد.

### ۳. فراخوانی تابع

**بش:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**پاورشل:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

هوش مصنوعی به طور خودکار هنگام نیاز متدهای جاوای شما را فراخوانی می‌کند.

### ۴. سؤال و جواب مستند (Easy RAG)

**بش:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**پاورشل:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

با استفاده از Easy RAG به همراه جاسازی و بازیابی خودکار، سوالاتی درباره اسناد خود بپرسید.

### ۵. هوش مصنوعی مسئول

**بش:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**پاورشل:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

ببینید چگونه فیلترهای ایمنی هوش مصنوعی محتوای مضر را مسدود می‌کنند.

## هر نمونه چه چیزی را نشان می‌دهد

**چت پایه** – [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

از اینجا شروع کنید تا ساده‌ترین حالت LangChain4j را ببینید. یک `OpenAiOfficialChatModel` ایجاد می‌کنید، یک پرامپت با `.chat()` ارسال می‌کنید، و پاسخ دریافت می‌کنید. این نشان‌دهنده پایه است: چگونه مدل‌ها را با نقاط انتهایی سفارشی و کلیدهای API مقداردهی اولیه کنید. وقتی این الگو را فهمیدید، همه چیز بر اساس آن ساخته می‌شود.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 امتحان با [GitHub Copilot](https://github.com/features/copilot) Chat:** فایل [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) را باز کنید و بپرسید:
> - "چگونه در این کد از مدل‌های گیت‌هاب به Azure OpenAI سوئیچ کنم؟"
> - "چه پارامترهای دیگری را می‌توانم در OpenAiOfficialChatModel.builder() تنظیم کنم؟"
> - "چگونه پاسخ‌های استریمینگ اضافه کنم به جای انتظار برای پاسخ کامل؟"

**مهندسی پرامپت** – [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

حالا که می‌دانید چگونه با مدل صحبت کنید، بیایید ببینیم چه چیزی به آن می‌گویید. این دمو همان پیکربندی مدل را استفاده می‌کند اما پنج الگوی پرامپت مختلف را نشان می‌دهد. پرامپت‌های صفر-شات برای دستورالعمل‌های مستقیم، چند-شات برای یادگیری از نمونه‌ها، زنجیره‌افکار برای آشکار کردن مراحل استدلال و نقش‌محور که زمینه را تنظیم می‌کند. خواهید دید که همان مدل نتایج متفاوتی بر اساس نحوه چارچوب‌بندی درخواست ارائه می‌دهد.

دمو همچنین قالب‌های پرامپت را نشان می‌دهد، که راهی قدرتمند برای ساخت پرامپت‌های قابل استفاده مجدد با متغیرها است.
مثال زیر پرامپتی را نشان می‌دهد که از `PromptTemplate` در LangChain4j برای پر کردن متغیرها استفاده می‌کند. هوش مصنوعی بر اساس مقصد و فعالیت ارائه‌شده پاسخ می‌دهد.

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
> - "تفاوت پرامپت صفر-شات و چند-شات چیست و کی باید از هرکدام استفاده کنم؟"
> - "پارامتر دما چگونه پاسخ‌های مدل را تحت تأثیر قرار می‌دهد؟"
> - "برخی تکنیک‌ها برای جلوگیری از حملات تزریق پرامپت در تولید چیست؟"
> - "چگونه می‌توانم اشیاء PromptTemplate قابل استفاده مجدد برای الگوهای رایج بسازم؟"

**یکپارچه‌سازی ابزار** – [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

اینجاست که LangChain4j قدرتمند می‌شود. از `AiServices` استفاده خواهید کرد تا دستیار هوش مصنوعی بسازید که می‌تواند متدهای جاوای شما را فراخوانی کند. کافی است متدها را با `@Tool("توضیح")` علامت‌گذاری کنید و LangChain4j بقیه کار را انجام می‌دهد - هوش مصنوعی خودش تصمیم می‌گیرد چه زمانی هر ابزار را بر اساس درخواست کاربر استفاده کند. این باعث می‌شود که فراخوانی تابع، تکنیکی کلیدی برای ساخت هوش مصنوعی باشد که می‌تواند عمل کند، نه فقط به سوالات پاسخ دهد.

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

> **🤖 امتحان با [GitHub Copilot](https://github.com/features/copilot) Chat:** فایل [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) را باز کنید و بپرسید:
> - "یادداشت @Tool چگونه کار می‌کند و LangChain4j پشت صحنه با آن چه می‌کند؟"
> - "آیا هوش مصنوعی می‌تواند چند ابزار را به ترتیب فراخوانی کند تا مسائل پیچیده را حل کند؟"
> - "اگر یک ابزار استثنا پرتاب کند، چگونه باید خطاها را مدیریت کنم؟"
> - "چگونه یک API واقعی را به جای این مثال ماشین حساب یکپارچه کنم؟"

**سؤال و جواب مستند (Easy RAG)** – [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

در اینجا RAG (تولید افزوده بازیابی) را با رویکرد "Easy RAG" در LangChain4j می‌بینید. اسناد بارگذاری می‌شوند، به صورت خودکار تقسیم و در یک حافظه درون‌برنامه جاسازی می‌شوند، سپس بازیاب محتوا قطعات مرتبط را هنگام پرس و جو به هوش مصنوعی می‌دهد. هوش مصنوعی بر اساس اسناد شما پاسخ می‌دهد، نه دانش عمومی خود.

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

> **🤖 امتحان با [GitHub Copilot](https://github.com/features/copilot) Chat:** فایل [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) را باز کنید و بپرسید:
> - "چگونه RAG جلوی توهمات هوش مصنوعی را نسبت به استفاده از داده‌های آموزش مدل می‌گیرد؟"
> - "تفاوت این رویکرد آسان با یک خط لوله RAG سفارشی چیست؟"
> - "چگونه این را برای مدیریت چند سند یا پایگاه‌های دانش بزرگتر مقیاس می‌دهم؟"

**هوش مصنوعی مسئول** – [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

هوش مصنوعی امن با دفاع چند لایه بسازید. این دمو دو لایه محافظت را که با هم کار می‌کنند نشان می‌دهد:

**قسمت ۱: حصارهای ورودی LangChain4j** – قبل از رسیدن به LLM پرامپت‌های خطرناک را مسدود می‌کند. حصارهای سفارشی ایجاد کنید که کلمات کلیدی یا الگوهای ممنوع را بررسی کنند. این‌ها در کد شما اجرا می‌شوند، بنابراین سریع و رایگان هستند.

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

**قسمت ۲: فیلترهای ایمنی ارائه‌دهنده** – مدل‌های گیت‌هاب فیلترهایی داخلی دارند که آنچه حصارهای شما ممکن است از دست بدهند را می‌گیرند. بلوک‌های سخت (خطاهای HTTP 400) برای موارد شدید و ردهای نرم که هوش مصنوعی مودبانه امتناع می‌کند را خواهید دید.

> **🤖 امتحان با [GitHub Copilot](https://github.com/features/copilot) Chat:** فایل [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) را باز کنید و بپرسید:
> - "InputGuardrail چیست و چگونه یکی بسازم؟"
> - "تفاوت بلوک سخت و امتناع نرم چیست؟"
> - "چرا باید هم حصارها و هم فیلترهای ارائه‌دهنده را با هم استفاده کرد؟"

## گام‌های بعدی

**ماژول بعدی:** [۰۱-مقدمه - شروع کار با LangChain4j و gpt-5 روی Azure](../01-introduction/README.md)

---

**ناوبری:** [← بازگشت به اصلی](../README.md) | [بعدی: ماژول ۰۱ - مقدمه →](../01-introduction/README.md)

---

## عیب‌یابی

### اولین ساخت Maven

**مشکل:** اجرای اولیه `mvn clean compile` یا `mvn package` طولانی است (۱۰ تا ۱۵ دقیقه)

**دلیل:** Maven باید همه وابستگی‌های پروژه (Spring Boot، کتابخانه‌های LangChain4j، SDKهای Azure و غیره) را در اولین ساخت دانلود کند.

**راه حل:** این رفتار طبیعی است. ساخت‌های بعدی خیلی سریع‌تر خواهند بود زیرا وابستگی‌ها به صورت محلی کش می‌شوند. زمان دانلود به سرعت شبکه شما بستگی دارد.

### نحو دستورات Maven در PowerShell

**مشکل:** دستورات Maven با خطای `Unknown lifecycle phase ".mainClass=..."` مواجه می‌شوند.
**علت**: پاورشل عملگر `=` را به عنوان عملگر انتساب متغیر تفسیر می‌کند که باعث خراب شدن نحو ویژگی‌های Maven می‌شود

**راه‌حل**: از عملگر توقف تجزیه `--%` قبل از فرمان Maven استفاده کنید:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

عملگر `--%` به PowerShell می‌گوید که همه آرگومان‌های باقی‌ مانده را بدون تفسیر به صورت حرفی به Maven منتقل کند.

### نمایش ایموجی در Windows PowerShell

**مشکل**: پاسخ‌های هوش مصنوعی به صورت کاراکترهای نامفهوم (مثلاً `????` یا `â??`) به جای ایموجی‌ها در PowerShell نمایش داده می‌شوند

**علت**: کدگذاری پیش‌فرض PowerShell از ایموجی‌های UTF-8 پشتیبانی نمی‌کند

**راه‌حل**: این دستور را قبل از اجرای برنامه‌های جاوا اجرا کنید:
```cmd
chcp 65001
```

این دستور کدگذاری UTF-8 را در ترمینال اجباری می‌کند. به طور جایگزین، از Windows Terminal استفاده کنید که پشتیبانی بهتری از یونیکد دارد.

### اشکال‌زدایی تماس‌های API

**مشکل**: خطاهای احراز هویت، محدودیت‌های نرخ، یا پاسخ‌های غیرمنتظره از مدل هوش مصنوعی

**راه‌حل**: نمونه‌ها شامل `.logRequests(true)` و `.logResponses(true)` هستند تا تماس‌های API را در کنسول نمایش دهند. این کار به عیب‌یابی خطاهای احراز هویت، محدودیت نرخ، یا پاسخ‌های غیرمنتظره کمک می‌کند. در محیط تولید این گزینه‌ها را حذف کنید تا نویز لاگ کاهش یابد.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**سلب مسئولیت**:
این سند با استفاده از سرویس ترجمه هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما تلاش می‌کنیم دقت را رعایت کنیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است حاوی خطاها یا نواقصی باشند. سند اصلی به زبان مادری آن باید به عنوان منبع موثق در نظر گرفته شود. برای اطلاعات حیاتی، استفاده از ترجمه حرفه‌ای انسانی توصیه می‌شود. ما در قبال هر گونه سوءتفاهم یا برداشت نادرست ناشی از استفاده از این ترجمه مسئولیتی نداریم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->