# Module 01: Bắt đầu với LangChain4j

## Mục lục

- [Hướng dẫn qua video](../../../01-introduction)
- [Bạn sẽ học được gì](../../../01-introduction)
- [Yêu cầu trước](../../../01-introduction)
- [Hiểu về vấn đề cốt lõi](../../../01-introduction)
- [Hiểu về Token](../../../01-introduction)
- [Cách bộ nhớ hoạt động](../../../01-introduction)
- [Cách sử dụng LangChain4j](../../../01-introduction)
- [Triển khai hạ tầng Azure OpenAI](../../../01-introduction)
- [Chạy ứng dụng trên máy cục bộ](../../../01-introduction)
- [Sử dụng ứng dụng](../../../01-introduction)
  - [Chat không trạng thái (Bảng bên trái)](../../../01-introduction)
  - [Chat có trạng thái (Bảng bên phải)](../../../01-introduction)
- [Bước tiếp theo](../../../01-introduction)

## Hướng dẫn qua video

Xem phiên trực tiếp này giải thích cách bắt đầu với module này:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Bạn sẽ học được gì

Trong phần khởi động nhanh, bạn đã dùng GitHub Models để gửi prompt, gọi công cụ, xây dựng pipeline RAG và thử nghiệm guardrails. Những demo đó cho thấy những gì có thể làm — bây giờ chúng ta chuyển sang Azure OpenAI và GPT-5.2 để bắt đầu xây dựng các ứng dụng kiểu sản xuất. Module này tập trung vào AI hội thoại có khả năng ghi nhớ ngữ cảnh và duy trì trạng thái — những khái niệm mà các demo khởi động nhanh đã sử dụng phía sau nhưng không giải thích rõ.

Chúng ta sẽ sử dụng GPT-5.2 của Azure OpenAI xuyên suốt hướng dẫn này vì khả năng suy luận nâng cao giúp nhìn rõ hơn cách các mẫu khác nhau hoạt động. Khi thêm bộ nhớ, bạn sẽ thấy sự khác biệt rõ ràng. Điều này giúp dễ dàng hiểu mỗi thành phần mang lại gì cho ứng dụng của bạn.

Bạn sẽ xây dựng một ứng dụng thể hiện cả hai mẫu:

**Chat không trạng thái** - Mỗi yêu cầu là độc lập. Mô hình không nhớ các tin nhắn trước đó. Đây là mẫu bạn đã sử dụng trong khởi động nhanh.

**Hội thoại có trạng thái** - Mỗi yêu cầu bao gồm lịch sử hội thoại. Mô hình duy trì ngữ cảnh qua nhiều lượt. Đây là yêu cầu của các ứng dụng sản xuất.

## Yêu cầu trước

- Tài khoản Azure với quyền truy cập Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Lưu ý:** Java, Maven, Azure CLI và Azure Developer CLI (azd) được cài sẵn trong devcontainer được cung cấp.

> **Lưu ý:** Module này sử dụng GPT-5.2 trên Azure OpenAI. Việc triển khai được cấu hình tự động qua `azd up` — không chỉnh sửa tên mô hình trong mã nguồn.

## Hiểu về vấn đề cốt lõi

Các mô hình ngôn ngữ là không trạng thái. Mỗi lần gọi API là độc lập. Nếu bạn gửi "Tên tôi là John" rồi hỏi "Tên tôi là gì?", mô hình không biết bạn vừa giới thiệu bản thân. Nó coi mỗi yêu cầu như là cuộc hội thoại đầu tiên bạn từng có.

Điều này ổn với các câu hỏi đơn giản nhưng vô dụng với ứng dụng thực tế. Các bot dịch vụ khách hàng cần nhớ thông tin bạn đã nói. Trợ lý cá nhân cần có ngữ cảnh. Bất kỳ cuộc hội thoại nhiều lượt nào cũng cần có bộ nhớ.

Sơ đồ dưới đây so sánh hai cách tiếp cận — bên trái là gọi không trạng thái quên tên bạn; bên phải là gọi có trạng thái với ChatMemory ghi nhớ tên bạn.

<img src="../../../translated_images/vi/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Sự khác biệt giữa hội thoại không trạng thái (gọi độc lập) và hội thoại có trạng thái (nhận biết ngữ cảnh)*

## Hiểu về Token

Trước khi đi sâu vào các cuộc hội thoại, cần hiểu về token - đơn vị cơ bản của văn bản mà các mô hình ngôn ngữ xử lý:

<img src="../../../translated_images/vi/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Ví dụ về cách đoạn văn bản được tách thành các token - "I love AI!" thành 4 đơn vị xử lý riêng biệt*

Token là cách các mô hình AI đo lường và xử lý văn bản. Từ, dấu câu và thậm chí dấu cách đều có thể là token. Mô hình của bạn có giới hạn số token có thể xử lý cùng lúc (400,000 cho GPT-5.2, với tối đa 272,000 token đầu vào và 128,000 token đầu ra). Hiểu token giúp bạn quản lý độ dài hội thoại và chi phí.

## Cách bộ nhớ hoạt động

Bộ nhớ chat giải quyết vấn đề không trạng thái bằng cách duy trì lịch sử hội thoại. Trước khi gửi yêu cầu tới mô hình, khung làm việc thêm vào các tin nhắn trước có liên quan. Khi bạn hỏi "Tên tôi là gì?", hệ thống thực sự gửi toàn bộ lịch sử hội thoại, cho phép mô hình thấy rằng bạn đã nói "Tên tôi là John" trước đó.

LangChain4j cung cấp các cài đặt bộ nhớ tự động xử lý việc này. Bạn chọn số lượng tin nhắn giữ lại và khung làm việc quản lý cửa sổ ngữ cảnh. Sơ đồ dưới đây cho thấy cách MessageWindowChatMemory duy trì một cửa sổ trượt các tin nhắn gần đây.

<img src="../../../translated_images/vi/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory duy trì một cửa sổ trượt các tin nhắn gần đây, tự động loại bỏ tin nhắn cũ*

## Cách sử dụng LangChain4j

Module này mở rộng khởi động nhanh bằng cách tích hợp Spring Boot và thêm bộ nhớ hội thoại. Đây là cách các phần phối hợp với nhau:

**Dependencies** - Thêm hai thư viện LangChain4j:

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

Trình tạo đọc thông tin xác thực từ biến môi trường được thiết lập bởi `azd up`. Thiết lập `baseUrl` trỏ đến endpoint Azure của bạn làm cho client OpenAI hoạt động với Azure OpenAI.

**Bộ nhớ hội thoại** - Theo dõi lịch sử chat với MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Tạo bộ nhớ với `withMaxMessages(10)` để giữ 10 tin nhắn cuối cùng. Thêm tin nhắn người dùng và AI với các wrapper kiểu: `UserMessage.from(text)` và `AiMessage.from(text)`. Lấy lịch sử với `memory.messages()` rồi gửi đến mô hình. Service lưu các phiên bộ nhớ riêng cho từng ID hội thoại, cho phép nhiều người dùng chat đồng thời.

> **🤖 Thử cùng [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) và hỏi:
> - "MessageWindowChatMemory quyết định loại bỏ tin nhắn nào khi cửa sổ đầy như thế nào?"
> - "Tôi có thể triển khai lưu trữ bộ nhớ tùy chỉnh bằng cơ sở dữ liệu thay vì bộ nhớ trong RAM không?"
> - "Làm thế nào để thêm tóm tắt nén lịch sử hội thoại cũ?"

Điểm cuối chat không trạng thái bỏ qua bộ nhớ hoàn toàn — chỉ `chatModel.chat(prompt)` như khởi động nhanh. Điểm cuối có trạng thái thêm tin nhắn vào bộ nhớ, lấy lịch sử và bao gồm ngữ cảnh này với mỗi yêu cầu. Cấu hình mô hình giống nhau, cách tiếp cận khác nhau.

## Triển khai hạ tầng Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Chọn đăng ký và vị trí (khuyến nghị eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Chọn đăng ký và vị trí (khuyến nghị eastus2)
```

> **Lưu ý:** Nếu bạn gặp lỗi timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), chỉ cần chạy lại `azd up`. Các tài nguyên Azure có thể đang được triển khai ở nền và thử lại cho phép quá trình hoàn tất khi tài nguyên đạt trạng thái cuối.

Điều này sẽ:
1. Triển khai tài nguyên Azure OpenAI với các mô hình GPT-5.2 và text-embedding-3-small
2. Tự động tạo file `.env` ở thư mục gốc dự án với thông tin xác thực
3. Thiết lập tất cả biến môi trường cần thiết

**Gặp vấn đề khi triển khai?** Xem [Infra README](infra/README.md) để biết hướng dẫn xử lý chi tiết bao gồm xung đột tên miền phụ, các bước triển khai thủ công qua Azure Portal và hướng dẫn cấu hình mô hình.

**Xác nhận triển khai thành công:**

**Bash:**
```bash
cat ../.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, v.v.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, v.v.
```

> **Lưu ý:** Lệnh `azd up` tự động tạo file `.env`. Nếu cần cập nhật sau, có thể chỉnh sửa thủ công hoặc tạo lại bằng cách chạy:
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

## Chạy ứng dụng trên máy cục bộ

**Xác nhận triển khai:**

Đảm bảo file `.env` tồn tại trong thư mục gốc với thông tin xác thực Azure. Chạy lệnh này từ thư mục module (`01-introduction/`):

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

Dev container bao gồm tiện ích mở rộng Spring Boot Dashboard, cung cấp giao diện trực quan để quản lý tất cả ứng dụng Spring Boot. Bạn có thể tìm nó trong Thanh hoạt động bên trái VS Code (biểu tượng hình lá cờ Spring Boot).

Từ Spring Boot Dashboard, bạn có thể:
- Xem tất cả ứng dụng Spring Boot có trong workspace
- Khởi động/dừng ứng dụng với một cú nhấp chuột
- Xem nhật ký ứng dụng theo thời gian thực
- Giám sát trạng thái ứng dụng

Chỉ cần nhấp nút chạy bên cạnh "introduction" để bắt đầu module này, hoặc chạy tất cả module cùng lúc.

<img src="../../../translated_images/vi/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard trong VS Code — khởi động, dừng và giám sát tất cả module từ một nơi*

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

Cả hai script tự động nạp biến môi trường từ file `.env` gốc và sẽ build file JAR nếu chưa tồn tại.

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

## Sử dụng ứng dụng

Ứng dụng cung cấp giao diện web với hai triển khai chat đặt cạnh nhau.

<img src="../../../translated_images/vi/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Bảng điều khiển hiển thị cả tùy chọn Chat Đơn giản (không trạng thái) và Chat Hội thoại (có trạng thái)*

### Chat không trạng thái (Bảng bên trái)

Thử trước phần này. Hỏi "Tên tôi là John" rồi ngay lập tức hỏi "Tên tôi là gì?" Mô hình sẽ không nhớ vì mỗi tin nhắn là độc lập. Điều này minh họa vấn đề cốt lõi khi tích hợp mô hình ngôn ngữ cơ bản - không có ngữ cảnh hội thoại.

<img src="../../../translated_images/vi/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI không nhớ tên bạn từ tin nhắn trước*

### Chat có trạng thái (Bảng bên phải)

Bây giờ thử lại chuỗi câu hỏi tương tự ở đây. Hỏi "Tên tôi là John" rồi hỏi "Tên tôi là gì?" Lần này nó nhớ. Khác biệt là MessageWindowChatMemory - nó duy trì lịch sử hội thoại và bao gồm ngữ cảnh với mỗi yêu cầu. Đây là cách AI hội thoại trong sản xuất hoạt động.

<img src="../../../translated_images/vi/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI nhớ tên bạn từ trước trong cuộc hội thoại*

Cả hai bảng đều dùng chung mô hình GPT-5.2. Điểm khác biệt duy nhất là bộ nhớ. Điều này làm rõ bộ nhớ mang lại gì cho ứng dụng và tại sao nó quan trọng với các trường hợp sử dụng thực tế.

## Bước tiếp theo

**Module tiếp theo:** [02-prompt-engineering - Kỹ thuật Prompt với GPT-5.2](../02-prompt-engineering/README.md)

---

**Điều hướng:** [← Trước: Module 00 - Khởi động nhanh](../00-quick-start/README.md) | [Quay về chính](../README.md) | [Tiếp: Module 02 - Kỹ thuật Prompt →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố từ chối trách nhiệm**:  
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng các bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ gốc nên được xem là nguồn chính thức. Đối với thông tin quan trọng, nên sử dụng dịch vụ dịch thuật chuyên nghiệp do con người thực hiện. Chúng tôi không chịu trách nhiệm về bất kỳ sự hiểu lầm hay giải thích sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->