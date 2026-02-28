# Modul 03: RAG (Retrieval-Augmented Generation)

## Jadual Kandungan

- [Penerangan Video](../../../03-rag)
- [Apa Yang Akan Anda Pelajari](../../../03-rag)
- [Prasyarat](../../../03-rag)
- [Memahami RAG](../../../03-rag)
  - [Pendekatan RAG Mana Yang Digunakan Tutorial Ini?](../../../03-rag)
- [Bagaimana Ia Berfungsi](../../../03-rag)
  - [Pemprosesan Dokumen](../../../03-rag)
  - [Mewujudkan Embeddings](../../../03-rag)
  - [Carian Semantik](../../../03-rag)
  - [Penjanaan Jawapan](../../../03-rag)
- [Jalankan Aplikasi](../../../03-rag)
- [Menggunakan Aplikasi](../../../03-rag)
  - [Muat Naik Dokumen](../../../03-rag)
  - [Ajukan Soalan](../../../03-rag)
  - [Semak Rujukan Sumber](../../../03-rag)
  - [Bereksperimen dengan Soalan](../../../03-rag)
- [Konsep Utama](../../../03-rag)
  - [Strategi Pemecahan](../../../03-rag)
  - [Skor Kesesuaian](../../../03-rag)
  - [Penyimpanan Dalam Memori](../../../03-rag)
  - [Pengurusan Tetingkap Konteks](../../../03-rag)
- [Bila RAG Penting](../../../03-rag)
- [Langkah Seterusnya](../../../03-rag)

## Penerangan Video

Tonton sesi langsung ini yang menerangkan cara memulakan modul ini:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## Apa Yang Akan Anda Pelajari

Dalam modul-modul sebelumnya, anda telah belajar bagaimana untuk berbual dengan AI dan menyusun arahan anda dengan berkesan. Tetapi terdapat batasan asas: model bahasa hanya tahu apa yang mereka pelajari semasa latihan. Mereka tidak boleh menjawab soalan mengenai polisi syarikat anda, dokumentasi projek anda, atau apa-apa maklumat yang mereka tidak dilatih.

RAG (Retrieval-Augmented Generation) menyelesaikan masalah ini. Daripada cuba mengajar model maklumat anda (yang mahal dan tidak praktikal), anda memberinya kemampuan untuk mencari maklumat dalam dokumen anda. Apabila seseorang mengemukakan soalan, sistem ini mencari maklumat yang relevan dan memasukkannya dalam arahan. Model kemudiannya menjawab berdasarkan konteks yang diperoleh itu.

Fikirkan RAG sebagai memberikan model sebuah perpustakaan rujukan. Apabila anda bertanya soalan, sistem:

1. **Pertanyaan Pengguna** - Anda bertanya soalan  
2. **Embedding** - Menukarkan soalan anda ke dalam vektor  
3. **Carian Vektor** - Mencari kepingan dokumen yang serupa  
4. **Penyusunan Konteks** - Menambah kepingan relevan ke arahan  
5. **Respons** - LLM menghasilkan jawapan berdasarkan konteks  

Ini menjadikan jawapan model berasaskan data sebenar anda dan bukan hanya berdasarkan pengetahuan latihan atau menghasilkan jawapan secara rekaan.

## Prasyarat

- Lengkapkan [Modul 00 - Quick Start](../00-quick-start/README.md) (untuk contoh Easy RAG yang dirujuk di atas)  
- Lengkapkan [Modul 01 - Pengenalan](../01-introduction/README.md) (sumber Azure OpenAI sudah dideploy, termasuk model embedding `text-embedding-3-small`)  
- Fail `.env` di direktori root dengan kelayakan Azure (dicipta oleh `azd up` dalam Modul 01)  

> **Nota:** Jika anda belum lengkap Modul 01, ikut arahan deployment di sana dahulu. Perintah `azd up` akan deploy kedua-dua model chat GPT dan model embedding yang digunakan oleh modul ini.

## Memahami RAG

Rajah di bawah menerangkan konsep teras: daripada hanya bergantung pada data latihan model, RAG memberikannya perpustakaan rujukan dokumen anda untuk dirujuk sebelum menjana setiap jawapan.

<img src="../../../translated_images/ms/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*Rajah ini menunjukkan perbezaan antara LLM biasa (yang meneka berdasarkan data latihan) dan LLM yang diperkaya RAG (yang merujuk dokumen anda terlebih dahulu).*

Berikut adalah cara setiap bahagian disambungkan dari awal hingga akhir. Soalan pengguna mengalir melalui empat tahap — embedding, carian vektor, penyusunan konteks, dan penjanaan jawapan — setiap satu dibina atas tahap sebelumnya:

<img src="../../../translated_images/ms/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*Rajah ini menunjukkan saluran RAG dari awal hingga akhir — pertanyaan pengguna mengalir melalui embedding, carian vektor, penyusunan konteks, dan penjanaan jawapan.*

Selepas ini, modul akan menerangkan setiap peringkat dengan terperinci, lengkap dengan kod yang boleh anda jalankan dan ubahsuai.

### Pendekatan RAG Mana Yang Digunakan Tutorial Ini?

LangChain4j menawarkan tiga cara untuk melaksanakan RAG, masing-masing dengan tahap abstraksi berbeza. Rajah di bawah membandingkan mereka secara berdampingan:

<img src="../../../translated_images/ms/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*Rajah ini membandingkan tiga pendekatan RAG LangChain4j — Easy, Native, dan Advanced — menunjukkan komponen utama dan bila untuk guna masing-masing.*

| Pendekatan | Apa Yang Dilakukan | Pertukaran |
|---|---|---|
| **Easy RAG** | Menghubungkan semua secara automatik melalui `AiServices` dan `ContentRetriever`. Anda tandakan antara muka, lampirkan retriever, dan LangChain4j mengendalikan embedding, pencarian, dan penyusunan prompt di belakang tabir. | Kod minimum, tetapi anda tidak nampak apa yang berlaku pada setiap langkah. |
| **Native RAG** | Anda panggil model embedding, cari dalam stor, bina prompt, dan jana jawapan sendiri — satu langkah jelas pada satu masa. | Lebih banyak kod, tetapi setiap tahap jelas dan boleh diubahsuai. |
| **Advanced RAG** | Menggunakan rangka kerja `RetrievalAugmentor` dengan transformer pertanyaan, perute, penilai semula, dan pemasuk kandungan yang boleh disambung untuk saluran produksi yang lengkap. | Fleksibiliti maksimum, tetapi dengan tahap kerumitan yang jauh lebih tinggi. |

**Tutorial ini menggunakan pendekatan Native.** Setiap langkah saluran RAG — embedding pertanyaan, carian stor vektor, penyusunan konteks, dan penjanaan jawapan — ditulis dengan jelas dalam [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Ini sengaja dibuat: sebagai sumber pembelajaran, lebih penting anda nampak dan faham setiap peringkat daripada kod yang terlalu diperkecilkan. Setelah anda biasa dengan cara bahagian-bahagian ini berfungsi, anda boleh beralih ke Easy RAG untuk prototaip cepat atau Advanced RAG untuk sistem produksi.

> **💡 Sudah lihat Easy RAG beraksi?** Modul [Quick Start](../00-quick-start/README.md) mengandungi contoh Document Q&A ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) yang menggunakan pendekatan Easy RAG — LangChain4j mengendalikan embedding, carian, dan penyusunan prompt secara automatik. Modul ini pula membuka saluran itu supaya anda boleh lihat dan kawal setiap tahap sendiri.

<img src="../../../translated_images/ms/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Rajah ini menunjukkan saluran Easy RAG daripada `SimpleReaderDemo.java`. Bandingkan dengan pendekatan Native yang digunakan dalam modul ini: Easy RAG menyembunyikan embedding, penemuan, dan penyusunan prompt di belakang `AiServices` dan `ContentRetriever` — anda hanya muat naik dokumen, pasang retriever, dan dapatkan jawapan. Pendekatan Native dalam modul ini membuka saluran tersebut supaya anda panggil sendiri setiap langkah (embed, cari, susun konteks, jana), memberi anda penglihatan dan kawalan penuh.*

## Bagaimana Ia Berfungsi

Saluran RAG dalam modul ini dibahagikan kepada empat peringkat yang dijalankan secara berurutan setiap kali pengguna bertanya soalan. Pertama, dokumen yang dimuat naik **diparse dan dipecahkan** kepada kepingan yang boleh diurus. Kepingan itu kemudiannya ditukar menjadi **embedding vektor** dan disimpan supaya boleh dibandingkan secara matematik. Apabila pertanyaan tiba, sistem menjalankan **carian semantik** untuk mencari kepingan yang paling relevan, dan akhirnya menyerahkan kepingan itu sebagai konteks kepada LLM untuk **penjanaan jawapan**. Bahagian-bahagian di bawah menerangkan setiap peringkat dengan kod sebenar dan rajah. Mari lihat pada langkah pertama.

### Pemprosesan Dokumen

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Apabila anda memuat naik dokumen, sistem memparse dokumen itu (PDF atau teks biasa), menambah metadata seperti nama fail, dan kemudian memecahkan dokumen itu kepada kepingan — bahagian kecil yang muat dengan selesa dalam tetingkap konteks model. Kepingan-kepingan itu bertindih sedikit supaya anda tidak kehilangan konteks di sempadan.

```java
// Huraikan fail yang dimuat naik dan bungkuskan dalam Dokumen LangChain4j
Document document = Document.from(content, metadata);

// Bahagikan kepada ketulan 300-token dengan pertindihan 30-token
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Rajah di bawah menunjukkan bagaimana ini berfungsi secara visual. Perhatikan bagaimana setiap kepingan berkongsi beberapa token dengan jiran-jirannya — pertindihan 30 token memastikan tiada konteks penting hilang di celah-celah:

<img src="../../../translated_images/ms/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*Rajah ini menunjukkan dokumen dipecahkan kepada kepingan 300 token dengan pertindihan 30 token, mengekalkan konteks di sempadan kepingan.*

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) dan tanya:  
> - "Bagaimana LangChain4j memecahkan dokumen menjadi kepingan dan mengapa pertindihan itu penting?"  
> - "Apakah saiz kepingan optimum untuk jenis dokumen berbeza dan mengapa?"  
> - "Bagaimana saya mengendalikan dokumen dalam pelbagai bahasa atau dengan format khas?"

### Mewujudkan Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Setiap kepingan ditukar menjadi representasi berangka yang dipanggil embedding — secara asasnya penukar makna kepada nombor. Model embedding ini tidak "bijak" seperti model chat; ia tidak boleh mengikuti arahan, berfikir secara rasional, atau menjawab soalan. Apa yang boleh dilakukannya adalah memetakan teks ke dalam ruang matematik di mana makna yang serupa berkumpul berdekatan — "kereta" berhampiran "automobil", "dasar bayaran balik" dekat "kembalikan wang saya." Fikirkan model chat sebagai seseorang yang anda boleh ajak berbual; model embedding adalah sistem fail yang sangat baik.

<img src="../../../translated_images/ms/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*Rajah ini menunjukkan bagaimana model embedding menukar teks kepada vektor berangka, meletakkan makna yang serupa — seperti "kereta" dan "automobil" — berdekatan antara satu sama lain dalam ruang vektor.*

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
  
Rajah kelas di bawah menunjukkan dua aliran berasingan dalam saluran RAG dan kelas LangChain4j yang melaksanakan mereka. **Aliran pengambilan (ingestion)** (dijalankan sekali semasa muat naik) memecahkan dokumen, membuat embedding kepingan, dan menyimpannya melalui `.addAll()`. **Aliran pertanyaan (query)** (dijalankan setiap kali pengguna bertanya) membuat embedding soalan, mencari dalam stor melalui `.search()`, dan menyerahkan konteks padanan ke model chat. Kedua-dua aliran ini berjumpa di antara muka bersama `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/ms/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*Rajah ini menunjukkan kedua-dua aliran dalam saluran RAG — pengambilan dan pertanyaan — dan bagaimana mereka disambungkan melalui EmbeddingStore bersama.*

Setelah embedding disimpan, kandungan serupa secara semula jadi berkumpulan dalam ruang vektor. Visualisasi di bawah menunjukkan bagaimana dokumen berkaitan subjek muncul sebagai titik berhampiran, yang memungkinkan carian semantik:

<img src="../../../translated_images/ms/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*Visualisasi ini menunjukkan bagaimana dokumen berkaitan berkumpul bersama dalam ruang vektor 3D, dengan topik seperti Dokumen Teknikal, Peraturan Perniagaan, dan Soalan Lazim membentuk kumpulan tersendiri.*

Apabila pengguna membuat carian, sistem mengikuti empat langkah: embed dokumen sekali, embed pertanyaan setiap kali carian dibuat, bandingkan vektor pertanyaan dengan semua vektor tersimpan menggunakan keserupaan kosinus, dan pulangkan kepingan teratas dengan skor tertinggi. Rajah di bawah menerangkan setiap langkah dan kelas LangChain4j yang terlibat:

<img src="../../../translated_images/ms/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*Rajah ini menunjukkan proses carian embedding empat langkah: embed dokumen, embed pertanyaan, bandingkan vektor dengan keserupaan kosinus, dan pulangkan keputusan teratas.*

### Carian Semantik

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Apabila anda bertanya soalan, soalan anda juga akan diembeddingkan. Sistem membandingkan embedding soalan anda dengan semua embedding kepingan dokumen. Ia mencari kepingan yang paling serupa maknanya - bukan hanya kata kunci yang sama, tetapi keserupaan semantik sebenar.

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
  
Rajah di bawah membezakan carian semantik dengan carian kata kunci tradisional. Carian kata kunci untuk "vehicle" terlepas satu kepingan tentang "kereta dan lori," tetapi carian semantik faham bahawa ia membawa maksud sama dan mengembalikannya sebagai padanan skor tinggi:

<img src="../../../translated_images/ms/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*Rajah ini membandingkan carian berdasarkan kata kunci dengan carian semantik, menunjukkan bagaimana carian semantik mengambil kandungan yang berkaitan secara konsep walaupun kata kunci tepat berbeza.*

Di belakang tabir, keserupaan diukur menggunakan keserupaan kosinus — pada asasnya bertanya "adakah dua anak panah ini menunjuk ke arah yang sama?" Dua kepingan boleh menggunakan perkataan yang berbeza sama sekali, tetapi jika maknanya sama, vektor mereka akan menunjuk arah yang sama dan skor hampir kepada 1.0:

<img src="../../../translated_images/ms/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>
*Rajah ini menggambarkan keserupaan kosinus sebagai sudut antara vektor penanaman — vektor yang lebih sejajar mencapai skor lebih dekat ke 1.0, menunjukkan keserupaan semantik yang lebih tinggi.*

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) dan tanya:
> - "Bagaimana caranya pencarian keserupaan berfungsi dengan penanaman dan apa yang menentukan skor?"
> - "Ambang keserupaan apa yang harus saya gunakan dan bagaimana ia mempengaruhi hasil?"
> - "Bagaimana saya mengendalikan kes dimana tiada dokumen yang relevan dijumpai?"

### Penjanaan Jawapan

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kepingan paling relevan disusun menjadi satu prompt berstruktur yang merangkumi arahan eksplisit, konteks yang diperoleh, dan soalan pengguna. Model membaca kepingan tertentu itu dan menjawab berdasarkan maklumat tersebut — ia hanya boleh menggunakan apa yang ada di hadapannya, yang menghalang halusinasi.

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

Rajah di bawah menunjukkan penyusunan ini dalam tindakan — kepingan dengan skor tertinggi dari langkah pencarian disuntik ke dalam templat prompt, dan `OpenAiOfficialChatModel` menjana jawapan yang berasaskan data:

<img src="../../../translated_images/ms/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Rajah ini menunjukkan bagaimana kepingan dengan skor tertinggi disusun menjadi prompt berstruktur, membolehkan model menjana jawapan berasaskan data anda.*

## Jalankan Aplikasi

**Sahkan penyebaran:**

Pastikan fail `.env` wujud dalam direktori akar dengan kelayakan Azure (dibuat semasa Modul 01):

**Bash:**
```bash
cat ../.env  # Harus menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Sepatutnya menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Mulakan aplikasi:**

> **Nota:** Jika anda sudah memulakan semua aplikasi menggunakan `./start-all.sh` dari Modul 01, modul ini sudah berjalan pada port 8081. Anda boleh langkau arahan mula di bawah dan pergi terus ke http://localhost:8081.

**Pilihan 1: Menggunakan Spring Boot Dashboard (Disyorkan untuk pengguna VS Code)**

Bekas pembangunan termasuk sambungan Spring Boot Dashboard, yang menyediakan antara muka visual untuk mengurus semua aplikasi Spring Boot. Anda boleh menemuinya di Bar Aktiviti di sebelah kiri VS Code (cari ikon Spring Boot).

Daripada Spring Boot Dashboard, anda boleh:
- Lihat semua aplikasi Spring Boot yang tersedia dalam ruang kerja
- Mula/hentikan aplikasi dengan satu klik
- Lihat log aplikasi masa nyata
- Pantau status aplikasi

Klik butang main di sebelah "rag" untuk memulakan modul ini, atau mulakan semua modul sekaligus.

<img src="../../../translated_images/ms/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Tangkapan skrin ini menunjukkan Spring Boot Dashboard dalam VS Code, di mana anda boleh mula, hentikan, dan pantau aplikasi secara visual.*

**Pilihan 2: Menggunakan skrip shell**

Mula semua aplikasi web (modul 01-04):

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

Kedua skrip secara automatik memuatkan pembolehubah persekitaran dari fail `.env` akar dan akan bina JAR jika ia tiada.

> **Nota:** Jika anda lebih suka membina semua modul secara manual sebelum memulakan:
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

## Menggunakan Aplikasi

Aplikasi menyediakan antara muka web untuk muat naik dokumen dan bertanya soalan.

<a href="images/rag-homepage.png"><img src="../../../translated_images/ms/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Tangkapan skrin ini menunjukkan antara muka aplikasi RAG di mana anda memuat naik dokumen dan bertanya soalan.*

### Muat Naik Dokumen

Mulakan dengan memuat naik dokumen - fail TXT adalah paling sesuai untuk ujian. Satu `sample-document.txt` disediakan dalam direktori ini yang mengandungi maklumat tentang ciri LangChain4j, pelaksanaan RAG, dan amalan terbaik - sesuai untuk menguji sistem.

Sistem memproses dokumen anda, memecahkannya menjadi kepingan, dan mencipta penanaman untuk setiap kepingan. Ini berlaku secara automatik apabila anda muat naik.

### Tanyakan Soalan

Kini tanya soalan khusus mengenai kandungan dokumen. Cuba fakta yang jelas dinyatakan dalam dokumen. Sistem mencari kepingan relevan, memasukkan ke dalam prompt, dan menjana jawapan.

### Semak Rujukan Sumber

Perhatikan setiap jawapan termasuk rujukan sumber dengan skor keserupaan. Skor ini (0 hingga 1) menunjukkan betapa relevannya setiap kepingan dengan soalan anda. Skor yang lebih tinggi bermakna padanan lebih baik. Ini membolehkan anda mengesahkan jawapan dengan bahan sumber.

<a href="images/rag-query-results.png"><img src="../../../translated_images/ms/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Tangkapan skrin ini menunjukkan hasil pertanyaan dengan jawapan dijana, rujukan sumber, dan skor relevan untuk setiap kepingan yang diperoleh.*

### Eksperimen dengan Soalan

Cuba pelbagai jenis soalan:
- Fakta khusus: "Apakah topik utama?"
- Perbandingan: "Apakah perbezaan antara X dan Y?"
- Rumusan: "Ringkaskan perkara utama tentang Z"

Perhatikan bagaimana skor relevan berubah berdasarkan keserasian soalan dengan kandungan dokumen.

## Konsep Utama

### Strategi Pemecahan Kepingan

Dokumen dipecahkan menjadi kepingan 300 token dengan 30 token bertindih. Seimbangan ini memastikan setiap kepingan mempunyai konteks mencukupi untuk bermakna sambil kekal cukup kecil agar beberapa kepingan boleh dimasukkan dalam satu prompt.

### Skor Keserupaan

Setiap kepingan yang diperoleh datang dengan skor keserupaan antara 0 dan 1 yang menunjukkan sejauh mana ia padan dengan soalan pengguna. Rajah di bawah menggambarkan julat skor dan bagaimana sistem menggunakannya untuk menapis hasil:

<img src="../../../translated_images/ms/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Rajah ini menunjukkan julat skor dari 0 ke 1, dengan ambang minimum 0.5 yang menapis kepingan yang tidak relevan.*

Skor berkisar antara 0 ke 1:
- 0.7-1.0: Sangat relevan, padanan tepat
- 0.5-0.7: Relevan, konteks baik
- Di bawah 0.5: Ditapis, terlalu berbeza

Sistem hanya mengambil kepingan di atas ambang minimum untuk memastikan kualiti.

Penanaman berfungsi baik apabila makna berkelompok dengan jelas, tetapi ada kelemahan. Rajah di bawah menunjukkan mod kegagalan biasa — kepingan yang terlalu besar menghasilkan vektor kabur, kepingan terlalu kecil kekurangan konteks, istilah samar menunjukkan beberapa kelompok, dan carian padanan tepat (ID, nombor bahagian) sama sekali tidak berfungsi dengan penanaman:

<img src="../../../translated_images/ms/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Rajah ini menunjukkan mod kegagalan penanaman biasa: kepingan terlalu besar, kepingan terlalu kecil, istilah samar yang menunjuk ke beberapa kelompok, dan carian padanan tepat seperti ID.*

### Penyimpanan Dalam Memori

Modul ini menggunakan penyimpanan dalam memori untuk kesederhanaan. Bila anda mulakan semula aplikasi, dokumen yang dimuat naik akan hilang. Sistem pengeluaran menggunakan pangkalan data vektor kekal seperti Qdrant atau Azure AI Search.

### Pengurusan Tetingkap Konteks

Setiap model mempunyai tetingkap konteks maksimum. Anda tidak boleh memasukkan setiap kepingan dari dokumen besar. Sistem mengambil N kepingan paling relevan teratas (default 5) untuk kekal dalam had sambil menyediakan cukup konteks bagi jawapan tepat.

## Bila RAG Penting

RAG tidak selalu pendekatan yang tepat. Panduan keputusan di bawah membantu anda menentukan bila RAG menambah nilai berbanding bila pendekatan lebih mudah — seperti memasukkan kandungan terus dalam prompt atau bergantung kepada pengetahuan terbenam model — sudah mencukupi:

<img src="../../../translated_images/ms/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Rajah ini menunjukkan panduan keputusan untuk bila RAG menambah nilai berbanding bila pendekatan lebih mudah sudah mencukupi.*

**Gunakan RAG apabila:**
- Menjawab soalan tentang dokumen proprietari
- Maklumat berubah kerap (dasar, harga, spesifikasi)
- Ketepatan memerlukan atribusi sumber
- Kandungan terlalu besar untuk muat dalam satu prompt
- Anda perlukan jawapan boleh disahkan dan berasaskan fakta

**Jangan gunakan RAG apabila:**
- Soalan memerlukan pengetahuan umum yang sudah ada dalam model
- Data masa nyata diperlukan (RAG berfungsi pada dokumen yang dimuat naik)
- Kandungan cukup kecil untuk dimasukkan terus dalam prompt

## Langkah Seterusnya

**Modul Seterusnya:** [04-tools - Ejen AI dengan Alat](../04-tools/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 02 - Kejuruteraan Prompt](../02-prompt-engineering/README.md) | [Kembali ke Utama](../README.md) | [Seterusnya: Modul 04 - Alat →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk mencapai ketepatan, sila ambil maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang sahih. Untuk maklumat penting, adalah disyorkan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->