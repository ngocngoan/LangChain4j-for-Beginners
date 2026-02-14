# Azure infraštruktúra pre LangChain4j Začíname

## Obsah

- [Predpoklady](../../../../01-introduction/infra)
- [Architektúra](../../../../01-introduction/infra)
- [Vytvorené zdroje](../../../../01-introduction/infra)
- [Rýchly štart](../../../../01-introduction/infra)
- [Konfigurácia](../../../../01-introduction/infra)
- [Správcovské príkazy](../../../../01-introduction/infra)
- [Optimalizácia nákladov](../../../../01-introduction/infra)
- [Monitorovanie](../../../../01-introduction/infra)
- [Riešenie problémov](../../../../01-introduction/infra)
- [Aktualizácia infraštruktúry](../../../../01-introduction/infra)
- [Vyčistenie](../../../../01-introduction/infra)
- [Štruktúra súborov](../../../../01-introduction/infra)
- [Bezpečnostné odporúčania](../../../../01-introduction/infra)
- [Dodatočné zdroje](../../../../01-introduction/infra)

Tento adresár obsahuje Azure infraštruktúru ako kód (IaC) použitím Bicep a Azure Developer CLI (azd) na nasadenie Azure OpenAI zdrojov.

## Predpoklady

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (verzia 2.50.0 alebo novšia)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (verzia 1.5.0 alebo novšia)
- Predplatné Azure s oprávneniami na vytváranie zdrojov

## Architektúra

**Zjednodušené nastavenie lokálneho vývoja** - Nasadiť iba Azure OpenAI, všetky aplikácie spustiť lokálne.

Infra nasadí nasledujúce Azure zdroje:

### AI služby
- **Azure OpenAI**: Kognitívne služby s dvoma nasadeniami modelov:
  - **gpt-5.2**: Model pre chatovací doplnok (kapacita 20 tisíc TPM)
  - **text-embedding-3-small**: Model pre vloženie pre RAG (kapacita 20 tisíc TPM)

### Lokálny vývoj
Všetky Spring Boot aplikácie bežia lokálne na vašom počítači:
- 01-introduction (port 8080)
- 02-prompt-engineering (port 8083)
- 03-rag (port 8081)
- 04-tools (port 8084)

## Vytvorené zdroje

| Typ zdroja | Vzor názvu zdroja | Účel |
|--------------|----------------------|---------|
| Resource Group | `rg-{environmentName}` | Obsahuje všetky zdroje |
| Azure OpenAI | `aoai-{resourceToken}` | Hosting AI modelu |

> **Poznámka:** `{resourceToken}` je jedinečný reťazec generovaný zo subscription ID, názvu prostredia a lokality

## Rýchly štart

### 1. Nasadenie Azure OpenAI

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

Po výzve:
- Vyberte svoje Azure predplatné
- Zvoľte lokalitu (odporúčané: `eastus2` pre dostupnosť GPT-5.2)
- Potvrďte názov prostredia (predvolené: `langchain4j-dev`)

Týmto vytvoríte:
- Azure OpenAI zdroj s GPT-5.2 a text-embedding-3-small
- Výstupné pripojovacie údaje

### 2. Získajte údaje o pripojení

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Zobrazuje sa:
- `AZURE_OPENAI_ENDPOINT`: URL vášho Azure OpenAI endpointu
- `AZURE_OPENAI_KEY`: API kľúč pre autentifikáciu
- `AZURE_OPENAI_DEPLOYMENT`: Názov chatovacieho modelu (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Názov modelu pre vloženie

### 3. Spustite aplikácie lokálne

Príkaz `azd up` automaticky vytvorí `.env` súbor v koreňovom adresári so všetkými potrebnými premennými prostredia.

**Odporúčané:** Spustite všetky webové aplikácie:

**Bash:**
```bash
# Z koreňového adresára
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# Z koreňového adresára
cd ../..
.\start-all.ps1
```

Alebo spustite jeden modul:

**Bash:**
```bash
# Príklad: Spustite iba modul úvodu
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Príklad: Spustiť len modul úvodu
cd ../01-introduction
.\start.ps1
```

Oba skripty automaticky načítajú premenné prostredia zo súboru `.env` v koreňovom adresári, ktorý vytvoril príkaz `azd up`.

## Konfigurácia

### Prispôsobenie nasadení modelov

Ak chcete zmeniť nasadenia modelov, upravte `infra/main.bicep` a zmeňte parameter `openAiDeployments`:

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

Dostupné modely a verzie: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Zmena Azure regiónov

Ak chcete nasadiť v inom regióne, upravte `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Skontrolujte dostupnosť GPT-5.2: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Ak chcete infraštruktúru aktualizovať po úpravách Bicep súborov:

**Bash:**
```bash
# Znovu zostaviť ARM šablónu
az bicep build --file infra/main.bicep

# Náhľad zmien
azd provision --preview

# Použiť zmeny
azd provision
```

**PowerShell:**
```powershell
# Znovu zostaviť ARM šablónu
az bicep build --file infra/main.bicep

# Náhľad zmien
azd provision --preview

# Použiť zmeny
azd provision
```

## Vyčistenie

Ak chcete zmazať všetky zdroje:

**Bash:**
```bash
# Odstrániť všetky zdroje
azd down

# Odstrániť všetko vrátane prostredia
azd down --purge
```

**PowerShell:**
```powershell
# Odstrániť všetky zdroje
azd down

# Odstrániť všetko vrátane prostredia
azd down --purge
```

**Upozornenie**: Tento krok nenávratne vymaže všetky Azure zdroje.

## Štruktúra súborov

## Optimalizácia nákladov

### Vývoj/Testovanie
Pre vývojové/testovacie prostredia môžete znížiť náklady:
- Použite štandardnú vrstvu (S0) pre Azure OpenAI
- Nastavte nižšiu kapacitu (10 tisíc TPM namiesto 20 tisíc) v `infra/core/ai/cognitiveservices.bicep`
- Zmažte zdroje, keď ich nepoužívate: `azd down`

### Produkcia
Pre produkciu:
- Zvýšte kapacitu OpenAI podľa používania (50 tisíc a viac TPM)
- Povoliť zónovú redundanciu pre vyššiu dostupnosť
- Zaviesť správne monitorovanie a upozornenia na náklady

### Odhad nákladov
- Azure OpenAI: Platba za token (vstup + výstup)
- GPT-5.2: cca 3-5 USD za 1 milión tokenov (skontrolujte aktuálne ceny)
- text-embedding-3-small: cca 0,02 USD za 1 milión tokenov

Kalkulačka cien: https://azure.microsoft.com/pricing/calculator/

## Monitorovanie

### Zobrazenie metrík Azure OpenAI

Prejdite do Azure portálu → Váš OpenAI zdroj → Metriky:
- Využitie založené na tokenoch
- Miera HTTP požiadaviek
- Čas odozvy
- Aktívne tokeny

## Riešenie problémov

### Problém: Konflikt názvu subdomény Azure OpenAI

**Chybové hlásenie:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Príčina:**
Názov subdomény generovaný z vášho predplatného/prostredia už je v používaní, pravdepodobne z predchádzajúceho nasadenia, ktoré nebolo úplne odstránené.

**Riešenie:**
1. **Možnosť 1 - Použiť iný názov prostredia:**
   
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

2. **Možnosť 2 - Manuálne nasadenie cez Azure portál:**
   - Prejdite do Azure portálu → Vytvoriť zdroj → Azure OpenAI
   - Vyberte jedinečný názov pre zdroj
   - Nasadte nasledujúce modely:
     - **GPT-5.2**
     - **text-embedding-3-small** (pre RAG moduly)
   - **Dôležité:** Zapamätajte si názvy nasadení - musia sa zhodovať s konfiguráciou `.env`
   - Po nasadení získajte endpoint a API kľúč v sekcii „Keys and Endpoint“
   - Vytvorte `.env` súbor v koreňovom projekte s:
     
     **Príklad `.env` súboru:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Odporúčania pre mená nasadení modelov:**
- Používajte jednoduché a konzistentné mená: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Názvy nasadení musia presne zodpovedať tým, čo nastavíte v `.env`
- Bežná chyba: Model vytvorený s jedným menom, ale v kóde sa odkazuje iné meno

### Problém: GPT-5.2 nie je dostupný v zvolenom regióne

**Riešenie:**
- Vyberte región s prístupom k GPT-5.2 (napr. eastus2)
- Skontrolujte dostupnosť: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Problém: Nedostatok kvóty pre nasadenie

**Riešenie:**
1. Požiadajte o zvýšenie kvóty v Azure Porte
2. Alebo použite nižšiu kapacitu v `main.bicep` (napr. capacity: 10)

### Problém: „Resource not found“ pri lokálnom spustení

**Riešenie:**
1. Overte nasadenie: `azd env get-values`
2. Skontrolujte správnosť endpointu a kľúča
3. Uistite sa, že resource group existuje v Azure Porte

### Problém: Autentifikácia zlyhala

**Riešenie:**
- Overte správnosť `AZURE_OPENAI_API_KEY`
- Formát kľúča by mal byť 32-miestny hexadecimálny reťazec
- Ak treba, získajte nový kľúč z Azure Portálu

### Nasadenie zlyháva

**Problém**: `azd provision` hlási chyby kvóty alebo kapacity

**Riešenie**: 
1. Skúste iný región - pozrite sekciu [Zmena Azure regiónov](../../../../01-introduction/infra)
2. Skontrolujte, či má vaše predplatné kvótu pre Azure OpenAI:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Aplikácia sa nepripája

**Problém**: Java aplikácia hlási chyby pripojenia

**Riešenie**:
1. Overte exportovanie premenných prostredia:
   
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

2. Skontrolujte, či je správny formát endpointu (má byť `https://xxx.openai.azure.com`)
3. Overte, že API kľúč je primárny alebo sekundárny z Azure portálu

**Problém**: 401 Unauthorized z Azure OpenAI

**Riešenie**:
1. Získajte nový API kľúč v Azure Portáli → Keys and Endpoint
2. Re-exportujte premennú `AZURE_OPENAI_API_KEY`
3. Overte, či sú nasadenia modelov dokončené (skontrolujte v Azure Portáli)

### Výkonnostné problémy

**Problém**: Pomalé časy odozvy

**Riešenie**:
1. Skontrolujte využívanie tokenov a obmedzenia v metrikách Azure Portálu
2. Zvýšte TPM kapacitu, ak dosahujete limity
3. Zvážte použitie vyššej úrovne úsilia pre rozumovanie (nízka/stredná/vysoká)

## Aktualizácia infraštruktúry

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

## Bezpečnostné odporúčania

1. **Nikdy nezverejňujte API kľúče** - používajte premenné prostredia
2. **Používajte .env súbory lokálne** - pridajte `.env` do `.gitignore`
3. **Pravidelne rotujte kľúče** - generujte nové kľúče v Azure Portáli
4. **Obmedzte prístup** - používajte Azure RBAC na kontrolu prístupov
5. **Monitorujte využívanie** - nastavte upozornenia na náklady v Azure Portáli

## Dodatočné zdroje

- [Dokumentácia Azure OpenAI služby](https://learn.microsoft.com/azure/ai-services/openai/)
- [Dokumentácia modelu GPT-5.2](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Dokumentácia Azure Developer CLI](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Dokumentácia Bicep](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI oficiálna integrácia](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Podpora

Pre problémy:
1. Skontrolujte [sekciu riešenia problémov](../../../../01-introduction/infra) vyššie
2. Skontrolujte stav služby Azure OpenAI v Azure Portáli
3. Otvorte issue v repozitári

## Licencia

Detaily nájdete v hlavnom súbore [LICENSE](../../../../LICENSE).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Oznámenie o vylúčení zodpovednosti**:
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, uvedomte si, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Originálny dokument v jeho pôvodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za akékoľvek nedorozumenia alebo nesprávne výklady vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->