# การทดสอบแอปพลิเคชัน LangChain4j

## สารบัญ

- [เริ่มต้นอย่างรวดเร็ว](../../../docs)
- [สิ่งที่การทดสอบครอบคลุม](../../../docs)
- [การรันการทดสอบ](../../../docs)
- [การรันการทดสอบใน VS Code](../../../docs)
- [รูปแบบการทดสอบ](../../../docs)
- [ปรัชญาการทดสอบ](../../../docs)
- [ขั้นตอนถัดไป](../../../docs)

คู่มือนี้จะพาคุณผ่านการทดสอบที่แสดงวิธีการทดสอบแอปพลิเคชัน AI โดยไม่ต้องใช้คีย์ API หรือบริการภายนอก

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

เมื่อการทดสอบทั้งหมดผ่าน คุณจะเห็นผลลัพธ์เหมือนกับภาพหน้าจอด้านล่าง — การทดสอบทำงานโดยไม่มีความล้มเหลว

<img src="../../../translated_images/th/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*การทำงานของการทดสอบที่สำเร็จแสดงให้เห็นการผ่านทุกการทดสอบโดยไม่มีความล้มเหลว*

## สิ่งที่การทดสอบครอบคลุม

คอร์สนี้เน้นที่ **การทดสอบหน่วย** ที่รันในเครื่องแต่ละการทดสอบจะแสดงแนวคิดเฉพาะของ LangChain4j อย่างโดดเดี่ยว พีระมิดการทดสอบด้านล่างแสดงตำแหน่งของการทดสอบหน่วย — เป็นฐานที่รวดเร็วและเชื่อถือได้ซึ่งกลยุทธ์การทดสอบอื่น ๆ ของคุณจะสร้างขึ้น

<img src="../../../translated_images/th/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*พีระมิดการทดสอบแสดงความสมดุลระหว่างการทดสอบหน่วย (รวดเร็ว แยกส่วน), การทดสอบแบบบูรณาการ (ส่วนประกอบจริง), และการทดสอบแบบ end-to-end การฝึกอบรมนี้ครอบคลุมการทดสอบหน่วย*

| โมดูล | การทดสอบ | โฟกัส | ไฟล์หลัก |
|--------|-------|-------|-----------|
| **00 - เริ่มต้นอย่างรวดเร็ว** | 6 | เทมเพลตพรอมต์และการแทนที่ตัวแปร | `SimpleQuickStartTest.java` |
| **01 - บทนำ** | 8 | หน่วยความจำการสนทนาและแชทที่มีสถานะ | `SimpleConversationTest.java` |
| **02 - การสร้างพรอมต์** | 12 | รูปแบบ GPT-5.2, ระดับความกระตือรือร้น, เอาต์พุตที่มีโครงสร้าง | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | การนำเข้าข้อมูลเอกสาร, การฝังตัว, การค้นหาความคล้ายคลึง | `DocumentServiceTest.java` |
| **04 - เครื่องมือ** | 12 | การเรียกใช้งานฟังก์ชันและการเชื่อมโยงเครื่องมือ | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | โปรโตคอล Model Context กับการขนส่ง stdio | `SimpleMcpTest.java` |

## การรันการทดสอบ

**รันการทดสอบทั้งหมดจากโฟลเดอร์ราก:**

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

**รันคลาสการทดสอบเดียว:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**รันเมธอดทดสอบเฉพาะ:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#ควรรักษาประวัติการสนทนาไว้
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#ควรรักษาประวัติการสนทนาไว้
```

## การรันการทดสอบใน VS Code

หากคุณใช้ Visual Studio Code เครื่องมือ Test Explorer จะให้ส่วนติดต่อกราฟิกสำหรับรันและดีบักการทดสอบ

<img src="../../../translated_images/th/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer แสดงโครงสร้างต้นไม้การทดสอบพร้อมคลาสการทดสอบ Java ทั้งหมดและเมธอดทดสอบรายตัว*

**วิธีการรันการทดสอบใน VS Code:**

1. เปิด Test Explorer โดยคลิกไอคอนบีกเกอร์ใน Activity Bar  
2. ขยายต้นไม้การทดสอบเพื่อดูโมดูลและคลาสทดสอบทั้งหมด  
3. คลิกปุ่มเล่นข้างการทดสอบใดก็ได้เพื่อรันแบบเดี่ยว  
4. คลิก "Run All Tests" เพื่อรันชุดทั้งหมด  
5. คลิกขวาการทดสอบแล้วเลือก "Debug Test" เพื่อวางเบรกพอยต์และเดินโค้ดทีละขั้น  

Test Explorer จะแสดงเครื่องหมายถูกสีเขียวสำหรับการทดสอบที่ผ่านและแสดงข้อความความล้มเหลวโดยละเอียดเมื่อการทดสอบล้มเหลว

## รูปแบบการทดสอบ

### รูปแบบที่ 1: การทดสอบเทมเพลตพรอมต์

รูปแบบที่ง่ายที่สุดคือการทดสอบเทมเพลตพรอมต์โดยไม่เรียกใช้โมเดล AI คุณตรวจสอบว่าการแทนที่ตัวแปรทำงานถูกต้องและพรอมต์ถูกจัดรูปแบบตามที่คาดไว้

<img src="../../../translated_images/th/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*การทดสอบเทมเพลตพรอมต์แสดงกระบวนการแทนที่ตัวแปร: เทมเพลตพร้อมช่องว่าง → ค่าที่ถูกนำไปใช้ → ตรวจสอบเอาต์พุตที่จัดรูปแบบ*

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

**รันได้ด้วย:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#การทดสอบการจัดรูปแบบเทมเพลตพร้อมท์
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#ทดสอบฟอร์แมตเท็มเพลตแบบข้อความ
```

### รูปแบบที่ 2: การจำลองโมเดลภาษา

เมื่อทดสอบตรรกะการสนทนา ให้ใช้ Mockito สร้างโมเดลปลอมที่คืนค่าตอบสนองที่กำหนดไว้ล่วงหน้า วิธีนี้ทำให้การทดสอบเร็ว ฟรี และมีผลลัพธ์แน่นอน

<img src="../../../translated_images/th/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*การเปรียบเทียบแสดงเหตุผลที่การใช้ mocks เหมาะสำหรับการทดสอบ: เร็ว ฟรี แน่นอน และไม่ต้องใช้คีย์ API*

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
        assertThat(history).hasSize(6); // 3 ข้อความของผู้ใช้ + 3 ข้อความของ AI
    }
}
```

รูปแบบนี้พบได้ใน `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` ซึ่ง mock ช่วยให้พฤติกรรมคงที่เพื่อคุณจะยืนยันได้ว่าการจัดการหน่วยความจำทำงานถูกต้อง

### รูปแบบที่ 3: การทดสอบแยกความเป็นอิสระของการสนทนา

หน่วยความจำการสนทนาต้องแยกผู้ใช้หลายคนออกจากกัน การทดสอบนี้ยืนยันว่าการสนทนาแต่ละชุดไม่ผสมผสานบริบทกัน

<img src="../../../translated_images/th/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*การทดสอบแยกความเป็นอิสระของการสนทนาแสดงที่เก็บหน่วยความจำแยกต่างหากสำหรับผู้ใช้ต่างกันเพื่อป้องกันการผสมบริบท*

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

แต่ละการสนทนาจะรักษาประวัติแบบอิสระของตัวเอง ในระบบการผลิต การแยกนี้มีความสำคัญสำหรับแอปหลายผู้ใช้

### รูปแบบที่ 4: การทดสอบเครื่องมืออย่างอิสระ

เครื่องมือเป็นฟังก์ชันที่ AI สามารถเรียกใช้ ทดสอบเครื่องมือโดยตรงเพื่อให้แน่ใจว่าทำงานถูกต้องไม่ว่าจะตัดสินใจของ AI เป็นอย่างไร

<img src="../../../translated_images/th/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*การทดสอบเครื่องมืออย่างอิสระแสดงการรันเครื่องมือแบบ mock โดยไม่ต้องเรียก AI เพื่อยืนยันตรรกะธุรกิจ*

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

การทดสอบเหล่านี้จาก `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` ตรวจสอบตรรกะเครื่องมือโดยไม่เกี่ยวข้องกับ AI ตัวอย่างการเชื่อมโยงแสดงว่าเอาต์พุตของเครื่องมือหนึ่งป้อนเป็นอินพุตของอีกเครื่องมือได้อย่างไร

### รูปแบบที่ 5: การทดสอบ RAG ในหน่วยความจำ

ระบบ RAG โดยทั่วไปต้องใช้ฐานข้อมูลเวกเตอร์และบริการฝังข้อมูล รูปแบบในหน่วยความจำช่วยให้คุณทดสอบท่อทั้งระบบโดยไม่พึ่งพาภายนอก

<img src="../../../translated_images/th/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*เวิร์กโฟลว์การทดสอบ RAG ในหน่วยความจำแสดงการแยกเอกสาร การเก็บข้อมูลฝัง และการค้นหาความคล้ายคลึงโดยไม่ต้องใช้ฐานข้อมูล*

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

การทดสอบนี้จาก `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` สร้างเอกสารในหน่วยความจำและตรวจสอบการแบ่งชิ้นส่วนและการจัดการเมตาดาต้า

### รูปแบบที่ 6: การทดสอบการบูรณาการ MCP

โมดูล MCP ทดสอบการบูรณาการโปรโตคอล Model Context โดยใช้การขนส่ง stdio การทดสอบเหล่านี้ยืนยันว่าแอปของคุณสามารถเปิดและสื่อสารกับเซิร์ฟเวอร์ MCP ในรูปแบบ subprocess ได้

การทดสอบใน `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` ตรวจสอบพฤติกรรมของลูกค้า MCP

**รันได้ด้วย:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## ปรัชญาการทดสอบ

ทดสอบโค้ดของคุณ ไม่ใช่ AI การทดสอบของคุณควรตรวจสอบโค้ดที่คุณเขียนโดยดูว่าพรอมต์ถูกสร้างอย่างไร, การจัดการหน่วยความจำเป็นอย่างไร และการเรียกใช้เครื่องมือเป็นอย่างไร ตอบสนองของ AI มีความแปรผันและไม่ควรเป็นส่วนหนึ่งของการตรวจสอบผลการทดสอบ ถามว่าพรอมต์เทมเพลตของคุณแทนที่ตัวแปรถูกต้องหรือไม่ ไม่ใช่ว่า AI ให้คำตอบถูกต้องหรือไม่

ใช้ mocks สำหรับโมเดลภาษา พวกเขาเป็นการพึ่งพาภายนอกที่ช้า แพง และไม่แน่นอน การใช้ mock ทำให้การทดสอบเร็วในระดับมิลลิวินาทีแทนที่จะเป็นวินาที ฟรีโดยไม่เสียค่าใช้จ่าย API และแน่นอนเพราะผลลัพธ์เหมือนเดิมทุกครั้ง

รักษาการทดสอบให้แยกจากกัน การทดสอบแต่ละตัวควรกำหนดข้อมูลของตัวเอง ไม่พึ่งพาการทดสอบอื่น และทำความสะอาดหลังจบการทดสอบ การทดสอบควรผ่านไม่ว่าจะรันในลำดับใดก็ตาม

ทดสอบกรณีขอบเขตที่เกินเส้นทางปกติ ลองป้อนข้อมูลว่าง, ข้อมูลขนาดใหญ่มาก, ตัวอักษรพิเศษ, พารามิเตอร์ที่ไม่ถูกต้อง และเงื่อนไขขอบเขต กรณีเหล่านี้มักเปิดเผยบั๊กที่การใช้งานปกติไม่พบ

ใช้ชื่อที่บ่งบอกความหมาย เปรียบเทียบ `shouldMaintainConversationHistoryAcrossMultipleMessages()` กับ `test1()` ชื่อแรกบอกชัดเจนว่าทดสอบอะไร ทำให้การดีบักความล้มเหลวง่ายขึ้นมาก

## ขั้นตอนถัดไป

เมื่อคุณเข้าใจรูปแบบการทดสอบแล้ว ให้เจาะลึกแต่ละโมดูล:

- **[00 - เริ่มต้นอย่างรวดเร็ว](../00-quick-start/README.md)** - เริ่มต้นกับพื้นฐานเทมเพลตพรอมต์  
- **[01 - บทนำ](../01-introduction/README.md)** - เรียนรู้การจัดการหน่วยความจำการสนทนา  
- **[02 - การสร้างพรอมต์](../02/prompt-engineering/README.md)** - ฝึกฝนรูปแบบการสร้างพรอมต์ GPT-5.2  
- **[03 - RAG](../03-rag/README.md)** - สร้างระบบการสร้างข้อมูลเรียกค้นแบบเสริม  
- **[04 - เครื่องมือ](../04-tools/README.md)** - ใช้ฟังก์ชันการเรียกใช้งานและการเชื่อมโยงเครื่องมือ  
- **[05 - MCP](../05-mcp/README.md)** - รวมโปรโตคอล Model Context  

README ของแต่ละโมดูลอธิบายรายละเอียดแนวคิดที่ทดสอบในที่นี้

---

**การนำทาง:** [← กลับไปหน้าหลัก](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**คำปฏิเสธความรับผิดชอบ**:
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษาด้วย AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้เราจะพยายามให้ความถูกต้อง โปรดทราบว่าการแปลอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้อง เอกสารต้นฉบับในภาษาต้นทางควรถือเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลที่สำคัญ แนะนำให้ใช้การแปลโดยผู้เชี่ยวชาญทางภาษามนุษย์ เราจะไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความผิดที่เกิดขึ้นจากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->