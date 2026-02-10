# زیرساخت Azure برای LangChain4j شروع کار

## فهرست مطالب

- [پیش‌نیازها](../../../../01-introduction/infra)
- [معماری](../../../../01-introduction/infra)
- [منابع ایجاد شده](../../../../01-introduction/infra)
- [شروع سریع](../../../../01-introduction/infra)
- [پیکربندی](../../../../01-introduction/infra)
- [دستورات مدیریت](../../../../01-introduction/infra)
- [بهینه‌سازی هزینه](../../../../01-introduction/infra)
- [نظارت](../../../../01-introduction/infra)
- [عیب‌یابی](../../../../01-introduction/infra)
- [به‌روزرسانی زیرساخت](../../../../01-introduction/infra)
- [پاک‌سازی](../../../../01-introduction/infra)
- [ساختار فایل](../../../../01-introduction/infra)
- [توصیه‌های امنیتی](../../../../01-introduction/infra)
- [منابع اضافی](../../../../01-introduction/infra)

این دایرکتوری شامل زیرساخت Azure به صورت کد (IaC) با استفاده از Bicep و Azure Developer CLI (azd) برای استقرار منابع Azure OpenAI است.

## پیش‌نیازها

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (نسخه 2.50.0 یا بالاتر)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (نسخه 1.5.0 یا بالاتر)
- اشتراک Azure با دسترسی برای ایجاد منابع

## معماری

**تنظیم توسعه محلی ساده‌شده** - فقط Azure OpenAI را مستقر کنید، همه برنامه‌ها را محلی اجرا کنید.

این زیرساخت منابع زیر Azure را مستقر می‌کند:

### خدمات هوش مصنوعی
- **Azure OpenAI**: خدمات شناختی با دو استقرار مدل:
  - **gpt-5.2**: مدل چت تکمیلی (ظرفیت ۲۰ هزار TPM)
  - **text-embedding-3-small**: مدل جاسازی برای RAG (ظرفیت ۲۰ هزار TPM)

### توسعه محلی
همه برنامه‌های Spring Boot به صورت محلی روی دستگاه شما اجرا می‌شوند:
- 01-introduction (درگاه 8080)
- 02-prompt-engineering (درگاه 8083)
- 03-rag (درگاه 8081)
- 04-tools (درگاه 8084)

## منابع ایجاد شده

| نوع منبع | الگوی نام منبع | هدف |
|--------------|----------------------|---------|
| گروه منبع | `rg-{environmentName}` | شامل همه منابع |
| Azure OpenAI | `aoai-{resourceToken}` | میزبانی مدل AI |

> **توجه:** `{resourceToken}` یک رشته یکتا است که از شناسه اشتراک، نام محیط و مکان ایجاد می‌شود

## شروع سریع

### ۱. استقرار Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up
```

**PowerShell:**
```powershell
cd 01-introduction
azd up
```

وقتی از شما خواسته شد:
- اشتراک Azure خود را انتخاب کنید
- یک مکان انتخاب کنید (توصیه‌شده: `eastus2` برای دسترسی به GPT-5.2)
- نام محیط را تأیید کنید (پیش‌فرض: `langchain4j-dev`)

این کار ایجاد خواهد کرد:
- منبع Azure OpenAI با GPT-5.2 و text-embedding-3-small
- خروجی جزئیات اتصال

### ۲. دریافت جزئیات اتصال

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

این موارد را نمایش می‌دهد:
- `AZURE_OPENAI_ENDPOINT`: URL نقطه انتهایی Azure OpenAI شما
- `AZURE_OPENAI_KEY`: کلید API برای احراز هویت
- `AZURE_OPENAI_DEPLOYMENT`: نام مدل چت (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: نام مدل جاسازی

### ۳. اجرای برنامه‌ها به صورت محلی

دستور `azd up` به صورت خودکار فایل `.env` را در دایرکتوری ریشه با همه متغیرهای محیط ضروری ایجاد می‌کند.

**توصیه شده:** همه برنامه‌های وب را شروع کنید:

**Bash:**
```bash
# از دایرکتوری ریشه
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# از دایرکتوری ریشه
cd ../..
.\start-all.ps1
```

یا فقط یک ماژول را اجرا کنید:

**Bash:**
```bash
# مثال: فقط ماژول معرفی را شروع کنید
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# مثال: فقط ماژول معرفی را شروع کنید
cd ../01-introduction
.\start.ps1
```

هر دو اسکریپت به صورت خودکار متغیرهای محیطی را از فایل `.env` ریشه که توسط `azd up` ساخته شده بارگذاری می‌کنند.

## پیکربندی

### سفارشی‌سازی استقرار مدل‌ها

برای تغییر استقرار مدل‌ها، فایل `infra/main.bicep` را ویرایش کرده و پارامتر `openAiDeployments` را اصلاح کنید:

```bicep
param openAiDeployments array = [
  {
    name: 'gpt-5.2'  // Model deployment name
    model: {
      format: 'OpenAI'
      name: 'gpt-5.2'
      version: '2025-12-11'  // Model version
    }
    sku: {
      name: 'GlobalStandard'
      capacity: 20  // TPM in thousands
    }
  }
  // Add more deployments...
]
```

مدل‌ها و نسخه‌های موجود: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### تغییر مناطق Azure

برای استقرار در منطقه‌ای دیگر، فایل `infra/main.bicep` را ویرایش کنید:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

بررسی دسترسی GPT-5.2: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

برای به‌روزرسانی زیرساخت پس از اعمال تغییرات در فایل‌های Bicep:

**Bash:**
```bash
# بازسازی قالب ARM
az bicep build --file infra/main.bicep

# پیش‌نمایش تغییرات
azd provision --preview

# اعمال تغییرات
azd provision
```

**PowerShell:**
```powershell
# بازسازی قالب ARM
az bicep build --file infra/main.bicep

# پیش‌نمایش تغییرات
azd provision --preview

# اعمال تغییرات
azd provision
```

## پاک‌سازی

برای حذف همه منابع:

**Bash:**
```bash
# همه منابع را حذف کنید
azd down

# همه چیز را از جمله محیط حذف کنید
azd down --purge
```

**PowerShell:**
```powershell
# حذف همه منابع
azd down

# حذف همه چیز شامل محیط
azd down --purge
```

**هشدار**: این کار همه منابع Azure را به صورت دائمی حذف خواهد کرد.

## ساختار فایل

## بهینه‌سازی هزینه

### توسعه/آزمایش
برای محیط‌های توسعه/آزمایش می‌توانید هزینه‌ها را کاهش دهید:
- استفاده از سطح استاندارد (S0) برای Azure OpenAI
- تنظیم ظرفیت کمتر (۱۰ هزار TPM به جای ۲۰ هزار) در `infra/core/ai/cognitiveservices.bicep`
- حذف منابع در صورت عدم استفاده: `azd down`

### تولید
برای محیط تولید:
- افزایش ظرفیت OpenAI بر اساس میزان استفاده (۵۰ هزار یا بیشتر TPM)
- فعال کردن افزونگی منطقه‌ای برای دسترسی بالاتر
- پیاده‌سازی نظارت و هشدارهای هزینه مناسب

### تخمین هزینه
- Azure OpenAI: پرداخت به ازای توکن (ورودی + خروجی)
- GPT-5.2: حدود ۳ تا ۵ دلار برای ۱ میلیون توکن (قیمت جاری را بررسی کنید)
- text-embedding-3-small: حدود ۰.۰۲ دلار برای ۱ میلیون توکن

ماشین حساب قیمت: https://azure.microsoft.com/pricing/calculator/

## نظارت

### مشاهده معیارهای Azure OpenAI

به پورتال Azure → منبع OpenAI شما → معیارها بروید:
- استفاده مبتنی بر توکن
- نرخ درخواست‌های HTTP
- زمان پاسخ
- توکن‌های فعال

## عیب‌یابی

### مشکل: تداخل نام زیر دامنه Azure OpenAI

**پیغام خطا:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**علت:**
نام زیر دامنه‌ای که از اشتراک/محیط شما تولید شده قبلاً استفاده شده است، احتمالاً از استقرار قبلی که به طور کامل حذف نشده بود.

**راه‌حل:**
1. **گزینه ۱ - استفاده از نام محیط متفاوت:**
   
   **Bash:**
   ```bash
   azd env new my-unique-env-name
   azd up
   ```
   
   **PowerShell:**
   ```powershell
   azd env new my-unique-env-name
   azd up
   ```

2. **گزینه ۲ - استقرار دستی از طریق پورتال Azure:**
   - به پورتال Azure → ایجاد منبع → Azure OpenAI بروید
   - نام منبع منحصر به فرد انتخاب کنید
   - مدل‌های زیر را مستقر کنید:
     - **GPT-5.2**
     - **text-embedding-3-small** (برای ماژول‌های RAG)
   - **مهم:** نام‌های استقرار شما باید با پیکربندی `.env` مطابقت داشته باشد
   - پس از استقرار، نقطه انتهایی و کلید API را از بخش "Keys and Endpoint" دریافت کنید
   - یک فایل `.env` در ریشه پروژه بسازید با:
     
     **مثال فایل `.env`:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**راهنمای نامگذاری استقرار مدل:**
- از نام‌های ساده و یکسان استفاده کنید: `gpt-5.2`، `gpt-4o`، `text-embedding-3-small`
- نام‌های استقرار باید دقیقاً با آنچه در `.env` تنظیم کرده‌اید مطابقت داشته باشند
- اشتباه رایج: ساخت مدل با یک نام اما ارجاع به نام متفاوت در کد

### مشکل: نبود مدل GPT-5.2 در منطقه انتخاب شده

**راه‌حل:**
- منطقه‌ای با دسترسی GPT-5.2 را انتخاب کنید (مثلاً eastus2)
- بررسی دسترسی: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### مشکل: سهمیه کافی برای استقرار وجود ندارد

**راه‌حل:**
1. تقاضای افزایش سهمیه در پورتال Azure کنید
2. یا ظرفیت کمتری در `main.bicep` استفاده کنید (مثلاً ظرفیت: ۱۰)

### مشکل: "منبع یافت نشد" هنگام اجرای محلی

**راه‌حل:**
1. استقرار را بررسی کنید: `azd env get-values`
2. بررسی کنید نقطه انتهایی و کلید درست باشند
3. مطمئن شوید گروه منبع در پورتال Azure موجود است

### مشکل: احراز هویت ناموفق

**راه‌حل:**
- مطمئن شوید `AZURE_OPENAI_API_KEY` به درستی تنظیم شده است
- قالب کلید باید رشته هگزادسیمال ۳۲ کاراکتری باشد
- در صورت نیاز کلید جدید از پورتال Azure دریافت کنید

### استقرار ناموفق

**مشکل**: `azd provision` با خطاهای سهمیه یا ظرفیت شکست می‌خورد

**راه‌حل**: 
1. منطقه متفاوتی را امتحان کنید - بخش [تغییر مناطق Azure](../../../../01-introduction/infra) برای نحوه تنظیم مناطق را ببینید
2. مطمئن شوید اشتراک شما سهمیه Azure OpenAI دارد:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### برنامه اتصال ندارد

**مشکل**: برنامه جاوا خطاهای اتصال نمایش می‌دهد

**راه‌حل**:
1. اطمینان حاصل کنید متغیرهای محیطی صادر شده‌اند:
   
   **Bash:**
   ```bash
   echo $AZURE_OPENAI_ENDPOINT
   echo $AZURE_OPENAI_API_KEY
   ```
   
   **PowerShell:**
   ```powershell
   Write-Host $env:AZURE_OPENAI_ENDPOINT
   Write-Host $env:AZURE_OPENAI_API_KEY
   ```

2. بررسی کنید قالب نقطه انتهایی صحیح باشد (باید `https://xxx.openai.azure.com` باشد)
3. مطمئن شوید کلید API کلید اصلی یا ثانویه از پورتال Azure است

**مشکل**: ۴۰۱ Unauthorized از Azure OpenAI

**راه‌حل**:
1. کلید API تازه‌ای از پورتال Azure → Keys and Endpoint دریافت کنید
2. متغیر محیطی `AZURE_OPENAI_API_KEY` را دوباره صادر کنید
3. مطمئن شوید استقرارهای مدل کامل شده‌اند (پورتال Azure را چک کنید)

### مشکلات عملکرد

**مشکل**: زمان پاسخ‌دهی کند

**راه‌حل**:
1. مصرف توکن OpenAI و محدودیت‌ها را در معیارهای پورتال Azure بررسی کنید
2. در صورت رسیدن به حد، ظرفیت TPM را افزایش دهید
3. در نظر بگیرید از سطح تلاش استدلالی بالاتر (کم/متوسط/بالا) استفاده کنید

## به‌روزرسانی زیرساخت

```
infra/
├── main.bicep                       # Main infrastructure definition
├── main.json                        # Compiled ARM template (auto-generated)
├── main.bicepparam                  # Parameter file
├── README.md                        # This file
└── core/
    └── ai/
        └── cognitiveservices.bicep  # Azure OpenAI module
```

## توصیه‌های امنیتی

1. **کلیدهای API را هرگز کامیت نکنید** - از متغیرهای محیطی استفاده کنید
2. **از فایل‌های .env به صورت محلی استفاده کنید** - `.env` را به `.gitignore` اضافه کنید
3. **کلیدها را به صورت منظم چرخش دهید** - کلیدهای جدید از پورتال Azure بسازید
4. **دسترسی را محدود کنید** - از Azure RBAC برای کنترل دسترسی استفاده کنید
5. **مصرف را نظارت کنید** - هشدارهای هزینه را در پورتال Azure تنظیم کنید

## منابع اضافی

- [مستندات سرویس Azure OpenAI](https://learn.microsoft.com/azure/ai-services/openai/)
- [مستندات مدل GPT-5.2](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [مستندات Azure Developer CLI](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [مستندات Bicep](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [ادغام رسمی LangChain4j با OpenAI](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## پشتیبانی

برای مشکلات:
1. بخش [عیب‌یابی](../../../../01-introduction/infra) بالا را بررسی کنید
2. سلامت سرویس Azure OpenAI را در پورتال Azure مرور کنید
3. یک مسئله در مخزن باز کنید

## مجوز

جزئیات در فایل [LICENSE](../../../../LICENSE) ریشه را ببینید.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**سلب مسئولیت**:  
این سند با استفاده از سرویس ترجمه ماشینی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. با اینکه ما در تلاش برای دقت ترجمه هستیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است حاوی خطا یا نادرستی باشند. سند اصلی به زبان مبدا، منبع معتبر تلقی می‌شود. برای اطلاعات مهم و حیاتی، ترجمه حرفه‌ای انسانی توصیه می‌شود. ما مسئولیتی در قبال هرگونه سوءتفاهم یا تفسیر نادرست ناشی از استفاده از این ترجمه نداریم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->