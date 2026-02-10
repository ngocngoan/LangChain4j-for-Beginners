# Menguji Aplikasi LangChain4j

## Jadual Kandungan

- [Mula Cepat](../../../docs)
- [Apa yang Diliputi oleh Ujian](../../../docs)
- [Menjalankan Ujian](../../../docs)
- [Menjalankan Ujian dalam VS Code](../../../docs)
- [Corak Pengujian](../../../docs)
- [Falsafah Pengujian](../../../docs)
- [Langkah Seterusnya](../../../docs)

Panduan ini membimbing anda melalui ujian yang menunjukkan cara menguji aplikasi AI tanpa memerlukan kekunci API atau perkhidmatan luaran.

## Mula Cepat

Jalankan semua ujian dengan satu arahan:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/ms/test-results.ea5c98d8f3642043.webp" alt="Hasil Ujian Berjaya" width="800"/>

*Pelaksanaan ujian berjaya menunjukkan semua ujian lulus tanpa kegagalan*

## Apa yang Diliputi oleh Ujian

Kursus ini memberi tumpuan kepada **ujian unit** yang dijalankan secara tempatan. Setiap ujian menunjukkan konsep LangChain4j tertentu secara terpisah.

<img src="../../../translated_images/ms/testing-pyramid.2dd1079a0481e53e.webp" alt="Piramid Ujian" width="800"/>

*Piramid ujian yang menunjukkan keseimbangan antara ujian unit (pantas, terasing), ujian integrasi (komponen sebenar), dan ujian hujung-ke-hujung. Latihan ini meliputi ujian unit.*

| Modul | Ujian | Fokus | Fail Utama |
|--------|-------|-------|-----------|
| **00 - Mula Cepat** | 6 | Templat petikan dan penggantian pembolehubah | `SimpleQuickStartTest.java` |
| **01 - Pengenalan** | 8 | Ingatan perbualan dan chat berstatus | `SimpleConversationTest.java` |
| **02 - Kejuruteraan Petikan** | 12 | Corak GPT-5.2, tahap keghairahan, output berstruktur | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Pengambilan dokumen, embeddings, pencarian kesamaan | `DocumentServiceTest.java` |
| **04 - Alat** | 12 | Panggilan fungsi dan rantai alat | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Protokol Konteks Model dengan pengangkutan Stdio | `SimpleMcpTest.java` |

## Menjalankan Ujian

**Jalankan semua ujian dari root:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Jalankan ujian untuk modul tertentu:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Atau dari root
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Atau dari akar
mvn --% test -pl 01-introduction
```

**Jalankan satu kelas ujian:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Jalankan kaedah ujian tertentu:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#harusMengekalkanSejarahPerbualan
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#harusMenjagaSejarahPerbualan
```

## Menjalankan Ujian dalam VS Code

Jika anda menggunakan Visual Studio Code, Test Explorer menyediakan antara muka grafik untuk menjalankan dan debugging ujian.

<img src="../../../translated_images/ms/vscode-testing.f02dd5917289dced.webp" alt="Peneroka Ujian VS Code" width="800"/>

*Peneroka Ujian VS Code menunjukkan pokok ujian dengan semua kelas ujian Java dan kaedah ujian individu*

**Untuk menjalankan ujian dalam VS Code:**

1. Buka Test Explorer dengan mengklik ikon beker di Bar Aktiviti
2. Kembangkan pokok ujian untuk melihat semua modul dan kelas ujian
3. Klik butang main di sebelah mana-mana ujian untuk menjalankannya secara individu
4. Klik "Run All Tests" untuk melaksanakan kesemua set ujian
5. Klik kanan mana-mana ujian dan pilih "Debug Test" untuk menetapkan titik henti dan langkah melalui kod

Test Explorer menunjukkan tanda semak hijau untuk ujian yang lulus dan menyediakan mesej kegagalan terperinci apabila ujian gagal.

## Corak Pengujian

### Corak 1: Menguji Templat Petikan

Corak paling mudah menguji templat petikan tanpa memanggil mana-mana model AI. Anda mengesahkan bahawa penggantian pembolehubah berfungsi dengan betul dan petikan diformat seperti yang dijangka.

<img src="../../../translated_images/ms/prompt-template-testing.b902758ddccc8dee.webp" alt="Pengujian Templat Petikan" width="800"/>

*Pengujian templat petikan menunjukkan aliran penggantian pembolehubah: templat dengan tempat letak → nilai diterapkan → output diformat disahkan*

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

Ujian ini berada di `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Jalankan ia:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#formatTemplatArahanUji
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#ujiFormatTemplatArahan
```

### Corak 2: Memalsukan Model Bahasa

Apabila menguji logik perbualan, gunakan Mockito untuk membuat model palsu yang mengembalikan respons yang telah ditentukan. Ini menjadikan ujian pantas, percuma, dan deterministik.

<img src="../../../translated_images/ms/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Perbandingan Mock vs API Sebenar" width="800"/>

*Perbandingan yang menunjukkan mengapa mock lebih disukai untuk pengujian: ia pantas, percuma, deterministik, dan tidak memerlukan kekunci API*

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
        assertThat(history).hasSize(6); // 3 mesej pengguna + 3 mesej AI
    }
}
```

Corak ini muncul dalam `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mock memastikan kelakuan konsisten supaya anda dapat mengesahkan pengurusan memori berfungsi dengan betul.

### Corak 3: Menguji Pengasingan Perbualan

Ingatan perbualan mesti memisahkan beberapa pengguna. Ujian ini mengesahkan bahawa perbualan tidak mencampuradukkan konteks.

<img src="../../../translated_images/ms/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Pengasingan Perbualan" width="800"/>

*Pengujian pengasingan perbualan menunjukkan stor ingatan berasingan untuk pengguna berbeza bagi mengelakkan pencampuran konteks*

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

Setiap perbualan mengekalkan sejarahnya sendiri secara bebas. Dalam sistem pengeluaran, pengasingan ini kritikal untuk aplikasi pelbagai pengguna.

### Corak 4: Menguji Alat Secara Bebas

Alat adalah fungsi yang boleh dipanggil oleh AI. Uji mereka secara langsung untuk memastikan mereka berfungsi dengan betul tanpa mengira keputusan AI.

<img src="../../../translated_images/ms/tools-testing.3e1706817b0b3924.webp" alt="Pengujian Alat" width="800"/>

*Pengujian alat secara bebas menunjukkan pelaksanaan alat palsu tanpa panggilan AI untuk mengesahkan logik perniagaan*

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

Ujian ini dari `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` mengesahkan logik alat tanpa penglibatan AI. Contoh rantai menunjukkan bagaimana output satu alat memberi input kepada alat lain.

### Corak 5: Pengujian RAG Dalam Memori

Sistem RAG secara tradisional memerlukan pangkalan data vektor dan perkhidmatan embedding. Corak dalam memori membolehkan anda menguji keseluruhan saluran tanpa kebergantungan luaran.

<img src="../../../translated_images/ms/rag-testing.ee7541b1e23934b1.webp" alt="Pengujian RAG Dalam Memori" width="800"/>

*Aliran kerja pengujian RAG dalam memori menunjukkan penganalisaan dokumen, penyimpanan embedding, dan pencarian kesamaan tanpa memerlukan pangkalan data*

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

Ujian ini dari `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` mencipta dokumen dalam memori dan mengesahkan pemecahan dan pengendalian metadata.

### Corak 6: Pengujian Integrasi MCP

Modul MCP menguji protokol Model Context Protocol menggunakan pengangkutan stdio. Ujian ini mengesahkan bahawa aplikasi anda boleh menjana dan berkomunikasi dengan pelayan MCP sebagai subproses.

Ujian dalam `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` mengesahkan kelakuan klien MCP.

**Jalankan mereka:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Falsafah Pengujian

Uji kod anda, bukan AI. Ujian anda harus mengesahkan kod yang anda tulis dengan memeriksa cara petikan dibina, cara memori diurus, dan cara alat dilaksanakan. Respons AI berubah-ubah dan tidak seharusnya menjadi sebahagian daripada pernyataan ujian. Tanyakan pada diri sendiri sama ada templat petikan anda menggantikan pembolehubah dengan betul, bukan sama ada AI memberikan jawapan yang betul.

Gunakan mock untuk model bahasa. Mereka adalah kebergantungan luaran yang perlahan, mahal, dan tidak deterministik. Pemalsuan menjadikan ujian pantas dengan milisaat dan bukan saat, percuma tanpa kos API, serta deterministik dengan keputusan sama setiap masa.

Pastikan ujian berdikari. Setiap ujian harus menyediakan data sendiri, tidak bergantung pada ujian lain, dan membersihkan selepasnya. Ujian harus lulus tanpa mengira susunan pelaksanaan.

Uji kes sempadan selain laluan yang biasa. Cubalah input kosong, input sangat besar, aksara khas, parameter tidak sah, dan keadaan sempadan. Ini sering mendedahkan pepijat yang tidak terdedah dalam penggunaan biasa.

Gunakan nama yang deskriptif. Bandingkan `shouldMaintainConversationHistoryAcrossMultipleMessages()` dengan `test1()`. Yang pertama memberitahu anda dengan tepat apa yang diuji, memudahkan penjejakan kegagalan.

## Langkah Seterusnya

Sekarang anda telah memahami corak pengujian, selami lebih dalam setiap modul:

- **[00 - Mula Cepat](../00-quick-start/README.md)** - Mulakan dengan asas templat petikan
- **[01 - Pengenalan](../01-introduction/README.md)** - Belajar pengurusan ingatan perbualan
- **[02 - Kejuruteraan Petikan](../02-prompt-engineering/README.md)** - Kuasai corak petikan GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Bina sistem penjanaan beraugmen pengambilan
- **[04 - Alat](../04-tools/README.md)** - Laksanakan panggilan fungsi dan rantai alat
- **[05 - MCP](../05-mcp/README.md)** - Integrasi Protokol Konteks Model

README setiap modul menyediakan penerangan terperinci tentang konsep yang diuji di sini.

---

**Navigasi:** [← Kembali ke Utama](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila ambil perhatian bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber rujukan yang sahih. Untuk maklumat penting, terjemahan profesional oleh manusia adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->