# Module 05: Giao thức Ngữ cảnh Mô hình (MCP)

## Mục lục

- [Bạn sẽ học được gì](../../../05-mcp)
- [MCP là gì?](../../../05-mcp)
- [Cách MCP hoạt động](../../../05-mcp)
- [Mô-đun Agentic](../../../05-mcp)
- [Chạy các ví dụ](../../../05-mcp)
  - [Yêu cầu trước](../../../05-mcp)
- [Bắt đầu nhanh](../../../05-mcp)
  - [Thao tác Tệp (Stdio)](../../../05-mcp)
  - [Agent Giám sát](../../../05-mcp)
    - [Chạy Demo](../../../05-mcp)
    - [Cách Giám sát hoạt động](../../../05-mcp)
    - [Cách FileAgent Khám phá công cụ MCP khi chạy](../../../05-mcp)
    - [Chiến lược phản hồi](../../../05-mcp)
    - [Hiểu kết quả đầu ra](../../../05-mcp)
    - [Giải thích tính năng của Mô-đun Agentic](../../../05-mcp)
- [Khái niệm chính](../../../05-mcp)
- [Chúc mừng!](../../../05-mcp)
  - [Tiếp theo là gì?](../../../05-mcp)

## Bạn sẽ học được gì

Bạn đã xây dựng AI hội thoại, làm chủ các prompt, dựa câu trả lời trên tài liệu và tạo ra các agent với công cụ. Nhưng tất cả những công cụ đó đều được xây dựng riêng cho ứng dụng cụ thể của bạn. Nếu bạn có thể cho AI truy cập vào một hệ sinh thái công cụ tiêu chuẩn mà bất kỳ ai cũng có thể tạo và chia sẻ thì sao? Trong module này, bạn sẽ học cách làm điều đó với Giao thức Ngữ cảnh Mô hình (MCP) và mô-đun agentic của LangChain4j. Chúng tôi đầu tiên trình bày một trình đọc tệp MCP đơn giản và sau đó cho thấy cách nó dễ dàng tích hợp vào các quy trình agentic nâng cao sử dụng mô hình Agent Giám sát.

## MCP là gì?

Giao thức Ngữ cảnh Mô hình (MCP) cung cấp chính xác điều đó - một cách chuẩn để các ứng dụng AI khám phá và sử dụng các công cụ bên ngoài. Thay vì viết các tích hợp tùy chỉnh cho từng nguồn dữ liệu hay dịch vụ, bạn kết nối với các máy chủ MCP để chúng công khai năng lực theo định dạng thống nhất. Agent AI của bạn có thể tự động khám phá và sử dụng các công cụ này.

Sơ đồ dưới đây cho thấy sự khác biệt — không dùng MCP, mỗi tích hợp cần kết nối điểm-điểm tùy chỉnh; còn dùng MCP, một giao thức duy nhất kết nối ứng dụng của bạn với bất kỳ công cụ nào:

<img src="../../../translated_images/vi/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Trước MCP: Tích hợp phức tạp theo kiểu điểm-điểm. Sau MCP: Một giao thức, vô số khả năng.*

MCP giải quyết một vấn đề cơ bản trong phát triển AI: mỗi tích hợp đều phải tùy chỉnh. Muốn truy cập GitHub? Viết mã tùy chỉnh. Muốn đọc tệp? Mã tùy chỉnh. Muốn truy vấn cơ sở dữ liệu? Mã tùy chỉnh. Và không tích hợp nào trong số đó hoạt động với các ứng dụng AI khác.

MCP chuẩn hóa điều này. Một máy chủ MCP trình bày công cụ với mô tả rõ ràng và sơ đồ tham số. Bất kỳ ứng dụng MCP nào cũng có thể kết nối, khám phá công cụ sẵn có và sử dụng chúng. Xây dựng một lần, dùng mọi nơi.

Sơ đồ dưới đây minh họa kiến trúc này — một client MCP (ứng dụng AI của bạn) kết nối tới nhiều máy chủ MCP, mỗi máy chủ cung cấp bộ công cụ riêng thông qua giao thức chuẩn:

<img src="../../../translated_images/vi/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Kiến trúc Giao thức Ngữ cảnh Mô hình - khám phá và thực thi công cụ chuẩn hóa*

## Cách MCP hoạt động

Bên trong, MCP sử dụng kiến trúc phân lớp. Ứng dụng Java của bạn (client MCP) khám phá công cụ có sẵn, gửi yêu cầu JSON-RPC qua lớp truyền tải (Stdio hoặc HTTP), và máy chủ MCP thực thi thao tác rồi trả kết quả. Sơ đồ sau chia nhỏ từng lớp của giao thức này:

<img src="../../../translated_images/vi/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Cách MCP hoạt động bên trong — client khám phá công cụ, trao đổi thông điệp JSON-RPC, và thực thi qua lớp truyền tải.*

**Kiến trúc Máy chủ-Client**

MCP dùng mô hình client-server. Các máy chủ cung cấp công cụ - đọc tệp, truy vấn DB, gọi API. Client (ứng dụng AI của bạn) kết nối máy chủ và dùng công cụ đó.

Để dùng MCP với LangChain4j, thêm phụ thuộc Maven sau:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Khám phá Công cụ**

Khi client kết nối với máy chủ MCP, nó hỏi "Bạn có công cụ gì?" Máy chủ trả về danh sách công cụ sẵn có, mỗi công cụ kèm mô tả và sơ đồ tham số. Agent AI của bạn sẽ quyết định công cụ nào dùng dựa trên yêu cầu người dùng. Sơ đồ dưới đây mô tả quá trình trao đổi — client gửi yêu cầu `tools/list` và máy chủ trả về danh sách cùng mô tả, sơ đồ tham số:

<img src="../../../translated_images/vi/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI khám phá công cụ có sẵn khi khởi động — biết năng lực hiện có và quyết định công cụ dùng.*

**Cơ chế Truyền tải**

MCP hỗ trợ các cơ chế truyền tải khác nhau. Hai lựa chọn là Stdio (cho giao tiếp quy trình phụ địa phương) và HTTP có thể stream (cho máy chủ từ xa). Module này minh họa truyền tải Stdio:

<img src="../../../translated_images/vi/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*Cơ chế truyền tải MCP: HTTP cho máy chủ từ xa, Stdio cho quy trình địa phương*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Dùng cho các quy trình địa phương. Ứng dụng của bạn khởi tạo một máy chủ như quy trình phụ và giao tiếp qua đầu vào/đầu ra chuẩn. Hữu ích cho truy cập file hệ thống hoặc công cụ dòng lệnh.

```java
McpTransport stdioTransport = new StdioMcpTransport.Builder()
    .command(List.of(
        npmCmd, "exec",
        "@modelcontextprotocol/server-filesystem@2025.12.18",
        resourcesDir
    ))
    .logEvents(false)
    .build();
```

Máy chủ `@modelcontextprotocol/server-filesystem` cung cấp các công cụ sau, tất cả được cách ly trong các thư mục bạn chỉ định:

| Công cụ | Mô tả |
|------|-------------|
| `read_file` | Đọc nội dung một tệp đơn |
| `read_multiple_files` | Đọc nhiều tệp trong một lần gọi |
| `write_file` | Tạo hoặc ghi đè tệp |
| `edit_file` | Thực hiện tìm và thay thế có mục tiêu |
| `list_directory` | Liệt kê tệp và thư mục tại một đường dẫn |
| `search_files` | Tìm kiếm đệ quy tệp theo mẫu |
| `get_file_info` | Lấy metadata tệp (kích thước, thời gian, quyền) |
| `create_directory` | Tạo thư mục (bao gồm thư mục cha) |
| `move_file` | Di chuyển hoặc đổi tên tệp/thư mục |

Sơ đồ dưới thể hiện cách truyền tải Stdio hoạt động khi chạy — ứng dụng Java khởi tạo máy chủ MCP như quy trình con và giao tiếp qua pipe stdin/stdout, không dùng mạng hoặc HTTP:

<img src="../../../translated_images/vi/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Truyền tải Stdio trong thực tế — ứng dụng khởi tạo server MCP như con và giao tiếp qua stdin/stdout pipe.*

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) và hỏi:
> - "Truyền tải Stdio hoạt động thế nào và khi nào dùng thay vì HTTP?"
> - "LangChain4j quản lý vòng đời của các quy trình server MCP sinh ra thế nào?"
> - "Những rủi ro bảo mật khi cho AI truy cập hệ thống tệp là gì?"

## Mô-đun Agentic

Trong khi MCP cung cấp công cụ chuẩn hóa, mô-đun **agentic** của LangChain4j cung cấp cách khai báo để xây dựng agent điều phối các công cụ đó. Chú thích `@Agent` và `AgenticServices` cho phép bạn định nghĩa hành vi agent qua các interface thay vì mã lệnh thủ tục.

Trong module này, bạn sẽ khám phá mô hình **Agent Giám sát** — một cách tiếp cận AI agentic nâng cao, nơi agent "giám sát" quyết định động sub-agent nào được kích hoạt dựa vào yêu cầu người dùng. Chúng tôi kết hợp cả hai khái niệm bằng cách trang bị cho một sub-agent khả năng truy cập tệp MCP.

Để dùng mô-đun agentic, thêm phụ thuộc Maven sau:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Lưu ý:** Mô-đun `langchain4j-agentic` dùng thuộc tính phiên bản riêng (`langchain4j.mcp.version`) vì nó được phát hành theo lịch trình khác với thư viện LangChain4j cốt lõi.

> **⚠️ Thử nghiệm:** Mô-đun `langchain4j-agentic` vẫn là **thử nghiệm** và có thể thay đổi. Cách ổn định để xây trợ lý AI vẫn là dùng `langchain4j-core` với công cụ tùy chỉnh (Module 04).

## Chạy các ví dụ

### Yêu cầu trước

- Hoàn thành [Module 04 - Công cụ](../04-tools/README.md) (module này xây dựng trên khái niệm công cụ tùy chỉnh và so sánh với công cụ MCP)
- Tệp `.env` ở thư mục gốc chứa thông tin xác thực Azure (được tạo bởi `azd up` trong Module 01)
- Java 21+, Maven 3.9+
- Node.js 16+ và npm (cho máy chủ MCP)

> **Lưu ý:** Nếu bạn chưa thiết lập biến môi trường, hãy xem [Module 01 - Giới thiệu](../01-introduction/README.md) để biết hướng dẫn triển khai (lệnh `azd up` tạo tự động tệp `.env`), hoặc sao chép `.env.example` thành `.env` tại thư mục gốc và điền thông tin.

## Bắt đầu nhanh

**Dùng VS Code:** Nhấn chuột phải vào bất kỳ tệp demo nào trong Explorer và chọn **"Run Java"**, hoặc dùng cấu hình chạy từ bảng Run and Debug (đảm bảo tệp `.env` đã cấu hình thông tin Azure).

**Dùng Maven:** Ngoài ra, có thể chạy từ dòng lệnh với các ví dụ dưới đây.

### Thao tác Tệp (Stdio)

Ví dụ này trình diễn công cụ dựa trên quy trình phụ địa phương.

**✅ Không cần yêu cầu trước** - server MCP sẽ được khởi tạo tự động.

**Dùng script khởi động (Được khuyên dùng):**

Các script khởi động tự động tải biến môi trường từ tệp `.env` gốc:

**Bash:**
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-stdio.ps1
```

**Dùng VS Code:** Nhấn chuột phải vào `StdioTransportDemo.java` và chọn **"Run Java"** (đảm bảo tệp `.env` đã cấu hình).

Ứng dụng khởi tạo tự động server MCP hệ thống tệp và đọc một tệp cục bộ. Bạn sẽ thấy cách quản lý quy trình phụ được xử lý tự động.

**Kết quả dự kiến:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agent Giám sát

Mô hình **Agent Giám sát** là dạng AI agentic **linh hoạt**. Một Giám sát sử dụng LLM để tự động quyết định agent nào chạy dựa trên yêu cầu người dùng. Trong ví dụ tiếp theo, chúng tôi kết hợp truy cập tệp qua MCP với agent LLM để tạo quy trình đọc tệp → báo cáo có giám sát.

Demo này, `FileAgent` đọc tệp qua công cụ hệ thống tệp MCP, và `ReportAgent` tạo báo cáo có cấu trúc với tóm tắt điều hành (1 câu), 3 điểm chính và khuyến nghị. Giám sát tự điều phối luồng này:

<img src="../../../translated_images/vi/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Giám sát dùng LLM của mình để quyết định agent nào cần mời và theo thứ tự nào — không cần định tuyến cứng mã.*

Quy trình cụ thể cho pipeline đọc tệp sang báo cáo như sau:

<img src="../../../translated_images/vi/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent đọc tệp qua công cụ MCP, ReportAgent biến nội dung thô thành báo cáo cấu trúc.*

Sơ đồ tuần tự sau theo dõi toàn bộ luồng điều phối Giám sát — từ khởi tạo server MCP, qua lựa chọn agent độc lập của Giám sát, tới gọi công cụ qua stdio và kết quả cuối cùng:

<img src="../../../translated_images/vi/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*Giám sát tự động gọi FileAgent (gọi server MCP qua stdio để đọc tệp), rồi gọi ReportAgent để tạo báo cáo cấu trúc — mỗi agent lưu kết quả vào Agentic Scope dùng chung.*

Mỗi agent lưu kết quả vào **Agentic Scope** (bộ nhớ dùng chung), cho phép các agent tiếp theo truy cập kết quả trước đó. Điều này minh họa cách công cụ MCP tích hợp mượt mà vào quy trình agentic — Giám sát không cần biết *cách* đọc tệp, chỉ cần biết `FileAgent` làm được việc đó.

#### Chạy Demo

Các script khởi động tự động tải biến môi trường từ tệp `.env` gốc:

**Bash:**
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-supervisor.ps1
```

**Dùng VS Code:** Nhấn chuột phải vào `SupervisorAgentDemo.java` và chọn **"Run Java"** (đảm bảo tệp `.env` đã cấu hình).

#### Cách Giám sát hoạt động

Trước khi tạo agent, cần kết nối giao thức MCP với client rồi bao bọc nó dưới dạng `ToolProvider`. Đây là cách công cụ máy chủ MCP trở nên sẵn có cho agent của bạn:

```java
// Tạo một client MCP từ giao thức truyền tải
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Đóng gói client như một ToolProvider — điều này kết nối các công cụ MCP vào LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Bây giờ bạn có thể tiêm `mcpToolProvider` vào bất kỳ agent nào cần công cụ MCP:

```java
// Bước 1: FileAgent đọc tập tin sử dụng các công cụ MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Có công cụ MCP cho các thao tác trên tập tin
        .build();

// Bước 2: ReportAgent tạo báo cáo có cấu trúc
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor điều phối quy trình làm việc từ tập tin → báo cáo
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Trả lại báo cáo cuối cùng
        .build();

// Supervisor quyết định gọi các agent nào dựa trên yêu cầu
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Cách FileAgent Khám phá công cụ MCP khi chạy

Bạn có thể thắc mắc: **làm sao `FileAgent` biết cách dùng công cụ hệ thống tệp npm?** Câu trả lời là nó không biết — chính **LLM** tìm hiểu khi chạy bằng sơ đồ công cụ.

Giao diện `FileAgent` chỉ là một **định nghĩa prompt**. Nó không có kiến thức cứng về `read_file`, `list_directory` hay bất kỳ công cụ MCP nào khác. Quá trình cuối cùng ra sao:
1. **Máy chủ khởi tạo:** `StdioMcpTransport` khởi chạy gói npm `@modelcontextprotocol/server-filesystem` như một tiến trình con  
2. **Phát hiện công cụ:** `McpClient` gửi yêu cầu JSON-RPC `tools/list` đến máy chủ, máy chủ trả lời với tên công cụ, mô tả, và schema tham số (ví dụ, `read_file` — *"Đọc toàn bộ nội dung của một tập tin"* — `{ path: string }`)  
3. **Tiêm schema:** `McpToolProvider` bao bọc các schema được phát hiện và làm cho chúng có sẵn với LangChain4j  
4. **LLM quyết định:** Khi gọi `FileAgent.readFile(path)`, LangChain4j gửi tin nhắn hệ thống, tin nhắn người dùng, **và danh sách các schema công cụ** đến LLM. LLM đọc mô tả công cụ và tạo ra một lời gọi công cụ (ví dụ, `read_file(path="/some/file.txt")`)  
5. **Thực thi:** LangChain4j bắt lời gọi công cụ, chuyển nó qua MCP client về tiến trình phụ Node.js, lấy kết quả và trả lại cho LLM  

Đây là cùng một cơ chế [Phát hiện Công cụ](../../../05-mcp) đã mô tả ở trên, nhưng áp dụng cụ thể cho luồng làm việc của agent. Các chú thích `@SystemMessage` và `@UserMessage` hướng dẫn hành vi của LLM, trong khi `ToolProvider` được tiêm cung cấp **khả năng** — LLM nối kết hai phần này vào thời gian chạy.

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) và hỏi:  
> - "Agent này biết gọi công cụ MCP nào như thế nào?"  
> - "Điều gì sẽ xảy ra nếu tôi bỏ `ToolProvider` khỏi agent builder?"  
> - "Schema công cụ được truyền đến LLM thế nào?"

#### Chiến lược Phản hồi

Khi bạn cấu hình một `SupervisorAgent`, bạn xác định cách nó nên tổng hợp câu trả lời cuối cùng cho người dùng sau khi các sub-agent hoàn thành nhiệm vụ. Sơ đồ dưới đây thể hiện ba chiến lược có sẵn — LAST trả về đầu ra cuối cùng của agent trực tiếp, SUMMARY tổng hợp tất cả đầu ra qua một LLM, và SCORED chọn đầu ra có điểm cao hơn so với yêu cầu ban đầu:

<img src="../../../translated_images/vi/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Ba chiến lược để Supervisor tổng hợp phản hồi cuối cùng — chọn dựa vào bạn muốn đầu ra của agent cuối cùng, bản tóm tắt tổng hợp, hay lựa chọn có điểm cao nhất.*

Các chiến lược có sẵn là:

| Chiến lược | Mô tả |
|----------|-------------|
| **LAST** | Supervisor trả về đầu ra của sub-agent hoặc công cụ gọi cuối cùng. Điều này hữu ích khi agent cuối cùng trong luồng làm việc được thiết kế đặc biệt để tạo ra câu trả lời hoàn chỉnh, cuối cùng (ví dụ, một "Summary Agent" trong pipeline nghiên cứu). |
| **SUMMARY** | Supervisor sử dụng chính mô hình ngôn ngữ nội bộ (LLM) của mình để tổng hợp bản tóm tắt toàn bộ tương tác và tất cả đầu ra sub-agent, rồi trả về bản tóm tắt đó như phản hồi cuối cùng. Điều này cung cấp một câu trả lời gọn gàng, tổng hợp cho người dùng. |
| **SCORED** | Hệ thống sử dụng một LLM nội bộ để đánh giá cả phản hồi LAST và bản SUMMARY của tương tác dựa trên yêu cầu người dùng ban đầu, trả về đầu ra có điểm cao hơn. |

Xem [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) để biết triển khai đầy đủ.

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) và hỏi:  
> - "Supervisor quyết định gọi agent nào như thế nào?"  
> - "Sự khác biệt giữa Supervisor và mẫu quy trình Sequential là gì?"  
> - "Làm sao tôi tùy chỉnh hành vi lập kế hoạch của Supervisor?"

#### Hiểu về Đầu ra

Khi bạn chạy demo, bạn sẽ thấy quá trình có cấu trúc về cách Supervisor điều phối nhiều agent. Dưới đây là ý nghĩa từng phần:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Phần đầu** giới thiệu khái niệm luồng làm việc: một pipeline tập trung từ việc đọc file đến tạo báo cáo.

```
--- WORKFLOW ---------------------------------------------------------
  ┌─────────────┐      ┌──────────────┐
  │  FileAgent  │ ───▶ │ ReportAgent  │
  │ (MCP tools) │      │  (pure LLM)  │
  └─────────────┘      └──────────────┘
   outputKey:           outputKey:
   'fileContent'        'report'

--- AVAILABLE AGENTS -------------------------------------------------
  [FILE]   FileAgent   - Reads files via MCP → stores in 'fileContent'
  [REPORT] ReportAgent - Generates structured report → stores in 'report'
```
  
**Sơ đồ luồng làm việc** thể hiện luồng dữ liệu giữa các agent. Mỗi agent có vai trò cụ thể:  
- **FileAgent** đọc file qua công cụ MCP và lưu nội dung thô vào `fileContent`  
- **ReportAgent** sử dụng nội dung đó và tạo báo cáo có cấu trúc trong `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Yêu cầu Người dùng** hiển thị nhiệm vụ. Supervisor phân tích và quyết định gọi FileAgent → ReportAgent.

```
--- SUPERVISOR ORCHESTRATION -----------------------------------------
  The Supervisor decides which agents to invoke and passes data between them...

  +-- STEP 1: Supervisor chose -> FileAgent (reading file via MCP)
  |
  |   Input: .../file.txt
  |
  |   Result: LangChain4j is an open-source, provider-agnostic Java framework for building LLM...
  +-- [OK] FileAgent (reading file via MCP) completed

  +-- STEP 2: Supervisor chose -> ReportAgent (generating structured report)
  |
  |   Input: LangChain4j is an open-source, provider-agnostic Java framew...
  |
  |   Result: Executive Summary...
  +-- [OK] ReportAgent (generating structured report) completed
```
  
**Điều phối bởi Supervisor** trình bày luồng 2 bước hoạt động:  
1. **FileAgent** đọc file qua MCP và lưu nội dung  
2. **ReportAgent** nhận nội dung và tạo báo cáo có cấu trúc

Supervisor đưa ra những quyết định này **độc lập** dựa trên yêu cầu của người dùng.

```
--- FINAL RESPONSE ---------------------------------------------------
Executive Summary
...

Key Points
...

Recommendations
...

--- AGENTIC SCOPE (Data Flow) ----------------------------------------
  Each agent stores its output for downstream agents to consume:
  * fileContent: LangChain4j is an open-source, provider-agnostic Java framework...
  * report: Executive Summary...
```
  
#### Giải thích về Tính năng Agentic Module

Ví dụ này trình bày một số tính năng nâng cao của module agentic. Hãy cùng xem xét kỹ hơn Agentic Scope và Agent Listeners.

**Agentic Scope** biểu thị vùng nhớ được chia sẻ, nơi các agent lưu kết quả của họ bằng `@Agent(outputKey="...")`. Điều này cho phép:  
- Các agent sau truy cập đầu ra của các agent trước  
- Supervisor tổng hợp phản hồi cuối cùng  
- Bạn kiểm tra được sản phẩm của từng agent

Sơ đồ dưới đây minh họa cách Agentic Scope hoạt động như vùng nhớ chia sẻ trong luồng file thành báo cáo — FileAgent ghi đầu ra dưới khóa `fileContent`, ReportAgent đọc và ghi đầu ra của mình dưới khóa `report`:

<img src="../../../translated_images/vi/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope hoạt động như vùng nhớ chia sẻ — FileAgent ghi `fileContent`, ReportAgent đọc nó và ghi `report`, và mã của bạn đọc kết quả cuối cùng.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Dữ liệu tệp thô từ FileAgent
String report = scope.readState("report");            // Báo cáo cấu trúc từ ReportAgent
```
  
**Agent Listeners** giúp giám sát và gỡ lỗi việc thực thi agent. Đầu ra từng bước bạn thấy trong demo đến từ một AgentListener gắn vào mỗi lần gọi agent:  
- **beforeAgentInvocation** - Gọi khi Supervisor chọn một agent, giúp bạn thấy agent nào được chọn và lý do  
- **afterAgentInvocation** - Gọi khi một agent hoàn thành, hiển thị kết quả của nó  
- **inheritedBySubagents** - Khi đúng, listener sẽ theo dõi tất cả agent trong hệ thống phân cấp

Sơ đồ dưới đây diễn tả vòng đời đầy đủ của Agent Listener, bao gồm cách `onError` xử lý lỗi phát sinh trong quá trình agent thực thi:

<img src="../../../translated_images/vi/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners gắn vào vòng đời thực thi — giám sát khi agent bắt đầu, hoàn thành hoặc gặp lỗi.*

```java
AgentListener monitor = new AgentListener() {
    private int step = 0;
    
    @Override
    public void beforeAgentInvocation(AgentRequest request) {
        step++;
        System.out.println("  +-- STEP " + step + ": " + request.agentName());
    }
    
    @Override
    public void afterAgentInvocation(AgentResponse response) {
        System.out.println("  +-- [OK] " + response.agentName() + " completed");
    }
    
    @Override
    public boolean inheritedBySubagents() {
        return true; // Lan truyền đến tất cả các đại lý phụ
    }
};
```
  
Ngoài mẫu Supervisor, module `langchain4j-agentic` cung cấp nhiều mẫu luồng làm việc mạnh mẽ. Sơ đồ dưới đây trình bày năm mẫu — từ pipeline tuần tự đơn giản đến luồng công việc phê duyệt có sự tham gia của con người:

<img src="../../../translated_images/vi/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Năm mẫu luồng làm việc để điều phối agent — từ pipeline tuần tự đơn giản đến luồng phê duyệt có người tham gia.*

| Mẫu | Mô tả | Trường hợp sử dụng |
|---------|-------------|----------|
| **Sequential** | Thực thi agent theo thứ tự, đầu ra chuyển sang agent kế tiếp | Pipeline: nghiên cứu → phân tích → báo cáo |
| **Parallel** | Chạy các agent đồng thời | Nhiệm vụ độc lập: thời tiết + tin tức + chứng khoán |
| **Loop** | Lặp lại cho đến khi điều kiện được thỏa mãn | Đánh giá chất lượng: cải tiến đến khi điểm ≥ 0.8 |
| **Conditional** | Tuyến đường dựa trên điều kiện | Phân loại → chuyển đến agent chuyên gia |
| **Human-in-the-Loop** | Thêm các bước kiểm tra của con người | Luồng phê duyệt, duyệt nội dung |

## Khái niệm chính

Khi bạn đã khám phá MCP và module agentic trong thực hành, hãy tóm tắt khi nào nên sử dụng từng phương pháp.

Một trong những lợi thế lớn nhất của MCP là hệ sinh thái ngày càng phát triển. Sơ đồ dưới đây trình bày cách một giao thức phổ quát kết nối ứng dụng AI của bạn với nhiều máy chủ MCP khác nhau — từ truy cập hệ thống tập tin, cơ sở dữ liệu đến GitHub, email, web scraping, và nhiều hơn nữa:

<img src="../../../translated_images/vi/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP tạo ra hệ sinh thái giao thức phổ quát — bất kỳ máy chủ MCP tương thích nào làm việc với mọi client MCP tương thích, cho phép chia sẻ công cụ giữa các ứng dụng.*

**MCP** lý tưởng khi bạn muốn tận dụng hệ sinh thái công cụ sẵn có, xây dựng công cụ mà nhiều ứng dụng có thể chia sẻ, tích hợp dịch vụ bên thứ ba với giao thức chuẩn, hoặc thay đổi triển khai công cụ mà không cần đổi mã nguồn.

**Module Agentic** phù hợp nhất khi bạn muốn định nghĩa agent theo cách khai báo với chú thích `@Agent`, cần điều phối luồng làm việc (tuần tự, vòng lặp, song song), ưu tiên thiết kế agent dựa trên giao diện thay vì mã lệnh rõ ràng, hoặc kết hợp nhiều agent chia sẻ đầu ra qua `outputKey`.

**Mẫu Supervisor Agent** nổi bật khi luồng làm việc không thể dự đoán trước và bạn muốn để LLM quyết định, khi có nhiều agent chuyên biệt cần điều phối động, khi xây dựng hệ thống hội thoại định tuyến đến các khả năng khác nhau, hoặc khi bạn muốn hành vi agent linh hoạt và thích ứng nhất.

Để giúp bạn lựa chọn giữa các phương pháp `@Tool` tùy chỉnh trong Module 04 và các công cụ MCP trong module này, so sánh sau đây làm nổi bật các điểm mạnh — công cụ tùy chỉnh mang lại sự gắn kết chặt chẽ và an toàn kiểu dữ liệu cho logic riêng của ứng dụng, trong khi công cụ MCP cung cấp tích hợp chuẩn hóa, có thể tái sử dụng:

<img src="../../../translated_images/vi/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Khi nào dùng phương pháp @Tool tùy chỉnh so với công cụ MCP — công cụ tùy chỉnh cho logic ứng dụng cụ thể với an toàn kiểu đầy đủ, công cụ MCP cho tích hợp chuẩn hóa hoạt động đa ứng dụng.*

## Chúc mừng!

Bạn đã hoàn thành tất cả năm module của khóa học LangChain4j for Beginners! Dưới đây là hành trình học tập đầy đủ bạn đã trải qua — từ chat cơ bản đến hệ thống agentic chạy trên MCP:

<img src="../../../translated_images/vi/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Hành trình học tập của bạn qua tất cả năm module — từ chat cơ bản đến hệ thống agentic chạy trên MCP.*

Bạn đã hoàn thành khóa học LangChain4j for Beginners. Bạn đã học:  

- Cách xây dựng AI hội thoại có bộ nhớ (Module 01)  
- Mẫu kỹ thuật prompt cho các nhiệm vụ khác nhau (Module 02)  
- Căn cứ phản hồi dựa trên tài liệu với RAG (Module 03)  
- Tạo agent AI cơ bản (trợ lý) với công cụ tùy chỉnh (Module 04)  
- Tích hợp công cụ chuẩn hóa với module LangChain4j MCP và Agentic (Module 05)  

### Tiếp theo là gì?

Sau khi hoàn thành các module, khám phá [Testing Guide](../docs/TESTING.md) để xem các khái niệm kiểm thử LangChain4j trong thực tế.

**Tài nguyên chính thức:**  
- [Tài liệu LangChain4j](https://docs.langchain4j.dev/) - Hướng dẫn toàn diện và tham khảo API  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Mã nguồn và ví dụ  
- [Hướng dẫn LangChain4j](https://docs.langchain4j.dev/tutorials/) - Các bài học từng bước cho nhiều trường hợp sử dụng  

Cảm ơn bạn đã hoàn thành khóa học này!

---

**Điều hướng:** [← Trước: Module 04 - Tools](../04-tools/README.md) | [Quay lại chính](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố từ chối trách nhiệm**:  
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng các bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ gốc của nó nên được coi là nguồn tham khảo chính thức. Đối với các thông tin quan trọng, nên sử dụng dịch vụ dịch thuật chuyên nghiệp bởi con người. Chúng tôi không chịu trách nhiệm về bất kỳ sự hiểu nhầm hoặc diễn giải sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->