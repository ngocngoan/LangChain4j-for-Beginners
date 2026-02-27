# Module 02: Kỹ Thuật Lập Prompt với GPT-5.2

## Mục Lục

- [Video Hướng Dẫn](../../../02-prompt-engineering)
- [Những Gì Bạn Sẽ Học](../../../02-prompt-engineering)
- [Yêu Cầu Tiền Đề](../../../02-prompt-engineering)
- [Hiểu Về Kỹ Thuật Lập Prompt](../../../02-prompt-engineering)
- [Cơ Bản Về Kỹ Thuật Lập Prompt](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Các Mẫu Nâng Cao](../../../02-prompt-engineering)
- [Sử Dụng Tài Nguyên Azure Có Sẵn](../../../02-prompt-engineering)
- [Ảnh Chụp Ứng Dụng](../../../02-prompt-engineering)
- [Khám Phá Các Mẫu](../../../02-prompt-engineering)
  - [Nhiệt Tình Thấp vs Cao](../../../02-prompt-engineering)
  - [Thực Thi Tác Vụ (Phần Mở Đầu Công Cụ)](../../../02-prompt-engineering)
  - [Mã Tự Phản Chiếu](../../../02-prompt-engineering)
  - [Phân Tích Có Cấu Trúc](../../../02-prompt-engineering)
  - [Chat Đa Vòng](../../../02-prompt-engineering)
  - [Lý Luận Từng Bước Một](../../../02-prompt-engineering)
  - [Đầu Ra Có Giới Hạn](../../../02-prompt-engineering)
- [Bạn Thực Sự Đang Học Gì](../../../02-prompt-engineering)
- [Bước Tiếp Theo](../../../02-prompt-engineering)

## Video Hướng Dẫn

Xem buổi trực tiếp giải thích cách bắt đầu với module này: [Prompt Engineering with LangChain4j - Live Session](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## Những Gì Bạn Sẽ Học

<img src="../../../translated_images/vi/what-youll-learn.c68269ac048503b2.webp" alt="Những Gì Bạn Sẽ Học" width="800"/>

Trong module trước, bạn đã thấy cách bộ nhớ cho phép AI hội thoại hoạt động và sử dụng các Mô hình GitHub cho các tương tác cơ bản. Bây giờ chúng ta sẽ tập trung vào cách bạn đặt câu hỏi — chính các prompt — sử dụng GPT-5.2 của Azure OpenAI. Cách bạn cấu trúc prompt ảnh hưởng mạnh mẽ đến chất lượng câu trả lời nhận được. Chúng ta bắt đầu với việc xem lại các kỹ thuật lập prompt cơ bản, rồi chuyển sang tám mẫu nâng cao tận dụng đầy đủ khả năng của GPT-5.2.

Chúng ta sẽ dùng GPT-5.2 vì nó giới thiệu khả năng kiểm soát suy luận — bạn có thể chỉ dẫn mô hình phải suy nghĩ bao lâu trước khi trả lời. Điều này làm rõ hơn các chiến lược lập prompt khác nhau và giúp bạn hiểu khi nào dùng mỗi cách. Chúng ta cũng được lợi khi Azure hạn chế tần suất cho GPT-5.2 thấp hơn so với các Mô hình GitHub.

## Yêu Cầu Tiền Đề

- Hoàn thành Module 01 (đã triển khai tài nguyên Azure OpenAI)
- Có file `.env` ở thư mục gốc với thông tin xác thực Azure (được tạo bởi `azd up` ở Module 01)

> **Lưu ý:** Nếu bạn chưa hoàn thành Module 01, hãy làm theo hướng dẫn triển khai ở đó trước.

## Hiểu Về Kỹ Thuật Lập Prompt

<img src="../../../translated_images/vi/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Kỹ Thuật Lập Prompt Là Gì?" width="800"/>

Kỹ thuật lập prompt là thiết kế văn bản đầu vào sao cho luôn nhận được kết quả bạn cần. Nó không chỉ là đặt câu hỏi — mà còn là cấu trúc yêu cầu sao cho mô hình hiểu chính xác bạn muốn gì và cách thức đáp ứng.

Hãy nghĩ như bạn đang đưa chỉ dẫn cho đồng nghiệp. "Sửa lỗi" thì chung chung. "Sửa lỗi null pointer exception trong UserService.java dòng 45 bằng cách thêm kiểm tra null" thì cụ thể. Các mô hình ngôn ngữ cũng vậy — tính cụ thể và cấu trúc rất quan trọng.

<img src="../../../translated_images/vi/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j Đóng Vai Trò Gì" width="800"/>

LangChain4j cung cấp hạ tầng — kết nối mô hình, bộ nhớ, và các loại thông điệp — trong khi các mẫu prompt chỉ là văn bản được cấu trúc kỹ bạn gửi qua hạ tầng đó. Các khối xây dựng chính là `SystemMessage` (xác định hành vi và vai trò AI) và `UserMessage` (chứa yêu cầu thực tế của bạn).

## Cơ Bản Về Kỹ Thuật Lập Prompt

<img src="../../../translated_images/vi/five-patterns-overview.160f35045ffd2a94.webp" alt="Tổng Quan Năm Mẫu Kỹ Thuật Lập Prompt" width="800"/>

Trước khi đi sâu vào các mẫu nâng cao trong module này, hãy xem lại năm kỹ thuật lập prompt cơ bản. Đây là nền tảng mà mọi kỹ sư lập prompt cần biết. Nếu bạn đã làm qua module [Quick Start](../00-quick-start/README.md#2-prompt-patterns), bạn đã thấy chúng trong thực tế — đây là khung khái niệm phía sau.

### Zero-Shot Prompting

Cách đơn giản nhất: đưa cho mô hình chỉ dẫn trực tiếp mà không có ví dụ. Mô hình hoàn toàn dựa trên đào tạo để hiểu và thực thi tác vụ. Cách này hiệu quả với các yêu cầu đơn giản, nơi hành vi mong đợi rõ ràng.

<img src="../../../translated_images/vi/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Chỉ dẫn trực tiếp không có ví dụ — mô hình suy luận tác vụ từ chỉ dẫn thôi*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Phản hồi: "Tích cực"
```

**Khi dùng:** Phân loại đơn giản, câu hỏi trực tiếp, dịch thuật, hoặc bất kỳ tác vụ nào mô hình có thể xử lý mà không cần hướng dẫn thêm.

### Few-Shot Prompting

Cung cấp ví dụ minh họa mẫu bạn muốn mô hình theo. Mô hình học định dạng đầu vào-đầu ra từ ví dụ của bạn và áp dụng cho đầu vào mới. Cách này cải thiện đáng kể sự nhất quán với các tác vụ mà định dạng hoặc hành vi mong muốn không rõ ràng.

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

**Khi dùng:** Phân loại tùy chỉnh, định dạng nhất quán, tác vụ chuyên ngành, hoặc khi kết quả zero-shot không ổn định.

### Chain of Thought

Yêu cầu mô hình thể hiện suy luận từng bước. Thay vì nhảy ngay tới câu trả lời, mô hình phân tích vấn đề và làm việc từng phần rõ ràng. Cách này nâng cao độ chính xác cho toán học, logic, và các tác vụ suy luận đa bước.

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

**Khi dùng:** Bài toán toán học, câu đố logic, gỡ lỗi, hoặc bất kỳ tác vụ nào mà việc trình bày quá trình suy luận giúp cải thiện độ chính xác và tin cậy.

### Role-Based Prompting

Đặt một nhân vật hoặc vai trò cho AI trước khi đặt câu hỏi. Điều này tạo ngữ cảnh ảnh hưởng đến cách trả lời về giọng điệu, chiều sâu và trọng tâm. Một "kiến trúc sư phần mềm" sẽ cho lời khuyên khác với "lập trình viên mới" hoặc "kiểm toán viên bảo mật".

<img src="../../../translated_images/vi/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Đặt ngữ cảnh và nhân vật — cùng câu hỏi nhưng nhận câu trả lời khác nhau tùy vai trò*

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

**Khi dùng:** Đánh giá mã, giảng dạy, phân tích chuyên ngành, hoặc khi bạn cần câu trả lời phù hợp với trình độ chuyên môn hoặc góc nhìn cụ thể.

### Prompt Templates

Tạo prompt tái sử dụng với các chỗ giữ biến. Thay vì viết prompt mới mỗi lần, định nghĩa một mẫu một lần rồi điền các giá trị khác nhau. Lớp `PromptTemplate` của LangChain4j giúp làm điều này dễ dàng với cú pháp `{{variable}}`.

<img src="../../../translated_images/vi/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompt tái sử dụng với chỗ giữ biến — một mẫu dùng nhiều lần*

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

**Khi dùng:** Các truy vấn lặp lại với đầu vào khác nhau, xử lý hàng loạt, xây dựng luồng công việc AI tái sử dụng, hoặc bất kỳ tình huống nào cấu trúc prompt giống nhau nhưng dữ liệu thay đổi.

---

Năm kỹ thuật cơ bản này cung cấp cho bạn bộ công cụ chắc chắn cho hầu hết tác vụ lập prompt. Phần còn lại của module xây dựng dựa trên đó với **tám mẫu nâng cao** tận dụng kiểm soát suy luận, tự đánh giá, và khả năng đầu ra có cấu trúc của GPT-5.2.

## Các Mẫu Nâng Cao

Sau khi nắm vững cơ bản, hãy chuyển sang tám mẫu nâng cao làm module này trở nên đặc biệt. Không phải mọi vấn đề đều cần cùng một cách tiếp cận. Có câu hỏi cần trả lời nhanh, có câu cần suy nghĩ sâu. Có câu cần suy luận rõ ràng, có câu chỉ cần kết quả. Mỗi mẫu dưới đây tối ưu cho một kịch bản khác — và khả năng kiểm soát suy luận của GPT-5.2 làm phân biệt các cách này rõ ràng hơn.

<img src="../../../translated_images/vi/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Tám Mẫu Lập Prompt" width="800"/>

*Tổng quan tám mẫu kỹ thuật lập prompt và các trường hợp sử dụng*

<img src="../../../translated_images/vi/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kiểm Soát Suy Luận với GPT-5.2" width="800"/>

*Khả năng kiểm soát suy luận của GPT-5.2 cho phép bạn chỉ định mức độ suy nghĩ của mô hình — từ câu trả lời nhanh trực tiếp đến phân tích sâu sắc*

**Nhiệt Tình Thấp (Nhanh & Tập Trung)** - Dùng cho các câu hỏi đơn giản cần câu trả lời nhanh, trực tiếp. Mô hình suy luận tối thiểu — tối đa 2 bước. Dùng cho tính toán, tra cứu, hoặc câu hỏi rõ ràng.

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

> 💡 **Thám hiểm với GitHub Copilot:** Mở [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) và hỏi:
> - "Sự khác biệt giữa mẫu nhiệt tình thấp và nhiệt tình cao là gì?"
> - "Các thẻ XML trong prompt giúp cấu trúc phản hồi AI thế nào?"
> - "Khi nào nên dùng mẫu tự phản chiếu so với chỉ dẫn trực tiếp?"

**Nhiệt Tình Cao (Sâu & Tỉ Mỉ)** - Dùng cho các vấn đề phức tạp cần phân tích toàn diện. Mô hình khám phá kỹ lưỡng và trình bày suy luận chi tiết. Dùng cho thiết kế hệ thống, quyết định kiến trúc, hoặc nghiên cứu phức tạp.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Thực Thi Tác Vụ (Tiến Trình Từng Bước)** - Dùng cho các luồng công việc đa bước. Mô hình cung cấp kế hoạch trước, kể từng bước khi làm, rồi tóm tắt. Dùng cho di cư, triển khai, hoặc bất kỳ quy trình đa bước nào.

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

Mẫu Chain-of-Thought yêu cầu mô hình thể hiện quá trình suy luận, cải thiện độ chính xác cho các tác vụ phức tạp. Việc phân tách từng bước giúp cả người và AI hiểu logic.

> **🤖 Thử với Chat [GitHub Copilot](https://github.com/features/copilot):** Hỏi về mẫu này:
> - "Làm thế nào để điều chỉnh mẫu thực thi tác vụ cho các hoạt động chạy lâu?"
> - "Các thực hành tốt nhất để cấu trúc phần mở đầu công cụ trong ứng dụng sản xuất là gì?"
> - "Làm sao để thu thập và hiển thị cập nhật tiến trình trung gian trong giao diện người dùng?"

<img src="../../../translated_images/vi/task-execution-pattern.9da3967750ab5c1e.webp" alt="Mẫu Thực Thi Tác Vụ" width="800"/>

*Kế hoạch → Thực thi → Tóm tắt cho các tác vụ đa bước*

**Mã Tự Phản Chiếu** - Dùng để tạo mã chất lượng sản xuất. Mô hình sinh mã theo tiêu chuẩn sản xuất với xử lý lỗi đúng cách. Dùng khi xây dựng tính năng hoặc dịch vụ mới.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/vi/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Chu Kỳ Tự Phản Chiếu" width="800"/>

*Vòng lặp cải tiến lặp đi lặp lại - sinh, đánh giá, nhận diện vấn đề, cải thiện, lặp lại*

**Phân Tích Có Cấu Trúc** - Dùng để đánh giá nhất quán. Mô hình xem xét mã theo khung cố định (độ chính xác, thực hành, hiệu suất, bảo mật, khả năng duy trì). Dùng cho review mã hoặc đánh giá chất lượng.

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
> - "Cách tốt nhất để phân tích và xử lý đầu ra có cấu trúc bằng lập trình là gì?"
> - "Làm thế nào đảm bảo mức độ nghiêm trọng nhất quán qua các phiên đánh giá?"

<img src="../../../translated_images/vi/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Mẫu Phân Tích Có Cấu Trúc" width="800"/>

*Khung để đánh giá mã nhất quán với các mức độ nghiêm trọng*

**Chat Đa Vòng** - Dùng cho các cuộc hội thoại cần ngữ cảnh. Mô hình nhớ các tin nhắn trước đó và xây dựng phản hồi dựa trên chúng. Dùng cho phiên trợ giúp tương tác hoặc hỏi đáp phức tạp.

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

*Cách ngữ cảnh hội thoại tích lũy qua nhiều vòng cho tới khi đủ token*

**Lý Luận Từng Bước Một** - Dùng cho các vấn đề cần lý luận rõ ràng. Mô hình thể hiện suy luận cụ thể từng bước. Dùng cho toán học, câu đố logic, hoặc khi bạn cần hiểu quá trình suy nghĩ.

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

**Đầu Ra Có Giới Hạn** - Dành cho các câu trả lời yêu cầu định dạng cụ thể. Mô hình tuân thủ nghiêm ngặt các quy tắc về định dạng và độ dài. Dùng cho tóm tắt hoặc khi cần cấu trúc đầu ra chính xác.

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

<img src="../../../translated_images/vi/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Mẫu Đầu Ra Có Giới Hạn" width="800"/>

*Bắt buộc các yêu cầu về định dạng, độ dài, và cấu trúc cụ thể*

## Sử Dụng Tài Nguyên Azure Có Sẵn

**Kiểm tra triển khai:**

Đảm bảo file `.env` tồn tại ở thư mục gốc với thông tin xác thực Azure (được tạo trong Module 01):
```bash
cat ../.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Khởi chạy ứng dụng:**

> **Lưu ý:** Nếu bạn đã khởi chạy tất cả ứng dụng bằng `./start-all.sh` từ Module 01, module này đang chạy trên cổng 8083. Bạn có thể bỏ qua các lệnh khởi chạy dưới đây và truy cập trực tiếp http://localhost:8083.

**Tùy chọn 1: Sử dụng Spring Boot Dashboard (Khuyến nghị cho người dùng VS Code)**
Dev container bao gồm tiện ích mở rộng Spring Boot Dashboard, cung cấp giao diện trực quan để quản lý tất cả các ứng dụng Spring Boot. Bạn có thể tìm thấy nó ở Thanh Hoạt Động bên trái của VS Code (tìm biểu tượng Spring Boot).

Từ Spring Boot Dashboard, bạn có thể:
- Xem tất cả các ứng dụng Spring Boot có trong workspace
- Khởi động/dừng ứng dụng chỉ với một lần nhấp
- Xem nhật ký ứng dụng theo thời gian thực
- Giám sát trạng thái ứng dụng

Chỉ cần nhấn nút chơi bên cạnh "prompt-engineering" để khởi chạy module này, hoặc khởi chạy tất cả các module cùng lúc.

<img src="../../../translated_images/vi/dashboard.da2c2130c904aaf0.webp" alt="Bảng điều khiển Spring Boot" width="400"/>

**Lựa chọn 2: Sử dụng các script shell**

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

Cả hai script tự động nạp các biến môi trường từ file `.env` trong thư mục gốc và sẽ build các file JAR nếu chưa tồn tại.

> **Lưu ý:** Nếu bạn muốn build tất cả các module thủ công trước khi khởi chạy:
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
.\stop.ps1  # Chỉ mô-đun này
# Hoặc
cd ..; .\stop-all.ps1  # Tất cả các mô-đun
```

## Ảnh chụp màn hình ứng dụng

<img src="../../../translated_images/vi/dashboard-home.5444dbda4bc1f79d.webp" alt="Trang chủ Dashboard" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Dashboard chính hiển thị 8 mẫu kỹ thuật prompt với các đặc điểm và trường hợp sử dụng*

## Khám phá các mẫu

Giao diện web cho phép bạn thử nghiệm với các chiến lược prompt khác nhau. Mỗi mẫu giải quyết các vấn đề khác nhau - hãy thử để xem cách nào phù hợp nhất.

> **Lưu ý: Streaming vs Non-Streaming** — Mỗi trang mẫu đều có hai nút: **🔴 Stream Response (Live)** và tùy chọn **Non-streaming**. Streaming sử dụng Server-Sent Events (SSE) để hiển thị token theo thời gian thực khi mô hình tạo ra chúng, giúp bạn thấy tiến trình ngay lập tức. Tùy chọn non-streaming sẽ đợi phản hồi hoàn chỉnh mới hiển thị. Với các prompt đòi hỏi suy nghĩ sâu (ví dụ: High Eagerness, Self-Reflecting Code), gọi non-streaming có thể mất rất nhiều thời gian — đôi khi vài phút — mà không có phản hồi hiển thị. **Dùng streaming khi thử nghiệm với các prompt phức tạp** để bạn có thể thấy mô hình hoạt động và tránh cảm giác yêu cầu bị hết thời gian chờ.
>
> **Lưu ý: Yêu cầu trình duyệt** — Tính năng streaming sử dụng Fetch Streams API (`response.body.getReader()`) cần trình duyệt đầy đủ (Chrome, Edge, Firefox, Safari). Nó **không hoạt động** trong Simple Browser tích hợp của VS Code, vì webview của nó không hỗ trợ ReadableStream API. Nếu bạn dùng Simple Browser, nút non-streaming vẫn hoạt động bình thường — chỉ nút streaming bị ảnh hưởng. Mở `http://localhost:8083` trong trình duyệt ngoài để có trải nghiệm đầy đủ.

### Low vs High Eagerness

Hỏi một câu đơn giản như "15% của 200 là bao nhiêu?" với Low Eagerness. Bạn sẽ nhận được câu trả lời nhanh chóng, trực tiếp. Bây giờ hỏi một câu phức tạp như "Thiết kế chiến lược cache cho một API lưu lượng cao" với High Eagerness. Nhấn **🔴 Stream Response (Live)** và xem mô hình lý giải chi tiết từng token một. Cùng một mô hình, cùng cấu trúc câu hỏi - nhưng prompt cho biết mức độ suy nghĩ cần thiết.

### Thực thi tác vụ (Tool Preambles)

Các workflow nhiều bước cần được lên kế hoạch trước và diễn giải tiến trình. Mô hình sẽ phác thảo những gì nó sẽ làm, kể lại từng bước, rồi tóm tắt kết quả.

### Code tự phản chiếu

Thử "Tạo dịch vụ kiểm tra email". Thay vì chỉ tạo code và dừng lại, mô hình tạo ra, đánh giá theo tiêu chí chất lượng, nhận diện điểm yếu và cải tiến. Bạn sẽ thấy nó lặp đi lặp lại cho đến khi code đạt chuẩn sản xuất.

### Phân tích có cấu trúc

Xem xét code cần các khung đánh giá thống nhất. Mô hình phân tích code dựa trên các danh mục cố định (độ chính xác, thực hành, hiệu năng, bảo mật) với các mức độ nghiêm trọng.

### Chat đa lượt

Hỏi "Spring Boot là gì?" rồi ngay lập tức hỏi tiếp "Cho tôi ví dụ". Mô hình nhớ câu hỏi đầu và đưa ra ví dụ cụ thể về Spring Boot. Nếu không có bộ nhớ, câu hỏi thứ hai sẽ quá mơ hồ.

### Lý luận từng bước

Chọn một bài toán toán và thử với cả Step-by-Step Reasoning và Low Eagerness. Low eagerness chỉ cho bạn đáp án - nhanh nhưng không rõ ràng. Step-by-step thể hiện từng phép tính và quyết định.

### Đầu ra có giới hạn

Khi bạn cần định dạng hay số lượng từ cụ thể, mẫu này đảm bảo tuân thủ nghiêm ngặt. Thử tạo tóm tắt đúng 100 từ theo dạng gạch đầu dòng.

## Những gì bạn thật sự đang học

**Nỗ lực lý luận thay đổi mọi thứ**

GPT-5.2 cho phép bạn kiểm soát mức độ tính toán qua prompt. Nỗ lực thấp có nghĩa phản hồi nhanh với ít tìm kiếm. Nỗ lực cao cho phép mô hình suy nghĩ sâu hơn. Bạn đang học cách điều chỉnh nỗ lực theo độ phức tạp nhiệm vụ - đừng lãng phí thời gian hỏi những câu đơn giản, nhưng cũng đừng vội vàng khi xử lý quyết định phức tạp.

**Cấu trúc hướng dẫn hành vi**

Bạn có thấy các thẻ XML trong prompt không? Chúng không chỉ để trang trí. Mô hình tuân theo hướng dẫn có cấu trúc đáng tin cậy hơn so với văn bản tự do. Khi cần quy trình nhiều bước hoặc logic phức tạp, cấu trúc giúp mô hình theo dõi vị trí và bước tiếp theo.

<img src="../../../translated_images/vi/prompt-structure.a77763d63f4e2f89.webp" alt="Cấu trúc Prompt" width="800"/>

*Cấu trúc một prompt tốt với các phần rõ ràng và tổ chức theo kiểu XML*

**Chất lượng qua tự đánh giá**

Các mẫu tự phản chiếu làm rõ tiêu chí chất lượng. Thay vì hy vọng mô hình "làm đúng", bạn nói rõ "đúng" nghĩa là gì: logic chính xác, xử lý lỗi, hiệu năng, bảo mật. Mô hình có thể tự đánh giá kết quả và cải tiến. Điều này biến việc tạo code từ sổ số thành quá trình kiểm soát.

**Bối cảnh có giới hạn**

Hội thoại đa lượt hoạt động bằng cách bao gồm lịch sử tin nhắn với mỗi yêu cầu. Nhưng có giới hạn - mỗi mô hình có số token tối đa. Khi hội thoại kéo dài, bạn cần chiến lược giữ lại ngữ cảnh quan trọng mà không vượt quá giới hạn. Module này chỉ bạn cách bộ nhớ hoạt động; sẽ học cách tóm tắt, quên và truy xuất sau.

## Bước tiếp theo

**Module kế tiếp:** [03-rag - RAG (Tạo nội dung tăng cường truy xuất)](../03-rag/README.md)

---

**Điều hướng:** [← Trước: Module 01 - Giới thiệu](../01-introduction/README.md) | [Quay lại Trang chính](../README.md) | [Tiếp: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố từ chối trách nhiệm**:  
Tài liệu này đã được dịch sử dụng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ gốc của nó nên được xem là nguồn chính xác và đáng tin cậy. Đối với thông tin quan trọng, nên sử dụng dịch vụ dịch thuật chuyên nghiệp do con người thực hiện. Chúng tôi không chịu trách nhiệm đối với bất kỳ sự hiểu nhầm hoặc diễn giải sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->