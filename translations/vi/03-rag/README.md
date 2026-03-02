# Module 03: RAG (Tạo Nội Dung Tăng Cường Tìm Kiếm)

## Mục Lục

- [Video Hướng Dẫn](../../../03-rag)
- [Bạn Sẽ Học được Gì](../../../03-rag)
- [Yêu Cầu Tiên Quyết](../../../03-rag)
- [Hiểu về RAG](../../../03-rag)
  - [Phương pháp RAG nào mà Hướng dẫn này sử dụng?](../../../03-rag)
- [Cách Thức Hoạt Động](../../../03-rag)
  - [Xử Lý Tài Liệu](../../../03-rag)
  - [Tạo Embeddings](../../../03-rag)
  - [Tìm Kiếm Ngữ Nghĩa](../../../03-rag)
  - [Tạo Câu Trả Lời](../../../03-rag)
- [Chạy Ứng Dụng](../../../03-rag)
- [Sử Dụng Ứng Dụng](../../../03-rag)
  - [Tải Tài Liệu Lên](../../../03-rag)
  - [Đặt Câu Hỏi](../../../03-rag)
  - [Kiểm Tra Tham Chiếu Nguồn](../../../03-rag)
  - [Thử Nghiệm Với Các Câu Hỏi](../../../03-rag)
- [Khái Niệm Chính](../../../03-rag)
  - [Chiến Lược Chia Khúc](../../../03-rag)
  - [Điểm Tương Đồng](../../../03-rag)
  - [Lưu Trữ Trong Bộ Nhớ](../../../03-rag)
  - [Quản Lý Cửa Sổ Ngữ Cảnh](../../../03-rag)
- [Khi Nào RAG Quan Trọng](../../../03-rag)
- [Bước Tiếp Theo](../../../03-rag)

## Video Hướng Dẫn

Xem buổi phát trực tiếp này để hiểu cách bắt đầu với module này:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## Bạn Sẽ Học được Gì

Trong các module trước, bạn đã học cách trò chuyện với AI và cấu trúc lời nhắc hiệu quả. Nhưng có một giới hạn cơ bản: các mô hình ngôn ngữ chỉ biết những gì chúng học được trong quá trình đào tạo. Chúng không thể trả lời các câu hỏi về chính sách công ty bạn, tài liệu dự án của bạn, hay bất kỳ thông tin nào mà chúng không được đào tạo.

RAG (Tạo Nội Dung Tăng Cường Tìm Kiếm) giải quyết vấn đề này. Thay vì cố gắng dạy cho mô hình thông tin của bạn (điều này tốn kém và không thực tế), bạn cho nó khả năng tìm kiếm trong các tài liệu của bạn. Khi ai đó đặt câu hỏi, hệ thống tìm thông tin liên quan và thêm nó vào lời nhắc. Mô hình sau đó trả lời dựa trên ngữ cảnh được truy xuất đó.

Hãy nghĩ về RAG như việc cung cấp cho mô hình một thư viện tham khảo. Khi bạn hỏi một câu, hệ thống:

1. **Truy vấn của Người dùng** - Bạn đặt câu hỏi
2. **Embedding** - Chuyển câu hỏi thành vector
3. **Tìm kiếm Vector** - Tìm các khúc tài liệu tương tự
4. **Lắp ráp Ngữ cảnh** - Thêm các khúc liên quan vào lời nhắc
5. **Phản hồi** - LLM tạo câu trả lời dựa trên ngữ cảnh

Điều này giúp các phản hồi của mô hình dựa trên dữ liệu thực của bạn thay vì chỉ dựa vào kiến thức đào tạo hoặc bịa ra câu trả lời.

## Yêu Cầu Tiên Quyết

- Hoàn thành [Module 00 - Khởi Đầu Nhanh](../00-quick-start/README.md) (cho ví dụ Easy RAG được nhắc đến phía trên)
- Hoàn thành [Module 01 - Giới Thiệu](../01-introduction/README.md) (các tài nguyên Azure OpenAI đã được triển khai, bao gồm mô hình embedding `text-embedding-3-small`)
- Tập tin `.env` ở thư mục gốc với thông tin đăng nhập Azure (được tạo bằng lệnh `azd up` trong Module 01)

> **Lưu ý:** Nếu bạn chưa hoàn thành Module 01, hãy làm theo hướng dẫn triển khai ở đó trước. Lệnh `azd up` triển khai cả mô hình chat GPT và mô hình embedding được module này dùng.

## Hiểu về RAG

Hình dưới đây minh họa khái niệm cốt lõi: thay vì chỉ dựa trên dữ liệu đào tạo của mô hình, RAG cung cấp cho nó một thư viện tham khảo các tài liệu của bạn để sử dụng trước khi tạo câu trả lời.

<img src="../../../translated_images/vi/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*Hình này cho thấy sự khác biệt giữa LLM thông thường (đoán dựa trên dữ liệu đào tạo) và LLM tăng cường RAG (tham khảo tài liệu của bạn trước).*

Dưới đây là cách các thành phần kết nối từ đầu đến cuối. Câu hỏi của người dùng đi qua bốn giai đoạn — embedding, tìm kiếm vector, lắp ráp ngữ cảnh và tạo câu trả lời — mỗi giai đoạn xây dựng trên giai đoạn trước:

<img src="../../../translated_images/vi/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*Hình minh họa quy trình RAG từ đầu đến cuối — truy vấn của người dùng đi qua các bước embedding, tìm kiếm vector, lắp ráp ngữ cảnh, tạo câu trả lời.*

Phần còn lại của module này sẽ đi qua từng giai đoạn chi tiết, với mã bạn có thể chạy và chỉnh sửa.

### Phương pháp RAG nào mà Hướng dẫn này sử dụng?

LangChain4j cung cấp ba cách triển khai RAG, mỗi cách có mức độ trừu tượng khác nhau. Hình dưới so sánh các cách đó:

<img src="../../../translated_images/vi/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*Hình này so sánh ba phương pháp RAG của LangChain4j — Easy, Native và Advanced — cho thấy các thành phần chính và khi sử dụng từng cách.*

| Phương pháp | Công việc thực hiện | Đổi lấy |
|---|---|---|
| **Easy RAG** | Kết nối tự động mọi thứ qua `AiServices` và `ContentRetriever`. Bạn chú thích một giao diện, đính kèm retriever, và LangChain4j xử lý embedding, tìm kiếm, và lắp ráp lời nhắc phía sau. | Ít mã nhất, nhưng bạn không thấy chi tiết từng bước. |
| **Native RAG** | Bạn gọi trực tiếp mô hình embedding, tìm kiếm trong kho, xây dựng lời nhắc và tạo câu trả lời — từng bước rõ ràng. | Mã nhiều hơn, nhưng mỗi giai đoạn hiển thị rõ và có thể chỉnh sửa. |
| **Advanced RAG** | Dùng framework `RetrievalAugmentor` với các bộ chuyển đổi truy vấn, bộ phân phối, bộ xếp hạng lại và bộ chèn nội dung có thể cắm thêm — phù hợp cho pipeline sản xuất. | Linh hoạt tối đa, nhưng phức tạp hơn rất nhiều. |

**Hướng dẫn này dùng phương pháp Native.** Mỗi bước của pipeline RAG — embedding truy vấn, tìm trong kho vector, lắp ráp ngữ cảnh và tạo câu trả lời — đều được viết chi tiết trong [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Điều này là có chủ đích: như một nguồn học tập, quan trọng hơn là bạn thấy và hiểu từng giai đoạn thay vì tối giản mã. Khi bạn nắm rõ các phần, có thể chuyển sang Easy RAG để thử nghiệm nhanh hoặc Advanced RAG cho hệ thống sản xuất.

> **💡 Đã từng thấy Easy RAG hoạt động?** Module [Khởi Đầu Nhanh](../00-quick-start/README.md) có ví dụ Hỏi & Đáp Tài liệu ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) dùng Easy RAG — LangChain4j tự động xử lý embedding, tìm kiếm và lắp ráp lời nhắc. Module này mở rộng pipeline đó để bạn có thể thấy và kiểm soát từng bước riêng biệt.

<img src="../../../translated_images/vi/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Hình minh họa pipeline Easy RAG trong `SimpleReaderDemo.java`. So sánh với Native được dùng trong module này: Easy RAG ẩn embedding, tìm kiếm và lắp ráp lời nhắc sau `AiServices` và `ContentRetriever` — bạn tải tài liệu, đính kèm retriever và nhận câu trả lời. Native chia nhỏ pipeline để bạn gọi từng bước (embedding, tìm kiếm, lắp ráp ngữ cảnh, tạo câu trả lời) với toàn quyền kiểm soát.*

## Cách Thức Hoạt Động

Pipeline RAG trong module này chia thành bốn giai đoạn chạy tuần tự mỗi khi người dùng đặt câu hỏi. Đầu tiên, tài liệu tải lên được **phân tích và chia khúc** thành các phần nhỏ dễ quản lý. Các phần này sau đó được chuyển thành **embedding vector** và lưu trữ để so sánh toán học. Khi có truy vấn, hệ thống thực hiện **tìm kiếm ngữ nghĩa** để tìm các phần liên quan nhất, cuối cùng truyền chúng làm ngữ cảnh cho LLM để **tạo câu trả lời**. Các phần dưới đây hướng dẫn từng bước với mã và hình minh họa. Bắt đầu với bước đầu tiên.

### Xử Lý Tài Liệu

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Khi bạn tải tài liệu lên, hệ thống phân tích nó (PDF hoặc văn bản thuần), thêm siêu dữ liệu như tên file, rồi chia thành các khúc — những phần nhỏ hơn phù hợp với cửa sổ ngữ cảnh của mô hình. Các khúc này chồng lấn nhẹ để không bị mất ngữ cảnh ở vùng nối.

```java
// Phân tích tệp đã tải lên và bao bọc nó trong một Tài liệu LangChain4j
Document document = Document.from(content, metadata);

// Chia thành các phần 300 token với chồng lấn 30 token
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Hình dưới thể hiện cách hoạt động trực quan. Hãy chú ý mỗi khúc chia sẻ một vài token với các khúc kề — 30 token chồng lấn đảm bảo không mất ngữ cảnh quan trọng giữa các phần:

<img src="../../../translated_images/vi/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*Hình minh họa một tài liệu được chia thành các khúc 300 token với chồng lấn 30 token, giữ nguyên ngữ cảnh ở ranh giới các khúc.*

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) và hỏi:
> - "LangChain4j chia tài liệu thành các khúc như thế nào và tại sao chồng lấn lại quan trọng?"
> - "Kích thước khúc tối ưu cho các loại tài liệu khác nhau là bao nhiêu và vì sao?"
> - "Làm thế nào để xử lý tài liệu đa ngôn ngữ hoặc có định dạng đặc biệt?"

### Tạo Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Mỗi khúc được chuyển thành một biểu diễn số gọi là embedding — về cơ bản là bộ chuyển đổi ý nghĩa thành con số. Mô hình embedding không "thông minh" như mô hình chat; nó không theo dõi chỉ dẫn, không suy luận hay trả lời câu hỏi. Nó chỉ có thể ánh xạ văn bản vào không gian toán học sao cho các ý nghĩa tương tự sẽ nằm gần nhau — "car" gần "automobile", "refund policy" gần "return my money." Hãy nghĩ mô hình chat như người bạn nói chuyện, và mô hình embedding như hệ thống lưu trữ cực kỳ tốt.

<img src="../../../translated_images/vi/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*Hình minh họa cách một mô hình embedding chuyển văn bản thành vector số, đặt các ý nghĩa tương tự như "car" và "automobile" gần nhau trong không gian vector.*

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

Sơ đồ lớp dưới đây thể hiện hai luồng riêng biệt trong pipeline RAG và các lớp LangChain4j thực thi chúng. Luồng **nhập dữ liệu** (chạy một lần khi tải lên) chia tài liệu, embed các khúc, rồi lưu trữ qua `.addAll()`. Luồng **truy vấn** (chạy mỗi khi người dùng hỏi) embed câu hỏi, tìm trong kho qua `.search()`, và truyền ngữ cảnh khớp tới mô hình chat. Cả hai luồng hội tụ ở giao diện chung `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/vi/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*Hình minh họa hai luồng trong pipeline RAG — nhập dữ liệu và truy vấn — và cách chúng kết nối qua một EmbeddingStore chung.*

Một khi embedding được lưu, nội dung tương tự tự nhiên tập hợp thành cụm trong không gian vector. Hình dưới đây cho thấy các tài liệu về chủ đề liên quan tập trung gần nhau, điều giúp tìm kiếm ngữ nghĩa thành công:

<img src="../../../translated_images/vi/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*Hình minh họa các tài liệu liên quan nhóm lại trong không gian vector 3D, với các chủ đề như Tài liệu Kỹ Thuật, Quy Tắc Kinh Doanh, và Câu hỏi Thường gặp tạo thành các nhóm riêng biệt.*

Khi người dùng tìm, hệ thống thực hiện bốn bước: embed tài liệu một lần, embed truy vấn mỗi lần tìm, so sánh vector truy vấn với tất cả vector đã lưu bằng độ tương đồng cosine, và trả về top-K khúc điểm cao nhất. Hình dưới mô tả từng bước và các lớp LangChain4j tham gia:

<img src="../../../translated_images/vi/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*Hình minh họa quá trình tìm kiếm embedding 4 bước: embed tài liệu, embed truy vấn, so sánh vector qua cosine similarity, và trả về kết quả top-K.*

### Tìm Kiếm Ngữ Nghĩa

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Khi bạn đặt câu hỏi, câu hỏi của bạn cũng được chuyển thành embedding. Hệ thống so sánh embedding câu hỏi với embedding của tất cả các khúc tài liệu. Nó tìm ra các khúc có ý nghĩa tương đồng nhất — không chỉ khớp từ khóa mà là tương đồng ngữ nghĩa thực sự.

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

Hình dưới đây đối chiếu tìm kiếm ngữ nghĩa với tìm kiếm dựa trên từ khóa truyền thống. Tìm kiếm từ khóa cho "vehicle" bỏ qua khúc nói về "cars and trucks," nhưng tìm kiếm ngữ nghĩa hiểu chúng cùng nghĩa và trả về như kết quả điểm cao:

<img src="../../../translated_images/vi/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*Hình so sánh tìm kiếm từ khóa với tìm kiếm ngữ nghĩa, cho thấy tìm kiếm ngữ nghĩa lấy được nội dung liên quan khái niệm ngay cả khi từ khóa không giống.*

Ở bên trong, độ tương đồng được đo bằng cosine similarity — đơn giản hỏi "hai mũi tên này có cùng hướng không?" Hai khúc có thể dùng từ hoàn toàn khác, nhưng nếu cùng nghĩa vector sẽ cùng hướng và điểm rất gần 1.0:

<img src="../../../translated_images/vi/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>
*Biểu đồ này minh họa độ tương đồng cosine như góc giữa các vector nhúng — các vector càng đồng hướng thì điểm số càng gần 1.0, biểu thị sự tương đồng ngữ nghĩa cao hơn.*

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) và hỏi:
> - "Tìm kiếm tương đồng hoạt động với embeddings như thế nào và điều gì quyết định điểm số?"
> - "Ngưỡng tương đồng nào tôi nên dùng và nó ảnh hưởng thế nào đến kết quả?"
> - "Làm thế nào để xử lý trường hợp không tìm thấy tài liệu liên quan?"

### Tạo Câu Trả Lời

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Các đoạn liên quan nhất được tập hợp thành một lời nhắc cấu trúc bao gồm các hướng dẫn rõ ràng, ngữ cảnh được truy xuất và câu hỏi của người dùng. Mô hình đọc những đoạn cụ thể đó và trả lời dựa trên thông tin đó — nó chỉ có thể dùng những gì có trước mặt, điều này ngăn ngừa lỗi tưởng tượng.

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

Biểu đồ dưới đây cho thấy cách tập hợp này hoạt động — các đoạn có điểm số cao nhất từ bước tìm kiếm được chèn vào mẫu lời nhắc, và `OpenAiOfficialChatModel` tạo câu trả lời có căn cứ:

<img src="../../../translated_images/vi/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Biểu đồ này cho thấy cách các đoạn có điểm cao nhất được tập hợp thành một lời nhắc cấu trúc, cho phép mô hình tạo câu trả lời có căn cứ từ dữ liệu của bạn.*

## Chạy Ứng Dụng

**Xác thực triển khai:**

Đảm bảo file `.env` tồn tại trong thư mục gốc với thông tin đăng nhập Azure (được tạo trong Module 01):

**Bash:**
```bash
cat ../.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Khởi động ứng dụng:**

> **Lưu ý:** Nếu bạn đã khởi động tất cả ứng dụng bằng `./start-all.sh` từ Module 01, module này đã chạy trên cổng 8081. Bạn có thể bỏ qua các lệnh khởi động dưới đây và truy cập trực tiếp http://localhost:8081.

**Lựa chọn 1: Dùng Spring Boot Dashboard (Khuyến nghị cho người dùng VS Code)**

Dev container có kèm extension Spring Boot Dashboard, cung cấp giao diện trực quan để quản lý tất cả ứng dụng Spring Boot. Bạn có thể tìm nó trên thanh Activity bên trái của VS Code (tìm biểu tượng Spring Boot).

Từ Spring Boot Dashboard, bạn có thể:
- Xem tất cả ứng dụng Spring Boot có trong workspace
- Khởi động/dừng ứng dụng chỉ với một cú nhấp
- Xem log ứng dụng theo thời gian thực
- Giám sát trạng thái ứng dụng

Chỉ cần nhấp nút play bên cạnh "rag" để khởi động module này, hoặc khởi động tất cả module cùng lúc.

<img src="../../../translated_images/vi/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Ảnh chụp màn hình này hiển thị Spring Boot Dashboard trong VS Code, nơi bạn có thể khởi động, dừng và giám sát ứng dụng một cách trực quan.*

**Lựa chọn 2: Dùng các script shell**

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

Cả hai script tự động tải biến môi trường từ file `.env` trong thư mục gốc và sẽ build các JAR nếu chúng chưa tồn tại.

> **Lưu ý:** Nếu bạn muốn build thủ công tất cả các module trước khi khởi động:
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

## Sử Dụng Ứng Dụng

Ứng dụng cung cấp giao diện web để tải lên tài liệu và đặt câu hỏi.

<a href="images/rag-homepage.png"><img src="../../../translated_images/vi/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ảnh chụp màn hình giao diện ứng dụng RAG nơi bạn tải lên tài liệu và đặt câu hỏi.*

### Tải Lên Tài Liệu

Bắt đầu bằng cách tải lên một tài liệu - các file TXT hoạt động tốt nhất để thử nghiệm. Một file `sample-document.txt` được cung cấp trong thư mục này chứa thông tin về các tính năng LangChain4j, cách triển khai RAG và các thực hành tốt nhất - rất phù hợp để thử hệ thống.

Hệ thống xử lý tài liệu của bạn, chia nhỏ nó thành các đoạn, và tạo embeddings cho từng đoạn. Việc này diễn ra tự động khi bạn tải lên.

### Đặt Câu Hỏi

Bây giờ hãy hỏi các câu hỏi cụ thể về nội dung tài liệu. Thử những câu hỏi mang tính sự thật rõ ràng được nêu trong tài liệu. Hệ thống tìm kiếm các đoạn liên quan, bao gồm chúng trong lời nhắc, và tạo ra câu trả lời.

### Kiểm Tra Tham Chiếu Nguồn

Lưu ý mỗi câu trả lời bao gồm tham chiếu nguồn kèm điểm số tương đồng. Các điểm số này (từ 0 đến 1) cho thấy mức độ liên quan của mỗi đoạn với câu hỏi của bạn. Điểm càng cao nghĩa là càng phù hợp. Điều này giúp bạn xác thực câu trả lời dựa trên tài liệu gốc.

<a href="images/rag-query-results.png"><img src="../../../translated_images/vi/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ảnh chụp màn hình kết quả truy vấn với câu trả lời được tạo, tham chiếu nguồn, và điểm số liên quan của từng đoạn được truy xuất.*

### Thử Nghiệm Với Các Câu Hỏi

Thử các loại câu hỏi khác nhau:
- Sự thật cụ thể: "Chủ đề chính là gì?"
- So sánh: "Điểm khác biệt giữa X và Y là gì?"
- Tóm tắt: "Tóm tắt các điểm chính về Z"

Theo dõi cách điểm liên quan thay đổi dựa trên việc câu hỏi của bạn phù hợp thế nào với nội dung tài liệu.

## Các Khái Niệm Chính

### Chiến Lược Chia Đoạn

Tài liệu được chia thành các đoạn 300 token với độ chồng chéo 30 token. Sự cân bằng này đảm bảo mỗi đoạn có đủ ngữ cảnh để có ý nghĩa trong khi vẫn đủ nhỏ để có thể chèn nhiều đoạn vào lời nhắc.

### Điểm Số Tương Đồng

Mỗi đoạn được truy xuất kèm theo điểm số tương đồng từ 0 đến 1 cho biết mức độ khớp với câu hỏi của người dùng. Biểu đồ dưới đây trực quan hóa các khoảng điểm và cách hệ thống dùng chúng để lọc kết quả:

<img src="../../../translated_images/vi/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Biểu đồ này hiển thị các khoảng điểm từ 0 đến 1, với ngưỡng tối thiểu 0.5 để lọc bỏ các đoạn không liên quan.*

Điểm số trải dài từ 0 đến 1:
- 0.7-1.0: Rất liên quan, khớp chính xác
- 0.5-0.7: Liên quan, ngữ cảnh tốt
- Dưới 0.5: Bị lọc, quá khác biệt

Hệ thống chỉ truy xuất các đoạn trên ngưỡng tối thiểu để đảm bảo chất lượng.

Embeddings hoạt động tốt khi nghĩa phân cụm rõ ràng, nhưng có một số điểm mù. Biểu đồ dưới đây thể hiện các chế độ thất bại thường gặp — đoạn quá lớn tạo vector mờ nhạt, đoạn quá nhỏ thiếu ngữ cảnh, các thuật ngữ mơ hồ chỉ đến nhiều cụm khác nhau, và việc tra cứu trùng khớp chính xác (ID, số bộ phận) không hoạt động với embeddings:

<img src="../../../translated_images/vi/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Biểu đồ này cho thấy các chế độ thất bại phổ biến của embeddings: đoạn quá lớn, đoạn quá nhỏ, thuật ngữ mơ hồ chỉ đến nhiều cụm, và tra cứu trùng khớp chính xác như ID.*

### Lưu Trữ Trong Bộ Nhớ

Module này sử dụng lưu trữ trong bộ nhớ để đơn giản. Khi bạn khởi động lại ứng dụng, tài liệu tải lên sẽ bị mất. Các hệ thống sản xuất dùng cơ sở dữ liệu vector bền vững như Qdrant hoặc Azure AI Search.

### Quản Lý Cửa Sổ Ngữ Cảnh

Mỗi mô hình có giới hạn cửa sổ ngữ cảnh tối đa. Bạn không thể bao gồm mọi đoạn từ một tài liệu lớn. Hệ thống truy xuất N đoạn có liên quan cao nhất (mặc định 5) để giữ trong giới hạn đồng thời cung cấp đủ ngữ cảnh cho các câu trả lời chính xác.

## Khi Nào Nên Dùng RAG

RAG không phải lúc nào cũng là phương pháp phù hợp. Hướng dẫn quyết định dưới đây giúp bạn xác định khi nào RAG mang lại giá trị so với khi nào các phương pháp đơn giản hơn — như đưa nội dung trực tiếp vào lời nhắc hoặc dựa vào kiến thức sẵn có của mô hình — là đủ:

<img src="../../../translated_images/vi/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Biểu đồ này hiển thị hướng dẫn quyết định khi nào RAG mang lại giá trị so với khi nào các phương pháp đơn giản là đủ.*

**Dùng RAG khi:**
- Trả lời câu hỏi về tài liệu riêng tư
- Thông tin thay đổi thường xuyên (chính sách, giá cả, thông số)
- Yêu cầu độ chính xác có tham chiếu nguồn
- Nội dung quá lớn không thể đưa hết vào một lời nhắc
- Cần câu trả lời có thể kiểm chứng và có căn cứ

**Không dùng RAG khi:**
- Câu hỏi yêu cầu kiến thức chung mà mô hình đã có sẵn
- Cần dữ liệu theo thời gian thực (RAG hoạt động trên tài liệu đã tải lên)
- Nội dung đủ nhỏ để có thể đưa trực tiếp vào lời nhắc

## Bước Tiếp Theo

**Module tiếp theo:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Điều hướng:** [← Trước đó: Module 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Quay lại Trang Chính](../README.md) | [Tiếp: Module 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố từ chối trách nhiệm**:
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng các bản dịch tự động có thể chứa lỗi hoặc sự không chính xác. Tài liệu gốc bằng ngôn ngữ nguyên bản nên được xem là nguồn thông tin chính xác và có thẩm quyền. Đối với các thông tin quan trọng, nên sử dụng dịch vụ dịch thuật chuyên nghiệp do con người thực hiện. Chúng tôi không chịu trách nhiệm về bất kỳ sự hiểu nhầm hay diễn giải sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->