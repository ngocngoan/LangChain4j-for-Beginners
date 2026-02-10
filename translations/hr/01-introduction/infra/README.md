# Azure Infrastruktura za LangChain4j Početak Rada

## Sadržaj

- [Preduvjeti](../../../../01-introduction/infra)
- [Arhitektura](../../../../01-introduction/infra)
- [Stvoreni Resursi](../../../../01-introduction/infra)
- [Brzi Početak](../../../../01-introduction/infra)
- [Konfiguracija](../../../../01-introduction/infra)
- [Upravljanje Naredbama](../../../../01-introduction/infra)
- [Optimizacija Troškova](../../../../01-introduction/infra)
- [Nadzor](../../../../01-introduction/infra)
- [Otklanjanje Problema](../../../../01-introduction/infra)
- [Ažuriranje Infrastrukture](../../../../01-introduction/infra)
- [Čišćenje](../../../../01-introduction/infra)
- [Struktura Datoteka](../../../../01-introduction/infra)
- [Sigurnosne Preporuke](../../../../01-introduction/infra)
- [Dodatni Resursi](../../../../01-introduction/infra)

Ovaj direktorij sadrži Azure infrastrukturu kao kod (IaC) koristeći Bicep i Azure Developer CLI (azd) za implementaciju Azure OpenAI resursa.

## Preduvjeti

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (verzija 2.50.0 ili novija)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (verzija 1.5.0 ili novija)
- Pretplata na Azure s dopuštenjima za stvaranje resursa

## Arhitektura

**Pojednostavljeni Postav za Lokalni Razvoj** - Implementira samo Azure OpenAI, sve aplikacije se pokreću lokalno.

Infrastruktura implementira sljedeće Azure resurse:

### AI Usluge
- **Azure OpenAI**: Kognitivne usluge sa dvije implementacije modela:
  - **gpt-5.2**: Model za chat dovršavanje (kapacitet 20K TPM)
  - **text-embedding-3-small**: Model za ugradnju za RAG (kapacitet 20K TPM)

### Lokalni Razvoj
Sve Spring Boot aplikacije se pokreću lokalno na vašem računalu:
- 01-introduction (port 8080)
- 02-prompt-engineering (port 8083)
- 03-rag (port 8081)
- 04-tools (port 8084)

## Stvoreni Resursi

| Tip Resursa | Uzorak Imena Resursa | Namjena |
|--------------|----------------------|---------|
| Grupa resursa | `rg-{environmentName}` | Sadrži sve resurse |
| Azure OpenAI | `aoai-{resourceToken}` | Hosting AI modela |

> **Napomena:** `{resourceToken}` je jedinstveni niz generiran iz ID pretplate, imena okoline i lokacije

## Brzi Početak

### 1. Implementirajte Azure OpenAI

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

Kad vas se upita:
- Odaberite svoju Azure pretplatu
- Izaberite lokaciju (preporučeno: `eastus2` za dostupnost GPT-5.2)
- Potvrdite ime okoline (zadano: `langchain4j-dev`)

Ovo će stvoriti:
- Azure OpenAI resurs s GPT-5.2 i text-embedding-3-small
- Izlazne detalje veze

### 2. Dohvatite Detalje Veze

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Ovo prikazuje:
- `AZURE_OPENAI_ENDPOINT`: URL vaše Azure OpenAI krajnje točke
- `AZURE_OPENAI_KEY`: API ključ za autentifikaciju
- `AZURE_OPENAI_DEPLOYMENT`: Ime chat modela (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Ime modela za ugradnju

### 3. Pokrenite Aplikacije Lokalno

Naredba `azd up` automatski stvara `.env` datoteku u korijenskom direktoriju sa svim potrebnim varijablama okoline.

**Preporučeno:** Pokrenite sve web aplikacije:

**Bash:**
```bash
# Iz korijenskog direktorija
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# Iz korijenskog direktorija
cd ../..
.\start-all.ps1
```

Ili pokrenite jedan modul:

**Bash:**
```bash
# Primjer: Pokrenite samo modul uvoda
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Primjer: Pokreni samo modul za uvod
cd ../01-introduction
.\start.ps1
```

Oba skripta automatski učitavaju varijable okoline iz kreirane `.env` datoteke u korijenu koju je stvorio `azd up`.

## Konfiguracija

### Prilagodba Implementacija Modela

Za promjenu implementacija modela, uredite `infra/main.bicep` i izmijenite parametar `openAiDeployments`:

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

Dostupni modeli i verzije: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Promjena Azure Regija

Za implementaciju u drugoj regiji, uredite `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Provjerite dostupnost GPT-5.2: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Za ažuriranje infrastrukture nakon promjena u Bicep datotekama:

**Bash:**
```bash
# Ponovno izgradite ARM predložak
az bicep build --file infra/main.bicep

# Pregled promjena
azd provision --preview

# Primijeni promjene
azd provision
```

**PowerShell:**
```powershell
# Ponovno izradite ARM predložak
az bicep build --file infra/main.bicep

# Pregledajte promjene
azd provision --preview

# Primijenite promjene
azd provision
```

## Čišćenje

Za brisanje svih resursa:

**Bash:**
```bash
# Izbriši sve resurse
azd down

# Izbriši sve uključujući okruženje
azd down --purge
```

**PowerShell:**
```powershell
# Izbriši sve resurse
azd down

# Izbriši sve uključujući okruženje
azd down --purge
```

**Upozorenje**: Ovo trajno briše sve Azure resurse.

## Struktura Datoteka

## Optimizacija Troškova

### Razvoj/Testiranje
Za razvojne/testne okoline možete smanjiti troškove:
- Koristite Standardni sloj (S0) za Azure OpenAI
- Postavite niži kapacitet (10K TPM umjesto 20K) u `infra/core/ai/cognitiveservices.bicep`
- Brišite resurse kada nisu u upotrebi: `azd down`

### Produkcija
Za produkciju:
- Povećajte kapacitet OpenAI-a ovisno o korištenju (50K+ TPM)
- Omogućite zonu redundantnosti za veću dostupnost
- Implementirajte odgovarajući nadzor i upozorenja o troškovima

### Procjena Troškova
- Azure OpenAI: Plaćanje po tokenu (ulaz + izlaz)
- GPT-5.2: ~$3-5 po 1M tokena (provjerite trenutačne cijene)
- text-embedding-3-small: ~$0.02 po 1M tokena

Kalkulator cijena: https://azure.microsoft.com/pricing/calculator/

## Nadzor

### Pregled Metrika Azure OpenAI

Idite na Azure Portal → Vaš OpenAI resurs → Metrike:
- Korištenje po tokenima
- Brzina HTTP zahtjeva
- Vrijeme odziva
- Aktivni tokeni

## Otklanjanje Problema

### Problem: Sukob imena poddomene Azure OpenAI

**Poruka o pogrešci:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Uzrok:**
Ime poddomene generirano iz vaše pretplate/okoline već je u upotrebi, moguće od prethodne implementacije koja nije u potpunosti uklonjena.

**Rješenje:**
1. **Opcija 1 - Koristite drugo ime okoline:**
   
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

2. **Opcija 2 - Ručna implementacija putem Azure Portala:**
   - Idite na Azure Portal → Stvori resurs → Azure OpenAI
   - Odaberite jedinstveno ime za svoj resurs
   - Implementirajte sljedeće modele:
     - **GPT-5.2**
     - **text-embedding-3-small** (za RAG module)
   - **Važno:** Zabilježite imena implementacija - moraju odgovarati konfiguraciji `.env`
   - Nakon implementacije, dohvatite svoju krajnju točku i API ključ iz "Ključevi i Krajnje Točke"
   - Kreirajte `.env` datoteku u korijenu projekta sa:
     
     **Primjer `.env` datoteke:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Smjernice za imenovanje implementacija modela:**
- Koristite jednostavna, dosljedna imena: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Imena implementacija moraju točno odgovarati onome što konfigurirate u `.env`
- Česta greška: Kreiranje modela s jednim imenom ali referenciranje drugog u kodu

### Problem: GPT-5.2 nije dostupan u odabranoj regiji

**Rješenje:**
- Izaberite regiju s pristupom GPT-5.2 (npr. eastus2)
- Provjerite dostupnost: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Problem: Nedovoljna kvota za implementaciju

**Rješenje:**
1. Zatražite povećanje kvote u Azure Portalu
2. Ili koristite manji kapacitet u `main.bicep` (npr. kapacitet: 10)

### Problem: "Resource not found" kod lokalnog pokretanja

**Rješenje:**
1. Provjerite implementaciju: `azd env get-values`
2. Provjerite jesu li krajnja točka i ključ točni
3. Provjerite postoji li grupa resursa u Azure Portalu

### Problem: Neuspjela autentikacija

**Rješenje:**
- Provjerite je li `AZURE_OPENAI_API_KEY` ispravno postavljen
- Format ključa treba biti 32-znamenkasti heksadecimalni niz
- Ako treba, dohvatite novi ključ iz Azure Portala

### Neuspjela Implementacija

**Problem**: `azd provision` ne uspijeva zbog problema s kvotom ili kapacitetom

**Rješenje**: 
1. Isprobajte drugu regiju - pogledajte odjeljak [Promjena Azure Regija](../../../../01-introduction/infra) za način konfiguracije regija
2. Provjerite ima li vaša pretplata kvotu za Azure OpenAI:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Aplikacija se Ne Spoji

**Problem**: Java aplikacija prikazuje greške povezivanja

**Rješenje**:
1. Provjerite jesu li varijable okoline eksportirane:
   
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

2. Provjerite je li format krajnje točke ispravan (trebalo bi biti `https://xxx.openai.azure.com`)
3. Provjerite je li API ključ primarni ili sekundarni ključ iz Azure Portala

**Problem**: 401 Unauthorized iz Azure OpenAI

**Rješenje**:
1. Dohvatite novi API ključ iz Azure Portala → Ključevi i Krajnje Točke
2. Ponovno eksportirajte varijablu okoline `AZURE_OPENAI_API_KEY`
3. Provjerite jesu li implementacije modela dovršene (provjerite Azure Portal)

### Problemi s Performansama

**Problem**: Sporo vrijeme odziva

**Rješenje**:
1. Provjerite korištenje tokena i ograničenja u metriku Azure Portala
2. Povećajte TPM kapacitet ako dosežete limite
3. Razmislite o korištenju višeg nivoa napora rezoniranja (low/medium/high)

## Ažuriranje Infrastrukture

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

## Sigurnosne Preporuke

1. **Nikad ne spremati API ključeve u repozitorij** - Koristite varijable okoline
2. **Koristite .env datoteke lokalno** - Dodajte `.env` u `.gitignore`
3. **Redovito rotirajte ključeve** - Generirajte nove ključeve u Azure Portalu
4. **Ograničite pristup** - Koristite Azure RBAC za kontrolu pristupa resursima
5. **Nadzor korištenja** - Postavite upozorenja o troškovima u Azure Portalu

## Dodatni Resursi

- [Dokumentacija Azure OpenAI servisa](https://learn.microsoft.com/azure/ai-services/openai/)
- [Dokumentacija GPT-5.2 modela](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Dokumentacija Azure Developer CLI](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Dokumentacija Bicep](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI službena integracija](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Podrška

Za probleme:
1. Provjerite [odjeljak za otklanjanje problema](../../../../01-introduction/infra) gore
2. Pregledajte zdravlje Azure OpenAI servisa u Azure Portalu
3. Otvorite issue u repozitoriju

## Licenca

Pogledajte root [LICENSE](../../../../LICENSE) datoteku za detalje.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Odricanje od odgovornosti**:
Ovaj dokument preveden je korištenjem AI usluge prijevoda [Co-op Translator](https://github.com/Azure/co-op-translator). Iako težimo točnosti, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba se smatrati službenim izvorom. Za važne informacije preporučuje se profesionalni ljudski prijevod. Nismo odgovorni za bilo kakve nesporazume ili pogrešne interpretacije koje proizlaze iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->