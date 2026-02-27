# Modul 05: Protokol Konteks Model (MCP)

## Isi Kandungan

- [Apa yang Anda Akan Pelajari](../../../05-mcp)
- [Apa itu MCP?](../../../05-mcp)
- [Bagaimana MCP Berfungsi](../../../05-mcp)
- [Modul Agentik](../../../05-mcp)
- [Menjalankan Contoh-Contoh](../../../05-mcp)
  - [Keperluan Awal](../../../05-mcp)
- [Mula Pantas](../../../05-mcp)
  - [Operasi Fail (Stdio)](../../../05-mcp)
  - [Ejen Penyelia](../../../05-mcp)
    - [Menjalankan Demo](../../../05-mcp)
    - [Bagaimana Penyelia Berfungsi](../../../05-mcp)
    - [Strategi Respons](../../../05-mcp)
    - [Memahami Output](../../../05-mcp)
    - [Penjelasan Ciri-ciri Modul Agentik](../../../05-mcp)
- [Konsep Utama](../../../05-mcp)
- [Tahniah!](../../../05-mcp)
  - [Apa Seterusnya?](../../../05-mcp)

## Apa yang Anda Akan Pelajari

Anda telah membina AI perbualan, menguasai arahan (prompts), mengikat respons dalam dokumen, dan mencipta ejen dengan alat. Tetapi semua alat itu dibina khas untuk aplikasi spesifik anda. Bagaimana jika anda boleh memberikan AI anda akses kepada ekosistem alat piawai yang sesiapa sahaja boleh cipta dan kongsikan? Dalam modul ini, anda akan belajar bagaimana melakukan itu dengan Protokol Konteks Model (MCP) dan modul agentik LangChain4j. Kami mula-mula mempamerkan pembaca fail MCP yang mudah dan kemudian tunjukkan bagaimana ia mudah diintegrasikan ke dalam aliran kerja agentik canggih menggunakan corak Ejen Penyelia.

## Apa itu MCP?

Protokol Konteks Model (MCP) menyediakan tepat itu — cara piawai bagi aplikasi AI untuk menemui dan menggunakan alat luaran. Daripada menulis integrasi khusus untuk setiap sumber data atau perkhidmatan, anda sambungkan kepada pelayan MCP yang mendedahkan keupayaan mereka dalam format yang konsisten. Ejen AI anda kemudian boleh menemui dan menggunakan alat ini secara automatik.

<img src="../../../translated_images/ms/mcp-comparison.9129a881ecf10ff5.webp" alt="Perbandingan MCP" width="800"/>

*Sebelum MCP: Integrasi titik-ke-titik yang kompleks. Selepas MCP: Satu protokol, kemungkinan tanpa had.*

MCP menyelesaikan masalah asas dalam pembangunan AI: setiap integrasi adalah khusus. Mahu akses GitHub? Kod khusus. Mahu baca fail? Kod khusus. Mahu tanya pangkalan data? Kod khusus. Dan tiada satu pun integrasi ini berfungsi dengan aplikasi AI lain.

MCP menstandardkan ini. Pelayan MCP mendedahkan alat dengan keterangan dan skema yang jelas. Mana-mana klien MCP boleh sambung, menemui alat tersedia, dan menggunakannya. Bina sekali, guna di mana-mana.

<img src="../../../translated_images/ms/mcp-architecture.b3156d787a4ceac9.webp" alt="Senibina MCP" width="800"/>

*Senibina Protokol Konteks Model – penemuan dan pelaksanaan alat yang distandardkan*

## Bagaimana MCP Berfungsi

<img src="../../../translated_images/ms/mcp-protocol-detail.01204e056f45308b.webp" alt="Perincian Protokol MCP" width="800"/>

*Bagaimana MCP berfungsi di belakang tabir — klien menemui alat, bertukar mesej JSON-RPC, dan melaksanakan operasi melalui lapisan pengangkutan.*

**Senibina Pelayan-Klien**

MCP menggunakan model klien-pelayan. Pelayan menyediakan alat — membaca fail, menyoal pangkalan data, memanggil API. Klien (aplikasi AI anda) menyambung kepada pelayan dan menggunakan alat mereka.

Untuk menggunakan MCP dengan LangChain4j, tambah pergantungan Maven ini:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Penemuan Alat**

Apabila klien anda menyambung kepada pelayan MCP, ia bertanya "Apakah alat yang anda ada?" Pelayan membalas dengan senarai alat yang tersedia, setiap satu dengan keterangan dan skema parameter. Ejen AI anda kemudian boleh memutuskan alat mana yang hendak digunakan berdasarkan permintaan pengguna.

<img src="../../../translated_images/ms/tool-discovery.07760a8a301a7832.webp" alt="Penemuan Alat MCP" width="800"/>

*AI menemui alat yang tersedia pada permulaan — kini ia tahu keupayaan apa yang ada dan boleh memutuskan yang mana hendak digunakan.*

**Mekanisme Pengangkutan**

MCP menyokong mekanisme pengangkutan yang berbeza. Modul ini mempamerkan pengangkutan Stdio untuk proses tempatan:

<img src="../../../translated_images/ms/transport-mechanisms.2791ba7ee93cf020.webp" alt="Mekanisme Pengangkutan" width="800"/>

*Mekanisme pengangkutan MCP: HTTP untuk pelayan jauh, Stdio untuk proses tempatan*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Untuk proses tempatan. Aplikasi anda menjana pelayan sebagai subproses dan berkomunikasi melalui input/output standard. Berguna untuk akses sistem fail atau alat baris arahan.

```java
McpTransport stdioTransport = new StdioMcpTransport.Builder()
    .command(List.of(
        npmCmd, "exec",
        "@modelcontextprotocol/server-filesystem@2025.12.18",
        resourcesDir
    ))
    .logEvents(false)
    .build();
```

<img src="../../../translated_images/ms/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Aliran Pengangkutan Stdio" width="800"/>

*Pengangkutan Stdio dalam tindakan — aplikasi anda menjana pelayan MCP sebagai proses anak dan berkomunikasi melalui paip stdin/stdout.*

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) dan tanya:
> - "Bagaimana pengangkutan Stdio berfungsi dan bila saya patut guna ia berbanding HTTP?"
> - "Bagaimana LangChain4j menguruskan kitar hidup proses pelayan MCP yang dijana?"
> - "Apakah implikasi keselamatan memberikan AI akses ke sistem fail?"

## Modul Agentik

Walaupun MCP menyediakan alat piawai, modul **agentik** LangChain4j menyediakan cara deklaratif untuk membina ejen yang mengatur alat-alat tersebut. Anotasi `@Agent` dan `AgenticServices` membolehkan anda mentakrifkan kelakuan ejen melalui antara muka berbanding kod imperatif.

Dalam modul ini, anda akan meneroka corak **Ejen Penyelia** — pendekatan agentik AI canggih di mana ejen "penyelia" membuat keputusan dinamik ejen sub mana yang akan dipanggil berdasarkan permintaan pengguna. Kami gabungkan kedua konsep dengan memberikan salah satu sub-ejen kami keupayaan akses fail dipacu MCP.

Untuk menggunakan modul agentik, tambah pergantungan Maven ini:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **⚠️ Eksperimen:** Modul `langchain4j-agentic` adalah **eksperimen** dan tertakluk kepada perubahan. Cara stabil untuk membina pembantu AI kekal `langchain4j-core` dengan alat khusus (Modul 04).

## Menjalankan Contoh-Contoh

### Keperluan Awal

- Java 21+, Maven 3.9+
- Node.js 16+ dan npm (untuk pelayan MCP)
- Pembolehubah persekitaran dikonfigurasi dalam fail `.env` (dari direktori utama):
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (sama seperti Modul 01-04)

> **Nota:** Jika anda belum tetapkan pembolehubah persekitaran anda, lihat [Modul 00 - Mula Pantas](../00-quick-start/README.md) untuk arahan, atau salin `.env.example` ke `.env` di direktori utama dan isikan nilai anda.

## Mula Pantas

**Menggunakan VS Code:** Klik kanan pada mana-mana fail demo dalam Penjelajah dan pilih **"Run Java"**, atau guna konfigurasi pelancaran dari panel Run and Debug (pastikan anda telah tambah token anda ke fail `.env` dahulu).

**Menggunakan Maven:** Alternatifnya, anda boleh jalankan dari baris perintah dengan contoh di bawah.

### Operasi Fail (Stdio)

Ini menunjukkan alat berasaskan subproses tempatan.

**✅ Tiada keperluan awal diperlukan** - pelayan MCP dijana secara automatik.

**Menggunakan Skrip Mula (Disyorkan):**

Skrip mula secara automatik memuat pembolehubah persekitaran dari fail `.env` utama:

**Bash:**
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-stdio.ps1
```

**Menggunakan VS Code:** Klik kanan pada `StdioTransportDemo.java` dan pilih **"Run Java"** (pastikan fail `.env` anda dikonfigurasi).

Aplikasi menjana pelayan MCP sistem fail secara automatik dan membaca fail tempatan. Perhatikan bagaimana pengurusan subproses dikendalikan untuk anda.

**Output yang dijangka:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Ejen Penyelia

Corak **Ejen Penyelia** adalah bentuk AI agentik yang **fleksibel**. Penyelia menggunakan LLM untuk secara automatik memutuskan ejen mana yang perlu dipanggil berdasarkan permintaan pengguna. Dalam contoh seterusnya, kami gabungkan akses fail dipacu MCP dengan ejen LLM untuk mencipta aliran kerja baca fail → laporan yang diawasi.

Dalam demo, `FileAgent` membaca fail menggunakan alat sistem fail MCP, dan `ReportAgent` menghasilkan laporan berstruktur dengan ringkasan eksekutif (1 ayat), 3 perkara utama, dan cadangan. Penyelia mengatur aliran ini secara automatik:

<img src="../../../translated_images/ms/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Corak Ejen Penyelia" width="800"/>

*Penyelia menggunakan LLMnya untuk memutuskan ejen mana yang hendak dipanggil dan dalam susunan apa — tiada penghalaan keras kod diperlukan.*

Ini adalah aliran kerja konkrit untuk saluran fail-ke-laporan kami:

<img src="../../../translated_images/ms/file-report-workflow.649bb7a896800de9.webp" alt="Aliran Kerja Fail ke Laporan" width="800"/>

*FileAgent membaca fail melalui alat MCP, kemudian ReportAgent menukar kandungan mentah itu ke dalam laporan berstruktur.*

Setiap ejen menyimpan outputnya dalam **Kawasan Agentik** (memori dikongsi), membolehkan ejen hiliran mengakses keputusan terdahulu. Ini menunjukkan bagaimana alat MCP disepadukan dengan lancar ke dalam aliran kerja agentik — Penyelia tidak perlu tahu *bagaimana* fail dibaca, hanya bahawa `FileAgent` boleh lakukannya.

#### Menjalankan Demo

Skrip mula secara automatik memuat pembolehubah persekitaran dari fail `.env` utama:

**Bash:**
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-supervisor.ps1
```

**Menggunakan VS Code:** Klik kanan pada `SupervisorAgentDemo.java` dan pilih **"Run Java"** (pastikan fail `.env` anda dikonfigurasi).

#### Bagaimana Penyelia Berfungsi

```java
// Langkah 1: FileAgent membaca fail menggunakan alat MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Mempunyai alat MCP untuk operasi fail
        .build();

// Langkah 2: ReportAgent menghasilkan laporan berstruktur
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor mengatur aliran kerja fail → laporan
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Pulangkan laporan akhir
        .build();

// Supervisor menentukan ejen mana yang akan dipanggil berdasarkan permintaan
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Strategi Respons

Apabila anda konfigurasikan `SupervisorAgent`, anda tentukan bagaimana ia hendak merumuskan jawapan terakhir kepada pengguna selepas sub-ejen menyelesaikan tugasan mereka.

<img src="../../../translated_images/ms/response-strategies.3d0cea19d096bdf9.webp" alt="Strategi Respons" width="800"/>

*Tiga strategi bagaimana Penyelia merumuskan respons akhirnya — pilih berdasarkan sama ada anda mahu output ejen terakhir, ringkasan yang disintesis, atau pilihan dengan skor terbaik.*

Strategi yang tersedia adalah:

| Strategi | Penerangan |
|----------|------------|
| **LAST** | Penyelia mengembalikan output dari sub-ejen atau alat terakhir yang dipanggil. Ini berguna apabila ejen akhir dalam aliran kerja direka khusus untuk menghasilkan jawapan lengkap akhir (contoh: "Ejen Ringkasan" dalam saluran penyelidikan). |
| **SUMMARY** | Penyelia menggunakan Model Bahasa Dalaman (LLM) untuk mensintesis ringkasan keseluruhan interaksi dan semua output sub-ejen, kemudian mengembalikan ringkasan itu sebagai respons akhir. Ini memberikan jawapan agregat yang bersih kepada pengguna. |
| **SCORED** | Sistem menggunakan LLM dalaman untuk memberi skor kepada kedua-dua respons LAST dan ringkasan (SUMMARY) interaksi berdasarkan permintaan asal pengguna, mengembalikan output yang mendapat skor lebih tinggi. |

Lihat [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) untuk pelaksanaan penuh.

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) dan tanya:
> - "Bagaimana Penyelia memutuskan ejen mana yang hendak dipanggil?"
> - "Apa perbezaan antara corak Penyelia dan kerja berurutan?"
> - "Bagaimana saya boleh sesuaikan tingkah laku perancangan Penyelia?"

#### Memahami Output

Apabila anda jalankan demo, anda akan melihat penerangan berstruktur bagaimana Penyelia mengatur pelbagai ejen. Ini makna setiap bahagian:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Tajuk utama** memperkenalkan konsep aliran kerja: saluran fokus dari baca fail ke penjanaan laporan.

```
--- WORKFLOW ---------------------------------------------------------
  ┌─────────────┐      ┌──────────────┐
  │  FileAgent  │ ───▶ │ ReportAgent  │
  │ (MCP tools) │      │  (pure LLM)  │
  └─────────────┘      └──────────────┘
   outputKey:           outputKey:
   'fileContent'        'report'

--- AVAILABLE AGENTS -------------------------------------------------
  [FILE]   FileAgent   - Reads files via MCP → stores in 'fileContent'
  [REPORT] ReportAgent - Generates structured report → stores in 'report'
```

**Diagram Aliran Kerja** menunjukkan aliran data antara ejen. Setiap ejen mempunyai peranan spesifik:
- **FileAgent** membaca fail menggunakan alat MCP dan menyimpan kandungan mentah dalam `fileContent`
- **ReportAgent** menggunakan kandungan itu dan menghasilkan laporan berstruktur dalam `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Permintaan Pengguna** menunjukkan tugasan. Penyelia memproses dan memutuskan untuk memanggil FileAgent → ReportAgent.

```
--- SUPERVISOR ORCHESTRATION -----------------------------------------
  The Supervisor decides which agents to invoke and passes data between them...

  +-- STEP 1: Supervisor chose -> FileAgent (reading file via MCP)
  |
  |   Input: .../file.txt
  |
  |   Result: LangChain4j is an open-source, provider-agnostic Java framework for building LLM...
  +-- [OK] FileAgent (reading file via MCP) completed

  +-- STEP 2: Supervisor chose -> ReportAgent (generating structured report)
  |
  |   Input: LangChain4j is an open-source, provider-agnostic Java framew...
  |
  |   Result: Executive Summary...
  +-- [OK] ReportAgent (generating structured report) completed
```

**Pengurusan Penyelia** menunjukkan aliran 2-langkah dalam tindakan:
1. **FileAgent** membaca fail melalui MCP dan menyimpan kandungan
2. **ReportAgent** menerima kandungan dan menghasilkan laporan berstruktur

Penyelia membuat keputusan ini secara **autonomi** berdasarkan permintaan pengguna.

```
--- FINAL RESPONSE ---------------------------------------------------
Executive Summary
...

Key Points
...

Recommendations
...

--- AGENTIC SCOPE (Data Flow) ----------------------------------------
  Each agent stores its output for downstream agents to consume:
  * fileContent: LangChain4j is an open-source, provider-agnostic Java framework...
  * report: Executive Summary...
```

#### Penjelasan Ciri-ciri Modul Agentik

Contoh ini menunjukkan beberapa ciri lanjutan modul agentik. Mari lihat lebih dekat Kawasan Agentik dan Pendengar Ejen.

**Kawasan Agentik** menunjukkan memori dikongsi tempat ejen menyimpan keputusan mereka menggunakan `@Agent(outputKey="...")`. Ini membolehkan:
- Ejen kemudian mengakses output ejen terdahulu
- Penyelia mensintesis respons akhir
- Anda memeriksa apa yang dihasilkan setiap ejen

<img src="../../../translated_images/ms/agentic-scope.95ef488b6c1d02ef.webp" alt="Kawasan Agentik Memori Dikongsi" width="800"/>

*Kawasan Agentik bertindak sebagai memori dikongsi — FileAgent menulis `fileContent`, ReportAgent membacanya dan menulis `report`, dan kod anda membaca hasil akhir.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Data fail mentah dari FileAgent
String report = scope.readState("report");            // Laporan berstruktur dari ReportAgent
```

**Pendengar Ejen** membolehkan pemantauan dan debug pelaksanaan ejen. Output langkah demi langkah yang anda lihat dalam demo datang dari AgentListener yang tersambung pada setiap pemanggilan ejen:
- **beforeAgentInvocation** - Dipanggil apabila Penyelia memilih agen, membolehkan anda melihat agen mana yang dipilih dan mengapa
- **afterAgentInvocation** - Dipanggil apabila agen selesai, menunjukkan hasilnya
- **inheritedBySubagents** - Apabila benar, pendengar memantau semua agen dalam hierarki

<img src="../../../translated_images/ms/agent-listeners.784bfc403c80ea13.webp" alt="Kitaran Hayat Pendengar Agen" width="800"/>

*Pendengar Agen menyambung ke kitaran hayat pelaksanaan — memantau apabila agen bermula, selesai, atau menghadapi ralat.*

```java
AgentListener monitor = new AgentListener() {
    private int step = 0;
    
    @Override
    public void beforeAgentInvocation(AgentRequest request) {
        step++;
        System.out.println("  +-- STEP " + step + ": " + request.agentName());
    }
    
    @Override
    public void afterAgentInvocation(AgentResponse response) {
        System.out.println("  +-- [OK] " + response.agentName() + " completed");
    }
    
    @Override
    public boolean inheritedBySubagents() {
        return true; // Sebarkan kepada semua sub-ejen
    }
};
```

Selain daripada corak Penyelia, modul `langchain4j-agentic` menyediakan beberapa corak aliran kerja dan ciri yang berkuasa:

<img src="../../../translated_images/ms/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Corak Aliran Kerja Agen" width="800"/>

*Lima corak aliran kerja untuk mengatur agen — dari rangkaian mudah berturutan ke aliran kerja kelulusan manusia-dalam-laluan.*

| Corak | Penerangan | Kes Penggunaan |
|---------|-------------|---------------|
| **Berturutan** | Laksanakan agen mengikut urutan, output mengalir ke seterusnya | Rangkaian: penyelidikan → analisis → laporan |
| **Serentak** | Jalankan agen secara serentak | Tugasan bebas: cuaca + berita + saham |
| **Gelung** | Ulang sehingga syarat dipenuhi | Skor kualiti: halusi sehingga skor ≥ 0.8 |
| **Bersyarat** | Pandu berdasarkan syarat | Klasifikasi → alihkan ke agen pakar |
| **Manusia-dalam-Laluan** | Tambah titik pemeriksaan manusia | Aliran kerja kelulusan, semakan kandungan |

## Konsep Utama

Sekarang anda telah menerokai MCP dan modul agentic dalam tindakan, mari kita ringkaskan bila untuk menggunakan setiap pendekatan.

<img src="../../../translated_images/ms/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="Ekosistem MCP" width="800"/>

*MCP mencipta ekosistem protokol universal — mana-mana pelayan serasi MCP berfungsi dengan mana-mana klien serasi MCP, membenarkan perkongsian alat merentas aplikasi.*

**MCP** sesuai apabila anda mahu memanfaatkan ekosistem alat sedia ada, bina alat yang boleh dikongsi oleh pelbagai aplikasi, integrasi perkhidmatan pihak ketiga dengan protokol standard, atau menukar pelaksanaan alat tanpa menukar kod.

**Modul Agentic** berfungsi terbaik apabila anda mahukan definisi agen deklaratif dengan anotasi `@Agent`, memerlukan pengurusan aliran kerja (berturutan, gelung, serentak), mengutamakan reka bentuk agen berasaskan antara muka berbanding kod imperatif, atau menggabungkan pelbagai agen yang berkongsi output melalui `outputKey`.

**Corak Agen Penyelia** menonjol apabila aliran kerja tidak dapat diramalkan terlebih dahulu dan anda mahu LLM membuat keputusan, apabila anda mempunyai pelbagai agen khusus yang memerlukan pengurusan dinamik, apabila membina sistem perbualan yang menghala ke keupayaan berbeza, atau apabila anda mahukan tingkah laku agen yang paling fleksibel dan adaptif.

<img src="../../../translated_images/ms/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Alat Tersuai vs Alat MCP" width="800"/>

*Bila menggunakan kaedah @Tool tersuai vs alat MCP — alat tersuai untuk logik khusus aplikasi dengan keselamatan jenis penuh, alat MCP untuk integrasi standard yang bekerja merentas aplikasi.*

## Tahniah!

<img src="../../../translated_images/ms/course-completion.48cd201f60ac7570.webp" alt="Penyiapan Kursus" width="800"/>

*Perjalanan pembelajaran anda melalui kelima-lima modul — dari sembang asas ke sistem agentic dikuasakan MCP.*

Anda telah menamatkan kursus LangChain4j untuk Pemula. Anda telah belajar:

- Cara membina AI perbualan dengan memori (Modul 01)
- Corak kejuruteraan arahan untuk tugasan berbeza (Modul 02)
- Membumikan jawapan dalam dokumen anda dengan RAG (Modul 03)
- Mencipta agen AI asas (pembantu) dengan alat tersuai (Modul 04)
- Mengintegrasi alat standard dengan modul LangChain4j MCP dan Agentic (Modul 05)

### Apa Seterusnya?

Selepas menamatkan modul, terokai [Panduan Ujian](../docs/TESTING.md) untuk melihat konsep ujian LangChain4j dalam tindakan.

**Sumber Rasmi:**
- [Dokumentasi LangChain4j](https://docs.langchain4j.dev/) - Panduan komprehensif dan rujukan API
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Kod sumber dan contoh
- [Tutorial LangChain4j](https://docs.langchain4j.dev/tutorials/) - Tutorial langkah demi langkah untuk pelbagai kes penggunaan

Terima kasih kerana menamatkan kursus ini!

---

**Navigasi:** [← Sebelum: Modul 04 - Alat](../04-tools/README.md) | [Kembali ke Utama](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila ambil maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber rujukan yang sah. Untuk maklumat penting, terjemahan oleh penterjemah manusia profesional adalah disyorkan. Kami tidak bertanggungjawab terhadap sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->