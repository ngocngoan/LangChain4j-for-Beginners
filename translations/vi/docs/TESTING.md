# Kiểm thử Ứng dụng LangChain4j

## Mục lục

- [Bắt đầu nhanh](../../../docs)
- [Những gì các kiểm thử bao gồm](../../../docs)
- [Chạy kiểm thử](../../../docs)
- [Chạy kiểm thử trong VS Code](../../../docs)
- [Mẫu kiểm thử](../../../docs)
- [Triết lý kiểm thử](../../../docs)
- [Bước tiếp theo](../../../docs)

Hướng dẫn này đưa bạn qua các bài kiểm thử minh họa cách kiểm thử ứng dụng AI mà không cần khóa API hay dịch vụ bên ngoài.

## Bắt đầu nhanh

Chạy tất cả các kiểm thử với một lệnh duy nhất:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/vi/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Thực thi kiểm thử thành công cho thấy tất cả các kiểm thử đều đậu với số lỗi bằng không*

## Những gì các kiểm thử bao gồm

Khóa học này tập trung vào **kiểm thử đơn vị** chạy cục bộ. Mỗi kiểm thử minh họa một khái niệm LangChain4j cụ thể một cách độc lập.

<img src="../../../translated_images/vi/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Kim tự tháp kiểm thử thể hiện sự cân bằng giữa kiểm thử đơn vị (nhanh, tách biệt), kiểm thử tích hợp (các thành phần thực) và kiểm thử đầu-cuối. Khóa đào tạo này bao gồm kiểm thử đơn vị.*

| Module | Kiểm thử | Tập trung | File chính |
|--------|---------|----------|------------|
| **00 - Bắt đầu nhanh** | 6 | Mẫu lời nhắc và thay thế biến | `SimpleQuickStartTest.java` |
| **01 - Giới thiệu** | 8 | Bộ nhớ hội thoại và chat trạng thái | `SimpleConversationTest.java`` |
| **02 - Kỹ thuật Prompt** | 12 | Mẫu GPT-5.2, mức độ háo hức, đầu ra có cấu trúc | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Tiếp nhận tài liệu, embeddings, tìm kiếm tương đồng | `DocumentServiceTest.java` |
| **04 - Công cụ** | 12 | Gọi hàm và chuỗi công cụ | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Giao thức Model Context với giao tiếp Stdio | `SimpleMcpTest.java` |

## Chạy kiểm thử

**Chạy tất cả kiểm thử từ thư mục gốc:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Chạy kiểm thử cho một module cụ thể:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Hoặc từ root
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Hoặc từ root
mvn --% test -pl 01-introduction
```

**Chạy một lớp kiểm thử duy nhất:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Chạy một phương thức kiểm thử cụ thể:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#nên duy trì lịch sử hội thoại
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#nên duy trì lịch sử cuộc trò chuyện
```

## Chạy kiểm thử trong VS Code

Nếu bạn dùng Visual Studio Code, Test Explorer cung cấp giao diện đồ họa để chạy và gỡ lỗi các kiểm thử.

<img src="../../../translated_images/vi/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer thể hiện cây kiểm thử với tất cả các lớp kiểm thử Java và các phương thức kiểm thử riêng lẻ*

**Để chạy kiểm thử trong VS Code:**

1. Mở Test Explorer bằng cách bấm biểu tượng bình thí nghiệm trên Thanh hoạt động
2. Mở rộng cây kiểm thử để xem các module và lớp kiểm thử
3. Bấm nút chạy cạnh bất kỳ kiểm thử nào để chạy riêng lẻ
4. Bấm "Run All Tests" để chạy toàn bộ bộ kiểm thử
5. Nhấp chuột phải vào kiểm thử và chọn "Debug Test" để đặt breakpoints và bước qua mã

Test Explorer hiện dấu kiểm màu xanh khi kiểm thử thành công và cung cấp thông báo lỗi chi tiết khi kiểm thử thất bại.

## Mẫu kiểm thử

### Mẫu 1: Kiểm thử mẫu lời nhắc

Mẫu đơn giản nhất kiểm thử các mẫu lời nhắc mà không gọi bất kỳ mô hình AI nào. Bạn xác thực rằng thay thế biến hoạt động chính xác và lời nhắc được định dạng như mong đợi.

<img src="../../../translated_images/vi/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Kiểm thử mẫu lời nhắc thể hiện luồng thay thế biến: mẫu với các chỗ giữ chỗ → giá trị được áp dụng → đầu ra được kiểm tra định dạng*

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

Kiểm thử này nằm trong `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Chạy nó:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#định dạng mẫu nhắc thử nghiệm
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#kiểm traĐịnhDạngMẫuNhắcNhở
```

### Mẫu 2: Mô phỏng mô hình ngôn ngữ

Khi kiểm thử logic hội thoại, sử dụng Mockito để tạo mô hình giả trả về câu trả lời định sẵn. Điều này khiến kiểm thử nhanh, miễn phí và có kết quả nhất quán.

<img src="../../../translated_images/vi/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*So sánh cho thấy vì sao mô hình giả được ưu tiên cho kiểm thử: nhanh, miễn phí, xác định trước và không cần khóa API*

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
        assertThat(history).hasSize(6); // 3 tin nhắn người dùng + 3 tin nhắn AI
    }
}
```

Mẫu này xuất hiện trong `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mô phỏng đảm bảo hành vi nhất quán để bạn có thể xác nhận việc quản lý bộ nhớ hoạt động đúng.

### Mẫu 3: Kiểm thử tách biệt hội thoại

Bộ nhớ hội thoại phải giữ các người dùng riêng biệt. Kiểm thử này xác nhận hội thoại không bị trộn lẫn bối cảnh.

<img src="../../../translated_images/vi/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Kiểm thử tách biệt hội thoại thể hiện bộ nhớ riêng cho từng người dùng khác nhau để tránh trộn bối cảnh*

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

Mỗi hội thoại duy trì lịch sử riêng biệt của nó. Trong hệ thống sản xuất, sự tách biệt này rất quan trọng cho ứng dụng đa người dùng.

### Mẫu 4: Kiểm thử Công cụ độc lập

Công cụ là các hàm AI có thể gọi. Kiểm thử trực tiếp để đảm bảo chúng hoạt động đúng mà không phụ thuộc quyết định AI.

<img src="../../../translated_images/vi/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Kiểm thử công cụ độc lập thể hiện thực thi công cụ mô phỏng không gọi AI để xác nhận logic nghiệp vụ*

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

Các kiểm thử này từ `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` xác thực logic công cụ không liên quan đến AI. Ví dụ chuỗi thể hiện làm thế nào đầu ra của một công cụ làm đầu vào cho công cụ khác.

### Mẫu 5: Kiểm thử RAG trong bộ nhớ

Hệ thống RAG truyền thống yêu cầu cơ sở dữ liệu vector và dịch vụ embedding. Mẫu bộ nhớ cho phép kiểm thử toàn bộ quy trình mà không cần phụ thuộc bên ngoài.

<img src="../../../translated_images/vi/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*Luồng kiểm thử RAG trong bộ nhớ thể hiện phân tích tài liệu, lưu trữ embedding, và tìm kiếm tương đồng mà không cần cơ sở dữ liệu*

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

Kiểm thử này từ `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` tạo tài liệu trong bộ nhớ và xác nhận phân đoạn cũng như xử lý siêu dữ liệu.

### Mẫu 6: Kiểm thử tích hợp MCP

Module MCP kiểm thử giao thức Model Context với truyền thông stdio. Các kiểm thử này xác nhận ứng dụng của bạn có thể khởi tạo và giao tiếp với máy chủ MCP như các tiến trình con.

Các kiểm thử trong `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` xác thực hành vi client MCP.

**Chạy chúng:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Triết lý kiểm thử

Kiểm thử mã của bạn, không phải AI. Các kiểm thử nên xác nhận mã bạn viết bằng cách kiểm tra cách dựng lời nhắc, cách quản lý bộ nhớ, và cách các công cụ thực thi. Phản hồi AI rất khác nhau và không nên là phần của khẳng định kiểm thử. Hãy tự hỏi mẫu lời nhắc có thay thế biến chính xác không, chứ không phải AI có đáp án đúng hay không.

Dùng mô phỏng cho mô hình ngôn ngữ. Chúng là phụ thuộc bên ngoài chậm, tốn kém, và không xác định. Mô phỏng làm cho kiểm thử nhanh với mili giây thay vì vài giây, miễn phí không tốn phí API, và xác định với kết quả giống nhau mỗi lần.

Giữ kiểm thử độc lập. Mỗi kiểm thử nên tự chuẩn bị dữ liệu, không phụ thuộc kiểm thử khác, và dọn dẹp sau khi chạy. Kiểm thử nên đậu bất kể thứ tự thực thi.

Kiểm thử các trường hợp biên ngoài đường đi lý tưởng. Thử đầu vào rỗng, đầu vào rất lớn, ký tự đặc biệt, tham số không hợp lệ, và điều kiện biên. Những điều này thường phát hiện lỗi không thấy khi dùng bình thường.

Dùng tên mô tả. So sánh `shouldMaintainConversationHistoryAcrossMultipleMessages()` với `test1()`. Tên đầu tiên cho bạn biết chính xác điều đang được kiểm thử, giúp dễ gỡ lỗi khi thất bại hơn nhiều.

## Bước tiếp theo

Giờ bạn đã hiểu các mẫu kiểm thử, hãy đi sâu hơn vào từng module:

- **[00 - Bắt đầu nhanh](../00-quick-start/README.md)** - Bắt đầu với cơ bản về mẫu lời nhắc
- **[01 - Giới thiệu](../01-introduction/README.md)** - Tìm hiểu quản lý bộ nhớ hội thoại
- **[02 - Kỹ thuật Prompt](../02-prompt-engineering/README.md)** - Làm chủ các mẫu prompt GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Xây dựng hệ thống tăng cường truy xuất
- **[04 - Công cụ](../04-tools/README.md)** - Thực thi gọi hàm và chuỗi công cụ
- **[05 - MCP](../05-mcp/README.md)** - Tích hợp Giao thức Model Context

README mỗi module cung cấp giải thích chi tiết về các khái niệm được kiểm thử ở đây.

---

**Điều hướng:** [← Quay lại Chính](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố từ chối trách nhiệm**:  
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng các bản dịch tự động có thể chứa lỗi hoặc sai sót. Tài liệu gốc bằng ngôn ngữ nguyên bản nên được xem là nguồn tham khảo chính xác. Đối với các thông tin quan trọng, khuyến nghị sử dụng dịch vụ dịch thuật chuyên nghiệp bởi con người. Chúng tôi không chịu trách nhiệm về bất kỳ hiểu lầm hoặc giải thích sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->