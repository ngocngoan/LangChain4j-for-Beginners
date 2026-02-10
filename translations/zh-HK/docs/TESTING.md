# 測試 LangChain4j 應用程式

## 目錄

- [快速開始](../../../docs)
- [測試涵蓋範圍](../../../docs)
- [執行測試](../../../docs)
- [在 VS Code 執行測試](../../../docs)
- [測試範式](../../../docs)
- [測試理念](../../../docs)
- [下一步](../../../docs)

本指南帶您了解如何測試 AI 應用程式，無需 API 金鑰或外部服務。

## 快速開始

使用單一指令執行所有測試：

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/zh-HK/test-results.ea5c98d8f3642043.webp" alt="成功的測試結果" width="800"/>

*成功執行測試，所有測試通過且無失敗*

## 測試涵蓋範圍

本課程專注於本地執行的**單元測試**。每個測試演示 LangChain4j 中的一個特定概念。

<img src="../../../translated_images/zh-HK/testing-pyramid.2dd1079a0481e53e.webp" alt="測試金字塔" width="800"/>

*測試金字塔顯示單元測試（快速且獨立）、整合測試（真實組件）、端對端測試的平衡。本訓練涵蓋單元測試。*

| 模組 | 測試數 | 重點 | 主要檔案 |
|--------|-------|-------|-----------|
| **00 - 快速開始** | 6 | 提示模板與變數替換 | `SimpleQuickStartTest.java` |
| **01 - 介紹** | 8 | 對話記憶與有狀態聊天 | `SimpleConversationTest.java` |
| **02 - 提示工程** | 12 | GPT-5.2 模式、急切程度、結構化輸出 | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | 文件攝取、嵌入、相似度搜尋 | `DocumentServiceTest.java` |
| **04 - 工具** | 12 | 函數呼叫與工具鏈接 | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | 使用 Stdio 傳輸的模型上下文協定 | `SimpleMcpTest.java` |

## 執行測試

**從根目錄執行所有測試：**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**執行特定模組測試：**

**Bash:**
```bash
cd 01-introduction && mvn test
# 或從根目錄開始
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# 或由根目錄開始
mvn --% test -pl 01-introduction
```

**執行單一測試類：**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**執行特定測試方法：**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#應該保持對話歷史
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#應該保持對話歷史
```

## 在 VS Code 執行測試

使用 Visual Studio Code 時，Test Explorer 提供圖形化介面來執行和除錯測試。

<img src="../../../translated_images/zh-HK/vscode-testing.f02dd5917289dced.webp" alt="VS Code 測試瀏覽器" width="800"/>

*VS Code 測試瀏覽器顯示所有 Java 測試類別與個別測試方法的測試樹*

**在 VS Code 執行測試：**

1. 點擊活動列的燒杯圖標以開啟測試瀏覽器
2. 展開測試樹以查看所有模組和測試類別
3. 點擊任一測試旁的播放按鈕執行單獨測試
4. 點擊「執行所有測試」執行整個測試套件
5. 右鍵點擊任意測試，選擇「除錯測試」以設置斷點並單步調試程式碼

測試瀏覽器會用綠色勾號顯示通過的測試，失敗時會提供詳細的錯誤訊息。

## 測試範式

### 範式 1：測試提示模板

最簡單的範式是測試提示模板，而不呼叫任何 AI 模型。驗證變數替換正確且提示格式符合預期。

<img src="../../../translated_images/zh-HK/prompt-template-testing.b902758ddccc8dee.webp" alt="提示模板測試" width="800"/>

*測試提示模板展示變數替換流程：模板帶有佔位符 → 應用值 → 驗證格式化輸出*

```java
@Test
@DisplayName("Should format prompt template with variables")
void testPromptTemplateFormatting() {
    PromptTemplate template = PromptTemplate.from(
        "Best time to visit {{destination}} for {{activity}}?"
    );
    
    Prompt prompt = template.apply(Map.of(
        "destination", "Paris",
        "activity", "sightseeing"
    ));
    
    assertThat(prompt.text()).isEqualTo("Best time to visit Paris for sightseeing?");
}
```

此測試位於 `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`。

**執行方式：**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#測試提示模板格式化
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#測試提示模板格式化
```

### 範式 2：模擬語言模型

測試對話邏輯時，使用 Mockito 創建返回預設回應的假模型。這使測試快速、免費且可預測。

<img src="../../../translated_images/zh-HK/mock-vs-real.3b8b1f85bfe6845e.webp" alt="模擬與真實 API 比較" width="800"/>

*比對顯示為何偏好模擬測試：速度快、免費、結果可預測且不需 API 金鑰*

```java
@ExtendWith(MockitoExtension.class)
class SimpleConversationTest {
    
    private ConversationService conversationService;
    
    @Mock
    private OpenAiOfficialChatModel mockChatModel;
    
    @BeforeEach
    void setUp() {
        ChatResponse mockResponse = ChatResponse.builder()
            .aiMessage(AiMessage.from("This is a test response"))
            .build();
        when(mockChatModel.chat(anyList())).thenReturn(mockResponse);
        
        conversationService = new ConversationService(mockChatModel);
    }
    
    @Test
    void shouldMaintainConversationHistory() {
        String conversationId = conversationService.startConversation();
        
        ChatResponse mockResponse1 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 1"))
            .build();
        ChatResponse mockResponse2 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 2"))
            .build();
        ChatResponse mockResponse3 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 3"))
            .build();
        
        when(mockChatModel.chat(anyList()))
            .thenReturn(mockResponse1)
            .thenReturn(mockResponse2)
            .thenReturn(mockResponse3);

        conversationService.chat(conversationId, "First message");
        conversationService.chat(conversationId, "Second message");
        conversationService.chat(conversationId, "Third message");

        List<ChatMessage> history = conversationService.getHistory(conversationId);
        assertThat(history).hasSize(6); // 3 個用戶 + 3 個 AI 訊息
    }
}
```

此範式出現在 `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`。模擬能確保行為一致，讓您檢驗記憶管理是否正確。

### 範式 3：測試對話隔離

對話記憶必須保持多使用者隔離。此測試確保對話不會混淆上下文。

<img src="../../../translated_images/zh-HK/conversation-isolation.e00336cf8f7a3e3f.webp" alt="對話隔離" width="800"/>

*測試對話隔離展示不同使用者獨立記憶存儲，防止上下文混合*

```java
@Test
void shouldIsolateConversationsByid() {
    String conv1 = conversationService.startConversation();
    String conv2 = conversationService.startConversation();
    
    ChatResponse mockResponse = ChatResponse.builder()
        .aiMessage(AiMessage.from("Response"))
        .build();
    when(mockChatModel.chat(anyList())).thenReturn(mockResponse);

    conversationService.chat(conv1, "Message for conversation 1");
    conversationService.chat(conv2, "Message for conversation 2");

    List<ChatMessage> history1 = conversationService.getHistory(conv1);
    List<ChatMessage> history2 = conversationService.getHistory(conv2);
    
    assertThat(history1).hasSize(2);
    assertThat(history2).hasSize(2);
}
```

每個對話維持獨立歷史。在生產系統中，這種隔離對多使用者應用至關重要。

### 範式 4：獨立測試工具

工具是 AI 可呼叫的函數。直接測試工具以確保其正確運作，不受 AI 決策影響。

<img src="../../../translated_images/zh-HK/tools-testing.3e1706817b0b3924.webp" alt="工具測試" width="800"/>

*獨立測試工具，展示使用模擬工具執行而非 AI 呼叫，驗證業務邏輯*

```java
@Test
void shouldConvertCelsiusToFahrenheit() {
    TemperatureTool tempTool = new TemperatureTool();
    String result = tempTool.celsiusToFahrenheit(25.0);
    assertThat(result).containsPattern("77[.,]0°F");
}

@Test
void shouldDemonstrateToolChaining() {
    WeatherTool weatherTool = new WeatherTool();
    TemperatureTool tempTool = new TemperatureTool();

    String weatherResult = weatherTool.getCurrentWeather("Seattle");
    assertThat(weatherResult).containsPattern("\\d+°C");

    String conversionResult = tempTool.celsiusToFahrenheit(22.0);
    assertThat(conversionResult).containsPattern("71[.,]6°F");
}
```

這些測試來自 `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java`，驗證工具邏輯無需 AI 介入。鏈接範例示範一個工具的輸出如何成為另一個的輸入。

### 範式 5：記憶中 RAG 測試

RAG 系統傳統上需要向量資料庫和嵌入服務。記憶中範式讓您無需外部依賴即可測試整個流程。

<img src="../../../translated_images/zh-HK/rag-testing.ee7541b1e23934b1.webp" alt="記憶中 RAG 測試" width="800"/>

*記憶中 RAG 測試流程展示文件解析、嵌入存儲和相似度搜尋，無需資料庫*

```java
@Test
void testProcessTextDocument() {
    String content = "This is a test document.\nIt has multiple lines.";
    InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    
    DocumentService.ProcessedDocument result = 
        documentService.processDocument(inputStream, "test.txt");

    assertNotNull(result);
    assertTrue(result.segments().size() > 0);
    assertEquals("test.txt", result.segments().get(0).metadata().getString("filename"));
}
```

此測試來自 `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java`，在記憶中建立文件並驗證切塊及元資料處理。

### 範式 6：MCP 整合測試

MCP 模組測試使用 stdio 傳輸的模型上下文協定整合。測試確保應用能作為子程序啟動並與 MCP 伺服器通訊。

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` 中的測試驗證 MCP 用戶端行為。

**執行方式：**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## 測試理念

測試您撰寫的程式碼，而非 AI。本測試應驗證程式碼：提示如何構建、記憶如何管理，工具如何執行。AI 回應多樣化，不應納入測試斷言。請判斷提示模板是否正確替換變數，而非 AI 是否給出正確答案。

對語言模型使用模擬。它們是緩慢、昂貴且非確定性的外部依賴。模擬使測試快速（毫秒級而非秒級）、免費（無 API 成本）且可重複（結果一致）。

保持測試獨立。每項測試應自行建立資料，不依賴其他測試，且自行清理。測試無論執行順序如何均應通過。

測試邊界條件而非僅限理想情況。嘗試空輸入、超大輸入、特殊字元、無效參數與邊界條件。這些常揭露常規使用無法發現的錯誤。

使用具描述性的名稱。例如，比較 `shouldMaintainConversationHistoryAcrossMultipleMessages()` 與 `test1()`。前者明確指示被測內容，便利除錯失敗。

## 下一步

理解測試範式後，深入探索各模組：

- **[00 - 快速開始](../00-quick-start/README.md)** - 從提示模板基礎開始
- **[01 - 介紹](../01-introduction/README.md)** - 學習對話記憶管理
- **[02 - 提示工程](../02/prompt-engineering/README.md)** - 精通 GPT-5.2 提示模式
- **[03 - RAG](../03-rag/README.md)** - 建置檢索增強生成系統
- **[04 - 工具](../04-tools/README.md)** - 實作函數呼叫與工具鏈
- **[05 - MCP](../05-mcp/README.md)** - 整合模型上下文協定

各模組的 README 解釋了此處測試的詳細概念。

---

**導覽：** [← 返回主頁](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件已使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們力求準確，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威資料來源。對於重要資訊，建議採用專業人工翻譯。我們概不對因使用此翻譯而引致的任何誤解或誤釋負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->