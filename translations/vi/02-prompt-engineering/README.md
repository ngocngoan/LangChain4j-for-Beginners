# Module 02: Kỹ Thuật Lập Trình Prompt với GPT-5.2

## Mục Lục

- [Bạn Sẽ Học Những Gì](../../../02-prompt-engineering)
- [Yêu Cầu Tiền Đề](../../../02-prompt-engineering)
- [Hiểu Về Kỹ Thuật Lập Trình Prompt](../../../02-prompt-engineering)
- [Các Nguyên Tắc Cơ Bản về Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Các Mẫu Nâng Cao](../../../02-prompt-engineering)
- [Sử Dụng Tài Nguyên Azure Hiện Có](../../../02-prompt-engineering)
- [Ảnh Chụp Màn Hình Ứng Dụng](../../../02-prompt-engineering)
- [Khám Phá Các Mẫu](../../../02-prompt-engineering)
  - [Nhiệt Tình Thấp vs Cao](../../../02-prompt-engineering)
  - [Thực Thi Tác Vụ (Phần Mở Đầu Công Cụ)](../../../02-prompt-engineering)
  - [Mã Tự Phản Chiếu](../../../02-prompt-engineering)
  - [Phân Tích Có Cấu Trúc](../../../02-prompt-engineering)
  - [Chat Nhiều Vòng](../../../02-prompt-engineering)
  - [Lý Luận Theo Từng Bước](../../../02-prompt-engineering)
  - [Đầu Ra Có Hạn Chế](../../../02-prompt-engineering)
- [Bạn Thực Sự Đang Học Gì](../../../02-prompt-engineering)
- [Bước Tiếp Theo](../../../02-prompt-engineering)

## Bạn Sẽ Học Những Gì

<img src="../../../translated_images/vi/what-youll-learn.c68269ac048503b2.webp" alt="Bạn Sẽ Học Những Gì" width="800"/>

Trong module trước, bạn đã thấy cách bộ nhớ hỗ trợ AI hội thoại và sử dụng GitHub Models để tương tác cơ bản. Bây giờ chúng ta sẽ tập trung vào cách bạn đặt câu hỏi — chính các prompt — sử dụng GPT-5.2 của Azure OpenAI. Cách bạn cấu trúc prompt ảnh hưởng lớn đến chất lượng câu trả lời nhận được. Chúng ta bắt đầu bằng việc xem lại các kỹ thuật prompt cơ bản, sau đó đi vào tám mẫu nâng cao tận dụng tối đa khả năng của GPT-5.2.

Chúng ta sẽ dùng GPT-5.2 vì nó giới thiệu điều khiển lý luận — bạn có thể hướng dẫn mô hình suy nghĩ bao nhiêu trước khi trả lời. Điều này làm cho các chiến lược prompt khác nhau trở nên rõ ràng hơn và giúp bạn hiểu khi nào nên dùng từng cách. Chúng ta cũng được lợi từ các giới hạn sử dụng thấp hơn của Azure cho GPT-5.2 so với GitHub Models.

## Yêu Cầu Tiền Đề

- Hoàn thành Module 01 (đã triển khai tài nguyên Azure OpenAI)
- File `.env` ở thư mục gốc chứa thông tin đăng nhập Azure (được tạo bởi `azd up` trong Module 01)

> **Lưu ý:** Nếu bạn chưa hoàn thành Module 01, hãy làm theo hướng dẫn triển khai ở đó trước.

## Hiểu Về Kỹ Thuật Lập Trình Prompt

<img src="../../../translated_images/vi/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Kỹ Thuật Lập Trình Prompt Là Gì?" width="800"/>

Prompt engineering là về thiết kế văn bản đầu vào sao cho luôn đạt được kết quả bạn cần. Nó không chỉ là hỏi câu hỏi — mà còn là cấu trúc yêu cầu sao cho mô hình hiểu chính xác bạn muốn gì và cách cung cấp nó.

Hãy nghĩ như việc bạn đưa hướng dẫn cho đồng nghiệp. "Sửa lỗi" thì mơ hồ. "Sửa lỗi null pointer trong UserService.java dòng 45 bằng cách thêm kiểm tra null" thì rõ ràng. Các mô hình ngôn ngữ cũng vậy — sự cụ thể và cấu trúc rất quan trọng.

<img src="../../../translated_images/vi/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j Phù Hợp Như Thế Nào" width="800"/>

LangChain4j cung cấp hạ tầng — kết nối mô hình, bộ nhớ và loại tin nhắn — trong khi các mẫu prompt chỉ là văn bản được cấu trúc cẩn thận bạn gửi qua hạ tầng đó. Các thành phần cốt lõi là `SystemMessage` (đặt hành vi và vai trò AI) và `UserMessage` (chứa yêu cầu thực tế của bạn).

## Các Nguyên Tắc Cơ Bản về Prompt Engineering

<img src="../../../translated_images/vi/five-patterns-overview.160f35045ffd2a94.webp" alt="Tổng Quan Năm Mẫu Prompt Engineering" width="800"/>

Trước khi đi vào các mẫu nâng cao trong module này, hãy xem lại năm kỹ thuật prompt nền tảng. Đây là những viên gạch xây dựng mà mọi kỹ sư prompt nên biết. Nếu bạn đã làm qua [mô-đun Quick Start](../00-quick-start/README.md#2-prompt-patterns), bạn đã thấy chúng trong thực tế — dưới đây là khung khái niệm đằng sau chúng.

### Zero-Shot Prompting

Cách đơn giản nhất: cho mô hình một chỉ dẫn trực tiếp mà không có ví dụ. Mô hình dựa hoàn toàn vào việc học để hiểu và thực hiện nhiệm vụ. Cách này phù hợp với yêu cầu đơn giản, nơi hành vi mong đợi dễ hiểu.

<img src="../../../translated_images/vi/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Chỉ dẫn trực tiếp không có ví dụ — mô hình suy luận nhiệm vụ chỉ từ chỉ dẫn*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Phản hồi: "Tích cực"
```

**Khi nào dùng:** Phân loại đơn giản, câu hỏi trực tiếp, dịch thuật hoặc bất kỳ tác vụ nào mô hình có thể xử lý mà không cần hướng dẫn thêm.

### Few-Shot Prompting

Cung cấp ví dụ minh họa mẫu bạn muốn mô hình theo. Mô hình học được định dạng đầu vào-đầu ra mong đợi từ ví dụ và áp dụng cho dữ liệu mới. Cách này cải thiện rất nhiều sự nhất quán khi định dạng hoặc hành vi mong muốn không rõ ràng.

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

**Khi nào dùng:** Phân loại tùy chỉnh, format đồng nhất, tác vụ chuyên ngành, hoặc khi kết quả zero-shot không nhất quán.

### Chain of Thought

Yêu cầu mô hình thể hiện lý luận từng bước. Thay vì trả lời ngay lập tức, mô hình chia nhỏ vấn đề và lý giải từng phần rõ ràng. Điều này nâng cao độ chính xác trên toán học, logic, và các nhiệm vụ cần suy luận nhiều bước.

<img src="../../../translated_images/vi/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Suy luận từng bước — phân nhỏ vấn đề phức tạp thành các bước logic rõ ràng*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Mô hình cho thấy: 15 - 8 = 7, sau đó 7 + 12 = 19 quả táo
```

**Khi nào dùng:** Các bài toán, câu đố logic, gỡ lỗi, hoặc bất cứ tác vụ nào việc thể hiện quá trình suy luận giúp tăng độ chính xác và tin cậy.

### Role-Based Prompting

Đặt vai trò hoặc persona cho AI trước khi hỏi. Điều này cung cấp ngữ cảnh tạo ra tông điệu, độ sâu và trọng tâm khác cho câu trả lời. Một "kiến trúc sư phần mềm" sẽ cho lời khuyên khác "lập trình viên mới" hoặc "chuyên viên kiểm thử an ninh".

<img src="../../../translated_images/vi/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Đặt ngữ cảnh và nhân vật — cùng một câu hỏi nhưng tùy vai trò sẽ có câu trả lời khác nhau*

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

**Khi nào dùng:** Đánh giá code, giảng dạy, phân tích chuyên ngành, hoặc khi bạn cần câu trả lời phù hợp trình độ chuyên môn hoặc góc nhìn cụ thể.

### Prompt Templates

Tạo prompt có thể tái sử dụng với các biến chỗ trống. Thay vì viết prompt mới mỗi lần, định nghĩa một mẫu và điền giá trị khác nhau. Lớp `PromptTemplate` của LangChain4j làm việc này dễ dàng với cú pháp `{{variable}}`.

<img src="../../../translated_images/vi/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompt tái sử dụng với biến chỗ trống — một mẫu, nhiều lần dùng*

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

**Khi nào dùng:** Truy vấn lặp đi lặp lại với đầu vào khác nhau, xử lý hàng loạt, xây dựng quy trình AI tái sử dụng, hoặc bất cứ tình huống nào cấu trúc prompt giữ nguyên nhưng dữ liệu thay đổi.

---

Năm nguyên tắc cơ bản này cung cấp cho bạn bộ công cụ vững chắc cho hầu hết nhiệm vụ prompt. Phần còn lại của module xây dựng dựa trên chúng với **tám mẫu nâng cao** tận dụng điều khiển lý luận, tự đánh giá và khả năng đầu ra có cấu trúc của GPT-5.2.

## Các Mẫu Nâng Cao

Khi đã bao quát các nguyên lý cơ bản, hãy chuyển sang tám mẫu nâng cao làm cho module này đặc biệt. Không phải vấn đề nào cũng cần cách tiếp cận giống nhau. Một số câu hỏi cần trả lời nhanh, có câu cần suy nghĩ sâu. Có câu cần thể hiện lý luận rõ ràng, câu khác chỉ cần kết quả. Mỗi mẫu dưới đây được tối ưu cho tình huống khác nhau — và điều khiển lý luận của GPT-5.2 làm sự khác biệt càng rõ nét hơn.

<img src="../../../translated_images/vi/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Tám Mẫu Prompting" width="800"/>

*Tổng quan tám mẫu kỹ thuật prompt và trường hợp sử dụng*

<img src="../../../translated_images/vi/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Điều Khiển Lý Luận Với GPT-5.2" width="800"/>

*Điều khiển lý luận của GPT-5.2 cho phép bạn chỉ định mức độ suy nghĩ mô hình nên thực hiện — từ trả lời nhanh gọn đến lý giải sâu sắc*

<img src="../../../translated_images/vi/reasoning-effort.db4a3ba5b8e392c1.webp" alt="So Sánh Nỗ Lực Lý Luận" width="800"/>

*Nhiệt tình thấp (nhanh, trực tiếp) so với nhiệt tình cao (tỉ mỉ, khám phá) trong cách lý luận*

**Nhiệt Tình Thấp (Nhanh & Tập Trung)** - Cho các câu hỏi đơn giản bạn muốn câu trả lời nhanh, trực tiếp. Mô hình suy nghĩ tối thiểu - tối đa 2 bước. Dùng cho phép tính, tra cứu hoặc câu hỏi đơn giản.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Khám phá với GitHub Copilot:** Mở [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) và hỏi:
> - "Sự khác biệt giữa mẫu prompt nhiệt tình thấp và nhiệt tình cao là gì?"
> - "Các thẻ XML trong prompt giúp cấu trúc câu trả lời AI thế nào?"
> - "Khi nào nên dùng mẫu tự phản ánh thay vì chỉ dẫn trực tiếp?"

**Nhiệt Tình Cao (Sâu & Kỹ Lưỡng)** - Cho các vấn đề phức tạp bạn muốn phân tích toàn diện. Mô hình khám phá kỹ và thể hiện lý luận chi tiết. Dùng cho thiết kế hệ thống, quyết định kiến trúc hoặc nghiên cứu phức tạp.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Thực Thi Tác Vụ (Tiến Trình Từng Bước)** - Cho quy trình làm việc nhiều bước. Mô hình cung cấp kế hoạch trước, tường thuật từng bước khi làm việc, rồi tổng kết. Dùng cho di chuyển dữ liệu, triển khai hoặc bất kỳ quá trình nhiều bước nào.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Mẫu Chain-of-Thought yêu cầu mô hình thể hiện quá trình lý luận, nâng cao độ chính xác cho nhiệm vụ phức tạp. Việc phân tách từng bước giúp cả người và AI hiểu logic.

> **🤖 Thử với Chat [GitHub Copilot](https://github.com/features/copilot):** Hỏi về mẫu này:
> - "Làm sao mình điều chỉnh mẫu thực thi tác vụ cho các hoạt động chạy lâu?"
> - "Các thực hành tốt nhất để cấu trúc phần mở đầu công cụ trong ứng dụng sản xuất là gì?"
> - "Làm sao mình ghi lại và hiển thị tiến trình trung gian trong giao diện UI?"

<img src="../../../translated_images/vi/task-execution-pattern.9da3967750ab5c1e.webp" alt="Mẫu Thực Thi Tác Vụ" width="800"/>

*Kế hoạch → Thực thi → Tổng kết cho tác vụ nhiều bước*

**Mã Tự Phản Chiếu** - Cho việc tạo mã chất lượng sản xuất. Mô hình tạo mã, kiểm tra tiêu chí chất lượng, cải tiến lặp đi lặp lại. Dùng khi xây dựng tính năng hoặc dịch vụ mới.

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

*Vòng cải tiến lặp - tạo, đánh giá, xác định lỗi, cải thiện, lặp lại*

**Phân Tích Có Cấu Trúc** - Cho đánh giá nhất quán. Mô hình kiểm tra code theo khung cố định (đúng đắn, thực hành tốt, hiệu suất, an toàn). Dùng cho đánh giá code hoặc kiểm định chất lượng.

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

> **🤖 Thử với Chat [GitHub Copilot](https://github.com/features/copilot):** Hỏi về phân tích có cấu trúc:
> - "Làm sao để tùy chỉnh khung phân tích cho các loại đánh giá code khác nhau?"
> - "Cách tốt nhất để phân tích và xử lý đầu ra có cấu trúc theo chương trình là gì?"
> - "Làm sao đảm bảo mức độ nghiêm trọng đồng nhất qua các phiên đánh giá khác nhau?"

<img src="../../../translated_images/vi/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Mẫu Phân Tích Có Cấu Trúc" width="800"/>

*Khung bốn loại để đánh giá code nhất quán với các mức độ nghiêm trọng*

**Chat Nhiều Vòng** - Cho cuộc hội thoại cần ngữ cảnh. Mô hình nhớ các tin nhắn trước và xây dựng dựa trên đó. Dùng cho phiên trợ giúp tương tác hoặc hỏi đáp phức tạp.

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

**Lý Luận Theo Từng Bước** - Cho các vấn đề cần logic hiển thị. Mô hình thể hiện rõ ràng lý luận từng bước. Dùng cho bài toán toán học, câu đố logic hoặc khi bạn cần hiểu quá trình tư duy.

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

*Phân tách vấn đề thành từng bước logic rõ ràng*

**Đầu Ra Có Hạn Chế** - Cho câu trả lời với yêu cầu định dạng cụ thể. Mô hình tuân thủ nghiêm ngặt các quy tắc về format và độ dài. Dùng cho tóm tắt hoặc khi cần cấu trúc đầu ra chính xác.

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

<img src="../../../translated_images/vi/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Mẫu Đầu Ra Có Hạn Chế" width="800"/>

*Áp đặt các yêu cầu định dạng, kích thước, và cấu trúc nghiêm ngặt*

## Sử Dụng Tài Nguyên Azure Hiện Có

**Xác minh triển khai:**

Đảm bảo file `.env` tồn tại ở thư mục gốc với thông tin xác thực Azure (được tạo trong Module 01):
```bash
cat ../.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Khởi động ứng dụng:**

> **Lưu ý:** Nếu bạn đã khởi động tất cả ứng dụng bằng `./start-all.sh` từ Module 01, module này đã chạy trên cổng 8083. Bạn có thể bỏ qua các lệnh khởi động bên dưới và truy cập trực tiếp http://localhost:8083.

**Cách 1: Sử dụng Spring Boot Dashboard (Khuyên dùng cho người dùng VS Code)**

Dev container bao gồm tiện ích mở rộng Spring Boot Dashboard, cung cấp giao diện trực quan để quản lý tất cả ứng dụng Spring Boot. Bạn có thể tìm thấy nó trong Thanh Hoạt Động bên trái màn hình VS Code (tìm biểu tượng Spring Boot).
Từ Bảng điều khiển Spring Boot, bạn có thể:
- Xem tất cả các ứng dụng Spring Boot có trong workspace
- Khởi động/dừng ứng dụng chỉ với một cú nhấp chuột
- Xem nhật ký ứng dụng theo thời gian thực
- Giám sát trạng thái ứng dụng

Chỉ cần nhấp vào nút phát bên cạnh "prompt-engineering" để khởi động module này, hoặc khởi động tất cả các module cùng lúc.

<img src="../../../translated_images/vi/dashboard.da2c2130c904aaf0.webp" alt="Bảng điều khiển Spring Boot" width="400"/>

**Tùy chọn 2: Sử dụng shell script**

Khởi động tất cả ứng dụng web (module 01-04):

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

Cả hai script đều tự động tải biến môi trường từ file `.env` gốc và sẽ xây dựng các file JAR nếu chưa tồn tại.

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

<img src="../../../translated_images/vi/dashboard-home.5444dbda4bc1f79d.webp" alt="Trang chủ Bảng điều khiển" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Bảng điều khiển chính hiển thị 8 mẫu prompt engineering với đặc tính và trường hợp sử dụng của chúng*

## Khám phá các Mẫu

Giao diện web cho phép bạn thử nghiệm với các chiến lược prompt khác nhau. Mỗi mẫu giải quyết các vấn đề khác nhau - hãy thử chúng để xem khi nào từng cách tiếp cận phát huy hiệu quả.

### Độ Nhiệt Tình Thấp vs Cao

Hãy hỏi một câu đơn giản như "15% của 200 là bao nhiêu?" với Độ Nhiệt Tình Thấp. Bạn sẽ nhận được câu trả lời trực tiếp ngay lập tức. Bây giờ hãy hỏi điều phức tạp như "Thiết kế chiến lược cache cho API có lưu lượng cao" với Độ Nhiệt Tình Cao. Quan sát cách mô hình chậm lại và đưa ra lý luận chi tiết. Cùng một mô hình, cùng cấu trúc câu hỏi - nhưng prompt chỉ dẫn nó suy nghĩ bao nhiêu.

<img src="../../../translated_images/vi/low-eagerness-demo.898894591fb23aa0.webp" alt="Demo Độ Nhiệt Tình Thấp" width="800"/>

*Tính toán nhanh với ít suy luận*

<img src="../../../translated_images/vi/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Demo Độ Nhiệt Tình Cao" width="800"/>

*Chiến lược cache toàn diện (2.8MB)*

### Thực thi Nhiệm vụ (Các phần mở đầu công cụ)

Các workflow đa bước hưởng lợi từ việc lập kế hoạch trước và thuyết minh tiến trình. Mô hình phác thảo những gì sẽ làm, thuyết minh từng bước, rồi tổng kết kết quả.

<img src="../../../translated_images/vi/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Demo Thực thi Nhiệm vụ" width="800"/>

*Tạo một endpoint REST với thuyết minh từng bước (3.9MB)*

### Mã Tự Phản Chiếu

Thử "Tạo một dịch vụ xác thực email". Thay vì chỉ tạo mã rồi dừng, mô hình tạo, đánh giá theo tiêu chí chất lượng, xác định điểm yếu, và cải tiến. Bạn sẽ thấy nó lặp đi lặp lại cho tới khi mã đạt tiêu chuẩn sản xuất.

<img src="../../../translated_images/vi/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Demo Mã Tự Phản Chiếu" width="800"/>

*Dịch vụ xác thực email hoàn chỉnh (5.2MB)*

### Phân tích Cấu trúc

Đánh giá mã cần khung đánh giá nhất quán. Mô hình phân tích mã sử dụng các danh mục cố định (độ chính xác, thực hành, hiệu suất, bảo mật) với các mức độ nghiêm trọng.

<img src="../../../translated_images/vi/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Demo Phân tích Cấu trúc" width="800"/>

*Đánh giá mã dựa trên khung*

### Hội thoại Nhiều lượt

Hỏi "Spring Boot là gì?" rồi ngay lập tức tiếp tục với "Cho tôi một ví dụ". Mô hình nhớ câu hỏi đầu tiên và đưa ra ví dụ Spring Boot cụ thể. Nếu không có bộ nhớ, câu hỏi thứ hai sẽ quá mơ hồ.

<img src="../../../translated_images/vi/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Demo Hội thoại Nhiều lượt" width="800"/>

*Giữ nguyên ngữ cảnh qua các câu hỏi*

### Lý luận Từng bước

Chọn một bài toán toán học và thử với cả Lý luận Từng bước và Độ Nhiệt Tình Thấp. Độ nhiệt tình thấp chỉ đưa ra câu trả lời nhanh - nhanh nhưng không rõ ràng. Lý luận từng bước cho bạn thấy mọi phép tính và quyết định.

<img src="../../../translated_images/vi/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Demo Lý luận Từng bước" width="800"/>

*Bài toán toán với các bước rõ ràng*

### Đầu ra Bị giới hạn

Khi bạn cần định dạng cụ thể hoặc số lượng từ cố định, mẫu này bắt buộc tuân thủ nghiêm ngặt. Thử tạo một bản tóm tắt với đúng 100 từ theo định dạng gạch đầu dòng.

<img src="../../../translated_images/vi/constrained-output-demo.567cc45b75da1633.webp" alt="Demo Đầu ra Bị giới hạn" width="800"/>

*Tóm tắt học máy với kiểm soát định dạng*

## Những gì Bạn Thực Sự Học được

**Nỗ lực Lý luận Thay đổi Mọi thứ**

GPT-5.2 cho phép bạn điều khiển nỗ lực tính toán thông qua prompt của mình. Nỗ lực thấp có nghĩa là trả lời nhanh với ít khám phá. Nỗ lực cao nghĩa là mô hình dành thời gian suy nghĩ sâu sắc. Bạn đang học cách phối hợp nỗ lực với độ phức tạp nhiệm vụ - đừng lãng phí thời gian cho câu hỏi đơn giản, nhưng cũng đừng vội vàng với quyết định phức tạp.

**Cấu trúc Hướng Dẫn Hành vi**

Có nhận thấy các thẻ XML trong prompt không? Chúng không phải để trang trí. Mô hình theo các chỉ dẫn có cấu trúc đáng tin cậy hơn so với văn bản tự do. Khi bạn cần quy trình đa bước hoặc logic phức tạp, cấu trúc giúp mô hình theo dõi vị trí và bước tiếp theo.

<img src="../../../translated_images/vi/prompt-structure.a77763d63f4e2f89.webp" alt="Cấu trúc Prompt" width="800"/>

*Cấu tạo của một prompt có cấu trúc tốt với các phần rõ ràng và tổ chức kiểu XML*

**Chất lượng Qua Tự Đánh giá**

Các mẫu tự phản chiếu hoạt động bằng cách làm rõ các tiêu chí chất lượng. Thay vì hy vọng mô hình "làm đúng", bạn chỉ rõ chính xác "đúng" nghĩa là gì: logic chính xác, xử lý lỗi, hiệu suất, bảo mật. Mô hình có thể tự đánh giá đầu ra và cải thiện. Điều này biến việc tạo code từ một trò chơi xổ số thành một quy trình.

**Ngữ cảnh là Có giới hạn**

Hội thoại nhiều lượt hoạt động bằng cách bao gồm lịch sử tin nhắn trong mỗi yêu cầu. Nhưng có giới hạn - mỗi mô hình có số lượng token tối đa. Khi hội thoại lớn lên, bạn cần chiến lược giữ ngữ cảnh phù hợp mà không vượt quá giới hạn đó. Module này chỉ bạn cách bộ nhớ hoạt động; sau này bạn sẽ học khi nào tóm tắt, khi nào quên, và khi nào truy xuất.

## Bước tiếp theo

**Module kế tiếp:** [03-rag - RAG (Tạo nội dung bổ sung truy xuất)](../03-rag/README.md)

---

**Điều hướng:** [← Trước: Module 01 - Giới thiệu](../01-introduction/README.md) | [Quay lại Chính](../README.md) | [Tiếp: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố miễn trừ trách nhiệm**:  
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo tính chính xác, xin lưu ý rằng bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ mẹ đẻ của nó nên được xem là nguồn tham khảo chính thống. Đối với các thông tin quan trọng, khuyến nghị sử dụng dịch vụ dịch thuật chuyên nghiệp do con người thực hiện. Chúng tôi không chịu trách nhiệm về bất kỳ sự hiểu lầm hoặc giải thích sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->