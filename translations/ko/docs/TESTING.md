# LangChain4j 애플리케이션 테스트

## 목차

- [빠른 시작](../../../docs)
- [테스트 범위](../../../docs)
- [테스트 실행하기](../../../docs)
- [VS Code에서 테스트 실행하기](../../../docs)
- [테스트 패턴](../../../docs)
- [테스트 철학](../../../docs)
- [다음 단계](../../../docs)

이 가이드는 API 키나 외부 서비스 없이 AI 애플리케이션을 테스트하는 방법을 보여주는 테스트들을 안내합니다.

## 빠른 시작

단일 명령어로 모든 테스트를 실행하세요:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

모든 테스트가 통과하면 아래 스크린샷과 같은 출력이 나타납니다 — 테스트가 하나도 실패하지 않고 실행됩니다.

<img src="../../../translated_images/ko/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*모든 테스트가 실패 없이 성공적으로 실행된 사례*

## 테스트 범위

이 과정은 로컬에서 실행되는 **단위 테스트**에 초점을 맞춥니다. 각 테스트는 고립된 상태에서 특정 LangChain4j 개념을 보여줍니다. 아래 테스트 피라미드는 단위 테스트가 어디에 속하는지 보여줍니다 — 단위 테스트는 나머지 테스트 전략이 구축되는 빠르고 신뢰할 수 있는 기반을 형성합니다.

<img src="../../../translated_images/ko/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*단위 테스트(빠르고 고립됨), 통합 테스트(실제 컴포넌트), 그리고 엔드투엔드 테스트 간 균형을 보여주는 테스트 피라미드. 이 교육은 단위 테스트를 다룹니다.*

| 모듈 | 테스트 수 | 중점 | 주요 파일 |
|--------|-------|-------|-----------|
| **00 - 빠른 시작** | 6 | 프롬프트 템플릿 및 변수 대체 | `SimpleQuickStartTest.java` |
| **01 - 소개** | 8 | 대화 메모리 및 상태 유지 채팅 | `SimpleConversationTest.java` |
| **02 - 프롬프트 엔지니어링** | 12 | GPT-5.2 패턴, 열의 수준, 구조화된 출력 | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | 문서 입력, 임베딩, 유사도 검색 | `DocumentServiceTest.java` |
| **04 - 도구** | 12 | 함수 호출 및 도구 체이닝 | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Stdio 전송방식의 모델 컨텍스트 프로토콜 | `SimpleMcpTest.java` |

## 테스트 실행하기

**루트에서 모든 테스트 실행:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**특정 모듈의 테스트 실행:**

**Bash:**
```bash
cd 01-introduction && mvn test
# 루트에서 또는
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# 또는 루트에서부터
mvn --% test -pl 01-introduction
```

**단일 테스트 클래스 실행:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**특정 테스트 메서드 실행:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#대화 기록을 유지해야 합니다
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#대화 기록을 유지해야 합니다
```

## VS Code에서 테스트 실행하기

Visual Studio Code를 사용하는 경우, 테스트 탐색기는 테스트를 실행하고 디버그할 수 있는 그래픽 인터페이스를 제공합니다.

<img src="../../../translated_images/ko/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*모든 자바 테스트 클래스와 개별 테스트 메서드를 보여주는 VS Code 테스트 탐색기*

**VS Code에서 테스트를 실행하려면:**

1. 활동 표시줄에서 플라스크 아이콘을 클릭하여 테스트 탐색기를 엽니다.
2. 테스트 트리를 확장해 모든 모듈과 테스트 클래스를 확인합니다.
3. 임의의 테스트 옆의 실행 버튼을 클릭해 개별 실행합니다.
4. "모든 테스트 실행"을 클릭해 전체 테스트를 실행합니다.
5. 임의의 테스트에서 마우스 오른쪽 버튼을 클릭 후 "테스트 디버그"를 선택해 중단점을 설정하고 코드 단계를 밟아갈 수 있습니다.

테스트 탐색기는 통과한 테스트에는 녹색 체크표시를, 실패한 테스트에는 자세한 실패 메시지를 제공합니다.

## 테스트 패턴

### 패턴 1: 프롬프트 템플릿 테스트

가장 단순한 패턴으로, AI 모델을 호출하지 않고 프롬프트 템플릿을 테스트합니다. 변수 대체가 올바르게 작동하는지, 프롬프트가 예상대로 형식화되는지 확인합니다.

<img src="../../../translated_images/ko/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*프롬프트 템플릿 테스트: 플레이스홀더가 있는 템플릿 → 값 적용 → 포맷된 출력 확인 흐름*

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

이 테스트는 `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`에 위치합니다.

**실행 방법:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#테스트프롬프트템플릿포맷팅
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#테스트프롬프트템플릿포매팅
```

### 패턴 2: 언어 모델 모킹

대화 로직 테스트 시 Mockito를 사용해 미리 정해진 응답을 반환하는 가짜 모델을 만듭니다. 이렇게 하면 테스트가 빠르고 무료이며 결정론적입니다.

<img src="../../../translated_images/ko/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*테스트에 모킹이 선호되는 이유 비교: 빠르고, 무료이며, 결정론적이고, API 키가 필요 없음*

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
        assertThat(history).hasSize(6); // 사용자 3명 + AI 메시지 3개
    }
}
```

이 패턴은 `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`에 나타납니다. 모킹은 일관된 동작을 보장해 메모리 관리가 올바르게 작동하는지 검증합니다.

### 패턴 3: 대화 고립성 테스트

대화 메모리는 여러 사용자를 분리해야 합니다. 이 테스트는 대화가 컨텍스트를 섞지 않는지 확인합니다.

<img src="../../../translated_images/ko/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*대화 고립성 테스트: 서로 다른 사용자용 독립 메모리 저장소로 컨텍스트 혼합 방지*

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

각 대화는 독립된 히스토리를 유지합니다. 운영 환경에서는 이 고립성이 다중 사용자 애플리케이션에 매우 중요합니다.

### 패턴 4: 도구 독립 테스트

도구는 AI가 호출할 수 있는 함수들입니다. AI 결정과 관계없이 도구가 올바르게 작동하는지 직접 테스트합니다.

<img src="../../../translated_images/ko/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*도구 독립 테스트: AI 호출 없이 모킹 도구 실행으로 비즈니스 로직 검증*

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

이 테스트들은 `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java`에서 AI 개입 없이 도구 로직을 검증합니다. 체이닝 예제는 한 도구의 출력이 다른 도구 입력으로 어떻게 이어지는지 보여줍니다.

### 패턴 5: 인메모리 RAG 테스트

RAG 시스템은 보통 벡터 데이터베이스와 임베딩 서비스가 필요합니다. 인메모리 패턴을 사용하면 외부 의존성 없이 파이프라인 전체를 테스트할 수 있습니다.

<img src="../../../translated_images/ko/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*인메모리 RAG 테스트 워크플로우: 데이터베이스 없이 문서 파싱, 임베딩 저장, 유사도 검색*

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

이 테스트는 `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java`에서 메모리에 문서를 생성하고 청크 분할 및 메타데이터 처리를 검증합니다.

### 패턴 6: MCP 통합 테스트

MCP 모듈은 stdio 전송방식을 사용하는 모델 컨텍스트 프로토콜 통합을 테스트합니다. 이 테스트들은 애플리케이션이 서브프로세스로 MCP 서버를 실행하고 통신할 수 있는지 검증합니다.

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java`에 MCP 클라이언트 동작을 검증하는 테스트가 있습니다.

**실행 방법:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## 테스트 철학

AI를 테스트하지 말고, 코드를 테스트하세요. 테스트는 프롬프트가 어떻게 구성되는지, 메모리가 어떻게 관리되는지, 도구가 어떻게 실행되는지를 검증해야 합니다. AI 응답은 달라질 수 있으므로 테스트 단언에 포함하지 마세요. 프롬프트 템플릿이 올바른 변수 대체를 하는지를 묻고, AI가 정답을 주는지를 묻지 마세요.

언어 모델에는 모킹을 사용하세요. 언어 모델은 느리고, 비용이 들며, 비결정론적인 외부 의존성입니다. 모킹은 테스트를 초 단위가 아닌 밀리초 단위로 빠르게 만들고, API 비용이 없으며, 항상 같은 결과로 결정론적입니다.

테스트는 독립적으로 유지하세요. 각 테스트는 자신의 데이터를 설정하고, 다른 테스트에 의존하지 않으며, 스스로 정리해야 합니다. 테스트는 실행 순서와 관계없이 통과해야 합니다.

정상 경로뿐 아니라 경계 조건도 테스트하세요. 빈 입력, 매우 큰 입력, 특수 문자, 잘못된 매개변수, 경계 조건 등을 시도하세요. 이런 테스트는 정상 사용에서 발견하기 어려운 버그를 노출합니다.

설명적인 이름을 사용하세요. `shouldMaintainConversationHistoryAcrossMultipleMessages()`와 `test1()`를 비교해보세요. 전자는 무엇을 테스트하는지 정확히 알려줘 실패 원인 파악이 훨씬 쉽습니다.

## 다음 단계

이제 테스트 패턴을 이해했으니 각 모듈을 더 깊이 탐구하세요:

- **[00 - 빠른 시작](../00-quick-start/README.md)** - 프롬프트 템플릿 기본부터 시작
- **[01 - 소개](../01-introduction/README.md)** - 대화 메모리 관리 배우기
- **[02 - 프롬프트 엔지니어링](../02/prompt-engineering/README.md)** - GPT-5.2 프롬프트 패턴 마스터하기
- **[03 - RAG](../03-rag/README.md)** - 검색 보강 생성 시스템 구축하기
- **[04 - 도구](../04-tools/README.md)** - 함수 호출 및 도구 체인 구현하기
- **[05 - MCP](../05-mcp/README.md)** - 모델 컨텍스트 프로토콜 통합하기

각 모듈의 README는 여기서 테스트된 개념들을 상세히 설명합니다.

---

**네비게이션:** [← 메인으로 돌아가기](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 최선을 다하고 있으나, 자동 번역은 오류나 부정확성이 포함될 수 있음을 유의해 주시기 바랍니다. 원본 문서가 권위 있는 출처로 간주되어야 합니다. 중요한 정보의 경우에는 전문적인 인간 번역을 권장합니다. 이 번역의 사용으로 인해 발생하는 오해나 잘못된 해석에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->