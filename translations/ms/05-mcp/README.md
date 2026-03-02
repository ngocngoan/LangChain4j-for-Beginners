# Modul 05: Protokol Konteks Model (MCP)

## Jadual Kandungan

- [Apa yang Anda Akan Pelajari](../../../05-mcp)
- [Apakah MCP?](../../../05-mcp)
- [Bagaimana MCP Berfungsi](../../../05-mcp)
- [Modul Agenik](../../../05-mcp)
- [Menjalankan Contoh-contoh](../../../05-mcp)
  - [Prasyarat](../../../05-mcp)
- [Mula dengan Pantas](../../../05-mcp)
  - [Operasi Fail (Stdio)](../../../05-mcp)
  - [Ejen Penyelia](../../../05-mcp)
    - [Menjalankan Demo](../../../05-mcp)
    - [Bagaimana Penyelia Berfungsi](../../../05-mcp)
    - [Strategi Respons](../../../05-mcp)
    - [Memahami Output](../../../05-mcp)
    - [Penjelasan Ciri Modul Agenik](../../../05-mcp)
- [Konsep Utama](../../../05-mcp)
- [Tahniah!](../../../05-mcp)
  - [Apa Seterusnya?](../../../05-mcp)

## Apa yang Anda Akan Pelajari

Anda telah membina AI perbualan, menguasai arahan, mengikat respons dalam dokumen, dan mencipta ejen dengan alat-alat. Tetapi semua alat tersebut dibina khas untuk aplikasi anda. Bagaimana jika anda boleh memberi AI anda akses kepada ekosistem alat yang distandardkan yang boleh dibuat dan dikongsi oleh sesiapa sahaja? Dalam modul ini, anda akan belajar bagaimana melakukannya dengan Protokol Konteks Model (MCP) dan modul agenik LangChain4j. Kami mula dengan menunjukkan pembaca fail MCP yang mudah dan kemudian tunjukkan bagaimana ia mudah disepadukan ke dalam aliran kerja agenik maju menggunakan corak Ejen Penyelia.

## Apakah MCP?

Protokol Konteks Model (MCP) menyediakan tepat itu - cara standard untuk aplikasi AI menemui dan menggunakan alat luaran. Daripada menulis integrasi custom untuk setiap sumber data atau perkhidmatan, anda menyambung kepada pelayan MCP yang mendedahkan keupayaan mereka dalam format yang konsisten. Ejen AI anda kemudian boleh menemui dan menggunakan alat ini secara automatik.

Rajah di bawah menunjukkan perbezaannya — tanpa MCP, setiap integrasi memerlukan sambungan satu-ke-satu custom; dengan MCP, satu protokol menghubungkan aplikasi anda ke mana-mana alat:

<img src="../../../translated_images/ms/mcp-comparison.9129a881ecf10ff5.webp" alt="Perbandingan MCP" width="800"/>

*Sebelum MCP: Integrasi titik-ke-titik yang kompleks. Selepas MCP: Satu protokol, kemungkinan tanpa batas.*

MCP menyelesaikan masalah asas dalam pembangunan AI: setiap integrasi adalah custom. Nak akses GitHub? Kod custom. Nak baca fail? Kod custom. Nak tanya pangkalan data? Kod custom. Dan tiada satu pun integrasi ini berfungsi dengan aplikasi AI lain.

MCP menstandardkan ini. Pelayan MCP mendedahkan alat dengan penerangan jelas dan skema. Mana-mana klien MCP boleh sambung, menemui alat tersedia, dan menggunakannya. Bina sekali, guna di mana-mana.

Rajah berikut menerangkan seni bina ini — satu klien MCP (aplikasi AI anda) menyambung kepada pelbagai pelayan MCP, masing-masing mendedahkan set alat mereka melalui protokol standard:

<img src="../../../translated_images/ms/mcp-architecture.b3156d787a4ceac9.webp" alt="Seni Bina MCP" width="800"/>

*Seni Bina Protokol Konteks Model - penemuan dan pelaksanaan alat distandardkan*

## Bagaimana MCP Berfungsi

Di belakang tabir, MCP menggunakan seni bina berlapis. Aplikasi Java anda (klien MCP) menemui alat yang tersedia, menghantar permintaan JSON-RPC melalui lapisan pengangkutan (Stdio atau HTTP), dan pelayan MCP melaksanakan operasi dan mengembalikan hasil. Rajah berikut memecahkan setiap lapisan protokol ini:

<img src="../../../translated_images/ms/mcp-protocol-detail.01204e056f45308b.webp" alt="Perincian Protokol MCP" width="800"/>

*Bagaimana MCP berfungsi di bawah tabir — klien menemui alat, bertukar-tukar mesej JSON-RPC, dan melaksanakan operasi melalui lapisan pengangkutan.*

**Seni Bina Pelayan-Klien**

MCP menggunakan model pelayan-klien. Pelayan menyediakan alat - membaca fail, bertanya pangkalan data, memanggil API. Klien (aplikasi AI anda) menyambung kepada pelayan dan menggunakan alat mereka.

Untuk menggunakan MCP dengan LangChain4j, tambahkan pergantungan Maven ini:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Penemuan Alat**

Apabila klien anda menyambung kepada pelayan MCP, ia bertanya "Alat apa yang awak ada?" Pelayan membalas dengan senarai alat yang tersedia, masing-masing dengan penerangan dan skema parameter. Ejen AI anda kemudian boleh memutuskan alat mana yang hendak digunakan berdasarkan permintaan pengguna. Rajah berikut menunjukkan proses jabat tangan ini — klien menghantar permintaan `tools/list` dan pelayan memulangkan alat tersedia bersama penerangan dan skema parameter mereka:

<img src="../../../translated_images/ms/tool-discovery.07760a8a301a7832.webp" alt="Penemuan Alat MCP" width="800"/>

*AI menemui alat tersedia pada permulaan — ia kini tahu keupayaan yang ada dan boleh memutuskan mana yang akan digunakan.*

**Mekanisma Pengangkutan**

MCP menyokong pelbagai mekanisma pengangkutan. Dua pilihan ialah Stdio (untuk komunikasi subprocess tempatan) dan Streamable HTTP (untuk pelayan jauh). Modul ini menunjukkan pengangkutan Stdio:

<img src="../../../translated_images/ms/transport-mechanisms.2791ba7ee93cf020.webp" alt="Mekanisma Pengangkutan" width="800"/>

*Mekanisma pengangkutan MCP: HTTP untuk pelayan jauh, Stdio untuk proses tempatan*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Untuk proses tempatan. Aplikasi anda menjanakan pelayan sebagai subprocess dan berkomunikasi melalui input/output standard. Berguna untuk akses sistem fail atau alat baris perintah.

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

Pelayan `@modelcontextprotocol/server-filesystem` mendedahkan alat berikut, semuanya di sandboxkan ke direktori yang anda tentukan:

| Alat | Penerangan |
|------|-------------|
| `read_file` | Membaca kandungan satu fail |
| `read_multiple_files` | Membaca berbilang fail dalam satu panggilan |
| `write_file` | Membuat atau timpa fail |
| `edit_file` | Membuat suntingan cari-ganti terarah |
| `list_directory` | Senaraikan fail dan direktori pada satu laluan |
| `search_files` | Cari fail secara rekursif yang padan dengan corak |
| `get_file_info` | Dapatkan metadata fail (saiz, cap masa, kebenaran) |
| `create_directory` | Buat direktori (termasuk direktori induk) |
| `move_file` | Pindah atau ubah nama fail atau direktori |

Rajah berikut menunjukkan bagaimana pengangkutan Stdio berfungsi semasa masa jalan — aplikasi Java anda menjana pelayan MCP sebagai proses anak dan mereka berkomunikasi melalui paip stdin/stdout, tanpa rangkaian atau HTTP terlibat:

<img src="../../../translated_images/ms/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Aliran Pengangkutan Stdio" width="800"/>

*Pengangkutan Stdio dalam tindakan — aplikasi anda menjana pelayan MCP sebagai proses anak dan berkomunikasi melalui paip stdin/stdout.*

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) dan tanya:
> - "Bagaimana pengangkutan Stdio berfungsi dan bila saya patut guna ia berbanding HTTP?"
> - "Bagaimana LangChain4j mengurus kitar hayat proses pelayan MCP yang dijanakan?"
> - "Apakah implikasi keselamatan memberi AI akses kepada sistem fail?"

## Modul Agenik

Walaupun MCP menyediakan alat distandardkan, modul agenik LangChain4j menyediakan cara deklaratif untuk membina ejen yang mengatur alat tersebut. Anotasi `@Agent` dan `AgenticServices` membolehkan anda mentakrifkan perilaku ejen melalui antara muka dan bukan kod imperatif.

Dalam modul ini, anda akan terokai corak **Ejen Penyelia** — pendekatan AI agenik maju di mana ejen "penyelia" membuat keputusan dinamik ejen sub mana yang dipanggil berdasarkan permintaan pengguna. Kami menggabungkan kedua-dua konsep dengan memberikan salah satu sub-ejen kami keupayaan akses fail dikuasakan MCP.

Untuk menggunakan modul agenik, tambahkan pergantungan Maven ini:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Nota:** Modul `langchain4j-agentic` menggunakan sifat versi berasingan (`langchain4j.mcp.version`) kerana ia dikeluarkan mengikut jadual berbeza berbanding perpustakaan teras LangChain4j.

> **⚠️ Eksperimen:** Modul `langchain4j-agentic` adalah **eksperimen** dan tertakluk kepada perubahan. Cara stabil membina pembantu AI masih `langchain4j-core` dengan alat custom (Modul 04).

## Menjalankan Contoh-contoh

### Prasyarat

- Menyelesaikan [Modul 04 - Alat](../04-tools/README.md) (modul ini membina konsep alat custom dan membandingkannya dengan alat MCP)
- Fail `.env` dalam direktori akar dengan kelayakan Azure (dibuat oleh `azd up` dalam Modul 01)
- Java 21+, Maven 3.9+
- Node.js 16+ dan npm (untuk pelayan MCP)

> **Nota:** Jika anda belum menyediakan pembolehubah persekitaran, lihat [Modul 01 - Pengenalan](../01-introduction/README.md) untuk arahan penyebaran (`azd up` membuat fail `.env` automatik), atau salin `.env.example` kepada `.env` dalam direktori akar dan isi nilai anda.

## Mula dengan Pantas

**Menggunakan VS Code:** Klik kanan mana-mana fail demo dalam Explorer dan pilih **"Run Java"**, atau gunakan konfigurasi pelancaran dari panel Run and Debug (pastikan fail `.env` anda telah dikonfigurasikan dengan kelayakan Azure terlebih dahulu).

**Menggunakan Maven:** Sebagai alternatif, anda boleh jalankan dari baris arahan dengan contoh di bawah.

### Operasi Fail (Stdio)

Ini menunjukkan alat berasaskan subprocess tempatan.

**✅ Tiada prasyarat diperlukan** - pelayan MCP dijanakan secara automatik.

**Menggunakan Skrip Mula (Disyorkan):**

Skrip mula secara automatik memuat pembolehubah persekitaran dari fail `.env` akar:

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

**Menggunakan VS Code:** Klik kanan pada `StdioTransportDemo.java` dan pilih **"Run Java"** (pastikan fail `.env` telah dikonfigurasikan).

Aplikasi menjana pelayan MCP sistem fail secara automatik dan membaca fail tempatan. Perhatikan bagaimana pengurusan subprocess diuruskan untuk anda.

**Output yang dijangka:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Ejen Penyelia

Corak **Ejen Penyelia** adalah bentuk AI agenik yang **fleksibel**. Penyelia menggunakan LLM untuk membuat keputusan secara autonomi ejen mana yang hendak dipanggil berdasarkan permintaan pengguna. Dalam contoh seterusnya, kami gabungkan akses fail dikuasakan MCP dengan ejen LLM untuk mencipta aliran kerja bacaan fail → laporan yang diawasi.

Dalam demo, `FileAgent` membaca fail menggunakan alat sistem fail MCP, dan `ReportAgent` menghasilkan laporan berstruktur dengan ringkasan eksekutif (1 ayat), 3 perkara utama, dan cadangan. Penyelia mengatur aliran ini secara automatik:

<img src="../../../translated_images/ms/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Corak Ejen Penyelia" width="800"/>

*Penyelia menggunakan LLMnya untuk memutuskan ejen mana yang hendak dipanggil dan dalam susunan mana — tiada pengekodan laluan statik diperlukan.*

Ini aliran kerja konkrit bagi saluran fail-ke-laporan kami:

<img src="../../../translated_images/ms/file-report-workflow.649bb7a896800de9.webp" alt="Aliran Kerja Fail ke Laporan" width="800"/>

*FileAgent membaca fail melalui alat MCP, kemudian ReportAgent menukar kandungan mentah menjadi laporan berstruktur.*

Setiap ejen menyimpan outputnya dalam **Skop Agenik** (memori kongsi), membolehkan ejen hiliran mengakses keputusan sebelumnya. Ini menunjukkan bagaimana alat MCP disepadukan dengan lancar dalam aliran kerja agenik — Penyelia tidak perlu tahu *bagaimana* fail dibaca, hanya bahawa `FileAgent` boleh melakukannya.

#### Menjalankan Demo

Skrip mula secara automatik memuat pembolehubah persekitaran dari fail `.env` akar:

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

**Menggunakan VS Code:** Klik kanan pada `SupervisorAgentDemo.java` dan pilih **"Run Java"** (pastikan fail `.env` telah dikonfigurasikan).

#### Bagaimana Penyelia Berfungsi

Sebelum membina ejen, anda perlu sambungkan pengangkutan MCP ke klien dan bungkus sebagai `ToolProvider`. Ini cara alat pelayan MCP menjadi tersedia kepada ejen anda:

```java
// Cipta klien MCP daripada pengangkut
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Balut klien sebagai PenyediaAlat — ini menghubungkan alat MCP ke dalam LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Sekarang anda boleh suntik `mcpToolProvider` ke mana-mana ejen yang memerlukan alat MCP:

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

// Penyelia mengatur aliran kerja fail → laporan
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Mengembalikan laporan akhir
        .build();

// Penyelia memutuskan agen mana yang dipanggil berdasarkan permintaan
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Strategi Respons

Apabila anda konfigurasi `SupervisorAgent`, anda nyatakan bagaimana ia harus merumuskan jawapan akhir kepada pengguna selepas sub-ejen selesai tugasan mereka. Rajah di bawah menunjukkan tiga strategi yang tersedia — LAST memulangkan output ejen terakhir secara langsung, SUMMARY mensintesis semua output melalui LLM, dan SCORED memilih mana yang mendapat markah lebih tinggi berbanding permintaan asal:

<img src="../../../translated_images/ms/response-strategies.3d0cea19d096bdf9.webp" alt="Strategi Respons" width="800"/>

*Tiga strategi bagaimana Penyelia merumuskan respons akhirnya — pilih berdasarkan sama ada anda mahu output ejen terakhir, ringkasan sintesis, atau pilihan markah terbaik.*

Strategi tersedia adalah:

| Strategi | Penerangan |
|----------|-------------|
| **LAST** | Penyelia memulangkan output sub-ejen atau alat terakhir yang dipanggil. Ini berguna apabila ejen terakhir dalam aliran kerja direka khusus untuk menghasilkan jawapan lengkap, akhir (contoh: "Ejen Ringkasan" dalam saluran penyelidikan). |
| **SUMMARY** | Penyelia menggunakan Model Bahasa (LLM) dalaman sendiri untuk mensintesis ringkasan seluruh interaksi dan semua output sub-ejen, kemudian memulangkan ringkasan itu sebagai respons akhir. Ini menyediakan jawapan yang bersih dan agregat kepada pengguna. |
| **SCORED** | Sistem menggunakan LLM dalaman untuk menilai kedua-dua respons LAST dan SUMMARY interaksi berbanding permintaan pengguna asal, memulangkan output yang mendapat skor lebih tinggi. |
Lihat [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) untuk pelaksanaan lengkap.

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) dan tanya:
> - "Bagaimana Supervisor memutuskan ejen mana yang akan dipanggil?"
> - "Apakah perbezaan antara Supervisor dan corak aliran kerja Sequential?"
> - "Bagaimana saya boleh sesuaikan tingkah laku perancangan Supervisor?"

#### Memahami Output

Apabila anda menjalankan demo, anda akan melihat satu panduan berstruktur tentang bagaimana Supervisor mengatur beberapa ejen. Ini maksud setiap bahagian:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Tajuk** memperkenalkan konsep aliran kerja: rangkaian tersusun dari pembacaan fail ke penjanaan laporan.

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
  
**Diagram Aliran Kerja** menunjukkan aliran data antara ejen. Setiap ejen mempunyai peranan khusus:
- **FileAgent** membaca fail menggunakan alat MCP dan menyimpan kandungan mentah dalam `fileContent`
- **ReportAgent** menggunakan kandungan tersebut dan menghasilkan laporan tersusun dalam `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Permintaan Pengguna** menunjukkan tugasan. Supervisor memproses ini dan memutuskan untuk memanggil FileAgent → ReportAgent.

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
  
**Pengurusan Supervisor** menunjukkan aliran 2-langkah dalam tindakan:  
1. **FileAgent** membaca fail melalui MCP dan menyimpan kandungan  
2. **ReportAgent** menerima kandungan tersebut dan menghasilkan laporan tersusun

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
  
#### Penjelasan Ciri-ciri Modul Agentic

Contoh ini menunjukkan beberapa ciri lanjutan modul agentic. Mari lihat dengan lebih dekat Agentic Scope dan Agent Listeners.

**Agentic Scope** menunjukkan memori bersama di mana ejen menyimpan keputusan mereka menggunakan `@Agent(outputKey="...")`. Ini membolehkan:  
- Ejen kemudian mengakses output ejen sebelumnya  
- Supervisor mensintesis respons akhir  
- Anda memeriksa apa yang setiap ejen hasilkan

Diagram di bawah menunjukkan bagaimana Agentic Scope berfungsi sebagai memori bersama dalam aliran kerja fail-ke-laporan — FileAgent menulis output di bawah kunci `fileContent`, ReportAgent membacanya dan menulis outputnya di bawah `report`:

<img src="../../../translated_images/ms/agentic-scope.95ef488b6c1d02ef.webp" alt="Memori Bersama Agentic Scope" width="800"/>

*Agentic Scope bertindak sebagai memori bersama — FileAgent menulis `fileContent`, ReportAgent membacanya dan menulis `report`, dan kod anda membaca hasil akhir.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Data fail mentah dari FileAgent
String report = scope.readState("report");            // Laporan berstruktur dari ReportAgent
```
  
**Agent Listeners** membolehkan pemantauan dan penyahpepijatan pelaksanaan ejen. Output langkah demi langkah yang anda lihat dalam demo datang daripada AgentListener yang mengaitkan setiap panggilan ejen:  
- **beforeAgentInvocation** - Dipanggil apabila Supervisor memilih ejen, membolehkan anda lihat ejen yang dipilih dan sebabnya  
- **afterAgentInvocation** - Dipanggil apabila ejen selesai, memaparkan hasilnya  
- **inheritedBySubagents** - Apabila benar, pendengar memantau semua ejen dalam hierarki

Diagram berikut menunjukkan kitaran hayat penuh Agent Listener, termasuk bagaimana `onError` mengendalikan kegagalan semasa pelaksanaan ejen:

<img src="../../../translated_images/ms/agent-listeners.784bfc403c80ea13.webp" alt="Kitaran Hayat Agent Listeners" width="800"/>

*Agent Listeners mengait dalam kitaran hidup pelaksanaan — memantau apabila ejen mula, selesai, atau menghadapi ralat.*

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
  
Selain corak Supervisor, modul `langchain4j-agentic` menyediakan beberapa corak aliran kerja yang berkuasa. Diagram di bawah menunjukkan semua lima — dari saluran mudah secara berurutan hingga aliran kerja kelulusan manusia-dalam-laluan:

<img src="../../../translated_images/ms/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Corak Aliran Kerja Ejen" width="800"/>

*Lima corak aliran kerja untuk mengatur ejen — dari saluran berurutan mudah hingga aliran kerja kelulusan manusia-dalam-laluan.*

| Corak | Penerangan | Kes Penggunaan |
|---------|-------------|----------|
| **Sequential** | Melaksanakan ejen mengikut urutan, output mengalir ke seterusnya | Saluran: kajian → analisis → laporan |
| **Parallel** | Menjalankan ejen secara serentak | Tugasan bebas: cuaca + berita + saham |
| **Loop** | Ulang sehingga syarat dipenuhi | Skor kualiti: haluskan sehingga skor ≥ 0.8 |
| **Conditional** | Hantar berdasarkan syarat | Klasifikasi → hantar ke ejen pakar |
| **Human-in-the-Loop** | Tambah titik semakan manusia | Aliran kerja kelulusan, semakan kandungan |

## Konsep Utama

Kini anda telah meneroka MCP dan modul agentic secara praktikal, mari kita ringkaskan bila menggunakan setiap pendekatan.

Salah satu kelebihan terbesar MCP adalah ekosistem yang berkembang. Diagram di bawah menunjukkan bagaimana satu protokol universal menghubungkan aplikasi AI anda ke pelbagai pelayan MCP — dari akses sistem fail dan pangkalan data ke GitHub, emel, web scraping, dan banyak lagi:

<img src="../../../translated_images/ms/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="Ekosistem MCP" width="800"/>

*MCP mewujudkan ekosistem protokol universal — mana-mana pelayan serasi MCP berfungsi dengan mana-mana klien serasi MCP, membolehkan perkongsian alat antara aplikasi.*

**MCP** sesuai apabila anda ingin memanfaatkan ekosistem alat sedia ada, membina alat yang boleh dikongsi oleh pelbagai aplikasi, mengintegrasi perkhidmatan pihak ketiga dengan protokol standard, atau menukar pelaksanaan alat tanpa mengubah kod.

**Modul Agentic** paling sesuai apabila anda mahukan definisi ejen secara deklaratif dengan anotasi `@Agent`, perlukan pengurusan aliran kerja (berurutan, gelung, selari), lebih suka reka bentuk ejen berasaskan antara muka berbanding kod imperatif, atau menggabungkan banyak ejen yang berkongsi output melalui `outputKey`.

**Corak Supervisor Agent** menyerlah ketika aliran kerja tidak boleh diramalkan terlebih dahulu dan anda mahu LLM membuat keputusan, apabila anda mempunyai banyak ejen khusus yang memerlukan pengurusan dinamik, ketika membina sistem perbualan yang menghala ke pelbagai keupayaan, atau apabila anda mahukan tingkah laku ejen yang paling fleksibel dan adaptif.

Untuk membantu anda memilih antara kaedah `@Tool` khusus dari Modul 04 dan alat MCP dari modul ini, perbandingan berikut menyerlahkan pertukaran utama — alat khusus memberikan pengikatan rapat dan keselamatan jenis penuh untuk logik khusus aplikasi, manakala alat MCP menawarkan integrasi piawai dan boleh digunakan semula:

<img src="../../../translated_images/ms/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Alat Khusus vs Alat MCP" width="800"/>

*Bila menggunakan kaedah @Tool khusus vs alat MCP — alat khusus untuk logik aplikasi dengan keselamatan jenis penuh, alat MCP untuk integrasi piawai yang berfungsi merentas aplikasi.*

## Tahniah!

Anda telah menamatkan kesemua lima modul kursus LangChain4j untuk Pemula! Berikut adalah gambaran perjalanan pembelajaran penuh yang telah anda lengkapkan — dari sembang asas hingga sistem agentic berkuasa MCP:

<img src="../../../translated_images/ms/course-completion.48cd201f60ac7570.webp" alt="Penamat Kursus" width="800"/>

*Perjalanan pembelajaran anda melalui semua lima modul — dari sembang asas hingga sistem agentic berkuasa MCP.*

Anda telah menamatkan kursus LangChain4j untuk Pemula. Anda telah belajar:

- Cara membina AI perbualan dengan memori (Modul 01)  
- Corak kejuruteraan prompt untuk pelbagai tugasan (Modul 02)  
- Membumikan respons dalam dokumen anda dengan RAG (Modul 03)  
- Membuat ejen AI asas (penolong) dengan alat khusus (Modul 04)  
- Mengintegrasi alat piawai dengan modul LangChain4j MCP dan Agentic (Modul 05)

### Apa Selepas Ini?

Selepas menamatkan modul, terokai [Panduan Ujian](../docs/TESTING.md) untuk melihat konsep ujian LangChain4j secara praktikal.

**Sumber Rasmi:**  
- [Dokumentasi LangChain4j](https://docs.langchain4j.dev/) - Panduan lengkap dan rujukan API  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Kod sumber dan contoh  
- [Tutorial LangChain4j](https://docs.langchain4j.dev/tutorials/) - Tutorial langkah demi langkah untuk pelbagai kes penggunaan

Terima kasih kerana menamatkan kursus ini!

---

**Navigasi:** [← Sebelumnya: Modul 04 - Alat](../04-tools/README.md) | [Kembali ke Utama](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk mendapatkan ketepatan, sila maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang sahih. Untuk maklumat penting, terjemahan oleh penterjemah manusia profesional adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang berlaku daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->