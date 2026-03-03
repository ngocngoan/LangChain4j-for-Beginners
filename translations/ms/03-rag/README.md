# Modul 03: RAG (Retrieval-Augmented Generation)

## Jadual Kandungan

- [Video Walkthrough](../../../03-rag)
- [Apa Yang Anda Akan Pelajari](../../../03-rag)
- [Prasyarat](../../../03-rag)
- [Memahami RAG](../../../03-rag)
  - [Pendekatan RAG Mana Yang Digunakan Oleh Tutorial Ini?](../../../03-rag)
- [Bagaimana Ia Berfungsi](../../../03-rag)
  - [Pemprosesan Dokumen](../../../03-rag)
  - [Mewujudkan Embedding](../../../03-rag)
  - [Carian Semantik](../../../03-rag)
  - [Penjanaan Jawapan](../../../03-rag)
- [Jalankan Aplikasi](../../../03-rag)
- [Menggunakan Aplikasi](../../../03-rag)
  - [Muat Naik Dokumen](../../../03-rag)
  - [Tanya Soalan](../../../03-rag)
  - [Semak Sumber Rujukan](../../../03-rag)
  - [Eksperimen Dengan Soalan](../../../03-rag)
- [Konsep Utama](../../../03-rag)
  - [Strategi Pemecahan (Chunking)](../../../03-rag)
  - [Skor Kesamaan](../../../03-rag)
  - [Penyimpanan Dalam Memori](../../../03-rag)
  - [Pengurusan Tetingkap Konteks](../../../03-rag)
- [Bilakah RAG Penting](../../../03-rag)
- [Langkah Seterusnya](../../../03-rag)

## Video Walkthrough

Tonton sesi langsung ini yang menerangkan cara untuk memulakan modul ini:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## Apa Yang Anda Akan Pelajari

Dalam modul-modul sebelum ini, anda telah belajar cara untuk berbual dengan AI dan menyusun prompt anda dengan berkesan. Tetapi terdapat had asas: model bahasa hanya tahu apa yang mereka pelajari semasa latihan. Mereka tidak dapat menjawab soalan mengenai polisi syarikat anda, dokumentasi projek anda, atau maklumat yang mereka tidak dilatih.

RAG (Retrieval-Augmented Generation) menyelesaikan masalah ini. Daripada cuba mengajar model maklumat anda (yang mahal dan tidak praktikal), anda memberikannya keupayaan untuk mencari dalam dokumen anda. Apabila seseorang bertanya soalan, sistem akan mencari maklumat yang berkaitan dan menyertakannya dalam prompt. Model kemudian menjawab berdasarkan konteks yang diperoleh itu.

Fikirkan RAG sebagai memberikan model sebuah perpustakaan rujukan. Apabila anda bertanya soalan, sistem:

1. **Pertanyaan Pengguna** - Anda bertanya soalan  
2. **Embedding** - Menukar soalan anda kepada vektor  
3. **Carian Vektor** - Mencari kepingan dokumen yang serupa  
4. **Penggabungan Konteks** - Menambah kepingan yang berkaitan ke dalam prompt  
5. **Respons** - LLM menjana jawapan berdasarkan konteks  

Ini menjadikan respons model berasaskan data sebenar anda dan bukan bergantung pada pengetahuan latihan atau menjana jawapan rekaan.

## Prasyarat

- Menyelesaikan [Modul 00 - Quick Start](../00-quick-start/README.md) (untuk contoh Easy RAG yang dirujuk kemudian dalam modul ini)  
- Menyelesaikan [Modul 01 - Pengenalan](../01-introduction/README.md) (sumber Azure OpenAI telah ditempatkan, termasuk model embedding `text-embedding-3-small`)  
- Fail `.env` di direktori root dengan kelayakan Azure (dicipta oleh `azd up` dalam Modul 01)  

> **Nota:** Jika anda belum menyelesaikan Modul 01, ikut arahan pelaksanaan di situ dahulu. Perintah `azd up` akan melaksanakan kedua-dua model chat GPT dan model embedding yang digunakan oleh modul ini.

## Memahami RAG

Rajah di bawah menggambarkan konsep utama: daripada bergantung hanya pada data latihan model, RAG memberikan perpustakaan rujukan dokumen anda untuk dirujuk sebelum menjana setiap jawapan.

<img src="../../../translated_images/ms/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*Rajah ini menunjukkan perbezaan antara LLM standard (yang meneka berdasarkan data latihan) dan LLM yang diperkuat RAG (yang merujuk dokumen anda terlebih dahulu).*

Ini adalah bagaimana komponen-komponen itu bersambung dari hujung ke hujung. Soalan pengguna mengalir melalui empat peringkat — embedding, carian vektor, penggabungan konteks, dan penjanaan jawapan — setiap satu membina atas yang sebelumnya:

<img src="../../../translated_images/ms/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*Rajah ini menunjukkan pipeline RAG dari hujung ke hujung — pertanyaan pengguna mengalir melalui embedding, carian vektor, penggabungan konteks, dan penjanaan jawapan.*

Selepas itu, modul ini menerangkan setiap peringkat secara terperinci, dengan kod yang boleh anda jalankan dan ubah suai.

### Pendekatan RAG Mana Yang Digunakan Oleh Tutorial Ini?

LangChain4j menawarkan tiga cara untuk melaksanakan RAG, masing-masing dengan tahap abstraksi yang berbeza. Rajah di bawah membandingkannya secara berdampingan:

<img src="../../../translated_images/ms/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*Rajah ini membandingkan tiga pendekatan RAG LangChain4j — Easy, Native, dan Advanced — menunjukkan komponen utama mereka dan bila untuk menggunakan setiap satu.*

| Pendekatan | Apa Yang Dilakukan | Timbangan |
|---|---|---|
| **Easy RAG** | Menghubungkan semuanya secara automatik melalui `AiServices` dan `ContentRetriever`. Anda mentanda sebuah antaramuka, lampirkan retriever, dan LangChain4j mengurus embedding, pencarian, dan pemasangan prompt secara belakang tabir. | Kod minimum, tetapi anda tidak nampak apa yang berlaku pada setiap langkah. |
| **Native RAG** | Anda panggil model embedding, cari stor, bina prompt, dan jana jawapan sendiri — satu langkah jelas pada satu masa. | Lebih banyak kod, tetapi setiap peringkat jelas dan boleh diubah. |
| **Advanced RAG** | Menggunakan rangka kerja `RetrievalAugmentor` dengan transformer pertanyaan boleh sambung, penentu laluan, penilaian semula, dan penyuntik kandungan untuk talian produksi. | Fleksibiliti maksimum, tetapi dengan kerumitan yang jauh lebih tinggi. |

**Tutorial ini menggunakan pendekatan Native.** Setiap langkah pipeline RAG — embedding pertanyaan, carian stor vektor, penggabungan konteks, dan penjanaan jawapan — ditulis dengan jelas dalam [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Ini memang disengajakan: sebagai sumber pembelajaran, lebih penting anda lihat dan faham setiap peringkat daripada kod diperkecilkan. Setelah anda selesa dengan bagaimana komponen-komponen itu bersambung, anda boleh beralih ke Easy RAG untuk prototaip cepat atau Advanced RAG untuk sistem produksi.

> **💡 Sudah lihat Easy RAG beraksi?** Modul [Quick Start](../00-quick-start/README.md) termasuk contoh Soal Jawab Dokumen ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) yang menggunakan pendekatan Easy RAG — LangChain4j mengendalikan embedding, pencarian, dan pemasangan prompt secara automatik. Modul ini membawa langkah seterusnya dengan membuka pipeline itu supaya anda dapat melihat dan kawal setiap peringkat sendiri.

Rajah di bawah menunjukkan pipeline Easy RAG daripada contoh Quick Start itu. Perhatikan bagaimana `AiServices` dan `EmbeddingStoreContentRetriever` menyembunyikan semua kerumitan — anda memuat naik dokumen, lampirkan retriever, dan dapat jawapan. Pendekatan Native dalam modul ini memecahkan setiap langkah tersembunyi itu:

<img src="../../../translated_images/ms/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Rajah ini menunjukkan pipeline Easy RAG dari `SimpleReaderDemo.java`. Bandingkan dengan pendekatan Native yang digunakan dalam modul ini: Easy RAG menyembunyikan embedding, pengambilan, dan pemasangan prompt di belakang `AiServices` dan `ContentRetriever` — anda muat naik dokumen, lampirkan retriever, dan dapat jawapan. Pendekatan Native dalam modul ini membuka pipeline itu supaya anda panggil setiap peringkat (embed, cari, gabung konteks, jana) sendiri, memberi anda keterlihatan dan kawalan penuh.*

## Bagaimana Ia Berfungsi

Pipeline RAG dalam modul ini terbahagi kepada empat peringkat yang dijalankan secara berurutan setiap kali pengguna bertanya soalan. Pertama, dokumen yang dimuat naik **ditafsir dan dipecah** kepada kepingan yang boleh diurus. Kepingan-kepingan itu kemudiannya ditukar menjadi **embedding vektor** dan disimpan supaya dapat dibandingkan secara matematik. Apabila pertanyaan masuk, sistem melaksanakan **carian semantik** untuk mencari kepingan yang paling relevan, dan akhirnya menggunakannya sebagai konteks kepada LLM untuk **penjanaan jawapan**. Bahagian-bahagian berikut menerangkan setiap peringkat dengan kod sebenar dan rajah. Mari lihat peringkat pertama.

### Pemprosesan Dokumen

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Apabila anda memuat naik dokumen, sistem akan menafsirkannya (PDF atau teks biasa), melampirkan metadata seperti nama fail, dan kemudian memecahkannya menjadi kepingan — bahagian lebih kecil yang sesuai dengan tetingkap konteks model. Kepingan itu bertindih sedikit supaya konteks penting tidak hilang di sempadan.

```java
// Huraikan fail yang dimuat naik dan bungkus dalam Dokumen LangChain4j
Document document = Document.from(content, metadata);

// Bahagikan kepada cebisan 300-token dengan pertindihan 30-token
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Rajah di bawah menunjukkan bagaimana ia berfungsi secara visual. Perhatikan bagaimana setiap kepingan berkongsi sebahagian token dengan jiran-jirannya — pertindihan 30 token memastikan tiada konteks penting yang terlepas:

<img src="../../../translated_images/ms/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*Rajah ini menunjukkan dokumen dipecah kepada kepingan 300-token dengan pertindihan 30-token, mengekalkan konteks di sempadan kepingan.*

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) dan tanya:  
> - "Bagaimana LangChain4j memecahkan dokumen kepada kepingan dan mengapa pertindihan penting?"  
> - "Apakah saiz kepingan optimum untuk jenis dokumen yang berlainan dan kenapa?"  
> - "Bagaimana saya mengendalikan dokumen dalam pelbagai bahasa atau dengan format khas?"

### Mewujudkan Embedding

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Setiap kepingan ditukar menjadi representasi nombor yang dipanggil embedding — secara asasnya penukar makna kepada nombor. Model embedding bukan "cerdik" seperti model chat; ia tidak boleh ikut arahan, membuat penalaran, atau menjawab soalan. Apa yang boleh dilakukan ialah memetakan teks ke ruang matematik di mana makna yang serupa terletak berhampiran antara satu sama lain — "kereta" dekat dengan "automobil", "dasar bayaran balik" dekat dengan "pulangkan wang saya." Anggap model chat sebagai orang yang boleh anda ajak berbual; model embedding adalah sistem pengarkiban yang sangat bagus.

Rajah di bawah memvisualisasikan konsep ini — teks masuk, vektor nombor keluar, dan makna serupa menghasilkan vektor yang berhampiran:

<img src="../../../translated_images/ms/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*Rajah ini menunjukkan bagaimana model embedding menukar teks menjadi vektor nombor, meletakkan makna serupa — seperti "kereta" dan "automobil" — berhampiran antara satu sama lain dalam ruang vektor.*

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
  
Rajah kelas di bawah menunjukkan dua aliran berasingan dalam pipeline RAG dan kelas LangChain4j yang melaksanakannya. **Aliran ingest** (dijalankan sekali semasa muat naik) memecahkan dokumen, menghasilkan embedding untuk kepingan, dan menyimpan melalui `.addAll()`. **Aliran pertanyaan** (dijalankan setiap kali pengguna bertanya) menghasilkan embedding untuk soalan, mencari dalam stor melalui `.search()`, dan menyerahkan konteks yang dipadankan ke model chat. Kedua-dua aliran bertemu pada antaramuka `EmbeddingStore<TextSegment>` yang dikongsi:

<img src="../../../translated_images/ms/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*Rajah ini menunjukkan dua aliran dalam pipeline RAG — ingest dan pertanyaan — dan bagaimana mereka bersambung melalui EmbeddingStore yang dikongsi.*

Setelah embedding disimpan, kandungan serupa secara semulajadi mengelompok bersama dalam ruang vektor. Visualisasi di bawah menunjukkan bagaimana dokumen berkaitan topik berkumpul sebagai titik berdekatan, yang membolehkan carian semantik:

<img src="../../../translated_images/ms/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*Visualisasi ini menunjukkan bagaimana dokumen berkaitan berkumpul bersama dalam ruang vektor 3D, dengan topik seperti Dokumen Teknikal, Peraturan Perniagaan, dan Soalan Lazim membentuk kelompok berbeza.*

Apabila pengguna mencari, sistem mengikuti empat langkah: embedkan dokumen sekali sahaja, embedkan pertanyaan pada setiap carian, bandingkan vektor pertanyaan dengan semua vektor yang disimpan menggunakan persamaan kosinus, dan pulangkan top-K kepingan dengan skor tertinggi. Rajah di bawah menerangkan setiap langkah dan kelas LangChain4j yang terlibat:

<img src="../../../translated_images/ms/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*Rajah ini menunjukkan empat langkah proses carian embedding: embed dokumen, embed pertanyaan, bandingkan vektor dengan persamaan kosinus, dan pulangkan hasil top-K.*

### Carian Semantik

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Apabila anda bertanya soalan, soalan anda juga ditukar menjadi embedding. Sistem membandingkan embedding soalan anda dengan semua embedding kepingan dokumen. Ia mencari kepingan yang mempunyai makna paling serupa — bukan sahaja berdasarkan kata kunci yang sepadan, tetapi kesamaan semantik sebenar.

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
  
Rajah di bawah membezakan carian semantik dengan carian kata kunci tradisional. Carian kata kunci untuk "vehicle" terlepas kepingan mengenai "kereta dan trak," tetapi carian semantik faham mereka bermaksud perkara yang sama dan mengembalikannya sebagai padanan skor tinggi:

<img src="../../../translated_images/ms/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*Rajah ini membandingkan carian berdasarkan kata kunci dengan carian semantik, menunjukkan bagaimana carian semantik mengambil kandungan berkaitan konsep walaupun kata kunci tepat berbeza.*
Di sebalik tabir, kesamaan diukur menggunakan kesamaan kosinus — secara asasnya bertanya "adakah kedua-dua anak panah ini menunjuk ke arah yang sama?" Dua potongan boleh menggunakan perkataan yang berbeza sama sekali, tetapi jika maksudnya sama, vektor mereka menunjuk ke arah yang sama dan skor hampir kepada 1.0:

<img src="../../../translated_images/ms/cosine-similarity.9baeaf3fc3336abb.webp" alt="Kesamaan Kosinus" width="800"/>

*Rajah ini menggambarkan kesamaan kosinus sebagai sudut antara vektor pelampaan — vektor yang lebih selari memberikan skor hampir kepada 1.0, yang menunjukkan kesamaan semantik yang lebih tinggi.*

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) dan tanya:
> - "Bagaimanakah carian kesamaan berfungsi dengan pelampaan dan apakah yang menentukan skor?"
> - "Apakah ambang kesamaan yang harus saya gunakan dan bagaimana ia mempengaruhi keputusan?"
> - "Bagaimana saya mengendalikan kes apabila tiada dokumen yang berkaitan ditemui?"

### Penjanaan Jawapan

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Potongan paling relevan akan disusun menjadi arahan berstruktur yang merangkumi arahan eksplisit, konteks yang diperolehi, dan soalan pengguna. Model membaca potongan khusus tersebut dan menjawab berdasarkan maklumat itu — ia hanya boleh menggunakan apa yang ada di hadapannya, yang mencegah halusinasi.

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

Rajah di bawah menunjukkan penyusunan ini dalam tindakan — potongan dengan skor tertinggi dari langkah carian disisipkan ke dalam templat arahan, dan `OpenAiOfficialChatModel` menjana jawapan berasaskan bukti:

<img src="../../../translated_images/ms/context-assembly.7e6dd60c31f95978.webp" alt="Penyusunan Konteks" width="800"/>

*Rajah ini menunjukkan bagaimana potongan dengan skor tertinggi disusun menjadi arahan berstruktur, membolehkan model menjana jawapan berasaskan data anda.*

## Jalankan Aplikasi

**Sahkan penyebaran:**

Pastikan fail `.env` wujud di direktori akar dengan kelayakan Azure (dibuat semasa Modul 01). Jalankan dari direktori modul (`03-rag/`):

**Bash:**
```bash
cat ../.env  # Harus menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Perlu memaparkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Mulakan aplikasi:**

> **Nota:** Jika anda sudah memulakan semua aplikasi menggunakan `./start-all.sh` dari direktori akar (seperti yang diterangkan dalam Modul 01), modul ini sudah berjalan di port 8081. Anda boleh abaikan arahan mula di bawah dan terus ke http://localhost:8081.

**Pilihan 1: Menggunakan Spring Boot Dashboard (Disyorkan untuk pengguna VS Code)**

Kontena dev termasuk sambungan Spring Boot Dashboard, yang menyediakan antara muka visual untuk mengurus semua aplikasi Spring Boot. Anda boleh menemukannya di Bar Aktiviti di sebelah kiri VS Code (cari ikon Spring Boot).

Daripada Spring Boot Dashboard, anda boleh:
- Melihat semua aplikasi Spring Boot yang tersedia dalam ruang kerja
- Mula/hentikan aplikasi dengan satu klik
- Lihat log aplikasi secara masa nyata
- Pantau status aplikasi

Klik butang main di sebelah "rag" untuk mula modul ini, atau mula semua modul sekaligus.

<img src="../../../translated_images/ms/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Tangkapan skrin ini menunjukkan Spring Boot Dashboard dalam VS Code, di mana anda boleh mula, hentikan, dan pantau aplikasi secara visual.*

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

Atau mulakan hanya modul ini sahaja:

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

Kedua-dua skrip secara automatik memuatkan pembolehubah persekitaran dari fail `.env` akar dan akan membina JAR jika belum wujud.

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

**Untuk hentikan:**

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

## Menggunakan Aplikasi

Aplikasi menyediakan antara muka web untuk muat naik dokumen dan soal jawab.

<a href="images/rag-homepage.png"><img src="../../../translated_images/ms/rag-homepage.d90eb5ce1b3caa94.webp" alt="Antara Muka Aplikasi RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Tangkapan skrin ini menunjukkan antara muka aplikasi RAG di mana anda memuat naik dokumen dan mengemukakan soalan.*

### Muat Naik Dokumen

Mula dengan memuat naik dokumen - fail TXT paling sesuai untuk ujian. Sebuah `sample-document.txt` disediakan dalam direktori ini yang mengandungi maklumat tentang ciri LangChain4j, pelaksanaan RAG, dan amalan terbaik - sesuai untuk menguji sistem.

Sistem memproses dokumen anda, memecahnya menjadi potongan, dan mencipta pelampaan untuk setiap potongan. Ini berlaku secara automatik apabila anda memuat naik.

### Tanyakan Soalan

Sekarang tanya soalan khusus tentang kandungan dokumen. Cuba sesuatu yang faktual yang diterangkan dengan jelas dalam dokumen. Sistem mencari potongan yang berkaitan, memasukkannya dalam arahan, dan menjana jawapan.

### Semak Rujukan Sumber

Perhatikan setiap jawapan termasuk rujukan sumber dengan skor kesamaan. Skor ini (0 hingga 1) menunjukkan betapa relevannya setiap potongan dengan soalan anda. Skor lebih tinggi bermakna padanan lebih baik. Ini membolehkan anda mengesahkan jawapan berdasarkan bahan sumber.

<a href="images/rag-query-results.png"><img src="../../../translated_images/ms/rag-query-results.6d69fcec5397f355.webp" alt="Keputusan Pertanyaan RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Tangkapan skrin ini menunjukkan keputusan pertanyaan dengan jawapan yang dijana, rujukan sumber, dan skor relevansi untuk setiap potongan yang diperoleh.*

### Eksperimen dengan Soalan

Cuba jenis soalan yang berbeza:
- Fakta khusus: "Apakah topik utama?"
- Perbandingan: "Apakah perbezaan antara X dan Y?"
- Ringkasan: "Ringkaskan perkara utama tentang Z"

Perhatikan bagaimana skor relevansi berubah berdasarkan sejauh mana soalan anda padan dengan kandungan dokumen.

## Konsep Utama

### Strategi Penyusunan Potongan

Dokumen dibahagikan kepada potongan 300 token dengan tumpang tindih 30 token. Keseimbangan ini memastikan setiap potongan mempunyai konteks cukup untuk bermakna sambil cukup kecil untuk memasukkan beberapa potongan dalam satu arahan.

### Skor Kesamaan

Setiap potongan yang diperoleh datang dengan skor kesamaan antara 0 dan 1 yang menunjukkan betapa rapatnya ia padan dengan soalan pengguna. Rajah di bawah memvisualisasikan julat skor dan bagaimana sistem menggunakannya untuk menapis keputusan:

<img src="../../../translated_images/ms/similarity-scores.b0716aa911abf7f0.webp" alt="Skor Kesamaan" width="800"/>

*Rajah ini menunjukkan julat skor dari 0 hingga 1, dengan ambang minimum 0.5 yang menapis potongan yang tidak relevan.*

Skor dari 0 hingga 1:
- 0.7-1.0: Sangat relevan, padanan tepat
- 0.5-0.7: Relevan, konteks baik
- Kurang dari 0.5: Ditapis keluar, terlalu tidak serupa

Sistem hanya memperoleh potongan di atas ambang minimum untuk memastikan kualiti.

Pelampaan berfungsi dengan baik apabila makna berkumpulan dengan jelas, tetapi ia mempunyai kekurangan. Rajah di bawah menunjukkan mod kegagalan biasa — potongan yang terlalu besar menghasilkan vektor tidak jelas, potongan terlalu kecil kurang konteks, istilah kabur menunjuk kepada pelbagai kelompok, dan pencarian padanan tepat (ID, nombor bahagian) tidak berfungsi dengan pelampaan sama sekali:

<img src="../../../translated_images/ms/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Mod Kegagalan Pelampaan" width="800"/>

*Rajah ini menunjukkan mod kegagalan pelampaan biasa: potongan terlalu besar, potongan terlalu kecil, istilah kabur yang menunjuk ke beberapa kelompok, dan carian padanan tepat seperti ID.*

### Penyimpanan Dalam Memori

Modul ini menggunakan penyimpanan dalam memori untuk kesederhanaan. Apabila anda mulakan semula aplikasi, dokumen yang dimuat naik akan hilang. Sistem produksi menggunakan pangkalan data vektor kekal seperti Qdrant atau Azure AI Search.

### Pengurusan Tetingkap Konteks

Setiap model mempunyai tetingkap konteks maksimum. Anda tidak boleh memasukkan semua potongan dari dokumen besar. Sistem memperoleh N potongan paling relevan (lalai 5) untuk kekal dalam had sambil menyediakan konteks cukup untuk jawapan tepat.

## Bila RAG Penting

RAG tidak selalu pendekatan yang betul. Panduan keputusan di bawah membantu anda menentukan bila RAG menambah nilai berbanding bila pendekatan lebih mudah — seperti memasukkan kandungan terus dalam arahan atau bergantung pada pengetahuan terbina dalam model — cukup:

<img src="../../../translated_images/ms/when-to-use-rag.1016223f6fea26bc.webp" alt="Bila Menggunakan RAG" width="800"/>

*Rajah ini menunjukkan panduan keputusan untuk bila RAG menambah nilai berbanding pendekatan lebih mudah sudah mencukupi.*

## Langkah Seterusnya

**Modul Seterusnya:** [04-tools - Ejen AI dengan Alat](../04-tools/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 02 - Rekabentuk Arahan](../02-prompt-engineering/README.md) | [Kembali ke Utama](../README.md) | [Seterusnya: Modul 04 - Alat →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila maklum bahawa terjemahan automatik mungkin mengandungi ralat atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang sahih. Untuk maklumat penting, terjemahan profesional oleh manusia adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->