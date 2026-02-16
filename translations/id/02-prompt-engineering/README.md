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
- [Tangkapan Layar Aplikasi](../../../02-prompt-engineering)
- [Menjelajahi Pola](../../../02-prompt-engineering)
  - [Eagerness Rendah vs Tinggi](../../../02-prompt-engineering)
  - [Eksekusi Tugas (Tool Preambles)](../../../02-prompt-engineering)
  - [Kode yang Merefleksikan Diri Sendiri](../../../02-prompt-engineering)
  - [Analisis Terstruktur](../../../02-prompt-engineering)
  - [Chat Multi-Giliran](../../../02-prompt-engineering)
  - [Penalaran Langkah demi Langkah](../../../02-prompt-engineering)
  - [Output yang Terbatas](../../../02-prompt-engineering)
- [Apa yang Sebenarnya Anda Pelajari](../../../02-prompt-engineering)
- [Langkah Berikutnya](../../../02-prompt-engineering)

## Apa yang Akan Anda Pelajari

<img src="../../../translated_images/id/what-youll-learn.c68269ac048503b2.webp" alt="Apa yang Akan Anda Pelajari" width="800"/>

Dalam modul sebelumnya, Anda melihat bagaimana memori memungkinkan AI percakapan dan menggunakan Model GitHub untuk interaksi dasar. Sekarang kita akan fokus pada cara Anda mengajukan pertanyaan — prompt itu sendiri — menggunakan GPT-5.2 dari Azure OpenAI. Cara Anda menyusun prompt secara dramatis mempengaruhi kualitas respons yang Anda dapatkan. Kita mulai dengan tinjauan teknik-teknik dasar prompt, lalu beralih ke delapan pola lanjutan yang memanfaatkan kemampuan GPT-5.2 secara penuh.

Kita menggunakan GPT-5.2 karena ia memperkenalkan kontrol penalaran - Anda bisa memberi tahu model seberapa banyak pikirannya sebelum menjawab. Ini membuat berbagai strategi prompting menjadi lebih jelas dan membantu Anda memahami kapan harus menggunakan setiap pendekatan. Kita juga mendapat manfaat dari batasan tingkat yang lebih sedikit di Azure untuk GPT-5.2 dibandingkan Model GitHub.

## Prasyarat

- Telah menyelesaikan Modul 01 (sumber daya Azure OpenAI sudah terdeploy)
- File `.env` di direktori root dengan kredensial Azure (dibuat oleh `azd up` di Modul 01)

> **Catatan:** Jika Anda belum menyelesaikan Modul 01, ikuti petunjuk deployment di sana terlebih dahulu.

## Memahami Rekayasa Prompt

<img src="../../../translated_images/id/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Apa itu Rekayasa Prompt?" width="800"/>

Rekayasa prompt adalah tentang merancang teks input yang secara konsisten memberikan hasil yang Anda butuhkan. Ini bukan sekedar mengajukan pertanyaan - ini tentang menyusun permintaan agar model benar-benar memahami apa yang Anda inginkan dan bagaimana cara memberikannya.

Bayangkan seperti memberi instruksi kepada rekan kerja. "Perbaiki bug" terlalu samar. "Perbaiki null pointer exception di UserService.java baris 45 dengan menambahkan pengecekan null" jelas dan spesifik. Model bahasa bekerja dengan cara yang sama - spesifikasi dan struktur sangat penting.

<img src="../../../translated_images/id/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Bagaimana LangChain4j Berperan" width="800"/>

LangChain4j menyediakan infrastruktur — koneksi model, memori, dan tipe pesan — sementara pola prompt hanyalah teks yang disusun dengan cermat yang Anda kirimkan melalui infrastruktur itu. Blok bangunan utamanya adalah `SystemMessage` (yang mengatur perilaku dan peran AI) dan `UserMessage` (yang membawa permintaan Anda sebenarnya).

## Dasar-Dasar Rekayasa Prompt

<img src="../../../translated_images/id/five-patterns-overview.160f35045ffd2a94.webp" alt="Tinjauan Lima Pola Rekayasa Prompt" width="800"/>

Sebelum masuk ke pola lanjutan dalam modul ini, mari kita tinjau lima teknik prompting dasar. Ini adalah blok bangunan yang harus diketahui setiap insinyur prompt. Jika Anda sudah bekerja dengan [modul Quick Start](../00-quick-start/README.md#2-prompt-patterns), Anda sudah melihat aplikasinya — berikut kerangka konseptual di baliknya.

### Zero-Shot Prompting

Pendekatan paling sederhana: berikan model instruksi langsung tanpa contoh. Model sepenuhnya mengandalkan pelatihannya untuk memahami dan menjalankan tugas. Ini berfungsi baik untuk permintaan langsung dimana perilaku yang diharapkan jelas.

<img src="../../../translated_images/id/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Instruksi langsung tanpa contoh — model menyimpulkan tugas hanya dari instruksi tersebut*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Respon: "Positif"
```

**Kapan digunakan:** Klasifikasi sederhana, pertanyaan langsung, terjemahan, atau tugas apa pun yang bisa ditangani model tanpa panduan tambahan.

### Few-Shot Prompting

Berikan contoh yang menunjukkan pola yang Anda ingin model ikuti. Model belajar format input-output yang diharapkan dari contoh Anda dan menerapkannya pada input baru. Ini sangat meningkatkan konsistensi untuk tugas dimana format atau perilaku yang diinginkan tidak jelas.

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

**Kapan digunakan:** Klasifikasi khusus, format yang konsisten, tugas domain-spesifik, atau saat hasil zero-shot kurang konsisten.

### Chain of Thought

Minta model menunjukkan proses penalarannya langkah demi langkah. Alih-alih langsung ke jawaban, model memecah masalah dan mengerjakan tiap bagian secara eksplisit. Ini meningkatkan akurasi untuk matematika, logika, dan penalaran multi-langkah.

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

**Kapan digunakan:** Masalah matematika, teka-teki logika, debugging, atau tugas apapun dimana menunjukkan proses penalaran meningkatkan akurasi dan kepercayaan.

### Role-Based Prompting

Tetapkan persona atau peran untuk AI sebelum mengajukan pertanyaan. Ini menyediakan konteks yang membentuk nada, kedalaman, dan fokus respons. “Arsitek perangkat lunak” memberikan saran berbeda dibandingkan “pengembang junior” atau “auditor keamanan”.

<img src="../../../translated_images/id/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Mengatur konteks dan persona — pertanyaan yang sama mendapat respons berbeda tergantung peran yang diberikan*

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

**Kapan digunakan:** Review kode, pembimbingan, analisis domain-spesifik, atau saat Anda membutuhkan respons yang disesuaikan dengan tingkat keahlian atau perspektif tertentu.

### Prompt Templates

Buat prompt yang dapat digunakan ulang dengan placeholder variabel. Alih-alih menulis prompt baru setiap kali, definisikan template sekali dan isi dengan nilai yang berbeda. Kelas `PromptTemplate` LangChain4j memudahkan ini dengan sintaks `{{variable}}`.

<img src="../../../translated_images/id/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompt yang dapat digunakan ulang dengan placeholder variabel — satu template, banyak kegunaan*

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

**Kapan digunakan:** Permintaan berulang dengan input berbeda, pemrosesan batch, membangun workflow AI yang bisa digunakan ulang, atau saat struktur prompt tetap sama tapi data berubah.

---

Lima dasar ini memberi Anda toolkit solid untuk sebagian besar tugas prompting. Sisanya di modul ini membangun di atasnya dengan **delapan pola lanjutan** yang memanfaatkan kontrol penalaran, evaluasi diri, dan kemampuan output terstruktur GPT-5.2.

## Pola Lanjutan

Setelah dasar tercakup, mari masuk ke delapan pola lanjutan yang membuat modul ini unik. Tidak semua masalah membutuhkan pendekatan yang sama. Beberapa pertanyaan butuh jawaban cepat, yang lain butuh pemikiran mendalam. Ada yang perlu penalaran terlihat, ada yang cukup hasil saja. Tiap pola di bawah dioptimalkan untuk skenario berbeda — dan kontrol penalaran GPT-5.2 membuat perbedaan makin jelas.

<img src="../../../translated_images/id/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Delapan Pola Prompting" width="800"/>

*Tinjauan delapan pola rekayasa prompt dan kasus penggunaannya*

<img src="../../../translated_images/id/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontrol Penalaran dengan GPT-5.2" width="800"/>

*Kontrol penalaran GPT-5.2 memungkinkan Anda menentukan seberapa banyak model harus berpikir — dari jawaban cepat langsung sampai eksplorasi mendalam*

<img src="../../../translated_images/id/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Perbandingan Upaya Penalaran" width="800"/>

*Eagerness rendah (cepat, langsung) vs eagerness tinggi (mendalam, eksploratif) pendekatan penalaran*

**Eagerness Rendah (Cepat & Fokus)** - Untuk pertanyaan sederhana dimana Anda ingin jawaban cepat dan langsung. Model melakukan penalaran minimal - maksimal 2 langkah. Gunakan ini untuk perhitungan, pencarian, atau pertanyaan langsung.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Jelajahi dengan GitHub Copilot:** Buka [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) dan tanyakan:
> - "Apa perbedaan antara pola prompting eagerness rendah dan eagerness tinggi?"
> - "Bagaimana tag XML dalam prompt membantu menyusun respons AI?"
> - "Kapan saya harus menggunakan pola refleksi diri vs instruksi langsung?"

**Eagerness Tinggi (Mendalam & Teliti)** - Untuk masalah kompleks dimana Anda ingin analisis komprehensif. Model mengeksplorasi secara mendalam dan menunjukkan penalaran rinci. Gunakan ini untuk desain sistem, keputusan arsitektur, atau riset kompleks.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Eksekusi Tugas (Progres Langkah demi Langkah)** - Untuk workflow multi-langkah. Model memberikan rencana awal, menjelaskan tiap langkah saat berjalan, lalu memberikan ringkasan. Gunakan ini untuk migrasi, implementasi, atau proses multi-langkah apa pun.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought prompting secara eksplisit meminta model untuk menunjukkan proses penalarannya, meningkatkan akurasi untuk tugas kompleks. Pemecahan langkah demi langkah membantu manusia dan AI memahami logika.

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Tanyakan tentang pola ini:
> - "Bagaimana saya menyesuaikan pola eksekusi tugas untuk operasi yang berjalan lama?"
> - "Apa praktik terbaik untuk menyusun tool preambles dalam aplikasi produksi?"
> - "Bagaimana cara merekam dan menampilkan update progres antara di UI?"

<img src="../../../translated_images/id/task-execution-pattern.9da3967750ab5c1e.webp" alt="Pola Eksekusi Tugas" width="800"/>

*Rencana → Eksekusi → Ringkas workflow untuk tugas multi-langkah*

**Kode yang Merefleksikan Diri Sendiri** - Untuk menghasilkan kode berkualitas produksi. Model membuat kode, memeriksa kriteria kualitas, dan memperbaikinya secara iteratif. Gunakan ini saat membangun fitur atau layanan baru.

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

*Loop peningkatan iteratif - buat, evaluasi, identifikasi masalah, perbaiki, ulangi*

**Analisis Terstruktur** - Untuk evaluasi yang konsisten. Model meninjau kode menggunakan kerangka kerja tetap (ketepatan, praktik, performa, keamanan). Gunakan ini untuk review kode atau penilaian kualitas.

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

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Tanyakan tentang analisis terstruktur:
> - "Bagaimana saya menyesuaikan kerangka analisis untuk tipe review kode berbeda?"
> - "Apa cara terbaik untuk mengurai dan bertindak atas output terstruktur secara programatis?"
> - "Bagaimana cara memastikan level keparahan konsisten antar sesi review berbeda?"

<img src="../../../translated_images/id/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Pola Analisis Terstruktur" width="800"/>

*Kerangka empat kategori untuk review kode konsisten dengan level keparahan*

**Chat Multi-Giliran** - Untuk percakapan yang memerlukan konteks. Model mengingat pesan sebelumnya dan membangun darinya. Gunakan untuk sesi bantuan interaktif atau Q&A kompleks.

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

*Bagaimana konteks percakapan terakumulasi selama beberapa giliran hingga mencapai batas token*

**Penalaran Langkah demi Langkah** - Untuk masalah yang membutuhkan logika terlihat. Model menunjukkan penalaran eksplisit untuk tiap langkah. Gunakan ini untuk masalah matematika, teka-teki logika, atau saat Anda perlu memahami proses berpikir.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/id/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Pola Langkah demi Langkah" width="800"/>

*Memecah masalah menjadi langkah logis eksplisit*

**Output yang Terbatas** - Untuk respons dengan format yang spesifik. Model mengikuti aturan format dan panjang dengan ketat. Gunakan ini untuk ringkasan atau saat Anda membutuhkan struktur output yang presisi.

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

*Memenuhi persyaratan format, panjang, dan struktur tertentu*

## Menggunakan Sumber Daya Azure yang Ada

**Verifikasi deployment:**

Pastikan file `.env` ada di direktori root dengan kredensial Azure (dibuat saat Modul 01):
```bash
cat ../.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Mulai aplikasi:**

> **Catatan:** Jika Anda sudah memulai semua aplikasi menggunakan `./start-all.sh` dari Modul 01, modul ini sudah berjalan di port 8083. Anda bisa melewati perintah start di bawah dan langsung ke http://localhost:8083.

**Opsi 1: Menggunakan Spring Boot Dashboard (Direkomendasikan untuk pengguna VS Code)**

Dev container menyertakan ekstensi Spring Boot Dashboard, yang menyediakan antarmuka visual untuk mengelola semua aplikasi Spring Boot. Anda dapat menemukannya di Bilah Aktivitas di sisi kiri VS Code (cari ikon Spring Boot).
Dari Spring Boot Dashboard, Anda dapat:
- Melihat semua aplikasi Spring Boot yang tersedia di workspace
- Memulai/menyhentikan aplikasi dengan satu klik
- Melihat log aplikasi secara real-time
- Memantau status aplikasi

Cukup klik tombol play di sebelah "prompt-engineering" untuk memulai modul ini, atau mulai semua modul sekaligus.

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
.\stop.ps1  # Hanya modul ini
# Atau
cd ..; .\stop-all.ps1  # Semua modul
```

## Screenshot Aplikasi

<img src="../../../translated_images/id/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Dashboard utama yang menampilkan semua 8 pola prompt engineering dengan karakteristik dan kasus penggunaannya*

## Menjelajahi Pola

Antarmuka web memungkinkan Anda bereksperimen dengan berbagai strategi prompting. Setiap pola menyelesaikan masalah berbeda - coba untuk melihat kapan setiap pendekatan paling efektif.

### Keinginan Rendah vs Tinggi

Ajukan pertanyaan sederhana seperti "Berapa 15% dari 200?" menggunakan Keinginan Rendah. Anda akan mendapatkan jawaban langsung dan instan. Sekarang ajukan sesuatu yang kompleks seperti "Rancang strategi caching untuk API dengan lalu lintas tinggi" menggunakan Keinginan Tinggi. Perhatikan bagaimana model melambat dan memberikan penjelasan rinci. Model sama, struktur pertanyaan sama - tapi prompt memberitahu seberapa banyak pemikiran yang harus dilakukan.

<img src="../../../translated_images/id/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Perhitungan cepat dengan sedikit penalaran*

<img src="../../../translated_images/id/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Strategi caching komprehensif (2.8MB)*

### Eksekusi Tugas (Tool Preambles)

Alur kerja multi-langkah mendapat manfaat dari perencanaan awal dan narasi kemajuan. Model menguraikan apa yang akan dilakukan, menceritakan setiap langkah, kemudian meringkas hasil.

<img src="../../../translated_images/id/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Menciptakan endpoint REST dengan narasi langkah-demi-langkah (3.9MB)*

### Kode yang Refleksif

Coba "Buat layanan validasi email". Alih-alih hanya menghasilkan kode dan berhenti, model menghasilkan, mengevaluasi berdasarkan kriteria kualitas, mengidentifikasi kelemahan, dan memperbaiki. Anda akan melihat ia beriterasi sampai kode memenuhi standar produksi.

<img src="../../../translated_images/id/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Layanan validasi email lengkap (5.2MB)*

### Analisis Terstruktur

Ulasan kode memerlukan kerangka evaluasi yang konsisten. Model menganalisis kode menggunakan kategori tetap (ketepatan, praktek, performa, keamanan) dengan tingkat keparahan.

<img src="../../../translated_images/id/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Ulasan kode berbasis kerangka*

### Chat Multi-Turn

Tanya "Apa itu Spring Boot?" lalu langsung lanjutkan dengan "Tunjukkan contohnya". Model mengingat pertanyaan pertama dan memberikan contoh Spring Boot secara khusus. Tanpa memori, pertanyaan kedua itu akan terlalu samar.

<img src="../../../translated_images/id/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Pelestarian konteks antar pertanyaan*

### Penalaran Langkah-demi-Langkah

Pilih masalah matematika dan coba dengan Penalaran Langkah-demi-Langkah dan Keinginan Rendah. Keinginan rendah hanya memberi Anda jawaban - cepat tapi tidak jelas. Penalaran langkah-demi-langkah menunjukkan setiap perhitungan dan keputusan.

<img src="../../../translated_images/id/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Masalah matematika dengan langkah eksplisit*

### Output Terbatas

Saat Anda membutuhkan format tertentu atau jumlah kata spesifik, pola ini menegakkan kepatuhan ketat. Coba buat ringkasan dengan tepat 100 kata dalam format poin.

<img src="../../../translated_images/id/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Ringkasan machine learning dengan kontrol format*

## Apa yang Sebenarnya Anda Pelajari

**Upaya Penalaran Mengubah Segalanya**

GPT-5.2 memungkinkan Anda mengontrol usaha komputasi melalui prompt Anda. Upaya rendah berarti respons cepat dengan eksplorasi minimal. Upaya tinggi berarti model mengambil waktu untuk berpikir mendalam. Anda belajar mencocokkan usaha dengan kompleksitas tugas - jangan buang waktu untuk pertanyaan sederhana, tapi juga jangan terburu-buru dalam keputusan kompleks.

**Struktur Mengarahkan Perilaku**

Perhatikan tag XML dalam prompt? Itu bukan hiasan. Model mengikuti instruksi terstruktur lebih dapat diandalkan daripada teks bebas. Ketika Anda membutuhkan proses multi-langkah atau logika kompleks, struktur membantu model melacak posisinya dan langkah selanjutnya.

<img src="../../../translated_images/id/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi prompt yang terstruktur dengan jelas dan organisasi gaya XML*

**Kualitas Melalui Evaluasi Diri**

Pola self-reflecting bekerja dengan membuat kriteria kualitas menjadi eksplisit. Alih-alih berharap model "melakukan dengan benar", Anda memberitahu secara tepat apa arti "benar": logika yang benar, penanganan kesalahan, performa, keamanan. Model bisa mengevaluasi sendiri outputnya dan memperbaiki. Ini mengubah generasi kode dari judi menjadi proses.

**Konteks Itu Terbatas**

Percakapan multi-turn bekerja dengan menyertakan riwayat pesan pada setiap permintaan. Tapi ada batas - setiap model memiliki jumlah token maksimum. Saat percakapan berkembang, Anda butuh strategi untuk menjaga konteks relevan tanpa melewati batas itu. Modul ini menunjukkan cara kerja memori; nanti Anda akan belajar kapan harus merangkum, kapan melupakan, dan kapan mengambil kembali.

## Langkah Selanjutnya

**Modul Berikutnya:** [03-rag - RAG (Generasi dengan Pencarian Terintegrasi)](../03-rag/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 01 - Pengantar](../01-introduction/README.md) | [Kembali ke Utama](../README.md) | [Berikutnya: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berupaya untuk memberikan hasil yang akurat, mohon diingat bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidaktepatan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang sahih. Untuk informasi yang penting, disarankan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau penafsiran yang keliru yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->