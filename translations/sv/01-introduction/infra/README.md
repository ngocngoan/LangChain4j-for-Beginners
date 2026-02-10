# Azure-infrastruktur för LangChain4j Komma igång

## Innehållsförteckning

- [Förutsättningar](../../../../01-introduction/infra)
- [Arkitektur](../../../../01-introduction/infra)
- [Skapade resurser](../../../../01-introduction/infra)
- [Snabbstart](../../../../01-introduction/infra)
- [Konfiguration](../../../../01-introduction/infra)
- [Hantera kommandon](../../../../01-introduction/infra)
- [Kostnadsoptimering](../../../../01-introduction/infra)
- [Övervakning](../../../../01-introduction/infra)
- [Felsökning](../../../../01-introduction/infra)
- [Uppdatera infrastruktur](../../../../01-introduction/infra)
- [Städa upp](../../../../01-introduction/infra)
- [Filtstruktur](../../../../01-introduction/infra)
- [Säkerhetsrekommendationer](../../../../01-introduction/infra)
- [Ytterligare resurser](../../../../01-introduction/infra)

Den här katalogen innehåller Azure-infrastruktur som kod (IaC) med Bicep och Azure Developer CLI (azd) för att distribuera Azure OpenAI-resurser.

## Förutsättningar

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (version 2.50.0 eller senare)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (version 1.5.0 eller senare)
- Ett Azure-abonnemang med behörighet att skapa resurser

## Arkitektur

**Förenklad lokal utvecklingsmiljö** – Distribuera endast Azure OpenAI, kör alla appar lokalt.

Infrastrukturen distribuerar följande Azure-resurser:

### AI-tjänster
- **Azure OpenAI**: Cognitive Services med två modelldistributioner:
  - **gpt-5.2**: Chattkompletteringsmodell (kapacitet 20K TPM)
  - **text-embedding-3-small**: Inbäddningsmodell för RAG (kapacitet 20K TPM)

### Lokal utveckling
Alla Spring Boot-applikationer körs lokalt på din dator:
- 01-introduction (port 8080)
- 02-prompt-engineering (port 8083)
- 03-rag (port 8081)
- 04-tools (port 8084)

## Skapade resurser

| Resurstyp | Resursnamnsmönster | Syfte |
|--------------|----------------------|---------|
| Resursgrupp | `rg-{environmentName}` | Innehåller alla resurser |
| Azure OpenAI | `aoai-{resourceToken}` | Hosting för AI-modell |

> **Notera:** `{resourceToken}` är en unik sträng genererad utifrån abonnemangs-ID, miljönamn och plats

## Snabbstart

### 1. Distribuera Azure OpenAI

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

När du uppmanas:
- Välj ditt Azure-abonnemang
- Välj en plats (rekommenderas: `eastus2` för GPT-5.2 tillgänglighet)
- Bekräfta miljönamnet (standard: `langchain4j-dev`)

Detta skapar:
- Azure OpenAI-resurs med GPT-5.2 och text-embedding-3-small
- Utdata med anslutningsinformation

### 2. Hämta anslutningsuppgifter

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Detta visar:
- `AZURE_OPENAI_ENDPOINT`: Din Azure OpenAI endpoint-URL
- `AZURE_OPENAI_KEY`: API-nyckel för autentisering
- `AZURE_OPENAI_DEPLOYMENT`: Namn på chattmodellen (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Namn på inbäddningsmodellen

### 3. Kör applikationer lokalt

Kommandot `azd up` skapar automatiskt en `.env`-fil i rotkatalogen med alla nödvändiga miljövariabler.

**Rekommenderat:** Starta alla webbapplikationer:

**Bash:**
```bash
# Från rotkatalogen
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# Från rotkatalogen
cd ../..
.\start-all.ps1
```

Eller starta en enskild modul:

**Bash:**
```bash
# Exempel: Starta endast introduktionsmodulen
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Exempel: Starta bara introduktionsmodulen
cd ../01-introduction
.\start.ps1
```

Båda skripten laddar automatiskt miljövariabler från rotens `.env`-fil som skapats av `azd up`.

## Konfiguration

### Anpassa modelldistributioner

För att ändra modellutplaceringar, redigera `infra/main.bicep` och ändra parametern `openAiDeployments`:

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

Tillgängliga modeller och versioner: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Byta Azure-region

För att distribuera i en annan region, redigera `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Kontrollera GPT-5.2 tillgänglighet: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

För att uppdatera infrastrukturen efter ändringar i Bicep-filerna:

**Bash:**
```bash
# Bygg om ARM-mallen
az bicep build --file infra/main.bicep

# Förhandsgranska ändringar
azd provision --preview

# Tillämpa ändringar
azd provision
```

**PowerShell:**
```powershell
# Bygg om ARM-mallen
az bicep build --file infra/main.bicep

# Förhandsgranska ändringar
azd provision --preview

# Tillämpa ändringar
azd provision
```

## Städa upp

För att ta bort alla resurser:

**Bash:**
```bash
# Ta bort alla resurser
azd down

# Ta bort allt inklusive miljön
azd down --purge
```

**PowerShell:**
```powershell
# Ta bort alla resurser
azd down

# Ta bort allt inklusive miljön
azd down --purge
```

**Varning**: Detta tar permanent bort alla Azure-resurser.

## Filtstruktur

## Kostnadsoptimering

### Utveckling/Testning
För utvecklings- och testmiljöer kan du minska kostnader:
- Använd Standardnivå (S0) för Azure OpenAI
- Sätt lägre kapacitet (10K TPM istället för 20K) i `infra/core/ai/cognitiveservices.bicep`
- Ta bort resurser när de inte används: `azd down`

### Produktion
För produktion:
- Öka OpenAI-kapaciteten baserat på användning (50K+ TPM)
- Aktivera zonredundans för högre tillgänglighet
- Implementera ordentlig övervakning och kostnadsvarningar

### Kostnadsuppskattning
- Azure OpenAI: Betala per token (input + output)
- GPT-5.2: ca 3-5 USD per 1M tokens (kontrollera aktuell prisbild)
- text-embedding-3-small: ca 0,02 USD per 1M tokens

Priskalkylator: https://azure.microsoft.com/pricing/calculator/

## Övervakning

### Visa Azure OpenAI-mått

Gå till Azure Portal → Din OpenAI-resurs → Metrics:
- Tokenbaserad användning
- HTTP-förfrågningsfrekvens
- Svarstid
- Aktiva tokens

## Felsökning

### Problem: Konflikt med Azure OpenAI subdomännamn

**Felmeddelande:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Orsak:**
Subdomännamnet som genererats från ditt abonnemang/miljö är redan använt, troligen från en tidigare distribution som inte tagits bort helt.

**Lösning:**
1. **Alternativ 1 – Använd ett annat miljönamn:**
   
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

2. **Alternativ 2 – Manuell distribution via Azure Portal:**
   - Gå till Azure Portal → Skapa en resurs → Azure OpenAI
   - Välj ett unikt namn för din resurs
   - Distribuera följande modeller:
     - **GPT-5.2**
     - **text-embedding-3-small** (för RAG-moduler)
   - **Viktigt:** Notera namn på distributionerna – de måste matcha konfigurationen i `.env`
   - Efter distribution, hämta endpoint och API-nyckel från "Keys and Endpoint"
   - Skapa en `.env`-fil i projektroten med:
     
     **Exempel på `.env`-fil:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Riktlinjer för modellnamn vid distribution:**
- Använd enkla, konsekventa namn: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Distribueringsnamn måste exakt matcha det du anger i `.env`
- Vanligt misstag: Skapa modell med ett namn men referera med ett annat i koden

### Problem: GPT-5.2 finns inte i vald region

**Lösning:**
- Välj en region med GPT-5.2-support (t.ex. eastus2)
- Kontrollera tillgänglighet: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Problem: Otillräcklig kvot för distribution

**Lösning:**
1. Begär kvöthöjning i Azure Portal
2. Eller använd lägre kapacitet i `main.bicep` (t.ex. capacity: 10)

### Problem: "Resource not found" vid lokal körning

**Lösning:**
1. Kontrollera distribution: `azd env get-values`
2. Kontrollera att endpoint och nyckel är korrekta
3. Säkerställ att resursgruppen existerar i Azure Portal

### Problem: Autentisering misslyckades

**Lösning:**
- Kontrollera att `AZURE_OPENAI_API_KEY` är korrekt satt
- Nyckelformat ska vara 32-teckens hexadecimalt
- Hämta ny nyckel från Azure Portal vid behov

### Distribution misslyckas

**Problem**: `azd provision` misslyckas med kvot- eller kapacitetsfel

**Lösning**: 
1. Prova en annan region – se avsnittet [Byta Azure-regioner](../../../../01-introduction/infra) för hur man konfigurerar regioner
2. Kontrollera att ditt abonnemang har kvot för Azure OpenAI:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Applikation kopplar inte

**Problem**: Java-applikationen visar anslutningsfel

**Lösning**:
1. Kontrollera att miljövariabler är exporterade:
   
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

2. Kontrollera att endpoint har korrekt format (bör vara `https://xxx.openai.azure.com`)
3. Verifiera att API-nyckel är primär eller sekundär från Azure Portal

**Problem**: 401 Unauthorized från Azure OpenAI

**Lösning**:
1. Hämta en ny API-nyckel från Azure Portal → Keys and Endpoint
2. Exportera om miljövariabeln `AZURE_OPENAI_API_KEY`
3. Säkerställ att modelldistributionerna är kompletta (kontrollera Azure Portal)

### Prestandaproblem

**Problem**: Långsamma svar

**Lösning**:
1. Kontrollera OpenAI-tokenanvändning och begränsningar i Azure Portal-mått
2. Öka TPM-kapaciteten om du når gränser
3. Överväg att använda högre nivå av resonemangsinsats (låg/medel/hög)

## Uppdatera infrastruktur

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

## Säkerhetsrekommendationer

1. **Lämna aldrig in API-nycklar i koden** – Använd miljövariabler
2. **Använd .env-filer lokalt** – Lägg till `.env` i `.gitignore`
3. **Roterar nycklar regelbundet** – Generera nya nycklar i Azure Portal
4. **Begränsa åtkomst** – Använd Azure RBAC för att styra vem som kan använda resurser
5. **Övervaka användning** – Ställ in kostnadsvarningar i Azure Portal

## Ytterligare resurser

- [Azure OpenAI Service Dokumentation](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 Modell Dokumentation](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI Dokumentation](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep Dokumentation](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI Officiell Integration](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Support

Vid problem:
1. Kontrollera [felsökningsavsnittet](../../../../01-introduction/infra) ovan
2. Granska Azure OpenAI-servicens status i Azure Portal
3. Skapa ett ärende i repot

## Licens

Se huvudfilen [LICENSE](../../../../LICENSE) för detaljer.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, var vänlig observera att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess modersmål ska betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för eventuella missförstånd eller feltolkningar som uppstår vid användning av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->