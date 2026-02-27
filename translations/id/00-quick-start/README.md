# Modul 00: Mulai Cepat

## Daftar Isi

- [Pendahuluan](../../../00-quick-start)
- [Apa itu LangChain4j?](../../../00-quick-start)
- [Dependensi LangChain4j](../../../00-quick-start)
- [Prasyarat](../../../00-quick-start)
- [Pengaturan](../../../00-quick-start)
  - [1. Dapatkan Token GitHub Anda](../../../00-quick-start)
  - [2. Setel Token Anda](../../../00-quick-start)
- [Jalankan Contoh](../../../00-quick-start)
  - [1. Obrolan Dasar](../../../00-quick-start)
  - [2. Pola Prompt](../../../00-quick-start)
  - [3. Pemanggilan Fungsi](../../../00-quick-start)
  - [4. T&J Dokumen (Easy RAG)](../../../00-quick-start)
  - [5. AI Bertanggung Jawab](../../../00-quick-start)
- [Apa yang Dipertunjukkan Setiap Contoh](../../../00-quick-start)
- [Langkah Berikutnya](../../../00-quick-start)
- [Pemecahan Masalah](../../../00-quick-start)

## Pendahuluan

Quickstart ini dimaksudkan agar Anda dapat segera memulai dengan LangChain4j secepat mungkin. Ini mencakup dasar-dasar mutlak membangun aplikasi AI dengan LangChain4j dan Model GitHub. Dalam modul berikutnya Anda akan menggunakan Azure OpenAI dengan LangChain4j untuk membangun aplikasi yang lebih maju.

## Apa itu LangChain4j?

LangChain4j adalah perpustakaan Java yang menyederhanakan pembangunan aplikasi bertenaga AI. Alih-alih berurusan dengan klien HTTP dan parsing JSON, Anda bekerja dengan API Java yang bersih.

"Chain" dalam LangChain mengacu pada penggabungan beberapa komponen - Anda bisa menggabungkan prompt ke model ke parser, atau menggabungkan beberapa panggilan AI di mana output satu menjadi input berikutnya. Mulai cepat ini fokus pada dasar-dasar sebelum mengeksplorasi rantai yang lebih kompleks.

<img src="../../../translated_images/id/langchain-concept.ad1fe6cf063515e1.webp" alt="Konsep Penggabungan LangChain4j" width="800"/>

*Menggabungkan komponen dalam LangChain4j - blok bangunan yang terhubung untuk menciptakan alur kerja AI yang kuat*

Kita akan menggunakan tiga komponen inti:

**ChatModel** - Antarmuka untuk interaksi model AI. Panggil `model.chat("prompt")` dan dapatkan string respons. Kami menggunakan `OpenAiOfficialChatModel` yang bekerja dengan endpoint kompatibel OpenAI seperti Model GitHub.

**AiServices** - Membuat antarmuka layanan AI yang aman tipe. Definisikan metode, anotasi dengan `@Tool`, dan LangChain4j mengatur orkestrasi. AI secara otomatis memanggil metode Java Anda saat dibutuhkan.

**MessageWindowChatMemory** - Memelihara riwayat percakapan. Tanpa ini, setiap permintaan berdiri sendiri. Dengan ini, AI mengingat pesan sebelumnya dan mempertahankan konteks selama beberapa putaran.

<img src="../../../translated_images/id/architecture.eedc993a1c576839.webp" alt="Arsitektur LangChain4j" width="800"/>

*Arsitektur LangChain4j - komponen inti bekerja sama untuk mendukung aplikasi AI Anda*

## Dependensi LangChain4j

Quickstart ini menggunakan tiga dependensi Maven dalam [`pom.xml`](../../../00-quick-start/pom.xml):

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

Modul `langchain4j-open-ai-official` menyediakan kelas `OpenAiOfficialChatModel` yang menghubungkan ke API kompatibel OpenAI. Model GitHub menggunakan format API yang sama, jadi tidak diperlukan adaptor khusus - cukup arahkan URL dasar ke `https://models.github.ai/inference`.

Modul `langchain4j-easy-rag` menyediakan pemisahan dokumen otomatis, embedding, dan pengambilan sehingga Anda dapat membangun aplikasi RAG tanpa konfigurasi manual setiap langkah.

## Prasyarat

**Menggunakan Dev Container?** Java dan Maven sudah terpasang. Anda hanya memerlukan Token Akses Personal GitHub.

**Pengembangan Lokal:**
- Java 21+, Maven 3.9+
- Token Akses Personal GitHub (instruksi di bawah)

> **Catatan:** Modul ini menggunakan `gpt-4.1-nano` dari Model GitHub. Jangan mengubah nama model dalam kode - sudah dikonfigurasi untuk bekerja dengan model yang tersedia di GitHub.

## Pengaturan

### 1. Dapatkan Token GitHub Anda

1. Kunjungi [Pengaturan GitHub → Token Akses Personal](https://github.com/settings/personal-access-tokens)
2. Klik "Generate new token"
3. Berikan nama deskriptif (misalnya, "LangChain4j Demo")
4. Atur masa berlaku (7 hari direkomendasikan)
5. Di bawah "Account permissions", cari "Models" dan atur sebagai "Read-only"
6. Klik "Generate token"
7. Salin dan simpan token Anda - Anda tidak akan melihatnya lagi

### 2. Setel Token Anda

**Opsi 1: Menggunakan VS Code (Direkomendasikan)**

Jika menggunakan VS Code, tambahkan token Anda ke file `.env` di root proyek:

Jika file `.env` tidak ada, salin `.env.example` ke `.env` atau buat file `.env` baru di root proyek.

**Contoh file `.env`:**
```bash
# Di /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Kemudian Anda cukup klik kanan pada file demo apa pun (misalnya `BasicChatDemo.java`) di Explorer dan pilih **"Run Java"** atau gunakan konfigurasi peluncuran dari panel Run and Debug.

**Opsi 2: Menggunakan Terminal**

Setel token sebagai variabel lingkungan:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Jalankan Contoh

**Menggunakan VS Code:** Cukup klik kanan pada file demo apa pun di Explorer dan pilih **"Run Java"**, atau gunakan konfigurasi peluncuran dari panel Run and Debug (pastikan sudah menambahkan token ke file `.env` terlebih dahulu).

**Menggunakan Maven:** Alternatifnya, Anda bisa menjalankan dari baris perintah:

### 1. Obrolan Dasar

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

### 3. Pemanggilan Fungsi

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI secara otomatis memanggil metode Java Anda saat dibutuhkan.

### 4. T&J Dokumen (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Ajukan pertanyaan tentang dokumen Anda menggunakan Easy RAG dengan embedding dan pengambilan otomatis.

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

## Apa yang Dipertunjukkan Setiap Contoh

**Obrolan Dasar** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Mulailah di sini untuk melihat LangChain4j dalam bentuk paling sederhana. Anda akan membuat `OpenAiOfficialChatModel`, mengirim prompt dengan `.chat()`, dan mendapatkan respons. Ini menunjukkan fondasi: bagaimana menginisialisasi model dengan endpoint dan kunci API khusus. Setelah Anda mengerti pola ini, semuanya dibangun di atasnya.

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
> - "Parameter lain apa yang bisa saya konfigurasikan di OpenAiOfficialChatModel.builder()?"
> - "Bagaimana cara menambahkan streaming respons daripada menunggu respons lengkap?"

**Rekayasa Prompt** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Sekarang setelah Anda tahu cara berbicara dengan model, mari jelajahi apa yang Anda katakan padanya. Demo ini menggunakan setup model yang sama tapi menunjukkan lima pola prompting yang berbeda. Coba prompt zero-shot untuk instruksi langsung, prompt few-shot yang belajar dari contoh, prompt chain-of-thought yang mengungkap langkah penalaran, dan prompt berbasis peran yang menetapkan konteks. Anda akan melihat bagaimana model yang sama memberikan hasil sangat berbeda berdasarkan cara Anda membingkai permintaan.

Demo ini juga menunjukkan template prompt, yang merupakan cara kuat untuk membuat prompt yang dapat digunakan ulang dengan variabel.
Contoh di bawah ini menunjukkan prompt menggunakan `PromptTemplate` LangChain4j untuk mengisi variabel. AI akan menjawab berdasarkan destinasi dan aktivitas yang diberikan.

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
> - "Apa perbedaan antara zero-shot dan few-shot prompting, dan kapan saya harus menggunakan masing-masing?"
> - "Bagaimana parameter temperature mempengaruhi respons model?"
> - "Apa saja teknik untuk mencegah serangan prompt injection di produksi?"
> - "Bagaimana cara membuat objek PromptTemplate yang dapat digunakan ulang untuk pola umum?"

**Integrasi Alat** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Di sinilah LangChain4j menjadi kuat. Anda akan menggunakan `AiServices` untuk membuat asisten AI yang dapat memanggil metode Java Anda. Cukup anotasi metode dengan `@Tool("deskripsi")` dan LangChain4j menangani sisanya - AI secara otomatis memutuskan kapan menggunakan setiap alat berdasarkan apa yang diminta pengguna. Ini menunjukkan pemanggilan fungsi, teknik kunci untuk membangun AI yang dapat mengambil tindakan, bukan hanya menjawab pertanyaan.

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
> - "Bagaimana anotasi @Tool bekerja dan apa yang dilakukan LangChain4j di belakang layar?"
> - "Bisakah AI memanggil beberapa alat berurutan untuk menyelesaikan masalah kompleks?"
> - "Apa yang terjadi jika alat melempar pengecualian - bagaimana saya harus menangani error?"
> - "Bagaimana cara mengintegrasikan API nyata alih-alih contoh kalkulator ini?"

**T&J Dokumen (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Di sini Anda akan melihat RAG (retrieval-augmented generation) menggunakan pendekatan "Easy RAG" LangChain4j. Dokumen dimuat, otomatis dipisah dan di-embed ke dalam penyimpanan memori, lalu pengambil konten menyediakan potongan relevan kepada AI saat kueri. AI menjawab berdasarkan dokumen Anda, bukan pengetahuan umum model.

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
> - "Apa bedanya pendekatan mudah ini dengan pipeline RAG kustom?"
> - "Bagaimana saya skala ini untuk menangani banyak dokumen atau basis pengetahuan yang lebih besar?"

**AI Bertanggung Jawab** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Bangun keamanan AI dengan pertahanan berlapis. Demo ini menunjukkan dua lapisan perlindungan yang bekerja sama:

**Bagian 1: LangChain4j Input Guardrails** - Blokir prompt berbahaya sebelum mencapai LLM. Buat guardrails kustom yang memeriksa kata kunci atau pola terlarang. Ini berjalan di kode Anda, jadi cepat dan gratis.

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

**Bagian 2: Filter Keamanan Penyedia** - Model GitHub memiliki filter bawaan yang menangkap apa yang mungkin terlewatkan guardrail Anda. Anda akan melihat blok keras (error HTTP 400) untuk pelanggaran berat dan penolakan lunak di mana AI sopan menolak.

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) dan tanyakan:
> - "Apa itu InputGuardrail dan bagaimana saya membuatnya sendiri?"
> - "Apa perbedaan blok keras dan penolakan lunak?"
> - "Kenapa memakai guardrails dan filter penyedia sekaligus?"

## Langkah Berikutnya

**Modul Berikutnya:** [01-introduction - Memulai dengan LangChain4j dan gpt-5 di Azure](../01-introduction/README.md)

---

**Navigasi:** [← Kembali ke Utama](../README.md) | [Berikutnya: Modul 01 - Pendahuluan →](../01-introduction/README.md)

---

## Pemecahan Masalah

### Build Maven Pertama Kali

**Masalah**: `mvn clean compile` atau `mvn package` pertama kali membutuhkan waktu lama (10-15 menit)

**Penyebab**: Maven perlu mengunduh semua dependensi proyek (Spring Boot, perpustakaan LangChain4j, SDK Azure, dll.) saat build pertama.

**Solusi**: Ini perilaku normal. Build berikutnya akan jauh lebih cepat karena dependensi sudah di-cache secara lokal. Waktu unduh bergantung pada kecepatan jaringan Anda.

### Sintaks Perintah Maven di PowerShell

**Masalah**: Perintah Maven gagal dengan error `Unknown lifecycle phase ".mainClass=..."`
**Penyebab**: PowerShell mengartikan `=` sebagai operator penugasan variabel, sehingga memecah sintaks properti Maven

**Solusi**: Gunakan operator berhenti-memparsing `--%` sebelum perintah Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operator `--%` memberitahu PowerShell untuk meneruskan semua argumen yang tersisa secara literal ke Maven tanpa interpretasi.

### Tampilan Emoji Windows PowerShell

**Masalah**: Respons AI menampilkan karakter kacau (misalnya, `????` atau `â??`) alih-alih emoji di PowerShell

**Penyebab**: Encoding default PowerShell tidak mendukung emoji UTF-8

**Solusi**: Jalankan perintah ini sebelum menjalankan aplikasi Java:
```cmd
chcp 65001
```

Ini memaksa encoding UTF-8 di terminal. Alternatifnya, gunakan Windows Terminal yang memiliki dukungan Unicode lebih baik.

### Debugging Panggilan API

**Masalah**: Kesalahan otentikasi, batasan rate, atau respons tak terduga dari model AI

**Solusi**: Contoh-contoh tersebut menyertakan `.logRequests(true)` dan `.logResponses(true)` untuk menampilkan panggilan API di konsol. Ini membantu memecahkan masalah kesalahan otentikasi, batasan rate, atau respons yang tidak terduga. Hapus flag ini di produksi untuk mengurangi kebisingan log.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berupaya untuk akurat, harap diketahui bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang sah. Untuk informasi penting, disarankan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau salah tafsir yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->