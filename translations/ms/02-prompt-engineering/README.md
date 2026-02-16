# Modul 02: Kejuruteraan Prompt dengan GPT-5.2

## Jadual Kandungan

- [Apa Yang Akan Anda Pelajari](../../../02-prompt-engineering)
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
- [Tangkap Layar Aplikasi](../../../02-prompt-engineering)
- [Meneroka Corak](../../../02-prompt-engineering)
  - [Rendah vs Tinggi Keghairahan](../../../02-prompt-engineering)
  - [Pelaksanaan Tugas (Preambles Alat)](../../../02-prompt-engineering)
  - [Kod Berfikir Sendiri](../../../02-prompt-engineering)
  - [Analisis Berstruktur](../../../02-prompt-engineering)
  - [Sembang Berbilang Giliran](../../../02-prompt-engineering)
  - [Penalaran Langkah demi Langkah](../../../02-prompt-engineering)
  - [Output Terhad](../../../02-prompt-engineering)
- [Apa Yang Anda Sebenarnya Pelajari](../../../02-prompt-engineering)
- [Langkah Seterusnya](../../../02-prompt-engineering)

## Apa Yang Akan Anda Pelajari

<img src="../../../translated_images/ms/what-youll-learn.c68269ac048503b2.webp" alt="Apa Yang Akan Anda Pelajari" width="800"/>

Dalam modul sebelumnya, anda melihat bagaimana memori membolehkan AI bersembang dan menggunakan Model GitHub untuk interaksi asas. Kini kita akan fokus pada cara anda mengemukakan soalan — prompt itu sendiri — menggunakan GPT-5.2 dari Azure OpenAI. Cara anda menyusun prompt memberi kesan besar kepada kualiti jawapan yang anda terima. Kita mulakan dengan semakan teknik-teknik asas prompting, kemudian beralih ke lapan corak lanjutan yang memanfaatkan sepenuhnya kebolehan GPT-5.2.

Kita gunakan GPT-5.2 kerana ia memperkenalkan kawalan penalaran - anda boleh memberitahu model berapa banyak pemikiran yang perlu dilakukan sebelum menjawab. Ini menjadikan strategi prompting yang berbeza lebih nyata dan membantu anda memahami bila menggunakan pendekatan yang sesuai. Kita juga mendapat manfaat dari had kadar yang lebih rendah di Azure untuk GPT-5.2 berbanding Model GitHub.

## Prasyarat

- Modul 01 telah disiapkan (sumber Azure OpenAI telah disebarkan)
- Fail `.env` di direktori akar dengan kelayakan Azure (dicipta oleh `azd up` dalam Modul 01)

> **Nota:** Jika anda belum menyiapkan Modul 01, ikuti arahan penyebaran di sana terlebih dahulu.

## Memahami Kejuruteraan Prompt

<img src="../../../translated_images/ms/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Apa Itu Kejuruteraan Prompt?" width="800"/>

Kejuruteraan prompt adalah tentang mereka bentuk teks input yang secara konsisten memberi anda hasil yang diperlukan. Ia bukan sekadar bertanya soalan - ia mengenai menyusun permintaan supaya model faham dengan tepat apa yang anda mahu dan bagaimana untuk menyampaikannya.

Fikirkan ia seperti memberi arahan kepada rakan sekerja. "Betulkan pepijat" adalah samar. "Betulkan pengecualian penunjuk null di UserService.java baris 45 dengan menambah pemeriksaan null" adalah spesifik. Model bahasa berfungsi cara yang sama - kepastian dan struktur penting.

<img src="../../../translated_images/ms/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Bagaimana LangChain4j Sesuai" width="800"/>

LangChain4j menyediakan infrastruktur — sambungan model, memori, dan jenis mesej — manakala corak prompt hanyalah teks berstruktur rapi yang anda hantar melalui infrastruktur itu. Blok binaan utama adalah `SystemMessage` (yang menetapkan tingkah laku dan peranan AI) dan `UserMessage` (yang membawa permintaan sebenar anda).

## Asas Kejuruteraan Prompt

<img src="../../../translated_images/ms/five-patterns-overview.160f35045ffd2a94.webp" alt="Gambaran Keseluruhan Lima Corak Kejuruteraan Prompt" width="800"/>

Sebelum menyelami corak lanjutan dalam modul ini, mari kita tinjau lima teknik prompting asas. Ini adalah alat utama yang setiap jurutera prompt harus tahu. Jika anda sudah melalui [modul Permulaan Pantas](../00-quick-start/README.md#2-prompt-patterns), anda sudah melihat ini berfungsi — di sini adalah rangka kerja konseptual di sebaliknya.

### Zero-Shot Prompting

Pendekatan paling mudah: beri arahan langsung kepada model tanpa contoh. Model bergantung sepenuhnya pada latihan untuk memahami dan melaksanakan tugas. Ini berfungsi baik untuk permintaan yang jelas di mana perilaku yang diharapkan nyata.

<img src="../../../translated_images/ms/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Arahan langsung tanpa contoh — model mentafsir tugas hanya dari arahan itu*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Respons: "Positif"
```

**Bilakah digunakan:** Pengelasan mudah, soalan langsung, terjemahan, atau mana-mana tugas yang boleh dikendalikan tanpa panduan tambahan.

### Few-Shot Prompting

Berikan contoh yang menunjukkan pola yang anda mahu model ikuti. Model belajar format input-output yang diharapkan dari contoh anda dan mengaplikasikannya pada input baru. Ini sangat meningkatkan konsistensi untuk tugas di mana format atau perilaku yang dikehendaki tidak jelas.

<img src="../../../translated_images/ms/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Belajar dari contoh — model mengenal pasti pola dan menggunakannya untuk input baru*

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

**Bilakah digunakan:** Pengelasan khusus, pemformatan konsisten, tugas domain khusus, atau apabila hasil zero-shot tidak konsisten.

### Chain of Thought

Minta model tunjukkan proses penalarannya langkah demi langkah. Daripada terus ke jawapan, model memecahkan masalah dan bekerja setiap bahagian secara jelas. Ini meningkatkan ketepatan untuk matematik, logik, dan penalaran berbilang langkah.

<img src="../../../translated_images/ms/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Penalaran langkah demi langkah — memecahkan masalah kompleks menjadi langkah logik yang jelas*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model menunjukkan: 15 - 8 = 7, kemudian 7 + 12 = 19 epal
```

**Bilakah digunakan:** Masalah matematik, teka-teki logik, debugging, atau mana-mana tugas yang menunjukkan proses penalaran meningkatkan ketepatan dan keyakinan.

### Role-Based Prompting

Tetapkan persona atau peranan AI sebelum mengemukakan soalan. Ini memberikan konteks yang membentuk nada, kedalaman, dan fokus respons. "Arkitek perisian" memberi nasihat berbeza daripada "pembangun junior" atau "juruaudit keselamatan".

<img src="../../../translated_images/ms/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Menetapkan konteks dan persona — soalan yang sama mendapat jawapan berbeza bergantung pada peranan yang diberikan*

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

**Bilakah digunakan:** Semakan kod, pembelajaran, analisis domain tertentu, atau apabila anda perlukan jawapan yang disesuaikan dengan tahap kepakaran atau perspektif tertentu.

### Prompt Templates

Cipta prompt boleh guna semula dengan tempat letak pemboleh ubah. Daripada menulis prompt baru setiap kali, definisikan templat sekali dan isikan nilai berlainan. Kelas `PromptTemplate` LangChain4j memudahkan ini dengan sintaks `{{variable}}`.

<img src="../../../translated_images/ms/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompt boleh guna semula dengan tempat letak pemboleh ubah — satu templat, banyak kegunaan*

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

**Bilakah digunakan:** Pertanyaan berulang dengan input berlainan, pemprosesan batch, membina aliran kerja AI boleh guna semula, atau apa-apa senario di mana struktur prompt tetap sama tetapi datanya berubah.

---

Lima asas ini memberi anda alat yang kuat untuk kebanyakan tugas prompting. Selebihnya modul ini membina ke atas dengan **lapan corak lanjutan** yang memanfaatkan kawalan penalaran GPT-5.2, penilaian kendiri, dan keupayaan output berstruktur.

## Corak Lanjutan

Dengan asas selesai, mari beralih ke lapan corak lanjutan yang menjadikan modul ini unik. Tidak semua masalah memerlukan pendekatan yang sama. Ada soalan perlukan jawapan cepat, ada perlukan pemikiran mendalam. Ada perlukan penalaran yang nampak, ada hanya perlukan hasil. Setiap corak di bawah dioptimumkan untuk senario berbeza — dan kawalan penalaran GPT-5.2 menjadikan perbezaan lebih ketara.

<img src="../../../translated_images/ms/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Lapan Corak Prompting" width="800"/>

*Gambaran keseluruhan lapan corak kejuruteraan prompt dan kes penggunaannya*

<img src="../../../translated_images/ms/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kawalan Penalaran dengan GPT-5.2" width="800"/>

*Kawalan penalaran GPT-5.2 membolehkan anda tentukan berapa banyak pemikiran model perlu buat — dari jawapan cepat terus ke penerokaan mendalam*

<img src="../../../translated_images/ms/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Perbandingan Usaha Penalaran" width="800"/>

*Keghairahan Rendah (cepat, terus) vs Keghairahan Tinggi (teliti, meneroka)*

**Keghairahan Rendah (Cepat & Fokus)** - Untuk soalan mudah yang mahukan jawapan cepat dan terus. Model buat penalaran minimum - maksimum 2 langkah. Gunakan ini untuk pengiraan, carian, atau soalan mudah.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Terokai dengan GitHub Copilot:** Buka [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) dan tanya:
> - "Apakah perbezaan antara corak prompting keghairahan rendah dan keghairahan tinggi?"
> - "Bagaimana tag XML dalam prompt membantu susun respons AI?"
> - "Bilakah saya harus gunakan corak refleksi diri berbanding arahan langsung?"

**Keghairahan Tinggi (Mendalam & Teliti)** - Untuk masalah kompleks yang perlukan analisis menyeluruh. Model meneroka dengan teliti dan tunjukkan penalaran terperinci. Gunakan ini untuk reka bentuk sistem, keputusan seni bina, atau penyelidikan kompleks.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Pelaksanaan Tugas (Kemajuan Langkah demi Langkah)** - Untuk aliran kerja berbilang langkah. Model sediakan pelan awal, beritahu setiap langkah semasa bekerja, kemudian beri ringkasan. Gunakan ini untuk migrasi, pelaksanaan, atau proses berbilang langkah.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Prompting Chain-of-Thought secara eksplisit minta model tunjukkan proses penalarannya, meningkatkan ketepatan untuk tugas kompleks. Pecahan langkah demi langkah membantu manusia dan AI faham logik.

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Tanya tentang corak ini:
> - "Bagaimana saya sesuaikan corak pelaksanaan tugas untuk operasi jangka panjang?"
> - "Apakah amalan terbaik susun preambles alat dalam aplikasi produksi?"
> - "Bagaimana saya tangkap dan paparkan kemas kini kemajuan antara melalui antara muka pengguna?"

<img src="../../../translated_images/ms/task-execution-pattern.9da3967750ab5c1e.webp" alt="Corak Pelaksanaan Tugas" width="800"/>

*Aliran kerja Rancang → Laksanakan → Rumus untuk tugas berbilang langkah*

**Kod Berfikir Sendiri** - Untuk menjana kod berkualiti produksi. Model hasilkan kod, semak berdasarkan kriteria kualiti, dan perbaiki secara berterusan. Gunakan ini semasa membina ciri atau perkhidmatan baru.

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

<img src="../../../translated_images/ms/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Kitaran Refleksi Diri" width="800"/>

*Gelung penambahbaikan berulang - jana, nilai, kenal pasti isu, baiki, ulang*

**Analisis Berstruktur** - Untuk penilaian konsisten. Model semak kod menggunakan rangka kerja tetap (ketepatan, amalan, prestasi, keselamatan). Gunakan ini untuk semakan kod atau penilaian kualiti.

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

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Tanya tentang analisis berstruktur:
> - "Bagaimana saya laraskan rangka kerja analisis untuk jenis semakan kod berbeza?"
> - "Apakah cara terbaik untuk mengurai dan bertindak atas output berstruktur secara program?"
> - "Bagaimana saya pastikan tahap keterukan konsisten merentas sesi semakan berbeza?"

<img src="../../../translated_images/ms/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Corak Analisis Berstruktur" width="800"/>

*Rangka kerja empat kategori untuk semakan kod konsisten dengan tahap keterukan*

**Sembang Berbilang Giliran** - Untuk perbualan yang perlukan konteks. Model ingat mesej sebelum ini dan bina padanya. Gunakan ini untuk sesi bantuan interaktif atau Q&A kompleks.

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

*Bagaimana konteks perbualan terkumpul dalam pelbagai giliran sehingga mencapai had token*

**Penalaran Langkah demi Langkah** - Untuk masalah yang perlukan logik yang jelas. Model tunjukkan penalaran eksplisit untuk setiap langkah. Gunakan ini untuk masalah matematik, teka-teki logik, atau apabila anda perlu faham proses pemikiran.

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

*Memecahkan masalah menjadi langkah logik yang jelas*

**Output Terhad** - Untuk respons dengan keperluan format tertentu. Model ikut dengan ketat peraturan format dan panjang. Gunakan ini untuk ringkasan atau apabila anda perlukan struktur output yang tepat.

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

*Mematuhi keperluan format, panjang, dan struktur tertentu*

## Menggunakan Sumber Azure Sedia Ada

**Sahkan penyebaran:**

Pastikan fail `.env` wujud di direktori akar dengan kelayakan Azure (dicipta semasa Modul 01):
```bash
cat ../.env  # Perlu menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Mulakan aplikasi:**

> **Nota:** Jika anda sudah memulakan semua aplikasi menggunakan `./start-all.sh` dari Modul 01, modul ini sudah berjalan di port 8083. Anda boleh langkau arahan mula di bawah dan terus ke http://localhost:8083.

**Pilihan 1: Menggunakan Spring Boot Dashboard (Disyorkan untuk pengguna VS Code)**

Bekas pembangunan menyertakan pelanjutan Spring Boot Dashboard, yang menyediakan antara muka visual untuk mengurus semua aplikasi Spring Boot. Anda boleh cari di Bar Aktiviti di sebelah kiri VS Code (carilah ikon Spring Boot).
Dari Papan Pemuka Spring Boot, anda boleh:
- Melihat semua aplikasi Spring Boot yang tersedia dalam ruang kerja
- Memulakan/menhentikan aplikasi dengan satu klik
- Melihat log aplikasi secara masa nyata
- Memantau status aplikasi

Cukup klik butang main di sebelah "prompt-engineering" untuk memulakan modul ini, atau mulakan semua modul sekaligus.

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

Kedua-dua skrip secara automatik memuat pemboleh ubah persekitaran daripada fail `.env` di akar dan akan membina JAR jika ia tidak wujud.

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

## Tangkapan Layar Aplikasi

<img src="../../../translated_images/ms/dashboard-home.5444dbda4bc1f79d.webp" alt="Halaman Utama Papan Pemuka" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Papan pemuka utama yang memaparkan semua 8 corak kejuruteraan arahan dengan ciri-ciri dan kes penggunaan mereka*

## Meneroka Corak-corak

Antaramuka web membolehkan anda mencuba strategi arahan yang berbeza. Setiap corak menyelesaikan masalah berbeza - cuba mereka untuk melihat bila pendekatan setiap satu bersinar.

### Kegelinciran Rendah vs Tinggi

Tanya soalan mudah seperti "Apakah 15% daripada 200?" menggunakan Kegelinciran Rendah. Anda akan mendapat jawapan segera dan terus. Sekarang tanya sesuatu yang kompleks seperti "Reka strategi caching untuk API trafik tinggi" menggunakan Kegelinciran Tinggi. Saksikan bagaimana model melambat dan memberikan alasan terperinci. Model yang sama, struktur soalan yang sama - tetapi prompt memberitahu berapa banyak pemikiran yang perlu dilakukan.

<img src="../../../translated_images/ms/low-eagerness-demo.898894591fb23aa0.webp" alt="Demo Kegelinciran Rendah" width="800"/>

*Pengiraan cepat dengan pemikiran minimum*

<img src="../../../translated_images/ms/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Demo Kegelinciran Tinggi" width="800"/>

*Strategi caching menyeluruh (2.8MB)*

### Pelaksanaan Tugasan (Pra-tajuk Alat)

Aliran kerja berbilang langkah mendapat manfaat daripada perancangan awal dan narasi kemajuan. Model menggariskan apa yang akan dilakukan, mendongeng setiap langkah, kemudian merumuskan keputusan.

<img src="../../../translated_images/ms/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Demo Pelaksanaan Tugasan" width="800"/>

*Mewujudkan titik akhir REST dengan narasi langkah demi langkah (3.9MB)*

### Kod Pemantauan Diri

Cuba "Cipta perkhidmatan pengesahan emel". Daripada hanya menjana kod dan berhenti, model menjana, menilai berdasarkan kriteria kualiti, mengenal pasti kelemahan, dan memperbaiki. Anda akan melihat ia ulang sehingga kod memenuhi piawaian produksi.

<img src="../../../translated_images/ms/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Demo Kod Pemantauan Diri" width="800"/>

*Perkhidmatan pengesahan emel lengkap (5.2MB)*

### Analisis Berstruktur

Semakan kod memerlukan rangka kerja penilaian konsisten. Model menganalisis kod menggunakan kategori tetap (ketepatan, amalan, prestasi, keselamatan) dengan tahap keterukan.

<img src="../../../translated_images/ms/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Demo Analisis Berstruktur" width="800"/>

*Semakan kod berasaskan rangka kerja*

### Sembang Berbilang Giliran

Tanya "Apa itu Spring Boot?" kemudian segera sambung dengan "Tunjukkan saya contoh". Model mengingati soalan pertama anda dan memberikan contoh Spring Boot khas. Tanpa memori, soalan kedua itu akan terlalu kabur.

<img src="../../../translated_images/ms/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Demo Sembang Berbilang Giliran" width="800"/>

*Pemeliharaan konteks merentas soalan*

### Penalaran Langkah demi Langkah

Pilih masalah matematik dan cuba dengan Penalaran Langkah demi Langkah dan Kegelinciran Rendah. Kegelinciran rendah hanya memberi jawapan - cepat tetapi samar. Penalaran langkah demi langkah menunjukkan setiap pengiraan dan keputusan.

<img src="../../../translated_images/ms/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Demo Penalaran Langkah demi Langkah" width="800"/>

*Masalah matematik dengan langkah yang jelas*

### Output Terhad

Apabila anda perlukan format khusus atau bilangan kata, corak ini memaksa pematuhan ketat. Cuba jana ringkasan dengan tepat 100 perkataan dalam format titik peluru.

<img src="../../../translated_images/ms/constrained-output-demo.567cc45b75da1633.webp" alt="Demo Output Terhad" width="800"/>

*Ringkasan pembelajaran mesin dengan kawalan format*

## Apa Yang Sebenarnya Anda Pelajari

**Usaha Penalaran Mengubah Segalanya**

GPT-5.2 membolehkan anda mengawal usaha pengiraan melalui arahan anda. Usaha rendah bermaksud respons cepat dengan penerokaan minimum. Usaha tinggi bermaksud model mengambil masa untuk berfikir secara mendalam. Anda belajar menyamakan usaha dengan kerumitan tugasan - jangan bazirkan masa pada soalan mudah, tetapi jangan tergesa-gesa dalam keputusan kompleks juga.

**Struktur Membimbing Tingkah Laku**

Perhatikan tag XML dalam arahan? Ia bukan sekadar hiasan. Model mengikuti arahan berstruktur dengan lebih boleh dipercayai berbanding teks bebas. Apabila anda perlukan proses berbilang langkah atau logik kompleks, struktur membantu model mengesan di mana ia berada dan apa seterusnya.

<img src="../../../translated_images/ms/prompt-structure.a77763d63f4e2f89.webp" alt="Struktur Arahan" width="800"/>

*Anatomi arahan berstruktur dengan seksyen jelas dan organisasi gaya XML*

**Kualiti Melalui Penilaian Diri**

Corak pemantauan diri berfungsi dengan menjadikan kriteria kualiti jelas. Daripada berharap model "melakukan dengan betul", anda memberitahu secara tepat apa maksud "benar": logik betul, pengendalian ralat, prestasi, keselamatan. Model kemudian boleh menilai output sendiri dan memperbaiki. Ini menukar penjanaan kod daripada cabutan bertuah kepada proses.

**Konteks Adalah Terhad**

Perbualan berbilang gilir bekerja dengan memasukkan sejarah mesej dengan setiap permintaan. Tetapi ada had - setiap model mempunyai kiraan token maksimum. Apabila perbualan berkembang, anda perlu strategi untuk mengekalkan konteks relevan tanpa mencapai had itu. Modul ini menunjukkan cara memori berfungsi; kemudian anda akan belajar bila untuk merumus, bila untuk lupa, dan bila untuk cari semula.

## Langkah Seterusnya

**Modul Seterusnya:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 01 - Pengenalan](../01-introduction/README.md) | [Kembali ke Utama](../README.md) | [Seterusnya: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila ambil maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya hendaklah dianggap sebagai sumber rujukan yang sah. Untuk maklumat penting, terjemahan profesional oleh manusia adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->