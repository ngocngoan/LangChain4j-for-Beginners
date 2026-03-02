# Module 04: 도구가 있는 AI 에이전트

## 목차

- [학습 내용](../../../04-tools)
- [필수 조건](../../../04-tools)
- [도구가 있는 AI 에이전트 이해하기](../../../04-tools)
- [도구 호출 작동 방식](../../../04-tools)
  - [도구 정의](../../../04-tools)
  - [결정 내리기](../../../04-tools)
  - [실행](../../../04-tools)
  - [응답 생성](../../../04-tools)
  - [아키텍처: Spring Boot 자동 연결](../../../04-tools)
- [도구 체이닝](../../../04-tools)
- [애플리케이션 실행하기](../../../04-tools)
- [애플리케이션 사용하기](../../../04-tools)
  - [간단한 도구 사용해보기](../../../04-tools)
  - [도구 체이닝 테스트](../../../04-tools)
  - [대화 흐름 보기](../../../04-tools)
  - [다양한 요청 실험하기](../../../04-tools)
- [주요 개념](../../../04-tools)
  - [ReAct 패턴 (추론과 행동)](../../../04-tools)
  - [도구 설명의 중요성](../../../04-tools)
  - [세션 관리](../../../04-tools)
  - [오류 처리](../../../04-tools)
- [사용 가능한 도구](../../../04-tools)
- [도구 기반 에이전트 사용 시기](../../../04-tools)
- [도구와 RAG 비교](../../../04-tools)
- [다음 단계](../../../04-tools)

## 학습 내용

지금까지 AI와 대화하는 방법, 효과적으로 프롬프트를 구조화하는 방법, 응답을 문서에 기반하여 만드는 방법을 배웠습니다. 그러나 근본적인 한계가 있습니다: 언어 모델은 텍스트만 생성할 수 있다는 것입니다. 날씨를 확인하거나, 계산을 수행하거나, 데이터베이스에 질의하거나, 외부 시스템과 상호작용할 수 없습니다.

도구는 이러한 한계를 바꿉니다. 모델이 호출할 수 있는 기능에 접근할 수 있도록 하면, 텍스트 생성기에서 행동을 취할 수 있는 에이전트로 변신합니다. 모델은 언제 도구가 필요한지, 어떤 도구를 사용할지, 어떤 매개변수를 전달할지 결정합니다. 사용자의 코드는 함수를 실행하고 결과를 반환하며, 모델은 그 결과를 응답에 통합합니다.

## 필수 조건

- [모듈 01 - 소개](../01-introduction/README.md)를 완료함 (Azure OpenAI 리소스 배포 완료)
- 이전 모듈 완료 권장 (이 모듈은 도구와 RAG 비교에서 [모듈 03의 RAG 개념](../03-rag/README.md)을 참조함)
- 루트 디렉터리에 Azure 자격 증명이 포함된 `.env` 파일 있음 (모듈 01에서 `azd up`으로 생성)

> **참고:** 아직 모듈 01을 완료하지 않았다면, 먼저 그곳의 배포 지침을 따르세요.

## 도구가 있는 AI 에이전트 이해하기

> **📝 참고:** 이 모듈에서 "에이전트"라는 용어는 도구 호출 기능이 강화된 AI 어시스턴트를 의미합니다. 이는 [모듈 05: MCP](../05-mcp/README.md)에서 다루게 될 계획, 메모리, 다단계 추론 기능을 갖춘 자율 에이전트인 **Agentic AI** 패턴과는 다릅니다.

도구 없이 언어 모델은 훈련 데이터에서 텍스트만 생성할 수 있습니다. 현재 날씨를 물으면 추측해야 합니다. 도구를 주면 날씨 API를 호출하거나, 계산하거나, 데이터베이스를 쿼리할 수 있으며, 그 실제 결과를 응답에 반영합니다.

<img src="../../../translated_images/ko/what-are-tools.724e468fc4de64da.webp" alt="도구가 없을 때와 있을 때" width="800"/>

*도구 없이는 모델이 단지 추측만 하지만 — 도구가 있으면 API를 호출하고 계산하며 실시간 데이터를 반환할 수 있습니다.*

도구가 있는 AI 에이전트는 **추론과 행동 (ReAct)** 패턴을 따릅니다. 모델이 단순히 응답하는 것이 아니라 무엇이 필요한지 생각하고, 도구를 호출해 행동하고, 결과를 관찰한 뒤 다시 행동할지 아니면 최종 답변을 할지 결정합니다:

1. **추론** — 에이전트가 사용자의 질문을 분석하여 필요한 정보를 파악
2. **행동** — 적합한 도구를 선택하고 올바른 매개변수를 생성해 호출
3. **관찰** — 도구의 출력 결과를 받고 평가
4. **반복 또는 응답** — 추가 데이터가 필요하면 반복하고, 아니면 자연어 답변 작성

<img src="../../../translated_images/ko/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct 패턴" width="800"/>

*ReAct 사이클 — 에이전트가 무엇을 할지 추론하고, 도구를 호출해 행동하고, 결과를 관찰하며, 최종 답변을 할 때까지 반복함.*

이 과정은 자동으로 일어납니다. 사용자가 도구와 그 설명을 정의하면, 모델이 언제 어떻게 사용할지 결정합니다.

## 도구 호출 작동 방식

### 도구 정의

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

명확한 설명과 매개변수 명세가 포함된 함수들을 정의합니다. 모델은 시스템 프롬프트에서 이 설명을 보고 각 도구가 무엇을 하는지 이해합니다.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // 귀하의 날씨 조회 로직
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

아래 다이어그램은 각 애노테이션을 분해하여 AI가 언제 도구를 호출하고 어떤 인수를 전달할지 이해하는 데 어떻게 도움이 되는지 보여줍니다:

<img src="../../../translated_images/ko/tool-definitions-anatomy.f6468546037cf28b.webp" alt="도구 정의 구조" width="800"/>

*도구 정의의 구조 — @Tool은 AI에게 언제 사용할지 알려주고, @P는 각 파라미터를 설명하며, @AiService는 시작 시 모든 것을 연결합니다.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat으로 시도해 보세요:** [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java)를 열고 질문해 보세요:
> - "모의 데이터 대신 실제 OpenWeatherMap API를 어떻게 통합할 수 있나요?"
> - "AI가 도구를 올바르게 사용하도록 돕는 좋은 도구 설명은 무엇인가요?"
> - "API 오류와 호출 제한을 도구 구현에서 어떻게 처리하나요?"

### 결정 내리기

사용자가 "시애틀의 날씨는 어떤가요?"라고 묻으면, 모델은 무작위로 도구를 고르지 않습니다. 사용자 의도를 모든 도구 설명과 비교해 관련성 점수를 매기고 최적의 도구를 선택합니다. 그런 다음 올바른 매개변수를 설정한 구조화된 함수 호출을 생성합니다 — 이 경우 `location`을 `"Seattle"`로 설정합니다.

사용자 요청에 맞는 도구가 없으면 모델은 자체 지식으로 답변합니다. 여러 도구가 적합하면 가장 구체적인 도구를 선택합니다.

<img src="../../../translated_images/ko/decision-making.409cd562e5cecc49.webp" alt="AI가 도구 선택하는 방법" width="800"/>

*모델은 사용자의 의도와 모든 도구를 평가해 가장 적합한 것을 고르므로, 명확하고 구체적인 도구 설명이 중요합니다.*

### 실행

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot는 선언적인 `@AiService` 인터페이스를 모든 등록된 도구와 자동으로 연결하며, LangChain4j가 도구 호출을 자동으로 실행합니다. 내부적으로 도구 호출은 사용자의 자연어 질문부터 자연어 답변으로 돌아오기까지 여섯 단계로 흐릅니다:

<img src="../../../translated_images/ko/tool-calling-flow.8601941b0ca041e6.webp" alt="도구 호출 흐름" width="800"/>

*엔드 투 엔드 흐름 — 사용자가 질문하면 모델이 도구를 선택, LangChain4j가 실행, 모델이 결과를 자연어 응답에 녹여냄.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat으로 시도해 보세요:** [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)를 열고 질문해 보세요:
> - "ReAct 패턴은 어떻게 작동하며 AI 에이전트에 왜 효과적인가요?"
> - "에이전트는 어떤 도구를 언제 어떤 순서로 사용할지 어떻게 결정하나요?"
> - "도구 실행 실패 시 어떻게 견고하게 오류를 처리하나요?"

### 응답 생성

모델은 날씨 데이터를 받아 사용자에게 자연어 응답으로 포맷합니다.

### 아키텍처: Spring Boot 자동 연결

이 모듈은 LangChain4j의 Spring Boot 통합과 선언적인 `@AiService` 인터페이스를 사용합니다. 시작 시 Spring Boot는 `@Tool` 메서드가 포함된 모든 `@Component`, `ChatModel` 빈, `ChatMemoryProvider`를 발견하고, 모두 단일 `Assistant` 인터페이스로 연결해 보일러플레이트 코드를 없앱니다.

<img src="../../../translated_images/ko/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot 자동 연결 아키텍처" width="800"/>

*@AiService 인터페이스가 ChatModel, 도구 컴포넌트, 메모리 공급자를 묶고 — Spring Boot가 모든 연결을 자동으로 처리합니다.*

이 접근법의 주요 장점:

- **Spring Boot 자동 연결** — ChatModel과 도구가 자동 주입됨
- **@MemoryId 패턴** — 세션 기반 자동 메모리 관리
- **싱글 인스턴스** — Assistant가 한 번 생성되어 재사용되어 성능 향상
- **타입 안전 실행** — Java 메서드를 타입 변환과 함께 직접 호출
- **다중 턴 오케스트레이션** — 도구 체이닝 자동 처리
- **보일러플레이트 없음** — 수동 `AiServices.builder()` 호출 및 메모리 HashMap 불필요

수동 `AiServices.builder()` 방식은 코드가 더 많고 Spring Boot 통합 장점을 놓칩니다.

## 도구 체이닝

**도구 체이닝** — 도구 기반 에이전트의 진정한 힘은 단일 질문에 여러 도구가 필요할 때 드러납니다. "시애틀의 화씨 온도는 몇 도인가요?"라고 물으면 에이전트가 자동으로 두 도구를 연결합니다: 먼저 `getCurrentWeather`를 호출해 섭씨 온도를 가져오고, 그 값을 `celsiusToFahrenheit`에 전달해 변환하며 — 모두 한 대화 턴에서 처리합니다.

<img src="../../../translated_images/ko/tool-chaining-example.538203e73d09dd82.webp" alt="도구 체이닝 예시" width="800"/>

*도구 체이닝 작동 예 — 에이전트가 먼저 getCurrentWeather를 호출하고, 섭씨 결과를 celsiusToFahrenheit로 전달하는 방식으로 결합된 답변 전달.*

**우아한 실패 처리** — 모의 데이터에 없는 도시의 날씨를 물으면, 도구는 오류 메시지를 반환하고 AI는 충돌하지 않고 도와줄 수 없음을 설명합니다. 도구 실패는 안전하게 처리됩니다. 아래 다이어그램은 두 가지 접근 방식을 비교합니다 — 적절한 오류 처리를 하면 에이전트가 예외를 잡아 유용한 답변을 하고, 그렇지 않으면 애플리케이션 전체가 충돌합니다:

<img src="../../../translated_images/ko/error-handling-flow.9a330ffc8ee0475c.webp" alt="오류 처리 흐름" width="800"/>

*도구 실패 시 에이전트가 오류를 잡아 충돌 대신 도움 되는 설명으로 응답.*

이 모든 과정이 한 대화 턴에 일어납니다. 에이전트는 여러 도구 호출을 자율적으로 조율합니다.

## 애플리케이션 실행하기

**배포 확인:**

루트 디렉터리에 Azure 자격증명이 포함된 `.env` 파일이 있는지 확인하세요 (모듈 01에서 생성됨). 모듈 디렉터리(`04-tools/`)에서 다음을 실행합니다:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT를 표시해야 합니다
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT를 표시해야 합니다
```

**애플리케이션 시작:**

> **참고:** 이미 루트 디렉터리에서 `./start-all.sh`로 모든 애플리케이션을 시작한 경우(모듈 01 설명 참조), 이 모듈은 이미 포트 8084에서 실행 중입니다. 아래 시작 명령어를 생략하고 http://localhost:8084 로 바로 접속할 수 있습니다.

**옵션 1: Spring Boot 대시보드 사용 (VS Code 사용자 권장)**

개발 컨테이너에는 모든 Spring Boot 애플리케이션을 관리할 수 있는 시각적 인터페이스인 Spring Boot 대시보드 확장 기능이 포함되어 있습니다. VS Code 좌측 액티비티 바에서 Spring Boot 아이콘을 찾으세요.

대시보드에서 할 수 있는 일:
- 워크스페이스의 모든 Spring Boot 애플리케이션 목록 확인
- 클릭 한 번으로 애플리케이션 시작/중지
- 실시간 로그 보기
- 애플리케이션 상태 모니터링

"tools" 옆 재생 버튼을 클릭해 이 모듈을 시작하거나, 모든 모듈을 한 번에 시작할 수 있습니다.

VS Code에서의 Spring Boot 대시보드 모습:

<img src="../../../translated_images/ko/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot 대시보드" width="400"/>

*VS Code의 Spring Boot 대시보드 — 한 곳에서 모든 모듈을 시작, 중지, 모니터링*

**옵션 2: 셸 스크립트 사용**

모든 웹 애플리케이션(모듈 01-04) 시작:

**Bash:**
```bash
cd ..  # 루트 디렉토리에서부터
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
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
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

브라우저에서 http://localhost:8084 를 엽니다.

**중지 명령:**

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

이 애플리케이션은 날씨와 온도 변환 도구에 접근할 수 있는 AI 에이전트와 상호작용할 수 있는 웹 인터페이스를 제공합니다. 인터페이스 모습은 다음과 같으며, 빠른 시작 예제와 요청을 보내는 채팅 패널이 포함되어 있습니다:
<a href="images/tools-homepage.png"><img src="../../../translated_images/ko/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools 인터페이스" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI Agent Tools 인터페이스 - 도구와 상호작용하기 위한 빠른 예제와 채팅 인터페이스*

### 간단한 도구 사용해보기

간단한 요청부터 시작해보세요: "100도 화씨를 섭씨로 변환해줘". 에이전트는 온도 변환 도구가 필요하다는 것을 인식하고, 올바른 매개변수로 호출하여 결과를 반환합니다. 어떤 도구를 사용할지 또는 어떻게 호출할지 명시하지 않았는데도 자연스러운 점에 주목하세요.

### 도구 연쇄 테스트

이제 좀 더 복잡한 것을 시도해보세요: "시애틀 날씨 알려주고 화씨로 변환해줘". 에이전트가 단계별로 작업하는 모습을 지켜보세요. 먼저 (섭씨로 반환되는) 날씨 정보를 얻고, 화씨로 변환해야 함을 인식하며, 변환 도구를 호출하고 두 결과를 하나로 합쳐서 응답합니다.

### 대화 흐름 보기

채팅 인터페이스는 대화 기록을 유지하여 다중 턴 상호작용을 가능하게 합니다. 이전의 모든 쿼리와 응답을 볼 수 있어 대화를 추적하고 에이전트가 여러 교환을 거쳐 컨텍스트를 구축하는 방식을 쉽게 이해할 수 있습니다.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/ko/tools-conversation-demo.89f2ce9676080f59.webp" alt="여러 도구 호출이 포함된 대화" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*간단한 변환, 날씨 조회, 도구 연쇄를 보여주는 다중 턴 대화*

### 다양한 요청 실험해보기

다양한 조합을 시도해보세요:
- 날씨 조회: "도쿄 날씨 어때?"
- 온도 변환: "25°C가 켈빈으로 얼마야?"
- 결합 쿼리: "파리 날씨 확인하고 20°C 이상인지 알려줘"

에이전트가 자연어를 해석해 적절한 도구 호출에 매핑하는 방식을 주목하세요.

## 주요 개념

### ReAct 패턴 (추론과 실행)

에이전트는 추론(무엇을 할지 결정)과 실행(도구 사용)을 번갈아 수행합니다. 이 패턴은 지침에 단순히 응답하는 것보다 자율적인 문제 해결을 가능하게 합니다.

### 도구 설명의 중요성

도구 설명의 품질이 에이전트의 도구 사용 능력에 직접적인 영향을 미칩니다. 명확하고 구체적인 설명은 모델이 언제 어떻게 각 도구를 호출해야 하는지 이해하는 데 도움을 줍니다.

### 세션 관리

`@MemoryId` 주석은 자동 세션 기반 메모리 관리를 가능하게 합니다. 각 세션 ID는 `ChatMemoryProvider` 빈이 관리하는 독립된 `ChatMemory` 인스턴스를 갖기 때문에 여러 사용자가 서로 대화가 섞이지 않고 동시에 에이전트와 상호작용할 수 있습니다. 다음 다이어그램은 여러 사용자가 세션 ID를 기준으로 고립된 메모리 저장소로 라우팅되는 방식을 보여줍니다:

<img src="../../../translated_images/ko/session-management.91ad819c6c89c400.webp" alt="@MemoryId를 사용한 세션 관리" width="800"/>

*각 세션 ID는 독립된 대화 내역에 매핑됩니다 — 사용자는 서로의 메시지를 절대 볼 수 없습니다.*

### 오류 처리

도구는 실패할 수 있습니다 — API가 시간 초과되거나, 매개변수가 잘못되었거나, 외부 서비스가 중단될 수 있습니다. 실제 환경에서는 모델이 문제를 설명하거나 대안을 시도할 수 있도록 오류 처리가 필요하며, 전체 애플리케이션이 중단되지 않도록 해야 합니다. 도구에서 예외가 발생하면 LangChain4j가 이를 포착해 오류 메시지를 모델에 전달하며, 모델은 자연어로 문제를 설명할 수 있습니다.

## 사용 가능한 도구

아래 다이어그램은 구축할 수 있는 광범위한 도구 생태계를 보여줍니다. 이 모듈은 날씨와 온도 도구를 시연하지만, 동일한 `@Tool` 패턴은 데이터베이스 쿼리부터 결제 처리까지 모든 Java 메서드에 적용됩니다.

<img src="../../../translated_images/ko/tool-ecosystem.aad3d74eaa14a44f.webp" alt="도구 생태계" width="800"/>

*@Tool로 주석 처리된 모든 Java 메서드는 AI가 사용할 수 있도록 제공됩니다 — 이 패턴은 데이터베이스, API, 이메일, 파일 작업 등으로 확장됩니다.*

## 도구 기반 에이전트 사용 시기

모든 요청에 도구가 필요한 것은 아닙니다. 결정은 AI가 외부 시스템과 상호작용해야 하는지 또는 자체 지식만으로 답할 수 있는지에 달려 있습니다. 다음 가이드는 도구가 가치를 더하는 경우와 불필요한 경우를 요약합니다:

<img src="../../../translated_images/ko/when-to-use-tools.51d1592d9cbdae9c.webp" alt="도구 사용 시기" width="800"/>

*간단한 결정 가이드 — 도구는 실시간 데이터, 계산, 작업용; 일반 지식 및 창의적 작업에는 불필요함.*

## 도구 vs RAG

모듈 03과 04는 AI가 할 수 있는 일을 확장하지만, 근본적으로 다른 방식입니다. RAG는 문서를 검색해 모델에 **지식** 접근 권한을 부여합니다. 도구는 함수를 호출해 모델이 **행동**할 수 있도록 합니다. 아래 다이어그램은 두 접근 방식을 나란히 비교하며 각 워크플로우의 작동 방식과 장단점 차이를 설명합니다:

<img src="../../../translated_images/ko/tools-vs-rag.ad55ce10d7e4da87.webp" alt="도구 vs RAG 비교" width="800"/>

*RAG는 정적인 문서에서 정보를 검색하고 — 도구는 동적인 실시간 데이터를 가져오거나 작업을 실행합니다. 많은 실제 시스템은 두 방식을 모두 결합합니다.*

실제로 많은 생산 시스템은 두 방식을 모두 사용합니다: 문서 기반 답변 근거용으로 RAG, 라이브 데이터 조회나 작업 수행용으로 도구.

## 다음 단계

**다음 모듈:** [05-mcp - 모델 컨텍스트 프로토콜 (MCP)](../05-mcp/README.md)

---

**탐색:** [← 이전: 모듈 03 - RAG](../03-rag/README.md) | [메인으로 돌아가기](../README.md) | [다음: 모듈 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 기하기 위해 노력하고 있으나, 자동 번역에는 오류나 부정확성이 포함될 수 있음을 유의해 주시기 바랍니다. 원본 문서가 권위 있는 출처로 간주되어야 합니다. 중요한 정보에 대해서는 전문 인간 번역을 권장합니다. 본 번역의 사용으로 인해 발생하는 모든 오해나 해석상의 문제에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->