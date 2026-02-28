# Module 02: GPT-5.2로 프롬프트 엔지니어링

## 목차

- [영상 안내](../../../02-prompt-engineering)
- [학습 내용](../../../02-prompt-engineering)
- [사전 준비사항](../../../02-prompt-engineering)
- [프롬프트 엔지니어링 이해하기](../../../02-prompt-engineering)
- [프롬프트 엔지니어링 기초](../../../02-prompt-engineering)
  - [제로샷 프롬프트](../../../02-prompt-engineering)
  - [퓨샷 프롬프트](../../../02-prompt-engineering)
  - [사고의 연쇄](../../../02-prompt-engineering)
  - [역할 기반 프롬프트](../../../02-prompt-engineering)
  - [프롬프트 템플릿](../../../02-prompt-engineering)
- [고급 패턴](../../../02-prompt-engineering)
- [기존 Azure 리소스 사용하기](../../../02-prompt-engineering)
- [애플리케이션 스크린샷](../../../02-prompt-engineering)
- [패턴 탐색](../../../02-prompt-engineering)
  - [낮은 열의도 vs 높은 열의도](../../../02-prompt-engineering)
  - [작업 실행 (도구 프리앰블)](../../../02-prompt-engineering)
  - [자기 성찰 코드](../../../02-prompt-engineering)
  - [구조화된 분석](../../../02-prompt-engineering)
  - [다중 턴 채팅](../../../02-prompt-engineering)
  - [단계별 추론](../../../02-prompt-engineering)
  - [제한된 출력](../../../02-prompt-engineering)
- [진짜 배우는 것](../../../02-prompt-engineering)
- [다음 단계](../../../02-prompt-engineering)

## 영상 안내

이 모듈 시작 방법을 설명하는 라이브 세션을 시청하세요:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="LangChain4j와 함께하는 Prompt Engineering - 라이브 세션" width="800"/></a>

## 학습 내용

<img src="../../../translated_images/ko/what-youll-learn.c68269ac048503b2.webp" alt="학습 내용" width="800"/>

이전 모듈에서 메모리가 대화형 AI를 가능하게 하는 방법과 기본 상호작용을 위한 GitHub Models 사용법을 배웠습니다. 이제는 Azure OpenAI의 GPT-5.2를 사용하여 질문하는 방법, 즉 프롬프트 자체에 집중합니다. 프롬프트 구조가 응답 품질에 미치는 영향은 매우 큽니다. 기본 프롬프트 기법을 복습한 후 GPT-5.2의 기능을 최대한 활용하는 8가지 고급 패턴으로 넘어갑니다.

GPT-5.2를 사용하는 이유는 추론 제어 기능이 도입되어 모델이 답변하기 전에 얼마나 깊게 생각할지 지정할 수 있기 때문입니다. 이를 통해 다양한 프롬프트 전략이 명확해지고 각 접근법을 언제 사용할지 이해할 수 있습니다. 또한 GitHub Models 대비 Azure에서 GPT-5.2에 부여하는 제한이 적어서 장점입니다.

## 사전 준비사항

- 모듈 01 완료 (Azure OpenAI 리소스 배포 완료)
- 루트 디렉터리에 `.env` 파일이 있고 Azure 자격 증명이 포함되어 있음 (모듈 01에서 `azd up` 실행 시 생성)

> **참고:** 모듈 01을 완료하지 않았다면, 먼저 해당 모듈의 배포 지침을 따르세요.

## 프롬프트 엔지니어링 이해하기

<img src="../../../translated_images/ko/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="프롬프트 엔지니어링이란?" width="800"/>

프롬프트 엔지니어링이란 원하는 결과를 꾸준히 얻을 수 있도록 입력 텍스트를 설계하는 것입니다. 단순히 질문하는 것이 아니라 모델이 요구 사항과 제공 방식을 정확히 이해하도록 요청을 구조화하는 방식입니다.

동료에게 지시하는 것과 비슷합니다. "버그를 고쳐"는 모호합니다. "UserService.java 45행에서 null 체크를 추가하여 널 포인터 예외를 수정해"는 구체적입니다. 언어 모델도 마찬가지로 구체성과 구조가 중요합니다.

<img src="../../../translated_images/ko/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j의 역할" width="800"/>

LangChain4j는 모델 연결, 메모리 및 메시지 타입과 같은 인프라를 제공합니다. 프롬프트 패턴은 이 인프라를 통해 전달하는 세심하게 구조화된 텍스트입니다. 핵심 빌딩 블록은 AI의 동작과 역할을 설정하는 `SystemMessage`와 실제 요청을 담는 `UserMessage`입니다.

## 프롬프트 엔지니어링 기초

<img src="../../../translated_images/ko/five-patterns-overview.160f35045ffd2a94.webp" alt="기본 프롬프트 엔지니어링 패턴 개요" width="800"/>

고급 패턴으로 들어가기 전에 다섯 가지 기본 프롬프트 기법을 복습해봅니다. 모든 프롬프트 엔지니어가 알아야 할 기본 도구들입니다. 이미 [퀵스타트 모듈](../00-quick-start/README.md#2-prompt-patterns)을 완료했다면 이 기법들을 실전에서 봤을 것입니다 — 여기에 이론적 틀을 설명합니다.

### 제로샷 프롬프트

가장 단순한 접근법: 예제 없이 모델에 직접 명령을 내립니다. 모델은 전적으로 학습에 의존해 과제를 이해하고 수행합니다. 예상 행동이 명백한 단순한 요청에서 잘 작동합니다.

<img src="../../../translated_images/ko/zero-shot-prompting.7abc24228be84e6c.webp" alt="제로샷 프롬프트" width="800"/>

*예제 없이 직접 명령 — 모델은 명령만으로 과제를 추론*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 응답: "긍정적"
```

**사용 시기:** 단순 분류, 직접 질문, 번역 또는 별도 안내 없이 모델이 처리 가능한 작업에 적합합니다.

### 퓨샷 프롬프트

모델이 따라야 할 패턴을 보여주는 예제를 제공합니다. 모델은 예제로부터 입력-출력 형식을 학습해 새로운 입력에도 적용합니다. 원하는 형식이나 행동이 명확하지 않은 작업에서 일관성을 크게 향상시킵니다.

<img src="../../../translated_images/ko/few-shot-prompting.9d9eace1da88989a.webp" alt="퓨샷 프롬프트" width="800"/>

*예제로 학습 — 모델이 패턴을 파악해 새 입력에 적용*

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

**사용 시기:** 사용자 맞춤 분류, 일관된 형식, 도메인 특화 작업 또는 제로샷 결과가 불안정할 때 유용합니다.

### 사고의 연쇄

모델이 단계별로 추론을 보여주도록 합니다. 바로 답 안 내고 문제를 쪼개고 각 부분을 명확히 해결합니다. 수학, 논리, 다단계 추론 작업에서 정확도가 크게 향상됩니다.

<img src="../../../translated_images/ko/chain-of-thought.5cff6630e2657e2a.webp" alt="사고의 연쇄 프롬프트" width="800"/>

*단계별 추론 — 복잡한 문제를 명시적 논리 단계로 분해*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 모델은 보여줍니다: 15 - 8 = 7, 그리고 7 + 12 = 19개의 사과
```

**사용 시기:** 수학 문제, 논리 퍼즐, 디버깅 또는 추론 과정을 보여주면 정밀도와 신뢰도가 향상되는 작업에 적합합니다.

### 역할 기반 프롬프트

질문하기 전에 AI의 페르소나나 역할을 설정합니다. 이렇게 하면 응답의 어조, 깊이, 초점이 달라집니다. "소프트웨어 아키텍트"는 "초급 개발자"나 "보안 감사자"와 전혀 다른 조언을 줍니다.

<img src="../../../translated_images/ko/role-based-prompting.a806e1a73de6e3a4.webp" alt="역할 기반 프롬프트" width="800"/>

*맥락과 페르소나 설정 — 같은 질문이라도 역할에 따라 다른 답변*

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

**사용 시기:** 코드 리뷰, 튜터링, 도메인 특화 분석 또는 전문성 수준이나 관점에 맞춘 응답이 필요할 때 사용합니다.

### 프롬프트 템플릿

변수 자리표시자가 있는 재사용 가능한 프롬프트를 만듭니다. 매번 새 프롬프트를 쓰는 대신 템플릿을 한 번 정의하고 다양한 값을 채워 넣습니다. LangChain4j의 `PromptTemplate` 클래스는 `{{variable}}` 구문으로 쉽게 만듭니다.

<img src="../../../translated_images/ko/prompt-templates.14bfc37d45f1a933.webp" alt="프롬프트 템플릿" width="800"/>

*변수 자리표시자가 있는 재사용 가능한 프롬프트 — 하나의 템플릿 다수 활용*

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

**사용 시기:** 다른 입력이 반복되는 쿼리, 일괄 처리, 재사용 가능한 AI 워크플로우 구축 또는 프롬프트 구조는 같고 데이터만 바뀔 때 유용합니다.

---

이 다섯 가지 기초는 대부분 프롬프트 작업을 위한 탄탄한 도구 키트를 제공합니다. 이 모듈 나머지 부분은 GPT-5.2의 추론 제어, 자기 평가, 구조화된 출력 기능을 활용하는 **8가지 고급 패턴**을 구축합니다.

## 고급 패턴

기초를 익혔으니 이 모듈을 독특하게 만드는 8가지 고급 패턴으로 넘어갑니다. 모든 문제가 같은 접근법을 필요로 하지 않습니다. 어떤 질문은 빠른 답변이 필요하고 어떤 질문은 깊은 사고가 필요합니다. 일부는 추론 과정을 보여야 하고, 일부는 결과만 필요합니다. 아래 각 패턴은 서로 다른 시나리오에 최적화되어 있으며 GPT-5.2의 추론 제어 기능 덕분에 그 차이가 더욱 두드러집니다.

<img src="../../../translated_images/ko/eight-patterns.fa1ebfdf16f71e9a.webp" alt="여덟 가지 프롬프트 엔지니어링 패턴" width="800"/>

*여덟 가지 프롬프트 엔지니어링 패턴과 사용 사례 개요*

<img src="../../../translated_images/ko/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2의 추론 제어" width="800"/>

*GPT-5.2의 추론 제어는 모델이 얼마나 깊게 생각할지 지정할 수 있게 해줍니다 — 빠르고 직접적인 답변부터 심층 탐색까지*

**낮은 열의도 (빠르고 집중된 답변)** - 빠르고 직접적인 답변이 필요한 간단한 질문에 적합. 모델은 최소한의 추론만 수행 — 최대 2단계. 계산, 조회 또는 직관적인 질문에 사용하십시오.

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **GitHub Copilot으로 탐색하기:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)를 열어 다음을 물어보세요:
> - "낮은 열의도와 높은 열의도 프롬프트 패턴의 차이는 무엇인가요?"
> - "프롬프트 내 XML 태그가 AI의 응답 구조화에 어떻게 도움이 되나요?"
> - "자기 성찰 패턴과 직접 명령 패턴은 언제 각각 사용해야 하나요?"

**높은 열의도 (깊고 철저한 분석)** - 포괄적 분석이 필요한 복잡한 문제에 적합. 모델이 철저히 탐색하고 상세한 추론을 보여줍니다. 시스템 설계, 아키텍처 결정 또는 복잡한 연구에 사용합니다.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**작업 실행 (단계별 진행)** - 다단계 워크플로우에 적합. 모델은 사전 계획을 제시하고, 작업하면서 각 단계를 서술한 다음 마지막에 요약합니다. 마이그레이션, 구현 또는 다단계 프로세스에 사용합니다.

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought 프롬프트는 모델이 추론 과정을 명확히 보여주도록 요구하여 복잡한 작업의 정확도를 높입니다. 단계별 분해는 사람과 AI 모두가 논리를 이해하는 데 도움을 줍니다.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 채팅에서 시도해 보세요:** 이 패턴에 대해 물어보세요:
> - "장시간 실행 작업에 작업 실행 패턴을 어떻게 적응할 수 있나요?"
> - "생산용 애플리케이션에서 도구 프리앰블을 구조화하는 모범 사례는 무엇인가요?"
> - "UI에서 중간 진행 상황 업데이트를 어떻게 캡처하고 표시할 수 있나요?"

<img src="../../../translated_images/ko/task-execution-pattern.9da3967750ab5c1e.webp" alt="작업 실행 패턴" width="800"/>

*다단계 작업을 위한 계획 → 실행 → 요약 워크플로우*

**자기 성찰 코드** - 생산 수준의 코드를 생성합니다. 모델은 적절한 오류 처리와 함께 생산 표준을 준수하는 코드를 만듭니다. 새로운 기능이나 서비스를 구축할 때 사용하십시오.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ko/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="자기 성찰 주기" width="800"/>

*반복 개선 루프 — 생성, 평가, 문제 식별, 개선, 반복*

**구조화된 분석** - 일관된 평가를 위해 고정된 프레임워크(정확성, 관행, 성능, 보안, 유지보수성)를 사용하여 코드를 검토합니다. 코드 리뷰 또는 품질 평가에 적합합니다.

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 채팅에서 시도해 보세요:** 구조화된 분석에 대해 질문:
> - "코드 리뷰 유형별 분석 프레임워크를 어떻게 맞춤화할 수 있을까요?"
> - "구조화된 출력을 프로그래밍 방식으로 구문 분석하고 실행하는 최선의 방법은 무엇인가요?"
> - "다른 리뷰 세션 간 심각도 수준을 일관되게 유지하려면 어떻게 해야 하나요?"

<img src="../../../translated_images/ko/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="구조화된 분석 패턴" width="800"/>

*심각도 수준과 함께 일관된 코드 리뷰를 위한 프레임워크*

**다중 턴 채팅** - 맥락이 필요한 대화용. 모델이 이전 메시지를 기억하고 이를 바탕으로 답변합니다. 대화형 지원 세션이나 복잡한 Q&A에 적합합니다.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ko/context-memory.dff30ad9fa78832a.webp" alt="대화 맥락 메모리" width="800"/>

*대화 맥락이 여러 턴에 걸쳐 누적되어 토큰 제한에 도달할 때까지 유지*

**단계별 추론** - 명확한 논리가 필요한 문제에 적합. 모델이 각 단계를 명시적으로 보여줍니다. 수학 문제, 논리 퍼즐, 또는 사고 과정을 파악하고 싶을 때 사용합니다.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ko/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="단계별 추론 패턴" width="800"/>

*문제를 명시적 논리 단계로 분해*

**제한된 출력** - 특정 형식 요구 사항이 있는 응답 생성용. 모델이 형식과 길이 규칙을 엄격히 준수합니다. 요약 또는 정확한 출력 구조가 필요할 때 사용합니다.

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

<img src="../../../translated_images/ko/constrained-output-pattern.0ce39a682a6795c2.webp" alt="제한된 출력 패턴" width="800"/>

*특정 형식, 길이 및 구조 요구 사항 강제 실행*

## 기존 Azure 리소스 사용하기

**배포 확인:**

루트 디렉터리에 Azure 자격 증명이 포함된 `.env` 파일이 있는지 확인 (모듈 01에서 생성됨):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT를 표시해야 합니다
```

**애플리케이션 시작:**

> **참고:** 모듈 01에서 `./start-all.sh`로 애플리케이션을 이미 모두 시작했다면, 이 모듈은 포트 8083에서 이미 실행 중입니다. 아래 시작 명령을 건너뛰고 바로 http://localhost:8083 으로 이동하세요.
**옵션 1: Spring Boot 대시보드 사용 (VS Code 사용자에게 권장)**

개발 컨테이너에는 모든 Spring Boot 애플리케이션을 관리할 수 있는 시각적 인터페이스인 Spring Boot 대시보드 확장 기능이 포함되어 있습니다. VS Code 왼쪽의 활동 표시줄에서 Spring Boot 아이콘을 찾아보세요.

Spring Boot 대시보드에서는 다음을 할 수 있습니다:
- 작업 공간에 있는 모든 Spring Boot 애플리케이션 보기
- 클릭 한 번으로 애플리케이션 시작/중지
- 애플리케이션 로그를 실시간으로 보기
- 애플리케이션 상태 모니터링

간단히 "prompt-engineering" 옆의 재생 버튼을 클릭하여 이 모듈을 시작하거나, 모든 모듈을 한 번에 시작할 수 있습니다.

<img src="../../../translated_images/ko/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**옵션 2: 셸 스크립트 사용**

모든 웹 애플리케이션(모듈 01-04) 시작:

**Bash:**
```bash
cd ..  # 루트 디렉터리에서부터
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

두 스크립트 모두 루트 `.env` 파일에서 환경 변수를 자동으로 로드하며, JAR 파일이 없으면 빌드합니다.

> **참고:** 시작 전에 모든 모듈을 수동으로 빌드하려면:
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

브라우저에서 http://localhost:8083 을 열어보세요.

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

*특징과 사용 사례를 포함한 8가지 프롬프트 엔지니어링 패턴이 모두 표시된 메인 대시보드*

## 패턴 탐험하기

웹 인터페이스를 통해 다양한 프롬프트 전략을 실험할 수 있습니다. 각 패턴은 다른 문제를 해결하므로, 다양한 방식을 시도하며 언제 각 접근법이 효과적인지 확인해 보세요.

> **참고: 스트리밍 vs 비스트리밍** — 모든 패턴 페이지에는 두 개의 버튼이 있습니다: **🔴 스트림 응답(실시간)** 과 **비스트리밍** 옵션. 스트리밍은 서버 전송 이벤트(SSE)를 사용하여 모델이 생성하는 토큰을 실시간으로 표시하므로 즉시 진행 상황을 볼 수 있습니다. 비스트리밍 옵션은 전체 응답을 기다린 다음 표시합니다. 깊은 추론을 요구하는 프롬프트(예: High Eagerness, Self-Reflecting Code)는 비스트리밍 호출이 매우 오래 걸릴 수 있으며(때로는 수 분), 눈에 띄는 피드백이 없습니다. **복잡한 프롬프트를 실험할 때는 스트리밍을 사용하세요**. 모델이 작동하는 모습을 볼 수 있고 요청이 시간이 초과된 것처럼 느껴지는 것을 방지할 수 있습니다.
>
> **참고: 브라우저 요구사항** — 스트리밍 기능은 Fetch Streams API(`response.body.getReader()`)를 사용하며, 완전한 브라우저(Chrome, Edge, Firefox, Safari)에서만 작동합니다. VS Code 내장 Simple Browser에서는 웹뷰가 ReadableStream API를 지원하지 않아 **작동하지 않습니다**. Simple Browser를 사용할 경우 비스트리밍 버튼은 정상 작동하지만 스트리밍 버튼만 영향을 받습니다. 전체 기능을 위해 외부 브라우저에서 `http://localhost:8083` 을 여세요.

### 낮은 Eagerness vs 높은 Eagerness

낮은 Eagerness를 사용해 "200의 15%는?" 같은 간단한 질문을 하면 즉각적이고 직접적인 답변을 받습니다. 이제 높은 Eagerness로 "트래픽이 많은 API에 대한 캐싱 전략 설계" 같은 복잡한 질문을 해보세요. **🔴 스트림 응답(실시간)** 을 클릭한 후 모델의 상세한 추론이 토큰별로 나타나는 것을 관찰할 수 있습니다. 같은 모델, 같은 질문 구조지만 프롬프트가 생각하는 양을 지정합니다.

### 작업 실행 (도구 프리앰블)

다단계 워크플로우에는 사전 계획과 진행 내레이션이 유리합니다. 모델이 수행할 작업을 개요로 작성하고, 각 단계를 설명하며, 결과를 요약합니다.

### 자기 반성 코드

"이메일 검증 서비스를 만들어줘"라고 시도해 보세요. 단순히 코드를 생성하고 중지하는 대신, 모델은 품질 기준에 따라 생성, 평가, 약점 식별, 개선을 반복합니다. 코드가 생산 기준을 만족할 때까지 반복되는 과정을 볼 수 있습니다.

### 구조화된 분석

코드 리뷰에는 일관된 평가 프레임워크가 필요합니다. 모델은 엄격한 카테고리(정확성, 모범 사례, 성능, 보안)와 심각도 수준을 활용해 코드를 분석합니다.

### 다중 턴 채팅

"Spring Boot가 뭐야?"라고 묻고 바로 "예를 보여줘"라고 이어서 질문하세요. 모델은 첫 질문을 기억하고 특정 Spring Boot 예제를 제공합니다. 메모리가 없으면 두 번째 질문은 너무 모호해집니다.

### 단계별 추론

수학 문제를 골라 단계별 추론과 낮은 Eagerness 모두로 시도해 보세요. 낮은 Eagerness는 빠르지만 불투명한 답만 제공합니다. 단계별 추론은 모든 계산과 결정을 보여줍니다.

### 제한된 출력

특정 형식이나 단어 수가 필요한 경우, 이 패턴은 엄격한 준수를 보장합니다. 정확히 100단어로 된 요약을 글머리표 형식으로 생성해보세요.

## 진짜 배우는 것

**추론 노력은 모든 것을 바꾼다**

GPT-5.2는 프롬프트를 통해 계산 노력을 제어할 수 있게 해줍니다. 낮은 노력은 빠른 응답과 최소한의 탐색을 의미합니다. 높은 노력은 모델이 깊이 생각하도록 시간을 줍니다. 작업 난이도에 맞춰 노력을 조절하는 법을 배우는 중입니다 — 간단한 질문에 시간 낭비하지 말고, 복잡한 결정은 서두르지 마세요.

**구조는 행동을 안내한다**

프롬프트 내 XML 태그를 눈여겨보세요? 장식이 아닙니다. 모델은 자유 형식 텍스트보다 구조화된 지침을 더 잘 따릅니다. 다단계 프로세스나 복잡한 논리가 필요할 때는 구조가 모델이 현재 위치와 다음 단계를 추적하는 데 도움을 줍니다.

<img src="../../../translated_images/ko/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*명확한 섹션과 XML 스타일 구성을 갖춘 잘 구조화된 프롬프트의 해부*

**자기 평가를 통한 품질 향상**

자기 반성 패턴은 품질 기준을 명확히 함으로써 작동합니다. 모델이 "잘 하기를 기대"하는 대신, 정확한 논리, 오류 처리, 성능, 보안과 같은 "잘함"의 의미를 명확히 알려줍니다. 모델은 자신의 출력을 평가하고 개선할 수 있어 코드 생성이 복권이 아닌 과정이 됩니다.

**컨텍스트는 유한하다**

다중 턴 대화는 메시지 기록을 각 요청과 함께 포함시켜 작동합니다. 하지만 한계가 있습니다 — 모든 모델에 최대 토큰 수 제한이 있습니다. 대화가 커질수록 관련 컨텍스트를 유지하는 전략이 필요합니다. 이 모듈은 메모리가 어떻게 작동하는지 보여주며, 이후 요약이 필요한 시점, 잊어야 할 때, 불러와야 할 때를 배우게 됩니다.

## 다음 단계

**다음 모듈:** [03-rag - RAG (검색 증강 생성)](../03-rag/README.md)

---

**탐색:** [← 이전: 모듈 01 - 소개](../01-introduction/README.md) | [메인으로 돌아가기](../README.md) | [다음: 모듈 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 노력하고 있지만, 자동 번역에는 오류나 부정확성이 포함될 수 있음을 유의하시기 바랍니다. 원본 문서의 원어가 권위 있는 출처로 간주되어야 합니다. 중요한 정보의 경우에는 전문적인 인간 번역을 권장합니다. 본 번역의 사용으로 인해 발생하는 어떠한 오해나 잘못된 해석에 대해서도 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->