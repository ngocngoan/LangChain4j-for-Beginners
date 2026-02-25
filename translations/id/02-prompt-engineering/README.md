# Modul 02: Rekayasa Prompt dengan GPT-5.2

## Daftar Isi

- [Apa yang Akan Anda Pelajari](../../../02-prompt-engineering)
- [Prasyarat](../../../02-prompt-engineering)
- [Memahami Rekayasa Prompt](../../../02-prompt-engineering)
- [Dasar-Dasar Rekayasa Prompt](../../../02-prompt-engineering)
  - [Prompt Zero-Shot](../../../02-prompt-engineering)
  - [Prompt Few-Shot](../../../02-prompt-engineering)
  - [Rantai Pemikiran](../../../02-prompt-engineering)
  - [Prompt Berbasis Peran](../../../02-prompt-engineering)
  - [Template Prompt](../../../02-prompt-engineering)
- [Pola Lanjutan](../../../02-prompt-engineering)
- [Menggunakan Sumber Daya Azure yang Ada](../../../02-prompt-engineering)
- [Tangkapan Layar Aplikasi](../../../02-prompt-engineering)
- [Mengeksplorasi Pola](../../../02-prompt-engineering)
  - [Semangat Rendah vs Tinggi](../../../02-prompt-engineering)
  - [Pelaksanaan Tugas (Preambul Alat)](../../../02-prompt-engineering)
  - [Kode Refleksi Diri](../../../02-prompt-engineering)
  - [Analisis Terstruktur](../../../02-prompt-engineering)
  - [Chat Multi-Turn](../../../02-prompt-engineering)
  - [Penalaran Langkah-demi-Langkah](../../../02-prompt-engineering)
  - [Output Terbatas](../../../02-prompt-engineering)
- [Apa yang Sebenarnya Anda Pelajari](../../../02-prompt-engineering)
- [Langkah Selanjutnya](../../../02-prompt-engineering)

## Apa yang Akan Anda Pelajari

<img src="../../../translated_images/id/what-youll-learn.c68269ac048503b2.webp" alt="Apa yang Akan Anda Pelajari" width="800"/>

Di modul sebelumnya, Anda melihat bagaimana memori memungkinkan AI percakapan dan menggunakan Model GitHub untuk interaksi dasar. Sekarang kita akan fokus pada cara Anda mengajukan pertanyaan — prompt itu sendiri — menggunakan GPT-5.2 dari Azure OpenAI. Cara Anda menyusun prompt secara dramatis mempengaruhi kualitas jawaban yang Anda dapatkan. Kami mulai dengan tinjauan teknik dasar pemberian prompt, kemudian berlanjut ke delapan pola lanjutan yang memanfaatkan kemampuan GPT-5.2 secara penuh.

Kami menggunakan GPT-5.2 karena model ini memperkenalkan kontrol penalaran - Anda bisa memberitahu model seberapa banyak berpikir sebelum menjawab. Ini membuat strategi pemberian prompt yang berbeda menjadi lebih jelas dan membantu Anda memahami kapan harus menggunakan setiap pendekatan. Kami juga mendapat manfaat dari batasan laju yang lebih sedikit di Azure untuk GPT-5.2 dibandingkan Model GitHub.

## Prasyarat

- Modul 01 telah selesai (sumber daya Azure OpenAI sudah dideploy)
- File `.env` di direktori root dengan kredensial Azure (dibuat oleh `azd up` di Modul 01)

> **Catatan:** Jika Anda belum menyelesaikan Modul 01, ikuti instruksi deployment di sana terlebih dahulu.

## Memahami Rekayasa Prompt

<img src="../../../translated_images/id/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Apa itu Rekayasa Prompt?" width="800"/>

Rekayasa prompt adalah tentang merancang teks input yang secara konsisten menghasilkan hasil yang Anda butuhkan. Ini bukan hanya tentang mengajukan pertanyaan - ini tentang menyusun permintaan agar model memahami dengan tepat apa yang Anda inginkan dan bagaimana menyampaikannya.

Bayangkan seperti memberi instruksi kepada rekan kerja. "Perbaiki bug" terlalu umum. "Perbaiki null pointer exception di UserService.java baris 45 dengan menambahkan pemeriksaan null" lebih spesifik. Model bahasa bekerja dengan cara yang sama - spesifikasi dan struktur itu penting.

<img src="../../../translated_images/id/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Bagaimana LangChain4j Cocok" width="800"/>

LangChain4j menyediakan infrastruktur — koneksi model, memori, dan tipe pesan — sementara pola prompt hanyalah teks yang disusun dengan hati-hati yang Anda kirimkan melalui infrastruktur itu. Blok bangunan kuncinya adalah `SystemMessage` (yang mengatur perilaku dan peran AI) dan `UserMessage` (yang membawa permintaan Anda sebenarnya).

## Dasar-Dasar Rekayasa Prompt

<img src="../../../translated_images/id/five-patterns-overview.160f35045ffd2a94.webp" alt="Tinjauan Lima Pola Rekayasa Prompt" width="800"/>

Sebelum masuk ke pola lanjutan di modul ini, mari kita tinjau lima teknik pemberian prompt dasar. Ini adalah blok bangunan yang harus diketahui setiap engineer prompt. Jika Anda sudah menyelesaikan [modul Mulai Cepat](../00-quick-start/README.md#2-prompt-patterns), Anda sudah melihat ini dalam praktik — berikut kerangka konseptual di baliknya.

### Prompt Zero-Shot

Pendekatan paling sederhana: berikan model instruksi langsung tanpa contoh. Model mengandalkan pelatihan sehingga memahami dan menjalankan tugas. Ini bekerja baik untuk permintaan sederhana di mana perilaku yang diharapkan jelas.

<img src="../../../translated_images/id/zero-shot-prompting.7abc24228be84e6c.webp" alt="Prompt Zero-Shot" width="800"/>

*Instruksi langsung tanpa contoh — model menafsirkan tugas hanya dari instruksi*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Respon: "Positif"
```

**Kapan digunakan:** Klasifikasi sederhana, pertanyaan langsung, terjemahan, atau tugas yang dapat ditangani model tanpa panduan tambahan.

### Prompt Few-Shot

Berikan contoh yang menunjukkan pola yang Anda inginkan model ikuti. Model belajar format input-output yang diharapkan dari contoh Anda dan menerapkannya pada input baru. Ini sangat meningkatkan konsistensi untuk tugas di mana format atau perilaku yang diinginkan tidak langsung jelas.

<img src="../../../translated_images/id/few-shot-prompting.9d9eace1da88989a.webp" alt="Prompt Few-Shot" width="800"/>

*Belajar dari contoh — model mengidentifikasi pola dan menerapkannya pada input baru*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**Kapan digunakan:** Klasifikasi kustom, format konsisten, tugas spesifik domain, atau saat hasil zero-shot tidak konsisten.

### Rantai Pemikiran

Minta model menunjukkan proses penalarannya langkah demi langkah. Alih-alih langsung memberi jawaban, model memecah masalah dan mengerjakan setiap bagian secara eksplisit. Ini meningkatkan akurasi pada tugas matematika, logika, dan penalaran multi-langkah.

<img src="../../../translated_images/id/chain-of-thought.5cff6630e2657e2a.webp" alt="Prompt Rantai Pemikiran" width="800"/>

*Penalaran langkah demi langkah — memecah masalah kompleks menjadi langkah logis yang eksplisit*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model menunjukkan: 15 - 8 = 7, kemudian 7 + 12 = 19 apel
```

**Kapan digunakan:** Masalah matematika, teka-teki logika, debugging, atau tugas apa pun di mana menampilkan proses penalaran meningkatkan akurasi dan kepercayaan.

### Prompt Berbasis Peran

Atur persona atau peran AI sebelum mengajukan pertanyaan Anda. Ini memberi konteks yang membentuk nada, kedalaman, dan fokus jawaban. "Arsitek perangkat lunak" memberikan saran berbeda dibandingkan "pengembang junior" atau "auditor keamanan".

<img src="../../../translated_images/id/role-based-prompting.a806e1a73de6e3a4.webp" alt="Prompt Berbasis Peran" width="800"/>

*Menetapkan konteks dan persona — pertanyaan sama mendapat jawaban berbeda tergantung peran yang ditetapkan*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**Kapan digunakan:** Review kode, bimbingan, analisis spesifik domain, atau saat Anda membutuhkan jawaban yang sesuai dengan tingkat keahlian atau perspektif tertentu.

### Template Prompt

Buat prompt yang dapat digunakan ulang dengan placeholder variabel. Alih-alih menulis prompt baru setiap kali, definisikan template sekali dan isi dengan nilai yang berbeda-beda. Kelas `PromptTemplate` dari LangChain4j memudahkan ini dengan sintaks `{{variable}}`.

<img src="../../../translated_images/id/prompt-templates.14bfc37d45f1a933.webp" alt="Template Prompt" width="800"/>

*Prompt yang dapat digunakan ulang dengan placeholder variabel — satu template, banyak penggunaan*

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

**Kapan digunakan:** Query berulang dengan input berbeda, pemrosesan batch, membangun alur kerja AI yang dapat digunakan ulang, atau skenario apa pun di mana struktur prompt tetap sama tapi datanya berubah.

---

Lima dasar ini memberikan Anda toolkit yang solid untuk sebagian besar tugas pemberian prompt. Sisanya modul ini membangun dengan **delapan pola lanjutan** yang memanfaatkan kontrol penalaran, evaluasi diri, dan kemampuan output terstruktur GPT-5.2.

## Pola Lanjutan

Setelah dasar terpenuhi, mari beranjak ke delapan pola lanjutan yang membuat modul ini unik. Tidak semua masalah memerlukan pendekatan yang sama. Beberapa pertanyaan butuh jawaban cepat, sebagian lagi butuh pemikiran mendalam. Ada yang butuh penalaran terlihat, ada yang cuma perlu hasil. Setiap pola di bawah ini dioptimalkan untuk skenario berbeda — dan kontrol penalaran GPT-5.2 membuat perbedaan ini semakin jelas.

<img src="../../../translated_images/id/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Delapan Pola Prompting" width="800"/>

*Tinjauan delapan pola rekayasa prompt dan kasus penggunaannya*

<img src="../../../translated_images/id/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontrol Penalaran dengan GPT-5.2" width="800"/>

*Kontrol penalaran GPT-5.2 memungkinkan Anda menentukan seberapa banyak model harus berpikir — dari jawaban cepat dan langsung sampai eksplorasi mendalam*

**Semangat Rendah (Cepat & Fokus)** - Untuk pertanyaan sederhana yang menginginkan jawaban cepat dan langsung. Model melakukan penalaran minimal - maksimal 2 langkah. Gunakan ini untuk perhitungan, pencarian, atau pertanyaan langsung.

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Jelajahi dengan GitHub Copilot:** Buka [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) dan tanyakan:
> - "Apa perbedaan pola prompting semangat rendah dan tinggi?"
> - "Bagaimana tag XML di prompt membantu menyusun respons AI?"
> - "Kapan saya harus menggunakan pola refleksi diri vs instruksi langsung?"

**Semangat Tinggi (Mendalam & Teliti)** - Untuk masalah kompleks yang membutuhkan analisis komprehensif. Model mengeksplorasi secara menyeluruh dan menampilkan penalaran rinci. Gunakan untuk desain sistem, keputusan arsitektur, atau riset kompleks.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Pelaksanaan Tugas (Progres Langkah-demi-Langkah)** - Untuk alur kerja multi-langkah. Model memberikan rencana di awal, menjelaskan setiap langkah saat dikerjakan, lalu memberikan ringkasan. Gunakan untuk migrasi, implementasi, atau proses multi-langkah lainnya.

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

Prompt chain-of-thought secara eksplisit meminta model menunjukkan proses penalarannya, meningkatkan akurasi untuk tugas kompleks. Rincian langkah demi langkah membantu manusia dan AI memahami logika.

> **🤖 Coba dengan Chat [GitHub Copilot](https://github.com/features/copilot):** Tanyakan tentang pola ini:
> - "Bagaimana saya menyesuaikan pola pelaksanaan tugas untuk operasi yang berjalan lama?"
> - "Apa praktik terbaik untuk menyusun preambul alat di aplikasi produksi?"
> - "Bagaimana saya menangkap dan menampilkan pembaruan progres antara di UI?"

<img src="../../../translated_images/id/task-execution-pattern.9da3967750ab5c1e.webp" alt="Pola Pelaksanaan Tugas" width="800"/>

*Rencana → Eksekusi → Ringkas alur kerja untuk tugas multi-langkah*

**Kode Refleksi Diri** - Untuk menghasilkan kode kualitas produksi. Model membuat kode sesuai standar produksi dengan penanganan error yang tepat. Gunakan saat membangun fitur atau layanan baru.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/id/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Siklus Refleksi Diri" width="800"/>

*Loop perbaikan iteratif - buat, evaluasi, identifikasi masalah, perbaiki, ulangi*

**Analisis Terstruktur** - Untuk evaluasi konsisten. Model mereview kode menggunakan kerangka kerja tetap (kebenaran, praktik, performa, keamanan, maintainabilitas). Gunakan untuk review kode atau penilaian kualitas.

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Coba dengan Chat [GitHub Copilot](https://github.com/features/copilot):** Tanyakan tentang analisis terstruktur:
> - "Bagaimana cara menyesuaikan kerangka analisis untuk berbagai jenis review kode?"
> - "Apa cara terbaik untuk mengurai dan memproses output terstruktur secara programatik?"
> - "Bagaimana memastikan tingkat keparahan konsisten di berbagai sesi review?"

<img src="../../../translated_images/id/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Pola Analisis Terstruktur" width="800"/>

*Kerangka kerja untuk review kode yang konsisten dengan tingkat keparahan*

**Chat Multi-Turn** - Untuk percakapan yang butuh konteks. Model mengingat pesan sebelumnya dan membangun dari sana. Gunakan untuk sesi bantuan interaktif atau tanya jawab kompleks.

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

*Bagaimana konteks percakapan terkumpul selama banyak putaran sampai mencapai batas token*

**Penalaran Langkah-demi-Langkah** - Untuk masalah yang membutuhkan logika terlihat. Model menampilkan penalaran eksplisit untuk tiap langkah. Gunakan untuk masalah matematika, teka-teki logika, atau saat Anda ingin memahami proses berpikir.

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

*Memecah masalah menjadi langkah logis yang eksplisit*

**Output Terbatas** - Untuk jawaban dengan aturan format spesifik. Model mengikuti aturan format dan panjang secara ketat. Gunakan untuk ringkasan atau saat Anda butuh struktur output yang presisi.

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

*Menegakkan aturan format, panjang, dan struktur tertentu*

## Menggunakan Sumber Daya Azure yang Ada

**Verifikasi deployment:**

Pastikan file `.env` ada di direktori root dengan kredensial Azure (dibuat selama Modul 01):
```bash
cat ../.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Mulai aplikasi:**

> **Catatan:** Jika Anda sudah menjalankan semua aplikasi menggunakan `./start-all.sh` dari Modul 01, modul ini sudah berjalan di port 8083. Anda dapat melewati perintah memulai di bawah dan langsung membuka http://localhost:8083.

**Opsi 1: Menggunakan Spring Boot Dashboard (Direkomendasikan untuk pengguna VS Code)**

Dev container sudah menyertakan ekstensi Spring Boot Dashboard, yang menyediakan antarmuka visual untuk mengelola semua aplikasi Spring Boot. Anda dapat menemukannya di Activity Bar di sisi kiri VS Code (cari ikon Spring Boot).

Dari Spring Boot Dashboard, Anda dapat:
- Melihat semua aplikasi Spring Boot yang tersedia di workspace
- Memulai/menghentikan aplikasi dengan satu klik
- Melihat log aplikasi secara real-time
- Memantau status aplikasi
Cukup klik tombol putar di sebelah "prompt-engineering" untuk memulai modul ini, atau mulai semua modul sekaligus.

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

Kedua skrip secara otomatis memuat variabel lingkungan dari file `.env` di root dan akan membangun JAR jika belum ada.

> **Catatan:** Jika Anda ingin membangun semua modul secara manual sebelum memulai:
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

Buka http://localhost:8083 di peramban Anda.

**Untuk menghentikan:**

**Bash:**
```bash
./stop.sh  # Modul ini saja
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

<img src="../../../translated_images/id/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Dasbor utama yang menampilkan semua 8 pola rekayasa prompt beserta karakteristik dan kasus penggunaannya*

## Menjelajahi Pola

Antarmuka web memungkinkan Anda bereksperimen dengan berbagai strategi prompt. Setiap pola memecahkan masalah berbeda - coba dan lihat kapan pendekatan tersebut unggul.

> **Catatan: Streaming vs Non-Streaming** — Setiap halaman pola menyediakan dua tombol: **🔴 Stream Response (Live)** dan opsi **Non-streaming**. Streaming menggunakan Server-Sent Events (SSE) untuk menampilkan token secara real-time saat model menghasilkan, sehingga Anda bisa melihat kemajuan secara langsung. Opsi non-streaming menunggu seluruh respons sebelum menampilkannya. Untuk prompt yang memicu penalaran mendalam (misalnya, High Eagerness, Self-Reflecting Code), panggilan non-streaming bisa memakan waktu sangat lama — kadang menit — tanpa umpan balik yang terlihat. **Gunakan streaming saat bereksperimen dengan prompt yang kompleks** agar Anda dapat melihat model bekerja dan menghindari kesan bahwa permintaan telah habis waktu.
>
> **Catatan: Persyaratan Peramban** — Fitur streaming menggunakan Fetch Streams API (`response.body.getReader()`) yang membutuhkan peramban penuh (Chrome, Edge, Firefox, Safari). Fitur ini **tidak** berfungsi di Simple Browser bawaan VS Code, karena webview-nya tidak mendukung ReadableStream API. Jika Anda menggunakan Simple Browser, tombol non-streaming tetap berfungsi normal — hanya tombol streaming yang terpengaruh. Buka `http://localhost:8083` di peramban eksternal untuk pengalaman penuh.

### Low vs High Eagerness

Ajukan pertanyaan sederhana seperti "Berapa 15% dari 200?" menggunakan Low Eagerness. Anda akan mendapat jawaban langsung dan instan. Sekarang, tanyakan sesuatu yang kompleks seperti "Rancang strategi caching untuk API dengan trafik tinggi" menggunakan High Eagerness. Klik **🔴 Stream Response (Live)** dan saksikan alasan rinci model muncul token demi token. Model sama, struktur pertanyaan sama - tapi prompt memberitahu seberapa banyak pemikiran yang harus dilakukan.

### Pelaksanaan Tugas (Tool Preambles)

Alur kerja multi-langkah mendapat keuntungan dari perencanaan awal dan narasi progres. Model menguraikan apa yang akan dilakukan, menceritakan setiap langkah, lalu merangkum hasilnya.

### Self-Reflecting Code

Coba "Buat layanan validasi email". Alih-alih hanya menghasilkan kode dan berhenti, model menghasilkan, mengevaluasi berdasarkan kriteria kualitas, mengidentifikasi kelemahan, dan memperbaiki. Anda akan melihatnya mengiterasi hingga kode memenuhi standar produksi.

### Analisis Terstruktur

Ulasan kode membutuhkan kerangka evaluasi yang konsisten. Model menganalisis kode menggunakan kategori tetap (kebenaran, praktik, kinerja, keamanan) dengan tingkat keparahan.

### Multi-Turn Chat

Tanyakan "Apa itu Spring Boot?" lalu langsung lanjut dengan "Tunjukkan contoh." Model mengingat pertanyaan pertama dan memberikan contoh Spring Boot secara spesifik. Tanpa memori, pertanyaan kedua akan terlalu samar.

### Penalaran Langkah demi Langkah

Pilih masalah matematika dan coba dengan Penalaran Langkah demi Langkah dan Low Eagerness. Low eagerness hanya memberi jawaban - cepat tapi buram. Langkah demi langkah menunjukkan setiap perhitungan dan keputusan.

### Output Terbatas

Ketika Anda membutuhkan format atau jumlah kata tertentu, pola ini menerapkan kepatuhan ketat. Coba buat ringkasan dengan tepat 100 kata dalam format poin-poin.

## Apa yang Sebenarnya Anda Pelajari

**Upaya Penalaran Mengubah Segalanya**

GPT-5.2 memungkinkan Anda mengontrol upaya komputasi melalui prompt. Upaya rendah berarti respons cepat dengan eksplorasi minimal. Upaya tinggi berarti model meluangkan waktu untuk berpikir mendalam. Anda belajar mencocokkan upaya dengan kompleksitas tugas - jangan buang waktu untuk pertanyaan sederhana, tapi juga jangan terburu-buru dalam keputusan kompleks.

**Struktur Membimbing Perilaku**

Perhatikan tag XML dalam prompt? Itu bukan hiasan. Model mengikuti instruksi terstruktur lebih andal daripada teks bebas. Saat Anda membutuhkan proses multi-langkah atau logika kompleks, struktur membantu model melacak posisinya dan langkah selanjutnya.

<img src="../../../translated_images/id/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi prompt yang terstruktur baik dengan bagian jelas dan organisasi gaya XML*

**Kualitas Melalui Evaluasi Diri**

Pola self-reflecting bekerja dengan membuat kriteria kualitas menjadi eksplisit. Alih-alih berharap model "melakukannya dengan benar", Anda memberi tahu secara tepat apa arti "benar": logika benar, penanganan kesalahan, kinerja, keamanan. Model lalu dapat mengevaluasi output sendiri dan memperbaiki. Ini mengubah pembuatan kode dari lotere menjadi proses.

**Konteks Itu Terbatas**

Percakapan multi-turn bekerja dengan menyertakan riwayat pesan dalam setiap permintaan. Tapi ada batas - setiap model memiliki jumlah token maksimum. Saat percakapan bertambah, Anda perlu strategi untuk mempertahankan konteks relevan tanpa mencapai batas itu. Modul ini menunjukkan cara kerja memori; nanti Anda akan belajar kapan merangkum, kapan melupakan, dan kapan mengambil kembali.

## Langkah Berikutnya

**Modul Berikutnya:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 01 - Pengantar](../01-introduction/README.md) | [Kembali ke Utama](../README.md) | [Berikutnya: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berupaya untuk mencapai akurasi, harap diketahui bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang otoritatif. Untuk informasi penting, disarankan menggunakan jasa terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau penafsiran yang salah yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->