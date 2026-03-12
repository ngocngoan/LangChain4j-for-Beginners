# Module 05: Protokol Konteks Model (MCP)

## Daftar Isi

- [Video Walkthrough](../../../05-mcp)
- [Apa yang Akan Anda Pelajari](../../../05-mcp)
- [Apa itu MCP?](../../../05-mcp)
- [Bagaimana MCP Bekerja](../../../05-mcp)
- [Modul Agentik](../../../05-mcp)
- [Menjalankan Contoh](../../../05-mcp)
  - [Prasyarat](../../../05-mcp)
- [Mulai Cepat](../../../05-mcp)
  - [Operasi File (Stdio)](../../../05-mcp)
  - [Agen Supervisor](../../../05-mcp)
    - [Menjalankan Demo](../../../05-mcp)
    - [Bagaimana Supervisor Bekerja](../../../05-mcp)
    - [Bagaimana FileAgent Menemukan Alat MCP saat Runtime](../../../05-mcp)
    - [Strategi Respons](../../../05-mcp)
    - [Memahami Output](../../../05-mcp)
    - [Penjelasan Fitur Modul Agentik](../../../05-mcp)
- [Konsep Kunci](../../../05-mcp)
- [Selamat!](../../../05-mcp)
  - [Apa Selanjutnya?](../../../05-mcp)

## Video Walkthrough

Tonton sesi langsung ini yang menjelaskan cara memulai dengan modul ini:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="AI Agents with Tools and MCP - Live Session" width="800"/></a>

## Apa yang Akan Anda Pelajari

Anda telah membangun AI percakapan, menguasai prompt, mendasari respons dalam dokumen, dan membuat agen dengan alat. Tetapi semua alat tersebut dibuat khusus untuk aplikasi Anda. Bagaimana jika Anda bisa memberi AI Anda akses ke ekosistem alat standar yang dapat dibuat dan dibagikan oleh siapa saja? Dalam modul ini, Anda akan belajar cara melakukan itu dengan Protokol Konteks Model (MCP) dan modul agentik LangChain4j. Kami pertama-tama menampilkan pembaca file MCP sederhana lalu menunjukkan bagaimana ia mudah diintegrasikan ke dalam alur kerja agentik canggih menggunakan pola Agen Supervisor.

## Apa itu MCP?

Protokol Konteks Model (MCP) memberikan tepat itu - cara standar bagi aplikasi AI untuk menemukan dan menggunakan alat eksternal. Alih-alih menulis integrasi khusus untuk setiap sumber data atau layanan, Anda terhubung ke server MCP yang menampilkan kemampuan mereka dalam format yang konsisten. Agen AI Anda kemudian bisa menemukan dan menggunakan alat ini secara otomatis.

Diagram di bawah menunjukkan perbedaannya — tanpa MCP, setiap integrasi membutuhkan pengkabelan titik-ke-titik khusus; dengan MCP, satu protokol menghubungkan aplikasi Anda ke alat apa saja:

<img src="../../../translated_images/id/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Sebelum MCP: Integrasi titik-ke-titik yang kompleks. Setelah MCP: Satu protokol, kemungkinan tanpa batas.*

MCP memecahkan masalah mendasar dalam pengembangan AI: setiap integrasi adalah kustom. Ingin mengakses GitHub? Kode kustom. Ingin membaca file? Kode kustom. Ingin melakukan query ke database? Kode kustom. Dan tidak satu pun integrasi ini bekerja dengan aplikasi AI lain.

MCP menstandarisasi ini. Server MCP menampilkan alat dengan deskripsi dan skema parameter yang jelas. Klien MCP mana pun dapat menghubungkan, menemukan alat yang tersedia, dan menggunakannya. Bangun sekali, gunakan di mana saja.

Diagram di bawah menggambarkan arsitektur ini — satu klien MCP (aplikasi AI Anda) menghubungkan ke banyak server MCP, masing-masing menampilkan set alat mereka sendiri melalui protokol standar:

<img src="../../../translated_images/id/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Arsitektur Protokol Konteks Model - penemuan dan eksekusi alat yang distandarisasi*

## Bagaimana MCP Bekerja

Di balik layar, MCP menggunakan arsitektur berlapis. Aplikasi Java Anda (klien MCP) menemukan alat yang tersedia, mengirim permintaan JSON-RPC melalui lapisan transportasi (Stdio atau HTTP), dan server MCP mengeksekusi operasi dan mengembalikan hasil. Diagram berikut memecah setiap lapisan protokol ini:

<img src="../../../translated_images/id/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Bagaimana MCP bekerja di balik layar — klien menemukan alat, bertukar pesan JSON-RPC, dan mengeksekusi operasi melalui lapisan transportasi.*

**Arsitektur Server-Klien**

MCP menggunakan model klien-server. Server menyediakan alat – membaca file, query database, memanggil API. Klien (aplikasi AI Anda) menghubungkan ke server dan menggunakan alat mereka.

Untuk menggunakan MCP dengan LangChain4j, tambahkan dependensi Maven ini:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Penemuan Alat**

Ketika klien Anda menghubungkan ke server MCP, ia bertanya "Alat apa yang Anda miliki?" Server merespons dengan daftar alat yang tersedia, masing-masing dengan deskripsi dan skema parameter. Agen AI Anda kemudian dapat memilih alat berdasarkan permintaan pengguna. Diagram di bawah menunjukkan jabat tangan ini — klien mengirim permintaan `tools/list` dan server mengembalikan alat yang tersedia dengan deskripsi dan skema parameter:

<img src="../../../translated_images/id/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI menemukan alat yang tersedia saat startup — kini tahu kemampuan apa yang ada dan dapat memilih yang akan digunakan.*

**Mekanisme Transportasi**

MCP mendukung berbagai mekanisme transportasi. Dua opsi adalah Stdio (untuk komunikasi proses lokal) dan Streamable HTTP (untuk server jarak jauh). Modul ini menunjukkan transport Stdio:

<img src="../../../translated_images/id/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*Mekanisme transport MCP: HTTP untuk server jarak jauh, Stdio untuk proses lokal*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Untuk proses lokal. Aplikasi Anda memulai server sebagai proses anak dan berkomunikasi melalui input/output standar. Berguna untuk akses sistem file atau alat baris perintah.

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

Server `@modelcontextprotocol/server-filesystem` menampilkan alat-alat berikut, semuanya dibatasi ke direktori yang Anda tentukan:

| Alat | Deskripsi |
|------|-----------|
| `read_file` | Membaca isi satu file |
| `read_multiple_files` | Membaca beberapa file dalam satu panggilan |
| `write_file` | Membuat atau menimpa file |
| `edit_file` | Melakukan edit cari-ganti yang terarah |
| `list_directory` | Menampilkan daftar file dan direktori pada sebuah jalur |
| `search_files` | Mencari file secara rekursif yang cocok dengan pola |
| `get_file_info` | Mendapatkan metadata file (ukuran, cap waktu, izin) |
| `create_directory` | Membuat direktori (termasuk direktori induk) |
| `move_file` | Memindahkan atau mengganti nama file atau direktori |

Diagram berikut menunjukkan bagaimana transport Stdio bekerja saat runtime — aplikasi Java Anda memulai server MCP sebagai proses anak dan mereka berkomunikasi melalui pipa stdin/stdout, tanpa jaringan atau HTTP:

<img src="../../../translated_images/id/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Transport Stdio dalam aksi — aplikasi Anda memulai server MCP sebagai proses anak dan berkomunikasi melalui pipa stdin/stdout.*

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) dan tanya:
> - "Bagaimana cara kerja transport Stdio dan kapan sebaiknya saya menggunakannya dibanding HTTP?"
> - "Bagaimana LangChain4j mengelola siklus hidup proses server MCP yang dipanggil?"
> - "Apa implikasi keamanan dari memberi AI akses ke sistem file?"

## Modul Agentik

Sementara MCP menyediakan alat standar, modul **agentik** LangChain4j menyediakan cara deklaratif untuk membangun agen yang mengorkestrasi alat tersebut. Anotasi `@Agent` dan `AgenticServices` membiarkan Anda mendefinisikan perilaku agen melalui antarmuka bukan kode imperatif.

Dalam modul ini, Anda akan mengeksplorasi pola **Supervisor Agent** — pendekatan AI agentik canggih di mana agen "supervisor" secara dinamis menentukan agen sub mana yang dipanggil berdasarkan permintaan pengguna. Kita akan menggabungkan kedua konsep dengan memberi salah satu sub-agen kita kemampuan akses file yang didukung MCP.

Untuk menggunakan modul agentik, tambahkan dependensi Maven ini:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Catatan:** Modul `langchain4j-agentic` menggunakan properti versi terpisah (`langchain4j.mcp.version`) karena dirilis dengan jadwal berbeda dari pustaka inti LangChain4j.

> **⚠️ Eksperimental:** Modul `langchain4j-agentic` bersifat **eksperimental** dan mungkin berubah. Cara stabil membangun asisten AI tetap dengan `langchain4j-core` dengan alat kustom (Modul 04).

## Menjalankan Contoh

### Prasyarat

- Sudah menyelesaikan [Modul 04 - Tools](../04-tools/README.md) (modul ini membangun konsep alat kustom dan membandingkannya dengan alat MCP)
- File `.env` di direktori root dengan kredensial Azure (dibuat oleh `azd up` di Modul 01)
- Java 21+, Maven 3.9+
- Node.js 16+ dan npm (untuk server MCP)

> **Catatan:** Jika Anda belum mengatur variabel lingkungan, lihat [Modul 01 - Pengenalan](../01-introduction/README.md) untuk instruksi deploy (`azd up` otomatis membuat file `.env`), atau salin `.env.example` ke `.env` di root dan isi nilainya.

## Mulai Cepat

**Menggunakan VS Code:** Klik kanan pada file demo apa saja di Explorer dan pilih **"Run Java"**, atau gunakan konfigurasi peluncur dari panel Run and Debug (pastikan file `.env` sudah dikonfigurasi dengan kredensial Azure).

**Menggunakan Maven:** Alternatifnya, Anda bisa menjalankan dari baris perintah dengan contoh di bawah.

### Operasi File (Stdio)

Ini mendemonstrasikan alat berbasis proses anak lokal.

**✅ Tidak perlu prasyarat** - server MCP dijalankan otomatis.

**Menggunakan Skrip Mulai (Direkomendasikan):**

Skrip mulai memuat variabel lingkungan secara otomatis dari file `.env` root:

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

**Menggunakan VS Code:** Klik kanan pada `StdioTransportDemo.java` dan pilih **"Run Java"** (pastikan file `.env` sudah dikonfigurasi).

Aplikasi ini secara otomatis menjalankan server sistem file MCP dan membaca file lokal. Perhatikan bagaimana manajemen proses subprocess ditangani untuk Anda.

**Output yang diharapkan:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agen Supervisor

Pola **Supervisor Agent** adalah bentuk AI agentik yang **fleksibel**. Seorang Supervisor menggunakan LLM untuk memutuskan secara otomatis agen mana yang dipanggil berdasarkan permintaan pengguna. Pada contoh berikut, kita menggabungkan akses file berbasis MCP dengan agen LLM untuk membuat alur kerja baca file → laporan yang diawasi.

Dalam demo, `FileAgent` membaca file menggunakan alat filesystem MCP, dan `ReportAgent` membuat laporan terstruktur dengan ringkasan eksekutif (1 kalimat), 3 poin utama, dan rekomendasi. Supervisor mengorkestrasi alur ini secara otomatis:

<img src="../../../translated_images/id/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Supervisor menggunakan LLM-nya untuk menentukan agen mana yang dipanggil dan dalam urutan apa — tanpa routing yang dikodekan keras.*

Berikut alur kerja konkret untuk pipeline file-ke-laporan kami:

<img src="../../../translated_images/id/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent membaca file lewat alat MCP, kemudian ReportAgent mengubah isi mentah menjadi laporan terstruktur.*

Diagram urutan berikut melacak orkestrasi lengkap Supervisor — dari memulai server MCP, melalui pemilihan agen otonom Supervisor, hingga panggilan alat lewat stdio dan laporan akhir:

<img src="../../../translated_images/id/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*Supervisor secara otonom memanggil FileAgent (yang memanggil server MCP lewat stdio untuk membaca file), lalu memanggil ReportAgent untuk menghasilkan laporan terstruktur — setiap agen menyimpan output di Scope Agentik bersama.*

Setiap agen menyimpan outputnya di **Scope Agentik** (memori bersama), memungkinkan agen berikutnya mengakses hasil sebelumnya. Ini menunjukkan bagaimana alat MCP terintegrasi mulus ke dalam alur kerja agentik — Supervisor tidak perlu tahu *bagaimana* file dibaca, hanya bahwa `FileAgent` bisa melakukannya.

#### Menjalankan Demo

Skrip mulai memuat variabel lingkungan secara otomatis dari file `.env` root:

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

**Menggunakan VS Code:** Klik kanan pada `SupervisorAgentDemo.java` dan pilih **"Run Java"** (pastikan file `.env` sudah dikonfigurasi).

#### Bagaimana Supervisor Bekerja

Sebelum membangun agen, Anda perlu menghubungkan transport MCP ke klien dan membungkusnya sebagai `ToolProvider`. Inilah cara alat server MCP tersedia untuk agen Anda:

```java
// Buat klien MCP dari transportasi
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Bungkus klien sebagai ToolProvider — ini menghubungkan alat MCP ke LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Sekarang Anda bisa menyisipkan `mcpToolProvider` ke agen mana pun yang membutuhkan alat MCP:

```java
// Langkah 1: FileAgent membaca file menggunakan alat MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Memiliki alat MCP untuk operasi file
        .build();

// Langkah 2: ReportAgent menghasilkan laporan terstruktur
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor mengatur alur kerja file → laporan
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Mengembalikan laporan akhir
        .build();

// Supervisor memutuskan agen mana yang akan dipanggil berdasarkan permintaan
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Bagaimana FileAgent Menemukan Alat MCP saat Runtime

Anda mungkin bertanya-tanya: **bagaimana `FileAgent` tahu cara menggunakan alat filesystem npm?** Jawabannya adalah tidak — **LLM** yang mengetahuinya saat runtime lewat skema alat.
Antarmuka `FileAgent` hanyalah sebuah **definisi prompt**. Ia tidak memiliki pengetahuan yang terprogram secara langsung tentang `read_file`, `list_directory`, atau alat MCP lainnya. Berikut ini yang terjadi secara menyeluruh:

1. **Server dijalankan:** `StdioMcpTransport` meluncurkan paket npm `@modelcontextprotocol/server-filesystem` sebagai proses anak
2. **Penemuan alat:** `McpClient` mengirim permintaan JSON-RPC `tools/list` ke server, yang membalas dengan nama alat, deskripsi, dan skema parameter (misalnya, `read_file` — *"Membaca isi lengkap sebuah file"* — `{ path: string }`)
3. **Injeksi skema:** `McpToolProvider` membungkus skema yang ditemukan ini dan membuatnya tersedia untuk LangChain4j
4. **Keputusan LLM:** Ketika `FileAgent.readFile(path)` dipanggil, LangChain4j mengirim pesan sistem, pesan pengguna, **dan daftar skema alat** ke LLM. LLM membaca deskripsi alat dan menghasilkan panggilan alat (misalnya, `read_file(path="/some/file.txt")`)
5. **Eksekusi:** LangChain4j menyusupi panggilan alat, mengarahkannya melalui klien MCP kembali ke proses Node.js, mengambil hasilnya, dan memberikannya kembali ke LLM

Ini adalah mekanisme [Tool Discovery](../../../05-mcp) yang sama seperti yang dijelaskan di atas, tetapi diterapkan secara khusus pada alur kerja agent. Anotasi `@SystemMessage` dan `@UserMessage` membimbing perilaku LLM, sementara `ToolProvider` yang disuntikkan memberinya **kemampuan** — LLM menghubungkan keduanya saat runtime.

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) dan tanyakan:
> - "Bagaimana agent ini tahu alat MCP mana yang harus dipanggil?"
> - "Apa yang terjadi jika saya menghapus ToolProvider dari builder agen?"
> - "Bagaimana skema alat diteruskan ke LLM?"

#### Strategi Respon

Saat Anda mengonfigurasi `SupervisorAgent`, Anda menentukan bagaimana ia harus merumuskan jawaban akhirnya kepada pengguna setelah sub-agent menyelesaikan tugasnya. Diagram berikut menunjukkan tiga strategi yang tersedia — LAST mengembalikan output agen terakhir secara langsung, SUMMARY mensintesis semua output melalui LLM, dan SCORED memilih yang nilainya lebih tinggi terhadap permintaan asli:

<img src="../../../translated_images/id/response-strategies.3d0cea19d096bdf9.webp" alt="Strategi Respon" width="800"/>

*Tiga strategi untuk bagaimana Supervisor merumuskan respons akhirnya — pilih berdasarkan apakah Anda ingin output agen terakhir, ringkasan yang disintesis, atau opsi dengan skor terbaik.*

Strategi yang tersedia adalah:

| Strategi | Deskripsi |
|----------|-------------|
| **LAST** | Supervisor mengembalikan output dari sub-agent atau alat terakhir yang dipanggil. Ini berguna ketika agen terakhir dalam alur kerja secara khusus dirancang untuk menghasilkan jawaban lengkap dan final (misalnya, "Summary Agent" dalam pipeline riset). |
| **SUMMARY** | Supervisor menggunakan Model Bahasa internalnya untuk mensintesis ringkasan seluruh interaksi dan semua output sub-agent, lalu mengembalikan ringkasan itu sebagai respons akhir. Ini memberikan jawaban yang bersih dan teragregasi kepada pengguna. |
| **SCORED** | Sistem menggunakan LLM internal untuk memberi skor pada respons LAST dan SUMMARY terhadap permintaan asli pengguna, mengembalikan output yang mendapat skor lebih tinggi. |

Lihat [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) untuk implementasi lengkap.

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) dan tanyakan:
> - "Bagaimana Supervisor memutuskan agen mana yang akan dipanggil?"
> - "Apa perbedaan antara pola kerja Supervisor dan Sequential?"
> - "Bagaimana saya bisa menyesuaikan perilaku perencanaan Supervisor?"

#### Memahami Output

Saat Anda menjalankan demo, Anda akan melihat langkah terstruktur tentang bagaimana Supervisor mengorkestrasi beberapa agen. Berikut arti setiap bagian:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Header** memperkenalkan konsep alur kerja: pipeline terfokus dari pembacaan file hingga pembuatan laporan.

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

**Diagram Alur Kerja** menunjukkan aliran data antar agen. Setiap agen memiliki peran spesifik:
- **FileAgent** membaca file menggunakan alat MCP dan menyimpan isi mentah di `fileContent`
- **ReportAgent** menggunakan isi itu dan menghasilkan laporan terstruktur di `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Permintaan Pengguna** menunjukkan tugas. Supervisor memparsing ini dan memutuskan untuk memanggil FileAgent → ReportAgent.

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

**Orkestrasi Supervisor** menunjukkan alur 2 langkah dalam aksi:
1. **FileAgent** membaca file via MCP dan menyimpan isinya
2. **ReportAgent** menerima isi tersebut dan membuat laporan terstruktur

Supervisor membuat keputusan ini **secara otonom** berdasarkan permintaan pengguna.

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

#### Penjelasan Fitur Modul Agentic

Contoh ini memperagakan beberapa fitur lanjutan modul agentic. Mari kita lihat lebih dekat Agentic Scope dan Agent Listeners.

**Agentic Scope** menunjukkan memori bersama di mana agen menyimpan hasil mereka menggunakan `@Agent(outputKey="...")`. Ini memungkinkan:
- Agen berikutnya mengakses output agen sebelumnya
- Supervisor mensintesis respons akhir
- Anda memeriksa apa yang dihasilkan setiap agen

Diagram berikut menunjukkan bagaimana Agentic Scope berfungsi sebagai memori bersama dalam alur kerja file-ke-laporan — FileAgent menulis output di bawah kunci `fileContent`, ReportAgent membacanya dan menulis outputnya di bawah `report`:

<img src="../../../translated_images/id/agentic-scope.95ef488b6c1d02ef.webp" alt="Memori Bersama Agentic Scope" width="800"/>

*Agentic Scope bertindak sebagai memori bersama — FileAgent menulis `fileContent`, ReportAgent membacanya dan menulis `report`, dan kode Anda membaca hasil akhir.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Data file mentah dari FileAgent
String report = scope.readState("report");            // Laporan terstruktur dari ReportAgent
```

**Agent Listeners** memungkinkan pemantauan dan debugging eksekusi agen. Output langkah demi langkah yang Anda lihat di demo berasal dari AgentListener yang terhubung ke setiap panggilan agen:
- **beforeAgentInvocation** - Dipanggil saat Supervisor memilih agen, memungkinkan Anda melihat agen mana yang dipilih dan alasannya
- **afterAgentInvocation** - Dipanggil saat agen selesai, menampilkan hasilnya
- **inheritedBySubagents** - Jika true, listener memantau semua agen dalam hierarki

Diagram berikut menunjukkan siklus hidup lengkap Agent Listener, termasuk bagaimana `onError` menangani kegagalan selama eksekusi agen:

<img src="../../../translated_images/id/agent-listeners.784bfc403c80ea13.webp" alt="Siklus Hidup Agent Listeners" width="800"/>

*Agent Listeners terhubung ke siklus hidup eksekusi — memantau saat agen mulai, selesai, atau mengalami kesalahan.*

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
        return true; // Sebarkan ke semua sub-agen
    }
};
```

Selain pola Supervisor, modul `langchain4j-agentic` menyediakan beberapa pola alur kerja yang kuat. Diagram berikut menunjukkan kelimanya — dari pipeline sederhana berurutan sampai alur kerja persetujuan dengan manusia dalam loop:

<img src="../../../translated_images/id/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Pola Alur Kerja Agen" width="800"/>

*Lima pola alur kerja untuk mengorkestrasi agen — dari pipeline berurutan sederhana sampai alur kerja persetujuan dengan manusia dalam loop.*

| Pola | Deskripsi | Kasus Penggunaan |
|---------|-------------|----------|
| **Sequential** | Eksekusi agen secara berurutan, output mengalir ke berikutnya | Pipeline: riset → analisis → laporan |
| **Parallel** | Jalankan agen secara bersamaan | Tugas independen: cuaca + berita + saham |
| **Loop** | Ulangi sampai kondisi terpenuhi | Skor kualitas: perbaiki sampai skor ≥ 0.8 |
| **Conditional** | Rute berdasarkan kondisi | Klasifikasi → rute ke agen spesialis |
| **Human-in-the-Loop** | Tambah titik pemeriksaan manusia | Alur kerja persetujuan, review konten |

## Konsep Kunci

Setelah Anda menjelajahi MCP dan modul agentic dalam aksi, mari kita rangkum kapan menggunakan masing-masing pendekatan.

Salah satu keunggulan terbesar MCP adalah ekosistemnya yang berkembang. Diagram berikut menunjukkan bagaimana sebuah protokol universal menghubungkan aplikasi AI Anda ke berbagai server MCP — dari akses sistem file dan database hingga GitHub, email, web scraping, dan lainnya:

<img src="../../../translated_images/id/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="Ekosistem MCP" width="800"/>

*MCP menciptakan ekosistem protokol universal — server MCP kompatibel mana pun bekerja dengan klien MCP kompatibel mana pun, memungkinkan berbagi alat antar aplikasi.*

**MCP** ideal ketika Anda ingin memanfaatkan ekosistem alat yang sudah ada, membangun alat yang dapat dibagikan oleh banyak aplikasi, mengintegrasikan layanan pihak ketiga dengan protokol standar, atau mengganti implementasi alat tanpa mengubah kode.

**Modul Agentic** paling cocok ketika Anda menginginkan definisi agen deklaratif dengan anotasi `@Agent`, membutuhkan orkestrasi alur kerja (berurutan, loop, paralel), lebih menyukai desain agen berbasis antarmuka daripada kode imperatif, atau menggabungkan beberapa agen yang berbagi output melalui `outputKey`.

**Pola Supervisor Agent** menonjol ketika alur kerja tidak dapat diprediksi sebelumnya dan Anda ingin LLM yang memutuskan, saat memiliki beberapa agen spesialis yang memerlukan orkestrasi dinamis, saat membangun sistem percakapan yang mengarahkan ke berbagai kemampuan, atau saat Anda menginginkan perilaku agen yang paling fleksibel dan adaptif.

Untuk membantu memilih antara metode `@Tool` khusus dari Modul 04 dan alat MCP dari modul ini, perbandingan berikut menyoroti pertukaran utama — alat khusus memberikan keterikatan erat dan keamanan tipe penuh untuk logika aplikasi spesifik, sementara alat MCP menawarkan integrasi standar dan dapat digunakan ulang:

<img src="../../../translated_images/id/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Metode Alat Khusus vs Alat MCP" width="800"/>

*Kapan menggunakan metode @Tool khusus vs alat MCP — alat khusus untuk logika aplikasi spesifik dengan keamanan tipe penuh, alat MCP untuk integrasi standar yang bekerja lintas aplikasi.*

## Selamat!

Anda telah menyelesaikan kelima modul kursus LangChain4j untuk Pemula! Berikut ini perjalanan pembelajaran lengkap yang telah Anda lalui — dari chat dasar hingga sistem agentic yang didukung MCP:

<img src="../../../translated_images/id/course-completion.48cd201f60ac7570.webp" alt="Penyelesaian Kursus" width="800"/>

*Perjalanan pembelajaran Anda melalui kelima modul — dari chat dasar hingga sistem agentic yang didukung MCP.*

Anda telah menyelesaikan kursus LangChain4j untuk Pemula. Anda telah belajar:

- Cara membangun AI percakapan dengan memori (Modul 01)
- Pola rekayasa prompt untuk tugas berbeda (Modul 02)
- Membumikan respons dalam dokumen dengan RAG (Modul 03)
- Membuat agen AI dasar (asisten) dengan alat khusus (Modul 04)
- Mengintegrasikan alat standar dengan LangChain4j MCP dan modul Agentic (Modul 05)

### Apa Selanjutnya?

Setelah menyelesaikan modul, jelajahi [Panduan Pengujian](../docs/TESTING.md) untuk melihat konsep pengujian LangChain4j dalam aksi.

**Sumber Resmi:**
- [Dokumentasi LangChain4j](https://docs.langchain4j.dev/) - Panduan lengkap dan referensi API
- [GitHub LangChain4j](https://github.com/langchain4j/langchain4j) - Kode sumber dan contoh
- [Tutorial LangChain4j](https://docs.langchain4j.dev/tutorials/) - Tutorial langkah demi langkah untuk berbagai kasus penggunaan

Terima kasih telah menyelesaikan kursus ini!

---

**Navigasi:** [← Sebelumnya: Modul 04 - Tools](../04-tools/README.md) | [Kembali ke Utama](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berupaya mencapai akurasi, harap diperhatikan bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang sah. Untuk informasi penting, disarankan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau interpretasi yang salah yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->