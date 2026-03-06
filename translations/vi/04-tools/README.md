# Module 04: Đại lý AI với công cụ

## Mục lục

- [Bạn sẽ học được gì](../../../04-tools)
- [Yêu cầu trước](../../../04-tools)
- [Hiểu về Đại lý AI với công cụ](../../../04-tools)
- [Cách hoạt động gọi công cụ](../../../04-tools)
  - [Định nghĩa công cụ](../../../04-tools)
  - [Quyết định](../../../04-tools)
  - [Thực thi](../../../04-tools)
  - [Tạo phản hồi](../../../04-tools)
  - [Kiến trúc: Spring Boot tự động kết nối](../../../04-tools)
- [Chuỗi công cụ](../../../04-tools)
- [Chạy ứng dụng](../../../04-tools)
- [Sử dụng ứng dụng](../../../04-tools)
  - [Thử sử dụng công cụ đơn giản](../../../04-tools)
  - [Kiểm tra chuỗi công cụ](../../../04-tools)
  - [Xem luồng hội thoại](../../../04-tools)
  - [Thử nghiệm với các yêu cầu khác nhau](../../../04-tools)
- [Các khái niệm chính](../../../04-tools)
  - [Mẫu ReAct (Lý luận và Hành động)](../../../04-tools)
  - [Mô tả công cụ quan trọng](../../../04-tools)
  - [Quản lý phiên làm việc](../../../04-tools)
  - [Xử lý lỗi](../../../04-tools)
- [Công cụ có sẵn](../../../04-tools)
- [Khi nào nên sử dụng đại lý dựa trên công cụ](../../../04-tools)
- [Công cụ so với RAG](../../../04-tools)
- [Bước tiếp theo](../../../04-tools)

## Bạn sẽ học được gì

Cho đến nay, bạn đã học cách trò chuyện với AI, cấu trúc prompt hiệu quả và nền tảng hóa phản hồi trong tài liệu của bạn. Nhưng vẫn còn một hạn chế cơ bản: các mô hình ngôn ngữ chỉ có thể tạo văn bản. Chúng không thể kiểm tra thời tiết, thực hiện tính toán, truy vấn cơ sở dữ liệu hoặc tương tác với các hệ thống bên ngoài.

Công cụ thay đổi điều này. Bằng cách cho mô hình quyền truy cập các hàm nó có thể gọi, bạn biến nó từ một trình tạo văn bản thành đại lý có thể thực hiện hành động. Mô hình quyết định khi nào cần công cụ, công cụ nào sử dụng, và tham số gì truyền vào. Mã của bạn thực thi hàm và trả kết quả lại. Mô hình tích hợp kết quả đó vào phản hồi.

## Yêu cầu trước

- Hoàn thành [Module 01 - Giới thiệu](../01-introduction/README.md) (đã triển khai tài nguyên Azure OpenAI)
- Khuyến nghị hoàn thành các modules trước (module này tham chiếu [khái niệm RAG từ Module 03](../03-rag/README.md) trong phần so sánh Công cụ vs RAG)
- Có tệp `.env` trong thư mục gốc với thông tin xác thực Azure (được tạo bởi lệnh `azd up` trong Module 01)

> **Lưu ý:** Nếu bạn chưa hoàn thành Module 01, vui lòng làm theo hướng dẫn triển khai ở đó trước.

## Hiểu về Đại lý AI với công cụ

> **📝 Ghi chú:** Thuật ngữ "đại lý" trong module này chỉ trợ lý AI được nâng cấp với khả năng gọi công cụ. Điều này khác với các mẫu **Agentic AI** (đại lý tự chủ với lập kế hoạch, lưu trữ và lý luận đa bước) mà chúng ta sẽ trình bày trong [Module 05: MCP](../05-mcp/README.md).

Không có công cụ, mô hình ngôn ngữ chỉ có thể tạo văn bản dựa trên dữ liệu huấn luyện. Hỏi nó về thời tiết hiện tại, nó phải đoán. Cung cấp công cụ, nó có thể gọi API thời tiết, tính toán, hoặc truy vấn cơ sở dữ liệu — rồi kết hợp kết quả thực tế vào câu trả lời.

<img src="../../../translated_images/vi/what-are-tools.724e468fc4de64da.webp" alt="Không có Công cụ vs Có Công cụ" width="800"/>

*Không có công cụ, mô hình chỉ đoán — có công cụ, mô hình có thể gọi API, chạy phép tính, và trả dữ liệu thời gian thực.*

Đại lý AI với công cụ tuân theo mẫu **Lý luận và Hành động (ReAct)**. Mô hình không chỉ trả lời — nó suy nghĩ về nhu cầu, hành động bằng cách gọi công cụ, quan sát kết quả, rồi quyết định xem có tiếp tục hay trả lời cuối cùng:

1. **Lý luận** — Đại lý phân tích câu hỏi người dùng và xác định thông tin cần thiết
2. **Hành động** — Đại lý chọn công cụ phù hợp, tạo tham số đúng và gọi công cụ đó
3. **Quan sát** — Đại lý nhận dữ liệu đầu ra công cụ và đánh giá kết quả
4. **Lặp lại hoặc Trả lời** — Nếu cần thông tin thêm, lặp lại; nếu không, tạo câu trả lời tự nhiên

<img src="../../../translated_images/vi/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Mẫu ReAct" width="800"/>

*Chu trình ReAct — đại lý suy nghĩ việc làm gì, hành động bằng gọi công cụ, quan sát kết quả, và lặp lại cho đến khi có câu trả lời cuối.*

Điều này diễn ra tự động. Bạn định nghĩa công cụ và mô tả của chúng. Mô hình đảm nhiệm việc đưa ra quyết định khi nào và cách sử dụng.

## Cách hoạt động gọi công cụ

### Định nghĩa công cụ

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Bạn định nghĩa hàm với mô tả rõ ràng và tham số cụ thể. Mô hình nhìn thấy mô tả này trong prompt hệ thống và hiểu công dụng từng công cụ.

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

Sơ đồ dưới đây phân tích mọi chú thích và cho thấy cách từng phần giúp AI hiểu khi nào gọi công cụ và truyền đối số nào:

<img src="../../../translated_images/vi/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Cấu tạo Định nghĩa Công cụ" width="800"/>

*Cấu tạo một định nghĩa công cụ — @Tool báo cho AI khi dùng, @P mô tả từng tham số, và @AiService kết nối mọi thứ lúc khởi động.*

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) và hỏi:
> - "Làm thế nào tích hợp API thời tiết thật như OpenWeatherMap thay vì dữ liệu giả?"
> - "Một mô tả công cụ tốt giúp AI sử dụng đúng như thế nào?"
> - "Làm sao xử lý lỗi API và giới hạn tốc độ trong triển khai công cụ?"

### Quyết định

Khi người dùng hỏi "Thời tiết ở Seattle thế nào?", mô hình không chọn công cụ một cách ngẫu nhiên. Nó so sánh ý định người dùng với mô tả từng công cụ, chấm điểm liên quan, rồi chọn công cụ phù hợp nhất. Sau đó tạo lời gọi hàm có cấu trúc với tham số đúng — ví dụ, `location` là `"Seattle"`.

Nếu không có công cụ nào phù hợp, mô hình trả lời dựa trên kiến thức của nó. Nếu nhiều công cụ phù hợp, mô hình chọn công cụ cụ thể nhất.

<img src="../../../translated_images/vi/decision-making.409cd562e5cecc49.webp" alt="Cách AI Quyết định công cụ dùng" width="800"/>

*Mô hình đánh giá từng công cụ với ý định người dùng và chọn công cụ chính xác — vì vậy mô tả công cụ rõ ràng rất quan trọng.*

### Thực thi

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot tự động kết nối giao diện khai báo `@AiService` với tất cả công cụ đăng ký, và LangChain4j tự động thực thi các cuộc gọi công cụ. Ở hậu trường, một cuộc gọi công cụ hoàn chỉnh trải qua sáu giai đoạn — từ câu hỏi ngôn ngữ tự nhiên của người dùng đến câu trả lời ngôn ngữ tự nhiên trả lại:

<img src="../../../translated_images/vi/tool-calling-flow.8601941b0ca041e6.webp" alt="Luồng Gọi Công cụ" width="800"/>

*Luồng toàn bộ — người dùng hỏi, mô hình chọn công cụ, LangChain4j thực thi, mô hình dệt kết quả vào câu trả lời tự nhiên.*

Nếu bạn đã chạy demo [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) trong Module 00, bạn sẽ thấy mẫu này hoạt động — các công cụ `Calculator` được gọi tương tự. Sơ đồ trình tự dưới đây cho thấy chính xác điều gì xảy ra bên trong demo đó:

<img src="../../../translated_images/vi/tool-calling-sequence.94802f406ca26278.webp" alt="Sơ đồ Trình tự Gọi Công cụ" width="800"/>

*Vòng gọi công cụ từ demo Quick Start — `AiServices` gửi tin nhắn và schema công cụ tới LLM, LLM trả lời với lời gọi hàm như `add(42, 58)`, LangChain4j thực thi phương thức `Calculator` tại chỗ, trả kết quả cho câu trả lời cuối.*

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) và hỏi:
> - "Mẫu ReAct hoạt động như thế nào và tại sao hiệu quả với đại lý AI?"
> - "Đại lý quyết định dùng công cụ nào và thứ tự ra sao?"
> - "Nếu thực thi công cụ thất bại - tôi nên xử lý lỗi thế nào để chắc chắn?"

### Tạo phản hồi

Mô hình nhận dữ liệu thời tiết và định dạng nó thành phản hồi ngôn ngữ tự nhiên cho người dùng.

### Kiến trúc: Spring Boot tự động kết nối

Module này dùng tích hợp LangChain4j với Spring Boot qua các giao diện khai báo `@AiService`. Khi khởi động, Spring Boot phát hiện mọi `@Component` chứa phương thức `@Tool`, bean `ChatModel`, và `ChatMemoryProvider` — sau đó kết nối chúng thành một giao diện `Assistant` duy nhất không cần boilerplate.

<img src="../../../translated_images/vi/spring-boot-wiring.151321795988b04e.webp" alt="Kiến trúc Spring Boot Tự động Kết nối" width="800"/>

*Giao diện @AiService liên kết ChatModel, các thành phần công cụ, và bộ nhớ — Spring Boot tự xử lý toàn bộ kết nối.*

Dưới đây là vòng đời yêu cầu đầy đủ dưới dạng sơ đồ trình tự — từ yêu cầu HTTP qua controller, service, proxy tự động kết nối tới thực thi công cụ và trả lại:

<img src="../../../translated_images/vi/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Sơ đồ Trình tự Gọi Công cụ Spring Boot" width="800"/>

*Vòng đời yêu cầu Spring Boot đầy đủ — HTTP đi qua controller và service tới proxy Assistant tự kết nối, phối hợp LLM và gọi công cụ tự động.*

Lợi ích chính của cách tiếp cận này:

- **Spring Boot tự động kết nối** — ChatModel và công cụ được chèn tự động
- **Mẫu @MemoryId** — Quản lý bộ nhớ phiên làm việc tự động
- **Phiên bản đơn** — Assistant khởi tạo một lần và tái sử dụng để tăng hiệu năng
- **Thực thi an toàn kiểu** — Gọi trực tiếp phương thức Java với chuyển đổi kiểu
- **Điều phối đa lượt** — Xử lý chuỗi công cụ tự động
- **Không boilerplate** — Không cần gọi thủ công `AiServices.builder()` hay HashMap bộ nhớ

Các phương pháp thay thế (tạo thủ công `AiServices.builder()`) đòi hỏi nhiều mã hơn và thiếu lợi ích Spring Boot.

## Chuỗi công cụ

**Chuỗi công cụ** — Sức mạnh thật sự của đại lý dựa trên công cụ hiển thị khi một câu hỏi cần nhiều công cụ. Hỏi "Thời tiết ở Seattle theo Fahrenheit thế nào?" và đại lý tự động xâu chuỗi hai công cụ: đầu tiên gọi `getCurrentWeather` để lấy nhiệt độ Celsius, sau đó chuyển kết quả vào `celsiusToFahrenheit` để chuyển đổi — tất cả trong một lượt hội thoại.

<img src="../../../translated_images/vi/tool-chaining-example.538203e73d09dd82.webp" alt="Ví dụ Chuỗi Công cụ" width="800"/>

*Chuỗi công cụ hoạt động — đại lý gọi trước getCurrentWeather, rồi chuyền kết quả Celsius sang celsiusToFahrenheit, và trả lời tổng hợp.*

**Xử lý lỗi nhẹ nhàng** — Hỏi thời tiết ở thành phố không có trong dữ liệu mẫu. Công cụ trả lỗi, AI giải thích không thể giúp thay vì bị sập. Công cụ lỗi an toàn. Sơ đồ dưới so sánh hai cách — với xử lý lỗi đúng, đại lý bắt ngoại lệ và trả lời hữu ích, nếu không thì ứng dụng bị lỗi ngừng chạy:

<img src="../../../translated_images/vi/error-handling-flow.9a330ffc8ee0475c.webp" alt="Luồng Xử lý Lỗi" width="800"/>

*Khi công cụ lỗi, đại lý bắt lỗi và trả lời giải thích hữu ích thay vì bị sập.*

Điều này diễn ra trong một lượt hội thoại. Đại lý tự điều phối nhiều cuộc gọi công cụ.

## Chạy ứng dụng

**Xác minh triển khai:**

Đảm bảo tệp `.env` tồn tại ở thư mục gốc với thông tin xác thực Azure (được tạo trong Module 01). Chạy lệnh này tại thư mục module (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Khởi động ứng dụng:**

> **Lưu ý:** Nếu bạn đã khởi động tất cả ứng dụng bằng `./start-all.sh` từ thư mục gốc (theo hướng dẫn Module 01), module này đã chạy trên cổng 8084. Bạn có thể bỏ qua lệnh khởi động dưới đây và truy cập trực tiếp http://localhost:8084.

**Lựa chọn 1: Dùng Spring Boot Dashboard (Khuyên dùng cho người dùng VS Code)**

Container phát triển bao gồm tiện ích mở rộng Spring Boot Dashboard, cung cấp giao diện trực quan quản lý mọi ứng dụng Spring Boot. Bạn có thể tìm thấy nó trên Thanh hoạt động bên trái VS Code (biểu tượng Spring Boot).

Từ Spring Boot Dashboard, bạn có thể:
- Xem tất cả ứng dụng Spring Boot có trong workspace
- Khởi động/tạm dừng ứng dụng chỉ với một cú nhấp
- Xem nhật ký ứng dụng thời gian thực
- Giám sát trạng thái ứng dụng

Nhấn nút play bên cạnh "tools" để khởi động module này, hoặc khởi động tất cả các module cùng lúc.

Dưới đây là hình ảnh Spring Boot Dashboard trong VS Code:

<img src="../../../translated_images/vi/dashboard.9b519b1a1bc1b30a.webp" alt="Bảng điều khiển Spring Boot" width="400"/>

*Spring Boot Dashboard trong VS Code — khởi động, dừng, và giám sát tất cả các module tại một nơi*

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
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

Cả hai script tự động tải biến môi trường từ tệp `.env` gốc và sẽ xây dựng các JAR nếu chúng chưa tồn tại.

> **Lưu ý:** Nếu bạn muốn xây dựng tất cả các module thủ công trước khi bắt đầu:
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

Mở http://localhost:8084 trong trình duyệt của bạn.

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

## Sử Dụng Ứng Dụng

Ứng dụng cung cấp giao diện web, nơi bạn có thể tương tác với một tác nhân AI có quyền truy cập vào các công cụ thời tiết và chuyển đổi nhiệt độ. Đây là giao diện — bao gồm các ví dụ khởi động nhanh và bảng chat để gửi yêu cầu:

<a href="images/tools-homepage.png"><img src="../../../translated_images/vi/tools-homepage.4b4cd8b2717f9621.webp" alt="Giao Diện Công Cụ Tác Nhân AI" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Giao diện Công Cụ Tác Nhân AI - các ví dụ nhanh và giao diện chat để tương tác với công cụ*

### Thử Sử Dụng Công Cụ Đơn Giản

Bắt đầu với một yêu cầu đơn giản: "Chuyển đổi 100 độ Fahrenheit sang Celsius". Tác nhân nhận biết rằng cần dùng công cụ chuyển đổi nhiệt độ, gọi công cụ với tham số đúng, và trả kết quả. Bạn thấy cảm giác tự nhiên như thế nào - bạn không cần chỉ định công cụ nào hoặc cách gọi.

### Thử Chuỗi Công Cụ

Bây giờ hãy thử điều gì đó phức tạp hơn: "Thời tiết ở Seattle thế nào và chuyển nó sang Fahrenheit?" Xem tác nhân thực hiện theo từng bước. Nó lấy thời tiết (trả về Celsius), nhận ra cần chuyển sang Fahrenheit, gọi công cụ chuyển đổi, và kết hợp hai kết quả thành một phản hồi.

### Xem Luồng Hội Thoại

Giao diện chat lưu giữ lịch sử hội thoại, cho phép bạn có các tương tác đa lượt. Bạn có thể xem tất cả các truy vấn và phản hồi trước đó, giúp dễ dàng theo dõi hội thoại và hiểu cách tác nhân xây dựng ngữ cảnh qua nhiều lượt trao đổi.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/vi/tools-conversation-demo.89f2ce9676080f59.webp" alt="Hội Thoại Với Nhiều Lần Gọi Công Cụ" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Hội thoại đa lượt thể hiện các chuyển đổi đơn giản, tra cứu thời tiết và chuỗi công cụ*

### Thử Nghiệm Với Các Yêu Cầu Khác Nhau

Thử các kết hợp khác nhau:
- Tra cứu thời tiết: "Thời tiết ở Tokyo thế nào?"
- Chuyển đổi nhiệt độ: "25°C là bao nhiêu Kelvin?"
- Yêu cầu kết hợp: "Kiểm tra thời tiết ở Paris và nói cho tôi biết nó có trên 20°C không"

Bạn sẽ thấy tác nhân hiểu ngôn ngữ tự nhiên và ánh xạ sang các cuộc gọi công cụ phù hợp.

## Khái Niệm Chính

### Mẫu ReAct (Lập Luận và Hành Động)

Tác nhân xen kẽ giữa lập luận (quyết định cần làm gì) và hành động (sử dụng công cụ). Mẫu này cho phép giải quyết vấn đề tự động thay vì chỉ phản hồi theo hướng dẫn.

### Mô Tả Công Cụ Quan Trọng

Chất lượng mô tả công cụ ảnh hưởng trực tiếp đến cách tác nhân sử dụng chúng. Mô tả rõ ràng, cụ thể giúp mô hình hiểu khi nào và cách gọi mỗi công cụ.

### Quản Lý Phiên Làm Việc

Chú thích `@MemoryId` cho phép quản lý bộ nhớ dựa trên phiên làm việc tự động. Mỗi ID phiên được gán một thể hiện `ChatMemory` do `ChatMemoryProvider` quản lý, vì vậy nhiều người dùng có thể tương tác cùng lúc mà không lẫn lộn cuộc trò chuyện. Sơ đồ dưới đây thể hiện cách nhiều người dùng được chuyển hướng tới kho lưu trữ bộ nhớ riêng biệt dựa trên ID phiên:

<img src="../../../translated_images/vi/session-management.91ad819c6c89c400.webp" alt="Quản Lý Phiên Với @MemoryId" width="800"/>

*Mỗi ID phiên ánh xạ tới một lịch sử hội thoại riêng biệt — người dùng không bao giờ xem được tin nhắn của nhau.*

### Xử Lý Lỗi

Công cụ có thể bị lỗi — API hết thời gian chờ, tham số không hợp lệ, dịch vụ ngoài sập. Tác nhân sản xuất cần xử lý lỗi để mô hình có thể giải thích vấn đề hay thử phương án khác thay vì làm sập toàn bộ ứng dụng. Khi công cụ ném ra ngoại lệ, LangChain4j bắt lỗi và gửi lại thông báo lỗi cho mô hình, từ đó mô hình có thể giải thích vấn đề bằng ngôn ngữ tự nhiên.

## Các Công Cụ Có Sẵn

Sơ đồ dưới đây thể hiện hệ sinh thái rộng lớn của các công cụ bạn có thể xây dựng. Module này trình diễn các công cụ thời tiết và nhiệt độ, nhưng mẫu `@Tool` cũng hoạt động với bất kỳ phương thức Java nào — từ truy vấn cơ sở dữ liệu đến xử lý thanh toán.

<img src="../../../translated_images/vi/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Hệ Sinh Thái Công Cụ" width="800"/>

*Mọi phương thức Java được chú thích @Tool đều có thể dùng cho AI — mẫu này mở rộng tới cơ sở dữ liệu, API, email, thao tác tập tin, và nhiều hơn nữa.*

## Khi Nào Nên Dùng Tác Nhân Dựa Trên Công Cụ

Không phải yêu cầu nào cũng cần công cụ. Quyết định dựa trên việc AI có cần tương tác với hệ thống bên ngoài hay có thể trả lời từ kiến thức sẵn có. Hướng dẫn dưới đây tóm tắt khi nào công cụ có giá trị và khi nào không cần:

<img src="../../../translated_images/vi/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Khi Nào Nên Dùng Công Cụ" width="800"/>

*Hướng dẫn nhanh — công cụ dùng cho dữ liệu thời gian thực, tính toán và hành động; kiến thức chung và tác vụ sáng tạo thì không cần.*

## Công Cụ so với RAG

Module 03 và 04 đều mở rộng khả năng AI, nhưng theo cách cơ bản khác nhau. RAG cho mô hình truy cập **kiến thức** bằng cách truy xuất tài liệu. Công cụ cho mô hình khả năng thực hiện **hành động** thông qua gọi hàm. Sơ đồ dưới đây so sánh hai cách tiếp cận — từ cách hoạt động quy trình tới những đánh đổi giữa chúng:

<img src="../../../translated_images/vi/tools-vs-rag.ad55ce10d7e4da87.webp" alt="So Sánh Công Cụ và RAG" width="800"/>

*RAG lấy thông tin từ tài liệu tĩnh — Công cụ thực thi hành động và lấy dữ liệu động, thời gian thực. Nhiều hệ thống sản xuất kết hợp cả hai.*

Thực tế, nhiều hệ thống sản xuất kết hợp cả hai cách: RAG để căn cứ câu trả lời vào tài liệu, và Công cụ để lấy dữ liệu trực tiếp hoặc thực hiện thao tác.

## Bước Tiếp Theo

**Module Tiếp Theo:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Điều Hướng:** [← Trước: Module 03 - RAG](../03-rag/README.md) | [Quay Lại Trang Chính](../README.md) | [Tiếp: Module 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố từ chối trách nhiệm**:  
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng các bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ nguyên bản nên được coi là nguồn chính xác và đáng tin cậy. Đối với các thông tin quan trọng, nên sử dụng dịch vụ dịch thuật chuyên nghiệp do con người thực hiện. Chúng tôi không chịu trách nhiệm về bất kỳ sự hiểu lầm hay giải thích sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->