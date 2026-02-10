# LangChain4j நேற்றைய ஆரம்பக்கட்டு உருவமைப்பு Azure மைய அமைப்பு

## உள்ளடக்க பட்டியல்

- [முன் நிபந்தனைகள்](../../../../01-introduction/infra)
- [கட்டமைப்பு](../../../../01-introduction/infra)
- [உருவாக்கப்பட்ட வளங்கள்](../../../../01-introduction/infra)
- [விரைவான தொடக்கம்](../../../../01-introduction/infra)
- [கட்டமைப்பு](../../../../01-introduction/infra)
- [மேற்காட்டு கட்டளைகள்](../../../../01-introduction/infra)
- [செலவு பராமரிப்பு](../../../../01-introduction/infra)
- [கண்காணிப்பு](../../../../01-introduction/infra)
- [பணி முடிவுகள்](../../../../01-introduction/infra)
- [புதியமைப்பு புதுப்பிப்பு](../../../../01-introduction/infra)
- [சுத்தம் செய்யும் செயல்முறை](../../../../01-introduction/infra)
- [கோப்பு அமைப்பு](../../../../01-introduction/infra)
- [பாதுகாப்பு பரிந்துரைகள்](../../../../01-introduction/infra)
- [மேலும் வளங்கள்](../../../../01-introduction/infra)

இந்த அடைவு Azure OpenAI வளங்களை dağıப்பு செய்ய Bicep மற்றும் Azure Developer CLI (azd) பயன்படுத்த Azure கட்டமைப்பை குறியீட்டாக (IaC) கொண்டுள்ளது.

## முன் நிபந்தனைகள்

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (பதிப்பு 2.50.0 அல்லது பிறகு)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (பதிப்பு 1.5.0 அல்லது பிறகு)
- வளத்தை உருவாக்க அனுமதியுடன் Azure சந்தா

## கட்டமைப்பு

**எளிய உள்ளூர் மேம்பாட்டு அமைப்பு** - Azure OpenAI மட்டும் ஏற்பு செய்து, அனைத்து செயலிகளை உள்ளூர் இயக்கு.

இந்த கட்டமைப்பு பின்வரும் Azure வளங்களை உருவாக்குகிறது:

### AI சேவைகள்
- **Azure OpenAI**: இரண்டு மாதிரி விளக்கு Cognitive Services:
  - **gpt-5.2**: உரையாடல் முடிவடைவு மாதிரி (20K TPM திறன்)
  - **text-embedding-3-small**: RAGக்கான சேர் மாதிரி (20K TPM திறன்)

### உள்ளூர் மேம்பாடு
எல்லா Spring Boot செயலிகளும் உங்கள் இயந்திரத்தில் உள்ளூர் இயங்கும்:
- 01-introduction (போர்ட் 8080)
- 02-prompt-engineering (போர்ட் 8083)
- 03-rag (போர்ட் 8081)
- 04-tools (போர்ட் 8084)

## உருவாக்கப்பட்ட வளங்கள்

| வள வகை | வள பெயர் மாதிரி | நோக்கம் |
|--------------|----------------------|---------|
| வள குழு | `rg-{environmentName}` | அனைத்து வளங்களையும் உள்ளடக்கும் |
| Azure OpenAI | `aoai-{resourceToken}` | AI மாதிரிகளைப் பொறுமை செய்கிறது |

> **குறிப்பு:** `{resourceToken}` சேர்க்கும் ID, சூழல் பெயர் மற்றும் இடம் கொண்டு உருவான தனித்த சொற்றொடர்

## விரைவான தொடக்கம்

### 1. Azure OpenAI உருவாக்கவும்

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

கேட்கப்படும் போது:
- உங்கள் Azure சந்தாவை தேர்வு செய்யவும்
- இடம் தேர்வு செய்யவும் (பரிந்துரை: GPT-5.2 கிடைக்கும் `eastus2`)
- சூழல் பெயரை உறுதிப்படுத்தவும் (இயல்பானது: `langchain4j-dev`)

இது உருவாக்கும்:
- GPT-5.2 மற்றும் text-embedding-3-small உடன் Azure OpenAI வளம்
- இணைப்பு விவரங்களை வெளியிடும்

### 2. இணைப்பு விவரங்களை பெறவும்

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

இதன் மூலம் காண்பிக்கப்படும் விவரங்கள்:
- `AZURE_OPENAI_ENDPOINT`: உங்கள் Azure OpenAI இறுதிக் குறியிடு URL
- `AZURE_OPENAI_KEY`: அடையாளப்பதிவுக்கான API விசை
- `AZURE_OPENAI_DEPLOYMENT`: உரையாடல் மாதிரி பெயர் (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: சேர் மாதிரி பெயர்

### 3. செயலிகளை உள்ளூரில் இயக்கவும்

`azd up` கட்டளை அட்டவணை அடைவில் `.env` கோப்பை தானாக உருவாக்கி தேவையான சூழல் மாறிகளை சேர்க்கும்.

**பரிந்துரை:** அனைத்து வலை செயலிகளையும் துவங்கு:

**Bash:**
```bash
# ரூட் கோப்புறையிலிருந்து
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# ஆதார் கோப்புறை இருந்து
cd ../..
.\start-all.ps1
```

அல்லது தனிப்பட்ட தொகுதியை துவங்கு:

**Bash:**
```bash
# உதாரணம்: அறிமுக தொகுதியைப் பதிவிறக்கம் செய்ய தொடங்கு
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# எடுத்துக்காட்டு: அறிமுகமான இயக்கியும் ஆரம்பிக்கவும்
cd ../01-introduction
.\start.ps1
```

இரு ஸ்கிரிப்டுகள் `.env` கோப்பிலிருந்து சூழல் மாறிகளை தானாக ஏற்றும்.

## கட்டமைப்பு

### மாதிரி விளக்கங்களை மாற்றுதல்

மாதிரி விளக்கங்களை மாற்ற `infra/main.bicep` கோப்பை திருத்தி `openAiDeployments` அளவுருவை மாற்றவும்:

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

கிடைக்கும் மாதிரிகள் மற்றும் பதிப்புகள்: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Azure பகுதியை மாற்றுதல்

வேறு பகுதியில் உருவாக்க `infra/main.bicep` கோப்பை திருத்தவும்:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

GPT-5.2 கிடைக்கும் பகுதிகளைக் காண: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Bicep கோப்புகளை மாற்றிய பிறகு கட்டமைப்பை புதுப்பிக்க:

**Bash:**
```bash
# ARM வார்ப்புருவை மீண்டும் கட்டமைக்கவும்
az bicep build --file infra/main.bicep

# மாற்றங்களை முன்வெளியிடுக
azd provision --preview

# மாற்றங்களை முன்னெடுக்கவும்
azd provision
```

**PowerShell:**
```powershell
# ARM வார்ப்புருவை மறுசீரமைக்கவும்
az bicep build --file infra/main.bicep

# மாற்றங்களை முன்னோட்டமாகக் காண்க
azd provision --preview

# மாற்றங்களை பொருத்தவும்
azd provision
```

## சுத்தம் செய்யும் செயல்முறை

எல்லா வளங்களும் அழிக்க:

**Bash:**
```bash
# அனைத்து வளங்களையும் நீக்கு
azd down

# சூழலை உள்ளடக்கிய அனைத்தையும் நீக்கு
azd down --purge
```

**PowerShell:**
```powershell
# அனைத்து வளங்களையும் அழிக்கவும்
azd down

# சூழலை உட்பட அனைத்தையும் அழிக்கவும்
azd down --purge
```

**எச்சரிக்கை**: இது அனைத்து Azure வளங்களையும் நிரந்தரமாக அழிக்கும்.

## கோப்பு அமைப்பு

## செலவு பராமரிப்பு

### மேம்பாடு/சோதனை
மேம்பாடு/சோதனை சுற்றுச்சூழலுக்குப் செலவை குறைக்க:
- Azure OpenAIக்கு Standard tier (S0) பயன்படுத்தவும்
- `infra/core/ai/cognitiveservices.bicep` இல்லத்தில் திறனை (20K என்ற பதிலாக 10K TPM) குறைக்கவும்
- பயன்பாடல்லாத போது வளங்களை அழிக்க: `azd down`

### தயாரிப்பு
உற்பத்திக்கு:
- பயன்பாட்டின் படி திறனை உயர்த்தவும் (50K+ TPM)
- அதிக கிடைக்கும் கண்காணிப்புக்கு zone redundancy செயல்படுத்தவும்
- சரியான கண்காணிப்பு மற்றும் செலவு அறிவிப்புகளை செயல்படுத்தவும்

### செலவு மதிப்பீடு
- Azure OpenAI: குறிச்சொல் அடிப்படையிலான கட்டணம் (உள்ளீடு + வெளியீடு)
- GPT-5.2: சுமார் $3-5 ஒரு மில்லியன் குறிச்சொல் (தற்போதைய விலை பார்க்கவும்)
- text-embedding-3-small: சுமார் $0.02 ஒரு மில்லியன் குறிச்சொல்

கட்டண கணக்கீடு: https://azure.microsoft.com/pricing/calculator/

## கண்காணிப்பு

### Azure OpenAI அளவுகோல்களை காண

Azure போர்டலில் → உங்கள் OpenAI வளம் → அளவுகோல்கள்:
- குறிச்சொல் அடிப்படுத்திய பயன்பாடு
- HTTP கோரிக்கை வீதம்
- பதில் நேரம்
- செயல்பாட்டிலுள்ள குறிச்சொற்கள்

## பணி முடிவுகள்

### பிரச்சனை: Azure OpenAI உட்பகுதி பெயர் மோதல்

**தவறு செய்தி:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**காரணம்:**
உங்கள் சந்தா/சூழல் முதலில் பயன்படுத்திய உபப்பிராந்திய பெயர் இன்னும் இருக்கும்.

**தீர்வு:**
1. **விருப்பம் 1 - வேறு சூழல் பெயர் பயன்படுத்தவும்:**
   
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

2. **விருப்பம் 2 - Azure போர்டல் மூலம் கையால் உருவாக்குதல்:**
   - Azure போர்டல் → வள உருவாக்க → Azure OpenAI
   - உங்கள் வளம் க்கு தனித்த பெயர் தேர்வு செய்யவும்
   - பின்வரும் மாதிரிகளை நிறுவவும்:
     - **GPT-5.2**
     - **text-embedding-3-small** (RAG தொகுதிகள் க்காக)
   - **முக்கியம்:** உங்கள் `.env` கட்டமைப்புக்கு பொருந்தும் பெயர்களை கவனிக்கவும்
   - நிறுவிய பின் "Keys and Endpoint" இலிருந்து இறுதிக் குறியீடு மற்றும் API விசையை பெறவும்
   - திட்ட அடைவில் `.env` கோப்பை உருவாக்கவும்:
     
     **உதாரண `.env` கோப்பு:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**மாதிரி விளக்கு பெயரிடல் வழிகாட்டிகள்:**
- எளிமையான, ஒரே மாதிரியைப் பயன்படுத்தவும்: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- விளக்கு பெயர்கள் `.env` உடையவை பொருந்த வேண்டும்
- தவறான செயல்: ஒரே மாதிரியை உருவாக்கி வேறொரு பெயரை குறியிடல்

### பிரச்சனை: தேர்ந்தெடுக்கப்பட்ட இடத்தில் GPT-5.2 கிடைக்கவில்லை

**தீர்வு:**
- GPT-5.2 சேவை உள்ள இடத்தை தேர்வு செய்யவும் (எ.கா., eastus2)
- கிடைப்பைக் காணவும்: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### பிரச்சனை: விளக்குக்கு போதுமான இடம் இல்லை

**தீர்வு:**
1. Azure போர்டல் உடன் இடத்தை அதிகரிக்க கோரிக்கை செய்யவும்
2. அல்லது `main.bicep` இல் திறனை குறைக்கவும் (எ.கா., திறன்: 10)

### பிரச்சனை: “Resource not found” உள்ளூரில் இயக்கும்போது

**தீர்வு:**
1. நிறுவுவதை சரிபார்க்க: `azd env get-values`
2. இறுதிக் குறியீடு மற்றும் விசை சரி என உறுதி செய்துகொள்ளவும்
3. Azure போர்டலில் வள குழு உள்ளதா என்று காணவும்

### பிரச்சனை: அங்கீகாரம் தோல்வி

**தீர்வு:**
- `AZURE_OPENAI_API_KEY` சரியாக அமைக்கப்பட்டுள்ளது என பரிசோதிக்கவும்
- விசை 32-அங்க எழுத்தாட்சி கொண்ட ஹெக்ஸக்சிமல் சரி
- Azure போர்டலில் இருந்து புதிய விசை பெறவும்

### நிறுவல் தோல்வி

**பிரச்சனை**: `azd provision` இடத்தை அல்லது திறன் தவறுகளால் தோல்விக்கு உள்ளாகிறது

**தீர்வு**: 
1. வேறு இடம் முயற்சிக்கவும் - [பிராந்திய மாற்றம்](../../../../01-introduction/infra) பகுதியை பார்க்கவும்
2. உங்கள் சந்தாவிற்கு Azure OpenAI இடம் உள்ளது என்பதை சரிபார்க்கவும்:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### செயலி இணைப்பு இல்லை

**பிரச்சனை**: ஜாவா செயலியில் இணைப்பு தவறுகள்

**தீர்வு**:
1. சூழல் மாறிகள் ஏற்றப்பட்டுள்ளனவா என்று சரிபார்க்கவும்:
   
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

2. இறுதிக் குறியீடு வடிவம் சரியா (`https://xxx.openai.azure.com`) என்றும் நினைவில் கொள்ளவும்
3. API விசை Azure போர்டலின் முதன்மை அல்லது இரண்டாவது விசை என உறுதிப்படுத்தவும்

**பிரச்சனை**: Azure OpenAI இலிருந்து 401 கட்டுப்படுத்தப்பட்டது

**தீர்வு**:
1. Azure போர்டல் → Keys and Endpoint இருந்து புதிய API விசை பெறவும்
2. `AZURE_OPENAI_API_KEY` சூழல் மாறியை மீண்டும் ஏற்றவும்
3. மாதிரி விளக்கங்கள் நிறுவப்பட்டுள்ளனவா என்று Azure போர்டலில் சரிபார்க்கவும்

### செயல்திறன் பிரச்சனைகள்

**பிரச்சனை**: பதில் நேரம் மெல்ல

**தீர்வு**:
1. Azure போர்டல் அளவுகோல்-களில் OpenAI குறிச்சொல் பயன்பாடு மற்றும் தடைகளைப் பார்க்கவும்
2. TPM திறனை அதிகரிக்கவும் (கட்டுப்பாடுகள் அடைந்தால்)
3. Reasoning-effort உடன்பாடு அதிகரிக்க(low/medium/high பயன்படுத்தவும்)

## புதியமைப்பு புதுப்பிப்பு

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

## பாதுகாப்பு பரிந்துரைகள்

1. **API விசைகளை எப்போதும் commit செய்யாதீர்கள்** - சூழல் மாறிகளைப் பயன்படுத்தவும்
2. **உள்ளூரில் `.env` கோப்புகள் பயன்படுத்தவும்** - `.gitignore`-இல் சேர்க்கவும்
3. **விசைகளை இடைக்காலமாக மாற்றவும்** - Azure போர்டலில் புதிய விசைகள் உருவாக்கவும்
4. **அணுகலை கட்டுப்படுத்தவும்** - Azure RBAC பயன்படுத்தி உதவி இயக்கலாம்
5. **பயன்பாட்டைக் கண்காணிக்கவும்** - Azure போர்டலில் செலவு அறிவிப்புகளை அமைக்கவும்

## மேலும் வளங்கள்

- [Azure OpenAI சேவை ஆவணி](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 மாதிரி ஆவணி](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI ஆவணங்கள்](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep ஆவணங்கள்](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI அதிகாரப்பூர்வ இணைப்பு](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## ஆதரவு

சிக்கல்களுக்கு:
1. மேல் குறிப்பிடப்பட்ட [பணி முடிவுகள்](../../../../01-introduction/infra) பகுதியைக் காணவும்
2. Azure போர்டலில் Azure OpenAI சேவை ஆரோக்கியத்தை ஆய்வு செய்யவும்
3. GitHub சமாளிப்பு பகுதியில் பிரச்சனை பதிவேற்றவும்

## உரிமம்

சரிகாண root [LICENSE](../../../../LICENSE) கோப்பில் காணவும்.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**மறுப்பு அறிவிப்பு**:  
இந்த 문서는 AI மொழிபெயர்ப்பு சேவையான [Co-op Translator](https://github.com/Azure/co-op-translator) மூலம் மொழிபெயர்க்கப்பட்டது. நாங்கள் துல்லியத்திற்காக முயன்றாலும், தானாக செய்யப்பட்ட மொழிபெயர்ப்புகளில் பிழைகள் அல்லது தவறுகள் இருக்கக்கூடும் என்பதை தயவுசெய்து கவனியுங்கள். மூல ஆவணம் அதனுடைய இயல்பான மொழியில் அதிகாரப்பூர்வ ஆதாரமாக கருதப்பட வேண்டும். முக்கியமான தகவல்களுக்கு, தொழில்முறை மனித மொழிபெயர்ப்பு பரிந்துரைக்கப்படுகிறது. இந்த மொழிபெயர்ப்பின் பயன்படுத்தலில் ஏற்படும் எதற்குமான தவறான புரிதல்கள் அல்லது தவறான விளக்கங்களுக்கு நாங்கள் பொறுப்பேற்போமில்லை.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->