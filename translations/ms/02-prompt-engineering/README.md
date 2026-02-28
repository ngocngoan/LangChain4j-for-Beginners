# Modul 02: Kejuruteraan Prompt dengan GPT-5.2

## Jadual Kandungan

- [Video Walkthrough](../../../02-prompt-engineering)
- [Apa yang Anda Akan Pelajari](../../../02-prompt-engineering)
- [Prasyarat](../../../02-prompt-engineering)
- [Memahami Kejuruteraan Prompt](../../../02-prompt-engineering)
- [Asas Kejuruteraan Prompt](../../../02-prompt-engineering)
  - [Prompt Tanpa Contoh (Zero-Shot)](../../../02-prompt-engineering)
  - [Prompt Dengan Beberapa Contoh (Few-Shot)](../../../02-prompt-engineering)
  - [Rantaian Pemikiran (Chain of Thought)](../../../02-prompt-engineering)
  - [Prompt Berdasarkan Peranan (Role-Based Prompting)](../../../02-prompt-engineering)
  - [Templat Prompt](../../../02-prompt-engineering)
- [Corak Lanjutan](../../../02-prompt-engineering)
- [Menggunakan Sumber Azure Sedia Ada](../../../02-prompt-engineering)
- [Tangkapan Layar Aplikasi](../../../02-prompt-engineering)
- [Meneroka Corak](../../../02-prompt-engineering)
  - [Semangat Rendah vs Tinggi](../../../02-prompt-engineering)
  - [Pelaksanaan Tugas (Prabina Alat)](../../../02-prompt-engineering)
  - [Kod Refleksi Diri](../../../02-prompt-engineering)
  - [Analisis Berstruktur](../../../02-prompt-engineering)
  - [Sembang Pelbagai Giliran](../../../02-prompt-engineering)
  - [Pemikiran Langkah demi Langkah](../../../02-prompt-engineering)
  - [Keluaran Terhad](../../../02-prompt-engineering)
- [Apa yang Sebenarnya Anda Pelajari](../../../02-prompt-engineering)
- [Langkah Seterusnya](../../../02-prompt-engineering)

## Video Walkthrough

Tonton sesi langsung ini yang menerangkan cara memulakan modul ini: [Prompt Engineering with LangChain4j - Live Session](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## Apa yang Anda Akan Pelajari

<img src="../../../translated_images/ms/what-youll-learn.c68269ac048503b2.webp" alt="Apa yang Anda Akan Pelajari" width="800"/>

Dalam modul sebelumnya, anda telah melihat bagaimana memori membolehkan AI perbualan dan menggunakan Model GitHub untuk interaksi asas. Kini kita akan fokus pada cara anda mengajukan soalan — prompt itu sendiri — menggunakan GPT-5.2 Azure OpenAI. Cara anda menyusun prompt memberi kesan besar kepada kualiti respons yang anda terima. Kita mulakan dengan ulasan teknik asas prompting, kemudian beralih ke lapan corak lanjutan yang memanfaatkan sepenuhnya keupayaan GPT-5.2.

Kita akan menggunakan GPT-5.2 kerana ia memperkenalkan kawalan pemikiran – anda boleh memberitahu model berapa banyak pemikiran yang perlu dilakukan sebelum menjawab. Ini menjadikan strategi prompting yang berbeza lebih nyata dan membantu anda memahami bila untuk menggunakan setiap pendekatan. Kita juga mendapat manfaat dari limit kadar yang lebih sedikit untuk GPT-5.2 di Azure berbanding Model GitHub.

## Prasyarat

- Selesai Modul 01 (sumber Azure OpenAI diterapkan)
- Fail `.env` dalam direktori akar dengan kelayakan Azure (dicipta oleh `azd up` dalam Modul 01)

> **Nota:** Jika anda belum menyelesaikan Modul 01, ikut arahan penerapan di sana terlebih dahulu.

## Memahami Kejuruteraan Prompt

<img src="../../../translated_images/ms/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Apa itu Kejuruteraan Prompt?" width="800"/>

Kejuruteraan prompt adalah tentang mereka bentuk teks input yang sentiasa mendapat hasil yang anda perlukan. Ia bukan sekadar bertanya soalan - ia tentang menyusun permintaan supaya model memahami dengan tepat apa yang anda inginkan dan cara untuk menyampaikannya.

Fikirkan ia seperti memberi arahan kepada rakan sekerja. "Betulkan pepijat" adalah samar. "Betulkan pengecualian penunjuk null di UserService.java baris 45 dengan menambah pemeriksaan null" adalah spesifik. Model bahasa berfungsi dengan cara yang sama - kekhususan dan struktur penting.

<img src="../../../translated_images/ms/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Bagaimana LangChain4j Sesuai" width="800"/>

LangChain4j menyediakan infrastruktur — sambungan model, memori, dan jenis mesej — sementara corak prompt hanyalah teks yang disusun dengan teliti yang anda hantar melalui infrastruktur itu. Blok binaan utama adalah `SystemMessage` (yang menetapkan tingkah laku dan peranan AI) dan `UserMessage` (yang membawa permintaan sebenar anda).

## Asas Kejuruteraan Prompt

<img src="../../../translated_images/ms/five-patterns-overview.160f35045ffd2a94.webp" alt="Gambaran Keseluruhan Lima Corak Kejuruteraan Prompt" width="800"/>

Sebelum menyelami corak lanjutan dalam modul ini, mari kita ulas lima teknik prompting asas. Ini adalah blok binaan yang harus diketahui oleh setiap jurutera prompt. Jika anda sudah mengusahakan [modul Mula Cepat](../00-quick-start/README.md#2-prompt-patterns), anda telah melihat ini dalam tindakan — berikut rangka kerja konseptual di sebaliknya.

### Prompt Tanpa Contoh (Zero-Shot Prompting)

Pendekatan paling mudah: beri model arahan langsung tanpa contoh. Model bergantung sepenuhnya pada latihannya untuk memahami dan melaksanakan tugasan. Ini berkesan untuk permintaan ringkas di mana tingkah laku yang dijangka adalah jelas.

<img src="../../../translated_images/ms/zero-shot-prompting.7abc24228be84e6c.webp" alt="Prompt Tanpa Contoh (Zero-Shot Prompting)" width="800"/>

*Arahan langsung tanpa contoh — model membuat inferens tugasan dari arahan sahaja*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Respons: "Positif"
```

**Bila digunakan:** Klasifikasi mudah, soalan langsung, terjemahan, atau mana-mana tugas yang model boleh kendalikan tanpa panduan tambahan.

### Prompt Dengan Beberapa Contoh (Few-Shot Prompting)

Berikan contoh yang menunjukkan corak yang anda mahu model ikuti. Model belajar format input-output yang dijangka dari contoh anda dan menerapkannya pada input baru. Ini meningkatkan konsistensi untuk tugasan di mana format atau tingkah laku yang diingini tidak jelas.

<img src="../../../translated_images/ms/few-shot-prompting.9d9eace1da88989a.webp" alt="Prompt Dengan Beberapa Contoh" width="800"/>

*Belajar dari contoh — model mengenal pasti corak dan menerapkannya pada input baru*

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

**Bila digunakan:** Klasifikasi khusus, format konsisten, tugasan domain-spesifik, atau apabila hasil zero-shot tidak konsisten.

### Rantaian Pemikiran (Chain of Thought)

Minta model menunjukkan pemikirannya langkah demi langkah. Sebaliknya dari terus ke jawapan, model memecahkan masalah dan menguraikan setiap bahagian secara eksplisit. Ini meningkatkan ketepatan untuk matematik, logik, dan tugasan pemikiran berbilang langkah.

<img src="../../../translated_images/ms/chain-of-thought.5cff6630e2657e2a.webp" alt="Prompt Rantaian Pemikiran" width="800"/>

*Pemikiran langkah demi langkah — memecah masalah kompleks ke dalam langkah logik yang jelas*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model menunjukkan: 15 - 8 = 7, kemudian 7 + 12 = 19 epal
```

**Bila digunakan:** Masalah matematik, teka-teki logik, penyahpepijatan, atau mana-mana tugasan yang menunjukkan proses pemikiran meningkatkan ketepatan dan kepercayaan.

### Prompt Berdasarkan Peranan (Role-Based Prompting)

Tetapkan personaliti atau peranan AI sebelum bertanya soalan anda. Ini menyediakan konteks yang membentuk nada, kedalaman, dan fokus respons. "Arkitek perisian" memberi nasihat berbeza daripada "pembangun junior" atau "juruaudit keselamatan".

<img src="../../../translated_images/ms/role-based-prompting.a806e1a73de6e3a4.webp" alt="Prompt Berdasarkan Peranan" width="800"/>

*Menetapkan konteks dan personaliti — soalan yang sama mendapat respons berbeza bergantung pada peranan yang diberikan*

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

**Bila digunakan:** Ulasan kod, pembelajaran, analisis domain-spesifik, atau apabila anda memerlukan respons disesuaikan pada tahap kepakaran atau perspektif tertentu.

### Templat Prompt

Cipta prompt yang boleh digunakan semula dengan tempat letak pembolehubah. Daripada menulis prompt baru setiap kali, definisikan templat sekali dan isikan nilai yang berbeza. Kelas `PromptTemplate` LangChain4j memudahkan ini dengan sintaks `{{variable}}`.

<img src="../../../translated_images/ms/prompt-templates.14bfc37d45f1a933.webp" alt="Templat Prompt" width="800"/>

*Prompt boleh guna semula dengan tempat letak pembolehubah — satu templat, banyak kegunaan*

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

**Bila digunakan:** Pertanyaan ulang dengan input berbeza, pemprosesan batch, membina aliran kerja AI boleh guna semula, atau mana-mana senario di mana struktur prompt kekal sama tetapi data berubah.

---

Lima asas ini memberi anda toolkit yang kukuh untuk kebanyakan tugasan prompting. Selebihnya modul ini dibina ke atasnya dengan **lapan corak lanjutan** yang memanfaatkan kawalan pemikiran GPT-5.2, penilaian kendiri, dan keupayaan keluaran berstruktur.

## Corak Lanjutan

Dengan asas-asas sudah ditutup, mari beralih ke lapan corak lanjutan yang menjadikan modul ini unik. Tidak semua masalah memerlukan pendekatan yang sama. Sesetengah soalan perlukan jawapan cepat, yang lain perlukan pemikiran mendalam. Ada yang perlukan pemikiran yang kelihatan, ada yang sekadar hasil. Setiap corak di bawah dioptimumkan untuk senario berbeza — dan kawalan pemikiran GPT-5.2 menjadikan perbezaan itu lebih nyata.

<img src="../../../translated_images/ms/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Lapan Corak Prompting" width="800"/>

*Gambaran keseluruhan lapan corak kejuruteraan prompt dan kegunaannya*

<img src="../../../translated_images/ms/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kawalan Pemikiran dengan GPT-5.2" width="800"/>

*Kawalan pemikiran GPT-5.2 membolehkan anda tentukan berapa banyak pemikiran model perlu lakukan — dari jawapan pantas terus ke penerokaan mendalam*

**Semangat Rendah (Cepat & Fokus)** - Untuk soalan mudah di mana anda mahukan jawapan pantas dan terus. Model membuat pemikiran minimum - maksimum 2 langkah. Gunakan ini untuk pengiraan, carian, atau soalan mudah.

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

> 💡 **Teroka dengan GitHub Copilot:** Buka [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) dan tanya:
> - "Apa perbezaan antara corak prompting semangat rendah dan semangat tinggi?"
> - "Bagaimana tag XML dalam prompt membantu menyusun respons AI?"
> - "Bila patut saya gunakan corak refleksi kendiri berbanding arahan langsung?"

**Semangat Tinggi (Mendalam & Teliti)** - Untuk masalah kompleks di mana anda mahukan analisis menyeluruh. Model meneroka dengan teliti dan menunjukkan pemikiran terperinci. Gunakan ini untuk reka bentuk sistem, keputusan seni bina, atau penyelidikan rumit.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Pelaksanaan Tugas (Kemajuan Langkah demi Langkah)** - Untuk aliran kerja berbilang langkah. Model menyediakan pelan awal, menceritakan setiap langkah semasa kerja berjalan, kemudian memberi ringkasan. Gunakan ini untuk migrasi, pelaksanaan, atau proses berbilang langkah.

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

Prompt rantai pemikiran secara eksplisit meminta model menunjukkan proses pemikiran, meningkatkan ketepatan untuk tugasan kompleks. Penguraian langkah demi langkah membantu manusia dan AI faham logik.

> **🤖 Cuba dengan Sembang [GitHub Copilot](https://github.com/features/copilot):** Tanyakan tentang corak ini:
> - "Bagaimana saya akan sesuaikan corak pelaksanaan tugas untuk operasi jangka panjang?"
> - "Apakah amalan terbaik untuk menyusun prabina alat dalam aplikasi produksi?"
> - "Bagaimana saya boleh tangkap dan paparkan kemas kini kemajuan pertengahan dalam UI?"

<img src="../../../translated_images/ms/task-execution-pattern.9da3967750ab5c1e.webp" alt="Corak Pelaksanaan Tugas" width="800"/>

*Aliran kerja Rancang → Laksanakan → Rumus untuk tugasan berbilang langkah*

**Kod Refleksi Diri** - Untuk menjana kod kualiti produksi. Model menjana kod mengikut piawaian produksi dengan pengendalian ralat yang betul. Gunakan ini bila membina ciri atau perkhidmatan baru.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ms/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Kitaran Refleksi Diri" width="800"/>

*Kitaran penambahbaikan berulang - jana, nilai, kenal pasti isu, perbaiki, ulang*

**Analisis Berstruktur** - Untuk penilaian konsisten. Model mengulas kod menggunakan rangka kerja tetap (ketepatan, amalan, prestasi, keselamatan, boleh diselenggara). Gunakan ini untuk ulasan kod atau penilaian kualiti.

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

> **🤖 Cuba dengan Sembang [GitHub Copilot](https://github.com/features/copilot):** Tanyakan tentang analisis berstruktur:
> - "Bagaimana saya boleh sesuaikan rangka kerja analisis untuk jenis ulasan kod berbeza?"
> - "Apakah cara terbaik untuk mengurai dan bertindak atas keluaran berstruktur secara program?"
> - "Bagaimana saya pastikan tahap keparahan konsisten merentas sesi ulasan berbeza?"

<img src="../../../translated_images/ms/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Corak Analisis Berstruktur" width="800"/>

*Rangka kerja untuk ulasan kod konsisten dengan tahap keparahan*

**Sembang Pelbagai Giliran** - Untuk perbualan yang memerlukan konteks. Model mengingati mesej sebelumnya dan membina daripadanya. Gunakan ini untuk sesi bantuan interaktif atau soal jawab kompleks.

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

**Pemikiran Langkah demi Langkah** - Untuk masalah yang memerlukan logik yang kelihatan. Model menunjukkan pemikiran eksplisit untuk setiap langkah. Gunakan ini untuk masalah matematik, teka-teki logik, atau bila anda perlu faham proses pemikiran.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ms/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Corak Langkah demi Langkah" width="800"/>

*Memecah masalah ke dalam langkah logik yang jelas*

**Keluaran Terhad** - Untuk respons dengan keperluan format khusus. Model mematuhi ketat peraturan format dan panjang. Gunakan ini untuk ringkasan atau bila anda perlukan struktur output yang tepat.

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

<img src="../../../translated_images/ms/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Corak Keluaran Terhad" width="800"/>

*Mengekalkan keperluan format, panjang, dan struktur tertentu*

## Menggunakan Sumber Azure Sedia Ada

**Sahkan penerapan:**

Pastikan fail `.env` wujud dalam direktori akar dengan kelayakan Azure (dicipta semasa Modul 01):
```bash
cat ../.env  # Harus memaparkan AZURE_OPENAI_ENDPOINT, API_KEY, PENDEPLOYAN
```

**Mulakan aplikasi:**

> **Nota:** Jika anda sudah memulakan semua aplikasi menggunakan `./start-all.sh` dari Modul 01, modul ini sudah berjalan di port 8083. Anda boleh langkau arahan mula di bawah dan terus ke http://localhost:8083.

**Pilihan 1: Menggunakan Papan Pemuka Spring Boot (Disyorkan untuk pengguna VS Code)**
Kontena dev termasuk sambungan Spring Boot Dashboard, yang menyediakan antara muka visual untuk menguruskan semua aplikasi Spring Boot. Anda boleh menemuinya di Bar Aktiviti di sebelah kiri VS Code (cari ikon Spring Boot).

Daripada Spring Boot Dashboard, anda boleh:
- Melihat semua aplikasi Spring Boot yang tersedia dalam ruang kerja
- Memulakan/menhentikan aplikasi dengan satu klik
- Melihat log aplikasi secara masa nyata
- Memantau status aplikasi

Cuma klik butang main di sebelah "prompt-engineering" untuk memulakan modul ini, atau mulakan semua modul sekaligus.

<img src="../../../translated_images/ms/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Pilihan 2: Menggunakan skrip shell**

Mulakan semua aplikasi web (modul 01-04):

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

Atau mulakan hanya modul ini:

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

Kedua-dua skrip secara automatik memuatkan pembolehubah persekitaran dari fail `.env` akar dan akan membina JAR jika ia tidak wujud.

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

## Skrin Aplikasi

<img src="../../../translated_images/ms/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Papan pemuka utama yang memaparkan semua 8 corak kejuruteraan prompt dengan ciri-ciri dan kes penggunaan mereka*

## Meneroka Corak-corak

Antara muka web membolehkan anda mencuba pelbagai strategi prompting. Setiap corak menyelesaikan masalah yang berbeza - cuba mereka untuk melihat bila setiap pendekatan bersinar.

> **Nota: Aliran vs Bukan Aliran** — Setiap halaman corak menawarkan dua butang: **🔴 Aliran Respons (Masa Nyata)** dan pilihan **Bukan aliran**. Aliran menggunakan Server-Sent Events (SSE) untuk memaparkan token secara masa nyata semasa model menjana, jadi anda melihat kemajuan dengan segera. Pilihan bukan aliran menunggu keseluruhan respons sebelum memaparkannya. Untuk prompt yang mencetuskan penalaran mendalam (contoh, High Eagerness, Kod Reflektif Sendiri), panggilan bukan aliran boleh mengambil masa yang sangat lama — kadangkala minit — tanpa maklum balas jelas. **Gunakan aliran semasa mencuba prompt yang kompleks** supaya anda boleh melihat model berfungsi dan mengelakkan tanggapan bahawa permintaan telah tamat masa.
>
> **Nota: Keperluan Pelayar** — Ciri aliran menggunakan Fetch Streams API (`response.body.getReader()`) yang memerlukan pelayar penuh (Chrome, Edge, Firefox, Safari). Ia **tidak** berfungsi dalam Simple Browser terbina dalam VS Code, kerana webview-nya tidak menyokong ReadableStream API. Jika anda menggunakan Simple Browser, butang bukan aliran masih akan berfungsi secara normal — hanya butang aliran terjejas. Buka `http://localhost:8083` dalam pelayar luaran untuk pengalaman penuh.

### Rendah vs Tinggi Eagerness

Tanya soalan mudah seperti "Apakah 15% daripada 200?" menggunakan Low Eagerness. Anda akan mendapat jawapan segera dan terus. Sekarang tanya sesuatu yang kompleks seperti "Reka strategi caching untuk API trafik tinggi" menggunakan High Eagerness. Klik **🔴 Aliran Respons (Masa Nyata)** dan tonton penalaran terperinci model muncul token demi token. Model sama, struktur soalan sama - tetapi prompt memberitahunya berapa banyak pemikiran untuk dilakukan.

### Pelaksanaan Tugas (Preambul Alat)

Aliran kerja berbilang langkah mendapat manfaat daripada perancangan awal dan narasi kemajuan. Model menggariskan apa yang akan dilakukan, merakam setiap langkah, kemudian merumuskan hasil.

### Kod Reflektif Sendiri

Cuba "Buat perkhidmatan pengesahan emel". Daripada hanya menjana kod dan berhenti, model menjana, menilai berdasarkan kriteria kualiti, mengenal pasti kelemahan, dan memperbaiki. Anda akan melihat ia mengulangi sehingga kod mencapai piawaian pengeluaran.

### Analisis Berstruktur

Ulasan kod memerlukan rangka kerja penilaian yang konsisten. Model menganalisis kod menggunakan kategori tetap (ketepatan, amalan, prestasi, keselamatan) dengan tahap keterukan.

### Sembang Pelbagai Giliran

Tanya "Apakah Spring Boot?" kemudian teruskan dengan "Tunjukkan saya contoh". Model mengingati soalan pertama anda dan memberikan contoh Spring Boot khusus. Tanpa memori, soalan kedua itu terlalu kabur.

### Penalaran Langkah demi Langkah

Pilih masalah matematik dan cuba dengan Penalaran Langkah demi Langkah dan Low Eagerness. Low eagerness hanya memberikan jawapan - pantas tapi kurang jelas. Langkah demi langkah menunjukkan setiap pengiraan dan keputusan.

### Output Terhad

Apabila anda memerlukan format tertentu atau bilangan perkataan yang spesifik, corak ini memaksa kepatuhan ketat. Cuba jana ringkasan dengan tepat 100 perkataan dalam format titik peluru.

## Apa yang Anda Sebenarnya Belajar

**Usaha Penalaran Mengubah Segalanya**

GPT-5.2 membolehkan anda mengawal usaha pengiraan melalui prompt anda. Usaha rendah bermakna respons pantas dengan penerokaan minimum. Usaha tinggi bermakna model mengambil masa untuk berfikir dengan mendalam. Anda belajar menyesuaikan usaha dengan kerumitan tugasan - jangan bazirkan masa pada soalan mudah, tapi jangan tergesa-gesa membuat keputusan kompleks juga.

**Struktur Membimbing Perilaku**

Perasan tag XML dalam prompt? Mereka bukan hiasan. Model lebih boleh dipercayai mengikuti arahan berstruktur daripada teks bebas. Apabila anda memerlukan proses berbilang langkah atau logik kompleks, struktur membantu model mengesan di mana ia berada dan apa yang seterusnya.

<img src="../../../translated_images/ms/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi prompt yang berstruktur baik dengan bahagian jelas dan organisasi gaya XML*

**Kualiti Melalui Penilaian Diri**

Corak reflektif sendiri berfungsi dengan membuat kriteria kualiti jelas. Daripada berharap model "buat dengan betul", anda beritahu dengan tepat apa maksud "betul": logik tepat, pengendalian ralat, prestasi, keselamatan. Model kemudian boleh menilai hasil sendiri dan memperbaiki. Ini menjadikan penjanaan kod daripada seumpama loteri ke satu proses.

**Konteks Adalah Terhad**

Perbualan berbilang giliran berfungsi dengan memasukkan sejarah mesej dengan setiap permintaan. Tetapi ada had - setiap model mempunyai jumlah token maksimum. Apabila perbualan berkembang, anda akan memerlukan strategi untuk mengekalkan konteks relevan tanpa mencapai had itu. Modul ini menunjukkan bagaimana memori berfungsi; nanti anda akan belajar bila untuk merumus, bila untuk melupakan, dan bila untuk mengakses semula.

## Langkah Seterusnya

**Modul Seterusnya:** [03-rag - RAG (Penjanaan Diperkaya Pengambilan)](../03-rag/README.md)

---

**Navigasi:** [← Sebelum: Modul 01 - Pengenalan](../01-introduction/README.md) | [Kembali ke Utama](../README.md) | [Seterusnya: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila ambil perhatian bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang sahih. Untuk maklumat penting, terjemahan profesional oleh manusia adalah disyorkan. Kami tidak bertanggungjawab atas sebarang kekeliruan atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->