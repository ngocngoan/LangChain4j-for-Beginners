# Module 00: Bắt đầu Nhanh

## Mục Lục

- [Giới thiệu](../../../00-quick-start)
- [LangChain4j là gì?](../../../00-quick-start)
- [Phụ thuộc của LangChain4j](../../../00-quick-start)
- [Yêu cầu trước](../../../00-quick-start)
- [Cài đặt](../../../00-quick-start)
  - [1. Lấy Token GitHub của bạn](../../../00-quick-start)
  - [2. Đặt Token của bạn](../../../00-quick-start)
- [Chạy các ví dụ](../../../00-quick-start)
  - [1. Trò chuyện cơ bản](../../../00-quick-start)
  - [2. Mẫu nhắc](../../../00-quick-start)
  - [3. Gọi Hàm](../../../00-quick-start)
  - [4. Hỏi đáp Tài liệu (RAG)](../../../00-quick-start)
  - [5. AI có Trách nhiệm](../../../00-quick-start)
- [Mỗi ví dụ trình bày gì](../../../00-quick-start)
- [Bước tiếp theo](../../../00-quick-start)
- [Khắc phục sự cố](../../../00-quick-start)

## Giới thiệu

Bắt đầu nhanh này nhằm giúp bạn khởi động và chạy với LangChain4j nhanh nhất có thể. Nó bao gồm những kiến thức cơ bản tuyệt đối để xây dựng ứng dụng AI với LangChain4j và GitHub Models. Trong các module tiếp theo, bạn sẽ sử dụng Azure OpenAI cùng LangChain4j để xây dựng các ứng dụng nâng cao hơn.

## LangChain4j là gì?

LangChain4j là một thư viện Java giúp đơn giản hóa việc xây dựng các ứng dụng sử dụng AI. Thay vì phải xử lý các HTTP client và phân tích JSON, bạn làm việc với các API Java rõ ràng.

“Từ khóa chain trong LangChain nói về việc nối các thành phần lại với nhau” - bạn có thể nối một prompt tới một model rồi tới một parser, hoặc nối nhiều lời gọi AI để đầu ra của lời gọi này làm đầu vào cho lời gọi tiếp theo. Bắt đầu nhanh này tập trung vào những nền tảng cơ bản trước khi khám phá các chuỗi phức tạp hơn.

<img src="../../../translated_images/vi/langchain-concept.ad1fe6cf063515e1.webp" alt="Khái niệm Chuỗi LangChain4j" width="800"/>

*Kết nối các thành phần trong LangChain4j - các khối xây dựng kết nối tạo ra quy trình làm việc AI mạnh mẽ*

Chúng ta sẽ sử dụng ba thành phần cốt lõi:

**ChatLanguageModel** - Giao diện cho các tương tác với mô hình AI. Gọi `model.chat("prompt")` và nhận về chuỗi phản hồi. Chúng ta dùng `OpenAiOfficialChatModel` làm việc với các endpoint tương thích OpenAI như GitHub Models.

**AiServices** - Tạo ra các giao diện dịch vụ AI có kiểu an toàn. Định nghĩa các phương thức, chú thích chúng với `@Tool`, và LangChain4j sẽ điều phối. AI tự động gọi các phương thức Java của bạn khi cần.

**MessageWindowChatMemory** - Duy trì lịch sử hội thoại. Nếu không có nó, mỗi yêu cầu là độc lập. Có nó, AI nhớ các tin nhắn trước và duy trì ngữ cảnh qua nhiều lượt.

<img src="../../../translated_images/vi/architecture.eedc993a1c576839.webp" alt="Kiến trúc LangChain4j" width="800"/>

*Kiến trúc LangChain4j - các thành phần cốt lõi làm việc cùng để cung cấp sức mạnh cho ứng dụng AI của bạn*

## Phụ thuộc của LangChain4j

Bắt đầu nhanh này sử dụng hai phụ thuộc Maven trong [`pom.xml`](../../../00-quick-start/pom.xml):

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
```

Module `langchain4j-open-ai-official` cung cấp lớp `OpenAiOfficialChatModel` kết nối với các API tương thích OpenAI. GitHub Models dùng cùng định dạng API, nên không cần adapter đặc biệt - chỉ cần trỏ base URL tới `https://models.github.ai/inference`.

## Yêu cầu trước

**Dùng Dev Container?** Java và Maven đã được cài đặt sẵn. Bạn chỉ cần một GitHub Personal Access Token.

**Phát triển cục bộ:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (hướng dẫn dưới đây)

> **Lưu ý:** Module này dùng `gpt-4.1-nano` từ GitHub Models. Không chỉnh sửa tên model trong code - nó đã được cấu hình để làm việc với các model có sẵn của GitHub.

## Cài đặt

### 1. Lấy Token GitHub của bạn

1. Vào [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Nhấn “Generate new token”
3. Đặt tên mô tả (ví dụ: “LangChain4j Demo”)
4. Đặt hạn dùng (khuyên dùng 7 ngày)
5. Dưới “Account permissions”, tìm “Models” và đặt thành “Read-only”
6. Nhấn “Generate token”
7. Sao chép và lưu token của bạn - bạn sẽ không thấy lại nó

### 2. Đặt Token của bạn

**Tùy chọn 1: Dùng VS Code (Khuyến nghị)**

Nếu bạn dùng VS Code, thêm token vào file `.env` trong thư mục gốc dự án:

Nếu file `.env` chưa tồn tại, sao chép `.env.example` thành `.env` hoặc tạo mới file `.env` trong thư mục gốc.

**Ví dụ file `.env`:**
```bash
# Trong /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Sau đó bạn có thể nhấn chuột phải trên bất kỳ file demo nào (ví dụ, `BasicChatDemo.java`) trong Explorer và chọn **"Run Java"** hoặc dùng cấu hình chạy trong panel Run and Debug.

**Tùy chọn 2: Dùng Terminal**

Đặt token thành biến môi trường:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Chạy các ví dụ

**Dùng VS Code:** Nhấn chuột phải vào bất kỳ file demo nào trong Explorer và chọn **"Run Java"** hoặc dùng cấu hình chạy trong panel Run and Debug (đảm bảo bạn đã thêm token vào file `.env` trước).

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

Hiển thị các kiểu prompt zero-shot, few-shot, chuỗi suy nghĩ và theo vai trò.

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

### 4. Hỏi đáp Tài liệu (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Hỏi về nội dung trong `document.txt`.

### 5. AI có Trách nhiệm

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Xem bộ lọc an toàn AI chặn nội dung gây hại như thế nào.

## Mỗi ví dụ trình bày gì

**Trò chuyện cơ bản** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Bắt đầu ở đây để thấy LangChain4j đơn giản nhất. Bạn tạo một `OpenAiOfficialChatModel`, gửi prompt với `.chat()`, và nhận về phản hồi. Điều này trình bày nền tảng: cách khởi tạo model với endpoint và API key tùy chỉnh. Khi hiểu mẫu này, mọi thứ khác sẽ dựa trên đó.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) và hỏi:
> - "Làm thế nào để chuyển từ GitHub Models sang Azure OpenAI trong code này?"
> - "Có những tham số nào khác tôi có thể cấu hình trong OpenAiOfficialChatModel.builder()?"
> - "Làm cách nào để thêm phản hồi luồng thay vì chờ phản hồi hoàn chỉnh?"

**Kỹ thuật Prompt** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Giờ bạn đã biết cách trò chuyện với model, hãy khám phá bạn nói gì với nó. Demo này dùng cùng thiết lập model nhưng trình bày năm mẫu prompt khác nhau. Thử zero-shot cho hướng dẫn trực tiếp, few-shot học từ ví dụ, chuỗi suy nghĩ đưa ra các bước lý luận, và prompt theo vai trò để đặt bối cảnh. Bạn sẽ thấy cùng một model tạo ra kết quả rất khác nhau tùy cách bạn thiết lập yêu cầu.

Demo cũng minh họa các mẫu template prompt, cách mạnh mẽ để tạo prompt tái sử dụng với biến.
Ví dụ dưới đây là prompt dùng `PromptTemplate` của LangChain4j để điền biến. AI trả lời dựa trên điểm đến và hoạt động được cung cấp.

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
> - "Sự khác biệt giữa zero-shot và few-shot prompting là gì, và khi nào nên dùng cái nào?"
> - "Tham số nhiệt độ ảnh hưởng như thế nào đến phản hồi của model?"
> - "Có những kỹ thuật nào giúp ngăn chặn tấn công chèn prompt trong sản xuất?"
> - "Làm sao để tạo đối tượng PromptTemplate tái sử dụng cho các mẫu phổ biến?"

**Tích hợp Công cụ** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Đây là lúc LangChain4j trở nên mạnh mẽ. Bạn sử dụng `AiServices` để tạo một trợ lý AI có thể gọi các phương thức Java của bạn. Chỉ cần chú thích phương thức bằng `@Tool("mô tả")` và LangChain4j lo hết - AI tự quyết định khi nào dùng công cụ dựa trên yêu cầu người dùng. Đây là minh họa gọi hàm, kỹ thuật quan trọng để xây dựng AI có thể thực hiện hành động, chứ không chỉ trả lời câu hỏi.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) và hỏi:
> - "@Tool annotation hoạt động như thế nào và LangChain4j làm gì phía sau?"
> - "AI có thể gọi nhiều công cụ theo trình tự để giải quyết vấn đề phức tạp không?"
> - "Nếu một công cụ ném ra ngoại lệ thì sao - tôi nên xử lý lỗi thế nào?"
> - "Làm sao để tích hợp một API thật thay vì ví dụ máy tính này?"

**Hỏi đáp Tài liệu (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Ở đây bạn sẽ thấy nền tảng của RAG (generation tăng cường truy xuất). Thay vì dựa vào dữ liệu huấn luyện của model, bạn tải nội dung từ [`document.txt`](../../../00-quick-start/document.txt) và đưa vào prompt. AI trả lời dựa trên tài liệu của bạn, không phải kiến thức chung. Đây là bước đầu xây dựng hệ thống làm việc với dữ liệu riêng của bạn.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Lưu ý:** Cách đơn giản này nạp toàn bộ tài liệu vào prompt. Với file lớn (>10KB), bạn sẽ vượt quá giới hạn ngữ cảnh. Module 03 sẽ trình bày phân đoạn và tìm kiếm vector cho hệ thống RAG trong sản xuất.

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) và hỏi:
> - "RAG phòng tránh việc AI tưởng tượng ra thông tin sai so với dùng dữ liệu huấn luyện của model như thế nào?"
> - "Khác biệt giữa cách đơn giản này và dùng vector embedding để truy xuất là gì?"
> - "Làm sao để mở rộng xử lý nhiều tài liệu hoặc cơ sở tri thức lớn hơn?"
> - "Thực hành tốt nhất để cấu trúc prompt đảm bảo AI chỉ dùng ngữ cảnh được cung cấp là gì?"

**AI có Trách nhiệm** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Xây dựng tính an toàn AI với phòng thủ đa lớp. Demo này trình bày hai lớp bảo vệ phối hợp:

**Phần 1: LangChain4j Input Guardrails** - Chặn các prompt nguy hiểm trước khi gửi đến LLM. Tạo các guardrail tùy chỉnh kiểm tra từ khóa hay mẫu bị cấm. Chúng chạy trong code của bạn, nhanh và miễn phí.

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

**Phần 2: Bộ Lọc An toàn của Nhà cung cấp** - GitHub Models có bộ lọc sẵn bắt những gì guardrail có thể bỏ sót. Bạn sẽ thấy chặn cứng (lỗi HTTP 400) với vi phạm nghiêm trọng và từ chối mềm khi AI lịch sự từ chối.

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) và hỏi:
> - "InputGuardrail là gì và làm sao tạo guardrail riêng?"
> - "Khác biệt giữa chặn cứng và từ chối mềm là gì?"
> - "Tại sao nên dùng cả guardrails và bộ lọc của nhà cung cấp cùng nhau?"

## Bước tiếp theo

**Module tiếp theo:** [01-introduction - Bắt đầu với LangChain4j và gpt-5 trên Azure](../01-introduction/README.md)

---

**Điều hướng:** [← Quay lại Chính](../README.md) | [Tiếp theo: Module 01 - Giới thiệu →](../01-introduction/README.md)

---

## Khắc phục sự cố

### Xây dựng Maven lần đầu

**Vấn đề**: Lệnh `mvn clean compile` hoặc `mvn package` ban đầu mất rất lâu (10-15 phút)

**Nguyên nhân**: Maven cần tải tất cả phụ thuộc dự án (Spring Boot, thư viện LangChain4j, Azure SDK, v.v.) trong lần build đầu tiên.

**Giải pháp**: Đây là hành vi bình thường. Các lần build sau sẽ nhanh hơn nhiều do phụ thuộc đã được lưu trong bộ nhớ cache cục bộ. Thời gian tải còn tùy tốc độ mạng của bạn.
### Cú pháp lệnh Maven trong PowerShell

**Vấn đề**: Các lệnh Maven thất bại với lỗi `Unknown lifecycle phase ".mainClass=..."`

**Nguyên nhân**: PowerShell hiểu `=` là toán tử gán biến, làm hỏng cú pháp thuộc tính Maven

**Giải pháp**: Sử dụng toán tử dừng phân tích cú pháp `--%` trước lệnh Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Toán tử `--%` báo cho PowerShell truyền tất cả các đối số còn lại một cách nguyên văn cho Maven mà không phân tích.

### Hiển thị Biểu tượng cảm xúc trong Windows PowerShell

**Vấn đề**: Các phản hồi AI hiển thị ký tự rác (ví dụ: `????` hoặc `â??`) thay vì biểu tượng cảm xúc trong PowerShell

**Nguyên nhân**: Mã hóa mặc định của PowerShell không hỗ trợ biểu tượng cảm xúc UTF-8

**Giải pháp**: Chạy lệnh này trước khi thực thi các ứng dụng Java:
```cmd
chcp 65001
```

Điều này bắt buộc mã hóa UTF-8 trong terminal. Ngoài ra, bạn có thể sử dụng Windows Terminal, hỗ trợ Unicode tốt hơn.

### Gỡ lỗi các cuộc gọi API

**Vấn đề**: Lỗi xác thực, giới hạn tần suất hoặc phản hồi không mong muốn từ mô hình AI

**Giải pháp**: Các ví dụ bao gồm `.logRequests(true)` và `.logResponses(true)` để hiển thị các cuộc gọi API trên bảng điều khiển. Điều này giúp xử lý lỗi xác thực, giới hạn tần suất hoặc phản hồi không mong muốn. Hãy loại bỏ các tùy chọn này trong môi trường sản xuất để giảm bớt tiếng ồn của nhật ký.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố miễn trừ trách nhiệm**:
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng bản dịch tự động có thể chứa lỗi hoặc thiếu sót. Văn bản gốc bằng ngôn ngữ gốc của nó nên được coi là nguồn tham khảo chính xác nhất. Đối với thông tin quan trọng, khuyến nghị sử dụng dịch vụ dịch thuật chuyên nghiệp do con người thực hiện. Chúng tôi không chịu trách nhiệm về bất kỳ sự hiểu lầm hoặc giải thích sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->