# Module 05: Giao Thức Ngữ Cảnh Mô Hình (MCP)

## Mục Lục

- [Bạn Sẽ Học Gì](../../../05-mcp)
- [MCP là gì?](../../../05-mcp)
- [MCP Hoạt Động Như Thế Nào](../../../05-mcp)
- [Module Agentic](../../../05-mcp)
- [Chạy Các Ví Dụ](../../../05-mcp)
  - [Yêu Cầu Tiền Đề](../../../05-mcp)
- [Bắt Đầu Nhanh](../../../05-mcp)
  - [Các Thao Tác Tập Tin (Stdio)](../../../05-mcp)
  - [Agent Giám Sát](../../../05-mcp)
    - [Chạy Demo](../../../05-mcp)
    - [Cách Giám Sát Hoạt Động](../../../05-mcp)
    - [Chiến Lược Phản Hồi](../../../05-mcp)
    - [Hiểu Về Đầu Ra](../../../05-mcp)
    - [Giải Thích Các Tính Năng Của Module Agentic](../../../05-mcp)
- [Khái Niệm Chính](../../../05-mcp)
- [Chúc Mừng!](../../../05-mcp)
  - [Tiếp Theo Là Gì?](../../../05-mcp)

## Bạn Sẽ Học Gì

Bạn đã xây dựng AI hội thoại, làm chủ các prompt, dựa phản hồi trên tài liệu, và tạo ra các agent với công cụ. Nhưng tất cả những công cụ đó được xây dựng riêng cho ứng dụng cụ thể của bạn. Nếu bạn có thể cung cấp cho AI của mình truy cập vào một hệ sinh thái công cụ chuẩn hóa mà bất cứ ai cũng có thể tạo và chia sẻ? Trong module này, bạn sẽ học cách làm chính xác điều đó với Giao Thức Ngữ Cảnh Mô Hình (MCP) và module agentic của LangChain4j. Chúng tôi sẽ trình diễn đầu tiên một bộ đọc tập tin MCP đơn giản rồi sau đó cho thấy cách nó dễ dàng tích hợp vào các workflow agentic nâng cao sử dụng mô hình Agent Giám Sát.

## MCP là gì?

Giao Thức Ngữ Cảnh Mô Hình (MCP) cung cấp chính xác điều đó - một cách tiêu chuẩn để các ứng dụng AI khám phá và sử dụng các công cụ bên ngoài. Thay vì viết các tích hợp tùy chỉnh cho mỗi nguồn dữ liệu hoặc dịch vụ, bạn kết nối với các máy chủ MCP cho phép truy cập khả năng của họ theo định dạng nhất quán. Agent AI của bạn có thể tự động phát hiện và sử dụng các công cụ này.

Sơ đồ dưới đây cho thấy sự khác biệt — không có MCP, mỗi tích hợp yêu cầu dây nối điểm-điểm tùy chỉnh; với MCP, một giao thức duy nhất kết nối ứng dụng của bạn với bất kỳ công cụ nào:

<img src="../../../translated_images/vi/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Trước MCP: Tích hợp điểm-điểm phức tạp. Sau MCP: Một giao thức, vô tận khả năng.*

MCP giải quyết một vấn đề nền tảng trong phát triển AI: mỗi tích hợp là tùy chỉnh. Muốn truy cập GitHub? Mã tùy chỉnh. Muốn đọc tập tin? Mã tùy chỉnh. Muốn truy vấn cơ sở dữ liệu? Mã tùy chỉnh. Và không một tích hợp nào trong số này hoạt động với các ứng dụng AI khác.

MCP tiêu chuẩn hóa điều đó. Một máy chủ MCP cung cấp các công cụ với mô tả rõ ràng và bảng mô tả tham số. Bất kỳ client MCP nào cũng có thể kết nối, khám phá công cụ có sẵn và sử dụng chúng. Xây dựng một lần, dùng khắp mọi nơi.

Sơ đồ dưới đây minh họa kiến trúc này — một client MCP duy nhất (ứng dụng AI của bạn) kết nối tới nhiều máy chủ MCP, mỗi máy chủ cung cấp bộ công cụ riêng của họ qua giao thức chuẩn:

<img src="../../../translated_images/vi/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Kiến trúc Giao Thức Ngữ Cảnh Mô Hình - khám phá và thực thi công cụ chuẩn hóa*

## MCP Hoạt Động Như Thế Nào

Trong lõi, MCP sử dụng kiến trúc phân lớp. Ứng dụng Java của bạn (client MCP) khám phá các công cụ có sẵn, gửi các yêu cầu JSON-RPC qua lớp truyền tải (Stdio hoặc HTTP), và máy chủ MCP thực thi thao tác rồi trả về kết quả. Sơ đồ sau phân tích từng lớp của giao thức này:

<img src="../../../translated_images/vi/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Cách MCP hoạt động bên trong — clients khám phá công cụ, trao đổi tin nhắn JSON-RPC, và thực thi thao tác qua lớp truyền tải.*

**Kiến Trúc Máy Chủ-Khách**

MCP sử dụng mô hình client-server. Máy chủ cung cấp công cụ - đọc tập tin, truy vấn cơ sở dữ liệu, gọi API. Clients (ứng dụng AI của bạn) kết nối với máy chủ và sử dụng công cụ của họ.

Để sử dụng MCP với LangChain4j, thêm dependency Maven sau:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Khám Phá Công Cụ**

Khi client của bạn kết nối tới máy chủ MCP, nó hỏi "Bạn có những công cụ nào?" Máy chủ sẽ phản hồi với danh sách công cụ có sẵn, mỗi công cụ đi kèm mô tả và bảng tham số. Agent AI của bạn có thể quyết định công cụ nào nên dùng dựa trên yêu cầu của người dùng. Sơ đồ dưới đây trình bày quá trình bắt tay — client gửi yêu cầu `tools/list` và máy chủ trả về công cụ hiện có với mô tả và bảng tham số:

<img src="../../../translated_images/vi/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI khám phá các công cụ có sẵn khi khởi động — nó biết khả năng hiện có và có thể quyết định công cụ nào sẽ dùng.*

**Cơ Chế Truyền Tải**

MCP hỗ trợ các cơ chế truyền tải khác nhau. Hai lựa chọn là Stdio (cho giao tiếp subprocess cục bộ) và HTTP có thể streaming (cho máy chủ remote). Module này trình diễn truyền tải Stdio:

<img src="../../../translated_images/vi/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*Cơ chế truyền tải MCP: HTTP cho máy chủ remote, Stdio cho tiến trình cục bộ*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Dành cho tiến trình cục bộ. Ứng dụng của bạn sinh ra một máy chủ dưới dạng subprocess và giao tiếp qua stdin/stdout. Hữu ích cho truy cập hệ thống tập tin hoặc công cụ dòng lệnh.

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

Máy chủ `@modelcontextprotocol/server-filesystem` cung cấp các công cụ sau, tất cả được giới hạn trong các thư mục bạn chỉ định:

| Công Cụ | Mô Tả |
|------|-------------|
| `read_file` | Đọc nội dung của một tập tin đơn |
| `read_multiple_files` | Đọc nhiều tập tin trong một lần gọi |
| `write_file` | Tạo mới hoặc ghi đè lên một tập tin |
| `edit_file` | Thực hiện tìm và thay thế có mục tiêu |
| `list_directory` | Liệt kê tập tin và thư mục tại một đường dẫn |
| `search_files` | Tìm kiếm tập tin đệ quy theo mẫu |
| `get_file_info` | Lấy thông tin siêu dữ liệu tập tin (kích thước, dấu thời gian, quyền truy cập) |
| `create_directory` | Tạo thư mục (bao gồm thư mục cha) |
| `move_file` | Di chuyển hoặc đổi tên tập tin hoặc thư mục |

Sơ đồ dưới đây cho thấy cách truyền tải Stdio hoạt động khi chạy — ứng dụng Java của bạn sinh ra máy chủ MCP như một tiến trình con và họ giao tiếp qua các ống stdin/stdout, không qua mạng hoặc HTTP:

<img src="../../../translated_images/vi/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Truyền tải Stdio hoạt động — ứng dụng của bạn sinh máy chủ MCP dưới dạng tiến trình con và giao tiếp qua ống stdin/stdout.*

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) và hỏi:
> - "Truyền tải Stdio hoạt động như thế nào và khi nào tôi nên dùng thay vì HTTP?"
> - "LangChain4j quản lý vòng đời các tiến trình máy chủ MCP được sinh ra như thế nào?"
> - "Hệ quả bảo mật khi cho AI truy cập hệ thống tập tin là gì?"

## Module Agentic

Trong khi MCP cung cấp các công cụ chuẩn hóa, module **agentic** của LangChain4j cung cấp cách khai báo để xây dựng các agent điều phối các công cụ đó. Annotation `@Agent` và `AgenticServices` cho phép bạn định nghĩa hành vi agent qua giao diện thay vì code mệnh lệnh.

Trong module này, bạn sẽ khám phá mô hình **Agent Giám Sát (Supervisor Agent)** — một cách tiếp cận agentic AI nâng cao, nơi một agent "giám sát" quyết định động các sub-agent nào được kích hoạt dựa trên yêu cầu người dùng. Chúng ta sẽ kết hợp cả hai khái niệm bằng cách trang bị cho một sub-agent khả năng truy cập tập tin dựa trên MCP.

Để sử dụng module agentic, thêm dependency Maven sau:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Lưu ý:** Module `langchain4j-agentic` sử dụng thuộc tính phiên bản riêng (`langchain4j.mcp.version`) vì nó được phát hành theo lịch trình khác với thư viện cốt lõi LangChain4j.

> **⚠️ Thử nghiệm:** Module `langchain4j-agentic` đang trong giai đoạn **thử nghiệm** và có thể thay đổi. Cách ổn định để xây dựng trợ lý AI vẫn là dùng `langchain4j-core` với công cụ tùy chỉnh (Module 04).

## Chạy Các Ví Dụ

### Yêu Cầu Tiền Đề

- Đã hoàn thành [Module 04 - Công Cụ](../04-tools/README.md) (module này xây dựng dựa trên khái niệm công cụ tùy chỉnh và so sánh với công cụ MCP)
- File `.env` trong thư mục gốc chứa thông tin đăng nhập Azure (được tạo bởi `azd up` trong Module 01)
- Java 21+, Maven 3.9+
- Node.js 16+ và npm (cho máy chủ MCP)

> **Lưu ý:** Nếu bạn chưa thiết lập biến môi trường, xem [Module 01 - Giới Thiệu](../01-introduction/README.md) để biết hướng dẫn triển khai (`azd up` tự động tạo file `.env`), hoặc sao chép `.env.example` thành `.env` trong thư mục gốc và điền thông tin của bạn.

## Bắt Đầu Nhanh

**Dùng VS Code:** Nhấp chuột phải vào bất cứ file demo nào trong Explorer và chọn **"Run Java"**, hoặc sử dụng cấu hình launch từ panel Run and Debug (đảm bảo file `.env` của bạn đã được cấu hình với thông tin Azure).

**Dùng Maven:** Ngoài ra, bạn có thể chạy qua dòng lệnh với ví dụ bên dưới.

### Các Thao Tác Tập Tin (Stdio)

Đây là minh họa các công cụ dựa trên subprocess cục bộ.

**✅ Không cần yêu cầu tiền đề** - máy chủ MCP được sinh ra tự động.

**Dùng các Script Khởi Động (Khuyến nghị):**

Các script khởi động tự động tải biến môi trường từ file `.env` gốc:

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

**Dùng VS Code:** Nhấp chuột phải vào `StdioTransportDemo.java` và chọn **"Run Java"** (đảm bảo file `.env` đã được cấu hình).

Ứng dụng tự động sinh ra máy chủ MCP hệ thống tập tin và đọc một tập tin cục bộ. Chú ý cách quản lý subprocess được xử lý cho bạn.

**Đầu ra dự kiến:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agent Giám Sát

Mô hình **Agent Giám Sát** là hình thức **linh hoạt** của agentic AI. Một Supervisor dùng LLM để tự quyết định agent con nào được gọi dựa trên yêu cầu người dùng. Trong ví dụ tiếp theo, chúng ta kết hợp truy cập tập tin dựa trên MCP với một agent LLM để tạo một quy trình đọc tập tin → tạo báo cáo được giám sát.

Trong demo, `FileAgent` đọc tập tin dùng công cụ MCP filesystem, và `ReportAgent` tạo báo cáo cấu trúc với bản tóm tắt điều hành (1 câu), 3 điểm chính, và khuyến nghị. Supervisor điều phối luồng này tự động:

<img src="../../../translated_images/vi/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Supervisor dùng LLM của nó để quyết định agent nào được gọi và theo thứ tự nào — không cần điều hướng cứng mã.*

Đây là quy trình làm việc cụ thể cho pipeline tập tin thành báo cáo của chúng ta:

<img src="../../../translated_images/vi/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent đọc tập tin qua các công cụ MCP, rồi ReportAgent chuyển nội dung thô thành báo cáo cấu trúc.*

Mỗi agent lưu đầu ra của nó vào **Phạm Vi Agentic** (bộ nhớ chia sẻ), cho phép các agent phía sau truy cập kết quả trước đó. Điều này cho thấy công cụ MCP tích hợp liền mạch vào các workflow agentic — Supervisor không cần biết *cách* đọc tập tin, chỉ biết `FileAgent` có thể làm điều đó.

#### Chạy Demo

Các script khởi động tự động tải biến môi trường từ file `.env` gốc:

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

**Dùng VS Code:** Nhấp chuột phải vào `SupervisorAgentDemo.java` và chọn **"Run Java"** (đảm bảo `.env` được cấu hình).

#### Cách Giám Sát Hoạt Động

Trước khi xây dựng agent, bạn cần kết nối transport MCP với client và đóng gói nó dưới dạng `ToolProvider`. Đây là cách các công cụ của máy chủ MCP trở nên khả dụng cho agent của bạn:

```java
// Tạo một client MCP từ transport
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Bao bọc client như một ToolProvider — điều này kết nối các công cụ MCP vào LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Bây giờ bạn có thể tiêm `mcpToolProvider` vào bất kỳ agent nào cần công cụ MCP:

```java
// Bước 1: FileAgent đọc các tệp sử dụng công cụ MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Có công cụ MCP cho các thao tác tệp
        .build();

// Bước 2: ReportAgent tạo các báo cáo có cấu trúc
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor điều phối quy trình làm việc từ tệp → báo cáo
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Trả về báo cáo cuối cùng
        .build();

// Supervisor quyết định kích hoạt các agent nào dựa trên yêu cầu
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Chiến Lược Phản Hồi

Khi cấu hình một `SupervisorAgent`, bạn chỉ định cách nó nên hình thành câu trả lời cuối cùng cho người dùng sau khi các sub-agent hoàn thành nhiệm vụ. Sơ đồ dưới đây cho thấy ba chiến lược có sẵn — LAST trả về đầu ra của agent cuối cùng trực tiếp, SUMMARY tổng hợp tất cả đầu ra qua LLM, và SCORED chọn đầu ra nào điểm cao hơn so với yêu cầu gốc:

<img src="../../../translated_images/vi/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Ba chiến lược để Supervisor hình thành phản hồi cuối cùng — chọn dựa trên bạn muốn đầu ra của agent cuối cùng, bản tóm tắt tổng hợp, hoặc lựa chọn điểm cao nhất.*

Chiến lược có sẵn gồm:

| Chiến Lược | Mô Tả |
|----------|-------------|
| **LAST** | Supervisor trả về đầu ra của sub-agent hoặc công cụ cuối cùng được gọi. Điều này hữu ích khi agent cuối cùng trong workflow được thiết kế để tạo ra câu trả lời hoàn chỉnh cuối cùng (ví dụ: một "Agent Tóm tắt" trong pipeline nghiên cứu). |
| **SUMMARY** | Supervisor sử dụng Language Model (LLM) nội bộ của mình để tổng hợp bản tóm tắt toàn bộ tương tác và tất cả đầu ra của sub-agent, sau đó trả về bản tóm tắt đó như câu trả lời cuối cùng. Điều này cung cấp một câu trả lời sạch, tổng hợp cho người dùng. |
| **SCORED** | Hệ thống sử dụng một LLM nội bộ để chấm điểm cả phản hồi LAST và bản tóm tắt SUMMARY dựa trên yêu cầu gốc của người dùng, trả về đầu ra nào có điểm cao hơn. |
Xem [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) để biết triển khai đầy đủ.

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) và hỏi:
> - "Supervisor quyết định gọi những agent nào như thế nào?"
> - "Sự khác biệt giữa Supervisor và các mẫu workflow Sequential là gì?"
> - "Làm thế nào để tùy chỉnh hành vi lập kế hoạch của Supervisor?"

#### Hiểu kết quả đầu ra

Khi bạn chạy demo, bạn sẽ thấy một quy trình có cấu trúc về cách Supervisor điều phối nhiều agent. Dưới đây là ý nghĩa của từng phần:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Phần tiêu đề** giới thiệu khái niệm workflow: một dòng xử lý tập trung từ việc đọc file đến tạo báo cáo.

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

**Sơ đồ Workflow** thể hiện luồng dữ liệu giữa các agent. Mỗi agent có một vai trò cụ thể:
- **FileAgent** đọc file sử dụng công cụ MCP và lưu nội dung thô vào `fileContent`
- **ReportAgent** sử dụng nội dung đó và tạo báo cáo có cấu trúc trong `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Yêu cầu của người dùng** hiển thị nhiệm vụ. Supervisor phân tích và quyết định gọi FileAgent → ReportAgent.

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

**Điều phối của Supervisor** thể hiện luồng 2 bước đang hoạt động:
1. **FileAgent** đọc file qua MCP và lưu nội dung
2. **ReportAgent** nhận nội dung và tạo báo cáo có cấu trúc

Supervisor đưa ra những quyết định này **một cách tự động** dựa trên yêu cầu của người dùng.

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

#### Giải thích về các tính năng của Module Agentic

Ví dụ này trình bày một số tính năng nâng cao của module agentic. Hãy cùng xem xét kỹ Agentic Scope và Agent Listeners.

**Agentic Scope** cho thấy bộ nhớ chia sẻ nơi các agent lưu kết quả của mình dùng `@Agent(outputKey="...")`. Điều này cho phép:
- Các agent sau truy cập kết quả của các agent trước
- Supervisor tổng hợp phản hồi cuối cùng
- Bạn kiểm tra được thứ mà mỗi agent tạo ra

Sơ đồ dưới đây cho thấy cách Agentic Scope hoạt động như bộ nhớ chia sẻ trong quy trình từ file đến báo cáo — FileAgent ghi kết quả dưới khóa `fileContent`, ReportAgent đọc và ghi kết quả riêng dưới `report`:

<img src="../../../translated_images/vi/agentic-scope.95ef488b6c1d02ef.webp" alt="Bộ nhớ chia sẻ Agentic Scope" width="800"/>

*Agentic Scope hoạt động như bộ nhớ chia sẻ — FileAgent ghi `fileContent`, ReportAgent đọc và ghi `report`, và mã của bạn đọc kết quả cuối cùng.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Dữ liệu tệp thô từ FileAgent
String report = scope.readState("report");            // Báo cáo có cấu trúc từ ReportAgent
```

**Agent Listeners** cho phép giám sát và gỡ lỗi quá trình thực thi agent. Kết quả từng bước bạn thấy trong demo đến từ một AgentListener gắn vào từng lần gọi agent:
- **beforeAgentInvocation** - Gọi khi Supervisor chọn agent, cho bạn thấy agent nào được chọn và lý do
- **afterAgentInvocation** - Gọi khi agent hoàn thành, hiển thị kết quả của nó
- **inheritedBySubagents** - Khi true, listener giám sát tất cả agent trong hệ thống phân cấp

Sơ đồ sau đây minh họa toàn bộ vòng đời của Agent Listener, bao gồm cách `onError` xử lý lỗi trong quá trình thực thi agent:

<img src="../../../translated_images/vi/agent-listeners.784bfc403c80ea13.webp" alt="Vòng đời Agent Listeners" width="800"/>

*Agent Listeners gắn vào vòng đời thực thi — giám sát khi agent bắt đầu, hoàn thành, hoặc gặp lỗi.*

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
        return true; // Phát triển đến tất cả các đại lý phụ
    }
};
```

Ngoài mẫu Supervisor, module `langchain4j-agentic` cung cấp nhiều mẫu workflow mạnh mẽ. Sơ đồ dưới đây thể hiện tất cả năm mẫu — từ pipeline tuần tự đơn giản đến các workflow phê duyệt có người tham gia:

<img src="../../../translated_images/vi/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Mẫu Workflow Agent" width="800"/>

*Năm mẫu workflow để điều phối agent — từ pipeline tuần tự đơn giản đến workflow phê duyệt có người tham gia.*

| Mẫu | Mô tả | Trường hợp sử dụng |
|---------|-------------|----------|
| **Sequential** | Thực thi agent theo thứ tự, đầu ra chuyển cho agent kế | Pipeline: nghiên cứu → phân tích → báo cáo |
| **Parallel** | Chạy agent đồng thời | Công việc độc lập: thời tiết + tin tức + chứng khoán |
| **Loop** | Lặp lại đến khi đạt điều kiện | Đánh giá chất lượng: tinh chỉnh đến khi điểm ≥ 0.8 |
| **Conditional** | Chuyển hướng theo điều kiện | Phân loại → chuyển đến chuyên gia |
| **Human-in-the-Loop** | Thêm điểm kiểm tra con người | Workflow phê duyệt, duyệt nội dung |

## Khái niệm chính

Bây giờ bạn đã khám phá MCP và module agentic trong thực tế, hãy tóm tắt khi nào nên dùng mỗi cách.

Một trong những lợi thế lớn nhất của MCP là hệ sinh thái ngày càng phát triển. Sơ đồ dưới đây cho thấy cách một giao thức chung kết nối ứng dụng AI của bạn với nhiều máy chủ MCP — từ truy cập hệ thống file và cơ sở dữ liệu đến GitHub, email, web scraping, và nhiều hơn nữa:

<img src="../../../translated_images/vi/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="Hệ sinh thái MCP" width="800"/>

*MCP tạo ra một hệ sinh thái giao thức chung — bất kỳ máy chủ tương thích MCP nào cũng làm việc với bất kỳ client tương thích MCP nào, cho phép chia sẻ công cụ giữa các ứng dụng.*

**MCP** phù hợp khi bạn muốn tận dụng hệ sinh thái công cụ hiện có, xây dựng các công cụ có thể dùng chung cho nhiều ứng dụng, tích hợp dịch vụ bên thứ ba với giao thức chuẩn, hoặc thay đổi cài đặt công cụ mà không cần đổi mã.

**Module Agentic** làm việc tốt nhất khi bạn muốn định nghĩa agent theo kiểu khai báo với chú thích `@Agent`, cần điều phối workflow (tuần tự, vòng lặp, song song), ưu tiên thiết kế agent theo giao diện thay vì mã lệnh, hoặc kết hợp nhiều agent chia sẻ dữ liệu qua `outputKey`.

**Mẫu Supervisor Agent** nổi bật khi workflow không thể dự đoán trước và bạn muốn LLM quyết định, khi có nhiều agent chuyên biệt cần điều phối động, khi xây dựng hệ thống hội thoại điều hướng các khả năng khác nhau, hoặc khi bạn muốn hành vi agent linh hoạt, thích ứng nhất.

Để giúp bạn lựa chọn giữa các phương pháp tùy chỉnh `@Tool` từ Module 04 và công cụ MCP từ module này, so sánh sau đây làm nổi bật các điểm đổi trade-off chính — công cụ tùy chỉnh cho phép liên kết chặt chẽ và an toàn kiểu đầy đủ cho logic ứng dụng cụ thể, trong khi công cụ MCP cung cấp tích hợp chuẩn hóa, tái sử dụng cho nhiều ứng dụng:

<img src="../../../translated_images/vi/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Công cụ Tùy chỉnh so với Công cụ MCP" width="800"/>

*Khi nào dùng phương pháp @Tool tùy chỉnh và khi nào dùng công cụ MCP — công cụ tùy chỉnh cho logic ứng dụng với an toàn kiểu đầy đủ, công cụ MCP cho tích hợp chuẩn hóa dùng xuyên ứng dụng.*

## Chúc mừng!

Bạn đã hoàn thành tất cả năm module của khóa học LangChain4j dành cho người mới bắt đầu! Dưới đây là hành trình học tập đầy đủ bạn đã trải qua — từ chat cơ bản đến các hệ thống agentic được hỗ trợ bởi MCP:

<img src="../../../translated_images/vi/course-completion.48cd201f60ac7570.webp" alt="Hoàn thành Khóa học" width="800"/>

*Hành trình học tập của bạn qua tất cả năm module — từ chat cơ bản đến hệ thống agentic có MCP.*

Bạn đã hoàn thành khóa học LangChain4j cho người mới bắt đầu. Bạn đã học được:

- Cách xây dựng AI hội thoại có bộ nhớ (Module 01)
- Mẫu kỹ thuật prompt cho các tác vụ khác nhau (Module 02)
- Cách cung cấp phản hồi dựa trên tài liệu với RAG (Module 03)
- Tạo agent AI cơ bản (trợ lý) với công cụ tùy chỉnh (Module 04)
- Tích hợp công cụ chuẩn hóa với LangChain4j MCP và module Agentic (Module 05)

### Tiếp theo là gì?

Sau khi hoàn thành các module, hãy khám phá [Hướng dẫn Kiểm thử](../docs/TESTING.md) để xem các khái niệm kiểm thử LangChain4j trong thực tế.

**Tài nguyên chính thức:**
- [Tài liệu LangChain4j](https://docs.langchain4j.dev/) - Hướng dẫn toàn diện và tham khảo API
- [GitHub LangChain4j](https://github.com/langchain4j/langchain4j) - Mã nguồn và ví dụ
- [Hướng dẫn LangChain4j](https://docs.langchain4j.dev/tutorials/) - Các tutorial theo bước cho nhiều trường hợp sử dụng

Cảm ơn bạn đã hoàn thành khóa học này!

---

**Điều hướng:** [← Trước: Module 04 - Công cụ](../04-tools/README.md) | [Quay lại Trang chính](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố từ chối trách nhiệm**:
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng bản dịch tự động có thể chứa lỗi hoặc sự không chính xác. Tài liệu gốc bằng ngôn ngữ nguyên bản của nó nên được coi là nguồn tham khảo chính xác. Đối với các thông tin quan trọng, nên sử dụng dịch thuật chuyên nghiệp bởi con người. Chúng tôi không chịu trách nhiệm đối với bất kỳ sự hiểu lầm hay giải thích sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->