# បរិក្ខារជាលំហូរសម្រាប់ LangChain4j ការចាប់ផ្តើម

## មាតិកា

- [អ្វីដែលត្រូវមាន](#អ្វីដែលត្រូវមាន)
- [សំណុំពាណិជ្ជកម្ម](#សំណុំពាណិជ្ជកម្ម)
- [ធនធានដែលបានបង្កើត](#ធនធានដែលបានបង្កើត)
- [ការចាប់ផ្តើមរហ័ស](#ការចាប់ផ្តើមរហ័ស)
- [ការកំណត់រចនា](#ការកំណត់រចនា)
- [ពាក្យបញ្ជាមេណេជឺម៉ិន](#management-commands)
- [ការតំលៃថ្លៃដើម](#ការតំលៃថ្លៃដើម)
- [ការត្រួតពិនិត្យ](#ការត្រួតពិនិត្យ)
- [ដោះស្រាយបញ្ហា](#ដោះស្រាយបញ្ហា)
- [ការបច្ចុប្បន្នភាពបរិក្ខារ](#ការបច្ចុប្បន្នភាពបរិក្ខារ)
- [ការសម្អាត](#សម្អាត)
- [រចនាសម្ព័ន្ធឯកសារ](#រចនាសម្ព័ន្ធឯកសារ)
- [អនុសាសន៍សុវត្ថិភាព](#អនុសាសន៍សុវត្ថិភាព)
- [ធនធានបន្ថែម](#ធនធានបន្ថែម)

ថតនេះមានបរិក្ខារ Azure ជា code (IaC) ប្រើប្រាស់ Bicep និង Azure Developer CLI (azd) សម្រាប់ដាក់ចេញធនធាន Azure OpenAI។

## អ្វីដែលត្រូវមាន

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (កំណែ 2.50.0 ឬចុងក្រោយ)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (កំណែ 1.5.0 ឬចុងក្រោយ)
- មេដឹកនាំ Azure ដែលមានសិទ្ធិបង្កើតធនធាន

## សំណុំពាណិជ្ជកម្ម

**ការកំណត់បរិក្ខារអភិវឌ្ឍន៍ក្នុងកំណត់និកាយ** - ដាក់ចេញ Azure OpenAI ប៉ុណ្ណោះ និងដំណើរការប្រព័ន្ធទាំងអស់នៅក្នុងកុំព្យូទ័រផ្ទាល់ខ្លួន។

បរិក្ខារ deploy ធនធាន Azure ខាងក្រោម៖

### សេវាកម្ម AI
- **Azure OpenAI**: សេវាកម្មស្វ័យប្រវត្តិក្នុងវិស័យប្រៀបធៀបមានការប្រើប្រាស់ម៉ូដែលពីរដែលបានដាក់ចេញ៖
  - **gpt-5.2**: ម៉ូដែលបញ្ចប់ការជជែក (សមត្ថភាព 20K TPM)
  - **text-embedding-3-small**: ម៉ូដែលបញ្ចូលសញ្ញាសម្រាប់ RAG (សមត្ថភាព 20K TPM)

### អភិវឌ្ឍន៍ក្នុងកុំព្យូទ័រផ្ទាល់ខ្លួន
កម្មវិធី Spring Boot ទាំងអស់ដំណើរការនៅក្នុងកុំព្យូទ័ររបស់អ្នក៖
- 01-introduction (ពេញ 8080)
- 02-prompt-engineering (ពេញ 8083)
- 03-rag (ពេញ 8081)
- 04-tools (ពេញ 8084)

## ធនធានដែលបានបង្កើត

| ប្រភេទធនធាន | ម៉ូតបទ្រង់ទ្រាយឈ្មោះធនធាន | គោលបំណង |
|--------------|-------------------------------|-----------|
| Resource Group | `rg-{environmentName}` | រួមបញ្ចូលធនធានទាំងអស់ |
| Azure OpenAI | `aoai-{resourceToken}` | ផ្ទុកម៉ូដែល AI |

> **សម្គាល់:** `{resourceToken}` គឺជាតួអក្សរចម្លែកដែលបង្កើតពី subscription ID, environment name និងទីតាំង

## ការចាប់ផ្តើមរហ័ស

### 1. ដាក់ចេញ Azure OpenAI

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

ពេលមានការស្នើសុំ:
- ជ្រើសរើសប្រព័ន្ធមេដឹកនាំ Azure របស់អ្នក
- ជ្រើសទីតាំង (ផ្តើមបានល្អ៖ `eastus2` សម្រាប់មាន GPT-5.2)
- បញ្ជាក់ឈ្មោះបរិក្ខារ (លំនាំដើម៖ `langchain4j-dev`)

នេះនឹងបង្កើត៖
- ធនធាន Azure OpenAI មាន GPT-5.2 និង text-embedding-3-small
- ផ្តល់ព័ត៌មានការតភ្ជាប់ចេញ

### 2. ទទួលព័ត៌មានការតភ្ជាប់

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

នេះបង្ហាញ៖
- `AZURE_OPENAI_ENDPOINT`: URL endpoint របស់ Azure OpenAI
- `AZURE_OPENAI_KEY`: គន្លឹះ API សម្រាប់ authenticate
- `AZURE_OPENAI_DEPLOYMENT`: ឈ្មោះម៉ូដែលជជែក (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: ឈ្មោះម៉ូដែល embed

### 3. រត់កម្មវិធីក្នុងកុំព្យូទ័រផ្ទាល់ខ្លួន

ពាក្យបញ្ជា `azd up` បង្កើតឯកសារ `.env` នៅថតដើមជាមួយអថេរបរិយាកាសទាំងអស់ដែលត្រូវការ។

**ណែនាំ:** ចាប់ផ្តើមកម្មវិធីវ៉េបទាំងអស់៖

**Bash:**
```bash
# ពីថតឫស
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# ពីថតកំណត់ឫស
cd ../..
.\start-all.ps1
```

ឬចាប់ផ្តើមម៉ូឌុលតែមួយ៖

**Bash:**
```bash
# ឧទាហរណ៍ៈ ចាប់ផ្តើមគ្រាប់នៃយ៉ាងតែម៉ូឌុលប مقدمه
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# ឧទាហរណ៍៖ ចាប់ផ្តើមបានតែម៉ូឌុលណែនាំតែប៉ុណ្ណោះ
cd ../01-introduction
.\start.ps1
```

ស្គ្រីបទាំងពីរត្រូវបញ្ចូលអថេរបរិយាកាសពីឯកសារ `.env` ដែលបានបង្កើតដោយ `azd up` នៅថតដើមដោយស្វ័យប្រវត្តិ។

## ការកំណត់រចនា

### ការប្ដូរតម្លើងម៉ូដែល

ដើម្បីផ្លាស់ប្ដូរតម្លើងម៉ូដែល រៀបចំឯកសារ `infra/main.bicep` ហើយកែប្រែប៉ារ៉ាម៉ែត្រ `openAiDeployments`៖

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

ម៉ូដែល និងកំណែក្នុង https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### ការប្ដូរតំបន់ Azure

ដើម្បី deploy នៅតំបន់ផ្សេង ដំណើរការខាងក្រោមនៅក្នុង `infra/main.bicep`៖

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

ពិនិត្យភាពទំនេររបស់ GPT-5.2: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

ដើម្បីធ្វើបច្ចុប្បន្នភាពបរិក្ខារបន្ទាប់ពីកែប្រែកម្មវិធី Bicep៖

**Bash:**
```bash
# សាងសង់ពុម្ពអក្សរ ARM កំណែថ្មី
az bicep build --file infra/main.bicep

# មើលជាមុននូវការផ្លាស់ប្តូរ
azd provision --preview

# អនុវត្តការផ្លាស់ប្តូរ
azd provision
```

**PowerShell:**
```powershell
# សាងសង់គំរូ ARM វិញ
az bicep build --file infra/main.bicep

# 미리ឃើញការផ្លាស់ប្តូរ
azd provision --preview

# អនុវត្តការផ្លាស់ប្តូរ
azd provision
```

## សម្អាត

ដើម្បីលុបធនធានទាំងអស់៖

**Bash:**
```bash
# លុបធនធានទាំងអស់
azd down

# លុបអ្វីគ្រប់យ៉ាង រួមទាំងបរិយាកាស
azd down --purge
```

**PowerShell:**
```powershell
# លុបធនធានទាំងអស់
azd down

# លុបគ្រប់យ៉ាងរួមទាំងបរិយាកាស
azd down --purge
```

**ការព្រមាន**: នេះនឹងលុបធនធាន Azure ទាំងអស់ថPermanent។

## រចនាសម្ព័ន្ធឯកសារ

## ការតំលៃថ្លៃដើម

### អភិវឌ្ឍន៍/សាកល្បង
សម្រាប់បរិយាកាស dev/test អ្នកអាចកាត់បន្ថយថ្លៃដើម៖
- ប្រើថ្នាក់ធម្មតា (S0) សម្រាប់ Azure OpenAI
- កំណត់សមត្ថភាពទាបជាង (10K TPM ជំនួស 20K) នៅក្នុង `infra/core/ai/cognitiveservices.bicep`
- លុបធនធានពេលមិនប្រើ៖ `azd down`

### ផលិត
សម្រាប់ផលិត៖
- បន្ថែមសមត្ថភាព OpenAI អាស្រ័យលើការប្រើប្រាស់ (50K+ TPM)
- បើកដំណើរការជំរៅរង្វង់សម្រាប់ភាពទំនេរខ្ពស់
- អនុវត្តត្រួតពិនិត្យនិងការជូនដំណឹងថ្លៃដើមត្រឹមត្រូវ

### ប៉ារ៉ាម៉ែត្រតំលៃ
- Azure OpenAI: បង់លើរាល់Token (បញ្ចូល +បញ្ចេញ)
- GPT-5.2: ~$3-5 ក្នុង 1M token (ពិនិត្យតំលៃបច្ចុប្បន្ន)
- text-embedding-3-small: ~$0.02 ក្នុង 1M token

កម្មវិធីគណនាតំលៃ: https://azure.microsoft.com/pricing/calculator/

## ការត្រួតពិនិត្យ

### មើលមាត្រដ្ឋាន Azure OpenAI

ចូលទៅ Azure Portal → ធនធាន OpenAI របស់អ្នក → មាត្រដ្ឋាន:
- ការប្រើប្រាស់ប្រកាសដោយToken
- អត្រាការស្នើសុំពី HTTP
- ពេលចប់ការឆ្លើយតប
- Token ប្រើសកម្ម

## ដោះស្រាយបញ្ហា

### បញ្ហា: ឈ្មោះ subdomain Azure OpenAI បែបជConflict

**សារ Error:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**មូលហេតុ:**
ឈ្មោះ subdomain ដែលបង្កើតពី subscription/environment របស់អ្នកបានប្រើរួចហើយ ប្រហែលមកពី deploy មុនមិនបានលុបស្អាត។

**ដំណោះស្រាយ:**
1. **ជម្រើស 1 - ប្រើឈ្មោះបរិយាកាសផ្សេង:**
   
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

2. **ជម្រើស 2 - ដាក់ដោយដៃតាម Azure Portal:**
   - ចូល Azure Portal → បង្កើតធនធានថ្មី → Azure OpenAI
   - ជ្រើសឈ្មោះធនធានទាក់ទង
   - ដាក់ម៉ូដែលខាងក្រោម៖
     - **GPT-5.2**
     - **text-embedding-3-small** (សម្រាប់ម៉ូឌុល RAG)
   - **សំខាន់:** ចងចាំឈ្មោះ deployment ត្រូវតែផ្គូផ្គង `.env`
   - បន្ទាប់ពី deploy សូមទទួល endpoint និង API key ពី "Keys and Endpoint"
   - បង្កើត `.env` នៅថតគំរោង មាន៖
     
     **ឧទាហរណ៍ .env៖**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**ច្បាប់ឈ្មោះ Deployment ម៉ូដែល:**
- ប្រើឈ្មោះសាមញ្ញ មានប្រក្រតី៖ `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- ឈ្មោះ deployment ត្រូវតែផ្គូផ្គងអោយត្រឹមត្រូវជាមួយ `.env`
- កំហុសរួមគឺបង្កើតម៉ូដែលឈ្មោះមួយ ប៉ុន្តែកូដយោងឈ្មោះផ្សេង

### បញ្ហា: GPT-5.2 មិនមាននៅតំបន់បានជ្រើស

**ដំណោះស្រាយ:**
- ជ្រើសតំបន់ដែលមាន GPT-5.2 (ឧ. eastus2)
- ពិនិត្យភាពទំនេរ: https://learn.microsoft.com/azure/ai-services/openai/concepts/models



### បញ្ហា: សមត្ថភាព deployment មិនគ្រប់គ្រាន់

**ដំណោះស្រាយ:**
1. ស្នើសុំបន្ថែមសមត្ថភាពនៅ Azure Portal
2. ឬប្រើសមត្ថភាពទាបជាងក្នុង `main.bicep` (ឧ. capacity: 10)

### បញ្ហា: "Resource not found" ពេលរត់ក្នុងកន្លែង

**ដំណោះស្រាយ:**
1. បញ្ជាក់ deployment: `azd env get-values`
2. ពិនិត្យ endpoint និងគន្លឹះត្រឹមត្រូវ
3. ប្រាកដថា resource group មាននៅក្នុង Azure Portal

### បញ្ហា: Authentication បរាជ័យ

**ដំណោះស្រាយ:**
- ពិនិត្យ `AZURE_OPENAI_API_KEY` ត្រូវបានកំណត់ត្រឹមត្រូវ
- រូបមន្តគន្លឹះគឺជា string hexadecimal 32 ខ្ទង់
- ទទួលគន្លឹះថ្មីពី Azure Portal ប្រសិនបើចាំបាច់

### បញ្ហា Deployment បរាជ័យ

**បញ្ហា**: `azd provision` បរាជ័យដោយសារកំហុស quota ឬ capacity

**ដំណោះស្រាយ**: 
1. ព្យាយามក្នុងតំបន់ផ្សេង - មើលផ្នែក [Changing Azure Regions](#ការប្ដូរតំបន់-azure) ដើម្បីកំណត់តំបន់
2. ពិនិត្យថា subscription មាន quota Azure OpenAI៖
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### កម្មវិធីមិនបានតភ្ជាប់

**បញ្ហា**: កម្មវិធី Java បង្ហាញកំហុសតភ្ជាប់

**ដំណោះស្រាយ**:
1. ពិនិត្យអថេរបរិយាកាសបាន export:
   
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

2. ពិនិត្យរាងហៅ endpoint ត្រឹមត្រូវ (គួរតែជា `https://xxx.openai.azure.com`)
3. ប្រាកដថាគន្លឹះ API ជាគន្លឹះថ្នាក់ទីមួយ ឬទីពីរពី Azure Portal

**បញ្ហា**: 401 Unauthorized ពី Azure OpenAI

**ដំណោះស្រាយ**:
1. ទទួលគន្លឹះ API ថ្មីពី Azure Portal → Keys and Endpoint
2. បន្សល់ export លេខ `AZURE_OPENAI_API_KEY` ថ្មី
3. ប្រាកដថា deployment ម៉ូដែលបានបញ្ចប់ (ពិនិត្យ Azure Portal)

### បញ្ហាប្រសិទ្ធភាព

**បញ្ហា**: ពេលចម្លើយយឺត

**ដំណោះស្រាយ**:
1. ពិនិត្យការប្រើប្រាស់ token និងការបង្ហាប់នៅលើ metrics នៃ Azure Portal
2. បន្ថែមសមត្ថភាព TPM បើទទួលពោលដល់កំណត់
3. ពិចារណាការប្រើប្រាស់ reasoning-effort ខ្ពស់ (ទាប/មធ្យម/ខ្ពស់)

## ការបច្ចុប្បន្នភាពបរិក្ខារ

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

## អនុសាសន៍សុវត្ថិភាព

1. **កុំចុះ commit គន្លឹះ API** - ប្រើអថេរបរិយាកាស
2. **ប្រើឯកសារ .env ក្នុងកុំព្យូទ័រផ្ទាល់ខ្លួន** - បន្ថែម `.env` ទៅ `.gitignore`
3. **បង្វិលគន្លឹះជាប្រចាំ** - បង្កើតគន្លឹះថ្មី ក្នុង Azure Portal
4. **កំណត់សិទ្ធិចូលប្រើ** - ប្រើ Azure RBAC ដើម្បីគ្រប់គ្រងអ្នកចូលប្រើ
5. **តាមដានការប្រើប្រាស់** - កំណត់សេចក្តីជូនដំណឹងថ្លៃក្នុង Azure Portal

## ធនធានបន្ថែម

- [ឯកសារសេវា Azure OpenAI](https://learn.microsoft.com/azure/ai-services/openai/)
- [ឯកសារម៉ូដែល GPT-5.2](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [ឯកសារ Azure Developer CLI](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [ឯកសារ Bicep](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI រួមបញ្ចូលផ្លូវការជាមួយ](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## គាំទ្រ

សម្រាប់បញ្ហា៖
1. ពិនិត្យផ្នែក [ដោះស្រាយបញ្ហា](#ដោះស្រាយបញ្ហា) ខាងលើ
2. ពិនិត្យសុខភាពសេវា Azure OpenAI នៅក្នុង Azure Portal
3. បើកបញ្ហាជាមួយកន្លែងរកចុះបញ្ជី

## អាជ្ញាបណ្ណ

មើលឯកសារ [LICENSE](../../../../LICENSE) ដែលនៅថតដើមសម្រាប់ព័ត៌មានលម្អិត។

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ការបដិសេធ**៖  
ឯកសារនេះត្រូវបានបកប្រែដោយប្រើសេវាកម្មបកប្រែ AI [Co-op Translator](https://github.com/Azure/co-op-translator)។ ខណៈពេលដែលយើងខិតខំសម្រាប់ភាពត្រឹមត្រូវ សូមជ្រាបថាការបកប្រែអូតូម៉ាទិចអាចមានកំហុសឬភាពមិនត្រឹមត្រូវ។ ឯកសារដែលមានភាសាតំបន់ដើមគួរត្រូវបានទទួលស្គាល់ជាតុបតែងផ្លូវការជាមូលដ្ឋាន។ សម្រាប់ព័ត៌មានសំខាន់ គួរប្រើប្រាស់ការបកប្រែដោយមនុស្សដែលមានវិជ្ជាជីវៈ។ យើងមិនទទួលខុសត្រូវចំពោះការយល់ច្រឡំ ឬការបកប្រែខុសផ្សេងៗណាមួយដែលកើតឡើងដោយការប្រើប្រាស់ការបកប្រែនេះទេ។
<!-- CO-OP TRANSLATOR DISCLAIMER END -->