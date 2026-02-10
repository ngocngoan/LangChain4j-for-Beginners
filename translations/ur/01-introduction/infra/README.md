# LangChain4j کے لیے Azure انفراسٹرکچر شروع کرنے کا طریقہ

## فہرست مضامین

- [ضروری شرائط](../../../../01-introduction/infra)
- [معماری](../../../../01-introduction/infra)
- [بنائے گئے وسائل](../../../../01-introduction/infra)
- [فوری آغاز](../../../../01-introduction/infra)
- [تشکیل](../../../../01-introduction/infra)
- [انتظامی کمانڈز](../../../../01-introduction/infra)
- [لاگت کی بچت](../../../../01-introduction/infra)
- [مانیٹرنگ](../../../../01-introduction/infra)
- [مسائل کا حل](../../../../01-introduction/infra)
- [انفراسٹرکچر کی تازہ کاری](../../../../01-introduction/infra)
- [صفائی](../../../../01-introduction/infra)
- [فائل کی ساخت](../../../../01-introduction/infra)
- [سیکیورٹی کی سفارشات](../../../../01-introduction/infra)
- [مزید وسائل](../../../../01-introduction/infra)

یہ ڈائریکٹری Azure OpenAI وسائل کی تعیناتی کے لیے Bicep اور Azure Developer CLI (azd) استعمال کرتے ہوئے کوڈ کے طور پر Azure انفراسٹرکچر (IaC) پر مشتمل ہے۔

## ضروری شرائط

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (ورژن 2.50.0 یا اس کے بعد کا)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (ورژن 1.5.0 یا اس کے بعد کا)
- Azure سبسکرپشن جس میں وسائل بنانے کی اجازت ہو

## معماری

**سادہ مقامی ترقی کا سیٹ اپ** - صرف Azure OpenAI تعینات کریں، تمام ایپس مقامی طور پر چلائیں۔

انفراسٹرکچر درج ذیل Azure وسائل تعینات کرتا ہے:

### AI خدمات
- **Azure OpenAI**: کوگنیٹو سروسز دو ماڈل تعیناتیوں کے ساتھ:
  - **gpt-5.2**: چیٹ کمپلیشن ماڈل (20K TPM کی صلاحیت)
  - **text-embedding-3-small**: RAG کے لیے ایمبیڈنگ ماڈل (20K TPM کی صلاحیت)

### مقامی ترقی
تمام اسپرنگ بوٹ ایپلیکیشنز آپ کے کمپیوٹر پر مقامی طور پر چلتی ہیں:
- 01-introduction (پورٹ 8080)
- 02-prompt-engineering (پورٹ 8083)
- 03-rag (پورٹ 8081)
- 04-tools (پورٹ 8084)

## بنائے گئے وسائل

| قسمِ وسیلہ | وسیلہ نام کی شکل | مقصد |
|--------------|----------------------|---------|
| Resource Group | `rg-{environmentName}` | تمام وسائل پر مشتمل |
| Azure OpenAI | `aoai-{resourceToken}` | AI ماڈل ہوسٹنگ |

> **نوٹ:** `{resourceToken}` ایک منفرد اسٹرنگ ہے جو سبسکرپشن آئی ڈی، ماحول کا نام، اور مقام سے پیدا ہوتی ہے

## فوری آغاز

### 1. Azure OpenAI تعینات کریں

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

جب پوچها جائے:
- اپنی Azure سبسکرپشن منتخب کریں
- مقام منتخب کریں (تجویز کردہ: `eastus2` GPT-5.2 کے لیے)
- ماحول کا نام تصدیق کریں (ڈیفالٹ: `langchain4j-dev`)

یہ بنائے گا:
- GPT-5.2 اور text-embedding-3-small کے ساتھ Azure OpenAI وسیلہ
- کنکشن کی تفصیلات آؤٹ پٹ کریں گے

### 2. کنکشن کی تفصیلات حاصل کریں

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

یہ دکھاتا ہے:
- `AZURE_OPENAI_ENDPOINT`: آپ کا Azure OpenAI اینڈ پوائنٹ URL
- `AZURE_OPENAI_KEY`: توثیق کے لیے API کلید
- `AZURE_OPENAI_DEPLOYMENT`: چیٹ ماڈل کا نام (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: ایمبیڈنگ ماڈل کا نام

### 3. ایپلیکیشنز مقامی طور پر چلائیں

`azd up` کمانڈ خودکار طور پر روٹ ڈائریکٹری میں `.env` فائل بناتی ہے جس میں تمام ضروری ماحول کے متغیرات شامل ہوتے ہیں۔

**تجویز:** تمام ویب ایپلیکیشنز شروع کریں:

**Bash:**
```bash
# جڑ ڈائرکٹری سے
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# روٹ ڈائریکٹری سے
cd ../..
.\start-all.ps1
```

یا ایک ماڈیول شروع کریں:

**Bash:**
```bash
# مثال: صرف تعارفی ماڈیول شروع کریں
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# مثال: صرف تعارفی ماڈیول شروع کریں
cd ../01-introduction
.\start.ps1
```

دونوں اسکرپٹس خودکار طور پر `.env` فائل سے ماحولیاتی متغیرات لوڈ کرتے ہیں جو `azd up` نے بنائی ہے۔

## تشکیل

### ماڈل تعیناتیوں کو حسب ضرورت بنائیں

ماڈل تعیناتیوں کو تبدیل کرنے کے لیے `infra/main.bicep` فائل میں `openAiDeployments` پیرا میٹر تبدیل کریں:

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

دستیاب ماڈلز اور ورژنز: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Azure علاقوں کی تبدیلی

مختلف علاقے میں تعیناتی کے لیے `infra/main.bicep` میں ترمیم کریں:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

GPT-5.2 کی دستیابی چیک کریں: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Bicep فائلز میں تبدیلی کے بعد انفراسٹرکچر کو اپ ڈیٹ کرنے کے لیے:

**Bash:**
```bash
# اے آر ایم ٹیمپلیٹ کو دوبارہ بنائیں
az bicep build --file infra/main.bicep

# تبدیلیوں کا پیش نظارہ کریں
azd provision --preview

# تبدیلیاں لگائیں
azd provision
```

**PowerShell:**
```powershell
# اے آر ایم ٹیمپلیٹ کو دوبارہ بنائیں
az bicep build --file infra/main.bicep

# تبدیلیوں کا پیش نظارہ
azd provision --preview

# تبدیلیاں نافذ کریں
azd provision
```

## صفائی

تمام وسائل حذف کرنے کے لیے:

**Bash:**
```bash
# تمام وسائل کو حذف کریں
azd down

# ماحول سميت سب کچھ حذف کریں
azd down --purge
```

**PowerShell:**
```powershell
# تمام وسائل کو حذف کریں
azd down

# ماحول سمیت سب کچھ حذف کریں
azd down --purge
```

**انتباہ**: یہ تمام Azure وسائل مستقل طور پر حذف کر دے گا۔

## فائل کی ساخت

## لاگت کی بچت

### ترقی/ٹیسٹنگ
ڈیولپمنٹ/ٹیسٹنگ ماحولیات کے لیے، لاگت کم کریں:
- Azure OpenAI کے لیے اسٹینڈرڈ سٹیئر (S0) استعمال کریں
- `infra/core/ai/cognitiveservices.bicep` میں صلاحیت کم کریں (10K TPM کے بجائے 20K)
- استعمال نہ ہونے پر وسائل حذف کریں: `azd down`

### پیداوار
پیداوار کے لیے:
- OpenAI صلاحیت استعمال کے مطابق بڑھائیں (50K+ TPM)
- زیادہ دستیابی کے لیے زون ریڈنڈنسی فعال کریں
- مناسب مانیٹرنگ اور لاگت کی اطلاع نافذ کریں

### لاگت کا اندازہ
- Azure OpenAI: ٹوکن کے حساب سے ادائیگی (ان پٹ + آؤٹ پٹ)
- GPT-5.2: تقریباً $3-5 فی 1M ٹوکن (موجودہ قیمت دیکھیں)
- text-embedding-3-small: تقریباً $0.02 فی 1M ٹوکن

پرائسنگ کیلکولیٹر: https://azure.microsoft.com/pricing/calculator/

## مانیٹرنگ

### Azure OpenAI میٹرکس دیکھیں

Azure پورٹل میں جائیں → اپنا OpenAI وسیلہ → میٹرکس:
- ٹوکن پر مبنی استعمال
- HTTP درخواست کی شرح
- جواب دینے کا وقت
- فعال ٹوکنز

## مسائل کا حل

### مسئلہ: Azure OpenAI سب ڈومین نام میں تصادم

**غلطی کا پیغام:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**وجہ:**
آپ کی سبسکرپشن/ماحول سے پیدا ہونے والا سب ڈومین نام پہلے سے استعمال میں ہے، ممکنہ طور پر پچھلی تعیناتی مکمل طریقے سے ختم نہیں ہوئی۔

**حل:**
1. **آپشن 1 - مختلف ماحول کا نام استعمال کریں:**
   
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

2. **آپشن 2 - Azure پورٹل کے ذریعے دستی تعیناتی:**
   - Azure پورٹل میں جائیں → نیا وسیلہ بنائیں → Azure OpenAI
   - اپنے وسیلہ کے لیے ایک منفرد نام منتخب کریں
   - درج ذیل ماڈلز تعینات کریں:
     - **GPT-5.2**
     - **text-embedding-3-small** (RAG ماڈیولز کے لیے)
   - **اہم:** تعیناتی کے نام نوٹ کریں - یہ `.env` تشکیل سے میل کھانے چاہئیں
   - تعیناتی کے بعد "Keys and Endpoint" سے اپنا اینڈ پوائنٹ اور API کلید حاصل کریں
   - پروجیکٹ روٹ میں `.env` فائل بنائیں جس میں:

     **مثال `.env` فائل:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**ماڈل تعیناتی کے نام دینے کے اصول:**
- سادہ اور مستقل نام استعمال کریں: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- تعیناتی کے نام بالکل ویسے ہی ہونے چاہئیں جیسے آپ `.env` میں ترتیب دیتے ہیں
- عام غلطی: ماڈل ایک نام سے بنانا اور کوڈ میں مختلف نام کا حوالہ دینا

### مسئلہ: منتخب کردہ علاقے میں GPT-5.2 دستیاب نہیں

**حل:**
- GPT-5.2 کی دستیابی والے علاقے کا انتخاب کریں (مثلاً eastus2)
- دستیابی چیک کریں: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### مسئلہ: تعیناتی کے لیے کوٹا ناکافی

**حل:**
1. Azure پورٹل میں کوٹا میں اضافہ کی درخواست کریں
2. یا `main.bicep` میں کم صلاحیت استعمال کریں (مثلاً capacity: 10)

### مسئلہ: مقامی طور پر چلانے پر "وسیلہ نہیں ملا"

**حل:**
1. تعیناتی کی تصدیق کریں: `azd env get-values`
2. اینڈ پوائنٹ اور کلید درست ہیں دیکھیں
3. Azure پورٹل میں Resource Group موجود ہے یقینی بنائیں

### مسئلہ: توثیق ناکام ہوئی

**حل:**
- `AZURE_OPENAI_API_KEY` کی درست سیٹنگ کی تصدیق کریں
- کلید کا فارمیٹ 32-حروف پر مشتمل ہیکسادسیمل ہونا چاہیے
- ضرورت پڑنے پر Azure پورٹل سے نیا کی حاصل کریں

### تعیناتی ناکام ہو گئی

**مسئلہ**: `azd provision` کوٹا یا صلاحیت کی غلطیوں کے ساتھ ناکام ہو جاتا ہے

**حل**: 
1. مختلف علاقے کا انتخاب کریں - علاقوں کی ترتیب کے لیے [Changing Azure Regions](../../../../01-introduction/infra) سیکشن دیکھیں
2. آپ کی سبسکرپشن میں Azure OpenAI کوٹا ہو چیک کریں:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### ایپلیکیشن کنکشن نہیں کر رہی

**مسئلہ**: جاوا ایپلیکیشن کنکشن کی غلطیاں ظاہر کرتی ہے

**حل**:
1. ماحولیاتی متغیرات کی تصدیق کریں کہ وہ ایکسپورٹ ہوئے ہیں:
   
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

2. اینڈ پوائنٹ کا فارمیٹ صحیح ہے (مثلاً `https://xxx.openai.azure.com`)
3. API کلید Azure پورٹل کی پرائمری یا سیکنڈری کی ہے یہ تصدیق کریں

**مسئلہ**: Azure OpenAI سے 401 غیرمجاز

**حل**:
1. Azure پورٹل → Keys and Endpoint سے نیا API کلید حاصل کریں
2. `AZURE_OPENAI_API_KEY` ماحول کے متغیر کو دوبارہ ایکسپورٹ کریں
3. ماڈل کی تعیناتیاں مکمل ہیں یہ یقینی بنائیں (Azure پورٹل چیک کریں)

### کارکردگی کے مسائل

**مسئلہ**: ردعمل کا وقت سست ہے

**حل**:
1. Azure پورٹل میٹرکس میں OpenAI ٹوکن استعمال اور تھروٹلنگ چیک کریں
2. اگر حدیں پہنچ رہی ہوں تو TPM صلاحیت بڑھائیں
3. زیادہ reasoning-effort (کم/درمیانہ/زیادہ) استعمال کرنے پر غور کریں

## انفراسٹرکچر کی تازہ کاری

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

## سیکیورٹی کی سفارشات

1. **کبھی API کیز کو کمیٹ نہ کریں** - ماحول کے متغیرات استعمال کریں
2. **مقامی طور پر .env فائل استعمال کریں** - `.env` کو `.gitignore` میں شامل کریں
3. **کیز کو باقاعدگی سے تبدیل کریں** - Azure پورٹل میں نئی کیز بنائیں
4. **رسائی محدود کریں** - Azure RBAC استعمال کریں کہ کون وسائل تک رسائی رکھتا ہے
5. **استعمال کی نگرانی کریں** - Azure پورٹل میں لاگت کی اطلاع لگائیں

## مزید وسائل

- [Azure OpenAI سروس دستاویزات](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 ماڈل دستاویزات](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI دستاویزات](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep دستاویزات](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI سرکاری انضمام](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## سپورٹ

مسائل کے لیے:
1. اوپر موجود [مسائل کا حل](../../../../01-introduction/infra) سیکشن دیکھیں
2. Azure پورٹل میں Azure OpenAI سروس کی صحت کا جائزہ لیں
3. ریپوزیٹری میں مسئلہ کھولیں

## لائسنس

تفصیلات کے لیے روٹ [LICENSE](../../../../LICENSE) فائل دیکھیں۔

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**اہم نوٹ**:  
یہ دستاویز AI ترجمہ سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کے ذریعے ترجمہ کی گئی ہے۔ اگرچہ ہم درستگی کے لیے کوشش کرتے ہیں، براہ کرم یہ بات ذہن میں رکھیں کہ خودکار ترجمے میں غلطیاں یا عدم درستیاں ہو سکتی ہیں۔ اصل دستاویز اپنی مادری زبان میں مستند ماخذ سمجھی جانی چاہیے۔ اہم معلومات کے لیے پیشہ ور ماہر انسانی ترجمہ کی سفارش کی جاتی ہے۔ اس ترجمے کے استعمال سے پیدا ہونے والے کسی بھی غلط فہمی یا غلط تشریح کی ذمہ داری ہم پر نہیں ہوگی۔
<!-- CO-OP TRANSLATOR DISCLAIMER END -->