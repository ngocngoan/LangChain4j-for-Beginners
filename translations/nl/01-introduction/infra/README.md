# Azure-infrastructuur voor LangChain4j Aan de Slag

## Inhoudsopgave

- [Vereisten](../../../../01-introduction/infra)
- [Architectuur](../../../../01-introduction/infra)
- [Gemaakte resources](../../../../01-introduction/infra)
- [Quick Start](../../../../01-introduction/infra)
- [Configuratie](../../../../01-introduction/infra)
- [Beheercommando's](../../../../01-introduction/infra)
- [Kostenoptimalisatie](../../../../01-introduction/infra)
- [Monitoring](../../../../01-introduction/infra)
- [Probleemoplossing](../../../../01-introduction/infra)
- [Infrastructuur bijwerken](../../../../01-introduction/infra)
- [Opruimen](../../../../01-introduction/infra)
- [Bestandsstructuur](../../../../01-introduction/infra)
- [Beveiligingsaanbevelingen](../../../../01-introduction/infra)
- [Aanvullende bronnen](../../../../01-introduction/infra)

Deze map bevat de Azure infrastructuur als code (IaC) met Bicep en Azure Developer CLI (azd) voor het uitrollen van Azure OpenAI-resources.

## Vereisten

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (versie 2.50.0 of hoger)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (versie 1.5.0 of hoger)
- Een Azure-abonnement met permissies om resources aan te maken

## Architectuur

**Vereenvoudigde lokale ontwikkelomgeving** – Alleen Azure OpenAI uitrollen, alle apps lokaal draaien.

De infrastructuur zet de volgende Azure-resources op:

### AI-diensten
- **Azure OpenAI**: Cognitive Services met twee model-uitrols:
  - **gpt-5.2**: Chat completion-model (20K TPM capaciteit)
  - **text-embedding-3-small**: Embedding-model voor RAG (20K TPM capaciteit)

### Lokale ontwikkeling
Alle Spring Boot-applicaties draaien lokaal op je machine:
- 01-introduction (poort 8080)
- 02-prompt-engineering (poort 8083)
- 03-rag (poort 8081)
- 04-tools (poort 8084)

## Gemaakte resources

| Resource Type | Resource Naam Patroon | Doel |
|--------------|----------------------|---------|
| Resourcegroep | `rg-{environmentName}` | Bevat alle resources |
| Azure OpenAI | `aoai-{resourceToken}` | Hosting AI-model |

> **Opmerking:** `{resourceToken}` is een unieke string die wordt gegenereerd uit het abonnement-ID, omgevingsnaam en locatie

## Quick Start

### 1. Azure OpenAI uitrollen

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

Bij de vraag:
- Selecteer je Azure-abonnement
- Kies een locatie (aanbevolen: `eastus2` voor GPT-5.2 beschikbaarheid)
- Bevestig de omgevingsnaam (standaard: `langchain4j-dev`)

Dit maakt aan:
- Azure OpenAI-resource met GPT-5.2 en text-embedding-3-small
- Geeft verbindingsgegevens weer

### 2. Verkrijg verbindingsgegevens

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Dit toont:
- `AZURE_OPENAI_ENDPOINT`: Jouw Azure OpenAI eindpunt-URL
- `AZURE_OPENAI_KEY`: API-sleutel voor authenticatie
- `AZURE_OPENAI_DEPLOYMENT`: Naam chat-model (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Naam embedding-model

### 3. Applicaties lokaal uitvoeren

Het `azd up`-commando maakt automatisch een `.env`-bestand aan in de hoofdmap met alle benodigde omgevingsvariabelen.

**Aanbevolen:** Start alle webapplicaties:

**Bash:**
```bash
# Vanaf de hoofdmap
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# Vanuit de hoofdmap
cd ../..
.\start-all.ps1
```

Of start een enkele module:

**Bash:**
```bash
# Voorbeeld: Start alleen de introductiemodule
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Voorbeeld: Begin alleen met de introductiemodule
cd ../01-introduction
.\start.ps1
```

Beide scripts laden automatisch omgevingsvariabelen uit het hoofdbestand `.env` dat door `azd up` is aangemaakt.

## Configuratie

### Aanpassen model-implementaties

Om modelimplementaties te wijzigen, bewerk je `infra/main.bicep` en pas je de parameter `openAiDeployments` aan:

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

Beschikbare modellen en versies: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Azure-regio’s wijzigen

Om in een andere regio uit te rollen, bewerk je `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Controleer GPT-5.2 beschikbaarheid: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Om infrastructuur bij te werken na aanpassingen aan Bicep-bestanden:

**Bash:**
```bash
# Bouw de ARM-sjabloon opnieuw op
az bicep build --file infra/main.bicep

# Wijzigingen bekijken
azd provision --preview

# Wijzigingen toepassen
azd provision
```

**PowerShell:**
```powershell
# Bouw de ARM-sjabloon opnieuw op
az bicep build --file infra/main.bicep

# Wijzigingen bekijken
azd provision --preview

# Wijzigingen toepassen
azd provision
```

## Opruimen

Om alle resources te verwijderen:

**Bash:**
```bash
# Verwijder alle bronnen
azd down

# Verwijder alles inclusief de omgeving
azd down --purge
```

**PowerShell:**
```powershell
# Verwijder alle bronnen
azd down

# Verwijder alles inclusief de omgeving
azd down --purge
```

**Waarschuwing**: Dit verwijdert definitief alle Azure-resources.

## Bestandsstructuur

## Kostenoptimalisatie

### Ontwikkeling/Testen
Voor dev/test-omgevingen kun je kosten verlagen:
- Gebruik Standard-tier (S0) voor Azure OpenAI
- Stel lagere capaciteit in (10K TPM in plaats van 20K) in `infra/core/ai/cognitiveservices.bicep`
- Verwijder resources als je ze niet gebruikt: `azd down`

### Productie
Voor productie:
- Verhoog OpenAI-capaciteit op basis van gebruik (50K+ TPM)
- Schakel zone-redundantie in voor hogere beschikbaarheid
- Implementeer goede monitoring en kostenalerts

### Kostenraming
- Azure OpenAI: betalen per token (invoer + uitvoer)
- GPT-5.2: ongeveer $3-5 per 1M tokens (controleer actuele prijzen)
- text-embedding-3-small: ongeveer $0,02 per 1M tokens

Prijscalculator: https://azure.microsoft.com/pricing/calculator/

## Monitoring

### Bekijk Azure OpenAI-metrics

Ga naar Azure Portal → Je OpenAI-resource → Metrics:
- Token-gebaseerd gebruik
- HTTP-verzoekfrequentie
- Reactietijd
- Actieve tokens

## Probleemoplossing

### Probleem: Azure OpenAI subdomeinnaamconflict

**Foutmelding:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Oorzaak:**
De subdomeinnaam die is gegenereerd uit je abonnement/omgeving is al in gebruik, mogelijk van een eerdere uitrol die niet volledig is verwijderd.

**Oplossing:**
1. **Optie 1 - Gebruik een andere omgevingsnaam:**
   
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

2. **Optie 2 - Handmatige uitrol via Azure Portal:**
   - Ga naar Azure Portal → Maak een resource → Azure OpenAI
   - Kies een unieke naam voor je resource
   - Rol de volgende modellen uit:
     - **GPT-5.2**
     - **text-embedding-3-small** (voor RAG-modules)
   - **Belangrijk:** Noteer je uitrolnamen – deze moeten overeenkomen met de `.env` configuratie
   - Na uitrol, haal je je eindpunt en API-sleutel op bij "Keys and Endpoint"
   - Maak een `.env`-bestand aan in de projectroot met:
     
     **Voorbeeld `.env` bestand:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Model Uitrol Naamgevingsrichtlijnen:**
- Gebruik eenvoudige, consistente namen: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Uitrolnamen moeten exact overeenkomen met de configuratie in `.env`
- Veelvoorkomende fout: model aanmaken met één naam maar in code een andere naam gebruiken

### Probleem: GPT-5.2 niet beschikbaar in geselecteerde regio

**Oplossing:**
- Kies een regio met GPT-5.2 toegang (bijv. eastus2)
- Controleer beschikbaarheid: https://learn.microsoft.com/azure/ai-services/openai/concepts/models



### Probleem: Onvoldoende quota voor uitrol

**Oplossing:**
1. Vraag een quota-verhoging aan in Azure Portal
2. Of gebruik lagere capaciteit in `main.bicep` (bijv. capaciteit: 10)

### Probleem: "Resource niet gevonden" bij lokaal uitvoeren

**Oplossing:**
1. Controleer uitrol: `azd env get-values`
2. Controleer of eindpunt en sleutel correct zijn
3. Zorg dat resourcegroep bestaat in Azure Portal

### Probleem: Authenticatie mislukt

**Oplossing:**
- Controleer of `AZURE_OPENAI_API_KEY` correct is ingesteld
- Sleutel heeft 32-karakter hexadecimale formaat
- Vraag nieuwe sleutel aan in Azure Portal indien nodig

### Uitrol mislukt

**Probleem**: `azd provision` mislukt met quota- of capaciteitsfouten

**Oplossing**: 
1. Probeer een andere regio - zie [Azure-regio's wijzigen](../../../../01-introduction/infra) sectie voor configuratie
2. Controleer of abonnement OpenAI-quota heeft:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Applicatie maakt geen verbinding

**Probleem**: Java-applicatie geeft verbindingsfouten

**Oplossing**:
1. Controleer of omgevingsvariabelen zijn geëxporteerd:
   
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

2. Controleer of eindpunt juist is (moet `https://xxx.openai.azure.com` zijn)
3. Controleer of API-sleutel de primaire of secundaire sleutel van Azure Portal is

**Probleem**: 401 Unauthorized van Azure OpenAI

**Oplossing**:
1. Vraag een nieuwe API-sleutel aan in Azure Portal → Keys and Endpoint
2. Exporteer de `AZURE_OPENAI_API_KEY` omgevingsvariabele opnieuw
3. Zorg dat model-uitrollen compleet zijn (controleer Azure Portal)

### Prestatieproblemen

**Probleem**: Trage reactietijden

**Oplossing**:
1. Controleer OpenAI token gebruik en throttling in Azure Portal-metrics
2. Verhoog TPM-capaciteit als je limieten bereikt
3. Overweeg gebruik te maken van een hoger redeneerniveau (laag/midden/hoog)

## Infrastructuur bijwerken

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

## Beveiligingsaanbevelingen

1. **Nooit API-sleutels committen** – Gebruik omgevingsvariabelen
2. **Gebruik lokaal .env-bestanden** – Voeg `.env` toe aan `.gitignore`
3. **Draai sleutels regelmatig** – Genereer nieuwe sleutels in Azure Portal
4. **Beperk toegang** – Gebruik Azure RBAC om toegang te beheren
5. **Monitor gebruik** – Stel kostenalerts in Azure Portal in

## Aanvullende bronnen

- [Azure OpenAI Service Documentatie](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 Model Documentatie](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI Documentatie](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep Documentatie](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI Officiële Integratie](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Ondersteuning

Bij problemen:
1. Bekijk de [probleemoplossingssectie](../../../../01-introduction/infra) hierboven
2. Controleer de gezondheid van de Azure OpenAI-service in Azure Portal
3. Open een issue in de repository

## Licentie

Bekijk het hoofdbestand [LICENSE](../../../../LICENSE) voor details.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:  
Dit document is vertaald met behulp van de AI-vertalingsdienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het oorspronkelijke document in de oorspronkelijke taal moet worden beschouwd als de gezaghebbende bron. Voor cruciale informatie wordt een professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->