# LangChain4j ಅಪ್ಲಿಕೇಶನ್‌ಗಳನ್ನು ಪರೀಕ್ಷೆ ಮಾಡುವುದು

## ವಿಷಯದ ಟೇಬಲ್

- [ವಿಜ್ಞಾಪನೆ ಪ್ರಾರಂಭಿಸಿ](../../../docs)
- [ಪರೀಕ್ಷೆಗಳು ಏನು ಒಳಗೊಂಡಿವೆ](../../../docs)
- [ಪರೀಕ್ಷೆಗಳನ್ನು ನಡೆಸುವುದು](../../../docs)
- [VS ಕೋಡ್‌ನಲ್ಲಿ ಪರೀಕ್ಷೆ ನಡೆಸುವುದು](../../../docs)
- [ಪರೀಕ್ಷೆ ಮಾದರಿಗಳು](../../../docs)
- [ಪರೀಕ್ಷಾ ತತ್ತ್ವ](../../../docs)
- [ಮುಂದಿನ ಹೆಜ್ಜೆಗಳು](../../../docs)

ಈ ಮಾರ್ಗದರ್ಶಕಿ ನೀವು API ಕೀಗಳು ಅಥವಾ ಬಾಹ್ಯ ಸೇವೆಗಳ ಅಗತ್ಯವಿಲ್ಲದೆ AI ಅಪ್ಲಿಕೇಶನ್‌ಗಳನ್ನು ಪರೀಕ್ಷಿಸುವ ವಿಧಾನವನ್ನು ತೋರಿಸುವ ಪರೀಕ್ಷೆಗಳ ಮೂಲಕ ಹೋಗುತ್ತದೆ.

## Quick Start

ಒಂದೇ ಕಮಾಂಡ್ ಮೂಲಕ ಎಲ್ಲಾ ಪರೀಕ್ಷೆಗಳನ್ನು ನಡೆಸಿ:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/kn/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*ಎಲ್ಲಾ ಪರೀಕ್ಷೆಗಳು ಯಶಸ್ವಿಯಾಗಿ ಪಾಸಾಗಿರುವುದು ತೋರಿಸುವ ಯಶಸ್ವಿ ಪರೀಕ್ಷಾ ವರದಿ*

## ಪರಿಕ್ಷೆಗಳು ಏನು ಒಳಗೊಂಡಿವೆ

ಈ ಕೋರ್ಸ್ **ಯುನಿಟ್ ಟೆಸ್ಟ್‌ಗಳು** ಮೇಲೆ ಕೇಂದ್ರೀಕರಿಸುತ್ತದೆ, ಅವು ಸ್ಥಳೀಯವಾಗಿ ನಡಿಸುತ್ತದೆ. ಪ್ರತಿಯೊಂದು ಪರೀಕ್ಷೆ LangChain4j ಸಂಧರ್ಭದಲ್ಲಿ ವಿಶೇಷ ಸಂಜ್ಞೆಯನ್ನು ತೋರಿಸುತ್ತದೆ.

<img src="../../../translated_images/kn/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*ಪರೀಕ್ಷಾ ಪಿರಮಿಡ್ ಯುನಿಟ್ ಟೆಸ್ಟ್‌ಗಳು (ತ್ವರಿತ, ಪ್ರತ್ಯೇಕಿತ), ಇಂಟಿಗ್ರೇಷನ್ ಟೆಸ್ಟ್‌ಗಳು (ವಾಸ್ತವಿಕ ಘಟಕಗಳು), ಮತ್ತು ಎಂಡ್-ಟು-ಎಂಡ್ ಟೆಸ್ಟ್‌ಗಳ ನಡುವೆ ಸಮತೋಲನವನ್ನು ತೋರಿಸುತ್ತದೆ. ಈ ತರಬೆತಿಯಲ್ಲಿ ಯುನಿಟ್ ಟೆಸ್ಟಿಂಗ್ ಹೊಂದಿದೆ.*

| ಮೊಡ್ಯೂಲ್ | ಪರೀಕ್ಷೆಗಳು | ಕೇಂದ್ರೀಕರಣ | ಮುಖ್ಯ ಫೈಲುಗಳು |
|--------|-------|-------|-----------|
| **00 - Quick Start** | 6 | ಪ್ರಾಂಪ್ಟ್ ಟೆಂಪ್ಲೇಟುಗಳು ಮತ್ತು ವೇರಿಯಬಲ್ ಪರಿಭಾಷೆ | `SimpleQuickStartTest.java` |
| **01 - Introduction** | 8 | ಸಂಭಾಷಣಾ ಸ್ಮರಣೆ ಮತ್ತು ಸ್ಥಿತಿಗತ ಚಾಟ್ | `SimpleConversationTest.java` |
| **02 - Prompt Engineering** | 12 | GPT-5.2 ಮಾದರಿಗಳು, ಉತ್ಸಾಹದ ಮಟ್ಟಗಳು, ರಚಿಸಲಾದ ಔಟ್‌ಪುಟ್ | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | ಡಾಕ್ಯುಮೆಂಟ್ ಇಂಜೆಸ್ಟನ್, ಎಂಬೆಡಿಂಗ್ಸ್, ಸಮಾನತೆ ಶೋಧನೆ | `DocumentServiceTest.java` |
| **04 - Tools** | 12 | ಕಾರ್ಯರೂಪ ಕರೆ ಮತ್ತು ಉಪಕರಣ ಸರಣೀಕರಣ | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | ಮಾದರಿಯ ಕಾಂಟೆಕ್ಸ್ಟ್ ಪ್ರೋಟೋಕಾಲ್ stdio ಸಾರಿಗೆ ಮೂಲಕ | `SimpleMcpTest.java` |

## ಪರೀಕ್ಷೆಗಳನ್ನು ನಡೆಸುವುದು

**ರೂಟ್‌ನಿಂದ ಎಲ್ಲಾ ಪರೀಕ್ಷೆಗಳನ್ನು chạy ಮಾಡಿರಿ:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**ನಿರ್ದಿಷ್ಟ ಮೊಡ್ಯೂಲ್ಗೆ ಪರೀಕ್ಷೆಗಳನ್ನು chạy ಮಾಡಿರಿ:**

**Bash:**
```bash
cd 01-introduction && mvn test
# ಅಥವಾ ರೂಟ್‌ನಿಂದ
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# ಅಥವಾ ಮೂಲದಿಂದ
mvn --% test -pl 01-introduction
```

**ಒಂದು ಪರೀಕ್ಷಾ ಕ್ಲಾಸ್ನ್ನು chạy ಮಾಡಿರಿ:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**ನಿರ್ದಿಷ್ಟ ಪರೀಕ್ಷಾ ವಿಧಾನವನ್ನು chạy ಮಾಡಿರಿ:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#ಸಂವಾದ ಇತಿಹಾಸವನ್ನು ರಕ್ಷಿಸಬೇಕು
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#ಸಂಭಾಷಣೆಯ ಇತಿಹಾಸವನ್ನು ಕಾಯ್ದಿಡಬೇಕು
```

## VS ಕೋಡ್‌ನಲ್ಲಿ ಪರೀಕ್ಷೆಗಳನ್ನು ನಡೆಸುವುದು

ನೀವು Visual Studio Code ಬಳಸುತ್ತಿದ್ದರೆ, ಟೆಸ್ಟ್ ಎಕ್ಸ್ಪ್ಲೋರರ್ ಗ್ರಾಫಿಕಲ್ ಇಂಟರ್ಫೇಸ್ ಒದಗಿಸುತ್ತದೆ ಪರೀಕ್ಷೆಗಳನ್ನು chạy ಮತ್ತು ಡಿಬಗ್ಗಿಂಗ್ ಮಾಡಲು.

<img src="../../../translated_images/kn/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS ಕೋಡ್ ಟೆಸ್ಟ್ ಎಕ್ಸ್ಪ್ಲೋರರ್ ಎಲ್ಲಾ ಜಾವಾ ಟೆಸ್ಟ್ ಕ್ಲಾಸ್‌ಗಳು ಮತ್ತು ವೈಯಕ್ತಿಕ ಪರೀಕ್ಷಾ ವಿಧಾನಗಳ ಟೆಸ್ಟ್ ವೃಕ್ಷವನ್ನು ತೋರಿಸುತ್ತದೆ*

**VS ಕೋಡ್‌ನಲ್ಲಿ ಪರೀಕ್ಷೆಗಳನ್ನು chạy ಮಾಡಲು:**

1. ಕಾರ್ಯಗತಿಪಟ್ಟಿಯಲ್ಲಿ ಬುುಂಬು ಐಕಾನ್ ಕ್ಲಿಕ್ ಮಾಡಿ ಟೆಸ್ಟ್ ಎಕ್ಸ್ಪ್ಲೋರರ್ ತೆಗೆಯಿರಿ
2. ಎಲ್ಲಾ ಮೊಡ್ಯೂಲ್‌ಗಳು ಮತ್ತು ಟೆಸ್ಟ್ ಕ್ಲಾಸ್‌ಗಳನ್ನು ವಿಸ್ತರಿಸಿ ವೀಕ್ಷಿಸಿ
3. ಯಾವುದೇ ಪರೀಕ್ಷೆಯ ಪಕ್ಕದಲ್ಲಿ ಇರುವ ಪ್ಲೇ ಬಟನ್ ಕ್ಲಿಕ್ ಮಾಡಿ ಅವುಗಳನ್ನು ವೈಯಕ್ತಿಕವಾಗಿ ಚಾಲನೆ ಮಾಡಬಹುದು
4. "Run All Tests" ಕ್ಲಿಕ್ ಮಾಡಿ ಸಂಪೂರ್ಣ ಪರೀಕ್ಷಾ ಸಂಕಲನವನ್ನು ನಡೆಸಿ
5. ಯಾವುದೇ ಪರೀಕ್ಷೆ ಮೇಲೆ ರೈಟ್-ಕ್ಲಿಕ್ ಮಾಡಿ "Debug Test" ಆಯ್ಕೆ ಮಾಡಿ ಬ್ರೇಕ್‌ಪಾಯಿಂಟ್‌ಗಳನ್ನು ಹೊಂದಿಸಿ, ಕೋಡ್ ಮೂಲಕ ಹೆಜ್ಜೆ ಹಾಕಿ

ಟೆಸ್ಟ್ ಎಕ್ಸ್ಪ್ಲೋರರ್ ಎದುರಿಸಿದ ಮತ್ತು ಪಾಸಾದ ಪರೀಕ್ಷೆಗಳಿಗೆ ಹಸಿರು ತಿಕ್ಕೆಗಳನ್ನು ತೋರಿಸುತ್ತದೆ ಮತ್ತು ವಿಫಲವಾದಾಗ ವಿವರಣೆಾತ್ಮಕ ದೋಷ ಸಂದೇಶಗಳು ಒದಗಿಸುತ್ತದೆ.

## ಪರೀಕ್ಷಾ ಮಾದರಿಗಳು

### ಮಾದರಿ 1: ಪ್ರಾಂಪ್ಟ್ ಟೆಂಪ್ಲೇಟ್‌ಗಳನ್ನು ಪರೀಕ್ಷಿಸುವುದು

ಲರಿಶ್ಬ模式的模。</summary>ೆಪ್ಲೇಟ್‌ಗಳು ಯಾವುದೇ AI ಮಾದರಿಯನ್ನು ಕರೆ ಮಾಡದೆ ಪರೀಕ್ಷಿಸಬಹುದು. ನೀವು ವೇರಿಯಬಲ್ ಬದಲಾವಣೆ ಸರಿಯಾಗಿ ಕಾರ್ಯನಿರ್ವಹಿಸುತ್ತಿರಲೆಯೇ ಮತ್ತು ಪ್ರಾಂಪ್ಟ್‌ಗಳು ನಿರೀಕ್ಷಿತವಾಗಿ ರೂಪಿಸಲ್ಪಟ್ಟಿವೆ ಎಂದು ಪರಿಶೀಲಿಸುತ್ತೀರಿ.

<img src="../../../translated_images/kn/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*ಪ್ರಾಂಪ್ಟ್ ಟೆಂಪ್ಲೇಟ್‌ಗಳನ್ನು ಪರೀಕ್ಷಿಸುವುದು ವೇರಿಯಬಲ್ ಪರಿಭಾಷೆ ಹರಿವು ತೋರಿಸುತ್ತದೆ: ಪ್ಲೇಸ್‌ಹೋಲ್ಡರ್‌ಗಳೊಂದಿಗೆ ಟೆಂಪ್ಲೇಟ್ → ಮೌಲ್ಯಗಳು ಅನ್ವಯಿಸಲಾಗುತ್ತದೆ → ಸ್ವರೂಪಿಸಲಾಗಿದೆ ಔಟ್‌ಪುಟ್ ಪರಿಶೀಲಿಸಲಾಗಿದೆ*

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

ಈ ಪರೀಕ್ಷೆ `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java` ನಲ್ಲಿ ಇದೆ.

**ನಡೆಸಿರಿ:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#ಟೆಸ್ಟ್ ಪ್ರಾಂಪ್ಟ್ ಟೆಂಪ್ಲೇಟ್ ಫಾರ್ಮ್ಯಾಟಿಂಗ್
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#ಟೆಸ್ಟ್ ಪ್ರಾಂಪ್ಟ್ ಟೆಂಪ್ಲೇಟು ಫಾರ್ಮ್ಯಾಟ್ ಮಾಡುವುದು
```

### ಮಾದರಿ 2: ಭಾಷಾ ಮಾದರಿಗಳನ್ನು ಮೋಕಿಂಗ್ ಮಾಡುವುದು

 ಸಂಭಾಷಣಾ ತರ್ಕವನ್ನು ಪರೀಕ್ಷಿಸುವಾಗ, ಮುಂಚಿತವಾಗಿ ನಿರ್ದಿಷ್ಟ ಉತ್ತರಗಳನ್ನು ನೀಡುವ ನಕಲಿ ಮಾದರಿಗಳನ್ನು ನಿರ್ಮಿಸಲು Mockito ಉಪಯೋಗಿಸಿ. ಇದು ಪರೀಕ್ಷೆಗಳನ್ನು ತ್ವರಿತ, ಉಚಿತ ಮತ್ತು ನಿರ್ಣಾಯಕವಾಗಿರಿಸುತ್ತದೆ.

<img src="../../../translated_images/kn/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*ಪರೀಕ್ಷೆಗಾಗಿ ಮೋಕ್‌ಗಳು ಏಕೆ ಮೆಚ್ಚುಗೆಯಾದವು ಎಂಬುದನ್ನು ತೋರಿಸುವ ಹೋಲಿಕೆ: ಅವು ತ್ವರಿತ, ಉಚಿತ, ನಿರ್ಣಾಯಕ ಮತ್ತು API ಕೀಗಳನ್ನು ಅಗತ್ಯವಿಲ್ಲದೆ ಇರುತ್ತವೆ*

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
        assertThat(history).hasSize(6); // 3 ಬಳಕೆದಾರ + 3 AI ಸಂದೇಶಗಳು
    }
}
```

ಈ ಮಾದರಿ `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` ನಲ್ಲಿ ಕಾಣಿಸುತ್ತದೆ. ಮೋಕ್ ತತ್ಕಾಲಿಕ ನಡವಳಿಕೆಯನ್ನು ಖಚಿತಪಡಿಸುತ್ತದೆ ಆದ್ದರಿಂದ ನೀವು ಸ್ಮರಣೆ ನಿರ್ವಹಣೆ ಸರಿಯಾಗಿವೆ ಎಂದು ಪರಿಶೀಲಿಸಬಹುದು.

### ಮಾದರಿ 3: ಸಂಭಾಷಣೆಯ ವಿಭಜನೆ ಪರೀಕ್ಷೆ

ಸಂಭಾಷಣಾ ಸ್ಮರಣೆ ಬಹು ಬಳಕೆದಾರರನ್ನು ಪ್ರತ್ಯೇಕವಾಗಿ ಇರಿಸಬೇಕಾಗುತ್ತದೆ. ಈ ಪರೀಕ್ಷೆ ಸಂಭಾಷಣೆಗಳು ಸಂದೇಶಗಳನ್ನು ಮಿಕ್ಸ್ ಮಾಡುತ್ತಿವೆಯೇ ಇಲ್ಲವೇ ಎಂಬುದನ್ನು ಪರಿಶೀಲಿಸುತ್ತದೆ.

<img src="../../../translated_images/kn/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*ಸಂಭಾಷಣೆಯ ವಿಭಜನೆ ತೋರಿಸುವುದು ವಿಭಿನ್ನ ಬಳಕೆದಾರರಿಗಾಗಿ ಪ್ರತ್ಯೇಕ ಸ್ಮರಣೆ ಸಂಗ್ರಹಣೆಗಳಿರುವುದು ಮತ್ತು ಸಂದೇಶದ ಮಿಶ್ರಣವನ್ನು ತಡೆಗಟ್ಟುವುದು*

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

ಪ್ರತ್ಯೇಕ ಸಂಸ್ಥೆಯನ್ನು ಪ್ರತಿಯೊಂದು ಸಂಭಾಷಣೆಯು ತನ್ನ ಸ್ವಂತ ಇತಿಹಾಸವನ್ನು ಕಾಪಾಡುತ್ತದೆ. ಉತ್ಪಾದನಾ ವ್ಯವಸ್ಥೆಗಳಲ್ಲಿ, ಈ ವಿಭಜನೆ ಬಹು ಬಳಕೆದಾರ ಅಪ್ಲಿಕೇಶನ್‌ಗಳಿಗೆ ಅತ್ಯಗತ್ಯವಾಗಿದೆ.

### ಮಾದರಿ 4: ಉಪಕರಣಗಳನ್ನು ಸ್ವತಂತ್ರವಾಗಿ ಪರೀಕ್ಷಿಸುವುದು

ಉಪಕರಣಗಳು AI ಕರೆಮಾಡಬಹುದಾದ ಕಾರ್ಯಗಳು. AI ನಿರ್ಧಾರಗಳನ್ನು ಲೆಕ್ಕಿಸದೆ ನೇರವಾಗಿ ಅವುಗಳನ್ನು ಪರೀಕ್ಷಿಸಿ ಅವು ಸರಿಯಾಗಿವೆ ಎಂಬುದನ್ನು ಖಚಿತಪಡಿಸಿ.

<img src="../../../translated_images/kn/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*ಉಪಕರಣಗಳನ್ನು ಸ್ವತಂತ್ರವಾಗಿ ಪರೀಕ್ಷಿಸುವುದು AI ಕರೆ ಇಲ್ಲದೆ ಮೋಕ್ ಉಪಕರಣ ಕಾರ್ಯಾಚರಣೆ ತೋರಿಸುತ್ತದೆ ವ್ಯವಹಾರ ಲಾಜಿಕ್ ಪರಿಶೀಲಿಸಲು*

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

`04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` ಇಂದ ಈ ಪರೀಕ್ಷೆಗಳು AI ಪಾಲ್ಗೊಳ್ಳದೆ ಉಪಕರಣದ ಲಾಜಿಕ್ ಪರಿಶೀಲಿಸುತ್ತವೆ. ಸರಣೀಕರಣ ಉದಾಹರಣೆ ಒಂದು ಉಪಕರಣದ ಔಟ್‌ಪುಟ್ ಇನ್ನೊಂದು ಉಪಕರಣದ ಇನ್‌ಪುಟ್‌ಗೆ ಹೇಗೆ ಕಾಣಿಸುತ್ತದೆ ಎಂದು ತೋರಿಸುತ್ತದೆ.

### ಮಾದರಿ 5: ಮೆಮೊರಿ ಆಧಾರಿತ RAG ಪರೀಕ್ಷೆ

RAG ವ್ಯವಸ್ಥೆಗಳಲ್ಲಿ ಸಾಮಾನ್ಯವಾಗಿ ವೆಕ್ಟರ್ ಡೇಟಾಬೇಸ್ ಮತ್ತು ಎಂಬೆಡಿಂಗ್ ಸರ್ವೀಸುಗಳು ಅಗತ್ಯವಿರುತ್ತವೆ. ಮೆಮೊರಿ ಆಧಾರಿತ ಮಾದರಿ ಇಲ್ಲೇ ಎಲ್ಲಾ ಪ್ರಕ್ರಿಯೆಯನ್ನು ಬಾಹ್ಯ ಅವಲಂಬನೆಯೇ ಇಲ್ಲದೆ ಪರೀಕ್ಷಿಸಲು ಅನುಮತಿಸುತ್ತದೆ.

<img src="../../../translated_images/kn/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*ಮೆಮೊರಿ ಆಧಾರಿತ RAG ಪರೀಕ್ಷಾ ಕಾರ್ಯವಿಧಾನವನ್ನು ತೋರಿಸುವುದು ಡಾಕ್ಯುಮೆಂಟ್ ಪಾರ್ಸಿಂಗ್, ಎಂಬೆಡಿಂಗ್ ಸಂಗ್ರಹಣೆ ಮತ್ತು ಸಮಾನತೆ ಶೋಧನೆ ಡೇಟಾಬೇಸ್ ಅಗತ್ಯವಿಲ್ಲದೆ*

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

ಈ ಪರೀಕ್ಷೆ `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` ನಿಂದ ಡಾಕ್ಯುಮೆಂಟ್ ಸೃಷ್ಟಿಸಿ ಚಂಕಿಂಗ್ ಮತ್ತು ಮೆಟಾಡೇಟಾ ಸಂಸ್ಕರಣೆಯನ್ನು ಪರಿಶೀಲಿಸುತ್ತದೆ.

### ಮಾದರಿ 6: MCP ಏಕತೆ ಪರೀಕ್ಷೆ

MCP ಮೊಡ್ಯೂಲ್ ಮಾದರಿಯ ಕಾಂಟೆಕ್ಸ್ پروಟೋಕಾಲ್ stdio ಸಾರಿಗೆಯನ್ನು ಉಪಯೋಗಿಸುವ ಏಕತೆ ಪರೀಕ್ಷೆಯನ್ನು ನಡೆಸುತ್ತದೆ. ಈ ಪರೀಕ್ಷೆಗಳು ನಿಮ್ಮ ಅಪ್ಲಿಕೇಶನ್ MCP ಸರ್ವರ್‌ಗಳನ್ನು ఉపಪ್ರಕ್ರಿಯೆಗಳಾಗಿ ಸ್ಟಾರ್ಟ್ ಮಾಡಿಸಿ ಸಂವಹನ ನಿಯಂತ್ರಣ კ್امತಪಡಿಸುತ್ತದೆ.

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` ನಲ್ಲಿ ಈ ಪರೀಕ್ಷೆಗಳು MCP ಕ್ಲೈಂಟ್ ನಡವಳಿಕೆಯನ್ನು ಪರಿಶೀಲಿಸುತ್ತವೆ.

**ನಡೆಸಿರಿ:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## ಪರೀಕ್ಷಾ ತತ್ತ್ವ

ನೀವು AI ಅನ್ನು ಪರೀಕ್ಷಿಸಬೇಡಿ, ನಿಮ್ಮ ಕೋಡ್ ಅನ್ನು ಪರೀಕ್ಷಿಸಿ. ನಿಮ್ಮ ಪರೀಕ್ಷೆಗಳು ನೀವು ಬರೆಯುವ ಕೋಡ್ ಅನ್ನು ಪರಿಶೀಲಿಸಲು ಇರಬೇಕು, ಉದಾ: ಪ್ರಾಂಪ್ಟ್‌ಗಳನ್ನು ಹೇಗೆ ರಚಿಸಲಾಗಿದೆ, ಸ್ಮರಣೆ ಹೇಗೆ ನಿರ್ವಹಿಸಲಾಗಿದೆ, ಮತ್ತು ಉಪಕರಣಗಳು ಹೇಗೆ ಕಾರ್ಯಗರುತ್ತವೆ ಎಂಬುದನ್ನು ಪರೀಕ್ಷಿಸುವ ಮೂಲಕ. AI ಪ್ರತಿಕ್ರಿಯೆಗಳು ಬದಲಾಗಬಹುದು ಮತ್ತು ಅವು ಪರೀಕ್ಷಾ ಖಚಿತಪಡಿಸುವಿಕೆಯ ಭಾಗವಾಗಿರಬಾರದು. ನಿಮ್ಮ ಪ್ರಾಂಪ್ಟ್ ಟೆಂಪ್ಲೇಟ್ ವೇರಿಯಬಲ್ಸ್ ಸರಿಯಾಗಿ ಬದಲಾಯಿಸುತ್ತಿದೆಯೇ ಎಂದು ಪರಿಶೀಲಿಸಿ, AI ಸರಿ ಉತ್ತರ ನೀಡುತ್ತದೆಯೇ ಎಂದು ಅಲ್ಲ.

ಭಾಷಾ ಮಾದರಿಗಾಗಿ ಮೋಕ್‌ಗಳನ್ನು ಉಪಯೋಗಿಸಿ. ಅವು ಹಿಂದಿರುಗಿದಿರುವ ಬಾಹ್ಯ ಅವಲಂಬನೆಗಳು ಕುಸಿತ ಮತ್ತು ದೀರ್ಘ ಸಮಯ ಹಿಡಿದಿರುತ್ತವೆ. ಮೋಕ್ ಮಾಡುವುದು ಪರೀಕ್ಷೆಗಳನ್ನು ಸೆಕೆಂಡುಗಳ ಬದಲಿಗೆ ಮಿಲಿಸೆಕೆಂಡುಗಳಲ್ಲಿ ತ್ವರಿತಗೊಳಿಸುತ್ತದೆ, ಉಚಿತವಾಗಿರುತ್ತದೆ ಮತ್ತು ಪ್ರತಿವេលೆಯಲ್ಲಿಯೂ ಇದೇ ಫಲಿತಾಂಶ ನೀಡುತ್ತದೆ.

ಪರೀಕ್ಷೆಗಳನ್ನು ಸ್ವತಂತ್ರವಾಗಿರಿಸಿ. ಪ್ರತಿಯೊಂದು ಪರೀಕ್ಷೆಯು ಸ್ವಂತ ಡೇಟಾವನ್ನು ಹೊಂದಿರಬೇಕು, ಇತರ ಪರೀಕ್ಷೆಗಳನ್ನು ಅವಲಂಬಿಸಬಾರದು ಮತ್ತು ಸ್ವತಃ ಸ್ವಚ್ಛಗೊಳಿಸಬೇಕು. ಪರೀಕ್ಷೆಗಳು ಕಾರ್ಯಗತಿಗೊಳಿಸುವ ಸರಣಿಯಿಂದ ಸ್ವತಂತ್ರವಾಗಿ ಪಾಸಾಗಬೇಕು.

ಸಂತೋಷದ ಮಾರ್ಗವನ್ನು ಮೀರಿ ಎಡ್ಜ್ ಕೇಸ್‌ಗಳನ್ನು ಪರೀಕ್ಷಿಸಿ. ಖಾಲಿ ಇನ್ಪುಟ್‌ಗಳು, ಅತ್ಯಂತ ದೊಡ್ಡ ಇನ್ಪುಟ್‌ಗಳು, ವಿಶೇಷ ಅಕ್ಷರಗಳು, ಅಮಾನ್ಯ ನಿಯಮಗಳು ಮತ್ತು ಗಡಿಬಿಡಿ ಪರಿಸ್ಥಿತಿಗಳನ್ನು ಪ್ರಯತ್ನಿಸಿ. ಇವು ಸಾಮಾನ್ಯ ಬಳಕೆ ಮುಚ್ಛಿದ ತಪ್ಪುಗಳನ್ನು ಬಹುಮಾನವಾಗಿ ಪ್ರಗಟಿಸಬಹುದು.

ವಿವರಣೆ ವಿಷಯಕ ಹೆಸರುಗಳನ್ನು ಉಪಯೋಗಿಸಿ. `shouldMaintainConversationHistoryAcrossMultipleMessages()` ಅನ್ನು `test1()` ಗೆ ಹೋಲಿಸಿ. ಮೊದಲನೆಯದು ನಿಖರವಾಗಿ ಏನು ಪರೀಕ್ಷಿಸಲಾಗುತ್ತಿದೆ ಎಂಬುದನ್ನು ಹೇಳುತ್ತದೆ, ದೋಷ ಪತ್ತೆمال ವಿವರಿಸಲು ಸುಲಭ.

## ಮುಂದಿನ ಹೆಜ್ಜೆಗಳು

ಈಗ ನೀವು ಪರೀಕ್ಷಾ ಮಾದರಿಗಳನ್ನು ಅರ್ಥಮಾಡಿಕೊಂಡಿದ್ದೀರಿ, ಪ್ರತಿ ಮೊಡ್ಯೂಲ್ deeperವಾಗಿ ಎಣೆಯಿರಿ:

- **[00 - Quick Start](../00-quick-start/README.md)** - ಪ್ರಾಂಪ್ಟ್ ಟೆಂಪ್ಲೇಟ್ ಮೂಲಭೂತಗಳನ್ನು ಪ್ರಾರಂಭಿಸಿ
- **[01 - Introduction](../01-introduction/README.md)** - ಸಂಭಾಷಣಾ ಸ್ಮರಣೆ ನಿರ್ವಹಣೆಯನ್ನು ಕಲಿಯಿರಿ
- **[02 - Prompt Engineering](../02-prompt-engineering/README.md)** - GPT-5.2 ಪ್ರಾಂಪ್ಟ್ ಮಾದರಿಗಳನ್ನು ಮಾಸ್ಟರ್ ಮಾಡಿ
- **[03 - RAG](../03-rag/README.md)** - ನಿರೂಪಣ-ಮೆಚ್ಚುಗೆಯ ಉತ್ಪಾದನೆ ವ್ಯವಸ್ಥೆಗಳನ್ನು ನಿರ್ಮಿಸಿ
- **[04 - Tools](../04-tools/README.md)** - ಕಾರ್ಯರೂಪ ಕರೆ ಮತ್ತು ಉಪಕರಣ ಸರಣಿಗಳನ್ನು ಜಾರಿಗೆ ಹಾಕಿ
- **[05 - MCP](../05-mcp/README.md)** - ಮಾದರಿಯ ಕಾಂಟೆಕ್ಸ್ಟ್ ಪ್ರೋಟೋಕಾಲ್ ಅನ್ನು ಏಕೀಕೃತಗೊಳಿಸಿ

ಪ್ರತಿ ಮೊಡ್ಯೂಲ್‌ನ README ಇಲ್ಲಿ ಪರೀಕ್ಷಿಸಲ್ಪಟ್ಟ ಸಂಜ್ಞೆಗಳ ವಿಶದಿ ವಿವರಗಳನ್ನು ಒದಗಿಸುತ್ತದೆ.

---

**ಸಂಚರಣೆ:** [← ಮುಖ್ಯಕ್ಕೆ ಹಿಂದಿರುಗಿ](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**:
ಈ ದಾಖಲೆ AI ಅನುವಾದ ಸೇವೆ [Co-op Translator](https://github.com/Azure/co-op-translator) ಬಳಸಿ ಅನುವಾದಿಸಲಾಗಿದೆ. ನಾವು ನಿಖರತೆಯಿಗಾಗಿ ಪ್ರಯತ್ನಿಸುವಾಗ, ಸ್ವಯಂಚಾಲಿತ ಅನುವಾದಗಳಲ್ಲಿ ದೋಷಗಳು ಅಥವಾ ಅಸತ್ಯತೆಗಳಿರಬಹುದು ಎಂಬುದನ್ನು ಗಮನದಲ್ಲಿರಿಸಿ. ಮೂಲ ಭಾಷೆಯಲ್ಲಿನ ಡಾಕ್ಯುಮೆಂಟ್ ಅನ್ನು ಆಧಾರಭೂತ ಮೂಲವಾಗಿ ಪರಿಗಣಿಸಬೇಕು. ಪ್ರಮುಖ ಮಾಹಿತಿಗಾಗಿ, ವೃತ್ತಿಪರ ಮಾನವ ಅನುವಾದವನ್ನು ಶಿಫಾರಸು ಮಾಡಲಾಗುತ್ತದೆ. ಈ ಅನುವಾದವನ್ನು ಬಳಸದಾಗ ಉಂಟಾಗುವ ಯಾವುದೇ ತಪ್ಪು ಕಲ್ಪನೆಗಳು ಅಥವಾ ಅನ್ವಯಾತ್ಮಕಗಳಿಗಾಗಿ ನಾವು ಜವಾಬ್ದಾರಿಯಲ್ಲ.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->