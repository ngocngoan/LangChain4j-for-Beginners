# Modul 00: Mula Cepat

## Jadual Kandungan

- [Pengenalan](../../../00-quick-start)
- [Apa itu LangChain4j?](../../../00-quick-start)
- [Kebergantungan LangChain4j](../../../00-quick-start)
- [Prasyarat](../../../00-quick-start)
- [Tetapan](../../../00-quick-start)
  - [1. Dapatkan Token GitHub Anda](../../../00-quick-start)
  - [2. Tetapkan Token Anda](../../../00-quick-start)
- [Jalankan Contoh](../../../00-quick-start)
  - [1. Sembang Asas](../../../00-quick-start)
  - [2. Corak Prompt](../../../00-quick-start)
  - [3. Panggilan Fungsi](../../../00-quick-start)
  - [4. Soal Jawab Dokumen (RAG)](../../../00-quick-start)
  - [5. AI Bertanggungjawab](../../../00-quick-start)
- [Apa Yang Ditunjukkan Setiap Contoh](../../../00-quick-start)
- [Langkah Seterusnya](../../../00-quick-start)
- [Selesaikan Masalah](../../../00-quick-start)

## Pengenalan

Mula cepat ini bertujuan untuk membantu anda mula menggunakan LangChain4j dengan secepat mungkin. Ia merangkumi asas mutlak membina aplikasi AI dengan LangChain4j dan Model GitHub. Dalam modul-modul seterusnya anda akan menggunakan Azure OpenAI dengan LangChain4j untuk membina aplikasi yang lebih maju.

## Apa itu LangChain4j?

LangChain4j adalah perpustakaan Java yang memudahkan pembangunan aplikasi berkuasa AI. Daripada mengendalikan klien HTTP dan penguraian JSON, anda bekerja dengan API Java yang bersih.

"Rantai" dalam LangChain merujuk kepada menghubungkan pelbagai komponen bersama-sama — anda mungkin menghubungkan prompt ke model ke parser, atau menghubungkan beberapa panggilan AI bersama di mana satu output menjadi input seterusnya. Mula cepat ini menumpukan pada asas sebelum meneroka rantai yang lebih kompleks.

<img src="../../../translated_images/ms/langchain-concept.ad1fe6cf063515e1.webp" alt="Konsep Rantaian LangChain4j" width="800"/>

*Menghubungkan komponen dalam LangChain4j - blok binaan yang menghubungkan untuk mencipta aliran kerja AI yang berkuasa*

Kita akan menggunakan tiga komponen teras:

**ChatLanguageModel** - Antaramuka untuk interaksi model AI. Panggil `model.chat("prompt")` dan dapatkan rentetan balasan. Kami menggunakan `OpenAiOfficialChatModel` yang berfungsi dengan titik akhir yang serasi OpenAI seperti Model GitHub.

**AiServices** - Mewujudkan antaramuka perkhidmatan AI yang jenis-selamat. Definisikan kaedah, anotasi dengan `@Tool`, dan LangChain4j mengendalikan pengurusan. AI secara automatik memanggil kaedah Java anda apabila diperlukan.

**MessageWindowChatMemory** - Menyimpan sejarah perbualan. Tanpanya, setiap permintaan adalah berasingan. Dengan ini, AI mengingati mesej sebelumnya dan mengekalkan konteks merentasi pelbagai pusingan.

<img src="../../../translated_images/ms/architecture.eedc993a1c576839.webp" alt="Seni Bina LangChain4j" width="800"/>

*Seni bina LangChain4j - komponen teras bekerjasama untuk menggerakkan aplikasi AI anda*

## Kebergantungan LangChain4j

Mula cepat ini menggunakan dua kebergantungan Maven dalam [`pom.xml`](../../../00-quick-start/pom.xml):

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

Modul `langchain4j-open-ai-official` menyediakan kelas `OpenAiOfficialChatModel` yang bersambung ke API yang serasi OpenAI. Model GitHub menggunakan format API yang sama, jadi tiada penyesuai khas diperlukan — cuma tunjukkan URL asas ke `https://models.github.ai/inference`.

## Prasyarat

**Menggunakan Dev Container?** Java dan Maven sudah dipasang. Anda hanya perlu Token Akses Peribadi GitHub.

**Pembangunan Tempatan:**
- Java 21+, Maven 3.9+
- Token Akses Peribadi GitHub (arahan di bawah)

> **Nota:** Modul ini menggunakan `gpt-4.1-nano` dari Model GitHub. Jangan ubah nama model dalam kod — ia dikonfigurasikan untuk berfungsi dengan model yang tersedia di GitHub.

## Tetapan

### 1. Dapatkan Token GitHub Anda

1. Pergi ke [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Klik "Generate new token"
3. Tetapkan nama yang deskriptif (contoh: "Demo LangChain4j")
4. Tetapkan tempoh luput (disyorkan 7 hari)
5. Di bawah "Account permissions", cari "Models" dan tetapkan ke "Read-only"
6. Klik "Generate token"
7. Salin dan simpan token anda — anda tidak akan melihatnya lagi

### 2. Tetapkan Token Anda

**Pilihan 1: Menggunakan VS Code (Disyorkan)**

Jika anda menggunakan VS Code, tambahkan token anda ke fail `.env` di akar projek:

Jika fail `.env` tidak wujud, salin `.env.example` ke `.env` atau buat fail `.env` baru di akar projek.

**Contoh fail `.env`:**
```bash
# Di /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Kemudian anda boleh klik kanan pada mana-mana fail demo (contoh: `BasicChatDemo.java`) di Explorer dan pilih **"Run Java"** atau gunakan konfigurasi pelancaran dari panel Run and Debug.

**Pilihan 2: Menggunakan Terminal**

Tetapkan token sebagai pemboleh ubah persekitaran:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Jalankan Contoh

**Menggunakan VS Code:** Klik kanan sahaja pada mana-mana fail demo di Explorer dan pilih **"Run Java"**, atau gunakan konfigurasi pelancaran dari panel Run and Debug (pastikan anda sudah tambahkan token dalam fail `.env` dahulu).

**Menggunakan Maven:** Sebagai alternatif, anda boleh jalankan dari baris perintah:

### 1. Sembang Asas

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Corak Prompt

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Menunjukkan zero-shot, few-shot, chain-of-thought, dan prompt berasaskan peranan.

### 3. Panggilan Fungsi

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI secara automatik memanggil kaedah Java anda apabila diperlukan.

### 4. Soal Jawab Dokumen (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Tanya soalan tentang kandungan dalam `document.txt`.

### 5. AI Bertanggungjawab

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Lihat bagaimana penapis keselamatan AI menghalang kandungan berbahaya.

## Apa Yang Ditunjukkan Setiap Contoh

**Sembang Asas** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Mulakan di sini untuk melihat LangChain4j pada tahap paling asas. Anda akan buat `OpenAiOfficialChatModel`, hantar prompt dengan `.chat()`, dan dapatkan balasan. Ini menunjukkan asasnya: cara inisialisasi model dengan titik akhir dan kunci API tersuai. Setelah faham corak ini, semua yang lain dibina di atasnya.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) dan tanya:
> - "Bagaimana saya tukar dari Model GitHub ke Azure OpenAI dalam kod ini?"
> - "Apakah parameter lain yang boleh saya konfigurasikan dalam OpenAiOfficialChatModel.builder()?"
> - "Bagaimana saya tambah jawapan streaming dan bukannya tunggu jawapan lengkap?"

**Kejuruteraan Prompt** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Sekarang anda sudah tahu cara bercakap dengan model, mari terokai apa yang anda katakan kepadanya. Demo ini menggunakan susunan model yang sama tetapi menunjukkan lima corak prompt berlainan. Cuba prompt zero-shot untuk arahan langsung, few-shot yang belajar dari contoh, rantai pemikiran yang mendedahkan langkah pemikiran, dan prompt berasaskan peranan yang menetapkan konteks. Anda akan lihat bagaimana model yang sama memberi hasil sangat berbeza mengikut cara anda rangka permintaan.

Demo juga menunjukkan templat prompt, cara berkuasa untuk buat prompt boleh guna semula dengan pemboleh ubah. Contoh di bawah menunjukkan prompt menggunakan LangChain4j `PromptTemplate` untuk mengisi pemboleh ubah. AI akan menjawab berdasarkan destinasi dan aktiviti yang diberikan.

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

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) dan tanya:
> - "Apakah perbezaan antara zero-shot dan few-shot prompting, dan bila saya harus guna setiap satu?"
> - "Bagaimana parameter suhu menjejaskan jawapan model?"
> - "Apakah teknik untuk cegah serangan suntikan prompt dalam produksi?"
> - "Bagaimana saya buat objek PromptTemplate boleh guna semula untuk corak biasa?"

**Integrasi Alat** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Di sinilah LangChain4j menjadi berkuasa. Anda akan guna `AiServices` untuk buat pembantu AI yang boleh panggil kaedah Java anda. Hanya anotasi kaedah dengan `@Tool("deskripsi")` dan LangChain4j uruskan selebihnya — AI secara automatik tentukan bila guna setiap alat berdasarkan soalan pengguna. Ini menunjukkan panggilan fungsi, teknik utama untuk bina AI yang boleh mengambil tindakan, bukan hanya menjawab soalan.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) dan tanya:
> - "Bagaimana anotasi @Tool berfungsi dan apa yang LangChain4j buat di belakang tabir?"
> - "Bolehkah AI panggil beberapa alat secara berurutan untuk selesaikan masalah kompleks?"
> - "Apa jadi jika alat beri pengecualian — bagaimana saya urus ralat?"
> - "Bagaimana saya integrasi API sebenar menggantikan contoh kalkulator ini?"

**Soal Jawab Dokumen (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Di sini anda lihat asas RAG (penjanaan berimbas masuk). Daripada bergantung pada data latihan model, anda muatkan kandungan dari [`document.txt`](../../../00-quick-start/document.txt) dan termasuk dalam prompt. AI menjawab berdasarkan dokumen anda, bukan pengetahuan amnya. Ini langkah pertama untuk bina sistem yang boleh guna data sendiri.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Nota:** Pendekatan mudah ini memuatkan keseluruhan dokumen ke dalam prompt. Untuk fail besar (>10KB), anda akan melebihi had konteks. Modul 03 terangkan cara potong dan cari vektor untuk sistem RAG produksi.

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) dan tanya:
> - "Bagaimana RAG elak halusinasi AI berbanding guna data latihan model?"
> - "Apakah perbezaan antara pendekatan mudah ini dengan guna pembenaman vektor untuk pencarian?"
> - "Bagaimana saya skalakan ini untuk tangani dokumen berganda atau pangkalan pengetahuan lebih besar?"
> - "Apakah amalan terbaik untuk struktur prompt agar AI guna hanya konteks disediakan?"

**AI Bertanggungjawab** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Bina keselamatan AI dengan perlindungan berlapis. Demo ini tunjuk dua lapisan perlindungan berkerjasama:

**Bahagian 1: LangChain4j Input Guardrails** - Halang prompt berbahaya sebelum sampai ke LLM. Buat kawalan kustom yang periksa kata kunci atau corak dilarang. Ini dijalankan dalam kod anda, jadi cepat dan percuma.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**Bahagian 2: Penapis Keselamatan Penyedia** - Model GitHub ada penapis terbina dalam yang tangkap apa yang kawalan anda mungkin terlepas. Anda akan lihat blok keras (ralat HTTP 400) untuk pelanggaran serius dan penolakan lembut apabila AI menolak dengan cara sopan.

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) dan tanya:
> - "Apa itu InputGuardrail dan bagaimana saya buat sendiri?"
> - "Apakah perbezaan antara blok keras dan penolakan lembut?"
> - "Kenapa guna kedua-dua guardrails dan penapis penyedia sekaligus?"

## Langkah Seterusnya

**Modul Seterusnya:** [01-pengenalan - Mula Bermula dengan LangChain4j dan gpt-5 di Azure](../01-introduction/README.md)

---

**Navigasi:** [← Kembali ke Utama](../README.md) | [Seterusnya: Modul 01 - Pengenalan →](../01-introduction/README.md)

---

## Selesaikan Masalah

### Kompilasi Maven Kali Pertama

**Isu**: `mvn clean compile` atau `mvn package` pertama mengambil masa lama (10-15 minit)

**Punca**: Maven perlu muat turun semua kebergantungan projek (Spring Boot, perpustakaan LangChain4j, SDK Azure, dll.) pada binaan pertama.

**Penyelesaian**: Ini kelakuan biasa. Binaan berikutnya akan jauh lebih pantas kerana kebergantungan telah disimpan di cache tempatan. Masa muat turun bergantung pada kelajuan rangkaian anda.
### Sintaks Perintah PowerShell Maven

**Masalah**: Perintah Maven gagal dengan ralat `Unknown lifecycle phase ".mainClass=..."`

**Punca**: PowerShell mentafsir `=` sebagai operator penetapan pemboleh ubah, yang merosakkan sintaks hartanah Maven

**Penyelesaian**: Gunakan operator berhenti-mentafsir `--%` sebelum perintah Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operator `--%` memberitahu PowerShell untuk menyerahkan semua argumen yang tinggal secara literal kepada Maven tanpa tafsiran.

### Paparan Emoji Windows PowerShell

**Masalah**: Respons AI memaparkan aksara sampah (contohnya, `????` atau `â??`) menggantikan emoji dalam PowerShell

**Punca**: Pengekodan lalai PowerShell tidak menyokong emoji UTF-8

**Penyelesaian**: Jalankan perintah ini sebelum melaksanakan aplikasi Java:
```cmd
chcp 65001
```

Ini memaksa pengekodan UTF-8 dalam terminal. Sebagai alternatif, gunakan Windows Terminal yang mempunyai sokongan Unicode lebih baik.

### Penyahpepijatan Panggilan API

**Masalah**: Ralat pengesahan, had kadar, atau respons yang tidak dijangka daripada model AI

**Penyelesaian**: Contoh termasuk `.logRequests(true)` dan `.logResponses(true)` untuk menunjukkan panggilan API dalam konsol. Ini membantu menyelesaikan ralat pengesahan, had kadar, atau respons yang tidak dijangka. Buang penanda ini dalam produksi untuk mengurangkan bunyi log.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk mencapai ketepatan, sila ambil maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidakakuratan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang sahih. Untuk maklumat penting, terjemahan profesional oleh manusia adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->