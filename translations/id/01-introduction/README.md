# Modul 01: Memulai dengan LangChain4j

## Daftar Isi

- [Panduan Video](../../../01-introduction)
- [Apa yang Akan Anda Pelajari](../../../01-introduction)
- [Prasyarat](../../../01-introduction)
- [Memahami Masalah Inti](../../../01-introduction)
- [Memahami Token](../../../01-introduction)
- [Cara Kerja Memori](../../../01-introduction)
- [Cara Ini Menggunakan LangChain4j](../../../01-introduction)
- [Menerapkan Infrastruktur Azure OpenAI](../../../01-introduction)
- [Menjalankan Aplikasi Secara Lokal](../../../01-introduction)
- [Menggunakan Aplikasi](../../../01-introduction)
  - [Chat Stateless (Panel Kiri)](../../../01-introduction)
  - [Chat Stateful (Panel Kanan)](../../../01-introduction)
- [Langkah Selanjutnya](../../../01-introduction)

## Panduan Video

Tonton sesi langsung yang menjelaskan cara memulai dengan modul ini:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Apa yang Akan Anda Pelajari

Jika Anda telah menyelesaikan quick start, Anda sudah melihat bagaimana mengirim prompt dan mendapatkan respons. Itu adalah dasarannya, tapi aplikasi nyata membutuhkan lebih banyak. Modul ini mengajarkan Anda cara membangun AI percakapan yang mengingat konteks dan mempertahankan status - perbedaan antara demo sekali pakai dan aplikasi siap produksi.

Kita akan menggunakan GPT-5.2 dari Azure OpenAI sepanjang panduan ini karena kemampuan penalaran tingkat lanjutnya membuat perilaku berbagai pola lebih jelas. Ketika Anda menambahkan memori, Anda akan dengan jelas melihat perbedaannya. Ini mempermudah memahami apa yang setiap komponen bawa ke aplikasi Anda.

Anda akan membangun satu aplikasi yang menunjukkan kedua pola:

**Chat Stateless** - Setiap permintaan berdiri sendiri. Model tidak memiliki memori pesan sebelumnya. Ini adalah pola yang Anda gunakan di quick start.

**Percakapan Stateful** - Setiap permintaan menyertakan riwayat percakapan. Model mempertahankan konteks di beberapa putaran. Ini yang dibutuhkan aplikasi produksi.

## Prasyarat

- Langganan Azure dengan akses Azure OpenAI
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Catatan:** Java, Maven, Azure CLI dan Azure Developer CLI (azd) sudah terpasang di dalam devcontainer yang disediakan.

> **Catatan:** Modul ini menggunakan GPT-5.2 di Azure OpenAI. Deployment dikonfigurasi otomatis melalui `azd up` - jangan ubah nama model di kode.

## Memahami Masalah Inti

Model bahasa bersifat stateless. Setiap panggilan API bersifat mandiri. Jika Anda mengirim "Nama saya John" lalu bertanya "Siapa nama saya?", model tidak tahu Anda baru saja memperkenalkan diri. Ia memperlakukan setiap permintaan seolah itu adalah percakapan pertama yang pernah Anda lakukan.

Ini cukup untuk Q&A sederhana tapi tidak berguna untuk aplikasi nyata. Bot layanan pelanggan harus mengingat apa yang Anda katakan. Asisten pribadi membutuhkan konteks. Setiap percakapan multi-putaran membutuhkan memori.

<img src="../../../translated_images/id/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Perbedaan antara percakapan stateless (panggilan independen) dan stateful (sadar konteks)*

## Memahami Token

Sebelum masuk ke percakapan, penting memahami token - unit dasar teks yang diproses model bahasa:

<img src="../../../translated_images/id/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Contoh bagaimana teks dibagi menjadi token - "I love AI!" menjadi 4 unit pemrosesan terpisah*

Token adalah cara model AI mengukur dan memproses teks. Kata, tanda baca, bahkan spasi bisa menjadi token. Model Anda memiliki batas berapa banyak token yang dapat diproses sekaligus (400.000 untuk GPT-5.2, dengan hingga 272.000 token input dan 128.000 token output). Memahami token membantu mengelola panjang percakapan dan biaya.

## Cara Kerja Memori

Memori chat menyelesaikan masalah stateless dengan mempertahankan riwayat percakapan. Sebelum mengirim permintaan ke model, framework menyisipkan pesan sebelumnya yang relevan. Saat Anda bertanya "Siapa nama saya?", sistem sebenarnya mengirim seluruh riwayat percakapan, memungkinkan model melihat bahwa Anda sebelumnya mengatakan "Nama saya John."

LangChain4j menyediakan implementasi memori yang menangani ini secara otomatis. Anda memilih berapa banyak pesan yang ingin disimpan dan framework mengatur jendela konteks.

<img src="../../../translated_images/id/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory mempertahankan jendela geser pesan terbaru, otomatis menghapus pesan lama*

## Cara Ini Menggunakan LangChain4j

Modul ini memperluas quick start dengan mengintegrasikan Spring Boot dan menambahkan memori percakapan. Berikut cara bagian-bagiannya terpasang:

**Dependencies** - Tambahkan dua pustaka LangChain4j:

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

**Model Chat** - Konfigurasikan Azure OpenAI sebagai bean Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder membaca kredensial dari variabel lingkungan yang diset oleh `azd up`. Pengaturan `baseUrl` ke endpoint Azure Anda membuat klien OpenAI bekerja dengan Azure OpenAI.

**Memori Percakapan** - Melacak riwayat chat dengan MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Buat memori dengan `withMaxMessages(10)` untuk menyimpan 10 pesan terakhir. Tambahkan pesan pengguna dan AI dengan pembungkus yang bertipe: `UserMessage.from(text)` dan `AiMessage.from(text)`. Ambil riwayat dengan `memory.messages()` dan kirim ke model. Servis menyimpan instance memori terpisah per ID percakapan, memungkinkan banyak pengguna mengobrol secara bersamaan.

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) dan tanyakan:
> - "Bagaimana MessageWindowChatMemory memutuskan pesan mana yang dihapus saat jendela sudah penuh?"
> - "Bisakah saya mengimplementasikan penyimpanan memori khusus menggunakan database daripada di memori?"
> - "Bagaimana saya menambahkan ringkasan untuk mengompres riwayat percakapan lama?"

Endpoint chat stateless melewati memori sepenuhnya - hanya `chatModel.chat(prompt)` seperti quick start. Endpoint stateful menambahkan pesan ke memori, mengambil riwayat, dan menyertakan konteks itu dengan setiap permintaan. Konfigurasi model sama, pola berbeda.

## Menerapkan Infrastruktur Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Pilih langganan dan lokasi (eastus2 direkomendasikan)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Pilih langganan dan lokasi (eastus2 direkomendasikan)
```

> **Catatan:** Jika Anda menemui kesalahan timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), cukup jalankan ulang `azd up`. Sumber daya Azure mungkin masih sedang dibuat di latar belakang, dan percobaan ulang memungkinkan penyelesaian deployment saat sumber daya telah berada di status terminal.

Ini akan:
1. Menerapkan sumber daya Azure OpenAI dengan model GPT-5.2 dan text-embedding-3-small
2. Membuat file `.env` secara otomatis di root proyek dengan kredensial
3. Mengatur semua variabel lingkungan yang diperlukan

**Mengalami masalah deployment?** Lihat [README Infrastruktur](infra/README.md) untuk pemecahan masalah rinci termasuk konflik nama subdomain, langkah manual deployment di Azure Portal, dan panduan konfigurasi model.

**Verifikasi deployment berhasil:**

**Bash:**
```bash
cat ../.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, dll.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, dll.
```

> **Catatan:** Perintah `azd up` secara otomatis membuat file `.env`. Jika perlu memperbarui nanti, Anda bisa mengedit file `.env` secara manual atau membuat ulang dengan menjalankan:
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

**Verifikasi deployment:**

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

Dev container menyertakan ekstensi Spring Boot Dashboard, yang menyediakan antarmuka visual untuk mengelola semua aplikasi Spring Boot. Anda dapat menemukannya di Activity Bar di sisi kiri VS Code (cari ikon Spring Boot).

Dari Spring Boot Dashboard, Anda dapat:
- Melihat semua aplikasi Spring Boot yang tersedia di workspace
- Memulai/berhenti aplikasi dengan satu klik
- Melihat log aplikasi secara real-time
- Memantau status aplikasi

Klik tombol play di sebelah "introduction" untuk memulai modul ini, atau mulai semua modul sekaligus.

<img src="../../../translated_images/id/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Opsi 2: Menggunakan skrip shell**

Mulai semua aplikasi web (modul 01-04):

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

Atau mulai hanya modul ini:

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

Kedua skrip otomatis memuat variabel lingkungan dari file `.env` root dan akan membangun JAR jika belum ada.

> **Catatan:** Jika Anda lebih suka membangun semua modul secara manual sebelum mulai:
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

Buka http://localhost:8080 di peramban Anda.

**Untuk menghentikan:**

**Bash:**
```bash
./stop.sh  # Modul ini saja
# Atau
cd .. && ./stop-all.sh  # Semua modul
```

**PowerShell:**
```powershell
.\stop.ps1  # Modul ini saja
# Atau
cd ..; .\stop-all.ps1  # Semua modul
```

## Menggunakan Aplikasi

Aplikasi menyediakan antarmuka web dengan dua implementasi chat berdampingan.

<img src="../../../translated_images/id/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard menampilkan pilihan Simple Chat (stateless) dan Conversational Chat (stateful)*

### Chat Stateless (Panel Kiri)

Coba ini dulu. Tanyakan "Nama saya John" lalu segera tanya "Siapa nama saya?" Model tidak akan mengingat karena setiap pesan berdiri sendiri. Ini menunjukkan masalah inti dengan integrasi model bahasa dasar - tidak ada konteks percakapan.

<img src="../../../translated_images/id/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI tidak mengingat nama Anda dari pesan sebelumnya*

### Chat Stateful (Panel Kanan)

Sekarang coba urutan yang sama di sini. Tanyakan "Nama saya John" lalu "Siapa nama saya?" Kali ini AI mengingat. Perbedaannya adalah MessageWindowChatMemory - yang mempertahankan riwayat percakapan dan menyertakannya dengan setiap permintaan. Inilah cara kerja AI percakapan produksi.

<img src="../../../translated_images/id/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI mengingat nama Anda dari awal percakapan*

Kedua panel menggunakan model GPT-5.2 yang sama. Satu-satunya perbedaan adalah memori. Ini membuat jelas apa yang dibawa memori ke aplikasi Anda dan mengapa itu penting untuk kasus penggunaan nyata.

## Langkah Selanjutnya

**Modul Berikutnya:** [02-prompt-engineering - Rekayasa Prompt dengan GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 00 - Quick Start](../00-quick-start/README.md) | [Kembali ke Utama](../README.md) | [Berikutnya: Modul 02 - Rekayasa Prompt →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berusaha untuk akurasi, harap maklum bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang otoritatif. Untuk informasi penting, disarankan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau kesalahan tafsir yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->