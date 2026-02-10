# 測試 LangChain4j 應用程式

## 目錄

- [快速開始](../../../docs)
- [測試涵蓋範圍](../../../docs)
- [如何執行測試](../../../docs)
- [在 VS Code 中執行測試](../../../docs)
- [測試模式](../../../docs)
- [測試理念](../../../docs)
- [後續步驟](../../../docs)

本指南引導你瞭解示範如何測試 AI 應用程式的測試，且不需 API 金鑰或外部服務。

## 快速開始

透過單一指令執行所有測試：

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/zh-MO/test-results.ea5c98d8f3642043.webp" alt="成功的測試結果" width="800"/>

*成功執行測試並全部通過，無失敗*

## 測試涵蓋範圍

本課程專注於**在地端執行的單元測試**。每個測試都展示一個獨立的 LangChain4j 概念。

<img src="../../../translated_images/zh-MO/testing-pyramid.2dd1079a0481e53e.webp" alt="測試金字塔" width="800"/>

*測試金字塔展示單元測試（快速、獨立）、整合測試（實體組件）及端對端測試之間的平衡。本培訓涵蓋單元測試。*

| 模組 | 測試數 | 焦點 | 主要檔案 |
|--------|-------|-------|-----------|
| **00 - 快速開始** | 6 | 提示模板與變數替換 | `SimpleQuickStartTest.java` |
| **01 - 介紹** | 8 | 對話記憶與有狀態對話 | `SimpleConversationTest.java` |
| **02 - 提示工程** | 12 | GPT-5.2 模式、急切等級、結構化輸出 | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | 文件攝取、嵌入、相似度搜尋 | `DocumentServiceTest.java` |
| **04 - 工具** | 12 | 函式呼叫與工具串接 | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol 與 Stdio 傳輸 | `SimpleMcpTest.java` |

## 如何執行測試

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
# 或者由根目錄開始
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
mvn test -Dtest=SimpleConversationTest#應該保持對話歷史
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#應保持對話歷史
```

## 在 VS Code 中執行測試

若使用 Visual Studio Code，Test Explorer 提供圖形化介面執行及偵錯測試。

<img src="../../../translated_images/zh-MO/vscode-testing.f02dd5917289dced.webp" alt="VS Code 測試總管" width="800"/>

*VS Code 測試總管展示測試樹，包括所有 Java 測試類別及個別測試方法*

**於 VS Code 執行測試：**

1. 點擊活動欄的燒杯圖示開啟測試總管
2. 展開測試樹以檢視所有模組與測試類別
3. 點擊任一測試旁的播放鈕以單獨執行
4. 點擊「Run All Tests」執行整個測試套件
5. 右鍵任一測試並選擇「Debug Test」設置斷點逐步除錯

測試總管以綠色勾勾顯示通過測試，失敗時提供詳細錯誤訊息。

## 測試模式

### 模式 1：測試提示模板

最簡單的模式是測試提示模板，不呼叫任何 AI 模型。驗證變數替換是否正確，提示格式是否符合預期。

<img src="../../../translated_images/zh-MO/prompt-template-testing.b902758ddccc8dee.webp" alt="提示模板測試" width="800"/>

*測試提示模板展示變數替換流程：模板含佔位符 → 應用值 → 驗證格式化輸出*

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

**執行它：**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#測試提示範本格式化
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#測試提示範本格式化
```

### 模式 2：模擬語言模型

測試對話邏輯時，使用 Mockito 建立回傳預先定義答案的假模型。測試執行快速、免費且結果可預期。

<img src="../../../translated_images/zh-MO/mock-vs-real.3b8b1f85bfe6845e.webp" alt="模擬與真實 API 比較" width="800"/>

*比較顯示模擬較適合測試：快速、免費、結果穩定且不需 API 金鑰*

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

此模式出現在 `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`。模擬確保行為一致，可驗證記憶管理正確。

### 模式 3：測試對話隔離

對話記憶必須為多用戶分隔。此測試驗證對話不會混淆上下文。

<img src="../../../translated_images/zh-MO/conversation-isolation.e00336cf8f7a3e3f.webp" alt="對話隔離" width="800"/>

*測試對話隔離展示不同用戶的獨立記憶存儲，以避免上下文混淆*

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

每個對話保持獨立歷史。在生產系統中，隔離對多用戶應用極為重要。

### 模式 4：獨立測試工具

工具是 AI 可呼叫的函式。直接測試工具，確保其功能正確，不受 AI 判斷影響。

<img src="../../../translated_images/zh-MO/tools-testing.3e1706817b0b3924.webp" alt="工具測試" width="800"/>

*獨立測試工具展示模擬工具執行，無 AI 介入，驗證商業邏輯*

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

這些測試來自 `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java`，驗證工具邏輯，無需 AI。串接範例展示一工具輸出作為另一輸入。

### 模式 5：記憶體內 RAG 測試

RAG 系統通常需要向量資料庫及嵌入服務。此記憶體內模式允許不依賴外部即測試整條管線。

<img src="../../../translated_images/zh-MO/rag-testing.ee7541b1e23934b1.webp" alt="記憶體內 RAG 測試" width="800"/>

*記憶體內 RAG 測試工作流展示文件解析、嵌入儲存與相似度搜尋，無需資料庫*

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

該測試出自 `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java`，在記憶體中建立文件，驗證分塊與元資料處理。

### 模式 6：MCP 整合測試

MCP 模組測試 Model Context Protocol 與 stdio 傳輸整合。測試可驗證應用程式能作為子程序啟動與 MCP 伺服器溝通。

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` 中的測試驗證 MCP 客戶端行為。

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

測試你的程式碼，而非 AI。你的測試應驗證你的程式碼，通過檢查提示構建、記憶管理與工具執行。AI 回應是變動的，不應作為測試判斷依據。問自己提示模板是否正確替換變數，而非 AI 是否給出正確答案。

使用模擬測試語言模型。這些是外部依賴，且速度慢、成本高、結果不可預測。模擬使測試快速（毫秒級）、免費且結果一致。

保持測試獨立。每個測試自行設定資料，不依賴其他測試，且須自行清理。測試應無論執行順序皆能通過。

測試邊緣情況，超越理想路徑。嘗試空輸入、非常大輸入、特殊字元、無效參數及邊界條件。這些常暴露正常使用不會發現的錯誤。

使用描述性名稱。比較 `shouldMaintainConversationHistoryAcrossMultipleMessages()` 與 `test1()`。前者精確傳達被測試內容，讓除錯失敗時更容易。

## 後續步驟

既然你已瞭解測試模式，深入探究各模組：

- **[00 - 快速開始](../00-quick-start/README.md)** - 從提示模板基本開始
- **[01 - 介紹](../01-introduction/README.md)** - 學習對話記憶管理
- **[02 - 提示工程](../02/prompt-engineering/README.md)** - 精通 GPT-5.2 提示模式
- **[03 - RAG](../03-rag/README.md)** - 建立檢索增強生成系統
- **[04 - 工具](../04-tools/README.md)** - 實作函式呼叫與工具串連
- **[05 - MCP](../05-mcp/README.md)** - 整合 Model Context Protocol

各模組的 README 提供本處測試概念的詳細說明。

---

**導覽：** [← 返回主頁](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻譯而成。儘管我們致力於確保準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件之母語版本應被視為權威來源。對於重要資訊，建議採用專業人工翻譯。我們不對因使用本翻譯而引起之任何誤解或誤譯承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->