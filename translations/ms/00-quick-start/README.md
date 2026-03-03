# Module 00: Mula Cepat

## Table of Contents

- [Pengenalan](../../../00-quick-start)
- [Apakah LangChain4j?](../../../00-quick-start)
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
- [Apa yang Ditunjukkan oleh Setiap Contoh](../../../00-quick-start)
- [Langkah Seterusnya](../../../00-quick-start)
- [Penyelesaian Masalah](../../../00-quick-start)

## Pengenalan

Mula cepat ini bertujuan untuk membantu anda memulakan LangChain4j dengan cepat. Ia merangkumi asas membina aplikasi AI menggunakan LangChain4j dan Model GitHub. Dalam modul berikutnya, anda akan beralih ke Azure OpenAI dan GPT-5.2 serta mendalami setiap konsep.

## Apakah LangChain4j?

LangChain4j adalah perpustakaan Java yang memudahkan pembangunan aplikasi yang dikuasakan oleh AI. Daripada mengurus klien HTTP dan penguraian JSON, anda bekerja dengan API Java yang kemas.

"Rantai" dalam LangChain merujuk kepada penggabungan beberapa komponen — anda mungkin mengaitkan satu prompt dengan model, dengan parser, atau mengaitkan beberapa panggilan AI di mana satu output menjadi input seterusnya. Mula cepat ini menumpukan kepada asas sebelum meneroka rantai yang lebih kompleks.

<img src="../../../translated_images/ms/langchain-concept.ad1fe6cf063515e1.webp" alt="Konsep Pengaitan LangChain4j" width="800"/>

*Mengaitkan komponen dalam LangChain4j - blok binaan menyambung untuk mencipta aliran kerja AI yang kuat*

Kami akan menggunakan tiga komponen teras:

**ChatModel** - Antara muka untuk interaksi model AI. Panggil `model.chat("prompt")` dan dapatkan rentetan respons. Kami menggunakan `OpenAiOfficialChatModel` yang berfungsi dengan titik akhir yang serasi OpenAI seperti Model GitHub.

**AiServices** - Mewujudkan antara muka perkhidmatan AI yang selamat jenis. Takrifkan kaedah, hias dengan `@Tool`, dan LangChain4j mengaturkan orchestration. AI secara automatik memanggil kaedah Java anda apabila diperlukan.

**MessageWindowChatMemory** - Mengekalkan sejarah perbualan. Tanpa ini, setiap permintaan berdiri sendiri. Dengan ini, AI mengingati mesej sebelumnya dan mengekalkan konteks merentasi beberapa pusingan.

<img src="../../../translated_images/ms/architecture.eedc993a1c576839.webp" alt="Seni Bina LangChain4j" width="800"/>

*Seni bina LangChain4j - komponen teras bekerjasama untuk menggerakkan aplikasi AI anda*

## Kebergantungan LangChain4j

Mula cepat ini menggunakan tiga kebergantungan Maven dalam [`pom.xml`](../../../00-quick-start/pom.xml):

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

Modul `langchain4j-open-ai-official` menyediakan kelas `OpenAiOfficialChatModel` yang menyambung ke API yang serasi OpenAI. Model GitHub menggunakan format API yang sama, jadi tiada penyesuai khas diperlukan — cuma tetapkan URL asas kepada `https://models.github.ai/inference`.

Modul `langchain4j-easy-rag` menyediakan pemecahan dokumen automatik, penyematan, dan pengambilan supaya anda boleh membina aplikasi RAG tanpa konfigurasi manual setiap langkah.

## Prasyarat

**Menggunakan Dev Container?** Java dan Maven sudah dipasang. Anda hanya memerlukan Token Akses Peribadi GitHub.

**Pembangunan Tempatan:**
- Java 21+, Maven 3.9+
- Token Akses Peribadi GitHub (arahan di bawah)

> **Nota:** Modul ini menggunakan `gpt-4.1-nano` dari Model GitHub. Jangan ubah nama model dalam kod — ia dikonfigurasi untuk berfungsi dengan model yang tersedia di GitHub.

## Persediaan

### 1. Dapatkan Token GitHub Anda

1. Pergi ke [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Klik "Generate new token"
3. Tetapkan nama yang deskriptif (contoh, "LangChain4j Demo")
4. Tetapkan tarikh luput (7 hari disyorkan)
5. Di bawah "Account permissions", cari "Models" dan tetapkan kepada "Read-only"
6. Klik "Generate token"
7. Salin dan simpan token anda — anda tidak akan melihatnya lagi

### 2. Tetapkan Token Anda

**Pilihan 1: Menggunakan VS Code (Disyorkan)**

Jika anda menggunakan VS Code, tambahkan token anda ke fail `.env` di akar projek:

Jika fail `.env` tidak wujud, salin `.env.example` ke `.env` atau cipta fail `.env` baru di akar projek.

**Contoh fail `.env`:**
```bash
# Dalam /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Kemudian anda hanya perlu klik kanan pada mana-mana fail demo (contoh, `BasicChatDemo.java`) di Explorer dan pilih **"Run Java"** atau gunakan konfigurasi pelancaran dari panel Run and Debug.

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

**Menggunakan VS Code:** Klik kanan sahaja pada mana-mana fail demo di Explorer dan pilih **"Run Java"**, atau gunakan konfigurasi pelancaran dari panel Run and Debug (pastikan anda telah menambah token ke fail `.env` terlebih dahulu).

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

Menunjukkan zero-shot, few-shot, chain-of-thought, dan role-based prompting.

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

Tanya soalan tentang dokumen anda menggunakan Easy RAG dengan penyematan dan pengambilan automatik.

### 5. AI Bertanggungjawab

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Lihat bagaimana penapis keselamatan AI menyekat kandungan berbahaya.

## Apa yang Ditunjukkan oleh Setiap Contoh

**Sembang Asas** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Mula di sini untuk melihat LangChain4j pada tahap paling asas. Anda akan mencipta `OpenAiOfficialChatModel`, hantar prompt dengan `.chat()`, dan dapatkan respons. Ini menunjukkan asas: cara menginisialisasi model dengan titik akhir dan kunci API tersuai. Setelah anda faham corak ini, segala yang lain dibina di atasnya.

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
> - "Bagaimana saya tukar dari Model GitHub ke Azure OpenAI dalam kod ini?"
> - "Apakah parameter lain yang boleh saya konfigurasi dalam OpenAiOfficialChatModel.builder()?"
> - "Bagaimana saya tambah respons penstriman dan bukannya tunggu respons lengkap?"

**Kejuruteraan Prompt** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Sekarang anda tahu cara bercakap dengan model, mari teroka apa yang anda katakan kepadanya. Demo ini menggunakan tetapan model yang sama tetapi menunjukkan lima corak prompt yang berbeza. Cuba zero-shot untuk arahan terus, few-shot yang belajar dari contoh, chain-of-thought yang mendedahkan langkah rasional, dan role-based yang menetapkan konteks. Anda akan lihat bagaimana model yang sama memberikan hasil berbeza bergantung pada cara anda membingkai permintaan.

Demo juga menunjukkan templat prompt, cara yang kuat untuk cipta prompt boleh guna semula dengan pembolehubah.
Contoh di bawah menunjukkan prompt menggunakan `PromptTemplate` LangChain4j untuk mengisi pembolehubah. AI akan menjawab berdasarkan destinasi dan aktiviti yang diberikan.

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
> - "Apakah perbezaan antara zero-shot dan few-shot prompting, dan bila saya harus guna masing-masing?"
> - "Bagaimana parameter suhu mempengaruhi respons model?"
> - "Apakah teknik untuk elakkan serangan suntikan prompt dalam pengeluaran?"
> - "Bagaimana saya cipta objek PromptTemplate boleh guna semula untuk corak biasa?"

**Integrasi Alat** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Di sinilah LangChain4j menjadi hebat. Anda akan menggunakan `AiServices` untuk mewujudkan pembantu AI yang boleh memanggil kaedah Java anda. Cuma hias kaedah dengan `@Tool("penerangan")` dan LangChain4j mengurus selebihnya — AI secara automatik memutuskan bila guna setiap alat berdasarkan apa yang pengguna minta. Ini menunjukkan panggilan fungsi, teknik utama membina AI yang boleh bertindak, bukan sekadar menjawab soalan.

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
> - "Bagaimana anotasi @Tool berfungsi dan apa yang LangChain4j buat dengannya di belakang tabir?"
> - "Bolehkah AI memanggil pelbagai alat secara berurutan untuk selesaikan masalah kompleks?"
> - "Apa jadi jika alat membuang pengecualian — bagaimana saya hendak tangani ralat?"
> - "Bagaimana saya integrasi API sebenar bukan contoh kalkulator ini?"

**Soal Jawab Dokumen (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Di sini anda akan lihat RAG (retrieval-augmented generation) menggunakan pendekatan "Easy RAG" LangChain4j. Dokumen dimuat, secara automatik dipecah dan disemat ke dalam stor memori, kemudian pengambil kandungan membekalkan pecahan berkaitan kepada AI semasa waktu soal. AI menjawab berdasarkan dokumen anda, bukan pengetahuan umum.

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
> - "Apakah perbezaan antara pendekatan mudah ini dan saluran RAG khusus?"
> - "Bagaimana saya skala ini untuk urus pelbagai dokumen atau pangkalan pengetahuan lebih besar?"

**AI Bertanggungjawab** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Bina keselamatan AI dengan pertahanan berlapis. Demo ini tunjuk dua lapisan perlindungan bekerja bersama:

**Bahagian 1: LangChain4j Input Guardrails** - Sekat prompt bahaya sebelum sampai ke LLM. Cipta guardrail tersuai yang periksa kata kunci atau corak dilarang. Ini dijalankan dalam kod anda, jadi pantas dan percuma.

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

**Bahagian 2: Penapis Keselamatan Penyedia** - Model GitHub ada penapis terbina yang tangkap apa guardrail mungkin terlepas. Anda akan lihat sekatan keras (ralat HTTP 400) untuk pelanggaran teruk dan penolakan lembut di mana AI menolak dengan sopan.

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) dan tanya:
> - "Apakah InputGuardrail dan bagaimana saya cipta sendiri?"
> - "Apakah perbezaan antara sekatan keras dan penolakan lembut?"
> - "Kenapa gunakan kedua-dua guardrail dan penapis penyedia bersama?"

## Langkah Seterusnya

**Modul Seterusnya:** [01-pengenalan - Mula dengan LangChain4j](../01-introduction/README.md)

---

**Navigasi:** [← Kembali ke Utama](../README.md) | [Seterusnya: Modul 01 - Pengenalan →](../01-introduction/README.md)

---

## Penyelesaian Masalah

### Kompilasi Maven Kali Pertama

**Isu**: `mvn clean compile` atau `mvn package` pertama kali mengambil masa lama (10-15 minit)

**Sebab**: Maven perlu muat turun semua kebergantungan projek (Spring Boot, perpustakaan LangChain4j, SDK Azure, dan lain-lain) pada binaan pertama.

**Penyelesaian**: Ini adalah tingkah laku normal. Binaan seterusnya akan lebih pantas kerana kebergantungan sudah disimpan secara tempatan. Masa muat turun bergantung pada kelajuan rangkaian anda.

### Syntax Arahan Maven PowerShell

**Isu**: Arahan Maven gagal dengan ralat `Unknown lifecycle phase ".mainClass=..."`
**Punca**: PowerShell mentafsir `=` sebagai operator penetapan pembolehubah, yang memecahkan sintaks harta Maven

**Penyelesaian**: Gunakan operator hentikan-penafsiran `--%` sebelum arahan Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operator `--%` memberitahu PowerShell untuk menyerahkan semua argumen yang tinggal secara literal kepada Maven tanpa tafsiran.

### Paparan Emoji Windows PowerShell

**Isu**: Respons AI memaparkan aksara sampah (contohnya, `????` atau `â??`) dan bukannya emoji dalam PowerShell

**Punca**: Pengekodan lalai PowerShell tidak menyokong emoji UTF-8

**Penyelesaian**: Jalankan arahan ini sebelum melaksanakan aplikasi Java:
```cmd
chcp 65001
```

Ini memaksa pengekodan UTF-8 dalam terminal. Sebagai alternatif, gunakan Windows Terminal yang mempunyai sokongan Unicode yang lebih baik.

### Menyahpepijat Panggilan API

**Isu**: Ralat pengesahan, had kadar, atau respons tidak dijangka daripada model AI

**Penyelesaian**: Contoh ini termasuk `.logRequests(true)` dan `.logResponses(true)` untuk memaparkan panggilan API dalam konsol. Ini membantu menyelesaikan ralat pengesahan, had kadar, atau respons tidak dijangka. Buang bendera ini dalam pengeluaran untuk mengurangkan bunyi log.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang sahih. Untuk maklumat yang kritikal, penterjemahan profesional oleh manusia adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->