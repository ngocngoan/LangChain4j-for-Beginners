# Modul 05: Model Context Protocol (MCP)

## Daftar Isi

- [Apa yang Akan Anda Pelajari](../../../05-mcp)
- [Apa itu MCP?](../../../05-mcp)
- [Bagaimana MCP Bekerja](../../../05-mcp)
- [Modul Agentik](../../../05-mcp)
- [Menjalankan Contoh](../../../05-mcp)
  - [Prasyarat](../../../05-mcp)
- [Mulai Cepat](../../../05-mcp)
  - [Operasi Berkas (Stdio)](../../../05-mcp)
  - [Agen Pengawas](../../../05-mcp)
    - [Menjalankan Demo](../../../05-mcp)
    - [Bagaimana Pengawas Bekerja](../../../05-mcp)
    - [Strategi Respon](../../../05-mcp)
    - [Memahami Output](../../../05-mcp)
    - [Penjelasan Fitur Modul Agentik](../../../05-mcp)
- [Konsep Kunci](../../../05-mcp)
- [Selamat!](../../../05-mcp)
  - [Apa Selanjutnya?](../../../05-mcp)

## Apa yang Akan Anda Pelajari

Anda telah membangun AI percakapan, menguasai prompt, mendasari jawaban pada dokumen, dan membuat agen dengan alat. Namun semua alat tersebut dibuat khusus untuk aplikasi spesifik Anda. Bagaimana jika Anda bisa memberikan AI Anda akses ke ekosistem alat yang distandarisasi yang bisa dibuat dan dibagikan siapa saja? Di modul ini, Anda akan belajar cara melakukan hal itu dengan Model Context Protocol (MCP) dan modul agentik LangChain4j. Kami pertama-tama menampilkan pembaca berkas MCP sederhana dan kemudian menunjukkan bagaimana ia mudah diintegrasikan ke alur kerja agentik lanjutan menggunakan pola Agen Pengawas.

## Apa itu MCP?

Model Context Protocol (MCP) menyediakan tepat itu — cara standar bagi aplikasi AI untuk menemukan dan menggunakan alat eksternal. Alih-alih menulis integrasi khusus untuk setiap sumber data atau layanan, Anda menghubungkan ke server MCP yang menampilkan kapabilitas mereka dalam format yang konsisten. Agen AI Anda kemudian dapat secara otomatis menemukan dan menggunakan alat-alat ini.

Diagram di bawah menunjukkan perbedaannya — tanpa MCP, setiap integrasi membutuhkan pengkabelan khusus point-to-point; dengan MCP, satu protokol menghubungkan aplikasi Anda ke alat apa pun:

<img src="../../../translated_images/id/mcp-comparison.9129a881ecf10ff5.webp" alt="Perbandingan MCP" width="800"/>

*Sebelum MCP: Integrasi kompleks point-to-point. Sesudah MCP: Satu protokol, kemungkinan tanpa batas.*

MCP memecahkan masalah mendasar dalam pengembangan AI: setiap integrasi bersifat khusus. Ingin mengakses GitHub? Kode khusus. Ingin membaca file? Kode khusus. Ingin kueri basis data? Kode khusus. Dan tidak ada integrasi ini yang bekerja dengan aplikasi AI lain.

MCP menstandarkan ini. Server MCP mengekspose alat dengan deskripsi dan skema parameter yang jelas. Klien MCP mana pun dapat terhubung, menemukan alat yang tersedia, dan menggunakannya. Bangun sekali, gunakan di mana-mana.

Diagram di bawah menggambarkan arsitektur ini — satu klien MCP (aplikasi AI Anda) terhubung ke beberapa server MCP, masing-masing mengekspose set alat mereka sendiri melalui protokol standar:

<img src="../../../translated_images/id/mcp-architecture.b3156d787a4ceac9.webp" alt="Arsitektur MCP" width="800"/>

*Arsitektur Model Context Protocol - penemuan dan eksekusi alat yang distandarisasi*

## Bagaimana MCP Bekerja

Di balik layar, MCP menggunakan arsitektur berlapis. Aplikasi Java Anda (klien MCP) menemukan alat yang tersedia, mengirimkan permintaan JSON-RPC melalui lapisan transport (Stdio atau HTTP), dan server MCP mengeksekusi operasi serta mengembalikan hasil. Diagram berikut merinci tiap lapisan dari protokol ini:

<img src="../../../translated_images/id/mcp-protocol-detail.01204e056f45308b.webp" alt="Detail Protokol MCP" width="800"/>

*Bagaimana MCP bekerja di balik layar — klien menemukan alat, bertukar pesan JSON-RPC, dan mengeksekusi operasi melalui lapisan transport.*

**Arsitektur Server-Klien**

MCP menggunakan model klien-server. Server menyediakan alat — membaca berkas, kueri database, memanggil API. Klien (aplikasi AI Anda) terhubung ke server dan menggunakan alat mereka.

Untuk menggunakan MCP dengan LangChain4j, tambahkan dependensi Maven ini:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Penemuan Alat**

Ketika klien Anda terhubung ke server MCP, ia bertanya "Alat apa yang Anda miliki?" Server merespon dengan daftar alat yang tersedia, masing-masing dengan deskripsi dan skema parameter. Agen AI Anda kemudian dapat memutuskan alat mana yang akan digunakan berdasarkan permintaan pengguna. Diagram di bawah menunjukkan jabat tangan ini — klien mengirim permintaan `tools/list` dan server mengembalikan alat yang tersedia beserta deskripsi dan skema parameter:

<img src="../../../translated_images/id/tool-discovery.07760a8a301a7832.webp" alt="Penemuan Alat MCP" width="800"/>

*AI menemukan alat yang tersedia pada saat mulai — kini ia tahu kapabilitas yang tersedia dan dapat memutuskan mana yang digunakan.*

**Mekanisme Transport**

MCP mendukung berbagai mekanisme transport. Dua opsi adalah Stdio (untuk komunikasi subprocess lokal) dan Streamable HTTP (untuk server jarak jauh). Modul ini mendemonstrasikan transport Stdio:

<img src="../../../translated_images/id/transport-mechanisms.2791ba7ee93cf020.webp" alt="Mekanisme Transport" width="800"/>

*Mekanisme transport MCP: HTTP untuk server jarak jauh, Stdio untuk proses lokal*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Untuk proses lokal. Aplikasi Anda menjalankan server sebagai subprocess dan berkomunikasi melalui input/output standar. Berguna untuk akses sistem berkas atau alat baris perintah.

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

Server `@modelcontextprotocol/server-filesystem` mengekspose alat berikut, semuanya dibatasi pada direktori yang Anda tentukan:

| Alat | Deskripsi |
|------|-----------|
| `read_file` | Membaca isi satu berkas |
| `read_multiple_files` | Membaca beberapa berkas dalam satu panggilan |
| `write_file` | Membuat atau menimpa berkas |
| `edit_file` | Melakukan pengeditan cari dan ganti yang terarah |
| `list_directory` | Mendaftar berkas dan direktori pada suatu path |
| `search_files` | Mencari berkas secara rekursif sesuai pola |
| `get_file_info` | Mendapatkan metadata berkas (ukuran, stempel waktu, izin) |
| `create_directory` | Membuat direktori (termasuk direktori induk) |
| `move_file` | Memindahkan atau mengganti nama berkas atau direktori |

Diagram berikut menunjukkan bagaimana transport Stdio bekerja saat runtime — aplikasi Java Anda menjalankan server MCP sebagai proses anak dan mereka berkomunikasi melalui pipa stdin/stdout, tanpa jaringan atau HTTP:

<img src="../../../translated_images/id/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Alur Transport Stdio" width="800"/>

*Transport Stdio dalam aksi — aplikasi Anda menjalankan server MCP sebagai proses anak dan berkomunikasi melalui pipa stdin/stdout.*

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) dan tanyakan:
> - "Bagaimana transport Stdio bekerja dan kapan saya harus menggunakannya dibanding HTTP?"
> - "Bagaimana LangChain4j mengelola siklus hidup proses server MCP yang dijalankan?"
> - "Apa implikasi keamanan dari memberikan AI akses ke sistem berkas?"

## Modul Agentik

Walaupun MCP menyediakan alat standar, modul **agentik** LangChain4j menyediakan cara deklaratif untuk membangun agen yang mengorkestrasi alat tersebut. Anotasi `@Agent` dan `AgenticServices` memungkinkan Anda mendefinisikan perilaku agen lewat antarmuka bukan kode imperatif.

Dalam modul ini, Anda akan mengeksplorasi pola **Agen Pengawas** — pendekatan AI agentik lanjutan di mana agen "pengawas" secara dinamis memutuskan sub-agen mana yang dipanggil berdasar permintaan pengguna. Kami menggabungkan kedua konsep dengan memberikan salah satu sub-agen kemampuan akses berkas yang didukung MCP.

Untuk menggunakan modul agentik, tambahkan dependensi Maven ini:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Catatan:** Modul `langchain4j-agentic` menggunakan properti versi terpisah (`langchain4j.mcp.version`) karena dirilis dengan jadwal berbeda dari pustaka inti LangChain4j.

> **⚠️ Eksperimental:** Modul `langchain4j-agentic` **bersifat eksperimental** dan dapat berubah. Cara stabil membangun asisten AI tetap menggunakan `langchain4j-core` dengan alat khusus (Modul 04).

## Menjalankan Contoh

### Prasyarat

- Sudah menyelesaikan [Modul 04 - Tools](../04-tools/README.md) (modul ini membangun konsep alat khusus dan membandingkannya dengan alat MCP)
- Berkas `.env` di direktori root dengan kredensial Azure (dibuat oleh `azd up` di Modul 01)
- Java 21+, Maven 3.9+
- Node.js 16+ dan npm (untuk server MCP)

> **Catatan:** Jika Anda belum mengatur variabel lingkungan, lihat [Modul 01 - Pendahuluan](../01-introduction/README.md) untuk instruksi deploy (`azd up` membuat berkas `.env` secara otomatis), atau salin `.env.example` ke `.env` di direktori root dan isi nilainya.

## Mulai Cepat

**Menggunakan VS Code:** Cukup klik kanan pada file demo di Explorer dan pilih **"Run Java"**, atau gunakan konfigurasi peluncuran dari panel Run and Debug (pastikan berkas `.env` sudah dikonfigurasi dengan kredensial Azure terlebih dahulu).

**Menggunakan Maven:** Sebagai alternatif, Anda bisa menjalankan dari baris perintah dengan contoh di bawah.

### Operasi Berkas (Stdio)

Ini mendemonstrasikan alat berbasis subprocess lokal.

**✅ Tidak perlu prasyarat** - server MCP dijalankan otomatis.

**Menggunakan Skrip Mulai (Direkomendasikan):**

Skrip mulai otomatis memuat variabel lingkungan dari berkas `.env` root:

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

**Menggunakan VS Code:** Klik kanan pada `StdioTransportDemo.java` dan pilih **"Run Java"** (pastikan berkas `.env` sudah dikonfigurasi).

Aplikasi menjalankan server MCP filesystem secara otomatis dan membaca sebuah berkas lokal. Perhatikan bagaimana pengelolaan proses subprocess ditangani untuk Anda.

**Output yang diharapkan:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agen Pengawas

**Pola Agen Pengawas** adalah bentuk AI agentik yang **fleksibel**. Seorang Pengawas menggunakan LLM untuk secara otonom memutuskan agen mana yang akan dipanggil berdasarkan permintaan pengguna. Dalam contoh berikut, kami menggabungkan akses berkas bertenaga MCP dengan agen LLM untuk membuat alur kerja baca berkas → laporan yang diawasi.

Dalam demo, `FileAgent` membaca berkas menggunakan alat filesystem MCP, dan `ReportAgent` menghasilkan laporan terstruktur dengan ringkasan eksekutif (1 kalimat), 3 poin kunci, dan rekomendasi. Pengawas mengorkestrasi alur ini secara otomatis:

<img src="../../../translated_images/id/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Pola Agen Pengawas" width="800"/>

*Pengawas menggunakan LLM-nya untuk memutuskan agen mana yang dipanggil dan dalam urutan apa — tanpa perlu routing koding keras.*

Berikut alur kerja konkret untuk pipeline file-ke-laporan kami:

<img src="../../../translated_images/id/file-report-workflow.649bb7a896800de9.webp" alt="Alur Kerja File ke Laporan" width="800"/>

*FileAgent membaca berkas lewat alat MCP, lalu ReportAgent mengubah isi mentah menjadi laporan terstruktur.*

Setiap agen menyimpan outputnya di **Agentic Scope** (memori bersama), memungkinkan agen berikutnya mengakses hasil sebelumnya. Ini menunjukkan bagaimana alat MCP terintegrasi mulus ke alur kerja agentik — Pengawas tidak perlu tahu *bagaimana* berkas dibaca, hanya bahwa `FileAgent` bisa melakukannya.

#### Menjalankan Demo

Skrip mulai otomatis memuat variabel lingkungan dari berkas `.env` root:

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

**Menggunakan VS Code:** Klik kanan pada `SupervisorAgentDemo.java` dan pilih **"Run Java"** (pastikan berkas `.env` sudah dikonfigurasi).

#### Bagaimana Pengawas Bekerja

Sebelum membangun agen, Anda perlu menghubungkan transport MCP ke klien dan membungkusnya sebagai `ToolProvider`. Ini cara alat server MCP tersedia untuk agen Anda:

```java
// Buat klien MCP dari transportasi
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Bungkus klien sebagai ToolProvider — ini menjembatani alat MCP ke dalam LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Sekarang Anda bisa menyuntikkan `mcpToolProvider` ke agen mana pun yang membutuhkan alat MCP:

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

#### Strategi Respon

Saat mengonfigurasi `SupervisorAgent`, Anda menentukan bagaimana ia harus merumuskan jawaban akhir kepada pengguna setelah sub-agen menyelesaikan tugas mereka. Diagram di bawah menunjukkan tiga strategi yang tersedia — LAST mengembalikan keluaran agen terakhir secara langsung, SUMMARY mensintesis semua keluaran melalui LLM, dan SCORED memilih yang nilainya lebih tinggi terhadap permintaan asli:

<img src="../../../translated_images/id/response-strategies.3d0cea19d096bdf9.webp" alt="Strategi Respon" width="800"/>

*Tiga strategi bagaimana Pengawas merumuskan respons akhirnya — pilih berdasarkan apakah Anda ingin keluaran agen terakhir, ringkasan yang disintesis, atau opsi dengan skor terbaik.*

Strategi yang tersedia adalah:

| Strategi | Deskripsi |
|----------|-----------|
| **LAST** | Pengawas mengembalikan output dari sub-agen atau alat terakhir yang dipanggil. Ini berguna ketika agen akhir dalam alur kerja memang dirancang untuk menghasilkan jawaban lengkap dan final (misalnya, "Agen Ringkasan" dalam pipeline riset). |
| **SUMMARY** | Pengawas menggunakan Model Bahasa internalnya sendiri (LLM) untuk mensintesis ringkasan seluruh interaksi dan semua output sub-agen, lalu mengembalikan ringkasan itu sebagai respons akhir. Ini memberikan jawaban teragregasi yang bersih kepada pengguna. |
| **SCORED** | Sistem menggunakan LLM internal untuk menilai baik respons LAST maupun SUMMARY terhadap permintaan pengguna asli, mengembalikan output yang mendapat skor lebih tinggi. |
Lihat [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) untuk implementasi lengkap.

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) dan tanyakan:
> - "Bagaimana Supervisor memutuskan agen mana yang akan dipanggil?"
> - "Apa perbedaan antara pola Supervisor dan pola alur kerja Sequential?"
> - "Bagaimana saya bisa menyesuaikan perilaku perencanaan Supervisor?"

#### Memahami Output

Saat Anda menjalankan demo, Anda akan melihat panduan terstruktur tentang bagaimana Supervisor mengorkestrasi beberapa agen. Berikut arti dari setiap bagian:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Header** memperkenalkan konsep alur kerja: sebuah pipeline terfokus dari pembacaan file hingga pembuatan laporan.

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
  
**Diagram Alur Kerja** menunjukkan aliran data antar agen. Setiap agen memiliki peran khusus:  
- **FileAgent** membaca file menggunakan alat MCP dan menyimpan konten mentah di `fileContent`  
- **ReportAgent** menggunakan konten tersebut dan menghasilkan laporan terstruktur di `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Permintaan Pengguna** menunjukkan tugasnya. Supervisor menguraikan permintaan ini dan memutuskan untuk memanggil FileAgent → ReportAgent.

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
1. **FileAgent** membaca file melalui MCP dan menyimpan kontennya  
2. **ReportAgent** menerima konten dan menghasilkan laporan terstruktur

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

Contoh ini memperlihatkan beberapa fitur canggih dari modul agentic. Mari kita lihat lebih dekat Agentic Scope dan Agent Listeners.

**Agentic Scope** menunjukkan memori bersama tempat agen menyimpan hasil mereka menggunakan `@Agent(outputKey="...")`. Ini memungkinkan:  
- Agen berikutnya mengakses keluaran agen sebelumnya  
- Supervisor mensintesis respons akhir  
- Anda memeriksa apa yang dihasilkan setiap agen

Diagram di bawah menunjukkan bagaimana Agentic Scope bekerja sebagai memori bersama dalam alur kerja file-ke-laporan — FileAgent menulis hasilnya di bawah kunci `fileContent`, ReportAgent membaca itu dan menulis hasilnya di bawah `report`:

<img src="../../../translated_images/id/agentic-scope.95ef488b6c1d02ef.webp" alt="Memori Bersama Agentic Scope" width="800"/>

*Agentic Scope bertindak sebagai memori bersama — FileAgent menulis `fileContent`, ReportAgent membacanya dan menulis `report`, dan kode Anda membaca hasil akhirnya.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Data file mentah dari FileAgent
String report = scope.readState("report");            // Laporan terstruktur dari ReportAgent
```
  
**Agent Listeners** memungkinkan pemantauan dan debugging eksekusi agen. Output langkah demi langkah yang Anda lihat di demo berasal dari AgentListener yang terhubung pada setiap pemanggilan agen:  
- **beforeAgentInvocation** - Dipanggil saat Supervisor memilih agen, memungkinkan Anda melihat agen mana yang dipilih dan mengapa  
- **afterAgentInvocation** - Dipanggil saat agen selesai, menunjukkan hasilnya  
- **inheritedBySubagents** - Bila true, listener memantau semua agen dalam hierarki

Diagram berikut menunjukkan siklus hidup penuh Agent Listener, termasuk bagaimana `onError` menangani kegagalan saat eksekusi agen:

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
        return true; // Menyebarkan ke semua sub-agen
    }
};
```
  
Selain pola Supervisor, modul `langchain4j-agentic` menyediakan beberapa pola alur kerja yang kuat. Diagram di bawah menunjukkan kelima pola — dari pipeline berurutan sederhana hingga alur kerja persetujuan dengan manusia dalam loop:

<img src="../../../translated_images/id/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Pola Alur Kerja Agen" width="800"/>

*Lima pola alur kerja untuk mengorkestrasi agen — dari pipeline berurutan sederhana hingga alur kerja persetujuan manusia dalam loop.*

| Pola | Deskripsi | Kasus Penggunaan |
|---------|-------------|----------|
| **Sequential** | Jalankan agen secara berurutan, keluaran mengalir ke agen berikutnya | Pipeline: riset → analisis → laporan |
| **Parallel** | Jalankan agen secara bersamaan | Tugas independen: cuaca + berita + saham |
| **Loop** | Ulangi sampai kondisi terpenuhi | Penilaian kualitas: perbaiki sampai skor ≥ 0.8 |
| **Conditional** | Arahkan berdasarkan kondisi | Klasifikasikan → arahkan ke agen spesialis |
| **Human-in-the-Loop** | Tambah titik pemeriksaan manusia | Alur kerja persetujuan, review konten |

## Konsep Kunci

Sekarang Anda telah menjelajahi MCP dan modul agentic dalam aksi, mari kita ringkas kapan menggunakan masing-masing pendekatan.

Salah satu keunggulan terbesar MCP adalah ekosistem yang berkembang. Diagram berikut menunjukkan bagaimana satu protokol universal menghubungkan aplikasi AI Anda dengan berbagai server MCP — dari akses sistem file dan database hingga GitHub, email, web scraping, dan lainnya:

<img src="../../../translated_images/id/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="Ekosistem MCP" width="800"/>

*MCP menciptakan ekosistem protokol universal — setiap server kompatibel MCP bekerja dengan setiap klien kompatibel MCP, memungkinkan berbagi alat antar aplikasi.*

**MCP** ideal ketika Anda ingin memanfaatkan ekosistem alat yang sudah ada, membangun alat yang dapat dibagikan oleh banyak aplikasi, mengintegrasikan layanan pihak ketiga dengan protokol standar, atau mengganti implementasi alat tanpa mengubah kode.

**Modul Agentic** bekerja terbaik ketika Anda menginginkan definisi agen deklaratif dengan anotasi `@Agent`, membutuhkan orkestrasi alur kerja (sekuensial, loop, paralel), lebih suka desain agen berbasis antarmuka daripada kode imperatif, atau menggabungkan beberapa agen yang berbagi keluaran melalui `outputKey`.

**Pola Agent Supervisor** bersinar ketika alur kerja tidak dapat diprediksi di muka dan Anda ingin LLM memutuskan, ketika Anda memiliki beberapa agen khusus yang butuh orkestrasi dinamis, saat membangun sistem percakapan yang mengarahkan ke kemampuan berbeda, atau saat Anda menginginkan perilaku agen yang sangat fleksibel dan adaptif.

Untuk membantu Anda memilih antara metode khusus `@Tool` dari Modul 04 dan alat MCP dari modul ini, perbandingan berikut menyoroti pertukaran utama — alat khusus memberi integrasi ketat dan keamanan tipe penuh untuk logika spesifik aplikasi, sementara alat MCP menawarkan integrasi standar yang dapat digunakan ulang:

<img src="../../../translated_images/id/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Alat Khusus vs Alat MCP" width="800"/>

*Kapan menggunakan metode @Tool khusus vs alat MCP — alat khusus untuk logika spesifik aplikasi dengan keamanan tipe penuh, alat MCP untuk integrasi standar yang bekerja lintas aplikasi.*

## Selamat!

Anda telah menyelesaikan semua lima modul dari kursus LangChain4j untuk Pemula! Berikut tampilan perjalanan pembelajaran lengkap yang telah Anda lalui — dari chat dasar hingga sistem agentic bertenaga MCP:

<img src="../../../translated_images/id/course-completion.48cd201f60ac7570.webp" alt="Penyelesaian Kursus" width="800"/>

*Perjalanan pembelajaran Anda melalui semua lima modul — dari chat dasar hingga sistem agentic bertenaga MCP.*

Anda telah menyelesaikan kursus LangChain4j untuk Pemula. Anda telah belajar:

- Cara membangun AI percakapan dengan memori (Modul 01)  
- Pola rekayasa prompt untuk berbagai tugas (Modul 02)  
- Mendasarkan respons Anda pada dokumen dengan RAG (Modul 03)  
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
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berusaha untuk akurasi, harap diperhatikan bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang sah. Untuk informasi penting, disarankan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau kesalahan interpretasi yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->