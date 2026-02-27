# Module 02: GPT-5.2를 활용한 프롬프트 엔지니어링

## 목차

- [비디오 워크쓰루](../../../02-prompt-engineering)
- [배울 내용](../../../02-prompt-engineering)
- [사전 준비사항](../../../02-prompt-engineering)
- [프롬프트 엔지니어링 이해하기](../../../02-prompt-engineering)
- [프롬프트 엔지니어링 기본](../../../02-prompt-engineering)
  - [제로샷 프롬프트](../../../02-prompt-engineering)
  - [퓨샷 프롬프트](../../../02-prompt-engineering)
  - [사고의 사슬](../../../02-prompt-engineering)
  - [역할 기반 프롬프트](../../../02-prompt-engineering)
  - [프롬프트 템플릿](../../../02-prompt-engineering)
- [고급 패턴](../../../02-prompt-engineering)
- [기존 Azure 리소스 사용하기](../../../02-prompt-engineering)
- [응용 화면 캡처](../../../02-prompt-engineering)
- [패턴 탐색](../../../02-prompt-engineering)
  - [낮은 vs 높은 열의](../../../02-prompt-engineering)
  - [작업 실행 (도구 프리앰블)](../../../02-prompt-engineering)
  - [셀프 리플렉팅 코드](../../../02-prompt-engineering)
  - [구조화된 분석](../../../02-prompt-engineering)
  - [멀티턴 채팅](../../../02-prompt-engineering)
  - [단계별 추론](../../../02-prompt-engineering)
  - [제한된 출력](../../../02-prompt-engineering)
- [진정으로 배우는 것](../../../02-prompt-engineering)
- [다음 단계](../../../02-prompt-engineering)

## 비디오 워크쓰루

이 모듈 시작 방법을 설명하는 라이브 세션 시청: [LangChain4j로 프롬프트 엔지니어링 - 라이브 세션](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## 배울 내용

<img src="../../../translated_images/ko/what-youll-learn.c68269ac048503b2.webp" alt="배울 내용" width="800"/>

이전 모듈에서 메모리가 대화형 AI에 어떻게 작용하는지, 그리고 기본 상호작용을 위해 GitHub 모델을 사용하는 방법을 보았습니다. 이제는 Azure OpenAI의 GPT-5.2를 사용하여 질문하는 방법, 즉 프롬프트 자체에 집중합니다. 프롬프트를 구성하는 방식은 응답 품질에 큰 영향을 미칩니다. 먼저 기본 프롬프팅 기법을 복습하고, GPT-5.2의 기능을 최대한 활용하는 여덟 가지 고급 패턴으로 넘어갑니다.

GPT-5.2를 사용하는 이유는 추론 제어 기능이 도입되었기 때문입니다 - 모델에게 답변하기 전에 얼마나 깊게 생각할지 지시할 수 있습니다. 이는 다양한 프롬프팅 전략을 더 명확히 하며, 각각의 접근법을 언제 사용해야 하는지 이해하는 데 도움을 줍니다. 또한 Azure가 GitHub 모델에 비해 GPT-5.2의 API 호출 한도가 더 적은 제한을 두고 있습니다.

## 사전 준비사항

- 01 모듈 완료 (Azure OpenAI 리소스 배포 완료)
- 루트 디렉터리에 Azure 자격 증명이 포함된 `.env` 파일 존재 (01 모듈에서 `azd up` 명령으로 생성됨)

> **참고:** 01 모듈을 완료하지 않았다면 먼저 그곳의 배포 지침을 따르세요.

## 프롬프트 엔지니어링 이해하기

<img src="../../../translated_images/ko/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="프롬프트 엔지니어링이란?" width="800"/>

프롬프트 엔지니어링은 필요한 결과를 일관되게 얻을 수 있도록 입력 텍스트를 설계하는 작업입니다. 단순히 질문을 던지는 것이 아니라 모델이 당신이 원하는 바를 정확히 이해하고 전달할 수 있도록 요청을 구조화하는 것입니다.

동료에게 지시하는 것과 비슷합니다. "버그를 고쳐"라는 말은 모호합니다. "UserService.java 45번째 줄에서 널 체크를 추가해 널 포인터 예외를 고쳐"가 명확합니다. 언어 모델도 마찬가지로, 구체성과 구조가 중요합니다.

<img src="../../../translated_images/ko/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j의 역할" width="800"/>

LangChain4j는 인프라를 제공합니다 — 모델 연결, 메모리, 메시지 타입 등 — 프롬프트 패턴은 그 인프라를 통해 전달하는 정교하게 구성된 텍스트입니다. 주요 구성 요소는 AI의 행동과 역할을 설정하는 `SystemMessage`와 실제 요청을 담는 `UserMessage`입니다.

## 프롬프트 엔지니어링 기본

<img src="../../../translated_images/ko/five-patterns-overview.160f35045ffd2a94.webp" alt="프롬프트 엔지니어링 다섯 가지 기본 패턴 개요" width="800"/>

이 모듈의 고급 패턴으로 들어가기 전, 다섯 가지 기초 프롬프팅 기법을 복습해 봅시다. 이는 모든 프롬프트 엔지니어가 알아야 할 기본입니다. 이미 [Quick Start 모듈](../00-quick-start/README.md#2-prompt-patterns)을 따라해 봤다면, 여기서는 개념적 틀을 다지고 있습니다.

### 제로샷 프롬프팅

가장 간단한 접근법: 예시 없이 직접 명령을 내립니다. 모델은 학습한 지식만으로 작업을 이해하고 수행합니다. 예상되는 행동이 명확한 단순 요청에 적합합니다.

<img src="../../../translated_images/ko/zero-shot-prompting.7abc24228be84e6c.webp" alt="제로샷 프롬프팅" width="800"/>

*예시 없이 직접 명령 — 명령만으로 작업을 모델이 유추*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 응답: "긍정적"
```

**사용 시기:** 단순 분류, 직접 질문, 번역 또는 추가 안내 없이 모델이 처리 가능한 작업.

### 퓨샷 프롬프팅

모델에게 따라야 하는 패턴을 보여주는 예시를 제공합니다. 모델은 예시로부터 입력-출력 포맷을 학습하고 새 입력에 적용합니다. 원하는 형식이나 행동이 명확하지 않을 때 일관성이 크게 향상됩니다.

<img src="../../../translated_images/ko/few-shot-prompting.9d9eace1da88989a.webp" alt="퓨샷 프롬프팅" width="800"/>

*예시에서 학습 — 패턴을 파악하고 새 입력에 적용*

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

**사용 시기:** 맞춤 분류, 일관된 포맷, 도메인 특정 작업, 제로샷 결과가 불안정할 때.

### 사고의 사슬

모델에게 한 걸음씩 자신의 추론 과정을 보여달라고 요청합니다. 바로 답변으로 넘어가지 않고 문제를 나누어 명확히 풀어가는 과정을 나타냅니다. 수학, 논리, 다단계 추론 작업의 정확성이 향상됩니다.

<img src="../../../translated_images/ko/chain-of-thought.5cff6630e2657e2a.webp" alt="사고의 사슬 프롬프팅" width="800"/>

*단계별 추론 — 복잡한 문제를 명확한 논리 단계로 분해*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 모델은 보여 줍니다: 15 - 8 = 7, 그리고 7 + 12 = 19개의 사과
```

**사용 시기:** 수학 문제, 논리 퍼즐, 디버깅, 추론 과정 표시가 정확도와 신뢰도 향상에 도움이 될 때.

### 역할 기반 프롬프팅

질문 전에 AI에게 페르소나 또는 역할을 설정합니다. 이는 응답의 어조, 깊이, 초점을 형성하는 맥락을 제공합니다. "소프트웨어 아키텍트", "주니어 개발자", "보안 감사관"은 각각 다른 조언을 합니다.

<img src="../../../translated_images/ko/role-based-prompting.a806e1a73de6e3a4.webp" alt="역할 기반 프롬프팅" width="800"/>

*맥락과 페르소나 설정 — 동일 질문도 역할에 따라 다른 답변*

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

**사용 시기:** 코드 리뷰, 튜터링, 도메인 특정 분석, 특정 전문성 수준이나 관점에 맞춘 응답이 필요할 때.

### 프롬프트 템플릿

가변 자리표시자를 가진 재사용 가능한 프롬프트를 만듭니다. 매번 새 프롬프트를 작성하는 대신 한 번 템플릿을 정의하고 값을 채웁니다. LangChain4j의 `PromptTemplate` 클래스는 `{{variable}}` 문법으로 간편하게 지원합니다.

<img src="../../../translated_images/ko/prompt-templates.14bfc37d45f1a933.webp" alt="프롬프트 템플릿" width="800"/>

*가변 자리표시자를 가진 재사용 가능한 프롬프트 — 하나의 템플릿, 다양한 용도*

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

**사용 시기:** 다른 입력과 함께 반복 쿼리, 배치 처리, 재사용 가능한 AI 워크플로우 구축, 프롬프트 구조는 같고 데이터만 다른 경우.

---

이 다섯 가지 기본기는 대부분의 프롬프팅 작업에 튼튼한 도구를 제공합니다. 이 모듈의 나머지 부분은 GPT-5.2의 추론 제어, 자기 평가, 구조화된 출력 기능을 활용하는 **여덟 가지 고급 패턴**으로 발전합니다.

## 고급 패턴

기본기를 익혔으니, 이 모듈을 독특하게 만드는 여덟 가지 고급 패턴으로 넘어갑니다. 모든 문제에 같은 접근이 필요한 것은 아닙니다. 어떤 질문은 빠른 답이 필요하고, 어떤 것은 깊은 사고가 필요합니다. 어떤 경우는 추론 과정을 명확히 보여줘야 하고, 어떤 경우는 결과만 필요합니다. 아래 각 패턴은 다른 시나리오에 최적화되어 있으며, GPT-5.2의 추론 제어 기능으로 차이가 더욱 뚜렷해집니다.

<img src="../../../translated_images/ko/eight-patterns.fa1ebfdf16f71e9a.webp" alt="여덟 가지 프롬프팅 패턴" width="800"/>

*여덟 가지 프롬프트 엔지니어링 패턴 및 사용 사례 개요*

<img src="../../../translated_images/ko/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2의 추론 제어" width="800"/>

*GPT-5.2의 추론 제어 기능으로 모델이 얼마나 깊이 생각할지 지정 가능 — 빠른 직접 답변부터 심층 탐색까지*

**낮은 열의 (빠르고 집중적)** - 빠르고 직접적인 답변이 필요한 단순 질문용. 모델은 최소한의 추론 — 최대 2단계만 수행. 계산, 조회, 단순 질문에 적합.

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

> 💡 **GitHub Copilot과 탐색해보기:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 열고 질문해보기:
> - "낮은 열의와 높은 열의 프롬프팅 패턴의 차이점은 무엇인가요?"
> - "프롬프트 내 XML 태그는 AI 응답을 어떻게 구조화하는 데 도움을 주나요?"
> - "셀프 리플렉션 패턴과 직접 지시 패턴은 언제 사용해야 하나요?"

**높은 열의 (깊고 철저하게)** - 포괄적 분석이 필요한 복잡한 문제용. 모델이 꼼꼼히 탐색하고 자세한 추론을 보여줌. 시스템 설계, 아키텍처 결정, 복잡한 연구에 적합.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**작업 실행 (단계별 진행)** - 다단계 워크플로우용. 모델이 사전에 계획을 제시하고, 작업을 진행하며 각 단계를 내레이션하고, 요약을 제공함. 마이그레이션, 구현, 복합 작업에 적합.

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

사고의 사슬 프롬프팅은 모델에게 추론 과정을 명확히 보여달라고 명시적으로 요청하여 복잡한 작업의 정확도를 높입니다. 단계별 분해는 사람과 AI 모두가 논리를 이해하는 데 도움됩니다.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 채팅으로 시도해보기:** 이 패턴에 대해 질문해보기:
> - "장기 실행 작업에 작업 실행 패턴을 어떻게 적용하나요?"
> - "운영 애플리케이션에서 도구 프리앰블 구조화의 모범 사례는 무엇인가요?"
> - "중간 진행 상태 업데이트를 UI에서 어떻게 캡처하고 표시할 수 있나요?"

<img src="../../../translated_images/ko/task-execution-pattern.9da3967750ab5c1e.webp" alt="작업 실행 패턴" width="800"/>

*다단계 작업을 위한 계획 → 실행 → 요약 워크플로*

**셀프 리플렉팅 코드** - 프로덕션 품질 코드를 생성할 때. 모델이 적절한 오류 처리와 함께 프로덕션 표준에 맞춰 코드를 생성함. 새로운 기능이나 서비스를 만들 때 사용.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ko/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="셀프 리플렉션 사이클" width="800"/>

*반복 개선 루프 - 생성, 평가, 문제점 식별, 개선, 반복*

**구조화된 분석** - 일관된 평가를 위해. 모델이 고정된 프레임워크(정확성, 관행, 성능, 보안, 유지보수성)로 코드를 리뷰함. 코드 리뷰나 품질 평가에 적합.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 채팅으로 시도해보기:** 구조화된 분석에 대해 질문해보기:
> - "다양한 코드 리뷰 유형에 맞게 분석 프레임워크를 어떻게 맞춤 설정하나요?"
> - "구조화된 출력을 프로그래밍 방식으로 파싱하고 대응하는 최선의 방법은 무엇인가요?"
> - "다른 리뷰 세션들 간 심각도 수준의 일관성을 어떻게 보장할 수 있나요?"

<img src="../../../translated_images/ko/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="구조화된 분석 패턴" width="800"/>

*심각도 수준을 포함한 일관된 코드 리뷰 프레임워크*

**멀티턴 채팅** - 컨텍스트가 필요한 대화용. 모델이 이전 메시지를 기억하고 이를 기반으로 이어감. 인터랙티브 도움 세션이나 복잡한 Q&A에 적합.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ko/context-memory.dff30ad9fa78832a.webp" alt="컨텍스트 메모리" width="800"/>

*여러 턴에 걸쳐 대화 컨텍스트가 누적되어 토큰 한도에 도달할 때까지 유지되는 방식*

**단계별 추론** - 명확한 논리가 필요한 문제용. 모델이 각 단계에 대해 명시적인 추론을 보여줌. 수학 문제, 논리 퍼즐, 사고 과정을 이해해야 할 때 적합.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ko/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="단계별 패턴" width="800"/>

*문제를 명확한 논리 단계로 분해*

**제한된 출력** - 특정 형식 요구사항이 있는 응답용. 모델이 형식과 길이 규칙을 엄격히 준수함. 요약이나 정확한 출력 구조가 필요할 때 사용.

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

*특정 형식, 길이, 구조 요구사항 강제 적용*

## 기존 Azure 리소스 사용하기

**배포 확인:**

루트 디렉터리에 Azure 자격 증명이 포함된 `.env` 파일이 존재하는지 확인 (01 모듈 실행 중 생성됨):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT를 표시해야 합니다
```

**애플리케이션 시작:**

> **참고:** 01 모듈에서 `./start-all.sh`로 모든 애플리케이션을 이미 시작했다면 이 모듈은 이미 포트 8083에서 실행 중입니다. 아래 시작 명령을 생략하고 바로 http://localhost:8083 으로 이동하세요.

**옵션 1: 스프링 부트 대시보드 사용 (VS Code 사용자 권장)**
개발 컨테이너에는 모든 Spring Boot 애플리케이션을 관리할 수 있는 시각적 인터페이스를 제공하는 Spring Boot 대시보드 확장 기능이 포함되어 있습니다. VS Code 왼쪽에 있는 활동 표시줄에서 Spring Boot 아이콘을 찾아보세요.

Spring Boot 대시보드에서 할 수 있는 작업:
- 작업 공간 내의 모든 사용 가능한 Spring Boot 애플리케이션 보기
- 클릭 한 번으로 애플리케이션 시작/중지
- 실시간 애플리케이션 로그 보기
- 애플리케이션 상태 모니터링

"prompt-engineering" 옆의 재생 버튼을 클릭하여 이 모듈을 시작하거나, 모든 모듈을 한 번에 시작할 수 있습니다.

<img src="../../../translated_images/ko/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**옵션 2: 셸 스크립트 사용**

모든 웹 애플리케이션(모듈 01-04) 시작:

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

두 스크립트 모두 자동으로 루트 `.env` 파일에서 환경 변수를 로드하며 JAR 파일이 존재하지 않는 경우 빌드합니다.

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

*특성 및 사용 사례와 함께 8가지 프롬프트 엔지니어링 패턴을 모두 보여주는 메인 대시보드*

## 패턴 탐색

웹 인터페이스를 통해 다양한 프롬프트 전략을 실험할 수 있습니다. 각 패턴은 다른 문제를 해결하므로 각각 언제 빛을 발하는지 직접 확인해 보세요.

> **참고: 스트리밍 vs 비스트리밍** — 모든 패턴 페이지에는 **🔴 스트림 응답(실시간)** 버튼과 **비스트리밍** 옵션 두 가지 버튼이 제공됩니다. 스트리밍은 서버 발송 이벤트(SSE)를 사용하여 모델이 토큰을 생성하는 즉시 실시간으로 표시하므로 진행 상황을 바로 볼 수 있습니다. 비스트리밍 옵션은 전체 응답을 기다린 다음 표시합니다. 깊은 추론을 요구하는 프롬프트(예: High Eagerness, Self-Reflecting Code)의 경우 비스트리밍 호출은 매우 오랜 시간이 걸릴 수 있으며 때때로 몇 분이 소요되어 시각적 피드백이 없습니다. **복잡한 프롬프트 실험 시에는 스트리밍을 사용하여** 모델 작동을 직접 보고 요청이 시간 초과된 것처럼 느끼지 않도록 하십시오.
>
> **참고: 브라우저 요구 사항** — 스트리밍 기능은 Fetch Streams API(`response.body.getReader()`)를 사용하며, 이는 크롬, 엣지, 파이어폭스, 사파리 등의 완전한 브라우저에서만 작동합니다. VS Code 내장 Simple Browser에서는 이 기능이 작동하지 않으며, 해당 웹뷰가 ReadableStream API를 지원하지 않기 때문입니다. Simple Browser를 사용하는 경우, 비스트리밍 버튼은 정상 작동하지만 스트리밍 버튼만 영향을 받습니다. 전체 기능을 원할 경우 외부 브라우저에서 `http://localhost:8083`을 여십시오.

### Low vs High Eagerness

Low Eagerness를 사용해 "What is 15% of 200?" 같은 간단한 질문을 해보면 즉시 직접적인 답변을 얻을 수 있습니다. 이제 High Eagerness를 사용해 "고트래픽 API를 위한 캐싱 전략 설계" 같은 복잡한 질문을 해보세요. **🔴 스트림 응답(실시간)** 버튼을 클릭하면 모델의 상세 추론 과정이 토큰 단위로 나타나는 것을 볼 수 있습니다. 같은 모델, 같은 질문 구조지만 프롬프트가 사고 깊이를 지시합니다.

### 작업 실행 (도구 서문)

다단계 워크플로에는 사전 계획 및 진행 내레이터가 도움이 됩니다. 모델은 할 일 개요를 제시하고 각 단계를 설명한 뒤 결과를 요약합니다.

### 자기 반영 코드

"이메일 유효성 검사 서비스 생성"을 시도해 보세요. 코드만 생성하고 멈추는 대신, 모델이 생성된 코드를 품질 기준에 따라 평가하고, 약점을 파악하며, 개선을 진행합니다. 생산 수준에 도달할 때까지 반복하는 과정을 볼 수 있습니다.

### 구조적 분석

코드 리뷰에는 일관된 평가 기준이 필요합니다. 모델은 고정된 범주(정확성, 관행, 성능, 보안)와 심각도 수준을 이용해 코드를 분석합니다.

### 다중 회차 챗

"Spring Boot가 무엇인가요?"라고 묻고 즉시 "예제를 보여줘"라고 이어서 질문하세요. 모델은 첫 질문을 기억하여 Spring Boot 예제를 제공합니다. 기억 기능이 없다면 두 번째 질문은 너무 모호했을 것입니다.

### 단계별 추론

수학 문제를 골라서 Step-by-Step Reasoning과 Low Eagerness로 각각 시도해 보세요. Low eagerness는 빠르지만 불투명한 답만 제공합니다. 단계별 추론은 계산과 결정을 모두 보여줍니다.

### 제한된 출력

특정 형식이나 단어 수가 필요한 경우, 이 패턴은 엄격한 준수를 보장합니다. 꼭 100단어로 된 요약을 글머리 기호 형식으로 생성해 보세요.

## 진짜 배우는 것

**추론 노력은 모든 것을 바꾼다**

GPT-5.2는 프롬프트를 통해 계산 노력을 제어할 수 있게 해줍니다. 노력 수준이 낮으면 최소한의 탐색과 빠른 응답을, 높으면 깊이 있는 사고를 위해 모델이 시간을 할애합니다. 작업 복잡도에 맞는 노력을 매치하는 법을 배우는 중입니다 — 간단한 질문에 시간을 낭비하지 말고, 복잡한 결정도 서두르지 마세요.

**구조는 행동을 안내한다**

프롬프트에 XML 태그가 들어있는 것을 눈여겨보셨나요? 단순 장식이 아닙니다. 모델은 자유형 텍스트보다 구조화된 지시를 훨씬 신뢰성 있게 따릅니다. 다단계 프로세스나 복잡한 논리가 필요할 때, 구조는 모델이 단계와 다음 작업을 추적하는 데 도움을 줍니다.

<img src="../../../translated_images/ko/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*명확한 섹션과 XML 스타일 구조로 구성된 잘 짜인 프롬프트의 해부*

**품질은 자기 평가에서 온다**

자기 반영 패턴은 품질 기준을 명확히 함으로써 작동합니다. 모델이 “잘 하길” 바라는 대신, “잘 한다”는 것이 무엇인지 정확하게 알려줍니다: 정확한 논리, 오류 처리, 성능, 보안 등. 모델은 그 결과물을 평가하고 개선할 수 있습니다. 코드 생성을 복권이 아닌 과정으로 바꾸는 셈입니다.

**컨텍스트는 유한하다**

다중 회차 대화는 각 요청에 메시지 기록을 포함해 작동합니다. 하지만 한계가 분명하며 - 모든 모델에는 최대 토큰 수가 존재합니다. 대화가 길어지면 관련 컨텍스트를 유지하면서 한계에 도달하지 않는 전략이 필요합니다. 이 모듈은 메모리 작동 방식을 보여주고, 이후 요약, 잊기, 검색 시점을 배우게 됩니다.

## 다음 단계

**다음 모듈:** [03-rag - RAG (검색 증강 생성)](../03-rag/README.md)

---

**내비게이션:** [← 이전: Module 01 - 소개](../01-introduction/README.md) | [메인으로](../README.md) | [다음: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 노력하고 있으나, 자동 번역에는 오류나 부정확성이 있을 수 있음을 유념해 주시기 바랍니다. 원본 문서는 해당 언어로 된 원본 문서가 권위 있는 출처로 간주되어야 합니다. 중요한 정보의 경우, 전문적인 인간 번역을 권장합니다. 본 번역의 사용으로 인해 발생하는 오해나 오해석에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->