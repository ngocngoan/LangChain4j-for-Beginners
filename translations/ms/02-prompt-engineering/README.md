# Modul 02: Kejuruteraan Prompt dengan GPT-5.2

## Jadual Kandungan

- [Apa yang Akan Anda Pelajari](../../../02-prompt-engineering)
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
- [Meneroka Corak](../../../02-prompt-engineering)
  - [Keghairahan Rendah vs Tinggi](../../../02-prompt-engineering)
  - [Pelaksanaan Tugas (Preambule Alat)](../../../02-prompt-engineering)
  - [Kod Refleksi Diri](../../../02-prompt-engineering)
  - [Analisis Berstruktur](../../../02-prompt-engineering)
  - [Sembang Multi-Pusingan](../../../02-prompt-engineering)
  - [Penalaran Langkah demi Langkah](../../../02-prompt-engineering)
  - [Output Terhad](../../../02-prompt-engineering)
- [Apa yang Anda Sebenarnya Pelajari](../../../02-prompt-engineering)
- [Langkah Seterusnya](../../../02-prompt-engineering)

## Apa yang Akan Anda Pelajari

<img src="../../../translated_images/ms/what-youll-learn.c68269ac048503b2.webp" alt="Apa yang Akan Anda Pelajari" width="800"/>

Dalam modul sebelumnya, anda melihat bagaimana memori membolehkan AI perbualan dan menggunakan Model GitHub untuk interaksi asas. Kini kita akan fokus pada cara anda mengemukakan soalan — prompt itu sendiri — menggunakan GPT-5.2 Azure OpenAI. Cara anda menyusun prompt secara dramatik mempengaruhi kualiti jawapan yang anda dapat. Kita bermula dengan tinjauan teknik prompting asas, kemudian beralih ke lapan corak lanjutan yang memanfaatkan sepenuhnya keupayaan GPT-5.2.

Kita menggunakan GPT-5.2 kerana ia memperkenalkan kawalan penalaran - anda boleh memberitahu model berapa banyak pemikiran yang perlu dilakukan sebelum menjawab. Ini menjadikan strategi prompting yang berbeza lebih jelas dan membantu anda memahami bila menggunakan setiap pendekatan. Kita juga akan mendapat manfaat daripada had kadar yang lebih rendah Azure untuk GPT-5.2 berbanding Model GitHub.

## Prasyarat

- Telah menyelesaikan Modul 01 (sumber Azure OpenAI telah disebarkan)
- Fail `.env` di direktori utama dengan kelayakan Azure (dicipta oleh `azd up` dalam Modul 01)

> **Nota:** Jika anda belum menyelesaikan Modul 01, ikuti arahan penyebaran di sana terlebih dahulu.

## Memahami Kejuruteraan Prompt

<img src="../../../translated_images/ms/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Apa itu Kejuruteraan Prompt?" width="800"/>

Kejuruteraan prompt adalah tentang mereka teks input yang sentiasa memberikan hasil yang anda perlukan. Ia bukan sekadar bertanya soalan - ia tentang menyusun permintaan supaya model memahami dengan tepat apa yang anda mahu dan bagaimana untuk menyampaikannya.

Fikirkan ia seperti memberi arahan kepada rakan sekerja. "Betulkan pepijat" adalah samar. "Betulkan pengecualian penunjuk kosong dalam UserService.java baris 45 dengan menambah semakan null" adalah spesifik. Model bahasa berfungsi dengan cara yang sama - ketepatan dan struktur penting.

<img src="../../../translated_images/ms/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Bagaimana LangChain4j Sesuai" width="800"/>

LangChain4j menyediakan infrastruktur — sambungan model, memori, dan jenis mesej — manakala corak prompt hanyalah teks yang disusun dengan teliti yang anda hantar melalui infrastruktur itu. Blok binaan utama ialah `SystemMessage` (yang menetapkan tingkah laku dan peranan AI) dan `UserMessage` (yang membawa permintaan sebenar anda).

## Asas Kejuruteraan Prompt

<img src="../../../translated_images/ms/five-patterns-overview.160f35045ffd2a94.webp" alt="Gambaran Keseluruhan Lima Corak Kejuruteraan Prompt" width="800"/>

Sebelum menyelami corak lanjutan dalam modul ini, mari kita tinjau lima teknik prompting asas. Ini adalah blok binaan yang setiap jurutera prompt harus tahu. Jika anda sudah bekerja melalui [modul Mula Cepat](../00-quick-start/README.md#2-prompt-patterns), anda sudah melihatnya beraksi — berikut adalah rangka kerja konsep di sebaliknya.

### Zero-Shot Prompting

Pendekatan paling mudah: berikan model arahan terus tanpa contoh. Model bergantung sepenuhnya pada latihannya untuk memahami dan melaksanakan tugas. Ini berfungsi dengan baik untuk permintaan mudah di mana tingkah laku yang diharapkan adalah jelas.

<img src="../../../translated_images/ms/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Arahan langsung tanpa contoh — model membuat inferens tentang tugas hanya dari arahan*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Respons: "Positif"
```
  
**Bila digunakan:** Klasifikasi mudah, soalan terus, terjemahan, atau mana-mana tugas yang model boleh kendalikan tanpa panduan tambahan.

### Few-Shot Prompting

Berikan contoh yang menunjukkan corak yang anda mahu model ikut. Model belajar format input-output yang dijangka dari contoh anda dan mengaplikasikannya kepada input baharu. Ini meningkatkan konsistensi secara dramatik untuk tugas dimana format atau tingkah laku yang dikehendaki tidak jelas.

<img src="../../../translated_images/ms/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Belajar daripada contoh — model mengenal pasti corak dan mengaplikasikannya pada input baru*

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
  
**Bila digunakan:** Klasifikasi tersuai, format konsisten, tugas khusus domain, atau bila hasil zero-shot tidak konsisten.

### Chain of Thought

Minta model menunjukkan penalarannya secara langkah demi langkah. Daripada terus lompat ke jawapan, model memecahkan masalah dan mengerjakan setiap bahagian secara jelas. Ini meningkatkan ketepatan pada masalah matematik, logik, dan penalaran berbilang langkah.

<img src="../../../translated_images/ms/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Penalaran langkah demi langkah — memecah masalah kompleks kepada langkah logik eksplisit*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model itu menunjukkan: 15 - 8 = 7, kemudian 7 + 12 = 19 epal
```
  
**Bila digunakan:** Masalah matematik, teka-teki logik, pengesanan pepijat, atau mana-mana tugas di mana menunjukkan proses penalaran meningkatkan ketepatan dan kepercayaan.

### Role-Based Prompting

Tetapkan persona atau peranan untuk AI sebelum bertanya soalan anda. Ini memberikan konteks yang membentuk nada, kedalaman, dan fokus jawapan. "Arkitek perisian" memberi nasihat berbeza daripada "pembangun junior" atau "juruaudit keselamatan".

<img src="../../../translated_images/ms/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Menetapkan konteks dan persona — soalan sama mendapat jawapan berbeza bergantung pada peranan yang diberikan*

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
  
**Bila digunakan:** Semakan kod, bimbingan, analisis domain khusus, atau bila anda perlukan jawapan disesuaikan dengan tahap kepakaran atau perspektif tertentu.

### Prompt Templates

Cipta prompt boleh guna semula dengan pemegang pembolehubah. Daripada menulis prompt baru setiap kali, takrifkan templat sekali dan isi nilai berbeza. Kelas `PromptTemplate` LangChain4j memudahkan ini dengan sintaks `{{variable}}`.

<img src="../../../translated_images/ms/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompt boleh guna semula dengan pemegang pembolehubah — satu templat, banyak kegunaan*

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
  
**Bila digunakan:** Pertanyaan berulang dengan input berbeza, pemprosesan batch, membina aliran kerja AI boleh guna semula, atau mana-mana senario di mana struktur prompt kekal sama tetapi data berubah.

---

Lima asas ini memberi anda set alat kukuh untuk kebanyakan tugas prompting. Selebihnya modul ini dibina atas mereka dengan **lapan corak lanjutan** yang memanfaatkan kawalan penalaran, penilaian kendiri, dan keupayaan output berstruktur GPT-5.2.

## Corak Lanjutan

Dengan asas yang telah dibincangkan, mari beralih ke lapan corak lanjutan yang menjadikan modul ini unik. Tidak semua masalah memerlukan pendekatan yang sama. Sesetengah soalan memerlukan jawapan cepat, yang lain memerlukan pemikiran mendalam. Sesetengah memerlukan penalaran yang kelihatan, yang lain hanya memerlukan hasil. Setiap corak di bawah dioptimumkan untuk senario berbeza — dan kawalan penalaran GPT-5.2 menjadikan perbezaan itu lebih ketara.

<img src="../../../translated_images/ms/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Lapan Corak Prompting" width="800"/>

*Gambaran keseluruhan lapan corak kejuruteraan prompt dan kes gunaannya*

<img src="../../../translated_images/ms/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kawalan Penalaran dengan GPT-5.2" width="800"/>

*Kawalan penalaran GPT-5.2 membolehkan anda menentukan berapa banyak pemikiran model harus lakukan — dari jawapan pantas terus ke penerokaan mendalam*

**Keghairahan Rendah (Pantas & Fokus)** - Untuk soalan mudah di mana anda mahu jawapan pantas dan terus. Model melakukan penalaran minimum - maksimum 2 langkah. Gunakan ini untuk pengiraan, carian, atau soalan mudah.

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
> - "Apa perbezaan corak prompting keghairahan rendah dan tinggi?"  
> - "Bagaimana tag XML dalam prompt membantu menyusun jawapan AI?"  
> - "Bila saya harus guna corak refleksi diri berbanding arahan langsung?"

**Keghairahan Tinggi (Mendalam & Teliti)** - Untuk masalah kompleks di mana anda mahu analisis menyeluruh. Model meneroka dengan teliti dan menunjukkan penalaran terperinci. Gunakan ini untuk reka bentuk sistem, keputusan seni bina, atau penyelidikan kompleks.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**Pelaksanaan Tugas (Kemajuan Langkah demi Langkah)** - Untuk aliran kerja berbilang langkah. Model menyediakan pelan awal, menceritakan setiap langkah sambil bekerja, kemudian memberi ringkasan. Gunakan ini untuk migrasi, pelaksanaan, atau mana-mana proses berbilang langkah.

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
  
Prompt chain-of-thought secara eksplisit meminta model menunjukkan proses penalarannya, meningkatkan ketepatan untuk tugas kompleks. Pecahan langkah demi langkah membantu manusia dan AI memahami logik.

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Tanyakan tentang corak ini:  
> - "Bagaimana saya sesuaikan corak pelaksanaan tugas untuk operasi jangka panjang?"  
> - "Apakah amalan terbaik untuk menyusun preambule alat dalam aplikasi produksi?"  
> - "Bagaimana saya menangkap dan memaparkan kemas kini kemajuan antara langkah dalam UI?"

<img src="../../../translated_images/ms/task-execution-pattern.9da3967750ab5c1e.webp" alt="Corak Pelaksanaan Tugas" width="800"/>

*Rancang → Laksanakan → Rumus aliran kerja untuk tugas berbilang langkah*

**Kod Refleksi Diri** - Untuk menjana kod berkualiti pengeluaran. Model menjana kod mengikuti piawaian pengeluaran dengan pengendalian ralat yang betul. Gunakan ini bila membina ciri baru atau perkhidmatan.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/ms/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Kitaran Refleksi Diri" width="800"/>

*Gelung penambahbaikan berulang - jana, nilai, kenal pasti isu, baik pulih, ulang*

**Analisis Berstruktur** - Untuk penilaian konsisten. Model mengkaji kod menggunakan kerangka tetap (ketepatan, amalan, prestasi, keselamatan, kemudahan diselenggara). Gunakan ini untuk semakan kod atau penilaian kualiti.

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
  
> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Tanyakan tentang analisis berstruktur:  
> - "Bagaimana saya sesuaikan kerangka analisis untuk pelbagai jenis semakan kod?"  
> - "Apakah cara terbaik untuk menguraikan dan bertindak atas output berstruktur secara programatik?"  
> - "Bagaimana saya pastikan tahap keterukan konsisten merentas sesi semakan berbeza?"

<img src="../../../translated_images/ms/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Corak Analisis Berstruktur" width="800"/>

*Kerangka untuk semakan kod konsisten dengan tahap keterukan*

**Sembang Multi-Pusingan** - Untuk perbualan yang memerlukan konteks. Model ingat mesej sebelum ini dan bina daripadanya. Gunakan ini untuk sesi bantuan interaktif atau Q&A kompleks.

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

*Bagaimana konteks perbualan terkumpul sepanjang pelbagai pusingan sehingga mencapai had token*

**Penalaran Langkah demi Langkah** - Untuk masalah yang memerlukan logik yang kelihatan. Model menunjukkan penalaran eksplisit untuk setiap langkah. Gunakan ini untuk masalah matematik, teka-teki logik, atau bila anda perlu faham proses pemikiran.

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

*Memecah masalah kepada langkah logik eksplisit*

**Output Terhad** - Untuk jawapan dengan keperluan format khusus. Model mengikut ketat peraturan format dan panjang. Gunakan ini untuk ringkasan atau bila anda perlukan struktur output yang tepat.

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

*Mematuhi keperluan format, panjang, dan struktur khusus*

## Menggunakan Sumber Azure Sedia Ada

**Sahkan penyebaran:**

Pastikan fail `.env` wujud di direktori utama dengan kelayakan Azure (dicipta semasa Modul 01):  
```bash
cat ../.env  # Perlu menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Mulakan aplikasi:**

> **Nota:** Jika anda sudah memulakan semua aplikasi menggunakan `./start-all.sh` dari Modul 01, modul ini sudah berjalan di port 8083. Anda boleh abaikan arahan mula di bawah dan terus ke http://localhost:8083.

**Pilihan 1: Menggunakan Spring Boot Dashboard (Disyorkan untuk pengguna VS Code)**

Kontena pembangunan termasuk sambungan Spring Boot Dashboard, yang menyediakan antara muka visual untuk mengurus semua aplikasi Spring Boot. Anda boleh menemuinya di Bar Aktiviti di sebelah kiri VS Code (cari ikon Spring Boot).

Daripada Spring Boot Dashboard, anda boleh:  
- Melihat semua aplikasi Spring Boot yang tersedia dalam ruang kerja  
- Mula/hentikan aplikasi dengan satu klik  
- Melihat log aplikasi secara masa nyata  
- Memantau status aplikasi
Cuma klik butang main di sebelah "prompt-engineering" untuk memulakan modul ini, atau mulakan semua modul sekaligus.

<img src="../../../translated_images/ms/dashboard.da2c2130c904aaf0.webp" alt="Papan Pemuka Spring Boot" width="400"/>

**Pilihan 2: Menggunakan skrip shell**

Mulakan semua aplikasi web (modul 01-04):

**Bash:**
```bash
cd ..  # Dari direktori akar
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

Kedua-dua skrip secara automatik memuatkan pembolehubah persekitaran dari fail `.env` di akar dan akan membina JAR jika ia tidak wujud.

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

<img src="../../../translated_images/ms/dashboard-home.5444dbda4bc1f79d.webp" alt="Papan Pemuka Utama" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Papan pemuka utama yang memaparkan semua 8 corak kejuruteraan arahan dengan ciri-ciri dan kes penggunaannya*

## Meneroka Corak

Antaramuka web membolehkan anda bereksperimen dengan pelbagai strategi pemintaan. Setiap corak menyelesaikan masalah yang berbeza - cuba mereka untuk melihat bila setiap pendekatan menjadi effektif.

### Keghairahan Rendah vs Tinggi

Tanya soalan mudah seperti "Apakah 15% daripada 200?" menggunakan Keghairahan Rendah. Anda akan mendapat jawapan segera dan langsung. Sekarang tanya sesuatu yang kompleks seperti "Reka strategi caching untuk API trafik tinggi" menggunakan Keghairahan Tinggi. Perhatikan bagaimana model melambatkan dan memberikan penjelasan terperinci. Model sama, struktur soalan sama - tetapi arahan memberitahu berapa banyak pemikiran yang perlu dilakukan.

<img src="../../../translated_images/ms/low-eagerness-demo.898894591fb23aa0.webp" alt="Demo Keghairahan Rendah" width="800"/>

*Pengiraan cepat dengan penalaran minimum*

<img src="../../../translated_images/ms/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Demo Keghairahan Tinggi" width="800"/>

*Strategi caching komprehensif (2.8MB)*

### Pelaksanaan Tugas (Preambul Alat)

Aliran kerja berbilang langkah mendapat faedah dari perancangan awal dan penceritaan kemajuan. Model menerangkan apa yang akan dilakukan, menceritakan setiap langkah, kemudian merumuskan keputusan.

<img src="../../../translated_images/ms/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Demo Pelaksanaan Tugas" width="800"/>

*Mewujudkan titik akhir REST dengan penceritaan langkah demi langkah (3.9MB)*

### Kod Reflektif Sendiri

Cuba "Cipta perkhidmatan pengesahan emel". Daripada hanya menghasilkan kod dan berhenti, model menghasilkan, menilai berdasarkan kriteria kualiti, mengenal pasti kelemahan, dan memperbaiki. Anda akan melihat ia mengulangi sehingga kod mencapai standard produksi.

<img src="../../../translated_images/ms/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Demo Kod Reflektif Sendiri" width="800"/>

*Perkhidmatan pengesahan emel lengkap (5.2MB)*

### Analisis Berstruktur

Ulasan kod memerlukan rangka kerja penilaian yang konsisten. Model menganalisis kod menggunakan kategori tetap (ketepatan, amalan, prestasi, keselamatan) dengan tahap keterukan.

<img src="../../../translated_images/ms/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Demo Analisis Berstruktur" width="800"/>

*Ulasan kod berasaskan rangka kerja*

### Sembang Berpusingan

Tanya "Apakah Spring Boot?" kemudian teruskan dengan "Tunjukkan saya contoh". Model mengingati soalan pertama anda dan memberi contoh Spring Boot khusus untuk itu. Tanpa memori, soalan kedua terlalu samar.

<img src="../../../translated_images/ms/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Demo Sembang Berpusingan" width="800"/>

*Pemeliharaan konteks merentas soalan*

### Penalaran Langkah demi Langkah

Pilih masalah matematik dan cuba dengan Penalaran Langkah demi Langkah dan Keghairahan Rendah. Keghairahan rendah hanya memberi jawapan - pantas tetapi tidak jelas. Langkah demi langkah menunjukkan setiap pengiraan dan keputusan.

<img src="../../../translated_images/ms/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Demo Penalaran Langkah demi Langkah" width="800"/>

*Masalah matematik dengan langkah jelas*

### Output Terhad

Apabila anda memerlukan format khusus atau bilangan perkataan, corak ini memaksa pematuhan ketat. Cuba jana rumusan dengan tepat 100 perkataan dalam format point bulet.

<img src="../../../translated_images/ms/constrained-output-demo.567cc45b75da1633.webp" alt="Demo Output Terhad" width="800"/>

*Rumusan pembelajaran mesin dengan kawalan format*

## Apa Yang Anda Sebenarnya Belajar

**Usaha Penalaran Mengubah Segalanya**

GPT-5.2 membolehkan anda mengawal usaha pengiraan melalui arahan anda. Usaha rendah bermakna respons pantas dengan penerokaan minimum. Usaha tinggi bermakna model mengambil masa untuk berfikir secara mendalam. Anda belajar untuk menyesuaikan usaha dengan kerumitan tugas - jangan bazir masa pada soalan mudah, tetapi jangan tergesa-gesa membuat keputusan kompleks juga.

**Struktur Membimbing Tingkah Laku**

Perhatikan tag XML dalam arahan? Ia bukan hiasan. Model mengikuti arahan berstruktur dengan lebih boleh diharapkan berbanding teks bebas. Apabila anda memerlukan proses berbilang langkah atau logik kompleks, struktur membantu model mengesan di mana ia berada dan apa yang seterusnya.

<img src="../../../translated_images/ms/prompt-structure.a77763d63f4e2f89.webp" alt="Struktur Arahan" width="800"/>

*Anatomi arahan berstruktur baik dengan bahagian jelas dan organisasi gaya XML*

**Kualiti Melalui Penilaian Sendiri**

Corak reflektif sendiri berfungsi dengan menjadikan kriteria kualiti eksplisit. Daripada berharap model "melakukannya dengan betul", anda memberitahu dengan tepat apa maksud "betul": logik tepat, pengendalian ralat, prestasi, keselamatan. Model kemudian boleh menilai hasilnya sendiri dan memperbaiki. Ini mengubah penjanaan kod daripada cabutan bertuah kepada proses.

**Konteks Adalah Terhad**

Perbualan berpusingan berfungsi dengan memasukkan sejarah mesej dengan setiap permintaan. Tetapi ada had - setiap model mempunyai jumlah token maksimum. Apabila perbualan berkembang, anda perlu strategi untuk mengekalkan konteks relevan tanpa melebihi had itu. Modul ini menunjukkan cara memori berfungsi; kemudian anda akan belajar bila untuk merumuskan, bila untuk lupa, dan bila untuk memanggil semula.

## Langkah Seterusnya

**Modul Seterusnya:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 01 - Pengenalan](../01-introduction/README.md) | [Kembali ke Utama](../README.md) | [Seterusnya: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk memastikan ketepatan, sila ambil maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber rujukan yang sahih. Untuk maklumat penting, terjemahan manusia profesional adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->