# Module 05: Giao Thức Ngữ Cảnh Mô Hình (MCP)

## Mục Lục

- [Bạn Sẽ Học Gì](../../../05-mcp)
- [MCP là gì?](../../../05-mcp)
- [MCP Hoạt Động Như Thế Nào](../../../05-mcp)
- [Module Agentic](../../../05-mcp)
- [Chạy Các Ví Dụ](../../../05-mcp)
  - [Yêu Cầu Trước](../../../05-mcp)
- [Bắt Đầu Nhanh](../../../05-mcp)
  - [Các Thao Tác Tập Tin (Stdio)](../../../05-mcp)
  - [Agent Giám Sát](../../../05-mcp)
    - [Chạy Demo](../../../05-mcp)
    - [Giám Sát Hoạt Động Như Thế Nào](../../../05-mcp)
    - [Chiến Lược Phản Hồi](../../../05-mcp)
    - [Hiểu Về Đầu Ra](../../../05-mcp)
    - [Giải Thích Các Tính Năng Của Module Agentic](../../../05-mcp)
- [Khái Niệm Chính](../../../05-mcp)
- [Chúc Mừng!](../../../05-mcp)
  - [Tiếp Theo Là Gì?](../../../05-mcp)

## Bạn Sẽ Học Gì

Bạn đã xây dựng AI hội thoại, thành thạo với các prompt, căn cứ câu trả lời vào tài liệu, và tạo các agent với công cụ. Nhưng tất cả các công cụ đó đều được thiết kế riêng cho ứng dụng của bạn. Nếu bạn có thể cung cấp cho AI của mình quyền truy cập vào một hệ sinh thái công cụ chuẩn hóa mà bất kỳ ai cũng có thể tạo và chia sẻ thì sao? Trong module này, bạn sẽ học cách làm điều đó với Giao Thức Ngữ Cảnh Mô Hình (MCP) và module agentic của LangChain4j. Chúng ta sẽ trình bày trước một trình đọc file MCP đơn giản rồi sau đó cho thấy cách nó dễ dàng tích hợp vào các quy trình agentic nâng cao sử dụng mẫu Agent Giám Sát.

## MCP là gì?

Giao Thức Ngữ Cảnh Mô Hình (MCP) cung cấp chính xác điều đó - một cách chuẩn hóa để các ứng dụng AI khám phá và sử dụng các công cụ bên ngoài. Thay vì viết tích hợp tùy chỉnh cho từng nguồn dữ liệu hoặc dịch vụ, bạn kết nối với các máy chủ MCP công bố khả năng của họ theo một định dạng thống nhất. Agent AI của bạn có thể tự động phát hiện và sử dụng các công cụ này.

<img src="../../../translated_images/vi/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Trước MCP: Các tích hợp phức tạp điểm-điểm. Sau MCP: Một giao thức, vô vàn khả năng.*

MCP giải quyết một vấn đề cơ bản trong phát triển AI: mọi tích hợp đều tùy chỉnh. Muốn truy cập GitHub? Mã tùy chỉnh. Muốn đọc file? Mã tùy chỉnh. Muốn truy vấn cơ sở dữ liệu? Mã tùy chỉnh. Và không tích hợp nào trong số này làm việc với các ứng dụng AI khác.

MCP chuẩn hóa điều này. Một máy chủ MCP cung cấp công cụ với các mô tả rõ ràng và sơ đồ tham số. Bất kỳ client MCP nào cũng có thể kết nối, phát hiện các công cụ có sẵn và sử dụng chúng. Xây dựng một lần, dùng mọi nơi.

<img src="../../../translated_images/vi/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Kiến trúc Giao Thức Ngữ Cảnh Mô Hình - tiêu chuẩn hóa việc phát hiện và thực thi công cụ*

## MCP Hoạt Động Như Thế Nào

<img src="../../../translated_images/vi/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Cách MCP hoạt động bên trong — client khám phá công cụ, trao đổi các thông điệp JSON-RPC, và thực thi tác vụ qua lớp vận chuyển.*

**Kiến trúc Server-Client**

MCP sử dụng mô hình client-server. Máy chủ cung cấp công cụ - đọc file, truy vấn cơ sở dữ liệu, gọi API. Client (ứng dụng AI của bạn) kết nối tới máy chủ và sử dụng công cụ của họ.

Để sử dụng MCP với LangChain4j, thêm dependency Maven này:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Phát Hiện Công Cụ**

Khi client của bạn kết nối tới máy chủ MCP, nó hỏi "Bạn có những công cụ gì?" Máy chủ trả lời với danh sách các công cụ có sẵn, mỗi công cụ có mô tả và sơ đồ tham số. Agent AI của bạn sau đó quyết định công cụ nào sử dụng dựa trên yêu cầu người dùng.

<img src="../../../translated_images/vi/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI phát hiện các công cụ có sẵn khi khởi động — giờ nó biết những khả năng có và có thể chọn công cụ phù hợp để dùng.*

**Cơ Chế Vận Chuyển**

MCP hỗ trợ nhiều cơ chế vận chuyển khác nhau. Module này minh họa vận chuyển Stdio cho các process cục bộ:

<img src="../../../translated_images/vi/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*Cơ chế vận chuyển MCP: HTTP cho máy chủ từ xa, Stdio cho các process cục bộ*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Dành cho các process cục bộ. Ứng dụng của bạn tạo một máy chủ dưới dạng tiến trình con và giao tiếp qua đầu vào/đầu ra chuẩn. Hữu ích để truy cập hệ thống file hoặc công cụ dòng lệnh.

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

<img src="../../../translated_images/vi/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Vận chuyển Stdio trong thực tế — ứng dụng của bạn tạo máy chủ MCP như một process con và giao tiếp qua các ống stdin/stdout.*

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) và hỏi:
> - "Vận chuyển Stdio hoạt động như thế nào và khi nào nên dùng so với HTTP?"
> - "LangChain4j quản lý vòng đời các process máy chủ MCP được sinh ra như thế nào?"
> - "Những vấn đề an ninh khi cho AI truy cập hệ thống file là gì?"

## Module Agentic

Trong khi MCP cung cấp công cụ chuẩn hóa, module **agentic** của LangChain4j cung cấp cách khai báo để xây dựng các agent điều phối các công cụ đó. Annotation `@Agent` và `AgenticServices` cho phép bạn định nghĩa hành vi agent qua các interface thay vì mã lệnh mệnh lệnh.

Trong module này, bạn sẽ khám phá mẫu **Supervisor Agent** — một cách tiếp cận AI agentic tiên tiến nơi agent "giám sát" quyết định động agent con nào được gọi dựa trên yêu cầu người dùng. Chúng ta sẽ kết hợp hai khái niệm bằng cách cấp cho một trong các agent con khả năng truy cập file qua MCP.

Để sử dụng module agentic, thêm dependency Maven này:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **⚠️ Thử nghiệm:** Module `langchain4j-agentic` là **thử nghiệm** và có thể thay đổi. Cách ổn định để xây dựng trợ lý AI vẫn là `langchain4j-core` với công cụ tùy chỉnh (Module 04).

## Chạy Các Ví Dụ

### Yêu Cầu Trước

- Java 21+, Maven 3.9+
- Node.js 16+ và npm (cho máy chủ MCP)
- Các biến môi trường được cấu hình trong file `.env` (từ thư mục gốc):
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (giống Modules 01-04)

> **Lưu ý:** Nếu bạn chưa thiết lập các biến môi trường, xem [Module 00 - Bắt Đầu Nhanh](../00-quick-start/README.md) để biết hướng dẫn, hoặc sao chép `.env.example` thành `.env` trong thư mục gốc và điền giá trị.

## Bắt Đầu Nhanh

**Sử dụng VS Code:** Đơn giản nhấp chuột phải vào bất kỳ file demo nào trong Explorer và chọn **"Run Java"**, hoặc dùng cấu hình chạy trong bảng Run and Debug (đảm bảo bạn đã thêm token vào file `.env` trước).

**Sử dụng Maven:** Ngoài ra bạn có thể chạy từ dòng lệnh với các ví dụ dưới đây.

### Các Thao Tác Tập Tin (Stdio)

Ví dụ minh họa các công cụ dựa trên tiến trình con cục bộ.

**✅ Không cần yêu cầu trước** - máy chủ MCP sinh ra tự động.

**Sử dụng Script Khởi Động (Khuyến nghị):**

Các script khởi động tự động tải biến môi trường từ file `.env` ở thư mục gốc:

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

**Sử dụng VS Code:** Nhấp chuột phải vào `StdioTransportDemo.java` và chọn **"Run Java"** (đảm bảo file `.env` đã được cấu hình).

Ứng dụng tự động tạo máy chủ MCP filesystem và đọc một file cục bộ. Chú ý cách quản lý tiến trình con được xử lý cho bạn.

**Kết quả mong đợi:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agent Giám Sát

Mẫu **Supervisor Agent** là dạng AI agentic **linh hoạt**. Một Supervisor dùng LLM để tự động quyết định agent nào được gọi dựa trên yêu cầu người dùng. Trong ví dụ tiếp theo, chúng ta kết hợp truy cập file qua MCP với agent LLM để tạo quy trình đọc file → báo cáo có giám sát.

Trong demo, `FileAgent` đọc file bằng công cụ MCP filesystem, và `ReportAgent` tạo báo cáo có cấu trúc với bản tóm tắt điều hành (1 câu), 3 điểm chính, và các đề xuất. Supervisor điều phối quy trình này tự động:

<img src="../../../translated_images/vi/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Supervisor dùng LLM của mình để quyết định gọi agent nào và thứ tự — không cần mã cứng định tuyến.*

Đây là quy trình cụ thể cho pipeline file-to-report của chúng ta:

<img src="../../../translated_images/vi/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent đọc file qua công cụ MCP, sau đó ReportAgent biến nội dung thô thành báo cáo có cấu trúc.*

Mỗi agent lưu kết quả của mình vào **Agentic Scope** (bộ nhớ chia sẻ), cho phép các agent tiếp theo truy cập kết quả trước đó. Điều này minh họa cách công cụ MCP tích hợp liền mạch vào quy trình agentic — Supervisor không cần biết *cách* file được đọc, chỉ cần `FileAgent` có thể làm điều đó.

#### Chạy Demo

Các script khởi động tự động tải biến môi trường từ file `.env` ở thư mục gốc:

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

**Sử dụng VS Code:** Nhấp chuột phải vào `SupervisorAgentDemo.java` và chọn **"Run Java"** (đảm bảo `.env` đã được cấu hình).

#### Giám Sát Hoạt Động Như Thế Nào

```java
// Bước 1: FileAgent đọc các tệp bằng công cụ MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Có công cụ MCP cho các thao tác tệp
        .build();

// Bước 2: ReportAgent tạo ra các báo cáo có cấu trúc
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor điều phối quy trình làm việc từ tệp → báo cáo
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Trả về báo cáo cuối cùng
        .build();

// Supervisor quyết định gọi các agent nào dựa trên yêu cầu
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Chiến Lược Phản Hồi

Khi bạn cấu hình một `SupervisorAgent`, bạn chỉ định cách agent này nên tạo câu trả lời cuối cùng cho người dùng sau khi các sub-agent hoàn thành nhiệm vụ.

<img src="../../../translated_images/vi/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Ba chiến lược tạo phản hồi cuối cùng của Supervisor — chọn dựa trên việc bạn muốn đầu ra của agent cuối cùng, bản tóm tắt tổng hợp, hay lựa chọn điểm số cao nhất.*

Các chiến lược có sẵn là:

| Chiến lược | Mô tả |
|------------|-------|
| **LAST** | Supervisor trả về kết quả của agent hoặc công cụ cuối cùng được gọi. Dùng khi agent cuối cùng trong luồng được thiết kế để tạo câu trả lời hoàn chỉnh (ví dụ, một "Summary Agent" trong pipeline nghiên cứu). |
| **SUMMARY** | Supervisor dùng chính LLM nội bộ của nó để tổng hợp một bản tóm tắt toàn bộ tương tác và tất cả đầu ra của các agent con, sau đó trả bản tóm tắt này làm phản hồi cuối cùng. Điều này cung cấp câu trả lời tổng hợp rõ ràng cho người dùng. |
| **SCORED** | Hệ thống dùng LLM nội bộ để đánh giá cả phản hồi LAST và SUMMARY dựa trên yêu cầu người dùng gốc, rồi trả đầu ra có điểm số cao hơn. |

Xem [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) để biết triển khai hoàn chỉnh.

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) và hỏi:
> - "Supervisor quyết định gọi agent nào như thế nào?"
> - "Khác biệt giữa mẫu Supervisor và mẫu Quy trình Tuần tự là gì?"
> - "Làm sao để tùy chỉnh hành vi lập kế hoạch của Supervisor?"

#### Hiểu Về Đầu Ra

Khi bạn chạy demo, bạn sẽ thấy một hướng dẫn có cấu trúc về cách Supervisor điều phối nhiều agent. Đây là ý nghĩa từng phần:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Phần tiêu đề** giới thiệu khái niệm quy trình: một pipeline tập trung từ đọc file đến tạo báo cáo.

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

**Sơ đồ Quy trình** cho thấy luồng dữ liệu giữa các agent. Mỗi agent có vai trò cụ thể:
- **FileAgent** đọc file sử dụng công cụ MCP và lưu nội dung thô vào `fileContent`
- **ReportAgent** sử dụng nội dung đó để tạo báo cáo có cấu trúc trong `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Yêu Cầu Người Dùng** hiển thị nhiệm vụ. Supervisor phân tích và quyết định gọi FileAgent → ReportAgent.

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

**Điều Phối của Supervisor** cho thấy luồng 2 bước trong thực tế:
1. **FileAgent** đọc file qua MCP và lưu nội dung
2. **ReportAgent** nhận nội dung và tạo báo cáo có cấu trúc

Supervisor tự quyết định dựa trên yêu cầu người dùng.

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

#### Giải Thích Các Tính Năng Của Module Agentic

Ví dụ minh họa một số tính năng nâng cao của module agentic. Cùng xem xét kỹ hơn về Agentic Scope và Agent Listeners.

**Agentic Scope** cho thấy bộ nhớ chia sẻ nơi các agent lưu kết quả qua `@Agent(outputKey="...")`. Điều này cho phép:
- Agent sau truy cập đầu ra của agent trước
- Supervisor tổng hợp phản hồi cuối cùng
- Bạn kiểm tra kết quả từng agent tạo ra

<img src="../../../translated_images/vi/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope đóng vai trò là bộ nhớ chia sẻ — FileAgent ghi `fileContent`, ReportAgent đọc và ghi `report`, mã của bạn đọc kết quả cuối cùng.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Dữ liệu tệp thô từ FileAgent
String report = scope.readState("report");            // Báo cáo có cấu trúc từ ReportAgent
```

**Agent Listeners** cho phép giám sát và gỡ lỗi quá trình chạy agent. Đầu ra từng bước bạn thấy trong demo đến từ một AgentListener được gắn vào mỗi lần gọi agent:
- **beforeAgentInvocation** - Được gọi khi Supervisor chọn một agent, cho phép bạn thấy agent nào đã được chọn và lý do tại sao
- **afterAgentInvocation** - Được gọi khi một agent hoàn thành, hiển thị kết quả của nó
- **inheritedBySubagents** - Khi đúng, listener theo dõi tất cả các agent trong hệ thống phân cấp

<img src="../../../translated_images/vi/agent-listeners.784bfc403c80ea13.webp" alt="Vòng đời Agent Listeners" width="800"/>

*Agent Listeners kết nối vào vòng đời thực thi — theo dõi khi agents bắt đầu, hoàn thành, hoặc gặp lỗi.*

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
        return true; // Lan truyền tới tất cả các đại lý phụ
    }
};
```

Bên cạnh mô hình Supervisor, module `langchain4j-agentic` cung cấp nhiều mẫu quy trình làm việc và tính năng mạnh mẽ:

<img src="../../../translated_images/vi/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Mẫu Quy trình làm việc của Agent" width="800"/>

*Năm mẫu quy trình làm việc để điều phối agents — từ pipeline tuần tự đơn giản đến quy trình phê duyệt có sự tham gia của con người.*

| Mẫu | Mô tả | Trường hợp sử dụng |
|---------|-------------|----------|
| **Tuần tự** | Thực thi các agent theo thứ tự, đầu ra chảy sang bước tiếp theo | Pipeline: nghiên cứu → phân tích → báo cáo |
| **Song song** | Chạy các agent đồng thời | Tác vụ độc lập: thời tiết + tin tức + chứng khoán |
| **Vòng lặp** | Lặp lại cho đến khi điều kiện đạt được | Đánh giá chất lượng: tinh chỉnh cho đến điểm ≥ 0.8 |
| **Có điều kiện** | Định tuyến dựa trên điều kiện | Phân loại → chuyển đến agent chuyên môn |
| **Con người tham gia** | Thêm các điểm kiểm tra con người | Quy trình phê duyệt, xem xét nội dung |

## Khái niệm chính

Bây giờ bạn đã khám phá MCP và module agentic trong thực tế, hãy tóm tắt khi nào nên dùng từng cách tiếp cận.

<img src="../../../translated_images/vi/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="Hệ sinh thái MCP" width="800"/>

*MCP tạo ra một hệ sinh thái giao thức phổ quát — bất kỳ server tương thích MCP nào cũng làm việc với bất kỳ client tương thích MCP nào, cho phép chia sẻ công cụ xuyên ứng dụng.*

**MCP** lý tưởng khi bạn muốn tận dụng hệ sinh thái công cụ hiện có, xây dựng công cụ mà nhiều ứng dụng có thể dùng chung, tích hợp dịch vụ bên thứ ba qua giao thức chuẩn, hoặc thay thế triển khai công cụ mà không đổi code.

**Module Agentic** hoạt động tốt nhất khi bạn muốn định nghĩa agent khai báo với chú thích `@Agent`, cần điều phối workflow (tuần tự, vòng lặp, song song), ưu tiên thiết kế agent dựa trên interface thay vì code mệnh lệnh, hoặc kết hợp nhiều agent cùng chia sẻ đầu ra qua `outputKey`.

**Mẫu Supervisor Agent** nổi bật khi workflow không thể dự đoán trước và bạn muốn LLM quyết định, khi có nhiều agent chuyên biệt cần điều phối động, xây dựng hệ thống hội thoại định tuyến đến nhiều năng lực khác nhau, hoặc khi bạn muốn hành vi agent linh hoạt và thích ứng nhất.

<img src="../../../translated_images/vi/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Công cụ Tùy chỉnh so với Công cụ MCP" width="800"/>

*Khi nào dùng phương thức @Tool tùy chỉnh so với công cụ MCP — công cụ tùy chỉnh cho logic ứng dụng riêng với an toàn kiểu đầy đủ, công cụ MCP cho tích hợp chuẩn dùng chung ứng dụng.*

## Chúc mừng!

<img src="../../../translated_images/vi/course-completion.48cd201f60ac7570.webp" alt="Hoàn thành Khóa học" width="800"/>

*Hành trình học của bạn qua năm module — từ trò chuyện cơ bản đến hệ thống agentic được hỗ trợ bởi MCP.*

Bạn đã hoàn thành khóa học LangChain4j dành cho người mới bắt đầu. Bạn đã học:

- Cách xây dựng AI hội thoại có bộ nhớ (Module 01)
- Mẫu kỹ thuật prompt cho các tác vụ khác nhau (Module 02)
- Căn cứ phản hồi trên tài liệu với RAG (Module 03)
- Tạo agent AI cơ bản (trợ lý) với công cụ tùy chỉnh (Module 04)
- Tích hợp công cụ chuẩn hóa với các module LangChain4j MCP và Agentic (Module 05)

### Tiếp theo là gì?

Sau khi hoàn thành các module, hãy khám phá [Hướng dẫn Kiểm thử](../docs/TESTING.md) để xem các khái niệm kiểm thử LangChain4j trong thực tế.

**Tài nguyên chính thức:**
- [Tài liệu LangChain4j](https://docs.langchain4j.dev/) - Hướng dẫn toàn diện và tham khảo API
- [LangChain4j trên GitHub](https://github.com/langchain4j/langchain4j) - Mã nguồn và ví dụ
- [Hướng dẫn LangChain4j](https://docs.langchain4j.dev/tutorials/) - Hướng dẫn từng bước cho các trường hợp sử dụng khác nhau

Cảm ơn bạn đã hoàn thành khóa học này!

---

**Điều hướng:** [← Trước: Module 04 - Công cụ](../04-tools/README.md) | [Quay lại Trang chính](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố từ chối trách nhiệm**:
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ nguyên bản nên được xem là nguồn tin chính xác và đáng tin cậy. Đối với thông tin quan trọng, nên sử dụng bản dịch do người dịch chuyên nghiệp thực hiện. Chúng tôi không chịu trách nhiệm cho bất kỳ sự hiểu lầm hoặc giải thích sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->