# Module 00: 빠른 시작

## 목차

- [소개](../../../00-quick-start)
- [LangChain4j란?](../../../00-quick-start)
- [LangChain4j 의존성](../../../00-quick-start)
- [사전 준비물](../../../00-quick-start)
- [설정](../../../00-quick-start)
  - [1. GitHub 토큰 받기](../../../00-quick-start)
  - [2. 토큰 설정하기](../../../00-quick-start)
- [예제 실행하기](../../../00-quick-start)
  - [1. 기본 채팅](../../../00-quick-start)
  - [2. 프롬프트 패턴](../../../00-quick-start)
  - [3. 함수 호출](../../../00-quick-start)
  - [4. 문서 Q&A (RAG)](../../../00-quick-start)
  - [5. 책임 있는 AI](../../../00-quick-start)
- [각 예제가 보여주는 것](../../../00-quick-start)
- [다음 단계](../../../00-quick-start)
- [문제 해결](../../../00-quick-start)

## 소개

이 빠른 시작 가이드는 LangChain4j를 최대한 빨리 실행하고 활용할 수 있도록 설계되었습니다. LangChain4j와 GitHub 모델을 사용해 AI 애플리케이션을 만드는 기본 사항을 다룹니다. 이후 모듈에서는 Azure OpenAI와 LangChain4j를 사용해 더 복잡한 애플리케이션을 구축할 것입니다.

## LangChain4j란?

LangChain4j는 AI 기반 애플리케이션 구축을 간소화하는 자바 라이브러리입니다. HTTP 클라이언트나 JSON 파싱 작업 없이 깔끔한 자바 API로 작업할 수 있습니다.

LangChain의 "체인"은 여러 구성요소를 연결하는 것을 의미합니다. 예를 들어 프롬프트를 모델에 연결하고, 다시 파서에 연결하거나 여러 AI 호출을 순차적으로 연결해 한 출력이 다음 입력으로 들어가는 방식입니다. 이 빠른 시작은 복잡한 체인보다 기본 개념에 집중합니다.

<img src="../../../translated_images/ko/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j 구성요소 연결 - 강력한 AI 워크플로우를 위한 빌딩 블록*

우리는 세 가지 핵심 구성요소를 사용할 것입니다:

**ChatLanguageModel** - AI 모델 상호작용 인터페이스입니다. `model.chat("prompt")`를 호출하면 응답 문자열을 얻습니다. 여기서는 GitHub 모델 같은 OpenAI 호환 엔드포인트와 작동하는 `OpenAiOfficialChatModel`을 사용합니다.

**AiServices** - 타입 안전 AI 서비스 인터페이스를 만듭니다. 메서드를 정의하고 `@Tool`로 주석 처리하면 LangChain4j가 오케스트레이션을 처리합니다. AI가 필요할 때 자동으로 자바 메서드를 호출합니다.

**MessageWindowChatMemory** - 대화 기록을 유지합니다. 없으면 각 요청은 독립적입니다. 있으면 AI가 이전 메시지를 기억하며 여러 턴 동안 문맥을 유지합니다.

<img src="../../../translated_images/ko/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j 아키텍처 - 핵심 구성요소가 협력하여 AI 애플리케이션 구동*

## LangChain4j 의존성

이 빠른 시작은 [`pom.xml`](../../../00-quick-start/pom.xml) 내 두 Maven 의존성을 사용합니다:

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
```

`langchain4j-open-ai-official` 모듈은 OpenAI 호환 API에 연결하는 `OpenAiOfficialChatModel` 클래스를 제공합니다. GitHub 모델은 동일 API 형식을 사용하므로 별도의 어댑터가 필요 없고, 베이스 URL만 `https://models.github.ai/inference`로 지정하면 됩니다.

## 사전 준비물

**개발 컨테이너 사용 중인가요?** Java와 Maven이 이미 설치되어 있습니다. GitHub 개인 액세스 토큰만 필요합니다.

**로컬 개발 환경:**
- Java 21 이상, Maven 3.9 이상
- GitHub 개인 액세스 토큰 (아래 지침 참고)

> **참고:** 이 모듈은 GitHub 모델에서 `gpt-4.1-nano`를 사용합니다. 코드 내 모델 이름을 수정하지 마세요 - GitHub에서 제공하는 모델에 맞게 구성되어 있습니다.

## 설정

### 1. GitHub 토큰 받기

1. [GitHub 설정 → 개인 액세스 토큰](https://github.com/settings/personal-access-tokens)으로 이동
2. "새 토큰 생성" 클릭
3. 설명이 포함된 이름 지정 (예: "LangChain4j Demo")
4. 만료기간 설정 (7일 권장)
5. "계정 권한"에서 "Models" 찾아 "읽기 전용" 설정
6. "토큰 생성" 클릭
7. 토큰을 복사해 저장하세요 - 다시는 볼 수 없습니다

### 2. 토큰 설정하기

**옵션 1: VS Code 사용 (권장)**

VS Code를 사용하는 경우 프로젝트 루트에 `.env` 파일에 토큰을 추가하세요:

`.env` 파일이 없으면 `.env.example`을 복사해서 사용하거나 새 `.env` 파일을 만드세요.

**예제 `.env` 파일:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env 안에 있음
GITHUB_TOKEN=your_token_here
```

이후 탐색기에서 아무 데모 파일(e.g. `BasicChatDemo.java`)을 우클릭하고 **"Run Java"** 선택하거나 실행 및 디버그 패널의 실행 구성을 사용할 수 있습니다.

**옵션 2: 터미널 사용**

환경 변수로 토큰을 설정하세요:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## 예제 실행하기

**VS Code 사용 시:** 탐색기에서 데모 파일을 우클릭하고 **"Run Java"** 선택하거나 실행 및 디버그 패널에서 실행 구성 사용 (토큰을 `.env`에 추가했는지 확인).

**Maven 사용 시:** 다음 명령어를 터미널에서 실행할 수 있습니다.

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

제로샷, 퓨샷, 사고 연쇄, 역할 기반 프롬프트를 보여줍니다.

### 3. 함수 호출

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

필요 시 AI가 자동으로 자바 메서드를 호출합니다.

### 4. 문서 Q&A (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

`document.txt` 내용에 대해 질문할 수 있습니다.

### 5. 책임 있는 AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI 안전 필터가 어떻게 해로운 콘텐츠를 차단하는지 볼 수 있습니다.

## 각 예제가 보여주는 것

**기본 채팅** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

LangChain4j의 가장 단순한 사용법을 보여줍니다. `OpenAiOfficialChatModel`을 생성하고 `.chat()`으로 프롬프트를 보낸 뒤 응답을 받습니다. 모델 초기화, 사용자 지정 엔드포인트 및 API 키 설정 기본 구조를 이해할 수 있습니다. 이 패턴을 알면 다른 기능도 쉽게 확장할 수 있습니다.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 GitHub Copilot** Chat으로 시도해 보세요: [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)를 열고 질문해 보세요:
> - "이 코드에서 GitHub Models 대신 Azure OpenAI를 사용하려면 어떻게 하나요?"
> - "OpenAiOfficialChatModel.builder()에서 설정할 수 있는 다른 파라미터는 무엇인가요?"
> - "완전한 응답을 기다리지 않고 스트리밍 응답을 추가하려면 어떻게 하나요?"

**프롬프트 엔지니어링** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

모델과 대화하는 방법을 알았으니, 어떤 말을 할지 탐색합니다. 같은 모델 설정을 쓰면서도 다섯 가지 프롬프트 패턴을 보여줍니다. 직접 명령하는 제로샷, 예제로 학습하는 퓨샷, 추론 과정을 보여주는 사고 연쇄, 문맥 설정용 역할 기반 프롬프트 등이 있습니다. 프레이밍에 따라 같은 모델이라도 결과가 크게 달라지는 모습을 볼 수 있습니다.

또 재사용 가능한 프롬프트 템플릿의 강력함도 시연합니다. 아래 예제는 LangChain4j `PromptTemplate`을 이용해 변수에 값을 채워 질문하는 프롬프트입니다. AI가 제공된 목적지와 활동에 따라 대답합니다.

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

> **🤖 GitHub Copilot** Chat으로 시도해 보세요: [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)를 열고 질문해 보세요:
> - "제로샷과 퓨샷 프롬프트의 차이와 각각 언제 사용하는 게 좋은가요?"
> - "온도 매개변수가 모델 응답에 어떻게 영향을 주나요?"
> - "운영 환경에서 프롬프트 인젝션 공격을 방지하는 기법에는 어떤 것들이 있나요?"
> - "공통 패턴에 재사용 가능한 PromptTemplate 객체는 어떻게 만들 수 있나요?"

**툴 통합** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

LangChain4j가 강력해지는 부분입니다. `AiServices`를 사용해 AI 어시스턴트를 만들고, 자바 메서드를 호출할 수 있습니다. 메서드에 `@Tool("설명")` 어노테이션만 달면 나머지를 LangChain4j가 관리합니다. AI가 사용자의 요구에 따라 언제 도구를 사용할지 자동 결정합니다. 함수 호출을 활용한 AI 행동 수행 기술을 시연합니다.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 GitHub Copilot** Chat으로 시도해 보세요: [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)를 열고 질문해 보세요:
> - "@Tool 어노테이션은 어떻게 작동하며 LangChain4j는 뒤에서 무엇을 하나요?"
> - "AI가 여러 도구를 순차적으로 호출해 복잡한 문제를 해결할 수 있나요?"
> - "도구가 예외를 던지면 어떻게 처리해야 하나요?"
> - "이 계산기 예제 대신 실제 API는 어떻게 통합하나요?"

**문서 Q&A (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

RAG(검색 강화 생성)의 기초를 보여줍니다. 모델 훈련 데이터 대신 [`document.txt`](../../../00-quick-start/document.txt) 문서 내용을 로드해 프롬프트에 포함시킵니다. AI가 일반 지식이 아닌 문서에 기반해 답변합니다. 자신만의 데이터로 작동하는 시스템 구축의 첫걸음입니다.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **참고:** 이 간단한 방식은 문서를 전부 프롬프트에 로드합니다. 큰 파일(10KB 이상)은 컨텍스트 제한을 초과할 수 있습니다. 모듈 03에서 생산용 RAG 시스템을 위한 청킹과 벡터 검색을 다룹니다.

> **🤖 GitHub Copilot** Chat으로 시도해 보세요: [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) 열고 질문해 보세요:
> - "RAG가 어떻게 모델 훈련 데이터만 사용할 때보다 AI 환각을 방지하나요?"
> - "이 간단한 방식과 벡터 임베딩 검색 사용 방식 차이는 무엇인가요?"
> - "여러 문서나 더 큰 지식 베이스를 처리하려면 어떻게 확장해야 하나요?"
> - "프롬프트가 제공된 문맥만 사용하도록 구성하는 최선의 방법은 무엇인가요?"

**책임 있는 AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

다층 방어로 AI 안전을 구축합니다. 두 가지 보호 레이어가 함께 작동하는 모습을 보여줍니다:

**1부: LangChain4j 입력 가드레일** - 위험한 프롬프트가 LLM에 도달하기 전에 차단합니다. 금지된 키워드나 패턴을 검사하는 맞춤형 가드레일을 만들 수 있습니다. 코드 내에서 실행되어 빠르고 무료입니다.

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

**2부: 공급자 안전 필터** - GitHub 모델에는 가드레일이 놓친 부분을 포착하는 내장 필터가 있습니다. 심각한 위반 시 HTTP 400 오류 같은 강제 차단, 또는 AI가 정중히 거부하는 소프트 거부를 확인할 수 있습니다.

> **🤖 GitHub Copilot** Chat으로 시도해 보세요: [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) 열고 질문해 보세요:
> - "InputGuardrail이란 무엇이며 나만의 가드레일은 어떻게 만드나요?"
> - "강제 차단과 소프트 거부의 차이는 무엇인가요?"
> - "가드레일과 공급자 필터를 함께 사용하는 이유는 무엇인가요?"

## 다음 단계

**다음 모듈:** [01-introduction - LangChain4j와 Azure에서 gpt-5 시작하기](../01-introduction/README.md)

---

**탐색:** [← 메인으로 돌아가기](../README.md) | [다음: Module 01 - 소개 →](../01-introduction/README.md)

---

## 문제 해결

### 첫 빌드 시 Maven 문제

**문제:** 초기 `mvn clean compile` 또는 `mvn package` 명령이 오래 걸림 (10-15분)

**원인:** Maven이 첫 빌드에서 프로젝트 의존성(스프링 부트, LangChain4j 라이브러리, Azure SDK 등)을 모두 다운로드해야 함

**해결책:** 정상 동작입니다. 이후 빌드는 로컬에 의존성이 캐싱되어 훨씬 빨라집니다. 다운로드 시간은 네트워크 속도에 따라 다릅니다.
### PowerShell Maven 명령 구문

**문제**: `Unknown lifecycle phase ".mainClass=..."` 오류와 함께 Maven 명령이 실패함

**원인**: PowerShell이 `=` 를 변수 할당 연산자로 해석하여 Maven 속성 구문이 깨짐

**해결 방법**: Maven 명령 앞에 중지 구문 연산자 `--%` 를 사용:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` 연산자는 PowerShell에 나머지 인수를 해석 없이 그대로 Maven에 전달하도록 지시합니다.

### Windows PowerShell 이모지 표시

**문제**: PowerShell에서 이모지가 아닌 쓰레기 문자(예: `????` 또는 `â??`)가 AI 응답에 나타남

**원인**: PowerShell 기본 인코딩이 UTF-8 이모지를 지원하지 않음

**해결 방법**: Java 애플리케이션 실행 전에 다음 명령을 실행:
```cmd
chcp 65001
```

이 명령은 터미널에서 UTF-8 인코딩을 강제합니다. 또는 더 나은 유니코드 지원이 있는 Windows Terminal을 사용하세요.

### API 호출 디버깅

**문제**: 인증 오류, 속도 제한, AI 모델의 예기치 않은 응답

**해결 방법**: 예제에는 API 호출을 콘솔에 표시하기 위해 `.logRequests(true)` 와 `.logResponses(true)` 가 포함되어 있습니다. 이는 인증 오류, 속도 제한 또는 예기치 않은 응답 문제를 해결하는 데 도움이 됩니다. 운영 환경에서는 로그 소음을 줄이기 위해 이 플래그를 제거하세요.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 노력하고 있으나, 자동 번역은 오류나 부정확한 표현을 포함할 수 있음을 유의하시기 바랍니다. 원문 문서가 권위 있는 출처로 간주되어야 합니다. 중요한 정보의 경우 전문 인간 번역을 권장합니다. 이 번역 사용으로 인해 발생하는 오해나 잘못된 해석에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->