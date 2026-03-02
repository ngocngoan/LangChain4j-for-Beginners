# Menguji Aplikasi LangChain4j

## Kandungan

- [Mula Pantas](../../../docs)
- [Apa yang Diuji](../../../docs)
- [Menjalankan Ujian](../../../docs)
- [Menjalankan Ujian dalam VS Code](../../../docs)
- [Corak Pengujian](../../../docs)
- [Falsafah Pengujian](../../../docs)
- [Langkah Seterusnya](../../../docs)

Panduan ini membimbing anda melalui ujian yang menunjukkan cara menguji aplikasi AI tanpa memerlukan kunci API atau perkhidmatan luar.

## Mula Pantas

Jalankan semua ujian dengan satu arahan:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

Apabila semua ujian lulus, anda akan melihat output seperti tangkapan skrin di bawah — ujian dijalankan tanpa kegagalan.

<img src="../../../translated_images/ms/test-results.ea5c98d8f3642043.webp" alt="Keputusan Ujian Berjaya" width="800"/>

*Pelaksanaan ujian berjaya menunjukkan semua ujian lulus tanpa kegagalan*

## Apa yang Diuji

Kursus ini memfokuskan pada **ujian unit** yang dijalankan secara tempatan. Setiap ujian menunjukkan konsep LangChain4j tertentu secara terpencil. Piramid pengujian di bawah menunjukkan di mana ujian unit sesuai — ia membentuk asas yang pantas dan boleh dipercayai yang dibina strategi ujian anda yang lain.

<img src="../../../translated_images/ms/testing-pyramid.2dd1079a0481e53e.webp" alt="Piramid Pengujian" width="800"/>

*Piramid pengujian menunjukkan keseimbangan antara ujian unit (pantas, terasing), ujian integrasi (komponen sebenar), dan ujian hujung ke hujung. Latihan ini merangkumi ujian unit.*

| Modul | Ujian | Fokus | Fail Utama |
|--------|-------|-------|-----------|
| **00 - Mula Pantas** | 6 | Templat rangsangan dan penggantian pembolehubah | `SimpleQuickStartTest.java` |
| **01 - Pengenalan** | 8 | Memori perbualan dan sembang berstatus | `SimpleConversationTest.java` |
| **02 - Kejuruteraan Rangsangan** | 12 | Corak GPT-5.2, tahap kesungguhan, output berstruktur | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Penyepaduan dokumen, embedding, carian persamaan | `DocumentServiceTest.java` |
| **04 - Alat** | 12 | Panggilan fungsi dan rantaian alat | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Protokol Konteks Model dengan pengangkutan Stdio | `SimpleMcpTest.java` |

## Menjalankan Ujian

**Jalankan semua ujian dari akar:**

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
# Atau dari akar
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
mvn test -Dtest=SimpleConversationTest#harusMenjagaSejarahPerbualan
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#harusMenjagaSejarahPerbualan
```

## Menjalankan Ujian dalam VS Code

Jika anda menggunakan Visual Studio Code, Test Explorer menyediakan antara muka grafik untuk menjalankan dan menyahpepijat ujian.

<img src="../../../translated_images/ms/vscode-testing.f02dd5917289dced.webp" alt="Penjelajah Ujian VS Code" width="800"/>

*Penjelajah Ujian VS Code menunjukkan pokok ujian dengan semua kelas ujian Java dan kaedah ujian individu*

**Untuk menjalankan ujian dalam VS Code:**

1. Buka Test Explorer dengan mengklik ikon bekas kimia di Bar Aktiviti
2. Kembangkan pokok ujian untuk melihat semua modul dan kelas ujian
3. Klik butang main di sebelah mana-mana ujian untuk menjalankannya secara individu
4. Klik "Run All Tests" untuk melaksanakan keseluruhan set ujian
5. Klik kanan mana-mana ujian dan pilih "Debug Test" untuk menetapkan titik pecah dan langkah melalui kod

Penjelajah Ujian menunjukkan tanda semak hijau untuk ujian yang lulus dan memberikan mesej kegagalan terperinci apabila ujian gagal.

## Corak Pengujian

### Corak 1: Menguji Templat Rangsangan

Corak paling mudah menguji templat rangsangan tanpa memanggil mana-mana model AI. Anda memeriksa bahawa penggantian pembolehubah dilakukan dengan betul dan rangsangan diformatkan seperti yang dijangka.

<img src="../../../translated_images/ms/prompt-template-testing.b902758ddccc8dee.webp" alt="Pengujian Templat Rangsangan" width="800"/>

*Pengujian templat rangsangan yang menunjukkan aliran penggantian pembolehubah: templat dengan tempat letak → nilai digunakan → output yang diformat disahkan*

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

Ujian ini terletak di `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Jalankan:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#ujianFormatTemplatArahan
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#formatTemplatPromptUjian
```

### Corak 2: Memalsukan Model Bahasa

Apabila menguji logik perbualan, gunakan Mockito untuk mencipta model palsu yang mengembalikan respons yang telah ditentukan. Ini menjadikan ujian pantas, percuma, dan deterministik.

<img src="../../../translated_images/ms/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Perbandingan Palsu vs API Sebenar" width="800"/>

*Perbandingan menunjukkan mengapa palsu lebih disukai untuk pengujian: ia pantas, percuma, deterministik, dan tidak memerlukan kunci API*

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

Corak ini muncul dalam `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Palsu memastikan tingkah laku konsisten supaya anda boleh mengesahkan pengurusan memori berfungsi dengan betul.

### Corak 3: Menguji Pengasingan Perbualan

Memori perbualan mesti memisahkan beberapa pengguna. Ujian ini memeriksa bahawa perbualan tidak mencampur konteks.

<img src="../../../translated_images/ms/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Pengasingan Perbualan" width="800"/>

*Pengujian pengasingan perbualan menunjukkan stor memori berasingan untuk pengguna yang berbeza bagi mengelakkan pencampuran konteks*

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

Setiap perbualan menyimpan sejarahnya sendiri secara bebas. Dalam sistem produksi, pengasingan ini kritikal untuk aplikasi berbilang pengguna.

### Corak 4: Menguji Alat Secara Berasingan

Alat adalah fungsi yang boleh dipanggil oleh AI. Uji mereka secara langsung untuk memastikan ia berfungsi walaupun tanpa keputusan AI.

<img src="../../../translated_images/ms/tools-testing.3e1706817b0b3924.webp" alt="Pengujian Alat" width="800"/>

*Pengujian alat secara berasingan menunjukkan pelaksanaan alat palsu tanpa panggilan AI untuk mengesahkan logik perniagaan*

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

Ujian ini dari `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` mengesahkan logik alat tanpa penglibatan AI. Contoh rantaian menunjukkan bagaimana output satu alat memberi makan kepada input alat lain.

### Corak 5: Pengujian RAG Dalam Memori

Sistem RAG tradisional memerlukan pangkalan data vektor dan perkhidmatan embedding. Corak dalam memori membolehkan anda menguji seluruh saluran tanpa pergantungan luar.

<img src="../../../translated_images/ms/rag-testing.ee7541b1e23934b1.webp" alt="Pengujian RAG Dalam Memori" width="800"/>

*Aliran kerja pengujian RAG dalam memori menunjukkan pengecaman dokumen, penyimpanan embedding, dan carian persamaan tanpa memerlukan pangkalan data*

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

Ujian ini dari `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` mencipta dokumen dalam memori dan memeriksa pemecahan serta pengendalian metadata.

### Corak 6: Pengujian Integrasi MCP

Modul MCP menguji integrasi Protokol Konteks Model menggunakan pengangkutan stdio. Ujian ini mengesahkan aplikasi anda boleh menjana dan berkomunikasi dengan pelayan MCP sebagai proses bawah.

Ujian dalam `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` mengesahkan tingkah laku klien MCP.

**Jalankan:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Falsafah Pengujian

Uji kod anda, bukan AI. Ujian anda harus mengesahkan kod yang anda tulis dengan memeriksa bagaimana rangsangan dibina, bagaimana memori diurus, dan bagaimana alat dilaksanakan. Respons AI berubah-ubah dan tidak sepatutnya menjadi sebahagian daripada penegasan ujian. Tanyakan pada diri anda sama ada templat rangsangan anda menggantikan pembolehubah dengan betul, bukan sama ada AI memberikan jawapan yang betul.

Gunakan palsu untuk model bahasa. Mereka adalah kebergantungan luaran yang perlahan, mahal, dan tidak deterministik. Memalsukan menjadikan ujian pantas dalam milisaat, percuma tanpa kos API, dan deterministik dengan keputusan sama setiap kali.

Pastikan ujian berdikari. Setiap ujian harus menyediakan data sendiri, tidak bergantung pada ujian lain, dan membersihkan selepas dirinya. Ujian harus lulus tanpa mengira urutan pelaksanaan.

Uji kes luar selain jalan gembira. Cuba input kosong, input sangat besar, aksara khas, parameter tidak sah, dan syarat sempadan. Ini sering mendedahkan pepijat yang penggunaan normal tidak dedahkan.

Gunakan nama deskriptif. Bandingkan `shouldMaintainConversationHistoryAcrossMultipleMessages()` dengan `test1()`. Yang pertama memberitahu anda dengan tepat apa yang diuji, memudahkan debug kegagalan.

## Langkah Seterusnya

Kini anda memahami corak pengujian, terokai lebih mendalam setiap modul:

- **[00 - Mula Pantas](../00-quick-start/README.md)** - Mulakan dengan asas templat rangsangan
- **[01 - Pengenalan](../01-introduction/README.md)** - Pelajari pengurusan memori perbualan
- **[02 - Kejuruteraan Rangsangan](../02/prompt-engineering/README.md)** - Kuasai corak rangsangan GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Bangunkan sistem penghasilan diperkuatkan pengambilan
- **[04 - Alat](../04-tools/README.md)** - Laksanakan panggilan fungsi dan rantai alat
- **[05 - MCP](../05-mcp/README.md)** - Integrasi Protokol Konteks Model

README setiap modul menyediakan penjelasan terperinci konsep yang diuji di sini.

---

**Navigasi:** [← Kembali ke Utama](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, harap maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya hendaklah dianggap sebagai sumber yang sahih. Untuk maklumat penting, terjemahan manusia profesional adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->