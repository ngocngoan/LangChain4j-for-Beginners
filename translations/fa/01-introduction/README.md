# ماژول ۰۱: شروع کار با LangChain4j

## فهرست مطالب

- [نمایش ویدئویی](../../../01-introduction)
- [آنچه یاد خواهید گرفت](../../../01-introduction)
- [پیش‌نیازها](../../../01-introduction)
- [درک مشکل اصلی](../../../01-introduction)
- [درک توکن‌ها](../../../01-introduction)
- [نحوه عملکرد حافظه](../../../01-introduction)
- [نحوه استفاده از LangChain4j](../../../01-introduction)
- [استقرار زیرساخت Azure OpenAI](../../../01-introduction)
- [اجرای برنامه به‌صورت محلی](../../../01-introduction)
- [استفاده از برنامه](../../../01-introduction)
  - [چت ایستا (پنل چپ)](../../../01-introduction)
  - [چت دارای وضعیت (پنل راست)](../../../01-introduction)
- [گام‌های بعدی](../../../01-introduction)

## نمایش ویدئویی

این جلسه زنده را تماشا کنید که توضیح می‌دهد چگونه با این ماژول شروع کنید:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="شروع کار با LangChain4j - جلسه زنده" width="800"/></a>

## آنچه یاد خواهید گرفت

در شروع سریع، از مدل‌های GitHub برای ارسال درخواست‌ها، فراخوانی ابزارها، ساخت خط لوله RAG و آزمایش محافظ‌ها استفاده کردید. آن دموها نشان دادند چه چیزی ممکن است — اکنون به Azure OpenAI و GPT-5.2 سوئیچ می‌کنیم و شروع به ساخت برنامه‌های تولیدی می‌کنیم. این ماژول بر هوش مصنوعی مکالمه‌ای که زمینه را به خاطر می‌سپارد و وضعیت را حفظ می‌کند تمرکز دارد — مفاهیمی که آن دموهای شروع سریع در پشت صحنه استفاده کردند اما توضیح ندادند.

در تمام این راهنما از GPT-5.2 Azure OpenAI استفاده خواهیم کرد چون قابلیت‌های پیشرفته استدلال آن رفتار الگوهای مختلف را واضح‌تر می‌کند. وقتی حافظه اضافه کنید، تفاوت را به روشنی خواهید دید. این کار فهمیدن اینکه هر مؤلفه چه چیزی به برنامه شما می‌آورد را آسان‌تر می‌کند.

شما یک برنامه خواهید ساخت که هر دو الگو را نشان می‌دهد:

**چت ایستا** - هر درخواست مستقل است. مدل هیچ حافظه‌ای از پیام‌های قبلی ندارد. این همان الگویی است که در شروع سریع استفاده کردید.

**مکالمه دارای وضعیت** - هر درخواست شامل تاریخچه مکالمه است. مدل زمینه را در طول چندین نوبت حفظ می‌کند. این چیزی است که برنامه‌های تولیدی نیاز دارند.

## پیش‌نیازها

- اشتراک Azure با دسترسی Azure OpenAI
- Java 21، Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **توجه:** Java، Maven، Azure CLI و Azure Developer CLI (azd) در devcontainer ارائه شده از قبل نصب شده‌اند.

> **توجه:** این ماژول از GPT-5.2 روی Azure OpenAI استفاده می‌کند. استقرار به صورت خودکار از طریق `azd up` تنظیم می‌شود - نام مدل را در کد تغییر ندهید.

## درک مشکل اصلی

مدل‌های زبانی ایستا هستند. هر فراخوان API مستقل است. اگر بگویید «اسم من جان است» و سپس بپرسید «اسم من چیست؟» مدل نمی‌داند که شما همین الان خودتان را معرفی کردید. هر درخواست را انگار اولین مکالمه‌ای است که تا به حال داشته‌اید، می‌داند.

این برای پرسش و پاسخ ساده خوب است اما برای برنامه‌های واقعی بی‌فایده است. ربات‌های خدمات مشتری باید به یاد داشته باشند چه گفته‌اید. دستیارهای شخصی نیاز به زمینه دارند. هر مکالمه چند مرحله‌ای به حافظه نیاز دارد.

نمودار زیر دو رویکرد را مقایسه می‌کند — در سمت چپ، یک فراخوان ایستا که نام شما را فراموش می‌کند؛ در سمت راست، یک فراخوان دارای وضعیت پشتیبانی شده توسط ChatMemory که آن را به یاد می‌آورد.

<img src="../../../translated_images/fa/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="مکالمات ایستا در مقابل دارای وضعیت" width="800"/>

*تفاوت بین مکالمات ایستا (فراخوان‌های مستقل) و دارای وضعیت (آگاه به زمینه)*

## درک توکن‌ها

قبل از شروع مکالمات، مهم است که توکن‌ها را بفهمیم - واحدهای پایه متن که مدل‌های زبانی پردازش می‌کنند:

<img src="../../../translated_images/fa/token-explanation.c39760d8ec650181.webp" alt="توضیح توکن" width="800"/>

*نمونه‌ای از چگونگی تقسیم متن به توکن‌ها - "I love AI!" به ۴ واحد پردازش جداگانه تبدیل می‌شود*

توکن‌ها راهی هستند که مدل‌های هوش مصنوعی متن را اندازه‌گیری و پردازش می‌کنند. کلمات، نشانه‌گذاری و حتی فاصله‌ها می‌توانند توکن باشند. مدل شما محدودیت تعداد توکن‌هایی که می‌تواند یکجا پردازش کند را دارد (۴۰۰,۰۰۰ برای GPT-5.2، شامل حداکثر ۲۷۲,۰۰۰ توکن ورودی و ۱۲۸,۰۰۰ توکن خروجی). فهمیدن توکن‌ها به مدیریت طول مکالمه و هزینه‌ها کمک می‌کند.

## نحوه عملکرد حافظه

حافظه چت مشکل ایستا بودن را با حفظ تاریخچه مکالمه حل می‌کند. پیش از ارسال درخواست به مدل، چارچوب پیام‌های قبلی مرتبط را اضافه می‌کند. وقتی می‌پرسید «اسم من چیست؟»، سیستم در واقع تمام تاریخچه مکالمه را ارسال می‌کند تا مدل ببیند که پیش‌تر گفته بودید «اسم من جان است.»

LangChain4j پیاده‌سازی‌هایی از حافظه ارائه می‌دهد که این کار را به صورت خودکار انجام می‌دهند. شما تعیین می‌کنید چند پیام نگهداری شود و چارچوب پنجره زمینه را مدیریت می‌کند. نمودار زیر نشان می‌دهد چگونه MessageWindowChatMemory پنجره‌ای لغزنده از پیام‌های اخیر را حفظ می‌کند.

<img src="../../../translated_images/fa/memory-window.bbe67f597eadabb3.webp" alt="مفهوم پنجره حافظه" width="800"/>

*MessageWindowChatMemory پنجره لغزنده‌ای از پیام‌های اخیر نگه می‌دارد و پیام‌های قدیمی‌تر را به صورت خودکار حذف می‌کند*

## نحوه استفاده از LangChain4j

این ماژول شروع سریع را با ادغام Spring Boot و افزودن حافظه مکالمه گسترش می‌دهد. اینجا چگونگی کنار هم قرار گرفتن بخش‌ها آمده است:

**وابستگی‌ها** - دو کتابخانه LangChain4j اضافه کنید:

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

**مدل چت** - Azure OpenAI را به‌عنوان یک bean در Spring پیکربندی کنید ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

سازنده اعتبارنامه‌ها را از متغیرهای محیطی که توسط `azd up` تنظیم شده‌اند می‌خواند. تنظیم `baseUrl` به نقطه پایان Azure شما باعث می‌شود کلاینت OpenAI با Azure OpenAI کار کند.

**حافظه مکالمه** - تاریخچه چت را با MessageWindowChatMemory پیگیری کنید ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

حافظه را با `withMaxMessages(10)` ایجاد کنید تا ۱۰ پیام آخر را نگه دارد. پیام‌های کاربر و هوش مصنوعی را با پوشش‌های تایپ شده `UserMessage.from(text)` و `AiMessage.from(text)` اضافه کنید. تاریخچه را با `memory.messages()` بازیابی کنید و به مدل ارسال کنید. این سرویس نمونه‌های حافظه جداگانه به ازای هر شناسه مکالمه ذخیره می‌کند، که به چند کاربر اجازه می‌دهد به طور همزمان گفتگو کنند.

> **🤖 امتحان کنید با گفتگوی [GitHub Copilot](https://github.com/features/copilot):** فایل [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) را باز کنید و بپرسید:
> - «MessageWindowChatMemory چگونه تصمیم می‌گیرد کدام پیام‌ها را وقتی پنجره پر است حذف کند؟»
> - «آیا می‌توانم ذخیره‌سازی حافظه سفارشی با استفاده از پایگاه داده به جای حافظه درون‌برنامه‌ای پیاده کنم؟»
> - «چگونه می‌توانم خلاصه‌سازی برای فشرده‌سازی تاریخچه قدیمی مکالمه اضافه کنم؟»

نقطه پایان چت ایستا حافظه را به کلی رد می‌کند — فقط `chatModel.chat(prompt)` مانند شروع سریع. نقطه پایان دارای وضعیت پیام‌ها را به حافظه اضافه می‌کند، تاریخچه را بازیابی می‌کند و آن زمینه را با هر درخواست می‌فرستد. همان پیکربندی مدل، الگوهای متفاوت.

## استقرار زیرساخت Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # اشتراک و مکان را انتخاب کنید (eastus2 توصیه می‌شود)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # اشتراک و موقعیت را انتخاب کنید (پیشنهاد شده: eastus2)
```

> **توجه:** اگر با خطای زمان انتظار (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`) مواجه شدید، کافی است دوباره `azd up` را اجرا کنید. منابع Azure ممکن است هنوز در پس‌زمینه در حال آماده‌سازی باشند و تلاش مجدد اجازه می‌دهد استقرار پس از رسیدن منابع به حالت نهایی کامل شود.

این کارها را انجام خواهد داد:
1. استقرار منبع Azure OpenAI با مدل‌های GPT-5.2 و text-embedding-3-small
2. تولید خودکار فایل `.env` در روت پروژه با اعتبارنامه‌ها
3. تنظیم تمام متغیرهای محیطی لازم

**مشکل در استقرار دارید؟** راهنمای عیب‌یابی دقیق شامل تعارض نام دامنه فرعی، مراحل استقرار دستی در Azure Portal، و راهنمای پیکربندی مدل را در [Infrastructure README](infra/README.md) ببینید.

**بررسی موفقیت استقرار:**

**Bash:**
```bash
cat ../.env  # باید AZURE_OPENAI_ENDPOINT، API_KEY و غیره را نشان دهد.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # باید AZURE_OPENAI_ENDPOINT، API_KEY و غیره را نشان دهد.
```

> **توجه:** دستور `azd up` به صورت خودکار فایل `.env` را ایجاد می‌کند. اگر بعداً نیاز به به‌روزرسانی دارید، می‌توانید یا فایل `.env` را به صورت دستی ویرایش کنید یا آن را با اجرای:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## اجرای برنامه به‌صورت محلی

**بررسی استقرار:**

اطمینان حاصل کنید فایل `.env` در دایرکتوری ریشه با اعتبارنامه‌های Azure موجود است. این را از دایرکتوری ماژول (`01-introduction/`) اجرا کنید:

**Bash:**
```bash
cat ../.env  # باید AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT را نمایش دهد
```

**PowerShell:**
```powershell
Get-Content ..\.env  # باید AZURE_OPENAI_ENDPOINT، API_KEY، DEPLOYMENT را نشان دهد
```

**شروع برنامه‌ها:**

**گزینه ۱: استفاده از Spring Boot Dashboard (توصیه شده برای کاربران VS Code)**

dev container شامل افزونه Spring Boot Dashboard است که رابطی بصری برای مدیریت همه برنامه‌های Spring Boot فراهم می‌کند. می‌توانید آن را در Activity Bar در سمت چپ VS Code پیدا کنید (آیکون Spring Boot را ببینید).

از Spring Boot Dashboard می‌توانید:
- همه برنامه‌های Spring Boot موجود در فضای کاری را ببینید
- با یک کلیک برنامه‌ها را شروع یا متوقف کنید
- لاگ‌های برنامه را به صورت زنده مشاهده کنید
- وضعیت برنامه‌ها را نظارت کنید

فقط روی دکمه پلی کنار "introduction" کلیک کنید تا این ماژول شروع شود، یا همه ماژول‌ها را یک‌جا اجرا کنید.

<img src="../../../translated_images/fa/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*داشبورد Spring Boot در VS Code — شروع، توقف و نظارت بر همه ماژول‌ها از یک مکان*

**گزینه ۲: استفاده از اسکریپت‌های شل**

تمام برنامه‌های وب (ماژول‌های ۰۱ تا ۰۴) را اجرا کنید:

**Bash:**
```bash
cd ..  # از دایرکتوری ریشه
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # از فهرست ریشه
.\start-all.ps1
```

یا فقط این ماژول را اجرا کنید:

**Bash:**
```bash
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

هر دو اسکریپت به صورت خودکار متغیرهای محیطی را از فایل `.env` ریشه بارگذاری می‌کنند و در صورت نبود، فایل‌های JAR را می‌سازند.

> **توجه:** اگر ترجیح می‌دهید همه ماژول‌ها را دستی قبل از شروع بسازید:
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

مرورگر خود را باز کرده و http://localhost:8080 را مشاهده کنید.

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

## استفاده از برنامه

این برنامه یک رابط وب با دو پیاده‌سازی چت کنار هم فراهم می‌کند.

<img src="../../../translated_images/fa/home-screen.121a03206ab910c0.webp" alt="صفحه اصلی برنامه" width="800"/>

*داشبورد نشان‌دهنده گزینه‌های چت ساده (ایستا) و چت مکالمه‌ای (دارای وضعیت)*

### چت ایستا (پنل چپ)

ابتدا این را امتحان کنید. بگویید «اسم من جان است» و سپس فوراً بپرسید «اسم من چیست؟» مدل به یاد نمی‌آورد چون هر پیام مستقل است. این نشان‌دهنده مشکل اصلی ادغام ساده مدل‌های زبانی — نبود زمینه مکالمه — است.

<img src="../../../translated_images/fa/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="دموی چت ایستا" width="800"/>

*هوش مصنوعی نام شما را از پیام قبلی به یاد نمی‌آورد*

### چت دارای وضعیت (پنل راست)

اکنون همان دنباله را اینجا امتحان کنید. بگویید «اسم من جان است» و سپس بپرسید «اسم من چیست؟» این بار به یاد دارد. تفاوت MessageWindowChatMemory است — تاریخچه مکالمه را حفظ می‌کند و با هر درخواست آن را ارسال می‌کند. این همان کاری است که هوش مصنوعی مکالمه‌ای در تولید انجام می‌دهد.

<img src="../../../translated_images/fa/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="دموی چت دارای وضعیت" width="800"/>

*هوش مصنوعی نام شما را از ابتدای مکالمه به یاد می‌آورد*

هر دو پنل از همان مدل GPT-5.2 استفاده می‌کنند. تنها تفاوت حافظه است. این واضح می‌کند که حافظه چه چیزی به برنامه شما می‌آورد و چرا برای موارد واقعی ضروری است.

## گام‌های بعدی

**ماژول بعدی:** [۰۲-مهندسی_پرومپت - مهندسی پرومپت با GPT-5.2](../02-prompt-engineering/README.md)

---

**ناوبری:** [← قبلی: ماژول ۰۰ - شروع سریع](../00-quick-start/README.md) | [بازگشت به اصلی](../README.md) | [بعدی: ماژول ۰۲ - مهندسی پرومپت →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**توضیح مهم**:  
این سند با استفاده از سرویس ترجمه هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما در تلاش برای دقت هستیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است شامل خطاها یا نواقصی باشد. سند اصلی به زبان بومی خود باید به عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حساس، استفاده از ترجمه حرفه‌ای انسانی توصیه می‌شود. ما مسئول هیچ گونه سوء تفاهم یا تفسیر نادرستی که از استفاده این ترجمه ناشی شود، نیستیم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->