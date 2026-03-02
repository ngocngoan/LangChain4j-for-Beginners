# 測試 LangChain4j 應用程式

## 目錄

- [快速開始](../../../docs)
- [測試涵蓋範圍](../../../docs)
- [執行測試](../../../docs)
- [在 VS Code 中執行測試](../../../docs)
- [測試模式](../../../docs)
- [測試理念](../../../docs)
- [後續步驟](../../../docs)

本指南引導你透過測試示範如何在不需要 API 金鑰或外部服務的情況下測試 AI 應用程式。

## 快速開始

使用一條命令執行所有測試：

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

當所有測試通過時，您應該會看到類似下方截圖的輸出結果—測試以零失敗完成執行。

<img src="../../../translated_images/zh-HK/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*成功執行測試，全部測試以零失敗通過*

## 測試涵蓋範圍

本課程專注於本地執行的**單元測試**。每個測試展示一個 LangChain4j 的特定概念。下方測試金字塔展示了單元測試的位置——它們構成快速、可靠的基礎，亦是你整體測試策略的根基。

<img src="../../../translated_images/zh-HK/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*測試金字塔顯示單元測試（快速、獨立）、整合測試（真實組件）與端對端測試的比例。本培訓涵蓋單元測試。*

| 模組 | 測試數 | 重點 | 主要檔案 |
|--------|-------|-------|-----------|
| **00 - 快速開始** | 6 | 提示模板與變數替換 | `SimpleQuickStartTest.java` |
| **01 - 介紹** | 8 | 對話記憶與狀態聊天 | `SimpleConversationTest.java` |
| **02 - 提示工程** | 12 | GPT-5.2 模式、 eager 級別、結構化輸出 | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | 文件導入、嵌入向量、相似度搜索 | `DocumentServiceTest.java` |
| **04 - 工具** | 12 | 函數呼叫與工具串接 | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | 利用 Stdio 傳輸的模型上下文協定 | `SimpleMcpTest.java` |

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
# 或從根目錄開始
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# 或者從根目錄開始
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
mvn --% test -Dtest=SimpleConversationTest#應該保持對話歷史
```

## 在 VS Code 中執行測試

如果你使用 Visual Studio Code，Test Explorer 提供圖形介面來執行與除錯測試。

<img src="../../../translated_images/zh-HK/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code 測試總管顯示所有 Java 測試類別與各個測試方法的樹狀清單*

**在 VS Code 中執行測試步驟：**

1. 點擊活動欄中的燒杯圖示開啟測試總管
2. 展開測試樹狀結構查看所有模組及測試類別
3. 點擊任一測試旁的播放按鈕執行該測試
4. 點擊「Run All Tests」執行全部測試套件
5. 右鍵任一測試並選擇「Debug Test」設置斷點並逐步除錯程式碼

測試總管使用綠色勾選標示通過的測試，失敗時會提供詳細錯誤訊息。

## 測試模式

### 模式 1：測試提示模板

最簡單的模式是測試提示模板，本身不呼叫任何 AI 模型。你確認變量替換正確無誤，且提示格式符合預期。

<img src="../../../translated_images/zh-HK/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*測試提示模板顯示變量替換流程：模板帶有佔位符 → 應用值 → 驗證格式化後輸出*

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

**執行方法：**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#測試提示模板格式化
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#測試提示模板格式化
```

### 模式 2：模擬語言模型

測試對話邏輯時，使用 Mockito 建立返回預定義結果的假模型。這使測試快速、免費且結果確定。

<img src="../../../translated_images/zh-HK/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*比較展示為何模擬優於真實：模擬快速、免費、確定性高，且無需 API 金鑰*

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

該模式出現在 `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`。模擬確保行為一致，讓你能驗證記憶管理運作正常。

### 模式 3：測試對話隔離

對話記憶必須保持多用戶間隔離。該測試驗證對話上下文不會交叉混淆。

<img src="../../../translated_images/zh-HK/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*測試對話隔離顯示不同用戶有獨立記憶庫以避免上下文混合*

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

每個對話皆維持獨立歷史。在生產系統中，此隔離對多用戶應用非常重要。

### 模式 4：獨立測試工具

工具是 AI 可呼叫的函數。直接測試它們以確保無論 AI 決策如何，工具都能正常運作。

<img src="../../../translated_images/zh-HK/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*獨立測試工具展示模擬工具執行、無需 AI 呼叫以驗證業務邏輯*

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

這些測試來自 `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java`，用於驗證工具邏輯不受 AI 影響。串接範例展示一個工具輸出如何成為另一個工具輸入。

### 模式 5：記憶內 RAG 測試

RAG 系統傳統上需用向量資料庫及嵌入服務。此記憶內模式讓你在無外部依賴下測試整個流程。

<img src="../../../translated_images/zh-HK/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*記憶內 RAG 測試工作流程，展示文件解析、向量存儲與相似度搜尋，無需資料庫*

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

此測試來自 `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java`，建立記憶中文件並驗證分塊與元資料處理。

### 模式 6：MCP 整合測試

MCP 模組測試使用 stdio 傳輸的模型上下文協定整合。這些測試驗證你的應用能以子程序形式啟動並與 MCP 伺服器通信。

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

測試你的程式碼，而非 AI。你的測試應驗證你寫的程式碼，檢查提示如何構造、記憶如何管理、工具如何執行。AI 回應會變化，不應納入測試斷言。問自己的是「你的提示模板是否正確替換了變數」，而不是「AI 是否給出正確答案」。

使用模擬語言模型。它們是外部依賴，執行慢、昂貴且非確定性。模擬讓測試快速（毫秒級而非秒級）、免費且結果一致。

保持測試獨立。每個測試都應自行設置資料、不依賴其他測試且執行完畢會清理自身。無論執行順序如何，測試皆會通過。

測試邊界狀況與極端案例，超越理想情況。嘗試空輸入、極大輸入、特殊字元、無效參數及邊緣條件。這些常揭露正常使用無法發現的 bug。

使用具描述性的命名。將 `shouldMaintainConversationHistoryAcrossMultipleMessages()` 與 `test1()` 比較。前者明確說明測試內容，讓除錯失敗更容易。

## 後續步驟

既然你已了解測試模式，可深入學習各模組：

- **[00 - 快速開始](../00-quick-start/README.md)** — 從提示模板基礎開始
- **[01 - 介紹](../01-introduction/README.md)** — 學習對話記憶管理
- **[02 - 提示工程](../02/prompt-engineering/README.md)** — 精通 GPT-5.2 的提示模式
- **[03 - RAG](../03-rag/README.md)** — 建構檢索增強生成系統
- **[04 - 工具](../04-tools/README.md)** — 實作函數呼叫與工具串接
- **[05 - MCP](../05-mcp/README.md)** — 整合模型上下文協定

各模組的 README 提供本章測試概念的詳細說明。

---

**導航：** [← 返回主頁](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們力求準確，但請注意自動翻譯可能包含錯誤或不準確之處。原文文件的原始語言版本應視為權威來源。對於重要資訊，建議採用專業人工翻譯。我們不對因使用此翻譯而引起的任何誤解或曲解承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->