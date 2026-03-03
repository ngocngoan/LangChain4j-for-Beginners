# LangChain4j ಅಪ್ಲಿಕೇಶನ್‌ಗಳನ್ನು ಪರೀಕ್ಷಿಸುವುದು

## ವಿಷಯಗಳ ಪಟ್ಟಿ

- [ತ್ವರಿತ ಪ್ರಾರಂಭ](../../../docs)
- [ಪರೀಕ್ಷೆಗಳು ಏನು ಒಳಗೊಂಡಿವೆ](../../../docs)
- [ಪರೀಕ್ಷೆಗಳನ್ನು ನಡೆಸುವುದು](../../../docs)
- [VS ಕೋಡ್‌ನಲ್ಲಿ ಪರೀಕ್ಷೆಗಳನ್ನು ನಡೆಸುವುದು](../../../docs)
- [ಪರೀಕ್ಷಾ ಉಲ್ಲೇಖಗಳು](../../../docs)
- [ಪರೀಕ್ಷಾ ತತ್ತ್ವಶಾಸ್ತ್ರ](../../../docs)
- [ಮುಂದಿನ ಹಂತಗಳು](../../../docs)

ಈ ಮಾರ್ಗದರ್ಶಿ API ಕೀಗಳು ಅಥವಾ ಹೊರಗಿನ ಸೇವೆಗಳ ಅಗತ್ಯವಿಲ್ಲದೆ AI ಅಪ್ಲಿಕೇಶನ್‌ಗಳನ್ನು ಹೇಗೆ ಪರೀಕ್ಷಿಸಲು ಸಾಧ್ಯವೆಂಬುದನ್ನು ತೋರಿಸುವ ಪರೀಕ್ಷೆಗಳ ಮೂಲಕ ನಿಮಗೆ ಮಾರ್ಗದರ್ಶನ ಮಾಡುತ್ತದೆ.

## ತ್ವರಿತ ಪ್ರಾರಂಭ

ಒಂದು ಆಜ್ಞೆಯೊಂದಿಗೆ ಎಲ್ಲಾ ಪರೀಕ್ಷೆಗಳನ್ನು ರನ್ ಮಾಡಿ:

**ಬ್ಯಾಶ್:**
```bash
mvn test
```

**ಪವರ್‌ಶೆಲ್:**
```powershell
mvn --% test
```

ಎಲ್ಲಾ ಪರೀಕ್ಷೆಗಳು ಯಶಸ್ವಿಯಾಗಿ ನಡೆದರೆ, ಕೆಳಗಿನ ಸ್ಕ್ರೀನ್ಶಾಟ್‌ನಂತೆ ಔಟ್‌ಪುಟ್ ಕಾಣಬೇಕು — ಯಾವುದೇ ವಿಫಲತೆಗಳಿಲ್ಲದೆ ಪರೀಕ್ಷೆಗಳು ನಡೆಯುತ್ತವೆ.

<img src="../../../translated_images/kn/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*ಯಶಸ್ವಿಯಾದ ಪರೀಕ್ಷೆಗಳ ನಿರ್ವಹಣೆ ಸಕಲ ಪರೀಕ್ಷೆಗಳು ಯಾವುದೇ ದೋಷಗಳಿಲ್ಲದೆ ಜಯವಂತವಾಗಿದೆ ಎಂಬುದನ್ನು ತೋರಿಸುತ್ತದೆ*

## ಪರೀಕ್ಷೆಗಳು ಏನು ಒಳಗೊಂಡಿವೆ

ಈ ಕೋರ್ಸ್ ಸ್ಥಳೀಯವಾಗಿ ನಡೆಯುವ **ಒಕ್ಕಣೆ ಪರೀಕ್ಷೆಗಳನ್ನು** (unit tests) ಗಮನಿಸುತ್ತದೆ. ಪ್ರತಿ ಪರೀಕ್ಷೆ ಒಂದು ನಿರ್ದಿಷ್ಟ LangChain4j ತತ್ವವನ್ನು ವಿಭಿನ್ನವಾಗಿ ತೋರಿಸುತ್ತದೆ. ಕೆಳಗಿನ ಪರೀಕ್ಷಾ ಪಿರಮಿಡುವು ಒಕ್ಕಣೆ ಪರೀಕ್ಷೆಗಳು ಎಲ್ಲಿ ಹೊಂದಿಕೊಳ್ಳುತ್ತವೆ ಎಂಬುದನ್ನು ತೋರುತ್ತದೆ — ಇವು ವೇಗವಾದ, ನಂಬಬಹುದಾದ ಆಧಾರವನ್ನು ರೂಪಿಸಿ, ನಿಮ್ಮ ಇತರೆ ಪರೀಕ್ಷಾ ತಂತ್ರಗಳನ್ನು ನಿರ್ಮಾಪಕವಾಗಿವೆ.

<img src="../../../translated_images/kn/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*ಒಕ್ಕಣೆ ಪರೀಕ್ಷೆಗಳು (ವೇಗವಾದ, ವಿಭಜಿತ), ಸಂಯೋಜನೆ ಪರೀಕ್ಷೆಗಳು (ನಿಜವಾದ ಘಟಕಗಳು), ಮತ್ತು ಅಂತ್ಯ-देखि-ಅಂತ್ಯ ಪರೀಕ್ಷೆಗಳ ನಡುವಿನ ಸಮತೋಲನವನ್ನು ತೋರುವ ಪರೀಕ್ಷಾ ಪಿರಮಿಡು. ಈ ತರಬೇತಿ ಒಕ್ಕಣೆ ಪರೀಕ್ಷೆಯನ್ನು ಒಳಗೊಂಡಿದೆ.*

| ಮೊಡ್ಯೂಲ್ | ಪರೀಕ್ಷೆಗಳು | ಗಮನ | ಮುಖ್ಯ ಫೈಲ್‌ಗಳು |
|--------|-------|-------|-----------|
| **00 - ತ್ವರಿತ ಪ್ರಾರಂಭ** | 6 | ಪ್ರಾಂಪ್ಟ್ ಟೆಂಪ್ಲೇಟುಗಳು ಮತ್ತು ಬದಲಾವಣಾ ಪರಿವರ್ತನೆ | `SimpleQuickStartTest.java` |
| **01 - ಪರಿಚಯ** | 8 | ಸಂವಾದ ಸ್ಮೃತಿ ಮತ್ತು ಸ್ಥಿತಿ ಸಂವಾದ | `SimpleConversationTest.java` |
| **02 - ಪ್ರಾಂಪ್ಟ್ ಎಂಜಿನಿಯರಿಂಗ್** | 12 | GPT-5.2 ಮಾದರಿಗಳು, ಆಸಕ್ತಿರ್ದೇಶಕ ಮಟ್ಟಗಳು, ವಿನ್ಯಾಸಗೊಳಿಸಿದ ಔಟ್‌ಪುಟ್ | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | ಡಾಕ್ಯುಮೆಂಟ್ ಒಳಗೊಂಡಿಕೆ, ಎಂಬೆಡ್ಡಿಂಗ್ಸ್, ಸಾದೃಶ್ಯ ಹುಡುಕಾಟ | `DocumentServiceTest.java` |
| **04 - ಉಪಕರಣಗಳು** | 12 | ಕಾರ್ಯ ಕರೆ ಮತ್ತು ಉಪಕರಣ ಸರಪಳಿ | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol ಜೊತೆಗೆ Stdio ಸಾರಿಗೆ | `SimpleMcpTest.java` |

## ಪರೀಕ್ಷೆಗಳನ್ನು ನಡೆಸುವುದು

**ಮೂಲದಿಂದ ಎಲ್ಲಾ ಪರೀಕ್ಷೆಗಳನ್ನು ರನ್ ಮಾಡು:**

**ಬ್ಯಾಶ್:**
```bash
mvn test
```

**ಪವರ್‌ಶೆಲ್:**
```powershell
mvn --% test
```

**ನಿರ್ದಿಷ್ಟ ಮೊಡ್ಯೂಲ್‌ಗೆ ಪರೀಕ್ಷೆಗಳನ್ನು ರನ್ ಮಾಡು:**

**ಬ್ಯಾಶ್:**
```bash
cd 01-introduction && mvn test
# ಅಥವಾ ಮೂಲದಿಂದ
mvn test -pl 01-introduction
```

**ಪವರ್‌ಶೆಲ್:**
```powershell
cd 01-introduction; mvn --% test
# ಅಥವಾ ರೂಟ್‌ನಿಂದ
mvn --% test -pl 01-introduction
```

**ಒಂದು ಪರೀಕ್ಷಾ ಕ್ಲಾಸ್ ರನ್ ಮಾಡು:**

**ಬ್ಯಾಶ್:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**ಪವರ್‌ಶೆಲ್:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**ನಿರ್ದಿಷ್ಟ ಪರೀಕ್ಷಾ ವಿಧಾನ ರನ್ ಮಾಡು:**

**ಬ್ಯಾಶ್:**
```bash
mvn test -Dtest=SimpleConversationTest#ಸಂವowaćನ ಇತಿಹಾಸವನ್ನು ಕಾಪಾಡಬೇಕು
```

**ಪವರ್‌ಶೆಲ್:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#ಸಂಭಾಷಣೆ ಇತಿಹಾಸವನ್ನು ಕಾಪಾಡಿಕೊಳ್ಳಬೇಕು
```

## VS ಕೋಡ್‌ನಲ್ಲಿ ಪರೀಕ್ಷೆಗಳನ್ನು ನಡೆಸುವುದು

ನೀವು Visual Studio Code ಬಳಸುತ್ತಿದ್ದರೆ, Test Explorer ಗ್ರಾಫಿಕಲ್ ಇಂಟರ್ಫೇಸ್‌ನ್ನು ಪರೀಕ್ಷೆಗಳು ರನ್ ಮತ್ತು ಡಿಬಗ್ ಮಾಡಲು ನೀಡುತ್ತದೆ.

<img src="../../../translated_images/kn/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS ಕೋಡ್ ಟೆಸ್ಟ್ ಎಕ್ಸ್‌ಪ್ಲೋರರ್ ಎಲ್ಲಾ ಜಾವಾ ಪರೀಕ್ಷಾ ಕ್ಲಾಸ್‌ಗಳು ಮತ್ತು ವೈಯಕ್ತಿಕ ಪರೀಕ್ಷಾ ವಿಧಾನಗಳೊಂದಿಗೆ ಪರೀಕ್ಷಾ ಮರವನ್ನು ತೋರುತ್ತದೆ*

**VS ಕೋಡ್‌ನಲ್ಲಿ ಪರೀಕ್ಷೆಗಳನ್ನು ನಡೆಸಲು:**

1. ಚಟುವಟಿಕೆ ಬಾರಿನಲ್ಲಿ ಇರುವ ಬಿಕರ್ ಐಕಾನ್ ಮೇಲೆ ಕ್ಲಿಕ್ ಮಾಡಿ Test Explorer ತೆರೆಯಿರಿ
2. ಎಲ್ಲಾ ಮೊಡ್ಯೂಲ್‌ಗಳು ಮತ್ತು ಪರೀಕ್ಷಾ ಕ್ಲಾಸ್‌ಗಳನ್ನು ನೋಡಲು ಪರೀಕ್ಷಾ ಮರವನ್ನು ವಿಸ್ತರಿಸಿ
3. ಯಾವುದೇ ಪರೀಕ್ಷೆಯನ್ನು ವೈಯಕ್ತಿಕವಾಗಿ ರನ್ ಮಾಡಲು ಅದರ ಪಕ್ಕದ ಪ್ಲೇ ಬಟನ್ ಕ್ಲಿಕ್ ಮಾಡಿ
4. ಪೂರ್ಣ ಸьют್ ಅನ್ನು ರನ್ ಮಾಡಲು "Run All Tests" ಕ್ಲಿಕ್ ಮಾಡಿ
5. ಯಾವುದೇ ಪರೀಕ್ಷೆಯನ್ನು ರೈಟ್ ಕ್ಲಿಕ್ ಮಾಡಿ "Debug Test" ಆಯ್ಕೆ ಮಾಡಿ ಬ್ರೇಕ್‌ಪಾಯಿಂಟ್‌ಗಳನ್ನು ಸೆಟ್ ಮಾಡಿ ಮತ್ತು ಕೋಡ್ನಲ್ಲಿ ಪ್ರಗತಿಪಡಿರಿ

ಪరీక్షೆ ಯಶಸ್ವಿಯಾಗಿದ್ದಾಗ, ಪರೀಕ್ಷಾ ಎಕ್ಸ್‌ಪ್ಲೋರರ್ ಹಸಿರು ಚಿನ್ನದ ಗುರುತುಗಳನ್ನು ತೋರಿಸುತ್ತದೆ ಮತ್ತು ವಿಫಲವಾದಾಗ ವಿವರವಾದ ದೋಷ ಸಂದೇಶಗಳನ್ನು ನೀಡುತ್ತದೆ.

## ಪರೀಕ್ಷಾ ಉಲ್ಲೇಖಗಳು

### ಉಲ್ಲೇಖ 1: ಪ್ರಾಂಪ್ಟ್ ಟೆಂಪ್ಲೇಟುಗಳನ್ನು ಪರೀಕ್ಷಿಸುವುದು

ಸರಳವಾದ ಉಲ್ಲೇಖವು ಯಾವುದೇ AI ಮಾದರಿಯನ್ನು ಕರೆಸದೇ ಪ್ರಾಂಪ್ಟ್ ಟೆಂಪ್ಲೇಟುಗಳನ್ನು ಪರೀಕ್ಷಿಸುತ್ತದೆ. ನಿಮಗೆ ಬದಲಾವಣೆ ಪರಿವರ್ತನೆ ಸರಿಯಾಗಿ ಕೆಲಸ ಮಾಡುತ್ತಿದೆಯೇ ಮತ್ತು ಪ್ರಾಂಪ್ಟ್‌ಗಳು ನಿರೀಕ್ಷಿತ ರೀತಿಯಲ್ಲಿ ವಿನ್ಯಾಸಗೊಳ್ಳುತ್ತವೆಯೇ ಎಂಬುದನ್ನು ಪರಿಶೀಲಿಸುವ ಅವಕಾಶ ಇರುತ್ತದೆ.

<img src="../../../translated_images/kn/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*ಬದಲಾವಣೆ ಸ್ಥಾನದೊಂದಿಗೆ ಟೆಂಪ್ಲೇಟ್ → ಮೌಲ್ಯ ಅನ್ವಯಣೆ → ವಿನ್ಯಾಸಗೊಳಿಸಿದ ಔಟ್‌ಪುಟ್ ಪರಿಶೀಲನೆ ಹರಿವು ತೋರಿಸುವ ಪ್ರಾಂಪ್ಟ್ ಟೆಂಪ್ಲೇಟ್ ಪರೀಕ್ಷೆ*

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

**ಇದನ್ನು ರನ್ ಮಾಡುವ ಮೂಲಕ:**

**ಬ್ಯಾಶ್:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#ಪರೀಕ್ಷಾ ಪ್ರಾಂಪ್ಟ್ ಟೆಂಪ್ಲೇಟಿನ ರೂಪರೇಷೆ
```

**ಪವರ್‌ಶೆಲ್:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#ಟೆಸ್ಟ್ ಪ್ರಾಂಪ್ಟ್ ಟೆಂಪ್ಲೇಟು ಫಾರ್ಮ್ಯಾಟಿಂಗ್
```

### ಉಲ್ಲೇಖ 2: ಭಾಷಾ ಮಾದರಿಗಳನ್ನು ಮಾಕಿಂಗ್ ಮಾಡುವುದು

ಸಂವಾದ ಲಾಜಿಕ್ನು ಪರೀಕ್ಷಿಸುವಾಗ, ನಿರ್ಧಾರಿತ ಪ್ರತಿಕ್ರಿಯೆಗಳನ್ನ ನೀಡುವ ನಕಲಿ ಮಾದರಿಗಳನ್ನು ರಚಿಸಲು Mockito ಬಳಸಿ. ಇದರಿಂದ ಪರೀಕ್ಷೆಗಳು ವೇಗವಾಗಿ, ಉಚಿತವಾಗಿ ಮತ್ತು ನಿರ್ದಿಷ್ಟವಾಗಿ ನಡೆಯುತ್ತವೆ.

<img src="../../../translated_images/kn/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*ಪರೀಕ್ಷೆಗಾಗಿ ಮಾಕ್‌ಗಳನ್ನು προಥಮಸ್ಥಾನದಿರುವುದಕ್ಕೆ ಕಾರಣ ತೋರಿಸುವ ಹೋಲಿಕೆ: ಅವುಗಳು ವೇಗವಾದವು, ಉಚಿತವಾಗಿವೆ, ನಿರ್ದಿಷ್ಟವಾಗಿವೆ ಮತ್ತು API ಕೀಲಿಗಳನ್ನು ಅಗತ್ಯವಾಗದೆ ಇರಿಸುವುದು*

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

ಈ ಉಲ್ಲೇಖ `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`‌ನಲ್ಲಿ ಇದೆ. ಮಾಕ್ ಸ್ಥಿರ ಬಿಹೇವಿಯರ್ ಖಚಿತಪಡಿಸುವುದರಿಂದ ನೀವು ಸ್ಮೃತಿ ನಿರ್ವಹಣೆಯು ಸರಿಯಾಗಿದೆಯೇ ಎಂದು ಪರಿಶೀಲಿಸಬಹುದು.

### ಉಲ್ಲೇಖ 3: ಸಂವಾದ ವಿಭಜನೆಯ ಪರೀಕ್ಷೆ

ಸಂವಾದ ಸ್ಮೃತಿ ಹಲವು ಬಳಕೆದಾರರನ್ನು ಬೇರೆಯಾಗಿ ಹಿಡಿದಿಡಬೇಕು. ಈ ಪರೀಕ್ಷೆ ಸಂವಾದಗಳು ಸ೦ದರ್ಭಗಳನ್ನು ಮಿಶ್ರಮಾಡುವುದಿಲ್ಲ ಎಂದು ಪರಿಶೀಲಿಸುತ್ತದೆ.

<img src="../../../translated_images/kn/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*ವಿಭಿನ್ನ ಬಳಕೆದಾರರಿಗಾಗಿ ಪ್ರತ್ಯೇಕ ಸ್ಮೃತಿ ಸಂಗ್ರಹಣೆ ತೋರಿಸುವ ಸಂವಾದ ವಿಭಜನೆಯ ಪರೀಕ್ಷೆ, ಸ೦ದರ್ಭ ಮಿಶ್ರಣ ತಡೆಯಲು*

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

ಪ್ರತಿ ಸಂವಾದವು ತನ್ನ ಸ್ವತಂತ್ರ ಇತಿಹಾಸವನ್ನು ನಿರ್ವಹಿಸುತ್ತದೆ. ಉತ್ಪಾದನಾ ವ್ಯವಸ್ಥೆಗಳಲ್ಲಿ, ಬಹು ಬಳಕೆದಾರರ ಅಪ್ಲಿಕೇಶನ್‌ಗಳಿಗೆ ಈ ವಿಭಜನೆ ಅವಶ್ಯಕ.

### ಉಲ್ಲೇಖ 4: ಉಪಕರಣಗಳನ್ನು ಸ್ವತಂತ್ರವಾಗಿ ಪರೀಕ್ಷಿಸುವುದು

ಉಪಕರಣಗಳು AI ಕರೆಸಬಹುದಾದ ಕಾರ್ಯಗಳಾಗಿವೆ. AI ನಿರ್ಧಾರಗಳ ಪ್ರಭಾವವಿಲ್ಲದೆ ಸರಿಯಾಗಿ ಕೆಲಸ ಮಾಡುತ್ತಿರುವುದನ್ನು ಖಚಿತಪಡಿಸಲು ಅವುಗಳನ್ನು ನೇರವಾಗಿ ಪರೀಕ್ಷಿಸಿ.

<img src="../../../translated_images/kn/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*AI ಕರೆಗಳು ಇಲ್ಲದೆ ನಕಲಿ ಉಪಕರಣ ಕಾರ್ಯಗತಗೊಳಿಸುವಿಕೆ ತೋರಿಸುವ ಉಪಕರಣಗಳ ಸ್ವತಂತ್ರ ಪರೀಕ್ಷೆ, ವ್ಯವಹಾರ ಲಾಜಿಕ್ ಪರಿಶೀಲನೆಗಾಗಿ*

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

`04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` ನಿಂದ ಈ ಪರೀಕ್ಷೆಗಳು AI ಭಾಗವಹಿಸದೇ ಉಪಕರಣ ಲಾಜಿಕ್ ಸರಿಹೊರಹನ್ನು ದೃಢಪಡಿಸುತ್ತವೆ. ಸರಪಳಿ ಉದಾಹರಣೆ ಒಂದು ಉಪಕರಣದ ಔಟ್‌ಪುಟ್ ಮತ್ತೊಂದು ಉಪಕರಣದ ಇನ್‌ಪುಟ್ ಆಗಿ ಬಳಸಲಿರುವ ವಿಧಾನ ತೋರಿಸುತ್ತದೆ.

### ಉಲ್ಲೇಖ 5: ಮೇಮರಿ RAG ಪರೀಕ್ಷೆ

RAG ವ್ಯವಸ್ಥೆಗಳು ಸಾಮಾನ್ಯವಾಗಿ ವೆಕ್ಟರ್ ಡೇಟಾಬೇಸ್ ಮತ್ತು ಎಂಬೆಡ್ಡಿಂಗ್ ಸೇವೆಗಳ ಅವಶ್ಯಕತೆ ಇರುತ್ತದೆ. ಮೇಮರಿ ಉಲ್ಲೇಖವು ಹೊರಗಿನ ಅವಲಂಬನೆಗಳು ಇಲ್ಲದೆ ಸಂಪೂರ್ಣ ಪೈಪ್‌ಲೈನ್ ಅನ್ನು ಪರೀಕ್ಷಿಸಲು ಅನುಮತಿಸುತ್ತದೆ.

<img src="../../../translated_images/kn/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*ಡಾಕ್ಯುಮೆಂಟ್ ಪಾರ್ಸಿಂಗ್, ಎಂಬೆಡ್ಡಿಂಗ್ ಸಂಗ್ರಹಣೆ ಮತ್ತು ಸಾದೃಶ್ಯ ಹುಡುಕಾಟವನ್ನು ಡೇಟಾಬೇಸ್ ಅವಶ್ಯಕತೆ ಇಲ್ಲದೆ ತೋರಿಸುವ ಮೇಮರಿ RAG ಪರೀಕ್ಷೆಯ ಕಾರ್ಯವಾಹಿ*

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

ಈ ಪರೀಕ್ಷೆ `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` ನಲ್ಲಿ ಇದೆ, ಇದು ಡಾಕ್ಯುಮೆಂಟ್ ಅನ್ನು ಮೇಮರಿಯಲ್ಲಿ ರೂಪಿಸಿ ಚಂಕಿಂಗ್ ಮತ್ತು ಮೆಟಾಡೇಟಾ ನಿರ್ವಹಣೆಯನ್ನು ಪರಿಶೀಲಿಸುತ್ತದೆ.

### ಉಲ್ಲೇಖ 6: MCP ಸಂಯೋಜನಾ ಪರೀಕ್ಷೆ

MCP ಮೊಡ್ಯೂಲ್ Model Context Protocol ಸಂಯೋಜನೆಯನ್ನು stdio ಸಾರಿಗೆಯೊಂದಿಗೆ ಪರೀಕ್ಷಿಸುತ್ತದೆ. ಈ ಪರೀಕ್ಷೆಗಳು ನಿಮ್ಮ ಅಪ್ಲಿಕೇಶನ್ MCP ಸರ್ವರ್‌ಗಳನ್ನು subprocess ಗಳಾಗಿ ಸ್ಟಾರ್ಟ್ ಮಾಡಿ ಸಂವಹನ ನಡೆಸಬಲ್ಲುದೇ ಎಂಬುದನ್ನು ದೃಢಪಡಿಸುತ್ತವೆ.

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` ನಲ್ಲಿ ಪರೀಕ್ಷೆಗಳು MCP ಕ್ಲಯಿಂಟ್ ವರ್ತನೆಯನ್ನ ಪರಿಶೀಲಿಸುತ್ತವೆ.

**ಅವುಗಳನ್ನು ರನ್ ಮಾಡುವುದಕ್ಕಾಗಿ:**

**ಬ್ಯಾಶ್:**
```bash
cd 05-mcp && mvn test
```

**ಪವರ್‌ಶೆಲ್:**
```powershell
cd 05-mcp; mvn --% test
```

## ಪರೀಕ್ಷಾ ತತ್ತ್ವಶಾಸ್ತ್ರ

ನೀವು AI ಯನ್ನು ಪರೀಕ್ಷಿಸುವುದಲ್ಲ, ನಿಮ್ಮ ಕೋಡನ್ನು ಪರೀಕ್ಷಿಸಿ. ನಿಮ್ಮ ಪರೀಕ್ಷೆಗಳು ನೀವು ಬರೆದ ಕೋಡನ್ನು ಪರಿಶೀಲಿಸುವಂತೆ ರಚಿಸಬೇಕು, ಅಂದರೆ ಪ್ರಾಂಪ್ಟ್‌ಗಳು ಹೇಗೆ ನಿರ್ಮಿಸಲ್ಪಡುವುವು, ಸ್ಮೃತಿ ಹೇಗೆ ನಿರ್ವಹಿಸಲಾಗುವುದು ಮತ್ತು ಉಪಕರಣಗಳು ಹೇಗೆ ಕಾರ್ಯನಿರ್ವಹಿಸುತ್ತವೆ ಎಂಬುದನ್ನು ಚೆಕ್ ಮಾಡಬೇಕು. AI ಪ್ರತಿಕ್ರಿಯೆಗಳು ಬೇರೆಬೇರೆ ಆಗಿರಬಹುದು, ಅವು ಪರೀಕ್ಷಾ ದೃಢೀಕರಣದ ಭಾಗವಾಗಬಾರದು. ನಿಮ್ಮ ಪ್ರಾಂಪ್ಟ್ ಟೆಂಪ್ಲೇಟ್ ಸರಿಯಾಗಿ ಬದಲಾವಣೆಗಳನ್ನು ಪರಿವರ್ತಿಸುತ್ತದೆಯೇ ಎಂದು ಪ್ರಶ್ನಿಸಿ, AI ಸರಿಯಾದ ಉತ್ತರ ಕೊಟ್ಟಿದೆಯೇ ಎಂದು ಅಲ್ಲ.

ಭಾಷಾ ಮಾದರಿಗಳಿಗಾಗಿ ಮಾಕ್‌ಗಳನ್ನು ಬಳಸಿ. ಅವು ನಿಧಾನ, ದುಬಾರಿ ಮತ್ತು ನಿರ್ಧಾರಾತ್ಮಕವಲ್ಲದ ಹೊರಗಿನ ಅವಲಂಬನೆಗಳಾಗಿವೆ. ಮಾಕಿಂಗ್ ಪರೀಕ್ಷೆಗಳನ್ನು ಸೆಕೆಂಡ್‌ಗಳ ಬದಲಾಗಿ ಮಿಲಿಸೆಕೆಂಡ್‌ಗಳಲ್ಲಿ ವೇಗವಾಗಿ, API ಖರ್ಚಿಲ್ಲದೆ ಉಚಿತವಾಗಿ ಮತ್ತು ಯಾವಾಗಲೂ ಒಂದೇ ಫಲಿತಾಂಶ ನೀಡುವಂತೆ ನಿರ್ಧಾರಾತ್ಮಕವಾಗಿ ಮಾಡುತ್ತದೆ.

ಪರೀಕ್ಷೆಗಳನ್ನು ಸ್ವತಂತ್ರವಾಗಿ ಇಡು. ಪ್ರತಿ ಪರೀಕ್ಷೆ ತನ್ನ ಸ್ವಂತ ಡೇಟಾವನ್ನು ಸಿದ್ದಪಡಿಸಬೇಕು, ಇತರ ಪರೀಕ್ಷೆಗಳ ಮೇಲೆ ಆಶ್ರಯವಿರುವುದಿಲ್ಲ, ಮತ್ತು ತನ್ನ ನಂತರ ಸ್ವಚ್ಛಗೊಳಿಸಬೇಕು. ಪರೀಕ್ಷೆಗಳು ಕಾರ್ಯಾಚರಣಾ ಕ್ರಮದ ಮೇಲೆ ಅವಲಂಬಿಸದಿರಲಿ.

ಸುಖಪಥವನ್ನು ಮೀರಿ ಗಡಿ ಪ್ರಕರಣಗಳನ್ನು ಪರೀಕ್ಷಿಸಿ. ಖಾಲಿ ಒಳmondುಗಳು, ಬಹು ದೊಡ್ಡ ಒಳmondುಗಳು, ವಿಶೇಷ ಅಕ್ಷರಗಳು, ಅಮಾನ್ಯ ನಿಯಮಗಳು ಮತ್ತು ಅಳವಡಿಕೆ ವಿಮರ್ಶೆಗಳನ್ನು ಪ್ರಯತ್ನಿಸಿ. ಇವು ಸಾಮಾನ್ಯ ಬಳಕೆಯಲ್ಲಿ ಕಾಣಿಸದ ದೋಷಗಳನ್ನು ಬಹುಶಃ ಪತ್ತೆಹಚ್ಚುತ್ತವೆ.

ವಿವರಣಾತ್ಮಕ ಹೆಸರುಗಳನ್ನು ಬಳಸಿರಿ. ಉದಾಹರಣೆಗೆ `shouldMaintainConversationHistoryAcrossMultipleMessages()` ಅನ್ನು `test1()` ಜೊತೆ ಹೋಲಿಸಿ. ಮೊದಲದು ಯಾವುದು ಪರೀಕ್ಷೆಯಾಗಿದೆ ಎಂಬುದನ್ನು ಸರಿಯಾಗಿ ತಿಳಿಸುತ್ತದೆ, ದೋಷ ಪರಿಶೀಲನೆಯನ್ನು ತುಂಬಾ ಸುಲಭಗೊಳಿಸುತ್ತದೆ.

## ಮುಂದಿನ ಹಂತಗಳು

ನೀವು ಈಗ ಪರೀಕ್ಷಾ ಉಲ್ಲೇಖಗಳನ್ನು ಅರ್ಥಮಾಡಿಕೊಂಡಿದ್ದೀರಿ, ಪ್ರತಿ ಮಂಟಪದಲ್ಲಿ ಕೆಲವು ಗಂಭೀರ ಅಧ್ಯಯನ ಮಾಡಿ:

- **[00 - ತ್ವರಿತ ಪ್ರಾರಂಭ](../00-quick-start/README.md)** - ಪ್ರಾಂಪ್ಟ್ ಟೆಂಪ್ಲೇಟ್ ಮೂಲಭೂತಗಳು
- **[01 - ಪರಿಚಯ](../01-introduction/README.md)** - ಸಂವಾದ ಸ್ಮೃತಿ ನಿರ್ವಹಣೆ ಕಲಿತುಕೊಳ್ಳಿ
- **[02 - ಪ್ರಾಂಪ್ಟ್ ಎಂಜಿನಿಯರಿಂಗ್](../02-prompt-engineering/README.md)** - GPT-5.2 ಪ್ರಾಂಪ್ಟ್ ಮಾದರಿಗಳನ್ನು ماهಿರ ಆಗಿ
- **[03 - RAG](../03-rag/README.md)** - ರಿಟ್ರೀವಲ್-ಆಗ್ಮೆಂಟೆಡ್ ಜೇನರೇಷನ್ ವ್ಯವಸ್ಥೆಗಳನ್ನು ನಿರ್ಮಿಸಿ
- **[04 - ಉಪಕರಣಗಳು](../04-tools/README.md)** - ಕಾರ್ಯ ಕರೆ ಮತ್ತು ಉಪಕರಣ ಸರಪಳಿಗಳನ್ನು ಜಾರಿಗೆ ತರುವುದು
- **[05 - MCP](../05-mcp/README.md)** - Model Context Protocol ಸಂಯೋಜಿಸಿ

ಪ್ರತಿ ಮಂಟಪದ README ಇಲ್ಲಿ ಪರೀಕ್ಷಿಸಲಾದ ತತ್ವಗಳ ವಿವರವಾದ ವಿವರಣೆಗಳನ್ನು ನೀಡುತ್ತದೆ.

---

**Navigation:** [← ಹಿಂದುಮುಖ ಮಾಡಿ ಮುಖ್ಯ ಪುಟへ](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ಅಮರ್ಪಣೆ**:  
ಈ ದಾಖಲೆ AI ಅನುವಾದ ಸೇವೆ [Co-op Translator](https://github.com/Azure/co-op-translator) ಬಳಸಿ ಅನುವದಿಸಲಾಗಿದೆ. ನಾವು ಶುದ್ಧತೆಯಿಗಾಗಿ ಪ್ರಯತ್ನಿಸಿದರೂ, ಸ್ವಯಂಕ್ರಿಯ ಅನುವಾದದಲ್ಲಿ ದೋಷಗಳು ಅಥವಾ ಅಸತ್ಯತೆಗಳಿರಬಹುದೆಂದು ದಯವಿಟ್ಟು ಗಮನಿಸಿ. ಮೂಲ ದಸ್ತಾವೇಜು ಅದರ ಸ್ವದೇಶಿ ಭಾಷೆಯಲ್ಲಿ ಪ್ರಾಧಿಕೃತ ಮೂಲವಾಗಿಯೇ ಪರಿಗಣಿಸಬೇಕು. ಪ್ರಮುಖ ಮಾಹಿತಿಗಾಗಿ, ವೃತ್ತಿಪರ ಮಾನವ ಅನುವಾದವನ್ನು ಶಿಫಾರಸು ಮಾಡಲಾಗುತ್ತದೆ. ಈ ಅನುವಾದ ಬಳಕೆಯಿಂದ ಉಂಟಾಗುವ ಯಾವುದೇ ತಪ್ಪುದೊಡ್ಡಿಕೆಗಳು ಅಥವಾ ತಪ್ಪು ಅರ್ಥಗಳಿಂದ ನಾವು ಜವಾಬ್ದಾರರಾಗುವುದಿಲ್ಲ.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->