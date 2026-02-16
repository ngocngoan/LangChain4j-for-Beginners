# Module 02: GPT-5.2를 활용한 프롬프트 엔지니어링

## 목차

- [학습 내용](../../../02-prompt-engineering)
- [사전 요구사항](../../../02-prompt-engineering)
- [프롬프트 엔지니어링 이해하기](../../../02-prompt-engineering)
- [프롬프트 엔지니어링 기초](../../../02-prompt-engineering)
  - [제로샷 프롬프트](../../../02-prompt-engineering)
  - [퓨샷 프롬프트](../../../02-prompt-engineering)
  - [생각의 사슬](../../../02-prompt-engineering)
  - [역할 기반 프롬프트](../../../02-prompt-engineering)
  - [프롬프트 템플릿](../../../02-prompt-engineering)
- [고급 패턴](../../../02-prompt-engineering)
- [기존 Azure 리소스 사용하기](../../../02-prompt-engineering)
- [애플리케이션 스크린샷](../../../02-prompt-engineering)
- [패턴 탐구하기](../../../02-prompt-engineering)
  - [낮은 의욕 vs 높은 의욕](../../../02-prompt-engineering)
  - [작업 실행 (도구 전제조건)](../../../02-prompt-engineering)
  - [자기성찰 코드](../../../02-prompt-engineering)
  - [구조화된 분석](../../../02-prompt-engineering)
  - [다중 대화](../../../02-prompt-engineering)
  - [단계별 추론](../../../02-prompt-engineering)
  - [제한된 출력](../../../02-prompt-engineering)
- [진정으로 배우는 것](../../../02-prompt-engineering)
- [다음 단계](../../../02-prompt-engineering)

## 학습 내용

<img src="../../../translated_images/ko/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

이전 모듈에서는 메모리가 대화형 AI에 어떻게 작용하는지 살펴보고 GitHub 모델로 기본 상호작용을 경험했습니다. 이제는 질문하는 방식, 즉 프롬프트 자체에 집중합니다 — Azure OpenAI의 GPT-5.2를 사용하여 프롬프트를 구성하는 방법입니다. 프롬프트를 어떻게 구조화하느냐에 따라 응답 품질이 크게 달라집니다. 먼저 기본 프롬프트 기술을 복습한 후 GPT-5.2의 기능을 최대한 활용하는 8가지 고급 패턴으로 나아갑니다.

GPT-5.2를 사용하는 이유는 추론 제어 기능을 도입했기 때문입니다 — 모델이 대답하기 전에 얼마나 많이 생각할지 지시할 수 있습니다. 이를 통해 다양한 프롬프트 전략의 차이를 더 명확히 하고 각 접근법을 언제 사용해야 하는지 이해하기 쉽습니다. 또한 Azure에서는 GitHub 모델보다 GPT-5.2에 대한 요율 제한이 적어 더 유리합니다.

## 사전 요구사항

- 모듈 01 완료 (Azure OpenAI 리소스 배포 완료)
- 루트 디렉터리에 Azure 자격 증명이 포함된 `.env` 파일 (모듈 01에서 `azd up` 명령어를 통해 생성됨)

> **참고:** 모듈 01을 완료하지 않았다면 먼저 그 모듈의 배포 지침을 따라주세요.

## 프롬프트 엔지니어링 이해하기

<img src="../../../translated_images/ko/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

프롬프트 엔지니어링은 원하는 결과를 일관성 있게 얻기 위해 입력 텍스트를 설계하는 작업입니다. 단순히 질문을 던지는 것이 아니라, 모델이 무엇을 원하는지, 어떻게 답변해야 하는지를 정확히 이해하도록 요청을 구조화하는 것입니다.

동료에게 지시하는 것과 비슷합니다. "버그를 고쳐"라는 말은 모호합니다. 하지만 "UserService.java 45번째 줄에서 null 체크를 추가하여 널 포인터 예외를 고쳐"라고 하면 구체적입니다. 언어 모델도 마찬가지입니다 — 구체성과 구조가 중요합니다.

<img src="../../../translated_images/ko/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j는 인프라스트럭처 — 모델 연결, 메모리, 메시지 타입 — 를 제공하며, 프롬프트 패턴은 그 인프라를 통해 전송하는 세심하게 구조화된 텍스트입니다. 주요 구성 요소는 AI의 행동과 역할을 설정하는 `SystemMessage`와 실제 요청을 담는 `UserMessage`입니다.

## 프롬프트 엔지니어링 기초

<img src="../../../translated_images/ko/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

이 모듈의 고급 패턴에 들어가기 전에, 다섯 가지 기본적인 프롬프트 기법을 복습해봅시다. 이는 모든 프롬프트 엔지니어가 알아야 할 기본 빌딩 블록입니다. 만약 [빠른 시작 모듈](../00-quick-start/README.md#2-prompt-patterns)을 이미 진행했다면, 이 기법들을 실제로 경험해봤을 것입니다 — 여기에 그 개념적 틀이 담겨 있습니다.

### 제로샷 프롬프트

가장 간단한 방법: 예시 없이 모델에 직접 명령을 내립니다. 모델은 전적으로 학습된 내용을 바탕으로 작업을 이해하고 수행합니다. 기대 동작이 명확한 직관적인 요청에 적합합니다.

<img src="../../../translated_images/ko/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*예시 없이 직접 지시 — 명령만으로 작업을 유추하는 모델*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 응답: "긍정적"
```

**사용 시기:** 단순 분류, 직접 질문, 번역, 또는 추가 안내 없이 모델이 처리 가능한 모든 작업에 적합.

### 퓨샷 프롬프트

모델이 따라야 할 패턴을 보여주는 예시를 제공합니다. 모델은 예시를 통해 예상 입력-출력 형식을 학습하고 새 입력에 적용합니다. 원하는 형식이나 동작이 명확하지 않은 작업에서 일관성을 크게 높입니다.

<img src="../../../translated_images/ko/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*예시로 학습 — 모델이 패턴을 파악하고 새 입력에 적용*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**사용 시기:** 맞춤 분류, 일관된 포맷, 도메인 특화 작업, 또는 제로샷 결과가 불안정할 때.

### 생각의 사슬

모델에게 추론 과정을 단계별로 보여달라고 요청합니다. 바로 답변하는 대신 문제가 각 부분별로 나누어 명확하게 해결됩니다. 수학, 논리 문제 및 다단계 추론 작업에서 정확도가 향상됩니다.

<img src="../../../translated_images/ko/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*단계별 추론 — 복잡한 문제를 명확한 논리 단계로 분해*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 모델은 다음을 보여줍니다: 15 - 8 = 7, 그 다음 7 + 12 = 19개의 사과
```

**사용 시기:** 수학 문제, 논리 퍼즐, 디버깅 또는 추론 과정 공개가 정확성과 신뢰성을 높일 때.

### 역할 기반 프롬프트

질문 전에 AI에게 페르소나나 역할을 설정합니다. 이는 응답의 톤, 깊이, 초점을 결정하는 맥락을 제공합니다. "소프트웨어 아키텍트"는 "주니어 개발자" 또는 "보안 감사관"과 전혀 다른 조언을 합니다.

<img src="../../../translated_images/ko/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*맥락과 역할 설정 — 같은 질문이라도 할당된 역할에 따라 다른 답변*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**사용 시기:** 코드 리뷰, 튜터링, 도메인 분석, 특정 전문성이나 관점에 맞춘 답변이 필요할 때.

### 프롬프트 템플릿

재사용 가능한 프롬프트를 변수 자리 표시자로 만듭니다. 매번 새 프롬프트를 작성하는 대신 템플릿을 한 번 정의하고 다양한 값으로 채울 수 있습니다. LangChain4j의 `PromptTemplate` 클래스는 `{{variable}}` 구문으로 이를 쉽게 지원합니다.

<img src="../../../translated_images/ko/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*변수 자리표시자를 포함한 재사용 가능한 프롬프트 — 하나의 템플릿, 다양한 사용*

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

**사용 시기:** 서로 다른 입력으로 반복 질의, 배치 처리, 재사용 가능한 AI 워크플로우 구축 또는 프롬프트 구조는 동일하지만 데이터만 바뀌는 시나리오.

---

이 다섯 가지 기본기는 대부분의 프롬프트 작업에 견고한 도구를 제공합니다. 나머지 모듈은 GPT-5.2의 추론 제어, 자기 평가, 구조화된 출력 기능을 활용하는 **8가지 고급 패턴**을 기반으로 합니다.

## 고급 패턴

기본기를 다뤘으니 이제 이 모듈을 특별하게 만드는 8가지 고급 패턴으로 넘어가겠습니다. 모든 문제에 동일한 접근법이 필요한 것은 아닙니다. 어떤 질문은 빠른 답변이 필요하고, 어떤 것은 깊은 사고가 필요합니다. 어떤 것은 명시적인 추론이 필요하고, 어떤 것은 결과만 필요합니다. 아래 각 패턴은 다른 시나리오에 맞게 최적화되어 있으며, GPT-5.2의 추론 제어로 차이가 더욱 뚜렷해집니다.

<img src="../../../translated_images/ko/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*8가지 프롬프트 엔지니어링 패턴과 그 사용 사례 개요*

<img src="../../../translated_images/ko/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2의 추론 제어는 모델이 얼마나 깊이 생각할지 지정할 수 있게 해줌 — 빠르고 직접적인 답변부터 깊이 있는 탐색까지*

<img src="../../../translated_images/ko/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*낮은 의욕(빠르고 직접적) vs 높은 의욕(철저하고 탐구적인) 추론 방식 비교*

**낮은 의욕 (빠르고 집중된)** - 빠르고 직접적인 답변이 필요한 단순 질문에 적합합니다. 모델이 최소 2단계만 추론합니다. 계산, 조회, 혹은 직관적인 질문에 사용하세요.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **GitHub Copilot과 함께 탐구해보기:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 파일을 열고 다음 질문을 해보세요:
> - "낮은 의욕과 높은 의욕 프롬프트 패턴의 차이는 무엇인가?"
> - "프롬프트 내 XML 태그가 AI 응답 구조에 어떻게 도움을 주나?"
> - "자기성찰 패턴과 직접 명령 패턴은 언제 사용해야 하나?"

**높은 의욕 (깊고 철저한)** - 포괄적 분석이 필요한 복잡한 문제에 적합합니다. 모델이 깊게 탐구하며 상세한 추론을 보여줍니다. 시스템 설계, 아키텍처 결정, 복잡한 연구에 사용하세요.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**작업 실행 (단계별 진행)** - 다단계 워크플로우용. 모델이 사전에 계획을 제공하고 작업하며 각 단계를 설명, 최종적으로 요약합니다. 마이그레이션, 구현, 다단계 프로세스에 적합합니다.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought 프롬프트는 모델에게 추론 과정을 명시적으로 보여달라고 요청해 복잡한 문제의 정확도를 높입니다. 단계별 분석은 인간과 AI 모두 논리를 이해하는 데 도움이 됩니다.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat과 함께 시도해보기:** 다음 질문을 해보세요:
> - "장기 실행 작업을 위한 작업 실행 패턴을 어떻게 수정할 수 있나요?"
> - "프로덕션 애플리케이션에서 도구 전제조건을 구조화하는 모범 사례는?"
> - "UI에서 중간 진행 상황 업데이트를 캡처하고 표시하려면 어떻게 해야 하나요?"

<img src="../../../translated_images/ko/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*계획 → 실행 → 요약 워크플로우로 다단계 작업 처리*

**자기성찰 코드** - 프로덕션 수준 코드를 생성할 때 사용합니다. 모델이 코드를 생성하고 품질 기준에 따라 검증한 뒤 반복적으로 개선합니다. 새 기능이나 서비스를 구축할 때 적합합니다.

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

*반복 개선 루프 - 생성, 평가, 문제 발견, 개선, 반복*

**구조화된 분석** - 일관된 평가를 위해 고정된 프레임워크(정확성, 관행, 성능, 보안)를 사용해 코드를 검토합니다. 코드 리뷰나 품질 평가에 유용합니다.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat과 함께 시도해보기:** 구조화된 분석에 대해 질문해보세요:
> - "다양한 코드 리뷰 유형에 맞춰 분석 프레임워크를 어떻게 맞춤화할 수 있나요?"
> - "구조화된 출력을 프로그래밍적으로 파싱하고 처리하는 최선의 방법은?"
> - "다른 리뷰 세션 간 심각도 수준을 일관되게 유지하려면 어떻게 해야 하나요?"

<img src="../../../translated_images/ko/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*심각도 수준을 포함한 4가지 분류 체계로 일관된 코드 리뷰 수행*

**다중 대화** - 맥락이 필요한 대화용. 모델이 이전 메시지를 기억하고 그 위에 답변을 구축합니다. 인터랙티브 도움말 세션이나 복잡한 Q&A에 적합합니다.

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

*토큰 한도에 도달할 때까지 여러 회차에 걸쳐 누적되는 대화 맥락*

**단계별 추론** - 명확한 논리가 필요한 문제용. 모델이 각 단계별로 명시적인 추론 과정을 보여줍니다. 수학 문제, 논리 퍼즐, 사고 과정을 이해해야 할 때 적합합니다.

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

*문제를 명확한 논리 단계로 분할*

**제한된 출력** - 특정 형식 요구사항이 있는 응답에 사용합니다. 모델이 형식과 길이 규칙을 엄격히 따릅니다. 요약이나 정확한 출력 구조가 필요할 때 유용합니다.

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

*특정 형식, 길이, 구조 요구사항 강제*

## 기존 Azure 리소스 사용하기

**배포 확인하기:**

루트 디렉터리에 Azure 자격 증명이 포함된 `.env` 파일이 존재하는지 확인하세요 (모듈 01에서 생성됨):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT를 표시해야 합니다
```

**애플리케이션 시작하기:**

> **참고:** 이미 모듈 01에서 `./start-all.sh` 명령어로 모든 애플리케이션을 시작했다면, 이 모듈은 포트 8083에서 이미 실행 중입니다. 아래 시작 명령은 건너뛰고 바로 http://localhost:8083 으로 접속하세요.

**옵션 1: Spring Boot 대시보드 사용 (VS Code 사용자 권장)**

개발 컨테이너에는 모든 Spring Boot 애플리케이션을 관리할 수 있는 시각적 인터페이스인 Spring Boot 대시보드 확장 기능이 포함되어 있습니다. VS Code 왼쪽의 액티비티 바에서 Spring Boot 아이콘을 찾아 확인할 수 있습니다.
Spring Boot 대시보드에서 다음을 할 수 있습니다:
- 작업 공간에 있는 모든 Spring Boot 애플리케이션 보기
- 클릭 한 번으로 애플리케이션 시작/중지
- 실시간으로 애플리케이션 로그 보기
- 애플리케이션 상태 모니터링

"prompt-engineering" 옆에 있는 재생 버튼을 클릭하여 이 모듈을 시작하거나 모든 모듈을 한 번에 시작하세요.

<img src="../../../translated_images/ko/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**옵션 2: 셸 스크립트 사용하기**

모든 웹 애플리케이션(모듈 01-04) 시작:

**Bash:**
```bash
cd ..  # 루트 디렉터리에서
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # 루트 디렉토리에서부터
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

두 스크립트 모두 루트 `.env` 파일에서 환경 변수를 자동으로 로드하며, JAR가 없으면 빌드합니다.

> **참고:** 모든 모듈을 수동으로 빌드한 후 시작하려면:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

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

*모든 8가지 프롬프트 엔지니어링 패턴과 그 특성 및 사용 사례를 보여주는 메인 대시보드*

## 패턴 탐색하기

웹 인터페이스를 사용하여 다양한 프롬프트 전략을 실험해 보세요. 각 패턴은 다른 문제를 해결합니다. 언제 각 접근법이 빛나는지 직접 확인해보세요.

### 낮은 열의 vs 높은 열의

"200의 15%는 얼마인가요?"와 같은 간단한 질문을 낮은 열의로 해보세요. 즉각적이고 직접적인 답변을 받을 수 있습니다. 이제 "고트래픽 API용 캐싱 전략 설계"와 같은 복잡한 질문을 높은 열의로 해보세요. 모델이 천천히 자세한 추론을 제공하는 모습을 볼 수 있습니다. 같은 모델, 같은 질문 구조지만 프롬프트가 얼마나 깊이 생각할지 알려줍니다.

<img src="../../../translated_images/ko/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*최소한의 추론과 함께 빠른 계산*

<img src="../../../translated_images/ko/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*포괄적인 캐싱 전략 (2.8MB)*

### 작업 실행 (도구 준비문)

다단계 워크플로는 사전 계획과 진행 내레이션이 도움이 됩니다. 모델은 할 일을 요약하고 각 단계를 설명한 다음 결과를 요약합니다.

<img src="../../../translated_images/ko/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*단계별 내레이션으로 REST 엔드포인트 생성 (3.9MB)*

### 자기 반영 코드

"이메일 검증 서비스 생성"을 시도해보세요. 단순히 코드를 생성하고 멈추지 않고, 모델은 생성, 품질 기준에 따른 평가, 약점 식별 및 개선 과정을 거칩니다. 코드는 생산 기준에 도달할 때까지 반복됩니다.

<img src="../../../translated_images/ko/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*완성된 이메일 검증 서비스 (5.2MB)*

### 구조화된 분석

코드 리뷰에는 일관된 평가 프레임워크가 필요합니다. 모델은 고정된 범주(정확성, 관행, 성능, 보안)와 심각도 수준으로 코드를 분석합니다.

<img src="../../../translated_images/ko/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*프레임워크 기반 코드 리뷰*

### 다중 회차 채팅

"Spring Boot가 무엇인가요?"라고 질문한 다음 즉시 "예제 보여주세요"라고 하세요. 모델은 첫 질문을 기억하고 구체적인 Spring Boot 예제를 제공합니다. 메모리가 없으면 두 번째 질문이 너무 모호해집니다.

<img src="../../../translated_images/ko/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*질문 간의 문맥 보존*

### 단계별 추론

수학 문제를 선택하여 단계별 추론과 낮은 열의 모두로 시도해보세요. 낮은 열의는 빠르지만 불투명하게 답만 줍니다. 단계별은 모든 계산과 결정을 명확히 보여줍니다.

<img src="../../../translated_images/ko/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*명확한 단계가 포함된 수학 문제*

### 제한된 출력

특정 형식이나 단어 수가 필요할 때 이 패턴은 엄격한 준수를 강제합니다. 정확히 100 단어로 글머리 기호 형식 요약을 생성해보세요.

<img src="../../../translated_images/ko/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*형식 제어가 포함된 머신 러닝 요약*

## 당신이 실제로 배우고 있는 것

**추론 노력은 모든 것을 바꾼다**

GPT-5.2는 프롬프트를 통해 계산 노력을 제어할 수 있습니다. 낮은 노력은 빠른 답변과 최소 탐색을 의미합니다. 높은 노력은 모델이 깊이 생각하는 데 시간을 쓴다는 뜻입니다. 작업 복잡도에 맞게 노력을 조절하는 법을 배우는 것입니다 — 간단한 질문에 시간을 낭비하지 말고, 복잡한 결정도 서두르지 마세요.

**구조가 행동을 안내한다**

프롬프트에 있는 XML 태그를 보셨나요? 장식용이 아닙니다. 모델은 자유 형식 텍스트보다 구조화된 지시를 더 신뢰성 있게 따릅니다. 다단계 프로세스나 복잡한 논리가 필요할 때 구조가 모델이 어디에 있는지, 다음에 무엇을 해야 하는지 추적하는 데 도움을 줍니다.

<img src="../../../translated_images/ko/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*명확한 섹션과 XML 스타일 구성이 있는 잘 구조화된 프롬프트*

**자기 평가를 통한 품질**

자기 반영 패턴은 품질 기준을 명확히 합니다. 모델이 "맞게 하길 바라기" 대신, "옳음"이 무엇인지 정확히 알려줍니다: 올바른 로직, 오류 처리, 성능, 보안. 모델은 자신의 출력을 평가하고 개선할 수 있습니다. 이로써 코드 생성이 복권이 아닌 과정이 됩니다.

**문맥은 유한하다**

다중 회차 대화는 각 요청에 메시지 기록을 포함함으로써 작동합니다. 그러나 한계가 있습니다 — 모든 모델은 최대 토큰 수 한도를 갖습니다. 대화가 길어질수록 적절한 문맥을 유지하되 한계에 닿지 않는 전략이 필요합니다. 이 모듈은 메모리 작동 방식을 보여주며, 나중에 언제 요약하고, 언제 잊고, 언제 불러올지 배울 것입니다.

## 다음 단계

**다음 모듈:** [03-rag - RAG (검색 증강 생성)](../03-rag/README.md)

---

**내비게이션:** [← 이전: 모듈 01 - 소개](../01-introduction/README.md) | [메인으로 돌아가기](../README.md) | [다음: 모듈 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 최선을 다하고 있으나, 자동 번역에는 오류나 부정확성이 포함될 수 있음을 유의해 주시기 바랍니다. 원문은 해당 언어로 된 원본 문서를 권위 있는 출처로 간주해야 합니다. 중요한 정보의 경우, 전문 인력에 의한 번역을 권장합니다. 본 번역 사용으로 인한 오해나 해석상의 문제에 대해서는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->