# Kamus LangChain4j

## Jadual Kandungan

- [Konsep Teras](../../../docs)
- [Komponen LangChain4j](../../../docs)
- [Konsep AI/ML](../../../docs)
- [Talian Penjaga](../../../docs)
- [Kejuruteraan Prompt](../../../docs)
- [RAG (Penjanaan Beraugmencarian)](../../../docs)
- [Ejen dan Alat](../../../docs)
- [Modul Agentik](../../../docs)
- [Protokol Konteks Model (MCP)](../../../docs)
- [Perkhidmatan Azure](../../../docs)
- [Ujian dan Pembangunan](../../../docs)

Rujukan pantas untuk istilah dan konsep yang digunakan sepanjang kursus.

## Konsep Teras

**Ejen AI** - Sistem yang menggunakan AI untuk berfikir dan bertindak secara autonomi. [Modul 04](../04-tools/README.md)

**Rantaian** - Urutan operasi di mana output menjadi input langkah seterusnya.

**Pemecahan** - Memecahkan dokumen kepada bahagian lebih kecil. Lazim: 300-500 token dengan tumpang tindih. [Modul 03](../03-rag/README.md)

**Tetingkap Konteks** - Maksimum token yang model boleh proses. GPT-5.2: 400K token.

**Penyalinan** - Vektor bernombor yang mewakili maksud teks. [Modul 03](../03-rag/README.md)

**Panggilan Fungsi** - Model menjana permintaan berstruktur untuk memanggil fungsi luaran. [Modul 04](../04-tools/README.md)

**Halusinasi** - Apabila model menjana maklumat salah tetapi kelihatan munasabah.

**Prompt** - Input teks kepada model bahasa. [Modul 02](../02-prompt-engineering/README.md)

**Carian Semantik** - Carian berdasarkan maksud menggunakan penyalinan, bukan kata kunci. [Modul 03](../03-rag/README.md)

**Berkeadaan vs Tidak Berkeadaan** - Tidak berkeadaan: tiada memori. Berkeadaan: menyimpan sejarah perbualan. [Modul 01](../01-introduction/README.md)

**Token** - Unit teks asas yang model proses. Mempengaruhi kos dan had. [Modul 01](../01-introduction/README.md)

**Rantaian Alat** - Pelaksanaan alat secara berurutan di mana output memaklumkan panggilan seterusnya. [Modul 04](../04-tools/README.md)

## Komponen LangChain4j

**AiServices** - Mencipta antara muka perkhidmatan AI yang selamat jenis.

**OpenAiOfficialChatModel** - Pelanggan bersatu untuk model OpenAI dan Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Mencipta penyalinan menggunakan pelanggan rasmi OpenAI (menyokong OpenAI dan Azure OpenAI).

**ChatModel** - Antara muka teras untuk model bahasa.

**ChatMemory** - Menyimpan sejarah perbualan.

**ContentRetriever** - Mencari bahagian dokumen relevan untuk RAG.

**DocumentSplitter** - Memecahkan dokumen kepada bahagian.

**EmbeddingModel** - Menukar teks menjadi vektor bernombor.

**EmbeddingStore** - Menyimpan dan mengambil penyalinan.

**MessageWindowChatMemory** - Menyimpan tetingkap luncur mesej terkini.

**PromptTemplate** - Mencipta prompt boleh guna semula dengan tempat letak `{{variable}}`.

**TextSegment** - Bahagian teks dengan metadata. Digunakan dalam RAG.

**ToolExecutionRequest** - Mewakili permintaan pelaksanaan alat.

**UserMessage / AiMessage / SystemMessage** - Jenis mesej perbualan.

## Konsep AI/ML

**Pembelajaran Beberapa Contoh** - Memberi contoh dalam prompt. [Modul 02](../02-prompt-engineering/README.md)

**Model Bahasa Besar (LLM)** - Model AI yang dilatih dengan data teks besar.

**Usaha Penalaran** - Parameter GPT-5.2 yang mengawal tahap pemikiran. [Modul 02](../02-prompt-engineering/README.md)

**Suhu** - Mengawal kebebasan output. Rendah=deterministik, tinggi=kreatif.

**Pangkalan Data Vektor** - Pangkalan data khusus untuk penyalinan. [Modul 03](../03-rag/README.md)

**Pembelajaran Tanpa Contoh** - Melaksanakan tugas tanpa contoh. [Modul 02](../02-prompt-engineering/README.md)

## Talian Penjaga - [Modul 00](../00-quick-start/README.md)

**Pertahanan Bertingkat** - Pendekatan keselamatan berlapis menggabungkan talian penjaga peringkat aplikasi dengan penapis keselamatan penyedia.

**Sekatan Keras** - Penyedia membuang ralat HTTP 400 untuk pelanggaran kandungan serius.

**InputGuardrail** - Antara muka LangChain4j untuk mengesahkan input pengguna sebelum sampai ke LLM. Menjimatkan kos dan masa lewat dengan menyekat prompt berbahaya lebih awal.

**InputGuardrailResult** - Jenis pulangan untuk pengesahan talian penjaga: `success()` atau `fatal("reason")`.

**OutputGuardrail** - Antara muka untuk mengesahkan respons AI sebelum dikembalikan kepada pengguna.

**Penapis Keselamatan Penyedia** - Penapis kandungan terbina dalam daripada penyedia AI (contoh: Model GitHub) yang mengesan pelanggaran pada peringkat API.

**Penolakan Lembut** - Model enggan menjawab dengan sopan tanpa membuang ralat.

## Kejuruteraan Prompt - [Modul 02](../02-prompt-engineering/README.md)

**Rantaian Pemikiran** - Penalaran langkah demi langkah untuk ketepatan lebih baik.

**Output Terhad** - Memastikan format atau struktur tertentu.

**Kesungguhan Tinggi** - Corak GPT-5.2 untuk penalaran menyeluruh.

**Kesungguhan Rendah** - Corak GPT-5.2 untuk jawapan cepat.

**Perbualan Berbilang Pusingan** - Mengekalkan konteks merentas pertukaran.

**Prompt Berasaskan Peranan** - Menetapkan persona model melalui mesej sistem.

**Refleksi Diri** - Model menilai dan memperbaiki outputnya.

**Analisis Berstruktur** - Rangka penilaian tetap.

**Corak Pelaksanaan Tugas** - Rancang → Laksanakan → Rumus.

## RAG (Penjanaan Beraugmencarian) - [Modul 03](../03-rag/README.md)

**Saluran Pemprosesan Dokumen** - Muat → pecah → salin → simpan.

**Penyimpanan Penyalinan Dalam Memori** - Penyimpanan tidak kekal untuk ujian.

**RAG** - Menggabungkan pengecaman dengan penjanaan untuk asas respons.

**Skor Persamaan** - Ukuran (0-1) persamaan semantik.

**Rujukan Sumber** - Metadata tentang kandungan yang diambil.

## Ejen dan Alat - [Modul 04](../04-tools/README.md)

**@Tool Annotation** - Menandakan kaedah Java sebagai alat yang boleh dipanggil AI.

**Corak ReAct** - Berfikir → Bertindak → Perhatikan → Ulang.

**Pengurusan Sesi** - Konteks berasingan untuk pengguna berbeza.

**Alat** - Fungsi yang boleh dipanggil oleh ejen AI.

**Penerangan Alat** - Dokumentasi tujuan dan parameter alat.

## Modul Agentik - [Modul 05](../05-mcp/README.md)

**@Agent Annotation** - Menandakan antara muka sebagai ejen AI dengan definisi tingkah laku deklaratif.

**Pendengar Ejen** - Hook untuk memantau pelaksanaan agen melalui `beforeAgentInvocation()` dan `afterAgentInvocation()`.

**Skop Agentik** - Memori kongsi di mana ejen menyimpan output menggunakan `outputKey` untuk digunakan ejen hiliran.

**AgenticServices** - Kilang untuk mencipta agen menggunakan `agentBuilder()` dan `supervisorBuilder()`.

**Aliran Kerja Bersyarat** - Laluan berdasarkan syarat ke agen pakar berbeza.

**Manusia dalam Gelung** - Corak aliran kerja menambah titik pemeriksaan manusia untuk kelulusan atau semakan kandungan.

**langchain4j-agentic** - Pergantungan Maven untuk pembinaan agen deklaratif (eksperimen).

**Aliran Kerja Kitaran** - Ulang pelaksanaan agen sehingga syarat dipenuhi (contoh: skor kualiti ≥ 0.8).

**outputKey** - Parameter anotasi agen yang menentukan di mana keputusan disimpan dalam Skop Agentik.

**Aliran Kerja Selari** - Jalankan banyak agen serentak untuk tugas bebas.

**Strategi Respons** - Bagaimana penyelia membentuk jawapan akhir: TERAKHIR, RINGKASAN, atau DISKOR.

**Aliran Kerja Bersiri** - Melaksanakan agen mengikut urutan di mana output mengalir ke langkah seterusnya.

**Corak Ejen Penyelia** - Corak agentik lanjutan di mana LLM penyelia memutuskan secara dinamik ejen sub mana akan dipanggil.

## Protokol Konteks Model (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** - Pergantungan Maven untuk integrasi MCP dalam LangChain4j.

**MCP** - Protokol Konteks Model: standard untuk menghubungkan aplikasi AI ke alat luaran. Buat sekali, guna di mana-mana.

**Klien MCP** - Aplikasi yang menyambung ke pelayan MCP untuk mencari dan menggunakan alat.

**Pelayan MCP** - Perkhidmatan yang mendedahkan alat melalui MCP dengan penerangan dan skema parameter jelas.

**McpToolProvider** - Komponen LangChain4j yang membalut alat MCP untuk digunakan dalam perkhidmatan AI dan agen.

**McpTransport** - Antara muka untuk komunikasi MCP. Pelaksanaan termasuk Stdio dan HTTP.

**Pengangkutan Stdio** - Pengangkutan proses tempatan melalui stdin/stdout. Berguna untuk akses sistem fail atau alat baris arahan.

**StdioMcpTransport** - Pelaksanaan LangChain4j yang melancarkan pelayan MCP sebagai proses subprocess.

**Penemuan Alat** - Klien bertanya pelayan tentang alat tersedia dengan penerangan dan skema.

## Perkhidmatan Azure - [Modul 01](../01-introduction/README.md)

**Azure AI Search** - Carian awan dengan keupayaan vektor. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Mengurus sumber Azure.

**Azure OpenAI** - Perkhidmatan AI perusahaan Microsoft.

**Bicep** - Bahasa infrastruktur sebagai kod Azure. [Panduan Infrastruktur](../01-introduction/infra/README.md)

**Nama Penempatan** - Nama untuk penempatan model di Azure.

**GPT-5.2** - Model OpenAI terkini dengan kawalan penalaran. [Modul 02](../02-prompt-engineering/README.md)

## Ujian dan Pembangunan - [Panduan Ujian](TESTING.md)

**Dev Container** - Persekitaran pembangunan berbekas. [Konfigurasi](../../../.devcontainer/devcontainer.json)

**Model GitHub** - Taman permainan model AI percuma. [Modul 00](../00-quick-start/README.md)

**Ujian Dalam Memori** - Ujian dengan penyimpanan dalam memori.

**Ujian Integrasi** - Ujian dengan infrastruktur sebenar.

**Maven** - Alat automasi binaan Java.

**Mockito** - Rangka kerja memalsukan Java.

**Spring Boot** - Rangka kerja aplikasi Java. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila ambil perhatian bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya hendaklah dianggap sebagai sumber yang sahih. Untuk maklumat penting, terjemahan profesional oleh manusia adalah disyorkan. Kami tidak bertanggungjawab terhadap sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->