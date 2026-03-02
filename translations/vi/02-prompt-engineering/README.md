# Module 02: Kỹ thuật Prompt với GPT-5.2

## Mục lục

- [Video Hướng Dẫn](../../../02-prompt-engineering)
- [Những gì bạn sẽ học](../../../02-prompt-engineering)
- [Yêu cầu trước](../../../02-prompt-engineering)
- [Hiểu về Kỹ thuật Prompt](../../../02-prompt-engineering)
- [Các nguyên tắc cơ bản của Kỹ thuật Prompt](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Các Mẫu nâng cao](../../../02-prompt-engineering)
- [Sử dụng tài nguyên Azure hiện có](../../../02-prompt-engineering)
- [Ảnh chụp ứng dụng](../../../02-prompt-engineering)
- [Khám phá các Mẫu](../../../02-prompt-engineering)
  - [Nhiệt huyết thấp vs Cao](../../../02-prompt-engineering)
  - [Thực thi tác vụ (Tool Preambles)](../../../02-prompt-engineering)
  - [Mã tự phản chiếu](../../../02-prompt-engineering)
  - [Phân tích có cấu trúc](../../../02-prompt-engineering)
  - [Chat đa lượt](../../../02-prompt-engineering)
  - [Lý luận từng bước](../../../02-prompt-engineering)
  - [Đầu ra giới hạn](../../../02-prompt-engineering)
- [Bạn thực sự đang học gì](../../../02-prompt-engineering)
- [Bước tiếp theo](../../../02-prompt-engineering)

## Video Hướng Dẫn

Xem buổi trực tiếp này giải thích cách bắt đầu với module này:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Những gì bạn sẽ học

<img src="../../../translated_images/vi/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

Trong module trước, bạn đã thấy cách bộ nhớ hỗ trợ AI đàm thoại và sử dụng GitHub Models cho các tương tác cơ bản. Giờ đây, chúng ta sẽ tập trung vào cách bạn đặt câu hỏi — chính là các prompt — sử dụng GPT-5.2 của Azure OpenAI. Cách bạn cấu trúc prompt ảnh hưởng rất lớn đến chất lượng câu trả lời nhận được. Chúng ta bắt đầu với việc ôn lại các kỹ thuật prompt căn bản, sau đó chuyển sang tám mẫu nâng cao tận dụng tối đa khả năng của GPT-5.2.

Chúng ta sẽ sử dụng GPT-5.2 vì nó giới thiệu tính năng kiểm soát suy luận - bạn có thể yêu cầu mô hình suy nghĩ bao nhiêu trước khi trả lời. Điều này giúp các chiến lược prompt khác nhau trở nên rõ ràng hơn và giúp bạn hiểu khi nào nên dùng từng cách. Ngoài ra, Azure cũng có giới hạn rate thấp hơn cho GPT-5.2 so với GitHub Models.

## Yêu cầu trước

- Hoàn thành Module 01 (đã triển khai tài nguyên Azure OpenAI)
- File `.env` trong thư mục gốc có thông tin xác thực Azure (được tạo bởi `azd up` trong Module 01)

> **Lưu ý:** Nếu bạn chưa hoàn thành Module 01, hãy làm theo hướng dẫn triển khai ở đó trước.

## Hiểu về Kỹ thuật Prompt

<img src="../../../translated_images/vi/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Kỹ thuật prompt là thiết kế đầu vào văn bản sao cho luôn nhận được kết quả bạn cần. Nó không chỉ là đặt câu hỏi — mà còn là cách cấu trúc yêu cầu để mô hình hiểu chính xác bạn muốn gì và cách cung cấp.

Hãy nghĩ như đang chỉ dẫn đồng nghiệp. "Sửa cái lỗi" thì mơ hồ. "Sửa lỗi null pointer exception trong UserService.java dòng 45 bằng cách thêm kiểm tra null" thì cụ thể. Mô hình ngôn ngữ cũng tương tự — độ cụ thể và cấu trúc rất quan trọng.

<img src="../../../translated_images/vi/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j cung cấp hạ tầng — kết nối mô hình, bộ nhớ và loại tin nhắn — trong khi các mẫu prompt chỉ là văn bản được cấu trúc cẩn thận gửi qua hạ tầng đó. Các thành phần chính là `SystemMessage` (đặt hành vi và vai trò AI) và `UserMessage` (chứa yêu cầu thực tế của bạn).

## Các nguyên tắc cơ bản của Kỹ thuật Prompt

<img src="../../../translated_images/vi/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Trước khi đi sâu vào các mẫu nâng cao trong module này, hãy ôn lại năm kỹ thuật prompting căn bản. Đây là các nền tảng mà mọi kỹ sư prompt nên biết. Nếu bạn đã làm qua [module Khởi đầu nhanh](../00-quick-start/README.md#2-prompt-patterns), bạn đã thấy chúng hoạt động — đây là khung khái niệm phía sau.

### Zero-Shot Prompting

Cách đơn giản nhất: đưa mô hình chỉ thị trực tiếp mà không có ví dụ. Mô hình dựa hoàn toàn vào huấn luyện để hiểu và thực hiện nhiệm vụ. Cách này phù hợp với yêu cầu đơn giản và hành vi mong đợi rõ ràng.

<img src="../../../translated_images/vi/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Chỉ thị trực tiếp không kèm ví dụ — mô hình suy luận nhiệm vụ từ chỉ thị duy nhất*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Phản hồi: "Tích cực"
```

**Khi dùng:** Phân loại đơn giản, câu hỏi trực tiếp, dịch thuật, hoặc bất kỳ công việc nào mô hình có thể xử lý mà không cần hướng dẫn thêm.

### Few-Shot Prompting

Cung cấp ví dụ minh họa mẫu bạn muốn mô hình làm theo. Mô hình học định dạng đầu vào - đầu ra bạn mong muốn từ ví dụ rồi áp dụng cho đầu vào mới. Cách này cải thiện đáng kể tính nhất quán khi định dạng hoặc hành vi mong muốn không rõ ràng.

<img src="../../../translated_images/vi/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Học từ ví dụ — mô hình nhận dạng mẫu và áp dụng cho đầu vào mới*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**Khi dùng:** Phân loại tùy chỉnh, định dạng nhất quán, tác vụ chuyên ngành, hoặc khi kết quả zero-shot không ổn định.

### Chain of Thought

Yêu cầu mô hình trình bày suy luận từng bước. Thay vì trả lời ngay, mô hình phân tích vấn đề và làm rõ từng phần. Cách này cải thiện độ chính xác với toán học, logic và các tác vụ suy luận nhiều bước.

<img src="../../../translated_images/vi/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Suy luận từng bước — phân tách vấn đề phức tạp thành các bước logic rõ ràng*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Mô hình cho thấy: 15 - 8 = 7, sau đó 7 + 12 = 19 quả táo
```

**Khi dùng:** Bài toán toán học, câu đố logic, gỡ lỗi, hoặc tác vụ cần trình bày quá trình suy nghĩ để tăng độ chính xác và tin cậy.

### Role-Based Prompting

Đặt vai trò hoặc nhân vật cho AI trước khi đặt câu hỏi. Điều này cung cấp ngữ cảnh định hướng giọng điệu, độ sâu và trọng tâm trả lời. Một "kiến trúc sư phần mềm" sẽ đưa lời khuyên khác với "lập trình viên mới" hay "kiểm toán viên bảo mật".

<img src="../../../translated_images/vi/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Thiết lập ngữ cảnh và vai trò — cùng câu hỏi nhưng phản hồi khác nhau tùy vai được gán*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**Khi dùng:** Đánh giá mã, hướng dẫn, phân tích chuyên ngành, hoặc khi bạn cần câu trả lời phù hợp với trình độ chuyên môn hoặc quan điểm cụ thể.

### Prompt Templates

Tạo prompt có thể tái sử dụng với chỗ giữ biến. Thay vì viết prompt mới mỗi lần, hãy tạo một mẫu và điền các giá trị khác nhau. Lớp `PromptTemplate` của LangChain4j hỗ trợ dễ dàng với cú pháp `{{variable}}`.

<img src="../../../translated_images/vi/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompt tái sử dụng có chỗ giữ biến — một mẫu, nhiều lần dùng*

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

**Khi dùng:** Truy vấn lặp lại với đầu vào khác nhau, xử lý hàng loạt, xây dựng quy trình AI tái sử dụng, hoặc khi cấu trúc prompt không thay đổi nhưng dữ liệu đổi.

---

Năm kỹ thuật cơ bản này cung cấp cho bạn bộ công cụ vững chắc cho đa số tác vụ prompt. Phần còn lại của module xây dựng trên đó với **tám mẫu nâng cao** tận dụng khả năng kiểm soát suy luận, tự đánh giá và xuất kết quả có cấu trúc của GPT-5.2.

## Các Mẫu nâng cao

Sau khi nắm căn bản, hãy chuyển sang tám mẫu nâng cao làm module này đặc biệt. Không phải vấn đề nào cũng cần cách tiếp cận giống nhau. Có câu hỏi cần trả lời nhanh, có câu cần suy nghĩ sâu. Có chuyện cần suy luận rõ ràng, có chuyện chỉ cần kết quả. Mỗi mẫu dưới đây tối ưu cho một tình huống khác nhau — và khả năng kiểm soát suy luận của GPT-5.2 càng làm khác biệt này rõ nét hơn.

<img src="../../../translated_images/vi/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Tổng quan tám mẫu kỹ thuật prompt và các trường hợp dùng*

<img src="../../../translated_images/vi/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Khả năng kiểm soát suy luận của GPT-5.2 cho phép bạn chỉ định mức độ suy nghĩ của mô hình — từ trả lời nhanh trực tiếp đến khảo sát sâu rộng*

**Nhiệt huyết thấp (Nhanh & Tập trung)** - Cho câu hỏi đơn giản bạn muốn trả lời nhanh, trực tiếp. Mô hình suy luận tối thiểu - tối đa 2 bước. Dùng cho tính toán, tra cứu hoặc câu hỏi rõ ràng.

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Khám phá với GitHub Copilot:** Mở [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) và hỏi:
> - "Sự khác biệt giữa mẫu nhiệt huyết thấp và cao là gì?"
> - "Cách các thẻ XML trong prompt giúp cấu trúc phản hồi AI ra sao?"
> - "Khi nào nên dùng mẫu tự phản chiếu thay vì chỉ thị trực tiếp?"

**Nhiệt huyết cao (Sâu & Toàn diện)** - Cho những vấn đề phức tạp cần phân tích toàn diện. Mô hình suy nghĩ kỹ, trình bày chi tiết quá trình suy luận. Dùng cho thiết kế hệ thống, quyết định kiến trúc hoặc nghiên cứu phức tạp.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Thực thi tác vụ (Tiến trình theo từng bước)** - Cho quy trình nhiều bước. Mô hình đưa kế hoạch đầu, miêu tả từng bước khi thực hiện rồi tóm tắt. Dùng cho di chuyển dữ liệu, triển khai hoặc bất kỳ quy trình đa bước nào.

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought (Suy luận từng bước) yêu cầu mô hình trình bày quá trình suy nghĩ rõ ràng, tăng độ chính xác cho những tác vụ phức tạp. Việc phân tích từng bước giúp cả người và AI hiểu logic.

> **🤖 Thử với Chat [GitHub Copilot](https://github.com/features/copilot):** Hỏi về mẫu này:
> - "Làm thế nào để điều chỉnh mẫu thực thi tác vụ cho hoạt động thời gian dài?"
> - "Thực hành tốt nhất khi cấu trúc 'tool preambles' trong ứng dụng sản xuất là gì?"
> - "Làm sao để ghi nhận và hiển thị tiến độ trung gian trên giao diện?"

<img src="../../../translated_images/vi/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Quy trình Lập kế hoạch → Thực hiện → Tóm tắt cho các tác vụ đa bước*

**Mã tự phản chiếu** - Cho tạo mã đạt chuẩn sản xuất. Mô hình tạo mã theo tiêu chuẩn, xử lý lỗi phù hợp. Dùng khi xây dựng tính năng hoặc dịch vụ mới.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/vi/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Vòng cải tiến liên tục - tạo, đánh giá, phát hiện vấn đề, cải tiến, lặp lại*

**Phân tích có cấu trúc** - Cho đánh giá nhất quán. Mô hình xem xét mã theo khung cố định (độ chính xác, thực hành, hiệu năng, bảo mật, duy trì). Dùng cho đánh giá mã hoặc kiểm tra chất lượng.

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Thử với Chat [GitHub Copilot](https://github.com/features/copilot):** Hỏi về phân tích có cấu trúc:
> - "Làm sao tùy chỉnh khung phân tích cho các loại đánh giá mã khác nhau?"
> - "Cách tốt nhất xử lý và phản hồi đầu ra có cấu trúc theo chương trình là gì?"
> - "Làm sao đảm bảo mức độ nghiêm trọng nhất quán ở các phiên đánh giá khác nhau?"

<img src="../../../translated_images/vi/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Khung đánh giá mã nhất quán kèm mức độ nghiêm trọng*

**Chat đa lượt** - Cho cuộc trò chuyện cần có bối cảnh. Mô hình nhớ các tin nhắn trước và xây dựng dựa trên đó. Dùng cho hỗ trợ tương tác hoặc hỏi đáp phức tạp.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/vi/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Cách bối cảnh cuộc trò chuyện tích luỹ qua nhiều lượt cho đến giới hạn token*

**Lý luận từng bước** - Cho các vấn đề cần logic hiện rõ. Mô hình trình bày suy luận rõ ràng cho từng bước. Dùng cho toán học, câu đố logic, hoặc khi bạn cần hiểu quá trình suy nghĩ.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/vi/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Phân tách vấn đề thành các bước logic rõ ràng*

**Đầu ra giới hạn** - Cho phản hồi cần định dạng cụ thể. Mô hình tuân thủ nghiêm ngặt các quy tắc định dạng và độ dài. Dùng cho tóm tắt hoặc khi cần kết quả có cấu trúc chính xác.

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

<img src="../../../translated_images/vi/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Áp đặt yêu cầu định dạng, độ dài và cấu trúc cụ thể*

## Sử dụng tài nguyên Azure hiện có

**Xác minh triển khai:**

Đảm bảo file `.env` tồn tại trong thư mục gốc với thông tin xác thực Azure (được tạo trong Module 01):
```bash
cat ../.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Khởi động ứng dụng:**

> **Lưu ý:** Nếu bạn đã khởi động tất cả ứng dụng bằng `./start-all.sh` từ Module 01, module này đã chạy trên cổng 8083. Bạn có thể bỏ qua lệnh khởi động bên dưới và vào trực tiếp http://localhost:8083.
**Tùy chọn 1: Sử dụng Spring Boot Dashboard (Khuyến nghị cho người dùng VS Code)**

Dev container bao gồm tiện ích mở rộng Spring Boot Dashboard, cung cấp giao diện trực quan để quản lý tất cả các ứng dụng Spring Boot. Bạn có thể tìm thấy nó trong Thanh Hoạt động bên trái VS Code (tìm biểu tượng Spring Boot).

Từ Spring Boot Dashboard, bạn có thể:
- Xem tất cả các ứng dụng Spring Boot có trong workspace
- Bắt đầu/dừng các ứng dụng chỉ với một lần nhấp
- Xem nhật ký ứng dụng theo thời gian thực
- Giám sát trạng thái ứng dụng

Chỉ cần nhấp nút phát bên cạnh "prompt-engineering" để bắt đầu module này, hoặc khởi động tất cả các module cùng lúc.

<img src="../../../translated_images/vi/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Tùy chọn 2: Sử dụng shell scripts**

Khởi động tất cả các ứng dụng web (các module 01-04):

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

Hoặc chỉ bắt đầu module này:

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

Cả hai script đều tự động tải các biến môi trường từ file `.env` gốc và sẽ build các file JAR nếu chúng chưa tồn tại.

> **Lưu ý:** Nếu bạn muốn build tất cả các module thủ công trước khi bắt đầu:
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

Mở http://localhost:8083 trong trình duyệt của bạn.

**Để dừng:**

**Bash:**
```bash
./stop.sh  # Chỉ mô-đun này
# Hoặc
cd .. && ./stop-all.sh  # Tất cả mô-đun
```

**PowerShell:**
```powershell
.\stop.ps1  # Chỉ mô-đun này
# Hoặc
cd ..; .\stop-all.ps1  # Tất cả các mô-đun
```

## Ảnh chụp màn hình ứng dụng

<img src="../../../translated_images/vi/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Bảng điều khiển chính hiển thị tất cả 8 mẫu prompt engineering với đặc điểm và các trường hợp sử dụng của chúng*

## Khám phá các Mẫu

Giao diện web cho phép bạn thử nghiệm với các chiến lược prompt khác nhau. Mỗi mẫu giải quyết các vấn đề khác biệt — hãy thử để xem cách nào phù hợp nhất với trường hợp sử dụng của bạn.

> **Lưu ý: Streaming vs Không Streaming** — Mỗi trang mẫu cung cấp hai nút: **🔴 Stream Response (Live)** và tùy chọn **Không streaming**. Streaming sử dụng Server-Sent Events (SSE) để hiển thị các token theo thời gian thực khi mô hình tạo chúng, bạn sẽ thấy tiến trình ngay lập tức. Tùy chọn không streaming sẽ đợi toàn bộ phản hồi xong mới hiển thị. Đối với các prompt cần suy nghĩ sâu (ví dụ: High Eagerness, Self-Reflecting Code), lựa chọn không streaming có thể mất rất nhiều thời gian — đôi khi vài phút — mà không có phản hồi rõ ràng. **Sử dụng streaming khi thử nghiệm các prompt phức tạp** để bạn có thể thấy mô hình làm việc và tránh cảm giác yêu cầu đã hết thời gian chờ.
>
> **Lưu ý: Yêu cầu trình duyệt** — Tính năng streaming sử dụng Fetch Streams API (`response.body.getReader()`) yêu cầu một trình duyệt đầy đủ (Chrome, Edge, Firefox, Safari). Nó **không** hoạt động trên Simple Browser tích hợp trong VS Code, vì webview của nó không hỗ trợ ReadableStream API. Nếu bạn dùng Simple Browser, các nút không streaming vẫn hoạt động bình thường — chỉ các nút streaming bị ảnh hưởng. Mở `http://localhost:8083` trong trình duyệt bên ngoài để trải nghiệm đầy đủ.

### Low vs High Eagerness

Hỏi một câu đơn giản như "What is 15% of 200?" với Low Eagerness. Bạn sẽ nhận được câu trả lời ngay lập tức, trực tiếp. Bây giờ hỏi một câu phức tạp như "Design a caching strategy for a high-traffic API" với High Eagerness. Nhấn **🔴 Stream Response (Live)** và quan sát quá trình suy luận chi tiết của mô hình hiện ra từng token một. Cùng một mô hình, cùng cấu trúc câu hỏi — nhưng prompt cho nó biết mức độ suy nghĩ cần thực hiện.

### Thực thi tác vụ (Tool Preambles)

Các workflow nhiều bước được hưởng lợi từ việc lập kế hoạch trước và diễn giải tiến trình. Mô hình phác thảo những gì nó sẽ làm, kể lại từng bước, sau đó tóm tắt kết quả.

### Self-Reflecting Code

Thử "Create an email validation service". Thay vì chỉ tạo code rồi dừng lại, mô hình sẽ tạo, đánh giá theo tiêu chí chất lượng, xác định điểm yếu và cải thiện. Bạn sẽ thấy nó lặp lại nhiều lần cho đến khi code đạt chuẩn sản xuất.

### Structured Analysis

Review code cần có khung đánh giá nhất quán. Mô hình phân tích code theo các danh mục cố định (độ chính xác, thực hành, hiệu suất, bảo mật) với các mức độ nghiêm trọng.

### Multi-Turn Chat

Hỏi "What is Spring Boot?" sau đó ngay lập tức hỏi tiếp "Show me an example". Mô hình nhớ câu hỏi đầu và cho bạn ví dụ về Spring Boot cụ thể. Nếu không có bộ nhớ, câu thứ hai sẽ quá mơ hồ.

### Step-by-Step Reasoning

Chọn một bài toán toán học và thử với cả Step-by-Step Reasoning và Low Eagerness. Low eagerness chỉ trả lời câu trả lời - nhanh nhưng không minh bạch. Step-by-step hiển thị từng phép tính và quyết định.

### Constrained Output

Khi bạn cần định dạng hoặc số từ cụ thể, mẫu này bắt buộc tuân thủ nghiêm ngặt. Thử tạo một bản tóm tắt đúng 100 từ theo dạng gạch đầu dòng.

## Điều bạn thực sự đang học

**Nỗ lực Suy luận Thay đổi Mọi thứ**

GPT-5.2 cho phép bạn điều khiển nỗ lực tính toán thông qua các prompt. Nỗ lực thấp nghĩa là phản hồi nhanh với mức khám phá tối thiểu. Nỗ lực cao nghĩa là mô hình dành thời gian suy nghĩ sâu sắc. Bạn đang học cách cân bằng nỗ lực với độ phức tạp của tác vụ — đừng lãng phí thời gian cho câu hỏi đơn giản, nhưng cũng đừng vội vàng với các quyết định phức tạp.

**Cấu trúc Dẫn dắt Hành vi**

Bạn có nhận thấy các thẻ XML trong prompt không? Chúng không chỉ để trang trí. Mô hình tuân theo các hướng dẫn có cấu trúc một cách đáng tin cậy hơn so với văn bản tự do. Khi bạn cần quá trình nhiều bước hoặc logic phức tạp, cấu trúc giúp mô hình theo dõi vị trí và bước tiếp theo.

<img src="../../../translated_images/vi/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Cấu trúc của một prompt được tổ chức tốt với các phần rõ ràng và cấu trúc kiểu XML*

**Chất lượng Qua Tự Đánh giá**

Các mẫu tự phản chiếu hoạt động bằng cách làm cho tiêu chí chất lượng trở nên rõ ràng. Thay vì hy vọng mô hình "làm đúng", bạn chỉ cho nó chính xác "đúng" nghĩa là gì: logic chính xác, xử lý lỗi, hiệu suất, bảo mật. Mô hình sau đó có thể tự đánh giá đầu ra và cải thiện. Điều này biến việc tạo code từ trò xổ số thành một quy trình có kiểm soát.

**Ngữ cảnh Có Giới hạn**

Cuộc trò chuyện nhiều vòng hoạt động bằng cách bao gồm lịch sử tin nhắn với mỗi yêu cầu. Nhưng có giới hạn — mỗi mô hình có số lượng token tối đa. Khi cuộc trò chuyện kéo dài, bạn sẽ cần các chiến lược để giữ lại ngữ cảnh phù hợp mà không vượt quá giới hạn đó. Module này chỉ cho bạn cách bộ nhớ hoạt động; sau này bạn sẽ học khi nào nên tóm tắt, khi nào nên quên, và khi nào nên truy xuất lại.

## Bước Tiếp theo

**Module tiếp theo:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Điều hướng:** [← Trước: Module 01 - Giới thiệu](../01-introduction/README.md) | [Trở về Chính](../README.md) | [Tiếp theo: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố từ chối trách nhiệm**:  
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng các bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ gốc được coi là nguồn tham khảo chính xác nhất. Đối với các thông tin quan trọng, chúng tôi khuyến nghị sử dụng dịch thuật chuyên nghiệp do con người thực hiện. Chúng tôi không chịu trách nhiệm đối với bất kỳ sự hiểu lầm hoặc diễn giải sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->