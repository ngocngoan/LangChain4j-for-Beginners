# Modul 02: Kejuruteraan Prompt dengan GPT-5.2

## Jadual Kandungan

- [Apa Yang Anda Akan Pelajari](../../../02-prompt-engineering)
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
- [Gambar Skrin Aplikasi](../../../02-prompt-engineering)
- [Meneroka Corak](../../../02-prompt-engineering)
  - [Rendah vs Tinggi Semangat](../../../02-prompt-engineering)
  - [Pelaksanaan Tugasan (Preamble Alat)](../../../02-prompt-engineering)
  - [Kod Bersifat Reflektif Sendiri](../../../02-prompt-engineering)
  - [Analisis Berstruktur](../../../02-prompt-engineering)
  - [Sembang Berbilang Giliran](../../../02-prompt-engineering)
  - [Penalaran Langkah demi Langkah](../../../02-prompt-engineering)
  - [Output Terhad](../../../02-prompt-engineering)
- [Apa Yang Anda Sebenarnya Pelajari](../../../02-prompt-engineering)
- [Langkah Seterusnya](../../../02-prompt-engineering)

## Apa Yang Anda Akan Pelajari

<img src="../../../translated_images/ms/what-youll-learn.c68269ac048503b2.webp" alt="Apa Yang Anda Akan Pelajari" width="800"/>

Dalam modul sebelum ini, anda melihat bagaimana memori membolehkan AI perbualan dan menggunakan Model GitHub untuk interaksi asas. Kini kita akan fokus pada cara anda mengajukan soalan — prompt itu sendiri — menggunakan GPT-5.2 dari Azure OpenAI. Cara anda menyusun prompt secara dramatik mempengaruhi kualiti jawapan yang anda peroleh. Kita bermula dengan ulasan teknik asas prompt, kemudian beralih ke lapan corak lanjutan yang memanfaatkan sepenuhnya kebolehan GPT-5.2.

Kita menggunakan GPT-5.2 kerana ia memperkenalkan kawalan penalaran - anda boleh memberitahu model berapa banyak pemikiran yang perlu dilakukan sebelum menjawab. Ini menjadikan strategi prompting berbeza lebih nyata dan membantu anda memahami bila menggunakan setiap pendekatan. Kita juga akan mendapat manfaat dari had kadar yang lebih rendah untuk GPT-5.2 berbanding Model GitHub di Azure.

## Prasyarat

- Menyelesaikan Modul 01 (Sumber Azure OpenAI telah dideploy)
- Fail `.env` di direktori root dengan kelayakan Azure (dicipta oleh `azd up` dalam Modul 01)

> **Nota:** Jika anda belum menyelesaikan Modul 01, ikut arahan deployment di sana terlebih dahulu.

## Memahami Kejuruteraan Prompt

<img src="../../../translated_images/ms/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Apa Itu Kejuruteraan Prompt?" width="800"/>

Kejuruteraan prompt ialah tentang mereka bentuk teks input yang sentiasa memberikan hasil yang anda perlukan. Ia bukan hanya tentang mengajukan soalan - ia mengenai menyusun permintaan supaya model benar-benar faham apa yang anda mahu dan bagaimana untuk menyampaikannya.

Fikirkan seperti memberikan arahan kepada rakan sekerja. "Betulkan pepijat" adalah samar. "Betulkan pengecualian penunjuk null dalam UserService.java baris 45 dengan menambah pemeriksaan null" adalah spesifik. Model bahasa berfungsi sama - ketepatan dan struktur penting.

<img src="../../../translated_images/ms/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Bagaimana LangChain4j Sesuai" width="800"/>

LangChain4j menyediakan infrastruktur — sambungan model, memori, dan jenis mesej — sementara corak prompt hanyalah teks yang disusun dengan teliti yang anda hantar melalui infrastruktur itu. Blok binaan utama ialah `SystemMessage` (yang menetapkan tingkah laku dan peranan AI) dan `UserMessage` (yang membawa permintaan sebenar anda).

## Asas Kejuruteraan Prompt

<img src="../../../translated_images/ms/five-patterns-overview.160f35045ffd2a94.webp" alt="Lima Corak Asas Kejuruteraan Prompt" width="800"/>

Sebelum meneroka corak lanjutan dalam modul ini, mari kita ulas lima teknik prompting asas. Ini adalah blok binaan yang perlu diketahui setiap jurutera prompt. Jika anda sudah meneliti modul [Permulaan Pantas](../00-quick-start/README.md#2-prompt-patterns), anda telah melihat ini beraksi — berikut kerangka konsep di sebaliknya.

### Zero-Shot Prompting

Pendekatan paling mudah: berikan model arahan terus tanpa contoh. Model bergantung sepenuhnya pada latihan untuk memahami dan melaksanakan tugasan. Ini berkesan untuk permintaan yang sederhana dimana tingkah laku yang dijangka jelas.

<img src="../../../translated_images/ms/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Arahan terus tanpa contoh — model membuat inferens tugasan hanya dari arahan*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Respons: "Positif"
```

**Bilakah digunakan:** Klasifikasi mudah, soalan terus, terjemahan, atau apa-apa tugasan yang model boleh kendalikan tanpa panduan tambahan.

### Few-Shot Prompting

Berikan contoh yang menunjukkan corak yang anda mahu model ikuti. Model belajar format input-output yang dijangka dari contoh anda dan mengaplikasikannya pada input baru. Ini secara ketara meningkatkan konsistensi untuk tugasan dimana format atau tingkah laku yang dikehendaki tidak jelas.

<img src="../../../translated_images/ms/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

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

**Bilakah digunakan:** Klasifikasi khusus, format konsisten, tugasan domain khusus, atau apabila hasil zero-shot tidak konsisten.

### Chain of Thought

Minta model menunjukkan penalaran langkah demi langkah. Daripada terus ke jawapan, model memecahkan masalah dan mengerjakan setiap bahagian secara jelas. Ini memperbaiki ketepatan pada tugasan matematik, logik, dan penalaran berbilang langkah.

<img src="../../../translated_images/ms/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Penalaran langkah demi langkah — memecahkan masalah kompleks ke langkah logik yang jelas*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model menunjukkan: 15 - 8 = 7, kemudian 7 + 12 = 19 epal
```

**Bilakah digunakan:** Masalah matematik, teka-teki logik, pembaikan ralat, atau apa-apa tugasan dimana menunjukkan proses penalaran meningkatkan ketepatan dan kepercayaan.

### Role-Based Prompting

Tetapkan persona atau peranan untuk AI sebelum mengemukakan soalan anda. Ini memberikan konteks yang membentuk nada, kedalaman, dan fokus respons. Seorang "arkitek perisian" memberi nasihat berbeza daripada "pembangun junior" atau "juru-audit keselamatan".

<img src="../../../translated_images/ms/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Menetapkan konteks dan persona — soalan sama mendapat respons berbeza bergantung peranan ditetapkan*

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

**Bilakah digunakan:** Semakan kod, bimbingan, analisis domain khusus, atau apabila anda memerlukan respons yang disesuaikan dengan tahap kepakaran atau perspektif tertentu.

### Prompt Templates

Cipta prompt boleh guna semula dengan tempat letak pembolehubah. Daripada menulis prompt baru setiap kali, definisikan template sekali dan isi nilai berlainan. Kelas `PromptTemplate` LangChain4j memudahkan ini dengan sintaks `{{variable}}`.

<img src="../../../translated_images/ms/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompt boleh guna semula dengan tempat letak pembolehubah — satu template, banyak guna*

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

**Bilakah digunakan:** Pertanyaan berulang dengan input berlainan, pemprosesan batch, membina workflow AI boleh guna semula, atau apa-apa senario dimana struktur prompt kekal tapi data berubah.

---

Lima asas ini memberikan anda toolkit kukuh untuk kebanyakan tugasan prompting. Selebihnya modul ini membina ke atasnya dengan **lapan corak lanjutan** yang memanfaatkan kawalan penalaran, penilaian kendiri, dan kemampuan output berstruktur GPT-5.2.

## Corak Lanjutan

Setelah asas dibincangkan, mari beralih kepada lapan corak lanjutan yang menjadikan modul ini unik. Tidak semua masalah memerlukan pendekatan sama. Ada soalan memerlukan jawapan pantas, ada pula yang perlu pemikiran mendalam. Ada yang perlu penalaran yang kelihatan, ada yang hanya perlukan hasil. Setiap corak di bawah dioptimumkan untuk senario berbeza — dan kawalan penalaran GPT-5.2 menjadikan perbezaan lebih jelas.

<img src="../../../translated_images/ms/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Lapan Corak Prompting" width="800"/>

*Gambaran keseluruhan lapan corak kejuruteraan prompt dan kes penggunaannya*

<img src="../../../translated_images/ms/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kawalan Penalaran dengan GPT-5.2" width="800"/>

*Kawalan penalaran GPT-5.2 membolehkan anda tentukan berapa banyak pemikiran model perlu buat — dari jawapan terus pantas ke penerokaan mendalam*

**Semangat Rendah (Pantas & Fokus)** - Untuk soalan mudah di mana anda mahukan jawapan pantas dan langsung. Model membuat penalaran minimum - maksimum 2 langkah. Gunakan ini untuk pengiraan, carian, atau soalan mudah.

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
> - "Apa perbezaan antara corak prompting semangat rendah dan tinggi?"
> - "Bagaimana tag XML dalam prompt membantu susun respons AI?"
> - "Bila saya patut guna corak refleksi kendiri vs arahan langsung?"

**Semangat Tinggi (Mendalam & Teliti)** - Untuk masalah kompleks di mana anda mahukan analisis menyeluruh. Model meneroka dengan teliti dan menunjukkan penalaran terperinci. Gunakan ini untuk reka bentuk sistem, keputusan seni bina, atau kajian kompleks.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Pelaksanaan Tugasan (Kemajuan Langkah demi Langkah)** - Untuk workflow berbilang langkah. Model memberi rancangan awal, menceritakan setiap langkah ketika berkerja, lalu memberi ringkasan. Gunakan ini untuk migrasi, pelaksanaan, atau apa-apa proses berbilang langkah.

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

Prompt Chain-of-Thought secara eksplisit meminta model menunjukkan proses penalarannya, meningkatkan ketepatan untuk tugasan kompleks. Pecahan langkah demi langkah membantu manusia dan AI faham logik.

> **🤖 Cuba dengan Sembang [GitHub Copilot](https://github.com/features/copilot):** Tanyakan tentang corak ini:
> - "Bagaimana saya sesuaikan corak pelaksanaan tugasan untuk operasi jangka panjang?"
> - "Apa amalan terbaik untuk menyusun preamble alat dalam aplikasi produksi?"
> - "Bagaimana saya rakam dan paparkan kemas kini kemajuan pertengahan dalam UI?"

<img src="../../../translated_images/ms/task-execution-pattern.9da3967750ab5c1e.webp" alt="Corak Pelaksanaan Tugasan" width="800"/>

*Rancangan → Laksanakan → Ringkaskan workflow untuk tugas berbilang langkah*

**Kod Bersifat Reflektif Sendiri** - Untuk menjana kod berkualiti produksi. Model menghasilkan kod ikut piawaian produksi dengan pengendalian ralat sewajarnya. Gunakan ini apabila membina ciri atau perkhidmatan baru.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ms/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Kitaran Refleksi Diri" width="800"/>

*Kitaran peningkatan berulang - jana, nilai, kenal pasti isu, perbaiki, ulang*

**Analisis Berstruktur** - Untuk penilaian konsisten. Model mengkaji kod menggunakan kerangka tetap (ketepatan, amalan, prestasi, keselamatan, kebolehselenggaraan). Gunakan ini untuk semakan kod atau penilaian kualiti.

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
> - "Bagaimana nak sesuaikan kerangka analisis untuk jenis semakan kod berlainan?"
> - "Apakah cara terbaik untuk parse dan guna output berstruktur secara program?"
> - "Bagaimana saya pastikan tahap keterukan konsisten merentas sesi semakan berbeza?"

<img src="../../../translated_images/ms/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Corak Analisis Berstruktur" width="800"/>

*Kerangka untuk semakan kod konsisten dengan tahap keterukan*

**Sembang Berbilang Giliran** - Untuk perbualan yang memerlukan konteks. Model mengingati mesej sebelumnya dan membina daripadanya. Gunakan ini untuk sesi bantuan interaktif atau Q&A kompleks.

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

*Bagaimana konteks perbualan terkumpul sepanjang banyak giliran hingga cap token tercapai*

**Penalaran Langkah demi Langkah** - Untuk masalah yang memerlukan logik kelihatan. Model menunjuk penalaran jelas untuk setiap langkah. Gunakan ini untuk masalah matematik, teka-teki logik, atau bila anda mahu faham proses berfikir.

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

*Memecah masalah kepada langkah logik yang jelas*

**Output Terhad** - Untuk respons yang memerlukan keperluan format khusus. Model mengikuti ketat peraturan format dan panjang. Gunakan ini untuk ringkasan atau bila anda perlu struktur output tepat.

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

*Memaksa format, panjang, dan keperluan struktur tertentu*

## Menggunakan Sumber Azure Sedia Ada

**Sahkan deployment:**

Pastikan fail `.env` wujud di direktori root dengan kelayakan Azure (dicipta semasa Modul 01):
```bash
cat ../.env  # Patut menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, PENDEPLOYAN
```

**Mulakan aplikasi:**

> **Nota:** Jika anda sudah memulakan semua aplikasi menggunakan `./start-all.sh` dari Modul 01, modul ini sudah berjalan di port 8083. Anda boleh langkau arahan mula di bawah dan terus ke http://localhost:8083.

**Pilihan 1: Menggunakan Spring Boot Dashboard (Disyorkan untuk pengguna VS Code)**

Kontena pembangun merangkumi sambungan Spring Boot Dashboard, yang menyediakan antara muka visual untuk mengurus semua aplikasi Spring Boot. Anda boleh menjumpainya di Bar Aktiviti di sebelah kiri VS Code (carilah ikon Spring Boot).

Dari Spring Boot Dashboard, anda boleh:
- Melihat semua aplikasi Spring Boot yang tersedia dalam workspace
- Mula/hentikan aplikasi dengan satu klik
- Melihat log aplikasi secara masa nyata
- Memantau status aplikasi
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

Kedua-dua skrip secara automatik memuatkan pemboleh ubah persekitaran dari fail `.env` root dan akan membina JAR jika ia tidak wujud.

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

*Papan pemuka utama menunjukkan kesemua 8 corak kejuruteraan prompt dengan ciri-ciri dan kes penggunaannya*

## Meneroka Corak

Antara muka web membolehkan anda mencuba pelbagai strategi prompting. Setiap corak menyelesaikan masalah yang berbeza - cuba untuk melihat bila setiap pendekatan menyerlah.

> **Nota: Penstriman vs Bukan Penstriman** — Setiap halaman corak menawarkan dua butang: **🔴 Respon Penstriman (Langsung)** dan pilihan **Bukan penstriman**. Penstriman menggunakan Server-Sent Events (SSE) untuk memaparkan token secara masa nyata semasa model menjana, jadi anda nampak kemajuan serta-merta. Pilihan bukan penstriman menunggu keseluruhan respon sebelum memaparkannya. Untuk prompt yang memerlukan pemikiran mendalam (contoh: High Eagerness, Self-Reflecting Code), panggilan bukan penstriman boleh mengambil masa sangat lama — kadang-kadang minit — tanpa maklum balas yang kelihatan. **Gunakan penstriman apabila mencuba prompt yang kompleks** supaya anda boleh melihat model bekerja dan mengelakkan tanggapan permintaan telah tamat masa.
>
> **Nota: Keperluan Pelayar** — Ciri penstriman menggunakan Fetch Streams API (`response.body.getReader()`) yang memerlukan pelayar penuh (Chrome, Edge, Firefox, Safari). Ia **tidak** berfungsi dalam Simple Browser terbina dalam VS Code, kerana webviewnya tidak menyokong ReadableStream API. Jika anda menggunakan Simple Browser, butang bukan penstriman masih berfungsi seperti biasa — hanya butang penstriman yang terjejas. Buka `http://localhost:8083` dalam pelayar luar untuk pengalaman penuh.

### Low vs High Eagerness

Tanya soalan mudah seperti "Apakah 15% daripada 200?" menggunakan Low Eagerness. Anda akan dapat jawapan segera dan langsung. Sekarang tanya sesuatu yang kompleks seperti "Reka strategi caching untuk API trafik tinggi" menggunakan High Eagerness. Klik **🔴 Respon Penstriman (Langsung)** dan tonton penjelasan terperinci model muncul token demi token. Model yang sama, struktur soalan yang sama - tetapi prompt memberitahu berapa banyak pemikiran yang perlu dilakukan.

### Pelaksanaan Tugasan (Preambul Alat)

Aliran kerja berbilang langkah mendapat manfaat dari perancangan awal dan narasi kemajuan. Model menggariskan apa yang akan dilakukan, menceritakan setiap langkah, kemudian merumuskan hasil.

### Kod Refleksi Diri

Cuba "Cipta perkhidmatan pengesahan email". Bukannya hanya menjana kod dan berhenti, model menjana, menilai berdasarkan kriteria kualiti, mengenal pasti kelemahan, dan memperbaiki. Anda akan melihat ia mengulang sehingga kod memenuhi piawaian produksi.

### Analisis Berstruktur

Ulasan kod memerlukan kerangka penilaian yang konsisten. Model menganalisis kod menggunakan kategori tetap (ketepatan, amalan, prestasi, keselamatan) dengan tahap keterukan.

### Sembang Berbilang Giliran

Tanya "Apakah Spring Boot?" kemudian segera sambung dengan "Tunjukkan contoh". Model ingat soalan pertama anda dan memberikan contoh Spring Boot khusus. Tanpa ingatan, soalan kedua itu akan terlalu kabur.

### Penalaran Langkah demi Langkah

Pilih masalah matematik dan cuba dengan kedua-dua Penalaran Langkah demi Langkah dan Low Eagerness. Low eagerness hanya memberi jawapan - cepat tapi tidak jelas. Penalaran langkah demi langkah menunjukkan setiap pengiraan dan keputusan.

### Output Terhad

Apabila anda memerlukan format atau bilangan perkataan tertentu, corak ini menguatkuasakan pematuhan ketat. Cuba jana ringkasan dengan tepat 100 patah perkataan dalam format titik peluru.

## Apa Yang Anda Sebenarnya Pelajari

**Usaha Penalaran Mengubah Segalanya**

GPT-5.2 membolehkan anda mengawal usaha pengiraan melalui prompt anda. Usaha rendah bermaksud respon cepat dengan penerokaan minimum. Usaha tinggi bermaksud model mengambil masa untuk berfikir dengan mendalam. Anda belajar untuk memadankan usaha dengan kerumitan tugasan - jangan bazir masa pada soalan mudah, tetapi jangan tergesa-gesa membuat keputusan kompleks juga.

**Struktur Membimbing Tingkah Laku**

Perasan tag XML dalam prompt? Ia bukan hiasan. Model mengikuti arahan berstruktur dengan lebih boleh dipercayai daripada teks bebas. Apabila anda memerlukan proses berbilang langkah atau logik kompleks, struktur membantu model mengesan di mana ia berada dan apa yang seterusnya.

<img src="../../../translated_images/ms/prompt-structure.a77763d63f4e2f89.webp" alt="Struktur Prompt" width="800"/>

*Anatomi prompt berstruktur baik dengan seksyen jelas dan organisasi gaya XML*

**Kualiti Melalui Penilaian Diri**

Corak refleksi diri berfungsi dengan menjadikan kriteria kualiti eksplisit. Daripada berharap model "melakukannya dengan betul", anda beritahu tepat apa maksud "betul": logik yang tepat, pengendalian ralat, prestasi, keselamatan. Model boleh kemudian menilai output sendiri dan memperbaiki. Ini mengubah penjanaan kod dari cabutan bertuah kepada proses.

**Konteks Adalah Terhad**

Perbualan berbilang giliran berfungsi dengan menyertakan sejarah mesej dengan setiap permintaan. Tapi ada had - setiap model mempunyai had token maksimum. Apabila perbualan bertambah, anda perlu strategi untuk mengekalkan konteks relevan tanpa melebihi had itu. Modul ini menunjukkan bagaimana ingatan berfungsi; kemudian anda akan belajar bila hendak merumus, bila hendak lupa, dan bila hendak ambil semula.

## Langkah Seterusnya

**Modul Seterusnya:** [03-rag - RAG (Generasi Dipertingkatkan Pengambilan)](../03-rag/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 01 - Pengenalan](../01-introduction/README.md) | [Kembali ke Utama](../README.md) | [Seterusnya: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila ambil perhatian bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang sahih. Untuk maklumat penting, terjemahan manusia profesional adalah disyorkan. Kami tidak bertanggungjawab terhadap sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->