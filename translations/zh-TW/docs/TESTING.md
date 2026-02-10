# 測試 LangChain4j 應用程式

## 目錄

- [快速開始](../../../docs)
- [測試涵蓋內容](../../../docs)
- [執行測試](../../../docs)
- [在 VS Code 中執行測試](../../../docs)
- [測試模式](../../../docs)
- [測試理念](../../../docs)
- [後續步驟](../../../docs)

本指南帶您了解展示如何在不需要 API 金鑰或外部服務的情況下測試 AI 應用程式的測試案例。

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

<img src="../../../translated_images/zh-TW/test-results.ea5c98d8f3642043.webp" alt="成功的測試結果" width="800"/>

*成功執行測試，所有測試通過且無失敗*

## 測試涵蓋內容

本課程著重於可本地執行的**單元測試**。每個測試獨立展示一個 LangChain4j 的特定概念。

<img src="../../../translated_images/zh-TW/testing-pyramid.2dd1079a0481e53e.webp" alt="測試金字塔" width="800"/>

*測試金字塔顯示單元測試（快速、獨立）、整合測試（真實元件）與端到端測試間的平衡。本課程涵蓋單元測試。*

| 模組 | 測試數 | 焦點 | 主要檔案 |
|--------|-------|-------|-----------|
| **00 - 快速開始** | 6 | 提示模板與變數替換 | `SimpleQuickStartTest.java` |
| **01 - 介紹** | 8 | 對話記憶與狀態式聊天 | `SimpleConversationTest.java` |
| **02 - 提示工程** | 12 | GPT-5.2 模式、渴望度、結構化輸出 | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | 文件擷取、嵌入、相似性搜尋 | `DocumentServiceTest.java` |
| **04 - 工具** | 12 | 函數呼叫與工具鏈 | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | 使用 Stdio 傳輸的模型上下文協議 | `SimpleMcpTest.java` |

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

**執行特定模組的測試：**

**Bash:**
```bash
cd 01-introduction && mvn test
# 或從 root 開始
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# 或從根目錄開始
mvn --% test -pl 01-introduction
```

**執行單一測試類別：**

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
mvn test -Dtest=SimpleConversationTest#是否應該保持對話歷史
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#應該維持對話歷史
```

## 在 VS Code 中執行測試

如果您使用 Visual Studio Code，Test Explorer 提供圖形介面來執行和除錯測試。

<img src="../../../translated_images/zh-TW/vscode-testing.f02dd5917289dced.webp" alt="VS Code 測試總覽" width="800"/>

*VS Code 測試總覽顯示所有 Java 測試類別和個別測試方法的測試樹*

**於 VS Code 中執行測試：**

1. 點擊活動列上的燒杯圖示開啟測試總覽
2. 展開測試樹以查看所有模組與測試類別
3. 點選任意測試右側的執行按鈕，單獨執行該測試
4. 點選「Run All Tests」執行整個測試套件
5. 右鍵點擊任意測試，選擇「Debug Test」設定斷點並逐步除錯

測試總覽會以綠色勾選標記通過的測試，並在失敗時提供詳細的錯誤訊息。

## 測試模式

### 模式 1：測試提示模板

最簡單的模式是測試提示模板，不呼叫任何 AI 模型。您驗證變數替換是否正確，且提示格式是否符合預期。

<img src="../../../translated_images/zh-TW/prompt-template-testing.b902758ddccc8dee.webp" alt="提示模板測試" width="800"/>

*測試提示模板，展示變數替換流程：含佔位符的模板 → 套用值 → 驗證格式化輸出*

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

### 模式 2：語言模型模擬

測試對話邏輯時，使用 Mockito 創建假模型回傳預設回答，使測試快速、免費且確定性高。

<img src="../../../translated_images/zh-TW/mock-vs-real.3b8b1f85bfe6845e.webp" alt="模擬與真實 API 比較" width="800"/>

*比較顯示為何使用模擬進行測試：快速、免費、確定性高，且不需 API 金鑰*

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
        assertThat(history).hasSize(6); // 3 個使用者 + 3 個 AI 訊息
    }
}
```

此模式出現在 `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`。模擬確保行為一致，以驗證記憶管理正確運作。

### 模式 3：測試會話隔離

會話記憶必須將多位使用者保持分離。此測試驗證對話不會混淆上下文。

<img src="../../../translated_images/zh-TW/conversation-isolation.e00336cf8f7a3e3f.webp" alt="會話隔離" width="800"/>

*測試會話隔離，展示不同使用者使用獨立記憶庫防止上下文混雜*

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

每個會話維持獨立歷史紀錄。在生產系統中，這種隔離對多用戶應用非常關鍵。

### 模式 4：獨立測試工具

工具是 AI 可呼叫的函數。直接測試工具以確保其正確運作，不受 AI 決策影響。

<img src="../../../translated_images/zh-TW/tools-testing.3e1706817b0b3924.webp" alt="工具測試" width="800"/>

*獨立測試工具，展示模擬工具執行而不呼叫 AI 以驗證業務邏輯*

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

這些測試來自 `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java`，驗證工具邏輯不涉及 AI。鏈接範例展示一個工具的輸出如何作為另一個的輸入。

### 模式 5：記憶中 RAG 測試

RAG 系統通常需要向量資料庫與嵌入服務。記憶中模式讓您在無外部依賴狀況下測試整個流程。

<img src="../../../translated_images/zh-TW/rag-testing.ee7541b1e23934b1.webp" alt="記憶中 RAG 測試" width="800"/>

*記憶中 RAG 測試工作流程，展示文件解析、嵌入儲存與相似度搜尋，無需資料庫*

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

此測試來自 `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java`，在記憶中建立文件並驗證分塊和元資料處理。

### 模式 6：MCP 整合測試

MCP 模組測試使用 stdio 傳輸的模型上下文協議整合。這些測試確認您的應用能正確啟動並以子程序模式與 MCP 伺服器通訊。

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` 的測試驗證 MCP 用戶端行為。

**執行它們：**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## 測試理念

測試的是您的程式碼，而非 AI。您的測試應驗證您撰寫的程式碼，檢查提示如何構造、記憶如何管理，以及工具如何執行。AI 回應會變動，不應納入測試斷言。問自己您的提示模板是否正確替換變數，而非 AI 是否給出正確答案。

使用模擬來取代語言模型。它們是緩慢、昂貴且不確定性的外部依賴。模擬使測試快速，執行時間從秒縮短至毫秒；免費，無 API 費用；且確定性高，同樣的輸入帶來相同的結果。

讓測試保持獨立。每個測試應自行建立資料，不依賴其他測試，且應清理其環境。測試結果不應受執行順序影響。

測試邊緣案例，超出正向流程。嘗試空輸入、超大輸入、特殊字元、無效參數和邊界條件。這些經常揭露正常使用不會發現的錯誤。

使用具有描述性的命名。比較 `shouldMaintainConversationHistoryAcrossMultipleMessages()` 與 `test1()`。前者明確告訴您測試內容，便於失敗除錯。

## 後續步驟

現在您已了解測試模式，深入探索每個模組：

- **[00 - 快速開始](../00-quick-start/README.md)** - 從提示模板基礎開始
- **[01 - 介紹](../01-introduction/README.md)** - 學習對話記憶管理
- **[02 - 提示工程](../02/prompt-engineering/README.md)** - 精通 GPT-5.2 提示模式
- **[03 - RAG](../03-rag/README.md)** - 建立檢索增強生成系統
- **[04 - 工具](../04-tools/README.md)** - 實作函數呼叫與工具鏈
- **[05 - MCP](../05-mcp/README.md)** - 整合模型上下文協議

各模組的 README 裡提供詳細解說此處測試的概念。

---

**導覽：** [← 回主頁](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係由 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 所翻譯。雖然我們致力於提升翻譯準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威依據。對於關鍵資訊，建議採用專業人工翻譯。我們不對因使用本翻譯所產生之任何誤解或誤譯負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->