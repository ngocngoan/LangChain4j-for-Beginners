# Azure infrastruktura pro LangChain4j Začínáme

## Obsah

- [Požadavky](../../../../01-introduction/infra)
- [Architektura](../../../../01-introduction/infra)
- [Vytvořené prostředky](../../../../01-introduction/infra)
- [Rychlý start](../../../../01-introduction/infra)
- [Konfigurace](../../../../01-introduction/infra)
- [Příkazy pro správu](../../../../01-introduction/infra)
- [Optimalizace nákladů](../../../../01-introduction/infra)
- [Monitorování](../../../../01-introduction/infra)
- [Řešení problémů](../../../../01-introduction/infra)
- [Aktualizace infrastruktury](../../../../01-introduction/infra)
- [Vyčištění](../../../../01-introduction/infra)
- [Struktura souborů](../../../../01-introduction/infra)
- [Doporučení pro zabezpečení](../../../../01-introduction/infra)
- [Další zdroje](../../../../01-introduction/infra)

Tento adresář obsahuje Azure infrastrukturu jako kód (IaC) pomocí Bicep a Azure Developer CLI (azd) pro nasazení Azure OpenAI prostředků.

## Požadavky

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (verze 2.50.0 nebo novější)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (verze 1.5.0 nebo novější)
- Azure předplatné s oprávněními pro vytváření prostředků

## Architektura

**Zjednodušené lokální vývojové prostředí** – nasadí pouze Azure OpenAI, všechny aplikace běží lokálně.

Infrastruktura nasazuje následující Azure prostředky:

### AI služby
- **Azure OpenAI**: Kognitivní služby se dvěma modelovými nasazeními:
  - **gpt-5.2**: Model pro chatové dokončení (kapacita 20K TPM)
  - **text-embedding-3-small**: Model pro vkládání (embedding) pro RAG (kapacita 20K TPM)

### Lokální vývoj
Všechny Spring Boot aplikace běží lokálně na vašem počítači:
- 01-introduction (port 8080)
- 02-prompt-engineering (port 8083)
- 03-rag (port 8081)
- 04-tools (port 8084)

## Vytvořené prostředky

| Typ prostředku | Vzor názvu prostředku | Účel |
|--------------|----------------------|---------|
| Resource Group | `rg-{environmentName}` | Obsahuje všechny prostředky |
| Azure OpenAI | `aoai-{resourceToken}` | Hosting AI modelu |

> **Poznámka:** `{resourceToken}` je unikátní řetězec generovaný z ID předplatného, názvu prostředí a lokace

## Rychlý start

### 1. Nasazení Azure OpenAI

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

Po výzvě:
- Vyberte své Azure předplatné
- Zvolte lokaci (doporučeno: `eastus2` pro dostupnost GPT-5.2)
- Potvrďte název prostředí (ve výchozím nastavení: `langchain4j-dev`)

Tím se vytvoří:
- Azure OpenAI prostředek s GPT-5.2 a text-embedding-3-small
- Výstupní připojovací detaily

### 2. Získání připojovacích detailů

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Toto zobrazí:
- `AZURE_OPENAI_ENDPOINT`: URL koncového bodu Azure OpenAI
- `AZURE_OPENAI_KEY`: API klíč pro autentizaci
- `AZURE_OPENAI_DEPLOYMENT`: Název chat modelu (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Název embedding modelu

### 3. Spuštění aplikací lokálně

Příkaz `azd up` automaticky vytvoří soubor `.env` v kořenovém adresáři se všemi potřebnými proměnnými prostředí.

**Doporučeno:** Spusťte všechny webové aplikace:

**Bash:**
```bash
# Ze základního adresáře
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# Ze základního adresáře
cd ../..
.\start-all.ps1
```

Nebo spusťte jednotlivý modul:

**Bash:**
```bash
# Příklad: Spusťte pouze úvodní modul
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Příklad: Spusťte pouze úvodní modul
cd ../01-introduction
.\start.ps1
```

Oba skripty automaticky načítají proměnné prostředí ze souboru `.env` v kořenovém adresáři vytvořeného příkazem `azd up`.

## Konfigurace

### Přizpůsobení nasazení modelů

Pro změnu modelových nasazení upravte `infra/main.bicep` a změňte parametr `openAiDeployments`:

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

Dostupné modely a verze: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Změna Azure regionů

Pro nasazení v jiném regionu upravte `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Zkontrolujte dostupnost GPT-5.2: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Pro aktualizaci infrastruktury po změnách Bicep souborů:

**Bash:**
```bash
# Přestavět ARM šablonu
az bicep build --file infra/main.bicep

# Náhled změn
azd provision --preview

# Použít změny
azd provision
```

**PowerShell:**
```powershell
# Přestavět ARM šablonu
az bicep build --file infra/main.bicep

# Náhled změn
azd provision --preview

# Použít změny
azd provision
```

## Vyčištění

Pro odstranění všech prostředků:

**Bash:**
```bash
# Odstraňte všechny prostředky
azd down

# Odstraňte vše včetně prostředí
azd down --purge
```

**PowerShell:**
```powershell
# Odstraňte všechny zdroje
azd down

# Odstraňte vše včetně prostředí
azd down --purge
```

**Upozornění**: Toto trvale odstraní všechny Azure prostředky.

## Struktura souborů

## Optimalizace nákladů

### Vývoj/Testování
Pro dev/test prostředí můžete snížit náklady:
- Použijte Standardní úroveň (S0) pro Azure OpenAI
- Nastavte nižší kapacitu (10K TPM místo 20K) v `infra/core/ai/cognitiveservices.bicep`
- Odstraňte prostředky když nejsou používány: `azd down`

### Produkce
Pro produkci:
- Zvyšte kapacitu OpenAI podle využití (50K+ TPM)
- Povolit zónovou redundanci pro vyšší dostupnost
- Implementujte správné monitorování a upozornění na náklady

### Odhad nákladů
- Azure OpenAI: Platba za token (vstup + výstup)
- GPT-5.2: cca $3-5 za 1M tokenů (ověřte aktuální ceny)
- text-embedding-3-small: cca $0.02 za 1M tokenů

Kalkulačka cen: https://azure.microsoft.com/pricing/calculator/

## Monitorování

### Zobrazení metrik Azure OpenAI

Přejděte do Azure Portálu → Váš OpenAI prostředek → Metriky:
- Využití založené na tokenech
- Míra HTTP požadavků
- Doba odezvy
- Aktivní tokeny

## Řešení problémů

### Problém: Konflikt názvu subdomény Azure OpenAI

**Chybová zpráva:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Příčina:**
Název subdomény vygenerovaný z vašeho předplatného/prostředí již existuje, pravděpodobně z dřívějšího nasazení, které nebylo úplně vyčištěno.

**Řešení:**
1. **Možnost 1 – Použijte jiný název prostředí:**
   
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

2. **Možnost 2 – Manuální nasazení přes Azure Portal:**
   - Přejděte do Azure Portal → Vytvořit prostředek → Azure OpenAI
   - Zvolte unikátní název pro váš prostředek
   - Nasadíte následující modely:
     - **GPT-5.2**
     - **text-embedding-3-small** (pro RAG moduly)
   - **Důležité:** Poznamenejte si názvy nasazení - musí odpovídat konfiguraci `.env`
   - Po nasazení získejte endpoint a API klíč z "Klíče a koncový bod"
   - Vytvořte `.env` soubor v kořenovém adresáři s:
     
     **Příklad `.env` souboru:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Pokyny pro pojmenování nasazení modelů:**
- Používejte jednoduché, konzistentní názvy: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Názvy nasazení musejí přesně odpovídat konfiguraci v `.env`
- častá chyba: vytvoření modelu s jiným názvem než je nastaven v kódu

### Problém: GPT-5.2 není dostupný ve vybraném regionu

**Řešení:**
- Vyberte region s dostupností GPT-5.2 (např. eastus2)
- Zkontrolujte dostupnost: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Problém: Nedostatečný kvóta pro nasazení

**Řešení:**
1. Požádejte o navýšení kvóty v Azure Portálu
2. Nebo použijte nižší kapacitu v `main.bicep` (např. capacity: 10)

### Problém: „Resource not found“ při lokálním spuštění

**Řešení:**
1. Ověřte nasazení: `azd env get-values`
2. Zkontrolujte správnost endpointu a klíče
3. Ujistěte se, že resource group existuje v Azure Portálu

### Problém: Autentizace selhala

**Řešení:**
- Ověřte správné nastavení `AZURE_OPENAI_API_KEY`
- Formát klíče by měl být 32znakový hexadecimální řetězec
- Pokud potřebujete, získejte nový klíč z Azure Portálu

### Nasazení selhalo

**Problém**: `azd provision` selže s chybou kvóty nebo kapacity

**Řešení**: 
1. Zkuste jiný region – viz sekce [Změna Azure regionů](../../../../01-introduction/infra)
2. Ověřte, že vaše předplatné má kvótu pro Azure OpenAI:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Aplikace se nepřipojuje

**Problém**: Java aplikace hlásí chyby připojení

**Řešení**:
1. Ověřte, že jsou exportovány proměnné prostředí:
   
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

2. Zkontrolujte formát endpointu (mělo by být `https://xxx.openai.azure.com`)
3. Ověřte, že API klíč je primární nebo sekundární klíč z Azure Portálu

**Problém**: 401 Unauthorized z Azure OpenAI

**Řešení**:
1. Získejte nový API klíč z Azure Portálu → Klíče a koncový bod
2. Znovu exportujte proměnnou `AZURE_OPENAI_API_KEY`
3. Ujistěte se, že modelová nasazení jsou kompletní (zkontrolujte Azure Portal)

### Problémy s výkonem

**Problém**: Pomalé doby odezvy

**Řešení**:
1. Zkontrolujte využití tokenů a throttling v metrikách Azure Portálu
2. Zvyšte kapacitu TPM pokud dosahujete limitu
3. Zvažte použití vyšší úrovně „reasoning-effort“ (low/medium/high)

## Aktualizace infrastruktury

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

## Doporučení pro zabezpečení

1. **Nikdy nezveřejňujte API klíče** – používejte proměnné prostředí
2. **Používejte lokálně `.env` soubory** – přidejte `.env` do `.gitignore`
3. **Pravidelně rotujte klíče** – generujte nové v Azure Portálu
4. **Omezte přístup** – použijte Azure RBAC pro řízení přístupu k prostředkům
5. **Monitorujte využití** – nastavte upozornění na náklady v Azure Portálu

## Další zdroje

- [Dokumentace služby Azure OpenAI](https://learn.microsoft.com/azure/ai-services/openai/)
- [Dokumentace k modelu GPT-5.2](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Dokumentace Azure Developer CLI](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Dokumentace Bicep](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [Oficiální integrace LangChain4j OpenAI](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Podpora

Pro řešení problémů:
1. Podívejte se na [sekci řešení problémů](../../../../01-introduction/infra) výše
2. Zkontrolujte stav služby Azure OpenAI v Azure Portálu
3. Otevřete issue v repozitáři

## Licence

Podrobnosti najdete v souboru [LICENSE](../../../../LICENSE) v kořenovém adresáři.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). I když usilujeme o přesnost, mějte prosím na paměti, že automatické překlady mohou obsahovat chyby nebo nepřesnosti. Originální dokument v jeho původním jazyce by měl být považován za závazný zdroj. Pro důležité informace se doporučuje profesionální lidský překlad. Nepřebíráme žádnou odpovědnost za nesprávné pochopení nebo špatné výklady vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->