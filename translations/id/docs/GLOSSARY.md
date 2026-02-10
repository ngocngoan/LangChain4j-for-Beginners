# Glosarium LangChain4j

## Daftar Isi

- [Konsep Inti](../../../docs)
- [Komponen LangChain4j](../../../docs)
- [Konsep AI/ML](../../../docs)
- [Guardrails](../../../docs)
- [Rekayasa Prompt](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agen dan Alat](../../../docs)
- [Modul Agen](../../../docs)
- [Protokol Konteks Model (MCP)](../../../docs)
- [Layanan Azure](../../../docs)
- [Pengujian dan Pengembangan](../../../docs)

Referensi cepat untuk istilah dan konsep yang digunakan sepanjang kursus.

## Konsep Inti

**AI Agent** - Sistem yang menggunakan AI untuk bernalar dan bertindak secara mandiri. [Modul 04](../04-tools/README.md)

**Chain** - Urutan operasi di mana keluaran memasok langkah berikutnya.

**Chunking** - Memecah dokumen menjadi potongan lebih kecil. Umumnya: 300-500 token dengan tumpang tindih. [Modul 03](../03-rag/README.md)

**Context Window** - Maksimum token yang dapat diproses oleh model. GPT-5.2: 400K token.

**Embeddings** - Vektor numerik yang mewakili makna teks. [Modul 03](../03-rag/README.md)

**Function Calling** - Model menghasilkan permintaan terstruktur untuk memanggil fungsi eksternal. [Modul 04](../04-tools/README.md)

**Hallucination** - Saat model menghasilkan informasi yang salah tapi tampak masuk akal.

**Prompt** - Input teks ke model bahasa. [Modul 02](../02-prompt-engineering/README.md)

**Semantic Search** - Pencarian berdasarkan makna menggunakan embeddings, bukan kata kunci. [Modul 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: tanpa memori. Stateful: mempertahankan riwayat percakapan. [Modul 01](../01-introduction/README.md)

**Tokens** - Unit teks dasar yang diproses model. Mempengaruhi biaya dan batas. [Modul 01](../01-introduction/README.md)

**Tool Chaining** - Eksekusi alat secara berurutan di mana keluaran menginformasikan panggilan berikutnya. [Modul 04](../04-tools/README.md)

## Komponen LangChain4j

**AiServices** - Membuat antarmuka layanan AI yang aman tipe.

**OpenAiOfficialChatModel** - Klien terpadu untuk model OpenAI dan Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Membuat embeddings menggunakan klien OpenAI Official (mendukung OpenAI dan Azure OpenAI).

**ChatModel** - Antarmuka inti untuk model bahasa.

**ChatMemory** - Mempertahankan riwayat percakapan.

**ContentRetriever** - Menemukan potongan dokumen relevan untuk RAG.

**DocumentSplitter** - Memecah dokumen menjadi potongan.

**EmbeddingModel** - Mengonversi teks menjadi vektor numerik.

**EmbeddingStore** - Menyimpan dan mengambil embeddings.

**MessageWindowChatMemory** - Mempertahankan jendela geser pesan terbaru.

**PromptTemplate** - Membuat prompt yang dapat digunakan ulang dengan placeholder `{{variable}}`.

**TextSegment** - Potongan teks dengan metadata. Digunakan di RAG.

**ToolExecutionRequest** - Mewakili permintaan eksekusi alat.

**UserMessage / AiMessage / SystemMessage** - Jenis pesan percakapan.

## Konsep AI/ML

**Few-Shot Learning** - Memberikan contoh dalam prompt. [Modul 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - Model AI yang dilatih pada data teks besar.

**Reasoning Effort** - Parameter GPT-5.2 yang mengendalikan tingkat pemikiran. [Modul 02](../02-prompt-engineering/README.md)

**Temperature** - Mengatur keacakan keluaran. Rendah=deterministik, tinggi=kreatif.

**Vector Database** - Basis data khusus untuk embeddings. [Modul 03](../03-rag/README.md)

**Zero-Shot Learning** - Melakukan tugas tanpa contoh. [Modul 02](../02-prompt-engineering/README.md)

## Guardrails - [Modul 00](../00-quick-start/README.md)

**Defense in Depth** - Pendekatan keamanan berlapis yang menggabungkan guardrails tingkat aplikasi dengan filter keamanan penyedia.

**Hard Block** - Penyedia melemparkan error HTTP 400 untuk pelanggaran konten serius.

**InputGuardrail** - Antarmuka LangChain4j untuk memvalidasi input pengguna sebelum mencapai LLM. Menghemat biaya dan latensi dengan memblokir prompt berbahaya lebih awal.

**InputGuardrailResult** - Tipe pengembalian untuk validasi guardrail: `success()` atau `fatal("reason")`.

**OutputGuardrail** - Antarmuka untuk memvalidasi respons AI sebelum dikembalikan ke pengguna.

**Provider Safety Filters** - Filter konten bawaan dari penyedia AI (misal: GitHub Models) yang menangkap pelanggaran di tingkat API.

**Soft Refusal** - Model menolak menjawab secara sopan tanpa melempar error.

## Rekayasa Prompt - [Modul 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Penalaran langkah demi langkah untuk akurasi lebih baik.

**Constrained Output** - Memaksakan format atau struktur tertentu.

**High Eagerness** - Pola GPT-5.2 untuk penalaran mendalam.

**Low Eagerness** - Pola GPT-5.2 untuk jawaban cepat.

**Multi-Turn Conversation** - Mempertahankan konteks sepanjang percakapan.

**Role-Based Prompting** - Menetapkan persona model melalui pesan sistem.

**Self-Reflection** - Model mengevaluasi dan memperbaiki keluarannya.

**Structured Analysis** - Kerangka evaluasi tetap.

**Task Execution Pattern** - Rencana → Eksekusi → Ringkasan.

## RAG (Retrieval-Augmented Generation) - [Modul 03](../03-rag/README.md)

**Document Processing Pipeline** - Muat → pecah → embed → simpan.

**In-Memory Embedding Store** - Penyimpanan non-persistent untuk pengujian.

**RAG** - Menggabungkan pengambilan dengan generasi untuk mendasarkan respons.

**Similarity Score** - Ukuran (0-1) kesamaan semantik.

**Source Reference** - Metadata tentang konten yang diambil.

## Agen dan Alat - [Modul 04](../04-tools/README.md)

**@Tool Annotation** - Menandai metode Java sebagai alat yang dapat dipanggil AI.

**ReAct Pattern** - Razon → Bertindak → Mengamati → Ulangi.

**Session Management** - Konteks terpisah untuk pengguna berbeda.

**Tool** - Fungsi yang dapat dipanggil agen AI.

**Tool Description** - Dokumentasi tentang tujuan dan parameter alat.

## Modul Agen - [Modul 05](../05-mcp/README.md)

**@Agent Annotation** - Menandai antarmuka sebagai agen AI dengan definisi perilaku deklaratif.

**Agent Listener** - Hook untuk memantau eksekusi agen lewat `beforeAgentInvocation()` dan `afterAgentInvocation()`.

**Agentic Scope** - Memori bersama di mana agen menyimpan keluaran menggunakan `outputKey` untuk dikonsumsi agen berikutnya.

**AgenticServices** - Pabrik untuk membuat agen menggunakan `agentBuilder()` dan `supervisorBuilder()`.

**Conditional Workflow** - Rute berdasarkan kondisi ke agen spesialis berbeda.

**Human-in-the-Loop** - Pola alur kerja dengan checkpoint manusia untuk persetujuan atau tinjauan konten.

**langchain4j-agentic** - Dependency Maven untuk pembangunan agen deklaratif (eksperimental).

**Loop Workflow** - Mengulangi eksekusi agen sampai kondisi terpenuhi (misalnya score kualitas ≥ 0.8).

**outputKey** - Parameter anotasi agen yang menentukan tempat penyimpanan hasil di Agentic Scope.

**Parallel Workflow** - Menjalankan banyak agen secara bersamaan untuk tugas independen.

**Response Strategy** - Cara supervisor merumuskan jawaban akhir: LAST, SUMMARY, atau SCORED.

**Sequential Workflow** - Menjalankan agen berurutan dengan keluaran mengalir ke langkah berikutnya.

**Supervisor Agent Pattern** - Pola agen canggih di mana supervisor LLM memutuskan secara dinamis agen sub mana yang diaktifkan.

## Protokol Konteks Model (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** - Dependency Maven untuk integrasi MCP di LangChain4j.

**MCP** - Model Context Protocol: standar untuk menghubungkan aplikasi AI ke alat eksternal. Bangun sekali, gunakan di mana-mana.

**MCP Client** - Aplikasi yang terhubung ke server MCP untuk menemukan dan menggunakan alat.

**MCP Server** - Layanan yang mengekspos alat melalui MCP dengan deskripsi dan skema parameter yang jelas.

**McpToolProvider** - Komponen LangChain4j yang membungkus alat MCP untuk digunakan di layanan AI dan agen.

**McpTransport** - Antarmuka untuk komunikasi MCP. Implementasi termasuk Stdio dan HTTP.

**Stdio Transport** - Transport proses lokal via stdin/stdout. Berguna untuk akses sistem file atau alat command-line.

**StdioMcpTransport** - Implementasi LangChain4j yang menjalankan server MCP sebagai subprocess.

**Tool Discovery** - Klien menanyakan server alat yang tersedia dengan deskripsi dan skema.

## Layanan Azure - [Modul 01](../01-introduction/README.md)

**Azure AI Search** - Pencarian cloud dengan kemampuan vektor. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Mendeploy resource Azure.

**Azure OpenAI** - Layanan AI enterprise Microsoft.

**Bicep** - Bahasa infrastruktur-as-code Azure. [Panduan Infrastruktur](../01-introduction/infra/README.md)

**Deployment Name** - Nama untuk penempatan model di Azure.

**GPT-5.2** - Model OpenAI terbaru dengan kontrol penalaran. [Modul 02](../02-prompt-engineering/README.md)

## Pengujian dan Pengembangan - [Panduan Pengujian](TESTING.md)

**Dev Container** - Lingkungan pengembangan yang tercontainerisasi. [Konfigurasi](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Playground model AI gratis. [Modul 00](../00-quick-start/README.md)

**In-Memory Testing** - Pengujian dengan penyimpanan dalam memori.

**Integration Testing** - Pengujian dengan infrastruktur nyata.

**Maven** - Alat otomatisasi build Java.

**Mockito** - Framework mocking Java.

**Spring Boot** - Framework aplikasi Java. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berupaya untuk mencapai akurasi, harap diingat bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber otoritatif. Untuk informasi penting, disarankan menggunakan terjemahan profesional oleh penerjemah manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau salah tafsir yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->