# Modul 04: Agen AI dengan Alat

## Daftar Isi

- [Apa yang Akan Anda Pelajari](../../../04-tools)
- [Prasyarat](../../../04-tools)
- [Memahami Agen AI dengan Alat](../../../04-tools)
- [Bagaimana Pemanggilan Alat Bekerja](../../../04-tools)
  - [Definisi Alat](../../../04-tools)
  - [Pengambilan Keputusan](../../../04-tools)
  - [Eksekusi](../../../04-tools)
  - [Pembuatan Respons](../../../04-tools)
  - [Arsitektur: Spring Boot Auto-Wiring](../../../04-tools)
- [Penghubungan Alat](../../../04-tools)
- [Menjalankan Aplikasi](../../../04-tools)
- [Menggunakan Aplikasi](../../../04-tools)
  - [Coba Penggunaan Alat Sederhana](../../../04-tools)
  - [Uji Penghubungan Alat](../../../04-tools)
  - [Lihat Alur Percakapan](../../../04-tools)
  - [Bereksperimen dengan Permintaan Berbeda](../../../04-tools)
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

Sejauh ini, Anda telah belajar bagaimana berbicara dengan AI, menyusun prompt secara efektif, dan mendasarkan respons pada dokumen Anda. Namun masih ada keterbatasan mendasar: model bahasa hanya bisa menghasilkan teks. Mereka tidak dapat memeriksa cuaca, melakukan perhitungan, membuka database, atau berinteraksi dengan sistem eksternal.

Alat mengubah ini. Dengan memberi model akses ke fungsi yang bisa dipanggilnya, Anda mengubahnya dari penghasil teks menjadi agen yang dapat mengambil tindakan. Model memutuskan kapan ia butuh alat, alat mana yang digunakan, dan parameter apa yang diberikan. Kode Anda mengeksekusi fungsi itu dan mengembalikan hasilnya. Model memasukkan hasil itu ke dalam responsnya.

## Prasyarat

- Menyelesaikan Modul 01 (sumber daya Azure OpenAI sudah dideploy)
- File `.env` di direktori root dengan kredensial Azure (dibuat oleh `azd up` di Modul 01)

> **Catatan:** Jika Anda belum menyelesaikan Modul 01, ikuti dulu instruksi deployment di sana.

## Memahami Agen AI dengan Alat

> **📝 Catatan:** Istilah "agen" dalam modul ini merujuk pada asisten AI yang diperkaya dengan kemampuan pemanggilan alat. Ini berbeda dengan pola **Agentic AI** (agen otonom dengan perencanaan, memori, dan penalaran multi-langkah) yang akan kita bahas di [Modul 05: MCP](../05-mcp/README.md).

Tanpa alat, model bahasa hanya bisa menghasilkan teks dari data latihannya. Tanyakan cuaca saat ini, dan ia harus menebak. Berikan alat, dan ia dapat memanggil API cuaca, melakukan perhitungan, atau membuka database — lalu menggabungkan hasil nyata itu dalam responnya.

<img src="../../../translated_images/id/what-are-tools.724e468fc4de64da.webp" alt="Tanpa Alat vs Dengan Alat" width="800"/>

*Tanpa alat model hanya bisa menebak — dengan alat ia bisa memanggil API, menjalankan perhitungan, dan mengembalikan data real-time.*

Agen AI dengan alat mengikuti pola **Reasoning and Acting (ReAct)**. Model tidak hanya merespons — ia memikirkan apa yang dibutuhkannya, bertindak dengan memanggil alat, mengamati hasil, lalu memutuskan apakah akan bertindak lagi atau memberikan jawaban akhir:

1. **Reason** — Agen menganalisis pertanyaan pengguna dan menentukan informasi apa yang dibutuhkan
2. **Act** — Agen memilih alat yang tepat, membuat parameter yang benar, dan memanggilnya
3. **Observe** — Agen menerima output alat dan mengevaluasi hasilnya
4. **Repeat or Respond** — Jika data lebih diperlukan, agen mengulang; jika tidak, ia merangkai jawaban dalam bahasa alami

<img src="../../../translated_images/id/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Pola ReAct" width="800"/>

*Siklus ReAct — agen mempertimbangkan apa yang harus dilakukan, bertindak dengan memanggil alat, mengamatinya, dan mengulang sampai bisa memberikan jawaban akhir.*

Ini terjadi secara otomatis. Anda mendefinisikan alat dan deskripsinya. Model menangani keputusan kapan dan bagaimana menggunakannya.

## Bagaimana Pemanggilan Alat Bekerja

### Definisi Alat

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Anda mendefinisikan fungsi dengan deskripsi yang jelas dan spesifikasi parameter. Model melihat deskripsi ini dalam system prompt-nya dan mengerti apa fungsi setiap alat.

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

Diagram di bawah ini menjelaskan setiap anotasi dan menunjukkan bagaimana setiap bagian membantu AI memahami kapan memanggil alat dan argumen apa yang harus diberikan:

<img src="../../../translated_images/id/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomi Definisi Alat" width="800"/>

*Anatomi definisi alat — @Tool memberitahu AI kapan menggunakannya, @P mendeskripsikan setiap parameter, dan @AiService menghubungkan semuanya saat startup.*

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) dan tanyakan:
> - "Bagaimana saya mengintegrasikan API cuaca nyata seperti OpenWeatherMap menggantikan data palsu?"
> - "Apa yang membuat deskripsi alat bagus sehingga membantu AI menggunakannya dengan benar?"
> - "Bagaimana menangani kesalahan API dan batasan rate dalam implementasi alat?"

### Pengambilan Keputusan

Saat pengguna bertanya "Bagaimana cuaca di Seattle?", model tidak memilih alat secara acak. Ia membandingkan niat pengguna dengan deskripsi setiap alat yang dimilikinya, memberi skor relevansi, dan memilih yang paling cocok. Lalu ia membuat panggilan fungsi terstruktur dengan parameter yang benar — dalam kasus ini, menetapkan `location` ke `"Seattle"`.

Jika tidak ada alat yang cocok dengan permintaan pengguna, model menjawab berdasarkan pengetahuannya sendiri. Jika ada beberapa alat yang cocok, ia memilih yang paling spesifik.

<img src="../../../translated_images/id/decision-making.409cd562e5cecc49.webp" alt="Bagaimana AI Memutuskan Alat Mana yang Digunakan" width="800"/>

*Model mengevaluasi setiap alat yang tersedia terhadap niat pengguna dan memilih yang paling cocok — ini sebabnya penting menulis deskripsi alat yang jelas dan spesifik.*

### Eksekusi

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot secara otomatis menghubungkan interface deklaratif `@AiService` dengan semua alat yang terdaftar, dan LangChain4j mengeksekusi panggilan alat secara otomatis. Di balik layar, panggilan alat lengkap mengalir melalui enam tahap — dari pertanyaan bahasa alami pengguna hingga kembali ke jawaban bahasa alami:

<img src="../../../translated_images/id/tool-calling-flow.8601941b0ca041e6.webp" alt="Alur Pemanggilan Alat" width="800"/>

*Alur menyeluruh — pengguna mengajukan pertanyaan, model memilih alat, LangChain4j mengeksekusi, dan model menggabungkan hasil ke dalam respons natural.*

> **🤖 Coba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) dan tanyakan:
> - "Bagaimana pola ReAct bekerja dan mengapa efektif untuk agen AI?"
> - "Bagaimana agen memutuskan alat mana yang digunakan dan dalam urutan apa?"
> - "Apa yang terjadi jika eksekusi alat gagal - bagaimana saya harus menangani kesalahan secara kuat?"

### Pembuatan Respons

Model menerima data cuaca dan memformatnya menjadi respons bahasa alami untuk pengguna.

### Arsitektur: Spring Boot Auto-Wiring

Modul ini menggunakan integrasi LangChain4j dengan Spring Boot memakai interface deklaratif `@AiService`. Saat startup, Spring Boot mendeteksi setiap `@Component` yang memiliki metode `@Tool`, bean `ChatModel` Anda, dan `ChatMemoryProvider` — lalu menghubungkan semuanya ke satu interface `Assistant` tanpa kode boilerplate.

<img src="../../../translated_images/id/spring-boot-wiring.151321795988b04e.webp" alt="Arsitektur Spring Boot Auto-Wiring" width="800"/>

*Interface @AiService menggabungkan ChatModel, komponen alat, dan penyedia memori — Spring Boot mengatur semua wiring secara otomatis.*

Manfaat utama pendekatan ini:

- **Spring Boot auto-wiring** — ChatModel dan alat di-inject otomatis
- **Pola @MemoryId** — Manajemen memori berbasis sesi otomatis
- **Instance tunggal** — Assistant dibuat sekali dan digunakan ulang untuk performa lebih baik
- **Eksekusi tipe-aman** — Metode Java dipanggil langsung dengan konversi tipe
- **Orkestrasi multi-turn** — Menangani penghubungan alat otomatis
- **Tanpa boilerplate** — Tidak perlu panggilan manual `AiServices.builder()` atau HashMap memori

Pendekatan alternatif (manual `AiServices.builder()`) membutuhkan lebih banyak kode dan kehilangan manfaat integrasi Spring Boot.

## Penghubungan Alat

**Penghubungan Alat** — Kekuatan sesungguhnya dari agen berbasis alat terlihat ketika satu pertanyaan memerlukan banyak alat. Tanyakan "Bagaimana cuaca di Seattle dalam Fahrenheit?" dan agen secara otomatis menghubungkan dua alat: pertama memanggil `getCurrentWeather` untuk mendapatkan suhu dalam Celsius, lalu meneruskan nilai itu ke `celsiusToFahrenheit` untuk konversi — semua dalam satu putaran percakapan.

<img src="../../../translated_images/id/tool-chaining-example.538203e73d09dd82.webp" alt="Contoh Penghubungan Alat" width="800"/>

*Penghubungan alat dalam aksi — agen memanggil getCurrentWeather dulu, lalu memasukkan hasil Celsius ke celsiusToFahrenheit, dan memberikan jawaban gabungan.*

Berikut tampilan ini di aplikasi berjalan — agen menghubungkan dua panggilan alat dalam satu putaran percakapan:

<a href="images/tool-chaining.png"><img src="../../../translated_images/id/tool-chaining.3b25af01967d6f7b.webp" alt="Penghubungan Alat" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Output aplikasi nyata — agen secara otomatis menghubungkan getCurrentWeather → celsiusToFahrenheit dalam satu putaran.*

**Kegagalan yang Elegan** — Minta cuaca di kota yang tidak ada di data palsu. Alat mengembalikan pesan kesalahan, dan AI menjelaskan bahwa ia tidak bisa membantu daripada mengalami crash. Alat gagal dengan aman.

<img src="../../../translated_images/id/error-handling-flow.9a330ffc8ee0475c.webp" alt="Alur Penanganan Kesalahan" width="800"/>

*Saat alat gagal, agen menangkap kesalahan dan menanggapi dengan penjelasan yang membantu alih-alih crash.*

Ini terjadi dalam satu putaran percakapan. Agen mengorkestrasi banyak panggilan alat secara otonom.

## Menjalankan Aplikasi

**Verifikasi deployment:**

Pastikan file `.env` ada di direktori root dengan kredensial Azure (dibuat selama Modul 01):
```bash
cat ../.env  # Seharusnya menampilkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Mulai aplikasi:**

> **Catatan:** Jika Anda sudah menjalankan semua aplikasi dengan `./start-all.sh` dari Modul 01, modul ini sudah berjalan di port 8084. Anda bisa melewatkan perintah start di bawah dan langsung membuka http://localhost:8084.

**Opsi 1: Menggunakan Spring Boot Dashboard (Direkomendasikan untuk pengguna VS Code)**

Kontainer dev berisi ekstensi Spring Boot Dashboard yang menyediakan antarmuka visual untuk mengelola semua aplikasi Spring Boot. Anda bisa menemukannya di Activity Bar di sisi kiri VS Code (lihat ikon Spring Boot).

Dari Spring Boot Dashboard, Anda bisa:
- Melihat semua aplikasi Spring Boot yang tersedia di workspace
- Memulai/menghentikan aplikasi dengan satu klik
- Melihat log aplikasi secara real-time
- Memantau status aplikasi

Klik tombol play di samping "tools" untuk memulai modul ini, atau mulai semua modul sekaligus.

<img src="../../../translated_images/id/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

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

> **Catatan:** Jika Anda ingin membangun semua modul secara manual sebelum memulai:
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

**Untuk menghentikan:**

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

Aplikasi menyediakan antarmuka web di mana Anda dapat berinteraksi dengan agen AI yang memiliki akses ke alat cuaca dan konversi suhu.

<a href="images/tools-homepage.png"><img src="../../../translated_images/id/tools-homepage.4b4cd8b2717f9621.webp" alt="Antarmuka Alat Agen AI" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Antarmuka Alat Agen AI - contoh cepat dan antarmuka chat untuk berinteraksi dengan alat*

### Coba Penggunaan Alat Sederhana
Mulailah dengan permintaan sederhana: "Konversikan 100 derajat Fahrenheit ke Celsius". Agen mengenali bahwa ia memerlukan alat konversi suhu, memanggilnya dengan parameter yang tepat, dan mengembalikan hasilnya. Perhatikan betapa alami rasanya - Anda tidak menyebutkan alat mana yang digunakan atau bagaimana memanggilnya.

### Menguji Rangkaian Alat

Sekarang coba sesuatu yang lebih kompleks: "Bagaimana cuaca di Seattle dan konversikan ke Fahrenheit?" Amati agen bekerja melalui langkah-langkah ini. Pertama, ia mengambil data cuaca (yang mengembalikan suhu dalam Celsius), mengenali bahwa perlu dikonversi ke Fahrenheit, memanggil alat konversi, dan menggabungkan kedua hasil tersebut menjadi satu respons.

### Lihat Alur Percakapan

Antarmuka obrolan menyimpan riwayat percakapan, memungkinkan Anda melakukan interaksi berputar-putar. Anda dapat melihat semua pertanyaan dan jawaban sebelumnya, memudahkan untuk melacak percakapan dan memahami bagaimana agen membangun konteks selama beberapa pertukaran.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/id/tools-conversation-demo.89f2ce9676080f59.webp" alt="Percakapan dengan Beberapa Pemanggilan Alat" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Percakapan berputar-putar menunjukkan konversi sederhana, pencarian cuaca, dan rantai alat*

### Bereksperimen dengan Permintaan Berbeda

Coba berbagai kombinasi:
- Pencarian cuaca: "Bagaimana cuaca di Tokyo?"
- Konversi suhu: "Berapa 25°C dalam Kelvin?"
- Pertanyaan gabungan: "Periksa cuaca di Paris dan beri tahu saya jika di atas 20°C"

Perhatikan bagaimana agen menginterpretasikan bahasa alami dan memetakan ke pemanggilan alat yang sesuai.

## Konsep Utama

### Pola ReAct (Reasoning and Acting)

Agen bergantian antara penalaran (memutuskan apa yang harus dilakukan) dan bertindak (menggunakan alat). Pola ini memungkinkan pemecahan masalah secara mandiri, bukan hanya merespon instruksi.

### Deskripsi Alat Penting

Kualitas deskripsi alat Anda langsung memengaruhi seberapa baik agen menggunakannya. Deskripsi yang jelas dan spesifik membantu model memahami kapan dan bagaimana memanggil setiap alat.

### Manajemen Sesi

Anotasi `@MemoryId` memungkinkan manajemen memori berbasis sesi secara otomatis. Setiap ID sesi mendapatkan instance `ChatMemory` yang dikelola oleh bean `ChatMemoryProvider`, sehingga banyak pengguna bisa berinteraksi dengan agen secara bersamaan tanpa percakapan mereka tercampur.

<img src="../../../translated_images/id/session-management.91ad819c6c89c400.webp" alt="Manajemen Sesi dengan @MemoryId" width="800"/>

*Setiap ID sesi memetakan ke riwayat percakapan terisolasi — pengguna tidak pernah melihat pesan satu sama lain.*

### Penanganan Kesalahan

Alat bisa gagal — API timeout, parameter mungkin tidak valid, layanan eksternal mati. Agen produksi memerlukan penanganan kesalahan agar model dapat menjelaskan masalah atau mencoba alternatif daripada membuat aplikasi crash. Ketika alat melempar pengecualian, LangChain4j menangkapnya dan mengirimkan pesan kesalahan kembali ke model, yang kemudian dapat menjelaskan masalah tersebut dengan bahasa alami.

## Alat yang Tersedia

Diagram berikut menunjukkan ekosistem luas alat yang dapat Anda bangun. Modul ini mendemonstrasikan alat cuaca dan suhu, tetapi pola `@Tool` yang sama berlaku untuk metode Java apa pun — dari query basis data hingga pemrosesan pembayaran.

<img src="../../../translated_images/id/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ekosistem Alat" width="800"/>

*Setiap metode Java yang dianotasi dengan @Tool menjadi tersedia untuk AI — pola ini meluas ke database, API, email, operasi file, dan lainnya.*

## Kapan Menggunakan Agen Berbasis Alat

<img src="../../../translated_images/id/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Kapan Menggunakan Alat" width="800"/>

*Panduan cepat — alat untuk data real-time, perhitungan, dan tindakan; pengetahuan umum dan tugas kreatif tidak membutuhkannya.*

**Gunakan alat ketika:**
- Jawaban membutuhkan data real-time (cuaca, harga saham, inventaris)
- Anda perlu melakukan perhitungan lebih dari matematika sederhana
- Mengakses database atau API
- Melakukan tindakan (mengirim email, membuat tiket, memperbarui catatan)
- Menggabungkan beberapa sumber data

**Jangan gunakan alat ketika:**
- Pertanyaan dapat dijawab dari pengetahuan umum
- Respon hanya bersifat percakapan
- Latensi alat akan membuat pengalaman terlalu lambat

## Alat vs RAG

Modul 03 dan 04 keduanya memperluas kemampuan AI, tapi dengan cara yang sangat berbeda. RAG memberi model akses ke **pengetahuan** dengan mengambil dokumen. Alat memberi model kemampuan melakukan **tindakan** dengan memanggil fungsi.

<img src="../../../translated_images/id/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Perbandingan Alat vs RAG" width="800"/>

*RAG mengambil informasi dari dokumen statis — Alat mengeksekusi tindakan dan mengambil data dinamis, real-time. Banyak sistem produksi menggabungkan keduanya.*

Dalam praktiknya, banyak sistem produksi mengombinasikan kedua pendekatan: RAG untuk memberi dasar jawaban pada dokumentasi Anda, dan Alat untuk mengambil data live atau melakukan operasi.

## Langkah Selanjutnya

**Modul Berikutnya:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 03 - RAG](../03-rag/README.md) | [Kembali ke Utama](../README.md) | [Berikutnya: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penyangkalan**:  
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berupaya untuk mencapai akurasi, harap diketahui bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang sahih. Untuk informasi penting, disarankan menggunakan jasa penerjemah profesional. Kami tidak bertanggung jawab atas kesalahpahaman atau penafsiran yang salah yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->