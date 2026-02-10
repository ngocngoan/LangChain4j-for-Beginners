# Thuật Ngữ LangChain4j

## Mục Lục

- [Các Khái Niệm Cốt Lõi](../../../docs)
- [Các Thành Phần LangChain4j](../../../docs)
- [Khái Niệm AI/ML](../../../docs)
- [Hàng Rào Bảo Vệ](../../../docs)
- [Kỹ Thuật Prompt](../../../docs)
- [RAG (Tạo Sinh Kết Hợp Truy Xuất)](../../../docs)
- [Tác Nhân và Công Cụ](../../../docs)
- [Mô-đun Agentic](../../../docs)
- [Giao Thức Ngữ Cảnh Mô Hình (MCP)](../../../docs)
- [Dịch Vụ Azure](../../../docs)
- [Kiểm Thử và Phát Triển](../../../docs)

Tham khảo nhanh các thuật ngữ và khái niệm sử dụng xuyên suốt khóa học.

## Các Khái Niệm Cốt Lõi

**Tác Nhân AI** - Hệ thống sử dụng AI để suy luận và hành động tự động. [Mô-đun 04](../04-tools/README.md)

**Chuỗi (Chain)** - Chuỗi các thao tác mà kết quả đầu ra được dùng cho bước kế tiếp.

**Phân Đoạn (Chunking)** - Chia tài liệu thành các phần nhỏ hơn. Thông thường: 300-500 token có chồng lấn. [Mô-đun 03](../03-rag/README.md)

**Cửa Sổ Ngữ Cảnh** - Số token tối đa mà mô hình có thể xử lý. GPT-5.2: 400K token.

**Embedding** - Vector số biểu diễn ý nghĩa văn bản. [Mô-đun 03](../03-rag/README.md)

**Gọi Hàm (Function Calling)** - Mô hình tạo yêu cầu có cấu trúc để gọi hàm bên ngoài. [Mô-đun 04](../04-tools/README.md)

**Ảo Tưởng (Hallucination)** - Khi mô hình tạo ra thông tin sai nhưng có vẻ hợp lý.

**Prompt** - Đoạn văn bản đầu vào cho mô hình ngôn ngữ. [Mô-đun 02](../02-prompt-engineering/README.md)

**Tìm Kiếm Ngữ Nghĩa (Semantic Search)** - Tìm kiếm dựa trên ý nghĩa bằng embeddings, không dùng từ khóa. [Mô-đun 03](../03-rag/README.md)

**Trạng Thái vs Không Trạng Thái** - Không trạng thái: không ghi nhớ. Có trạng thái: duy trì lịch sử hội thoại. [Mô-đun 01](../01-introduction/README.md)

**Token** - Đơn vị văn bản cơ bản mà mô hình xử lý. Ảnh hưởng chi phí và giới hạn. [Mô-đun 01](../01-introduction/README.md)

**Chuỗi Công Cụ** - Thực thi công cụ tuần tự, đầu ra ảnh hưởng cuộc gọi kế tiếp. [Mô-đun 04](../04-tools/README.md)

## Các Thành Phần LangChain4j

**AiServices** - Tạo các giao diện dịch vụ AI kiểu an toàn.

**OpenAiOfficialChatModel** - Client thống nhất cho các mô hình OpenAI và Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Tạo embeddings bằng client OpenAI Official (hỗ trợ cả OpenAI và Azure OpenAI).

**ChatModel** - Giao diện chính cho các mô hình ngôn ngữ.

**ChatMemory** - Duy trì lịch sử hội thoại.

**ContentRetriever** - Tìm các đoạn tài liệu liên quan cho RAG.

**DocumentSplitter** - Chia tài liệu thành các đoạn.

**EmbeddingModel** - Chuyển văn bản thành vector số.

**EmbeddingStore** - Lưu trữ và truy xuất embeddings.

**MessageWindowChatMemory** - Duy trì cửa sổ trượt các tin nhắn gần nhất.

**PromptTemplate** - Tạo prompt có thể tái sử dụng với chỗ giữ chỗ `{{variable}}`.

**TextSegment** - Đoạn văn bản kèm metadata. Dùng trong RAG.

**ToolExecutionRequest** - Đại diện yêu cầu thực thi công cụ.

**UserMessage / AiMessage / SystemMessage** - Các loại tin nhắn hội thoại.

## Khái Niệm AI/ML

**Few-Shot Learning** - Cung cấp ví dụ trong prompt. [Mô-đun 02](../02-prompt-engineering/README.md)

**Mô Hình Ngôn Ngữ Lớn (LLM)** - Mô hình AI được huấn luyện trên lượng lớn dữ liệu văn bản.

**Nỗ Lực Lý Luận** - Tham số GPT-5.2 kiểm soát độ sâu suy nghĩ. [Mô-đun 02](../02-prompt-engineering/README.md)

**Nhiệt Độ** - Điều chỉnh độ ngẫu nhiên đầu ra. Thấp=định trước, cao=sáng tạo.

**Cơ Sở Dữ Liệu Vector** - Cơ sở dữ liệu chuyên biệt cho embeddings. [Mô-đun 03](../03-rag/README.md)

**Zero-Shot Learning** - Thực hiện nhiệm vụ không có ví dụ. [Mô-đun 02](../02-prompt-engineering/README.md)

## Hàng Rào Bảo Vệ - [Mô-đun 00](../00-quick-start/README.md)

**Phòng Thủ Nhiều Lớp** - Cách tiếp cận bảo mật đa tầng kết hợp hàng rào cấp ứng dụng với bộ lọc an toàn nhà cung cấp.

**Block Cứng** - Nhà cung cấp trả lỗi HTTP 400 cho vi phạm nội dung nghiêm trọng.

**InputGuardrail** - Giao diện LangChain4j để xác thực đầu vào người dùng trước khi tới LLM. Tiết kiệm chi phí và độ trễ bằng cách chặn prompt gây hại sớm.

**InputGuardrailResult** - Kiểu trả về xác thực hàng rào: `success()` hoặc `fatal("lý do")`.

**OutputGuardrail** - Giao diện để xác thực phản hồi AI trước khi trả lại người dùng.

**Bộ Lọc An Toàn Nhà Cung Cấp** - Bộ lọc nội dung tích hợp từ nhà cung cấp AI (ví dụ: GitHub Models) bắt lỗi ở cấp API.

**Từ Chối Mềm** - Mô hình lịch sự từ chối trả lời mà không gây lỗi.

## Kỹ Thuật Prompt - [Mô-đun 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Suy luận từng bước để tăng độ chính xác.

**Đầu Ra Có Giới Hạn** - Bắt buộc định dạng hoặc cấu trúc cụ thể.

**Độ Nhiệt Cao** - Mẫu GPT-5.2 cho suy luận kỹ lưỡng.

**Độ Nhiệt Thấp** - Mẫu GPT-5.2 cho câu trả lời nhanh.

**Hội Thoại Nhiều Lượt** - Duy trì ngữ cảnh qua các trao đổi.

**Prompt Theo Vai Trò** - Đặt persona mô hình qua tin nhắn hệ thống.

**Tự Phản Chiếu** - Mô hình đánh giá và cải thiện đầu ra.

**Phân Tích Cấu Trúc** - Khung đánh giá cố định.

**Mẫu Thực Thi Nhiệm Vụ** - Lập kế hoạch → Thực thi → Tổng kết.

## RAG (Tạo Sinh Kết Hợp Truy Xuất) - [Mô-đun 03](../03-rag/README.md)

**Quy Trình Xử Lý Tài Liệu** - Tải → phân đoạn → nhúng → lưu trữ.

**Kho Embedding Bộ Nhớ** - Lưu trữ không bền cho kiểm thử.

**RAG** - Kết hợp truy xuất với tạo sinh để dựa trên dữ liệu.

**Điểm Tương Đồng** - Đo lường (0-1) sự giống nghĩa.

**Tham Chiếu Nguồn** - Metadata về nội dung được truy xuất.

## Tác Nhân và Công Cụ - [Mô-đun 04](../04-tools/README.md)

**@Tool Annotation** - Đánh dấu phương thức Java làm công cụ gọi AI.

**Mẫu ReAct** - Suy luận → Hành động → Quan sát → Lặp lại.

**Quản Lý Phiên** - Ngữ cảnh riêng biệt cho từng người dùng.

**Tool** - Hàm mà tác nhân AI có thể gọi.

**Mô Tả Công Cụ** - Tài liệu mục đích và tham số công cụ.

## Mô-đun Agentic - [Mô-đun 05](../05-mcp/README.md)

**@Agent Annotation** - Đánh dấu giao diện làm tác nhân AI với định nghĩa hành vi khai báo.

**Agent Listener** - Hook theo dõi thực thi tác nhân qua `beforeAgentInvocation()` và `afterAgentInvocation()`.

**Agentic Scope** - Bộ nhớ chia sẻ nơi tác nhân lưu đầu ra dùng cho các tác nhân sau.

**AgenticServices** - Nhà máy tạo tác nhân dùng `agentBuilder()` và `supervisorBuilder()`.

**Quy Trình Có Điều Kiện** - Định tuyến theo điều kiện đến các tác nhân chuyên biệt khác nhau.

**Human-in-the-Loop** - Mẫu quy trình thêm điểm kiểm tra con người để phê duyệt hoặc xem xét nội dung.

**langchain4j-agentic** - Phụ thuộc Maven xây dựng tác nhân khai báo (thử nghiệm).

**Quy Trình Lặp** - Lặp thực thi tác nhân cho đến khi thỏa điều kiện (ví dụ: điểm chất lượng ≥ 0.8).

**outputKey** - Tham số annotation tác nhân chỉ nơi lưu kết quả trong Agentic Scope.

**Quy Trình Song Song** - Chạy nhiều tác nhân cùng lúc cho các nhiệm vụ độc lập.

**Chiến Lược Phản Hồi** - Cách giám sát viên dựng câu trả lời cuối: LAST, SUMMARY, hoặc SCORED.

**Quy Trình Tuần Tự** - Thực thi tác nhân theo thứ tự, đầu ra đi tới bước tiếp theo.

**Mẫu Tác Nhân Giám Sát** - Mẫu agentic nâng cao, giám sát viên LLM quyết định động tác nhân con nào gọi.

## Giao Thức Ngữ Cảnh Mô Hình (MCP) - [Mô-đun 05](../05-mcp/README.md)

**langchain4j-mcp** - Phụ thuộc Maven tích hợp MCP trong LangChain4j.

**MCP** - Giao thức ngữ cảnh mô hình: chuẩn kết nối app AI với công cụ bên ngoài. Xây dựng một lần, dùng mọi nơi.

**MCP Client** - Ứng dụng kết nối tới máy chủ MCP để khám phá và dùng công cụ.

**MCP Server** - Dịch vụ phơi bày công cụ qua MCP với mô tả rõ ràng và sơ đồ tham số.

**McpToolProvider** - Thành phần LangChain4j bao bọc công cụ MCP dùng trong dịch vụ AI và tác nhân.

**McpTransport** - Giao diện giao tiếp MCP. Các bản triển khai gồm Stdio và HTTP.

**Stdio Transport** - Giao tiếp tiến trình cục bộ qua stdin/stdout. Hữu ích cho truy cập hệ thống tập tin hoặc công cụ dòng lệnh.

**StdioMcpTransport** - Triển khai LangChain4j khởi tạo máy chủ MCP như tiến trình con.

**Khám Phá Công Cụ** - Client truy vấn server lấy công cụ có mô tả và sơ đồ.

## Dịch Vụ Azure - [Mô-đun 01](../01-introduction/README.md)

**Azure AI Search** - Dịch vụ tìm kiếm đám mây với khả năng vector. [Mô-đun 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Triển khai tài nguyên Azure.

**Azure OpenAI** - Dịch vụ AI doanh nghiệp của Microsoft.

**Bicep** - Ngôn ngữ hạ tầng dưới dạng mã của Azure. [Hướng dẫn Hạ tầng](../01-introduction/infra/README.md)

**Tên Triển Khai** - Tên cho việc triển khai mô hình trên Azure.

**GPT-5.2** - Mô hình OpenAI mới nhất với điều khiển suy luận. [Mô-đun 02](../02-prompt-engineering/README.md)

## Kiểm Thử và Phát Triển - [Hướng Dẫn Kiểm Thử](TESTING.md)

**Dev Container** - Môi trường phát triển đóng gói trong container. [Cấu hình](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Sân chơi mô hình AI miễn phí. [Mô-đun 00](../00-quick-start/README.md)

**Kiểm Thử Bộ Nhớ** - Kiểm thử với kho lưu trữ trong bộ nhớ.

**Kiểm Thử Tích Hợp** - Kiểm thử với hạ tầng thực tế.

**Maven** - Công cụ tự động xây dựng Java.

**Mockito** - Framework giả lập Java.

**Spring Boot** - Framework ứng dụng Java. [Mô-đun 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố từ chối trách nhiệm**:
Tài liệu này đã được dịch sử dụng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng bản dịch tự động có thể chứa lỗi hoặc sai sót. Tài liệu gốc bằng ngôn ngữ bản địa nên được xem là nguồn chính xác và đáng tin cậy. Đối với thông tin quan trọng, nên sử dụng dịch vụ dịch thuật chuyên nghiệp do con người thực hiện. Chúng tôi không chịu trách nhiệm đối với bất kỳ sự hiểu lầm hoặc giải thích sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->