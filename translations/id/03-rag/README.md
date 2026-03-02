# Modul 03: RAG (Retrieval-Augmented Generation)

## Daftar Isi

- [Video Walkthrough](../../../03-rag)
- [Apa yang Akan Anda Pelajari](../../../03-rag)
- [Prasyarat](../../../03-rag)
- [Memahami RAG](../../../03-rag)
  - [Pendekatan RAG Mana yang Digunakan Tutorial Ini?](../../../03-rag)
- [Cara Kerjanya](../../../03-rag)
  - [Pengolahan Dokumen](../../../03-rag)
  - [Membuat Embedding](../../../03-rag)
  - [Pencarian Semantik](../../../03-rag)
  - [Generasi Jawaban](../../../03-rag)
- [Jalankan Aplikasi](../../../03-rag)
- [Menggunakan Aplikasi](../../../03-rag)
  - [Unggah Dokumen](../../../03-rag)
  - [Ajukan Pertanyaan](../../../03-rag)
  - [Periksa Referensi Sumber](../../../03-rag)
  - [Eksperimen dengan Pertanyaan](../../../03-rag)
- [Konsep Kunci](../../../03-rag)
  - [Strategi Chunking](../../../03-rag)
  - [Skor Kesamaan](../../../03-rag)
  - [Penyimpanan Dalam Memori](../../../03-rag)
  - [Manajemen Jendela Konteks](../../../03-rag)
- [Kapan RAG Penting](../../../03-rag)
- [Langkah Selanjutnya](../../../03-rag)

## Video Walkthrough

Tonton sesi langsung ini yang menjelaskan cara memulai dengan modul ini:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## Apa yang Akan Anda Pelajari

Dalam modul-modul sebelumnya, Anda belajar bagaimana mengobrol dengan AI dan menyusun prompt secara efektif. Namun ada batasan mendasar: model bahasa hanya tahu apa yang mereka pelajari selama pelatihan. Mereka tidak bisa menjawab pertanyaan tentang kebijakan perusahaan Anda, dokumentasi proyek Anda, atau informasi apapun yang tidak mereka pelajari.

RAG (Retrieval-Augmented Generation) memecahkan masalah ini. Alih-alih mencoba mengajari model tentang informasi Anda (yang mahal dan tidak praktis), Anda memberinya kemampuan untuk mencari melalui dokumen Anda. Saat seseorang mengajukan pertanyaan, sistem menemukan informasi relevan dan menyertakannya dalam prompt. Model kemudian menjawab berdasarkan konteks yang diambil tersebut.

Bayangkan RAG sebagai memberi model sebuah perpustakaan referensi. Saat Anda bertanya, sistem:

1. **Pertanyaan Pengguna** - Anda mengajukan pertanyaan  
2. **Embedding** - Mengubah pertanyaan Anda menjadi vektor  
3. **Pencarian Vektor** - Mencari potongan dokumen yang serupa  
4. **Perakitan Konteks** - Menambahkan potongan relevan ke dalam prompt  
5. **Respon** - LLM menghasilkan jawaban berdasarkan konteks

Ini mendasarkan jawaban model pada data nyata Anda daripada mengandalkan pengetahuan pelatihan atau membuat jawaban.

## Prasyarat

- Menyelesaikan [Modul 00 - Quick Start](../00-quick-start/README.md) (untuk contoh Easy RAG yang disebutkan di atas)  
- Menyelesaikan [Modul 01 - Pengantar](../01-introduction/README.md) (sumber daya Azure OpenAI dideploy, termasuk model embedding `text-embedding-3-small`)  
- File `.env` di direktori root dengan kredensial Azure (dibuat oleh `azd up` pada Modul 01)

> **Catatan:** Jika Anda belum menyelesaikan Modul 01, ikuti instruksi deployment di sana terlebih dahulu. Perintah `azd up` mendeploy model chat GPT dan model embedding yang dipakai modul ini.

## Memahami RAG

Diagram di bawah menggambarkan konsep inti: alih-alih hanya mengandalkan data pelatihan model, RAG memberinya perpustakaan referensi dokumen Anda untuk dikonsultasikan sebelum menghasilkan setiap jawaban.

<img src="../../../translated_images/id/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*Diagram ini menunjukkan perbedaan antara LLM standar (yang menebak dari data pelatihan) dan LLM yang ditingkatkan RAG (yang mengonsultasikan dokumen Anda terlebih dahulu).*

Berikut cara bagian-bagiannya terhubung dari awal hingga akhir. Pertanyaan pengguna mengalir melalui empat tahap — embedding, pencarian vektor, perakitan konteks, dan generasi jawaban — tiap tahap membangun dari yang sebelumnya:

<img src="../../../translated_images/id/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*Diagram ini menunjukkan pipeline RAG dari ujung ke ujung — pertanyaan pengguna mengalir melalui embedding, pencarian vektor, perakitan konteks, dan generasi jawaban.*

Sisa modul ini menjelaskan tiap tahap secara rinci, dengan kode yang dapat Anda jalankan dan modifikasi.

### Pendekatan RAG Mana yang Digunakan Tutorial Ini?

LangChain4j menawarkan tiga cara mengimplementasikan RAG, masing-masing dengan tingkat abstraksi berbeda. Diagram berikut membandingkan ketiganya berdampingan:

<img src="../../../translated_images/id/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*Diagram ini membandingkan tiga pendekatan RAG LangChain4j — Easy, Native, dan Advanced — menunjukkan komponen utama dan kapan digunakan masing-masing.*

| Pendekatan | Apa yang Dilakukan | Trade-off |
|---|---|---|
| **Easy RAG** | Menghubungkan semuanya secara otomatis melalui `AiServices` dan `ContentRetriever`. Anda memberi anotasi antarmuka, mengaitkan retriever, dan LangChain4j menangani embedding, pencarian, dan perakitan prompt secara tersembunyi. | Kode minimal, tapi Anda tidak melihat apa yang terjadi di setiap langkah. |
| **Native RAG** | Anda memanggil model embedding, mencari di penyimpanan, membangun prompt, dan menghasilkan jawaban sendiri — satu langkah eksplisit tiap waktu. | Lebih banyak kode, tapi setiap tahap terlihat dan bisa diubah. |
| **Advanced RAG** | Menggunakan framework `RetrievalAugmentor` dengan transformator query, router, re-ranker, dan injector konten yang dapat dipasang untuk pipeline tingkat produksi. | Fleksibilitas maksimum, tapi dengan kompleksitas jauh lebih tinggi. |

**Tutorial ini menggunakan pendekatan Native.** Setiap langkah pipeline RAG — embedding query, pencarian di vector store, perakitan konteks, dan menghasilkan jawaban — ditulis secara eksplisit dalam [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Ini disengaja: sebagai sumber pembelajaran, lebih penting Anda bisa melihat dan memahami setiap tahap ketimbang kodenya diminimalisir. Setelah nyaman dengan bagaimana bagian-bagian terhubung, Anda bisa lanjut ke Easy RAG untuk prototipe cepat atau Advanced RAG untuk sistem produksi.

> **💡 Sudah lihat Easy RAG beraksi?** Modul [Quick Start](../00-quick-start/README.md) mencakup contoh Document Q&A ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) yang menggunakan pendekatan Easy RAG — LangChain4j menangani embedding, pencarian, dan perakitan prompt secara otomatis. Modul ini melangkah lebih jauh dengan membuka pipeline tersebut agar Anda bisa melihat dan mengontrol setiap tahap secara mandiri.

<img src="../../../translated_images/id/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Diagram ini menunjukkan pipeline Easy RAG dari `SimpleReaderDemo.java`. Bandingkan dengan pendekatan Native yang digunakan modul ini: Easy RAG menyembunyikan embedding, pengambilan, dan perakitan prompt di balik `AiServices` dan `ContentRetriever` — Anda memuat dokumen, mengaitkan retriever, dan dapat jawaban. Pendekatan Native dalam modul ini membuka pipeline tersebut sehingga Anda memanggil tiap tahap (embedding, pencarian, perakitan konteks, generasi) sendiri, memberi visibilitas dan kendali penuh.*

## Cara Kerjanya

Pipeline RAG dalam modul ini dibagi menjadi empat tahap yang dijalankan berurutan setiap kali pengguna mengajukan pertanyaan. Pertama, dokumen yang diunggah **diurai dan dibagi menjadi potongan** agar mudah dikelola. Potongan-potongan tersebut kemudian dikonversi menjadi **embedding vektor** dan disimpan agar bisa dibandingkan secara matematis. Ketika query datang, sistem melakukan **pencarian semantik** untuk menemukan potongan paling relevan, dan akhirnya melewatkannya sebagai konteks ke LLM untuk **menghasilkan jawaban**. Bagian-bagian berikut menjelaskan tiap tahap dengan kode dan diagram aktual. Mari kita lihat langkah pertama.

### Pengolahan Dokumen

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Saat Anda mengunggah dokumen, sistem menguraikannya (PDF atau teks biasa), menempelkan metadata seperti nama file, lalu memecahnya menjadi potongan — bagian kecil yang muat dengan nyaman di jendela konteks model. Potongan-potongan ini saling tumpang tindih sedikit agar konteks di batas tidak hilang.

```java
// Menguraikan file yang diunggah dan membungkusnya dalam Dokumen LangChain4j
Document document = Document.from(content, metadata);

// Membagi menjadi potongan 300-token dengan tumpang tindih 30-token
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Diagram di bawah menunjukkan cara kerja secara visual. Perhatikan bagaimana tiap potongan berbagi beberapa token dengan tetangganya — tumpang tindih 30 token memastikan tidak ada konteks penting yang hilang:

<img src="../../../translated_images/id/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*Diagram ini menunjukkan dokumen yang dibagi menjadi potongan berukuran 300 token dengan tumpang tindih 30 token, mempertahankan konteks di batas potongan.*

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) dan tanyakan:  
> - "Bagaimana LangChain4j membagi dokumen menjadi potongan dan mengapa tumpang tindih penting?"  
> - "Apa ukuran potongan optimal untuk berbagai tipe dokumen dan mengapa?"  
> - "Bagaimana menangani dokumen dalam berbagai bahasa atau dengan format khusus?"

### Membuat Embedding

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Setiap potongan diubah menjadi representasi numerik yang disebut embedding — pada dasarnya pengubah makna menjadi angka. Model embedding bukan "pintar" seperti model chat; ia tidak bisa mengikuti instruksi, bernalar, atau menjawab pertanyaan. Yang bisa dilakukannya adalah memetakan teks ke ruang matematis di mana makna serupa berkumpul — "mobil" dekat dengan "automobile," "kebijakan pengembalian" dekat dengan "kembalikan uangku." Pikirkan model chat seperti orang yang bisa diajak bicara; model embedding seperti sistem arsip ultra-handal.

<img src="../../../translated_images/id/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*Diagram ini menunjukkan bagaimana model embedding mengubah teks menjadi vektor numerik, menempatkan makna serupa — seperti "mobil" dan "automobile" — berdekatan di ruang vektor.*

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
  
Diagram kelas di bawah memperlihatkan dua alur terpisah dalam pipeline RAG dan kelas LangChain4j yang mengimplementasikannya. Alur **pengambilan** (dijalankan sekali saat upload) memecah dokumen, meng-embed potongan, dan menyimpannya via `.addAll()`. Alur **query** (dijalankan setiap kali pengguna bertanya) meng-embed pertanyaan, mencari di penyimpanan via `.search()`, dan melewatkan konteks yang cocok ke model chat. Kedua alur bertemu di antarmuka bersama `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/id/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*Diagram ini menunjukkan dua alur dalam pipeline RAG — pengambilan dan query — dan bagaimana mereka terhubung melalui EmbeddingStore bersama.*

Setelah embedding disimpan, konten serupa secara alami mengelompok di ruang vektor. Visualisasi di bawah memperlihatkan bagaimana dokumen terkait topik berdekatan sebagai titik-titik, yang memungkinkan pencarian semantik berjalan:

<img src="../../../translated_images/id/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*Visualisasi ini menunjukkan bagaimana dokumen terkait mengelompok bersama di ruang vektor 3D, dengan topik seperti Dokumen Teknis, Aturan Bisnis, dan FAQ membentuk kelompok tersendiri.*

Saat pengguna mencari, sistem mengikuti empat langkah: embed dokumen sekali, embed query setiap pencarian, bandingkan vektor query dengan semua vektor tersimpan menggunakan cosine similarity, dan kembalikan potongan top-K dengan skor tertinggi. Diagram di bawah menjelaskan tiap langkah dan kelas LangChain4j yang terlibat:

<img src="../../../translated_images/id/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*Diagram ini menunjukkan proses pencarian embedding empat langkah: embed dokumen, embed query, bandingkan vektor dengan cosine similarity, dan kembalikan hasil top-K.*

### Pencarian Semantik

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Saat Anda mengajukan pertanyaan, pertanyaan Anda juga diubah menjadi embedding. Sistem membandingkan embedding pertanyaan dengan embedding semua potongan dokumen. Sistem menemukan potongan dengan makna paling serupa — bukan hanya mencocokkan kata kunci, tapi kesamaan semantik yang sebenarnya.

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
  
Diagram di bawah membandingkan pencarian semantik dengan pencarian berbasis kata kunci tradisional. Pencarian kata kunci untuk “kendaraan” melewatkan potongan tentang “mobil dan truk,” tapi pencarian semantik memahami itu berarti sama dan mengembalikannya sebagai hasil skor tinggi:

<img src="../../../translated_images/id/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*Diagram ini membandingkan pencarian berbasis kata kunci dengan pencarian semantik, menunjukkan bagaimana pencarian semantik mengambil konten yang terkait konsep meskipun kata kunci tepat berbeda.*

Di balik layar, kesamaan diukur menggunakan cosine similarity — secara sederhana bertanya “apakah dua panah ini menunjuk ke arah yang sama?” Dua potongan bisa menggunakan kata yang sangat berbeda, tapi jika maknanya sama, vektor mereka menunjuk arah yang sama dan skornya dekat dengan 1.0:

<img src="../../../translated_images/id/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>
*Diagram ini menggambarkan similarity cosine sebagai sudut antara vektor embedding — vektor yang lebih sejajar mendapatkan skor lebih dekat ke 1.0, menunjukkan kesamaan semantik yang lebih tinggi.*

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) dan tanyakan:
> - "Bagaimana pencarian similarity bekerja dengan embeddings dan apa yang menentukan skornya?"
> - "Ambang similarity apa yang harus saya gunakan dan bagaimana pengaruhnya terhadap hasil?"
> - "Bagaimana cara menangani kasus ketika tidak ditemukan dokumen relevan?"

### Jawaban Generasi

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Potongan paling relevan disusun menjadi prompt terstruktur yang mencakup instruksi eksplisit, konteks yang diambil, dan pertanyaan pengguna. Model membaca potongan spesifik tersebut dan menjawab berdasarkan informasi itu — model hanya dapat menggunakan apa yang ada di depannya, yang mencegah halusinasi.

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

Diagram di bawah ini menunjukkan penyusunan ini dalam aksi — potongan dengan skor tertinggi dari langkah pencarian disuntikkan ke template prompt, dan `OpenAiOfficialChatModel` menghasilkan jawaban yang berbasis pada data:

<img src="../../../translated_images/id/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Diagram ini menunjukkan bagaimana potongan dengan skor tertinggi disusun menjadi prompt terstruktur, memungkinkan model menghasilkan jawaban yang berbasis data Anda.*

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

> **Catatan:** Jika Anda sudah memulai semua aplikasi menggunakan `./start-all.sh` dari Modul 01, modul ini sudah berjalan di port 8081. Anda dapat melewatkan perintah start di bawah dan langsung ke http://localhost:8081.

**Opsi 1: Menggunakan Spring Boot Dashboard (Direkomendasikan untuk pengguna VS Code)**

Dev container menyertakan ekstensi Spring Boot Dashboard, yang menyediakan antarmuka visual untuk mengelola semua aplikasi Spring Boot. Anda dapat menemukannya di Activity Bar di sisi kiri VS Code (cari ikon Spring Boot).

Dari Spring Boot Dashboard, Anda dapat:
- Melihat semua aplikasi Spring Boot yang tersedia di workspace
- Memulai/menghentikan aplikasi dengan satu klik
- Melihat log aplikasi secara real-time
- Memantau status aplikasi

Cukup klik tombol play di samping "rag" untuk memulai modul ini, atau mulai semua modul sekaligus.

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
cd ..  # Dari direktori akar
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

Kedua skrip secara otomatis memuat variabel lingkungan dari file `.env` root dan akan membangun JAR jika belum ada.

> **Catatan:** Jika Anda lebih suka membangun semua modul secara manual sebelum memulai:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Buka http://localhost:8081 di browser Anda.

**Untuk menghentikan:**

**Bash:**
```bash
./stop.sh  # Modul ini saja
# Atau
cd .. && ./stop-all.sh  # Semua modul
```

**PowerShell:**
```powershell
.\stop.ps1  # Modul ini saja
# Atau
cd ..; .\stop-all.ps1  # Semua modul
```

## Menggunakan Aplikasi

Aplikasi menyediakan antarmuka web untuk mengunggah dokumen dan bertanya.

<a href="images/rag-homepage.png"><img src="../../../translated_images/id/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Tangkapan layar ini menampilkan antarmuka aplikasi RAG di mana Anda mengunggah dokumen dan mengajukan pertanyaan.*

### Unggah Dokumen

Mulailah dengan mengunggah dokumen - file TXT paling cocok untuk pengujian. `sample-document.txt` disediakan di direktori ini yang berisi informasi tentang fitur LangChain4j, implementasi RAG, dan praktik terbaik - sempurna untuk menguji sistem.

Sistem memproses dokumen Anda, memecahnya menjadi potongan-potongan, dan membuat embedding untuk setiap potongan. Ini terjadi otomatis saat Anda mengunggah.

### Ajukan Pertanyaan

Sekarang ajukan pertanyaan spesifik tentang isi dokumen. Coba sesuatu yang faktual dan jelas tertulis di dokumen tersebut. Sistem mencari potongan yang relevan, menyertakannya dalam prompt, dan menghasilkan jawaban.

### Periksa Referensi Sumber

Perhatikan setiap jawaban menyertakan referensi sumber dengan skor kemiripan. Skor ini (0 sampai 1) menunjukkan seberapa relevan setiap potongan dengan pertanyaan Anda. Skor lebih tinggi berarti kecocokan lebih baik. Ini memungkinkan Anda memverifikasi jawaban dengan materi sumber.

<a href="images/rag-query-results.png"><img src="../../../translated_images/id/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Tangkapan layar ini menunjukkan hasil query dengan jawaban yang dihasilkan, referensi sumber, dan skor relevansi untuk setiap potongan yang diambil.*

### Bereksperimen dengan Pertanyaan

Coba berbagai jenis pertanyaan:
- Fakta spesifik: "Apa topik utama?"
- Perbandingan: "Apa perbedaan antara X dan Y?"
- Ringkasan: "Ringkas poin-poin utama tentang Z"

Perhatikan bagaimana skor relevansi berubah berdasarkan seberapa baik pertanyaan Anda cocok dengan isi dokumen.

## Konsep Kunci

### Strategi Pemecahan Potongan

Dokumen dibagi menjadi potongan 300 token dengan tumpang tindih 30 token. Keseimbangan ini memastikan setiap potongan memiliki konteks yang cukup untuk bermakna tetapi tetap kecil agar beberapa potongan bisa dimasukkan dalam sebuah prompt.

### Skor Kemiripan

Setiap potongan yang diambil disertai skor kemiripan antara 0 sampai 1 yang menunjukkan seberapa dekat kecocokannya dengan pertanyaan pengguna. Diagram di bawah memvisualisasikan rentang skor dan bagaimana sistem menggunakannya untuk memfilter hasil:

<img src="../../../translated_images/id/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Diagram ini menunjukkan rentang skor dari 0 sampai 1, dengan ambang minimum 0.5 yang menyaring potongan tidak relevan.*

Skor berkisar dari 0 sampai 1:
- 0.7-1.0: Sangat relevan, cocok tepat
- 0.5-0.7: Relevan, konteks baik
- Kurang dari 0.5: Disaring, terlalu tidak mirip

Sistem hanya mengambil potongan di atas ambang minimum untuk menjamin kualitas.

Embedding bekerja baik ketika makna terkelompok dengan jelas, tetapi memiliki titik buta. Diagram di bawah menunjukkan mode kegagalan umum — potongan terlalu besar menghasilkan vektor yang tidak jelas, potongan terlalu kecil kekurangan konteks, istilah ambigu menunjuk ke beberapa kluster, dan pencarian cocok tepat (ID, nomor bagian) tidak bekerja dengan embedding sama sekali:

<img src="../../../translated_images/id/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Diagram ini menunjukkan mode kegagalan embedding umum: potongan terlalu besar, potongan terlalu kecil, istilah ambigu yang menunjuk ke beberapa kluster, dan pencarian cocok tepat seperti ID.*

### Penyimpanan In-Memory

Modul ini menggunakan penyimpanan in-memory untuk kesederhanaan. Saat Anda memulai ulang aplikasi, dokumen yang diunggah hilang. Sistem produksi menggunakan database vektor persisten seperti Qdrant atau Azure AI Search.

### Manajemen Jendela Konteks

Setiap model memiliki jendela konteks maksimum. Anda tidak bisa memasukkan setiap potongan dari dokumen besar. Sistem mengambil N potongan paling relevan teratas (default 5) agar tetap dalam batas sambil menyediakan cukup konteks untuk jawaban akurat.

## Saat RAG Penting

RAG tidak selalu pendekatan yang tepat. Panduan keputusan di bawah membantu menentukan kapan RAG memberikan nilai dibandingkan pendekatan yang lebih sederhana — seperti memasukkan konten langsung dalam prompt atau mengandalkan pengetahuan bawaan model — sudah cukup:

<img src="../../../translated_images/id/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Diagram ini menunjukkan panduan keputusan kapan RAG memberi nilai tambah versus kapan pendekatan sederhana sudah cukup.*

**Gunakan RAG ketika:**
- Menjawab pertanyaan tentang dokumen proprietary
- Informasi sering berubah (kebijakan, harga, spesifikasi)
- Akurasi memerlukan atribusi sumber
- Konten terlalu besar untuk muat dalam satu prompt
- Anda butuh respons yang dapat diverifikasi dan berbasis data

**Jangan gunakan RAG ketika:**
- Pertanyaan membutuhkan pengetahuan umum yang sudah ada di model
- Data real-time diperlukan (RAG bekerja pada dokumen yang diunggah)
- Konten cukup kecil untuk dimasukkan langsung dalam prompt

## Langkah Selanjutnya

**Modul Berikutnya:** [04-tools - Agen AI dengan Alat](../04-tools/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Kembali ke Utama](../README.md) | [Berikutnya: Modul 04 - Alat →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berusaha untuk akurat, harap diperhatikan bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang sah. Untuk informasi penting, disarankan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau penafsiran yang salah yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->