# 測試 LangChain4j 應用程式

## 目錄

- [快速開始](../../../docs)
- [測試涵蓋範圍](../../../docs)
- [執行測試](../../../docs)
- [在 VS Code 中執行測試](../../../docs)
- [測試模式](../../../docs)
- [測試理念](../../../docs)
- [後續步驟](../../../docs)

本指南將帶您了解示範如何在不要求 API 金鑰或外部服務的情況下測試 AI 應用程式的測試。

## 快速開始

使用單一指令執行全部測試：

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

當所有測試通過後，您應該會看到如下截圖所示的輸出 — 測試完全通過且無失敗。

<img src="../../../translated_images/zh-TW/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*成功執行測試，所有測試通過且無失敗*

## 測試涵蓋範圍

本課程專注於本地執行的**單元測試**。每個測試皆獨立展示特定的 LangChain4j 概念。下圖的測試金字塔顯示單元測試的位置 — 它們是快速且可靠的基礎，後續測試策略皆建構於此之上。

<img src="../../../translated_images/zh-TW/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*測試金字塔顯示單元測試（快速、獨立）、整合測試（使用真實元件）、端對端測試之間的平衡。本課程涵蓋單元測試。*

| 模組 | 測試數 | 焦點 | 主要檔案 |
|--------|-------|-------|-----------|
| **00 - 快速開始** | 6 | 提示範本與變數代換 | `SimpleQuickStartTest.java` |
| **01 - 介紹** | 8 | 對話記憶與有狀態聊天 | `SimpleConversationTest.java`  |
| **02 - 提示工程** | 12 | GPT-5.2 模式、積極程度、結構化輸出 | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | 文件輸入、嵌入、相似度搜尋 | `DocumentServiceTest.java` |
| **04 - 工具** | 12 | 函式呼叫與工具鍊結 | `SimpleToolsTest.java` |
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

**執行特定模組的測試：**

**Bash:**
```bash
cd 01-introduction && mvn test
# 或者從根目錄開始
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# 或從根目錄
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
mvn test -Dtest=SimpleConversationTest#應該維持對話歷史
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#是否應維持對話歷史
```

## 在 VS Code 中執行測試

若您使用 Visual Studio Code，測試總管（Test Explorer）提供圖形介面來執行及偵錯測試。

<img src="../../../translated_images/zh-TW/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code 測試總管顯示測試樹，包括所有 Java 測試類別與個別測試方法*

**於 VS Code 執行測試：**

1. 點擊活動列（Activity Bar）中的燒杯圖示開啟測試總管
2. 展開測試樹以檢視所有模組與測試類別
3. 點擊任一測試旁的播放鈕以執行該測試
4. 點擊「執行所有測試」來執行整個測試套件
5. 右鍵點擊任一測試並選擇「偵錯測試」以設定斷點並逐步除錯程式碼

測試總管以綠色核取符號顯示通過測試，失敗時則提供詳細錯誤訊息。

## 測試模式

### 模式 1：測試提示範本

最簡單的模式是測試提示範本，不呼叫任何 AI 模型。您驗證變數代換是否正確且提示格式符合預期。

<img src="../../../translated_images/zh-TW/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*測試提示範本顯示變數代換流程：帶有佔位符的範本 → 套用數值 → 驗證格式化輸出*

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
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#測試提示範本格式化
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#測試提示模板格式化
```

### 模式 2：模擬語言模型

測試對話邏輯時，使用 Mockito 創建回傳預先設定響應的假模型。這使測試快速、免費且可重複。

<img src="../../../translated_images/zh-TW/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*比較說明模擬測試的優勢：快速、免費、可重複，且不需 API 金鑰*

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

此模式實現在 `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`。模擬保證行為一致，讓您能驗證記憶管理是否正常。

### 模式 3：測試對話隔離

對話記憶必須保持多用戶隔離。本測試確保不同對話不混淆上下文。

<img src="../../../translated_images/zh-TW/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*測試對話隔離顯示不同用戶有獨立記憶儲存，避免上下文混淆*

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

每個對話維護獨立歷史。在生產系統中，多用戶應用必須確保此隔離。

### 模式 4：獨立測試工具

工具是 AI 可以呼叫的函式。直接測試工具確保它們運作正常，不依賴 AI 決策。

<img src="../../../translated_images/zh-TW/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*獨立測試工具展示模擬工具執行、不呼叫 AI 以驗證業務邏輯*

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

這些測試來自 `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java`，驗證工具邏輯無需 AI 介入。鍊結範例顯示如何將一工具輸出輸入到另一工具。

### 模式 5：記憶體內 RAG 測試

RAG 系統傳統上需要向量資料庫與嵌入服務。記憶體內模式讓您在無外部依賴下測試整體流程。

<img src="../../../translated_images/zh-TW/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*記憶體內 RAG 測試流程示意文件解析、嵌入儲存與相似度搜尋，無需資料庫*

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

此測試來自 `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java`，在記憶體內建立文件並驗證切段及元資料處理。

### 模式 6：MCP 整合測試

MCP 模組測試使用 stdio 傳輸的模型上下文協定整合。測試驗證您的應用可作為子程序啟動並與 MCP 伺服器通訊。

位於 `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` 的測試驗證 MCP 客戶端行為。

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

測試您的程式碼，而非 AI。測試應驗證您撰寫的程式碼，檢查提示如何建立、記憶如何管理以及工具如何執行。AI 回應會變動，因此不應納入測試斷言。問自己提示範本是否正確代換變數，而非 AI 是否回答正確。

使用模擬語言模型。它們是慢、昂貴、不可重複的外部依賴。模擬讓測試快（毫秒級而非秒級）、免費（無 API 成本）且重複性高（結果一致）。

保持測試獨立。每個測試應自行建立資料，不依賴其他測試，且執行後清理。測試結果不應受執行順序影響。

測試邊界條件與非理想情境。嘗試空輸入、非常大輸入、特殊字元、無效參數及邊界條件。這些常揭露正常使用不易發現的錯誤。

使用描述性名稱。比較 `shouldMaintainConversationHistoryAcrossMultipleMessages()` 與 `test1()`。前者明確說明測試內容，除錯失敗時更容易。

## 後續步驟

既然您已了解測試模式，深入學習每個模組：

- **[00 - 快速開始](../00-quick-start/README.md)** - 從提示範本基礎開始
- **[01 - 介紹](../01-introduction/README.md)** - 學習對話記憶管理
- **[02 - 提示工程](../02-prompt-engineering/README.md)** - 精通 GPT-5.2 提示模式
- **[03 - RAG](../03-rag/README.md)** - 建立檢索增強生成系統
- **[04 - 工具](../04-tools/README.md)** - 實作函式呼叫與工具鍊結
- **[05 - MCP](../05-mcp/README.md)** - 整合模型上下文協定

每個模組的 README 都詳細解釋此處測試的概念。

---

**導覽：** [← 返回主頁](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於提供準確的翻譯，但請注意自動翻譯可能包含錯誤或不準確之處。原始語言版本之文件應視為權威來源。對於重要資訊，建議採用專業人工翻譯。我們不對因使用本翻譯而產生之任何誤解或曲解負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->