# Modul 01: Memulakan dengan LangChain4j

## Jadual Kandungan

- [Video Panduan](../../../01-introduction)
- [Apa yang Anda Akan Pelajari](../../../01-introduction)
- [Prasyarat](../../../01-introduction)
- [Memahami Masalah Teras](../../../01-introduction)
- [Memahami Token](../../../01-introduction)
- [Bagaimana Memori Berfungsi](../../../01-introduction)
- [Bagaimana Ini Menggunakan LangChain4j](../../../01-introduction)
- [Menguruskan Infrastruktur Azure OpenAI](../../../01-introduction)
- [Jalankan Aplikasi Secara Tempatan](../../../01-introduction)
- [Menggunakan Aplikasi](../../../01-introduction)
  - [Sembang Tanpa Keadaan (Panel Kiri)](../../../01-introduction)
  - [Sembang Berkeadaan (Panel Kanan)](../../../01-introduction)
- [Langkah Seterusnya](../../../01-introduction)

## Video Panduan

Tonton sesi langsung ini yang menerangkan cara untuk memulakan modul ini:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Memulakan dengan LangChain4j - Sesi Langsung" width="800"/></a>

## Apa yang Anda Akan Pelajari

Dalam permulaan pantas, anda menggunakan Model GitHub untuk menghantar permintaan, memanggil alat, membina saluran RAG, dan menguji kawalan keselamatan. Demo tersebut menunjukkan apa yang mungkin — kini kami berpindah kepada Azure OpenAI dan GPT-5.2 dan mula membina aplikasi gaya pengeluaran. Modul ini menumpukan pada AI perbualan yang mengingati konteks dan mengekalkan keadaan — konsep yang digunakan dalam demo permulaan pantas tetapi tidak diterangkan.

Kami akan menggunakan GPT-5.2 Azure OpenAI sepanjang panduan ini kerana kebolehannya dalam penalaran yang maju menjadikan tingkah laku corak yang berbeza lebih jelas. Apabila anda menambah memori, anda akan lihat perbezaannya dengan jelas. Ini memudahkan untuk memahami apa yang dibawa oleh setiap komponen kepada aplikasi anda.

Anda akan membina satu aplikasi yang menunjukkan kedua-dua corak:

**Sembang Tanpa Keadaan** - Setiap permintaan adalah bebas. Model tidak mengingati mesej sebelumnya. Ini adalah corak yang anda gunakan dalam permulaan pantas.

**Perbualan Berkeadaan** - Setiap permintaan termasuk sejarah perbualan. Model mengekalkan konteks merentas beberapa gelung. Ini adalah yang diperlukan oleh aplikasi pengeluaran.

## Prasyarat

- Langganan Azure dengan akses Azure OpenAI
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Nota:** Java, Maven, Azure CLI dan Azure Developer CLI (azd) telah dipasang dalam devcontainer yang disediakan.

> **Nota:** Modul ini menggunakan GPT-5.2 di Azure OpenAI. Penyebaran dikonfigurasi secara automatik melalui `azd up` - jangan ubah nama model dalam kod.

## Memahami Masalah Teras

Model bahasa adalah tanpa keadaan. Setiap panggilan API adalah bebas. Jika anda menghantar "Nama saya John" dan kemudian bertanya "Siapakah nama saya?", model tidak tahu anda baru saja memperkenalkan diri. Ia menganggap setiap permintaan seperti perbualan pertama anda sekali.

Ini sesuai untuk soal jawab mudah tetapi tidak berguna untuk aplikasi sebenar. Bot perkhidmatan pelanggan perlu mengingati apa yang anda beritahu mereka. Pembantu peribadi memerlukan konteks. Apa-apa perbualan berbilang gelung memerlukan memori.

Rajah berikut membezakan dua pendekatan — di kiri, panggilan tanpa keadaan yang melupakan nama anda; di kanan, panggilan berkeadaan yang disokong oleh ChatMemory yang mengingatinya.

<img src="../../../translated_images/ms/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Perbualan Tanpa Keadaan vs Berkeadaan" width="800"/>

*Perbezaan antara perbualan tanpa keadaan (panggilan bebas) dan berkeadaan (sadar konteks)*

## Memahami Token

Sebelum menyelami perbualan, penting untuk memahami token - unit asas teks yang diproses oleh model bahasa:

<img src="../../../translated_images/ms/token-explanation.c39760d8ec650181.webp" alt="Penjelasan Token" width="800"/>

*Contoh bagaimana teks dipecah menjadi token - "I love AI!" menjadi 4 unit pemprosesan berasingan*

Token adalah cara model AI mengukur dan memproses teks. Perkataan, tanda baca, dan bahkan ruang boleh menjadi token. Model anda mempunyai had berapa banyak token yang boleh diproses sekali gus (400,000 untuk GPT-5.2, dengan sehingga 272,000 token input dan 128,000 token output). Memahami token membantu anda mengurus panjang perbualan dan kos.

## Bagaimana Memori Berfungsi

Memori sembang menyelesaikan masalah tanpa keadaan dengan mengekalkan sejarah perbualan. Sebelum menghantar permintaan anda ke model, rangka kerja menambahkan mesej sebelumnya yang relevan terlebih dahulu. Apabila anda bertanya "Siapakah nama saya?", sistem sebenarnya menghantar keseluruhan sejarah perbualan, membolehkan model melihat anda sebelum ini berkata "Nama saya John."

LangChain4j menyediakan pelaksanaan memori yang mengendalikannya secara automatik. Anda memilih berapa banyak mesej untuk disimpan dan rangka kerja menguruskan tetingkap konteks. Rajah di bawah menunjukkan bagaimana MessageWindowChatMemory mengekalkan tetingkap gelungsur mesej terkini.

<img src="../../../translated_images/ms/memory-window.bbe67f597eadabb3.webp" alt="Konsep Tetingkap Memori" width="800"/>

*MessageWindowChatMemory mengekalkan tetingkap gelungsur mesej terkini, secara automatik membuang mesej lama*

## Bagaimana Ini Menggunakan LangChain4j

Modul ini mengembangkan permulaan pantas dengan mengintegrasikan Spring Boot dan menambah memori perbualan. Berikut adalah cara komponen disusun:

**Pergantungan** - Tambah dua perpustakaan LangChain4j:

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

**Model Sembang** - Konfigurasikan Azure OpenAI sebagai bean Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder membaca kelayakan dari pembolehubah persekitaran yang ditetapkan oleh `azd up`. Menetapkan `baseUrl` ke titik akhir Azure anda membuat klien OpenAI berfungsi dengan Azure OpenAI.

**Memori Perbualan** - Jejaki sejarah sembang dengan MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Buat memori dengan `withMaxMessages(10)` untuk menyimpan 10 mesej terakhir. Tambah mesej pengguna dan AI dengan pembalut berjenis: `UserMessage.from(text)` dan `AiMessage.from(text)`. Dapatkan sejarah dengan `memory.messages()` dan hantar ke model. Perkhidmatan menyimpan instans memori yang berasingan bagi setiap ID perbualan, membolehkan pelbagai pengguna berbual serentak.

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Sembang:** Buka [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) dan tanya:
> - "Bagaimana MessageWindowChatMemory memutuskan mesej mana yang akan dibuang apabila tetingkap penuh?"
> - "Bolehkah saya melaksanakan penyimpanan memori khusus menggunakan pangkalan data dan bukannya dalam memori?"
> - "Bagaimana saya menambah ringkasan untuk memampatkan sejarah perbualan lama?"

Akhir sembang tanpa keadaan tidak menggunakan memori sama sekali - cuma `chatModel.chat(prompt)` seperti permulaan pantas. Akhir berkeadaan menambah mesej ke memori, mengambil sejarah, dan memasukkan konteks itu dengan setiap permintaan. Konfigurasi model sama, corak berbeza.

## Menguruskan Infrastruktur Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Pilih langganan dan lokasi (eastus2 disyorkan)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Pilih langganan dan lokasi (eastus2 disyorkan)
```

> **Nota:** Jika anda menghadapi ralat masa tamat (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), jalankan semula `azd up`. Sumber Azure mungkin masih dalam proses penyediaan di belakang tabir, dan cuba semula membenarkan penyebaran selesai apabila sumber mencapai keadaan terminal.

Ini akan:
1. Menyebarkan sumber Azure OpenAI dengan model GPT-5.2 dan text-embedding-3-small
2. Menjana fail `.env` secara automatik di akar projek dengan kelayakan
3. Menyediakan semua pembolehubah persekitaran yang diperlukan

**Mengalami masalah penyebaran?** Lihat [README Infrastruktur](infra/README.md) untuk penyelesaian terperinci termasuk konflik nama subdomain, langkah penyebaran manual di Portal Azure, dan panduan konfigurasi model.

**Sahkan penyebaran berjaya:**

**Bash:**
```bash
cat ../.env  # Patut menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, dan lain-lain.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Patut menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, dan lain-lain.
```

> **Nota:** Perintah `azd up` menjana fail `.env` secara automatik. Jika anda perlu mengemas kini fail itu kemudian, anda boleh sunting fail `.env` secara manual atau hasilkan semula dengan menjalankan:
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

## Jalankan Aplikasi Secara Tempatan

**Sahkan penyebaran:**

Pastikan fail `.env` wujud di direktori akar dengan kelayakan Azure. Jalankan ini dari direktori modul (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Perlu menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Patut menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Mulakan aplikasi:**

**Pilihan 1: Menggunakan Spring Boot Dashboard (Disyorkan untuk pengguna VS Code)**

Kontena dev termasuk peluasan Spring Boot Dashboard, yang menyediakan antara muka visual untuk mengurus semua aplikasi Spring Boot. Anda boleh menjumpainya di Bar Aktiviti di sebelah kiri VS Code (cari ikon Spring Boot).

Dari Spring Boot Dashboard, anda boleh:
- Lihat semua aplikasi Spring Boot yang tersedia dalam ruang kerja
- Mulakan/berhenti aplikasi dengan satu klik
- Lihat log aplikasi secara masa nyata
- Pantau status aplikasi

Klik butang main di sebelah "introduction" untuk memulakan modul ini, atau mulakan semua modul sekaligus.

<img src="../../../translated_images/ms/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard di VS Code — mulakan, berhenti, dan pantau semua modul dari satu tempat*

**Pilihan 2: Menggunakan skrip shell**

Mulakan semua aplikasi web (modul 01-04):

**Bash:**
```bash
cd ..  # Dari direktori root
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Dari direktori akar
.\start-all.ps1
```

Atau mulakan hanya modul ini:

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

Kedua-dua skrip secara automatik memuat pembolehubah persekitaran dari fail `.env` akar dan akan membina JAR jika belum ada.

> **Nota:** Jika anda lebih suka membina semua modul secara manual sebelum memulakan:
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

Buka http://localhost:8080 dalam pelayar anda.

**Untuk berhenti:**

**Bash:**
```bash
./stop.sh  # Modul ini sahaja
# Atau
cd .. && ./stop-all.sh  # Semua modul
```

**PowerShell:**
```powershell
.\stop.ps1  # Modul ini sahaja
# Atau
cd ..; .\stop-all.ps1  # Semua modul
```

## Menggunakan Aplikasi

Aplikasi menyediakan antara muka web dengan dua pelaksanaan sembang berdampingan.

<img src="../../../translated_images/ms/home-screen.121a03206ab910c0.webp" alt="Skrin Utama Aplikasi" width="800"/>

*Papan pemuka menunjukkan pilihan Sembang Mudah (tanpa keadaan) dan Sembang Perbualan (berkeadaan)*

### Sembang Tanpa Keadaan (Panel Kiri)

Cuba ini dahulu. Tanyakan "Nama saya John" dan kemudian segera tanya "Siapakah nama saya?" Model tidak akan ingat kerana setiap mesej adalah bebas. Ini menunjukkan masalah teras dengan integrasi model bahasa asas - tiada konteks perbualan.

<img src="../../../translated_images/ms/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo Sembang Tanpa Keadaan" width="800"/>

*AI tidak mengingati nama anda dari mesej sebelumnya*

### Sembang Berkeadaan (Panel Kanan)

Sekarang cuba urutan yang sama di sini. Tanya "Nama saya John" dan kemudian "Siapakah nama saya?" Kali ini ia ingat. Perbezaannya adalah MessageWindowChatMemory - ia mengekalkan sejarah perbualan dan memasukkannya dengan setiap permintaan. Ini cara AI perbualan pengeluaran berfungsi.

<img src="../../../translated_images/ms/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo Sembang Berkeadaan" width="800"/>

*AI mengingati nama anda dari awal perbualan*

Kedua-dua panel menggunakan model GPT-5.2 yang sama. Satu-satunya perbezaan adalah memori. Ini menunjukkan dengan jelas apa yang dibawa oleh memori kepada aplikasi anda dan mengapa ia penting untuk kes penggunaan sebenar.

## Langkah Seterusnya

**Modul Seterusnya:** [02-prompt-engineering - Kejuruteraan Prompt dengan GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 00 - Permulaan Pantas](../00-quick-start/README.md) | [Kembali ke Utama](../README.md) | [Seterusnya: Modul 02 - Kejuruteraan Prompt →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila ambil maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang sahih. Untuk maklumat penting, terjemahan profesional oleh manusia adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->