# Azure infrastruktura za LangChain4j Začetek

## Kazalo

- [Pogoji](../../../../01-introduction/infra)
- [Arhitektura](../../../../01-introduction/infra)
- [Ustvarjeni viri](../../../../01-introduction/infra)
- [Hiter začetek](../../../../01-introduction/infra)
- [Konfiguracija](../../../../01-introduction/infra)
- [Ukazi za upravljanje](../../../../01-introduction/infra)
- [Optimizacija stroškov](../../../../01-introduction/infra)
- [Nadzor](../../../../01-introduction/infra)
- [Odpravljanje težav](../../../../01-introduction/infra)
- [Posodobitev infrastrukture](../../../../01-introduction/infra)
- [Čiščenje](../../../../01-introduction/infra)
- [Struktura datotek](../../../../01-introduction/infra)
- [Priporočila za varnost](../../../../01-introduction/infra)
- [Dodatni viri](../../../../01-introduction/infra)

Ta imenik vsebuje Azure infrastrukturo kot kodo (IaC) z uporabo Bicep in Azure Developer CLI (azd) za nameščanje Azure OpenAI virov.

## Pogoji

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (različica 2.50.0 ali novejša)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (različica 1.5.0 ali novejša)
- Azure naročnina s pravicami za ustvarjanje virov

## Arhitektura

**Poenostavljena nastavitev za lokalni razvoj** – Nameščanje samo Azure OpenAI, vsi programi tečejo lokalno.

Infrastruktura namešča naslednje Azure vire:

### AI storitve
- **Azure OpenAI**: Kognitivne storitve z dvema modeloma za nameščanje:
  - **gpt-5.2**: model za klepet s končnim izhodom (kapaciteta 20K TPM)
  - **text-embedding-3-small**: model za vgradnjo za RAG (kapaciteta 20K TPM)

### Lokalni razvoj
Vse Spring Boot aplikacije tečejo lokalno na vašem računalniku:
- 01-introduction (vrata 8080)
- 02-prompt-engineering (vrata 8083)
- 03-rag (vrata 8081)
- 04-tools (vrata 8084)

## Ustvarjeni viri

| Vrsta vira | Vzorec imena vira | Namen |
|------------|-------------------|-------|
| Skupina virov | `rg-{environmentName}` | Vsebuje vse vire |
| Azure OpenAI | `aoai-{resourceToken}` | Gostovanje AI modela |

> **Opomba:** `{resourceToken}` je edinstvena niz, ustvarjen iz ID-ja naročnine, imena okolja in lokacije

## Hiter začetek

### 1. Namestitev Azure OpenAI

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
  
Ko vas sistem pozove:  
- Izberite svojo Azure naročnino  
- Izberite lokacijo (priporočeno: `eastus2` za dostopnost GPT-5.2)  
- Potrdite ime okolja (privzeto: `langchain4j-dev`)  

To bo ustvarilo:  
- Azure OpenAI vir z GPT-5.2 in text-embedding-3-small  
- Izpis podrobnosti povezave  

### 2. Pridobitev podrobnosti povezave

**Bash:**
```bash
azd env get-values
```
  
**PowerShell:**
```powershell
azd env get-values
```
  
Prikazuje:  
- `AZURE_OPENAI_ENDPOINT`: URL vaše Azure OpenAI končne točke  
- `AZURE_OPENAI_KEY`: API ključ za avtentikacijo  
- `AZURE_OPENAI_DEPLOYMENT`: ime klepetalnega modela (gpt-5.2)  
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: ime vgradbenega modela  

### 3. Zagon aplikacij lokalno

Ukaz `azd up` samodejno ustvari `.env` datoteko v korenskem imeniku z vsemi potrebnimi okoljskimi spremenljivkami.

**Priporočeno:** Zaženite vse spletne aplikacije:

**Bash:**
```bash
# Iz korenske mape
cd ../..
./start-all.sh
```
  
**PowerShell:**
```powershell
# Iz korenskega imenika
cd ../..
.\start-all.ps1
```
  
Ali zaženite posamezni modul:

**Bash:**
```bash
# Primer: Zaženite samo modul uvoda
cd ../01-introduction
./start.sh
```
  
**PowerShell:**
```powershell
# Primer: Zaženite samo modul uvoda
cd ../01-introduction
.\start.ps1
```
  
Oba skripta samodejno naložita okoljske spremenljivke iz korenske `.env` datoteke, ki jo ustvari `azd up`.

## Konfiguracija

### Prilagoditev namestitev modelov

Za spremembo namestitev modelov uredite `infra/main.bicep` in po potrebi spremenite parameter `openAiDeployments`:

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
  
Razpoložljivi modeli in različice: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Spreminjanje Azure regij

Za nameščanje v drugi regiji uredite `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```
  
Preverite razpoložljivost GPT-5.2: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Za posodobitev infrastrukture po spremembah Bicep datotek:

**Bash:**
```bash
# Znova sestavi ARM predlogo
az bicep build --file infra/main.bicep

# Predogled sprememb
azd provision --preview

# Uveljavi spremembe
azd provision
```
  
**PowerShell:**
```powershell
# Ponovno zgradi ARM predlogo
az bicep build --file infra/main.bicep

# Predogled sprememb
azd provision --preview

# Uporabi spremembe
azd provision
```
  
## Čiščenje

Za izbris vseh virov:

**Bash:**
```bash
# Izbriši vse vire
azd down

# Izbriši vse, vključno z okoljem
azd down --purge
```
  
**PowerShell:**
```powershell
# Izbriši vse vire
azd down

# Izbriši vse, vključno z okoljem
azd down --purge
```
  
**Opozorilo**: S tem boste trajno izbrisali vse Azure vire.

## Struktura datotek

## Optimizacija stroškov

### Razvoj/Testiranje
Za razvojna/testna okolja lahko znižate stroške:  
- Uporabite Standardni nivo (S0) za Azure OpenAI  
- Nastavite manjšo kapaciteto (10K TPM namesto 20K) v `infra/core/ai/cognitiveservices.bicep`  
- Izbrišite vire, ko niso v rabi: `azd down`

### Produkcija
Za produkcijo:  
- Povečajte kapaciteto OpenAI glede na uporabo (50K+ TPM)  
- Omogočite conično redundanco za večjo razpoložljivost  
- Implementirajte ustrezno spremljanje in opozorila o stroških

### Ocena stroškov
- Azure OpenAI: Plačilo na token (vhod + izhod)  
- GPT-5.2: približno 3–5 USD na 1M tokenov (preverite trenutno ceno)  
- text-embedding-3-small: približno 0,02 USD na 1M tokenov

Kalkulator cen: https://azure.microsoft.com/pricing/calculator/

## Nadzor

### Ogled metrik Azure OpenAI

Pojdite v Azure Portal → Vaš OpenAI vir → Metrike:  
- Uporaba na osnovi tokenov  
- Hitrost HTTP zahtev  
- Čas odziva  
- Aktivni tokeni

## Odpravljanje težav

### Težava: konflikt imena poddomene Azure OpenAI

**Sporočilo o napaki:**  
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```
  
**Vzrok:**  
Ime poddomene, ustvarjeno iz vaše naročnine/okolja, je že v uporabi, verjetno zaradi prejšnje namestitve, ki ni bila popolnoma izbrisana.

**Rešitev:**  
1. **Možnost 1 - Uporabite drugačno ime okolja:**  
   
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
  
2. **Možnost 2 - Ročna namestitev preko Azure portala:**  
   - Pojdite v Azure Portal → Ustvari vir → Azure OpenAI  
   - Izberite edinstveno ime za svoj vir  
   - Namestite naslednje modele:  
     - **GPT-5.2**  
     - **text-embedding-3-small** (za RAG module)  
   - **Pomembno:** Zabeležite si imena namestitev – morajo biti enaka konfiguraciji v `.env`  
   - Po namestitvi pridobite svoj endpoint in API ključ iz "Keys and Endpoint"  
   - Ustvarite `.env` datoteko v korenu projekta z:  
     
     **Primer `.env` datoteke:**  
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```
  
**Smernice za poimenovanje namestitev modelov:**  
- Uporabljajte preprosta, dosledna imena: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`  
- Imena namestitev morajo natančno ustrezati tistim, ki jih nastavite v `.env`  
- Pogosta napaka: Ustvariti model z enim imenom, a v kodi uporabiti drugo

### Težava: GPT-5.2 ni na voljo v izbrani regiji

**Rešitev:**  
- Izberite regijo z dostopom do GPT-5.2 (npr. eastus2)  
- Preverite razpoložljivost: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Težava: Nezadostna kvota za namestitev

**Rešitev:**  
1. Zahtevajte povečanje kvote v Azure portalu  
2. Ali uporabite manjšo kapaciteto v `main.bicep` (npr. capacity: 10)

### Težava: "Resource not found" pri lokalnem zagonu

**Rešitev:**  
1. Preverite namestitev: `azd env get-values`  
2. Preverite, da sta endpoint in ključ pravilna  
3. Zagotovite, da skupina virov obstaja v Azure portalu

### Težava: Avtentikacija ni uspela

**Rešitev:**  
- Preverite, da je `AZURE_OPENAI_API_KEY` pravilno nastavljen  
- Ključ naj ima 32-mestno številsko-šestnajstiško obliko  
- Pridobite nov ključ iz Azure portala, če je potrebno

### Namestitev spodleti

**Težava:** `azd provision` spodleti zaradi napak s kvoto ali kapaciteto

**Rešitev:**  
1. Poskusite drugo regijo - glejte razdelek [Spreminjanje Azure regij](../../../../01-introduction/infra) za navodila  
2. Preverite, ali imate kvoto Azure OpenAI:  
   
   **Bash:**  
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
     
   **PowerShell:**  
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```
  
### Aplikacija se ne povezuje

**Težava:** Java aplikacija prikazuje napake povezave

**Rešitev:**  
1. Preverite, da so okoljske spremenljivke izvozene:  
   
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
  
2. Preverite, da je format endpointa pravilen (mora biti `https://xxx.openai.azure.com`)  
3. Preverite, da je API ključ primarni ali sekundarni ključ iz Azure portala

**Težava:** 401 Neavtoriziran iz Azure OpenAI

**Rešitev:**  
1. Pridobite svež API ključ v Azure portalu → Keys and Endpoint  
2. Ponovno izvozite okoljsko spremenljivko `AZURE_OPENAI_API_KEY`  
3. Zagotovite, da so namestitve modelov dokončane (preverite Azure portal)

### Težave s hitrostjo delovanja

**Težava:** Počasni časi odziva

**Rešitev:**  
1. Preverite uporabo tokenov in omejevanje v metrikah Azure portala  
2. Povečajte TPM kapaciteto, če dosežete omejitve  
3. Razmislite o uporabi višje ravni napora za sklepanja (nizka/srednja/visoka)

## Posodabljanje infrastrukture

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
  
## Priporočila za varnost

1. **Nikoli ne vključujte API ključev v repozitorij** – uporabljajte okoljske spremenljivke  
2. **Uporabljajte .env datoteke lokalno** – dodajte `.env` v `.gitignore`  
3. **Redno menjajte ključe** – ustvarjajte nove ključe v Azure portalu  
4. **Omejite dostop** – uporabljajte Azure RBAC za nadzor dostopa do virov  
5. **Spremljajte uporabo** – nastavite opozorila o stroških v Azure portalu

## Dodatni viri

- [Dokumentacija za Azure OpenAI storitev](https://learn.microsoft.com/azure/ai-services/openai/)
- [Dokumentacija modela GPT-5.2](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Dokumentacija Azure Developer CLI](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Dokumentacija za Bicep](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [Uradna integracija LangChain4j OpenAI](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Podpora

Za težave:  
1. Preverite zgornji [razdelek za odpravljanje težav](../../../../01-introduction/infra)  
2. Preglejte stanje Azure OpenAI storitve v Azure portalu  
3. Odprite težavo v repozitoriju

## Licenca

Podrobnosti so v datoteki [LICENSE](../../../../LICENSE) v korenu.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo AI prevajalske storitve [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas prosimo, da upoštevate, da lahko avtomatizirani prevodi vsebujejo napake ali netočnosti. Izvirni dokument v njegovem izvirnem jeziku velja kot avtoritativni vir. Za pomembne informacije priporočamo strokovni človeški prevod. Nismo odgovorni za morebitna nesporazume ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->