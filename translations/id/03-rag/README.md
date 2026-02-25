# Modul 03: RAG (Retrieval-Augmented Generation)

## Daftar Isi

- [Apa yang Akan Anda Pelajari](../../../03-rag)
- [Memahami RAG](../../../03-rag)
- [Prasyarat](../../../03-rag)
- [Bagaimana Cara Kerjanya](../../../03-rag)
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
- [Konsep Kunci](../../../03-rag)
  - [Strategi Chunking](../../../03-rag)
  - [Skor Kesamaan](../../../03-rag)
  - [Penyimpanan Dalam Memori](../../../03-rag)
  - [Manajemen Jendela Konteks](../../../03-rag)
- [Kapan RAG Penting](../../../03-rag)
- [Langkah Selanjutnya](../../../03-rag)

## Apa yang Akan Anda Pelajari

Dalam modul-modul sebelumnya, Anda telah belajar bagaimana melakukan percakapan dengan AI dan menyusun prompt Anda secara efektif. Namun ada keterbatasan mendasar: model bahasa hanya mengetahui apa yang mereka pelajari selama pelatihan. Mereka tidak bisa menjawab pertanyaan tentang kebijakan perusahaan Anda, dokumentasi proyek Anda, atau informasi lain yang belum dilatihkan pada mereka.

RAG (Retrieval-Augmented Generation) memecahkan masalah ini. Alih-alih mencoba mengajarkan model informasi Anda (yang mahal dan tidak praktis), Anda memberikannya kemampuan untuk mencari di dokumen Anda. Ketika seseorang mengajukan pertanyaan, sistem menemukan informasi relevan dan menyertakannya dalam prompt. Model kemudian menjawab berdasarkan konteks yang diambil tersebut.

Pikirkan RAG sebagai memberikan model sebuah perpustakaan referensi. Ketika Anda mengajukan pertanyaan, sistem:

1. **Pertanyaan Pengguna** - Anda mengajukan pertanyaan  
2. **Embedding** - Mengonversi pertanyaan Anda menjadi vektor  
3. **Pencarian Vektor** - Menemukan potongan dokumen yang mirip  
4. **Perakitan Konteks** - Menambahkan potongan relevan ke prompt  
5. **Respons** - LLM menghasilkan jawaban berdasarkan konteks  

Ini membuat jawaban model berlandaskan data aktual Anda, bukan hanya bergantung pada pengetahuan pelatihan atau mengada-ada jawaban.

## Memahami RAG

Diagram di bawah ini menggambarkan konsep inti: alih-alih hanya bergantung pada data pelatihan model, RAG memberinya perpustakaan referensi dokumen Anda untuk dikonsultasikan sebelum menghasilkan setiap jawaban.

<img src="../../../translated_images/id/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

Berikut cara setiap bagian terhubung secara menyeluruh. Pertanyaan pengguna melewati empat tahap — embedding, pencarian vektor, perakitan konteks, dan generasi jawaban — yang masing-masing dibangun dari tahap sebelumnya:

<img src="../../../translated_images/id/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

Sisa modul ini akan menjelaskan tiap tahap secara rinci, dengan kode yang dapat Anda jalankan dan modifikasi.

## Prasyarat

- Menyelesaikan Modul 01 (sumber daya Azure OpenAI sudah diterapkan)  
- File `.env` di direktori root dengan kredensial Azure (dibuat oleh `azd up` di Modul 01)  

> **Catatan:** Jika Anda belum menyelesaikan Modul 01, ikuti instruksi penerapan di sana terlebih dahulu.

## Bagaimana Cara Kerjanya

### Pemrosesan Dokumen

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Saat Anda mengunggah dokumen, sistem akan mem-parsing-nya (PDF atau teks biasa), melampirkan metadata seperti nama file, lalu memecahnya menjadi potongan-potongan kecil — bagian lebih kecil yang bisa dengan nyaman masuk ke jendela konteks model. Potongan ini saling tumpang tindih sedikit sehingga Anda tidak kehilangan konteks pada batasnya.

```java
// Mengurai file yang diunggah dan membungkusnya dalam Dokumen LangChain4j
Document document = Document.from(content, metadata);

// Membagi menjadi potongan 300-token dengan tumpang tindih 30-token
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Diagram di bawah menunjukkan cara kerja ini secara visual. Perhatikan bagaimana setiap potongan berbagi token dengan tetangganya — tumpang tindih 30 token memastikan tidak ada konteks penting yang hilang:

<img src="../../../translated_images/id/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) dan tanya:  
> - "Bagaimana LangChain4j membagi dokumen menjadi potongan dan mengapa tumpang tindih penting?"  
> - "Berapa ukuran potongan optimal untuk jenis dokumen berbeda dan mengapa?"  
> - "Bagaimana cara menangani dokumen dalam berbagai bahasa atau dengan format khusus?"

### Membuat Embedding

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Setiap potongan diubah menjadi representasi numerik yang disebut embedding - pada dasarnya sidik jari matematis yang menangkap makna dari teks tersebut. Teks yang mirip menghasilkan embedding yang mirip pula.

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
  
Diagram kelas berikut menunjukkan bagaimana komponen LangChain4j ini terhubung. `OpenAiOfficialEmbeddingModel` mengonversi teks menjadi vektor, `InMemoryEmbeddingStore` menyimpan vektor bersama data `TextSegment` aslinya, dan `EmbeddingSearchRequest` mengendalikan parameter pengambilan seperti `maxResults` dan `minScore`:

<img src="../../../translated_images/id/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

Setelah embedding disimpan, konten serupa secara alami akan mengelompok di ruang vektor. Visualisasi di bawah menunjukkan bagaimana dokumen tentang topik terkait berakhir sebagai titik-titik yang berdekatan, yang memungkinkan pencarian semantik:

<img src="../../../translated_images/id/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

### Pencarian Semantik

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Saat Anda mengajukan pertanyaan, pertanyaan Anda juga diubah menjadi embedding. Sistem membandingkan embedding pertanyaan Anda dengan embedding potongan dokumen. Ia menemukan potongan dengan makna paling mirip — bukan hanya mencocokkan kata kunci, tetapi kesamaan semantik sebenarnya.

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
  
Diagram di bawah membandingkan pencarian semantik dengan pencarian kata kunci tradisional. Pencarian kata kunci untuk "kendaraan" melewatkan potongan tentang "mobil dan truk," tapi pencarian semantik mengerti bahwa itu berarti sama dan mengembalikannya sebagai hasil dengan skor tinggi:

<img src="../../../translated_images/id/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) dan tanya:  
> - "Bagaimana pencarian kemiripan bekerja dengan embedding dan apa yang menentukan skornya?"  
> - "Ambang batas kemiripan apa yang seharusnya saya gunakan dan bagaimana pengaruhnya terhadap hasil?"  
> - "Bagaimana saya menangani kasus tidak ditemukan dokumen relevan?"

### Generasi Jawaban

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Potongan paling relevan disusun menjadi prompt terstruktur yang mencakup instruksi eksplisit, konteks yang diambil, dan pertanyaan pengguna. Model membaca potongan khusus tersebut dan menjawab berdasarkan informasi tersebut — ia hanya bisa menggunakan apa yang ada di depannya, sehingga mencegah halusinasi.

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
  
Diagram di bawah menunjukkan perakitan ini dalam aksi — potongan hasil pencarian dengan skor tertinggi disisipkan ke template prompt, dan `OpenAiOfficialChatModel` menghasilkan jawaban yang berlandaskan konteks:

<img src="../../../translated_images/id/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

## Menjalankan Aplikasi

**Verifikasi penerapan:**

Pastikan file `.env` ada di root direktori dengan kredensial Azure (dibuat selama Modul 01):  
```bash
cat ../.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Mulai aplikasinya:**

> **Catatan:** Jika Anda sudah menjalankan semua aplikasi menggunakan `./start-all.sh` dari Modul 01, modul ini sudah berjalan di port 8081. Anda dapat melewati perintah start berikut dan langsung ke http://localhost:8081.

**Opsi 1: Menggunakan Spring Boot Dashboard (Disarankan untuk pengguna VS Code)**

Dev container sudah termasuk ekstensi Spring Boot Dashboard yang menyediakan antarmuka visual untuk mengelola semua aplikasi Spring Boot. Anda dapat menemukannya di Bilah Aktivitas di sisi kiri VS Code (cari ikon Spring Boot).

Dari Spring Boot Dashboard, Anda dapat:  
- Melihat semua aplikasi Spring Boot yang tersedia di workspace  
- Mulai/berhenti aplikasi dengan sekali klik  
- Melihat log aplikasi secara real-time  
- Memantau status aplikasi  

Klik tombol play di sebelah "rag" untuk memulai modul ini, atau mulai semua modul sekaligus.

<img src="../../../translated_images/id/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

**Opsi 2: Menggunakan skrip shell**

Jalankan semua aplikasi web (modul 01-04):  

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
  
Atau jalankan hanya modul ini:  

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
./stop.sh  # Hanya modul ini
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

Aplikasi menyediakan antarmuka web untuk unggah dokumen dan tanya jawab.

<a href="images/rag-homepage.png"><img src="../../../translated_images/id/rag-homepage.d90eb5ce1b3caa94.webp" alt="Antarmuka Aplikasi RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Antarmuka aplikasi RAG - unggah dokumen dan ajukan pertanyaan*

### Unggah Dokumen

Mulailah dengan mengunggah dokumen — file TXT paling cocok untuk pengujian. Sebuah `sample-document.txt` disediakan di direktori ini yang berisi informasi tentang fitur LangChain4j, implementasi RAG, dan praktik terbaik — sempurna untuk menguji sistem.

Sistem memproses dokumen Anda, memecahnya menjadi potongan-potongan, dan membuat embedding untuk setiap potongan secara otomatis saat Anda mengunggah.

### Ajukan Pertanyaan

Sekarang ajukan pertanyaan spesifik tentang isi dokumen. Coba sesuatu yang faktual dan jelas dinyatakan dalam dokumen. Sistem akan mencari potongan relevan, menyertakannya dalam prompt, dan menghasilkan jawaban.

### Periksa Referensi Sumber

Perhatikan setiap jawaban menyertakan referensi sumber dengan skor kemiripan. Skor ini (0 sampai 1) menunjukkan seberapa relevan setiap potongan terhadap pertanyaan Anda. Skor lebih tinggi berarti kecocokan lebih baik. Ini memungkinkan Anda memverifikasi jawaban terhadap materi sumber.

<a href="images/rag-query-results.png"><img src="../../../translated_images/id/rag-query-results.6d69fcec5397f355.webp" alt="Hasil Query RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Hasil query menunjukkan jawaban dengan referensi sumber dan skor relevansi*

### Bereksperimen dengan Pertanyaan

Coba berbagai jenis pertanyaan:  
- Fakta spesifik: "Apa topik utama?"  
- Perbandingan: "Apa bedanya X dan Y?"  
- Ringkasan: "Ringkas poin utama tentang Z"

Amati bagaimana skor relevansi berubah berdasarkan seberapa baik pertanyaan Anda cocok dengan konten dokumen.

## Konsep Kunci

### Strategi Chunking

Dokumen dibagi menjadi potongan 300 token dengan tumpang tindih 30 token. Keseimbangan ini memastikan setiap potongan memiliki konteks cukup untuk bermakna sekaligus cukup kecil agar beberapa potongan bisa disertakan dalam satu prompt.

### Skor Kesamaan

Setiap potongan yang diambil dilengkapi dengan skor kesamaan antara 0 dan 1 yang menunjukkan seberapa dekat kecocokannya dengan pertanyaan pengguna. Diagram di bawah memvisualisasikan rentang skor dan bagaimana sistem menggunakan mereka untuk menyaring hasil:

<img src="../../../translated_images/id/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

Skor berkisar antara 0 sampai 1:  
- 0.7-1.0: Sangat relevan, cocok tepat  
- 0.5-0.7: Relevan, konteks baik  
- Di bawah 0.5: Disaring, terlalu berbeda

Sistem hanya mengambil potongan di atas ambang minimum untuk memastikan kualitas.

### Penyimpanan Dalam Memori

Modul ini menggunakan penyimpanan dalam memori untuk kesederhanaan. Saat Anda memulai ulang aplikasi, dokumen yang diunggah akan hilang. Sistem produksi menggunakan database vektor persisten seperti Qdrant atau Azure AI Search.

### Manajemen Jendela Konteks

Setiap model memiliki jendela konteks maksimum. Anda tidak bisa menyertakan semua potongan dari dokumen besar. Sistem mengambil N potongan paling relevan (default 5) untuk tetap dalam batas sekaligus memberikan konteks cukup untuk jawaban akurat.

## Kapan RAG Penting

RAG tidak selalu merupakan pendekatan yang tepat. Panduan keputusan di bawah membantu Anda menentukan kapan RAG menambah nilai versus kapan pendekatan lebih sederhana — seperti menyertakan isi langsung dalam prompt atau mengandalkan pengetahuan bawaan model — sudah cukup:

<img src="../../../translated_images/id/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

**Gunakan RAG ketika:**
- Menjawab pertanyaan tentang dokumen kepemilikan
- Informasi sering berubah (kebijakan, harga, spesifikasi)
- Akurasi memerlukan atribusi sumber
- Konten terlalu besar untuk dimuat dalam satu prompt
- Anda memerlukan respons yang dapat diverifikasi dan berbasis sumber

**Jangan gunakan RAG ketika:**
- Pertanyaan memerlukan pengetahuan umum yang sudah dimiliki model
- Data waktu nyata diperlukan (RAG bekerja pada dokumen yang diunggah)
- Konten cukup kecil untuk disertakan langsung dalam prompt

## Langkah Selanjutnya

**Modul Berikutnya:** [04-tools - Agen AI dengan Alat](../04-tools/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 02 - Rekayasa Prompt](../02-prompt-engineering/README.md) | [Kembali ke Utama](../README.md) | [Berikutnya: Modul 04 - Alat →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berupaya mencapai akurasi, harap diingat bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidaktepatan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang otoritatif. Untuk informasi penting, disarankan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau interpretasi yang keliru yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->