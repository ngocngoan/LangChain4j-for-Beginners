# Testing LangChain4j Applications

## Table of Contents

- [Quick Start](../../../docs)
- [What the Tests Cover](../../../docs)
- [Running the Tests](../../../docs)
- [Running Tests in VS Code](../../../docs)
- [Testing Patterns](../../../docs)
- [Testing Philosophy](../../../docs)
- [Next Steps](../../../docs)

This guide walks you through the tests that demonstrate how to test AI applications without requiring API keys or external services.

## Quick Start

Run all tests with a single command:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/pa/test-results.ea5c98d8f3642043.webp" alt="ਸਫਲ ਟੈਸਟ ਨਤੀਜੇ" width="800"/>

*ਸਫਲ ਟੈਸਟ ਕਾਰਜਕਾਰੀ ਦਿਖਾਉਂਦਾ ਹੈ ਸਾਰੇ ਟੈਸਟ ਜਿਹੜੇ ਬਿਨਾਂ ਕਿਸੇ ਅਸਫਲਤਾ ਦੇ ਪਾਸ ਹੋ ਰਹੇ ਹਨ*

## What the Tests Cover

This course focuses on **unit tests** that run locally. Each test demonstrates a specific LangChain4j concept in isolation.

<img src="../../../translated_images/pa/testing-pyramid.2dd1079a0481e53e.webp" alt="ਪ੍ਰੀਖਿਆ ਪਿਰਾਮਿਡ" width="800"/>

*ਪ੍ਰੀਖਿਆ ਪਿਰਾਮਿਡ ਦਿਖਾ ਰਹੀ ਹੈ ਸੰਤੁਲਨ ਯੂਨੀਟ ਟੈਸਟਾਂ (ਤੇਜ਼, ਵੱਖਰੇ), ਇੰਟੀਗ੍ਰੇਸ਼ਨ ਟੈਸਟਾਂ (ਅਸਲੀ ਕਾਮਪੋਨੈਂਟ), ਅਤੇ ਅੰਤ-ਤੱਕ-ਅੰਤ ਟੈਸਟਾਂ ਵਿੱਚ। ਇਹ ਤਾਲੀਮ ਯੂਨੀਟ ਪ੍ਰੀਖਿਆ ਨੂੰ ਧਿਆਨ ਵਿੱਚ ਰੱਖਦੀ ਹੈ।*

| Module | Tests | Focus | Key Files |
|--------|-------|-------|-----------|
| **00 - Quick Start** | 6 | ਪ੍ਰੰਪਟ ਟੈਂਪਲੇਟ ਅਤੇ ਵੈਰੀਏਬਲ ਸਬਸਟਿਊਸ਼ਨ | `SimpleQuickStartTest.java` |
| **01 - Introduction** | 8 | ਗੱਲਬਾਤ ਦੀ ਯਾਦ ਅਤੇ ਸਟੇਟਫੁਲ ਚੈਟ | `SimpleConversationTest.java` |
| **02 - Prompt Engineering** | 12 | GPT-5.2 ਪੈਟਰਨਸ, ਤਤਪਰਤਾ ਦੇ ਸਤਰ, ਸੰਜੋਤਾ ਨਿਕਾਸ | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | ਦਸਤਾਵੇਜ਼ ਇਨਕੈਸ਼ਨ, ਐਮਬੈੱਡਿੰਗ, ਸਮਾਨਤਾ ਖੋਜ | `DocumentServiceTest.java` |
| **04 - Tools** | 12 | ਫੰਕਸ਼ਨ ਕਾਲਿੰਗ ਅਤੇ ਟੂਲ ਚੇਨਿੰਗ | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | ਮਾਡਲ ਕਾਂਟੈਕਸਟ ਪ੍ਰੋਟੋਕੋਲ ਸਟਡੀਓ ਟ੍ਰਾਂਸਪੋਰਟ ਨਾਲ | `SimpleMcpTest.java` |

## Running the Tests

**Run all tests from root:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**ਕਿਸੇ ਖਾਸ ਮੋਡੀਊਲ ਲਈ ਟੈਸਟ ਚਲਾਓ:**

**Bash:**
```bash
cd 01-introduction && mvn test
# ਜਾਂ ਰੂਟ ਤੋਂ
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# ਜਾਂ ਮੂਲ ਤੋਂ
mvn --% test -pl 01-introduction
```

**ਇੱਕ ਸਿੰਗਲ ਟੈਸਟ ਕਲਾਸ ਚਲਾਓ:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**ਕਿਸੇ ਖਾਸ ਟੈਸਟ ਮੈਥਡ ਨੂੰ ਚਲਾਓ:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#ਗੱਲਬਾਤ ਦੇ ਇਤਿਹਾਸ ਨੂੰ ਜਾਰੀ ਰੱਖਣਾ ਚਾਹੀਦਾ ਹੈ
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#ਗੱਲਬਾਤ ਦਾ ਇਤਿਹਾਸ ਬਰਕਰਾਰ ਰੱਖਣਾ ਚਾਹੀਦਾ ਹੈ
```

## Running Tests in VS Code

If you're using Visual Studio Code, the Test Explorer provides a graphical interface for running and debugging tests.

<img src="../../../translated_images/pa/vscode-testing.f02dd5917289dced.webp" alt="VS ਕੋਡ ਟੈਸਟ ਐਕਸਪਲੋਰਰ" width="800"/>

*VS ਕੋਡ ਟੈਸਟ ਐਕਸਪਲੋਰਰ ਦਿਖਾ ਰਿਹਾ ਹੈ ਟੈਸਟ ਟਰੀ ਨਾਲ ਸਾਰੇ ਜਾਵਾ ਟੈਸਟ ਕਲਾਸਾਂ ਅਤੇ ਵਿਅਕਤੀਗਤ ਟੈਸਟ ਮੈਥਡਸ*

**VS Code ਵਿੱਚ ਟੈਸਟ ਚਲਾਉਣ ਲਈ:**

1. ਐਕਟਿਵਿਟੀ ਬਾਰ ਵਿੱਚ ਬੀਕਰ ਆਈਕਨ 'ਤੇ ਕਲਿਕ ਕਰਕੇ ਟੈਸਟ ਐਕਸਪਲੋਰਰ ਖੋਲ੍ਹੋ
2. ਸਾਰੇ ਮੋਡੀਊਲ ਅਤੇ ਟੈਸਟ ਕਲਾਸਾਂ ਦੇਖਣ ਲਈ ਟੈਸਟ ਟਰੀ ਖੋਲ੍ਹੋ
3. ਕਿਸੇ ਵੀ ਟੈਸਟ ਦੇ ਨਾਲ ਖੇਡਣ ਵਾਲਾ ਬਟਨ ਦਬਾ ਕੇ ਅਲੱਗ-ਅਲੱਗ ਟੈਸਟ ਚਲਾਓ
4. "Run All Tests" 'ਤੇ ਕਲਿਕ ਕਰੋ ਤਾ ਕਿ ਪੂਰਾ ਸੂਟ ਚਲ ਜਾਵੇ
5. ਕਿਸੇ ਵੀ ਟੈਸਟ 'ਤੇ ਰਾਈਟ ਕਲਿਕ ਕਰਕੇ "Debug Test" ਚੁਣੋ ਤਾ ਕਿ ਬ੍ਰੇਕਪੋਇੰਟ ਸੈਟ ਕਰਕੇ ਕੋਡ ਵਿਚ ਅੱਗੇ ਵਧੋ

ਟੈਸਟ ਐਕਸਪਲੋਰਰ ਸਾਬਤ ਕਰਨ ਵਾਲੇ ਟੈਸਟਾਂ ਲਈ ਹਰਾ ਟਿੱਕ ਦਿਖਾਉਂਦਾ ਹੈ ਅਤੇ ਜਦੋਂ ਟੈਸਟ ਫੇਲ ਹੁੰਦੇ ਹਨ ਤਾਂ ਵਿਸਥਾਰਿਤ ਅਸਫਲਤਾ ਸੰਦੇਸ਼ ਪ੍ਰਦਾਨ ਕਰਦਾ ਹੈ।

## Testing Patterns

### Pattern 1: Testing Prompt Templates

The simplest pattern tests prompt templates without calling any AI model. You verify that variable substitution works correctly and prompts are formatted as expected.

<img src="../../../translated_images/pa/prompt-template-testing.b902758ddccc8dee.webp" alt="ਪ੍ਰੰਪਟ ਟੈਮਪਲੇਟ ਟੈਸਟਿੰਗ" width="800"/>

*ਪ੍ਰੰਪਟ ਟੈਮਪਲੇਟ ਟੈਸਟਿੰਗ ਦਿਖਾ ਰਹੀ ਹੈ ਵੈਰੀਏਬਲ ਬਦਲੀ ਦਾ ਪ੍ਰਕਿਰਿਆ: ਟੈਂਪਲੇਟ ਵਿੱਚ ਪਲੇਸਹੋਲਡਰ → ਮੂਲਿਆਂ ਦਾ ਲਾਗੂ ਕਰਨਾ → ਫਾਰਮੈਟ ਕੀਤਾ ਨਿਕਾਸ ਸੰਪਾਦਿਤ ਕੀਤਾ ਗਿਆ*

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

This test lives in `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**ਇਸ ਨੂੰ ਚਲਾਓ:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#ਟੈਸਟ ਪ੍ਰੰਪਟ ਟੈਮਪਲੇਟ ਫਾਰਮੈਟਿੰਗ
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#ਟੈਸਟ ਪ੍ਰਾਂਪਟ ਟੈਮਪਲੇਟ ਫਾਰਮੈਟਿੰਗ
```

### Pattern 2: Mocking Language Models

When testing conversation logic, use Mockito to create fake models that return predetermined responses. This makes tests fast, free, and deterministic.

<img src="../../../translated_images/pa/mock-vs-real.3b8b1f85bfe6845e.webp" alt="ਮੌਕ ਵਿਰੁੱਧ ਅਸਲੀ API ਮੁਕਾਬਲਾ" width="800"/>

*ਤੁਲਨਾ ਦਿਖਾਉਂਦੀ ਹੈ ਕਿ ਕਿਉਂ ਮੌਕ ਟੈਸਟਿੰਗ ਲਈ ਪਸੰਦ ਕੀਤੀ ਜਾਂਦੀ ਹੈ: ਇਹ ਤੇਜ਼, ਮੁਫਤ, ਨਿਰਣਾਇਕ ਹਨ ਅਤੇ ਕਿਸੇ ਵੀ API ਕੁੰਜੀ ਦੀ ਲੋੜ ਨਹੀਂ ਹੁੰਦੀ*

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
        assertThat(history).hasSize(6); // 3 ਉਪਭੋਗਤਾ + 3 AI ਸੁਨੇਹੇ
    }
}
```

This pattern appears in `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. The mock ensures consistent behavior so you can verify memory management works correctly.

### Pattern 3: Testing Conversation Isolation

Conversation memory must keep multiple users separate. This test verifies that conversations don't mix contexts.

<img src="../../../translated_images/pa/conversation-isolation.e00336cf8f7a3e3f.webp" alt="ਗੱਲਬਾਤ ਅਲੱਗਾਵ" width="800"/>

*ਗੱਲਬਾਤ ਅਲੱਗਾਵ ਦਿਖਾ ਰਿਹਾ ਹੈ ਵੱਖ-ਵੱਖ ਉਪਭੋਗਤਾਵਾਂ ਲਈ ਵੱਖਰੇ ਯਾਦਸਤੋਰ ਤਾਂ ਜੋ ਸੰਦੇਸ਼ਾਂ ਦੇ ਸੰਦਰਭ ਨਾ ਮਿਲਣ*

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

Each conversation maintains its own independent history. In production systems, this isolation is critical for multi-user applications.

### Pattern 4: Testing Tools Independently

Tools are functions the AI can call. Test them directly to ensure they work correctly regardless of AI decisions.

<img src="../../../translated_images/pa/tools-testing.3e1706817b0b3924.webp" alt="ਟੂਲਸ ਟੈਸਟਿੰਗ" width="800"/>

*ਟੂਲਸ ਦੀ ਸੁਤੰਤਰ ਟੈਸਟਿੰਗ ਦਿਖਾਈ ਜਾ ਰਹੀ ਹੈ ਜਿੱਥੇ ਮੌਕ ਟੂਲ ਚਲਾਇਆ ਜਾ ਰਿਹਾ ਹੈ ਬਿਨਾਂ AI ਕਾਲਾਂ ਦੇ ਤਾ ਕਿ ਕਾਰੋਬਾਰੀ ਤਰਕ ਸਹੀ ਢੰਗ ਨਾਲ ਕੰਮ ਕਰਦਾ ਰਹੇ*

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

These tests from `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` validate tool logic without AI involvement. The chaining example shows how one tool's output feeds into another's input.

### Pattern 5: In-Memory RAG Testing

RAG systems traditionally require vector databases and embedding services. The in-memory pattern lets you test the entire pipeline without external dependencies.

<img src="../../../translated_images/pa/rag-testing.ee7541b1e23934b1.webp" alt="ਇਨ-ਮੇਮੋਰੀ RAG ਟੈਸਟਿੰਗ" width="800"/>

*ਇਨ-ਮੇਮੋਰੀ RAG ਟੈਸਟਿੰਗ ਵਹੀ ਪ੍ਰਕਿਰਿਆ ਦਿਖਾ ਰਹੀ ਹੈ ਜਿਸ ਵਿੱਚ ਦਸਤਾਵੇਜ਼ ਦੀ ਪਾਰਸਿੰਗ, ਐਮਬੈੱਡਿੰਗ ਸਟੋਰੇਜ, ਅਤੇ ਸਮਾਨਤਾ ਖੋਜ ਹੁੰਦੀ ਹੈ ਬਿਨਾਂ ਕਿਸੇ ਡੇਟਾਬੇਸ ਦੀ ਲੋੜ ਦੇ*

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

This test from `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` creates a document in memory and verifies chunking and metadata handling.

### Pattern 6: MCP Integration Testing

The MCP module tests the Model Context Protocol integration using stdio transport. These tests verify that your application can spawn and communicate with MCP servers as subprocesses.

The tests in `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` validate MCP client behavior.

**ਇਹਨਾਂ ਨੂੰ ਚਲਾਓ:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Testing Philosophy

Test your code, not the AI. Your tests should validate the code you write by checking how prompts are constructed, how memory is managed, and how tools execute. AI responses vary and shouldn't be part of test assertions. Ask yourself whether your prompt template correctly substitutes variables, not whether the AI gives the right answer.

Use mocks for language models. They're external dependencies that are slow, expensive, and non-deterministic. Mocking makes tests fast with milliseconds instead of seconds, free with no API costs, and deterministic with the same result every time.

Keep tests independent. Each test should set up its own data, not rely on other tests, and clean up after itself. Tests should pass regardless of execution order.

Test edge cases beyond the happy path. Try empty inputs, very large inputs, special characters, invalid parameters, and boundary conditions. These often reveal bugs that normal usage doesn't expose.

Use descriptive names. Compare `shouldMaintainConversationHistoryAcrossMultipleMessages()` with `test1()`. The first tells you exactly what's being tested, making debugging failures much easier.

## Next Steps

Now that you understand the testing patterns, dive deeper into each module:

- **[00 - Quick Start](../00-quick-start/README.md)** - Start with prompt template basics
- **[01 - Introduction](../01-introduction/README.md)** - Learn conversation memory management
- **[02 - Prompt Engineering](../02-prompt-engineering/README.md)** - Master GPT-5.2 prompting patterns
- **[03 - RAG](../03-rag/README.md)** - Build retrieval-augmented generation systems
- **[04 - Tools](../04-tools/README.md)** - Implement function calling and tool chains
- **[05 - MCP](../05-mcp/README.md)** - Integrate Model Context Protocol

Each module's README provides detailed explanations of the concepts tested here.

---

**Navigation:** [← ਮੁੱਖ ਪੇਜ਼ ਤੇ ਵਾਪਸ](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ਡਿਸਕਲੇਮਰ**:  
ਇਹ ਦਸਤਾਵੇਜ਼ AI ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਅਨੁਵਾਦਿਤ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦੋਂ ਕਿ ਅਸੀਂ ਸਹੀਤਾ ਲਈ ਯਤਨਸ਼ੀਲ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਦਿਓ ਕਿ ਸਵੈਚालित ਅਨੁਵਾਦ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਣਸੁੱਚੀਆਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਆਪਣੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਪ੍ਰਧਾਨ ਸਰੋਤ ਵਜੋਂ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਆਵਸ਼੍ਯਕ ਜਾਣਕਾਰੀ ਲਈ, ਪੇਸ਼ੇਵਰ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫ਼ਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਇਸ ਅਨੁਵਾਦ ਦੀ ਵਰਤੋਂ ਤੋਂ ਉੱਪਜਣ ਵਾਲੇ ਕਿਸੇ ਵੀ ਗਲਤਫਹਿਮੀ ਜਾਂ ਭ੍ਰਮ ਲਈ ਅਸੀਂ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->