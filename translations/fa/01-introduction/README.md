# ماژول ۰۱: شروع به کار با LangChain4j

## فهرست مطالب

- [آموزش ویدیویی](../../../01-introduction)
- [آنچه خواهید آموخت](../../../01-introduction)
- [پیش‌نیازها](../../../01-introduction)
- [درک مسئله اصلی](../../../01-introduction)
- [درک توکن‌ها](../../../01-introduction)
- [نحوه کارکرد حافظه](../../../01-introduction)
- [نحوه استفاده از LangChain4j](../../../01-introduction)
- [استقرار زیرساخت Azure OpenAI](../../../01-introduction)
- [اجرای برنامه به‌صورت محلی](../../../01-introduction)
- [استفاده از برنامه](../../../01-introduction)
  - [چت بدون حالت (پنل سمت چپ)](../../../01-introduction)
  - [چت با حالت (پنل سمت راست)](../../../01-introduction)
- [گام‌های بعدی](../../../01-introduction)

## آموزش ویدیویی

این جلسه زنده را تماشا کنید که توضیح می‌دهد چگونه با این ماژول شروع کنید:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## آنچه خواهید آموخت

اگر شروع سریع را تکمیل کرده‌اید، دیدید چگونه پرامپت‌ها را ارسال کرده و پاسخ دریافت کنید. این پایه است، اما برنامه‌های واقعی بیشتر نیاز دارند. این ماژول به شما می‌آموزد چگونه هوش مصنوعی محاوره‌ای بسازید که زمینه را به یاد داشته و وضعیت را حفظ کند - تفاوت بین یک دموی یک‌بار مصرف و یک برنامه آماده تولید.

ما در طول این راهنما از GPT-5.2 Azure OpenAI استفاده خواهیم کرد چون توانایی‌های پیشرفته استدلال آن باعث می‌شود رفتار الگوهای مختلف واضح‌تر شود. وقتی حافظه اضافه می‌کنید، تفاوت را به وضوح خواهید دید. این باعث می‌شود درک اینکه هر مولفه چه چیزی به برنامه شما می‌آورد آسان‌تر شود.

شما یک برنامه خواهید ساخت که هر دو الگو را نشان دهد:

**چت بدون حالت** - هر درخواست مستقل است. مدل هیچ حافظه‌ای از پیام‌های قبلی ندارد. این همان الگویی است که در شروع سریع استفاده کردید.

**گفتگوی با حالت** - هر درخواست شامل تاریخچه گفتگو است. مدل زمینه را در چند نوبت حفظ می‌کند. این چیزی است که برنامه‌های تولیدی نیاز دارند.

## پیش‌نیازها

- اشتراک Azure با دسترسی به Azure OpenAI
- Java 21، Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **توجه:** Java، Maven، Azure CLI و Azure Developer CLI (azd) در devcontainer ارائه شده از قبل نصب شده‌اند.

> **توجه:** این ماژول از GPT-5.2 در Azure OpenAI استفاده می‌کند. استقرار به‌طور خودکار از طریق `azd up` پیکربندی شده است - نام مدل را در کد تغییر ندهید.

## درک مسئله اصلی

مدل‌های زبانی بدون حالت هستند. هر تماس API مستقل است. اگر بگویید «اسم من جان است» و بعد بپرسید «اسم من چیست؟»، مدل هیچ ایده‌ای ندارد که شما تازه خودتان را معرفی کرده‌اید. هر درخواست را همانند اولین گفتگوی شما در نظر می‌گیرد.

این برای پرسش و پاسخ ساده خوب است اما برای برنامه‌های واقعی بی‌فایده است. ربات‌های خدمات مشتری باید به یاد داشته باشند چه گفتید. دستیاران شخصی به زمینه نیاز دارند. هر گفتگوی چند نوبتی حافظه نیاز دارد.

<img src="../../../translated_images/fa/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="گفتگوهای بدون حالت در مقابل با حالت" width="800"/>

*تفاوت بین گفتگوهای بدون حالت (تماس‌های مستقل) و با حالت (آگاه به زمینه)*

## درک توکن‌ها

قبل از ورود به گفتگوها، مهم است که توکن‌ها را بشناسید - واحدهای پایه متن که مدل‌های زبانی پردازش می‌کنند:

<img src="../../../translated_images/fa/token-explanation.c39760d8ec650181.webp" alt="توضیح توکن" width="800"/>

*مثالی از نحوه شکستن متن به توکن‌ها - "I love AI!" به ۴ واحد جداگانه پردازش تبدیل می‌شود*

توکن‌ها نحوه اندازه‌گیری و پردازش متن توسط مدل‌های هوش مصنوعی هستند. کلمات، نقطه‌گذاری و حتی فاصله‌ها می‌توانند توکن باشند. مدل شما محدودیتی دارد که چند توکن را می‌تواند همزمان پردازش کند (۴۰۰,۰۰۰ برای GPT-5.2، با حداکثر ۲۷۲,۰۰۰ توکن ورودی و ۱۲۸,۰۰۰ توکن خروجی). درک توکن‌ها به شما کمک می‌کند طول گفتگو و هزینه‌ها را مدیریت کنید.

## نحوه کارکرد حافظه

حافظه چت مشکل بدون حالت بودن را با حفظ تاریخچه گفتگو حل می‌کند. قبل از ارسال درخواست به مدل، چارچوب پیام‌های قبلی مرتبط را اضافه می‌کند. وقتی می‌پرسید «اسم من چیست؟»، در واقع کل تاریخچه گفتگو ارسال می‌شود و مدل می‌بیند شما قبلاً گفته‌اید «اسم من جان است.»

LangChain4j پیاده‌سازی‌های حافظه‌ای ارائه می‌دهد که این کار را خودکار انجام می‌دهند. شما تعیین می‌کنید چند پیام نگهداری شود و چارچوب پنجره زمینه را مدیریت می‌کند.

<img src="../../../translated_images/fa/memory-window.bbe67f597eadabb3.webp" alt="مفهوم پنجره حافظه" width="800"/>

*MessageWindowChatMemory پنجره لغزنده‌ای از پیام‌های اخیر نگه می‌دارد و قدیمی‌ها را خودکار حذف می‌کند*

## نحوه استفاده از LangChain4j

این ماژول شروع سریع را با یکپارچه‌سازی Spring Boot و افزودن حافظه گفتگو گسترش می‌دهد. این‌گونه قطعات کنار هم قرار می‌گیرند:

**وابستگی‌ها** - افزودن دو کتابخانه LangChain4j:

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

**مدل چت** - تنظیم Azure OpenAI به عنوان یک Bean در Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

سازنده اعتبارنامه‌ها را از متغیرهای محیطی که توسط `azd up` تنظیم شده‌اند می‌خواند. تنظیم `baseUrl` به نقطه پایانی Azure شما باعث می‌شود کلاینت OpenAI با Azure OpenAI کار کند.

**حافظه گفتگو** - پیگیری تاریخچه گفتگو با MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

حافظه را با `withMaxMessages(10)` ایجاد کنید تا ۱۰ پیام آخر نگه داشته شود. پیام‌های کاربر و هوش مصنوعی را با پوشش‌دهنده‌های تایپ‌شده اضافه کنید: `UserMessage.from(text)` و `AiMessage.from(text)`. تاریخچه را با `memory.messages()` دریافت و به مدل ارسال کنید. سرویس نمونه‌های حافظه جداگانه برای هر شناسه گفتگو ذخیره می‌کند که امکان چت همزمان چند کاربر را فراهم می‌آورد.

> **🤖 امتحان کنید با چت [GitHub Copilot](https://github.com/features/copilot):** فایل [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) را باز کنید و بپرسید:
> - "MessageWindowChatMemory چگونه تصمیم می‌گیرد پیام‌ها را وقتی پنجره پر است حذف کند؟"
> - "آیا می‌توانم حافظه سفارشی با استفاده از پایگاه داده به جای حافظه داخلی پیاده کنم؟"
> - "چگونه خلاصه‌سازی برای فشرده‌سازی تاریخچه قدیمی گفتگو اضافه کنم؟"

نقطه اتصال چت بدون حالت کاملاً حافظه را نادیده می‌گیرد - فقط `chatModel.chat(prompt)` مثل شروع سریع. نقطه اتصال با حالت پیام‌ها را به حافظه اضافه می‌کند، تاریخچه را بازیابی کرده و آن زمینه را با هر درخواست می‌فرستد. همان پیکربندی مدل، الگوهای متفاوت.

## استقرار زیرساخت Azure OpenAI

**بش:**
```bash
cd 01-introduction
azd up  # اشتراک و مکان را انتخاب کنید (eastus2 توصیه شده)
```

**پاورشل:**
```powershell
cd 01-introduction
azd up  # اشتراک و موقعیت را انتخاب کنید (توصیه شده eastus2)
```

> **توجه:** اگر با خطای تایم‌اوت (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`) مواجه شدید، فقط دوباره `azd up` را اجرا کنید. ممکن است منابع Azure هنوز در حال استقرار پس‌زمینه باشند و تکرار اجرای آن اجازه می‌دهد استقرار وقتی منابع به وضعیت نهایی رسیدند کامل شود.

این کار موارد زیر را انجام می‌دهد:
1. استقرار منبع Azure OpenAI با مدل‌های GPT-5.2 و text-embedding-3-small
2. تولید خودکار فایل `.env` در ریشه پروژه با اعتبارنامه‌ها
3. تنظیم تمام متغیرهای محیطی مورد نیاز

**مشکل در استقرار دارید؟** راهنمای [README زیرساخت](infra/README.md) را ببینید برای رفع مشکلات از جمله تعارض نام زیر دامنه، مراحل دستی استقرار در پرتال Azure و راهنمای پیکربندی مدل.

**اطمینان از موفقیت استقرار:**

**بش:**
```bash
cat ../.env  # باید AZURE_OPENAI_ENDPOINT، API_KEY و غیره را نمایش دهد.
```

**پاورشل:**
```powershell
Get-Content ..\.env  # باید AZURE_OPENAI_ENDPOINT، API_KEY و غیره را نشان دهد.
```

> **توجه:** دستور `azd up` فایل `.env` را خودکار تولید می‌کند. اگر لازم بود بعداً آن را به‌روزرسانی کنید، یا فایل `.env` را دستی ویرایش کنید یا با اجرای:
>
> **بش:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **پاورشل:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## اجرای برنامه به‌صورت محلی

**اطمینان از استقرار:**

اطمینان حاصل کنید فایل `.env` در دایرکتوری ریشه با اعتبارنامه Azure موجود است:

**بش:**
```bash
cat ../.env  # باید AZURE_OPENAI_ENDPOINT، API_KEY، DEPLOYMENT را نمایش دهد
```

**پاورشل:**
```powershell
Get-Content ..\.env  # باید AZURE_OPENAI_ENDPOINT، API_KEY، DEPLOYMENT را نمایش دهد
```

**شروع برنامه‌ها:**

**گزینه ۱: استفاده از Spring Boot Dashboard (توصیه شده برای کاربران VS Code)**

کانتینر توسعه شامل افزونه Spring Boot Dashboard است که رابط گرافیکی برای مدیریت همه برنامه‌های Spring Boot فراهم می‌کند. از نوار فعالیت سمت چپ VS Code آن را می‌یابید (آیکون Spring Boot را پیدا کنید).

از Spring Boot Dashboard می‌توانید:
- همه برنامه‌های Spring Boot موجود در محیط کاری را ببینید
- برنامه‌ها را با یک کلیک اجرا/توقف کنید
- گزارش‌های برنامه را به‌صورت زنده مشاهده کنید
- وضعیت برنامه‌ها را پایش کنید

فقط روی دکمه پخش کنار "introduction" کلیک کنید تا این ماژول اجرا شود، یا همه ماژول‌ها را هم‌زمان شروع کنید.

<img src="../../../translated_images/fa/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**گزینه ۲: استفاده از اسکریپت‌های شل**

همه برنامه‌های وب (ماژول‌های ۰۱-۰۴) را اجرا کنید:

**بش:**
```bash
cd ..  # از دایرکتوری ریشه
./start-all.sh
```

**پاورشل:**
```powershell
cd ..  # از شاخه ریشه
.\start-all.ps1
```

یا فقط این ماژول را اجرا کنید:

**بش:**
```bash
cd 01-introduction
./start.sh
```

**پاورشل:**
```powershell
cd 01-introduction
.\start.ps1
```

هر دو اسکریپت متغیرهای محیطی را از فایل `.env` ریشه بارگذاری می‌کنند و فایل‌های JAR را می‌سازند اگر موجود نباشند.

> **توجه:** اگر ترجیح می‌دهید قبل از شروع همه ماژول‌ها را دستی بسازید:
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

آدرس http://localhost:8080 را در مرورگر باز کنید.

**برای توقف:**

**بش:**
```bash
./stop.sh  # فقط این ماژول
# یا
cd .. && ./stop-all.sh  # تمام ماژول‌ها
```

**پاورشل:**
```powershell
.\stop.ps1  # فقط این ماژول
# یا
cd ..; .\stop-all.ps1  # همه ماژول‌ها
```

## استفاده از برنامه

برنامه یک رابط وب با دو پیاده‌سازی چت کنار هم ارائه می‌دهد.

<img src="../../../translated_images/fa/home-screen.121a03206ab910c0.webp" alt="صفحه اصلی برنامه" width="800"/>

*داشبوردی که هر دو گزینه چت ساده (بدون حالت) و چت محاوره‌ای (با حالت) را نشان می‌دهد*

### چت بدون حالت (پنل سمت چپ)

اول این را امتحان کنید. بگویید «اسم من جان است» و بلافاصله بپرسید «اسم من چیست؟» مدل به خاطر نمی‌آورد چون هر پیام مستقل است. این مشکل اصلی ادغام پایه مدل‌های زبانی است - نبود زمینه گفتگو.

<img src="../../../translated_images/fa/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="نمایش چت بدون حالت" width="800"/>

*هوش مصنوعی اسم شما را از پیام قبلی به یاد نمی‌آورد*

### چت با حالت (پنل سمت راست)

اکنون همان دنباله را اینجا امتحان کنید. بگویید «اسم من جان است» و سپس بپرسید «اسم من چیست؟» این بار به یاد می‌آورد. تفاوت MessageWindowChatMemory است - تاریخچه گفتگو را حفظ می‌کند و با هر درخواست آن را شامل می‌کند. این نحوه عملکرد هوش مصنوعی محاوره‌ای در تولید است.

<img src="../../../translated_images/fa/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="نمایش چت با حالت" width="800"/>

*هوش مصنوعی اسم شما را از گفتگوهای قبلی به یاد می‌آورد*

هر دو پنل از همان مدل GPT-5.2 استفاده می‌کنند. تنها تفاوت حافظه است. این واضح می‌کند حافظه چه چیزی به برنامه شما می‌آورد و چرا برای موارد واقعی ضروری است.

## گام‌های بعدی

**ماژول بعدی:** [۰۲-مهندسی-پرامپت - مهندسی پرامپت با GPT-5.2](../02-prompt-engineering/README.md)

---

**ناوبری:** [← قبلی: ماژول ۰۰ - شروع سریع](../00-quick-start/README.md) | [بازگشت به اصلی](../README.md) | [بعدی: ماژول ۰۲ - مهندسی پرامپت →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**توضیح مهم**:
این سند با استفاده از سرویس ترجمه هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما در تلاش برای دقت هستیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است حاوی خطاها یا نواقصی باشند. سند اصلی به زبان مادری آن باید به عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حساس، استفاده از ترجمه حرفه‌ای انسانی توصیه می‌شود. ما مسئول هیچ گونه سوءتفاهم یا برداشت نادرست ناشی از استفاده از این ترجمه نیستیم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->