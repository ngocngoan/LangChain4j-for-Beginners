# Kiểm thử Ứng dụng LangChain4j

## Mục Lục

- [Bắt đầu Nhanh](../../../docs)
- [Nội dung Kiểm thử](../../../docs)
- [Chạy Các Kiểm thử](../../../docs)
- [Chạy Kiểm thử trong VS Code](../../../docs)
- [Mẫu Kiểm thử](../../../docs)
- [Triết lý Kiểm thử](../../../docs)
- [Bước Tiếp theo](../../../docs)

Hướng dẫn này giúp bạn hiểu các kiểm thử minh họa cách kiểm thử ứng dụng AI mà không cần khóa API hoặc dịch vụ bên ngoài.

## Bắt đầu Nhanh

Chạy tất cả các kiểm thử chỉ với một lệnh:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

Khi tất cả các kiểm thử đều thành công, bạn sẽ thấy kết quả giống ảnh chụp màn hình dưới đây — các kiểm thử chạy với không có lỗi nào.

<img src="../../../translated_images/vi/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Thực thi kiểm thử thành công hiển thị tất cả các kiểm thử đều đạt*

## Nội dung Kiểm thử

Khóa học này tập trung vào **kiểm thử đơn vị** chạy tại chỗ. Mỗi kiểm thử minh họa một khái niệm LangChain4j riêng biệt. Kim tự tháp kiểm thử bên dưới cho thấy vị trí của kiểm thử đơn vị — chúng tạo thành nền tảng nhanh chóng và đáng tin cậy để xây dựng chiến lược kiểm thử của bạn.

<img src="../../../translated_images/vi/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Kim tự tháp kiểm thử biểu thị sự cân bằng giữa kiểm thử đơn vị (nhanh, tách biệt), kiểm thử tích hợp (các thành phần thực) và kiểm thử đầu-cuối. Khóa học này bao gồm kiểm thử đơn vị.*

| Module | Các Kiểm Thử | Trọng Tâm | Tập Tin Chính |
|--------|--------------|-----------|---------------|
| **00 - Bắt đầu Nhanh** | 6 | Mẫu prompt và thay thế biến | `SimpleQuickStartTest.java` |
| **01 - Giới thiệu** | 8 | Bộ nhớ hội thoại và trạng thái chat | `SimpleConversationTest.java` |
| **02 - Kỹ thuật Prompt** | 12 | Mẫu GPT-5.2, mức độ háo hức, đầu ra có cấu trúc | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Nhập văn bản, embeddings, tìm kiếm tương đồng | `DocumentServiceTest.java` |
| **04 - Công Cụ** | 12 | Gọi hàm và xâu chuỗi công cụ | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol với giao tiếp Stdio | `SimpleMcpTest.java` |

## Chạy Các Kiểm thử

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
mvn test -Dtest=SimpleConversationTest#nên duy trì lịch sử cuộc trò chuyện
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#nênDuyTrìLịchSửCuộcTròChuyện
```

## Chạy Kiểm thử trong VS Code

Nếu bạn sử dụng Visual Studio Code, Test Explorer cung cấp giao diện đồ họa để chạy và gỡ lỗi các kiểm thử.

<img src="../../../translated_images/vi/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*Test Explorer của VS Code hiển thị cây kiểm thử với tất cả các lớp kiểm thử Java và các phương thức kiểm thử riêng lẻ*

**Để chạy kiểm thử trong VS Code:**

1. Mở Test Explorer bằng cách nhấp vào biểu tượng bình thử nghiệm ở Thanh Hoạt động
2. Mở rộng cây kiểm thử để xem tất cả các module và lớp kiểm thử
3. Nhấp nút chạy bên cạnh bất kỳ kiểm thử nào để chạy riêng
4. Nhấp "Run All Tests" để thực thi toàn bộ bộ kiểm thử
5. Nhấp chuột phải lên kiểm thử và chọn "Debug Test" để thiết lập điểm dừng và bước qua mã

Test Explorer hiển thị dấu tích màu xanh khi kiểm thử thành công và cung cấp thông báo lỗi chi tiết khi kiểm thử thất bại.

## Mẫu Kiểm thử

### Mẫu 1: Kiểm thử Mẫu Prompt

Mẫu đơn giản nhất kiểm thử các mẫu prompt mà không gọi mô hình AI. Bạn kiểm tra rằng thay thế biến hoạt động chính xác và prompt được định dạng như mong đợi.

<img src="../../../translated_images/vi/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Kiểm thử mẫu prompt hiển thị luồng thay thế biến: mẫu với các chỗ giữ chỗ → áp dụng giá trị → xác nhận đầu ra đã định dạng*

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
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#định dạngMẫuCâuHỏiKiểmTra
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#kiểm traĐịnhDạngMẫuCâuLệnh
```

### Mẫu 2: Giả lập Mô hình Ngôn ngữ

Khi kiểm thử logic hội thoại, dùng Mockito để tạo mô hình giả trả về phản hồi đã định trước. Điều này làm cho kiểm thử nhanh, miễn phí và xác định được kết quả.

<img src="../../../translated_images/vi/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*So sánh cho thấy vì sao mocks được ưa thích trong kiểm thử: nhanh, miễn phí, xác định, không cần khóa API*

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

Mẫu này xuất hiện trong `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mô hình giả đảm bảo hành vi nhất quán để bạn có thể xác nhận quản lý bộ nhớ hoạt động đúng.

### Mẫu 3: Kiểm thử Tách biệt Hội thoại

Bộ nhớ hội thoại phải giữ các người dùng riêng biệt. Kiểm thử này xác nhận rằng không có sự trộn lẫn ngữ cảnh giữa các hội thoại.

<img src="../../../translated_images/vi/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Kiểm thử tách biệt hội thoại cho thấy bộ nhớ lưu trữ riêng của từng người dùng để tránh trộn lẫn ngữ cảnh*

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

Mỗi hội thoại duy trì lịch sử độc lập riêng. Trong hệ thống sản xuất, tách biệt này rất quan trọng cho các ứng dụng đa người dùng.

### Mẫu 4: Kiểm thử Công Cụ Riêng biệt

Công cụ là các hàm mà AI có thể gọi. Kiểm thử trực tiếp để đảm bảo chúng hoạt động đúng bất kể quyết định của AI.

<img src="../../../translated_images/vi/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Kiểm thử công cụ độc lập cho thấy thực thi công cụ giả mà không gọi AI để xác nhận logic nghiệp vụ*

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

Các kiểm thử này từ `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` xác nhận logic công cụ mà không liên quan AI. Ví dụ xâu chuỗi cho thấy đầu ra của một công cụ được đưa làm đầu vào cho công cụ khác.

### Mẫu 5: Kiểm thử RAG Tại Bộ Nhớ Trong

Hệ thống RAG truyền thống yêu cầu cơ sở dữ liệu vector và dịch vụ embedding. Mẫu tại bộ nhớ trong cho phép bạn kiểm thử toàn bộ quy trình mà không cần phụ thuộc bên ngoài.

<img src="../../../translated_images/vi/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*Quy trình kiểm thử RAG trong bộ nhớ cho thấy phân tích tài liệu, lưu trữ embedding và tìm kiếm tương đồng mà không cần cơ sở dữ liệu*

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

Kiểm thử này ở `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` tạo tài liệu trong bộ nhớ và xác nhận việc chia nhỏ và xử lý metadata.

### Mẫu 6: Kiểm thử Tích hợp MCP

Module MCP kiểm thử tích hợp Model Context Protocol sử dụng giao tiếp stdio. Các kiểm thử này xác nhận ứng dụng của bạn có thể sinh và giao tiếp với các máy chủ MCP dưới dạng tiến trình phụ.

Các kiểm thử trong `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` xác nhận hành vi client MCP.

**Chạy chúng:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Triết lý Kiểm thử

Kiểm thử mã của bạn, không phải AI. Các kiểm thử nên xác nhận mã bạn viết bằng cách kiểm tra cách xây dựng prompt, cách quản lý bộ nhớ, và cách công cụ thực thi. Phản hồi AI thay đổi và không nên là một phần của các khẳng định kiểm thử. Hãy tự hỏi mẫu prompt của bạn có thay thế biến đúng không, chứ không phải AI có đưa ra câu trả lời đúng hay không.

Sử dụng giả lập cho các mô hình ngôn ngữ. Chúng là phụ thuộc bên ngoài chậm chạp, đắt đỏ và không xác định được kết quả. Giả lập làm kiểm thử nhanh với mili giây thay vì giây, miễn phí không tốn phí API, và xác định được cùng một kết quả mỗi lần.

Giữ cho kiểm thử độc lập. Mỗi kiểm thử nên tự thiết lập dữ liệu riêng, không phụ thuộc kiểm thử khác, và dọn dẹp sau khi chạy. Kiểm thử nên thành công bất kể thứ tự chạy như thế nào.

Kiểm thử các trường hợp biên bên cạnh đường đi thuận lợi. Thử đầu vào rỗng, đầu vào rất lớn, ký tự đặc biệt, tham số không hợp lệ, và điều kiện biên. Những trường hợp này thường phát hiện lỗi mà sử dụng bình thường không thấy.

Dùng tên mô tả. So sánh `shouldMaintainConversationHistoryAcrossMultipleMessages()` với `test1()`. Cái đầu tiên cho bạn biết chính xác điều gì đang được kiểm thử, giúp gỡ lỗi dễ dàng hơn nhiều.

## Bước Tiếp theo

Bây giờ bạn đã hiểu các mẫu kiểm thử, hãy đi sâu hơn vào từng module:

- **[00 - Bắt đầu Nhanh](../00-quick-start/README.md)** - Bắt đầu với kiến thức cơ bản về mẫu prompt
- **[01 - Giới thiệu](../01-introduction/README.md)** - Tìm hiểu quản lý bộ nhớ hội thoại
- **[02 - Kỹ thuật Prompt](../02-prompt-engineering/README.md)** - Làm chủ các mẫu prompt GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Xây dựng hệ thống tạo nội dung tăng cường truy xuất
- **[04 - Công Cụ](../04-tools/README.md)** - Triển khai gọi hàm và xâu chuỗi công cụ
- **[05 - MCP](../05-mcp/README.md)** - Tích hợp Model Context Protocol

README của từng module cung cấp giải thích chi tiết các khái niệm kiểm thử tại đây.

---

**Điều hướng:** [← Quay lại Chính](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố từ chối trách nhiệm**:
Tài liệu này đã được dịch sử dụng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng các bản dịch tự động có thể chứa lỗi hoặc sai sót. Tài liệu gốc bằng ngôn ngữ mẹ đẻ được coi là nguồn chính thức và có thẩm quyền. Đối với các thông tin quan trọng, nên sử dụng dịch vụ dịch thuật chuyên nghiệp do con người thực hiện. Chúng tôi không chịu trách nhiệm đối với bất kỳ sự hiểu lầm hoặc giải thích sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->