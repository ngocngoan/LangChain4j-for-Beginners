# 测试 LangChain4j 应用程序

## 目录

- [快速开始](../../../docs)
- [测试涵盖内容](../../../docs)
- [运行测试](../../../docs)
- [在 VS Code 中运行测试](../../../docs)
- [测试模式](../../../docs)
- [测试理念](../../../docs)
- [后续步骤](../../../docs)

本指南将带您了解演示如何测试 AI 应用程序的测试步骤，无需 API 密钥或外部服务。

## 快速开始

运行所有测试，只需一个命令：

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

当所有测试通过时，您应该看到如下截图中的输出——测试运行无一失败。

<img src="../../../translated_images/zh-CN/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*成功执行测试，显示所有测试全部通过且无失败*

## 测试涵盖内容

本课程关注于本地运行的**单元测试**。每个测试都演示了一个具体的 LangChain4j 概念的独立实现。下面的测试金字塔展示了单元测试所在的位置——它们构成了快速、可靠的基础，构建您其余测试策略的基石。

<img src="../../../translated_images/zh-CN/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*测试金字塔展示单元测试（快速，隔离）、集成测试（真实组件）和端到端测试之间的平衡。本课程覆盖单元测试。*

| 模块 | 测试数 | 重点 | 关键文件 |
|--------|-------|-------|-----------|
| **00 - 快速开始** | 6 | 提示模板与变量替换 | `SimpleQuickStartTest.java` |
| **01 - 介绍** | 8 | 会话内存和有状态聊天 | `SimpleConversationTest.java` |
| **02 - 提示工程** | 12 | GPT-5.2 模式、积极性等级、结构化输出 | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | 文档导入、嵌入、相似度搜索 | `DocumentServiceTest.java` |
| **04 - 工具** | 12 | 函数调用与工具链 | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | 带有 Stdio 传输的模型上下文协议 | `SimpleMcpTest.java` |

## 运行测试

**在根目录运行所有测试：**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**运行特定模块的测试：**

**Bash:**
```bash
cd 01-introduction && mvn test
# 或者从根目录开始
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# 或来自根目录
mvn --% test -pl 01-introduction
```

**运行单个测试类：**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**运行特定测试方法：**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#应该维护对话历史
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#应保持对话历史
```

## 在 VS Code 中运行测试

如果您使用 Visual Studio Code，测试资源管理器提供了图形界面，用于运行和调试测试。

<img src="../../../translated_images/zh-CN/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code 测试资源管理器显示所有 Java 测试类和单个测试方法的测试树*

**在 VS Code 中运行测试的方法：**

1. 点击活动栏中的烧杯图标以打开测试资源管理器
2. 展开测试树查看所有模块和测试类
3. 点击任何测试旁的播放按钮单独运行
4. 点击“运行所有测试”执行整个测试套件
5. 右键点击任意测试选择“调试测试”，可设置断点并逐步调试代码

测试资源管理器用绿色对勾显示通过测试，测试失败时提供详细的错误信息。

## 测试模式

### 模式 1：测试提示模板

最简单的模式是测试提示模板，而不调用任何 AI 模型。您验证变量替换是否正确，提示是否格式化符合预期。

<img src="../../../translated_images/zh-CN/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*测试提示模板演示变量替换流程：带占位符的模板 → 应用变量值 → 验证格式化输出*

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

此测试位于 `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`。

**运行方法：**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#测试提示模板格式化
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#测试提示模板格式化
```

### 模式 2：模拟语言模型

测试会话逻辑时，使用 Mockito 创建返回预设响应的假模型。这使测试快速、免费且结果确定。

<img src="../../../translated_images/zh-CN/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*对比显示为何测试首选模拟：快速、免费、确定且无需 API 密钥*

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
        assertThat(history).hasSize(6); // 3 条用户消息 + 3 条 AI 消息
    }
}
```

此模式出现在 `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`。模拟确保行为一致，便于验证内存管理的正确性。

### 模式 3：测试会话隔离

会话内存必须将多个用户分开存储。此测试验证对话不会混淆上下文。

<img src="../../../translated_images/zh-CN/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*测试会话隔离展示不同用户的独立内存存储，防止上下文混合*

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

每个会话维持独立的历史记录。在生产系统中，这种隔离对多用户应用至关重要。

### 模式 4：独立测试工具

工具是 AI 可以调用的函数。直接测试它们，确保无论 AI 决策如何，工具都能正确工作。

<img src="../../../translated_images/zh-CN/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*独立测试工具显示模拟工具执行，无 AI 调用，以验证业务逻辑*

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

这些测试来自 `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java`，验证工具逻辑，无需 AI 参与。链式调用示例展示一个工具的输出如何作为下一个工具的输入。

### 模式 5：内存中 RAG 测试

传统 RAG 系统需要向量数据库和嵌入服务。内存中模式允许您在无外部依赖的情况下测试整个管道。

<img src="../../../translated_images/zh-CN/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*内存中 RAG 测试工作流显示文档解析、嵌入存储和相似度搜索，无需数据库*

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

此测试来自 `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java`，在内存中创建文档，验证拆分及元数据处理。

### 模式 6：MCP 集成测试

MCP 模块测试使用 stdio 传输的模型上下文协议集成。这些测试验证您的应用是否能以子进程方式启动并与 MCP 服务器通信。

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` 中的测试验证 MCP 客户端行为。

**运行方法：**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## 测试理念

测试您的代码，而非 AI。您的测试应通过检查提示如何构造、内存如何管理、工具如何执行来验证您编写的代码。AI 响应不稳定， 不应用于断言。问自己，提示模板是否正确替换变量，而非 AI 是否给出正确答案。

语言模型使用模拟。它们是外部依赖，速度慢、费用高且结果不确定。模拟让测试速度以毫秒级而非秒级，免费无 API 成本，且每次结果一致。

保持测试独立。每个测试应自行搭建数据环境，不依赖其他测试，且完成后清理环境。测试通过与否不依赖执行顺序。

测试边界情况，超出理想路径。尝试空输入、超大输入、特殊字符、无效参数和边界条件。它们通常揭示正常使用未发现的漏洞。

使用描述性命名。对比 `shouldMaintainConversationHistoryAcrossMultipleMessages()` 与 `test1()`。前者告诉您具体测试内容，更易定位故障。

## 后续步骤

理解测试模式后，请深入学习各模块：

- **[00 - 快速开始](../00-quick-start/README.md)** - 从提示模板基础开始
- **[01 - 介绍](../01-introduction/README.md)** - 了解会话内存管理
- **[02 - 提示工程](../02-prompt-engineering/README.md)** - 精通 GPT-5.2 提示模式
- **[03 - RAG](../03-rag/README.md)** - 构建检索增强生成系统
- **[04 - 工具](../04-tools/README.md)** - 实现函数调用与工具链
- **[05 - MCP](../05-mcp/README.md)** - 集成模型上下文协议

各模块的 README 提供对本处测试概念的详细解释。

---

**导航：** [← 返回主页](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：  
本文件使用AI翻译服务[Co-op Translator](https://github.com/Azure/co-op-translator)翻译。虽然我们努力确保准确性，但请注意自动翻译可能包含错误或不准确之处。原文应被视为权威来源。对于重要信息，建议采用专业人工翻译。本公司对因使用本翻译而产生的任何误解或误释不承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->