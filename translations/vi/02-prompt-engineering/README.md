# Module 02: Kỹ Thuật Tạo Prompt với GPT-5.2

## Mục Lục

- [Video Hướng Dẫn](../../../02-prompt-engineering)
- [Bạn Sẽ Học Gì](../../../02-prompt-engineering)
- [Yêu Cầu Tiền Đề](../../../02-prompt-engineering)
- [Hiểu Về Kỹ Thuật Tạo Prompt](../../../02-prompt-engineering)
- [Các Nguyên Tắc Cơ Bản của Kỹ Thuật Tạo Prompt](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Các Mẫu Nâng Cao](../../../02-prompt-engineering)
- [Chạy Ứng Dụng](../../../02-prompt-engineering)
- [Ảnh Chụp Màn Hình Ứng Dụng](../../../02-prompt-engineering)
- [Khám Phá Các Mẫu](../../../02-prompt-engineering)
  - [Nhiệt Tình Thấp và Cao](../../../02-prompt-engineering)
  - [Thực Thi Nhiệm Vụ (Phần Mở Đầu Công Cụ)](../../../02-prompt-engineering)
  - [Mã Tự Đánh Giá](../../../02-prompt-engineering)
  - [Phân Tích Cấu Trúc](../../../02-prompt-engineering)
  - [Trò Chuyện Nhiều Vòng](../../../02-prompt-engineering)
  - [Lý Luận Từng Bước Một](../../../02-prompt-engineering)
  - [Đầu Ra Có Ràng Buộc](../../../02-prompt-engineering)
- [Bạn Thực Sự Đang Học Gì](../../../02-prompt-engineering)
- [Bước Tiếp Theo](../../../02-prompt-engineering)

## Video Hướng Dẫn

Xem buổi trực tiếp này giải thích cách bắt đầu với module này:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Bạn Sẽ Học Gì

Sơ đồ dưới đây cung cấp cái nhìn tổng quan về các chủ đề chính và kỹ năng bạn sẽ phát triển trong module này — từ các kỹ thuật tinh chỉnh prompt đến quy trình làm việc từng bước bạn sẽ theo dõi.

<img src="../../../translated_images/vi/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

Trong các module trước, bạn đã khám phá các tương tác cơ bản với LangChain4j cùng các Mô hình GitHub và thấy cách bộ nhớ cho phép AI hội thoại với Azure OpenAI. Bây giờ chúng ta sẽ tập trung vào cách bạn đặt câu hỏi — chính các prompt — sử dụng GPT-5.2 của Azure OpenAI. Cách bạn cấu trúc prompt ảnh hưởng mạnh mẽ đến chất lượng phản hồi bạn nhận được. Chúng ta bắt đầu với việc ôn lại các kỹ thuật tạo prompt cơ bản, sau đó tiến đến tám mẫu nâng cao tận dụng tối đa khả năng của GPT-5.2.

Chúng ta sẽ dùng GPT-5.2 vì nó giới thiệu tính năng điều khiển suy luận — bạn có thể chỉ định cho mô hình biết mức độ suy nghĩ trước khi trả lời. Điều này làm các chiến lược prompt khác nhau hiện rõ hơn và giúp bạn hiểu khi nào nên dùng mỗi cách. Chúng ta cũng sẽ tận dụng lợi thế của Azure với ít giới hạn về tốc độ hơn cho GPT-5.2 so với Mô hình GitHub.

## Yêu Cầu Tiền Đề

- Đã hoàn thành Module 01 (đã triển khai tài nguyên Azure OpenAI)
- File `.env` trong thư mục gốc chứa thông tin xác thực Azure (được tạo bởi `azd up` trong Module 01)

> **Lưu ý:** Nếu bạn chưa hoàn thành Module 01, hãy làm theo hướng dẫn triển khai ở đó trước.

## Hiểu Về Kỹ Thuật Tạo Prompt

Về cơ bản, kỹ thuật tạo prompt là sự khác biệt giữa hướng dẫn mơ hồ và hướng dẫn chính xác, như so sánh dưới đây minh họa.

<img src="../../../translated_images/vi/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Kỹ thuật tạo prompt là thiết kế văn bản đầu vào sao cho bạn luôn nhận được kết quả mình cần. Nó không chỉ đơn thuần là đặt câu hỏi - mà còn là cấu trúc yêu cầu sao cho mô hình hiểu chính xác bạn muốn gì và cách để đáp ứng.

Hãy nghĩ giống như đưa hướng dẫn cho đồng nghiệp. "Sửa lỗi" thì mơ hồ. "Sửa lỗi null pointer exception trong UserService.java dòng 45 bằng cách thêm kiểm tra null" thì cụ thể. Các mô hình ngôn ngữ cũng tương tự - sự cụ thể và cấu trúc rất quan trọng.

Sơ đồ dưới đây cho thấy cách LangChain4j đóng vai trò trong bức tranh này — kết nối các mẫu prompt của bạn với mô hình thông qua các thành phần xây dựng SystemMessage và UserMessage.

<img src="../../../translated_images/vi/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j cung cấp cơ sở hạ tầng — kết nối mô hình, bộ nhớ và các loại tin nhắn — trong khi các mẫu prompt chỉ là văn bản có cấu trúc cẩn thận bạn gửi qua cơ sở hạ tầng đó. Các thành phần then chốt là `SystemMessage` (đặt hành vi và vai trò AI) và `UserMessage` (chứa yêu cầu thực tế của bạn).

## Các Nguyên Tắc Cơ Bản của Kỹ Thuật Tạo Prompt

Năm kỹ thuật cốt lõi dưới đây tạo nền tảng cho kỹ thuật tạo prompt hiệu quả. Mỗi kỹ thuật giải quyết một khía cạnh khác nhau trong cách bạn giao tiếp với các mô hình ngôn ngữ.

<img src="../../../translated_images/vi/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Trước khi đi sâu vào các mẫu nâng cao trong module này, chúng ta hãy ôn lại năm kỹ thuật tạo prompt cơ bản. Đây là những khối xây dựng mà mọi kỹ sư prompt đều nên biết. Nếu bạn đã làm qua [module Khởi Đầu Nhanh](../00-quick-start/README.md#2-prompt-patterns), bạn đã thấy chúng hoạt động — đây là khuôn khổ khái niệm đằng sau chúng.

### Zero-Shot Prompting

Cách tiếp cận đơn giản nhất: đưa cho mô hình một hướng dẫn trực tiếp mà không có ví dụ. Mô hình dựa hoàn toàn vào việc huấn luyện để hiểu và thực hiện nhiệm vụ. Cách này hiệu quả với các yêu cầu đơn giản mà hành vi mong đợi rõ ràng.

<img src="../../../translated_images/vi/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Hướng dẫn trực tiếp không có ví dụ — mô hình suy luận nhiệm vụ chỉ qua hướng dẫn*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Phản hồi: "Tích cực"
```

**Khi nào sử dụng:** Phân loại đơn giản, câu hỏi trực tiếp, dịch thuật hoặc bất kỳ nhiệm vụ nào mô hình có thể xử lý mà không cần hướng dẫn thêm.

### Few-Shot Prompting

Cung cấp các ví dụ thể hiện mẫu bạn muốn mô hình theo. Mô hình học định dạng đầu vào-đầu ra mong đợi từ ví dụ của bạn và áp dụng cho các đầu vào mới. Điều này cải thiện đáng kể sự nhất quán với các nhiệm vụ mà định dạng hoặc hành vi mong muốn không rõ ràng.

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

**Khi nào sử dụng:** Phân loại tùy chỉnh, định dạng đồng nhất, nhiệm vụ chuyên ngành, hoặc khi kết quả zero-shot không nhất quán.

### Chain of Thought

Yêu cầu mô hình trình bày suy luận từng bước. Thay vì nhảy ngay đến câu trả lời, mô hình phân tích vấn đề và giải quyết từng phần một cách rõ ràng. Cách này cải thiện độ chính xác cho các bài toán toán học, logic và suy luận nhiều bước.

<img src="../../../translated_images/vi/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Suy luận từng bước — phân tách vấn đề phức tạp thành các bước logic rõ ràng*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Mô hình này cho thấy: 15 - 8 = 7, sau đó 7 + 12 = 19 quả táo
```

**Khi nào sử dụng:** Bài toán toán học, câu đố logic, gỡ lỗi, hoặc bất kỳ nhiệm vụ nào khi việc thể hiện quá trình suy luận giúp tăng độ chính xác và tin cậy.

### Role-Based Prompting

Đặt vai trò hoặc persona cho AI trước khi đặt câu hỏi của bạn. Điều này cung cấp ngữ cảnh định hình giọng điệu, chiều sâu và trọng tâm của phản hồi. Một "kiến trúc sư phần mềm" sẽ đưa ra lời khuyên khác so với một "lập trình viên mới vào nghề" hoặc "kiểm toán viên bảo mật".

<img src="../../../translated_images/vi/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Đặt ngữ cảnh và vai trò — cùng một câu hỏi nhưng nhận được phản hồi khác biệt tùy theo vai trò được giao*

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

**Khi nào sử dụng:** Đánh giá code, gia sư, phân tích chuyên ngành, hoặc khi bạn cần phản hồi phù hợp với trình độ chuyên môn hoặc góc nhìn cụ thể.

### Prompt Templates

Tạo prompt có thể tái sử dụng với các chỗ chứa biến. Thay vì viết prompt mới mỗi lần, định nghĩa một mẫu và điền giá trị khác nhau. Lớp `PromptTemplate` của LangChain4j giúp việc này rất dễ dàng với cú pháp `{{variable}}`.

<img src="../../../translated_images/vi/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompt tái sử dụng với biến — một mẫu, nhiều lần dùng*

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

**Khi nào sử dụng:** Truy vấn lặp đi lặp lại với dữ liệu khác nhau, xử lý hàng loạt, xây dựng quy trình AI tái sử dụng, hoặc bất kỳ tình huống nào cấu trúc prompt giữ nguyên mà dữ liệu thay đổi.

---

Năm nguyên tắc cơ bản này cung cấp cho bạn bộ công cụ vững chắc cho hầu hết các nhiệm vụ tạo prompt. Phần còn lại của module sẽ xây dựng trên đó với **tám mẫu nâng cao** tận dụng khả năng điều khiển suy luận, tự đánh giá và đầu ra có cấu trúc của GPT-5.2.

## Các Mẫu Nâng Cao

Sau khi đã hiểu cơ bản, chúng ta chuyển sang tám mẫu nâng cao tạo nên sự độc đáo của module này. Không phải vấn đề nào cũng cần cùng một cách tiếp cận. Một số câu hỏi cần câu trả lời nhanh, số khác cần suy nghĩ sâu. Một vài cần thể hiện suy luận cụ thể, số khác chỉ cần kết quả. Mỗi mẫu dưới đây được tối ưu cho bối cảnh khác nhau — và điều khiển suy luận của GPT-5.2 càng làm rõ sự khác biệt đó.

<img src="../../../translated_images/vi/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Tổng quan về tám mẫu kỹ thuật tạo prompt và trường hợp sử dụng của chúng*

GPT-5.2 thêm một chiều kích nữa vào các mẫu này: *điều khiển suy luận*. Thanh trượt dưới đây cho thấy bạn có thể điều chỉnh mức độ suy nghĩ của mô hình — từ trả lời nhanh và trực tiếp đến phân tích sâu và toàn diện.

<img src="../../../translated_images/vi/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Điều khiển suy luận của GPT-5.2 cho phép bạn chỉ định mức độ suy nghĩ của mô hình — từ câu trả lời nhanh tới phân tích sâu sắc*

**Nhiệt Tình Thấp (Nhanh & Tập Trung)** - Dùng cho câu hỏi đơn giản bạn muốn câu trả lời nhanh, trực tiếp. Mô hình suy luận tối thiểu - tối đa 2 bước. Dùng cho tính toán, tra cứu hoặc câu hỏi đơn giản.

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
> - "Sự khác biệt giữa mẫu nhiệt tình thấp và cao trong tạo prompt là gì?"
> - "Các thẻ XML trong prompt giúp cấu trúc phản hồi AI như thế nào?"
> - "Khi nào nên dùng mẫu tự phản ánh so với hướng dẫn trực tiếp?"

**Nhiệt Tình Cao (Sâu & Toàn Diện)** - Dùng cho vấn đề phức tạp bạn muốn phân tích toàn diện. Mô hình khám phá chi tiết và trình bày suy luận cụ thể. Dùng cho thiết kế hệ thống, quyết định kiến trúc, hoặc nghiên cứu phức tạp.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Thực Thi Nhiệm Vụ (Tiến Trình Từng Bước)** - Dùng quy trình nhiều bước. Mô hình cung cấp kế hoạch trước, mô tả từng bước khi thực hiện, rồi đưa ra tóm tắt. Dùng cho di cư, triển khai hoặc quy trình đa bước.

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

Prompt chuỗi suy nghĩ (Chain-of-Thought) yêu cầu mô hình trình bày quá trình suy luận, cải thiện độ chính xác công việc phức tạp. Các bước từng bước giúp cả người và AI hiểu logic.

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Hỏi về mẫu này:
> - "Làm thế nào để điều chỉnh mẫu thực thi nhiệm vụ cho các công việc chạy lâu?"
> - "Các thực tiễn tốt nhất để cấu trúc phần mở đầu công cụ trong ứng dụng sản xuất là gì?"
> - "Làm sao để ghi lại và hiển thị tiến trình trung gian trong giao diện người dùng?"

Sơ đồ dưới thể hiện quy trình Kế Hoạch → Thực Thi → Tóm Tắt.

<img src="../../../translated_images/vi/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Quy trình Kế Hoạch → Thực Thi → Tóm Tắt cho các nhiệm vụ đa bước*

**Mã Tự Đánh Giá** - Dùng để tạo mã chất lượng sản xuất. Mô hình tạo code theo chuẩn chất lượng với xử lý lỗi thích hợp. Dùng khi xây dựng tính năng hoặc dịch vụ mới.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

Sơ đồ dưới đây mô tả vòng lặp cải tiến lặp đi lặp lại — tạo mã, đánh giá, xác định điểm yếu, tinh chỉnh cho đến khi mã đạt chuẩn sản xuất.

<img src="../../../translated_images/vi/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Vòng lặp cải tiến lặp lại - tạo, đánh giá, xác định lỗi, cải thiện, lặp lại*

**Phân Tích Có Cấu Trúc** - Dùng để đánh giá nhất quán. Mô hình xem xét code theo khung cố định (đúng sai, thực hành tốt, hiệu năng, bảo mật, khả năng bảo trì). Dùng cho đánh giá code hoặc kiểm định chất lượng.

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
> - "Làm sao tùy chỉnh khung phân tích cho các dạng đánh giá code khác nhau?"
> - "Cách tốt nhất để phân tích và xử lý đầu ra có cấu trúc một cách lập trình là gì?"
> - "Làm thế nào để đảm bảo mức độ nghiêm trọng nhất quán qua các phiên đánh giá khác nhau?"

Sơ đồ dưới đây thể hiện khung phân tích tổ chức đánh giá code thành các hạng mục nhất quán với mức độ nghiêm trọng.

<img src="../../../translated_images/vi/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Khung đánh giá code nhất quán với các mức độ nghiêm trọng*

**Trò Chuyện Nhiều Vòng** - Dùng cho các cuộc hội thoại cần ngữ cảnh. Mô hình nhớ các tin nhắn trước và xây dựng dựa trên đó. Dùng cho các phiên trợ giúp tương tác hoặc hỏi đáp phức tạp.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

Sơ đồ dưới đây trực quan hóa cách ngữ cảnh cuộc hội thoại tích lũy qua nhiều vòng và liên quan tới giới hạn token của mô hình.

<img src="../../../translated_images/vi/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Cách ngữ cảnh hội thoại được tích lũy qua nhiều lượt cho đến giới hạn token*
**Lý luận từng bước** - Dành cho các bài toán yêu cầu logic rõ ràng. Mô hình trình bày lý luận cụ thể cho từng bước. Sử dụng cách này cho các bài toán toán học, câu đố logic, hoặc khi bạn cần hiểu quy trình suy nghĩ.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

Sơ đồ dưới đây minh họa cách mô hình phân tách các bài toán thành các bước logic rõ ràng, được đánh số.

<img src="../../../translated_images/vi/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Mẫu từng bước" width="800"/>

*Phân tách vấn đề thành các bước logic rõ ràng*

**Đầu ra có giới hạn** - Dành cho câu trả lời yêu cầu định dạng cụ thể. Mô hình tuân thủ nghiêm ngặt các quy tắc về định dạng và độ dài. Sử dụng cách này cho các bản tóm tắt hoặc khi bạn cần kết quả chính xác theo cấu trúc yêu cầu.

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

Sơ đồ sau cho thấy cách ràng buộc định dạng hướng dẫn mô hình tạo ra đầu ra tuân thủ đúng định dạng và độ dài của bạn.

<img src="../../../translated_images/vi/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Mẫu đầu ra có giới hạn" width="800"/>

*Tuân thủ nghiêm ngặt các yêu cầu về định dạng, độ dài và cấu trúc*

## Chạy Ứng Dụng

**Xác minh triển khai:**

Đảm bảo tồn tại tệp `.env` trong thư mục gốc với thông tin xác thực Azure (đã tạo trong Module 01). Chạy từ thư mục module (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Khởi động ứng dụng:**

> **Lưu ý:** Nếu bạn đã khởi động tất cả các ứng dụng bằng `./start-all.sh` từ thư mục gốc (như mô tả trong Module 01), module này đã chạy trên cổng 8083. Bạn có thể bỏ qua các lệnh khởi động bên dưới và truy cập trực tiếp http://localhost:8083.

**Lựa chọn 1: Sử dụng Spring Boot Dashboard (Khuyên dùng cho người dùng VS Code)**

Dev container bao gồm tiện ích mở rộng Spring Boot Dashboard, cung cấp giao diện trực quan để quản lý tất cả các ứng dụng Spring Boot. Bạn có thể tìm thấy nó trong Thanh Hoạt Động bên trái của VS Code (tìm biểu tượng Spring Boot).

Từ Spring Boot Dashboard, bạn có thể:
- Xem tất cả các ứng dụng Spring Boot có trong workspace
- Khởi động/dừng ứng dụng chỉ với một cú nhấp chuột
- Xem nhật ký ứng dụng theo thời gian thực
- Giám sát trạng thái ứng dụng

Chỉ cần nhấn nút play bên cạnh "prompt-engineering" để khởi động module này, hoặc khởi động tất cả modules cùng lúc.

<img src="../../../translated_images/vi/dashboard.da2c2130c904aaf0.webp" alt="Bảng điều khiển Spring Boot" width="400"/>

*Spring Boot Dashboard trong VS Code — khởi động, dừng, và giám sát tất cả các module từ một chỗ*

**Lựa chọn 2: Sử dụng script shell**

Khởi động tất cả các ứng dụng web (modules 01-04):

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

Cả hai script tự động nạp biến môi trường từ tệp `.env` ở thư mục gốc và sẽ build các JAR nếu chưa tồn tại.

> **Lưu ý:** Nếu bạn muốn build thủ công tất cả các module trước khi khởi động:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

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
cd ..; .\stop-all.ps1  # Tất cả mô-đun
```

## Ảnh chụp màn hình ứng dụng

Đây là giao diện chính của module prompt engineering, nơi bạn có thể thử nghiệm tất cả tám mẫu cạnh nhau.

<img src="../../../translated_images/vi/dashboard-home.5444dbda4bc1f79d.webp" alt="Trang chủ Dashboard" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Bảng điều khiển chính hiển thị 8 mẫu prompt engineering cùng với đặc điểm và cách sử dụng*

## Khám phá các Mẫu

Giao diện web cho phép bạn thử nghiệm các chiến lược prompt khác nhau. Mỗi mẫu giải quyết các vấn đề khác nhau - hãy thử để xem khi nào từng cách thực sự hiệu quả.

> **Lưu ý: Streaming so với Không streaming** — Mỗi trang mẫu đều cung cấp hai nút: **🔴 Phát trực tiếp (Live)** và tùy chọn **Không streaming**. Streaming sử dụng Server-Sent Events (SSE) để hiển thị token theo thời gian thực khi mô hình tạo ra chúng, giúp bạn thấy tiến trình ngay lập tức. Tùy chọn không streaming sẽ đợi đến khi trả về toàn bộ phản hồi mới hiển thị. Với các prompt đòi hỏi suy luận sâu (ví dụ: High Eagerness, Self-Reflecting Code), gọi không streaming có thể mất rất lâu — đôi khi vài phút — mà không có phản hồi trực quan. **Dùng streaming khi thử nghiệm các prompt phức tạp** để bạn thấy mô hình đang hoạt động và tránh hiểu nhầm là yêu cầu đã hết time out.
>
> **Lưu ý: Yêu cầu trình duyệt** — Tính năng streaming sử dụng Fetch Streams API (`response.body.getReader()`) cần trình duyệt đầy đủ (Chrome, Edge, Firefox, Safari). Nó **không hoạt động** trong Simple Browser tích hợp của VS Code vì webview không hỗ trợ ReadableStream API. Nếu bạn dùng Simple Browser, các nút không streaming vẫn hoạt động bình thường — chỉ các nút streaming bị ảnh hưởng. Mở `http://localhost:8083` bằng trình duyệt bên ngoài để trải nghiệm đầy đủ.

### Low vs High Eagerness

Hỏi một câu đơn giản như "15% của 200 là bao nhiêu?" bằng Low Eagerness. Bạn sẽ nhận được câu trả lời trực tiếp ngay lập tức. Bây giờ hỏi một câu phức tạp như "Thiết kế chiến lược caching cho API có lưu lượng cao" với High Eagerness. Nhấn **🔴 Phát trực tiếp (Live)** và theo dõi quá trình suy luận chi tiết của mô hình từng token một. Cùng một mô hình, cùng dạng câu hỏi - nhưng prompt xác định mức độ suy nghĩ.

### Thực thi tác vụ (Tool Preambles)

Các quy trình nhiều bước hưởng lợi từ việc lập kế hoạch trước và mô tả tiến trình. Mô hình sẽ phác thảo những việc sẽ làm, mô tả từng bước, rồi tóm tắt kết quả.

### Self-Reflecting Code

Thử "Tạo dịch vụ xác thực email". Thay vì chỉ tạo code rồi dừng, mô hình sẽ tạo, đánh giá theo tiêu chí chất lượng, chỉ ra điểm yếu và cải tiến. Bạn sẽ thấy nó lặp lại cho đến khi code đạt chuẩn sản xuất.

### Phân tích cấu trúc

Đánh giá code cần khung đánh giá nhất quán. Mô hình phân tích code theo các hạng mục cố định (độ chính xác, thực hành, hiệu năng, bảo mật) với cấp độ nghiêm trọng.

### Chat đa lượt

Hỏi "Spring Boot là gì?" rồi ngay lập tức tiếp tục với "Cho tôi xem một ví dụ". Mô hình nhớ câu hỏi đầu tiên và đưa ra ví dụ Spring Boot cụ thể. Nếu không có bộ nhớ, câu hỏi thứ hai sẽ quá chung chung.

### Lý luận từng bước

Chọn một bài toán toán học và thử cả Step-by-Step Reasoning và Low Eagerness. Low eagerness chỉ cho bạn đáp án - nhanh nhưng mơ hồ. Lý luận từng bước cho thấy từng phép tính và quyết định.

### Đầu ra có giới hạn

Khi bạn cần định dạng hoặc số từ cụ thể, mẫu này giúp tuân thủ nghiêm ngặt. Thử tạo tóm tắt đúng 100 từ với dạng gạch đầu dòng.

## Bạn đang học gì thực sự

**Nỗ lực suy luận thay đổi mọi thứ**

GPT-5.2 cho phép bạn kiểm soát nỗ lực tính toán thông qua prompt. Nỗ lực thấp nghĩa là phản hồi nhanh với ít khám phá. Nỗ lực cao nghĩa là mô hình dành thời gian suy nghĩ sâu sắc. Bạn học cách điều chỉnh nỗ lực theo độ phức tạp công việc - không phí thời gian cho câu hỏi đơn giản, nhưng cũng không vội vàng trong những quyết định phức tạp.

**Cấu trúc hướng dẫn hành vi**

Bạn có thấy các thẻ XML trong prompt không? Chúng không chỉ là trang trí. Mô hình theo dõi chỉ dẫn có cấu trúc đáng tin cậy hơn so với văn bản tự do. Khi bạn cần quy trình nhiều bước hoặc logic phức tạp, cấu trúc giúp mô hình biết đang ở đâu và bước tiếp theo là gì. Sơ đồ dưới đây phân tích một prompt có cấu trúc tốt, chỉ ra cách các thẻ như `<system>`, `<instructions>`, `<context>`, `<user-input>`, và `<constraints>` tổ chức hướng dẫn thành các phần rõ ràng.

<img src="../../../translated_images/vi/prompt-structure.a77763d63f4e2f89.webp" alt="Cấu trúc Prompt" width="800"/>

*Giải phẫu một prompt cấu trúc tốt với các phần rõ ràng và tổ chức theo kiểu XML*

**Chất lượng qua tự đánh giá**

Các mẫu tự phản ánh hoạt động bằng cách làm rõ các tiêu chí chất lượng. Thay vì hy vọng mô hình "làm đúng", bạn chỉ cho nó chính xác nghĩa của "đúng": logic chính xác, xử lý lỗi, hiệu năng, bảo mật. Mô hình có thể tự đánh giá đầu ra và cải tiến. Điều này biến việc tạo code từ một trò chơi may rủi thành một quy trình.

**Ngữ cảnh hữu hạn**

Đàm thoại nhiều lượt hoạt động bằng cách kèm theo lịch sử tin nhắn với mỗi yêu cầu. Nhưng có giới hạn - mỗi mô hình có số token tối đa. Khi cuộc hội thoại mở rộng, bạn cần các chiến lược giữ ngữ cảnh liên quan mà không vượt quá giới hạn đó. Module này hướng dẫn bạn cách bộ nhớ hoạt động; sau này bạn sẽ học khi nào tóm tắt, khi nào quên, và khi nào truy xuất.

## Bước tiếp theo

**Module kế tiếp:** [03-rag - RAG (Tạo văn bản có hỗ trợ truy xuất)](../03-rag/README.md)

---

**Điều hướng:** [← Trước: Module 01 - Giới thiệu](../01-introduction/README.md) | [Trở về Chính](../README.md) | [Tiếp theo: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố từ chối trách nhiệm**:  
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo sự chính xác, nhưng xin lưu ý rằng các bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ ban đầu của nó vẫn được coi là nguồn tham khảo uy tín nhất. Đối với thông tin quan trọng, chúng tôi khuyến nghị sử dụng dịch vụ dịch thuật chuyên nghiệp do con người thực hiện. Chúng tôi không chịu trách nhiệm về bất kỳ sự hiểu lầm hoặc diễn giải sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->