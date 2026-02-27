# Modul 03: RAG (Retrieval-Augmented Generation)

## Daftar Isi

- [Video Walkthrough](../../../03-rag)
- [Apa yang Akan Anda Pelajari](../../../03-rag)
- [Prasyarat](../../../03-rag)
- [Memahami RAG](../../../03-rag)
  - [Pendekatan RAG Mana yang Digunakan Tutorial Ini?](../../../03-rag)
- [Cara Kerjanya](../../../03-rag)
  - [Pemrosesan Dokumen](../../../03-rag)
  - [Membuat Embedding](../../../03-rag)
  - [Pencarian Semantik](../../../03-rag)
  - [Generasi Jawaban](../../../03-rag)
- [Menjalankan Aplikasi](../../../03-rag)
- [Menggunakan Aplikasi](../../../03-rag)
  - [Unggah Dokumen](../../../03-rag)
  - [Ajukan Pertanyaan](../../../03-rag)
  - [Periksa Referensi Sumber](../../../03-rag)
  - [Bereksperimen dengan Pertanyaan](../../../03-rag)
- [Konsep Utama](../../../03-rag)
  - [Strategi Pemotongan](../../../03-rag)
  - [Skor Kesamaan](../../../03-rag)
  - [Penyimpanan Dalam Memori](../../../03-rag)
  - [Manajemen Jendela Konteks](../../../03-rag)
- [Kapan RAG Penting](../../../03-rag)
- [Langkah Berikutnya](../../../03-rag)

## Video Walkthrough

Tonton sesi langsung ini yang menjelaskan cara memulai dengan modul ini: [RAG dengan LangChain4j - Sesi Langsung](https://www.youtube.com/watch?v=_olq75ZH_eY)

## Apa yang Akan Anda Pelajari

Dalam modul sebelumnya, Anda telah belajar bagaimana cara berdialog dengan AI dan menyusun prompt Anda secara efektif. Namun ada batasan mendasar: model bahasa hanya tahu apa yang mereka pelajari selama pelatihan. Mereka tidak dapat menjawab pertanyaan tentang kebijakan perusahaan Anda, dokumentasi proyek Anda, atau informasi apapun yang tidak diajarkan saat pelatihan.

RAG (Retrieval-Augmented Generation) memecahkan masalah ini. Alih-alih mencoba mengajari model tentang informasi Anda (yang mahal dan tidak praktis), Anda memberikannya kemampuan untuk mencari dokumen Anda. Ketika seseorang mengajukan pertanyaan, sistem menemukan informasi relevan dan memasukkannya ke dalam prompt. Model kemudian menjawab berdasarkan konteks yang diambil tersebut.

Pikirkan RAG seperti memberikan model sebuah perpustakaan referensi. Saat Anda mengajukan pertanyaan, sistem:

1. **Pertanyaan Pengguna** - Anda mengajukan pertanyaan  
2. **Embedding** - Mengonversi pertanyaan Anda menjadi vektor  
3. **Pencarian Vektor** - Menemukan potongan dokumen yang serupa  
4. **Perakitan Konteks** - Menambahkan potongan relevan ke prompt  
5. **Respon** - LLM menghasilkan jawaban berdasarkan konteks  

Ini membuat respons model berlandaskan data Anda yang sebenarnya, bukan hanya mengandalkan pengetahuan pelatihan atau membuat jawaban.

## Prasyarat

- Telah menyelesaikan [Modul 00 - Quick Start](../00-quick-start/README.md) (untuk contoh Easy RAG yang dirujuk di atas)  
- Telah menyelesaikan [Modul 01 - Introduction](../01-introduction/README.md) (Sumber daya Azure OpenAI sudah diterapkan, termasuk model embedding `text-embedding-3-small`)  
- File `.env` di direktori root dengan kredensial Azure (dibuat oleh `azd up` di Modul 01)  

> **Catatan:** Jika Anda belum menyelesaikan Modul 01, ikuti instruksi penerapan di sana terlebih dahulu. Perintah `azd up` men-deploy model chat GPT dan model embedding yang digunakan modul ini.

## Memahami RAG

Diagram di bawah menggambarkan konsep inti: alih-alih hanya mengandalkan data pelatihan model, RAG memberikannya perpustakaan referensi dari dokumen Anda untuk dikonsultasikan sebelum menghasilkan setiap jawaban.

<img src="../../../translated_images/id/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*Diagram ini menunjukkan perbedaan antara LLM standar (yang menebak dari data pelatihan) dan LLM yang didukung RAG (yang terlebih dahulu merujuk dokumen Anda).*

Berikut cara bagian-bagian tersebut bekerja secara menyeluruh. Pertanyaan pengguna mengalir melalui empat tahap — embedding, pencarian vektor, perakitan konteks, dan generasi jawaban — masing-masing membangun dari tahap sebelumnya:

<img src="../../../translated_images/id/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*Diagram ini menunjukkan alur RAG end-to-end — pertanyaan pengguna melewati embedding, pencarian vektor, perakitan konteks, dan generasi jawaban.*

Sisa modul ini menjelaskan setiap tahap secara detail, dengan kode yang dapat Anda jalankan dan modifikasi.

### Pendekatan RAG Mana yang Digunakan Tutorial Ini?

LangChain4j menawarkan tiga cara untuk mengimplementasikan RAG, masing-masing dengan tingkat abstraksi berbeda. Diagram di bawah membandingkannya berdampingan:

<img src="../../../translated_images/id/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*Diagram ini membandingkan tiga pendekatan LangChain4j RAG — Easy, Native, dan Advanced — menampilkan komponen kunci mereka dan kapan harus digunakan.*

| Pendekatan | Apa yang Dilakukan | Trade-off |
|---|---|---|
| **Easy RAG** | Menghubungkan semuanya secara otomatis melalui `AiServices` dan `ContentRetriever`. Anda memberi anotasi antarmuka, memasang retriever, dan LangChain4j mengurus embedding, pencarian, dan perakitan prompt secara otomatis di belakang layar. | Kode minimal, tapi Anda tidak melihat apa yang terjadi di setiap langkah. |
| **Native RAG** | Anda memanggil model embedding, mencari ke dalam store, membangun prompt, dan menghasilkan jawaban sendiri — satu langkah eksplisit sekaligus. | Lebih banyak kode, tapi setiap tahap terlihat dan dapat dimodifikasi. |
| **Advanced RAG** | Menggunakan kerangka `RetrievalAugmentor` dengan transformer query, router, re-ranker, dan content injector yang dapat dipasang untuk pipeline produksi. | Fleksibilitas maksimum, tapi kompleksitas jauh lebih tinggi. |

**Tutorial ini menggunakan pendekatan Native.** Setiap langkah pipeline RAG — embedding query, pencarian vector store, perakitan konteks, dan generasi jawaban — dituliskan secara eksplisit di [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Ini disengaja: sebagai sumber belajar, lebih penting Anda melihat dan memahami setiap tahap daripada kode yang dipersingkat. Setelah Anda nyaman dengan cara bagian-bagian bekerja, Anda bisa beralih ke Easy RAG untuk prototipe cepat atau Advanced RAG untuk sistem produksi.

> **💡 Sudah melihat Easy RAG beraksi?** Modul [Quick Start](../00-quick-start/README.md) memuat contoh Q&A Dokumen ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) yang menggunakan pendekatan Easy RAG — LangChain4j mengelola embedding, pencarian, dan perakitan prompt secara otomatis. Modul ini melangkah lebih jauh dengan membuka pipeline tersebut supaya Anda dapat melihat dan mengontrol setiap tahap sendiri.

<img src="../../../translated_images/id/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Diagram ini menunjukkan pipeline Easy RAG dari `SimpleReaderDemo.java`. Bandingkan dengan pendekatan Native yang digunakan di modul ini: Easy RAG menyembunyikan embedding, pencarian, dan perakitan prompt di balik `AiServices` dan `ContentRetriever` — Anda hanya memuat dokumen, memasang retriever, dan mendapatkan jawaban. Pendekatan Native di modul ini membuka pipeline tersebut sehingga Anda memanggil setiap tahap (embed, search, assemble context, generate) sendiri, memberikan visibilitas dan kendali penuh.*

## Cara Kerjanya

Pipeline RAG dalam modul ini dibagi menjadi empat tahap yang berjalan berurutan setiap kali pengguna mengajukan pertanyaan. Pertama, dokumen yang diunggah akan **diparsing dan dipotong menjadi potongan-potongan** yang mudah dikelola. Potongan tersebut kemudian diubah menjadi **embedding vektor** dan disimpan agar dapat dibandingkan secara matematis. Saat ada pertanyaan, sistem melakukan **pencarian semantik** untuk menemukan potongan paling relevan, lalu meneruskannya sebagai konteks ke LLM untuk **generasi jawaban**. Bagian-bagian di bawah menjelaskan tiap tahap dengan kode dan diagram sesungguhnya. Mari kita lihat langkah pertama.

### Pemrosesan Dokumen

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Saat Anda mengunggah dokumen, sistem mem-parsing-nya (PDF atau teks biasa), melampirkan metadata seperti nama file, lalu memecahnya menjadi potongan-potongan — bagian lebih kecil yang muat dengan nyaman di jendela konteks model. Potongan-potongan tersebut saling overlap sedikit agar konteks di batas tidak hilang.

```java
// Menguraikan file yang diunggah dan membungkusnya dalam Dokumen LangChain4j
Document document = Document.from(content, metadata);

// Membagi menjadi potongan berukuran 300 token dengan tumpang tindih 30 token
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Diagram di bawah menunjukkan cara kerjanya secara visual. Perhatikan bagaimana setiap potongan berbagi beberapa token dengan tetangganya — overlap 30 token memastikan tidak ada konteks penting yang terlewat:

<img src="../../../translated_images/id/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*Diagram ini menunjukkan dokumen yang dibagi menjadi potongan 300 token dengan 30 token overlap, menjaga konteks di batas potongan.*

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) dan tanyakan:  
> - "Bagaimana LangChain4j memotong dokumen menjadi potongan dan mengapa overlap itu penting?"  
> - "Berapa ukuran potongan optimal untuk jenis dokumen berbeda dan mengapa?"  
> - "Bagaimana menangani dokumen dengan banyak bahasa atau format khusus?"

### Membuat Embedding

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Setiap potongan diubah menjadi representasi numerik yang disebut embedding — pada dasarnya konverter makna ke angka. Model embedding tidak "cerdas" seperti model chat; ia tidak bisa mengikuti instruksi, menalar, atau menjawab pertanyaan. Yang bisa dilakukannya adalah memetakan teks ke ruang matematis di mana makna serupa berdekatan — "mobil" dekat dengan "otomobil," "kebijakan pengembalian dana" dekat dengan "kembalikan uang saya." Pikirkan model chat sebagai orang yang bisa diajak bicara; model embedding adalah sistem pengarsipan super bagus.

<img src="../../../translated_images/id/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*Diagram ini menunjukkan bagaimana model embedding mengonversi teks menjadi vektor numerik, menempatkan makna serupa — seperti "mobil" dan "otomobil" — berdekatan di ruang vektor.*

```java
@Bean
public EmbeddingModel embeddingModel() {
    return OpenAiOfficialEmbeddingModel.builder()
        .baseUrl(azureOpenAiEndpoint)
        .apiKey(azureOpenAiKey)
        .modelName(azureEmbeddingDeploymentName)
        .build();
}

EmbeddingStore<TextSegment> embeddingStore = 
    new InMemoryEmbeddingStore<>();
```
  
Diagram kelas di bawah menunjukkan dua alur terpisah dalam pipeline RAG dan kelas LangChain4j yang mengimplementasikannya. Alur **ingest** (dijalankan sekali saat unggah) memecah dokumen, membuat embedding potongan, dan menyimpan via `.addAll()`. Alur **query** (dijalankan setiap kali pengguna bertanya) membuat embedding pertanyaan, mencari di penyimpanan lewat `.search()`, dan meneruskan konteks yang ditemukan ke model chat. Kedua alur bertemu pada interface `EmbeddingStore<TextSegment>` bersama:

<img src="../../../translated_images/id/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*Diagram ini menunjukkan dua alur pipeline RAG — ingest dan query — dan bagaimana keduanya terhubung melalui `EmbeddingStore`.*

Setelah embedding disimpan, konten serupa secara alami mengelompok di ruang vektor. Visualisasi di bawah menunjukkan bagaimana dokumen tentang topik terkait berakhir sebagai titik berdekatan, yang membuat pencarian semantik mungkin dilakukan:

<img src="../../../translated_images/id/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*Visualisasi ini menunjukkan bagaimana dokumen terkait mengelompok di ruang vektor 3D, dengan topik seperti Dokumen Teknis, Aturan Bisnis, dan FAQ membentuk kelompok terpisah.*

Saat pengguna mencari, sistem mengikuti empat langkah: embedding dokumen sekali, embedding query di setiap pencarian, membandingkan vektor query dengan semua vektor yang disimpan menggunakan cosine similarity, dan mengembalikan top-K potongan dengan skor tertinggi. Diagram di bawah menjelaskan setiap langkah dan kelas LangChain4j yang terlibat:

<img src="../../../translated_images/id/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*Diagram ini menunjukkan proses pencarian embedding empat langkah: embed dokumen, embed query, bandingkan vektor dengan cosine similarity, dan kembalikan hasil top-K.*

### Pencarian Semantik

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Saat Anda mengajukan pertanyaan, pertanyaan tersebut juga diubah menjadi embedding. Sistem membandingkan embedding pertanyaan Anda dengan semua embedding potongan dokumen. Ia menemukan potongan dengan makna paling mirip — bukan hanya mencocokkan kata kunci, tapi kesamaan semantik nyata.

```java
Embedding queryEmbedding = embeddingModel.embed(question).content();

EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
    .queryEmbedding(queryEmbedding)
    .maxResults(5)
    .minScore(0.5)
    .build();

EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);
List<EmbeddingMatch<TextSegment>> matches = searchResult.matches();

for (EmbeddingMatch<TextSegment> match : matches) {
    String relevantText = match.embedded().text();
    double score = match.score();
}
```
  
Diagram di bawah membandingkan pencarian semantik dengan pencarian kata kunci tradisional. Pencarian kata kunci untuk "kendaraan" melewatkan potongan tentang "mobil dan truk," tetapi pencarian semantik memahami makna sama dan mengembalikannya sebagai hasil dengan skor tinggi:

<img src="../../../translated_images/id/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*Diagram ini membandingkan pencarian berdasarkan kata kunci dengan pencarian semantik, menunjukkan bagaimana pencarian semantik mengambil konten yang terkait secara konseptual meskipun kata kunci berbeda.*

Di balik layar, kesamaan diukur menggunakan cosine similarity — pada dasarnya bertanya "apakah dua panah ini menunjuk ke arah yang sama?" Dua potongan bisa menggunakan kata yang sangat berbeda, tapi jika maknanya sama, vektor mereka mengarah ke arah yang sama dan mendapatkan skor dekat 1.0:

<img src="../../../translated_images/id/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*Diagram ini mengilustrasikan cosine similarity sebagai sudut antara vektor embedding — vektor yang lebih sejajar skor mendekati 1.0, menandakan kesamaan semantik lebih tinggi.*
> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) dan tanyakan:
> - "Bagaimana pencarian kesamaan bekerja dengan embeddings dan apa yang menentukan skor?"
> - "Ambang kesamaan apa yang harus saya gunakan dan bagaimana pengaruhnya terhadap hasil?"
> - "Bagaimana cara menangani kasus di mana tidak ditemukan dokumen yang relevan?"

### Jawaban Generation

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Potongan yang paling relevan disusun menjadi prompt terstruktur yang mencakup instruksi eksplisit, konteks yang diambil, dan pertanyaan pengguna. Model membaca potongan spesifik tersebut dan menjawab berdasarkan informasi itu — model hanya dapat menggunakan apa yang ada di depannya, yang mencegah halusinasi.

```java
String context = matches.stream()
    .map(match -> match.embedded().text())
    .collect(Collectors.joining("\n\n"));

String prompt = String.format("""
    Answer the question based on the following context.
    If the answer cannot be found in the context, say so.

    Context:
    %s

    Question: %s

    Answer:""", context, request.question());

String answer = chatModel.chat(prompt);
```

Diagram di bawah ini menunjukkan aksi penyusunan ini — potongan dengan skor tertinggi dari langkah pencarian disuntikkan ke dalam template prompt, dan `OpenAiOfficialChatModel` menghasilkan jawaban yang berlandaskan:

<img src="../../../translated_images/id/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Diagram ini menunjukkan bagaimana potongan dengan skor tertinggi disusun menjadi prompt terstruktur, memungkinkan model menghasilkan jawaban yang berlandaskan data Anda.*

## Jalankan Aplikasi

**Verifikasi deployment:**

Pastikan file `.env` ada di direktori root dengan kredensial Azure (dibuat selama Modul 01):

**Bash:**
```bash
cat ../.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Mulai aplikasi:**

> **Catatan:** Jika Anda sudah memulai semua aplikasi menggunakan `./start-all.sh` dari Modul 01, modul ini sudah berjalan di port 8081. Anda dapat melewati perintah start di bawah ini dan langsung ke http://localhost:8081.

**Opsi 1: Menggunakan Spring Boot Dashboard (Direkomendasikan untuk pengguna VS Code)**

Dev container sudah termasuk ekstensi Spring Boot Dashboard, yang menyediakan antarmuka visual untuk mengelola semua aplikasi Spring Boot. Anda dapat menemukannya di Activity Bar di sisi kiri VS Code (cari ikon Spring Boot).

Dari Spring Boot Dashboard, Anda dapat:
- Melihat semua aplikasi Spring Boot yang tersedia di workspace
- Mulai/hentikan aplikasi dengan satu klik
- Melihat log aplikasi secara real-time
- Memantau status aplikasi

Cukup klik tombol play di sebelah "rag" untuk memulai modul ini, atau mulai semua modul sekaligus.

<img src="../../../translated_images/id/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Tangkapan layar ini menunjukkan Spring Boot Dashboard di VS Code, di mana Anda dapat memulai, menghentikan, dan memantau aplikasi secara visual.*

**Opsi 2: Menggunakan skrip shell**

Mulai semua aplikasi web (modul 01-04):

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

Atau mulai hanya modul ini:

**Bash:**
```bash
cd 03-rag
./start.sh
```

**PowerShell:**
```powershell
cd 03-rag
.\start.ps1
```

Kedua skrip otomatis memuat variabel lingkungan dari file `.env` root dan akan membangun JAR jika belum ada.

> **Catatan:** Jika Anda lebih suka membangun semua modul secara manual sebelum mulai:
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

Buka http://localhost:8081 di browser Anda.

**Untuk menghentikan:**

**Bash:**
```bash
./stop.sh  # Hanya modul ini
# Atau
cd .. && ./stop-all.sh  # Semua modul
```

**PowerShell:**
```powershell
.\stop.ps1  # Hanya modul ini
# Atau
cd ..; .\stop-all.ps1  # Semua modul
```

## Menggunakan Aplikasi

Aplikasi menyediakan antarmuka web untuk unggah dokumen dan bertanya.

<a href="images/rag-homepage.png"><img src="../../../translated_images/id/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Tangkapan layar ini menunjukkan antarmuka aplikasi RAG di mana Anda mengunggah dokumen dan mengajukan pertanyaan.*

### Unggah Dokumen

Mulailah dengan mengunggah dokumen - file TXT paling cocok untuk pengujian. Sebuah `sample-document.txt` disediakan di direktori ini yang berisi informasi tentang fitur LangChain4j, implementasi RAG, dan praktik terbaik - sempurna untuk menguji sistem.

Sistem memproses dokumen Anda, memecahnya menjadi potongan-potongan, dan membuat embeddings untuk setiap potongan. Ini terjadi secara otomatis saat Anda mengunggah.

### Ajukan Pertanyaan

Sekarang ajukan pertanyaan spesifik tentang isi dokumen. Coba sesuatu yang faktual dan jelas tercantum dalam dokumen. Sistem mencari potongan yang relevan, menyertakannya dalam prompt, dan menghasilkan jawaban.

### Cek Referensi Sumber

Perhatikan setiap jawaban menyertakan referensi sumber dengan skor kesamaan. Skor ini (0 sampai 1) menunjukkan seberapa relevan setiap potongan dengan pertanyaan Anda. Skor yang lebih tinggi berarti kecocokan yang lebih baik. Ini memungkinkan Anda memverifikasi jawaban dengan materi sumber.

<a href="images/rag-query-results.png"><img src="../../../translated_images/id/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Tangkapan layar ini menunjukkan hasil kueri dengan jawaban yang dihasilkan, referensi sumber, dan skor relevansi untuk setiap potongan yang diambil.*

### Bereksperimen dengan Pertanyaan

Cobalah berbagai jenis pertanyaan:
- Fakta spesifik: "Apa topik utama?"
- Perbandingan: "Apa perbedaan antara X dan Y?"
- Ringkasan: "Rangkum poin utama tentang Z"

Perhatikan bagaimana skor relevansi berubah berdasarkan seberapa baik pertanyaan Anda cocok dengan isi dokumen.

## Konsep Utama

### Strategi Chunking

Dokumen dibagi menjadi potongan 300 token dengan tumpang tindih 30 token. Keseimbangan ini memastikan setiap potongan memiliki konteks cukup untuk bermakna sekaligus tetap cukup kecil agar banyak potongan bisa dimasukkan dalam satu prompt.

### Skor Kesamaan

Setiap potongan yang diambil disertai skor kesamaan antara 0 dan 1 yang menunjukkan seberapa dekat kecocokannya dengan pertanyaan pengguna. Diagram di bawah memvisualisasikan rentang skor dan bagaimana sistem menggunakannya untuk memfilter hasil:

<img src="../../../translated_images/id/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Diagram ini menunjukkan rentang skor dari 0 sampai 1, dengan ambang minimum 0,5 yang memfilter potongan tidak relevan.*

Skor berkisar antara 0 sampai 1:
- 0,7-1,0: Sangat relevan, kecocokan tepat
- 0,5-0,7: Relevan, konteks baik
- Di bawah 0,5: Difilter keluar, terlalu tidak mirip

Sistem hanya mengambil potongan di atas ambang minimum untuk menjamin kualitas.

Embeddings bekerja baik saat makna mengelompok dengan jelas, tapi punya titik buta. Diagram di bawah menunjukkan mode kegagalan umum — potongan terlalu besar menghasilkan vektor kabur, potongan terlalu kecil kurang konteks, istilah ambigu menunjuk ke banyak kluster, dan pencarian kecocokan tepat (ID, nomor bagian) sama sekali tidak bekerja dengan embeddings:

<img src="../../../translated_images/id/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Diagram ini menunjukkan mode kegagalan embedding umum: potongan terlalu besar, potongan terlalu kecil, istilah ambigu yang menunjuk banyak kluster, dan pencarian kecocokan tepat seperti ID.*

### Penyimpanan In-Memory

Modul ini menggunakan penyimpanan in-memory untuk kesederhanaan. Saat Anda me-restart aplikasi, dokumen yang diunggah hilang. Sistem produksi menggunakan basis data vektor persisten seperti Qdrant atau Azure AI Search.

### Manajemen Jendela Konteks

Setiap model memiliki jendela konteks maksimum. Anda tidak dapat memasukkan setiap potongan dari dokumen besar. Sistem mengambil N potongan paling relevan teratas (default 5) untuk tetap di dalam batas dan memberikan konteks yang cukup agar jawaban akurat.

## Kapan RAG Penting

RAG tidak selalu merupakan pendekatan yang tepat. Panduan keputusan di bawah membantu Anda menentukan kapan RAG menambah nilai dibandingkan ketika pendekatan lebih sederhana — seperti menyertakan konten langsung dalam prompt atau mengandalkan pengetahuan bawaan model — sudah cukup:

<img src="../../../translated_images/id/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Diagram ini menunjukkan panduan keputusan kapan RAG menambah nilai dan kapan pendekatan sederhana sudah cukup.*

**Gunakan RAG saat:**
- Menjawab pertanyaan tentang dokumen kepemilikan
- Informasi sering berubah (kebijakan, harga, spesifikasi)
- Akurasi memerlukan atribusi sumber
- Konten terlalu besar untuk dimasukkan dalam satu prompt
- Anda membutuhkan jawaban yang dapat diverifikasi dan berlandaskan

**Jangan gunakan RAG saat:**
- Pertanyaan memerlukan pengetahuan umum yang sudah dimodel
- Data real-time dibutuhkan (RAG bekerja pada dokumen yang diunggah)
- Konten cukup kecil untuk dimasukkan langsung dalam prompt

## Langkah Selanjutnya

**Modul Berikutnya:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Kembali ke Utama](../README.md) | [Berikutnya: Modul 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berusaha mencapai akurasi, harap diperhatikan bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber otoritatif. Untuk informasi yang penting, disarankan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau penafsiran yang salah yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->