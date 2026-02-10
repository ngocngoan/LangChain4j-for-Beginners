# Module 01: Bắt Đầu với LangChain4j

## Mục Lục

- [Bạn Sẽ Học Được Gì](../../../01-introduction)
- [Các Yêu Cầu Tiên Quyết](../../../01-introduction)
- [Hiểu Vấn Đề Cốt Lõi](../../../01-introduction)
- [Hiểu về Token](../../../01-introduction)
- [Cách Bộ Nhớ Hoạt Động](../../../01-introduction)
- [Cách Đây Sử Dụng LangChain4j](../../../01-introduction)
- [Triển Khai Hạ Tầng Azure OpenAI](../../../01-introduction)
- [Chạy Ứng Dụng Cục Bộ](../../../01-introduction)
- [Sử Dụng Ứng Dụng](../../../01-introduction)
  - [Chat Không Bộ Nhớ (Bảng Bên Trái)](../../../01-introduction)
  - [Chat Có Bộ Nhớ (Bảng Bên Phải)](../../../01-introduction)
- [Các Bước Tiếp Theo](../../../01-introduction)

## Bạn Sẽ Học Được Gì

Nếu bạn đã hoàn thành phần bắt đầu nhanh, bạn đã thấy cách gửi yêu cầu và nhận phản hồi. Đó là nền tảng, nhưng các ứng dụng thực tế cần nhiều hơn thế. Module này sẽ dạy bạn cách xây dựng AI hội thoại nhớ ngữ cảnh và duy trì trạng thái - sự khác biệt giữa một bản demo một lần và một ứng dụng sẵn sàng sản xuất.

Chúng ta sẽ sử dụng GPT-5.2 của Azure OpenAI trong suốt hướng dẫn này vì khả năng suy luận nâng cao giúp làm rõ hành vi của các mẫu khác nhau. Khi bạn thêm bộ nhớ, bạn sẽ thấy rõ sự khác biệt. Điều này giúp dễ hiểu hơn về vai trò của mỗi thành phần trong ứng dụng của bạn.

Bạn sẽ xây dựng một ứng dụng minh họa cả hai mẫu:

**Chat Không Bộ Nhớ** - Mỗi yêu cầu độc lập. Mô hình không nhớ các tin nhắn trước. Đây là mẫu bạn đã dùng trong phần bắt đầu nhanh.

**Cuộc Hội Thoại Có Bộ Nhớ** - Mỗi yêu cầu bao gồm lịch sử hội thoại. Mô hình duy trì ngữ cảnh qua nhiều lượt trò chuyện. Đây là điều các ứng dụng sản xuất cần.

## Các Yêu Cầu Tiên Quyết

- Tài khoản Azure với quyền truy cập Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Lưu ý:** Java, Maven, Azure CLI và Azure Developer CLI (azd) đã được cài sẵn trong devcontainer cung cấp.

> **Lưu ý:** Module này sử dụng GPT-5.2 trên Azure OpenAI. Việc triển khai được cấu hình tự động qua `azd up` - không chỉnh sửa tên mô hình trong mã.

## Hiểu Vấn Đề Cốt Lõi

Các mô hình ngôn ngữ không có trạng thái. Mỗi cuộc gọi API độc lập. Nếu bạn gửi "Tên tôi là John" rồi hỏi "Tên tôi là gì?", mô hình sẽ không biết bạn vừa giới thiệu tên. Nó xử lý mỗi yêu cầu như là cuộc trò chuyện đầu tiên bạn từng có.

Điều này ổn cho những câu hỏi đơn giản nhưng không dùng được cho ứng dụng thực tế. Bot hỗ trợ khách hàng cần nhớ những gì bạn nói. Trợ lý cá nhân cần ngữ cảnh. Bất cứ cuộc hội thoại đa lượt nào cũng cần bộ nhớ.

<img src="../../../translated_images/vi/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Sự khác biệt giữa hội thoại không trạng thái (gọi độc lập) và có trạng thái (có nhận thức ngữ cảnh)*

## Hiểu về Token

Trước khi đi sâu vào hội thoại, bạn cần hiểu token - đơn vị cơ bản của văn bản mà mô hình ngôn ngữ xử lý:

<img src="../../../translated_images/vi/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Ví dụ cách văn bản được chia thành các token - "I love AI!" trở thành 4 đơn vị xử lý riêng biệt*

Token là cách các mô hình AI đo lường và xử lý văn bản. Từ, dấu câu, thậm chí khoảng trắng cũng có thể là token. Mô hình của bạn có giới hạn số token có thể xử lý cùng lúc (400,000 với GPT-5.2, gồm tối đa 272,000 token đầu vào và 128,000 token đầu ra). Hiểu token giúp bạn quản lý độ dài hội thoại và chi phí.

## Cách Bộ Nhớ Hoạt Động

Bộ nhớ chat giải quyết vấn đề không trạng thái bằng cách duy trì lịch sử hội thoại. Trước khi gửi yêu cầu tới mô hình, khung làm việc sẽ thêm vào các tin nhắn trước đó có liên quan. Khi bạn hỏi "Tên tôi là gì?", hệ thống thực sự gửi toàn bộ lịch sử cuộc trò chuyện, cho phép mô hình thấy bạn đã nói "Tên tôi là John" trước đó.

LangChain4j cung cấp các triển khai bộ nhớ xử lý việc này tự động. Bạn chọn số tin nhắn cần giữ và khung tự quản lý cửa sổ ngữ cảnh.

<img src="../../../translated_images/vi/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory duy trì cửa sổ trượt các tin nhắn gần đây, tự động loại bỏ các tin nhắn cũ*

## Cách Đây Sử Dụng LangChain4j

Module này mở rộng phần bắt đầu nhanh bằng cách tích hợp Spring Boot và thêm bộ nhớ hội thoại. Các thành phần kết hợp như sau:

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
  
**Mô Hình Chat** - Cấu hình Azure OpenAI dưới dạng bean Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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
  
Trình xây dựng đọc thông tin đăng nhập từ biến môi trường được thiết lập bởi `azd up`. Việc đặt `baseUrl` tới endpoint Azure của bạn khiến client OpenAI làm việc cùng Azure OpenAI.

**Bộ Nhớ Hội Thoại** - Theo dõi lịch sử chat với MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```
  
Tạo bộ nhớ với `withMaxMessages(10)` để giữ 10 tin nhắn cuối. Thêm tin nhắn người dùng và AI bằng lớp đóng gói kiểu `UserMessage.from(text)` và `AiMessage.from(text)`. Lấy lại lịch sử với `memory.messages()` và gửi cho mô hình. Dịch vụ lưu trữ bộ nhớ riêng cho từng ID cuộc hội thoại, cho phép nhiều người dùng chat cùng lúc.

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) và hỏi:
> - "MessageWindowChatMemory quyết định loại tin nhắn nào khi cửa sổ đầy bằng cách nào?"
> - "Tôi có thể cài bộ nhớ tùy chỉnh dùng cơ sở dữ liệu thay vì trong bộ nhớ không?"
> - "Làm sao để thêm tóm tắt để nén lịch sử hội thoại cũ?"

Điểm cuối chat không trạng thái bỏ qua bộ nhớ hoàn toàn - chỉ `chatModel.chat(prompt)` như phần bắt đầu nhanh. Điểm cuối có trạng thái thêm tin nhắn vào bộ nhớ, lấy lịch sử, và bao gồm ngữ cảnh đó trong mỗi yêu cầu. Cấu hình mô hình giống nhau, mẫu dùng khác nhau.

## Triển Khai Hạ Tầng Azure OpenAI

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
  
> **Lưu ý:** Nếu gặp lỗi hết giờ (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), chỉ cần chạy lại `azd up`. Các tài nguyên Azure có thể vẫn đang được thiết lập nền, việc thử lại cho phép triển khai hoàn thành khi tài nguyên ở trạng thái cuối.

Việc này sẽ:
1. Triển khai tài nguyên Azure OpenAI với các mô hình GPT-5.2 và text-embedding-3-small  
2. Tự động tạo file `.env` trong thư mục gốc dự án với thông tin đăng nhập  
3. Thiết lập tất cả biến môi trường cần thiết  

**Gặp sự cố triển khai?** Xem [README Hạ Tầng](infra/README.md) để biết giải pháp chi tiết, bao gồm trùng tên miền phụ, các bước triển khai thủ công trên Azure Portal, và hướng dẫn cấu hình mô hình.

**Xác minh việc triển khai thành công:**

**Bash:**  
```bash
cat ../.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, v.v.
```
  
**PowerShell:**  
```powershell
Get-Content ..\.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, v.v.
```
  
> **Lưu ý:** Lệnh `azd up` tự động tạo file `.env`. Nếu cần cập nhật sau, bạn có thể chỉnh sửa file `.env` thủ công hoặc tạo lại bằng lệnh:
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

Dev container bao gồm tiện ích mở rộng Spring Boot Dashboard cung cấp giao diện trực quan quản lý tất cả ứng dụng Spring Boot. Bạn có thể tìm thấy nó trong Thanh Hoạt Động ở bên trái VS Code (biểu tượng Spring Boot).

Từ Spring Boot Dashboard, bạn có thể:
- Xem tất cả ứng dụng Spring Boot có trong workspace  
- Khởi động/dừng ứng dụng chỉ với một cú nhấp  
- Xem nhật ký ứng dụng theo thời gian thực  
- Giám sát trạng thái ứng dụng  

Chỉ cần nhấn nút chạy bên cạnh "introduction" để khởi động module này, hoặc khởi động tất cả các module cùng lúc.

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
  
Cả hai script tự động tải biến môi trường từ file `.env` gốc và sẽ build JAR nếu chưa tồn tại.

> **Lưu ý:** Nếu muốn build thủ công tất cả module trước khi chạy:
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

**Dừng ứng dụng:**

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
  

## Sử Dụng Ứng Dụng

Ứng dụng cung cấp giao diện web với hai chế độ chat cạnh nhau.

<img src="../../../translated_images/vi/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Bảng điều khiển hiển thị cả hai tùy chọn Chat Đơn Giản (không trạng thái) và Chat Hội Thoại (có trạng thái)*

### Chat Không Bộ Nhớ (Bảng Bên Trái)

Thử trước. Hỏi "Tên tôi là John" rồi ngay lập tức hỏi "Tên tôi là gì?" Mô hình sẽ không nhớ vì mỗi tin nhắn độc lập. Điều này minh họa vấn đề cốt lõi của tích hợp mô hình ngôn ngữ cơ bản - không có ngữ cảnh hội thoại.

<img src="../../../translated_images/vi/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI không nhớ tên bạn từ tin nhắn trước*

### Chat Có Bộ Nhớ (Bảng Bên Phải)

Bây giờ thử chuỗi câu hỏi tương tự tại đây. Hỏi "Tên tôi là John" rồi "Tên tôi là gì?" Lần này nó nhớ. Khác biệt là MessageWindowChatMemory - nó duy trì lịch sử hội thoại và gửi kèm trong mỗi yêu cầu. Đây là cách AI hội thoại sản xuất hoạt động.

<img src="../../../translated_images/vi/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI nhớ tên bạn từ trước trong cuộc trò chuyện*

Cả hai bảng dùng chung mô hình GPT-5.2. Khác biệt duy nhất là bộ nhớ. Điều này làm rõ bộ nhớ mang lại gì cho ứng dụng của bạn và tại sao nó cần thiết cho trường hợp sử dụng thực tế.

## Các Bước Tiếp Theo

**Module tiếp theo:** [02-prompt-engineering - Kỹ Thuật Lập Prompt với GPT-5.2](../02-prompt-engineering/README.md)

---

**Điều hướng:** [← Trước: Module 00 - Bắt Đầu Nhanh](../00-quick-start/README.md) | [Quay Lại Chính](../README.md) | [Tiếp: Module 02 - Kỹ Thuật Lập Prompt →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố miễn trách nhiệm**:
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo tính chính xác, xin lưu ý rằng các bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ nguyên thủy nên được coi là nguồn chính xác và đáng tin cậy. Đối với những thông tin quan trọng, nên sử dụng dịch vụ dịch thuật chuyên nghiệp do con người thực hiện. Chúng tôi không chịu trách nhiệm về bất kỳ sự hiểu lầm hay diễn giải sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->