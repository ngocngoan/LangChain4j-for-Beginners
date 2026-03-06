# Modul 04: Agen AI dengan Alat

## Daftar Isi

- [Apa yang Akan Anda Pelajari](../../../04-tools)
- [Prasyarat](../../../04-tools)
- [Memahami Agen AI dengan Alat](../../../04-tools)
- [Cara Kerja Pemanggilan Alat](../../../04-tools)
  - [Definisi Alat](../../../04-tools)
  - [Pengambilan Keputusan](../../../04-tools)
  - [Eksekusi](../../../04-tools)
  - [Generasi Respons](../../../04-tools)
  - [Arsitektur: Spring Boot Auto-Wiring](../../../04-tools)
- [Pengaitan Alat](../../../04-tools)
- [Jalankan Aplikasi](../../../04-tools)
- [Menggunakan Aplikasi](../../../04-tools)
  - [Coba Penggunaan Alat Sederhana](../../../04-tools)
  - [Uji Pengaitan Alat](../../../04-tools)
  - [Lihat Alur Percakapan](../../../04-tools)
  - [Eksperimen dengan Permintaan Berbeda](../../../04-tools)
- [Konsep Kunci](../../../04-tools)
  - [Polanya ReAct (Reasoning and Acting)](../../../04-tools)
  - [Deskripsi Alat Penting](../../../04-tools)
  - [Manajemen Sesi](../../../04-tools)
  - [Penanganan Kesalahan](../../../04-tools)
- [Alat yang Tersedia](../../../04-tools)
- [Kapan Menggunakan Agen Berbasis Alat](../../../04-tools)
- [Alat vs RAG](../../../04-tools)
- [Langkah Selanjutnya](../../../04-tools)

## Apa yang Akan Anda Pelajari

Sejauh ini, Anda telah belajar bagaimana melakukan percakapan dengan AI, menyusun prompt secara efektif, dan mendasarkan respons pada dokumen Anda. Namun masih ada keterbatasan mendasar: model bahasa hanya dapat menghasilkan teks. Mereka tidak bisa memeriksa cuaca, melakukan perhitungan, menanyakan database, atau berinteraksi dengan sistem eksternal.

Alat mengubah hal ini. Dengan memberikan model akses ke fungsi yang dapat dipanggilnya, Anda mengubahnya dari generator teks menjadi agen yang dapat mengambil tindakan. Model memutuskan kapan membutuhkan alat, alat mana yang akan digunakan, dan parameter apa yang perlu diberikan. Kode Anda menjalankan fungsi tersebut dan mengembalikan hasilnya. Model memasukkan hasil itu ke dalam responsnya.

## Prasyarat

- Selesai [Modul 01 - Pengantar](../01-introduction/README.md) (sumber daya Azure OpenAI sudah dideploy)
- Disarankan menyelesaikan modul sebelumnya (modul ini merujuk pada [konsep RAG dari Modul 03](../03-rag/README.md) dalam perbandingan Tools vs RAG)
- File `.env` di direktori root dengan kredensial Azure (dibuat dengan `azd up` di Modul 01)

> **Catatan:** Jika Anda belum menyelesaikan Modul 01, ikuti petunjuk deployment di sana terlebih dahulu.

## Memahami Agen AI dengan Alat

> **📝 Catatan:** Istilah "agen" dalam modul ini merujuk pada asisten AI yang ditingkatkan dengan kemampuan pemanggilan alat. Ini berbeda dari pola **Agentic AI** (agen otonom dengan perencanaan, memori, dan penalaran multi-langkah) yang akan kita bahas di [Modul 05: MCP](../05-mcp/README.md).

Tanpa alat, model bahasa hanya dapat menghasilkan teks dari data latihannya. Tanyakan cuaca saat ini, dan dia harus menebak. Berikan alat, dan dia dapat memanggil API cuaca, melakukan perhitungan, atau menanyakan database — lalu menganyam hasil nyata itu ke dalam responsnya.

<img src="../../../translated_images/id/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*Tanpa alat model hanya dapat menebak — dengan alat dapat memanggil API, menjalankan perhitungan, dan mengembalikan data waktu nyata.*

Agen AI dengan alat mengikuti pola **Reasoning and Acting (ReAct)**. Model tidak hanya merespons — ia memikirkan apa yang dibutuhkannya, bertindak dengan memanggil alat, mengamati hasil, lalu memutuskan apakah harus bertindak lagi atau memberikan jawaban akhir:

1. **Reason** — Agen menganalisis pertanyaan pengguna dan menentukan informasi yang dibutuhkannya
2. **Act** — Agen memilih alat yang tepat, membuat parameter yang benar, dan memanggilnya
3. **Observe** — Agen menerima output alat dan mengevaluasi hasilnya
4. **Repeat or Respond** — Jika data lebih dibutuhkan, agen berulang; jika tidak, menyusun jawaban dengan bahasa alami

<img src="../../../translated_images/id/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*Siklus ReAct — agen merenungkan apa yang harus dilakukan, bertindak dengan memanggil alat, mengamati hasil, dan mengulang sampai dapat memberikan jawaban akhir.*

Ini terjadi secara otomatis. Anda mendefinisikan alat dan deskripsinya. Model menangani pengambilan keputusan kapan dan bagaimana cara menggunakannya.

## Cara Kerja Pemanggilan Alat

### Definisi Alat

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Anda mendefinisikan fungsi dengan deskripsi jelas dan spesifikasi parameter. Model melihat deskripsi ini dalam prompt sistemnya dan memahami apa fungsi masing-masing alat.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Logika pencarian cuaca Anda
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Asisten secara otomatis terhubung oleh Spring Boot dengan:
// - Bean ChatModel
// - Semua metode @Tool dari kelas @Component
// - ChatMemoryProvider untuk manajemen sesi
```

Diagram di bawah memecah setiap anotasi dan menunjukkan bagaimana setiap bagian membantu AI memahami kapan memanggil alat dan argumen apa yang harus diberikan:

<img src="../../../translated_images/id/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*Anatomi definisi alat — @Tool memberitahu AI kapan menggunakannya, @P mendeskripsikan tiap parameter, dan @AiService menghubungkan semuanya saat startup.*

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) dan tanyakan:
> - "Bagaimana saya mengintegrasikan API cuaca nyata seperti OpenWeatherMap menggantikan data contoh?"
> - "Apa yang membuat deskripsi alat yang baik sehingga membantu AI menggunakannya dengan benar?"
> - "Bagaimana saya menangani kesalahan API dan batasan kuota dalam implementasi alat?"

### Pengambilan Keputusan

Ketika pengguna bertanya "Bagaimana cuaca di Seattle?", model tidak memilih alat secara acak. Ia membandingkan maksud pengguna dengan setiap deskripsi alat yang ia miliki, memberi skor relevansi, dan memilih yang paling sesuai. Kemudian menghasilkan panggilan fungsi terstruktur dengan parameter yang tepat — dalam kasus ini, menetapkan `location` ke `"Seattle"`.

Jika tidak ada alat yang sesuai dengan permintaan pengguna, model kembali menjawab berdasarkan pengetahuannya sendiri. Jika beberapa alat cocok, ia memilih yang paling spesifik.

<img src="../../../translated_images/id/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*Model mengevaluasi setiap alat yang tersedia terhadap maksud pengguna dan memilih yang paling cocok — inilah alasan mengapa menulis deskripsi alat yang jelas dan spesifik sangat penting.*

### Eksekusi

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot menghubungkan secara otomatis interface deklaratif `@AiService` dengan semua alat yang terdaftar, dan LangChain4j menjalankan panggilan alat secara otomatis. Di balik layar, panggilan alat lengkap mengalir melalui enam tahap — dari pertanyaan bahasa alami pengguna sampai kembali menjadi jawaban bahasa alami:

<img src="../../../translated_images/id/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*Alur ujung-ke-ujung — pengguna mengajukan pertanyaan, model memilih alat, LangChain4j menjalankannya, dan model menyisipkan hasilnya ke dalam respons alami.*

Jika Anda menjalankan [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) di Modul 00, Anda sudah melihat pola ini — alat `Calculator` juga dipanggil dengan cara yang sama. Diagram urutan di bawah menunjukkan persis apa yang terjadi di balik layar selama demo tersebut:

<img src="../../../translated_images/id/tool-calling-sequence.94802f406ca26278.webp" alt="Tool Calling Sequence Diagram" width="800"/>

*Siklus pemanggilan alat dari demo Quick Start — `AiServices` mengirim pesan dan skema alat ke LLM, LLM menjawab dengan panggilan fungsi seperti `add(42, 58)`, LangChain4j menjalankan metode `Calculator` secara lokal, dan memberikan hasilnya kembali untuk jawaban akhir.*

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) dan tanyakan:
> - "Bagaimana pola ReAct bekerja dan mengapa efektif untuk agen AI?"
> - "Bagaimana agen memutuskan alat mana yang digunakan dan dalam urutan apa?"
> - "Apa yang terjadi jika eksekusi alat gagal - bagaimana cara menangani kesalahan dengan baik?"

### Generasi Respons

Model menerima data cuaca dan memformatnya menjadi respons bahasa alami untuk pengguna.

### Arsitektur: Spring Boot Auto-Wiring

Modul ini menggunakan integrasi LangChain4j dengan Spring Boot melalui interface deklaratif `@AiService`. Saat startup, Spring Boot menemukan setiap `@Component` yang mengandung metode `@Tool`, bean `ChatModel` Anda, dan `ChatMemoryProvider` — lalu menghubungkan semuanya ke interface `Assistant` tunggal tanpa boilerplate.

<img src="../../../translated_images/id/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*Interface @AiService menghubungkan ChatModel, komponen alat, dan penyedia memori — Spring Boot mengatur semua wiring secara otomatis.*

Berikut siklus hidup permintaan lengkap dalam diagram urutan — dari permintaan HTTP melalui controller, service, dan proxy auto-wired, hingga eksekusi alat dan balik kembali:

<img src="../../../translated_images/id/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Spring Boot Tool Calling Sequence" width="800"/>

*Siklus hidup permintaan Spring Boot lengkap — permintaan HTTP mengalir lewat controller dan service ke proxy Assistant yang auto-wired, yang mengorkestrasi LLM dan panggilan alat secara otomatis.*

Manfaat utama pendekatan ini:

- **Spring Boot auto-wiring** — ChatModel dan alat otomatis disuntikkan
- **Pola @MemoryId** — Manajemen memori berbasis sesi otomatis
- **Instansi tunggal** — Assistant dibuat sekali dan digunakan ulang untuk performa lebih baik
- **Eksekusi tipe-aman** — Metode Java dipanggil langsung dengan konversi tipe
- **Orkestrasi multi-putar** — Menangani pengaitan alat secara otomatis
- **Nol boilerplate** — Tanpa pemanggilan manual `AiServices.builder()` atau HashMap memori

Pendekatan alternatif (manual `AiServices.builder()`) membutuhkan lebih banyak kode dan kehilangan manfaat integrasi Spring Boot.

## Pengaitan Alat

**Pengaitan Alat** — Kekuatan sebenarnya dari agen berbasis alat terlihat saat satu pertanyaan membutuhkan beberapa alat. Tanyakan "Bagaimana cuaca di Seattle dalam Fahrenheit?" dan agen secara otomatis mengaitkan dua alat: pertama memanggil `getCurrentWeather` untuk mendapatkan suhu dalam Celsius, kemudian mengoper nilai itu ke `celsiusToFahrenheit` untuk konversi — semua dalam satu putaran percakapan.

<img src="../../../translated_images/id/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*Pengaitan alat dalam aksi — agen memanggil getCurrentWeather dulu, lalu mengalirkan hasil Celsius ke celsiusToFahrenheit, dan memberikan jawaban gabungan.*

**Kegagalan yang Sopan** — Minta cuaca di kota yang tidak ada di data contoh. Alat mengembalikan pesan kesalahan, dan AI menjelaskan bahwa ia tidak dapat membantu daripada crash. Alat gagal dengan aman. Diagram di bawah membandingkan dua pendekatan — dengan penanganan kesalahan yang tepat, agen menangkap pengecualian dan merespons dengan penjelasan yang membantu, sedangkan tanpa itu seluruh aplikasi crash:

<img src="../../../translated_images/id/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*Ketika sebuah alat gagal, agen menangkap kesalahan dan merespons dengan penjelasan yang membantu, bukan crash.*

Ini terjadi dalam satu putaran percakapan. Agen mengorkestrasi beberapa panggilan alat secara otomatis.

## Jalankan Aplikasi

**Verifikasi deployment:**

Pastikan file `.env` ada di direktori root dengan kredensial Azure (dibuat selama Modul 01). Jalankan ini dari direktori modul (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Mulai aplikasi:**

> **Catatan:** Jika Anda sudah memulai semua aplikasi menggunakan `./start-all.sh` dari direktori root (seperti yang dijelaskan di Modul 01), modul ini sudah berjalan di port 8084. Anda bisa melewati perintah start di bawah dan langsung ke http://localhost:8084.

**Opsi 1: Menggunakan Spring Boot Dashboard (Disarankan untuk pengguna VS Code)**

Dev container menyertakan ekstensi Spring Boot Dashboard, yang menyediakan antarmuka visual untuk mengelola semua aplikasi Spring Boot. Anda dapat menemukannya di Activity Bar di sisi kiri VS Code (cari ikon Spring Boot).

Dari Spring Boot Dashboard, Anda dapat:
- Melihat semua aplikasi Spring Boot yang tersedia dalam workspace
- Memulai/menghentikan aplikasi dengan satu klik
- Melihat log aplikasi secara real-time
- Memantau status aplikasi

Cukup klik tombol play di sebelah "tools" untuk memulai modul ini, atau mulai semua modul sekaligus.

Inilah tampilan Spring Boot Dashboard di VS Code:

<img src="../../../translated_images/id/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard di VS Code — mulai, hentikan, dan pantau semua modul dari satu tempat*

**Opsi 2: Menggunakan skrip shell**

Mulai semua aplikasi web (modul 01-04):

**Bash:**
```bash
cd ..  # Dari direktori root
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Dari direktori akar
.\start-all.ps1
```

Atau mulai hanya modul ini:

**Bash:**
```bash
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

Kedua skrip secara otomatis memuat variabel lingkungan dari file `.env` root dan akan membangun JAR jika belum ada.

> **Catatan:** Jika Anda lebih suka membangun semua modul secara manual sebelum memulai:
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

Buka http://localhost:8084 di browser Anda.

**Untuk berhenti:**

**Bash:**
```bash
./stop.sh  # Modul ini saja
# Atau
cd .. && ./stop-all.sh  # Semua modul
```

**PowerShell:**
```powershell
.\stop.ps1  # Hanya modul ini
# Atau
cd ..; .\stop-all.ps1  # Semua modul
```

## Menggunakan Aplikasi

Aplikasi menyediakan antarmuka web tempat Anda dapat berinteraksi dengan agen AI yang memiliki akses ke alat cuaca dan konversi suhu. Berikut tampilan antarmukanya — mencakup contoh penggunaan cepat dan panel chat untuk mengirim permintaan:

<a href="images/tools-homepage.png"><img src="../../../translated_images/id/tools-homepage.4b4cd8b2717f9621.webp" alt="Antarmuka Alat Agen AI" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Antarmuka Alat Agen AI - contoh cepat dan antarmuka chat untuk berinteraksi dengan alat*

### Coba Penggunaan Alat Sederhana

Mulai dengan permintaan sederhana: "Konversikan 100 derajat Fahrenheit ke Celsius". Agen mengenali bahwa ia membutuhkan alat konversi suhu, memanggilnya dengan parameter yang tepat, dan mengembalikan hasilnya. Perhatikan betapa naturalnya ini terasa - Anda tidak perlu menentukan alat mana yang digunakan atau bagaimana memanggilnya.

### Uji Rangkaian Alat

Sekarang coba sesuatu yang lebih kompleks: "Bagaimana cuaca di Seattle dan konversikan ke Fahrenheit?" Saksikan agen bekerja melalui langkah-langkah ini. Pertama ia mendapatkan cuaca (yang mengembalikan dalam Celsius), mengenali bahwa ia perlu mengonversi ke Fahrenheit, memanggil alat konversi, dan menggabungkan kedua hasil tersebut menjadi satu respons.

### Lihat Alur Percakapan

Antarmuka chat menyimpan riwayat percakapan, memungkinkan Anda melakukan interaksi berulang kali. Anda bisa melihat semua kueri dan respons sebelumnya, memudahkan untuk melacak percakapan dan memahami bagaimana agen membangun konteks selama beberapa pertukaran.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/id/tools-conversation-demo.89f2ce9676080f59.webp" alt="Percakapan dengan Banyak Pemanggilan Alat" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Percakapan multi-giliran yang menunjukkan konversi sederhana, pencarian cuaca, dan rangkaian alat*

### Bereksperimen dengan Permintaan Berbeda

Coba berbagai kombinasi:
- Pencarian cuaca: "Bagaimana cuaca di Tokyo?"
- Konversi suhu: "Berapa 25°C dalam Kelvin?"
- Kuiri gabungan: "Cek cuaca di Paris dan beri tahu jika suhunya di atas 20°C"

Perhatikan bagaimana agen menginterpretasikan bahasa alami dan memetakannya ke panggilan alat yang tepat.

## Konsep Utama

### Pola ReAct (Reasoning dan Acting)

Agen bergantian antara bernalar (memutuskan apa yang harus dilakukan) dan bertindak (menggunakan alat). Pola ini memungkinkan penyelesaian masalah secara mandiri, bukan hanya merespon instruksi.

### Deskripsi Alat Penting

Kualitas deskripsi alat Anda secara langsung memengaruhi seberapa baik agen menggunakannya. Deskripsi yang jelas dan spesifik membantu model memahami kapan dan bagaimana memanggil setiap alat.

### Manajemen Sesi

Anotasi `@MemoryId` memungkinkan manajemen memori berbasis sesi secara otomatis. Setiap ID sesi mendapatkan instance `ChatMemory` sendiri yang dikelola oleh bean `ChatMemoryProvider`, sehingga banyak pengguna dapat berinteraksi dengan agen secara bersamaan tanpa percakapan mereka tercampur. Diagram berikut menunjukkan bagaimana beberapa pengguna dialihkan ke penyimpanan memori terisolasi berdasarkan ID sesi mereka:

<img src="../../../translated_images/id/session-management.91ad819c6c89c400.webp" alt="Manajemen Sesi dengan @MemoryId" width="800"/>

*Setiap ID sesi memetakan ke riwayat percakapan terisolasi — pengguna tidak pernah melihat pesan satu sama lain.*

### Penanganan Error

Alat bisa gagal — API bisa timeout, parameter mungkin tidak valid, layanan eksternal bisa turun. Agen produksi butuh penanganan error agar model dapat menjelaskan masalah atau mencoba alternatif daripada membuat aplikasi keseluruhan crash. Saat alat melemparkan pengecualian, LangChain4j menangkapnya dan mengirimkan pesan error kembali ke model, yang kemudian dapat menjelaskan masalah tersebut dalam bahasa alami.

## Alat yang Tersedia

Diagram di bawah menunjukkan ekosistem luas alat yang bisa Anda bangun. Modul ini mendemonstrasikan alat cuaca dan suhu, tapi pola `@Tool` yang sama bekerja untuk metode Java apapun — dari kueri database hingga pemrosesan pembayaran.

<img src="../../../translated_images/id/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ekosistem Alat" width="800"/>

*Setiap metode Java yang dianotasi dengan @Tool menjadi tersedia untuk AI — pola ini meluas ke database, API, email, operasi berkas, dan lainnya.*

## Kapan Menggunakan Agen Berbasis Alat

Tidak setiap permintaan membutuhkan alat. Keputusan bergantung pada apakah AI perlu berinteraksi dengan sistem eksternal atau dapat menjawab berdasarkan pengetahuannya sendiri. Panduan berikut merangkum kapan alat menambah nilai dan kapan tidak diperlukan:

<img src="../../../translated_images/id/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Kapan Menggunakan Alat" width="800"/>

*Panduan keputusan cepat — alat digunakan untuk data waktu nyata, perhitungan, dan tindakan; pengetahuan umum dan tugas kreatif tidak memerlukannya.*

## Alat vs RAG

Modul 03 dan 04 keduanya memperluas apa yang bisa dilakukan AI, tetapi dengan cara yang sangat berbeda. RAG memberi akses model ke **pengetahuan** dengan mengambil dokumen. Alat memberi model kemampuan melakukan **tindakan** dengan memanggil fungsi. Diagram di bawah membandingkan kedua pendekatan ini berdampingan — dari cara setiap alur kerja beroperasi hingga pertukaran antara keduanya:

<img src="../../../translated_images/id/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Perbandingan Alat vs RAG" width="800"/>

*RAG mengambil informasi dari dokumen statis — Alat menjalankan tindakan dan mengambil data dinamis waktu nyata. Banyak sistem produksi menggabungkan keduanya.*

Dalam praktiknya, banyak sistem produksi menggunakan kedua pendekatan: RAG untuk memberi dasar jawaban dari dokumentasi Anda, dan Alat untuk mengambil data langsung atau melakukan operasi.

## Langkah Berikutnya

**Modul Berikutnya:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 03 - RAG](../03-rag/README.md) | [Kembali ke Utama](../README.md) | [Berikutnya: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berusaha untuk memberikan terjemahan yang akurat, harap diketahui bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang sahih. Untuk informasi penting, disarankan menggunakan terjemahan profesional oleh penerjemah manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau penafsiran yang mungkin timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->