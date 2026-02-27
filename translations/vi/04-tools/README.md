# Module 04: Đại lý AI với Công cụ

## Mục Lục

- [Những gì bạn sẽ học](../../../04-tools)
- [Yêu cầu trước](../../../04-tools)
- [Hiểu về Đại lý AI với Công cụ](../../../04-tools)
- [Cách gọi Công cụ hoạt động](../../../04-tools)
  - [Định nghĩa Công cụ](../../../04-tools)
  - [Quyết định](../../../04-tools)
  - [Thực thi](../../../04-tools)
  - [Tạo phản hồi](../../../04-tools)
  - [Kiến trúc: Tự động kết nối Spring Boot](../../../04-tools)
- [Chuỗi Công cụ](../../../04-tools)
- [Chạy Ứng dụng](../../../04-tools)
- [Sử dụng Ứng dụng](../../../04-tools)
  - [Thử sử dụng Công cụ đơn giản](../../../04-tools)
  - [Kiểm tra Chuỗi Công cụ](../../../04-tools)
  - [Xem luồng hội thoại](../../../04-tools)
  - [Thử nghiệm với các yêu cầu khác nhau](../../../04-tools)
- [Khái niệm chính](../../../04-tools)
  - [Mẫu ReAct (Lý luận và Hành động)](../../../04-tools)
  - [Mô tả Công cụ quan trọng](../../../04-tools)
  - [Quản lý Phiên làm việc](../../../04-tools)
  - [Xử lý Lỗi](../../../04-tools)
- [Các Công cụ có sẵn](../../../04-tools)
- [Khi nào nên dùng Đại lý dựa trên Công cụ](../../../04-tools)
- [Công cụ so với RAG](../../../04-tools)
- [Bước tiếp theo](../../../04-tools)

## Những gì bạn sẽ học

Cho đến nay, bạn đã học cách trò chuyện với AI, cấu trúc prompt hiệu quả, và căn cứ các phản hồi vào tài liệu của bạn. Nhưng vẫn còn một giới hạn cơ bản: các mô hình ngôn ngữ chỉ có thể tạo văn bản. Chúng không thể kiểm tra thời tiết, tính toán, truy vấn cơ sở dữ liệu, hay tương tác với hệ thống bên ngoài.

Công cụ thay đổi điều này. Bằng cách cung cấp cho mô hình quyền truy cập vào các chức năng mà nó có thể gọi, bạn biến nó từ một trình tạo văn bản thành một đại lý có thể thực hiện hành động. Mô hình quyết định khi nào cần công cụ, công cụ nào dùng, và truyền tham số gì. Code của bạn sẽ thực thi hàm đó và trả về kết quả. Mô hình tích hợp kết quả đó vào phản hồi của nó.

## Yêu cầu trước

- Hoàn thành Module 01 (đã triển khai tài nguyên Azure OpenAI)
- File `.env` trong thư mục gốc với thông tin xác thực Azure (được tạo bởi `azd up` trong Module 01)

> **Lưu ý:** Nếu bạn chưa hoàn thành Module 01, hãy làm theo hướng dẫn triển khai ở đó trước.

## Hiểu về Đại lý AI với Công cụ

> **📝 Lưu ý:** Thuật ngữ "đại lý" trong module này đề cập đến trợ lý AI được nâng cấp với khả năng gọi công cụ. Điều này khác với các mẫu **Agentic AI** (đại lý tự động với lập kế hoạch, bộ nhớ, và suy luận nhiều bước) mà chúng ta sẽ đề cập trong [Module 05: MCP](../05-mcp/README.md).

Nếu không có công cụ, mô hình ngôn ngữ chỉ có thể tạo văn bản từ dữ liệu huấn luyện. Hỏi nó về thời tiết hiện tại, nó phải đoán. Cung cấp công cụ, nó có thể gọi API thời tiết, thực hiện tính toán hoặc truy vấn cơ sở dữ liệu — rồi dệt kết quả thực tế ấy vào câu trả lời.

<img src="../../../translated_images/vi/what-are-tools.724e468fc4de64da.webp" alt="Không có Công cụ vs Có Công cụ" width="800"/>

*Không có công cụ, mô hình chỉ đoán - có công cụ, nó có thể gọi API, tính toán, và trả dữ liệu thời gian thực.*

Một đại lý AI với công cụ theo mẫu **Lý luận và Hành động (ReAct)**. Mô hình không chỉ phản hồi — nó suy nghĩ về những gì cần, hành động bằng cách gọi công cụ, quan sát kết quả, rồi quyết định có hành động thêm hay trả lời cuối cùng:

1. **Lý luận** — Đại lý phân tích câu hỏi của người dùng và xác định thông tin cần thiết
2. **Hành động** — Đại lý chọn công cụ đúng, tạo tham số phù hợp, và gọi nó
3. **Quan sát** — Đại lý nhận kết quả từ công cụ và đánh giá
4. **Lặp lại hoặc Phản hồi** — Nếu cần thêm dữ liệu, đại lý lặp lại; nếu không thì tổng hợp câu trả lời tự nhiên

<img src="../../../translated_images/vi/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Mẫu ReAct" width="800"/>

*Chu kỳ ReAct — đại lý suy nghĩ phải làm gì, hành động gọi công cụ, quan sát kết quả, và lặp lại đến khi có câu trả lời cuối.*

Điều này xảy ra tự động. Bạn định nghĩa công cụ và mô tả của chúng. Mô hình xử lý phần quyết định khi nào và cách dùng.

## Cách gọi Công cụ hoạt động

### Định nghĩa Công cụ

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Bạn định nghĩa các hàm với mô tả rõ ràng và thông số cụ thể. Mô hình nhìn thấy mô tả này trong prompt hệ thống và hiểu công dụng mỗi công cụ.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Logic tra cứu thời tiết của bạn
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Trợ lý được tự động kết nối bởi Spring Boot với:
// - Bean ChatModel
// - Tất cả các phương thức @Tool từ các lớp @Component
// - ChatMemoryProvider để quản lý phiên làm việc
```

Sơ đồ dưới đây phân tích kỹ từng chú thích và cho thấy mỗi phần giúp AI hiểu khi nào gọi công cụ và truyền tham số gì:

<img src="../../../translated_images/vi/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Cấu trúc Định nghĩa Công cụ" width="800"/>

*Cấu trúc một định nghĩa công cụ — @Tool nói AI khi nào dùng, @P mô tả từng tham số, và @AiService kết nối tất cả lúc khởi động.*

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) và hỏi:
> - "Làm sao tích hợp API thời tiết thật như OpenWeatherMap thay vì dữ liệu giả?"
> - "Một mô tả công cụ tốt giúp AI dùng đúng cách như thế nào?"
> - "Làm sao xử lý lỗi API và giới hạn tần suất trong triển khai công cụ?"

### Quyết định

Khi người dùng hỏi "Thời tiết ở Seattle thế nào?", mô hình không chọn ngẫu nhiên công cụ. Nó so sánh ý định người dùng với mọi mô tả công cụ có, chấm điểm mức liên quan và chọn công cụ phù hợp nhất. Sau đó nó tạo cuộc gọi hàm có cấu trúc với tham số đúng — trong trường hợp này, đặt `location` là `"Seattle"`.

Nếu không có công cụ nào phù hợp với yêu cầu, mô hình trả lời dựa trên kiến thức của nó. Nếu nhiều công cụ phù hợp, nó chọn công cụ cụ thể nhất.

<img src="../../../translated_images/vi/decision-making.409cd562e5cecc49.webp" alt="Cách AI Quyết định Công cụ dùng" width="800"/>

*Mô hình đánh giá từng công cụ so với ý định người dùng và chọn công cụ tốt nhất — vì vậy mô tả công cụ rõ ràng và cụ thể rất quan trọng.*

### Thực thi

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot tự động kết nối giao diện khai báo `@AiService` với tất cả công cụ đã đăng ký, và LangChain4j tự động thực thi các cuộc gọi công cụ. Ở hậu trường, một cuộc gọi công cụ hoàn chỉnh trải qua sáu giai đoạn — từ câu hỏi ngôn ngữ tự nhiên của người dùng đến câu trả lời ngôn ngữ tự nhiên:

<img src="../../../translated_images/vi/tool-calling-flow.8601941b0ca041e6.webp" alt="Luồng Gọi Công cụ" width="800"/>

*Luồng end-to-end — người dùng hỏi, mô hình chọn công cụ, LangChain4j thực thi, mô hình dệt kết quả vào câu trả lời.*

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) và hỏi:
> - "Mẫu ReAct hoạt động thế nào và tại sao hiệu quả với đại lý AI?"
> - "Đại lý quyết định công cụ nào dùng và theo thứ tự ra sao?"
> - "Nếu thực thi công cụ thất bại thì sao - làm sao xử lý lỗi hiệu quả?"

### Tạo phản hồi

Mô hình nhận dữ liệu thời tiết và định dạng thành phản hồi ngôn ngữ tự nhiên cho người dùng.

### Kiến trúc: Tự động kết nối Spring Boot

Module này sử dụng tích hợp LangChain4j với Spring Boot qua giao diện khai báo `@AiService`. Khi khởi động, Spring Boot phát hiện mọi `@Component` chứa phương thức `@Tool`, bean `ChatModel` của bạn, và `ChatMemoryProvider` — rồi kết nối tất cả thành một giao diện `Assistant` duy nhất không cần boilerplate.

<img src="../../../translated_images/vi/spring-boot-wiring.151321795988b04e.webp" alt="Kiến trúc Tự động kết nối Spring Boot" width="800"/>

*Giao diện @AiService liên kết ChatModel, thành phần công cụ, và nhà cung cấp bộ nhớ — Spring Boot tự xử lý kết nối.*

Lợi ích chính của phương pháp này:

- **Tự động kết nối Spring Boot** — ChatModel và các công cụ tự động được tiêm
- **Mẫu @MemoryId** — Quản lý bộ nhớ theo phiên tự động
- **Một thể hiện duy nhất** — Assistant tạo một lần và tái sử dụng để tăng hiệu suất
- **Thực thi an toàn kiểu** — Gọi phương thức Java trực tiếp với chuyển đổi kiểu
- **Điều phối đa lượt** — Tự động xử lý chuỗi công cụ
- **Không boilerplate** — Không cần gọi thủ công `AiServices.builder()` hay HashMap bộ nhớ

Cách làm thủ công (`AiServices.builder()`) yêu cầu nhiều code hơn và không tận dụng lợi ích tích hợp Spring Boot.

## Chuỗi Công cụ

**Chuỗi Công cụ** — Sức mạnh thực sự của đại lý dựa trên công cụ thể hiện khi một câu hỏi cần nhiều công cụ. Hỏi "Thời tiết ở Seattle bằng độ Fahrenheit là bao nhiêu?" và đại lý tự động chuỗi hai công cụ: trước tiên gọi `getCurrentWeather` để lấy nhiệt độ Celsius, sau đó truyền giá trị đó sang `celsiusToFahrenheit` để chuyển đổi — tất cả trong một lượt trò chuyện.

<img src="../../../translated_images/vi/tool-chaining-example.538203e73d09dd82.webp" alt="Ví dụ Chuỗi Công cụ" width="800"/>

*Chuỗi công cụ trong thực tế — đại lý gọi getCurrentWeather trước, rồi truyền kết quả Celsius vào celsiusToFahrenheit, và đưa ra câu trả lời kết hợp.*

Đây là cách nó hoạt động trong ứng dụng thực tế — đại lý chuỗi gọi hai công cụ trong một lượt hội thoại:

<a href="images/tool-chaining.png"><img src="../../../translated_images/vi/tool-chaining.3b25af01967d6f7b.webp" alt="Chuỗi Công cụ" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Phản hồi thực tế của ứng dụng — đại lý tự động chuỗi getCurrentWeather → celsiusToFahrenheit trong một lượt.*

**Xử lý lỗi khéo léo** — Hỏi thời tiết ở thành phố không có trong dữ liệu giả. Công cụ trả về thông báo lỗi, AI giải thích không thể giúp thay vì bị lỗi. Công cụ thất bại an toàn.

<img src="../../../translated_images/vi/error-handling-flow.9a330ffc8ee0475c.webp" alt="Luồng Xử lý Lỗi" width="800"/>

*Khi công cụ lỗi, đại lý bắt lỗi và phản hồi giải thích hữu ích thay vì sập.*

Điều này xảy ra trong một lượt hội thoại duy nhất. Đại lý tự điều phối nhiều cuộc gọi công cụ.

## Chạy Ứng dụng

**Xác minh triển khai:**

Đảm bảo file `.env` tồn tại tại thư mục gốc với thông tin xác thực Azure (được tạo trong Module 01):
```bash
cat ../.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Khởi động ứng dụng:**

> **Lưu ý:** Nếu bạn đã khởi động tất cả ứng dụng bằng `./start-all.sh` từ Module 01, module này đã chạy trên cổng 8084. Bạn có thể bỏ qua lệnh khởi động bên dưới và truy cập trực tiếp http://localhost:8084.

**Lựa chọn 1: Dùng Spring Boot Dashboard (Khuyến nghị cho người dùng VS Code)**

Container phát triển bao gồm phần mở rộng Spring Boot Dashboard, cung cấp giao diện quản lý tất cả ứng dụng Spring Boot. Bạn có thể tìm thấy nó trên Thanh hoạt động bên trái VS Code (tìm biểu tượng Spring Boot).

Từ Spring Boot Dashboard, bạn có thể:
- Xem tất cả ứng dụng Spring Boot có trong workspace
- Khởi động/dừng ứng dụng chỉ với một cú nhấp
- Xem nhật ký ứng dụng theo thời gian thực
- Giám sát trạng thái ứng dụng

Chỉ cần nhấp nút chạy bên cạnh "tools" để khởi động module này, hoặc khởi động tất cả module cùng lúc.

<img src="../../../translated_images/vi/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

**Lựa chọn 2: Dùng script shell**

Khởi động tất cả ứng dụng web (modules 01-04):

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

Hoặc chỉ khởi động riêng module này:

**Bash:**  
```bash
cd 04-tools
./start.sh
```

**PowerShell:**  
```powershell
cd 04-tools
.\start.ps1
```

Cả hai script tự động tải biến môi trường từ file `.env` ở thư mục gốc và sẽ build các file JAR nếu chưa có.

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

Mở http://localhost:8084 trên trình duyệt của bạn.

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

## Sử dụng Ứng dụng

Ứng dụng cung cấp giao diện web nơi bạn có thể tương tác với đại lý AI có quyền truy cập công cụ thời tiết và chuyển đổi nhiệt độ.

<a href="images/tools-homepage.png"><img src="../../../translated_images/vi/tools-homepage.4b4cd8b2717f9621.webp" alt="Giao diện Công cụ Đại lý AI" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Giao diện Công cụ Đại lý AI - các ví dụ nhanh và giao diện chat để tương tác với công cụ*

### Thử sử dụng Công cụ đơn giản
Bắt đầu với một yêu cầu đơn giản: "Chuyển 100 độ Fahrenheit sang Celsius". Đại lý nhận ra nó cần công cụ chuyển đổi nhiệt độ, gọi công cụ với các tham số đúng và trả về kết quả. Hãy chú ý cách mà điều này diễn ra tự nhiên – bạn không cần chỉ định công cụ nào để sử dụng hay cách gọi nó.

### Kiểm tra Chuỗi công cụ

Bây giờ thử một câu hỏi phức tạp hơn: "Thời tiết ở Seattle thế nào và chuyển nó sang Fahrenheit?" Hãy quan sát đại lý thực hiện qua các bước. Nó đầu tiên lấy thông tin thời tiết (trả về độ C), nhận ra cần chuyển đổi sang độ F, gọi công cụ chuyển đổi, và kết hợp cả hai kết quả thành một phản hồi duy nhất.

### Xem Luồng Hội thoại

Giao diện chat duy trì lịch sử hội thoại, cho phép bạn có các tương tác nhiều lượt. Bạn có thể xem tất cả các truy vấn và phản hồi trước đó, giúp dễ dàng theo dõi cuộc trò chuyện và hiểu cách đại lý xây dựng ngữ cảnh qua nhiều lượt trao đổi.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/vi/tools-conversation-demo.89f2ce9676080f59.webp" alt="Cuộc hội thoại với nhiều lần gọi công cụ" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Cuộc hội thoại nhiều lượt cho thấy các chuyển đổi đơn giản, tra cứu thời tiết, và chuỗi công cụ*

### Thử nghiệm với các Yêu cầu Khác nhau

Thử các kết hợp đa dạng:
- Tra cứu thời tiết: "Thời tiết ở Tokyo thế nào?"
- Chuyển đổi nhiệt độ: "25°C bằng bao nhiêu Kelvin?"
- Câu hỏi kết hợp: "Kiểm tra thời tiết ở Paris và cho tôi biết nếu nó trên 20°C"

Hãy chú ý cách đại lý diễn giải ngôn ngữ tự nhiên và ánh xạ nó tới các gọi công cụ phù hợp.

## Các Khái niệm Chính

### Mô hình ReAct (Lý luận và Hành động)

Đại lý luân phiên giữa lý luận (quyết định phải làm gì) và hành động (sử dụng công cụ). Mô hình này cho phép giải quyết vấn đề tự động thay vì chỉ phản hồi theo chỉ dẫn.

### Mô tả Công cụ Quan trọng

Chất lượng mô tả công cụ ảnh hưởng trực tiếp đến cách đại lý sử dụng chúng. Mô tả rõ ràng, cụ thể giúp mô hình hiểu khi nào và làm thế nào để gọi từng công cụ.

### Quản lý Phiên làm việc

Chú thích `@MemoryId` cho phép quản lý bộ nhớ theo phiên tự động. Mỗi ID phiên có một thể hiện `ChatMemory` riêng do bean `ChatMemoryProvider` quản lý, nên nhiều người dùng có thể tương tác với đại lý cùng lúc mà không bị trộn lẫn cuộc trò chuyện.

<img src="../../../translated_images/vi/session-management.91ad819c6c89c400.webp" alt="Quản lý Phiên làm việc với @MemoryId" width="800"/>

*Mỗi ID phiên tương ứng với một lịch sử hội thoại riêng biệt — người dùng không bao giờ thấy tin nhắn của nhau.*

### Xử lý Lỗi

Công cụ có thể thất bại — API hết thời gian chờ, tham số không hợp lệ, dịch vụ bên ngoài gặp sự cố. Đại lý sản xuất cần xử lý lỗi để mô hình có thể giải thích vấn đề hoặc thử phương án khác thay vì làm hỏng toàn bộ ứng dụng. Khi công cụ ném ra ngoại lệ, LangChain4j bắt lỗi đó và truyền thông báo lỗi lại cho mô hình, từ đó mô hình có thể giải thích vấn đề bằng ngôn ngữ tự nhiên.

## Các Công cụ Có sẵn

Sơ đồ dưới đây cho thấy hệ sinh thái rộng lớn của các công cụ bạn có thể xây dựng. Module này minh họa các công cụ thời tiết và nhiệt độ, nhưng cùng mẫu `@Tool` cũng áp dụng cho bất kỳ phương thức Java nào — từ truy vấn cơ sở dữ liệu đến xử lý thanh toán.

<img src="../../../translated_images/vi/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Hệ sinh thái Công cụ" width="800"/>

*Mọi phương thức Java được chú thích @Tool đều có thể sử dụng bởi AI — mẫu này mở rộng tới cơ sở dữ liệu, API, email, thao tác file, và nhiều hơn nữa.*

## Khi nào nên dùng Đại lý dựa trên Công cụ

<img src="../../../translated_images/vi/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Khi nào nên dùng Công cụ" width="800"/>

*Một hướng dẫn ra quyết định nhanh — công cụ dùng cho dữ liệu thời gian thực, tính toán và hành động; kiến thức chung và nhiệm vụ sáng tạo thì không cần.*

**Dùng công cụ khi:**
- Trả lời yêu cầu dữ liệu thời gian thực (thời tiết, giá cổ phiếu, tồn kho)
- Cần thực hiện các phép tính vượt quá toán học đơn giản
- Truy cập cơ sở dữ liệu hoặc API
- Thực hiện hành động (gửi email, tạo ticket, cập nhật bản ghi)
- Kết hợp nhiều nguồn dữ liệu

**Không dùng công cụ khi:**
- Câu hỏi có thể trả lời bằng kiến thức chung
- Phản hồi chỉ mang tính hội thoại
- Độ trễ của công cụ làm trải nghiệm quá chậm

## Công cụ so với RAG

Modules 03 và 04 đều mở rộng khả năng của AI, nhưng theo cách hoàn toàn khác nhau. RAG cho mô hình truy cập **kiến thức** qua việc truy xuất tài liệu. Công cụ cho mô hình khả năng thực hiện **hành động** bằng cách gọi hàm.

<img src="../../../translated_images/vi/tools-vs-rag.ad55ce10d7e4da87.webp" alt="So sánh Công cụ và RAG" width="800"/>

*RAG truy xuất thông tin từ tài liệu tĩnh — Công cụ thực thi hành động và lấy dữ liệu động, thời gian thực. Nhiều hệ thống sản xuất kết hợp cả hai.*

Trong thực tế, nhiều hệ thống sản xuất kết hợp cả hai cách tiếp cận: RAG để củng cố câu trả lời dựa trên tài liệu của bạn, và Công cụ để lấy dữ liệu trực tiếp hoặc thực hiện thao tác.

## Các bước tiếp theo

**Module tiếp theo:** [05-mcp - Giao thức Ngữ cảnh Mô hình (MCP)](../05-mcp/README.md)

---

**Điều hướng:** [← Trước: Module 03 - RAG](../03-rag/README.md) | [Quay về chính](../README.md) | [Tiếp theo: Module 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố miễn trừ trách nhiệm**:
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng các bản dịch tự động có thể chứa lỗi hoặc thông tin không chính xác. Tài liệu gốc bằng ngôn ngữ nguyên bản nên được coi là nguồn tham khảo chính thức. Đối với các thông tin quan trọng, nên sử dụng dịch vụ dịch thuật chuyên nghiệp do con người thực hiện. Chúng tôi không chịu trách nhiệm về bất kỳ sự hiểu lầm hay diễn giải sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->