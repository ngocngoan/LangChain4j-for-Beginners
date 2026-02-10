# Infrastruktur Azure untuk LangChain4j Memulai

## Daftar Isi

- [Prasyarat](../../../../01-introduction/infra)
- [Arsitektur](../../../../01-introduction/infra)
- [Sumber Daya yang Dibuat](../../../../01-introduction/infra)
- [Memulai Cepat](../../../../01-introduction/infra)
- [Konfigurasi](../../../../01-introduction/infra)
- [Perintah Manajemen](../../../../01-introduction/infra)
- [Optimisasi Biaya](../../../../01-introduction/infra)
- [Pemantauan](../../../../01-introduction/infra)
- [Pemecahan Masalah](../../../../01-introduction/infra)
- [Memperbarui Infrastruktur](../../../../01-introduction/infra)
- [Pembersihan](../../../../01-introduction/infra)
- [Struktur Berkas](../../../../01-introduction/infra)
- [Rekomendasi Keamanan](../../../../01-introduction/infra)
- [Sumber Daya Tambahan](../../../../01-introduction/infra)

Direktori ini berisi infrastruktur Azure sebagai kode (IaC) menggunakan Bicep dan Azure Developer CLI (azd) untuk penerapan sumber daya Azure OpenAI.

## Prasyarat

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (versi 2.50.0 atau lebih baru)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (versi 1.5.0 atau lebih baru)
- Langganan Azure dengan izin untuk membuat sumber daya

## Arsitektur

**Pengaturan Pengembangan Lokal yang Disederhanakan** - Terapkan hanya Azure OpenAI, jalankan semua aplikasi secara lokal.

Infrastruktur menerapkan sumber daya Azure berikut:

### Layanan AI
- **Azure OpenAI**: Cognitive Services dengan dua penerapan model:
  - **gpt-5.2**: Model penyelesaian chat (kapasitas 20K TPM)
  - **text-embedding-3-small**: Model embedding untuk RAG (kapasitas 20K TPM)

### Pengembangan Lokal
Semua aplikasi Spring Boot dijalankan secara lokal di mesin Anda:
- 01-introduction (port 8080)
- 02-prompt-engineering (port 8083)
- 03-rag (port 8081)
- 04-tools (port 8084)

## Sumber Daya yang Dibuat

| Jenis Sumber Daya | Pola Nama Sumber Daya | Tujuan |
|--------------|----------------------|---------|
| Grup Sumber Daya | `rg-{environmentName}` | Menampung semua sumber daya |
| Azure OpenAI | `aoai-{resourceToken}` | Hosting model AI |

> **Catatan:** `{resourceToken}` adalah string unik yang dibuat dari ID langganan, nama lingkungan, dan lokasi

## Memulai Cepat

### 1. Terapkan Azure OpenAI

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

Saat diminta:
- Pilih langganan Azure Anda
- Pilih lokasi (disarankan: `eastus2` untuk ketersediaan GPT-5.2)
- Konfirmasi nama lingkungan (default: `langchain4j-dev`)

Ini akan membuat:
- Sumber daya Azure OpenAI dengan GPT-5.2 dan text-embedding-3-small
- Detail koneksi output

### 2. Dapatkan Detail Koneksi

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Ini menampilkan:
- `AZURE_OPENAI_ENDPOINT`: URL endpoint Azure OpenAI Anda
- `AZURE_OPENAI_KEY`: Kunci API untuk autentikasi
- `AZURE_OPENAI_DEPLOYMENT`: Nama model chat (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Nama model embedding

### 3. Jalankan Aplikasi Secara Lokal

Perintah `azd up` secara otomatis membuat file `.env` di direktori root dengan semua variabel lingkungan yang diperlukan.

**Disarankan:** Mulai semua aplikasi web:

**Bash:**
```bash
# Dari direktori akar
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# Dari direktori root
cd ../..
.\start-all.ps1
```

Atau mulai modul tunggal:

**Bash:**
```bash
# Contoh: Mulai hanya modul pengantar
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Contoh: Mulai hanya modul pengantar
cd ../01-introduction
.\start.ps1
```

Kedua skrip secara otomatis memuat variabel lingkungan dari file `.env` root yang dibuat oleh `azd up`.

## Konfigurasi

### Kustomisasi Penerapan Model

Untuk mengubah penerapan model, edit `infra/main.bicep` dan ubah parameter `openAiDeployments`:

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

### Mengubah Wilayah Azure

Untuk menerapkan di wilayah lain, edit `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Periksa ketersediaan GPT-5.2: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Untuk memperbarui infrastruktur setelah mengubah file Bicep:

**Bash:**
```bash
# Bangun ulang template ARM
az bicep build --file infra/main.bicep

# Pratinjau perubahan
azd provision --preview

# Terapkan perubahan
azd provision
```

**PowerShell:**
```powershell
# Bangun ulang template ARM
az bicep build --file infra/main.bicep

# Pratinjau perubahan
azd provision --preview

# Terapkan perubahan
azd provision
```

## Pembersihan

Untuk menghapus semua sumber daya:

**Bash:**
```bash
# Hapus semua sumber daya
azd down

# Hapus semuanya termasuk lingkungan
azd down --purge
```

**PowerShell:**
```powershell
# Hapus semua sumber daya
azd down

# Hapus semuanya termasuk lingkungan
azd down --purge
```

**Peringatan**: Ini akan menghapus semua sumber daya Azure secara permanen.

## Struktur Berkas

## Optimisasi Biaya

### Pengembangan/Pengujian
Untuk lingkungan dev/test, Anda dapat mengurangi biaya:
- Gunakan tier Standar (S0) untuk Azure OpenAI
- Atur kapasitas lebih rendah (10K TPM bukan 20K) di `infra/core/ai/cognitiveservices.bicep`
- Hapus sumber daya saat tidak dipakai: `azd down`

### Produksi
Untuk produksi:
- Tingkatkan kapasitas OpenAI sesuai penggunaan (50K+ TPM)
- Aktifkan redundansi zona untuk ketersediaan lebih tinggi
- Terapkan pemantauan dan peringatan biaya yang tepat

### Estimasi Biaya
- Azure OpenAI: Bayar per token (input + output)
- GPT-5.2: ~$3-5 per 1J token (cek harga terbaru)
- text-embedding-3-small: ~$0.02 per 1J token

Kalkulator harga: https://azure.microsoft.com/pricing/calculator/

## Pemantauan

### Lihat Metrik Azure OpenAI

Pergi ke Azure Portal → Sumber Daya OpenAI Anda → Metrics:
- Pemanfaatan Berbasis Token
- Laju Permintaan HTTP
- Waktu Respons
- Token Aktif

## Pemecahan Masalah

### Masalah: Konflik nama subdomain Azure OpenAI

**Pesan Kesalahan:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Penyebab:**
Nama subdomain yang dibuat dari langganan/lingkungan Anda sudah dipakai, mungkin dari penerapan sebelumnya yang belum sepenuhnya dihapus.

**Solusi:**
1. **Opsi 1 - Gunakan nama lingkungan berbeda:**
   
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

2. **Opsi 2 - Penerapan manual via Azure Portal:**
   - Ke Azure Portal → Buat sumber daya → Azure OpenAI
   - Pilih nama unik untuk sumber daya Anda
   - Terapkan model berikut:
     - **GPT-5.2**
     - **text-embedding-3-small** (untuk modul RAG)
   - **Penting:** Catat nama penerapan Anda - harus sesuai konfigurasi `.env`
   - Setelah penerapan, dapatkan endpoint dan kunci API dari "Keys and Endpoint"
   - Buat file `.env` di root proyek dengan:
     
     **Contoh file `.env`:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Panduan Penamaan Penerapan Model:**
- Gunakan nama sederhana dan konsisten: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Nama penerapan harus persis sama seperti yang Anda konfigurasi di `.env`
- Kesalahan umum: Membuat model dengan nama satu tapi merujuk nama berbeda dalam kode

### Masalah: GPT-5.2 tidak tersedia di wilayah yang dipilih

**Solusi:**
- Pilih wilayah yang memiliki akses GPT-5.2 (misal: eastus2)
- Periksa ketersediaan: https://learn.microsoft.com/azure/ai-services/openai/concepts/models



### Masalah: Kuota tidak cukup untuk penerapan

**Solusi:**
1. Minta peningkatan kuota di Azure Portal
2. Atau gunakan kapasitas lebih rendah di `main.bicep` (misal kapasitas: 10)

### Masalah: "Resource not found" saat menjalankan lokal

**Solusi:**
1. Verifikasi penerapan: `azd env get-values`
2. Periksa endpoint dan kunci sudah benar
3. Pastikan grup sumber daya ada di Azure Portal

### Masalah: Autentikasi gagal

**Solusi:**
- Pastikan `AZURE_OPENAI_API_KEY` sudah diatur dengan benar
- Format kunci harus 32 karakter string hexadesimal
- Ambil kunci baru dari Azure Portal jika perlu

### Penerapan Gagal

**Masalah**: `azd provision` gagal dengan kesalahan kuota atau kapasitas

**Solusi**: 
1. Coba wilayah berbeda - Lihat bagian [Mengubah Wilayah Azure](../../../../01-introduction/infra) untuk cara konfigurasi wilayah
2. Periksa langganan Anda memiliki kuota Azure OpenAI:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Aplikasi Tidak Terhubung

**Masalah**: Aplikasi Java menunjukkan kesalahan koneksi

**Solusi**:
1. Verifikasi variabel lingkungan sudah diekspor:
   
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

2. Periksa format endpoint sudah benar (harus `https://xxx.openai.azure.com`)
3. Pastikan kunci API adalah kunci primer atau sekunder dari Azure Portal

**Masalah**: 401 Unauthorized dari Azure OpenAI

**Solusi**:
1. Dapatkan kunci API baru dari Azure Portal → Keys and Endpoint
2. Ekspor ulang variabel lingkungan `AZURE_OPENAI_API_KEY`
3. Pastikan penerapan model sudah selesai (cek Azure Portal)

### Masalah Performa

**Masalah**: Waktu respons lambat

**Solusi**:
1. Periksa penggunaan token OpenAI dan throttling di metrik Azure Portal
2. Tingkatkan kapasitas TPM jika Anda mencapai batas
3. Pertimbangkan menggunakan tingkat usaha penalaran lebih tinggi (rendah/sedang/tinggi)

## Memperbarui Infrastruktur

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

## Rekomendasi Keamanan

1. **Jangan pernah commit kunci API** - Gunakan variabel lingkungan
2. **Gunakan file .env secara lokal** - Tambahkan `.env` ke `.gitignore`
3. **Rotasi kunci secara rutin** - Buat kunci baru di Azure Portal
4. **Batasi akses** - Gunakan Azure RBAC untuk mengontrol siapa yang dapat mengakses sumber daya
5. **Pantau penggunaan** - Atur peringatan biaya di Azure Portal

## Sumber Daya Tambahan

- [Dokumentasi Layanan Azure OpenAI](https://learn.microsoft.com/azure/ai-services/openai/)
- [Dokumentasi Model GPT-5.2](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Dokumentasi Azure Developer CLI](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Dokumentasi Bicep](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [Integrasi Resmi LangChain4j OpenAI](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Dukungan

Untuk masalah:
1. Periksa [bagian pemecahan masalah](../../../../01-introduction/infra) di atas
2. Tinjau status layanan Azure OpenAI di Azure Portal
3. Buka isu di repositori

## Lisensi

Lihat berkas root [LICENSE](../../../../LICENSE) untuk detail.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berusaha untuk memberikan terjemahan yang akurat, harap diketahui bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang resmi. Untuk informasi yang sangat penting, disarankan menggunakan jasa terjemahan manusia profesional. Kami tidak bertanggung jawab atas kesalahpahaman atau penafsiran yang keliru yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->