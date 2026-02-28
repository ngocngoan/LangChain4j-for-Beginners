# Module 00: 빠른 시작

## 목차

- [소개](../../../00-quick-start)
- [LangChain4j란?](../../../00-quick-start)
- [LangChain4j 의존성](../../../00-quick-start)
- [필수 조건](../../../00-quick-start)
- [설정](../../../00-quick-start)
  - [1. GitHub 토큰 받기](../../../00-quick-start)
  - [2. 토큰 설정하기](../../../00-quick-start)
- [예제 실행하기](../../../00-quick-start)
  - [1. 기본 채팅](../../../00-quick-start)
  - [2. 프롬프트 패턴](../../../00-quick-start)
  - [3. 함수 호출](../../../00-quick-start)
  - [4. 문서 Q&A (Easy RAG)](../../../00-quick-start)
  - [5. 책임 있는 AI](../../../00-quick-start)
- [각 예제 설명](../../../00-quick-start)
- [다음 단계](../../../00-quick-start)
- [문제 해결](../../../00-quick-start)

## 소개

이 빠른 시작은 LangChain4j를 최대한 빨리 시작하고 실행할 수 있도록 돕기 위한 것입니다. LangChain4j와 GitHub Models로 AI 애플리케이션을 구축하는 기본 사항을 다룹니다. 다음 모듈에서는 LangChain4j와 Azure OpenAI를 사용하여 더 고급 애플리케이션을 구축할 것입니다.

## LangChain4j란?

LangChain4j는 AI 기반 애플리케이션 구축을 간소화하는 자바 라이브러리입니다. HTTP 클라이언트나 JSON 파싱을 직접 다루는 대신, 깔끔한 자바 API를 사용합니다.

LangChain의 "체인"은 여러 구성 요소를 연결하는 것을 의미합니다 - 프롬프트를 모델에, 모델을 파서에 연결하거나 여러 AI 호출을 연쇄하여 한 출력이 다음 입력이 되는 방식을 뜻합니다. 이 빠른 시작은 복잡한 체인을 탐구하기 전에 기본에 집중합니다.

<img src="../../../translated_images/ko/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j에서 구성 요소 연결 - 강력한 AI 워크플로우를 만들기 위해 빌딩 블록을 연결하는 모습*

우리는 세 가지 핵심 구성 요소를 사용할 것입니다:

**ChatModel** - AI 모델과 상호작용하는 인터페이스입니다. `model.chat("prompt")`를 호출하면 응답 문자열을 받습니다. OpenAI 호환 엔드포인트인 GitHub Models와 작동하는 `OpenAiOfficialChatModel`을 사용합니다.

**AiServices** - 타입 안전 AI 서비스 인터페이스를 생성합니다. 메서드를 정의하고 `@Tool`로 주석을 추가하면 LangChain4j가 오케스트레이션을 처리합니다. AI가 필요할 때 자동으로 자바 메서드를 호출합니다.

**MessageWindowChatMemory** - 대화 기록을 유지합니다. 없으면 각 요청이 독립적이지만, 있으면 AI가 이전 메시지를 기억해 여러 턴에 걸쳐 문맥을 유지합니다.

<img src="../../../translated_images/ko/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j 아키텍처 - 핵심 구성 요소가 함께 작동하여 AI 애플리케이션을 구동*

## LangChain4j 의존성

이 빠른 시작은 [`pom.xml`](../../../00-quick-start/pom.xml)에서 세 가지 Maven 의존성을 사용합니다:

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

`langchain4j-open-ai-official` 모듈은 OpenAI 호환 API에 연결하는 `OpenAiOfficialChatModel` 클래스를 제공합니다. GitHub Models는 동일한 API 형식을 사용하므로 별도의 어댑터가 필요 없으며, 기본 URL만 `https://models.github.ai/inference`로 지정하면 됩니다.

`langchain4j-easy-rag` 모듈은 자동 문서 분할, 임베딩 및 검색을 제공하여 각 단계를 수동으로 구성하지 않고 RAG 애플리케이션을 만들 수 있게 합니다.

## 필수 조건

**개발 컨테이너 사용 중인가요?** Java와 Maven은 이미 설치되어 있습니다. GitHub 개인 액세스 토큰만 필요합니다.

**로컬 개발 환경:**
- Java 21 이상, Maven 3.9 이상
- GitHub 개인 액세스 토큰 (아래 지침 참고)

> **참고:** 이 모듈은 GitHub Models의 `gpt-4.1-nano`를 사용합니다. 코드 내 모델 이름을 변경하지 마세요 - GitHub에서 지원하는 모델과 작동하도록 설정되어 있습니다.

## 설정

### 1. GitHub 토큰 받기

1. [GitHub 설정 → 개인 액세스 토큰](https://github.com/settings/personal-access-tokens)으로 이동
2. "새 토큰 생성" 클릭
3. 이름 설정 (예: "LangChain4j Demo")
4. 만료일 설정 (7일 권장)
5. "계정 권한"에서 "Models" 권한을 "읽기 전용"으로 설정
6. "토큰 생성" 클릭
7. 토큰 복사 후 저장 (다시 볼 수 없습니다)

### 2. 토큰 설정하기

**옵션 1: VS Code 사용 (권장)**

VS Code를 사용하는 경우, 프로젝트 루트의 `.env` 파일에 토큰을 추가하세요:

`.env` 파일이 없으면 `.env.example`을 `.env`로 복사하거나 새 `.env` 파일을 만드세요.

**예시 `.env` 파일:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env 안에 있습니다
GITHUB_TOKEN=your_token_here
```

그 후 탐색기에서 데모 파일(예: `BasicChatDemo.java`)을 마우스 오른쪽 버튼으로 클릭 후 **"Run Java"**를 선택하거나 실행 및 디버그 패널의 실행 구성을 사용할 수 있습니다.

**옵션 2: 터미널 사용**

환경 변수로 토큰 설정:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## 예제 실행하기

**VS Code 사용 시:** 탐색기에서 데모 파일을 오른쪽 클릭하고 **"Run Java"**를 선택하거나 실행 및 디버그 패널의 실행 구성을 사용하세요 (.env 파일에 토큰을 추가해야 합니다).

**Maven 사용 시:** 또는 명령줄에서 실행할 수 있습니다:

### 1. 기본 채팅

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. 프롬프트 패턴

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

제로샷, 퓨샷, 체인 오브 스로트, 역할 기반 프롬프트를 보여줍니다.

### 3. 함수 호출

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

필요할 때 AI가 자동으로 자바 메서드를 호출합니다.

### 4. 문서 Q&A (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

자동 임베딩 및 검색이 포함된 Easy RAG을 사용해 문서에 대해 질문하세요.

### 5. 책임 있는 AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI 안전 필터가 유해 콘텐츠를 차단하는 모습을 확인하세요.

## 각 예제 설명

**기본 채팅** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

가장 단순한 LangChain4j 사용법을 볼 수 있는 시작점입니다. `OpenAiOfficialChatModel`을 생성하고 `.chat()`으로 프롬프트를 보내면 응답을 받습니다. 맞춤 엔드포인트와 API 키로 모델을 초기화하는 기본을 보여줍니다. 이 패턴을 이해하면 나머지 모든 것이 이것을 기반으로 합니다.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 챗으로 시도해 보세요:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)를 열고 다음을 물어보세요:
> - "이 코드에서 GitHub Models에서 Azure OpenAI로 어떻게 전환하나요?"
> - "OpenAiOfficialChatModel.builder()에서 설정할 수 있는 다른 매개변수는 무엇인가요?"
> - "전체 응답을 기다리지 않고 스트리밍 응답을 추가하려면 어떻게 하나요?"

**프롬프트 엔지니어링** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

모델과 대화하는 법을 알았다면, 이제 무엇을 말할지 탐구합니다. 이 데모는 동일 모델 설정을 사용하지만 다섯 가지 다른 프롬프트 패턴을 보여줍니다. 직접 지시하는 제로샷, 예시로 배우는 퓨샷, 추론 단계를 드러내는 체인 오브 스로트, 문맥을 설정하는 역할 기반 프롬프트를 시도해 보세요. 같은 모델이 요청하는 방식에 따라 크게 달라지는 결과를 볼 수 있습니다.

이 데모는 또한 변수로 재사용 가능한 프롬프트를 만드는 데 강력한 프롬프트 템플릿도 보여줍니다.
아래 예시는 LangChain4j `PromptTemplate`을 사용해 변수를 채우는 프롬프트입니다. AI는 주어진 목적지와 활동을 기반으로 답변합니다.

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 챗으로 시도해 보세요:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)를 열고 묻기:
> - "제로샷과 퓨샷 프롬프트의 차이가 뭔가요, 그리고 각각 언제 써야 하죠?"
> - "temperature 매개변수가 모델 응답에 어떻게 영향을 미치나요?"
> - "프로덕션에서 프롬프트 인젝션 공격을 방지하는 기법은 뭐가 있나요?"
> - "공통 패턴에 재사용 가능한 PromptTemplate 객체는 어떻게 만들어요?"

**도구 통합** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

여기서 LangChain4j가 강력해집니다. `AiServices`를 사용해 자바 메서드를 호출할 수 있는 AI 비서를 만들 수 있습니다. 메서드에 `@Tool("설명")` 주석만 달면 나머지는 LangChain4j가 처리합니다 - 사용자가 요청할 때 AI가 자동으로 도구를 선택해 호출합니다. 이것은 함수 호출 기능을 시연하며, 질문에 답하는 것 이상으로 행동할 수 있는 AI 구축 핵심 기법입니다.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 챗으로 시도해 보세요:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)를 열고:
> - "@Tool 주석은 어떻게 작동하며, LangChain4j는 내부적으로 무엇을 하나요?"
> - "AI가 여러 도구를 순차적으로 호출해 복잡한 문제를 해결할 수 있나요?"
> - "도구가 예외를 던지면 어떻게 해야 하나요 - 오류 처리는 어떻게 해야 하나요?"
> - "이 계산기 예제 대신 실제 API를 어떻게 통합하나요?"

**문서 Q&A (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

여기서는 LangChain4j의 "Easy RAG" 방식을 활용한 RAG(검색 강화 생성)를 볼 수 있습니다. 문서를 불러오고 자동으로 분할 및 임베딩하여 메모리 기반 저장소에 저장한 뒤, 쿼리 시 관련 조각을 AI에 제공합니다. AI는 일반적인 지식이 아닌 문서 기반으로 답변합니다.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 챗으로 시도해 보세요:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)를 열고:
> - "RAG는 모델 학습 데이터만 사용할 때와 비교해 AI 환각을 어떻게 방지하나요?"
> - "이 간편한 방식과 맞춤형 RAG 파이프라인의 차이는 무엇인가요?"
> - "이걸 다수 문서나 더 큰 지식 베이스로 확장하려면 어떻게 해야 하나요?"

**책임 있는 AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

심층 방어로 AI 안전을 구축합니다. 이 데모는 두 층의 보호를 함께 보여줍니다:

**1부: LangChain4j 입력 가드레일** - 위험한 프롬프트가 LLM에 도달하기 전에 차단합니다. 금지 키워드나 패턴을 검사하는 커스텀 가드레일을 만드세요. 코드 내에서 실행되어 빠르고 비용이 들지 않습니다.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**2부: 공급자 안전 필터** - GitHub Models에 내장된 필터가 가드레일이 놓친 부분을 잡아냅니다. 심각한 위반에는 강제 차단(HTTP 400 오류), 경미한 경우에는 AI가 정중히 거절하는 소프트 거부가 있습니다.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 챗으로 시도해 보세요:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)를 열고:
> - "InputGuardrail이란 무엇이며, 어떻게 직접 만들 수 있나요?"
> - "강제 차단과 소프트 거부의 차이는 무엇인가요?"
> - "왜 가드레일과 공급자 필터를 함께 사용해야 하나요?"

## 다음 단계

**다음 모듈:** [01-introduction - LangChain4j와 Azure의 gpt-5 시작하기](../01-introduction/README.md)

---

**내비게이션:** [← 메인으로 돌아가기](../README.md) | [다음: 모듈 01 - 소개 →](../01-introduction/README.md)

---

## 문제 해결

### 첫 빌드 시 Maven 빌드 문제

**문제:** 초기 `mvn clean compile` 또는 `mvn package` 실행 시간이 오래 걸림 (10-15분)

**원인:** Maven이 첫 빌드 시 모든 프로젝트 의존성(Spring Boot, LangChain4j 라이브러리, Azure SDK 등)을 다운로드해야 함.

**해결:** 정상적인 동작입니다. 이후 빌드는 로컬에 의존성이 캐시되어 훨씬 빨라집니다. 다운로드 시간은 네트워크 속도에 따라 다름.

### PowerShell에서 Maven 명령어 구문 오류

**문제:** Maven 명령 실행 시 `Unknown lifecycle phase ".mainClass=..."` 오류가 발생함
**원인**: PowerShell은 `=`를 변수 할당 연산자로 해석하여 Maven 속성 구문이 깨짐

**해결책**: Maven 명령어 앞에 정지 구문 분석 연산자 `--%`를 사용하세요:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` 연산자는 PowerShell에 나머지 인수를 해석하지 않고 Maven에 문자 그대로 전달하도록 지시합니다.

### Windows PowerShell 이모지 표시

**문제**: PowerShell에서 AI 응답에 이모지 대신 깨진 문자(`????` 또는 `â??` 등)가 표시됨

**원인**: PowerShell 기본 인코딩이 UTF-8 이모지를 지원하지 않음

**해결책**: Java 애플리케이션 실행 전에 다음 명령을 실행하세요:
```cmd
chcp 65001
```

이 명령은 터미널에서 UTF-8 인코딩을 강제합니다. 또는 더 나은 유니코드 지원이 있는 Windows Terminal을 사용할 수 있습니다.

### API 호출 디버깅

**문제**: AI 모델에서 인증 오류, 속도 제한 또는 예상치 못한 응답 발생

**해결책**: 예제에는 콘솔에 API 호출을 표시하는 `.logRequests(true)` 및 `.logResponses(true)`가 포함되어 있습니다. 이는 인증 오류, 속도 제한 또는 예상치 못한 응답 문제를 해결하는 데 도움이 됩니다. 프로덕션에서는 로그 소음을 줄이기 위해 이 플래그들을 제거하세요.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 최선을 다하고 있으나 자동 번역에는 오류나 부정확한 부분이 있을 수 있음을 유의하시기 바랍니다. 원본 문서의 본래 언어가 권위 있는 출처로 간주되어야 합니다. 중요한 정보의 경우에는 전문 번역가의 번역을 권장합니다. 본 번역 사용으로 인해 발생하는 오해나 잘못된 해석에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->