# Modul 01: Mula dengan LangChain4j

## Jadual Kandungan

- [Video Panduan](../../../01-introduction)
- [Apa yang Anda Akan Pelajari](../../../01-introduction)
- [Prasyarat](../../../01-introduction)
- [Memahami Masalah Teras](../../../01-introduction)
- [Memahami Token](../../../01-introduction)
- [Bagaimana Memori Berfungsi](../../../01-introduction)
- [Bagaimana Ini Menggunakan LangChain4j](../../../01-introduction)
- [Menyebarkan Infrastruktur Azure OpenAI](../../../01-introduction)
- [Menjalankan Aplikasi Secara Tempatan](../../../01-introduction)
- [Menggunakan Aplikasi](../../../01-introduction)
  - [Sembang Tanpa Keadaan (Panel Kiri)](../../../01-introduction)
  - [Sembang Berkeadaan (Panel Kanan)](../../../01-introduction)
- [Langkah Seterusnya](../../../01-introduction)

## Video Panduan

Tonton sesi langsung ini yang menerangkan cara memulakan modul ini: [Mula dengan LangChain4j - Sesi Langsung](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Apa yang Anda Akan Pelajari

Jika anda telah menyelesaikan permulaan pantas, anda telah melihat bagaimana untuk menghantar arahan dan mendapatkan respons. Itulah asasnya, tetapi aplikasi sebenar memerlukan lebih banyak. Modul ini mengajar anda cara membina AI perbualan yang mengingati konteks dan mengekalkan keadaan - perbezaan antara demo sekali sahaja dan aplikasi yang sedia untuk produksi.

Kami akan menggunakan GPT-5.2 Azure OpenAI sepanjang panduan ini kerana keupayaan penalaran canggihnya menjadikan tingkah laku corak yang berbeza lebih jelas. Apabila anda menambah memori, anda akan dapat melihat perbezaannya dengan jelas. Ini memudahkan pemahaman apa yang dibawa setiap komponen ke aplikasi anda.

Anda akan membina satu aplikasi yang menunjukkan kedua-dua corak:

**Sembang Tanpa Keadaan** - Setiap permintaan adalah bebas. Model tidak ingat mesej sebelum ini. Ini adalah corak yang anda gunakan dalam permulaan pantas.

**Perbualan Berkeadaan** - Setiap permintaan termasuk sejarah perbualan. Model mengekalkan konteks merentasi beberapa pusingan. Ini yang diperlukan oleh aplikasi produksi.

## Prasyarat

- Langganan Azure dengan akses Azure OpenAI
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Nota:** Java, Maven, Azure CLI dan Azure Developer CLI (azd) telah dipasang terlebih dahulu dalam devcontainer yang disediakan.

> **Nota:** Modul ini menggunakan GPT-5.2 pada Azure OpenAI. Penyebaran dikonfigurasi secara automatik melalui `azd up` - jangan ubah nama model dalam kod.

## Memahami Masalah Teras

Model bahasa adalah tanpa keadaan. Setiap panggilan API adalah bebas. Jika anda menghantar "Nama saya John" dan kemudian bertanya "Siapa nama saya?", model tidak tahu anda baru sahaja memperkenalkan diri. Ia menganggap setiap permintaan seperti ia perbualan pertama yang anda pernah ada.

Ini sesuai untuk soalan dan jawapan mudah tetapi tidak berguna untuk aplikasi sebenar. Bot khidmat pelanggan perlu mengingati apa yang anda beritahu mereka. Pembantu peribadi memerlukan konteks. Mana-mana perbualan multi-pusing memerlukan memori.

<img src="../../../translated_images/ms/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Perbualan Tanpa Keadaan vs Berkeadaan" width="800"/>

*Perbezaan antara perbualan tanpa keadaan (panggilan bebas) dan berkeadaan (sedar konteks)*

## Memahami Token

Sebelum menyelami perbualan, penting untuk memahami token - unit asas teks yang diproses oleh model bahasa:

<img src="../../../translated_images/ms/token-explanation.c39760d8ec650181.webp" alt="Penjelasan Token" width="800"/>

*Contoh bagaimana teks dipecahkan menjadi token - "I love AI!" menjadi 4 unit pemprosesan berasingan*

Token adalah cara model AI mengukur dan memproses teks. Perkataan, tanda baca, malah ruang boleh menjadi token. Model anda mempunyai had berapa banyak token yang boleh diproses sekaligus (400,000 untuk GPT-5.2, dengan sehingga 272,000 token input dan 128,000 token output). Memahami token membantu anda mengurus panjang perbualan dan kos.

## Bagaimana Memori Berfungsi

Memori sembang menyelesaikan masalah tanpa keadaan dengan mengekalkan sejarah perbualan. Sebelum menghantar permintaan anda ke model, rangka kerja menambah mesej sebelumnya yang relevan di hadapan. Apabila anda bertanya "Siapa nama saya?", sistem sebenarnya menghantar keseluruhan sejarah perbualan, membolehkan model melihat bahawa anda sebelum ini berkata "Nama saya John."

LangChain4j menyediakan pelaksanaan memori yang mengendalikan ini secara automatik. Anda memilih berapa banyak mesej untuk disimpan dan rangka kerja mengurus tetingkap konteks.

<img src="../../../translated_images/ms/memory-window.bbe67f597eadabb3.webp" alt="Konsep Tetingkap Memori" width="800"/>

*MessageWindowChatMemory mengekalkan tetingkap gelongsor mesej terbaru, secara automatik membuang mesej lama*

## Bagaimana Ini Menggunakan LangChain4j

Modul ini melanjutkan permulaan pantas dengan mengintegrasikan Spring Boot dan menambah memori perbualan. Berikut adalah bagaimana bahagian-bahagian bersatu:

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
  
Pembina membaca kelayakan dari pembolehubah persekitaran yang ditetapkan oleh `azd up`. Menetapkan `baseUrl` ke endpoint Azure anda menjadikan klien OpenAI berfungsi dengan Azure OpenAI.

**Memori Perbualan** - Jejaki sejarah sembang dengan MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```
  
Buat memori dengan `withMaxMessages(10)` untuk menyimpan 10 mesej terakhir. Tambah mesej pengguna dan AI dengan pembalut bertipe: `UserMessage.from(text)` dan `AiMessage.from(text)`. Dapatkan sejarah dengan `memory.messages()` dan hantar ke model. Perkhidmatan menyimpan instance memori berasingan bagi setiap ID perbualan, membenarkan pelbagai pengguna berbual serentak.

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) dan tanya:  
> - "Bagaimana MessageWindowChatMemory menentukan mesej mana yang dibuang apabila tetingkap penuh?"  
> - "Bolehkah saya melaksanakan penyimpanan memori tersuai menggunakan pangkalan data dan bukannya dalam memori?"  
> - "Bagaimana saya menambah ringkasan untuk memampatkan sejarah perbualan lama?"

Endpoint sembang tanpa keadaan melangkau memori sepenuhnya - cuma `chatModel.chat(prompt)` seperti permulaan pantas. Endpoint berkeadaan menambah mesej ke memori, mendapatkan sejarah, dan menyertakan konteks itu dengan setiap permintaan. Konfigurasi model sama, corak berbeza.

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
  
> **Nota:** Jika anda menghadapi ralat had masa tamat (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), jalankan sahaja `azd up` sekali lagi. Sumber Azure mungkin masih dalam penyediaan di latar belakang, dan mencuba semula membolehkan penyebaran selesai apabila sumber mencapai keadaan terminal.

Ini akan:  
1. Menyebarkan sumber Azure OpenAI dengan model GPT-5.2 dan text-embedding-3-small  
2. Menjana fail `.env` secara automatik dalam akar projek dengan kelayakan  
3. Menyediakan semua pembolehubah persekitaran yang diperlukan  

**Mengalami masalah penyebaran?** Lihat [README Infrastruktur](infra/README.md) untuk panduan menyelesaikan masalah termasuk konflik nama subdomain, langkah penyebaran manual Azure Portal, dan panduan konfigurasi model.

**Sahkan penyebaran berjaya:**  

**Bash:**  
```bash
cat ../.env  # Patut menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, dan lain-lain.
```
  
**PowerShell:**  
```powershell
Get-Content ..\.env  # Perlu menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, dll.
```
  
> **Nota:** Perintah `azd up` menjana fail `.env` secara automatik. Jika perlu kemaskini kemudian, anda boleh sama ada mengedit fail `.env` secara manual atau menjana semula dengan menjalankan:  
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
  
## Menjalankan Aplikasi Secara Tempatan

**Sahkan penyebaran:**  

Pastikan fail `.env` wujud dalam direktori akar dengan kelayakan Azure:

**Bash:**  
```bash
cat ../.env  # Patut menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, PENDEPLOYAN
```
  
**PowerShell:**  
```powershell
Get-Content ..\.env  # Harus menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Mulakan aplikasi:**  

**Pilihan 1: Menggunakan Spring Boot Dashboard (Disyorkan untuk pengguna VS Code)**

Dev container termasuk sambungan Spring Boot Dashboard, yang menyediakan antara muka visual untuk mengurus semua aplikasi Spring Boot. Anda boleh menemuinya di Bar Aktiviti di sebelah kiri VS Code (cari ikon Spring Boot).

Dari Spring Boot Dashboard, anda boleh:  
- Melihat semua aplikasi Spring Boot tersedia dalam ruang kerja  
- Mula/berhenti aplikasi dengan satu klik  
- Melihat log aplikasi secara langsung  
- Memantau status aplikasi  

Klik butang main di sebelah "introduction" untuk memulakan modul ini, atau mula semua modul sekaligus.

<img src="../../../translated_images/ms/dashboard.69c7479aef09ff6b.webp" alt="Papan Pemuka Spring Boot" width="400"/>

**Pilihan 2: Menggunakan skrip shell**

Mula semua aplikasi web (modul 01-04):

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
  
Kedua-dua skrip secara automatik memuatkan pembolehubah persekitaran dari fail `.env` akar dan akan bina JAR jika belum ada.

> **Nota:** Jika anda lebih suka membina semua modul secara manual sebelum mula:  
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

Aplikasi menyediakan antara muka web dengan dua pelaksanaan sembang bersebelahan.

<img src="../../../translated_images/ms/home-screen.121a03206ab910c0.webp" alt="Skrin Utama Aplikasi" width="800"/>

*Papan pemuka menunjukkan kedua-dua pilihan Sembang Mudah (tanpa keadaan) dan Sembang Perbualan (berkeadaan)*

### Sembang Tanpa Keadaan (Panel Kiri)

Cuba ini dahulu. Tanya "Nama saya John" dan kemudian terus bertanya "Siapa nama saya?" Model tidak akan ingat kerana setiap mesej adalah bebas. Ini menunjukkan masalah teras dengan integrasi model bahasa asas - tiada konteks perbualan.

<img src="../../../translated_images/ms/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo Sembang Tanpa Keadaan" width="800"/>

*AI tidak mengingati nama anda dari mesej sebelumnya*

### Sembang Berkeadaan (Panel Kanan)

Sekarang cuba urutan yang sama di sini. Tanya "Nama saya John" dan kemudian "Siapa nama saya?" Kali ini ia ingat. Perbezaannya ialah MessageWindowChatMemory - ia mengekalkan sejarah perbualan dan termasuknya dengan setiap permintaan. Ini cara AI perbualan produksi berfungsi.

<img src="../../../translated_images/ms/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo Sembang Berkeadaan" width="800"/>

*AI mengingati nama anda dari awal perbualan*

Kedua-dua panel menggunakan model GPT-5.2 yang sama. Satu-satunya perbezaan adalah memori. Ini menjelaskan apa yang dibawa oleh memori ke aplikasi anda dan mengapa ia penting untuk kes penggunaan sebenar.

## Langkah Seterusnya

**Modul Seterusnya:** [02-prompt-engineering - Kejuruteraan Arahan dengan GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 00 - Permulaan Pantas](../00-quick-start/README.md) | [Kembali ke Utama](../README.md) | [Seterusnya: Modul 02 - Kejuruteraan Arahan →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila ambil maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya hendaklah dianggap sebagai sumber yang sahih. Untuk maklumat penting, terjemahan profesional oleh manusia adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau tafsiran yang salah yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->