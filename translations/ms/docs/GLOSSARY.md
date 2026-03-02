# Glosari LangChain4j

## Jadual Kandungan

- [Konsep Teras](../../../docs)
- [Komponen LangChain4j](../../../docs)
- [Konsep AI/ML](../../../docs)
- [Guardrails](../../../docs)
- [Kejuruteraan Prompt](../../../docs)
- [RAG (Generasi Beraugmen Ambilan)](../../../docs)
- [Ejen dan Alat](../../../docs)
- [Modul Agentik](../../../docs)
- [Protokol Konteks Model (MCP)](../../../docs)
- [Perkhidmatan Azure](../../../docs)
- [Ujian dan Pembangunan](../../../docs)

Rujukan pantas untuk terma dan konsep yang digunakan sepanjang kursus.

## Konsep Teras

**Ejen AI** - Sistem yang menggunakan AI untuk berfikir dan bertindak secara autonomi. [Modul 04](../04-tools/README.md)

**Rantaian** - Urutan operasi di mana output menjadi input ke langkah seterusnya.

**Pengpecahan** - Memecah dokumen menjadi bahagian yang lebih kecil. Biasanya: 300-500 token dengan tumpang tindih. [Modul 03](../03-rag/README.md)

**Tingkap Konteks** - Maksimum token yang boleh diproses oleh model. GPT-5.2: 400K token (hingga 272K input, 128K output).

**Penempatan** - Vektor angka yang mewakili maksud teks. [Modul 03](../03-rag/README.md)

**Panggilan Fungsi** - Model menjana permintaan berstruktur untuk memanggil fungsi luaran. [Modul 04](../04-tools/README.md)

**Halusinasi** - Apabila model menjana maklumat yang salah tetapi munasabah.

**Prompt** - Input teks kepada model bahasa. [Modul 02](../02-prompt-engineering/README.md)

**Carian Semantik** - Carian berdasarkan makna menggunakan penempatan, bukan kata kunci. [Modul 03](../03-rag/README.md)

**Bersifat Stateful vs Stateless** - Stateless: tiada memori. Stateful: mengekalkan sejarah perbualan. [Modul 01](../01-introduction/README.md)

**Token** - Unit asas teks yang diproses model. Mempengaruhi kos dan had. [Modul 01](../01-introduction/README.md)

**Rantaian Alat** - Pelaksanaan alat berturutan di mana output memaklumkan panggilan seterusnya. [Modul 04](../04-tools/README.md)

## Komponen LangChain4j

**AiServices** - Mencipta antara muka perkhidmatan AI yang selamat jenis.

**OpenAiOfficialChatModel** - Pelanggan sekata untuk model OpenAI dan Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Mencipta penempatan menggunakan pelanggan Rasmi OpenAI (menyokong OpenAI dan Azure OpenAI).

**ChatModel** - Antara muka teras untuk model bahasa.

**ChatMemory** - Mengekalkan sejarah perbualan.

**ContentRetriever** - Mencari bahagian dokumen yang relevan untuk RAG.

**DocumentSplitter** - Memecah dokumen kepada bahagian.

**EmbeddingModel** - Menukar teks ke vektor angka.

**EmbeddingStore** - Menyimpan dan mengambil penempatan.

**MessageWindowChatMemory** - Mengekalkan tetingkap gelongsor mesej terkini.

**PromptTemplate** - Mencipta prompt boleh guna semula dengan ruang letak `{{variable}}`.

**TextSegment** - Segmen teks dengan metadata. Digunakan dalam RAG.

**ToolExecutionRequest** - Mewakili permintaan pelaksanaan alat.

**UserMessage / AiMessage / SystemMessage** - Jenis mesej perbualan.

## Konsep AI/ML

**Pembelajaran Few-Shot** - Memberi contoh dalam prompt. [Modul 02](../02-prompt-engineering/README.md)

**Model Bahasa Besar (LLM)** - Model AI yang dilatih pada data teks besar.

**Usaha Penalaran** - Parameter GPT-5.2 yang mengawal kedalaman pemikiran. [Modul 02](../02-prompt-engineering/README.md)

**Suhu** - Mengawal rawak output. Rendah=deterministik, tinggi=kreatif.

**Pangkalan Data Vektor** - Pangkalan data khusus untuk penempatan. [Modul 03](../03-rag/README.md)

**Pembelajaran Zero-Shot** - Melaksanakan tugasan tanpa contoh. [Modul 02](../02-prompt-engineering/README.md)

## Guardrails - [Modul 00](../00-quick-start/README.md)

**Pertahanan Bertingkat** - Pendekatan keselamatan berlapis yang menggabungkan guardrails pada tingkat aplikasi dengan penapis keselamatan penyedia.

**Sekatan Keras** - Penyedia membuang ralat HTTP 400 untuk pelanggaran kandungan yang serius.

**InputGuardrail** - Antara muka LangChain4j untuk mengesahkan input pengguna sebelum sampai ke LLM. Menjimatkan kos dan kelewatan dengan menyekat prompt berbahaya awal.

**InputGuardrailResult** - Jenis pulangan untuk pengesahan guardrail: `success()` atau `fatal("reason")`.

**OutputGuardrail** - Antara muka untuk mengesahkan respons AI sebelum dikembalikan kepada pengguna.

**Penapis Keselamatan Penyedia** - Penapis kandungan terbina dalam daripada penyedia AI (contoh: GitHub Models) yang mengesan pelanggaran pada tahap API.

**Penolakan Lembut** - Model dengan sopan menolak menjawab tanpa membuang ralat.

## Kejuruteraan Prompt - [Modul 02](../02-prompt-engineering/README.md)

**Rantaian Pemikiran** - Penalaran langkah demi langkah untuk ketepatan lebih baik.

**Output Terhad** - Memaksa format atau struktur tertentu.

**Keghairahan Tinggi** - Corak GPT-5.2 untuk penalaran menyeluruh.

**Keghairahan Rendah** - Corak GPT-5.2 untuk jawapan cepat.

**Perbualan Berbilang Giliran** - Mengekalkan konteks sepanjang pertukaran.

**Prompt Berdasarkan Peranan** - Menetapkan persona model melalui mesej sistem.

**Refleksi Diri** - Model menilai dan memperbaiki outputnya.

**Analisis Berstruktur** - Rangka penilaian tetap.

**Corak Pelaksanaan Tugasan** - Rancang → Laksanakan → Rumuskan.

## RAG (Generasi Beraugmen Ambilan) - [Modul 03](../03-rag/README.md)

**Laluan Pemprosesan Dokumen** - Muat → pecah → tanam → simpan.

**Kedai Penempatan Dalam Memori** - Penyimpanan tidak kekal untuk ujian.

**RAG** - Menggabungkan ambilan dengan generasi untuk mendasari jawapan.

**Skor Kesamaan** - Ukuran (0-1) kesamaan semantik.

**Rujukan Sumber** - Metadata tentang kandungan yang diambil.

## Ejen dan Alat - [Modul 04](../04-tools/README.md)

**@Tool Annotation** - Menandakan kaedah Java sebagai alat yang boleh dipanggil AI.

**Corak ReAct** - Berfikir → Bertindak → Memerhati → Ulang.

**Pengurusan Sesi** - Konteks berasingan untuk pengguna berbeza.

**Alat** - Fungsi yang boleh dipanggil oleh ejen AI.

**Penerangan Alat** - Dokumentasi tujuan dan parameter alat.

## Modul Agentik - [Modul 05](../05-mcp/README.md)

**@Agent Annotation** - Menandakan antara muka sebagai ejen AI dengan definisi kelakuan deklaratif.

**Pendengar Ejen** - Hook untuk memantau pelaksanaan ejen melalui `beforeAgentInvocation()` dan `afterAgentInvocation()`.

**Skop Agentik** - Memori dikongsi di mana ejen menyimpan output menggunakan `outputKey` untuk diproses ejen hiliran.

**AgenticServices** - Kilang untuk mencipta ejen menggunakan `agentBuilder()` dan `supervisorBuilder()`.

**Aliran Kerja Bersyarat** - Laluan berdasarkan syarat ke ejen pakar berbeza.

**Manusia Dalam Gelung** - Corak aliran kerja menambah titik pemeriksaan manusia untuk kelulusan atau semakan kandungan.

**langchain4j-agentic** - Kebergantungan Maven untuk pembinaan ejen deklaratif (eksperimen).

**Aliran Kerja Gelung** - Ulang pelaksanaan ejen sehingga syarat dipenuhi (contoh: skor kualiti ≥ 0.8).

**outputKey** - Parameter pengecaman ejen yang menentukan di mana keputusan disimpan dalam Skop Agentik.

**Aliran Kerja Selari** - Jalankan beberapa ejen serentak untuk tugasan bebas.

**Strategi Respons** - Cara penyelia merumuskan jawapan akhir: TERAKHIR, RINGKASAN, atau BOARD.

**Aliran Kerja Bersiri** - Laksanakan ejen mengikut urutan di mana output mengalir ke langkah seterusnya.

**Corak Ejen Penyelia** - Corak agentik canggih di mana LLM penyelia menentukan secara dinamik sub-ejen yang akan dipanggil.

## Protokol Konteks Model (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** - Kebergantungan Maven untuk integrasi MCP dalam LangChain4j.

**MCP** - Protokol Konteks Model: standard untuk menyambungkan aplikasi AI kepada alat luaran. Bina sekali, guna di mana sahaja.

**Pelanggan MCP** - Aplikasi yang menyambung ke pelayan MCP untuk menemui dan menggunakan alat.

**Pelayan MCP** - Perkhidmatan yang mendedahkan alat melalui MCP dengan penerangan jelas dan skema parameter.

**McpToolProvider** - Komponen LangChain4j yang membalut alat MCP untuk digunakan dalam perkhidmatan dan ejen AI.

**McpTransport** - Antara muka untuk komunikasi MCP. Implementasi termasuk Stdio dan HTTP.

**Pengangkutan Stdio** - Pengangkutan proses tempatan melalui stdin/stdout. Berguna untuk akses sistem fail atau alat baris arahan.

**StdioMcpTransport** - Pelaksanaan LangChain4j yang memulakan pelayan MCP sebagai proses sub.

**Penemuan Alat** - Pelanggan bertanya pelayan tentang alat tersedia dengan penerangan dan skema.

## Perkhidmatan Azure - [Modul 01](../01-introduction/README.md)

**Azure AI Search** - Carian awan dengan keupayaan vektor. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Mengedar sumber Azure.

**Azure OpenAI** - Perkhidmatan AI perusahaan Microsoft.

**Bicep** - Bahasa infrastruktur sebagai kod Azure. [Panduan Infrastruktur](../01-introduction/infra/README.md)

**Nama Pengedaran** - Nama untuk pengedaran model di Azure.

**GPT-5.2** - Model OpenAI terkini dengan kawalan penalaran. [Modul 02](../02-prompt-engineering/README.md)

## Ujian dan Pembangunan - [Panduan Ujian](TESTING.md)

**Dev Container** - Persekitaran pembangunan berasaskan kontena. [Konfigurasi](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Tapak percubaan model AI percuma. [Modul 00](../00-quick-start/README.md)

**Ujian Dalam Memori** - Ujian dengan storan dalam memori.

**Ujian Integrasi** - Ujian dengan infrastruktur sebenar.

**Maven** - Alat automasi binaan Java.

**Mockito** - Kerangka kerja pemalsuan Java.

**Spring Boot** - Rangka aplikasi Java. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk memberikan ketepatan, sila ambil maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang sahih. Untuk maklumat penting, terjemahan profesional oleh manusia adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->