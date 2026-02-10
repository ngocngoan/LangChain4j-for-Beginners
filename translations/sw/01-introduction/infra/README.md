# Miundombinu ya Azure kwa LangChain4j Kuanzia

## Jedwali la Maudhui

- [Mahitaji ya Awali](../../../../01-introduction/infra)
- [Miundo](../../../../01-introduction/infra)
- [Rasilimali Zilizotengenezwa](../../../../01-introduction/infra)
- [Kuanzia Haraka](../../../../01-introduction/infra)
- [Marekebisho](../../../../01-introduction/infra)
- [Amri za Usimamizi](../../../../01-introduction/infra)
- [Uboreshaji wa Gharama](../../../../01-introduction/infra)
- [Ufuatiliaji](../../../../01-introduction/infra)
- [Utatuzi wa Matatizo](../../../../01-introduction/infra)
- [Kusasisha Miundombinu](../../../../01-introduction/infra)
- [Kusafisha](../../../../01-introduction/infra)
- [Muundo wa Faili](../../../../01-introduction/infra)
- [Mapendekezo ya Usalama](../../../../01-introduction/infra)
- [Rasilimali Zaidi](../../../../01-introduction/infra)

Folda hii ina miundombinu ya Azure kama msimbo (IaC) kwa kutumia Bicep na Azure Developer CLI (azd) kwa ajili ya kupeleka rasilimali za Azure OpenAI.

## Mahitaji ya Awali

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (toleo 2.50.0 au baadaye)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (toleo 1.5.0 au baadaye)
- Usajili wa Azure wenye ruhusa za kuunda rasilimali

## Miundo

**Mpangilio Rahisi wa Maendeleo ya Kwenye Mtaa** - Tumia Azure OpenAI tu, endesha programu zote kwa ndani kwenye mashine yako.

Miundombinu hupeleka rasilimali zifuatazo za Azure:

### Huduma za AI
- **Azure OpenAI**: Huduma za Cognitive na utekelezaji wa model mbili:
  - **gpt-5.2**: Modeli ya mazungumzo ya kukamilisha (uwezo wa TPM 20K)
  - **text-embedding-3-small**: Modeli ya kuingiza kwa RAG (uwezo wa TPM 20K)

### Maendeleo ya Kwenye Mtaa
Programu zote za Spring Boot zinaendesha mahali hapa kwenye mashine yako:
- 01-introduction (bandari 8080)
- 02-prompt-engineering (bandari 8083)
- 03-rag (bandari 8081)
- 04-tools (bandari 8084)

## Rasilimali Zilizotengenezwa

| Aina ya Rasilimali | Mifano ya Majina ya Rasilimali | Kusudi |
|--------------|----------------------|---------|
| Kikundi cha Rasilimali | `rg-{environmentName}` | Kina rasilimali zote |
| Azure OpenAI | `aoai-{resourceToken}` | Kukaribisha modeli ya AI |

> **Kumbuka:** `{resourceToken}` ni mfululizo wa kipekee unaotengenezwa kutoka kwa kitambulisho cha usajili, jina la mazingira, na eneo

## Kuanzia Haraka

### 1. Telekeza Azure OpenAI

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

Unapoulizwa:
- Chagua usajili wako wa Azure
- Chagua eneo (liopendekezwa: `eastus2` kwa upatikanaji wa GPT-5.2)
- Thibitisha jina la mazingira (chaguo-msingi: `langchain4j-dev`)

Hii itaunda:
- Rasilimali ya Azure OpenAI yenye GPT-5.2 na text-embedding-3-small
- Toa maelezo ya kuunganishwa

### 2. Pata Maelezo ya Kuunganishwa

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Hii itaonyesha:
- `AZURE_OPENAI_ENDPOINT`: URL ya sehemu yako ya Azure OpenAI
- `AZURE_OPENAI_KEY`: Kidhibiti cha API kwa uthibitishaji
- `AZURE_OPENAI_DEPLOYMENT`: Jina la modeli ya mazungumzo (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Jina la modeli ya kuingiza

### 3. Endesha Programu Kwenye Mtaa

Amri ya `azd up` huunda moja kwa moja faili `.env` kwenye kimo cha juu chenye mazingira yote muhimu.

**Inapendekezwa:** Anzisha programu zote za wavuti:

**Bash:**
```bash
# Kutoka kwenye saraka ya mzizi
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# Kutoka kwenye saraka kuu
cd ../..
.\start-all.ps1
```

Au anzisha moduli moja:

**Bash:**
```bash
# Mfano: Anza tu moduli ya utangulizi
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Mfano: Anza tu na moduli ya utangulizi
cd ../01-introduction
.\start.ps1
```

Skripti zote huingiza moja kwa moja mabadiliko ya mazingira kutoka kwa faili `.env` ulilotengenezwa na `azd up`.

## Marekebisho

### Kubadilisha Utekelezaji wa Modeli

Ili kubadilisha utekelezaji wa modeli, hariri `infra/main.bicep` na badilisha parameter `openAiDeployments`:

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

Modeli na toleo zinazopatikana: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Kubadilisha Eneo la Azure

Ili kupeleka kwenye eneo tofauti, hariri `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Angalia upatikanaji wa GPT-5.2: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Kusasaisha miundombinu baada ya kufanya mabadiliko kwenye faili za Bicep:

**Bash:**
```bash
# Jenga upya kiolezo cha ARM
az bicep build --file infra/main.bicep

# Angalia mabadiliko
azd provision --preview

# Tumia mabadiliko
azd provision
```

**PowerShell:**
```powershell
# Jenga upya templeti ya ARM
az bicep build --file infra/main.bicep

# Tazama mabadiliko
azd provision --preview

# Tekeleza mabadiliko
azd provision
```

## Kusafisha

Ili kufuta rasilimali zote:

**Bash:**
```bash
# Futa rasilimali zote
azd down

# Futa kila kitu ikiwa ni pamoja na mazingira
azd down --purge
```

**PowerShell:**
```powershell
# Futa rasilimali zote
azd down

# Futa kila kitu ikiwa ni pamoja na mazingira
azd down --purge
```

**Onyo**: Hii itafuta kabisa rasilimali zote za Azure.

## Muundo wa Faili

## Uboreshaji wa Gharama

### Maendeleo/Majaribio
Kwa mazingira ya maendeleo/majaribio, unaweza kupunguza gharama:
- Tumia tier ya Standard (S0) kwa Azure OpenAI
- Weka uwezo mdogo (10K TPM badala ya 20K) katika `infra/core/ai/cognitiveservices.bicep`
- Futa rasilimali ukiwa hutumii: `azd down`

### Uzalishaji
Kwa uzalishaji:
- Ongeza uwezo wa OpenAI kulingana na matumizi (50K+ TPM)
- Washa upanuzi wa mikoa kwa upatikanaji bora
- Tekeleza ufuatiliaji mzuri na tahadhari za gharama

### Makadirio ya Gharama
- Azure OpenAI: Lipa kwa tokeni (ingizo + pato)
- GPT-5.2: ~$3-5 kwa milioni 1 ya tokeni (angalia bei ya sasa)
- text-embedding-3-small: ~$0.02 kwa milioni 1 ya tokeni

Kalkuleta ya bei: https://azure.microsoft.com/pricing/calculator/

## Ufuatiliaji

### Angalia Vipimo vya Azure OpenAI

Nenda Azure Portal → Rasilimali yako ya OpenAI → Vipimo:
- Matumizi kwa tokeni
- Kiwango cha Maombi ya HTTP
- Muda wa Majibu
- Tokeni Zenye Matumizi

## Utatuzi wa Matatizo

### Tatizo: Mgongano wa jina la subdomain ya Azure OpenAI

**Ujumbe wa Hitilafu:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Sababu:**
Jina la subdomain linalotengenezwa kutoka kwa usajili/mazingira yako tayari linatumika, labda kutoka kwa utawala wa awali ambao haujakamilika kufutwa.

**Suluhisho:**
1. **Chaguo 1 - Tumia jina tofauti la mazingira:**
   
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

2. **Chaguo 2 - Upelekaji wa mkono kupitia Azure Portal:**
   - Nenda Azure Portal → Unda rasilimali → Azure OpenAI
   - Chagua jina la kipekee kwa rasilimali yako
   - Telekeza modeli zifuatazo:
     - **GPT-5.2**
     - **text-embedding-3-small** (kwa moduli za RAG)
   - **Muhimu:** Kumbuka majina ya utekelezaji - lazima yaendane na usanidi `.env`
   - Baada ya utekelezaji, pata sehemu yako na ufunguo wa API kutoka kwa "Keys and Endpoint"
   - Tengeneza faili `.env` kwenye mzizi wa mradi wenye:
     
     **Mfano wa faili `.env`:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Miongozo ya Kujina kwa Utekelezaji wa Modeli:**
- Tumia majina rahisi, ya muendelezo: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Majina ya utekelezaji lazima yaendane kabisa na yale unayoweka katika `.env`
- Makosa ya kawaida: Kuunda modeli na jina moja lakini kurejelea jina tofauti katika msimbo

### Tatizo: GPT-5.2 haipatikani katika eneo ulilochagua

**Suluhisho:**
- Chagua eneo lenye upatikanaji wa GPT-5.2 (mfano, eastus2)
- Angalia upatikanaji: https://learn.microsoft.com/azure/ai-services/openai/concepts/models



### Tatizo: Kiasi cha kutosha hakipo kwa utekelezaji

**Suluhisho:**
1. Omba ongezeko la kiasi katika Azure Portal
2. Au tumia uwezo mdogo katika `main.bicep` (mfano, uwezo: 10)

### Tatizo: "Rasilimali haikupatikana" unapoendesha mahali hapa

**Suluhisho:**
1. Hakikisha utekelezaji: `azd env get-values`
2. Angalia sehemu na ufunguo ni sahihi
3. Hakikisha kikundi cha rasilimali kiko Azure Portal

### Tatizo: Uthibitishaji umefeli

**Suluhisho:**
- Hakikisha `AZURE_OPENAI_API_KEY` imewekwa kwa usahihi
- Muundo wa ufunguo unapaswa kuwa mfuatano wa herufi 32 za hexadecimal
- Pata ufunguo mpya kutoka Azure Portal kama unahitajika

### Utekelezaji Umeshindwa

**Tatizo**: `azd provision` imeshindwa kwa sababu za kiasi au uwezo

**Suluhisho**: 
1. Jaribu eneo tofauti - Angalia sehemu [Kubadilisha Eneo la Azure](../../../../01-introduction/infra) jinsi ya kusanidi maeneo
2. Hakikisha usajili wako una kiasi cha Azure OpenAI:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Programu Haijiunganishi

**Tatizo**: Programu ya Java inaonyesha makosa ya muunganisho

**Suluhisho**:
1. Hakikisha mabadiliko ya mazingira yamehamishwa:
   
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

2. Angalia muundo wa sehemu ni sahihi (unapaswa kuwa `https://xxx.openai.azure.com`)
3. Hakikisha ufunguo wa API ni ufunguo wa kwanza au wa pili kutoka Azure Portal

**Tatizo**: 401 Unauthorized kutoka Azure OpenAI

**Suluhisho**:
1. Pata ufunguo mpya wa API kutoka Azure Portal → Keys and Endpoint
2. Hamisha upya mabadiliko ya mazingira ya `AZURE_OPENAI_API_KEY`
3. Hakikisha utekelezaji wa modeli umekamilika (angalia Azure Portal)

### Masuala ya Utendaji

**Tatizo**: Muda wa majibu ni mrefu

**Suluhisho**:
1. Angalia matumizi ya tokeni za OpenAI na vikwazo katika vipimo vya Azure Portal
2. Ongeza uwezo wa TPM kama unafikia mipaka
3. Fikiria kutumia kiwango cha juu cha jitihada za kufikiria (chini/kati/juu)

## Kusasisha Miundombinu

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

## Mapendekezo ya Usalama

1. **Huuzi ufunguo wa API** - Tumia mabadiliko ya mazingira
2. **Tumia faili `.env` kwa ndani** - Taja `.env` kwenye `.gitignore`
3. **Geuza funguo mara kwa mara** - Tengeneza funguo mpya katika Azure Portal
4. **Zuia upatikanaji usiohitajika** - Tumia Azure RBAC kudhibiti nani anaweza kufikia rasilimali
5. **Fuatilia matumizi** - Weka tahadhari za gharama katika Azure Portal

## Rasilimali Zaidi

- [Nyaraka za Huduma ya Azure OpenAI](https://learn.microsoft.com/azure/ai-services/openai/)
- [Nyaraka za Modeli ya GPT-5.2](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Nyaraka za Azure Developer CLI](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Nyaraka za Bicep](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [Uingizaji Rasmi wa LangChain4j OpenAI](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Msaada

Kwa matatizo:
1. Angalia [sehemu ya utatuzi wa matatizo](../../../../01-introduction/infra) hapo juu
2. Kagua afya ya huduma ya Azure OpenAI katika Azure Portal
3. Fungua suala kwenye hazina

## Leseni

Angalia faili la [LICENSE](../../../../LICENSE) kwenye mzizi kwa maelezo.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Kauli ya Msamaha**:
Nyaraka hii imetafsiriwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kwa usahihi, tafadhali fahamu kwamba tafsiri za kiotomatiki zinaweza kuwa na makosa au upungufu wa usahihi. Nyaraka asili katika lugha yake ya asili inapaswa kuzingatiwa kama chanzo cha mamlaka. Kwa habari muhimu, tafsiri ya kitaalamu ya binadamu inashauriwa. Hatuwezi kuwajibika kwa uelewa wa makosa au tafsiri potofu zinazotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->