# LangChain4j Getting Started ਲਈ Azure ഇൻഫ്രാസ്ട്രക്ചർ

## ഉള്ളടക്ക പട്ടിക

- [ആവശ്യമായ പലതും](../../../../01-introduction/infra)
- [വാസ്തുവിദ്യ](../../../../01-introduction/infra)
- [സൃഷ്ടിച്ച സ്രോതസ്സുകൾ](../../../../01-introduction/infra)
- [ത്വരിതാരംഭം](../../../../01-introduction/infra)
- [കോണഫിഗറേഷൻ](../../../../01-introduction/infra)
- [മാനേജ്മെന്റ് കമാൻഡുകൾ](../../../../01-introduction/infra)
- [ചെലവ് അനുഭവം](../../../../01-introduction/infra)
- [മോണിറ്ററിംഗ്](../../../../01-introduction/infra)
- [പ്രശ്നപരിഹാരം](../../../../01-introduction/infra)
- [ഇൻഫ്രാസ്ട്രക്ചർ അപ്ഡേറ്റ്](../../../../01-introduction/infra)
- [ശുചീകരണം](../../../../01-introduction/infra)
- [ഫയൽ ഘടന](../../../../01-introduction/infra)
- [സുരക്ഷാ നിർദ്ദേശങ്ങൾ](../../../../01-introduction/infra)
- [കൂടുതൽ സ്രോതസ്സുകൾ](../../../../01-introduction/infra)

Bicep மற்றும் Azure Developer CLI (azd) ഉപയോഗിച്ച് Azure OpenAI സ്രോതസ്സുകൾ വിന്യസിക്കാൻ ഈ ഡയറക്ടറി Azure ഇൻഫ്രാസ്ട്രക്ചർ ഐകോഡ് (IaC) ആയി ഉണ്ട്.

## ആവശ്യമായ പലതും

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (അവതരണം 2.50.0 അല്ലെങ്കിൽ പുതിയത്)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (അവതരണം 1.5.0 അല്ലെങ്കിൽ പുതിയത്)
- റിസോഴ്സ് സൃഷ്ടിക്കാനുള്ള അനുമതികളോടെ ഒരു Azure സബ്സ്ക്രിപ്ഷൻ

## വാസ്തുവിദ്യ

**സരളമായ ലോക്കൽ ഡെവലപ്പ്മെന്റ് സെറ്റ്‌അപ്പ്** - Azure OpenAI മാത്രമേ വിന്യസിക്കൂ, എല്ലാം അപ്ലിക്കേഷനുകൾ ലോക്കലായി ഓടിക്കുക.

ഇൻഫ്രാസ്ട്രക്ചർ താഴെ പറയുന്ന Azure സ്രോതസ്സുകൾ വിന്യസിക്കുന്നു:

### AI സേവനങ്ങൾ
- **Azure OpenAI**: രണ്ട് മോഡൽ വിന്യസകരണങ്ങളുള്ള കോഗ്നിറ്റീവ് സർവീസ്:
  - **gpt-5.2**: ചാറ്റ് പൂരിപ്പിക്കൽ മോഡൽ (20K TPM ശേഷി)
  - **text-embedding-3-small**: RAG-ക്കായി എമ്പെഡ്ഡിംഗ് മോഡൽ (20K TPM ശേഷി)

### ലോക്കൽ ഡെവലപ്പ്മെന്റ്
എല്ലാ സ്ട്രിംഗ് ബൂട്ട് അപ്ലിക്കേഷനുകളും നിങ്ങളുടെ യന്ത്രത്തിൽ ലോക്കലായി ഓടുന്നു:
- 01-introduction (പോർട്ട് 8080)
- 02-prompt-engineering (പോർട്ട് 8083)
- 03-rag (പോർട്ട് 8081)
- 04-tools (പോർട്ട് 8084)

## സൃഷ്ടിച്ച സ്രോതസ്സുകൾ

| സ്രോതസ് തരം | സ്രോതസ് നാമ പാറ്റേൺ | ഉദ്ദേശ്യം |
|--------------|----------------------|---------|
| റിസോഴ്‌സ് ഗ്രൂപ്പ് | `rg-{environmentName}` | എല്ലാ സ്രോതസ്സുകളും ഉൾക്കൊള്ളുന്നു |
| Azure OpenAI | `aoai-{resourceToken}` | AI മോഡൽ ഹോസ്റ്റിംഗ് |

> **കുറിപ്പ്:** `{resourceToken}` സബ്സ്ക്രിപ്ഷൻ ഐഡി, എന്വയോൺമെന്റ് നെയിം, ലൊക്കേഷനിൽ നിന്നുള്ള യുണീക്ക് സ്ട്രിംഗ് ആണ്

## ത്വരിതാരംഭം

### 1. Azure OpenAI വിന്യസിക്കുക

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

പ്രീറ്റുമായപ്പോൾ:
- നിങ്ങളുടെ Azure സബ്സ്ക്രിപ്ഷൻ തിരഞ്ഞെടുക്കുക
- ഒരു ലൊക്കേഷൻ തിരഞ്ഞെടുക്കുക (ശുപാർശ: GPT-5.2 ലഭ്യതയ്ക്ക് `eastus2`)
- എന്വയോൺമെന്റ് നെയിം സ്ഥിരീകരിക്കുക (ഡീഫോൾട്ട്: `langchain4j-dev`)

ഇത് സൃഷ്ടിക്കും:
- GPT-5.2നും text-embedding-3-smallനുമുള്ള Azure OpenAI റിസോഴ്സ്
- ബന്ധം വിവരങ്ങൾ ഔട്ട്പുട്ടിൽ

### 2. ബന്ധവിവരങ്ങൾ നേടുക

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

ഇത് കാണിക്കും:
- `AZURE_OPENAI_ENDPOINT`: നിങ്ങളുടെ Azure OpenAI എൻഡ്‌పോയിന്റ് URL
- `AZURE_OPENAI_KEY`: ഓത്തന്റിക്കേഷൻ API കീ
- `AZURE_OPENAI_DEPLOYMENT`: ചാറ്റ് മോഡൽ നാമം (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: എമ്പെഡ്ഡിംഗ് മോഡൽ നാമം

### 3. അപ്ലിക്കേഷനുകൾ ലോക്കലായി ഓടിക്കുക

`azd up` കമാൻഡ് സ്വയം-root ഡയറക്ടറിയിൽ ആവശ്യമായ എല്ലാ എൻവയോൺമെന്റ് വേരിയബിളുകളോടുകൂടിയ `.env` ഫയൽ സൃഷ്ടിക്കുന്നു.

**ശുപാർശ ചെയ്യുന്നു:** എല്ലാ വെബ് അപ്ലിക്കേഷനുകളും ആരംഭിക്കുക:

**Bash:**
```bash
# റൂട്ടു ഡയറക്ടറിയിൽ നിന്ന്
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# റൂട്ട് ഡയറക്ടറിയിൽ നിന്ന്
cd ../..
.\start-all.ps1
```

അല്ലെങ്കിൽ ഒരു ഒറ്റ മോഡ്യൂൾ മാത്രമേ ആരംഭിക്കൂ:

**Bash:**
```bash
# ഉദാഹരണം:介绍模块 മാത്രം തുടങ്ങുക
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# ഉദാഹരണം: പരിചയ_module_ മാത്രമാണ് ആരംഭിക്കുന്നത്
cd ../01-introduction
.\start.ps1
```

രണ്ടു സ്‌ക്രിപ്റ്റുകളും സ്വയം-root `.env` ഫയലിൽ നിന്ന് എൻവയോൺമെന്റ് വേരിയബിളുകൾ ലോഡ് ചെയ്യും (azd up സൃഷ്ടിച്ചത്).

## കോൺഫിഗറേഷൻ

### മോഡൽ വിന്യസങ്ങളിലെക്കുള്ള ഇച്ഛാനുസൃതമാക്കൽ

മോഡൽ വിന്യസനങ്ങൾ മാറ്റാൻ, `infra/main.bicep` എഡിറ്റ് ചെയ്ത് `openAiDeployments` പാരാമീറ്റർ മാറ്റുക:

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

ലഭ്യമായ മോഡലുകളും പതിപ്പുകളും: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Azure ഭൂപ്രദേശങ്ങൾ മാറ്റുന്നു

മറ്റൊരു ഭൂപ്രദേശത്ത് വിന്യസിക്കാൻ, `infra/main.bicep` എഡിറ്റ് ചെയ്യുക:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

GPT-5.2 ലഭ്യത പരിശോധിക്കുക: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Bicep ഫയലുകളിൽ മാറ്റങ്ങൾ ചെയ്തതിന് ശേഷം ഇൻഫ്രാസ്ട്രക്ചർ അപ്ഡേറ്റ് ചെയ്യാൻ:

**Bash:**
```bash
# ARM ടെംപ്ലേറ്റ് പുനർനിർമിക്കുക
az bicep build --file infra/main.bicep

# മാറ്റങ്ങൾ മുൻവൈകൽ പരിശോധിക്കുക
azd provision --preview

# മാറ്റങ്ങൾ പ്രയോഗിക്കുക
azd provision
```

**PowerShell:**
```powershell
# ARM ടെംപ്ലേറ്റ് പുനർനിർമ്മിക്കുക
az bicep build --file infra/main.bicep

# മാറ്റങ്ങൾ മുമ്പ് കാണുക
azd provision --preview

# മാറ്റങ്ങൾ പ്രയോഗിക്കുക
azd provision
```

## ശുചീകരണം

എല്ലാ സ്രോതസ്സുകളും ഇല്ലാതാക്കാൻ:

**Bash:**
```bash
# എല്ലാ വഴി വസ്തുക്കളും ഇല്ലാതാക്കുക
azd down

# പരിതസ്ഥിതി അടക്കമുള്ള എല്ലാം ഇല്ലാതാക്കുക
azd down --purge
```

**PowerShell:**
```powershell
# എല്ലാ വിഭവങ്ങളും മായ്ക്കുക
azd down

# പാരിസ്ഥിതികം ഉൾപ്പെടെ എല്ലാം മായ്ക്കുക
azd down --purge
```

**മുന്നറിയിപ്പ്**: ഇത് സ്ഥിരമായി എല്ലാ Azure സ്രോതസ്സുകളും ഇല്ലാതാക്കുമെന്നതാണ്.

## ഫയൽ ഘടന

## ചെലവ് അനുഭവം

### ഡെവലപ്‌മെന്റ്/ടെസ്റ്റിങ്
ഡെവ്/ടെസ്റ്റ്’environnementുകളിൽ ചെലവ് കുറയ്ക്കാൻ കഴിയും:
- Azure OpenAI ന് സ്റ്റാൻഡേർഡ് ടിയർ (S0) ഉപയോഗിക്കുക
- `infra/core/ai/cognitiveservices.bicep`ൽ 20K-ഇന്റെ പകരം 10K TPM ശേഷി ക്രമീകരിക്കുക
- ഉപയോഗിക്കാത്തപ്പോൾ സ്രോതസ്സുകൾ ഇല്ലാതാക്കുക: `azd down`

### പ്രൊഡക്ഷൻ
പ്രൊഡക്ഷനിൽ:
- ഉപയോഗത്തിനനുസരിച്ച് OpenAI ശേഷി കൂട്ടുക (50K+ TPM)
- ഉയർന്ന ലഭ്യതയ്ക്കായി സോൻ റിഡണ്ടൻസി അനുവദിക്കുക
- ശരിയായ മോണിറ്ററിംഗ്, ചെലവ് അലേർട്ടുകൾ നടപ്പിലാക്കുക

### ചെലവ് കണക്കുകൂട്ടൽ
- Azure OpenAI: ടോക്കൻ (ഇൻപുട്ട് + ഔട്ട്പുട്ട്) അടിസ്ഥാനത്തിൽ പണമടയ്ക്കുക
- GPT-5.2: ഏകദേശം $3-5 1M ടോക്കന്ക് (നിലവിലുള്ള വില പരിശോധിക്കുക)
- text-embedding-3-small: ഏകദേശം $0.02 1M ടോക്കന്ക്

വർഗ്ഗീകരണ കാൽക്കുലേറ്റർ: https://azure.microsoft.com/pricing/calculator/

## മോണിറ്ററിംഗ്

### Azure OpenAI Metrics കാണുക

Azure പോർട്ടലിലേക്ക് പോവുക → നിങ്ങളുടെ OpenAI സ്രോതസ് → Metrics:
- ടോക്കൻ അടിസ്ഥാനത്തിലുള്ള ഉപയോഗം
- HTTP അഭ്യർത്ഥന നിരക്ക്
- പ്രതികരണ സമയം
- സജീവ ടോക്കനുകൾ

## പ്രശ്നപരിഹാരം

### പ്രശ്നം: Azure OpenAI സബ്‌ഡൊമെയ്ൻ നാമ എംഭേദം

**പിശക് സന്ദേശം:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**കാരണം:**
നിങ്ങളുടെ സബ്സ്ക്രിപ്ഷൻ/എന്വയോൺമെന്റ് നിന്ന് സൃഷ്ടിച്ച സബ്‌ഡൊമെയ്ൻ നാമം മറ്റൊരു പ്രായം സജീവമായ നിന്ന് ഉപയോഗത്തിൽ ആണെങ്കിൽ, സാധ്യതയുണ്ട് മുമ്പത്തെ വിന്യാസം പൂർണമായും ഇല്ലാതാക്കിയിട്ടില്ല.

**പരിഹാരം:**
1. **ഓപ്ഷൻ 1 - വ്യത്യസ്ത എന്വയോൺമെന്റ് നാമം ഉപയോഗിക്കുക:**
   
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

2. **ഓപ്ഷൻ 2 - Azure പോർട്ടൽ വഴി മാനുവൽ വിന്യാസം:**
   - Azure പോർട്ടലിൽ പോകുക → Create a resource → Azure OpenAI തിരഞ്ഞെടുക്കുക
   - നിങ്ങളുടെ റിസോഴ്സിന് ഒരു യുണീക് നാമം തിരഞ്ഞെടുക്കുക
   - താഴെ കാണുന്ന മോഡലുകൾ വിന്യസിക്കുക:
     - **GPT-5.2**
     - **text-embedding-3-small** (RAG മോഡ്യൂളുകൾക്കായി)
   - **മഹत्त्वം:** നിങ്ങളുടെ വിന്യാസ നാമങ്ങൾ `.env` കോൺഫിഗറേഷനുമായി ഒത്തിരിക്കുക
   - വിന്യതിനുശേഷം, "Keys and Endpoint" ൽ നിന്നുള്ള എൻഡ്‌പോയിന്റും API കീയും നേടുക
   - പ്രോജക്ട് റూటിൽ `.env` ഫയൽ സൃഷ്ടിക്കുക:
     
     **ഉദാഹരണ `.env` ഫയൽ:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**മോഡൽ വിന്യാസ നാമ നിർദ്ദേശങ്ങൾ:**
- ലളിതവും സ്ഥിരവും ഉള്ള നാമങ്ങൾ ഉപയോഗിക്കുക: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- വിന്യാസ നാമങ്ങൾ `.env`യിൽ ക്രമീകരിച്ചതുമായി യാഥാർത്ഥ്യം പാലിക്കണം
- സാധാരണ പിശക്: ഒരു മോഡൽ ഒരു നാമത്തിൽ സൃഷ്ടിച്ച് കോഡിൽ മറ്റൊരു നാമം പരാമർശിക്കുന്നത്

### പ്രശ്നം: തിരഞ്ഞെടുക്കപ്പെട്ട ഭൂപ്രദേശത്ത് GPT-5.2 ലഭ്യമല്ല

**പരിഹാരം:**
- GPT-5.2 ലഭ്യമുള്ള ഭൂപ്രദേശം തിരഞ്ഞെടുക്കുക (ഉദാ: eastus2)
- ലഭ്യത പരിശോധിക്കുക: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### പ്രശ്നം: വിന്യാസത്തിന് മതിയാകാത്ത ക്വോട്ട

**പരിഹാരം:**
1. Azure പോർട്ടലിൽ ക്വോട്ട വർധന അഭ്യർത്ഥിക്കുക
2. അല്ലെങ്കിൽ `main.bicep`യിൽ കുറഞ്ഞ ശേഷി ഉപയോഗിക്കുക (ഉദാ: ശേഷി: 10)

### പ്രശ്നം: ലോക്കലായി ഓടുമ്പോൾ "Resource not found"

**പരിഹാരം:**
1. വിന്യാസം പരിശോധിക്കുക: `azd env get-values`
2. എൻഡ്‌പോയിന്റും കീയും ശരിയാണെന്ന് ഉറപ്പാക്കുക
3. Azure പോർട്ടലിൽ റിസോഴ്‌സ് ഗ്രൂപ്പ് ഉണ്ട് എന്ന് ഉറപ്പാക്കുക

### പ്രശ്നം: ഓത്തൻ ൽ പരാജയം

**പരിഹാരം:**
- `AZURE_OPENAI_API_KEY` ശരിയായ വിധത്തിൽ സജ്ജമാകിയിട്ടുണ്ടോ എന്ന് പരിശോധിക്കുക
- കീ ഫോർമാറ്റ് 32-അക്ഷര ഹെക്സാഡെസിമൽ സ്ട്രിംഗ് ആയിരിക്കണം
- ആവശ്യമെങ്കിൽ Azure പോർട്ടലിൽ നിന്ന് പുതിയ കീ നേടുക

### വിന്യാസം പരാജയപ്പെടുന്നു

**പ്രശ്നം**: `azd provision` ക്വോട്ട/ ശേഷി പിശക് കൊണ്ട് പരാജയപ്പെടുന്നു

**പരിഹാരം**: 
1. വ്യത്യസ്ത ഭൂപ്രദേശം പരീക്ഷിക്കുക - [Changing Azure Regions](../../../../01-introduction/infra) വിഭാഗം കാണുക
2. നിങ്ങളുടെ സബ്സ്ക്രിപ്ഷന്റേക്കുറിച്ച് Azure OpenAI ക്വോട്ട ഉണ്ടാകണം:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### അപ്ലിക്കേഷൻ കണക്റ്റ് ചെയ്യുകയില്ല

**പ്രശ്നം**: Java അപ്ലിക്കേഷനിൽ കണക്ഷൻ പിശക് കാണിക്കുന്നു

**പരിഹാരം**:
1. എൻവയോൺമെന്റ് വേരിയബിളുകൾ എക്സ്പോർട്ട് ചെയ്തിട്ടുണ്ടോ എന്ന് പരിശോധിക്കുക:
   
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

2. എൻഡ്‌പോയിന്റ് ഫോർമാറ്റ് ശരിയായതാണ് എന്ന് ഉറപ്പാക്കുക (`https://xxx.openai.azure.com` എന്നിട്ട്)
3. API കീ Azure പോർട്ടലിലെ പ്രൈമറി അല്ലെങ്കിൽ സെക്കണ്ടറി ആണ് എന്ന് സ്ഥിരീകരിക്കുക

**പ്രശ്നം**: Azure OpenAI നിന്ന് 401 അനധികൃത പിശക്

**പരിഹാരം**:
1. Azure പോർട്ടൽ → Keys and Endpoint-ൽ നിന്ന് പുതിയ API കീ നേടുക
2. `AZURE_OPENAI_API_KEY` എൻവയോൺമെന്റ് വേരിയബിൾ വീണ്ടും എക്സ്പോർട്ട് ചെയ്യുക
3. മോഡൽ വിന്യാസം പൂർത്തിയായിട്ടുണ്ടെന്ന് ഉറപ്പാക്കുക (Azure പോർട്ടൽ പരിശോധിക്കുക)

### പെർഫോമൻസ് പ്രശ്നങ്ങൾ

**പ്രശ്നം**: പ്രതികരണ സമയം മന്ദഗതിയിലാണ്

**പരിഹാരം**:
1. Azure പോർട്ടലിലെ മെട്രിക്‌സ് വഴി OpenAI ടോക്കൻ ഉപയോഗവും ത്രോട്ട്ലിംഗും പരിശോധിക്കുക
2. പരിധികളിലെങ്കിൽ TPM ശേഷി വർദ്ധിപ്പിക്കുക
3. ഉയർന്ന പരിഗണനാ പ്രയത്‌ന നില (അത以下/മധ്യം/ഉയരം) ഉപയോഗICT ചെയ്യുക

## ഇൻഫ്രാസ്ട്രക്ചർ അപ്ഡേറ്റ്

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

## സുരക്ഷാ നിർദ്ദേശങ്ങൾ

1. **എന്തായാലും API കീകൾ കമ്മിറ്റ് ചെയ്യരുത്** — എൻവയോൺമെന്റ് വേരിയബിളുകൾ ഉപയോഗിക്കുക
2. **ലോക്കലായി `.env` ഫയൽ ഉപയോഗിക്കുക** — `.env` `.gitignore` ലേക്ക് ചേർക്കുക
3. **കീകൾ പുനരുദ്ധരിക്കുക** — Azure പോർട്ടലിൽ പുതിയ കീകൾ സൃഷ്ടിക്കുക
4. **പ്രവേശനം പരിമിതപ്പെടുത്തുക** — Azure RBAC ഉപയോഗിച്ച് റിസോഴ്‌സുകളിൽ ആക്‌സസ് നിയന്ത്രിക്കുക
5. **ഉപയോഗം മോണിറ്റർ ചെയ്യുക** — Azure പോർട്ടലിൽ ചെലവ് അലേർട്ടുകൾ ക്രമീകരിക്കുക

## കൂടുതൽ സ്രോതസ്സുകൾ

- [Azure OpenAI സേവന ഡോക്യുമെന്റേഷൻ](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 മോഡൽ ഡോക്യുമെന്റേഷൻ](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI ഡോക്യുമെന്റേഷൻ](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep ഡോക്യുമെന്റേഷൻ](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI ഔദ്യോഗിക ഇന്റഗ്രേഷൻ](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## പിന്തുണ

പ്രശ്നങ്ങൾക്കായി:
1. മുകളിൽ കാണുന്ന [പ്രശ്നപരിഹാര വിഭാഗം](../../../../01-introduction/infra) പരിശോധിക്കുക
2. Azure പോർട്ടലിൽ Azure OpenAI സേവനത്തിന്റെ ആരോഗ്യം നിരീക്ഷിക്കുക
3. റിപോസിറ്ററിയിൽ പ്രശ്നം തുറക്കുക

## ലൈസൻസ്

റൂട്ട് ലെ [LICENSE](../../../../LICENSE) ഫയൽ കാണുക.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**പരിചിതമായ നിഷേധം**:  
ഈ രേഖ [Co-op Translator](https://github.com/Azure/co-op-translator) എന്ന AI الترجمة സേവനത്തിന്റെ സഹായത്തോടെ പരിഭാഷപ്പെടുത്തിയതാണ്. നാം കൃത്യത ഉറപ്പാക്കാൻ പരിശ്രമിച്ചിട്ടും, സ്വയമേവത്തിലുള്ള പരിഭാഷയിൽ പിശകുകൾ അല്ലെങ്കിൽ തെറ്റായ വിവരങ്ങൾ ഉണ്ടാകാമെന്ന് ദയവായി ശ്രദ്ധിക്കണം. യഥാർത്ഥ പത്രം അതിന്റെ മാതൃഭാഷയിൽ തന്നെയാണ് പ്രാമാണികമായ ഉറവിടം എന്ന് കരുതേണ്ടതാണ്. അത്യാവശ്യമായ വിവരങ്ങൾക്ക്, പ്രൊഫഷണൽ മനുഷ്യ പരിഭാഷ ശുപാർശ ചെയ്യപ്പെടുന്നു. ഈ പരിഭാഷ ഉപയോഗിക്കുന്നതിൽ നിന്നുള്ള ഏതെങ്കിലും തെറ്റിദ്ധരിപ്പിക്കലോ അസംബന്ധമായ വ്യാഖ്യാനങ്ങളോ ഉണ്ടെങ്കിൽ അതിന് ഞങ്ങൾ ഉത്തരവാദിത്തം വിവരം വെക്കുന്നില്ല.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->