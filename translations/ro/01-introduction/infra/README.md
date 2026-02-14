# Infrastructura Azure pentru LangChain4j Începători

## Cuprins

- [Cerințe preliminare](../../../../01-introduction/infra)
- [Arhitectură](../../../../01-introduction/infra)
- [Resurse create](../../../../01-introduction/infra)
- [Pornire rapidă](../../../../01-introduction/infra)
- [Configurare](../../../../01-introduction/infra)
- [Comenzi de administrare](../../../../01-introduction/infra)
- [Optimizarea costurilor](../../../../01-introduction/infra)
- [Monitorizare](../../../../01-introduction/infra)
- [Depanare](../../../../01-introduction/infra)
- [Actualizarea infrastructurii](../../../../01-introduction/infra)
- [Curățare](../../../../01-introduction/infra)
- [Structura fișierelor](../../../../01-introduction/infra)
- [Recomandări de securitate](../../../../01-introduction/infra)
- [Resurse suplimentare](../../../../01-introduction/infra)

Acest director conține infrastructura Azure ca cod (IaC) folosind Bicep și Azure Developer CLI (azd) pentru implementarea resurselor Azure OpenAI.

## Cerințe preliminare

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (versiunea 2.50.0 sau mai recentă)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (versiunea 1.5.0 sau mai recentă)
- Un abonament Azure cu permisiuni pentru a crea resurse

## Arhitectură

**Configurație simplificată pentru dezvoltare locală** - Se implementează doar Azure OpenAI, toate aplicațiile rulează local.

Infrastructura implementează următoarele resurse Azure:

### Servicii AI
- **Azure OpenAI**: Servicii cognitive cu două implementări model:
  - **gpt-5.2**: Model pentru completare chat (capacitate 20K TPM)
  - **text-embedding-3-small**: Model embedding pentru RAG (capacitate 20K TPM)

### Dezvoltare locală
Toate aplicațiile Spring Boot rulează local pe calculatorul tău:
- 01-introduction (port 8080)
- 02-prompt-engineering (port 8083)
- 03-rag (port 8081)
- 04-tools (port 8084)

## Resurse create

| Tip resursă | Model nume resursă | Scop |
|--------------|----------------------|---------|
| Grupă de resurse | `rg-{environmentName}` | Conține toate resursele |
| Azure OpenAI | `aoai-{resourceToken}` | Găzduire model AI |

> **Notă:** `{resourceToken}` este un șir unic generat din ID-ul de abonament, numele mediului și locație

## Pornire rapidă

### 1. Implementați Azure OpenAI

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

Când vi se solicită:
- Selectați abonamentul Azure
- Alegeți o locație (recomandat: `eastus2` pentru disponibilitatea GPT-5.2)
- Confirmați numele mediului (implicit: `langchain4j-dev`)

Aceasta va crea:
- Resursa Azure OpenAI cu GPT-5.2 și text-embedding-3-small
- Va afișa detaliile de conectare

### 2. Obțineți detaliile de conectare

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Aceasta afișează:
- `AZURE_OPENAI_ENDPOINT`: URL-ul endpoint-ului Azure OpenAI
- `AZURE_OPENAI_KEY`: cheia API pentru autentificare
- `AZURE_OPENAI_DEPLOYMENT`: numele modelului chat (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: numele modelului embedding

### 3. Rulați aplicațiile local

Comanda `azd up` creează automat un fișier `.env` în directorul rădăcină cu toate variabilele de mediu necesare.

**Recomandat:** Porniți toate aplicațiile web:

**Bash:**
```bash
# Din directorul rădăcină
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# Din directorul rădăcină
cd ../..
.\start-all.ps1
```

Sau porniți un modul individual:

**Bash:**
```bash
# Exemplu: Porniți doar modulul de introducere
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Exemplu: Pornește doar modulul de introducere
cd ../01-introduction
.\start.ps1
```

Ambele scripturi încarcă automat variabilele de mediu din fișierul `.env` rădăcină creat de `azd up`.

## Configurare

### Personalizarea implementărilor modelului

Pentru a schimba implementările modelului, editați `infra/main.bicep` și modificați parametrul `openAiDeployments`:

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

Modele și versiuni disponibile: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Schimbarea regiunilor Azure

Pentru a implementa într-o regiune diferită, editați `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Verificați disponibilitatea GPT-5.2: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Pentru a actualiza infrastructura după modificarea fișierelor Bicep:

**Bash:**
```bash
# Reconstruiește șablonul ARM
az bicep build --file infra/main.bicep

# Previzualizează modificările
azd provision --preview

# Aplică modificările
azd provision
```

**PowerShell:**
```powershell
# Reconstruiește șablonul ARM
az bicep build --file infra/main.bicep

# Previzuare modificări
azd provision --preview

# Aplică modificările
azd provision
```

## Curățare

Pentru a șterge toate resursele:

**Bash:**
```bash
# Șterge toate resursele
azd down

# Șterge totul, inclusiv mediul
azd down --purge
```

**PowerShell:**
```powershell
# Șterge toate resursele
azd down

# Șterge totul, inclusiv mediul de lucru
azd down --purge
```

**Atenție**: Aceasta va șterge permanent toate resursele Azure.

## Structura fișierelor

## Optimizarea costurilor

### Dezvoltare/Testare
Pentru mediile de dezvoltare/test puteți reduce costurile:
- Folosiți nivelul Standard (S0) pentru Azure OpenAI
- Setați capacitate mai mică (10K TPM în loc de 20K) în `infra/core/ai/cognitiveservices.bicep`
- Ștergeți resursele când nu sunt în uz: `azd down`

### Producție
Pentru producție:
- Creșteți capacitatea OpenAI bazat pe utilizare (50K+ TPM)
- Activați redundanța zonală pentru disponibilitate sporită
- Implementați monitorizare adecvată și alerte de cost

### Estimarea costurilor
- Azure OpenAI: Plata per token (input + output)
- GPT-5.2: aproximativ 3–5 USD per 1M tokeni (verificați prețurile curente)
- text-embedding-3-small: aproximativ 0,02 USD per 1M tokeni

Calculator de prețuri: https://azure.microsoft.com/pricing/calculator/

## Monitorizare

### Vizualizați metricele Azure OpenAI

Accesați Portalul Azure → Resursa dvs. OpenAI → Metrice:
- Utilizare bazată pe tokeni
- Rata cererilor HTTP
- Timp de răspuns
- Tokeni activi

## Depanare

### Problemă: Conflict nume subdomeniu Azure OpenAI

**Mesaj de eroare:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Cauză:**
Numele subdomeniului generat din abonament/mediu este deja folosit, posibil dintr-o implementare anterioară neterminată complet.

**Soluție:**
1. **Opțiunea 1 - Folosiți un nume de mediu diferit:**
   
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

2. **Opțiunea 2 - Implementare manuală prin Portalul Azure:**
   - Mergeți în Portal Azure → Creați o resursă → Azure OpenAI
   - Alegeți un nume unic pentru resursa dvs.
   - Implementați următoarele modele:
     - **GPT-5.2**
     - **text-embedding-3-small** (pentru modulele RAG)
   - **Important:** Notați numele implementărilor - trebuie să coincidă cu configurația din `.env`
   - După implementare, obțineți endpoint și cheia API din „Keys and Endpoint”
   - Creați un fișier `.env` în rădăcina proiectului cu:
     
     **Exemplu fișier `.env`:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Ghid pentru denumirea implementărilor modelelor:**
- Folosiți nume simple și consistente: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Numele implementărilor trebuie să corespundă exact cu configurarea în `.env`
- Greșeală comună: Crearea unui model cu un nume, dar referirea altui nume în cod

### Problemă: GPT-5.2 nu este disponibil în regiunea selectată

**Soluție:**
- Alegeți o regiune cu acces GPT-5.2 (ex. eastus2)
- Verificați disponibilitatea: https://learn.microsoft.com/azure/ai-services/openai/concepts/models



### Problemă: Cota insuficientă pentru implementare

**Soluție:**
1. Cereți creșterea cotei în Portalul Azure
2. Sau folosiți capacitate mai mică în `main.bicep` (ex. capacity: 10)

### Problemă: "Resource not found" la rularea locală

**Soluție:**
1. Verificați implementarea: `azd env get-values`
2. Verificați dacă endpoint-ul și cheia sunt corecte
3. Asigurați-vă că grupa de resurse există în Portal Azure

### Problemă: Autentificare eșuată

**Soluție:**
- Verificați dacă `AZURE_OPENAI_API_KEY` este setat corect
- Formatul cheii trebuie să fie un șir hexazecimal de 32 caractere
- Obțineți o cheie nouă din Portal Azure dacă este necesar

### Implementarea eșuează

**Problemă**: `azd provision` eșuează cu erori legate de cotă sau capacitate

**Soluție**: 
1. Încercați o regiune diferită - Consultați secțiunea [Schimbarea regiunilor Azure](../../../../01-introduction/infra) pentru configurarea regiunilor
2. Verificați dacă abonamentul are cotă Azure OpenAI:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Aplicația nu se conectează

**Problemă**: Aplicația Java afișează erori de conectare

**Soluție**:
1. Verificați dacă variabilele de mediu sunt exportate:
   
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

2. Verificați corectitudinea formatului endpoint-ului (trebuie să fie `https://xxx.openai.azure.com`)
3. Verificați dacă cheia API este cheia primară sau secundară din Portal Azure

**Problemă**: 401 Unauthorized de la Azure OpenAI

**Soluție**:
1. Obțineți o cheie API nouă din Portal Azure → Keys and Endpoint
2. Re-exportați variabila de mediu `AZURE_OPENAI_API_KEY`
3. Asigurați-vă că implementările modelelor sunt finalizate (verificați în Portal Azure)

### Probleme de performanță

**Problemă**: Timpuri de răspuns lente

**Soluție**:
1. Verificați utilizarea tokenilor și limitările în metricele Portalului Azure
2. Creșteți capacitatea TPM dacă atingeți limitele
3. Luați în considerare folosirea unui nivel mai ridicat de efort de raționare (low/medium/high)

## Actualizarea infrastructurii

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

## Recomandări de securitate

1. **Nu comiteți niciodată cheile API** - Folosiți variabile de mediu
2. **Folosiți fișiere .env local** - Adăugați `.env` în `.gitignore`
3. **Rotiți cheile regulat** - Generați chei noi în Portal Azure
4. **Limitați accesul** - Folosiți RBAC Azure pentru control acces resurse
5. **Monitorizați utilizarea** - Configurați alerte de cost în Portal Azure

## Resurse suplimentare

- [Documentația serviciului Azure OpenAI](https://learn.microsoft.com/azure/ai-services/openai/)
- [Documentația modelului GPT-5.2](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Documentația Azure Developer CLI](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Documentația Bicep](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [Integrarea oficială LangChain4j OpenAI](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Suport

Pentru probleme:
1. Consultați [secțiunea de depanare](../../../../01-introduction/infra) de mai sus
2. Verificați starea serviciului Azure OpenAI în Portal Azure
3. Deschideți un issue în depozit

## Licență

Consultați fișierul rădăcină [LICENSE](../../../../LICENSE) pentru detalii.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:  
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să aveți în vedere că traducerile automate pot conține erori sau inexactități. Documentul original, în limba sa nativă, trebuie considerat sursa autoritară. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm responsabilitatea pentru orice neînțelegeri sau interpretări greșite rezultate din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->