# Azure infrastruktuur LangChain4j jaoks - alustamine

## Sisukord

- [Eeltingimused](../../../../01-introduction/infra)
- [Arhitektuur](../../../../01-introduction/infra)
- [Loodud ressursid](../../../../01-introduction/infra)
- [Kiire algus](../../../../01-introduction/infra)
- [Konfiguratsioon](../../../../01-introduction/infra)
- [Haldusekäsud](../../../../01-introduction/infra)
- [Kuluoptimeerimine](../../../../01-introduction/infra)
- [Jälgimine](../../../../01-introduction/infra)
- [Tõrkeotsing](../../../../01-introduction/infra)
- [Infrastruktuuri uuendamine](../../../../01-introduction/infra)
- [Puhastamine](../../../../01-introduction/infra)
- [Failistruktuur](../../../../01-introduction/infra)
- [Turvalisuse soovitused](../../../../01-introduction/infra)
- [Lisamaterjalid](../../../../01-introduction/infra)

See kataloog sisaldab Azure infrastruktuuri koodina (IaC) kasutades Bicep ja Azure Developer CLI (azd) Azure OpenAI ressursside juurutamiseks.

## Eeltingimused

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (versioon 2.50.0 või uuem)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (versioon 1.5.0 või uuem)
- Azure tellimus, millel on õigused ressursside loomisel

## Arhitektuur

**Lihtsustatud lokaalse arenduse seadistus** – juurutatakse ainult Azure OpenAI, kõik rakendused jooksevad lokaalsetena.

Infrastruktuur juurutab järgmised Azure ressursid:

### Tehisintellekti teenused
- **Azure OpenAI**: kognitiivsed teenused kahe mudelijuhtimisega:
  - **gpt-5.2**: vestluse lõpetamise mudel (20K TPM võimsus)
  - **text-embedding-3-small**: manustamismudel RAG jaoks (20K TPM võimsus)

### Kohalik arendus
Kõik Spring Boot rakendused jooksutatakse lokaalselt sinu masina peal:
- 01-introduction (port 8080)
- 02-prompt-engineering (port 8083)
- 03-rag (port 8081)
- 04-tools (port 8084)

## Loodud ressursid

| Ressursitüüp | Ressursi nime mustrit | Eesmärk |
|--------------|----------------------|---------|
| Ressursigrupp | `rg-{environmentName}` | Sisaldab kõiki ressursse |
| Azure OpenAI | `aoai-{resourceToken}` | AI mudeli majutamine |

> **Märkus:** `{resourceToken}` on unikaalne string, mis genereeritakse tellimuse ID, keskkonna nime ja asukoha põhjal

## Kiire algus

### 1. Juuruta Azure OpenAI

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

Kui küsitakse:
- Vali oma Azure tellimus
- Vali asukoht (soovitatav: `eastus2` GPT-5.2 saadavuse tõttu)
- Kinnita keskkonna nimi (vaikimisi: `langchain4j-dev`)

See loob:
- Azure OpenAI ressursi koos GPT-5.2 ja text-embedding-3-small mudelitega
- Kuvab ühenduse andmed

### 2. Hangi ühenduse andmed

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Kuvatakse:
- `AZURE_OPENAI_ENDPOINT`: sinu Azure OpenAI lõpp-punkti URL
- `AZURE_OPENAI_KEY`: autentimisklahv
- `AZURE_OPENAI_DEPLOYMENT`: vestlusmudeli nimi (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: manustamismudeli nimi

### 3. Käivita rakendused lokaalselt

Käsk `azd up` loob automaatselt juurkausta `.env` faili kõigi vajalike keskkonnamuutujatega.

**Soovitus:** Käivita kõik veebirakendused:

**Bash:**
```bash
# Juure kataloogist
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# Juurekataloogist
cd ../..
.\start-all.ps1
```

Või käivita üks moodul:

**Bash:**
```bash
# Näide: Käivita ainult sissejuhatuse moodul
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Näide: Käivita ainult sissejuhatuse moodul
cd ../01-introduction
.\start.ps1
```

Mõlemad skriptid laadivad automaatselt keskkonnamuutujad juurkaustas olevast `.env` failist, mille lõi `azd up`.

## Konfiguratsioon

### Mudelijuhtimiste kohandamine

Mudelite juurutamiseks muutmiseks redigeeri faili `infra/main.bicep` ja muuda parameetrit `openAiDeployments`:

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

Saadaval mudelite ja versioonide kohta: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Azure regioonide muutmine

Kui soovid juurutada teises regioonis, muuda `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Kontrolli GPT-5.2 saadavust: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Infrastruktuuri uuendamiseks pärast Bicep failide muutmist:

**Bash:**
```bash
# Ehita ARM-mall uuesti
az bicep build --file infra/main.bicep

# Muudatuste eelvaade
azd provision --preview

# Muudatuste rakendamine
azd provision
```

**PowerShell:**
```powershell
# Koosta ARM mall uuesti
az bicep build --file infra/main.bicep

# Muutuste eelvaade
azd provision --preview

# Rakenda muudatused
azd provision
```

## Puhastamine

Kõikide ressursside kustutamiseks:

**Bash:**
```bash
# Kustuta kõik ressursid
azd down

# Kustuta kõik, kaasa arvatud keskkond
azd down --purge
```

**PowerShell:**
```powershell
# Kustuta kõik ressursid
azd down

# Kustuta kõik, kaasa arvatud keskkond
azd down --purge
```

**Hoiatus**: See kustutab jäädavalt kõik Azure ressursid.

## Failistruktuur

## Kuluoptimeerimine

### Arendus/testimine
Arendus- või testkeskkondade jaoks saad kulusid vähendada:
- Kasuta Azure OpenAI puhul Standard reitingut (S0)
- Sea väiksem võimsus (10K TPM asemel 20K) failis `infra/core/ai/cognitiveservices.bicep`
- Kustuta ressursid, kui pole kasutusel: `azd down`

### Tootmine
Tootmiskeskkonnas:
- Suurenda OpenAI võimsust vastavalt kasutusele (50K+ TPM)
- Lülita sisse tsooni varukoopia suurema kättesaadavuse jaoks
- Rakenda korralik jälgimine ja kuluhoiatused

### Kulu hindamine
- Azure OpenAI: maksmine tokeni põhiselt (sisend + väljund)
- GPT-5.2: umbes 3-5 dollarit miljoni tokeni eest (kontrolli hetkehindu)
- text-embedding-3-small: umbes 0.02 dollarit miljoni tokeni eest

Hinnakalkulaator: https://azure.microsoft.com/pricing/calculator/

## Jälgimine

### Vaata Azure OpenAI mõõdikuid

Mine Azure Portaal → oma OpenAI ressursile → Mõõdikud:
- Tokenite kasutus
- HTTP päringute sagedus
- Vastuse aeg
- Aktiivsete tokenite arv

## Tõrkeotsing

### Probleem: Azure OpenAI alamdomeeni nime konflikt

**Veateade:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Põhjus:**
Tellimusest/keskkonnast genereeritud alamdomeeni nimi on juba kasutusel, tõenäoliselt eelmise juurutuse tõttu, mis pole täielikult kustutatud.

**Lahendus:**
1. **Valik 1 – Kasuta teist keskkonna nime:**

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

2. **Valik 2 – Käsitsi juurutamine Azure Portaalis:**
   - Mine Azure Portaal → Loo ressurss → Azure OpenAI
   - Vali ressursile unikaalne nimi
   - Juuruta järgmised mudelid:
     - **GPT-5.2**
     - **text-embedding-3-small** (RAG moodulite jaoks)
   - **Tähtis:** märgi üles oma juurutuste nimed – need peavad ühtima `.env` konfiguratsiooniga
   - Pärast juurutamist saa oma lõpp-punkt ja API võti "Keys and Endpoint" alt
   - Loo projekti juurkausta `.env` fail järgmise sisuga:

**Näidise `.env` fail:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Mudelijuurutuse nimede juhised:**
- Kasuta lihtsaid, ühtlaseid nimesid: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Juurutuste nimed peavad täpselt ühtima `.env` failis
- Sageli esinev viga: mudeli loomine ühe nimega, aga koodis viidatakse teise nimega

### Probleem: GPT-5.2 ei ole valitud regioonis saadaval

**Lahendus:**
- Vali regioon, kus GPT-5.2 on saadaval (nt eastus2)
- Kontrolli saadavust: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Probleem: Juurutuseks napp kvantiteet

**Lahendus:**
1. Taotle kvantiteedi suurendust Azure Portaalis
2. Või kasuta väiksemat võimsust `main.bicep` failis (nt capacity: 10)

### Probleem: „Resource not found“ lokaalselt töötades

**Lahendus:**
1. Kontrolli juurutust: `azd env get-values`
2. Veendu, et lõpp-punkt ja võti on korrektsed
3. Kontrolli, et ressursigrupp on Azure Portaalis olemas

### Probleem: Autentimine ebaõnnestus

**Lahendus:**
- Veendu, et `AZURE_OPENAI_API_KEY` on õigesti määratud
- Võtme formaat peaks olema 32-kohaline heksadesimaalne string
- Hangi uus võti Azure Portaalist, kui vaja

### Juurutamine ebaõnnestub

**Probleem**: `azd provision` ebaõnnestub kvantiteedi või võimsuse vigade tõttu

**Lahendus**: 
1. Proovi teist regiooni – vt jaotist [Changing Azure Regions](../../../../01-introduction/infra) piirkondade muutmiseks
2. Kontrolli, kas sinu tellimusel on Azure OpenAI kvantiteet:

**Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
**PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Rakendus ei ühendu

**Probleem**: Java rakendus annab ühendusvead

**Lahendus**:
1. Veendu, et keskkonnamuutujad on eksporditud:

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

2. Kontrolli, et lõpp-punkti formaat on korrektne (peab olema `https://xxx.openai.azure.com`)
3. Veendu, et API võti on Azure Portaalist saadud primaar- või sekundaarvõti

**Probleem**: 401 Unauthorized Azure OpenAI juures

**Lahendus**:
1. Hangi uus API võti Azure Portaalist → Keys and Endpoint
2. Paku uuesti `AZURE_OPENAI_API_KEY` keskkonnamuutuja
3. Veendu, et mudelid on juurutatud (vaata Azure Portaalist)

### Jõudlusprobleemid

**Probleem**: Vastusajad on aeglased

**Lahendus**:
1. Kontrolli OpenAI tokenite kasutust ja piiranguid Azure Portaalis mõõdikutest
2. Suurenda TPM võimsust, kui jõuad limiitideni
3. Kaalu suurema loogilise tasemega (madal/keskmine/kõrge) kasutamist

## Infrastruktuuri uuendamine

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

## Turvalisuse soovitused

1. **Ära kunagi salvesta API võtmeid koodi** – kasuta keskkonnamuutujaid
2. **Kasuta lokaalselt .env faile** – lisa `.env` `.gitignore` faili
3. **Vaheta võtmeid regulaarselt** – genereeri uued võtmed Azure Portaalis
4. **Piira ligipääsu** – kasuta Azure RBAC-i, et kontrollida, kes pääseb ressurssidele ligi
5. **Jälgi kasutust** – sea Azure Portaalis üles kuluhinnangud ja hoiatused

## Lisamaterjalid

- [Azure OpenAI teenuse dokumentatsioon](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 mudeli dokumentatsioon](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI dokumentatsioon](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep dokumentatsioon](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI ametlik integratsioon](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Tugi

Probleemide korral:
1. Vaata [tõrkeotsingu jaotist](../../../../01-introduction/infra) eespool
2. Kontrolli Azure OpenAI teenuse olekut Azure Portaalis
3. Ava probleemileht selles repos

## Litsents

Vaata juuras asuvat [LICENSE](../../../../LICENSE) faili detailide jaoks.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:
See dokument on tõlgitud kasutades tehisintellekti tõlketeenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi püüame täpsust, palun arvestage, et automatiseeritud tõlked võivad sisaldada vigu või ebatäpsusi. Dokument algkeeles tuleks pidada autoriteetseks allikaks. Olulise teabe puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta selle tõlke kasutamisest tingitud väärarusaamade või valesti mõistmiste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->