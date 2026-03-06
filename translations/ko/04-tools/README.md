# Module 04: 도구를 갖춘 AI 에이전트

## 목차

- [학습 내용](../../../04-tools)
- [필수 조건](../../../04-tools)
- [도구를 갖춘 AI 에이전트 이해하기](../../../04-tools)
- [도구 호출 방식](../../../04-tools)
  - [도구 정의](../../../04-tools)
  - [결정 과정](../../../04-tools)
  - [실행](../../../04-tools)
  - [응답 생성](../../../04-tools)
  - [아키텍처: Spring Boot 자동 연결](../../../04-tools)
- [도구 체이닝](../../../04-tools)
- [애플리케이션 실행](../../../04-tools)
- [애플리케이션 사용법](../../../04-tools)
  - [간단한 도구 사용 시도](../../../04-tools)
  - [도구 체이닝 테스트](../../../04-tools)
  - [대화 흐름 보기](../../../04-tools)
  - [다양한 요청 실험](../../../04-tools)
- [핵심 개념](../../../04-tools)
  - [ReAct 패턴 (추론과 행동)](../../../04-tools)
  - [도구 설명의 중요성](../../../04-tools)
  - [세션 관리](../../../04-tools)
  - [오류 처리](../../../04-tools)
- [사용 가능한 도구](../../../04-tools)
- [도구 기반 에이전트 사용 시기](../../../04-tools)
- [도구와 RAG 비교](../../../04-tools)
- [다음 단계](../../../04-tools)

## 학습 내용

지금까지 AI와 대화하는 방법, 효과적으로 프롬프트를 구성하는 방법, 문서 기반으로 답변을 근거하는 방법을 배웠습니다. 하지만 기본적인 한계가 있습니다: 언어 모델은 텍스트만 생성할 수 있습니다. 날씨를 확인하거나, 계산을 수행하거나, 데이터베이스를 조회하거나, 외부 시스템과 상호작용할 수 없습니다.

도구가 이 한계를 바꿉니다. 모델에 호출할 수 있는 함수 접근 권한을 주면, 단순한 텍스트 생성기에서 실제 행동을 취할 수 있는 에이전트로 변모합니다. 모델은 도구가 필요할 때, 어떤 도구를 사용할지, 어떤 매개변수를 전달할지 결정합니다. 코드는 함수를 실행하고 결과를 반환합니다. 모델은 그 결과를 응답에 반영합니다.

## 필수 조건

- [Module 01 - Introduction](../01-introduction/README.md) 완료 (Azure OpenAI 리소스 배포 완료)
- 이전 모듈 완료 권장 (이 모듈은 [Module 03의 RAG 개념](../03-rag/README.md)을 도구와 RAG 비교에 참조)
- 루트 디렉터리에 `.env` 파일 존재 (Module 01에서 `azd up` 명령어로 생성된 Azure 자격 증명 포함)

> **참고:** Module 01을 완료하지 않았다면, 먼저 해당 모듈의 배포 지침을 따르세요.

## 도구를 갖춘 AI 에이전트 이해하기

> **📝 참고:** 이 모듈에서 "에이전트"라는 용어는 도구 호출 기능이 확장된 AI 어시스턴트를 가리킵니다. 이는 [Module 05: MCP](../05-mcp/README.md)에서 다룰 **Agentic AI** 패턴(계획, 기억, 다단계 추론이 포함된 자율 에이전트)과는 다릅니다.

도구 없이는 언어 모델이 훈련 데이터 기반으로 텍스트만 생성합니다. 현재 날씨를 물으면 추측만 해야 합니다. 도구를 주면 날씨 API를 호출하거나 계산을 수행하거나 데이터베이스를 조회할 수 있고, 그 실시간 데이터를 응답에 반영합니다.

<img src="../../../translated_images/ko/what-are-tools.724e468fc4de64da.webp" alt="도구 없음 vs 도구 있음" width="800"/>

*도구가 없으면 모델은 추측만 가능하지만 도구가 있으면 API 호출, 계산 실행, 실시간 데이터 반환이 가능합니다.*

도구를 갖춘 AI 에이전트는 **추론과 행동(ReAct)** 패턴을 따릅니다. 모델이 단순히 응답하지 않고, 필요한 것을 생각하고, 도구를 호출해 행동하며, 결과를 관찰하고 다시 행동할지 최종 답변을 낼지 결정합니다:

1. **추론** — 에이전트가 사용자의 질문을 분석해 필요한 정보를 결정
2. **행동** — 에이전트가 적합한 도구를 선택하고 올바른 매개변수를 생성해 호출
3. **관찰** — 에이전트가 도구 출력을 받고 결과를 평가
4. **반복 또는 응답** — 추가 데이터가 필요하면 반복, 아니면 자연어 답변 생성

<img src="../../../translated_images/ko/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct 패턴" width="800"/>

*ReAct 사이클 — 에이전트가 무엇을 해야 할지 추론하고 도구를 호출해 행동하며, 결과를 관찰하고 최종 답변을 낼 때까지 반복.*

이 과정은 자동으로 진행됩니다. 사용자가 도구와 설명을 정의하면, 모델은 언제 어떻게 도구를 쓸지 결정합니다.

## 도구 호출 방식

### 도구 정의

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

설명과 매개변수 명세가 명확한 함수를 정의합니다. 모델은 시스템 프롬프트 안에서 이 설명을 보고 각 도구의 역할을 이해합니다.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // 당신의 날씨 조회 로직
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// 어시스턴트는 Spring Boot에 의해 자동으로 연결됩니다:
// - ChatModel 빈
// - @Component 클래스의 모든 @Tool 메서드
// - 세션 관리를 위한 ChatMemoryProvider
```

아래 다이어그램은 모든 애노테이션을 분해하고 각 부분이 AI가 도구 호출 시기와 매개변수를 이해하는 데 어떻게 도움이 되는지 보여줍니다:

<img src="../../../translated_images/ko/tool-definitions-anatomy.f6468546037cf28b.webp" alt="도구 정의 구성" width="800"/>

*도구 정의 구조 — @Tool은 AI에 도구 사용 시기를 알려주고, @P는 각 매개변수를 설명하며, @AiService는 시작 시 모두 연결합니다.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat으로 시도해 보세요:** [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java)를 열고 물어보세요:
> - "모의 데이터 대신 OpenWeatherMap 같은 실제 날씨 API를 통합하려면 어떻게 해야 하나요?"
> - "AI가 올바르게 도구를 사용하도록 돕는 좋은 도구 설명은 무엇인가요?"
> - "도구 구현 시 API 오류와 호출 제한은 어떻게 처리해야 하나요?"

### 결정 과정

사용자가 "시애틀 날씨가 어때?"라고 물으면, 모델이 무작위로 도구를 선택하지 않습니다. 사용자 의도와 모든 도구 설명을 비교해서 관련성을 평가하고 가장 적합한 도구를 선택합니다. 그 다음 올바른 매개변수를 생성한 구조화된 함수 호출을 만듭니다 — 이 경우 `location` 값을 `"Seattle"`로 설정합니다.

사용자 요청과 일치하는 도구가 없다면 모델은 자체 지식으로 답합니다. 복수 도구가 일치하면 가장 구체적인 도구를 선택합니다.

<img src="../../../translated_images/ko/decision-making.409cd562e5cecc49.webp" alt="AI가 어떤 도구를 사용할지 결정하는 방법" width="800"/>

*모델은 사용자의 의도에 따라 모든 사용 가능한 도구를 평가하고 최적 도구를 선택합니다 — 그래서 명확하고 구체적인 도구 설명을 쓰는 게 중요합니다.*

### 실행

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot는 선언적인 `@AiService` 인터페이스와 모든 등록된 도구를 자동 연결하며, LangChain4j는 도구 호출을 자동으로 실행합니다. 내부적으로 도구 호출은 사용자의 자연어 질문부터 자연어 답변까지 여섯 단계로 흐릅니다:

<img src="../../../translated_images/ko/tool-calling-flow.8601941b0ca041e6.webp" alt="도구 호출 흐름" width="800"/>

*엔드 투 엔드 흐름 — 사용자가 질문하면 모델이 도구를 선택하고, LangChain4j가 실행 후 결과를 자연스러운 답변에 반영합니다.*

Module 00의 [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)를 실행해 보았다면 이 패턴을 이미 봤습니다 — `Calculator` 도구가 같은 방식으로 호출됩니다. 아래 시퀀스 다이어그램은 그 데모에서 내부적으로 어떤 일이 벌어졌는지 보여줍니다:

<img src="../../../translated_images/ko/tool-calling-sequence.94802f406ca26278.webp" alt="도구 호출 시퀀스 다이어그램" width="800"/>

*퀵 스타트 데모의 도구 호출 루프 — `AiServices`가 사용자 메시지와 도구 스키마를 LLM에 보내면 LLM이 `add(42, 58)` 같은 함수 호출로 응답, LangChain4j가 로컬에서 `Calculator` 메서드를 실행하고 결과를 최종 답변에 반영합니다.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat으로 시도해 보세요:** [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)를 열고 물어보세요:
> - "ReAct 패턴은 어떻게 작동하며 왜 AI 에이전트에 효과적인가요?"
> - "에이전트는 어떤 도구를 사용하고 어떤 순서로 사용할지 어떻게 결정하나요?"
> - "도구 실행이 실패하면 어떻게 처리해야 하나요? 견고한 오류 처리 방법은?"

### 응답 생성

모델이 날씨 데이터를 받아 사용자에게 자연어 형식으로 답변을 작성합니다.

### 아키텍처: Spring Boot 자동 연결

이 모듈은 LangChain4j의 Spring Boot 통합과 선언적인 `@AiService` 인터페이스를 사용합니다. 시작 시 Spring Boot가 `@Tool` 메서드를 포함한 모든 `@Component`, `ChatModel` 빈, `ChatMemoryProvider`를 발견한 후, 모든 것을 하나의 `Assistant` 인터페이스에 보일러플레이트 없이 자동 연결합니다.

<img src="../../../translated_images/ko/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot 자동 연결 아키텍처" width="800"/>

*@AiService 인터페이스가 ChatModel, 도구 컴포넌트, 메모리 공급자를 묶고 — Spring Boot가 자동으로 연결을 담당.*

아래는 HTTP 요청부터 컨트롤러, 서비스, 자동 연결된 프록시, 도구 실행, 그리고 다시 돌아오는 전체 요청 라이프사이클 시퀀스 다이어그램입니다:

<img src="../../../translated_images/ko/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Spring Boot 도구 호출 시퀀스" width="800"/>

*완전한 Spring Boot 요청 라이프사이클 — HTTP 요청이 컨트롤러와 서비스를 거쳐 자동 연결된 Assistant 프록시로 흘러가고, 이는 LLM과 도구 호출을 자동으로 조율.*

이 방법의 주요 장점:

- **Spring Boot 자동 연결** — ChatModel과 도구가 자동 주입됨
- **@MemoryId 패턴** — 세션 기반 메모리 자동 관리
- **단일 인스턴스** — Assistant를 한 번만 생성해 성능 향상
- **타입 안전 실행** — 자바 메서드를 직접 호출하며 타입 변환 수행
- **다중 대화(turn) 오케스트레이션** — 도구 체이닝 자동 처리
- **보일러플레이트 제로** — 수동 `AiServices.builder()` 호출 또는 메모리 HashMap 불필요

수동 `AiServices.builder()` 같은 대안은 더 많은 코드가 필요하고 Spring Boot 통합 이점을 누리지 못합니다.

## 도구 체이닝

**도구 체이닝** — 도구 기반 에이전트의 진정한 힘은 단일 질문에 여러 도구가 필요한 경우에 드러납니다. 예를 들어 "시애틀의 화씨 기준 날씨가 어때?"라고 묻으면 에이전트가 두 도구를 연쇄적으로 호출합니다: 먼저 `getCurrentWeather`를 호출해 섭씨 온도를 얻고, 그 결과를 `celsiusToFahrenheit`에 넘겨 화씨로 변환합니다 — 한 번의 대화 턴에서 모두 이뤄집니다.

<img src="../../../translated_images/ko/tool-chaining-example.538203e73d09dd82.webp" alt="도구 체이닝 예" width="800"/>

*도구 체이닝 작동 예 — 에이전트가 먼저 getCurrentWeather를 호출하고, 섭씨 결과를 celsiusToFahrenheit로 넘겨 결합된 답변을 제공합니다.*

**우아한 실패 처리** — 모의 데이터에 없는 도시의 날씨를 물으면 도구가 오류 메시지를 반환하고, AI가 다운되지 않고 도와줄 수 없음을 설명합니다. 도구가 안전하게 실패합니다. 아래 다이어그램은 두 접근법을 대비합니다 — 적절한 오류 처리 시 에이전트가 예외를 잡아 친절하게 응답하고, 그렇지 않으면 애플리케이션이 완전히 다운됩니다:

<img src="../../../translated_images/ko/error-handling-flow.9a330ffc8ee0475c.webp" alt="오류 처리 흐름" width="800"/>

*도구 실패 시 에이전트가 오류를 잡아 친절한 설명으로 응답하며 다운을 방지.*

이는 단일 대화 턴에서 발생합니다. 에이전트가 다수 도구 호출을 자율적으로 조율합니다.

## 애플리케이션 실행

**배포 확인:**

Azure 자격증명이 포함된 `.env` 파일이 루트에 존재하는지 확인하세요 (Module 01 실행 시 생성됨). 이 모듈 디렉터리(`04-tools/`)에서 실행합니다:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT를 표시해야 합니다
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT를 표시해야 합니다
```

**애플리케이션 시작:**

> **참고:** 루트 디렉터리에서 `./start-all.sh`를 실행해 이미 모든 애플리케이션을 시작했다면 (Module 01 참조), 이 모듈은 포트 8084에서 이미 실행 중입니다. 아래 시작 명령은 건너뛰고 바로 http://localhost:8084 로 접속할 수 있습니다.

**옵션 1: Spring Boot 대시보드 이용 (VS Code 사용자에게 권장)**

개발 컨테이너에는 Spring Boot 대시보드 확장 기능이 포함되어 있어 모든 Spring Boot 애플리케이션을 시각적으로 관리할 수 있습니다. VS Code 왼쪽 활동 표시줄에서 Spring Boot 아이콘을 찾으세요.

Spring Boot 대시보드에서 할 수 있는 것:
- 워크스페이스 내 모든 Spring Boot 애플리케이션 조회
- 클릭 한 번으로 애플리케이션 시작/중지
- 실시간 로그 보기
- 애플리케이션 상태 모니터링

"tools" 옆의 재생 버튼을 클릭해 이 모듈을 시작하거나, 모든 모듈을 한 번에 시작할 수도 있습니다.

VS Code에서 Spring Boot 대시보드 모습:

<img src="../../../translated_images/ko/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot 대시보드" width="400"/>

*VS Code Spring Boot 대시보드 - 한 곳에서 모든 모듈 시작, 중지, 모니터링*

**옵션 2: 쉘 스크립트 이용**

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

또는 이 모듈만 시작하세요:

**Bash:**
```bash
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

두 스크립트는 루트 `.env` 파일에서 환경 변수를 자동으로 로드하고, JAR 파일이 없으면 빌드합니다.

> **참고:** 시작하기 전에 모든 모듈을 수동으로 빌드하고 싶다면:
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

브라우저에서 http://localhost:8084 를 엽니다.

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

## 애플리케이션 사용하기

이 애플리케이션은 날씨와 온도 변환 도구에 접근할 수 있는 AI 에이전트와 상호작용할 수 있는 웹 인터페이스를 제공합니다. 인터페이스는 다음과 같이 생겼습니다 — 빠른 시작 예제와 요청을 보내는 채팅 패널이 포함되어 있습니다:

<a href="images/tools-homepage.png"><img src="../../../translated_images/ko/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI 에이전트 도구 인터페이스 - 빠른 예제와 도구와 상호작용하는 채팅 인터페이스*

### 간단한 도구 사용해보기

"100도 화씨를 섭씨로 변환해줘" 같은 간단한 요청으로 시작하세요. 에이전트는 온도 변환 도구가 필요함을 인지하고, 올바른 매개변수와 함께 호출하여 결과를 반환합니다. 어떤 도구를 사용할지 또는 어떻게 호출할지 따로 명시하지 않았는데도 아주 자연스럽게 작동하는 점에 주목하세요.

### 도구 연결 테스트하기

이제 좀 더 복잡한 요청을 시도해보세요: "시애틀 날씨를 알려주고 화씨로 변환해줘". 에이전트가 단계별로 처리하는 과정을 관찰하세요. 먼저 날씨(섭씨)를 얻고, 화씨로 변환해야 한다는 것을 인지하여 변환 도구를 호출하며, 두 결과를 한 응답에 결합합니다.

### 대화 흐름 보기

채팅 인터페이스는 대화 기록을 유지하여 다중 턴 상호작용이 가능합니다. 모든 이전 쿼리와 응답을 볼 수 있어 대화를 추적하고 에이전트가 여러 교환을 통해 어떻게 문맥을 구축하는지 이해하기 쉽습니다.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/ko/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*단순 변환, 날씨 조회, 도구 연결을 보여주는 다중 턴 대화*

### 다양한 요청 실험하기

다음과 같은 조합을 시도해보세요:
- 날씨 조회: "도쿄 날씨 어때?"
- 온도 변환: "25°C는 켈빈으로 얼마야?"
- 복합 쿼리: "파리 날씨 확인하고 20°C 이상인지 알려줘"

에이전트가 자연어를 해석해 적절한 도구 호출로 매핑하는 방식을 눈여겨보세요.

## 핵심 개념

### ReAct 패턴 (추론과 실행)

에이전트는 추론(무엇을 할지 결정)과 실행(도구 사용)을 번갈아 수행합니다. 이 패턴은 단순히 지시를 따르는 것을 넘어 자율적 문제 해결을 가능하게 합니다.

### 도구 설명의 중요성

도구 설명의 품질이 에이전트의 도구 사용에 직접적인 영향을 미칩니다. 명확하고 구체적인 설명이 모델이 언제 어떻게 도구를 호출해야 하는지 이해하는 데 도움을 줍니다.

### 세션 관리

`@MemoryId` 애노테이션은 자동 세션 기반 메모리 관리를 가능하게 합니다. 각 세션 ID는 `ChatMemoryProvider` 빈이 관리하는 고유한 `ChatMemory` 인스턴스를 가지므로 여러 사용자가 대화를 섞이지 않고 동시에 에이전트와 상호작용할 수 있습니다. 다음 다이어그램은 여러 사용자가 세션 ID에 따라 분리된 메모리 저장소로 라우팅되는 방식을 보여줍니다:

<img src="../../../translated_images/ko/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*각 세션 ID는 고립된 대화 기록에 매핑되어 사용자가 서로의 메시지를 볼 수 없습니다.*

### 오류 처리

도구는 실패할 수 있습니다 — API 타임아웃, 잘못된 매개변수, 외부 서비스 다운 등. 프로덕션 환경 에이전트는 모델이 문제를 설명하거나 대안을 시도하게 하기 위한 오류 처리 기능이 필요합니다. 도구에서 예외가 발생하면 LangChain4j가 이를 잡아 오류 메시지를 모델에 전달하여 자연스러운 언어로 문제를 설명할 수 있게 합니다.

## 사용 가능한 도구

다음 다이어그램은 구축할 수 있는 도구의 광범위한 생태계를 보여줍니다. 이 모듈은 날씨와 온도 도구를 시연하지만 동일한 `@Tool` 패턴은 데이터베이스 쿼리부터 결제 처리까지 모든 자바 메서드에 적용할 수 있습니다.

<img src="../../../translated_images/ko/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*@Tool 애노테이션이 붙은 모든 자바 메서드를 AI가 사용할 수 있으며, 이 패턴은 데이터베이스, API, 이메일, 파일 작업 등으로 확장됩니다.*

## 도구 기반 에이전트 사용 시기

모든 요청에 도구가 필요한 것은 아닙니다. 결정 기준은 AI가 외부 시스템과 상호작용해야 하는지 아니면 자체 지식만으로 답변 가능한지에 달려 있습니다. 다음 가이드는 언제 도구가 가치를 더하고 언제 불필요한지 요약합니다:

<img src="../../../translated_images/ko/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*간단한 의사 결정 가이드 — 도구는 실시간 데이터, 계산, 작업에 유용; 일반 지식과 창의적 작업에는 필요 없음.*

## 도구 vs RAG

모듈 03과 04는 AI의 기능을 확장하지만 근본적으로 다른 방식을 사용합니다. RAG는 문서 검색을 통해 모델에 **지식** 접근을 부여합니다. 도구는 함수 호출을 통해 모델에 **행동** 능력을 제공합니다. 다음 다이어그램은 두 접근법을 나란히 비교하여 각 워크플로우 작동 방식과 각각의 트레이드오프를 보여줍니다:

<img src="../../../translated_images/ko/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG는 정적인 문서에서 정보를 검색 — 도구는 동적이고 실시간 데이터를 실행하고 가져옴. 많은 프로덕션 시스템이 두 방식을 결합함.*

실제로 많은 프로덕션 시스템은 RAG로 문서 기반 답변의 근거를 마련하고 도구로 실시간 데이터 조회나 작업 수행을 병행합니다.

## 다음 단계

**다음 모듈:** [05-mcp - 모델 컨텍스트 프로토콜 (MCP)](../05-mcp/README.md)

---

**탐색:** [← 이전: 모듈 03 - RAG](../03-rag/README.md) | [메인으로 돌아가기](../README.md) | [다음: 모듈 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 최선을 다했으나, 자동 번역에는 오류나 부정확성이 포함될 수 있음을 유의하시기 바랍니다. 원문 문서는 권위 있는 자료로 간주되어야 합니다. 중요한 정보의 경우 전문 인간 번역을 권장합니다. 본 번역의 사용으로 인한 오해나 잘못된 해석에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->