# البنية التحتية لـ Azure لـ LangChain4j للبدء السريع

## جدول المحتويات

- [المتطلبات الأساسية](../../../../01-introduction/infra)
- [البنية المعمارية](../../../../01-introduction/infra)
- [الموارد التي تم إنشاؤها](../../../../01-introduction/infra)
- [البدء السريع](../../../../01-introduction/infra)
- [التكوين](../../../../01-introduction/infra)
- [أوامر الإدارة](../../../../01-introduction/infra)
- [تحسين التكلفة](../../../../01-introduction/infra)
- [المراقبة](../../../../01-introduction/infra)
- [استكشاف الأخطاء وإصلاحها](../../../../01-introduction/infra)
- [تحديث البنية التحتية](../../../../01-introduction/infra)
- [التنظيف](../../../../01-introduction/infra)
- [هيكل الملفات](../../../../01-introduction/infra)
- [توصيات الأمان](../../../../01-introduction/infra)
- [موارد إضافية](../../../../01-introduction/infra)

يحتوي هذا الدليل على بنية تحتية لـ Azure ككود (IaC) باستخدام Bicep و Azure Developer CLI (azd) لنشر موارد Azure OpenAI.

## المتطلبات الأساسية

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (الإصدار 2.50.0 أو أحدث)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (الإصدار 1.5.0 أو أحدث)
- اشتراك Azure يحتوي على أذونات لإنشاء الموارد

## البنية المعمارية

**إعداد تطوير محلي مبسط** - نشر Azure OpenAI فقط، وتشغيل جميع التطبيقات محليًا.

تنشر البنية التحتية موارد Azure التالية:

### خدمات الذكاء الاصطناعي
- **Azure OpenAI**: خدمات معرفية مع نشرين للطراز:
  - **gpt-5.2**: نموذج إكمال الدردشة (سعة 20K TPM)
  - **text-embedding-3-small**: نموذج التضمين لـ RAG (سعة 20K TPM)

### التطوير المحلي
جميع تطبيقات Spring Boot تعمل محليًا على جهازك:
- 01-introduction (المنفذ 8080)
- 02-prompt-engineering (المنفذ 8083)
- 03-rag (المنفذ 8081)
- 04-tools (المنفذ 8084)

## الموارد التي تم إنشاؤها

| نوع المورد | نمط اسم المورد | الغرض |
|--------------|----------------------|---------|
| مجموعة الموارد | `rg-{environmentName}` | يحتوي على جميع الموارد |
| Azure OpenAI | `aoai-{resourceToken}` | استضافة نموذج الذكاء الاصطناعي |

> **ملاحظة:** `{resourceToken}` هو سلسلة فريدة يتم توليدها من معرف الاشتراك، واسم البيئة، والموقع

## البدء السريع

### 1. نشر Azure OpenAI

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

عند الظهور للمطالبة:
- اختر اشتراك Azure الخاص بك
- اختر موقعًا (موصى به: `eastus2` لتوفر GPT-5.2)
- أكد اسم البيئة (الافتراضي: `langchain4j-dev`)

سيقوم هذا بإنشاء:
- مورد Azure OpenAI مع GPT-5.2 و text-embedding-3-small
- تفاصيل الاتصال

### 2. الحصول على تفاصيل الاتصال

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

يعرض هذا:
- `AZURE_OPENAI_ENDPOINT`: عنوان URL لنقطة نهاية Azure OpenAI الخاصة بك
- `AZURE_OPENAI_KEY`: مفتاح API للمصادقة
- `AZURE_OPENAI_DEPLOYMENT`: اسم نموذج الدردشة (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: اسم نموذج التضمين

### 3. تشغيل التطبيقات محليًا

يقوم أمر `azd up` تلقائيًا بإنشاء ملف `.env` في الدليل الجذر يحتوي على جميع متغيرات البيئة اللازمة.

**موصى به:** ابدأ جميع تطبيقات الويب:

**Bash:**
```bash
# من دليل الجذر
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# من الدليل الجذري
cd ../..
.\start-all.ps1
```

أو ابدأ وحدة واحدة فقط:

**Bash:**
```bash
# مثال: ابدأ فقط وحدة المقدمة
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# مثال: ابدأ فقط وحدة المقدمة
cd ../01-introduction
.\start.ps1
```

تقوم كلا النصوص بتحميل متغيرات البيئة تلقائيًا من ملف `.env` الموجود في الجذر الذي تم إنشاؤه بواسطة `azd up`.

## التكوين

### تخصيص نشرات النماذج

لتغيير نشرات النماذج، حرر `infra/main.bicep` وعدل معلمة `openAiDeployments`:

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

النماذج والإصدارات المتاحة: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### تغيير المناطق في Azure

لنشر في منطقة مختلفة، حرر `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

تحقق من توفر GPT-5.2: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

لتحديث البنية التحتية بعد إجراء تغييرات على ملفات Bicep:

**Bash:**
```bash
# إعادة بناء قالب ARM
az bicep build --file infra/main.bicep

# معاينة التغييرات
azd provision --preview

# تطبيق التغييرات
azd provision
```

**PowerShell:**
```powershell
# إعادة بناء قالب ARM
az bicep build --file infra/main.bicep

# معاينة التغييرات
azd provision --preview

# تطبيق التغييرات
azd provision
```

## التنظيف

لحذف جميع الموارد:

**Bash:**
```bash
# حذف جميع الموارد
azd down

# حذف كل شيء بما في ذلك البيئة
azd down --purge
```

**PowerShell:**
```powershell
# حذف كل الموارد
azd down

# حذف كل شيء بما في ذلك البيئة
azd down --purge
```

**تحذير**: سيؤدي هذا إلى حذف جميع موارد Azure بشكل دائم.

## هيكل الملفات

## تحسين التكلفة

### التطوير/الاختبار
لبيئات التطوير/الاختبار، يمكنك تقليل التكاليف:
- استخدم الطبقة القياسية (S0) لـ Azure OpenAI
- قم بتعيين سعة أقل (10K TPM بدلاً من 20K) في `infra/core/ai/cognitiveservices.bicep`
- احذف الموارد عند عدم الاستخدام: `azd down`

### الإنتاج
لبيئة الإنتاج:
- زيادة سعة OpenAI بناءً على الاستخدام (50K+ TPM)
- تمكين التكرار عبر مناطق لزيادة التوفر
- تنفيذ المراقبة الصحيحة وتنبيهات التكلفة

### تقدير التكلفة
- Azure OpenAI: الدفع حسب الرمز المميز (الإدخال + الإخراج)
- GPT-5.2: حوالي 3-5 دولارات لكل مليون رمز مميز (تحقق من الأسعار الحالية)
- text-embedding-3-small: حوالي 0.02 دولار لكل مليون رمز مميز

حاسبة الأسعار: https://azure.microsoft.com/pricing/calculator/

## المراقبة

### عرض مقاييس Azure OpenAI

اذهب إلى بوابة Azure → مورد OpenAI الخاص بك → المقاييس:
- الاستخدام حسب الرمز المميز
- معدل طلبات HTTP
- زمن الاستجابة
- الرموز المميزة النشطة

## استكشاف الأخطاء وإصلاحها

### المشكلة: تعارض اسم النطاق الفرعي لـ Azure OpenAI

**رسالة الخطأ:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**السبب:**
اسم النطاق الفرعي الذي تم توليده من اشتراكك/بيئتك مستخدم بالفعل، ربما من نشر سابق لم يتم تنظيفه بالكامل.

**الحل:**
1. **الخيار 1 - استخدام اسم بيئة مختلف:**
   
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

2. **الخيار 2 - النشر اليدوي عبر بوابة Azure:**
   - انتقل إلى بوابة Azure → إنشاء مورد → Azure OpenAI
   - اختر اسمًا فريدًا لموردك
   - انشر النماذج التالية:
     - **GPT-5.2**
     - **text-embedding-3-small** (لوحدات RAG)
   - **مهم:** لاحظ أسماء نشراتك - يجب أن تتوافق مع تكوين `.env`
   - بعد النشر، احصل على نقطة النهاية ومفتاح API من "المفاتيح ونقطة النهاية"
   - أنشئ ملف `.env` في جذر المشروع مع:

     **مثال على ملف `.env`:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**إرشادات تسمية نشر النماذج:**
- استخدم أسماء بسيطة ومتناسقة: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- يجب أن تتطابق أسماء النشر تمامًا مع ما تكوّنه في `.env`
- خطأ شائع: إنشاء نموذج باسم واحد ولكن الإشارة إليه باسم مختلف في الكود

### المشكلة: GPT-5.2 غير متوفر في المنطقة المحددة

**الحل:**
- اختر منطقة تحتوي على وصول GPT-5.2 (مثل eastus2)
- تحقق من التوفر: https://learn.microsoft.com/azure/ai-services/openai/concepts/models



### المشكلة: حصة غير كافية للنشر

**الحل:**
1. طلب زيادة الحصة من بوابة Azure
2. أو استخدم سعة أقل في `main.bicep` (مثل السعة: 10)

### المشكلة: "المورد غير موجود" عند التشغيل محليًا

**الحل:**
1. تحقق من النشر: `azd env get-values`
2. تحقق من صحة نقطة النهاية والمفتاح
3. تأكد من وجود مجموعة الموارد في بوابة Azure

### المشكلة: فشل المصادقة

**الحل:**
- تحقق من تعيين `AZURE_OPENAI_API_KEY` بشكل صحيح
- يجب أن يكون تنسيق المفتاح سلسلة سداسية عشرية مكونة من 32 حرفًا
- احصل على مفتاح جديد من بوابة Azure إذا لزم الأمر

### فشل النشر

**المشكلة**: `azd provision` يفشل مع أخطاء الحصة أو السعة

**الحل**: 
1. جرب منطقة مختلفة - راجع قسم [تغيير مناطق Azure](../../../../01-introduction/infra) لكيفية تكوين المناطق
2. تحقق من أن اشتراكك يحتوي على حصة Azure OpenAI:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### التطبيق لا يتصل

**المشكلة**: تطبيق Java يعرض أخطاء اتصال

**الحل**:
1. تحقق من تصدير متغيرات البيئة:
   
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

2. تحقق من صحة تنسيق نقطة النهاية (يجب أن تكون `https://xxx.openai.azure.com`)
3. تحقق من أن مفتاح API هو المفتاح الأساسي أو الثانوي من بوابة Azure

**المشكلة**: 401 غير مصرح من Azure OpenAI

**الحل**:
1. احصل على مفتاح API جديد من بوابة Azure → المفاتيح ونقطة النهاية
2. أعد تصدير متغير البيئة `AZURE_OPENAI_API_KEY`
3. تأكد من اكتمال نشر النماذج (تحقق من بوابة Azure)

### مشاكل الأداء

**المشكلة**: زمن استجابة بطيء

**الحل**:
1. تحقق من استخدام رموز OpenAI والحد من المعدل في مقاييس بوابة Azure
2. زد سعة TPM إذا وصلت إلى الحد الأقصى
3. فكر في استخدام مستوى جهد استدلال أعلى (منخفض/متوسط/عالي)

## تحديث البنية التحتية

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

## توصيات الأمان

1. **لا تلتزم بمفاتيح API أبدًا** - استخدم متغيرات البيئة
2. **استخدم ملفات .env محليًا** - أضف `.env` إلى `.gitignore`
3. **قم بتدوير المفاتيح بانتظام** - أنشئ مفاتيح جديدة في بوابة Azure
4. **حدد الوصول** - استخدم Azure RBAC للتحكم بمن يمكنه الوصول إلى الموارد
5. **راقب الاستخدام** - قم بإعداد تنبيهات التكلفة في بوابة Azure

## موارد إضافية

- [وثائق خدمة Azure OpenAI](https://learn.microsoft.com/azure/ai-services/openai/)
- [وثائق نموذج GPT-5.2](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [وثائق Azure Developer CLI](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [وثائق Bicep](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [التكامل الرسمي لـ LangChain4j OpenAI](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## الدعم

للمشاكل:
1. تحقق من [قسم استكشاف الأخطاء وإصلاحها](../../../../01-introduction/infra) أعلاه
2. راجع حالة خدمة Azure OpenAI في بوابة Azure
3. افتح مشكلة في المستودع

## الترخيص

راجع ملف [LICENSE](../../../../LICENSE) في الجذر للتفاصيل.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**تنويه**:
تمت ترجمة هذا المستند باستخدام خدمة الترجمة بالذكاء الاصطناعي [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى لتحقيق الدقة، يُرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار الوثيقة الأصلية بلغتها الأصلية المصدر المعتمد. بالنسبة للمعلومات الحساسة، يُنصح بالاعتماد على الترجمة البشرية المهنية. نحن غير مسؤولين عن أي سوء فهم أو تفسير ناتج عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->