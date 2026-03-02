# Testing LangChain4j Applications

## Table of Contents

- [Quick Start](../../../docs)
- [Wet Tin De Tests Cover](../../../docs)
- [How To Run Di Tests](../../../docs)
- [How To Run Tests For VS Code](../../../docs)
- [Testing Patterns](../../../docs)
- [Testing Philosophy](../../../docs)
- [Next Steps](../../../docs)

Dis guide go waka you through di tests wey dem dey use show how to test AI applications without you need API keys or outside services.

## Quick Start

Run all di tests wit just one command:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

When all di tests pass, you go see output like di screenshot below — di tests run wit zero failures.

<img src="../../../translated_images/pcm/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Successful test execution showing all tests passing with zero failures*

## Wet Tin De Tests Cover

Dis course focus on **unit tests** wey dey run for local machine. Each test dey show one LangChain4j idea for isolation. Di testing pyramid below dey show where unit tests fit — dem dey form di fast, reliable foundation wey all your other test strategy base on top.

<img src="../../../translated_images/pcm/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Testing pyramid wey dey show balance between unit tests (fast, isolated), integration tests (real components), and end-to-end tests. Dis training na for unit testing.*

| Module | Tests | Focus | Key Files |
|--------|-------|-------|-----------|
| **00 - Quick Start** | 6 | Prompt templates and variable substitution | `SimpleQuickStartTest.java` |
| **01 - Introduction** | 8 | Conversation memory and stateful chat | `SimpleConversationTest.java` |
| **02 - Prompt Engineering** | 12 | GPT-5.2 patterns, eagerness levels, structured output | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Document ingestion, embeddings, similarity search | `DocumentServiceTest.java` |
| **04 - Tools** | 12 | Function calling and tool chaining | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol with Stdio transport | `SimpleMcpTest.java` |

## How To Run Di Tests

**Run all di tests from root:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Run tests for one specific module:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Or from di root
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Or from di root
mvn --% test -pl 01-introduction
```

**Run one test class:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Run specific test method:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#suppose mek e keep di tori wey dem don talk before
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#suppose make e dey keep di gist wey dey before
```

## How To Run Tests For VS Code

If you dey use Visual Studio Code, di Test Explorer get graphic interface wey fit help you run and debug di tests.

<img src="../../../translated_images/pcm/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer wey dey show di test tree wit all di Java test classes and individual test methods*

**To run the tests for VS Code:**

1. Open di Test Explorer by clicking di beaker icon for di Activity Bar
2. Expand di test tree to see all modules and test classes
3. Click di play button wey dey next to any test to run am one by one
4. Click "Run All Tests" to run all di suite
5. Right-click any test and choose "Debug Test" to set breakpoints and step through di code

Di Test Explorer go show green checkmarks if tests pass and go give you detailed failure messages if e fail.

## Testing Patterns

### Pattern 1: Testing Prompt Templates

Di simplest pattern na to test prompt templates without to call any AI model. You go check if di variable substitution dey work well and confirm say di prompts dey formatted like how you expect am.

<img src="../../../translated_images/pcm/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Testing prompt templates wey dey show variable substitution flow: template wey get placeholders → values dem apply → formatted output we verify*

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

Dis test dey `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

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

When you dey test conversation logic, use Mockito to create fake models wey go return set responses wey you don arrange before. Dis one dey make tests fast, free, and predictable.

<img src="../../../translated_images/pcm/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Comparison wey dey show why mocks dey beta for testing: dem fast, dem no cost anything, dem dey repeat same result, and dem no need API keys*

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
        assertThat(history).hasSize(6); // 3 user + 3 AI messages
    }
}
```

Dis pattern dey for `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Di mock dey make sure say behavior dey consistent so you fit check if memory management work well.

### Pattern 3: Testing Conversation Isolation

Conversation memory must keep many users separate. Dis test dey check if conversations no dey mix their contexts.

<img src="../../../translated_images/pcm/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Testing conversation isolation wey dey show separate memory stores for different users so no mixing of context*

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

Each conversation get e own independent history. For production systems, dis kind isolation na very important for multi-user applications.

### Pattern 4: Testing Tools Independently

Tools na functions wey di AI fit call. You test dem direct to make sure dem dey work well no matter how AI go take decide.

<img src="../../../translated_images/pcm/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Testing tools independently wey dey show mock tool execution without AI calls to verify business logic*

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

Dem get dis tests from `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` wey validate tool logic without AI interference. Di chaining example dey show how one tool output dey feed into another input.

### Pattern 5: In-Memory RAG Testing

RAG systems dey normally require vector databases plus embedding services. Di in-memory pattern dey allow you test all di pipeline without outside dependencies.

<img src="../../../translated_images/pcm/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*In-memory RAG testing workflow wey dey show document parsing, embedding storage, and similarity search without needing database*

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

Dis test from `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` dey create document for memory and verify chunking and metadata management.

### Pattern 6: MCP Integration Testing

Di MCP module dey test Model Context Protocol integration using stdio transport. Dem tests dey verify say your app fit spawn and communicate with MCP servers as subprocesses.

Di tests wey dey `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` dey validate MCP client behavior.

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

Test your code, no be AI. Your tests suppose validate di code wey you write by checking how prompts dey build, how memory dey managed, and how tools dey execute. AI response dey change, e no suppose dey part of test assertions. Ask yourself if your prompt template dey correctly substitute variables, no be if AI dey give the correct answer.

Use mocks for language models. Dem na external things wey dey slow, expensive and no dey predictable. Mocking dey make tests fast with milliseconds instead of seconds, free without API cost, and predictable with the same result every time.

Make tests independent. Every test suppose setup im own data, no rely on other tests and clean up after itself. Tests should pass no matter how you run am.

Test edge cases way beyond the common way. Try empty inputs, big big inputs, special characters, invalid parameters, and boundary conditions. These ones fit show bugs wey normal usage no go fit show.

Use descriptive names. Compare `shouldMaintainConversationHistoryAcrossMultipleMessages()` with `test1()`. Di first one go tell you exactly wetin di test mean to test, to make debugging easier.

## Next Steps

Now wey you don understand di testing patterns, you fit dive deep into each module:

- **[00 - Quick Start](../00-quick-start/README.md)** - Start wit prompt template basics
- **[01 - Introduction](../01-introduction/README.md)** - Learn conversation memory management
- **[02 - Prompt Engineering](../02/prompt-engineering/README.md)** - Master GPT-5.2 prompting patterns
- **[03 - RAG](../03-rag/README.md)** - Build retrieval-augmented generation systems
- **[04 - Tools](../04-tools/README.md)** - Implement function calling and tool chains
- **[05 - MCP](../05-mcp/README.md)** - Integrate Model Context Protocol

Each module README go provide detailed explanations of di concepts tested here.

---

**Navigation:** [← Back to Main](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**: Dis document don translate by AI translation service wey dem dey call [Co-op Translator](https://github.com/Azure/co-op-translator). Even though we dey try make am correct, abeg sabi say automated translation fit get mistakes or no correct well. Di original document for im own language na di correct one wey you suppose trust. If na beta important information, e better make person wey sabi do human translation help you do am. We no go responsible if any misunderstanding or wrong meaning come from dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->