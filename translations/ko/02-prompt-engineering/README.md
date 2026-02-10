# Module 02: GPT-5.2를 활용한 프롬프트 엔지니어링

## 목차

- [학습 내용](../../../02-prompt-engineering)
- [사전 준비 사항](../../../02-prompt-engineering)
- [프롬프트 엔지니어링 이해하기](../../../02-prompt-engineering)
- [LangChain4j 활용법](../../../02-prompt-engineering)
- [핵심 패턴](../../../02-prompt-engineering)
- [기존 Azure 리소스 사용하기](../../../02-prompt-engineering)
- [애플리케이션 스크린샷](../../../02-prompt-engineering)
- [패턴 탐색](../../../02-prompt-engineering)
  - [낮은 열의 vs 높은 열의](../../../02-prompt-engineering)
  - [작업 실행 (도구 프리앰블)](../../../02-prompt-engineering)
  - [자기 성찰 코드](../../../02-prompt-engineering)
  - [구조화된 분석](../../../02-prompt-engineering)
  - [멀티턴 채팅](../../../02-prompt-engineering)
  - [단계별 추론](../../../02-prompt-engineering)
  - [제한된 출력](../../../02-prompt-engineering)
- [진짜 배우는 것](../../../02-prompt-engineering)
- [다음 단계](../../../02-prompt-engineering)

## 학습 내용

이전 모듈에서는 메모리가 대화형 AI를 어떻게 가능하게 하는지 살펴보고 GitHub 모델을 사용해 기본 상호작용을 구현했습니다. 이제는 Azure OpenAI의 GPT-5.2를 사용하여 질문을 어떻게 던질 것인지, 즉 프롬프트 자체에 집중합니다. 프롬프트를 구성하는 방식이 응답 품질에 큰 영향을 미칩니다.

GPT-5.2를 사용하는 이유는 추론 제어 기능이 도입되어 모델에게 답변 전에 얼마나 생각할지 지시할 수 있기 때문입니다. 이것은 다양한 프롬프트 전략이 더욱 명확해지고 언제 어떤 방식을 써야 할지 이해하는 데 도움이 됩니다. 또한 GPT-5.2는 GitHub 모델보다 Azure에서 더 적은 속도 제한(rate limits)을 갖는 이점도 있습니다.

## 사전 준비 사항

- 모듈 01 완료 (Azure OpenAI 리소스 배포 완료)
- 루트 디렉토리에 Azure 자격 증명이 포함된 `.env` 파일 (`azd up` 명령으로 모듈 01에서 생성)

> **참고:** 모듈 01을 완료하지 않았다면 먼저 그 배포 지침을 따르세요.

## 프롬프트 엔지니어링 이해하기

프롬프트 엔지니어링은 원하는 결과를 일관되게 얻을 수 있게 입력 텍스트를 설계하는 과정입니다. 단순히 질문하는 것을 넘어서서, 모델이 정확히 무엇을 원하는지, 어떻게 전달할지 이해하도록 요청 구조를 만드는 것입니다.

이는 동료에게 지시하는 것과 같습니다. “버그 수정해”는 모호해요. “UserService.java 45행에서 null 체크를 추가하여 null 포인터 예외를 수정해”는 구체적이죠. 언어 모델도 마찬가지로 명확성과 구조가 중요합니다.

## LangChain4j 활용법

이 모듈은 이전 모듈들과 동일한 LangChain4j 기반을 사용하여 고급 프롬프트 패턴을 보여주며, 프롬프트 구조와 추론 제어에 중점을 둡니다.

<img src="../../../translated_images/ko/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*LangChain4j가 프롬프트를 Azure OpenAI GPT-5.2에 연결하는 방식*

**종속성** - 모듈 02는 `pom.xml`에 정의된 다음 langchain4j 종속성을 사용합니다:
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**OpenAiOfficialChatModel 구성** - [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

채팅 모델은 Azure OpenAI 엔드포인트를 지원하는 OpenAI 공식 클라이언트를 사용하여 Spring 빈으로 수동 구성됩니다. 모듈 01과의 주요 차이는 모델 설정이 아니라 `chatModel.chat()`에 전달하는 프롬프트 구조 방식입니다.

**시스템 및 사용자 메시지** - [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j는 메시지 유형을 명확히 구분합니다. `SystemMessage`는 AI의 동작과 컨텍스트를 설정(예: "당신은 코드 리뷰어입니다")하고, `UserMessage`는 실제 요청을 담습니다. 이 구분 덕분에 다양한 사용자 쿼리에도 AI 동작을 일관되게 유지할 수 있습니다.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/ko/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage는 지속적인 컨텍스트를 제공하며 UserMessage는 개별 요청을 포함*

**멀티턴을 위한 MessageWindowChatMemory** - 멀티턴 대화 패턴에서는 모듈 01에서 사용한 `MessageWindowChatMemory`를 재사용합니다. 각 세션은 `Map<String, ChatMemory>`에 자체 메모리 인스턴스를 가지므로 여러 대화를 동시에 진행해도 컨텍스트가 섞이지 않습니다.

**프롬프트 템플릿** - 진짜 초점은 새로운 LangChain4j API가 아니라 프롬프트 엔지니어링입니다. 각 패턴(낮은 열의, 높은 열의, 작업 실행 등)은 동일한 `chatModel.chat(prompt)` 메서드를 사용하되, 세심하게 구조화된 프롬프트 문자열을 전달합니다. XML 태그, 지시문, 포맷팅 모두 프롬프트 텍스트 일부이며 LangChain4j 기능이 아닙니다.

**추론 제어** - GPT-5.2의 추론 노력 정도는 “최대 2단계 추론”, “철저히 탐색” 같은 프롬프트 지침을 통해 조절됩니다. 이는 LangChain4j 설정이 아니라 프롬프트 엔지니어링 기법입니다. 라이브러리는 단순히 프롬프트를 모델에 전달할 뿐입니다.

주요 요점: LangChain4j는 인프라(모델 연결: [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), 메모리 및 메시지 처리: [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java))를 제공하며, 이 모듈은 해당 인프라 내에서 효과적인 프롬프트를 작성하는 방법을 가르칩니다.

## 핵심 패턴

모든 문제에 같은 접근법이 필요한 것은 아닙니다. 어떤 질문은 빠른 답을 필요로 하고, 어떤 것은 깊은 사고를 요구합니다. 어떤 경우는 눈에 보이는 추론 과정을 필요로 하고, 어떤 경우는 결과만 필요합니다. 이 모듈은 각기 다른 상황에 최적화된 8가지 프롬프트 패턴을 다룹니다. 여러분은 모두 실습하며 각각 언제 가장 적합한지 배울 것입니다.

<img src="../../../translated_images/ko/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*8가지 프롬프트 엔지니어링 패턴 개요 및 사용 사례*

<img src="../../../translated_images/ko/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*낮은 열의(빠르고 직설적) vs 높은 열의(철저하고 탐색적) 추론 접근법 비교*

**낮은 열의 (빠르고 집중된)** - 간단한 질문에 대해 빠르고 직설적인 답변을 원할 때 사용합니다. 모델은 최대 2단계 내부 추론만 수행합니다. 계산, 조회, 단순 질문에 적합합니다.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **GitHub Copilot와 함께 탐색하기:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)를 열고 다음 질문을 해보세요:
> - 낮은 열의와 높은 열의 프롬프트 패턴의 차이는 무엇인가요?
> - 프롬프트 내 XML 태그가 AI의 응답 구조화에 어떻게 도움이 되나요?
> - 자기 성찰 패턴과 직접 지시 패턴은 언제 사용해야 하나요?

**높은 열의 (깊고 철저한)** - 복잡한 문제에 대한 포괄적 분석을 원할 때 사용합니다. 모델은 철저히 탐색하고 상세한 추론을 보여줍니다. 시스템 설계, 아키텍처 결정, 복잡한 연구에 적합합니다.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**작업 실행 (단계별 진행)** - 여러 단계를 거치는 워크플로우에 적합합니다. 모델이 사전에 계획을 세우고 각 단계를 내레이션하며, 마지막에 요약을 제공합니다. 마이그레이션, 구현, 기타 다단계 프로세스에 사용하세요.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought 프롬프트는 모델에게 추론 과정을 명시적으로 보여 달라고 요청해 복잡한 작업의 정확도를 높입니다. 단계별 분해는 사람과 AI 모두 논리를 이해하는 데 도움을 줍니다.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 채팅으로 시도해 보기:** 이 패턴에 대해 질문해 보세요:
> - 작업 실행 패턴을 장기 실행 작업에 맞게 어떻게 조정할까요?
> - 프로덕션 애플리케이션에서 도구 프리앰블 구조화의 최선 사례는 무엇인가요?
> - UI에서 중간 진행 상태 업데이트를 어떻게 캡처하고 표시할 수 있나요?

<img src="../../../translated_images/ko/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*계획 → 실행 → 요약의 다단계 작업 워크플로우*

**자기 성찰 코드** - 프로덕션 품질의 코드를 생성하는 데 적합합니다. 모델이 코드를 생성하고 품질 기준에 따라 점검하며 반복적으로 개선합니다. 새 기능이나 서비스를 구축할 때 사용하세요.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ko/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*반복적인 개선 루프 - 생성, 평가, 문제 식별, 개선, 반복*

**구조화된 분석** - 일관된 평가를 위한 패턴입니다. 모델이 고정된 프레임워크(정확성, 관행, 성능, 보안)를 사용하여 코드를 리뷰합니다. 코드 리뷰나 품질 평가에 적합합니다.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 채팅으로 시도해 보기:** 구조화된 분석에 대해 질문해 보세요:
> - 다양한 코드 리뷰 유형에 맞게 분석 프레임워크를 어떻게 맞춤화할 수 있나요?
> - 구조화된 출력을 프로그래밍적으로 파싱하고 처리하는 최선 방법은 무엇인가요?
> - 서로 다른 리뷰 세션 간에 일관된 심각도 수준을 어떻게 보장할 수 있나요?

<img src="../../../translated_images/ko/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*심각도 수준과 함께 네 가지 범주 프레임워크로 일관된 코드 리뷰 제공*

**멀티턴 채팅** - 컨텍스트가 필요한 대화에 적합합니다. 모델은 이전 메시지를 기억하고 이어서 대화를 만듭니다. 대화형 지원 세션이나 복잡한 Q&A에 사용하세요.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ko/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*대화 컨텍스트가 여러 턴에 걸쳐 누적되어 토큰 제한에 도달할 때까지 유지됨*

**단계별 추론** - 눈에 보이는 논리가 필요한 문제에 적합합니다. 모델이 각 단계를 명시적으로 추론 과정을 보여줍니다. 수학 문제, 논리 퍼즐, 사고 과정을 이해하고자 할 때 사용하세요.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ko/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*문제를 명시적인 논리적 단계로 분해*

**제한된 출력** - 특정 형식 요건을 충족하는 응답이 필요할 때 사용합니다. 모델이 형식과 길이 규칙을 엄격하게 준수합니다. 요약이나 정밀한 출력 구조가 요구될 때 적합합니다.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ko/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*특정 형식, 길이 및 구조 요건 강제 적용*

## 기존 Azure 리소스 사용하기

**배포 확인:**

루트 디렉토리에 Azure 자격 증명이 포함된 `.env` 파일이 존재하는지 확인하세요 (모듈 01에서 생성됨):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT을 표시해야 합니다
```

**애플리케이션 시작:**

> **참고:** 만약 모듈 01에서 `./start-all.sh`로 모든 앱을 이미 시작했다면, 이 모듈은 포트 8083에서 이미 실행 중입니다. 아래 시작 명령은 생략하고 http://localhost:8083 로 바로 접속할 수 있습니다.

**옵션 1: Spring Boot 대시보드 사용 (VS Code 사용자 권장)**

개발 컨테이너에는 모든 Spring Boot 앱을 관리하는 시각적 인터페이스를 제공하는 Spring Boot 대시보드 확장 기능이 포함되어 있습니다. VS Code 왼쪽 활동 표시줄에서 스프링 부트 아이콘을 찾아 실행할 수 있습니다.

Spring Boot 대시보드에서는:
- 워크스페이스 내 모든 Spring Boot 앱 목록 확인
- 클릭 한 번으로 앱 시작/중지
- 실시간 애플리케이션 로그 보기
- 애플리케이션 상태 모니터링

"prompt-engineering" 옆에 있는 재생 버튼을 눌러 이 모듈을 시작하거나 모든 모듈을 한꺼번에 시작할 수 있습니다.

<img src="../../../translated_images/ko/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**옵션 2: 셸 스크립트 사용**

모듈 01-04까지 모든 웹 애플리케이션 시작:

**Bash:**
```bash
cd ..  # 루트 디렉토리에서
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # 루트 디렉토리에서
.\start-all.ps1
```

또는 이 모듈만 시작:

**Bash:**
```bash
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

양쪽 스크립트 모두 루트 `.env` 파일로부터 환경 변수를 자동으로 불러오며, JAR 파일이 없으면 빌드합니다.

> **참고:** 시작 전에 수동으로 모든 모듈을 빌드하고 싶다면:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

브라우저에서 http://localhost:8083 을 엽니다.

**중지하려면:**

**Bash:**
```bash
./stop.sh  # 이 모듈만
# 또는
cd .. && ./stop-all.sh  # 모든 모듈
```

**PowerShell:**
```powershell
.\stop.ps1  # 이 모듈만
# 또는
cd ..; .\stop-all.ps1  # 모든 모듈
```

## 애플리케이션 스크린샷

<img src="../../../translated_images/ko/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*특징 및 사용 사례와 함께 8가지 프롬프트 엔지니어링 패턴을 보여주는 메인 대시보드*

## 패턴 탐색

웹 인터페이스를 통해 다양한 프롬프트 전략을 실험할 수 있습니다. 각 패턴은 다른 문제를 해결하니, 직접 사용해 보고 언제 어떤 접근법이 빛나는지 확인하세요.

### 낮은 열의 vs 높은 열의

“200의 15%는 얼마인가요?” 같은 간단한 질문을 낮은 열의로 물어보면 즉각적이고 직설적인 답변을 받게 됩니다. 반면에 “트래픽이 많은 API에 대한 캐싱 전략을 설계하라” 같은 복잡한 질문을 높은 열의로 물으면 모델이 속도를 늦추며 자세한 추론을 제공합니다. 같은 모델, 같은 질문 구조지만 프롬프트가 얼마나 깊게 생각할지 알려주는 차이입니다.
<img src="../../../translated_images/ko/low-eagerness-demo.898894591fb23aa0.webp" alt="낮은 적극성 데모" width="800"/>

*최소한의 추론으로 빠른 계산*

<img src="../../../translated_images/ko/high-eagerness-demo.4ac93e7786c5a376.webp" alt="높은 적극성 데모" width="800"/>

*포괄적 캐싱 전략 (2.8MB)*

### 작업 실행 (도구 서문)

다단계 워크플로우는 사전 계획과 진행 내레이션의 이점을 누립니다. 모델은 수행할 작업을 개요로 제공하고, 각 단계를 내레이션한 후 결과를 요약합니다.

<img src="../../../translated_images/ko/tool-preambles-demo.3ca4881e417f2e28.webp" alt="작업 실행 데모" width="800"/>

*단계별 내레이션으로 REST 엔드포인트 생성 (3.9MB)*

### 자기반성 코드

"이메일 검증 서비스 생성"을 시도해 보세요. 단순히 코드를 생성하고 중단하는 대신, 모델은 생성, 품질 기준에 대한 평가, 약점 식별, 개선을 수행합니다. 코드는 생산 기준에 부합할 때까지 반복됩니다.

<img src="../../../translated_images/ko/self-reflecting-code-demo.851ee05c988e743f.webp" alt="자기반성 코드 데모" width="800"/>

*완성된 이메일 검증 서비스 (5.2MB)*

### 구조화된 분석

코드 리뷰는 일관된 평가 프레임워크가 필요합니다. 모델은 고정된 카테고리(정확성, 관행, 성능, 보안)와 심각도 수준을 사용하여 코드를 분석합니다.

<img src="../../../translated_images/ko/structured-analysis-demo.9ef892194cd23bc8.webp" alt="구조화된 분석 데모" width="800"/>

*프레임워크 기반 코드 리뷰*

### 다중 턴 채팅

"Spring Boot가 무엇인가요?"라고 묻고 곧바로 "예제를 보여주세요"라고 질문하세요. 모델은 첫 질문을 기억하고 특정한 Spring Boot 예제를 제공합니다. 메모리가 없으면 두 번째 질문은 너무 모호했을 것입니다.

<img src="../../../translated_images/ko/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="다중 턴 채팅 데모" width="800"/>

*질문 간 맥락 보존*

### 단계별 추론

수학 문제를 선택한 후 단계별 추론과 낮은 적극성으로 시도해 보세요. 낮은 적극성은 단지 빠르게 답을 제공하지만 불투명합니다. 단계별 추론은 모든 계산과 결정을 보여줍니다.

<img src="../../../translated_images/ko/step-by-step-reasoning-demo.12139513356faecd.webp" alt="단계별 추론 데모" width="800"/>

*명확한 단계가 포함된 수학 문제*

### 제약된 출력

특정 형식이나 단어 수가 필요할 때 이 패턴은 엄격한 준수를 강제합니다. 정확히 100단어의 요약을 불릿 포인트 형식으로 생성해 보세요.

<img src="../../../translated_images/ko/constrained-output-demo.567cc45b75da1633.webp" alt="제약된 출력 데모" width="800"/>

*형식 제어가 적용된 머신러닝 요약*

## 여러분이 실제로 배우는 것

**추론 노력은 모든 것을 바꿉니다**

GPT-5.2는 프롬프트를 통해 계산 노력을 제어할 수 있습니다. 낮은 노력은 최소한의 탐색으로 빠른 응답을 의미합니다. 높은 노력은 모델이 깊게 생각할 시간을 갖는 것을 뜻합니다. 여러분은 작업 복잡도에 맞춰 노력을 조절하는 법을 배우고 있습니다 - 단순 질문에 시간을 낭비하지 말고 복잡한 결정은 서두르지 마세요.

**구조는 행동을 안내합니다**

프롬프트에 XML 태그가 있는 것을 주목했나요? 이것들은 장식용이 아닙니다. 모델은 자유 형식 텍스트보다 구조화된 지시를 더 신뢰성 있게 따릅니다. 다단계 프로세스나 복잡한 논리가 필요할 때 구조는 모델이 어디에 있고 다음에 무엇을 해야 하는지 추적하는 데 도움을 줍니다.

<img src="../../../translated_images/ko/prompt-structure.a77763d63f4e2f89.webp" alt="프롬프트 구조" width="800"/>

*명확한 섹션과 XML 스타일 조직이 포함된 잘 구성된 프롬프트 해부*

**자기평가를 통한 품질**

자기반성 패턴은 품질 기준을 명확히 함으로써 작동합니다. 모델이 "잘 하길" 기대하는 대신 "잘함"이 무엇인지 정확히 알립니다: 올바른 논리, 오류 처리, 성능, 보안. 모델은 자신의 출력을 평가하고 개선할 수 있습니다. 이것은 코드 생성을 복권에서 프로세스로 전환합니다.

**맥락은 유한합니다**

다중 턴 대화는 각 요청에 메시지 기록을 포함시켜 작동합니다. 하지만 한계가 있습니다 - 모든 모델은 최대 토큰 수가 정해져 있습니다. 대화가 길어질수록 관련 맥락을 유지하는 전략이 필요합니다. 이 모듈은 메모리가 어떻게 작동하는지 보여주며; 이후에 요약 시점, 잊어야 할 때, 불러와야 할 때를 배울 것입니다.

## 다음 단계

**다음 모듈:** [03-rag - RAG (검색 증강 생성)](../03-rag/README.md)

---

**탐색:** [← 이전: 모듈 01 - 소개](../01-introduction/README.md) | [메인으로 돌아가기](../README.md) | [다음: 모듈 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 노력하고 있으나, 자동 번역에는 오류나 부정확성이 포함될 수 있음을 유의해 주시기 바랍니다. 원문은 원어로 작성된 문서가 권위 있는 자료로 간주되어야 합니다. 중요한 정보의 경우 전문적인 인간 번역을 권장합니다. 본 번역으로 인한 오해나 잘못된 해석에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->