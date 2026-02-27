# 모듈 04: 도구가 포함된 AI 에이전트

## 목차

- [학습 내용](../../../04-tools)
- [사전 준비 사항](../../../04-tools)
- [도구가 포함된 AI 에이전트 이해하기](../../../04-tools)
- [도구 호출 방식](../../../04-tools)
  - [도구 정의](../../../04-tools)
  - [의사결정](../../../04-tools)
  - [실행](../../../04-tools)
  - [응답 생성](../../../04-tools)
  - [아키텍처: Spring Boot 자동 연결](../../../04-tools)
- [도구 체이닝](../../../04-tools)
- [애플리케이션 실행](../../../04-tools)
- [애플리케이션 사용법](../../../04-tools)
  - [간단한 도구 사용해보기](../../../04-tools)
  - [도구 체이닝 테스트하기](../../../04-tools)
  - [대화 흐름 보기](../../../04-tools)
  - [다양한 요청 실험하기](../../../04-tools)
- [핵심 개념](../../../04-tools)
  - [ReAct 패턴 (추론 및 행동)](../../../04-tools)
  - [도구 설명의 중요성](../../../04-tools)
  - [세션 관리](../../../04-tools)
  - [오류 처리](../../../04-tools)
- [사용 가능한 도구](../../../04-tools)
- [도구 기반 에이전트 사용 시점](../../../04-tools)
- [도구와 RAG 비교](../../../04-tools)
- [다음 단계](../../../04-tools)

## 학습 내용

지금까지 AI와 대화하는 방법, 효과적인 프롬프트 구조 작성, 문서에 근거한 응답 방법을 배웠습니다. 하지만 근본적인 한계가 있습니다: 언어 모델은 텍스트만 생성할 수 있습니다. 날씨를 확인하거나, 계산을 수행하거나, 데이터베이스를 조회하거나, 외부 시스템과 상호작용할 수 없습니다.

도구는 이를 변화시킵니다. 모델에 호출할 수 있는 기능을 제공해 텍스트 생성기에서 행동을 수행할 수 있는 에이전트로 바꾸는 것입니다. 모델은 언제 도구가 필요한지, 어떤 도구를 사용할지, 어떤 매개변수를 전달할지 결정합니다. 코드는 해당 기능을 실행하고 결과를 반환합니다. 모델은 그 결과를 응답에 반영합니다.

## 사전 준비 사항

- 모듈 01 완료 (Azure OpenAI 리소스 배포)
- 루트 디렉터리에 Azure 자격 증명이 포함된 `.env` 파일 (모듈 01에서 `azd up` 실행 시 생성됨)

> **참고:** 모듈 01을 완료하지 않았다면 먼저 그 배포 지침을 따르세요.

## 도구가 포함된 AI 에이전트 이해하기

> **📝 참고:** 본 모듈에서 "에이전트"는 도구 호출 기능이 강화된 AI 어시스턴트를 의미합니다. 이는 [모듈 05: MCP](../05-mcp/README.md)에서 다룰 계획인 **Agentic AI** 패턴(기획, 기억, 다단계 추론을 갖춘 자율 에이전트)과 다릅니다.

도구가 없으면 언어 모델은 훈련 데이터에서 텍스트를 생성할 수밖에 없습니다. 현재 날씨를 묻는다면 단순히 추측합니다. 도구가 있다면 날씨 API를 호출하거나, 계산을 실행하거나, 데이터베이스를 조회하고, 그런 실제 결과를 응답에 녹여낼 수 있습니다.

<img src="../../../translated_images/ko/what-are-tools.724e468fc4de64da.webp" alt="도구 없음 vs 도구 있음" width="800"/>

*도구 없이는 모델이 추측만 할 수 있지만, 도구가 있으면 API 호출, 계산 수행, 실시간 데이터 반환이 가능합니다.*

도구가 포함된 AI 에이전트는 **추론 및 행동(ReAct)** 패턴을 따릅니다. 모델은 단순 응답을 넘어서, 무엇이 필요한지 생각하고, 도구를 호출하며, 결과를 관찰하고, 다시 행동하거나 최종 답변을 전달합니다:

1. **추론** — 에이전트가 사용자의 질문을 분석하고 필요한 정보를 판단
2. **행동** — 에이전트가 적합한 도구를 선택하고, 적절한 매개변수를 생성하여 호출
3. **관찰** — 에이전트가 도구의 출력을 받고 결과를 평가
4. **반복 또는 응답** — 추가 데이터가 필요하면 반복, 아니면 자연어 답변 작성

<img src="../../../translated_images/ko/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct 패턴" width="800"/>

*ReAct 사이클 — 에이전트가 할 일을 추론하고 도구를 호출하며 결과를 관찰하고, 최종 답변이 나올 때까지 반복.*

이 과정은 자동으로 수행됩니다. 도구와 설명을 정의하면, 모델이 사용 시기와 방법을 결정합니다.

## 도구 호출 방식

### 도구 정의

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

명확한 설명과 매개변수 사양으로 함수를 정의합니다. 모델은 시스템 프롬프트에서 이 설명을 보고 각 도구가 하는 일을 이해합니다.

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
// - @Component 클래스에서의 모든 @Tool 메서드
// - 세션 관리를 위한 ChatMemoryProvider
```

아래 다이어그램은 각 주석을 분해해 AI가 언제 도구를 호출하고 어떤 인수를 넘겨야 하는지 이해하는 방법을 설명합니다:

<img src="../../../translated_images/ko/tool-definitions-anatomy.f6468546037cf28b.webp" alt="도구 정의 구조" width="800"/>

*도구 정의 구조 — @Tool이 AI에게 사용 시기를 알리고, @P는 매개변수를 설명하며, @AiService는 시작 시 모두 연결합니다.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat으로 시도:** [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java)를 열고 질문해 보세요:
> - "실제 OpenWeatherMap 같은 날씨 API를 모의 데이터 대신 통합하려면 어떻게 하나요?"
> - "AI가 도구를 올바르게 사용하도록 돕는 좋은 도구 설명은 무엇인가요?"
> - "도구 구현에서 API 오류와 호출 제한은 어떻게 처리하나요?"

### 의사결정

사용자가 "시애틀 날씨가 어때?"라고 묻는 경우, 모델은 무작위로 도구를 선택하지 않습니다. 사용자 의도와 모든 도구 설명을 비교해 적합도를 평가하고, 가장 알맞은 도구를 선택합니다. 그런 다음 올바른 매개변수로 구조화된 함수 호출을 생성합니다 — 여기서는 `location`을 `"Seattle"`로 설정합니다.

도구가 요청에 맞지 않으면 모델은 자체 지식으로 답변합니다. 여러 도구가 적합하면 가장 구체적인 도구를 선택합니다.

<img src="../../../translated_images/ko/decision-making.409cd562e5cecc49.webp" alt="AI의 도구 선택 방법" width="800"/>

*모델은 사용자의 의도에 맞게 모든 도구를 평가하고 최적의 도구를 선택합니다 — 그렇기에 명확하고 구체적인 도구 설명이 중요합니다.*

### 실행

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot가 선언적 `@AiService` 인터페이스에 등록된 모든 도구를 자동 주입하고, LangChain4j가 도구 호출을 자동 실행합니다. 내부적으로 완전한 도구 호출은 여섯 단계로 흐르며 — 사용자 자연어 질문에서 자연어 답변까지 이어집니다:

<img src="../../../translated_images/ko/tool-calling-flow.8601941b0ca041e6.webp" alt="도구 호출 흐름" width="800"/>

*종단 간 흐름 — 사용자가 질문, 모델이 도구 선택, LangChain4j가 실행, 모델이 결과를 응답에 반영.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat으로 시도:** [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)를 열고 질문해 보세요:
> - "ReAct 패턴은 어떻게 작동하며 왜 AI 에이전트에 효과적인가요?"
> - "에이전트는 어떤 도구를 어떤 순서로 사용할지 어떻게 결정하나요?"
> - "도구 실행이 실패하면 어떻게 되며, 어떻게 견고하게 오류를 처리해야 하나요?"

### 응답 생성

모델은 날씨 데이터를 수신하여 사용자에게 자연어 형식으로 응답합니다.

### 아키텍처: Spring Boot 자동 연결

이 모듈은 LangChain4j의 Spring Boot 통합을 사용하여 선언적 `@AiService` 인터페이스를 활용합니다. 시작 시 Spring Boot가 `@Tool` 메서드를 포함한 모든 `@Component`와 `ChatModel` 빈, `ChatMemoryProvider`를 발견하고, 모든 것을 단일 `Assistant` 인터페이스로 연결해 반복 코드 없이 완성합니다.

<img src="../../../translated_images/ko/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot 자동 연결 아키텍처" width="800"/>

*@AiService 인터페이스는 ChatModel, 도구 컴포넌트, 메모리 공급자를 연결하며 — Spring Boot가 모든 연결을 자동 처리합니다.*

이 접근법의 주요 장점:

- **Spring Boot 자동 주입** — ChatModel과 도구가 자동 주입됨
- **@MemoryId 패턴** — 자동 세션 기반 메모리 관리
- **단일 인스턴스** — Assistant를 한 번 생성해 재사용으로 성능 향상
- **타입 안전 실행** — Java 메서드를 직접 호출하며 타입 변환 포함
- **다중 턴 조율** — 도구 체이닝 자동 처리
- **반복 코드 제로** — 수동 `AiServices.builder()` 호출이나 메모리 HashMap 불필요

수동 `AiServices.builder()` 방식은 코드가 더 많고 Spring Boot 통합 이점을 활용하지 못합니다.

## 도구 체이닝

**도구 체이닝** — 도구 기반 에이전트의 진정한 힘은 하나의 질문에 여러 도구가 필요할 때 빛납니다. "시애틀 날씨가 화씨로는 어떻죠?"라고 묻는다면, 에이전트가 자동으로 두 도구를 체인으로 연결합니다: 먼저 `getCurrentWeather`를 호출해 섭씨 온도를 얻고, 그 값을 `celsiusToFahrenheit`에 넘겨 변환합니다 — 이 모든 것을 한 대화 턴에서 수행합니다.

<img src="../../../translated_images/ko/tool-chaining-example.538203e73d09dd82.webp" alt="도구 체이닝 예시" width="800"/>

*도구 체이닝 실행 예 — 에이전트가 먼저 getCurrentWeather를 호출, 이어서 섭씨 결과를 celsiusToFahrenheit로 전달, 통합된 답변을 제공.*

실제 애플리케이션에서는 이렇게 진행됩니다 — 에이전트가 한 대화 턴에서 두 도구를 체이닝 호출함:

<a href="images/tool-chaining.png"><img src="../../../translated_images/ko/tool-chaining.3b25af01967d6f7b.webp" alt="도구 체이닝" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*실제 애플리케이션 출력 — 에이전트가 자동으로 getCurrentWeather → celsiusToFahrenheit를 한 턴에 체인 호출.*

**우아한 실패 처리** — 모의 데이터에 없는 도시의 날씨를 요청하면 도구가 오류 메시지를 반환하고 AI는 중단 대신 도움을 줄 수 없음을 설명합니다. 도구는 안전하게 실패합니다.

<img src="../../../translated_images/ko/error-handling-flow.9a330ffc8ee0475c.webp" alt="오류 처리 흐름" width="800"/>

*도구 실패 시 에이전트가 오류를 포착해 중단하지 않고 설명을 포함해 응답.*

이 모든 동작은 한 대화 턴에서 이루어지며, 에이전트가 다중 도구 호출을 자율적으로 조율합니다.

## 애플리케이션 실행

**배포 확인:**

루트 디렉터리에 Azure 자격 증명이 포함된 `.env` 파일이 존재하는지 확인하세요 (모듈 01에서 생성됨):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT를 보여줘야 합니다
```

**애플리케이션 시작:**

> **참고:** 모듈 01에서 `./start-all.sh`로 모든 애플리케이션을 이미 시작했다면, 이 모듈은 포트 8084에서 이미 실행 중입니다. 아래 시작 명령은 건너뛰고 http://localhost:8084 로 바로 접속해도 됩니다.

**옵션 1: Spring Boot 대시보드 사용 (VS Code 사용자 권장)**

개발 컨테이너에는 모든 Spring Boot 애플리케이션 관리를 돕는 Spring Boot 대시보드 확장이 포함되어 있습니다. VS Code 좌측 Activity Bar에서 Spring Boot 아이콘을 찾아 실행할 수 있습니다.

Spring Boot 대시보드에서 할 수 있는 일:
- 작업 공간 내 모든 Spring Boot 애플리케이션 목록 확인
- 클릭 한 번으로 애플리케이션 시작/중지
- 실시간 애플리케이션 로그 보기
- 애플리케이션 상태 모니터링

"tools" 옆 재생 버튼을 클릭해 모듈을 시작하거나, 모든 모듈을 한 번에 시작할 수 있습니다.

<img src="../../../translated_images/ko/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot 대시보드" width="400"/>

**옵션 2: 쉘 스크립트 사용**

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
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

두 스크립트 모두 루트 `.env` 파일에서 환경변수를 자동 로드하고, JAR 파일이 없으면 빌드합니다.

> **참고:** 시작 전에 모든 모듈을 수동으로 빌드하려면:
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

브라우저에서 http://localhost:8084 에 접속하세요.

**중지 방법:**

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

## 애플리케이션 사용법

이 애플리케이션은 날씨 및 온도 변환 도구에 접근 가능한 AI 에이전트와 상호작용할 수 있는 웹 인터페이스를 제공합니다.

<a href="images/tools-homepage.png"><img src="../../../translated_images/ko/tools-homepage.4b4cd8b2717f9621.webp" alt="AI 에이전트 도구 인터페이스" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI 에이전트 도구 인터페이스 — 도구와 상호작용하는 빠른 예시 및 채팅 인터페이스*

### 간단한 도구 사용해보기
간단한 요청부터 시작해 보세요: "100도 화씨를 섭씨로 변환해 줘". 에이전트는 온도 변환 도구가 필요하다는 것을 인식하고, 적절한 매개변수와 함께 호출한 뒤 결과를 반환합니다. 이 과정이 매우 자연스럽게 느껴지는 점에 주목하세요 - 어떤 도구를 사용할지, 어떻게 호출할지 명시하지 않았습니다.

### 도구 연쇄 테스트

이제 좀 더 복잡한 요청을 시도해 보세요: "시애틀의 날씨를 알려주고 화씨로 변환해 줘". 에이전트가 단계별로 작업하는 모습을 관찰하세요. 먼저 날씨를 받아오고(섭씨 단위), 화씨로 변환해야 한다는 것을 인식해 변환 도구를 호출한 뒤 두 결과를 하나의 응답으로 결합합니다.

### 대화 흐름 보기

채팅 인터페이스는 대화 이력을 유지하여 다중 턴 상호작용이 가능합니다. 이전 쿼리와 응답을 모두 볼 수 있어 대화를 추적하고 에이전트가 여러 교환을 거치면서 어떻게 문맥을 구축하는지 이해하기 쉽습니다.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/ko/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*단순 변환, 날씨 조회, 도구 연쇄를 보여주는 다중 턴 대화*

### 다양한 요청 실험

다양한 조합을 시도해 보세요:
- 날씨 조회: "도쿄 날씨 어때?"
- 온도 변환: "25°C는 켈빈으로 얼마야?"
- 복합 쿼리: "파리 날씨 확인하고 20°C 이상인지 알려줘"

에이전트가 자연어를 해석해 적절한 도구 호출로 매핑하는 방식을 확인할 수 있습니다.

## 핵심 개념

### ReAct 패턴 (추론과 실행)

에이전트는 추론(무엇을 할지 결정)과 실행(도구 사용)을 번갈아 수행합니다. 이 패턴은 단순 명령 응답을 넘어 자율 문제 해결을 가능하게 합니다.

### 도구 설명의 중요성

도구 설명의 품질은 에이전트가 도구를 얼마나 잘 사용하는지에 직접적인 영향을 미칩니다. 명확하고 구체적인 설명은 모델이 언제, 어떻게 도구를 호출해야 하는지 이해하도록 돕습니다.

### 세션 관리

`@MemoryId` 주석은 자동 세션 기반 메모리 관리를 가능하게 합니다. 각 세션 ID는 `ChatMemoryProvider` 빈이 관리하는 독립된 `ChatMemory` 인스턴스와 연결되어, 여러 사용자가 대화를 서로 섞지 않고 동시에 에이전트와 상호작용할 수 있습니다.

<img src="../../../translated_images/ko/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*각 세션 ID는 분리된 대화 이력을 매핑합니다 — 사용자는 서로의 메시지를 볼 수 없습니다.*

### 오류 처리

도구는 실패할 수 있습니다 — API 시간 초과, 잘못된 매개변수, 외부 서비스 장애 등. 실제 운영 환경에서는 모델이 문제를 설명하거나 대안을 시도하게 하도록 오류 처리가 필수적입니다. 도구가 예외를 던지면 LangChain4j는 이를 포착해 오류 메시지를 모델에 전달하며, 모델은 자연스럽게 문제를 설명할 수 있습니다.

## 사용 가능한 도구

아래 다이어그램은 구축할 수 있는 도구들의 광범위한 생태계를 보여줍니다. 이 모듈은 날씨 및 온도 도구를 시연하지만 동일한 `@Tool` 패턴은 데이터베이스 쿼리부터 결제 처리까지 모든 Java 메서드에 적용됩니다.

<img src="../../../translated_images/ko/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*@Tool 주석이 붙은 Java 메서드는 AI가 사용할 수 있게 됩니다 — 이 패턴은 데이터베이스, API, 이메일, 파일 작업 등으로 확장됩니다.*

## 도구 기반 에이전트를 사용해야 할 때

<img src="../../../translated_images/ko/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*빠른 결정 가이드 — 도구는 실시간 데이터, 계산, 작업용; 일반 지식 및 창의적 작업에는 불필요.*

**도구를 사용해야 할 때:**
- 실시간 데이터(날씨, 주식 가격, 재고 등) 응답이 필요할 때
- 간단한 수학을 넘는 계산이 필요할 때
- 데이터베이스 또는 API 접근 시
- 작업 수행 시(이메일 전송, 티켓 생성, 기록 업데이트)
- 여러 데이터 소스를 결합할 때

**도구를 사용하지 말아야 할 때:**
- 일반 지식으로 질문을 해결할 수 있을 때
- 응답이 순수 대화 목적일 때
- 도구 지연으로 인해 경험이 지나치게 느려질 때

## 도구와 RAG 비교

모듈 03과 04는 AI가 할 수 있는 일을 확장하지만 근본적으로 다릅니다. RAG는 문서 검색을 통한 **지식** 접근을 제공하고, 도구는 함수 호출을 통한 **행동** 수행 능력을 제공합니다.

<img src="../../../translated_images/ko/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG는 정적 문서에서 정보를 찾아내고 — 도구는 동적, 실시간 데이터를 가져오거나 작업을 실행합니다. 많은 실제 시스템은 두 방식을 모두 결합합니다.*

실제로 많은 운영 시스템은 RAG를 통해 문서 기반 근거를 제공하고, 도구를 통해 실시간 데이터 검색이나 작업 수행을 병행합니다.

## 다음 단계

**다음 모듈:** [05-mcp - 모델 컨텍스트 프로토콜 (MCP)](../05-mcp/README.md)

---

**탐색:** [← 이전: 모듈 03 - RAG](../03-rag/README.md) | [메인으로 돌아가기](../README.md) | [다음: 모듈 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 저희는 정확성을 위해 노력하고 있으나, 자동 번역에는 오류나 부정확성이 있을 수 있음을 알려드립니다. 원본 문서의 원어 버전을 권위 있는 자료로 간주하시기 바랍니다. 중요한 정보의 경우 전문가의 인간 번역을 권장합니다. 본 번역 사용으로 인한 오해나 잘못된 해석에 대해 당사는 책임지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->