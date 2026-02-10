# Azure-infrastruktuuri LangChain4j:n Aloittaminen

## Sisällysluettelo

- [Esivaatimukset](../../../../01-introduction/infra)
- [Arkkitehtuuri](../../../../01-introduction/infra)
- [Luodut resurssit](../../../../01-introduction/infra)
- [Pika-aloitus](../../../../01-introduction/infra)
- [Konfigurointi](../../../../01-introduction/infra)
- [Hallintakomennot](../../../../01-introduction/infra)
- [Kustannusten optimointi](../../../../01-introduction/infra)
- [Valvonta](../../../../01-introduction/infra)
- [Vianmääritys](../../../../01-introduction/infra)
- [Infrastruktuurin päivittäminen](../../../../01-introduction/infra)
- [Siivous](../../../../01-introduction/infra)
- [Tiedostorakenne](../../../../01-introduction/infra)
- [Turvasuositukset](../../../../01-introduction/infra)
- [Lisäresurssit](../../../../01-introduction/infra)

Tämä hakemisto sisältää Azure-infrastruktuurin koodina (IaC) Bicepin ja Azure Developer CLI:n (azd) avulla Azure OpenAI -resurssien käyttöönottoa varten.

## Esivaatimukset

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (versio 2.50.0 tai uudempi)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (versio 1.5.0 tai uudempi)
- Azure-tilaus, jolla on oikeudet resurssien luomiseen

## Arkkitehtuuri

**Yksinkertaistettu paikallinen kehitysympäristö** – Käytetään vain Azure OpenAI:ta, kaikki sovellukset ajetaan paikallisesti.

Infrastruktuuri ottaa käyttöön seuraavat Azure-resurssit:

### AI-palvelut
- **Azure OpenAI**: Tietojenkäsittelypalvelu, jossa kaksi mallin käyttöönottoa:
  - **gpt-5.2**: Chat-vastausten malli (20K TPM kapasiteetti)
  - **text-embedding-3-small**: RAG-käyttöön upotusten malli (20K TPM kapasiteetti)

### Paikallinen kehitys
Kaikki Spring Boot -sovellukset ajetaan koneellasi paikallisesti:
- 01-introduction (portti 8080)
- 02-prompt-engineering (portti 8083)
- 03-rag (portti 8081)
- 04-tools (portti 8084)

## Luodut resurssit

| Resurssityyppi | Resurssin nimi | Tarkoitus |
|--------------|----------------------|---------|
| Resurssiryhmä | `rg-{environmentName}` | Sisältää kaikki resurssit |
| Azure OpenAI | `aoai-{resourceToken}` | AI-mallin hosting |

> **Huom:** `{resourceToken}` on uniikki merkkijono, joka muodostetaan tilaus-ID:stä, ympäristön nimestä ja sijainnista

## Pika-aloitus

### 1. Ota Azure OpenAI käyttöön

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

Kun sinua kehotetaan:
- Valitse Azure-tilauksesi
- Valitse sijainti (suositus: `eastus2` GPT-5.2 saatavuuden vuoksi)
- Vahvista ympäristön nimi (oletus: `langchain4j-dev`)

Tämä luo:
- Azure OpenAI -resurssin GPT-5.2:lla ja text-embedding-3-small -malleilla
- Näyttää liitettävyystiedot

### 2. Hanki liitetyiedot

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Näyttää:
- `AZURE_OPENAI_ENDPOINT`: Azure OpenAI -rajapinnan URL-osoite
- `AZURE_OPENAI_KEY`: API-avain autentikointiin
- `AZURE_OPENAI_DEPLOYMENT`: Chat-mallin nimi (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Upotusmallin nimi

### 3. Käynnistä sovellukset paikallisesti

`azd up` -komento luo automaattisesti `.env`-tiedoston juurihakemistoon kaikki tarvittavat ympäristömuuttujat sisältäen.

**Suositus:** Käynnistä kaikki web-sovellukset:

**Bash:**
```bash
# Juurihakemistosta
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# Juurihakemistosta
cd ../..
.\start-all.ps1
```

Tai käynnistä yksittäinen moduuli:

**Bash:**
```bash
# Esimerkki: Käynnistä vain esittelymoduuli
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Esimerkki: Käynnistä vain johdantomoduuli
cd ../01-introduction
.\start.ps1
```

Molemmat skriptit lataavat automaattisesti ympäristömuuttujat juurihakemistossa olevasta `azd up` -komennolla luodusta `.env`-tiedostosta.

## Konfigurointi

### Mallien käyttöönoton räätälöinti

Mallien käyttöönoton muuttamiseksi muokkaa `infra/main.bicep` -tiedostoa ja muuta `openAiDeployments`-parametria:

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

Saatavilla olevat mallit ja versiot: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Azure-alueen vaihtaminen

Jos haluat ottaa infrastruktuurin käyttöön eri alueella, muokkaa `infra/main.bicep` -tiedostoa:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Tarkista GPT-5.2 saatavuus: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Infrastruktuurin päivitys Bicep-tiedostoihin tehtyjen muutosten jälkeen:

**Bash:**
```bash
# Rakenna ARM-malli uudelleen
az bicep build --file infra/main.bicep

# Esikatsele muutokset
azd provision --preview

# Käytä muutokset
azd provision
```

**PowerShell:**
```powershell
# Kokoa ARM-malli uudelleen
az bicep build --file infra/main.bicep

# Tarkastele muutoksia
azd provision --preview

# Käytä muutokset
azd provision
```

## Siivous

Poistaaksesi kaikki resurssit:

**Bash:**
```bash
# Poista kaikki resurssit
azd down

# Poista kaikki mukaan lukien ympäristö
azd down --purge
```

**PowerShell:**
```powershell
# Poista kaikki resurssit
azd down

# Poista kaikki mukaan lukien ympäristö
azd down --purge
```

**Varoitus:** Tämä poistaa pysyvästi kaikki Azure-resurssit.

## Tiedostorakenne

## Kustannusten optimointi

### Kehitys/Testaus
Kehitys- ja testausympäristöissä kustannuksia voi alentaa:
- Käytetään Standard-tasoa (S0) Azure OpenAI:ssa
- Aseta pienempi kapasiteetti (10K TPM 20K sijaan) tiedostossa `infra/core/ai/cognitiveservices.bicep`
- Poista resurssit käytön ulkopuolella: `azd down`

### Tuotanto
Tuotannossa:
- Nosta OpenAI-kapasiteettia käytön mukaan (50K+ TPM)
- Ota käyttöön aluetuki saavutettavuuden parantamiseksi
- Toteuta asianmukainen valvonta ja kustannushälytykset

### Kustannusarvio
- Azure OpenAI: Maksu token-pohjaisesti (syöte + tuloste)
- GPT-5.2: n. $3-5 per 1M tokenia (tarkista ajantasainen hinnoittelu)
- text-embedding-3-small: n. $0.02 per 1M tokenia

Hinnoittelulaskuri: https://azure.microsoft.com/pricing/calculator/

## Valvonta

### Azure OpenAI -mittareiden tarkastelu

Siirry Azure-portaaliin → OpenAI-resurssi → Mittarit:
- Tokenien käyttö
- HTTP-pyyntöjen määrä
- Vastausaika
- Aktiiviset tokenit

## Vianmääritys

### Ongelma: Azure OpenAI aliavain-nimen konflikti

**Virheilmoitus:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Syy:**
Aliavaimen nimi, joka generoidaan tilauksen/ympäristön perusteella, on jo käytössä, mahdollisesti aiemmasta käyttöönotosta, jota ei ole kokonaan poistettu.

**Ratkaisu:**
1. **Vaihtoehto 1 – Käytä eri ympäristön nimeä:**

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

2. **Vaihtoehto 2 – Manuaalinen käyttöönotto Azure-portaalin kautta:**
   - Siirry Azure-portaaliin → Luo resurssi → Azure OpenAI
   - Valitse resurssillesi uniikki nimi
   - Ota käyttöön seuraavat mallit:
     - **GPT-5.2**
     - **text-embedding-3-small** (RAG-moduuleille)
   - **Tärkeää:** Merkitse ylös käyttöönoton nimet – niiden on vastattava `.env`-konfiguraatiota
   - Käyttöönoton jälkeen hae päätepiste ja API-avain kohdasta "Keys and Endpoint"
   - Luo projektin juureen `.env`-tiedosto, jossa on:

     **Esimerkki `.env`-tiedostosta:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Mallin käyttöönoton nimeämiskäytännöt:**
- Käytä yksinkertaisia, johdonmukaisia nimiä: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Käyttöönoton nimien on vastattava tarkalleen `.env`-tiedostoa
- Yleinen virhe: luodaan malli yhdellä nimellä, mutta viitataan koodissa toiseen nimeen

### Ongelma: GPT-5.2 ei ole käytettävissä valitulla alueella

**Ratkaisu:**
- Valitse alue, jolla on GPT-5.2-tuki (esim. eastus2)
- Tarkista saatavuus: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Ongelma: Ei riittävästi quota-kapasiteettia käyttöönottoon

**Ratkaisu:**
1. Pyydä quotalisäystä Azure-portalissa
2. Tai käytä pienempää kapasiteettia `main.bicep` tiedostossa (esim. capacity: 10)

### Ongelma: "Resource not found" paikallisesti ajettaessa

**Ratkaisu:**
1. Varmista käyttöönotto: `azd env get-values`
2. Tarkista, että päätepiste ja avain ovat oikein
3. Varmista, että resurssiryhmä on olemassa Azure-portaalissa

### Ongelma: Todennus epäonnistui

**Ratkaisu:**
- Varmista, että `AZURE_OPENAI_API_KEY` on asetettu oikein
- Avain on 32-merkkinen heksadesimaalijono
- Hanki uusi avain tarvittaessa Azure-portaalista

### Käyttöönotto epäonnistuu

**Ongelma:** `azd provision` epäonnistuu quota- tai kapasiteettivirheen takia

**Ratkaisu:** 
1. Kokeile eri aluetta – Katso [Azure-alueen vaihtaminen](../../../../01-introduction/infra) -osio
2. Tarkista, että tilauksellasi on Azure OpenAI -quota:

   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Sovellus ei yhdistä

**Ongelma:** Java-sovellus näyttää yhteysvirheitä

**Ratkaisu:**
1. Varmista ympäristömuuttujien vienti:

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

2. Tarkista päätepisteen muoto (pitää olla `https://xxx.openai.azure.com`)
3. Varmista, että käytössä on Azure-portaalin ensisijainen tai toissijainen API-avain

**Ongelma:** 401 Unauthorized Azure OpenAI:lta

**Ratkaisu:**
1. Hanki uusi API-avain Azure-portaalista → Keys and Endpoint
2. Vie uudelleen `AZURE_OPENAI_API_KEY` ympäristömuuttuja
3. Varmista, että mallien käyttöönotto on valmis (tarkista Azure-portaalista)

### Suorituskykyongelmat

**Ongelma:** Hitaat vastausajat

**Ratkaisu:**
1. Tarkista OpenAI-tokenien käyttö ja rajoitukset Azure-portaalin mittareista
2. Nosta TPM-kapasiteettia, jos saavutat rajoja
3. Käytä korkeampaa päättelytason asetusta (low/medium/high)

## Infrastruktuurin päivittäminen

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

## Turvasuositukset

1. **Älä koskaan tallenna API-avaimia versionhallintaan** – Käytä ympäristömuuttujia
2. **Käytä .env-tiedostoja paikallisesti** – Lisää `.env` `.gitignore`-tiedostoon
3. **Kierrätä avaimia säännöllisesti** – Luo uudet avaimet Azure-portaalissa
4. **Rajoita pääsyä** – Käytä Azure RBAC:ia resurssien käyttöoikeuksien hallintaan
5. **Valvo käyttöä** – Luo kustannushälytyksiä Azure-portaaliin

## Lisäresurssit

- [Azure OpenAI -palvelun dokumentaatio](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 mallin dokumentaatio](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI:n dokumentaatio](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep-dokumentaatio](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI virallinen integraatio](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Tuki

Ongelmatilanteissa:
1. Tarkista yllä oleva [vianmäärityskohdasta](../../../../01-introduction/infra)
2. Tarkista Azure OpenAI -palvelun tila Azure-portaalissa
3. Avaa tiketti tässä repositoriossa

## Lisenssi

Katso juurikansion [LICENSE](../../../../LICENSE) tiedosto lisätietoja varten.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, on hyvä huomioida, että automaattikäännöksissä voi esiintyä virheitä tai epätarkkuuksia. Alkuperäistä asiakirjaa sen alkuperäisellä kielellä tulee pitää virallisena lähteenä. Tärkeissä asioissa suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä aiheutuvista väärinymmärryksistä tai tulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->