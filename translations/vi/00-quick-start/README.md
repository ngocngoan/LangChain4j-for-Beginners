# Module 00: Bắt Đầu Nhanh

## Mục Lục

- [Giới thiệu](../../../00-quick-start)
- [LangChain4j là gì?](../../../00-quick-start)
- [Phụ thuộc của LangChain4j](../../../00-quick-start)
- [Yêu cầu trước](../../../00-quick-start)
- [Cài đặt](../../../00-quick-start)
  - [1. Lấy Token GitHub của bạn](../../../00-quick-start)
  - [2. Cài đặt Token của bạn](../../../00-quick-start)
- [Chạy Các Ví Dụ](../../../00-quick-start)
  - [1. Trò chuyện cơ bản](../../../00-quick-start)
  - [2. Mẫu nhắc](../../../00-quick-start)
  - [3. Gọi hàm](../../../00-quick-start)
  - [4. Hỏi & Đáp tài liệu (Easy RAG)](../../../00-quick-start)
  - [5. Trí tuệ nhân tạo có trách nhiệm](../../../00-quick-start)
- [Mỗi Ví Dụ Cho Thấy Điều Gì](../../../00-quick-start)
- [Bước Tiếp Theo](../../../00-quick-start)
- [Khắc phục sự cố](../../../00-quick-start)

## Giới thiệu

Bắt đầu nhanh này nhằm giúp bạn nhanh chóng làm quen và chạy với LangChain4j. Nó bao gồm những điều cơ bản nhất về xây dựng ứng dụng AI với LangChain4j và GitHub Models. Trong các module tiếp theo, bạn sẽ chuyển sang Azure OpenAI và GPT-5.2 cũng như đào sâu hơn vào từng khái niệm.

## LangChain4j là gì?

LangChain4j là thư viện Java giúp đơn giản hóa việc xây dựng các ứng dụng sử dụng AI. Thay vì phải xử lý các HTTP client và phân tích cú pháp JSON, bạn làm việc với các API Java sạch sẽ.

“Chain” trong LangChain nghĩa là liên kết các thành phần lại với nhau — bạn có thể liên kết một lời nhắc đến một mô hình đến một bộ phân tích, hoặc liên kết nhiều lần gọi AI mà đầu ra của lần này trở thành đầu vào cho lần kế tiếp. Bắt đầu nhanh này tập trung vào những điều cơ bản trước khi khám phá các chuỗi phức tạp hơn.

<img src="../../../translated_images/vi/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Liên kết các thành phần trong LangChain4j - các viên gạch xây dựng kết nối tạo nên quy trình AI mạnh mẽ*

Chúng ta sẽ dùng ba thành phần cốt lõi:

**ChatModel** - Giao diện tương tác với mô hình AI. Gọi `model.chat("prompt")` và nhận một chuỗi phản hồi. Chúng ta dùng `OpenAiOfficialChatModel` hoạt động với các endpoint tương thích OpenAI như GitHub Models.

**AiServices** - Tạo giao diện dịch vụ AI an toàn về kiểu dữ liệu. Định nghĩa các phương thức, chú thích chúng bằng `@Tool`, và LangChain4j sẽ điều phối. AI tự động gọi các phương thức Java của bạn khi cần.

**MessageWindowChatMemory** - Duy trì lịch sử hội thoại. Nếu không có nó, mỗi yêu cầu là độc lập. Có nó, AI sẽ nhớ các tin nhắn trước và giữ ngữ cảnh qua nhiều lượt gọi.

<img src="../../../translated_images/vi/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Kiến trúc LangChain4j - các thành phần cốt lõi làm việc cùng nhau để cung cấp sức mạnh cho ứng dụng AI của bạn*

## Phụ thuộc của LangChain4j

Bắt đầu nhanh này sử dụng ba phụ thuộc Maven trong [`pom.xml`](../../../00-quick-start/pom.xml):

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

Module `langchain4j-open-ai-official` cung cấp lớp `OpenAiOfficialChatModel` kết nối tới API tương thích OpenAI. GitHub Models dùng cùng định dạng API nên không cần bộ chuyển đổi đặc biệt - chỉ cần trỏ URL gốc tới `https://models.github.ai/inference`.

Module `langchain4j-easy-rag` cung cấp tự động chia nhỏ tài liệu, tạo embedding, và truy xuất nên bạn có thể xây dựng ứng dụng RAG mà không phải cấu hình từng bước thủ công.

## Yêu cầu trước

**Dùng Dev Container?** Java và Maven đã được cài sẵn. Bạn chỉ cần Token Truy cập Cá nhân GitHub.

**Phát triển cục bộ:**
- Java 21+, Maven 3.9+
- Token Truy cập Cá nhân GitHub (hướng dẫn bên dưới)

> **Lưu ý:** Module này sử dụng `gpt-4.1-nano` từ GitHub Models. Đừng sửa tên mô hình trong mã — nó được cấu hình để hoạt động với các mô hình có sẵn của GitHub.

## Cài đặt

### 1. Lấy Token GitHub của bạn

1. Truy cập [Cài đặt GitHub → Token Truy cập Cá nhân](https://github.com/settings/personal-access-tokens)
2. Nhấn "Generate new token"
3. Đặt tên mô tả (ví dụ: "LangChain4j Demo")
4. Chọn thời hạn (khuyến nghị 7 ngày)
5. Dưới "Quyền tài khoản", tìm "Models" và đặt là "Chỉ đọc"
6. Nhấn "Generate token"
7. Sao chép và lưu token — bạn sẽ không thấy lại lần nữa

### 2. Cài đặt Token của bạn

**Chọn 1: Dùng VS Code (được khuyên dùng)**

Nếu bạn dùng VS Code, thêm token vào file `.env` ở thư mục gốc dự án:

Nếu file `.env` không tồn tại, sao chép `.env.example` thành `.env` hoặc tạo file `.env` mới ở gốc dự án.

**Ví dụ file `.env`:**
```bash
# Trong /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Sau đó bạn chỉ cần click chuột phải vào bất kỳ file demo nào (ví dụ `BasicChatDemo.java`) trong Explorer và chọn **"Run Java"** hoặc dùng cấu hình khởi chạy ở bảng Run and Debug.

**Chọn 2: Dùng Terminal**

Đặt token làm biến môi trường:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Chạy Các Ví Dụ

**Dùng VS Code:** Chỉ cần click chuột phải vào file demo bất kỳ trong Explorer và chọn **"Run Java"**, hoặc dùng cấu hình khởi chạy từ bảng Run and Debug (nhớ đã thêm token vào `.env` trước).

**Dùng Maven:** Bạn cũng có thể chạy từ dòng lệnh:

### 1. Trò chuyện cơ bản

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Mẫu nhắc

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Hiển thị các mẫu nhắc zero-shot, few-shot, chuỗi suy nghĩ, và dựa trên vai trò.

### 3. Gọi hàm

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI tự động gọi các phương thức Java của bạn khi cần thiết.

### 4. Hỏi & Đáp tài liệu (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Hỏi các câu về tài liệu của bạn sử dụng Easy RAG với nhúng (embedding) và truy xuất tự động.

### 5. Trí tuệ nhân tạo có trách nhiệm

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Xem cách bộ lọc an toàn AI chặn nội dung có hại.

## Mỗi Ví Dụ Cho Thấy Điều Gì

**Trò chuyện cơ bản** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Bắt đầu tại đây để thấy LangChain4j đơn giản nhất. Bạn sẽ tạo một `OpenAiOfficialChatModel`, gửi prompt với `.chat()`, và nhận lại câu trả lời. Điều này minh họa nền tảng: cách khởi tạo mô hình với endpoint và API key tùy chỉnh. Khi bạn đã hiểu mẫu này, mọi thứ khác sẽ xây dựng dựa trên đó.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) và hỏi:
> - "Làm sao để chuyển từ GitHub Models sang Azure OpenAI trong mã này?"
> - "Có những tham số nào khác tôi có thể cấu hình trong OpenAiOfficialChatModel.builder()?"
> - "Làm cách nào để thêm phản hồi dạng streaming thay vì chờ phản hồi hoàn chỉnh?"

**Kỹ thuật Nhắc** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Giờ bạn đã biết cách nói chuyện với mô hình, hãy khám phá cách bạn nói với nó. Demo này dùng cùng cấu hình mô hình nhưng trình bày năm mẫu nhắc khác nhau. Thử prompt zero-shot chỉ dẫn trực tiếp, prompt few-shot học từ ví dụ, prompt chuỗi suy nghĩ thể hiện các bước lí luận, và prompt dựa trên vai trò để thiết lập ngữ cảnh. Bạn sẽ thấy mô hình cùng một cách nhưng kết quả khác biệt đáng kể dựa vào cách bạn đặt câu hỏi.

Demo cũng trình bày các mẫu prompt template, là cách hiệu quả để tạo prompt tái sử dụng với biến số.
Ví dụ dưới dùng `PromptTemplate` của LangChain4j điền biến. AI sẽ trả lời dựa trên điểm đến và hoạt động được cung cấp.

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) và hỏi:
> - "Sự khác nhau giữa zero-shot và few-shot prompting là gì, và khi nào tôi nên dùng mỗi loại?"
> - "Tham số temperature ảnh hưởng thế nào đến câu trả lời của mô hình?"
> - "Có những kỹ thuật nào để ngăn chặn tấn công prompt injection trong sản xuất?"
> - "Làm sao để tạo các object PromptTemplate tái sử dụng cho các mẫu phổ biến?"

**Tích hợp Công cụ** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Đây là điểm LangChain4j trở nên mạnh mẽ. Bạn sẽ dùng `AiServices` để tạo trợ lý AI có thể gọi các phương thức Java của bạn. Chỉ cần chú thích phương thức với `@Tool("mô tả")` và LangChain4j sẽ lo phần còn lại — AI tự động quyết định khi nào dùng công cụ nào dựa trên yêu cầu của người dùng. Điều này thể hiện kỹ thuật gọi hàm, một kỹ thuật quan trọng để xây dựng AI có thể thực thi hành động, không chỉ trả lời câu hỏi.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) và hỏi:
> - "Chú thích @Tool hoạt động ra sao và LangChain4j làm gì với nó phía sau?"
> - "AI có thể gọi nhiều công cụ theo chuỗi để giải quyết vấn đề phức tạp không?"
> - "Nếu một công cụ phát sinh ngoại lệ thì sao — tôi nên xử lý lỗi thế nào?"
> - "Làm sao để tích hợp một API thật thay vì ví dụ máy tính này?"

**Hỏi & Đáp tài liệu (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Ở đây bạn sẽ thấy RAG (retrieval-augmented generation) sử dụng cách tiếp cận "Easy RAG" của LangChain4j. Tài liệu được tải lên, tự động chia nhỏ và tạo embedding vào bộ nhớ trong, rồi bộ truy xuất nội dung cung cấp các đoạn liên quan cho AI lúc truy vấn. AI trả lời dựa trên tài liệu của bạn, không phải kiến thức chung.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) và hỏi:
> - "RAG tránh ảo giác AI thế nào so với dùng dữ liệu huấn luyện của mô hình?"
> - "Sự khác biệt giữa cách tiếp cận dễ dàng này và pipeline RAG tùy chỉnh là gì?"
> - "Làm sao tôi mở rộng để xử lý nhiều tài liệu hoặc kho kiến thức lớn?"

**Trí tuệ nhân tạo có trách nhiệm** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Xây dựng an toàn AI với phòng thủ nhiều lớp. Demo này trình bày hai tầng bảo vệ phối hợp:

**Phần 1: LangChain4j Input Guardrails** - Chặn prompt nguy hiểm trước khi chúng đến LLM. Tạo guardrails tùy chỉnh kiểm tra từ khóa hoặc mẫu bị cấm. Chúng chạy trong mã của bạn nên nhanh và miễn phí.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**Phần 2: Bộ lọc an toàn của nhà cung cấp** - GitHub Models có bộ lọc tích hợp bắt các điều guardrails có thể bỏ sót. Bạn sẽ thấy chặn cứng (lỗi HTTP 400) với vi phạm nghiêm trọng và từ chối nhẹ nhàng khi AI lịch sự từ chối.

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) và hỏi:
> - "InputGuardrail là gì và làm sao tôi tạo guardrail của riêng mình?"
> - "Sự khác nhau giữa chặn cứng và từ chối nhẹ nhàng là gì?"
> - "Tại sao sử dụng đồng thời guardrails và bộ lọc nhà cung cấp?"

## Bước Tiếp Theo

**Module tiếp theo:** [01-introduction - Bắt đầu với LangChain4j](../01-introduction/README.md)

---

**Điều hướng:** [← Quay lại Trang chính](../README.md) | [Tiếp theo: Module 01 - Giới thiệu →](../01-introduction/README.md)

---

## Khắc phục sự cố

### Lần Đầu Xây dựng với Maven

**Vấn đề**: Lần chạy `mvn clean compile` hoặc `mvn package` đầu tiên rất lâu (10-15 phút)

**Nguyên nhân**: Maven cần tải về tất cả phụ thuộc dự án (Spring Boot, thư viện LangChain4j, SDK Azure, v.v.) trong lần build đầu tiên.

**Giải pháp**: Đây là hành vi bình thường. Các lần build sau sẽ nhanh hơn nhiều do phụ thuộc được lưu cache cục bộ. Thời gian tải phụ thuộc tốc độ mạng của bạn.

### Cú Pháp Lệnh Maven trên PowerShell

**Vấn đề**: Các lệnh Maven bị lỗi `Unknown lifecycle phase ".mainClass=..."`
**Nguyên nhân**: PowerShell hiểu dấu `=` là toán tử gán biến, khiến cú pháp thuộc tính Maven bị phá vỡ

**Giải pháp**: Sử dụng toán tử dừng phân tích cú pháp `--%` trước lệnh Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Toán tử `--%` cho PowerShell biết truyền tất cả các đối số còn lại một cách nguyên văn đến Maven mà không giải thích.

### Hiển thị Biểu tượng cảm xúc trên Windows PowerShell

**Vấn đề**: Phản hồi AI hiển thị ký tự rác (ví dụ: `????` hoặc `â??`) thay vì biểu tượng cảm xúc trong PowerShell

**Nguyên nhân**: Mã hóa mặc định của PowerShell không hỗ trợ biểu tượng cảm xúc UTF-8

**Giải pháp**: Chạy lệnh này trước khi thực thi ứng dụng Java:
```cmd
chcp 65001
```

Lệnh này ép mã hóa UTF-8 trong terminal. Ngoài ra, có thể dùng Windows Terminal vì nó hỗ trợ Unicode tốt hơn.

### Gỡ lỗi các cuộc gọi API

**Vấn đề**: Lỗi xác thực, giới hạn tần suất, hoặc phản hồi không mong đợi từ mô hình AI

**Giải pháp**: Các ví dụ có chứa `.logRequests(true)` và `.logResponses(true)` để hiển thị các cuộc gọi API trên bảng điều khiển. Điều này giúp khắc phục lỗi xác thực, giới hạn tần suất hoặc phản hồi không mong đợi. Hãy loại bỏ các cờ này trong môi trường sản xuất để giảm nhiễu log.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố từ chối trách nhiệm**:  
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo tính chính xác, xin lưu ý rằng các bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ bản địa nên được coi là nguồn thông tin chính thức. Đối với các thông tin quan trọng, nên sử dụng dịch vụ dịch thuật chuyên nghiệp bởi con người. Chúng tôi không chịu trách nhiệm về bất kỳ sự hiểu lầm hay nhầm lẫn nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->