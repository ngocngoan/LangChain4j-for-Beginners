# Modul 04: Ejen AI dengan Alat

## Jadual Kandungan

- [Apa Yang Akan Anda Pelajari](../../../04-tools)
- [Prasyarat](../../../04-tools)
- [Memahami Ejen AI dengan Alat](../../../04-tools)
- [Bagaimana Panggilan Alat Berfungsi](../../../04-tools)
  - [Definisi Alat](../../../04-tools)
  - [Pengambilan Keputusan](../../../04-tools)
  - [Pelaksanaan](../../../04-tools)
  - [Penjanaan Respons](../../../04-tools)
  - [Seni Bina: Spring Boot Auto-Wiring](../../../04-tools)
- [Rantaian Alat](../../../04-tools)
- [Jalankan Aplikasi](../../../04-tools)
- [Menggunakan Aplikasi](../../../04-tools)
  - [Cuba Penggunaan Alat Mudah](../../../04-tools)
  - [Uji Rantaian Alat](../../../04-tools)
  - [Lihat Aliran Perbualan](../../../04-tools)
  - [Cuba Permintaan Berbeza](../../../04-tools)
- [Konsep Utama](../../../04-tools)
  - [Corak ReAct (Berfikir dan Bertindak)](../../../04-tools)
  - [Penerangan Alat Penting](../../../04-tools)
  - [Pengurusan Sesi](../../../04-tools)
  - [Pengendalian Ralat](../../../04-tools)
- [Alat Tersedia](../../../04-tools)
- [Bila Menggunakan Ejen Berasaskan Alat](../../../04-tools)
- [Alat vs RAG](../../../04-tools)
- [Langkah Seterusnya](../../../04-tools)

## Apa Yang Akan Anda Pelajari

Setakat ini, anda telah belajar cara berbual dengan AI, menyusun prompt dengan berkesan, dan mendasari respons dalam dokumen anda. Tetapi masih ada had asas: model bahasa hanya boleh menjana teks. Ia tidak boleh menyemak cuaca, melakukan pengiraan, soal pangkalan data, atau berinteraksi dengan sistem luaran.

Alat mengubah ini. Dengan memberikan model akses kepada fungsi yang boleh dipanggil, anda mengubahnya daripada penjana teks menjadi ejen yang boleh mengambil tindakan. Model menentukan bila ia memerlukan alat, alat mana untuk digunakan, dan parameter apa yang perlu dihantar. Kod anda melaksanakan fungsi dan memulangkan hasilnya. Model menggabungkan keputusan itu ke dalam responsnya.

## Prasyarat

- Menyelesaikan [Modul 01 - Pengenalan](../01-introduction/README.md) (sumber Azure OpenAI telah diterapkan)
- Modul terdahulu disyorkan diselesaikan (modul ini merujuk [konsep RAG dari Modul 03](../03-rag/README.md) dalam perbandingan Alat vs RAG)
- Fail `.env` di direktori root dengan kelayakan Azure (dibuat oleh `azd up` dalam Modul 01)

> **Nota:** Jika anda belum menyelesaikan Modul 01, ikut arahan penerapan di sana terlebih dahulu.

## Memahami Ejen AI dengan Alat

> **📝 Nota:** Istilah "ejen" dalam modul ini merujuk kepada pembantu AI yang ditingkatkan dengan kebolehan pemanggilan alat. Ini berbeza daripada corak **Agentic AI** (ejen autonomi dengan perancangan, memori, dan pemikiran berbilang langkah) yang akan kita kupas dalam [Modul 05: MCP](../05-mcp/README.md).

Tanpa alat, model bahasa hanya boleh menjana teks daripada data latihannya. Tanyakan cuaca semasa kepadanya, dan ia hanya meneka. Berikan alat, dan ia boleh memanggil API cuaca, melakukan pengiraan, atau menyemak pangkalan data — lalu menyisipkan keputusan sebenar itu dalam responsnya.

<img src="../../../translated_images/ms/what-are-tools.724e468fc4de64da.webp" alt="Tanpa Alat vs Dengan Alat" width="800"/>

*Tanpa alat model hanya boleh meneka — dengan alat ia boleh memanggil API, melakukan pengiraan, dan memulangkan data masa sebenar.*

Ejen AI dengan alat mengikuti corak **Berfikir dan Bertindak (ReAct)**. Model bukan hanya memberi respons — ia berfikir tentang apa yang diperlukan, bertindak dengan memanggil alat, memerhati keputusan, dan kemudian memilih sama ada untuk bertindak lagi atau memberi jawapan akhir:

1. **Berfikir** — Ejen menganalisis soalan pengguna dan menentukan maklumat yang diperlukan
2. **Bertindak** — Ejen memilih alat yang betul, menjana parameter yang tepat, dan memanggilnya
3. **Memerhati** — Ejen menerima output alat dan menilai keputusan
4. **Ulang atau Respons** — Jika lebih data diperlukan, ejen mengulangi proses; jika tidak, ia membentuk jawapan dalam bahasa semula jadi

<img src="../../../translated_images/ms/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Corak ReAct" width="800"/>

*Kitaran ReAct — ejen berfikir tentang apa yang perlu dilakukan, bertindak dengan memanggil alat, memerhati keputusan, dan berulang sampai dapat jawapan akhir.*

Ini berlaku secara automatik. Anda mentakrif alat dan penerangannya. Model mengendalikan pengambilan keputusan bila dan bagaimana menggunakannya.

## Bagaimana Panggilan Alat Berfungsi

### Definisi Alat

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Anda mentakrif fungsi dengan penerangan jelas dan spesifikasi parameter. Model melihat penerangan ini dalam prompt sistemnya dan memahami fungsi setiap alat.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Logik carian cuaca anda
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Pembantu disambungkan secara automatik oleh Spring Boot dengan:
// - Bean ChatModel
// - Semua kaedah @Tool dari kelas @Component
// - ChatMemoryProvider untuk pengurusan sesi
```

Rajah di bawah menerangkan setiap penanda dan menunjukkan bagaimana setiap bahagian membantu AI memahami bila memanggil alat dan argumen apa yang dihantar:

<img src="../../../translated_images/ms/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomi Definisi Alat" width="800"/>

*Anatomi definisi alat — @Tool memberitahu AI bila untuk guna, @P menerangkan setiap parameter, dan @AiService menghubungkan semuanya semasa mula.*

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) dan tanya:
> - "Bagaimana saya integrasikan API cuaca sebenar seperti OpenWeatherMap menggantikan data palsu?"
> - "Apa yang menjadikan penerangan alat yang baik untuk membantu AI menggunakannya dengan betul?"
> - "Bagaimana saya tangani kesilapan API dan had kadar dalam pelaksanaan alat?"

### Pengambilan Keputusan

Apabila pengguna bertanya "Bagaimana cuaca di Seattle?", model tidak memilih alat secara rawak. Ia membandingkan niat pengguna dengan setiap penerangan alat yang ada, menilai relevan, dan memilih padanan terbaik. Ia kemudian menjana panggilan fungsi berstruktur dengan parameter betul — dalam kes ini, menetapkan `location` ke `"Seattle"`.

Jika tiada alat sepadan dengan permintaan, model akan menjawab berdasarkan pengetahuannya sendiri. Jika berbilang alat sepadan, ia memilih yang paling spesifik.

<img src="../../../translated_images/ms/decision-making.409cd562e5cecc49.webp" alt="Bagaimana AI Memilih Alat Yang Sesuai" width="800"/>

*Model menilai setiap alat yang ada berbanding niat pengguna dan memilih padanan terbaik — sebab itu penting menulis penerangan alat yang jelas dan spesifik.*

### Pelaksanaan

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot menghubungkan secara automatik antara antaramuka deklaratif `@AiService` dengan semua alat yang didaftarkan, dan LangChain4j melaksanakan panggilan alat secara automatik. Di belakang tabir, satu panggilan alat lengkap melalui enam peringkat — dari soalan bahasa semula jadi pengguna hingga jawapan bahasa semula jadi:

<img src="../../../translated_images/ms/tool-calling-flow.8601941b0ca041e6.webp" alt="Aliran Panggilan Alat" width="800"/>

*Aliran dari mula ke akhir — pengguna bertanya soalan, model memilih alat, LangChain4j melaksanakannya, dan model menyisip hasil ke dalam respons semula jadi.*

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) dan tanya:
> - "Bagaimana corak ReAct berfungsi dan mengapa efektif untuk ejen AI?"
> - "Bagaimana ejen memilih alat dan mengikut urutan apa?"
> - "Apa berlaku jika pelaksanaan alat gagal - bagaimana saya tangani ralat dengan kukuh?"

### Penjanaan Respons

Model menerima data cuaca dan memformatkannya menjadi respons bahasa semula jadi untuk pengguna.

### Seni Bina: Spring Boot Auto-Wiring

Modul ini menggunakan integrasi LangChain4j Spring Boot dengan antaramuka deklaratif `@AiService`. Semasa mula, Spring Boot mengesan setiap `@Component` yang mengandungi kaedah `@Tool`, bean `ChatModel`, dan `ChatMemoryProvider` — kemudian menghubungkan semuanya ke dalam satu antaramuka `Assistant` tanpa sebarang kod boilerplate.

<img src="../../../translated_images/ms/spring-boot-wiring.151321795988b04e.webp" alt="Seni Bina Spring Boot Auto-Wiring" width="800"/>

*Antaramuka @AiService menggabungkan ChatModel, komponen alat, dan penyedia memori — Spring Boot mengendalikan sambungan secara automatik.*

Faedah utama pendekatan ini:

- **Spring Boot auto-wiring** — ChatModel dan alat disuntik secara automatik
- **Corak @MemoryId** — Pengurusan memori berasaskan sesi secara automatik
- **Satu instans** — Assistant dicipta sekali dan digunakan semula untuk prestasi lebih baik
- **Pelaksanaan selamat jenis** — Kaedah Java dipanggil secara langsung dengan penukaran jenis
- **Orkestrasi berbilang pusingan** — Mengendalikan rantaian alat secara automatik
- **Tiada kod boilerplate** — Tiada panggilan manual `AiServices.builder()` atau HashMap memori

Pendekatan alternatif (manual `AiServices.builder()`) memerlukan lebih banyak kod dan kehilangan faedah integrasi Spring Boot.

## Rantaian Alat

**Rantaian Alat** — Kuasa sebenar ejen berasaskan alat nampak apabila satu soalan memerlukan pelbagai alat. Tanyakan "Bagaimana cuaca di Seattle dalam Fahrenheit?" dan ejen secara automatik merantaikan dua alat: pertama ia memanggil `getCurrentWeather` untuk dapatkan suhu dalam Celsius, kemudian menghantar nilai itu ke `celsiusToFahrenheit` untuk penukaran — semua dalam satu pusingan perbualan.

<img src="../../../translated_images/ms/tool-chaining-example.538203e73d09dd82.webp" alt="Contoh Rantaian Alat" width="800"/>

*Rantaian alat sedang beraksi — ejen panggil getCurrentWeather dahulu, kemudian alirkan keputusan Celsius ke celsiusToFahrenheit, dan berikan jawapan gabungan.*

**Kegagalan Terurus** — Tanyakan cuaca di bandar yang tiada dalam data palsu. Alat memulangkan mesej ralat, dan AI menerangkan ia tidak dapat membantu tanpa terhenti. Alat gagal dengan selamat. Rajah di bawah menunjukkan perbandingan dua pendekatan — dengan pengendalian ralat yang betul, ejen tangkap pengecualian dan jawab dengan penjelasan berguna, tanpa itu keseluruhan aplikasi terhenti:

<img src="../../../translated_images/ms/error-handling-flow.9a330ffc8ee0475c.webp" alt="Aliran Pengendalian Ralat" width="800"/>

*Apabila alat gagal, ejen menangkap ralat dan memberi penjelasan berguna bukannya terhenti.*

Ini berlaku dalam satu pusingan perbualan. Ejen mengendalikan pelbagai panggilan alat secara autonomi.

## Jalankan Aplikasi

**Sahkan penerapan:**

Pastikan fail `.env` wujud di direktori root dengan kelayakan Azure (dicipta semasa Modul 01). Jalankan ini dari direktori modul (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Patut menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Harus memaparkan AZURE_OPENAI_ENDPOINT, API_KEY, PENDEPLOYAN
```

**Mulakan aplikasi:**

> **Nota:** Jika anda sudah memulakan semua aplikasi menggunakan `./start-all.sh` dari direktori root (seperti yang diterangkan dalam Modul 01), modul ini sudah berjalan pada port 8084. Anda boleh langkau arahan mula di bawah dan terus ke http://localhost:8084.

**Pilihan 1: Menggunakan Spring Boot Dashboard (Disyorkan untuk pengguna VS Code)**

Kontena pembangunan mengandungi ekstensi Spring Boot Dashboard, yang menyediakan antara muka visual untuk mengurus semua aplikasi Spring Boot. Anda boleh menjumpainya di Bar Aktiviti di sebelah kiri VS Code (cari ikon Spring Boot).

Daripada Spring Boot Dashboard, anda boleh:
- Lihat semua aplikasi Spring Boot yang ada dalam ruang kerja
- Mulakan/hentikan aplikasi dengan satu klik
- Lihat log aplikasi secara masa nyata
- Pantau status aplikasi

Klik butang main di sebelah "tools" untuk mula modul ini, atau mula semua modul serentak.

Ini rupa Spring Boot Dashboard dalam VS Code:

<img src="../../../translated_images/ms/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard dalam VS Code — mula, hentikan, dan pantau semua modul dari satu tempat*

**Pilihan 2: Menggunakan skrip shell**

Mula semua aplikasi web (modul 01-04):

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

Atau mulakan modul ini sahaja:

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

Kedua-dua skrip secara automatik memuatkan pembolehubah persekitaran dari fail `.env` root dan akan membina JAR jika belum wujud.

> **Nota:** Jika anda mahu bina semua modul secara manual sebelum mula:
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

Buka http://localhost:8084 dalam pelayar anda.

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

Aplikasi menyediakan antara muka web di mana anda boleh berinteraksi dengan ejen AI yang mempunyai akses ke alat cuaca dan penukaran suhu. Ini rupa antara muka — ia mengandungi contoh permulaan pantas dan panel perbualan untuk menghantar permintaan:
<a href="images/tools-homepage.png"><img src="../../../translated_images/ms/tools-homepage.4b4cd8b2717f9621.webp" alt="Antara Muka Alat Ejen AI" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Antara muka Alat Ejen AI - contoh pantas dan antara muka sembang untuk berinteraksi dengan alat*

### Cuba Penggunaan Alat Mudah

Mulakan dengan permintaan yang mudah: "Tukar 100 darjah Fahrenheit ke Celsius". Ejen mengenal pasti ia memerlukan alat penukaran suhu, memanggilnya dengan parameter yang betul, dan mengembalikan hasilnya. Perhatikan betapa semulajadinya ini terasa - anda tidak menyatakan alat mana yang harus digunakan atau bagaimana untuk memanggilnya.

### Uji Penghubungan Alat

Sekarang cuba sesuatu yang lebih kompleks: "Apa cuaca di Seattle dan tukarkannya ke Fahrenheit?" Saksikan bagaimana ejen ini berfungsi langkah demi langkah. Ia mula-mula mendapatkan cuaca (yang memberi hasil dalam Celsius), mengenal pasti ia perlu menukar ke Fahrenheit, memanggil alat penukaran, dan menggabungkan kedua-dua hasil menjadi satu jawapan.

### Lihat Aliran Perbualan

Antara muka sembang mengekalkan sejarah perbualan, membolehkan anda mempunyai interaksi pelbagai pusingan. Anda boleh melihat semua pertanyaan dan jawapan sebelumnya, memudahkan untuk mengesan perbualan dan memahami bagaimana ejen membina konteks melalui beberapa pertukaran.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/ms/tools-conversation-demo.89f2ce9676080f59.webp" alt="Perbualan dengan Pelbagai Panggilan Alat" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Perbualan pelbagai pusingan menunjukkan penukaran mudah, carian cuaca, dan penghubungan alat*

### Cuba Pelbagai Permintaan

Cuba gabungan berikut:
- Carian cuaca: "Apa cuaca di Tokyo?"
- Penukaran suhu: "Berapakah 25°C dalam Kelvin?"
- Pertanyaan gabungan: "Semak cuaca di Paris dan beritahu saya jika ia melebihi 20°C"

Perhatikan bagaimana ejen mentafsir bahasa semula jadi dan memetakannya kepada panggilan alat yang sesuai.

## Konsep Utama

### Corak ReAct (Penalaran dan Bertindak)

Ejen bergilir antara membuat pertimbangan (menentukan apa yang perlu dilakukan) dan bertindak (menggunakan alat). Corak ini membolehkan penyelesaian masalah secara autonomi dan bukan sekadar bertindak balas kepada arahan.

### Penerangan Alat Penting

Kualiti penerangan alat anda memberi kesan langsung kepada sejauh mana ejen menggunakannya dengan baik. Penerangan yang jelas dan khusus membantu model memahami bila dan bagaimana untuk memanggil setiap alat.

### Pengurusan Sesi

Anotasi `@MemoryId` membolehkan pengurusan memori berasaskan sesi secara automatik. Setiap ID sesi mendapat instans `ChatMemory` tersendiri yang diurus oleh bean `ChatMemoryProvider`, jadi pengguna berbilang boleh berinteraksi dengan ejen serentak tanpa perbualan mereka bercampur. Rajah berikut menunjukkan bagaimana berbilang pengguna diarahkan ke stor memori yang terasing berdasarkan ID sesi mereka:

<img src="../../../translated_images/ms/session-management.91ad819c6c89c400.webp" alt="Pengurusan Sesi dengan @MemoryId" width="800"/>

*Setiap ID sesi dipetakan ke sejarah perbualan yang terasing — pengguna tidak pernah melihat mesej satu sama lain.*

### Pengendalian Ralat

Alat boleh gagal — API tamat masa, parameter mungkin tidak sah, perkhidmatan luaran mungkin terhenti. Ejen produksi memerlukan pengendalian ralat supaya model dapat menjelaskan masalah atau mencuba alternatif daripada menyebabkan aplikasi terhenti. Apabila alat melemparkan pengecualian, LangChain4j akan menangkapnya dan memberi maklum balas mesej ralat kepada model, yang kemudian boleh menerangkan masalah dengan bahasa semula jadi.

## Alat Tersedia

Rajah di bawah menunjukkan ekosistem luas alat yang anda boleh bina. Modul ini menunjukkan alat cuaca dan suhu, tetapi corak `@Tool` yang sama boleh digunakan untuk mana-mana kaedah Java — dari pertanyaan pangkalan data hingga pemprosesan pembayaran.

<img src="../../../translated_images/ms/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ekosistem Alat" width="800"/>

*Mana-mana kaedah Java yang dianotasi dengan @Tool menjadi boleh digunakan oleh AI — corak ini meluas kepada pangkalan data, API, e-mel, operasi fail, dan banyak lagi.*

## Bila untuk Menggunakan Ejen Berasaskan Alat

Tidak setiap permintaan memerlukan alat. Keputusan bergantung pada sama ada AI perlu berinteraksi dengan sistem luaran atau boleh menjawab dari pengetahuannya sendiri. Panduan berikut meringkaskan bila alat menambah nilai dan bila ia tidak diperlukan:

<img src="../../../translated_images/ms/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Bila Untuk Menggunakan Alat" width="800"/>

*Panduan keputusan pantas — alat digunakan untuk data masa nyata, pengiraan, dan tindakan; pengetahuan umum dan tugas kreatif tidak memerlukannya.*

## Alat vs RAG

Modul 03 dan 04 kedua-duanya memperluas apa yang boleh dilakukan AI, tetapi dengan cara yang berbeza secara asas. RAG memberi model akses kepada **pengetahuan** dengan mendapatkan dokumen. Alat memberi model keupayaan untuk mengambil **tindakan** dengan memanggil fungsi. Rajah di bawah membandingkan dua pendekatan ini berdampingan — dari bagaimana setiap aliran kerja beroperasi hingga pertukaran antara keduanya:

<img src="../../../translated_images/ms/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Perbandingan Alat vs RAG" width="800"/>

*RAG mendapatkan maklumat dari dokumen statik — Alat melaksanakan tindakan dan mengambil data dinamik masa nyata. Banyak sistem produksi menggabungkan kedua-duanya.*

Dalam praktiknya, banyak sistem produksi menggabungkan kedua-dua pendekatan: RAG untuk memberi asas jawapan dalam dokumentasi anda, dan Alat untuk mendapatkan data langsung atau menjalankan operasi.

## Langkah Seterusnya

**Modul Seterusnya:** [05-mcp - Protokol Konteks Model (MCP)](../05-mcp/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 03 - RAG](../03-rag/README.md) | [Kembali ke Utama](../README.md) | [Seterusnya: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang sahih. Untuk maklumat yang penting, terjemahan manusia profesional adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->