# Modul 05: Protokol Konteks Model (MCP)

## Jadual Kandungan

- [Apa Yang Anda Akan Pelajari](../../../05-mcp)
- [Apakah MCP?](../../../05-mcp)
- [Bagaimana MCP Berfungsi](../../../05-mcp)
- [Modul Agenik](../../../05-mcp)
- [Menjalankan Contoh](../../../05-mcp)
  - [Prasyarat](../../../05-mcp)
- [Panduan Pantas](../../../05-mcp)
  - [Operasi Fail (Stdio)](../../../05-mcp)
  - [Ejen Penyelia](../../../05-mcp)
    - [Menjalankan Demo](../../../05-mcp)
    - [Bagaimana Penyelia Berfungsi](../../../05-mcp)
    - [Bagaimana FileAgent Mengesan Alat MCP Semasa Runtime](../../../05-mcp)
    - [Strategi Respons](../../../05-mcp)
    - [Memahami Output](../../../05-mcp)
    - [Penjelasan Ciri Modul Agenik](../../../05-mcp)
- [Konsep Utama](../../../05-mcp)
- [Tahniah!](../../../05-mcp)
  - [Apa Seterusnya?](../../../05-mcp)

## Apa Yang Anda Akan Pelajari

Anda telah membina AI perbualan, menguasai arahan, mengasaskan respons dalam dokumen, dan mencipta ejen dengan alat. Tetapi semua alat itu dibina khas untuk aplikasi anda. Bagaimana jika anda boleh memberi AI anda akses kepada ekosistem alat yang distandardkan yang sesiapa pun boleh cipta dan kongsi? Dalam modul ini, anda akan belajar bagaimana untuk melakukan perkara itu dengan Protokol Konteks Model (MCP) dan modul agenik LangChain4j. Kami mula-mula menunjukkan pembaca fail MCP yang mudah dan kemudian tunjuk bagaimana ia mudah disepadukan dalam aliran kerja agenik canggih menggunakan corak Ejen Penyelia.

## Apakah MCP?

Protokol Konteks Model (MCP) menyediakan tepat itu - satu cara standard untuk aplikasi AI menemui dan menggunakan alat luaran. Daripada menulis integrasi tersuai untuk setiap sumber data atau perkhidmatan, anda menyambung ke pelayan MCP yang mendedahkan keupayaan mereka dalam format yang konsisten. Ejen AI anda kemudian boleh menemui dan menggunakan alat ini secara automatik.

Rajah di bawah menunjukkan perbezaannya — tanpa MCP, setiap integrasi memerlukan penyambungan titik-ke-titik tersuai; dengan MCP, satu protokol menghubungkan aplikasi anda kepada mana-mana alat:

<img src="../../../translated_images/ms/mcp-comparison.9129a881ecf10ff5.webp" alt="Perbandingan MCP" width="800"/>

*Sebelum MCP: Integrasi titik-ke-titik yang kompleks. Selepas MCP: Satu protokol, kemungkinan tanpa had.*

MCP menyelesaikan masalah asas dalam pembangunan AI: setiap integrasi adalah tersuai. Mahu akses GitHub? Kod tersuai. Mahu baca fail? Kod tersuai. Mahu soal pangkalan data? Kod tersuai. Dan tiada satu pun integrasi ini berfungsi dengan aplikasi AI lain.

MCP menyeragamkan ini. Pelayan MCP mendedahkan alat dengan penerangan jelas dan skema. Mana-mana klien MCP boleh menyambung, menemui alat tersedia, dan menggunakannya. Bina sekali, guna di mana-mana.

Rajah di bawah menerangkan seni bina ini — satu klien MCP (aplikasi AI anda) menyambung ke pelbagai pelayan MCP, setiap satu mendedahkan set alat mereka melalui protokol standard:

<img src="../../../translated_images/ms/mcp-architecture.b3156d787a4ceac9.webp" alt="Seni Bina MCP" width="800"/>

*Seni bina Protokol Konteks Model - penemuan dan pelaksanaan alat yang distandardkan*

## Bagaimana MCP Berfungsi

Di bawah permukaan, MCP menggunakan seni bina berlapis. Aplikasi Java anda (klien MCP) menemui alat tersedia, menghantar permintaan JSON-RPC melalui lapisan pengangkutan (Stdio atau HTTP), dan pelayan MCP melaksanakan operasi serta mengembalikan keputusan. Rajah berikut memecahkan setiap lapisan protokol ini:

<img src="../../../translated_images/ms/mcp-protocol-detail.01204e056f45308b.webp" alt="Perincian Protokol MCP" width="800"/>

*Bagaimana MCP berfungsi di bawah permukaan — klien menemui alat, bertukar mesej JSON-RPC, dan melaksanakan operasi melalui lapisan pengangkutan.*

**Seni Bina Server-Klien**

MCP menggunakan model klien-pelayan. Pelayan menyediakan alat - membaca fail, soal pangkalan data, panggil API. Klien (aplikasi AI anda) menyambung ke pelayan dan menggunakan alat mereka.

Untuk menggunakan MCP dengan LangChain4j, tambahkan kebergantungan Maven ini:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Penemuan Alat**

Apabila klien anda menyambung ke pelayan MCP, ia bertanya "Apakah alat yang anda ada?" Pelayan membalas dengan senarai alat yang tersedia, setiap satunya dengan penerangan dan skema parameter. Ejen AI anda kemudian boleh memutuskan alat mana yang akan digunakan berdasarkan permintaan pengguna. Rajah di bawah menunjukkan pertemuan ini — klien menghantar permintaan `tools/list` dan pelayan membalas dengan alat yang tersedia serta penerangan dan skema parameter mereka:

<img src="../../../translated_images/ms/tool-discovery.07760a8a301a7832.webp" alt="Penemuan Alat MCP" width="800"/>

*AI menemui alat tersedia semasa permulaan — ia kini tahu keupayaan yang ada dan boleh memutuskan mana yang hendak digunakan.*

**Mekanisme Pengangkutan**

MCP menyokong mekanisme pengangkutan yang berbeza. Dua pilihan adalah Stdio (untuk komunikasi subprocess tempatan) dan HTTP Boleh Aliran (untuk pelayan jauh). Modul ini menunjukkan pengangkutan Stdio:

<img src="../../../translated_images/ms/transport-mechanisms.2791ba7ee93cf020.webp" alt="Mekanisme Pengangkutan" width="800"/>

*Mekanisme pengangkutan MCP: HTTP untuk pelayan jauh, Stdio untuk proses tempatan*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Untuk proses tempatan. Aplikasi anda memulakan pelayan sebagai subprocess dan berkomunikasi melalui input/output standard. Berguna untuk akses sistem fail atau alat baris perintah.

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

Pelayan `@modelcontextprotocol/server-filesystem` mendedahkan alat berikut, semuanya dibatasi pada direktori yang anda tentukan:

| Alat | Penerangan |
|------|-------------|
| `read_file` | Baca kandungan satu fail |
| `read_multiple_files` | Baca banyak fail dalam satu panggilan |
| `write_file` | Cipta atau timpa fail |
| `edit_file` | Buat suntingan cari-dan-ganti yang disasarkan |
| `list_directory` | Senaraikan fail dan direktori pada satu laluan |
| `search_files` | Cari fail yang sepadan pola secara rekursif |
| `get_file_info` | Dapatkan metadata fail (saiz, cap masa, kebenaran) |
| `create_directory` | Cipta direktori (termasuk direktori induk) |
| `move_file` | Pindah atau namakan semula fail atau direktori |

Rajah berikut menunjukkan bagaimana pengangkutan Stdio berfungsi semasa runtime — aplikasi Java anda memulakan pelayan MCP sebagai proses anak dan mereka berkomunikasi melalui paip stdin/stdout, tanpa rangkaian atau HTTP terlibat:

<img src="../../../translated_images/ms/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Aliran Pengangkutan Stdio" width="800"/>

*Pengangkutan Stdio dalam tindakan — aplikasi anda memulakan pelayan MCP sebagai proses anak dan berkomunikasi melalui paip stdin/stdout.*

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) dan tanya:
> - "Bagaimana pengangkutan Stdio berfungsi dan bila saya perlu menggunakan ia berbanding HTTP?"
> - "Bagaimana LangChain4j mengurus kitar hayat proses pelayan MCP yang dilahirkan?"
> - "Apakah implikasi keselamatan memberikan AI akses ke sistem fail?"

## Modul Agenik

Walaupun MCP menyediakan alat yang distandardkan, modul **agenik** LangChain4j menyediakan cara deklaratif untuk membina ejen yang mengatur alat-alat itu. Anotasi `@Agent` dan `AgenticServices` membolehkan anda mendefinisikan tingkah laku ejen melalui antara muka dan bukan kod imperatif.

Dalam modul ini, anda akan meneroka corak **Ejen Penyelia** — pendekatan AI agenik canggih di mana ejen "penyelia" memutuskan secara dinamik ejen sub mana yang dipanggil berdasarkan permintaan pengguna. Kami akan menggabungkan kedua-dua konsep dengan memberi salah satu ejen sub kami kemampuan akses fail yang dikuasakan oleh MCP.

Untuk menggunakan modul agenik, tambahkan kebergantungan Maven ini:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Nota:** Modul `langchain4j-agentic` menggunakan harta versi berasingan (`langchain4j.mcp.version`) kerana ia dikeluarkan pada jadual berbeza dari perpustakaan teras LangChain4j.

> **⚠️ Eksperimen:** Modul `langchain4j-agentic` adalah **eksperimen** dan tertakluk kepada perubahan. Cara stabil untuk membina pembantu AI kekal `langchain4j-core` dengan alat tersuai (Modul 04).

## Menjalankan Contoh

### Prasyarat

- Selesai [Modul 04 - Tools](../04-tools/README.md) (modul ini membina konsep alat tersuai dan membandingkannya dengan alat MCP)
- Fail `.env` di direktori akar dengan kelayakan Azure (dicipta oleh `azd up` dalam Modul 01)
- Java 21+, Maven 3.9+
- Node.js 16+ dan npm (untuk pelayan MCP)

> **Nota:** Jika anda belum menyediakan pemboleh ubah persekitaran anda, lihat [Modul 01 - Pengenalan](../01-introduction/README.md) untuk arahan penyebaran (`azd up` mencipta fail `.env` secara automatik), atau salin `.env.example` ke `.env` di direktori akar dan isi nilai anda.

## Panduan Pantas

**Menggunakan VS Code:** Cukup klik kanan pada mana-mana fail demo dalam Penjelajah dan pilih **"Run Java"**, atau gunakan konfigurasi pelancaran dari panel Run and Debug (pastikan fail `.env` anda dikonfigurasi dengan kelayakan Azure terlebih dahulu).

**Menggunakan Maven:** Sebagai alternatif, anda boleh jalankan dari baris arahan dengan contoh di bawah.

### Operasi Fail (Stdio)

Ini menunjukkan alat berasaskan subprocess tempatan.

**✅ Tiada prasyarat diperlukan** - pelayan MCP dilahirkan secara automatik.

**Menggunakan Skrip Mula (Disyorkan):**

Skrip mula memuatkan pemboleh ubah persekitaran secara automatik dari fail `.env` di akar:

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

Aplikasi melahirkan pelayan MCP sistem fail secara automatik dan membaca fail tempatan. Perhatikan bagaimana pengurusan subprocess diuruskan untuk anda.

**Output yang dijangka:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Ejen Penyelia

Corak **Ejen Penyelia** adalah bentuk **fleksibel** AI agenik. Penyelia menggunakan LLM untuk memutuskan secara autonomi ejen mana yang akan dipanggil berdasarkan permintaan pengguna. Dalam contoh seterusnya, kami menggabungkan akses fail dikuasakan MCP dengan ejen LLM untuk mencipta aliran kerja laporan baca fail → laporan.

Dalam demo, `FileAgent` membaca fail menggunakan alat sistem fail MCP, dan `ReportAgent` menjana laporan berstruktur dengan ringkasan eksekutif (1 ayat), 3 perkara utama, dan cadangan. Penyelia mengatur aliran ini secara automatik:

<img src="../../../translated_images/ms/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Corak Ejen Penyelia" width="800"/>

*Penyelia menggunakan LLMnya untuk memutuskan ejen mana dipanggil dan dalam susunan apa — tiada routing keras diperlukan.*

Berikut ialah aliran kerja konkrit bagi paip fail-ke-laporan kami:

<img src="../../../translated_images/ms/file-report-workflow.649bb7a896800de9.webp" alt="Aliran Kerja Fail ke Laporan" width="800"/>

*FileAgent membaca fail melalui alat MCP, kemudian ReportAgent mengubah kandungan mentah menjadi laporan berstruktur.*

Rajah urutan berikut mengesan penyeliaan penuh oleh Penyelia — dari melahirkan pelayan MCP, melalui pemilihan ejen autonomi Penyelia, ke panggilan alat melalui stdio dan laporan akhir:

<img src="../../../translated_images/ms/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Rajah Urutan Ejen Penyelia" width="800"/>

*Penyelia memanggil secara autonomi FileAgent (yang memanggil pelayan MCP melalui stdio untuk membaca fail), kemudian memanggil ReportAgent untuk menjana laporan berstruktur — setiap ejen menyimpan outputnya dalam Skop Agenik kongsi.*

Setiap ejen menyimpan outputnya dalam **Skop Agenik** (memori kongsi), membolehkan ejen hiliran mengakses keputusan terdahulu. Ini menunjukkan bagaimana alat MCP disepadukan dengan lancar ke dalam aliran kerja agenik — Penyelia tidak perlu tahu *bagaimana* fail dibaca, hanya bahawa `FileAgent` boleh melakukannya.

#### Menjalankan Demo

Skrip mula memuatkan pemboleh ubah persekitaran secara automatik dari fail `.env` di akar:

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

Sebelum membina ejen, anda perlu menyambungkan pengangkutan MCP ke klien dan membungkusnya sebagai `ToolProvider`. Inilah cara alat pelayan MCP menjadi tersedia kepada ejen anda:

```java
// Cipta klien MCP daripada pengangkut
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Bungkus klien sebagai ToolProvider — ini menghubungkan alat MCP ke dalam LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Kini anda boleh suntik `mcpToolProvider` ke dalam mana-mana ejen yang memerlukan alat MCP:

```java
// Langkah 1: FileAgent membaca fail menggunakan alat MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Mempunyai alat MCP untuk operasi fail
        .build();

// Langkah 2: ReportAgent menghasilkan laporan yang berstruktur
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Penyelia mengatur aliran kerja fail → laporan
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Pulangkan laporan akhir
        .build();

// Penyelia memutuskan ejen mana yang akan dipanggil berdasarkan permintaan
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Bagaimana FileAgent Mengesan Alat MCP Semasa Runtime

Anda mungkin tertanya-tanya: **bagaimana `FileAgent` tahu cara menggunakan alat sistem fail npm?** Jawapannya ialah ia tidak tahu — **LLM** yang menentukannya semasa runtime melalui skema alat.

Antara muka `FileAgent` hanyalah satu **definisi arahan**. Ia tidak mempunyai pengetahuan keras tentang `read_file`, `list_directory`, atau mana-mana alat MCP lain. Berikut adalah apa yang berlaku end-to-end:
1. **Server melancarkan:** `StdioMcpTransport` melancarkan pakej npm `@modelcontextprotocol/server-filesystem` sebagai proses anak  
2. **Penemuan alat:** `McpClient` menghantar permintaan JSON-RPC `tools/list` kepada server, yang membalas dengan nama alat, penerangan, dan skema parameter (contoh, `read_file` — *"Baca kandungan penuh fail"* — `{ path: string }`)  
3. **Penyuntikan skema:** `McpToolProvider` membungkus skema-skema yang ditemui ini dan menjadikannya tersedia untuk LangChain4j  
4. **LLM membuat keputusan:** Apabila `FileAgent.readFile(path)` dipanggil, LangChain4j menghantar mesej sistem, mesej pengguna, **dan senarai skema alat** kepada LLM. LLM membaca penerangan alat dan menjana panggilan alat (contoh, `read_file(path="/some/file.txt")`)  
5. **Pelaksanaan:** LangChain4j menyekat panggilan alat, mengarahkannya melalui klien MCP kembali ke proses anak Node.js, mendapat hasilnya, dan memberikannya kembali kepada LLM  

Ini adalah mekanisme [Penemuan Alat](../../../05-mcp) yang sama seperti yang diterangkan di atas, tetapi digunakan secara khusus dalam aliran kerja agen. Anotasi `@SystemMessage` dan `@UserMessage` membimbing tingkah laku LLM, sementara `ToolProvider` yang disuntik memberikan **keupayaan** — LLM menghubungkan kedua-duanya ketika waktu jalan.

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) dan tanyakan:  
> - "Bagaimana agen ini mengetahui alat MCP mana yang hendak dipanggil?"  
> - "Apa yang berlaku jika saya membuang ToolProvider dari penyusun agen?"  
> - "Bagaimana skema alat dihantar kepada LLM?"

#### Strategi Respons

Apabila anda mengkonfigurasi `SupervisorAgent`, anda menentukan bagaimana ia harus merumuskan jawapan akhirnya untuk pengguna selepas sub-agen menyelesaikan tugas mereka. Rajah di bawah menunjukkan tiga strategi yang tersedia — LAST mengembalikan output agen terakhir secara langsung, SUMMARY mensintesis semua output melalui LLM, dan SCORED memilih yang mana mendapat markah lebih tinggi berbanding permintaan asal:

<img src="../../../translated_images/ms/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Tiga strategi bagaimana Supervisor merumuskan respons akhirnya — pilih berdasarkan sama ada anda mahu output agen terakhir, ringkasan yang disintesis, atau pilihan dengan markah terbaik.*

Strategi yang tersedia adalah:

| Strategi | Penerangan |
|----------|------------|
| **LAST** | Penyelia mengembalikan output agen atau alat sub-akhir yang dipanggil. Ini berguna apabila agen terakhir dalam aliran kerja direka khusus untuk menghasilkan jawapan lengkap dan akhir (contoh, "Agen Ringkasan" dalam rangka kerja penyelidikan). |
| **SUMMARY** | Penyelia menggunakan Model Bahasa (LLM) dalaman untuk mensintesis ringkasan keseluruhan interaksi dan semua output sub-agen, kemudian mengembalikan ringkasan itu sebagai respons akhir. Ini menyediakan jawapan yang jelas dan terkumpul kepada pengguna. |
| **SCORED** | Sistem menggunakan LLM dalaman untuk memberi markah kepada respons LAST dan SUMMARY berbanding permintaan asal pengguna, mengembalikan output yang menerima markah lebih tinggi. |

Lihat [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) untuk pelaksanaan lengkap.

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) dan tanyakan:  
> - "Bagaimana Supervisor memutuskan agen mana yang hendak dipanggil?"  
> - "Apakah perbezaan antara corak aliran kerja Supervisor dan Sequential?"  
> - "Bagaimana saya boleh mengubah suai tingkah laku perancangan Supervisor?"

#### Memahami Output

Apabila anda menjalankan demo, anda akan melihat panduan berstruktur bagaimana Supervisor mengatur beberapa agen. Berikut maksud setiap bahagian:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Tajuk utama** memperkenalkan konsep aliran kerja: satu saluran fokus dari pembacaan fail ke penjanaan laporan.

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
  
**Rajah Aliran Kerja** menunjukkan aliran data antara agen. Setiap agen mempunyai peranan khusus:  
- **FileAgent** membaca fail menggunakan alat MCP dan menyimpan kandungan mentah dalam `fileContent`  
- **ReportAgent** menggunakan kandungan itu dan menghasilkan laporan berstruktur dalam `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Permintaan Pengguna** menunjukkan tugas. Supervisor menguraikan ini dan memutuskan untuk memanggil FileAgent → ReportAgent.

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
  
**Penyelarasan Supervisor** menunjukkan aliran 2 langkah dalam tindakan:  
1. **FileAgent** membaca fail melalui MCP dan menyimpan kandungan  
2. **ReportAgent** menerima kandungan dan menjana laporan berstruktur  

Supervisor membuat keputusan ini **secara autonomi** berdasarkan permintaan pengguna.

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
  
#### Penjelasan Ciri Modul Agentic

Contoh ini menunjukkan beberapa ciri maju modul agentic. Mari kita lihat lebih dekat Agentic Scope dan Agent Listeners.

**Agentic Scope** menunjukkan memori kongsi di mana agen menyimpan hasil mereka menggunakan `@Agent(outputKey="...")`. Ini membolehkan:  
- Agen kemudian mengakses output agen terdahulu  
- Supervisor mensintesis respons akhir  
- Anda memeriksa apa yang dihasilkan oleh setiap agen

Rajah di bawah menunjukkan bagaimana Agentic Scope berfungsi sebagai memori kongsi dalam aliran kerja fail-ke-laporan — FileAgent menulis outputnya di bawah kunci `fileContent`, ReportAgent membaca itu dan menulis outputnya sendiri di bawah `report`:

<img src="../../../translated_images/ms/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope bertindak sebagai memori kongsi — FileAgent menulis `fileContent`, ReportAgent membacanya dan menulis `report`, dan kod anda membaca hasil akhir.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Data fail mentah dari FileAgent
String report = scope.readState("report");            // Laporan berstruktur dari ReportAgent
```
  
**Agent Listeners** membolehkan pemantauan dan penyahpepijatan pelaksanaan agen. Output langkah demi langkah yang anda lihat dalam demo datang dari AgentListener yang disambungkan ke setiap panggilan agen:  
- **beforeAgentInvocation** - Dipanggil apabila Supervisor memilih agen, membolehkan anda lihat agen mana yang dipilih dan mengapa  
- **afterAgentInvocation** - Dipanggil apabila agen selesai, menunjukkan hasilnya  
- **inheritedBySubagents** - Apabila benar, pendengar memantau semua agen dalam hierarki

Rajah berikut menunjukkan keseluruhan kitaran hayat Agent Listener, termasuk bagaimana `onError` mengendalikan kegagalan semasa pelaksanaan agen:

<img src="../../../translated_images/ms/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners disambungkan ke kitaran hayat pelaksanaan — memantau bila agen bermula, selesai, atau mengalami ralat.*

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
        return true; // Sebarkan kepada semua agen bawah
    }
};
```
  
Selain corak Supervisor, modul `langchain4j-agentic` menyediakan beberapa corak aliran kerja yang kukuh. Rajah di bawah menunjukkan kesemua lima — dari saluran berturutan ringkas ke aliran kerja kelulusan manusia-dalam-gelanggang:

<img src="../../../translated_images/ms/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Lima corak aliran kerja untuk mengatur agen — dari saluran berturutan ringkas ke aliran kerja kelulusan manusia-dalam-gelanggang.*

| Corak | Penerangan | Kes Penggunaan |
|---------|-------------|----------|
| **Sequential** | Melaksanakan agen mengikut urutan, output mengalir ke berikutnya | Saluran: penyelidikan → analisis → laporan |
| **Parallel** | Menjalankan agen secara serentak | Tugas bebas: cuaca + berita + saham |
| **Loop** | Ulang sehingga syarat dipenuhi | Penilaan mutu: perbaiki sehingga skor ≥ 0.8 |
| **Conditional** | Menghala berdasarkan syarat | Klasifikasi → hala ke agen pakar |
| **Human-in-the-Loop** | Tambah titik semakan manusia | Aliran kerja kelulusan, semakan kandungan |

## Konsep Utama

Sekarang anda telah meneroka MCP dan modul agentic dalam tindakan, mari kita ringkaskan bila hendak gunakan setiap pendekatan.

Salah satu kelebihan terbesar MCP adalah ekosistemnya yang berkembang. Rajah di bawah menunjukkan bagaimana satu protokol universal menghubungkan aplikasi AI anda ke pelbagai server MCP — dari akses sistem fail dan pangkalan data hingga GitHub, emel, menghimpun web, dan banyak lagi:

<img src="../../../translated_images/ms/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP mencipta ekosistem protokol universal — sebarang server kompatibel MCP berfungsi dengan sebarang klien kompatibel MCP, membolehkan perkongsian alat merentas aplikasi.*

**MCP** sangat sesuai apabila anda mahu memanfaatkan ekosistem alat sedia ada, membina alat yang boleh dikongsi oleh pelbagai aplikasi, mengintegrasi perkhidmatan pihak ketiga dengan protokol standard, atau menukar pelaksanaan alat tanpa mengubah kod.

**Modul Agentic** paling sesuai apabila anda mahu definisi agen bersifat deklaratif dengan anotasi `@Agent`, memerlukan pengurusan aliran kerja (berturutan, gelung, selari), lebih suka reka bentuk agen berasaskan antara muka berbanding kod imperatif, atau menggabungkan pelbagai agen yang berkongsi output melalui `outputKey`.

**Corak Supervisor Agent** bersinar apabila aliran kerja tidak boleh diramalkan terlebih dahulu dan anda mahu LLM membuat keputusan, apabila anda mempunyai pelbagai agen khusus yang memerlukan penyelarasan dinamik, ketika membina sistem perbualan yang menghala ke keupayaan berbeza, atau apabila anda mahu tingkah laku agen yang paling fleksibel dan adaptif.

Untuk membantu anda memilih antara kaedah khusus `@Tool` dari Modul 04 dan alat MCP dari modul ini, perbandingan berikut menonjolkan pertukaran utama — alat khusus memberi anda hubungan rapat dan keselamatan jenis penuh untuk logik khusus aplikasi, manakala alat MCP menawarkan integrasi piawai dan boleh digunakan semula:

<img src="../../../translated_images/ms/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Bila hendak guna kaedah @Tool khusus vs alat MCP — alat khusus untuk logik khusus aplikasi dengan keselamatan jenis lengkap, alat MCP untuk integrasi piawai yang berfungsi merentas aplikasi.*

## Tahniah!

Anda telah menamatkan kelima-lima modul Kursus LangChain4j untuk Pemula! Ini gambaran perjalanan pembelajaran penuh yang anda lalui — dari chat asas sehingga sistem agentic berkuasa MCP:

<img src="../../../translated_images/ms/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Perjalanan pembelajaran anda melalui kelima-lima modul — dari chat asas ke sistem agentic berkuasa MCP.*

Anda telah menamatkan kursus LangChain4j untuk Pemula. Anda telah belajar:

- Cara membina AI perbualan dengan memori (Modul 01)  
- Corak kejuruteraan prompt untuk tugasan berbeza (Modul 02)  
- Mengasaskan respons dalam dokumen anda dengan RAG (Modul 03)  
- Membina agen AI asas (pembantu) dengan alat khusus (Modul 04)  
- Mengintegrasi alat piawai dengan modul LangChain4j MCP dan Agentic (Modul 05)

### Apa Seterusnya?

Selepas menamatkan modul, terokai [Panduan Ujian](../docs/TESTING.md) untuk melihat konsep ujian LangChain4j dalam tindakan.

**Sumber Rasmi:**  
- [Dokumentasi LangChain4j](https://docs.langchain4j.dev/) - Panduan komprehensif dan rujukan API  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Kod sumber dan contoh  
- [Tutorial LangChain4j](https://docs.langchain4j.dev/tutorials/) - Tutorial langkah demi langkah untuk pelbagai kes penggunaan  

Terima kasih kerana menamatkan kursus ini!

---

**Navigasi:** [← Sebelumnya: Modul 04 - Alat](../04-tools/README.md) | [Kembali ke Utama](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila ambil perhatian bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya hendaklah dianggap sebagai sumber yang sahih. Untuk maklumat penting, terjemahan profesional oleh manusia adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->