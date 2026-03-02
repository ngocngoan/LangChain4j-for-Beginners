# Modul 01: Memulai dengan LangChain4j

## Daftar Isi

- [Video Walkthrough](../../../01-introduction)
- [Apa yang Akan Anda Pelajari](../../../01-introduction)
- [Prasyarat](../../../01-introduction)
- [Memahami Masalah Inti](../../../01-introduction)
- [Memahami Token](../../../01-introduction)
- [Cara Kerja Memori](../../../01-introduction)
- [Cara Ini Menggunakan LangChain4j](../../../01-introduction)
- [Menerapkan Infrastruktur Azure OpenAI](../../../01-introduction)
- [Menjalankan Aplikasi Secara Lokal](../../../01-introduction)
- [Menggunakan Aplikasi](../../../01-introduction)
  - [Obrolan Stateless (Panel Kiri)](../../../01-introduction)
  - [Obrolan Stateful (Panel Kanan)](../../../01-introduction)
- [Langkah Selanjutnya](../../../01-introduction)

## Video Walkthrough

Tonton sesi langsung ini yang menjelaskan cara memulai dengan modul ini:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Apa yang Akan Anda Pelajari

Dalam quick start, Anda menggunakan Model GitHub untuk mengirim prompt, memanggil alat, membangun pipeline RAG, dan menguji guardrails. Demo tersebut menunjukkan apa yang mungkin — sekarang kita beralih ke Azure OpenAI dan GPT-5.2 dan mulai membangun aplikasi bergaya produksi. Modul ini fokus pada AI percakapan yang mengingat konteks dan menjaga status — konsep yang digunakan oleh demo quick start di balik layar tapi tidak dijelaskan.

Kita akan menggunakan GPT-5.2 dari Azure OpenAI sepanjang panduan ini karena kemampuan penalarannya yang maju membuat perilaku pola yang berbeda lebih jelas. Saat Anda menambahkan memori, Anda akan melihat perbedaannya dengan jelas. Ini memudahkan Anda memahami apa yang dibawa setiap komponen ke aplikasi Anda.

Anda akan membangun satu aplikasi yang menunjukkan kedua pola:

**Obrolan Stateless** - Setiap permintaan bersifat independen. Model tidak mengingat pesan sebelumnya. Ini adalah pola yang Anda gunakan dalam quick start.

**Percakapan Stateful** - Setiap permintaan menyertakan riwayat percakapan. Model menjaga konteks di beberapa giliran. Ini yang dibutuhkan oleh aplikasi produksi.

## Prasyarat

- Langganan Azure dengan akses Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Catatan:** Java, Maven, Azure CLI dan Azure Developer CLI (azd) sudah terpasang dalam devcontainer yang disediakan.

> **Catatan:** Modul ini menggunakan GPT-5.2 di Azure OpenAI. Deployment dikonfigurasi otomatis melalui `azd up` - jangan ubah nama model di kode.

## Memahami Masalah Inti

Model bahasa bersifat stateless. Setiap panggilan API independen. Jika Anda mengirim "Nama saya John" lalu tanya "Siapa nama saya?", model tidak tahu bahwa Anda baru saja memperkenalkan diri. Model memperlakukan setiap permintaan seolah-olah itu adalah percakapan pertama Anda.

Ini cukup untuk Q&A sederhana tapi tidak berguna untuk aplikasi nyata. Bot layanan pelanggan perlu mengingat apa yang Anda katakan. Asisten pribadi perlu konteks. Setiap percakapan multi-giliran membutuhkan memori.

Diagram berikut membandingkan dua pendekatan — di kiri, panggilan stateless yang lupa nama Anda; di kanan, panggilan stateful dengan ChatMemory yang mengingatnya.

<img src="../../../translated_images/id/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Perbedaan antara percakapan stateless (panggilan independen) dan stateful (memahami konteks)*

## Memahami Token

Sebelum masuk ke percakapan, penting untuk memahami token - unit teks dasar yang diproses model bahasa:

<img src="../../../translated_images/id/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Contoh bagaimana teks dipecah menjadi token - "I love AI!" menjadi 4 unit pemrosesan terpisah*

Token adalah cara model AI mengukur dan memproses teks. Kata, tanda baca, bahkan spasi bisa menjadi token. Model Anda memiliki batas berapa banyak token yang bisa diproses sekaligus (400.000 untuk GPT-5.2, dengan hingga 272.000 token input dan 128.000 token output). Memahami token membantu Anda mengatur panjang percakapan dan biaya.

## Cara Kerja Memori

Chat memory menyelesaikan masalah stateless dengan menjaga riwayat percakapan. Sebelum mengirim permintaan ke model, framework menambahkan pesan sebelumnya yang relevan. Ketika Anda tanya "Siapa nama saya?", sistem sebenarnya mengirim seluruh riwayat percakapan, memungkinkan model melihat Anda sebelumnya berkata "Nama saya John."

LangChain4j menyediakan implementasi memori yang menangani ini secara otomatis. Anda memilih berapa banyak pesan yang disimpan dan framework mengelola jendela konteks. Diagram di bawah menunjukkan bagaimana MessageWindowChatMemory menjaga jendela geser pesan terbaru.

<img src="../../../translated_images/id/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory menjaga jendela geser pesan terbaru, otomatis menghapus pesan lama*

## Cara Ini Menggunakan LangChain4j

Modul ini memperluas quick start dengan mengintegrasikan Spring Boot dan menambahkan memori percakapan. Berikut cara komponennya bekerja sama:

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

**Memori Percakapan** - Melacak riwayat obrolan dengan MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Buat memori dengan `withMaxMessages(10)` untuk menyimpan 10 pesan terakhir. Tambahkan pesan pengguna dan AI dengan pembungkus tipe: `UserMessage.from(text)` dan `AiMessage.from(text)`. Ambil riwayat dengan `memory.messages()` dan kirimkan ke model. Service menyimpan instance memori terpisah per ID percakapan, memungkinkan banyak pengguna mengobrol secara bersamaan.

> **🤖 Coba dengan Chat [GitHub Copilot](https://github.com/features/copilot):** Buka [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) dan tanyakan:
> - "Bagaimana MessageWindowChatMemory memutuskan pesan mana yang dihapus saat jendelanya penuh?"
> - "Bisakah saya mengimplementasikan penyimpanan memori kustom menggunakan database daripada di memori?"
> - "Bagaimana saya menambahkan ringkasan untuk mengompres riwayat percakapan lama?"

Endpoint obrolan stateless melewati memori sepenuhnya - hanya `chatModel.chat(prompt)` seperti quick start. Endpoint stateful menambahkan pesan ke memori, mengambil riwayat, dan menyertakan konteks itu di setiap permintaan. Konfigurasi model sama, pola berbeda.

## Menerapkan Infrastruktur Azure OpenAI

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

> **Catatan:** Jika Anda menghadapi kesalahan timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), cukup jalankan kembali `azd up`. Sumber daya Azure mungkin masih dalam proses penyediaan di latar belakang, dan mencoba lagi memungkinkan deployment selesai setelah sumber daya mencapai status terminal.

Ini akan:
1. Menerapkan sumber daya Azure OpenAI dengan model GPT-5.2 dan text-embedding-3-small
2. Otomatis membuat file `.env` di root proyek dengan kredensial
3. Mengatur semua variabel lingkungan yang diperlukan

**Mengalami masalah deployment?** Lihat [Infrastructure README](infra/README.md) untuk pemecahan masalah terperinci termasuk konflik nama subdomain, langkah manual deployment Azure Portal, dan panduan konfigurasi model.

**Verifikasi deployment berhasil:**

**Bash:**
```bash
cat ../.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, dll.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, dll.
```

> **Catatan:** Perintah `azd up` otomatis menghasilkan file `.env`. Jika perlu memperbarui nanti, Anda dapat mengedit manual file `.env` atau membuat ulang dengan menjalankan:
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

Pastikan file `.env` ada di direktori root dengan kredensial Azure. Jalankan ini dari direktori modul (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Harus menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Mulai aplikasi:**

**Pilihan 1: Menggunakan Spring Boot Dashboard (Direkomendasikan untuk pengguna VS Code)**

Dev container mencakup ekstensi Spring Boot Dashboard, yang menyediakan antarmuka visual untuk mengelola semua aplikasi Spring Boot. Anda dapat menemukannya di Bilah Aktivitas di sisi kiri VS Code (cari ikon Spring Boot).

Dari Spring Boot Dashboard, Anda dapat:
- Melihat semua aplikasi Spring Boot yang tersedia di workspace
- Memulai/berhenti aplikasi dengan satu klik
- Melihat log aplikasi secara real-time
- Memantau status aplikasi

Cukup klik tombol putar di samping "introduction" untuk memulai modul ini, atau mulai semua modul sekaligus.

<img src="../../../translated_images/id/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard di VS Code — mulai, hentikan, dan pantau semua modul dari satu tempat*

**Pilihan 2: Menggunakan skrip shell**

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

Kedua skrip secara otomatis memuat variabel lingkungan dari file `.env` root dan akan membangun JAR jika belum ada.

> **Catatan:** Jika Anda ingin membangun semua modul secara manual sebelum mulai:
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

**Untuk berhenti:**

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

Aplikasi menyediakan antarmuka web dengan dua implementasi obrolan berdampingan.

<img src="../../../translated_images/id/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard menampilkan opsi Simple Chat (stateless) dan Conversational Chat (stateful)*

### Obrolan Stateless (Panel Kiri)

Coba ini dulu. Tanyakan "Nama saya John" lalu langsung tanya "Siapa nama saya?" Model tidak akan mengingat karena setiap pesan bersifat independen. Ini menunjukkan masalah inti integrasi model bahasa dasar - tanpa konteks percakapan.

<img src="../../../translated_images/id/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI tidak mengingat nama Anda dari pesan sebelumnya*

### Obrolan Stateful (Panel Kanan)

Sekarang coba urutan yang sama di sini. Tanyakan "Nama saya John" lalu "Siapa nama saya?" Kali ini ia mengingatnya. Bedanya adalah MessageWindowChatMemory - ia menjaga riwayat percakapan dan menyertakannya dengan setiap permintaan. Inilah cara AI percakapan produksi bekerja.

<img src="../../../translated_images/id/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI mengingat nama Anda dari awal percakapan*

Kedua panel menggunakan model GPT-5.2 yang sama. Satu-satunya perbedaan adalah memori. Ini membuat jelas apa yang dibawa memori ke aplikasi Anda dan mengapa penting untuk kasus penggunaan nyata.

## Langkah Selanjutnya

**Modul Berikutnya:** [02-prompt-engineering - Rekayasa Prompt dengan GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 00 - Quick Start](../00-quick-start/README.md) | [Kembali ke Utama](../README.md) | [Berikutnya: Modul 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berupaya untuk mencapai ketepatan, harap diketahui bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang sahih. Untuk informasi penting, disarankan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau tafsir yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->