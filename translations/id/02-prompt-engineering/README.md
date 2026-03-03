# Modul 02: Rekayasa Prompt dengan GPT-5.2

## Daftar Isi

- [Video Walkthrough](../../../02-prompt-engineering)
- [Apa yang Akan Anda Pelajari](../../../02-prompt-engineering)
- [Persyaratan Awal](../../../02-prompt-engineering)
- [Memahami Rekayasa Prompt](../../../02-prompt-engineering)
- [Dasar-Dasar Rekayasa Prompt](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Template Prompt](../../../02-prompt-engineering)
- [Pola Lanjutan](../../../02-prompt-engineering)
- [Jalankan Aplikasi](../../../02-prompt-engineering)
- [Cuplikan Aplikasi](../../../02-prompt-engineering)
- [Menjelajah Pola](../../../02-prompt-engineering)
  - [Antusiasme Rendah vs Tinggi](../../../02-prompt-engineering)
  - [Eksekusi Tugas (Preambul Alat)](../../../02-prompt-engineering)
  - [Kode yang Merefleksikan Diri](../../../02-prompt-engineering)
  - [Analisis Terstruktur](../../../02-prompt-engineering)
  - [Chat Multi-Turn](../../../02-prompt-engineering)
  - [Penalaran Langkah-demi-Langkah](../../../02-prompt-engineering)
  - [Output Terbatas](../../../02-prompt-engineering)
- [Apa yang Sebenarnya Anda Pelajari](../../../02-prompt-engineering)
- [Langkah Selanjutnya](../../../02-prompt-engineering)

## Video Walkthrough

Tonton sesi langsung ini yang menjelaskan cara memulai dengan modul ini:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Apa yang Akan Anda Pelajari

Diagram berikut memberikan gambaran tentang topik utama dan keterampilan yang akan Anda kembangkan dalam modul ini — dari teknik penyempurnaan prompt hingga alur kerja langkah demi langkah yang akan Anda ikuti.

<img src="../../../translated_images/id/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

Dalam modul sebelumnya, Anda menjelajahi interaksi dasar LangChain4j dengan Model GitHub dan melihat bagaimana memori memungkinkan AI percakapan dengan Azure OpenAI. Sekarang kita akan fokus pada bagaimana Anda mengajukan pertanyaan — prompt itu sendiri — menggunakan GPT-5.2 dari Azure OpenAI. Cara Anda menyusun prompt secara dramatis memengaruhi kualitas jawaban yang Anda dapatkan. Kami mulai dengan tinjauan teknik prompting dasar, lalu berlanjut ke delapan pola lanjutan yang memanfaatkan sepenuhnya kemampuan GPT-5.2.

Kami menggunakan GPT-5.2 karena memperkenalkan kontrol penalaran - Anda dapat memberi tahu model seberapa banyak berpikir sebelum menjawab. Ini membuat berbagai strategi prompting menjadi lebih jelas dan membantu Anda memahami kapan menggunakan setiap pendekatan. Kami juga akan mendapat manfaat dari batasan rate lebih sedikit di Azure untuk GPT-5.2 dibandingkan Model GitHub.

## Persyaratan Awal

- Menyelesaikan Modul 01 (sumber daya Azure OpenAI telah dideploy)
- File `.env` di direktori root dengan kredensial Azure (dibuat oleh `azd up` di Modul 01)

> **Catatan:** Jika Anda belum menyelesaikan Modul 01, ikuti instruksi deployment di sana terlebih dahulu.

## Memahami Rekayasa Prompt

Pada intinya, rekayasa prompt adalah perbedaan antara instruksi yang samar dan yang tepat, seperti yang ditunjukkan perbandingan di bawah ini.

<img src="../../../translated_images/id/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Rekayasa prompt adalah tentang merancang teks input yang secara konsisten memberikan hasil yang Anda butuhkan. Ini bukan hanya tentang mengajukan pertanyaan - ini tentang menyusun permintaan sehingga model memahami dengan tepat apa yang Anda inginkan dan bagaimana menyajikannya.

Pikirkan seperti memberi instruksi kepada rekan kerja. "Perbaiki bug" itu samar. "Perbaiki null pointer exception di UserService.java baris 45 dengan menambahkan pengecekan null" itu spesifik. Model bahasa bekerja dengan cara yang sama — spesifikasi dan struktur sangat penting.

Diagram di bawah menunjukkan bagaimana LangChain4j masuk ke dalam gambar ini — menghubungkan pola prompt Anda ke model melalui blok bangunan SystemMessage dan UserMessage.

<img src="../../../translated_images/id/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j menyediakan infrastruktur — koneksi model, memori, dan tipe pesan — sementara pola prompt hanyalah teks yang disusun dengan hati-hati yang Anda kirim melalui infrastruktur itu. Blok bangunan kunci adalah `SystemMessage` (yang mengatur perilaku dan peran AI) dan `UserMessage` (yang membawa permintaan Anda yang sebenarnya).

## Dasar-Dasar Rekayasa Prompt

Lima teknik inti yang ditunjukkan di bawah ini membentuk fondasi rekayasa prompt yang efektif. Masing-masing menangani aspek berbeda dari bagaimana Anda berkomunikasi dengan model bahasa.

<img src="../../../translated_images/id/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Sebelum menyelami pola-pola lanjutan dalam modul ini, mari tinjau kembali lima teknik prompting dasar. Ini adalah blok bangunan yang harus diketahui setiap insinyur prompt. Jika Anda sudah mengerjakan [modul Quick Start](../00-quick-start/README.md#2-prompt-patterns), Anda sudah melihat ini dalam praktik — di sini kerangka konseptual di baliknya.

### Zero-Shot Prompting

Pendekatan paling sederhana: beri instruksi langsung ke model tanpa contoh. Model sepenuhnya mengandalkan pelatihannya untuk memahami dan menjalankan tugas. Ini bekerja baik untuk permintaan yang langsung di mana perilaku yang diharapkan jelas.

<img src="../../../translated_images/id/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Instruksi langsung tanpa contoh — model menyimpulkan tugas hanya dari instruksi*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Respon: "Positif"
```

**Kapan digunakan:** Klasifikasi sederhana, pertanyaan langsung, terjemahan, atau tugas apa pun yang dapat ditangani model tanpa panduan tambahan.

### Few-Shot Prompting

Berikan contoh yang menunjukkan pola yang ingin Anda model ikuti. Model belajar format input-output yang diharapkan dari contoh Anda dan menerapkannya ke input baru. Ini secara dramatis meningkatkan konsistensi untuk tugas di mana format atau perilaku yang diinginkan tidak jelas.

<img src="../../../translated_images/id/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

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

**Kapan digunakan:** Klasifikasi khusus, format yang konsisten, tugas domain-spesifik, atau ketika hasil zero-shot tidak konsisten.

### Chain of Thought

Minta model menunjukkan penalarannya langkah demi langkah. Alih-alih langsung menuju jawaban, model memecah masalah dan mengerjakan setiap bagian secara eksplisit. Ini meningkatkan akurasi pada tugas matematika, logika, dan penalaran bertahap.

<img src="../../../translated_images/id/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Penalaran langkah demi langkah — memecah masalah kompleks menjadi langkah logis eksplisit*

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

Tetapkan persona atau peran untuk AI sebelum Anda mengajukan pertanyaan. Ini memberikan konteks yang membentuk nada, kedalaman, dan fokus respons. "Arsitek perangkat lunak" memberikan saran berbeda dari "pengembang junior" atau "auditor keamanan".

<img src="../../../translated_images/id/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Menetapkan konteks dan persona — pertanyaan yang sama mendapat respons berbeda tergantung peran yang ditugaskan*

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

**Kapan digunakan:** Review kode, pengajaran, analisis domain-spesifik, atau saat Anda membutuhkan respons yang disesuaikan dengan tingkat keahlian atau perspektif tertentu.

### Prompt Templates

Membuat prompt yang dapat digunakan ulang dengan placeholder variabel. Alih-alih menulis prompt baru setiap kali, definisikan template sekali dan isi dengan nilai berbeda. Kelas `PromptTemplate` di LangChain4j memudahkan ini dengan sintaks `{{variable}}`.

<img src="../../../translated_images/id/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

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

**Kapan digunakan:** Query berulang dengan input yang berbeda, pemrosesan batch, membangun alur kerja AI yang dapat digunakan ulang, atau skenario apa pun di mana struktur prompt tetap sama tapi data berubah.

---

Lima dasar ini memberi Anda toolkit solid untuk sebagian besar tugas prompting. Sisanya dari modul ini membangun di atasnya dengan **delapan pola lanjutan** yang memanfaatkan kontrol penalaran, evaluasi mandiri, dan kemampuan output terstruktur GPT-5.2.

## Pola Lanjutan

Dengan dasar-dasar telah dibahas, mari kita beralih ke delapan pola lanjutan yang menjadikan modul ini unik. Tidak semua masalah membutuhkan pendekatan yang sama. Beberapa pertanyaan membutuhkan jawaban cepat, lainnya butuh pemikiran mendalam. Ada yang butuh penalaran yang terlihat, ada juga yang hanya butuh hasil. Setiap pola di bawah dioptimalkan untuk skenario berbeda — dan kontrol penalaran GPT-5.2 membuat perbedaan ini semakin jelas.

<img src="../../../translated_images/id/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Ikhtisar delapan pola rekayasa prompt dan kasus penggunaannya*

GPT-5.2 menambahkan dimensi lain ke pola-pola ini: *kontrol penalaran*. Penggeser di bawah ini menunjukkan bagaimana Anda bisa mengatur tingkat berpikir model — dari jawaban cepat dan langsung sampai analisis yang mendalam dan menyeluruh.

<img src="../../../translated_images/id/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Kontrol penalaran GPT-5.2 memungkinkan Anda menentukan seberapa banyak model harus berpikir — dari jawaban cepat hingga eksplorasi mendalam*

**Antusiasme Rendah (Cepat & Fokus)** - Untuk pertanyaan sederhana di mana Anda ingin jawaban yang cepat dan langsung. Model melakukan penalaran minimal - maksimum 2 langkah. Gunakan ini untuk perhitungan, pencarian, atau pertanyaan langsung.

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
> - "Apa perbedaan antara pola prompting antusiasme rendah dan tinggi?"
> - "Bagaimana tag XML dalam prompt membantu menyusun respons AI?"
> - "Kapan saya harus menggunakan pola refleksi diri vs instruksi langsung?"

**Antusiasme Tinggi (Mendalam & Menyeluruh)** - Untuk masalah kompleks di mana Anda ingin analisis yang komprehensif. Model menjelajah dengan menyeluruh dan menunjukkan penalaran rinci. Gunakan ini untuk desain sistem, keputusan arsitektur, atau riset kompleks.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Eksekusi Tugas (Kemajuan Langkah-demi-Langkah)** - Untuk alur kerja multi-langkah. Model menyediakan rencana awal, menceritakan setiap langkah saat dikerjakan, lalu memberi ringkasan. Gunakan ini untuk migrasi, implementasi, atau proses multi-langkah apa pun.

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

Prompting Chain-of-Thought secara eksplisit meminta model untuk menunjukkan proses penalarannya, meningkatkan akurasi untuk tugas kompleks. Pemecahan langkah demi langkah membantu manusia dan AI memahami logika.

> **🤖 Coba dengan Chat [GitHub Copilot](https://github.com/features/copilot):** Tanyakan tentang pola ini:
> - "Bagaimana saya mengadaptasi pola eksekusi tugas untuk operasi jangka panjang?"
> - "Apa praktik terbaik untuk menyusun preambul alat di aplikasi produksi?"
> - "Bagaimana cara menangkap dan menampilkan pembaruan kemajuan perantara di UI?"

Diagram berikut mengilustrasikan alur kerja Rencana → Eksekusi → Ringkasan ini.

<img src="../../../translated_images/id/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Alur kerja Rencana → Eksekusi → Ringkasan untuk tugas multi-langkah*

**Kode yang Merefleksikan Diri** - Untuk menghasilkan kode berkualitas produksi. Model menghasilkan kode mengikuti standar produksi dengan penanganan kesalahan yang tepat. Gunakan ini saat membangun fitur atau layanan baru.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

Diagram berikut menunjukkan siklus perbaikan iteratif ini — menghasilkan, mengevaluasi, mengidentifikasi kelemahan, dan menyempurnakan sampai kode memenuhi standar produksi.

<img src="../../../translated_images/id/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Siklus perbaikan iteratif - hasilkan, evaluasi, identifikasi masalah, perbaiki, ulangi*

**Analisis Terstruktur** - Untuk evaluasi yang konsisten. Model meninjau kode menggunakan kerangka kerja tetap (kebenaran, praktik, kinerja, keamanan, pemeliharaan). Gunakan ini untuk review kode atau penilaian kualitas.

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
> - "Bagaimana saya dapat menyesuaikan kerangka analisis untuk berbagai jenis review kode?"
> - "Apa cara terbaik mengurai dan memproses output terstruktur secara programatik?"
> - "Bagaimana memastikan tingkat keparahan yang konsisten di berbagai sesi review?"

Diagram berikut menunjukkan bagaimana kerangka terstruktur ini mengorganisasi review kode ke dalam kategori konsisten dengan tingkat keparahan.

<img src="../../../translated_images/id/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Kerangka kerja untuk review kode konsisten dengan tingkat keparahan*

**Chat Multi-Turn** - Untuk percakapan yang membutuhkan konteks. Model mengingat pesan sebelumnya dan membangun dari sana. Gunakan ini untuk sesi bantuan interaktif atau tanya jawab kompleks.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

Diagram berikut memvisualisasikan bagaimana konteks percakapan terakumulasi dengan setiap giliran dan bagaimana itu terkait dengan batas token model.

<img src="../../../translated_images/id/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Bagaimana konteks percakapan terkumpul selama beberapa giliran sampai mencapai batas token*
**Penalaran Langkah demi Langkah** - Untuk masalah yang memerlukan logika yang terlihat. Model menunjukkan penalaran eksplisit untuk setiap langkah. Gunakan ini untuk masalah matematika, teka-teki logika, atau ketika Anda perlu memahami proses pemikiran.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

Diagram di bawah ini menggambarkan bagaimana model memecah masalah menjadi langkah-langkah logis yang eksplisit dan bernomor.

<img src="../../../translated_images/id/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Pola Langkah demi Langkah" width="800"/>

*Memecah masalah menjadi langkah-langkah logis yang eksplisit*

**Output Terbatas** - Untuk respons dengan persyaratan format tertentu. Model mengikuti aturan format dan panjang secara ketat. Gunakan ini untuk ringkasan atau ketika Anda memerlukan struktur output yang tepat.

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

Diagram berikut menunjukkan bagaimana batasan membimbing model untuk menghasilkan output yang benar-benar mengikuti format dan persyaratan panjang Anda.

<img src="../../../translated_images/id/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Pola Output Terbatas" width="800"/>

*Menegakkan persyaratan format, panjang, dan struktur tertentu*

## Jalankan Aplikasi

**Verifikasi deployment:**

Pastikan file `.env` ada di direktori root dengan kredensial Azure (dibuat selama Modul 01). Jalankan ini dari direktori modul (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Mulai aplikasi:**

> **Catatan:** Jika Anda sudah memulai semua aplikasi menggunakan `./start-all.sh` dari direktori root (seperti yang dijelaskan di Modul 01), modul ini sudah berjalan di port 8083. Anda dapat melewati perintah mulai di bawah dan langsung buka http://localhost:8083.

**Opsi 1: Menggunakan Spring Boot Dashboard (Disarankan untuk pengguna VS Code)**

Dev container sudah termasuk ekstensi Spring Boot Dashboard, yang menyediakan antarmuka visual untuk mengelola semua aplikasi Spring Boot. Anda dapat menemukannya di Activity Bar di sisi kiri VS Code (cari ikon Spring Boot).

Dari Spring Boot Dashboard, Anda dapat:
- Melihat semua aplikasi Spring Boot yang tersedia di workspace
- Memulai/berhenti aplikasi dengan satu klik
- Melihat log aplikasi secara real-time
- Memantau status aplikasi

Cukup klik tombol play di sebelah "prompt-engineering" untuk memulai modul ini, atau mulai semua modul sekaligus.

<img src="../../../translated_images/id/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard di VS Code — mulai, hentikan, dan pantau semua modul dari satu tempat*

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

Berikut adalah antarmuka utama modul rekayasa prompt, di mana Anda dapat bereksperimen dengan kedelapan pola secara berdampingan.

<img src="../../../translated_images/id/dashboard-home.5444dbda4bc1f79d.webp" alt="Beranda Dashboard" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Dashboard utama yang menunjukkan semua 8 pola rekayasa prompt beserta karakteristik dan kasus penggunaannya*

## Menjelajahi Pola

Antarmuka web memungkinkan Anda bereksperimen dengan berbagai strategi prompt. Setiap pola menyelesaikan masalah yang berbeda - coba untuk melihat kapan setiap pendekatan bersinar.

> **Catatan: Streaming vs Non-Streaming** — Setiap halaman pola menawarkan dua tombol: **🔴 Stream Response (Live)** dan opsi **Non-streaming**. Streaming menggunakan Server-Sent Events (SSE) untuk menampilkan token secara real-time saat model menghasilkannya, sehingga Anda melihat progres langsung. Opsi non-streaming menunggu seluruh respons selesai baru menampilkannya. Untuk prompt yang memicu penalaran mendalam (misalnya, High Eagerness, Self-Reflecting Code), panggilan non-streaming dapat memakan waktu sangat lama — kadang menit — tanpa umpan balik yang terlihat. **Gunakan streaming saat bereksperimen dengan prompt kompleks** agar Anda dapat melihat model bekerja dan menghindari kesan bahwa permintaan kadaluwarsa.
>
> **Catatan: Persyaratan Browser** — Fitur streaming menggunakan Fetch Streams API (`response.body.getReader()`) yang memerlukan browser penuh (Chrome, Edge, Firefox, Safari). Ini **tidak** berfungsi di Simple Browser bawaan VS Code, karena webview-nya tidak mendukung API ReadableStream. Jika Anda menggunakan Simple Browser, tombol non-streaming tetap berfungsi normal — hanya tombol streaming yang terpengaruh. Buka `http://localhost:8083` di browser eksternal untuk pengalaman penuh.

### Low vs High Eagerness

Tanyakan pertanyaan sederhana seperti "Berapa 15% dari 200?" menggunakan Low Eagerness. Anda akan mendapatkan jawaban langsung dan instan. Sekarang tanyakan sesuatu yang kompleks seperti "Rancang strategi caching untuk API dengan trafik tinggi" menggunakan High Eagerness. Klik **🔴 Stream Response (Live)** dan lihat penalaran rinci model muncul token demi token. Model sama, struktur pertanyaan sama - tetapi prompt memberi tahu seberapa banyak pemikiran yang harus dilakukan.

### Pelaksanaan Tugas (Tool Preambles)

Workflow multi-langkah mendapat manfaat dari perencanaan awal dan narasi kemajuan. Model menguraikan apa yang akan dilakukan, menceritakan tiap langkah, lalu merangkum hasil.

### Self-Reflecting Code

Coba "Buat layanan validasi email". Alih-alih hanya menghasilkan kode dan berhenti, model menghasilkan, mengevaluasi berdasarkan kriteria kualitas, mengidentifikasi kelemahan, dan memperbaiki. Anda akan melihat iterasi hingga kode memenuhi standar produksi.

### Analisis Terstruktur

Review kode membutuhkan kerangka evaluasi yang konsisten. Model menganalisis kode menggunakan kategori tetap (kebenaran, praktik, performa, keamanan) dengan tingkat keparahan.

### Multi-Turn Chat

Tanyakan "Apa itu Spring Boot?" kemudian langsung lanjutkan dengan "Tunjukkan contoh". Model mengingat pertanyaan pertama Anda dan memberi contoh Spring Boot yang spesifik. Tanpa memori, pertanyaan kedua akan terlalu ambigu.

### Penalaran Langkah demi Langkah

Pilih masalah matematika dan coba dengan Penalaran Langkah demi Langkah dan Low Eagerness. Low eagerness hanya memberi jawaban - cepat tapi buram. Penalaran langkah demi langkah menunjukkan setiap perhitungan dan keputusan.

### Output Terbatas

Saat Anda membutuhkan format atau jumlah kata tertentu, pola ini menegakkan kepatuhan ketat. Coba menghasilkan ringkasan dengan tepat 100 kata dalam format poin-poin.

## Apa yang Sebenarnya Anda Pelajari

**Upaya Penalaran Mengubah Segalanya**

GPT-5.2 memungkinkan Anda mengontrol upaya komputasi lewat prompt Anda. Upaya rendah berarti respons cepat dengan eksplorasi minimal. Upaya tinggi berarti model mengambil waktu untuk berpikir mendalam. Anda belajar mencocokkan upaya dengan kompleksitas tugas - jangan membuang waktu untuk pertanyaan sederhana, tapi juga jangan terburu-buru pada keputusan kompleks.

**Struktur Membimbing Perilaku**

Perhatikan tag XML di dalam prompt? Mereka bukan hiasan. Model mengikuti instruksi terstruktur lebih andal daripada teks bebas. Saat Anda membutuhkan proses multi-langkah atau logika kompleks, struktur membantu model melacak posisinya dan tahap selanjutnya. Diagram di bawah memecah prompt yang terstruktur dengan baik, menunjukkan bagaimana tag seperti `<system>`, `<instructions>`, `<context>`, `<user-input>`, dan `<constraints>` mengatur instruksi Anda ke dalam bagian yang jelas.

<img src="../../../translated_images/id/prompt-structure.a77763d63f4e2f89.webp" alt="Struktur Prompt" width="800"/>

*Anatomi prompt yang terstruktur dengan bagian yang jelas dan organisasi bergaya XML*

**Kualitas Melalui Evaluasi Diri**

Pola self-reflecting bekerja dengan membuat kriteria kualitas eksplisit. Alih-alih berharap model "melakukannya dengan benar", Anda memberi tahu secara tepat apa arti "benar": logika benar, penanganan error, performa, keamanan. Model kemudian dapat mengevaluasi outputnya sendiri dan memperbaiki. Ini mengubah pembuatan kode dari lotere menjadi proses.

**Konteks Itu Terbatas**

Percakapan multi-putar bekerja dengan menyertakan riwayat pesan dalam tiap permintaan. Tapi ada batasnya - setiap model memiliki jumlah token maksimum. Saat percakapan bertambah, Anda memerlukan strategi untuk mempertahankan konteks relevan tanpa mencapai batas itu. Modul ini menunjukkan cara kerja memori; nanti Anda akan belajar kapan merangkum, kapan melupakan, dan kapan mengambil kembali.

## Langkah Selanjutnya

**Modul Berikutnya:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 01 - Pengantar](../01-introduction/README.md) | [Kembali ke Utama](../README.md) | [Berikutnya: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berupaya untuk memberikan terjemahan yang akurat, harap diperhatikan bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang berwenang. Untuk informasi yang sangat penting, disarankan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau penafsiran yang salah yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->