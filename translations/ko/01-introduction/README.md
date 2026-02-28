# Module 01: LangChain4j 시작하기

## 목차

- [비디오 안내](../../../01-introduction)
- [학습 내용](../../../01-introduction)
- [필수 조건](../../../01-introduction)
- [핵심 문제 이해하기](../../../01-introduction)
- [토큰 이해하기](../../../01-introduction)
- [메모리 작동 방식](../../../01-introduction)
- [LangChain4j 사용 방법](../../../01-introduction)
- [Azure OpenAI 인프라 배포](../../../01-introduction)
- [로컬에서 애플리케이션 실행](../../../01-introduction)
- [애플리케이션 사용법](../../../01-introduction)
  - [무상태 채팅 (왼쪽 패널)](../../../01-introduction)
  - [상태 저장 채팅 (오른쪽 패널)](../../../01-introduction)
- [다음 단계](../../../01-introduction)

## 비디오 안내

이 모듈 시작 방법을 설명하는 라이브 세션을 시청하세요:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## 학습 내용

빠른 시작을 완료했다면 프롬프트를 보내고 응답을 받는 방법을 보셨습니다. 이것이 기본이지만, 실제 애플리케이션은 그 이상을 필요로 합니다. 이 모듈에서는 컨텍스트를 기억하고 상태를 유지하는 대화형 AI를 구축하는 방법을 배웁니다 — 이 차이가 단발성 데모와 프로덕션 준비 애플리케이션의 차이입니다.

이 가이드 전체에서 Azure OpenAI의 GPT-5.2를 사용할 것입니다. 고급 추론 기능으로 인해 다양한 패턴의 동작이 더 명확하게 드러나기 때문입니다. 메모리를 추가하면 그 차이를 분명히 볼 수 있습니다. 이를 통해 각 구성 요소가 애플리케이션에 가져다주는 것을 더 쉽게 이해할 수 있습니다.

두 가지 패턴을 모두 보여주는 애플리케이션을 하나 만들 것입니다:

**무상태 채팅** - 각 요청은 독립적입니다. 모델은 이전 메시지를 기억하지 않습니다. 빠른 시작에서 사용한 패턴입니다.

**상태 저장 대화** - 각 요청에 대화 기록이 포함됩니다. 모델은 여러 턴에 걸쳐 컨텍스트를 유지합니다. 프로덕션 애플리케이션이 요구하는 패턴입니다.

## 필수 조건

- Azure OpenAI 접근 권한이 있는 Azure 구독
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **참고:** 제공된 개발 컨테이너에는 Java, Maven, Azure CLI 및 Azure Developer CLI(azd)가 사전 설치되어 있습니다.

> **참고:** 이 모듈은 Azure OpenAI에서 GPT-5.2를 사용합니다. 배포는 `azd up`으로 자동 구성되며, 코드 내 모델 이름을 변경하지 마십시오.

## 핵심 문제 이해하기

언어 모델은 무상태입니다. 각 API 호출은 독립적입니다. "내 이름은 존입니다"라고 보내고 나서 "내 이름이 뭐야?"라고 물으면 모델은 방금 자신을 소개했다는 사실을 모릅니다. 매 요청이 처음 대화인 것처럼 처리됩니다.

이는 단순 Q&A에는 괜찮지만 실제 애플리케이션에는 쓸모가 없습니다. 고객 서비스 봇은 사용자가 말한 내용을 기억해야 합니다. 개인 비서는 컨텍스트가 필요합니다. 다중 턴 대화에는 메모리가 필수적입니다.

<img src="../../../translated_images/ko/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="무상태 대화 vs 상태 저장 대화" width="800"/>

*무상태(독립 호출)와 상태 저장(컨텍스트 인식) 대화의 차이*

## 토큰 이해하기

대화로 들어가기 전에 토큰을 이해하는 것이 중요합니다 — 언어 모델이 처리하는 텍스트의 기본 단위입니다:

<img src="../../../translated_images/ko/token-explanation.c39760d8ec650181.webp" alt="토큰 설명" width="800"/>

*텍스트가 어떻게 토큰으로 분할되는지 예시 - "I love AI!"는 4개의 별도 처리 단위로 나뉨*

토큰은 AI 모델이 텍스트를 측정하고 처리하는 단위입니다. 단어, 구두점, 심지어 공백도 토큰이 될 수 있습니다. 모델은 한 번에 처리할 수 있는 토큰 수 제한이 있습니다 (GPT-5.2는 최대 400,000 토큰, 입력은 최대 272,000 토큰, 출력은 최대 128,000 토큰). 토큰을 이해하면 대화 길이와 비용을 관리하는 데 도움이 됩니다.

## 메모리 작동 방식

챗 메모리는 무상태 문제를 해결하기 위해 대화 기록을 유지합니다. 요청을 모델에 보내기 전에 프레임워크가 관련 이전 메시지를 붙입니다. "내 이름이 뭐야?"라고 묻는다면 시스템은 실제로 전체 대화 기록을 보내서 모델이 "내 이름은 존입니다"라고 했던 사실을 볼 수 있도록 합니다.

LangChain4j는 이 과정을 자동으로 처리하는 메모리 구현체를 제공합니다. 유지할 메시지 수를 선택하면 프레임워크가 컨텍스트 윈도우를 관리합니다.

<img src="../../../translated_images/ko/memory-window.bbe67f597eadabb3.webp" alt="메모리 윈도우 개념" width="800"/>

*MessageWindowChatMemory는 최근 메시지의 슬라이딩 윈도우를 유지하며 오래된 메시지는 자동으로 버립니다*

## LangChain4j 사용 방법

이 모듈은 빠른 시작을 확장하여 Spring Boot를 통합하고 대화 메모리를 추가합니다. 구성 요소들이 어떻게 연결되는지 설명합니다:

**종속성** - 두 개의 LangChain4j 라이브러리를 추가하세요:

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

**챗 모델** - Azure OpenAI를 Spring 빈으로 구성 ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

빌더는 `azd up`이 설정한 환경 변수를 통해 자격 증명을 읽습니다. `baseUrl`을 Azure 엔드포인트로 설정하면 OpenAI 클라이언트가 Azure OpenAI와 연동됩니다.

**대화 메모리** - MessageWindowChatMemory로 채팅 기록 추적 ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

`withMaxMessages(10)`으로 메모리를 생성해 최근 10개의 메시지를 유지합니다. 사용자 및 AI 메시지는 `UserMessage.from(text)` 및 `AiMessage.from(text)` 같은 타입 래퍼로 추가합니다. `memory.messages()`로 기록을 가져와 모델에 보냅니다. 서비스는 각 대화 ID별로 별도의 메모리 인스턴스를 저장해 여러 사용자가 동시에 채팅할 수 있게 합니다.

> **🤖 GitHub Copilot과 함께 시도하기:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)를 열고 다음을 물어보세요:
> - "MessageWindowChatMemory가 윈도우가 가득 찼을 때 어떤 메시지를 떨어뜨릴지 어떻게 결정하나요?"
> - "메모리 저장을 메모리 내 저장소 대신 데이터베이스로 커스텀 구현할 수 있나요?"
> - "예전 대화 기록을 요약해 압축하는 기능은 어떻게 추가하나요?"

무상태 채팅 엔드포인트는 메모리를 건너뛰고 빠른 시작과 같이 그냥 `chatModel.chat(prompt)`를 호출합니다. 상태 저장 엔드포인트는 메시지를 메모리에 추가하고 기록을 가져와 매 요청마다 그 컨텍스트를 포함합니다. 동일한 모델 구성이지만 다른 패턴입니다.

## Azure OpenAI 인프라 배포

**Bash:**
```bash
cd 01-introduction
azd up  # 구독 및 위치 선택 (eastus2 권장)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # 구독 및 위치 선택 (eastus2 추천)
```

> **참고:** 타임아웃 오류(`RequestConflict: Cannot modify resource ... provisioning state is not terminal`)가 발생하면 `azd up`을 다시 실행하세요. Azure 리소스가 백그라운드에서 아직 프로비저닝 중일 수 있으며, 다시 시도하면 리소스가 종료 상태에 도달했을 때 배포가 완료됩니다.

이 명령은:
1. GPT-5.2 및 text-embedding-3-small 모델이 포함된 Azure OpenAI 리소스를 배포합니다.
2. 프로젝트 루트에 자격 증명이 담긴 `.env` 파일을 자동 생성합니다.
3. 필요한 모든 환경 변수를 설정합니다.

**배포에 문제가 있나요?** [인프라 README](infra/README.md)에서 서브도메인 이름 충돌, 수동 Azure 포털 배포 단계, 모델 구성 지침 등의 자세한 문제 해결 방법을 확인하세요.

**배포 성공 여부 확인:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY 등을 보여야 합니다.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY 등을 보여야 합니다.
```

> **참고:** `azd up` 명령은 `.env` 파일을 자동으로 생성합니다. 나중에 업데이트가 필요하면 `.env` 파일을 직접 편집하거나 다음 명령으로 재생성할 수 있습니다:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## 로컬에서 애플리케이션 실행

**배포 확인:**

루트 디렉터리에 Azure 자격 증명이 포함된 `.env` 파일이 존재하는지 확인하세요:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT를 보여줘야 합니다
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT를 표시해야 합니다
```

**애플리케이션 시작하기:**

**옵션 1: Spring Boot 대시보드 사용 (VS Code 사용자에게 권장)**

개발 컨테이너에는 Spring Boot 대시보드 확장 기능이 포함되어 있어 모든 Spring Boot 애플리케이션 관리를 시각적으로 할 수 있습니다. VS Code 왼쪽의 활동 표시줄에서 Spring Boot 아이콘을 찾으세요.

Spring Boot 대시보드에서 할 수 있는 것:
- 작업공간의 모든 Spring Boot 애플리케이션 보기
- 클릭 한 번으로 애플리케이션 시작/중지
- 실시간 애플리케이션 로그 확인
- 애플리케이션 상태 모니터링

"introduction" 옆 플레이 버튼을 클릭해 이 모듈을 시작하거나 모든 모듈을 동시에 시작할 수 있습니다.

<img src="../../../translated_images/ko/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot 대시보드" width="400"/>

**옵션 2: 셸 스크립트 사용**

모든 웹 애플리케이션(01-04 모듈) 시작:

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
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
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

브라우저에서 http://localhost:8080 을 열어 접속하세요.

**애플리케이션 종료:**

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

애플리케이션은 두 개의 채팅 구현을 나란히 보여주는 웹 인터페이스를 제공합니다.

<img src="../../../translated_images/ko/home-screen.121a03206ab910c0.webp" alt="애플리케이션 홈 화면" width="800"/>

*간단 채팅 (무상태)와 대화형 채팅 (상태 저장) 옵션 모두를 보여주는 대시보드*

### 무상태 채팅 (왼쪽 패널)

먼저 이것을 시도해보세요. "내 이름은 존입니다"라고 하고 바로 "내 이름이 뭐야?"라고 물어보세요. 각 메시지가 독립적이기 때문에 모델은 기억하지 못합니다. 이는 기본 언어 모델 통합의 핵심 문제 — 대화 컨텍스트 부재 — 를 보여줍니다.

<img src="../../../translated_images/ko/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="무상태 채팅 데모" width="800"/>

*AI가 이전 메시지에서 당신 이름을 기억하지 못함*

### 상태 저장 채팅 (오른쪽 패널)

이제 같은 시퀀스를 여기에서 시도해보세요. "내 이름은 존입니다"라고 하고 "내 이름이 뭐야?"라고 물으면 기억합니다. 차이는 MessageWindowChatMemory 덕분입니다 — 대화 기록을 유지하며 각 요청과 함께 포함합니다. 이것이 프로덕션 대화형 AI가 작동하는 방식입니다.

<img src="../../../translated_images/ko/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="상태 저장 채팅 데모" width="800"/>

*AI가 이전 대화에서 당신 이름을 기억함*

두 패널 모두 같은 GPT-5.2 모델을 사용합니다. 차이는 메모리뿐입니다. 이를 통해 메모리가 애플리케이션에 어떤 가치를 주는지, 왜 실제 사용 사례에 필수적인지 명확하게 알 수 있습니다.

## 다음 단계

**다음 모듈:** [02-prompt-engineering - GPT-5.2를 활용한 프롬프트 엔지니어링](../02-prompt-engineering/README.md)

---

**네비게이션:** [← 이전: Module 00 - 빠른 시작](../00-quick-start/README.md) | [메인으로 돌아가기](../README.md) | [다음: Module 02 - 프롬프트 엔지니어링 →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 최선을 다했지만, 자동 번역에는 오류나 부정확한 부분이 있을 수 있음을 양지해 주시기 바랍니다. 원본 문서는 해당 언어의 원문을 권위 있는 자료로 간주해야 합니다. 중요한 정보의 경우 전문적인 인간 번역을 권장합니다. 본 번역의 사용으로 인한 오해나 잘못된 해석에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->