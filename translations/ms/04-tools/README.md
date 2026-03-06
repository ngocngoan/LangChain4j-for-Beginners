# Modul 04: Ejen AI dengan Alat

## Jadual Kandungan

- [Apa yang Anda Akan Pelajari](../../../04-tools)
- [Prasyarat](../../../04-tools)
- [Memahami Ejen AI dengan Alat](../../../04-tools)
- [Bagaimana Pemanggilan Alat Berfungsi](../../../04-tools)
  - [Definisi Alat](../../../04-tools)
  - [Pembuatan Keputusan](../../../04-tools)
  - [Pelaksanaan](../../../04-tools)
  - [Penjanaan Respons](../../../04-tools)
  - [Senibina: Auto-Wiring Spring Boot](../../../04-tools)
- [Rantai Alat](../../../04-tools)
- [Jalankan Aplikasi](../../../04-tools)
- [Menggunakan Aplikasi](../../../04-tools)
  - [Cuba Penggunaan Alat Ringkas](../../../04-tools)
  - [Uji Rantaian Alat](../../../04-tools)
  - [Lihat Aliran Perbualan](../../../04-tools)
  - [Eksperimen dengan Permintaan Berbeza](../../../04-tools)
- [Konsep Utama](../../../04-tools)
  - [Corak ReAct (Berfikir dan Bertindak)](../../../04-tools)
  - [Penerangan Alat Penting](../../../04-tools)
  - [Pengurusan Sesi](../../../04-tools)
  - [Pengendalian Ralat](../../../04-tools)
- [Alat yang Tersedia](../../../04-tools)
- [Bila Menggunakan Ejen Berasaskan Alat](../../../04-tools)
- [Alat vs RAG](../../../04-tools)
- [Langkah Seterusnya](../../../04-tools)

## Apa yang Anda Akan Pelajari

Setakat ini, anda telah belajar bagaimana untuk bersembang dengan AI, menyusun arahan (prompt) dengan berkesan, dan membuat jawapan berpandukan dokumen anda. Tetapi masih ada had asas: model bahasa hanya boleh menjana teks. Ia tidak boleh memeriksa cuaca, melakukan pengiraan, bertanya kepada pangkalan data, atau berinteraksi dengan sistem luaran.

Alat mengubah perkara ini. Dengan memberikan model akses kepada fungsi yang boleh dipanggil, anda mengubahnya dari penjana teks menjadi ejen yang boleh melakukan tindakan. Model menentukan bila ia memerlukan alat, alat mana yang hendak digunakan, dan parameter apa yang perlu dihantar. Kod anda melaksanakan fungsi tersebut dan mengembalikan hasilnya. Model menggabungkan hasil itu dalam responsnya.

## Prasyarat

- Selesai [Modul 01 - Pengenalan](../01-introduction/README.md) (Sumber Azure OpenAI sudah dideploy)
- Disyorkan menamatkan modul sebelumnya (modul ini merujuk [konsep RAG dari Modul 03](../03-rag/README.md) dalam perbandingan Alat vs RAG)
- Fail `.env` di direktori root dengan kelayakan Azure (dibuat oleh `azd up` dalam Modul 01)

> **Nota:** Jika anda belum menamatkan Modul 01, ikuti arahan deployment di sana terlebih dahulu.

## Memahami Ejen AI dengan Alat

> **📝 Nota:** Istilah "ejen" dalam modul ini merujuk kepada pembantu AI yang dipertingkatkan dengan keupayaan pemanggilan alat. Ini berbeza dengan corak **Agentic AI** (ejen autonomi dengan perancangan, memori, dan reasoning berbilang langkah) yang akan kami terangkan dalam [Modul 05: MCP](../05-mcp/README.md).

Tanpa alat, model bahasa hanya boleh menghasilkan teks berdasarkan data latihan. Tanyakan cuaca terkini, ia hanya boleh meneka. Beri ia alat, dan ia boleh memanggil API cuaca, melakukan pengiraan, atau bertanya ke pangkalan data — kemudian menyusunkan keputusan sebenar itu dalam jawapannya.

<img src="../../../translated_images/ms/what-are-tools.724e468fc4de64da.webp" alt="Tanpa Alat vs Dengan Alat" width="800"/>

*Tanpa alat model hanya boleh meneka — dengan alat ia boleh memanggil API, jalankan pengiraan, dan pulangkan data masa sebenar.*

Ejen AI dengan alat mengikut corak **Berfikir dan Bertindak (ReAct)**. Model bukan hanya memberi respons — ia berfikir apa yang diperlukan, bertindak dengan memanggil alat, memerhati hasilnya, dan kemudian memutuskan sama ada bertindak lagi atau menyampaikan jawapan akhir:

1. **Berfikir** — Ejen menganalisis soalan pengguna dan menentukan maklumat yang diperlukan  
2. **Bertindak** — Ejen memilih alat yang betul, menjana parameter yang tepat, dan memanggilnya  
3. **Memerhati** — Ejen menerima output alat dan menilai hasilnya  
4. **Ulang atau Jawab** — Jika perlu data lagi, ejen mengulangi langkah; jika tidak, ia menyusun jawapan bahasa semula jadi

<img src="../../../translated_images/ms/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Corak ReAct" width="800"/>

*Kitaran ReAct — ejen berfikir apa yang perlu dibuat, bertindak dengan memanggil alat, memerhati hasil, dan berputar sehingga boleh memberikan jawapan terakhir.*

Ini berlaku secara automatik. Anda menentukan alat dan penerangannya. Model menguruskan pembuatan keputusan bila dan bagaimana menggunakannya.

## Bagaimana Pemanggilan Alat Berfungsi

### Definisi Alat

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Anda mentakrif fungsi dengan penerangan jelas dan spesifikasi parameter. Model melihat penerangan ini dalam prompt sistem dan memahami apa fungsi setiap alat.

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
  
Rajah di bawah menganalisis setiap anotasi dan menunjukkan bagaimana setiap bahagian membantu AI memahami bila untuk memanggil alat dan argumen apa yang perlu dihantar:

<img src="../../../translated_images/ms/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomi Definisi Alat" width="800"/>

*Anatomi definisi alat — @Tool memberitahu AI bila menggunakannya, @P menerangkan setiap parameter, dan @AiService menghubungkan semuanya semasa permulaan.*

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) dan tanya:  
> - "Bagaimana saya integrasikan API cuaca sebenar seperti OpenWeatherMap yang sebenar, bukan data contoh?"  
> - "Apa ciri penerangan alat yang baik supaya AI menggunakannya dengan betul?"  
> - "Bagaimana saya mengendalikan ralat API dan had kadar dalam pelaksanaan alat?"

### Pembuatan Keputusan

Apabila pengguna bertanya "Cuaca di Seattle bagaimana?", model tidak memilih alat secara rawak. Ia membandingkan niat pengguna dengan setiap penerangan alat yang ada, menilai relevansi setiap satu, dan memilih yang paling sesuai. Kemudian ia menjana panggilan fungsi berstruktur dengan parameter betul — dalam kes ini, menetapkan `location` kepada `"Seattle"`.

Jika tiada alat sesuai dengan permintaan pengguna, model akan menjawab berdasarkan pengetahuan sendiri. Jika lebih dari satu alat sesuai, ia memilih satu yang paling spesifik.

<img src="../../../translated_images/ms/decision-making.409cd562e5cecc49.webp" alt="Bagaimana AI Memutuskan Alat Mana Untuk Digunakan" width="800"/>

*Model menilai setiap alat yang tersedia terhadap niat pengguna dan memilih yang terbaik — sebab itu penerangan alat yang jelas dan spesifik penting.*

### Pelaksanaan

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot menghubungkan secara automatik antara muka deklaratif `@AiService` dengan semua alat yang didaftarkan, dan LangChain4j melaksanakan panggilan alat secara automatik. Di belakang tabir, panggilan alat lengkap mengalir melalui enam peringkat — dari soalan bahasa semula jadi pengguna hingga jawapan bahasa semula jadi:

<img src="../../../translated_images/ms/tool-calling-flow.8601941b0ca041e6.webp" alt="Aliran Pemanggilan Alat" width="800"/>

*Aliran hujung-ke-hujung — pengguna bertanya, model memilih alat, LangChain4j melaksanakannya, dan model menyusun hasilnya ke dalam respons alami.*

Jika anda sudah jalankan [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) dalam Modul 00, anda sudah lihat corak ini berfungsi — alat `Calculator` dipanggil dengan cara sama. Rajah urutan di bawah menunjukkan apa yang berlaku secara terperinci semasa demo itu:

<img src="../../../translated_images/ms/tool-calling-sequence.94802f406ca26278.webp" alt="Rajah Urutan Pemanggilan Alat" width="800"/>

*Gelung pemanggilan alat dari demo Quick Start — `AiServices` hantar mesej dan skema alat ke LLM, LLM jawab dengan panggilan fungsi seperti `add(42, 58)`, LangChain4j laksana kaedah `Calculator` secara lokal, dan hasil dikembalikan untuk jawapan akhir.*

> **🤖 Cuba dengan [GitHub Copilot](https://github.com/features/copilot) Chat:** Buka [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) dan tanya:  
> - "Bagaimana corak ReAct berfungsi dan mengapa ia berkesan untuk ejen AI?"  
> - "Bagaimana ejen menentukan alat mana untuk digunakan dan dalam apa urutan?"  
> - "Apa yang berlaku jika pelaksanaan alat gagal - bagaimana saya hendak mengendalikan ralat dengan kukuh?"

### Penjanaan Respons

Model menerima data cuaca dan memformatnya menjadi respons bahasa semula jadi untuk pengguna.

### Senibina: Auto-Wiring Spring Boot

Modul ini menggunakan integrasi LangChain4j dengan Spring Boot melalui antara muka deklaratif `@AiService`. Semasa permulaan, Spring Boot mengesan setiap `@Component` yang mengandungi kaedah `@Tool`, bean `ChatModel` anda, dan `ChatMemoryProvider` — kemudian menghubungkan semuanya ke satu antara muka `Assistant` tanpa sebarang kod boilerplate.

<img src="../../../translated_images/ms/spring-boot-wiring.151321795988b04e.webp" alt="Senibina Auto-Wiring Spring Boot" width="800"/>

*Antara muka @AiService menggabungkan ChatModel, komponen alat, dan pembekal memori — Spring Boot mengurus semua sambungan secara automatik.*

Berikut adalah kitar hayat permintaan penuh sebagai rajah urutan — dari permintaan HTTP hinggalah ke controller, servis, dan proxy auto-wired, sehingga pelaksanaan alat dan kembali semula:

<img src="../../../translated_images/ms/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Rajah Urutan Pemanggilan Alat Spring Boot" width="800"/>

*Kitar hayat permintaan lengkap Spring Boot — permintaan HTTP melalu controller dan servis ke proxy Assistant auto-wired, yang mengatur LLM dan panggilan alat secara automatik.*

Kelebihan utama pendekatan ini:

- **Auto-wiring Spring Boot** — ChatModel dan alat disuntik secara automatik  
- **Corak @MemoryId** — Pengurusan memori berasaskan sesi secara automatik  
- **Instans tunggal** — Assistant dicipta sekali dan digunakan semula untuk prestasi lebih baik  
- **Pelaksanaan jenis selamat** — Kaedah Java dipanggil terus dengan penukaran jenis  
- **Orkestrasi berbilang pusingan** — Mengendalikan rantaian alat secara automatik  
- **Tiada boilerplate** — Tiada panggilan manual `AiServices.builder()` atau HashMap memori  

Pendekatan alternatif (manual `AiServices.builder()`) memerlukan lebih banyak kod dan kehilangan kebaikan integrasi Spring Boot.

## Rantai Alat

**Rantai Alat** — Kuasa sebenar ejen berasaskan alat muncul apabila satu soalan memerlukan beberapa alat. Tanya "Cuaca di Seattle dalam Fahrenheit?" dan ejen secara automatik merantai dua alat: pertama ia panggil `getCurrentWeather` untuk dapatkan suhu dalam Celsius, kemudian hantar nilai itu ke `celsiusToFahrenheit` untuk penukaran — semua dalam satu pusingan perbualan.

<img src="../../../translated_images/ms/tool-chaining-example.538203e73d09dd82.webp" alt="Contoh Rantaian Alat" width="800"/>

*Rantaian alat beraksi — ejen panggil getCurrentWeather dahulu, kemudian paip keputusan Celsius ke celsiusToFahrenheit, dan beri jawapan gabungan.*

**Kegagalan dengan Anggun** — Minta cuaca di bandar yang tiada dalam data contoh. Alat kembalikan mesej ralat, dan AI jelaskan ia tidak dapat membantu daripada aplikasi terus rosak (crash). Alat gagal dengan selamat. Rajah di bawah membezakan dua pendekatan — dengan pengendalian ralat betul, ejen tangkap exception dan beri respons membantu, manakala tanpa itu aplikasi keseluruhan rosak:

<img src="../../../translated_images/ms/error-handling-flow.9a330ffc8ee0475c.webp" alt="Aliran Pengendalian Ralat" width="800"/>

*Apabila alat gagal, ejen tangkap ralat dan beri penjelasan membantu bukannya crash.*

Ini berlaku dalam satu pusingan perbualan. Ejen mengatur pelbagai panggilan alat secara autonomi.

## Jalankan Aplikasi

**Sahkan deployment:**

Pastikan fail `.env` wujud di direktori root dengan kelayakan Azure (dibuat semasa Modul 01). Jalankan ini dari direktori modul (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Patut menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, PENDEPLOYAN
```
  
**PowerShell:**
```powershell
Get-Content ..\.env  # Perlu menunjukkan AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Mulakan aplikasi:**

> **Nota:** Jika anda sudah mulakan semua aplikasi menggunakan `./start-all.sh` dari direktori root (seperti dalam Modul 01), modul ini sudah berjalan pada port 8084. Anda boleh langkau arahan start di bawah dan terus ke http://localhost:8084.

**Pilihan 1: Menggunakan Papan Pemuka Spring Boot (Disyorkan untuk pengguna VS Code)**

Kontena dev menyertakan sambungan Papan Pemuka Spring Boot, yang menyediakan antara muka visual untuk mengurus semua aplikasi Spring Boot. Anda boleh menemukannya di Bar Aktiviti di sisi kiri VS Code (cari ikon Spring Boot).

Dari Papan Pemuka Spring Boot, anda boleh:
- Lihat semua aplikasi Spring Boot yang tersedia dalam ruang kerja  
- Mula/hentikan aplikasi dengan satu klik  
- Lihat log aplikasi secara masa nyata  
- Pantau status aplikasi  

Klik butang main di sebelah "tools" untuk mulakan modul ini, atau mulakan semua modul sekaligus.

Ini rupa Papan Pemuka Spring Boot dalam VS Code:

<img src="../../../translated_images/ms/dashboard.9b519b1a1bc1b30a.webp" alt="Papan Pemuka Spring Boot" width="400"/>

*Papan Pemuka Spring Boot dalam VS Code — mula, hentikan, dan pantau semua modul dari satu tempat.*

**Pilihan 2: Menggunakan skrip shell**

Mulakan semua aplikasi web (modul 01-04):

**Bash:**
```bash
cd ..  # Dari direktori akar
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Dari direktori akar
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

Kedua-dua skrip secara automatik memuatkan pemboleh ubah persekitaran dari fail `.env` akar dan akan membina JAR jika ia tidak wujud.

> **Nota:** Jika anda lebih suka membina semua modul secara manual sebelum memulakan:
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

Buka http://localhost:8084 dalam penyemak imbas anda.

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

Aplikasi ini menyediakan antara muka web di mana anda boleh berinteraksi dengan ejen AI yang mempunyai akses kepada alat cuaca dan penukaran suhu. Inilah rupa antara muka — ia merangkumi contoh permulaan pantas dan panel chat untuk menghantar permintaan:

<a href="images/tools-homepage.png"><img src="../../../translated_images/ms/tools-homepage.4b4cd8b2717f9621.webp" alt="Antara Muka Alat Ejen AI" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Antara muka Alat Ejen AI - contoh cepat dan antara muka chat untuk berinteraksi dengan alat*

### Cuba Penggunaan Alat Mudah

Mula dengan permintaan yang mudah: "Tukar 100 darjah Fahrenheit ke Celsius". Ejen mengenal pasti ia memerlukan alat penukaran suhu, memanggilnya dengan parameter yang betul, dan mengembalikan hasilnya. Perhatikan betapa semulajadinya perasaan ini - anda tidak menetapkan alat mana untuk digunakan atau bagaimana untuk memanggilnya.

### Uji Rantaian Alat

Sekarang cuba sesuatu yang lebih kompleks: "Bagaimana cuaca di Seattle dan tukarkan ke Fahrenheit?" Saksikan ejen bekerja melalui langkah ini. Ia mula-mula mendapatkan cuaca (yang mengembalikan Celsius), mengenal pasti ia perlu menukar ke Fahrenheit, memanggil alat penukaran, dan menggabungkan kedua-dua hasil ke dalam satu respons.

### Lihat Aliran Perbualan

Antara muka chat mengekalkan sejarah perbualan, membolehkan anda mempunyai interaksi berbilang giliran. Anda boleh melihat semua pertanyaan dan respons sebelum ini, memudahkan anda menjejaki perbualan dan memahami bagaimana ejen membina konteks melalui pertukaran berganda.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/ms/tools-conversation-demo.89f2ce9676080f59.webp" alt="Perbualan dengan Pelbagai Panggilan Alat" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Perbualan berbilang giliran menunjukkan penukaran mudah, pencarian cuaca, dan rantaian alat*

### Eksperimen dengan Pelbagai Permintaan

Cuba gabungan berikut:
- Pencarian cuaca: "Bagaimana cuaca di Tokyo?"
- Penukaran suhu: "Berapakah 25°C dalam Kelvin?"
- Pertanyaan gabungan: "Periksa cuaca di Paris dan beritahu saya jika ia melebihi 20°C"

Perhatikan bagaimana ejen mentafsir bahasa semulajadi dan memetakan kepada panggilan alat yang sesuai.

## Konsep Utama

### Corak ReAct (Penalaran dan Bertindak)

Ejen bergilir antara penalaran (memutuskan apa yang perlu dibuat) dan bertindak (menggunakan alat). Corak ini membolehkan penyelesaian masalah autonomi dan bukannya hanya bertindak balas kepada arahan.

### Penerangan Alat Penting

Kualiti penerangan alat anda secara langsung mempengaruhi bagaimana baik ejen menggunakannya. Penerangan yang jelas dan khusus membantu model memahami bila dan bagaimana memanggil setiap alat.

### Pengurusan Sesi

Anotasi `@MemoryId` membolehkan pengurusan memori berasaskan sesi secara automatik. Setiap ID sesi mendapat instans `ChatMemory` sendiri yang diurus oleh bean `ChatMemoryProvider`, jadi pelbagai pengguna boleh berinteraksi dengan ejen secara serentak tanpa perbualan mereka bercampur. Rajah berikut menunjukkan bagaimana pelbagai pengguna diarahkan ke stor memori terasing berdasarkan ID sesi mereka:

<img src="../../../translated_images/ms/session-management.91ad819c6c89c400.webp" alt="Pengurusan Sesi dengan @MemoryId" width="800"/>

*Setiap ID sesi memetakan sejarah perbualan terasing — pengguna tidak pernah melihat mesej pengguna lain.*

### Pengendalian Ralat

Alat boleh gagal — API tamat masa, parameter mungkin tidak sah, perkhidmatan luaran terhenti. Ejen produksi memerlukan pengendalian ralat supaya model boleh menerangkan masalah atau mencuba alternatif daripada menyebabkan seluruh aplikasi rosak. Apabila alat membuang pengecualian, LangChain4j menangkapnya dan memberi mesej ralat kembali kepada model, yang kemudian boleh menerangkan masalah tersebut dalam bahasa semulajadi.

## Alat Yang Tersedia

Rajah di bawah menunjukkan ekosistem luas alat yang anda boleh bina. Modul ini menunjukkan alat cuaca dan suhu, tetapi corak `@Tool` yang sama berfungsi untuk mana-mana kaedah Java — dari pertanyaan pangkalan data ke pemprosesan pembayaran.

<img src="../../../translated_images/ms/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ekosistem Alat" width="800"/>

*Mana-mana kaedah Java yang dianotasi dengan @Tool menjadi tersedia untuk AI — corak ini meliputi pangkalan data, API, emel, operasi fail, dan lebih banyak lagi.*

## Bila Perlu Menggunakan Ejen Berasaskan Alat

Tidak setiap permintaan memerlukan alat. Keputusan bergantung sama ada AI perlu berinteraksi dengan sistem luaran atau boleh menjawab berdasarkan pengetahuan sendiri. Panduan berikut merumuskan bila alat memberikan nilai dan bila ia tidak diperlukan:

<img src="../../../translated_images/ms/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Bila Perlu Menggunakan Alat" width="800"/>

*Panduan keputusan cepat — alat sesuai untuk data masa nyata, pengiraan, dan tindakan; pengetahuan umum dan tugas kreatif tidak memerlukannya.*

## Alat vs RAG

Modul 03 dan 04 kedua-duanya meluaskan apa yang AI boleh lakukan, tetapi dengan cara yang berbeza secara mendasar. RAG membolehkan model mengakses **pengetahuan** dengan mengambil dokumen. Alat membolehkan model mengambil **tindakan** dengan memanggil fungsi. Rajah di bawah membandingkan kedua-dua pendekatan ini sebelah menyebelah — dari cara setiap aliran kerja beroperasi kepada pertukaran antara mereka:

<img src="../../../translated_images/ms/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Perbandingan Alat vs RAG" width="800"/>

*RAG mendapatkan maklumat dari dokumen statik — Alat melaksanakan tindakan dan mengambil data masa nyata yang dinamik. Banyak sistem produksi menggabungkan kedua-duanya.*

Dalam praktik, banyak sistem produksi menggabungkan kedua-dua pendekatan: RAG untuk mengasaskan jawapan dalam dokumentasi anda, dan Alat untuk mengambil data langsung atau melakukan operasi.

## Langkah Seterusnya

**Modul Seterusnya:** [05-mcp - Protokol Konteks Model (MCP)](../05-mcp/README.md)

---

**Navigasi:** [← Sebelumnya: Modul 03 - RAG](../03-rag/README.md) | [Kembali ke Utama](../README.md) | [Seterusnya: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk memastikan ketepatan, sila ambil perhatian bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya hendaklah dianggap sebagai sumber yang sahih. Untuk maklumat penting, terjemahan profesional oleh manusia adalah disyorkan. Kami tidak bertanggungjawab terhadap sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->