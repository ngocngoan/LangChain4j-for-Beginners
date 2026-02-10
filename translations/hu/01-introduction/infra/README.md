# Azure infrastruktúra a LangChain4j kezdő lépésekhez

## Tartalomjegyzék

- [Előfeltételek](../../../../01-introduction/infra)
- [Architektúra](../../../../01-introduction/infra)
- [Létrehozott erőforrások](../../../../01-introduction/infra)
- [Gyors kezdés](../../../../01-introduction/infra)
- [Konfiguráció](../../../../01-introduction/infra)
- [Kezelő parancsok](../../../../01-introduction/infra)
- [Költségoptimalizálás](../../../../01-introduction/infra)
- [Monitorozás](../../../../01-introduction/infra)
- [Hibakeresés](../../../../01-introduction/infra)
- [Infrastruktúra frissítése](../../../../01-introduction/infra)
- [Takarítás](../../../../01-introduction/infra)
- [Fájl szerkezet](../../../../01-introduction/infra)
- [Biztonsági ajánlások](../../../../01-introduction/infra)
- [További erőforrások](../../../../01-introduction/infra)

Ez a könyvtár tartalmazza az Azure infrastruktúrát mint kódot (IaC) Bicep és az Azure Developer CLI (azd) segítségével az Azure OpenAI erőforrások telepítéséhez.

## Előfeltételek

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (2.50.0-s vagy újabb verzió)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (1.5.0-s vagy újabb verzió)
- Azure előfizetés, amelyhez erőforrás létrehozási jogosultságok tartoznak

## Architektúra

**Egyszerűsített helyi fejlesztési beállítás** – Csak Azure OpenAI telepítése, az összes alkalmazás helyileg fut.

Az infrastruktúra a következő Azure erőforrásokat telepíti:

### AI szolgáltatások
- **Azure OpenAI**: Kognitív szolgáltatások két modell telepítéssel:
  - **gpt-5.2**: Chat befejező modell (20K TPM kapacitás)
  - **text-embedding-3-small**: Beágyazó modell RAG-hoz (20K TPM kapacitás)

### Helyi fejlesztés
Minden Spring Boot alkalmazás helyileg fut a gépeden:
- 01-introduction (8080 port)
- 02-prompt-engineering (8083 port)
- 03-rag (8081 port)
- 04-tools (8084 port)

## Létrehozott erőforrások

| Erőforrás típusa | Erőforrás név minta | Cél |
|------------------|---------------------|-----|
| Erőforráscsoport | `rg-{environmentName}` | Minden erőforrást tartalmaz |
| Azure OpenAI     | `aoai-{resourceToken}` | AI modell hosztolás |

> **Megjegyzés:** A `{resourceToken}` egy egyedi karakterlánc, amely az előfizetés azonosítóból, környezet névből és helyszínből generálódik

## Gyors kezdés

### 1. Azure OpenAI telepítése

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

Amikor a rendszer kéri:
- Válassza ki Azure előfizetését
- Válasszon helyszínt (ajánlott: `eastus2` a GPT-5.2 elérhetőség miatt)
- Erősítse meg a környezet nevét (alapértelmezett: `langchain4j-dev`)

Ez létrehozza:
- Azure OpenAI erőforrás GPT-5.2 és text-embedding-3-small modellekkel
- Kimeneti kapcsolódási adatokat

### 2. Kapcsolódási adatok lekérése

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Ez megjeleníti:
- `AZURE_OPENAI_ENDPOINT`: Azure OpenAI végpont URL-je
- `AZURE_OPENAI_KEY`: API kulcs azonosításhoz
- `AZURE_OPENAI_DEPLOYMENT`: Chat modell neve (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Beágyazó modell neve

### 3. Alkalmazások helyi futtatása

Az `azd up` parancs automatikusan létrehoz egy `.env` fájlt a gyökérkönyvtárban az összes szükséges környezeti változóval.

**Ajánlott:** Indítsd el az összes webalkalmazást:

**Bash:**
```bash
# A gyökérkönyvtárból
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# A gyökér könyvtárból
cd ../..
.\start-all.ps1
```

Vagy indíts meg egyetlen modult:

**Bash:**
```bash
# Példa: Csak a bevezető modult indítsa el
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Példa: Csak az bevezető modult indítsa el
cd ../01-introduction
.\start.ps1
```

Mindkét szkript automatikusan betölti a környezeti változókat a `azd up` által létrehozott gyökér `.env` fájlból.

## Konfiguráció

### Modell telepítések testreszabása

A modell telepítések módosításához szerkeszd az `infra/main.bicep` fájlt, és módosítsd az `openAiDeployments` paramétert:

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

Elérhető modellek és verziók: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Azure régiók módosítása

Más régióba telepítéshez szerkeszd az `infra/main.bicep` fájlt:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Ellenőrizd a GPT-5.2 elérhetőségét: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Az infrastruktúra frissítéséhez Bicep fájlok módosítása után:

**Bash:**
```bash
# ARM sablon újrafelépítése
az bicep build --file infra/main.bicep

# Változtatások előnézete
azd provision --preview

# Változtatások érvényesítése
azd provision
```

**PowerShell:**
```powershell
# Az ARM sablon újraépítése
az bicep build --file infra/main.bicep

# Változások előnézete
azd provision --preview

# Változások alkalmazása
azd provision
```

## Takarítás

Az összes erőforrás törléséhez:

**Bash:**
```bash
# Minden erőforrás törlése
azd down

# Minden törlése, beleértve a környezetet is
azd down --purge
```

**PowerShell:**
```powershell
# Minden erőforrás törlése
azd down

# Minden törlése, beleértve a környezetet is
azd down --purge
```

**Figyelem**: Ez véglegesen törli az összes Azure erőforrást.

## Fájl szerkezet

## Költségoptimalizálás

### Fejlesztés/Tesztelés
Fejlesztési/teszt környezetekben csökkentheted a költségeket:
- Használj Standard (S0) szintet Azure OpenAI-hoz
- Állíts alacsonyabb kapacitást (10K TPM a 20K helyett) az `infra/core/ai/cognitiveservices.bicep` fájlban
- Töröld az erőforrásokat, ha nem használod: `azd down`

### Üzemeltetés
Üzemeltetéshez:
- Növeld az OpenAI kapacitását a használat alapján (50K+ TPM)
- Engedélyezd a zónák közötti redundanciát a magasabb rendelkezésre állásért
- Alkalmazz megfelelő monitorozást és költségértesítéseket

### Költségbecslés
- Azure OpenAI: fizetés tokenenként (input + output)
- GPT-5.2: kb. 3-5 USD 1 millió tokenenként (aktuális árakat ellenőrizd)
- text-embedding-3-small: kb. 0,02 USD 1 millió tokenenként

Árkalkulátor: https://azure.microsoft.com/pricing/calculator/

## Monitorozás

### Azure OpenAI metrikák megtekintése

Lépj az Azure Portál → Az OpenAI erőforrásod → Metrikák:
- Token-alapú kihasználtság
- HTTP kérés ráta
- Válaszidő
- Aktív tokenek

## Hibakeresés

### Probléma: Azure OpenAI aldoménnév ütközés

**Hibaüzenet:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Ok:**
Az előfizetésed/környezeted alapján generált aldoménnév már használatban van, valószínűleg egy korábbi telepítés után, amit nem töröltek teljesen.

**Megoldás:**
1. **Opció 1 - Használj más környezet nevet:**
   
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

2. **Opció 2 - Manuális telepítés Azure Portálon keresztül:**
   - Menj az Azure Portálra → Erőforrás létrehozása → Azure OpenAI
   - Válassz egy egyedi nevet az erőforrásnak
   - Telepítsd a következő modelleket:
     - **GPT-5.2**
     - **text-embedding-3-small** (RAG modulokhoz)
   - **Fontos:** Jegyezd fel a telepítési neveket – meg kell egyezniük a `.env` konfigurációval
   - A telepítés után az “API kulcsok és végpont” menüben szerezd meg a végpontot és API kulcsot
   - Hozz létre a projekt gyökerében egy `.env` fájlt a következőkkel:
     
     **Példa `.env` fájl:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Model telepítési név irányelvek:**
- Használj egyszerű, következetes neveket: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- A telepítési neveknek pontosan meg kell egyezniük a `.env` fájl konfigurációjával
- Gyakori hiba: a modell létrehozása más névvel, majd más név hivatkozása a kódban

### Probléma: GPT-5.2 nem elérhető a kiválasztott régióban

**Megoldás:**
- Válassz olyan régiót, ahol elérhető a GPT-5.2 (pl. eastus2)
- Elérhetőség ellenőrzése: https://learn.microsoft.com/azure/ai-services/openai/concepts/models



### Probléma: Kevés kvóta a telepítéshez

**Megoldás:**
1. Kérj kvótaemelést az Azure Portálon
2. Vagy használj kisebb kapacitást a `main.bicep` fájlban (pl. kapacitás: 10)

### Probléma: "Erőforrás nem található" helyi futtatásnál

**Megoldás:**
1. Ellenőrizd a telepítést: `azd env get-values`
2. Ellenőrizd, hogy a végpont és az API kulcs helyes-e
3. Győződj meg róla, hogy az erőforráscsoport létezik az Azure Portálon

### Probléma: Hitelesítés sikertelen

**Megoldás:**
- Ellenőrizd, hogy az `AZURE_OPENAI_API_KEY` helyesen van-e beállítva
- A kulcs formátuma 32 karakteres hexadecimális string legyen
- Ha szükséges, szerezz új kulcsot az Azure Portálról

### Telepítés sikertelen

**Probléma**: Az `azd provision` kvóta vagy kapacitás hibával leáll

**Megoldás**: 
1. Próbálj meg másik régiót használni – Lásd a [Azure régiók módosítása](../../../../01-introduction/infra) részt
2. Ellenőrizd, hogy az előfizetésed rendelkezik Azure OpenAI kvótával:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Az alkalmazás nem csatlakozik

**Probléma**: Java alkalmazás kapcsolódási hibákat mutat

**Megoldás**:
1. Ellenőrizd, hogy a környezeti változók exportálva vannak:
   
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

2. Ellenőrizd, hogy a végpont formátuma helyes (pl. `https://xxx.openai.azure.com`)
3. Győződj meg róla, hogy az API kulcs az Azure Portálon található elsődleges vagy másodlagos kulcs

**Probléma**: 401 Nem engedélyezett az Azure OpenAI használatánál

**Megoldás**:
1. Szerezz új API kulcsot az Azure Portál → Kulcsok és végpont
2. Újra exportáld az `AZURE_OPENAI_API_KEY` környezeti változót
3. Ellenőrizd, hogy a modell telepítés befejeződött-e (Azure Portálon)

### Teljesítmény problémák

**Probléma**: Lassú válaszidő

**Megoldás**:
1. Ellenőrizd az OpenAI token használatot és korlátozásokat az Azure Portál metrikáin
2. Növeld a TPM kapacitást, ha kifogytál a keretből
3. Fontold meg magasabb értelmezési szint (alacsony/közepes/magas) használatát

## Infrastruktúra frissítése

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

## Biztonsági ajánlások

1. **Soha ne tárold API kulcsokat a verziókövetésben** – Használj környezeti változókat
2. **Használj `.env` fájlokat helyileg** – Add hozzá `.gitignore`-hoz
3. **Rendszeresen forgasd a kulcsokat** – Új kulcsokat generálj az Azure Portálon
4. **Korlátozd a hozzáférést** – Azure RBAC használatával szabályozd, ki férhet hozzá az erőforrásokhoz
5. **Kísérd figyelemmel a használatot** – Állíts be költségértesítéseket az Azure Portálon

## További erőforrások

- [Azure OpenAI szolgáltatás dokumentáció](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 modell dokumentáció](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI dokumentáció](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep dokumentáció](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI hivatalos integráció](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Támogatás

Problémák esetén:
1. Ellenőrizd a fenti [hibakeresési részt](../../../../01-introduction/infra)
2. Nézd meg az Azure OpenAI szolgáltatás állapotát az Azure Portálon
3. Nyiss problémát a tárolóban

## Licenc

A részletekért lásd a gyökérbeli [LICENSE](../../../../LICENSE) fájlt.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Felelősségkizárás**:
Ez a dokumentum az AI fordító szolgáltatás, a [Co-op Translator](https://github.com/Azure/co-op-translator) használatával készült. Bár a pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások tartalmazhatnak hibákat vagy pontatlanságokat. Az eredeti dokumentum az anyanyelvén tekintendő hiteles forrásnak. Kritikus információk esetén szakmai emberi fordítást javaslunk. Nem vállalunk felelősséget az ebből a fordításból adódó félreértésekért vagy félreértelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->