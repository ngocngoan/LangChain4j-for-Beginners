# Module 02: GPT-5.2를 활용한 프롬프트 엔지니어링

## 목차

- [학습 내용](../../../02-prompt-engineering)
- [사전 준비 사항](../../../02-prompt-engineering)
- [프롬프트 엔지니어링 이해하기](../../../02-prompt-engineering)
- [프롬프트 엔지니어링 기본](../../../02-prompt-engineering)
  - [제로샷 프롬프트](../../../02-prompt-engineering)
  - [퓨샷 프롬프트](../../../02-prompt-engineering)
  - [사고의 연쇄](../../../02-prompt-engineering)
  - [역할 기반 프롬프트](../../../02-prompt-engineering)
  - [프롬프트 템플릿](../../../02-prompt-engineering)
- [고급 패턴](../../../02-prompt-engineering)
- [기존 Azure 리소스 활용](../../../02-prompt-engineering)
- [애플리케이션 스크린샷](../../../02-prompt-engineering)
- [패턴 탐구](../../../02-prompt-engineering)
  - [낮은 열의 vs 높은 열의](../../../02-prompt-engineering)
  - [작업 실행 (도구 머리말)](../../../02-prompt-engineering)
  - [셀프 리플렉팅 코드](../../../02-prompt-engineering)
  - [구조화된 분석](../../../02-prompt-engineering)
  - [다중 턴 채팅](../../../02-prompt-engineering)
  - [단계별 추론](../../../02-prompt-engineering)
  - [제한된 출력](../../../02-prompt-engineering)
- [실제로 배우는 것](../../../02-prompt-engineering)
- [다음 단계](../../../02-prompt-engineering)

## 학습 내용

<img src="../../../translated_images/ko/what-youll-learn.c68269ac048503b2.webp" alt="학습 내용" width="800"/>

이전 모듈에서 기억 기능이 대화형 AI에 어떻게 활용되는지 살펴보고 GitHub 모델을 사용해 기본 상호작용을 경험했습니다. 이제는 Azure OpenAI의 GPT-5.2를 사용해 질문하는 방법—즉, 프롬프트 자체—에 집중합니다. 프롬프트를 구성하는 방식이 응답 품질에 결정적인 영향을 미칩니다. 기본 프롬프트 기법을 복습한 뒤, GPT-5.2의 기능을 최대한 활용하는 8가지 고급 패턴으로 나아갑니다.

GPT-5.2는 추론 제어 기능을 도입했기에 사용합니다—모델에게 답변 전 얼마나 고민할지 지정할 수 있습니다. 이는 다양한 프롬프트 전략을 더욱 명확하게 보여주며, 각각의 접근법을 언제 써야 할지 이해하는 데 도움을 줍니다. 또한 GPT-5.2는 GitHub 모델보다 Azure의 호출 제한이 적어 유리합니다.

## 사전 준비 사항

- 모듈 01 완료 (Azure OpenAI 리소스 배포)
- 루트 디렉터리에 Azure 자격 증명이 포함된 `.env` 파일 (모듈 01에서 `azd up`으로 생성됨)

> **참고:** 모듈 01을 완료하지 않았다면, 먼저 그 배포 지침을 따라주세요.

## 프롬프트 엔지니어링 이해하기

<img src="../../../translated_images/ko/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="프롬프트 엔지니어링이란?" width="800"/>

프롬프트 엔지니어링은 원하는 결과를 일관되게 얻기 위한 입력 텍스트를 설계하는 것입니다. 단순히 질문하는 것이 아니라, 모델이 무엇을 어떻게 전달해야 할지 정확히 이해하도록 요청을 체계적으로 구성하는 일입니다.

동료에게 지시를 내리는 것과 비슷하게 생각하세요. "버그를 고쳐"라고 모호하게 말하는 대신 "UserService.java 45번째 줄에서 널 체크를 추가해 널 포인터 예외를 고쳐"라고 구체적으로 말하는 것입니다. 언어 모델도 마찬가지로 구체성과 구조가 중요합니다.

<img src="../../../translated_images/ko/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j의 역할" width="800"/>

LangChain4j는 모델 연결, 메모리, 메시지 유형 같은 인프라를 제공하고, 프롬프트 패턴은 그 인프라를 통해 보내는 정교하게 구조화된 텍스트입니다. 주요 구성 요소는 AI의 동작과 역할을 설정하는 `SystemMessage`와 실제 요청을 담는 `UserMessage`입니다.

## 프롬프트 엔지니어링 기본

<img src="../../../translated_images/ko/five-patterns-overview.160f35045ffd2a94.webp" alt="프롬프트 엔지니어링 5가지 패턴 개요" width="800"/>

이 모듈의 고급 패턴에 들어가기 전에 다섯 가지 기본 프롬프트 기법을 복습해봅시다. 모두가 알아야 할 기초이며, 이미 [퀵 스타트 모듈](../00-quick-start/README.md#2-prompt-patterns)을 진행했다면 이 개념들을 접한 적 있을 것입니다.

### 제로샷 프롬프트

가장 단순한 방법: 예시 없이 직접 지시를 제공합니다. 모델은 전적으로 훈련된 지식에 의존하여 작업을 이해하고 실행합니다. 명확한 기대 동작이 있는 단순 요청에 적합합니다.

<img src="../../../translated_images/ko/zero-shot-prompting.7abc24228be84e6c.webp" alt="제로샷 프롬프트" width="800"/>

*예시 없이 직접 지시 — 모델이 지시만으로 작업을 추론*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 응답: "긍정적"
```

**사용 시기:** 간단한 분류, 직접 질문, 번역 또는 추가 안내 없이 모델이 처리 가능한 작업에 적합합니다.

### 퓨샷 프롬프트

모델이 따라야 할 패턴을 보여주는 예시를 제공합니다. 모델은 예시에서 입력-출력 형식을 학습해 새 입력에 적용합니다. 기대하는 형식이나 동작이 명확하지 않은 작업에서 일관성을 크게 향상시킵니다.

<img src="../../../translated_images/ko/few-shot-prompting.9d9eace1da88989a.webp" alt="퓨샷 프롬프트" width="800"/>

*예시로 학습 — 모델이 패턴을 인식하고 새 입력에 적용*

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

**사용 시기:** 맞춤 분류, 일관된 포맷, 도메인 특화 작업, 제로샷 결과가 일관되지 않을 때 사용합니다.

### 사고의 연쇄

모델에게 단계별로 추론 과정을 보여달라고 요청합니다. 바로 답을 내지 않고 문제를 나누어 각 부분을 명확히 작업합니다. 수학, 논리, 다단계 추론 작업에서 정확도를 높입니다.

<img src="../../../translated_images/ko/chain-of-thought.5cff6630e2657e2a.webp" alt="사고의 연쇄 프롬프트" width="800"/>

*단계별 추론 — 복잡한 문제를 명확한 논리 단계로 분해*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 모델은 15 - 8 = 7을 보여주고, 그 다음에 7 + 12 = 19 사과를 보여줍니다
```

**사용 시기:** 수학 문제, 논리 퍼즐, 디버깅 등 추론 과정을 명확히 보여야 정확도와 신뢰가 향상되는 경우에 적합합니다.

### 역할 기반 프롬프트

질문 전에 AI의 페르소나 또는 역할을 지정합니다. 답변의 어조, 깊이, 초점을 형성하는 문맥을 제공합니다. "소프트웨어 아키텍트"와 "주니어 개발자", "보안 감사자"는 각각 다른 조언을 제공합니다.

<img src="../../../translated_images/ko/role-based-prompting.a806e1a73de6e3a4.webp" alt="역할 기반 프롬프트" width="800"/>

*문맥과 페르소나 설정 — 같은 질문도 역할에 따라 다른 답변*

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

**사용 시기:** 코드 리뷰, 튜터링, 도메인 분석, 특정 전문성 수준이나 관점에 맞춘 답변이 필요할 때 사용합니다.

### 프롬프트 템플릿

변수 자리표시자가 포함된 재사용 가능한 프롬프트를 만듭니다. 매번 새 프롬프트를 작성하는 대신 한 번 템플릿을 정의하고 다양한 값으로 채웁니다. LangChain4j의 `PromptTemplate` 클래스는 `{{variable}}` 문법으로 쉽게 만듭니다.

<img src="../../../translated_images/ko/prompt-templates.14bfc37d45f1a933.webp" alt="프롬프트 템플릿" width="800"/>

*변수 자리표시자가 있는 재사용 가능한 프롬프트 — 하나의 템플릿, 다양한 활용*

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

**사용 시기:** 다른 입력으로 반복 요청하거나, 배치 처리, 재사용 가능한 AI 워크플로 구축, 프롬프트 구조는 같고 데이터만 바뀔 때 적합합니다.

---

이 다섯 가지 기본을 통해 대부분의 프롬프트 작업에 튼튼한 도구를 갖춥니다. 이 모듈의 나머지는 GPT-5.2의 추론 제어, 자기 평가, 구조화된 출력 기능을 활용한 **8가지 고급 패턴**을 다룹니다.

## 고급 패턴

기본을 마쳤으니 이 모듈을 특별하게 만드는 8가지 고급 패턴으로 넘어갑니다. 모든 문제에 동일한 접근법이 필요한 것은 아닙니다. 어떤 질문은 빠른 답변이 필요하고, 어떤 것은 깊은 사고를 요구합니다. 어떤 경우는 명시적 추론이 필요하고, 어떤 경우는 결과만 필요합니다. 아래 각 패턴은 다른 시나리오에 최적화되어 있으며, GPT-5.2의 추론 제어 기능으로 그 차이가 더욱 도드라집니다.

<img src="../../../translated_images/ko/eight-patterns.fa1ebfdf16f71e9a.webp" alt="8가지 프롬프트 엔지니어링 패턴" width="800"/>

*8가지 프롬프트 엔지니어링 패턴과 사용 사례 개요*

<img src="../../../translated_images/ko/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 추론 제어" width="800"/>

*GPT-5.2의 추론 제어는 모델이 얼마나 깊이 생각할지 지정할 수 있게 함 — 빠른 직접 답변부터 깊은 탐색까지*

<img src="../../../translated_images/ko/reasoning-effort.db4a3ba5b8e392c1.webp" alt="추론 노력 비교" width="800"/>

*낮은 열의(빠르고 직접적) vs 높은 열의(철저하고 탐색적) 추론 접근법*

**낮은 열의 (빠르고 집중된)** - 빠른 직접 답변이 필요한 간단한 질문에 적합합니다. 모델은 최소한의 추론만 수행하며 최대 2단계로 제한합니다. 계산, 조회 또는 직관적 질문에 사용하세요.

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

> 💡 **GitHub Copilot으로 탐구:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)를 열고 다음 질문을 해보세요:
> - "낮은 열의와 높은 열의 프롬프트 패턴 차이는 무엇인가요?"
> - "프롬프트 내 XML 태그가 AI 응답 구조를 어떻게 돕나요?"
> - "자기 반성 패턴과 직접 지시를 언제 써야 하나요?"

**높은 열의 (깊고 철저한)** - 종합적인 분석이 필요한 복잡한 문제에 적합합니다. 모델이 철저히 탐색하며 꼼꼼한 추론을 보여줍니다. 시스템 설계, 아키텍처 결정, 복잡한 연구에 사용하세요.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**작업 실행 (단계별 진행)** - 다단계 워크플로우에 적합합니다. 모델이 사전 계획을 제공하고 각 단계를 진행하며 마지막에 요약합니다. 마이그레이션, 구현 또는 다단계 프로세스에 사용합니다.

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

사고의 연쇄 프롬프트는 모델에 추론 과정을 명확하게 보여주도록 요청하여 복잡한 작업의 정확도를 향상시킵니다. 단계별 분해는 인간과 AI 모두 논리를 이해하는 데 도움을 줍니다.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat에서 시도해보세요:** 이 패턴에 대해 질문하세요:
> - "장시간 작업을 위해 작업 실행 패턴을 어떻게 변형할 수 있나요?"
> - "운영 환경에서 도구 머리말을 구조화하는 최선의 방법은 무엇인가요?"
> - "UI에서 중간 진행 상황 업데이트를 어떻게 캡처하고 표시할 수 있나요?"

<img src="../../../translated_images/ko/task-execution-pattern.9da3967750ab5c1e.webp" alt="작업 실행 패턴" width="800"/>

*계획 → 실행 → 요약하는 다단계 작업 워크플로우*

**셀프 리플렉팅 코드** - 프로덕션 품질 코드를 생성하는 데 적합합니다. 모델이 프로덕션 표준에 맞춰 에러 핸들링을 고려하며 코드를 만듭니다. 새 기능 또는 서비스를 구축할 때 사용하세요.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ko/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="자기 반성 주기" width="800"/>

*반복적 개선 루프 - 생성 → 평가 → 문제 식별 → 개선 → 반복*

**구조화된 분석** - 일관된 평가를 위해 모델이 고정된 프레임워크(정확성, 관행, 성능, 보안, 유지보수성)를 사용해 코드를 검토합니다. 코드 리뷰나 품질 평가에 사용하세요.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat에서 시도해보세요:** 구조화된 분석에 관해 질문하세요:
> - "다양한 유형의 코드 리뷰에 맞게 분석 프레임워크를 어떻게 맞춤화할 수 있나요?"
> - "구조화된 출력을 프로그래밍 방식으로 파싱하고 활용하는 최선의 방법은?"
> - "다양한 리뷰 세션 간에 일관된 중대도 수준을 어떻게 보장하나요?"

<img src="../../../translated_images/ko/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="구조화된 분석 패턴" width="800"/>

*중대도 수준이 포함된 일관된 코드 리뷰를 위한 프레임워크*

**다중 턴 채팅** - 문맥이 필요한 대화에 적합합니다. 모델이 이전 메시지를 기억하고 계속 쌓아나갑니다. 인터랙티브 도움말 세션이나 복잡한 Q&A에 사용하세요.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ko/context-memory.dff30ad9fa78832a.webp" alt="대화 문맥 메모리" width="800"/>

*여러 턴에 걸쳐 대화 문맥이 누적되어 토큰 한도에 도달할 때까지 유지*

**단계별 추론** - 명확한 논리가 필요한 문제에 적합합니다. 모델이 각 단계를 명시적으로 추론해 보여줍니다. 수학 문제, 논리 퍼즐, 추론 과정을 이해해야 할 때 사용하세요.

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

**제한된 출력** - 특정 형식 요건이 있는 답변에 적합합니다. 모델이 형식과 길이를 엄격하게 준수하도록 합니다. 요약 또는 정확한 출력 구조가 필요한 경우 사용합니다.

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

*특정 형식, 길이, 구조 요건 강제 적용*

## 기존 Azure 리소스 활용

**배포 확인:**

루트 디렉터리에 Azure 자격증명이 포함된 `.env` 파일이 있는지 확인하세요 (모듈 01 배포 중 생성됨):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT를 보여야 합니다
```

**애플리케이션 시작:**

> **참고:** 모듈 01에서 `./start-all.sh`를 사용해 이미 모든 애플리케이션을 시작했다면, 이 모듈은 이미 8083 포트에서 실행 중입니다. 아래 시작 명령은 건너뛰고 바로 http://localhost:8083 으로 접속하세요.

**옵션 1: Spring Boot 대시보드 사용하기 (VS Code 사용자 권장)**

개발 컨테이너에는 Spring Boot 대시보드 확장 프로그램이 포함되어 있어 모든 Spring Boot 애플리케이션을 시각적으로 관리할 수 있습니다. VS Code 왼쪽의 액티비티 바에서 Spring Boot 아이콘을 찾아 사용하세요.
Spring Boot 대시보드에서 다음을 할 수 있습니다:
- 워크스페이스의 모든 사용 가능한 Spring Boot 애플리케이션 보기
- 한 번의 클릭으로 애플리케이션 시작/중지
- 애플리케이션 로그를 실시간으로 보기
- 애플리케이션 상태 모니터링

"prompt-engineering" 옆에 있는 재생 버튼을 클릭하면 이 모듈을 시작하거나 모든 모듈을 한 번에 시작할 수 있습니다.

<img src="../../../translated_images/ko/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**옵션 2: 셸 스크립트 사용**

모든 웹 애플리케이션(모듈 01-04) 시작:

**Bash:**
```bash
cd ..  # 루트 디렉터리에서
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

두 스크립트 모두 루트 `.env` 파일에서 환경 변수를 자동으로 불러오며, JAR 파일이 없으면 빌드합니다.

> **참고:** 시작 전에 모든 모듈을 수동으로 빌드하고 싶다면:
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

*모든 8가지 프롬프트 엔지니어링 패턴과 그 특징 및 사용 사례가 표시된 메인 대시보드*

## 패턴 탐색

웹 인터페이스에서는 다양한 프롬프트 전략을 실험할 수 있습니다. 각 패턴은 다른 문제를 해결하므로, 각각의 접근 방식이 빛을 발하는 때를 확인해보세요.

### 낮은 적극성 vs 높은 적극성

"200의 15%는 얼마입니까?"와 같은 간단한 질문을 낮은 적극성으로 물어보세요. 즉시 직접적인 답변을 받게 됩니다. 이제 "고트래픽 API를 위한 캐싱 전략을 설계하라"와 같은 복잡한 질문을 높은 적극성으로 해보세요. 모델이 느려지면서 자세한 추론을 제공합니다. 같은 모델, 같은 질문 구조지만 프롬프트가 얼마나 심층적으로 생각할지 지시하는 것입니다.

<img src="../../../translated_images/ko/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*최소한의 추론으로 빠른 계산*

<img src="../../../translated_images/ko/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*포괄적인 캐싱 전략 (2.8MB)*

### 작업 실행 (도구 사전 선언)

다단계 워크플로우는 사전 계획과 진행 상황 내레이션에 도움이 됩니다. 모델은 수행할 작업을 개요로 설명하고 각 단계를 내레이션하며 결과를 요약합니다.

<img src="../../../translated_images/ko/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*단계별 내레이션으로 REST 엔드포인트 생성 (3.9MB)*

### 자기 반영 코드

"이메일 유효성 검사 서비스를 만들어라"를 시도해 보세요. 단순히 코드를 생성하고 멈추는 대신, 모델은 생성, 품질 기준에 따른 평가, 약점 식별, 개선을 반복합니다. 코드가 생산 기준에 부합할 때까지 반복하는 모습을 볼 수 있습니다.

<img src="../../../translated_images/ko/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*완성된 이메일 유효성 검사 서비스 (5.2MB)*

### 구조화된 분석

코드 리뷰에는 일관된 평가 프레임워크가 필요합니다. 모델은 고정된 카테고리(정확성, 관행, 성능, 보안)와 심각도 수준을 사용하여 코드를 분석합니다.

<img src="../../../translated_images/ko/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*프레임워크 기반 코드 리뷰*

### 다중 턴 채팅

"Spring Boot가 무엇인가요?"라고 물은 뒤 바로 "예제를 보여줘"라고 하세요. 모델은 첫 번째 질문을 기억해 Spring Boot 예제를 구체적으로 제공합니다. 메모리가 없다면 두 번째 질문은 너무 모호했을 것입니다.

<img src="../../../translated_images/ko/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*질문 간 컨텍스트 보존*

### 단계별 추론

수학 문제를 선택해 단계별 추론과 낮은 적극성 모두로 시도해 보세요. 낮은 적극성은 답만 빠르게 제공하지만 불투명합니다. 단계별 추론은 모든 계산과 결정을 보여줍니다.

<img src="../../../translated_images/ko/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*명확한 단계가 포함된 수학 문제*

### 제한된 출력

특정 형식이나 단어 수가 필요할 때 이 패턴은 엄격한 준수를 강제합니다. 정확히 100 단어, 글머리표 형식의 요약을 생성해 보세요.

<img src="../../../translated_images/ko/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*형식 제어가 있는 머신 러닝 요약*

## 진짜 배우는 것

**추론 노력으로 모든 것이 달라진다**

GPT-5.2는 프롬프트를 통해 계산 노력을 제어할 수 있게 합니다. 낮은 노력은 최소한의 탐색으로 빠른 응답을 의미합니다. 높은 노력은 모델이 깊이 생각하는 데 시간을 쓴다는 뜻입니다. 과제 복잡도에 맞게 노력을 조절하는 법을 배우는 것입니다 - 간단한 질문에 시간을 낭비하지 말고, 복잡한 결정은 서두르지 마세요.

**구조가 행동을 이끈다**

프롬프트 내 XML 태그를 눈여겨보세요. 장식용이 아닙니다. 모델은 자유형 텍스트보다 구조화된 지시를 더 신뢰성 있게 따릅니다. 다단계 처리나 복잡한 논리가 필요할 때 구조는 모델이 위치와 다음 단계를 추적하도록 돕습니다.

<img src="../../../translated_images/ko/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*명확한 섹션과 XML 스타일 조직이 포함된 잘 구조화된 프롬프트의 해부*

**품질은 자기 평가를 통해**

자기 반영 패턴은 품질 기준을 명시적으로 만듭니다. 모델이 "잘 하길 바라는" 대신 정확한 의미로 "잘 해야 할 것"을 알려줍니다: 올바른 논리, 오류 처리, 성능, 보안. 모델은 자신의 출력물을 평가하고 개선할 수 있습니다. 이것은 코드 생성을 복권이 아닌 과정으로 만듭니다.

**컨텍스트는 유한하다**

다중 턴 대화는 각 요청에 메시지 이력을 포함하는 방식으로 작동합니다. 하지만 최대 토큰 수 제한이 있습니다. 대화가 커질수록 관련 컨텍스트를 유지하면서 한계를 넘지 않는 전략이 필요합니다. 이 모듈은 메모리가 어떻게 작동하는지 보여주며, 이후에는 언제 요약하고, 언제 잊고, 언제 다시 불러와야 하는지 배웁니다.

## 다음 단계

**다음 모듈:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**내비게이션:** [← 이전: 모듈 01 - 소개](../01-introduction/README.md) | [메인으로 돌아가기](../README.md) | [다음: 모듈 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 노력하고 있으나, 자동 번역에는 오류나 부정확성이 포함될 수 있음을 양지해 주시기 바랍니다. 원문 문서가 권위 있는 출처로 간주되어야 합니다. 중요한 정보의 경우 전문 인력의 번역을 권장합니다. 본 번역문의 사용으로 인해 발생하는 오해나 오해에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->