# Modul 02: Rekayasa Prompt dengan GPT-5.2

## Daftar Isi

- [Apa yang Akan Anda Pelajari](../../../02-prompt-engineering)
- [Prasyarat](../../../02-prompt-engineering)
- [Memahami Rekayasa Prompt](../../../02-prompt-engineering)
- [Dasar-Dasar Rekayasa Prompt](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Pola Lanjutan](../../../02-prompt-engineering)
- [Menggunakan Sumber Daya Azure yang Ada](../../../02-prompt-engineering)
- [Screenshot Aplikasi](../../../02-prompt-engineering)
- [Menjelajahi Pola-Pola](../../../02-prompt-engineering)
  - [Semangat Rendah vs Tinggi](../../../02-prompt-engineering)
  - [Pelaksanaan Tugas (Preambul Alat)](../../../02-prompt-engineering)
  - [Kode Reflektif Diri](../../../02-prompt-engineering)
  - [Analisis Terstruktur](../../../02-prompt-engineering)
  - [Obrolan Multi-Turn](../../../02-prompt-engineering)
  - [Penalaran Langkah-demi-Langkah](../../../02-prompt-engineering)
  - [Output Terbatas](../../../02-prompt-engineering)
- [Apa yang Sebenarnya Anda Pelajari](../../../02-prompt-engineering)
- [Langkah Selanjutnya](../../../02-prompt-engineering)

## Apa yang Akan Anda Pelajari

<img src="../../../translated_images/id/what-youll-learn.c68269ac048503b2.webp" alt="Apa yang Akan Anda Pelajari" width="800"/>

Dalam modul sebelumnya, Anda melihat bagaimana memori memungkinkan AI percakapan dan menggunakan Model GitHub untuk interaksi dasar. Sekarang kita akan fokus pada cara Anda mengajukan pertanyaan — prompt itu sendiri — menggunakan GPT-5.2 dari Azure OpenAI. Cara Anda menyusun prompt sangat memengaruhi kualitas respons yang Anda dapatkan. Kita mulai dengan tinjauan teknik-teknik dasar prompting, lalu melanjutkan ke delapan pola lanjutan yang memanfaatkan sepenuhnya kemampuan GPT-5.2.

Kita menggunakan GPT-5.2 karena memperkenalkan kontrol penalaran — Anda dapat memberitahu model seberapa banyak pemikiran yang harus dilakukan sebelum menjawab. Ini membuat berbagai strategi prompting menjadi lebih jelas dan membantu Anda memahami kapan menggunakan setiap pendekatan. Kita juga mendapat keuntungan dari batasan laju yang lebih sedikit pada GPT-5.2 dibandingkan Model GitHub di Azure.

## Prasyarat

- Modul 01 telah selesai (sumber daya Azure OpenAI sudah terdeploy)
- File `.env` di direktori root berisi kredensial Azure (dibuat oleh `azd up` di Modul 01)

> **Catatan:** Jika Anda belum menyelesaikan Modul 01, ikuti instruksi deployment di sana terlebih dahulu.

## Memahami Rekayasa Prompt

<img src="../../../translated_images/id/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Apa Itu Rekayasa Prompt?" width="800"/>

Rekayasa prompt adalah tentang merancang teks input yang secara konsisten memberi Anda hasil yang Anda butuhkan. Ini bukan hanya soal mengajukan pertanyaan — ini tentang menyusun permintaan agar model memahami dengan tepat apa yang Anda inginkan dan bagaimana menyampaikannya.

Bayangkan seperti memberikan instruksi kepada kolega. "Perbaiki bug" itu samar. "Perbaiki pengecualian null pointer di UserService.java baris 45 dengan menambahkan pengecekan null" itu spesifik. Model bahasa bekerja seperti itu — kejelasan dan struktur itu penting.

<img src="../../../translated_images/id/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Bagaimana LangChain4j Sesuai" width="800"/>

LangChain4j menyediakan infrastruktur — koneksi model, memori, dan tipe pesan — sementara pola prompt hanyalah teks yang disusun dengan hati-hati yang Anda kirim melalui infrastruktur tersebut. Komponen kuncinya adalah `SystemMessage` (yang mengatur perilaku dan peran AI) dan `UserMessage` (yang membawa permintaan Anda sebenarnya).

## Dasar-Dasar Rekayasa Prompt

<img src="../../../translated_images/id/five-patterns-overview.160f35045ffd2a94.webp" alt="Gambaran Lima Pola Rekayasa Prompt" width="800"/>

Sebelum menyelami pola lanjutan dalam modul ini, mari kita tinjau lima teknik prompting dasar. Ini adalah blok bangunan yang harus diketahui setiap engineer prompt. Jika Anda sudah mengerjakan [modul Quick Start](../00-quick-start/README.md#2-prompt-patterns), Anda sudah melihat ini beraksi — berikut kerangka konseptual di baliknya.

### Zero-Shot Prompting

Pendekatan paling sederhana: berikan model instruksi langsung tanpa contoh. Model sepenuhnya mengandalkan pelatihannya untuk memahami dan menjalankan tugas. Ini cocok untuk permintaan yang jelas di mana perilaku yang diharapkan sudah jelas.

<img src="../../../translated_images/id/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Instruksi langsung tanpa contoh — model menafsirkan tugas hanya dari instruksi*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Respons: "Positif"
```

**Kapan digunakan:** Klasifikasi sederhana, pertanyaan langsung, terjemahan, atau tugas apa pun yang dapat ditangani model tanpa panduan tambahan.

### Few-Shot Prompting

Berikan contoh yang menunjukkan pola yang ingin Anda model ikuti. Model mempelajari format input-output yang diharapkan dari contoh Anda dan menerapkannya ke input baru. Ini sangat meningkatkan konsistensi untuk tugas di mana format atau perilaku yang diinginkan tidak jelas.

<img src="../../../translated_images/id/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Belajar dari contoh — model mengenali pola dan menerapkannya pada input baru*

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

**Kapan digunakan:** Klasifikasi khusus, format konsisten, tugas domain-spesifik, atau saat hasil zero-shot tidak konsisten.

### Chain of Thought

Minta model menunjukkan penalarannya langkah demi langkah. Alih-alih langsung memberikan jawaban, model memecah masalah dan menguraikan setiap bagian secara eksplisit. Ini meningkatkan akurasi pada masalah matematika, logika, dan penalaran multi-langkah.

<img src="../../../translated_images/id/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Penalaran langkah demi langkah — memecah masalah kompleks ke dalam langkah logis eksplisit*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model menunjukkan: 15 - 8 = 7, kemudian 7 + 12 = 19 apel
```

**Kapan digunakan:** Masalah matematika, teka-teki logika, debugging, atau tugas apa pun di mana menunjukkan proses penalaran meningkatkan akurasi dan kepercayaan.

### Role-Based Prompting

Tetapkan persona atau peran untuk AI sebelum mengajukan pertanyaan. Ini memberikan konteks yang membentuk nada, kedalaman, dan fokus respons. "Arsitek perangkat lunak" memberikan saran berbeda dibandingkan "pengembang junior" atau "auditor keamanan".

<img src="../../../translated_images/id/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Mengatur konteks dan persona — pertanyaan yang sama mendapat respons berbeda tergantung peran yang ditugaskan*

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

**Kapan digunakan:** Review kode, pembelajaran, analisis domain-spesifik, atau saat Anda butuh respons yang disesuaikan dengan tingkat keahlian atau perspektif tertentu.

### Prompt Templates

Buat prompt yang dapat digunakan ulang dengan placeholder variabel. Alih-alih menulis prompt baru setiap kali, definisikan template satu kali dan isi dengan nilai berbeda. Kelas `PromptTemplate` LangChain4j memudahkan ini dengan sintaks `{{variable}}`.

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

**Kapan digunakan:** Permintaan berulang dengan input berbeda, pemrosesan batch, membangun alur kerja AI yang dapat digunakan ulang, atau setiap skenario di mana struktur prompt tetap sama tapi data berubah.

---

Lima dasar ini memberi Anda alat yang kuat untuk sebagian besar tugas prompting. Sisanya dari modul ini membangun di atasnya dengan **delapan pola lanjutan** yang memanfaatkan kontrol penalaran, evaluasi diri, dan output terstruktur GPT-5.2.

## Pola Lanjutan

Setelah dasar tercakup, mari kita lanjut ke delapan pola lanjutan yang membuat modul ini unik. Tidak semua masalah membutuhkan pendekatan yang sama. Beberapa pertanyaan butuh jawaban cepat, yang lain butuh pemikiran mendalam. Beberapa butuh penalaran yang terlihat, yang lain hanya butuh hasil. Masing-masing pola di bawah ini dioptimalkan untuk skenario berbeda — dan kontrol penalaran GPT-5.2 membuat perbedaan ini semakin jelas.

<img src="../../../translated_images/id/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Delapan Pola Prompting" width="800"/>

*Gambaran delapan pola rekayasa prompt dan kasus penggunaannya*

<img src="../../../translated_images/id/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontrol Penalaran dengan GPT-5.2" width="800"/>

*Kontrol penalaran GPT-5.2 memungkinkan Anda menentukan seberapa banyak pemikiran yang harus dilakukan model — dari jawaban langsung cepat hingga eksplorasi mendalam*

**Semangat Rendah (Cepat & Fokus)** - Untuk pertanyaan sederhana yang ingin Anda dapatkan jawaban cepat dan langsung. Model melakukan penalaran minimal - maksimal 2 langkah. Gunakan ini untuk perhitungan, pencarian, atau pertanyaan langsung.

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

> 💡 **Eksplorasi dengan GitHub Copilot:** Buka [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) dan tanyakan:
> - "Apa perbedaan antara pola prompting semangat rendah dan tinggi?"
> - "Bagaimana tag XML dalam prompt membantu menyusun respons AI?"
> - "Kapan saya harus menggunakan pola refleksi diri dibanding instruksi langsung?"

**Semangat Tinggi (Mendalam & Teliti)** - Untuk masalah kompleks yang Anda butuhkan analisis komprehensif. Model menjelajah secara menyeluruh dan menunjukkan penalaran detail. Gunakan ini untuk desain sistem, keputusan arsitektur, atau riset kompleks.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Pelaksanaan Tugas (Kemajuan Langkah-demi-Langkah)** - Untuk alur kerja multi-langkah. Model memberikan rencana awal, mengisahkan setiap langkah saat bekerja, lalu memberikan ringkasan. Gunakan ini untuk migrasi, implementasi, atau proses multi-langkah apa pun.

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

Prompt Chain-of-Thought secara eksplisit meminta model menunjukkan proses penalarannya, meningkatkan akurasi untuk tugas kompleks. Pemecahan langkah demi langkah membantu manusia dan AI memahami logika.

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Tanyakan tentang pola ini:
> - "Bagaimana saya menyesuaikan pola pelaksanaan tugas untuk operasi jangka panjang?"
> - "Apa praktik terbaik dalam menyusun preambul alat di aplikasi produksi?"
> - "Bagaimana cara menangkap dan menampilkan pembaruan kemajuan perantara di UI?"

<img src="../../../translated_images/id/task-execution-pattern.9da3967750ab5c1e.webp" alt="Pola Pelaksanaan Tugas" width="800"/>

*Rencana → Eksekusi → Ringkas alur kerja untuk tugas multi-langkah*

**Kode Reflektif Diri** - Untuk menghasilkan kode dengan kualitas produksi. Model menghasilkan kode sesuai standar produksi dengan penanganan kesalahan yang tepat. Gunakan ini saat membangun fitur atau layanan baru.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/id/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Siklus Refleksi Diri" width="800"/>

*Loop perbaikan iteratif - buat, evaluasi, identifikasi masalah, perbaiki, ulangi*

**Analisis Terstruktur** - Untuk evaluasi konsisten. Model meninjau kode menggunakan kerangka tetap (kebenaran, praktik, performa, keamanan, maintainability). Gunakan ini untuk review kode atau penilaian kualitas.

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

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Tanyakan tentang analisis terstruktur:
> - "Bagaimana saya menyesuaikan kerangka analisis untuk berbagai jenis review kode?"
> - "Apa cara terbaik untuk mengurai dan menindaklanjuti output terstruktur secara programatis?"
> - "Bagaimana saya memastikan tingkat keparahan konsisten di berbagai sesi review?"

<img src="../../../translated_images/id/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Pola Analisis Terstruktur" width="800"/>

*Kerangka untuk review kode konsisten dengan tingkat keparahan*

**Obrolan Multi-Turn** - Untuk percakapan yang butuh konteks. Model mengingat pesan sebelumnya dan membangun dari sana. Gunakan ini untuk sesi bantuan interaktif atau Q&A kompleks.

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

*Bagaimana konteks percakapan terakumulasi selama beberapa putaran hingga mencapai batas token*

**Penalaran Langkah-demi-Langkah** - Untuk masalah yang memerlukan logika terlihat. Model menunjukkan penalaran eksplisit untuk setiap langkah. Gunakan ini untuk masalah matematika, teka-teki logika, atau saat Anda perlu memahami proses pemikiran.

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

**Output Terbatas** - Untuk respons dengan persyaratan format spesifik. Model secara ketat mengikuti aturan format dan panjang. Gunakan ini untuk ringkasan atau saat Anda perlu struktur output yang presisi.

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

*Memaksa kepatuhan terhadap format, panjang, dan struktur yang spesifik*

## Menggunakan Sumber Daya Azure yang Ada

**Verifikasi deployment:**

Pastikan file `.env` ada di direktori root dengan kredensial Azure (dibuat selama Modul 01):
```bash
cat ../.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Mulai aplikasi:**

> **Catatan:** Jika Anda sudah menjalankan semua aplikasi menggunakan `./start-all.sh` dari Modul 01, modul ini sudah berjalan di port 8083. Anda dapat melewati perintah start di bawah dan langsung buka http://localhost:8083.

**Opsi 1: Menggunakan Spring Boot Dashboard (Disarankan untuk pengguna VS Code)**

Container pengembangan sudah termasuk ekstensi Spring Boot Dashboard, yang menyediakan antarmuka visual untuk mengelola semua aplikasi Spring Boot. Anda dapat menemukannya di Bilah Aktivitas di sisi kiri VS Code (cari ikon Spring Boot).

Dari Spring Boot Dashboard, Anda dapat:
- Melihat semua aplikasi Spring Boot yang tersedia di workspace
- Memulai/berhenti aplikasi dengan satu klik
- Melihat log aplikasi secara real-time
- Memantau status aplikasi
Cukup klik tombol putar di sebelah "prompt-engineering" untuk memulai modul ini, atau mulai semua modul sekaligus.

<img src="../../../translated_images/id/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opsi 2: Menggunakan script shell**

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

> **Catatan:** Jika Anda lebih suka membangun semua modul secara manual sebelum memulai:
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
./stop.sh  # Hanya modul ini
# Atau
cd .. && ./stop-all.sh  # Semua modul
```

**PowerShell:**
```powershell
.\stop.ps1  # Modul ini saja
# Atau
cd ..; .\stop-all.ps1  # Semua modul
```

## Screenshot Aplikasi

<img src="../../../translated_images/id/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Dasbor utama yang menampilkan semua 8 pola rekayasa prompt dengan karakteristik dan kasus penggunaannya*

## Menjelajahi Pola

Antarmuka web memungkinkan Anda bereksperimen dengan berbagai strategi prompting. Setiap pola menyelesaikan masalah yang berbeda - coba untuk melihat kapan setiap pendekatan bersinar.

### Antusiasme Rendah vs Tinggi

Ajukan pertanyaan sederhana seperti "Berapa 15% dari 200?" menggunakan Antusiasme Rendah. Anda akan mendapatkan jawaban langsung dan instan. Sekarang ajukan sesuatu yang kompleks seperti "Rancang strategi caching untuk API dengan lalu lintas tinggi" menggunakan Antusiasme Tinggi. Perhatikan bagaimana model melambat dan memberikan alasan yang rinci. Model yang sama, struktur pertanyaan yang sama - tapi prompt memberi tahu seberapa banyak berpikir yang harus dilakukan.

<img src="../../../translated_images/id/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Perhitungan cepat dengan penalaran minimal*

<img src="../../../translated_images/id/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Strategi caching komprehensif (2.8MB)*

### Eksekusi Tugas (Preambel Alat)

Alur kerja multi-langkah mendapat manfaat dari perencanaan awal dan narasi kemajuan. Model menguraikan apa yang akan dilakukan, menceritakan setiap langkah, lalu merangkum hasil.

<img src="../../../translated_images/id/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Membuat endpoint REST dengan narasi langkah demi langkah (3.9MB)*

### Kode yang Mempertimbangkan Diri Sendiri

Coba "Buat layanan validasi email". Alih-alih hanya menghasilkan kode dan berhenti, model membuat, mengevaluasi berdasarkan kriteria kualitas, mengidentifikasi kelemahan, dan memperbaiki. Anda akan melihatnya mengulangi sampai kode memenuhi standar produksi.

<img src="../../../translated_images/id/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Layanan validasi email lengkap (5.2MB)*

### Analisis Terstruktur

Review kode membutuhkan kerangka evaluasi yang konsisten. Model menganalisis kode menggunakan kategori tetap (kebenaran, praktik, performa, keamanan) dengan tingkat keparahan.

<img src="../../../translated_images/id/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Review kode berbasis kerangka kerja*

### Chat Multi-Turn

Tanyakan "Apa itu Spring Boot?" lalu langsung lanjutkan dengan "Tunjukkan saya contohnya". Model mengingat pertanyaan pertama Anda dan memberikan contoh Spring Boot secara spesifik. Tanpa memori, pertanyaan kedua akan terlalu samar.

<img src="../../../translated_images/id/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Preservasi konteks antar pertanyaan*

### Penalaran Langkah-demi-Langkah

Pilih masalah matematika dan coba dengan Penalaran Langkah-demi-Langkah serta Antusiasme Rendah. Antusiasme rendah hanya memberikan jawaban - cepat tapi tidak jelas. Langkah demi langkah menunjukkan setiap perhitungan dan keputusan.

<img src="../../../translated_images/id/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Masalah matematika dengan langkah eksplisit*

### Output Terbatas

Saat Anda membutuhkan format atau jumlah kata tertentu, pola ini menegakkan kepatuhan ketat. Coba buat ringkasan dengan tepat 100 kata dalam format poin.

<img src="../../../translated_images/id/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Ringkasan pembelajaran mesin dengan kontrol format*

## Apa yang Sebenarnya Anda Pelajari

**Usaha Penalaran Mengubah Segalanya**

GPT-5.2 memungkinkan Anda mengontrol usaha komputasi melalui prompt Anda. Usaha rendah berarti respons cepat dengan eksplorasi minimal. Usaha tinggi berarti model meluangkan waktu untuk berpikir mendalam. Anda belajar menyesuaikan usaha dengan kompleksitas tugas - jangan buang waktu untuk pertanyaan sederhana, tapi jangan terburu-buru dalam keputusan kompleks.

**Struktur Membimbing Perilaku**

Perhatikan tag XML dalam prompt? Itu bukan hiasan. Model mengikuti instruksi terstruktur lebih andal daripada teks bebas. Saat Anda butuh proses multi-langkah atau logika kompleks, struktur membantu model melacak posisi dan langkah selanjutnya.

<img src="../../../translated_images/id/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi prompt yang terstruktur dengan jelas dan pengorganisasian gaya XML*

**Kualitas Melalui Evaluasi Diri**

Pola refleksi diri bekerja dengan membuat kriteria kualitas menjadi eksplisit. Alih-alih berharap model "melakukannya dengan benar", Anda memberi tahu arti "benar" secara tepat: logika benar, penanganan error, performa, keamanan. Model kemudian dapat mengevaluasi output sendiri dan memperbaiki. Ini mengubah pembuatan kode dari untung-untungan menjadi proses.

**Konteks Itu Terbatas**

Percakapan multi-turn bekerja dengan menyertakan riwayat pesan setiap permintaan. Tapi ada batas - setiap model memiliki jumlah token maksimum. Seiring pertumbuhan percakapan, Anda perlu strategi untuk menjaga konteks relevan tanpa melewati batas itu. Modul ini menunjukkan cara kerja memori; nanti Anda akan belajar kapan merangkum, kapan melupakan, dan kapan mengambil kembali.

## Langkah Berikutnya

**Modul Selanjutnya:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 01 - Pengantar](../01-introduction/README.md) | [Kembali ke Utama](../README.md) | [Berikutnya: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berusaha untuk menjaga akurasi, harap diingat bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang sahih. Untuk informasi penting, disarankan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau salah tafsir yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->