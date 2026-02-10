# Module 02: Kỹ Thuật Tạo Prompt với GPT-5.2

## Mục Lục

- [Bạn Sẽ Học Gì](../../../02-prompt-engineering)
- [Yêu Cầu Tiền Đề](../../../02-prompt-engineering)
- [Hiểu về Kỹ Thuật Tạo Prompt](../../../02-prompt-engineering)
- [Cách Thức Sử Dụng LangChain4j](../../../02-prompt-engineering)
- [Các Mẫu Cơ Bản](../../../02-prompt-engineering)
- [Sử Dụng Tài Nguyên Azure Có Sẵn](../../../02-prompt-engineering)
- [Ảnh Chụp Ứng Dụng](../../../02-prompt-engineering)
- [Khám Phá Các Mẫu](../../../02-prompt-engineering)
  - [Tham Vọng Thấp vs Cao](../../../02-prompt-engineering)
  - [Thực Thi Nhiệm Vụ (Tiền Tố Công Cụ)](../../../02-prompt-engineering)
  - [Mã Tự Phản Chiếu](../../../02-prompt-engineering)
  - [Phân Tích Có Cấu Trúc](../../../02-prompt-engineering)
  - [Chat Nhiều Lượt](../../../02-prompt-engineering)
  - [Lý Luận Từng Bước Một](../../../02-prompt-engineering)
  - [Đầu Ra Bị Hạn Chế](../../../02-prompt-engineering)
- [Bạn Thực Sự Học Gì](../../../02-prompt-engineering)
- [Bước Tiếp Theo](../../../02-prompt-engineering)

## Bạn Sẽ Học Gì

Trong module trước, bạn đã thấy bộ nhớ giúp AI đàm thoại thế nào và sử dụng các Mô hình GitHub cho các tương tác cơ bản. Giờ chúng ta sẽ tập trung vào cách bạn đặt câu hỏi – chính là các prompt – sử dụng GPT-5.2 của Azure OpenAI. Cách bạn cấu trúc prompt ảnh hưởng đáng kể đến chất lượng câu trả lời mà bạn nhận được.

Chúng ta dùng GPT-5.2 vì nó giới thiệu tính năng điều khiển lý luận – bạn có thể cho mô hình biết mức độ suy nghĩ trước khi trả lời. Điều này làm cho các chiến lược tạo prompt khác nhau rõ ràng hơn và giúp bạn hiểu khi nào nên dùng cách nào. Chúng ta cũng được lợi từ việc Azure hạn chế tần suất thấp hơn so với các Mô hình GitHub cho GPT-5.2.

## Yêu Cầu Tiền Đề

- Hoàn thành Module 01 (đã triển khai tài nguyên Azure OpenAI)
- Tệp `.env` trong thư mục gốc chứa thông tin xác thực Azure (được tạo bằng `azd up` trong Module 01)

> **Lưu ý:** Nếu bạn chưa hoàn thành Module 01, hãy làm theo hướng dẫn triển khai ở đó trước.

## Hiểu về Kỹ Thuật Tạo Prompt

Kỹ thuật tạo prompt là về thiết kế văn bản đầu vào sao cho liên tục mang lại kết quả bạn cần. Nó không chỉ là đặt câu hỏi mà là cấu trúc yêu cầu để mô hình hiểu chính xác bạn muốn gì và cách thức cung cấp.

Hãy nghĩ nó giống như đưa hướng dẫn cho đồng nghiệp. "Sửa lỗi" là quá chung chung. "Sửa lỗi con trỏ null trong UserService.java dòng 45 bằng cách thêm kiểm tra null" cụ thể hơn. Các mô hình ngôn ngữ cũng vậy – sự cụ thể và cấu trúc rất quan trọng.

## Cách Thức Sử Dụng LangChain4j

Module này trình bày các mẫu tạo prompt nâng cao sử dụng cùng nền tảng LangChain4j từ các module trước, với trọng tâm là cấu trúc prompt và điều khiển lý luận.

<img src="../../../translated_images/vi/langchain4j-flow.48e534666213010b.webp" alt="Luồng LangChain4j" width="800"/>

*Cách LangChain4j kết nối các prompt của bạn với Azure OpenAI GPT-5.2*

**Phụ Thuộc** – Module 02 dùng các phụ thuộc langchain4j sau được định nghĩa trong `pom.xml`:
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**Cấu Hình OpenAiOfficialChatModel** – [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Mô hình chat được cấu hình thủ công như một bean Spring sử dụng client OpenAI Official, có hỗ trợ endpoint Azure OpenAI. Điểm khác biệt so với Module 01 là cách chúng ta cấu trúc prompt gửi vào `chatModel.chat()`, không phải cách thiết lập mô hình.

**Tin Nhắn Hệ Thống và Người Dùng** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j tách riêng các loại tin nhắn để minh bạch. `SystemMessage` đặt hành vi và ngữ cảnh AI (ví dụ: "Bạn là người đánh giá code"), còn `UserMessage` chứa yêu cầu thực tế. Việc tách biệt giúp duy trì hành vi AI nhất quán trên các truy vấn khác nhau.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/vi/message-types.93e0779798a17c9d.webp" alt="Kiến Trúc Các Loại Tin Nhắn" width="800"/>

*SystemMessage cung cấp ngữ cảnh lâu dài trong khi UserMessages chứa các yêu cầu riêng lẻ*

**MessageWindowChatMemory cho Đàm Thoại Nhiều Lượt** – Với mẫu đàm thoại nhiều lượt, chúng ta tái sử dụng `MessageWindowChatMemory` từ Module 01. Mỗi phiên làm việc có một thể hiện bộ nhớ riêng lưu trong `Map<String, ChatMemory>`, cho phép nhiều cuộc hội thoại đồng thời mà không bị lẫn ngữ cảnh.

**Mẫu Prompt** – Trọng tâm thực sự ở đây là kỹ thuật tạo prompt, không phải API mới của LangChain4j. Mỗi mẫu (tham vọng thấp, cao, thực thi nhiệm vụ, v.v.) dùng cùng phương pháp `chatModel.chat(prompt)` nhưng với chuỗi prompt được cấu trúc kỹ lưỡng. Các thẻ XML, hướng dẫn, và định dạng đều nằm trong văn bản prompt, không phải tính năng của LangChain4j.

**Điều Khiển Lý Luận** – Sự nỗ lực lý luận của GPT-5.2 được điều khiển qua các chỉ dẫn prompt như "tối đa 2 bước lý luận" hoặc "khám phá kỹ lưỡng". Đây là kỹ thuật tạo prompt, không phải cấu hình LangChain4j. Thư viện chỉ đơn giản chuyển prompt của bạn tới mô hình.

Điều then chốt: LangChain4j cung cấp hạ tầng (kết nối mô hình qua [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), bộ nhớ, xử lý tin nhắn qua [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), trong khi module này dạy bạn cách tạo prompt hiệu quả trong hạ tầng đó.

## Các Mẫu Cơ Bản

Không phải vấn đề nào cũng cần cùng một cách tiếp cận. Có câu hỏi cần trả lời nhanh, có câu cần suy nghĩ sâu. Có câu cần hiển thị lý luận, có câu chỉ cần kết quả. Module này bao gồm tám mẫu tạo prompt – mỗi mẫu tối ưu cho các hoàn cảnh khác nhau. Bạn sẽ thử nghiệm tất cả để học khi nào cách nào hiệu quả.

<img src="../../../translated_images/vi/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Tám Mẫu Kỹ Thuật Tạo Prompt" width="800"/>

*Tổng quan về tám mẫu kỹ thuật tạo prompt và các trường hợp sử dụng*

<img src="../../../translated_images/vi/reasoning-effort.db4a3ba5b8e392c1.webp" alt="So Sánh Nỗ Lực Lý Luận" width="800"/>

*Tham vọng thấp (nhanh, trực tiếp) vs Tham vọng cao (kỹ lưỡng, khám phá) trong các cách lý luận*

**Tham Vọng Thấp (Nhanh & Tập Trung)** – Dùng cho câu hỏi đơn giản muốn câu trả lời nhanh và trực tiếp. Mô hình thực hiện ít bước lý luận – tối đa 2 bước. Dùng cho tính toán, tra cứu, hoặc câu hỏi đơn giản.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Khám phá với GitHub Copilot:** Mở [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) và hỏi:
> - "Sự khác biệt giữa mẫu tham vọng thấp và tham vọng cao là gì?"
> - "Các thẻ XML trong prompt giúp cấu trúc phản hồi AI ra sao?"
> - "Khi nào tôi nên dùng mẫu tự phản chiếu so với hướng dẫn trực tiếp?"

**Tham Vọng Cao (Sâu Sắc & Kỹ Lưỡng)** – Cho các vấn đề phức tạp cần phân tích toàn diện. Mô hình khám phá kỹ và hiển thị lý luận chi tiết. Dùng cho thiết kế hệ thống, quyết định kiến trúc, hoặc nghiên cứu phức tạp.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Thực Thi Nhiệm Vụ (Tiến Độ Từng Bước)** – Dùng cho quy trình nhiều bước. Mô hình cung cấp kế hoạch trước, kể chi tiết từng bước khi thực hiện, rồi tổng kết. Dùng cho di cư, triển khai, hoặc mọi quy trình phức tạp.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Chuỗi suy nghĩ (Chain-of-Thought) yêu cầu mô hình hiển thị quá trình lý luận, nâng cao độ chính xác trong các nhiệm vụ phức tạp. Phân chia từng bước giúp người và AI hiểu logic hơn.

> **🤖 Thử với chat [GitHub Copilot](https://github.com/features/copilot):** Hỏi về mẫu này:
> - "Làm sao điều chỉnh mẫu thực thi nhiệm vụ cho các tác vụ chạy lâu?"
> - "Các phương pháp hay nhất để cấu trúc tiền tố công cụ trong ứng dụng sản xuất là gì?"
> - "Làm sao ghi lại và hiển thị tiến độ trung gian trong giao diện người dùng?"

<img src="../../../translated_images/vi/task-execution-pattern.9da3967750ab5c1e.webp" alt="Mẫu Thực Thi Nhiệm Vụ" width="800"/>

*Kế hoạch → Thực thi → Tổng kết cho các nhiệm vụ nhiều bước*

**Mã Tự Phản Chiếu** – Dùng để tạo mã chất lượng sản xuất. Mô hình sinh mã, kiểm tra theo tiêu chí chất lượng, và cải tiến theo vòng lặp. Dùng khi xây dựng tính năng hoặc dịch vụ mới.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/vi/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Chu Kỳ Tự Phản Chiếu" width="800"/>

*Vòng lặp cải tiến tuần tự – tạo, đánh giá, xác định vấn đề, cải thiện, lặp lại*

**Phân Tích Có Cấu Trúc** – Dùng để đánh giá nhất quán. Mô hình xem xét mã theo khuôn khổ cố định (độ đúng, thực hành, hiệu suất, bảo mật). Dùng cho đánh giá mã hoặc kiểm định chất lượng.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Thử với chat [GitHub Copilot](https://github.com/features/copilot):** Hỏi về phân tích có cấu trúc:
> - "Làm sao tùy chỉnh khuôn khổ phân tích cho các loại đánh giá mã khác nhau?"
> - "Phương pháp tốt nhất để phân tích và xử lý đầu ra có cấu trúc programmatic là gì?"
> - "Làm sao đảm bảo mức độ nghiêm trọng nhất quán giữa các phiên đánh giá khác nhau?"

<img src="../../../translated_images/vi/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Mẫu Phân Tích Có Cấu Trúc" width="800"/>

*Khuôn khổ bốn mục để đánh giá mã nhất quán với các mức độ nghiêm trọng*

**Chat Nhiều Lượt** – Dùng cho hội thoại cần ngữ cảnh. Mô hình nhớ các tin nhắn trước và xây dựng dựa trên đó. Dùng cho phiên trợ giúp tương tác hoặc hỏi đáp phức tạp.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/vi/context-memory.dff30ad9fa78832a.webp" alt="Bộ Nhớ Ngữ Cảnh" width="800"/>

*Cách ngữ cảnh hội thoại tích lũy qua nhiều lượt cho đến khi đạt giới hạn token*

**Lý Luận Từng Bước Một** – Cho các vấn đề cần logic rõ ràng. Mô hình hiển thị lý luận rõ ràng từng bước. Dùng cho bài toán toán học, câu đố logic, hoặc khi bạn cần hiểu quy trình suy nghĩ.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/vi/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Mẫu Từng Bước Một" width="800"/>

*Phân tách vấn đề thành các bước logic rõ ràng*

**Đầu Ra Bị Hạn Chế** – Cho câu trả lời cần theo định dạng cụ thể. Mô hình tuân thủ nghiêm ngặt các quy tắc về định dạng và độ dài. Dùng cho tóm tắt hoặc khi cần cấu trúc đầu ra chính xác.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/vi/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Mẫu Đầu Ra Bị Hạn Chế" width="800"/>

*Áp dụng quy định về định dạng, độ dài và cấu trúc cụ thể*

## Sử Dụng Tài Nguyên Azure Có Sẵn

**Xác minh triển khai:**

Đảm bảo tệp `.env` tồn tại trong thư mục gốc với thông tin xác thực Azure (đã tạo trong Module 01):
```bash
cat ../.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Khởi động ứng dụng:**

> **Lưu ý:** Nếu bạn đã khởi động tất cả ứng dụng bằng `./start-all.sh` từ Module 01, module này đã chạy ở cổng 8083. Bạn có thể bỏ qua các lệnh khởi động dưới đây và truy cập trực tiếp http://localhost:8083.

**Tùy chọn 1: Sử dụng Spring Boot Dashboard (Khuyên dùng cho người dùng VS Code)**

Dev container bao gồm tiện ích mở rộng Spring Boot Dashboard, cung cấp giao diện trực quan quản lý tất cả ứng dụng Spring Boot. Bạn có thể tìm thấy nó ở Thanh Hoạt Động bên trái VS Code (tìm biểu tượng Spring Boot).

Từ Spring Boot Dashboard, bạn có thể:
- Xem tất cả các ứng dụng Spring Boot trong workspace
- Khởi động/dừng ứng dụng chỉ với một cú nhấp
- Xem nhật ký ứng dụng theo thời gian thực
- Giám sát trạng thái ứng dụng

Chỉ cần nhấn nút chạy bên cạnh "prompt-engineering" để khởi động module này hoặc chạy tất cả các module cùng lúc.

<img src="../../../translated_images/vi/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Tùy chọn 2: Sử dụng tập lệnh shell**

Khởi động các ứng dụng web (các module 01-04):

**Bash:**
```bash
cd ..  # Từ thư mục gốc
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Từ thư mục gốc
.\start-all.ps1
```

Hoặc chỉ khởi động module này:

**Bash:**
```bash
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

Cả hai script tự động nạp biến môi trường từ tệp `.env` trong thư mục gốc và sẽ xây dựng file JAR nếu chưa có.

> **Lưu ý:** Nếu bạn muốn tự xây dựng trước khi khởi động:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Mở http://localhost:8083 trong trình duyệt.

**Để dừng:**

**Bash:**
```bash
./stop.sh  # Chỉ mô-đun này
# Hoặc
cd .. && ./stop-all.sh  # Tất cả các mô-đun
```

**PowerShell:**
```powershell
.\stop.ps1  # Chỉ mô-đun này
# Hoặc
cd ..; .\stop-all.ps1  # Tất cả các mô-đun
```

## Ảnh Chụp Ứng Dụng

<img src="../../../translated_images/vi/dashboard-home.5444dbda4bc1f79d.webp" alt="Trang Chủ Dashboard" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Bảng điều khiển chính hiển thị tám mẫu kỹ thuật tạo prompt với đặc điểm và trường hợp sử dụng*

## Khám Phá Các Mẫu

Giao diện web cho phép bạn thử các chiến lược tạo prompt khác nhau. Mỗi mẫu giải quyết các vấn đề khác nhau – hãy thử để biết khi nào cách nào phát huy hiệu quả.

### Tham Vọng Thấp vs Cao

Hãy hỏi một câu đơn giản như "15% của 200 là bao nhiêu?" bằng cách dùng Tham Vọng Thấp. Bạn sẽ nhận được câu trả lời nhanh và trực tiếp. Bây giờ thử hỏi câu phức tạp như "Thiết kế chiến lược bộ nhớ đệm cho một API có lưu lượng cao" dùng Tham Vọng Cao. Quan sát cách mô hình chậm lại và đưa ra lý luận chi tiết. Cùng một mô hình, cùng một cấu trúc câu hỏi – nhưng prompt báo cho nó biết mức độ suy nghĩ cần làm.
<img src="../../../translated_images/vi/low-eagerness-demo.898894591fb23aa0.webp" alt="Demo Ít Nhiệt Tình" width="800"/>

*Tính toán nhanh với lý luận tối thiểu*

<img src="../../../translated_images/vi/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Demo Nhiệt Tình Cao" width="800"/>

*Chiến lược lưu trữ toàn diện (2.8MB)*

### Thực Thi Nhiệm Vụ (Mở đầu Công Cụ)

Các quy trình làm việc nhiều bước được hưởng lợi từ việc lập kế hoạch trước và thuật lại tiến trình. Mô hình phác thảo những gì nó sẽ làm, thuật lại từng bước, sau đó tóm tắt kết quả.

<img src="../../../translated_images/vi/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Demo Thực Thi Nhiệm Vụ" width="800"/>

*Tạo một REST endpoint với thuật lại từng bước (3.9MB)*

### Mã Tự Phản Chiếu

Thử "Tạo dịch vụ kiểm tra email". Thay vì chỉ tạo mã và dừng lại, mô hình tạo ra, đánh giá dựa trên tiêu chí chất lượng, xác định điểm yếu và cải tiến. Bạn sẽ thấy nó lặp đi lặp lại cho đến khi mã đạt chuẩn sản xuất.

<img src="../../../translated_images/vi/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Demo Mã Tự Phản Chiếu" width="800"/>

*Dịch vụ kiểm tra email hoàn chỉnh (5.2MB)*

### Phân Tích Có Cấu Trúc

Đánh giá mã cần các khung đánh giá nhất quán. Mô hình phân tích mã theo các danh mục cố định (độ chính xác, thực hành, hiệu suất, bảo mật) với các mức độ nghiêm trọng.

<img src="../../../translated_images/vi/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Demo Phân Tích Có Cấu Trúc" width="800"/>

*Đánh giá mã theo khuôn khổ*

### Trò Chuyện Nhiều Vòng

Hỏi "Spring Boot là gì?" rồi ngay lập tức hỏi tiếp "Cho tôi một ví dụ". Mô hình nhớ câu hỏi đầu tiên và cung cấp ví dụ Spring Boot cụ thể cho bạn. Nếu không có bộ nhớ, câu hỏi thứ hai sẽ quá mơ hồ.

<img src="../../../translated_images/vi/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Demo Trò Chuyện Nhiều Vòng" width="800"/>

*Bảo toàn ngữ cảnh qua các câu hỏi*

### Lý Luận Từng Bước

Chọn một bài toán học toán và thử cả Lý Luận Từng Bước và Ít Nhiệt Tình. Ít nhiệt tình chỉ cho bạn câu trả lời - nhanh nhưng mơ hồ. Từng bước cho bạn thấy mọi phép tính và quyết định.

<img src="../../../translated_images/vi/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Demo Lý Luận Từng Bước" width="800"/>

*Bài toán toán học với các bước rõ ràng*

### Đầu Ra Có Hạn Chế

Khi bạn cần định dạng hoặc số từ cụ thể, mẫu này bắt buộc tuân thủ chặt chẽ. Thử tạo một bản tóm tắt với chính xác 100 từ dạng gạch đầu dòng.

<img src="../../../translated_images/vi/constrained-output-demo.567cc45b75da1633.webp" alt="Demo Đầu Ra Có Hạn Chế" width="800"/>

*Tóm tắt học máy với kiểm soát định dạng*

## Những Gì Bạn Thực Sự Đang Học

**Nỗ Lực Lý Luận Thay Đổi Mọi Thứ**

GPT-5.2 cho phép bạn kiểm soát nỗ lực tính toán qua các gợi ý. Nỗ lực thấp có nghĩa phản hồi nhanh với khám phá tối thiểu. Nỗ lực cao nghĩa là mô hình dành thời gian suy nghĩ sâu sắc. Bạn học cách điều chỉnh nỗ lực với độ phức tạp nhiệm vụ - không phí thời gian cho câu hỏi đơn giản, nhưng cũng không vội vàng khi quyết định phức tạp.

**Cấu Trúc Dẫn Dắt Hành Vi**

Bạn có để ý các thẻ XML trong gợi ý? Chúng không phải để trang trí. Mô hình tuân theo hướng dẫn có cấu trúc đáng tin cậy hơn so với văn bản tự do. Khi bạn cần quy trình đa bước hay logic phức tạp, cấu trúc giúp mô hình theo dõi vị trí và bước tiếp theo.

<img src="../../../translated_images/vi/prompt-structure.a77763d63f4e2f89.webp" alt="Cấu Trúc Gợi Ý" width="800"/>

*Cấu trúc của một gợi ý được tổ chức tốt với các phần rõ ràng và bố cục kiểu XML*

**Chất Lượng Qua Tự Đánh Giá**

Mẫu tự phản chiếu hoạt động bằng cách làm rõ các tiêu chí chất lượng. Thay vì hy vọng mô hình "làm đúng", bạn nói rõ “đúng” là gì: logic chính xác, xử lý lỗi, hiệu suất, bảo mật. Mô hình sau đó có thể tự đánh giá đầu ra và cải thiện. Điều này biến việc tạo mã từ xổ số thành quy trình.

**Ngữ Cảnh Có Giới Hạn**

Hội thoại nhiều lượt làm việc bằng cách bao gồm lịch sử tin nhắn với mỗi yêu cầu. Nhưng có giới hạn - mỗi mô hình có số lượng token tối đa. Khi hội thoại kéo dài, bạn cần chiến lược giữ ngữ cảnh liên quan mà không vượt trần. Module này cho bạn thấy cách hoạt động bộ nhớ; sau này bạn sẽ học khi nào nên tóm tắt, khi nào nên quên, và khi nào nên truy xuất.

## Bước Tiếp Theo

**Module Tiếp Theo:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Điều Hướng:** [← Trước: Module 01 - Giới Thiệu](../01-introduction/README.md) | [Quay Về Chính](../README.md) | [Tiếp Theo: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố miễn trừ trách nhiệm**:  
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi nỗ lực để đảm bảo độ chính xác, xin lưu ý rằng bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ ban đầu nên được coi là nguồn tham khảo chính xác nhất. Đối với các thông tin quan trọng, nên sử dụng dịch vụ dịch thuật chuyên nghiệp do con người thực hiện. Chúng tôi không chịu trách nhiệm về bất kỳ sự hiểu lầm hay diễn giải sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->