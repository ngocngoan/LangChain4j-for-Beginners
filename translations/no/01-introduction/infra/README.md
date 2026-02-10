# Azure Infrastruktur for LangChain4j Komme i gang

## Innholdsfortegnelse

- [Forutsetninger](../../../../01-introduction/infra)
- [Arkitektur](../../../../01-introduction/infra)
- [Opprettede ressurser](../../../../01-introduction/infra)
- [Rask start](../../../../01-introduction/infra)
- [Konfigurasjon](../../../../01-introduction/infra)
- [Administrasjonskommandoer](../../../../01-introduction/infra)
- [Kostnadsoptimalisering](../../../../01-introduction/infra)
- [Overvåking](../../../../01-introduction/infra)
- [Feilsøking](../../../../01-introduction/infra)
- [Oppdatere infrastruktur](../../../../01-introduction/infra)
- [Rydding](../../../../01-introduction/infra)
- [Filstruktur](../../../../01-introduction/infra)
- [Sikkerhetsanbefalinger](../../../../01-introduction/infra)
- [Ekstra ressurser](../../../../01-introduction/infra)

Denne katalogen inneholder Azure-infrastruktur som kode (IaC) ved bruk av Bicep og Azure Developer CLI (azd) for utrulling av Azure OpenAI-ressurser.

## Forutsetninger

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (versjon 2.50.0 eller nyere)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (versjon 1.5.0 eller nyere)
- Et Azure-abonnement med rettigheter til å opprette ressurser

## Arkitektur

**Forenklet lokal utviklingsoppsett** - Distribuer kun Azure OpenAI, kjør alle apper lokalt.

Infrastrukturen distribuerer følgende Azure-ressurser:

### AI-tjenester
- **Azure OpenAI**: Cognitive Services med to modellutrullinger:
  - **gpt-5.2**: Chat completion-modell (20K TPM kapasitet)
  - **text-embedding-3-small**: Inbeddingsmodell for RAG (20K TPM kapasitet)

### Lokal utvikling
Alle Spring Boot-applikasjoner kjører lokalt på din maskin:
- 01-introduction (port 8080)
- 02-prompt-engineering (port 8083)
- 03-rag (port 8081)
- 04-tools (port 8084)

## Opprettede ressurser

| Ressurstype | Ressursnavnsmønster | Formål |
|--------------|----------------------|---------|
| Ressursgruppe | `rg-{environmentName}` | Inneholder alle ressurser |
| Azure OpenAI | `aoai-{resourceToken}` | Hosting av AI-modeller |

> **Merk:** `{resourceToken}` er en unik streng generert fra abonnement-ID, miljønavn og lokasjon

## Rask start

### 1. Distribuer Azure OpenAI

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

Når du blir spurt:
- Velg ditt Azure-abonnement
- Velg en lokasjon (anbefalt: `eastus2` for GPT-5.2 tilgjengelighet)
- Bekreft miljønavnet (standard: `langchain4j-dev`)

Dette vil opprette:
- Azure OpenAI-ressurs med GPT-5.2 og text-embedding-3-small
- Utdatakoblingsdetaljer

### 2. Hent tilkoblingsdetaljer

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Dette viser:
- `AZURE_OPENAI_ENDPOINT`: Din Azure OpenAI-endepunkt-URL
- `AZURE_OPENAI_KEY`: API-nøkkel for autentisering
- `AZURE_OPENAI_DEPLOYMENT`: Chat-modellnavn (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Embeddingsmodellnavn

### 3. Kjør applikasjoner lokalt

Kommandoen `azd up` lager automatisk en `.env`-fil i rotkatalogen med alle nødvendige miljøvariabler.

**Anbefalt:** Start alle webapplikasjonene:

**Bash:**
```bash
# Fra rotkatalogen
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# Fra rotkatalogen
cd ../..
.\start-all.ps1
```

Eller start et enkelt modul:

**Bash:**
```bash
# Eksempel: Start bare introduksjonsmodulen
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Eksempel: Start bare introduksjonsmodulen
cd ../01-introduction
.\start.ps1
```

Begge skriptene laster automatisk miljøvariabler fra rotens `.env`-fil opprettet av `azd up`.

## Konfigurasjon

### Tilpasse modellutrullinger

For å endre modellutrullinger, rediger `infra/main.bicep` og modifiser parameteren `openAiDeployments`:

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

Tilgjengelige modeller og versjoner: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Endre Azure-regioner

For å distribuere i en annen region, rediger `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Sjekk GPT-5.2 tilgjengelighet: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

For å oppdatere infrastrukturen etter endringer i Bicep-filene:

**Bash:**
```bash
# Bygg opp ARM-malen på nytt
az bicep build --file infra/main.bicep

# Forhåndsvis endringer
azd provision --preview

# Bruk endringer
azd provision
```

**PowerShell:**
```powershell
# Bygg opp ARM-malen på nytt
az bicep build --file infra/main.bicep

# Forhåndsvis endringer
azd provision --preview

# Bruk endringer
azd provision
```

## Rydding

For å slette alle ressurser:

**Bash:**
```bash
# Slett alle ressurser
azd down

# Slett alt inkludert miljøet
azd down --purge
```

**PowerShell:**
```powershell
# Slett alle ressurser
azd down

# Slett alt inkludert miljøet
azd down --purge
```

**Advarsel**: Dette vil permanent slette alle Azure-ressurser.

## Filstruktur

## Kostnadsoptimalisering

### Utvikling/testing
For utviklings/testmiljøer kan du redusere kostnader:
- Bruk Standard nivå (S0) for Azure OpenAI
- Sett lavere kapasitet (10K TPM i stedet for 20K) i `infra/core/ai/cognitiveservices.bicep`
- Slett ressurser når de ikke er i bruk: `azd down`

### Produksjon
For produksjon:
- Øk OpenAI-kapasitet basert på bruk (50K+ TPM)
- Aktiver sone-redundans for høyere tilgjengelighet
- Implementer riktig overvåking og kostnadsvarsler

### Kostnadsestimering
- Azure OpenAI: Betal per token (inngang + utgang)
- GPT-5.2: Ca. $3-5 per 1M tokens (sjekk gjeldende priser)
- text-embedding-3-small: Ca. $0,02 per 1M tokens

Prisberegner: https://azure.microsoft.com/pricing/calculator/

## Overvåking

### Se Azure OpenAI-metrikker

Gå til Azure Portal → Din OpenAI-ressurs → Metrikker:
- Token-basert utnyttelse
- HTTP forespørselsrate
- Responstid
- Aktive tokens

## Feilsøking

### Problem: Navnekonflikt for Azure OpenAI subdomene

**Feilmelding:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Årsak:**
Subdomenenavnet generert fra ditt abonnement/miljø er allerede i bruk, muligens fra en tidligere distribusjon som ikke ble fullstendig fjernet.

**Løsning:**
1. **Alternativ 1 - Bruk et annet miljønavn:**
   
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

2. **Alternativ 2 - Manuell distribusjon via Azure Portal:**
   - Gå til Azure Portal → Opprett en ressurs → Azure OpenAI
   - Velg et unikt navn for ressursen din
   - Distribuer følgende modeller:
     - **GPT-5.2**
     - **text-embedding-3-small** (for RAG-moduler)
   - **Viktig:** Merk deg utrullingsnavnene – de må stemme overens med `.env`-konfigurasjonen
   - Etter utrulling, hent endepunkt og API-nøkkel fra "Keys and Endpoint"
   - Lag en `.env`-fil i prosjektets rot med:
     
     **Eksempel på `.env`-fil:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Retningslinjer for modellutrullingsnavn:**
- Bruk enkle, konsistente navn: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Utrullingsnavn må samsvare nøyaktig med det som settes i `.env`
- Vanlig feil: Lage modell med ett navn, men referere til et annet i koden

### Problem: GPT-5.2 ikke tilgjengelig i valgt region

**Løsning:**
- Velg en region med GPT-5.2-tilgang (f.eks. eastus2)
- Sjekk tilgjengelighet: https://learn.microsoft.com/azure/ai-services/openai/concepts/models



### Problem: Utilstrekkelig kvote for distribusjon

**Løsning:**
1. Be om kvoteøkning i Azure Portal
2. Eller bruk lavere kapasitet i `main.bicep` (f.eks. capacity: 10)

### Problem: "Resource not found" når du kjører lokalt

**Løsning:**
1. Verifiser distribusjon: `azd env get-values`
2. Sjekk at endepunkt og nøkkel er korrekt
3. Forsikre deg om at ressursgruppen finnes i Azure Portal

### Problem: Autentisering feilet

**Løsning:**
- Sjekk at `AZURE_OPENAI_API_KEY` er satt riktig
- Nøkkelformat skal være en 32-tegns heksadesimal streng
- Hent ny nøkkel fra Azure Portal ved behov

### Distribusjon feiler

**Problem**: `azd provision` feiler med kvote- eller kapasitetsfeil

**Løsning**: 
1. Prøv en annen region - Se seksjonen [Endre Azure-regioner](../../../../01-introduction/infra) for hvordan konfigurere regioner
2. Sjekk at abonnementet ditt har Azure OpenAI-kvote:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Applikasjon kobler ikke til

**Problem**: Java-applikasjon viser tilkoblingsfeil

**Løsning**:
1. Verifiser at miljøvariabler er eksportert:
   
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

2. Sjekk at endepunktformat er korrekt (skal være `https://xxx.openai.azure.com`)
3. Verifiser at API-nøkkelen er primær- eller sekundærnøkkel fra Azure Portal

**Problem**: 401 Unauthorized fra Azure OpenAI

**Løsning**:
1. Skaff ny API-nøkkel fra Azure Portal → Keys and Endpoint
2. Eksporter `AZURE_OPENAI_API_KEY` miljøvariabel på nytt
3. Sørg for at modellutrullinger er fullført (sjekk Azure Portal)

### Ytelsesproblemer

**Problem**: Trege responstider

**Løsning**:
1. Sjekk OpenAI-tokenbruk og begrensning i Azure Portal sine metrikker
2. Øk TPM-kapasitet hvis du når grenseverdier
3. Vurder å bruke høyere innsatsnivå for resonnering (lav/middels/høy)

## Oppdatere infrastruktur

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

## Sikkerhetsanbefalinger

1. **Ikke legg ut API-nøkler i kode** - Bruk miljøvariabler
2. **Bruk .env-filer lokalt** - Legg `.env` i `.gitignore`
3. **Roter nøkler regelmessig** - Generer nye nøkler i Azure Portal
4. **Begrens tilgang** - Bruk Azure RBAC for å styre hvem som har tilgang til ressurser
5. **Overvåk bruk** - Sett opp kostnadsvarsler i Azure Portal

## Ekstra ressurser

- [Azure OpenAI Service Dokumentasjon](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 Modell Dokumentasjon](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI Dokumentasjon](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep Dokumentasjon](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI Offisiell Integrasjon](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Support

For problemer:
1. Sjekk [feilsøkingsseksjonen](../../../../01-introduction/infra) ovenfor
2. Gjennomgå Azure OpenAI tjenestestatus i Azure Portal
3. Åpne en issue i repository

## Lisens

Se rotmappen [LICENSE](../../../../LICENSE) for detaljer.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vennligst vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det opprinnelige dokumentet på dets opprinnelige språk bør anses som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi påtar oss ikke ansvar for eventuelle misforståelser eller feiltolkninger som følge av bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->