# Testing LangChain4j Applications

## Table of Contents

- [Quick Start](../../../docs)
- [Wetn Di Tests Dey Cover](../../../docs)
- [How To Run Di Tests](../../../docs)
- [How To Run Tests for VS Code](../../../docs)
- [Testing Patterns](../../../docs)
- [Testing Philosophy](../../../docs)
- [Wetin Next](../../../docs)

Dis guide go help you waka through di tests wey show how to test AI applications without need API keys or outside service dem.

## Quick Start

Run all tests with one command:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/pcm/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Successful test execution wey show say all tests pass without any failure*

## Wetn Di Tests Dey Cover

Dis course na about **unit tests** wey dey run locally. Every test dey show one specific LangChain4j idea alone.

<img src="../../../translated_images/pcm/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Testing pyramid wey show di balance between unit tests (fast, isolated), integration tests (real components), and end-to-end tests. Dis training cover unit testing.*

| Module | Tests | Focus | Key Files |
|--------|-------|-------|-----------|
| **00 - Quick Start** | 6 | Prompt templates and variable substitution | `SimpleQuickStartTest.java` |
| **01 - Introduction** | 8 | Conversation memory and stateful chat | `SimpleConversationTest.java` |
| **02 - Prompt Engineering** | 12 | GPT-5.2 patterns, eagerness levels, structured output | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Document ingestion, embeddings, similarity search | `DocumentServiceTest.java` |
| **04 - Tools** | 12 | Function calling and tool chaining | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol with Stdio transport | `SimpleMcpTest.java` |

## How To Run Di Tests

**Run all tests from root:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Run tests for one particular module:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Or from root na
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Or fom root
mvn --% test -pl 01-introduction
```

**Run just one test class:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Run one particular test method:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#fọ gẹ́t conversation history keep stay dey
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#mek sure sey conversation history dey kept
```

## How To Run Tests for VS Code

If you dey use Visual Studio Code, the Test Explorer dey give graphical interface to run and debug tests.

<img src="../../../translated_images/pcm/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer dey show test tree with all Java test classes and individual test methods*

**To run tests for VS Code:**

1. Open Test Explorer by clicking the beaker icon for Activity Bar
2. Expand the test tree to see all modules and test classes
3. Click di play button beside any test to run am alone
4. Click "Run All Tests" to run the whole suite
5. Right-click any test and choose "Debug Test" to set breakpoints and step through code

Test Explorer dey show green check marks for passing tests and dey give detailed failure messages if test fail.

## Testing Patterns

### Pattern 1: Testing Prompt Templates

Di simplest pattern na to test prompt templates without calling any AI model. You go check say variable substitution dey work well and prompt dem dey formatted as e suppose be.

<img src="../../../translated_images/pcm/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Testing prompt templates dey show how variable substitution dey flow: template with placeholders → values added → formatted output confirmed*

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

Dis test dey inside `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Run am:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testPromptTemplateFormatting
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testPromptTemplateFormatting
```

### Pattern 2: Mocking Language Models

When you dey test conversation logic, use Mockito to create fake models wey go return set answers. This one make tests fast, free, and predictable.

<img src="../../../translated_images/pcm/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Comparison wey show why mocks dey better for testing: dem dey fast, free, predictable, and no need API keys*

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
        assertThat(history).hasSize(6); // 3 user + 3 AI mesij dem
    }
}
```

Dis pattern dey `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. The mock dey ensure say behaviour remain consistent so you fit verify memory management dey work well.

### Pattern 3: Testing Conversation Isolation

Conversation memory suppose separate many users. Dis test go verify say conversations no dey mix context.

<img src="../../../translated_images/pcm/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Testing conversation isolation dey show separate memory stores for different users to prevent context mixing*

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

Every conversation get its own independent history. For production systems, dis isolation dey very important for multi-user apps.

### Pattern 4: Testing Tools Independently

Tools na functions wey AI fit call. Test dem directly to make sure dem dey work well regardless of wetin AI talk.

<img src="../../../translated_images/pcm/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Testing tools independently dey show mock tool running without AI calls to check business logic*

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

Dis tests wey come from `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` dey validate tool logic without AI. The chaining example dey show how tool one output go feed tool two input.

### Pattern 5: In-Memory RAG Testing

RAG systems typically need vector databases and embedding services. The in-memory pattern let you test whole pipeline without external dependencies.

<img src="../../../translated_images/pcm/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*In-memory RAG testing workflow dey show document parsing, embedding storage, and similarity search without need database*

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

Dis test from `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` dey create document inside memory and verify chunking and metadata handling.

### Pattern 6: MCP Integration Testing

The MCP module dey test Model Context Protocol integration using stdio transport. These tests dey verify say your app fit spawn and communicate with MCP servers as subprocesses.

Tests inside `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` dey validate MCP client behaviour.

**Run dem:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Testing Philosophy

Test your code, no be the AI. Your tests suppose check di code wey you write by verifying how prompts dey built, how memory dey managed, and how tools dey run. AI response fit change, no suppose become part of test assertion. Ask yourself if your prompt template dey correct to substitute variables, no be if AI give correct answer.

Use mocks for language models. Dem na external dependencies wey slow, expensive, and no dey predictable. Mocking make tests fast with milliseconds instead of seconds, free with no API cost, and predictable with same result every time.

Keep tests independent. Every test suppose set up im own data, no depend on other tests, and clean up after imself. Tests suppose pass regardless of wetin order you run am.

Test edge cases pass wetin normal one dey expect. Try empty input, very big input, special characters, invalid parameters, and boundary conditions. Dem go help find bugs wey normal use no go show.

Use names wey tell story. Compare `shouldMaintainConversationHistoryAcrossMultipleMessages()` with `test1()`. Di first one go tell you exactly wetin dey test, e go make debugging easy.

## Wetin Next

Now wey you don understand testing patterns, dig deeper for each module:

- **[00 - Quick Start](../00-quick-start/README.md)** - Start with prompt template basics
- **[01 - Introduction](../01-introduction/README.md)** - Learn conversation memory management
- **[02 - Prompt Engineering](../02-prompt-engineering/README.md)** - Master GPT-5.2 prompting patterns
- **[03 - RAG](../03-rag/README.md)** - Build retrieval-augmented generation systems
- **[04 - Tools](../04-tools/README.md)** - Implement function calling and tool chains
- **[05 - MCP](../05-mcp/README.md)** - Integrate Model Context Protocol

Every module README get detailed explanations of concepts wey dem test for here.

---

**Navigation:** [← Back to Main](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:  
Dis dokument don translate by AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). Even though we try make e correct, abeg sabi say automated translation fit get errors or waka enter way we no suppose. Di original dokument for im own language na di correct source. For important info, make you use professional human translation. We no responsible for any misunderstanding or wrong meaning we fit come from dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->