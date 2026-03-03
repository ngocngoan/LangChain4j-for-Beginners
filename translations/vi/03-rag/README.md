# Module 03: RAG (Tạo nội dung tăng cường truy xuất)

## Mục lục

- [Video hướng dẫn](../../../03-rag)
- [Bạn sẽ học được gì](../../../03-rag)
- [Yêu cầu trước](../../../03-rag)
- [Hiểu về RAG](../../../03-rag)
  - [Phương pháp RAG nào được sử dụng trong hướng dẫn này?](../../../03-rag)
- [Cách hoạt động](../../../03-rag)
  - [Xử lý tài liệu](../../../03-rag)
  - [Tạo embeddings](../../../03-rag)
  - [Tìm kiếm ngữ nghĩa](../../../03-rag)
  - [Tạo câu trả lời](../../../03-rag)
- [Chạy ứng dụng](../../../03-rag)
- [Sử dụng ứng dụng](../../../03-rag)
  - [Tải lên tài liệu](../../../03-rag)
  - [Đặt câu hỏi](../../../03-rag)
  - [Kiểm tra nguồn tham khảo](../../../03-rag)
  - [Thử nghiệm với các câu hỏi](../../../03-rag)
- [Khái niệm chính](../../../03-rag)
  - [Chiến lược chia đoạn](../../../03-rag)
  - [Điểm tương đồng](../../../03-rag)
  - [Lưu trữ trong bộ nhớ](../../../03-rag)
  - [Quản lý cửa sổ ngữ cảnh](../../../03-rag)
- [Khi nào RAG quan trọng](../../../03-rag)
- [Bước tiếp theo](../../../03-rag)

## Video hướng dẫn

Xem buổi trực tiếp này giải thích cách bắt đầu với module này:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## Bạn sẽ học được gì

Trong các module trước, bạn đã học cách trò chuyện với AI và cấu trúc các prompt hiệu quả. Nhưng có một giới hạn cơ bản: các mô hình ngôn ngữ chỉ biết những gì chúng học được trong quá trình huấn luyện. Chúng không thể trả lời các câu hỏi về chính sách của công ty bạn, tài liệu dự án của bạn, hoặc bất kỳ thông tin nào mà chúng không được huấn luyện.

RAG (Tạo nội dung tăng cường truy xuất) giải quyết vấn đề này. Thay vì cố gắng dạy mô hình thông tin của bạn (điều này tốn kém và không thực tế), bạn cho nó khả năng tìm kiếm qua các tài liệu của bạn. Khi ai đó hỏi câu hỏi, hệ thống sẽ tìm thông tin liên quan và thêm nó vào prompt. Mô hình sau đó trả lời dựa trên ngữ cảnh được truy xuất đó.

Hãy coi RAG như việc cung cấp cho mô hình một thư viện tham khảo. Khi bạn đặt câu hỏi, hệ thống:

1. **Truy vấn người dùng** - Bạn đặt câu hỏi
2. **Embedding** - Chuyển câu hỏi thành vector
3. **Tìm kiếm vector** - Tìm các đoạn tài liệu tương tự
4. **Tập hợp ngữ cảnh** - Thêm các đoạn liên quan vào prompt
5. **Phản hồi** - LLM tạo câu trả lời dựa trên ngữ cảnh

Điều này giúp câu trả lời của mô hình dựa trên dữ liệu thực tế của bạn thay vì dựa vào kiến thức huấn luyện hoặc bịa câu trả lời.

## Yêu cầu trước

- Đã hoàn thành [Module 00 - Bắt đầu nhanh](../00-quick-start/README.md) (cho ví dụ Easy RAG được tham chiếu sau trong module này)
- Đã hoàn thành [Module 01 - Giới thiệu](../01-introduction/README.md) (đã triển khai tài nguyên Azure OpenAI, bao gồm mô hình embedding `text-embedding-3-small`)
- File `.env` trong thư mục gốc chứa thông tin xác thực Azure (tạo bởi `azd up` trong Module 01)

> **Lưu ý:** Nếu bạn chưa hoàn thành Module 01, hãy làm theo hướng dẫn triển khai ở đó trước. Lệnh `azd up` triển khai cả mô hình chat GPT và mô hình embedding được sử dụng trong module này.

## Hiểu về RAG

Sơ đồ dưới đây minh họa khái niệm cốt lõi: thay vì chỉ dựa trên dữ liệu huấn luyện của mô hình, RAG cung cấp cho nó một thư viện tham khảo gồm các tài liệu của bạn để tham khảo trước khi sinh câu trả lời.

<img src="../../../translated_images/vi/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*Sơ đồ này thể hiện sự khác biệt giữa một LLM chuẩn (dự đoán dựa trên dữ liệu đào tạo) và một LLM được cải tiến bởi RAG (tham khảo tài liệu của bạn trước).*

Dưới đây là cách các bước kết nối từ đầu đến cuối. Câu hỏi của người dùng đi qua bốn giai đoạn — embedding, tìm kiếm vector, tập hợp ngữ cảnh và tạo câu trả lời — mỗi bước xây dựng trên bước trước:

<img src="../../../translated_images/vi/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*Sơ đồ này thể hiện quy trình RAG từ đầu đến cuối — câu hỏi người dùng đi qua embedding, tìm kiếm vector, tập hợp ngữ cảnh và tạo câu trả lời.*

Phần còn lại của module này sẽ hướng dẫn chi tiết từng giai đoạn, với mã bạn có thể chạy và sửa đổi.

### Phương pháp RAG nào được sử dụng trong hướng dẫn này?

LangChain4j cung cấp ba cách để triển khai RAG, mỗi cách có mức độ trừu tượng khác nhau. Sơ đồ dưới đây so sánh chúng cạnh nhau:

<img src="../../../translated_images/vi/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*Sơ đồ này so sánh ba phương pháp RAG của LangChain4j — Easy, Native và Advanced — thể hiện các thành phần chính và khi nào nên dùng từng loại.*

| Phương pháp | Điều nó làm | Đánh đổi |
|---|---|---|
| **Easy RAG** | Kết nối mọi thứ tự động qua `AiServices` và `ContentRetriever`. Bạn chú thích một interface, gắn bộ truy xuất, LangChain4j xử lý embedding, tìm kiếm và ráp prompt phía sau. | Ít mã nhất, nhưng bạn không thấy các bước bên trong. |
| **Native RAG** | Bạn gọi mô hình embedding, tìm kiếm trong kho, xây dựng prompt và tạo câu trả lời theo từng bước rõ ràng. | Mã nhiều hơn, nhưng từng giai đoạn đều rõ ràng và có thể sửa đổi. |
| **Advanced RAG** | Dùng framework `RetrievalAugmentor` với các bộ biến đổi truy vấn, bộ điều hướng, bộ xếp lại, và bộ chèn nội dung cắm thêm cho hệ thống sản xuất. | Linh hoạt tối đa, nhưng phức tạp đáng kể. |

**Hướng dẫn này sử dụng phương pháp Native.** Mỗi bước trong pipeline RAG — embedding truy vấn, tìm kiếm trong kho vector, tập hợp ngữ cảnh, và tạo câu trả lời — được viết rõ ràng trong [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Điều này là có chủ ý: như một tài nguyên học tập, điều quan trọng hơn là bạn thấy và hiểu từng bước hơn là rút gọn mã. Khi bạn đã quen với cách các phần nối với nhau, bạn có thể chuyển sang Easy RAG cho prototype nhanh hoặc Advanced RAG cho hệ thống sản xuất.

> **💡 Đã từng thấy Easy RAG hoạt động?** Module [Bắt đầu nhanh](../00-quick-start/README.md) bao gồm ví dụ Hỏi đáp tài liệu ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) dùng phương pháp Easy RAG — LangChain4j tự động xử lý embedding, tìm kiếm, và ráp prompt. Module này sẽ đi sâu hơn bằng cách mở rộng pipeline đó để bạn có thể thấy và kiểm soát từng bước.

Sơ đồ dưới đây thể hiện pipeline Easy RAG từ ví dụ Bắt đầu nhanh đó. Hãy chú ý cách `AiServices` và `EmbeddingStoreContentRetriever` ẩn đi toàn bộ sự phức tạp — bạn tải tài liệu, gắn bộ truy xuất, và nhận câu trả lời. Phương pháp Native trong module này sẽ mở rộng từng bước ẩn đó:

<img src="../../../translated_images/vi/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Sơ đồ này thể hiện pipeline Easy RAG từ `SimpleReaderDemo.java`. So sánh với phương pháp Native dùng trong module này: Easy RAG ẩn embedding, truy xuất, và ráp prompt phía sau `AiServices` và `ContentRetriever` — bạn tải tài liệu, gắn bộ truy xuất, và nhận câu trả lời. Phương pháp Native trong module này mở pipeline đó ra để bạn gọi từng bước (embed, tìm kiếm, ráp ngữ cảnh, tạo câu trả lời) trực tiếp, cho phép bạn thấy và kiểm soát toàn bộ.*

## Cách hoạt động

Pipeline RAG trong module này chia thành bốn giai đoạn chạy tuần tự mỗi lần người dùng hỏi. Đầu tiên, tài liệu tải lên được **phân tích và chia thành các đoạn nhỏ** vừa phải. Các đoạn này sau đó được chuyển thành **embedding vector** và lưu trữ để có thể so sánh về mặt toán học. Khi có truy vấn, hệ thống thực hiện **tìm kiếm ngữ nghĩa** để tìm các đoạn có liên quan nhất, và cuối cùng chuyển chúng làm ngữ cảnh cho LLM để **tạo câu trả lời**. Các phần dưới đây sẽ hướng dẫn từng bước với mã thực tế và sơ đồ. Bắt đầu với bước đầu tiên.

### Xử lý tài liệu

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Khi bạn tải một tài liệu lên, hệ thống sẽ phân tích nó (dạng PDF hoặc văn bản thuần túy), gắn thêm metadata như tên file, rồi chia nó thành các đoạn nhỏ — các phần nhỏ hơn phù hợp với cửa sổ ngữ cảnh của mô hình. Các đoạn này đan xen một chút để bạn không mất ngữ cảnh ở các điểm giới hạn.

```java
// Phân tích tệp đã tải lên và bao gói nó trong một tài liệu LangChain4j
Document document = Document.from(content, metadata);

// Chia thành các đoạn 300 token với chồng lấn 30 token
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Sơ đồ dưới đây thể hiện cách hoạt động một cách trực quan. Chú ý cách mỗi đoạn chia sẻ một số token với các đoạn xung quanh — sự chồng lấp 30 token đảm bảo không mất ngữ cảnh quan trọng tại ranh giới:

<img src="../../../translated_images/vi/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*Sơ đồ này thể hiện một tài liệu được chia thành các đoạn 300 token với 30 token chồng lấp, giữ nguyên ngữ cảnh ở ranh giới các đoạn.*

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) và hỏi:
> - "LangChain4j chia tài liệu thành các đoạn như thế nào và tại sao sự chồng lấp lại quan trọng?"
> - "Kích thước đoạn tối ưu cho các loại tài liệu khác nhau là bao nhiêu và tại sao?"
> - "Làm sao để xử lý tài liệu bằng nhiều ngôn ngữ hoặc có định dạng đặc biệt?"

### Tạo embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Mỗi đoạn được chuyển thành một biểu diễn số gọi là embedding — cơ bản là bộ chuyển đổi từ ý nghĩa thành số. Mô hình embedding không "thông minh" như mô hình chat; nó không thể theo lệnh, lý luận, hoặc trả lời câu hỏi. Điều nó làm được là ánh xạ văn bản vào một không gian toán học, nơi các ý nghĩa tương tự nằm gần nhau — "car" gần "automobile", "refund policy" gần "return my money." Hãy nghĩ mô hình chat như một người bạn nói chuyện được; còn mô hình embedding là hệ thống lưu trữ siêu tốt.

Sơ đồ dưới đây thể hiện khái niệm này — văn bản vào, vector số ra, và các ý nghĩa tương tự tạo ra các vector gần nhau:

<img src="../../../translated_images/vi/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*Sơ đồ này thể hiện cách mô hình embedding chuyển văn bản thành vector số, đặt các ý nghĩa tương tự — như "car" và "automobile" — nằm sát nhau trong không gian vector.*

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

Sơ đồ lớp dưới đây mô tả hai luồng riêng biệt trong pipeline RAG và các lớp LangChain4j triển khai chúng. Luồng **nhập liệu** (chạy một lần khi tải lên) chia tài liệu, tạo embedding cho các đoạn, và lưu chúng qua `.addAll()`. Luồng **truy vấn** (chạy mỗi khi người dùng hỏi) tạo embedding câu hỏi, tìm kiếm trong kho qua `.search()`, và đưa ngữ cảnh phù hợp cho mô hình chat. Hai luồng này gặp nhau tại interface chung `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/vi/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*Sơ đồ này thể hiện hai luồng trong pipeline RAG — nhập liệu và truy vấn — và cách chúng kết nối qua EmbeddingStore chung.*

Khi embeddings được lưu, nội dung tương tự tự nhiên gom vào gần nhau trong không gian vector. Hình dưới đây thể hiện cách các tài liệu về chủ đề liên quan tụ lại thành các điểm gần nhau, điều này làm cho tìm kiếm ngữ nghĩa khả thi:

<img src="../../../translated_images/vi/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*Hình minh họa cách các tài liệu liên quan được gom nhóm trong không gian vector 3D, với các chủ đề như Tài liệu kỹ thuật, Quy tắc kinh doanh, và Câu hỏi thường gặp tạo thành các nhóm riêng biệt.*

Khi người dùng tìm kiếm, hệ thống thực hiện bốn bước: tạo embedding cho tài liệu một lần, tạo embedding cho truy vấn mỗi lần tìm, so sánh vector truy vấn với tất cả vector đã lưu bằng độ tương đồng cosine, và trả về top-K đoạn có điểm cao nhất. Sơ đồ dưới đây minh họa từng bước và các lớp LangChain4j liên quan:

<img src="../../../translated_images/vi/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*Sơ đồ thể hiện quy trình tìm kiếm embedding bốn bước: tạo embedding tài liệu, tạo embedding truy vấn, so sánh vector bằng độ tương đồng cosine, và trả về kết quả top-K.*

### Tìm kiếm ngữ nghĩa

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Khi bạn hỏi một câu, câu hỏi của bạn cũng được chuyển thành embedding. Hệ thống so sánh embedding của câu hỏi với tất cả embedding của các đoạn tài liệu. Nó tìm các đoạn có ý nghĩa tương tự nhất — không chỉ khớp từ khóa mà còn là tương đồng ngữ nghĩa thực tế.

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

Sơ đồ dưới đây đối chiếu tìm kiếm ngữ nghĩa với tìm kiếm từ khóa truyền thống. Tìm kiếm từ khóa "vehicle" bỏ sót đoạn nói về "cars and trucks," nhưng tìm kiếm ngữ nghĩa hiểu rằng chúng có nghĩa như nhau và trả về kết quả với điểm cao:

<img src="../../../translated_images/vi/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*Sơ đồ so sánh tìm kiếm dựa trên từ khóa với tìm kiếm ngữ nghĩa, thể hiện cách tìm kiếm ngữ nghĩa truy xuất nội dung liên quan về mặt khái niệm ngay cả khi từ khóa chính xác khác nhau.*
Dưới bề mặt, sự tương đồng được đo bằng độ tương đồng cosin — về cơ bản hỏi "liệu hai mũi tên này có đang chỉ cùng một hướng không?" Hai đoạn văn có thể sử dụng từ ngữ hoàn toàn khác nhau, nhưng nếu chúng có cùng ý nghĩa thì các vec-tơ của chúng sẽ chỉ cùng một hướng và điểm số sẽ gần bằng 1.0:

<img src="../../../translated_images/vi/cosine-similarity.9baeaf3fc3336abb.webp" alt="Độ tương đồng Cosin" width="800"/>

*Biểu đồ này minh họa độ tương đồng cosin như góc giữa các vec-tơ embedding — các vec-tơ càng đồng hướng thì điểm số càng gần 1.0, cho thấy sự tương đồng ngữ nghĩa cao hơn.*

> **🤖 Thử với [GitHub Copilot](https://github.com/features/copilot) Chat:** Mở [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) và hỏi:
> - "Cách tìm kiếm tương đồng hoạt động với embeddings như thế nào và điều gì quyết định điểm số?"
> - "Ngưỡng tương đồng nào tôi nên dùng và nó ảnh hưởng thế nào đến kết quả?"
> - "Tôi xử lý trường hợp không tìm thấy tài liệu liên quan ra sao?"

### Tạo Câu Trả Lời

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Các đoạn văn liên quan nhất được tập hợp thành một lời nhắc có cấu trúc bao gồm hướng dẫn rõ ràng, ngữ cảnh được lấy ra, và câu hỏi của người dùng. Mô hình đọc các đoạn văn cụ thể đó và trả lời dựa trên thông tin đó — nó chỉ có thể sử dụng những gì đang có trước mặt, điều này ngăn chặn việc tạo ra thông tin không chính xác.

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

Biểu đồ dưới đây cho thấy quá trình tập hợp này — các đoạn văn có điểm cao nhất từ bước tìm kiếm được chèn vào mẫu lời nhắc, và `OpenAiOfficialChatModel` tạo ra một câu trả lời có căn cứ:

<img src="../../../translated_images/vi/context-assembly.7e6dd60c31f95978.webp" alt="Tập hợp Ngữ cảnh" width="800"/>

*Biểu đồ này cho thấy cách các đoạn văn điểm cao nhất được tập hợp thành lời nhắc có cấu trúc, cho phép mô hình tạo ra câu trả lời dựa trên dữ liệu của bạn.*

## Chạy Ứng Dụng

**Xác minh triển khai:**

Đảm bảo file `.env` tồn tại trong thư mục gốc với thông tin xác thực Azure (đã tạo trong Module 01). Chạy lệnh này từ thư mục module (`03-rag/`):

**Bash:**
```bash
cat ../.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Nên hiển thị AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Khởi động ứng dụng:**

> **Lưu ý:** Nếu bạn đã khởi chạy tất cả ứng dụng bằng `./start-all.sh` từ thư mục gốc (theo mô tả trong Module 01), module này đã chạy trên cổng 8081. Bạn có thể bỏ qua các lệnh khởi động dưới đây và truy cập trực tiếp http://localhost:8081.

**Lựa chọn 1: Dùng Spring Boot Dashboard (Khuyến nghị cho người dùng VS Code)**

Dev container bao gồm extension Spring Boot Dashboard, cung cấp giao diện trực quan để quản lý tất cả ứng dụng Spring Boot. Bạn có thể tìm thấy ở thanh hoạt động bên trái VS Code (tìm biểu tượng Spring Boot).

Từ Spring Boot Dashboard, bạn có thể:
- Xem tất cả các ứng dụng Spring Boot có trong workspace
- Khởi động/dừng ứng dụng chỉ với một cú click
- Xem nhật ký ứng dụng theo thời gian thực
- Giám sát trạng thái ứng dụng

Chỉ cần nhấn nút play cạnh "rag" để khởi động module này, hoặc khởi động tất cả các module cùng lúc.

<img src="../../../translated_images/vi/dashboard.fbe6e28bf4267ffe.webp" alt="Bảng điều khiển Spring Boot" width="400"/>

*Ảnh chụp màn hình này hiển thị Spring Boot Dashboard trong VS Code, nơi bạn có thể khởi động, dừng và giám sát các ứng dụng một cách trực quan.*

**Lựa chọn 2: Dùng các script shell**

Khởi động tất cả các ứng dụng web (modules 01-04):

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

Cả hai script tự động tải biến môi trường từ file `.env` ở thư mục gốc và sẽ build JAR nếu chưa tồn tại.

> **Lưu ý:** Nếu bạn muốn build tất cả các module thủ công trước khi khởi động:
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

Mở http://localhost:8081 trong trình duyệt của bạn.

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

## Sử dụng Ứng Dụng

Ứng dụng cung cấp giao diện web để tải tài liệu lên và đặt câu hỏi.

<a href="images/rag-homepage.png"><img src="../../../translated_images/vi/rag-homepage.d90eb5ce1b3caa94.webp" alt="Giao diện Ứng dụng RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ảnh chụp màn hình này hiển thị giao diện ứng dụng RAG, nơi bạn có thể tải tài liệu lên và đặt câu hỏi.*

### Tải Lên Một Tài Liệu

Bắt đầu bằng cách tải lên một tài liệu - các file TXT là lựa chọn tốt nhất để thử nghiệm. Một file `sample-document.txt` được cung cấp trong thư mục này với thông tin về các tính năng của LangChain4j, cách triển khai RAG, và các thực hành tốt nhất - rất phù hợp để thử hệ thống.

Hệ thống xử lý tài liệu của bạn, chia nhỏ thành các đoạn, và tạo embeddings cho mỗi đoạn. Quá trình này diễn ra tự động khi bạn tải lên.

### Đặt Câu Hỏi

Bây giờ hãy hỏi những câu hỏi cụ thể về nội dung tài liệu. Thử một điều gì đó có tính thực tế được trình bày rõ ràng trong tài liệu. Hệ thống sẽ tìm kiếm các đoạn liên quan, đưa vào lời nhắc, và tạo ra câu trả lời.

### Kiểm Tra Tham Chiếu Nguồn

Lưu ý mỗi câu trả lời đều có tham chiếu nguồn kèm theo điểm số tương đồng. Những điểm số này (từ 0 đến 1) cho thấy mức độ liên quan của từng đoạn với câu hỏi của bạn. Điểm càng cao đồng nghĩa với sự phù hợp càng tốt. Điều này cho phép bạn kiểm chứng câu trả lời với tài liệu nguồn.

<a href="images/rag-query-results.png"><img src="../../../translated_images/vi/rag-query-results.6d69fcec5397f355.webp" alt="Kết quả Truy vấn RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ảnh chụp màn hình này hiển thị kết quả truy vấn với câu trả lời được tạo, tham chiếu nguồn, và điểm tương đồng cho mỗi đoạn được truy xuất.*

### Thử Nghiệm Với Các Câu Hỏi

Thử các loại câu hỏi khác nhau:
- Các sự kiện cụ thể: "Chủ đề chính là gì?"
- So sánh: "Sự khác biệt giữa X và Y là gì?"
- Tóm tắt: "Tóm tắt những điểm chính về Z"

Theo dõi cách điểm số tương đồng thay đổi dựa trên mức độ phù hợp câu hỏi với nội dung tài liệu.

## Các Khái Niệm Chính

### Chiến lược Chia Đoạn

Tài liệu được chia thành các đoạn 300 token với 30 token chồng lắp. Cân bằng này đảm bảo mỗi đoạn có đủ ngữ cảnh để có ý nghĩa trong khi vẫn đủ nhỏ để có thể đưa nhiều đoạn vào lời nhắc.

### Điểm Tương Đồng

Mỗi đoạn được truy xuất đều có điểm tương đồng từ 0 đến 1 biểu thị mức độ phù hợp với câu hỏi người dùng. Biểu đồ dưới đây trực quan hóa phạm vi điểm và cách hệ thống sử dụng để lọc kết quả:

<img src="../../../translated_images/vi/similarity-scores.b0716aa911abf7f0.webp" alt="Điểm Tương đồng" width="800"/>

*Biểu đồ này hiển thị phạm vi điểm từ 0 đến 1, với ngưỡng tối thiểu 0.5 để lọc bỏ những đoạn không phù hợp.*

Điểm số dao động từ 0 đến 1:
- 0.7-1.0: Rất liên quan, trùng khớp chính xác
- 0.5-0.7: Liên quan, ngữ cảnh tốt
- Dưới 0.5: Bị loại, quá khác biệt

Hệ thống chỉ truy xuất các đoạn trên ngưỡng tối thiểu để đảm bảo chất lượng.

Embeddings hoạt động tốt khi các ý nghĩa cụ thể được nhóm rõ ràng, nhưng vẫn có điểm mù. Biểu đồ dưới đây cho thấy các lỗi phổ biến — đoạn quá lớn tạo ra vec-tơ mờ nhạt, đoạn quá nhỏ thiếu ngữ cảnh, thuật ngữ mơ hồ chỉ đến nhiều nhóm, và tra cứu trùng khớp chính xác (ID, số bộ phận) hoàn toàn không hoạt động với embeddings:

<img src="../../../translated_images/vi/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Các lỗi thường gặp với Embedding" width="800"/>

*Biểu đồ này chỉ ra các lỗi thường gặp với embeddings: đoạn quá lớn, đoạn quá nhỏ, thuật ngữ mơ hồ chỉ đến nhiều nhóm, và tra cứu chính xác như ID.*

### Lưu trữ Trong Bộ Nhớ

Module này sử dụng lưu trữ trong bộ nhớ cho đơn giản. Khi bạn khởi động lại ứng dụng, các tài liệu đã tải lên sẽ bị mất. Hệ thống sản xuất sử dụng cơ sở dữ liệu vector bền vững như Qdrant hoặc Azure AI Search.

### Quản lý Cửa Sổ Ngữ Cảnh

Mỗi mô hình có giới hạn tối đa về cửa sổ ngữ cảnh. Bạn không thể đưa mọi đoạn từ một tài liệu lớn vào lời nhắc. Hệ thống lấy ra N đoạn liên quan nhất (mặc định là 5) để giữ trong giới hạn, đồng thời cung cấp đủ ngữ cảnh cho câu trả lời chính xác.

## Khi Nào Nên Dùng RAG

RAG không phải lúc nào cũng là lựa chọn phù hợp. Hướng dẫn quyết định dưới đây giúp bạn xác định khi nào RAG mang lại giá trị so với các phương pháp đơn giản hơn — như đưa nội dung trực tiếp vào lời nhắc hoặc dựa vào kiến thức tích hợp của mô hình — là đủ:

<img src="../../../translated_images/vi/when-to-use-rag.1016223f6fea26bc.webp" alt="Khi nào sử dụng RAG" width="800"/>

*Biểu đồ này minh họa hướng dẫn quyết định khi nào RAG có giá trị so với khi nào phương pháp đơn giản hơn là đủ.*

## Các Bước Tiếp Theo

**Module tiếp theo:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Điều hướng:** [← Trước: Module 02 - Kỹ thuật tạo Prompt](../02-prompt-engineering/README.md) | [Quay lại Trang chính](../README.md) | [Tiếp: Module 04 - Công cụ →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố từ chối trách nhiệm**:  
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi nỗ lực để đảm bảo độ chính xác, xin lưu ý rằng bản dịch tự động có thể chứa lỗi hoặc thiếu chính xác. Tài liệu gốc bằng ngôn ngữ bản địa nên được xem là nguồn chính xác và đáng tin cậy. Đối với các thông tin quan trọng, việc dịch thuật chuyên nghiệp do con người thực hiện được khuyến nghị. Chúng tôi không chịu trách nhiệm cho bất kỳ sự hiểu nhầm hoặc diễn giải sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->