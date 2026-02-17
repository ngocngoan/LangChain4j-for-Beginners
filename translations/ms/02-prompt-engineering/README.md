# Modul 02: Kejuruteraan Prompt dengan GPT-5.2

## Jadual Kandungan

- [Apa yang Anda Akan Pelajari](../../../02-prompt-engineering)
- [Prasyarat](../../../02-prompt-engineering)
- [Memahami Kejuruteraan Prompt](../../../02-prompt-engineering)
- [Asas Kejuruteraan Prompt](../../../02-prompt-engineering)
  - [Prompt Tanpa Contoh (Zero-Shot)](../../../02-prompt-engineering)
  - [Prompt Dengan Beberapa Contoh (Few-Shot)](../../../02-prompt-engineering)
  - [Rantaian Fikiran (Chain of Thought)](../../../02-prompt-engineering)
  - [Prompt Berdasarkan Peranan](../../../02-prompt-engineering)
  - [Templat Prompt](../../../02-prompt-engineering)
- [Corak Lanjutan](../../../02-prompt-engineering)
- [Menggunakan Sumber Azure Yang Sedia Ada](../../../02-prompt-engineering)
- [Tangkapan Skrin Aplikasi](../../../02-prompt-engineering)
- [Meneroka Corak](../../../02-prompt-engineering)
  - [Semangat Rendah vs Tinggi](../../../02-prompt-engineering)
  - [Pelaksanaan Tugas (Preambul Alat)](../../../02-prompt-engineering)
  - [Kod Refleksi Diri](../../../02-prompt-engineering)
  - [Analisis Terstruktur](../../../02-prompt-engineering)
  - [Sembang Berbilang Pusingan](../../../02-prompt-engineering)
  - [Penalaran Langkah demi Langkah](../../../02-prompt-engineering)
  - [Output Terhad](../../../02-prompt-engineering)
- [Apa yang Sebenarnya Anda Pelajari](../../../02-prompt-engineering)
- [Langkah Seterusnya](../../../02-prompt-engineering)

## Apa yang Anda Akan Pelajari

<img src="../../../translated_images/ms/what-youll-learn.c68269ac048503b2.webp" alt="Apa yang Anda Akan Pelajari" width="800"/>

Dalam modul sebelumnya, anda telah melihat bagaimana memori membolehkan AI perbualan dan menggunakan Model GitHub untuk interaksi asas. Kini kita akan menumpukan pada cara anda mengajukan soalan — prompt itu sendiri — menggunakan GPT-5.2 dari Azure OpenAI. Cara anda menyusun prompt sangat mempengaruhi kualiti jawapan yang anda terima. Kita mulakan dengan ulasan teknik-teknik asas prompting, kemudian beralih ke lapan corak lanjutan yang memanfaatkan sepenuhnya keupayaan GPT-5.2.

Kita menggunakan GPT-5.2 kerana ia memperkenalkan kawalan penalaran - anda boleh memberitahu model berapa banyak pemikiran yang perlu dilakukan sebelum menjawab. Ini menjadikan pelbagai strategi prompting lebih jelas dan membantu anda memahami bila harus menggunakan setiap pendekatan. Kita juga akan mendapat manfaat dari had kadar yang lebih rendah pada GPT-5.2 di Azure berbanding Model GitHub.

## Prasyarat

- Menyelesaikan Modul 01 (sumber Azure OpenAI telah disediakan)
- Fail `.env` dalam direktori akar dengan kelayakan Azure (dibuat oleh `azd up` dalam Modul 01)

> **Nota:** Jika anda belum menyelesaikan Modul 01, ikut arahan penyediaan di sana dahulu.

## Memahami Kejuruteraan Prompt

<img src="../../../translated_images/ms/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Apa itu Kejuruteraan Prompt?" width="800"/>

Kejuruteraan prompt adalah tentang mereka bentuk teks input yang sentiasa memberikan hasil yang anda perlukan. Ia bukan hanya tentang bertanya soalan - ia tentang menyusun permintaan supaya model faham dengan tepat apa yang anda mahu dan cara untuk menyampaikannya.

Fikirkan ia seperti memberi arahan kepada rakan sekerja. "Betulkan bug" adalah tidak jelas. "Betulkan pengecualian pointer null di UserService.java baris 45 dengan menambah pemeriksaan null" adalah spesifik. Model bahasa berfungsi dengan cara yang sama - kepastian dan struktur sangat penting.

<img src="../../../translated_images/ms/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Bagaimana LangChain4j Sesuai" width="800"/>

LangChain4j menyediakan infrastruktur — sambungan model, memori, dan jenis mesej — manakala corak prompt hanyalah teks yang disusun rapi yang anda hantar melalui infrastruktur itu. Blok binaan utama adalah `SystemMessage` (yang menetapkan tingkah laku dan peranan AI) dan `UserMessage` (yang membawa permintaan sebenar anda).

## Asas Kejuruteraan Prompt

<img src="../../../translated_images/ms/five-patterns-overview.160f35045ffd2a94.webp" alt="Gambaran Keseluruhan Lima Corak Kejuruteraan Prompt" width="800"/>

Sebelum menyelami corak lanjutan dalam modul ini, mari kita ulas lima teknik prompting asas. Ini adalah blok binaan yang perlu diketahui oleh setiap jurutera prompt. Jika anda telah menjalani [modul Permulaan Pantas](../00-quick-start/README.md#2-prompt-patterns), anda telah melihat ini beraksi — ini adalah rangka kerja konseptual di sebaliknya.

### Prompt Tanpa Contoh (Zero-Shot)

Pendekatan paling mudah: berikan arahan langsung kepada model tanpa contoh. Model bergantung sepenuhnya pada latihan untuk memahami dan melaksanakan tugasan. Ini berkesan untuk permintaan yang mudah di mana tingkah laku yang dijangka jelas.

<img src="../../../translated_images/ms/zero-shot-prompting.7abc24228be84e6c.webp" alt="Prompt Tanpa Contoh" width="800"/>

*Arahan langsung tanpa contoh — model membuat inferens tugas daripada arahan sahaja*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Respons: "Positif"
```

**Bila guna:** Klasifikasi mudah, soalan langsung, terjemahan, atau apa saja tugasan yang boleh model kendali tanpa panduan tambahan.

### Prompt Dengan Beberapa Contoh (Few-Shot)

Berikan contoh yang menunjukkan corak yang anda mahu model ikuti. Model belajar format input-output yang dijangka dari contoh anda dan menggunakannya untuk input baru. Ini secara dramatik meningkatkan konsistensi untuk tugasan di mana format atau tingkah laku yang diingini tidak jelas.

<img src="../../../translated_images/ms/few-shot-prompting.9d9eace1da88989a.webp" alt="Prompt Dengan Beberapa Contoh" width="800"/>

*Belajar dari contoh — model mengenal pasti corak dan menggunakannya pada input baru*

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

**Bila guna:** Klasifikasi tersuai, format konsisten, tugasan khusus domain, atau apabila hasil zero-shot tidak konsisten.

### Rantaian Fikiran (Chain of Thought)

Minta model tunjukkan penalaran langkah demi langkah. Daripada terus memberi jawapan, model memecahkan masalah dan mengerjakan setiap bahagian secara eksplisit. Ini meningkatkan ketepatan untuk tugasan matematik, logik, dan penalaran berbilang langkah.

<img src="../../../translated_images/ms/chain-of-thought.5cff6630e2657e2a.webp" alt="Prompt Rantaian Fikiran" width="800"/>

*Penalaran langkah demi langkah — memecahkan masalah kompleks kepada langkah logik yang jelas*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model menunjukkan: 15 - 8 = 7, kemudian 7 + 12 = 19 epal
```

**Bila guna:** Masalah matematik, teka-teki logik, penyahpepijatan, atau tugasan yang penunjukan proses penalaran meningkatkan ketepatan dan kepercayaan.

### Prompt Berdasarkan Peranan

Tetapkan persona atau peranan untuk AI sebelum bertanya soalan anda. Ini menyediakan konteks yang membentuk nada, kedalaman, dan fokus jawapan. "Arkitek perisian" memberi nasihat berbeza daripada "pembangun junior" atau "juru audit keselamatan".

<img src="../../../translated_images/ms/role-based-prompting.a806e1a73de6e3a4.webp" alt="Prompt Berdasarkan Peranan" width="800"/>

*Menetapkan konteks dan persona — soalan sama mendapat jawapan berbeza mengikut peranan yang diberikan*

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

**Bila guna:** Semakan kod, bimbingan, analisis khusus domain, atau apabila anda perlukan jawapan disesuaikan mengikut tahap kepakaran atau perspektif tertentu.

### Templat Prompt

Buat prompt boleh guna semula dengan pemegang tempat pembolehubah. Daripada menulis prompt baru setiap kali, tentukan templat sekali dan isikan nilai berbeza. Kelas `PromptTemplate` LangChain4j memudahkan ini dengan sintaks `{{variable}}`.

<img src="../../../translated_images/ms/prompt-templates.14bfc37d45f1a933.webp" alt="Templat Prompt" width="800"/>

*Prompt boleh guna semula dengan pemegang tempat pembolehubah — satu templat, banyak kegunaan*

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

**Bila guna:** Pertanyaan berulang dengan input berbeza, proses batch, membina aliran kerja AI boleh guna semula, atau apa saja senario di mana struktur prompt kekal sama tapi data berubah.

---

Lima asas ini memberi anda set alat kukuh untuk kebanyakan tugasan prompting. Selebihnya modul ini dibina di atasnya dengan **lapan corak lanjutan** yang memanfaatkan kawalan penalaran GPT-5.2, penilaian sendiri, dan keupayaan output terstruktur.

## Corak Lanjutan

Setelah asas dikupas, mari beralih ke lapan corak lanjutan yang menjadikan modul ini unik. Tidak semua masalah memerlukan pendekatan sama. Ada soalan perlukan jawapan cepat, ada yang perlukan pemikiran mendalam. Ada yang perlukan penalaran kelihatan, ada yang hanya perlukan hasil. Setiap corak di bawah dioptimumkan untuk senario tertentu — dan kawalan penalaran GPT-5.2 menjadikan perbezaan lebih ketara.

<img src="../../../translated_images/ms/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Lapan Corak Prompt Engineering" width="800"/>

*Gambaran keseluruhan lapan corak kejuruteraan prompt dan kes penggunaannya*

<img src="../../../translated_images/ms/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kawalan Penalaran dengan GPT-5.2" width="800"/>

*Kawalan penalaran GPT-5.2 membolehkan anda tentukan berapa banyak model perlu berfikir — dari jawapan cepat terus ke eksplorasi mendalam*

<img src="../../../translated_images/ms/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Perbandingan Usaha Penalaran" width="800"/>

*Semangat rendah (cepat, langsung) vs Semangat tinggi (teliti, eksploratori) dalam pendekatan penalaran*

**Semangat Rendah (Cepat & Fokus)** - Untuk soalan mudah yang anda mahu jawapan cepat dan langsung. Model melakukan penalaran minimum - maksimum 2 langkah. Guna ini untuk pengiraan, carian, atau soalan langsung.

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

> 💡 **Jelajah dengan GitHub Copilot:** Buka [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) dan tanya:
> - "Apa perbezaan antara corak prompting semangat rendah dan semangat tinggi?"
> - "Bagaimana tag XML dalam prompt membantu menyusun jawapan AI?"
> - "Bila saya harus guna corak refleksi diri vs arahan langsung?"

**Semangat Tinggi (Mendalam & Teliti)** - Untuk masalah kompleks yang anda mahu analisis menyeluruh. Model meneroka dengan teliti dan tunjukkan penalaran terperinci. Guna ini untuk reka bentuk sistem, keputusan seni bina, atau penyelidikan kompleks.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Pelaksanaan Tugas (Kemajuan Langkah demi Langkah)** - Untuk aliran kerja berbilang langkah. Model menyediakan rancangan awal, menceritakan setiap langkah semasa berfungsi, kemudian beri ringkasan. Guna ini untuk migrasi, pelaksanaan, atau proses berbilang langkah.

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

Prompt Rantaian Fikiran secara eksplisit meminta model tunjukkan proses penalarannya, meningkatkan ketepatan untuk tugasan kompleks. Pecahan langkah demi langkah membantu manusia dan AI faham logik.

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Tanya mengenai corak ini:
> - "Bagaimana saya boleh sesuaikan corak pelaksanaan tugas untuk operasi berjangka panjang?"
> - "Apakah amalan terbaik untuk menyusun preambul alat dalam aplikasi produksi?"
> - "Bagaimana saya tangkap dan paparkan kemas kini kemajuan sederhana dalam UI?"

<img src="../../../translated_images/ms/task-execution-pattern.9da3967750ab5c1e.webp" alt="Corak Pelaksanaan Tugas" width="800"/>

*Rancangan → Laksana → Ringkasan aliran kerja untuk tugasan berbilang langkah*

**Kod Refleksi Diri** - Untuk menghasilkan kod berkualiti produksi. Model menjana kod mengikut piawaian produksi dengan pengendalian ralat yang betul. Guna ini semasa membina ciri atau perkhidmatan baru.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ms/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Kitaran Refleksi Diri" width="800"/>

*kitaran penambahbaikan iteratif - jana, nilaikan, kenal pasti isu, perbaiki, ulang*

**Analisis Terstruktur** - Untuk penilaian konsisten. Model menyemak kod menggunakan rangka kerja tetap (ketepatan, amalan, prestasi, keselamatan, kebolehpeliharaan). Guna ini untuk semakan kod atau penilaian kualiti.

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

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Tanya tentang analisis terstruktur:
> - "Bagaimana saya boleh sesuaikan rangka kerja analisis untuk jenis semakan kod berbeza?"
> - "Apakah cara terbaik untuk mengurai dan bertindak atas output terstruktur secara programatik?"
> - "Bagaimana saya pastikan tahap keterukan konsisten merentas sesi semakan berbeza?"

<img src="../../../translated_images/ms/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Corak Analisis Terstruktur" width="800"/>

*Rangka kerja untuk semakan kod konsisten dengan tahap keterukan*

**Sembang Berbilang Pusingan** - Untuk perbualan yang memerlukan konteks. Model ingat mesej sebelumnya dan bina berdasarkan itu. Guna ini untuk sesi bantuan interaktif atau soal jawab kompleks.

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

*Bagaimana konteks perbualan terkumpul sepanjang pusingan sehingga mencapai had token*

**Penalaran Langkah demi Langkah** - Untuk masalah yang memerlukan logik kelihatan. Model tunjukkan penalaran eksplisit untuk setiap langkah. Guna ini untuk masalah matematik, teka-teki logik, atau bila anda mahu faham proses pemikiran.

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

*Memecahkan masalah kepada langkah logik yang jelas*

**Output Terhad** - Untuk jawapan dengan keperluan format khusus. Model mematuhi dengan ketat format dan peraturan panjang. Guna ini untuk ringkasan atau bila anda perlukan struktur output tepat.

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

*Memastikan keperluan format, panjang, dan struktur tertentu dipenuhi*

## Menggunakan Sumber Azure Yang Sedia Ada

**Sahkan penyediaan:**

Pastikan fail `.env` wujud di direktori akar dengan kelayakan Azure (dibuat semasa Modul 01):
```bash
cat ../.env  # Perlu menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Mula aplikasi:**

> **Nota:** Jika anda sudah mula semua aplikasi menggunakan `./start-all.sh` dari Modul 01, modul ini sudah berjalan pada port 8083. Anda boleh langkau arahan mula di bawah dan terus pergi ke http://localhost:8083.

**Pilihan 1: Menggunakan Spring Boot Dashboard (Disarankan untuk pengguna VS Code)**

Kontena dev termasuk sambungan Spring Boot Dashboard, yang menyediakan antara muka visual untuk mengurus semua aplikasi Spring Boot. Anda boleh menjumpainya di Bar Aktiviti di sebelah kiri VS Code (cari ikon Spring Boot).
Daripada Papan Pemuka Spring Boot, anda boleh:
- Melihat semua aplikasi Spring Boot yang tersedia dalam ruang kerja
- Mulakan/hentikan aplikasi dengan satu klik
- Lihat log aplikasi secara masa nyata
- Pantau status aplikasi

Klik sahaja butang main di sebelah "prompt-engineering" untuk memulakan modul ini, atau mulakan semua modul sekaligus.

<img src="../../../translated_images/ms/dashboard.da2c2130c904aaf0.webp" alt="Papan Pemuka Spring Boot" width="400"/>

**Pilihan 2: Menggunakan skrip shell**

Mulakan semua aplikasi web (modul 01-04):

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

Kedua-dua skrip secara automatik memuatkan pembolehubah persekitaran daripada fail `.env` akar dan akan membina JAR jika ia tidak wujud.

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

## Tangkapan Skrin Aplikasi

<img src="../../../translated_images/ms/dashboard-home.5444dbda4bc1f79d.webp" alt="Laman Utama Papan Pemuka" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Papan pemuka utama yang menunjukkan semua 8 corak kejuruteraan prompt dengan ciri-ciri dan kes penggunaan mereka*

## Meneroka Corak

Antara muka web membolehkan anda mencuba pelbagai strategi pengepromptan. Setiap corak menyelesaikan masalah yang berlainan - cuba mereka untuk melihat bila setiap pendekatan bersinar.

### Kerajinan Rendah vs Tinggi

Tanya soalan mudah seperti "Apakah 15% daripada 200?" menggunakan Kerajinan Rendah. Anda akan mendapat jawapan yang segera dan terus. Sekarang tanya sesuatu yang kompleks seperti "Rancang strategi caching untuk API trafik tinggi" menggunakan Kerajinan Tinggi. Perhatikan bagaimana model melambat dan memberikan alasan yang terperinci. Model yang sama, struktur soalan yang sama - tetapi prompt memberitahu berapa banyak pemikiran yang perlu dilakukan.

<img src="../../../translated_images/ms/low-eagerness-demo.898894591fb23aa0.webp" alt="Demo Kerajinan Rendah" width="800"/>

*Pengiraan pantas dengan alasan minimum*

<img src="../../../translated_images/ms/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Demo Kerajinan Tinggi" width="800"/>

*Strategi caching yang komprehensif (2.8MB)*

### Pelaksanaan Tugas (Preambul Alat)

Aliran kerja berbilang langkah mendapat manfaat daripada perancangan awal dan narasi kemajuan. Model menggariskan apa yang akan dilakukan, menceritakan setiap langkah, kemudian merumuskan hasil.

<img src="../../../translated_images/ms/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Demo Pelaksanaan Tugas" width="800"/>

*Mencipta endpoint REST dengan narasi langkah demi langkah (3.9MB)*

### Kod Refleksi Diri

Cuba "Cipta perkhidmatan pengesahan email". Daripada hanya menjana kod dan berhenti, model menghasilkan, menilai berdasarkan kriteria kualiti, mengenal pasti kelemahan, dan memperbaiki. Anda akan melihat ia ulang sehingga kod memenuhi standard pengeluaran.

<img src="../../../translated_images/ms/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Demo Kod Refleksi Diri" width="800"/>

*Perkhidmatan pengesahan email lengkap (5.2MB)*

### Analisis Berstruktur

Semakan kod memerlukan rangka kerja penilaian yang konsisten. Model menganalisis kod menggunakan kategori tetap (ketepatan, amalan, prestasi, keselamatan) dengan tahap keterukan.

<img src="../../../translated_images/ms/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Demo Analisis Berstruktur" width="800"/>

*Semakan kod berasaskan rangka kerja*

### Perbualan Berbilang Giliran

Tanya "Apakah Spring Boot?" kemudian teruskan dengan "Tunjukkan saya contoh". Model mengingati soalan pertama anda dan memberi contoh Spring Boot yang khusus. Tanpa memori, soalan kedua itu terlalu kabur.

<img src="../../../translated_images/ms/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Demo Perbualan Berbilang Giliran" width="800"/>

*Pemeliharaan konteks merentasi soalan*

### Penalaran Langkah demi Langkah

Pilih masalah matematik dan cuba dengan kedua-dua Penalaran Langkah demi Langkah dan Kerajinan Rendah. Kerajinan rendah hanya memberikan jawapan - pantas tetapi samar. Langkah demi langkah menunjukkan setiap pengiraan dan keputusan.

<img src="../../../translated_images/ms/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Demo Penalaran Langkah demi Langkah" width="800"/>

*Masalah matematik dengan langkah yang jelas*

### Output Terhad

Apabila anda memerlukan format tertentu atau bilangan perkataan, corak ini menguatkuasakan pematuhan ketat. Cuba jana ringkasan dengan tepat 100 perkataan dalam format titik peluru.

<img src="../../../translated_images/ms/constrained-output-demo.567cc45b75da1633.webp" alt="Demo Output Terhad" width="800"/>

*Ringkasan pembelajaran mesin dengan kawalan format*

## Apa Yang Anda Sebenarnya Pelajari

**Usaha Penalaran Mengubah Segalanya**

GPT-5.2 membolehkan anda mengawal usaha pengiraan melalui prompt anda. Usaha rendah bermakna respons pantas dengan eksplorasi minimum. Usaha tinggi bermakna model mengambil masa untuk berfikir secara mendalam. Anda belajar untuk memadankan usaha dengan kerumitan tugas - jangan bazirkan masa pada soalan mudah, tetapi jangan tergesa-gesa membuat keputusan kompleks juga.

**Struktur Membimbing Tingkah Laku**

Perasan tanda XML dalam prompt? Ia bukan hiasan. Model mengikuti arahan berstruktur dengan lebih boleh dipercayai daripada teks bebas. Apabila anda memerlukan proses berbilang langkah atau logik kompleks, struktur membantu model mengesan di mana ia berada dan apa yang akan datang.

<img src="../../../translated_images/ms/prompt-structure.a77763d63f4e2f89.webp" alt="Struktur Prompt" width="800"/>

*Anatomi prompt yang berstruktur baik dengan bahagian jelas dan organisasi gaya XML*

**Kualiti Melalui Penilaian Diri**

Corak refleksi diri berfungsi dengan menjadikan kriteria kualiti eksplisit. Daripada berharap model "melakukannya dengan betul", anda memberitahunya apa maksud "betul": logik tepat, pengendalian ralat, prestasi, keselamatan. Model kemudian boleh menilai output sendiri dan memperbaiki. Ini menjadikan penjanaan kod dari sebuah loteri menjadi proses.

**Konteks Itu Terhad**

Perbualan berbilang giliran berfungsi dengan memasukkan sejarah mesej dengan setiap permintaan. Tetapi terdapat had - setiap model mempunyai kiraan token maksimum. Apabila perbualan berkembang, anda memerlukan strategi untuk mengekalkan konteks relevan tanpa mencapai had itu. Modul ini menunjukkan bagaimana memori berfungsi; kemudian anda akan belajar bila untuk meringkaskan, bila untuk melupakan, dan bila untuk mengambil kembali.

## Langkah Seterusnya

**Modul Seterusnya:** [03-rag - RAG (Pembuatan Dipertingkatkan Pengambilan)](../03-rag/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 01 - Pengenalan](../01-introduction/README.md) | [Kembali ke Utama](../README.md) | [Seterusnya: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang sahih. Untuk maklumat kritikal, terjemahan profesional oleh manusia adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->