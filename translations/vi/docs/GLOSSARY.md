# Bảng Thuật Ngữ LangChain4j

## Mục Lục

- [Khái Niệm Cốt Lõi](../../../docs)
- [Các Thành Phần LangChain4j](../../../docs)
- [Khái Niệm AI/ML](../../../docs)
- [Hàng Rào Bảo Vệ](../../../docs)
- [Kỹ Thuật Prompt](../../../docs)
- [RAG (Tạo Văn Bản Kèm Truy Xuất)](../../../docs)
- [Agent và Công Cụ](../../../docs)
- [Module Agentic](../../../docs)
- [Giao Thức Ngữ Cảnh Mô Hình (MCP)](../../../docs)
- [Dịch Vụ Azure](../../../docs)
- [Kiểm Thử và Phát Triển](../../../docs)

Tham khảo nhanh các thuật ngữ và khái niệm được sử dụng trong suốt khóa học.

## Khái Niệm Cốt Lõi

**AI Agent** - Hệ thống sử dụng AI để suy luận và hành động một cách tự động. [Module 04](../04-tools/README.md)

**Chain** - Chuỗi các thao tác mà kết quả đầu ra được truyền vào bước tiếp theo.

**Chunking** - Phân tách tài liệu thành các mảnh nhỏ hơn. Thông thường: 300-500 token với phần chồng lặp. [Module 03](../03-rag/README.md)

**Context Window** - Số token tối đa mà mô hình có thể xử lý. GPT-5.2: 400K token (tối đa 272K đầu vào, 128K đầu ra).

**Embeddings** - Vector số biểu diễn ý nghĩa văn bản. [Module 03](../03-rag/README.md)

**Function Calling** - Mô hình tạo các yêu cầu có cấu trúc để gọi hàm bên ngoài. [Module 04](../04-tools/README.md)

**Hallucination** - Khi mô hình tạo ra thông tin sai nhưng có vẻ hợp lý.

**Prompt** - Văn bản đầu vào cho mô hình ngôn ngữ. [Module 02](../02-prompt-engineering/README.md)

**Semantic Search** - Tìm kiếm theo ý nghĩa sử dụng embeddings, không phải từ khóa. [Module 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: không có bộ nhớ. Stateful: duy trì lịch sử cuộc trò chuyện. [Module 01](../01-introduction/README.md)

**Tokens** - Đơn vị văn bản cơ bản mà mô hình xử lý. Ảnh hưởng đến chi phí và giới hạn. [Module 01](../01-introduction/README.md)

**Tool Chaining** - Thực thi công cụ tuần tự nơi kết quả đầu ra điều chỉnh cuộc gọi tiếp theo. [Module 04](../04-tools/README.md)

## Các Thành Phần LangChain4j

**AiServices** - Tạo giao diện dịch vụ AI kiểu an toàn.

**OpenAiOfficialChatModel** - Khách hàng đồng nhất cho các mô hình OpenAI và Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Tạo embeddings sử dụng khách OpenAI chính thức (hỗ trợ cả OpenAI và Azure OpenAI).

**ChatModel** - Giao diện cốt lõi cho các mô hình ngôn ngữ.

**ChatMemory** - Duy trì lịch sử cuộc trò chuyện.

**ContentRetriever** - Tìm các đoạn tài liệu phù hợp cho RAG.

**DocumentSplitter** - Phân tách tài liệu thành các đoạn nhỏ.

**EmbeddingModel** - Chuyển đổi văn bản thành vector số.

**EmbeddingStore** - Lưu trữ và truy xuất embeddings.

**MessageWindowChatMemory** - Duy trì cửa sổ trượt các tin nhắn gần đây.

**PromptTemplate** - Tạo prompt tái sử dụng với các chỗ giữ chỗ `{{variable}}`.

**TextSegment** - Đoạn văn bản có metadata. Dùng trong RAG.

**ToolExecutionRequest** - Đại diện cho yêu cầu thực thi công cụ.

**UserMessage / AiMessage / SystemMessage** - Các loại tin nhắn trong cuộc trò chuyện.

## Khái Niệm AI/ML

**Few-Shot Learning** - Cung cấp ví dụ trong prompt. [Module 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - Các mô hình AI được huấn luyện trên lượng lớn dữ liệu văn bản.

**Reasoning Effort** - Tham số GPT-5.2 điều khiển độ sâu suy nghĩ. [Module 02](../02-prompt-engineering/README.md)

**Temperature** - Điều khiển độ ngẫu nhiên của đầu ra. Thấp = xác định, cao = sáng tạo.

**Vector Database** - Cơ sở dữ liệu chuyên biệt cho embeddings. [Module 03](../03-rag/README.md)

**Zero-Shot Learning** - Thực hiện nhiệm vụ mà không có ví dụ. [Module 02](../02-prompt-engineering/README.md)

## Hàng Rào Bảo Vệ - [Module 00](../00-quick-start/README.md)

**Defense in Depth** - Phương pháp bảo mật đa lớp kết hợp hàng rào ứng dụng với bộ lọc an toàn nhà cung cấp.

**Hard Block** - Nhà cung cấp trả lỗi HTTP 400 cho vi phạm nội dung nghiêm trọng.

**InputGuardrail** - Giao diện LangChain4j để kiểm tra đầu vào người dùng trước khi tới LLM. Tiết kiệm chi phí và giảm độ trễ bằng cách chặn prompt độc hại sớm.

**InputGuardrailResult** - Kiểu trả về cho kiểm tra guardrail: `success()` hoặc `fatal("lý do")`.

**OutputGuardrail** - Giao diện xác thực phản hồi AI trước khi trả cho người dùng.

**Provider Safety Filters** - Bộ lọc nội dung tích hợp sẵn từ nhà cung cấp AI (ví dụ GitHub Models) bắt vi phạm ngay cấp API.

**Soft Refusal** - Mô hình từ chối trả lời một cách lịch sự mà không gây lỗi.

## Kỹ Thuật Prompt - [Module 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Suy luận từng bước để tăng độ chính xác.

**Constrained Output** - Áp đặt định dạng hoặc cấu trúc cụ thể.

**High Eagerness** - Mẫu GPT-5.2 cho suy luận kỹ lưỡng.

**Low Eagerness** - Mẫu GPT-5.2 cho trả lời nhanh.

**Multi-Turn Conversation** - Duy trì ngữ cảnh qua nhiều lượt trao đổi.

**Role-Based Prompting** - Thiết lập nhân vật cho mô hình qua tin nhắn hệ thống.

**Self-Reflection** - Mô hình tự đánh giá và cải thiện kết quả.

**Structured Analysis** - Khung đánh giá cố định.

**Task Execution Pattern** - Lập kế hoạch → Thực thi → Tóm tắt.

## RAG (Tạo Văn Bản Kèm Truy Xuất) - [Module 03](../03-rag/README.md)

**Document Processing Pipeline** - Tải → chia đoạn → nhúng → lưu trữ.

**In-Memory Embedding Store** - Kho lưu trữ không bền dùng cho thử nghiệm.

**RAG** - Kết hợp truy xuất dữ liệu với tạo văn bản để củng cố phản hồi.

**Similarity Score** - Đo lường (0-1) độ tương đồng ngữ nghĩa.

**Source Reference** - Metadata về nội dung được truy xuất.

## Agent và Công Cụ - [Module 04](../04-tools/README.md)

**@Tool Annotation** - Đánh dấu phương thức Java là công cụ có thể gọi bởi AI.

**ReAct Pattern** - Suy nghĩ → Hành động → Quan sát → Lặp lại.

**Session Management** - Tách biệt ngữ cảnh cho các người dùng khác nhau.

**Tool** - Hàm mà AI agent có thể gọi.

**Tool Description** - Tài liệu về mục đích và tham số của công cụ.

## Module Agentic - [Module 05](../05-mcp/README.md)

**@Agent Annotation** - Đánh dấu giao diện là agent AI với định nghĩa hành vi khai báo.

**Agent Listener** - Hook để giám sát thực thi agent qua `beforeAgentInvocation()` và `afterAgentInvocation()`.

**Agentic Scope** - Bộ nhớ chia sẻ nơi agent lưu kết quả sử dụng `outputKey` để agent khác sử dụng.

**AgenticServices** - Nhà máy tạo agent qua `agentBuilder()` và `supervisorBuilder()`.

**Conditional Workflow** - Định tuyến dựa trên điều kiện tới các agent chuyên gia khác nhau.

**Human-in-the-Loop** - Mẫu luồng công việc thêm điểm kiểm duyệt con người để phê duyệt hoặc xem xét nội dung.

**langchain4j-agentic** - Phụ thuộc Maven cho xây dựng agent khai báo (thử nghiệm).

**Loop Workflow** - Lặp thực thi agent đến khi thỏa điều kiện (ví dụ điểm chất lượng ≥ 0.8).

**outputKey** - Tham số chú thích agent chỉ ra nơi lưu kết quả trong Agentic Scope.

**Parallel Workflow** - Chạy nhiều agent đồng thời cho các nhiệm vụ độc lập.

**Response Strategy** - Cách supervisor tạo câu trả lời cuối cùng: LAST (cuối cùng), SUMMARY (tóm tắt), hoặc SCORED (được điểm).

**Sequential Workflow** - Thực thi agent theo thứ tự, kết quả đầu ra chảy sang bước tiếp theo.

**Supervisor Agent Pattern** - Mẫu agentic nâng cao nơi supervisor LLM quyết định động các sub-agent cần gọi.

## Giao Thức Ngữ Cảnh Mô Hình (MCP) - [Module 05](../05-mcp/README.md)

**langchain4j-mcp** - Phụ thuộc Maven cho tích hợp MCP trong LangChain4j.

**MCP** - Giao thức Ngữ Cảnh Mô Hình: chuẩn kết nối ứng dụng AI với công cụ bên ngoài. Xây một lần, dùng khắp nơi.

**MCP Client** - Ứng dụng kết nối tới máy chủ MCP để phát hiện và sử dụng công cụ.

**MCP Server** - Dịch vụ công khai công cụ qua MCP với mô tả rõ ràng và schema tham số.

**McpToolProvider** - Thành phần LangChain4j bao gói công cụ MCP dùng trong dịch vụ AI và agent.

**McpTransport** - Giao diện truyền thông MCP. Các triển khai gồm Stdio và HTTP.

**Stdio Transport** - Truyền thông process cục bộ qua stdin/stdout. Hữu ích với truy cập hệ thống file hoặc công cụ dòng lệnh.

**StdioMcpTransport** - Triển khai LangChain4j khởi tạo MCP server dưới dạng tiến trình con.

**Tool Discovery** - Client truy vấn server để lấy danh sách công cụ có mô tả và schema.

## Dịch Vụ Azure - [Module 01](../01-introduction/README.md)

**Azure AI Search** - Dịch vụ tìm kiếm đám mây với khả năng vector. [Module 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Triển khai tài nguyên Azure.

**Azure OpenAI** - Dịch vụ AI doanh nghiệp của Microsoft.

**Bicep** - Ngôn ngữ hạ tầng dưới dạng mã của Azure. [Hướng dẫn hạ tầng](../01-introduction/infra/README.md)

**Deployment Name** - Tên triển khai mô hình trong Azure.

**GPT-5.2** - Mô hình OpenAI mới nhất với kiểm soát suy luận. [Module 02](../02-prompt-engineering/README.md)

## Kiểm Thử và Phát Triển - [Hướng Dẫn Kiểm Thử](TESTING.md)

**Dev Container** - Môi trường phát triển dưới dạng container. [Cấu hình](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Sân chơi mô hình AI miễn phí. [Module 00](../00-quick-start/README.md)

**In-Memory Testing** - Kiểm thử với kho lưu trữ trong bộ nhớ.

**Integration Testing** - Kiểm thử với hạ tầng thực tế.

**Maven** - Công cụ tự động hóa xây dựng Java.

**Mockito** - Thư viện giả lập trong Java.

**Spring Boot** - Framework ứng dụng Java. [Module 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố miễn trừ trách nhiệm**:  
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng bản dịch tự động có thể chứa lỗi hoặc sự không chính xác. Tài liệu gốc bằng ngôn ngữ ban đầu nên được coi là nguồn tham khảo chính thức. Đối với thông tin quan trọng, khuyến nghị sử dụng dịch vụ dịch thuật chuyên nghiệp do con người thực hiện. Chúng tôi không chịu trách nhiệm về bất kỳ sự hiểu lầm hay giải thích sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->