# LangChain4j सुरु गर्नका लागि Azure पूर्वाधार

## विषयवस्तु

- [पूर्वआवश्यकताहरू](../../../../01-introduction/infra)
- [आर्किटेक्चर](../../../../01-introduction/infra)
- [संसाधनहरू सिर्जना गरियो](../../../../01-introduction/infra)
- [छिटो सुरु](../../../../01-introduction/infra)
- [कन्फिगरेसन](../../../../01-introduction/infra)
- [प्रबंधन आदेशहरू](../../../../01-introduction/infra)
- [लागत अनुकूलन](../../../../01-introduction/infra)
- [मोनिटरिङ](../../../../01-introduction/infra)
- [समस्या समाधान](../../../../01-introduction/infra)
- [पूर्वाधार अद्यावधिक गर्दै](../../../../01-introduction/infra)
- [सफाइ](../../../../01-introduction/infra)
- [फाइल संरचना](../../../../01-introduction/infra)
- [सुरक्षा सिफारिसहरू](../../../../01-introduction/infra)
- [अतिरिक्त स्रोतहरू](../../../../01-introduction/infra)

यो निर्देशिका Azure OpenAI संसाधनहरू तैनाथ गर्न Bicep र Azure Developer CLI (azd) प्रयोग गरी Azure पूर्वाधारलाई कोड (IaC) रूपमा समावेश गर्छ।

## पूर्वआवश्यकताहरू

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (संस्करण 2.50.0 वा नयाँ)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (संस्करण 1.5.0 वा नयाँ)
- संसाधनहरू सिर्जना गर्ने अनुमति सहित Azure सदस्यता

## आर्किटेक्चर

**सरलीकृत स्थानीय विकास सेटअप** - केवल Azure OpenAI तैनाथ गर्नुहोस्, सबै अनुप्रयोगहरू स्थानीय रूपमा चलाउनुहोस्।

पूर्वाधारले निम्न Azure संसाधनहरू तैनाथ गर्छ:

### एआई सेवा
- **Azure OpenAI**: सञ्चित सेवाहरू दुई मोडेल तैनाथहरूसहित:
  - **gpt-5.2**: कुराकानी पूर्ति मोडेल (20K TPM क्षमता)
  - **text-embedding-3-small**: RAG का लागि एम्बेडिङ मोडेल (20K TPM क्षमता)

### स्थानीय विकास
सबै Spring Boot अनुप्रयोगहरू तपाईंको मेशिनमा स्थानीय रूपमा चल्छन्:
- 01-introduction (पोर्ट 8080)
- 02-prompt-engineering (पोर्ट 8083)
- 03-rag (पोर्ट 8081)
- 04-tools (पोर्ट 8084)

## सिर्जना गरिएका संसाधनहरू

| संसाधन प्रकार | संसाधन नाम नमूना | उद्देश्य |
|--------------|----------------------|---------|
| संसाधन समूह | `rg-{environmentName}` | सबै संसाधनहरू समावेश गर्दछ |
| Azure OpenAI | `aoai-{resourceToken}` | एआई मोडेल होस्टिङ |

> **नोट:** `{resourceToken}` सदस्यता ID, वातावरण नाम, र स्थानबाट उत्पन्न एक अद्वितीय स्ट्रिङ हो

## छिटो सुरु

### 1. Azure OpenAI तैनाथ गर्नुहोस्

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

प्रश्न सोध्दा:
- आफ्नो Azure सदस्यता चयन गर्नुहोस्
- स्थान छान्नुहोस् (सिफारिस गरिएको: GPT-5.2 को लागि `eastus2`)
- वातावरण नाम पुष्टि गर्नुहोस् (पूर्वनिर्धारित: `langchain4j-dev`)

यसले सिर्जना गर्नेछ:
- GPT-5.2 र text-embedding-3-small सहित Azure OpenAI संसाधन
- कनेक्शन विवरण आउटपुट

### 2. कनेक्शन विवरण प्राप्त गर्नुहोस्

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

यो देखाउछ:
- `AZURE_OPENAI_ENDPOINT`: तपाईंको Azure OpenAI अन्तबिन्दु URL
- `AZURE_OPENAI_KEY`: प्रमाणीकरणको लागि API कुञ्जी
- `AZURE_OPENAI_DEPLOYMENT`: कुराकानी मोडेल नाम (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: एम्बेडिङ मोडेल नाम

### 3. अनुप्रयोगहरू स्थानीय रूपमा चलाउनुहोस्

`azd up` आदेशले मूल निर्देशिकामा सबै आवश्यक वातावरण चरहरूसँग `.env` फाइल स्वचालित रूपमा सिर्जना गर्छ।

**सिफारिस गरिएको:** सबै वेब अनुप्रयोगहरू सुरु गर्नुहोस्:

**Bash:**
```bash
# मूल निर्देशिकाबाट
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# मूल निर्देशिकाबाट
cd ../..
.\start-all.ps1
```

वा एकल मोड्युल सुरु गर्नुहोस्:

**Bash:**
```bash
# उदाहरण: केवल परिचय मोड्युल सुरु गर्नुहोस्
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# उदाहरण: केवल परिचय मोड्युल सुरु गर्नुहोस्
cd ../01-introduction
.\start.ps1
```

दुबै स्क्रिप्टहरूले `azd up` द्वारा सिर्जना गरिएको मूल `.env` फाइलबाट वातावरण चरहरू स्वचालित रूपमा लोड गर्छन्।

## कन्फिगरेसन

### मोडेल तैनाथीकरण कस्टमाइज गर्ने

मोडेल तैनाथीकरण परिवर्तन गर्न `infra/main.bicep` सम्पादन गर्नुहोस् र `openAiDeployments` प्यारामिटर समायोजन गर्नुहोस्:

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

उपलब्ध मोडेलहरू र संस्करणहरू: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Azure क्षेत्रहरू परिवर्तन गर्ने

अर्को क्षेत्रमा तैनाथ गर्न `infra/main.bicep` सम्पादन गर्नुस्:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

GPT-5.2 उपलब्धता जाँच्नुहोस्: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Bicep फाइलहरूमा परिवर्तन गरेपछि पूर्वाधार अद्यावधिक गर्न:

**Bash:**
```bash
# ARM टेम्प्लेट पुनर्निर्माण गर्नुहोस्
az bicep build --file infra/main.bicep

# परिवर्तनहरू पूर्वावलोकन गर्नुहोस्
azd provision --preview

# परिवर्तनहरू लागू गर्नुहोस्
azd provision
```

**PowerShell:**
```powershell
# ARM टेम्प्लेट पुनर्निर्माण गर्नुहोस्
az bicep build --file infra/main.bicep

# परिवर्तनहरू पूर्वावलोकन गर्नुहोस्
azd provision --preview

# परिवर्तनहरू लागू गर्नुहोस्
azd provision
```

## सफाइ

सबै संसाधनहरू मेटाउन:

**Bash:**
```bash
# सबै स्रोतहरू मेटाउनुहोस्
azd down

# वातावरण सहित सबै कुरा मेटाउनुहोस्
azd down --purge
```

**PowerShell:**
```powershell
# सबै संसाधनहरू मेटाउनुहोस्
azd down

# वातावरण सहित सबै कुरा मेटाउनुहोस्
azd down --purge
```

**चेतावनी**: यसले स्थायी रूपमा सबै Azure संसाधनहरू मेट्नेछ।

## फाइल संरचना

## लागत अनुकूलन

### विकास/परीक्षण
डेभ/टेस्ट वातावरणका लागि लागत घटाउन सकिन्छ:
- Azure OpenAI को लागि Standard स्तर (S0) प्रयोग गर्नुहोस्
- `infra/core/ai/cognitiveservices.bicep` मा क्षमता कम (10K TPM सट्टा 20K)
- प्रयोगमा नहुनु परेमा संसाधनहरू मेट्नुहोस्: `azd down`

### उत्पादन
उत्पादनका लागि:
- प्रयोग अनुसार OpenAI क्षमतामा वृद्धि गर्नुहोस् (50K+ TPM)
- उच्च उपलब्धताका लागि जोन पुनरावृत्ति सक्षम गर्नुहोस्
- उचित मोनिटरिङ र लागत सूचना लागू गर्नुहोस्

### लागत अनुमान
- Azure OpenAI: टोकन आधारमा भुक्तानी (इनपुट + आउटपुट)
- GPT-5.2: ~१M टोकन प्रति $3-5 (हालको मूल्य जाँच्नुहोस्)
- text-embedding-3-small: ~१M टोकन प्रति $0.02

मूल्य गणक: https://azure.microsoft.com/pricing/calculator/

## मोनिटरिङ

### Azure OpenAI मेट्रिक्स हेर्नुहोस्

Azure पोर्टल → तपाईंको OpenAI संसाधन → मेमेट्रिक्स:
- टोकन-आधारित उपयोग
- HTTP अनुरोध दर
- प्रतिक्रिया समय
- सक्रिय टोकनहरू

## समस्या समाधान

### समस्या: Azure OpenAI सबडोमेन नाम द्वन्द्व

**त्रुटि सन्देश:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**कारण:**
तपाईंको सदस्यता/वातावरणबाट सिर्जित सबडोमेन नाम पहिल्यै प्रयोग भइसकेको छ, सम्भवतः पहिल्यैको अधूरो हटाइएको तैनाथीकरणबाट।

**समाधान:**
1. **विकल्प १ - फरक वातावरण नाम प्रयोग गर्नुहोस्:**
   
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

2. **विकल्प २ - Azure पोर्टल मार्फत म्यानुअल तैनाथीकरण:**
   - Azure पोर्टल → स्रोत सिर्जना गर्नुहोस् → Azure OpenAI
   - आफ्नो संसाधनको लागि एक अद्वितीय नाम छान्नुहोस्
   - निम्न मोडेलहरू तैनाथ गर्नुहोस्:
     - **GPT-5.2**
     - **text-embedding-3-small** (RAG मोड्युलहरूको लागि)
   - **महत्वपूर्ण:** तपाईंका तैनाथीकरण नामहरू नोट गर्नुहोस् - ती `.env` कन्फिगरेसनसँग मेल खानुपर्छ
   - तैनाथीकरणपछि, "Keys and Endpoint" बाट अन्तबिन्दु र API कुञ्जी प्राप्त गर्नुहोस्
   - परियोजना मूलमा `.env` फाइल तयार गर्नुहोस्:
     
     **उदाहरण `.env` फाइल:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**मोडेल तैनाथीकरण नामकरणका दिशानिर्देशहरू:**
- सरल, सुसंगत नामहरू प्रयोग गर्नुहोस्: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- तैनाथीकरण नामहरू `.env` मा निश्चित गरिएको नामसँग ठीक मेल खानु पर्दछ
- सामान्य गल्ती: मोडेल एउटै नामले बनाएर कोडमा फरक नामले उल्लेख गर्नु

### समस्या: GPT-5.2 चयन गरिएको क्षेत्रमा उपलब्ध छैन

**समाधान:**
- GPT-5.2 पहुँच भएको क्षेत्र छनौट गर्नुहोस् (जस्तै, eastus2)
- उपलब्धता जाँच्नुहोस्: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### समस्या: तैनाथीकरणको लागि पर्याप्त कोटा छैन

**समाधान:**
1. Azure पोर्टलमा कोटा वृद्धि अनुरोध गर्नुहोस्
2. वा `main.bicep` मा कम क्षमता प्रयोग गर्नुहोस् (जस्तै, क्षमता: 10)

### समस्या: स्थानीय रूपमा चलाउँदा "Resource not found"

**समाधान:**
1. तैनाथीकरण प्रमाणित गर्नुहोस्: `azd env get-values`
2. अन्तबिन्दु र कुञ्जी सही छन् कि छैनन् जाँच गर्नुहोस्
3. Azure पोर्टलमा संसाधन समूह अवस्थित छ कि छैन सुनिश्चित गर्नुहोस्

### समस्या: प्रमाणीकरण असफल

**समाधान:**
- `AZURE_OPENAI_API_KEY` सही सेट गरिएको छ भनी जाँच्नुहोस्
- कुञ्जी फारम्याट 32-अक्षरी हेक्साडेसिमल स्ट्रिङ हुनु पर्छ
- नयाँ कुञ्जी Azure पोर्टलबाट लिनुहोस् यदि आवश्यक छ भने

### तैनाथीकरण असफल

**समस्या**: `azd provision` कोटा वा क्षमता त्रुटिहरू संग असफल

**समाधान**: 
1. फरक क्षेत्र प्रयास गर्नुहोस् - क्षेत्रहरू कन्फिगर गर्ने तरिका हेर्नुहोस् [Changing Azure Regions](../../../../01-introduction/infra) खण्ड
2. तपाईंको सदस्यतामा Azure OpenAI कोटा छ कि छैन जाँच्नुहोस्:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### अनुप्रयोग जडान हुँदैन

**समस्या**: जाभा अनुप्रयोगमा जडान त्रुटिहरू देखिन्छ

**समाधान**:
1. वातावरण चर निर्यात गरिएको छ कि छैन जाँच्नुहोस्:
   
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

2. अन्तबिन्दु फारम्याट सही छ कि छैन (यो `https://xxx.openai.azure.com` हुनुपर्छ)
3. API कुञ्जी Azure पोर्टलबाट प्राथमिक वा द्वितीयक कुञ्जी हो कि छैन जाँच गर्नुहोस्

**समस्या**: Azure OpenAI बाट 401 Unauthorized

**समाधान**:
1. Azure पोर्टल → Keys and Endpoint बाट नयाँ API कुञ्जी लिनुहोस्
2. `AZURE_OPENAI_API_KEY` वातावरण चर पुनः निर्यात गर्नुहोस्
3. मोडेल तैनाथीकरण पूरा भएको छ कि छैन (Azure पोर्टल चेक गर्नुहोस्) सुनिश्चित गर्नुहोस्

### प्रदर्शन सम्बन्धी समस्या

**समस्या**: प्रतिक्रिया समय सुस्त छ

**समाधान**:
1. Azure पोर्टल मेट्रिक्समा OpenAI टोकन उपयोग र थ्रोटलिङ जाँच्नुहोस्
2. सीमा पुगेमा TPM क्षमता बढाउनुहोस्
3. उच्च reasoning-effort स्तर (low/medium/high) प्रयोग गर्ने विचार गर्नुहोस्

## पूर्वाधार अद्यावधिक गर्दै

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

## सुरक्षा सिफारिसहरू

1. **कहिल्यै API कुञ्जीहरू कमिट नगर्नुहोस्** - वातावरण चरहरू प्रयोग गर्नुहोस्
2. **स्थानीय रूपमा .env फाइलहरू प्रयोग गर्नुहोस्** - `.env` लाई `.gitignore` मा थप्नुहोस्
3. **कुञ्जीहरू नियमित घुमाउनुहोस्** - Azure पोर्टलमा नयाँ कुञ्जीहरू उत्पादन गर्नुहोस्
4. **पहुँच सीमित गर्नुहोस्** - Azure RBAC प्रयोग गरी को संसाधन पहुँच गर्न सक्छ नियन्त्रण गर्नुहोस्
5. **प्रयोग मोनिटर गर्नुहोस्** - Azure पोर्टलमा लागत सूचना सेटअप गर्नुहोस्

## अतिरिक्त स्रोतहरू

- [Azure OpenAI सेवा दस्तावेज](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 मोडेल दस्तावेज](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI दस्तावेज](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep दस्तावेज](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI आधिकारिक एकीकरण](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## समर्थन

समस्या भएमा:
1. माथिको [समस्या समाधान खण्ड](../../../../01-introduction/infra) जाँच्नुहोस्
2. Azure पोर्टलमा Azure OpenAI सेवा स्वास्थ्य समीक्षा गर्नुहोस्
3. रिपोजिटरीमा समस्या खोल्नुहोस्

## लाइसेन्स

विवरणको लागि मूल [LICENSE](../../../../LICENSE) फाइल हेर्नुहोस्।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:  
यो दस्तावेज AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरी अनुवाद गरिएको हो। हामी शुद्धताको लागि प्रयासरत छौं भने पनि, कृपया ध्यान दिनुहोस् कि स्वचालित अनुवादमा त्रुटि वा अशुद्धि हुन सक्छ। मूल दस्तावेजलाई यसको स्वदेशी भाषामा आधिकारिक स्रोत मान्नुपर्छ। महत्वपूर्ण जानकारीको लागि, पेशेवर मानव अनुवाद सिफारिस गरिन्छ। यस अनुवाद प्रयोग गर्दा हुने कुनै पनि गलतफहमी वा गलत व्याख्याका लागि हामी जिम्मेवार छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->