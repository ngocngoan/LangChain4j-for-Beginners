# Modul 04: Agen AI dengan Alat

## Daftar Isi

- [Apa yang Akan Anda Pelajari](../../../04-tools)
- [Prasyarat](../../../04-tools)
- [Memahami Agen AI dengan Alat](../../../04-tools)
- [Cara Kerja Panggilan Alat](../../../04-tools)
  - [Definisi Alat](../../../04-tools)
  - [Pengambilan Keputusan](../../../04-tools)
  - [Eksekusi](../../../04-tools)
  - [Generasi Respons](../../../04-tools)
  - [Arsitektur: Spring Boot Auto-Wiring](../../../04-tools)
- [Penggabungan Alat](../../../04-tools)
- [Menjalankan Aplikasi](../../../04-tools)
- [Menggunakan Aplikasi](../../../04-tools)
  - [Coba Penggunaan Alat Sederhana](../../../04-tools)
  - [Uji Penggabungan Alat](../../../04-tools)
  - [Lihat Alur Percakapan](../../../04-tools)
  - [Eksperimen dengan Permintaan Berbeda](../../../04-tools)
- [Konsep Kunci](../../../04-tools)
  - [Pola ReAct (Reasoning and Acting)](../../../04-tools)
  - [Deskripsi Alat Penting](../../../04-tools)
  - [Manajemen Sesi](../../../04-tools)
  - [Penanganan Kesalahan](../../../04-tools)
- [Alat yang Tersedia](../../../04-tools)
- [Kapan Menggunakan Agen Berbasis Alat](../../../04-tools)
- [Alat vs RAG](../../../04-tools)
- [Langkah Selanjutnya](../../../04-tools)

## Apa yang Akan Anda Pelajari

Sejauh ini, Anda telah belajar bagaimana melakukan percakapan dengan AI, menyusun prompt secara efektif, dan menghubungkan respons dengan dokumen Anda. Namun masih ada keterbatasan mendasar: model bahasa hanya bisa menghasilkan teks. Mereka tidak bisa memeriksa cuaca, melakukan perhitungan, menanyakan database, atau berinteraksi dengan sistem eksternal.

Alat mengubah ini. Dengan memberi model akses ke fungsi yang dapat dipanggil, Anda mengubahnya dari pembuat teks menjadi agen yang dapat mengambil tindakan. Model memutuskan kapan membutuhkan alat, alat mana yang dipakai, dan parameter apa yang harus diteruskan. Kode Anda mengeksekusi fungsi dan mengembalikan hasilnya. Model mengintegrasikan hasil itu dalam responsnya.

## Prasyarat

- Menyelesaikan [Modul 01 - Pengantar](../01-introduction/README.md) (sumber daya Azure OpenAI sudah disiapkan)
- Disarankan menyelesaikan modul sebelumnya (modul ini mengacu pada [konsep RAG dari Modul 03](../03-rag/README.md) dalam perbandingan Tools vs RAG)
- File `.env` di direktori root dengan kredensial Azure (dibuat oleh `azd up` di Modul 01)

> **Catatan:** Jika Anda belum menyelesaikan Modul 01, ikuti petunjuk deployment di sana terlebih dahulu.

## Memahami Agen AI dengan Alat

> **📝 Catatan:** Istilah "agen" dalam modul ini merujuk pada asisten AI yang ditingkatkan dengan kemampuan pemanggilan alat. Ini berbeda dengan pola **Agentic AI** (agen otonom dengan perencanaan, memori, dan penalaran multi-langkah) yang akan kita bahas di [Modul 05: MCP](../05-mcp/README.md).

Tanpa alat, model bahasa hanya bisa menghasilkan teks berdasarkan data latihannya. Tanya cuaca saat ini, maka ia hanya akan menebak. Beri ia alat, dan ia bisa memanggil API cuaca, melakukan perhitungan, atau menanyakan database — kemudian menyusun hasil nyata itu ke dalam responsnya.

<img src="../../../translated_images/id/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*Tanpa alat model hanya bisa menebak — dengan alat model bisa memanggil API, menjalankan perhitungan, dan mengembalikan data waktu nyata.*

Agen AI dengan alat mengikuti pola **Reasoning and Acting (ReAct)**. Model tidak hanya merespons — ia berpikir tentang apa yang dibutuhkan, bertindak dengan memanggil alat, mengamati hasilnya, lalu memutuskan apakah bertindak lagi atau memberikan jawaban akhir:

1. **Reason (Berpikir)** — Agen menganalisis pertanyaan pengguna dan menentukan informasi apa yang dibutuhkan
2. **Act (Bertindak)** — Agen memilih alat yang tepat, menghasilkan parameter yang benar, dan memanggilnya
3. **Observe (Mengamati)** — Agen menerima output alat dan mengevaluasi hasilnya
4. **Repeat or Respond (Ulangi atau Jawab)** — Jika data masih kurang, agen mengulangi siklus; jika tidak, agen menyusun jawaban dalam bahasa alami

<img src="../../../translated_images/id/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*Siklus ReAct — agen memikirkan apa yang harus dilakukan, bertindak dengan memanggil alat, mengamati hasil, dan berulang sampai bisa memberikan jawaban akhir.*

Ini terjadi secara otomatis. Anda mendefinisikan alat dan deskripsinya. Model menangani pengambilan keputusan kapan dan bagaimana menggunakannya.

## Cara Kerja Panggilan Alat

### Definisi Alat

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Anda mendefinisikan fungsi dengan deskripsi yang jelas dan spesifikasi parameter. Model melihat deskripsi ini di prompt sistemnya dan memahami apa fungsi setiap alat.

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

// Asisten secara otomatis dihubungkan oleh Spring Boot dengan:
// - Bean ChatModel
// - Semua metode @Tool dari kelas @Component
// - ChatMemoryProvider untuk manajemen sesi
```

Diagram di bawah menjelaskan setiap anotasi dan menunjukkan bagaimana tiap bagian membantu AI memahami kapan memanggil alat dan argumen apa yang harus diteruskan:

<img src="../../../translated_images/id/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*Anatomi definisi alat — @Tool memberitahu AI kapan menggunakannya, @P mendeskripsikan setiap parameter, dan @AiService menghubungkan semuanya saat startup.*

> **🤖 Coba dengan Chat [GitHub Copilot](https://github.com/features/copilot):** Buka [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) dan tanyakan:
> - "Bagaimana saya mengintegrasikan API cuaca nyata seperti OpenWeatherMap menggantikan data tiruan?"
> - "Apa yang membuat deskripsi alat yang baik sehingga membantu AI menggunakannya dengan benar?"
> - "Bagaimana cara menangani kesalahan API dan batasan rate pada implementasi alat?"

### Pengambilan Keputusan

Ketika pengguna bertanya "Apa cuaca di Seattle?", model tidak sembarang memilih alat. Ia membandingkan niat pengguna dengan setiap deskripsi alat yang tersedia, menilai relevansi masing-masing, dan memilih yang paling sesuai. Model lalu menghasilkan panggilan fungsi terstruktur dengan parameter yang tepat — dalam kasus ini, menyetel `location` ke `"Seattle"`.

Jika tidak ada alat yang cocok dengan permintaan pengguna, model kembali menjawab berdasarkan pengetahuan sendiri. Jika beberapa alat cocok, model memilih yang paling spesifik.

<img src="../../../translated_images/id/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*Model mengevaluasi setiap alat yang ada terhadap niat pengguna dan memilih yang paling tepat — ini alasan kenapa menulis deskripsi alat yang jelas dan spesifik itu penting.*

### Eksekusi

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot secara otomatis menghubungkan interface deklaratif `@AiService` dengan semua alat yang terdaftar, dan LangChain4j menjalankan pemanggilan alat secara otomatis. Di balik layar, panggilan alat lengkap melalui enam tahap — dari pertanyaan bahasa alami pengguna sampai kembali dengan jawaban bahasa alami:

<img src="../../../translated_images/id/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*Alur end-to-end — pengguna bertanya, model memilih alat, LangChain4j menjalankannya, dan model memasukkan hasilnya ke dalam respons alami.*

> **🤖 Coba dengan Chat [GitHub Copilot](https://github.com/features/copilot):** Buka [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) dan tanyakan:
> - "Bagaimana pola ReAct bekerja dan mengapa efektif bagi agen AI?"
> - "Bagaimana agen memutuskan alat mana yang digunakan dan urutan pemakaiannya?"
> - "Apa yang terjadi jika eksekusi alat gagal - bagaimana cara menangani kesalahan dengan robust?"

### Generasi Respons

Model menerima data cuaca dan memformatnya ke dalam respons bahasa alami untuk pengguna.

### Arsitektur: Spring Boot Auto-Wiring

Modul ini menggunakan integrasi LangChain4j dengan Spring Boot yang mendukung interface `@AiService` deklaratif. Saat startup, Spring Boot menemukan tiap `@Component` yang berisi metode `@Tool`, bean `ChatModel` Anda, dan `ChatMemoryProvider` — lalu menghubungkan semuanya ke dalam satu interface `Assistant` tanpa boilerplate.

<img src="../../../translated_images/id/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*Interface @AiService menggabungkan ChatModel, komponen alat, dan penyedia memori — Spring Boot mengurus semua penghubungan otomatis.*

Manfaat utama pendekatan ini:

- **Spring Boot auto-wiring** — ChatModel dan alat disuntikkan otomatis
- **Pola @MemoryId** — Manajemen memori berbasis sesi otomatis
- **Instance tunggal** — Assistant dibuat sekali dan digunakan ulang untuk performa lebih baik
- **Eksekusi tipe-aman** — Metode Java dipanggil langsung dengan konversi tipe
- **Orkestrasi multi-giliran** — Menangani penggabungan alat secara otomatis
- **Tanpa boilerplate** — Tidak perlu panggilan manual `AiServices.builder()` atau HashMap memori

Pendekatan alternatif (manual `AiServices.builder()`) membutuhkan lebih banyak kode dan kehilangan manfaat integrasi Spring Boot.

## Penggabungan Alat

**Penggabungan Alat** — Kekuatan sesungguhnya agen berbasis alat terlihat saat sebuah pertanyaan membutuhkan beberapa alat. Tanyakan "Apa cuaca di Seattle dalam Fahrenheit?" dan agen secara otomatis menggabungkan dua alat: pertama memanggil `getCurrentWeather` untuk mendapat suhu dalam Celsius, lalu meneruskan nilai itu ke `celsiusToFahrenheit` untuk konversi — semua dalam satu giliran percakapan.

<img src="../../../translated_images/id/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*Penggabungan alat sedang berjalan — agen memanggil getCurrentWeather dulu, kemudian mengalirkan hasil Celsius ke celsiusToFahrenheit, dan memberikan jawaban gabungan.*

**Penanganan Gagal Santun** — Minta cuaca di kota yang tidak ada dalam data tiruan. Alat mengembalikan pesan kesalahan, dan AI menjelaskan tidak bisa membantu daripada aplikasi crash. Alat gagal dengan aman. Diagram berikut membandingkan dua pendekatan — dengan penanganan error yang tepat, agen menangkap pengecualian dan merespons dengan penjelasan membantu, sedangkan tanpa itu aplikasi langsung crash:

<img src="../../../translated_images/id/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*Saat alat gagal, agen menangkap error dan merespons dengan penjelasan berguna, bukan crash.*

Ini terjadi dalam satu giliran percakapan. Agen mengorkestrasi pemanggilan alat berulang secara otonom.

## Menjalankan Aplikasi

**Verifikasi deployment:**

Pastikan file `.env` ada di root direktori dengan kredensial Azure (dibuat saat Modul 01). Jalankan perintah ini dari direktori modul (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**PowerShell:**
```powershell
Get-Content ..\.env  # Harus menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Mulai aplikasi:**

> **Catatan:** Jika Anda sudah memulai semua aplikasi dengan `./start-all.sh` dari root direktori (seperti di Modul 01), modul ini sudah berjalan di port 8084. Anda dapat melewati perintah mulai di bawah dan langsung buka http://localhost:8084.

**Opsi 1: Menggunakan Spring Boot Dashboard (Direkomendasikan untuk pengguna VS Code)**

Dev container menyertakan ekstensi Spring Boot Dashboard, yang menyediakan antarmuka visual untuk mengelola semua aplikasi Spring Boot. Anda dapat menemukannya di Activity Bar sebelah kiri VS Code (carilah ikon Spring Boot).

Dari Spring Boot Dashboard, Anda bisa:
- Melihat semua aplikasi Spring Boot yang tersedia di workspace
- Memulai/berhenti aplikasi dengan satu klik
- Melihat log aplikasi secara real-time
- Memantau status aplikasi

Klik tombol play di sebelah "tools" untuk memulai modul ini, atau mulai semua modul sekaligus.

Ini tampilan Spring Boot Dashboard di VS Code:

<img src="../../../translated_images/id/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard di VS Code — mulai, berhenti, dan pantau semua modul dari satu tempat*

**Opsi 2: Menggunakan skrip shell**

Mulai semua aplikasi web (modul 01-04):

**Bash:**
```bash
cd ..  # Dari direktori root
./start-all.sh
```
  
**PowerShell:**
```powershell
cd ..  # Dari direktori root
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
  
Kedua skrip otomatis memuat variabel lingkungan dari file `.env` root dan akan membangun JAR jika belum ada.

> **Catatan:** Jika Anda lebih suka membangun semua modul secara manual sebelum mulai:
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
./stop.sh  # Hanya modul ini
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

Aplikasi menyediakan antarmuka web di mana Anda dapat berinteraksi dengan agen AI yang memiliki akses ke alat cuaca dan konversi suhu. Berikut tampilan antar mukanya — termasuk contoh cepat dan panel chat untuk mengirim permintaan:
<a href="images/tools-homepage.png"><img src="../../../translated_images/id/tools-homepage.4b4cd8b2717f9621.webp" alt="Antarmuka Alat Agen AI" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Antarmuka Alat Agen AI - contoh cepat dan antarmuka obrolan untuk berinteraksi dengan alat*

### Coba Penggunaan Alat Sederhana

Mulailah dengan permintaan yang sederhana: "Ubah 100 derajat Fahrenheit ke Celsius". Agen mengenali bahwa ia memerlukan alat konversi suhu, memanggilnya dengan parameter yang tepat, dan mengembalikan hasilnya. Perhatikan betapa alaminya ini terasa - Anda tidak menentukan alat mana yang digunakan atau bagaimana memanggilnya.

### Uji Rangkaian Alat

Sekarang coba sesuatu yang lebih kompleks: "Bagaimana cuaca di Seattle dan ubah ke Fahrenheit?" Saksikan agen ini bekerja dalam beberapa langkah. Pertama ia mendapatkan informasi cuaca (yang mengembalikan dalam Celsius), kemudian mengenali bahwa ia perlu mengubah ke Fahrenheit, memanggil alat konversi, dan menggabungkan kedua hasil menjadi satu respons.

### Lihat Alur Percakapan

Antarmuka obrolan mempertahankan riwayat percakapan, memungkinkan Anda melakukan interaksi multi-tahap. Anda dapat melihat semua pertanyaan dan jawaban sebelumnya, sehingga mudah melacak percakapan dan memahami bagaimana agen membangun konteks dalam beberapa pertukaran.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/id/tools-conversation-demo.89f2ce9676080f59.webp" alt="Percakapan dengan Berbagai Panggilan Alat" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Percakapan multi-tahap yang menunjukkan konversi sederhana, pencarian cuaca, dan rangkaian alat*

### Bereksperimen dengan Permintaan Berbeda

Coba berbagai kombinasi:
- Pencarian cuaca: "Bagaimana cuaca di Tokyo?"
- Konversi suhu: "Berapa 25°C dalam Kelvin?"
- Query gabungan: "Periksa cuaca di Paris dan beri tahu saya jika di atas 20°C"

Perhatikan bagaimana agen mengartikan bahasa alami dan memetakan ke panggilan alat yang sesuai.

## Konsep Utama

### Pola ReAct (Reasoning and Acting)

Agen bergantian antara berpikir (memutuskan apa yang harus dilakukan) dan bertindak (menggunakan alat). Pola ini memungkinkan pemecahan masalah secara otonom daripada hanya merespons instruksi.

### Deskripsi Alat Penting

Kualitas deskripsi alat Anda memengaruhi seberapa baik agen menggunakan alat tersebut. Deskripsi yang jelas dan spesifik membantu model memahami kapan dan bagaimana memanggil tiap alat.

### Manajemen Sesi

Anotasi `@MemoryId` memungkinkan manajemen memori berbasis sesi secara otomatis. Setiap ID sesi mendapatkan instance `ChatMemory` sendiri yang dikelola oleh bean `ChatMemoryProvider`, sehingga banyak pengguna dapat berinteraksi dengan agen secara bersamaan tanpa percakapan mereka saling bercampur. Diagram berikut menunjukkan bagaimana banyak pengguna diarahkan ke penyimpanan memori terisolasi berdasarkan ID sesi mereka:

<img src="../../../translated_images/id/session-management.91ad819c6c89c400.webp" alt="Manajemen Sesi dengan @MemoryId" width="800"/>

*Setiap ID sesi memetakan ke riwayat percakapan yang terisolasi — pengguna tidak pernah melihat pesan satu sama lain.*

### Penanganan Kesalahan

Alat bisa gagal — API timeout, parameter mungkin tidak valid, layanan eksternal mati. Agen produksi membutuhkan penanganan kesalahan sehingga model dapat menjelaskan masalah atau mencoba alternatif daripada membuat seluruh aplikasi crash. Ketika alat melemparkan pengecualian, LangChain4j menangkapnya dan mengembalikan pesan kesalahan ke model, yang kemudian dapat menjelaskan masalah dalam bahasa alami.

## Alat yang Tersedia

Diagram di bawah ini menunjukkan ekosistem luas dari alat yang dapat Anda bangun. Modul ini menunjukkan alat cuaca dan suhu, tetapi pola `@Tool` yang sama berfungsi untuk metode Java apa pun — mulai dari query basis data hingga pemrosesan pembayaran.

<img src="../../../translated_images/id/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ekosistem Alat" width="800"/>

*Metode Java apa pun yang dianotasi dengan @Tool menjadi tersedia untuk AI — pola ini meluas ke basis data, API, email, operasi file, dan lainnya.*

## Kapan Menggunakan Agen Berbasis Alat

Tidak semua permintaan membutuhkan alat. Keputusan tergantung pada apakah AI perlu berinteraksi dengan sistem eksternal atau dapat menjawab dari pengetahuannya sendiri. Panduan berikut merangkum kapan alat memberikan nilai tambah dan kapan tidak diperlukan:

<img src="../../../translated_images/id/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Kapan Menggunakan Alat" width="800"/>

*Panduan keputusan cepat — alat untuk data real-time, perhitungan, dan tindakan; pengetahuan umum dan tugas kreatif tidak membutuhkannya.*

## Alat vs RAG

Modul 03 dan 04 keduanya memperluas apa yang dapat dilakukan AI, tetapi dengan cara fundamental yang berbeda. RAG memberikan akses model ke **pengetahuan** dengan mengambil dokumen. Alat memberi kemampuan model untuk mengambil **tindakan** dengan memanggil fungsi. Diagram berikut membandingkan kedua pendekatan ini secara berdampingan — dari cara kerja masing-masing alur hingga kelebihan dan kekurangannya:

<img src="../../../translated_images/id/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Perbandingan Alat vs RAG" width="800"/>

*RAG mengambil informasi dari dokumen statis — Alat menjalankan tindakan dan mengambil data dinamis waktu nyata. Banyak sistem produksi menggabungkan keduanya.*

Dalam praktiknya, banyak sistem produksi menggabungkan kedua pendekatan: RAG untuk mendasari jawaban berdasarkan dokumentasi Anda, dan Alat untuk mengambil data langsung atau melakukan operasi.

## Langkah Selanjutnya

**Modul Berikutnya:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 03 - RAG](../03-rag/README.md) | [Kembali ke Utama](../README.md) | [Berikutnya: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berupaya mencapai keakuratan, harap diketahui bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber otoritatif. Untuk informasi penting, disarankan menggunakan jasa terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau salah tafsir yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->