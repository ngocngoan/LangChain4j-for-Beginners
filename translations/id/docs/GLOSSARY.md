# Glosarium LangChain4j

## Daftar Isi

- [Konsep Inti](../../../docs)
- [Komponen LangChain4j](../../../docs)
- [Konsep AI/ML](../../../docs)
- [Guardrails](../../../docs)
- [Rekayasa Prompt](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agen dan Alat](../../../docs)
- [Modul Agenik](../../../docs)
- [Protokol Konteks Model (MCP)](../../../docs)
- [Layanan Azure](../../../docs)
- [Pengujian dan Pengembangan](../../../docs)

Referensi cepat untuk istilah dan konsep yang digunakan sepanjang kursus.

## Konsep Inti

**AI Agent** - Sistem yang menggunakan AI untuk bernalar dan bertindak secara mandiri. [Module 04](../04-tools/README.md)

**Chain** - Urutan operasi di mana keluaran menjadi masukan langkah berikutnya.

**Chunking** - Memecah dokumen menjadi bagian lebih kecil. Biasanya: 300-500 token dengan tumpang tindih. [Module 03](../03-rag/README.md)

**Context Window** - Maksimum token yang dapat diproses model. GPT-5.2: 400K token (hingga 272K input, 128K output).

**Embeddings** - Vektor numerik yang mewakili makna teks. [Module 03](../03-rag/README.md)

**Function Calling** - Model menghasilkan permintaan terstruktur untuk memanggil fungsi eksternal. [Module 04](../04-tools/README.md)

**Hallucination** - Saat model menghasilkan informasi yang salah tapi tampak masuk akal.

**Prompt** - Masukan teks untuk model bahasa. [Module 02](../02-prompt-engineering/README.md)

**Semantic Search** - Pencarian berdasarkan makna dengan embeddings, bukan kata kunci. [Module 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: tanpa memori. Stateful: menyimpan riwayat percakapan. [Module 01](../01-introduction/README.md)

**Tokens** - Unit dasar teks yang diproses model. Mempengaruhi biaya dan batasan. [Module 01](../01-introduction/README.md)

**Tool Chaining** - Eksekusi alat berurutan di mana keluaran memberi informasi untuk panggilan berikutnya. [Module 04](../04-tools/README.md)

## Komponen LangChain4j

**AiServices** - Membuat antarmuka layanan AI yang tipe-safenya terjamin.

**OpenAiOfficialChatModel** - Klien terpadu untuk model OpenAI dan Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Membuat embeddings menggunakan klien OpenAI Official (mendukung OpenAI dan Azure OpenAI).

**ChatModel** - Antarmuka inti untuk model bahasa.

**ChatMemory** - Menyimpan riwayat percakapan.

**ContentRetriever** - Menemukan potongan dokumen yang relevan untuk RAG.

**DocumentSplitter** - Memecah dokumen menjadi potongan.

**EmbeddingModel** - Mengubah teks menjadi vektor numerik.

**EmbeddingStore** - Menyimpan dan mengambil embeddings.

**MessageWindowChatMemory** - Menjaga jendela geser pesan terbaru.

**PromptTemplate** - Membuat prompt yang dapat digunakan ulang dengan placeholder `{{variable}}`.

**TextSegment** - Potongan teks dengan metadata. Digunakan dalam RAG.

**ToolExecutionRequest** - Mewakili permintaan eksekusi alat.

**UserMessage / AiMessage / SystemMessage** - Jenis pesan percakapan.

## Konsep AI/ML

**Few-Shot Learning** - Memberikan contoh dalam prompt. [Module 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - Model AI yang dilatih pada data teks besar.

**Reasoning Effort** - Parameter GPT-5.2 yang mengontrol kedalaman penalaran. [Module 02](../02-prompt-engineering/README.md)

**Temperature** - Mengontrol keacakan keluaran. Rendah=deterministik, tinggi=kreatif.

**Vector Database** - Basis data khusus untuk embeddings. [Module 03](../03-rag/README.md)

**Zero-Shot Learning** - Melakukan tugas tanpa contoh. [Module 02](../02-prompt-engineering/README.md)

## Guardrails - [Module 00](../00-quick-start/README.md)

**Defense in Depth** - Pendekatan keamanan berlapis yang menggabungkan guardrails tingkat aplikasi dengan filter keamanan penyedia.

**Hard Block** - Penyedia memberikan error HTTP 400 untuk pelanggaran konten serius.

**InputGuardrail** - Antarmuka LangChain4j untuk memvalidasi masukan pengguna sebelum diterima LLM. Menghemat biaya dan latensi dengan memblokir prompt berbahaya lebih awal.

**InputGuardrailResult** - Tipe pengembalian untuk validasi guardrail: `success()` atau `fatal("reason")`.

**OutputGuardrail** - Antarmuka untuk memvalidasi respons AI sebelum dikembalikan ke pengguna.

**Provider Safety Filters** - Filter konten bawaan dari penyedia AI (misal, GitHub Models) yang mendeteksi pelanggaran di tingkat API.

**Soft Refusal** - Model dengan sopan menolak menjawab tanpa mengeluarkan error.

## Rekayasa Prompt - [Module 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Penalaran langkah demi langkah untuk akurasi lebih baik.

**Constrained Output** - Memaksa format atau struktur tertentu.

**High Eagerness** - Pola GPT-5.2 untuk penalaran mendalam.

**Low Eagerness** - Pola GPT-5.2 untuk jawaban cepat.

**Multi-Turn Conversation** - Mempertahankan konteks antar pertukaran.

**Role-Based Prompting** - Menetapkan persona model melalui pesan sistem.

**Self-Reflection** - Model mengevaluasi dan meningkatkan keluarannya.

**Structured Analysis** - Kerangka evaluasi tetap.

**Task Execution Pattern** - Rencana → Eksekusi → Ringkasan.

## RAG (Retrieval-Augmented Generation) - [Module 03](../03-rag/README.md)

**Document Processing Pipeline** - Muat → potong → buat embedding → simpan.

**In-Memory Embedding Store** - Penyimpanan non-persisten untuk pengujian.

**RAG** - Menggabungkan pengambilan dengan generasi untuk menguatkan jawaban.

**Similarity Score** - Ukuran (0-1) kesamaan semantik.

**Source Reference** - Metadata tentang konten yang diambil.

## Agen dan Alat - [Module 04](../04-tools/README.md)

**@Tool Annotation** - Menandai metode Java sebagai alat yang dapat dipanggil AI.

**ReAct Pattern** - Bernalar → Bertindak → Mengamati → Ulangi.

**Session Management** - Konteks terpisah untuk pengguna berbeda.

**Tool** - Fungsi yang dapat dipanggil agen AI.

**Tool Description** - Dokumentasi tujuan dan parameter alat.

## Modul Agenik - [Module 05](../05-mcp/README.md)

**@Agent Annotation** - Menandai antarmuka sebagai agen AI dengan definisi perilaku deklaratif.

**Agent Listener** - Hook untuk memantau eksekusi agen melalui `beforeAgentInvocation()` dan `afterAgentInvocation()`.

**Agentic Scope** - Memori bersama tempat agen menyimpan keluaran menggunakan `outputKey` yang dapat digunakan agen lain.

**AgenticServices** - Pabrik untuk membuat agen menggunakan `agentBuilder()` dan `supervisorBuilder()`.

**Conditional Workflow** - Rute berbasis kondisi menuju agen spesialis berbeda.

**Human-in-the-Loop** - Pola alur kerja yang menambahkan titik pemeriksaan manusia untuk persetujuan atau tinjauan konten.

**langchain4j-agentic** - Dependensi Maven untuk pembuatan agen deklaratif (eksperimental).

**Loop Workflow** - Iterasi eksekusi agen sampai kondisi terpenuhi (misal, skor kualitas ≥ 0.8).

**outputKey** - Parameter anotasi agen yang menentukan tempat hasil disimpan di Agentic Scope.

**Parallel Workflow** - Menjalankan beberapa agen secara simultan untuk tugas independen.

**Response Strategy** - Cara supervisor merumuskan jawaban akhir: LAST, SUMMARY, atau SCORED.

**Sequential Workflow** - Menjalankan agen berurutan di mana keluaran mengalir ke langkah berikutnya.

**Supervisor Agent Pattern** - Pola agenik tingkat lanjut di mana supervisor LLM secara dinamis memutuskan sub-agen mana yang dipanggil.

## Protokol Konteks Model (MCP) - [Module 05](../05-mcp/README.md)

**langchain4j-mcp** - Dependensi Maven untuk integrasi MCP di LangChain4j.

**MCP** - Model Context Protocol: standar untuk menghubungkan aplikasi AI ke alat eksternal. Bangun sekali, gunakan di mana saja.

**MCP Client** - Aplikasi yang terhubung ke server MCP untuk menemukan dan menggunakan alat.

**MCP Server** - Layanan yang mengekspos alat melalui MCP dengan deskripsi jelas dan skema parameter.

**McpToolProvider** - Komponen LangChain4j yang membungkus alat MCP untuk digunakan di layanan AI dan agen.

**McpTransport** - Antarmuka untuk komunikasi MCP. Implementasi termasuk Stdio dan HTTP.

**Stdio Transport** - Transport proses lokal melalui stdin/stdout. Berguna untuk akses sistem berkas atau alat command-line.

**StdioMcpTransport** - Implementasi LangChain4j yang menjalankan server MCP sebagai subprocess.

**Tool Discovery** - Klien menanyakan server tentang alat yang tersedia dengan deskripsi dan skema.

## Layanan Azure - [Module 01](../01-introduction/README.md)

**Azure AI Search** - Pencarian cloud dengan kemampuan vektor. [Module 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Men-deploy sumber daya Azure.

**Azure OpenAI** - Layanan AI perusahaan Microsoft.

**Bicep** - Bahasa infrastruktur sebagai kode Azure. [Panduan Infrastruktur](../01-introduction/infra/README.md)

**Deployment Name** - Nama untuk penyebaran model di Azure.

**GPT-5.2** - Model OpenAI terbaru dengan kontrol penalaran. [Module 02](../02-prompt-engineering/README.md)

## Pengujian dan Pengembangan - [Panduan Pengujian](TESTING.md)

**Dev Container** - Lingkungan pengembangan berbasis kontainer. [Konfigurasi](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Playground model AI gratis. [Module 00](../00-quick-start/README.md)

**In-Memory Testing** - Pengujian dengan penyimpanan memori.

**Integration Testing** - Pengujian dengan infrastruktur nyata.

**Maven** - Alat otomasi build Java.

**Mockito** - Framework mocking Java.

**Spring Boot** - Kerangka aplikasi Java. [Module 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berupaya mencapai ketepatan, harap diketahui bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang otoritatif. Untuk informasi penting, disarankan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau penafsiran yang keliru yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->