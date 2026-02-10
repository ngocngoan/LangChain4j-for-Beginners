# Menguji Aplikasi LangChain4j

## Daftar Isi

- [Mulai Cepat](../../../docs)
- [Apa yang Dicakup oleh Tes](../../../docs)
- [Menjalankan Tes](../../../docs)
- [Menjalankan Tes di VS Code](../../../docs)
- [Polanya Pengujian](../../../docs)
- [Filosofi Pengujian](../../../docs)
- [Langkah Berikutnya](../../../docs)

Panduan ini memandu Anda melalui tes yang menunjukkan cara menguji aplikasi AI tanpa memerlukan kunci API atau layanan eksternal.

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

<img src="../../../translated_images/id/test-results.ea5c98d8f3642043.webp" alt="Hasil Tes Berhasil" width="800"/>

*Eksekusi tes berhasil yang menunjukkan semua tes lulus tanpa kegagalan*

## Apa yang Dicakup oleh Tes

Kursus ini fokus pada **unit tests** yang dijalankan secara lokal. Setiap tes menunjukkan konsep LangChain4j tertentu secara terpisah.

<img src="../../../translated_images/id/testing-pyramid.2dd1079a0481e53e.webp" alt="Piramida Pengujian" width="800"/>

*Piramida pengujian menunjukkan keseimbangan antara unit tests (cepat, terisolasi), integration tests (komponen nyata), dan end-to-end tests. Pelatihan ini mencakup pengujian unit.*

| Modul | Tes | Fokus | File Utama |
|--------|-------|-------|-----------|
| **00 - Mulai Cepat** | 6 | Template prompt dan substitusi variabel | `SimpleQuickStartTest.java` |
| **01 - Pengantar** | 8 | Memori percakapan dan chat berstatus | `SimpleConversationTest.java` |
| **02 - Rekayasa Prompt** | 12 | Pola GPT-5.2, tingkat antusiasme, output terstruktur | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Pengambilan dokumen, embeddings, pencarian kemiripan | `DocumentServiceTest.java` |
| **04 - Alat** | 12 | Pemanggilan fungsi dan pengaitan alat | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol dengan transport stdio | `SimpleMcpTest.java` |

## Menjalankan Tes

**Jalankan semua tes dari root:**

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

**Jalankan satu kelas tes:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Jalankan metode tes tertentu:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#harusMempertahankanRiwayatPercakapan
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#harusMempertahankanRiwayatPercakapan
```

## Menjalankan Tes di VS Code

Jika Anda menggunakan Visual Studio Code, Test Explorer menyediakan antarmuka grafis untuk menjalankan dan debug tes.

<img src="../../../translated_images/id/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer menampilkan pohon tes dengan semua kelas tes Java dan metode tes individual*

**Untuk menjalankan tes di VS Code:**

1. Buka Test Explorer dengan mengklik ikon tabung reaksi di Activity Bar
2. Perluas pohon tes untuk melihat semua modul dan kelas tes
3. Klik tombol play di samping tes mana pun untuk menjalankannya secara individual
4. Klik "Run All Tests" untuk menjalankan seluruh suite
5. Klik kanan tes mana pun dan pilih "Debug Test" untuk menempatkan breakpoint dan menelusuri kode

Test Explorer menampilkan tanda centang hijau untuk tes yang lulus dan menyediakan pesan kegagalan rinci saat tes gagal.

## Polanya Pengujian

### Pola 1: Menguji Template Prompt

Pola paling sederhana menguji template prompt tanpa memanggil model AI apa pun. Anda memverifikasi bahwa substitusi variabel bekerja dengan benar dan prompt diformat sesuai harapan.

<img src="../../../translated_images/id/prompt-template-testing.b902758ddccc8dee.webp" alt="Pengujian Template Prompt" width="800"/>

*Pengujian template prompt menunjukkan alur substitusi variabel: template dengan placeholder → nilai diterapkan → output format diverifikasi*

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

Tes ini ada di `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Jalankan:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#mengujiFormatTemplatePrompt
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#pengujianFormatTemplatePrompt
```

### Pola 2: Memalsukan Model Bahasa

Saat menguji logika percakapan, gunakan Mockito untuk membuat model palsu yang mengembalikan respons yang sudah ditentukan sebelumnya. Ini membuat tes cepat, gratis, dan deterministik.

<img src="../../../translated_images/id/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Perbandingan Mock vs API Asli" width="800"/>

*Perbandingan menunjukkan mengapa mock lebih dipilih untuk pengujian: mereka cepat, gratis, deterministik, dan tidak memerlukan kunci API*

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

Pola ini muncul di `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mock memastikan perilaku konsisten sehingga Anda bisa memverifikasi manajemen memori bekerja dengan benar.

### Pola 3: Menguji Isolasi Percakapan

Memori percakapan harus menjaga beberapa pengguna tetap terpisah. Tes ini memverifikasi bahwa percakapan tidak mencampur konteks.

<img src="../../../translated_images/id/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Isolasi Percakapan" width="800"/>

*Pengujian isolasi percakapan menunjukkan penyimpanan memori terpisah untuk pengguna berbeda guna mencegah pencampuran konteks*

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

Setiap percakapan mempertahankan riwayatnya sendiri secara independen. Dalam sistem produksi, isolasi ini penting untuk aplikasi multi-pengguna.

### Pola 4: Menguji Alat Secara Mandiri

Alat adalah fungsi yang dapat dipanggil AI. Uji langsung untuk memastikan alat bekerja dengan benar terlepas dari keputusan AI.

<img src="../../../translated_images/id/tools-testing.3e1706817b0b3924.webp" alt="Pengujian Alat" width="800"/>

*Pengujian alat secara mandiri menunjukkan eksekusi alat mock tanpa pemanggilan AI untuk memverifikasi logika bisnis*

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

Tes ini dari `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` memvalidasi logika alat tanpa keterlibatan AI. Contoh pengaitan menunjukkan bagaimana output alat satu masuk ke input alat lain.

### Pola 5: Pengujian RAG Dalam Memori

Sistem RAG biasanya membutuhkan database vektor dan layanan embedding. Pola in-memory memungkinkan Anda menguji seluruh pipeline tanpa ketergantungan eksternal.

<img src="../../../translated_images/id/rag-testing.ee7541b1e23934b1.webp" alt="Pengujian RAG Dalam Memori" width="800"/>

*Alur kerja pengujian RAG dalam memori menunjukkan penguraian dokumen, penyimpanan embedding, dan pencarian kemiripan tanpa perlu database*

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

Tes ini dari `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` membuat dokumen di memori dan memverifikasi pemecahan serta penanganan metadata.

### Pola 6: Pengujian Integrasi MCP

Modul MCP menguji integrasi Model Context Protocol menggunakan transport stdio. Tes ini memverifikasi aplikasi Anda bisa memulai dan berkomunikasi dengan server MCP sebagai subprocess.

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

Uji kode Anda, bukan AI. Tes Anda harus memvalidasi kode yang Anda tulis dengan memeriksa bagaimana prompt dibuat, bagaimana memori dikelola, dan bagaimana alat dijalankan. Respons AI bervariasi dan tidak harus menjadi bagian dari asersi tes. Tanyakan pada diri sendiri apakah template prompt Anda benar melakukan substitusi variabel, bukan apakah AI memberikan jawaban yang benar.

Gunakan mock untuk model bahasa. Mereka adalah ketergantungan eksternal yang lambat, mahal, dan tidak deterministik. Memalsukan membuat tes cepat dengan hitungan milidetik dibanding detik, gratis tanpa biaya API, dan deterministik dengan hasil yang sama setiap saat.

Jaga agar tes tetap independen. Setiap tes harus menyiapkan data sendiri, tidak bergantung pada tes lain, dan membersihkan setelah selesai. Tes harus lulus tanpa memperhatikan urutan eksekusi.

Uji kasus tepi selain jalur utama. Coba input kosong, input sangat besar, karakter khusus, parameter tidak valid, dan kondisi batas. Ini sering mengungkap bug yang tidak terdeteksi dengan penggunaan normal.

Gunakan nama yang deskriptif. Bandingkan `shouldMaintainConversationHistoryAcrossMultipleMessages()` dengan `test1()`. Yang pertama memberi tahu Anda dengan tepat apa yang diuji, sehingga memudahkan debug jika gagal.

## Langkah Berikutnya

Sekarang Anda memahami pola pengujian, jelajahi lebih dalam setiap modul:

- **[00 - Mulai Cepat](../00-quick-start/README.md)** - Mulai dengan dasar template prompt
- **[01 - Pengantar](../01-introduction/README.md)** - Pelajari manajemen memori percakapan
- **[02 - Rekayasa Prompt](../02/prompt-engineering/README.md)** - Kuasai pola pemanggilan GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Bangun sistem retrieval-augmented generation
- **[04 - Alat](../04-tools/README.md)** - Implementasikan pemanggilan fungsi dan rantai alat
- **[05 - MCP](../05-mcp/README.md)** - Integrasikan Model Context Protocol

README setiap modul menyediakan penjelasan rinci konsep yang diuji di sini.

---

**Navigasi:** [← Kembali ke Utama](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berusaha untuk ketepatan, harap diingat bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang sah. Untuk informasi penting, disarankan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau salah tafsir yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->