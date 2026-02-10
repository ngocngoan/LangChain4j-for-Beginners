# 测试 LangChain4j 应用

## 目录

- [快速开始](../../../docs)
- [测试内容](../../../docs)
- [运行测试](../../../docs)
- [在 VS Code 中运行测试](../../../docs)
- [测试模式](../../../docs)
- [测试理念](../../../docs)
- [下一步](../../../docs)

本指南将引导您了解展示如何测试 AI 应用程序的测试方法，无需 API 密钥或外部服务。

## 快速开始

使用单条命令运行所有测试：

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/zh-CN/test-results.ea5c98d8f3642043.webp" alt="测试成功结果" width="800"/>

*成功执行测试，显示所有测试都通过，无失败*

## 测试内容

本课程专注于本地运行的**单元测试**。每个测试展示了一个单独的 LangChain4j 概念。

<img src="../../../translated_images/zh-CN/testing-pyramid.2dd1079a0481e53e.webp" alt="测试金字塔" width="800"/>

*测试金字塔展示单元测试（快速、隔离）、集成测试（真实组件）和端到端测试之间的平衡。本培训包含单元测试。*

| 模块 | 测试数 | 重点 | 关键文件 |
|--------|-------|-------|-----------|
| **00 - 快速开始** | 6 | 提示模板与变量替换 | `SimpleQuickStartTest.java` |
| **01 - 介绍** | 8 | 会话记忆与有状态聊天 | `SimpleConversationTest.java` |
| **02 - 提示工程** | 12 | GPT-5.2 模式、积极程度、结构化输出 | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | 文档导入、嵌入、相似度搜索 | `DocumentServiceTest.java` |
| **04 - 工具** | 12 | 函数调用与工具链 | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | 使用 Stdio 传输的模型上下文协议 | `SimpleMcpTest.java` |

## 运行测试

**从根目录运行所有测试：**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**运行指定模块的测试：**

**Bash:**
```bash
cd 01-introduction && mvn test
# 或者从根目录开始
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# 或者从根目录开始
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

**运行指定的测试方法：**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#是否应保持对话历史
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#应该保持对话历史
```

## 在 VS Code 中运行测试

如果您使用 Visual Studio Code，测试资源管理器提供图形界面来运行和调试测试。

<img src="../../../translated_images/zh-CN/vscode-testing.f02dd5917289dced.webp" alt="VS Code 测试资源管理器" width="800"/>

*VS Code 测试资源管理器显示所有 Java 测试类和单独的测试方法的测试树*

**在 VS Code 中运行测试：**

1. 点击活动栏中的试管图标打开测试资源管理器
2. 展开测试树查看所有模块和测试类
3. 点击任意测试旁的播放按钮单独运行该测试
4. 点击“运行所有测试”执行整个测试套件
5. 右键点击任意测试，选择“调试测试”以设置断点并逐步执行代码

测试资源管理器对通过的测试显示绿色勾号，并在测试失败时提供详细的失败信息。

## 测试模式

### 模式 1：测试提示模板

最简单的模式是测试提示模板，无需调用任何 AI 模型。您可以验证变量替换是否正确，且提示格式符合预期。

<img src="../../../translated_images/zh-CN/prompt-template-testing.b902758ddccc8dee.webp" alt="提示模板测试" width="800"/>

*提示模板测试展示变量替换流程：含占位符的模板 → 应用变量值 → 验证格式化输出*

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

该测试位于 `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`。

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

测试对话逻辑时，使用 Mockito 创建返回预设响应的假模型。这使测试快速、免费且确定性。

<img src="../../../translated_images/zh-CN/mock-vs-real.3b8b1f85bfe6845e.webp" alt="模拟与真实 API 比较" width="800"/>

*对比演示为何测试中偏好模拟：快速、免费、确定性且无需 API 密钥*

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
        assertThat(history).hasSize(6); // 3条用户消息 + 3条AI消息
    }
}
```

此模式出现在 `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`。模拟确保行为一致，方便验证记忆管理是否正确。

### 模式 3：测试对话隔离

会话记忆必须区分多个用户。此测试验证对话不会混淆上下文。

<img src="../../../translated_images/zh-CN/conversation-isolation.e00336cf8f7a3e3f.webp" alt="会话隔离" width="800"/>

*测试对话隔离展示不同用户的独立内存存储，防止上下文混淆*

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

每个对话维护自己的独立历史。在生产系统中，这种隔离对于多用户应用非常关键。

### 模式 4：独立测试工具

工具是 AI 可以调用的函数。直接测试工具，确保它们无论 AI 决策如何都能正确工作。

<img src="../../../translated_images/zh-CN/tools-testing.3e1706817b0b3924.webp" alt="工具测试" width="800"/>

*独立测试工具展示模拟工具执行，无需 AI 调用即可验证业务逻辑*

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

这些测试来自 `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java`，验证无 AI 参与的工具逻辑。链式示例展示一个工具的输出如何作为另一个工具输入。

### 模式 5：内存中 RAG 测试

RAG 系统通常需要向量数据库和嵌入服务。内存中模式让您无需外部依赖即可测试整条管线。

<img src="../../../translated_images/zh-CN/rag-testing.ee7541b1e23934b1.webp" alt="内存中 RAG 测试" width="800"/>

*内存中 RAG 测试工作流程展示文档解析、嵌入存储和相似度搜索，无需数据库*

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

此测试来自 `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java`，创建内存中文档，验证切分和元数据处理。

### 模式 6：MCP 集成测试

MCP 模块测试使用 stdio 传输的模型上下文协议集成。测试验证您的应用程序能以子进程方式启动和通信 MCP 服务器。

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

测试您的代码，而非 AI。您的测试应该通过检查提示构建、内存管理和工具执行来验证您编写的代码。AI 的响应多变，不应成为测试断言的一部分。问问自己，提示模板是否正确替换了变量，而不是 AI 是否给出正确答案。

使用模拟语言模型。它们是外部依赖，响应慢、成本高且不确定。模拟让测试快速（毫秒级而非秒级）、免费（无 API 费用）、确定性（每次结果相同）。

保持测试独立。每个测试应自行准备数据，不依赖其他测试，并在完成后清理环境。测试应无论顺序如何均能通过。

测试异常情况，超越正常路径。尝试空输入、超大输入、特殊字符、无效参数和边界条件。这些通常会暴露正常用例检测不到的bug。

使用描述性名称。比较 `shouldMaintainConversationHistoryAcrossMultipleMessages()` 与 `test1()`。前者清楚说明测试内容，调试失败时更易定位问题。

## 下一步

现在您了解测试模式，可以深入每个模块：

- **[00 - 快速开始](../00-quick-start/README.md)** - 从提示模板基础开始
- **[01 - 介绍](../01-introduction/README.md)** - 学习会话记忆管理
- **[02 - 提示工程](../02/prompt-engineering/README.md)** - 掌握 GPT-5.2 提示模式
- **[03 - RAG](../03-rag/README.md)** - 构建检索增强生成系统
- **[04 - 工具](../04-tools/README.md)** - 实现函数调用和工具链
- **[05 - MCP](../05-mcp/README.md)** - 集成模型上下文协议

每个模块的 README 提供此处测试概念的详细说明。

---

**导航：** [← 返回主页](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：  
本文件通过人工智能翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 进行翻译。虽然我们力求准确，但请注意自动翻译可能存在错误或不准确之处。原始文件的母语版本应被视为权威来源。对于重要信息，建议使用专业人工翻译。因使用本翻译而产生的任何误解或错误解释，我们概不负责。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->