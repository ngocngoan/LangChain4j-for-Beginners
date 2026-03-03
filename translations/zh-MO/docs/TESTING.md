# 測試 LangChain4j 應用程式

## 目錄

- [快速開始](../../../docs)
- [測試範圍](../../../docs)
- [執行測試](../../../docs)
- [在 VS Code 中執行測試](../../../docs)
- [測試模式](../../../docs)
- [測試理念](../../../docs)
- [下一步](../../../docs)

本指南引導您了解示範如何測試 AI 應用程式，而無需 API 金鑰或外部服務的測試。

## 快速開始

使用一個指令運行所有測試：

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

當所有測試通過時，您應該會看到如下截圖的輸出 — 測試沒有失敗。

<img src="../../../translated_images/zh-MO/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*成功執行測試，所有測試通過且無失敗*

## 測試範圍

本課程專注於本地執行的 **單元測試**。每個測試獨立示範了 LangChain4j 的特定概念。下面的測試金字塔展示了單元測試的位置 — 它們構成快速且可靠的基礎，您其餘的測試策略都建立於此。

<img src="../../../translated_images/zh-MO/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*測試金字塔顯示單元測試（快速、獨立）、整合測試（真實組件）及端對端測試的比例。本培訓涵蓋單元測試。*

| 模組 | 測試 | 重點 | 主要檔案 |
|--------|-------|-------|-----------|
| **00 - 快速開始** | 6 | 提示模板與變數替換 | `SimpleQuickStartTest.java` |
| **01 - 介紹** | 8 | 會話記憶與有狀態聊天 | `SimpleConversationTest.java` |
| **02 - 提示工程** | 12 | GPT-5.2 模式、積極性等級、結構化輸出 | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | 文件攝取、嵌入、相似度搜尋 | `DocumentServiceTest.java` |
| **04 - 工具** | 12 | 函數呼叫與工具串接 | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | 搭配 Stdio 傳輸的模型上下文協定 | `SimpleMcpTest.java` |

## 執行測試

**從根目錄運行所有測試：**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**運行指定模組的測試：**

**Bash:**
```bash
cd 01-introduction && mvn test
# 或從根目錄開始
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# 或者從根目錄開始
mvn --% test -pl 01-introduction
```

**運行單一測試類別：**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**運行指定測試方法：**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#應該保持對話歷史
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#應該保持對話記錄
```

## 在 VS Code 中執行測試

如果您使用 Visual Studio Code，測試總覽器提供了圖形介面來執行和除錯測試。

<img src="../../../translated_images/zh-MO/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code 測試總覽器顯示測試樹，包含所有 Java 測試類及個別測試方法*

**在 VS Code 中執行測試：**

1. 點擊活動列的燒杯圖示打開測試總覽器
2. 展開測試樹以查看所有模組和測試類別
3. 點擊任一測試旁的播放按鈕個別執行
4. 點擊「執行所有測試」以執行整個測試套件
5. 右鍵點擊任一測試並選擇「除錯測試」以設定斷點並逐步檢視程式碼

測試總覽器對通過的測試顯示綠色勾號，失敗時提供詳細的錯誤訊息。

## 測試模式

### 模式 1：測試提示模板

最簡單的模式是在不呼叫任何 AI 模型的情況下測試提示模板。您驗證變數替換是否正確，提示是否格式化如預期。

<img src="../../../translated_images/zh-MO/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*提示模板測試展示變數替換流程：帶有佔位符的模板 → 套用值 → 驗證格式化輸出*

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
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#測試提示模板格式
```

### 模式 2：模擬語言模型

測試會話邏輯時，使用 Mockito 創建假模型返回預設回應。這使測試快速、免費且可預測。

<img src="../../../translated_images/zh-MO/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*比較展示為何測試優先使用模擬：快速、免費、可預測，且無需 API 金鑰*

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
        assertThat(history).hasSize(6); // 3 用戶 + 3 AI 訊息
    }
}
```

此模式出現在 `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`。模擬確保行為一致，便於驗證記憶管理正常。

### 模式 3：測試會話隔離

會話記憶必須將多用戶分離。此測試驗證會話不會混淆上下文。

<img src="../../../translated_images/zh-MO/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*測試會話隔離，展示不同用戶分開的記憶存儲以防止上下文混淆*

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

每個會話保持獨立歷史。於生產系統中，此隔離對多用戶應用至關重要。

### 模式 4：獨立測試工具

工具是 AI 可呼叫的函數。直接測試它們確保即使 AI 不決定仍能正常運作。

<img src="../../../translated_images/zh-MO/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*獨立測試工具，展示用模擬工具執行，不須 AI 呼叫以驗證商業邏輯*

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

此測試來自 `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java`，驗證工具邏輯無需 AI 參與。串接範例說明一工具輸出如何作為另一工具輸入。

### 模式 5：記憶體內 RAG 測試

RAG 系統通常依賴向量資料庫和嵌入服務。記憶體內模式讓您無需外部依賴測試整條管線。

<img src="../../../translated_images/zh-MO/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*記憶體內 RAG 測試流程，展示文件解析、嵌入儲存與相似度搜索，無需資料庫*

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

此測試來自 `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java`，在記憶體建文件並驗證分塊與元資料處理。

### 模式 6：MCP 整合測試

MCP 模組測試使用 stdio 傳輸的模型上下文協定整合。這些測試驗證應用程式可作為子程序啟動並與 MCP 伺服器通訊。

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

測試您的程式碼，不是 AI。測試應驗證您撰寫的程式碼，檢查提示如何建構、記憶如何管理、工具如何執行。AI 回應有變化，不應成為測試斷言的一部分。要問自己您的提示模板是否正確替換變數，而不是 AI 是否給正確答案。

對語言模型使用模擬。它們是慢、貴且非決定性的外部依賴。使用模擬能讓測試快速（毫秒而非秒）、免費（無 API 費用）、決定性（每次結果一致）。

保持測試獨立。每個測試應自行準備資料，不依賴其他測試且執行後清理。測試不應受執行順序影響皆能通過。

測試邊界條件，超越正常流程。嘗試空輸入、非常大輸入、特殊字元、無效參數與邊界條件。這些通常揭露一般使用不會發現的錯誤。

使用具描述性的名稱。比較 `shouldMaintainConversationHistoryAcrossMultipleMessages()` 與 `test1()`。前者明確指出測試內容，方便除錯失敗。

## 下一步

既然您了解測試模式，請深入瞭解各模組：

- **[00 - 快速開始](../00-quick-start/README.md)** - 從提示模板基礎開始
- **[01 - 介紹](../01-introduction/README.md)** - 學習會話記憶管理
- **[02 - 提示工程](../02/prompt-engineering/README.md)** - 精通 GPT-5.2 提示模式
- **[03 - RAG](../03-rag/README.md)** - 建置檢索增強生成系統
- **[04 - 工具](../04-tools/README.md)** - 實作函數呼叫與工具串接
- **[05 - MCP](../05-mcp/README.md)** - 整合模型上下文協定

各模組 README 提供本處測試概念的詳細說明。

---

**導覽：** [← 返回主頁](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 所翻譯。雖然我哋致力追求準確，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件之母語版本應被視為權威來源。對於重要資訊，建議採用專業人工翻譯。我哋不對因使用本翻譯而引致嘅任何誤解或錯誤解讀承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->