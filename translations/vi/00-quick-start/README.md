# Module 00: Bắt Đầu Nhanh

## Mục Lục

- [Giới thiệu](../../../00-quick-start)
- [LangChain4j là gì?](../../../00-quick-start)
- [Các phụ thuộc của LangChain4j](../../../00-quick-start)
- [Yêu cầu trước](../../../00-quick-start)
- [Thiết lập](../../../00-quick-start)
  - [1. Lấy Mã Token GitHub của bạn](../../../00-quick-start)
  - [2. Đặt Mã Token của bạn](../../../00-quick-start)
- [Chạy Các Ví Dụ](../../../00-quick-start)
  - [1. Trò chuyện cơ bản](../../../00-quick-start)
  - [2. Mẫu Lời nhắc](../../../00-quick-start)
  - [3. Gọi Hàm](../../../00-quick-start)
  - [4. Hỏi Đáp Văn Bản (Easy RAG)](../../../00-quick-start)
  - [5. AI Có Trách Nhiệm](../../../00-quick-start)
- [Mỗi Ví Dụ Cho Thấy Điều Gì](../../../00-quick-start)
- [Bước Tiếp Theo](../../../00-quick-start)
- [Khắc Phục Sự Cố](../../../00-quick-start)

## Giới thiệu

Khởi động nhanh này nhằm giúp bạn bắt đầu với LangChain4j nhanh nhất có thể. Nó bao gồm những điều cơ bản nhất về xây dựng ứng dụng AI với LangChain4j và GitHub Models. Trong các module tiếp theo, bạn sẽ dùng Azure OpenAI với LangChain4j để xây dựng các ứng dụng nâng cao hơn.

## LangChain4j là gì?

LangChain4j là một thư viện Java giúp đơn giản hóa việc xây dựng các ứng dụng AI. Thay vì phải xử lý các client HTTP và phân tích JSON, bạn làm việc với các API Java rõ ràng.

“Chain” trong LangChain ám chỉ việc liên kết nhiều thành phần - bạn có thể liên kết một lời nhắc với một mô hình, rồi đến bộ phân tích cú pháp, hoặc liên kết nhiều lần gọi AI với nhau, nơi đầu ra của lần này là đầu vào cho lần tiếp theo. Khởi động nhanh này tập trung vào các nguyên tắc cơ bản trước khi khám phá các chuỗi phức tạp hơn.

<img src="../../../translated_images/vi/langchain-concept.ad1fe6cf063515e1.webp" alt="Khái niệm Liên kết trong LangChain4j" width="800"/>

*Liên kết các thành phần trong LangChain4j - các khối xây dựng kết nối để tạo ra quy trình AI mạnh mẽ*

Chúng ta sẽ sử dụng ba thành phần cốt lõi:

**ChatModel** - Giao diện tương tác với mô hình AI. Gọi `model.chat("prompt")` và nhận chuỗi phản hồi. Chúng ta dùng `OpenAiOfficialChatModel` làm việc với các điểm cuối tương thích OpenAI như GitHub Models.

**AiServices** - Tạo các giao diện dịch vụ AI an toàn về kiểu. Định nghĩa phương thức, chú thích bằng `@Tool`, và LangChain4j sẽ xử lý phần điều phối. AI tự động gọi các phương thức Java của bạn khi cần thiết.

**MessageWindowChatMemory** - Duy trì lịch sử hội thoại. Nếu không có nó, mỗi yêu cầu là độc lập. Với nó, AI nhớ các tin nhắn trước và duy trì ngữ cảnh qua nhiều lượt nói chuyện.

<img src="../../../translated_images/vi/architecture.eedc993a1c576839.webp" alt="Kiến trúc LangChain4j" width="800"/>

*Kiến trúc LangChain4j - các thành phần cốt lõi phối hợp cùng nhau để chạy các ứng dụng AI của bạn*

## Các phụ thuộc của LangChain4j

Khởi động nhanh này sử dụng ba phụ thuộc Maven trong file [`pom.xml`](../../../00-quick-start/pom.xml):

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
  
Module `langchain4j-open-ai-official` cung cấp lớp `OpenAiOfficialChatModel` kết nối với API tương thích OpenAI. GitHub Models dùng cùng định dạng API nên không cần adapter đặc biệt - chỉ cần trỏ đường dẫn cơ sở đến `https://models.github.ai/inference`.

Module `langchain4j-easy-rag` cung cấp tự động chia nhỏ tài liệu, nhúng, và truy xuất giúp bạn xây dựng ứng dụng RAG mà không cần cấu hình thủ công từng bước.

## Yêu cầu trước

**Dùng Dev Container?** Java và Maven đã được cài sẵn. Bạn chỉ cần có Token Truy Cập Cá Nhân GitHub.

**Phát triển cục bộ:**  
- Java 21+, Maven 3.9+  
- Token Truy Cập Cá Nhân GitHub (hướng dẫn bên dưới)

> **Lưu ý:** Module này sử dụng `gpt-4.1-nano` từ GitHub Models. Không thay đổi tên mô hình trong mã - nó đã được cấu hình để hoạt động với các mô hình hiện có của GitHub.

## Thiết lập

### 1. Lấy Mã Token GitHub của bạn

1. Vào [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)  
2. Nhấn "Generate new token"  
3. Đặt tên mô tả (ví dụ: "LangChain4j Demo")  
4. Chọn thời hạn hết hạn (khuyên dùng 7 ngày)  
5. Dưới "Account permissions", tìm "Models" và đặt quyền "Read-only"  
6. Nhấn "Generate token"  
7. Sao chép và lưu lại mã token - bạn sẽ không thấy lại lần nữa

### 2. Đặt Mã Token của bạn

**Tùy chọn 1: Dùng VS Code (Khuyến nghị)**

Nếu bạn dùng VS Code, thêm token vào file `.env` trong thư mục gốc dự án:

Nếu file `.env` chưa tồn tại, bạn sao chép từ `.env.example` sang `.env` hoặc tạo file `.env` mới tại thư mục gốc.

**Ví dụ file `.env`:**  
```bash
# Trong /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```
  
Sau đó bạn chỉ việc click chuột phải vào bất kỳ file demo nào (ví dụ `BasicChatDemo.java`) trong Explorer và chọn **"Run Java"** hoặc dùng cấu hình khởi chạy trong bảng Run and Debug.

**Tùy chọn 2: Dùng Terminal**

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

**Dùng VS Code:** Nhấp chuột phải vào bất kỳ file demo nào trong Explorer và chọn **"Run Java"**, hoặc dùng cấu hình khởi chạy trong Run and Debug (đảm bảo thêm token vào file `.env` trước).

**Dùng Maven:** Bạn cũng có thể chạy trên dòng lệnh:

### 1. Trò chuyện cơ bản

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```
  
### 2. Mẫu Lời nhắc

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```
  
Hiển thị các mẫu zero-shot, few-shot, chain-of-thought, và role-based prompting.

### 3. Gọi Hàm

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
AI tự động gọi các phương thức Java của bạn khi cần.

### 4. Hỏi Đáp Văn Bản (Easy RAG)

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
Hỏi các câu về tài liệu của bạn dùng Easy RAG với nhúng và truy xuất tự động.

### 5. AI Có Trách Nhiệm

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```
  
Xem cách bộ lọc an toàn AI ngăn chặn nội dung có hại.

## Mỗi Ví Dụ Cho Thấy Điều Gì

**Trò chuyện cơ bản** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Bắt đầu từ đây để xem LangChain4j cơ bản nhất. Bạn sẽ tạo `OpenAiOfficialChatModel`, gửi một lời nhắc với `.chat()`, và nhận về phản hồi. Đây là nền tảng: cách khởi tạo mô hình với các điểm cuối tùy chỉnh và khóa API. Khi hiểu được mẫu này, mọi thứ khác sẽ được xây dựng trên đó.

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
> - "Làm sao để thêm phản hồi theo dạng streaming thay vì chờ phản hồi đầy đủ?"

**Kỹ thuật Lời nhắc** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Giờ bạn đã biết cách nói chuyện với mô hình, hãy khám phá bạn nói gì với nó. Demo này dùng cùng một thiết lập mô hình nhưng trình bày năm mẫu lời nhắc khác nhau. Thử zero-shot để chỉ dẫn trực tiếp, few-shot học từ ví dụ, chain-of-thought để hiển thị các bước suy nghĩ, và role-based để thiết lập bối cảnh. Bạn sẽ thấy cùng một mô hình trả về kết quả rất khác dựa trên cách bạn định hình yêu cầu.

Demo còn thể hiện các mẫu lời nhắc (prompt templates), một cách mạnh mẽ để tạo lời nhắc tái sử dụng với biến số. Ví dụ dưới đây cho thấy một lời nhắc dùng `PromptTemplate` của LangChain4j để điền biến. AI sẽ trả lời dựa trên điểm đến và hoạt động cho trước.

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
> - "Sự khác biệt giữa zero-shot và few-shot prompting là gì, và khi nào nên dùng mỗi loại?"  
> - "Tham số nhiệt độ ảnh hưởng như thế nào đến phản hồi của mô hình?"  
> - "Các kỹ thuật nào giúp ngăn tấn công tiêm lời nhắc (prompt injection) trong sản xuất?"  
> - "Làm sao để tạo các đối tượng PromptTemplate có thể tái sử dụng cho các mẫu phổ biến?"

**Tích hợp Công cụ** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Đây là nơi LangChain4j thực sự mạnh mẽ. Bạn sẽ dùng `AiServices` để tạo trợ lý AI có thể gọi các phương thức Java của bạn. Chỉ cần chú thích các phương thức với `@Tool("mô tả")` và LangChain4j sẽ xử lý phần còn lại - AI tự động quyết định khi nào dùng từng công cụ dựa trên yêu cầu người dùng. Đây là kỹ thuật gọi hàm, rất quan trọng để xây dựng AI không chỉ trả lời mà còn có thể thực thi hành động.

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
> - "Chú thích @Tool hoạt động như thế nào và LangChain4j làm gì với nó phía sau hậu trường?"  
> - "AI có thể gọi nhiều công cụ theo thứ tự để giải quyết vấn đề phức tạp không?"  
> - "Nếu một công cụ ném ra lỗi thì sao - tôi nên xử lý lỗi như thế nào?"  
> - "Làm sao để tích hợp API thật thay vì ví dụ về máy tính này?"

**Hỏi Đáp Văn Bản (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Ở đây, bạn sẽ thấy RAG (retrieval-augmented generation) dùng phương pháp "Easy RAG" của LangChain4j. Tài liệu được tải lên, tự động chia nhỏ và nhúng vào bộ nhớ trong, sau đó bộ truy xuất nội dung cung cấp các đoạn liên quan cho AI khi truy vấn. AI trả lời dựa trên tài liệu của bạn, không phải kiến thức tổng quát.

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
> - "Làm sao RAG ngăn chặn ảo giác AI so với việc dùng dữ liệu huấn luyện của mô hình?"  
> - "Phân biệt cách tiếp cận này với pipeline RAG tùy chỉnh là gì?"  
> - "Làm sao để mở rộng cho nhiều tài liệu hoặc các cơ sở tri thức lớn hơn?"

**AI Có Trách Nhiệm** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Xây dựng an toàn AI với phòng thủ nhiều lớp. Demo này cho thấy hai lớp bảo vệ phối hợp:

**Phần 1: LangChain4j Input Guardrails** - Ngăn lời nhắc nguy hiểm trước khi đến LLM. Tạo các guardrail tùy chỉnh kiểm tra từ khóa hoặc mẫu bị cấm. Chạy trong mã của bạn nên nhanh và miễn phí.

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
  
**Phần 2: Bộ lọc An toàn Nhà cung cấp** - GitHub Models có bộ lọc tích hợp giúp phát hiện những gì guardrail có thể bỏ sót. Bạn sẽ thấy các chặn cứng (lỗi HTTP 400) cho vi phạm nghiêm trọng và từ chối nhẹ nhàng khi AI lịch sự từ chối.

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) và hỏi:  
> - "InputGuardrail là gì và làm sao tôi tạo guardrail riêng?"  
> - "Khác nhau giữa chặn cứng và từ chối nhẹ thế nào?"  
> - "Tại sao cần dùng cả guardrail và bộ lọc nhà cung cấp cùng lúc?"

## Bước Tiếp Theo

**Module tiếp:** [01-introduction - Bắt đầu với LangChain4j và gpt-5 trên Azure](../01-introduction/README.md)

---

**Điều hướng:** [← Quay về chính](../README.md) | [Tiếp: Module 01 - Giới thiệu →](../01-introduction/README.md)

---

## Khắc Phục Sự Cố

### Lần Đầu Dùng Maven Build

**Vấn đề:** Lệnh `mvn clean compile` hoặc `mvn package` đầu tiên mất nhiều thời gian (10-15 phút)

**Nguyên nhân:** Maven cần tải về tất cả các phụ thuộc dự án (Spring Boot, thư viện LangChain4j, SDK Azure, v.v.) khi build lần đầu.

**Giải pháp:** Đây là hành vi bình thường. Các lần build tiếp theo sẽ nhanh hơn nhiều vì phụ thuộc được lưu cache cục bộ. Thời gian tải phụ thuộc vào tốc độ mạng của bạn.

### Cú pháp Lệnh Maven trong PowerShell

**Vấn đề:** Lệnh Maven báo lỗi `Unknown lifecycle phase ".mainClass=..."`
**Nguyên nhân**: PowerShell hiểu `=` là toán tử gán biến, làm hỏng cú pháp thuộc tính của Maven

**Giải pháp**: Sử dụng toán tử ngừng phân tích cú pháp `--%` trước lệnh Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Toán tử `--%` chỉ thị PowerShell truyền tất cả các tham số còn lại một cách nguyên văn cho Maven mà không phân tích.

### Hiển thị Emoji trên Windows PowerShell

**Vấn đề**: Phản hồi AI hiển thị các ký tự rác (ví dụ `????` hoặc `â??`) thay vì emoji trong PowerShell

**Nguyên nhân**: Mã hóa mặc định của PowerShell không hỗ trợ emoji UTF-8

**Giải pháp**: Chạy lệnh này trước khi thực thi ứng dụng Java:
```cmd
chcp 65001
```

Điều này bắt buộc mã hóa UTF-8 trong terminal. Ngoài ra, hãy sử dụng Windows Terminal có hỗ trợ Unicode tốt hơn.

### Gỡ lỗi các cuộc gọi API

**Vấn đề**: Lỗi xác thực, giới hạn tần suất, hoặc phản hồi không mong đợi từ mô hình AI

**Giải pháp**: Các ví dụ bao gồm `.logRequests(true)` và `.logResponses(true)` để hiển thị các cuộc gọi API trên console. Điều này giúp khắc phục lỗi xác thực, giới hạn tần suất hoặc phản hồi không mong muốn. Hãy loại bỏ các cờ này trong môi trường sản xuất để giảm nhiễu log.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố từ chối trách nhiệm**:  
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ bản địa của nó nên được xem là nguồn chính thức. Đối với các thông tin quan trọng, khuyên bạn nên sử dụng dịch vụ dịch thuật chuyên nghiệp do con người thực hiện. Chúng tôi không chịu trách nhiệm về bất kỳ sự hiểu nhầm hay giải thích sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->