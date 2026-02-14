# LangChain4j కోసం Azure మౌలిక సదుపాయాలతో ప్రారంభించడం

## ముఖ్యాంశాలు

- [ముందస్తు అవసరాలు](../../../../01-introduction/infra)
- [వాస్తవ్య నిర్మాణం](../../../../01-introduction/infra)
- [సృష్టించిన వనరులు](../../../../01-introduction/infra)
- [త్వరిత ప్రారంభం](../../../../01-introduction/infra)
- [నిర్వచనం](../../../../01-introduction/infra)
- [నిర్వహణ ఆదేశాలు](../../../../01-introduction/infra)
- [ఖర్చు ఆప్టిమైజేషన్](../../../../01-introduction/infra)
- [మానిటరింగ్](../../../../01-introduction/infra)
- [పరిష్కారాలు](../../../../01-introduction/infra)
- [ఇన్‌ఫ్రాస్ట్రక్చర్ నవీకరణ](../../../../01-introduction/infra)
- [శుభ్రపరచడం](../../../../01-introduction/infra)
- [ఫైల్ నిర్మాణం](../../../../01-introduction/infra)
- [భద్రతా సిఫార్సులు](../../../../01-introduction/infra)
- [అదనపు వనరులు](../../../../01-introduction/infra)

ఈ డైరెక్టరీ Bicep మరియు Azure Developer CLI (azd) ఉపయోగించి Azure OpenAI వనరులను డిప్లాయ్ చేయడానికి Azure మౌలిక సదుపాయాలను కోడ్ (IaC) రూపంలో కలిగి ఉంటుంది.

## ముందస్తు అవసరాలు

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (వెర్షన్ 2.50.0 లేదా అంతకంటే తాజా)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (వెర్షన్ 1.5.0 లేదా అంతకంటే తాజా)
- వనరులు సృష్టించడానికి అనుమతులున్న Azure సబ్‌స్క్రిప్షన్

## వాస్తవ్య నిర్మాణం

**సరళమైన స్థానిక అభివృద్ధి సెట్టప్** - Azure OpenAI మాత్రమే డిప్లాయ్ చేయండి, అన్ని యాప్స్ స్థానికంగా నడిపించండి.

ఇన్‌ఫ్రాస్ట్రక్చర్ ఈ క్రింది Azure వనరులను డిప్లాయ్ చేస్తుంది:

### AI సేవలు
- **Azure OpenAI**: రెండు మోడల్ డిప్లాయ్‌మెంట్స్ తో కాగ్నిటివ్ సేవలు:
  - **gpt-5.2**: చాట్ కంప్లీషన్ మోడల్ (20K TPM సామర్థ్యం)
  - **text-embedding-3-small**: RAG కోసం ఎంబెడ్డింగ్ మోడల్ (20K TPM సామర్థ్యం)

### స్థానిక అభివృద్ధి
మీ యంత్రంలో అన్ని Spring Boot అప్లికేషన్లు స్థానికంగా నడుస్తాయి:
- 01-introduction (పోర్ట్ 8080)
- 02-prompt-engineering (పోర్ట్ 8083)
- 03-rag (పోర్ట్ 8081)
- 04-tools (పోర్ట్ 8084)

## సృష్టించిన వనరులు

| వనరు రకం    | వనరు పేరు నమూనా          | ఉపయోగం                |
|--------------|--------------------------|------------------------|
| Resource Group | `rg-{environmentName}`    | అన్ని వనరులను కలిగి ఉంచుతుంది |
| Azure OpenAI | `aoai-{resourceToken}`    | AI మోడల్ హోస్టింగ్       |

> **గమనిక:** `{resourceToken}` అనేది సబ్‌స్క్రిప్షన్ ID, పరిసర పేరు, మరియు ప్రాంతం నుండి రూపొందించిన ప్రత్యేక స్ట్రింగ్

## త్వరిత ప్రారంభం

### 1. Azure OpenAI డిప్లాయ్ చేయండి

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

ప్రాంప్ట్ వచ్చినప్పుడు:
- మీ Azure సబ్‌స్క్రిప్షన్ సెలెక్ట్ చేసుకోండి
- ఒక ప్రాంతం ఎంచుకోండి (సిఫార్సు: GPT-5.2 కోసం `eastus2`)
- పరిసర పేరును నిర్ధారించండి (డిఫాల్ట్: `langchain4j-dev`)

ఇది క్రియేట్ చేస్తుంది:
- GPT-5.2 మరియు text-embedding-3-small కలిగిన Azure OpenAI వనరు
- కనెక్షన్ వివరాలు వెలువడిస్తాయి

### 2. కనెక్షన్ వివరాలు పొందండి

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

ఇది ప్రదర్శిస్తుంది:
- `AZURE_OPENAI_ENDPOINT`: మీ Azure OpenAI ఎండ్‌పాయింట్ URL
- `AZURE_OPENAI_KEY`: ధృవీకరణ కోసం API కీ
- `AZURE_OPENAI_DEPLOYMENT`: చాట్ మోడల్ పేరు (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: ఎంబెడ్డింగ్ మోడల్ పేరు

### 3. అప్లికేషన్లను స్థానికంగా నడపండి

`azd up` కమాండ్ రూట్ డైరెక్టరీలో అవసరమైన అన్ని ఎన్విరాన్‌మెంట్ వేరియబుల్స్‌తో `.env` ఫైల్‌ని ఆటోమాటిక్‌గా సృష్టిస్తుంది.

**శిఫార్సు:** అన్ని వెబ్ అప్లికేషన్లను ప్రారంభించండి:

**Bash:**
```bash
# రూట్ డైరెక్టరీ నుండి
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# మూల డైరెక్టరీ నుండి
cd ../..
.\start-all.ps1
```

లేదా ఒకే మాడ్యూల్‌ను ప్రారంభించండి:

**Bash:**
```bash
# ఉదాహరణ: పరిచయ మాడ్యూల్‌నినే ప్రారంభించండి
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# ఉదాహరణ: ప్రారంభం కేవలం పరిచయ మాడ్యూల్ నుంచి
cd ../01-introduction
.\start.ps1
```

రెండు స్క్రిప్ట్‌లు కూడా `azd up` ద్వారా సృష్టించబడిన రూట్ `.env` ఫైల్ నుండి ఎన్విరాన్‌మెంట్ వేరియబుల్స్‌ను ఆటోమేటిక్‌గా లోడ్ చేస్తాయి.

## నిర్వచనం

### మోడల్ డిప్లాయ్‌మెంట్‌లను కસ્ટમైజ్ చేయడం

మోడల్ డిప్లాయ్‌మెంట్లను మార్చడానికి `infra/main.bicep` ను ఎడిట్ చేసి `openAiDeployments` ప్యారామీటర్‌‌ను సవరించండి:

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

అందుబాటులో ఉన్న మోడల్స్ మరియు వెర్షన్లు: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Azure ప్రాంతాలను మార్చడం

వేరే ప్రాంతంలో డిప్లాయ్ చేయడానికి `infra/main.bicep` ను సవరించండి:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

GPT-5.2 అందుబాటుని పరిశీలించండి: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Bicep ఫైళ్లలో మార్పులు చేసిన తర్వాత ఇన్‌ఫ్రాస్ట్రుకున్నకు నవీకరణ:

**Bash:**
```bash
# ARM టెంప్లేట్‌ను మళ్లీ నిర్మించండి
az bicep build --file infra/main.bicep

# మార్పులను ముందుదృష్టి చేయండి
azd provision --preview

# మార్పులను వర్తింప చేయండి
azd provision
```

**PowerShell:**
```powershell
# ARM టెంప్లేట్‌ను మళ్లీ నిర్మించండి
az bicep build --file infra/main.bicep

# మార్పులను ముందుగా చూడండి
azd provision --preview

# మార్పులను వర్తింపచేయండి
azd provision
```

## శుభ్రపరచడం

అన్ని వనరులను తొలగించడానికి:

**Bash:**
```bash
# అన్ని వనరులను తొలగించండి
azd down

# పర్యావరణం సహా అన్నింటినీ తొలగించండి
azd down --purge
```

**PowerShell:**
```powershell
# అన్ని వనరులను తొలగించండి
azd down

# వాతావరణం సహా అన్ని విషయాలను తొలగించండి
azd down --purge
```

**హెచ్చరిక:** ఇది అన్ని Azure వనరులను శాశ్వతంగా తొలగిస్తుంది.

## ఫైల్ నిర్మాణం

## ఖర్చు ఆప్టిమైజేషన్

### అభివృద్ధి/పరీక్ష

డెవ్/టెస్ట్ పరిసరాలలో ఖర్చు తగ్గించడానికి:
- Azure OpenAI కోసం Standard టియర్ (S0) ఉపయోగించండి
- `infra/core/ai/cognitiveservices.bicep` లో సామర్థ్యాన్ని తక్కువగా (10K TPM 20K కాని) సెట్ చేయండి
- ఉపయోగంలో కాదప్పుడు వనరులను తొలగించండి: `azd down`

### ఉత్పత్తి

ఉత్పత్తి కోసం:
- వినియోగానికి అనుగుణంగా OpenAI సామర్థ్యాన్ని పెంచండి (50K+ TPM)
- అధిక అందుబాటుచే zona redundancy ను ఎనేబుల్ చేయండి
- సరైన మానిటరింగ్ మరియు ఖర్చు అలర్ట్‌లను అమలు చేయండి

### ఖర్చు అంచనా

- Azure OpenAI: టోకెన్ పరంగా చెల్లింపు (ఇన్‌పుట్ + అవుట్పుట్)
- GPT-5.2: సుమారు $3-5 ప్ర‌తి 1 మిలియన్ టోకెన్స్ (ప్రస్తుత ధర‌ల‌ను తనిఖీ చేయండి)
- text-embedding-3-small: సుమారు $0.02 ప్ర‌తి 1 మిలియన్ టోకెన్స్

ధరల కొలత యంత్రం: https://azure.microsoft.com/pricing/calculator/

## మానిటరింగ్

### Azure OpenAI మెట్రిక్స్ చూసే విధానం

Azure పోర్టల్ → మీ OpenAI వనరు → మెట్రిక్స్ వద్ద:
- టోకెన్-ఆధారిత వినియోగం
- HTTP అభ్యర్థన రేటు
- సమాధానానికి సమయం
- క్రియాశీల టోకెన్లు

## పరిష్కారాలు

### సమస్య: Azure OpenAI సబ్‌డొమైన్ పేరు ఘర్షణ

**లోప సందేశం:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**కారణం:**
మీ సబ్‌స్క్రిప్షన్/పరిసరాల నుండి ఉత్పన్నమయ్యే సబ్‌డొమైన్ పేరు ఇప్పటికే ఉపయోగంలో ఉంది, అది పూర్వ డిప్లాయ్‌మెంట్ పూర్తిగా తొలగించబడదు.

**పరిష్కారం:**
1. **వికల్పం 1 - వేరే పరిసర పేరు ఉపయోగించండి:**
   
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

2. **వికల్పం 2 - Azure పోర్టల్ ద్వారా మాన్యువల్ డిప్లాయ్‌మెంట్:**
   - Azure పోర్టల్ → Create a resource → Azure OpenAI కి వెళ్ళండి
   - మీ వనరుకు ప్రత్యేక పేరు ఎంచుకోండి
   - ఈ క్రింది మోడల్స్‌ను డిప్లాయ్ చేయండి:
     - **GPT-5.2**
     - **text-embedding-3-small** (RAG మాడ్యూల్స్ కోసం)
   - **ముఖ్యo:** డిప్లాయ్‌మెంట్ పేర్లు `.env` కాన్ఫిగరేషన్‌తో సరిపోవాలి
   - డిప్లాయ్‌మెంట్ తర్వాత, "Keys and Endpoint" నుండి మీ ఎండ్‌పాయింట్ మరియు API కీ పొందండి
   - ప్రాజెక్ట్ రూట్‌లో `.env` ఫైల్ సృష్టించి లోపల వెయ్యండి:
     
     **ఉదాహరణ `.env` ఫైల్:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**మోడల్ డిప్లాయ్‌మెంట్ పేరు గైడ్లైన్లు:**
- సాదా, సరళమైన పేర్లను ఉపయోగించండి: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- డిప్లాయ్‌మెంట్ పేర్లు `.env` లో మీరు సెట్ చేసిన పేర్లతో ఖచ్చితంగా మ్యాచ్ అయి ఉండాలి
- సాధారణ తప్పు: ఒక పేరుతో మోడల్ క్రియేట్ చేసి, కోడ్‌లో వేరే పేరుని సూచించడం

### సమస్య: ఎంచుకున్న ప్రాంతంలో GPT-5.2 అందుబాటులో లేదు

**పరిష్కారం:**
- GPT-5.2 అందుబాటున్న ప్రాంతాన్ని ఎంచుకోండి (ఉదా: eastus2)
- అందుబాటును తనిఖీ చేయండి: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### సమస్య: డిప్లాయ్‌మెంట్ కోసం తగిన క్షమత లేదు

**పరిష్కారం:**
1. Azure పోర్టల్‌లో క్వోటా పెంచాలని అభ్యర్థించండి
2. లేదా `main.bicep` లో తక్కువ సామర్థ్యం (ఉదా: capacity: 10) ఉపయోగించండి

### సమస్య: స్థానికంగా నడుపుతున్నప్పుడు "Resource not found" సందేశం

**పరిష్కారం:**
1. డిప్లాయ్‌మెంట్‌ను ధృవీకరించండి: `azd env get-values`
2. ఎండ్‌పాయింట్ మరియు కీ సరిచూసుకోండి
3. Azure పోర్టల్‌లో వనరు గ్రూప్ ఉందో లేదో పరిశీలించండి

### సమస్య: ధృవీకరణ విఫలము

**పరిష్కారం:**
- `AZURE_OPENAI_API_KEY` సరిగ్గా సెట్ చేయబడిందా అని తనిఖీ చేయండి
- కీ ఫార్మాట్ 32 అక్షరాల హెక్సడెసిమల్ స్ట్రింగ్ అయి ఉండాలి
- అవసరం అయితే Azure పోర్టల్ నుండి కొత్త కీ ని పొందండి

### డిప్లాయ్‌మెంట్ విఫలం

**సమస్య:** `azd provision` క్వోటా లేదా సామర్థ్య లోపాలతో విఫలమయ్యింది

**పరిష్కారం:** 
1. వేరే ప్రాంతాన్ని ప్రయత్నించండి - [Azure ప్రాంతాలు మార్చడం](../../../../01-introduction/infra) సెక్షన్ చూడండి
2. మీ సబ్‌స్క్రిప్షన్ Azure OpenAI క్వోటా కలిగి ఉందా ఇప్పటికే చూసుకోండి:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### అప్లికేషన్ కనెక్షన్ విఫలం

**సమస్య:** Java అప్లికేషన్ కనెక్షన్ లోపాలను చూపిస్తోంది

**పరిష్కారం:**
1. ఎన్విరాన్‌మెంట్ వేరియబుల్స్ ఎగుమతిచేయబడ్డాయా తనిఖీ చేయండి:
   
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

2. ఎండ్‌పాయింట్ ఫార్మాట్ సరైనదా చూసుకోండి (ఉదా: `https://xxx.openai.azure.com`)
3. API కీ Azure పోర్టల్ నుండి ప్రాథమిక లేదా ద్వితీయ కీగా ఉందా ధృవీకరించండి

**సమస్య:** Azure OpenAI నుండి 401 Unauthorized

**పరిష్కారం:**
1. Azure పోర్టల్ → Keys and Endpoint నుండి తాజా API కీ పొందండి
2. `AZURE_OPENAI_API_KEY` ఎన్విరాన్‌మెంట్ వేరియబుల్‌ను మళ్ళీ ఎగుమతిచేయండి
3. మోడల్ డిప్లాయ్‌మెంట్ పూర్తయిందా Azure పోర్టల్‌లో తనిఖీ చేయండి

### పనితీరు సమస్యలు

**సమస్య:** ప్రతిస్పందన సమయాలు మందగించినవి

**పరిష్కారం:**
1. Azure పోర్టల్ మెట్రిక్స్‌లో OpenAI టోకెన్ వినియోగం మరియు థ్రాట్లింగ్ తనిఖీ చేయండి
2. మీరు పరిమితులకు చేరుకుంటున్నట్లైతే TPM సామర్థ్యం పెంచండి
3. మరింత reasoning-effort స్థాయిని (తక్కువ/మధ్యస్థ/ఎత్తు) ఉపయోగించmayı పరiganఁ్

## ఇన్‌ఫ్రాస్ట్రక్చర్ నవీకరణ

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

## భద్రతా సిఫార్సులు

1. **ఏదైనా API కీస్ కమిట్ చేయవద్దు** - ఎన్విరాన్‌మెంట్ వేరియబుల్స్ ఉపయోగించండి
2. **స్థానికంగా .env ఫైళ్ళను ఉపయోగించండి** - `.env` ను `.gitignore` లో ఉంచండి
3. **కీస్‌ను నియమితంగా మారుస్తూ ఉండండి** - Azure పోర్టల్‌లో కొత్త కీస్ సృష్టించండి
4. **ప్రవేశ పరిమితులను అమలు చేయండి** - Azure RBAC ఉపయోగించి వనరులకు యాక్సెస్ నియంత్రించండి
5. **వినియోగాన్ని మానిటర్ చేయండి** - Azure పోర్టల్‌లో ఖర్చుల అలర్ట్‌లు సెట్ చేయండి

## అదనపు వనరులు

- [Azure OpenAI సర్వీస్ డాక్యుమెంటేషన్](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 మోడల్ డాక్యుమెంటేషన్](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI డాక్యుమెంటేషన్](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep డాక్యుమెంటేషన్](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI అధికారిక ఇంటిగ్రేషన్](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## మద్దతు

సమస్యల కోసం:
1. పై [పరిష్కారాలు భాగాన్ని](../../../../01-introduction/infra) చూడండి
2. Azure పోర్టల్‌లో Azure OpenAI సేవ ఆరోగ్యాన్ని సమీక్షించండి
3. రిపోజిటరీలో ఇష్యూ ఓపెన్ చేయండి

## లైసెన్స్

వివరాలకు రూట్ [LICENSE](../../../../LICENSE) ఫైల్ చూడండి.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**అస్పష్టత**:
ఈ పత్రం AI అనువాద సేవ [Co-op Translator](https://github.com/Azure/co-op-translator) ఉపయోగించి అనువదించబడింది. మేము సరైనత కోసం యత్నించినప్పటికీ, ఆటోమేటెడ్ అనువాదాల్లో తప్పులు లేదా అసత్యతలు ఉండవచ్చు. మూల పత్రాన్ని దాని స్థానిక భాషలో అధికారం కలిగిన మూలంగా పరిగణించాలి. అత్యవసరమైన సమాచారం కోసం, వృత్తిపరమైన మానవ అనువాదాన్ని సిఫార్సు చేస్తాము. ఈ అనువాదం వాడకంలో పుట్టే మాట్లాడుకోలేని లేదా తప్పుగా అర్ధం చేసుకోవడంలో మేము బాధ్యులు కేము.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->