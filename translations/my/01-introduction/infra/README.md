# LangChain4j အတွက် Azure အခြေခံအဆောက်အအုံ စတင်အသုံးပြုခြင်း

## အကြောင်းအရာ စာရင်း

- [လိုအပ်ချက်များ](../../../../01-introduction/infra)
- [ပုံဆွဲပုံ](../../../../01-introduction/infra)
- [ဖန်တီးထားသော အရင်းအမြစ်များ](../../../../01-introduction/infra)
- [လျင်မြန်စွာ စတင်ခြင်း](../../../../01-introduction/infra)
- [ဖွဲ့စည်းမှု](../../../../01-introduction/infra)
- [စီမံခန့်ခွဲမှု အမိန့်များ](../../../../01-introduction/infra)
- [ကုန်ကျစရိတ်တိုးတက်မှု](../../../../01-introduction/infra)
- [ကြည့်ရှုခြင်း](../../../../01-introduction/infra)
- [ပြဿနာဖြေရှင်းခြင်း](../../../../01-introduction/infra)
- [အဆောက်အအုံသစ် ပြုပြင်မွမ်းမံခြင်း](../../../../01-introduction/infra)
- [ရှင်းလင်းခြင်း](../../../../01-introduction/infra)
- [ဖိုင် ဖွဲ့စည်းမှု](../../../../01-introduction/infra)
- [လုံခြုံရေး အကြံဉာဏ်များ](../../../../01-introduction/infra)
- [ထပ်ဆောင်း အရင်းအမြစ်များ](../../../../01-introduction/infra)

ဤ ဖိုလ်ဒါတွင် Bicep နှင့် Azure Developer CLI (azd) ကိုအသုံးပြု၍ Azure OpenAI အရင်းအမြစ်များကို တပ်ဆင်ရန် Azure အခြေခံအဆောက်အအုံ အနေဖြင့် ကုဒ် (IaC) ထည့်သွင်းထားသည်။

## လိုအပ်ချက်များ

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (ဗားရှင်း 2.50.0 သို့မဟုတ် နောက်ပိုင်း)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (ဗားရှင်း 1.5.0 သို့မဟုတ် နောက်ပိုင်း)
- အရင်းအမြစ်ဖန်တီးခွင့်ရှိသည့် Azure အကောင့်

## ပုံဆွဲပုံ

**ရိုးရိုးရှင်းရှင်း ဒေသခံဖွံ့ဖြိုးတိုးတက်မှု ပတ်ဝန်းကျင်** - Azure OpenAI ကိုသာ တပ်ဆင်ပြီး အက်ပ်များအားလုံးကို ဒေသခံတွင် ပြေးဆွဲမည်။

အောက်ပါ Azure အရင်းအမြစ်များ တပ်ဆင်မည်။

### AI ဝန်ဆောင်မှုများ
- **Azure OpenAI**: ၂ မော်ဒယ် တပ်ဆင်ထားသည့် Cognitive Services:
  - **gpt-5.2**: စကားပြောပြီးစီးခြင်း မော်ဒယ် (20K TPM အသုံးပြုနိုင်မှု)
  - **text-embedding-3-small**: RAG အတွက် embedding မော်ဒယ် (20K TPM အသုံးပြုနိုင်မှု)

### ဒေသခံဖွံ့ဖြိုးတိုးတက်မှု
အားလုံး Spring Boot အက်ပ်များကို သင်၏ စက်ပေါ်တွင် ဒေသခံအတိုင်း ပြေးဆွဲမည်။
- 01-introduction (ပေါ့(တ်) 8080)
- 02-prompt-engineering (ပေါ့(တ်) 8083)
- 03-rag (ပေါ့(တ်) 8081)
- 04-tools (ပေါ့(တ်) 8084)

## ဖန်တီးထားသော အရင်းအမြစ်များ

| အရင်းအမြစ် အမျိုးအစား | အရင်းအမြစ် အမည် ပုံစံ | ရည်ရွယ်ချက် |
|--------------|----------------------|---------|
| Resource Group | `rg-{environmentName}` | အရင်းအမြစ်အားလုံး ပါရှိသော အုပ်စု |
| Azure OpenAI | `aoai-{resourceToken}` | AI မော်ဒယ် ပလက်ဖောင်း |

> **မှတ်ချက်:** `{resourceToken}` သည် subscription ID၊ ပတ်ဝန်းကျင် အမည်နှင့် တည်နေရာမှ ထုတ်လုပ်သော ထူးခြားသော string ဖြစ်သည်။

## လျင်မြန်စွာ စတင်ခြင်း

### 1. Azure OpenAI တပ်ဆင်ခြင်း

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

တောင်းဆိုလိုက်သောအခါ:
- သင့် Azure subscription ကို ရွေးချယ်ပါ
- တည်နေရာကို ရွေးချယ်ပါ (အကြံပြုချက်: GPT-5.2 ရရှိနိုင်သည့် `eastus2`)
- ပတ်ဝန်းကျင် အမည်အား အတည်ပြုပါ (ပုံမှန်တန်ဖိုး: `langchain4j-dev`)

ဒါကြောင့် ပြုလုပ်ရန်:
- GPT-5.2 နှင့် text-embedding-3-small ပါရှိသည့် Azure OpenAI အရင်းအမြစ်
- ချိတ်ဆက်မှုအသေးစိတ် များထုတ်ပေးမည်

### 2. ချိတ်ဆက်မှု အသေးစိတ် ရယူခြင်း

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

ဤသည်သည် ပြသပါမည်
- `AZURE_OPENAI_ENDPOINT`: သင်၏ Azure OpenAI နယ်နိမိတ် URL
- `AZURE_OPENAI_KEY`: အတည်ပြုရေး API key
- `AZURE_OPENAI_DEPLOYMENT`: စကားပြောမော်ဒယ်အမည် (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: embedding မော်ဒယ်အမည်

### 3. ဒေသခံ၌ အက်ပ်များ ပြေးဆွဲခြင်း

`azd up` အမိန့်အား Root ဖိုလ်ဒါ၌ `.env` ဖိုင်ကို အလိုအလျောက် ဖန်တီးပြီး လိုအပ်သော ပတ်ဝန်းကျင် ဗယ်ကြယ်ကိုလည်း ထည့်သွင်းမည်။

**သင့်တော်သော**: ဝက်ဘ် အက်ပ်များအားလုံး စတင်ကြောင်း

**Bash:**
```bash
# မူရင်းဖိုင်တွဲမှ
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# မူရင်းဖိုင်အညွှန်းကြီးမှ
cd ../..
.\start-all.ps1
```

သို့မဟုတ် တစ်ခုတည်း Module ကိုစတင်ရန်

**Bash:**
```bash
# နမူနာ- မိတ်ဆက် module ကိုသာ စတင်ရန်
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# ဥပမာ - စတင်ရန်ဘာသာရပ်မိတ်ဆက်မော်ဂျူးကိုသာအစပြုပါ
cd ../01-introduction
.\start.ps1
```

အဆိုပါ script နှစ်ခုစလုံး မူလ `.env` ဖိုင်မှ ပတ်ဝန်းကျင် ဗယ်ကြယ်များအား လုပ်ပြီး ပြုလုပ်နေသည်။

## ဖွဲ့စည်းမှု

### မော်ဒယ် တပ်ဆင်မှုကို အလိုက်အပြောင်း ပြုလုပ်ခြင်း

မော်ဒယ်တပ်ဆင်မှုများ ပြောင်းလဲရန် `infra/main.bicep` ကို တည်းဖြတ်ပြီး `openAiDeployments` ပါရာမီတာကို အသစ်ပြင်ဆင်ပါ။

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

ရရှိနိုင်သည့် မော်ဒယ်များနှင့် ဗားရှင်းများ: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Azure ဒေသဗျဉ်းပြောင်းခြင်း

မတူညီသော ဒေသ၌ တပ်ဆင်လိုပါက `infra/main.bicep` ကို တည်းဖြတ်ပါ။

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

GPT-5.2 ရရှိနိုင်မှု စစ်ဆေးရန်: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Bicep ဖိုင်များ ပြောင်းလဲပြီးနောက် အဆောက်အအုံအား Update ပြုလုပ်ရန်:

**Bash:**
```bash
# ARM  ပုံစံကို ပြန်လည်တည်ဆောက်ပါ
az bicep build --file infra/main.bicep

# ပြောင်းလဲမှုများကို ကြိုကြည့်ပါ
azd provision --preview

# ပြောင်းလဲမှုများကို သက်ဆိုင်ရာ အပေါ်တွင် အကျိုးတူလုပ်ဆောင်ပါ
azd provision
```

**PowerShell:**
```powershell
# ARM မှာပုံစံကို ထပ်မံတည်ဆောက်ပါ
az bicep build --file infra/main.bicep

# ပြင်ဆင်မှုများကို ကြိုမြင်ကြည့်ရှုပါ
azd provision --preview

# ပြင်ဆင်မှုများကို အကောင်အထည်ဖော်ပါ
azd provision
```

## ရှင်းလင်းခြင်း

အရင်းအမြစ်များအားလုံး ဖျက်ရန်:

**Bash:**
```bash
# ရင်းမြစ်အားလုံးဖျက်ပါ
azd down

# ပတ်ဝန်းကျင်အပါအဝင် အားလုံးကိုဖျက်ပါ
azd down --purge
```

**PowerShell:**
```powershell
# အရင်းအမြစ်များအားလုံးကို ဖျက်ပစ်ပါ
azd down

# ပတ်ဝန်းကျင်အပါအဝင် အားလုံးကို ဖျက်ပစ်ပါ
azd down --purge
```

**သတိပေးချက်**: ဤလုပ်ဆောင်ချက်သည် Azure အရင်းအမြစ်အားလုံးကို တစ်သက်တန်ဖိုး ဖျက်သိမ်းမည်။

## ဖိုင် ဖွဲ့စည်းမှု

## ကုန်ကျစရိတ် တိုးတက်မှု

### ဖွံ့ဖြိုးတိုးတက်မှု/စမ်းသပ်မှု
ဖွံ့ဖြိုးမှု/စမ်းသပ်မှု ပတ်ဝန်းကျင်များအပါအဝင် အောက်ပါအတိုင်း ကုန်ကျစရိတ် ဖြတ်တောက်နိုင်သည်
- Azure OpenAI အတွက် Standard tier (S0) အသုံးပြုခြင်း
- `infra/core/ai/cognitiveservices.bicep` တွင် အသုံးပြုခွင့် ကို (10K TPM ထိလျှော့ချခြင်း)
- မသုံးအပ်စဉ်တွင် အရင်းအမြစ်များ ဖျက်ခြင်း (azd down)

### ထုတ်လုပ်မှု
ထုတ်လုပ်မှုအတွက်
- အသုံးပြုမှုအခြေခံ capacity တိုးမြှင့်ခြင်း (50K+ TPM)
- Zone redundancy အတွက် ပိုမိုမြင့်မားသော ရရှိနိုင်မှု တိုးမြှင့်ခြင်း
- သက်ဆိုင်ရာ ကြည့်ရှုမှုနှင့် ကုန်ကျစရိတ် သတိပေးမှု ထည့်သွင်းတက်လုပ်ခြင်း

### ကုန်ကျစရိတ် ခန့်မှန်းခြင်း
- Azure OpenAI: Token များအပေါ် Pay-per-token (input + output)
- GPT-5.2: တစ်မီလီယံ token အတွက် ကျပ် ၃-၅ (လက်ရှိ စျေးနှုန်း ကြည့်ပါ)
- text-embedding-3-small: တစ်မီလီယံ token အတွက် ကျပ် ၀.၀၂

စျေးနှုန်းတွက်ချက်ရန်: https://azure.microsoft.com/pricing/calculator/

## ကြည့်ရှုခြင်း

### Azure OpenAI အချက်အလက်များ ကြည့်ရှုခြင်း

Azure Portal → သင့် OpenAI အရင်းအမြစ် → Metrics:
- Token-Based အသုံးပြုမှု
- HTTP တောင်းဆိုမှုနှုန်း
- ပြန်တုံ့ပြန်ချိန်
- လှုပ်ရှားနေသည့် Tokens

## ပြဿနာဖြေရှင်းခြင်း

### ပြဿနာ: Azure OpenAI subdomain အမည် သရုပ်ခွဲမှု

**အမှားစာပြောချက်:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**အကြောင်းအရင်း:**
သင့် subscription/ပတ်ဝန်းကျင်မှ ထုတ်လုပ်သည့် subdomain အမည်သည် ယခင် တပ်ဆင်မှုမှ ကြာမြင့်စွာ မဖျက်ထားခြင်းကြောင့် ရှိပြီးသားဖြစ်နိုင်သည်။

**ဖြေရှင်းနည်း:**
1. **နည်းလမ်း ၁ - မတူညီသော ပတ်ဝန်းကျင်အမည် အသုံးပြုခြင်း:**
   
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

2. **နည်းလမ်း ၂ - Azure Portal မှ လက်မခံ တပ်ဆင်ခြင်း:**
   - Azure Portal → Create a resource → Azure OpenAI သို့ သွားပါ
   - မူရင်းအမည် ထူးခြားသော နာမည် ရွေးချယ်ပါ
   - အောက်ပါ မော်ဒယ်များ တပ်ဆင်ပါ:
     - **GPT-5.2**
     - **text-embedding-3-small** (RAG မော်ဂျူးများအတွက်)
   - **အရေးကြီး:** သင့် တပ်ဆင်ရေးအမည်များကို `.env` ဖိုင်နှင့် ကိုက်ညီစေရန် မှတ်သားပါ
   - တပ်ဆင်ပြီးနောက် "Keys and Endpoint" မှ endpoint နှင့် API key ရယူပါ
   - စီမံကိန်း အခြေခံဖိုလ်ဒါတည်နေရာတွင် `.env` ဖိုင် တစ်ခု ဖန်တီးပါ:

     **ဥပမာ `.env` ဖိုင်:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**မော်ဒယ် တပ်ဆင်ရေး အမည် သတ်မှတ်ချက်များ:**
- ရိုးရှင်း၍ တည့်တည့်သော နာမည်များ အသုံးပြုပါ: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- တပ်ဆင်ရေး အမည်များသည် `.env` တွင် ကိုက်ညီမှသာ ရပါမည်
- မှားယွင်းမှုများသည် မော်ဒယ်တစ်ခုအမည်ဖြင့် ဖန်တီးပြီး သင့်ကုဒ်တွင် အမည်ခြားခြား သုံးခြင်းဖြစ်သည်

### ပြဿနာ: GPT-5.2 ရွေးချယ်ထားသော ဒေသတွင် မရရှိနိုင်ခြင်း

**ဖြေရှင်းနည်း:**
- GPT-5.2 ရရှိနိုင်သော ဒေသရွေးပါ (ဥပမာ eastus2)
- ရရှိနိုင်မှု စစ်ဆေးရန်: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### ပြဿနာ: တပ်ဆင်မှုအတွက် ကော်တာမလုံလောက်ခြင်း

**ဖြေရှင်းနည်း:**
1. Azure Portal တွင် ကော်တာ တိုးမြှင့်ရန် တောင်းဆိုပါ
2. ဒုတိယအဖြစ် `main.bicep` တွင် capacity ကိုနည်းပါးစေရန် ပြင်ဆင်ပါ (ဥပမာ capacity: 10)

### ပြဿနာ: ဒေသခံတွင် "Resource not found" မြင်ခြင်း

**ဖြေရှင်းနည်း:**
1. တပ်ဆင်မှုကို `azd env get-values` ဖြင့် စစ်ဆေးပါ
2. endpoint နှင့် key မှန်ကန်ကြောင်း စစ်ဆေးပါ
3. Azure Portal တွင် Resource Group ရှိမရှိ ကြည့်ရှုပါ

### ပြဿနာ: အတည်ပြုချက် မအောင်မြင်ခြင်း

**ဖြေရှင်းနည်း:**
- `AZURE_OPENAI_API_KEY` ကို မှန်ကန်စွာ သတ်မှတ်ထားမှု စစ်ဆေးပါ
- key အမျိုးအစားမှာ 32-လုံး hexadecimal string ဖြစ်ဖို့ လိုအပ်သည်
- လိုအပ်ပါက Azure Portal မှ key အသစ် ရယူပါ

### တပ်ဆင်မှု မအောင်မြင်ခြင်း

**ပြဿနာ**: `azd provision` က ကော်တာ သို့မဟုတ် capacity အမှားများကြောင့်ပျက်သွားခြင်း

**ဖြေရှင်းနည်း**: 
1. မတူညီသော ဒေသကို စမ်းကြည့်ပါ - [Changing Azure Regions](../../../../01-introduction/infra) အပိုင်းကို ကြည့်ရှုပါ
2. သင့် subscription တွင် Azure OpenAI ကော်တာ ရှိမရှိ ရှာဖွေပါ

   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### အက်ပ် ချိတ်ဆက်မှု မရှိခြင်း

**ပြဿနာ**: Java အက်ပ်များတွင် ချိတ်ဆက်မှု အမှား ထွက်ရှိခြင်း

**ဖြေရှင်းနည်း**:
1. ပတ်ဝန်းကျင်ဗယ်များ ပေးပို့ပြီးကြောင်းစစ်ဆေးပါ
   
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

2. endpoint ပုံစံမှန်ကန်မှု စစ်ဆေးပါ (`https://xxx.openai.azure.com` ဖြစ်ဖို့လိုသည်)
3. API key သည် Azure Portal မှ လက်မှတ် မဟုတ် primary သို့ secondary key ဖြစ်ကြောင်း စစ်ဆေးပါ

**ပြဿနာ**: Azure OpenAI မှ 401 Unauthorized ပြန်ကြားချက်

**ဖြေရှင်းနည်း**:
1. Azure Portal → Keys and Endpoint မှ အသစ်သော API key ရယူပါ
2. `AZURE_OPENAI_API_KEY` ပတ်ဝန်းကျင်ဗယ်ပြန်သတ်မှတ်ပါ
3. မော်ဒယ် တပ်ဆင်မှု ပြီးစီးကြောင်းအတည်ပြုပါ (Azure Portal ကြည့်ရှုပါ)

### လုပ်ဆောင်မှု ပြဿနာများ

**ပြဿနာ**: တုံ့ပြန်ချိန် နှေးကွေးခြင်း

**ဖြေရှင်းနည်း**:
1. Azure Portal Metrics ထဲတွင် OpenAI token အသုံးပြုမှုနှင့် Throttling စစ်ဆေးပါ
2. TPM capacity တိုးမြှင့်ပါ သတ်မှန်းချက်ထိရောက်မှုရှိလျှင်
3. reasoning-effort အဆင့် (low/medium/high) ပိုမြင့်မားသောအဆင့်သို့ ရွေးချယ်ပါ

## အဆောက်အအုံ သစ် ပြုပြင်မွမ်းမံခြင်း

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

## လုံခြုံရေး အကြံဉာဏ်များ

1. **API key များကို အမြဲ မဝေဖန်ပါနှင့်** - ပတ်ဝန်းကျင် ဗယ်များ သာ အသုံးပြုပါ
2. **ဒေသခံတွင် .env ဖိုင်များ အသုံးပြုပါ** - `.env` ကို `.gitignore` တွင် ထည့်ပါ
3. **key များ အဆက်မပြတ် ရှေ့ပြေးပြောင်းပါ** - Azure Portal မှ အသစ်သော keys ဖန်တီးပါ
4. **ခွင့်များကို ကန့်သတ်ပါ** - Azure RBAC ဖြင့် ဘယ်သူ အသုံးပြုနိုင်မှုကို ထိန်းချုပ်ပါ
5. **အသုံးပြုမှုကို ကြည့်ရှုပါ** - Azure Portal မှ ကုန်ကျစရိတ် သတိပေးမှုများ သတ်မှတ်ပါ

## ထပ်ဆောင်း အရင်းအမြစ်များ

- [Azure OpenAI Service စာရွက်စာတမ်းများ](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 Model စာရွက်စာတမ်းများ](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI စာရွက်စာတမ်းများ](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep စာရွက်စာတမ်းများ](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI တရားဝင် ပေါင်းစပ်မှု](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## ထောက်ခံမှု

ပြဿနာများအတွက်:
1. အထက်ပါ [ပြဿနာဖြေရှင်းမှု အပိုင်း](../../../../01-introduction/infra) ကို စစ်ဆေးပါ
2. Azure Portal တွင် Azure OpenAI ဝန်ဆောင်မှု ကျန်းမာရေး ကြည့်ရှုပါ
3. Repository တွင် Issue တင်ပါ

## သက်ဆိုင်ရာလိုင်စင်

နောက်ခံ [LICENSE](../../../../LICENSE) ဖိုင်တွင် ကြည့်ရှုပါ။

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**မှတ်ချက်**:
ဤစာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) ဖြင့် ဘာသာပြန်ထားပါသည်။ တိကျမှုအတွက် ကြိုးပမ်း သော်လည်း စက်လွတ်ဘာသာပြန်ချက်များတွင် အမှားများ သို့မဟုတ် မှားယွင်းချက်များ ပါရှိနိုင်မှုကို ကျေးဇူးပြု၍ သတိပြုကြပါရန်။ မူရင်းစာတမ်းသည် မူအရင်းဘာသာဖြင့် တရားဝင်အရင်းအမြစ်အဖြစ် သတ်မှတ်ထားရမည်ဖြစ်သည်။ အရေးကြီးသောအချက်အလက်များအတွက် ယုံကြည်ရသော လူ့ဘာသာပြန်သူများ၏ ဘာသာပြန်ချက်ကို အကြံပြုပါသည်။ ဤဘာသာပြန်ချက်ကို အသုံးပြုစဉ် ဖြစ်ပေါ်လာသော နားလည်မှု မှားယွင်းမှုများအတွက် ကျွန်ုပ်တို့သည် တာဝန် မယူပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->