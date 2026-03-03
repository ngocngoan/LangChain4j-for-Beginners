# Menguji Aplikasi LangChain4j

## Daftar Isi

- [Mulai Cepat](../../../docs)
- [Apa yang Diuji](../../../docs)
- [Menjalankan Tes](../../../docs)
- [Menjalankan Tes di VS Code](../../../docs)
- [Pola Pengujian](../../../docs)
- [Filosofi Pengujian](../../../docs)
- [Langkah Selanjutnya](../../../docs)

Panduan ini memandu Anda melalui tes yang menunjukkan bagaimana menguji aplikasi AI tanpa memerlukan kunci API atau layanan eksternal.

## Mulai Cepat

Jalankan semua tes dengan satu perintah:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

Saat semua tes berhasil, Anda akan melihat output seperti pada tangkapan layar di bawah — tes berjalan tanpa kegagalan.

<img src="../../../translated_images/id/test-results.ea5c98d8f3642043.webp" alt="Hasil Tes Berhasil" width="800"/>

*Eksekusi tes yang berhasil menunjukkan semua tes lulus tanpa kegagalan*

## Apa yang Diuji

Kursus ini berfokus pada **unit test** yang dijalankan secara lokal. Setiap tes menunjukkan konsep LangChain4j tertentu secara terpisah. Piramida pengujian di bawah menunjukkan posisi unit test — mereka membentuk fondasi yang cepat dan andal yang dibangun atas strategi pengujian Anda.

<img src="../../../translated_images/id/testing-pyramid.2dd1079a0481e53e.webp" alt="Piramida Pengujian" width="800"/>

*Piramida pengujian menunjukkan keseimbangan antara unit test (cepat, terisolasi), integration test (komponen asli), dan end-to-end test. Pelatihan ini membahas pengujian unit.*

| Modul | Tes | Fokus | Berkas Utama |
|--------|-------|-------|-----------|
| **00 - Mulai Cepat** | 6 | Template prompt dan substitusi variabel | `SimpleQuickStartTest.java` |
| **01 - Pengantar** | 8 | Memori percakapan dan chat stateful | `SimpleConversationTest.java` |
| **02 - Rekayasa Prompt** | 12 | Pola GPT-5.2, tingkat eagerness, output terstruktur | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Pengolahan dokumen, embeddings, pencarian kemiripan | `DocumentServiceTest.java` |
| **04 - Alat** | 12 | Pemanggilan fungsi dan pengurutan alat | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol dengan transportasi Stdio | `SimpleMcpTest.java` |

## Menjalankan Tes

**Jalankan semua tes dari direktori root:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Jalankan tes untuk modul tertentu:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Atau dari root
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Atau dari root
mvn --% test -pl 01-introduction
```

**Jalankan kelas tes tunggal:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Jalankan metode tes spesifik:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#harusMempertahankanRiwayatPercakapan
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#harusMempertahankanRiwayatPercakapan
```

## Menjalankan Tes di VS Code

Jika Anda menggunakan Visual Studio Code, Test Explorer menyediakan antarmuka grafis untuk menjalankan dan men-debug tes.

<img src="../../../translated_images/id/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer menampilkan pohon tes dengan semua kelas tes Java dan metode tes individu*

**Untuk menjalankan tes di VS Code:**

1. Buka Test Explorer dengan mengklik ikon tabung kimia di Activity Bar
2. Perluas pohon tes untuk melihat semua modul dan kelas tes
3. Klik tombol play di samping tes apa pun untuk menjalankannya secara individual
4. Klik "Run All Tests" untuk menjalankan seluruh suite
5. Klik kanan pada tes apa pun dan pilih "Debug Test" untuk menetapkan breakpoint dan menelusuri kode

Test Explorer menampilkan tanda centang hijau untuk tes yang lulus dan menyediakan pesan kegagalan mendetail saat tes gagal.

## Pola Pengujian

### Pola 1: Menguji Template Prompt

Pola paling sederhana menguji template prompt tanpa memanggil model AI. Anda memverifikasi bahwa substitusi variabel bekerja dengan benar dan prompt diformat sesuai harapan.

<img src="../../../translated_images/id/prompt-template-testing.b902758ddccc8dee.webp" alt="Pengujian Template Prompt" width="800"/>

*Pengujian template prompt menunjukkan alur substitusi variabel: template dengan placeholder → nilai diterapkan → output diformat diverifikasi*

```java
@Test
@DisplayName("Should format prompt template with variables")
void testPromptTemplateFormatting() {
    PromptTemplate template = PromptTemplate.from(
        "Best time to visit {{destination}} for {{activity}}?"
    );
    
    Prompt prompt = template.apply(Map.of(
        "destination", "Paris",
        "activity", "sightseeing"
    ));
    
    assertThat(prompt.text()).isEqualTo("Best time to visit Paris for sightseeing?");
}
```

Tes ini berada di `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Jalankan:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#pengujianFormatTemplatPrompt
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#ujiFormatTemplatePrompt
```

### Pola 2: Mocking Model Bahasa

Saat menguji logika percakapan, gunakan Mockito untuk membuat model palsu yang mengembalikan respons yang sudah ditentukan. Ini membuat tes cepat, gratis, dan deterministik.

<img src="../../../translated_images/id/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Perbandingan Mock vs API Asli" width="800"/>

*Perbandingan yang menunjukkan mengapa mock lebih disukai untuk pengujian: cepat, gratis, deterministik, dan tidak memerlukan kunci API*

```java
@ExtendWith(MockitoExtension.class)
class SimpleConversationTest {
    
    private ConversationService conversationService;
    
    @Mock
    private OpenAiOfficialChatModel mockChatModel;
    
    @BeforeEach
    void setUp() {
        ChatResponse mockResponse = ChatResponse.builder()
            .aiMessage(AiMessage.from("This is a test response"))
            .build();
        when(mockChatModel.chat(anyList())).thenReturn(mockResponse);
        
        conversationService = new ConversationService(mockChatModel);
    }
    
    @Test
    void shouldMaintainConversationHistory() {
        String conversationId = conversationService.startConversation();
        
        ChatResponse mockResponse1 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 1"))
            .build();
        ChatResponse mockResponse2 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 2"))
            .build();
        ChatResponse mockResponse3 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 3"))
            .build();
        
        when(mockChatModel.chat(anyList()))
            .thenReturn(mockResponse1)
            .thenReturn(mockResponse2)
            .thenReturn(mockResponse3);

        conversationService.chat(conversationId, "First message");
        conversationService.chat(conversationId, "Second message");
        conversationService.chat(conversationId, "Third message");

        List<ChatMessage> history = conversationService.getHistory(conversationId);
        assertThat(history).hasSize(6); // 3 pesan pengguna + 3 pesan AI
    }
}
```

Pola ini muncul di `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mock memastikan perilaku konsisten sehingga Anda dapat memverifikasi pengelolaan memori berfungsi dengan benar.

### Pola 3: Menguji Isolasi Percakapan

Memori percakapan harus menjaga beberapa pengguna tetap terpisah. Tes ini memverifikasi bahwa percakapan tidak mencampur konteks.

<img src="../../../translated_images/id/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Isolasi Percakapan" width="800"/>

*Pengujian isolasi percakapan menunjukkan penyimpanan memori terpisah untuk pengguna berbeda agar konteks tidak tercampur*

```java
@Test
void shouldIsolateConversationsByid() {
    String conv1 = conversationService.startConversation();
    String conv2 = conversationService.startConversation();
    
    ChatResponse mockResponse = ChatResponse.builder()
        .aiMessage(AiMessage.from("Response"))
        .build();
    when(mockChatModel.chat(anyList())).thenReturn(mockResponse);

    conversationService.chat(conv1, "Message for conversation 1");
    conversationService.chat(conv2, "Message for conversation 2");

    List<ChatMessage> history1 = conversationService.getHistory(conv1);
    List<ChatMessage> history2 = conversationService.getHistory(conv2);
    
    assertThat(history1).hasSize(2);
    assertThat(history2).hasSize(2);
}
```

Setiap percakapan mempertahankan riwayat independennya sendiri. Dalam sistem produksi, isolasi ini sangat penting untuk aplikasi multi-pengguna.

### Pola 4: Menguji Alat Secara Mandiri

Alat adalah fungsi yang dapat dipanggil AI. Uji langsung untuk memastikan alat berfungsi dengan benar tanpa bergantung pada keputusan AI.

<img src="../../../translated_images/id/tools-testing.3e1706817b0b3924.webp" alt="Pengujian Alat" width="800"/>

*Pengujian alat secara mandiri memperlihatkan eksekusi alat mock tanpa panggilan AI untuk memverifikasi logika bisnis*

```java
@Test
void shouldConvertCelsiusToFahrenheit() {
    TemperatureTool tempTool = new TemperatureTool();
    String result = tempTool.celsiusToFahrenheit(25.0);
    assertThat(result).containsPattern("77[.,]0°F");
}

@Test
void shouldDemonstrateToolChaining() {
    WeatherTool weatherTool = new WeatherTool();
    TemperatureTool tempTool = new TemperatureTool();

    String weatherResult = weatherTool.getCurrentWeather("Seattle");
    assertThat(weatherResult).containsPattern("\\d+°C");

    String conversionResult = tempTool.celsiusToFahrenheit(22.0);
    assertThat(conversionResult).containsPattern("71[.,]6°F");
}
```

Tes ini dari `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` memvalidasi logika alat tanpa keterlibatan AI. Contoh chaining menunjukkan bagaimana output alat satu menjadi input alat lain.

### Pola 5: Pengujian RAG In-Memory

Sistem RAG biasanya memerlukan basis data vektor dan layanan embedding. Pola in-memory memungkinkan pengujian alur lengkap tanpa ketergantungan eksternal.

<img src="../../../translated_images/id/rag-testing.ee7541b1e23934b1.webp" alt="Pengujian RAG In-Memory" width="800"/>

*Alur kerja pengujian RAG in-memory menunjukkan parsing dokumen, penyimpanan embedding, dan pencarian kemiripan tanpa memerlukan database*

```java
@Test
void testProcessTextDocument() {
    String content = "This is a test document.\nIt has multiple lines.";
    InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    
    DocumentService.ProcessedDocument result = 
        documentService.processDocument(inputStream, "test.txt");

    assertNotNull(result);
    assertTrue(result.segments().size() > 0);
    assertEquals("test.txt", result.segments().get(0).metadata().getString("filename"));
}
```

Tes ini dari `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` membuat dokumen di memori dan memverifikasi pemisahan chunk dan penanganan metadata.

### Pola 6: Pengujian Integrasi MCP

Modul MCP menguji integrasi Model Context Protocol menggunakan transportasi stdio. Tes ini memverifikasi aplikasi Anda dapat memunculkan dan berkomunikasi dengan server MCP sebagai subprocess.

Tes di `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` memvalidasi perilaku klien MCP.

**Jalankan:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Filosofi Pengujian

Uji kode Anda, bukan AI-nya. Tes Anda harus memvalidasi kode yang Anda tulis dengan memeriksa bagaimana prompt dibuat, bagaimana memori dikelola, dan bagaimana alat dieksekusi. Respons AI bervariasi dan tidak boleh menjadi bagian dari pernyataan tes. Tanyakan pada diri Anda apakah template prompt Anda menggantikan variabel dengan benar, bukan apakah AI memberikan jawaban yang benar.

Gunakan mock untuk model bahasa. Mereka adalah dependensi eksternal yang lambat, mahal, dan tidak deterministik. Mock membuat tes cepat dengan waktu mili-detik daripada detik, gratis tanpa biaya API, dan deterministik dengan hasil yang sama setiap kali.

Jaga tes agar tetap independen. Setiap tes harus menyiapkan datanya sendiri, tidak bergantung pada tes lain, dan membersihkan setelahnya. Tes harus lulus terlepas dari urutan eksekusi.

Uji kasus tepi di luar jalur bahagia. Coba input kosong, input sangat besar, karakter khusus, parameter tidak valid, dan kondisi batas. Ini sering mengungkap bug yang tidak tampak pada penggunaan biasa.

Gunakan nama yang deskriptif. Bandingkan `shouldMaintainConversationHistoryAcrossMultipleMessages()` dengan `test1()`. Yang pertama memberi tahu Anda persis apa yang diuji, membuat debugging kegagalan jauh lebih mudah.

## Langkah Selanjutnya

Sekarang Anda memahami pola pengujian, gali lebih dalam setiap modul:

- **[00 - Mulai Cepat](../00-quick-start/README.md)** - Mulai dengan dasar template prompt
- **[01 - Pengantar](../01-introduction/README.md)** - Pelajari pengelolaan memori percakapan
- **[02 - Rekayasa Prompt](../02-prompt-engineering/README.md)** - Kuasai pola pemicu GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Bangun sistem retrieval-augmented generation
- **[04 - Alat](../04-tools/README.md)** - Implementasikan pemanggilan fungsi dan rangkaian alat
- **[05 - MCP](../05-mcp/README.md)** - Integrasikan Model Context Protocol

README setiap modul menyediakan penjelasan mendetail mengenai konsep yang diuji di sini.

---

**Navigasi:** [← Kembali ke Utama](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan penerjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berusaha untuk akurasi, harap diketahui bahwa terjemahan otomatis mungkin mengandung kesalahan atau kekeliruan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang sah dan utama. Untuk informasi yang bersifat penting, disarankan menggunakan jasa penerjemah profesional manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau kesalahan interpretasi yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->