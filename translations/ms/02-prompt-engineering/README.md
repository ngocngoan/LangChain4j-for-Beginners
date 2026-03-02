# Modul 02: Kejuruteraan Prompt dengan GPT-5.2

## Jadual Kandungan

- [Video Panduan](../../../02-prompt-engineering)
- [Apa yang Anda Akan Pelajari](../../../02-prompt-engineering)
- [Prasyarat](../../../02-prompt-engineering)
- [Memahami Kejuruteraan Prompt](../../../02-prompt-engineering)
- [Asas Kejuruteraan Prompt](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Corak Lanjutan](../../../02-prompt-engineering)
- [Menggunakan Sumber Azure Sedia Ada](../../../02-prompt-engineering)
- [Tangkapan Skrin Aplikasi](../../../02-prompt-engineering)
- [Meneroka Corak-Corak](../../../02-prompt-engineering)
  - [Kerajinan Rendah vs Tinggi](../../../02-prompt-engineering)
  - [Pelaksanaan Tugasan (Prakata Alat)](../../../02-prompt-engineering)
  - [Kod Refleksi Diri](../../../02-prompt-engineering)
  - [Analisis Berstruktur](../../../02-prompt-engineering)
  - [Sembang Multi-Pusingan](../../../02-prompt-engineering)
  - [Penalaran Langkah demi Langkah](../../../02-prompt-engineering)
  - [Output Terhad](../../../02-prompt-engineering)
- [Apa yang Sebenarnya Anda Pelajari](../../../02-prompt-engineering)
- [Langkah Seterusnya](../../../02-prompt-engineering)

## Video Panduan

Tonton sesi langsung ini yang menjelaskan cara memulakan modul ini:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Apa yang Anda Akan Pelajari

<img src="../../../translated_images/ms/what-youll-learn.c68269ac048503b2.webp" alt="Apa yang Anda Akan Pelajari" width="800"/>

Dalam modul sebelumnya, anda telah melihat bagaimana memori membolehkan AI perbualan dan menggunakan Model GitHub untuk interaksi asas. Kini kita akan fokus pada cara anda mengemukakan soalan — prompt itu sendiri — menggunakan GPT-5.2 Azure OpenAI. Cara anda menyusun prompt anda sangat mempengaruhi kualiti jawapan yang anda peroleh. Kita mula dengan semakan teknik asas prompting, kemudian beralih ke lapan corak lanjutan yang memanfaatkan sepenuhnya keupayaan GPT-5.2.

Kita menggunakan GPT-5.2 kerana ia memperkenalkan kawalan penalaran - anda boleh memberitahu model berapa banyak pemikiran yang perlu dilakukan sebelum menjawab. Ini menjadikan strategi prompting yang berbeza lebih jelas dan membantu anda faham bila untuk menggunakan setiap pendekatan. Kita juga akan mendapat manfaat daripada had kadar yang lebih sedikit Azure untuk GPT-5.2 berbanding Model GitHub.

## Prasyarat

- Modul 01 selesai (sumber Azure OpenAI telah dideploy)
- Fail `.env` dalam direktori root dengan kelayakan Azure (dicipta oleh `azd up` dalam Modul 01)

> **Nota:** Jika anda belum menyelesaikan Modul 01, ikuti arahan deploy di sana terlebih dahulu.

## Memahami Kejuruteraan Prompt

<img src="../../../translated_images/ms/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Apa itu Kejuruteraan Prompt?" width="800"/>

Kejuruteraan prompt adalah tentang mereka bentuk teks input yang secara konsisten memberikan anda hasil yang anda perlukan. Ia bukan sekadar bertanya soalan - ia adalah menyusun permintaan supaya model memahami dengan tepat apa yang anda mahu dan bagaimana untuk menyampaikannya.

Fikirkan ia seperti memberi arahan kepada rakan sekerja. "Betulkan pepijat" adalah samar. "Betulkan pengecualian penunjuk null di UserService.java baris 45 dengan menambah pemeriksaan null" adalah spesifik. Model bahasa berfungsi dengan cara yang sama - kepastian dan struktur sangat penting.

<img src="../../../translated_images/ms/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Bagaimana LangChain4j Sesuai" width="800"/>

LangChain4j menyediakan infrastruktur — sambungan model, memori, dan jenis mesej — manakala corak prompt hanyalah teks yang disusun rapi yang anda hantar melalui infrastruktur itu. Blok binaan utama ialah `SystemMessage` (yang menetapkan tingkah laku dan peranan AI) dan `UserMessage` (yang membawa permintaan sebenar anda).

## Asas Kejuruteraan Prompt

<img src="../../../translated_images/ms/five-patterns-overview.160f35045ffd2a94.webp" alt="Gambaran Keseluruhan Lima Corak Kejuruteraan Prompt" width="800"/>

Sebelum menyelami corak lanjutan dalam modul ini, mari kita semak lima teknik prompting asas. Ini adalah blok binaan yang setiap jurutera prompt harus tahu. Jika anda sudah melalui [modul Permulaan Pantas](../00-quick-start/README.md#2-prompt-patterns), anda telah melihat ini beraksi — ini adalah rangka kerja konsep di sebaliknya.

### Zero-Shot Prompting

Pendekatan paling mudah: berikan model arahan langsung tanpa contoh. Model bergantung sepenuhnya pada latihannya untuk memahami dan menjalankan tugas. Ini berfungsi dengan baik untuk permintaan mudah di mana tingkah laku yang dijangkakan jelas.

<img src="../../../translated_images/ms/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Arahan langsung tanpa contoh — model membuat inferens tugas daripada arahan sahaja*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Respons: "Positif"
```

**Bila untuk digunakan:** Pengelasan mudah, soalan langsung, terjemahan, atau mana-mana tugasan yang model boleh kendalikan tanpa panduan tambahan.

### Few-Shot Prompting

Berikan contoh yang menunjukkan corak yang anda mahu model ikuti. Model mempelajari format input-output yang dijangka daripada contoh anda dan menggunakannya kepada input baru. Ini meningkatkan konsistensi dengan ketara untuk tugasan di mana format atau tingkah laku yang diinginkan tidak jelas.

<img src="../../../translated_images/ms/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Belajar daripada contoh — model mengenal pasti corak dan menggunakannya pada input baru*

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

**Bila untuk digunakan:** Pengelasan khusus, pemformatan konsisten, tugasan domain khusus, atau apabila hasil zero-shot tidak konsisten.

### Chain of Thought

Minta model menunjukkan penalarannya secara langkah demi langkah. Daripada terus ke jawapan, model memecahkan masalah dan bekerja melalui setiap bahagian secara jelas. Ini meningkatkan ketepatan pada tugasan matematik, logik, dan penalaran multi-langkah.

<img src="../../../translated_images/ms/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Penalaran langkah demi langkah — memecahkan masalah kompleks ke dalam langkah logik eksplisit*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model menunjukkan: 15 - 8 = 7, kemudian 7 + 12 = 19 epal
```

**Bila untuk digunakan:** Masalah matematik, teka-teki logik, debugging, atau mana-mana tugasan di mana penunjukan proses penalaran meningkatkan ketepatan dan keyakinan.

### Role-Based Prompting

Tetapkan persona atau peranan untuk AI sebelum bertanya soalan anda. Ini menyediakan konteks yang membentuk nada, kedalaman, dan fokus jawapan. Seorang "arkitek perisian" memberi nasihat berbeza daripada seorang "pembangun junior" atau "juruaudit keselamatan".

<img src="../../../translated_images/ms/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Menetapkan konteks dan persona — soalan sama mendapat jawapan berbeza bergantung pada peranan yang ditetapkan*

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

**Bila untuk digunakan:** Semakan kod, pengajaran, analisis domain khusus, atau apabila anda memerlukan jawapan yang disesuaikan dengan tahap kepakaran atau perspektif tertentu.

### Prompt Templates

Cipta prompt boleh guna semula dengan tempat letak pembolehubah. Daripada menulis prompt baru setiap kali, tetapkan template sekali dan isikan dengan nilai berbeza. Kelas `PromptTemplate` LangChain4j memudahkan ini dengan sintaks `{{variable}}`.

<img src="../../../translated_images/ms/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompt boleh guna semula dengan tempat letak pembolehubah — satu template, banyak kegunaan*

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

**Bila untuk digunakan:** Pertanyaan berulang dengan input berbeza, pemprosesan secara berkelompok, membina aliran kerja AI boleh guna semula, atau mana-mana senario di mana struktur prompt kekal sama namun data berubah.

---

Lima asas ini memberikan anda set alat mantap untuk kebanyakan tugasan prompting. Selepas ini modul membina ke atas mereka dengan **lapan corak lanjutan** yang memanfaatkan kawalan penalaran GPT-5.2, penilaian kendiri, dan keupayaan output berstruktur.

## Corak Lanjutan

Setelah asas diliputi, mari kita terus ke lapan corak lanjutan yang menjadikan modul ini unik. Tidak semua masalah memerlukan pendekatan sama. Sesetengah soalan memerlukan jawapan cepat, yang lain memerlukan pemikiran mendalam. Ada yang perlu penalaran kelihatan, ada yang cuma perlukan keputusan. Setiap corak di bawah dioptimumkan untuk senario berbeza — dan kawalan penalaran GPT-5.2 menjadikan perbezaan lebih ketara.

<img src="../../../translated_images/ms/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Lapan Corak Prompting" width="800"/>

*Gambaran keseluruhan lapan corak kejuruteraan prompt dan kes penggunaannya*

<img src="../../../translated_images/ms/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kawalan Penalaran dengan GPT-5.2" width="800"/>

*Kawalan penalaran GPT-5.2 membolehkan anda menentukan berapa banyak pemikiran model harus lakukan — daripada jawapan pantas dan langsung hingga penerokaan mendalam*

**Kerajinan Rendah (Pantas & Fokus)** - Untuk soalan mudah di mana anda mahukan jawapan pantas dan langsung. Model buat penalaran minimum - maksimum 2 langkah. Gunakan ini untuk pengiraan, carian, atau soalan langsung.

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

> 💡 **Terokai dengan GitHub Copilot:** Buka [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) dan tanya:
> - "Apakah perbezaan antara corak prompting kerajinan rendah dan tinggi?"
> - "Bagaimana tag XML dalam prompt membantu menyusun jawapan AI?"
> - "Bilakah saya perlu menggunakan corak refleksi kendiri vs arahan langsung?"

**Kerajinan Tinggi (Mendalam & Teliti)** - Untuk masalah kompleks yang memerlukan analisis menyeluruh. Model meneroka secara lengkap dan menunjukkan penalaran terperinci. Gunakan ini untuk reka bentuk sistem, keputusan seni bina, atau penyelidikan kompleks.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Pelaksanaan Tugasan (Kemajuan Langkah demi Langkah)** - Untuk aliran kerja multi-langkah. Model menyatakan pelan terlebih dahulu, menerangkan setiap langkah semasa bekerja, kemudian memberikan ringkasan. Gunakan ini untuk migrasi, pelaksanaan, atau mana-mana proses multi-langkah.

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

Chain-of-Thought prompting secara eksplisit meminta model menunjukkan proses penalarannya, meningkatkan ketepatan untuk tugasan kompleks. Pecahan langkah demi langkah membantu manusia dan AI memahami logik.

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Tanya tentang corak ini:
> - "Bagaimana saya boleh menyesuaikan corak pelaksanaan tugasan untuk operasi jangka panjang?"
> - "Apakah amalan terbaik untuk menyusun prakata alat dalam aplikasi produksi?"
> - "Bagaimana saya boleh menangkap dan memaparkan kemas kini kemajuan antara langkah dalam UI?"

<img src="../../../translated_images/ms/task-execution-pattern.9da3967750ab5c1e.webp" alt="Corak Pelaksanaan Tugasan" width="800"/>

*Rangka kerja Rancang → Laksana → Rumus untuk tugasan multi-langkah*

**Kod Refleksi Diri** - Untuk menjana kod berkualiti produksi. Model menjana kod mengikut piawaian produksi dengan pengendalian ralat yang betul. Gunakan ini apabila membina ciri atau perkhidmatan baru.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ms/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Kitaran Refleksi Diri" width="800"/>

*Kitaran penambahbaikan berulang - jana, nilaikan, kenal pasti isu, perbaiki, ulang*

**Analisis Berstruktur** - Untuk penilaian konsisten. Model menyemak kod menggunakan rangka kerja tetap (ketepatan, amalan, prestasi, keselamatan, kemampuan penyelenggaraan). Gunakan ini untuk semakan kod atau penilaian kualiti.

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

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Tanya tentang analisis berstruktur:
> - "Bagaimana saya boleh sesuaikan rangka kerja analisis untuk jenis semakan kod berbeza?"
> - "Apakah cara terbaik untuk mengurai dan bertindak atas output berstruktur secara program?"
> - "Bagaimana saya memastikan tahap keterukan konsisten dalam sesi semakan berbeza?"

<img src="../../../translated_images/ms/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Corak Analisis Berstruktur" width="800"/>

*Rangka kerja untuk semakan kod konsisten dengan tahap keterukan*

**Sembang Multi-Pusingan** - Untuk perbualan yang memerlukan konteks. Model mengingati mesej sebelumnya dan membina daripadanya. Gunakan ini untuk sesi bantuan interaktif atau soalan dan jawapan kompleks.

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

*Bagaimana konteks perbualan terkumpul dalam banyak pusingan sehingga mencapai had token*

**Penalaran Langkah demi Langkah** - Untuk masalah yang memerlukan logik kelihatan. Model menunjukkan penalaran eksplisit untuk setiap langkah. Gunakan ini untuk masalah matematik, teka-teki logik, atau apabila anda perlu faham proses pemikiran.

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

*Memecahkan masalah ke dalam langkah logik eksplisit*

**Output Terhad** - Untuk jawapan dengan keperluan format khusus. Model mematuhi secara ketat peraturan format dan panjang. Gunakan ini untuk ringkasan atau apabila anda perlukan struktur output tepat.

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

*Menguatkuasakan keperluan format, panjang, dan struktur khusus*

## Menggunakan Sumber Azure Sedia Ada

**Sahkan deployment:**

Pastikan fail `.env` wujud dalam direktori root dengan kelayakan Azure (dicipta semasa Modul 01):
```bash
cat ../.env  # Perlu menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Mulakan aplikasi:**

> **Nota:** Jika anda sudah mula semua aplikasi menggunakan `./start-all.sh` dari Modul 01, modul ini sudah berjalan di port 8083. Anda boleh langkau arahan mula di bawah dan terus ke http://localhost:8083.
**Pilihan 1: Menggunakan Spring Boot Dashboard (Disyorkan untuk pengguna VS Code)**

Bekas pembangunan termasuk sambungan Spring Boot Dashboard, yang menyediakan antara muka visual untuk menguruskan semua aplikasi Spring Boot. Anda boleh menemuinya di Bar Aktiviti di sebelah kiri VS Code (cari ikon Spring Boot).

Dari Spring Boot Dashboard, anda boleh:
- Lihat semua aplikasi Spring Boot yang tersedia dalam ruang kerja
- Mulakan/hentikan aplikasi dengan satu klik
- Lihat log aplikasi secara masa nyata
- Monitor status aplikasi

Cuma klik butang main di sebelah "prompt-engineering" untuk memulakan modul ini, atau mulakan semua modul sekali gus.

<img src="../../../translated_images/ms/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

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
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

Kedua-dua skrip secara automatik memuatkan pembolehubah persekitaran dari fail `.env` akar dan akan membina JAR jika belum wujud.

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

**Untuk menghentikan:**

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

<img src="../../../translated_images/ms/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Papan pemuka utama yang menunjukkan semua 8 pola kejuruteraan prompt dengan ciri-ciri dan kes penggunaan mereka*

## Meneroka Pola-pola

Antara muka web membolehkan anda mencuba strategi menyoal berbeza. Setiap pola menyelesaikan masalah berlainan - cuba mereka untuk melihat bila setiap pendekatan bersinar.

> **Nota: Penstriman vs Tidak Penstriman** — Setiap halaman pola menawarkan dua butang: **🔴 Respon Penstriman (Langsung)** dan pilihan **Tidak Penstriman**. Penstriman menggunakan Server-Sent Events (SSE) untuk memaparkan token secara masa nyata semasa model menghasilkan, jadi anda dapat melihat kemajuan serta-merta. Pilihan tidak penstriman menunggu keseluruhan respons sebelum memaparkannya. Untuk prompt yang mencetuskan pemikiran mendalam (contoh, High Eagerness, Kod Reflektif Diri), panggilan tidak penstriman boleh mengambil masa yang sangat lama — kadang-kadang minit — tanpa maklum balas yang kelihatan. **Gunakan penstriman apabila mencuba prompt kompleks** supaya anda boleh melihat model berfungsi dan mengelakkan andaian bahawa permintaan telah tamat masa.
>
> **Nota: Keperluan Pelayar** — Ciri penstriman menggunakan Fetch Streams API (`response.body.getReader()`) yang memerlukan pelayar penuh (Chrome, Edge, Firefox, Safari). Ia **tidak** berfungsi dalam Simple Browser terbina dalam VS Code, kerana webview-nya tidak menyokong ReadableStream API. Jika anda menggunakan Simple Browser, butang tidak penstriman masih berfungsi seperti biasa — hanya butang penstriman terjejas. Buka `http://localhost:8083` dalam pelayar luar untuk pengalaman penuh.

### Eagerness Rendah vs Tinggi

Tanya soalan mudah seperti "Apakah 15% dari 200?" menggunakan Eagerness Rendah. Anda akan dapat jawapan segera dan terus. Kini tanya sesuatu yang kompleks seperti "Reka strategi caching untuk API trafik tinggi" menggunakan Eagerness Tinggi. Klik **🔴 Respon Penstriman (Langsung)** dan saksikan pemikiran terperinci model muncul token demi token. Model sama, struktur soalan sama - tetapi prompt memberitahu model berapa banyak pemikiran yang perlu dibuat.

### Pelaksanaan Tugasan (Preambul Alat)

Proses kerja berbilang langkah mendapat manfaat daripada perancangan awal dan narasi kemajuan. Model menggariskan apa yang akan dilakukan, menceritakan setiap langkah, kemudian meringkaskan hasil.

### Kod Reflektif Diri

Cuba "Buat perkhidmatan pengesahan e-mel". Daripada hanya menghasilkan kod dan berhenti, model menghasilkan, menilai mengikut kriteria kualiti, mengenal pasti kelemahan, dan memperbaiki. Anda akan melihat ia ulang sehingga kod memenuhi piawaian pengeluaran.

### Analisis Berstruktur

Semakan kod memerlukan kerangka penilaian konsisten. Model menganalisis kod menggunakan kategori tetap (ketepatan, amalan, prestasi, keselamatan) dengan tahap keterukan.

### Sembang Berbilang Giliran

Tanya "Apakah Spring Boot?" kemudian teruskan dengan "Tunjukkan saya satu contoh". Model mengingati soalan pertama anda dan memberikan contoh Spring Boot khusus. Tanpa ingatan, soalan kedua itu terlalu samar.

### Pemikiran Langkah-demi-Langkah

Pilih masalah matematik dan cuba dengan Pemikiran Langkah-demi-Langkah dan Eagerness Rendah. Eagerness rendah cuma beri jawapan - cepat tetapi tidak jelas. Langkah demi langkah tunjukkan setiap pengiraan dan keputusan.

### Output Terhad

Apabila anda perlukan format atau bilangan perkataan tertentu, pola ini menguatkuasakan pematuhan ketat. Cuba hasilkan ringkasan dengan tepat 100 perkataan dalam format titik peluru.

## Apa Yang Sebenarnya Anda Pelajari

**Usaha Pemikiran Mengubah Segalanya**

GPT-5.2 membolehkan anda kawal usaha pengiraan melalui prompt anda. Usaha rendah bermakna respons cepat dengan eksplorasi minima. Usaha tinggi bermakna model mengambil masa untuk berfikir dengan mendalam. Anda belajar memadankan usaha dengan kerumitan tugasan - jangan bazir masa pada soalan mudah, tapi jangan buru-buru buat keputusan rumit juga.

**Struktur Membimbing Tingkah Laku**

Perasan tag XML dalam prompt? Ia bukan hiasan. Model lebih boleh dipercayai mengikuti arahan berstruktur daripada teks bebas. Bila anda perlukan proses berbilang langkah atau logik kompleks, struktur membantu model jejak di mana ia berada dan apa yang seterusnya.

<img src="../../../translated_images/ms/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi prompt berstruktur baik dengan bahagian jelas dan organisasi gaya XML*

**Kualiti Melalui Penilaian Kendiri**

Pola refleksi kendiri berfungsi dengan menjelaskan kriteria kualiti. Daripada harap model "buat dengan betul", anda beritahu dengan tepat apa maksud "betul": logik tepat, pengendalian ralat, prestasi, keselamatan. Model kemudian boleh nilai hasil sendiri dan perbaiki. Ini mengubah generasi kod dari loteri ke proses.

**Konteks Ada Had**

Perbualan berbilang giliran berfungsi dengan memasukkan sejarah mesej setiap permintaan. Tapi ada had - setiap model ada had token maksimum. Bila perbualan bertambah, anda perlukan strategi untuk kekalkan konteks relevan tanpa melebihi had itu. Modul ini tunjukkan cara ingatan berfungsi; kemudian anda akan belajar bila nak ringkaskan, bila nak terlupa, dan bila nak ambil semula.

## Langkah Seterusnya

**Modul Seterusnya:** [03-rag - RAG (Penjanaan Diperkaya Pengambilan)](../03-rag/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 01 - Pengenalan](../01-introduction/README.md) | [Kembali ke Utama](../README.md) | [Seterusnya: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila ambil maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber utama. Untuk maklumat penting, terjemahan profesional oleh manusia adalah disyorkan. Kami tidak bertanggungjawab terhadap sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->