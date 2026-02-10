# Modul 01: Memulakan dengan LangChain4j

## Senarai Kandungan

- [Apa yang Anda Akan Pelajari](../../../01-introduction)
- [Prasyarat](../../../01-introduction)
- [Memahami Masalah Teras](../../../01-introduction)
- [Memahami Token](../../../01-introduction)
- [Bagaimana Memori Berfungsi](../../../01-introduction)
- [Bagaimana Ini Menggunakan LangChain4j](../../../01-introduction)
- [Menyebarkan Infrastruktur Azure OpenAI](../../../01-introduction)
- [Menjalankan Aplikasi Secara Lokal](../../../01-introduction)
- [Menggunakan Aplikasi](../../../01-introduction)
  - [Sembang Tanpa Keadaan (Panel Kiri)](../../../01-introduction)
  - [Sembang Berkeadaan (Panel Kanan)](../../../01-introduction)
- [Langkah Seterusnya](../../../01-introduction)

## Apa yang Anda Akan Pelajari

Jika anda telah menyelesaikan pengehadan pantas, anda telah melihat bagaimana menghantar arahan dan mendapatkan respons. Itu adalah asas, tetapi aplikasi sebenar memerlukan lebih daripada itu. Modul ini mengajar anda cara membina AI perbualan yang mengingati konteks dan mengekalkan keadaan - perbezaan antara demo sekali dan aplikasi yang sedia untuk produksi.

Kami akan menggunakan GPT-5.2 Azure OpenAI sepanjang panduan ini kerana kebolehan penaakulan canggihnya menjadikan tingkah laku pelbagai corak lebih jelas. Apabila anda menambah memori, anda akan jelas melihat perbezaannya. Ini memudahkan anda memahami apa yang setiap komponen bawa ke aplikasi anda.

Anda akan membina satu aplikasi yang menunjukkan kedua-dua corak:

**Sembang Tanpa Keadaan** - Setiap permintaan adalah berdikari. Model tidak mempunyai memori mesej sebelumnya. Ini adalah corak yang anda gunakan dalam pengehadan pantas.

**Perbualan Berkeadaan** - Setiap permintaan termasuk sejarah perbualan. Model mengekalkan konteks merentas beberapa pusingan. Ini yang diperlukan oleh aplikasi produksi.

## Prasyarat

- Langganan Azure dengan akses Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Nota:** Java, Maven, Azure CLI dan Azure Developer CLI (azd) telah dipasang terlebih dahulu dalam devcontainer yang disediakan.

> **Nota:** Modul ini menggunakan GPT-5.2 pada Azure OpenAI. Penyebaran dikonfigurasi secara automatik melalui `azd up` - jangan ubah nama model dalam kod.

## Memahami Masalah Teras

Model bahasa adalah tanpa keadaan. Setiap panggilan API adalah berdikari. Jika anda hantar "Nama saya John" dan kemudian tanya "Siapakah nama saya?", model tidak tahu anda baru saja memperkenalkan diri. Ia menganggap setiap permintaan seolah-olah ini adalah perbualan pertama yang pernah anda lakukan.

Ini baik untuk soalan dan jawapan mudah tetapi tidak berguna untuk aplikasi sebenar. Bot khidmat pelanggan perlu ingat apa yang anda beritahu mereka. Pembantu peribadi memerlukan konteks. Sebarang perbualan berbilang pusingan memerlukan memori.

<img src="../../../translated_images/ms/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Perbualan Tanpa Keadaan vs Berkeadaan" width="800"/>

*Perbezaan antara perbualan tanpa keadaan (panggilan berdikari) dan berkeadaan (sedar konteks)*

## Memahami Token

Sebelum menyelami perbualan, penting untuk memahami token - unit asas teks yang diproses model bahasa:

<img src="../../../translated_images/ms/token-explanation.c39760d8ec650181.webp" alt="Penjelasan Token" width="800"/>

*Contoh bagaimana teks dipecah menjadi token - "I love AI!" menjadi 4 unit pemprosesan berasingan*

Token ialah cara model AI mengukur dan memproses teks. Perkataan, tanda baca, dan bahkan ruang boleh menjadi token. Model anda mempunyai had berapa banyak token yang boleh diproses sekaligus (400,000 untuk GPT-5.2, dengan sehingga 272,000 token input dan 128,000 token output). Memahami token membantu anda menguruskan panjang perbualan dan kos.

## Bagaimana Memori Berfungsi

Memori sembang menyelesaikan masalah tanpa keadaan dengan mengekalkan sejarah perbualan. Sebelum menghantar permintaan anda ke model, rangka kerja menambah mesej-mesej sebelumnya yang relevan. Apabila anda bertanya "Siapakah nama saya?", sistem sebenarnya menghantar seluruh sejarah perbualan, membolehkan model melihat anda sebelum ini berkata "Nama saya John."

LangChain4j menyediakan pelaksanaan memori yang mengurus ini secara automatik. Anda memilih berapa banyak mesej untuk disimpan dan rangka kerja menguruskan tetingkap konteks.

<img src="../../../translated_images/ms/memory-window.bbe67f597eadabb3.webp" alt="Konsep Tetingkap Memori" width="800"/>

*MessageWindowChatMemory mengekalkan tetingkap gelongsor mesej terkini, secara automatik membuang yang lama*

## Bagaimana Ini Menggunakan LangChain4j

Modul ini melanjutkan pengehadan pantas dengan menggabungkan Spring Boot dan menambah memori perbualan. Berikut cara komponennya bersambung:

**Kebergantungan** - Tambah dua perpustakaan LangChain4j:

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

Pembina membaca kelayakan dari pembolehubah persekitaran yang ditetapkan oleh `azd up`. Menetapkan `baseUrl` kepada titik akhir Azure anda menjadikan klien OpenAI berfungsi dengan Azure OpenAI.

**Memori Perbualan** - Jejak sejarah sembang dengan MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Cipta memori dengan `withMaxMessages(10)` untuk menyimpan 10 mesej terakhir. Tambah mesej pengguna dan AI dengan pembalut berjenis: `UserMessage.from(text)` dan `AiMessage.from(text)`. Dapatkan sejarah dengan `memory.messages()` dan hantarkannya ke model. Perkhidmatan menyimpan instans memori berasingan bagi setiap ID perbualan, membolehkan banyak pengguna berbual serentak.

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) dan tanya:
> - "Bagaimana MessageWindowChatMemory memutuskan mesej mana yang dibuang apabila tetingkap penuh?"
> - "Bolehkah saya melaksanakan storan memori tersuai menggunakan pangkalan data dan bukannya dalam memori?"
> - "Bagaimana saya boleh menambah ringkasan untuk memampatkan sejarah perbualan lama?"

Titik akhir sembang tanpa keadaan mengabaikan memori sepenuhnya - hanya `chatModel.chat(prompt)` seperti pengehadan pantas. Titik akhir berkeadaan menambah mesej ke memori, mengambil sejarah, dan termasuk konteks itu pada setiap permintaan. Konfigurasi model sama, corak berbeza.

## Menyebarkan Infrastruktur Azure OpenAI

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

> **Nota:** Jika anda menghadapi ralat masa tamat (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), hanya jalankan `azd up` sekali lagi. Sumber Azure mungkin masih dalam proses penyediaan di latar belakang, dan cuba semula membenarkan penyebaran selesai apabila sumber mencapai keadaan terminal.

Ini akan:
1. Menyebarkan sumber Azure OpenAI dengan model GPT-5.2 dan text-embedding-3-small
2. Secara automatik menghasilkan fail `.env` di akar projek dengan kelayakan
3. Menyediakan semua pembolehubah persekitaran yang diperlukan

**Mengalami masalah penyebaran?** Lihat [Infrastruktur README](infra/README.md) untuk penyelesaian masalah terperinci termasuk konflik nama subdomain, langkah penyebaran manual di Azure Portal, dan panduan konfigurasi model.

**Sahkan penyebaran berjaya:**

**Bash:**
```bash
cat ../.env  # Perlu menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, dan lain-lain.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Perlu menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, dan lain-lain.
```

> **Nota:** Perintah `azd up` secara automatik menghasilkan fail `.env`. Jika anda perlu mengemas kini kemudian, anda boleh edit fail `.env` secara manual atau menjana semula dengan menjalankan:
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

**Sahkan penyebaran:**

Pastikan fail `.env` wujud dalam direktori akar dengan kelayakan Azure:

**Bash:**
```bash
cat ../.env  # Harus menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Patut menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Mulakan aplikasi:**

**Pilihan 1: Menggunakan Spring Boot Dashboard (Disarankan untuk pengguna VS Code)**

Kontena pembangunan termasuk sambungan Spring Boot Dashboard, yang menyediakan antara muka visual untuk mengurus semua aplikasi Spring Boot. Anda boleh menjumpainya di Bar Aktiviti di sebelah kiri VS Code (cari ikon Spring Boot).

Daripada Spring Boot Dashboard, anda boleh:
- Melihat semua aplikasi Spring Boot yang tersedia dalam ruang kerja
- Mula/henti aplikasi dengan satu klik
- Lihat log aplikasi secara masa nyata
- Pantau status aplikasi

Cuma klik butang main di sebelah "introduction" untuk memulakan modul ini, atau mula semua modul sekaligus.

<img src="../../../translated_images/ms/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

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

Atau mula hanya modul ini:

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

Kedua-dua skrip secara automatik memuatkan pembolehubah persekitaran dari fail `.env` akar dan akan membina JAR jika tidak wujud.

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

Buka http://localhost:8080 dalam penyemak imbas anda.

**Untuk berhenti:**

**Bash:**
```bash
./stop.sh  # Hanya modul ini
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

Aplikasi menyediakan antara muka web dengan dua pelaksanaan sembang bersebelahan.

<img src="../../../translated_images/ms/home-screen.121a03206ab910c0.webp" alt="Skrin Utama Aplikasi" width="800"/>

*Papan pemuka menunjukkan pilihan Sembang Mudah (tanpa keadaan) dan Sembang Perbualan (berkeadaan)*

### Sembang Tanpa Keadaan (Panel Kiri)

Cuba ini dahulu. Tanya "Nama saya John" dan kemudian segera tanya "Siapakah nama saya?" Model tidak akan ingat kerana setiap mesej berdikari. Ini menunjukkan masalah teras dengan integrasi model bahasa asas - tiada konteks perbualan.

<img src="../../../translated_images/ms/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo Sembang Tanpa Keadaan" width="800"/>

*AI tidak mengingati nama anda dari mesej sebelum ini*

### Sembang Berkeadaan (Panel Kanan)

Kini cuba urutan yang sama di sini. Tanya "Nama saya John" dan kemudian "Siapakah nama saya?" Kali ini ia ingat. Perbezaannya ialah MessageWindowChatMemory - ia mengekalkan sejarah perbualan dan memasukkannya dengan setiap permintaan. Inilah cara AI perbualan produksi berfungsi.

<img src="../../../translated_images/ms/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo Sembang Berkeadaan" width="800"/>

*AI mengingati nama anda dari awal perbualan*

Kedua-dua panel menggunakan model GPT-5.2 yang sama. Satu-satunya perbezaan ialah memori. Ini menjelaskan apa yang dibawa oleh memori ke aplikasi anda dan mengapa ia penting untuk kes penggunaan sebenar.

## Langkah Seterusnya

**Modul Seterusnya:** [02-prompt-engineering - Kejuruteraan Arahan dengan GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 00 - Pengenalan Pantas](../00-quick-start/README.md) | [Kembali ke Utama](../README.md) | [Seterusnya: Modul 02 - Kejuruteraan Arahan →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk mencapai ketepatan, sila ambil perhatian bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang sahih. Untuk maklumat penting, terjemahan profesional oleh manusia adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->