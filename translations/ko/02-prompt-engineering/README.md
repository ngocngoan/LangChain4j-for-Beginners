# 모듈 02: GPT-5.2를 활용한 프롬프트 엔지니어링

## 목차

- [비디오 안내](../../../02-prompt-engineering)
- [학습 내용](../../../02-prompt-engineering)
- [사전 준비 사항](../../../02-prompt-engineering)
- [프롬프트 엔지니어링 이해하기](../../../02-prompt-engineering)
- [프롬프트 엔지니어링 기본 원칙](../../../02-prompt-engineering)
  - [제로샷 프롬팅](../../../02-prompt-engineering)
  - [퓨샷 프롬팅](../../../02-prompt-engineering)
  - [사고의 연쇄](../../../02-prompt-engineering)
  - [역할 기반 프롬팅](../../../02-prompt-engineering)
  - [프롬프트 템플릿](../../../02-prompt-engineering)
- [고급 패턴](../../../02-prompt-engineering)
- [애플리케이션 실행하기](../../../02-prompt-engineering)
- [애플리케이션 스크린샷](../../../02-prompt-engineering)
- [패턴 탐구하기](../../../02-prompt-engineering)
  - [낮은 열의 vs 높은 열의](../../../02-prompt-engineering)
  - [작업 실행 (툴 프리앰블)](../../../02-prompt-engineering)
  - [자기 반영 코드](../../../02-prompt-engineering)
  - [구조화된 분석](../../../02-prompt-engineering)
  - [다중 회차 채팅](../../../02-prompt-engineering)
  - [단계별 추론](../../../02-prompt-engineering)
  - [제한된 출력](../../../02-prompt-engineering)
- [실제로 배우는 것](../../../02-prompt-engineering)
- [다음 단계](../../../02-prompt-engineering)

## 비디오 안내

이 모듈 시작 방법을 설명하는 라이브 세션을 시청하세요:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## 학습 내용

다음 다이어그램은 이 모듈에서 개발할 주요 주제와 기술 개요를 제공합니다 — 프롬프트 세련화 기법부터 따라야 하는 단계별 워크플로우까지.

<img src="../../../translated_images/ko/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

이전 모듈에서는 GitHub 모델과 기본 LangChain4j 상호작용을 탐구했고, 메모리가 Azure OpenAI와 함께 어떻게 대화형 AI를 구현하는지 살펴보았습니다. 이제는 질문하는 방식 — 즉, 프롬프트 자체 — 에 집중합니다. Azure OpenAI의 GPT-5.2를 사용하며, 프롬프트 구성 방법이 응답 품질에 얼마나 큰 영향을 미치는지 알아봅니다. 기본 프롬프트 기술을 리뷰한 다음 GPT-5.2의 기능을 최대한 활용하는 8가지 고급 패턴으로 이동합니다.

GPT-5.2를 사용하는 이유는 추론 제어 기능이 도입되어 모델이 답변 전 얼마나 많이 생각할지 지시할 수 있기 때문입니다. 이는 다양한 프롬프트 전략을 분명하게 하며, 언제 어떤 방식을 사용할지 이해하는 데 도움이 됩니다. 또한 GitHub 모델보다 GPT-5.2는 Azure에서 더 적은 호출 제한을 가지고 있어 이점이 있습니다.

## 사전 준비 사항

- 모듈 01 완성 (Azure OpenAI 리소스 배포 완료)
- 루트 디렉터리에 Azure 자격 증명이 포함된 `.env` 파일 (모듈 01에서 `azd up` 명령어 실행으로 생성됨)

> **참고:** 모듈 01을 완료하지 않았다면, 먼저 해당 모듈의 배포 지침을 따라주세요.

## 프롬프트 엔지니어링 이해하기

본질적으로 프롬프트 엔지니어링은 모호한 명령과 정확한 명령의 차이입니다. 아래 비교가 그것을 보여줍니다.

<img src="../../../translated_images/ko/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

프롬프트 엔지니어링은 원하는 결과를 꾸준히 얻을 수 있도록 입력 텍스트를 설계하는 것입니다. 단순히 질문하는 것이 아니라, 모델이 정확히 무엇을 원하는지 그리고 어떻게 전달할 것인지 이해하도록 요청을 구조화하는 것입니다.

예를 들어 동료에게 지시하는 것과 같습니다. "버그를 고쳐라"는 모호하지만, "UserService.java 45번째 줄에서 널 포인터 예외를 널 체크를 추가하여 수정하라"는 구체적입니다. 언어 모델도 마찬가지이며, 구체성 및 구조가 중요합니다.

아래 다이어그램은 LangChain4j가 어떻게 이 과정에서 역할을 하는지 보여줍니다 — SystemMessage와 UserMessage 빌딩 블록을 통해 프롬프트 패턴을 모델에 연결합니다.

<img src="../../../translated_images/ko/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j는 인프라스트럭처를 제공합니다 — 모델 연결, 메모리, 메시지 타입 — 반면 프롬프트 패턴은 그 인프라를 통해 전달하는 정교하게 구조화된 텍스트일 뿐입니다. 핵심 빌딩 블록은 `SystemMessage` (AI의 동작과 역할 설정)와 `UserMessage` (실제 요청 전달)입니다.

## 프롬프트 엔지니어링 기본 원칙

아래 보여지는 다섯 가지 핵심 기법은 효과적인 프롬프트 엔지니어링의 기본입니다. 각각은 언어 모델과 소통하는 다른 면을 다룹니다.

<img src="../../../translated_images/ko/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

이 모듈의 고급 패턴에 들어가기 전에, 다섯 가지 기본 프롬프트 기법을 복습해 봅시다. 이는 모든 프롬프트 엔지니어가 알아야 하는 빌딩 블록입니다. 만약 이미 [빠른 시작 모듈](../00-quick-start/README.md#2-prompt-patterns)을 완료했다면, 여기에 그 이론적 프레임워크가 있습니다.

### 제로샷 프롬팅

가장 간단한 방식: 예시 없이 모델에 직접 명령을 내립니다. 모델은 자신의 학습만을 바탕으로 작업을 이해하고 수행합니다. 이는 예상 행동이 명확한 간단한 요청에 적합합니다.

<img src="../../../translated_images/ko/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*예시 없이 직접 명령 — 모델이 명령만으로 작업을 추론*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 응답: "긍정적"
```
  
**사용 시기:** 단순 분류, 직접 질문, 번역 또는 추가 안내 없이 모델이 처리 가능한 작업.

### 퓨샷 프롬팅

모델이 따라야 할 패턴을 보여주는 예시를 제공합니다. 모델은 제공된 입력-출력 형식을 학습하고 이를 새로운 입력에 적용합니다. 이는 원하는 형식이나 행동이 명확하지 않은 작업에서 일관성을 대폭 향상시킵니다.

<img src="../../../translated_images/ko/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*예시를 통해 학습 — 모델이 패턴을 파악해 새 입력에 적용*

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
  
**사용 시기:** 맞춤 분류, 일관된 포매팅, 도메인 특화 작업, 제로샷 결과가 불안정할 때.

### 사고의 연쇄

모델에게 단계별 추론 과정을 보여 달라고 요청합니다. 바로 답을 제시하기보다 문제를 분해해 각 부분을 명확히 해결해 나갑니다. 수학, 논리, 다단계 추론 작업에서 정확도를 높입니다.

<img src="../../../translated_images/ko/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*단계별 추론 — 복잡한 문제를 명백한 논리적 단계로 분해*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 모델은 15 - 8 = 7을 보여주고, 그 다음에 7 + 12 = 19개의 사과를 보여줍니다
```
  
**사용 시기:** 수학 문제, 논리 퍼즐, 디버깅 또는 추론 과정이 정확도와 신뢰도를 높이는 작업.

### 역할 기반 프롬팅

질문 전에 AI에게 페르소나나 역할을 부여합니다. 이는 응답의 톤, 깊이, 초점을 형성하는 맥락을 제공합니다. “소프트웨어 아키텍트”는 “주니어 개발자”나 “보안 감사관”과는 다른 조언을 줍니다.

<img src="../../../translated_images/ko/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*맥락과 페르소나 설정 — 같은 질문도 역할에 따라 다른 답변*

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
  
**사용 시기:** 코드 리뷰, 튜터링, 도메인 특화 분석 혹은 특정 전문성 수준이나 관점에 맞춘 응답이 필요할 때.

### 프롬프트 템플릿

가변 자리표시자를 가진 재사용 가능한 프롬프트를 만듭니다. 매번 새 프롬프트를 작성하는 대신 템플릿을 한 번 정의하고 다양한 값을 채웁니다. LangChain4j의 `PromptTemplate` 클래스는 `{{variable}}` 구문으로 쉽게 지원합니다.

<img src="../../../translated_images/ko/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*변수 자리표시자가 포함된 재사용 가능한 프롬프트 — 하나의 템플릿, 여러 용도*

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
  
**사용 시기:** 다양한 입력을 가진 반복 쿼리, 배치 처리, 재사용 가능한 AI 워크플로우 구축 또는 프롬프트 구조는 고정이고 데이터만 변경되는 시나리오.

---

이 다섯 가지 기본기는 대부분의 프롬팅 작업에 탄탄한 도구세트를 제공합니다. 이 모듈의 나머지 부분은 여기에 **8가지 고급 패턴**을 더해 GPT-5.2의 추론 제어, 자기 평가 및 구조화된 출력 기능을 활용합니다.

## 고급 패턴

기본기를 다룬 후, 이 모듈만의 고유한 8가지 고급 패턴으로 넘어갑니다. 모든 문제에 동일한 접근법이 필요한 것은 아닙니다. 어떤 질문은 빠른 답변이 필요하고, 어떤 것은 깊은 사고가 필요합니다. 일부는 추론 과정을 시각적으로 보여야 하며, 다른 것은 결과만 필요합니다. 아래 각 패턴은 각기 다른 상황에 최적화되어 있으며, GPT-5.2의 추론 제어 기능이 차이를 더욱 뚜렷하게 만듭니다.

<img src="../../../translated_images/ko/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*8가지 프롬프트 엔지니어링 패턴 및 사용 사례 개요*

GPT-5.2가 이 패턴들에 또 다른 축을 더합니다: *추론 제어*. 슬라이더를 통해 모델이 얼마나 많이 사고할지 조절할 수 있습니다 — 빠르고 직접적인 답변에서 깊고 철저한 분석까지.

<img src="../../../translated_images/ko/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2의 추론 제어로 모델이 사고하는 정도를 지정할 수 있음 — 빠른 직접 답변부터 깊은 탐색까지*

**낮은 열의 (빠르고 집중적)** - 빠르고 직접적인 답변을 원하는 단순 질문에 적합합니다. 모델은 최소한의 추론만 수행 — 최대 2단계. 계산, 조회 또는 명확한 질문에 사용하세요.

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
  
> 💡 **GitHub Copilot으로 탐구하기:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 파일을 열고 다음 질문을 해보세요:  
> - "낮은 열의와 높은 열의 프롬팅 패턴의 차이는 무엇인가요?"  
> - "프롬프트 내 XML 태그가 AI 응답 구조에 어떻게 도움을 주나요?"  
> - "자기 반영 패턴과 직접 명령 패턴을 언제 사용해야 하나요?"

**높은 열의 (깊고 철저함)** - 포괄적인 분석이 필요한 복잡한 문제에 적합합니다. 모델이 철저히 탐구하고 상세한 추론을 보여줍니다. 시스템 설계, 아키텍처 결정, 복잡한 연구에 사용할 수 있습니다.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**작업 실행 (단계별 진행)** - 다단계 워크플로우를 위해. 모델이 처음에 계획을 제시하고, 작업하는 동안 각 단계를 설명하며, 마지막에 요약을 제공합니다. 마이그레이션, 구현 등 다단계 작업에 적합합니다.

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
  
사고의 연쇄 프롬팅은 모델이 추론 과정을 명시적으로 보여 주도록 하여 복잡한 작업에서 정확도를 향상시킵니다. 단계별 분해는 인간과 AI 모두가 논리를 이해하는 데 도움을 줍니다.

> **🤖 GitHub Copilot Chat에서 시도해보세요:** 다음 질문을 해보세요:  
> - "장시간 소요되는 작업에 작업 실행 패턴을 어떻게 적용할 수 있나요?"  
> - "프로덕션 애플리케이션에서 툴 프리앰블 구조화의 모범 사례는 무엇인가요?"  
> - "중간 진행 상황을 UI에 캡처하고 표시하는 방법은?"

아래 다이어그램은 이 계획 → 실행 → 요약 워크플로우를 나타냅니다.

<img src="../../../translated_images/ko/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*다단계 작업을 위한 계획 → 실행 → 요약 워크플로우*

**자기 반영 코드** - 프로덕션 품질의 코드를 생성하기 위해. 모델은 적절한 에러 핸들링과 함께 프로덕션 표준을 따르는 코드를 생성합니다. 새로운 기능이나 서비스를 구축할 때 사용하세요.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```
  
아래 다이어그램은 반복 개선 루프 — 생성, 평가, 약점 파악, 개선 — 이 프로덕션 표준을 만족할 때까지 반복하는 과정을 보여줍니다.

<img src="../../../translated_images/ko/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*반복 개선 루프 — 생성, 평가, 문제 파악, 개선, 반복*

**구조화된 분석** - 일관된 평가를 위해. 모델은 고정된 프레임워크(정확성, 관행, 성능, 보안, 유지보수성)를 사용해 코드를 검토합니다. 코드 리뷰나 품질 평가에 적합합니다.

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
  
> **🤖 GitHub Copilot Chat에서 시도해보세요:** 구조화된 분석에 대해 질문해보세요:  
> - "다양한 코드 리뷰 유형별로 분석 프레임워크를 어떻게 맞춤화할 수 있나요?"  
> - "구조화된 출력을 프로그램적으로 파싱하고 활용하는 최선의 방법은 무엇인가요?"  
> - "서로 다른 리뷰 세션에서 심각도 수준을 일관되게 유지하려면 어떻게 해야 하나요?"

아래 다이어그램은 이 구조화된 프레임워크가 심각도 수준과 함께 코드를 일관되게 검토하도록 분류하는 방법을 보여줍니다.

<img src="../../../translated_images/ko/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*심각도 수준을 포함한 일관된 코드 리뷰를 위한 프레임워크*

**다중 회차 채팅** - 맥락이 필요한 대화에 적합합니다. 모델은 이전 메시지를 기억하고 이를 기반으로 빌드업합니다. 대화형 도움말 세션이나 복잡한 Q&A에 사용합니다.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
아래 다이어그램은 각 회차마다 대화 맥락이 어떻게 누적되며, 그 누적이 모델의 토큰 한도와 어떻게 관련되는지 시각화합니다.

<img src="../../../translated_images/ko/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*여러 회차에 걸쳐 대화 맥락이 누적되고 토큰 한도에 도달하는 과정*
**단계별 추론** - 명확한 논리가 필요한 문제에 사용합니다. 모델이 각 단계를 명시적으로 설명합니다. 수학 문제, 논리 퍼즐 또는 사고 과정을 이해해야 할 때 사용하세요.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

아래 다이어그램은 모델이 문제를 명확한 번호가 붙은 논리적 단계로 어떻게 분해하는지 보여줍니다.

<img src="../../../translated_images/ko/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*문제를 명확한 논리 단계로 분해하기*

**제한된 출력** - 특정 형식 요구사항이 있는 응답에 사용합니다. 모델이 형식과 길이 규칙을 엄격히 준수합니다. 요약하거나 정확한 출력 구조가 필요할 때 사용하세요.

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

아래 다이어그램은 제약조건이 모델이 특정 형식과 길이 요구사항을 엄격히 준수하도록 안내하는 방식을 보여줍니다.

<img src="../../../translated_images/ko/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*특정 형식, 길이, 구조 요구사항을 강제하기*

## 애플리케이션 실행

**배포 확인:**

루트 디렉터리에 Azure 자격 증명(모듈 01에서 생성)과 함께 `.env` 파일이 존재하는지 확인하세요. 모듈 디렉터리(`02-prompt-engineering/`)에서 다음을 실행합니다:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT를 표시해야 합니다
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT를 보여야 합니다
```

**애플리케이션 시작:**

> **참고:** 이미 루트 디렉터리에서 `./start-all.sh` (모듈 01 설명 참고)를 사용해 모든 애플리케이션을 시작했다면 이 모듈은 포트 8083에서 이미 실행 중입니다. 아래 시작 명령은 건너뛰고 http://localhost:8083 로 바로 접속하세요.

**옵션 1: Spring Boot 대시보드 사용하기 (VS Code 사용자 권장)**

dev 컨테이너에는 Spring Boot 대시보드 확장이 포함되어 있어 모든 Spring Boot 애플리케이션을 시각적으로 관리할 수 있습니다. VS Code 왼쪽 Activity Bar에서 Spring Boot 아이콘을 찾아보세요.

Spring Boot 대시보드에서:
- 워크스페이스 내 모든 Spring Boot 애플리케이션을 확인
- 클릭 한 번으로 애플리케이션 시작/중지
- 실시간으로 애플리케이션 로그 보기
- 애플리케이션 상태 모니터링

"prompt-engineering" 옆의 재생 버튼을 클릭하면 이 모듈을 시작하거나 모든 모듈을 한 번에 시작할 수 있습니다.

<img src="../../../translated_images/ko/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code 내 Spring Boot 대시보드 — 하나의 장소에서 모든 모듈 시작, 중지, 모니터링*

**옵션 2: 셸 스크립트 사용하기**

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

두 스크립트는 자동으로 루트 `.env` 파일에서 환경 변수를 불러오고, JAR 파일이 없으면 빌드합니다.

> **참고:** 시작 전에 모든 모듈을 수동으로 빌드하고 싶다면:
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

아래는 프롬프트 엔지니어링 모듈의 메인 인터페이스로, 8가지 패턴을 나란히 실험할 수 있습니다.

<img src="../../../translated_images/ko/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*특징과 사용 사례가 표시된 8가지 프롬프트 엔지니어링 패턴 전용 메인 대시보드*

## 패턴 탐색

웹 인터페이스에서 다양한 프롬프트 전략을 실험할 수 있습니다. 각 패턴은 다른 문제를 해결합니다 — 언제 어떤 접근법이 유리한지 직접 확인해 보세요.

> **참고: 스트리밍 vs 비스트리밍** — 모든 패턴 페이지는 **🔴 실시간 스트림 응답**과 **비스트리밍** 두 버튼을 제공합니다. 스트리밍은 서버 전송 이벤트(SSE)를 사용해 토큰이 생성되는 즉시 보여 줍니다. 비스트리밍은 전체 응답이 완료될 때까지 기다립니다. 깊은 추론이 필요한 프롬프트(예: High Eagerness, Self-Reflecting Code)는 비스트리밍 호출 시 몇 분씩 걸리며 아무 피드백 없을 수 있습니다. **복잡한 프롬프트를 실험할 때는 스트리밍을 사용해 모델 작업 과정을 실시간으로 확인하세요.** 요청이 타임아웃된 것처럼 느끼는 일을 방지할 수 있습니다.
>
> **참고: 브라우저 요구사항** — 스트리밍 기능은 Fetch Streams API (`response.body.getReader()`)를 사용하며, 완전한 브라우저(Chrome, Edge, Firefox, Safari)가 필요합니다. VS Code 내장 Simple Browser는 ReadableStream API를 지원하지 않아 스트리밍이 작동하지 않습니다. Simple Browser에서 비스트리밍 버튼은 정상 작동하지만 스트리밍 버튼만 제한됩니다. 전체 기능을 위해 외부 브라우저에서 `http://localhost:8083`에 접속하세요.

### 낮은 열의도 vs 높은 열의도

"200의 15%는 얼마인가?" 같은 단순 질문을 낮은 열의도( Low Eagerness)로 물어보세요. 즉각적이고 직관적인 답을 얻을 수 있습니다. "트래픽이 많은 API를 위한 캐싱 전략 설계" 같은 복잡한 질문은 높은 열의도(High Eagerness)로 물어보세요. **🔴 실시간 스트림 응답**을 클릭해 모델의 자세한 추론 과정을 한 토큰씩 확인하세요. 같은 모델, 같은 질문 구조지만 프롬프트가 사고의 깊이를 조절합니다.

### 작업 실행 (도구 프리앰블)

여러 단계의 워크플로에는 미리 계획하고 진행 상황을 서술하는 것이 유리합니다. 모델이 수행할 작업을 개괄하고, 각 단계를 설명하며, 결과를 요약합니다.

### 자기반영 코드

"이메일 유효성 검사 서비스 생성"을 시도해 보세요. 단순히 코드만 생성하지 않고, 모델이 품질 기준에 따라 평가하고 약점 파악 후 개선하며, 생산 기준에 맞을 때까지 반복합니다.

### 구조화된 분석

코드 리뷰에는 일관된 평가 프레임워크가 필요합니다. 모델은 고정된 범주별(정확성, 관행, 성능, 보안) 심각도 레벨을 사용해 코드를 분석합니다.

### 다중 턴 대화

"Spring Boot가 뭐야?"라고 물은 후 즉시 "예시 보여줘"라고 이어 질문해 보세요. 모델은 첫 질문을 기억하고 구체적인 Spring Boot 예시를 제공합니다. 기억 기능이 없으면 두 번째 질문은 너무 모호합니다.

### 단계별 추론

수학 문제를 골라 단계별 추론과 낮은 열의도 방식 모두로 시도해 보세요. 낮은 열의도는 빠르지만 정답만 제공합니다. 단계별 추론은 모든 계산과 결정을 보여줍니다.

### 제한된 출력

특정 형식이나 단어 수가 낯에 필요할 때 이 패턴은 엄격히 준수하게 합니다. 정확히 100단어 요약을 글머리 기호 형식으로 생성해 보세요.

## 진짜 배우는 것

**추론 노력은 모든 것을 바꾼다**

GPT-5.2는 프롬프트를 통해 연산 노력 정도를 조절할 수 있습니다. 적은 노력은 빠르고 최소한의 탐색을 의미합니다. 많은 노력은 모델이 깊게 사고하도록 합니다. 작업 복잡도에 맞게 노력을 조절하는 법을 배우는 것입니다 — 단순 질문에 시간 낭비하지 말고, 복잡한 결정도 서두르지 마세요.

**구조가 행동을 안내한다**

프롬프트 내 XML 태그를 주목하세요? 단순한 장식이 아닙니다. 모델은 자유 텍스트보다 구조화된 지시문을 더 신뢰성 있게 따릅니다. 다단계 과정이나 복잡한 논리가 필요할 때 구조는 모델이 현재 위치와 다음 단계를 추적하는 데 도움을 줍니다. 아래 다이어그램은 `<system>`, `<instructions>`, `<context>`, `<user-input>`, `<constraints>` 같은 태그가 명확히 구분된 섹션으로 지시를 조직하는 방식을 보여줍니다.

<img src="../../../translated_images/ko/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*명확한 구획과 XML 스타일 구성으로 이루어진 잘 구조화된 프롬프트 해부*

**자기평가로 품질 향상**

자기반영 패턴은 품질 기준을 명시적으로 만듭니다. 모델이 “잘 하길 바라기”보다 “올바름”이 무엇인지(정확한 논리, 오류 처리, 성능, 보안)를 직접 알려줍니다. 모델은 스스로 출력물을 평가하고 개선할 수 있습니다. 코드 생성을 복권이 아닌 과정으로 바꾸는 것입니다.

**컨텍스트는 유한하다**

다중 턴 대화는 각 요청에 메시지 이력을 포함해 작동합니다. 하지만 최대 토큰 수 한도가 있습니다. 대화가 길어질수록 관련 컨텍스트를 유지하면서 한도를 넘지 않는 전략이 필요합니다. 이 모듈에서 메모리 작동 방식을 배우고, 추후에 언제 요약하고 잊고 불러올지 배울 것입니다.

## 다음 단계

**다음 모듈:** [03-rag - RAG (검색 확장 생성)](../03-rag/README.md)

---

**네비게이션:** [← 이전: 모듈 01 - 소개](../01-introduction/README.md) | [메인으로 돌아가기](../README.md) | [다음: 모듈 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 이용해 번역되었습니다. 정확성을 위해 노력하고 있으나, 자동 번역은 오류나 부정확한 부분이 있을 수 있음을 유의하시기 바랍니다. 원문은 해당 언어로 된 원본 문서를 권위 있는 출처로 간주해야 합니다. 중요한 정보의 경우 전문적인 인간 번역을 권장합니다. 이 번역 사용으로 인한 오해나 잘못된 해석에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->