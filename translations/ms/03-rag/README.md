# Module 03: RAG (Generasi Dipertingkatkan dengan Pengambilan)

## Table of Contents

- [Apa yang Akan Anda Pelajari](../../../03-rag)
- [Memahami RAG](../../../03-rag)
- [Prasyarat](../../../03-rag)
- [Bagaimana Ia Berfungsi](../../../03-rag)
  - [Pemprosesan Dokumen](../../../03-rag)
  - [Mewujudkan Embedding](../../../03-rag)
  - [Carian Semantik](../../../03-rag)
  - [Penjanaan Jawapan](../../../03-rag)
- [Jalankan Aplikasi](../../../03-rag)
- [Menggunakan Aplikasi](../../../03-rag)
  - [Muat Naik Dokumen](../../../03-rag)
  - [Ajukan Soalan](../../../03-rag)
  - [Semak Rujukan Sumber](../../../03-rag)
  - [Eksperimen dengan Soalan](../../../03-rag)
- [Konsep Utama](../../../03-rag)
  - [Strategi Pemecahan Chunk](../../../03-rag)
  - [Skor Kesamaan](../../../03-rag)
  - [Penyimpanan Dalam Memori](../../../03-rag)
  - [Pengurusan Tetingkap Konteks](../../../03-rag)
- [Bila RAG Penting](../../../03-rag)
- [Langkah Seterusnya](../../../03-rag)

## What You'll Learn

Dalam modul-modul sebelumnya, anda belajar bagaimana untuk berbual dengan AI dan menyusun prom anda dengan berkesan. Tetapi terdapat had asas: model bahasa hanya tahu apa yang mereka pelajari semasa latihan. Mereka tidak dapat menjawab soalan tentang polisi syarikat anda, dokumentasi projek anda, atau apa-apa maklumat yang mereka tidak dilatih untuk mengetahuinya.

RAG (Generasi Dipertingkatkan dengan Pengambilan) menyelesaikan masalah ini. Daripada cuba mengajar model maklumat anda (yang mahal dan tidak praktikal), anda memberikannya keupayaan untuk mencari dokumen anda. Apabila seseorang bertanya soalan, sistem mencari maklumat yang relevan dan memasukkannya ke dalam permintaan. Model kemudian menjawab berdasarkan konteks yang diperoleh itu.

Bayangkan RAG seperti memberi model sebuah perpustakaan rujukan. Apabila anda bertanya soalan, sistem:

1. **Pertanyaan Pengguna** - Anda bertanya soalan  
2. **Embedding** - Menukar soalan anda kepada vektor  
3. **Carian Vektor** - Mencari chunk dokumen yang serupa  
4. **Penyusunan Konteks** - Menambah chunk yang relevan ke dalam permintaan  
5. **Respons** - LLM menghasilkan jawapan berdasarkan konteks  

Ini mendasari jawapan model pada data sebenar anda dan bukannya bergantung pada pengetahuan latihannya atau mereka-reka jawapan.

## Understanding RAG

Rajah di bawah menerangkan konsep teras: daripada bergantung pada data latihan model sahaja, RAG memberinya perpustakaan rujukan dokumen anda untuk dirujuk sebelum menjana setiap jawapan.

<img src="../../../translated_images/ms/what-is-rag.1f9005d44b07f2d8.webp" alt="Apa itu RAG" width="800"/>

Ini adalah bagaimana komponen-komponen ini berhubung dari hujung ke hujung. Soalan pengguna melalui empat peringkat — embedding, carian vektor, penyusunan konteks, dan penjanaan jawapan — setiap satu dibina berdasarkan yang sebelumnya:

<img src="../../../translated_images/ms/rag-architecture.ccb53b71a6ce407f.webp" alt="Seni Bina RAG" width="800"/>

Selepas ini, modul akan menerangkan setiap peringkat dengan terperinci, lengkap dengan kod yang anda boleh jalankan dan ubah suai.

## Prerequisites

- Modul 01 telah disiapkan (Sumber Azure OpenAI telah diterapkan)  
- Fail `.env` dalam direktori akar dengan kelayakan Azure (dibuat oleh `azd up` dalam Modul 01)  

> **Nota:** Jika anda belum siapkan Modul 01, ikuti arahan penerapan di sana terlebih dahulu.

## How It Works

### Document Processing

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Apabila anda memuat naik dokumen, sistem akan menguraikannya (PDF atau teks biasa), melampirkan metadata seperti nama fail, dan kemudian memecahkannya menjadi chunk — bahagian yang lebih kecil yang sesuai dengan tetingkap konteks model. Chunk ini sedikit bertindih supaya konteks di sempadan tidak hilang.

```java
// Analisis fail yang dimuat naik dan bungkus ia dalam Dokumen LangChain4j
Document document = Document.from(content, metadata);

// Bahagikan kepada ketulan 300-token dengan tumpang tindih 30-token
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Rajah di bawah menunjukkan bagaimana ini berfungsi secara visual. Perhatikan bagaimana setiap chunk berkongsi beberapa token dengan jiran-jirannya — pertindihan 30 token memastikan tiada konteks penting terlepas:

<img src="../../../translated_images/ms/document-chunking.a5df1dd1383431ed.webp" alt="Pemecahan Dokumen" width="800"/>

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) dan tanya:  
> - "Bagaimana LangChain4j memecah dokumen menjadi chunk dan mengapa pertindihan itu penting?"  
> - "Apakah saiz chunk optimum untuk jenis dokumen berbeza dan mengapa?"  
> - "Bagaimana saya mengendalikan dokumen dalam pelbagai bahasa atau dengan format khas?"

### Creating Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Setiap chunk ditukar menjadi representasi berangka yang dipanggil embedding - pada dasarnya cap jari matematik yang menangkap makna teks tersebut. Teks yang serupa menghasilkan embedding yang serupa.

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

Rajah kelas di bawah menunjukkan bagaimana komponen LangChain4j ini berhubung. `OpenAiOfficialEmbeddingModel` menukar teks menjadi vektor, `InMemoryEmbeddingStore` menyimpan vektor bersama data `TextSegment` asalnya, dan `EmbeddingSearchRequest` mengawal parameter pengambilan seperti `maxResults` dan `minScore`:

<img src="../../../translated_images/ms/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="Kelas LangChain4j RAG" width="800"/>

Setelah embedding disimpan, kandungan serupa semula jadi akan berkumpulan dalam ruang vektor. Visualisasi di bawah menunjukkan bagaimana dokumen tentang topik berkaitan menjadi titik berdekatan, yang memudahkan carian semantik:

<img src="../../../translated_images/ms/vector-embeddings.2ef7bdddac79a327.webp" alt="Ruang Embedding Vektor" width="800"/>

### Semantic Search

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Apabila anda bertanya soalan, soalan anda juga menjadi embedding. Sistem membandingkan embedding soalan anda dengan semua embedding chunk dokumen. Ia mencari chunk yang paling serupa maknanya — bukan hanya padanan kata kunci, tetapi kesamaan semantik sebenar.

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

Rajah di bawah membezakan carian semantik dengan carian kata kunci tradisional. Carian kata kunci untuk "vehicle" terlepas chunk tentang "kereta dan lori," tetapi carian semantik faham mereka bermaksud benda yang sama dan mengembalikannya sebagai padanan skor tinggi:

<img src="../../../translated_images/ms/semantic-search.6b790f21c86b849d.webp" alt="Carian Semantik" width="800"/>

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) dan tanya:  
> - "Bagaimana carian kesamaan berfungsi dengan embedding dan apa yang menentukan skor?"  
> - "Apakah ambang kesamaan yang patut saya guna dan bagaimana ia mempengaruhi hasil?"  
> - "Bagaimana saya tangani kes di mana tiada dokumen relevan ditemui?"

### Answer Generation

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Chunk yang paling relevan disusun ke dalam prom yang terstruktur yang merangkumi arahan eksplisit, konteks yang diperoleh, dan soalan pengguna. Model membaca chunk khusus itu dan menjawab berdasarkan maklumat tersebut — ia hanya boleh gunakan apa yang dihadapannya, yang mencegah halusinasi.

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

Rajah di bawah menunjukkan penyusunan ini beraksi — chunk dengan skor tertinggi dari langkah carian dimasukkan ke dalam templat prom, dan `OpenAiOfficialChatModel` menjana jawapan yang berasaskan konteks:

<img src="../../../translated_images/ms/context-assembly.7e6dd60c31f95978.webp" alt="Penyusunan Konteks" width="800"/>

## Run the Application

**Sahkan penerapan:**

Pastikan fail `.env` wujud dalam direktori akar dengan kelayakan Azure (dicipta semasa Modul 01):  
```bash
cat ../.env  # Harus menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Mulakan aplikasi:**

> **Nota:** Jika anda sudah mula semua aplikasi menggunakan `./start-all.sh` dari Modul 01, modul ini sudah berjalan pada port 8081. Anda boleh langkau arahan mula di bawah dan terus ke http://localhost:8081.

**Pilihan 1: Menggunakan Spring Boot Dashboard (Disarankan untuk pengguna VS Code)**

Kontena dev termasuk pelanjut Spring Boot Dashboard yang menyediakan antara muka visual untuk mengurus semua aplikasi Spring Boot. Anda boleh menemuinya di Bar Aktiviti di sebelah kiri VS Code (cari ikon Spring Boot).

Dari Spring Boot Dashboard, anda boleh:  
- Melihat semua aplikasi Spring Boot yang tersedia dalam ruang kerja  
- Mulakan/berhenti aplikasi dengan satu klik  
- Lihat log aplikasi secara masa nyata  
- Pantau status aplikasi  

Klik butang main di sebelah "rag" untuk memulakan modul ini, atau mulakan semua modul sekaligus.

<img src="../../../translated_images/ms/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

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
cd 03-rag
./start.sh
```
  
**PowerShell:**  
```powershell
cd 03-rag
.\start.ps1
```
  
Kedua-dua skrip secara automatik memuat pembolehubah persekitaran dari fail `.env` akar dan akan bina JAR jika belum wujud.

> **Nota:** Jika anda lebih suka membina semua modul secara manual sebelum mula:  
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
  
Buka http://localhost:8081 dalam pelayar anda.

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
  
## Using the Application

Aplikasi menyediakan antara muka web untuk muat naik dokumen dan bertanya soalan.

<a href="images/rag-homepage.png"><img src="../../../translated_images/ms/rag-homepage.d90eb5ce1b3caa94.webp" alt="Antara Muka Aplikasi RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Antara muka aplikasi RAG - muat naik dokumen dan ajukan soalan*

### Upload a Document

Mula dengan memuat naik dokumen — fail TXT paling sesuai untuk ujian. Fail `sample-document.txt` disediakan dalam direktori ini yang mengandungi maklumat tentang ciri LangChain4j, pelaksanaan RAG, dan amalan terbaik — sesuai untuk menguji sistem.

Sistem memproses dokumen anda, memecahnya menjadi chunk, dan mencipta embedding untuk setiap chunk. Ini berlaku secara automatik apabila anda memuat naik.

### Ask Questions

Sekarang ajukan soalan khusus mengenai kandungan dokumen. Cuba sesuatu yang faktual yang dinyatakan dengan jelas dalam dokumen. Sistem mencari chunk yang relevan, memasukkannya ke dalam permintaan, dan menjana jawapan.

### Check Source References

Perhatikan setiap jawapan menyertakan rujukan sumber dengan skor kesamaan. Skor ini (0 hingga 1) menunjukkan betapa relevannya setiap chunk kepada soalan anda. Skor yang lebih tinggi bermakna padanan yang lebih baik. Ini membolehkan anda mengesahkan jawapan dengan bahan sumber.

<a href="images/rag-query-results.png"><img src="../../../translated_images/ms/rag-query-results.6d69fcec5397f355.webp" alt="Keputusan Pertanyaan RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Keputusan pertanyaan yang menunjukkan jawapan dengan rujukan sumber dan skor relevansi*

### Experiment with Questions

Cuba pelbagai jenis soalan:  
- Fakta khusus: "Apakah topik utama?"  
- Perbandingan: "Apakah perbezaan antara X dan Y?"  
- Ringkasan: "Ringkaskan perkara utama tentang Z"  

Perhatikan bagaimana skor relevansi berubah berdasarkan sebaik mana soalan anda padan dengan kandungan dokumen.

## Key Concepts

### Chunking Strategy

Dokumen dipecah menjadi chunk 300 token dengan 30 token pertindihan. Keseimbangan ini memastikan setiap chunk mempunyai cukup konteks untuk bermakna sambil kekal kecil supaya boleh memasukkan beberapa chunk dalam satu permintaan.

### Similarity Scores

Setiap chunk yang diperoleh datang dengan skor kesamaan antara 0 hingga 1 yang menunjukkan betapa hampirnya ia padan dengan soalan pengguna. Rajah di bawah memvisualisasikan julat skor dan bagaimana sistem menggunakannya untuk menapis hasil:

<img src="../../../translated_images/ms/similarity-scores.b0716aa911abf7f0.webp" alt="Skor Kesamaan" width="800"/>

Skor dari 0 ke 1:  
- 0.7-1.0: Sangat relevan, padanan tepat  
- 0.5-0.7: Relevan, konteks baik  
- Di bawah 0.5: Ditapis keluar, terlalu berbeza  

Sistem hanya mengambil chunk di atas ambang minimum untuk memastikan kualiti.

### In-Memory Storage

Modul ini menggunakan penyimpanan dalam memori demi kesederhanaan. Apabila anda mulakan semula aplikasi, dokumen yang dimuat naik hilang. Sistem pengeluaran menggunakan pangkalan data vektor berterusan seperti Qdrant atau Azure AI Search.

### Context Window Management

Setiap model mempunyai tetingkap konteks maksimum. Anda tidak boleh memasukkan setiap chunk dari dokumen besar. Sistem mengambil N chunk paling relevan teratas (lalai 5) untuk kekal dalam had sambil menyediakan konteks cukup untuk jawapan tepat.

## When RAG Matters

RAG tidak sentiasa pendekatan yang tepat. Panduan keputusan di bawah membantu anda tentukan bila RAG menambah nilai berbanding bila pendekatan lebih mudah — seperti memasukkan kandungan terus dalam permintaan atau bergantung pada pengetahuan terpahat model — sudah mencukupi:

<img src="../../../translated_images/ms/when-to-use-rag.1016223f6fea26bc.webp" alt="Bila untuk Gunakan RAG" width="800"/>

**Gunakan RAG apabila:**
- Menjawab soalan mengenai dokumen proprietari
- Maklumat sering berubah (dasar, harga, spesifikasi)
- Ketepatan memerlukan atribusi sumber
- Kandungan terlalu besar untuk dimuat dalam satu prompt
- Anda memerlukan jawapan yang boleh disahkan dan berasaskan sumber

**Jangan gunakan RAG apabila:**
- Soalan memerlukan pengetahuan umum yang sudah dimiliki model
- Data masa nyata diperlukan (RAG berfungsi pada dokumen yang dimuat naik)
- Kandungan cukup kecil untuk dimasukkan terus dalam prompt

## Langkah Seterusnya

**Modul Seterusnya:** [04-tools - Ejen AI dengan Alat](../04-tools/README.md)

---

**Navigasi:** [← Sebelum: Modul 02 - Kejuruteraan Prompt](../02-prompt-engineering/README.md) | [Kembali ke Utama](../README.md) | [Seterusnya: Modul 04 - Alat →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila ambil maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang sahih. Untuk maklumat yang kritikal, disyorkan terjemahan profesional oleh manusia. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->