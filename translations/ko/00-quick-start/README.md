# Module 00: 빠른 시작

## 목차

- [소개](../../../00-quick-start)
- [LangChain4j란?](../../../00-quick-start)
- [LangChain4j 의존성](../../../00-quick-start)
- [사전 요구 사항](../../../00-quick-start)
- [설정](../../../00-quick-start)
  - [1. GitHub 토큰 받기](../../../00-quick-start)
  - [2. 토큰 설정하기](../../../00-quick-start)
- [예제 실행하기](../../../00-quick-start)
  - [1. 기본 채팅](../../../00-quick-start)
  - [2. 프롬프트 패턴](../../../00-quick-start)
  - [3. 함수 호출](../../../00-quick-start)
  - [4. 문서 Q&A (Easy RAG)](../../../00-quick-start)
  - [5. 책임 있는 AI](../../../00-quick-start)
- [각 예제가 보여주는 것](../../../00-quick-start)
- [다음 단계](../../../00-quick-start)
- [문제 해결](../../../00-quick-start)

## 소개

이 빠른 시작 가이드는 LangChain4j를 최대한 빠르게 시작할 수 있도록 설계되었습니다. LangChain4j와 GitHub 모델을 사용하여 AI 애플리케이션을 구축하는 기본을 다룹니다. 다음 모듈에서는 Azure OpenAI와 GPT-5.2로 전환하여 각 개념을 더 깊이 탐구합니다.

## LangChain4j란?

LangChain4j는 AI 기반 애플리케이션 구축을 단순화하는 자바 라이브러리입니다. HTTP 클라이언트와 JSON 파싱을 다루는 대신, 깔끔한 자바 API로 작업할 수 있습니다.

LangChain의 "체인"이란 여러 구성 요소를 연결하는 것을 의미합니다 - 프롬프트를 모델에 연결하고 파서와 연결하거나 여러 AI 호출을 연결하여 한 출력이 다음 입력이 되는 식입니다. 이 빠른 시작에서는 더 복잡한 체인을 탐구하기 전 기본에 집중합니다.

<img src="../../../translated_images/ko/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j에서 구성 요소를 체인으로 연결 - 강력한 AI 워크플로를 만드는 빌딩 블록*

세 가지 핵심 구성 요소를 사용합니다:

**ChatModel** - AI 모델 상호작용을 위한 인터페이스입니다. `model.chat("prompt")`를 호출하여 응답 문자열을 받습니다. 우리는 OpenAI 호환 엔드포인트(예: GitHub 모델)와 작동하는 `OpenAiOfficialChatModel`을 사용합니다.

**AiServices** - 타입 안정 AI 서비스 인터페이스를 생성합니다. 메서드를 정의하고 `@Tool`로 주석 처리하면 LangChain4j가 오케스트레이션을 처리합니다. AI가 필요할 때 자동으로 자바 메서드를 호출합니다.

**MessageWindowChatMemory** - 대화 기록을 유지합니다. 이 기능 없이는 각 요청이 독립적입니다. 이 기능이 있으면 AI가 이전 메시지를 기억하여 여러 턴에 걸쳐 컨텍스트를 유지합니다.

<img src="../../../translated_images/ko/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j 아키텍처 - 핵심 구성 요소가 함께 작동하여 AI 애플리케이션을 구동*

## LangChain4j 의존성

이 빠른 시작은 [`pom.xml`](../../../00-quick-start/pom.xml) 내의 세 가지 Maven 의존성을 사용합니다:

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

`langchain4j-open-ai-official` 모듈은 OpenAI 호환 API에 연결하는 `OpenAiOfficialChatModel` 클래스를 제공합니다. GitHub 모델은 동일한 API 형식을 사용하므로 특별한 어댑터가 필요 없으며, 기본 URL을 `https://models.github.ai/inference`로 지정하면 됩니다.

`langchain4j-easy-rag` 모듈은 자동 문서 분할, 임베딩, 검색 기능을 제공하여 각 단계를 수동으로 구성하지 않고 RAG 애플리케이션을 구축할 수 있게 합니다.

## 사전 요구 사항

**Dev Container를 사용 중이신가요?** Java와 Maven이 이미 설치되어 있습니다. 오직 GitHub 개인 액세스 토큰만 필요합니다.

**로컬 개발 환경:**
- Java 21 이상, Maven 3.9 이상
- GitHub 개인 액세스 토큰 (아래 지침 참조)

> **참고:** 이 모듈은 GitHub 모델의 `gpt-4.1-nano` 모델을 사용합니다. 코드 내 모델 이름을 변경하지 마십시오 - GitHub에서 제공하는 모델에 맞게 구성되어 있습니다.

## 설정

### 1. GitHub 토큰 받기

1. [GitHub 설정 → 개인 액세스 토큰](https://github.com/settings/personal-access-tokens)으로 이동
2. "새 토큰 생성" 클릭
3. 설명 이름 설정 (예: "LangChain4j 데모")
4. 만료 기간 설정 (7일 권장)
5. "계정 권한"에서 "Models"를 "읽기 전용"으로 설정
6. "토큰 생성" 클릭
7. 토큰을 복사하여 저장 (다시 볼 수 없습니다)

### 2. 토큰 설정하기

**옵션 1: VS Code 사용(권장)**

VS Code를 사용하는 경우 프로젝트 루트의 `.env` 파일에 토큰을 추가합니다:

`.env` 파일이 없으면 `.env.example`을 복사해 `.env`로 만들거나 새로운 `.env` 파일을 프로젝트 루트에 생성하세요.

**예시 `.env` 파일:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env 에서
GITHUB_TOKEN=your_token_here
```

이후 탐색기에서 데모 파일(예: `BasicChatDemo.java`)을 우클릭하고 **"Run Java"**를 선택하거나, 실행 및 디버그 패널의 실행 구성을 사용할 수 있습니다.

**옵션 2: 터미널 사용**

환경 변수로 토큰을 설정합니다:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## 예제 실행하기

**VS Code 사용 시:** 탐색기에서 데모 파일을 우클릭하고 **"Run Java"** 선택하거나 실행 및 디버그 패널의 실행 구성을 사용하세요 (먼저 `.env` 파일에 토큰을 추가해야 합니다).

**Maven 사용 시:** 명령줄에서 다음과 같이 실행할 수 있습니다:

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

제로샷, 퓨샷, 사고의 사슬, 역할 기반 프롬프팅을 보여줍니다.

### 3. 함수 호출

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI가 필요할 때 자동으로 자바 메서드를 호출합니다.

### 4. 문서 Q&A (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

자동 임베딩 및 검색이 포함된 Easy RAG로 문서에 대한 질문을 할 수 있습니다.

### 5. 책임 있는 AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI 안전 필터가 유해한 콘텐츠를 차단하는 방식을 확인할 수 있습니다.

## 각 예제가 보여주는 것

**기본 채팅** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

LangChain4j의 가장 기본적인 모습을 여기서 확인하세요. `OpenAiOfficialChatModel`을 생성하고 `.chat()`으로 프롬프트를 보내며 응답을 받습니다. 이 기본 패턴을 이해하면 나머지 내용은 이 위에 구축됩니다.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 채팅으로 시도해 보기:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)를 열고 묻기:
> - "이 코드에서 GitHub 모델에서 Azure OpenAI로 어떻게 전환하나요?"
> - "OpenAiOfficialChatModel.builder()에서 다른 설정 가능한 매개변수는 무엇인가요?"
> - "완전한 응답을 기다리는 대신 스트리밍 응답을 어떻게 추가하나요?"

**프롬프트 엔지니어링** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

모델과 대화하는 방법을 알았다면, 이제 무엇을 말할지 살펴봅니다. 이 데모는 동일한 모델 설정을 사용하지만 다섯 가지 다른 프롬프트 패턴을 보여줍니다. 직접 지시하는 제로샷, 예제로 학습하는 퓨샷, 추론 단계를 나타내는 사고의 사슬, 문맥을 설정하는 역할 기반 프롬프트를 시도해 보세요. 동일 모델이 어떻게 요청 방식에 따라 크게 다른 결과를 내는지 확인할 수 있습니다.

프롬프트 템플릿도 시연하는데, 변수로 재사용 가능한 프롬프트를 만드는 강력한 방법입니다.
아래 예시는 LangChain4j `PromptTemplate`을 사용해 변수를 채운 프롬프트로, AI가 제공된 목적지와 활동에 따라 답변합니다.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 채팅으로 시도해 보기:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)를 열고 묻기:
> - "제로샷과 퓨샷 프롬프팅의 차이점은 무엇이며 각각 언제 사용해야 하나요?"
> - "온도 파라미터가 모델 응답에 어떻게 영향을 주나요?"
> - "프로덕션에서 프롬프트 인젝션 공격을 방지하는 기술은 무엇이 있나요?"
> - "일반 패턴에 대해 재사용 가능한 PromptTemplate 객체를 어떻게 만들 수 있나요?"

**도구 통합** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

LangChain4j의 강력한 기능을 경험할 부분입니다. `AiServices`를 사용해 자바 메서드를 호출할 수 있는 AI 비서을 만듭니다. 메서드에 `@Tool("설명")`로 주석만 달면 LangChain4j가 나머지를 처리하여 AI가 사용자 요청에 따라 도구를 자동으로 선택해 호출합니다. 액션을 수행하는 AI 구축에 핵심적인 함수 호출 기능을 보여줍니다.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 채팅으로 시도해 보기:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)를 열고 묻기:
> - "@Tool 주석은 어떻게 작동하며 LangChain4j가 뒤에서 무엇을 하나요?"
> - "AI가 복잡한 문제를 풀기 위해 여러 도구를 순차적으로 호출할 수 있나요?"
> - "도구가 예외를 발생시키면 어떻게 오류를 처리해야 하나요?"
> - "이 계산기 예제 대신 실제 API를 통합하려면 어떻게 하나요?"

**문서 Q&A (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

LangChain4j의 "Easy RAG" 접근법을 통한 RAG (검색 증강 생성)를 보게 됩니다. 문서가 로드되어 자동으로 분할 및 임베딩되어 인메모리 저장소에 저장되고, 컨텐츠 검색기가 쿼리 시점에 관련 청크를 AI에 제공합니다. AI는 일반 지식이 아니라 문서 기준으로 답변합니다.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 채팅으로 시도해 보기:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)를 열고 묻기:
> - "모델 훈련 데이터 사용과 비교해 RAG가 AI 허위정보(hallucination)를 어떻게 방지하나요?"
> - "이 쉬운 접근법과 커스텀 RAG 파이프라인의 차이점은 무엇인가요?"
> - "여러 문서나 더 큰 지식 기반을 처리하도록 확장하려면 어떻게 해야 하나요?"

**책임 있는 AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

다층 방어로 AI 안전을 구축합니다. 이 데모에서는 두 가지 보호 계층이 함께 작동하는 방식을 보여줍니다:

**1부: LangChain4j 입력 가드레일** - 위험한 프롬프트가 LLM에 도달하기 전에 차단합니다. 금지된 키워드나 패턴을 검사하는 커스텀 가드레일을 만들 수 있습니다. 코드 내에서 실행되어 빠르고 무료입니다.

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

**2부: 제공자 안전 필터** - GitHub 모델은 가드레일이 놓칠 수 있는 부분을 포착하는 내장 필터를 갖고 있습니다. 심각한 위반에 대해 강력 차단(HTTP 400 오류)과 AI가 정중히 거절하는 완화 차단을 볼 수 있습니다.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 채팅으로 시도해 보기:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)를 열고 묻기:
> - "InputGuardrail이란 무엇이며, 직접 만드는 방법은?"
> - "강력 차단과 완화 거절의 차이점은 무엇인가요?"
> - "왜 가드레일과 제공자 필터를 함께 사용하나요?"

## 다음 단계

**다음 모듈:** [01-introduction - LangChain4j 시작하기](../01-introduction/README.md)

---

**탐색:** [← 메인으로 돌아가기](../README.md) | [다음: Module 01 - 소개 →](../01-introduction/README.md)

---

## 문제 해결

### 처음 Maven 빌드 시

**문제:** 초기 `mvn clean compile` 또는 `mvn package` 실행 시 오래 걸림 (10-15분)

**원인:** Maven이 첫 빌드 시 프로젝트 의존성(스프링 부트, LangChain4j 라이브러리, Azure SDK 등)을 모두 다운로드해야 함

**해결:** 정상적인 동작입니다. 이후 빌드는 의존성 캐시로 인해 훨씬 빠릅니다. 다운로드 속도는 네트워크 상태에 따라 다릅니다.

### PowerShell Maven 명령 구문 오류

**문제:** Maven 명령 실행 시 `Unknown lifecycle phase ".mainClass=..."` 오류 발생
**원인**: PowerShell이 `=` 를 변수 할당 연산자로 해석하여 Maven 속성 구문을 깨뜨림

**해결책**: Maven 명령어 앞에 stop-parsing 연산자 `--%` 를 사용하십시오:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` 연산자는 PowerShell에 나머지 인수를 해석 없이 Maven에 문자 그대로 전달하도록 지시합니다.

### Windows PowerShell 이모지 표시

**문제**: PowerShell에서 AI 응답이 이모지 대신 쓰레기 문자(예: `????` 또는 `â??`)로 표시됨

**원인**: PowerShell 기본 인코딩이 UTF-8 이모지를 지원하지 않음

**해결책**: 자바 애플리케이션 실행 전에 다음 명령을 실행하십시오:
```cmd
chcp 65001
```

이 명령은 터미널에서 UTF-8 인코딩을 강제 적용합니다. 또는 유니코드 지원이 더 좋은 Windows Terminal을 사용하십시오.

### API 호출 디버깅

**문제**: 인증 오류, 속도 제한 또는 AI 모델에서 예상치 못한 응답 발생

**해결책**: 예제에는 API 호출을 콘솔에 표시하기 위해 `.logRequests(true)` 와 `.logResponses(true)` 가 포함되어 있습니다. 이는 인증 오류, 속도 제한 또는 예상치 못한 응답 문제를 해결하는 데 도움이 됩니다. 프로덕션에서는 로그 소음을 줄이기 위해 이 플래그들을 제거하십시오.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 노력하고 있으나 자동 번역에는 오류나 부정확한 부분이 있을 수 있음을 유의하시기 바랍니다. 원본 문서의 원어 버전을 권위 있는 자료로 간주해야 합니다. 중요한 정보의 경우 전문적인 인간 번역을 권장합니다. 이 번역의 사용으로 발생하는 오해나 잘못된 해석에 대해서는 당사가 책임지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->