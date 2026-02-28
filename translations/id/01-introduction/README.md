# Modul 01: Memulai dengan LangChain4j

## Daftar Isi

- [Video Walkthrough](../../../01-introduction)
- [Apa yang Akan Anda Pelajari](../../../01-introduction)
- [Prasyarat](../../../01-introduction)
- [Memahami Masalah Utama](../../../01-introduction)
- [Memahami Token](../../../01-introduction)
- [Cara Kerja Memori](../../../01-introduction)
- [Cara Ini Menggunakan LangChain4j](../../../01-introduction)
- [Menyebarkan Infrastruktur Azure OpenAI](../../../01-introduction)
- [Menjalankan Aplikasi Secara Lokal](../../../01-introduction)
- [Menggunakan Aplikasi](../../../01-introduction)
  - [Chat Tanpa Status (Panel Kiri)](../../../01-introduction)
  - [Chat dengan Status (Panel Kanan)](../../../01-introduction)
- [Langkah Selanjutnya](../../../01-introduction)

## Video Walkthrough

Tonton sesi langsung ini yang menjelaskan cara memulai dengan modul ini: [Memulai dengan LangChain4j - Sesi Langsung](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Apa yang Akan Anda Pelajari

Jika Anda telah menyelesaikan quick start, Anda melihat cara mengirim prompt dan mendapatkan respons. Itu adalah fondasi, tetapi aplikasi nyata membutuhkan lebih dari itu. Modul ini mengajarkan Anda bagaimana membangun AI percakapan yang mengingat konteks dan menjaga status—perbedaan antara demo sekali jalan dan aplikasi siap produksi.

Kami akan menggunakan GPT-5.2 Azure OpenAI sepanjang panduan ini karena kemampuan penalaran lanjutannya membuat perilaku pola yang berbeda lebih jelas. Saat Anda menambahkan memori, Anda akan dengan jelas melihat perbedaannya. Ini memudahkan untuk memahami apa yang dibawa setiap komponen ke aplikasi Anda.

Anda akan membuat satu aplikasi yang menunjukkan kedua pola:

**Chat Tanpa Status** - Setiap permintaan independen. Model tidak mengingat pesan sebelumnya. Ini adalah pola yang Anda gunakan di quick start.

**Percakapan dengan Status** - Setiap permintaan mencakup riwayat percakapan. Model menjaga konteks selama beberapa giliran. Ini yang dibutuhkan aplikasi produksi.

## Prasyarat

- Langganan Azure dengan akses Azure OpenAI
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Catatan:** Java, Maven, Azure CLI, dan Azure Developer CLI (azd) sudah terpasang sebelumnya di devcontainer yang disediakan.

> **Catatan:** Modul ini menggunakan GPT-5.2 pada Azure OpenAI. Penyebaran dikonfigurasi secara otomatis melalui `azd up` - jangan ubah nama model di kode.

## Memahami Masalah Utama

Model bahasa bersifat tanpa status. Setiap panggilan API independen. Jika Anda mengirim "Nama saya John" dan kemudian bertanya "Siapa nama saya?", model tidak tahu Anda baru saja memperkenalkan diri. Ia memperlakukan setiap permintaan seolah itu percakapan pertama yang pernah Anda lakukan.

Ini bagus untuk Q&A sederhana tapi tidak berguna untuk aplikasi nyata. Bot layanan pelanggan perlu mengingat apa yang Anda katakan. Asisten pribadi memerlukan konteks. Percakapan multi-gilir membutuhkan memori.

<img src="../../../translated_images/id/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Perbedaan antara percakapan tanpa status (panggilan independen) dan dengan status (sadar konteks)*

## Memahami Token

Sebelum menyelami percakapan, penting untuk memahami token—unit dasar teks yang diproses model bahasa:

<img src="../../../translated_images/id/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Contoh bagaimana teks dipecah menjadi token—"I love AI!" menjadi 4 unit proses terpisah*

Token adalah cara model AI mengukur dan memproses teks. Kata, tanda baca, dan bahkan spasi bisa menjadi token. Model Anda memiliki batas berapa banyak token yang bisa diproses sekaligus (400.000 untuk GPT-5.2, dengan maksimal 272.000 token input dan 128.000 token output). Memahami token membantu Anda mengelola panjang percakapan dan biaya.

## Cara Kerja Memori

Memori chat menyelesaikan masalah tanpa status dengan menjaga riwayat percakapan. Sebelum mengirim permintaan Anda ke model, kerangka kerja menambahkan pesan-pesan relevan sebelumnya. Ketika Anda bertanya "Siapa nama saya?", sistem sebenarnya mengirim seluruh riwayat percakapan, memungkinkan model melihat bahwa Anda sebelumnya mengatakan "Nama saya John."

LangChain4j menyediakan implementasi memori yang mengelola ini secara otomatis. Anda memilih berapa banyak pesan yang akan disimpan dan kerangka kerja mengatur jendela konteks.

<img src="../../../translated_images/id/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory menjaga jendela geser pesan terbaru, secara otomatis menghapus pesan lama*

## Cara Ini Menggunakan LangChain4j

Modul ini memperluas quick start dengan mengintegrasikan Spring Boot dan menambah memori percakapan. Berikut bagaimana komponennya bekerja bersama:

**Dependensi** - Tambahkan dua pustaka LangChain4j:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**Chat Model** - Konfigurasikan Azure OpenAI sebagai bean Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

Builder membaca kredensial dari variabel lingkungan yang diatur oleh `azd up`. Menetapkan `baseUrl` ke endpoint Azure Anda membuat klien OpenAI bekerja dengan Azure OpenAI.

**Memori Percakapan** - Melacak riwayat chat dengan MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Buat memori dengan `withMaxMessages(10)` untuk menyimpan 10 pesan terakhir. Tambahkan pesan pengguna dan AI dengan pembungkus terketik: `UserMessage.from(text)` dan `AiMessage.from(text)`. Ambil riwayat dengan `memory.messages()` dan kirim ke model. Layanan menyimpan instansi memori terpisah per ID percakapan, memungkinkan banyak pengguna chat secara bersamaan.

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) dan tanyakan:
> - "Bagaimana MessageWindowChatMemory memutuskan pesan mana yang dihapus saat jendelanya penuh?"
> - "Bisakah saya mengimplementasikan penyimpanan memori kustom menggunakan database alih-alih di memori?"
> - "Bagaimana saya menambahkan ringkasan untuk mengompresi riwayat percakapan lama?"

Endpoint chat tanpa status melewati memori sepenuhnya - hanya `chatModel.chat(prompt)` seperti quick start. Endpoint chat dengan status menambahkan pesan ke memori, mengambil riwayat, dan menyertakan konteks itu pada setiap permintaan. Konfigurasi model sama, pola berbeda.

## Menyebarkan Infrastruktur Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Pilih langganan dan lokasi (eastus2 direkomendasikan)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Pilih langganan dan lokasi (disarankan eastus2)
```

> **Catatan:** Jika Anda mengalami error timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), cukup jalankan `azd up` lagi. Sumber daya Azure mungkin masih disiapkan di latar belakang, dan mencoba ulang memungkinkan penyebaran selesai saat sumber daya mencapai status terminal.

Ini akan:
1. Menyebarkan sumber daya Azure OpenAI dengan model GPT-5.2 dan text-embedding-3-small
2. Otomatis menghasilkan file `.env` di root proyek dengan kredensial
3. Mengatur semua variabel lingkungan yang diperlukan

**Mengalami masalah penyebaran?** Lihat [README Infrastruktur](infra/README.md) untuk pemecahan masalah mendetail termasuk konflik nama subdomain, langkah penyebaran manual di Azure Portal, dan panduan konfigurasi model.

**Verifikasi penyebaran berhasil:**

**Bash:**
```bash
cat ../.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, dll.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, dll.
```

> **Catatan:** Perintah `azd up` secara otomatis membuat file `.env`. Jika perlu memperbaruinya nanti, Anda bisa mengedit file `.env` secara manual atau membangunnya ulang dengan menjalankan:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## Menjalankan Aplikasi Secara Lokal

**Verifikasi penyebaran:**

Pastikan file `.env` ada di direktori root dengan kredensial Azure:

**Bash:**
```bash
cat ../.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Mulai aplikasi:**

**Opsi 1: Menggunakan Spring Boot Dashboard (Direkomendasikan untuk pengguna VS Code)**

Dev container sudah termasuk ekstensi Spring Boot Dashboard, yang menyediakan antarmuka visual untuk mengelola semua aplikasi Spring Boot. Anda dapat menemukannya di Activity Bar di sebelah kiri VS Code (cari ikon Spring Boot).

Dari Spring Boot Dashboard, Anda dapat:
- Melihat semua aplikasi Spring Boot yang tersedia di workspace
- Memulai/berhenti aplikasi dengan sekali klik
- Melihat log aplikasi secara real-time
- Memantau status aplikasi

Cukup klik tombol play di samping "introduction" untuk memulai modul ini, atau jalankan semua modul sekaligus.

<img src="../../../translated_images/id/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Opsi 2: Menggunakan skrip shell**

Jalankan semua aplikasi web (modul 01-04):

**Bash:**
```bash
cd ..  # Dari direktori root
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Dari direktori root
.\start-all.ps1
```

Atau jalankan hanya modul ini:

**Bash:**
```bash
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Kedua skrip secara otomatis memuat variabel lingkungan dari file `.env` root dan akan membangun JAR jika belum ada.

> **Catatan:** Jika Anda ingin membangun semua modul secara manual sebelum menjalankan:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Buka http://localhost:8080 di browser Anda.

**Untuk menghentikan:**

**Bash:**
```bash
./stop.sh  # Hanya modul ini
# Atau
cd .. && ./stop-all.sh  # Semua modul
```

**PowerShell:**
```powershell
.\stop.ps1  # Hanya modul ini
# Atau
cd ..; .\stop-all.ps1  # Semua modul
```

## Menggunakan Aplikasi

Aplikasi menyediakan antarmuka web dengan dua implementasi chat berdampingan.

<img src="../../../translated_images/id/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard menunjukkan opsi Simple Chat (tanpa status) dan Conversational Chat (dengan status)*

### Chat Tanpa Status (Panel Kiri)

Coba ini dulu. Tanyakan "Nama saya John" lalu langsung tanya "Siapa nama saya?" Model tidak akan ingat karena setiap pesan berdiri sendiri. Ini menunjukkan masalah utama integrasi model bahasa dasar—tidak ada konteks percakapan.

<img src="../../../translated_images/id/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI tidak mengingat nama Anda dari pesan sebelumnya*

### Chat dengan Status (Panel Kanan)

Sekarang coba urutan yang sama di sini. Tanya "Nama saya John" lalu "Siapa nama saya?" Kali ini ia mengingatnya. Bedanya adalah MessageWindowChatMemory—yang menjaga riwayat percakapan dan menyertakannya dengan setiap permintaan. Inilah cara kerja AI percakapan produksi.

<img src="../../../translated_images/id/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI mengingat nama Anda dari percakapan sebelumnya*

Kedua panel menggunakan model GPT-5.2 yang sama. Perbedaannya hanya memori. Ini membuat jelas apa yang dibawa memori ke aplikasi Anda dan mengapa itu penting untuk kasus penggunaan nyata.

## Langkah Selanjutnya

**Modul Berikutnya:** [02-prompt-engineering - Prompt Engineering dengan GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 00 - Quick Start](../00-quick-start/README.md) | [Kembali ke Utama](../README.md) | [Berikutnya: Modul 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berusaha untuk mencapai ketepatan, harap diingat bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang otoritatif. Untuk informasi penting, disarankan menggunakan jasa terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau kesalahan interpretasi yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->