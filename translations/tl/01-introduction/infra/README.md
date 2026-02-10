# Azure Infrastructure para sa LangChain4j Pagsisimula

## Talaan ng Nilalaman

- [Mga Kinakailangan](../../../../01-introduction/infra)
- [Arkitektura](../../../../01-introduction/infra)
- [Mga Nilikhang Resources](../../../../01-introduction/infra)
- [Mabilis na Simula](../../../../01-introduction/infra)
- [Konfigurasyon](../../../../01-introduction/infra)
- [Mga Utos sa Pamamahala](../../../../01-introduction/infra)
- [Pag-optimize ng Gastos](../../../../01-introduction/infra)
- [Pagmamanman](../../../../01-introduction/infra)
- [Pag-troubleshoot](../../../../01-introduction/infra)
- [Pag-update ng Infrastructure](../../../../01-introduction/infra)
- [Paglilinis](../../../../01-introduction/infra)
- [Estruktura ng File](../../../../01-introduction/infra)
- [Mga Rekomendasyon sa Seguridad](../../../../01-introduction/infra)
- [Karagdagang Resources](../../../../01-introduction/infra)

Ang direktoryong ito ay naglalaman ng Azure infrastructure bilang code (IaC) gamit ang Bicep at Azure Developer CLI (azd) para sa pag-deploy ng Azure OpenAI resources.

## Mga Kinakailangan

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (bersyon 2.50.0 o mas bago)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (bersyon 1.5.0 o mas bago)
- Isang Azure subscription na may permiso na gumawa ng mga resources

## Arkitektura

**Pinasimpleng Setup para sa Lokal na Pag-develop** - I-deploy lang ang Azure OpenAI, patakbuhin lahat ng apps nang lokal sa iyong makina.

Ang infrastructure ay nagde-deploy ng mga sumusunod na Azure resources:

### AI Services
- **Azure OpenAI**: Cognitive Services na may dalawang model deployment:
  - **gpt-5.2**: Chat completion model (20K TPM kapasidad)
  - **text-embedding-3-small**: Embedding model para sa RAG (20K TPM kapasidad)

### Lokal na Pag-develop
Lahat ng Spring Boot na aplikasyon ay tumatakbo nang lokal sa iyong makina:
- 01-introduction (port 8080)
- 02-prompt-engineering (port 8083)
- 03-rag (port 8081)
- 04-tools (port 8084)

## Mga Nilikhang Resources

| Uri ng Resource | Pattern ng Pangalan ng Resource | Layunin |
|--------------|----------------------|---------|
| Resource Group | `rg-{environmentName}` | Naglalaman ng lahat ng resources |
| Azure OpenAI | `aoai-{resourceToken}` | Pagho-host ng AI model |

> **Tandaan:** `{resourceToken}` ay isang natatanging string na ginawa mula sa subscription ID, pangalan ng kapaligiran, at lokasyon

## Mabilis na Simula

### 1. I-deploy ang Azure OpenAI

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

Kapag na-prompt:
- Piliin ang iyong Azure subscription
- Pumili ng lokasyon (inirerekomenda: `eastus2` para sa availability ng GPT-5.2)
- Kumpirmahin ang pangalan ng kapaligiran (default: `langchain4j-dev`)

Ito ay lilikha ng:
- Azure OpenAI resource na may GPT-5.2 at text-embedding-3-small
- Ipinalalabas ang detalye ng koneksyon

### 2. Kunin ang Detalye ng Koneksyon

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Ito ay nagpapakita ng:
- `AZURE_OPENAI_ENDPOINT`: Ang iyong Azure OpenAI endpoint URL
- `AZURE_OPENAI_KEY`: API key para sa authentication
- `AZURE_OPENAI_DEPLOYMENT`: Pangalan ng chat model (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Pangalan ng embedding model

### 3. Patakbuhin ang Mga Aplikasyon Nang Lokal

Ang utos na `azd up` ay awtomatikong gumagawa ng `.env` file sa root directory na may lahat ng kinakailangang environment variables.

**Inirerekomenda:** Simulan ang lahat ng web applications:

**Bash:**
```bash
# Mula sa ugat na direktoryo
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# Mula sa ugat na direktoryo
cd ../..
.\start-all.ps1
```

O simulan ang isang module lamang:

**Bash:**
```bash
# Halimbawa: Simulan lamang ang module ng pagpapakilala
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Halimbawa: Simulan lamang ang module ng introduksyon
cd ../01-introduction
.\start.ps1
```

Parehong scripts ay awtomatikong naglo-load ng environment variables mula sa root `.env` file na nilikha ng `azd up`.

## Konfigurasyon

### Pag-customize ng Model Deployments

Para baguhin ang model deployments, i-edit ang `infra/main.bicep` at baguhin ang parameter na `openAiDeployments`:

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

Mga available na modelo at bersyon: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Pagpapalit ng Azure Regions

Para mag-deploy sa ibang rehiyon, i-edit ang `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Suriin ang availability ng GPT-5.2: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Para i-update ang infrastructure pagkatapos gumawa ng mga pagbabago sa mga Bicep files:

**Bash:**
```bash
# Muling buuin ang ARM template
az bicep build --file infra/main.bicep

# Tingnan ang mga pagbabago
azd provision --preview

# Ipatupad ang mga pagbabago
azd provision
```

**PowerShell:**
```powershell
# Muling buuin ang ARM template
az bicep build --file infra/main.bicep

# Tingnan ang mga pagbabago
azd provision --preview

# Ilapat ang mga pagbabago
azd provision
```

## Paglilinis

Para burahin lahat ng resources:

**Bash:**
```bash
# Burahin ang lahat ng mga resources
azd down

# Burahin ang lahat kasama na ang kapaligiran
azd down --purge
```

**PowerShell:**
```powershell
# Tanggalin lahat ng mga mapagkukunan
azd down

# Tanggalin lahat kasama ang kapaligiran
azd down --purge
```

**Babala**: Permanenteng mabubura lahat ng Azure resources.

## Estruktura ng File

## Pag-optimize ng Gastos

### Development/Pagsusuri
Para sa dev/test na mga kapaligiran, maaari mong bawasan ang gastos:
- Gamitin ang Standard tier (S0) para sa Azure OpenAI
- Itakda ang mas mababang kapasidad (10K TPM sa halip na 20K) sa `infra/core/ai/cognitiveservices.bicep`
- Burahin ang mga resources kapag hindi ginagamit: `azd down`

### Produksyon
Para sa produksyon:
- Taasan ang OpenAI capacity batay sa paggamit (50K+ TPM)
- Paganahin ang zone redundancy para sa mas mataas na availability
- Magpatupad ng tamang pagmamanman at alerts sa gastos

### Pagtataya ng Gastos
- Azure OpenAI: Bayad kada token (input + output)
- GPT-5.2: Humigit-kumulang $3-5 kada 1M tokens (tingnan ang kasalukuyang presyo)
- text-embedding-3-small: Humigit-kumulang $0.02 kada 1M tokens

Presyo calculator: https://azure.microsoft.com/pricing/calculator/

## Pagmamanman

### Tingnan ang Microsoft Azure OpenAI Metrics

Pumunta sa Azure Portal → Ang iyong OpenAI resource → Metrics:
- Token-Based Utilization
- HTTP Request Rate
- Time To Response
- Active Tokens

## Pag-troubleshoot

### Isyu: Conflict sa Subdomain Name ng Azure OpenAI

**Error Message:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Dahilan:**
Ang subdomain name na ginawa mula sa iyong subscription/environment ay ginagamit na, posibleng mula sa isang naunang deployment na hindi ganap na na-delete.

**Solusyon:**
1. **Opsyon 1 - Gumamit ng ibang pangalan ng environment:**
   
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

2. **Opsyon 2 - Manwal na deployment gamit ang Azure Portal:**
   - Pumunta sa Azure Portal → Gumawa ng resource → Azure OpenAI
   - Pumili ng natatanging pangalan para sa iyong resource
   - I-deploy ang mga sumusunod na modelo:
     - **GPT-5.2**
     - **text-embedding-3-small** (para sa mga RAG modules)
   - **Mahalaga:** Tandaan ang mga pangalan ng iyong deployment - dapat tugma ito sa konfigurasyon ng `.env`
   - Pagkatapos ng deployment, kunin ang endpoint at API key mula sa "Keys and Endpoint"
   - Gumawa ng `.env` file sa root ng proyekto na may:

     **Halimbawa ng `.env` file:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Mga Patnubay sa Pagpangalan ng Model Deployment:**
- Gumamit ng simpleng, pare-parehong pangalan: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Dapat eksaktong tugma ang deployment names sa iyong `.env` configuration
- Karaniwang pagkakamali: Gumawa ng model na may isang pangalan ngunit ginagamit ang ibang pangalan sa code

### Isyu: GPT-5.2 hindi available sa napiling rehiyon

**Solusyon:**
- Pumili ng isang rehiyon na may GPT-5.2 access (hal. eastus2)
- Suriin ang availability: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Isyu: Hindi sapat ang quota para sa deployment

**Solusyon:**
1. Humiling ng quota increase sa Azure Portal
2. O gamitin ang mas mababang kapasidad sa `main.bicep` (halimbawa: capacity: 10)

### Isyu: "Resource not found" kapag tumatakbo nang lokal

**Solusyon:**
1. Suriin ang deployment: `azd env get-values`
2. Siguraduhin tamang endpoint at key
3. Tiyakin na umiiral ang resource group sa Azure Portal

### Isyu: Nabigong Authentication

**Solusyon:**
- Siguraduhing tama ang `AZURE_OPENAI_API_KEY`
- Ang format ng key ay dapat 32-character hexadecimal string
- Kumuha ng bagong key mula sa Azure Portal kung kinakailangan

### Nabigong Deployment

**Isyu**: Nabibigo ang `azd provision` dahil sa quota o capacity errors

**Solusyon**: 
1. Subukan ang ibang rehiyon - Tingnan ang seksyong [Pagpapalit ng Azure Regions](../../../../01-introduction/infra) para sa paraan ng pag-configure ng mga rehiyon
2. Siguraduhing may Azure OpenAI quota ang iyong subscription:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Hindi Nakakakonekta ang Aplikasyon

**Isyu**: Nagpapakita ng connection errors ang Java application

**Solusyon**:
1. Siguraduhing na-export ang mga environment variables:
   
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

2. Suriin ang tamang format ng endpoint (dapat https://xxx.openai.azure.com)
3. I-verify na ang API key ay pangunahing o pangalawang key mula sa Azure Portal

**Isyu**: 401 Unauthorized mula sa Azure OpenAI

**Solusyon**:
1. Kumuha ng bagong API key mula sa Azure Portal → Keys and Endpoint
2. I-re-export ang `AZURE_OPENAI_API_KEY` environment variable
3. Tiyaking kumpleto ang mga deployment ng modelo (suriin sa Azure Portal)

### Mga Isyu sa Pagganap

**Isyu**: Mabagal ang mga tugon

**Solusyon**:
1. Suriin ang paggamit ng OpenAI tokens at throttling sa Azure Portal metrics
2. Taasan ang TPM capacity kung naabot ang limitasyon
3. Isaalang-alang ang paggamit ng mas mataas na antas ng reasoning-effort (mababa/katamtaman/mataas)

## Pag-update ng Infrastructure

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

## Mga Rekomendasyon sa Seguridad

1. **Huwag kailanman i-commit ang API keys** - Gumamit ng environment variables
2. **Gumamit ng .env files nang lokal** - Idagdag ang `.env` sa `.gitignore`
3. **Palitan ang mga keys nang regular** - Gumawa ng bagong keys sa Azure Portal
4. **Limitahan ang access** - Gamitin ang Azure RBAC para kontrolin kung sino ang may access sa resources
5. **I-monitor ang paggamit** - Mag-set up ng cost alerts sa Azure Portal

## Karagdagang Resources

- [Dokumentasyon ng Azure OpenAI Service](https://learn.microsoft.com/azure/ai-services/openai/)
- [Dokumentasyon ng GPT-5.2 Model](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Dokumentasyon ng Azure Developer CLI](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Dokumentasyon ng Bicep](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI Opisyal na Integrasyon](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Suporta

Para sa mga isyu:
1. Suriin ang [seksyon ng troubleshooting](../../../../01-introduction/infra) sa itaas
2. Tingnan ang kalusugan ng Azure OpenAI service sa Azure Portal
3. Magbukas ng isyu sa repository

## Lisensya

Tingnan ang root [LICENSE](../../../../LICENSE) file para sa mga detalye.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Paunawa**:  
Ang dokumentong ito ay isinalin gamit ang AI translation service na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagamat nagsusumikap kami para sa katumpakan, pakatandaan na ang mga awtomatikong pagsasalin ay maaaring magkaroon ng mga pagkakamali o maling pagkaunawa. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na opisyal na sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasaling-tao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na maaaring magmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->