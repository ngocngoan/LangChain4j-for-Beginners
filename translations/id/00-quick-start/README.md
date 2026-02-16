# Module 00: Memulai Cepat

## Daftar Isi

- [Pengenalan](../../../00-quick-start)
- [Apa itu LangChain4j?](../../../00-quick-start)
- [Ketergantungan LangChain4j](../../../00-quick-start)
- [Prasyarat](../../../00-quick-start)
- [Persiapan](../../../00-quick-start)
  - [1. Dapatkan Token GitHub Anda](../../../00-quick-start)
  - [2. Setel Token Anda](../../../00-quick-start)
- [Jalankan Contoh-contoh](../../../00-quick-start)
  - [1. Chat Dasar](../../../00-quick-start)
  - [2. Pola Prompt](../../../00-quick-start)
  - [3. Pemanggilan Fungsi](../../../00-quick-start)
  - [4. Tanya Jawab Dokumen (RAG)](../../../00-quick-start)
  - [5. AI Bertanggung Jawab](../../../00-quick-start)
- [Apa yang Ditunjukkan Setiap Contoh](../../../00-quick-start)
- [Langkah Berikutnya](../../../00-quick-start)
- [Pemecahan Masalah](../../../00-quick-start)

## Pengenalan

Memulai cepat ini dimaksudkan agar Anda dapat langsung menggunakan LangChain4j dengan cepat. Ini mencakup dasar-dasar membangun aplikasi AI dengan LangChain4j dan Model GitHub. Dalam modul berikutnya Anda akan menggunakan Azure OpenAI dengan LangChain4j untuk membangun aplikasi yang lebih maju.

## Apa itu LangChain4j?

LangChain4j adalah perpustakaan Java yang menyederhanakan pembangunan aplikasi yang didukung AI. Alih-alih berurusan dengan klien HTTP dan parsing JSON, Anda bekerja dengan API Java yang bersih.

"Kait" dalam LangChain merujuk pada penggabungan beberapa komponen — Anda mungkin mengaitkan prompt ke model ke parser, atau mengaitkan beberapa panggilan AI di mana keluaran satu menjadi masukan berikutnya. Memulai cepat ini fokus pada dasar-dasar sebelum mengeksplor rantai yang lebih kompleks.

<img src="../../../translated_images/id/langchain-concept.ad1fe6cf063515e1.webp" alt="Konsep Kait LangChain4j" width="800"/>

*Mengaitkan komponen dalam LangChain4j - blok bangunan yang saling terhubung untuk membuat alur kerja AI yang kuat*

Kami menggunakan tiga komponen inti:

**ChatLanguageModel** - Antarmuka untuk interaksi model AI. Panggil `model.chat("prompt")` dan dapatkan string respons. Kami menggunakan `OpenAiOfficialChatModel` yang bekerja dengan endpoint yang kompatibel dengan OpenAI seperti Model GitHub.

**AiServices** - Membuat antarmuka layanan AI yang tipe-aman. Definisikan metode, beri anotasi dengan `@Tool`, dan LangChain4j mengatur orkestrasinya. AI secara otomatis memanggil metode Java Anda saat diperlukan.

**MessageWindowChatMemory** - Memelihara riwayat percakapan. Tanpa ini, setiap permintaan berdiri sendiri. Dengan ini, AI mengingat pesan sebelumnya dan menjaga konteks selama beberapa putaran.

<img src="../../../translated_images/id/architecture.eedc993a1c576839.webp" alt="Arsitektur LangChain4j" width="800"/>

*Arsitektur LangChain4j - komponen inti bekerja bersama untuk memberi daya pada aplikasi AI Anda*

## Ketergantungan LangChain4j

Memulai cepat ini menggunakan dua ketergantungan Maven dalam [`pom.xml`](../../../00-quick-start/pom.xml):

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

Modul `langchain4j-open-ai-official` menyediakan kelas `OpenAiOfficialChatModel` yang terhubung ke API yang kompatibel dengan OpenAI. Model GitHub menggunakan format API yang sama, jadi tidak perlu adaptor khusus - cukup arahkan URL basis ke `https://models.github.ai/inference`.

## Prasyarat

**Menggunakan Dev Container?** Java dan Maven sudah terpasang. Anda hanya perlu Token Akses Pribadi GitHub.

**Pengembangan Lokal:**
- Java 21+, Maven 3.9+
- Token Akses Pribadi GitHub (petunjuk di bawah)

> **Catatan:** Modul ini menggunakan `gpt-4.1-nano` dari Model GitHub. Jangan ubah nama model dalam kode - sudah dikonfigurasi untuk bekerja dengan model yang tersedia di GitHub.

## Persiapan

### 1. Dapatkan Token GitHub Anda

1. Pergi ke [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Klik "Generate new token"
3. Beri nama yang deskriptif (mis. "Demo LangChain4j")
4. Atur masa berlaku (disarankan 7 hari)
5. Di bawah "Account permissions", cari "Models" dan setel ke "Read-only"
6. Klik "Generate token"
7. Salin dan simpan token Anda — Anda tidak akan melihatnya lagi

### 2. Setel Token Anda

**Opsi 1: Menggunakan VS Code (Disarankan)**

Jika menggunakan VS Code, tambahkan token ke file `.env` di root proyek:

Jika file `.env` tidak ada, salin `.env.example` ke `.env` atau buat file `.env` baru di root proyek.

**Contoh file `.env`:**
```bash
# Di /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Kemudian Anda cukup klik kanan pada file demo mana saja (misalnya `BasicChatDemo.java`) di Explorer dan pilih **"Run Java"** atau gunakan konfigurasi peluncuran dari panel Run and Debug.

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

## Jalankan Contoh-contoh

**Menggunakan VS Code:** Klik kanan pada file demo mana saja di Explorer dan pilih **"Run Java"**, atau gunakan konfigurasi peluncuran dari panel Run and Debug (pastikan token sudah Anda tambahkan ke file `.env`).

**Menggunakan Maven:** Sebagai alternatif, jalankan dari baris perintah:

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

### 3. Pemanggilan Fungsi

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI secara otomatis memanggil metode Java Anda saat diperlukan.

### 4. Tanya Jawab Dokumen (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Ajukan pertanyaan tentang isi `document.txt`.

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

Mulailah di sini untuk melihat LangChain4j dalam bentuk paling sederhana. Anda akan membuat `OpenAiOfficialChatModel`, mengirim prompt dengan `.chat()`, dan mendapatkan respons. Ini menunjukkan dasar: bagaimana menginisialisasi model dengan endpoint dan kunci API khusus. Setelah memahami pola ini, semuanya akan dibangun di atasnya.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Coba dengan Chat [GitHub Copilot](https://github.com/features/copilot):** Buka [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) dan tanyakan:
> - "Bagaimana saya beralih dari Model GitHub ke Azure OpenAI di kode ini?"
> - "Parameter lain apa yang bisa saya atur di OpenAiOfficialChatModel.builder()?"
> - "Bagaimana saya menambahkan respons streaming alih-alih menunggu respons selesai?"

**Rekayasa Prompt** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Sekarang Anda tahu cara berbicara ke model, mari jelajahi apa yang Anda katakan. Demo ini menggunakan pengaturan model yang sama tetapi menunjukkan lima pola prompt yang berbeda. Coba prompt zero-shot untuk instruksi langsung, prompt few-shot yang belajar dari contoh, prompt chain-of-thought yang mengungkapkan langkah penalaran, dan prompt berbasis peran yang mengatur konteks. Anda akan melihat bagaimana model yang sama memberikan hasil yang sangat berbeda berdasarkan cara Anda menyusun permintaan.

Demo ini juga menunjukkan template prompt, cara yang kuat untuk membuat prompt yang dapat digunakan ulang dengan variabel. Contoh di bawah menunjukkan prompt menggunakan `PromptTemplate` LangChain4j untuk mengisi variabel. AI akan menjawab berdasarkan tujuan dan aktivitas yang diberikan.

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

> **🤖 Coba dengan Chat [GitHub Copilot](https://github.com/features/copilot):** Buka [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) dan tanyakan:
> - "Apa perbedaan antara zero-shot dan few-shot prompting, dan kapan saya harus menggunakan masing-masing?"
> - "Bagaimana parameter temperature memengaruhi respons model?"
> - "Apa beberapa teknik untuk mencegah serangan injeksi prompt dalam produksi?"
> - "Bagaimana saya membuat objek PromptTemplate yang dapat digunakan ulang untuk pola umum?"

**Integrasi Alat** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Di sinilah LangChain4j menjadi kuat. Anda akan menggunakan `AiServices` untuk membuat asisten AI yang dapat memanggil metode Java Anda. Cukup beri anotasi metode dengan `@Tool("deskripsi")` dan LangChain4j mengatur sisanya — AI secara otomatis menentukan kapan menggunakan setiap alat berdasarkan apa yang pengguna minta. Ini menunjukkan pemanggilan fungsi, teknik utama untuk membangun AI yang bisa mengambil tindakan, bukan hanya menjawab pertanyaan.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Coba dengan Chat [GitHub Copilot](https://github.com/features/copilot):** Buka [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) dan tanyakan:
> - "Bagaimana anotasi @Tool bekerja dan apa yang dilakukan LangChain4j di belakang layar?"
> - "Bisakah AI memanggil beberapa alat berurutan untuk menyelesaikan masalah kompleks?"
> - "Apa yang terjadi jika sebuah alat melempar pengecualian - bagaimana saya menangani kesalahan?"
> - "Bagaimana saya mengintegrasikan API nyata alih-alih contoh kalkulator ini?"

**Tanya Jawab Dokumen (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Di sini Anda akan melihat dasar RAG (retrieval-augmented generation). Alih-alih mengandalkan data pelatihan model, Anda memuat isi dari [`document.txt`](../../../00-quick-start/document.txt) dan menyertakannya dalam prompt. AI menjawab berdasarkan dokumen Anda, bukan pengetahuan umum model. Ini adalah langkah pertama menuju pembangunan sistem yang dapat bekerja dengan data Anda sendiri.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Catatan:** Pendekatan sederhana ini memuat seluruh dokumen ke dalam prompt. Untuk file besar (>10KB), Anda akan melewati batas konteks. Modul 03 membahas chunking dan pencarian vektor untuk sistem RAG produksi.

> **🤖 Coba dengan Chat [GitHub Copilot](https://github.com/features/copilot):** Buka [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) dan tanyakan:
> - "Bagaimana RAG mencegah halusinasi AI dibanding menggunakan data pelatihan model?"
> - "Apa perbedaan antara pendekatan sederhana ini dan menggunakan embedding vektor untuk pencarian?"
> - "Bagaimana saya bisa skala ini untuk menangani banyak dokumen atau basis pengetahuan besar?"
> - "Apa praktik terbaik untuk menyusun prompt agar AI hanya menggunakan konteks yang diberikan?"

**AI Bertanggung Jawab** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Bangun keamanan AI dengan pertahanan berlapis. Demo ini menunjukkan dua lapis perlindungan yang bekerja bersama:

**Bagian 1: Input Guardrails LangChain4j** - Memblokir prompt berbahaya sebelum mencapai LLM. Buat guardrail kustom yang memeriksa kata kunci atau pola terlarang. Ini dijalankan dalam kode Anda, sehingga cepat dan gratis.

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

**Bagian 2: Filter Keamanan Penyedia** - Model GitHub memiliki filter bawaan yang menangkap apa yang mungkin terlewat guardrail Anda. Anda akan melihat blok keras (error HTTP 400) untuk pelanggaran berat dan penolakan lunak di mana AI menolak dengan sopan.

> **🤖 Coba dengan Chat [GitHub Copilot](https://github.com/features/copilot):** Buka [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) dan tanyakan:
> - "Apa itu InputGuardrail dan bagaimana membuat guardrail sendiri?"
> - "Apa perbedaan blok keras dengan penolakan lunak?"
> - "Mengapa menggunakan guardrails dan filter penyedia bersama-sama?"

## Langkah Berikutnya

**Modul Berikutnya:** [01-introduction - Memulai dengan LangChain4j dan gpt-5 di Azure](../01-introduction/README.md)

---

**Navigasi:** [← Kembali ke Utama](../README.md) | [Berikutnya: Modul 01 - Pengenalan →](../01-introduction/README.md)

---

## Pemecahan Masalah

### Build Maven Pertama Kali

**Masalah**: `mvn clean compile` atau `mvn package` awal memakan waktu lama (10-15 menit)

**Penyebab**: Maven perlu mengunduh semua ketergantungan proyek (Spring Boot, pustaka LangChain4j, SDK Azure, dll.) pada build pertama.

**Solusi**: Ini adalah perilaku normal. Build berikutnya akan jauh lebih cepat karena ketergantungan sudah di-cache secara lokal. Waktu unduh tergantung pada kecepatan jaringan Anda.
### Sintaks Perintah Maven PowerShell

**Masalah**: Perintah Maven gagal dengan error `Unknown lifecycle phase ".mainClass=..."`

**Penyebab**: PowerShell mengartikan `=` sebagai operator penugasan variabel, yang merusak sintaks properti Maven

**Solusi**: Gunakan operator berhenti-parsing `--%` sebelum perintah Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operator `--%` memberitahu PowerShell untuk meneruskan semua argumen yang tersisa secara literal ke Maven tanpa interpretasi.

### Tampilkan Emoji di Windows PowerShell

**Masalah**: Respon AI menampilkan karakter sampah (misalnya `????` atau `â??`) alih-alih emoji di PowerShell

**Penyebab**: Encoding default PowerShell tidak mendukung emoji UTF-8

**Solusi**: Jalankan perintah ini sebelum menjalankan aplikasi Java:
```cmd
chcp 65001
```

Ini memaksa encoding UTF-8 di terminal. Sebagai alternatif, gunakan Windows Terminal yang memiliki dukungan Unicode lebih baik.

### Debugging Panggilan API

**Masalah**: Kesalahan autentikasi, batasan laju, atau respon tak terduga dari model AI

**Solusi**: Contoh menyertakan `.logRequests(true)` dan `.logResponses(true)` untuk menampilkan panggilan API di konsol. Ini membantu memecahkan masalah kesalahan autentikasi, batasan laju, atau respon tak terduga. Hapus flag ini di produksi untuk mengurangi kebisingan log.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berupaya untuk mencapai akurasi, harap diperhatikan bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang sah. Untuk informasi penting, disarankan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau interpretasi yang salah yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->