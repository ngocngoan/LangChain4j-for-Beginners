# Modul 02: Kejuruteraan Prompt dengan GPT-5.2

## Kandungan

- [Apa yang Anda Akan Pelajari](../../../02-prompt-engineering)
- [Prasyarat](../../../02-prompt-engineering)
- [Memahami Kejuruteraan Prompt](../../../02-prompt-engineering)
- [Bagaimana Ini Menggunakan LangChain4j](../../../02-prompt-engineering)
- [Corak Teras](../../../02-prompt-engineering)
- [Menggunakan Sumber Azure Sedia Ada](../../../02-prompt-engineering)
- [Tangkapan Skrin Aplikasi](../../../02-prompt-engineering)
- [Meneroka Corak](../../../02-prompt-engineering)
  - [Rendah vs Tinggi Semangat](../../../02-prompt-engineering)
  - [Pelaksanaan Tugas (Preambul Alat)](../../../02-prompt-engineering)
  - [Kod Berfikir Sendiri](../../../02-prompt-engineering)
  - [Analisis Berstruktur](../../../02-prompt-engineering)
  - [Sembang Berbilang Giliran](../../../02-prompt-engineering)
  - [Penalaran Langkah Demi Langkah](../../../02-prompt-engineering)
  - [Output Terhad](../../../02-prompt-engineering)
- [Apa yang Anda Sebenarnya Pelajari](../../../02-prompt-engineering)
- [Langkah Seterusnya](../../../02-prompt-engineering)

## Apa yang Anda Akan Pelajari

Dalam modul sebelumnya, anda telah melihat bagaimana memori membolehkan AI perbualan dan menggunakan Model GitHub untuk interaksi asas. Kini kita akan memberi tumpuan kepada cara anda mengemukakan soalan - prompt itu sendiri - menggunakan GPT-5.2 Azure OpenAI. Cara anda menyusun prompt secara dramatik mempengaruhi kualiti jawapan yang anda peroleh.

Kita akan menggunakan GPT-5.2 kerana ia memperkenalkan kawalan penalaran – anda boleh memberitahu model berapa banyak pemikiran yang perlu dilakukan sebelum menjawab. Ini menjadikan pelbagai strategi prompt lebih jelas dan membantu anda memahami bila hendak menggunakan setiap pendekatan. Kita juga akan mendapat manfaat daripada had kadar Azure yang lebih sedikit untuk GPT-5.2 berbanding Model GitHub.

## Prasyarat

- Telah menamatkan Modul 01 (sumber Azure OpenAI telah diterapkan)
- Fail `.env` dalam direktori root dengan kelayakan Azure (dicipta oleh `azd up` dalam Modul 01)

> **Nota:** Jika anda belum menamatkan Modul 01, ikut arahan penerapan di sana terlebih dahulu.

## Memahami Kejuruteraan Prompt

Kejuruteraan prompt adalah tentang mereka bentuk teks input yang konsisten mendapat hasil yang anda perlukan. Ia bukan sekadar bertanya soalan – ia adalah mengenai menyusun permintaan supaya model memahami dengan tepat apa yang anda mahu dan bagaimana untuk menyampaikannya.

Fikirkan seperti memberi arahan kepada rakan sekerja. "Betulkan pepijat" adalah samar-samar. "Betulkan pengecualian penunjuk null dalam UserService.java baris 45 dengan menambah pemeriksaan null" adalah spesifik. Model bahasa juga berfungsi begitu – kekhususan dan struktur penting.

## Bagaimana Ini Menggunakan LangChain4j

Modul ini menunjukkan corak lanjutan dalam prompt menggunakan asas LangChain4j yang sama dari modul sebelumnya, dengan fokus pada struktur prompt dan kawalan penalaran.

<img src="../../../translated_images/ms/langchain4j-flow.48e534666213010b.webp" alt="Aliran LangChain4j" width="800"/>

*Bagaimana LangChain4j menghubungkan prompt anda ke Azure OpenAI GPT-5.2*

**Kebergantungan** - Modul 02 menggunakan kebergantungan langchain4j berikut yang ditakrifkan dalam `pom.xml`:
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

**Konfigurasi OpenAiOfficialChatModel** - [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Model sembang dikonfigurasi secara manual sebagai bean Spring menggunakan klien OpenAI Rasmi, yang menyokong titik akhir Azure OpenAI. Perbezaan utama dari Modul 01 adalah bagaimana kita menyusun prompt yang dihantar ke `chatModel.chat()`, bukan konfigurasi model itu sendiri.

**Mesej Sistem dan Pengguna** - [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j memisahkan jenis mesej untuk kejelasan. `SystemMessage` menetapkan tingkah laku dan konteks AI (seperti "Anda adalah penilai kod"), manakala `UserMessage` mengandungi permintaan sebenar. Pemisahan ini membolehkan anda mengekalkan tingkah laku AI yang konsisten merentasi pertanyaan pengguna yang berbeza.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/ms/message-types.93e0779798a17c9d.webp" alt="Seni Bina Jenis Mesej" width="800"/>

*Mesej Sistem menyediakan konteks berterusan sementara Mesej Pengguna mengandungi permintaan individu*

**MessageWindowChatMemory untuk Berbilang Giliran** - Untuk corak perbualan berbilang giliran, kita menggunakan semula `MessageWindowChatMemory` dari Modul 01. Setiap sesi mendapat contoh memori sendiri yang disimpan dalam `Map<String, ChatMemory>`, membolehkan pelbagai perbualan serentak tanpa konteks bercampur.

**Templat Prompt** - Fokus sebenar di sini adalah kejuruteraan prompt, bukan API LangChain4j baru. Setiap corak (semangat rendah, semangat tinggi, pelaksanaan tugas, dsb.) menggunakan kaedah yang sama `chatModel.chat(prompt)` tetapi dengan rentetan prompt yang disusun dengan teliti. Tag XML, arahan, dan pemformatan adalah sebahagian daripada teks prompt, bukan ciri LangChain4j.

**Kawalan Penalaran** - Usaha penalaran GPT-5.2 dikawal melalui arahan prompt seperti "maksimum 2 langkah penalaran" atau "teroka secara menyeluruh". Ini adalah teknik kejuruteraan prompt, bukan konfigurasi LangChain4j. Perpustakaan hanya menyampaikan prompt anda ke model.

Intipati penting: LangChain4j menyediakan infrastruktur (sambungan model melalui [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), memori, pengurusan mesej melalui [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), manakala modul ini mengajar anda cara mengarang prompt berkesan dalam infrastruktur itu.

## Corak Teras

Tidak semua masalah memerlukan pendekatan yang sama. Ada soalan memerlukan jawapan cepat, ada yang memerlukan pemikiran mendalam. Ada yang memerlukan penalaran yang kelihatan, ada yang cuma perlu hasil. Modul ini merangkumi lapan corak prompt - setiap satu dioptimumkan untuk senario berbeza. Anda akan mencuba semua untuk belajar bila setiap pendekatan berkesan.

<img src="../../../translated_images/ms/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Lapan Corak Prompt" width="800"/>

*Gambaran keseluruhan lapan corak kejuruteraan prompt dan kes penggunaannya*

<img src="../../../translated_images/ms/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Perbandingan Usaha Penalaran" width="800"/>

*Pendekatan penalaran semangat rendah (pantas, langsung) vs semangat tinggi (teliti, eksploratori)*

**Semangat Rendah (Cepat & Fokus)** - Untuk soalan mudah di mana anda mahu jawapan pantas dan langsung. Model melakukan penalaran minimum - maksimum 2 langkah. Gunakan ini untuk pengiraan, carian, atau soalan mudah.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Terokai dengan GitHub Copilot:** Buka [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) dan tanya:
> - "Apa perbezaan antara corak prompt semangat rendah dan tinggi?"
> - "Bagaimana tag XML dalam prompt membantu menyusun jawapan AI?"
> - "Bila saya harus gunakan corak refleksi sendiri vs arahan langsung?"

**Semangat Tinggi (Mendalam & Teliti)** - Untuk masalah kompleks di mana anda mahukan analisis menyeluruh. Model meneroka dengan teliti dan menunjukkan penalaran terperinci. Gunakan ini untuk reka bentuk sistem, keputusan seni bina, atau penyelidikan kompleks.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Pelaksanaan Tugas (Kemajuan Langkah demi Langkah)** - Untuk aliran kerja berbilang langkah. Model memberi pelan awal, menceritakan setiap langkah semasa ia berfungsi, kemudian memberikan ringkasan. Gunakan ini untuk migrasi, pelaksanaan, atau proses berbilang langkah lain.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Prompt Rantaian Pemikiran secara eksplisit meminta model menunjuk proses penalarannya, memperbaiki ketepatan untuk tugas kompleks. Pecahan langkah demi langkah membantu manusia dan AI memahami logik.

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Sembang:** Tanya tentang corak ini:
> - "Bagaimana saya menyesuaikan corak pelaksanaan tugas untuk operasi jangka panjang?"
> - "Apa amalan terbaik untuk menyusun preambul alat dalam aplikasi pengeluaran?"
> - "Bagaimana saya boleh menangkap dan memaparkan kemas kini kemajuan pertengahan dalam UI?"

<img src="../../../translated_images/ms/task-execution-pattern.9da3967750ab5c1e.webp" alt="Corak Pelaksanaan Tugas" width="800"/>

*Rancang → Laksanakan → Rumuskan aliran kerja untuk tugas berbilang langkah*

**Kod Berfikir Sendiri** - Untuk menjana kod berkualiti pengeluaran. Model menjana kod, memeriksa kriteria kualiti, dan menambah baik secara berulang. Gunakan ini semasa membina ciri atau perkhidmatan baru.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ms/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Kitaran Refleksi Diri" width="800"/>

*Gelung penambahbaikan iteratif - jana, nilai, kenal pasti isu, perbaiki, ulang*

**Analisis Berstruktur** - Untuk penilaian konsisten. Model menyemak kod menggunakan rangka kerja tetap (ketepatan, amalan, prestasi, keselamatan). Gunakan ini untuk semakan kod atau penilaian kualiti.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Sembang:** Tanya tentang analisis berstruktur:
> - "Bagaimana saya boleh sesuaikan rangka kerja analisis untuk jenis semakan kod yang berbeza?"
> - "Apakah cara terbaik untuk mengurai dan bertindak ke atas output berstruktur secara programatik?"
> - "Bagaimana saya memastikan tahap keterukan konsisten merentasi sesi semakan berbeza?"

<img src="../../../translated_images/ms/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Corak Analisis Berstruktur" width="800"/>

*Rangka kerja empat kategori untuk semakan kod konsisten dengan tahap keterukan*

**Sembang Berbilang Giliran** - Untuk perbualan yang memerlukan konteks. Model mengingati mesej sebelumnya dan membina daripadanya. Gunakan ini untuk sesi bantuan interaktif atau soalan kompleks.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ms/context-memory.dff30ad9fa78832a.webp" alt="Memori Konteks" width="800"/>

*Bagaimana konteks perbualan terkumpul sepanjang beberapa giliran sehingga mencapai had token*

**Penalaran Langkah Demi Langkah** - Untuk masalah yang memerlukan logik yang kelihatan. Model menunjukkan penalaran eksplisit untuk setiap langkah. Gunakan ini untuk masalah matematik, teka-teki logik, atau apabila anda perlu memahami proses pemikiran.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ms/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Corak Langkah Demi Langkah" width="800"/>

*Memecah masalah kepada langkah logik yang eksplisit*

**Output Terhad** - Untuk jawapan dengan syarat format tertentu. Model mematuhi peraturan format dan panjang dengan ketat. Gunakan ini untuk ringkasan atau apabila anda memerlukan struktur output yang tepat.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ms/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Corak Output Terhad" width="800"/>

*Menguatkuasakan syarat format, panjang, dan struktur tertentu*

## Menggunakan Sumber Azure Sedia Ada

**Sahkan penerapan:**

Pastikan fail `.env` wujud dalam direktori root dengan kelayakan Azure (dicipta semasa Modul 01):
```bash
cat ../.env  # Harus menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Mulakan aplikasi:**

> **Nota:** Jika anda sudah memulakan semua aplikasi menggunakan `./start-all.sh` dari Modul 01, modul ini sudah berjalan di port 8083. Anda boleh abaikan arahan mula di bawah dan terus ke http://localhost:8083.

**Pilihan 1: Menggunakan Spring Boot Dashboard (Disyorkan untuk pengguna VS Code)**

Container pembangunan termasuk sambungan Spring Boot Dashboard, yang menyediakan antara muka visual untuk mengurus semua aplikasi Spring Boot. Anda boleh dapati di Bar Aktiviti di sebelah kiri VS Code (cari ikon Spring Boot).

Daripada Spring Boot Dashboard, anda boleh:
- Melihat semua aplikasi Spring Boot yang tersedia dalam ruang kerja
- Mula/hentikan aplikasi dengan satu klik
- Melihat log aplikasi secara masa nyata
- Memantau status aplikasi

Cuma klik butang main di sebelah "prompt-engineering" untuk mulakan modul ini, atau mulakan semua modul sekaligus.

<img src="../../../translated_images/ms/dashboard.da2c2130c904aaf0.webp" alt="Papan Pemuka Spring Boot" width="400"/>

**Pilihan 2: Menggunakan skrip shell**

Mula semua aplikasi web (modul 01-04):

**Bash:**
```bash
cd ..  # Dari direktori akar
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Dari direktori root
.\start-all.ps1
```

Atau mulakan modul ini sahaja:

**Bash:**
```bash
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

Kedua-dua skrip secara automatik memuat pembolehubah persekitaran dari fail `.env` root dan akan bina JAR jika belum ada.

> **Nota:** Jika anda mahu bina semua modul secara manual sebelum memulakan:
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

Buka http://localhost:8083 dalam pelayar anda.

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

## Tangkapan Skrin Aplikasi

<img src="../../../translated_images/ms/dashboard-home.5444dbda4bc1f79d.webp" alt="Laman Utama Papan Pemuka" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Papan pemuka utama menunjukkan lapan corak kejuruteraan prompt dengan ciri dan kes penggunaannya*

## Meneroka Corak

Antara muka web membolehkan anda mencuba strategi prompt yang berbeza. Setiap corak menyelesaikan masalah berbeza – cuba untuk lihat bila setiap pendekatan menyerlah.

### Rendah vs Tinggi Semangat

Tanya soalan mudah seperti "Apakah 15% dari 200?" menggunakan Semangat Rendah. Anda akan mendapat jawapan segera dan langsung. Kini tanya sesuatu yang kompleks seperti "Reka strategi caching untuk API trafik tinggi" menggunakan Semangat Tinggi. Perhatikan bagaimana model melambat dan memberikan penalaran terperinci. Model sama, struktur soalan sama – tetapi prompt memberitahu berapa banyak pemikiran yang perlu dilakukan.
<img src="../../../translated_images/ms/low-eagerness-demo.898894591fb23aa0.webp" alt="Demo Keinginan Rendah" width="800"/>

*Pengiraan pantas dengan pemikiran minimum*

<img src="../../../translated_images/ms/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Demo Keinginan Tinggi" width="800"/>

*Strategi cache menyeluruh (2.8MB)*

### Pelaksanaan Tugasan (Pendahuluan Alat)

Aliran kerja berbilang langkah mendapat manfaat daripada perancangan awal dan narasi kemajuan. Model ini menggariskan apa yang akan dilakukan, menceritakan setiap langkah, kemudian merumuskan hasil.

<img src="../../../translated_images/ms/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Demo Pelaksanaan Tugasan" width="800"/>

*Mewujudkan titik akhir REST dengan narasi langkah demi langkah (3.9MB)*

### Kod Self-Reflecting

Cuba "Buat perkhidmatan pengesahan e-mel". Daripada hanya menjana kod dan berhenti, model ini menjana, menilai berdasarkan kriteria kualiti, mengenal pasti kelemahan, dan memperbaiki. Anda akan melihat ia berulang sehingga kod memenuhi piawaian pengeluaran.

<img src="../../../translated_images/ms/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Demo Kod Self-Reflecting" width="800"/>

*Perkhidmatan pengesahan e-mel lengkap (5.2MB)*

### Analisis Berstruktur

Semakan kod memerlukan rangka kerja penilaian yang konsisten. Model ini menganalisis kod menggunakan kategori tetap (ketepatan, amalan, prestasi, keselamatan) dengan tahap keterukan.

<img src="../../../translated_images/ms/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Demo Analisis Berstruktur" width="800"/>

*Semakan kod berdasarkan rangka kerja*

### Sembang Berbilang Giliran

Tanya "Apa itu Spring Boot?" kemudian teruskan dengan "Tunjukkan contoh". Model ingat soalan pertama anda dan memberikan contoh Spring Boot khusus. Tanpa memori, soalan kedua itu akan terlalu kabur.

<img src="../../../translated_images/ms/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Demo Sembang Berbilang Giliran" width="800"/>

*Pemeliharaan konteks merentasi soalan*

### Pemikiran Langkah Demi Langkah

Pilih masalah matematik dan cuba dengan kedua-dua Pemikiran Langkah Demi Langkah dan Keinginan Rendah. Keinginan rendah hanya memberikan jawapan - pantas tetapi tidak telus. Langkah demi langkah menunjukkan setiap pengiraan dan keputusan.

<img src="../../../translated_images/ms/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Demo Pemikiran Langkah Demi Langkah" width="800"/>

*Masalah matematik dengan langkah yang terperinci*

### Output Terhad

Apabila anda memerlukan format spesifik atau bilangan perkataan, corak ini menguatkuasakan pematuhan yang ketat. Cuba menjana ringkasan dengan tepat 100 perkataan dalam format berbulet.

<img src="../../../translated_images/ms/constrained-output-demo.567cc45b75da1633.webp" alt="Demo Output Terhad" width="800"/>

*Ringkasan pembelajaran mesin dengan kawalan format*

## Apa Yang Anda Sebenarnya Belajar

**Usaha Berfikir Mengubah Segalanya**

GPT-5.2 membenarkan anda mengawal usaha pengiraan melalui prompt anda. Usaha rendah bermaksud respons cepat dengan penerokaan minimum. Usaha tinggi bermaksud model mengambil masa untuk berfikir secara mendalam. Anda belajar menyesuaikan usaha mengikut kerumitan tugasan - jangan bazir masa pada soalan mudah, tetapi jangan tergesa-gesa membuat keputusan kompleks juga.

**Struktur Membimbing Tingkah Laku**

Perasan tag XML dalam prompt? Ia bukan hiasan. Model mengikuti arahan berstruktur dengan lebih boleh dipercayai berbanding teks bebas. Apabila anda memerlukan proses berbilang langkah atau logik kompleks, struktur membantu model menjejak di mana ia berada dan apa yang seterusnya.

<img src="../../../translated_images/ms/prompt-structure.a77763d63f4e2f89.webp" alt="Struktur Prompt" width="800"/>

*Anatomi prompt berstruktur dengan bahagian jelas dan organisasi gaya XML*

**Kualiti Melalui Penilaian Diri**

Corak self-reflecting berfungsi dengan menjadikan kriteria kualiti jelas. Daripada berharap model "melakukannya dengan betul", anda memberitahunya tepat apa maksud "betul": logik tepat, pengendalian ralat, prestasi, keselamatan. Model kemudian boleh menilai hasilnya sendiri dan memperbaiki. Ini menjadikan penjanaan kod dari loteri menjadi proses.

**Konteks Adalah Terhad**

Perbualan berbilang giliran berfungsi dengan menyertakan sejarah mesej dengan setiap permintaan. Tetapi ada had - setiap model mempunyai bilangan token maksimum. Apabila perbualan berkembang, anda perlukan strategi untuk mengekalkan konteks relevan tanpa melebihi had itu. Modul ini menunjukkan bagaimana memori berfungsi; kemudian anda akan belajar bila untuk meringkaskan, bila untuk melupakan, dan bila untuk mengambil semula.

## Langkah Seterusnya

**Modul Seterusnya:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 01 - Pengenalan](../01-introduction/README.md) | [Kembali ke Utama](../README.md) | [Seterusnya: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila ambil perhatian bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya hendaklah dianggap sebagai sumber yang sahih. Untuk maklumat penting, terjemahan profesional oleh manusia adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau tafsiran yang salah yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->