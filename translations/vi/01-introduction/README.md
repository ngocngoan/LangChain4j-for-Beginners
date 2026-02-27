# Module 01: Bắt Đầu với LangChain4j

## Mục lục

- [Video Hướng Dẫn](../../../01-introduction)
- [Bạn Sẽ Học Gì](../../../01-introduction)
- [Yêu Cầu Trước](../../../01-introduction)
- [Hiểu Vấn Đề Cốt Lõi](../../../01-introduction)
- [Hiểu Về Token](../../../01-introduction)
- [Cách Bộ Nhớ Hoạt Động](../../../01-introduction)
- [Cách Sử Dụng LangChain4j](../../../01-introduction)
- [Triển Khai Hạ Tầng Azure OpenAI](../../../01-introduction)
- [Chạy Ứng Dụng Cục Bộ](../../../01-introduction)
- [Sử Dụng Ứng Dụng](../../../01-introduction)
  - [Chat Không Bộ Nhớ (Bảng Điều Khiển Bên Trái)](../../../01-introduction)
  - [Chat Có Bộ Nhớ (Bảng Điều Khiển Bên Phải)](../../../01-introduction)
- [Bước Tiếp Theo](../../../01-introduction)

## Video Hướng Dẫn

Xem buổi trực tiếp giải thích cách bắt đầu với module này: [Bắt Đầu với LangChain4j - Buổi Trực Tiếp](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Bạn Sẽ Học Gì

Nếu bạn đã hoàn thành phần khởi động nhanh, bạn đã thấy cách gửi đề bài và nhận phản hồi. Đó là nền tảng, nhưng ứng dụng thực sự cần nhiều hơn thế. Module này dạy bạn cách xây dựng AI hội thoại nhớ ngữ cảnh và duy trì trạng thái - điểm khác biệt giữa demo một lần và ứng dụng sẵn sàng cho sản xuất.

Chúng ta sẽ dùng GPT-5.2 của Azure OpenAI xuyên suốt hướng dẫn này vì khả năng suy luận nâng cao giúp làm rõ hành vi của các mẫu khác nhau. Khi thêm bộ nhớ, bạn sẽ thấy khác biệt rõ ràng. Điều này giúp dễ hiểu hơn về những gì từng thành phần mang lại cho ứng dụng của bạn.

Bạn sẽ xây dựng một ứng dụng minh họa cả hai mẫu:

**Chat Không Bộ Nhớ** - Mỗi yêu cầu độc lập. Mô hình không nhớ tin nhắn trước. Đây là mẫu bạn sử dụng trong phần khởi động nhanh.

**Hội Thoại Có Bộ Nhớ** - Mỗi yêu cầu bao gồm lịch sử hội thoại. Mô hình duy trì ngữ cảnh qua nhiều lượt. Đây là điều ứng dụng sản xuất cần.

## Yêu Cầu Trước

- Tài khoản Azure có quyền truy cập Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Lưu ý:** Java, Maven, Azure CLI và Azure Developer CLI (azd) đã được cài đặt sẵn trong devcontainer cung cấp.

> **Lưu ý:** Module này dùng GPT-5.2 trên Azure OpenAI. Việc triển khai được cấu hình tự động qua `azd up` - không sửa tên mô hình trong code.

## Hiểu Vấn Đề Cốt Lõi

Mô hình ngôn ngữ không có trạng thái. Mỗi cuộc gọi API độc lập. Nếu bạn gửi "My name is John" rồi hỏi "Tôi tên gì?", mô hình không biết bạn vừa giới thiệu tên. Nó xử lý mỗi yêu cầu như thể đó là cuộc hội thoại đầu tiên bạn từng có.

Điều này ổn cho Q&A đơn giản nhưng vô dụng với ứng dụng thật. Bot chăm sóc khách hàng cần nhớ bạn đã nói gì. Trợ lý cá nhân cần ngữ cảnh. Mọi hội thoại nhiều lượt đều cần bộ nhớ.

<img src="../../../translated_images/vi/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Sự khác biệt giữa hội thoại không trạng thái (gọi độc lập) và có trạng thái (nhận biết ngữ cảnh)*

## Hiểu Về Token

Trước khi vào hội thoại, bạn cần hiểu token - đơn vị cơ bản của văn bản mà mô hình ngôn ngữ xử lý:

<img src="../../../translated_images/vi/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Ví dụ về cách văn bản được tách thành token - "I love AI!" thành 4 đơn vị xử lý riêng biệt*

Token là cách mô hình AI đo lường và xử lý văn bản. Từ, dấu câu và cả khoảng trắng cũng có thể là token. Mô hình của bạn giới hạn số token nó có thể xử lý cùng lúc (400,000 cho GPT-5.2, tối đa 272,000 token đầu vào và 128,000 token đầu ra). Hiểu token giúp bạn quản lý độ dài hội thoại và chi phí.

## Cách Bộ Nhớ Hoạt Động

Bộ nhớ chat giải quyết vấn đề không trạng thái bằng cách duy trì lịch sử hội thoại. Trước khi gửi yêu cầu đến mô hình, framework thêm vào các tin nhắn trước có liên quan. Khi bạn hỏi "Tôi tên gì?", hệ thống thực sự gửi toàn bộ lịch sử hội thoại để mô hình thấy bạn đã nói "My name is John" trước đó.

LangChain4j cung cấp các triển khai bộ nhớ tự động xử lý việc này. Bạn chọn số tin nhắn giữ lại và framework quản lý ngữ cảnh.

<img src="../../../translated_images/vi/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory duy trì cửa sổ trượt các tin nhắn gần đây, tự động loại bỏ tin nhắn cũ*

## Cách Sử Dụng LangChain4j

Module này mở rộng phần khởi động nhanh bằng cách tích hợp Spring Boot và thêm bộ nhớ hội thoại. Cách các phần kết nối:

**Phụ thuộc** - Thêm hai thư viện LangChain4j:

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

**Mô hình Chat** - Cấu hình Azure OpenAI làm Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

Builder đọc thông tin đăng nhập từ biến môi trường do `azd up` đặt. Cài đặt `baseUrl` trỏ đến endpoint Azure của bạn làm client OpenAI hoạt động với Azure OpenAI.

**Bộ nhớ Hội Thoại** - Theo dõi lịch sử chat với MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Tạo bộ nhớ với `withMaxMessages(10)` giữ 10 tin nhắn gần nhất. Thêm tin nhắn người dùng và AI dạng typed wrapper: `UserMessage.from(text)` và `AiMessage.from(text)`. Lấy lịch sử bằng `memory.messages()` và gửi tới mô hình. Dịch vụ lưu các phiên bản bộ nhớ riêng theo conversation ID, cho phép nhiều người dùng chat cùng lúc.

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) và hỏi:
> - "MessageWindowChatMemory quyết định bỏ tin nhắn nào khi cửa sổ đầy bằng cách nào?"
> - "Tôi có thể triển khai lưu bộ nhớ tùy chỉnh sử dụng cơ sở dữ liệu thay vì bộ nhớ trong không?"
> - "Làm thế nào để tôi thêm tóm tắt nén lịch sử hội thoại cũ?"

Điểm cuối chat không bộ nhớ bỏ qua hoàn toàn - chỉ `chatModel.chat(prompt)` như phần khởi động nhanh. Điểm cuối có trạng thái thêm tin nhắn vào bộ nhớ, lấy lịch sử và đính kèm ngữ cảnh mỗi yêu cầu. Cấu hình mô hình giống nhau, mẫu khác nhau.

## Triển Khai Hạ Tầng Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Chọn đăng ký và vị trí (khuyến nghị eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Chọn đăng ký và vị trí (gợi ý eastus2)
```

> **Lưu ý:** Nếu gặp lỗi hết thời gian chờ (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), chỉ cần chạy lại `azd up`. Tài nguyên Azure có thể vẫn đang triển khai nền, và thử lại cho phép triển khai hoàn tất khi tài nguyên đạt trạng thái cuối.

Việc này sẽ:
1. Triển khai tài nguyên Azure OpenAI với GPT-5.2 và các mô hình text-embedding-3-small
2. Tự động tạo file `.env` trong thư mục gốc dự án với thông tin đăng nhập
3. Thiết lập tất cả biến môi trường cần thiết

**Gặp sự cố triển khai?** Xem [README hạ tầng](infra/README.md) để biết chi tiết xử lý sự cố bao gồm xung đột tên miền phụ, các bước triển khai thủ công qua Azure Portal và hướng dẫn cấu hình mô hình.

**Xác nhận triển khai thành công:**

**Bash:**
```bash
cat ../.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, v.v.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, v.v.
```

> **Lưu ý:** Lệnh `azd up` tự động tạo file `.env`. Nếu cần cập nhật sau, bạn có thể chỉnh file `.env` thủ công hoặc tạo lại bằng cách chạy:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## Chạy Ứng Dụng Cục Bộ

**Kiểm tra triển khai:**

Đảm bảo file `.env` tồn tại trong thư mục gốc với thông tin đăng nhập Azure:

**Bash:**
```bash
cat ../.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Khởi động ứng dụng:**

**Lựa chọn 1: Dùng Spring Boot Dashboard (Khuyến nghị cho người dùng VS Code)**

Dev container có tiện ích mở rộng Spring Boot Dashboard cung cấp giao diện trực quan quản lý tất cả ứng dụng Spring Boot. Bạn tìm thấy nó ở Thanh Hoạt Động bên trái VS Code (biểu tượng Spring Boot).

Từ Spring Boot Dashboard, bạn có thể:
- Xem tất cả ứng dụng Spring Boot trong workspace
- Khởi động/dừng ứng dụng chỉ với một cú nhấp
- Xem nhật ký ứng dụng theo thời gian thực
- Giám sát trạng thái ứng dụng

Chỉ cần bấm nút chạy bên cạnh "introduction" để khởi động module này, hoặc khởi động mọi module cùng lúc.

<img src="../../../translated_images/vi/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Lựa chọn 2: Dùng script shell**

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
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Cả hai script tự động tải biến môi trường từ file `.env` gốc và sẽ build file JAR nếu chưa tồn tại.

> **Lưu ý:** Nếu bạn muốn build thủ công tất cả module trước khi chạy:
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

Mở http://localhost:8080 trên trình duyệt.

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

## Sử Dụng Ứng Dụng

Ứng dụng cung cấp giao diện web với hai phiên bản chat đặt cạnh nhau.

<img src="../../../translated_images/vi/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Bảng điều khiển hiển thị cả tùy chọn Chat Đơn Giản (không bộ nhớ) và Chat Hội Thoại (có bộ nhớ)*

### Chat Không Bộ Nhớ (Bảng Điều Khiển Bên Trái)

Thử trước. Hỏi "My name is John" rồi ngay lập tức hỏi "Tôi tên gì?" Mô hình sẽ không nhớ vì mỗi tin nhắn độc lập. Điều này minh họa vấn đề cốt lõi khi tích hợp mô hình ngôn ngữ cơ bản - không có ngữ cảnh cuộc hội thoại.

<img src="../../../translated_images/vi/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI không nhớ tên bạn từ tin nhắn trước*

### Chat Có Bộ Nhớ (Bảng Điều Khiển Bên Phải)

Bây giờ thử chuỗi tương tự ở đây. Hỏi "My name is John" rồi "Tôi tên gì?" Lần này nó nhớ. Khác biệt là MessageWindowChatMemory - nó giữ lịch sử hội thoại và đính kèm vào mỗi yêu cầu. Đây là cách AI hội thoại trong sản xuất hoạt động.

<img src="../../../translated_images/vi/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI nhớ tên bạn từ trước trong cuộc hội thoại*

Cả hai bảng dùng chung mô hình GPT-5.2. Khác biệt duy nhất là bộ nhớ. Điều này làm rõ giá trị bộ nhớ mang lại cho ứng dụng của bạn và tại sao nó cần thiết cho trường hợp sử dụng thực tế.

## Bước Tiếp Theo

**Module tiếp theo:** [02-prompt-engineering - Kỹ Thuật Tạo Đề Bài với GPT-5.2](../02-prompt-engineering/README.md)

---

**Điều hướng:** [← Trước: Module 00 - Khởi Đầu Nhanh](../00-quick-start/README.md) | [Trở Về Chính](../README.md) | [Tiếp: Module 02 - Kỹ Thuật Tạo Đề Bài →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố từ chối trách nhiệm**:
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng các bản dịch tự động có thể chứa lỗi hoặc sai sót. Văn bản gốc bằng ngôn ngữ gốc của tài liệu nên được xem là nguồn thông tin chính xác và đáng tin cậy. Đối với những thông tin quan trọng, nên sử dụng dịch vụ dịch thuật chuyên nghiệp do con người thực hiện. Chúng tôi không chịu trách nhiệm đối với bất kỳ sự hiểu lầm hoặc giải thích sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->