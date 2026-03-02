# Module 00: Mulai Cepat

## Daftar Isi

- [Pendahuluan](../../../00-quick-start)
- [Apa itu LangChain4j?](../../../00-quick-start)
- [Dependensi LangChain4j](../../../00-quick-start)
- [Prasyarat](../../../00-quick-start)
- [Pengaturan](../../../00-quick-start)
  - [1. Dapatkan Token GitHub Anda](../../../00-quick-start)
  - [2. Atur Token Anda](../../../00-quick-start)
- [Jalankan Contoh](../../../00-quick-start)
  - [1. Chat Dasar](../../../00-quick-start)
  - [2. Pola Prompt](../../../00-quick-start)
  - [3. Panggilan Fungsi](../../../00-quick-start)
  - [4. Tanya Jawab Dokumen (Easy RAG)](../../../00-quick-start)
  - [5. AI Bertanggung Jawab](../../../00-quick-start)
- [Apa yang Ditunjukkan Setiap Contoh](../../../00-quick-start)
- [Langkah Selanjutnya](../../../00-quick-start)
- [Pemecahan Masalah](../../../00-quick-start)

## Pendahuluan

Mulai cepat ini dimaksudkan agar Anda bisa langsung menggunakan LangChain4j secepat mungkin. Ini mencakup dasar-dasar membangun aplikasi AI dengan LangChain4j dan Model GitHub. Pada modul-modul berikutnya Anda akan beralih ke Azure OpenAI dan GPT-5.2 dan menggali lebih dalam setiap konsep.

## Apa itu LangChain4j?

LangChain4j adalah perpustakaan Java yang mempermudah pembangunan aplikasi bertenaga AI. Alih-alih berurusan dengan klien HTTP dan parsing JSON, Anda bekerja dengan API Java yang bersih.

"Chain" dalam LangChain mengacu pada menghubungkan beberapa komponen bersama-sama - Anda mungkin menghubungkan prompt ke model ke parser, atau menghubungkan beberapa panggilan AI di mana satu output menjadi input berikutnya. Mulai cepat ini fokus pada dasar-dasar sebelum mengeksplorasi rantai yang lebih kompleks.

<img src="../../../translated_images/id/langchain-concept.ad1fe6cf063515e1.webp" alt="Konsep Penggabungan LangChain4j" width="800"/>

*Menggabungkan komponen dalam LangChain4j - blok bangunan menghubungkan untuk membuat alur kerja AI yang kuat*

Kita akan menggunakan tiga komponen inti:

**ChatModel** - Antarmuka untuk interaksi model AI. Panggil `model.chat("prompt")` dan dapatkan string respon. Kami menggunakan `OpenAiOfficialChatModel` yang bekerja dengan endpoint yang kompatibel dengan OpenAI seperti Model GitHub.

**AiServices** - Membuat antarmuka layanan AI yang tipe-aman. Definisikan metode, anotasi dengan `@Tool`, dan LangChain4j menangani orkestrasi. AI secara otomatis memanggil metode Java Anda saat dibutuhkan.

**MessageWindowChatMemory** - Mempertahankan riwayat percakapan. Tanpa ini, setiap permintaan bersifat independen. Dengan ini, AI ingat pesan sebelumnya dan mempertahankan konteks di beberapa gilirannya.

<img src="../../../translated_images/id/architecture.eedc993a1c576839.webp" alt="Arsitektur LangChain4j" width="800"/>

*Arsitektur LangChain4j - komponen inti bekerja bersama untuk memberi tenaga pada aplikasi AI Anda*

## Dependensi LangChain4j

Mulai cepat ini menggunakan tiga dependensi Maven dalam [`pom.xml`](../../../00-quick-start/pom.xml):

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

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

Modul `langchain4j-open-ai-official` menyediakan kelas `OpenAiOfficialChatModel` yang terhubung ke API yang kompatibel dengan OpenAI. Model GitHub menggunakan format API yang sama, jadi tidak diperlukan adaptor khusus — cukup arahkan URL dasar ke `https://models.github.ai/inference`.

Modul `langchain4j-easy-rag` menyediakan pemisahan dokumen otomatis, embedding, dan pengambilan sehingga Anda dapat membangun aplikasi RAG tanpa mengkonfigurasi setiap langkah secara manual.

## Prasyarat

**Menggunakan Dev Container?** Java dan Maven sudah terpasang. Anda hanya memerlukan Token Akses Pribadi GitHub.

**Pengembangan Lokal:**
- Java 21+, Maven 3.9+
- Token Akses Pribadi GitHub (instruksi di bawah)

> **Catatan:** Modul ini menggunakan `gpt-4.1-nano` dari Model GitHub. Jangan ubah nama model dalam kode — ini dikonfigurasi untuk bekerja dengan model yang tersedia di GitHub.

## Pengaturan

### 1. Dapatkan Token GitHub Anda

1. Pergi ke [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Klik "Generate new token"
3. Beri nama yang deskriptif (misal, "LangChain4j Demo")
4. Tentukan masa berlaku (7 hari direkomendasikan)
5. Di bawah "Account permissions", cari "Models" dan atur ke "Read-only"
6. Klik "Generate token"
7. Salin dan simpan token Anda — Anda tidak akan melihatnya lagi

### 2. Atur Token Anda

**Pilihan 1: Menggunakan VS Code (Direkomendasikan)**

Jika Anda menggunakan VS Code, tambahkan token Anda ke file `.env` di root proyek:

Jika file `.env` tidak ada, salin `.env.example` ke `.env` atau buat file `.env` baru di root proyek.

**Contoh file `.env`:**
```bash
# Di /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Lalu Anda cukup klik kanan pada file demo apa saja (misal `BasicChatDemo.java`) di Explorer dan pilih **"Run Java"** atau gunakan konfigurasi peluncuran dari panel Run and Debug.

**Pilihan 2: Menggunakan Terminal**

Atur token sebagai variabel lingkungan:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Jalankan Contoh

**Menggunakan VS Code:** Cukup klik kanan pada file demo apa saja di Explorer dan pilih **"Run Java"**, atau gunakan konfigurasi peluncuran dari panel Run and Debug (pastikan token sudah ditambahkan ke file `.env` dulu).

**Menggunakan Maven:** Alternatifnya, Anda dapat menjalankan dari baris perintah:

### 1. Chat Dasar

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Pola Prompt

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Menampilkan zero-shot, few-shot, chain-of-thought, dan prompt berbasis peran.

### 3. Panggilan Fungsi

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI secara otomatis memanggil metode Java Anda saat dibutuhkan.

### 4. Tanya Jawab Dokumen (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Tanyakan pertanyaan tentang dokumen Anda menggunakan Easy RAG dengan embedding dan pengambilan otomatis.

### 5. AI Bertanggung Jawab

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Lihat bagaimana filter keamanan AI memblokir konten berbahaya.

## Apa yang Ditunjukkan Setiap Contoh

**Chat Dasar** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Mulai di sini untuk melihat LangChain4j dalam bentuk paling sederhana. Anda akan membuat `OpenAiOfficialChatModel`, mengirim prompt dengan `.chat()`, dan mendapatkan respons kembali. Ini menunjukkan fondasi: bagaimana menginisialisasi model dengan endpoint dan kunci API khusus. Setelah memahami pola ini, semuanya dibangun di atasnya.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) dan tanyakan:
> - "Bagaimana saya beralih dari Model GitHub ke Azure OpenAI dalam kode ini?"
> - "Parameter lain apa yang bisa saya konfigurasi di OpenAiOfficialChatModel.builder()?"
> - "Bagaimana cara menambahkan streaming respons alih-alih menunggu respons lengkap?"

**Rekayasa Prompt** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Sekarang Anda tahu cara berkomunikasi dengan model, mari jelajahi apa yang Anda katakan padanya. Demo ini menggunakan setup model yang sama tapi menunjukkan lima pola prompting berbeda. Coba zero-shot untuk instruksi langsung, few-shot yang belajar dari contoh, chain-of-thought yang mengungkap langkah penalaran, dan prompt berbasis peran yang menetapkan konteks. Anda akan melihat bagaimana model yang sama memberikan hasil sangat berbeda tergantung cara Anda mengatur permintaan.

Demo juga memperlihatkan template prompt, yang merupakan cara kuat untuk membuat prompt yang dapat digunakan ulang dengan variabel.
Contoh berikut menunjukkan prompt menggunakan LangChain4j `PromptTemplate` untuk mengisi variabel. AI akan menjawab berdasarkan tujuan dan aktivitas yang diberikan.

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

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) dan tanyakan:
> - "Apa perbedaan antara zero-shot dan few-shot prompting, dan kapan harus menggunakan masing-masing?"
> - "Bagaimana parameter temperature memengaruhi respons model?"
> - "Apa saja teknik untuk mencegah serangan injeksi prompt di produksi?"
> - "Bagaimana cara membuat objek PromptTemplate yang dapat digunakan ulang untuk pola umum?"

**Integrasi Alat** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Di sinilah LangChain4j menjadi kuat. Anda akan menggunakan `AiServices` untuk membuat asisten AI yang dapat memanggil metode Java Anda. Cukup anotasi metode dengan `@Tool("deskripsi")` dan LangChain4j mengurus sisanya — AI secara otomatis memutuskan kapan menggunakan setiap alat berdasarkan permintaan pengguna. Ini menunjukkan panggilan fungsi, teknik utama untuk membangun AI yang bisa mengambil tindakan, bukan hanya menjawab pertanyaan.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) dan tanyakan:
> - "Bagaimana anotasi @Tool bekerja dan apa yang LangChain4j lakukan di balik layar?"
> - "Apakah AI bisa memanggil beberapa alat secara berurutan untuk memecahkan masalah kompleks?"
> - "Apa yang terjadi jika sebuah alat melemparkan pengecualian — bagaimana saya menangani kesalahan?"
> - "Bagaimana cara mengintegrasikan API nyata bukan contoh kalkulator ini?"

**Tanya Jawab Dokumen (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Di sini Anda akan melihat RAG (retrieval-augmented generation) menggunakan pendekatan "Easy RAG" LangChain4j. Dokumen dimuat, secara otomatis dipisah dan diembed ke dalam penyimpanan memori, kemudian pengambil konten memberikan potongan relevan ke AI saat waktu kueri. AI menjawab berdasarkan dokumen Anda, bukan pengetahuan umum model.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) dan tanyakan:
> - "Bagaimana RAG mencegah halusinasi AI dibandingkan menggunakan data pelatihan model?"
> - "Apa perbedaan pendekatan mudah ini dengan pipeline RAG kustom?"
> - "Bagaimana saya mengskalakan ini untuk menangani banyak dokumen atau basis pengetahuan yang lebih besar?"

**AI Bertanggung Jawab** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Bangun keamanan AI dengan pertahanan berlapis. Demo ini menunjukkan dua lapis perlindungan bekerja bersama:

**Bagian 1: LangChain4j Input Guardrails** - Memblokir prompt berbahaya sebelum mencapai LLM. Buat guardrail kustom yang memeriksa kata kunci atau pola terlarang. Ini dijalankan di kode Anda, jadi cepat dan gratis.

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

**Bagian 2: Filter Keamanan Penyedia** - Model GitHub memiliki filter bawaan yang menangkap apa yang mungkin terlewat guardrails Anda. Anda akan melihat blok keras (error HTTP 400) untuk pelanggaran berat dan penolakan lunak di mana AI menolak dengan sopan.

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) dan tanyakan:
> - "Apa itu InputGuardrail dan bagaimana cara membuatnya?"
> - "Apa perbedaan antara blok keras dan penolakan lunak?"
> - "Mengapa menggunakan keduanya, guardrails dan filter penyedia?"

## Langkah Selanjutnya

**Modul Berikutnya:** [01-introduction - Memulai dengan LangChain4j](../01-introduction/README.md)

---

**Navigasi:** [← Kembali ke Utama](../README.md) | [Berikutnya: Module 01 - Pendahuluan →](../01-introduction/README.md)

---

## Pemecahan Masalah

### Build Maven Pertama Kali

**Masalah**: `mvn clean compile` atau `mvn package` awal memakan waktu lama (10-15 menit)

**Penyebab**: Maven perlu mengunduh semua dependensi proyek (Spring Boot, perpustakaan LangChain4j, SDK Azure, dll.) saat build pertama kali.

**Solusi**: Ini perilaku normal. Build berikutnya akan jauh lebih cepat karena dependensi sudah disimpan lokal. Waktu unduh tergantung kecepatan jaringan Anda.

### Sintaks Perintah Maven di PowerShell

**Masalah**: Perintah Maven gagal dengan error `Unknown lifecycle phase ".mainClass=..."`
**Penyebab**: PowerShell mengartikan `=` sebagai operator penugasan variabel, yang merusak sintaks properti Maven

**Solusi**: Gunakan operator stop-parsing `--%` sebelum perintah Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operator `--%` memberi tahu PowerShell untuk meneruskan semua argumen yang tersisa secara literal ke Maven tanpa interpretasi.

### Tampilkan Emoji di Windows PowerShell

**Masalah**: Respon AI menampilkan karakter sampah (misalnya, `????` atau `â??`) alih-alih emoji di PowerShell

**Penyebab**: Encoding default PowerShell tidak mendukung emoji UTF-8

**Solusi**: Jalankan perintah ini sebelum mengeksekusi aplikasi Java:
```cmd
chcp 65001
```

Ini memaksa encoding UTF-8 di terminal. Alternatifnya, gunakan Windows Terminal yang memiliki dukungan Unicode lebih baik.

### Debugging Panggilan API

**Masalah**: Kesalahan autentikasi, batasan rate, atau respon tak terduga dari model AI

**Solusi**: Contoh termasuk `.logRequests(true)` dan `.logResponses(true)` untuk menampilkan panggilan API di konsol. Ini membantu memecahkan masalah kesalahan autentikasi, batasan rate, atau respon tak terduga. Hapus flag ini di produksi untuk mengurangi kebisingan log.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berupaya untuk akurat, harap diingat bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang sah. Untuk informasi yang kritis, disarankan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau salah tafsir yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->