# LangChain4j साठी Azure पायाभूत सुविधा सुरू करणे

## विषय सूची

- [पूर्व आवश्यकता](../../../../01-introduction/infra)
- [आर्किटेक्चर](../../../../01-introduction/infra)
- [निर्मित संसाधने](../../../../01-introduction/infra)
- [जलद प्रारंभ](../../../../01-introduction/infra)
- [संरचना](../../../../01-introduction/infra)
- [व्यवस्थापन आदेश](../../../../01-introduction/infra)
- [खर्च अनुकूलन](../../../../01-introduction/infra)
- [निगराणी](../../../../01-introduction/infra)
- [समस्या निवारण](../../../../01-introduction/infra)
- [पायाभूत सुविधा अद्ययावत करणे](../../../../01-introduction/infra)
- [क्लीन अप](../../../../01-introduction/infra)
- [फाइल रचना](../../../../01-introduction/infra)
- [सुरक्षा शिफारसी](../../../../01-introduction/infra)
- [अतिरिक्त संसाधने](../../../../01-introduction/infra)

हा डिरेक्टरी बाइसेप आणि Azure Developer CLI (azd) वापरून Azure OpenAI संसाधने तैनात करण्यासाठी Azure पायाभूत सुविधा कोड (IaC) समाविष्ट करते.

## पूर्व आवश्यकता

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (आवृत्ती 2.50.0 किंवा नंतरची)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (आवृत्ती 1.5.0 किंवा नंतरची)
- संसाधने तयार करण्यासाठी परवानग्या असलेली Azure सदस्यता

## आर्किटेक्चर

**सोपे स्थानिक विकसन सेटअप** - फक्त Azure OpenAI तैनात करा, सर्व अॅप्स स्थानिकरित्या चालवा.

पायाभूत सुविधा खालील Azure संसाधने तैनात करते:

### AI सेवा
- **Azure OpenAI**: दोन मॉडेल तैनाती सह कॉग्निटिव्ह सेवा:
  - **gpt-5.2**: चॅट पूर्णता मॉडेल (20K TPM क्षमता)
  - **text-embedding-3-small**: RAG साठी एम्बेडिंग मॉडेल (20K TPM क्षमता)

### स्थानिक विकास
सर्व स्प्रिंग बूट अनुप्रयोग तुमच्या संगणकावर स्थानिकरित्या चालतात:
- 01-introduction (पोर्ट 8080)
- 02-prompt-engineering (पोर्ट 8083)
- 03-rag (पोर्ट 8081)
- 04-tools (पोर्ट 8084)

## निर्मित संसाधने

| संसाधन प्रकार | संसाधन नाव नमुना | उद्देश |
|--------------|----------------------|---------|
| संसाधन समूह | `rg-{environmentName}` | सर्व संसाधने यामध्ये असतात |
| Azure OpenAI | `aoai-{resourceToken}` | AI मॉडेल होस्टिंग |

> **टीप:** `{resourceToken}` हा सदस्यता आयडी, पर्यावरण नाव आणि स्थान यावरून तयार केलेला खास स्ट्रिंग आहे

## जलद प्रारंभ

### 1. Azure OpenAI तैनात करा

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

जेव्हा विचारले जाईल:
- तुमची Azure सदस्यता निवडा
- एक स्थान निवडा (शिफारस: GPT-5.2 उपलब्धतेसाठी `eastus2`)
- पर्यावरण नाव पुष्टी करा (पूर्वनिर्धारित: `langchain4j-dev`)

हे तयार करेल:
- GPT-5.2 आणि text-embedding-3-small सह Azure OpenAI संसाधन
- आउटपुट कनेक्शन तपशील

### 2. कनेक्शन तपशील मिळवा

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

हे दर्शविते:
- `AZURE_OPENAI_ENDPOINT`: तुमचा Azure OpenAI एंडपॉइंट URL
- `AZURE_OPENAI_KEY`: प्रमाणीकरणासाठी API की
- `AZURE_OPENAI_DEPLOYMENT`: चॅट मॉडेल नाव (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: एम्बेडिंग मॉडेल नाव

### 3. अनुप्रयोग स्थानिकरित्या चालवा

`azd up` कमांड आपोआप मूळ निर्देशिकेत सर्व आवश्यक पर्यावरण चलांसह `.env` फाइल तयार करते.

**शिफारस:** सर्व वेब अनुप्रयोग सुरू करा:

**Bash:**
```bash
# मूळ निर्देशिकेतून
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# मूळ निर्देशिका पासून
cd ../..
.\start-all.ps1
```

किंवा एकच मॉड्यूल सुरू करा:

**Bash:**
```bash
# उदाहरण: फक्त परिचय मोड्युल सुरू करा
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# उदाहरण: फक्त परिचय मॉड्यूल सुरू करा
cd ../01-introduction
.\start.ps1
```

दोन्ही स्क्रिप्ट `azd up` ने तयार केलेल्या मूळ `.env` फाइलमधून पर्यावरण चलं लोड करतात.

## संरचना

### मॉडेल तैनाती सानुकूलित करणे

मॉडेल तैनाती बदलण्यासाठी, `infra/main.bicep` संपादित करा आणि `openAiDeployments` पॅरामीटर बदला:

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

उपलब्ध मॉडेल आणि आवृत्त्या: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Azure प्रदेश बदलणे

वेगळ्या प्रदेशात तैनात करण्यासाठी, `infra/main.bicep` संपादित करा:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

GPT-5.2 उपलब्धतेसाठी तपासा: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

बाइसेप फायलींमध्ये बदल केल्यानंतर पायाभूत सुविधा अद्ययावत करण्यासाठी:

**Bash:**
```bash
# ARM टेम्पलेट पुन्हा तयार करा
az bicep build --file infra/main.bicep

# बदलांचे पूर्वावलोकन करा
azd provision --preview

# बदल लागू करा
azd provision
```

**PowerShell:**
```powershell
# ARM टेम्पलेट पुन्हा तयार करा
az bicep build --file infra/main.bicep

# बदल पूर्वदर्शन करा
azd provision --preview

# बदल लागू करा
azd provision
```

## क्लीन अप

सर्व संसाधने हटवण्यासाठी:

**Bash:**
```bash
# सर्व संसाधने हटवा
azd down

# पर्यावरणासह सर्वकाही हटवा
azd down --purge
```

**PowerShell:**
```powershell
# सर्व स्रोत हटवा
azd down

# पर्यावरणासह सर्वकाही हटवा
azd down --purge
```

**इशारा**: हे सर्व Azure संसाधने कायमस्वरूपी हटवेल.

## फाइल रचना

## खर्च अनुकूलन

### विकास/चाचणी
विकास/चाचणी पर्यावरणासाठी तुम्ही खर्च कमी करू शकता:
- Azure OpenAI साठी Standard tier (S0) वापरा
- `infra/core/ai/cognitiveservices.bicep` मध्ये 20K ऐवजी कमी क्षमता (10K TPM) सेट करा
- वापरात नसताना संसाधने काढा: `azd down`

### उत्पादन
उत्पादनासाठी:
- वापरावर आधारित OpenAI क्षमता वाढवा (50K+ TPM)
- उच्च उपलब्धतेसाठी झोन पुनरावृत्ती सक्षम करा
- योग्य निगराणी आणि खर्च सूचना वापरा

### खर्च अंदाज
- Azure OpenAI: प्रति टोकन (इनपुट + आउटपुट) पाहून पैसे द्या
- GPT-5.2: सुमारे $3-5 प्रति 1M टोकन (सध्याच्या किंमती तपासा)
- text-embedding-3-small: सुमारे $0.02 प्रति 1M टोकन

किंमत गणक: https://azure.microsoft.com/pricing/calculator/

## निगराणी

### Azure OpenAI मेट्रिक्स पहा

Azure पोर्टल → तुमचे OpenAI संसाधन → मेट्रिक्स:
- टोकन-आधारित वापर
- HTTP विनंती दर
- प्रतिसाद वेळ
- सक्रिय टोकन्स

## समस्या निवारण

### समस्या: Azure OpenAI उपडोमेन नावाचा वाद

**त्रुटी संदेश:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**कारण:**
तुमच्या सदस्यता/पर्यावरणातून तयार झालेल्या उपडोमेन नावाचा वापर आधीपासूनच होत आहे, कदाचित अगोदरच्या पूर्णपणे साफ न झालेल्या तैनातीमुळे.

**उपाय:**
1. **पर्याय 1 - वेगळे पर्यावरण नाव वापरा:**
   
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

2. **पर्याय 2 - Azure पोर्टलद्वारे मॅन्युअल तैनात करा:**
   - Azure पोर्टल वर जा → Create a resource → Azure OpenAI
   - तुमच्या संसाधनासाठी वेगळे नाव निवडा
   - पुढील मॉडेल तैनात करा:
     - **GPT-5.2**
     - **text-embedding-3-small** (RAG मॉड्यूलसाठी)
   - **महत्वाचे:** तुमची तैनाती नावे `.env` संरचनेशी जुळली पाहिजे
   - तैनाती नंतर, "Keys and Endpoint" मधून तुमचा एंडपॉइंट आणि API की मिळवा
   - प्रकल्प मूळ निर्देशिकेत `.env` फाइल तयार करा:
     
     **उदाहरण `.env` फाइल:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**मॉडेल तैनाती नाव मार्गदर्शक:**
- सोपी, सुसंगत नावे वापरा: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- तैनाती नावे `.env` मध्ये जसे आहेत तशीच जुळली पाहिजे
- सामान्य चूक: एका नावाने मॉडेल तयार करणे पण कोडमध्ये वेगळे नाव देणे

### समस्या: निवडलेल्या प्रदेशात GPT-5.2 उपलब्ध नाही

**उपाय:**
- GPT-5.2 उपलब्धतेसह प्रदेश निवडा (उदा. eastus2)
- उपलब्धता तपासा: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### समस्या: तैनातीसाठी अपुरी कोटा

**उपाय:**
1. Azure पोर्टल मध्ये कोटा वाढीची विनंती करा
2. किंवा `main.bicep` मध्ये कमी क्षमता वापरा (उदा. capacity: 10)

### समस्या: स्थानिक चालवताना "Resource not found" त्रुटी

**उपाय:**
1. तैनाती तपासा: `azd env get-values`
2. एंडपॉइंट आणि की योग्य आहेत का तपासा
3. Azure पोर्टल मध्ये संसाधन समूह अस्तित्वात आहे याची खात्री करा

### समस्या: प्रमाणीकरण अयशस्वी

**उपाय:**
- `AZURE_OPENAI_API_KEY` योग्य प्रकारे सेट आहे का तपासा
- की 32-कॅरेक्टर हेक्साडेसिमल स्ट्रिंग असावी
- आवश्यक असल्यास Azure पोर्टल मधून नवीन की मिळवा

### तैनाती अयशस्वी

**समस्या**: `azd provision` कोटा किंवा क्षमता त्रुटींसह अयशस्वी

**उपाय**: 
1. वेगळ्या प्रदेशात प्रयत्न करा - प्रदेश कसे सेट करायचे यासाठी [Changing Azure Regions](../../../../01-introduction/infra) विभाग पहा
2. तुमच्याकडे Azure OpenAI कोटा आहे का तपासा:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### अनुप्रयोग कनेक्ट होत नाही

**समस्या**: Java अनुप्रयोग कनेक्शन त्रुटी दर्शवित आहे

**उपाय**:
1. पर्यावरण चल निर्यात झाले आहेत का तपासा:
   
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

2. एंडपॉइंट योग्य स्वरूपात आहे का तपासा (`https://xxx.openai.azure.com` असावे)
3. API की Azure पोर्टलमधील मुख्य किंवा दुसरीकडे की आहे का तपासा

**समस्या**: Azure OpenAI कडून 401 Unauthorized

**उपाय**:
1. Azure पोर्टल → Keys and Endpoint मधून ताजी API की मिळवा
2. `AZURE_OPENAI_API_KEY` पर्यावरण चल पुन्हा निर्यात करा
3. मॉडेल तैनाती पूर्ण झाली आहेत का तपासा (Azure पोर्टल मध्ये)

### कार्यक्षमतेच्या समस्या

**समस्या**: प्रतिसाद वेळ खूप सतत

**उपाय**:
1. Azure पोर्टलमध्ये OpenAI टोकन वापर आणि थ्रॉटलिंग तपासा
2. जर मर्यादा गाठत असाल तर TPM क्षमता वाढवा
3. उच्च reasoning-effort स्तर वापर विचार करा (low/medium/high)

## पायाभूत सुविधा अद्ययावत करणे

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

## सुरक्षा शिफारसी

1. **कधीही API की कमिट करू नका** - पर्यावरण चल वापरा
2. **स्थानिकपणे .env फाइल वापरा** - `.env` ला `.gitignore` मध्ये जोडा
3. **एक की नियमितपणे फिरवा** - Azure पोर्टलमधून नवीन की तयार करा
4. **परवानग्या मर्यादित करा** - Azure RBAC वापरून कोण कोणाला प्रवेश आहे ते नियंत्रित करा
5. **वापराचे निरीक्षण करा** - Azure पोर्टलमध्ये खर्च सूचना सेट करा

## अतिरिक्त संसाधने

- [Azure OpenAI सेवा दस्तऐवज](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 मॉडेल दस्तऐवज](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI दस्तऐवज](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep दस्तऐवज](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI अधिकृत इंटिग्रेशन](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## समर्थन

समस्यांसाठी:
1. वर दिलेला [समस्या निवारण विभाग](../../../../01-introduction/infra) तपासा
2. Azure पोर्टल मध्ये Azure OpenAI सेवा आरोग्य तपासा
3. रिपॉझिटरीमध्ये इश्यू उघडा

## परवाना

सविस्तर माहितीसाठी मूळ [LICENSE](../../../../LICENSE) फाइल पहा.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
हा दस्तऐवज AI भाषांतर सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) चा वापर करून भाषांतरित करण्यात आला आहे. आम्ही अचूकतेसाठी प्रयत्नशील आहोत, तरी कृपया लक्षात घ्या की स्वयंचलित भाषांतरांमध्ये चुका किंवा अन्यथा अचूक नसणे शक्य आहे. मूळ दस्तऐवज त्याच्या स्थानिक भाषेत अधिकृत स्रोत समजावा. अत्यंत महत्त्वाची माहिती असल्या प्रकरणी व्यावसायिक मानवी भाषांतर करण्याची शिफारस केली जाते. या भाषांतराच्या वापरामुळे उद्भवलेल्या कोणत्याही गैरसमजुती किंवा चुकीच्या अर्थान्नी आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->