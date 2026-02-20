# Module 02: Kỹ Thuật Prompt với GPT-5.2

## Mục Lục

- [Bạn Sẽ Học Gì](../../../02-prompt-engineering)
- [Điều Kiện Tiên Quyết](../../../02-prompt-engineering)
- [Hiểu Về Kỹ Thuật Prompt](../../../02-prompt-engineering)
- [Cơ Bản về Kỹ Thuật Prompt](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Mẫu Nâng Cao](../../../02-prompt-engineering)
- [Sử Dụng Tài Nguyên Azure Hiện Có](../../../02-prompt-engineering)
- [Ảnh Chụp Màn Hình Ứng Dụng](../../../02-prompt-engineering)
- [Khám Phá Các Mẫu](../../../02-prompt-engineering)
  - [Low vs High Eagerness](../../../02-prompt-engineering)
  - [Task Execution (Tool Preambles)](../../../02-prompt-engineering)
  - [Self-Reflecting Code](../../../02-prompt-engineering)
  - [Structured Analysis](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Step-by-Step Reasoning](../../../02-prompt-engineering)
  - [Constrained Output](../../../02-prompt-engineering)
- [Bạn Thực Sự Đang Học Gì](../../../02-prompt-engineering)
- [Các Bước Tiếp Theo](../../../02-prompt-engineering)

## Bạn Sẽ Học Gì

<img src="../../../translated_images/vi/what-youll-learn.c68269ac048503b2.webp" alt="Bạn Sẽ Học Gì" width="800"/>

Trong module trước, bạn đã thấy cách bộ nhớ giúp AI hội thoại và sử dụng GitHub Models cho các tương tác cơ bản. Bây giờ chúng ta sẽ tập trung vào cách bạn đặt câu hỏi — chính là các prompt — sử dụng GPT-5.2 của Azure OpenAI. Cách bạn xây dựng prompt ảnh hưởng rất lớn đến chất lượng câu trả lời bạn nhận được. Chúng ta bắt đầu với ôn tập các kỹ thuật prompt cơ bản, sau đó đi vào tám mẫu nâng cao tận dụng tối đa khả năng của GPT-5.2.

Chúng ta sẽ dùng GPT-5.2 vì nó giới thiệu điều khiển suy luận - bạn có thể bảo mô hình suy nghĩ bao nhiêu trước khi trả lời. Điều này làm cho các chiến lược prompt khác nhau trở nên rõ ràng hơn và giúp bạn hiểu khi nào nên dùng phương pháp nào. Chúng ta cũng được lợi từ giới hạn tốc độ ít hơn của Azure cho GPT-5.2 so với GitHub Models.

## Điều Kiện Tiên Quyết

- Hoàn thành Module 01 (đã triển khai tài nguyên Azure OpenAI)
- Có file `.env` trong thư mục gốc chứa thông tin đăng nhập Azure (được tạo bởi lệnh `azd up` trong Module 01)

> **Lưu ý:** Nếu bạn chưa hoàn thành Module 01, hãy làm theo hướng dẫn triển khai tại đó trước.

## Hiểu Về Kỹ Thuật Prompt

<img src="../../../translated_images/vi/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Kỹ Thuật Prompt Là Gì?" width="800"/>

Kỹ thuật prompt là về thiết kế văn bản đầu vào sao cho luôn thu được kết quả bạn cần. Nó không chỉ là đặt câu hỏi - mà là cấu trúc yêu cầu để mô hình hiểu chính xác bạn muốn gì và cách trả lời.

Hãy nghĩ như đang đưa chỉ dẫn cho đồng nghiệp. "Sửa lỗi" thì mơ hồ. "Sửa lỗi null pointer exception trong UserService.java dòng 45 bằng cách thêm kiểm tra null" thì cụ thể. Các mô hình ngôn ngữ cũng vậy - tính cụ thể và cấu trúc là quan trọng.

<img src="../../../translated_images/vi/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j Đóng Vai Trò Gì" width="800"/>

LangChain4j cung cấp hạ tầng — kết nối mô hình, bộ nhớ, và các loại tin nhắn — còn các mẫu prompt chỉ là các văn bản được cấu trúc cẩn thận bạn gửi qua hạ tầng đó. Các thành phần chính là `SystemMessage` (thiết lập hành vi và vai trò AI) và `UserMessage` (mang yêu cầu thực tế của bạn).

## Cơ Bản về Kỹ Thuật Prompt

<img src="../../../translated_images/vi/five-patterns-overview.160f35045ffd2a94.webp" alt="Tổng Quan Năm Mẫu Kỹ Thuật Prompt" width="800"/>

Trước khi đi vào các mẫu nâng cao trong module này, hãy ôn lại năm kỹ thuật prompt cơ bản. Đây là các khối xây dựng mà mọi kỹ sư prompt nên biết. Nếu bạn đã làm qua [module Quick Start](../00-quick-start/README.md#2-prompt-patterns), bạn đã thấy chúng hoạt động — đây là khuôn khổ khái niệm đằng sau chúng.

### Zero-Shot Prompting

Cách đơn giản nhất: đưa ra chỉ dẫn trực tiếp không có ví dụ. Mô hình hoàn toàn dựa vào quá trình huấn luyện để hiểu và thực hiện nhiệm vụ. Cách này hiệu quả với các yêu cầu đơn giản và hành vi mong đợi rõ ràng.

<img src="../../../translated_images/vi/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Chỉ dẫn trực tiếp không có ví dụ — mô hình suy ra nhiệm vụ từ chỉ dẫn*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Phản hồi: "Tích cực"
```

**Khi nào dùng:** Phân loại đơn giản, câu hỏi trực tiếp, dịch thuật hoặc bất kỳ nhiệm vụ nào mô hình có thể xử lý mà không cần hướng dẫn thêm.

### Few-Shot Prompting

Cung cấp các ví dụ thể hiện mẫu mà bạn muốn mô hình theo. Mô hình học định dạng đầu vào-đầu ra từ ví dụ của bạn và áp dụng cho đầu vào mới. Điều này nâng cao đáng kể tính nhất quán khi định dạng hoặc hành vi mong muốn không rõ ràng.

<img src="../../../translated_images/vi/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Học từ ví dụ — mô hình nhận biết mẫu và áp dụng cho đầu vào mới*

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

**Khi nào dùng:** Phân loại tùy chỉnh, định dạng nhất quán, nhiệm vụ chuyên ngành hoặc khi kết quả zero-shot không đồng nhất.

### Chain of Thought

Yêu cầu mô hình trình bày suy luận bước từng bước. Thay vì nhảy tới đáp án, mô hình phân tích vấn đề và làm rõ từng phần. Cách này nâng cao độ chính xác cho toán học, logic, và các nhiệm vụ suy luận đa bước.

<img src="../../../translated_images/vi/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Suy luận từng bước rõ ràng — chia nhỏ vấn đề phức tạp thành các bước logic cụ thể*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Mô hình cho thấy: 15 - 8 = 7, sau đó 7 + 12 = 19 quả táo
```

**Khi nào dùng:** Bài toán toán học, câu đố logic, gỡ lỗi, hoặc bất kỳ nhiệm vụ nào việc thể hiện quá trình suy luận giúp tăng độ chính xác và sự tin tưởng.

### Role-Based Prompting

Thiết lập vai trò hoặc nhân vật cho AI trước khi đặt câu hỏi. Điều này tạo bối cảnh ảnh hưởng tới tông giọng, độ sâu, và trọng tâm trả lời. Một "kiến trúc sư phần mềm" đưa ra lời khuyên khác với "lập trình viên mới" hay "kiểm toán viên bảo mật".

<img src="../../../translated_images/vi/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Thiết lập bối cảnh và nhân vật — cùng câu hỏi nhưng câu trả lời khác tùy vai trò được giao*

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

**Khi nào dùng:** Đánh giá code, giảng dạy, phân tích theo lĩnh vực, hoặc khi bạn cần câu trả lời phù hợp với mức chuyên môn hoặc góc nhìn cụ thể.

### Prompt Templates

Tạo các prompt tái sử dụng với các chỗ giữ biến. Thay vì viết prompt mới mỗi lần, định nghĩa một mẫu một lần và điền các giá trị khác nhau vào. Lớp `PromptTemplate` của LangChain4j làm việc này dễ dàng với cú pháp `{{variable}}`.

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

**Khi nào dùng:** Truy vấn lặp lại với dữ liệu khác nhau, xử lý theo lô, xây dựng workflow AI tái sử dụng, hoặc bất kỳ trường hợp nào cấu trúc prompt giữ nguyên mà dữ liệu thay đổi.

---

Năm kỹ thuật căn bản này cung cấp cho bạn một bộ công cụ vững chắc cho hầu hết các nhiệm vụ prompt. Phần còn lại của module xây dựng trên đó với **tám mẫu nâng cao** tận dụng khả năng điều khiển suy luận, tự đánh giá, và kết quả có cấu trúc của GPT-5.2.

## Mẫu Nâng Cao

Sau khi đã nắm cơ bản, hãy chuyển sang tám mẫu nâng cao làm cho module này trở nên đặc biệt. Không phải vấn đề nào cũng cần cách tiếp cận giống nhau. Có câu hỏi cần câu trả lời nhanh, có câu lại cần suy nghĩ sâu. Có cái cần suy luận rõ ràng, có cái chỉ cần kết quả. Mỗi mẫu dưới đây tối ưu cho một tình huống khác nhau — và điều khiển suy luận của GPT-5.2 làm cho sự khác biệt này càng rõ nét.

<img src="../../../translated_images/vi/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Tám Mẫu Prompt Engineering" width="800"/>

*Tổng quan tám mẫu kỹ thuật prompt và trường hợp sử dụng*

<img src="../../../translated_images/vi/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Điều Khiển Suy Luận với GPT-5.2" width="800"/>

*Điều khiển suy luận của GPT-5.2 cho phép bạn chỉ định mức độ suy nghĩ của mô hình — từ trả lời nhanh trực tiếp đến khám phá sâu*

**Low Eagerness (Nhanh & Tập Trung)** - Dành cho câu hỏi đơn giản, bạn muốn câu trả lời nhanh và thẳng thắn. Mô hình suy luận tối thiểu - tối đa 2 bước. Dùng cho tính toán, tra cứu, hoặc câu hỏi đơn giản.

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
> - "Sự khác biệt giữa mẫu low eagerness và high eagerness là gì?"
> - "Các thẻ XML trong prompt giúp cấu trúc câu trả lời AI thế nào?"
> - "Khi nào nên dùng mẫu tự phản chiếu so với chỉ dẫn trực tiếp?"

**High Eagerness (Sâu & Kỹ Lưỡng)** - Dành cho các vấn đề phức tạp cần phân tích toàn diện. Mô hình khám phá kỹ càng và trình bày suy luận chi tiết. Dùng cho thiết kế hệ thống, quyết định kiến trúc, hoặc nghiên cứu phức tạp.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Task Execution (Tiến Trình Bước Từng Bước)** - Dành cho quy trình nhiều bước. Mô hình đưa ra kế hoạch trước, trình bày từng bước khi làm, rồi tóm tắt lại. Dùng cho di chuyển dữ liệu, triển khai, hoặc quy trình đa bước.

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

Chain-of-Thought prompting yêu cầu mô hình cho thấy quá trình suy luận, tăng độ chính xác cho các nhiệm vụ phức tạp. Phân tích từng bước giúp cả con người và AI hiểu logic.

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Hỏi về mẫu này:
> - "Làm sao tôi điều chỉnh mẫu task execution cho các thao tác chạy lâu?"
> - "Thực hành tốt nhất khi cấu trúc các tool preambles trong ứng dụng sản xuất là gì?"
> - "Làm sao để tôi ghi lại và hiển thị tiến độ trung gian trên giao diện người dùng?"

<img src="../../../translated_images/vi/task-execution-pattern.9da3967750ab5c1e.webp" alt="Mẫu Task Execution" width="800"/>

*Kế hoạch → Thực thi → Tóm tắt cho quy trình đa bước*

**Self-Reflecting Code** - Dùng để sinh code đạt chuẩn sản xuất. Mô hình tạo code theo tiêu chuẩn sản xuất, xử lý lỗi đúng cách. Dùng khi xây dựng tính năng hoặc dịch vụ mới.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/vi/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Chu Trình Tự Phản Chiếu" width="800"/>

*Chu trình cải tiến lặp - sinh, đánh giá, nhận diện lỗi, cải thiện, lặp lại*

**Structured Analysis** - Dùng để đánh giá nhất quán. Mô hình đánh giá code theo khung cố định (tính đúng, thực hành, hiệu năng, bảo mật, khả năng duy trì). Dùng cho đánh giá code hoặc kiểm tra chất lượng.

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

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Hỏi về phân tích có cấu trúc:
> - "Làm sao tùy chỉnh khung phân tích cho các loại đánh giá code khác nhau?"
> - "Cách tốt nhất để phân tích và xử lý đầu ra có cấu trúc theo trình tự là gì?"
> - "Làm sao đảm bảo mức độ nghiêm trọng nhất quán qua các phiên đánh giá khác nhau?"

<img src="../../../translated_images/vi/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Mẫu Phân Tích Có Cấu Trúc" width="800"/>

*Khung đánh giá code nhất quán với mức độ nghiêm trọng*

**Multi-Turn Chat** - Dùng cho cuộc trò chuyện cần bối cảnh. Mô hình nhớ các tin nhắn trước và xây dựng dựa trên đó. Dùng cho phiên hỗ trợ tương tác hoặc hỏi đáp phức tạp.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/vi/context-memory.dff30ad9fa78832a.webp" alt="Bộ Nhớ Bối Cảnh" width="800"/>

*Cách bối cảnh hội thoại được tích lũy qua nhiều lượt đến giới hạn token*

**Step-by-Step Reasoning** - Dành cho vấn đề cần lôgic rõ ràng. Mô hình trình bày suy luận từng bước cụ thể. Dùng cho bài toán toán học, câu đố logic, hoặc khi bạn cần hiểu quá trình suy nghĩ.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/vi/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Mẫu Từng Bước" width="800"/>

*Phân tách vấn đề thành các bước logic cụ thể*

**Constrained Output** - Dùng cho câu trả lời phải theo định dạng cụ thể. Mô hình tuân thủ nghiêm ngặt quy tắc về định dạng và độ dài. Dùng cho tóm tắt hoặc khi bạn cần cấu trúc đầu ra chính xác.

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

<img src="../../../translated_images/vi/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Mẫu Đầu Ra Có Ràng Buộc" width="800"/>

*Bắt buộc tuân thủ định dạng, độ dài và cấu trúc cụ thể*

## Sử Dụng Tài Nguyên Azure Hiện Có

**Kiểm tra triển khai:**

Đảm bảo file `.env` tồn tại ở thư mục gốc với thông tin xác thực Azure (được tạo trong Module 01):
```bash
cat ../.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Khởi động ứng dụng:**

> **Lưu ý:** Nếu bạn đã khởi động tất cả ứng dụng bằng `./start-all.sh` từ Module 01, module này đã chạy trên cổng 8083. Bạn có thể bỏ qua các lệnh khởi động dưới đây và truy cập trực tiếp http://localhost:8083.

**Tùy chọn 1: Sử dụng Spring Boot Dashboard (Khuyến nghị cho người dùng VS Code)**

Dev container đã bao gồm tiện ích mở rộng Spring Boot Dashboard, cung cấp giao diện trực quan để quản lý tất cả ứng dụng Spring Boot. Bạn có thể tìm thấy nó trên Thanh Hoạt Động bên trái của VS Code (tìm biểu tượng Spring Boot).

Từ Spring Boot Dashboard, bạn có thể:
- Xem tất cả các ứng dụng Spring Boot có trong workspace
- Khởi động/dừng ứng dụng chỉ với một cú nhấp chuột
- Xem nhật ký ứng dụng trong thời gian thực
- Giám sát trạng thái ứng dụng
Chỉ cần nhấn nút phát bên cạnh "prompt-engineering" để bắt đầu mô-đun này, hoặc khởi động tất cả các mô-đun cùng lúc.

<img src="../../../translated_images/vi/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Lựa chọn 2: Sử dụng shell scripts**

Khởi động tất cả các ứng dụng web (mô-đun 01-04):

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

Hoặc chỉ khởi động mô-đun này:

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

Cả hai script tự động tải biến môi trường từ tệp `.env` gốc và sẽ xây dựng các file JAR nếu chúng chưa tồn tại.

> **Lưu ý:** Nếu bạn muốn tự xây dựng tất cả các mô-đun trước khi khởi động:
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
cd .. && ./stop-all.sh  # Tất cả các mô-đun
```

**PowerShell:**
```powershell
.\stop.ps1  # Chỉ mô-đun này
# Hoặc
cd ..; .\stop-all.ps1  # Tất cả các mô-đun
```

## Ảnh chụp màn hình ứng dụng

<img src="../../../translated_images/vi/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Bảng điều khiển chính hiển thị 8 mẫu kỹ thuật prompt với đặc điểm và trường hợp sử dụng*

## Khám phá các mẫu

Giao diện web cho phép bạn thử nghiệm với các chiến lược yêu cầu khác nhau. Mỗi mẫu giải quyết các vấn đề khác nhau - hãy thử để thấy khi nào mỗi cách tiếp cận phát huy hiệu quả.

### Eagerness Thấp vs Cao

Hỏi một câu đơn giản như "15% của 200 là bao nhiêu?" sử dụng Eagerness Thấp. Bạn sẽ nhận được câu trả lời trực tiếp ngay lập tức. Bây giờ hãy hỏi thứ phức tạp như "Thiết kế chiến lược caching cho một API có lưu lượng cao" sử dụng Eagerness Cao. Quan sát cách mô hình chậm lại và cung cấp lý giải chi tiết. Cùng một mô hình, cùng cấu trúc câu hỏi - nhưng lời nhắc cho biết nó phải suy nghĩ nhiều hay ít.

<img src="../../../translated_images/vi/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Tính toán nhanh với lý luận tối thiểu*

<img src="../../../translated_images/vi/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Chiến lược caching toàn diện (2.8MB)*

### Thực thi nhiệm vụ (Tool Preambles)

Các quy trình nhiều bước được lợi từ việc lập kế hoạch trước và tường thuật tiến trình. Mô hình phác thảo những gì nó sẽ làm, tường thuật từng bước, sau đó tóm tắt kết quả.

<img src="../../../translated_images/vi/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Tạo một điểm cuối REST với tường thuật từng bước (3.9MB)*

### Mã tự phản chiếu

Thử "Tạo dịch vụ xác thực email". Thay vì chỉ tạo mã và dừng lại, mô hình tạo, đánh giá theo tiêu chí chất lượng, nhận diện điểm yếu và cải thiện. Bạn sẽ thấy nó lặp lại cho đến khi mã đạt tiêu chuẩn sản xuất.

<img src="../../../translated_images/vi/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Dịch vụ xác thực email hoàn chỉnh (5.2MB)*

### Phân tích có cấu trúc

Kiểm tra mã cần các khung đánh giá nhất quán. Mô hình phân tích mã theo các hạng mục cố định (đúng đắn, thực hành, hiệu năng, bảo mật) với các mức độ nghiêm trọng.

<img src="../../../translated_images/vi/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Đánh giá mã dựa trên khung phân tích*

### Chat Đa vòng

Hỏi "Spring Boot là gì?" rồi ngay sau đó hỏi tiếp "Cho tôi một ví dụ". Mô hình nhớ câu hỏi đầu tiên và trả về ví dụ Spring Boot chính xác cho bạn. Nếu không có bộ nhớ, câu hỏi thứ hai sẽ quá mơ hồ.

<img src="../../../translated_images/vi/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Bảo tồn ngữ cảnh qua các câu hỏi*

### Lý luận từng bước

Chọn một bài toán toán và thử với cả Lý luận từng bước và Eagerness Thấp. Eagerness thấp chỉ cung cấp câu trả lời - nhanh nhưng không rõ nét. Lý luận từng bước cho bạn thấy từng phép tính và quyết định.

<img src="../../../translated_images/vi/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Bài toán với các bước rõ ràng*

### Đầu ra có giới hạn

Khi bạn cần định dạng hoặc số từ cụ thể, mẫu này bắt buộc phải tuân thủ nghiêm ngặt. Thử tạo bản tóm tắt đúng 100 từ với định dạng gạch đầu dòng.

<img src="../../../translated_images/vi/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Tóm tắt học máy với kiểm soát định dạng*

## Điều bạn thực sự học được

**Nỗ lực lý luận thay đổi mọi thứ**

GPT-5.2 cho phép bạn kiểm soát nỗ lực tính toán qua các lời nhắc. Nỗ lực thấp có nghĩa phản hồi nhanh với khám phá tối thiểu. Nỗ lực cao đồng nghĩa mô hình dành thời gian suy nghĩ sâu sắc. Bạn học cách cân bằng nỗ lực với độ phức tạp nhiệm vụ - đừng phí thời gian cho câu hỏi đơn giản, nhưng cũng đừng vội vàng với quyết định phức tạp.

**Cấu trúc dẫn dắt hành vi**

Bạn có nhận ra các thẻ XML trong lời nhắc không? Chúng không phải để trang trí. Mô hình tuân theo các hướng dẫn có cấu trúc đáng tin cậy hơn so với văn bản tự do. Khi bạn cần quy trình đa bước hoặc logic phức tạp, cấu trúc giúp mô hình theo dõi vị trí và bước tiếp theo.

<img src="../../../translated_images/vi/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Cấu trúc lời nhắc tốt với các phần rõ ràng và tổ chức theo kiểu XML*

**Chất lượng qua tự đánh giá**

Các mẫu tự phản chiếu hoạt động bằng cách làm rõ các tiêu chí chất lượng. Thay vì hy vọng mô hình "làm đúng", bạn nói rõ "đúng" nghĩa là gì: logic chính xác, xử lý lỗi, hiệu năng, bảo mật. Mô hình sau đó đánh giá và cải thiện đầu ra của chính nó. Điều này biến việc tạo mã từ một vé số thành một quy trình.

**Ngữ cảnh là có giới hạn**

Các cuộc hội thoại đa vòng hoạt động bằng cách bao gồm lịch sử tin nhắn với mỗi yêu cầu. Nhưng có giới hạn - mỗi mô hình có số lượng token tối đa. Khi cuộc trò chuyện tăng, bạn cần chiến lược giữ ngữ cảnh liên quan mà không vượt quá giới hạn đó. Mô-đun này cho bạn thấy cách bộ nhớ hoạt động; sau này bạn sẽ học được khi nào cần tóm tắt, khi nào quên và khi nào truy xuất.

## Bước tiếp theo

**Mô-đun tiếp theo:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Điều hướng:** [← Trước: Mô-đun 01 - Giới thiệu](../01-introduction/README.md) | [Quay lại Chính](../README.md) | [Tiếp: Mô-đun 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố miễn trừ trách nhiệm**:  
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng các bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ nguyên bản của nó mới là nguồn đáng tin cậy nhất. Đối với những thông tin quan trọng, nên sử dụng dịch vụ dịch thuật chuyên nghiệp của con người. Chúng tôi không chịu trách nhiệm về bất kỳ sự hiểu lầm hoặc giải thích sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->