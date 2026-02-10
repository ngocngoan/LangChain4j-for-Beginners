# Modul 02: Rekayasa Prompt dengan GPT-5.2

## Daftar Isi

- [Apa yang Akan Anda Pelajari](../../../02-prompt-engineering)
- [Prasyarat](../../../02-prompt-engineering)
- [Memahami Rekayasa Prompt](../../../02-prompt-engineering)
- [Bagaimana Ini Menggunakan LangChain4j](../../../02-prompt-engineering)
- [Pola Inti](../../../02-prompt-engineering)
- [Menggunakan Sumber Daya Azure yang Ada](../../../02-prompt-engineering)
- [Tangkapan Layar Aplikasi](../../../02-prompt-engineering)
- [Menjelajahi Pola](../../../02-prompt-engineering)
  - [Eagerness Rendah vs Tinggi](../../../02-prompt-engineering)
  - [Eksekusi Tugas (Preambel Alat)](../../../02-prompt-engineering)
  - [Kode yang Merefleksikan Diri](../../../02-prompt-engineering)
  - [Analisis Terstruktur](../../../02-prompt-engineering)
  - [Chat Multi-Turn](../../../02-prompt-engineering)
  - [Penalaran Langkah-demi-Langkah](../../../02-prompt-engineering)
  - [Output Terbatas](../../../02-prompt-engineering)
- [Apa yang Sebenarnya Anda Pelajari](../../../02-prompt-engineering)
- [Langkah Berikutnya](../../../02-prompt-engineering)

## Apa yang Akan Anda Pelajari

Pada modul sebelumnya, Anda melihat bagaimana memori memungkinkan AI percakapan dan menggunakan Model GitHub untuk interaksi dasar. Sekarang kita akan fokus pada bagaimana Anda mengajukan pertanyaan — prompt itu sendiri — menggunakan GPT-5.2 dari Azure OpenAI. Cara Anda menyusun prompt sangat memengaruhi kualitas jawaban yang Anda dapatkan.

Kita akan menggunakan GPT-5.2 karena model ini memperkenalkan kontrol penalaran — Anda dapat memberitahu model seberapa banyak pemikiran yang harus dilakukan sebelum menjawab. Ini membuat strategi prompting yang berbeda menjadi lebih jelas dan membantu Anda memahami kapan harus menggunakan setiap pendekatan. Kita juga akan mendapat keuntungan dari batasan rate yang lebih sedikit pada GPT-5.2 di Azure dibandingkan Model GitHub.

## Prasyarat

- Telah menyelesaikan Modul 01 (sumber daya Azure OpenAI sudah dideploy)
- File `.env` ada di direktori root dengan kredensial Azure (dibuat oleh `azd up` di Modul 01)

> **Catatan:** Jika Anda belum menyelesaikan Modul 01, ikuti dulu instruksi deployment di sana.

## Memahami Rekayasa Prompt

Rekayasa prompt adalah tentang merancang teks input yang secara konsisten memberikan hasil yang Anda butuhkan. Ini bukan hanya tentang mengajukan pertanyaan — tetapi tentang menyusun permintaan agar model benar-benar memahami apa yang Anda inginkan dan bagaimana menyampaikannya.

Pikirkan seperti memberi instruksi kepada rekan kerja. "Perbaiki bug" itu samar. "Perbaiki null pointer exception di UserService.java baris 45 dengan menambahkan pengecekan null" itu spesifik. Model bahasa bekerja dengan cara yang sama — spesifikasi dan struktur sangat penting.

## Bagaimana Ini Menggunakan LangChain4j

Modul ini menunjukkan pola prompting lanjutan menggunakan fondasi LangChain4j yang sama dari modul sebelumnya, dengan fokus pada struktur prompt dan kontrol penalaran.

<img src="../../../translated_images/id/langchain4j-flow.48e534666213010b.webp" alt="Alur LangChain4j" width="800"/>

*Bagaimana LangChain4j menghubungkan prompt Anda ke Azure OpenAI GPT-5.2*

**Dependencies** – Modul 02 menggunakan dependensi langchain4j berikut yang didefinisikan di `pom.xml`:
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

**Konfigurasi OpenAiOfficialChatModel** – [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Model chat dikonfigurasi secara manual sebagai Spring bean menggunakan klien resmi OpenAI, yang mendukung endpoint Azure OpenAI. Perbedaan utama dari Modul 01 adalah bagaimana kita menyusun prompt yang dikirim ke `chatModel.chat()`, bukan pengaturan model itu sendiri.

**Pesan Sistem dan Pengguna** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j memisahkan tipe pesan untuk kejelasan. `SystemMessage` menetapkan perilaku dan konteks AI (misalnya "Anda adalah peninjau kode"), sementara `UserMessage` berisi permintaan sebenarnya. Pemisahan ini memungkinkan Anda menjaga perilaku AI yang konsisten di berbagai kueri pengguna.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/id/message-types.93e0779798a17c9d.webp" alt="Arsitektur Jenis Pesan" width="800"/>

*SystemMessage menyediakan konteks persisten sementara UserMessages memuat permintaan individual*

**MessageWindowChatMemory untuk Multi-Turn** – Untuk pola percakapan multi-turn, kami menggunakan kembali `MessageWindowChatMemory` dari Modul 01. Setiap sesi mendapat instance memori sendiri yang disimpan dalam `Map<String, ChatMemory>`, memungkinkan beberapa percakapan simultan tanpa campur konteks.

**Template Prompt** – Fokus sebenarnya di sini adalah rekayasa prompt, bukan API LangChain4j baru. Setiap pola (low eagerness, high eagerness, eksekusi tugas, dll.) menggunakan metode `chatModel.chat(prompt)` yang sama tetapi dengan string prompt yang disusun dengan cermat. Tag XML, instruksi, dan format semuanya bagian dari teks prompt, bukan fitur LangChain4j.

**Kontrol Penalaran** – Upaya penalaran GPT-5.2 dikontrol melalui instruksi prompt seperti "maksimum 2 langkah penalaran" atau "jelajahi secara menyeluruh". Ini adalah teknik rekayasa prompt, bukan konfigurasi LangChain4j. Perpustakaan hanya menyampaikan prompt Anda ke model.

Inti pembelajaran: LangChain4j menyediakan infrastrukturnya (koneksi model lewat [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), memori, penanganan pesan via [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), sementara modul ini mengajarkan Anda cara membuat prompt efektif dalam infrastruktur tersebut.

## Pola Inti

Tidak semua masalah membutuhkan pendekatan yang sama. Beberapa pertanyaan perlu jawaban cepat, yang lain memerlukan pemikiran mendalam. Ada yang butuh penalaran terlihat, ada yang hanya perlu hasil. Modul ini membahas delapan pola prompting—masing-masing dioptimalkan untuk skenario berbeda. Anda akan bereksperimen dengan semuanya untuk mempelajari kapan setiap pendekatan terbaik dipakai.

<img src="../../../translated_images/id/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Delapan Pola Prompting" width="800"/>

*Gambaran delapan pola rekayasa prompt beserta kasus penggunaannya*

<img src="../../../translated_images/id/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Perbandingan Upaya Penalaran" width="800"/>

*Pendekatan penalaran Eagerness Rendah (cepat, langsung) vs Eagerness Tinggi (mendalam, eksploratif)*

**Eagerness Rendah (Cepat & Terfokus)** – Untuk pertanyaan sederhana di mana Anda ingin jawaban cepat dan langsung. Model melakukan penalaran minimal - maksimum 2 langkah. Gunakan ini untuk perhitungan, pencarian, atau pertanyaan langsung.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Eksplorasi dengan GitHub Copilot:** Buka [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) dan tanyakan:
> - "Apa perbedaan pola prompting eagerness rendah dan tinggi?"
> - "Bagaimana tag XML dalam prompt membantu menyusun respons AI?"
> - "Kapan saya harus menggunakan pola refleksi diri vs instruksi langsung?"

**Eagerness Tinggi (Mendalam & Menyeluruh)** – Untuk masalah kompleks di mana Anda menginginkan analisis komprehensif. Model mengeksplor secara menyeluruh dan menampilkan penalaran rinci. Gunakan ini untuk desain sistem, keputusan arsitektur, atau riset kompleks.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Eksekusi Tugas (Progres Langkah-demi-Langkah)** – Untuk alur kerja multi-langkah. Model memberi rencana di depan, menjelaskan tiap langkah saat berjalan, lalu memberikan ringkasan. Gunakan ini untuk migrasi, implementasi, atau proses multi-langkah apa pun.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Prompt Chain-of-Thought secara eksplisit meminta model menunjukkan proses penalarannya, meningkatkan akurasi untuk tugas kompleks. Pemecahan langkah-demi-langkah ini membantu manusia dan AI memahami logika.

> **🤖 Coba dengan Chat [GitHub Copilot](https://github.com/features/copilot):** Tanyakan tentang pola ini:
> - "Bagaimana saya menyesuaikan pola eksekusi tugas untuk operasi jangka panjang?"
> - "Apa praktik terbaik menyusun preambel alat di aplikasi produksi?"
> - "Bagaimana saya menangkap dan menampilkan pembaruan progres antara di UI?"

<img src="../../../translated_images/id/task-execution-pattern.9da3967750ab5c1e.webp" alt="Pola Eksekusi Tugas" width="800"/>

*Alur kerja Rencana → Eksekusi → Ringkas untuk tugas multi-langkah*

**Kode yang Merefleksikan Diri** – Untuk menghasilkan kode kualitas produksi. Model menghasilkan kode, memeriksa berdasarkan kriteria kualitas, dan memperbaikinya secara iteratif. Gunakan ini saat membangun fitur atau layanan baru.

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

<img src="../../../translated_images/id/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Siklus Refleksi Diri" width="800"/>

*Loop perbaikan iteratif — menghasilkan, evaluasi, identifikasi masalah, perbaikan, ulangi*

**Analisis Terstruktur** – Untuk evaluasi yang konsisten. Model meninjau kode menggunakan kerangka kerja tetap (kebenaran, praktik, performa, keamanan). Gunakan ini untuk review kode atau penilaian kualitas.

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

> **🤖 Coba dengan Chat [GitHub Copilot](https://github.com/features/copilot):** Tanyakan tentang analisis terstruktur:
> - "Bagaimana cara menyesuaikan kerangka analisis untuk berbagai jenis review kode?"
> - "Apa cara terbaik mem-parsing dan bertindak atas output terstruktur secara programatik?"
> - "Bagaimana memastikan tingkat keparahan konsisten di berbagai sesi review?"

<img src="../../../translated_images/id/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Pola Analisis Terstruktur" width="800"/>

*Kerangka empat kategori untuk review kode konsisten dengan level keparahan*

**Chat Multi-Turn** – Untuk percakapan yang memerlukan konteks. Model mengingat pesan sebelumnya dan membangun darinya. Gunakan ini untuk sesi bantuan interaktif atau tanya jawab kompleks.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/id/context-memory.dff30ad9fa78832a.webp" alt="Memori Konteks" width="800"/>

*Bagaimana konteks percakapan terakumulasi selama banyak putaran sampai mencapai batas token*

**Penalaran Langkah-demi-Langkah** – Untuk masalah yang memerlukan logika terlihat. Model menunjukkan penalaran eksplisit untuk setiap langkah. Gunakan ini untuk soal matematika, teka-teki logika, atau saat Anda ingin memahami proses berpikir.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/id/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Pola Langkah-demi-Langkah" width="800"/>

*Memecah masalah menjadi langkah logis eksplisit*

**Output Terbatas** – Untuk respons dengan persyaratan format tertentu. Model mengikuti aturan format dan panjang secara ketat. Gunakan ini untuk ringkasan atau saat Anda butuh struktur output yang tepat.

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

<img src="../../../translated_images/id/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Pola Output Terbatas" width="800"/>

*Menegakkan aturan format, panjang, dan struktur spesifik*

## Menggunakan Sumber Daya Azure yang Ada

**Verifikasi deployment:**

Pastikan file `.env` ada di direktori root dengan kredensial Azure (dibuat saat Modul 01):
```bash
cat ../.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Mulai aplikasi:**

> **Catatan:** Jika Anda sudah menjalankan semua aplikasi menggunakan `./start-all.sh` dari Modul 01, modul ini sudah berjalan di port 8083. Anda bisa melewati perintah mulai di bawah dan langsung buka http://localhost:8083.

**Opsi 1: Menggunakan Spring Boot Dashboard (Disarankan untuk pengguna VS Code)**

Dev container menyertakan ekstensi Spring Boot Dashboard, yang menyediakan antarmuka visual untuk mengelola semua aplikasi Spring Boot. Anda bisa menemukannya di Activity Bar di sisi kiri VS Code (cari ikon Spring Boot).

Dari Spring Boot Dashboard, Anda bisa:
- Melihat semua aplikasi Spring Boot yang tersedia di workspace
- Memulai/berhenti aplikasi dengan satu klik
- Melihat log aplikasi secara waktu nyata
- Memantau status aplikasi

Cukup klik tombol play di samping "prompt-engineering" untuk memulai modul ini, atau jalankan semua modul sekaligus.

<img src="../../../translated_images/id/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

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
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
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

Buka http://localhost:8083 di browser Anda.

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

## Tangkapan Layar Aplikasi

<img src="../../../translated_images/id/dashboard-home.5444dbda4bc1f79d.webp" alt="Beranda Dashboard" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Dashboard utama yang menampilkan semua 8 pola rekayasa prompt dengan karakteristik dan kasus penggunaannya*

## Menjelajahi Pola

Antarmuka web memungkinkan Anda bereksperimen dengan berbagai strategi prompting. Setiap pola menyelesaikan masalah berbeda—cobalah untuk melihat kapan setiap pendekatan paling efektif.

### Eagerness Rendah vs Tinggi

Ajukan pertanyaan sederhana seperti "Berapa 15% dari 200?" menggunakan Eagerness Rendah. Anda akan mendapatkan jawaban cepat dan langsung. Sekarang tanyakan sesuatu yang kompleks seperti "Rancang strategi caching untuk API dengan lalu lintas tinggi" menggunakan Eagerness Tinggi. Amati bagaimana model melambat dan memberikan penalaran rinci. Model sama, struktur pertanyaan sama — tetapi prompt memberitahu seberapa banyak pemikiran yang harus dilakukan.
<img src="../../../translated_images/id/low-eagerness-demo.898894591fb23aa0.webp" alt="Demo Antusiasme Rendah" width="800"/>

*Perhitungan cepat dengan alasan minimal*

<img src="../../../translated_images/id/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Demo Antusiasme Tinggi" width="800"/>

*Strategi caching komprehensif (2,8MB)*

### Eksekusi Tugas (Preambul Alat)

Alur kerja multi-langkah mendapatkan manfaat dari perencanaan awal dan narasi kemajuan. Model menguraikan apa yang akan dilakukan, menceritakan setiap langkah, kemudian meringkas hasil.

<img src="../../../translated_images/id/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Demo Eksekusi Tugas" width="800"/>

*Membuat endpoint REST dengan narasi langkah demi langkah (3,9MB)*

### Kode yang Reflektif Diri

Coba "Buat layanan validasi email". Alih-alih hanya menghasilkan kode lalu berhenti, model menghasilkan, mengevaluasi berdasarkan kriteria kualitas, mengidentifikasi kelemahan, dan memperbaiki. Anda akan melihat iterasi sampai kode memenuhi standar produksi.

<img src="../../../translated_images/id/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Demo Kode Reflektif Diri" width="800"/>

*Layanan validasi email lengkap (5,2MB)*

### Analisis Terstruktur

Tinjauan kode membutuhkan kerangka evaluasi yang konsisten. Model menganalisis kode menggunakan kategori tetap (kebenaran, praktik, kinerja, keamanan) dengan tingkat keparahan.

<img src="../../../translated_images/id/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Demo Analisis Terstruktur" width="800"/>

*Tinjauan kode berbasis kerangka kerja*

### Chat Multi-Turn

Tanyakan "Apa itu Spring Boot?" lalu langsung lanjut dengan "Tunjukkan contohnya". Model mengingat pertanyaan pertama Anda dan memberikan contoh Spring Boot secara spesifik. Tanpa memori, pertanyaan kedua akan terlalu samar.

<img src="../../../translated_images/id/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Demo Chat Multi-Turn" width="800"/>

*Pelestarian konteks antar pertanyaan*

### Penalaran Langkah demi Langkah

Pilih masalah matematika dan coba dengan Penalaran Langkah demi Langkah dan Antusiasme Rendah. Antusiasme rendah hanya memberi Anda jawaban - cepat tapi tidak transparan. Penalaran langkah demi langkah menunjukkan setiap perhitungan dan keputusan.

<img src="../../../translated_images/id/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Demo Penalaran Langkah demi Langkah" width="800"/>

*Masalah matematika dengan langkah eksplisit*

### Output Terbatas

Saat Anda membutuhkan format atau jumlah kata tertentu, pola ini menegakkan kepatuhan ketat. Coba hasilkan ringkasan dengan tepat 100 kata dalam format poin-poin.

<img src="../../../translated_images/id/constrained-output-demo.567cc45b75da1633.webp" alt="Demo Output Terbatas" width="800"/>

*Ringkasan pembelajaran mesin dengan kontrol format*

## Apa yang Sebenarnya Anda Pelajari

**Upaya Penalaran Mengubah Segalanya**

GPT-5.2 memungkinkan Anda mengendalikan upaya komputasi melalui prompt Anda. Upaya rendah berarti respon cepat dengan eksplorasi minimal. Upaya tinggi berarti model mengambil waktu untuk berpikir mendalam. Anda belajar menyesuaikan upaya dengan kompleksitas tugas - jangan buang waktu untuk pertanyaan sederhana, tapi juga jangan buru-buru pada keputusan kompleks.

**Struktur Membimbing Perilaku**

Perhatikan tag XML dalam prompt? Mereka bukan sekadar hiasan. Model mengikuti instruksi terstruktur lebih andal daripada teks bebas. Saat Anda membutuhkan proses multi-langkah atau logika kompleks, struktur membantu model melacak posisinya dan apa yang harus dilakukan selanjutnya.

<img src="../../../translated_images/id/prompt-structure.a77763d63f4e2f89.webp" alt="Struktur Prompt" width="800"/>

*Anatomi prompt terstruktur dengan bagian jelas dan organisasi gaya XML*

**Kualitas Melalui Evaluasi Diri**

Pola refleksi diri bekerja dengan membuat kriteria kualitas menjadi eksplisit. Alih-alih berharap model "melakukannya dengan benar", Anda memberi tahu dengan tepat apa arti "benar": logika tepat, penanganan kesalahan, kinerja, keamanan. Model kemudian dapat mengevaluasi output-nya sendiri dan memperbaiki. Ini mengubah pembuatan kode dari keberuntungan menjadi proses.

**Konteks Itu Terbatas**

Percakapan multi-turn bekerja dengan menyertakan riwayat pesan pada setiap permintaan. Tapi ada batasnya - setiap model memiliki jumlah token maksimum. Saat percakapan bertambah, Anda butuh strategi untuk menjaga konteks relevan tanpa mencapai batas. Modul ini menunjukkan cara kerja memori; nanti Anda akan belajar kapan harus meringkas, kapan melupakan, dan kapan mengambil kembali.

## Langkah Selanjutnya

**Modul Berikutnya:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 01 - Pengantar](../01-introduction/README.md) | [Kembali ke Utama](../README.md) | [Berikutnya: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan penerjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berusaha untuk mencapai akurasi, harap diingat bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidaktepatan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang paling otoritatif. Untuk informasi penting, disarankan menggunakan terjemahan profesional oleh penerjemah manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau interpretasi yang keliru yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->