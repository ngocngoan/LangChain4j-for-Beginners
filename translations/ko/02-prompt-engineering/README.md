# Module 02: GPT-5.2를 활용한 프롬프트 엔지니어링

## 목차

- [배울 내용](../../../02-prompt-engineering)
- [사전 준비 사항](../../../02-prompt-engineering)
- [프롬프트 엔지니어링 이해하기](../../../02-prompt-engineering)
- [프롬프트 엔지니어링 기본](../../../02-prompt-engineering)
  - [제로샷 프롬프트](../../../02-prompt-engineering)
  - [퓨샷 프롬프트](../../../02-prompt-engineering)
  - [사고의 사슬](../../../02-prompt-engineering)
  - [역할 기반 프롬프트](../../../02-prompt-engineering)
  - [프롬프트 템플릿](../../../02-prompt-engineering)
- [고급 패턴](../../../02-prompt-engineering)
- [기존 Azure 리소스 사용](../../../02-prompt-engineering)
- [애플리케이션 스크린샷](../../../02-prompt-engineering)
- [패턴 탐색하기](../../../02-prompt-engineering)
  - [낮은 의욕 vs 높은 의욕](../../../02-prompt-engineering)
  - [작업 실행 (도구 프리앰블)](../../../02-prompt-engineering)
  - [자기 반영 코드](../../../02-prompt-engineering)
  - [구조화된 분석](../../../02-prompt-engineering)
  - [멀티턴 채팅](../../../02-prompt-engineering)
  - [단계별 추론](../../../02-prompt-engineering)
  - [제한된 출력](../../../02-prompt-engineering)
- [진짜 배우는 것](../../../02-prompt-engineering)
- [다음 단계](../../../02-prompt-engineering)

## 배울 내용

<img src="../../../translated_images/ko/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

이전 모듈에서 메모리가 대화형 AI를 가능하게 하는 방식을 살펴보고 GitHub 모델로 기본 상호작용을 수행했습니다. 이제는 Azure OpenAI의 GPT-5.2를 사용해 질문하는 방법—즉 프롬프트 자체에 집중합니다. 프롬프트를 어떻게 구성하느냐가 답변의 질에 큰 영향을 미칩니다. 기본 프롬프트 기법을 복습한 후, GPT-5.2의 기능을 최대한 활용하는 8가지 고급 패턴으로 넘어갑니다.

GPT-5.2를 사용하는 이유는 추론 제어 기능이 도입되어 모델이 답변 전에 얼마나 사고할지 지정할 수 있기 때문입니다. 이는 다양한 프롬프트 전략의 차이를 명확히 하고 각 방식을 언제 사용하는지 이해하는 데 도움을 줍니다. 또한 Azure는 GitHub 모델에 비해 GPT-5.2에 대해 더 적은 요율 제한을 제공합니다.

## 사전 준비 사항

- 모듈 01 완료 (Azure OpenAI 리소스 배포됨)
- 루트 디렉터리에 Azure 자격 증명이 포함된 `.env` 파일 (`azd up` 명령으로 모듈 01에서 생성됨)

> **참고:** 모듈 01을 완료하지 않았다면 먼저 그 배포 지침을 따르세요.

## 프롬프트 엔지니어링 이해하기

<img src="../../../translated_images/ko/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

프롬프트 엔지니어링은 원하는 결과를 일관되게 얻도록 입력 텍스트를 설계하는 것입니다. 단순한 질문이 아니라, 모델이 정확히 무엇을 어떻게 처리해야 하는지 이해하도록 요청을 구조화하는 것입니다.

동료에게 지시를 내리는 것과 비슷합니다. "버그 고쳐"는 모호하지만, "UserService.java 45번째 줄의 널 포인터 예외를 널 체크 추가로 고쳐"는 구체적입니다. 언어 모델도 마찬가지입니다—구체성과 구조가 중요합니다.

<img src="../../../translated_images/ko/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j는 인프라스트럭처—모델 연결, 메모리, 메시지 유형—를 제공하며, 프롬프트 패턴은 그 인프라를 통해 보내는 신중히 구조화된 텍스트 그 자체입니다. 핵심 빌딩 블록은 AI의 동작과 역할을 설정하는 `SystemMessage`와 실제 요청을 담는 `UserMessage`입니다.

## 프롬프트 엔지니어링 기본

<img src="../../../translated_images/ko/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

이 모듈의 고급 패턴에 들어가기 전에 다섯 가지 기본 프롬프트 기법을 복습하겠습니다. 모든 프롬프트 엔지니어가 알아야 할 빌딩 블록입니다. [퀵 스타트 모듈](../00-quick-start/README.md#2-prompt-patterns)을 이미 진행했다면 이 기법들을 경험했을 텐데, 여기에 그 개념적 틀이 있습니다.

### 제로샷 프롬프트

가장 간단한 접근법: 예시 없이 모델에 직접 지시를 제공합니다. 모델은 훈련만으로 작업을 이해하고 수행합니다. 예상되는 동작이 명확한 간단한 요청에 효과적입니다.

<img src="../../../translated_images/ko/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*예시 없이 직접 지시—명령만으로 작업을 유추하는 모델*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 응답: "긍정적"
```

**사용 시기:** 간단한 분류, 직접 질문, 번역, 추가 안내 없이 모델이 처리 가능한 작업.

### 퓨샷 프롬프트

모델이 따라야 할 패턴을 보여주는 예시를 제공합니다. 모델은 예시를 통해 기대하는 입력-출력 형식을 배우고 새 입력에 적용합니다. 원하는 형식이나 동작이 분명하지 않을 때 일관성을 크게 향상시킵니다.

<img src="../../../translated_images/ko/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*예시를 통해 학습—패턴 인식 후 새 입력에 적용하는 모델*

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

**사용 시기:** 맞춤 분류, 일관된 포맷, 도메인 특화 작업, 제로샷 결과가 불안정할 때.

### 사고의 사슬

모델에 단계별 추론 과정을 보여달라고 요청합니다. 답변에 바로 도달하지 않고 문제를 분해해 각 부분을 명확하게 처리합니다. 수학, 논리, 다단계 추론 작업의 정확도를 높입니다.

<img src="../../../translated_images/ko/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*단계별 추론—복잡한 문제를 명확한 논리 단계로 분해*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 모델은 보여줍니다: 15 - 8 = 7, 그 다음 7 + 12 = 19개의 사과를
```

**사용 시기:** 수학 문제, 논리 퍼즐, 디버깅, 추론 과정 공개가 정확도와 신뢰성을 높이는 경우.

### 역할 기반 프롬프트

질문 전에 AI의 페르소나나 역할을 설정합니다. 이는 답변의 어조, 깊이, 초점을 결정하는 컨텍스트를 제공합니다. "소프트웨어 아키텍트", "주니어 개발자", "보안 감사자"가 각각 다른 조언을 합니다.

<img src="../../../translated_images/ko/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*컨텍스트와 페르소나 설정—같은 질문도 할당된 역할에 따라 다르게 답변*

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

**사용 시기:** 코드 리뷰, 튜터링, 도메인 분석, 특정 전문 지식 수준 또는 관점 맞춤 답변이 필요할 때.

### 프롬프트 템플릿

변수 자리 표시자가 포함된 재사용 가능한 프롬프트를 만듭니다. 매번 새 프롬프트를 작성하는 대신 한 번 템플릿으로 정의하고 값을 채웁니다. LangChain4j의 `PromptTemplate` 클래스는 `{{variable}}` 문법으로 쉽게 지원합니다.

<img src="../../../translated_images/ko/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*변수 자리 표시자 포함 재사용 프롬프트—한 템플릿, 다양한 활용*

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

**사용 시기:** 서로 다른 입력으로 반복 쿼리, 배치 처리, 재사용 가능한 AI 워크플로우 구축, 프롬프트 구조는 일정하지만 데이터만 바뀌는 경우.

---

이 다섯 가지 기본기는 대부분의 프롬프트 작업에서 견고한 도구 키트를 제공합니다. 이 모듈 나머지는 GPT-5.2의 추론 제어, 자기 평가, 구조화 출력 기능을 활용하는 **8가지 고급 패턴**으로 확장합니다.

## 고급 패턴

기본기를 다뤘으니, 이 모듈을 특별하게 만드는 8가지 고급 패턴으로 넘어갑니다. 모든 문제에 같은 접근법이 맞는 것은 아닙니다. 어떤 질문은 빠른 답을, 어떤 질문은 깊은 사고를 필요로 합니다. 어떤 경우는 추론 과정을 보이길 원하고, 어떤 경우는 결과만 원합니다. 다음 각 패턴은 다른 시나리오에 최적화되어 있으며, GPT-5.2의 추론 제어 기능으로 그 차이가 더욱 뚜렷해집니다.

<img src="../../../translated_images/ko/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*8가지 프롬프트 엔지니어링 패턴 개요 및 활용 사례*

<img src="../../../translated_images/ko/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2의 추론 제어로 모델이 사고할 양을 지정 가능—빠른 직접 답변부터 깊은 탐구까지*

**낮은 의욕 (빠르고 집중된 답변)** - 빠르고 직접적인 답변을 원하는 간단한 질문에 적합합니다. 모델은 최소한의 추론만 수행—최대 2단계. 계산, 조회, 명확한 질문에 사용.

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

> 💡 **GitHub Copilot으로 탐색하기:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 열고 질문해 보세요:
> - "낮은 의욕과 높은 의욕 프롬프트 패턴의 차이는 무엇인가요?"
> - "프롬프트 내 XML 태그가 AI 응답 구조화에 어떻게 도움이 되나요?"
> - "언제 자기 반영 패턴을 쓰고, 언제 직접 지시를 써야 하나요?"

**높은 의욕 (깊고 철저한 분석)** - 전체적 분석이 필요한 복잡한 문제에 적합합니다. 모델이 깊이 탐구하며 상세한 추론을 보여줍니다. 시스템 설계, 아키텍처 결정, 복잡한 연구에 사용.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**작업 실행 (단계별 진행)** - 다단계 워크플로우용. 모델이 사전 계획을 제시하고 작업하면서 각 단계를 설명, 마지막에 요약까지 합니다. 마이그레이션, 구현, 다단계 프로세스에 적합.

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

사고의 사슬 프롬프트는 모델에 추론 과정을 명시적으로 요청하여 복잡한 작업의 정확도를 높입니다. 단계별 분해는 인간과 AI 모두 논리를 이해하는 데 도움을 줍니다.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 채팅에서 시도:** 이 패턴에 대해 물어보세요:
> - "장기 실행 작업에 작업 실행 패턴을 어떻게 조정할 수 있을까요?"
> - "운영 환경에서 도구 프리앰블 구조화 최선 사례는 무엇인가요?"
> - "중간 진행 상황 업데이트를 UI에 캡처하고 표시하려면 어떻게 해야 하나요?"

<img src="../../../translated_images/ko/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*다단계 작업을 위한 계획 → 실행 → 요약 워크플로우*

**자기 반영 코드** - 프로덕션 품질 코드를 생성할 때 사용. 모델이 프로덕션 표준과 적절한 오류 처리를 따르는 코드를 생성합니다. 새 기능이나 서비스를 개발할 때 적합.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ko/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*반복 개선 사이클—생성 → 평가 → 문제 확인 → 개선 → 반복*

**구조화된 분석** - 일관된 평가가 필요할 때. 모델이 고정된 프레임워크(정확성, 관행, 성능, 보안, 유지보수성)로 코드를 검토합니다. 코드 리뷰나 품질 평가에 적합.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 채팅에서 시도:** 구조화된 분석에 대해 질문해 보세요:
> - "다양한 유형 코드 리뷰에 맞게 분석 프레임워크를 어떻게 맞춤화할 수 있나요?"
> - "구조화된 출력을 프로그래밍적으로 파싱하고 처리하는 최선의 방법은?"
> - "서로 다른 리뷰 세션 간 일관된 심각도 레벨을 보장하려면?"

<img src="../../../translated_images/ko/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*심각도 레벨이 포함된 일관된 코드 리뷰 프레임워크*

**멀티턴 채팅** - 대화에 컨텍스트가 필요한 경우. 모델이 이전 메시지를 기억하고 이를 기반으로 응답을 만듭니다. 인터랙티브 도움 세션이나 복잡한 Q&A에 사용.

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

*여러 턴에 걸쳐 대화 컨텍스트가 누적되어 토큰 한계에 도달할 때까지 지속*

**단계별 추론** - 명확한 논리가 필요한 문제에. 모델이 각 단계를 명시적으로 추론 과정을 보여줍니다. 수학 문제, 논리 퍼즐, 사고 과정을 이해해야 할 때 사용.

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

*문제를 명확한 논리 단계로 분해*

**제한된 출력** - 특정 형식 요구사항이 있는 응답용. 모델이 형식과 길이 규칙을 엄격히 준수합니다. 요약이나 정확한 출력 구조가 필요한 경우 사용.

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

*특정 형식, 길이 및 구조 요구사항 적용*

## 기존 Azure 리소스 사용하기

**배포 확인:**

루트 디렉터리에 Azure 자격 증명이 포함된 `.env` 파일이 존재하는지 확인 (모듈 01 중 생성됨):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT를 표시해야 합니다
```

**애플리케이션 시작:**

> **참고:** 이미 모듈 01에서 `./start-all.sh`을 사용해 모든 애플리케이션을 시작했다면 이 모듈이 포트 8083에서 실행 중입니다. 아래의 시작 명령은 건너뛰고 바로 http://localhost:8083 에 접속하세요.

**옵션 1: Spring Boot 대시보드 사용 (VS Code 사용자에게 권장)**

개발 컨테이너에 Spring Boot 대시보드 확장팩이 포함되어 있어 모든 Spring Boot 애플리케이션을 시각적으로 관리할 수 있습니다. VS Code 좌측 Activity Bar의 Spring Boot 아이콘에서 찾을 수 있습니다.

Spring Boot 대시보드에서 할 수 있는 일:
- 작업 공간 내 모든 Spring Boot 애플리케이션 목록 확인
- 클릭 한 번으로 애플리케이션 시작/중지
- 애플리케이션 로그 실시간 보기
- 애플리케이션 상태 모니터링
"prompt-engineering" 옆의 재생 버튼을 클릭하여 이 모듈을 시작하거나, 모든 모듈을 한 번에 시작하세요.

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
cd ..  # 루트 디렉터리에서
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

두 스크립트 모두 루트 `.env` 파일에서 환경 변수를 자동으로 불러오며, JAR가 없으면 빌드합니다.

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

*특징과 사용 사례가 포함된 8가지 프롬프트 엔지니어링 패턴을 모두 보여주는 메인 대시보드*

## 패턴 탐색

웹 인터페이스를 통해 다양한 프롬팅 전략을 실험할 수 있습니다. 각 패턴은 다른 문제를 해결하므로, 언제 어떤 접근법이 뛰어난지 직접 확인해 보세요.

> **참고: 스트리밍 vs 비스트리밍** — 모든 패턴 페이지에는 두 개의 버튼이 있습니다: **🔴 스트림 응답 (실시간)** 과 **비스트리밍** 옵션. 스트리밍은 서버-센트 이벤트(SSE)를 사용하여 모델이 생성하는 토큰을 실시간으로 표시하므로 바로 진행 상황을 볼 수 있습니다. 비스트리밍 옵션은 전체 응답이 완료될 때까지 기다린 후 표시합니다. 깊은 추론이 필요한 프롬프트(예: 고열정, 자기반영 코드)에서는 비스트리밍 호출이 매우 오래 걸릴 수 있으며, 때로는 수 분 동안 아무 피드백이 없을 수도 있습니다. **복잡한 프롬프트 실험 시에는 스트리밍을 사용하여 모델이 작동하는 것을 확인하고 요청이 타임아웃된 것처럼 보이지 않게 하세요.**
>
> **참고: 브라우저 요구사항** — 스트리밍 기능은 Fetch Streams API(`response.body.getReader()`)를 사용하며, 이는 Chrome, Edge, Firefox, Safari와 같은 완전한 브라우저가 필요합니다. VS Code 내장 단순 브라우저(Simple Browser)에서는 작동하지 않습니다. 이유는 해당 웹뷰가 ReadableStream API를 지원하지 않기 때문입니다. 단순 브라우저를 사용하면 비스트리밍 버튼은 정상 작동하지만, 스트리밍 버튼만 영향을 받습니다. 전체 기능 경험을 위해 외부 브라우저에서 `http://localhost:8083` 을 여세요.

### 저열정 vs 고열정

"15%가 200의 얼마인가?" 같은 간단한 질문은 저열정으로 하면 즉각적이고 직접적인 답을 받을 수 있습니다. 반면 "고트래픽 API에 대한 캐싱 전략 설계" 같은 복잡한 질문은 고열정으로 시도해 보세요. **🔴 스트림 응답 (실시간)** 을 클릭하면 모델의 자세한 추론이 토큰 단위로 나타납니다. 같은 모델, 같은 질문 구성이라도 프롬프트가 얼마나 깊게 사고해야 하는지 알려줍니다.

### 작업 실행 (도구 프리앰블)

여러 단계의 워크플로우는 사전 계획과 진행 상황 내레이션의 이점이 있습니다. 모델이 수행할 작업을 개요로 만들고, 각 단계를 설명하며, 최종 결과를 요약합니다.

### 자기반영 코드

"이메일 검증 서비스 생성"을 시도해 보세요. 단순히 코드를 생성하고 멈추지 않고, 모델이 생성한 코드를 품질 기준에 따라 평가하고 약점을 식별하며 개선합니다. 코드가 프로덕션 기준에 부합할 때까지 반복하는 모습을 볼 수 있습니다.

### 구조화된 분석

코드 리뷰에는 일관된 평가 프레임워크가 필요합니다. 모델은 고정된 카테고리(정확성, 관행, 성능, 보안)와 심각도 수준으로 코드를 분석합니다.

### 다중 턴 채팅

"Spring Boot가 무엇인가요?" 라고 묻고 바로 "예제를 보여주세요"라고 추가 질문하세요. 모델이 첫 질문을 기억하여 Spring Boot 예제를 구체적으로 제공합니다. 기억 기능이 없다면 두 번째 질문은 너무 모호할 것입니다.

### 단계별 추론

수학 문제 하나를 골라 단계별 추론과 저열정으로 각각 시도해 보세요. 저열정은 답만 빠르게 주며 불투명합니다. 단계별 추론은 모든 계산과 결정을 보여줍니다.

### 제약 출력

특정 형식이나 단어 수가 필요할 때 이 패턴이 엄격히 준수하도록 합니다. 정확히 100단어 요약을 글머리표 형식으로 생성해 보세요.

## 여러분이 실제로 배우는 것

**추론 노력은 모든 것을 바꿉니다**

GPT-5.2는 프롬프트를 통해 계산 노력을 제어할 수 있습니다. 저노력은 최소 탐색으로 빠른 응답을 의미합니다. 고노력은 모델이 깊게 생각하는 데 시간을 할애합니다. 작업 복잡도에 맞춰 노력을 조절하는 법을 배우고 있습니다. 간단한 질문에 시간을 낭비하지 말고, 복잡한 결정은 서두르지 마세요.

**구조가 행동을 안내합니다**

프롬프트 내 XML 태그를 보셨나요? 장식이 아닙니다. 모델은 자유형 텍스트보다 구조화된 지침을 더 신뢰성 있게 따릅니다. 다단계 프로세스나 복잡한 논리를 필요로 할 때, 구조는 모델이 현재 위치와 다음 단계를 추적하는 데 도움을 줍니다.

<img src="../../../translated_images/ko/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*명확한 섹션과 XML 스타일 조직으로 잘 구조화된 프롬프트의 해부*

**자가 평가를 통한 품질**

자기반영 패턴은 품질 기준을 명시하여 작동합니다. 모델이 "잘하길 기대"하는 대신, "올바름"이란 무엇인지 정확히 알려줍니다: 정확한 논리, 오류 처리, 성능, 보안. 모델은 자체 출력을 평가하고 개선할 수 있습니다. 덕분에 코드 생성이 복권이 아닌 프로세스가 됩니다.

**맥락은 유한합니다**

다중 턴 대화는 각 요청에 메시지 기록을 포함시켜 작동합니다. 그러나 한계가 있습니다 – 모든 모델은 최대 토큰 수가 정해져 있습니다. 대화가 길어질수록 관련 맥락을 유지하면서 한계에 도달하지 않는 전략이 필요합니다. 이 모듈은 메모리 작동 방식을 보여 주며, 이후 요약할 때, 잊을 때, 검색할 때를 배우게 됩니다.

## 다음 단계

**다음 모듈:** [03-rag - RAG (검색 확장 생성)](../03-rag/README.md)

---

**네비게이션:** [← 이전: 모듈 01 - 소개](../01-introduction/README.md) | [메인으로 돌아가기](../README.md) | [다음: 모듈 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
본 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 노력하고 있으나, 자동 번역은 오류나 부정확성이 포함될 수 있음을 유의하시기 바랍니다. 원문 문서는 권위 있는 출처로 간주되어야 합니다. 중요한 정보에 대해서는 전문적인 인간 번역을 권장합니다. 본 번역 사용으로 인해 발생하는 오해나 잘못된 해석에 대해서는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->