# การทดสอบแอปพลิเคชัน LangChain4j

## สารบัญ

- [เริ่มต้นอย่างรวดเร็ว](../../../docs)
- [สิ่งที่การทดสอบครอบคลุม](../../../docs)
- [การรันการทดสอบ](../../../docs)
- [การรันทดสอบใน VS Code](../../../docs)
- [รูปแบบการทดสอบ](../../../docs)
- [ปรัชญาการทดสอบ](../../../docs)
- [ขั้นตอนต่อไป](../../../docs)

คู่มือนี้จะแนะนำคุณผ่านการทดสอบที่แสดงวิธีการทดสอบแอป AI โดยไม่ต้องใช้คีย์ API หรือบริการภายนอก

## เริ่มต้นอย่างรวดเร็ว

รันการทดสอบทั้งหมดด้วยคำสั่งเดียว:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/th/test-results.ea5c98d8f3642043.webp" alt="ผลการทดสอบสำเร็จ" width="800"/>

*การทำงานของการทดสอบที่ประสบความสำเร็จ แสดงให้เห็นว่าการทดสอบทั้งหมดผ่านโดยไม่มีข้อผิดพลาด*

## สิ่งที่การทดสอบครอบคลุม

หลักสูตรนี้เน้นที่ **การทดสอบหน่วย (unit tests)** ที่รันในเครื่องแต่ละอย่างแยกกัน แต่ละการทดสอบแสดงแนวคิดเฉพาะของ LangChain4j อย่างโดดเดี่ยว

<img src="../../../translated_images/th/testing-pyramid.2dd1079a0481e53e.webp" alt="พีรามิดการทดสอบ" width="800"/>

*พีรามิดการทดสอบแสดงสมดุลระหว่างการทดสอบหน่วย (รวดเร็ว, แยกจากกัน), การทดสอบเชิงบูรณาการ (ส่วนประกอบจริง), และการทดสอบแบบครอบคลุมทั้งหมด การฝึกอบรมนี้ครอบคลุมการทดสอบหน่วย*

| โมดูล | การทดสอบ | จุดเน้น | ไฟล์หลัก |
|--------|-------|-------|-----------|
| **00 - เริ่มต้นอย่างรวดเร็ว** | 6 | แบบฟอร์มพรอมต์และการแทนที่ตัวแปร | `SimpleQuickStartTest.java` |
| **01 - บทนำ** | 8 | หน่วยความจำการสนทนาและแชทแบบมีสถานะ | `SimpleConversationTest.java` |
| **02 - การออกแบบพรอมต์** | 12 | รูปแบบ GPT-5.2, ระดับความกระตือรือร้น, ผลลัพธ์ที่มีโครงสร้าง | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | การนำเข้าข้อมูลเอกสาร, embeddings, การค้นหาคล้ายกัน | `DocumentServiceTest.java` |
| **04 - เครื่องมือ** | 12 | การเรียกฟังก์ชันและการเชื่อมโยงเครื่องมือ | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | โปรโตคอลบริบทโมเดลกับการส่งข้อมูล stdio | `SimpleMcpTest.java` |

## การรันการทดสอบ

**รันการทดสอบทั้งหมดจาก root:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**รันการทดสอบสำหรับโมดูลเฉพาะ:**

**Bash:**
```bash
cd 01-introduction && mvn test
# หรือจากรูท
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# หรือจากรูท
mvn --% test -pl 01-introduction
```

**รันคลาสทดสอบเดียว:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**รันเมธอดการทดสอบเฉพาะ:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#ควรรักษาประวัติการสนทนาไว้
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#ควรรักษาประวัติการสนทนาไว้
```

## การรันทดสอบใน VS Code

ถ้าคุณใช้ Visual Studio Code, Test Explorer ให้ส่วนติดต่อกราฟิกสำหรับรันและดีบักการทดสอบ

<img src="../../../translated_images/th/vscode-testing.f02dd5917289dced.webp" alt="ตัวสำรวจการทดสอบ VS Code" width="800"/>

*ตัวสำรวจการทดสอบ VS Code แสดงโครงสร้างต้นไม้ของการทดสอบ พร้อมทั้งคลาสทดสอบ Java ทั้งหมดและเมธอดการทดสอบแต่ละรายการ*

**วิธีรันทดสอบใน VS Code:**

1. เปิดตัวสำรวจการทดสอบโดยคลิกไอคอนขวดทดลองในแถบกิจกรรม
2. ขยายต้นไม้การทดสอบเพื่อดูโมดูลและคลาสการทดสอบทั้งหมด
3. คลิกปุ่มเล่นข้างๆ การทดสอบใด ๆ เพื่อรันแบบเดี่ยว
4. คลิก "Run All Tests" เพื่อรันชุดการทดสอบทั้งหมด
5. คลิกขวาที่การทดสอบแล้วเลือก "Debug Test" เพื่อใส่เบรกพอยต์และติดตามโค้ด

ตัวสำรวจการทดสอบจะแสดงเครื่องหมายถูกสีเขียวสำหรับการทดสอบที่ผ่าน และข้อความรายละเอียดเมื่อทดสอบล้มเหลว

## รูปแบบการทดสอบ

### รูปแบบที่ 1: การทดสอบแบบฟอร์มพรอมต์

รูปแบบที่ง่ายที่สุดทดสอบแบบฟอร์มพรอมต์โดยไม่เรียกใช้โมเดล AI เพื่อคุณยืนยันว่าสามารถแทนที่ตัวแปรได้ถูกต้องและพรอมต์ถูกฟอร์แมตอย่างที่คาดไว้

<img src="../../../translated_images/th/prompt-template-testing.b902758ddccc8dee.webp" alt="การทดสอบแบบฟอร์มพรอมต์" width="800"/>

*การทดสอบแบบฟอร์มพรอมต์แสดงลำดับการแทนที่ตัวแปร: แบบฟอร์มที่มีตัวแทนที่ → ค่าที่ถูกใช้ → ผลลัพธ์ที่ฟอร์แมตและตรวจสอบ*

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

การทดสอบนี้อยู่ใน `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`

**รันได้โดย:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#การจัดรูปแบบเทมเพลตการทดสอบ
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#ทดสอบการจัดรูปแบบเทมเพลตพรอมต์
```

### รูปแบบที่ 2: การใช้ Mock กับโมเดลภาษา

เมื่อทดสอบตรรกะการสนทนา ใช้ Mockito สร้างโมเดลปลอมที่ส่งกลับคำตอบที่กำหนดไว้ล่วงหน้า ซึ่งทำให้การทดสอบรวดเร็ว ฟรี และกำหนดผลลัพธ์ได้

<img src="../../../translated_images/th/mock-vs-real.3b8b1f85bfe6845e.webp" alt="เปรียบเทียบ Mock กับ API จริง" width="800"/>

*เปรียบเทียบเหตุผลว่าทำไม mock ถึงถูกเลือกใช้สำหรับการทดสอบ: เร็ว, ฟรี, กำหนดผลได้, และไม่ต้องใช้คีย์ API*

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
        assertThat(history).hasSize(6); // ข้อความผู้ใช้ 3 ข้อความ + ข้อความ AI 3 ข้อความ
    }
}
```

รูปแบบนี้ปรากฏใน `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` โดย mock รับประกันพฤติกรรมสม่ำเสมอเพื่อคุณสามารถยืนยันว่าวิธีจัดการหน่วยความจำทำงานถูกต้อง

### รูปแบบที่ 3: การทดสอบการแยกการสนทนา

หน่วยความจำการสนทนาต้องแยกผู้ใช้หลายคนออกจากกัน การทดสอบนี้ยืนยันว่าสนทนาไม่ปะปนบริบทกัน

<img src="../../../translated_images/th/conversation-isolation.e00336cf8f7a3e3f.webp" alt="การแยกการสนทนา" width="800"/>

*การทดสอบการแยกการสนทนาแสดงให้เห็นการเก็บหน่วยความจำที่แยกกันสำหรับผู้ใช้แต่ละคนเพื่อป้องกันการปะปนของบริบท*

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

แต่ละการสนทนาจะเก็บประวัติโดยอิสระ ในระบบการผลิต การแยกนี้สำคัญมากสำหรับแอปหลายผู้ใช้

### รูปแบบที่ 4: การทดสอบเครื่องมือโดยแยกกัน

เครื่องมือเป็นฟังก์ชันที่ AI สามารถเรียกใช้ได้ ทดสอบพวกมันโดยตรงเพื่อให้แน่ใจว่าทำงานถูกต้องไม่ว่าจะอย่างไรก็ตามผลการตัดสินใจของ AI

<img src="../../../translated_images/th/tools-testing.3e1706817b0b3924.webp" alt="การทดสอบเครื่องมือ" width="800"/>

*การทดสอบเครื่องมืออย่างแยกส่วนโดยการรัน mock tool tanpa call AI เพื่อยืนยันทดสอบตรรกะทางธุรกิจ*

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

การทดสอบเหล่านี้จาก `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` ยืนยันทดสอบตรรกะเครื่องมือโดยไม่เกี่ยวข้องกับ AI ตัวอย่าง chaining แสดงวิธีผลลัพธ์ของเครื่องมือหนึ่งส่งต่อเป็นอินพุตของอีกเครื่องมือ

### รูปแบบที่ 5: การทดสอบ RAG ในหน่วยความจำ

ระบบ RAG โดยทั่วไปต้องใช้ฐานข้อมูลเวกเตอร์และบริการ embedding รูปแบบหน่วยความจำช่วยให้คุณทดสอบกระบวนการทั้งหมดโดยไม่มีการพึ่งพาภายนอก

<img src="../../../translated_images/th/rag-testing.ee7541b1e23934b1.webp" alt="การทดสอบ RAG ในหน่วยความจำ" width="800"/>

*เวิร์กโฟลว์การทดสอบ RAG ในหน่วยความจำ แสดงการแยกวิเคราะห์เอกสาร, การจัดเก็บ embedding, และการค้นหาคล้ายกันโดยไม่ต้องใช้ฐานข้อมูล*

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

การทดสอบนี้จาก `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` สร้างเอกสารในหน่วยความจำและตรวจสอบการแบ่งชิ้นและการจัดการ metadata

### รูปแบบที่ 6: การทดสอบบูรณาการ MCP

โมดูล MCP ทดสอบการบูรณาการโปรโตคอลบริบทโมเดลโดยใช้การส่งข้อมูล stdio การทดสอบเหล่านี้ยืนยันว่าสามารถสร้างและสื่อสารกับ MCP server เป็น subprocess ได้

การทดสอบใน `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` ยืนยันทดสอบพฤติกรรมลูกค้า MCP

**รันได้โดย:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## ปรัชญาการทดสอบ

ทดสอบโค้ดของคุณ ไม่ใช่ AI การทดสอบของคุณควรตรวจสอบโค้ดที่คุณเขียนโดยดูว่าสร้างพรอมต์อย่างไร, จัดการหน่วยความจำอย่างไร, และเครื่องมือทำงานอย่างไร AI มีการตอบสนองที่หลากหลายและไม่ควรเป็นส่วนหนึ่งของข้อสรุปในการทดสอบ ถามตัวเองว่าพรอมต์ของคุณแทนที่ตัวแปรถูกต้องไหม ไม่ใช่ว่า AI ให้คำตอบถูกไหม

ใช้ mock กับโมเดลภาษา เพราะเป็นการพึ่งพาภายนอกที่ช้า, แพง, และไม่กำหนดผลลัพธ์ได้ การใช้ mock ทำให้การทดสอบรวดเร็วใช้เวลาเป็นมิลลิวินาทีแทนวินาที, ฟรีไม่มีค่าใช้จ่าย API, และกำหนดผลลัพธ์เหมือนเดิมทุกครั้ง

ให้การทดสอบเป็นอิสระ แต่ละการทดสอบควรตั้งค่าข้อมูลเอง ไม่อาศัยการทดสอบอื่น และล้างข้อมูลหลังการทดสอบ การทดสอบควรผ่านไม่ว่าจะรันในลำดับใด

ทดสอบกรณีขอบเขตเกินเส้นทางที่ดี ลองป้อนข้อมูลว่าง, ข้อมูลขนาดใหญ่, ตัวอักษรพิเศษ, พารามิเตอร์ไม่ถูกต้อง, และเงื่อนไขขอบเขต เหล่านี้มักเผยบั๊กที่การใช้งานปกติไม่พบ

ใช้ชื่อที่สื่อความหมาย เปรียบเทียบ `shouldMaintainConversationHistoryAcrossMultipleMessages()` กับ `test1()` อันแรกบอกได้ชัดเจนว่าทดสอบอะไร ทำให้แก้บั๊กง่ายขึ้น

## ขั้นตอนต่อไป

ตอนนี้คุณเข้าใจรูปแบบการทดสอบแล้ว ลึกลงไปในแต่ละโมดูล:

- **[00 - เริ่มต้นอย่างรวดเร็ว](../00-quick-start/README.md)** - เริ่มต้นด้วยพื้นฐานแบบฟอร์มพรอมต์
- **[01 - บทนำ](../01-introduction/README.md)** - เรียนรู้การจัดการหน่วยความจำการสนทนา
- **[02 - การออกแบบพรอมต์](../02-prompt-engineering/README.md)** - เชี่ยวชาญรูปแบบการพรอมต์ GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - สร้างระบบ retrieval-augmented generation
- **[04 - เครื่องมือ](../04-tools/README.md)** - ใช้งานการเรียกฟังก์ชันและเชนเครื่องมือ
- **[05 - MCP](../05-mcp/README.md)** - บูรณาการโปรโตคอลบริบทโมเดล

README ของแต่ละโมดูลให้คำอธิบายโดยละเอียดของแนวคิดที่ทดสอบในนี้

---

**นำทาง:** [← กลับสู่หน้าหลัก](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ข้อจำกัดความรับผิดชอบ**:  
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลด้วย AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้เราจะพยายามให้ข้อมูลถูกต้องที่สุด แต่โปรดทราบว่าการแปลอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่แม่นยำ เอกสารต้นฉบับในภาษาดั้งเดิมควรถูกพิจารณาเป็นแหล่งข้อมูลที่ถูกต้องและเชื่อถือได้ สำหรับข้อมูลที่สำคัญ ขอแนะนำให้ใช้บริการแปลโดยมนุษย์ผู้เชี่ยวชาญ เราไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความที่ผิดพลาดใดๆ ที่เกิดจากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->