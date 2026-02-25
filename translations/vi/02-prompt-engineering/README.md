# Module 02: Kỹ Thuật Prompt với GPT-5.2

## Mục Lục

- [Bạn Sẽ Học Gì](../../../02-prompt-engineering)
- [Yêu Cầu Trước](../../../02-prompt-engineering)
- [Hiểu Về Kỹ Thuật Prompt](../../../02-prompt-engineering)
- [Các Nguyên Tắc Cơ Bản về Kỹ Thuật Prompt](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Các Mẫu Nâng Cao](../../../02-prompt-engineering)
- [Sử Dụng Tài Nguyên Azure Có Sẵn](../../../02-prompt-engineering)
- [Ảnh Chụp Ứng Dụng](../../../02-prompt-engineering)
- [Khám Phá Các Mẫu](../../../02-prompt-engineering)
  - [Sự Khác Biệt giữa Low và High Eagerness](../../../02-prompt-engineering)
  - [Thực Thi Tác Vụ (Tool Preambles)](../../../02-prompt-engineering)
  - [Code Tự Phản Chiếu](../../../02-prompt-engineering)
  - [Phân Tích Có Cấu Trúc](../../../02-prompt-engineering)
  - [Trò Chuyện Nhiều Lượt](../../../02-prompt-engineering)
  - [Lý Luận Bước Từng Bước](../../../02-prompt-engineering)
  - [Đầu Ra Hạn Chế](../../../02-prompt-engineering)
- [Bạn Thật Sự Đang Học Gì](../../../02-prompt-engineering)
- [Bước Tiếp Theo](../../../02-prompt-engineering)

## Bạn Sẽ Học Gì

<img src="../../../translated_images/vi/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

Trong module trước, bạn đã thấy cách bộ nhớ hỗ trợ AI hội thoại và sử dụng Mô hình GitHub cho các tương tác cơ bản. Giờ đây chúng ta sẽ tập trung vào cách bạn đặt câu hỏi — chính là các prompt — sử dụng GPT-5.2 của Azure OpenAI. Cách bạn cấu trúc prompt ảnh hưởng rất lớn đến chất lượng câu trả lời bạn nhận được. Chúng ta bắt đầu với việc ôn lại các kỹ thuật prompt cơ bản, sau đó chuyển sang tám mẫu nâng cao tận dụng tối đa khả năng của GPT-5.2.

Chúng ta sẽ sử dụng GPT-5.2 vì nó cho phép điều khiển quá trình suy nghĩ - bạn có thể chỉ dẫn mô hình về mức độ suy luận trước khi trả lời. Điều này làm rõ hơn các chiến lược prompt khác nhau và giúp bạn hiểu khi nào nên sử dụng từng cách. Chúng ta cũng được lợi từ giới hạn tốc độ ít hơn của Azure cho GPT-5.2 so với Mô hình GitHub.

## Yêu Cầu Trước

- Hoàn thành Module 01 (đã triển khai tài nguyên Azure OpenAI)
- File `.env` trong thư mục gốc có chứa thông tin xác thực Azure (được tạo bởi `azd up` trong Module 01)

> **Lưu ý:** Nếu bạn chưa hoàn thành Module 01, hãy làm theo hướng dẫn triển khai ở đó trước.

## Hiểu Về Kỹ Thuật Prompt

<img src="../../../translated_images/vi/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Kỹ thuật prompt là thiết kế văn bản đầu vào sao cho liên tục cho ra kết quả bạn cần. Nó không chỉ là đặt câu hỏi - mà còn là cấu trúc các yêu cầu sao cho mô hình hiểu chính xác bạn muốn gì và cách thức trả lời.

Hãy tưởng tượng bạn đang cho đồng nghiệp chỉ dẫn. "Sửa lỗi" thì mơ hồ. "Sửa lỗi null pointer exception trong UserService.java dòng 45 bằng cách thêm kiểm tra null" thì cụ thể. Mô hình ngôn ngữ cũng vậy — sự cụ thể và cấu trúc rất quan trọng.

<img src="../../../translated_images/vi/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j cung cấp hạ tầng — kết nối mô hình, bộ nhớ, và các kiểu tin nhắn — trong khi các mẫu prompt chỉ là văn bản được cấu trúc cẩn thận bạn gửi qua hạ tầng đó. Các khối xây dựng chính là `SystemMessage` (thiết lập hành vi và vai trò AI) và `UserMessage` (mang yêu cầu thực tế của bạn).

## Các Nguyên Tắc Cơ Bản về Kỹ Thuật Prompt

<img src="../../../translated_images/vi/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Trước khi đi vào các mẫu nâng cao trong module này, hãy ôn lại năm kỹ thuật prompt cơ bản. Đây là nền tảng mà mọi kỹ sư prompt nên biết. Nếu bạn đã làm qua [module Khởi Đầu Nhanh](../00-quick-start/README.md#2-prompt-patterns), bạn đã thấy những kỹ thuật này hoạt động — đây là khung khái niệm phía sau chúng.

### Zero-Shot Prompting

Cách đơn giản nhất: đưa cho mô hình chỉ dẫn trực tiếp mà không có ví dụ. Mô hình hoàn toàn dựa vào đào tạo của nó để hiểu và thực hiện nhiệm vụ. Phương pháp này hiệu quả với các yêu cầu đơn giản, hành vi mong đợi rõ ràng.

<img src="../../../translated_images/vi/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Chỉ dẫn trực tiếp không có ví dụ — mô hình suy luận nhiệm vụ từ chỉ thị*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Phản hồi: "Tích cực"
```

**Khi nào dùng:** Phân loại đơn giản, câu hỏi trực tiếp, dịch thuật, hoặc bất kỳ nhiệm vụ nào mô hình có thể xử lý mà không cần hướng dẫn thêm.

### Few-Shot Prompting

Cung cấp ví dụ minh họa mẫu bạn muốn mô hình theo. Mô hình học định dạng đầu ra đầu vào từ ví dụ và áp dụng cho dữ liệu mới. Cách này cải thiện đáng kể sự nhất quán cho các nhiệm vụ mà định dạng hay hành vi mong muốn không rõ ràng.

<img src="../../../translated_images/vi/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Học từ ví dụ — mô hình nhận diện mẫu và áp dụng cho đầu vào mới*

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

**Khi nào dùng:** Phân loại tuỳ chỉnh, định dạng nhất quán, nhiệm vụ theo lĩnh vực chuyên biệt, hoặc khi kết quả zero-shot không ổn định.

### Chain of Thought

Yêu cầu mô hình trình bày suy luận từng bước. Thay vì nhảy ngay đến câu trả lời, mô hình phân tích vấn đề và làm rõ từng phần một. Cách này tăng độ chính xác cho các bài toán toán học, logic, và suy luận đa bước.

<img src="../../../translated_images/vi/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Suy luận từng bước — chia nhỏ vấn đề phức tạp thành các bước logic rõ ràng*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Mô hình cho thấy: 15 - 8 = 7, sau đó 7 + 12 = 19 quả táo
```

**Khi nào dùng:** Bài toán toán học, câu đố logic, gỡ lỗi, hoặc bất kỳ nhiệm vụ nào mà quá trình suy luận rõ ràng giúp nâng cao độ chính xác và tạo niềm tin.

### Role-Based Prompting

Đặt vai trò hoặc persona cho AI trước khi hỏi. Điều này tạo ngữ cảnh định hình cách trả lời về giọng điệu, độ sâu, và trọng tâm. "Kiến trúc sư phần mềm" cho lời khuyên khác với "lập trình viên mới vào nghề" hay "kiểm toán viên bảo mật".

<img src="../../../translated_images/vi/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Thiết lập ngữ cảnh và persona — cùng một câu hỏi nhưng nhận được câu trả lời khác tùy vai trò được giao*

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

**Khi nào dùng:** Đánh giá code, hướng dẫn, phân tích theo chuyên ngành, hoặc khi bạn cần câu trả lời phù hợp với cấp độ chuyên môn hoặc góc nhìn cụ thể.

### Prompt Templates

Tạo các prompt có thể tái sử dụng với chỗ giữ biến. Thay vì viết prompt mới mỗi lần, định nghĩa một mẫu rồi điền các giá trị khác nhau. Lớp `PromptTemplate` của LangChain4j hỗ trợ dễ dàng với cú pháp `{{variable}}`.

<img src="../../../translated_images/vi/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompt tái sử dụng với chỗ giữ biến — một mẫu, nhiều lần dùng*

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

**Khi nào dùng:** Truy vấn lặp với các đầu vào khác nhau, xử lý hàng loạt, xây dựng luồng AI có thể dùng lại, hoặc bất kỳ tình huống nào mà cấu trúc prompt không đổi nhưng dữ liệu thay đổi.

---

Năm nguyên tắc cơ bản này cung cấp cho bạn bộ công cụ vững chắc cho hầu hết nhiệm vụ prompt. Phần còn lại của module này xây dựng trên chúng với **tám mẫu nâng cao** tận dụng khả năng điều khiển suy luận, tự đánh giá, và đầu ra có cấu trúc của GPT-5.2.

## Các Mẫu Nâng Cao

Sau khi nắm được các nguyên tắc, chúng ta sẽ chuyển sang tám mẫu nâng cao làm module này trở nên đặc biệt. Không phải vấn đề nào cũng cần cùng cách tiếp cận. Có câu hỏi cần câu trả lời nhanh, có câu đòi hỏi suy nghĩ sâu. Có câu cần suy luận hiển thị rõ, có câu chỉ cần kết quả. Mỗi mẫu dưới đây được tối ưu cho từng tình huống khác nhau — và khả năng điều khiển suy luận của GPT-5.2 làm sự khác biệt càng rõ nét hơn.

<img src="../../../translated_images/vi/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Tổng quan tám mẫu kỹ thuật prompt và các tình huống sử dụng*

<img src="../../../translated_images/vi/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Khả năng điều khiển suy luận của GPT-5.2 cho phép bạn chỉ định mức độ suy nghĩ của mô hình — từ câu trả lời nhanh trực tiếp đến khám phá sâu sắc*

**Low Eagerness (Nhanh & Tập Trung)** - Dành cho các câu hỏi đơn giản bạn muốn câu trả lời nhanh, trực tiếp. Mô hình thực hiện suy luận tối thiểu - tối đa 2 bước. Dùng cho tính toán, tra cứu, hoặc câu hỏi đơn giản.

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

> 💡 **Thử với GitHub Copilot:** Mở [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) và hỏi:
> - "Sự khác biệt giữa mẫu low eagerness và high eagerness trong prompt là gì?"
> - "Các thẻ XML trong prompt giúp cấu trúc phản hồi AI như thế nào?"
> - "Khi nào nên dùng mẫu tự phản chiếu thay vì chỉ dẫn trực tiếp?"

**High Eagerness (Sâu & Toàn Diện)** - Dành cho các vấn đề phức tạp cần phân tích kỹ lưỡng. Mô hình khám phá kỹ và trình bày suy luận chi tiết. Dùng cho thiết kế hệ thống, quyết định kiến trúc, hoặc nghiên cứu phức tạp.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Thực Thi Tác Vụ (Tiến Độ Bước Từng Bước)** - Dùng cho luồng công việc đa bước. Mô hình cung cấp kế hoạch ban đầu, mô tả mỗi bước trong quá trình làm, rồi tóm tắt cuối cùng. Dùng cho di trú dữ liệu, triển khai, hoặc bất kỳ quy trình nhiều bước nào.

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

Prompt Chain-of-Thought yêu cầu mô hình trình bày quá trình suy luận, cải thiện độ chính xác cho nhiệm vụ phức tạp. Việc phân chia bước giúp cả người và AI hiểu logic.

> **🤖 Thử với Chat [GitHub Copilot](https://github.com/features/copilot):** Hỏi về mẫu này:
> - "Làm thế nào để thích nghi mẫu thực thi tác vụ cho các hoạt động chạy lâu?"
> - "Thực hành tốt nhất khi cấu trúc tool preambles trong ứng dụng sản xuất là gì?"
> - "Làm sao để thu thập và hiển thị cập nhật tiến độ trung gian trong giao diện người dùng?"

<img src="../../../translated_images/vi/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Kế hoạch → Thực hiện → Tóm tắt luồng công việc cho các tác vụ nhiều bước*

**Code Tự Phản Chiếu** - Dành cho tạo mã chất lượng sản xuất. Mô hình sinh mã chuẩn theo tiêu chuẩn với xử lý lỗi chính xác. Dùng khi xây dựng tính năng hay dịch vụ mới.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/vi/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Một vòng cải tiến lặp - sinh, đánh giá, xác định lỗi, cải thiện, lặp lại*

**Phân Tích Có Cấu Trúc** - Cung cấp đánh giá nhất quán. Mô hình xem xét code theo khuôn khổ cố định (độ chính xác, thực hành, hiệu năng, bảo mật, khả năng bảo trì). Dùng cho đánh giá code hoặc kiểm định chất lượng.

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
> - "Làm sao tùy chỉnh khuôn khổ phân tích cho các loại đánh giá code khác nhau?"
> - "Cách hay nhất để phân tích và xử lý đầu ra có cấu trúc một cách lập trình là gì?"
> - "Làm thế nào đảm bảo mức độ nghiêm trọng nhất quán giữa các phiên đánh giá?"

<img src="../../../translated_images/vi/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Khuôn khổ đánh giá code nhất quán với mức độ nghiêm trọng*

**Trò Chuyện Nhiều Lượt** - Cho hội thoại cần ngữ cảnh. Mô hình nhớ các tin nhắn trước và xây dựng dựa trên đó. Dùng cho phiên hỗ trợ tương tác hoặc Q&A phức tạp.

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

*Cách ngữ cảnh hội thoại tích lũy qua nhiều lượt cho đến khi đạt giới hạn token*

**Lý Luận Bước Từng Bước** - Dành cho vấn đề cần logic rõ ràng. Mô hình trình bày suy luận cụ thể cho từng bước. Dùng cho bài toán toán học, câu đố logic, hoặc khi bạn cần hiểu quy trình suy nghĩ.

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

*Phân tích vấn đề thành các bước logic rõ ràng*

**Đầu Ra Hạn Chế** - Cho phản hồi với yêu cầu định dạng cụ thể. Mô hình tuân thủ nghiêm túc quy tắc về định dạng và độ dài. Dùng cho các bản tóm tắt hoặc khi cần đầu ra chính xác về cấu trúc.

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

*Tuân thủ các yêu cầu về định dạng, độ dài và cấu trúc cụ thể*

## Sử Dụng Tài Nguyên Azure Có Sẵn

**Xác minh việc triển khai:**

Đảm bảo file `.env` tồn tại trong thư mục gốc và có thông tin xác thực Azure (được tạo trong Module 01):
```bash
cat ../.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Khởi động ứng dụng:**

> **Lưu ý:** Nếu bạn đã khởi động tất cả ứng dụng bằng `./start-all.sh` từ Module 01, module này đã chạy trên cổng 8083. Bạn có thể bỏ qua lệnh khởi động dưới đây và truy cập trực tiếp http://localhost:8083.

**Tùy chọn 1: Sử dụng Spring Boot Dashboard (Khuyến nghị cho người dùng VS Code)**

Container phát triển bao gồm tiện ích mở rộng Spring Boot Dashboard, cung cấp giao diện trực quan để quản lý tất cả các ứng dụng Spring Boot. Bạn có thể tìm thấy nó trên Thanh hoạt động bên trái cửa sổ VS Code (biểu tượng Spring Boot).

Từ Spring Boot Dashboard, bạn có thể:
- Xem tất cả các ứng dụng Spring Boot có sẵn trong workspace
- Khởi động/dừng ứng dụng chỉ với một cú nhấp chuột
- Xem nhật ký ứng dụng theo thời gian thực
- Giám sát tình trạng ứng dụng
Chỉ cần nhấp vào nút phát bên cạnh "prompt-engineering" để bắt đầu module này, hoặc khởi động tất cả các module cùng lúc.

<img src="../../../translated_images/vi/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Lựa chọn 2: Sử dụng các script shell**

Khởi chạy tất cả các ứng dụng web (các module 01-04):

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

Cả hai script tự động tải biến môi trường từ file `.env` ở thư mục gốc và sẽ biên dịch JAR nếu chúng chưa tồn tại.

> **Lưu ý:** Nếu bạn muốn tự xây dựng tất cả các module trước khi khởi động:
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

Mở http://localhost:8083 trên trình duyệt của bạn.

**Để dừng:**

**Bash:**
```bash
./stop.sh  # Chỉ mô-đun này
# Hoặc
cd .. && ./stop-all.sh  # Tất cả các mô-đun
```

**PowerShell:**
```powershell
.\stop.ps1  # Chỉ module này
# Hoặc
cd ..; .\stop-all.ps1  # Tất cả các module
```

## Ảnh màn hình ứng dụng

<img src="../../../translated_images/vi/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Bảng điều khiển chính hiển thị tất cả 8 mẫu kỹ thuật prompt với đặc điểm và các trường hợp sử dụng của chúng*

## Khám phá các Mẫu

Giao diện web cho phép bạn thử nghiệm với các chiến lược prompting khác nhau. Mỗi mẫu giải quyết các vấn đề khác nhau - hãy thử để xem khi nào mỗi phương pháp phát huy tác dụng.

> **Lưu ý: Streaming và Không streaming** — Mỗi trang mẫu cung cấp hai nút: **🔴 Stream Response (Live)** và một tùy chọn **Không streaming**. Streaming sử dụng Server-Sent Events (SSE) để hiển thị các token ngay lập tức khi mô hình tạo ra chúng, vì vậy bạn thấy tiến trình ngay lập tức. Tùy chọn không streaming sẽ đợi đến khi có toàn bộ phản hồi mới hiển thị. Với các prompt kích hoạt suy luận sâu (ví dụ: High Eagerness, Self-Reflecting Code), cuộc gọi không streaming có thể mất rất lâu — đôi khi là vài phút — mà không có phản hồi hiển thị nào. **Hãy dùng streaming khi thử nghiệm với các prompt phức tạp** để bạn có thể thấy mô hình hoạt động và tránh hiểu lầm rằng yêu cầu đã hết giờ.
>
> **Lưu ý: Yêu cầu về trình duyệt** — Tính năng streaming sử dụng Fetch Streams API (`response.body.getReader()`) yêu cầu một trình duyệt đầy đủ (Chrome, Edge, Firefox, Safari). Nó **không** hoạt động trong Simple Browser tích hợp sẵn của VS Code, vì webview của nó không hỗ trợ ReadableStream API. Nếu bạn dùng Simple Browser, các nút không streaming vẫn hoạt động bình thường — chỉ các nút streaming bị ảnh hưởng. Hãy mở `http://localhost:8083` trong trình duyệt bên ngoài để có trải nghiệm đầy đủ.

### Low vs High Eagerness

Hãy hỏi một câu đơn giản như "15% của 200 là bao nhiêu?" sử dụng Low Eagerness. Bạn sẽ nhận được câu trả lời nhanh và trực tiếp. Bây giờ hãy hỏi một câu phức tạp như "Thiết kế chiến lược caching cho API có lưu lượng lớn" sử dụng High Eagerness. Nhấn **🔴 Stream Response (Live)** và theo dõi quá trình suy luận chi tiết của mô hình xuất hiện từng token một. Cùng một mô hình, cùng cấu trúc câu hỏi - nhưng prompt cho nó biết phải nghĩ nhiều đến mức nào.

### Thực hiện nhiệm vụ (Tool Preambles)

Các quy trình làm việc nhiều bước được hưởng lợi từ kế hoạch trước và việc tường thuật tiến trình. Mô hình nêu ra những gì sẽ làm, tường thuật từng bước, rồi tóm tắt kết quả.

### Self-Reflecting Code

Thử "Tạo một dịch vụ kiểm tra email hợp lệ". Thay vì chỉ tạo ra code rồi dừng, mô hình tạo, đánh giá theo tiêu chí chất lượng, xác định điểm yếu và cải thiện. Bạn sẽ thấy nó lặp đi lặp lại cho đến khi code đạt chuẩn sản xuất.

### Phân tích có cấu trúc

Việc review code cần các khung đánh giá nhất quán. Mô hình phân tích code theo các hạng mục cố định (độ chính xác, thực hành, hiệu suất, bảo mật) với các mức độ nghiêm trọng.

### Đàm thoại đa lượt

Hỏi "Spring Boot là gì?" rồi ngay lập tức tiếp tục bằng "Cho tôi xem một ví dụ". Mô hình nhớ câu hỏi đầu và cho bạn ví dụ về Spring Boot cụ thể. Nếu không có bộ nhớ, câu hỏi thứ hai sẽ quá mơ hồ.

### Suy luận từng bước

Chọn một bài toán toán học và thử với cả Step-by-Step Reasoning và Low Eagerness. Low eagerness chỉ đưa ra đáp án - nhanh nhưng không rõ ràng. Suy luận từng bước sẽ hiển thị từng phép tính và quyết định.

### Đầu ra bị giới hạn

Khi bạn cần định dạng hoặc số từ cụ thể, mẫu này ép buộc tuân thủ nghiêm ngặt. Thử tạo một bản tóm tắt chính xác 100 từ theo dạng điểm.

## Bạn thực sự đang học gì

**Nỗ lực suy luận thay đổi mọi thứ**

GPT-5.2 cho phép bạn kiểm soát nỗ lực tính toán qua prompt. Nỗ lực thấp có nghĩa phản hồi nhanh với ít thăm dò. Nỗ lực cao tức là mô hình dành thời gian nghĩ kỹ. Bạn đang học cách cân bằng nỗ lực với độ phức tạp của nhiệm vụ - đừng lãng phí thời gian cho câu hỏi đơn giản, nhưng cũng không nên vội vàng với quyết định phức tạp.

**Cấu trúc định hướng hành vi**

Bạn có nhận thấy các thẻ XML trong prompt không? Chúng không phải để trang trí. Mô hình tuân theo hướng dẫn có cấu trúc đáng tin cậy hơn văn bản tự do. Khi bạn cần quy trình đa bước hoặc logic phức tạp, cấu trúc giúp mô hình theo dõi đang ở đâu và tiếp theo là gì.

<img src="../../../translated_images/vi/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Cấu trúc của một prompt được tổ chức tốt với các phần rõ ràng và cách sắp xếp theo kiểu XML*

**Chất lượng qua tự đánh giá**

Các mẫu tự phản chiếu hoạt động bằng việc làm rõ các tiêu chí chất lượng. Thay vì hy vọng mô hình "làm đúng", bạn nói cho nó biết chính xác “đúng” nghĩa là gì: logic chính xác, xử lý lỗi, hiệu suất, bảo mật. Mô hình có thể tự đánh giá đầu ra và cải thiện. Điều này biến việc tạo code khỏi chỉ là trò may rủi thành một quy trình.

**Ngữ cảnh là hữu hạn**

Đàm thoại đa lượt hoạt động bằng cách bao gồm lịch sử tin nhắn trong mỗi yêu cầu. Nhưng có giới hạn - mỗi mô hình có tối đa số lượng token. Khi cuộc hội thoại kéo dài, bạn cần chiến lược giữ lại ngữ cảnh phù hợp mà không vượt quá giới hạn đó. Module này cho bạn biết cách bộ nhớ hoạt động; sau này bạn sẽ học khi nào cần tóm tắt, khi nào quên và khi nào truy hồi.

## Bước tiếp theo

**Module kế tiếp:** [03-rag - RAG (Phát sinh tăng cường truy xuất)](../03-rag/README.md)

---

**Điều hướng:** [← Trước: Module 01 - Giới thiệu](../01-introduction/README.md) | [Quay lại Trang chính](../README.md) | [Tiếp theo: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố từ chối trách nhiệm**:
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng các bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ nguyên bản nên được xem là nguồn thông tin chính xác nhất. Đối với thông tin quan trọng, nên sử dụng dịch vụ dịch thuật chuyên nghiệp do con người thực hiện. Chúng tôi không chịu trách nhiệm về bất kỳ sự hiểu lầm hoặc giải thích sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->