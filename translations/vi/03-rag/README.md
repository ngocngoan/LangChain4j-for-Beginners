# Module 03: RAG (Tạo Văn Bản Tăng Cường Tìm Kiếm)

## Mục Lục

- [Bạn Sẽ Học Gì](../../../03-rag)
- [Hiểu về RAG](../../../03-rag)
- [Yêu Cầu Tiền Đề](../../../03-rag)
- [Cách Hoạt Động](../../../03-rag)
  - [Xử Lý Tài Liệu](../../../03-rag)
  - [Tạo Embeddings](../../../03-rag)
  - [Tìm Kiếm Ngữ Nghĩa](../../../03-rag)
  - [Tạo Câu Trả Lời](../../../03-rag)
- [Chạy Ứng Dụng](../../../03-rag)
- [Sử Dụng Ứng Dụng](../../../03-rag)
  - [Tải Lên Tài Liệu](../../../03-rag)
  - [Đặt Câu Hỏi](../../../03-rag)
  - [Kiểm Tra Tham Khảo Nguồn](../../../03-rag)
  - [Thử Nghiệm với Câu Hỏi](../../../03-rag)
- [Khái Niệm Chính](../../../03-rag)
  - [Chiến Lược Chia Khúc](../../../03-rag)
  - [Điểm Tương Đồng](../../../03-rag)
  - [Lưu Trữ Trong Bộ Nhớ](../../../03-rag)
  - [Quản Lý Cửa Sổ Ngữ Cảnh](../../../03-rag)
- [Khi Nào RAG Quan Trọng](../../../03-rag)
- [Bước Tiếp Theo](../../../03-rag)

## Bạn Sẽ Học Gì

Trong các module trước, bạn đã học cách trò chuyện với AI và cấu trúc prompt hiệu quả. Nhưng có một giới hạn cơ bản: các mô hình ngôn ngữ chỉ biết những gì chúng được huấn luyện. Chúng không thể trả lời các câu hỏi về chính sách công ty bạn, tài liệu dự án của bạn, hoặc bất kỳ thông tin nào mà chúng không được huấn luyện.

RAG (Tạo Văn Bản Tăng Cường Tìm Kiếm) giải quyết vấn đề này. Thay vì cố gắng dạy mô hình thông tin của bạn (điều này tốn kém và không thực tế), bạn cho nó khả năng tìm kiếm qua tài liệu của bạn. Khi ai đó đặt câu hỏi, hệ thống tìm thông tin liên quan và đưa vào prompt. Mô hình sau đó trả lời dựa trên ngữ cảnh lấy được.

Hãy tưởng tượng RAG như việc cung cấp cho mô hình một thư viện tham khảo. Khi bạn đặt câu hỏi, hệ thống:

1. **Câu Hỏi Người Dùng** - Bạn đặt câu hỏi
2. **Embedding** - Chuyển câu hỏi thành vector
3. **Tìm Kiếm Vector** - Tìm các khúc tài liệu tương tự
4. **Tập Hợp Ngữ Cảnh** - Thêm các khúc liên quan vào prompt
5. **Phản Hồi** - LLM tạo câu trả lời dựa trên ngữ cảnh

Điều này làm cho câu trả lời của mô hình dựa trên dữ liệu thực tế của bạn thay vì dựa vào kiến thức huấn luyện hoặc tự suy diễn.

## Hiểu về RAG

Sơ đồ dưới đây minh họa khái niệm cốt lõi: thay vì chỉ dựa vào dữ liệu huấn luyện của mô hình, RAG cung cấp cho nó một thư viện tham khảo các tài liệu của bạn để tham khảo trước khi tạo mỗi câu trả lời.

<img src="../../../translated_images/vi/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

Dưới đây là cách các phần kết nối từ đầu đến cuối. Câu hỏi của người dùng đi qua bốn giai đoạn — embedding, tìm kiếm vector, tập hợp ngữ cảnh và tạo câu trả lời — mỗi giai đoạn xây dựng dựa trên giai đoạn trước:

<img src="../../../translated_images/vi/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

Phần còn lại của module này sẽ đi qua từng giai đoạn chi tiết, với mã bạn có thể chạy và chỉnh sửa.

## Yêu Cầu Tiền Đề

- Hoàn thành Module 01 (đã triển khai tài nguyên Azure OpenAI)
- File `.env` trong thư mục gốc chứa thông tin đăng nhập Azure (được tạo bởi `azd up` trong Module 01)

> **Lưu ý:** Nếu bạn chưa hoàn thành Module 01, hãy làm theo hướng dẫn triển khai ở đó trước.

## Cách Hoạt Động

### Xử Lý Tài Liệu

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Khi bạn tải lên một tài liệu, hệ thống sẽ phân tích nó (PDF hoặc văn bản thuần túy), gắn metadata như tên file, rồi chia nhỏ thành các khúc — các phần nhỏ hơn phù hợp với cửa sổ ngữ cảnh của mô hình. Những khúc này chồng lấn nhẹ nhau để bạn không mất ngữ cảnh ở ranh giới.

```java
// Phân tích tệp đã tải lên và bao bọc nó trong một Tài liệu LangChain4j
Document document = Document.from(content, metadata);

// Chia thành các đoạn 300 token với chồng lên 30 token
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Sơ đồ bên dưới minh họa cách hoạt động này bằng hình ảnh. Lưu ý mỗi khúc chia sẻ một số token với khúc liền kề — sự chồng lấn 30 token đảm bảo không có ngữ cảnh quan trọng nào bị mất ở giữa:

<img src="../../../translated_images/vi/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) và hỏi:
> - "LangChain4j chia nhỏ tài liệu thành các khúc như thế nào và tại sao việc chồng lấn quan trọng?"
> - "Kích thước khúc tối ưu cho các loại tài liệu khác nhau là bao nhiêu và tại sao?"
> - "Làm thế nào tôi xử lý tài liệu đa ngôn ngữ hoặc định dạng đặc biệt?"

### Tạo Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Mỗi khúc được chuyển đổi thành một dạng số gọi là embedding - về cơ bản là một dấu vân tay toán học thể hiện ý nghĩa của văn bản. Văn bản tương tự sẽ tạo embedding tương tự.

```java
@Bean
public EmbeddingModel embeddingModel() {
    return OpenAiOfficialEmbeddingModel.builder()
        .baseUrl(azureOpenAiEndpoint)
        .apiKey(azureOpenAiKey)
        .modelName(azureEmbeddingDeploymentName)
        .build();
}

EmbeddingStore<TextSegment> embeddingStore = 
    new InMemoryEmbeddingStore<>();
```

Sơ đồ lớp dưới đây cho thấy cách các thành phần LangChain4j liên kết. `OpenAiOfficialEmbeddingModel` chuyển văn bản thành vector, `InMemoryEmbeddingStore` giữ vector cùng với dữ liệu `TextSegment` gốc, và `EmbeddingSearchRequest` kiểm soát các tham số truy xuất như `maxResults` và `minScore`:

<img src="../../../translated_images/vi/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

Sau khi embeddings được lưu trữ, nội dung tương tự tự nhiên sẽ nhóm lại trong không gian vector. Hình ảnh trực quan dưới đây cho thấy các tài liệu về chủ đề liên quan kết thúc thành các điểm gần nhau, điều này làm cho tìm kiếm ngữ nghĩa trở nên khả thi:

<img src="../../../translated_images/vi/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

### Tìm Kiếm Ngữ Nghĩa

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Khi bạn đặt câu hỏi, câu hỏi của bạn cũng trở thành một embedding. Hệ thống sẽ so sánh embedding câu hỏi của bạn với embeddings của tất cả các khúc tài liệu. Nó tìm các khúc có ý nghĩa tương tự nhất - không chỉ khớp từ khóa mà là sự tương đồng ngữ nghĩa thực sự.

```java
Embedding queryEmbedding = embeddingModel.embed(question).content();

EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
    .queryEmbedding(queryEmbedding)
    .maxResults(5)
    .minScore(0.5)
    .build();

EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);
List<EmbeddingMatch<TextSegment>> matches = searchResult.matches();

for (EmbeddingMatch<TextSegment> match : matches) {
    String relevantText = match.embedded().text();
    double score = match.score();
}
```

Sơ đồ dưới đây đối chiếu tìm kiếm ngữ nghĩa với tìm kiếm từ khóa truyền thống. Tìm kiếm từ khóa với "vehicle" không tìm được khúc về "cars and trucks," nhưng tìm kiếm ngữ nghĩa hiểu rằng chúng có cùng ý nghĩa và trả về như một kết quả điểm cao:

<img src="../../../translated_images/vi/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) và hỏi:
> - "Tìm kiếm tương đồng hoạt động với embeddings như thế nào và điểm số được xác định ra sao?"
> - "Ngưỡng tương đồng nên dùng là bao nhiêu và nó ảnh hưởng thế nào đến kết quả?"
> - "Làm thế nào xử lý trường hợp không tìm thấy tài liệu liên quan?"

### Tạo Câu Trả Lời

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Các khúc có liên quan nhất được tập hợp vào một prompt có cấu trúc gồm các hướng dẫn rõ ràng, ngữ cảnh lấy được và câu hỏi của người dùng. Mô hình đọc các khúc đó và trả lời dựa trên thông tin đó — nó chỉ sử dụng những gì được cung cấp, ngăn chặn việc tạo thông tin sai.

```java
String context = matches.stream()
    .map(match -> match.embedded().text())
    .collect(Collectors.joining("\n\n"));

String prompt = String.format("""
    Answer the question based on the following context.
    If the answer cannot be found in the context, say so.

    Context:
    %s

    Question: %s

    Answer:""", context, request.question());

String answer = chatModel.chat(prompt);
```

Sơ đồ dưới đây cho thấy quá trình tập hợp này — các khúc điểm cao từ bước tìm kiếm được đưa vào template prompt, và `OpenAiOfficialChatModel` tạo câu trả lời có cơ sở:

<img src="../../../translated_images/vi/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

## Chạy Ứng Dụng

**Xác minh triển khai:**

Đảm bảo file `.env` tồn tại trong thư mục gốc với thông tin xác thực Azure (được tạo trong Module 01):
```bash
cat ../.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Khởi động ứng dụng:**

> **Lưu ý:** Nếu bạn đã khởi động tất cả ứng dụng bằng `./start-all.sh` từ Module 01, module này đã chạy trên cổng 8081. Bạn có thể bỏ qua lệnh khởi động dưới đây và truy cập trực tiếp http://localhost:8081.

**Lựa chọn 1: Sử dụng Spring Boot Dashboard (Khuyên dùng cho người dùng VS Code)**

Dev container bao gồm extension Spring Boot Dashboard, cung cấp giao diện quản lý trực quan cho tất cả ứng dụng Spring Boot. Bạn có thể tìm thấy nó trong Activity Bar bên trái VS Code (biểu tượng Spring Boot).

Từ Spring Boot Dashboard, bạn có thể:
- Xem tất cả ứng dụng Spring Boot có trong workspace
- Khởi động/dừng ứng dụng chỉ với một cú nhấp
- Xem nhật ký ứng dụng theo thời gian thực
- Giám sát trạng thái ứng dụng

Chỉ cần nhấp nút play cạnh "rag" để chạy module này hoặc chạy tất cả các module cùng một lúc.

<img src="../../../translated_images/vi/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

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
cd 03-rag
./start.sh
```

**PowerShell:**
```powershell
cd 03-rag
.\start.ps1
```

Cả hai script tự động tải biến môi trường từ file `.env` gốc và sẽ build JAR nếu chưa tồn tại.

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

Mở http://localhost:8081 trên trình duyệt của bạn.

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

Ứng dụng cung cấp giao diện web để tải tài liệu lên và đặt câu hỏi.

<a href="images/rag-homepage.png"><img src="../../../translated_images/vi/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Giao diện ứng dụng RAG - tải lên tài liệu và đặt câu hỏi*

### Tải Lên Tài Liệu

Bắt đầu bằng cách tải lên một tài liệu — file TXT là phù hợp nhất để thử nghiệm. Có một file `sample-document.txt` trong thư mục này chứa thông tin về các tính năng LangChain4j, triển khai RAG và các thực hành tốt nhất - rất thích hợp để thử nghiệm hệ thống.

Hệ thống xử lý tài liệu của bạn, chia nhỏ thành các khúc, và tạo embeddings cho mỗi khúc. Điều này diễn ra tự động khi bạn tải lên.

### Đặt Câu Hỏi

Bây giờ hãy đặt các câu hỏi cụ thể về nội dung tài liệu. Thử các câu hỏi có tính thực tế và được nêu rõ trong tài liệu. Hệ thống sẽ tìm các khúc liên quan, đưa chúng vào prompt và tạo câu trả lời.

### Kiểm Tra Tham Khảo Nguồn

Chú ý mỗi câu trả lời bao gồm tham khảo nguồn với điểm tương đồng. Các điểm số này (từ 0 đến 1) cho thấy mức độ liên quan của mỗi khúc với câu hỏi của bạn. Điểm cao hơn nghĩa là khớp tốt hơn. Điều này giúp bạn xác minh câu trả lời với tài liệu gốc.

<a href="images/rag-query-results.png"><img src="../../../translated_images/vi/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Kết quả truy vấn hiển thị câu trả lời kèm tham khảo nguồn và điểm sự liên quan*

### Thử Nghiệm với Câu Hỏi

Hãy thử các loại câu hỏi khác nhau:
- Thông tin cụ thể: "Chủ đề chính là gì?"
- So sánh: "Điểm khác biệt giữa X và Y là gì?"
- Tóm tắt: "Tóm tắt các điểm chính về Z"

Quan sát cách điểm số liên quan thay đổi dựa trên mức độ phù hợp của câu hỏi với nội dung tài liệu.

## Khái Niệm Chính

### Chiến Lược Chia Khúc

Tài liệu được chia thành các khúc 300 token với 30 token chồng lấn. Sự cân bằng này đảm bảo mỗi khúc đủ ngữ cảnh để có ý nghĩa trong khi vẫn đủ nhỏ để có thể bao gồm nhiều khúc trong một prompt.

### Điểm Tương Đồng

Mỗi khúc được truy xuất kèm theo một điểm tương đồng từ 0 đến 1 cho biết mức độ khớp với câu hỏi người dùng. Sơ đồ bên dưới minh họa các khoảng điểm và cách hệ thống sử dụng chúng để lọc kết quả:

<img src="../../../translated_images/vi/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

Điểm dao động từ 0 đến 1:
- 0.7-1.0: Rất liên quan, khớp chính xác
- 0.5-0.7: Có liên quan, ngữ cảnh tốt
- Dưới 0.5: Bị lọc loại, quá khác biệt

Hệ thống chỉ truy xuất các khúc trên ngưỡng tối thiểu để đảm bảo chất lượng.

### Lưu Trữ Trong Bộ Nhớ

Module này sử dụng lưu trữ trong bộ nhớ để đơn giản. Khi bạn khởi động lại ứng dụng, các tài liệu tải lên bị mất. Hệ thống sản xuất thường dùng cơ sở dữ liệu vector bền vững như Qdrant hoặc Azure AI Search.

### Quản Lý Cửa Sổ Ngữ Cảnh

Mỗi mô hình có giới hạn cửa sổ ngữ cảnh tối đa. Bạn không thể đưa tất cả các khúc của một tài liệu lớn vào. Hệ thống truy xuất N khúc liên quan nhất (mặc định 5) để nằm trong giới hạn đồng thời cung cấp đủ ngữ cảnh cho câu trả lời chính xác.

## Khi Nào RAG Quan Trọng

RAG không phải lúc nào cũng là giải pháp phù hợp. Hướng dẫn quyết định dưới đây giúp bạn xác định khi nào RAG mang lại giá trị so với các phương pháp đơn giản hơn — như đưa trực tiếp nội dung vào prompt hoặc dựa vào kiến thức có sẵn của mô hình:

<img src="../../../translated_images/vi/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

**Dùng RAG khi:**
- Trả lời các câu hỏi về tài liệu có bản quyền
- Thông tin thay đổi thường xuyên (chính sách, giá cả, thông số kỹ thuật)
- Độ chính xác đòi hỏi phải trích dẫn nguồn
- Nội dung quá lớn để vừa trong một lời nhắc duy nhất
- Bạn cần câu trả lời có thể kiểm chứng và có cơ sở

**Không sử dụng RAG khi:**
- Câu hỏi đòi hỏi kiến thức chung mà mô hình đã có sẵn
- Cần dữ liệu thời gian thực (RAG hoạt động trên các tài liệu đã tải lên)
- Nội dung đủ nhỏ để đưa trực tiếp vào lời nhắc

## Bước tiếp theo

**Mô-đun tiếp theo:** [04-tools - Đại lý AI với Công cụ](../04-tools/README.md)

---

**Điều hướng:** [← Trước: Mô-đun 02 - Kỹ thuật Gợi ý](../02-prompt-engineering/README.md) | [Quay lại Chính](../README.md) | [Tiếp theo: Mô-đun 04 - Công cụ →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố từ chối trách nhiệm**:  
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng các bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ bản địa nên được coi là nguồn chính thức và có thẩm quyền. Đối với thông tin quan trọng, khuyến nghị sử dụng dịch vụ dịch thuật chuyên nghiệp của con người. Chúng tôi không chịu trách nhiệm về bất kỳ sự hiểu lầm hoặc diễn giải sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->