# LangChain4j के लिए Azure इन्फ्रास्ट्रक्चर शुरुआत

## अनुक्रमणिका

- [प्रीरेक्विज़िट्स](../../../../01-introduction/infra)
- [आर्किटेक्चर](../../../../01-introduction/infra)
- [संसाधन बनाए गए](../../../../01-introduction/infra)
- [क्विक स्टार्ट](../../../../01-introduction/infra)
- [कॉन्फ़िगरेशन](../../../../01-introduction/infra)
- [मैनेजमेंट कमांड्स](../../../../01-introduction/infra)
- [लागत अनुकूलन](../../../../01-introduction/infra)
- [मॉनिटरिंग](../../../../01-introduction/infra)
- [समस्या निवारण](../../../../01-introduction/infra)
- [इन्फ्रास्ट्रक्चर अपडेट करना](../../../../01-introduction/infra)
- [साफ़-सफाई](../../../../01-introduction/infra)
- [फ़ाइल संरचना](../../../../01-introduction/infra)
- [सुरक्षा सिफारिशें](../../../../01-introduction/infra)
- [अतिरिक्त संसाधन](../../../../01-introduction/infra)

यह डायरेक्टरी Azure OpenAI संसाधनों की तैनाती के लिए Bicep और Azure Developer CLI (azd) का उपयोग करके Azure इन्फ्रास्ट्रक्चर कोड (IaC) रखती है।

## प्रीरेक्विज़िट्स

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (संस्करण 2.50.0 या बाद का)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (संस्करण 1.5.0 या बाद का)
- Azure सब्सक्रिप्शन जिसमें संसाधन बनाने की अनुमति हो

## आर्किटेक्चर

**सरल स्थानीय विकास सेटअप** - केवल Azure OpenAI तैनात करें, सभी ऐप्स स्थानीय रूप से चलाएं।

इन्फ्रास्ट्रक्चर निम्नलिखित Azure संसाधनों को तैनात करता है:

### AI सेवाएं
- **Azure OpenAI**: दो मॉडल तैनाती के साथ Cognitive Services:
  - **gpt-5.2**: चैट पूर्णता मॉडल (20K TPM क्षमता)
  - **text-embedding-3-small**: RAG के लिए एम्बेडिंग मॉडल (20K TPM क्षमता)

### स्थानीय विकास
सभी Spring Boot एप्लिकेशन आपकी मशीन पर स्थानीय रूप से चलते हैं:
- 01-introduction (पोर्ट 8080)
- 02-prompt-engineering (पोर्ट 8083)
- 03-rag (पोर्ट 8081)
- 04-tools (पोर्ट 8084)

## संसाधन बनाए गए

| संसाधन प्रकार | संसाधन नाम पैटर्न | उद्देश्य |
|--------------|----------------------|---------|
| Resource Group | `rg-{environmentName}` | सभी संसाधनों को समाहित करता है |
| Azure OpenAI | `aoai-{resourceToken}` | AI मॉडल होस्टिंग |

> **नोट:** `{resourceToken}` एक अद्वितीय स्ट्रिंग है जो सब्सक्रिप्शन ID, पर्यावरण नाम, और लोकेशन से उत्पन्न होती है

## क्विक स्टार्ट

### 1. Azure OpenAI तैनात करें

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

जब पूछा जाए:
- अपनी Azure सब्सक्रिप्शन चुनें
- स्थान चुनें (सुझाव: `eastus2` GPT-5.2 उपलब्धता के लिए)
- पर्यावरण नाम की पुष्टि करें (डिफ़ॉल्ट: `langchain4j-dev`)

इससे ये बनेंगे:
- Azure OpenAI संसाधन GPT-5.2 और text-embedding-3-small के साथ
- कनेक्शन विवरण आउटपुट करें

### 2. कनेक्शन विवरण प्राप्त करें

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

यह दिखाता है:
- `AZURE_OPENAI_ENDPOINT`: आपका Azure OpenAI एंडपॉइंट URL
- `AZURE_OPENAI_KEY`: प्रमाणीकरण के लिए API कुंजी
- `AZURE_OPENAI_DEPLOYMENT`: चैट मॉडल का नाम (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: एम्बेडिंग मॉडल का नाम

### 3. एप्लिकेशन स्थानीय रूप से चलाएं

`azd up` कमांड स्वचालित रूप से रूट डायरेक्टरी में सभी आवश्यक पर्यावरण चर के साथ `.env` फ़ाइल बनाता है।

**अनुशंसित:** सभी वेब एप्लिकेशन शुरू करें:

**Bash:**
```bash
# रूट डायरेक्टरी से
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# रूट डायरेक्टरी से
cd ../..
.\start-all.ps1
```

या एकल मॉड्यूल शुरू करें:

**Bash:**
```bash
# उदाहरण: केवल परिचय मॉड्यूल शुरू करें
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# उदाहरण: केवल परिचय मॉड्यूल शुरू करें
cd ../01-introduction
.\start.ps1
```

दोनों स्क्रिप्ट स्वचालित रूप से रूट `.env` फ़ाइल से पर्यावरण चर लोड करते हैं जिसे `azd up` द्वारा बनाया गया है।

## कॉन्फ़िगरेशन

### मॉडल तैनाती अनुकूलित करना

मॉडल तैनाती बदलने के लिए, `infra/main.bicep` संपादित करें और `openAiDeployments` पैरामीटर संशोधित करें:

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

उपलब्ध मॉडल और संस्करण: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Azure क्षेत्रों को बदलना

विभिन्न क्षेत्र में तैनाती करने के लिए, `infra/main.bicep` संपादित करें:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

GPT-5.2 उपलब्धता जांचें: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Bicep फाइलों में बदलाव के बाद इन्फ्रास्ट्रक्चर अपडेट करने के लिए:

**Bash:**
```bash
# ARM टेम्पलेट पुनर्निर्माण करें
az bicep build --file infra/main.bicep

# परिवर्तनों का पूर्वावलोकन करें
azd provision --preview

# परिवर्तनों को लागू करें
azd provision
```

**PowerShell:**
```powershell
# ARM टेम्पलेट को पुनः बनाएं
az bicep build --file infra/main.bicep

# परिवर्तनों का पूर्वावलोकन करें
azd provision --preview

# परिवर्तनों को लागू करें
azd provision
```

## साफ़-सफाई

सभी संसाधन हटाने के लिए:

**Bash:**
```bash
# सभी संसाधनों को हटाएं
azd down

# वातावरण सहित सब कुछ हटाएं
azd down --purge
```

**PowerShell:**
```powershell
# सभी संसाधनों को हटाएं
azd down

# पर्यावरण सहित सब कुछ हटाएं
azd down --purge
```

**चेतावनी**: यह सभी Azure संसाधनों को स्थायी रूप से हटा देगा।

## फ़ाइल संरचना

## लागत अनुकूलन

### विकास/परीक्षण
डेव/टेस्ट एनवायरमेंट्स की लागत कम करने के लिए:
- Azure OpenAI के लिए Standard टियर (S0) का उपयोग करें
- `infra/core/ai/cognitiveservices.bicep` में कम क्षमता सेट करें (20K के बजाय 10K TPM)
- उपयोग में न होने पर संसाधनों को हटाएं: `azd down`

### उत्पादन
उत्पादन के लिए:
- उपयोग के आधार पर OpenAI क्षमता बढ़ाएं (50K+ TPM)
- उच्च उपलब्धता के लिए जोन रिडंडेंसी सक्षम करें
- उचित मॉनिटरिंग और लागत अलर्ट लागू करें

### लागत अनुमान
- Azure OpenAI: टोकन-प्रति-भुगतान (इनपुट + आउटपुट)
- GPT-5.2: लगभग $3-5 प्रति 1M टोकन (वर्तमान मूल्य जांचें)
- text-embedding-3-small: लगभग $0.02 प्रति 1M टोकन

मूल्य निर्धारण कैलकुलेटर: https://azure.microsoft.com/pricing/calculator/

## मॉनिटरिंग

### Azure OpenAI मीट्रिक्स देखें

Azure पोर्टल पर जाएं → आपका OpenAI संसाधन → मीट्रिक्स:
- टोकन-आधारित उपयोग
- HTTP अनुरोध दर
- प्रतिक्रिया समय
- सक्रिय टोकन

## समस्या निवारण

### मुद्दा: Azure OpenAI सबडोमेन नाम संघर्ष

**त्रुटि संदेश:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**कारण:**
आपके सब्सक्रिप्शन/पर्यावरण से उत्पन्न सबडोमेन नाम पहले से उपयोग में है, संभवतः पिछली तैनाती के पूर्ण रूप से हटाए न जाने के कारण।

**समाधान:**
1. **विकल्प 1 - अलग पर्यावरण नाम का उपयोग करें:**
   
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

2. **विकल्प 2 - Azure पोर्टल के माध्यम से मैनुअल तैनाती:**
   - Azure पोर्टल जाएं → एक संसाधन बनाएं → Azure OpenAI
   - अपने संसाधन के लिए एक अनूठा नाम चुनें
   - निम्नलिखित मॉडल तैनात करें:
     - **GPT-5.2**
     - **text-embedding-3-small** (RAG मॉड्यूल्स के लिए)
   - **महत्वपूर्ण:** अपनी तैनाती के नाम नोट करें - इन्हें `.env` कॉन्फ़िगरेशन से मेल खाना चाहिए
   - तैनाती के बाद, "Keys and Endpoint" से अपने एंडपॉइंट और API कुंजी प्राप्त करें
   - प्रोजेक्ट रूट में एक `.env` फ़ाइल बनाएं जिसमें:

     **उदाहरण `.env` फ़ाइल:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**मॉडल तैनाती नामकरण दिशानिर्देश:**
- सरल, सुसंगत नामों का उपयोग करें: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- तैनाती के नाम `.env` में निर्धारित नाम से बिल्कुल मेल खाना चाहिए
- सामान्य गलती: मॉडल एक नाम से बनाना लेकिन कोड में अलग नाम संदर्भित करना

### मुद्दा: चयनित क्षेत्र में GPT-5.2 उपलब्ध नहीं

**समाधान:**
- GPT-5.2 एक्सेस वाले क्षेत्र का चयन करें (जैसे, eastus2)
- उपलब्धता जांचें: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### मुद्दा: तैनाती के लिए अपर्याप्त कोटा

**समाधान:**
1. Azure पोर्टल में कोटा वृद्धि का अनुरोध करें
2. या `main.bicep` में कम क्षमता का उपयोग करें (उदा., capacity: 10)

### मुद्दा: स्थानीय रूप से चलाते समय "Resource not found"

**समाधान:**
1. तैनाती सत्यापित करें: `azd env get-values`
2. एंडपॉइंट और कुंजी सही हैं कि नहीं जांचें
3. सुनिश्चित करें कि Azure पोर्टल में संसाधन समूह मौजूद है

### मुद्दा: प्रमाणीकरण विफल

**समाधान:**
- सुनिश्चित करें `AZURE_OPENAI_API_KEY` सही सेट है
- कुंजी फ़ॉर्मेट 32-कैरेक्टर हेक्साडेसिमल स्ट्रिंग होना चाहिए
- आवश्यकता हो तो Azure पोर्टल से नई कुंजी प्राप्त करें

### तैनाती विफल होती है

**मुद्दा**: `azd provision` कोटा या क्षमता त्रुटियों के साथ विफल

**समाधान**: 
1. अन्य क्षेत्र आज़माएं - [Azure क्षेत्रों को बदलना](../../../../01-introduction/infra) अनुभाग देखें
2. जांच करें आपकी सब्सक्रिप्शन में Azure OpenAI कोटा है:

   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### एप्लिकेशन कनेक्ट नहीं हो रहा

**मुद्दा**: Java एप्लिकेशन में कनेक्शन त्रुटियाँ दिख रही हैं

**समाधान**:
1. पर्यावरण चर ठीक तरह से निर्यात किए गए हैं, जांचें:
   
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

2. एंडपॉइंट फॉर्मेट सही है (उदाहरण: `https://xxx.openai.azure.com`)
3. API कुंजी Azure पोर्टल से प्राइमरी या सेकेंडरी कुंजी होनी चाहिए

**मुद्दा**: Azure OpenAI से 401 Unauthorized

**समाधान**:
1. Azure पोर्टल → Keys and Endpoint से नया API कुंजी प्राप्त करें
2. `AZURE_OPENAI_API_KEY` पर्यावरण चर पुनः निर्यात करें
3. सुनिश्चित करें मॉडल तैनाती पूर्ण हो चुकी है (Azure पोर्टल जांचें)

### प्रदर्शन समस्याएँ

**मुद्दा**: धीमी प्रतिक्रिया समय

**समाधान**:
1. Azure पोर्टल मीट्रिक्स में OpenAI टोकन उपयोग और थ्रॉटलिंग की जांच करें
2. यदि सीमा पर हैं तो TPM क्षमता बढ़ाएं
3. अधिक reasoning-effort स्तर (कम/मध्यम/उच्च) का उपयोग करने पर विचार करें

## इन्फ्रास्ट्रक्चर अपडेट करना

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

## सुरक्षा सिफारिशें

1. **कभी API कुंजियाँ कमिट न करें** - पर्यावरण चर का उपयोग करें
2. **स्थानीय रूप से .env फ़ाइलें उपयोग करें** - `.env` को `.gitignore` में जोड़ें
3. **कुंजियों को नियमित रूप से घुमाएं** - Azure पोर्टल में नई कुंजियाँ बनाएं
4. **पहुँच सीमित करें** - संसाधनों तक पहुँच नियंत्रित करने के लिए Azure RBAC का उपयोग करें
5. **उपयोग की निगरानी करें** - Azure पोर्टल में लागत अलर्ट सेट करें

## अतिरिक्त संसाधन

- [Azure OpenAI सेवा दस्तावेज़ीकरण](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 मॉडल दस्तावेज़ीकरण](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI दस्तावेज़ीकरण](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep दस्तावेज़ीकरण](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI आधिकारिक इंटीग्रेशन](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## सपोर्ट

समस्याओं के लिए:
1. ऊपर [समस्या निवारण अनुभाग](../../../../01-introduction/infra) देखें
2. Azure पोर्टल में Azure OpenAI सेवा स्वास्थ्य की समीक्षा करें
3. रिपॉजिटरी में एक इश्यू खोलें

## लाइसेंस

विवरण के लिए रूट [LICENSE](../../../../LICENSE) फ़ाइल देखें।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:  
इस दस्तावेज़ का अनुवाद AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके किया गया है। जब हम सटीकता के लिए प्रयासरत हैं, कृपया ध्यान दें कि स्वचालित अनुवादों में त्रुटियाँ या अशुद्धियाँ हो सकती हैं। मूल दस्तावेज़ अपनी मूल भाषा में अधिकृत स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए, पेशेवर मानवीय अनुवाद की सलाह दी जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गोपनीयता के लिए हम उत्तरदायी नहीं हैं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->