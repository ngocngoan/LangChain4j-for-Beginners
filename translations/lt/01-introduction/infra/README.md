# Azure infrastruktūra LangChain4j pradžiai

## Turinys

- [Reikalavimai](../../../../01-introduction/infra)
- [Architektūra](../../../../01-introduction/infra)
- [Sukurti resursai](../../../../01-introduction/infra)
- [Greitas pradėjimas](../../../../01-introduction/infra)
- [Konfigūracija](../../../../01-introduction/infra)
- [Valdymo komandos](../../../../01-introduction/infra)
- [Kainų optimizavimas](../../../../01-introduction/infra)
- [Stebėjimas](../../../../01-introduction/infra)
- [Probleminių situacijų sprendimas](../../../../01-introduction/infra)
- [Infrastruktūros atnaujinimas](../../../../01-introduction/infra)
- [Išvalymas](../../../../01-introduction/infra)
- [Failų struktūra](../../../../01-introduction/infra)
- [Saugumo rekomendacijos](../../../../01-introduction/infra)
- [Papildomi ištekliai](../../../../01-introduction/infra)

Šiame kataloge yra Azure infrastruktūra kaip kodas (IaC), naudojant Bicep ir Azure Developer CLI (azd) Azure OpenAI išteklių diegimui.

## Reikalavimai

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (2.50.0 arba naujesnė versija)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (1.5.0 arba naujesnė versija)
- Azure prenumerata su teisėmis kurti išteklius

## Architektūra

**Supaprastintas vietinio vystymo nustatymas** – diegti tik Azure OpenAI, visas programas paleisti vietoje.

Ši infrastruktūra diegia šiuos Azure išteklius:

### AI paslaugos
- **Azure OpenAI**: Kognityvinės paslaugos su dviem modelių diegimais:
  - **gpt-5.2**: Pokalbių užbaigimo modelis (20K TPM talpa)
  - **text-embedding-3-small**: Įterpimo modelis RAG (20K TPM talpa)

### Vietinis vystymas
Visos Spring Boot programos veikia jūsų kompiuteryje:
- 01-introduction (prievadas 8080)
- 02-prompt-engineering (prievadas 8083)
- 03-rag (prievadas 8081)
- 04-tools (prievadas 8084)

## Sukurti resursai

| Resurso tipas | Resurso pavadinimo šablonas | Paskirtis |
|--------------|-----------------------------|-----------|
| Resursų grupė | `rg-{environmentName}` | Laiko visus išteklius |
| Azure OpenAI | `aoai-{resourceToken}` | AI modelio talpinimas |

> **Pastaba:** `{resourceToken}` yra unikalus tekstas, sugeneruotas iš prenumeratos ID, aplinkos pavadinimo ir vietos

## Greitas pradėjimas

### 1. Diegti Azure OpenAI

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
  
Kai būsite paprašyti:
- Pasirinkite Azure prenumeratą
- Pasirinkite vietą (rekomenduojama: `eastus2` dėl GPT-5.2 pasiekiamumo)
- Patvirtinkite aplinkos pavadinimą (numatytasis: `langchain4j-dev`)

Tai sukurs:
- Azure OpenAI išteklių su GPT-5.2 ir text-embedding-3-small
- Išves prisijungimo duomenis

### 2. Gauti prisijungimo duomenis

**Bash:**
```bash
azd env get-values
```
  
**PowerShell:**
```powershell
azd env get-values
```
  
Bus parodyta:
- `AZURE_OPENAI_ENDPOINT`: Jūsų Azure OpenAI galinis taškas URL
- `AZURE_OPENAI_KEY`: API raktas autentifikavimui
- `AZURE_OPENAI_DEPLOYMENT`: Pokalbių modelio pavadinimas (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Įterpimo modelio pavadinimas

### 3. Paleisti programas vietoje

Komanda `azd up` automatiškai sukuria `.env` failą šakniniame kataloge su visais reikalingais aplinkos kintamaisiais.

**Rekomenduojama:** Paleiskite visas internetines programas:

**Bash:**
```bash
# Iš pagrindinio katalogo
cd ../..
./start-all.sh
```
  
**PowerShell:**
```powershell
# Iš šakninių katalogų
cd ../..
.\start-all.ps1
```
  
Arba paleiskite vieną modulį:

**Bash:**
```bash
# Pavyzdys: Paleiskite tik įvadinį modulį
cd ../01-introduction
./start.sh
```
  
**PowerShell:**
```powershell
# Pavyzdys: Pradėkite tik įvadinį modulį
cd ../01-introduction
.\start.ps1
```
  
Abu scenarijai automatiškai įkelia aplinkos kintamuosius iš šakniniame kataloge esančio `.env` failo, sukuriamo `azd up`.

## Konfigūracija

### Modelių diegimų pritaikymas

Norėdami pakeisti modelių diegimus, redaguokite `infra/main.bicep` ir pakeiskite parametrą `openAiDeployments`:

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
  
Galimi modeliai ir versijos: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Azure regionų keitimas

Kad diegtumėte kitame regione, redaguokite `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```
  
Patikrinkite GPT-5.2 prieinamumą: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Norėdami atnaujinti infrastruktūrą po Bicep failų pakeitimų:

**Bash:**
```bash
# Perstatyti ARM šabloną
az bicep build --file infra/main.bicep

# Peržiūrėti pakeitimus
azd provision --preview

# Pritaikyti pakeitimus
azd provision
```
  
**PowerShell:**
```powershell
# Perstatyti ARM šabloną
az bicep build --file infra/main.bicep

# Peržiūrėti pakeitimus
azd provision --preview

# Pritaikyti pakeitimus
azd provision
```
  
## Išvalymas

Norėdami ištrinti visus išteklius:

**Bash:**
```bash
# Ištrinti visus išteklius
azd down

# Ištrinti viską įskaitant aplinką
azd down --purge
```
  
**PowerShell:**
```powershell
# Ištrinti visus išteklius
azd down

# Ištrinti viską, įskaitant aplinką
azd down --purge
```
  
**Įspėjimas**: Tai visam laikui ištrins visus Azure išteklius.

## Failų struktūra

## Kainų optimizavimas

### Vystymas/testavimas  
Vystymo/testavimo aplinkose galite sumažinti kaštus:
- Naudokite Standard sluoksnį (S0) Azure OpenAI
- Nustatykite mažesnę talpą (10K TPM vietoje 20K) faile `infra/core/ai/cognitiveservices.bicep`
- Ištrinkite išteklius, kai nenaudojate: `azd down`

### Gamyba  
Gamybai:
- Padidinkite OpenAI talpą pagal naudojimą (50K+ TPM)
- Įjunkite zoninį atsparumą didesniam prieinamumui
- Įdiekite tinkamą stebėjimą ir kaštų įspėjimus

### Kaštų įvertinimas
- Azure OpenAI: moka už tokeną (įvestį + išvestį)
- GPT-5.2: apie 3–5 USD už 1 milijoną tokenų (žiūrėkite dabartinius kainoraščius)
- text-embedding-3-small: apie 0,02 USD už 1 milijoną tokenų

Kainų skaičiuoklė: https://azure.microsoft.com/pricing/calculator/

## Stebėjimas

### Peržiūrėti Azure OpenAI metrikas

Eikite į Azure Portal → Jūsų OpenAI išteklius → Metrikos:
- Pagal tokenus naudojimas
- HTTP užklausų kiekis
- Atsakymo laikas
- Aktyvūs tokenai

## Probleminių situacijų sprendimas

### Problema: Azure OpenAI potinklio vardų konfliktas

**Klaidos pranešimas:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```
  
**Priežastis:**  
Potinklio vardas, sugeneruotas iš jūsų prenumeratos/aplinkos, jau naudojamas, galbūt dėl ankstesnio nevisiško diegimo ištrynimo.

**Sprendimas:**  
1. **1 variantas – Naudokite kitą aplinkos pavadinimą:**
   
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
  
2. **2 variantas – Rankinis diegimas per Azure Portal:**
   - Eikite į Azure Portal → Kurti išteklių → Azure OpenAI
   - Pasirinkite unikalų ištekliaus pavadinimą
   - Įdiekite šiuos modelius:
     - **GPT-5.2**
     - **text-embedding-3-small** (RAG moduliams)
   - **Svarbu:** Užsirašykite diegimo pavadinimus – jie turi sutapti su `.env` konfigūracija
   - Po diegimo gaukite galinį tašką ir API raktą skiltyje "Keys and Endpoint"
   - Sukurkite `.env` failą projekto šaknyje su:

     **Pavyzdinis `.env` failas:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```
  
**Modelių diegimo pavadinimų gairės:**  
- Naudokite paprastus, nuoseklius pavadinimus: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`  
- Diegimų pavadinimai turi tiksliai atitikti `.env` konfigūraciją  
- Dažna klaida: sukurti modelį vienu vardu, bet kode nurodyti kitą  

### Problema: GPT-5.2 neprieinamas pasirinktoje zonoje

**Sprendimas:**  
- Pasirinkite zoną su GPT-5.2 prieiga (pvz., eastus2)  
- Patikrinkite prieinamumą: https://learn.microsoft.com/azure/ai-services/openai/concepts/models  

### Problema: Nepakanka kvotos diegimui

**Sprendimas:**  
1. Užsisakykite kvotos didinimą Azure Portale  
2. Arba naudokite mažesnę talpą `main.bicep` (pvz., capacity: 10)

### Problema: "Resource not found" paleidžiant vietoje

**Sprendimas:**  
1. Patikrinkite diegimą: `azd env get-values`  
2. Patikrinkite galinį tašką ir raktą, ar teisingi  
3. Įsitikinkite, kad resursų grupė egzistuoja Azure Portale

### Problema: Autentifikacija nepavyko

**Sprendimas:**  
- Patikrinkite, ar `AZURE_OPENAI_API_KEY` nustatytas tinkamai  
- Rakto formatas turi būti 32 simbolių šešioliktainė eilutė  
- Gavę naują raktą Azure Portale, atnaujinkite

### Diegimo klaidos

**Problema:** `azd provision` nepavyksta dėl kvotų ar talpos klaidų

**Sprendimas:**  
1. Išbandykite kitą regioną – žr. skyrių [Changing Azure Regions](../../../../01-introduction/infra)  
2. Patikrinkite, ar jūsų prenumeratoje yra Azure OpenAI kvota:  
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```
  
### Programa neprisijungia

**Problema:** Java programa rodo prisijungimo klaidas

**Sprendimas:**  
1. Patikrinkite, ar aplinkos kintamieji yra eksportuoti:  
   
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
  
2. Patikrinkite, ar galinis taškas tinkamo formato (`https://xxx.openai.azure.com`)  
3. Patikrinkite, ar API raktas yra pagrindinis arba antrinis raktas iš Azure Portalo

**Problema:** 401 Unauthorized iš Azure OpenAI

**Sprendimas:**  
1. Gaukite naują API raktą iš Azure Portalo → Keys and Endpoint  
2. Pakartotinai eksportuokite `AZURE_OPENAI_API_KEY` aplinkos kintamąjį  
3. Užtikrinkite, kad modelių diegimai baigti (patikrinkite Azure Portal)

### Veikimo problemos

**Problema:** Lėtas atsakymų laikas

**Sprendimas:**  
1. Patikrinkite OpenAI tokenų naudojimą ir apribojimus Azure Portal metrikose  
2. Padidinkite TPM talpą, jei pasiekiate ribas  
3. Apsvarstykite aukštesnį sprendimų lygį (žemas/vidutinis/aukštas)

## Infrastruktūros atnaujinimas

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
  
## Saugumo rekomendacijos

1. **Niekada neįtraukite API rakto į kodą** – naudokite aplinkos kintamuosius  
2. **Vietoje naudokite `.env` failus** – pridėkite `.env` į `.gitignore`  
3. **Reguliariai keiskite raktus** – generuokite naujus Azure Portale  
4. **Ribokite prieigą** – naudokite Azure RBAC prieigų valdymui  
5. **Stebėkite naudojimą** – nustatykite kaštų įspėjimus Azure Portale

## Papildomi ištekliai

- [Azure OpenAI paslaugos dokumentacija](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 modelio dokumentacija](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI dokumentacija](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep dokumentacija](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j oficialus OpenAI integravimas](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Pagalba

Dėl problemų:  
1. Peržvelkite [problemų sprendimo skyrių](../../../../01-introduction/infra) aukščiau  
2. Patikrinkite Azure OpenAI paslaugos būklę Azure Portale  
3. Atidarykite klausimą GitHub repozitorijoje

## Licenzija

Daugiau informacijos rasite šakniniame [LICENSE](../../../../LICENSE) faile.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:  
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, prašome atkreipti dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas jo gimtąja kalba turėtų būti laikomas autoritetingu šaltiniu. Svarbiai informacijai rekomenduojamas profesionalus žmogaus vertimas. Mes neatsakome už jokius nesusipratimus ar klaidingą interpretaciją, kylančią dėl šio vertimo naudojimo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->