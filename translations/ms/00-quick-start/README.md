# Modul 00: Mula dengan Cepat

## Jadual Kandungan

- [Pengenalan](../../../00-quick-start)
- [Apa itu LangChain4j?](../../../00-quick-start)
- [Kebergantungan LangChain4j](../../../00-quick-start)
- [Prasyarat](../../../00-quick-start)
- [Persediaan](../../../00-quick-start)
  - [1. Dapatkan Token GitHub Anda](../../../00-quick-start)
  - [2. Tetapkan Token Anda](../../../00-quick-start)
- [Jalankan Contoh](../../../00-quick-start)
  - [1. Sembang Asas](../../../00-quick-start)
  - [2. Corak Prompt](../../../00-quick-start)
  - [3. Panggilan Fungsi](../../../00-quick-start)
  - [4. Soal Jawab Dokumen (Easy RAG)](../../../00-quick-start)
  - [5. AI Bertanggungjawab](../../../00-quick-start)
- [Apa yang Ditunjukkan Setiap Contoh](../../../00-quick-start)
- [Langkah Seterusnya](../../../00-quick-start)
- [Penyelesaian Masalah](../../../00-quick-start)

## Pengenalan

Panduan pantas ini bertujuan untuk membantu anda memulakan dengan LangChain4j dengan secepat mungkin. Ia merangkumi asas-asas membina aplikasi AI dengan LangChain4j dan Model GitHub. Dalam modul berikut, anda akan menggunakan Azure OpenAI dengan LangChain4j untuk membina aplikasi yang lebih maju.

## Apa itu LangChain4j?

LangChain4j adalah perpustakaan Java yang memudahkan pembinaan aplikasi berkuasa AI. Daripada melibatkan klien HTTP dan penguraian JSON, anda bekerja dengan API Java yang kemas.

"Chain" dalam LangChain merujuk kepada penyambungan beberapa komponen - anda mungkin menyambungkan prompt ke model ke parser, atau menyambungkan beberapa panggilan AI bersama di mana satu output menjadi input seterusnya. Panduan pantas ini memfokuskan kepada asas sebelum meneroka rantaian yang lebih kompleks.

<img src="../../../translated_images/ms/langchain-concept.ad1fe6cf063515e1.webp" alt="Konsep Penyambungan LangChain4j" width="800"/>

*Menyambungkan komponen dalam LangChain4j - blok binaan yang bersambung untuk mencipta aliran kerja AI yang berkuasa*

Kita akan menggunakan tiga komponen teras:

**ChatModel** - Antara muka untuk interaksi model AI. Panggil `model.chat("prompt")` dan dapatkan respon sebagai rentetan. Kami menggunakan `OpenAiOfficialChatModel` yang berfungsi dengan titik akhir yang serasi OpenAI seperti Model GitHub.

**AiServices** - Membuat antara muka perkhidmatan AI jenis-selamat. Takrifkan kaedah, beri anotasi dengan `@Tool`, dan LangChain4j mengendalikan orkestrasi. AI secara automatik memanggil kaedah Java anda apabila diperlukan.

**MessageWindowChatMemory** - Mengekalkan sejarah perbualan. Tanpanya, setiap permintaan berdikari. Dengan ini, AI mengingati mesej sebelumnya dan mengekalkan konteks sepanjang beberapa giliran.

<img src="../../../translated_images/ms/architecture.eedc993a1c576839.webp" alt="Senibina LangChain4j" width="800"/>

*Senibina LangChain4j - komponen teras bekerjasama untuk menggerakkan aplikasi AI anda*

## Kebergantungan LangChain4j

Panduan pantas ini menggunakan tiga kebergantungan Maven dalam [`pom.xml`](../../../00-quick-start/pom.xml):

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

Modul `langchain4j-open-ai-official` menyediakan kelas `OpenAiOfficialChatModel` yang menyambung ke API yang serasi dengan OpenAI. Model GitHub menggunakan format API yang sama, jadi tiada penyesuai khas diperlukan - hanya tunjukkan URL asas ke `https://models.github.ai/inference`.

Modul `langchain4j-easy-rag` menyediakan pemecahan dokumen automatik, penanaman, dan pencarian supaya anda boleh membina aplikasi RAG tanpa konfigurasi manual setiap langkah.

## Prasyarat

**Menggunakan Kontena Dev?** Java dan Maven sudah dipasang. Anda hanya memerlukan Token Akses Peribadi GitHub.

**Pembangunan Tempatan:**
- Java 21+, Maven 3.9+
- Token Akses Peribadi GitHub (arahan di bawah)

> **Nota:** Modul ini menggunakan `gpt-4.1-nano` daripada Model GitHub. Jangan ubah nama model dalam kod - ia dikonfigurasi untuk berfungsi dengan model yang disediakan oleh GitHub.

## Persediaan

### 1. Dapatkan Token GitHub Anda

1. Pergi ke [Tetapan GitHub → Token Akses Peribadi](https://github.com/settings/personal-access-tokens)
2. Klik "Jana token baru"
3. Tetapkan nama deskriptif (contoh, "Demo LangChain4j")
4. Tetapkan tamat tempoh (disyorkan 7 hari)
5. Di bawah "Kebenaran akaun", cari "Models" dan tetapkan kepada "Baca Sahaja"
6. Klik "Jana token"
7. Salin dan simpan token anda - anda tidak akan melihatnya lagi

### 2. Tetapkan Token Anda

**Pilihan 1: Menggunakan VS Code (Disyorkan)**

Jika anda menggunakan VS Code, tambah token anda ke fail `.env` di akar projek:

Jika fail `.env` tidak wujud, salin `.env.example` ke `.env` atau buat fail `.env` baru di akar projek.

**Contoh fail `.env`:**
```bash
# Dalam /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Kemudian anda hanya boleh klik kanan mana-mana fail demo (contoh, `BasicChatDemo.java`) dalam Explorer dan pilih **"Run Java"** atau gunakan konfigurasi pelancaran dari panel Jalankan dan Debug.

**Pilihan 2: Menggunakan Terminal**

Tetapkan token sebagai pembolehubah persekitaran:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Jalankan Contoh

**Menggunakan VS Code:** Klik kanan pada mana-mana fail demo dalam Explorer dan pilih **"Run Java"**, atau gunakan konfigurasi pelancaran dari panel Jalankan dan Debug (pastikan anda telah tambah token kepada fail `.env` terlebih dahulu).

**Menggunakan Maven:** Sebagai alternatif, anda boleh jalankan dari baris arahan:

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

### 4. Soal Jawab Dokumen (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Tanya soalan tentang dokumen anda menggunakan Easy RAG dengan penanaman dan pencarian automatik.

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

## Apa yang Ditunjukkan Setiap Contoh

**Sembang Asas** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Mulakan di sini untuk melihat LangChain4j pada tahap paling asas. Anda akan cipta `OpenAiOfficialChatModel`, hantar prompt dengan `.chat()`, dan dapatkan respon kembali. Ini menunjukkan asas: bagaimana untuk inisialisasi model dengan titik akhir dan kunci API tersuai. Setelah faham corak ini, semuanya dibina di atasnya.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) dan tanya:
> - "Bagaimana saya bertukar dari Model GitHub ke Azure OpenAI dalam kod ini?"
> - "Apa parameter lain yang boleh saya konfigurasikan dalam OpenAiOfficialChatModel.builder()?"
> - "Bagaimana cara saya tambah balasan penstriman dan bukannya menunggu balasan lengkap?"

**Kejuruteraan Prompt** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Sekarang anda tahu bagaimana bercakap dengan model, mari terokai apa yang anda katakan kepadanya. Demo ini menggunakan konfigurasi model yang sama tetapi menunjukkan lima corak prompting berbeza. Cuba zero-shot untuk arahan langsung, few-shot yang belajar dari contoh, chain-of-thought yang mendedahkan langkah penalaran, dan prompt berasaskan peranan yang menetapkan konteks. Anda akan lihat bagaimana model yang sama memberikan hasil berbeza secara dramatik bergantung pada bagaimana anda merangka permintaan.

Demo juga menunjukkan templat prompt, cara yang berkuasa untuk mencipta prompt boleh guna semula dengan pembolehubah.
Contoh di bawah menunjukkan prompt menggunakan LangChain4j `PromptTemplate` untuk mengisi pembolehubah. AI akan menjawab berdasarkan destinasi dan aktiviti yang diberikan.

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
> - "Apakah perbezaan antara zero-shot dan few-shot prompting, dan bila saya patut guna masing-masing?"
> - "Bagaimana parameter suhu mempengaruhi respons model?"
> - "Apa teknik untuk mengelakkan serangan suntikan prompt dalam produksi?"
> - "Bagaimana saya cipta objek PromptTemplate boleh guna semula untuk corak biasa?"

**Integrasi Alat** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Di sini LangChain4j menjadi berkuasa. Anda akan gunakan `AiServices` untuk cipta pembantu AI yang boleh memanggil kaedah Java anda. Cuma beri anotasi kaedah dengan `@Tool("deskripsi")` dan LangChain4j mengurus selebihnya - AI secara automatik memutuskan bila guna setiap alat berdasarkan apa yang pengguna tanya. Ini menunjukkan panggilan fungsi, teknik utama untuk membina AI yang boleh mengambil tindakan, bukan hanya menjawab soalan.

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

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) dan tanya:
> - "Bagaimana anotasi @Tool berfungsi dan apa yang LangChain4j lakukan dengannya di belakang tabir?"
> - "Bolehkah AI panggil pelbagai alat secara berurutan untuk menyelesaikan masalah kompleks?"
> - "Apa yang berlaku jika alat membuang pengecualian - bagaimana saya kendali ralat?"
> - "Bagaimana saya integrasi API sebenar bukannya contoh kalkulator ini?"

**Soal Jawab Dokumen (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Di sini anda akan lihat RAG (penjanaan yang dipertingkatkan pengambilan) menggunakan pendekatan "Easy RAG" LangChain4j. Dokumen dimuat, dipecah secara automatik dan distanapkan ke dalam kedai memori, kemudian pencari kandungan membekalkan potongan relevan kepada AI pada masa pertanyaan. AI menjawab berdasarkan dokumen anda, bukan pengetahuan amnya.

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

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) dan tanya:
> - "Bagaimana RAG mengelakkan halusinasi AI berbanding menggunakan data latihan model?"
> - "Apakah perbezaan antara pendekatan mudah ini dengan saluran RAG tersuai?"
> - "Bagaimana saya skala ini untuk kendali banyak dokumen atau pangkalan pengetahuan yang lebih besar?"

**AI Bertanggungjawab** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Bina keselamatan AI dengan pertahanan bertingkat. Demo ini menunjukkan dua lapisan perlindungan bekerjasama:

**Bahagian 1: LangChain4j Input Guardrails** - Sekat prompt berbahaya sebelum sampai ke LLM. Cipta guardrail tersuai yang memeriksa kata kunci atau corak yang dilarang. Ia berjalan dalam kod anda, jadi pantas dan percuma.

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

**Bahagian 2: Penapis Keselamatan Penyedia** - Model GitHub mempunyai penapis terbina dalam yang menangkap apa yang mungkin terlepas dari guardrail anda. Anda akan nampak sekatan keras (ralat HTTP 400) untuk pelanggaran serius dan penolakan lembut di mana AI menolak secara sopan.

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) dan tanya:
> - "Apakah InputGuardrail dan bagaimana saya cipta sendiri?"
> - "Apakah perbezaan antara sekatan keras dan penolakan lembut?"
> - "Kenapa guna kedua-dua guardrail dan penapis penyedia bersama-sama?"

## Langkah Seterusnya

**Modul Seterusnya:** [01-introduction - Bermula dengan LangChain4j dan gpt-5 di Azure](../01-introduction/README.md)

---

**Navigasi:** [← Kembali ke Utama](../README.md) | [Seterusnya: Modul 01 - Pengenalan →](../01-introduction/README.md)

---

## Penyelesaian Masalah

### Build Maven Kali Pertama

**Isu:** `mvn clean compile` atau `mvn package` awal mengambil masa lama (10-15 minit)

**Punca:** Maven perlu muat turun semua kebergantungan projek (Spring Boot, perpustakaan LangChain4j, SDK Azure, dll) pada build pertama.

**Penyelesaian:** Ini adalah kelakuan biasa. Build seterusnya akan jauh lebih pantas kerana kebergantungan disimpan dalam cache tempatan. Masa muat turun bergantung pada kelajuan rangkaian anda.

### Sintaks Perintah PowerShell Maven

**Isu:** Perintah Maven gagal dengan ralat `Unknown lifecycle phase ".mainClass=..."`
**Sebab**: PowerShell mentafsir `=` sebagai operator penetapan pembolehubah, yang memecahkan sintaks sifat Maven

**Penyelesaian**: Gunakan operator hentian-penguraian `--%` sebelum arahan Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operator `--%` memberitahu PowerShell untuk menghantar semua argumen yang tinggal secara literal kepada Maven tanpa tafsiran.

### Paparan Emoji Windows PowerShell

**Isu**: Respons AI menunjukkan aksara sampah (contohnya, `????` atau `â??`) bukannya emoji dalam PowerShell

**Sebab**: Pengekodan lalai PowerShell tidak menyokong emoji UTF-8

**Penyelesaian**: Jalankan arahan ini sebelum melaksanakan aplikasi Java:
```cmd
chcp 65001
```

Ini memaksa pengekodan UTF-8 dalam terminal. Sebagai alternatif, gunakan Windows Terminal yang mempunyai sokongan Unicode yang lebih baik.

### Menyahpepijat Panggilan API

**Isu**: Ralat pengesahan, had kadar, atau respons tidak dijangka daripada model AI

**Penyelesaian**: Contoh-contoh termasuk `.logRequests(true)` dan `.logResponses(true)` untuk menunjukkan panggilan API dalam konsol. Ini membantu menyelesaikan masalah ralat pengesahan, had kadar, atau respons tidak dijangka. Buang penanda ini dalam pengeluaran untuk mengurangkan bunyi log.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila ambil perhatian bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang sahih. Untuk maklumat penting, terjemahan profesional oleh manusia adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->