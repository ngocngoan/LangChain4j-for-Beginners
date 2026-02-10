# Infrastruktur Azure untuk LangChain4j Memulakan

## Senarai Kandungan

- [Keperluan](../../../../01-introduction/infra)
- [Senibina](../../../../01-introduction/infra)
- [Sumber Dicipta](../../../../01-introduction/infra)
- [Mula Pantas](../../../../01-introduction/infra)
- [Konfigurasi](../../../../01-introduction/infra)
- [Arahan Pengurusan](../../../../01-introduction/infra)
- [Pengoptimuman Kos](../../../../01-introduction/infra)
- [Pemantauan](../../../../01-introduction/infra)
- [Penyelesaian Masalah](../../../../01-introduction/infra)
- [Mengemas kini Infrastruktur](../../../../01-introduction/infra)
- [Pembersihan](../../../../01-introduction/infra)
- [Struktur Fail](../../../../01-introduction/infra)
- [Syor Keselamatan](../../../../01-introduction/infra)
- [Sumber Tambahan](../../../../01-introduction/infra)

Direktori ini mengandungi infrastruktur Azure sebagai kod (IaC) menggunakan Bicep dan Azure Developer CLI (azd) untuk menyebarkan sumber Azure OpenAI.

## Keperluan

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (versi 2.50.0 atau lebih baru)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (versi 1.5.0 atau lebih baru)
- Langganan Azure dengan kebenaran untuk membuat sumber

## Senibina

**Persediaan Pembangunan Tempatan Mudah** - Sebarkan Azure OpenAI sahaja, jalankan semua aplikasi secara tempatan.

Infrastruktur menyebarkan sumber Azure berikut:

### Perkhidmatan AI
- **Azure OpenAI**: Perkhidmatan Kognitif dengan dua penyebaran model:
  - **gpt-5.2**: Model penyempurnaan sembang (kapasiti 20K TPM)
  - **text-embedding-3-small**: Model embedding untuk RAG (kapasiti 20K TPM)

### Pembangunan Tempatan
Semua aplikasi Spring Boot berjalan secara tempatan pada mesin anda:
- 01-introduction (port 8080)
- 02-prompt-engineering (port 8083)
- 03-rag (port 8081)
- 04-tools (port 8084)

## Sumber Dicipta

| Jenis Sumber | Corak Nama Sumber | Tujuan |
|--------------|-------------------|--------|
| Kumpulan Sumber | `rg-{environmentName}` | Mengandungi semua sumber |
| Azure OpenAI | `aoai-{resourceToken}` | Hos model AI |

> **Nota:** `{resourceToken}` adalah rentetan unik yang dijana daripada ID langganan, nama persekitaran, dan lokasi

## Mula Pantas

### 1. Sebarkan Azure OpenAI

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

Apabila diminta:
- Pilih langganan Azure anda
- Pilih lokasi (disyorkan: `eastus2` untuk ketersediaan GPT-5.2)
- Sahkan nama persekitaran (lalai: `langchain4j-dev`)

Ini akan mencipta:
- Sumber Azure OpenAI dengan GPT-5.2 dan text-embedding-3-small
- Butiran sambungan output

### 2. Dapatkan Butiran Sambungan

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Ini memaparkan:
- `AZURE_OPENAI_ENDPOINT`: URL titik akhir Azure OpenAI anda
- `AZURE_OPENAI_KEY`: Kunci API untuk pengesahan
- `AZURE_OPENAI_DEPLOYMENT`: Nama model sembang (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Nama model embedding

### 3. Jalankan Aplikasi Secara Tempatan

Arahan `azd up` secara automatik mencipta fail `.env` di direktori akar dengan semua pembolehubah persekitaran yang diperlukan.

**Disyorkan:** Mulakan semua aplikasi web:

**Bash:**
```bash
# Dari direktori akar
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# Dari direktori akar
cd ../..
.\start-all.ps1
```

Atau mulakan satu modul sahaja:

**Bash:**
```bash
# Contoh: Mula hanya modul pengenalan
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Contoh: Mulakan hanya modul pengenalan
cd ../01-introduction
.\start.ps1
```

Kedua-dua skrip memuatkan pembolehubah persekitaran secara automatik dari fail `.env` akar yang dicipta oleh `azd up`.

## Konfigurasi

### Mengubahsuai Penyebaran Model

Untuk menukar penyebaran model, sunting `infra/main.bicep` dan ubah parameter `openAiDeployments`:

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

Model dan versi yang tersedia: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Menukar Wilayah Azure

Untuk menyebar di wilayah lain, sunting `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Semak ketersediaan GPT-5.2: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Untuk mengemas kini infrastruktur selepas membuat perubahan pada fail Bicep:

**Bash:**
```bash
# Bina semula templat ARM
az bicep build --file infra/main.bicep

# Pratonton perubahan
azd provision --preview

# Terapkan perubahan
azd provision
```

**PowerShell:**
```powershell
# Bina semula templat ARM
az bicep build --file infra/main.bicep

# Pratonton perubahan
azd provision --preview

# Terapkan perubahan
azd provision
```

## Pembersihan

Untuk memadam semua sumber:

**Bash:**
```bash
# Padam semua sumber
azd down

# Padam semuanya termasuk persekitaran
azd down --purge
```

**PowerShell:**
```powershell
# Padam semua sumber
azd down

# Padam semua termasuk persekitaran
azd down --purge
```

**Amaran**: Ini akan memadamkan semua sumber Azure secara kekal.

## Struktur Fail

## Pengoptimuman Kos

### Pembangunan/Pengujian
Untuk persekitaran dev/test, anda boleh mengurangkan kos:
- Guna tier Standard (S0) untuk Azure OpenAI
- Tetapkan kapasiti lebih rendah (10K TPM bukan 20K) dalam `infra/core/ai/cognitiveservices.bicep`
- Padam sumber apabila tidak digunakan: `azd down`

### Pengeluaran
Untuk pengeluaran:
- Tingkatkan kapasiti OpenAI berdasarkan penggunaan (50K+ TPM)
- Hidupkan zona redundansi untuk kebolehpercayaan lebih tinggi
- Laksanakan pemantauan dan amaran kos yang sesuai

### Anggaran Kos
- Azure OpenAI: Bayar per token (masukan + keluaran)
- GPT-5.2: ~$3-5 setiap 1 juta token (semak harga terkini)
- text-embedding-3-small: ~$0.02 setiap 1 juta token

Kalkulator harga: https://azure.microsoft.com/pricing/calculator/

## Pemantauan

### Melihat Metrik Azure OpenAI

Pergi ke Azure Portal → Sumber OpenAI anda → Metrik:
- Penggunaan Berasaskan Token
- Kadar Permintaan HTTP
- Masa Balas
- Token Aktif

## Penyelesaian Masalah

### Masalah: Konflik nama subdomain Azure OpenAI

**Mesej Ralat:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Sebab:**
Nama subdomain yang dijana dari langganan/persekitaran anda sudah digunakan, mungkin dari penyebaran sebelumnya yang tidak dibersihkan sepenuhnya.

**Penyelesaian:**
1. **Pilihan 1 - Guna nama persekitaran berbeza:**
   
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

2. **Pilihan 2 - Penyebaran manual melalui Azure Portal:**
   - Pergi ke Azure Portal → Cipta sumber → Azure OpenAI
   - Pilih nama unik untuk sumber anda
   - Sebarkan model berikut:
     - **GPT-5.2**
     - **text-embedding-3-small** (untuk modul RAG)
   - **Penting:** Nota nama penyebaran anda - mesti sepadan dengan konfigurasi `.env`
   - Selepas penyebaran, dapatkan titik akhir dan kunci API dari "Keys and Endpoint"
   - Cipta fail `.env` di akar projek dengan:
     
     **Contoh fail `.env`:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Garisan Panduan Penamaan Penyebaran Model:**
- Gunakan nama ringkas dan konsisten: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Nama penyebaran mesti sepadan dengan tepat apa yang anda konfigurasikan dalam `.env`
- Kesalahan biasa: Mencipta model dengan satu nama tetapi merujuk nama yang berbeza dalam kod

### Masalah: GPT-5.2 tidak tersedia di wilayah yang dipilih

**Penyelesaian:**
- Pilih wilayah dengan akses GPT-5.2 (contoh: eastus2)
- Semak ketersediaan: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Masalah: Kuota tidak mencukupi untuk penyebaran

**Penyelesaian:**
1. Mohon kenaikan kuota dalam Azure Portal
2. Atau guna kapasiti lebih rendah dalam `main.bicep` (contoh: kapasiti: 10)

### Masalah: "Resource not found" ketika menjalankan secara tempatan

**Penyelesaian:**
1. Sahkan penyebaran: `azd env get-values`
2. Semak titik akhir dan kunci adalah betul
3. Pastikan kumpulan sumber wujud di Azure Portal

### Masalah: Pengesahan gagal

**Penyelesaian:**
- Semak `AZURE_OPENAI_API_KEY` sudah diset dengan betul
- Format kunci harus rentetan heksadesimal 32 aksara
- Dapatkan kunci baru dari Azure Portal jika perlu

### Penyebaran Gagal

**Masalah**: `azd provision` gagal dengan ralat kuota atau kapasiti

**Penyelesaian**: 
1. Cuba wilayah berbeza - Lihat bahagian [Menukar Wilayah Azure](../../../../01-introduction/infra) untuk cara konfigurasi wilayah
2. Semak langganan anda ada kuota Azure OpenAI:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Aplikasi Tidak Berhubung

**Masalah**: Aplikasi Java menunjukkan ralat sambungan

**Penyelesaian**:
1. Sahkan pembolehubah persekitaran sudah dieksport:
   
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

2. Semak format titik akhir adalah betul (hendaklah `https://xxx.openai.azure.com`)
3. Sahkan kunci API ialah kunci utama atau sekunder dari Azure Portal

**Masalah**: 401 Tidak Sah dari Azure OpenAI

**Penyelesaian**:
1. Dapatkan kunci API baru dari Azure Portal → Keys and Endpoint
2. Eksport semula pembolehubah persekitaran `AZURE_OPENAI_API_KEY`
3. Pastikan penyebaran model lengkap (semak Azure Portal)

### Masalah Prestasi

**Masalah**: Masa balas lambat

**Penyelesaian**:
1. Semak penggunaan token OpenAI dan penghadang di metrik Azure Portal
2. Tingkatkan kapasiti TPM jika anda mencapai had
3. Pertimbangkan menggunakan tahap usaha penalaran yang lebih tinggi (rendah/sederhana/tinggi)

## Mengemas kini Infrastruktur

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

## Syor Keselamatan

1. **Jangan pernah komit kunci API** - Gunakan pembolehubah persekitaran
2. **Gunakan fail .env secara tempatan** - Tambah `.env` ke `.gitignore`
3. **Putar kunci secara berkala** - Jana kunci baru di Azure Portal
4. **Hadkan akses** - Gunakan Azure RBAC untuk kawal siapa boleh akses sumber
5. **Pantau penggunaan** - Tetapkan amaran kos dalam Azure Portal

## Sumber Tambahan

- [Dokumentasi Perkhidmatan Azure OpenAI](https://learn.microsoft.com/azure/ai-services/openai/)
- [Dokumentasi Model GPT-5.2](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Dokumentasi Azure Developer CLI](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Dokumentasi Bicep](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [Integrasi Rasmi LangChain4j OpenAI](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Sokongan

Untuk isu:
1. Semak [bahagian penyelesaian masalah](../../../../01-introduction/infra) di atas
2. Semak kesihatan perkhidmatan Azure OpenAI di Azure Portal
3. Buka isu dalam repositori

## Lesen

Lihat fail [LICENSE](../../../../LICENSE) di akar untuk butiran.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila ambil maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya hendaklah dianggap sebagai sumber yang sahih. Untuk maklumat yang kritikal, terjemahan profesional oleh manusia adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->