# Module 01: Bắt đầu với LangChain4j

## Mục lục

- [Video Hướng Dẫn](../../../01-introduction)
- [Bạn Sẽ Học Được Gì](../../../01-introduction)
- [Yêu Cầu Trước](../../../01-introduction)
- [Hiểu Vấn Đề Cốt Lõi](../../../01-introduction)
- [Hiểu Về Tokens](../../../01-introduction)
- [Cách Bộ Nhớ Hoạt Động](../../../01-introduction)
- [Cách Điều Này Sử Dụng LangChain4j](../../../01-introduction)
- [Triển Khai Hạ Tầng Azure OpenAI](../../../01-introduction)
- [Chạy Ứng Dụng Cục Bộ](../../../01-introduction)
- [Sử Dụng Ứng Dụng](../../../01-introduction)
  - [Chat Không Trạng Thái (Bảng Trái)](../../../01-introduction)
  - [Chat Có Trạng Thái (Bảng Phải)](../../../01-introduction)
- [Bước Tiếp Theo](../../../01-introduction)

## Video Hướng Dẫn

Xem buổi live này giải thích cách bắt đầu với module này:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Bắt đầu với LangChain4j - Buổi Trực Tiếp" width="800"/></a>

## Bạn Sẽ Học Được Gì

Nếu bạn đã hoàn thành phần khởi động nhanh, bạn đã thấy cách gửi câu lệnh và nhận phản hồi. Đó là nền tảng, nhưng các ứng dụng thực sự cần nhiều hơn thế. Module này sẽ dạy bạn cách xây dựng AI đàm thoại nhớ ngữ cảnh và duy trì trạng thái — sự khác biệt giữa bản demo một lần và ứng dụng sẵn sàng sản xuất.

Chúng ta sẽ sử dụng GPT-5.2 của Azure OpenAI trong suốt hướng dẫn này vì khả năng lập luận nâng cao của nó giúp làm rõ hơn hành vi của các mô hình khác nhau. Khi bạn thêm bộ nhớ, bạn sẽ thấy sự khác biệt rõ ràng. Điều này giúp bạn dễ hiểu hơn về vai trò của từng thành phần cho ứng dụng của bạn.

Bạn sẽ xây dựng một ứng dụng minh họa cả hai mô hình:

**Chat Không Trạng Thái** - Mỗi yêu cầu độc lập. Mô hình không nhớ các tin nhắn trước đó. Đây là mô hình bạn đã sử dụng trong phần khởi động nhanh.

**Cuộc Đàm Thoại Có Trạng Thái** - Mỗi yêu cầu bao gồm lịch sử đàm thoại. Mô hình duy trì ngữ cảnh qua nhiều lượt. Đây là yêu cầu cho các ứng dụng sản xuất.

## Yêu Cầu Trước

- Tài khoản Azure có quyền truy cập Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Lưu ý:** Java, Maven, Azure CLI và Azure Developer CLI (azd) được cài sẵn trong devcontainer được cung cấp.

> **Lưu ý:** Module này sử dụng GPT-5.2 trên Azure OpenAI. Việc triển khai được cấu hình tự động qua `azd up` - không chỉnh sửa tên mô hình trong mã nguồn.

## Hiểu Vấn Đề Cốt Lõi

Các mô hình ngôn ngữ không có trạng thái. Mỗi lần gọi API là độc lập. Nếu bạn gửi "Tên tôi là John" rồi hỏi "Tên tôi là gì?", mô hình không biết bạn vừa giới thiệu tên. Nó coi mỗi yêu cầu như lần đầu tiên bạn nói chuyện.

Điều này ổn với Q&A đơn giản nhưng vô dụng với ứng dụng thực tế. Bot chăm sóc khách hàng cần nhớ những gì bạn nói. Trợ lý cá nhân cần ngữ cảnh. Mọi cuộc đàm thoại nhiều lượt đều cần bộ nhớ.

<img src="../../../translated_images/vi/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Cuộc đàm thoại không trạng thái vs có trạng thái" width="800"/>

*Sự khác biệt giữa đàm thoại không trạng thái (gọi độc lập) và có trạng thái (nhận biết ngữ cảnh)*

## Hiểu Về Tokens

Trước khi đi sâu vào đàm thoại, cần hiểu tokens - đơn vị cơ bản của văn bản mà mô hình ngôn ngữ xử lý:

<img src="../../../translated_images/vi/token-explanation.c39760d8ec650181.webp" alt="Giải thích Token" width="800"/>

*Ví dụ cách văn bản được tách thành tokens - "I love AI!" thành 4 đơn vị xử lý riêng biệt*

Tokens là cách mô hình AI đo lường và xử lý văn bản. Từ, dấu câu, thậm chí khoảng trắng có thể là tokens. Mô hình của bạn có giới hạn số tokens xử lý cùng lúc (400,000 cho GPT-5.2, với tối đa 272,000 tokens đầu vào và 128,000 tokens đầu ra). Hiểu tokens giúp bạn quản lý độ dài cuộc hội thoại và chi phí.

## Cách Bộ Nhớ Hoạt Động

Bộ nhớ đàm thoại giải quyết vấn đề không trạng thái bằng cách duy trì lịch sử cuộc hội thoại. Trước khi gửi yêu cầu đến mô hình, framework tụt thêm các tin nhắn trước có liên quan. Khi bạn hỏi "Tên tôi là gì?", hệ thống gửi toàn bộ lịch sử hội thoại, cho phép mô hình xem bạn đã nói "Tên tôi là John" trước đó.

LangChain4j cung cấp các cài đặt bộ nhớ xử lý tự động việc này. Bạn chọn số tin nhắn muốn giữ lại và framework quản lý cửa sổ ngữ cảnh.

<img src="../../../translated_images/vi/memory-window.bbe67f597eadabb3.webp" alt="Khái niệm Cửa Sổ Bộ Nhớ" width="800"/>

*MessageWindowChatMemory duy trì cửa sổ trượt của các tin nhắn gần đây, tự động loại bỏ tin nhắn cũ*

## Cách Điều Này Sử Dụng LangChain4j

Module này mở rộng phần khởi động nhanh với Spring Boot và thêm bộ nhớ cuộc hội thoại. Các phần liên kết như sau:

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

**Mô hình Chat** - Cấu hình Azure OpenAI như một bean Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder đọc thông tin xác thực từ biến môi trường do `azd up` thiết lập. Thiết lập `baseUrl` trỏ tới endpoint Azure của bạn giúp client OpenAI hoạt động với Azure OpenAI.

**Bộ nhớ Cuộc hội thoại** - Theo dõi lịch sử chat với MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Tạo bộ nhớ với `withMaxMessages(10)` giữ lại 10 tin nhắn cuối cùng. Thêm tin nhắn người dùng và AI với wrapper kiểu dữ liệu: `UserMessage.from(text)` và `AiMessage.from(text)`. Lấy lịch sử với `memory.messages()` và gửi đến mô hình. Service lưu bộ nhớ riêng cho mỗi ID cuộc hội thoại, cho phép nhiều người dùng trò chuyện cùng lúc.

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) và hỏi:
> - "MessageWindowChatMemory quyết định bỏ tin nhắn nào khi cửa sổ đầy như thế nào?"
> - "Tôi có thể triển khai bộ nhớ tùy chỉnh sử dụng database thay vì bộ nhớ trong không?"
> - "Làm sao để thêm tóm tắt để nén lịch sử hội thoại cũ?"

Điểm cuối chat không trạng thái bỏ qua bộ nhớ hoàn toàn - chỉ gọi `chatModel.chat(prompt)` như phần khởi động nhanh. Điểm cuối chat có trạng thái thêm tin nhắn vào bộ nhớ, lấy lịch sử và bao gồm ngữ cảnh mỗi yêu cầu. Cùng cấu hình mô hình, khác mô hình thao tác.

## Triển Khai Hạ Tầng Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Chọn đăng ký và vị trí (đề xuất eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Chọn đăng ký và vị trí (khuyến nghị eastus2)
```

> **Lưu ý:** Nếu gặp lỗi timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), hãy chạy lại `azd up`. Các tài nguyên Azure có thể đang được tạo ở nền, và thử lại sẽ cho phép triển khai hoàn tất khi tài nguyên đến trạng thái cuối.

Việc này sẽ:
1. Triển khai tài nguyên Azure OpenAI với các mô hình GPT-5.2 và text-embedding-3-small
2. Tự động tạo file `.env` trong thư mục gốc dự án với thông tin xác thực
3. Thiết lập tất cả biến môi trường cần thiết

**Gặp sự cố triển khai?** Xem [README Hạ tầng](infra/README.md) để biết chi tiết sửa lỗi bao gồm xung đột tên subdomain, bước triển khai thủ công qua Azure Portal, và hướng dẫn cấu hình mô hình.

**Xác minh triển khai thành công:**

**Bash:**
```bash
cat ../.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, v.v.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, v.v.
```

> **Lưu ý:** Lệnh `azd up` tự động tạo file `.env`. Nếu cần cập nhật sau, bạn có thể chỉnh sửa thủ công hoặc tạo lại bằng cách chạy:
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

**Xác minh triển khai:**

Đảm bảo file `.env` tồn tại trong thư mục gốc với thông tin xác thực Azure:

**Bash:**
```bash
cat ../.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Khởi động ứng dụng:**

**Lựa chọn 1: Sử dụng Spring Boot Dashboard (Khuyến khích cho người dùng VS Code)**

Dev container bao gồm tiện ích mở rộng Spring Boot Dashboard, cung cấp giao diện trực quan quản lý tất cả ứng dụng Spring Boot. Bạn có thể tìm thấy ở Thanh Hoạt Động bên trái của VS Code (biểu tượng Spring Boot).

Từ Spring Boot Dashboard, bạn có thể:
- Xem tất cả ứng dụng Spring Boot có trong workspace
- Khởi động/dừng ứng dụng chỉ với một lần nhấn
- Xem nhật ký ứng dụng theo thời gian thực
- Giám sát trạng thái ứng dụng

Chỉ cần nhấn nút chạy bên cạnh "introduction" để bắt đầu module này, hoặc khởi động tất cả các module cùng lúc.

<img src="../../../translated_images/vi/dashboard.69c7479aef09ff6b.webp" alt="Bảng điều khiển Spring Boot" width="400"/>

**Lựa chọn 2: Sử dụng shell scripts**

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

Cả hai script tự động tải biến môi trường từ file `.env` gốc và sẽ build JAR nếu chưa tồn tại.

> **Lưu ý:** Nếu bạn muốn build tất cả module thủ công trước khi chạy:
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

Mở http://localhost:8080 trên trình duyệt của bạn.

**Để dừng:**

**Bash:**
```bash
./stop.sh  # Chỉ module này
# Hoặc
cd .. && ./stop-all.sh  # Tất cả các module
```

**PowerShell:**
```powershell
.\stop.ps1  # Chỉ mô-đun này
# Hoặc
cd ..; .\stop-all.ps1  # Tất cả các mô-đun
```

## Sử Dụng Ứng Dụng

Ứng dụng cung cấp giao diện web với hai cài đặt chat đặt cạnh nhau.

<img src="../../../translated_images/vi/home-screen.121a03206ab910c0.webp" alt="Màn hình chính ứng dụng" width="800"/>

*Bảng điều khiển hiển thị cả tùy chọn Chat Đơn Giản (không trạng thái) và Chat Đàm Thoại (có trạng thái)*

### Chat Không Trạng Thái (Bảng Trái)

Hãy thử trước. Hỏi "Tên tôi là John" và ngay lập tức hỏi "Tên tôi là gì?" Mô hình sẽ không nhớ vì mỗi tin nhắn độc lập. Điều này cho thấy vấn đề cốt lõi khi tích hợp mô hình ngôn ngữ cơ bản - không có ngữ cảnh cuộc trò chuyện.

<img src="../../../translated_images/vi/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo Chat Không Trạng Thái" width="800"/>

*AI không nhớ tên bạn từ tin nhắn trước*

### Chat Có Trạng Thái (Bảng Phải)

Giờ thử chuỗi câu tương tự ở đây. Hỏi "Tên tôi là John" rồi "Tên tôi là gì?" Lần này nó nhớ. Bí quyết là MessageWindowChatMemory - nó duy trì lịch sử đàm thoại và bao gồm cùng mỗi yêu cầu. Đây là cách AI đàm thoại sản xuất hoạt động.

<img src="../../../translated_images/vi/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo Chat Có Trạng Thái" width="800"/>

*AI nhớ tên bạn từ trước trong cuộc đàm thoại*

Cả hai bảng đều dùng cùng mô hình GPT-5.2. Khác biệt duy nhất là bộ nhớ. Điều này cho thấy rõ bộ nhớ đem lại gì cho ứng dụng và vì sao nó thiết yếu với các trường hợp sử dụng thực tế.

## Bước Tiếp Theo

**Module Tiếp Theo:** [02-prompt-engineering - Kỹ Thuật Viết Prompt với GPT-5.2](../02-prompt-engineering/README.md)

---

**Điều hướng:** [← Trước đó: Module 00 - Khởi Đầu Nhanh](../00-quick-start/README.md) | [Quay lại Trang Chính](../README.md) | [Tiếp theo: Module 02 - Kỹ Thuật Viết Prompt →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố từ chối trách nhiệm**:
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ gốc của nó nên được coi là nguồn tham khảo chính xác nhất. Đối với những thông tin quan trọng, nên sử dụng dịch thuật chuyên nghiệp bởi con người. Chúng tôi không chịu trách nhiệm cho bất kỳ sự hiểu nhầm hoặc diễn giải sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->